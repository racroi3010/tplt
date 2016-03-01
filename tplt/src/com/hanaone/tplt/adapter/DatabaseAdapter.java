package com.hanaone.tplt.adapter;

import java.util.ArrayList;
import java.util.List;

import com.hanaone.tplt.db.ChoiceDataSet;
import com.hanaone.tplt.db.ExamDataSet;
import com.hanaone.tplt.db.LevelDataSet;
import com.hanaone.tplt.db.QuestionDataSet;
import com.hanaone.tplt.db.SectionDataSet;
import com.hanaone.tplt.db.dataset.Choice;
import com.hanaone.tplt.db.dataset.ExamLevel;
import com.hanaone.tplt.db.dataset.Examination;
import com.hanaone.tplt.db.dataset.FileExtra;
import com.hanaone.tplt.db.dataset.Level;
import com.hanaone.tplt.db.dataset.Question;
import com.hanaone.tplt.db.dataset.Section;
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
					
					FileExtra txt = dbHelper.selectFileById(lmodel.getTxt_id());
					l.setTxt(DatabaseUtils.fileModel2Pojo(txt));
					
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

				
				examLevel.setAudio_id((int)dbHelper.insert(DatabaseUtils.filePojo2Model(data.getAudio())));
				examLevel.setPdf_id((int)dbHelper.insert(DatabaseUtils.filePojo2Model(data.getPdf())));
				examLevel.setTxt_id((int)dbHelper.insert(DatabaseUtils.filePojo2Model(data.getTxt())));
				
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
		levelDataSet.setAudio(DatabaseUtils.fileModel2Pojo(audio));
		
		FileExtra pdf = dbHelper.selectFileById(examLevelModel.getPdf_id());
		levelDataSet.setPdf(DatabaseUtils.fileModel2Pojo(pdf));	
		
		FileExtra txt = dbHelper.selectFileById(examLevelModel.getTxt_id());
		levelDataSet.setTxt(DatabaseUtils.fileModel2Pojo(txt));	
		
		List<Section> sections = dbHelper.selectSectionByExamLevelId(examLevelModel.getId());
		List<SectionDataSet> sectiondatas = new ArrayList<SectionDataSet>();
		for(Section section:sections){
			SectionDataSet sectionData = DatabaseUtils.sectionModel2Pojo(section);
			
			List<Question> questionModels = dbHelper.selectQuestionBySectionId(section.getId());
			List<QuestionDataSet> questionDatas = new ArrayList<QuestionDataSet>();
			for(Question questionModel: questionModels){
				QuestionDataSet questionData = DatabaseUtils.questionModel2Pojo(questionModel);
				List<Choice> choiceModels = dbHelper.selectChoiceByQuestionId(questionModel.getId());
				List<ChoiceDataSet> choiceDatas = new ArrayList<ChoiceDataSet>();
				for(Choice choiceModel: choiceModels){
					ChoiceDataSet choiceData = DatabaseUtils.choiceModel2Pojo(choiceModel);
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
	
	public void addSection(SectionDataSet data, int levelId){
		Section model = DatabaseUtils.sectionPojo2Model(data);
		model.setExam_level_id(levelId);	
		long sectionId = dbHelper.insert(model);
		
		
		List<QuestionDataSet> questions = data.getQuestions();
		for(QuestionDataSet questionData: questions){
			Question questionModel = DatabaseUtils.questionPojo2Model(questionData);
			questionModel.setSection_id((int)sectionId);
			long questionId = dbHelper.insert(questionModel);
			
			List<ChoiceDataSet> choiceDatas = questionData.getChoices();
			for(ChoiceDataSet choiceData: choiceDatas){
				Choice choice = DatabaseUtils.choicePojo2Model(choiceData);
				choice.setQuestion_id((int)questionId);
				dbHelper.insert(choice);
			}
		}
		
	}
}
