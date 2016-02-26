package com.hanaone.tplt.adapter;

import java.util.List;

import com.hanaone.tplt.R;
import com.hanaone.tplt.db.ExamDataSet;
import com.hanaone.tplt.db.LevelDataSet;
import com.hanaone.tplt.db.dataset.ExamLevel.ExamLevelEntry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ListExamLevelAdapter extends BaseAdapter {
	private Context mContext;
	private ListExamLevelListener mListener;
	private List<LevelDataSet> levels;
	private LayoutInflater mInflater;
	
	public ListExamLevelAdapter(Context mContext,
			ListExamLevelListener mListener) {
		super();
		this.mContext = mContext;
		this.mListener = mListener;
		this.mInflater = LayoutInflater.from(mContext);
	}
	

	public void setLevels(List<LevelDataSet> levels) {
		this.levels = levels;
		this.notifyDataSetChanged();
	}


	@Override
	public int getCount() {
		if(levels != null) return levels.size(); 
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if(levels != null && levels.size() > position){
			return levels.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return levels.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.layout_exam_level, parent, false);
			
			holder = new ViewHolder();
			holder.layoutIcon = (RelativeLayout) convertView.findViewById(R.id.layout_icon);
			holder.txtLabel = (TextView) convertView.findViewById(R.id.txt_label);
			holder.imgNew = (ImageView) convertView.findViewById(R.id.img_new_lesson);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		LevelDataSet data = levels.get(position);
		if(data != null){
			//holder.txtLabel.setText(data.getLabel());
			//holder.imgNew.setVisibility(ImageView.VISIBLE);			
		}

		
		return convertView;
	}
	
	private class ViewHolder{
		RelativeLayout layoutIcon;
		ImageView imgNew;
		TextView txtLabel;
	}

}
