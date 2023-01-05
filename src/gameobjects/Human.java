package gameobjects;

import java.awt.Graphics2D;
import java.awt.Rectangle;

public abstract class Human extends ParticularObject {

	private boolean isJumping;
	private boolean isSquating;// ngồi xỏm
	private boolean isLanding;// trạng thái khuỵ gối/chống tay khi tiếp xúc đất

	public Human(float posX, float posY, float width, float height, float mass, int blood, GameWorld gameWorld) {
		super(posX, posY, width, height, mass, blood, gameWorld);
		this.setState(ALIVE);
	}

	public abstract void run();

	public abstract void jump();

	public abstract void dick();

	public abstract void standUp();

	public abstract void stopRun();

	public boolean isJumping() {
		return isJumping;
	}

	public void setJumping(boolean isJumping) {
		this.isJumping = isJumping;
	}

	public boolean isSquating() {
		return isSquating;
	}

	public void setSquating(boolean isSquating) {
		this.isSquating = isSquating;
	}

	public boolean isLanding() {
		return isLanding;
	}

	public void setLanding(boolean isLanding) {
		this.isLanding = isLanding;
	}

	@Override
	public void Update() {
		// Gọi đến Update() ở lớp cha
		super.Update();// update animation

		if (super.getState() == ALIVE || super.getState() == NOBEHURT) {
			/*
			 * Một khi isLanding=false thì nhân vật chưa chạm đất do đó ta cần cập nhật toạ
			 * độ cho nhân vật như nhảy lên,rơi xuống,va chạm với địa hình
			 */

			/*
			 * Còn một khi isLanding=true thì nhân vật đã đứng trên đất rồi thì không cần
			 * cập nhật nữa
			 */
			if (!this.isLanding) {
				/*
				 * Cộng thêm speedX vào để nhân vật có toạ độ X mới nhưng phải xử lí thêm nếu có
				 * va chạm với tường.
				 */
				super.setPosX(super.getPosX() + super.getSpeedX());

				PhysicalMap physicalMap = super.getGameWorld().getPhysMap();
				Rectangle boundWithMap = super.getBoundWithMap();
				Rectangle temp = null;
				if (super.getDirection() == LEFT_DIR
						&& (temp = physicalMap.haveCollisionWithLeftWall(boundWithMap)) != null) {
					/*
					 * Lúc này nhân vật chạm với tường bên trái nên toạ độ mới của nhân vật (posX
					 * toạ độ của tâm HCN bao xung quanh nhân vật) sẽ tính từ toạ độ mép bên phải
					 * của block va chạm cộng thêm width()/2 của nhân vật
					 */
					super.setPosX(temp.x + temp.width + super.getWidth() / 2);
				} else if (super.getDirection() == RIGHT_DIR
						&& (temp = physicalMap.haveCollisionWithRightWall(boundWithMap)) != null) {
					super.setPosX(temp.x - super.getWidth() / 2);
				}

				/*
				 * Đoạn code dưới đây kiểm tra có va chạm với nền nhà hay trần nhà không. Cộng y
				 * thêm speedY hoặc 2 để kiểm tra trong tương lai nhân vật có va chạm với nền
				 * nhà hay trần nhà hay không.
				 */

				/*
				 * Do ở trên có thể đã có thể vào if để thay đổi toạ độ nhân vật => Bound cx
				 * thay đổi =>Cần tìm bound lại
				 */
				Rectangle boundForCollisionWithMapFuture = getBoundWithMap();
				boundForCollisionWithMapFuture.y += (getSpeedY() != 0 ? getSpeedY() : 2);
				Rectangle rectLand = super.getGameWorld().getPhysMap()
						.haveCollisionWithLand(boundForCollisionWithMapFuture);
				Rectangle rectCeil = super.getGameWorld().getPhysMap()
						.haveCollisionWithCeil(boundForCollisionWithMapFuture);
				if (rectCeil != null) {
					// Khi chạm trần thì speedY đang âm giờ bằng 0
					super.setSpeedY(0);
					super.setPosY(rectCeil.y + rectCeil.height + super.getHeight() / 2);
				} else if (rectLand != null) {
					// Do chạm đất tức là trước đó đã có động tác nhảy
					this.isJumping = false;
					if (super.getSpeedY() > 0) {
						 this.isLanding = true;
						/*
						 * trạng thái tiếp đất,khuỵ chân set lại speedY =0 để khi chạm đất rồi thì không
						 * còn trạng thái tiếp đất,khuỵ chân nữa
						 */
					}

					super.setSpeedY(0);
					// Xem hiệu ứng của nhân vật khi có thêm -1
					super.setPosY(rectLand.y - super.getHeight() / 2 - 1);
				} else {
					/*
					 * Nếu không chạm đất hay trần thì nhân vật đang có trạng thái nhảy
					 */

					/*
					 * Nếu nhảy lên thì tốc độ ngày càng giảm do cộng với mass >0 trong khi speedY
					 * hiện tại đang đi lên nên <0 - Nếu rơi xuống thì cộng mass nên tốc độ càng
					 * tăng do speedY>0
					 */
					this.isJumping = true;
					this.isLanding = false;
					this.setSpeedY(this.getSpeedY() + this.getMass());
					this.setPosY(this.getPosY() + super.getSpeedY());
				}
			}
		}
	}

	@Override
	public Rectangle getBoundWithEnemy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void draw(Graphics2D g2) {

	}

	@Override
	public void attack() {
		// TODO Auto-generated method stub

	}

}
