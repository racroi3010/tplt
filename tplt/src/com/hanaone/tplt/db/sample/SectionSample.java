package com.hanaone.tplt.db.sample;

import com.hanaone.tplt.db.model.Section;

import android.provider.BaseColumns;

public class SectionSample extends Section {
	private int audio;


	public int getAudio() {
		return audio;
	}
	public void setAudio(int audio) {
		this.audio = audio;
	}




//	public static abstract class SectionEntry implements BaseColumns{
//		public static final String TABLE_NAME = "section";
//		public static final String COLUMN_NAME_NUMBER = "number";
//		public static final String COLUMN_NAME_START_AUDIO = "start_audio";
//		public static final String COLUMN_NAME_END_AUDIO = "end_audio";
//		public static final String COLUMN_NAME_TEXT = "text";
//		public static final String COLUMN_NAME_HINT = "hint";
//		public static final String COLUMN_NAME_EXAM_LEVEL_ID = "exam_level_id";
//	}	
}
