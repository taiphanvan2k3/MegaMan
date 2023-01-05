package test;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.MalformedURLException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;

import effect.CacheDataLoader;

public class test {
	public static void main(String[] args) {
		CacheDataLoader.getInstance().LoadSound();
		AudioClip audio = CacheDataLoader.getInstance().getSound("bluefireshooting");
		audio.play();
	}
}
