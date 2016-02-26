package com.hanaone.tplt.adapter;

import java.util.ArrayList;
import java.util.List;

import com.hanaone.tplt.db.ExamDataSet;
import com.hanaone.tplt.db.LevelDataSet;
import com.hanaone.tplt.db.dataset.Answer;
import com.hanaone.tplt.db.dataset.Audio;
import com.hanaone.tplt.db.dataset.Choice;
import com.hanaone.tplt.db.dataset.ExamLevel;
import com.hanaone.tplt.db.dataset.ExamLevel.ExamLevelEntry;
import com.hanaone.tplt.db.dataset.Examination;
import com.hanaone.tplt.db.dataset.Level;
import com.hanaone.tplt.db.dataset.Level.LevelEntry;
import com.hanaone.tplt.db.dataset.Question;
import com.hanaone.tplt.db.dataset.Section;
import com.hanaone.tplt.db.dataset.Answer.AnswerEntry;
import com.hanaone.tplt.db.dataset.Audio.AudioEntry;
import com.hanaone.tplt.db.dataset.Choice.ChoiceEntry;
import com.hanaone.tplt.db.dataset.Examination.ExamEntry;
import com.hanaone.tplt.db.dataset.Question.QuestionEntry;
import com.hanaone.tplt.db.dataset.Section.SectionEntry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "tplt.db";
	
	private static final String TEXT_TYPE = " TEXT";
	private static final String INTEGER_TYPE = " INTEGER";
	private static final String FLOAT_TYPE = " FLOAT";
	
	private static final String PRIMARY_KEY = " PRIMARY KEY";
	private static final String FOREIGN_KEY = " FOREIGN KEY";
	private static final String AUTOINCREMENT = " AUTOINCREMENT";
	private static final String COMMA_STEP = ",";
	
	private static final String CREATE_TABLE_ANSWER = 
			"CREATE TABLE " + AnswerEntry.TABLE_NAME + " ("
			+ AnswerEntry._ID + INTEGER_TYPE + PRIMARY_KEY + AUTOINCREMENT + COMMA_STEP
			+ AnswerEntry.COLUMN_NAME_QUESTION_ID + INTEGER_TYPE + COMMA_STEP
			+ AnswerEntry.COLUMN_NAME_CHOICE_ID + INTEGER_TYPE + COMMA_STEP
			+ FOREIGN_KEY + " (" + AnswerEntry.COLUMN_NAME_QUESTION_ID + ") REFERENCES " + QuestionEntry.TABLE_NAME + "(" + QuestionEntry._ID + ")" + COMMA_STEP
			+ FOREIGN_KEY + " (" + AnswerEntry.COLUMN_NAME_CHOICE_ID + ") REFERENCES " + ChoiceEntry.TABLE_NAME + "(" + ChoiceEntry._ID + ")"
			+ ")";
	private static final String CREATE_TABLE_AUDIO = 
			"CREATE TABLE " + AudioEntry.TABLE_NAME + " ("
			+ AudioEntry._ID + INTEGER_TYPE + PRIMARY_KEY + AUTOINCREMENT + COMMA_STEP
			+ AudioEntry.COLUMN_NAME_PATH + TEXT_TYPE + COMMA_STEP
			+ AudioEntry.COLUMN_NAME_EXAM_ID + INTEGER_TYPE + COMMA_STEP
			+ FOREIGN_KEY + " (" + AudioEntry.COLUMN_NAME_EXAM_ID + ") REFERENCES " + ExamEntry.TABLE_NAME + "(" + ExamEntry._ID + ")"
			+ ")";
	
	private static final String CREATE_TABLE_CHOICE = 
			"CREATE TABLE " + ChoiceEntry.TABLE_NAME + " ("
			+ ChoiceEntry._ID + INTEGER_TYPE + PRIMARY_KEY + AUTOINCREMENT + COMMA_STEP
			+ ChoiceEntry.COLUMN_NAME_NUMBER + INTEGER_TYPE + COMMA_STEP
			+ ChoiceEntry.COLUMN_NAME_LABEL + TEXT_TYPE + COMMA_STEP
			+ ChoiceEntry.COLUMN_NAME_TEXT + TEXT_TYPE + COMMA_STEP
			+ ChoiceEntry.COLUMN_QUESTION_ID + INTEGER_TYPE + COMMA_STEP
			+ FOREIGN_KEY + " (" + ChoiceEntry.COLUMN_QUESTION_ID + ") REFERENCES " + QuestionEntry.TABLE_NAME + "(" + QuestionEntry._ID + ")"
			+ ")";	

	private static final String CREATE_TABLE_EXAM = 
			"CREATE TABLE " + ExamEntry.TABLE_NAME + " ("
			+ ExamEntry._ID + INTEGER_TYPE + PRIMARY_KEY + AUTOINCREMENT + COMMA_STEP
			+ ExamEntry.COLUMN_NAME_NUMBER + INTEGER_TYPE + COMMA_STEP
			+ ExamEntry.COLUMN_NAME_DATE + TEXT_TYPE
			+ ")";		
	private static final String CREATE_TABLE_QUESTION = 
			"CREATE TABLE " + QuestionEntry.TABLE_NAME + " ("
			+ ChoiceEntry._ID + INTEGER_TYPE + PRIMARY_KEY + AUTOINCREMENT + COMMA_STEP
			+ QuestionEntry.COLUMN_NAME_NUMBER + INTEGER_TYPE + COMMA_STEP
			+ QuestionEntry.COLUMN_NAME_MARK + INTEGER_TYPE + COMMA_STEP
			+ QuestionEntry.COLUMN_NAME_TEXT + TEXT_TYPE + COMMA_STEP
			+ QuestionEntry.COLUMN_NAME_SECTION_ID + INTEGER_TYPE + COMMA_STEP
			+ FOREIGN_KEY + " (" + QuestionEntry.COLUMN_NAME_SECTION_ID + ") REFERENCES " + SectionEntry.TABLE_NAME + "(" + SectionEntry._ID + ")"
			+ ")";	
	private static final String CREATE_TABLE_SECTION = 
			"CREATE TABLE " + SectionEntry.TABLE_NAME + " ("
			+ SectionEntry._ID + INTEGER_TYPE + PRIMARY_KEY + AUTOINCREMENT + COMMA_STEP
			+ SectionEntry.COLUMN_NAME_NUMBER + INTEGER_TYPE + COMMA_STEP
			+ SectionEntry.COLUMN_NAME_START_AUDIO + FLOAT_TYPE + COMMA_STEP
			+ SectionEntry.COLUMN_NAME_END_AUDIO + FLOAT_TYPE + COMMA_STEP
			+ SectionEntry.COLUMN_NAME_TEXT + TEXT_TYPE + COMMA_STEP
			+ SectionEntry.COLUMN_NAME_EXAM_LEVEL_ID + INTEGER_TYPE + COMMA_STEP
			+ FOREIGN_KEY + " (" + SectionEntry.COLUMN_NAME_EXAM_LEVEL_ID + ") REFERENCES " + ExamLevelEntry.TABLE_NAME + "(" + ExamLevelEntry._ID + ")"
			+ ")";	
	private static final String CREATE_TABLE_LEVEL = 
			"CREATE TABLE " + LevelEntry.TABLE_NAME + " ("
			+ LevelEntry._ID + INTEGER_TYPE + PRIMARY_KEY + AUTOINCREMENT + COMMA_STEP
			+ LevelEntry.COLUMN_NAME_NUMBER + INTEGER_TYPE + COMMA_STEP
			+ LevelEntry.COLUMN_NAME_LABEL + TEXT_TYPE
			+ ")";		
	
	private static final String CREATE_TABLE_EXAM_LEVEL = 
			"CREATE TABLE " + ExamLevelEntry.TABLE_NAME + " ("
			+ ExamLevelEntry._ID + INTEGER_TYPE + PRIMARY_KEY + AUTOINCREMENT + COMMA_STEP
			+ ExamLevelEntry.COLUMN_NAME_EXAM_ID + INTEGER_TYPE  + COMMA_STEP
			+ ExamLevelEntry.COLUMN_NAME_LEVEL_ID + INTEGER_TYPE  + COMMA_STEP
			+ ExamLevelEntry.COLUMN_NAME_AUDIO_ID + INTEGER_TYPE  + COMMA_STEP
			+ FOREIGN_KEY + " (" + ExamLevelEntry.COLUMN_NAME_EXAM_ID + ") REFERENCES " + ExamEntry.TABLE_NAME + "(" + ExamEntry._ID + ")" + COMMA_STEP
			+ FOREIGN_KEY + " (" + ExamLevelEntry.COLUMN_NAME_LEVEL_ID + ") REFERENCES " + LevelEntry.TABLE_NAME + "(" + LevelEntry._ID + ")" + COMMA_STEP
			+ FOREIGN_KEY + " (" + ExamLevelEntry.COLUMN_NAME_AUDIO_ID + ") REFERENCES " + AudioEntry.TABLE_NAME + "(" + AudioEntry._ID + ")"
			+ ")";	
	
	private static final String DELETE_TABLE_ANSWER = 
			"DROP TABLE IF EXISTS " + AnswerEntry.TABLE_NAME; 
	private static final String DELETE_TABLE_AUDIO = 
			"DROP TABLE IF EXISTS " + AudioEntry.TABLE_NAME; 
	private static final String DELETE_TABLE_CHOICE = 
			"DROP TABLE IF EXISTS " + ChoiceEntry.TABLE_NAME; 
	private static final String DELETE_TABLE_EXAM = 
			"DROP TABLE IF EXISTS " + ExamEntry.TABLE_NAME; 
	private static final String DELETE_TABLE_QUESTION = 
			"DROP TABLE IF EXISTS " + QuestionEntry.TABLE_NAME; 
	private static final String DELETE_TABLE_SECTION = 
			"DROP TABLE IF EXISTS " + SectionEntry.TABLE_NAME; 
	private static final String DELETE_TABLE_LEVEL = 
			"DROP TABLE IF EXISTS " + LevelEntry.TABLE_NAME; 	
	private static final String DELETE_TABLE_EXAM_LEVEL = 
			"DROP TABLE IF EXISTS " + ExamLevelEntry.TABLE_NAME; 		
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}	
	
	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, DATABASE_NAME, factory, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_EXAM);
		db.execSQL(CREATE_TABLE_LEVEL);
		db.execSQL(CREATE_TABLE_EXAM_LEVEL);
		
		db.execSQL(CREATE_TABLE_SECTION);
		db.execSQL(CREATE_TABLE_AUDIO);
		db.execSQL(CREATE_TABLE_QUESTION);
		db.execSQL(CREATE_TABLE_CHOICE);
		db.execSQL(CREATE_TABLE_ANSWER);
		
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DELETE_TABLE_ANSWER);
		db.execSQL(DELETE_TABLE_CHOICE);
		db.execSQL(DELETE_TABLE_QUESTION);
		db.execSQL(DELETE_TABLE_AUDIO);
		db.execSQL(DELETE_TABLE_SECTION);
		
		db.execSQL(DELETE_TABLE_EXAM_LEVEL);
		db.execSQL(DELETE_TABLE_EXAM);
		db.execSQL(DELETE_TABLE_LEVEL);
		
		onCreate(db);
	}
	
	private long insert(Object obj){		
		long rowId = -1;
		if(obj == null) return rowId;
		ContentValues values  = null;
		String tableName = null;
		if(obj instanceof Examination){
			Examination exam = (Examination) obj;
			
			tableName = ExamEntry.TABLE_NAME;
			values = new ContentValues();
			values.put(ExamEntry.COLUMN_NAME_NUMBER, exam.getNumber());
			values.put(ExamEntry.COLUMN_NAME_DATE, exam.getDate());
		} else if(obj instanceof Level){
			Level level = (Level) obj;
			
			tableName = LevelEntry.TABLE_NAME;
			values = new ContentValues();
			values.put(LevelEntry.COLUMN_NAME_NUMBER, level.getNumber());
			values.put(LevelEntry.COLUMN_NAME_LABEL, level.getLabel());
		} else if(obj instanceof ExamLevel){
			ExamLevel examLevel = (ExamLevel) obj;
			
			tableName = ExamLevelEntry.TABLE_NAME;
			values = new ContentValues();
			values.put(ExamLevelEntry.COLUMN_NAME_EXAM_ID, examLevel.getExam_id());
			values.put(ExamLevelEntry.COLUMN_NAME_LEVEL_ID, examLevel.getLevel_id());	
			values.put(ExamLevelEntry.COLUMN_NAME_AUDIO_ID, examLevel.getAudio_id());	
		} else if(obj instanceof Section){
			Section section = (Section) obj;
			
			tableName = SectionEntry.TABLE_NAME;
			values = new ContentValues();
			values.put(SectionEntry.COLUMN_NAME_NUMBER, section.getNumber());
			values.put(SectionEntry.COLUMN_NAME_START_AUDIO, section.getStartAudio());		
			values.put(SectionEntry.COLUMN_NAME_END_AUDIO, section.getEndAudio());	
			values.put(SectionEntry.COLUMN_NAME_TEXT, section.getText());	
			values.put(SectionEntry.COLUMN_NAME_EXAM_LEVEL_ID, section.getExam_level_id());	
		} else if(obj instanceof Audio){
			Audio audio = (Audio) obj;
			
			tableName = AudioEntry.TABLE_NAME;
			values = new ContentValues();
			values.put(AudioEntry.COLUMN_NAME_PATH, audio.getPath());
			values.put(AudioEntry.COLUMN_NAME_EXAM_ID, audio.getExam_id());		
		} else if(obj instanceof Question){
			Question question = (Question) obj;
			
			tableName = QuestionEntry.TABLE_NAME;
			values = new ContentValues();
			values.put(QuestionEntry.COLUMN_NAME_NUMBER, question.getNumber());
			values.put(QuestionEntry.COLUMN_NAME_MARK, question.getMark());	
			values.put(QuestionEntry.COLUMN_NAME_TEXT, question.getMark());		
			values.put(QuestionEntry.COLUMN_NAME_SECTION_ID, question.getSection_id());		
		} else if(obj instanceof Choice){
			Choice choice = (Choice) obj;
			
			tableName = ChoiceEntry.TABLE_NAME;
			values = new ContentValues();
			values.put(ChoiceEntry.COLUMN_NAME_NUMBER, choice.getNumber());
			values.put(ChoiceEntry.COLUMN_NAME_LABEL, choice.getLabel());
			values.put(ChoiceEntry.COLUMN_NAME_TEXT, choice.getText());
			values.put(ChoiceEntry.COLUMN_QUESTION_ID, choice.getQuestion_id());
		} else if(obj instanceof Answer){
			Answer answer = (Answer) obj;
			
			tableName = AnswerEntry.TABLE_NAME;
			values = new ContentValues();
			values.put(AnswerEntry.COLUMN_NAME_QUESTION_ID, answer.getQuestion_id());
			values.put(AnswerEntry.COLUMN_NAME_CHOICE_ID, answer.getChoice_id());
		}
		
		if(values != null){
			SQLiteDatabase db = getWritableDatabase();
			rowId = db.insert(tableName, null, values);
			db.close();
		}
		
		return rowId;
	}
	
	public List<ExamDataSet> selectAllExam(){
		String query = "SELECT * FROM " + ExamEntry.TABLE_NAME + " ORDER BY " + ExamEntry.COLUMN_NAME_NUMBER + " DESC";
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.rawQuery(query, null);
		
		List<ExamDataSet> list = new ArrayList<ExamDataSet>();
		if(!c.moveToFirst()){
			db.close();
			return list;
		}
		do {
			ExamDataSet data = new ExamDataSet();
			data.setId(c.getInt(0));
			data.setNumber(c.getInt(1));
			data.setDate(c.getString(2));
			
			list.add(data);
			
		} while(c.moveToNext());
			
		c.close();
		db.close();
		
		return list;
	}
	
	public List<LevelDataSet> selectAllLevel(int examId){
		String query = "SELECT * FROM " + ExamLevelEntry.TABLE_NAME 
					+ " WHERE " + ExamLevelEntry.COLUMN_NAME_EXAM_ID + " = " + examId
					+ " ORDER BY " + ExamLevelEntry._ID + " DESC";
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.rawQuery(query, null);
		
		List<LevelDataSet> list = new ArrayList<LevelDataSet>();
		if(!c.moveToFirst()){
			db.close();
			return list;
		}
		do {
			ExamDataSet data = new ExamDataSet();
			data.setId(c.getInt(0));
			data.setNumber(c.getInt(1));
			data.setDate(c.getString(2));
			
			//list.add(data);
			
		} while(c.moveToNext());
			
		c.close();
		db.close();
		
		return list;		
	}
	
}
