package com.hanaone.tplt.db;

import java.util.List;

import com.hanaone.tplt.db.dataset.Section;

public class ExamDataSet {
	private int id;
	private int number;
	private String date;
	private List<LevelDataSet> levels;
	
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
