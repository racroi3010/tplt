package com.hanaone.tplt.adapter;

import java.util.List;

import com.hanaone.tplt.R;
import com.hanaone.tplt.db.ExamDataSet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ListExamAdapter extends BaseAdapter {
	private Context mContext;
	private ListExamLevelListener mListener;
	private List<ExamDataSet> exams;
	private LayoutInflater mInflater;
	public ListExamAdapter(Context mContext, ListExamLevelListener mListener) {
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
		return exams.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.layout_exam, parent, false);
			
			holder = new ViewHolder();
			holder.txtTitle = (TextView) convertView.findViewById(R.id.txt_title);
			holder.listLevel = (ListView) convertView.findViewById(R.id.list_level);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		ExamDataSet data = exams.get(position);
		if(data != null){
			holder.txtTitle.setText("TOPIK " + data.getNumber()+ "회");
			ListExamLevelAdapter adapter = new ListExamLevelAdapter(mContext, null);
			adapter.setLevels(data.getLevels());
			holder.listLevel.setAdapter(adapter);			
		}

		
		
		return convertView;
	}
	
	private class ViewHolder{
		TextView txtTitle;
		ListView listLevel;
	}

}
