package userInterface;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import gameobjects.GameWorld;

public class GamePanel extends JPanel implements Runnable, KeyListener {
	private Thread thread;
	private InputManager inputManager;
	private GameWorld gameWorld;

	public GamePanel() {
		gameWorld = new GameWorld();
		inputManager = new InputManager(gameWorld);
	}

	@Override
//	 paint là phương thức tự động gọi khi add Panel này vào
	public void paint(Graphics g) {
		/*
		 * Ko cần super.draw(g) nữa vì bufferedImage có kích thước to bằng panel nên ko
		 * sợ vẫn còn các hình ảnh lúc trước chưa được xoá
		 */
		g.drawImage(gameWorld.getBufferedImage(), 0, 0, this);
	}

	public void startGame() {
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	@Override
	public void run() {
		long FPS = 80; // 80 frame/s
		long period = 1000000000 / FPS;
		/*
		 * period: số chu kì của 1 frame = 1/80 (s) = 1e9/80 (ns)
		 */
		long beginTime = System.nanoTime();
		while (true) {
			gameWorld.Update();
			gameWorld.Render();
			repaint();

			long deltaTime = System.nanoTime() - beginTime;
			long sleepTime = period - deltaTime;
			try {
				// Thread.sleep(milisecond) => Từ ns/10^6 để ra ms
				if (sleepTime < 0)
					Thread.sleep(period / 2000000);
				else
					Thread.sleep(sleepTime / 1000000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			beginTime = System.nanoTime();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		inputManager.processKeyPressed(e.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		inputManager.processKeyReleased(e.getKeyCode());
	}

	public GameWorld getGameWorld() {
		return this.gameWorld;
	}
}
