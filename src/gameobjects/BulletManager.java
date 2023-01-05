package gameobjects;

public class BulletManager extends ParticularObjectManager {

	public BulletManager(GameWorld gameWorld) {
		super(gameWorld);
	}

	@Override
	public void updateObjects() {
		/*
		 * Khác với các loại quái khi ra khỏi view thì không được xoá. Nhưng đối với đạn
		 * thì nếu như bay ra khỏi camera thì ta sẽ xoá nó đi. Viên đạn khi bắn trúng
		 * người nhân vật thì cũng bị xoá đi
		 */
		super.updateObjects();
		synchronized (particularObjects) {
			for (int i = 0; i < particularObjects.size(); i++) {
				ParticularObject obj = this.particularObjects.get(i);
				if (obj.isObjectOutOfCameraView() || obj.getState() == ParticularObject.DEATH) {
					particularObjects.remove(obj);
				}

			}
		}
	}
}
