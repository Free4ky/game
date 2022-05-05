package ru.Entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Explosion extends MapEffect{


    public Explosion(int x, int y){
        super(x,y);

        width = 30;
        height = 30;

        try{
            BufferedImage spriteSheet = ImageIO.read(
                    getClass().getResourceAsStream(
                         "/Sprites/Enemies/explosion.gif"
                    )
            );
            sprites = new BufferedImage[6];
            for(int i = 0; i < sprites.length; i++){
                sprites[i] = spriteSheet.getSubimage(
                        i*width,
                        0,
                        width,
                        height
                );
            }
            animation = new Animation();
            animation.setFrames(sprites);
            animation.setDelay(70);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void update(){
        animation.update();
        if(animation.hasPlayedOnce()){
            remove = true;
        }
    }

    public boolean shouldRemove(){return remove;}

    public void setMapPosition(int x, int y){
        xmap = x;
        ymap = y;
    }

    public void draw(Graphics2D g){
        g.drawImage(
                animation.getImage(),
                x + xmap - width/2,
                y + ymap - height/2,
                null
        );
    }
}

