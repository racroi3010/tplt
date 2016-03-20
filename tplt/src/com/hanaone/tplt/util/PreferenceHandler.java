package com.hanaone.tplt.util;

import com.hanaone.tplt.Constants;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceHandler extends Constants {
	private static final String PREFERENCE_NAME = "TPLT";
	
	private static final String AUDIO_AUTO_PLAY = "AUDIO_AUTO_PLAY";
	private static final String HINT_DISPLAY = "HINT_DISPLAY";
	
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
	private static void setStringPreference(Context context, String key, String value){
		SharedPreferences pref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		
		editor.putString(key, value);
		
		editor.apply();
	}
	private static String getStringPreference(Context context, String key){
		return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).getString(key, null);
	}	
	
	public static void setAudioPlayPreference(Context context, boolean value){
		setBooleanPreference(context, AUDIO_AUTO_PLAY, value);
	}
	public static boolean getAudioPlayPreference(Context context){
		return getBooleanPreference(context, AUDIO_AUTO_PLAY);
	}

	public static void setHintDisplayPreference(Context context, boolean value){
		setBooleanPreference(context, HINT_DISPLAY, value);
	}
	public static boolean getHintDisplayPreference(Context context){
		return getBooleanPreference(context, HINT_DISPLAY);
	}
	
	public static void setQuestionModePreference(Context context, String value){
		setStringPreference(context, QUESTION_MODE, value);
	}
	public static String getQuestionModePreference(Context context){
		return getStringPreference(context, QUESTION_MODE);
	}
}
