package com.hanaone.tplt.db;

import java.util.List;

public class ExamDataSet {
	private int number;
	private String date;
	private List<LevelDataSet> levels;
	
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	public List<LevelDataSet> getLevels() {
		return levels;
	}
	public void setLevels(List<LevelDataSet> levels) {
		this.levels = levels;
	}
	
}
