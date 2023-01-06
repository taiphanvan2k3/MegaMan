package test;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import effect.CacheDataLoader;

public class test {
	public void playSound() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		File f = new File("data/robotRshooting.wav");
		AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(f);
		Clip clip=AudioSystem.getClip();
		clip.open(audioInputStream);
		clip.start();
	}

	public static void main(String[] args) {
		test t = new test();
		try {
			t.playSound();
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
