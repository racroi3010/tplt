package com.hanaone.gg;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.util.JsonReader;

import com.hanaone.tplt.db.ChoiceDataSet;
import com.hanaone.tplt.db.ExamDataSet;
import com.hanaone.tplt.db.FileDataSet;
import com.hanaone.tplt.db.LevelDataSet;
import com.hanaone.tplt.db.QuestionDataSet;
import com.hanaone.tplt.db.SectionDataSet;

public class JsonReaderHelper {
	public static List<ExamDataSet> readExams(File file) throws IOException{
		FileInputStream is = new FileInputStream(file);
		JsonReader reader = new JsonReader(new InputStreamReader(is, "UTF-8"));
		try {
			return readExams(reader);
		} finally{
			reader.close();
		}
		
	}
	public static List<SectionDataSet> readSections(File file) throws IOException{
		FileInputStream is = new FileInputStream(file);
		JsonReader reader = new JsonReader(new InputStreamReader(is, "UTF-8"));
		try {
			return readSections(reader);
		} finally{
			reader.close();
		}
		
	}	
	
	// read question
	public static List<SectionDataSet> readSections(JsonReader reader) throws IOException{
		List<SectionDataSet> section = new ArrayList<SectionDataSet>();
		reader.beginArray();
		while(reader.hasNext()){
			section.add(readSection(reader));
		}
		reader.endArray();
		return section;		
	}
	
	private static SectionDataSet readSection(JsonReader reader) throws IOException{
		SectionDataSet section = new SectionDataSet();
		
		reader.beginObject();
		while(reader.hasNext()){
			String name = reader.nextName();
			if(name.equals("section_number")){
				section.setNumber(reader.nextInt());
			} else if(name.equals("section_txt")){
				section.setText(reader.nextString());
			} else if(name.equals("section_hint")){
				section.setHint(reader.nextString());
			} else if(name.equals("questions")){
				section.setQuestions(readQuestions(reader));
			} else {
				reader.skipValue();
			}
		}
		reader.endObject();
		return section;
		
		
	}
	private static List<QuestionDataSet> readQuestions(JsonReader reader) throws IOException{
		List<QuestionDataSet> questions = new ArrayList<QuestionDataSet>();
		reader.beginArray();
		while(reader.hasNext()){
			questions.add(readQuestion(reader));
		}
		reader.endArray();
		return questions;
	}
	private static QuestionDataSet readQuestion(JsonReader reader) throws IOException{
		QuestionDataSet data = new QuestionDataSet();
		
		reader.beginObject();
		while(reader.hasNext()){
			String name = reader.nextName();
			if(name.equals("question_number")){
				data.setNumber(reader.nextInt());
			} else if(name.equals("question_mark")){
				data.setMark(reader.nextInt());
			} else if(name.equals("question_type")){
				data.setType(reader.nextString());
			} else if(name.equals("question_hint")){
				data.setHint(reader.nextString());
			} else if(name.equals("question_answer")){
				data.setAnswer(reader.nextInt());
			} else if(name.equals("question_choice_type")){
				data.setChoiceType(reader.nextString());
			} else if(name.equals("question_choices")){
				data.setChoices(readChoices(reader));
			} else {
				reader.skipValue();
			}
		}
		reader.endObject();
		return data;
	}
	private static List<ChoiceDataSet> readChoices(JsonReader reader) throws IOException{
		List<ChoiceDataSet> choices = new ArrayList<ChoiceDataSet>();
		
		reader.beginArray();
		int i = 0;
		ChoiceDataSet choice = null;
		while(reader.hasNext()){
			choice = readChoice(reader);
			if(choice != null){
				choice.setNumber(i++);
				choices.add(choice);
			}
		}
		reader.endArray();
		
		return choices;
	}
	private static ChoiceDataSet readChoice(JsonReader reader) throws IOException{
		ChoiceDataSet choice = new ChoiceDataSet();
		
		reader.beginObject();
		while(reader.hasNext()){
			String name = reader.nextName();
			if(name.equals("label")){
				choice.setLabel(reader.nextString());
			} else if(name.equals("txt")){
				choice.setText(reader.nextString());
			} else {
				reader.skipValue();
			}
		}
		reader.endObject();
		
		return choice;
	}
	// read conf file
	private static List<ExamDataSet> readExams(JsonReader reader) throws IOException{
		List<ExamDataSet> exams = new ArrayList<ExamDataSet>();
		
		reader.beginArray();
		while(reader.hasNext()){
			exams.add(readExam(reader));
		}
		reader.endArray();
		
		
		return exams;
	}
	
	private static ExamDataSet readExam(JsonReader reader) throws IOException{
		ExamDataSet exam = new ExamDataSet();
		reader.beginObject();
		while(reader.hasNext()){
			String name = reader.nextName();
			if(name.equals("number")){
				exam.setNumber(reader.nextInt());
			} else if(name.equals("date")){
				exam.setDate(reader.nextString());
			} else if(name.equals("levels")){
				exam.setLevels(readLevels(reader));
			} else {
				reader.skipValue();
			}
		}
		reader.endObject();
		return exam;
	}
	private static List<LevelDataSet> readLevels(JsonReader reader) throws IOException{
		List<LevelDataSet> levels = new ArrayList<LevelDataSet>();
		reader.beginArray();
		while(reader.hasNext()){
			levels.add(readLevel(reader));
		}
		reader.endArray();
		return levels;
	}
	private static LevelDataSet readLevel(JsonReader reader) throws IOException{
		LevelDataSet data = new LevelDataSet();
		
		reader.beginObject();
		while(reader.hasNext()){
			String name = reader.nextName();
			if(name.equals("number")){
				data.setNumber(reader.nextInt());
			} else if(name.equals("label")){
				data.setLabel(reader.nextString());
			} else if(name.equals("txt")){
				FileDataSet txt = new FileDataSet();
				txt.setName("txt");
				txt.setPath(reader.nextString());
				data.setTxt(txt);
			} else if(name.equals("pdf")){
				FileDataSet pdf = new FileDataSet();
				pdf.setName("pdf");
				pdf.setPath(reader.nextString());
				data.setPdf(pdf);				
			} else if(name.equals("mp3")){
				FileDataSet audio = new FileDataSet();
				audio.setName("pdf");
				audio.setPath(reader.nextString());
				data.setAudio(audio);				
			} else {
				reader.skipValue();
			}
		}
		reader.endObject();
		return data;
	}

}
