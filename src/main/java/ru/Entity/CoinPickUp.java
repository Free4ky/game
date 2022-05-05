package ru.Entity;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class CoinPickUp extends MapEffect{

    public CoinPickUp(int x, int y){
        super(x,y);
        width = 8;
        height = 8;
        try {
            BufferedImage spriteSheet = ImageIO.read(
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
            animation = new Animation();
            animation.setFrames(sprites);
            animation.setDelay(70);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
