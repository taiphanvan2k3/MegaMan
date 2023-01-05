package gameobjects;

import java.applet.AudioClip;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import effect.Animation;
import effect.CacheDataLoader;

public class RedEyeDevil extends ParticularObject {
	/*
	 * Con quái này đơn giản chi có đứng im một chỗ theo chiều nó bắn thôi
	 */
	private Animation forwardAnimation, backAnimation;
	/*
	 * Cứ cách 1 khoảng thời gian là con nhân vật này lại bắn đi chứ không phải như
	 * megaMan là có thuộc tính lastTimeShooting do người chơi chủ động bắn, còn
	 * không thì thôi
	 */
	private long startTimeToShoot;

	private AudioClip shooting;

	public RedEyeDevil(float posX, float posY, GameWorld gameWorld) {
		/*
		 * Không quan tâm khối lượng con quái này, chỉ cần quan tâm HP nó là 100
		 */
		super(posX, posY, 127, 69, 0, 100, gameWorld);
		backAnimation = CacheDataLoader.getInstance().getAnimation("redeye");
		forwardAnimation = CacheDataLoader.getInstance().getAnimation("redeye");
		forwardAnimation.flipAllImage();
		/*
		 * Đây là damage khi ta va chạm với con nhân vật chứ không phải damage do đạn
		 * của nó gây ra
		 */
		this.setDamage(10);
		this.setTimeForNoBehurt(1000000000);
		this.shooting = CacheDataLoader.getInstance().getSound("redeyeshooting");
	}

	@Override
	public Rectangle getBoundWithEnemy() {
		Rectangle r = this.getBoundWithMap();
		r.x += 20;
		r.width -= 40;
		return r;
	}

	@Override
	public void attack() {
		/*
		 * Đây là nơi tạo ra loại đạn cho con quái tấn công.
		 */
		shooting.play();
		Bullet bullet = new RedEyeBullet(this.getPosX(), this.getPosY(), getGameWorld());
		bullet.setTeamType(this.getTeamType());
		if (this.getDirection() == LEFT_DIR) {
			/*
			 * Nếu con quái đang hướng về bên trái. Và do con quái này chỉ đi qua đi lại nên
			 * không cần speedY
			 */
			bullet.setSpeedX(-8);
		} else
			bullet.setSpeedX(8);
		getGameWorld().getBulletManager().addObject(bullet);
	}

	@Override
	public void Update() {
		super.Update();
		/*
		 * Sẽ có 1 điều kiện ở lớp ParticularObjectManager để quyết định sẽ cập nhật
		 * trạng thái cho con quái này không? super.Update() vào để cập nhật trạng thái
		 * và animation... Phụ thuộc vào vị trí của nhân vật với con quái mà quái sẽ
		 * thay đổi hướng bắn.
		 */
		megaMan megaMan = getGameWorld().getMegaman();
		if (megaMan.getPosX() < this.getPosX())
			this.setDirection(LEFT_DIR);
		else
			this.setDirection(RIGHT_DIR);

		if (System.nanoTime() - startTimeToShoot > 1000 * 10000000) {
			this.attack();
			startTimeToShoot = System.nanoTime();
		}
	}

	@Override
	public void draw(Graphics2D g2) {
		if (!this.isObjectOutOfCameraView()) {
			// Chỉ vẽ khi con quái này nằm trong view của camera
			if (this.getState() == NOBEHURT && (System.nanoTime() / 10000000) % 2 != 0) {
				// Khi nó miễn đang ở trạng thái noBeHurt thì nó được miễn sát thương
			} else {
				if (this.getDirection() == LEFT_DIR) {
					backAnimation.Update(System.nanoTime());
					backAnimation.draw(g2, (int) (this.getPosX() - getGameWorld().getCamera().getPosX()),
							(int) (this.getPosY() - getGameWorld().getCamera().getPosY()));
				} else {
					forwardAnimation.Update(System.nanoTime());
					forwardAnimation.draw(g2, (int) (this.getPosX() - getGameWorld().getCamera().getPosX()),
							(int) (this.getPosY() - getGameWorld().getCamera().getPosY()));
				}
			}
		}
	}
}
