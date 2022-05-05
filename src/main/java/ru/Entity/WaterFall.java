package ru.Entity;

import ru.TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class WaterFall extends MapObject{

    private BufferedImage[] body;
    private BufferedImage[] bottom;

    public WaterFall(TileMap tm, int frame, int type){

        super(tm);

        //тело водопада
        if(type == 0){
            width = 16;
            height = 24;
            cwidth = 14;
            cheight = 22;
            try {
                // загрузка тела водопада
                BufferedImage spriteSheetBody = ImageIO.read(
                        getClass().getResourceAsStream("/Sprites/Items/WaterFall/waterfall_anim_strip_4.png")
                );
                body = new BufferedImage[4];
                bottom = new BufferedImage[4];
                for(int i = 0; i < 4; i++){
                    body[i] = spriteSheetBody.getSubimage(
                            i*width,
                            0,
                            width,
                            height
                    );
                }
                animation = new Animation();
                animation.setFrames(body, frame);
                animation.setDelay(70);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        else if(type == 1){
            width = 16;
            height = 8;
            cwidth = 14;
            cheight = 6;
            try {
                // загрузка пены
                BufferedImage spriteSheetBottom = ImageIO.read(
                        getClass().getResourceAsStream("/Sprites/Items/WaterFall/waterfall_bottom_anim_strip_4.png"));
                bottom = new BufferedImage[4];
                for(int i = 0; i < 4; i++){
                    bottom[i] = spriteSheetBottom.getSubimage(
                            i*width,
                            0,
                            width,
                            height
                    );
                }
                animation = new Animation();
                animation.setFrames(bottom);
                animation.setDelay(70);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void update(){
        animation.update();
    }

    public void draw(Graphics2D g) {
        setMapPosition();
        g.drawImage(
                animation.getImage(),
                (int)(x+xmap-width/2),
                (int)(y+ymap - height/2),
                null);
    }
}
