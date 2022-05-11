package ru.Entity;

import ru.Audio.AudioPlayer;
import ru.GameState.GameStateManager;
import ru.TileMap.TileMap;

public class Enemy extends MapObject{

    protected int health;
    protected int maxHealth;
    protected boolean dead;
    protected boolean startDeathAnimation;
    protected int damage;

    protected boolean flinching;
    protected long flinchTimer;

    public static final int ENEMY = 0;
    public static final int FRIEND = 1;

    public AudioPlayer deathEffect;

    public Enemy(TileMap tm){
        super(tm);
    }

    public AudioPlayer getDeathEffect() {
        return deathEffect;
    }

    public boolean isDead() {return dead;}
    public int getDamage(){return damage;}

    public void hit(int damage){
        if (dead || flinching) return;
        health -= damage;
        if(health < 0) health = 0;
        if(health == 0){
            dead = true;
            //здесь быстрее проигрывается звук
            deathEffect.play();
        }
        flinching = true;
        flinchTimer = System.nanoTime();

    }
    public void update(){}
}