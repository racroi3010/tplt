package com.hanaone.tplt;


import java.util.ArrayList;
import java.util.List;

import com.hanaone.tplt.db.ChoiceDataSet;
import com.hanaone.tplt.db.QuestionDataSet;
import com.hanaone.tplt.db.ResultDataSet;
import com.hanaone.tplt.db.SectionDataSet;
import com.hanaone.tplt.util.ImageUtils;
import com.hanaone.tplt.util.PreferenceHandler;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

public class QuestionSlideFragment extends Fragment {
	private static final String ARG_PAGE = "page";
	private ArrayList<SectionDataSet> mSections;
	private boolean isShowHint;
	public QuestionSlideFragment() {
		
	}

//	public QuestionSlideFragment(List<SectionDataSet> sections) {
//		this.mSections = sections;
//	}
	
	public static QuestionSlideFragment create(ArrayList<SectionDataSet> sections){
		QuestionSlideFragment fragment = new QuestionSlideFragment();
		//mSections = sections;
		Bundle data = new Bundle();
		data.putParcelableArrayList(ARG_PAGE, sections);
		fragment.setArguments(data);
		
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSections = getArguments().getParcelableArrayList(ARG_PAGE);
		String mode = PreferenceHandler.getQuestionModePreference(getActivity());
		if(Constants.QUESTION_MODE_PRACTICE.equals(mode) 
				|| Constants.QUESTION_MODE_REVIEW.equals(mode)){
			isShowHint = PreferenceHandler.getHintDisplayPreference(getActivity());
		}		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup sectionsView = (ViewGroup) inflater.inflate(R.layout.layout_question_fragment, container, false);
		
		LinearLayout layoutSections = (LinearLayout) sectionsView.findViewById(R.id.layout_sections);
		
		for(SectionDataSet section: mSections){
			ViewGroup sectionView = (ViewGroup) inflater.inflate(R.layout.layout_question_section, layoutSections, false);
			TextView txtSectionQuestion = (TextView) sectionView.findViewById(R.id.txt_section_question);
			
			LinearLayout layoutQuestions = (LinearLayout) sectionView.findViewById(R.id.layout_questions);
			
			final TextView txtSectionHint = (TextView) sectionView.findViewById(R.id.txt_section_hint);
			final Button btnSectionHint = (Button) sectionView.findViewById(R.id.btn_section_hint);
			
			if(section.getHint() == null || section.getHint().isEmpty()){
				txtSectionHint.setVisibility(TextView.GONE);
				btnSectionHint.setVisibility(Button.GONE);
			} else {
				txtSectionHint.setText(section.getHint());
				btnSectionHint.setVisibility(Button.VISIBLE);
				btnSectionHint.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						if(txtSectionHint.getVisibility() == TextView.VISIBLE){
							txtSectionHint.setVisibility(TextView.GONE);
							btnSectionHint.setBackgroundResource(R.drawable.ic_image_wb_sunny_black);
						} else {
							txtSectionHint.setVisibility(TextView.VISIBLE);
							btnSectionHint.setBackgroundResource(R.drawable.ic_image_wb_sunny_cyan);
						}
					}
				});
				if(isShowHint){
					txtSectionHint.setVisibility(LinearLayout.VISIBLE);
					btnSectionHint.setBackgroundResource(R.drawable.ic_image_wb_sunny_cyan);					
				} else {
					txtSectionHint.setVisibility(LinearLayout.GONE);
					btnSectionHint.setBackgroundResource(R.drawable.ic_image_wb_sunny_black);
				}				
				
			}
			
			
			List<QuestionDataSet> questions = section.getQuestions();
			String txt = "";
			if(questions != null && !questions.isEmpty()){
				txt += " # [" + questions.get(0).getNumber() + "~" + questions.get(questions.size() - 1).getNumber() + "] ";
			}
			txt += section.getText();
			
			
			txtSectionQuestion.setText(txt);			
			
			if(questions != null){
				for(final QuestionDataSet question: questions){
					ViewGroup questionView = (ViewGroup) inflater.inflate(R.layout.layout_question_question, layoutQuestions, false);
					
					TextView txtNumber = (TextView) questionView.findViewById(R.id.txt_question_number);
					TextView txtQuestionHint = (TextView) questionView.findViewById(R.id.txt_question_hint);
					final Button btn1 = (Button) questionView.findViewById(R.id.btn_question_choice_1);
					final Button btn2 = (Button) questionView.findViewById(R.id.btn_question_choice_2);
					final Button btn3 = (Button) questionView.findViewById(R.id.btn_question_choice_3);
					final Button btn4 = (Button) questionView.findViewById(R.id.btn_question_choice_4);
					
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
					if(question.getHint() == null || question.getHint().isEmpty()){
						btnQuestionHint.setVisibility(Button.GONE);
						layoutQuestionHint.setVisibility(LinearLayout.GONE);
					} else {
						txtQuestionHint.setText(question.getHint());
						btnQuestionHint.setVisibility(Button.VISIBLE);
						if(isShowHint){
							layoutQuestionHint.setVisibility(LinearLayout.VISIBLE);
							btnQuestionHint.setBackgroundResource(R.drawable.ic_image_wb_sunny_cyan);					
						} else {
							layoutQuestionHint.setVisibility(LinearLayout.GONE);
							btnQuestionHint.setBackgroundResource(R.drawable.ic_image_wb_sunny_black);
						}
						btnQuestionHint.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								
								if(layoutQuestionHint.getVisibility() == LinearLayout.VISIBLE){
									layoutQuestionHint.setVisibility(LinearLayout.GONE);
									btnQuestionHint.setBackgroundResource(R.drawable.ic_image_wb_sunny_black);
								} else {
									layoutQuestionHint.setVisibility(LinearLayout.VISIBLE);
									btnQuestionHint.setBackgroundResource(R.drawable.ic_image_wb_sunny_cyan);
								}
							}
						});
												
					}
					
					
					txt = question.getText();
					if( txt != null && !txt.isEmpty()){
						txtQuestionTxt.setText(txt + " (" + question.getMark() + "점)");
					} else {
						txtQuestionTxt.setText("(" + question.getMark() + "점)");
					}
					
					
					txtNumber.setText(question.getNumber() + ". ");
					
					
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
									
					
					layoutQuestions.addView(questionView);
				}
			}
			
			
			layoutSections.addView(sectionView);
		}
		

		
		return sectionsView;
	}
	
	private void onChoose(QuestionDataSet question, int btn
			, Button btn1, Button btn2
			, Button btn3, Button btn4){
		Context mContext = getActivity();
		switch (btn) {
		case 1:
			btn1.setBackgroundResource(R.drawable.circle_number_black);						
			btn2.setBackgroundResource(R.drawable.circle_number_trans);
			btn3.setBackgroundResource(R.drawable.circle_number_trans);
			btn4.setBackgroundResource(R.drawable.circle_number_trans);
			
			
			btn1.setTextColor(mContext.getResources().getColor(R.color.WHITE));
			btn2.setTextColor(mContext.getResources().getColor(R.color.BLACK));
			btn3.setTextColor(mContext.getResources().getColor(R.color.BLACK));
			btn4.setTextColor(mContext.getResources().getColor(R.color.BLACK));
			question.setChoice(1);			
			break;
		case 2:
			btn1.setBackgroundResource(R.drawable.circle_number_trans);
			btn2.setBackgroundResource(R.drawable.circle_number_black);
			btn3.setBackgroundResource(R.drawable.circle_number_trans);
			btn4.setBackgroundResource(R.drawable.circle_number_trans);
			
			btn1.setTextColor(mContext.getResources().getColor(R.color.BLACK));
			btn2.setTextColor(mContext.getResources().getColor(R.color.WHITE));
			btn3.setTextColor(mContext.getResources().getColor(R.color.BLACK));
			btn4.setTextColor(mContext.getResources().getColor(R.color.BLACK));			
			question.setChoice(2);			
			break;
		case 3:
			btn1.setBackgroundResource(R.drawable.circle_number_trans);
			btn2.setBackgroundResource(R.drawable.circle_number_trans);
			btn3.setBackgroundResource(R.drawable.circle_number_black);
			btn4.setBackgroundResource(R.drawable.circle_number_trans);
			
			btn1.setTextColor(mContext.getResources().getColor(R.color.BLACK));
			btn2.setTextColor(mContext.getResources().getColor(R.color.BLACK));
			btn3.setTextColor(mContext.getResources().getColor(R.color.WHITE));
			btn4.setTextColor(mContext.getResources().getColor(R.color.BLACK));	
			question.setChoice(3);			
			break;
		case 4:
			btn1.setBackgroundResource(R.drawable.circle_number_trans);
			btn2.setBackgroundResource(R.drawable.circle_number_trans);
			btn3.setBackgroundResource(R.drawable.circle_number_trans);		
			btn4.setBackgroundResource(R.drawable.circle_number_black);
			
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
