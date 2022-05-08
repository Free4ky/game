package ru.GameState;

import ru.Main.GamePanel;

import java.awt.*;
import java.util.ArrayList;

public class GameOverMenu extends InGameMenu{

    public String[] options = {
      "Try again",
      "Levels",
      "Main menu"
    };

    public String gameover;
    private long timer;
    private int gameoverX;
    private int gameoverY;
    private int alpha;
    private int length;

    public GameOverMenu(GameStateManager gsm){
        super(gsm);
        gameover = "G A M E  O V E R";
        timer = System.nanoTime();
        gameoverX = -1;
        gameoverY = GamePanel.HEIGHT/2 - 50;
    }

    public void draw(Graphics2D g) {

        if(gameoverX == -1){
            length = MenuState.stringLength(gameover, g);
            gameoverX = GamePanel.WIDTH/2 - length/2;
        }

        // draw menu rect
        g.setColor(Color.BLACK);
        g.drawRect(GamePanel.HEIGHT/2-10,start_y-35,button_width+35,(button_height+space_between)*num_buttons-space_between + 15);
        int cnt = 0;
        for(Rectangle r: buttons){
            if (currentChoice == cnt){
                g.setColor(Color.green);
            }
            else{
                g.setColor(Color.yellow);
            }
            g.fillRect(r.x,r.y,r.width,r.height);
            g.setColor(Color.BLACK);
            g.drawRect(r.x,r.y,r.width,r.height);
            cnt++;
        }
        // отрисовка названий кнопок

        for(int i = 0; i < options.length; i++){
            if(currentChoice == i){
                g.setColor(Color.BLACK);
            }
            else{
                g.setColor(fontColor);
            }
            int len = MenuState.stringLength(options[i],g);
            g.drawString(options[i],GamePanel.WIDTH/2-len/2,start_y+i*30 + 15);
        }

        g.setColor(new Color(0,0,0,alpha));
        g.drawString(gameover,gameoverX,gameoverY);

    }

    public void update(){
        long elapsed = (System.nanoTime() - timer)/1000000;
        alpha = (int)(255*Math.sin(3.14*elapsed/1000)); // прозрачность
        if (alpha >= 255) {
            alpha = 250;
        }
        else if (alpha < 0){
            alpha = 0;
        }
    }

    public void select() {
        if (currentChoice == 0){
            gsm.gameStates[GameStateManager.currentState].bgMusic.stop();
            gsm.setState(GameStateManager.currentState);
        }
        if(currentChoice == 1){
            gsm.gameStates[GameStateManager.currentState].bgMusic.stop();
            gsm.setState(GameStateManager.LEVEL_MENU);
        }
        if(currentChoice == 2){
            gsm.gameStates[GameStateManager.currentState].bgMusic.stop();
            gsm.setState(GameStateManager.MENUSTATE);
        }

    }
}
