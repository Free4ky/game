package ru.Entity.Enemies;

import ru.Entity.Animation;
import ru.Entity.Enemy;
import ru.TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Vase extends Enemy {

    private BufferedImage[] idle;
    private BufferedImage[] death;

    public Vase(TileMap tm){
        super(tm);
        dead = false;
        width = 16;
        height = 16;
        cwidth = 14;
        cheight = 14;

        moveSpeed = 0;
        fallSpeed = 0.2;
        maxFallSpeed = 10.0;
        health = 1;

        damage = 0;

        startDeathAnimation = false;

        try {
            idle = new BufferedImage[1];
            idle[0] = ImageIO.read(
                    getClass().getResourceAsStream("/Sprites/Enemies/Vase/vase.png"));
            BufferedImage spriteSheet = ImageIO.read(
                    getClass().getResourceAsStream("/Sprites/Enemies/Vase/death.png"));
            death = new BufferedImage[5];
            for(int i = 0; i < 5; i++){
                death[i] = spriteSheet.getSubimage(
                        i*width,
                        0,
                        width,
                        height
                );
            }
            animation = new Animation();
            animation.setFrames(idle);
            animation.setDelay(70);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        if(dead && !startDeathAnimation){
            startDeathAnimation = true;
            animation.setFrames(death);
            animation.setDelay(70);
        }
        if(falling){
            dy+= fallSpeed;
            if(dy > maxFallSpeed){
                dy = maxFallSpeed;
            }
        }
        checkTileMapCollision();
        setPosition(xtemp,ytemp);
        animation.update();
    }
    public void draw(Graphics2D g){

        setMapPosition();
        g.drawImage(
                animation.getImage(),
                (int)(x+xmap-width/2),
                (int)(y+ymap-height/2),
                null
        );
    }
}
