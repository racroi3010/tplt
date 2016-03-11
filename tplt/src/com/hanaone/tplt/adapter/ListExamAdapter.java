package com.hanaone.tplt.adapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hanaone.http.DownloadHelper;
import com.hanaone.http.JsonReaderHelper;
import com.hanaone.tplt.Constants;
import com.hanaone.tplt.R;
import com.hanaone.tplt.db.ExamDataSet;
import com.hanaone.tplt.db.LevelDataSet;
import com.hanaone.tplt.db.SectionDataSet;
import com.hanaone.tplt.db.model.Section;
import com.hanaone.tplt.util.ColorUtils;
import com.hanaone.tplt.util.DatabaseUtils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ListExamAdapter extends BaseAdapter {
	private Context mContext;
	private ListLevelListener mListener;
	private List<ExamDataSet> exams;
	private LayoutInflater mInflater;
	private DatabaseAdapter dbAdapter;
	private List<DownloadInfo> infos;
	public ListExamAdapter(Context mContext, ListLevelListener mListener) {
		super();
		this.mContext = mContext;
		this.mListener = mListener;
		this.mInflater = LayoutInflater.from(mContext);
		this.dbAdapter  = new DatabaseAdapter(mContext);
	}

	public void setExams(List<ExamDataSet> exams) {
		this.exams = exams;
		this.notifyDataSetChanged();
	}
	

	public void setDownloadInfos(List<DownloadInfo> infos) {
		this.infos = infos;
	}

	@Override
	public int getCount() {
		if(exams != null) return exams.size(); 
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if(exams != null && exams.size() > position){
			return exams.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return exams.get(position).getNumber();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		ExamDataSet data = exams.get(position);
		final DownloadInfo info = infos.get(position);
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.layout_exam, parent, false);
			
			holder = new ViewHolder();
			holder.txtTitle = (TextView) convertView.findViewById(R.id.txt_title);
			holder.btnView = (Button) convertView.findViewById(R.id.btn_down);
			holder.layoutLevel3 = (LinearLayout) convertView.findViewById(R.id.layout_level3);
			holder.layoutLevel2 = (LinearLayout) convertView.findViewById(R.id.layout_level2);
			holder.layoutLevel1 = (LinearLayout) convertView.findViewById(R.id.layout_level1);

			holder.info = info;
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();

			holder.info.setPrgBar1(null);
			holder.info.setPrgBar2(null);
			holder.info.setPrgBar3(null);
			holder.info.setTxtView1(null);
			holder.info.setTxtView2(null);
			holder.info.setTxtView3(null);
			
			holder.info = info;
			
			holder.info.setPrgBar1((ProgressBar)holder.layoutLevel1.findViewById(R.id.prg_level_1));
			holder.info.setPrgBar2((ProgressBar)holder.layoutLevel2.findViewById(R.id.prg_level_2));
			holder.info.setPrgBar3((ProgressBar)holder.layoutLevel3.findViewById(R.id.prg_level_3));
			
			holder.info.setTxtView1((TextView)holder.layoutLevel1.findViewById(R.id.txt_score_1));
			holder.info.setTxtView2((TextView)holder.layoutLevel2.findViewById(R.id.txt_score_2));
			holder.info.setTxtView3((TextView)holder.layoutLevel3.findViewById(R.id.txt_score_3));
			
			
		} 		

		info.setPrgBar1((ProgressBar)holder.layoutLevel1.findViewById(R.id.prg_level_1));
		info.setPrgBar2((ProgressBar)holder.layoutLevel2.findViewById(R.id.prg_level_2));
		info.setPrgBar3((ProgressBar)holder.layoutLevel3.findViewById(R.id.prg_level_3));
		
		info.setTxtView1((TextView)holder.layoutLevel1.findViewById(R.id.txt_score_1));
		info.setTxtView2((TextView)holder.layoutLevel2.findViewById(R.id.txt_score_2));
		info.setTxtView3((TextView)holder.layoutLevel3.findViewById(R.id.txt_score_3));
		
		holder.btnView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(holder.layoutLevel3.getVisibility() != LinearLayout.GONE){
					
					holder.btnView.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
					holder.layoutLevel3.setVisibility(LinearLayout.GONE);
					holder.layoutLevel2.setVisibility(LinearLayout.GONE);
					holder.layoutLevel1.setVisibility(LinearLayout.GONE);					
				} else {
					holder.btnView.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
					holder.layoutLevel3.setVisibility(LinearLayout.VISIBLE);
					holder.layoutLevel2.setVisibility(LinearLayout.VISIBLE);
					holder.layoutLevel1.setVisibility(LinearLayout.VISIBLE);					
				}
				

			}
		});	
		
		int color = ColorUtils.randomColor();
		
		((ImageView)holder.layoutLevel3.findViewById(R.id.img_new_lesson_3)).setBackgroundColor(color);
		((ImageView)holder.layoutLevel2.findViewById(R.id.img_new_lesson_2)).setBackgroundColor(color);
		((ImageView)holder.layoutLevel1.findViewById(R.id.img_new_lesson_1)).setBackgroundColor(color);
		
		holder.layoutLevel3.setVisibility(RelativeLayout.GONE);
		holder.layoutLevel2.setVisibility(RelativeLayout.GONE);
		holder.layoutLevel1.setVisibility(RelativeLayout.GONE);
		
		if(data != null){
			final String examName = "TOPIK " + data.getNumber()+ "회";
			holder.txtTitle.setText(examName);
			
			final List<LevelDataSet> levels = data.getLevels();
			if(levels != null){
				for(final LevelDataSet level: levels){
					int number = level.getNumber();
					if(number == 1){
						holder.layoutLevel1.setVisibility(RelativeLayout.VISIBLE);
						((TextView)holder.layoutLevel1.findViewById(R.id.txt_label_1)).setText(level.getLabel() + "");
						holder.layoutLevel1.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								if(level.getActive() == Constants.STATUS_ACTIVE){
									String examLevelName = examName + " - " + level.getLabel();
									mListener.onSelect(level.getId(), examLevelName);
								} else {
									onclick(level, info);
								}
								
								
							}
						});	
						if(level.getActive() != Constants.STATUS_ACTIVE){
							holder.layoutLevel1.setAlpha(0.5f);
						} else {
							holder.layoutLevel1.setAlpha(1f);
						}				
					} else if(number == 2){
						holder.layoutLevel2.setVisibility(RelativeLayout.VISIBLE);
						((TextView)holder.layoutLevel2.findViewById(R.id.txt_label_2)).setText(level.getLabel() + "");
						holder.layoutLevel2.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								if(level.getActive() == Constants.STATUS_ACTIVE){
									String examLevelName = examName + " - " + level.getLabel();
									mListener.onSelect(level.getId(), examLevelName);
								} else {
									onclick(level, info);
								}
								
								
							}
						});	
						if(level.getActive() != Constants.STATUS_ACTIVE){
							holder.layoutLevel2.setAlpha(0.5f);
						} else {
							holder.layoutLevel2.setAlpha(1f);
						}							
					} else if(number == 3){
						holder.layoutLevel3.setVisibility(RelativeLayout.VISIBLE);
						((TextView)holder.layoutLevel3.findViewById(R.id.txt_label_3)).setText(level.getLabel() + "");
						holder.layoutLevel3.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								if(level.getActive() == Constants.STATUS_ACTIVE){
									String examLevelName = examName + " - " + level.getLabel();
									mListener.onSelect(level.getId(), examLevelName);
								} else {
									onclick(level, info);
								}
								
								
							}
						});	
						if(level.getActive() != Constants.STATUS_ACTIVE){
							holder.layoutLevel3.setAlpha(0.5f);
						} else {
							holder.layoutLevel3.setAlpha(1f);
						}							
					}
				}
			
			}
		}

		
		
		return convertView;
	}
	
	public void onclick(final LevelDataSet level, final DownloadInfo info){
		new AlertDialog.Builder(mContext)
		.setPositiveButton("YES", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
//				final Dialog mDialog = new Dialog(mContext);
//				RelativeLayout layout = (RelativeLayout) mInflater.inflate(R.layout.dialog_download, null);
//
//				mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//				mDialog.setContentView(layout);
//				
//				WindowManager.LayoutParams params = mDialog.getWindow().getAttributes();
//				params.height = 400;
//				params.width = LayoutParams.MATCH_PARENT;				
//				mDialog.getWindow().setAttributes(params);
//				
//				mDialog.setCanceledOnTouchOutside(false);
//				mDialog.show();
				

				
				new Downloading(level, info).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				
			}
		})
		.setNegativeButton("NO", null)
		.show();

	}
	private class Downloading extends AsyncTask<Void, Integer, Boolean>{
		private LevelDataSet level;
//		private DownloadInfo info;
//		private Dialog mDialog;
//		private ProgressBar prgBar;
//		private TextView txtPer;
		private DownloadInfo info;
		public Downloading(LevelDataSet level, DownloadInfo info){
			
			this.level = level;
//			this.info = info;
//			this.mDialog = mDialog;
//
//			this.prgBar = (ProgressBar) mDialog.findViewById(R.id.prg_download);
//			this.txtPer = (TextView) mDialog.findViewById(R.id.txt_download_percent);
			this.info = info;
		}
		

		@Override
		protected void onPreExecute() {
			ProgressBar prgBar = null;
			TextView txtPer = null;
			switch (this.level.getNumber()) {
			case 1:
				prgBar = this.info.getPrgBar1();
				txtPer = this.info.getTxtView1();
				break;
			case 2:
				prgBar = this.info.getPrgBar2();
				txtPer = this.info.getTxtView2();				
				break;
			case 3:
				prgBar = this.info.getPrgBar3();
				txtPer = this.info.getTxtView3();				
				break;				
			default:
				break;
			}		
			if(prgBar != null) prgBar.setProgress(0);
			if(txtPer != null) txtPer.setText("0%");
			//this.layout.setAlpha(1f);
			
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if(!result){
				mHandler.obtainMessage(HANDLE_ACTIVE_LEVEL, false).sendToTarget();
				
			} else {
				mHandler.obtainMessage(HANDLE_ACTIVE_LEVEL, true).sendToTarget();
				
			}
			super.onPostExecute(result);
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			//level = params[0];
			
			DownloadHelper dlHelper = new DownloadHelper(mContext);
			
			// download audio
			boolean audioFlag = false;
			boolean txtFlag = false;
			String url = level.getAudio().get(0).getPath();
			String rootPath = Constants.getRootPath(mContext);
			String audioPath = rootPath + "/" + Constants.FILE_TYPE_MP3 + "_" + level.getId() + ".mp3";
			File file = new File(audioPath);
			if(url.contains("http")){
				try {				
					InputStream is = dlHelper.parseUrl(url);
					if(is != null){

						FileOutputStream os = new FileOutputStream(file);
						
						byte[] buf = new byte[1024];
						int read = 0;
												
						int a = 0;
						int sum = 0;
						int size = 73450798;
						while((read = is.read(buf)) > 0){
							os.write(buf, 0, read);	
							//a = is.available();	
							sum += read;
							a = (int) (((float)sum/size) * 100);		
							
							if(a != 0){
								publishProgress(a);
							}
						}
						os.close();
						is.close();	
						
						// update audio
						audioFlag = true;
											
					}						
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					//showMsg(e.getMessage());
				}				
			}

			
			// download text
			
			url = level.getTxt().getPath();
			String txtPath = rootPath + "/" + Constants.FILE_TYPE_TXT + "_" + level.getId() + ".txt";
			if(url.contains("http")){
				try {
					InputStream is = dlHelper.parseUrl(url);
					if(is != null){
						file = new File(txtPath);
						FileOutputStream os = new FileOutputStream(file);
						
						byte[] buf = new byte[1024];
						int read = 0;

						while((read = is.read(buf)) > 0){
							os.write(buf, 0, read);	
						}
						os.close();
						is.close();		
						
						// read
						txtFlag = true;

					}		
				} catch (IOException e) {
					showMsg(e.getMessage());
					e.printStackTrace();
				}					
			}

			// update level
			if(audioFlag && dbAdapter.updateLevelAudio(level.getId(), audioPath) > 0){
				level.getAudio().get(0).setPath(audioPath);
			}
			if(txtFlag && dbAdapter.updateLevelTxt(level.getId(), txtPath) > 0){
				level.getTxt().setPath(txtPath);
				file = new File(txtPath);
				if(file.exists() && file.isFile()){
					List<SectionDataSet> sections;
					try {
						sections = JsonReaderHelper.readSections(file);
						for(SectionDataSet data: sections){
							
							dbAdapter.addSection(data, level.getId());						
						}		
					} catch (IOException e) {
						
						e.printStackTrace();
						//showMsg(e.getMessage());	
					}
			
				}
			}
			
			return true;
		}
		
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			
			int p = values[0];
			ProgressBar prgBar = null;
			TextView txtPer = null;
			switch (this.level.getNumber()) {
			case 1:
				prgBar = this.info.getPrgBar1();
				txtPer = this.info.getTxtView1();
				break;
			case 2:
				prgBar = this.info.getPrgBar2();
				txtPer = this.info.getTxtView2();				
				break;
			case 3:
				prgBar = this.info.getPrgBar3();
				txtPer = this.info.getTxtView3();				
				break;				
			default:
				break;
			}		
			if(prgBar != null) prgBar.setProgress(p);
			if(txtPer != null) txtPer.setText(p + "%");
		}


		private Handler mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case HANDLE_INSERT_SECTION:
					String path = (String) msg.obj;


					File file = new File(path);
					if(file.exists() && file.isFile()){
						List<SectionDataSet> sections;
						try {
							sections = JsonReaderHelper.readSections(file);
							for(SectionDataSet data: sections){
								
								dbAdapter.addSection(data, level.getId());						
							}		
							obtainMessage(HANDLE_ACTIVE_LEVEL, true).sendToTarget();
						} catch (IOException e) {
							
							e.printStackTrace();
							showMsg(e.getMessage());	
							obtainMessage(HANDLE_ACTIVE_LEVEL, false).sendToTarget();
						}
				
					}
					break;					
				case HANDLE_ACTIVE_LEVEL:
					boolean result = (Boolean) msg.obj;
					if(result){
										
						ProgressBar prgBar = null;
						TextView txtPer = null;
						switch (level.getNumber()) {
						case 1:
							prgBar = info.getPrgBar1();
							txtPer = info.getTxtView1();
							break;
						case 2:
							prgBar = info.getPrgBar2();
							txtPer = info.getTxtView2();				
							break;
						case 3:
							prgBar = info.getPrgBar3();
							txtPer = info.getTxtView3();				
							break;				
						default:
							break;
						}		
						if(prgBar != null) prgBar.setProgress(100);
						if(txtPer != null) txtPer.setText("100%");
						level.setActive(Constants.STATUS_ACTIVE);
						//layout.setAlpha(1f);
						int updatedActive = dbAdapter.updateLevelActive(level.getId(), true);
						
						showMsg("download finish! " + updatedActive);			
					} else {
						//layout.setAlpha(0.5f);
						showMsg("download failed!");
					}						
					break;
				default:
					break;
				}
			}			
		};
		
	}

	private static final int HANDLE_INSERT_SECTION = 2;
	private static final int HANDLE_ACTIVE_LEVEL = 3;
	
	private void showMsg(String msg){
		Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
	}
	private class ViewHolder{
		TextView txtTitle;
		Button btnView;
		//ListView listLevel;
		LinearLayout layoutLevel3;
		LinearLayout layoutLevel2;
		LinearLayout layoutLevel1;
		
		DownloadInfo info;
	}

	

}
