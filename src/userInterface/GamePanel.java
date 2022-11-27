package userInterface;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import effect.Animation;
import effect.CacheDataLoader;
import effect.FrameImage;
import gameobjects.megaMan;

public class GamePanel extends JPanel implements Runnable, KeyListener {
	private Thread thread;
	private InputManager inputManager;
	private BufferedImage bufferedImage;
	private Graphics2D bufGraphic2D;

	private megaMan megaman;

	public GamePanel() {
		inputManager = new InputManager(this);
		bufferedImage = new BufferedImage(GameFrame.screen_width, GameFrame.screen_height, BufferedImage.TYPE_INT_ARGB);
		megaman = new megaMan(300, 300, 100, 100, 0.1f);
	}

	@Override
	// paint là phương thức tự động gọi khi add Panel này vào
	public void paint(Graphics g) {
		// Ko cần super.draw(g) nữa vì bufferedImage có kích thước to bằng
		// panel nên ko sợ vẫn còn các hình ảnh lúc trước chưa đựo xoá
		g.drawImage(bufferedImage, 0, 0, this);
	}

	public void updateGame() {
		// update lại vị trí x,y của nhân vật megaMan
		megaman.update();
	}

	public void renderGame() {
		if (bufferedImage == null) {
			bufferedImage = new BufferedImage(GameFrame.screen_width, GameFrame.screen_height,
					BufferedImage.TYPE_INT_ARGB);
		}

		if (bufGraphic2D == null)
			bufGraphic2D = (Graphics2D) bufferedImage.getGraphics();
		bufGraphic2D.setColor(Color.white);
		bufGraphic2D.fillRect(0, 0, this.getWidth(), this.getHeight());

		// draw object games here
		megaman.draw(bufGraphic2D);
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
			updateGame();
			renderGame();
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

	public megaMan getMegaMan() {
		return megaman;
	}
}
