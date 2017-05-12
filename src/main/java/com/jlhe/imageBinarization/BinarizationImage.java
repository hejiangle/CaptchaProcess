package com.jlhe.imageBinarization;

import java.awt.image.BufferedImage;


public class BinarizationImage {

    public static int[][] convertImageToBinary(int[][]gray, int w, int h){

        int threshold = ostu(gray, w, h);       //最大类间方差法求阈值
        //int threshold = iteration(gray, w, h);    //迭代法求阈值
        //int threshold = towPeaks(gray, w, h);     //双方法求阈值

        //完成二值化
        for (int x = 0; x < w; x++)
        {
            for (int y = 0; y < h; y++)
            {
                if (gray[x][y] > threshold)
                {
                    gray[x][y] |= 0x00FFFF;
                } else {
                    gray[x][y] &= 0xFF0000;
                }
            }
        }
        return gray;
    }


    //最大间方差法获取二值化阈值
    private static int ostu(int[][] gray, int w, int h){
        int[] histData = new int[w * h];
        // 计算直方图
        for (int x = 0; x < w; x++)
        {
            for (int y = 0; y < h; y++)
            {
                int value = gray[x][y];
                histData[value]++;
            }
        }
        // 计算像素点总数
        int total = w * h;

        float sum = 0;
        for (int t = 0; t < 256; t++)
            sum += t * histData[t];

        float sumB = 0;
        int wB = 0;
        int wF = 0;

        float varMax = 0;
        int threshold = 0;

        for (int t = 0; t < 256; t++)
        {
            wB += histData[t]; // 背景权重
            if (wB == 0)
                continue;
            wF = total - wB; // 前景权重
            if (wF == 0)
                break;
            sumB += (float) (t * histData[t]);
            float mB = sumB / wB; // 背景像素值均值
            float mF = (sum - sumB) / wF; // 前景像素值均值
            // 计算类间方差
            float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);
            // 找出最大类间方差
            if (varBetween > varMax)
            {
                varMax = varBetween;
                threshold = t;
            }
        }
        return threshold;
    }

    //利用迭代法求二值化阈值
    private static int iteration(int[][] gray, int w, int h) {
        int[] histData = new int[w * h];
        // Calculate histogram
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int value = gray[x][y];
                histData[value]++;
            }
        }

        // Total number of pixels
        int total = w * h;

        float sum = 0;
        for (int t = 0; t < 256; t++)
            sum += t * histData[t];

        float sumB = 0;
        int wB = 0;
        int wF = 0;

        float varMax = 0;
        int threshold = 0;

        for (int t = 0; t < 256; t++) {
            wB += histData[t]; // Weight Background
            if (wB == 0)
                continue;

            wF = total - wB; // Weight Foreground
            if (wF == 0)
                break;

            sumB += (float) (t * histData[t]);

            float mB = sumB / wB; // Mean Background
            float mF = (sum - sumB) / wF; // Mean Foreground

            // Calculate Between Class Variance
            float varBetween = (float) 0.5*(mB + mF);

            // Check if new maximum found
            if (Math.abs(varBetween - varMax) > 0.5) {
                varMax = varBetween;
                threshold = t;
            }
        }
        return threshold;
    }

    //利用双峰法求取二值化阈值
    private static int towPeaks(int[][]gray, int w, int h){
        int[] histData = new int[256];
        for(int x = 0; x < w; x++){
            for(int y = 0; y < h; y++){
                int value = gray[x][y];
                histData[value]++;
            }
        }

        int firstIndex = 0;
        int secondIndex = 0;
        int firstPeak = 0;
        int secondPeak = 0;
        for(int index = 0; index < histData.length; index++){
            if(index < 128){
                if(histData[index] > firstPeak){
                    firstPeak = histData[index];
                    firstIndex = index;
                }
            }else{
                if(histData[index] > secondPeak){
                    secondPeak = histData[index];
                    secondIndex = index;
                }
            }
        }

        int count = histData[firstIndex];
        int threshold = 0;
        for(int index = firstIndex; index <= secondIndex; index++) {
            if (count > histData[index]) {
                threshold = index;
            }
        }
        return threshold;
    }

//    private static int towPeaksWithOstu(int[][]gray, int w, int h) {
//        int[] histData = new int[256];
//        for (int x = 0; x < w; x++) {
//            for (int y = 0; y < h; y++) {
//                int value = gray[x][y];
//                histData[value]++;
//            }
//        }
//
//        int firstIndex = 0;
//        int secondIndex = 0;
//        int firstPeak = 0;
//        int secondPeak = 0;
//        for (int index = 0; index < histData.length; index++) {
//            if (index < 128) {
//                if (histData[index] > firstPeak) {
//                    firstPeak = histData[index];
//                    firstIndex = index;
//                }
//            } else {
//                if (histData[index] > secondPeak) {
//                    secondPeak = histData[index];
//                    secondIndex = index;
//                }
//            }
//        }
//        // Total number of pixels
//        int total = w * h;
//
//        float sum = 0;
//        for (int t = 0; t < 256; t++)
//            sum += t * histData[t];
//
//        float sumB = 0;
//        int wB = 0;
//        for(int t = 0;t < firstIndex;t++){
//            wB +
//        }
//        int wF = 0;
//
//        float varMax = 0;
//        int threshold = 0;
//
//        for (int t = firstIndex; t < secondIndex; t++) {
//            wB += histData[t]; // Weight Background
//            if (wB == 0)
//                continue;
//
//            wF = total - wB; // Weight Foreground
//            if (wF == 0)
//                break;
//
//            sumB += (float) (t * histData[t]);
//
//            float mB = sumB / wB; // Mean Background
//            float mF = (sum - sumB) / wF; // Mean Foreground
//
//            // Calculate Between Class Variance
//            float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);
//
//            // Check if new maximum found
//            if (varBetween > varMax) {
//                varMax = varBetween;
//                threshold = t;
//            }
//        }
//
//        return threshold;
//    }
}
