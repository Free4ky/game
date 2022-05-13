/**
 * Класс состояния меню урвней
 * @author Карпов Даниил, Иляс Гочмурадов
 * @version dev
 */
package ru.GameState;

import ru.Main.GamePanel;
import ru.TileMap.Background;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class LevelMenuState extends GameState{

    private static final int NUM_LEVELS = 10; // Количество уровней
    private static int OPENED_LEVELS = 1; // Количество открытых уровней

    private static final int numInRow = 4; // Количество уровней в строке (отрисованных кружков)
    private static final int rad = 25; // радиус кружка
    private static final int btw = 30; // расстояние между кружками (от центров)
    private static int y = 50; // начальная координата y для отрисовки кружков

    private String[] levels; // массив строк уровней
    private boolean[] levelStatus; // массив логических значений true - уровень открыт, false - закрыт
    private int currentChoice = 0; // текущий уровень в меню
    private Font font; // стиль текста
    //private Font blockedLevelFont;
    private Color fontColor; // цвет текста

    private Background bg; // фон для меню уровней

    private ArrayList<Coords> buttons;
    private Rectangle back_button;
    private BufferedImage lock;

    private Transition transition;

    private int xp, yp = 0;

    public LevelMenuState(GameStateManager gsm){
        init();
        this.gsm = gsm;
        bg = new Background("/Backgrounds/11.gif",1);
        bg.setVector(0,0);
        try{
            lock = ImageIO.read(getClass().getResourceAsStream("/Backgrounds/lock.png"));
            lock = GameState.resize(lock,rad/2,rad/2);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    // Инициализация основной логики меню уровней
    public void init() {
        levelStatus = new boolean[NUM_LEVELS + 1];
        levels = new String[NUM_LEVELS + 1];
        for (int i = 0; i < OPENED_LEVELS; i++){
            levelStatus[i] = true;
        }

        levelStatus[NUM_LEVELS] = true;

        for (int i = 0; i < NUM_LEVELS + 1; i++){
            if (levelStatus[i]){
                levels[i] = String.valueOf(i+1);
            }
            else{
                levels[i] = "X";
            }
            if(i == NUM_LEVELS){
                levels[i]="BACK";
            }
        }

        // инициализация картинки замка

        buttons = new ArrayList<Coords>();
        back_button = new Rectangle(GamePanel.WIDTH - 50,GamePanel.HEIGHT - 30,40,20);
        int counter = 0;
        for (int i = 0; i < NUM_LEVELS; i++){
            // обнуление счетчика при достижении значения numInRaw (количество кнопок в строке)
            if (counter / numInRow > 0){
                counter = 0;
            }
            buttons.add(new Coords(GamePanel.WIDTH/2 - (numInRow/2*(btw)) + btw*counter + rad/2,y + (i/4)*btw + rad /2));
            counter++;
        }
        try{
            //init bg
            //init font
            fontColor = new Color(128,0,0);
            font = new Font("Arial",Font.PLAIN,12);
            //blockedLevelFont = new Font("Arial",Font.PLAIN,8);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        transition = new Transition();
    }

    @Override
    public void update() {}

    // Метод отрисовки на холст
    @Override
    public void draw(Graphics2D g) {

        //Отрисовка фона
        bg.draw(g);
        //Отрисовка замка
        int counter = 0; // переменная, необходимая для отрисовки следующей стоки кружков-кнопок
        g.setFont(font);
        for (int i = 0; i < levels.length;i++){
            if (counter / numInRow > 0){
                counter = 0;
            }
            if(i == currentChoice && i < OPENED_LEVELS){
                g.setColor(Color.green); // цвет кнопки зеленый, если пользователь на ней
            }
            else if (i == currentChoice && i >= OPENED_LEVELS){
                g.setColor(Color.red); // цвет кнопки красный, если пользователь на ней и уровень не открыт
            }
            else {
                g.setColor(Color.black); // иначе цвет серый
            }

            if (i == NUM_LEVELS){ // отрисовка последней кнопки BACK
                g.setColor(Color.yellow);
                if (i == currentChoice){
                    g.setColor(Color.GREEN);
                }
                g.fillRect(GamePanel.WIDTH - 50,GamePanel.HEIGHT - 30,40,20);
                g.setColor(Color.BLACK);
                g.drawRect(back_button.x,back_button.y,back_button.width,back_button.height);
                if(currentChoice == NUM_LEVELS){
                    g.setColor(Color.BLACK);
                }
                else{
                    g.setColor(fontColor);
                }
                g.drawString(levels[i],GamePanel.WIDTH - 47,GamePanel.HEIGHT - 16);
            }
            else{
                g.fillOval(buttons.get(i).getX() - rad/2,buttons.get(i).getY() - rad/2, rad,rad); // центровка кружеов по центру экрана
                g.setColor(Color.yellow);
                g.fillOval(buttons.get(i).getX() + 1 - rad/2,buttons.get(i).getY() + 1 - rad/2, rad-2,rad-2);
                if(currentChoice == i){
                    g.setColor(Color.BLACK);
                }
                else{
                    g.setColor(fontColor);
                }
                // Отрисовка замочков
                if (levels[i] == "X"){
                    g.drawImage(lock,
                            GamePanel.WIDTH/2 + rad/3 - 1 - (numInRow/2*(btw)) + btw*counter,
                            y + (i/4)*btw + rad/3 - 1,
                            null);
                }
                else{
                    g.drawString(levels[i],
                            GamePanel.WIDTH/2 + 10 - (numInRow/2*(btw)) + btw*counter,
                            y + (i/4)*btw+17);
                }
            }
            counter++;
        }
        if(!transition.transitionHasPlayed()){
            transition.draw(g);
        }
    }

    public void select(){
        if(currentChoice == NUM_LEVELS){
            gsm.setState(GameStateManager.MENUSTATE); // выбор кнопки BACK возвращает в меню
        }
        else if(!levelStatus[currentChoice]){
            //nothing happens if current level locked
        }
        else{
            gsm.setState(currentChoice+2); // остальные кнопки соответствуют уровням
        }
    }

    // обработка нажатий клавиатуры
    public void keyPressed(int k) {
        if (k == KeyEvent.VK_ENTER){
            select(); // выбор при нажатии enter
        }
        // перемещение по списку при нажатии стрелок
        if (k == KeyEvent.VK_LEFT){
            currentChoice--;
            if (currentChoice == -1){
                currentChoice = levels.length -1;
            }
        }
        if (k == KeyEvent.VK_RIGHT){
            currentChoice++;
            if (currentChoice == levels.length){
                currentChoice = 0;
            }
        }
    }

    @Override
    public void keyReleased(int k) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int ex = e.getX();
        int ey = e.getY();
        for(Coords c: buttons){
            //делим на scale, чтобы получить достоверные координаты
            if(
                    ex < (int)((c.x + rad/2)*GamePanel.XSCALE2) &&
                    ex > (int)((c.x - rad/2)*GamePanel.XSCALE2) &&
                    ey < (int)((c.y + rad/2)*GamePanel.YSCALE2) &&
                    ey > (int)((c.y - rad/2) * GamePanel.YSCALE2)
            ){
                int row = (c.getY() - y) / btw;
                int col = numInRow - 1 + (c.getX() - GamePanel.WIDTH/2- (numInRow/2*btw))/btw;
                //System.out.println(row + " " + col);
                int index = row*numInRow + col;
                if(index == currentChoice){
                    select();
                }
                else{
                    currentChoice = index;
                }
            }
        }
        // for back button
        if (
            ex < (int)((back_button.x + back_button.width) * GamePanel.XSCALE2) &&
            ex > (int)((back_button.x) * GamePanel.XSCALE2) &&
            ey < (int)((back_button.y + back_button.height) * GamePanel.YSCALE2) &&
            ey > (int)((back_button.y)*GamePanel.YSCALE2)
        ){
            if (currentChoice == NUM_LEVELS){
                select();
            }
            else{
                currentChoice = NUM_LEVELS;
            }
        }
    }

    // Класс координат кнопок
    class Coords{
        private int x;
        private int y;

        public Coords(int x, int y){
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int distance(int x, int y){
            return (int)Math.sqrt(Math.pow((this.x - x),2) + Math.pow((this.y - y),2));
        }
    }
}
