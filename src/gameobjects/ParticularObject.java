package gameobjects;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import effect.Animation;

public abstract class ParticularObject extends GameObject {
	/*
	 * LEAGUE_TEAM: những đối tượng cùng phe với nhân vật chính khi ta chạm vào thì
	 * sẽ không mất máu
	 */
	public static final int LEAGUE_TEAM = 1;

	// ENEMY_TEAM: kẻ địch
	public static final int ENEMY_TEAM = 2;

	public static final int LEFT_DIR = 0;
	public static final int RIGHT_DIR = 1;

	public static final int ALIVE = 0;
	public static final int BEHURT = 1;
	public static final int FEY = 2;// sắp hết máu
	public static final int DEATH = 3;
	// khi hồi sinh thì có vài giây không bị mất máu cho dù bị đạn bắn
	public static final int NOBEHURT = 4;

	private int state = ALIVE;
	private float width, height, mass;
	private float speedX, speedY, blood;

	private int damage, direction;
	protected Animation behurtForwardAnimation, behurtBackAnimation;

	private int teamType;// league_team hay enemy_team
	private long startTimeNoBehurt;// thời điểm không bị đau
	private long timeForNoBehurt;// khoảng thời gian không bị đau

	public ParticularObject(float posX, float posY, float width, float height, float mass, int blood,
			GameWorld gameWorld) {
		super(posX, posY, gameWorld);
		this.width = width;
		this.height = height;
		this.blood = blood;
		this.mass = mass;
		// Mặc định là hướng hiện tại về bên phải
		this.direction = RIGHT_DIR;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
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

	public float getBlood() {
		return blood;
	}

	public void setBlood(float blood) {
		if (blood > 0)
			this.blood = blood;
		else
			this.blood = 0;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public Animation getbehurtForwardAnimation() {
		return behurtForwardAnimation;
	}

	public void setbehurtForwardAnimation(Animation behurtForwardAnimation) {
		this.behurtForwardAnimation = behurtForwardAnimation;
	}

	public Animation getbehurtBackAnimation() {
		return behurtBackAnimation;
	}

	public void setbehurtBackAnimation(Animation behurtBackAnimation) {
		this.behurtBackAnimation = behurtBackAnimation;
	}

	public int getTeamType() {
		return teamType;
	}

	public void setTeamType(int teamType) {
		this.teamType = teamType;
	}

	public long getStartTimeNoBehurt() {
		return startTimeNoBehurt;
	}

	public void setStartTimeNoBehurt(long startTimeNoBehurt) {
		this.startTimeNoBehurt = startTimeNoBehurt;
	}

	public long getTimeForNoBehurt() {
		return timeForNoBehurt;
	}

	public void setTimeForNoBehurt(long timeForBehurt) {
		this.timeForNoBehurt = timeForBehurt;
	}

	public void beHurt(int damage) {
		this.setBlood(this.blood - damage);
		this.state = BEHURT;
		/*
		 * Khi gọi hurtingCallBack() thì tuỳ vào việc ParticularObject new MegaMan hay
		 * quái thì sẽ có cách gọi khác nhau, lí do là vì hurtingCallBack() được
		 * Override lại ở lớp con.
		 */
		hurtingCallBack();
	}

	public Rectangle getBoundWithMap() {
		Rectangle r = new Rectangle();
		r.x = (int) (this.getPosX() - this.width / 2);
		r.y = (int) (this.getPosY() - this.height / 2);
		r.height = (int) this.height;
		r.width = (int) this.width;
		return r;
	}

	public boolean isObjectOutOfCameraView() {
		// Kiểm tra object có nằm ngoài view của camera hay không?
		Camera camera = getGameWorld().getCamera();
		if (getPosX() - camera.getPosX() > camera.getWidthView() || getPosX() - camera.getPosX() < -50)
			return true;
		if (getPosY() - camera.getPosY() > camera.getHeigthView() || getPosY() - camera.getPosY() < -50)
			return true;
		return false;
	}

	public void drawCollisionWithMap(Graphics2D g2) {
		Camera camera = this.getGameWorld().getCamera();
		Rectangle rect = this.getBoundWithMap();
		g2.drawRect(rect.x - (int) camera.getPosX(), rect.y - (int) camera.getPosY(), rect.width, rect.height);
	}

	public void drawCollisionWithEnemy(Graphics2D g2) {
		Camera camera = this.getGameWorld().getCamera();
		Rectangle rect = this.getBoundWithEnemy();
		g2.drawRect(rect.x - (int) camera.getPosX(), rect.y - (int) camera.getPosY(), rect.width, rect.height);
	}

	/*
	 * Tuỳ vào đối tượng kế thừa mà sẽ có cách getBoundWithEnemy khác nhau. Chẳng
	 * hạn với megaMan thì getBoundWithEnemy là 1 HCN nhỏ trong getBoundWithMap. Còn
	 * đối với các loại đạn thì getBoundWithEnemy chính là getBoundWithMap của
	 * bullet.
	 */
	public abstract Rectangle getBoundWithEnemy();

	/*
	 * Mỗi đối tượng có những cách vẽ khác nhau do đó ta cũng dùng phương thức trừu
	 * tượng draw
	 */
	public abstract void draw(Graphics2D g2);

	/*
	 * Vì ParticularObject là lớp cơ sở nên chưa biết được từng đối tượng thì sẽ tấn
	 * công như nào nhưng biết chắc chắn là phải có phương thức attack.
	 */
	public abstract void attack();

	public void hurtingCallBack() {
		// Lát có lớp override lại phương thức này
	}

	@Override
	public void Update() {
		switch (state) {
		case ALIVE:
			ParticularObject object = getGameWorld().getParticularObjectManager().getCollisionWithEnemyObject(this);
			if (object != null) {
				/*
				 * Tuỳ vào object có damage hay không.
				 */
				if (object.getDamage() > 0) {
					/*
					 * Này là va chạm với 1 đối tượng không phải đạn do đó ta không thể setBlood=0
					 * đc
					 */
					this.beHurt(object.damage);
				}
			}
			break;
		case BEHURT:
			/*
			 * Khi đạn chưa đến người tức là behurtBackAnimation ==null vì đạn mới vừa chạm
			 * nhân vật chứ chưa xuyên qua người nhân vật
			 */
			if (this.behurtBackAnimation == null) {
				/*
				 * Một khi đã bị bắn thì sẽ bị giảm HP và đc chuyển sang trạng thái nobehurt
				 * trong 1 khoảng thời gian nào đó
				 */
				this.state = NOBEHURT;
				this.startTimeNoBehurt = System.nanoTime();
				if (this.blood == 0) {
					this.state = FEY;
				}
			} else {
				/*
				 * Đạn đã bắn dính người nhân vật thì hiển thị ra animation của trúng đạn (điện
				 * giật,...)
				 */
				behurtForwardAnimation.Update(System.nanoTime());
				if (behurtForwardAnimation.isLastFrame()) {
					this.behurtForwardAnimation.reset();
					/*
					 * Sau khi hết hiệu ứng trúng đạn thì nhân vật được chuyển sang chế độ NOBEHURT
					 * trong 1 khoảng thời gian nào đó
					 */
					state = NOBEHURT;
					this.startTimeNoBehurt = System.nanoTime();
					if (this.blood == 0)
						this.state = FEY;
				}
			}
			break;
		case FEY:
			this.state = DEATH;
			break;
		case DEATH:
			break;
		case NOBEHURT:
			// Khi hết thời gian miễn sát thương thì quay lại trạng thái alive
			if (System.nanoTime() - startTimeNoBehurt > timeForNoBehurt)
				this.state = ALIVE;
			break;
		}
	}
}
