package gameobjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import userInterface.GameFrame;
import userInterface.GamePanel;

public class megaMan {
	// Do game 2D nên cần tạo độ x,y để xác định vị trí của nhân vật
	private float posX, posY;

	// MegaMan là 1 đối tượng cụ thể sẽ có chiều rộng, chiều cao và khối lượng
	// Vì khôg cần độ chính xác cao nên chỉ cần dùng HCN để mô tả
	private float width, height, mass;

	// Vận tốc theo phương ngang và vận tốc theo phương thẳng đứng
	private float speedX, speedY;

	// Hướng đi sang phải/trái của nhân vật để biết nhả đạn về hướng nào
	public static int DIR_LEFT, DIR_RIGHT;
	private int direction;

	private GameWorld gameWorld;

	public megaMan() {
	}

	public megaMan(float posX, float posY, float width, float height, float mass, GameWorld gameWorld) {
		this.gameWorld = gameWorld;
		this.posX = posX;
		this.posY = posY;
		this.width = width;
		this.height = height;
		this.mass = mass;
		this.speedX = this.speedY = 0;
	}

	public float getPosX() {
		return posX;
	}

	public void setPosX(float posX) {
		this.posX = posX;
	}

	public float getPosY() {
		return posY;
	}

	public void setPosY(float posY) {
		this.posY = posY;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getMass() {
		return mass;
	}

	public void setMass(float mass) {
		this.mass = mass;
	}

	public float getSpeedX() {
		return speedX;
	}

	public void setSpeedX(float speedX) {
		this.speedX = speedX;
	}

	public float getSpeedY() {
		return speedY;
	}

	public void setSpeedY(float speedY) {
		this.speedY = speedY;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public void draw(Graphics2D g2) {
		g2.setColor(Color.red);
		// Vẽ nhân vật ở chính giữa hình do đó ta dời đỉnh bắt đầu vẽ đi 1 lượng
		// this.width/2 và this.height/2
		g2.fillRect((int) (posX - this.width / 2), (int) (posY - this.height / 2), (int) height, (int) width);
		g2.setColor(Color.black);
		g2.fillRect((int) posX, (int) posY, 5, 5);
	}

	public Rectangle getBoundWithMap() {
		// Cần trả về vùng bao quanh nhân vật mà nhân vật chính là
		// tâm của HCN đó. Do đó ta phải tính tạo đọ góc trên bên trái
		// của HCN đó.
		Rectangle r = new Rectangle();
		r.x = (int) (this.posX - this.width / 2);
		r.y = (int) (this.posY - this.height / 2);
		r.width = (int) this.width;
		r.height = (int) this.height;
		return r;
	}

	public void update() {
		this.setPosX(posX + speedX);

		Rectangle futureRectangle = this.getBoundWithMap();
		futureRectangle.y += speedY;
		// futureRectangle: HCN trong tương lai bao bọc nhân vật, dùng HCN
		// này để lường trước xem có va chạm với đất hay không trước khi cập
		// nhật this.posY
		Rectangle rectLand = gameWorld.getPhysMap().haveCollisionWithLand(futureRectangle);
		if (rectLand != null) {
			// Nếu có va chạm với đất thì nhân vật phải đứng trên đất
			// Khi có tung độ bên trên của khối rectLand,tức là
			// ta có được tung độ tại cạnh dưới của HCN xung quanh nhân vật.
			// Do đó bây giờ, toạ độ Y bên trên của nhân vật phải lấy toạ độ Y của chân -
			// height/2
			this.posY = rectLand.y - this.height / 2;
		} else {
			// Nếu ko có va chạm thì posY tăng lên thể hiện cho việc nhân
			// vật đang đi xuống
			this.posY += speedY;
			this.speedY += mass;
		}

	}
}
