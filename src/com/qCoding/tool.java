package com.qCoding;

import edu.princeton.cs.algs4.Picture;
import java.awt.image.BufferedImage;
import java.io.*;
import java.awt.*;
import javax.imageio.ImageIO;

class tool {
    private BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }

    Picture resizeImage(Picture picture, String path, int desWidth, int desHeight, float... wid) {
        System.out.println("Staring to resize image");
        String picName = "pic";
        long time = System.nanoTime() / 10000000;
        String saveFile = path + picName + "_" + time + ".png";
        picture.save(saveFile);


        BufferedImage image = null;
        // READ IMAGE
        try {
            File input_file = new File(saveFile); //image file path
            image = new BufferedImage(picture.width(), picture.height(), BufferedImage.TYPE_INT_ARGB);

            image = ImageIO.read(input_file);

            System.out.println("Reading complete.");
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
        Image newImage = image.getScaledInstance((int) (( desWidth + picture.width())/2), (int) ((desHeight + picture.height())/2), Image.SCALE_SMOOTH);
        image = toBufferedImage(newImage);

        // WRITE IMAGE
        try {
            // Output file path
            File output_file = new File(saveFile);

            // Writing to file taking type and path as
            ImageIO.write(image, "png", output_file);

            System.out.println("Writing complete.");
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }

        picture = new Picture(saveFile);
        // deleting file
        File file = new File(saveFile);
        file.delete();
        return picture;
    }
}
