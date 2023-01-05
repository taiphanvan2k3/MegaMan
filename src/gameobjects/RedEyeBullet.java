package gameobjects;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import effect.Animation;
import effect.CacheDataLoader;

public class RedEyeBullet extends Bullet {
	private Animation bulletForwardAnimation, bulletBackAnimation;

	public RedEyeBullet(float posX, float posY, GameWorld gameWorld) {
		// posX,posY,width,height,mass,damage,Gameworld
		super(posX, posY, 30, 30, 1.0f, 10, gameWorld);
		bulletForwardAnimation = CacheDataLoader.getInstance().getAnimation("redeyebullet");
		bulletBackAnimation = CacheDataLoader.getInstance().getAnimation("redeyebullet");
		bulletBackAnimation.flipAllImage();
	}

	@Override
	public void draw(Graphics2D g2) {
		if (this.getSpeedX() > 0) {
			bulletForwardAnimation.Update(System.nanoTime());
			bulletForwardAnimation.draw(g2, (int) (this.getPosX() - getGameWorld().getCamera().getPosX()),
					(int) (this.getPosY() - getGameWorld().getCamera().getPosY()));
		} else {
			bulletBackAnimation.Update(System.nanoTime());
			bulletBackAnimation.draw(g2, (int) (this.getPosX() - getGameWorld().getCamera().getPosX()),
					(int) (this.getPosY() - getGameWorld().getCamera().getPosY()));
		}
	}

	@Override
	public Rectangle getBoundWithEnemy() {
		//Tương tự như blueFire thì chỉ cần getBoundWithMap thôi
		return this.getBoundWithMap();
	}

	@Override
	public void attack() {
		//Để trống vì đạn sẽ gây xác thương cho megaMan khi va chạm
	}

	/*
	 * Không cần viết lại phương thức update cho lớp này vì Update cho lớp này giống
	 * với update của lớp Bullet
	 */
}
