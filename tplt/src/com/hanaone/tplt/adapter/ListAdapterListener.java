package com.hanaone.tplt.adapter;

import android.widget.Button;

public interface ListAdapterListener {
	public void onSelect(int number);
	public void onPlayAudioSection(final Button audioButton, int sectionNumber);
	public void onPlayAudioQuestion(final Button audioButton, int sectionNumber, int questionNumber);
}
