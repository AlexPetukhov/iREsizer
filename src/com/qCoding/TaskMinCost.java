package com.qCoding;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class TaskMinCost implements Runnable {
    private int upperBound;
    private int lowerBound;
    private int[][] colors;
    private int[][] energy;
    private int[] nodeTo;
    private int[] distTo;
    private CyclicBarrier cb;
    private CountDownLatch countDownLatch;

    TaskMinCost(int[][] colors, int[][] energy, int[] nodeTo, int[] distTo, CyclicBarrier cb, CountDownLatch countDownLatch) {
        this.colors = colors;
        this.energy = energy;
        this.nodeTo = nodeTo;
        this.distTo = distTo;
        this.cb = cb;
        this.countDownLatch = countDownLatch;

    }

    void setUpperBound(int _upperBound) {
        upperBound = _upperBound;
    }

    void setLowerBound(int _lowerBound) {
        lowerBound = _lowerBound;
    }

    private int index(int x, int y) {
        return width() * y + x;
    }

    private int width() {
        return this.colors.length;
    }

    private int height() {
        return this.colors[0].length;
    }

    public void run() {
        for (int j = height() - 2; j >= 0; j--) {
            for (int i = lowerBound; i < upperBound; i++) {
                if (energy[i][j] == -1) continue;
                int ind = index(i, j);
                for (int k = -1; k <= 1; k++) {
                    if (i + k >= 0 && i + k < width() && j + 1 < height() && energy[i + k][j + 1] != -1) {
                        int indTO = index(i + k, j + 1);
                        if (distTo[indTO] == Integer.MAX_VALUE) continue;
                        if (distTo[indTO] + energy[i][j] < distTo[ind]) {
                            distTo[ind] = distTo[indTO] + energy[i][j];
                            nodeTo[ind] = indTO;
                        }
                    }
                }
            }
            try {
                cb.await();
            } catch (BrokenBarrierException exc) {
                System.out.println(exc);
            } catch (InterruptedException exc) {
                System.out.println(exc);
            }

        }
        countDownLatch.countDown();
    }

}
