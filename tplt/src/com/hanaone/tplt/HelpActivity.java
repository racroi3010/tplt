package com.hanaone.tplt;

import com.hanaone.tplt.util.PreferenceHandler;
import com.kyleduo.switchbutton.SwitchButton;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class HelpActivity extends Activity {
	private Context mContext;
	private SwitchButton swAudio;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_help);
		
		swAudio = (SwitchButton) findViewById(R.id.sw_help_audio);
		swAudio.setChecked(PreferenceHandler.getAudioPlayPreference(mContext));
		swAudio.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				PreferenceHandler.setAudioPlayPreference(mContext, isChecked);
			}
		});
	}

    public void onClick(View v){
    	switch (v.getId()) {
		case R.id.btn_home:
			startActivity(new Intent(mContext, MainActivity.class)
						.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
			break;

		default:
			break;
		}
    }	
}
