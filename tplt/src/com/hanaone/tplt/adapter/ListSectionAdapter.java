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
import com.hanaone.tplt.util.PreferenceHandler;

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
	private List<Item> mItems;
	//private ArrayList<ResultDataSet> mResults;
	private boolean isShowHint;
	private boolean isShowAudio;
	public ListSectionAdapter(Context mContext, ListAdapterListener mListener) {
		this.mContext = mContext;
		this.mListener = mListener;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		String mode = PreferenceHandler.getQuestionModePreference(mContext);
		if(Constants.QUESTION_MODE_PRACTICE.equals(mode) 
				|| Constants.QUESTION_MODE_REVIEW.equals(mode)){
			isShowHint = PreferenceHandler.getHintDisplayPreference(mContext);
			
		}
		
		if(Constants.QUESTION_MODE_REVIEW.equals(mode)){
			isShowAudio = true;
		}
	}

	public void setmDataSet(List<SectionDataSet> mDataSet) {
		this.mDataSet = mDataSet;
		
		mItems = new ArrayList<ListSectionAdapter.Item>();
		for(int i = 0; i < this.mDataSet.size(); i ++){
			SectionDataSet section = this.mDataSet.get(i);
			mItems.add(new SectionItem(section, i));
			for(int j = 0; j < section.getQuestions().size(); j ++){
				mItems.add(new QuestionItem(section, section.getQuestions().get(j), i, j));
			}
		}
		
		this.notifyDataSetChanged();
	}
//	public void setResults(ArrayList<ResultDataSet> results){
//		this.mResults = results;
//	}
	@Override
	public int getCount() {
		if(mItems != null){
			return mItems.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if(mItems != null && mItems.size() > position){
			return mItems.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		convertView = mItems.get(position).getView(mContext, mInflater, convertView, parent);
		
		return convertView;
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
	
	public interface Item{
		public int getViewType();
		public View getView(Context context, LayoutInflater inflater, View convertView, ViewGroup parent);		
	}
	public class SectionItem implements Item{
		private SectionDataSet section;
		private int sectionNumber;
		public SectionItem(SectionDataSet section, int sectionNumber) {
			this.section = section;
			this.sectionNumber = sectionNumber;
		}

		@Override
		public int getViewType() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(Context context, LayoutInflater inflater,
				View convertView, ViewGroup parent) {
			final SectionViewHolder holder;
			if(convertView == null || !(convertView.getTag() instanceof SectionViewHolder)){
				convertView = inflater.inflate(R.layout.layout_question_section, parent, false);
				
				holder = new SectionViewHolder();
				holder.txtQuestion = (TextView) convertView.findViewById(R.id.txt_section_question);
				holder.txtHint = (TextView) convertView.findViewById(R.id.txt_section_hint);
				holder.btnHint = (Button) convertView.findViewById(R.id.btn_section_hint);

				holder.btnAudio = (Button) convertView.findViewById(R.id.btn_section_audio);
				
				convertView.setTag(holder);
			} else {
				holder = (SectionViewHolder) convertView.getTag();
			}
			
			// fill data
			if(section.getHint() == null || section.getHint().isEmpty()){
				holder.txtHint.setVisibility(TextView.GONE);
				holder.btnHint.setVisibility(Button.GONE);
				holder.btnAudio.setVisibility(Button.GONE);
			} else {
				holder.txtHint.setText(section.getHint());
				holder.btnHint.setVisibility(Button.VISIBLE);
				holder.btnHint.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						if(holder.txtHint.getVisibility() == TextView.VISIBLE){
							holder.txtHint.setVisibility(TextView.GONE);
							holder.btnHint.setBackgroundResource(R.drawable.ic_image_wb_sunny_black);
						} else {
							holder.txtHint.setVisibility(TextView.VISIBLE);
							holder.btnHint.setBackgroundResource(R.drawable.ic_image_wb_sunny_cyan);
						}
					}
				});
				if(isShowHint){
					holder.txtHint.setVisibility(LinearLayout.VISIBLE);
					holder.btnHint.setBackgroundResource(R.drawable.ic_image_wb_sunny_cyan);					
				} else {
					holder.txtHint.setVisibility(LinearLayout.GONE);
					holder.btnHint.setBackgroundResource(R.drawable.ic_image_wb_sunny_black);
				}				
				
				
				if(isShowAudio){
					holder.btnAudio.setVisibility(Button.VISIBLE);
					//final int pos = position;
					holder.btnAudio.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							mListener.onPlayAudioSection(holder.btnAudio, sectionNumber);
						}
					});						
				}			
			}		
			List<QuestionDataSet> questions = section.getQuestions();

			String txt = "";
			if(questions != null && questions.size() > 1){
				txt += " # [" + questions.get(0).getNumber() + "~" + questions.get(questions.size() - 1).getNumber() + "] ";
			} else {
				txt += " # [" + questions.get(0).getNumber() + "]";
			}
			txt += section.getText();	
			holder.txtQuestion.setText(txt);			
			
			
			return convertView;
		}
		
		private class SectionViewHolder{
			TextView txtQuestion;
			Button btnHint;
			Button btnAudio;
			TextView txtHint;			
		}
	}
	public class QuestionItem implements Item{
		private SectionDataSet section;
		private QuestionDataSet question;
		private int sectionNumber;
		private int questionNumber;
		public QuestionItem(SectionDataSet section, QuestionDataSet question, int sectionNumber, int questionNumber) {
			this.question = question;
			this.section = section;
			this.sectionNumber = sectionNumber;
			this.questionNumber = questionNumber;
		}

		@Override
		public int getViewType() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(Context context, LayoutInflater inflater,
				View convertView, ViewGroup parent) {
			final QuestionViewHolder holder;
			if(convertView == null || !(convertView.getTag() instanceof QuestionViewHolder)){
				convertView = inflater.inflate(R.layout.layout_question_question, parent, false);
				
				holder = new QuestionViewHolder();
				
				
				holder.txtNumber = (TextView) convertView.findViewById(R.id.txt_question_number);
				holder.txtHint = (TextView) convertView.findViewById(R.id.txt_question_hint);
				
				holder.btnChoice1 = (Button) convertView.findViewById(R.id.btn_question_choice_1);
				holder.btnChoice2 = (Button) convertView.findViewById(R.id.btn_question_choice_2);
				holder.btnChoice3 = (Button) convertView.findViewById(R.id.btn_question_choice_3);
				holder.btnChoice4 = (Button) convertView.findViewById(R.id.btn_question_choice_4);
				
					
				
				holder.txtChoice1 = (TextView) convertView.findViewById(R.id.txt_question_choice_1);
				holder.txtChoice2 = (TextView) convertView.findViewById(R.id.txt_question_choice_2);
				holder.txtChoice3 = (TextView) convertView.findViewById(R.id.txt_question_choice_3);
				holder.txtChoice4 = (TextView) convertView.findViewById(R.id.txt_question_choice_4);	
				
				holder.txtQuestion = (TextView) convertView.findViewById(R.id.txt_question_txt);
				holder.imgQuestion = (ImageView) convertView.findViewById(R.id.img_question);
				
				holder.imgChoice1 = (ImageView) convertView.findViewById(R.id.img_question_choice_1);
				holder.imgChoice2 = (ImageView) convertView.findViewById(R.id.img_question_choice_2);
				holder.imgChoice3 = (ImageView) convertView.findViewById(R.id.img_question_choice_3);
				holder.imgChoice4 = (ImageView) convertView.findViewById(R.id.img_question_choice_4);
				
				
				holder.btnHint = (Button) convertView.findViewById(R.id.btn_question_hint);
				holder.layoutHint = (LinearLayout) convertView.findViewById(R.id.layout_question_hint);
				
				holder.btnAudio = (Button) convertView.findViewById(R.id.btn_question_audio);
								
			} else {
				holder = (QuestionViewHolder) convertView.getTag();
			}
			
			holder.btnChoice1.setBackgroundResource(R.drawable.num_trans);
			holder.btnChoice2.setBackgroundResource(R.drawable.num_trans);
			holder.btnChoice3.setBackgroundResource(R.drawable.num_trans);
			holder.btnChoice4.setBackgroundResource(R.drawable.num_trans);	
			
			if(question.getHint() == null || question.getHint().isEmpty()){
				holder.btnHint.setVisibility(Button.GONE);
				holder.btnAudio.setVisibility(Button.GONE);
				holder.layoutHint.setVisibility(LinearLayout.GONE);
			} else {
				holder.txtHint.setText(question.getHint());
				holder.btnHint.setVisibility(Button.VISIBLE);
				
				if(isShowHint){
					holder.layoutHint.setVisibility(LinearLayout.VISIBLE);
					holder.btnHint.setBackgroundResource(R.drawable.ic_image_wb_sunny_cyan);	
					
				} else {
					holder.layoutHint.setVisibility(LinearLayout.GONE);
					holder.btnHint.setBackgroundResource(R.drawable.ic_image_wb_sunny_black);
				}
				holder.btnHint.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						if(holder.layoutHint.getVisibility() == LinearLayout.VISIBLE){
							holder.layoutHint.setVisibility(LinearLayout.GONE);
							holder.btnHint.setBackgroundResource(R.drawable.ic_image_wb_sunny_black);
						} else {
							holder.layoutHint.setVisibility(LinearLayout.VISIBLE);
							holder.btnHint.setBackgroundResource(R.drawable.ic_image_wb_sunny_cyan);
						}
					}
				});
				
				if(isShowAudio){
					holder.btnAudio.setVisibility(Button.VISIBLE);
//					final int pos = position;
//					final int ipos = i;
					holder.btnAudio.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							mListener.onPlayAudioQuestion(holder.btnAudio, sectionNumber, questionNumber);
						}
					});						
				}

										
			}
			
			String questionTxt = question.getText(); 
			if(questionTxt != null && !questionTxt.isEmpty()){
				holder.txtQuestion.setText(questionTxt + " (" + question.getMark() + "점)");
			} else {
				holder.txtQuestion.setText("(" + question.getMark() + "점)");
			}
			
			
			holder.txtNumber.setText(question.getNumber() + ". ");
			
			
			List<ChoiceDataSet> choices = question.getChoices();
			if(choices != null){
				if(Constants.FILE_TYPE_IMG.equals(question.getChoiceType())){
					holder.txtChoice1.setVisibility(TextView.GONE);
					holder.txtChoice2.setVisibility(TextView.GONE);
					holder.txtChoice3.setVisibility(TextView.GONE);
					holder.txtChoice4.setVisibility(TextView.GONE);

					
					holder.imgChoice1.setImageBitmap(ImageUtils.decodeSampledBitmapFromFile(choices.get(0).getText(), 200, 200));
					holder.imgChoice2.setImageBitmap(ImageUtils.decodeSampledBitmapFromFile(choices.get(1).getText(), 200, 200));
					holder.imgChoice3.setImageBitmap(ImageUtils.decodeSampledBitmapFromFile(choices.get(2).getText(), 200, 200));
					holder.imgChoice4.setImageBitmap(ImageUtils.decodeSampledBitmapFromFile(choices.get(3).getText(), 200, 200));
					
					holder.imgChoice1.setVisibility(ImageView.VISIBLE);
					holder.imgChoice2.setVisibility(ImageView.VISIBLE);
					holder.imgChoice3.setVisibility(ImageView.VISIBLE);
					holder.imgChoice4.setVisibility(ImageView.VISIBLE);
				} else {
					holder.txtChoice1.setVisibility(TextView.VISIBLE);
					holder.txtChoice2.setVisibility(TextView.VISIBLE);
					holder.txtChoice3.setVisibility(TextView.VISIBLE);
					holder.txtChoice4.setVisibility(TextView.VISIBLE);	
					
					holder.txtChoice1.setText(choices.get(0).getText());
					holder.txtChoice2.setText(choices.get(1).getText());
					holder.txtChoice3.setText(choices.get(2).getText());
					holder.txtChoice4.setText(choices.get(3).getText());
					
					holder.imgChoice1.setVisibility(ImageView.GONE);
					holder.imgChoice2.setVisibility(ImageView.GONE);
					holder.imgChoice3.setVisibility(ImageView.GONE);
					holder.imgChoice4.setVisibility(ImageView.GONE);							
				}
				
			}
			holder.btnChoice1.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					onChoose(question, 1, holder.btnChoice1, holder.btnChoice2, holder.btnChoice3, holder.btnChoice4);
				}
			});
			holder.btnChoice2.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					onChoose(question, 2, holder.btnChoice1, holder.btnChoice2, holder.btnChoice3, holder.btnChoice4);
				}
			});
			holder.btnChoice3.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					onChoose(question, 3, holder.btnChoice1, holder.btnChoice2, holder.btnChoice3, holder.btnChoice4);
				}
			});
			holder.btnChoice4.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					onChoose(question, 4, holder.btnChoice1, holder.btnChoice2, holder.btnChoice3, holder.btnChoice4);
				}
			});				
				
	
			// check choice
			Button btn = null;
			switch (question.getChoice()) {
			
			case 1:
				btn = holder.btnChoice1;
				break;
			case 2:
				btn = holder.btnChoice2;
				break;
			case 3:
				btn = holder.btnChoice3;
				break;		
			case 4:
				btn = holder.btnChoice4;
				break;					
			default:
				break;
			}
			if(btn != null){
				btn.setTextColor(mContext.getResources().getColor(R.color.WHITE));
				btn.setBackgroundResource(R.drawable.num_black);
			}
			
			// check result
			
			if(Constants.QUESTION_MODE_REVIEW.equals(PreferenceHandler.getQuestionModePreference(mContext)) ){

				
				//ResultDataSet result = mResults.get(question.getNumber() - 1);
				
				Button rBtn = null;
				switch (question.getAnswer()) {
				case 1:
					btn = holder.btnChoice1;
					break;
				case 2:
					btn = holder.btnChoice2;
					break;
				case 3:
					btn = holder.btnChoice3;
					break;		
				case 4:
					btn = holder.btnChoice4;
					break;								
				default:
					break;
				}
				if(rBtn != null){
					rBtn.setBackgroundResource(R.drawable.num_green);
				}			
				
				rBtn = null;
				switch (question.getChoice()) {
				case 1:
					btn = holder.btnChoice1;
					break;
				case 2:
					btn = holder.btnChoice2;
					break;
				case 3:
					btn = holder.btnChoice3;
					break;		
				case 4:
					btn = holder.btnChoice4;
					break;							
				default:
					break;
				}
				if(rBtn != null){
					if(question.getChoice() != question.getAnswer()){
						rBtn.setBackgroundResource(R.drawable.num_red);
					}
					
				}														
					
			}			
			return convertView;
		}
		private class QuestionViewHolder{
			TextView txtNumber;
			TextView txtQuestion;
			ImageView imgQuestion;
			Button btnHint;
			Button btnAudio;
			LinearLayout layoutHint;
			TextView txtHint;	
			Button btnChoice1;
			TextView txtChoice1;
			ImageView imgChoice1;
			Button btnChoice2;
			TextView txtChoice2;
			ImageView imgChoice2;
			Button btnChoice3;
			TextView txtChoice3;
			ImageView imgChoice3;
			Button btnChoice4;
			TextView txtChoice4;
			ImageView imgChoice4;			
		}
	}
}
