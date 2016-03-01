package com.hanaone.tplt;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.hanaone.media.AudioControllerView;
import com.hanaone.media.AudioControllerView.MediaPlayerControl;
import com.hanaone.tplt.adapter.DatabaseAdapter;
import com.hanaone.tplt.adapter.QuestionSlideAdapter;
import com.hanaone.tplt.db.LevelDataSet;

public class QuestionActivity extends FragmentActivity implements OnPreparedListener, MediaPlayerControl{
	private AudioControllerView mControllerView;
	private Context mContext;
	private MediaPlayer mPlayer;
	
	// view pager
	private ViewPager mPager;
	private PagerAdapter mPagerAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_question_practice);
		mContext = this;
		
		// init data
		DatabaseAdapter dbAdapter = new DatabaseAdapter(mContext);
		int levelId = getIntent().getIntExtra(Constants.LEVEL_ID, -1);
		LevelDataSet level = dbAdapter.getLevel(levelId);
		
		
		
		mControllerView = new AudioControllerView(this);		
		mPlayer = new MediaPlayer();
		
		String path = Environment.getExternalStorageDirectory().getPath() +"/35.mp3";
		try {
			mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
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

		
		// pager
		mPager = (ViewPager) findViewById(R.id.viewpager_question_vp);
		
		mPagerAdapter = new QuestionSlideAdapter(getSupportFragmentManager(), level);
		mPager.setAdapter(mPagerAdapter);
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
