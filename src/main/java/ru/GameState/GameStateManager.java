/**
 * Класс отвечающий за контроль и выбор состояний игры
 */
package ru.GameState;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class GameStateManager {
	
	public GameState[] gameStates; // массив состояний игры
	public static final int NUMGAMESTATES = 3;
	public static int currentState; // текущее состояние игры
	
	public static final int MENUSTATE = 0;
	public static final int LEVEL_MENU = 1;
	public static final int LEVEL1STATE = 2;

	public GameStateManager() {

		gameStates = new GameState[NUMGAMESTATES];
		currentState = MENUSTATE;
		loadState(currentState);
	}

	private void loadState(int state){
		if(state == MENUSTATE){
			gameStates[state] = new MenuState(this);
		}
		if(state == LEVEL_MENU){
			gameStates[state] = new LevelMenuState(this);
		}
		if(state == LEVEL1STATE){
			gameStates[state] = new Level1State(this);
		}
	}

	private void unloadState(int state){

		gameStates[state] = null;

	}

	// Метод установки состояния игры
	public void setState(int state) {
		unloadState(currentState);
		currentState = state;
		loadState(currentState);
		//gameStates[currentState].init();
	}

	// Метод обновления
	public void update() {
		try{
			gameStates[currentState].update();
		}
		catch (Exception e){}
	}

	// Метод отрисовки
	public void draw(java.awt.Graphics2D g) {
		try {
			gameStates[currentState].draw(g);
		}
		catch (Exception e){}
	}


	// Обработка пользовательского ввода
	public void keyPressed(int k) {

		gameStates[currentState].keyPressed(k);
	}
	
	public void keyReleased(int k) {

		gameStates[currentState].keyReleased(k);
	}

	public void mouseClicked(MouseEvent e){
		gameStates[currentState].mouseClicked(e);
	}

}









