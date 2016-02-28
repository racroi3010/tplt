package com.hanaone.tplt.util;

import java.util.ArrayList;
import java.util.List;

import com.hanaone.tplt.db.ExamDataSet;
import com.hanaone.tplt.db.FileDataSet;
import com.hanaone.tplt.db.LevelDataSet;
import com.hanaone.tplt.db.dataset.Examination;
import com.hanaone.tplt.db.dataset.FileExtra;
import com.hanaone.tplt.db.dataset.Level;

public class DatabaseUtils {
	public static List<ExamDataSet> examModels2Pojos(List<Examination> list){
		List<ExamDataSet> rs = new ArrayList<ExamDataSet>();
		for(Examination exam: list){
			ExamDataSet data = new ExamDataSet();
			data.setNumber(exam.getNumber());
			data.setDate(exam.getDate());
		}
		
		return rs;
	}
	public static Examination examPojo2Model(ExamDataSet exam){
		if(exam == null) return null;
		Examination data = new Examination();
		data.setNumber(exam.getNumber());
		data.setDate(exam.getDate());
		
		return data;
	}
	public static ExamDataSet examModel2Pojo(Examination exam){
		if(exam == null) return null;
		ExamDataSet data = new ExamDataSet();
		data.setNumber(exam.getNumber());
		data.setDate(exam.getDate());
		return data;
	}
	
	public static FileDataSet fileModel2Pojo(FileExtra file){
		if(file == null) return null;
		FileDataSet data = new FileDataSet();
		data.setId(file.getId());
		data.setName(file.getName());
		data.setType(file.getType());
		data.setPath(file.getPath());
		
		return data;
	}
	public static LevelDataSet levelModel2Pojo(Level level){
		if(level == null) return null;
		LevelDataSet data = new LevelDataSet();
		data.setId(level.getId());
		
		return data;
	}
	public static Level levelPojo2Model(LevelDataSet level){
		if(level == null) return null;
		
		Level data = new Level();
		data.setId(level.getId());
		data.setLabel(level.getLabel());
		data.setNumber(level.getNumber());
		
		return data;
	}
}
