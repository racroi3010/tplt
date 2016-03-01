package com.hanaone.tplt.adapter;

import com.hanaone.tplt.QuestionSlideFragment;
import com.hanaone.tplt.db.LevelDataSet;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class QuestionSlideAdapter extends FragmentStatePagerAdapter {
	private LevelDataSet mLevel;
	
	public QuestionSlideAdapter(FragmentManager fm, LevelDataSet level) {
		super(fm);
		this.mLevel = level;
	}

	@Override
	public Fragment getItem(int arg0) {
		return QuestionSlideFragment.create(this.mLevel.getSections().get(arg0));
	}

	@Override
	public int getCount() {
		if(this.mLevel != null){
			return this.mLevel.getSections().size();
		}
		return 0;
	}

}
