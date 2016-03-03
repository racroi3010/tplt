package com.hanaone.tplt;


import java.util.ArrayList;
import java.util.List;

import com.hanaone.tplt.db.ChoiceDataSet;
import com.hanaone.tplt.db.QuestionDataSet;
import com.hanaone.tplt.db.SectionDataSet;
import com.hanaone.tplt.util.ImageUtils;

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
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup sectionsView = (ViewGroup) inflater.inflate(R.layout.layout_question_fragment, container, false);
		
		LinearLayout layoutSections = (LinearLayout) sectionsView.findViewById(R.id.layout_sections);
		
		for(SectionDataSet section: mSections){
			ViewGroup sectionView = (ViewGroup) inflater.inflate(R.layout.layout_question_section, layoutSections, false);
			TextView txtSectionQuestion = (TextView) sectionView.findViewById(R.id.txt_section_question);
			TextView txtSectionHint = (TextView) sectionView.findViewById(R.id.txt_section_hint);
			LinearLayout layoutQuestions = (LinearLayout) sectionView.findViewById(R.id.layout_questions);
			
			txtSectionQuestion.setText(section.getText());
			txtSectionHint.setText(section.getHint());
			
			List<QuestionDataSet> questions = section.getQuestions();
			if(questions != null){
				for(final QuestionDataSet question: questions){
					ViewGroup questionView = (ViewGroup) inflater.inflate(R.layout.layout_question_question, layoutQuestions, false);
					
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
					
					txtQuestionTxt.setText(" 2점");
					
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
					
					layoutQuestions.addView(questionView);
				}
			}
			layoutSections.addView(sectionView);
		}
		

		
		return sectionsView;
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
