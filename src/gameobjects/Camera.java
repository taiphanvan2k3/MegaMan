package gameobjects;

public class Camera extends GameObject {
	// Chiều rộng,chiều cao mà camera có thể bao trùm
	private float widthView;
	private float heigthView;

	// Khi isLocked=true thì camera nó không chạy theo nhân vật
	private boolean isLocked = false;

	public Camera(float posX, float posY, float widthView, float heightView, GameWorld gameWorld) {
		super(posX, posY, gameWorld);
		this.heigthView = heightView;
		this.widthView = widthView;
	}

	public void lock() {
		this.isLocked = true;
	}

	public void unlock() {
		this.isLocked = false;
	}

	@Override
	public void Update() {
		// Khi gặp boss cuối, camera sẽ không thay đổi vị trí
		if (!isLocked) {
			megaMan mainCharacter = super.getGameWorld().getMegaman();
			/*
			 * Nếu như nhân vật có toạ độ x=700,toạ độ x của camera là 200 thì camera sẽ di
			 * chuyển đến toạ độ x=700-400=300, để có thể quan sát được nhân vật Còn nếu
			 * nhân vật có x=500,camera có x=450 thì camera sẽ được dịch về trái tại toạ độ
			 * x=500-200=300
			 * 
			 * Do đó việc dùng if dưới đây là để camera có thể đi theo nhân vật và các thông
			 * số như 400,200 này là dựa vào kết quả chạy chương trình để thay đổi cho phù
			 * hợp
			 */
			if (mainCharacter.getPosX() - this.getPosX() > 400)
				this.setPosX(mainCharacter.getPosX() - 400);
			else if (mainCharacter.getPosX() - this.getPosX() < 200)
				this.setPosX(mainCharacter.getPosX() - 200);

			if (mainCharacter.getPosY() - this.getPosY() > 400)
				this.setPosY(mainCharacter.getPosY() - 400);
			else if (mainCharacter.getPosY() - this.getPosY() < 200)
				this.setPosY(mainCharacter.getPosY() - 250);
		}
	}

	public float getWidthView() {
		return widthView;
	}

	public void setWidthView(float widthView) {
		this.widthView = widthView;
	}

	public float getHeigthView() {
		return heigthView;
	}

	public void setHeigthView(float heigthView) {
		this.heigthView = heigthView;
	}

}
