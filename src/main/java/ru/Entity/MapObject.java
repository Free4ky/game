/**
 * Супер класс всех объектов карты.
 * Содержит основные поля и методы.
 */
package ru.Entity;

import ru.Main.GamePanel;
import  ru.TileMap.*;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

// super class for all objects
public abstract class MapObject{
    public int saved_x;
    public int saved_y;
    public double saved_dy;
    public double saved_dx;
    // tile stuff
    protected TileMap tileMap;
    protected int tileSize;
    protected double xmap;
    protected double ymap;

    // position and vector
    protected  double x;
    protected  double y;
    protected  double dx;
    protected  double dy;

    // dimensions, reading sprite sheets
    protected int width;
    protected int height;

    // collision box
    protected int cwidth;
    protected int cheight;

    // collision
    protected int currRow;
    protected int currCol;

    // next position
    protected double xdest;
    protected double ydest;

    protected double xtemp;
    protected double ytemp;

    // collision points
    protected boolean topLeft;
    protected boolean topRight;
    protected boolean bottomLeft;
    protected boolean bottomRight;

    //animation
    public Animation animation;
    protected int currentAction;
    protected int previousAction;
    protected boolean facingRight; // for flip sprite

    //movement
    protected boolean left;
    protected boolean right;
    protected boolean up;
    protected boolean down;
    protected boolean jumping;
    protected boolean falling;

    // movement attributes
    protected double moveSpeed; // how fast object accelerates
    protected double maxSpeed;
    protected double stopSpeed; // deceleration speed
    protected double fallSpeed; // gravity;
    protected double maxFallSpeed;
    protected double jumpStart; // how high object can jump
    protected double stopJumpSpeed;

    protected boolean nearTheWall;

    //constructor
    public MapObject(TileMap tm){
        nearTheWall = false;
        tileMap = tm;
        tileSize = tm.getTileSize();
    }

    public boolean intersects(MapObject o){
        //rectangle collision
        Rectangle r1 = getRectangle();
        Rectangle r2 = o.getRectangle();
        return r1.intersects(r2); // built-in method
    }

    public boolean intersectsHW(MapObject o){
        Rectangle r1 = getRectangleHW();
        Rectangle r2 = o.getRectangleHW();
        return r1.intersects(r2);
    }

    public Rectangle getRectangleHW(){
        return new Rectangle((int)x - width, (int)y-height,width,height);
    }
    public Rectangle getRectangle(){
        return new Rectangle(
                (int)x - cwidth, (int)y-cheight,cwidth,cheight);
    }

    public void calculateCorners(double x, double y){

        // column on the left
        int leftTile = (int)(x - cwidth/2)/tileSize;
        // column on the right
        int rightTile = (int)(x + cwidth/2 - 1)/tileSize;
        // top tile (top row)
        int topTile = (int)(y - cheight/2)/tileSize;
        // bottom tile (bottom row)
        int bottomTile = (int)(y+cheight/2 - 1)/tileSize;

        //topLeft type
        int tl = tileMap.getType(topTile,leftTile);
        // topRight type
        int tr = tileMap.getType(topTile,rightTile);
        //bottomLeft type
        int bl = tileMap.getType(bottomTile,leftTile);
        //bottomRight type
        int br = tileMap.getType(bottomTile,rightTile);

        topLeft = tl == Tile.BLOCKED;
        topRight = tr == Tile.BLOCKED;
        bottomLeft = bl == Tile.BLOCKED;
        bottomRight = br == Tile.BLOCKED;
    }

    // collision with map
    public void checkTileMapCollision(){
        // current map column
        currCol = (int)x / tileSize;
        // current map row
        currRow = (int)y / tileSize;

        //next position
        xdest = x + dx;
        ydest = y + dy;

        xtemp = x;
        ytemp = y;

        // calculate corners in y direction
        calculateCorners(x,ydest);
        if (dy < 0){
            if (topLeft || topRight) {
                // stop moving in up direction
                dy = 0;
                // set the object just below the tile it just hit
                ytemp = currRow * tileSize + cheight/2;
            }
            else{
                ytemp += dy;
            }
        }
        if (dy > 0){
            if (bottomLeft || bottomRight){
                dy = 0;
                falling = false;
                // set the object 1px above yhe tile it just landed
                ytemp = (currRow + 1) * tileSize - cheight/2;
            }
            else{
               ytemp+= dy;
            }
        }

        // calculate corners in x direction
        calculateCorners(xdest,y);
        // going left
        if (dx < 0){
            if(topLeft || bottomLeft){
                // stop moving
                dx = 0;
                nearTheWall = true;
                // set the object position just right the tile it hit
                xtemp = currCol * tileSize + cwidth/2;
            }
            else{
                nearTheWall = false;
                xtemp += dx;
            }
        }
        if (dx > 0){
            if (topRight || bottomRight){
                dx = 0;
                nearTheWall = true;
                //set the object just left the tile it hit
                xtemp = (currCol + 1) * tileSize - cwidth/2;
            }
            else{
                nearTheWall = false;
                xtemp +=dx;
            }
        }
        // run of the cliff
        if(!falling){
            // check ground 1px below feets
            calculateCorners(x,ydest + 1);
            // not on the solid ground -> start falling
            if (!bottomLeft && !bottomRight){
                falling = true;
            }
        }
    }

    public int getX(){return (int)x;}
    public int getY(){return (int)y;}
    public int getWidth(){return width;}
    public int getHeight(){return height;}
    public int getCHeight(){return cheight;}
    public int getCWidth(){return cwidth;}

    // global position
    public void setPosition(double x, double y){
        this.x = x;
        this.y = y;
    }

    public void setVector(double dx, double dy){
        this.dx = dx;
        this.dy = dy;
    }

    //local position
    // where actually draw the character
    // final position x + xmap, y + ymap ???
    public void setMapPosition(){
        xmap = tileMap.getX();
        ymap = tileMap.getY();
    }

    public void setLeft(boolean b){
        left = b;
    }
    public void setRight(boolean b){
        right = b;
    }
    public void setUP(boolean b){
        up = b;
    }
    public void setDown(boolean b){
        down = b;
    }
    public void setJumping(boolean b){
        jumping = b;
    }

    public boolean notOnScreen(){
        return x + xmap + width < 0 ||
                x + xmap - width > GamePanel.WIDTH ||
                y + ymap + height < 0 ||
                y + ymap - height > GamePanel.HEIGHT;
    }

    public void draw(Graphics2D g){
        if (facingRight){
            g.drawImage(
                    animation.getImage(),
                    (int)(x+xmap-width/2),
                    (int)(y+ymap - height/2),
                    null
            );
        }
        else{
            g.drawImage(
                    animation.getImage(),
                    // starting coordinate is left side of frame
                    (int)(x + xmap - width/2 + width),
                    (int)(y + ymap - height/2),
                    // draw frame from right to left
                    -width,
                    height,
                    null
            );
        }
    }
    public double getDy(){
        return dy;
    }

    public double getDx() {
        return dx;
    }

    public void saveState(){
        saved_dx = dx;
        saved_dy = dy;
        saved_x = (int)x;
        saved_y = (int)y;
    }
    public void uploadState(){
        long stopDelay = 1000000000;
        setVector(saved_dx,saved_dy);
        setPosition(saved_x, saved_y);
        animation.setDelay(stopDelay);
    }
}
