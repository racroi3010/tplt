package com.hanaone.tplt.adapter;

import java.util.List;

import com.hanaone.tplt.Constants;
import com.hanaone.tplt.R;
import com.hanaone.tplt.db.ChoiceDataSet;
import com.hanaone.tplt.db.QuestionDataSet;
import com.hanaone.tplt.db.SectionDataSet;
import com.hanaone.tplt.util.ImageUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

public class ListSectionAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mInflater;
	private ListAdapterListener mListener;
	private List<SectionDataSet> mDataSet;
	
	public ListSectionAdapter(Context mContext, ListAdapterListener mListener) {
		this.mContext = mContext;
		this.mListener = mListener;
		this.mInflater = LayoutInflater.from(mContext);
	}

	public void setmDataSet(List<SectionDataSet> mDataSet) {
		this.mDataSet = mDataSet;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if(mDataSet != null){
			return mDataSet.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if(mDataSet != null && mDataSet.size() > position){
			return mDataSet.get(position);
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
		if(null == convertView){
			convertView = mInflater.inflate(R.layout.layout_question_section, parent, false);
			
			holder = new ViewHolder();
			holder.txtQuestion = (TextView) convertView.findViewById(R.id.txt_section_question);
			holder.txtHint = (TextView) convertView.findViewById(R.id.txt_section_hint);
			holder.layoutQuestion = (LinearLayout) convertView.findViewById(R.id.layout_questions);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		final SectionDataSet section = mDataSet.get(position);

		if(section.getHint() == null || section.getHint().isEmpty()){
			holder.txtHint.setVisibility(TextView.GONE);
		} else {
			holder.txtHint.setText(section.getHint());
			holder.txtHint.setVisibility(TextView.VISIBLE);
		}		
		
			
		holder.layoutQuestion.removeAllViews();
		List<QuestionDataSet> questions = section.getQuestions();

		String txt = "";
		if(questions != null && !questions.isEmpty()){
			txt += "#[" + questions.get(0).getNumber() + "~" + questions.get(questions.size() - 1).getNumber() + "] ";
		}
		txt += section.getText();	
		holder.txtQuestion.setText(txt);
		
		if(questions != null){
			for(final QuestionDataSet question: questions){
				LinearLayout questionView = (LinearLayout) mInflater.inflate(R.layout.layout_question_question, holder.layoutQuestion, false);
				
				TextView txtNumber = (TextView) questionView.findViewById(R.id.txt_question_number);
				TextView txtQuestionHint = (TextView) questionView.findViewById(R.id.txt_question_hint);
				final RadioButton rd1 = (RadioButton) questionView.findViewById(R.id.rd_question_choice_1);
				final RadioButton rd2 = (RadioButton) questionView.findViewById(R.id.rd_question_choice_2);
				final RadioButton rd3 = (RadioButton) questionView.findViewById(R.id.rd_question_choice_3);
				final RadioButton rd4 = (RadioButton) questionView.findViewById(R.id.rd_question_choice_4);
				
				TextView txtQuestionTxt = (TextView) questionView.findViewById(R.id.txt_question_txt);
				ImageView imgQuestion = (ImageView) questionView.findViewById(R.id.img_question);
				
				ImageView img1 = (ImageView) questionView.findViewById(R.id.img_question_choice_1);
				ImageView img2 = (ImageView) questionView.findViewById(R.id.img_question_choice_2);
				ImageView img3 = (ImageView) questionView.findViewById(R.id.img_question_choice_3);
				ImageView img4 = (ImageView) questionView.findViewById(R.id.img_question_choice_4);
				
				
				Button btnQuestionHint = (Button) questionView.findViewById(R.id.btn_question_hint);
				final LinearLayout layoutQuestionHint = (LinearLayout) questionView.findViewById(R.id.layout_question_hint);
				btnQuestionHint.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						if(layoutQuestionHint.getVisibility() == LinearLayout.VISIBLE){
							layoutQuestionHint.setVisibility(LinearLayout.GONE);
						} else {
							layoutQuestionHint.setVisibility(LinearLayout.VISIBLE);
						}
					}
				});
				
				String questionTxt = question.getText(); 
				if(questionTxt != null && !questionTxt.isEmpty()){
					txtQuestionTxt.setText(questionTxt);
				} else {
					txtQuestionTxt.setText("(" + question.getMark() + "점)");
				}
				
				
				txtNumber.setText(question.getNumber() + ". ");
				txtQuestionHint.setText(question.getHint());
				
				List<ChoiceDataSet> choices = question.getChoices();
				if(choices != null){
					if(Constants.FILE_TYPE_IMG.equals(question.getChoiceType())){
						rd1.setText("");
						rd2.setText("");
						rd3.setText("");
						rd4.setText("");
						
						img1.setImageBitmap(ImageUtils.decodeSampledBitmapFromFile(choices.get(0).getText(), 200, 200));
						img2.setImageBitmap(ImageUtils.decodeSampledBitmapFromFile(choices.get(1).getText(), 200, 200));
						img3.setImageBitmap(ImageUtils.decodeSampledBitmapFromFile(choices.get(2).getText(), 200, 200));
						img4.setImageBitmap(ImageUtils.decodeSampledBitmapFromFile(choices.get(3).getText(), 200, 200));
						
						img1.setVisibility(ImageView.VISIBLE);
						img2.setVisibility(ImageView.VISIBLE);
						img3.setVisibility(ImageView.VISIBLE);
						img4.setVisibility(ImageView.VISIBLE);
					} else {
						rd1.setText(choices.get(0).getText());
						rd2.setText(choices.get(1).getText());
						rd3.setText(choices.get(2).getText());
						rd4.setText(choices.get(3).getText());							
					}
					
				}
				rd1.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						onChoose(question, 1, rd1, rd2, rd3, rd4);
					}
				});
				rd2.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						onChoose(question, 2, rd1, rd2, rd3, rd4);
					}
				});
				rd3.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						onChoose(question, 3, rd1, rd2, rd3, rd4);
					}
				});
				rd4.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						onChoose(question, 4, rd1, rd2, rd3, rd4);
					}
				});				
				
				holder.layoutQuestion.addView(questionView);
			}
		}
	
		
		return convertView;
	}
	
	private class ViewHolder{
		TextView txtQuestion;
		TextView txtHint;
		LinearLayout layoutQuestion;
	}
	private void onChoose(QuestionDataSet question, int rd
			, RadioButton rd1, RadioButton rd2
			, RadioButton rd3, RadioButton rd4){
		switch (rd) {
		case 1:
			rd1.setChecked(true);
			rd2.setChecked(false);
			rd3.setChecked(false);
			rd4.setChecked(false);
			question.setChoice(1);			
			break;
		case 2:
			rd1.setChecked(false);
			rd2.setChecked(true);
			rd3.setChecked(false);
			rd4.setChecked(false);
			question.setChoice(2);			
			break;
		case 3:
			rd1.setChecked(false);
			rd2.setChecked(false);
			rd3.setChecked(true);
			rd4.setChecked(false);
			question.setChoice(3);			
			break;
		case 4:
			rd1.setChecked(false);
			rd2.setChecked(false);
			rd3.setChecked(false);
			rd4.setChecked(true);
			question.setChoice(4);			
			break;			
		default:
			break;
		}	
	}
}
