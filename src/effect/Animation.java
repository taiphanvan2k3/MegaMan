package effect;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Animation {
	// Tên để quản lý animation
	private String name;

	// Kiểm tra xem trạng thái (false: animation sẽ không lặp lại)
	private boolean isRepeated;

	// Chứa các ảnh nhỏ để tạo animation
	private ArrayList<FrameImage> frameImages;

	// Chỉ ra vị trí của frame hiện tại
	private int currentFrame;

	// Chỉ ra xem hình ảnh nào không được lặp lại nữa
	// (vd như animation chạy thì sẽ bỏ ra một số frame chạy lúc khởi động
	// nếu quá trình lặp lại được diễn ra
	private ArrayList<Boolean> ignoreFrame;

	// Một mảng chứa giá trị tạo độ trễ để chuyển từ frame này sang frame khác
	private ArrayList<Double> delayFrame;

	private long beginTime;

	// Trong quá trình phát triển, vẽ khung hình cho nhân vật để dễ hình dung
	private boolean drawRectFrame;

	public Animation() {
		isRepeated = true;
		frameImages = new ArrayList<>();
		delayFrame = new ArrayList<>();
		ignoreFrame = new ArrayList<>();
		beginTime = 0;
		currentFrame = 0;
		drawRectFrame = true;
	}

	public Animation(Animation a) {
		isRepeated = a.isRepeated;
		beginTime = a.beginTime;
		currentFrame = a.currentFrame;
		drawRectFrame = a.drawRectFrame;
		frameImages = new ArrayList<>();
		delayFrame = new ArrayList<>();
		ignoreFrame = new ArrayList<>();
		for (Double d : a.delayFrame) {
			this.delayFrame.add(new Double(d));
		}

		for (Boolean b : a.ignoreFrame) {
			this.ignoreFrame.add(new Boolean(b));
		}

		for (FrameImage f : a.frameImages) {
			// Nếu như add(f) thì đang truyền địa chỉ vùng nhớ của f đang trỏ tới
			// vào ArrayList rồi do đó khi đối tượng a hay this thay đổi frame đó thì nó sẽ
			// thay đổi cả 2 bên
			this.frameImages.add(new FrameImage(f));
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean getIsRepeated() {
		return isRepeated;
	}

	public void setRepeated(boolean isRepeated) {
		this.isRepeated = isRepeated;
	}

	public ArrayList<FrameImage> getFrameImages() {
		return frameImages;
	}

	public void setFrameImages(ArrayList<FrameImage> frameImages) {
		this.frameImages = frameImages;
	}

	public int getCurrentFrame() {
		return currentFrame;
	}

	public ArrayList<Boolean> getIgnoreFrame() {
		return ignoreFrame;
	}

	public void setIgnoreFrame(ArrayList<Boolean> ignoreFrame) {
		this.ignoreFrame = ignoreFrame;
	}

	public ArrayList<Double> getDelayFrame() {
		return delayFrame;
	}

	public void setDelayFrame(ArrayList<Double> delayFrame) {
		this.delayFrame = delayFrame;
	}

	public long getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(long beginTime) {
		this.beginTime = beginTime;
	}

	public boolean getDrawRectFrame() {
		return drawRectFrame;
	}

	public void setDrawRectFrame(boolean drawRectFrame) {
		this.drawRectFrame = drawRectFrame;
	}

	public boolean isIgnoreFrame(int id) {
		return this.ignoreFrame.get(id);
	}

	public void setIgnoreFrame(int id) {
		if (id >= 0 && id < this.ignoreFrame.size())
			this.ignoreFrame.set(id, true);
	}

	public void unIgnoreFrame(int id) {
		if (id >= 0 && id < this.ignoreFrame.size())
			this.ignoreFrame.set(id, false);
	}

	public void setCurrentFrame(int id) {
		if (id >= 0 && id < this.frameImages.size())
			this.currentFrame = id;
		else
			this.currentFrame = 0;
	}

	// Chuyển động tác chạy thì sẽ reset lại từ đầu
	// vd đang chạy giữa chừng mà bây giờ đứng lại sau đó chạy tiếp
	// ko lẽ bây giờ chạy tiếp đoạn khúc sau => Vô lí
	// => Cần reset lại quá trình chạy
	public void reset() {
		this.beginTime = 0;
		this.currentFrame = 0;
		for (int i = 0; i < this.ignoreFrame.size(); i++)
			this.ignoreFrame.set(i, false);
	}

	public void add(FrameImage frameImage, double timeToNextFrame) {
		// Add ảnh mới vào arrayList
		this.frameImages.add(frameImage);
		this.ignoreFrame.add(false);
		this.delayFrame.add(timeToNextFrame);
	}

	// Lấy ra image ở vị trí hiện tại
	public BufferedImage getCurrentImage() {
		return this.frameImages.get(currentFrame).getImage();
	}

	public void nextFrame() {
		// Nếu đang ở ảnh cuối thì khi nextFrame thì quay lại ảnh đầu
		if (this.currentFrame >= this.frameImages.size() - 1)
			this.currentFrame = 0;
		else
			this.currentFrame++;

		// Nếu ko cho phép quay lại ảnh ở currentFrame thì gọi đệ quy
		// cho nó quay ảnh mới
		if (this.ignoreFrame.get(currentFrame))
			this.nextFrame();
	}

	public void Update(long l) {
		if (beginTime == 0)
			beginTime = l;
		else {
			if (l - beginTime > this.delayFrame.get(currentFrame)) {
				// Qua ảnh mới
				this.nextFrame();
				// Set tại beginTime
				beginTime = l;
			}
		}
	}

	public boolean isLastFrame() {
		// Kiểm tra xem animation đã xong chuyển động nó chưa
		// (vd nó chạm đất rồi rồi thì cho nó isRepeate=false để không chạm
		// đất nữa)
		return this.currentFrame == this.frameImages.size() - 1;
	}

	public void flipAllImage() {
		// Nhằm mục đích lật ngược tấm hình lại thì không có tấm hình
		// nào chạy sang trái hết mà toàn chạy sang phải
		for (int i = 0; i < this.frameImages.size(); i++) {
			BufferedImage img = this.frameImages.get(i).getImage();

			// Lật ngược ảnh sang trái
			AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
			tx.translate(-img.getWidth(), 0);

			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
			img = op.filter(img, null);

			this.frameImages.get(i).setImage(img);
		}
	}

	//Đa năng hoá phương thức draw
	public void draw(Graphics2D g2, int x, int y) {
		// Vẽ hình hiện tại
		// Muốn nhân vật hiển thị ra tại toạ độ x,y thì phải vẽ làm sao cho x,y là chính
		// giữa của HCN

		/*
		 * C1: BufferedImage img = this.getCurrentImage(); g2.drawImage(img,
		 * x-img.getWidth() / 2, y - img.getHeight() / 2, null);
		 */

		// Thay vì dùng c1 lặp lại code thì gọi FrameImage hiện tại rồi rồi paint thôi
		//vì class FrameImage ta đã xây dựng lại phương thức draw rồi
		FrameImage img = this.getFrameImages().get(currentFrame);
		img.draw(g2, x, y);

		// Vẽ viền bao quanh tấm hình và chú ý là drawRect chứ không fillRect
		g2.setColor(Color.red);
		if (drawRectFrame)
			/*
			 * img là BufferedImage img
			 * C1: 
			 * g2.drawRect(x-img.getWidth()/2,
			 * y-img.getHeight()/2, img.getWidth(), img.getHeight());
			 */
			g2.drawRect(x - img.getImageWidth() / 2, y - img.getImageHeight() / 2, img.getImageWidth(),
					img.getImageHeight());
	}
}
