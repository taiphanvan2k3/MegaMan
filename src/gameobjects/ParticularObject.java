package gameobjects;

import effect.Animation;

public class ParticularObject extends GameObject {
	// LEAGUE_TEAM: những đối tượng cùng phe với nhân vật chính
	// khi ta chạm vào thì sẽ không mất máu
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
	protected Animation behurtForwardCharacter, behurtBackCharacter;

	private int teamType;// league_team hay enemy_team
	private long startTimeNoBehurt;// thời điểm không bị đau
	private long timeForBehurt;// khoảng thời gian không bị đau

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

	@Override
	public void Update() {
		// TODO Auto-generated method stub

	}

}
