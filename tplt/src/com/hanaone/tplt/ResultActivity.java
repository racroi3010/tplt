package com.hanaone.tplt;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.hanaone.tplt.adapter.DatabaseAdapter;
import com.hanaone.tplt.adapter.ListAdapterListener;
import com.hanaone.tplt.adapter.ListResultAdapter;
import com.hanaone.tplt.adapter.PlayingInfo;
import com.hanaone.tplt.db.LevelDataSet;
import com.hanaone.tplt.db.QuestionDataSet;
import com.hanaone.tplt.db.ResultDataSet;
import com.hanaone.tplt.db.SectionDataSet;
import com.hanaone.tplt.util.Config;
import com.hanaone.tplt.util.PreferenceHandler;

public class ResultActivity extends Activity {
	private Context mContext;
	private LevelDataSet level;
	private ArrayList<ResultDataSet> listResult;
	private int score;
	private String mode;
	private int maxScore;
	private int correct;
	private int wrong;
	private int noAnswer;
	private InterstitialAd minInterstitialAd;
	private ListAdapterListener mListener = new ListAdapterListener() {
		
		@Override
		public void onSelect(int questionNumber, int sectionNumber) {
			Intent intent = new Intent(mContext, QuestionActivity.class);
			intent.putExtra(Constants.QUESTION_MODE, Constants.QUESTION_MODE_REVIEW);
			
			intent.putExtra(Constants.LEVEL, level);

			
			//intent.putExtra(Constants.QUESTION_NUMBER, listResult.get(number).getNumber());
			intent.putExtra(Constants.QUESTION_NUMBER, questionNumber);
			intent.putExtra(Constants.SECTION_NUMBER, sectionNumber);
			mContext.startActivity(intent);					
		}

		@Override
		public void onPlayAudioSection(final PlayingInfo playInfo, int sectionNumber) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPlayAudioQuestion(final PlayingInfo playInfo, int sectionNumber,
				int questionNumber) {
			// TODO Auto-generated method stub
			
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		mContext = this;
		
		level = getIntent().getParcelableExtra(Constants.LEVEL);
		score = 0;
		maxScore = 0;
		correct = 0;
		wrong = 0;
		noAnswer = 0;
		if(level != null){
			listResult = new ArrayList<ResultDataSet>();
			if(level.getSections() != null){
				for(SectionDataSet section: level.getSections())
					for(QuestionDataSet question: section.getQuestions()){
						ResultDataSet data = new ResultDataSet();
						data.setNumber(question.getNumber());
						data.setChoice(question.getChoice());
						data.setAnswer(question.getAnswer());
						data.setScore(question.getMark());
						
						listResult.add(data);
						
						if(data.getChoice() == data.getAnswer()){
							score += data.getScore();
							correct ++;
						} else if(data.getChoice() > 0){
							wrong ++;
						} else {
							noAnswer ++;
						}
						maxScore += question.getMark();
					}
			}			
		}

		
		ListView lvResult = (ListView) findViewById(R.id.lv_result);
		ListResultAdapter adapter = new ListResultAdapter(mContext, mListener);
		adapter.setDataSets(listResult);
		
		lvResult.setAdapter(adapter);
		
		lvResult.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				for(int i = 0; i < level.getSections().size(); i ++){
					SectionDataSet section = level.getSections().get(i);
					for(int j = 0; j < section.getQuestions().size(); j ++){
						QuestionDataSet question = section.getQuestions().get(j);
						if(question.getNumber() == listResult.get(arg2).getNumber()){
							mListener.onSelect(j, i);
							break;
						}
					}
				}
				//mListener.onSelect(arg2);
				
			}
			
		});

		
		TextView txtTotal = (TextView) findViewById(R.id.txt_result_total);
		TextView txtRight = (TextView) findViewById(R.id.txt_result_right);
		TextView txtScore = (TextView) findViewById(R.id.txt_result_score);
//		TextView txtGrade = (TextView) findViewById(R.id.txt_result_grade);
		
		
		txtTotal.setText(listResult.size() + "");
		txtRight.setText(correct + "");
		txtScore.setText(score + "/" + maxScore);

	
		mode = PreferenceHandler.getQuestionModePreference(mContext);
		if(Constants.QUESTION_MODE_EXAM.equals(mode)){
			new DatabaseAdapter(mContext).updateLevelScore(level.getId(), (score * 100)/maxScore);	
		}	
		
		// ads
		if(Config.adsSupport){
			minInterstitialAd = new InterstitialAd(this);
			minInterstitialAd.setAdUnitId(getResources().getString(R.string.interstitialAd_id));
			
			minInterstitialAd.setAdListener(new AdListener() {

				@Override
				public void onAdClosed() {
					requestNewInterstitial();
					goHome();
					super.onAdClosed();
				}
				
			});
			requestNewInterstitial();			
		}
		
		new Handler().post(new Runnable() {
			@Override
			public void run() {
				showDialog();
			}
		});

	}
    public void onClick(View v){
    	switch (v.getId()) {
		case R.id.btn_home:
			if(Config.adsSupport && minInterstitialAd.isLoaded()){
				minInterstitialAd.show();
			} else {
				goHome();
			}			
			
			break;

		default:
			break;
		}
    }
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if(Config.adsSupport && minInterstitialAd.isLoaded()){
			minInterstitialAd.show();
		} else {
			goHome();
		}			
		
	}	
	private void requestNewInterstitial(){
		AdRequest adRequest = new AdRequest.Builder().build();
		minInterstitialAd.loadAd(adRequest);	
					
	}    
	private void goHome(){
		Intent intent = new Intent(mContext, MainActivity.class);		
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
		if(Constants.QUESTION_MODE_EXAM.equals(mode)){
			intent.putExtra(Constants.UPDATE_SCORE_LEVEL_ID, level.getId());
			intent.putExtra(Constants.UPDATE_SCORE, score);
			
		}			
		startActivity(intent);		
	}
	private void showDialog(){
		final Dialog dialog = new Dialog(mContext);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.layout_dialog_result_ok);
		dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		dialog.show();
		
		TextView txtTotal = (TextView) dialog.findViewById(R.id.txt_result_total);
		txtTotal.setText(listResult.size() + "");
		TextView txtCorrect = (TextView) dialog.findViewById(R.id.txt_result_correct);
		txtCorrect.setText(correct + "");
		TextView txtWrong = (TextView) dialog.findViewById(R.id.txt_result_wrong);
		txtWrong.setText(wrong + "");
		TextView txtNoAnswer = (TextView) dialog.findViewById(R.id.txt_result_no_answer);
		txtNoAnswer.setText(noAnswer + "");
		TextView txtScore = (TextView) dialog.findViewById(R.id.txt_result_score);
		txtScore.setText(score + "/" + maxScore);
		TextView txtGrade = (TextView) dialog.findViewById(R.id.txt_result_grade);
		if(score < (6 * maxScore/10)){
			txtGrade.setText(mContext.getResources().getString(R.string.result_more_practice));
			txtGrade.setTextColor(getResources().getColor(R.color.RED));
		} else if(score < (9 * maxScore/10)){
			txtGrade.setText(mContext.getResources().getString(R.string.result_well_done));
			txtGrade.setTextColor(getResources().getColor(R.color.BLUE));
		} else {
			txtGrade.setText(mContext.getResources().getString(R.string.result_awesome));
			txtGrade.setTextColor(getResources().getColor(R.color.GREEN));			
		}
		dialog.findViewById(R.id.btn_dialog_ok).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});
	
	}

}
