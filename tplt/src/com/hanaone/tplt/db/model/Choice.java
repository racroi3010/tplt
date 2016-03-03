package com.hanaone.tplt.db.model;

import android.provider.BaseColumns;

public class Choice {
	private int id;
	private int number;
	private String label;
	private String text;
	private int question_id;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getQuestion_id() {
		return question_id;
	}
	public void setQuestion_id(int question_id) {
		this.question_id = question_id;
	}
	
	
	public static abstract class ChoiceEntry implements BaseColumns{
		public static final String TABLE_NAME = "choice";
		public static final String COLUMN_NAME_NUMBER = "number";
		public static final String COLUMN_NAME_LABEL = "label";
		public static final String COLUMN_NAME_TEXT = "text";
		public static final String COLUMN_QUESTION_ID = "question_id";
	}	
}
