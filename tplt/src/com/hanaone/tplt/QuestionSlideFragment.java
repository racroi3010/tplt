package com.hanaone.tplt;


import com.hanaone.tplt.db.SectionDataSet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class QuestionSlideFragment extends Fragment {
	private static final String ARG_PAGE = "page";
	
	private SectionDataSet mSection;


	public QuestionSlideFragment(SectionDataSet section) {
		this.mSection = section;
	}
	
	public static QuestionSlideFragment create(SectionDataSet section){
		QuestionSlideFragment fragment = new QuestionSlideFragment(section);

		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.layout_question_practice_fragment, container, false);
		
		return rootView;
	}

	
	
	
}
