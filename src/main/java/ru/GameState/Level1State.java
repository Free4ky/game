/**
 * Класс отвечающий за первый уровень игры
 * @version dev
 */

package ru.GameState;

import ru.Audio.AudioPlayer;
import ru.Entity.*;
import ru.Entity.Enemies.Slugger;
import ru.Entity.Enemies.Spike;
import ru.Entity.Enemies.Vase;
import ru.Main.GamePanel;
import ru.TileMap.Background;
import ru.TileMap.TileMap;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Level1State extends GameState{

    private static int counter = 0;
    private TileMap tileMap;
    private Background bg;

    private Player player;

    private InGameMenu menu;
    private GameOverMenu goMenu;
    private boolean gameOver;
    private Transition transition;
    private ArrayList<Coin> coins;
    private int numCoins;

    private ArrayList<Enemy> enemies;
    private ArrayList<Explosion> explosions;
    private ArrayList<PickUp> coinPickUps;

    private ArrayList<Enemy> vases;
    private ArrayList<Supplies> supplies;
    private ArrayList<PickUp> heartPickUps;

    private MovableObject stone;

    private HUD hud;

    // waterFall
    private ArrayList<WaterFall> waterFalls;

    public Level1State(GameStateManager gsm){
        this.gsm = gsm;
        init();
        //System.out.println(this.gsm ==null);
    }

    public ArrayList<Coin> getCoins() {
        return coins;
    }
    public ArrayList<Enemy> getEnemies(){
       return enemies;
    }
    public Player getPlayer(){
        return player;
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
        gameOver = false;
        isPaused = false;
        isStartPause = false;
        tileMap = new TileMap(30);
        tileMap.setTween(0.07);
        //tileMap.loadTiles("/Tilesets/new_tilemap3-modified1.png");
        tileMap.loadTiles("/Tilesets/TileSetNew.png");

        //tileMap.loadMap("/Maps/level1-2.map");
        tileMap.loadMap("/Maps/map.map");

        tileMap.setPosition(0,0);
        player = new Player(tileMap);
        player.setPosition(100,150);
        bg = new Background("/Backgrounds/grassbg1.gif",0.1);

        menu = new InGameMenu(gsm);
        transition = new Transition();

        // create coins
        createCoins(5,600,160);

        // Create enemies
        populateEnemies();

        // create Vases
        createVases(5,100,170);

        // init supplies
        supplies = new ArrayList<Supplies>();
        heartPickUps = new ArrayList<PickUp>();

        // create WaterFall
        createWaterFall(14,450,10);

        hud = new HUD(player);

        // Explosions
        explosions = new ArrayList<Explosion>();

        bgMusic = new AudioPlayer("/Music/level1-1.mp3");
        bgMusic.play();
        bgMusic.loop();

        menu.setBgMusic(bgMusic);

        // try to add movable obj
        stone = new MovableObject(tileMap,player);
        Point p = new Point(100,170);
        stone.setPosition(p.x,p.y);

        //long x = bgMusic.getDuration("/Music/level1-1.mp3");
        //System.out.println(x);

    }

    private void createVases(int num, int x, int y){
        vases = new ArrayList<Enemy>();
        Vase v;
        for(int i = 0; i < num; i++){
            v = new Vase(tileMap);
            v.setPosition(x + i*30,y);
            vases.add(v);
        }
    }

    private void createCoins(int num,int x, int y){
        coinPickUps = new ArrayList<PickUp>();
        numCoins = num;
        Coin.NumCoinsOnLevel[GameStateManager.LEVEL1STATE] = numCoins;
        coins = new ArrayList<Coin>();
        Coin c;
        for(int i = 0; i < numCoins; i++){
            c = new Coin(tileMap);
            c.setPosition(x + i*30, y);
            coins.add(c);
        }
    }

    private void populateEnemies(){
        enemies = new ArrayList<Enemy>();
        Slugger s;
        Point[] points = new Point[]{
                new Point(650,150),
                new Point(670,150),
                new Point(690,150),
                new Point(250,150)
        };
        for(int i = 0; i < points.length - 1; i++){
            s = new Slugger(tileMap);
            s.setPosition(points[i].x, points[i].y);
            enemies.add(s);
        }
        Spike spike = new Spike(tileMap);
        spike.setPosition(points[points.length-1].x,points[points.length-1].y);
        enemies.add(spike);
    }

    public void createWaterFall(int waterFallLength, int x, int y){
        waterFalls = new ArrayList<WaterFall>();
        for(int i = 0; i < waterFallLength; i++){
            WaterFall wf;
            if (i == waterFallLength - 1){
                wf = new WaterFall(tileMap, 0, 1);
                wf.setPosition(x,y + (waterFallLength-2)*waterFalls.get(i-1).getHeight() + wf.getHeight());
            }
            else{
                wf = new WaterFall(tileMap, i % 4, 0);
                wf.setPosition(x,y + i*wf.getHeight());
            }
            waterFalls.add(wf);
        }
    }

    private void checkGameOverState(){
        if (player == null) return;
        if (player.getHealth() == 0){
            if(!gameOver){
                goMenu = new GameOverMenu(gsm);
                gameOver = true;
            }
            if (player!= null && player.animation.hasPlayedOnce()){
                tileMap.savedPlayerX = player.getX();
                tileMap.savedPlayerY = player.getY();
                player = null;
            }
        }
    }

    @Override
    public void update() {


        try{
            stop();
            // update player
            if(player != null){
                player.update();
            }

            checkGameOverState();


            for (int i = 0; i < coins.size(); i++){
                Coin c = coins.get(i);
                c.update();
                if(player != null && c.intersects(player)){
                    c.getCoinEffect().play();
                    coinPickUps.add(new PickUp(c.getX(),c.getY(),PickUp.COIN));
                    coins.remove(i);
                    player.coinsAmount++;
                }
            }
            for(int i = 0; i < coinPickUps.size(); i++){
                PickUp cpu = coinPickUps.get(i);
                cpu.update();
                if(cpu.shouldRemove()){
                    coinPickUps.remove(i);
                    i--;
                }
            }

            if(player != null){
                tileMap.setPosition(
                        GamePanel.WIDTH/2 - player.getX(),
                        GamePanel.HEIGHT/2-player.getY() + tileMap.getTileSize()*2
                );
            }
            else{
                tileMap.setPosition(
                        GamePanel.WIDTH/2 - tileMap.savedPlayerX,
                        GamePanel.HEIGHT/2-tileMap.savedPlayerY + tileMap.getTileSize()*2
                );
            }

            // set background
            bg.setPosition(tileMap.getX(), 0);

            // attack enemies
            if(player != null && !isPaused){
                player.checkAttack(enemies, Enemy.ENEMY);
                player.checkAttack(vases, Enemy.FRIEND);
            }

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

            // update all vases
            for(int i = 0; i < vases.size(); i++){
                Enemy v = vases.get(i);
                v.update();
                if(v.isDead() && v.animation.hasPlayedOnce()){
                    double rand = Math.random(); // случайное число от 0 до 1
                    if (rand < 0.99){
                        Supplies s = new Supplies(tileMap,Supplies.HEART);
                        s.setPosition(v.getX(), v.getY());
                        supplies.add(s);
                    }
                    vases.remove(i);
                    i--;
                }
            }

            for(int i = 0; i < supplies.size(); i++){
                Supplies s = supplies.get(i);
                s.update();
                if(player.intersects(s)){
                    player.increaseHealth(1);
                    PickUp pi = new PickUp(s.getX(),s.getY(),PickUp.HEART);
                    pi.sfx.setVolume(bgMusic.currentVolume);
                    heartPickUps.add(pi);
                    supplies.remove(i);
                    i--;
                }
            }

            // update all heartPickUps
            for(int i = 0; i < heartPickUps.size(); i++){
                PickUp hpu = heartPickUps.get(i);
                hpu.update();
                if(hpu.shouldRemove()){
                    hpu.getSound().play();
                    heartPickUps.remove(i);
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

            //update WaterFall
            for(WaterFall part:waterFalls){
                part.update();
            }
            // update gameover menu
            if (goMenu != null){
                goMenu.update();
            }

            stone.update();

            menu.update();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void draw(Graphics2D g) {
        //draw bg
        bg.draw(g);

        // draw the player
        if(player != null){
            player.draw(g);
        }
        //draw tile map
        tileMap.draw(g);

        //draw coins
        for(int i = 0; i < coins.size(); i++){
            coins.get(i).draw(g);
        }

        // draw all enemies
        for(int i = 0; i < enemies.size(); i++){
            enemies.get(i).draw(g);
        }

        // draw vases
        try {
            for(int i = 0; i < vases.size(); i++){
                vases.get(i).draw(g);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        // draw supplies
        for(int i = 0; i < supplies.size(); i++){
            Supplies s = supplies.get(i);
            s.draw(g);
        }

        // draw explosions
        for(int i = 0; i < explosions.size(); i++){
            explosions.get(i).setMapPosition(
                    (int)tileMap.getX(), (int)tileMap.getY()
            );
            explosions.get(i).draw(g);
        }
        // draw coins pick up
        for(int i = 0; i < coinPickUps.size(); i++){
            coinPickUps.get(i).setMapPosition(
                    (int)tileMap.getX(), (int)tileMap.getY()
            );
            coinPickUps.get(i).draw(g);
        }

        for(int i = 0; i < heartPickUps.size(); i++){
            heartPickUps.get(i).setMapPosition(
                    (int)tileMap.getX(), (int)tileMap.getY()
            );
            heartPickUps.get(i).draw(g);
        }

        stone.draw(g);

        // draw waterFall
        for(WaterFall part: waterFalls){
            part.draw(g);
        }

        if(isPaused){
            menu.draw(g);
        }

        if(goMenu != null){
            goMenu.draw(g);
        }

        if (!transition.transitionHasPlayed()){
            transition.draw(g);
        }

        // draw hud
        hud.draw(g);
    }

    @Override
    public void keyPressed(int k) {
        if (!isPaused && !gameOver){
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
        else if(gameOver){
            if (k == KeyEvent.VK_UP){
                goMenu.currentChoice--;
                if(goMenu.currentChoice == -1){
                    goMenu.currentChoice = goMenu.options.length - 1;
                }
            }
            if (k == KeyEvent.VK_DOWN){
                goMenu.currentChoice++;
                if(goMenu.currentChoice == goMenu.options.length){
                    goMenu.currentChoice = 0;
                }
            }
            if(k==KeyEvent.VK_ENTER){
                goMenu.select();
            }
        }
        else if(isPaused){
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
                menu.select(this);
                if(menu.currentChoice != 0){
                    bgMusic.stop();
                }
            }
            if(k == KeyEvent.VK_LEFT){
                bgMusic.volumeDown();
                for(Enemy e: enemies){
                    e.deathEffect.volumeDown();
                }
                for(Enemy v:vases){
                    v.deathEffect.volumeDown();
                }
                for(AudioPlayer value:player.sfx.values()){
                    value.volumeDown();
                }
                for(Coin c: coins){
                    c.getCoinEffect().volumeDown();
                }
                for(PickUp hpu:heartPickUps){
                    hpu.sfx.volumeDown();
                }
            }
            if(k == KeyEvent.VK_RIGHT){
                bgMusic.volumeUp();
                for(Enemy e: enemies){
                    e.deathEffect.volumeUp();
                }
                for(Enemy v:vases){
                    v.deathEffect.volumeUp();
                }
                for(AudioPlayer value:player.sfx.values()){
                    value.volumeUp();
                }
                for(Coin c: coins){
                    c.getCoinEffect().volumeUp();
                }
                for(PickUp hpu:heartPickUps){
                    hpu.sfx.volumeUp();
                }
            }
        }
        if (k==KeyEvent.VK_ESCAPE){
            if(!gameOver){
                isPaused = !isPaused;
                isStartPause = true;
                // возобновление анимации монет
                if(!isPaused){
                    player.animation.setDelay(400);
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
    }

    @Override
    public void keyReleased(int k) {
        if(player != null){
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
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if(isPaused){
            for(Rectangle r: menu.buttons){
                if (
                        x < (int)((r.x+r.width)*GamePanel.XSCALE2) &&
                        x > (int)((r.x)*GamePanel.XSCALE2) &&
                        y < (int)((r.y+r.height)*GamePanel.YSCALE2) &&
                        y > (int)((r.y)*GamePanel.YSCALE2)
                ){
                    int i = (r.y - InGameMenu.start_y)/30;
                    if (i == menu.currentChoice){
                        menu.select(this);
                    }
                    else{
                        menu.currentChoice = i;
                    }
                }
            }
        }
        if(gameOver){
            for(Rectangle r: goMenu.buttons){
                if (
                        x < (int)((r.x+r.width)*GamePanel.XSCALE2) &&
                        x > (int)((r.x)*GamePanel.XSCALE2) &&
                        y < (int)((r.y+r.height)*GamePanel.YSCALE2) &&
                        y > (int)((r.y)*GamePanel.YSCALE2)
                ){
                    int i = (r.y - InGameMenu.start_y)/30;
                    if (i == goMenu.currentChoice){
                        goMenu.select();
                    }
                    else{
                        goMenu.currentChoice = i;
                    }
                }
            }
        }
    }
}
