package com.hanaone.tplt.adapter;

import android.widget.Button;

public interface ListAdapterListener {
	public void onSelect(int questionNumber, int sectionNumber);
	public void onPlayAudioSection(final PlayingInfo playInfo, int sectionNumber);
	public void onPlayAudioQuestion(final PlayingInfo playInfo, int sectionNumber, int questionNumber);
}
