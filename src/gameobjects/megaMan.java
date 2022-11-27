package gameobjects;

import java.awt.Color;
import java.awt.Graphics2D;

public class megaMan {
	// Do game 2D nên cần tạo độ x,y để xác định vị trí của nhân vật
	private float posX, posY;

	// MegaMan là 1 đối tượng cụ thể sẽ có chiều rộng, chiều cao và khối lượng
	// Vì khôg cần độ chính xác cao nên chỉ cần dùng HCN để mô tả
	private float width, height, mass;

	// Vận tốc theo phương ngang và vận tốc theo phương thẳng đứng
	private float speedX, speedY;

	// Hướng đi sang phải/trái của nhân vật
	private static int DIR_LEFT, DIR_RIGHT;
	private int direction;

	public megaMan() {
	}

	public megaMan(float posX, float posY, float width, float height, float mass) {
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
		g2.fillRect((int) posX, (int) posY, (int) height, (int) width);
	}

	public void update() {
		this.setPosX(posX + speedX);
		// Khi update xong phải cho speedX về lại 0, vì ta đag dùng vòng lặp thì cứ liên
		// tục gọi update nhân vật thì nhân vật cứ chạy miết
		speedX = 0;
	}

}
