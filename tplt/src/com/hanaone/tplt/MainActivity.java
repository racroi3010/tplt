package com.hanaone.tplt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.hanaone.http.DownloadHelper;
import com.hanaone.http.JsonReaderHelper;
import com.hanaone.jni.JNIHanaone;
import com.hanaone.tplt.adapter.DatabaseAdapter;
import com.hanaone.tplt.adapter.DownloadInfo;
import com.hanaone.tplt.adapter.ListExamAdapter;
import com.hanaone.tplt.adapter.ListLevelListener;
import com.hanaone.tplt.db.ExamDataSet;
import com.hanaone.tplt.db.FileDataSet;
import com.hanaone.tplt.db.LevelDataSet;
import com.hanaone.tplt.db.SectionDataSet;
import com.hanaone.tplt.util.ColorUtils;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
	private Context mContext;
	private DatabaseAdapter dbAdapter;
	private ListView listExam;
	private List<ExamDataSet> list;
	private List<DownloadInfo> infos;
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
		
		adapter = new ListExamAdapter(mContext, mListener);	
		listExam.setAdapter(adapter);
		
		init();
	}
    @Override
	protected void onResume() {
		super.onResume();
		Intent intent = getIntent();
		if(intent != null){
			int levelId = intent.getIntExtra(Constants.UPDATE_SCORE_LEVEL_ID, -1);
			if(levelId > -1){
				int score = intent.getIntExtra(Constants.UPDATE_SCORE, -1);
				if(score > -1){
					for(ExamDataSet exam: list)
						for(LevelDataSet level: exam.getLevels())
							if(level.getId() == levelId){
								level.setScore(score);
								
								break;
							}
					adapter.notifyDataSetChanged();			
				}

			}			
		}

		new LoadingNewData().execute();
	}
 
    @Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
	}
	public void onClick(View v){
    	switch (v.getId()) {
		case R.id.btn_setting:
			Intent intent = new Intent(mContext, HelpActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case R.id.btn_sample_test:
			intent = new Intent(mContext, SelectionActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra(Constants.SELECTION_MODE, Constants.SELECTION_MODE_SAMPLE);

			startActivity(intent);
			overridePendingTransition(R.anim.fadein, R.anim.fadeout);
			break;
		default:
			break;
		}
    }
	private void init(){		
		list = dbAdapter.getAllExam();		
		infos = new ArrayList<DownloadInfo>();
		for(int i = 0; i < list.size(); i ++){
			infos.add(new DownloadInfo());
		}
		adapter.setDownloadInfos(infos);
		adapter.setExams(list);		
		new LoadingNewData().execute();
		
	}
	private class LoadingNewData extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			DownloadHelper dHelper = new DownloadHelper(mContext);
			String confPath = Constants.getInternalRootPath(mContext) + "/config.txt";
			//confPath = Environment.getExternalStorageDirectory().getPath() + "/config.txt";
			
			String url = Constants.REMOTE_CONFIG_FILE_JSON;
			
			// test
			url = new JNIHanaone().stringFromJNI();
			boolean loaded = false;
			try {						
				loaded = dHelper.downloadFile(url, confPath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(loaded){
				File file = new File(confPath);
				if(file.exists()){
					try {
						List<ExamDataSet> exams = JsonReaderHelper.readExams(file);
						int colorOld = 0;
						int colorNew = 0;
						for(ExamDataSet exam: exams){
							if(!dbAdapter.checkExam(exam.getNumber())){
								
								do{
									colorOld = colorNew;
									colorNew = ColorUtils.randomColor(50, 250, 20, 0.9f, 0.5f);
								} while(colorNew == colorOld);
								exam.setColor(colorNew);
								dbAdapter.addExam(exam);
								
								list.add(exam);	
								infos.add(new DownloadInfo());
								publishProgress();
							}
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			
			adapter.notifyDataSetChanged();
			if(imgSync.getAnimation() != null) imgSync.getAnimation().cancel();
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

		@Override
		protected void onProgressUpdate(Void... values) {
			adapter.notifyDataSetChanged();
			super.onProgressUpdate(values);
		}
		
	}
	
	private ListLevelListener mListener = new ListLevelListener() {
		
		@Override
		public void onSelect(int examLevelId, String examLevelName) {
			Toast.makeText(mContext, "" + examLevelId, Toast.LENGTH_SHORT).show();
			
			Intent intent = new Intent(mContext, SelectionActivity.class);	

			intent.putExtra(Constants.SELECTION_MODE, Constants.SELECTION_MODE_EXAM);
			intent.putExtra(Constants.LEVEL_ID, examLevelId);
			intent.putExtra(Constants.LEVEL_NAME, examLevelName);
			startActivity(intent);
			overridePendingTransition(R.anim.fadein, R.anim.fadeout);
		}
	};

	@Override
	public void onBackPressed() {
		
	}

	
}
