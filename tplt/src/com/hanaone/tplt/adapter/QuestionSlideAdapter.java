package com.hanaone.tplt.adapter;

import java.util.ArrayList;
import java.util.List;

import com.hanaone.media.AudioControllerView.MediaPlayerControl;
import com.hanaone.tplt.QuestionSlideFragment;
import com.hanaone.tplt.db.LevelDataSet;
import com.hanaone.tplt.db.SectionDataSet;

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
		List<SectionDataSet> list = new ArrayList<SectionDataSet>();
		list.add(mLevel.getSections().get(arg0));
		return QuestionSlideFragment.create((ArrayList<SectionDataSet>)list);
	}

	@Override
	public int getCount() {
		if(this.mLevel != null){
			return this.mLevel.getSections().size();
		}
		return 0;
	}

}
