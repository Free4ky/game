package ru.Entity;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class PickUp extends MapEffect{

    public static final int COIN = 0;
    public static final int HEART = 1;

    public PickUp(int x, int y, int type){
        super(x,y);
        width = 8;
        height = 8;
        try {
            BufferedImage spriteSheet;
            switch (type){
                case COIN:
                    spriteSheet = ImageIO.read(
                            getClass().getResourceAsStream("/Sprites/Items/Coins/coinPickUpT.png"));
                    sprites = new BufferedImage[4];
                    for(int i = 0; i < 4; i++){
                        sprites[i] = spriteSheet.getSubimage(
                                i*width,
                                0,
                                width,
                                height
                        );
                    }
                    break;
                case HEART:
                    spriteSheet = ImageIO.read(
                            getClass().getResourceAsStream("/Sprites/Items/Heart/HeartPickUpT.png"));
                    sprites = new BufferedImage[4];
                    for(int i = 0; i < 4; i++){
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
}
