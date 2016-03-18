package com.hanaone.tplt;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.hanaone.tplt.adapter.ListAdapterListener;
import com.hanaone.tplt.adapter.ListResultAdapter;
import com.hanaone.tplt.db.LevelDataSet;
import com.hanaone.tplt.db.QuestionDataSet;
import com.hanaone.tplt.db.ResultDataSet;
import com.hanaone.tplt.db.SectionDataSet;

public class ResultActivity extends Activity {
	private Context mContext;
	private LevelDataSet level;
	private ArrayList<ResultDataSet> listResult;
	private ListAdapterListener mListener = new ListAdapterListener() {
		
		@Override
		public void onSelect(int number) {
			Intent intent = new Intent(mContext, QuestionActivity.class);
			intent.putExtra(Constants.QUESTION_MODE, Constants.QUESTION_MODE_REVIEW);
			
			intent.putExtra(Constants.LEVEL, level);
			intent.putParcelableArrayListExtra(Constants.LIST_RESULT, listResult);
			
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
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		mContext = this;
		
		level = getIntent().getParcelableExtra(Constants.LEVEL);
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
					}
			}			
		}

		
		ListView lvResult = (ListView) findViewById(R.id.lv_result);
		ListResultAdapter adapter = new ListResultAdapter(mContext, mListener);
		adapter.setDataSets(listResult);
		
		lvResult.setAdapter(adapter);

		
		
	}
    public void onClick(View v){
    	switch (v.getId()) {
		case R.id.btn_home:
			finish();
			startActivity(new Intent(mContext, MainActivity.class)
				.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
			break;

		default:
			break;
		}
    }		
}
