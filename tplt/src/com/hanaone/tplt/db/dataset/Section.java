package com.hanaone.tplt.db.dataset;

import android.provider.BaseColumns;

public class Section {
	private int id;
	private int number;
	private float startAudio;
	private float endAudio;
	private String text;
	private int exam_level_id;
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public float getStartAudio() {
		return startAudio;
	}
	public void setStartAudio(float startAudio) {
		this.startAudio = startAudio;
	}
	public float getEndAudio() {
		return endAudio;
	}
	public void setEndAudio(float endAudio) {
		this.endAudio = endAudio;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	

	public int getExam_level_id() {
		return exam_level_id;
	}
	public void setExam_level_id(int exam_level_id) {
		this.exam_level_id = exam_level_id;
	}



	public static abstract class SectionEntry implements BaseColumns{
		public static final String TABLE_NAME = "section";
		public static final String COLUMN_NAME_NUMBER = "number";
		public static final String COLUMN_NAME_START_AUDIO = "start_audio";
		public static final String COLUMN_NAME_END_AUDIO = "end_audio";
		public static final String COLUMN_NAME_TEXT = "text";
		public static final String COLUMN_NAME_EXAM_LEVEL_ID = "exam_level_id";
	}	
}
