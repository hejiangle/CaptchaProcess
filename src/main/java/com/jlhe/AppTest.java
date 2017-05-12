package com.jlhe;

import com.jlhe.filter.Filtering;
import com.jlhe.imageBinarization.BinarizationImage;
import com.jlhe.imageGray.GrayImage;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by cqu on 09/03/2017.
 */
public class AppTest {

    public static void main(String[] args) {
        File root = new File("./out/cs/5XSY");
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
                    File newFile = new File( "./out/cs/2Y6H",file.getName());

                    //BufferedImage grayImage = GrayImage.gray(bufferedImage);

                    //int[][] gray = GrayImage.gray(bufferedImage);//获取灰度矩阵
                    //BufferedImage binarizationImage = BinarizationImage.binarizatize(gray, gray.length, gray[0].length);//通过灰度矩阵，完成二值化
                    //int[][] binarization = BinarizationImage.binarizatize(gray, gray.length, gray[0].length);//获取二值化矩阵

                    //int[][] filtering_1 = Filtering.neighborhoodFiltering(binarization,binarization.length,binarization[0].length);
                    //int[][] filtering = Filtering.connectedDomainFiltering(filtering_1,filtering_1.length,filtering_1[0].length);

                    //BufferedImage processImage = createImage(filtering);

                    ImageIO.write(bufferedImage, "jpeg", newFile);

                    instance.setPageSegMode(8);
//                    instance.setOcrEngineMode(3);
                    String result = instance.doOCR(newFile).replaceAll("\n","").replaceAll(" ","");
                    String fileName = file.getName().replaceAll("[.][^.]+$", "");

                    System.out.println("图片名：" + fileName +" 识别结果："+result);
//                    if(fileName.equals(result)){
//                        ++correctCount;
//                    }
//                    ++imageCount;
                }
            }
//            System.out.println("识别耗时：" + (int)(System.currentTimeMillis()-startTime)/1000 + " s.");
//            System.out.println("识别正确率：" + (correctCount/imageCount)*100+"%");
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
