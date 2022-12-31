package userInterface;

import java.awt.event.KeyEvent;

import gameobjects.GameWorld;
import gameobjects.PhysicalMap;
import gameobjects.megaMan;

//Lớp này nhận 
public class InputManager {
	private GameWorld gameWorld;

	public InputManager(GameWorld gameWorld) {
		this.gameWorld = gameWorld;
	}

	// Nhận keyCode lấy từ bàn phím để xử lí
	public void processKeyPressed(int keyCode) {
		switch (keyCode) {
		case KeyEvent.VK_UP:
			this.gameWorld.getMegaman().setSpeedY(-5);
			this.gameWorld.getPhysMap().updatePosY(5);
			break;
		case KeyEvent.VK_DOWN:
			this.gameWorld.getPhysMap().updatePosY(-5);
			break;
		case KeyEvent.VK_LEFT:
			// Khi bấm qua trái thì cho nhân vật đi qua trái 3 px
			this.gameWorld.getMegaman().setDirection(megaMan.DIR_LEFT);
			this.gameWorld.getMegaman().setSpeedX(-3);
			this.gameWorld.getPhysMap().updatePosX(5);
			break;
		case KeyEvent.VK_RIGHT:
			this.gameWorld.getMegaman().setDirection(megaMan.DIR_RIGHT);
			this.gameWorld.getMegaman().setSpeedX(3);
			this.gameWorld.getPhysMap().updatePosX(-5);
			break;
		case KeyEvent.VK_ENTER:
			break;
		case KeyEvent.VK_SPACE:
//			 Khi bấm space thì nhảy lên.Vì chiều dương của trục y hướng xuống nên ta phải setSpeedY
//			 là âm để cho megaMan được vẽ hướng lên
			this.gameWorld.getMegaman().setSpeedY(-5);
			this.gameWorld.getPhysMap().updatePosY(5);
			break;
		case KeyEvent.VK_A:
			// A: để cho nhân vật bắn
			break;
		}
	}

	public void processKeyReleased(int keyCode) {
		switch (keyCode) {
		case KeyEvent.VK_UP:
			break;
		case KeyEvent.VK_DOWN:
			break;
		case KeyEvent.VK_LEFT:
			this.gameWorld.getMegaman().setSpeedX(0);
			this.gameWorld.getMegaman().setDirection(megaMan.DIR_LEFT);
			break;
		case KeyEvent.VK_RIGHT:
			this.gameWorld.getMegaman().setSpeedX(0);
			this.gameWorld.getMegaman().setDirection(megaMan.DIR_RIGHT);
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
