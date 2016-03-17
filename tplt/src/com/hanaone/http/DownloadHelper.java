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

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.hanaone.tplt.Constants;

import android.content.Context;
import android.os.AsyncTask;

public class DownloadHelper {
    private Context mContext;
	private ConnectionHelper mConnection;
	public DownloadHelper(Context mContext) {
		super();
		this.mContext = mContext;
		this.mConnection = new ConnectionHelper(mContext);
	}


	public boolean downloadFile(String remoteFile, String localFile) throws IOException, SAXException, ParserConfigurationException{	
		
		InputStream is = this.mConnection.connect(Constants.REMOTE_CONFIG_FILE_JSON, ConnectionHelper.HOST_GOOGLE);
		
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
	
		return false;
		
	}
	public InputStream parseUrl(String remoteFile) throws IOException, SAXException, ParserConfigurationException{
		return this.mConnection.connect(remoteFile, ConnectionHelper.HOST_GOOGLE);
	}
	public long getSize(String remoteFile) throws IOException{
		return this.mConnection.getSize(remoteFile, ConnectionHelper.HOST_GOOGLE);
	}
}
