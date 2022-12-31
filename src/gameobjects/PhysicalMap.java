package gameobjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import effect.CacheDataLoader;

public class PhysicalMap extends GameObject {
	public int[][] physicalMap;
	// posX,posY là toạ độ góc trên cùng của map

	// độ dài có hình vuông
	private int blockSize;

	public PhysicalMap(float posX, float posY, GameWorld gameWorld) {
		super(posX, posY, gameWorld);
		this.blockSize = 30;
		physicalMap = CacheDataLoader.getInstance().getPhysicalMap();
	}

	public void updatePosY(float posY) {
		this.setPosY(this.getPosY() + posY);
	}

	public void updatePosX(float posX) {
		this.setPosX(this.getPosX() + posX);
	}

	public int getBlockSize() {
		return this.blockSize;
	}

	public void draw(Graphics2D g2) {
		// Bắt đầu vẽ từng khối ô vuông từ vị trí (posX,posY)
		// Sẽ vẽ lần lượt từ trái qua phái rồi từ trên xuống
		// Mỗi khối cách nhau điểm bắt đầu vẽ là blockSize piposXel
		g2.setColor(Color.gray);
		for (int i = 0; i < this.physicalMap.length; i++) {
			for (int j = 0; j < this.physicalMap[0].length; j++) {
				int k = this.physicalMap[i][j];
				if (k == 1) {
					// super.getPosX() <=> this.getPosX()
					g2.fillRect((int) super.getPosX() + j * blockSize, (int) this.getPosY() + i * blockSize, blockSize,
							blockSize);
					//Vì vẽ theo CHỈ SỐ của ma trận nên khi physicalMap[i][j] là vật cản thì
					// ta sẽ vẽ 1 khối nằm ở vị trí (posX+j*blockSize,posY+i*blockSize)
				}
			}
		}
	}

	public Rectangle haveCollisionWithLand(Rectangle rect) {
		// Tham số truyền vào là HCN bao quanh nhân vật,giá trị trả về
		// là Rectangle đại diện cho hình chữ nhật thể hiện đất mà nhân vật
		// đã chạm đất (nếu có)
		// Do ma trận có nhiều phân tử,do đó cần xác định phạm vi cần lặp để
		// kiểm tra có va chạm hay không?
		int posX1 = rect.x / blockSize;
		posX1 -= 2;
		int posX2 = (rect.x + rect.width) / blockSize;
		posX2 += 2;
		// (posX1-=2,posX2+=2 để nới rộng khoảng kiểm tra va chạm để tăng khả năng
		// kiểm tra va chạm cho chính xác hơn.

		int posYOfRect = (rect.y + rect.height) / blockSize;
		// Xác định toạ độ chân của nhân vật để xác định có chạm với đất không

		if (posX1 < 0)
			posX1 = 0;
		if (posX2 >= physicalMap[0].length)
			posX2 = physicalMap[0].length - 1;

		// Nên sửa cận trên của y
		for (int y = posYOfRect; y < physicalMap.length; y++) {
			for (int x = posX1; x <= posX2; x++) {
				if (physicalMap[y][x] == 1) {
					Rectangle temp = new Rectangle((int) (this.getPosX() + x * blockSize),
							(int) this.getPosY() + y * blockSize, blockSize, blockSize);
					if (rect.intersects(temp)) {
						// trả về HCN của phần đất mà nhân vật va chạm
						return temp;
					}
				}
			}
		}
		return null;
	}

	@Override
	public void Update() {

	}
}
