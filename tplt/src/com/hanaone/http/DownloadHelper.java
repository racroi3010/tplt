package com.hanaone.http;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.AsyncTask;

public class DownloadHelper {
    private Context mContext;
	
	public DownloadHelper(Context mContext) {
		super();
		this.mContext = mContext;
	}


	public boolean downloadFile(String remoteFile, String localFile) throws IOException{	
		URL  url = new URL(remoteFile);
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
					//File folder = mContext.getDir("tplt", Context.MODE_PRIVATE);
					File file = new File(localFile);
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
	public InputStream parseUrl(String remoteFile) throws IOException{
		URL  url = new URL(remoteFile);
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
				return is;			
			}
		}		
		return null;
	}
	public int getSize(String remoteFile) throws IOException{
		URL  url = new URL(remoteFile);
		URLConnection connection = url.openConnection();
		List<String> values = connection.getHeaderFields().get("content-Length");
		if(values != null && !values.isEmpty()){
			String sLength = values.get(0);
			
			return Integer.parseInt(sLength);
		}
		return 0;
	}
}
