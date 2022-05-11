package ru.Entity;


import ru.TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Supplies extends MapObject{

    public static final int HEART = 0;

    private BufferedImage [] sprites;

    public Supplies(TileMap tm, int type){

        super(tm);

        try{

            switch (type){
                case HEART:
                    height = 16;
                    width = 16;
                    cheight = 14;
                    cwidth = 14;
                    BufferedImage spriteSheet = ImageIO.read(
                            getClass().getResourceAsStream("/Sprites/Items/Heart/heart2.png"));
                    sprites = new BufferedImage[5];
                    for(int i = 0; i < 5; i++){
                        sprites[i] = spriteSheet.getSubimage(
                                i*width,
                                0,
                                width,
                                height
                        );
                    }
                    break;
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
    }

    public void draw(Graphics2D g){

        if (notOnScreen()){
            setMapPosition();
            return;
        }
        setMapPosition();
        g.drawImage(
                animation.getImage(),
                (int)(x+xmap-width/2),
                (int)(y+ymap-height/2),
                null
        );
    }
}
