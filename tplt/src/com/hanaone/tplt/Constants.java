package com.hanaone.tplt;

import java.io.File;

import android.content.Context;
import android.os.Environment;

public class Constants {
	public static final String PATH = Environment.getExternalStorageDirectory().getPath();
	
	//public static final String REMOTE_CONFIG_FILE = "https://www.dropbox.com/s/z6t3xg0afbyyaa1/exam_list.txt?raw=1";
	public static final String REMOTE_CONFIG_FILE_JSON = "https://drive.google.com/uc?export=download&id=0Byaz20I0WF7YOGFIZnFwV2NQa2M";

	public static final String FILE_TYPE_PDF = "PDF";
	public static final String FILE_TYPE_TXT = "TXT";
	public static final String FILE_TYPE_MP3 = "MP3";
	public static final String FILE_TYPE_IMG = "IMG";
	
	public static final String LEVEL_ID = "level_id";
	public static final String LEVEL_NAME = "level_name";
	
	public static final String SELECTION_MODE = "selection_mode";
	public static final String SELECTION_MODE_EXAM = "selection_mode_exam";
	public static final String SELECTION_MODE_SAMPLE = "selection_mode_sample";
	
	
	public static final String QUESTION_MODE = "question_mode";
	
	public static final String QUESTION_MODE_PRACTICE = "question_mode_practice";
	public static final String QUESTION_MODE_EXAM = "question_mode_exam";
	public static final String QUESTION_MODE_SAMPLE_BEGINNER = "QUESTION_MODE_SAMPLE_BEGINNER";
	public static final String QUESTION_MODE_SAMPLE_INTERMEDIATE = "QUESTION_MODE_SAMPLE_INTERMEDIATE";
	public static final String QUESTION_MODE_SAMPLE_ADVANCED = "QUESTION_MODE_SAMPLE_ADVANCED";
	
	public static final String LIST_CHOICES = "list_choices";
	public static final String LIST_ANSWERS = "list_answers";
	public static final String LIST_SECTIONS = "list_sections";
	
	
	public static final String PATH_ROOT = "tplt";
	
	public static final String PATH_TEMP = "temp";
	public static final String PATH_FILE = "file";
	
	public static final int STATUS_INACTIVE = 0;
	public static final int STATUS_ACTIVE = 1;
	public static final int STATUS_DOWNLOADING = 2;
	
	
	
	
	public static String getRootPath(Context context){
		File folder = context.getDir(Constants.PATH_ROOT, Context.MODE_PRIVATE);
		return folder.getAbsolutePath();
	}
	public static String getPath(Context context, String folder){
		File root = context.getDir(Constants.PATH_ROOT, Context.MODE_PRIVATE);
		File dir = new File(root.getAbsolutePath() + File.separator + folder);
		dir.mkdirs();
		
		return dir.getAbsolutePath();
	}
}
