package gameobjects;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import effect.CacheDataLoader;

public class PhysicalMap extends GameObject {
	public int[][] physicalMap;

	// độ dài có hình vuông
	private int blockSize;

	public PhysicalMap(float posX, float posY, GameWorld gameWorld) {
		super(posX, posY, gameWorld);
		this.blockSize = 30;
		physicalMap = CacheDataLoader.getInstance().getPhysicalMap();
	}

	public int getBlockSize() {
		return this.blockSize;
	}

	public Rectangle haveCollisionWithLand(Rectangle rect) {
		/*
		 * Tham số truyền vào là HCN bao quanh nhân vật,giá trị trả về là Rectangle đại
		 * diện cho hình chữ nhật thể hiện đất mà nhân vật đã chạm đất (nếu có). Do ma
		 * trận có nhiều phân tử,do đó cần xác định phạm vi cần lặp để kiểm tra có va
		 * chạm hay không?
		 */
		int posX1 = rect.x / blockSize;
		posX1 -= 2;
		int posX2 = (rect.x + rect.width) / blockSize;
		posX2 += 2;
		/*
		 * (posX1-=2,posX2+=2 để nới rộng khoảng kiểm tra va chạm để tăng khả năng kiểm
		 * tra va chạm cho chính xác hơn.
		 */

		int posYOfRect = (rect.y + rect.height) / blockSize;
		/* Xác định toạ độ chân của nhân vật để xác định có chạm với đất không */

		if (posX1 < 0)
			posX1 = 0;
		if (posX2 >= physicalMap[0].length)
			posX2 = physicalMap[0].length - 1;

		// Nên sửa cận trên của y
		for (int y = posYOfRect; /* y <= posYOfRect + 2 && */ y < physicalMap.length; y++) {
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

	public Rectangle haveCollisionWithCeil(Rectangle rect) {
		/*
		 * Do cần va chạm với trần nhà nên ta sẽ kiểm tra va chạm với các block từ tung
		 * độ của cạnh bên trên HCN bao xung quanh nhân vật trở lên.
		 */

		// Cách xử lí với trần nhà thì tương tự cách xử lí với nền nhà
		int posXLeft = rect.x / blockSize;
		posXLeft -= 2;

		int posXRight = (rect.x + rect.width) / blockSize;
		posXRight += 2;

		if (posXLeft < 0)
			posXLeft = 0;

		if (posXRight >= this.physicalMap[0].length)
			posXRight = this.physicalMap[0].length - 1;
		// Tung độ của cạnh bên trên
		int topPosYOfRect = rect.y / blockSize;

		// Do đang xử lí va chạm với trần nhà nên sẽ sẽ duyệt y lên trên
		for (int y = topPosYOfRect; y >= 0; y--) {
			for (int x = posXLeft; x <= posXRight; x++) {
				if (physicalMap[y][x] == 1) {
					Rectangle temp = new Rectangle((int) (this.getPosX() + x * blockSize),
							(int) (this.getPosY() + y * blockSize), blockSize, blockSize);
					if (temp.intersects(rect))
						return temp;
				}
			}
		}
		return null;
	}

	public Rectangle haveCollisionWithRightWall(Rectangle rect) {
		int topPosYOfRect = rect.y / blockSize;
		topPosYOfRect -= 2;
		int bottomPosYOfRect = (rect.y + rect.height) / blockSize;
		bottomPosYOfRect += 2;
		if (topPosYOfRect < 0)
			topPosYOfRect = 0;
		if (bottomPosYOfRect >= this.physicalMap.length)
			bottomPosYOfRect = this.physicalMap.length - 1;

		int posXRight = (rect.x + rect.width) / blockSize;
		int posXRightUpperBound = posXRight + 3;
		if (posXRightUpperBound > this.physicalMap[0].length)
			posXRightUpperBound = this.physicalMap[0].length - 1;
		for (int x = posXRight; x <= posXRightUpperBound; x++) {
			for (int y = topPosYOfRect; y <= bottomPosYOfRect; y++) {
				if (physicalMap[y][x] == 1) {
					Rectangle temp = new Rectangle((int) (this.getPosX() + x * blockSize),
							(int) (this.getPosY() + y * blockSize), blockSize, blockSize);

					/*
					 * Nghĩ cũng không cần rect.y + rect.height - 1 > temp.y vì đã có hàm intersects
					 * là đủ
					 */
					if (rect.y + rect.height - 1 > temp.y && temp.intersects(rect)) {
						/*
						 * Như ta đã biết thì chiều cao của vật: height=y2-y1+1 Do đó tung độ của cạnh
						 * dưới là y2=height+y1-1 Toạ độ y của cạnh dưới HCN phải lớn hơn (k có bằng)
						 * toạ độ y bên trên của khối tường.Chú ý là không được lấy dấu = vì khi dấu
						 * bằng xảy ra vì vật hoàn toàn đứng trên khối đó được chứ không bị chặn lại.
						 */
						return temp;
					}
				}
			}
		}
		return null;
	}

	public Rectangle haveCollisionWithLeftWall(Rectangle rect) {
		int topPosYOfRect = rect.y / blockSize;
		topPosYOfRect -= 2;
		int bottomPosYOfRect = (rect.y + rect.height) / blockSize;
		bottomPosYOfRect += 2;

		if (topPosYOfRect < 0)
			topPosYOfRect = 0;

		if (bottomPosYOfRect >= this.physicalMap.length)
			bottomPosYOfRect = this.physicalMap.length - 1;

		int posXLeft = rect.x / blockSize;
		int posXLeftLowerBound = posXLeft - 3;
		if (posXLeftLowerBound < 0)
			posXLeftLowerBound = 0;

		for (int x = posXLeft; x >= posXLeftLowerBound; x--) {
			for (int y = topPosYOfRect; y <= bottomPosYOfRect; y++) {
				if (physicalMap[y][x] == 1) {
					Rectangle temp = new Rectangle((int) (this.getPosX() + x * blockSize),
							(int) (this.getPosY() + y * blockSize), blockSize, blockSize);
					/*
					 * Nghĩ cũng không cần rect.y + rect.height - 1 > temp.y vì đã có hàm intersects
					 * là đủ
					 */
					if (rect.y + rect.height - 1 > temp.y && temp.intersects(rect)) {
						return temp;
					}
				}
			}
		}
		return null;
	}

	@Override
	public void Update() {
		// nothing
	}

	public void draw(Graphics2D g2) {
		/*
		 * Do khi di chuyển thì camera cũng di chuyển theo nhân vật,do đó cảnh của map
		 * cũng cần phải thay đổi theo
		 */
		Camera camera = this.getGameWorld().getCamera();

		/*
		 * Bắt đầu vẽ từng khối ô vuông từ vị trí (posX,posY) Sẽ vẽ lần lượt từ trái qua
		 * phái rồi từ trên xuống
		 */
		for (int i = 0; i < this.physicalMap.length; i++) {
			for (int j = 0; j < this.physicalMap[0].length; j++) {
				int k = this.physicalMap[i][j];
				if (k == 1) {
					/*
					 * Như ta thấy khi nhân vật di chuyển sang bên phải thì dường như các bối cảnh
					 * bên phải hiện ra và các bối cảnh bên trái mất đi. Vậy map giống như một bức
					 * tranh trải dài, ta kéo bức tranh đó về bên trái thì các bối cảnh bên phải sẽ
					 * hiện ra
					 */
					g2.fillRect((int) (super.getPosX() + j * blockSize - camera.getPosX()),
							(int) (this.getPosY() + i * blockSize - camera.getPosY()), blockSize, blockSize);
					/*
					 * Vì vẽ theo CHỈ SỐ của ma trận nên khi physicalMap[i][j] là vật cản thì ta sẽ
					 * vẽ 1 khối nằm ở vị trí (posX+j*blockSize,posY+i*blockSize)
					 */
				}
			}
		}
	}
}
