package com.hanaone.gg;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import android.content.Context;

public class DownloadHelper {
	private Context mContext;
	
	
	public DownloadHelper(Context mContext) {
		super();
		this.mContext = mContext;
	}


	public boolean loadExam(String filePath) throws IOException{
		InputStream is = mContext.getAssets().open("test/exam_list.txt");
		
		if(is != null){
			File folder = mContext.getDir("tplt", Context.MODE_PRIVATE);
			File file = new File(folder.getAbsolutePath() + "/config.txt");
			FileOutputStream os = new FileOutputStream(file);
			byte[] buf = new byte[1024];
			int read = 0;
			while((read = is.read(buf)) > 0){
				os.write(buf, 0, read);
			}
			os.close();
			is.close();		
			
			return true;
		}
		
		return false;
		
	}
}
