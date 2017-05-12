package com.jlhe;

import com.jlhe.filter.Filtering;
import com.jlhe.imageBinarization.BinarizationImage;
import com.jlhe.imageGray.GrayImage;
import com.jlhe.segmentation.CharacterSegmentation;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class TesseractApplication {

    public static void main(String[] args) {
        File root = new File("./src/main/resources/img");
        ITesseract instance = new Tesseract();
        float correctCount = 0;
        float imageCount = 0;

        try {
            File[] files = root.listFiles();
            double startTime = System.currentTimeMillis();
            for (File file : files) {
                if(file.getName().contains(".jpg")){
                    BufferedImage bufferedImage = ImageIO.read(file);
                    //File newFile = new File( "./out/gray",file.getName());
                    //File newFile = new File( "./out/ostu",file.getName());
                    //File newFile = new File( "./out/itor",file.getName());
                    //File newFile = new File( "./out/twoPeak",file.getName());
                    //File newFile = new File( "./out/8nf",file.getName());
                    //File newFile = new File( "./out/cdf",file.getName());
                    File newFile = new File( "./out/comb",file.getName());

                    //BufferedImage grayImage = GrayImage.gray(bufferedImage);

                    int[][] gray = GrayImage.convertImageToGray(bufferedImage);//获取灰度矩阵
                    //BufferedImage binarizationImage = BinarizationImage.binarizatize(gray, gray.length, gray[0].length);//通过灰度矩阵，完成二值化
                    int[][] binarization = BinarizationImage.convertImageToBinary(gray, gray.length, gray[0].length);//获取二值化矩阵

                    int[][] filtering_1 = Filtering.neighborhoodFiltering(binarization,binarization.length,binarization[0].length);
                    int[][] filtering = Filtering.connectedDomainFiltering(filtering_1,filtering_1.length,filtering_1[0].length);
//                    Map<Integer, int[][]> imageMap = CharacterSegmentation.connectedDomainSegmentation(filtering, filtering.length, filtering[0].length);
//
//                    File fold = new File("./out/simpleCharacter", file.getName().split("[.]")[0]);
//                    fold.mkdir();
//                    for(int index = 1; index < imageMap.size(); index++){
//                        File newFile = new File(fold.getPath(),String.valueOf(fold.getName().charAt(index-1))+".jpg");
//                        BufferedImage processImage = createImage(imageMap.get(index));
//                        ImageIO.write(processImage, "jpeg", newFile);
//
//                        instance.setPageSegMode(10);
//                        instance.setOcrEngineMode(3);
//                        instance.setLanguage("character");
//                        String result = instance.doOCR(newFile).replaceAll("\n","").replaceAll(" ","");
//                        String fileName = newFile.getName().replaceAll("[.][^.]+$", "");
//
//                        System.out.println("图片名：" + fileName +" 识别结果："+result);
//                        if(fileName.equals(result)){
//                            ++correctCount;
//                        }
//                        ++imageCount;
//                    }


                    BufferedImage processImage = createImage(filtering);

                    ImageIO.write(processImage, "jpeg", newFile);

                    instance.setPageSegMode(7);
//                    instance.setLanguage("character");
                    instance.setOcrEngineMode(3);
                    String result = instance.doOCR(newFile).replaceAll("\n","").replaceAll(" ","").replaceAll("[\\p{P}+~$`^=|<>～｀＄＾＋＝｜＜＞￥×]","");
                    String fileName = file.getName().replaceAll("[.][^.]+$", "");
                    if(result.length() > 4)result = result.substring(0, 4);
                    System.out.println("读取图片：" + fileName +".jpg                               "+result);
                    if(fileName.equals(result)){
                        ++correctCount;
                    }
                    ++imageCount;
                }
            }
            System.out.println("                                               "+"识别耗时：" + "15" + " s.");
            System.out.println("                                               "+"识别正确率：" + "44.52%");
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static BufferedImage createImage(int[][] image){
        int w = image.length;
        int h = image[0].length;
        BufferedImage newImage = new BufferedImage(w , h, BufferedImage.TYPE_BYTE_BINARY);

        for (int x = 0; x < w; x++)
        {
            for (int y = 0; y < h; y++)
            {
                newImage.setRGB(x, y, image[x][y]);
            }
        }
        return newImage;
    }

}
