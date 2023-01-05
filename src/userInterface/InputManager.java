package userInterface;

import java.awt.event.KeyEvent;

import gameobjects.GameWorld;
import gameobjects.PhysicalMap;
import gameobjects.megaMan;

public class InputManager {
	private GameWorld gameWorld;

	public InputManager(GameWorld gameWorld) {
		this.gameWorld = gameWorld;
	}

	// Nhận keyCode lấy từ bàn phím để xử lí
	public void processKeyPressed(int keyCode) {
		switch (keyCode) {
		case KeyEvent.VK_UP:
			this.gameWorld.getMegaman().jump();
			break;
		case KeyEvent.VK_DOWN:
			this.gameWorld.getMegaman().dick();
			break;
		case KeyEvent.VK_LEFT:
			this.gameWorld.getMegaman().setDirection(megaMan.LEFT_DIR);
			this.gameWorld.getMegaman().run();
			break;
		case KeyEvent.VK_RIGHT:
			this.gameWorld.getMegaman().setDirection(megaMan.RIGHT_DIR);
			this.gameWorld.getMegaman().run();
			break;
		case KeyEvent.VK_ENTER:
			if (gameWorld.state == GameWorld.INIT_GAME) {
				if (gameWorld.previousState == GameWorld.GAMEPLAY)
					gameWorld.switchState(GameWorld.GAMEPLAY);
				else {
					gameWorld.switchState(GameWorld.TUTORIAL);
				}
				gameWorld.getBackgroundMusic().loop();
			}
			if (gameWorld.state == GameWorld.TUTORIAL && gameWorld.getIndexOfStoryTutorial() >= 1) {
				if (gameWorld.getIndexOfStoryTutorial() <= 3) {
					/*
					 * index để hiển thị chữ của tutorial đếm từ 1, còn index 0 là của việc hiển thị
					 * map
					 */
					gameWorld.setIndexOfStoryTutorial(gameWorld.getIndexOfStoryTutorial() + 1);
					gameWorld.setCurrentLengthOfTutorial(1);
					String textStory[] = gameWorld.getTextStory();
					gameWorld.setTextTutorial(textStory[gameWorld.getIndexOfStoryTutorial() - 1]);
				}else gameWorld.switchState(GameWorld.GAMEPLAY);
			}
			break;
		case KeyEvent.VK_SPACE:
			this.gameWorld.getMegaman().jump();
			break;
		case KeyEvent.VK_A:
			// A: để cho nhân vật bắn
			this.gameWorld.getMegaman().attack();
			break;
		}
	}

	public void processKeyReleased(int keyCode) {
		switch (keyCode) {
		case KeyEvent.VK_UP:
			break;
		case KeyEvent.VK_DOWN:
			gameWorld.getMegaman().standUp();
			break;
		case KeyEvent.VK_LEFT:
			// Khi nhả phím mũi tên trái ra thì cho nhân vật dừng
			if (this.gameWorld.getMegaman().getSpeedX() < 0)
				this.gameWorld.getMegaman().stopRun();
			break;
		case KeyEvent.VK_RIGHT:
			/*
			 * Chỉ cập cho stopRun khi vận tốc nó là 0 thôi, vì có stopRun() khi vận tốc
			 * bằng 0 cũng được nhưng dư thừa.
			 */
			if (this.gameWorld.getMegaman().getSpeedX() > 0)
				this.gameWorld.getMegaman().stopRun();
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
