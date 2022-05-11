/**
 * Точка запуска приложения
 * @version 0.1
 */

package ru.Main;

import javax.swing.JFrame;
import java.awt.*;

public class Game {

	public static int WIDTH;
	public static int HEIGHT;

	public static void main(String[] args) {

		GamePanel gp = new GamePanel();
		JFrame window = new JFrame("Game"); // устновка названия окна
		setMaxScreenSize(true);
		gp.setDim(WIDTH, HEIGHT);
		window.setContentPane(gp); // установка панели GamePanel в качестве содержимого окна window
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // установка операторо выхода
		window.setResizable(false); // запрезаем пользователю изменять размер окна;
		window.pack(); // устанавливает минимальный необходимый размер окна для отображения компонентов
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		window.setLocation(dim.width/2-window.getWidth()/2, dim.height/2-window.getHeight()/2);
		window.setVisible(true); // делаем окно видимым
	}

	public static void setMaxScreenSize(boolean b){
		JFrame frame = new JFrame();
		if(b){
			frame.setUndecorated(false);
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
			frame.setVisible(true);
			//frame.pack();
			WIDTH = frame.getWidth();
			HEIGHT = frame.getHeight();
			frame.dispose();
			//frame.setExtendedState(JFrame.NORMAL);
			System.out.println(WIDTH + " " + HEIGHT);
		}
	}
}