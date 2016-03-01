package com.hanaone.tplt;

import android.os.Environment;

public class Constants {
	public static final String PATH = Environment.getExternalStorageDirectory().getPath();
	
	public static final String REMOTE_CONFIG_FILE = "https://www.dropbox.com/s/z6t3xg0afbyyaa1/exam_list.txt?raw=1";
	public static final String REMOTE_CONFIG_FILE_JSON = "https://www.dropbox.com/s/csgq6kxmfae9md2/exam_list_json.txt?raw=1";

	public static final String FILE_TYPE_PDF = "PDF";
	public static final String FILE_TYPE_TXT = "TXT";
	public static final String FILE_TYPE_MP3 = "MP3";
	
	public static final String LEVEL_ID = "level_id";
}
