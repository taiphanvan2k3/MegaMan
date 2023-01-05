package gameobjects;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Hashtable;

import effect.Animation;
import effect.CacheDataLoader;

public class FinalBoss extends Human {

	private Animation idleForwardAnimation, idleBackAnimation;
	private Animation shootingForwardAnimation, shootingBackAnimation;
	// Skill lướt trên mặt đất
	private Animation slideForwardAnimation, slideBackAnimation;

	// Cứ mỗi khoảng thời gian startTimeForAttacked thì boss sẽ thực hiện tấn công
	private long startTimeForAttacked;

	// Với mỗi loại tấn công sẽ có mỗi thời gian dành cho nó khác nhau.
	private Hashtable<String, Long> timeForAttackType= new Hashtable<>();
	private String[] attackType = new String[4];
	private int indexOfAttackType = 0;

	/*
	 * Xem thử loại tấn công này đã chiếm bao lâu
	 * (lastAttackTime-startTimeForAttack) để biết mà chuyển qua loại tấn côg khác
	 */
	private long lastAttackTime;

	public FinalBoss(float posX, float posY, GameWorld gameWorld) {
		// super(posX, posY, width, height, mass, blood, gameWorld);
		super(posX, posY, 110, 150, 0.1f, 100, gameWorld);
		this.idleBackAnimation = CacheDataLoader.getInstance().getAnimation("boss_idle");
		this.idleForwardAnimation = CacheDataLoader.getInstance().getAnimation("boss_idle");
		this.idleForwardAnimation.flipAllImage();

		this.shootingBackAnimation = CacheDataLoader.getInstance().getAnimation("boss_shooting");
		this.shootingForwardAnimation = CacheDataLoader.getInstance().getAnimation("boss_shooting");
		this.shootingForwardAnimation.flipAllImage();

		this.slideBackAnimation = CacheDataLoader.getInstance().getAnimation("boss_slide");
		this.slideForwardAnimation = CacheDataLoader.getInstance().getAnimation("boss_slide");
		this.slideForwardAnimation.flipAllImage();

		this.setTimeForNoBehurt(500 * 1000000);
		// Khi va chạm với boss sẽ bị trừ 15hp
		this.setDamage(10);

		attackType[0] = "nothing";
		attackType[1] = "shooting";
		attackType[2] = "nothing";
		attackType[3] = "slide";

		// Đơn vị là ms
		timeForAttackType.put(attackType[0], (long) 2000);
		timeForAttackType.put(attackType[1], (long) 500);
		timeForAttackType.put(attackType[3], (long) 5000);
	}

	@Override
	public void Update() {
		// Xử lí va chạm với tường bằng Super.Update();
		super.Update();
		/*
		 * Ban đầu boss hướng về bên trái, nhưng nếu nhân vật nằm bên phải boss thì phải
		 * thay đổi hướng của boss.
		 */
		if (getGameWorld().getMegaman().getPosX() > this.getPosX())
			this.setDirection(RIGHT_DIR);
		else
			this.setDirection(LEFT_DIR);

		// Để tránh việc mới gặp mặt boss mà boss đã tấn công thì
		if (this.startTimeForAttacked == 0)
			this.startTimeForAttacked = System.currentTimeMillis();
		else if (System.currentTimeMillis() - startTimeForAttacked > 300) {
			// Nếu boss đã tấn công được hơn 300ms thì cho nó tấn công lại
			this.attack();
			this.startTimeForAttacked = System.currentTimeMillis();
		}

		String type = this.attackType[this.indexOfAttackType];
		if (type.equals("shooting")) {
			/*
			 * Thời gian cho loại tấn công là shooting kéo dài 0,5s. Khoảng thời gian này là
			 * khá nhiều nên sẽ được gọi rất nhiều lần Update() trong khoảng thời gian đó,
			 * do đó sinh ra rất nhiều đạn.
			 */
			Bullet bullet = new RocketBullet(getPosX(), getPosY() -50, getGameWorld());
			bullet.setTeamType(getTeamType());
			if (this.getDirection() == LEFT_DIR)
				bullet.setSpeedX(-4);
			else
				bullet.setSpeedX(4);
			this.getGameWorld().getBulletManager().addObject(bullet);
		} else if (type.equals("slide")) {
			Rectangle boundWithMap = this.getBoundWithMap();
			/*
			 * Nếu boss va chạm với tường phải thì đổi hướng chuyển động của nó bằng cách
			 * set lại speedX là -5 để nó chuyển động về bên trái. Ngược lại với khi va chạm
			 * với tường trái.
			 */
			if (getGameWorld().getPhysMap().haveCollisionWithLeftWall(boundWithMap) != null)
				this.setSpeedX(5);
			else if (getGameWorld().getPhysMap().haveCollisionWithRightWall(boundWithMap) != null)
				this.setSpeedX(-5);

			this.setPosX(this.getPosX() + this.getSpeedX());
		} else {
			// Nếu boss không làm gì =>Đứng im
			this.setSpeedX(0);
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	@Override
	public void jump() {
		this.setSpeedY(-5);
	}

	@Override
	public void dick() {

	}

	@Override
	public void standUp() {

	}

	@Override
	public void stopRun() {

	}

	@Override
	public void attack() {
		// Chỉ có việc chuyển trạng thái tấn công.
		if (System.currentTimeMillis() - this.lastAttackTime > timeForAttackType
				.get(attackType[this.indexOfAttackType])) {
			this.lastAttackTime = System.currentTimeMillis();
			this.indexOfAttackType++;
			if (this.indexOfAttackType >= this.attackType.length)
				this.indexOfAttackType = 0;
			if (attackType[this.indexOfAttackType].equals("slide")) {
				/*
				 * Nếu boss ở trạng thái lướt thì phải kiểm tra toạ độ X của boss so với nhân
				 * vật megaMan để cho boss lướt theo đúng hướng
				 */
				if (this.getPosX() < this.getGameWorld().getMegaman().getPosX())
					this.setSpeedX(5);
				else
					this.setSpeedX(-5);
			}
		}
	}

	@Override
	public Rectangle getBoundWithEnemy() {
		Rectangle rect = this.getBoundWithMap();
		if (attackType[this.indexOfAttackType].equals("slide")) {
			// Tăng y nhưng phải giảm height để boss nó slide sát đất
			rect.y += 100;
			rect.height -= 100;
		}
		return rect;
	}

	@Override
	public void draw(Graphics2D g2) {
		if (this.getState() == NOBEHURT && (System.nanoTime() / 10000000) % 2 != 1) {
			System.out.println("Boss flash");
		} else {
			if (attackType[this.indexOfAttackType].equals("nothing")) {
				if (this.getDirection() == RIGHT_DIR) {
					idleForwardAnimation.Update(System.nanoTime());
					idleForwardAnimation.draw(g2, (int) (getPosX() - getGameWorld().getCamera().getPosX()),
							(int) (getPosY() - getGameWorld().getCamera().getPosY()));
				} else {
					idleBackAnimation.Update(System.nanoTime());
					idleBackAnimation.draw(g2, (int) (getPosX() - getGameWorld().getCamera().getPosX()),
							(int) (getPosY() - getGameWorld().getCamera().getPosY()));
				}
			} else if (attackType[this.indexOfAttackType].equals("shooting")) {
				if (this.getDirection() == RIGHT_DIR) {
					shootingForwardAnimation.Update(System.nanoTime());
					shootingForwardAnimation.draw(g2, (int) (getPosX() - getGameWorld().getCamera().getPosX()),
							(int) (getPosY() - getGameWorld().getCamera().getPosY()));
				} else {
					shootingBackAnimation.Update(System.nanoTime());
					shootingBackAnimation.draw(g2, (int) (getPosX() - getGameWorld().getCamera().getPosX()),
							(int) (getPosY() - getGameWorld().getCamera().getPosY()));
				}
			} else if (attackType[this.indexOfAttackType].equals("slide")) {
				if (this.getDirection() == RIGHT_DIR) {
					slideForwardAnimation.Update(System.nanoTime());
					slideForwardAnimation.draw(g2, (int) (getPosX() - getGameWorld().getCamera().getPosX()),
							(int) (getPosY() - getGameWorld().getCamera().getPosY()) + 50);
				} else {
					slideBackAnimation.Update(System.nanoTime());
					slideBackAnimation.draw(g2, (int) (getPosX() - getGameWorld().getCamera().getPosX()),
							(int) (getPosY() - getGameWorld().getCamera().getPosY()) + 50);
				}
			}
		}
	}
}
