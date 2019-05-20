package com.qCoding;

import edu.princeton.cs.algs4.Picture;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.*;

public class App extends JFrame{
    private String path;
    private int foundPic = 0;
    private JTextArea ta = new JTextArea();
    private int width = 0;
    private int height = 0;
    private int desWidth = 0;
    private int desHeight = 0;
    private File fl;

    private void addComponentToPane(Container pane) {
        JTabbedPane tabbedPane = new JTabbedPane();

        //Create the "cards".

// start picture tab
        JPanel picCard = new JPanel(new BorderLayout());
        ImageIcon img = new ImageIcon("src/logo.png");
        img = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("logo.png")));


        JLabel comp = new JLabel(resize(img));
        JLabel tmp1 = new JLabel("Current image size: " + width + " x " + height + ".");
        JLabel tmp3 = new JLabel("Desired image size: " + desWidth + " x " + desHeight + ".");
// end picture tab


// settings tab
        JPanel sett = new JPanel();
        JPanel panel1 = new JPanel(new BorderLayout());
        JTextField tf1 = new HintTextField("Width");
        tf1.setColumns(15);
        JTextField tf2 = new HintTextField("Height");
        tf2.setColumns(15);
        JPanel panel2 = new JPanel();
        panel2.add(tf1);
        panel2.add(tf2);


        JPanel applyDesSizebtnPanel = new JPanel();
        JButton applyDesSizebtn = new JButton("Apply");
        applyDesSizebtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String Swid = tf1.getText();
                String Shei = tf2.getText();
                if(Swid.length() != 0){
                    if(isNumber(Swid)) {
                        if(width != 0) desWidth = Math.min(Integer.parseInt(Swid), (int)(1.5 * width));
                        else desWidth = Integer.parseInt(Swid);
                    }
                    else JOptionPane.showMessageDialog(null, "Width and Height must be positive numbers");
                }
                if(Shei.length() != 0){
                    if(isNumber(Shei)) {
                        if(height != 0) desHeight = Math.min(Integer.parseInt(Shei), (int)(1.5 * height));
                        else desHeight = Integer.parseInt(Shei);
                    }
                    else JOptionPane.showMessageDialog(null, "Width and Height must be positive numbers");
                }
                tmp3.setText("Desired image size: " + desWidth + " x " + desHeight + ".");
                tabbedPane.setSelectedIndex(tabbedPane.indexOfTab("Picture"));
            }
        });
        applyDesSizebtnPanel.add(applyDesSizebtn);

        JPanel desSizeBoxPan = new JPanel();
        desSizeBoxPan.setLayout(new BoxLayout(desSizeBoxPan,BoxLayout.Y_AXIS));
        desSizeBoxPan.add(panel2);
        desSizeBoxPan.add(applyDesSizebtnPanel);

        panel1.add(desSizeBoxPan,BorderLayout.NORTH);

        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Enter desired size:"));

        JPanel panel3 = new JPanel();

        JButton open = new JButton("Browse");
        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(locateImage()){
                    comp.setIcon(resize(new ImageIcon(path)));
                    tmp1.setText("Current image size: " + width + " x " + height + ".");
                    tmp3.setText("Desired image size: " + desWidth + " x " + desHeight + ".");
                    tabbedPane.setSelectedIndex(tabbedPane.indexOfTab("Picture"));
                }
            }
        });
        panel3.add(open);
        panel3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Load new picture:"));

        JPanel outPanel = new JPanel();
        outPanel.setLayout(new BoxLayout(outPanel,BoxLayout.Y_AXIS));
        outPanel.add(panel3);
        outPanel.add(panel1);
        sett.add(outPanel,BorderLayout.NORTH);

// picture tab
        picCard.add(comp, BorderLayout.CENTER);

        JPanel boxPan = new JPanel();
        boxPan.setLayout(new BoxLayout(boxPan,BoxLayout.Y_AXIS));

        JPanel inBoxPan2 = new JPanel();
        inBoxPan2.add(tmp1);
        JPanel inBoxPan3 = new JPanel();
        inBoxPan3.add(tmp3);

        JPanel btnPanel = new JPanel();
        JButton gobtn = new JButton("RUN iREsizer");
        gobtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(foundPic == 1) {
                    ta.setText(null);
                    tabbedPane.setSelectedIndex(tabbedPane.indexOfTab("LOG"));
                    runResizer();
                }
                else {
                    JOptionPane.showMessageDialog(null,"You have to choose image first!");
                }
            }
        });

        boxPan.add(inBoxPan2);
        boxPan.add(inBoxPan3);
        btnPanel.add(gobtn,BorderLayout.CENTER);
        boxPan.add(btnPanel);
        picCard.add(boxPan,BorderLayout.SOUTH);


// LOG TAB:
        JPanel panelLOG = new JPanel(new BorderLayout());
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        JButton clrLogBtn = new JButton("Clear");

        clrLogBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ta.setText(null);
            }
        });

        panelLOG.add(ta,BorderLayout.CENTER);
        panelLOG.add(clrLogBtn,BorderLayout.SOUTH);

        // adding all tabs to container
        tabbedPane.addTab("Picture", picCard);
        tabbedPane.addTab("Settings", sett);
        tabbedPane.addTab("LOG", panelLOG);

        pane.add(tabbedPane, BorderLayout.CENTER);
    }

    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("iREsizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500,500);

        //Create and set up the content pane.
        App demo = new App();
        demo.addComponentToPane(frame.getContentPane());

        //Display the window.
//        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        /* Use an appropriate Look and Feel */
        try {
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        /* Turn off metal's use of bold fonts */
//        UIManager.put("swing.boldMetal", Boolean.FALSE);

        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private boolean locateImage(){
        JButton tmp = new JButton(); // open up the box
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new java.io.File(".")); // cur directory = project location on computer
        fc.setDialogTitle("Choose image");
        FileFilter imageFilter = new FileNameExtensionFilter(
                "Image files", ImageIO.getReaderFileSuffixes());
        fc.setFileFilter(imageFilter);
        if(fc.showOpenDialog(tmp) == JFileChooser.APPROVE_OPTION){
            // picture is chosen
            System.out.println("You chose: " + fc.getSelectedFile().getAbsolutePath());
            path = fc.getSelectedFile().getAbsolutePath();
            fl = fc.getSelectedFile();
            Picture pic = new Picture(path);
            width = pic.width();
            height = pic.height();
            desHeight = desWidth = Math.min(width,height);
            foundPic = 1;
            return true;
        }else return false;
    }

    private void runResizer(){ // image already chosen
        long startTime = System.nanoTime();
        int allowResize = 1; // 1 - resize, 0 - keep origin size

        String picFullName = path.substring(path.lastIndexOf('/') + 1);
        String picType = picFullName.substring(picFullName.lastIndexOf('.') + 1);
        String picName = picFullName.substring(0, picFullName.lastIndexOf('.'));
        System.out.println(picType + " " + picName);

        tool tl = new tool();

        Picture picture = new Picture(path);
        width = picture.width();
        height = picture.height();

        writeToLog("Size of the selected picture: " + picture.width() + "x" + picture.height());

        if (allowResize == 1){// resizing of the input picture
            if(picture.width() > 1000 + desWidth || picture.height() > 1000 + desHeight) {
                picture = tl.resizeImage(picture, path, desWidth, desHeight);
                System.out.println(picture.width() + "x" + picture.height());
                System.out.println("Resize time : " + (System.nanoTime() - startTime) / 1000000000 + " seconds");
            }
        }
        Resizer rszr = new Resizer(picture);
        Picture pic;
        pic = rszr.RUN(desWidth, desHeight);

        String dirPath = fl.getParentFile().getAbsolutePath();
//        dirPath = path.substring(0, path.lastIndexOf('/')); // ".../AppSwing"
        long time = System.nanoTime() / 10000000000L;
        String saveFilePath = dirPath + "/" + picName + time + "." + picType;
        pic.save(saveFilePath);
        writeToLog("Saving picture: " + saveFilePath);

        long endTime = System.nanoTime();
        long totalTime = (endTime - startTime) / 100000000;
        writeToLog("TIME: " + totalTime / 10 + "." + totalTime % 10 + " seconds");
    }

    private ImageIcon resize(ImageIcon ii){
        if(ii.getIconHeight() == 400 && ii.getIconWidth() == 400)return ii;
        Image img = ii.getImage();
        Image newImg;
        if(width == 0 || height == 0){
            newImg = img.getScaledInstance(400,400,Image.SCALE_SMOOTH);
        }else{
            if(height > width){
                newImg = img.getScaledInstance(400*width/height,400,Image.SCALE_SMOOTH);
            }else{
                newImg = img.getScaledInstance(400,400*height/width,Image.SCALE_SMOOTH);
            }
        }
        return new ImageIcon(newImg);
    }

    private void writeToLog(String s){
        ta.append(s + "\n");
    }

    private boolean isNumber(String s){

        for(int i = 0; i < s.length();i++){
            if(!(s.charAt(i) >= '0' && s.charAt(i) <= '9')) return false;
        }
        return true;
    }
}