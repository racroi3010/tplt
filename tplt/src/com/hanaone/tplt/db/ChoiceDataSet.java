package com.hanaone.tplt.db;

import android.os.Parcel;
import android.os.Parcelable;

public class ChoiceDataSet implements Parcelable {
	private int id;
	private int number;
	private String label;
	private String text;
	public ChoiceDataSet() {

	}	
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
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeInt(number);
		dest.writeString(label);
		dest.writeString(text);
	}
	
	public static final Parcelable.Creator<ChoiceDataSet> CREATOR
				= new Parcelable.Creator<ChoiceDataSet>() {

					@Override
					public ChoiceDataSet createFromParcel(Parcel source) {
						
						return new ChoiceDataSet(source);
					}

					@Override
					public ChoiceDataSet[] newArray(int size) {
						return new ChoiceDataSet[size];
					}
		
		
				};
	private ChoiceDataSet(Parcel in){
		id = in.readInt();
		number = in.readInt();
		label = in.readString();
		text = in.readString();
		
	}
}
