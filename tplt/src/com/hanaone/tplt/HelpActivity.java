package com.hanaone.tplt;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.hanaone.tplt.util.PreferenceHandler;
import com.kyleduo.switchbutton.SwitchButton;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class HelpActivity extends Activity {
	private Context mContext;
	private Spinner spLanguage;
	private boolean userIsInteracting;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		mContext = this;
		int position = PreferenceHandler.getLanguagePosition(mContext);
		switch (position) {
		case 0:
			setLocale("en");
			break;
		case 1:
			setLocale("vi");
			break;
		case 2:
			setLocale("ko");
			break;
		default:
			break;
		}			
		setContentView(R.layout.activity_help);

		SwitchButton swAudio = (SwitchButton) findViewById(R.id.sw_help_audio);
		swAudio.setChecked(PreferenceHandler.getAudioPlayPreference(mContext));
		swAudio.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				PreferenceHandler.setAudioPlayPreference(mContext, isChecked);
			}
		});

		SwitchButton swHint = (SwitchButton) findViewById(R.id.sw_help_hint);
		swHint.setChecked(PreferenceHandler.getHintDisplayPreference(mContext));
		swHint.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				PreferenceHandler.setHintDisplayPreference(mContext, isChecked);
			}
		});

		spLanguage = (Spinner) findViewById(R.id.sp_language);
		
		spLanguage.setSelection(position);	
		
		spLanguage.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (userIsInteracting) {
					switch (arg2) {
					case 0:
						setLocale("en");
						break;
					case 1:
						setLocale("vi");
						break;
					case 2:
						setLocale("ko");
						break;
					default:
						break;
					}
					userIsInteracting = false;
					Intent refresh = new Intent(mContext, HelpActivity.class);
					refresh.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
					refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(refresh);					
					
				}
				PreferenceHandler.setLanguagePosition(mContext, arg2);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});
	}


	@Override
	public void onUserInteraction() {
		super.onUserInteraction();
		userIsInteracting = true;
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_home:
			startActivity(new Intent(mContext, MainActivity.class).addFlags(
					Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(
					Intent.FLAG_ACTIVITY_NEW_TASK));
			break;
		case R.id.btn_rate:
			rateApp();
		case R.id.btn_share:
			shareApp();
			break;
		default:
			break;
		}
	}

	private void shareApp() {
//		Intent normalIntent = new Intent(Intent.ACTION_SEND);
//		normalIntent.setType("text/plain");
//		normalIntent.setPackage("com.katana.facebook"); // I just know the package of Facebook, the rest you will have to search for or use my method.
//		normalIntent.putExtra(Intent.EXTRA_TEXT, "The text you want to share to Facebook");		
		List<Intent> targetedShareIntents = new ArrayList<Intent>();	
		Intent facebook = getShareIntent("facebook", "subject", "text");
		if(facebook != null){
			targetedShareIntents.add(facebook);
		}
		Intent twitterintent = getShareIntent("twitter", "subject", "text");
		if(twitterintent != null){
			targetedShareIntents.add(twitterintent);
		}		
		Intent plus = getShareIntent("plus", "subject", "text");
		if(plus != null){
			targetedShareIntents.add(plus);
		}	
		
		
		Intent chooser = Intent.createChooser(targetedShareIntents.remove(0), "abc");
		chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[]{}));
		startActivity(chooser);
	}

	private Intent getShareIntent(String type, String subject, String text){
		boolean found = false;
		Intent share = new Intent(Intent.ACTION_SEND);
		share.setType("text/plain");
		
		List<ResolveInfo> resInfo = mContext.getPackageManager().queryIntentActivities(share, 0);
		if(!resInfo.isEmpty()){
			for(ResolveInfo info: resInfo){
				if(info.activityInfo.packageName.toLowerCase().contains(type)
						|| info.activityInfo.name.toLowerCase().contains(type)){
					share.putExtra(Intent.EXTRA_SUBJECT, subject);
					share.putExtra(Intent.EXTRA_TEXT, text);
					share.setPackage(info.activityInfo.packageName);
					found = true;
					break;
				}
			}
			if(!found){
				return null;
			}
			return share;
		}
		return null;
	}
	private void setLocale(String language) {
		Locale myLocale = new Locale(language);
		Resources res = getResources();
		DisplayMetrics dm = res.getDisplayMetrics();
		Configuration conf = res.getConfiguration();
		conf.locale = myLocale;
		res.updateConfiguration(conf, dm);

	}

	private void rateApp() {
		Uri uri = Uri.parse("market://details?id=" + mContext.getPackageName());
		Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
		// To count with Play market backstack, After pressing back button,
		// to taken back to our application, we need to add following flags to
		// intent.
		goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY
				| Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET
				| Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
		try {
			startActivity(goToMarket);
		} catch (ActivityNotFoundException e) {
			startActivity(new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://play.google.com/store/apps/details?id="
							+ mContext.getPackageName())));
		}
	}
}
