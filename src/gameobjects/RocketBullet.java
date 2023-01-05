package gameobjects;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import effect.Animation;
import effect.CacheDataLoader;

public class RocketBullet extends Bullet {
	/*
	 * Viên đạn này ban đầu không đi thẳng, sau đó nó sẽ thay đổi hướng lên,
	 * xuống...
	 */
	private Animation bulletForwardAnimationUp, bulletForwardAnimationDown, bulletForwardAnimation;
	private Animation bulletBackAnimation, bulletBackAnimationUp, bulletBackAnimationDown;

	private long startTimeForChangeSpeedY;

	public RocketBullet(float posX, float posY, GameWorld gameWorld) {
		// super(posX, posY, width, height, mass, damage, gameWorld);
		super(posX, posY, 30, 30, 1.0f, 25, gameWorld);
		this.bulletBackAnimationUp = CacheDataLoader.getInstance().getAnimation("rocketUp");
		this.bulletBackAnimationDown = CacheDataLoader.getInstance().getAnimation("rocketDown");
		this.bulletBackAnimation = CacheDataLoader.getInstance().getAnimation("rocket");

		this.bulletForwardAnimationUp = CacheDataLoader.getInstance().getAnimation("rocketUp");
		this.bulletForwardAnimationUp.flipAllImage();

		this.bulletForwardAnimationDown = CacheDataLoader.getInstance().getAnimation("rocketDown");
		this.bulletForwardAnimationDown.flipAllImage();

		this.bulletForwardAnimation = CacheDataLoader.getInstance().getAnimation("rocket");
		this.bulletForwardAnimation.flipAllImage();

	}

	@Override
	public Rectangle getBoundWithEnemy() {
		return this.getBoundWithMap();
	}

	@Override
	public void attack() {
		/*
		 * Để trống vì đạn sẽ gây xác thương cho megaMan khi va chạm chứ không cần xây
		 * dựng phương thức này
		 */
	}

	@Override
	public void draw(Graphics2D g2) {
		if (this.getSpeedX() > 0) {
			if (this.getSpeedY() > 0) {
				bulletForwardAnimationUp.draw(g2, (int) (this.getPosX() - getGameWorld().getCamera().getPosX()),
						(int) (getPosY() - getGameWorld().getCamera().getPosY()));
			} else if (this.getSpeedY() < 0) {
				bulletForwardAnimationDown.draw(g2, (int) (this.getPosX() - getGameWorld().getCamera().getPosX()),
						(int) (getPosY() - getGameWorld().getCamera().getPosY()));
			} else
				bulletForwardAnimation.draw(g2, (int) (this.getPosX() - getGameWorld().getCamera().getPosX()),
						(int) (getPosY() - getGameWorld().getCamera().getPosY()));
		} else if (this.getSpeedX() < 0) {
			if (this.getSpeedY() > 0) {
				bulletBackAnimationUp.draw(g2, (int) (this.getPosX() - getGameWorld().getCamera().getPosX()),
						(int) (getPosY() - getGameWorld().getCamera().getPosY()));
			} else if (this.getSpeedY() < 0) {
				bulletBackAnimationDown.draw(g2, (int) (this.getPosX() - getGameWorld().getCamera().getPosX()),
						(int) (getPosY() - getGameWorld().getCamera().getPosY()));
			} else
				bulletBackAnimation.draw(g2, (int) (this.getPosX() - getGameWorld().getCamera().getPosX()),
						(int) (getPosY() - getGameWorld().getCamera().getPosY()));
		}
	}

	@Override
	public void Update() {
		super.Update();

		if (System.nanoTime() - this.startTimeForChangeSpeedY > 500 * 1000000) {
			// Cứ 0,5s sẽ thay đổi
			this.startTimeForChangeSpeedY = System.nanoTime();
			if (System.currentTimeMillis() % 3 == 0) {
				// Đạn bay lên
				this.setSpeedY(this.getSpeedX());
			} else if (System.currentTimeMillis() % 3 == 1) {
				/*
				 * ví dụ đạn bắn về bên phái lên trên thì làm điều này sẽ làm đạn bay về bên
				 * phải mà hướng xuống
				 */
				this.setSpeedY(-this.getSpeedX());
			} else {
				// Bằng 0 thì bay thẳng
				this.setSpeedY(0);
			}
		}
	}
}
