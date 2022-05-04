package ru.Entity;

import ru.Audio.AudioPlayer;
import ru.TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Coin extends MapObject{

    public static int NumCoinsOnLevel[] = new int[9];
    private AudioPlayer coinEffect;

    private BufferedImage[] sprites;
    int numFrames = 5;
    int ROTATE = 0;
    long timer;

    public Coin(TileMap tm){
        super(tm);
        init();
    }

    public AudioPlayer getCoinEffect() {
        return coinEffect;
    }

    public void init(){
        timer = System.nanoTime();
        width = 16;
        height = 16;
        cwidth = 12;
        cheight = 12;
        try{

            coinEffect = new AudioPlayer("/SFX/Coin_1.mp3");

            BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Items/MonedaD.png"));
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
        animation.setDelay(200);
    }

    public void update(){
        long elapsed = (System.nanoTime() - timer)/1000000;

        int n_y = (int)(15*Math.sin(Math.PI*elapsed/350));
        //System.out.println(n_y);
        animation.update();
        this.y = y + n_y/12;
        //System.out.println(saved_y);
        this.setPosition(this.x,y);
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