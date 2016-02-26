package com.hanaone.tplt.db;

import java.util.List;

public class LevelDataSet {
	private int id;
	private int number;
	private String label;
	private List<SectionDataSet> sections;
	private AudioDataSet audio;
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
	public AudioDataSet getAudio() {
		return audio;
	}
	public void setAudio(AudioDataSet audio) {
		this.audio = audio;
	}
	
}
