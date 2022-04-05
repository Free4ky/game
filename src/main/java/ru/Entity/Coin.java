package ru.Entity;

import ru.TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Coin extends MapObject{

    private BufferedImage[] sprites;
    int numFrames = 2;
    int ROTATE = 0;
    long timer;

    public Coin(TileMap tm){
        super(tm);
        init();
    }
    public void init(){
        timer = System.nanoTime();
        width = 30;
        height = 30;
        cwidth = 20;
        cheight = 20;
        try{
            BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/CoinSprite.png"));
            sprites = new BufferedImage[numFrames];
            for (int i = 0; i < numFrames; i++){
                sprites[i] = spriteSheet.getSubimage(
                        i*width,
                        0,
                        width,
                        height
                );
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
        animation = new Animation();
        currentAction = ROTATE;
        animation.setFrames(sprites);
        animation.setDelay(400);
    }

    public void update(){
        //long elapsed = (System.nanoTime() - timer)/1000000;
        //int n_y = (int)(Math.abs(Math.sin(Math.PI*elapsed)));
        checkTileMapCollision();
        //setPosition(xtemp,ytemp);
        animation.update();
        //this.setPosition(this.x,n_y);
    }
    public void draw(Graphics2D g){
        setMapPosition();
        g.drawImage(
                animation.getImage(),
                (int)(x+xmap-width/2),
                (int)(y+ymap - height/2),
                null);
    }
}