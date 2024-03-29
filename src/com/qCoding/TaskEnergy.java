package com.qCoding;

import java.util.concurrent.CountDownLatch;

public class TaskEnergy implements Runnable {
    private Thread t;
    private int upperBound;
    private int lowerBound;
    private int[][] colors;
    private int[][] energy;
    private CountDownLatch countDownLatch;

    TaskEnergy(CountDownLatch cdl, int[][] colors, int[][] energy) {
        countDownLatch = cdl;
        this.colors = colors;
        this.energy = energy;
    }

    void setUpperBound(int _upperBound) {
        upperBound = _upperBound;
    }

    void setLowerBound(int _lowerBound) {
        lowerBound = _lowerBound;
    }

    public void run() {
        for (int i = lowerBound; i < upperBound; i++) {
            for (int j = 0; j < this.height(); j++) {
                this.energy[i][j] = energyCalc(i, j);
            }
        }
        countDownLatch.countDown();
    }

    private int energyCalc(int x, int y) {
        if (x == 0 || x == this.width() - 1 || y == 0 || y == this.height() - 1) {
            return 1000000;
        } else {
            int deltaXRed = red(colors[x - 1][y]) -
                    red(colors[x + 1][y]);
            int deltaXGreen = green(colors[x - 1][y]) -
                    green(colors[x + 1][y]);
            int deltaXBlue = blue(colors[x - 1][y]) -
                    blue(colors[x + 1][y]);

            int deltaYRed = red(colors[x][y - 1]) - red(colors[x][y + 1]);
            int deltaYGreen = green(colors[x][y - 1]) - green(colors[x][y + 1]);
            int deltaYBlue = blue(colors[x][y - 1]) - blue(colors[x][y + 1]);

            return deltaXRed * deltaXRed + deltaXBlue * deltaXBlue + deltaXGreen * deltaXGreen + deltaYRed * deltaYRed + deltaYBlue * deltaYBlue + deltaYGreen * deltaYGreen;
        }

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

    public void start(int[][] _colors, int[][] _energy) {
        colors = _colors;
        energy = _energy;
        if (t == null) {
            t = new Thread(this);
            t.start();
        }
    }

    private int width() {
        return this.colors.length;
    }

    private int height() {
        return this.colors[0].length;
    }

}
