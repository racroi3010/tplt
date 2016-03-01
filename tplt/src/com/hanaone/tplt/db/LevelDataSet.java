package com.hanaone.tplt.db;

import java.util.List;

public class LevelDataSet {
	private int id;
	private int number;
	private String label;
	private List<SectionDataSet> sections;
	private boolean active;
	private FileDataSet txt;
	private FileDataSet audio;	
	private FileDataSet pdf;
	private int score;
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
	public List<SectionDataSet> getSections() {
		return sections;
	}
	public void setSections(List<SectionDataSet> sections) {
		this.sections = sections;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public FileDataSet getAudio() {
		return audio;
	}
	public void setAudio(FileDataSet audio) {
		this.audio = audio;
	}
	public FileDataSet getPdf() {
		return pdf;
	}
	public void setPdf(FileDataSet pdf) {
		this.pdf = pdf;
	}
	
	public FileDataSet getTxt() {
		return txt;
	}
	public void setTxt(FileDataSet txt) {
		this.txt = txt;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}

	
}
