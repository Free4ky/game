/**
 * Точка запуска приложения
 * @version 0.1
 */

package ru.Main;

import javax.swing.JFrame;

public class Game {
	
	public static void main(String[] args) {
		
		JFrame window = new JFrame("Game"); // устновка названия окна
		window.setContentPane(new GamePanel()); // установка панели GamePanel в качестве содержимого окна window
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // установка операторо выхода
		window.setResizable(false); // запрезаем пользователю изменять размер окна
		window.pack(); // устанавливает минимальный необходимый размер окна для отображения компонентов
		window.setVisible(true); // делаем окно видимым
	}

}