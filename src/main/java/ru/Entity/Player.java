package ru.Entity;

import ru.TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;

public class Player extends MapObject {

    // player stuff
    private int health;
    private int maxHealth;
    private int fire;
    private int maxFire;
    private boolean dead;
    private boolean flinching;
    private long flinchTimer;

    // fireball
    private boolean firing;
    private int fireCost;
    private int fireBallDamage;
    //private ArrayList<FireBall> fireballs;

    //scratch
    private boolean scratching;
    private int scratchDamage;
    private int scratchRange;

    //gliding
    private boolean gliding;

    // animations
    private ArrayList<BufferedImage[]> sprites;
    //number of frames inside each of animation actions
    private final int[] numFrames = {
            2, 8, 1, 2, 4, 2, 5
    };
    //animation actions (enums)

    private static final int IDLE = 0;
    private static final int WALKING = 1;
    private static final int JUMPING = 2;
    private static final int FALLING = 3;
    private static final int GLIDING = 4;
    private static final int FIREBALL = 5;
    private static final int SCRATCHING = 6;

    public Player(TileMap tm){
        super(tm);

        width = 30;
        height = 30;
        cwidth = 20;
        cheight = 20;

        moveSpeed = 0.3;
        maxSpeed = 1.6;
        stopSpeed = 0.4;
        fallSpeed = 0.15;
        maxFallSpeed = 4.0;
        jumpStart = -4.8;
        stopJumpSpeed = 0.3;

        facingRight = true;

        health = maxHealth = 5;
        fire = maxFire = 2500;

        fireCost = 200;
        fireBallDamage = 5;
        // fireBalls = new Array<FireBall>;
        scratchDamage = 8;
        scratchRange = 40;

        //load sprites
        try{
            BufferedImage spriteSheet = ImageIO.read(
                    getClass().getResourceAsStream(
                            "/Sprites/Player/playersprites.gif"));

            sprites = new ArrayList<BufferedImage[]>();

            // extract each of animation actions from sprite sheet
            // 7 - amount of animation actions in sprite sheet
            for(int i = 0; i < 7; i++){
                BufferedImage[] bi = new BufferedImage[numFrames[i]];
                // read in individual sprites (frames)
                for (int j = 0; j < numFrames[i]; j++){
                    if (i != 6){
                        bi[j] = spriteSheet.getSubimage(
                                j*width,
                                i*height,
                                width,
                                height
                        );
                    }
                    else{
                        bi[j] = spriteSheet.getSubimage(
                                j*width * 2,
                                i*height,
                                width * 2,
                                height
                        );
                    }
                }
                sprites.add(bi);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        animation = new Animation();
        currentAction = IDLE;
        animation.setFrames(sprites.get(IDLE));
        animation.setDelay(400);
    }

    public int getHealth(){return this.health;}
    public int getMaxHealth(){return maxHealth;}
    public int getFire(){return this.fire;}
    public int getMaxFire(){return maxFire;}

    public void setFiring() {
        this.firing = true;
    }

    public void setScratching(){
        this.scratching = true;
    }

    public void setGliding(boolean b){
        gliding = b;
    }

    // handle keyboard input
    private void getNextPosition() {

        // movement
        if (left) {
            dx -= moveSpeed;
            if (dx < -maxSpeed) {
                dx = -maxSpeed;
            }
        } else if (right) {
            dx += moveSpeed;
            if (dx > maxSpeed) {
                dx = maxSpeed;
            }
        } else {
            if (dx > 0) {
                dx -= stopSpeed;
                if (dx < 0) {
                    dx = 0;
                }
            } else if (dx < 0) {
                dx += stopSpeed;
                if (dx > 0) {
                    dx = 0;
                }
            }
        }

        // cannot move while attacking, except in air
        if ((currentAction == SCRATCHING ||
                currentAction == FIREBALL) &&
                !(jumping || falling)) {
            dx = 0;
        }
        // jumping
        if (jumping && !falling) {
            dy = jumpStart;
            falling = true;
        }

        // falling
        if (falling) {
            //gliding
            if (dy > 0 && gliding) {
                dy += fallSpeed * 0.1;
            } else { // falling
                dy += fallSpeed;
            }
            // no long jumping
            if (dy > 0) {
                jumping = false;
            }
            // longer you hold the jump button higher you'll jump
            if (dy < 0 && !jumping) {
                dy += stopJumpSpeed;
            }
            if (dy > maxFallSpeed) {
                dy = maxFallSpeed;
            }
        }
    }

    public void update(){

        // update position
        getNextPosition();
        checkTileMapCollision();
        setPosition(xtemp,ytemp);

        //check attack has stopped
        if (currentAction == SCRATCHING){
            if (animation.hasPlayedOnce()){
                scratching = false;
            }
        }
        if (currentAction == FIREBALL){
            if (animation.hasPlayedOnce()){
                firing = false;
            }
        }

        // set animations
        if(scratching){
            if(currentAction != SCRATCHING){
                currentAction = SCRATCHING;
                animation.setFrames(sprites.get(SCRATCHING));
                animation.setDelay(50);
                width = 60;
            }
        }
        else if(firing){
            if(currentAction != FIREBALL){
                currentAction = FIREBALL;
                animation.setFrames(sprites.get(FIREBALL));
                animation.setDelay(100);
                width = 30;
            }
        }
        else if (dy > 0){ // falling
            if (gliding){
                if (currentAction != GLIDING){
                    currentAction = GLIDING;
                    animation.setFrames(sprites.get(GLIDING));
                    animation.setDelay(100);
                    width = 30;
                }
            }
            else if(currentAction != FALLING){
                currentAction = FALLING;
                animation.setFrames(sprites.get(FALLING));
                animation.setDelay(100);
                width = 30;
            }

        }
        else if (dy < 0){
            if (currentAction != JUMPING){
                currentAction = JUMPING;
                animation.setFrames(sprites.get(JUMPING));
                animation.setDelay(-1);
                width = 30;
            }
        }
        else if (left || right){
            if(currentAction != WALKING){
                currentAction = WALKING;
                animation.setFrames(sprites.get(WALKING));
                animation.setDelay(40);
                width = 30;
            }
        }
        else {
            if(currentAction != IDLE){
                currentAction = IDLE;
                animation.setFrames(sprites.get(IDLE));
                animation.setDelay(400);
                width = 30;
            }
        }
        animation.update();

        //set direction
        if (currentAction != SCRATCHING && currentAction != FIREBALL){
            if (right){ facingRight = true;}
            if (left) {facingRight = false;}
        }
    }

    public void draw(Graphics2D g){
        setMapPosition();
        // draw the player
        // мигание при попадантт по игроку
        // blinking every 100 milliseconds
        if (flinching){
            long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
            if (elapsed / 100 % 2 == 0){
                return;
            }
        }

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
}