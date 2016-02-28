package com.hanaone.tplt.db.dataset;

import android.provider.BaseColumns;

public class Examination {
	private int number;
	private String date;
	
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
	
	public static abstract class ExamEntry implements BaseColumns{
		public static final String TABLE_NAME = "examination";
		public static final String COLUMN_NAME_NUMBER = "number";
		public static final String COLUMN_NAME_DATE = "date";
	}
}
