package com.hanaone.tplt;

import java.io.IOException;

import com.hanaone.media.AudioControllerView;
import com.hanaone.media.AudioControllerView.MediaPlayerControl;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class QuestionActivity extends Activity implements OnPreparedListener, MediaPlayerControl{
	private AudioControllerView mControllerView;
	private Context mContext;
	private MediaPlayer mPlayer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_question);
		mContext = this;
		
		mControllerView = new AudioControllerView(this);		
		mPlayer = new MediaPlayer();
		
		String path = Environment.getExternalStorageDirectory().getPath() +"/35.mp3";
		try {
			//mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mPlayer.setDataSource(path);
			mPlayer.prepareAsync();
			mPlayer.setOnPreparedListener(this);
			//mControllerView.show(0);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		return false;
	}


	public void onPrepared(MediaPlayer mp) {
		mControllerView.setMediaPlayer(this);
		mControllerView.setAnchorView((FrameLayout) findViewById(R.id.layout_audio));		
		//mPlayer.start();
		mControllerView.show(0);
	}
	
	public void start() {
		mPlayer.start();
		
	}
	public void pause() {
		mPlayer.pause();
	}
	public int getDuration() {
		return mPlayer.getDuration();
	}
	public int getCurrentPosition() {
		return mPlayer.getCurrentPosition();
	}
	public void seekTo(int pos) {
		mPlayer.seekTo(pos);
	}
	public boolean isPlaying() {
		return mPlayer.isPlaying();
	}
	public int getBufferPercentage() {
		return 0;
	}
	public boolean canPause() {
		return true;
	}
	public boolean canSeekBackward() {
		return true;
	}
	public boolean canSeekForward() {
		return true;
	}
	public boolean isFullScreen() {
		return true;
	}
	public void toggleFullScreen() {

		
	}


}
