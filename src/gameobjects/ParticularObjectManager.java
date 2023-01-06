package gameobjects;

import java.awt.Graphics2D;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ParticularObjectManager {
	private GameWorld gameWorld;
	// Để protected để lớp con có thể dùng mà không cần thông qua getter
	protected List<ParticularObject> particularObjects;

	public ParticularObjectManager(GameWorld gameWorld) {
		this.gameWorld = gameWorld;
		/*
		 * Cần thiết phải đồng bộ List vì game gồm 2 luồng là gameLoop và keyEvent cùng
		 * truy cập đến cái list này. Giả sử khi keyEvent gọi shoot thì sẽ sinh ra một
		 * đối tượng bullet và add vào list này, nhưng chưa kịp add thì luồng gameLoop
		 * lại gọi đến có đối tượng nào đó gọi đến shoot để thêm đạn vào list.Do đó cần
		 * phải đồng bộ dữ liệu dùng chung này.
		 */
		this.particularObjects = Collections.synchronizedList(new LinkedList<ParticularObject>());
	}

	public GameWorld getGameWorld() {
		return this.gameWorld;
	}

	public void addObject(ParticularObject obj) {
		synchronized (particularObjects) {
			this.particularObjects.add(obj);
		}
	}

	public void removeObject(ParticularObject particularObject) {
		synchronized (particularObjects) {
			particularObjects.remove(particularObject);
		}
	}

	public boolean onlyExistBoss() {
		return this.particularObjects.size() == 1;
	}

	public ParticularObject getCollisionWithEnemyObject(ParticularObject object) {
		/*
		 * Cũng phải đồng bộ vì lỡ như lúc ta vừa kiểm tra xem có ParticularObject nào
		 * va chạm với object mà lại vừa có một luồng thêm 1 đối tượng particularObject
		 * vào mà đối tượng này lại va chạm với tham số object thì thành ra ta bị sai.
		 */
		synchronized (particularObjects) {
			for (ParticularObject objInList : particularObjects) {
				if (object.getTeamType() != objInList.getTeamType()
						&& object.getBoundWithEnemy().intersects(objInList.getBoundWithEnemy()))
					return objInList;
			}
		}
		return null;
	}

	public void updateObjects() {
		synchronized (particularObjects) {
			// Không nền dùng for each
			for (int i = 0; i < this.particularObjects.size(); i++) {
				ParticularObject obj = this.particularObjects.get(i);
				// Chỉ update những object nào có xuất hiện trong view thôi
				if (!obj.isObjectOutOfCameraView())
					obj.Update();
				/*
				 * Vd như 1 con quái bị tiêu diệt thì ta phải xoá nó ra khỏi list. Chú ý nếu ta
				 * nghĩ là object ra khỏi view là remove nó là sai vì điều này chỉ đúng với viên
				 * đạn thôi,chứ còn như quái thì điều này là sai
				 */
				if (obj.getState() == ParticularObject.DEATH) {
					this.particularObjects.remove(obj);
					System.out.println("number of enemys" + this.particularObjects.size());
				}

			}
		}
	}

	public void draw(Graphics2D g2) {
		for (ParticularObject obj : particularObjects) {
			if (!obj.isObjectOutOfCameraView())
				obj.draw(g2);
		}
	}
}
