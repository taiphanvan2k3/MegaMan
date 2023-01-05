package gameobjects;

import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import effect.CacheDataLoader;
import effect.FrameImage;
import userInterface.GameFrame;

public class GameWorld {
	private BufferedImage bufferedImage;
	// Dùng ParticularObject để add megaMan và các con quái vào để quản lí
	private ParticularObjectManager particularObjectManager;
	private BulletManager bulletManager;

	private megaMan megaman;

	private PhysicalMap physMap;
	private BackgroundMap backgroundMap;

	private Camera camera;

	public static final int finalBossX = 3600;

	public static final int INIT_GAME = 0;
	public static final int TUTORIAL = 1;
	public static final int GAMEPLAY = 2;
	public static final int GAMEOVER = 3;
	public static final int GAMEWIN = 4;
	public static final int PAUSEGAME = 5;

	public static final int INTROGAME = 0;
	public static final int MEETFINALBOSS = 1;

	public int openIntroGameY = 0;
	public int state = INIT_GAME;
	public int previousState = state;
	public int tutorialState = INTROGAME;

	private int indexOfStoryTutorial = 0;
	private String[] textStory = new String[4];

	private String textTutorial;
	private int currentLengthOfTutorial = 1;

	private boolean finalBossTrigger = false;
	private ParticularObject boss;

	private AudioClip backGroundMusic;

	private FrameImage avatar = CacheDataLoader.getInstance().getFrameImage("avatar");

	// Nhân vật megaMan sẽ có 3 mạng
	private int numberOfLife = 3;

	public GameWorld() {
		textStory[0] = "We are heros, and our mission is protecting our Home\nEarth....";
		textStory[1] = "There was a Monster from University on Earth in 10 years\n"
				+ "and we lived in the scare in that 10 years....";
		textStory[2] = "Now is the time for us, kill it and get freedom!....";
		textStory[3] = "      LET'S GO!.....";

		textTutorial = textStory[0];

		bufferedImage = new BufferedImage(GameFrame.screen_width, GameFrame.screen_height, BufferedImage.TYPE_INT_ARGB);
		megaman = new megaMan(400, 400, this);
		megaman.setTeamType(ParticularObject.LEAGUE_TEAM);

		physMap = new PhysicalMap(0, 0, this);
		backgroundMap = new BackgroundMap(0, 0, this);

		camera = new Camera(0, 50, GameFrame.screen_width, GameFrame.screen_height, this);

		bulletManager = new BulletManager(this);

		particularObjectManager = new ParticularObjectManager(this);
		particularObjectManager.addObject(megaman);

		// Sinh ra quái
		this.initEnemies();

		// Khởi tạo âm thanh
		this.backGroundMusic = CacheDataLoader.getInstance().getSound("bgmusic");
	}

	public megaMan getMegaman() {
		return megaman;
	}

	public void setMegaman(megaMan megaman) {
		this.megaman = megaman;
	}

	public PhysicalMap getPhysMap() {
		return physMap;
	}

	public void setPhysMap(PhysicalMap physMap) {
		this.physMap = physMap;
	}

	public Camera getCamera() {
		return camera;
	}

	public BulletManager getBulletManager() {
		return this.bulletManager;
	}

	public ParticularObjectManager getParticularObjectManager() {
		return this.particularObjectManager;
	}

	public AudioClip getBackgroundMusic() {
		return this.backGroundMusic;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	public int getIndexOfStoryTutorial() {
		return indexOfStoryTutorial;
	}

	public void setIndexOfStoryTutorial(int indexOfStoryTutorial) {
		this.indexOfStoryTutorial = indexOfStoryTutorial;
	}

	public String[] getTextStory() {
		return textStory;
	}

	public void setTextStory(String[] textStory) {
		this.textStory = textStory;
	}

	public String getTextTutorial() {
		return textTutorial;
	}

	public void setTextTutorial(String textTutorial) {
		this.textTutorial = textTutorial;
	}

	public int getCurrentLengthOfTutorial() {
		return currentLengthOfTutorial;
	}

	public void setCurrentLengthOfTutorial(int currentLengthOfTutorial) {
		this.currentLengthOfTutorial = currentLengthOfTutorial;
	}

	public BufferedImage getBufferedImage() {
		return bufferedImage;
	}

	private void initEnemies() {
		RedEyeDevil redeye = new RedEyeDevil(1250, 410, this);
		redeye.setTeamType(ParticularObject.ENEMY_TEAM);
		redeye.setDirection(ParticularObject.LEFT_DIR);
		particularObjectManager.addObject(redeye);

		ParticularObject redeye2 = new RedEyeDevil(2500, 500, this);
		redeye2.setDirection(ParticularObject.LEFT_DIR);
		redeye2.setTeamType(ParticularObject.ENEMY_TEAM);
		particularObjectManager.addObject(redeye2);

		ParticularObject redeye3 = new RedEyeDevil(3450, 500, this);
		redeye3.setDirection(ParticularObject.LEFT_DIR);
		redeye3.setTeamType(ParticularObject.ENEMY_TEAM);
		particularObjectManager.addObject(redeye3);

		ParticularObject redeye4 = new RedEyeDevil(500, 1190, this);
		redeye4.setDirection(ParticularObject.RIGHT_DIR);
		redeye4.setTeamType(ParticularObject.ENEMY_TEAM);
		particularObjectManager.addObject(redeye4);
	}

	public void switchState(int state) {
		this.previousState = this.state;
		this.state = state;
	}

	

	private void updateTutorial() {
		switch (tutorialState) {
		case INTROGAME:
			if (indexOfStoryTutorial == 0) {
				if (openIntroGameY < 450) {
					// Mở dần dần của sổ giới thiệu tutorial lên
					openIntroGameY += 4;
				} else
					indexOfStoryTutorial++;
			} else {
				// Bây giờ sẽ hiển thị textStory[1],textStory[2],textStory[3]

				// Sẽ hiện chữ lần lượt, chứ không vẽ 1 lần
				if (this.currentLengthOfTutorial < textTutorial.length())
					this.currentLengthOfTutorial++;
			}
			break;
		case MEETFINALBOSS:
			if (indexOfStoryTutorial == 0) {
				// Sẽ khép dần lại intro
				if (openIntroGameY >= 450)
					openIntroGameY--;
				if (camera.getPosX() < finalBossX) {
					System.out.println("Increment posX of camera");
					camera.setPosX(camera.getPosX() + 2);
				}

				if (megaman.getPosX() < finalBossX + 150) {
					/*
					 * Giúp megaman vượt qua cổng đến vị trí nào đó.
					 */
					megaman.setDirection(ParticularObject.RIGHT_DIR);
					megaman.run();
					megaman.Update();
				} else
					megaman.stopRun();
			}

			if (openIntroGameY < 450 && camera.getPosX() >= finalBossX && megaman.getPosX() >= finalBossX + 150) {
				// Khoá camera nữa, không để camera chạy theo megaman
				camera.lock();
				indexOfStoryTutorial++;
				megaman.stopRun();

				// Thực hiện đóng cổng lại
				physMap.physicalMap[14][120] = 1;
				physMap.physicalMap[15][120] = 1;
				physMap.physicalMap[16][120] = 1;
				physMap.physicalMap[17][120] = 1;

				backgroundMap.map[14][120] = 17;
				backgroundMap.map[15][120] = 17;
				backgroundMap.map[16][120] = 17;
				backgroundMap.map[17][120] = 17;
			} else {
				// Giai đoạn đang chạy tutorial
				if (this.currentLengthOfTutorial < textTutorial.length())
					this.currentLengthOfTutorial++;
			}
			break;
		}

	}

	private void drawStringOfTutorial(Graphics2D g2, String text, int x, int y) {
		/*
		 * Dùng phương thức drawString để vẽ chữ lên BufferedImage. Vì mỗi textStory[i]
		 * có thể gồm 2 dòng, nhưng khi dùng hàm trên thì chỉ có thể vẽ từng dòng, do đó
		 * ta phải split ra rồi vẽ lần lượt
		 */
		for (String str : text.split("\n")) {
			g2.drawString(str, x, y);
			/*
			 * Lấy chiều cao của font chữ hiện tại thông qua phương thức getFontMetrics()
			 */
			y += g2.getFontMetrics().getHeight();
		}

	}
	private void renderTutorial(Graphics2D g2) {
		/*
		 * Khi mới mở game hay mới vừa gặp boss thì việc thu hẹp màn hình đen là như
		 * nhau chỉ khác là với việc mở game thì có tutorial Nửa trên và nửa dưới bắt
		 * đầu thu hẹp để hiện ra màn hình game
		 */
		int yMid = GameFrame.screen_height / 2 - 15;
		/*
		 * y1,y2: đại diện cho vị trí bắt đầu vẽ của nửa trên nửa dưới màn hình.Chú ý
		 * rằng y1 luôn âm, nhưng vẫn vẽ được
		 */
		int y1 = yMid - GameFrame.screen_height / 2 - openIntroGameY / 2;
		int y2 = yMid + openIntroGameY / 2;
		// Ban đầu màn hình sẽ là màu đen và thu hẹp nửa trên,nửa dưới lại
		g2.setColor(Color.black);
		g2.fillRect(0, y1, GameFrame.screen_width, GameFrame.screen_height / 2);
		g2.fillRect(0, y2, GameFrame.screen_width, GameFrame.screen_height / 2);

		if (tutorialState == INTROGAME) {
			if (indexOfStoryTutorial >= 1) {
				// Vẽ ra phần nhân vật megaman và text tutorial
				g2.drawImage(avatar.getImage(), 600, 350, null);
				g2.setColor(Color.blue);
				g2.fillRect(280, 450, 350, 80);
				g2.setColor(Color.white);
				String text = textTutorial.substring(0, this.currentLengthOfTutorial - 1);
				this.drawStringOfTutorial(g2, text, 290, 480);
			}
		}
		// meetfinalBoss thì đã có phần xử lí trước khi gọi if rồi.
	}
	public void Update() {
		switch (state) {
		case INIT_GAME:
			break;
		case TUTORIAL:
			updateTutorial();
			break;
		case GAMEPLAY:
			particularObjectManager.updateObjects();
			bulletManager.updateObjects();
			physMap.Update();
			camera.Update();

			if (megaman.getPosX() > finalBossX && !finalBossTrigger) {
				// Nghĩa là chạm mặt boss rồi
				this.finalBossTrigger = true;

				// Chuyển qua trạng thái tutorial và trạng thái tutorial là meetFinalBoss
				switchState(TUTORIAL);
				this.tutorialState = MEETFINALBOSS;

				indexOfStoryTutorial = 0;
				openIntroGameY = 550;

				boss = new FinalBoss(finalBossX + 700, 460, this);
				boss.setTeamType(ParticularObject.ENEMY_TEAM);
				boss.setDirection(ParticularObject.LEFT_DIR);
				particularObjectManager.addObject(boss);
			}

			if (megaman.getState() == ParticularObject.DEATH) {
				numberOfLife--;
				if (numberOfLife < 0) {
					this.switchState(GAMEOVER);
					backGroundMusic.stop();
				} else {
					megaman.setBlood(100);
					// Khi hồi sinh thì megaman được rơi từ trên xuống
					megaman.setPosY(megaman.getPosY() - 50);
					megaman.setState(ParticularObject.NOBEHURT);
					particularObjectManager.addObject(megaman);
				}
			}

			if (finalBossTrigger && boss.getBlood() == 0)
				this.switchState(GAMEWIN);
			break;

		case GAMEOVER:
		case GAMEWIN:
			// Hai state này không cần update nữa
			break;
		}
	}

	public void Render() {

		Graphics2D g2 = (Graphics2D) bufferedImage.getGraphics();
		if (g2 != null) {
			switch (state) {
			case INIT_GAME:
				g2.setColor(Color.black);
				g2.fillRect(0, 0, GameFrame.screen_width, GameFrame.screen_height);
				g2.setColor(Color.white);
				g2.drawString("PRESS ENTER TO CONTINUE", 400, 300);
				break;
			case PAUSEGAME:
				g2.setColor(Color.black);
				// Hiện ra một HCN giữa màn hình và dừng game
				g2.fillRect(300, 260, 500, 70);
				g2.setColor(Color.white);
				g2.drawString("PRESS ENTER TO CONTINUE", 400, 300);
				break;
			case TUTORIAL:
				backgroundMap.draw(g2);
				if (this.tutorialState == MEETFINALBOSS)
					particularObjectManager.draw(g2);
				this.renderTutorial(g2);
				break;
			case GAMEPLAY:
			case GAMEWIN:
				/*
				 * Không cần vẽ physMap nữa physMap.draw(g2).Chú ý phải vẽ backGroundMap trước
				 * khi vẽ megaMan để không bị vẽ nền đè lên megaMan.Không cần megaman.draw(g2);
				 * nữa vì đã có particularObjectManager.draw(g2);
				 */
				backgroundMap.draw(g2);
				particularObjectManager.draw(g2);
				bulletManager.draw(g2);
				g2.setColor(Color.gray);
				g2.fillRect(19, 59, 102, 22);
				g2.setColor(Color.red);
				g2.fillRect(20, 60, (int) megaman.getBlood(), 20);
				for (int i = 0; i < numberOfLife; i++) {
					g2.drawImage(CacheDataLoader.getInstance().getFrameImage("hearth").getImage(), 20 + i * 40, 18,
							null);
				}

				if (state == GAMEWIN) {
					g2.drawImage(CacheDataLoader.getInstance().getFrameImage("gamewin").getImage(), 300, 300, null);
				}
				break;
			case GAMEOVER:
				break;
			}
		}
	}
}
