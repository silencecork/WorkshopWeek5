package com.android.demo.game;

import android.content.Context;
import android.media.MediaPlayer;

public class MusicPlayer {
	
	private Context context;
	private MediaPlayer mp;
	
	public MusicPlayer(Context c, int resId) {
		context = c;
		mp = MediaPlayer.create(context, resId);
		mp.setLooping(true);
	}
	
	public void start() {
		if (!mp.isPlaying()) {
			mp.start();
		}
	}
	
	public void pause() {
		if (mp.isPlaying()) {
			mp.pause();
		}
	}
	
	public void resume() {
		if (!mp.isPlaying()) {
			mp.start();
		}
	}
	
	public void stop() {
		mp.stop();
		mp.release();	
	}
	
}
