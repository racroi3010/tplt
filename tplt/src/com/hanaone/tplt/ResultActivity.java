package com.hanaone.tplt;

import java.util.ArrayList;
import java.util.List;

import com.hanaone.tplt.adapter.ListResultAdapter;
import com.hanaone.tplt.db.QuestionDataSet;
import com.hanaone.tplt.db.ResultDataSet;
import com.hanaone.tplt.db.SectionDataSet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class ResultActivity extends Activity {
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		mContext = this;
		
		List<SectionDataSet> sections = getIntent().getParcelableArrayListExtra(Constants.LIST_SECTIONS);
		
		List<ResultDataSet> listResult = new ArrayList<ResultDataSet>();
		if(sections != null){
			for(SectionDataSet section: sections)
				for(QuestionDataSet question: section.getQuestions()){
					ResultDataSet data = new ResultDataSet();
					data.setNumber(question.getNumber());
					data.setChoice(question.getChoice());
					data.setAnswer(question.getAnswer());
					data.setScore(question.getMark());
					
					listResult.add(data);
				}
		}
		
		ListView lvResult = (ListView) findViewById(R.id.lv_result);
		ListResultAdapter adapter = new ListResultAdapter(mContext, null);
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
