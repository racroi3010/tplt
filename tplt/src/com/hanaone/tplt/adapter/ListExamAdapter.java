package com.hanaone.tplt.adapter;

import java.util.List;

import com.hanaone.tplt.R;
import com.hanaone.tplt.db.ExamDataSet;
import com.hanaone.tplt.db.LevelDataSet;
import com.hanaone.tplt.util.ColorUtils;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ListExamAdapter extends BaseAdapter {
	private Context mContext;
	private ListLevelListener mListener;
	private List<ExamDataSet> exams;
	private LayoutInflater mInflater;
	public ListExamAdapter(Context mContext, ListLevelListener mListener) {
		super();
		this.mContext = mContext;
		this.mListener = mListener;
		this.mInflater = LayoutInflater.from(mContext);
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
			holder.layoutLevel3 = (RelativeLayout) convertView.findViewById(R.id.layout_level3);
			holder.layoutLevel2 = (RelativeLayout) convertView.findViewById(R.id.layout_level2);
			holder.layoutLevel1 = (RelativeLayout) convertView.findViewById(R.id.layout_level1);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		int color = ColorUtils.randomColor();
		
		((ImageView)holder.layoutLevel3.findViewById(R.id.img_new_lesson_3)).setBackgroundColor(color);
		((ImageView)holder.layoutLevel2.findViewById(R.id.img_new_lesson_2)).setBackgroundColor(color);
		((ImageView)holder.layoutLevel1.findViewById(R.id.img_new_lesson_1)).setBackgroundColor(color);
		
		ExamDataSet data = exams.get(position);
		if(data != null){
			holder.txtTitle.setText("TOPIK " + data.getNumber()+ "회");
			
			final List<LevelDataSet> levels = data.getLevels();
			if(levels != null){
				if(levels.size() == 2){
					holder.layoutLevel3.setVisibility(RelativeLayout.GONE);
					((TextView)holder.layoutLevel2.findViewById(R.id.txt_label_2)).setText(levels.get(1).getNumber() + "");
					holder.layoutLevel2.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							mListener.onSelect(levels.get(1).getId());
							
						}
					});
					if(!levels.get(1).isActive()){
						holder.layoutLevel2.setAlpha(0.5f);
					}
					((TextView)holder.layoutLevel1.findViewById(R.id.txt_label_1)).setText(levels.get(0).getNumber() + "");
					holder.layoutLevel1.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							mListener.onSelect(levels.get(0).getId());
							
						}
					});	
					if(!levels.get(0).isActive()){
						holder.layoutLevel1.setAlpha(0.5f);
					}					
				} else if(levels.size() == 3){
					holder.layoutLevel3.setVisibility(RelativeLayout.VISIBLE);
					((TextView)holder.layoutLevel3.findViewById(R.id.txt_label_3)).setText(levels.get(2).getNumber() + "");
					holder.layoutLevel3.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							mListener.onSelect(levels.get(2).getId());
							
						}
					});		
					if(!levels.get(2).isActive()){
						holder.layoutLevel3.setAlpha(0.5f);
					}						
					((TextView)holder.layoutLevel2.findViewById(R.id.txt_label_2)).setText(levels.get(1).getNumber() + "");
					holder.layoutLevel2.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							mListener.onSelect(levels.get(1).getId());
							
						}
					});
					((TextView)holder.layoutLevel1.findViewById(R.id.txt_label_1)).setText(levels.get(0).getNumber() + "");
					holder.layoutLevel1.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							mListener.onSelect(levels.get(0).getId());
							
						}
					});						
				}
			
			}
		}

		
		
		return convertView;
	}
	
	private class ViewHolder{
		TextView txtTitle;
		//ListView listLevel;
		RelativeLayout layoutLevel3;
		RelativeLayout layoutLevel2;
		RelativeLayout layoutLevel1;
	}


}
