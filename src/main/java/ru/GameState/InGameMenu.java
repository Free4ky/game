package ru.GameState;

import ru.Main.Game;
import ru.Main.GamePanel;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class InGameMenu extends GameState{

    private Color fontColor;
    private Font font;

    public ArrayList<Rectangle> buttons;
    private GameStateManager gsm;

    private String name = "Options";

    public int currentChoice = 0;

    public String[] options = {
            "Continue",
            "Levels",
            "Main menu"
    };
    public static final int start_y = GamePanel.WIDTH/2 - (18+30)*3/2;

    public InGameMenu(GameStateManager gsm){
        this.gsm = gsm;
        init();
    }

    public void init(){
        buttons = new ArrayList<>();
        for (int i = 0; i < options.length;i++){
            buttons.add(new Rectangle(GamePanel.WIDTH/2 - 32,start_y+i*30,65,18));
        }
        fontColor = new Color(128,0,0);
        font = new Font("Arial",Font.PLAIN,12);
    }

    public void select(){
        if (currentChoice == 0){
            GameState.isPaused = false;
        }
        if (currentChoice == 1){
            gsm.setState(GameStateManager.LEVEL_MENU);
        }
        if (currentChoice == 2){
            gsm.setState(GameStateManager.MENUSTATE);
        }
    }

    @Override
    public void draw(Graphics2D g) {
        int nameLen = MenuState.stringLength(name,g);
        g.setColor(fontColor);
        g.setFont(font);
        g.drawString(name, GamePanel.WIDTH/2-nameLen/2,start_y-10);
        // draw menu rect
        g.setColor(Color.BLACK);
        g.drawRect(GamePanel.HEIGHT/2-10,start_y-30,100,120);
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

    @Override
    public void update() {

    }

    @Override
    public void keyPressed(int k) {
        if(k == KeyEvent.VK_ENTER){
            select();
        }
    }

    @Override
    public void keyReleased(int k) {

    }

    @Override
    public void mouseClicked(MouseEvent e){
    }
}
