package ru.GameState;

import ru.Entity.Player;
import ru.Main.GamePanel;
import ru.TileMap.Background;
import ru.TileMap.TileMap;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Level1State extends GameState{

    private static int counter = 0;
    private TileMap tileMap;
    private Background bg;

    private Player player;
    private InGameMenu menu;

    public Level1State(GameStateManager gsm){
        this.gsm = gsm;
        init();
    }

    public void stop(){
        if (isPaused){
            if (isStartPause){
                player.saved_x = player.getX();
                player.saved_y = player.getY() - 4;
                isStartPause = false;
            }
            else{
                player.setPosition(player.saved_x, player.saved_y);
                player.animation.setDelay(1000000000);
            }
        }
    }

    @Override
    public void init() {
        isPaused = false;
        isStartPause = false;
        tileMap = new TileMap(30);
        tileMap.loadTiles("/Tilesets/grasstileset.gif");
        tileMap.loadMap("/Maps/level1-1.map");
        tileMap.setPosition(0,0);
        player = new Player(tileMap);
        player.setPosition(100,100);
        bg = new Background("/Backgrounds/grassbg1.gif",0.1);
        menu = new InGameMenu(gsm);
    }

    @Override
    public void update() {
        stop();
        // update player
        player.update();
        tileMap.setPosition(
                GamePanel.WIDTH/2 - player.getX(),
                GamePanel.HEIGHT/2-player.getHeight()
        );
    }

    @Override
    public void draw(Graphics2D g) {
        //draw bg
        bg.draw(g);
        //draw tile map
        tileMap.draw(g);
        // draw the player
        player.draw(g);

        if(isPaused){
            menu.draw(g);
        }
    }

    @Override
    public void keyPressed(int k) {
        if (!isPaused){
            if (k == KeyEvent.VK_LEFT){
                player.setLeft(true);
            }
            if (k == KeyEvent.VK_RIGHT){
                player.setRight(true);
            }
            if (k == KeyEvent.VK_UP){
                player.setUP(true);
            }
            if (k == KeyEvent.VK_DOWN){
                player.setDown(true);
            }
            if (k == KeyEvent.VK_W){
                player.setJumping(true);
            }
            if (k == KeyEvent.VK_E){
                player.setGliding(true);
            }
            if (k == KeyEvent.VK_R){
                player.setScratching();
            }
            if (k == KeyEvent.VK_F){
                player.setFiring();
            }
        }
        else{
            if (k == KeyEvent.VK_UP){
                menu.currentChoice--;
                if(menu.currentChoice == -1) {
                    menu.currentChoice = menu.options.length - 1;
                }
            }
            if (k == KeyEvent.VK_DOWN){
                menu.currentChoice++;
                if(menu.currentChoice == menu.options.length) {
                    menu.currentChoice = 0;
                }
            }
            if(k==KeyEvent.VK_ENTER){
                menu.select();
            }
        }
        if (k==KeyEvent.VK_ESCAPE){
            isPaused = !isPaused;
            isStartPause = true;
        }
    }

    @Override
    public void keyReleased(int k) {
        if (k == KeyEvent.VK_LEFT){
            player.setLeft(false);
        }
        if (k == KeyEvent.VK_RIGHT){
            player.setRight(false);
        }
        if (k == KeyEvent.VK_UP){
            player.setUP(false);
        }
        if (k == KeyEvent.VK_DOWN){
            player.setDown(false);
        }
        if (k == KeyEvent.VK_W){
            player.setJumping(false);
        }
        if (k == KeyEvent.VK_E){
            player.setGliding(false);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        System.out.println(x + ' ' + y);
        for(Rectangle r: menu.buttons){
            if (r.contains(x/GamePanel.SCALE,y/GamePanel.SCALE)){
                int i = (r.y - menu.start_y)/30;
                if (i == menu.currentChoice){
                    menu.select();
                }
                else{
                    menu.currentChoice = i;
                }
            }
        }
    }
}
