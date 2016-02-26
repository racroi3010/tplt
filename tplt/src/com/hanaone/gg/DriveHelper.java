package com.hanaone.gg;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;

import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpRequest;
import com.google.api.client.http.json.JsonHttpRequestInitializer;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveRequest;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;

public class DriveHelper {
	private Context mContext;
	private boolean alreadyTriedAgain = false;
	
	
	public DriveHelper(Context mContext) {
		super();
		this.mContext = mContext;
		AccountManager am = AccountManager.get(mContext);
		am.getAuthToken(am.getAccounts()[0], "oauth2:" + DriveScopes.DRIVE,
		    new Bundle(),
		    true,
		    new OnTokenAcquired(),
		    null);		
		
	}

	private class OnTokenAcquired implements AccountManagerCallback<Bundle>{

		@Override
		public void run(AccountManagerFuture<Bundle> future) {
		       try {
		            final String token = future.getResult().getString(AccountManager.KEY_AUTHTOKEN);
		            HttpTransport httpTransport = new NetHttpTransport();
		            JacksonFactory jsonFactory = new JacksonFactory();
		            Drive.Builder b = new Drive.Builder(httpTransport, jsonFactory, null);
		            
		            b.setJsonHttpRequestInitializer(new JsonHttpRequestInitializer() {
		                @Override
		                public void initialize(JsonHttpRequest request) throws IOException {
		                    DriveRequest driveRequest = (DriveRequest) request;
		                    driveRequest.setPrettyPrint(true);
		                    driveRequest.setKey("");
		                    driveRequest.setOauthToken(token);
		                }
		            });

		            final Drive drive = b.build();

		            final com.google.api.services.drive.model.File body = new com.google.api.services.drive.model.File();
		            body.setTitle("My Test File");
		    body.setDescription("A Test File");
		    body.setMimeType("text/plain");

		            final FileContent mediaContent = new FileContent("text/plain", new java.io.File(""));
		            new Thread(new Runnable() {
		                public void run() {
		                    try {
		                        com.google.api.services.drive.model.File file = drive.files().insert(body, mediaContent).execute();
		                        alreadyTriedAgain = false; // Global boolean to make sure you don't repeatedly try too many times when the server is down or your code is faulty... they'll block requests until the next day if you make 10 bad requests, I found.
		                    } catch (IOException e) {
		                        if (!alreadyTriedAgain) {
		                            alreadyTriedAgain = true;
		                            AccountManager am = AccountManager.get(mContext);
		                            am.invalidateAuthToken(am.getAccounts()[0].type, null); // Requires the permissions MANAGE_ACCOUNTS & USE_CREDENTIALS in the Manifest
		                            //am.getAuthToken(account, token, options, mContext, callback, handler);
		                    		am.getAuthToken(am.getAccounts()[0], "oauth2:" + DriveScopes.DRIVE,
		                    			    new Bundle(),
		                    			    true,
		                    			    new OnTokenAcquired(),
		                    			    null);	
		                        } else {
		                            // Give up. Crash or log an error or whatever you want.
		                        }
		                    }
		                }
		            }).start();
		            Intent launch = (Intent)future.getResult().get(AccountManager.KEY_INTENT);
		            if (launch != null) {
		                ((Activity)mContext).startActivityForResult(launch, 3025);
		                return; // Not sure why... I wrote it here for some reason. Might not actually be necessary.
		            }
		        } catch (OperationCanceledException e) {
		            // Handle it...
		        } catch (AuthenticatorException e) {
		            // Handle it...
		        } catch (IOException e) {
		            // Handle it...
		        }			
		}
		
	}
	
	public java.io.File downloadGFile(Drive drive, String token, File gFile, java.io.File jFolder){
		if(gFile.getDownloadUrl() != null && gFile.getDownloadUrl().length() > 0){
			if(jFolder == null){
				jFolder = new java.io.File(Environment.getExternalStorageDirectory().getPath() + "/tplt/");
				jFolder.mkdirs();
			}
			
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(gFile.getDownloadUrl());
			get.setHeader("Authorization", "Bearer " + token);
			try {
				HttpResponse response = client.execute(get);
				
				InputStream is = response.getEntity().getContent();
				jFolder.mkdirs();
				
				java.io.File file = new java.io.File(jFolder.getAbsolutePath() + gFile.getTitle());
				
				FileOutputStream os = new FileOutputStream(file);
				byte[] bur = new byte[1024];
				int read = 0;
				while((read = is.read(bur)) > 0){
					os.write(bur, 0, read);
				}
				os.close();
				is.close();
				return file;
				
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		return null;
	}
}
