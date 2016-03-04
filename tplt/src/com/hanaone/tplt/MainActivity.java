package com.hanaone.tplt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.hanaone.http.DownloadHelper;
import com.hanaone.http.JsonReaderHelper;
import com.hanaone.tplt.adapter.DatabaseAdapter;
import com.hanaone.tplt.adapter.ListExamAdapter;
import com.hanaone.tplt.adapter.ListLevelListener;
import com.hanaone.tplt.db.ExamDataSet;
import com.hanaone.tplt.db.FileDataSet;
import com.hanaone.tplt.db.LevelDataSet;
import com.hanaone.tplt.db.SectionDataSet;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
	private Context mContext;
	private DatabaseAdapter dbAdapter;
	private ListView listExam;
	private List<ExamDataSet> list;
	private ListExamAdapter adapter;
	
	private ImageView imgSync;
	private LinearLayout layoutSync;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mContext = this;
		dbAdapter = new DatabaseAdapter(mContext);
		
		listExam = (ListView) findViewById(R.id.list_exam);
		
		imgSync = (ImageView) findViewById(R.id.img_sync);
		layoutSync = (LinearLayout) findViewById(R.id.layout_sync);	
		init();
	}
    @Override
	protected void onResume() {
		super.onResume();
		new LoadingNewData().execute();
	}
 
	private void init(){		
		list = dbAdapter.getAllExam();
		adapter = new ListExamAdapter(mContext, mListener);
		adapter.setExams(list);
		listExam.setAdapter(adapter);
		
		new LoadingNewData().execute();
		
	}
	private List<ExamDataSet> onReadConf(){
		DownloadHelper dHelper = new DownloadHelper(mContext);
		String confPath = Constants.getRootPath(mContext) + "/config.txt";
		boolean loaded = false;
		try {		
			loaded = dHelper.downloadFile(Constants.REMOTE_CONFIG_FILE_JSON, confPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(loaded){
			File file = new File(confPath);
			if(file.exists()){
				try {
					List<ExamDataSet> exams = JsonReaderHelper.readExams(file);
					for(ExamDataSet exam: exams){
						if(!dbAdapter.checkExam(exam.getNumber())){
							dbAdapter.addExam(exam);
							
							list.add(exam);	
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return list;
	}
	private class LoadingNewData extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			onReadConf();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			adapter.notifyDataSetChanged();
			imgSync.getAnimation().cancel();
			layoutSync.setVisibility(LinearLayout.GONE);
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			layoutSync.setVisibility(LinearLayout.VISIBLE);
			Animation rotation = AnimationUtils.loadAnimation(mContext, R.anim.clock_wise);
			imgSync.startAnimation(rotation);
			super.onPreExecute();
		}
		
	}
	
	private ListLevelListener mListener = new ListLevelListener() {
		
		@Override
		public void onSelect(int examLevelId, String examLevelName) {
			Toast.makeText(mContext, "" + examLevelId, Toast.LENGTH_SHORT).show();
			
			Intent intent = new Intent(mContext, SelectionActivity.class);
			intent.putExtra(Constants.LEVEL_ID, examLevelId);
			intent.putExtra(Constants.LEVEL_NAME, examLevelName);
			startActivity(intent);
		}
	};

	
}
