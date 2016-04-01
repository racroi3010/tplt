package com.hanaone.tplt.db.model;

import android.provider.BaseColumns;

public class Question {
	protected int id;
	protected int number;
	protected int mark;
	protected String text;
	protected int answer;
	protected String type;
	protected String hint;	
	protected float startAudio;
	protected float endAudio;	
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
	
	public int getAnswer() {
		return answer;
	}

	public void setAnswer(int answer) {
		this.answer = answer;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
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

	public static abstract class QuestionEntry implements BaseColumns{
		public static final String TABLE_NAME = "question";
		public static final String COLUMN_NAME_NUMBER = "number";
		public static final String COLUMN_NAME_MARK = "mark";
		public static final String COLUMN_NAME_TEXT = "text";
		public static final String COLUMN_NAME_ANSWER = "answer";
		public static final String COLUMN_NAME_TYPE = "type";
		public static final String COLUMN_NAME_HINT = "hint";
		public static final String COLUMN_NAME_START_AUDIO = "start_audio";
		public static final String COLUMN_NAME_END_AUDIO = "end_audio";
		public static final String COLUMN_NAME_SECTION_ID = "section_id";
	}		
}
