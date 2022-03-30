/**
 * Класс отвечающий за анимацию объектов карты
 * Хранит размеченные кадры анимации
 * Отвечает за задержку между кадрами анимации
 * @version dev
 * @since dev
 */

package ru.Entity;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.nio.Buffer;

public class Animation {
    private BufferedImage[] frames;
    private int currentFrame;

    private long startTime;
    private long delay;

    private boolean playedOnce;

    public Animation(){
        playedOnce = false;
    }

    public void setFrames(BufferedImage[] frames){
        this.frames = frames;
        currentFrame = 0;
        startTime = System.nanoTime();
        playedOnce = false;
    }

    public void setDelay(long d){
        this.delay = d;
    }
    public void setFrame(int i){
        currentFrame = i;
    }

    public void update(){
        // whether or not to move to the next frame
        if (delay == -1){
            return;
        }
        long elapsed = (System.nanoTime() - startTime) / 1000000;
        if (elapsed > delay){
            currentFrame++;
            startTime = System.nanoTime();
        }
        if (currentFrame == frames.length){
            currentFrame = 0;
            playedOnce = true;
        }
    }

    public int getFrame(){
        return  this.currentFrame;
    }

    public BufferedImage getImage(){
        return frames[currentFrame];
    }

    public boolean hasPlayedOnce(){
        return playedOnce;
    }
}
