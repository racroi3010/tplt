package com.hanaone.tplt.adapter;

import android.widget.ProgressBar;
import android.widget.TextView;

public class DownloadInfo {
	private volatile TextView txtView1;
	private volatile ProgressBar prgBar1;
	
	private volatile TextView txtView2;
	private volatile ProgressBar prgBar2;
	
	private volatile TextView txtView3;
	private volatile ProgressBar prgBar3;
	public  TextView getTxtView1() {
		return txtView1;
	}
	public void setTxtView1(TextView txtView1) {
		this.txtView1 = txtView1;
	}
	public ProgressBar getPrgBar1() {
		return prgBar1;
	}
	public void setPrgBar1(ProgressBar prgBar1) {
		this.prgBar1 = prgBar1;
	}
	public TextView getTxtView2() {
		return txtView2;
	}
	public void setTxtView2(TextView txtView2) {
		this.txtView2 = txtView2;
	}
	public ProgressBar getPrgBar2() {
		return prgBar2;
	}
	public void setPrgBar2(ProgressBar prgBar2) {
		this.prgBar2 = prgBar2;
	}
	public TextView getTxtView3() {
		return txtView3;
	}
	public void setTxtView3(TextView txtView3) {
		this.txtView3 = txtView3;
	}
	public ProgressBar getPrgBar3() {
		return prgBar3;
	}
	public void setPrgBar3(ProgressBar prgBar3) {
		this.prgBar3 = prgBar3;
	}
	
	
	
	
}
