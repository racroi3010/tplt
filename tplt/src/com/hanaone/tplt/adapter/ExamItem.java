package com.hanaone.tplt.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public interface ExamItem {
	public int getViewType();
	public View getView(LayoutInflater inflater, View convertView, ViewGroup parent);
}
