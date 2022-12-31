package gameobjects;

public abstract class GameObject {
	private float posX, posY;
	private GameWorld gameWorld;

	public GameObject() {

	}

	public GameObject(float posX, float posY, GameWorld gameWorld) {
		super();
		this.posX = posX;
		this.posY = posY;
		this.gameWorld = gameWorld;
	}

	public float getPosX() {
		return posX;
	}

	public void setPosX(float posX) {
		this.posX = posX;
	}

	public float getPosY() {
		return posY;
	}

	public void setPosY(float posY) {
		this.posY = posY;
	}

	public GameWorld getGameWorld() {
		return gameWorld;
	}

	public void setGameWorld(GameWorld gameWorld) {
		this.gameWorld = gameWorld;
	}

	// Vì mọi lớp con đều cần Update() nên sẽ dùng phương thức ảo
	public abstract void Update();
}
