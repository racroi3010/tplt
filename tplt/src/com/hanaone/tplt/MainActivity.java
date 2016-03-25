package com.hanaone.tplt;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.hanaone.http.DownloadHelper;
import com.hanaone.http.JsonReaderHelper;
import com.hanaone.jni.JNIHanaone;
import com.hanaone.tplt.adapter.DatabaseAdapter;
import com.hanaone.tplt.adapter.DownloadInfo;
import com.hanaone.tplt.adapter.ExamItem;
import com.hanaone.tplt.adapter.ListExamAdapter;
import com.hanaone.tplt.adapter.ListExamHeaderAdapter;
import com.hanaone.tplt.adapter.ListLevelListener;
import com.hanaone.tplt.db.ExamDataSet;
import com.hanaone.tplt.db.LevelDataSet;
import com.hanaone.tplt.util.ColorUtils;
import com.hanaone.tplt.util.LocaleUtils;
import com.hanaone.tplt.util.PreferenceHandler;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
	private Context mContext;
	private DatabaseAdapter dbAdapter;
	private SwipeMenuListView listExam;
	private List<ExamItem> listItem;
	private List<ExamDataSet> list;
//	private List<DownloadInfo> infos;
//	private ListExamAdapter adapter;
	private ListExamHeaderAdapter adapter;
	private ImageView imgSync;
	private LinearLayout layoutSync;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		dbAdapter = new DatabaseAdapter(mContext);
		
		initLayout();
		
		initData();
	}
    @Override
	protected void onResume() {
		super.onResume();


		new LoadingNewData().execute();
	}
 
    @Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
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
			
			// check update locale
			boolean updateLocale = intent.getBooleanExtra(Constants.UPDATE_LOCALE, false);
			if(updateLocale){
				initLayout();
				updateData();
			}
		}
	}
	public void onClick(View v){
    	switch (v.getId()) {
		case R.id.btn_setting:
			Intent intent = new Intent(mContext, HelpActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_sample_test:
			intent = new Intent(mContext, SelectionActivity.class);
			intent.putExtra(Constants.SELECTION_MODE, Constants.SELECTION_MODE_SAMPLE);

			startActivity(intent);
			overridePendingTransition(R.anim.fadein, R.anim.fadeout);
			break;
		default:
			break;
		}
    }
	private void initData(){
//		adapter = new ListExamAdapter(mContext, mListener);	
//		listExam.setAdapter(adapter);		
//		list = dbAdapter.getAllExam();		
//		infos = new ArrayList<DownloadInfo>();
//		for(int i = 0; i < list.size(); i ++){
//			infos.add(new DownloadInfo());
//		}
//		adapter.setDownloadInfos(infos);
//		adapter.setExams(list);		
//		new LoadingNewData().execute();
		
		// test
		listItem = new ArrayList<ExamItem>();
		list = dbAdapter.getAllExam();
//		for(ExamDataSet exam: list){
//			listItem.add(new ListExamHeaderAdapter.ExamHeader(exam));
//			for(LevelDataSet level: exam.getLevels()){
//				listItem.add(new ListExamHeaderAdapter.ExamLevelItem(level, new DownloadInfo()));
//			}
//		}		
		for(int i = 0; i < 100; i ++){
			ExamDataSet exam = new ExamDataSet();
			exam.setNumber(i);
			listItem.add(new ListExamHeaderAdapter.ExamHeader(exam));
			for(int j = 0; j < 5; j ++){
				LevelDataSet level = new LevelDataSet();
				level.setLabel(j + "");
				listItem.add(new ListExamHeaderAdapter.ExamLevelItem(level, new DownloadInfo()));
			}
		}				
		adapter = new ListExamHeaderAdapter(mContext, null);	
		adapter.setItems(listItem);
		listExam.setAdapter(adapter);
		
		SwipeMenuCreator creator = new SwipeMenuCreator() {

		    @Override
		    public void create(SwipeMenu menu) {
		        // create "open" item
		        SwipeMenuItem openItem = new SwipeMenuItem(
		                getApplicationContext());
		        // set item background
		        openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
		                0xCE)));
		        // set item width
		        openItem.setWidth(dp2px(90));
		        // set item title
		        openItem.setTitle("Open");
		        // set item title fontsize
		        openItem.setTitleSize(18);
		        // set item title font color
		        openItem.setTitleColor(Color.WHITE);
		        // add to menu
		        menu.addMenuItem(openItem);

		        // create "delete" item
		        SwipeMenuItem deleteItem = new SwipeMenuItem(
		                getApplicationContext());
		        // set item background
		        deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
		                0x3F, 0x25)));
		        // set item width
		        deleteItem.setWidth(dp2px(90));
		        // set a icon
		        deleteItem.setIcon(R.drawable.ic_launcher);
		        
		        
		        // add to menu
		        menu.addMenuItem(deleteItem);
		    }
		};	
		listExam.setMenuCreator(creator);
		
		listExam.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
				switch (index) {
				case 0:
					Toast.makeText(mContext, "click Open " + position, Toast.LENGTH_SHORT).show();
					break;
				case 1:
					Toast.makeText(mContext, "click Delete " + position, Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}
				return false;
			}
		});
		
		new LoadingNewData().execute();
	}
	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}	
	private void updateData(){
		listExam.setAdapter(adapter);	
		adapter.notifyDataSetChanged();
		new LoadingNewData().execute();
	}
	private void initLayout(){
		int position = PreferenceHandler.getLanguagePositionPreference(mContext);
		LocaleUtils.setLocale(mContext, position);		
		setContentView(R.layout.activity_main);				
		
		
		listExam = (SwipeMenuListView) findViewById(R.id.list_exam);
		
		imgSync = (ImageView) findViewById(R.id.img_sync);
		layoutSync = (LinearLayout) findViewById(R.id.layout_sync);	
		
		
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
								//exam.setColor(colorNew);
								for(LevelDataSet level: exam.getLevels()){
									level.setColor(colorNew);
								}
								dbAdapter.addExam(exam);
								
								list.add(exam);	
								//infos.add(new DownloadInfo());
								listItem.add(new ListExamHeaderAdapter.ExamHeader(exam));
								for(LevelDataSet level: exam.getLevels()){
									listItem.add(new ListExamHeaderAdapter.ExamLevelItem(level, new DownloadInfo()));
								}
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
		moveTaskToBack(true);
	}

	
}
