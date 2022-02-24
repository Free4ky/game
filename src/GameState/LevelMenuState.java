// Класс состояния меню урвней
package GameState;


import Main.GamePanel;
import TileMap.Background;

import java.awt.*;
import java.awt.event.KeyEvent;

public class LevelMenuState extends GameState{

    private static final int NUM_LEVELS = 5; // Количество уровней
    private static final int OPENED_LEVELS = 1; // Количество открытых уровней

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

    public LevelMenuState(GameStateManager gsm){
        this.gsm = gsm;
        bg = new Background("/Backgrounds/7Ik1.gif",1);
        bg.setVector(0,0);
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
    }

    @Override
    public void update() {}

    // Метод отрисовки на холст
    @Override
    public void draw(Graphics2D g) {

        bg.draw(g);//Отрисовка фона

        int counter = 0; // переменная, необходимая для отрисовки следующей стоки кружков-кнопок
        g.setFont(font);
        for (int i = 0; i < levels.length;i++){
            if(i == currentChoice){
                g.setColor(Color.green); // цвет кнопки зеленый, если пользователь на ней
            }
            else{
                g.setColor(Color.lightGray); // иначе цвет серый
            }
            // обнуление счетчика при достижении значения numInRaw (количество кнопок в строке)
            if (counter / numInRow > 0){
                counter = 0;
            }

            if (i == NUM_LEVELS){ // отрисовка последней кнопки BACK
                g.fillRect(GamePanel.WIDTH - 50,GamePanel.HEIGHT - 30,40,20);
                g.setColor(fontColor);
                g.drawString(levels[i],GamePanel.WIDTH - 47,GamePanel.HEIGHT - 16);
            }
            else{
                g.fillOval(GamePanel.WIDTH/2 - (numInRow/2*(btw)) + btw*counter,y + (i/4)*btw, rad,rad); // центровка кружеов по центру экрана
                g.setColor(fontColor);
                g.drawString(levels[i],GamePanel.WIDTH/2 + 10 - (numInRow/2*(btw)) + btw*counter,y + (i/4)*btw+17);
                counter++;
            }
        }
    }

    public void select(){
        if(currentChoice == NUM_LEVELS){
            gsm.setState(GameStateManager.MENUSTATE); // выбор кнопки BACK возвращает в меню
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
}
