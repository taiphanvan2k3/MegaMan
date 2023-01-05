package gameobjects;

import java.awt.Graphics2D;

import effect.CacheDataLoader;

public class BackgroundMap extends GameObject {
	public int[][] map;
	private int blockSize;

	public BackgroundMap(float x, float y, GameWorld gameWorld) {
		super(x, y, gameWorld);
		this.map = CacheDataLoader.getInstance().getBackGroundMap();
		this.blockSize = 30;
	}

	@Override
	public void Update() {
		// Nothing
	}

	public void draw(Graphics2D g2) {
		Camera camera = getGameWorld().getCamera();
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				if (j * blockSize - camera.getPosX() > -30 && j * blockSize - camera.getPosX() < camera.getWidthView()
						&& i * blockSize - camera.getPosY() > -30
						&& i * blockSize - camera.getPosY() < camera.getHeigthView()) {
					// Vẽ bằng phương thức drawImage của graphics2D

					/*
					 * j: là dòng của ma trận nên sẽ tương ứng với toạ độ x . i: cột của ma trận nên
					 * sẽ tương ứng với toạ độ y
					 */
					g2.drawImage(CacheDataLoader.getInstance().getFrameImage("tiled" + map[i][j]).getImage(),
							(int) (this.getPosX() + j * blockSize - camera.getPosX()),
							(int) (this.getPosY() + i * blockSize - camera.getPosY()), null);
				}
			}
		}
	}
}
