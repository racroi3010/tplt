package com.hanaone.tplt.adapter;

import android.widget.Button;

public class PlayingInfo {
	private boolean isPlaying;
	private Button playButton;
	public boolean isPlaying() {
		return isPlaying;
	}
	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}
	public Button getPlayButton() {
		return playButton;
	}
	public void setPlayButton(Button playButton) {
		this.playButton = playButton;
	}
	
}
