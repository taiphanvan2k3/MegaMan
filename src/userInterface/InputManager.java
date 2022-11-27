package userInterface;

import java.awt.event.KeyEvent;

//Lớp này nhận 
public class InputManager {
	private GamePanel gamePanel;

	public InputManager(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}

	// Nhận keyCode lấy từ bàn phím để xử lí
	public void processKeyPressed(int keyCode) {
		switch (keyCode) {
		case KeyEvent.VK_UP:
			System.out.println("You pressed up");
			break;
		case KeyEvent.VK_DOWN:
			System.out.println("You pressed down");
			break;
		case KeyEvent.VK_LEFT:
			//Khi bấm qua trái thì cho nhân vật đi qua 5 px
			gamePanel.getMegaMan().setSpeedX(-5);
			break;
		case KeyEvent.VK_RIGHT:
			gamePanel.getMegaMan().setSpeedX(5);
			break;
		case KeyEvent.VK_ENTER:
			break;
		case KeyEvent.VK_SPACE:
			break;
		case KeyEvent.VK_A:
			// A: để cho nhân vật bắn
			break;
		}
	}

	public void processKeyReleased(int keyCode) {
		switch (keyCode) {
		case KeyEvent.VK_UP:
			System.out.println("You released up");
			break;
		case KeyEvent.VK_DOWN:
			System.out.println("You released down");
			break;
		case KeyEvent.VK_LEFT:
			System.out.println("You released left");
			break;
		case KeyEvent.VK_RIGHT:
			System.out.println("You released right");
			break;
		case KeyEvent.VK_ENTER:
			break;
		case KeyEvent.VK_SPACE:
			break;
		case KeyEvent.VK_A:
			// A: để cho nhân vật bắn
			break;
		}
	}
}
