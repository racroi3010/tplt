package com.hanaone.tplt.db;

import java.util.List;

public class SectionDataSet {
	private int id;
	private int number;
	private float startAudio;
	private float endAudio;
	private String text;
	private List<QuestionDataSet> questions;
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
	public List<QuestionDataSet> getQuestions() {
		return questions;
	}
	public void setQuestions(List<QuestionDataSet> questions) {
		this.questions = questions;
	}
	
}
