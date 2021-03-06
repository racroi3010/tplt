package com.hanaone.tplt.db.sample;


public class QuestionSample{
	private int id;
	private int number;
	private int mark;
	private String text;
	private int answer;
	private String type;
	private String hint;	
	private float startAudio;
	private float endAudio;	
	private int audio;
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

	public int getAudio() {
		return audio;
	}

	public void setAudio(int audio) {
		this.audio = audio;
	}
//
//	public static abstract class QuestionEntry implements BaseColumns{
//		public static final String TABLE_NAME = "question";
//		public static final String COLUMN_NAME_NUMBER = "number";
//		public static final String COLUMN_NAME_MARK = "mark";
//		public static final String COLUMN_NAME_TEXT = "text";
//		public static final String COLUMN_NAME_ANSWER = "answer";
//		public static final String COLUMN_NAME_TYPE = "type";
//		public static final String COLUMN_NAME_CHOICE_TYPE = "choice_type";
//		public static final String COLUMN_NAME_HINT = "hint";
//		public static final String COLUMN_NAME_START_AUDIO = "start_audio";
//		public static final String COLUMN_NAME_END_AUDIO = "end_audio";
//		public static final String COLUMN_NAME_SECTION_ID = "section_id";
//	}		
}
