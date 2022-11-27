package effect;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import userInterface.GameFrame;

//Mục đích là tạo ra các hình nhỏ từ ảnh ban đầu
public class FrameImage {
	private String name;
	private BufferedImage image;

	public FrameImage() {
		this.name = null;
		this.image = null;
	}

	public FrameImage(String name, BufferedImage image) {
		this.name = name;
		this.image = image;
	}

	// Xây dựng copy constructor
	public FrameImage(FrameImage frameImage) {
		// Tránh việc rắc rối khi image của frameImage thay đổi thì
		// this.image cũng thay đổi theo => Ta cần new Image mới
		this.image = new BufferedImage(frameImage.getImageWidth(), frameImage.getImageHeight(),
				frameImage.getImage().getType());

		// Lúc này image mới chỉ có kích thước là chiều rộng,chiều cao và loại ảnh
		// như bức ảnh của tham số frameImage chứ chưa có hình ảnh nào cả

		// Bây giờ cần vẽ ảnh lên cái image đó
		// Lấy ra Graphics của image thì g là "cây cọ" của chính image
		// nên khi g.drawImage thì sẽ vẽ lên đối tượng image
		Graphics g = image.getGraphics();
		g.drawImage(frameImage.getImage(), 0, 0, null);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	// Lấy độ lớn bức ảnh
	public int getImageWidth() {
		return image.getWidth();
	}

	public int getImageHeight() {
		return image.getHeight();
	}

	public void draw(Graphics2D g, int x, int y) {
		// Để vẽ toạ độ x,y ở giữa khung hình
		g.drawImage(image, x - this.getImageWidth() / 2, y - this.getImageHeight() / 2, null);
	}
}
