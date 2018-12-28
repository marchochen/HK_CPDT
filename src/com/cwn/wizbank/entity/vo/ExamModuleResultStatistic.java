package com.cwn.wizbank.entity.vo;

import java.sql.Timestamp;
import java.util.List;

/**
 * 考试模块结构统计信息模型
 *	概括：每个考试统计模型除了考试情况基本信息外，还有已提交考试学员的情况（List<Learner>），而每个学员有多次提交考试的信息（List<CommitInfo>）
 * 	
 * @author andrew.xiao
 *
 */
public class ExamModuleResultStatistic {
	
	/**
	 * 课程标题如：xxx课程标题  > xxx班级标题  
	 */
	private String item_title;
	
	/**
	 * 模块标题
	 */
	private String module_title;
	
	/**
	 * 报名总数
	 */
	private int enroll_count;
	
	/**
	 * 总的提交次数，每个人可以提交多次
	 */
	private int commit_count;
	
	/**
	 * 试卷分数
	 */
	private float score;
	
	/**
	 * 提交的学员数，一个学员提交多次，还是算一个人
	 */
	private int commit_learner_count;
	
	/**
	 * 人均提交次数 =  提交总人次/已提交人数
	 */
	private float per_learner_commit_count;
	
	/**
	 * 最高分
	 */
	private float max_score;
	
	/**
	 * 合格人数
	 */
	private int pass_count;
	
	/**
	 * 最低分
	 */
	private float min_score;
	
	/**
	 * 不合格人数
	 */
	private int no_pass_count;
	
	/**
	 * 平均分
	 */
	private float average;
	
	/**
	 * 未提交人数
	 */
	private int un_commit_count;
	
	/**
	 * 已提交人数平均分
	 * 已提交人数平均分 = 每个人的最好成绩加总/已提交人数
	 */
	private float average_for_commited_learner;
	
	/**
	 * 已提交考试的学员集合
	 */
	private List<Learner> learnerList;
	
	public String getItem_title() {
		return item_title;
	}

	public void setItem_title(String item_title) {
		this.item_title = item_title;
	}

	public String getModule_title() {
		return module_title;
	}

	public void setModule_title(String module_title) {
		this.module_title = module_title;
	}

	public int getEnroll_count() {
		return enroll_count;
	}

	public void setEnroll_count(int enroll_count) {
		this.enroll_count = enroll_count;
	}

	public int getCommit_count() {
		return commit_count;
	}

	public void setCommit_count(int commit_count) {
		this.commit_count = commit_count;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public int getCommit_learner_count() {
		return commit_learner_count;
	}

	public void setCommit_learner_count(int commit_learner_count) {
		this.commit_learner_count = commit_learner_count;
	}

	public float getPer_learner_commit_count() {
		return per_learner_commit_count;
	}

	public void setPer_learner_commit_count(float per_learner_commit_count) {
		this.per_learner_commit_count = per_learner_commit_count;
	}

	public float getMax_score() {
		return max_score;
	}

	public void setMax_score(float max_score) {
		this.max_score = max_score;
	}

	public int getPass_count() {
		return pass_count;
	}

	public void setPass_count(int pass_count) {
		this.pass_count = pass_count;
	}

	public float getMin_score() {
		return min_score;
	}

	public void setMin_score(float min_score) {
		this.min_score = min_score;
	}

	public int getNo_pass_count() {
		return no_pass_count;
	}

	public void setNo_pass_count(int no_pass_count) {
		this.no_pass_count = no_pass_count;
	}

	public float getAverage() {
		return average;
	}

	public void setAverage(float average) {
		this.average = average;
	}

	public int getUn_commit_count() {
		return un_commit_count;
	}


	public void setUn_commit_count(int un_commit_count) {
		this.un_commit_count = un_commit_count;
	}

	public float getAverage_for_commited_learner() {
		return average_for_commited_learner;
	}

	public void setAverage_for_commited_learner(float average_for_commited_learner) {
		this.average_for_commited_learner = average_for_commited_learner;
	}

	public List<Learner> getLearnerList() {
		return learnerList;
	}

	public void setLearnerList(List<Learner> learnerList) {
		this.learnerList = learnerList;
	}

	/**
	 * 提交的学员情况
	 * @author
	 *
	 */
	public class Learner{
		
		/**
		 * 用户名
		 */
		public String usr_ste_usr_id;
		
		/**
		 * 全名
		 */
		public String usr_display_bil;
		
		/**
		 * 用户组
		 */
		public String usr_group;
		
		/**
		 * 每个学员可以提交多次考试，这里的max_score表示每个学员在所有提交记录中的最高分
		 */
		public float max_score;
		
		/**
		 * 是否合格
		 */
		public String is_passed;
		
		/**
		 * 学员提交信息集合（一个学员可以提交多次考试）
		 */
		public List<CommitInfo> commitInfoList;
		
		/**
		 * 
		 * 学员每次提交的情况
		 *
		 */
		public class CommitInfo {
			/**
			 * 提交时间
			 */
			public Timestamp commit_time;
			
			/**
			 * 是否合格
			 */
			public String is_passed;
			
			/**
			 * 分数
			 */
			public float score;
		}
		
	}
	
}
