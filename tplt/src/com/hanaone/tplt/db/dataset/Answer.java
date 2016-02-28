package com.hanaone.tplt.db.dataset;

import android.provider.BaseColumns;

public class Answer {
	private int id;
	private int question_id;
	private int choice_id;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getQuestion_id() {
		return question_id;
	}
	public void setQuestion_id(int question_id) {
		this.question_id = question_id;
	}
	public int getChoice_id() {
		return choice_id;
	}
	public void setChoice_id(int choice_id) {
		this.choice_id = choice_id;
	}
	public static abstract class AnswerEntry implements BaseColumns{
		public static final String TABLE_NAME = "answer";
		public static final String COLUMN_NAME_QUESTION_ID = "question_id";
		public static final String COLUMN_NAME_CHOICE_ID = "choice_id";
	}	
}
