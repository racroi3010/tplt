package com.hanaone.tplt.db.model;

import android.provider.BaseColumns;

public class Level {
	private int id;
	private int number;
	private String label;

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


	public abstract class LevelEntry implements BaseColumns{
		public static final String TABLE_NAME = "level";
		public static final String COLUMN_NAME_NUMBER = "number";
		public static final String COLUMN_NAME_LABEL = "label";	

	}
}
