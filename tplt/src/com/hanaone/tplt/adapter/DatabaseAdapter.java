package com.hanaone.tplt.adapter;

import java.util.ArrayList;
import java.util.List;

import com.hanaone.tplt.db.ExamDataSet;
import com.hanaone.tplt.db.LevelDataSet;
import com.hanaone.tplt.db.dataset.ExamLevel;
import com.hanaone.tplt.db.dataset.Examination;
import com.hanaone.tplt.db.dataset.FileExtra;
import com.hanaone.tplt.db.dataset.Level;
import com.hanaone.tplt.util.DatabaseUtils;

import android.content.Context;

public class DatabaseAdapter{
	private Context mContext;
	private DatabaseHelper dbHelper;
	public DatabaseAdapter(Context context){
		this.mContext = context;
		this.dbHelper = new DatabaseHelper(mContext);
	}
	
	public List<ExamDataSet> getAllExam(){
		List<Examination> examModels = dbHelper.selectAllExam();
		List<ExamDataSet> list = new ArrayList<ExamDataSet>();
		for(Examination exam: examModels){
			ExamDataSet data = DatabaseUtils.examModel2Pojo(exam);
			List<LevelDataSet> levels = new ArrayList<LevelDataSet>();
			
			List<ExamLevel> levelModels = dbHelper.selectExamLevelByExamNumber(exam.getNumber());
			if(levelModels != null){
				for(ExamLevel lmodel: levelModels){
					LevelDataSet l = new LevelDataSet();
					l.setId(lmodel.getId());
					
					Level levelModel = dbHelper.selectLevelById(lmodel.getLevel_id());
					if(levelModel != null){
						l.setNumber(levelModel.getNumber());
						l.setLabel(levelModel.getLabel());					
					}
					boolean active = lmodel.getActive() == 0 ? false : true;
					l.setActive(active);
					
					FileExtra audio = dbHelper.selectFileById(lmodel.getAudio_id());
					l.setAudio(DatabaseUtils.fileModel2Pojo(audio));
					
					FileExtra pdf = dbHelper.selectFileById(lmodel.getPdf_id());
					l.setPdf(DatabaseUtils.fileModel2Pojo(pdf));				
					levels.add(l);
				}
				
				
				data.setLevels(levels);
				
				list.add(data);
			}

			
		}
		return list;
	}
	public boolean checkExam(int examNumber){
		Examination exam = dbHelper.selectExamByNumber(examNumber);
		
		if(exam != null) return true;
		return false;
		
	}
	public void addExam(ExamDataSet examDataSet){
		Examination exam = DatabaseUtils.examPojo2Model(examDataSet);
		dbHelper.insert(exam);
		
		List<LevelDataSet> levels = examDataSet.getLevels();
		if(levels != null){
			for(LevelDataSet data: levels){
				Level level = dbHelper.selectLevelByNumber(data.getNumber());
				if(level == null){
					level = new Level();
					level.setNumber(data.getNumber());
					level.setLabel(data.getLabel());
					level.setId((int)dbHelper.insert(level));
				}
				
				ExamLevel examLevel = new ExamLevel();
				examLevel.setExam_id(exam.getNumber());
				examLevel.setLevel_id(level.getId());

				examLevel.setAudio_id(-1);
				examLevel.setPdf_id(-1);
				
				examLevel.setActive(0);
				examLevel.setUrl(data.getUrl());
				dbHelper.insert(examLevel);
			}
		}
		
		
	}
}
