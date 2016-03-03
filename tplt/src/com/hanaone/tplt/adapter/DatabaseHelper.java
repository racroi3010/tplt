package com.hanaone.tplt.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.hanaone.tplt.db.model.Choice;
import com.hanaone.tplt.db.model.ExamLevel;
import com.hanaone.tplt.db.model.Examination;
import com.hanaone.tplt.db.model.FileExtra;
import com.hanaone.tplt.db.model.Level;
import com.hanaone.tplt.db.model.Question;
import com.hanaone.tplt.db.model.Section;
import com.hanaone.tplt.db.model.Choice.ChoiceEntry;
import com.hanaone.tplt.db.model.ExamLevel.ExamLevelEntry;
import com.hanaone.tplt.db.model.Examination.ExamEntry;
import com.hanaone.tplt.db.model.FileExtra.FileExtraEntry;
import com.hanaone.tplt.db.model.Level.LevelEntry;
import com.hanaone.tplt.db.model.Question.QuestionEntry;
import com.hanaone.tplt.db.model.Section.SectionEntry;

public class DatabaseHelper extends SQLiteOpenHelper {
	public static final int DATABASE_VERSION = 34;
	public static final String DATABASE_NAME = "tplt.db";
	
	private static final String TEXT_TYPE = " TEXT";
	private static final String INTEGER_TYPE = " INTEGER";
	private static final String FLOAT_TYPE = " FLOAT";
	
	private static final String PRIMARY_KEY = " PRIMARY KEY";
	private static final String FOREIGN_KEY = " FOREIGN KEY";
	private static final String AUTOINCREMENT = " AUTOINCREMENT";
	private static final String COMMA_STEP = ",";
	
	private static final String CREATE_TABLE_FILE = 
			"CREATE TABLE " + FileExtraEntry.TABLE_NAME + " ("
			+ FileExtraEntry._ID + INTEGER_TYPE + PRIMARY_KEY + AUTOINCREMENT + COMMA_STEP
			+ FileExtraEntry.COLUMN_NAME_FILE_TYPE + TEXT_TYPE + COMMA_STEP
			+ FileExtraEntry.COLUMN_NAME_FILE_NAME + TEXT_TYPE + COMMA_STEP
			+ FileExtraEntry.COLUMN_NAME_FILE_PATH + TEXT_TYPE
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
			+ ExamEntry.COLUMN_NAME_NUMBER + INTEGER_TYPE + PRIMARY_KEY + COMMA_STEP
			+ ExamEntry.COLUMN_NAME_DATE + TEXT_TYPE
			+ ")";		
	private static final String CREATE_TABLE_QUESTION = 
			"CREATE TABLE " + QuestionEntry.TABLE_NAME + " ("
			+ ChoiceEntry._ID + INTEGER_TYPE + PRIMARY_KEY + AUTOINCREMENT + COMMA_STEP
			+ QuestionEntry.COLUMN_NAME_NUMBER + INTEGER_TYPE + COMMA_STEP
			+ QuestionEntry.COLUMN_NAME_MARK + INTEGER_TYPE + COMMA_STEP
			+ QuestionEntry.COLUMN_NAME_TEXT + TEXT_TYPE + COMMA_STEP
			+ QuestionEntry.COLUMN_NAME_ANSWER + INTEGER_TYPE + COMMA_STEP
			+ QuestionEntry.COLUMN_NAME_TYPE + TEXT_TYPE + COMMA_STEP
			+ QuestionEntry.COLUMN_NAME_CHOICE_TYPE + TEXT_TYPE + COMMA_STEP
			+ QuestionEntry.COLUMN_NAME_HINT + TEXT_TYPE + COMMA_STEP
			+ QuestionEntry.COLUMN_NAME_START_AUDIO + FLOAT_TYPE + COMMA_STEP
			+ QuestionEntry.COLUMN_NAME_END_AUDIO + FLOAT_TYPE + COMMA_STEP
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
			+ SectionEntry.COLUMN_NAME_HINT + TEXT_TYPE + COMMA_STEP
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
			+ ExamLevelEntry.COLUMN_NAME_PDF_ID + INTEGER_TYPE + COMMA_STEP
			+ ExamLevelEntry.COLUMN_NAME_TXT_ID + INTEGER_TYPE + COMMA_STEP
			+ ExamLevelEntry.COLUMN_NAME_SCORE + INTEGER_TYPE + COMMA_STEP
			+ ExamLevelEntry.COLUMN_NAME_ACTIVE + INTEGER_TYPE + COMMA_STEP
			+ FOREIGN_KEY + " (" + ExamLevelEntry.COLUMN_NAME_EXAM_ID + ") REFERENCES " + ExamEntry.TABLE_NAME + "(" + ExamEntry.COLUMN_NAME_NUMBER + ")" + COMMA_STEP
			+ FOREIGN_KEY + " (" + ExamLevelEntry.COLUMN_NAME_LEVEL_ID + ") REFERENCES " + LevelEntry.TABLE_NAME + "(" + LevelEntry._ID + ")" + COMMA_STEP
			+ FOREIGN_KEY + " (" + ExamLevelEntry.COLUMN_NAME_AUDIO_ID + ") REFERENCES " + FileExtraEntry.TABLE_NAME + "(" + FileExtraEntry._ID + ")" + COMMA_STEP
			+ FOREIGN_KEY + " (" + ExamLevelEntry.COLUMN_NAME_PDF_ID + ") REFERENCES " + FileExtraEntry.TABLE_NAME + "(" + FileExtraEntry._ID + ")" + COMMA_STEP
			+ FOREIGN_KEY + " (" + ExamLevelEntry.COLUMN_NAME_TXT_ID + ") REFERENCES " + FileExtraEntry.TABLE_NAME + "(" + FileExtraEntry._ID + ")"
			+ ")";	

	private static final String DELETE_TABLE_FILE = 
			"DROP TABLE IF EXISTS " + FileExtraEntry.TABLE_NAME; 
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
		db.execSQL(CREATE_TABLE_FILE);
		db.execSQL(CREATE_TABLE_QUESTION);
		db.execSQL(CREATE_TABLE_CHOICE);
		
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DELETE_TABLE_CHOICE);
		db.execSQL(DELETE_TABLE_QUESTION);
		db.execSQL(DELETE_TABLE_FILE);
		db.execSQL(DELETE_TABLE_SECTION);
		
		db.execSQL(DELETE_TABLE_EXAM_LEVEL);
		db.execSQL(DELETE_TABLE_EXAM);
		db.execSQL(DELETE_TABLE_LEVEL);
		
		onCreate(db);
	}
	
	protected long insert(Object obj){		
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
			values.put(ExamLevelEntry.COLUMN_NAME_PDF_ID, examLevel.getPdf_id());
			values.put(ExamLevelEntry.COLUMN_NAME_TXT_ID, examLevel.getTxt_id());
			values.put(ExamLevelEntry.COLUMN_NAME_SCORE, examLevel.getScore());
			values.put(ExamLevelEntry.COLUMN_NAME_ACTIVE, examLevel.getActive());
			
		} else if(obj instanceof Section){
			Section section = (Section) obj;
			
			tableName = SectionEntry.TABLE_NAME;
			values = new ContentValues();
			values.put(SectionEntry.COLUMN_NAME_NUMBER, section.getNumber());
			values.put(SectionEntry.COLUMN_NAME_START_AUDIO, section.getStartAudio());		
			values.put(SectionEntry.COLUMN_NAME_END_AUDIO, section.getEndAudio());	
			values.put(SectionEntry.COLUMN_NAME_TEXT, section.getText());	
			values.put(SectionEntry.COLUMN_NAME_HINT, section.getHint());	
			values.put(SectionEntry.COLUMN_NAME_EXAM_LEVEL_ID, section.getExam_level_id());	
		} else if(obj instanceof FileExtra){
			FileExtra file = (FileExtra) obj;
			
			tableName = FileExtraEntry.TABLE_NAME;
			values = new ContentValues();
			values.put(FileExtraEntry.COLUMN_NAME_FILE_TYPE, file.getType());
			values.put(FileExtraEntry.COLUMN_NAME_FILE_NAME, file.getName());
			values.put(FileExtraEntry.COLUMN_NAME_FILE_PATH, file.getPath());
		} else if(obj instanceof Question){
			Question question = (Question) obj;
			
			tableName = QuestionEntry.TABLE_NAME;
			values = new ContentValues();
			values.put(QuestionEntry.COLUMN_NAME_NUMBER, question.getNumber());
			values.put(QuestionEntry.COLUMN_NAME_MARK, question.getMark());	
			values.put(QuestionEntry.COLUMN_NAME_TEXT, question.getMark());	
			values.put(QuestionEntry.COLUMN_NAME_ANSWER, question.getAnswer());	
			values.put(QuestionEntry.COLUMN_NAME_TYPE, question.getType());	
			values.put(QuestionEntry.COLUMN_NAME_CHOICE_TYPE, question.getChoiceType());	
			values.put(QuestionEntry.COLUMN_NAME_HINT, question.getHint());	
			values.put(QuestionEntry.COLUMN_NAME_START_AUDIO, question.getStartAudio());	
			values.put(QuestionEntry.COLUMN_NAME_END_AUDIO, question.getEndAudio());	
			values.put(QuestionEntry.COLUMN_NAME_SECTION_ID, question.getSection_id());		
		} else if(obj instanceof Choice){
			Choice choice = (Choice) obj;
			
			tableName = ChoiceEntry.TABLE_NAME;
			values = new ContentValues();
			values.put(ChoiceEntry.COLUMN_NAME_NUMBER, choice.getNumber());
			values.put(ChoiceEntry.COLUMN_NAME_LABEL, choice.getLabel());
			values.put(ChoiceEntry.COLUMN_NAME_TEXT, choice.getText());
			values.put(ChoiceEntry.COLUMN_QUESTION_ID, choice.getQuestion_id());
		}
		
		if(values != null){
			SQLiteDatabase db = getWritableDatabase();
			rowId = db.insert(tableName, null, values);
			db.close();
		}
		
		return rowId;
	}
	protected int update(Object obj){		
		int rowId = -1;
		if(obj == null) return rowId;
		ContentValues values  = null;
		String tableName = null;
		int id = -1;
		String idColumn = null;
		if(obj instanceof Examination){
			Examination exam = (Examination) obj;
			
			tableName = ExamEntry.TABLE_NAME;
			id = exam.getNumber();
			idColumn = ExamEntry.COLUMN_NAME_NUMBER;
			
			values = new ContentValues();
			values.put(ExamEntry.COLUMN_NAME_NUMBER, exam.getNumber());
			values.put(ExamEntry.COLUMN_NAME_DATE, exam.getDate());
		} else if(obj instanceof Level){
			Level level = (Level) obj;
			
			tableName = LevelEntry.TABLE_NAME;
			id = level.getId();
			idColumn = LevelEntry._ID;
			
			values = new ContentValues();
			values.put(LevelEntry.COLUMN_NAME_NUMBER, level.getNumber());
			values.put(LevelEntry.COLUMN_NAME_LABEL, level.getLabel());
			
		} else if(obj instanceof ExamLevel){
			ExamLevel examLevel = (ExamLevel) obj;
			
			tableName = ExamLevelEntry.TABLE_NAME;
			id = examLevel.getId();
			idColumn = ExamLevelEntry._ID;
			
			values = new ContentValues();
			values.put(ExamLevelEntry.COLUMN_NAME_EXAM_ID, examLevel.getExam_id());
			values.put(ExamLevelEntry.COLUMN_NAME_LEVEL_ID, examLevel.getLevel_id());	
			values.put(ExamLevelEntry.COLUMN_NAME_AUDIO_ID, examLevel.getAudio_id());	
			values.put(ExamLevelEntry.COLUMN_NAME_PDF_ID, examLevel.getPdf_id());
			values.put(ExamLevelEntry.COLUMN_NAME_TXT_ID, examLevel.getTxt_id());
			values.put(ExamLevelEntry.COLUMN_NAME_SCORE, examLevel.getScore());
			values.put(ExamLevelEntry.COLUMN_NAME_ACTIVE, examLevel.getActive());
			
		} else if(obj instanceof Section){
			Section section = (Section) obj;
			
			tableName = SectionEntry.TABLE_NAME;
			id = section.getId();
			idColumn = SectionEntry._ID;
			
			values = new ContentValues();
			values.put(SectionEntry.COLUMN_NAME_NUMBER, section.getNumber());
			values.put(SectionEntry.COLUMN_NAME_START_AUDIO, section.getStartAudio());		
			values.put(SectionEntry.COLUMN_NAME_END_AUDIO, section.getEndAudio());	
			values.put(SectionEntry.COLUMN_NAME_TEXT, section.getText());	
			values.put(SectionEntry.COLUMN_NAME_HINT, section.getHint());	
			values.put(SectionEntry.COLUMN_NAME_EXAM_LEVEL_ID, section.getExam_level_id());	
		} else if(obj instanceof FileExtra){
			FileExtra file = (FileExtra) obj;
			
			tableName = FileExtraEntry.TABLE_NAME;
			id = file.getId();
			idColumn = FileExtraEntry._ID;
			
			values = new ContentValues();
			values.put(FileExtraEntry.COLUMN_NAME_FILE_TYPE, file.getType());
			values.put(FileExtraEntry.COLUMN_NAME_FILE_NAME, file.getName());
			values.put(FileExtraEntry.COLUMN_NAME_FILE_PATH, file.getPath());
		} else if(obj instanceof Question){
			Question question = (Question) obj;
			
			tableName = QuestionEntry.TABLE_NAME;
			id = question.getId();
			idColumn = QuestionEntry._ID;
			
			values = new ContentValues();
			values.put(QuestionEntry.COLUMN_NAME_NUMBER, question.getNumber());
			values.put(QuestionEntry.COLUMN_NAME_MARK, question.getMark());	
			values.put(QuestionEntry.COLUMN_NAME_TEXT, question.getMark());	
			values.put(QuestionEntry.COLUMN_NAME_ANSWER, question.getAnswer());	
			values.put(QuestionEntry.COLUMN_NAME_TYPE, question.getType());	
			values.put(QuestionEntry.COLUMN_NAME_CHOICE_TYPE, question.getChoiceType());	
			values.put(QuestionEntry.COLUMN_NAME_HINT, question.getHint());
			values.put(QuestionEntry.COLUMN_NAME_START_AUDIO, question.getStartAudio());	
			values.put(QuestionEntry.COLUMN_NAME_END_AUDIO, question.getEndAudio());
			values.put(QuestionEntry.COLUMN_NAME_SECTION_ID, question.getSection_id());		
		} else if(obj instanceof Choice){
			Choice choice = (Choice) obj;
			
			tableName = ChoiceEntry.TABLE_NAME;
			id = choice.getId();
			idColumn = ChoiceEntry._ID;
			
			values = new ContentValues();
			values.put(ChoiceEntry.COLUMN_NAME_NUMBER, choice.getNumber());
			values.put(ChoiceEntry.COLUMN_NAME_LABEL, choice.getLabel());
			values.put(ChoiceEntry.COLUMN_NAME_TEXT, choice.getText());
			values.put(ChoiceEntry.COLUMN_QUESTION_ID, choice.getQuestion_id());
		}
		
		if(values != null){
			SQLiteDatabase db = getWritableDatabase();
			//rowId = db.insert(tableName, null, values);
			rowId = db.update(tableName, values, idColumn + " = ?" , new String[]{id + ""});
			db.close();
		}
		
		return rowId;
	}	
	// exam
	public List<Examination> selectAllExam(){
		String query = "SELECT * FROM " + ExamEntry.TABLE_NAME 
				+ " ORDER BY " + ExamEntry.COLUMN_NAME_NUMBER + " DESC";
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.rawQuery(query, null);
		
		List<Examination> list = new ArrayList<Examination>();
		if(!c.moveToFirst()){
			db.close();
			return list;
		}
		do {
			Examination data = new Examination();
			data.setNumber(c.getInt(0));
			data.setDate(c.getString(1));
			
			list.add(data);
			
		} while(c.moveToNext());
			
		c.close();
		db.close();
		
		return list;				
	}

	public Examination selectExamByNumber(int examNumber){
		String query = "SELECT * FROM " + ExamEntry.TABLE_NAME 
				+ " WHERE " + ExamEntry.COLUMN_NAME_NUMBER + " = " + examNumber;
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.rawQuery(query, null);	
		if(!c.moveToFirst()){
			db.close();
			return null;
		}
		
		Examination exam = new Examination();
		exam.setNumber(c.getInt(0));
		exam.setDate(c.getString(1));		
		
			
		c.close();
		db.close();
		
		return exam;
	}

	// exam level
	public List<ExamLevel> selectExamLevelByExamNumber(int examNumber){
		String query = "SELECT * FROM " + ExamLevelEntry.TABLE_NAME 
					+ " WHERE " + ExamLevelEntry.COLUMN_NAME_EXAM_ID + " = " + examNumber
					+ " ORDER BY " + ExamLevelEntry._ID + " DESC";
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.rawQuery(query, null);
		
		List<ExamLevel> list = new ArrayList<ExamLevel>();
		if(!c.moveToFirst()){
			db.close();
			return list;
		}
		do {
			ExamLevel examLevel = new ExamLevel();
			examLevel.setId(c.getInt(0));
			examLevel.setExam_id(c.getInt(1));
			examLevel.setLevel_id(c.getInt(2));
			examLevel.setAudio_id(c.getInt(3));
			examLevel.setPdf_id(c.getInt(4));
			examLevel.setTxt_id(c.getInt(5));
			examLevel.setScore(c.getInt(6));
			examLevel.setActive(c.getInt(7));
			
			list.add(examLevel);
		} while(c.moveToNext());
			
		c.close();
		db.close();
		
		return list;		
	}
	
	// file
	public FileExtra selectFileById(int fileid){
		String query = "SELECT * FROM " + FileExtraEntry.TABLE_NAME 
					+ " WHERE " + FileExtraEntry._ID + " = " + fileid
					+ " ORDER BY " + ExamLevelEntry._ID + " DESC";
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.rawQuery(query, null);
		
		if(!c.moveToFirst()){
			db.close();
			return null;
		}
		FileExtra file = new FileExtra();
		file.setId(c.getInt(0));
		file.setType(c.getString(1));
		file.setName(c.getString(2));
		file.setPath(c.getString(3));
			
		c.close();
		db.close();
		
		return file;		
	}	
	
	// level
	public Level selectLevelByNumber(int levelNumber){
		String query = "SELECT * FROM " + LevelEntry.TABLE_NAME 
				+ " WHERE " + LevelEntry.COLUMN_NAME_NUMBER + " = " + levelNumber;
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.rawQuery(query, null);
		
		if(!c.moveToFirst()){
			db.close();
			return null;
		}		
		Level level = new Level();
		level.setId(c.getInt(0));
		level.setNumber(c.getInt(1));
		level.setLabel(c.getString(2));		
		
		c.close();
		db.close();
		
		return level;		
	}	
	public Level selectLevelById(int levelId){
		String query = "SELECT * FROM " + LevelEntry.TABLE_NAME 
				+ " WHERE " + LevelEntry._ID + " = " + levelId;
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.rawQuery(query, null);
		
		if(!c.moveToFirst()){
			db.close();
			return null;
		}		
		
		Level level = new Level();
		level.setId(c.getInt(0));
		level.setNumber(c.getInt(1));
		level.setLabel(c.getString(2));
		
			
		c.close();
		db.close();
		
		return level;	
	}
	
	public ExamLevel selectExamLevelById(int examLevelId){
		String query = "SELECT * FROM " + ExamLevelEntry.TABLE_NAME 
				+ " WHERE " + ExamLevelEntry._ID + " = " + examLevelId;
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.rawQuery(query, null);
		
		if(!c.moveToFirst()){
			db.close();
			return null;
		}		
		
		ExamLevel examLevel = new ExamLevel();
		examLevel.setId(c.getInt(0));
		examLevel.setExam_id(c.getInt(1));
		examLevel.setLevel_id(c.getInt(2));
		examLevel.setAudio_id(c.getInt(3));
		examLevel.setPdf_id(c.getInt(4));
		examLevel.setTxt_id(c.getInt(5));
		examLevel.setActive(c.getInt(6));
			
		c.close();
		db.close();
		
		return examLevel;			
	}
	
	public List<Section> selectSectionByExamLevelId(int examLevelId){
		String query = "SELECT * FROM " + SectionEntry.TABLE_NAME 
				+ " WHERE " + SectionEntry.COLUMN_NAME_EXAM_LEVEL_ID + " = " + examLevelId
				+ " ORDER BY " + SectionEntry.COLUMN_NAME_NUMBER + " ASC";
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.rawQuery(query, null);
		
		List<Section> list = new ArrayList<Section>();
		if(!c.moveToFirst()){
			db.close();
			return list;
		}
		do {
			Section section = new Section();
			section.setId(c.getInt(0));
			section.setNumber(c.getInt(1));
			section.setStartAudio(c.getFloat(2));
			section.setEndAudio(c.getFloat(3));
			section.setText(c.getString(4));
			section.setText(c.getString(5));
			section.setExam_level_id(c.getInt(6));
			
			list.add(section);
		} while(c.moveToNext());
			
		c.close();
		db.close();
			
		return list;
	}
	public List<Question> selectQuestionBySectionId(int sectionId){
		String query = "SELECT * FROM " + QuestionEntry.TABLE_NAME 
				+ " WHERE " + QuestionEntry.COLUMN_NAME_SECTION_ID + " = " + sectionId
				+ " ORDER BY " + QuestionEntry.COLUMN_NAME_NUMBER + " ASC";
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.rawQuery(query, null);
		
		List<Question> list = new ArrayList<Question>();
		if(!c.moveToFirst()){
			db.close();
			return list;
		}
		do {
			Question question = new Question();
			question.setId(c.getInt(0));
			question.setNumber(c.getInt(1));
			question.setMark(c.getInt(2));
			question.setText(c.getString(3));
			question.setAnswer(c.getInt(4));
			question.setType(c.getString(5));
			question.setChoiceType(c.getString(6));
			question.setHint(c.getString(7));
			question.setStartAudio(c.getFloat(8));
			question.setEndAudio(c.getFloat(9));
			question.setSection_id(c.getInt(10));			
			list.add(question);
		} while(c.moveToNext());
			
		c.close();
		db.close();
			
		return list;		
	}
	public List<Choice> selectChoiceByQuestionId(int questionId){
		String query = "SELECT * FROM " + ChoiceEntry.TABLE_NAME 
				+ " WHERE " + ChoiceEntry.COLUMN_QUESTION_ID + " = " + questionId
				+ " ORDER BY " + ChoiceEntry.COLUMN_NAME_NUMBER + " ASC";
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.rawQuery(query, null);
		
		List<Choice> list = new ArrayList<Choice>();
		if(!c.moveToFirst()){
			db.close();
			return list;
		}
		do {
			Choice choice = new Choice();
			choice.setId(c.getInt(0));
			choice.setNumber(c.getInt(1));
			choice.setLabel(c.getString(2));
			choice.setText(c.getString(3));
			choice.setQuestion_id(c.getInt(4));
			
			list.add(choice);
		} while(c.moveToNext());
			
		c.close();
		db.close();
			
		return list;		
	}
	
}
