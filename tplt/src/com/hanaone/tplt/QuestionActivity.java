package com.hanaone.tplt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.AsyncTask;
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
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import android.widget.DigitalClock;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.hanaone.media.AudioControllerView;
import com.hanaone.media.AudioControllerView.MediaPlayerControl;
import com.hanaone.tplt.adapter.DatabaseAdapter;
import com.hanaone.tplt.adapter.DownloadFileAdapter;
import com.hanaone.tplt.adapter.DownloadInfo;
import com.hanaone.tplt.adapter.DownloadLevelAdapter;
import com.hanaone.tplt.adapter.DownloadListener;
import com.hanaone.tplt.adapter.ListAdapterListener;
import com.hanaone.tplt.adapter.ListSectionAdapter;
import com.hanaone.tplt.adapter.PlayingInfo;
import com.hanaone.tplt.adapter.QuestionSlideAdapter;
import com.hanaone.tplt.db.FileDataSet;
import com.hanaone.tplt.db.LevelDataSet;
import com.hanaone.tplt.db.QuestionDataSet;
import com.hanaone.tplt.db.ResultDataSet;
import com.hanaone.tplt.db.SectionDataSet;
import com.hanaone.tplt.util.Config;
import com.hanaone.tplt.util.PreferenceHandler;
import com.hanaone.tplt.view.DigitalClockView;

public class QuestionActivity extends FragmentActivity implements OnPreparedListener, MediaPlayerControl, DownloadListener{
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
	private PlayingInfo mPlayInfo;
	
	// list result
	//private ArrayList<ResultDataSet> listResult;
	
//	private Timer timer;
	private long time;
	private DigitalClockView clock;
	
	private boolean activityPaused;
	
	private ListAdapterListener mListener = new ListAdapterListener() {
		private boolean isPathSet = false;
		@Override
		public void onSelect(int questionNumber, int sectionNumber) {
			
		}
		
		@Override
		public void onPlayAudioSection(final PlayingInfo playInfo, int sectionNumber) {
			if(mPlayInfo != null){
				mPlayInfo.setPlaying(false);
				if(mPlayInfo.getPlayButton() != null){
					mPlayInfo.getPlayButton().setBackgroundResource(R.drawable.ic_av_volume_down_black);
					mPlayInfo.getPlayButton().setEnabled(true);
					
				}					
			}
			if(Constants.QUESTION_MODE_REVIEW.equals(mMode)){
				mPlayInfo = playInfo;
				mPlayInfo.getPlayButton().setBackgroundResource(R.drawable.ic_av_volume_down_cyan);
				mPlayInfo.getPlayButton().setEnabled(false);
				mPlayInfo.setPlaying(true);
				currentItem = sectionNumber;
//				FileDataSet audio = null;
				if(level.getAudio().size() > 1){
					mHander.obtainMessage(HANDLE_PLAY_LIST).sendToTarget();
				} else {
					if(!isPathSet){
						mPlayer = getMediaPlayer();	
						String path = level.getAudio().get(0).getPathLocal();
						isPathSet = setAudioResouce(path, true);	
					} else {
						seekTo(0);
						start();
					}

				}
//				else {
//					audio = level.getAudio().get(0);
//				}
//				mPlayer = getMediaPlayer();	
//				String path = audio.getPathLocal();
//				setAudioResouce(path, true);

				
			}
		}

		@Override
		public void onPlayAudioQuestion(final PlayingInfo playInfo, int sectionNumber,
				int questionNumber) {
			if(mPlayInfo != null){
				mPlayInfo.setPlaying(false);
				if(mPlayInfo.getPlayButton() != null){
					mPlayInfo.getPlayButton().setBackgroundResource(R.drawable.ic_av_volume_down_black);
					mPlayInfo.getPlayButton().setEnabled(true);
					
				}					
			}
			if(Constants.QUESTION_MODE_REVIEW.equals(mMode)){
				mPlayInfo = playInfo;
				mPlayInfo.getPlayButton().setBackgroundResource(R.drawable.ic_av_volume_down_cyan);
				mPlayInfo.getPlayButton().setEnabled(false);
				mPlayInfo.setPlaying(true);
				currentItem = sectionNumber;
				
				// temporary set start end audio
				SectionDataSet section = level.getSections().get(sectionNumber);
				QuestionDataSet question = section.getQuestions().get(questionNumber);
				section.setStartAudio(question.getStartAudio());
				section.setEndAudio(question.getEndAudio());
				
//				FileDataSet audio = null;
				if(level.getAudio().size() > 1){
					mHander.obtainMessage(HANDLE_PLAY_LIST).sendToTarget();
				} else {
					if(!isPathSet){
						mPlayer = getMediaPlayer();	
						String path = level.getAudio().get(0).getPathLocal();
						isPathSet = setAudioResouce(path, true);	
					} else {
						seekTo(0);
						start();
					}
				}
				
			}			
		}


	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.activity_question_practice);
		
		overridePendingTransition(R.anim.fadein, R.anim.fadeout);
		
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
			String path = level.getAudio().get(0).getPathLocal();
			setAudioResouce(path, true);

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
			// keep reference object			
			questionNumber = getIntent().getIntExtra(Constants.QUESTION_NUMBER, 0);
			currentItem = getIntent().getIntExtra(Constants.SECTION_NUMBER, 0);
			
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
			findViewById(R.id.btn_audio_download).setVisibility(Button.GONE);
			

			title.setText(getResources().getString(R.string.question_review));
			mPager.setVisibility(ViewPager.GONE);
			//mList.setVisibility(ListView.VISIBLE);

			mListAdapter = new ListSectionAdapter(mContext, mListener);
			mList.setAdapter(mListAdapter);
			mListAdapter.setmDataSet(level.getSections());
			//mListAdapter.setResults(listResult);

//			boolean flag = false;
//			for(SectionDataSet section: level.getSections()){
//				index ++;
//				for(QuestionDataSet question: section.getQuestions()){				
//					if(question.getNumber() == questionNumber){
//						flag = true;
//						break;
//					}	
//					index ++;
//				}
//				if(flag){
//					break;
//				}
//					
//
//			}
			
			int index = 0;			
			for(int i = 0; i < currentItem; i ++){
				index += level.getSections().get(i).getQuestions().size() + 1;				
			}		
			index += questionNumber + 1;
				
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
		int sectionSize = level.getSections().size();	
		switch (v.getId()) {
		case R.id.btn_previous:	
			currentItem = mPager.getCurrentItem();
					
			if(currentItem > 0){
				currentItem --;
				mPager.setCurrentItem(currentItem);

				seekTo(0);
				mControllerView.show();	
				if(PreferenceHandler.getAudioPlayPreference(mContext)){
//					seekTo(0);
					start();
				}				
			}

			break;
		case R.id.btn_next:
			currentItem = mPager.getCurrentItem();
		
			if(currentItem < sectionSize - 1){
				currentItem ++;
				mPager.setCurrentItem(currentItem);

				seekTo(0);				
				mControllerView.show();
				if(PreferenceHandler.getAudioPlayPreference(mContext)){
//					seekTo(0);
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
			pause();	
//			if(timer != null){
//				timer.cancel();
//				timer = null;
//			}			
			finish();
			startActivity(new Intent(mContext, MainActivity.class));
			break;
		case R.id.btn_setting:
			Intent intent = new Intent(mContext, HelpActivity.class);
			startActivityForResult(intent, Constants.REQ_UPDATE_LOCALE);			
			break;
		case R.id.btn_audio_download:
			if(level.getAudio().size() > currentItem){
				confirmDownload(level.getAudio().get(currentItem));
			} else {
				confirmDownload(level.getAudio().get(0));
			}
			
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
		activityPaused = true;
		pause();
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
		activityPaused = false;
		if(Constants.QUESTION_MODE_PRACTICE.equals(mMode)){
			if(PreferenceHandler.getAudioPlayPreference(mContext)){
				start();
				mControllerView.updateProgress();
			}
		} else {
			start();
			mControllerView.updateProgress();				
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
			mControllerView.show();
			start();
			
		} else if(Constants.QUESTION_MODE_SAMPLE_BEGINNER.equals(mMode) || Constants.QUESTION_MODE_SAMPLE_INTERMEDIATE.equals(mMode)){
			seekTo(0);
			start();
			
			new PlayListThread().start();
		}
		
		if(activityPaused){
			pause();
			if(clock != null){
				clock.pause();
			}				
		}			
	}
	
	public void start() {
//		SectionDataSet section = level.getSections().get(currentItem);
//		int start = (int)(section.getStartAudio() * 1000);
		if(mPlayer != null){
			mPlayer.start();
		}
		
		mControllerView.updatePausePlay();
		
//		if(Constants.QUESTION_MODE_SAMPLE_BEGINNER.equals(mMode) || Constants.QUESTION_MODE_SAMPLE_INTERMEDIATE.equals(mMode)){
//			timer = new Timer(true);
//			timer.schedule(new Sleeper(), getDuration());			
//		} 		

		
	}
	public void pause() {
		if(mPlayer != null && mPlayer.isPlaying()){
			mPlayer.pause();
		}
		
		mControllerView.updatePausePlay();
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
			if(mPlayInfo != null){
				mPlayInfo.setPlaying(false);
				if(mPlayInfo.getPlayButton() != null){
					mPlayInfo.getPlayButton().setBackgroundResource(R.drawable.ic_av_volume_down_black);
					mPlayInfo.getPlayButton().setEnabled(true);
					
				}					
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
		mControllerView.updateProgress();
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
				pause();
				break;
			case HANDLE_PLAY_AUDIO:
				String path = (String) msg.obj;
				mPlayer.reset();
				setAudioResouce(path, true);
				
				break;
			case HANDLE_PLAY_LIST:
				List<FileDataSet> audios = level.getAudio();	
				if(audios.size() > currentItem){
					path = audios.get(currentItem).getPathLocal();
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

	private class PlayListThread extends Thread{

		@Override
		public void run() {
			int current = 0;
			int duration = getDuration();
			while(true){
				if(!activityPaused){
					current= getCurrentPosition();
				}
				
				if(current >= duration){
					mHander.obtainMessage(HANDLE_STOP_AUDIO).sendToTarget();
					if(Constants.QUESTION_MODE_SAMPLE_BEGINNER.equals(mMode) || Constants.QUESTION_MODE_SAMPLE_INTERMEDIATE.equals(mMode)){
						currentItem ++;
						mHander.obtainMessage(HANDLE_PLAY_LIST).sendToTarget();				
					} 					
					break;
				}	
				
			}
		}
		
	}
	public void confirmDownload(final FileDataSet file){
		Resources resouces = mContext.getResources();
		

		final Dialog dialog = new Dialog(mContext);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.layout_dialog_yes_no);
		dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		dialog.show();
		((TextView)dialog.findViewById(R.id.txt_dialog_content)).setText(resouces.getString(R.string.dialog_ask_download_big_audio));
		
		
		dialog.findViewById(R.id.btn_dialog_ok).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDownloadDialog(file);
				dialog.dismiss();
			}
		});	
		dialog.findViewById(R.id.btn_dialog_no).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});	
		
		
	}		
	public void showDownloadDialog(final FileDataSet file){
		final Dialog dialog = new Dialog(mContext);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.layout_dialog_download_cancel);
		dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		dialog.show();
		dialog.setCancelable(false);
		
		dialog.findViewById(R.id.btn_dialog_ok).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});	
		
		DownloadInfo info = new DownloadInfo();
		info.setPrgBar((ProgressBar)dialog.findViewById(R.id.prg_dialog_download));
		info.setTxtPer((TextView)dialog.findViewById(R.id.txt_dialog_file_progress));
		info.setTxtSize((TextView)dialog.findViewById(R.id.txt_dialog_file_size));
		
		new DownloadFileAdapter(file, info, mContext, new DatabaseAdapter(mContext), dialog, this).execute();
		
	}
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

	@Override
	public void onFinishNotify(boolean flag) {
		if(flag){
			findViewById(R.id.btn_audio_download).setVisibility(Button.GONE);
			if(Constants.QUESTION_MODE_EXAM.equals(mMode) || Constants.QUESTION_MODE_PRACTICE.equals(mMode)){
				String path = level.getAudio().get(0).getPathLocal();
				setAudioResouce(path, true);

			} else if(Constants.QUESTION_MODE_SAMPLE_BEGINNER.equals(mMode)){
				mHander.obtainMessage(HANDLE_PLAY_LIST).sendToTarget();
				
			} else if(Constants.QUESTION_MODE_SAMPLE_INTERMEDIATE.equals(mMode)){
				mHander.obtainMessage(HANDLE_PLAY_LIST).sendToTarget();
			}
		
			// update
			if(Constants.QUESTION_MODE_REVIEW.equals(mMode)){
				FileDataSet currentFile = level.getAudio().get(currentItem);
				for(FileDataSet fileData: level.getAudio()){
					if(fileData.getId() == currentFile.getId() && !fileData.equals(currentFile)){
						fileData.setPathLocal(currentFile.getPathLocal());
					}
				}
			}
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(RESULT_OK == resultCode && Constants.REQ_UPDATE_LOCALE == requestCode){
			// check update locale
			boolean updateLocale = data.getBooleanExtra(Constants.UPDATE_LOCALE, false);
			if(updateLocale){
				setContentView(R.layout.activity_question_practice);
				onInit();
				onInitLayout();
			}			
		}
	}	
	private boolean setAudioResouce(String path, boolean resetView){
		boolean flag = false;
		if(path != null && !path.isEmpty()){
			try {
				
				mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);					
				FileInputStream is = new FileInputStream(path);				
				mPlayer.setDataSource(is.getFD());
				is.close();
				mPlayer.prepareAsync();
				mPlayer.setOnPreparedListener(QuestionActivity.this);
				
				flag = true;
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
		if(resetView){
			if(flag){
				findViewById(R.id.btn_audio_download).setVisibility(Button.GONE);
				findViewById(R.id.layout_audio).setVisibility(FrameLayout.VISIBLE);
			} else {
				findViewById(R.id.btn_audio_download).setVisibility(Button.VISIBLE);
				findViewById(R.id.layout_audio).setVisibility(FrameLayout.GONE);
			}				
		}
	
		return flag;
		
	}
	
}
