package ru.Entity.Enemies;

import ru.Entity.Animation;
import ru.Entity.Enemy;
import ru.TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Slugger extends Enemy {

    private BufferedImage[] sprites;

    public Slugger (TileMap tm){
        super(tm);

        moveSpeed = 0.3;
        maxSpeed = 0.3;

        fallSpeed = 0.2;
        maxFallSpeed = 10.0;
        width = 30;
        height = 30;
        cwidth = 20;
        cheight = 20;

        health = maxHealth = 2;
        damage = 1;

        // load tileSheet
        try {
            BufferedImage spriteSheet = ImageIO.read(
                    getClass().getResourceAsStream(
                            "/Sprites/Enemies/slugger.gif")
            );
            sprites = new BufferedImage[3];
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
            animation.setDelay(300);
            right = true;
            facingRight = true;
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getNextPosition(){

        // movement
        if (left) {
            dx -= moveSpeed;
            if (dx < -maxSpeed) {
                dx = -maxSpeed;
            }
        }
        else if (right) {
            dx += moveSpeed;
            if (dx > maxSpeed) {
                dx = maxSpeed;
            }
        }

        if(falling){
            dy+= fallSpeed;
        }
    }

    public void update(){
        // update position
        getNextPosition();
        checkTileMapCollision();
        setPosition(xtemp,ytemp);

        // check flinching
        if(flinching){
            long elapsed = (System.nanoTime() - flinchTimer)/1000000;
            if (elapsed > 400){
                flinching = false;
            }
        }
        // if hits a wall, go other direction
        // dx == 0 means object have hit a map tile
        if(right && dx == 0){
            right = false;
            left = true;
            facingRight = false;
        }
        else if(left && dx == 0){
            right = true;
            left = false;
            facingRight = true;
        }

        // update animation
        animation.update();
    }

    public void draw(Graphics2D g){
        // if objects not on screen it is no point to draw them
        //if (notOnScreen()) return;

        setMapPosition();

        // draw depends on object's movement direction
        super.draw(g);
    }

}
