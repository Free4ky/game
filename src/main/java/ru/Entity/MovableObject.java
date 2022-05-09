package ru.Entity;

import ru.TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MovableObject extends MapObject{

    private BufferedImage sprite;
    private Player player;

    public MovableObject(TileMap tm, Player player){
        super(tm);

        try {
            sprite = ImageIO.read(
                    getClass().getResourceAsStream("/Sprites/MovableObjects/StoneT.png"));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        width = 30;
        height = 30;
        cwidth = 28;
        cheight = 28;
        fallSpeed = 0.3;
        maxFallSpeed = 10;
        this.player = player;
        dx = 0;
    }

    public void update(){
        if(
                intersects(player) &&
                y > player.getY() - player.height/2 &&
                y < player.getY() + player.height/2
        ){
            dx = player.getDx();
            if(nearTheWall){
                player.intersectsStoppedObject = true;
                if(player.facingRight){
                    player.setVector(0, player.dy);
                    player.setPosition(player.getX() - 1, player.getY());
                }
                else{
                    player.setVector(0, player.dy);
                    player.setPosition(player.getX() + 1, player.getY());
                }
            }
        }
        else if (intersects(player)){
            if(player.facingRight){
                if(player.getX() + width/2 > x){
                    player.intersectMovableObjectY = true;
                }
            }
            else{
                if(player.getX() - width/2 < x){
                    player.intersectMovableObjectY = true;
                }
            }
            //System.out.println(player.jumping);
            //System.out.println((player.getY() + height/2) + " " + (y - height/2));
        }
        else{
            player.intersectsStoppedObject = false;
            player.intersectMovableObjectY = false;
            dx = 0;
        }

        if(falling){
            dy+= fallSpeed;
            if(dy > maxFallSpeed){
                dy = maxFallSpeed;
            }
        }
        checkTileMapCollision();
        setPosition(xtemp,ytemp);
    }

    @Override
    public void draw(Graphics2D g) {
        setMapPosition();
        g.setColor(Color.white);
        g.drawImage(
                sprite,
                (int)(x+xmap-width/2),
                (int)(y+ymap - height/2),
                null
        );
    }
}
