/**
 * Класс отвечающий за контроль и выбор состояний игры
 */
package ru.GameState;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class GameStateManager {
	
	public ArrayList<GameState> gameStates; // массив состояний игры
	public static int currentState; // текущее состояние игры
	
	public static final int MENUSTATE = 0;
	public static final int LEVEL_MENU = 1;
	public static final int LEVEL1STATE = 2;

	public GameStateManager() {

		gameStates = new ArrayList<GameState>();
		
		currentState = MENUSTATE;
		gameStates.add(new MenuState(this));
		gameStates.add(new LevelMenuState(this));
		gameStates.add(new Level1State(this));
	}

	// Метод установки состояния игры
	public void setState(int state) {
		currentState = state;
		gameStates.get(currentState).init();
	}

	// Метод обновления
	public void update() {
		gameStates.get(currentState).update();
	}

	// Метод отрисовки
	public void draw(java.awt.Graphics2D g) {

		gameStates.get(currentState).draw(g);
	}


	// Обработка пользовательского ввода
	public void keyPressed(int k) {

		gameStates.get(currentState).keyPressed(k);
	}
	
	public void keyReleased(int k) {

		gameStates.get(currentState).keyReleased(k);
	}

	public void mouseClicked(MouseEvent e){
		gameStates.get(currentState).mouseClicked(e);
	}

}









