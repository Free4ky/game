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

    public GameOverMenu(GameStateManager gsm){
        super(gsm);
    }

    public void draw(Graphics2D g) {

        // draw menu rect
        g.setColor(Color.BLACK);
        g.drawRect(GamePanel.HEIGHT/2-10,start_y-15,button_width+35,(button_height+space_between)*num_buttons-space_between-10);
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
