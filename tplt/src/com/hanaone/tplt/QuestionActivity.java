package com.hanaone.tplt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.hanaone.media.AudioControllerView;
import com.hanaone.media.AudioControllerView.MediaPlayerControl;
import com.hanaone.tplt.adapter.DatabaseAdapter;
import com.hanaone.tplt.adapter.ListSectionAdapter;
import com.hanaone.tplt.adapter.QuestionSlideAdapter;
import com.hanaone.tplt.db.FileDataSet;
import com.hanaone.tplt.db.LevelDataSet;
import com.hanaone.tplt.db.SectionDataSet;
import com.hanaone.tplt.util.PreferenceHandler;

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
	private DatabaseAdapter dbAdapter;
	
	// list
	private ListView mList;
	private ListSectionAdapter mListAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_question_practice);
		mContext = this;
		dbAdapter = new DatabaseAdapter(mContext);
		
		// init data		
		onInit();
					
		
		onInitLayout();
		
		
	}
	
	private void onInit(){
		mMode = getIntent().getStringExtra(Constants.QUESTION_MODE);
		mControllerView = new AudioControllerView(this);		
		mPlayer = new MediaPlayer();		
		if(mMode.equals(Constants.QUESTION_MODE_EXAM) || mMode.equals(Constants.QUESTION_MODE_PRACTICE)){
			int levelId = getIntent().getIntExtra(Constants.LEVEL_ID, -1);
			level = dbAdapter.getLevel(levelId);			
			
			String path = level.getAudio().get(0).getPath();
			try {
				mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
				FileInputStream is = new FileInputStream(path);
				mPlayer.setDataSource(is.getFD());
				is.close();
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
		} else if(mMode.equals(Constants.QUESTION_MODE_SAMPLE_BEGINNER)){
			level = dbAdapter.generateSampleTest(1);
			currentItem = 0;
			mHander.obtainMessage(HANDLE_PLAY_LIST).sendToTarget();
			
		} else if(mMode.equals(Constants.QUESTION_MODE_SAMPLE_INTERMEDIATE)){
			level = dbAdapter.generateSampleTest(2);
			currentItem = 0;
			mHander.obtainMessage(HANDLE_PLAY_LIST).sendToTarget();
		}
		
	}
	private void onInitLayout(){
		// pager
		mPager = (ViewPager) findViewById(R.id.viewpager_question_vp);
		mList = (ListView) findViewById(R.id.list_sections);
		if(Constants.QUESTION_MODE_PRACTICE.equals(mMode)){
			mPager.setVisibility(ViewPager.VISIBLE);
			mList.setVisibility(ListView.GONE);
			
			mPagerAdapter = new QuestionSlideAdapter(getSupportFragmentManager(), level, mMode);
			mPager.setAdapter(mPagerAdapter);			
		} else {
			mPager.setVisibility(ViewPager.GONE);
			mList.setVisibility(ListView.VISIBLE);
			
			mListAdapter = new ListSectionAdapter(mContext, null);
			mList.setAdapter(mListAdapter);
			mListAdapter.setmDataSet(level.getSections());
			
		}
	}
	public void onClick(View v){
		currentItem = mPager.getCurrentItem();
		int sectionSize = level.getSections().size();
		switch (v.getId()) {
		case R.id.btn_previous:	
			if(currentItem > 0){
				currentItem --;
				mPager.setCurrentItem(currentItem);
				
				// re calculate
				SectionDataSet section = level.getSections().get(currentItem);
				int start = (int)(section.getStartAudio() * 1000);
				seekTo(start);
				mControllerView.show();	
				if(PreferenceHandler.getAudioPlayPreference(mContext)){
					start();
				}				
			}

			break;
		case R.id.btn_next:
			if(currentItem < sectionSize - 1){
				currentItem ++;
				mPager.setCurrentItem(currentItem);
				
				// re calculate
				SectionDataSet section = level.getSections().get(currentItem);
				int start = (int)(section.getStartAudio() * 1000);
				seekTo(start);				
				mControllerView.show();
				if(PreferenceHandler.getAudioPlayPreference(mContext)){
					start();
				}				
			} else {
				Intent intent = new Intent(mContext, ResultActivity.class);
				intent.putParcelableArrayListExtra(Constants.LIST_SECTIONS, (ArrayList<? extends Parcelable>) level.getSections());
				startActivity(intent);
			}
			break;
		case R.id.btn_home:
			startActivity(new Intent(mContext, MainActivity.class)
						.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
			break;			
		default:
			break;
		}
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		return false;
	}


	@Override
	protected void onPause() {
		if(mPlayer != null) mPlayer.pause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		if(mPlayer != null) mPlayer.start();
		super.onResume();
	}

	public void onPrepared(MediaPlayer mp) {
		mControllerView.setMediaPlayer(this);
		mControllerView.setAnchorView((FrameLayout) findViewById(R.id.layout_audio));		
//		mPlayer.start();
		if(Constants.QUESTION_MODE_PRACTICE.equals(mMode)){
			mControllerView.show();
			if(PreferenceHandler.getAudioPlayPreference(mContext)){
				start();
			}
		} else if(Constants.QUESTION_MODE_EXAM.equals(mMode)) {
			mPlayer.start();
		} else {
			start();
		}
		
	}
	
	public void start() {
		SectionDataSet section = level.getSections().get(currentItem);
		int start = (int)(section.getStartAudio() * 1000);
		seekTo(start);				
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
//		return mPlayer.getDuration();
	}
	public int getCurrentPosition() {
		// recalculate
		SectionDataSet section = level.getSections().get(currentItem);
		int start = (int)(section.getStartAudio() * 1000);
		int current = mPlayer.getCurrentPosition();
		return (current - start);
//		return mPlayer.getCurrentPosition();
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
	private static final int HANDLE_PLAY_AUDIO = 2;
	private static final int HANDLE_PLAY_LIST = 3;
	private Handler mHander = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HANDLE_STOP_AUDIO:
				mPlayer.pause();
				break;
			case HANDLE_PLAY_AUDIO:
				String path = (String) msg.obj;
				mPlayer.reset();
				mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
				FileInputStream is;
				try {
					is = new FileInputStream(path);
					mPlayer.setDataSource(is.getFD());
					is.close();
					mPlayer.prepareAsync();
					mPlayer.setOnPreparedListener(QuestionActivity.this);
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	
				break;
			case HANDLE_PLAY_LIST:
				List<FileDataSet> audios = level.getAudio();	
				if(audios.size() > currentItem){
					path = audios.get(currentItem).getPath();
					obtainMessage(HANDLE_PLAY_AUDIO, path).sendToTarget();					
				}

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
			if(Constants.QUESTION_MODE_SAMPLE_BEGINNER.equals(mMode) || Constants.QUESTION_MODE_SAMPLE_INTERMEDIATE.equals(mMode)){
				currentItem ++;
				mHander.obtainMessage(HANDLE_PLAY_LIST).sendToTarget();				
			} 
			
		}
		
	}
	
}
