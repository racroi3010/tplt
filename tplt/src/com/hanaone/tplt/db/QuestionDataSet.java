package com.hanaone.tplt.db;

import java.util.List;

public class QuestionDataSet {
	private int id;
	private int number;
	private int mark;
	private String text;
	private List<ChoiceDataSet> choices;
	private int answer;
	private String type;
	private String choiceType;
	private String hint;
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
	public List<ChoiceDataSet> getChoices() {
		return choices;
	}
	public void setChoices(List<ChoiceDataSet> choices) {
		this.choices = choices;
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
	public String getChoiceType() {
		return choiceType;
	}
	public void setChoiceType(String choiceType) {
		this.choiceType = choiceType;
	}
	
}
