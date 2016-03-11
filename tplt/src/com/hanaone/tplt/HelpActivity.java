package com.hanaone.tplt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HelpActivity extends Activity {
	private Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_help);
	}

    public void onClick(View v){
    	switch (v.getId()) {
		case R.id.btn_home:
			startActivity(new Intent(mContext, MainActivity.class));
			break;

		default:
			break;
		}
    }	
}
