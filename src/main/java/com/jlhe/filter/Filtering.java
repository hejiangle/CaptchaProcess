package com.jlhe.filter;

import com.jlhe.imageBinarization.BinarizationImage;
import com.jlhe.imageGray.GrayImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Filtering {

    public static int[][] neighborhoodFiltering(int[][] binarization , int w , int h) {
        int[] temp = new int[8];
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                if(binarization[x][y] == 0) {
                    if (x != 0 && x != w - 1 && y != 0 && y != h - 1) {
                        temp[0] = binarization[x - 1][y - 1];
                        temp[1] = binarization[x - 1][y];
                        temp[2] = binarization[x - 1][y + 1];
                        temp[3] = binarization[x][y - 1];
                        temp[4] = binarization[x][y + 1];
                        temp[5] = binarization[x + 1][y - 1];
                        temp[6] = binarization[x + 1][y];
                        temp[7] = binarization[x + 1][y + 1];

                        if(Arrays.stream(temp).filter(n -> n == 0).count() < 4){
                            binarization[x][y] = 65535;
                        }
                    }
                }
            }

        }
        return binarization;
    }

    public static int[][] connectedDomainFiltering(int[][] binarization, int w, int h){
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
                    if (binarization[x][y] == 0) {
                        count++;
                        dealBwlabe(temp, x, y, count);
                    }
                }
            }
        }

        int[] area = new int[count+1];

        for(int x = 0; x < w; x++){
            for(int y = 0; y < h; y++){
                for(int num = 0; num <= count; num++){
                    if(temp[x][y] == num){
                        area[num]++;
                    }
                }
            }
        }

        for(int x = 0; x < w; x++){
            for(int y = 0; y < h; y ++){
                for(int num = 0; num <= count; num++){
                    if(temp[x][y] == num && area[num] < 300){
                        binarization[x][y] = 65535;
                    }
                }
            }
        }

        return binarization;
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

//        //上左
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
        File file = new File("./out/comb/2Y6H.jpg");
        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            int[][] gray = GrayImage.convertImageToGray(bufferedImage);
            int[][] binar = BinarizationImage.convertImageToBinary(gray, gray.length,gray[0].length);
            int[][] process = neighborhoodFiltering(binar,binar.length,binar[0].length);
            int[][] result = connectedDomainFiltering(binar,binar.length,binar[0].length);

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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
