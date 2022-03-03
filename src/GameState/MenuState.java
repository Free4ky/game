package GameState;

import Main.Game;
import Main.GamePanel;
import TileMap.Background;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class MenuState extends GameState {
	
	private Background bg;
	
	private int currentChoice = 0;
	private String[] options = {
		"Start",
		"Options",
		"Help",
		"Quit"
	};


	private JButton jbt;


	private Color titleColor;
	private Font titleFont;

	private Font font;

	
	public MenuState(GameStateManager gsm) {
		
		this.gsm = gsm;
		
		try {
			bg = new Background("/Backgrounds/11.gif", 1);
			bg.setVector(0, 0);
			
			titleColor = new Color(0, 0, 0);
			titleFont = new Font(
					"Century Gothic",
					Font.BOLD,
					34);

			font = new Font("Arial", Font.PLAIN, 12);

		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void init() {}
	
	public void update() {
		bg.update();
	}
	
	public void draw(Graphics2D g) {
		
		// draw bg
		bg.draw(g);
		
		// draw title
		g.setColor(titleColor);
		g.setFont(titleFont);

		int length = stringLength("Dragon Survival",g);
		g.drawString("Dragon Survival", GamePanel.WIDTH/2 - length/2, 50);

		// draw menu options
		g.setFont(font);

		for(int i = 0; i < options.length; i++){
			if (currentChoice == i){
				g.setColor(Color.GREEN);
				g.fillRect(GamePanel.WIDTH/2 - 30,125 + i * 20,60,18);
			}
			else {
				g.setColor(Color.yellow);
				g.fillRect(GamePanel.WIDTH / 2 - 30, 125 + i * 20, 60, 18);
			}
			g.setColor(Color.black);
			g.drawRect(GamePanel.WIDTH/2 - 30,125 + i * 20,60,18);
		}

		for(int i = 0; i < options.length; i++) {

			//g.fillRect(130, 190, 80, 25);


			if(i == currentChoice) {
				g.setColor(Color.black);
			}
			else {
				g.setColor(Color.RED);
			}
			length = stringLength(options[i],g);
			g.drawString(options[i], GamePanel.WIDTH/2 - length/2, 140 + i * 20);
		}
		
	}
	
	private void select() {
		if(currentChoice == 0) {
			gsm.setState(GameStateManager.LEVEL_MENU);
		}
		if(currentChoice == 1) {
			// help
		}
		if(currentChoice == 2) {
			// help
		}
		if(currentChoice == 3) {
			System.exit(0);
		}
	}
	
	public void keyPressed(int k) {
		if(k == KeyEvent.VK_ENTER){
			select();
		}
		if(k == KeyEvent.VK_UP) {
			currentChoice--;
			if(currentChoice == -1) {
				currentChoice = options.length - 1;
			}
		}
		if(k == KeyEvent.VK_DOWN) {
			currentChoice++;
			if(currentChoice == options.length) {
				currentChoice = 0;
			}
		}
	}
	public void keyReleased(int k) {}

	// Функция возвращающая длину строки в пикселях
	// Необходима для центровки текста
	public static int stringLength(String s, Graphics2D g){
		return (int) g.getFontMetrics().getStringBounds(s,g).getWidth();
	}
}










