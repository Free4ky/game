package ru.Entity;

import ru.GameState.GameStateManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class HUD {

    private Player player;
    private BufferedImage image;
    private Font font;

    private BufferedImage healthBar;
    private BufferedImage fireBar;
    private BufferedImage coinBar;

    private int maxWidth;


    public HUD(Player p){

        maxWidth = 68;

        player = p;
        try {
            healthBar = ImageIO.read(getClass().getResourceAsStream("/HUD/HealthBarT.png"));
            fireBar = ImageIO.read(getClass().getResourceAsStream("/HUD/FlameBarT.png"));
            coinBar = ImageIO.read(getClass().getResourceAsStream("/HUD/CoinBarT.png"));
            font = new Font("Arial",Font.PLAIN, 12);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
    public void draw(Graphics2D g){

        g.setColor(Color.GREEN);
        double width = (double)player.getHealth()/(double) player.getMaxHealth() * maxWidth;
        g.fillRect(0,12,(int)width,15);

        if(player.getHealth() == player.getMaxHealth()){
            g.fillOval(58,12,15,15);
        }

        g.setColor(new Color(178,34,34));
        width = (double) player.getFire()/(double)player.getMaxFire() * maxWidth;
        g.fillRect(0,32,(int)width,15);

        if(player.getMaxFire() == player.getFire()){
            g.fillOval(58,32,15,15);
        }

        g.setColor(new Color(255,215,0));
        width = (double)player.coinsAmount/(double)Coin.NumCoinsOnLevel[GameStateManager.currentState] * maxWidth;
        g.fillRect(0,52,(int)width,15);

        if (player.coinsAmount == Coin.NumCoinsOnLevel[GameStateManager.currentState]){
            g.fillOval(58,52,15,15);
        }

        g.drawImage(healthBar, 0, 10,null);
        g.drawImage(fireBar, 0, 30,null);
        g.drawImage(coinBar, 0,50, null);


        g.setColor(Color.WHITE);
        g.setFont(font);
        g.drawString(
                player.getHealth() + "/" + player.getMaxHealth(),
                30,
                25
                );
        g.drawString(
                player.getFire() / 100 + "/" + player.getMaxFire() / 100,
                30,
                45);

        g.drawString(
                player.coinsAmount + "/" + Coin.NumCoinsOnLevel[GameStateManager.currentState],
                30,
                65);
    }
}
