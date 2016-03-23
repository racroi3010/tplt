package com.hanaone.tplt;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.hanaone.tplt.adapter.DatabaseAdapter;
import com.hanaone.tplt.adapter.ListAdapterListener;
import com.hanaone.tplt.adapter.ListResultAdapter;
import com.hanaone.tplt.db.LevelDataSet;
import com.hanaone.tplt.db.QuestionDataSet;
import com.hanaone.tplt.db.ResultDataSet;
import com.hanaone.tplt.db.SectionDataSet;
import com.hanaone.tplt.util.PreferenceHandler;

public class ResultActivity extends Activity {
	private Context mContext;
	private LevelDataSet level;
	private ArrayList<ResultDataSet> listResult;
	private int score;
	private String mode;
	private ListAdapterListener mListener = new ListAdapterListener() {
		
		@Override
		public void onSelect(int number) {
			Intent intent = new Intent(mContext, QuestionActivity.class);
			intent.putExtra(Constants.QUESTION_MODE, Constants.QUESTION_MODE_REVIEW);
			
			intent.putExtra(Constants.LEVEL, level);
			//intent.putParcelableArrayListExtra(Constants.LIST_RESULT, listResult);
			
			int sectionIndex = 0;
			List<SectionDataSet> sections = level.getSections();
			for(int i = 0; i < sections.size(); i ++){
				for(QuestionDataSet question: sections.get(i).getQuestions())
					if(question.getNumber() == listResult.get(number).getNumber()){
						sectionIndex = i;
						break;
					}
			}
				
			
			intent.putExtra(Constants.SECTION_INDEX, sectionIndex);
			mContext.startActivity(intent);					
		}

		@Override
		public void onPlayAudioSection(final Button audioButton, int sectionNumber) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPlayAudioQuestion(Button audioButton, int sectionNumber,
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
		int maxScore = 0;
		int right = 0;
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
							right ++;
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
				mListener.onSelect(arg2);
				
			}
			
		});

		
		TextView txtTotal = (TextView) findViewById(R.id.txt_result_total);
		TextView txtRight = (TextView) findViewById(R.id.txt_result_right);
		TextView txtScore = (TextView) findViewById(R.id.txt_result_score);
		TextView txtGrade = (TextView) findViewById(R.id.txt_result_grade);
		
		
		txtTotal.setText(listResult.size() + "");
		txtRight.setText(right + "");
		txtScore.setText(score + "");
		if(right >= listResult.size()/2){
			txtGrade.setText("PASS");
			txtGrade.setTextColor(getResources().getColor(R.color.GREEN));
		} else {
			txtGrade.setText("FAILED");
			txtGrade.setTextColor(getResources().getColor(R.color.RED));
		}
	
		mode = PreferenceHandler.getQuestionModePreference(mContext);
		if(Constants.QUESTION_MODE_EXAM.equals(mode)){
			new DatabaseAdapter(mContext).updateLevelScore(level.getId(), (score * 100)/maxScore);	
		}	
		
	}
    public void onClick(View v){
    	switch (v.getId()) {
		case R.id.btn_home:
			goHome();
			break;

		default:
			break;
		}
    }
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		goHome();
	}	
    
	private void goHome(){
		Intent intent = new Intent(mContext, MainActivity.class)
		.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);			
		if(Constants.QUESTION_MODE_EXAM.equals(mode)){
			intent.putExtra(Constants.UPDATE_SCORE_LEVEL_ID, level.getId());
			intent.putExtra(Constants.UPDATE_SCORE, score);
		}			
		finish();
		startActivity(intent);		
	}
}
