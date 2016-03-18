package com.hanaone.tplt.adapter;

import java.util.List;

import com.hanaone.tplt.Constants;
import com.hanaone.tplt.QuestionActivity;
import com.hanaone.tplt.R;
import com.hanaone.tplt.db.ResultDataSet;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class ListResultAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mInflater;
	private ListAdapterListener mListener;
	private List<ResultDataSet> dataSets;
	public ListResultAdapter(Context mContext, ListAdapterListener mListener) {
		super();
		this.mContext = mContext;
		this.mListener = mListener;
		this.mInflater = LayoutInflater.from(mContext);		
	}

	public void setDataSets(List<ResultDataSet> dataSets) {
		this.dataSets = dataSets;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if(dataSets != null){
			return dataSets.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if(dataSets != null && dataSets.size() > position){
			return dataSets.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.layout_result, parent, false);
			
			holder = new ViewHolder();
			holder.btnNumber = (Button) convertView.findViewById(R.id.btn_result_number);
			holder.txtChoice = (TextView) convertView.findViewById(R.id.txt_result_choice);
			holder.txtScore = (TextView) convertView.findViewById(R.id.txt_result_score);
			holder.btnReview = (Button) convertView.findViewById(R.id.btn_result_review);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		final ResultDataSet data = dataSets.get(position);
		if(data != null){
			holder.btnNumber.setText(data.getNumber() + "");
			holder.txtScore.setText("0");
			if(data.getChoice() == data.getAnswer()){
				holder.txtChoice.setText(data.getChoice() + "");
				//holder.txtChoice.setTextColor(mContext.getResources().getColor(R.color.GREEN));
				holder.btnNumber.setBackgroundResource(R.drawable.num_green);
				
				holder.txtScore.setText(data.getScore() + "");
			} else if(data.getChoice() != -1) {
				holder.txtChoice.setText(data.getChoice() + "");
				//holder.txtChoice.setTextColor(mContext.getResources().getColor(R.color.RED));	
				holder.btnNumber.setBackgroundResource(R.drawable.num_red);
				holder.txtScore.setText("0");	
			} else {
				holder.txtChoice.setText("");
				//holder.txtChoice.setTextColor(mContext.getResources().getColor(R.color.GREEN));
				holder.btnNumber.setBackgroundResource(R.drawable.num_trans);
				
				holder.txtScore.setText("0");				
			}
			final int mP = position;
			holder.btnReview.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mListener.onSelect(mP);
					
				}
			});
		}
		
		
		return convertView;
	}
	
	private class ViewHolder{
		Button btnNumber;
		TextView txtChoice;
		TextView txtScore;
		Button btnReview;
	}

}
