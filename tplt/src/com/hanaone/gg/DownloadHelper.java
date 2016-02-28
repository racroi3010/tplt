package com.hanaone.gg;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;

public class DownloadHelper {
    private Context mContext;
	
	public DownloadHelper(Context mContext) {
		super();
		this.mContext = mContext;
	}


	public boolean loadExam(String remoteFile) throws IOException{	
		URL  url = new URL("https://www.dropbox.com/s/91dov94ay0ozsiy/exam_list.txt?raw=1");
		URLConnection connection = url.openConnection();
		if(connection instanceof HttpURLConnection){
			HttpURLConnection httpConnection = (HttpURLConnection) connection;
			httpConnection.setAllowUserInteraction(false);
			httpConnection.setInstanceFollowRedirects(true);
			httpConnection.setRequestMethod("GET");
			httpConnection.connect();
			
			int resCode = httpConnection.getResponseCode();
			if(resCode == HttpURLConnection.HTTP_OK){
				InputStream is = httpConnection.getInputStream();
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
			}
		}
		
//		InputStream is = mContext.getAssets().open("test/exam_list.txt");
		

		
		return false;
		
	}
}
