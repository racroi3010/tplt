package com.hanaone.tplt.adapter;

import com.hanaone.tplt.db.LevelDataSet;

public interface ListLevelListener {
	public void onSelect(int examLevelId, String examLevelName);
	public void onSelect(LevelDataSet level, DownloadInfo info);
}
