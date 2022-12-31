package gameobjects;

import java.awt.Graphics2D;

public class GameWorld {
	private megaMan megaman;
	private PhysicalMap physMap;

	public GameWorld() {
		megaman = new megaMan(300, 465, 100, 100, 0.1f, this);
		physMap = new PhysicalMap(0, 0,this);
	}

	public megaMan getMegaman() {
		return megaman;
	}

	public void setMegaman(megaMan megaman) {
		this.megaman = megaman;
	}

	public PhysicalMap getPhysMap() {
		return physMap;
	}

	public void setPhysMap(PhysicalMap physMap) {
		this.physMap = physMap;
	}

	public void Update() {
		// Chúng ta sẽ không update,render trong GamePanel nữa
		// mà render trong GameWorld luôn
		megaman.update();
	}

	public void Render(Graphics2D g2) {
		// draw object games here
		physMap.draw(g2);
		megaman.draw(g2);
	}
}
