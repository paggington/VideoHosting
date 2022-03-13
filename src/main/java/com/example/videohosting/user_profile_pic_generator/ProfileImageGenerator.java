package com.example.videohosting.user_profile_pic_generator;


import com.example.videohosting.files.DIRECTORY;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.Random;


public class ProfileImageGenerator implements DIRECTORY {
    public void generateImage(){
        BufferedImage bufferedImage=new BufferedImage(100,100,BufferedImage.TYPE_USHORT_555_RGB);
        Graphics graphics=bufferedImage.createGraphics();
        graphics.setColor(Color.black);

        for (int i = 0; i < 10; i++) {
            int x=getRandomNumber(1,99);
            int y=getRandomNumber(1,99);
            int colorR=getRandomNumber(100,255);
            int colorG=getRandomNumber(100,255);
            int colorB=getRandomNumber(100,255);

            Graphics graphics1=bufferedImage.createGraphics();
            graphics1.setColor(new Color(colorR, colorG, colorB));
            graphics1.drawRect(x,y,20,20);
            graphics1.fillRect(x,y,20,20);
        }
        try {
            ImageIO.write(bufferedImage,"jpg",new File(DIR+"\\profile_images\\"+"asd.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private int getRandomNumber(int from,int to){

        return (int) (Math.random() * (to - from + 1) + from);
    }
    private int getRandomNumber(int from,int to,int step){
        int number=(int) (Math.random() * (from - to + 1) + from);
        if(number%step!=0){
            return getRandomNumber(from, to, step);
        }
        return number;
    }
}
