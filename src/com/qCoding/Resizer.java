package com.qCoding;

import edu.princeton.cs.algs4.Picture;
import java.awt.*;
import java.util.*;

class Resizer {
    private static ThreadHolder th;
    private int[][] colors;
    private int[][] Energy;

    Resizer(Picture picture) {
        if (picture == null) throw new NullPointerException();
        colors = new int[picture.width()][picture.height()];
        Energy = new int[picture.width()][picture.height()];
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                colors[i][j] = picture.get(i, j).getRGB();
            }
        }
        th = new ThreadHolder();
    }

    private int getAvgRGB(int pix1, int pix2) {
        int red = (red(pix1) + (red(pix2))) / 2;
        red = red << 16;
        int green = (green(pix1) + (green(pix2))) / 2;
        green = green << 8;
        int blue = (blue(pix1) + blue(pix2)) / 2;
        return red + green + blue;
    }

    private void addHorizontalSeam(int desHeight){
        this.colors = transpose(this.colors);
        this.Energy = transpose(this.Energy);
        addVerticalSeam(desHeight);
        this.colors = transpose(this.colors);
        this.Energy = transpose(this.Energy);
    }

    private void addVerticalSeam(int desWidth) {
        // num = number vert seams width++

        int num = desWidth - width();
        System.out.println("NUM : " + num);
        int times = (int)Math.log(num);
        num = num/times;
        System.out.println("Num: " + num + " times: " + times);
        for(int time = 0; time < times;time ++){
            int seam[][] = findVerticalSeam(num);
            int leftpix; //left pixel to be inserted
            int rightpix; // right one
            int tmp1, tmp2;
            int updatedColor[][] = new int[width() + num][height()];

            for (int i = 0; i < width(); i++) {
                System.arraycopy(this.colors[i], 0, updatedColor[i], 0, this.colors[i].length);
            }

            for (int k = 0; k < num; k++) {
                for (int i = 0; i < height(); i++) {
                    int x = seam[i][k];
                    if (x == 0) { //find averages for left and right pixels
                        rightpix = getAvgRGB(updatedColor[x + 1][i], updatedColor[x][i]);
                        leftpix = updatedColor[x][i];
                    } else if (x == width() - 1) {
                        leftpix = getAvgRGB(updatedColor[x - 1][i], updatedColor[x][i]);
                        rightpix = updatedColor[x][i];
                    } else {
                        leftpix = getAvgRGB(updatedColor[x - 1][i], updatedColor[x][i]);
                        rightpix = getAvgRGB(updatedColor[x + 1][i], updatedColor[x][i]);
                    }
                    if (x != 0) { // insert pixels
                        tmp2 = updatedColor[x][i];
                        updatedColor[x - 1][i] = leftpix;
                        updatedColor[x][i] = rightpix;
                        for (int j = x + 1; j < width() + num - 1; j++) {
                            tmp1 = updatedColor[j][i];
                            updatedColor[j][i] = tmp2;
                            tmp2 = tmp1;
                        }
                        updatedColor[width() + num - 1][i] = tmp2;
                    } else {
                        tmp2 = updatedColor[x + 1][i];
                        updatedColor[x][i] = leftpix;
                        updatedColor[x + 1][i] = rightpix;
                        for (int j = x + 2; j < width() + num - 1; j++) { // shift remaining pixels
                            tmp1 = updatedColor[j][i];
                            updatedColor[j][i] = tmp2;
                            tmp2 = tmp1;
                        }
                        updatedColor[width() + num - 1][i] = tmp2;
                    }
                }
            }
            colors = updatedColor;
            Energy = new int[colors.length][colors[0].length];
        }
    }

    private Picture picture() {
        Picture picture = new Picture(colors.length, colors[0].length);
        for (int i = 0; i < colors.length; i++) {
            for (int j = 0; j < colors[0].length; j++) {
                Color color = new Color(this.colors[i][j]);
                picture.set(i, j, color);
            }
        }
        return picture;
    }

    private int width() {
        return this.colors.length;
    }

    private int height() {
        return this.colors[0].length;
    }

    private int[][] findHorizontalSeam(int mult) {
        this.colors = transpose(this.colors);
        this.Energy = transpose(this.Energy);
        int[][] seam = findVerticalSeam(mult);
        this.colors = transpose(this.colors);
        this.Energy = transpose(this.Energy);
        return seam;
    }

    private int[][] findVerticalSeam(int mult) {
        int n = this.width() * this.height();
        int[][] seam = new int[this.height()][mult];
        th.calculateEnergy(this.colors, this.Energy);
        for (int curSeam = 0; curSeam < mult; curSeam++) {
            int[] nodeTo = new int[n];
            int[] distTo = new int[n];

            for (int i = 0; i < n; i++) distTo[i] = Integer.MAX_VALUE;

            int index = 0;
            int min = Integer.MAX_VALUE;

            for (int i = 0; i < width(); i++) { // top row distTO = Energy
                int ind = index(i, height() - 1);
                distTo[ind] = Energy[i][height() - 1];

            }
            th.calculateCostTable(this.colors, this.Energy, distTo, nodeTo);

            // find min dist in the bottom row
            for (int i = 0; i < width(); i++) {
                // j = 0
                int ind = index(i, 0);
                if (distTo[ind] < min) {
                    min = distTo[ind];
                    index = ind;
                }
            }

            // find seam one by one from bot to top
            for (int j = 0; j < height(); j++) {
                int y = j;
                int x = index - y * width();
                seam[y][curSeam] = x;
                index = nodeTo[index];
                Energy[x][y] = -1;
            }
        }

        return seam;
    }

    private void removeHorizontalSeam(int[][] seam, int mult) {
        int[][] updColor = new int[width()][height() - mult];
        for (int i = 0; i < seam.length; i++) { // seam.length cycle (i < seam.length)
            // height cycle
            int[] col = new int[mult];
            col = seam[i];
            Arrays.sort(col);
            int last = 0;
            int updLast = 0;
            for (int j = 0; j < mult; j++) {
                if (col[j] - last > 0) {
                    System.arraycopy(this.colors[i], last, updColor[i], updLast, col[j] - last);
                }
                updLast += col[j] - last;
                last = col[j] + 1;
                if (col[j] == height() - 1) break;
            }
            if (last < height()) {
                System.arraycopy(this.colors[i], last, updColor[i], updLast, height() - last);
            }
        }
        this.colors = updColor;
    }

    private int red(int rgb) {
        return (rgb >> 16) & 0xFF;
    }

    private int green(int rgb) {
        return (rgb >> 8) & 0xFF;
    }

    private int blue(int rgb) {
        return (rgb) & 0xFF;
    }

    private int index(int x, int y) {
        return width() * y + x;
    }

    private int[][] transpose(int[][] origin) {
        int[][] result = new int[origin[0].length][origin.length];
        for (int i = 0; i < origin[0].length; i++) {
            for (int j = 0; j < origin.length; j++) {
                result[i][j] = origin[j][i];
            }
        }
        return result;
    }

    private void normalMode(int desWidth, int desHeight) { // doing sqrt(times) seams at one time

        if(desWidth < width()){
            int times = width() - desWidth;
            int mult = (int) Math.sqrt(times); // number of seams to be removed at one time
            times = times / mult;
            this.colors = this.transpose(this.colors);
            this.Energy = this.transpose(this.Energy);
            for (int i = 0; i < times; i++) {
                if (i % 10 == 0) System.out.println(i);
                int[][] seam = this.findHorizontalSeam(mult);
                this.removeHorizontalSeam(seam, mult);
            }
            this.colors = this.transpose(this.colors);
            this.Energy = this.transpose(this.Energy);
        }

        if(desHeight < height()){
            int times = height() - desHeight;
            int mult = (int) Math.sqrt(times); // number of seams to be removed at one time
            times = times / mult;
            for (int i = 0; i < times; i++) {
                if (i % 10 == 0) System.out.println(i);
                int[][] seam = this.findHorizontalSeam(mult);
                this.removeHorizontalSeam(seam, mult);
            }
        }
        // saving picture :
        System.out.println("Size of the picture after carving: " + this.colors.length + "x" + this.colors[0].length);
    }

    Picture RUN(int desWidth, int desHeight){
        th = new ThreadHolder();
        if(desWidth != width()){
            if(desWidth > width()){
                // extend
                addVerticalSeam(desWidth);
            }else{
                // crop: remove vert seams
                normalMode(desWidth,height());
            }
        }
        if(desHeight != height()){
            if(desHeight > height()){
                // extend
                addHorizontalSeam(desHeight);

            }else{
                // crop: remove horizontal seams
                normalMode(width(), desHeight);
            }
        }
        th.shutDown();
        return this.picture();
    }
}