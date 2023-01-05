package gameobjects;

import java.applet.AudioClip;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import effect.Animation;
import effect.CacheDataLoader;
import userInterface.GameFrame;
import userInterface.GamePanel;

public class megaMan extends Human {
	public static final int RUNSPEED = 3;
	private Animation runForwardAnimation, runBackAnimation, runShootingForwardAnimation, runShootingBackAnimation;
	private Animation idleForwardAnimation, idleBackAnimation, idleShootingForwardAnimation, idleShootingBackAnimation;
	private Animation dickForwardAnimation, dickBackAnimation;
	private Animation flyForwardAnimation, flyBackAnimation, flyShootingForwardAnimation, flyShootingBackAnimation;
	private Animation landingForwardAnimation, landingBackAnimation;

	private Animation climWallForwardAnimation, climWallBackAnimation;
	private long lastShootingTime;
	private boolean isShooting = false;
	private AudioClip hurtingSound;
	private AudioClip shooting;

	public megaMan(float posX, float posY, GameWorld gameWorld) {
		super(posX, posY, 70, 90, 0.1f, 100, gameWorld);
		// Megaman thuộc team đồng minh
		this.setTeamType(LEAGUE_TEAM);
		this.setTimeForNoBehurt(2000 * 1000000);
		// 2*10^9 ns. Đây là khoảng thời gian mà nhân vật nó nhấp nháy

		this.shooting = CacheDataLoader.getInstance().getSound("bluefireshooting");
		this.hurtingSound = CacheDataLoader.getInstance().getSound("megamanhurt");
		
		runForwardAnimation = CacheDataLoader.getInstance().getAnimation("run");
		runBackAnimation = CacheDataLoader.getInstance().getAnimation("run");
		/*
		 * Do phương thức getAnimation tuy lấy từ Hashtable phần tử có key là "run" để
		 * trả về Animation nhưng thay vì trả về trực tiếp như vậy thì ta cấp phát một
		 * vùng nhớ mới cho Animation đó, do đó không sợ runBackAnimation.flipAllImage
		 * ảnh hưởng đến runBackAnimation
		 */
		runBackAnimation.flipAllImage();

		idleForwardAnimation = CacheDataLoader.getInstance().getAnimation("idle");
		idleBackAnimation = CacheDataLoader.getInstance().getAnimation("idle");
		idleBackAnimation.flipAllImage();

		dickForwardAnimation = CacheDataLoader.getInstance().getAnimation("dick");
		dickBackAnimation = CacheDataLoader.getInstance().getAnimation("dick");
		dickBackAnimation.flipAllImage();

		flyForwardAnimation = CacheDataLoader.getInstance().getAnimation("flyingup");
		flyBackAnimation = CacheDataLoader.getInstance().getAnimation("flyingup");
		flyBackAnimation.flipAllImage();

		landingForwardAnimation = CacheDataLoader.getInstance().getAnimation("landing");
		landingBackAnimation = CacheDataLoader.getInstance().getAnimation("landing");
		landingBackAnimation.flipAllImage();

		climWallBackAnimation = CacheDataLoader.getInstance().getAnimation("clim_wall");
		climWallForwardAnimation = CacheDataLoader.getInstance().getAnimation("clim_wall");
		climWallForwardAnimation.flipAllImage();

		behurtForwardAnimation = CacheDataLoader.getInstance().getAnimation("hurting");
		behurtBackAnimation = CacheDataLoader.getInstance().getAnimation("hurting");
		behurtBackAnimation.flipAllImage();

		idleShootingForwardAnimation = CacheDataLoader.getInstance().getAnimation("idleshoot");
		idleShootingBackAnimation = CacheDataLoader.getInstance().getAnimation("idleshoot");
		idleShootingBackAnimation.flipAllImage();

		runShootingForwardAnimation = CacheDataLoader.getInstance().getAnimation("runshoot");
		runShootingBackAnimation = CacheDataLoader.getInstance().getAnimation("runshoot");
		runShootingBackAnimation.flipAllImage();

		flyShootingForwardAnimation = CacheDataLoader.getInstance().getAnimation("flyingupshoot");
		flyShootingBackAnimation = CacheDataLoader.getInstance().getAnimation("flyingupshoot");
		flyShootingBackAnimation.flipAllImage();
	}

	@Override
	public Rectangle getBoundWithEnemy() {
		Rectangle rect = super.getBoundWithMap();
		if (super.isSquating()) {
			// Nếu nhân vật đang ngồi sỏm
			rect.x = (int) super.getPosX() - 22;
			rect.y = (int) super.getPosY() - 20;
			rect.width = 44;
			rect.height = 65;
		} else {
			rect.x = (int) super.getPosX() - 22;
			rect.y = (int) super.getPosY() - 40;
			rect.width = 44;
			rect.height = 80;
		}
		return rect;
	}

	@Override
	public void run() {
		if (getDirection() == LEFT_DIR)
			super.setSpeedX(-3);
		else
			setSpeedX(3);
	}

	@Override
	public void jump() {
		if (!this.isJumping()) {
			// Nếu như không bay thì mới cho nhân vật bay
			this.setJumping(true);
			this.setSpeedY(-5.0f);
		} else {
			/*
			 * Nếu nhảy rồi thì không cho nhảy tiếp nữa. Nhưng nếu nhảy mà dính lên tường
			 * thì cho phép nhảy lên
			 */
			Rectangle rectRightWall = new Rectangle(getBoundWithMap());
			rectRightWall.x += 1;

			Rectangle rectLeftWall = new Rectangle(getBoundWithMap());
			rectLeftWall.x -= 1;
			if (super.getGameWorld().getPhysMap().haveCollisionWithRightWall(rectRightWall) != null
					&& getSpeedX() > 0) {
				// Nếu chạm tường thì cho nhân vật nhảy lên tường và posY-=5
				this.setSpeedY(-5.0f);
			} else if (super.getGameWorld().getPhysMap().haveCollisionWithLeftWall(rectLeftWall) != null
					&& getSpeedX() < 0) {
				this.setSpeedY(-5f);
			}
		}
		flyBackAnimation.reset();
		flyForwardAnimation.reset();
	}

	@Override
	public void dick() {
		// dick và squat là ngồi xỏm
		if (!this.isJumping())
			this.setSquating(true);
	}

	@Override
	public void standUp() {
		this.setSquating(false);
		idleForwardAnimation.reset();
		idleBackAnimation.reset();
		dickBackAnimation.reset();
		dickForwardAnimation.reset();
	}

	@Override
	public void stopRun() {
		this.setSpeedX(0);
		runBackAnimation.reset();
		runBackAnimation.reset();

		// Khi dừng chạy thì cho chạy lại (unIgnore) animation đã bị ignore trước đó
		runForwardAnimation.unIgnoreFrame(0);
		runShootingBackAnimation.unIgnoreFrame(0);
	}

	@Override
	// Xem xét việc đưa attack() ở Human về abstract luôn
	public void attack() {
		// Nếu như bắn xồng phát đạn rồi và đang không ngồi thì mới được bắn
		if (!this.isShooting && !super.isSquating()) {
			// Bật nhạc của việc nghe âm thanh
			shooting.play();

			/*
			 * Bắn tại vị trí nhân vật nên đạn cũng xuất phát tại tâm (x,y) của HCN bao xung
			 * quanh nhân vật. Nhưng đây chỉ mới là tạm thời vì vào if ta sẽ cập nhật lại để
			 * đúng với nòng súng
			 */

			Bullet bullet = new BlueFire(getPosX(), getPosY(), getGameWorld());
			if (getDirection() == LEFT_DIR) {
				// Cứ giả sử ban đầu là nhân vật đang đứng bắn
				bullet.setSpeedX(-10);
				bullet.setPosX(bullet.getPosX() - 40);

				if (this.getSpeedX() != 0 && getSpeedY() == 0) {
					// Nếu nhân vật đang chạy
					bullet.setPosX(bullet.getPosX() - 10);

					/*
					 * Trừ 5 để đưa vị trí viên đạn bắn ra, vì như đã nói ở trên thì mới đâu viên
					 * đạn được set vị trí tại tâm HCN, nhưng như vậy là không hợp lí, do đó mới -5
					 * để đưa vị trí viên đạn bay ra đi lên.
					 */
					bullet.setPosY(bullet.getPosY() - 5);
				}
			} else {
				// Nếu nhân vật đang đứng bắn
				bullet.setSpeedX(10);
				bullet.setPosX(bullet.getPosX() + 40);

				if (this.getSpeedX() != 0 && getSpeedY() == 0) {
					// Nếu nhân vật đang chạy
					bullet.setPosX(bullet.getPosX() + 10);
					bullet.setPosY(bullet.getPosY() - 5);
				}
			}

			/*
			 * Nếu đang nhảy thì vị trí viên đạn bắn ra cũng sẽ khác nên mới chỉnh lại bằng
			 * cách -20 px
			 */
			if (this.isJumping())
				bullet.setPosY(bullet.getPosY() - 20);

			/*
			 * Do đây là viên đạn của megaMan nên nó sẽ không gây ra sát thương với megaMan
			 */
			bullet.setTeamType(this.getTeamType());
			this.getGameWorld().getBulletManager().addObject(bullet);

			lastShootingTime = System.nanoTime();
			// Sau 0.5s thì sẽ cho bắn tiếp (hàm update trong lớp này)
			// Việc vẽ ra đạn bay đã được vẽ ở hàm draw
			this.isShooting = true;
		}
	}

	@Override
	public void hurtingCallBack() {
		System.out.println("Call back hurting");
		hurtingSound.play();
	}

	public void draw(Graphics2D g2) {
		switch (this.getState()) {
		case ALIVE:
		case NOBEHURT:
			if (getState() == NOBEHURT && (System.nanoTime() / 10000000) % 2 != 1) {
				/*
				 * Nếu đang ở trạng thái miễn thương và thời gian nhấp nháy tuỳ thuộc vào thực
				 * tế mà sẽ có khoảng thời gian nhấp nháy khác nhau
				 */
			} else {
				/*
				 * Tiếp đất.Khi tiếp đất chỉ thực hiện mỗi việc tiếp đất chứ không làm được việc
				 * gì khác
				 */
				if (this.isLanding()) {
					if (this.getDirection() == RIGHT_DIR) {
						landingForwardAnimation.setCurrentFrame(landingBackAnimation.getCurrentFrame());
						landingForwardAnimation.draw(g2,
								(int) (this.getPosX() - this.getGameWorld().getCamera().getPosX()),
								(int) (getPosY() - getGameWorld().getCamera().getPosY()
										+ (getBoundWithMap().getHeight() / 2
												- landingForwardAnimation.getCurrentImage().getHeight() / 2)));
						/*
						 * Do chiều cao của getBoundWithMap() có thể lớn hơn chiều cao của khung ảnh, do
						 * đó nếu không cộng thêm lượng (...) thì khi vẽ tại toạ độ x,y đó nhân vật sẽ
						 * có một khoảng hở so với mặt đất.Còn việc chia 2 là để nhân vật tại tâm HCN
						 */
					} else {
						// Nếu hướng chuyển động là hướng trái thì dùng landingBackAnimation
						landingBackAnimation.draw(g2, (int) (getPosX() - getGameWorld().getCamera().getPosX()),
								(int) (getPosY() - getGameWorld().getCamera().getPosY() + (getBoundWithMap().height / 2
										- landingBackAnimation.getCurrentImage().getHeight() / 2)));
					}
				} else if (this.isJumping()) {
					if (this.getDirection() == RIGHT_DIR) {
						flyForwardAnimation.Update(System.nanoTime());
						if (this.isShooting) {
							/*
							 * Giả sử đang bay mà ta bắn thì không thể lấy lại frame đầu tiên trong
							 * flyShooting mà phải lấy frame tương ứng với thời điển nhảy lên hiện tại. Do
							 * đó ta mới cần setCurrentFrame cho flyShooting. Và do bắn về bên phải nên mới
							 * cộng thêm cho toạ độ x một lượng 10px kiểu như cho nhân vật không bị trượt về
							 * bên trái do bắn
							 */
							flyShootingForwardAnimation.setCurrentFrame(flyForwardAnimation.getCurrentFrame());
							flyShootingForwardAnimation.draw(g2,
									(int) (getPosX() - getGameWorld().getCamera().getPosX() + 10),
									(int) (getPosY() - getGameWorld().getCamera().getPosY()));
						} else {
							/*
							 * Nếu không bắn thì chỉ đơn giản hiển thị ra frame nhảy thôi
							 */
							flyForwardAnimation.draw(g2, (int) (getPosX() - getGameWorld().getCamera().getPosX()),
									(int) (getPosY() - getGameWorld().getCamera().getPosY()));
						}
					} else {
						flyBackAnimation.Update(System.nanoTime());
						if (this.isShooting) {
							/*
							 * Và do bắn về bên trái nên mới trừ đi cho toạ độ x một lượng 10px kiểu như cho
							 * nhân vật không bị trượt về bên phải do bắn
							 */
							flyShootingBackAnimation.setCurrentFrame(flyForwardAnimation.getCurrentFrame());
							flyShootingBackAnimation.draw(g2,
									(int) (getPosX() - getGameWorld().getCamera().getPosX() - 10),
									(int) (getPosY() - getGameWorld().getCamera().getPosY()));
						} else {
							/*
							 * Nếu không bắn thì chỉ đơn giản hiển thị ra frame nhảy thôi
							 */
							flyBackAnimation.draw(g2, (int) (getPosX() - getGameWorld().getCamera().getPosX()),
									(int) (getPosY() - getGameWorld().getCamera().getPosY()));
						}
					}
				} else if (this.isSquating()) {
					// <=> dicking, khác nhau về mặt từ ngữ thôi. Ý chỉ là nhân vật ngồi xuống
					if (getDirection() == RIGHT_DIR) {
						dickForwardAnimation.Update(System.nanoTime());
						dickForwardAnimation.draw(g2, (int) (getPosX() - getGameWorld().getCamera().getPosX()),
								(int) (getPosY() - getGameWorld().getCamera().getPosY()
										+ (getBoundWithMap().getHeight() / 2
												- dickForwardAnimation.getCurrentImage().getHeight() / 2)));
					} else {
						dickBackAnimation.Update(System.nanoTime());
						dickBackAnimation.draw(g2, (int) (getPosX() - getGameWorld().getCamera().getPosX()),
								(int) (getPosY() - getGameWorld().getCamera().getPosY()
										+ (getBoundWithMap().getHeight() / 2
												- dickBackAnimation.getCurrentImage().getHeight() / 2)));
					}
				} else {
					// rur or idleShooting
					if (getSpeedX() > 0) {
						runForwardAnimation.Update(System.nanoTime());
						if (isShooting) {
							runShootingForwardAnimation.setCurrentFrame(runForwardAnimation.getCurrentFrame() - 1);
							runShootingForwardAnimation.draw(g2,
									(int) (getPosX() - getGameWorld().getCamera().getPosX()),
									(int) (getPosY() - getGameWorld().getCamera().getPosY()));
						} else
							runForwardAnimation.draw(g2, (int) (getPosX() - getGameWorld().getCamera().getPosX()),
									(int) (getPosY() - getGameWorld().getCamera().getPosY()));
						if (runForwardAnimation.getCurrentFrame() == 1)
							runForwardAnimation.setIgnoreFrame(0);
					} else if (getSpeedX() < 0) {
						// run in the left direction
						runBackAnimation.Update(System.nanoTime());
						if (isShooting) {
							runShootingBackAnimation.setCurrentFrame(runBackAnimation.getCurrentFrame() - 1);
							runShootingBackAnimation.draw(g2, (int) (getPosX() - getGameWorld().getCamera().getPosX()),
									(int) (getPosY() - getGameWorld().getCamera().getPosY()));
						} else
							runBackAnimation.draw(g2, (int) (getPosX() - getGameWorld().getCamera().getPosX()),
									(int) (getPosY() - getGameWorld().getCamera().getPosY()));
						if (runBackAnimation.getCurrentFrame() == 1)
							runBackAnimation.setIgnoreFrame(0);
					} else {
						// idle
						if (getDirection() == RIGHT_DIR) {
							if (isShooting) {
								// idleShooting
								idleShootingForwardAnimation.Update(System.nanoTime());
								idleShootingForwardAnimation.draw(g2,
										(int) (getPosX() - getGameWorld().getCamera().getPosX()),
										(int) (getPosY() - getGameWorld().getCamera().getPosY()));
							} else {
								idleForwardAnimation.Update(System.nanoTime());
								idleForwardAnimation.draw(g2, (int) (getPosX() - getGameWorld().getCamera().getPosX()),
										(int) (getPosY() - getGameWorld().getCamera().getPosY()));
							}
						} else {
							if (isShooting) {
								// idleShooting
								idleShootingBackAnimation.Update(System.nanoTime());
								idleShootingBackAnimation.draw(g2,
										(int) (getPosX() - getGameWorld().getCamera().getPosX()),
										(int) (getPosY() - getGameWorld().getCamera().getPosY()));
							} else {
								idleBackAnimation.Update(System.nanoTime());
								idleBackAnimation.draw(g2, (int) (getPosX() - getGameWorld().getCamera().getPosX()),
										(int) (getPosY() - getGameWorld().getCamera().getPosY()));
							}
						}
					}
				}
			}
			break;
		case BEHURT:
			if (getDirection() == RIGHT_DIR) {
				behurtForwardAnimation.draw(g2, (int) (getPosX() - getGameWorld().getCamera().getPosX()),
						(int) (getPosY() - getGameWorld().getCamera().getPosY()));
			} else {
				behurtBackAnimation.setCurrentFrame(behurtForwardAnimation.getCurrentFrame());
				behurtBackAnimation.draw(g2, (int) (getPosX() - getGameWorld().getCamera().getPosX()),
						(int) (getPosY() - getGameWorld().getCamera().getPosY()));
			}
			break;
		case FEY:
			break;
		}
		/*
		 * Chủ yếu để test ms dùng drawCollisionWithMap và drawCollisionWithEnemy thôi
		 * super.drawCollisionWithMap(g2); super.drawCollisionWithEnemy(g2);
		 */

	}

	@Override
	public void Update() {
		/*
		 * super.Update() này là dùng để cập nhập vị trí cho nhân vật, xử lí va chạm
		 */
		super.Update();
		if (isShooting) {
			/*
			 * Xem khoảng thời gian đã bắn đạn ra đã lớn hơn 0.5s chưa mới được cho bắn tiếp
			 * Khoảng thời gian cho animation shooting là 0.5s
			 * 
			 */
			if (System.nanoTime() - lastShootingTime > 500 * 1000000) {
				isShooting = false;
			}
		}
		if (super.isLanding()) {
			/*
			 * Nếu landing thì chạy cho đến khi nào hết animation này thì thôi và chuyển qua
			 * gán lại isLanding=false
			 */
			landingBackAnimation.Update(System.nanoTime());
			if (landingBackAnimation.isLastFrame()) {
				super.setLanding(false);
				landingBackAnimation.reset();
				runForwardAnimation.reset();
				runBackAnimation.reset();
			}
		}
	}
}
