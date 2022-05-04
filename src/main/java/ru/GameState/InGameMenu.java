/**
 * Класс отвечающий за внутриигровое меню
 * @author Корпов Даниил
 * @version dev
 */

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
    private int button_width = 65;
    private int button_height = 18;
    private int space_between = 30;
    private int num_buttons;
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
        init();
        this.gsm = gsm;
        //System.out.println(gsm == null);
    }

    public void init(){
        buttons = new ArrayList<>();
        for (int i = 0; i < options.length;i++){
            buttons.add(new Rectangle(GamePanel.WIDTH/2 - 32,start_y+i*space_between,button_width,button_height));
        }
        num_buttons = options.length;
        fontColor = new Color(128,0,0);
        font = new Font("Arial",Font.PLAIN,12);
    }

    public void select(){
        if (currentChoice == 0){
            GameState.isPaused = false;
        }
        if (currentChoice == 1){
            System.out.println(gsm == null);
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
        g.drawRect(GamePanel.HEIGHT/2-10,start_y-30,button_width+35,(button_height+space_between)*num_buttons-space_between);
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

    }

    @Override
    public void keyReleased(int k) {

    }

    @Override
    public void mouseClicked(MouseEvent e){
    }
}
