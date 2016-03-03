package com.hanaone.tplt.adapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.hanaone.gg.DownloadHelper;
import com.hanaone.tplt.Constants;
import com.hanaone.tplt.db.ChoiceDataSet;
import com.hanaone.tplt.db.ExamDataSet;
import com.hanaone.tplt.db.FileDataSet;
import com.hanaone.tplt.db.LevelDataSet;
import com.hanaone.tplt.db.QuestionDataSet;
import com.hanaone.tplt.db.SectionDataSet;
import com.hanaone.tplt.db.model.Choice;
import com.hanaone.tplt.db.model.ExamLevel;
import com.hanaone.tplt.db.model.Examination;
import com.hanaone.tplt.db.model.FileExtra;
import com.hanaone.tplt.db.model.Level;
import com.hanaone.tplt.db.model.Question;
import com.hanaone.tplt.db.model.Section;
import com.hanaone.tplt.util.DatabaseUtils;

import android.content.Context;

public class DatabaseAdapter{
	private Context mContext;
	private DatabaseHelper dbHelper;
	private DownloadHelper dlHelper;
	public DatabaseAdapter(Context context){
		this.mContext = context;
		this.dbHelper = new DatabaseHelper(mContext);
		this.dlHelper = new DownloadHelper(mContext);
	}
	
	public List<ExamDataSet> getAllExam(){
		List<Examination> examModels = dbHelper.selectAllExam();
		List<ExamDataSet> list = new ArrayList<ExamDataSet>();
		for(Examination exam: examModels){
			ExamDataSet data = DatabaseUtils.convertObject(exam, ExamDataSet.class);
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
					l.setAudio(DatabaseUtils.convertObject(audio, FileDataSet.class));
					
					FileExtra pdf = dbHelper.selectFileById(lmodel.getPdf_id());
					l.setPdf(DatabaseUtils.convertObject(pdf, FileDataSet.class));	
					
					FileExtra txt = dbHelper.selectFileById(lmodel.getTxt_id());
					l.setTxt(DatabaseUtils.convertObject(txt, FileDataSet.class));
					
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
		//Examination exam = DatabaseUtils.examPojo2Model(examDataSet);
		Examination exam = DatabaseUtils.convertObject(examDataSet, Examination.class);
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

				
				examLevel.setAudio_id((int)dbHelper.insert(DatabaseUtils.convertObject(data.getAudio(), FileExtra.class)));
				examLevel.setPdf_id((int)dbHelper.insert(DatabaseUtils.convertObject(data.getPdf(), FileExtra.class)));
				examLevel.setTxt_id((int)dbHelper.insert(DatabaseUtils.convertObject(data.getTxt(), FileExtra.class)));
				
				examLevel.setActive(0);
				
				data.setId((int)dbHelper.insert(examLevel));
			}
		}
		
		
	}
	
	public LevelDataSet getLevel(int levelId){
		LevelDataSet levelDataSet = new LevelDataSet();
		
		ExamLevel examLevelModel = dbHelper.selectExamLevelById(levelId);
		Level levelModel = dbHelper.selectLevelById(examLevelModel.getLevel_id());
		if(levelModel != null){
			levelDataSet.setNumber(levelModel.getNumber());
			levelDataSet.setLabel(levelModel.getLabel());					
		}
		boolean active = examLevelModel.getActive() == 0 ? false : true;
		levelDataSet.setActive(active);
		
		FileExtra audio = dbHelper.selectFileById(examLevelModel.getAudio_id());
		levelDataSet.setAudio(DatabaseUtils.convertObject(audio, FileDataSet.class));
		
		FileExtra pdf = dbHelper.selectFileById(examLevelModel.getPdf_id());
		levelDataSet.setPdf(DatabaseUtils.convertObject(pdf, FileDataSet.class));	
		
		FileExtra txt = dbHelper.selectFileById(examLevelModel.getTxt_id());
		levelDataSet.setTxt(DatabaseUtils.convertObject(txt, FileDataSet.class));	
		
		List<Section> sections = dbHelper.selectSectionByExamLevelId(examLevelModel.getId());
		List<SectionDataSet> sectiondatas = new ArrayList<SectionDataSet>();
		for(Section section:sections){
			SectionDataSet sectionData = DatabaseUtils.convertObject(section, SectionDataSet.class);
			
			List<Question> questionModels = dbHelper.selectQuestionBySectionId(section.getId());
			List<QuestionDataSet> questionDatas = new ArrayList<QuestionDataSet>();
			for(Question questionModel: questionModels){
				QuestionDataSet questionData = DatabaseUtils.convertObject(questionModel, QuestionDataSet.class);
				List<Choice> choiceModels = dbHelper.selectChoiceByQuestionId(questionModel.getId());
				List<ChoiceDataSet> choiceDatas = new ArrayList<ChoiceDataSet>();
				for(Choice choiceModel: choiceModels){
					ChoiceDataSet choiceData = DatabaseUtils.convertObject(choiceModel, ChoiceDataSet.class);
					choiceDatas.add(choiceData);
				}
				questionData.setChoices(choiceDatas);
				questionDatas.add(questionData);
			}
			sectionData.setQuestions(questionDatas);
			
			sectiondatas.add(sectionData);
		}
		levelDataSet.setSections(sectiondatas);
		
		return levelDataSet;
	}
	public int updateLevelActive(int levelId, boolean active){
		ExamLevel levelModel = this.dbHelper.selectExamLevelById(levelId);
		if(levelModel != null){
			if(active){
				levelModel.setActive(1);
			} else {
				levelModel.setActive(0);
			}
			return this.dbHelper.update(levelModel);
		}
		return -1;
	}
	public void addSection(SectionDataSet data, int levelId){
		Section model = DatabaseUtils.convertObject(data, Section.class);
		model.setExam_level_id(levelId);	
		long sectionId = dbHelper.insert(model);
		
		
		List<QuestionDataSet> questions = data.getQuestions();
		for(QuestionDataSet questionData: questions){
			Question questionModel = DatabaseUtils.convertObject(questionData, Question.class);
			questionModel.setSection_id((int)sectionId);
			long questionId = dbHelper.insert(questionModel);
			
			List<ChoiceDataSet> choiceDatas = questionData.getChoices();
			for(ChoiceDataSet choiceData: choiceDatas){
				Choice choice = DatabaseUtils.convertObject(choiceData, Choice.class);
				choice.setQuestion_id((int)questionId);
				
				// image type
				if(Constants.FILE_TYPE_IMG.equals(questionData.getChoiceType())){
					// download
					File folder = mContext.getDir("tplt", Context.MODE_PRIVATE);
					String path = folder.getAbsolutePath() + "/file/";
					File file = new File(path);
					if(!file.exists()){
						file.mkdirs();
					}
					
					path += "img_" + sectionId + "_" + questionId + "_" +  choiceData.getLabel() + ".jpg";
					try {
						this.dlHelper.downloadFile(choice.getText(), path);
						choice.setText(path);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				dbHelper.insert(choice);
			}
		}
		
	}
}
