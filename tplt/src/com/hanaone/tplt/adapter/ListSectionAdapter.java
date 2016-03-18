package com.hanaone.tplt.adapter;

import java.util.ArrayList;
import java.util.List;

import com.hanaone.tplt.Constants;
import com.hanaone.tplt.R;
import com.hanaone.tplt.db.ChoiceDataSet;
import com.hanaone.tplt.db.QuestionDataSet;
import com.hanaone.tplt.db.ResultDataSet;
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
	private ArrayList<ResultDataSet> mResults;
	
	public ListSectionAdapter(Context mContext, ListAdapterListener mListener) {
		this.mContext = mContext;
		this.mListener = mListener;
		this.mInflater = LayoutInflater.from(mContext);
	}

	public void setmDataSet(List<SectionDataSet> mDataSet) {
		this.mDataSet = mDataSet;
		this.notifyDataSetChanged();
	}
	public void setResults(ArrayList<ResultDataSet> results){
		this.mResults = results;
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
			for(int i = 0; i < questions.size(); i ++){
				final QuestionDataSet question = questions.get(i);
				LinearLayout questionView = (LinearLayout) mInflater.inflate(R.layout.layout_question_question, holder.layoutQuestion, false);
				
				TextView txtNumber = (TextView) questionView.findViewById(R.id.txt_question_number);
				TextView txtQuestionHint = (TextView) questionView.findViewById(R.id.txt_question_hint);
				
				final Button btn1 = (Button) questionView.findViewById(R.id.btn_question_choice_1);
				final Button btn2 = (Button) questionView.findViewById(R.id.btn_question_choice_2);
				final Button btn3 = (Button) questionView.findViewById(R.id.btn_question_choice_3);
				final Button btn4 = (Button) questionView.findViewById(R.id.btn_question_choice_4);
				
				btn1.setBackgroundResource(R.drawable.num_trans);
				btn2.setBackgroundResource(R.drawable.num_trans);
				btn3.setBackgroundResource(R.drawable.num_trans);
				btn4.setBackgroundResource(R.drawable.num_trans);						
				
				final TextView txt1 = (TextView) questionView.findViewById(R.id.txt_question_choice_1);
				final TextView txt2 = (TextView) questionView.findViewById(R.id.txt_question_choice_2);
				final TextView txt3 = (TextView) questionView.findViewById(R.id.txt_question_choice_3);
				final TextView txt4 = (TextView) questionView.findViewById(R.id.txt_question_choice_4);	
				
				TextView txtQuestionTxt = (TextView) questionView.findViewById(R.id.txt_question_txt);
				ImageView imgQuestion = (ImageView) questionView.findViewById(R.id.img_question);
				
				ImageView img1 = (ImageView) questionView.findViewById(R.id.img_question_choice_1);
				ImageView img2 = (ImageView) questionView.findViewById(R.id.img_question_choice_2);
				ImageView img3 = (ImageView) questionView.findViewById(R.id.img_question_choice_3);
				ImageView img4 = (ImageView) questionView.findViewById(R.id.img_question_choice_4);
				
				
				final Button btnQuestionHint = (Button) questionView.findViewById(R.id.btn_question_hint);
				final LinearLayout layoutQuestionHint = (LinearLayout) questionView.findViewById(R.id.layout_question_hint);
				btnQuestionHint.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						if(layoutQuestionHint.getVisibility() == LinearLayout.VISIBLE){
							layoutQuestionHint.setVisibility(LinearLayout.GONE);
							btnQuestionHint.setBackgroundResource(R.drawable.ic_wb_sunny_black_24dp);
						} else {
							layoutQuestionHint.setVisibility(LinearLayout.VISIBLE);
							btnQuestionHint.setBackgroundResource(R.drawable.hint_cyan);
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
						txt1.setVisibility(TextView.GONE);
						txt2.setVisibility(TextView.GONE);
						txt3.setVisibility(TextView.GONE);
						txt4.setVisibility(TextView.GONE);

						
						img1.setImageBitmap(ImageUtils.decodeSampledBitmapFromFile(choices.get(0).getText(), 200, 200));
						img2.setImageBitmap(ImageUtils.decodeSampledBitmapFromFile(choices.get(1).getText(), 200, 200));
						img3.setImageBitmap(ImageUtils.decodeSampledBitmapFromFile(choices.get(2).getText(), 200, 200));
						img4.setImageBitmap(ImageUtils.decodeSampledBitmapFromFile(choices.get(3).getText(), 200, 200));
						
						img1.setVisibility(ImageView.VISIBLE);
						img2.setVisibility(ImageView.VISIBLE);
						img3.setVisibility(ImageView.VISIBLE);
						img4.setVisibility(ImageView.VISIBLE);
					} else {
						txt1.setVisibility(TextView.VISIBLE);
						txt2.setVisibility(TextView.VISIBLE);
						txt3.setVisibility(TextView.VISIBLE);
						txt4.setVisibility(TextView.VISIBLE);	
						
						txt1.setText(choices.get(0).getText());
						txt2.setText(choices.get(1).getText());
						txt3.setText(choices.get(2).getText());
						txt4.setText(choices.get(3).getText());
						
						img1.setVisibility(ImageView.GONE);
						img2.setVisibility(ImageView.GONE);
						img3.setVisibility(ImageView.GONE);
						img4.setVisibility(ImageView.GONE);							
					}
					
				}
				btn1.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						onChoose(question, 1, btn1, btn2, btn3, btn4);
					}
				});
				btn2.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						onChoose(question, 2, btn1, btn2, btn3, btn4);
					}
				});
				btn3.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						onChoose(question, 3, btn1, btn2, btn3, btn4);
					}
				});
				btn4.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						onChoose(question, 4, btn1, btn2, btn3, btn4);
					}
				});				
					
		
				// check choice
				Button btn = null;
				switch (question.getChoice()) {
				
				case 1:
					btn = btn1;
					break;
				case 2:
					btn = btn2;
					break;
				case 3:
					btn = btn3;
					break;		
				case 4:
					btn = btn4;
					break;					
				default:
					break;
				}
				if(btn != null){
					btn.setTextColor(mContext.getResources().getColor(R.color.WHITE));
					btn.setBackgroundResource(R.drawable.num_black);
				}
				
				// check result
				
				if(mResults != null){

					
					ResultDataSet result = mResults.get(i);

					
					Button rBtn = null;
					switch (result.getAnswer()) {
					case 1:
						rBtn = btn1;
						break;
					case 2:
						rBtn = btn2;
						break;
					case 3:
						rBtn = btn3;
						break;	
					case 4:
						rBtn = btn4;
						break;								
					default:
						break;
					}
					if(rBtn != null){
						rBtn.setBackgroundResource(R.drawable.num_green);
					}			
					
					rBtn = null;
					switch (result.getChoice()) {
					case 1:
						rBtn = btn1;
						break;
					case 2:
						rBtn = btn2;
						break;
					case 3:
						rBtn = btn3;
						break;	
					case 4:
						rBtn = btn4;
						break;								
					default:
						break;
					}
					if(rBtn != null){
						if(result.getChoice() != result.getAnswer()){
							rBtn.setBackgroundResource(R.drawable.num_red);
						}
						
					}										
				
					
		
				}
				//	
				
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
	private void onChoose(QuestionDataSet question, int btn
			, Button btn1, Button btn2
			, Button btn3, Button btn4){
		question.setChoice(btn);
		switch (btn) {
		case 1:
			btn1.setBackgroundResource(R.drawable.num_black);						
			btn2.setBackgroundResource(R.drawable.num_trans);
			btn3.setBackgroundResource(R.drawable.num_trans);
			btn4.setBackgroundResource(R.drawable.num_trans);
			
			
			btn1.setTextColor(mContext.getResources().getColor(R.color.WHITE));
			btn2.setTextColor(mContext.getResources().getColor(R.color.BLACK));
			btn3.setTextColor(mContext.getResources().getColor(R.color.BLACK));
			btn4.setTextColor(mContext.getResources().getColor(R.color.BLACK));
			question.setChoice(1);			
			break;
		case 2:
			btn1.setBackgroundResource(R.drawable.num_trans);
			btn2.setBackgroundResource(R.drawable.num_black);
			btn3.setBackgroundResource(R.drawable.num_trans);
			btn4.setBackgroundResource(R.drawable.num_trans);
			
			btn1.setTextColor(mContext.getResources().getColor(R.color.BLACK));
			btn2.setTextColor(mContext.getResources().getColor(R.color.WHITE));
			btn3.setTextColor(mContext.getResources().getColor(R.color.BLACK));
			btn4.setTextColor(mContext.getResources().getColor(R.color.BLACK));			
			question.setChoice(2);			
			break;
		case 3:
			btn1.setBackgroundResource(R.drawable.num_trans);
			btn2.setBackgroundResource(R.drawable.num_trans);
			btn3.setBackgroundResource(R.drawable.num_black);
			btn4.setBackgroundResource(R.drawable.num_trans);
			
			btn1.setTextColor(mContext.getResources().getColor(R.color.BLACK));
			btn2.setTextColor(mContext.getResources().getColor(R.color.BLACK));
			btn3.setTextColor(mContext.getResources().getColor(R.color.WHITE));
			btn4.setTextColor(mContext.getResources().getColor(R.color.BLACK));	
			question.setChoice(3);			
			break;
		case 4:
			btn1.setBackgroundResource(R.drawable.num_trans);
			btn2.setBackgroundResource(R.drawable.num_trans);
			btn3.setBackgroundResource(R.drawable.num_trans);		
			btn4.setBackgroundResource(R.drawable.num_black);
			
			btn1.setTextColor(mContext.getResources().getColor(R.color.BLACK));
			btn2.setTextColor(mContext.getResources().getColor(R.color.BLACK));
			btn3.setTextColor(mContext.getResources().getColor(R.color.BLACK));
			btn4.setTextColor(mContext.getResources().getColor(R.color.WHITE));	
			question.setChoice(4);			
			break;			
		default:
			break;
		}	
	}
}
