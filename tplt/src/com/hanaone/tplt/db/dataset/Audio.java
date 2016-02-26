package com.hanaone.tplt.db.dataset;

import android.provider.BaseColumns;

public class Audio {
	private int id;
	private String path;
	private int exam_id;
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public int getExam_id() {
		return exam_id;
	}
	public void setExam_id(int exam_id) {
		this.exam_id = exam_id;
	}
	
	public static abstract class AudioEntry implements BaseColumns{
		public static final String TABLE_NAME = "audio";
		public static final String COLUMN_NAME_PATH = "path";
		public static final String COLUMN_NAME_EXAM_ID = "exam_id";
	}	
}
