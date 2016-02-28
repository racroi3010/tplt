package com.hanaone.tplt.db.dataset;

import android.provider.BaseColumns;

public class Question {
	private int id;
	private int number;
	private int mark;
	private String text;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private int section_id;

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getMark() {
		return mark;
	}

	public void setMark(int mark) {
		this.mark = mark;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getSection_id() {
		return section_id;
	}

	public void setSection_id(int section_id) {
		this.section_id = section_id;
	}
	
	public static abstract class QuestionEntry implements BaseColumns{
		public static final String TABLE_NAME = "question";
		public static final String COLUMN_NAME_NUMBER = "number";
		public static final String COLUMN_NAME_MARK = "mark";
		public static final String COLUMN_NAME_TEXT = "text";
		public static final String COLUMN_NAME_SECTION_ID = "section_id";
	}		
}
