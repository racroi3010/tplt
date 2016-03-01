package com.hanaone.tplt.util;

import java.util.ArrayList;
import java.util.List;

import com.hanaone.tplt.db.ChoiceDataSet;
import com.hanaone.tplt.db.ExamDataSet;
import com.hanaone.tplt.db.FileDataSet;
import com.hanaone.tplt.db.LevelDataSet;
import com.hanaone.tplt.db.QuestionDataSet;
import com.hanaone.tplt.db.SectionDataSet;
import com.hanaone.tplt.db.dataset.Choice;
import com.hanaone.tplt.db.dataset.Examination;
import com.hanaone.tplt.db.dataset.FileExtra;
import com.hanaone.tplt.db.dataset.Level;
import com.hanaone.tplt.db.dataset.Question;
import com.hanaone.tplt.db.dataset.Section;

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
	public static FileExtra filePojo2Model(FileDataSet file){
		if(file == null) return null;
		FileExtra data = new FileExtra();
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
	
	public static SectionDataSet sectionModel2Pojo(Section data){
		if(data == null) return null;
		SectionDataSet section = new SectionDataSet();
		section.setId(data.getId());
		section.setNumber(data.getNumber());
		section.setStartAudio(data.getStartAudio());
		section.setEndAudio(data.getEndAudio());
		section.setText(data.getText());
		return section;
	}
	public static Section sectionPojo2Model(SectionDataSet data){
		if(data == null) return null;
		Section section = new Section();
		section.setId(data.getId());
		section.setNumber(data.getNumber());
		section.setStartAudio(data.getStartAudio());
		section.setEndAudio(data.getEndAudio());
		section.setText(data.getText());
		return section;
	}
	
	public static Question questionPojo2Model(QuestionDataSet data){
		if(data == null) return null;
		Question question = new Question();
		question.setId(data.getId());
		question.setMark(data.getMark());
		question.setNumber(data.getNumber());
		question.setText(data.getText());
		
		return question;
	}
	public static QuestionDataSet questionModel2Pojo(Question data){
		if(data == null) return null;
		QuestionDataSet question = new QuestionDataSet();
		question.setId(data.getId());
		question.setAnswer(-1);
		question.setMark(data.getMark());
		question.setNumber(data.getNumber());
		question.setText(data.getText());
		
		
		return question;
	}
	public static Choice choicePojo2Model(ChoiceDataSet data){
		if(data == null) return null;
		
		Choice choice = new Choice();
		choice.setId(data.getId());
		choice.setLabel(data.getLabel());
		choice.setNumber(data.getNumber());
		choice.setText(data.getText());
		
		return choice;
	}
	public static ChoiceDataSet choiceModel2Pojo(Choice data){
		if(data == null) return null;
		
		ChoiceDataSet choice = new ChoiceDataSet();
		choice.setId(data.getId());
		choice.setLabel(data.getLabel());
		choice.setNumber(data.getNumber());
		choice.setText(data.getText());
		
		return choice;
	}
}
