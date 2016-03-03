package com.hanaone.tplt.db;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class LevelDataSet implements Parcelable {
	private int id;
	private int number;
	private String label;
	private List<SectionDataSet> sections;
	private boolean active;
	private FileDataSet txt;
	private FileDataSet audio;	
	private FileDataSet pdf;
	private int score;
	
	public LevelDataSet() {

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
		dest.writeTypedList(sections);
		dest.writeBooleanArray(new boolean[]{active});
		dest.writeParcelable(txt, 1);
		dest.writeParcelable(audio, 1);
		dest.writeParcelable(pdf, 1);
		dest.writeInt(score);
		
	}
	public static final Parcelable.Creator<LevelDataSet> CREATOR
	= new Parcelable.Creator<LevelDataSet>() {

		@Override
		public LevelDataSet createFromParcel(Parcel source) {
			
			return new LevelDataSet(source);
		}

		@Override
		public LevelDataSet[] newArray(int size) {
			return new LevelDataSet[size];
		}


	};
	private LevelDataSet(Parcel in){
	
		id = in.readInt();
		number = in.readInt();
		label = in.readString();
		sections = new ArrayList<SectionDataSet>();
		in.readTypedList(sections, SectionDataSet.CREATOR);
		
		boolean[] arr = new boolean[1];
		in.readBooleanArray(arr);
		active = arr[0];
		
		txt = in.readParcelable((ClassLoader) FileDataSet.CREATOR);
		audio = in.readParcelable((ClassLoader) FileDataSet.CREATOR);
		pdf = in.readParcelable((ClassLoader) FileDataSet.CREATOR);
		
		score = in.readInt();
	}	
	
}
