package effect;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Hashtable;

import javax.imageio.ImageIO;

//Lớp này dùng để load dữ liệu từ file lên
public class CacheDataLoader {
	// Dùng design pattern singleton tạo ra instance để tránh việc
	// tạo ra nhiều đối tượng
	private static CacheDataLoader instance = null;

	// Dùng hashtable để truy xuất nhanh theo key là tên của
	// frameImage hay animation
	private Hashtable<String, FrameImage> frameImages;
	private Hashtable<String, Animation> animations;

	private String frameFile = "data/frame.txt";
	private String animationFile = "data/animation.txt";
	private String physMapFile = "data/phys_map.txt";

	// Mảng 2 chiều để chứa 0,1 thể hiện việc có in ra map hay không.
	int phys_map[][];

	// Dùng design pattern này thì không cho tạo constructor
	// mà lấy constructor thông qua biến static
	private CacheDataLoader() {
		this.frameImages = new Hashtable<String, FrameImage>();
		this.animations = new Hashtable<String, Animation>();
	}

	public static CacheDataLoader getInstance() {
		if (instance == null)
			instance = new CacheDataLoader();
		return instance;
	}

	public void LoadData() throws IOException {
		// Phải loadFrame trước mới được phép loadAnimation vì Animation gồm nhiều
		// FrameImage
		this.LoadFrame();
		this.LoadAnimation();
		this.LoadPhysMap();
	}

	public int[][] getPhysicalMap() {
		return this.phys_map;
	}

	private void LoadFrame() throws IOException {
		File f = new File(frameFile);
		// C1:
		/*
		 * try { BufferedReader br=Files.newBufferedReader(f.toPath()); } catch
		 * (IOException e) { e.printStackTrace(); }
		 */
		FileReader fr = new FileReader(frameFile);
		BufferedReader br = new BufferedReader(fr);

		// Chú ý null khác với rỗng ""
		if (br.readLine() == null) {
			System.out.println("No data");
			return;
		}

		// Nếu file không rỗng mà dòng đầu tiên của file có dữ liệu thì
		// ta vô tình bỏ qua dòng đó do đó new lại
		fr = new FileReader(frameFile);
		br = new BufferedReader(fr);

		String line = null;
		while ((line = br.readLine()).equals("")) {
		}
		int n = Integer.valueOf(line);
		for (int i = 0; i < n; i++) {
			FrameImage frame = new FrameImage();
			while ((line = br.readLine()).equals("")) {
				// Do tăng độ khó ho việc đọc file hoặc vô tình mà tạo ra các khoảng trống dư
				// thừa.Cho lặp miết để đọc bớt đi các dòng trống dư thừa
			}

			// Chạy xong thì line là dòng số
			frame.setName(line);
			while ((line = br.readLine()).equals("")) {
				// nothing
			}

			String ds[] = line.split(" ");
			String path = ds[1];
			while ((line = br.readLine()).equals("")) {
				// nothing
			}

			ds = line.split(" ");
			int x = Integer.valueOf(ds[1]);
			while ((line = br.readLine()).equals("")) {
				// nothing
			}
			ds = line.split(" ");
			int y = Integer.valueOf(ds[1]);

			while ((line = br.readLine()).equals("")) {
				// nothing
			}
			ds = line.split(" ");
			int witdth = Integer.valueOf(ds[1]);
			while ((line = br.readLine()).equals("")) {
				// nothing
			}

			ds = line.split(" ");
			int height = Integer.valueOf(ds[1]);

			// img chứa bức ảnh lớn còn subImage chứa bức ảnh con lấy tại toạ độ x,y với
			// kích thước width,height
			BufferedImage img = ImageIO.read(new File(path));
			BufferedImage subImage = img.getSubimage(x, y, witdth, height);
			frame.setImage(subImage);

			frameImages.put(frame.getName(), frame);
		}

	}

	public Animation getAnimation(String name) {
		Animation res = new Animation(this.animations.get(name));
		return res;
	}

	public FrameImage getFrameImage(String name) {
		// new ra vùng nhớ mới tránh bị tham chiếu đến cùng 1 vùng nhớ
		// Ở đây do ta đã xây dựng copy constructor rồi nên nó sẽ đi gán giá trị thôi
		// chứ không gán địa chỉ
		FrameImage res = new FrameImage(this.frameImages.get(name));
		return res;
	}

	public void LoadAnimation() throws IOException {
		FileReader fr = new FileReader(animationFile);
		BufferedReader br = new BufferedReader(fr);

		// Chú ý null khác với rỗng ""
		if (br.readLine() == null) {
			System.out.println("No data");
			br.close();
			return;
		}

		// Nếu file không rỗng mà dòng đầu tiên của file có dữ liệu thì
		// ta vô tình bỏ qua dòng đó do đó new lại
		fr = new FileReader(animationFile);
		br = new BufferedReader(fr);

		String line = br.readLine();
		int n = Integer.valueOf(line);
		for (int i = 0; i < n; i++) {
			Animation animation = new Animation();
			line = br.readLine();
			animation.setName(line);
			line = br.readLine();
			String ds[] = line.split(" ");
			int size = ds.length / 2;
			for (int j = 0; j < size; j++) {
				FrameImage frame = getFrameImage(ds[j * 2]);
				animation.add(frame, Double.valueOf(ds[j * 2 + 1]));
			}

			this.animations.put(animation.getName(), animation);
		}

		br.close();
		fr.close();
	}

	public void LoadPhysMap() {
		File f = new File(physMapFile);
		try (FileInputStream fis = new FileInputStream(f);
				InputStreamReader isr = new InputStreamReader(fis);
				BufferedReader br = new BufferedReader(isr);) {
			/*
			 * Hoặc chỉ đơn giản: FileReader fr=new FileReader(f); BufferedReader br=new
			 * BufferedReader(fr);
			 */
			String line = br.readLine();
			int rows = Integer.valueOf(line);
			line = br.readLine();
			int columns = Integer.valueOf(line);

			phys_map = new int[rows][columns];
			for (int i = 0; i < rows; i++) {
				line = br.readLine();
				String ds[] = line.split(" ");
				for (int j = 0; j < columns; j++)
					phys_map[i][j] = Integer.valueOf(ds[j]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
