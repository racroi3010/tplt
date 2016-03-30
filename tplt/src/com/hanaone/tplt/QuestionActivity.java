package com.hanaone.tplt;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import android.widget.DigitalClock;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.hanaone.media.AudioControllerView;
import com.hanaone.media.AudioControllerView.MediaPlayerControl;
import com.hanaone.tplt.adapter.DatabaseAdapter;
import com.hanaone.tplt.adapter.ListAdapterListener;
import com.hanaone.tplt.adapter.ListSectionAdapter;
import com.hanaone.tplt.adapter.QuestionSlideAdapter;
import com.hanaone.tplt.db.FileDataSet;
import com.hanaone.tplt.db.LevelDataSet;
import com.hanaone.tplt.db.QuestionDataSet;
import com.hanaone.tplt.db.ResultDataSet;
import com.hanaone.tplt.db.SectionDataSet;
import com.hanaone.tplt.util.Config;
import com.hanaone.tplt.util.PreferenceHandler;
import com.hanaone.tplt.view.DigitalClockView;

public class QuestionActivity extends FragmentActivity implements OnPreparedListener, MediaPlayerControl{
	private AudioControllerView mControllerView;
	private Context mContext;
	private static MediaPlayer mPlayer;
//	private Timer timer;
	// view pager
	private ViewPager mPager;
	private PagerAdapter mPagerAdapter;
	private LevelDataSet level;
	private String mMode;
	private int currentItem;
	private DatabaseAdapter dbAdapter;
	//private int sectionIndex;
	private int questionNumber;
	
	// list
	private ListView mList;
	private ListSectionAdapter mListAdapter;
	
	// audio
	private Button replayButton;
	
	// list result
	//private ArrayList<ResultDataSet> listResult;
	
//	private Timer timer;
	private long time;
	private DigitalClockView clock;
	
	private ListAdapterListener mListener = new ListAdapterListener() {
		
		@Override
		public void onSelect(int number) {
			
		}
		
		@Override
		public void onPlayAudioSection(final Button audioButton, int sectionNumber) {
			if(replayButton != null){
				replayButton.setEnabled(true);
				replayButton.setBackgroundResource(R.drawable.ic_av_volume_down_black);
			}
			if(Constants.QUESTION_MODE_REVIEW.equals(mMode)){
				replayButton = audioButton;
				replayButton.setBackgroundResource(R.drawable.ic_av_volume_down_cyan);
				replayButton.setEnabled(false);
				currentItem = sectionNumber;
				FileDataSet audio = null;
				if(level.getAudio().size() > sectionNumber){
					audio = level.getAudio().get(sectionNumber);
				} else {
					audio = level.getAudio().get(0);
				}
				mPlayer = getMediaPlayer();	
				String path = audio.getPath();
				try {
					
					mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);					
					FileInputStream is = new FileInputStream(path);				
					mPlayer.setDataSource(is.getFD());
					is.close();
					mPlayer.prepareAsync();
					mPlayer.setOnPreparedListener(QuestionActivity.this);
					
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
		}

		@Override
		public void onPlayAudioQuestion(final Button audioButton, int sectionNumber,
				int questionNumber) {
			if(replayButton != null){
				replayButton.setEnabled(true);
				replayButton.setBackgroundResource(R.drawable.ic_av_volume_down_black);
			}
			if(Constants.QUESTION_MODE_REVIEW.equals(mMode)){
				replayButton = audioButton;
				replayButton.setBackgroundResource(R.drawable.ic_av_volume_down_cyan);
				replayButton.setEnabled(false);
				currentItem = sectionNumber;
				
				// temporary set start end audio
				SectionDataSet section = level.getSections().get(sectionNumber);
				QuestionDataSet question = section.getQuestions().get(questionNumber);
				section.setStartAudio(question.getStartAudio());
				section.setEndAudio(question.getEndAudio());
				
				FileDataSet audio = null;
				if(level.getAudio().size() > sectionNumber){
					audio = level.getAudio().get(sectionNumber);
				} else {
					audio = level.getAudio().get(0);
				}
				mPlayer = getMediaPlayer();	
				String path = audio.getPath();
				try {
					
					mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);					
					FileInputStream is = new FileInputStream(path);				
					mPlayer.setDataSource(is.getFD());
					is.close();
					mPlayer.prepareAsync();
					mPlayer.setOnPreparedListener(QuestionActivity.this);
					
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
		}

	};
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
		PreferenceHandler.setQuestionModePreference(mContext, mMode);
		mControllerView = new AudioControllerView(this);		
		mPlayer = getMediaPlayer();		
		if(Constants.QUESTION_MODE_EXAM.equals(mMode) || Constants.QUESTION_MODE_PRACTICE.equals(mMode)){
			int levelId = getIntent().getIntExtra(Constants.LEVEL_ID, -1);
			level = dbAdapter.getLevel(levelId);			

			String path = level.getAudio().get(0).getPath();
		
			try {
			
				mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
				if(Config.LOGGING){
					Log.w("mystring", "before " + level.getAudio().get(0).toString());
				}						
				FileInputStream is = new FileInputStream(path);
				if(Config.LOGGING){
					Log.w("mystring", "after");
				}					
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
		} else if(Constants.QUESTION_MODE_SAMPLE_BEGINNER.equals(mMode)){
			level = dbAdapter.generateSampleTest(1);
			currentItem = 0;
			mHander.obtainMessage(HANDLE_PLAY_LIST).sendToTarget();
			
		} else if(Constants.QUESTION_MODE_SAMPLE_INTERMEDIATE.equals(mMode)){
			level = dbAdapter.generateSampleTest(2);
			currentItem = 0;
			mHander.obtainMessage(HANDLE_PLAY_LIST).sendToTarget();
		} else if(Constants.QUESTION_MODE_REVIEW.equals(mMode)){
			level = getIntent().getParcelableExtra(Constants.LEVEL);
			//listResult = getIntent().getParcelableArrayListExtra(Constants.LIST_RESULT);
			questionNumber = getIntent().getIntExtra(Constants.QUESTION_NUMBER, 0);
		}
		
	}
	private void onInitLayout(){
		// pager
		mPager = (ViewPager) findViewById(R.id.viewpager_question_vp);
		mList = (ListView) findViewById(R.id.list_sections);
		// ads
		
		TextView title = (TextView) findViewById(R.id.txt_question_practice_title);
		if(Config.adsSupport){	
			AdView mAdView = (AdView) findViewById(R.id.adView);
		    AdRequest adRequest = new AdRequest.Builder().build();
		    mAdView.loadAd(adRequest);	
		    
		    findViewById(R.id.layout_adView).setVisibility(LinearLayout.VISIBLE);
		    title.setVisibility(TextView.GONE);
		} else {
			findViewById(R.id.layout_adView).setVisibility(LinearLayout.GONE);
			title.setVisibility(TextView.VISIBLE);
		}
	
	    
	    
		if(Constants.QUESTION_MODE_PRACTICE.equals(mMode)){
			findViewById(R.id.layout_previous).setVisibility(LinearLayout.VISIBLE);
			findViewById(R.id.layout_next).setVisibility(LinearLayout.VISIBLE);
			findViewById(R.id.layout_home_setting).setVisibility(RelativeLayout.VISIBLE);
			findViewById(R.id.btn_submit).setVisibility(Button.GONE);	
			findViewById(R.id.layout_list_sections).setVisibility(RelativeLayout.GONE);	
			findViewById(R.id.layout_adView).setVisibility(LinearLayout.GONE);
			title.setVisibility(TextView.GONE);
			mPager.setVisibility(ViewPager.VISIBLE);
			//mList.setVisibility(ListView.GONE);
			
			mPagerAdapter = new QuestionSlideAdapter(getSupportFragmentManager(), level, mMode);
			mPager.setAdapter(mPagerAdapter);	
			mPager.setOnPageChangeListener(new OnPageChangeListener() {
				
				@Override
				public void onPageSelected(int position) {
					currentItem = position;
					if(currentItem >= level.getSections().size() - 1) {
						Intent intent = new Intent(mContext, ResultActivity.class);
						intent.putExtra(Constants.LEVEL, level);
						startActivity(intent);
					} else {
						seekTo(0);
						mControllerView.show();	
						if(PreferenceHandler.getAudioPlayPreference(mContext)){
							seekTo(0);
							start();
						}	
												
					}

					
				}
				
				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onPageScrollStateChanged(int arg0) {
					// TODO Auto-generated method stub
					
				}
			});

		} else if(Constants.QUESTION_MODE_REVIEW.equals(mMode)){
			findViewById(R.id.layout_previous).setVisibility(LinearLayout.GONE);
			findViewById(R.id.layout_next).setVisibility(LinearLayout.GONE);
			findViewById(R.id.btn_submit).setVisibility(Button.GONE);	
			findViewById(R.id.layout_home_setting).setVisibility(RelativeLayout.GONE);
			findViewById(R.id.btn_result).setVisibility(Button.VISIBLE);	
			findViewById(R.id.layout_list_sections).setVisibility(RelativeLayout.VISIBLE);
			

			title.setText(getResources().getString(R.string.question_review));
			mPager.setVisibility(ViewPager.GONE);
			//mList.setVisibility(ListView.VISIBLE);

			mListAdapter = new ListSectionAdapter(mContext, mListener);
			mList.setAdapter(mListAdapter);
			mListAdapter.setmDataSet(level.getSections());
			//mListAdapter.setResults(listResult);
			int index = 0;
			boolean flag = false;
			for(SectionDataSet section: level.getSections()){
				index ++;
				for(QuestionDataSet question: section.getQuestions()){				
					if(question.getNumber() == questionNumber){
						flag = true;
						break;
					}	
					index ++;
				}
				if(flag){
					break;
				}
					

			}
				
			mList.setSelection(index);
			mList.requestFocus();

		}else {
			findViewById(R.id.layout_previous).setVisibility(LinearLayout.GONE);
			findViewById(R.id.layout_next).setVisibility(LinearLayout.GONE);
			findViewById(R.id.layout_home_setting).setVisibility(RelativeLayout.GONE);
			findViewById(R.id.btn_submit).setVisibility(Button.VISIBLE);	
			findViewById(R.id.layout_list_sections).setVisibility(RelativeLayout.VISIBLE);	

			title.setText(getResources().getString(R.string.question_test));						
			mPager.setVisibility(ViewPager.GONE);
			//mList.setVisibility(ListView.VISIBLE);
			
			mListAdapter = new ListSectionAdapter(mContext, mListener);
			mList.setAdapter(mListAdapter);
			mListAdapter.setmDataSet(level.getSections());
			
			clock = (DigitalClockView) findViewById(R.id.clock);
			Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/digital-7.ttf");
			clock.setTypeface(tf);
			//RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			//clock.setLayoutParams(layoutParams);
			
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

				seekTo(0);
				mControllerView.show();	
				if(PreferenceHandler.getAudioPlayPreference(mContext)){
					seekTo(0);
					start();
				}				
			}

			break;
		case R.id.btn_next:
			if(currentItem < sectionSize - 1){
				currentItem ++;
				mPager.setCurrentItem(currentItem);

				seekTo(0);				
				mControllerView.show();
				if(PreferenceHandler.getAudioPlayPreference(mContext)){
					seekTo(0);
					start();
				}				
			} else {
				Intent intent = new Intent(mContext, ResultActivity.class);
				intent.putExtra(Constants.LEVEL, level);
				startActivity(intent);
			}
			break;
		case R.id.btn_result:
		case R.id.btn_submit:
			submit();
			break;
		case R.id.btn_home:
			if(mPlayer != null){
				mPlayer.pause();
			}	
//			if(timer != null){
//				timer.cancel();
//				timer = null;
//			}			
			finish();
			startActivity(new Intent(mContext, MainActivity.class));
			break;
		case R.id.btn_setting:
			Intent intent = new Intent(mContext, HelpActivity.class);
			startActivity(intent);			
			break;
		default:
			break;
		}
	}
	public void submit(){
		if(clock != null){
			clock.cancel();
		}
		Intent intent = new Intent(mContext, ResultActivity.class);
		intent.putExtra(Constants.LEVEL, level);
		startActivity(intent);			
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		return false;
	}


	@Override
	protected void onPause() {
		if(mPlayer != null){
			pause();
			mControllerView.updatePausePlay();		
			
		}
//		if(timer != null){
//			timer.cancel();
//			timer = null;
//		}	
		if(clock != null){
			clock.pause();
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		if(mPlayer != null){
			if(Constants.QUESTION_MODE_PRACTICE.equals(mMode)){
				if(PreferenceHandler.getAudioPlayPreference(mContext)){
					start();
					mControllerView.updateProgress();
				}
			} else {
				start();
				mControllerView.updateProgress();				
			}

		}
		if(clock != null){
			clock.resume();
		}		
		super.onResume();
	}

	public void onPrepared(MediaPlayer mp) {
		mControllerView.setMediaPlayer(this);
		mControllerView.setAnchorView((FrameLayout) findViewById(R.id.layout_audio));		
//		mPlayer.start();
		
		if(Constants.QUESTION_MODE_PRACTICE.equals(mMode)){
			seekTo(0);
			mControllerView.show();
			if(PreferenceHandler.getAudioPlayPreference(mContext)){
				seekTo(0);
				start();
			}
		} else if(Constants.QUESTION_MODE_EXAM.equals(mMode)) {
			mPlayer.start();
			
			
			time = mPlayer.getDuration();
			clock.start(time, this);
//			timer = new Timer();
//			timer.schedule(new ClockTimer(), 0, 1000);	
		} else if(Constants.QUESTION_MODE_REVIEW.equals(mMode)) {
//			if(timer != null){
//				timer.cancel();
//				timer = null;
//			}	
			seekTo(0);
			start();
			mControllerView.show();
		} else {
			start();
//			time = mPlayer.getDuration();
//			clock.start(time);
		}
		
	}
	
	public void start() {
//		SectionDataSet section = level.getSections().get(currentItem);
//		int start = (int)(section.getStartAudio() * 1000);
						
		mPlayer.start();
		mControllerView.updatePausePlay();
		
//		if(Constants.QUESTION_MODE_SAMPLE_BEGINNER.equals(mMode) || Constants.QUESTION_MODE_SAMPLE_INTERMEDIATE.equals(mMode)){
//			timer = new Timer(true);
//			timer.schedule(new Sleeper(), getDuration());			
//		} 		

		
	}
	public void pause() {
		mControllerView.updatePausePlay();
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
		int duration = current - start;
		if(duration >= getDuration()){
			pause();
			if(replayButton != null){
				replayButton.setBackgroundResource(R.drawable.ic_av_volume_down_black);
				replayButton.setEnabled(true);
				replayButton = null;
			}
		}
		return duration;
//		return mPlayer.getCurrentPosition();
	}
	public void seekTo(int pos) {
		SectionDataSet section = level.getSections().get(currentItem);
		int start = (int)(section.getStartAudio() * 1000);	
		if(pos >= 0 && pos <= getDuration()){
			mPlayer.seekTo(pos + start);
		}
		
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
//	private static final int HANDLE_UPDATE_CLOCK = 4;
	private Handler mHander = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HANDLE_STOP_AUDIO:
				if(mPlayer != null && mPlayer.isPlaying()) mPlayer.pause();
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
//			case HANDLE_UPDATE_CLOCK:
//				time = time - 1000;
//				clock.setText(convertTime(time) +"");
//				break;
			default:
				break;
			}
		}
		
	};

//	private class Sleeper extends TimerTask{
//
//		@Override
//		public void run() {
//			mHander.obtainMessage(HANDLE_STOP_AUDIO).sendToTarget();
//			if(Constants.QUESTION_MODE_SAMPLE_BEGINNER.equals(mMode) || Constants.QUESTION_MODE_SAMPLE_INTERMEDIATE.equals(mMode)){
//				currentItem ++;
//				mHander.obtainMessage(HANDLE_PLAY_LIST).sendToTarget();				
//			} 
//			
//		}
//		
//	}
	@Override
	public void onBackPressed() {
		if(clock != null){
			clock.cancel();
		}		
		if(mPlayer != null){
			mPlayer.pause();
		}
//		if(timer != null){
//			timer.cancel();
//			timer = null;
//		}
		super.onBackPressed();
	}
	private static MediaPlayer getMediaPlayer(){
		if(mPlayer == null){
			mPlayer = new MediaPlayer();
		} else {
			mPlayer.reset();
		}
		
		return mPlayer;		
	}
}
