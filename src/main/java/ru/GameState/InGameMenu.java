/**
 * Класс отвечающий за внутриигровое меню
 * @author Корпов Даниил
 * @version dev
 */

package ru.GameState;

import ru.Audio.AudioPlayer;
import ru.Entity.Coin;
import ru.Entity.Enemy;
import ru.Entity.FireBall;
import ru.Main.Game;
import ru.Main.GamePanel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class InGameMenu extends GameState{

    protected Color fontColor;
    protected Font font;
    private Font percentFont;

    public ArrayList<Rectangle> buttons;
    protected int button_width = 65;
    protected int button_height = 18;
    protected int space_between = 30;
    protected int num_buttons;
    protected GameStateManager gsm;

    protected String name = "Options";
    private String musicVolume = "Sounds volume";

    public int currentChoice = 0;

    public String[] options = {
            "Continue",
            "Levels",
            "Main menu"
    };
    public static final int start_y = GamePanel.WIDTH/2 - (18+30)*3/2;

    private Rectangle[] slider;

    private int sliderY;
    public int maxVolume = button_width;

    public int percent = 0;

    public InGameMenu(GameStateManager gsm){
        init();
        this.gsm = gsm;
        //System.out.println(gsm == null);
    }

    public void setBgMusic(AudioPlayer bgMusic){
        this.bgMusic = bgMusic;
    }

    public void createSlider(){
        int railW = maxVolume;
        int railH = 2;
        slider = new Rectangle[2];
        sliderY = buttons.get(buttons.size() - 1).y + space_between + font.getSize();
        slider[0] = new Rectangle(GamePanel.WIDTH/2-button_width/2-10, sliderY,railW,railH);

        int cursorW = 8;
        int cursorH = 7;
        slider[1] = new Rectangle(GamePanel.WIDTH/2-button_width/2,sliderY - cursorH/2,cursorW,cursorH);
    }

    public void init(){
        buttons = new ArrayList<>();
        for (int i = 0; i < options.length;i++){
            buttons.add(new Rectangle(GamePanel.WIDTH/2 - button_width/2,start_y+i*space_between,button_width,button_height));
        }
        num_buttons = options.length;
        fontColor = new Color(128,0,0);
        font = new Font("Arial",Font.PLAIN,12);
        percentFont = new Font("Arial",Font.PLAIN,9);
        createSlider();
    }

    public void select(GameState gs){
        if (currentChoice == 0){
            if (GameStateManager.currentState == GameStateManager.LEVEL1STATE){
                Level1State gs1 = (Level1State) gs;
                GameState.isPaused = false;
                for(Coin c: gs1.getCoins()){
                    c.animation.setDelay(200);
                }
                for(Enemy e: gs1.getEnemies()){
                    e.animation.setDelay(300);
                }
                for(FireBall fb: gs1.getPlayer().getFireBalls()){
                    fb.animation.setDelay(70);
                }
                gs1.getPlayer().animation.setDelay(400);
            }

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
        g.drawRect(
                GamePanel.HEIGHT/2-button_width/2+20,
                start_y-30,
                button_width+40,
                (button_height+space_between)*num_buttons);
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

        for(int i = 0; i < slider.length; i++){
            switch (i){
                case 0:
                    g.setColor(Color.BLACK);
                    break;
                case 1:
                    g.setColor(fontColor);
                    break;
            }
            g.fillRect(slider[i].x,slider[i].y,slider[i].width, slider[i].height);
        }
        g.setColor(Color.BLACK);
        g.setFont(font);
        int len = MenuState.stringLength(musicVolume,g);
        g.drawString(musicVolume,GamePanel.WIDTH/2 - len/2, sliderY - font.getSize()/2);
        g.setFont(percentFont);
        g.drawString(percent + "%",slider[0].x + slider[0].width + 8, slider[1].y + slider[1].height);
    }


    @Override
    public void update() {
        float volume = bgMusic.currentVolume - AudioPlayer.MIN_VOLUME;
        int nextCursorPosition = slider[0].x + (int)(volume * maxVolume/(AudioPlayer.MAX_VOLUME - AudioPlayer.MIN_VOLUME));
        slider[1].x = nextCursorPosition;
        percent = (int)(volume * 100/(AudioPlayer.MAX_VOLUME - AudioPlayer.MIN_VOLUME));
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
