package com.hanaone.tplt.db.model;

import android.provider.BaseColumns;

public class FileExtra {
	private int id;
	private String type;
	private String name;
	private String path;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public abstract class FileExtraEntry implements BaseColumns{
		public static final String TABLE_NAME = "fileextra";
		public static final String COLUMN_NAME_FILE_TYPE = "filetype";
		public static final String COLUMN_NAME_FILE_NAME = "filename";
		public static final String COLUMN_NAME_FILE_PATH = "path";
	}		
}
