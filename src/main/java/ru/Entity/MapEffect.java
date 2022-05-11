package ru.Entity;

import ru.Audio.AudioPlayer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class MapEffect {

    protected int x;
    protected int y;
    protected int xmap;
    protected int ymap;

    protected int width;
    protected int height;

    protected Animation animation;
    protected BufferedImage[] sprites;

    protected boolean remove;

    public AudioPlayer sfx;

    public MapEffect(int x, int y){
        this.x = x;
        this.y = y;

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
