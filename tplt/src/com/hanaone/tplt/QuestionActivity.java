package com.hanaone.tplt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.hanaone.media.AudioControllerView;
import com.hanaone.media.AudioControllerView.MediaPlayerControl;
import com.hanaone.tplt.adapter.DatabaseAdapter;
import com.hanaone.tplt.adapter.QuestionSlideAdapter;
import com.hanaone.tplt.db.LevelDataSet;
import com.hanaone.tplt.db.SectionDataSet;

public class QuestionActivity extends FragmentActivity implements OnPreparedListener, MediaPlayerControl{
	private AudioControllerView mControllerView;
	private Context mContext;
	private MediaPlayer mPlayer;
	
	// view pager
	private ViewPager mPager;
	private PagerAdapter mPagerAdapter;
	private LevelDataSet level;
	private String mMode;
	private int currentItem;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_question_practice);
		mContext = this;
		DatabaseAdapter dbAdapter = new DatabaseAdapter(mContext);
		
		// init data		
		int levelId = getIntent().getIntExtra(Constants.LEVEL_ID, -1);
		level = dbAdapter.getLevel(levelId);
		
		mMode = getIntent().getStringExtra(Constants.QUESTION_MODE);
			
		mControllerView = new AudioControllerView(this);		
		mPlayer = new MediaPlayer();
		String path = level.getAudio().getPath();
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
		
		mPagerAdapter = new QuestionSlideAdapter(getSupportFragmentManager(), level, mMode);
		mPager.setAdapter(mPagerAdapter);
		
		
	}
	
	public void onClick(View v){
		currentItem = mPager.getCurrentItem();
		int sectionSize = level.getSections().size();
		switch (v.getId()) {
		case R.id.btn_previous:
			currentItem --;
			mPager.setCurrentItem(currentItem);
			
			// re calculate
			SectionDataSet section = level.getSections().get(currentItem);
			int start = (int)(section.getStartAudio() * 1000);
			seekTo(start);
			mControllerView.show();
			break;
		case R.id.btn_next:
			if(currentItem < sectionSize - 1){
				currentItem ++;
				mPager.setCurrentItem(currentItem);
				
				// re calculate
				section = level.getSections().get(currentItem);
				start = (int)(section.getStartAudio() * 1000);
				seekTo(start);				
				mControllerView.show();
			} else {
				Intent intent = new Intent(mContext, ResultActivity.class);
				intent.putParcelableArrayListExtra(Constants.LIST_SECTIONS, (ArrayList<? extends Parcelable>) level.getSections());
				startActivity(intent);
			}
			break;
		default:
			break;
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
		if(Constants.QUESTION_MODE_PRACTICE.equals(mMode)){
			mControllerView.show();
		}
		
	}
	
	public void start() {
//		SectionDataSet section = level.getSections().get(currentItem);
//		int start = (int)(section.getStartAudio() * 1000);
//		int end = (int)(section.getEndAudio() * 1000);
//		mPlayer.seekTo(start);
		mPlayer.start();
		
		Timer timer = new Timer(true);
		timer.schedule(new Sleeper(), getDuration());
		
	}
	public void pause() {
		mPlayer.pause();
	}
	public int getDuration() {
		// re calculate
		SectionDataSet section = level.getSections().get(currentItem);
		int start = (int)(section.getStartAudio() * 1000);
		int end = (int)(section.getEndAudio() * 1000);
		return (end - start);
		//return mPlayer.getDuration();
	}
	public int getCurrentPosition() {
		// recalculate
		SectionDataSet section = level.getSections().get(currentItem);
		int start = (int)(section.getStartAudio() * 1000);
		int current = mPlayer.getCurrentPosition();
		return (current - start);
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

	private static final int HANDLE_STOP_AUDIO = 1;
	private Handler mHander = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HANDLE_STOP_AUDIO:
				mPlayer.pause();
				break;

			default:
				break;
			}
		}
		
	};
	private class Sleeper extends TimerTask{

		@Override
		public void run() {
			mHander.obtainMessage(HANDLE_STOP_AUDIO).sendToTarget();
		}
		
	}
}
