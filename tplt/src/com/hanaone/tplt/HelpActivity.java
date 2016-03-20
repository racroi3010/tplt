package com.hanaone.tplt;

import com.hanaone.tplt.util.PreferenceHandler;
import com.kyleduo.switchbutton.SwitchButton;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class HelpActivity extends Activity {
	private Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_help);
		
		SwitchButton swAudio = (SwitchButton) findViewById(R.id.sw_help_audio);
		swAudio.setChecked(PreferenceHandler.getAudioPlayPreference(mContext));
		swAudio.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				PreferenceHandler.setAudioPlayPreference(mContext, isChecked);
			}
		});
		
		SwitchButton swHint = (SwitchButton) findViewById(R.id.sw_help_hint);
		swHint.setChecked(PreferenceHandler.getHintDisplayPreference(mContext));
		swHint.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				PreferenceHandler.setHintDisplayPreference(mContext, isChecked);
			}
		});
	}

    public void onClick(View v){
    	switch (v.getId()) {
		case R.id.btn_home:
			startActivity(new Intent(mContext, MainActivity.class)
						.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
			break;
		case R.id.btn_rate:
			rateApp();
			
		default:
			break;
		}
    }	
    
    private void rateApp(){
        Uri uri = Uri.parse("market://details?id=" + mContext.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button, 
        // to taken back to our application, we need to add following flags to intent. 
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + mContext.getPackageName())));
        }    	
    }
}
