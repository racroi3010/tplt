package com.hanaone.tplt.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceHandler {
	private static final String PREFERENCE_NAME = "TPLT";
	
	private static final String AUDIO_AUTO_PLAY = "AUDIO_AUTO_PLAY";
	
	private static void removeAllPreference(Context context){
		SharedPreferences pref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();		
		editor.remove(AUDIO_AUTO_PLAY);
		
		editor.apply();
	}	
	
	private static void setBooleanPreference(Context context, String key, boolean value){
		SharedPreferences pref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		
		editor.putBoolean(key, value);
		
		editor.apply();
	}
	private static boolean getBooleanPreference(Context context, String key){
		return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).getBoolean(key, false);
	}
	
	
	public static void setAudioPlayPreference(Context context, boolean value){
		setBooleanPreference(context, AUDIO_AUTO_PLAY, value);
	}
	public static boolean getAudioPlayPreference(Context context){
		return getBooleanPreference(context, AUDIO_AUTO_PLAY);
	}

}
