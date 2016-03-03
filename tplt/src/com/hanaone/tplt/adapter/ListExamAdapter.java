package com.hanaone.tplt.adapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.hanaone.gg.DownloadHelper;
import com.hanaone.gg.JsonReaderHelper;
import com.hanaone.tplt.R;
import com.hanaone.tplt.db.ExamDataSet;
import com.hanaone.tplt.db.LevelDataSet;
import com.hanaone.tplt.db.SectionDataSet;
import com.hanaone.tplt.db.model.Section;
import com.hanaone.tplt.util.ColorUtils;
import com.hanaone.tplt.util.DatabaseUtils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.layout_exam, parent, false);
			
			holder = new ViewHolder();
			holder.txtTitle = (TextView) convertView.findViewById(R.id.txt_title);
			holder.btnView = (Button) convertView.findViewById(R.id.btn_down);
			holder.layoutLevel3 = (LinearLayout) convertView.findViewById(R.id.layout_level3);
			holder.layoutLevel2 = (LinearLayout) convertView.findViewById(R.id.layout_level2);
			holder.layoutLevel1 = (LinearLayout) convertView.findViewById(R.id.layout_level1);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
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
		
		ExamDataSet data = exams.get(position);
		if(data != null){
			holder.txtTitle.setText("TOPIK " + data.getNumber()+ "회");
			
			final List<LevelDataSet> levels = data.getLevels();
			if(levels != null){
				switch (levels.size()) {
					case 3:
						holder.layoutLevel3.setVisibility(RelativeLayout.VISIBLE);
						((TextView)holder.layoutLevel3.findViewById(R.id.txt_label_3)).setText(levels.get(2).getNumber() + "");
						holder.layoutLevel3.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								if(levels.get(2).isActive()){
									mListener.onSelect(levels.get(2).getId());
								} else {
									onclick(holder.layoutLevel3, levels.get(2));
								}								
								
							}
						});	
						if(!levels.get(2).isActive()){
							holder.layoutLevel3.setAlpha(0.5f);
						} else {
							holder.layoutLevel3.setAlpha(1f);
						}						
					case 2:		
						((TextView)holder.layoutLevel2.findViewById(R.id.txt_label_2)).setText(levels.get(1).getNumber() + "");
						holder.layoutLevel2.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								if(levels.get(1).isActive()){
									mListener.onSelect(levels.get(1).getId());
								} else {
									onclick(holder.layoutLevel2, levels.get(1));
								}
								
								
							}
						});
						if(!levels.get(1).isActive()){
							holder.layoutLevel2.setAlpha(0.5f);
						} else {
							holder.layoutLevel2.setAlpha(1f);
						}
						((TextView)holder.layoutLevel1.findViewById(R.id.txt_label_1)).setText(levels.get(0).getNumber() + "");
						holder.layoutLevel1.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								if(levels.get(0).isActive()){
									mListener.onSelect(levels.get(0).getId());
								} else {
									onclick(holder.layoutLevel1, levels.get(0));
								}
								
								
							}
						});	
						if(!levels.get(0).isActive()){
							holder.layoutLevel1.setAlpha(0.5f);
						} else {
							holder.layoutLevel1.setAlpha(1f);
						}
						
						if(levels.size() == 2){
							holder.layoutLevel3.setVisibility(LinearLayout.GONE);
						}
													
						break;
					default:
						break;
				}
			
			}
		}

		
		
		return convertView;
	}
	public void onclick(final LinearLayout v, final LevelDataSet level){
		new AlertDialog.Builder(mContext)
		.setPositiveButton("YES", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				ProgressBar prgBar = null;
				switch (v.getId()) {
				case R.id.layout_level1:
					prgBar = (ProgressBar) v.findViewById(R.id.prg_level_1);
					txtPer = (TextView) v.findViewById(R.id.txt_score_1);
					break;
				case R.id.layout_level2:
					prgBar = (ProgressBar) v.findViewById(R.id.prg_level_2);
					txtPer = (TextView) v.findViewById(R.id.txt_score_2);									
					break;
				case R.id.layout_level3:
					prgBar = (ProgressBar) v.findViewById(R.id.prg_level_3);
					txtPer = (TextView) v.findViewById(R.id.txt_score_3);	
						
					break;			
				default:
					break;
				}
				
				new Downloading(v,prgBar).execute(level);
				
				dialog.dismiss();
			}
		})
		.setNegativeButton("NO", null)
		.show();

	}
	private class Downloading extends AsyncTask<LevelDataSet, Boolean, Boolean>{
		private LinearLayout layout;
		private ProgressBar prgBar;
		//private TextView txtPer;
		private LevelDataSet level;

		public Downloading(LinearLayout layout, ProgressBar prgBar) {
			super();
			this.prgBar = prgBar;
			//this.txtPer = txtPer;
			this.layout = layout;
		}

		@Override
		protected void onPreExecute() {
			this.prgBar.setProgress(0);
			txtPer.setText("0%");
			this.layout.setAlpha(1f);
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if(result){
				this.prgBar.setProgress(100);
				txtPer.setText("100%");
				level.setActive(true);
				this.layout.setAlpha(1f);
				int updatedActive = dbAdapter.updateLevelActive(level.getId(), true);
				Toast.makeText(mContext, "download finish! " + updatedActive, Toast.LENGTH_SHORT).show();				
			} else {
				this.layout.setAlpha(0.5f);
				Toast.makeText(mContext, "download failed!", Toast.LENGTH_SHORT).show();
			}

			super.onPostExecute(result);
		}

		@Override
		protected Boolean doInBackground(LevelDataSet... params) {
			level = params[0];
			String url = level.getAudio().getPath();
			DownloadHelper dlHelper = new DownloadHelper(mContext);
//			try {
//				InputStream is = dlHelper.parseUrl(url);
//				if(is != null){
//					File folder = mContext.getDir("tplt", Context.MODE_PRIVATE);
//					File file = new File(folder.getAbsolutePath() + "/test.mp3");
//					FileOutputStream os = new FileOutputStream(file);
//					
//					byte[] buf = new byte[1024];
//					int read = 0;
//											
//					int a = 0;
//					int sum = 0;
//					int size = 73450798;
//					while((read = is.read(buf)) > 0){
//						os.write(buf, 0, read);	
//						//a = is.available();	
//						sum += read;
//						a = (int) (((float)sum/size) * 100);		
//						
//						if(a != 0){
//							this.prgBar.setProgress(a);
//							mHandler.obtainMessage(HANDLE_PERCENTAGE, a).sendToTarget();
//						}
//					}
//					os.close();
//					is.close();		
//
//				}		
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				return false;
//			}
//			
			// download text
			
			String txt = level.getTxt().getPath();
			try {
				InputStream is = dlHelper.parseUrl(txt);
				if(is != null){
					File folder = mContext.getDir("tplt", Context.MODE_PRIVATE);
					File file = new File(folder.getAbsolutePath() + "/test.txt");
					FileOutputStream os = new FileOutputStream(file);
					
					byte[] buf = new byte[1024];
					int read = 0;

					while((read = is.read(buf)) > 0){
						os.write(buf, 0, read);	
					}
					os.close();
					is.close();		
					
					// read
					List<SectionDataSet> sections = JsonReaderHelper.readSections(file);
					for(SectionDataSet data: sections){
						dbAdapter.addSection(data, level.getId());						
					}
							

				}		
			} catch (IOException e) {
				
				e.printStackTrace();
				return false;
			}			
			
			return true;
		}
		
	}
//	
//	private SectionDataSet readSection(BufferedReader bufferedReader) throws IOException{
//		SectionDataSet section = new SectionDataSet();
//		String line = "";
//		String temp = "";		
//		while((line = bufferedReader.readLine()) != null){
//			if("#SECTION_END".equals(line)){
//				break;
//			}
//			// section number;
//			String sectionNumber = bufferedReader.readLine();
//			section.setNumber(Integer.parseInt(sectionNumber));			
//
//			// section question
//			if("#SECTION_QUESTION_BEGIN".equals(bufferedReader.readLine())){
//				temp = "";
//				while((line = bufferedReader.readLine()) != null){
//					if(!"#SECTION_QUESTION_END".equals(line)){
//						temp += line;
//					} else {
//						break;
//					}
//				}
//				section.setText(temp);						
//			}
//			
//			// section hint
//			if("#SECTION_HINT_BEGIN".equals(bufferedReader.readLine())){
//				temp = "";
//				while((line = bufferedReader.readLine()) != null){
//					if(!"#SECTION_HINT_END".equals(line)){
//						temp += line;
//					} else {
//						break;
//					}
//				}
//				section.setHint(temp);					
//			}		
//			
//			// question
//
//			if("#QUESTION".equals(bufferedReader.readLine())){
//				temp = "";
//				while((line = bufferedReader.readLine()) != null){
//					if(!"#SECTION_HINT_END".equals(line)){
//						temp += line;
//					} else {
//						break;
//					}
//				}
//				section.setHint(temp);					
//			}						
//		}
//
//		return section;
//	}
	
	private static final int HANDLE_PERCENTAGE = 1;
	private TextView txtPer;
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HANDLE_PERCENTAGE:
				int per = (Integer) msg.obj;
				txtPer.setText(per + "%");
				break;

			default:
				break;
			}
		}
		
	};
	private class ViewHolder{
		TextView txtTitle;
		Button btnView;
		//ListView listLevel;
		LinearLayout layoutLevel3;
		LinearLayout layoutLevel2;
		LinearLayout layoutLevel1;
	}


}
