package com.hanaone.tplt.db;

import android.os.Parcel;
import android.os.Parcelable;

public class FileDataSet implements Parcelable {
	private int id;
	private String type;
	private String name;
	private String path;
	public FileDataSet() {

	}		
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(type);
		dest.writeString(name);
		dest.writeString(path);
		
	}
	public static final Parcelable.Creator<FileDataSet> CREATOR
	= new Parcelable.Creator<FileDataSet>() {

		@Override
		public FileDataSet createFromParcel(Parcel source) {
			
			return new FileDataSet(source);
		}

		@Override
		public FileDataSet[] newArray(int size) {
			return new FileDataSet[size];
		}


	};
	private FileDataSet(Parcel in){
		id = in.readInt();
		type = in.readString();
		name = in.readString();
		path = in.readString();
	}	
	
}
