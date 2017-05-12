package com.jlhe.segmentation;

import com.jlhe.imageBinarization.BinarizationImage;
import com.jlhe.imageGray.GrayImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jlhe.filter.Filtering.connectedDomainFiltering;
import static com.jlhe.filter.Filtering.neighborhoodFiltering;

public class CharacterSegmentation {

    public static void  histogramSegmentation(int[][] image, int w, int h){
        int[] xAxisProjection = new int[w];
        int[] yAxisProjection = new int[h];

        for(int y = 0; y < h; y++){
            for(int x = 0; x < w; x++){
                if(image[x][y] == 0){
                    xAxisProjection[x]++;
                    yAxisProjection[y]++;
                }
            }
        }



        System.out.println("X轴直方图统计数据：");
        for(int index = 0; index < xAxisProjection.length; index++){
            System.out.print("X坐标："+ index +replace(index));
            for(int value = 0; value < xAxisProjection[index]; value++){
                System.out.print("X");
            }
            System.out.println(" "+ xAxisProjection[index]);
        }
        System.out.println();
        System.out.println("Y轴直方图统计数据");
        for(int index = 0; index < yAxisProjection.length; index++){
            System.out.print("Y坐标："+ index +replace(index));
            for(int value = 0; value < yAxisProjection[index]; value++){
                System.out.print("X");
            }
            System.out.println(" "+ yAxisProjection[index]);
        }

    }

    public static Map<Integer, int[][]> connectedDomainSegmentation(int[][] binarization, int w, int h){
        int count = 0;
        int[][] temp = new int[w][h];
        for(int x = 0; x < w; x++){
            for(int y = 0; y < h; y++){
                temp[x][y] = binarization[x][y];
            }
        }
        for(int x = 0; x < w ; x++){
            for(int y = 0; y < h ; y++){
                if (x != 0 && x != w - 1 && y != 0 && y != h - 1) {
                    if (temp[x][y] == 0) {
                        count++;
                        dealBwlabe(temp, x, y, count);
                    }
                }
            }
        }

        Map<Integer, int[][]> imageMap = new HashMap<>();

        while(count >= 0){
            int[][] word = new int[w][h];
            for(int y = 0; y < h; y++){
                for(int x = 0 ;x < w; x++){
                    if(temp[x][y] == count){
                        word[x][y] = 0;
                    }else{
                        word[x][y] = 65535;
                    }
                }
            }
            imageMap.put(count, word);
            count--;
        }
        imageMap.put(0 , temp);

        return imageMap;
    }

    private static String replace(int index){
        if(index < 10)return "   |";
        if(index < 100)return "  |";
        return " |";
    }

    private static void dealBwlabe(int[][] image, int i, int j,int counter) {
        //上
        if (image[i-1][j] == 0) {
            image[i-1][j] = counter;
            if(i - 1 > 0) {
                dealBwlabe(image, i - 1, j, counter);
            }
        }
        //左
        if (image[i][j-1] == 0) {
            image[i][j-1] = counter;
            if(j - 1 > 0) {
                dealBwlabe(image, i, j - 1, counter);
            }
        }
        //下
        if (image[i+1][j] == 0) {
            image[i+1][j] = counter;
            if(i + 1 < image.length - 1) {
                dealBwlabe(image, i + 1, j, counter);
            }
        }
        //右
        if (image[i][j+1] == 0) {
            image[i][j+1] = counter;
            if(j + 1 < image[0].length - 1) {
                dealBwlabe(image, i, j + 1, counter);
            }
        }

        //上左
        if (image[i-1][j-1] == 0) {
            image[i-1][j-1] = counter;
            if(i - 1 > 0 && j - 1 > 0) {
                dealBwlabe(image, i - 1, j - 1, counter);
            }
        }
        //上右
        if (image[i-1][j+1] == 0) {
            image[i-1][j+1] = counter;
            if(i - 1 > 0 && j + 1 < image[0].length - 1) {
                dealBwlabe(image, i - 1, j + 1, counter);
            }
        }
        //下左
        if (image[i+1][j-1] == 0) {
            image[i+1][j-1] = counter;
            if(j - 1 > 0 && i + 1 < image.length - 1) {
                dealBwlabe(image, i + 1, j - 1, counter);
            }
        }
        //下右
        if (image[i+1][j+1] == 0) {
            image[i+1][j+1] = counter;
            if(i + 1 < image.length - 1 && j + 1 < image[0].length -1) {
                dealBwlabe(image, i + 1, j + 1, counter);
            }
        }
    }

    public static void main(String[] args){
        File file = new File("./src/main/resources/img/2Y6H.jpg");
        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            int[][] gray = GrayImage.convertImageToGray(bufferedImage);
            int[][] binar = BinarizationImage.convertImageToBinary(gray, gray.length,gray[0].length);
//            int[][] process_1 = neighborhoodFiltering(binar,binar.length,binar[0].length);
//            int[][] process_2 = connectedDomainFiltering(gray,gray.length,gray[0].length);

            Map<Integer, int[][]> imageMap = connectedDomainSegmentation(gray, gray.length, gray[0].length );

            int[][] result = imageMap.get(0);

            String[][] show = new String[result.length][result[0].length];
            for(int y = 0; y < result[0].length; y++){
                for(int x = 0 ; x < result.length; x++){
                    if(result[x][y] == 65535){
                        show[x][y] = " ";
                    }else{
                        show[x][y] = String.valueOf(result[x][y]);
                    }
                }
            }

            for(int h = 0;h < show[0].length;h ++){
                for(int w = 0;w < show.length;w++){
                    System.out.print(show[w][h]);
                }
                System.out.println();
            }

//            for(int index = 1; index < imageMap.size(); index++){
//                BufferedImage newImage = createImage(imageMap.get(index));
//                File newFile = new File("./out/cs/8DZ4", String.valueOf(file.getName().charAt(index-1))+".jpg");
//
//                ImageIO.write(newImage, "jpeg", newFile);
//
//            }

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
