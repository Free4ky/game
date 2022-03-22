// Класс отвечающий за фон в меню и в будущем в самой игре
package ru.TileMap;

import ru.GameState.GameStateManager;
import ru.Main.GamePanel;

import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Background {
	
	private BufferedImage image;
	private Image icon;

	private double x;
	private double y;
	private double dx;
	private double dy;
	
	private double moveScale;
	
	public Background(String s, double ms) {
		
		try {
			// Папка Resources помечена как Resource root, что делает файлы в ней скомпилированными
			// и готовыми получения как на строках ниже
			image = ImageIO.read(getClass().getResourceAsStream(s)); // для изображений
			icon = new ImageIcon(getClass().getResource(s)).getImage();  // для гифок
			moveScale = ms; // Создает паралакс эффект
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setPosition(double x, double y) {
		this.x = (x * moveScale) % GamePanel.WIDTH;
		this.y = (y * moveScale) % GamePanel.HEIGHT;
	}
	
	public void setVector(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}

	// Метод обновления
	public void update() {
		x += dx;
		y += dy;
	}

	// Метод отрисовки на холст
	public void draw(Graphics2D g) {

		if (GameStateManager.currentState == GameStateManager.MENUSTATE ||
				GameStateManager.currentState == GameStateManager.LEVEL_MENU){
			g.drawImage(icon,(int)x,(int)y,null);
		}
		else{
			g.drawImage(image,(int)x,(int)y,null);
		}
	}
}