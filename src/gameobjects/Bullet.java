package gameobjects;

import java.awt.Graphics2D;

public abstract class Bullet extends ParticularObject {

	public Bullet(float posX, float posY, float width, float height, float mass, int damage, GameWorld gameWorld) {
		// Thực chất là lớp này nhận vào int blood chứ không phải damage
		// do đó không quan tâm đến blood mà sẽ quan tâm do đó mới gán là 1
		super(posX, posY, width, height, mass, 1, gameWorld);
		this.setDamage(damage);
	}

	// Mỗi viên đạn có 1 cách vẽ khác nhau
	public abstract void draw(Graphics2D g2);

	@Override
	public void Update() {
		/*
		 * Phương thức này sẽ được Override lại tại lớp con Chẳng hạn như ở lớp BlueFire
		 * không hề dùng lại đoạn code Update này, còn RedEyeBullet lại sử dụng nguyên
		 * phương thức này
		 */
		super.Update();
		setPosX(this.getPosX() + getSpeedX());
		setPosY(this.getPosY() + getSpeedY());
		// Trả về đối tượng (megaman hoặc quái ) va chạm với viên đạn này
		ParticularObject object = getGameWorld().getParticularObjectManager().getCollisionWithEnemyObject(this);
		if (object != null && object.getState() == ALIVE) {
			/*
			 * Viên đạn hết hiệu lực nên setBlood(0) để nó xoá viên đạn này khỏi
			 * BulletManager
			 */
			this.setBlood(0);

			/*
			 * Đối tượng (megaman hay quái) sẽ bị trừ HP do damage của đạn Trong phương thức
			 * beHurt đã set lại trạng thái là beHurt rồi.
			 */
			object.beHurt(this.getDamage());
			System.out.println("Bullet are behurt for megaman");
		}
	}
}
