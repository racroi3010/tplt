package com.hanaone.tplt;

import java.util.ArrayList;
import java.util.List;

import com.hanaone.tplt.adapter.DatabaseHelper;
import com.hanaone.tplt.adapter.ListExamAdapter;
import com.hanaone.tplt.db.ExamDataSet;
import com.hanaone.tplt.db.LevelDataSet;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class MainActivity extends Activity {
	private Context mContext;
	private DatabaseHelper dbHelper;
	private ListView listExam;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mContext = this;
		dbHelper = new DatabaseHelper(mContext);
		
		listExam = (ListView) findViewById(R.id.list_exam);
		
		init();
	}

	private void init(){
		List<ExamDataSet> list = dbHelper.selectAllExam();
		
		// test
		for(int i = 23; i < 30; i ++){
			ExamDataSet exam = new ExamDataSet();
			exam.setNumber(i);
			exam.setDate("2014-01-23");
			List<LevelDataSet> levels = new ArrayList<LevelDataSet>();
			LevelDataSet level = new LevelDataSet();
			level.setLabel("TOPIK I");
			level.setNumber(1);
			levels.add(level);
			
			level = new LevelDataSet();
			level.setLabel("TOPIK II");
			level.setNumber(2);
			levels.add(level);	
			
			exam.setLevels(levels);
			
			list.add(exam);
		}
		// test
		
		ListExamAdapter adapter = new ListExamAdapter(mContext, null);
		adapter.setExams(list);
		listExam.setAdapter(adapter);
		
	}
	
	
}
