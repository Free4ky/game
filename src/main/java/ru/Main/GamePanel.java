/**
 * Класс GamePanel является ключевым в проекте. В нем происходит главный цикл игры (game loop)
 * обновление, отрисовка всех элементов игры.
 * @version dev
 */
package ru.Main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;

import javax.swing.JPanel;

import ru.GameState.GameStateManager;

public class GamePanel extends JPanel implements Runnable, KeyListener, MouseListener {

	// Размерности
	public static int WIDTH = 320;
	public static int HEIGHT = 240;

	public static int WIDTH_FULL = WIDTH;
	public static int HEIGHT_FULL = HEIGHT;

	public static int XSCALE = 4;
	public static int YSCALE = 4;

	public static double XSCALE2 = XSCALE;
	public static double YSCALE2 = YSCALE;

	// Игровой поток (thread)
	// нужен для регулировния скорости игры
	// Скорость зависит от производительности компьютера, если её не регулировать, будет слишком быстро
	private Thread thread;
	private boolean running;
	private int FPS = 70;
	private long targetTime = 1000 / FPS;

	// image
	private BufferedImage image;
	private Graphics2D g;

	// Класс управления состояниями игры
	private GameStateManager gsm;

	public GamePanel() {
		super();
		setPreferredSize(new Dimension(WIDTH * XSCALE, HEIGHT * YSCALE));
		setFocusable(true); // позволяет фокусироваться на объекте GamePanel. Необходимо для обработки пользовательского ввода
		requestFocus(); // получает фокус
		//adjustDim();
	}

	public void setFullScreen(){
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		gd.setFullScreenWindow(Game.window);
		WIDTH_FULL = Game.window.getWidth();
		HEIGHT_FULL = Game.window.getHeight();
		XSCALE = WIDTH_FULL / WIDTH;
		YSCALE = HEIGHT_FULL / HEIGHT;

		XSCALE2 = (double)WIDTH_FULL / WIDTH;
		YSCALE2 = (double) HEIGHT_FULL / HEIGHT;
	}

	public void setDim(int w, int h) {
		XSCALE = w / WIDTH;
		YSCALE = h / HEIGHT;
		setPreferredSize(new Dimension(WIDTH * XSCALE, HEIGHT * YSCALE));
		setFocusable(true); // позволяет фокусироваться на объекте GamePanel. Необходимо для обработки пользовательского ввода
		requestFocus(); // получает фокус
		System.out.println(XSCALE + " " + YSCALE);
	}

	// addNotify - Делает этот компонент JPanel отображаемым, подключая его к собственному экранному ресурсу.
	public void addNotify() {
		super.addNotify();
		if (thread == null) {
			thread = new Thread(this); // Создание потока с передачей в него экземпляра GamePanel
			addMouseListener(this);
			addKeyListener(this); // Добавляем ввод с клавиатуры
			thread.start(); // Запуск потока с передачей в него экземпляра GamePanel
		}
	}

	// Метод инициализации
	private void init() {
		// Создания холста
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		//Создание пера для холста image
		g = (Graphics2D) image.getGraphics();
		gsm = new GameStateManager();
		running = true;

		try {
			setFullScreen();
		}
		catch (Exception e){
			e.printStackTrace();
		}

	}

	// Запуск задачи потока
	public void run() {

		init();

		long start;
		long elapsed;
		long wait;

		// game loop
		while (running) {

			start = System.nanoTime();

			update();
			draw();
			drawToScreen();

			elapsed = System.nanoTime() - start;

			// переменная wait отвечает за то время, которое нужно подождать потоку
			// прежде чем запускать задачу потока снова
			// Это позволяет регулировать скорость игры
			wait = targetTime - elapsed / 1000000;
			if (wait < 0) wait = 5;
			try {
				Thread.sleep(wait);
			} catch (Exception e) {
				e.printStackTrace(); // e.pritStackTrace() выводит стек ошибок. Удобно для отладки проекта
			}
		}

	}

	// Метод обновления компонентов игры
	private void update() {

		gsm.update();
	}

	// Метод отрисовки компонентов игры.
	// Компоненты игры сначала отрисовываются на холст image в методе draw
	private void draw() {

		gsm.draw(g);
	}

	// Этот метод отвечает уже за отрисовку холста на экран
	// Такой подход называется double buffering (двойная буферизация)
	// Это позволяет снизить блики, мерцания и нечеткости итоговой картинки
	private void drawToScreen() {
		Graphics g2 = getGraphics();
		//g2.drawImage(image, 0, 0, (int)(WIDTH * XSCALE/(RATIO - 0.04)), (int)(HEIGHT * YSCALE/(RATIO + 0.05)), null);
		g2.drawImage(image, 0, 0, WIDTH_FULL, HEIGHT_FULL, null);

		g2.dispose();
	}

	// Методы обработки пользовательского ввода с клавиатуры
	public void keyTyped(KeyEvent key) {
	}

	public void keyPressed(KeyEvent key) {

		gsm.keyPressed(key.getKeyCode());
	}

	public void keyReleased(KeyEvent key) {

		gsm.keyReleased(key.getKeyCode());
	}

	public void mouseClicked(MouseEvent e) {
		gsm.mouseClicked(e);
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public static int[] getDim() {
		int[] dim = new int[2];

		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		dim[0] = gd.getDisplayMode().getWidth();
		dim[1] = gd.getDisplayMode().getHeight();
		return dim;
	}
}













