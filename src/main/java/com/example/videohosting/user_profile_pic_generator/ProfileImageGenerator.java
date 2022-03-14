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
    public String generateImage(String username){
        BufferedImage bufferedImage=new BufferedImage(400,400,BufferedImage.TYPE_USHORT_555_RGB);
        Graphics graphics=bufferedImage.createGraphics();
        graphics.setColor(getRandomColor());
        graphics.fillRect(0,0,400,400);

        for (int i = 0; i < 50; i++) {
            int x=getRandomNumber(0,400);
            int y=getRandomNumber(0,400);


            Graphics graphics1=bufferedImage.createGraphics();
            graphics1.setColor(getRandomColor());
            graphics1.drawRect(x,y,50+i,50+i);
            graphics1.fillRect(x,y,50,50);
        }
        graphics.setColor(getRandomColorForLines());
        for (int i = 0; i < getRandomNumber(1,5); i++) {
            int height=getRandomNumber(2,10);
            int y=getRandomNumber(0,400);
            graphics.drawRect(0,y,399,height);
            graphics.fillRect(0,y,399,height);
        }
        BufferedImage bufferedImage1=bufferedImage.getSubimage(1,1,1,1);
        try {
            ImageIO.write(bufferedImage,"jpg",new File(DIR+"\\profile_images\\"+username+".jpg"));
            return DIR+"\\profile_images\\"+username+".jpg";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return DIR+"\\profile_images\\asd.jpg";
    }
    private Color getRandomColor(){
        int colorR=getRandomNumber(100,255);
        int colorG=getRandomNumber(100,255);
        int colorB=getRandomNumber(100,255);
        return new Color(colorR, colorG, colorB);
    }
    private Color getRandomColorForLines(){
        int colorR=getRandomNumber(0,50);
        int colorG=getRandomNumber(0,100);
        int colorB=getRandomNumber(0,100);
        return new Color(colorR, colorG, colorB);
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
