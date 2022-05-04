/**
 * Класс отвечающий за первый уровень игры
 * @version dev
 */

package ru.GameState;

import ru.Entity.*;
import ru.Entity.Enemies.Slugger;
import ru.Main.Game;
import ru.Main.GamePanel;
import ru.TileMap.Background;
import ru.TileMap.TileMap;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Level1State extends GameState{

    private static int counter = 0;
    private TileMap tileMap;
    private Background bg;

    private Player player;
    private InGameMenu menu;
    private Transition transition;
    private boolean enteredState;
    private ArrayList<Coin> coins;
    private int numCoins;

    private ArrayList<Enemy> enemies;
    private  ArrayList<Explosion> explosions;

    private HUD hud;

    public Level1State(GameStateManager gsm){
        init();
        this.gsm = gsm;
    }

    public void stop(){
        if (isPaused){
            if (isStartPause){
                player.saveState();
                for(Coin c: coins){
                    c.saveState();
                }
                //MapObject.saveState(enemies);
                for(Enemy e: enemies){
                    e.saveState();
                }
                for(FireBall fb: player.getFireBalls()){
                    fb.saveState();
                }
                isStartPause = false;
            }
            else{
                player.uploadState();
                for (Coin c: coins){
                    c.uploadState();
                }
                for(Enemy e: enemies){
                    e.uploadState();
                }
                for(FireBall fb: player.getFireBalls()){
                    fb.uploadState();
                }
            }
        }
    }
    @Override
    public void init() {
        isPaused = false;
        isStartPause = false;
        tileMap = new TileMap(30);
        tileMap.setTween(0.07);
        tileMap.loadTiles("/Tilesets/new_tilemap3-modified1.png");
        tileMap.loadMap("/Maps/level1-2.map");
        tileMap.setPosition(0,0);
        player = new Player(tileMap);
        player.setPosition(100,100);
        bg = new Background("/Backgrounds/grassbg1.gif",0.1);

        menu = new InGameMenu(gsm);
        transition = new Transition();
        enteredState = true;

        numCoins = 5;
        coins = new ArrayList<Coin>();
        Coin c;
        for(int i = 0; i < numCoins; i++){
            c = new Coin(tileMap);
            c.setPosition(i*30, 80);
            coins.add(c);
        }

        // Create enemies
        populateEnemies();


        hud = new HUD(player);

        // Explosions
        explosions = new ArrayList<Explosion>();

    }

    private void populateEnemies(){
        enemies = new ArrayList<Enemy>();
        Slugger s;
        Point[] points = new Point[]{
                new Point(100,100),
                new Point(110,100),
                new Point(120,100)
        };
        for(int i = 0; i < points.length; i++){
            s = new Slugger(tileMap);
            s.setPosition(points[i].x, points[i].y);
            enemies.add(s);
        }
    }

    @Override
    public void update() {
        stop();
        // update player
        player.update();
        for (int i = 0; i < coins.size(); i++){
            coins.get(i).update();
            if(coins.get(i).intersects(player)){
                coins.remove(i);
                player.coinsAmount++;
            }
        }
        //if (player.getY() < tileMap.getHeight()/(GamePanel.SCALE * 2)){
          //  tileMap.setPosition(
            //        GamePanel.WIDTH/2 - player.getX(),
              //      GamePanel.HEIGHT/2-player.getY()
            //);
       // }
        //else{
            //tileMap.setPosition(
                //    GamePanel.WIDTH/2 - player.getX(),
              //      GamePanel.HEIGHT/2-player.getY() + tileMap.getTileSize()*3
            //);
        //}
        tileMap.setPosition(
                GamePanel.WIDTH/2 - player.getX(),
                GamePanel.HEIGHT/2-player.getY() + tileMap.getTileSize()*2
        );

        // set background
        bg.setPosition(tileMap.getX(), 0);

        // attack enemies
        player.checkAttack(enemies);

        //update all enemies
        for(int i = 0; i < enemies.size(); i++){
            Enemy e = enemies.get(i);
            e.update();
            if(e.isDead()){
                explosions.add(
                        new Explosion(e.getX(),e.getY()));
                enemies.remove(i);
                i--;
            }
        }
        // update all explosions
        for(int i = 0; i < explosions.size(); i++){
            Explosion e = explosions.get(i);
            e.update();
            if(e.shouldRemove()){
                explosions.remove(i);
                i--;
            }
        }
    }

    @Override
    public void draw(Graphics2D g) {
        //draw bg
        bg.draw(g);
        //draw tile map
        tileMap.draw(g);

        //draw coins
        for(int i = 0; i < coins.size(); i++){
            coins.get(i).draw(g);
        }

        // draw the player
        player.draw(g);

        // draw all enemies
        for(int i = 0; i < enemies.size(); i++){
            enemies.get(i).draw(g);
        }

        // draw explosions
        for(int i = 0; i < explosions.size(); i++){
            explosions.get(i).setMapPosition(
                    (int)tileMap.getX(), (int)tileMap.getY()
            );
            explosions.get(i).draw(g);
        }

        if(isPaused){
            menu.draw(g);
        }
        if (!transition.transitionHasPlayed()){
            transition.draw(g);
        }

        //draw temporary hud
        g.setColor(Color.BLACK);
        g.drawString(String.valueOf(player.coinsAmount),0,10);

        // draw hud
        hud.draw(g);
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
            // возобновление анимации монет
            if(!isPaused){
                for(Coin c:coins){
                    c.animation.setDelay(400);
                }
                for(Enemy e: enemies){
                    e.animation.setDelay(300);
                }
                for(FireBall fb: player.getFireBalls()){
                    fb.animation.setDelay(70);
                }
            }
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
        for(Rectangle r: menu.buttons){
            if (r.contains(x/GamePanel.SCALE,y/GamePanel.SCALE)){
                int i = (r.y - InGameMenu.start_y)/30;
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
