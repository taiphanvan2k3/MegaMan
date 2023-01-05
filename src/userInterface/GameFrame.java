package userInterface;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import effect.CacheDataLoader;

public class GameFrame extends JFrame {
	public static final int screen_width = 1000;
	public static final int screen_height = 600;

	private GamePanel gamePanel;

	public GameFrame() {
		long begin = System.currentTimeMillis();
		this.setSize(screen_width, screen_height);
		this.setTitle("Mega Man");
		Image img = new ImageIcon(this.getClass().getResource("/megaman2.png")).getImage();
		this.setIconImage(img);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		/*
		 * LoadData tại đây để vì tại và trước dòng newGamePanel() vì trong constructor
		 * GamePanel có sử dụng việc getAnimation/getFrameImage từ Hashtable nên bắt
		 * buộc Hashtable phải có dữ liệu trước đó rồi
		 */
		try {
			//Gọi LoadData để load hết các file lên để có dữ liệu cho GamePanel xử lí
			CacheDataLoader.getInstance().LoadData();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println(System.currentTimeMillis() - begin);
		gamePanel = new GamePanel();
		this.add(gamePanel, BorderLayout.CENTER);

		// Bắt sự kiện bàn phím
		this.addKeyListener(gamePanel);
	}

	public static void main(String[] args) {
		GameFrame gameFrame = new GameFrame();
		gameFrame.setVisible(true);
		gameFrame.startGame();
	}

	public void startGame() {
		gamePanel.startGame();
	}
}
