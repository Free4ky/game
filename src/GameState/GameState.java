// Cупер класс для всех состояний игры.
// Содержит главные методы для всех состояний и экземпляр класса управляющего состояниями
package GameState;

import java.awt.*;
import java.awt.event.MouseEvent;

public abstract class GameState {
	
	protected GameStateManager gsm;
	
	public abstract void init();
	public abstract void update();
	public abstract void draw(Graphics2D g);
	public abstract void keyPressed(int k);
	public abstract void keyReleased(int k);


	public abstract void mouseClicked(MouseEvent e);

}
