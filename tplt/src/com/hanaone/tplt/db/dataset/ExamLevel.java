package com.hanaone.tplt.db.dataset;

import android.provider.BaseColumns;

public class ExamLevel {
	private int id;
	private int exam_id;
	private int level_id;
	private int audio_id;
	public int getExam_id() {
		return exam_id;
	}
	public void setExam_id(int exam_id) {
		this.exam_id = exam_id;
	}
	public int getLevel_id() {
		return level_id;
	}
	public void setLevel_id(int level_id) {
		this.level_id = level_id;
	}
	
	public int getAudio_id() {
		return audio_id;
	}
	public void setAudio_id(int audio_id) {
		this.audio_id = audio_id;
	}

	public abstract class ExamLevelEntry implements BaseColumns{
		public static final String TABLE_NAME = "examlevel";
		public static final String COLUMN_NAME_EXAM_ID = "exam_id";
		public static final String COLUMN_NAME_LEVEL_ID = "level_id";	
		public static final String COLUMN_NAME_AUDIO_ID = "audio_id";
	}	
}
