package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import effect.CacheDataLoader;

public class test {
	int[][] phys_map;
	String physMapFile = "data/phys_map.txt";

	public static void main(String[] args) {
		test t = new test();
		try {
			t.testFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void testFile() throws IOException {
		File f = new File(physMapFile);
		FileInputStream fis = new FileInputStream(f);
		InputStreamReader isr = new InputStreamReader(fis);
		BufferedReader br = new BufferedReader(isr);
		String line = br.readLine();
		int rows = Integer.valueOf(line);
		line = br.readLine();
		int columns = Integer.valueOf(line);

		phys_map = new int[rows][columns];
		for (int i = 0; i < rows; i++) {
			line = br.readLine();
			String ds[] = line.split(" ");
			System.out.println(Arrays.toString(ds));
		}
	}
}
