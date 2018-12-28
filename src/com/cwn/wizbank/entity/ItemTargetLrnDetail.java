package com.cwn.wizbank.entity;



public class ItemTargetLrnDetail implements java.io.Serializable {
	private static final long serialVersionUID = -98241428486329451L;

		Integer itd_itm_id;

		Integer itd_usr_ent_id;
		/**
		 * 用户组关键维度
		 **/
		Integer itd_group_ind;
		/**
		 * 职级关键维度
		 **/
		Integer itd_grade_ind;
		/**
		 * 岗位关键
		 **/
		Integer itd_position_ind;
		/**
		 * 是否为必修
		 **/
		Integer itd_compulsory_ind;
		
		AeItem item;
		
		RegUser user;
		
		AeApplication app;
		
		AeAttendanceStatus attendanceStatus;
		
		CourseEvaluation cov;

		public ItemTargetLrnDetail(){
		}
	
		public CourseEvaluation getCov() {
			return cov;
		}

		public void setCov(CourseEvaluation cov) {
			this.cov = cov;
		}

		public Integer getItd_itm_id(){
			return this.itd_itm_id;
		}		
		public void setItd_itm_id(Integer itd_itm_id){
			this.itd_itm_id = itd_itm_id;
		}
		public Integer getItd_usr_ent_id(){
			return this.itd_usr_ent_id;
		}		
		public void setItd_usr_ent_id(Integer itd_usr_ent_id){
			this.itd_usr_ent_id = itd_usr_ent_id;
		}
		public Integer getItd_group_ind(){
			return this.itd_group_ind;
		}		
		public void setItd_group_ind(Integer itd_group_ind){
			this.itd_group_ind = itd_group_ind;
		}
		public Integer getItd_grade_ind(){
			return this.itd_grade_ind;
		}		
		public void setItd_grade_ind(Integer itd_grade_ind){
			this.itd_grade_ind = itd_grade_ind;
		}
		public Integer getItd_position_ind(){
			return this.itd_position_ind;
		}		
		public void setItd_position_ind(Integer itd_position_ind){
			this.itd_position_ind = itd_position_ind;
		}
		public Integer getItd_compulsory_ind(){
			return this.itd_compulsory_ind;
		}		
		public void setItd_compulsory_ind(Integer itd_compulsory_ind){
			this.itd_compulsory_ind = itd_compulsory_ind;
		}

		public AeItem getItem() {
			return item;
		}

		public void setItem(AeItem item) {
			this.item = item;
		}

		public RegUser getUser() {
			return user;
		}

		public void setUser(RegUser user) {
			this.user = user;
		}

		public AeApplication getApp() {
			return app;
		}

		public void setApp(AeApplication app) {
			this.app = app;
		}

		public AeAttendanceStatus getAttendanceStatus() {
			return attendanceStatus;
		}

		public void setAttendanceStatus(AeAttendanceStatus attendanceStatus) {
			this.attendanceStatus = attendanceStatus;
		}


}