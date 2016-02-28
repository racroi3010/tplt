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
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.hanaone.gg.DownloadHelper;
import com.hanaone.tplt.adapter.DatabaseAdapter;
import com.hanaone.tplt.adapter.ListExamAdapter;
import com.hanaone.tplt.adapter.ListLevelListener;
import com.hanaone.tplt.db.ExamDataSet;
import com.hanaone.tplt.db.LevelDataSet;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
	private Context mContext;
	private DatabaseAdapter dbAdapter;
	private ListView listExam;
	private List<ExamDataSet> list;
	private ListExamAdapter adapter;
	
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mContext = this;
		dbAdapter = new DatabaseAdapter(mContext);
		
		listExam = (ListView) findViewById(R.id.list_exam);
        	
		init();
	}
    @Override
	protected void onResume() {
		super.onResume();
	
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
		boolean loaded = false;
		try {
			loaded = dHelper.loadExam("https://www.dropbox.com/s/z4tg1y2hywda5u1/tax.txt?dl=0");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(loaded){
			File folder = mContext.getDir("tplt", Context.MODE_PRIVATE);
			File file = new File(folder.getAbsolutePath() + "/config.txt");
			if(file.exists()){
				try {
					BufferedReader reader = new BufferedReader(new FileReader(file));
					String txt = "";
					while((txt = reader.readLine()) != null){
						if(txt.equals("#EXAM")){
							ExamDataSet exam = new ExamDataSet();
							int number = Integer.parseInt(reader.readLine());
							if(!dbAdapter.checkExam(number)) {
								exam.setNumber(number);
								String date = reader.readLine();
								exam.setDate(date);
								int numOfLevels = Integer.parseInt(reader.readLine());
								List<LevelDataSet> levels = new ArrayList<LevelDataSet>();
								for(int i = 0; i < numOfLevels; i ++){
									String level = reader.readLine();
									String[] temp = level.split(";");
									LevelDataSet data = new LevelDataSet();
									data.setNumber(Integer.parseInt(temp[0]));
									data.setLabel(Integer.parseInt(temp[0]) + "");
									data.setUrl(temp[1]);
									levels.add(data);
								}
								exam.setLevels(levels);
								
								dbAdapter.addExam(exam);
								
								list.add(exam);								
							}

						}
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
			super.onPostExecute(result);
		}
		
	}
	
	private ListLevelListener mListener = new ListLevelListener() {
		
		@Override
		public void onSelect(int examLevelId) {
			Toast.makeText(mContext, "" + examLevelId, Toast.LENGTH_SHORT).show();
			
		}
	};
	
}
