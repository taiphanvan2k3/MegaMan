package gameobjects;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import effect.Animation;
import effect.CacheDataLoader;

public class BlueFire extends Bullet {
	private Animation bulletForwardAnimation, bulletBackAnimation;

	public BlueFire(float posX, float posY, GameWorld gameWorld) {
		super(posX, posY, 60, 30, 1.0f, 10, gameWorld);
		bulletForwardAnimation = CacheDataLoader.getInstance().getAnimation("bluefire");
		bulletBackAnimation = CacheDataLoader.getInstance().getAnimation("bluefire");
		bulletBackAnimation.flipAllImage();
	}

	@Override
	public Rectangle getBoundWithEnemy() {
		/*
		 * Đạn là kẻ địch đối với megaMan do đó getBoundWithEnemy thì chính là
		 * boundWithMap của đạn
		 */
		return getBoundWithMap();
	}

	@Override
	public void attack() {
		// Để trống vì đạn sẽ gây xác thương cho quái khi va chạm
	}

	@Override
	public void draw(Graphics2D g2) {
		// Override lại phương thức của lớp cha
		if (this.getSpeedX() > 0) {
			// Đạn hướng về bên phải
			if (!bulletForwardAnimation.isIgnoreFrame(0) && bulletForwardAnimation.getCurrentFrame() == 3) {
				/*
				 * Khi đã thực hiện 3 frame đầu tiên của việc bắn ra viên đạn (giai đoạn bắn và
				 * nổ) thì không lặp lại quá trình nổ này nữa mà là quá trình viên đạn được bắn
				 * đi.Do đó ta sẽ set cho 3 frame đầu là ignore và chỉ lặp cái frame kể từ 4 về
				 * sau.
				 */
				bulletForwardAnimation.setIgnoreFrame(0);
				bulletForwardAnimation.setIgnoreFrame(1);
				bulletForwardAnimation.setIgnoreFrame(2);
			}

			bulletForwardAnimation.Update(System.nanoTime());
			bulletForwardAnimation.draw(g2, (int) (this.getPosX() - getGameWorld().getCamera().getPosX()),
					(int) (this.getPosY() - getGameWorld().getCamera().getPosY()));
		} else {
			// Đạn hướng về bên trái
			if (!bulletBackAnimation.isIgnoreFrame(0) && bulletBackAnimation.getCurrentFrame() == 3) {
				/*
				 * Khi đã thực hiện 3 frame đầu tiên của việc bắn ra viên đạn (giai đoạn bắn và
				 * nổ) thì không lặp lại quá trình nổ này nữa mà là quá trình viên đạn được bắn
				 * đi.Do đó ta sẽ set cho 3 frame đầu là ignore và chỉ lặp cái frame kể từ 4 về
				 * sau.
				 */
				bulletBackAnimation.setIgnoreFrame(0);
				bulletBackAnimation.setIgnoreFrame(1);
				bulletBackAnimation.setIgnoreFrame(2);
			}

			bulletBackAnimation.Update(System.nanoTime());
			bulletBackAnimation.draw(g2, (int) (this.getPosX() - getGameWorld().getCamera().getPosX()),
					(int) (this.getPosY() - getGameWorld().getCamera().getPosY()));
		}
	}

	@Override
	public void Update() {
		if (bulletForwardAnimation.isIgnoreFrame(0) || bulletBackAnimation.isIgnoreFrame(0)) {
			/*
			 * Chỉ cập nhật vị trí của đạn khi kết thúc giai đoạn nổ.Mà kế thừa giai đoạn nổ
			 * là lúc ta đã setIgnoreFrame(0) = true rồi
			 */
			setPosX(this.getPosX() + this.getSpeedX());
		}

		ParticularObject object = getGameWorld().getParticularObjectManager().getCollisionWithEnemyObject(this);
		if (object != null && object.getState() == ALIVE) {
			/*
			 * Viên đạn hết hiệu lực nên setBlood(0) để nó xoá viên đạn này khỏi
			 * BulletManager
			 */
			this.setBlood(0);

			/*
			 * Quái sẽ bị trừ HP do damage của đạn. Trong phương thức beHurt đã set lại
			 * trạng thái là beHurt rồi.
			 */
			object.beHurt(this.getDamage());
		}
	}
}
