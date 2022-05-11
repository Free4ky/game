package ru.Entity.Enemies;

import ru.Audio.AudioPlayer;
import ru.Entity.Animation;
import ru.Entity.Enemy;
import ru.TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Spike extends Enemy {

    private BufferedImage sprite;

    public Spike(TileMap tm){
        super(tm);
        health = 100000000;
        width = 31;
        height = 4;
        cwidth = 30;
        cheight = 10;
        moveSpeed = 0;
        fallSpeed = 0.2;
        maxFallSpeed = 10.0;

        damage = 1;

        try{
            deathEffect = new AudioPlayer("/SFX/deathSound.mp3");
            sprite = ImageIO.read(
                    getClass().getResourceAsStream(
                            "/Sprites/Enemies/SpikeT.png"
                    )
            );
            animation = new Animation();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void draw(Graphics2D g) {
        if (notOnScreen()){
            setMapPosition();
            return;
        }
        setMapPosition();
        g.drawImage(
                sprite,
                (int)(x+xmap-width/2),
                (int)(y+ymap-height/2),
                null
        );
    }
    public void update() {
        if(falling){
            dy+= fallSpeed;
            if(dy > maxFallSpeed){
                dy = maxFallSpeed;
            }
        }
        checkTileMapCollision();
        setPosition(xtemp,ytemp);
    }
}
