package com.qCoding;

import edu.princeton.cs.algs4.Picture;
import java.awt.image.BufferedImage;
import java.io.*;
import java.awt.*;
import java.util.*;
import javax.imageio.ImageIO;

class tool {

    /**
     * Converts a given Image into a BufferedImage
     *
     * @param img The Image to be converted
     * @return The converted BufferedImage
     */
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


    Picture getPicture(int mode, String path, String picName, String type) {
        Picture picture;
        if (mode > 0) {
            picture = new Picture(path + picName + "." + type);
        } else {
            picture = new Picture("https://thispersondoesnotexist.com/image");
        }
        return picture;
    }

//    String[] getPicInfo(String path) {
//        String ans[] = new String[2];
//        String picName = "pic";
//        String picType = "png";
//        Scanner in = new Scanner(System.in);
//        int found = 0;
//        do {
//            System.out.println("Enter picture name:");
//            picName = in.nextLine();
//
//            DirectoryScanner scanner = new DirectoryScanner();
//            scanner.setIncludes(new String[]{picName + "*"});
//            scanner.setBasedir(path);
//            scanner.setCaseSensitive(false);
//            scanner.scan();
//            String[] files = scanner.getIncludedFiles();
//            int len = files.length;
//
//            if (len == 0) {
//                System.out.println("File with this name not found. Try again!");
//            } else if (len == 1) {
//                System.out.println("Found 1 file: " + files[0]);
//                System.out.println("Using this file");
//                String suffix = files[0].substring(files[0].lastIndexOf('.') + 1);
//                String prefix = files[0].substring(0, files[0].lastIndexOf('.'));
//                picName = prefix;
//                picType = suffix;
//                found = 1;
//            } else {
//                System.out.println("Found " + len + " files with this name: ");
//                for (String x : files) {
//                    System.out.println(x);
//                }
//                System.out.println("Enter picture name again:");
//                picName = in.nextLine();
//                DirectoryScanner scanner1 = new DirectoryScanner();
//                scanner1.setIncludes(new String[]{picName + "." + picType});
//                scanner1.setBasedir(path);
//                scanner1.setCaseSensitive(false);
//                scanner1.scan();
//                String[] files1 = scanner1.getIncludedFiles();
//                int len1 = files1.length;
//                if (len1 == 1) {
//                    System.out.println("Found 1 file: " + files1[0]);
//                    System.out.println("Using this file");
//                    String suffix = files[0].substring(files[0].lastIndexOf('.') + 1);
//                    String prefix = files[0].substring(0, files[0].lastIndexOf('.'));
//                    picName = prefix;
//                    picType = suffix;
//                    found = 1;
//                } else {
//                    System.out.println("Enter picture type(png, jpg)");
//                    picType = in.nextLine();
//                    DirectoryScanner scanner2 = new DirectoryScanner();
//                    scanner2.setIncludes(new String[]{picName + "." + picType});
//                    scanner2.setBasedir(path);
//                    scanner2.setCaseSensitive(false);
//                    scanner2.scan();
//                    String[] files2 = scanner2.getIncludedFiles();
//                    int len2 = files2.length;
//                    if (len2 == 1) {
//                        System.out.println("Found 1 file: " + files2[0]);
//                        System.out.println("Using this file");
//                        String suffix = files[0].substring(files[0].lastIndexOf('.') + 1);
//                        String prefix = files[0].substring(0, files[0].lastIndexOf('.'));
//                        picName = prefix;
//                        picType = suffix;
//                        found = 1;
//                    } else {
//                        System.out.println("File with this name not found. Try again!");
//                    }
//                }
//            }
//        } while (found == 0);
//        ans[0] = picName;
//        ans[1] = picType;
//        return ans;
//    }
}
