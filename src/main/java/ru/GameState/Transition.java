package ru.GameState;
/**
 *Класс отвечающий за отрисовку перехода между меню и уровнем
 * @author Карпов Даниил
 * @version dev
 */

import ru.Main.GamePanel;

import java.awt.*;

public class Transition {
    public int width;
    public int height;
    private long delay;
    private long startTime;
    public Transition(){
        init();
    }
    public void init(){
        this.width = GamePanel.WIDTH * GamePanel.XSCALE;
        this.height = GamePanel.HEIGHT * GamePanel.YSCALE;
        delay = 15;

    }
    public void draw(Graphics2D g){
        long elapsed = (System.nanoTime() - startTime)/1000000;
        if (elapsed > delay){
            width = (int)(width * 0.8);
            height = (int)(height * 0.8);
            startTime = System.nanoTime();
        }
        g.setColor(Color.BLACK);
        g.fillRect(GamePanel.HEIGHT/2-width/2+50,GamePanel.HEIGHT/2-height/2,width,height);
    }
    public boolean transitionHasPlayed(){
        return width == 0 && height == 0;
    }
}
