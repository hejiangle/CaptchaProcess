package com.jlhe.imageGray;

import com.jlhe.imageBinarization.BinarizationImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GrayImage {

    public static int[][] convertImageToGray(BufferedImage srcImage){

        int height = srcImage.getHeight(); //获取图片参数
        int width = srcImage.getWidth();

        int[][] gray = new int[width][height]; //初始化一个灰度数组，用于存储灰度化后的结果

        //遍历像素点，完成灰度化
        for(int w = 0; w < width; w++){
            for(int h = 0; h < height; h++){
                int rgb = srcImage.getRGB(w, h);
                //图片亮度增强
                int red = (int)(((rgb >> 16) & 0xFF)* 1.1 + 30);
                int green = (int)(((rgb >> 8) & 0xFF)* 1.1 + 30);
                int blue = (int)(((rgb >> 0) & 0xFF)* 1.1 + 30);
                //调整亮度增强后超出范围的值
                if (red >= 255)
                {
                    red = 255;
                }
                if (green >= 255)
                {
                    green = 255;
                }
                if (blue >= 255)
                {
                    blue = 255;
                }
                //加权平均法完成灰度化
                gray[w][h] = (int) Math.pow((0.30 * Math.pow(red, 2.2) +
                                    0.59 * Math.pow(green, 2.2) +
                                    0.11 * Math.pow(blue, 2.2)), 1/2.2);
            }
        }
        return gray;
    }
//     grayImage.setRGB(w, h, gray[w][h]);
//        return grayImage;
//BufferedImage grayImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY );
    public static void main(String[] args){
        File file = new File("./src/main/resources/img/5XSY.jpg");
        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            int[][] gray = convertImageToGray(bufferedImage);
            int[][] result = BinarizationImage.convertImageToBinary(gray, gray.length, gray[0].length);

            for(int h = 0;h < result[0].length;h ++){
                for(int w = 0;w < result.length;w++){
                    if (result[w][h] == 0) {
                        System.out.print(result[w][h]);
                    }else{
                        System.out.print(1);
                    }
                }
                System.out.println();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
