/*
Auth: Shelley
Date: 2008-04-15
Desc: bug5531
*/
update DisplayOption set dpo_title_ind = 1, dpo_desc_ind = 1 where dpo_view = 'IST_EDIT' and dpo_res_subtype = 'NETG_COK' and dpo_res_type = 'MOD'

/*
Auth: Tim
Date: 2008-07-30
Desc: for my staff report
*/

INSERT INTO ReportTemplate
  (rte_title_xml,
   rte_type,
   rte_get_xsl,
   rte_exe_xsl,
   rte_dl_xsl,
   rte_meta_data_url,
   rte_seq_no,
   rte_owner_ent_id,
   rte_create_usr_id,
   rte_create_timestamp,
   rte_upd_usr_id,
   rte_upd_timestamp)
  SELECT rte_title_xml,
         'STAFF',
         rte_get_xsl,
         rte_exe_xsl,
         rte_dl_xsl,
         rte_meta_data_url,
         rte_seq_no,
         rte_owner_ent_id,
         rte_create_usr_id,
         sysdate,
         rte_upd_usr_id,
         sysdate
  
    FROM ReportTemplate
   WHERE rte_type = 'LEARNER';

alter table ReportSpec add rsp_content nclob

/*
Auth: Tim
Date: 2008-08-02
Desc: create table  aeTreeNodeRelation
*/
create table aeTreeNodeRelation
(
tnr_child_tnd_id int not null,
tnr_ancestor_tnd_id int not null,
tnr_type nvarchar2(20) not null,
tnr_order int not null,
tnr_parent_ind int not null,
tnr_remain_on_syn int not null,
tnr_create_timestamp timestamp not null,
tnr_create_usr_id nvarchar2(20) not null
);
ALTER TABLE aeTreeNodeRelation ADD CONSTRAINT
	PK_tnr PRIMARY KEY  
	(
	tnr_child_tnd_id,
	tnr_ancestor_tnd_id
	);
ALTER TABLE aeTreeNodeRelation ADD CONSTRAINT
	FK_tnr_ent1 FOREIGN KEY
	(
	tnr_child_tnd_id
	) REFERENCES aeTreeNode
	(
	tnd_id
	);
ALTER TABLE aeTreeNodeRelation  ADD CONSTRAINT
	FK_tnr_ent2 FOREIGN KEY
	(
	tnr_ancestor_tnd_id
	) REFERENCES aeTreeNode
	(
	tnd_id
	);
CREATE 
  INDEX IX_tnr1 ON aeTreeNodeRelation (tnr_ancestor_tnd_id);

CREATE 
  INDEX IX_tnr2 ON aeTreeNodeRelation (tnr_parent_ind);




/*
Auth: Tim
Date: 2008-08-05
Desc: for study group
*/


create table studyGroup
(
sgp_id int not null ,
sgp_tcr_id int not null,
sgp_title  nvarchar2(100) not null,
sgp_desc nvarchar2(255),
sgp_public_type int not null,
sgp_create_usr_id nvarchar2(20) not null,
sgp_create_timestamp timestamp not null,
sgp_upd_timestamp timestamp not null
);
create sequence studyGroup_seq;
CREATE OR REPLACE TRIGGER studyGroup_trigger BEFORE INSERT ON studyGroup
FOR EACH ROW
WHEN (new.sgp_id IS NULL)
BEGIN
 SELECT studyGroup_seq.NEXTVAL INTO :new.sgp_id FROM DUAL;
END;

ALTER TABLE studyGroup ADD CONSTRAINT
	PK_sgp PRIMARY KEY  
	(
	sgp_id
	);
ALTER TABLE studyGroup  ADD CONSTRAINT
	FK_tc_id FOREIGN KEY
	(
	sgp_tcr_id 
	) REFERENCES tcTrainingCenter
	(
	tcr_id
	);
  

create table studyGroupMember
(
sgm_id  int  not null,
sgm_sgp_id int not null,
sgm_ent_id  int not null,
sgm_type  nvarchar2(20) not null,
sgm_status  nvarchar2(20) not null,
sgm_create_timestamp timestamp not null,
sgm_create_usr_id nvarchar2(20) not null,
sgm_upd_timestamp timestamp not null,
sgm_upd_usr_id nvarchar2(20) not null

);

create sequence studyGroupMember_seq;
CREATE OR REPLACE TRIGGER studyGroupMember_trigger BEFORE INSERT ON studyGroupMember
FOR EACH ROW
WHEN (new.sgm_id IS NULL)
BEGIN
 SELECT studyGroupMember_seq.NEXTVAL INTO :new.sgm_id FROM DUAL;
END;


ALTER TABLE studyGroupMember ADD CONSTRAINT
	PK_sgm PRIMARY KEY  
	(
	sgm_id
	);
ALTER TABLE studyGroupMember  ADD CONSTRAINT
	FK_sgp_id FOREIGN KEY
	(
	sgm_sgp_id 
	) REFERENCES studyGroup
	(
	sgp_id
	);

CREATE 
  INDEX IX_sgm_ent_id ON studyGroupMember (sgm_ent_id );
CREATE 
  INDEX IX_sgm_type ON studyGroupMember (sgm_type );
CREATE 
  INDEX IX_sgm_status ON studyGroupMember (sgm_status );


create table studyGroupRelation
(
sgr_sgp_id int  not null ,
sgr_ent_id  int not null,
sgr_type   varchar(20) not null,
sgr_create_timestamp timestamp not null,
sgr_create_usr_id varchar(20) not null

);
	
ALTER TABLE studyGroupRelation ADD CONSTRAINT
	PK_sgr PRIMARY KEY  
	(
	sgr_sgp_id,
	sgr_ent_id,
	sgr_type
	);

ALTER TABLE studyGroupRelation  ADD CONSTRAINT
	FK_sgr_sgp_id FOREIGN KEY
	(
	sgr_sgp_id 
	) REFERENCES studyGroup
	(
	sgp_id
	);
  

create table studyGroupResources
(
sgs_id   int  not null,
sgs_title  nvarchar2(50) not null,
sgs_type  varchar(20) not null,
sgs_content   nvarchar2(255) ,
sgs_desc   nvarchar2(255) ,
sgs_create_timestamp timestamp not null,
sgs_create_usr_id varchar(20) not null,
sgs_upd_timestamp  timestamp not null,
sgs_upd_usr_id  varchar(20) not null

);
create sequence studyGroupResources_seq;
CREATE OR REPLACE TRIGGER studyGroupResources_trigger BEFORE INSERT ON studyGroupResources
FOR EACH ROW
WHEN (new.sgs_id IS NULL)
BEGIN
 SELECT studyGroupResources_seq.NEXTVAL INTO :new.sgs_id FROM DUAL;
END;

ALTER TABLE studyGroupResources ADD CONSTRAINT
	PK_sgs PRIMARY KEY  
	(
	sgs_id
	);


/*
Auth: Tim
Date: 2008-08-12
Desc: for study group forum
*/

alter table Module add mod_sgp_ind int  DEFAULT 0
;
update Module set mod_sgp_ind=0
;


	
/*
Auth: Dean
Date: 2008-07-31
Desc: module,know
*/
CREATE TABLE knowCatalog (
	kca_id int NOT NULL ,
	kca_tcr_id int NOT NULL ,
	kca_code nvarchar2 (50) ,
	kca_title nvarchar2 (255) NOT NULL ,
	kca_status nvarchar2 (10) NOT NULL ,
	kca_type nvarchar2 (50) NOT NULL ,
	kca_public_ind int NOT NULL ,
	kca_que_count int NULL ,
	kca_public_timestamp timestamp NOT NULL ,
	kca_create_usr_id nvarchar2 (20)  NOT NULL ,
	kca_create_timestamp timestamp NOT NULL ,
	kca_update_usr_id nvarchar2 (20)  NOT NULL ,
	kca_update_timestamp timestamp NOT NULL 
);

CREATE SEQUENCE knowCatalog_seq;

CREATE OR REPLACE TRIGGER knowCatalog_trigger BEFORE INSERT ON knowCatalog
FOR EACH ROW
WHEN (new.kca_id IS NULL)
BEGIN
 SELECT knowCatalog_seq.NEXTVAL INTO :new.kca_id FROM DUAL;
END;

ALTER TABLE knowCatalog ADD 
	CONSTRAINT PK_knowCatalog PRIMARY KEY   
	(
		kca_id
	) ;

 CREATE  INDEX IX_kca_tcr_code_title ON knowCatalog(kca_tcr_id, kca_code, kca_title) 
;
 CREATE  INDEX IX_kca_status_type_public ON knowCatalog(kca_status, kca_type, kca_public_ind) 
;

ALTER TABLE knowCatalog ADD 
	CONSTRAINT FK_kca_tcr_id FOREIGN KEY 
	(
		kca_tcr_id
	) REFERENCES tcTrainingCenter (
		tcr_id
	)
;

CREATE TABLE knowQuestion (
	que_id int NOT NULL ,
	que_kca_id int NOT NULL ,
	que_title nvarchar2 (255) NOT NULL ,
	que_content nclob NULL ,
	que_answered_ind int NOT NULL ,
	que_answered_timestamp timestamp NULL ,
	que_popular_ind int NOT NULL ,
	que_popular_timestamp timestamp NULL ,
	que_reward_credits int NOT NULL ,
	que_status nvarchar2 (20) NOT NULL ,
	que_create_ent_id int NOT NULL ,
	que_create_timestamp timestamp NOT NULL ,
	que_update_ent_id int NOT NULL ,
	que_update_timestamp timestamp NOT NULL 
) ;

CREATE SEQUENCE knowQuestion_seq;

CREATE OR REPLACE TRIGGER knowQuestion_trigger BEFORE INSERT ON knowQuestion
FOR EACH ROW
WHEN (new.que_id IS NULL)
BEGIN
 SELECT knowQuestion_seq.NEXTVAL INTO :new.que_id FROM DUAL;
END;

ALTER TABLE knowQuestion  ADD 
	CONSTRAINT PK_knowQuestion PRIMARY KEY   
	(
		que_id
	)  
;

 CREATE  INDEX IX_knowQuestion_kca_id ON knowQuestion(que_kca_id) 
;
 CREATE  INDEX IX_knowQuestion_title ON knowQuestion(que_title) 
;

ALTER TABLE knowQuestion ADD 
	CONSTRAINT FK_kque_kca1 FOREIGN KEY 
	(
		que_kca_id
	) REFERENCES knowCatalog (
		kca_id
	)
;

CREATE TABLE knowAnswer (
	ans_id int NOT NULL ,
	ans_que_id int NOT NULL ,
	ans_content nclob NOT NULL ,
	ans_refer_content nvarchar2(510) NULL,
	ans_right_ind int NOT NULL ,
	ans_vote_total int NULL ,
	ans_vote_for int NULL ,
	ans_vote_down int NULL ,
	ans_temp_vote_total int NULL,
	ans_temp_vote_for int NULL,
	ans_temp_vote_for_down_diff int NULL,
	ans_status nvarchar2 (20) NOT NULL ,
	ans_create_ent_id int NOT NULL ,
	ans_create_timestamp timestamp NOT NULL ,
	ans_update_ent_id int NOT NULL ,
	ans_update_timestamp timestamp NOT NULL 
);
CREATE SEQUENCE knowAnswer_seq;

CREATE OR REPLACE TRIGGER knowAnswer_trigger BEFORE INSERT ON knowAnswer
FOR EACH ROW
WHEN (new.ans_id IS NULL)
BEGIN
 SELECT knowAnswer_seq.NEXTVAL INTO :new.ans_id FROM DUAL;
END;

ALTER TABLE knowAnswer ADD 
	CONSTRAINT PK_knowAnswer PRIMARY KEY   
	(
		ans_id
	) 
;

 CREATE  INDEX IX_knowAnswer_que_id ON knowAnswer(ans_que_id)
;

ALTER TABLE knowAnswer ADD 
	CONSTRAINT FK_kans_kque1 FOREIGN KEY 
	(
		ans_que_id
	) REFERENCES knowQuestion (
		que_id
	);
	
ALTER TABLE knowAnswer ADD CONSTRAINT FK_kans_usr FOREIGN KEY 
	(
		ans_create_ent_id
	) REFERENCES RegUser (
		usr_ent_id
	)
;

--table creditsType
CREATE TABLE creditsType (
	cty_id int NOT NULL ,
	cty_code nvarchar2 (20) NOT NULL ,
	cty_title nvarchar2 (100) NOT NULL ,
	cty_deduction_ind int NOT NULL ,
	cty_manual_ind int NOT NULL ,
	cty_deleted_ind int NOT NULL ,
	cty_relation_total_ind int NOT NULL ,
	cty_relation_type nvarchar2 (10) NULL ,
	cty_default_credits_ind int NOT NULL ,
	cty_default_credits int NOT NULL ,
	cty_create_usr_id nvarchar2 (20) NOT NULL ,
	cty_create_timestamp timestamp NOT NULL 
);
CREATE SEQUENCE creditsType_seq;

CREATE OR REPLACE TRIGGER creditsType_trigger BEFORE INSERT ON creditsType
FOR EACH ROW
WHEN (new.cty_id IS NULL)
BEGIN
 SELECT creditsType_seq.NEXTVAL INTO :new.cty_id FROM DUAL;
END;

ALTER TABLE creditsType ADD 
	CONSTRAINT PK_creditsType PRIMARY KEY   
	(
		cty_id
	) 
;

 CREATE  INDEX IX_creditsType_complex_1 ON creditsType(cty_deduction_ind, cty_manual_ind, cty_deleted_ind, cty_relation_total_ind, cty_relation_type) 
;



--initialization credit
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp)
VALUES('ZD_INIT','ZD_INIT',0,0,0,0,'ZD',1,50,'s1u3',sysdate);

--the credit that respondent can get, when he has answered one question.
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp)
VALUES('ZD_COMMIT_ANS','ZD_COMMIT_ANS',0,0,0,0,'ZD',1,2,'s1u3',sysdate);

--the credit that respondent can get, when his answer has been set optimal answer.
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp)
VALUES('ZD_RIGHT_ANS','ZD_RIGHT_ANS',0,0,0,0,'ZD',1,20,'s1u3',sysdate);

--the credit that user can get, when he has cancelled his question.
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp)
VALUES('ZD_CANCEL_QUE','ZD_CANCEL_QUE',0,0,0,0,'ZD',1,5,'s1u3',sysdate);

--the credit that requestor can get, when he has choosed the optimal answer.
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp)
VALUES('ZD_CHOOSE_ANS','ZD_CHOOSE_ANS',0,0,0,0,'ZD',1,5,'s1u3',sysdate);

--the credit that requestor can get, when he submits one question.
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp)
VALUES('ZD_NEW_QUE','ZD_NEW_QUE',1,0,0,0,'ZD',1,-5,'s1u3',sysdate);


--table userCreditsDetail
CREATE TABLE userCreditsDetail (
	ucd_ent_id int NOT NULL ,
	ucd_cty_id int NOT NULL ,
	ucd_itm_id int NULL ,
	ucd_total int NULL ,
	ucd_hit int NULL ,
	ucd_hit_temp int NULL ,
	ucd_create_timestamp timestamp NOT NULL ,
	ucd_create_usr_id nvarchar2 (20) NOT NULL ,
	ucd_update_timestamp timestamp NOT NULL ,
	ucd_update_usr_id nvarchar2 (20) NOT NULL 
) 
;

ALTER TABLE userCreditsDetail ADD 
	CONSTRAINT PK_userCreditsDetail PRIMARY KEY   
	(
		ucd_ent_id,
		ucd_cty_id
	)   
;

 CREATE  INDEX IX_userCreditsDetail ON userCreditsDetail(ucd_ent_id) 
;

 CREATE  INDEX IX_cty_ent_itm_1 ON userCreditsDetail(ucd_ent_id, ucd_cty_id, ucd_itm_id) 
;

ALTER TABLE userCreditsDetail ADD 
	CONSTRAINT FK_ucd_cty_1 FOREIGN KEY 
	(
		ucd_cty_id
	) REFERENCES creditsType (
		cty_id
	);
ALTER TABLE userCreditsDetail ADD 
	CONSTRAINT FK_ucd_usr_1 FOREIGN KEY 
	(
		ucd_ent_id
	) REFERENCES RegUser (
		usr_ent_id
	)
;

--table userCredits
CREATE TABLE userCredits (
	uct_ent_id int NOT NULL ,
	uct_total int NULL ,
	uct_update_timestamp timestamp NOT NULL ,
	uct_zd_total int NULL 
) 
;

ALTER TABLE userCredits ADD 
	CONSTRAINT PK_userCredits PRIMARY KEY   
	(
		uct_ent_id
	)  
;

 CREATE  INDEX IX_uct_total ON userCredits(uct_total) 
;

 CREATE  INDEX IX_uct_zd_total ON userCredits(uct_zd_total) 
;

ALTER TABLE userCredits ADD 
	CONSTRAINT FK_userCredits_RegUser FOREIGN KEY 
	(
		uct_ent_id
	) REFERENCES RegUser (
		usr_ent_id
	)
;

--table knowCatalogRelation
CREATE TABLE knowCatalogRelation (
	kcr_child_kca_id int NOT NULL ,
	kcr_ancestor_kca_id int NOT NULL ,
	kcr_type nvarchar2 (50) NOT NULL ,
	kcr_order int NOT NULL ,
	kcr_parent_ind int NOT NULL ,
	kcr_syn_timestamp timestamp NULL ,
	kcr_remain_on_syn int NULL ,
	kcr_create_usr_id nvarchar2 (20) NOT NULL ,
	kcr_create_timestamp timestamp NOT NULL 
) 
;

ALTER TABLE knowCatalogRelation ADD 
	CONSTRAINT PK_knowCatalogRelation PRIMARY KEY   
	(
		kcr_child_kca_id,
		kcr_ancestor_kca_id,
		kcr_type
	)
;

 CREATE  INDEX IX_kcr_ancestor_parent_ind ON knowCatalogRelation(kcr_ancestor_kca_id, kcr_parent_ind)
;

ALTER TABLE knowCatalogRelation ADD 
	CONSTRAINT FK_kcr_kca_1 FOREIGN KEY 
	(
		kcr_ancestor_kca_id
	) REFERENCES knowCatalog (
		kca_id
	)
;

--table userCreditsDetailLog
CREATE TABLE userCreditsDetailLog (
	ucl_usr_ent_id int NOT NULL,
	ucl_bpt_id int NOT NULL,
	ucl_relation_type nvarchar2 (10) NULL,
	ucl_source_id int NULL,
	ucl_point int NOT NULL,
	ucl_create_timestamp timestamp NOT NULL,
	ucl_create_usr_id nvarchar2 (20) NOT NULL 
)
;

ALTER TABLE userCreditsDetailLog ADD 
	CONSTRAINT FK_ucl_cty_1 FOREIGN KEY 
	(
		ucl_bpt_id
	) REFERENCES creditsType (
		cty_id
	);
ALTER TABLE userCreditsDetailLog ADD
	CONSTRAINT FK_ucl_usr_1 FOREIGN KEY 
	(
		ucl_usr_ent_id
	) REFERENCES RegUser (
		usr_ent_id
	)
;
/*
Auth: Dean
Date: 2008-08-22
Desc: for saving vote detail of know
*/
-- table knowVoteDetail
CREATE TABLE knowVoteDetail (
	kvd_que_id int NOT NULL ,
	kvd_ans_id int NOT NULL ,
	kvd_ent_id int NOT NULL ,
	kvd_create_usr_id nvarchar2 (20)  NOT NULL ,
	kvd_create_timestamp timestamp NOT NULL 
) 
;

ALTER TABLE knowVoteDetail ADD 
	CONSTRAINT PK_knowVoteDetail PRIMARY KEY   
	(
		kvd_que_id,
		kvd_ans_id,
		kvd_ent_id
	)   
;


ALTER TABLE knowVoteDetail ADD 
	CONSTRAINT FK_kvd_kans_1 FOREIGN KEY 
	(
		kvd_ans_id
	) REFERENCES knowAnswer (
		ans_id
	);
  
ALTER TABLE knowVoteDetail ADD 
	CONSTRAINT FK_kvd_kque_1 FOREIGN KEY 
	(
		kvd_que_id
	) REFERENCES knowQuestion (
		que_id
	);
ALTER TABLE knowVoteDetail ADD 
	CONSTRAINT FK_kvd_usr_1 FOREIGN KEY 
	(
		kvd_ent_id
	) REFERENCES RegUser (
		usr_ent_id
	)
;


--for access control
INSERT INTO acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
VALUES ('KNOW_MGT', 'FUNCTION', 'KNOW', to_date('2008-08-22 00:00:00','YYYY-mm-dd hh24:mi:ss'), null); 

INSERT INTO acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_creation_timestamp)
SELECT rol_id, ftn_id , sysdate FROM acFunction, acRole 
WHERE ftn_ext_id = 'KNOW_MGT' AND rol_ext_id LIKE 'TADM_%';

	
/**
Auth: Tim
Date: 2008-08-15
Desc: 	  add for course muodule
*/
alter table aeItemLesson add ils_place nvarchar2(200) ;

alter table Course add  cos_structure_json nclob;

alter table CourseMeasurement add cmt_duration decimal(9,2);
alter table CourseMeasurement add cmt_place nvarchar2(510);


create table aeItemComments
(
	ict_itm_id int not null,
	ict_ent_id int not null,
	ict_tkh_id int not null,
	ict_score int not null,
	ict_comment nvarchar2(510),
	ict_create_timestamp timestamp not null
);

ALTER TABLE aeItemComments ADD CONSTRAINT
	PK_ict PRIMARY KEY  
	(
	ict_itm_id,
	ict_ent_id,
	ict_tkh_id
	);

ALTER TABLE aeItemComments  ADD 
CONSTRAINT
	FK_ict_itm_id FOREIGN KEY
	(
	ict_itm_id 
	) REFERENCES aeItem
	(
	itm_id
	);
ALTER TABLE aeItemComments  ADD 
	CONSTRAINT
	FK_ict_ent_id FOREIGN KEY
	(
	ict_ent_id 
	) REFERENCES reguser
	(
	usr_ent_id
	);
ALTER TABLE aeItemComments  ADD 
	CONSTRAINT
	FK_ict_tkh_id FOREIGN KEY
	(
	ict_tkh_id 
	) REFERENCES TrackingHistory
	(
	tkh_id
	);


alter table aeItem add itm_comment_avg_score decimal(9,2);
alter table aeItem add itm_comment_total_count int;
alter table aeItem add itm_comment_total_score int;

CREATE  INDEX IX_itm_comment_avg_score ON aeItem(itm_comment_avg_score) ;

CREATE  INDEX IX_itm_comment_total_count ON aeItem(itm_comment_total_count) ;


alter table aeApplication add app_note nclob;

alter table CourseEvaluation add cov_progress decimal(9,2);


/**
Auth: canding	
Date: 2008-08-26
Desc: 	  add tow fields to  aeItem  for Sequence of the course
*/

alter table aeItem add itm_publish_timestamp timestamp null;


/**
Auth: canding	
Date: 2008-08-26
Desc: 	  add tow fields to  aeItem  for Sequence of the course
*/

alter table aeLearningSoln add lsn_start_datetime  timestamp null
;
alter table aeLearningSoln add lsn_end_datetime timestamp null
;


/**
Auth: Kim	
Date: 2008-08-27
Desc: aeItem??
*/
ALTER TABLE aeItem ADD
	itm_srh_content nclob NULL
;
 
/**
Auth: Terry
Date: 2008-08-29
Desc: ?
*/
ALTER TABLE aeItem ADD
	itm_desc nvarchar2(4000) NULL;
ALTER TABLE aeItem ADD
	itm_plan_code nvarchar2(50) NULL;
ALTER TABLE aeItem ADD
	itm_icon nvarchar2(50) 


/**
Auth: Dean
Date: 2008-08-30
Desc: modify table knowCatalog of module know.
*/

drop index IX_kca_status_type_public
;
alter table knowCatalog drop column kca_status
;
CREATE  INDEX IX_kca_type_public ON knowCatalog(kca_type, kca_public_ind)
;
alter table knowCatalog drop column kca_public_timestamp
;




/**
Auth: Tim
Date: 2008-08-26
Desc: add for item type
*/

alter table aeItemType
add ity_exam_ind int  DEFAULT 0 not null;
alter table aeItemType
add ity_blend_ind int  DEFAULT 0 not null;
alter table aeItemType
add ity_ref_ind int  DEFAULT 0 not null;



alter table aeItem
add itm_exam_ind int  DEFAULT 0 ;
alter table aeItem
add itm_blend_ind int  DEFAULT 0;
alter table aeItem
add itm_ref_ind int  DEFAULT 0;


update aeItemType
set ity_exam_ind=0,ity_blend_ind=0,ity_ref_ind=0;
update aeItem
set itm_exam_ind=0,itm_blend_ind=0,itm_ref_ind=0;

update aeItemType set ity_ref_ind = 1 where ity_id in ('BOOK','AUDIOVIDEO','WEBSITE');
update aeItem set itm_ref_ind = 1 where itm_type in ('BOOK','AUDIOVIDEO','WEBSITE');



alter table aeItemType  drop 
CONSTRAINT PK_aeItemType ;



alter table aeItemType  ADD 
CONSTRAINT PK_aeItemType  PRIMARY KEY  
(
	ity_owner_ent_id,
	ity_id,
	ity_run_ind,
	ity_exam_ind ,
	ity_blend_ind ,
	ity_ref_ind 
);

update aeItemType
set ity_seq_id=1
where ity_id='SELFSTUDY' and ity_run_ind=0;

update aeItemType
set ity_seq_id=2
where ity_id='CLASSROOM' and ity_run_ind=0;

update aeItemType
set ity_seq_id=10
where ity_id='CLASSROOM' and ity_run_ind=1;



update aeItemType
set ity_seq_id=7
where ity_id='BOOK' and ity_run_ind=0;

update aeItemType
set ity_seq_id=8
where ity_id='AUDIOVIDEO' and ity_run_ind=0;

update aeItemType
set ity_seq_id=9
where ity_id='WEBSITE' and ity_run_ind=0;

alter table aeItemType modify (ITY_TITLE_XML null);


insert into aeItemType
select ity_owner_ent_id,ity_id,ity_run_ind,3,
  '',null,sysdate,'slu3',null,ity_create_run_ind,ity_apply_ind,ity_qdb_ind,
  0,null,ity_certificate_ind,0,0,ity_has_attendance_ind,ity_ji_ind,ity_completion_criteria_ind,
  0,ity_target_method,0,ity_tkh_method,0,1,0
from aeItemType where ity_id='CLASSROOM' and ity_run_ind=0;


insert into aeItemType
select ity_owner_ent_id,ity_id,ity_run_ind,11,
  '',null,sysdate,'slu3',null,ity_create_run_ind,ity_apply_ind,ity_qdb_ind,
  0,null,ity_certificate_ind,0,0,ity_has_attendance_ind,ity_ji_ind,ity_completion_criteria_ind,
  0,ity_target_method,0,ity_tkh_method,0,1,0
from aeItemType where ity_id='CLASSROOM' and ity_run_ind=1;


insert into aeItemType
select ity_owner_ent_id,ity_id,ity_run_ind,4,
  '',null,sysdate,'slu3',null,ity_create_run_ind,ity_apply_ind,ity_qdb_ind,
  0,null,ity_certificate_ind,0,0,ity_has_attendance_ind,ity_ji_ind,ity_completion_criteria_ind,
  0,ity_target_method,0,ity_tkh_method,1,0,0
from aeItemType where ity_id='SELFSTUDY' ;


insert into aeItemType
select ity_owner_ent_id,ity_id,ity_run_ind,5,
  '',null,sysdate,'slu3',null,ity_create_run_ind,ity_apply_ind,ity_qdb_ind,
  0,null,ity_certificate_ind,0,0,ity_has_attendance_ind,ity_ji_ind,ity_completion_criteria_ind,
  0,ity_target_method,0,ity_tkh_method,1,0,0
from aeItemType where ity_id='CLASSROOM' and ity_run_ind=0 and ity_seq_id=2;


insert into aeItemType
select ity_owner_ent_id,ity_id,ity_run_ind,12,
  '',null,sysdate,'slu3',null,ity_create_run_ind,ity_apply_ind,ity_qdb_ind,
  0,null,ity_certificate_ind,0,0,ity_has_attendance_ind,ity_ji_ind,ity_completion_criteria_ind,
  0,ity_target_method,0,ity_tkh_method,1,0,0
from aeItemType where ity_id='CLASSROOM' and ity_run_ind=1  and ity_seq_id=10;


insert into aeItemType
select ity_owner_ent_id,ity_id,ity_run_ind,6,
  '',null,sysdate,'slu3',null,ity_create_run_ind,ity_apply_ind,ity_qdb_ind,
  0,null,ity_certificate_ind,0,0,ity_has_attendance_ind,ity_ji_ind,ity_completion_criteria_ind,
  0,ity_target_method,0,ity_tkh_method,1,1,0
from aeItemType where ity_id='CLASSROOM' and ity_run_ind=0  and ity_seq_id=2;


insert into aeItemType
select ity_owner_ent_id,ity_id,ity_run_ind,13,
  '',null,sysdate,'slu3',null,ity_create_run_ind,ity_apply_ind,ity_qdb_ind,
  0,null,ity_certificate_ind,0,0,ity_has_attendance_ind,ity_ji_ind,ity_completion_criteria_ind,
  0,ity_target_method,0,ity_tkh_method,1,1,0
from aeItemType where ity_id='CLASSROOM' and ity_run_ind=1  and ity_seq_id=10;

insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('ITM_EXAM_MAIN', 'FUNCTION', 'HOMEPAGE', sysdate, null);

insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('ITM_REF_MAIN', 'FUNCTION', 'HOMEPAGE', sysdate, null);

INSERT INTO acRoleFunction 
select rol_id, ftn_id, sysdate from acRole, acFunction where rol_ste_uid in ('TADM') and ftn_ext_id = 'ITM_EXAM_MAIN';

INSERT INTO acRoleFunction 
select rol_id, ftn_id, sysdate from acRole, acFunction where rol_ste_uid in ('TADM') and ftn_ext_id = 'ITM_REF_MAIN';

INSERT INTO acHomepage 
select null, rol_ext_id, 'ITM_EXAM_MAIN', 's1u3', sysdate from acRole where rol_ste_uid in ('TADM');

INSERT INTO acHomepage 
select null, rol_ext_id, 'ITM_REF_MAIN', 's1u3', sysdate from acRole where rol_ste_uid in ('TADM');

delete from acHomepage where ac_hom_ftn_ext_id='RUN_MAIN';




/*
Auth: kawai
Date: 2008-09-01
Desc: deprecate some functions
*/
delete from acRoleFunction
where rfn_ftn_id in (
	select ftn_id from acFunction
	where ftn_ext_id in ('LRN_CALENDAR','LRN_BLUEPRINT_ADMIN','LRN_BLUEPRINT_VIEW')
)
;
delete from acHomePage
where ac_hom_ftn_ext_id in ('LRN_CALENDAR','LRN_BLUEPRINT_ADMIN','LRN_BLUEPRINT_VIEW')
;


/**
Auth: Tim
Date: 2008-09-01
Desc: add for TA studyGroup page
*/

insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('STUDY_GROUP_MAIN', 'FUNCTION', 'HOMEPAGE', sysdate, null);

INSERT INTO acRoleFunction 
select rol_id, ftn_id, sysdate from acRole, acFunction where rol_ste_uid in ('TADM') and ftn_ext_id = 'STUDY_GROUP_MAIN';


INSERT INTO acHomepage 
select null, rol_ext_id, 'STUDY_GROUP_MAIN', 's1u3', sysdate from acRole where rol_ste_uid in ('TADM');


/**
Auth: Elvea 
Date: 2008-09-02
Desc: Know
*/
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('KNOW_MAIN', 'FUNCTION', 'HOMEPAGE', sysdate, null);

INSERT INTO acRoleFunction 
select rol_id, ftn_id, sysdate from acRole, acFunction where rol_ste_uid in ('TADM') and ftn_ext_id = 'KNOW_MAIN';

INSERT INTO acHomepage 
select null, rol_ext_id, 'KNOW_MAIN', 's1u3', sysdate from acRole where rol_ste_uid in ('TADM');
/**
Auth: canding
Date: 2008-09-04
Desc: change the user file
*/

alter table regUser add  usr_nickname nvarchar2(20) null;
alter table ReguserExtension add  urx_extra_41  nvarchar2(500) null;
alter table ReguserExtension add  urx_extra_42  nvarchar2(500) null;
alter table ReguserExtension add  urx_extra_43  nvarchar2(500) null;
alter table ReguserExtension add  urx_extra_44  nvarchar2(500) null;
alter table ReguserExtension add  urx_extra_45  nvarchar2(500) null;

/**
Auth: Tim
Date: 2008-09-03
Desc: add for cmSkillEntity
*/

create table cmSkillEntity
(
ske_id int not null,
ske_type nvarchar2(20) not null
);

CREATE SEQUENCE cmSkillEntity_seq
minvalue -1
start with -1;
CREATE OR REPLACE TRIGGER cmSkillEntity_trigger BEFORE INSERT ON cmSkillEntity
FOR EACH ROW
WHEN (new.ske_id IS NULL)
BEGIN
 SELECT cmSkillEntity_seq.NEXTVAL INTO :new.ske_id FROM DUAL;
END;


ALTER TABLE cmSkillEntity ADD CONSTRAINT
	PK_skt PRIMARY KEY  
	(
	ske_id
	);

alter table cmSkillBase add skb_ske_id int;
ALTER TABLE cmSkillBase ADD 
	CONSTRAINT FK_skb_ske_id FOREIGN KEY 
	(
		skb_ske_id
	) REFERENCES cmSkillEntity (
		ske_id
	);
alter table cmSkillSet add sks_ske_id int;
ALTER TABLE cmSkillSet ADD 
	CONSTRAINT FK_sks_ske_id FOREIGN KEY 
	(
		sks_ske_id
	) REFERENCES cmSkillEntity (
		ske_id
	);

insert into cmSkillEntity(ske_type) values('SYS_UNSPECIFIED');
insert into cmSkillEntity(ske_type) values('SYS_ALL');

alter table aeItemTargetRule add itr_skill_id nvarchar2(2000);

alter table aeItemTargetRuleDetail add ird_skill_id int default -1 not null;
alter table aeItemTargetRuleDetail drop CONSTRAINT PK_ird;
ALTER TABLE aeItemTargetRuleDetail ADD CONSTRAINT
	PK_ird PRIMARY KEY  
	(
		ird_itm_id, 
		ird_itr_id, 
		ird_group_id, 
		ird_grade_id,
		ird_skill_id
	);


create table RegUserSkillSet
(
uss_ent_id  int  not null,
uss_ske_id  int not null
);

ALTER TABLE RegUserSkillSet ADD CONSTRAINT 
 PK_ent_ske PRIMARY KEY  
(uss_ent_id,
uss_ske_id);
 
ALTER TABLE RegUserSkillSet  
ADD CONSTRAINT
	FK_uss_usr_id FOREIGN KEY
	(
		uss_ent_id 
	) REFERENCES RegUser
	(
	usr_ent_id 
	) ;

ALTER TABLE RegUserSkillSet  
ADD CONSTRAINT
	FK_uss_ske_id FOREIGN KEY
	(
		uss_ske_id 
	) REFERENCES cmSkillEntity
	(
	ske_id 
	);
	
	
/*
Auth: Dean
Date: 2008-09-05
Desc: for module: public evaluation survey
*/
insert into acRoleFunction (rfn_rol_id,rfn_ftn_id,rfn_creation_timestamp)
select rol_id, ftn_id , to_date('2008-09-05 00:00:00', 'YYYY-MM-DD HH24:MI:SS') from acFunction, acRole where ftn_ext_id = 'EVN_MAIN' and rol_ext_id like 'TADM_%';

insert into acRoleFunction (rfn_rol_id,rfn_ftn_id,rfn_creation_timestamp)
select rol_id, ftn_id ,  to_date('2008-09-05 00:00:00', 'YYYY-MM-DD HH24:MI:SS') from acFunction, acRole where ftn_ext_id = 'EVN_LIST' and rol_ext_id like 'NLRN_%';

insert into acHomePage (ac_hom_rol_ext_id,ac_hom_ftn_ext_id, ac_hom_create_usr_id, ac_hom_create_timestamp)
select rol_ext_id, 'EVN_LIST', 's1u3',  to_date('2008-09-05 00:00:00', 'YYYY-MM-DD HH24:MI:SS') from acRole where rol_ext_id like 'NLRN_%';

insert into acHomePage (ac_hom_rol_ext_id,ac_hom_ftn_ext_id, ac_hom_create_usr_id, ac_hom_create_timestamp)
select rol_ext_id, 'EVN_MAIN', 's1u3',  to_date('2008-09-05 00:00:00', 'YYYY-MM-DD HH24:MI:SS') from acRole where rol_ext_id like 'TADM_%';


CREATE TABLE EvalAccess (
	eac_res_id int NOT NULL ,
	eac_target_ent_id int NOT NULL ,
	eac_order int NOT NULL ,
	eac_create_timestamp timestamp NOT NULL ,
	eac_create_usr_id nvarchar2 (20) NOT NULL ,
	CONSTRAINT PK_EvalAccess PRIMARY KEY   
	(
		eac_res_id,
		eac_target_ent_id
	)  
);

CREATE TABLE moduleTrainingCenter (
	mtc_mod_id int NOT NULL,
	mtc_tcr_id int NOT NULL,
	mtc_create_timestamp timestamp NOT NULL,
	mtc_create_usr_id nvarchar2(20) NOT NULL 
) ;


ALTER TABLE moduleTrainingCenter  ADD 
	CONSTRAINT PK_moduleTrainingCenter PRIMARY KEY  
	(
		mtc_mod_id,
		mtc_tcr_id
	)   
;

ALTER TABLE moduleTrainingCenter ADD 
	CONSTRAINT FK_mtc_mod_1 FOREIGN KEY 
	(
		mtc_mod_id
	) REFERENCES Module (
		mod_res_id
	);
ALTER TABLE moduleTrainingCenter ADD 
	CONSTRAINT FK_mtc_tcr_1 FOREIGN KEY 
	(
		mtc_tcr_id
	) REFERENCES tcTrainingCenter (
		tcr_id
	);


/**
Auth: Tim
Date: 2008-09-03
Desc: open CM_ASS_MAIN、CM_MAIN、CM_SKL_ANALYSIS
*/	
-- CM related functions

insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_creation_timestamp)
select rol_id, ftn_id, to_date('2008-09-03 00:00:00', 'YYYY-MM-DD HH24:MI:SS') from acRole, acFunction
where
(rol_ext_id like 'TADM_%' and ftn_ext_id in ('CM_ASS_MAIN','CM_MAIN','CM_SKL_ANALYSIS'))
or
(rol_ext_id like 'TADM_%' or rol_ext_id like 'ADM_%' or rol_ext_id like 'NLRN_%') and ftn_ext_id in ('TO_DO_LIST');

insert into acHomePage (ac_hom_rol_ext_id, ac_hom_ftn_ext_id, ac_hom_create_usr_id, ac_hom_create_timestamp)
select rol_ext_id, ftn_ext_id, 's1u3', to_date('2008-09-03 00:00:00', 'YYYY-MM-DD HH24:MI:SS') from acRole, acFunction
where
(rol_ext_id like 'TADM_%' and ftn_ext_id in ('CM_ASS_MAIN','CM_MAIN','CM_SKL_ANALYSIS'))
or
(rol_ext_id like 'TADM_%' or rol_ext_id like 'ADM_%' or rol_ext_id like 'NLRN_%') and ftn_ext_id in ('TO_DO_LIST');
/*
Auth: Jacky xiao
Date: 2008-09-05
Desc: add function to management training plan
*/
Insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('TRA_PLA_MGT', 'FUNCTION', 'HOMEPAGE', sysdate, NULL);

insert into acrolefunction
select rol_id, ftn_id , sysdate from acFunction, acRole where ftn_ext_id = 'TRA_PLA_MGT' and rol_ste_uid in ('TADM');

/* PLAN_CARRY_OUT */
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('PLAN_CARRY_OUT', 'FUNCTION', 'HOMEPAGE', sysdate, null);

INSERT INTO acRoleFunction 
select rol_id, ftn_id, sysdate from acRole, acFunction where rol_ste_uid in ('TADM') 
and ftn_ext_id = 'PLAN_CARRY_OUT';

INSERT INTO acHomepage 
select null, rol_ext_id, 'PLAN_CARRY_OUT', 's1u3', sysdate from acRole where rol_ste_uid in ('TADM');
/* YEAR_PALN */	
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('YEAR_PALN', 'FUNCTION', 'HOMEPAGE', sysdate, null);

INSERT INTO acRoleFunction 
select rol_id, ftn_id, sysdate from acRole, acFunction where rol_ste_uid in ('TADM') 
and ftn_ext_id = 'YEAR_PALN';

INSERT INTO acHomepage 
select null, rol_ext_id, 'YEAR_PALN', 's1u3', sysdate from acRole where rol_ste_uid in ('TADM');	
/* MAKEUP_PLAN */
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('MAKEUP_PLAN', 'FUNCTION', 'HOMEPAGE', sysdate, null);

INSERT INTO acRoleFunction 
select rol_id, ftn_id, sysdate from acRole, acFunction where rol_ste_uid in ('TADM') 
and ftn_ext_id = 'MAKEUP_PLAN';

INSERT INTO acHomepage 
select null, rol_ext_id, 'MAKEUP_PLAN', 's1u3', sysdate from acRole where rol_ste_uid in ('TADM');	
/* YEAR_PLAN_APPR */
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('YEAR_PLAN_APPR', 'FUNCTION', 'HOMEPAGE', sysdate, null);

INSERT INTO acRoleFunction 
select rol_id, ftn_id, sysdate from acRole, acFunction where rol_ste_uid in ('TADM') 
and ftn_ext_id = 'YEAR_PLAN_APPR';

INSERT INTO acHomepage 
select null, rol_ext_id, 'YEAR_PLAN_APPR', 's1u3', sysdate from acRole where rol_ste_uid in ('TADM');	
/* MAKEUP_PLAN_APPR */
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('MAKEUP_PLAN_APPR', 'FUNCTION', 'HOMEPAGE', sysdate, null);

INSERT INTO acRoleFunction 
select rol_id, ftn_id, sysdate from acRole, acFunction where rol_ste_uid in ('TADM') 
and ftn_ext_id = 'MAKEUP_PLAN_APPR';

INSERT INTO acHomepage 
select null, rol_ext_id, 'MAKEUP_PLAN_APPR', 's1u3', sysdate from acRole where rol_ste_uid in ('TADM');	
/* YEAR_SETTING */
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('YEAR_SETTING', 'FUNCTION', 'HOMEPAGE', sysdate, null);

INSERT INTO acRoleFunction 
select rol_id, ftn_id, sysdate from acRole, acFunction where rol_ste_uid in ('TADM') 
and ftn_ext_id = 'YEAR_SETTING';

INSERT INTO acHomepage 
select null, rol_ext_id, 'YEAR_SETTING', 's1u3', sysdate from acRole where rol_ste_uid in ('TADM');	

/*
Auth: Jacky xiao
Date: 2008-09-05
Desc: new some tables for training plan management
*/
CREATE TABLE tpTrainingPlan
	(
	tpn_id int  NOT NULL,
	tpn_tcr_id int NOT NULL,
	tpn_date timestamp NOT NULL,
	tpn_code nvarchar2(50) NOT NULL,
	tpn_name nvarchar2(200) NOT NULL,
	tpn_cos_type nvarchar2(255) NOT NULL,
	tpn_tnd_title nvarchar2(100) NULL,
	tpn_introduction nclob NULL,
	tpn_aim nclob NULL,
	tpn_target nclob NULL,
	tpn_responser nvarchar2(255) NULL,
	tpn_lrn_count int NULL,
	tpn_duration nclob NULL,
	tpn_wb_start_date timestamp NULL,
	tpn_wb_end_date timestamp NULL,
	tpn_ftf_start_date timestamp NULL,
	tpn_ftf_end_date timestamp NULL,
	tpn_type nvarchar2(20) NOT NULL,
	tpn_fee float NULL,
	tpn_remark nclob NULL,
	tpn_status nvarchar2(20) NOT NULL,
	tpn_approve_usr_id nvarchar2(20) NULL,
	tpn_approve_timestamp timestamp NULL,
	tpn_create_usr_id nvarchar2(20) NOT NULL,
	tpn_create_timestamp timestamp NOT NULL,
	tpn_update_usr_id nvarchar2(20) NOT NULL,
	tpn_update_timestamp timestamp NOT NULL,
	tpn_submit_usr_id nvarchar2(20) NULL,
	tpn_submit_timestamp timestamp NULL
	) ;
CREATE SEQUENCE tpTrainingPlan_seq;

CREATE OR REPLACE TRIGGER tpTrainingPlan_trigger BEFORE INSERT ON tpTrainingPlan
FOR EACH ROW
WHEN (new.tpn_id IS NULL)
BEGIN
 SELECT tpTrainingPlan_seq.NEXTVAL INTO :new.tpn_id FROM DUAL;
END;




ALTER TABLE tpTrainingPlan ADD CONSTRAINT
	PK_tpTrainingPlan PRIMARY KEY 
	(
	tpn_id
	);


CREATE TABLE tpYearPlan
	(
	ypn_year int NOT NULL,
	ypn_tcr_id int NOT NULL,
	ypn_file_name nvarchar2(255) NOT NULL,
	ypn_status nvarchar2(20) NOT NULL,
	ypn_approve_usr_id nvarchar2(20) NULL,
	ypn_approve_timestamp timestamp NULL,
	ypn_submit_usr_id nvarchar2(20) NULL,
	ypn_submit_timestamp timestamp NULL,
	ypn_create_usr_id nvarchar2(20) NULL,
	ypn_create_timestamp timestamp NULL,
	ypn_update_usr_id nvarchar2(20) NULL,
	ypn_update_timestamp timestamp NULL
	) ;

ALTER TABLE tpYearPlan ADD CONSTRAINT
	PK_tpYearPlan PRIMARY KEY 
	(
	ypn_year,
	ypn_tcr_id
	) ;

CREATE TABLE tpYearSetting
	(
	ysg_tcr_id int NOT NULL,
	ysg_year int NOT NULL,
	ysg_child_tcr_id_lst nvarchar2(4000) NOT NULL,
	ysg_submit_start_datetime timestamp NOT NULL,
	ysg_submit_end_datetime timestamp NOT NULL,
	ysg_create_timestamp timestamp NOT NULL,
	ysg_create_usr_id nvarchar2(20) NOT NULL,
	ysg_update_timestamp timestamp NOT NULL,
	ysg_update_usr_id nvarchar2(20) NOT NULL
	)  
;
ALTER TABLE tpYearSetting ADD CONSTRAINT
	PK_tpYearSetting PRIMARY KEY 
	(
	ysg_tcr_id,
	ysg_year
	) 
;

/**
Auth: Dean
Date: 2008-09-11
Desc: for homepage of learner
*/	
--update homepage address of learner
update acRole set rol_url_home='htm/home.htm' where rol_ext_id like 'NLRN_%'

/**
Auth: Dean
Date: 2008-09-28
Desc: for bug7245.
*/	
update DisplayOption set dpo_desc_ind  = 1 where dpo_res_type= 'MOD' AND dpo_res_subtype = 'EVN';

/**
Auth: Tim
Date: 2008-10-10
Desc: set default skin
*/

delete from psnPreference 

/**
Auth: Dean
Date: 2008-10-28
Desc: for bug7517
*/	
--publish a question
update creditsType set cty_default_credits = -2 where cty_code = 'ZD_NEW_QUE';
--cancel a question
update creditsType set cty_default_credits = 2 where cty_code = 'ZD_CANCEL_QUE';
--set optimal answer
update creditsType set cty_default_credits = 2 where cty_code = 'ZD_CHOOSE_ANS';

/**
Auth: Tim
Date: 2008-10-29
Desc: for bug6980
*/

alter table cmAssessment  modify (asm_title nvarchar2(100));

/**
Auth: Tim
Date: 2008-10-31
Desc: for studygroup forum title(Bug 7582 )
*/
update resources 
set res_title=sgp_title
from resources res,studygroup,studygrouprelation
where sgr_type ='SGP_DISCUSS' 
and sgp_id=sgr_sgp_id
and res.res_id = sgr_ent_id



/**
Auth: Terry
Date: 2008-11-11
Desc: allow kca_code to be null
*/
alter table knowCatalog modify( kca_code null)

--r.15353(v4.7 to v5.0.1)

/**
Auth: Dean
Date: 2008-12-16
Desc: for bug8129
*/	
--to delete invaild relation record between question and catalog
delete from knowcatalogrelation
where kcr_type = 'QUE_PARENT_KCA' and kcr_child_kca_id not in (
	select distinct que_id from knowQuestion
)


/**
Auth: Tim
Date: 2008-12-29
Desc: for bug5796
*/	
update ModuleEvaluation set mov_status ='P'
where mov_mod_id in(
	select mod_res_id from Module where  mod_type ='ASS'
)
and mov_status ='C';


update ModuleEvaluationHistory set mvh_status ='P'
where mvh_mod_id in(
	select mod_res_id from Module where  mod_type ='ASS'
)
and mvh_status ='C';


update ResourceRequirement set rrq_status='P'
where rrq_req_res_id in(
	select mod_res_id from Module where mod_type='ASS'
)
and rrq_status='C';

/**
Auth: Kim
Date: 2009-01-09
Desc: 知道与知道管理模块的搜索不区分大小功能
*/

--在表knowAnswer中增加ans_content对应的搜索字段ans_content_search
alter table KNOWANSWER add ans_content_search nclob not null;

/**
Auth: Tim
Date: 2009-01-13
Desc: forget password  function
*/

create table usrPwdResetHis
(
   prh_id int not null,
   prh_ent_id int not null,
   prh_ip nvarchar2(50) not null,
   prh_status nvarchar2(40) not null,
   prh_attempted int not null,
   prh_create_timestamp timestamp not null

);
create sequence usrPwdResetHis_seq;
CREATE OR REPLACE TRIGGER usrPwdResetHis_trigger BEFORE INSERT ON usrPwdResetHis
FOR EACH ROW
WHEN (new.prh_id IS NULL)
BEGIN
 SELECT usrPwdResetHis_seq.NEXTVAL INTO :new.prh_id FROM DUAL;
END;


ALTER TABLE usrPwdResetHis ADD CONSTRAINT
	PK_prh PRIMARY KEY 
	(
	   prh_id
	);

ALTER TABLE usrPwdResetHis ADD CONSTRAINT
	FK_prh_ent_id FOREIGN KEY
	(
	   prh_ent_id
	) REFERENCES regUser
	(
	   usr_ent_id
	);
	
insert into xslTemplate(XTP_TYPE, XTP_SUBTYPE, XTP_CHANNEL_TYPE, XTP_CHANNEL_API, XTP_XSL, XTP_XML_URL, XTP_MAILMERGE_IND, XTP_TITLE)
values
('USER_PWD_RESET_NOTIFY', 'HTML', 'SMTP', null, 'usr_pwd_reset_notify.xsl', 'http://host:port/servlet/Dispatcher?module=JsonMod.user.UserModule&',0,null);

insert into xslparamname (XPN_XTP_ID , XPN_POS, XPN_NAME) select xtp_id, 1, 'cmd' from xslTemplate where xtp_type = 'USER_PWD_RESET_NOTIFY';
insert into xslparamname (XPN_XTP_ID , XPN_POS, XPN_NAME) select xtp_id, 2, 'sender_id' from xslTemplate where xtp_type = 'USER_PWD_RESET_NOTIFY';
insert into xslparamname (XPN_XTP_ID , XPN_POS, XPN_NAME) select xtp_id, 3, 'sid' from xslTemplate where xtp_type = 'USER_PWD_RESET_NOTIFY';
insert into xslparamname (XPN_XTP_ID , XPN_POS, XPN_NAME) select xtp_id, 4, 'usr_ent_id' from xslTemplate where xtp_type = 'USER_PWD_RESET_NOTIFY';

/*
Auth: Robin
Date: 2009-02-16
Desc: add a new role to manage the exam function.
*/	
INSERT INTO acRole (rol_ext_id,rol_seq_id,rol_ste_ent_id,rol_url_home,rol_creation_timestamp,rol_xml,rol_ste_default_ind,rol_report_ind,rol_skin_root,rol_status,rol_ste_uid,rol_target_ent_type,rol_auth_level, rol_tc_ind)
SELECT 'EXA_1', 9, 1, rol_url_home, sysdate, '', 0, 0, rol_skin_root, 'OK', 'EXA', null, 3, 0 FROM acRole WHERE rol_ext_id = 'TADM_1';

INSERT INTO acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('EXAM_ITEM_MAIN', 'FUNCTION', 'HOMEPAGE', sysdate, null);

INSERT INTO acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_creation_timestamp) 
SELECT rol_id, ftn_id, sysdate FROM acRole, acFunction WHERE rol_ste_uid = 'EXA' AND ftn_ext_id = 'EXAM_ITEM_MAIN';

INSERT INTO acHomepage (ac_hom_ent_id,ac_hom_rol_ext_id,ac_hom_ftn_ext_id,ac_hom_create_usr_id,ac_hom_create_timestamp) 
SELECT null, rol_ext_id, 'EXAM_ITEM_MAIN', 's1u3', sysdate FROM acRole WHERE rol_ste_uid = 'EXA';

INSERT INTO acHomePage (ac_hom_ent_id,ac_hom_rol_ext_id,ac_hom_ftn_ext_id,ac_hom_create_usr_id,ac_hom_create_timestamp)
SELECT null, rol_ext_id, 'USR_OWN_MAIN', 's1u3', sysdate FROM acRole WHERE rol_ste_uid = 'EXA';

INSERT INTO acHomePage (ac_hom_ent_id,ac_hom_rol_ext_id,ac_hom_ftn_ext_id,ac_hom_create_usr_id,ac_hom_create_timestamp)
SELECT null, rol_ext_id, 'USR_PWD_UPD', 's1u3', sysdate FROM acRole WHERE rol_ste_uid = 'EXA';

INSERT INTO acHomePage (ac_hom_ent_id,ac_hom_rol_ext_id,ac_hom_ftn_ext_id,ac_hom_create_usr_id,ac_hom_create_timestamp)
SELECT null, rol_ext_id, 'USR_OWN_PREFER', 's1u3', sysdate FROM acRole WHERE rol_ste_uid = 'EXA';


/*
Auth: Robin
Date: 2009-02-24
Desc: Test Paper Statistics Report
*/	
INSERT INTO ReportTemplate (RTE_TITLE_XML, RTE_TYPE, RTE_GET_XSL, RTE_EXE_XSL, RTE_DL_XSL, RTE_META_DATA_URL, RTE_SEQ_NO, RTE_OWNER_ENT_ID, RTE_CREATE_USR_ID, RTE_CREATE_TIMESTAMP, RTE_UPD_USR_ID, RTE_UPD_TIMESTAMP)
select 'XML', 'EXAM_PAPER_STAT', 'rpt_get_exam_paper_stat.xsl', 'rpt_exe_exam_paper_stat.xsl', 'rpt_dl_progress.xsl', NULL, max(rte_seq_no) + 1, 1, 'slu3', sysdate, 'slu3', sysdate
from ReportTemplate;

INSERT INTO acReportTemplate (ac_rte_id,ac_rte_rol_ext_id,ac_rte_ftn_ext_id,ac_rte_owner_ind,ac_rte_create_usr_id,ac_rte_create_timestamp)
SELECT rte_id, 'TADM_1','RTE_READ', 0, 's1u3', sysdate FROM ReportTemplate WHERE rte_type = 'EXAM_PAPER_STAT';

INSERT INTO ObjectView(ojv_owner_ent_id, ojv_type, ojv_subtype, ojv_option_xml, ojv_create_usr_id, ojv_create_timestamp, ojv_update_usr_id, ojv_update_timestamp)
SELECT ste_ent_id, 'EXAM_PAPER_STAT', 'USR', 'XML', 'slu3', sysdate, 'slu3', sysdate FROM acSite ;

INSERT INTO ObjectView(ojv_owner_ent_id, ojv_type, ojv_subtype, ojv_option_xml, ojv_create_usr_id, ojv_create_timestamp, ojv_update_usr_id, ojv_update_timestamp)
SELECT ste_ent_id, 'EXAM_PAPER_STAT', 'ITM', 'XML', 'slu3', sysdate, 'slu3', sysdate FROM acSite ;


/*
Auth: Joyce
Date: 2009-02-24
Desc: add Friendship Links function to System Administrator.
*/
insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('FS_LINK_MAIN', 'FUNCTION', 'HOMEPAGE', sysdate, null); 

insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_creation_timestamp)
select rol_id, ftn_id,sysdate from acRole, acFunction
where rol_ext_id like 'ADM_%' and ftn_ext_id = 'FS_LINK_MAIN';

insert into acHomePage (ac_hom_rol_ext_id,ac_hom_ftn_ext_id, ac_hom_create_usr_id, ac_hom_create_timestamp)
select rol_ext_id, 'FS_LINK_MAIN', 's1u3', sysdate from acRole where rol_ext_id like 'ADM_%';

/*
Auth: Joyce
Date: 2009-02-24
Desc: add Friendship Link Table.
*/
CREATE TABLE FriendshipLink
	(
	fsl_id int NOT NULL,
	fsl_title nvarchar2(50) NOT NULL,
	fsl_url nvarchar2(255) NOT NULL,
	fsl_status nvarchar2(20) NOT NULL,
	fsl_create_usr_id nvarchar2(20) NOT NULL,
	fsl_create_timestamp timestamp NOT NULL,
	fsl_update_usr_id nvarchar2(20) NOT NULL,
	fsl_update_timestamp timestamp NOT NULL
	);

create sequence FriendshipLink_seq;
CREATE OR REPLACE TRIGGER FriendshipLink_trigger BEFORE INSERT ON FriendshipLink
FOR EACH ROW
WHEN (new.fsl_id IS NULL)
BEGIN
 SELECT FriendshipLink_seq.NEXTVAL INTO :new.fsl_id FROM DUAL;
END;

ALTER TABLE FriendshipLink ADD CONSTRAINT
	PK_FriendshipLink PRIMARY KEY 
	(
	fsl_id
	);



/*
Auth: Joyce
Date: 2009-03-05
Desc: add Poster Management function to System Administrator.
*/
insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('POSTER_MAIN', 'FUNCTION', 'HOMEPAGE', sysdate, null); 

insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_creation_timestamp)
select rol_id, ftn_id,sysdate from acRole, acFunction
where rol_ext_id like 'ADM_%' and ftn_ext_id = 'POSTER_MAIN';

insert into acHomePage (ac_hom_rol_ext_id,ac_hom_ftn_ext_id, ac_hom_create_usr_id, ac_hom_create_timestamp)
select rol_ext_id, 'POSTER_MAIN', 's1u3', sysdate from acRole where rol_ext_id like 'ADM_%';

/*
Auth: Joyce
Date: 2009-03-05
Desc: add Poster Management Tabel.
*/
CREATE TABLE SitePoster (
	sp_ste_id int NOT NULL ,
	sp_media_file nvarchar2(50) NULL ,
	sp_url nvarchar2(255) NULL ,
	sp_status nvarchar2(50) NULL 
);


ALTER TABLE SitePoster ADD CONSTRAINT
	PK_SitePoster PRIMARY KEY
	(
	sp_ste_id
	);


ALTER TABLE SitePoster ADD
	CONSTRAINT FK_SitePoster_acSite FOREIGN KEY
	(
		sp_ste_id
	) REFERENCES acSite (
		ste_ent_id
	);


/*
Auth: Robin
Date: 2009-02-24
Desc: Training Expense Statistics Report
*/	
INSERT INTO ReportTemplate (RTE_TITLE_XML, RTE_TYPE, RTE_GET_XSL, RTE_EXE_XSL, RTE_DL_XSL, RTE_META_DATA_URL, RTE_SEQ_NO, RTE_OWNER_ENT_ID, RTE_CREATE_USR_ID, RTE_CREATE_TIMESTAMP, RTE_UPD_USR_ID, RTE_UPD_TIMESTAMP)
select 'XML', 'TRAIN_FEE_STAT', 'rpt_get_train_fee_stat.xsl', 'rpt_exe_train_fee_stat.xsl', 'rpt_dl_progress.xsl', NULL, max(rte_seq_no) + 1, 1, 'slu3', sysdate, 'slu3', sysdate
from ReportTemplate;

INSERT INTO acReportTemplate (ac_rte_id,ac_rte_rol_ext_id,ac_rte_ftn_ext_id,ac_rte_owner_ind,ac_rte_create_usr_id,ac_rte_create_timestamp)
SELECT rte_id, 'TADM_1','RTE_READ', 0, 's1u3', sysdate FROM ReportTemplate WHERE rte_type = 'TRAIN_FEE_STAT';

INSERT INTO ObjectView
SELECT ste_ent_id, 'TRAIN_FEE_STAT', 'ITM', 'XML', 'slu3', sysdate, 'slu3', sysdate FROM acSite ;


/*
Auth: Joyce
Date: 2009-03-09
Desc: add send email indicate for study group.
*/
ALTER TABLE studyGroup ADD
	sgp_send_email_ind int DEFAULT 0;


/*
Auth: Joyce
Date: 2009-03-09
Desc: add new xsl template for study group email.
*/
insert into xslTemplate(XTP_TYPE, XTP_SUBTYPE, XTP_CHANNEL_TYPE, XTP_CHANNEL_API, XTP_XSL, XTP_XML_URL, XTP_MAILMERGE_IND, XTP_TITLE)
values
('JOIN_STUDYGROUP_REMINDER', 'HTML', 'SMTP', null, 'study_group_join_reminder.xsl', 'http://host:port/servlet/Dispatcher?module=JsonMod.studyGroup.StudyGroupModule&',1,null);

insert into xslparamname (XPN_XTP_ID , XPN_POS, XPN_NAME) select xtp_id, 1, 'cmd' from xslTemplate where xtp_type = 'JOIN_STUDYGROUP_REMINDER';
insert into xslparamname (XPN_XTP_ID , XPN_POS, XPN_NAME) select xtp_id, 2, 'ent_ids' from xslTemplate where xtp_type = 'JOIN_STUDYGROUP_REMINDER';
insert into xslparamname (XPN_XTP_ID , XPN_POS, XPN_NAME) select xtp_id, 3, 'sender_id' from xslTemplate where xtp_type = 'JOIN_STUDYGROUP_REMINDER';
insert into xslparamname (XPN_XTP_ID , XPN_POS, XPN_NAME) select xtp_id, 4, 'sgp_id' from xslTemplate where xtp_type = 'JOIN_STUDYGROUP_REMINDER';
	
	
/*
Auth: Robin
Date: 2009-03-09
Desc: Training Cost Statistics Report
*/	
INSERT INTO ReportTemplate (RTE_TITLE_XML, RTE_TYPE, RTE_GET_XSL, RTE_EXE_XSL, RTE_DL_XSL, RTE_META_DATA_URL, RTE_SEQ_NO, RTE_OWNER_ENT_ID, RTE_CREATE_USR_ID, RTE_CREATE_TIMESTAMP, RTE_UPD_USR_ID, RTE_UPD_TIMESTAMP)
select 'XML', 'TRAIN_COST_STAT', 'rpt_get_train_cost_stat.xsl', 'rpt_exe_train_cost_stat.xsl', 'rpt_dl_progress.xsl', NULL, max(rte_seq_no) + 1, 1, 'slu3', sysdate, 'slu3', sysdate
from ReportTemplate;

INSERT INTO acReportTemplate (ac_rte_id,ac_rte_rol_ext_id,ac_rte_ftn_ext_id,ac_rte_owner_ind,ac_rte_create_usr_id,ac_rte_create_timestamp)
SELECT rte_id, 'TADM_1','RTE_READ', 0, 's1u3', sysdate FROM ReportTemplate WHERE rte_type = 'TRAIN_COST_STAT';

INSERT INTO ObjectView(ojv_owner_ent_id, ojv_type, ojv_subtype, ojv_option_xml, ojv_create_usr_id, ojv_create_timestamp, ojv_update_usr_id, ojv_update_timestamp)
SELECT ste_ent_id, 'TRAIN_COST_STAT', 'OTHER', 'XML', 'slu3', sysdate, 'slu3', sysdate FROM acSite ;


/*
Auth: Robin
Date: 2009-03-12
Desc: Facility Management
*/	
INSERT INTO fmFacilityType
select max(ftp_id) + 1, ' ', 0, '', '', 2, 1
from fmFacilityType;

ALTER TABLE fmFacility ADD fac_fee decimal(18,2) NULL;


/*
Auth: Robin
Date: 2009-03-13
Desc: Search Training Equipment Expense
*/	
insert into acRoleFunction 
select rol_id, ftn_id, sysdate from acRole, acFunction where rol_ext_id like 'ADM_%' and ftn_ext_id = 'TC_MAIN_IN_TCR';

INSERT INTO ReportTemplate(RTE_TITLE_XML, RTE_TYPE, RTE_GET_XSL, RTE_EXE_XSL, RTE_DL_XSL, RTE_META_DATA_URL, RTE_SEQ_NO, RTE_OWNER_ENT_ID, RTE_CREATE_USR_ID, RTE_CREATE_TIMESTAMP, RTE_UPD_USR_ID, RTE_UPD_TIMESTAMP)
select 'XML', 'FM_FEE', null, 'rpt_exe_fm_fee.xsl', 'rpt_dl_progress.xsl', NULL, max(rte_seq_no) + 1, 1, 'slu3', sysdate, 'slu3', sysdate
from ReportTemplate;


/*
Auth: Dean
Date: 2009-03-19
Desc: module - survey evaluation
*/	
alter table progressAttempt add atm_response_bil_ext nvarchar2(255) null;


/*
Auth: Joyce
Date: 2009-03-30
Desc: 积分管理
*/
-- 给ADM添加“积分管理”权限
insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('CREDIT_SETTING_MAIN', 'FUNCTION', 'HOMEPAGE', sysdate, null); 

insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_creation_timestamp)
select rol_id, ftn_id,sysdate from acRole, acFunction
where rol_ext_id like 'ADM_%' and ftn_ext_id = 'CREDIT_SETTING_MAIN';

insert into acHomePage (ac_hom_rol_ext_id,ac_hom_ftn_ext_id, ac_hom_create_usr_id, ac_hom_create_timestamp)
select rol_ext_id, 'CREDIT_SETTING_MAIN', 's1u3', sysdate from acRole where rol_ext_id like 'ADM_%';

-- 给TADM添加“积分管理”权限
insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('CREDIT_OTHER_MAIN', 'FUNCTION', 'HOMEPAGE', sysdate, null); 

insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_creation_timestamp)
select rol_id, ftn_id,sysdate from acRole, acFunction
where rol_ext_id like 'TADM_%' and ftn_ext_id = 'CREDIT_OTHER_MAIN';

insert into acHomePage (ac_hom_rol_ext_id,ac_hom_ftn_ext_id, ac_hom_create_usr_id, ac_hom_create_timestamp)
select rol_ext_id, 'CREDIT_OTHER_MAIN', 's1u3', sysdate from acRole where rol_ext_id like 'TADM_%';

-- 给积分点类型新增字段
ALTER TABLE creditsType ADD
	cty_update_usr_id nvarchar2(20) DEFAULT 's1u3' NOT NULL;
  
ALTER TABLE creditsType ADD
	cty_update_timestamp timestamp DEFAULT sysdate NOT NULL;
  
ALTER TABLE creditsType ADD
cty_hit int NULL;

ALTER TABLE creditsType ADD
	cty_period nvarchar2(20) NULL;

--修改知道积分点属性
UPDATE creditsType SET cty_relation_total_ind = 1 WHERE (cty_code = 'ZD_INIT');
UPDATE creditsType SET cty_relation_total_ind = 1, cty_period = 'MONTH', cty_hit = 10 WHERE (cty_code = 'ZD_COMMIT_ANS');
UPDATE creditsType SET cty_relation_total_ind = 1, cty_period = 'MONTH', cty_hit = 10 WHERE (cty_code = 'ZD_RIGHT_ANS');
UPDATE creditsType SET cty_relation_total_ind = 1, cty_period = 'MONTH', cty_hit = 10 WHERE (cty_code = 'ZD_CANCEL_QUE');
UPDATE creditsType SET cty_deduction_ind = 0, cty_relation_total_ind = 1, cty_default_credits =2 ,cty_period = 'MONTH', cty_hit = 10 WHERE (cty_code = 'ZD_NEW_QUE');


--插入自动积分点
--用户非首次登录
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_period,cty_hit)
VALUES('SYS_NORMAL_LOGIN','SYS_NORMAL_LOGIN',0,0,0,1,'SYS',1,2,'s1u3',sysdate,'s1u3',sysdate,'DAY',2);

--更改个人资料 
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_period,cty_hit)
VALUES('SYS_UPD_MY_PROFILE','SYS_UPD_MY_PROFILE',0,0,0,1,'SYS',1,5,'s1u3',sysdate,'s1u3',sysdate,'MONTH',2);

--参加公共调查问卷
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_period,cty_hit)
VALUES('SYS_SUBMIT_SVY','SYS_SUBMIT_SVY',0,0,0,1,'SYS',1,10,'s1u3',sysdate,'s1u3',sysdate,null,null);

--论坛发贴得分
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_period,cty_hit)
VALUES('SYS_INS_TOPIC','SYS_INS_TOPIC',0,0,0,1,'SYS',1,10,'s1u3',sysdate,'s1u3',sysdate,'MONTH',10);

--论坛回贴得分
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_period,cty_hit)
VALUES('SYS_INS_MSG','SYS_INS_MSG',0,0,0,1,'SYS',1,5,'s1u3',sysdate,'s1u3',sysdate,'MONTH',10);

--论坛共享资料得分
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_period,cty_hit)
VALUES('SYS_MSG_UPLOAD_RES','SYS_MSG_UPLOAD_RES',0,0,0,1,'SYS',1,5,'s1u3',sysdate,'s1u3',sysdate,'MONTH',10);

--成功报读培训
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_period,cty_hit)
VALUES('ITM_ENROLLED','ITM_ENROLLED',0,0,0,1,'COS',1,5,'s1u3',sysdate,'s1u3',sysdate,null,null);

--成绩达到60分或以上
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_period,cty_hit)
VALUES('ITM_SCORE_PAST_60','ITM_SCORE_PAST_60',0,0,0,1,'COS',1,5,'s1u3',sysdate,'s1u3',sysdate,null,null);

--成绩达到70分或以上
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_period,cty_hit)
VALUES('ITM_SCORE_PAST_70','ITM_SCORE_PAST_70',0,0,0,1,'COS',1,10,'s1u3',sysdate,'s1u3',sysdate,null,null);

--成绩达到80分或以上
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_period,cty_hit)
VALUES('ITM_SCORE_PAST_80','ITM_SCORE_PAST_80',0,0,0,1,'COS',1,15,'s1u3',sysdate,'s1u3',sysdate,null,null);

--成绩达到90分或以上
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_period,cty_hit)
VALUES('ITM_SCORE_PAST_90','ITM_SCORE_PAST_90',0,0,0,1,'COS',1,20,'s1u3',sysdate,'s1u3',sysdate,null,null);

--测验已通过
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_period,cty_hit)
VALUES('ITM__PAST_TEST','ITM_TEST_PAST',0,0,0,1,'COS',1,5,'s1u3',sysdate,'s1u3',sysdate,null,null);

--作业已提交
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_period,cty_hit)
VALUES('ITM_SUBMIT_ASS','ITM_SUBMIT_ASS',0,0,0,1,'COS',1,5,'s1u3',sysdate,'s1u3',sysdate,null,null);

--调查问卷已提交
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_period,cty_hit)
VALUES('ITM_SUBMIT_SVY','ITM_SUBMIT_SVY',0,0,0,1,'COS',1,5,'s1u3',sysdate,'s1u3',sysdate,null,null);

--教材已浏览
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_period,cty_hit)
VALUES('ITM_VIEW_RDG','ITM_VIEW_RDG',0,0,0,1,'COS',1,5,'s1u3',sysdate,'s1u3',sysdate,null,null);

--“课件”已浏览 
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_period,cty_hit)
VALUES('ITM_VIEW_COURSEWARE','ITM_VIEW_COURSEWARE',0,0,0,1,'COS',1,5,'s1u3',sysdate,'s1u3',sysdate,null,null);

--“课件”已完成
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_period,cty_hit)
VALUES('ITM_PAST_COURSEWARE','ITM_PAST_COURSEWARE',0,0,0,1,'COS',1,5,'s1u3',sysdate,'s1u3',sysdate,null,null);

--参考已浏览
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_period,cty_hit)
VALUES('ITM_VIEW_REF','ITM_VIEW_REF',0,0,0,1,'COS',1,5,'s1u3',sysdate,'s1u3',sysdate,null,null);

--视频点播已浏览
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_period,cty_hit)
VALUES('ITM_VIEW_VOD','ITM_VIEW_VOD',0,0,0,1,'COS',1,5,'s1u3',sysdate,'s1u3',sysdate,null,null);

--答疑栏已参与
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_period,cty_hit)
VALUES('ITM_VIEW_FAQ','ITM_VIEW_FAQ',0,0,0,1,'COS',1,5,'s1u3',sysdate,'s1u3',sysdate,null,null);

--培训论坛发贴得分
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_period,cty_hit)
VALUES('ITM_INS_TOPIC','ITM_INS_TOPIC',0,0,0,1,'COS',1,10,'s1u3',sysdate,'s1u3',sysdate,'MONTH',10);

--论坛回贴得分
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_period,cty_hit)
VALUES('ITM_INS_MSG','ITM_INS_MSG',0,0,0,1,'COS',1,5,'s1u3',sysdate,'s1u3',sysdate,'MONTH',10);

--论坛共享资料得分
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_period,cty_hit)
VALUES('ITM_MSG_UPLOAD_RES','ITM_MSG_UPLOAD_RES',0,0,0,1,'COS',1,5,'s1u3',sysdate,'s1u3',sysdate,'MONTH',10);

--导入课程积分
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_period,cty_hit)
VALUES('ITM_IMPORT_CREDIT','ITM_IMPORT_CREDIT',0,1,0,1,'COS',0,0,'s1u3',sysdate,'s1u3',sysdate,null,null);


/*
Auth: Joyce
Date: 2009-04-1
Desc: 修改测验已通过积分点code,title;
*/
update creditsType set cty_code='ITM_PAST_TEST', cty_title = 'ITM_PAST_TEST' where cty_code = 'ITM__PAST_TEST';


/*
Auth: Joyce
Date: 2009-04-1
Desc: 修改积分点类型code,title长度;
*/
alter table creditstype modify cty_code nvarchar2(255);


/*
Auth: Robin
Date: 2009-03-27
Desc: for Auto Credits
*/
ALTER TABLE aeItem ADD itm_bonus_ind int NULL;
ALTER TABLE aeItem ADD itm_diff_factor DECIMAL(8,2) NULL;

ALTER TABLE UserCreditsDetail ADD ucd_app_id int NULL
DROP INDEX IX_cty_ent_itm_1
CREATE  INDEX IX_cty_ent_itm_app_1 ON userCreditsDetail(ucd_ent_id, ucd_cty_id, ucd_itm_id, ucd_app_id)


/*
Auth: Dean
Date: 2009-03-19
Desc: module credit - credit report, my credit and credit chart
*/
insert into ReportTemplate
(rte_title_xml,rte_type,rte_get_xsl,rte_exe_xsl,rte_dl_xsl,rte_meta_data_url,rte_seq_no,rte_owner_ent_id,rte_create_usr_id,rte_create_timestamp,rte_upd_usr_id,rte_upd_timestamp)
select 'XML', 'CREDIT', 'rpt_credit_srh.xsl', 'rpt_credit_res.xsl', null, null, 14, ste_ent_id, 's1u3', sysdate, 's1u3', sysdate from acSite;

insert into acReportTemplate
(ac_rte_id,ac_rte_ent_id,ac_rte_rol_ext_id,ac_rte_ftn_ext_id,ac_rte_owner_ind,ac_rte_create_usr_id,ac_rte_create_timestamp)
select rte_id, null, rol_ext_id, 'RTE_READ', 0, 's1u3', sysdate 
from acRole, acHomePage, ReportTemplate
where rol_ste_ent_id <> 0
and rol_ext_id = ac_hom_rol_ext_id
and ac_hom_ftn_ext_id = 'RPT_LINK'
and rol_ste_ent_id = rte_owner_ent_id
and rte_type = 'CREDIT';


/*
Auth: Dean
Date: 2009-04-07
Desc: delete invaild type of credit 
*/
update creditsType set cty_deleted_ind = 1 where cty_code = 'ZD_CHOOSE_ANS';


/*
Auth: Dean
Date: 2009-04-07
Desc: update cty_relation_total_ind to agree with  module know
*/
update creditsType set cty_relation_total_ind = 1 where cty_code = 'ZD_CHOOSE_ANS';

/*
Auth: Robin
Date: 2009-04-08
Desc: 为积分的log增加app_id 字段
*/
ALTER TABLE userCreditsDetailLog ADD ucl_app_id int NULL;

/*
Auth: Harvey
Date: 2009-04-07
Desc: add ste_type for aeSite
*/	
ALTER TABLE acSite ADD ste_type nvarchar2(50) NULL;

update acSite set ste_type = '070084099097127100127101098075338';

ALTER TABLE acSite modify ste_type nvarchar2(50) NOT NULL;


/*
Auth: Harvey
Date: 2009-04-07
Desc: add table bereavedFunction
*/
CREATE TABLE bereavedFunction
	(
	brf_type nvarchar2(20) NOT NULL,
	brf_rol_ext_id nvarchar2(20) NOT NULL,
	brf_ftn_id int NOT NULL
	);

ALTER TABLE bereavedFunction ADD CONSTRAINT
	PK_bereavedFunction PRIMARY KEY
	(
	brf_type,
	brf_rol_ext_id,
	brf_ftn_id
	);


/*
Auth: Harvey
Date: 2009-04-08
Desc: add function "STUDY_GROUP_VIEW" & "CM_CENTER_VIEW" from learner
*/
insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('STUDY_GROUP_VIEW', 'FUNCTION', 'HOMEPAGE', sysdate, null); 

insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_creation_timestamp)
select rol_id, ftn_id,sysdate from acRole, acFunction
where rol_ext_id like 'NLRN_%' and ftn_ext_id = 'STUDY_GROUP_VIEW';

insert into acHomePage (ac_hom_rol_ext_id,ac_hom_ftn_ext_id, ac_hom_create_usr_id, ac_hom_create_timestamp)
select rol_ext_id, 'STUDY_GROUP_VIEW', 's1u3', sysdate from acRole where rol_ext_id like 'NLRN_%';


insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('CM_CENTER_VIEW', 'FUNCTION', 'HOMEPAGE', sysdate, null); 

insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_creation_timestamp)
select rol_id, ftn_id,sysdate from acRole, acFunction
where rol_ext_id like 'NLRN_%' and ftn_ext_id = 'CM_CENTER_VIEW';

insert into acHomePage (ac_hom_rol_ext_id,ac_hom_ftn_ext_id, ac_hom_create_usr_id, ac_hom_create_timestamp)
select rol_ext_id, 'CM_CENTER_VIEW', 's1u3', sysdate from acRole where rol_ext_id like 'NLRN_%';


/*
Auth: Harvey
Date: 2009-04-07
Desc: add dereaved function for STANDARD edition
*/
insert into bereavedFunction 
select 'STANDARD', 'TADM_1', ftn_id from acFunction where ftn_ext_id in 
('CM_MAIN', 'CM_ASS_MAIN','CM_SKL_ANALYSIS', 'STUDY_GROUP_MAIN'
,'PLAN_CARRY_OUT','YEAR_PALN','MAKEUP_PLAN','YEAR_PLAN_APPR','MAKEUP_PLAN_APPR','YEAR_SETTING');

insert into bereavedFunction 
select 'STANDARD', 'NLRN_1', ftn_id from acFunction where ftn_ext_id in 
('STUDY_GROUP_VIEW', 'CM_CENTER_VIEW');


/*
Auth: Robin
Date: 2009-04-08
Desc: fix the bug 8755 
*/
INSERT INTO acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_creation_timestamp) 
SELECT rol_id, ftn_id, sysdate FROM acRole, acFunction WHERE rol_ste_uid = 'EXA' AND ftn_ext_id = 'USR_OWN_MAIN';

INSERT INTO acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_creation_timestamp) 
SELECT rol_id, ftn_id, sysdate FROM acRole, acFunction WHERE rol_ste_uid = 'EXA' AND ftn_ext_id = 'USR_PWD_UPD';

INSERT INTO acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_creation_timestamp) 
SELECT rol_id, ftn_id, sysdate FROM acRole, acFunction WHERE rol_ste_uid = 'EXA' AND ftn_ext_id = 'USR_OWN_PREFER';


/*
Auth: Robin
Date: 2009-04-21
Desc: fix the bug 8774 
*/
INSERT INTO acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_creation_timestamp) 
SELECT rol_id, ftn_id, sysdate FROM acRole, acFunction WHERE rol_ste_uid = 'EXA' AND ftn_ext_id = 'USR_MGT_OWN';

/*
Auth: Shelley
Date: 2009-04-22
Desc: fix the bug 8753
*/
delete from acHomePage where ac_hom_ftn_ext_id = 'TO_DO_LIST';
delete from acRoleFunction where rfn_ftn_id in (
	select ftn_id from acFunction where ftn_ext_id = 'TO_DO_LIST'
);
delete from acFunction where ftn_ext_id = 'TO_DO_LIST';

/*
Auth: Harvey
Date: 2009-04-23
Desc: add dereaved function for STANDARD edition
*/
insert into bereavedFunction 
select 'STANDARD', 'TADM_1', ftn_id from acFunction where ftn_ext_id in ('TRA_PLA_MGT');


/*
Auth: Robin
Date: 2009-04-28
Desc: fix the bug 8818 
*/
ALTER TABLE UserCreditsDetail DROP CONSTRAINT PK_userCreditsDetail;

/*
Auth: Terry
Date: 2009-06-08
*/
update userCreditsDetail set ucd_total=ucd_hit*2 where ucd_cty_id in (select cty_id from creditsType where cty_code = 'ZD_NEW_QUE');

update userCreditsDetailLog set ucl_point=2 where ucl_bpt_id in (select cty_id from creditsType where cty_code = 'ZD_NEW_QUE');

/*
Auth: Shelley
Date: 因为knowQuestion表被改很多次，在oracle下会出ORA-00600（这个问题是oracle自己的bug，需要找oracle解决）导致库的数据损坏，改成把表knowQuestion重新建立一次
*/
alter table knowAnswer drop CONSTRAINT FK_kans_kque1;
alter table knowVoteDetail drop CONSTRAINT FK_kvd_kque_1;
drop table knowQuestion;
drop sequence knowQuestion_seq;

CREATE TABLE knowQuestion (
	que_id int NOT NULL ,
	que_title nvarchar2 (255) NOT NULL ,
	que_type nvarchar2 (20),
	que_content nvarchar2(500) NULL ,
	que_answered_timestamp timestamp NULL ,
	que_popular_ind int NOT NULL ,
	que_popular_timestamp timestamp NULL ,
	que_reward_credits int NOT NULL ,
	que_status nvarchar2 (20) NOT NULL ,
	que_create_ent_id int NOT NULL ,
	que_create_timestamp timestamp NOT NULL ,
	que_update_ent_id int NOT NULL ,
	que_update_timestamp timestamp NOT NULL 
) ;

CREATE SEQUENCE knowQuestion_seq;

CREATE OR REPLACE TRIGGER knowQuestion_trigger BEFORE INSERT ON knowQuestion
FOR EACH ROW
WHEN (new.que_id IS NULL)
BEGIN
 SELECT knowQuestion_seq.NEXTVAL INTO :new.que_id FROM DUAL;
END;

ALTER TABLE knowQuestion  ADD 
	CONSTRAINT PK_knowQuestion PRIMARY KEY   
	(
		que_id
	);

 CREATE  INDEX IX_knowQuestion_title ON knowQuestion(que_title);

ALTER TABLE knowAnswer ADD 
	CONSTRAINT FK_kans_kque1 FOREIGN KEY 
	(
		ans_que_id
	) REFERENCES knowQuestion (
		que_id
	);
	
ALTER TABLE knowVoteDetail ADD 
	CONSTRAINT FK_kvd_kque_1 FOREIGN KEY 
	(
		kvd_que_id
	) REFERENCES knowQuestion (
		que_id
	);
--r17400 (v5.0.1 to v5.1)



/*
Auth: Wrren
Date: 2010-10-18
Desc: add defined project function
*/  

insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('DEFINED_PROJECT', 'FUNCTION', 'HOMEPAGE', sysdate, null);

INSERT INTO acRoleFunction 
select rol_id, ftn_id, 'slu3', sysdate from acRole, acFunction where rol_ste_uid in ('SADM') 
and ftn_ext_id = 'DEFINED_PROJECT';

INSERT INTO acHomepage 
select null, rol_ext_id, 'DEFINED_PROJECT', 's1u3', sysdate from acRole where rol_ste_uid in ('SADM');

create sequence DefinedProject_seq;
create table DefinedProject(
dpt_id int primary key,
dpt_tcr_id int not null,
dpt_code nvarchar2(255) unique not null,
dpt_title nvarchar2(200) not null,
dpt_status nvarchar2(20) default 'OFF',
dpt_way nvarchar2(20) default 'LEFT',
dpt_create_timestamp timestamp not null,
dpt_create_usr_id nvarchar2(20) not null,
dpt_update_timestamp timestamp not null,
dpt_update_usr_id nvarchar2(20) not null
);

CREATE OR REPLACE TRIGGER DefinedProject_trigger BEFORE INSERT ON DefinedProject
FOR EACH ROW
WHEN (new.dpt_id IS NULL)
BEGIN
 SELECT DefinedProject_seq.NEXTVAL INTO :new.dpt_id FROM DUAL;
END;

--add foreign key constarint
alter table DefinedProject add constraint defind_for_cons foreign key (dpt_tcr_id)
  references tcTrainingCenter (tcr_id);

---add checek constraint
alter table DefinedProject add constraint check_status check(dpt_status in('ON','OFF'));
alter table DefinedProject add constraint check_dpt_way check(dpt_way in('LEFT','RIGHT'));

/*
Desc: support defined project link
*/ 
create sequence projectLink_seq;
create table projectLink(
pjl_id int primary key,
pjl_dpt_id int not null,
pjl_code nvarchar2(255) unique not null,
pjl_title nvarchar2(255) not null,
pjl_status nvarchar2(20) default 'ON' not null,
pjl_url nvarchar2(255) not null,
pjl_create_timestamp timestamp not null,
pjl_create_usr_id nvarchar2(20) not null,
pjl_update_timestamp timestamp not null,
pjl_update_usr_id nvarchar2(20) not null,
foreign key (pjl_dpt_id) references DefinedProject(dpt_id),
check (pjl_status in ('ON','OFF')));

CREATE OR REPLACE TRIGGER projectLink_trigger BEFORE INSERT ON projectLink
FOR EACH ROW
WHEN (new.pjl_id IS NULL)
BEGIN
 SELECT projectLink_seq.NEXTVAL INTO :new.pjl_id FROM DUAL;
END;

/*
Auth: Wrren
Date: 2010-10-18
Desc: support function of student home page display
*/
create sequence TcrModule_seq;
create table TcrModule(
tm_id int primary key,
tm_tcr_id int not null,
tm_code nvarchar2(255) unique not null,
tm_title_gb nvarchar2(255) not null,
tm_title_ch nvarchar2(255) not null,
tm_title_en nvarchar2(255) not null,
tm_is_centre int default 0 not null,
constraint tm_is_centre_cons check (tm_is_centre in (0,1)));

CREATE OR REPLACE TRIGGER TcrModule_trigger BEFORE INSERT ON TcrModule
FOR EACH ROW
WHEN (new.tm_id IS NULL)
BEGIN
 SELECT TcrModule_seq.NEXTVAL INTO :new.tm_id FROM DUAL;
END;

create sequence AdditivedTcrModule_seq;
create table AdditivedTcrModule(
atm_id int primary key,
atm_x_mod_value int not null,
atm_added_ind nvarchar2(10) not null,
constraint atm_id_cons foreign key(atm_id) references TcrModule (tm_id),
constraint atm_x_value_cons check(atm_x_mod_value in (0, 1, 2)),
constraint atm_added_ind_cons check(atm_added_ind in ('UNADDED', 'ADDED'))
);

CREATE OR REPLACE TRIGGER AdditivedTcrModule_trigger BEFORE INSERT ON AdditivedTcrModule
FOR EACH ROW
WHEN (new.atm_id IS NULL)
BEGIN
 SELECT AdditivedTcrModule_seq.NEXTVAL INTO :new.atm_id FROM DUAL;
END;

create sequence TcrTemplate_seq;
create table TcrTemplate(
tt_id int primary key,
tt_tcr_id int unique not null,
tt_dis_fun_navigation_ind int default 1 not null,
tt_create_timestamp timestamp not null,
tt_create_usr_id nvarchar2(20) not null,
tt_update_timestamp timestamp not null,
tt_update_usr_id nvarchar2(20) not null,
constraint tt_for_tcr foreign key (tt_tcr_id) references tcTrainingCenter (tcr_id)
);

CREATE OR REPLACE TRIGGER TcrTemplate_trigger BEFORE INSERT ON TcrTemplate
FOR EACH ROW
WHEN (new.tt_id IS NULL)
BEGIN
 SELECT TcrTemplate_seq.NEXTVAL INTO :new.tt_id FROM DUAL;
END;

create sequence TcrTemplateModule_seq;
create table TcrTemplateModule(
ttm_id int primary key,
ttm_tm_id int not null,
ttm_tt_id int not null,
ttm_mod_x_value int not null,
ttm_mod_y_value int not null,
ttm_mod_status int default 1,
constraint ttm_tm_id_for_cons foreign key (ttm_tm_id) references TcrModule(tm_id),
constraint ttm_tt_id_for_cons foreign key (ttm_tt_id) references TcrTemplate(tt_id),
constraint ttm_mod_status_cons check (ttm_mod_status in (0,1))
);

CREATE OR REPLACE TRIGGER TcrTemplateModule_trigger BEFORE INSERT ON TcrTemplateModule
FOR EACH ROW
WHEN (new.ttm_id IS NULL)
BEGIN
 SELECT TcrTemplateModule_seq.NEXTVAL INTO :new.ttm_id FROM DUAL;
END;

--init TcrModule data
insert into TcrModule (tm_tcr_id, tm_code, tm_title_gb, tm_title_ch, tm_title_en, tm_is_centre) 
values (0, 'APPROVE_DEMAND', 'title', 'title', 'title', 0);

insert into TcrModule (tm_tcr_id, tm_code, tm_title_gb, tm_title_ch, tm_title_en, tm_is_centre) 
values (0, 'RESEARCH_QUESTIONNAIRE', 'title', 'title', 'title', 0);

insert into TcrModule (tm_tcr_id, tm_code, tm_title_gb, tm_title_ch, tm_title_en, tm_is_centre) 
values (0, 'ABILITY_EVALUATE_QUESTIONNAIRE', 'title', 'title', 'title', 0);

insert into TcrModule (tm_tcr_id, tm_code, tm_title_gb, tm_title_ch, tm_title_en, tm_is_centre) 
values (0, 'PUBLIC_NOTICE', 'title', 'title', 'title', 0);

insert into TcrModule (tm_tcr_id, tm_code, tm_title_gb, tm_title_ch, tm_title_en, tm_is_centre) 
values (0, 'TRANING_CATALOG', 'title', 'title', 'title', 0);

insert into TcrModule (tm_tcr_id, tm_code, tm_title_gb, tm_title_ch, tm_title_en, tm_is_centre) 
values (0, 'ONELINE_ANSWERS', 'title', 'title', 'title', 0);

insert into TcrModule (tm_tcr_id, tm_code, tm_title_gb, tm_title_ch, tm_title_en, tm_is_centre) 
values (0, 'KNOWLEDGE_CENTER', 'title', 'title', 'title', 0);

insert into TcrModule (tm_tcr_id, tm_code, tm_title_gb, tm_title_ch, tm_title_en, tm_is_centre) 
values (0, 'USER_POSTER', 'title', 'title', 'title', 1);

insert into TcrModule (tm_tcr_id, tm_code, tm_title_gb, tm_title_ch, tm_title_en, tm_is_centre) 
values (0, 'INTEGRATED_TRANING', 'title', 'title', 'title', 1);

insert into TcrModule (tm_tcr_id, tm_code, tm_title_gb, tm_title_ch, tm_title_en, tm_is_centre) 
values (0, 'LEARNING_CENTRE', 'title', 'title', 'title', 1);

insert into TcrModule (tm_tcr_id, tm_code, tm_title_gb, tm_title_ch, tm_title_en, tm_is_centre) 
values (0, 'EXAM_CENTRE', 'title', 'title', 'title', 1); 

insert into TcrModule (tm_tcr_id, tm_code, tm_title_gb, tm_title_ch, tm_title_en, tm_is_centre) 
values (0, 'STUDY_GROUP', 'title', 'title', 'title', 1);

insert into TcrModule (tm_tcr_id, tm_code, tm_title_gb, tm_title_ch, tm_title_en, tm_is_centre) 
values (0, 'SYSTEM_INFORS', 'title', 'title', 'title', 0);

insert into TcrModule (tm_tcr_id, tm_code, tm_title_gb, tm_title_ch, tm_title_en, tm_is_centre) 
values (0, 'PRIVATE_INFORS', 'title', 'title', 'title', 0);

insert into TcrModule (tm_tcr_id, tm_code, tm_title_gb, tm_title_ch, tm_title_en, tm_is_centre) 
values (0, 'CREDIT_RANKING', 'title', 'title', 'title', 0); 

insert into TcrModule (tm_tcr_id, tm_code, tm_title_gb, tm_title_ch, tm_title_en, tm_is_centre) 
values (0, 'TRANING_RANKING', 'title', 'title', 'title', 0); 

insert into TcrModule (tm_tcr_id, tm_code, tm_title_gb, tm_title_ch, tm_title_en, tm_is_centre) 
values (0, 'FRIEND_LINK', 'title', 'title', 'title', 0);

alter table reguser add usr_choice_tcr_id int default 0;



/*
Auth: Wrren
Date: 2010-09-20
Desc: perfect defined project function
*/
INSERT INTO acRoleFunction 
select rol_id, ftn_id, 'slu3', sysdate from acRole, acFunction where rol_ste_uid in ('TADM') 
and ftn_ext_id = 'DEFINED_PROJECT';

INSERT INTO acHomepage 
select null, rol_ext_id, 'DEFINED_PROJECT', 's1u3', sysdate from acRole where rol_ste_uid in ('TADM');


/*
Auth: Wrren
Date: 2010-10-18
Desc: perfect function of student home page display
*/
drop trigger AdditivedTcrModule_trigger;
drop sequence Additivedtcrmodule_Seq;
drop table AdditivedTcrModule;
create table AdditivedTcrModule(
atm_tm_code nvarchar2(255) unique not null,
atm_added_ind nvarchar2(10) not null,
constraint atm_tm_code_cons foreign key(atm_tm_code) references TcrModule (tm_code),
constraint atm_added_ind_cons check(atm_added_ind in ('UNADDED', 'ADDED'))
);


/*
Auth: Wrren
Date: 2011-3-16
Desc: add function of student qq consultation
*/
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('QQ_CONSULTATION', 'FUNCTION', 'HOMEPAGE', sysdate, null);

INSERT INTO acRoleFunction 
select rol_id, ftn_id, 'slu3', sysdate from acRole, acFunction where rol_ste_uid in ('SADM') 
and ftn_ext_id = 'QQ_CONSULTATION';

INSERT INTO acHomepage 
select null, rol_ext_id, 'QQ_CONSULTATION', 's1u3', sysdate from acRole where rol_ste_uid in ('SADM');

INSERT INTO acRoleFunction 
select rol_id, ftn_id, 'slu3', sysdate from acRole, acFunction where rol_ste_uid in ('TADM') 
and ftn_ext_id = 'QQ_CONSULTATION';

INSERT INTO acHomepage 
select null, rol_ext_id, 'QQ_CONSULTATION', 's1u3', sysdate from acRole where rol_ste_uid in ('TADM');

insert into TcrModule (tm_tcr_id, tm_code, tm_title_gb, tm_title_ch, tm_title_en, tm_is_centre) 
values (0, 'QQ_CONSULTATION', 'title', 'title', 'title', 0);
insert into AdditivedTcrModule(atm_tm_code, atm_added_ind) values ('QQ_CONSULTATION', 'UNADDED');

create sequence companyQQ_seq;
create table companyQQ(
cpq_id number(19) primary key not null,
cpq_code nvarchar2(255) unique,
cpq_title nvarchar2(255),
cpq_number nvarchar2(50),
cpq_desc nvarchar2(500),
cpq_status nvarchar2(20) default 'ON',
cpq_create_timestamp timestamp default sysdate,
cpq_create_ent_id number(19),
cpq_update_timestamp timestamp default sysdate,
cpq_update_ent_id number(19),
constraint cpp_status_cons check (cpq_status in ('ON', 'OFF'))
);
CREATE OR REPLACE TRIGGER companyQQ_trigger BEFORE INSERT ON companyQQ
FOR EACH ROW
WHEN (new.cpq_id IS NULL)
BEGIN
 SELECT companyQQ_seq.NEXTVAL INTO :new.cpq_id FROM DUAL;
END;




/*
Auth: Willy
Date: 2011-04-01
Desc: modity the column type
*/
alter table userCreditsDetailLog modify ucl_point float;
DROP INDEX IX_uct_total;
DROP INDEX IX_uct_zd_total;
alter table userCredits modify uct_total float;
alter table userCreditsDetail modify ucd_total float;

/*
Auth: Joyce
Date: 2012-5-23
Desc: 屏蔽QQ咨询
*/
delete additivedtcrmodule where atm_tm_code='QQ_CONSULTATION';
delete tcrtemplatemodule where ttm_tm_id =18;
delete tcrmodule where tm_code= 'QQ_CONSULTATION';
commit;



/*
添加取目标学员存储过程，
ORACLE的存储过程，
Auth: randy
*/
CREATE OR REPLACE PROCEDURE getItemsForTargetUser(UserId in Numeric, p_CURSOR out sys_refcursor) IS 
BEGIN
    OPEN p_CURSOR FOR 
     SELECT DISTINCT itm_id FROM aeItem, aeItemTargetRuleDetail 
    WHERE   itm_access_type = ird_type  
    AND itm_id = ird_itm_id 
    AND ird_group_id in (Select ern_ancestor_ent_id From EntityRelation Where ern_type = 'USR_PARENT_USG' And ern_child_ent_id = UserId) 
    AND ird_grade_id in (Select ern_ancestor_ent_id From EntityRelation Where ern_type = 'USR_CURRENT_UGR' And ern_child_ent_id = UserId)

    AND (ird_skill_id =-1 or ird_skill_id in (Select uss_ske_id From RegUserSkillSet Where uss_ent_id= UserId))    
    UNION 
    SELECT itm_id FROM aeItem
    WHERE  itm_access_type = 'ALL' OR itm_access_type is NULL;

END getItemsForTargetUser;
commit



/*
oracle 修改列
*/
alter table reguser  rename column usr_login_status_xml to usr_login_status ;


/*
Auth: randy
Date: 2011-09-21
Desc: 修改学员首URL
*/
update acrole set rol_url_home = 'app/home' where rol_ext_id = 'NLRN_1' ;


/*
Auth: randy
Date: 2013-05-81
Desc: 迁移证书
*/
CREATE TABLE certificate(
	cfc_id int  NOT NULL ,
	cfc_title nvarchar2 (100) NOT NULL ,
	cfc_img nvarchar2 (200) NOT NULL ,
	cfc_tcr_id int NOT NULL,
	cfc_status nvarchar2(10) NOT NULL,	
	cfc_create_datetime timestamp,
	cfc_create_user_id nvarchar2(50),
  	cfc_update_datetime timestamp,
 	cfc_update_user_id nvarchar2(50),
	cfc_delete_datetime timestamp,
	cfc_delete_user_id nvarchar2(50),
	cfc_code nvarchar2(255) not NULL,
	cfc_end_date timestamp,
	CONSTRAINT PK_CFC_ID PRIMARY KEY (cfc_id),
   	CONSTRAINT FK_CFC_ID  FOREIGN KEY (cfc_tcr_id) references tcTrainingCenter(tcr_id)
)
create sequence certificate_seq;
CREATE OR REPLACE TRIGGER certificate_trigger BEFORE INSERT ON certificate
FOR EACH ROW
WHEN (new.cfc_id IS NULL)
BEGIN
 SELECT certificate_seq.NEXTVAL INTO :new.cfc_id FROM DUAL;
END;


/*
Author: walker liu
Date: 2010-05-18
Desc: Create certificate:添加一个FUNC，并分配权限给TADM 
*/
insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('CERTIFICATE_MAIN', 'FUNCTION', 'HOMEPAGE', to_date('2013-06-04 00:00:00','YYYY-mm-dd hh24:mi:ss'), null);

insert into acRoleFunction (rfn_rol_id, rfn_ftn_id)
select rol_id, ftn_id from acRole, acFunction
where rol_ext_id like 'TADM_%' and ftn_ext_id = 'CERTIFICATE_MAIN';

insert into acHomePage (ac_hom_rol_ext_id,ac_hom_ftn_ext_id, ac_hom_create_usr_id, ac_hom_create_timestamp)
select rol_ext_id, 'CERTIFICATE_MAIN', 's1u3', to_date('2013-06-04 00:00:00','YYYY-mm-dd hh24:mi:ss') from acRole where rol_ext_id like 'TADM_%';
commit;

/*
Auth: randy
Date: 2010-05-10
Desc: 课程证书 
*/
ALTER TABLE aeItem ADD
	itm_cfc_id int NULL


 

/* 
 Auth: leon
添加供应商管理TADM的权限
*/
insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('SUPPLIER_MAIN', 'FUNCTION', 'HOMEPAGE', to_date('2013-06-05 00:00:00','YYYY-mm-dd hh24:mi:ss'), null);

insert into acRoleFunction (rfn_rol_id, rfn_ftn_id)
select rol_id, ftn_id from acRole, acFunction
where rol_ext_id like 'TADM_%' and ftn_ext_id = 'SUPPLIER_MAIN';

insert into acHomePage (ac_hom_rol_ext_id,ac_hom_ftn_ext_id, ac_hom_create_usr_id, ac_hom_create_timestamp)
select rol_ext_id, 'SUPPLIER_MAIN', 's1u3', to_date('2013-06-05 00:00:00','YYYY-mm-dd hh24:mi:ss') from acRole where rol_ext_id like 'TADM_%';
commit;
/*
Auth: leon
对象:  Table supplier    脚本日期: 06/04/2013 17:09:42 
*/
CREATE TABLE supplier(
	spl_id int NOT NULL,
	spl_name nvarchar2(255) NOT NULL,
	spl_established_date timestamp NULL,
	spl_registered_capital float NULL,
	spl_type nvarchar2(255)   NULL,
	spl_address nvarchar2(255)   NULL,
	spl_post_num nvarchar2(255)   NULL,
	spl_contact nvarchar2(255)   NULL,
	spl_tel nvarchar2(255)   NULL,
	spl_mobile nvarchar2(255)   NULL,
	spl_email nvarchar2(255)   NULL,
	spl_total_staff int NULL,
	spl_full_time_inst int NULL,
	spl_part_time_inst int NULL,
	spl_expertise nvarchar2(1024)   NULL,
	spl_course nvarchar2(1024)   NULL,
	spl_status nvarchar2(50)   NULL,
	spl_attachment_1 nvarchar2(255)   NULL,
	spl_attachment_2 nvarchar2(255)   NULL,
	spl_attachment_3 nvarchar2(255)   NULL,
	spl_attachment_4 nvarchar2(255)   NULL,
	spl_attachment_5 nvarchar2(255)   NULL,
	spl_attachment_6 nvarchar2(255)   NULL,
	spl_attachment_7 nvarchar2(255)   NULL,
	spl_attachment_8 nvarchar2(255)   NULL,
	spl_attachment_9 nvarchar2(255)   NULL,
	spl_attachment_10 nvarchar2(255)   NULL,
	spl_create_datetime timestamp NULL,
	spl_create_usr_id nvarchar2(50)   NULL,
	spl_update_datetime timestamp NULL,
	spl_update_usr_id nvarchar2(50)   NULL,
	spl_representative nvarchar2(255)   NULL,
	CONSTRAINT PK_SPL_ID PRIMARY KEY (spl_id)
) 
create sequence supplier_seq;
CREATE OR REPLACE TRIGGER supplier_trigger BEFORE INSERT ON supplier
FOR EACH ROW
WHEN (new.spl_id IS NULL)
BEGIN
 SELECT supplier_seq.NEXTVAL INTO :new.spl_id FROM DUAL;
END;


/*
Auth: leon
对象:  Table dbo.supplierComment
*/
CREATE TABLE supplierComment(
	scm_id int NOT NULL,
	scm_spl_id int NOT NULL,
	scm_ent_id int NOT NULL,
	scm_design_score float NULL,
	scm_teaching_score float NULL,
	scm_price_score float NULL,
	scm_comment nvarchar2(500)   NULL,
	scm_update_datetime timestamp NULL,
	scm_create_datetime timestamp NULL,
	scm_management_score float NULL,
	CONSTRAINT PK_SCM_ID PRIMARY KEY (scm_id),
	CONSTRAINT FK_SCM_SPL_ID  FOREIGN KEY (scm_spl_id) references supplier(spl_id),
	CONSTRAINT FK_SCM_ENT_ID  FOREIGN KEY (scm_spl_id) references entity(ent_id)
) 
create sequence supplier_comment_seq;
CREATE OR REPLACE TRIGGER supplier_comment_trigger BEFORE INSERT ON supplierComment
FOR EACH ROW
WHEN (new.scm_id IS NULL)
BEGIN
 SELECT supplier_comment_seq.NEXTVAL INTO :new.scm_id FROM DUAL;
END;

CREATE INDEX supplierComment_INDEX ON supplierComment(scm_spl_id)


/*
Auth: leon
对象:  Table dbo.supplierCooperationExperience
*/
CREATE TABLE supplierCooperationExperience(
	sce_id int NOT NULL,
	sce_spl_id int NULL,
	sce_itm_name nvarchar2(255)  NULL,
	sce_start_date timestamp NULL,
	sce_end_date timestamp NULL,
	sce_desc nvarchar2(500)  NULL,
	sce_dpt nvarchar2(255)  NULL,
	sce_update_datetime timestamp NULL,
	sce_update_usr_id nvarchar2(50)  NULL,
	CONSTRAINT PK_SCE_ID PRIMARY KEY (sce_id),
	CONSTRAINT FK_SCE_SPL_ID  FOREIGN KEY (sce_spl_id) references supplier(spl_id)
) 
create sequence supplier_c_e_seq;
CREATE OR REPLACE TRIGGER supplier_c_e_trigger BEFORE INSERT ON supplierCooperationExperience
FOR EACH ROW
WHEN (new.sce_id IS NULL)
BEGIN
 SELECT supplier_c_e_seq.NEXTVAL INTO :new.sce_id FROM DUAL;
END;

CREATE INDEX SUPPLIER_SCE_INDEX ON supplierCooperationExperience(sce_spl_id)

/* 
对象:  Table dbo.supplierMainCourse   
 脚本日期: 06/04/2013 17:12:15 
 */
CREATE TABLE supplierMainCourse(
	smc_id int NOT NULL,
	smc_spl_id int NOT NULL,
	smc_name nvarchar2(255)  NULL,
	smc_inst nvarchar2(255)  NULL,
	smc_price float NULL,
	smc_update_datetime timestamp NULL,
	smc_update_usr_id nvarchar2(50)  NULL,
	CONSTRAINT PK_SMC_ID PRIMARY KEY (smc_id),
	CONSTRAINT FK_SMC_SPL_ID  FOREIGN KEY (smc_spl_id) references supplier(spl_id)
) 
create sequence supplier_m_c_seq;
CREATE OR REPLACE TRIGGER supplier_m_c_trigger BEFORE INSERT ON supplierMainCourse
FOR EACH ROW
WHEN (new.smc_id IS NULL)
BEGIN
 SELECT supplier_m_c_seq.NEXTVAL INTO :new.smc_id FROM DUAL;
END;
CREATE INDEX SUPPLIER_SMC_INDEX ON supplierMainCourse(smc_spl_id)


/*
Auth: Elvea
Date: 2013-05-24
Desc: 讲师维护
*/
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('EXT_INSTRUCTOR', 'FUNCTION', 'HOMEPAGE', to_date('2013-06-14 13:47:00','YYYY-mm-dd hh24:mi:ss'), null);

INSERT INTO acRoleFunction
select rol_id, ftn_id, 'slu3', to_date('2013-06-14 13:47:00','YYYY-mm-dd hh24:mi:ss') from acRole, acFunction where rol_ste_uid in ('TADM')
and ftn_ext_id = 'EXT_INSTRUCTOR';


insert into acHomePage (ac_hom_rol_ext_id,ac_hom_ftn_ext_id, ac_hom_create_usr_id, ac_hom_create_timestamp)
select rol_ext_id, 'EXT_INSTRUCTOR', 's1u3', to_date('2013-06-14 13:47:00','YYYY-mm-dd hh24:mi:ss') from acRole where rol_ste_uid in ('TADM');


insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('INT_INSTRUCTOR', 'FUNCTION', 'HOMEPAGE', to_date('2013-06-14 13:47:00','YYYY-mm-dd hh24:mi:ss'), null);

INSERT INTO acRoleFunction
select rol_id, ftn_id, 'slu3', to_date('2013-06-14 13:47:00','YYYY-mm-dd hh24:mi:ss') from acRole, acFunction where rol_ste_uid in ('TADM')
and ftn_ext_id = 'INT_INSTRUCTOR';

insert into acHomePage (ac_hom_rol_ext_id,ac_hom_ftn_ext_id, ac_hom_create_usr_id, ac_hom_create_timestamp)
select rol_ext_id, 'INT_INSTRUCTOR', 's1u3', to_date('2013-06-14 13:47:00','YYYY-mm-dd hh24:mi:ss') from acRole where rol_ste_uid in ('TADM');

commit;

create table InstructorInf (
	iti_ent_id int not null,
	iti_name nvarchar2(255),
	iti_gender nvarchar2(20),
	iti_bday timestamp,
	iti_mobile nvarchar2(255),
	iti_email nvarchar2(255),
	iti_img	nvarchar2(255),
	iti_introduction nvarchar2(1024),
	iti_level nvarchar2(20),
	iti_cos_type nvarchar2(20),
	iti_main_course	nvarchar2(1024),
	iti_type nvarchar2(255),
	iti_property nvarchar2(255),
	iti_highest_educational nvarchar2(255),
	iti_graduate_institutions nvarchar2(255),
	iti_address nvarchar2(255),
	iti_work_experience nvarchar2(255),
	iti_education_experience nvarchar2(255),
	iti_training_experience nvarchar2(255),
	iti_expertise_areas nvarchar2(255),
	iti_good_industry nvarchar2(255),
	iti_training_company nvarchar2(255),
	iti_training_contacts nvarchar2(255),
	iti_training_tel nvarchar2(255),
	iti_training_email nvarchar2(255),
	iti_training_address nvarchar2(255),
	iti_status nvarchar2(20),
	iti_type_mark nvarchar2(20),
	iti_score float,
	iti_create_datetime timestamp,
	iti_create_user_id nvarchar2(50),
	iti_update_datetime timestamp,
	iti_update_user_id nvarchar2(50),
	constraint PK_ITI_ID primary key (iti_ent_id)
);
/*	
create sequence InstructorInf_seq;
CREATE OR REPLACE TRIGGER InstructorInf_trigger BEFORE INSERT ON InstructorInf
FOR EACH ROW
WHEN (new.iti_ent_id IS NULL)
BEGIN
 SELECT InstructorInf_seq.NEXTVAL INTO :new.iti_ent_id FROM DUAL;
END;
*/

create table InstructorCos (
	ics_id int not null,
	ics_iti_ent_id int not null,
	ics_title nvarchar2(255),
	ics_fee float,
	ics_hours float,
	ics_target	nvarchar2(255),
	ics_content	nvarchar2(255),
	constraint PK_ICS_ID primary key (ics_id),
	constraint PK_ICS_ENT_ID foreign key (ics_iti_ent_id) references Entity(ent_id)
);	
create sequence InstructorCos_seq;
CREATE OR REPLACE TRIGGER InstructorCos_trigger BEFORE INSERT ON InstructorCos
FOR EACH ROW
WHEN (new.ics_id IS NULL)
BEGIN
 SELECT InstructorCos_seq.NEXTVAL INTO :new.ics_id FROM DUAL;
END;

create table InstructorComment (
	itc_id int not null,
	itc_itm_id int not null,
	itc_ent_id int not null,
	itc_iti_ent_id int not null,
	itc_style_score float,
	itc_quality_score float,
	itc_structure_score float,
	itc_interaction_score float,
	itc_score float,
	itc_comment	nvarchar2(255),
	itc_create_datetime timestamp,
	itc_create_user_id nvarchar2(50),
	itc_update_datetime timestamp,
	itc_update_user_id nvarchar2(50),
	constraint PK_ITC_ID primary key (itc_id),
	constraint PK_ITC_ITM_ID foreign key (itc_itm_id) references aeItem(itm_id),
	constraint PK_ITC_ENT_ID foreign key (itc_ent_id) references Entity(ent_id),
	constraint PK_ITC_ITI_ENT_ID foreign key (itc_iti_ent_id) references Entity(ent_id)
);				
create sequence InstructorComment_seq;
CREATE OR REPLACE TRIGGER InstructorComment_trigger BEFORE INSERT ON InstructorComment
FOR EACH ROW
WHEN (new.itc_id IS NULL)
BEGIN
 SELECT InstructorComment_seq.NEXTVAL INTO :new.itc_id FROM DUAL;
END;

/*
Auth: randy
Date: 2013-06-13
Desc: 培训单元讲师
*/
ALTER TABLE aeItem ADD  itm_inst_type nvarchar2(52) NULL;
ALTER TABLE aeItemLessonInstructor DROP CONSTRAINT FK_aeItemLessonInstructor_RegU;
ALTER TABLE aeItemLessonInstructor ADD CONSTRAINT FK_aeILInstructor_Entity FOREIGN KEY(ili_usr_ent_id) REFERENCES Entity(ent_id)



/*
Auth: kevin
Date: 2013-06-13
Desc: 供应商记录总分
*/
ALTER TABLE supplierComment ADD scm_score float NULL;

/*
Auth: Elvea
Date: 2013-06-17
Desc: 
*/
alter table InstructorInf modify iti_expertise_areas nvarchar2(1024);
alter table InstructorInf modify iti_good_industry nvarchar2(1024);
alter table InstructorInf modify iti_address nvarchar2(1024);
alter table InstructorInf modify iti_work_experience nvarchar2(1024);
alter table InstructorInf modify iti_education_experience nvarchar2(1024);
alter table InstructorInf modify iti_training_experience nvarchar2(1024);

/*
Auth: leon
Date: 2013-06-19
Desc: 
*/
alter table supplier modify spl_registered_capital nvarchar2(255);


/*
Auth: randy
Date: 2013-06-19
Desc: 
*/
alter table AEAPPNCOMMHISTORY modify ACH_CONTENT  NVARCHAR2(4000);




/*
Auth: kevin
Date: 2013-7-5
Desc: 添加宣告栏
*/
ALTER TABLE SitePoster ADD sp_media_file1 nvarchar2(50);
ALTER TABLE SitePoster ADD sp_url1 nvarchar2(255);
ALTER TABLE SitePoster ADD sp_status1 nvarchar2(50);
ALTER TABLE SitePoster ADD sp_media_file2 nvarchar2(50);
ALTER TABLE SitePoster ADD sp_url2 nvarchar2(255);
ALTER TABLE SitePoster ADD sp_status2 nvarchar2(50);
ALTER TABLE SitePoster ADD sp_media_file3 nvarchar2(50);
ALTER TABLE SitePoster ADD sp_url3 nvarchar2(255);
ALTER TABLE SitePoster ADD sp_status3 nvarchar2(50);
ALTER TABLE SitePoster ADD sp_media_file4 nvarchar2(50);
ALTER TABLE SitePoster ADD sp_url4 nvarchar2(255);
ALTER TABLE SitePoster ADD sp_status4 nvarchar2(50);

/*
Auth: randy
Date: 2013-7-10
Desc: 默认宣告栏
*/
delete from sitePoster;
insert into  sitePoster (sp_ste_id,sp_media_file,sp_url,sp_status) values(1,'banner01.png',NULL,'ON');





/*
Auth: kevin
Date: 2013-7-23
Desc: 课程评估问卷添加 "所属培训中心"
*/
ALTER TABLE Module ADD mod_tcr_id int;


/*
Auth: kevin
Date: 2013-7-24
Desc: 学员考试成绩合格才显示题目和答案
*/
alter table module  add mod_show_A_A_passed_ind  int



/*
Auth: 
Date: 2013-07-25
Desc: 积分规则可填写小数,把原来的数据类型改为float
*/
ALTER TABLE creditstype modify (cty_default_credits float)





/*
Auth: 
Date: 2014-01-20
Desc: Oracle
*/
/* begin ************************************************************************************************************* */
/*
Auth: randy
Date: 2013-7-10
Desc: 默认宣告栏
*/
delete from sitePoster;
insert into  sitePoster (sp_ste_id,sp_media_file,sp_url,sp_status) values(1,'banner01.png',NULL,'ON');

/*
Auth: kevin
Date: 2013-7-23
Desc: 课程评估问卷添加 "所属培训中心"
*/
ALTER TABLE Module ADD mod_tcr_id int;


/*
Auth: kevin
Date: 2013-7-24
Desc: 学员考试成绩合格才显示题目和答案
*/
alter table module  add mod_show_A_A_passed_ind  int




/*
Auth: 
Date: 2013-07-25
Desc: 积分规则可填写小数,把原来的数据类型改为float
*/
ALTER TABLE creditstype modify cty_default_credits float;



/*
Auth: Mike
Date: 2012-03-27
Desc: 培训管理员添加职务维护权限
*/
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_usr_id, rfn_create_timestamp)
select rol_id, ftn_id, 's1u3', sysdate from acRole, acFunction where ftn_ext_id = 'USR_MGT_GRADE' and rol_ext_id = 'TADM_1';


select * from acFunction where ftn_ext_id = 'USR_MGT_GRADE'

delete from acRoleFunction where rfn_ftn_id in ( select ftn_id from acFunction where ftn_ext_id = 'USR_MGT_GRADE')
and rfn_rol_id in (select rol_id from acRole where rol_ste_uid = 'TADM')






-- 职务维护添加与培训中心关联字段
ALTER TABLE usergrade ADD ugr_tcr_id INT;

update usergrade set ugr_tcr_id = 1;

/*
Auth: Harvey
Date: 2012-5-8
Desc: ApiToken
*/
create table APIToken(
	atk_id nvarchar2(64) not null,
	atk_usr_id nvarchar2(64) not null,
	atk_usr_ent_id int,
	atk_create_timestamp timestamp,
	atk_expiry_timestamp timestamp,
	atk_developer_id nvarchar2(128) not null
);


alter table acSite add ste_developer_id nvarchar2(128);

update acSite set ste_developer_id='MOBILE';

alter table regUser add usr_weixin_id nvarchar2(128);

/*
Auth: walker
Date: 2013-07-24
Desc: LEARNING_ACTIVITY_BY_COS
*/

insert into ReportTemplate
(rte_title_xml,rte_type,rte_get_xsl,rte_exe_xsl,rte_dl_xsl,
 rte_meta_data_url,rte_seq_no,rte_owner_ent_id,rte_create_usr_id,rte_create_timestamp,
 rte_upd_usr_id,rte_upd_timestamp)
select '<title> <desc lan="ISO-8859-1" name="Learning Activity Report By Course"/> <desc lan="Big5" name="學員學習進展報告（數據以課程分組）"/> <desc lan="GB2312" name="学员学习进展报告（数据以课程分组）"/></title>',
 'LEARNING_ACTIVITY_BY_COS', 'rpt_lrn_by_cos_srh.xsl', 'rpt_lrn_by_cos_res.xsl', 'rpt_dl_lrn_by_cos_xls.xsl',
 null, 3, ste_ent_id, 's1u3', sysdate, 
's1u3', sysdate
from acSite

GO
insert into acReportTemplate
(ac_rte_id,ac_rte_ent_id,ac_rte_rol_ext_id,ac_rte_ftn_ext_id,ac_rte_owner_ind,
ac_rte_create_usr_id,ac_rte_create_timestamp)
select rte_id, null, rol_ext_id, 'RTE_READ', 0,
's1u3', sysdate 
from acRole, acHomePage, ReportTemplate
where rol_ste_ent_id <> 0
and rol_ext_id = ac_hom_rol_ext_id
and ac_hom_ftn_ext_id = 'RPT_LINK'
and rol_ste_ent_id = rte_owner_ent_id
and rte_type = 'LEARNING_ACTIVITY_BY_COS';
GO
update ReportTemplate set rte_owner_ent_id = 0 where rte_type = 'LEARNING_ACTIVITY_COS';
GO

create view v_itm_by_cos as
select app_itm_id t_id, nvl(ire_parent_itm_id,itm_id) p_itm_id ,app_ent_id,app_id,att_ats_id,cov_score, cov_total_time,attempts_user,total_attempt
from aeItem
left join aeItemRelation ON (itm_id  = ire_child_itm_id)   
inner join aeApplication on (app_itm_id = itm_id)
INNER JOIN aeAttendance ON (app_id = att_app_id)
inner join CourseEvaluation on ( app_tkh_id = cov_tkh_id )
LEFT JOIN ( 
	SELECT DISTINCT mov_tkh_id, 1 AS  attempts_user, SUM(mov_total_attempt) AS total_attempt FROM ModuleEvaluation 
	WHERE mov_total_attempt IS NOT NULL AND mov_total_attempt>0  Group By mov_tkh_id  
) attempt ON (app_tkh_id= attempt.mov_tkh_id )  

/*
Auth: kevin
Date: 2013-7-26
Desc: 在FriendshipLink(友情链接)表中添加字段fsl_tcr_id 标示该链接所属培训中心
*/
alter table FriendshipLink add fsl_tcr_id int;

/*
Auth: kevin
Date: 2013-7-29
Desc: 培训管理员添加宣传栏功能
*/

alter table SitePoster add sp_tcr_id int default 1,

alter table SitePoster add sp_upd_usr_id nvarchar2(20)
alter table SitePoster add sp_upd_timestamp timestamp; 

ALTER TABLE SitePoster DROP CONSTRAINT FK_SitePoster_acSite

/*
Auth: Elvea
Date: 2013-8-1
Desc: 课程信息保存到数据库中
*/
create table aeItemExtension (
	ies_itm_id int not null,
	ies_lang nvarchar2(4000),
	ies_objective nvarchar2(4000),
	ies_contents nvarchar2(4000),
	ies_duration nvarchar2(4000),
	ies_audience nvarchar2(4000),
	ies_prerequisites nvarchar2(4000),
	ies_exemptions nvarchar2(4000),
	ies_remarks nvarchar2(4000),
	ies_enroll_confirm_remarks nvarchar2(4000),
	ies_schedule nvarchar2(4000),
	ies_itm_ref_materials_1 nvarchar2(500),
	ies_itm_ref_materials_2 nvarchar2(500),
	ies_itm_ref_materials_3 nvarchar2(500),
	ies_itm_ref_materials_4 nvarchar2(500),
	ies_itm_ref_materials_5 nvarchar2(500),
	ies_itm_ref_url_1 nvarchar2(500),
	ies_itm_ref_url_2 nvarchar2(500),
	ies_itm_ref_url_3 nvarchar2(500),
	ies_itm_ref_url_4 nvarchar2(500),
	ies_itm_ref_url_5 nvarchar2(500),
	ies_itm_rel_materials_1 nvarchar2(500),
	ies_itm_rel_materials_2 nvarchar2(500),
	ies_itm_rel_materials_3 nvarchar2(500),
	ies_itm_rel_materials_4 nvarchar2(500),
	ies_itm_rel_materials_5 nvarchar2(500),
	ies_itm_rel_materials_6 nvarchar2(500),
	ies_itm_rel_materials_7 nvarchar2(500),
	ies_itm_rel_materials_8 nvarchar2(500),
	ies_itm_rel_materials_9 nvarchar2(500),
	ies_itm_rel_materials_10 nvarchar2(500),
	ies_top_ind int null,
	ies_top_icon nvarchar2(55) null,
 	constraint FK_aeItemExtension foreign key (ies_itm_id) REFERENCES aeItem (itm_id)
)

create index IX_IES ON aeItemExtension(ies_itm_id);

insert into aeItemExtension (ies_itm_id) 
select itm_id from aeItem

/* 社区化 */
/* 收藏 */
create table sns_collect(
	s_clt_id				int not null,
	s_clt_title			nvarchar2(255),
	s_clt_url				nvarchar2(255),
	s_clt_create_datetime	timestamp,
	s_clt_uid				int,
	s_clt_module			nvarchar2(50),
	s_clt_target_id			int
);

CREATE SEQUENCE sns_collect_seq;

CREATE OR REPLACE TRIGGER sns_collect_trigger BEFORE INSERT ON sns_collect
FOR EACH ROW
WHEN (new.s_clt_id IS NULL)
BEGIN
 SELECT sns_collect_seq.NEXTVAL INTO :new.s_clt_id FROM DUAL;
END;


/* 分享 */
create table sns_share(
	s_sha_id				int not null,
	s_sha_title				nvarchar2(255),
	s_sha_url				nvarchar2(255),
	s_sha_create_datetime	timestamp,
	s_sha_uid				int,
	s_sha_module			nvarchar2(50),
	s_sha_target_id			int
);

CREATE SEQUENCE sns_share_seq;

CREATE OR REPLACE TRIGGER sns_share_trigger BEFORE INSERT ON sns_share
FOR EACH ROW
WHEN (new.s_sha_id IS NULL)
BEGIN
 SELECT sns_share_seq.NEXTVAL INTO :new.s_sha_id FROM DUAL;
END;


/* 评论 */
create table sns_comment(
	s_cmt_id				int primary key,
	s_cmt_uid				int,
	s_cmt_content			nvarchar2(4000),
	s_cmt_is_reply			nvarchar2(3),
	s_cmt_reply_to_id		int,
	s_cmt_create_datetime	timestamp,
	s_cmt_anonymous			int,
	s_cmt_module			nvarchar2(50),
	s_cmt_target_id			int
);

CREATE SEQUENCE sns_comment_seq;

CREATE OR REPLACE TRIGGER sns_comment_trigger BEFORE INSERT ON sns_comment
FOR EACH ROW
WHEN (new.s_cmt_id IS NULL)
BEGIN
 SELECT sns_comment_seq.NEXTVAL INTO :new.s_cmt_id FROM DUAL;
END;


/* 关注 */
create table sns_attention(
	s_att_id				int primary key,
	s_att_source_uid		int,
	s_att_target_uid		int,
	s_att_create_datetime	timestamp
);

CREATE SEQUENCE sns_attention_seq;

CREATE OR REPLACE TRIGGER sns_attention_trigger BEFORE INSERT ON sns_attention
FOR EACH ROW
WHEN (new.s_att_id IS NULL)
BEGIN
 SELECT sns_attention_seq.NEXTVAL INTO :new.s_att_id FROM DUAL;
END;

/* 评价 */
create table sns_valuation_log(
	s_vtl_log_id			int primary key,
	s_vtl_type				nvarchar2(50),
	s_vtl_score				int,
	s_vtl_create_datetime	timestamp,
	s_vtl_uid				int,
	s_vtl_module			nvarchar2(50),
	s_vtl_target_id			int
);

CREATE SEQUENCE sns_valuation_log_seq;

CREATE OR REPLACE TRIGGER sns_valuation_log_trigger BEFORE INSERT ON sns_valuation_log
FOR EACH ROW
WHEN (new.s_vtl_log_id IS NULL)
BEGIN
 SELECT sns_valuation_log_seq.NEXTVAL INTO :new.s_vtl_log_id FROM DUAL;
END;

create table sns_valuation(
	s_vlt_id				int  primary key, 
	s_vlt_type				nvarchar2(50),
	s_vlt_score				int,
	s_vlt_module			nvarchar2(50),
	s_vlt_target_id			int
);

CREATE SEQUENCE sns_valuation_seq;

CREATE OR REPLACE TRIGGER sns_valuation_trigger BEFORE INSERT ON sns_valuation
FOR EACH ROW
WHEN (new.s_vlt_id IS NULL)
BEGIN
 SELECT sns_valuation_seq.NEXTVAL INTO :new.s_vlt_id FROM DUAL;
END;

/* 动态 */
create table sns_doing (
	s_doi_id				int primary key,
	s_doi_act				nvarchar2(50),
	s_doi_title				nvarchar2(4000),
	s_doi_uid				int not null,
	s_doi_create_datetime	timestamp,
	s_doi_url				nvarchar2(255) not null,
	s_doi_module			nvarchar2(50),
	s_doi_target_id			int
);

CREATE SEQUENCE sns_doing_seq;

CREATE OR REPLACE TRIGGER sns_doing_trigger BEFORE INSERT ON sns_doing
FOR EACH ROW
WHEN (new.s_doi_id IS NULL)
BEGIN
 SELECT sns_doing_seq.NEXTVAL INTO :new.s_doi_id FROM DUAL;
END;

/* 隐私设置 */
create table sns_setting (
	s_set_id				int primary key,
	s_set_uid				int,
	s_set_comment			int,
	s_set_group				int,
	s_set_my_follow			int,
	s_set_my_fans			int,
	s_set_share				int,
	s_set_valuation			int,
	s_set_like				int,
	s_set_doing				int,
	s_set_create_uid		int null,
	s_set_create_datetime	timestamp,
	s_set_update_uid		int null,
	s_set_update_datetime	timestamp
);

CREATE SEQUENCE sns_setting_seq;

CREATE OR REPLACE TRIGGER sns_setting_trigger BEFORE INSERT ON sns_setting
FOR EACH ROW
WHEN (new.s_set_id IS NULL)
BEGIN
 SELECT sns_setting_seq.NEXTVAL INTO :new.s_set_id FROM DUAL;
END;


/* 群组 */
create table sns_group (
	s_grp_id				int  primary key,
	s_grp_uid				int,
	s_grp_title				nvarchar2(255),
	s_grp_desc				nvarchar2(255),
	s_grp_private			int,
	s_grp_create_uid		int null,
	s_grp_create_datetime	timestamp,
	s_grp_update_uid		int null,
	s_grp_update_datetime	timestamp
);

CREATE SEQUENCE sns_group_seq;

CREATE OR REPLACE TRIGGER sns_group_trigger BEFORE INSERT ON sns_group
FOR EACH ROW
WHEN (new.s_grp_id IS NULL)
BEGIN
 SELECT sns_group_seq.NEXTVAL INTO :new.s_grp_id FROM DUAL;
END;


create table sns_group_member (
	s_gpm_id				int primary key,
	s_gpm_grp_id			int,
	s_gpm_usr_id			int,
	s_gpm_join_datetime		timestamp,
	s_gpm_status			int,
	s_gpm_type				int
);

CREATE SEQUENCE sns_group_member_seq;

CREATE OR REPLACE TRIGGER sns_group_member_trigger BEFORE INSERT ON sns_group_member
FOR EACH ROW
WHEN (new.s_gpm_id IS NULL)
BEGIN
 SELECT sns_group_member_seq.NEXTVAL INTO :new.s_gpm_id FROM DUAL;
END;

/* 用户视图 */
create or replace view V_user as
select usr_ent_id, usr_display_bil, usr_ste_usr_id, usr_email, usr_tel_1, usr_status,
	usg_ent_id, usg_display_bil, urx_extra_43, usr_gender, follow.cnt as usr_follow, fans.cnt as usr_fans
	,(case when usr_nickname is null then usr_display_bil else usr_nickname end) usr_nickname,urx_extra_44
from reguser
inner join entityrelation on usr_ent_id = ern_child_ent_id
inner join usergroup on ern_ancestor_ent_id = usg_ent_id 
left join reguserExtension rge on rge.urx_usr_ent_id = usr_ent_id
left join (
	select count(s_att_id) cnt, s_att_source_uid from sns_attention group by s_att_source_uid
) follow on (ern_child_ent_id = follow.s_att_source_uid)
left join (
	select count(s_att_id) cnt, s_att_target_uid from sns_attention group by s_att_target_uid
) fans on (ern_child_ent_id = fans.s_att_target_uid)
where  ern_parent_ind = 1 ;


/* 用户关系视图 */
create or replace view V_usrTcRelation as
select ern_child_ent_id as u_id, ern_ancestor_ent_id as usg_id, ern_parent_ind, ern_order, tcr_id, tcr_code, tcr_title
from tctrainingcentertargetentity
inner join entityrelation on(ern_ancestor_ent_id =tce_ent_id)
inner join tcTrainingcenter on( tcr_id =tce_tcr_id) 
where  ern_type = 'USR_PARENT_USG'



/* 课程视图 */
create or replace view V_item as
select itm_id, itm_code, itm_title, itm_icon, itm_fee, itm_type, itm_tcr_id, tnd_title from aeItem left join (
    select tnd_itm_id, cat_title as tnd_title from aeTreeNode,aeCatalog where tnd_cat_id = cat_id and tnd_type = 'ITEM' and cat_id in(select max(tnd_cat_id) from  aeTreeNode where tnd_type = 'ITEM' group by tnd_itm_id)
) node on (itm_id = tnd_itm_id)
where itm_status = 'ON';

/* 课程统计信息视图 */
create or replace view v_item_info as
select itm_id iti_itm_id, nvl(collect_count, 0) iti_collect_count, nvl(share_count, 0) iti_share_count, 
	nvl(star_count, 0) iti_star_count, nvl(like_count, 0) iti_like_count, nvl(cmt_count, 0) iti_cmt_count, 
	nvl(s_vlt_score, 0) iti_score
from v_item left join (
	select s_clt_target_id, count(s_clt_id) collect_count from sns_collect group by s_clt_target_id
) clt on (itm_id = clt.s_clt_target_id) 
left join (
	select s_sha_target_id, count(s_sha_id) share_count from sns_share group by s_sha_target_id
) sha on (itm_id = sha.s_sha_target_id) 
left join (
	select s_vtl_target_id, count(s_vtl_log_id) star_count from sns_valuation_log where s_vtl_type = 'Star' group by s_vtl_target_id
) sta on (itm_id = sta.s_vtl_target_id) 
left join (
	select s_vtl_target_id, count(s_vtl_log_id) like_count from sns_valuation_log where s_vtl_type = 'Like' group by s_vtl_target_id
) lik on (itm_id = lik.s_vtl_target_id) 
left join (
	select s_cmt_target_id, count(s_cmt_id) cmt_count from sns_comment where s_cmt_module = 'Course' group by s_cmt_target_id
) cmt on (itm_id = cmt.s_cmt_target_id) 
left join (
	select s_vlt_target_id, s_vlt_score from sns_valuation where s_vlt_type = 'Star'
) val on (itm_id = val.s_vlt_target_id);




/*
Auth: randy
Date: 2013-8-22
Desc: 屏蔽论坛功能。
*/
delete from achomepage where ac_hom_ftn_ext_id = 'FOR_LINK' or ac_hom_ftn_ext_id = 'FOR_MAIN';

/*
Auth: Walker
Date: 2013-8-6
Desc: 讲师管理添加培训中心属性
*/
alter table InstructorInf add iti_tcr_id int;

/*
Auth: Elvea
Date: 2013-8-6
Desc: 课程指派
*/
insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('COURSE_MAIN', 'FUNCTION', 'HOMEPAGE', sysdate, null); 

insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp)
select rol_id, ftn_id, sysdate from acRole, acFunction
where rol_ext_id like 'ADM_%' and ftn_ext_id = 'COURSE_MAIN';

insert into acHomePage (ac_hom_rol_ext_id,ac_hom_ftn_ext_id, ac_hom_create_usr_id, ac_hom_create_timestamp)
select rol_ext_id, 'COURSE_MAIN', 's1u3', sysdate from acRole where rol_ext_id like 'ADM_%';

alter table aeItem add itm_parent_id int;

/*
Auth: Mike
Date: 2011-10-24
Desc: 积分管理添加 "所属培训中心"
*/
ALTER TABLE creditsType ADD cty_tcr_id int;

/*
Date: 2013-07-25
Desc: 积分清空
*/
 INSERT INTO creditsType 
 (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,
 cty_deleted_ind,cty_relation_total_ind,cty_relation_type,
 cty_default_credits_ind,cty_default_credits,cty_create_usr_id,
 cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,CTY_TCR_ID)
VALUES('INTEGRAL_EMPTY','INTEGRAL_EMPTY',1,1,0,1,'',0,0,'s1u3',sysdate,'s1u3',sysdate,1);




/*
Auth: 
Date: 2013-07-25
Desc: 积分规则可填写小数,把原来的数据类型改为float
*/
ALTER TABLE creditstype modify cty_default_credits float


/* 课程视图 */
create or replace view V_item as
select itm_id, itm_code, itm_title, itm_icon, itm_fee, itm_type, itm_tcr_id, itm_publish_timestamp, itm_appn_start_datetime, itm_appn_end_datetime, tnd_title from aeItem left join (
    select tnd_itm_id, cat_title as tnd_title from aeTreeNode,aeCatalog where tnd_cat_id = cat_id and tnd_type = 'ITEM' and cat_id in(select max(tnd_cat_id) from  aeTreeNode where tnd_type = 'ITEM' group by tnd_itm_id)
) node on (itm_id = tnd_itm_id)
where itm_status = 'ON';

/*
Auth: Elvea
Date: 2013-12-27
Desc: Bug 18179 从LGF迁移，修正清空积分报错的问题
*/
delete from creditsType where cty_id in (
  select cty.cty_id from creditsType cty
  where cty.cty_code = 'INTEGRAL_EMPTY' 
	and cty.cty_id not in (
		select min(tmp.cty_id) from creditsType tmp where tmp.cty_code = 'INTEGRAL_EMPTY' 
	)
)


/*
Auth: leon
Date: 2013-12-30
Desc: 加申请时间,审核时间,审批人
*/
alter table sns_group_member add s_gpm_apply_datetime timestamp;
alter table sns_group_member add s_gpm_check_datetime timestamp;
alter table sns_group_member add s_gpm_check_user  int;


/*
Auth: Elvea
Date: 2014-1-6
Desc: 个人信息隐私设置
*/
alter table sns_setting add s_set_prof_fullname int null;
alter table sns_setting add s_set_prof_bday int null;
alter table sns_setting add s_set_prof_cert_location int null;
alter table sns_setting add s_set_prof_pol_land int null;
alter table sns_setting add s_set_prof_email int null;
alter table sns_setting add s_set_prof_tel int null;
alter table sns_setting add s_set_prof_fax int null;
alter table sns_setting add s_set_prof_company int null;
alter table sns_setting add s_set_prof_nickname int null;
alter table sns_setting add s_set_prof_join_date int null;
alter table sns_setting add s_set_prof_grade int null;
alter table sns_setting add s_set_prof_msn int null;


create or replace view V_user as
select usr_ent_id as u_id, usr_display_bil, usr_ste_usr_id, usr_email, usr_tel_1, usr_bday, 
	usr_extra_4, usr_fax_1, usr_extra_5,
	usr_join_datetime, urx_extra_41, urx_extra_singleoption_21, usr_status,
	usg_ent_id, usg_display_bil, ugr_ent_id, ugr_display_bil,  urx_extra_43, usr_gender, follow.cnt as usr_follow, fans.cnt as usr_fans
	,(case when usr_nickname is null then usr_display_bil else usr_nickname end) usr_nickname,urx_extra_44
from reguser
inner join entityrelation usg on usr_ent_id = usg.ern_child_ent_id
inner join usergroup on usg.ern_ancestor_ent_id = usg_ent_id 
inner join entityrelation ugr on usr_ent_id = ugr.ern_child_ent_id
inner join usergrade on ugr.ern_ancestor_ent_id = ugr_ent_id 
left join reguserExtension rge on rge.urx_usr_ent_id = usr_ent_id
left join (
	select count(s_att_id) cnt, s_att_source_uid from sns_attention group by s_att_source_uid
) follow on (usr_ent_id = follow.s_att_source_uid)
left join (
	select count(s_att_id) cnt, s_att_target_uid from sns_attention group by s_att_target_uid
) fans on (usr_ent_id = fans.s_att_target_uid)
where ugr.ern_parent_ind = 1 
and usg.ern_parent_ind = 1 ;


create or replace view V_usrTcRelation as
select ern_child_ent_id as u_id, ern_ancestor_ent_id as usg_id, ern_parent_ind, ern_order, tcr_id, tcr_code, tcr_title
from tctrainingcentertargetentity
inner join entityrelation on(ern_ancestor_ent_id =tce_ent_id)
inner join tcTrainingcenter on( tcr_id =tce_tcr_id) 
where  ern_type = 'USR_PARENT_USG';

ALTER TABLE sns_doing modify s_doi_url nvarchar2(255) null;		 

/*
Auth: leon
Date: 2014-2-25
Desc: 去掉访问控制里的学习小组模块
*/
delete from acroleFunction where rfn_ftn_id in (
	select ftn_id from acFunction where ftn_ext_id = 'STUDY_GROUP_MAIN'
);
commit;

/*--------------------------------------------------------------------------------------------------------------------*/
/*
Auth: walker
Date: 2013-1-29
Desc: 添加视频课程
*/

/* VIDEO */
insert into aeItemType
select ity_owner_ent_id, 'VIDEO', ity_run_ind, 17, '', null, sysdate, 'slu3', null, ity_create_run_ind,
	ity_apply_ind, ity_qdb_ind, 0, null, ity_certificate_ind, 0, 0, ity_has_attendance_ind, ity_ji_ind, 
	ity_completion_criteria_ind, 0, ity_target_method, 0, ity_tkh_method, 0, 0, 0, 0
from aeItemType 
where ity_id='SELFSTUDY' and ity_exam_ind = 0;
commit;
/* */
insert into aeTemplate(tpl_ttp_id, tpl_title, tpl_xml, tpl_owner_ent_id, tpl_create_timestamp, tpl_create_usr_id, tpl_upd_timestamp, tpl_upd_usr_id, tpl_approval_ind)
select tpl_ttp_id, 'VIDEO Item Template', 'xml', tpl_owner_ent_id, sysdate, 's1u3', sysdate, 's1u3', tpl_approval_ind 
from aeTemplate 
where tpl_title = 'Self Study Item Template';
commit;
insert into aeTemplate(tpl_ttp_id, tpl_title, tpl_xml, tpl_owner_ent_id, tpl_create_timestamp, tpl_create_usr_id, tpl_upd_timestamp, tpl_upd_usr_id, tpl_approval_ind)
select tpl_ttp_id, 'VIDEO Enrollment Form Template', 'xml', tpl_owner_ent_id, sysdate, 's1u3', sysdate, 's1u3', tpl_approval_ind 
from aeTemplate 
where tpl_title = 'Self Study Enrollment Form Template';
commit;
/**/
insert into aeTemplateView 
select a.tpl_id, tvw_id, 'xml', tvw_cat_ind, tvw_target_ind, tvw_cm_ind, tvw_mote_ind, tvw_itm_acc_ind, tvw_res_ind, tvw_rsv_ind, sysdate, 's1u3', tvw_wrk_tpl_ind, tvw_cost_center_ind, tvw_ctb_ind, tvw_filesize_ind, tvw_km_domain_ind, tvw_km_published_version_ind, tvw_tcr_ind
from aeTemplate a, aeTemplateView, aeTemplate b
where tvw_tpl_id = b.tpl_id 
and a.tpl_title = 'VIDEO Item Template' 
and b.tpl_title='Self Study Item Template' 
and a.tpl_owner_ent_id = b.tpl_owner_ent_id;
commit;
insert into aeTemplateView 
select a.tpl_id, tvw_id, 'xml', tvw_cat_ind, tvw_target_ind, tvw_cm_ind, tvw_mote_ind, tvw_itm_acc_ind, tvw_res_ind, tvw_rsv_ind, sysdate, 's1u3', tvw_wrk_tpl_ind, tvw_cost_center_ind, tvw_ctb_ind, tvw_filesize_ind, tvw_km_domain_ind, tvw_km_published_version_ind, tvw_tcr_ind
from aeTemplate a, aeTemplateView, aeTemplate b
where tvw_tpl_id = b.tpl_id 
and a.tpl_title = 'VIDEO Enrollment Form Template' 
and b.tpl_title = 'Self Study Enrollment Form Template' 
and a.tpl_owner_ent_id = b.tpl_owner_ent_id;
commit;

/**/
insert into aeItemTypeTemplate 
select itt_ity_owner_ent_id, 'VIDEO', itt_ttp_id, itt_seq_id, tpl_id, itt_run_tpl_ind, sysdate, itt_create_usr_id ,itt_session_tpl_ind
from aeItemTypeTemplate, aeTemplate
where itt_ity_id = 'SELFSTUDY'
and tpl_title = 'Multi-level-approval Workflow Template'
and tpl_id = itt_tpl_id
and itt_ity_owner_ent_id = tpl_owner_ent_id;
commit;
insert into aeItemTypeTemplate 
select itt_ity_owner_ent_id, 'VIDEO', itt_ttp_id, itt_seq_id, a.tpl_id, itt_run_tpl_ind, sysdate, itt_create_usr_id ,itt_session_tpl_ind
from aeItemTypeTemplate, aeTemplate a, aeTemplate b
where itt_ity_id = 'SELFSTUDY'
and itt_tpl_id = b.tpl_id
and b.tpl_title = 'Self Study Item Template'
and a.tpl_title = 'VIDEO Item Template'
and a.tpl_owner_ent_id = b.tpl_owner_ent_id;
commit;
insert into aeItemTypeTemplate 
select itt_ity_owner_ent_id, 'VIDEO', itt_ttp_id, itt_seq_id, a.tpl_id, itt_run_tpl_ind, sysdate, itt_create_usr_id ,itt_session_tpl_ind
from aeItemTypeTemplate, aeTemplate a, aeTemplate b
where itt_ity_id = 'SELFSTUDY'
and itt_tpl_id = b.tpl_id
and b.tpl_title = 'Self Study Enrollment Form Template'
and a.tpl_title = 'VIDEO Enrollment Form Template'
and a.tpl_owner_ent_id = b.tpl_owner_ent_id;
commit;

update aeItemType set ity_seq_id = 4 where ity_id = 'VIDEO';
update aeItemType set ity_seq_id = ity_seq_id + 1 where ity_seq_id >=4 and ity_id <> 'VIDEO';
commit;



/*
Auth: waler
Date: 2011-12-08
Desc: 视频点播模块添加视频时长,视频截图
*/
alter table resources add res_vod_duration int;
alter table resources add res_img_link nvarchar2(200);

/*
Auth: waler
Date: 2011-12-21
Desc: 增加记录视频学习跟踪记录表
*/
CREATE TABLE vodLearnRecord
(
vlr_id int  primary key,
vlr_tkh_id int NOT NULL,
vlr_chapter_id nvarchar2(50) NOT NULL,
vlr_node_id nvarchar2(50) NOT NULL,
vlr_node_vod_res_id int,
vlr_create_usr_id nvarchar2(20) NOT NULL,
vlr_create_timestamp timestamp NOT NULL,
vlr_update_usr_id nvarchar2(20) NOT NULL,
vlr_update_timestamp timestamp NOT NULL
);
CREATE SEQUENCE vodLearnRecord_seq;

CREATE OR REPLACE TRIGGER vodLearnRecord_trigger BEFORE INSERT ON vodLearnRecord
FOR EACH ROW
WHEN (new.vlr_id IS NULL)
BEGIN
 SELECT vodLearnRecord_seq.NEXTVAL INTO :new.vlr_id FROM DUAL;
END;
/*
Auth: waler
Date: 20123-22
Desc: 视频课程要点
*/
alter table resources add res_vod_main NCLOB;

/*
Auth: leon
Date: 2014-2-28
Desc: 去掉访问控制里的无效数据
*/
delete from acroleFunction where rfn_ftn_id in (
	select ftn_id from acFunction where ftn_ext_id = 'COURSE_MAIN'
);
delete from acroleFunction where rfn_ftn_id in (
	select ftn_id from acFunction where ftn_ext_id = 'EIP_MAIN'
);
delete from acFunction where ftn_ext_id = 'COURSE_MAIN';
delete from acFunction where ftn_ext_id = 'EIP_MAIN';
commit;

/*
Auth: Randy
Date: 2014-02-27
*/
alter table COURSEMODULECRITERIA modify(CMR_STATUS_DESC_OPTION nvarchar2(100));


/*
Auth: leon
Date: 2014-3-3
Desc: 去掉添加角色中的学习小组
*/
delete from acFunction where ftn_ext_id = 'STUDY_GROUP_MAIN';
commit;

/*
Auth: leon
Date: 2014-3-6
Desc: 课程指派  企业管理
*/
insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('COURSE_MAIN', 'FUNCTION', 'HOMEPAGE', sysdate, null); 
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp)
select rol_id, ftn_id,sysdate from acRole, acFunction
where rol_ext_id like 'ADM_%' and ftn_ext_id = 'COURSE_MAIN';

insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('EIP_MAIN', 'FUNCTION', 'HOMEPAGE', sysdate, null); 
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp)
select rol_id, ftn_id,sysdate from acRole, acFunction
where rol_ext_id like 'ADM_%' and ftn_ext_id = 'EIP_MAIN';
commit;

/*
Auth: leon
Date: 2014-3-10
Desc: 删除课程指派
*/
delete from acroleFunction where rfn_ftn_id in (
	select ftn_id from acFunction where ftn_ext_id = 'COURSE_MAIN'
);
delete from acFunction where ftn_ext_id = 'COURSE_MAIN';
commit;


/*
Auth: randy
Date: 2014-2-2
Desc: 删除论坛相关积分项
*/
delete from userCreditsDetailLog where ucl_bpt_id in(select cty_id from creditsType where cty_code in('SYS_INS_TOPIC','SYS_MSG_UPLOAD_RES','SYS_INS_MSG','ITM_INS_TOPIC','ITM_INS_MSG','ITM_MSG_UPLOAD_RES'));

delete from userCreditsDetail where ucd_cty_id in(select cty_id from creditsType where cty_code in('SYS_INS_TOPIC','SYS_MSG_UPLOAD_RES','SYS_INS_MSG','ITM_INS_TOPIC','ITM_INS_MSG','ITM_MSG_UPLOAD_RES'));

delete from creditsType  where cty_code in('SYS_INS_TOPIC','SYS_INS_MSG','SYS_MSG_UPLOAD_RES','ITM_INS_TOPIC','ITM_INS_MSG','ITM_MSG_UPLOAD_RES');
commit;
/*
Auth: walker
Date: 2014-4-2
Desc: 学员进展报告修改
*/
create view view_lrn_activity_group as
select app_itm_id lar_c_itm_id, nvl(ire_parent_itm_id,itm_id) lar_p_itm_id ,app_ent_id lar_usr_ent_id,app_id lar_app_id,app_tkh_id lar_tkh_id,
att_ats_id lar_att_ats_id,cov_score lar_cov_score, cov_total_time lar_cov_total_time,attempts_user lar_attempts_user,total_attempt lar_total_attempt,
app_create_timestamp lar_app_create_timestamp, app_status lar_app_status, app_process_status lar_app_process_status,
att_timestamp lar_att_timestamp,att_create_timestamp lar_att_create_timestamp, att_remark lar_att_remark,att_rate lar_att_rate,
cov_cos_id lar_cov_cos_id,cov_commence_datetime	lar_cov_commence_datetime, cov_last_acc_datetime lar_cov_last_acc_datetime
from aeItem
left join aeItemRelation on (itm_id  = ire_child_itm_id)   
inner join aeApplication on (app_itm_id = itm_id)
inner join RegUser on (usr_ent_id = app_ent_id and usr_status = 'OK')
inner join aeAttendance on (app_id = att_app_id)
inner join CourseEvaluation on ( app_tkh_id = cov_tkh_id )
left join ( 
	select DISTINCT mov_tkh_id, 1 AS  attempts_user, SUM(mov_total_attempt) AS total_attempt from ModuleEvaluation 
	where mov_total_attempt IS NOT NULL and mov_total_attempt > 0  Group By mov_tkh_id  
) attempt on (app_tkh_id = attempt.mov_tkh_id);


/*
Auth: walker
Date: 2014-3-31
Desc: 添加表记录学习记录(通过线程把上面视图数据写入)
*/

CREATE TABLE lrnActivityReport
(
	lar_id int primary key,
	lar_c_itm_id int,
	lar_p_itm_id int,
	lar_usr_ent_id int,
	lar_app_id int,
	lar_tkh_id int,
	lar_att_ats_id int,
	lar_cov_score decimal(18,4),
	lar_cov_total_time float,
	lar_attempts_user int,
	lar_total_attempt int,
	lar_app_create_timestamp timestamp, 
	lar_app_status nvarchar2(20), 
	lar_app_process_status nvarchar2(50), 
	lar_att_timestamp timestamp,
	lar_att_create_timestamp timestamp, 
	lar_att_remark nvarchar2(2000), 
	lar_att_rate decimal(18,4),
	lar_cov_cos_id int,
	lar_cov_commence_datetime timestamp, 
	lar_cov_last_acc_datetime timestamp
);

CREATE SEQUENCE lrnActivityReport_seq;
CREATE OR REPLACE TRIGGER lrnActivityReport_trigger BEFORE INSERT ON lrnActivityReport
FOR EACH ROW
WHEN (new.lar_id IS NULL)
BEGIN
 SELECT lrnActivityReport_seq.NEXTVAL INTO :new.lar_id FROM DUAL;
END;
CREATE  INDEX IX_lrnActivityReport ON lrnActivityReport(lar_c_itm_id,lar_p_itm_id,lar_usr_ent_id,lar_app_id,lar_tkh_id);

/*
Auth: Barry
Date: 2014-1-27
Desc: 公告增加缩略图字段
*/
ALTER TABLE message ADD msg_icon nvarchar2(50);

/* 
Auth: Kenry
Date: 2014-1-27
Desc: Mobile2.0API 移动宣传栏添加字段,0 pc端 1 移动端
*/
alter table SITEPOSTER add SP_MOBILE_IND INTEGER default 0 not null;
alter table SITEPOSTER drop constraint PK_SITEPOSTER;
alter table SITEPOSTER add constraints PK_SITEPOSTER primary key (SP_STE_ID);

insert into SitePoster (SP_STE_ID, SP_MEDIA_FILE, SP_URL, SP_STATUS, SP_MEDIA_FILE1, SP_URL1, SP_STATUS1, SP_MEDIA_FILE2, SP_URL2, SP_STATUS2, SP_MEDIA_FILE3, SP_URL3, SP_STATUS3, SP_MEDIA_FILE4, SP_URL4, SP_STATUS4,SP_MOBILE_IND)
values (2, '', '', 'OFF', '', '', 'OFF', '', '', 'OFF', '', '', 'OFF', '', '', 'OFF',1);
commit;
/*
Auth: Kenry
Date: 2014-1-27
Desc: Mobile2.0API 调查问卷添加字段,0 pc端 1 移动端
*/
alter table MODULETRAININGCENTER add MTC_MOBILE_IND INTEGER default 0 not null;

/*
Auth: Barry
Date: 2014-1-27
Desc: 课程学习内容的视频点播增加必修时长字段，保存整数的分钟数，作为视频点播学习完成的标准
*/
ALTER TABLE module ADD mod_required_time INT NULL;

/*
Auth: Barry
Date: 2014-02-07
Desc: 培训目录表增加“是否是移动课程目录”字段
*/
ALTER TABLE aeCatalog ADD cat_mobile_ind INT DEFAULT (0);

/*
Auth: Kenry
Date: 2014-02-24
Desc: 公告添加是否移动端字段 0：pc端 1：移动端
*/
ALTER TABLE Message ADD msg_mobile_ind INT DEFAULT (0);

/*
Auth: Lance
Date: 2014-03-06
Desc: 课程学习内容的视频点播增加必修允许下载字段，限制移动课程中的“视频点播”是否允许下载, 0 为禁止  1 为允许
*/
ALTER TABLE module ADD mod_download_ind INTEGER default 0 not null;



/*
Auth: Harvey
Date: 2012-4-12
Desc: add new itm_type 'MOBILE'(从南航迁移移动课程类型)
*/

update aeItemType set ity_seq_id=ity_seq_id+1 where ity_seq_id>3;

insert into aeItemType 
Select Ity_Owner_Ent_Id,'MOBILE',Ity_Run_Ind,4,'XML',Ity_Icon_Url,sysdate,Ity_Create_Usr_Id,Ity_Init_Life_Status,Ity_Create_Run_Ind,Ity_Apply_Ind,Ity_Qdb_Ind,Ity_Auto_Enrol_Qdb_Ind,Ity_Cascade_Inherit_Col,Ity_Certificate_Ind,Ity_Session_Ind,Ity_Create_Session_Ind,Ity_Has_Attendance_Ind,Ity_Ji_Ind,Ity_Completion_Criteria_Ind,Ity_Can_Cancel_Ind,Ity_Target_Method,Ity_Reminder_Criteria_Ind,Ity_Tkh_Method,ity_exam_ind,ity_blend_ind,ity_ref_ind,ity_integ_ind From Aeitemtype
where Ity_Id = 'SELFSTUDY' and ity_exam_ind=0;

update aeItemType set ity_seq_id=ity_seq_id+1 where ity_seq_id>7;

insert into aeItemType 
Select Ity_Owner_Ent_Id,'MOBILE',Ity_Run_Ind,8,'XML',Ity_Icon_Url,sysdate,Ity_Create_Usr_Id,Ity_Init_Life_Status,Ity_Create_Run_Ind,Ity_Apply_Ind,Ity_Qdb_Ind,Ity_Auto_Enrol_Qdb_Ind,Ity_Cascade_Inherit_Col,Ity_Certificate_Ind,Ity_Session_Ind,Ity_Create_Session_Ind,Ity_Has_Attendance_Ind,Ity_Ji_Ind,Ity_Completion_Criteria_Ind,Ity_Can_Cancel_Ind,Ity_Target_Method,Ity_Reminder_Criteria_Ind,Ity_Tkh_Method,ity_exam_ind,ity_blend_ind,ity_ref_ind,ity_integ_ind From Aeitemtype
where Ity_Id = 'SELFSTUDY' and ity_exam_ind=1;
select * from aeItemType  order by ity_seq_id asc;

insert into aeTemplate(tpl_ttp_id,tpl_title,tpl_xml,tpl_owner_ent_id,tpl_create_timestamp,tpl_create_usr_id,tpl_upd_timestamp,tpl_upd_usr_id,tpl_approval_ind)
	select tpl_ttp_id, 'Mobile Learning Item Template', 'XML', tpl_owner_ent_id, sysdate, tpl_create_usr_id, sysdate,tpl_upd_usr_id, tpl_approval_ind 
	from aeTemplate 
	where tpl_title = 'Self Study Item Template';

insert into aeTemplate(tpl_ttp_id,tpl_title,tpl_xml,tpl_owner_ent_id,tpl_create_timestamp,tpl_create_usr_id,tpl_upd_timestamp,tpl_upd_usr_id,tpl_approval_ind)
	select tpl_ttp_id, 'Mobile Learning Enrollment Form Template', 'XML', tpl_owner_ent_id,sysdate, tpl_create_usr_id, sysdate, tpl_upd_usr_id, tpl_approval_ind 
	from aeTemplate 
	where tpl_title = 'Self Study Enrollment Form Template';

insert into aeTemplateView 
	select a.tpl_id, tvw_id, 'XML', tvw_cat_ind, tvw_target_ind, tvw_cm_ind, tvw_mote_ind, tvw_itm_acc_ind, tvw_res_ind, tvw_rsv_ind, sysdate, 's1u3', tvw_wrk_tpl_ind, tvw_cost_center_ind, tvw_ctb_ind, tvw_filesize_ind, tvw_km_domain_ind, tvw_km_published_version_ind, tvw_tcr_ind
	from aeTemplate a, aeTemplateView, aeTemplate b
	where tvw_tpl_id = b.tpl_id 
	and tvw_id <> 'CANCELLED_VIEW' 
	and a.tpl_title = 'Mobile Learning Item Template' 
	and b.tpl_title='Self Study Item Template'-- and b.tpl_exam
	and a.tpl_owner_ent_id = b.tpl_owner_ent_id;

insert into aeTemplateView 
	select a.tpl_id, tvw_id, 'XML', tvw_cat_ind, tvw_target_ind, tvw_cm_ind, tvw_mote_ind, tvw_itm_acc_ind, tvw_res_ind, tvw_rsv_ind, sysdate, 's1u3', tvw_wrk_tpl_ind, tvw_cost_center_ind, tvw_ctb_ind, tvw_filesize_ind, tvw_km_domain_ind, tvw_km_published_version_ind, tvw_tcr_ind
	from aeTemplate a, aeTemplateView, aeTemplate b
	where tvw_tpl_id = b.tpl_id 
	and a.tpl_title = 'Mobile Learning Enrollment Form Template' 
	and b.tpl_title = 'Self Study Enrollment Form Template' 
	and a.tpl_owner_ent_id = b.tpl_owner_ent_id;

insert into aeItemTypeTemplate 
	select itt_ity_owner_ent_id, 'MOBILE', itt_ttp_id, itt_seq_id, tpl_id, itt_run_tpl_ind, sysdate, itt_create_usr_id ,itt_session_tpl_ind
	from aeItemTypeTemplate, aeTemplate
	where itt_ity_id = 'SELFSTUDY'
	and tpl_title = 'Multi-level-approval Workflow Template'
	and tpl_id = itt_tpl_id
	and itt_ity_owner_ent_id = tpl_owner_ent_id;

insert into aeItemTypeTemplate 
	select itt_ity_owner_ent_id, 'MOBILE', itt_ttp_id, itt_seq_id, a.tpl_id, itt_run_tpl_ind, sysdate, itt_create_usr_id ,itt_session_tpl_ind
	from aeItemTypeTemplate, aeTemplate a, aeTemplate b
	where itt_ity_id = 'SELFSTUDY'
	and itt_tpl_id = b.tpl_id
	and b.tpl_title = 'Self Study Item Template'
	and a.tpl_title = 'Mobile Learning Item Template'
	and a.tpl_owner_ent_id = b.tpl_owner_ent_id;

insert into aeItemTypeTemplate 
	select itt_ity_owner_ent_id, 'MOBILE', itt_ttp_id, itt_seq_id, a.tpl_id, itt_run_tpl_ind, sysdate, itt_create_usr_id ,itt_session_tpl_ind
	from aeItemTypeTemplate, aeTemplate a, aeTemplate b
	where itt_ity_id = 'SELFSTUDY'
	and itt_tpl_id = b.tpl_id
	and b.tpl_title = 'Self Study Enrollment Form Template'
	and a.tpl_title = 'Mobile Learning Enrollment Form Template'
	and a.tpl_owner_ent_id = b.tpl_owner_ent_id;


insert into DisplayOption values (0,'MOD','MBL','LRN_READ',1,1,0,1,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0);
insert into DisplayOption values (0,'MOD','MBL','IST_READ',1,1,0,1,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,1,1,0,0);
insert into DisplayOption values (0,'MOD','MBL','IST_EDIT',1,1,0,1,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,1,1,0,0);
commit;
/*
Auth: Jacky 
Date: 2013-12-11
Desc: 修改课程评论
*/
create table aeItemComments_temp
(
	ict_itm_id int not null,
	ict_ent_id int not null,
	ict_tkh_id int not null,
	ict_score int not null,
	ict_comment nvarchar2(510),
	ict_create_timestamp timestamp not null
);
insert into  aeItemComments_temp(ict_itm_id,ict_ent_id,ict_tkh_id,ict_score,ict_comment,ict_create_timestamp)
select ict_itm_id,ict_ent_id,ict_tkh_id,ict_score,ict_comment,ict_create_timestamp from aeItemComments;
drop table aeItemComments;
commit;
create table aeItemComments
(       ict_id int primary key,
	ict_itm_id int not null,
	ict_ent_id int not null,
	ict_tkh_id int not null,
	ict_score int not null,
	ict_comment nvarchar2(510),
	ict_create_timestamp timestamp not null
);
CREATE SEQUENCE aeItemComments_seq;
CREATE OR REPLACE TRIGGER aeItemComments_trigger BEFORE INSERT ON aeItemComments
FOR EACH ROW
WHEN (new.ict_id IS NULL)
BEGIN
 SELECT aeItemComments_seq.NEXTVAL INTO :new.ict_id FROM DUAL;
END;

insert into  aeItemComments(ict_itm_id,ict_ent_id,ict_tkh_id,ict_score,ict_comment,ict_create_timestamp)
select ict_itm_id,ict_ent_id,ict_tkh_id,ict_score,ict_comment,ict_create_timestamp from aeItemComments_temp;
commit;



/**
 * weixin 用户表
 */
CREATE TABLE weixin_user(
	id nvarchar2(32) primary key,
	weixin_open_id nvarchar2(255) NULL,
	fwh_code nvarchar2(32) NULL,
	wizbank_token nvarchar2(32) NULL,
	wizbank_user_id nvarchar2(32) NULL,
	wizbank_user_account nvarchar2(32) NULL,
	wizbank_user_name nvarchar2(32) NULL,
	create_time timestamp NULL,
	modify_time timestamp NULL,
	state nvarchar2(1) NULL
);
ALTER TABLE weixin_user modify state nvarchar2(1) DEFAULT 's' NOT NULL;


/*
Auth: Walker 
Date: 2014-4-10
Desc: label英文大小写修改
*/
update fmFacilityType set ftp_title_xml = N'<title><desc lan="ISO-8859-1" name="Training room"/><desc lan="Big5" name="培訓教室"/><desc lan="GB2312" name="培训教室"/></title>'
where ftp_id = 3;
update fmFacilityType set ftp_title_xml = N'<title><desc lan="ISO-8859-1" name="Breakout room"/><desc lan="Big5" name="備用教室"/><desc lan="GB2312" name="备用教室"/></title>'
where ftp_id = 4;
update fmFacilityType set ftp_title_xml = N'<title><desc lan="ISO-8859-1" name="External venue"/><desc lan="Big5" name="外部地點"/><desc lan="GB2312" name="外部地点"/></title>'
where ftp_id = 9;
commit;

/* r45436 */

/*
Auth: Randy 
Date: 2014-5-6
Desc: 删除论坛管理功能
*/
delete from acrolefunction where rfn_ftn_id in( select ftn_id from acfunction where ftn_ext_id ='FOR_MAIN');
delete from achomepage where ac_hom_ftn_ext_id = 'FOR_MAIN';
delete from acfunction where ftn_ext_id ='FOR_MAIN';
commit;

/*
Auth: randy
Date: 2014-05-13
Desc: 
*/
CREATE TABLE scoRecord(
  srd_id int primary key,
  srd_tkh_id int NOT NULL,
  srd_mod_id int NOT NULL,
  srd_itemId nvarchar2(255) NOT NULL,
  srd_courseStyle nvarchar2(20) NOT NULL,
  srd_courseStatus nvarchar2(20) NULL,
  srd_courseCompletion nvarchar2(20) NULL,
  srd_courseCredit nvarchar2(20) NULL,
  srd_courseLaunchData nvarchar2(2048) NULL,
  srd_courseCount int NULL,
  srd_courseLastDate timestamp NULL,
  srd_courseTimeLength int NULL,
  srd_courseTimeLimit int NULL,
  srd_courseRawScore float NULL,
  srd_courseMaxScore float NULL,
  srd_courseMinScore float NULL,
  srd_coursePassScore float NULL,
  srd_courseLocation NCLOB NULL ,
  srd_courseSusData NCLOB NULL ,
  srd_courseInteractions NCLOB NULL ,
  srd_courseObjective NCLOB NULL ,
  srd_courseData1 NCLOB NULL 
);

CREATE SEQUENCE scoRecord_seq;
CREATE OR REPLACE TRIGGER scoRecord_trigger BEFORE INSERT ON scoRecord
FOR EACH ROW
WHEN (new.srd_id IS NULL)
BEGIN
 SELECT scoRecord_seq.NEXTVAL INTO :new.srd_id FROM DUAL;
END;

CREATE  INDEX IX_scoRecord_tkh_res ON scoRecord(srd_mod_id,srd_tkh_id);  
commit;

alter table resources add res_scor_identifier nvarchar2(255);
alter table resources add res_first_res_id int;
CREATE  INDEX IX_resources_first_res_id ON resources(res_first_res_id);
commit;

/* r46075 */

/*
Auth: randy
Date: 2014-05-13
Desc: 这个版本不能移到LNOW
*/
delete from acroleFunction where rfn_ftn_id in (
	select ftn_id from acFunction where ftn_ext_id = 'EIP_MAIN'
);
delete from acFunction where ftn_ext_id = 'EIP_MAIN';
commit;	   

/*
Auth: randy
Date: 2014-05-13
Desc: 在个SQL在MSSQL中不用再执行
*/
delete from SitePoster where sp_ste_id = 2 and sp_tcr_id = 1;
alter table SITEPOSTER drop constraint PK_SITEPOSTER;
drop  index PK_SITEPOSTER;
commit;

alter table SITEPOSTER add constraints PK_SITEPOSTER primary key (SP_STE_ID,SP_MOBILE_IND);
commit;

insert into SitePoster (SP_STE_ID, SP_MEDIA_FILE, SP_URL, SP_STATUS, SP_MEDIA_FILE1, SP_URL1, SP_STATUS1, SP_MEDIA_FILE2, SP_URL2, SP_STATUS2, SP_MEDIA_FILE3, SP_URL3, SP_STATUS3, SP_MEDIA_FILE4, SP_URL4, SP_STATUS4,SP_MOBILE_IND)
values (1, '', '', 'OFF', '', '', 'OFF', '', '', 'OFF', '', '', 'OFF', '', '', 'OFF',1);
commit;
/* r46105 */

/* end ************************************************************************************************************* */


/* r49228 start */

/*
Auth: randy
Date: 2014-08-06
Desc: 课程学习内容是否发布到移动端
*/
ALTER TABLE module ADD mod_mobile_ind INTEGER default 0 not null;


/*
Auth: randy
Date: 2014-08-06
Desc: 
*/
/*合并移动课程（考试）到网上课程（考试）*/
update aeItemtemplate set itp_tpl_id = (select tpl_id  from aetemplate where tpl_title ='Self Study Item Template')
where itp_ttp_id = 2 and  itp_itm_id in(select itm_id from aeItem where itm_type = 'MOBILE' );

update aeItemtemplate set itp_tpl_id = 
(select tpl_id  from aetemplate where tpl_title ='Self Study Enrollment Form Template')
where itp_ttp_id = 3 and  itp_itm_id in(select itm_id from aeItem where itm_type = 'MOBILE' );

update module set mod_mobile_ind= 1 where mod_res_id in
(select rcn_res_id_content from resourcecontent,course,aeItem where itm_id = cos_itm_id and rcn_res_id= cos_res_id and itm_type = 'MOBILE'  )

update aeItem set itm_type = 'SELFSTUDY' where itm_type = 'MOBILE' ;

delete from aeItemTypeTemplate where itt_ity_id = 'MOBILE';
delete from aeTemplateView where tvw_tpl_id 
  in(select tpl_id from aeTemplate where tpl_title = 'Mobile Learning Item Template' or tpl_title ='Mobile Learning Enrollment Form Template');
delete  from aeTemplate where tpl_title = 'Mobile Learning Item Template' or tpl_title ='Mobile Learning Enrollment Form Template';
delete from aeItemType where ity_id = 'MOBILE';

commit;


/*把离线课程改为混合式课程, 并删除离线课程这种类型*/
update aeItem set itm_blend_ind = 1 where itm_type = 'CLASSROOM' and itm_blend_ind = 0;
delete from aeItemType where ity_id = 'CLASSROOM' and ity_blend_ind = 0;
commit;

/*删除参考*/

delete from aeTreenoderelation where tnr_child_tnd_id in(select tnd_id from aeTreenode,aeItem where tnd_itm_id = itm_id and itm_type in('BOOK','WEBSITE','AUDIOVIDEO') and tnd_type ='ITEM');
delete from aeTreenode where tnd_type ='ITEM' and tnd_itm_id in(select itm_id from aeItem where itm_type in('BOOK','WEBSITE','AUDIOVIDEO'));
delete from aeItemAccess where iac_itm_id in (select itm_id from aeItem where itm_type in('BOOK','WEBSITE','AUDIOVIDEO'));
delete from aeItemExtension where ies_itm_id in (select itm_id from aeItem where itm_type in('BOOK','WEBSITE','AUDIOVIDEO'));
delete from aeItemTemplate where itp_itm_id in(select itm_id from aeItem where itm_type in('BOOK','WEBSITE','AUDIOVIDEO'));
delete from aeLearningSoln where lsn_itm_id in (select itm_id from aeItem where itm_type in('BOOK','WEBSITE','AUDIOVIDEO'));
delete from course where cos_itm_id in(select itm_id from aeItem where itm_type in('BOOK','WEBSITE','AUDIOVIDEO'));
delete from aeItem where itm_id in (select itm_id from aeItem where itm_type in('BOOK','WEBSITE','AUDIOVIDEO'));

delete from aeItemTypeTemplate where itt_ity_id in('BOOK','WEBSITE');
delete from aeTemplateView where tvw_tpl_id 
  in(select tpl_id from aeTemplate where tpl_title = 'Book Item Template' or tpl_title ='Website Item Template');
delete  from aeTemplate where tpl_title = 'Book Item Template' or tpl_title ='Website Item Template';
delete from  aeItemtype where ity_id in('BOOK','WEBSITE') ;
/**/
update aeItemtype set ity_qdb_ind = 1 where ity_id = 'AUDIOVIDEO' ;
commit;

/*班级管理功能*/
delete from acRoleFunction where rfn_ftn_id in(select ftn_id  from acfunction  where ftn_ext_id in('RUN_MAIN'));
delete from acfunction  where ftn_ext_id in('RUN_MAIN');
delete from achomepage where ac_hom_ftn_ext_id  in('RUN_MAIN');
commit;


/*删除视屏课程，并把原来有的视屏课程转化为网上课程
 */

update aeItemtemplate set itp_tpl_id = (select tpl_id  from aetemplate where tpl_title ='VIDEO Item Template')
where itp_ttp_id = 2 and  itp_itm_id in(select itm_id from aeItem where itm_type = 'VIDEO' );

update aeItemtemplate set itp_tpl_id = 
(select tpl_id  from aetemplate where tpl_title ='VIDEO Enrollment Form Template')
where itp_ttp_id = 3 and  itp_itm_id in(select itm_id from aeItem where itm_type = 'VIDEO' );
update aeItem set itm_type = 'SELFSTUDY' where itm_type = 'VIDEO' ;

delete from aeItemTypeTemplate where itt_ity_id = 'VIDEO';
delete from aeTemplateView where tvw_tpl_id 
  in(select tpl_id from aeTemplate where tpl_title = 'VIDEO Item Template' or tpl_title ='VIDEO Enrollment Form Template');
delete  from aeTemplate where tpl_title = 'VIDEO Item Template' or tpl_title ='VIDEO Enrollment Form Template';
delete from aeItemType where ity_id = 'VIDEO';
commit;

/*
Auth: Mike
Date: 2014-07-14
Desc: 职业发展学习任务管理
*/
insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('PROFESSION_MAIN', 'FUNCTION', 'HOMEPAGE', sysdate, null);

insert into acRoleFunction (rfn_rol_id, rfn_ftn_id)
select rol_id, ftn_id from acRole, acFunction
where rol_ext_id like 'ADM_%' and ftn_ext_id = 'PROFESSION_MAIN';

insert into acHomePage (ac_hom_rol_ext_id,ac_hom_ftn_ext_id, ac_hom_create_usr_id, ac_hom_create_timestamp)
select rol_ext_id, 'PROFESSION_MAIN', 's1u3', sysdate from acRole where rol_ext_id like 'ADM_%';


--发展序列表
CREATE TABLE Profession(
  pfs_id int NOT NULL,
  pfs_title nvarchar2(50) NOT NULL,
  pfs_create_time timestamp NULL,
  pfs_create_usr_id int NULL,
  pfs_update_time timestamp NULL,
  pfs_update_usr_id int NULL)
  
ALTER TABLE Profession ADD CONSTRAINT
	PK_Profession PRIMARY KEY 
	(
	pfs_id
	);
commit;
	
create sequence Profession_seq;
CREATE OR REPLACE TRIGGER Profession_trigger BEFORE INSERT ON Profession
FOR EACH ROW
WHEN (new.pfs_id IS NULL)
BEGIN
 SELECT Profession_seq.NEXTVAL INTO :new.pfs_id FROM DUAL;
END;
  

--发展序列关联课程表
CREATE TABLE ProfessionItem(
  psi_id int NOT NULL,
  psi_pfs_id int NULL,
  psi_ugr_id nvarchar2(1000) NULL,
  psi_itm nvarchar2(1000) NULL)
create sequence ProfessionItem_seq;
CREATE OR REPLACE TRIGGER ProfessionItem_trigger BEFORE INSERT ON ProfessionItem
FOR EACH ROW
WHEN (new.psi_id IS NULL)
BEGIN
 SELECT ProfessionItem_seq.NEXTVAL INTO :new.psi_id FROM DUAL;
END;
ALTER TABLE ProfessionItem ADD CONSTRAINT
	PK_ProfessionItem PRIMARY KEY 
	(
	psi_id
	);
commit;

 /*
Auth: Randy
Date: 2014-08-13
Desc: 目标学员修改
*/

/*修改目标学员的设置， 每天会有一个线程，根据每个课程目标学习设置，把课程的目标学员加载到这个表中，以提高平时应用中的查询效率。
   如果用户对应课程在这个表中有记录，那表示用户是该课程的目标学员
   以后查课程的目标学员都查这个表的数据，不要再去查aeItemTargetRuleDetail表
*/
CREATE TABLE itemTargetLrnDetail
  (
  itd_itm_id int NOT NULL,
  itd_usr_ent_id int NOT NULL,           
  itd_group_ind int default 0 NOT NULL ,  --用户组关键维度
  itd_grade_ind int  default 0 NOT NULL, --职级关键维度
  itd_position_ind int  default 0 NOT NULL, --岗位关键
  itd_compulsory_ind int  default 0 NOT NULL  --是否为必修
  )
  
ALTER TABLE itemTargetLrnDetail ADD CONSTRAINT
  PK_itemTargetLrnDetail PRIMARY KEY
  (
  itd_itm_id,
  itd_usr_ent_id
  )


alter table aeitemtargetrule add itr_group_ind int; --用户组关键维度
alter table aeitemtargetrule add itr_grade_ind int; --职级关键维度
alter table aeitemtargetrule add itr_position_ind int; --岗位关键
alter table aeitemtargetrule add itr_compulsory_ind int; --是否为必修
alter table aeApplication add app_nominate_type nvarchar2(20) NULL; --报名推荐类型，SUP：上司推推荐，TADM:公司推荐
--drop table aeItemTartgetRuleDetail;


/*用户职位表*/
create table UserPosition(
  upt_id    int,
  upt_code  nvarchar2(255) not null,
  upt_title  nvarchar2(255) not null,
  upt_desc  nclob null,
  upt_tcr_id  int default 1 not null ,
  pfs_update_usr_id int null,
  pfs_update_time  timestamp null
)

ALTER TABLE UserPosition ADD CONSTRAINT
	PK_UserPosition PRIMARY KEY 
	(
	upt_id
	);  
commit;

create sequence UserPosition_seq;
CREATE OR REPLACE TRIGGER UserPosition_trigger BEFORE INSERT ON UserPosition
FOR EACH ROW
WHEN (new.upt_id IS NULL)
BEGIN
 SELECT UserPosition_seq.NEXTVAL INTO :new.upt_id FROM DUAL;
END;

/*用户与职位关系表
 */
CREATE TABLE UserPositionRelation
  (
  upr_upt_id int NOT NULL,
  upr_usr_ent_id int NOT NULl
  )

ALTER TABLE UserPositionRelation ADD CONSTRAINT
  PK_UserPositionRelation PRIMARY KEY
  (
  upr_upt_id,
  upr_usr_ent_id
  )


/*更改目标学员设置表中的列名，指定到用户岗位*/
alter table aeitemtargetrule rename column itr_skill_id to itr_upt_id;
alter table aeitemtargetruledetail rename column ird_skill_id to ird_upt_id;
   
/*由于能力模型这一块功能将会删除，所以清空原来目标学员中的岗位设置，
   如果旧项目升级且要保留原来用户的岗位信息，则不需要执行以下两条SQL，但需要写程序把旧的岗位信息及用户与岗位的关系，
   迁移到UserPosition，UserPositionRelation， 并更新 aeitemtargetrule， aeitemtargetruledetail中对应的upt_id的值
*/
update aeitemtargetrule set itr_upt_id = -1;
update aeitemtargetruledetail set ird_upt_id = -1;


/*不再需要这个存贮过程*/
drop PROCEDURE getItemsForTargetUser;


/*删除能力模型这一功能模块*/
delete from  achomepage where ac_hom_ftn_ext_id in('CM_MAIN','CM_SKL_ANALYSIS','CM_ASS_MAIN');
delete from acrolefunction where rfn_ftn_id in(select ftn_id from acfunction where ftn_ext_id in('CM_MAIN','CM_SKL_ANALYSIS','CM_ASS_MAIN'));
delete from acfunction where  ftn_ext_id in('CM_MAIN','CM_SKL_ANALYSIS','CM_ASS_MAIN');

/*==============================================================*/
/* Table: 文章article                                               */
/*==============================================================*/
create table article (
   art_id               int,
   art_title            varchar2(255)         null,
   art_introduction     varchar2(2000)        null,
   art_keywords         varchar2(255)         null,
   art_content          NCLOB        null,
   art_user_id          int                  null,
   art_create_datetime  timestamp             null,
   art_update_datetime  timestamp             null,
   art_update_user_id   int                  null,
   art_location         varchar2(255)         null,
   art_type             varchar2(10)          null,
   art_status           int                  null,
   art_tcr_id           int                  null,
   art_push_mobile      int                  null,
   art_is_html          int                  null
)
create sequence article_seq;
CREATE OR REPLACE TRIGGER article_trigger BEFORE INSERT ON article
FOR EACH ROW
WHEN (new.art_id IS NULL)
BEGIN
 SELECT article_seq.NEXTVAL INTO :new.art_id FROM DUAL;
END;
ALTER TABLE article ADD CONSTRAINT
	PK_article PRIMARY KEY 
	(
	art_id
	);  


/* 
 Auth: leon
添加文章管理TADM的权限
*/
insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('ARTICLE_MAIN', 'FUNCTION', 'HOMEPAGE', sysdate, null);

insert into acRoleFunction (rfn_rol_id, rfn_ftn_id)
select rol_id, ftn_id from acRole, acFunction
where rol_ext_id like 'TADM_%' and ftn_ext_id = 'ARTICLE_MAIN';

insert into acHomePage (ac_hom_rol_ext_id,ac_hom_ftn_ext_id, ac_hom_create_usr_id, ac_hom_create_timestamp)
select rol_ext_id, 'ARTICLE_MAIN', 's1u3', sysdate from acRole where rol_ext_id like 'TADM_%';

 /* 
 Auth: randy
在设置tc_independent=true时， 二级培训中心可以分别单独设置自动/手动积分点。 
把原有的默认积分项目更新到根培训中心下。
*/
update creditsType set cty_tcr_id =(select tcr_id from  tcTrainingCenter where tcr_parent_tcr_id is null)
where cty_tcr_id is null;

/* 
 Auth: lance
   在宣传栏管理里加入不同语言logo字段,默认logo为logo.jpg
*/
alter table SitePoster add sp_logo_file_cn nvarchar2 (100)  default 'logo.jpg' NOT NULL;
alter table SitePoster add sp_logo_file_hk nvarchar2 (100)  default 'logo.jpg' NOT NULL;
alter table SitePoster add sp_logo_file_us nvarchar2 (100)  default 'logo.jpg' NOT NULL;

 /* 
 Auth: randy
由于"友情链接"功能在学员端已移除,所以同时也把管理员端相关功能移除.
*/
delete from  achomepage where ac_hom_ftn_ext_id in('CM_MAIN','CM_SKL_ANALYSIS','CM_ASS_MAIN');
delete from acrolefunction where rfn_ftn_id in(select ftn_id from acfunction where ftn_ext_id in('CM_MAIN','CM_SKL_ANALYSIS','CM_ASS_MAIN'));
delete from acfunction where  ftn_ext_id in('CM_MAIN','CM_SKL_ANALYSIS','CM_ASS_MAIN');


 /* 
 Auth: randy
在课程， 班级中添加学分属性
*/
alter table aeItemExtension add ies_credit float  NULL;

/* 
 Auth: lance
  为培训管理员增加宣传栏管理权限
*/
insert into acRoleFunction select rol_id,ftn_id,'slu3',sysdate from acRole,acFunction where rol_ext_id like 'TADM_%' and ftn_ext_id='POSTER_MAIN';
insert into acHomePage select null,rol_ext_id,'POSTER_MAIN','s1u3',sysdate from acRole where rol_ext_id like 'TADM_%';

/* 
 Auth: lance
  为系统管理员增加在线问答管理权限
*/
insert into acRoleFunction select rol_id,ftn_id,'slu3',sysdate from acRole,acFunction where rol_ext_id like 'ADM_%' and ftn_ext_id='KNOW_MAIN';
insert into acHomePage select null,rol_ext_id,'KNOW_MAIN','s1u3',sysdate from acRole where rol_ext_id like 'ADM_%';

/* 
 Auth: lance
  增加文章类型表
*/
create table articleType(
  aty_id                 int            ,
  aty_title              nvarchar2(255)        null,
  aty_tcr_id        int         null,
  aty_create_user_id    nvarchar2(50)        null,
  aty_create_datetime    timestamp            null,
  aty_update_user_id     nvarchar2(50)        null,
  aty_update_datetime    timestamp            null
)
create sequence articleType_seq;
CREATE OR REPLACE TRIGGER articleType_trigger BEFORE INSERT ON articleType
FOR EACH ROW
WHEN (new.aty_id IS NULL)
BEGIN
 SELECT articleType_seq.NEXTVAL INTO :new.aty_id FROM DUAL;
END;
ALTER TABLE articleType ADD CONSTRAINT
	PK_articleType PRIMARY KEY 
	(
	aty_id
	);  

/* 
 Auth: lance
  为系统管理员增加文章管理权限
*/
insert into acRoleFunction select rol_id,ftn_id,'slu3',sysdate from acRole,acFunction where rol_ext_id like 'ADM_%' and ftn_ext_id='ARTICLE_MAIN';
insert into acHomePage select null,rol_ext_id,'ARTICLE_MAIN','s1u3',sysdate from acRole where rol_ext_id like 'ADM_%';

/*
Auth: Lance
Date: 2014-9-5
Desc: 为个人信息隐私设置增加我的档案和我的收藏设置
*/
alter table sns_setting add s_set_my_files int;
alter table sns_setting add s_set_my_collection int;



/* 
 Auth: Randy
  经讨论后，决定保留面授课程，去掉混合式课程
*/
update aeItemType set ity_blend_ind = 0 where ity_id = 'CLASSROOM';
update aeItem set itm_blend_ind = 0 where itm_type = 'CLASSROOM' and itm_blend_ind = 1;



/* 
 Auth: Randy
  把社区化这一块的加入积分
*/
--创建群组积分
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_period,cty_hit)
VALUES('SYS_CREATE_GROUP','SYS_CREATE_GROUP',0,0,0,1,'SYS',1,10,'s1u3',sysdate,'s1u3',sysdate,null,null);

--参与群组积分
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_period,cty_hit)
VALUES('SYS_JION_GROUP','SYS_JION_GROUP',0,0,0,1,'SYS',1,10,'s1u3',sysdate,'s1u3',sysdate,null,null);

--被点赞积分
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_period,cty_hit)
VALUES('SYS_GET_LIKE','SYS_GET_LIKE',0,0,0,1,'SYS',1,10,'s1u3',sysdate,'s1u3',sysdate,null,null);

--点赞积分
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_period,cty_hit)
VALUES('SYS_CLICK_LIKE','SYS_CLICK_LIKE',0,0,0,1,'SYS',1,10,'s1u3',sysdate,'s1u3',sysdate,null,null);

--发表课程评论积分
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_period,cty_hit)
VALUES('SYS_COS_COMMENT','SYS_COS_COMMENT',0,0,0,1,'SYS',1,5,'s1u3',sysdate,'s1u3',sysdate,'MONTH',10);

update creditsType set cty_tcr_id =(select tcr_id from  tcTrainingCenter where tcr_parent_tcr_id is null)
where cty_tcr_id is null;

/* 
 Auth: leon
  评论表加上评论对象人
*/
alter table sns_comment add s_cmt_reply_to_uid int default(0);

/* 
 Auth: leon
 课程加上访问量
*/
alter table aeItemExtension add ies_access_count int;

/**
* 赞，分享，收藏的统计
*/
create table sns_count(
  s_cnt_id int,
  s_cnt_target_id int default(0),
  s_cnt_collect_count int default(0),
  s_cnt_share_count int default(0),
  s_cnt_like_count int default(0),
  s_cnt_update_time timestamp,
  s_cnt_create_time timestamp,
  s_cnt_create_usr_id int,
  s_cnt_update_usr_id int,
  s_cnt_module nvarchar2(32)
)
create sequence sns_count_seq;
CREATE OR REPLACE TRIGGER sns_count_trigger BEFORE INSERT ON sns_count
FOR EACH ROW
WHEN (new.s_cnt_id IS NULL)
BEGIN
 SELECT sns_count_seq.NEXTVAL INTO :new.s_cnt_id FROM DUAL;
END;
ALTER TABLE sns_count ADD CONSTRAINT
	PK_sns_count PRIMARY KEY 
	(
	s_cnt_id
	); 

/* 
 Auth: lance
  为群组表加上培训中心id字段
*/
alter table sns_group add s_grp_tcr_id int;
alter table sns_group add s_grp_card nvarchar2 (100);

/* 
 Auth: Jimmy
  试卷显示方式增加"一屏多题"
*/
alter table module add  mod_test_style nvarchar2(10) default 'many';

/* 
 Auth: Joyce
  新邮件模板及发送邮件相关表
*/

--邮件模板
CREATE TABLE messageTemplate
  (
  mtp_id int,
  mtp_type nvarchar2(255) NOT NULL, --模板类型
  mtp_subject nvarchar2(255) NOT NULL,--邮件默认主题
  mtp_content nclob NOT NULL,--邮件内容
  mtp_content_email_link nvarchar2(255) NULL,--内容url(邮件)
  mtp_content_pc_link nvarchar2(255) NULL,--内容url(站内信电脑)
  mtp_content_mobile_link nvarchar2(255) NULL,--内容url(站内信手机)
  mtp_remark_label nvarchar2(255) NULL,--备注Label
  mtp_web_message_ind number(2)  default(1)  NOT NULL,--是否站内信
  mtp_active_ind number(2) default(1)  NOT NULL,--是否启用
  mtp_tcr_id int NOT NULL,--关联培训中心ID(LN启用)
  mtp_update_ent_id int NOT NULL,
  mtp_update_timestamp timestamp NOT NULL
)
create sequence messageTemplate_seq;
CREATE OR REPLACE TRIGGER messageTemp_trigger BEFORE INSERT ON messageTemplate
FOR EACH ROW
WHEN (new.mtp_id IS NULL)
BEGIN
 SELECT messageTemplate_seq.NEXTVAL INTO :new.mtp_id FROM DUAL;
END;

ALTER TABLE messageTemplate ADD CONSTRAINT
	PK_messageTemplate PRIMARY KEY 
	(
	mtp_id
	);


--邮件模板参数
CREATE TABLE messageParamName
  (
  mpn_id int,
  mpn_mtp_id int NOT NULL, --模板类型
  mpn_name nvarchar2(255) NOT NULL, --参数名
  mpn_name_desc nvarchar2(255) NOT NULL --参数名描述
  );

create sequence messageParamName_seq;
CREATE OR REPLACE TRIGGER messageParamName_trigger BEFORE INSERT ON messageParamName
FOR EACH ROW
WHEN (new.mpn_id IS NULL)
BEGIN
 SELECT messageParamName_seq.NEXTVAL INTO :new.mpn_id FROM DUAL;
END;

ALTER TABLE messageParamName ADD CONSTRAINT
	PK_messageParamName PRIMARY KEY 
	(
	mpn_id
	);

ALTER TABLE messageParamName ADD CONSTRAINT FK_mpn_mtp
FOREIGN KEY (mpn_mtp_id) REFERENCES messageTemplate (mtp_id);


--邮件
CREATE TABLE emailMessage
  (
  emsg_id int,
  emsg_mtp_id int NULL, --模板ID
  emsg_send_ent_id int NOT NULL, --发件人
  emsg_rec_ent_ids nvarchar2(2000) NOT NULL, --收件人
  emsg_cc_ent_ids nvarchar2(2000) NULL, --抄送人
  emsg_subject nvarchar2(255) NOT NULL,--邮件主题
  emsg_content nclob NOT NULL,--邮件内容
  emsg_target_datetime timestamp NULL,--定时发送时间
  emsg_create_ent_id  int NOT NULL, --创建人
  emsg_create_timestamp timestamp NOT NULL--创建时间
  )
create sequence emailMessage_seq;
CREATE OR REPLACE TRIGGER emailMessage_trigger BEFORE INSERT ON emailMessage
FOR EACH ROW
WHEN (new.emsg_id IS NULL)
BEGIN
 SELECT emailMessage_seq.NEXTVAL INTO :new.emsg_id FROM DUAL;
END;

ALTER TABLE emailMessage ADD CONSTRAINT
	PK_emailMessage PRIMARY KEY 
	(
	emsg_id
	); 
  
ALTER TABLE emailMessage ADD CONSTRAINT FK_emsg_mtp 
FOREIGN KEY (emsg_mtp_id) REFERENCES messageTemplate (mtp_id);

--站内信
CREATE TABLE webMessage
  (
  wmsg_id int,
  wmsg_mtp_id int NULL, --模板ID
  wmsg_send_ent_id int NOT NULL, --发件人
  wmsg_rec_ent_id int NOT NULL, --收件人
  wmsg_subject nvarchar2(255) NOT NULL,--主题
  wmsg_content_pc nclob NOT NULL,--内容(电脑)
  wmsg_content_mobile nclob NULL,--内容(手机)
  wmsg_target_datetime timestamp NULL,--定时发送时间
  wmsg_type nvarchar2(64) default 'SYS' NOT NULL,--信息类型
  wmsg_create_ent_id  int NOT NULL, --创建人
  wmsg_create_timestamp timestamp NOT NULL--创建时间
  );
create sequence webMessage_seq;
CREATE OR REPLACE TRIGGER webMessage_trigger BEFORE INSERT ON webMessage
FOR EACH ROW
WHEN (new.wmsg_id IS NULL)
BEGIN
 SELECT webMessage_seq.NEXTVAL INTO :new.wmsg_id FROM DUAL;
END;

ALTER TABLE webMessage ADD CONSTRAINT
	PK_webMessage PRIMARY KEY 
	(
	wmsg_id
	); 
  
ALTER TABLE webMessage ADD CONSTRAINT FK_wmsg_mtp 
FOREIGN KEY (wmsg_mtp_id) REFERENCES messageTemplate (mtp_id);

--邮件发送历史
CREATE TABLE emailMsgRecHistory
  (
  emrh_emsg_id  int NOT NULL, --邮件ID
  emrh_status nvarchar2(20) NOT NULL,--Y:发送成功 N:发送失败
  emrh_sent_datetime timestamp NULL,--发送时间
  emrh_attempted int  default 0 NOT NULL --发送次数
  );
ALTER TABLE emailMsgRecHistory ADD CONSTRAINT FK_emrh_wmsg 
FOREIGN KEY (emrh_emsg_id) REFERENCES emailMessage(emsg_id);

CREATE INDEX IX_emailMsgRecHistory ON emailMsgRecHistory(emrh_status,emrh_attempted)

--站内信阅读历史
CREATE TABLE webMsgReadHistory
  (
  wmrh_wmsg_id  int NOT NULL, --消息ID
  wmrh_status nvarchar2(20) NOT NULL,--Y:已读 N或NULL: 未读
  wmrh_read_datetime timestamp NULL--阅读时间
  );

ALTER TABLE webMsgReadHistory ADD CONSTRAINT FK_wmrh_emsg 
FOREIGN KEY (wmrh_wmsg_id) REFERENCES webMessage(wmsg_id);


CREATE INDEX IX_webMsgReadHistory ON webMsgReadHistory(wmrh_status)




/**
 * Leon
 * 动态的附件
 */
create table moduleTempFile(
  mtf_id int,
  mtf_target_id int,
  mtf_module nvarchar2(32),
  mtf_usr_id int not null,
  mtf_file_type nvarchar2(32),
  mtf_file_name nvarchar2(255),
  mtf_file_rename nvarchar2(255),
  mtf_file_size NUMBER(19),
  mtf_create_time timestamp,
  mtf_url nvarchar2(255),   
  mtf_type nvarchar2(32)
)
create sequence moduleTempFile_seq;
CREATE OR REPLACE TRIGGER moduleTempFile_trigger BEFORE INSERT ON moduleTempFile
FOR EACH ROW
WHEN (new.mtf_id IS NULL)
BEGIN
 SELECT moduleTempFile_seq.NEXTVAL INTO :new.mtf_id FROM DUAL;
END;

ALTER TABLE moduleTempFile ADD CONSTRAINT
	PK_moduleTempFile PRIMARY KEY 
	(
	mtf_id
	);



/**
 * lance
 * 为群组添加状态字段
 */
alter table sns_group add s_grp_status nvarchar2(20) default 'OK';

/**
 * lance
 * 为隐私设置添加我的学习记录，我的积分，我的学习概况设置
 */
alter table sns_setting add s_set_my_credit int;
alter table sns_setting add s_set_my_learning_record int;
alter table sns_setting add s_set_my_learning_situation int;

/**
 * lance
 * 学习概况
 */
create table LearningSituation(
  ls_id int ,
  ls_ent_id int NOT NULL,            --学习概况者
  ls_learn_duration int NOT NULL,        --学习总时长
  ls_learn_credit int NOT NULL,        --总学分
  ls_total_integral int NOT NULL,        --总积分
  ls_total_courses int NOT NULL,        --课程总数
  ls_course_completed_num int NOT NULL,    --已完成课程总数
  ls_course_fail_num int NOT NULL,      --未完成课程总数
  ls_course_inprogress_num int NOT NULL,    --进行中课程总数
  ls_course_pending_num int NOT NULL,      --审批中课程总数
  ls_total_exams int NOT NULL,        --考试总数
  ls_exam_completed_num int NOT NULL,      --已完成考试总数
  ls_exam_fail_num int NOT NULL,         --未完成考试总数
  ls_exam_inprogress_num int NOT NULL,    --进行中考试总数
  ls_exam_pending_num int NOT NULL,      --审批中考试总数
  ls_fans_num int NOT NULL,          --粉丝
  ls_attention_num int NOT NULL,        --关注
  ls_praised_num int NOT NULL,        --被赞
  ls_praise_others_num int NOT NULL,      --赞他人
  ls_collect_num int NOT NULL,        --收藏
  ls_share_num int NOT NULL,          --分享
  ls_create_group_num int NOT NULL,      --创建群组
  ls_join_group_num int NOT NULL,        --参与群组
  ls_group_speech_num int NOT NULL,      --群组发言
  ls_question_num int NOT NULL,        --提问数
  ls_answer_num int NOT NULL,          --回答数
  ls_update_time timestamp NOT NULL      --更新时间 
)
create sequence LearningSituation_seq;
CREATE OR REPLACE TRIGGER LearningSituation_trigger BEFORE INSERT ON LearningSituation
FOR EACH ROW
WHEN (new.ls_id IS NULL)
BEGIN
 SELECT LearningSituation_seq.NEXTVAL INTO :new.ls_id FROM DUAL;
END;

ALTER TABLE LearningSituation ADD CONSTRAINT
	PK_LearningSituation PRIMARY KEY 
	(
	ls_id
	);

/*
Auth: Joyce
Date: 2014-10-4
Desc: add new function 邮件模板管理
*/
insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('MESSAGE_TEMPLATE', 'FUNCTION', 'HOMEPAGE', sysdate, null);

insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp)
select rol_id, ftn_id, sysdate from acRole, acFunction
where rol_ext_id like 'ADM_%' and ftn_ext_id = 'MESSAGE_TEMPLATE';

insert into acHomePage (ac_hom_rol_ext_id,ac_hom_ftn_ext_id, ac_hom_create_usr_id, ac_hom_create_timestamp)
select rol_ext_id, 'MESSAGE_TEMPLATE', 's1u3', sysdate from acRole where rol_ext_id like 'ADM_%';




-- 初始化邮件模板及模板参数
insert into messageTemplate(mtp_type, mtp_subject, mtp_content, mtp_remark_label, mtp_web_message_ind, mtp_active_ind, mtp_tcr_id, mtp_update_timestamp, mtp_update_ent_id) values ('ENROLLMENT_NEW','Course Enrollment Request Received','Course Enrollment Request Received','label_ENROLLMENT_NEW',1,1,1,sysdate,3);
insert into messageTemplate(mtp_type, mtp_subject, mtp_content, mtp_remark_label, mtp_web_message_ind, mtp_active_ind, mtp_tcr_id, mtp_update_timestamp, mtp_update_ent_id) values ('ENROLLMENT_CONFIRMED','Course Enrollment Confirmed','Course Enrollment Confirmed','label_ENROLLMENT_CONFIRMED',1,1,1,sysdate,3);
insert into messageTemplate(mtp_type, mtp_subject, mtp_content, mtp_remark_label, mtp_web_message_ind, mtp_active_ind, mtp_tcr_id, mtp_update_timestamp, mtp_update_ent_id) values ('ENROLLMENT_WAITLISTED','Course Enrollment: Waiting List','Course Enrollment: Waiting List','label_ENROLLMENT_WAITLISTED',1,1,1,sysdate,3);
insert into messageTemplate(mtp_type, mtp_subject, mtp_content, mtp_remark_label, mtp_web_message_ind, mtp_active_ind, mtp_tcr_id, mtp_update_timestamp, mtp_update_ent_id) values ('ENROLLMENT_NOT_CONFIRMED','Course Enrollment Request Declined','Course Enrollment Request Declined','label_ENROLLMENT_NOT_CONFIRMED',1,1,1,sysdate,3);
insert into messageTemplate(mtp_type, mtp_subject, mtp_content, mtp_remark_label, mtp_web_message_ind, mtp_active_ind, mtp_tcr_id, mtp_update_timestamp, mtp_update_ent_id) values ('ENROLLMENT_WITHDRAWAL_APPROVED','Course Enrollment Request Cancelled','Course Enrollment Request Cancelled','label_ENROLLMENT_WITHDRAWAL_APPROVED',1,1,1,sysdate,3);
insert into messageTemplate(mtp_type, mtp_subject, mtp_content, mtp_remark_label, mtp_web_message_ind, mtp_active_ind, mtp_tcr_id, mtp_update_timestamp, mtp_update_ent_id) values ('ENROLLMENT_REMOVED','Course Enrollment Request Cancelled','Course Enrollment Request Cancelled','label_ENROLLMENT_REMOVED',1,1,1,sysdate,3);
insert into messageTemplate(mtp_type, mtp_subject, mtp_content, mtp_remark_label, mtp_web_message_ind, mtp_active_ind, mtp_tcr_id, mtp_update_timestamp, mtp_update_ent_id) values ('ENROLLMENT_NEXT_APPROVERS','Course Enrollment Approval','Course Enrollment Approval','label_ENROLLMENT_NEXT_APPROVERS',1,1,1,sysdate,3);
insert into messageTemplate(mtp_type, mtp_subject, mtp_content, mtp_remark_label, mtp_web_message_ind, mtp_active_ind, mtp_tcr_id, mtp_update_timestamp, mtp_update_ent_id) values ('ENROLLMENT_REMOVED_REMINDER','Course Enrollment Notification','Course Enrollment Notification','label_ENROLLMENT_REMOVED_REMINDER',1,1,1,sysdate,3);
insert into messageTemplate(mtp_type, mtp_subject, mtp_content, mtp_remark_label, mtp_web_message_ind, mtp_active_ind, mtp_tcr_id, mtp_update_timestamp, mtp_update_ent_id) values ('ENROLLMENT_WITHDRAWAL_REMINDER','Course Enrollment Notification','Course Enrollment Notification','label_ENROLLMENT_WITHDRAWAL_REMINDER',1,1,1,sysdate,3);
insert into messageTemplate(mtp_type, mtp_subject, mtp_content, mtp_remark_label, mtp_web_message_ind, mtp_active_ind, mtp_tcr_id, mtp_update_timestamp, mtp_update_ent_id) values ('ENROLLMENT_CONFIRMED_REMINDER','Course Enrollment Notification','Course Enrollment Notification','label_ENROLLMENT_CONFIRMED_REMINDER',1,1,1,sysdate,3);
insert into messageTemplate(mtp_type, mtp_subject, mtp_content, mtp_remark_label, mtp_web_message_ind, mtp_active_ind, mtp_tcr_id, mtp_update_timestamp, mtp_update_ent_id) values ('ENROLLMENT_APPROVED_REMINDER','Course Enrollment Notification','Course Enrollment Notification','label_ENROLLMENT_APPROVED_REMINDER',1,1,1,sysdate,3);
insert into messageTemplate(mtp_type, mtp_subject, mtp_content, mtp_remark_label, mtp_web_message_ind, mtp_active_ind, mtp_tcr_id, mtp_update_timestamp, mtp_update_ent_id) values ('ENROLLMENT_NO_SUPERVISOR','Course Enrollment Approval','Course Enrollment Approval','label_ENROLLMENT_NO_SUPERVISOR',1,1,1,sysdate,3);
insert into messageTemplate(mtp_type, mtp_subject, mtp_content, mtp_remark_label, mtp_web_message_ind, mtp_active_ind, mtp_tcr_id, mtp_update_timestamp, mtp_update_ent_id) values ('COURSE_NOTIFY','Course Completion Reminder','Course Completion Reminder','label_COURSE_NOTIFY',1,1,1,sysdate,3);
insert into messageTemplate(mtp_type, mtp_subject, mtp_content, mtp_remark_label, mtp_web_message_ind, mtp_active_ind, mtp_tcr_id, mtp_update_timestamp, mtp_update_ent_id) values ('JI','Joining Instruction','Joining Instruction','label_JI',1,1,1,sysdate,3);
insert into messageTemplate(mtp_type, mtp_subject, mtp_content, mtp_remark_label, mtp_web_message_ind, mtp_active_ind, mtp_tcr_id, mtp_update_timestamp, mtp_update_ent_id) values ('REMINDER','Reminder','Reminder','label_REMINDER',1,1,1,sysdate,3);
insert into messageTemplate(mtp_type, mtp_subject, mtp_content, mtp_remark_label, mtp_web_message_ind, mtp_active_ind, mtp_tcr_id, mtp_update_timestamp, mtp_update_ent_id) values ('USR_REG_APPROVE','USR_REG_APPROVE','USR_REG_APPROVE','label_USR_REG_APPROVE',1,1,1,sysdate,3);
insert into messageTemplate(mtp_type, mtp_subject, mtp_content, mtp_remark_label, mtp_web_message_ind, mtp_active_ind, mtp_tcr_id, mtp_update_timestamp, mtp_update_ent_id) values ('USR_REG_DISAPPROVE','USR_REG_DISAPPROVE','USR_REG_DISAPPROVE','label_USR_REG_DISAPPROVE',1,1,1,sysdate,3);
insert into messageTemplate(mtp_type, mtp_subject, mtp_content, mtp_remark_label, mtp_web_message_ind, mtp_active_ind, mtp_tcr_id, mtp_update_timestamp, mtp_update_ent_id) values ('USER_IMPORT_SUCCESS','User Import Success Notification','User Import Success Notification','label_USER_IMPORT_SUCCESS',1,1,1,sysdate,3);
insert into messageTemplate(mtp_type, mtp_subject, mtp_content, mtp_remark_label, mtp_web_message_ind, mtp_active_ind, mtp_tcr_id, mtp_update_timestamp, mtp_update_ent_id) values ('ENROLLMENT_IMPORT_SUCCESS','Enrollment Import Success Notification','Enrollment Import Success Notification','label_ENROLLMENT_IMPORT_SUCCESS',1,1,1,sysdate,3);
insert into messageTemplate(mtp_type, mtp_subject, mtp_content, mtp_remark_label, mtp_web_message_ind, mtp_active_ind, mtp_tcr_id, mtp_update_timestamp, mtp_update_ent_id) values ('SYS_PERFORMANCE_NOTIFY','System Performance Notify','System Performance Notify','label_SYS_PERFORMANCE_NOTIFY',1,1,1,sysdate,3);

update messageTemplate set mtp_tcr_id =(select tcr_id from  tcTrainingCenter where tcr_parent_tcr_id is null);

insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (1,'[Learner name]','lab_msg_learner_name');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (1,'[Course name]','lab_msg_course_name');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (1,'[Course code]','lab_msg_course_code');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (2,'[Learner name]','lab_msg_learner_name');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (2,'[Course name]','lab_msg_course_name');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (2,'[Course code]','lab_msg_course_code');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (2,'[Link to login page]','lab_link_to_login_page');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (3,'[Learner name]','lab_msg_learner_name');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (3,'[Course name]','lab_msg_course_name');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (3,'[Course code]','lab_msg_course_code');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (4,'[Learner name]','lab_msg_learner_name');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (4,'[Course name]','lab_msg_course_name');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (4,'[Course code]','lab_msg_course_code');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (4,'[Training Administrator]','lab_msg_ta');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (5,'[Learner name]','lab_msg_learner_name');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (5,'[Course name]','lab_msg_course_name');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (5,'[Course code]','lab_msg_course_code');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (6,'[Learner name]','lab_msg_learner_name');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (6,'[Course name]','lab_msg_course_name');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (6,'[Course code]','lab_msg_course_code');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (7,'[Learner name]','lab_msg_learner_name');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (7,'[Course name]','lab_msg_course_name');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (7,'[Course code]','lab_msg_course_code');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (7,'[Link to login page]','lab_link_to_login_page');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (8,'[Course name]','lab_msg_course_name');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (8,'[Course code]','lab_msg_course_code');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (9,'[Learner name]','lab_msg_learner_name');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (9,'[Course name]','lab_msg_course_name');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (9,'[Course code]','lab_msg_course_code');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (10,'[Learner name]','lab_msg_learner_name');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (10,'[Course name]','lab_msg_course_name');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (10,'[Course code]','lab_msg_course_code');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (11,'[Learner name]','lab_msg_learner_name');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (11,'[Course name]','lab_msg_course_name');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (11,'[Course code]','lab_msg_course_code');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (11,'[Approval Name]','lab_msg_approve_name');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (12,'[Learner name]','lab_msg_learner_name');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (12,'[Course name]','lab_msg_course_name');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (12,'[Course code]','lab_msg_course_code');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (12,'[Link to login page]','lab_link_to_login_page');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (13,'[Learner name]','lab_msg_learner_name');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (13,'[Course name]','lab_msg_course_name');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (13,'[Course code]','lab_msg_course_code');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (13,'[Course period end date] ','lab_msg_course_end_date');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (14,'[Course name]','lab_msg_course_name');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (14,'[Class name]','lab_msg_class_name');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (14,'[Course period start date]','lab_msg_course_start_date');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (14,'[Course period end date] ','lab_msg_course_end_date');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (14,'[Learner(s)]','lab_msg_learner_lst');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (14,'[Training Administrator]','lab_msg_ta');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (15,'[Course name]','lab_msg_course_name');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (15,'[Class name]','lab_msg_class_name');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (15,'[Course period start date]','lab_msg_course_start_date');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (15,'[Course period end date] ','lab_msg_course_end_date');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (15,'[Learner(s)]','lab_msg_learner_lst');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (15,'[Training Administrator]','lab_msg_ta');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (16,'[Learner name]','lab_msg_learner_name');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (16,'[Link to login page]','lab_link_to_login_page');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (16,'[Learner password]','lab_msg_learner_pwd');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (17,'[Learner name]','lab_msg_learner_name');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (17,'[Refuse reason]','lab_msg_refuse_reason');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (18,'[User name]','lab_msg_user_name');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (18,'[Src file]','lab_msg_src_file');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (18,'[Import start date]','lab_msg_import_start_date');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (18,'[Success total]','lab_msg_success_total');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (18,'[Unsuccess total]','lab_msg_unscuccess_total');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (18,'[Import end date]','lab_msgimport_end_date');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (19,'[User name]','lab_msg_user_name');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (19,'[Src file]','lab_msg_src_file');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (19,'[Import start date]','lab_msg_import_start_date');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (19,'[Success total]','lab_msg_success_total');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (19,'[Unsuccess total]','lab_msg_unscuccess_total');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (19,'[Import end date]','lab_msgimport_end_date');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (20,'[Active user]','lab_msg_active_user');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (20,'[Warning user]','lab_msg_warning_user');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (20,'[Blocking user]','lab_msg_blocking_user');
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) values (20,'[Warning date]','lab_msg_warning_date');

/**
 动态关联动作对象
 leon
*/
alter table sns_doing add s_doi_act_id int 

/*
Auth: Joyce
Date: 2014-10-4
Desc: 
*/
--无模板
insert into messageTemplate(mtp_type, mtp_subject, mtp_content, mtp_remark_label, mtp_web_message_ind, mtp_active_ind, mtp_tcr_id, mtp_update_timestamp, mtp_update_ent_id) values ('EMPTY_TYPE','empty subject','empty content','label_empty_type',1,0,1,sysdate,3);

insert into messageParamName(mpn_mtp_id,MPN_NAME,MPN_NAME_desc) select mtp_id, '[Login id]', 'lab_msg_login_id' from messageTemplate where mtp_type='USR_REG_APPROVE';

update messageParamName set mpn_name='[Course period end date]' where mpn_name='[Course period end date] ';


/*
Auth: Joyce
Date: 2014-10-4
Desc: 开课通知
*/
Drop Table aeItemMessage;
create table aeItemMessage(
  img_itm_id int NOT NULL, --班级ID
  img_app_id int NOT NULL, --App ID
  img_msg_id int NOT NULL, --Message ID
  img_msg_type nvarchar2(20) NOT NULL,--Message Type (email|web)
  img_mtp_type nvarchar2(20) NOT NULL, --(JI|REMINDER)
  img_create_usr_id nvarchar2(20) NOT NULL,
  img_create_timestamp timestamp NOT NULL,
  img_update_usr_id nvarchar2(20) NOT NULL,
  img_update_timestamp timestamp NOT NULL
)

ALTER TABLE aeItemMessage ADD CONSTRAINT
	PK_aeItemMessage PRIMARY KEY 
	(
	img_itm_id,img_msg_id,img_msg_type
	);
alter table aeItem add itm_ji_send_datetime timestamp;
alter table aeItem add itm_reminder_send_datetime timestamp;

/**
 * Randy
 * 重建视图
 */
/* 用户关系视图 */

DROP view V_usrTcRelation;
create view V_usrTcRelation as
select ern_child_ent_id as u_id, ern_ancestor_ent_id as usg_id, ern_parent_ind, ern_order, tcr_id, tcr_code, tcr_title, tcr_parent_tcr_id
from tctrainingcentertargetentity
inner join entityrelation on(ern_ancestor_ent_id =tce_ent_id)
inner join tcTrainingcenter on( tcr_id =tce_tcr_id) 
where  ern_type = 'USR_PARENT_USG'

/*
Auth: Joyce
Date: 2014-10-4
Desc: 邮件、站内信增加附件字段
*/
alter table EmailMessage add emsg_attachment nvarchar2(2000);--附件，多个逗号分隔
alter table webMessage add wmsg_attachment nvarchar2(2000);--附件，多个逗号分隔


/**
 * 动态通知类型的回复对象id
*/
alter table sns_doing add s_doi_reply_id int;  --回复动态
alter table sns_doing add s_doi_operator_uid int;  --操作的人
alter table sns_doing add s_doi_target_type nvarchar2(20);  --1 ,表示为通知，0不是

/**
* 删除评论的时候，删除与之相关联的评论，赞，以及统计信息
**/
create or replace trigger delDoingRel
 after delete on  sns_doing
  for each row
     declare   
     PRAGMA AUTONOMOUS_TRANSACTION; 
  begin

  delete  from sns_comment
  where s_cmt_target_id = :old.s_doi_id;

  delete  from sns_doing
  where s_doi_target_id = :old.s_doi_id and s_doi_target_type = '1';

  delete  from sns_valuation_log
  where s_vtl_target_id = :old.s_doi_id and s_vtl_module = :old.s_doi_module;

  delete from sns_count
  where s_cnt_target_id =:old.s_doi_id and s_cnt_module = :old.s_doi_module;

   commit; 
  end;
  
    /**
  删除动态的时候删除附带的评论，赞一记统计信息
  */
  create or replace trigger delCommentRel
  after delete on sns_comment
  for each row
       declare   
     PRAGMA AUTONOMOUS_TRANSACTION; 
  begin
  delete from sns_valuation_log
  where s_vtl_target_id =:old.s_cmt_id and s_vtl_module = :old.s_cmt_module;
  commit; 
  delete from sns_count
  where s_cnt_target_id=:old.s_cmt_id and s_cnt_module = :old.s_cmt_module;
   commit; 
  end;

/*
Auth: Joyce
Date: 2014-10-4
Desc: 新增找回密码邮件
*/

insert into messageTemplate(mtp_type, mtp_subject, mtp_content, mtp_remark_label, mtp_web_message_ind, mtp_active_ind, mtp_tcr_id, mtp_update_timestamp, mtp_update_ent_id) 
values ('USER_PWD_RESET_NOTIFY',N'找回密码',N'找回密码','lab_USER_PWD_RESET_NOTIFY',0,1,1,sysdate,3);


insert into messageParamName(mpn_mtp_id,MPN_NAME,MPN_NAME_desc) select mtp_id, '[Request time]', 'lab_msg_request_time' from messageTemplate where mtp_type='USER_PWD_RESET_NOTIFY';
insert into messageParamName(mpn_mtp_id,MPN_NAME,MPN_NAME_desc) select mtp_id, '[Max days]', 'lab_msg_max_days' from messageTemplate where mtp_type='USER_PWD_RESET_NOTIFY';
insert into messageParamName(mpn_mtp_id,MPN_NAME,MPN_NAME_desc) select mtp_id, '[Link to reset password]', 'lab_msg_link_to_reset_password' from messageTemplate where mtp_type='USER_PWD_RESET_NOTIFY';

/**
初始化邮件模板邮件内容
*/
update messageTemplate set mtp_remark_label = 'lab_EMPTY_TYPE' where mtp_remark_label = 'label_empty_type';
update messageTemplate set mtp_remark_label = replace(mtp_remark_label,'label','lab');

update messageTemplate set mtp_content = '<p>   Dear <span style="color:#337FE5;"><span style="color:#4C33E5;">[Learner name]</span><span style="color:#4C33E5;">,</span></span>   </p>  <p>   Thank you for your interest in applying for the course<span style="color:#4C33E5;"> [Course name] </span><span style="color:#4C33E5;">([Course code]). </span>We are reviewing your enrollment request. Once the enrollment is confirmed. You will receive the confirmation by email.&nbsp;  </p>  <p>   Regards,<br />  Training &amp; Development  </p>' where mtp_type='ENROLLMENT_NEW';
update messageTemplate set mtp_content = '<p>   <span style="font-family:&quot;font-size:10pt;">Dear <span style="color:blue;">[Learner name]</span>,</span>   </p>  <p>   <span> </span>   </p>  <p align="left" style="text-align:left;">   <span style="font-family:&quot;font-size:10pt;">Thank you for your recent  enrollment of the course</span><span style="color:blue;"><span> </span></span><span style="color:blue;font-family:&quot;font-size:10pt;">[Course name]([Course code])</span><span style="font-family:&quot;font-size:10pt;">. We  have reviewed and approved your enrollment request. You can start your course  by logging into the system using the link below:</span>   </p>  <p>   <span> </span>   </p>  <p align="left" style="text-align:left;">   <span style="color:blue;font-family:&quot;font-size:10pt;">[Link to login page]</span>   </p>  <p>   <span> </span>   </p>  <p align="left" style="text-align:left;">   <span style="font-family:&quot;font-size:10pt;">Regards,<br />  Training &amp; Development</span>&nbsp;  </p>' where mtp_type='ENROLLMENT_CONFIRMED';
update messageTemplate set mtp_content = '<p align="left" style="background:white;text-align:left;">   <span style="font-family:&quot;Verdana&quot;,&quot;sans-serif&quot;;font-size:10pt;">Dear <span style="color:blue;">[Learner name]</span>,</span>  </p>  <span> </span>  <p>   <span style="font-family:&quot;Verdana&quot;,&quot;sans-serif&quot;;font-size:10pt;">Thank you for your interest in applying for the  course <span style="color:blue;">[Course name]([Course code])</span>. We regret  to inform you that due to over-subscription, we are not able to enroll you to  the course. However, we have put your request on the waiting list and will  notify you if there is further arrangement. </span>  </p>  <span> </span>  <p>   <span style="font-family:&quot;Verdana&quot;,&quot;sans-serif&quot;;font-size:10pt;">Regards,<br />  Training &amp; Development</span>  </p>  <span> </span>' where mtp_type='ENROLLMENT_WAITLISTED';
update messageTemplate set mtp_content = '<p>   <span> </span><span style="font-family:&quot;Verdana&quot;,&quot;sans-serif&quot;;font-size:10pt;">Dear <span style="color:blue;">[Learner name]</span>,</span>  </p>  <p>   <span> </span><span style="font-family:&quot;Verdana&quot;,&quot;sans-serif&quot;;font-size:10pt;">Thank you for your interest in applying for the course </span><span style="color:blue;font-family:&quot;Verdana&quot;,&quot;sans-serif&quot;;font-size:10pt;">[Course name]([Course code]</span><span style="color:blue;font-family:&quot;Verdana&quot;,&quot;sans-serif&quot;;font-size:10pt;">)</span><span style="font-family:&quot;Verdana&quot;,&quot;sans-serif&quot;;font-size:10pt;">. We regret to inform you that your enrollment request is declined by </span><span style="color:blue;font-family:&quot;Verdana&quot;,&quot;sans-serif&quot;;font-size:10pt;">[Training Administrator]</span>  </p>  <p>   <span> </span><span style="font-family:&quot;Verdana&quot;,&quot;sans-serif&quot;;font-size:10pt;">Regards,<br />  Training &amp; Development</span>  </p>' where mtp_type='ENROLLMENT_NOT_CONFIRMED';
update messageTemplate set mtp_content = '<p>   <span style="font-family:&quot;Verdana&quot;,&quot;sans-serif&quot;;font-size:10pt;">Dear <span style="color:blue;">[Learner name]</span>,</span>  </p>  <span> </span>  <p>   <span style="font-family:&quot;Verdana&quot;,&quot;sans-serif&quot;;font-size:10pt;">We have received  your cancellation request of your enrollment to the course <span style="color:blue;">[Course name]([Course code])</span>. Your enrollment is  officially cancelled.</span>  </p>  <span> </span>  <p>   <span style="font-family:&quot;Verdana&quot;,&quot;sans-serif&quot;;font-size:10pt;">Regards,<br />  Training &amp; Development</span>  </p>' where mtp_type='ENROLLMENT_WITHDRAWAL_APPROVED';
update messageTemplate set mtp_content = '<p>   <span style="font-family:&quot;Verdana&quot;,&quot;sans-serif&quot;;font-size:10pt;">Dear <span style="color:blue;">[Learner name]</span>,</span>  </p>  <span> </span>  <p>   <span style="font-family:&quot;Verdana&quot;,&quot;sans-serif&quot;;font-size:10pt;">Thank you for your interest in applying for the course <span style="color:blue;">[Course name]([Course code])</span>. We regret to  inform you that your enrollment request is removed by the Training  Administrator. </span>  </p>  <span> </span>  <p>   <span style="font-family:&quot;Verdana&quot;,&quot;sans-serif&quot;;font-size:10pt;">Regards,<br />  Training &amp; Development</span>  </p>  <span> </span>' where mtp_type='ENROLLMENT_REMOVED';
update messageTemplate set mtp_content = '<p>   <span> </span><span style="font-family:&quot;Verdana&quot;,&quot;sans-serif&quot;;font-size:10pt;">Dear Sir/Madam,</span>  </p>  <p>   <span> </span><span style="color:blue;line-height:170%;font-family:&quot;Verdana&quot;,&quot;sans-serif&quot;;font-size:10pt;">[Learner name]</span><span style="line-height:170%;font-family:&quot;Verdana&quot;,&quot;sans-serif&quot;;font-size:10pt;"> is applying for the course </span><span style="color:blue;line-height:170%;font-family:&quot;Verdana&quot;,&quot;sans-serif&quot;;font-size:10pt;">[Course name]([Course code])</span><span style="line-height:170%;font-family:&quot;Verdana&quot;,&quot;sans-serif&quot;;font-size:10pt;">. Please approve/decline the  enrollment request by logging into the system using the link below:</span>  </p>  <p>   <span> </span><span style="color:blue;line-height:170%;font-family:&quot;Verdana&quot;,&quot;sans-serif&quot;;font-size:10pt;">[Link to login page]</span>  </p>  <p>   <span style="font-family:&quot;Verdana&quot;,&quot;sans-serif&quot;;font-size:10pt;">Regards,<br />  Training &amp; Development</span>  </p>' where mtp_type='ENROLLMENT_NEXT_APPROVERS';
update messageTemplate set mtp_content = '<p>   <span style="line-height:170%;font-family:&quot;Verdana&quot;,&quot;sans-serif&quot;;font-size:10pt;">Dear Sir/Madam,</span>  </p>  <p>   <span> </span><span style="line-height:170%;font-family:&quot;Verdana&quot;,&quot;sans-serif&quot;;font-size:10pt;">Please be informed that the  enrollment request made by to the course </span><span style="color:blue;line-height:170%;font-family:&quot;Verdana&quot;,&quot;sans-serif&quot;;font-size:10pt;">[Course name]([Course code])</span><span style="line-height:170%;font-family:&quot;Verdana&quot;,&quot;sans-serif&quot;;font-size:10pt;"> was removed by the training  administrator.</span>  </p>  <p>   <span> </span><span style="line-height:170%;font-family:&quot;Verdana&quot;,&quot;sans-serif&quot;;font-size:10pt;">Regards,<br />  Training &amp; Development</span>  </p>' where mtp_type='ENROLLMENT_REMOVED_REMINDER';
update messageTemplate set mtp_content = '<p>   <span> </span><span style="line-height:170%;font-family:&quot;Verdana&quot;,&quot;sans-serif&quot;;font-size:10pt;">Dear Sir/Madam,</span>  </p>  <p>   <span> </span><span style="line-height:170%;font-family:&quot;Verdana&quot;,&quot;sans-serif&quot;;font-size:10pt;">Please be informed that the  enrollment request made by <span style="color:blue;">[Learner name]</span> to  the course </span><span style="color:blue;line-height:170%;font-family:&quot;Verdana&quot;,&quot;sans-serif&quot;;font-size:10pt;">[Course  name] ([Course code])</span><span style="line-height:170%;font-family:&quot;Verdana&quot;,&quot;sans-serif&quot;;font-size:10pt;"> was cancelled by the learner.</span>  </p>  <p>   <span> </span><span style="font-family:&quot;Verdana&quot;,&quot;sans-serif&quot;;font-size:10pt;">Regards,<br />  Training &amp; Development</span>  </p>' where mtp_type='ENROLLMENT_WITHDRAWAL_REMINDER';
update messageTemplate set mtp_content = '<p>   <span id="__kindeditor_bookmark_start_0__"></span><span>&nbsp;</span><span style="font-family:&quot;Arial&quot;,&quot;sans-serif&quot;;font-size:11.5pt;">Dear Sir/Madam,</span>  </p>  <p>   <span>&nbsp;</span><span style="font-family:&quot;Arial&quot;,&quot;sans-serif&quot;;font-size:11.5pt;">Please be informed that the enrollment  request made by<span style="color:blue;"> [Learner Name] </span>to the course</span><span style="font-family:&quot;Verdana&quot;,&quot;sans-serif&quot;;font-size:10pt;"> <span style="color:blue;">[Course name] ([Course code])</span></span><span style="font-family:&quot;Arial&quot;,&quot;sans-serif&quot;;font-size:11.5pt;"> was  confirmed by the training administrator.</span>  </p>  <p>   <span> </span><span style="font-family:&quot;Arial&quot;,&quot;sans-serif&quot;;font-size:11.5pt;">Regards,<br />  Training &amp; Development</span>  </p>' where mtp_type='ENROLLMENT_CONFIRMED_REMINDER';
update messageTemplate set mtp_content = '<p>   <span> </span><span style="font-family:&quot;Verdana&quot;,&quot;sans-serif&quot;;font-size:10pt;">Dear Sir/Madam,</span>  </p>  <p>   <span> </span><span style="font-family:&quot;Verdana&quot;,&quot;sans-serif&quot;;font-size:10pt;">Please be informed that the enrollment request made  by<span style="color:blue;"> [Learner Name] </span>to the course <span style="color:blue;">[Course name] ([Course code])</span> was approved by <span style="color:blue;">[Approval Name]</span>.</span>  </p>  <p>   <span> </span><span style="font-family:&quot;Verdana&quot;,&quot;sans-serif&quot;;font-size:10pt;">Regards,<br />  Training &amp; Development</span>  </p>' where mtp_type='ENROLLMENT_APPROVED_REMINDER';
update messageTemplate set mtp_content = '<p>   <span> </span><span style="font-family:&quot;Verdana&quot;,&quot;sans-serif&quot;;font-size:10pt;">Dear Sir/Madam,</span>  </p>  <p>   <span> </span><span style="color:blue;line-height:170%;font-family:&quot;Verdana&quot;,&quot;sans-serif&quot;;font-size:10pt;">[Learner name]</span><span style="line-height:170%;font-family:&quot;Verdana&quot;,&quot;sans-serif&quot;;font-size:10pt;"> is applying for the course </span><span style="color:blue;line-height:170%;font-family:&quot;Verdana&quot;,&quot;sans-serif&quot;;font-size:10pt;">[Course name]([Course code])</span><span style="line-height:170%;font-family:&quot;Verdana&quot;,&quot;sans-serif&quot;;font-size:10pt;">. However, the learner does  not have the supervisor for approval as required by the course and the approval  is routed to you instead. Please approve/decline the enrollment request by  logging into the system using the link below:</span>  </p>  <p>   <span> </span><span style="color:blue;font-family:&quot;Verdana&quot;,&quot;sans-serif&quot;;font-size:10pt;">[Link to login page]</span>  </p>  <p>   <span> </span><span style="font-family:&quot;Verdana&quot;,&quot;sans-serif&quot;;font-size:10pt;">Regards,<br />  Training &amp; Development</span>  </p>' where mtp_type='ENROLLMENT_NO_SUPERVISOR';
update messageTemplate set mtp_content = '<p>   <span style="font-family:&quot;font-size:10pt;">Dear <span style="color:blue;"><span style="color:#0000FF;font-family:Verdana, Geneva, Arial, Helvetica, sans-serif;line-height:normal;">[Learner name]</span></span>, </span>   </p>  <p>   <span> </span><span style="font-family:&quot;font-size:10pt;">We would like to remind you that you are obliged to  complete the course <span style="color:blue;">[Course name]([Course code])</span> by <span style="color:blue;">[Course period end date]</span>. </span>   </p>  <p>   <span> </span><span style="font-family:&quot;font-size:10pt;">Regards.<br />  Training and Development</span>   </p>' where mtp_type='COURSE_NOTIFY';
update messageTemplate set mtp_content = '<p>   <span> </span><span style="font-family:&quot;font-size:10pt;">Dear all, <br />  <br />  I am pleased to offer you a place in our In-house Training Course, <span style="color:blue;">[Course name]</span>: <span style="color:blue;">[Class name]</span>,  scheduled for <span style="color:blue;">[Course period start date]</span> to <span style="color:blue;">[Course period end date]</span>.<br />  <br />  You will be required to complete the assigned activities/exercises, submit the  course assignments and/or attend examinations, if any, through the E-learning  portal Link. By the end of the course, you should complete and return an online course  evaluation to us through the portal. A Certificate of Completion will be issued  to you if you satisfy all the requirements including passing of the  assignments/examination. To recognize your effort, information on your  attendance and/or assessment results will be reported to your head after the  Course. <br />  <br />  Kindly let me know in advance if you are unable to attend the above course. <br />  <br />  The names of participants are listed below for your reference:- <br />  1. <span style="color:blue;">[Learner(s)]</span><br />  <br />  <br />  <span style="color:blue;">[Training Administrator]</span><br />  Training Team<br />  Human Resources Unit</span>   </p>  <p>   <span> <br />  </span>   </p>' where mtp_type='JI';
update messageTemplate set mtp_content = '<p>   <span> </span><span style="font-family:&quot;font-size:10pt;">Dear all, <br />  <br />  I am pleased to offer you a place in our In-house Training Course, <span style="color:blue;">[Course name]</span>: <span style="color:blue;">[Class name]</span>,  scheduled for <span style="color:blue;">[Course period start date]</span> to <span style="color:blue;">[Course period end date]</span>.<br />  <br />  You will be required to complete the assigned activities/exercises, submit the  course assignments and/or attend examinations, if any, through the E-learning  portal Link. By the end of the course, you should complete and return an online course  evaluation to us through the portal. A Certificate of Completion will be issued  to you if you satisfy all the requirements including passing of the  assignments/examination. To recognize your effort, information on your  attendance and/or assessment results will be reported to your head after the  Course. <br />  <br />  Kindly let me know in advance if you are unable to attend the above course. <br />  <br />  The names of participants are listed below for your reference:- <br />  1. <span style="color:blue;">[Learner(s)]</span><br />  <br />  <br />  <span style="color:blue;">[Training Administrator]</span><br />  Training Team<br />  Human Resources Unit</span>   </p>' where mtp_type='REMINDER';
update messageTemplate set mtp_content = '<p>   <span> </span><span style="color:black;font-family:宋体;font-size:11pt;">Dear </span><span style="color:blue;font-family:宋体;font-size:11pt;">[Learner name]</span><span style="color:black;font-family:宋体;font-size:11pt;">,<br />  <br />  We are happy to provide you with a user name and temporary password that will  allow you to enroll for courses.<br />  Your User Name:</span><span style="color:blue;font-family:宋体;font-size:11pt;">[Learner name]</span><span style="color:black;font-family:宋体;font-size:11pt;"><br />  Your Password:</span><span style="color:blue;font-family:宋体;font-size:11pt;">[Learner password]<br />  <br />  [Link to login page]<br />  </span><span style="font-family:宋体;font-size:11pt;">Regards,<br />  Training &amp; Development</span>  </p>' where mtp_type='USR_REG_APPROVE';
update messageTemplate set mtp_content = '<p>   <span> </span><span style="color:black;font-family:宋体;font-size:11pt;">Dear </span><span style="color:blue;font-family:宋体;font-size:11pt;">[Learner name]</span><span style="color:black;font-family:宋体;font-size:11pt;">,<br />  <br />  Please be informed that your student account registration has been declined.  The reason given by your local faculty is as follows:</span><span style="color:blue;font-family:宋体;font-size:11pt;">[Refuse reason]<br />  <br />  </span><span style="color:black;font-family:宋体;font-size:11pt;">If you have any questions, please contact  your local faculty.</span><span style="color:blue;font-family:宋体;font-size:11pt;"><br />  <br />  </span><span style="color:black;font-family:宋体;font-size:11pt;">Regards,<br />  Training &amp; Development</span>   </p>' where mtp_type='USR_REG_DISAPPROVE';
update messageTemplate set mtp_content = '<p>   <span style="color:black;font-family:宋体;font-size:11pt;">Dear </span><span style="color:blue;font-family:宋体;font-size:11pt;">[User name]</span><span style="color:black;font-family:宋体;font-size:11pt;">,<br />  <br />  This email is to notify you that the import of user profile file </span><span style="color:blue;font-family:宋体;font-size:11pt;">[Src file]</span><span style="color:black;font-family:宋体;font-size:11pt;">, which you uploaded at </span><span style="color:blue;font-family:宋体;font-size:11pt;">[Import start date]</span><span style="color:black;font-family:宋体;font-size:11pt;">, has been completed.<br />  <br />  Success Entries : </span><span style="color:blue;font-family:宋体;font-size:11pt;">[Success total]</span><span style="color:black;font-family:宋体;font-size:11pt;"><br />  Unsuccessful Entries : </span><span style="color:blue;font-family:宋体;font-size:11pt;">[Unsuccess total]<br />  </span><span style="color:black;font-family:宋体;font-size:11pt;"><br />  The import process was completed at </span><span style="color:blue;font-family:宋体;font-size:11pt;">[Import end date]. </span><span style="font-family:宋体;font-size:11pt;">You  may login to the system to view the log file(s) in the import history. <br />  Thank you for your attention.<br />  <br />  Regards,<br />  Training &amp; Development</span>  </p>' where mtp_type='USER_IMPORT_SUCCESS';
update messageTemplate set mtp_content = '<p>   <span> </span><span style="color:black;font-family:宋体;font-size:11pt;">Dear </span><span style="color:blue;font-family:宋体;font-size:11pt;">[User name]</span><span style="color:black;font-family:宋体;font-size:11pt;">,<br />  <br />  This email is to notify you that the import of enrollment record </span><span style="color:blue;font-family:宋体;font-size:11pt;">[Src file]</span><span style="color:black;font-family:宋体;font-size:11pt;">, which you uploaded at </span><span style="color:blue;font-family:宋体;font-size:11pt;">[Import start date]</span><span style="color:black;font-family:宋体;font-size:11pt;">, has been completed.<br />  <br />  Success Entries : </span><span style="color:blue;font-family:宋体;font-size:11pt;">[Success total]</span><span style="color:black;font-family:宋体;font-size:11pt;"><br />  Unsuccessful Entries : </span><span style="color:blue;font-family:宋体;font-size:11pt;">[Unsuccess  total]<br />  </span><span style="color:black;font-family:宋体;font-size:11pt;"><br />  The import process was completed at </span><span style="color:blue;font-family:宋体;font-size:11pt;">[Import  end date]. </span><span style="font-family:宋体;font-size:11pt;">You may login to the system to  view the log file(s) in the import history. <br />  Thank you for your attention.<br />  <br />  Regards,<br />  Training &amp; Development</span>  </p>' where mtp_type='ENROLLMENT_IMPORT_SUCCESS';
update messageTemplate set mtp_content = '<span><span style="color:black;font-family:宋体;font-size:11pt;">当前活动用户量<span> : </span></span><span style="color:blue;font-family:宋体;font-size:11pt;">[Active user]<br />  </span><span style="color:black;font-family:宋体;font-size:11pt;">Performance warning level : </span><span style="color:blue;font-family:宋体;font-size:11pt;">[Warning user]<br />  </span><span style="font-family:宋体;font-size:11pt;">最大活动用户数 :<span>&nbsp;<span style="color:#0000FF;font-family:Verdana, Geneva, Arial, Helvetica, sans-serif;line-height:normal;"><span style="color:#0000FF;font-family:宋体;font-size:15px;line-height:22px;">[Blocking user]</span></span></span></span><span style="color:black;font-family:宋体;font-size:11pt;"><br />  </span><span style="color:black;font-family:宋体;font-size:11pt;">发生时间<span> : </span></span><span style="color:#0000CC;font-family:宋体;font-size:11pt;"><span style="color:#0000FF;font-family:宋体;font-size:15px;line-height:22px;">[Warning date]</span></span></span>   <p>   <br />  </p>' where mtp_type='SYS_PERFORMANCE_NOTIFY';
update messageTemplate set mtp_content = '亲爱的 先生/女士:<br />  <br />  你的“忘记密码”的要求已在<span style="color:#0000FF;font-family:Verdana, Geneva, Arial, Helvetica, sans-serif;line-height:normal;">[Request time]</span>接收。<br />  <p>   请点击以下链接重置你的密码：  </p>  <p>   <span style="color:#0000FF;font-family:Verdana, Geneva, Arial, Helvetica, sans-serif;line-height:normal;">[Link to reset password]</span><span style="line-height:1.5;">，此链接将在</span><span style="color:#0000FF;font-family:Verdana, Geneva, Arial, Helvetica, sans-serif;line-height:normal;">[Max days]</span><span style="line-height:1.5;">天后失效。</span>  </p>' where mtp_type='USER_PWD_RESET_NOTIFY';


/**
drop原邮件模板及邮件相关表
*/
drop table mgRecHistory;
drop table mgRecipient;
drop table mgxslParamValue;
drop table xslmgSelectedTemplate;
drop table cmAssessmentNotify; --有数据
drop table mgtnaSelectedMessage;
drop table mgitmSelectedMessage;
drop table mgmessage;
drop table xslParamName;
drop table xslTemplate;



/*
Auth: joe
Date: 2014-10-23
Desc:SNS 群组管理
*/
insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('SMS_GROUP_MANAGE', 'FUNCTION', 'HOMEPAGE', sysdate, null);

insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp)
select rol_id, ftn_id, sysdate from acRole, acFunction
where rol_ext_id like 'TADM_%' and ftn_ext_id = 'SMS_GROUP_MANAGE';

insert into acHomePage (ac_hom_rol_ext_id,ac_hom_ftn_ext_id, ac_hom_create_usr_id, ac_hom_create_timestamp)
select rol_ext_id, 'SMS_GROUP_MANAGE', 's1u3', sysdate from acRole where rol_ext_id like 'TADM_%';



/**
 * 赞的统计，区分是不是评论的赞
*/
alter table sns_count add s_cnt_is_comment int

/*
Auth: myron
Date: 2014-10-30
Desc: 企业管理
*/
CREATE TABLE EnterpriseInfoPortal(
  eip_id int NOT NULL , 
  eip_code nvarchar2(50) NOT NULL,
  eip_name nvarchar2(255) NOT NULL, 
  eip_tcr_id int NOT NULL,   
  eip_account_num int NOT NULL,  
  eip_status nvarchar2(10) NOT NULL,  
  eip_domain nvarchar2(50) NULL,    
  eip_login_bg nvarchar2(50) NULL,  
  eip_create_timestamp timestamp NOT NULL,  
  eip_create_usr_id nvarchar2(20) NOT NULL, 
  eip_update_timestamp timestamp NOT NULL, 
  eip_update_usr_id nvarchar2(20) NOT NULL  
)
create sequence EnterpriseInfoPortal_seq;
CREATE OR REPLACE TRIGGER EnterpriseInfoPortal_trigger BEFORE INSERT ON EnterpriseInfoPortal
FOR EACH ROW
WHEN (new.eip_id IS NULL)
BEGIN
 SELECT EnterpriseInfoPortal_seq.NEXTVAL INTO :new.eip_id FROM DUAL;
END;

ALTER TABLE EnterpriseInfoPortal ADD CONSTRAINT
  PK_EnterpriseInfoPortal PRIMARY KEY
  (
  eip_id
  );
ALTER TABLE EnterpriseInfoPortal ADD CONSTRAINT 
  FK_EIP_TC FOREIGN KEY
  (
    eip_tcr_id
  ) REFERENCES tcTrainingCenter 
  (
    tcr_id
  );

insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('EIP_MAIN', 'FUNCTION', 'HOMEPAGE', sysdate, null); 

insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp)
select rol_id, ftn_id,sysdate from acRole, acFunction
where rol_ext_id like 'ADM_%' and ftn_ext_id = 'EIP_MAIN';

insert into acHomePage (ac_hom_rol_ext_id,ac_hom_ftn_ext_id, ac_hom_create_usr_id, ac_hom_create_timestamp)
select rol_ext_id, 'EIP_MAIN', 's1u3', sysdate from acRole where rol_ext_id like 'ADM_%';

/*
Auth: myron
Date: 2014-10-31
Desc: 移动课程背景
*/
alter table EnterpriseInfoPortal add eip_mobile_login_bg nvarchar2(255) null;

/**
 * 删除企业信息表中关于登陆图片的字段
 */
alter table EnterpriseInfoPortal drop column eip_mobile_login_bg;
alter table EnterpriseInfoPortal drop column eip_login_bg;

/**
初始化邮件模板邮件内容
*/
update messageTemplate set mtp_content = '<p>   <span id="__kindeditor_bookmark_start_0__"></span><span>&nbsp;</span><span style="font-family:&quot;Arial&quot;,&quot;sans-serif&quot;;font-size:11.5pt;">Dear Sir/Madam,</span>  </p>  <p>   <span>&nbsp;</span><span style="font-family:&quot;Arial&quot;,&quot;sans-serif&quot;;font-size:11.5pt;">Please be informed that the enrollment  request made by<span style="color:blue;"> [Learner name] </span>to the course</span><span style="font-family:&quot;Verdana&quot;,&quot;sans-serif&quot;;font-size:10pt;"> <span style="color:blue;">[Course name] ([Course code])</span></span><span style="font-family:&quot;Arial&quot;,&quot;sans-serif&quot;;font-size:11.5pt;"> was  confirmed by the training administrator.</span>  </p>  <p>   <span> </span><span style="font-family:&quot;Arial&quot;,&quot;sans-serif&quot;;font-size:11.5pt;">Regards,<br />  Training &amp; Development</span>  </p>' where mtp_type='ENROLLMENT_CONFIRMED_REMINDER';
update messageTemplate set mtp_content = '<p>   <span> </span><span style="font-family:&quot;Verdana&quot;,&quot;sans-serif&quot;;font-size:10pt;">Dear Sir/Madam,</span>  </p>  <p>   <span> </span><span style="font-family:&quot;Verdana&quot;,&quot;sans-serif&quot;;font-size:10pt;">Please be informed that the enrollment request made  by<span style="color:blue;"> [Learner name] </span>to the course <span style="color:blue;">[Course name] ([Course code])</span> was approved by <span style="color:blue;">[Approval Name]</span>.</span>  </p>  <p>   <span> </span><span style="font-family:&quot;Verdana&quot;,&quot;sans-serif&quot;;font-size:10pt;">Regards,<br />  Training &amp; Development</span>  </p>' where mtp_type='ENROLLMENT_APPROVED_REMINDER';
/*
Auth: myron
Date: 2014-11-04
Desc:登录背景图片添加
*/
alter table SitePoster add login_bg_file1 nvarchar2(255) null;
alter table SitePoster add login_bg_file2 nvarchar2(255) null;
alter table SitePoster add login_bg_file3 nvarchar2(255) null;
alter table SitePoster add login_bg_file4 nvarchar2(255) null;
alter table SitePoster add login_bg_file5 nvarchar2(255) null;
/*
 * 修改默认logo
 */
update siteposter set sp_logo_file_cn='wizbang.png',sp_logo_file_hk='wizbang.png',sp_logo_file_us='wizbang.png',login_bg_file1='banner01.jpg',login_bg_file2='banner02.jpg',login_bg_file3='banner03.jpg' where sp_ste_id='1' and sp_mobile_ind='0'

/*
Auth: myron
Date: 2014-11-10
Desc: 增加课程指派功能
*/
insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('COURSE_MAIN', 'FUNCTION', 'HOMEPAGE', sysdate, null); 

insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp)
select rol_id, ftn_id,sysdate from acRole, acFunction
where rol_ext_id like 'ADM_%' and ftn_ext_id = 'COURSE_MAIN';

insert into acHomePage (ac_hom_rol_ext_id,ac_hom_ftn_ext_id, ac_hom_create_usr_id, ac_hom_create_timestamp)
select rol_ext_id, 'COURSE_MAIN', 's1u3', sysdate from acRole where rol_ext_id like 'ADM_%';

/*
Auth: myron
Date: 2014-11-10
Desc: 限制"视频点播"模块是否允许下载属性是否可以修改:, 1 为禁止 , 0 为允许; 即从顶级培训中心复制下来的不允许修改
*/
ALTER TABLE module ADD mod_copy_ind INTEGER default 0 not null;

/*
 *Auth: myron
 *Date: 2014-11-12
 *Desc: 删除单点链接查询和友情链接功能 
 */
delete from acRoleFunction where rfn_ftn_id in (select ftn_id from acfunction where ftn_ext_id='SSO_LINK_QUERY' or ftn_ext_id='FS_LINK_MAIN');
delete from achomepage where ac_hom_ftn_ext_id='SSO_LINK_QUERY' or ac_hom_ftn_ext_id='FS_LINK_MAIN';
delete from acfunction where ftn_ext_id='SSO_LINK_QUERY' or ftn_ext_id='FS_LINK_MAIN';


/*
Auth: Jimmy
Date: 2014-11-18
Desc: 增加物理删除用户信息记录表
*/

create table DeletedUserInfo (
  dui_usr_ent_id int NOT NULL ,
  dui_usr_ste_usr_id nvarchar2(255) NOT NULL ,
  dui_usr_display_bil nvarchar2(255) NOT NULL ,
  dui_delete_timestamp timestamp NOT NULL 
);


/*
Auth: lance
Date: 2014-11-14
Desc: 修改邮件模版连接
*/
update messagetemplate set mtp_content_email_link = '/login';
update messagetemplate set mtp_content_email_link = '/app/user/userResetPwd/' where mtp_type = 'USER_PWD_RESET_NOTIFY';
update messagetemplate set mtp_content_pc_link = '/app/course/signup' where mtp_type = 'ENROLLMENT_CONFIRMED';
update messagetemplate set mtp_content_pc_link = '/app/subordinate/subordinateApproval' where mtp_type = 'ENROLLMENT_NEXT_APPROVERS';
update messagetemplate set mtp_content_pc_link = '/app/subordinate/subordinateApproval' where mtp_type = 'ENROLLMENT_NO_SUPERVISOR';

/* r49228 end*/


/**
 * 赞，区分是不是评论的赞
*/
alter table sns_valuation_log add s_vtl_is_comment int
alter table sns_valuation add s_vlt_is_comment int

/**
Auth: lance	
Date: 2014-12-1
Desc: 字段类型修改，修复中文乱码问题
 */
alter table article modify art_title nvarchar2(255);
alter table article modify art_introduction  nvarchar2(2000);
alter table article modify art_keywords  nvarchar2(255);
alter table article modify art_location  nvarchar2(255);
alter table article modify art_type  nvarchar2(10);
alter table articleType modify aty_title  nvarchar2(255);

 
 /**
 删除评论的时候删除赞，统计，通知，回复
 */
  create or replace trigger delCommentRel
  after delete on sns_comment
  for each row
       declare   
     PRAGMA AUTONOMOUS_TRANSACTION; 
  begin
      delete from sns_comment where s_cmt_target_id = :old.s_cmt_id or s_cmt_reply_to_id = :old.s_cmt_id;
      delete from sns_doing where (:old.s_cmt_id = s_doi_reply_id or :old.s_cmt_id = s_doi_act_id ) and s_doi_target_type = '1' and s_doi_module = :old.s_cmt_module;
      delete from sns_doing where :old.s_cmt_id = s_doi_target_id and s_doi_target_type = '1' and s_doi_module = 'Comment';
      delete from sns_valuation_log where :old.s_cmt_id = s_vtl_target_id and s_vtl_module = :old.s_cmt_module;
      delete from sns_count where :old.s_cmt_id = s_cnt_target_id and s_cnt_module = :old.s_cmt_module;
      commit;
  end;


/**
  删除动态的时候删除附带的评论，赞一记统计信息
*/
create or replace trigger delDoingRel
 after delete on  sns_doing
  for each row
     declare   
     PRAGMA AUTONOMOUS_TRANSACTION;
     isNotice int;  
  begin
  isNotice:=:old.s_doi_target_type;
    if isNotice < 1 then
       delete from sns_doing
       where :old.s_doi_id = s_doi_target_id;
       delete from sns_doing
       where s_doi_target_id = :old.s_doi_id and s_doi_target_type = '1';
       delete from sns_valuation_log
	     where s_vtl_target_id = :old.s_doi_id and s_vtl_module = :old.s_doi_module;
	     delete from sns_count
	     where :old.s_doi_id = s_cnt_target_id and s_cnt_module = :old.s_doi_module;
       commit;
    end if;
  end;
  
    /**
   Auth: joe    
   Date: 2014-12-5
   Desc:文章图示类型
   */
  alter table article add art_icon_file nvarchar2 (100);
  
  
  /**
   Auth: joe
   Date: 2014-12-10
   Desc: 公开课视频记录表 
   */
  create table PublicCourseRecord
  (
    pcr_usr_ent_id int not null,
    pcr_itm_id int not null,
    pcr_mod_id int not null,
    pcr_duration int null,
    pcr_last_acc timestamp null,
    pcr_note nclob null
  );
  
  ALTER TABLE PublicCourseRecord ADD CONSTRAINT
  PK_PublicCourseRecord PRIMARY KEY 
  (
   pcr_usr_ent_id,pcr_itm_id,pcr_mod_id
	);
	
	
	  /*
   Auth: myron
   Date: 2014-12-15
   Desc: 文章中添加访问次数字段 
   */
  alter table article add art_access_count int default 0;
  
  
  
/*
Auth: elvea
Date: 2014-01-05
Desc: 标签管理
*/
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('TAG_MGT', 'FUNCTION', 'HOMEPAGE', sysdate, null);

INSERT INTO acRoleFunction
select rol_id, ftn_id, 'slu3', sysdate from acRole, acFunction where rol_ste_uid in ('TADM')
and ftn_ext_id = 'TAG_MGT';

insert into acHomePage (ac_hom_rol_ext_id,ac_hom_ftn_ext_id, ac_hom_create_usr_id, ac_hom_create_timestamp)
select rol_ext_id, 'TAG_MGT', 's1u3', sysdate from acRole where rol_ste_uid in ('TADM');
commit;



/* 标签*/
create table Tag (
  tag_id int not null ,
  tag_title nvarchar2(255),
  tag_tcr_id int,
  tag_create_datetime timestamp,
  tag_create_user_id nvarchar2(50),
  tag_update_datetime timestamp,
  tag_update_user_id nvarchar2(50)
);
create sequence Tag_seq;
CREATE OR REPLACE TRIGGER Tag_trigger BEFORE INSERT ON Tag
FOR EACH ROW
WHEN (new.tag_id IS NULL)
BEGIN
 SELECT Tag_seq.NEXTVAL INTO :new.tag_id FROM DUAL;
END;

ALTER TABLE Tag ADD CONSTRAINT
  PK_Tag PRIMARY KEY 
  (
  tag_id
	);
	
/*
Auth: elvea
Date: 2014-01-05
Desc: 知识库管理
*/
delete from acfunction where ftn_ext_id = 'KB_MGT';
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('KB_MGT', 'FUNCTION', 'HOMEPAGE', sysdate, null);

INSERT INTO acRoleFunction
select rol_id, ftn_id, 'slu3', sysdate from acRole, acFunction where rol_ste_uid in ('TADM')
and ftn_ext_id = 'KB_MGT';

insert into acHomePage (ac_hom_rol_ext_id,ac_hom_ftn_ext_id, ac_hom_create_usr_id, ac_hom_create_timestamp)
select rol_ext_id, 'KB_MGT', 's1u3', sysdate from acRole where rol_ste_uid in ('TADM');
commit;



/* 知识中心 - 目录 */
create table kb_catalog (
  kbc_id int not null,
  kbc_title nvarchar2(255),
  kbc_desc nvarchar2(255),
  kbc_status nvarchar2(255),
  kbc_tcr_id int,
  kbc_create_datetime timestamp,
  kbc_create_user_id nvarchar2(50),
  kbc_update_datetime timestamp,
  kbc_update_user_id nvarchar2(50)
);

create sequence kb_catalog_seq;
CREATE OR REPLACE TRIGGER kb_catalog_trigger BEFORE INSERT ON kb_catalog
FOR EACH ROW
WHEN (new.kbc_id IS NULL)
BEGIN
 SELECT kb_catalog_seq.NEXTVAL INTO :new.kbc_id FROM DUAL;
END;

ALTER TABLE kb_catalog ADD CONSTRAINT
  PK_kb_catalog PRIMARY KEY 
  (
  kbc_id
	);
	

/* 知识中心 - 知识 */
create table kb_item (
  kbi_id int not null,
  kbi_title nvarchar2(255),
  kbi_image nvarchar2(255),
  kbi_desc nvarchar2(255),
  kbi_content nclob,
  kbi_type nvarchar2(255),
  kbi_status nvarchar2(255),
  kbi_app_status nvarchar2(255),
  kbi_approve_datetime timestamp,
  kbi_approve_user_id nvarchar2(50),
  kbi_publish_datetime timestamp,
  kbi_publish_user_id nvarchar2(50),
  kbi_create_datetime timestamp,
  kbi_create_user_id nvarchar2(50),
  kbi_update_datetime timestamp,
  kbi_update_user_id nvarchar2(50)
);


create sequence kb_item_seq;
CREATE OR REPLACE TRIGGER kb_item_trigger BEFORE INSERT ON kb_item
FOR EACH ROW
WHEN (new.kbi_id IS NULL)
BEGIN
 SELECT kb_item_seq.NEXTVAL INTO :new.kbi_id FROM DUAL;
END;

ALTER TABLE kb_item ADD CONSTRAINT
  PK_kb_item PRIMARY KEY 
  (
  kbi_id
	);
	

/* 知识中心 - 附件 */
create table kb_attachment (
  kba_id int not null ,
  kba_filename nvarchar2(255),
  kba_file nvarchar2(255),
  kba_remark nvarchar2(255),
  kba_create_datetime timestamp,
  kba_create_user_id nvarchar2(50),
  kba_update_datetime timestamp,
  kba_update_user_id nvarchar2(50)
);


create sequence kb_attachment_seq;
CREATE OR REPLACE TRIGGER kb_attachment_trigger BEFORE INSERT ON kb_attachment
FOR EACH ROW
WHEN (new.kba_id IS NULL)
BEGIN
 SELECT kb_attachment_seq.NEXTVAL INTO :new.kba_id FROM DUAL;
END;

ALTER TABLE kb_attachment ADD CONSTRAINT
  PKkb_attachment PRIMARY KEY 
  (
  kba_id
	);
	
/* 知识中心 - 知识和附件关联 */
create table kb_item_attachment (
  kia_kbi_id int not null,
  kia_kba_id int not null,
  kia_create_datetime timestamp,
  kia_create_user_id nvarchar2(50)
);
ALTER TABLE kb_item_attachment ADD CONSTRAINT
  PK_kb_item_attachment PRIMARY KEY 
  (
  kia_kbi_id, kia_kba_id
	);
	
	
/* 知识中心 - 知识和目录关联 */
create table kb_item_cat (
  kic_kbi_id int not null,
  kic_kbc_id int not null,
  kic_create_datetime timestamp,
  kic_create_user_id nvarchar2(50),
  constraint PK_KB_KIC_ID primary key (kic_kbi_id, kic_kbc_id)
);

/* 知识中心 - 知识和标签关联 */
create table kb_item_tag (
  kit_kbi_id int not null,
  kit_tag_id int not null,
  kit_create_datetime timestamp,
  kit_create_user_id nvarchar2(50),
  constraint PK_KB_KIT_ID primary key (kit_kbi_id, kit_tag_id)
);



/*
drop table kb_item_attachment;
drop table kb_item_tag;
drop table kb_item_cat;
drop table kb_attachment;
drop table kb_item;
drop table kb_catalog;
drop table tag;
*/


/*
 微信表跟apitoken合并
 *
*/
alter table apitoken add atk_wechat_open_id nvarchar2(100);



/*
Auth: elvea
Date: 2014-01-05
Desc: 删除标签管理功能菜单
*/
delete from acHomePage where ac_hom_ftn_ext_id = 'TAG_MGT';
delete from acRoleFunction where rfn_ftn_id = (select ftn_id  from acfunction where ftn_ext_id = 'TAG_MGT');
delete from acfunction where ftn_ext_id = 'TAG_MGT';
drop table kb_item_view;

/* 知识中心 - 知识浏览表 */
create table kb_item_view (
	kiv_usr_ent_id int not null,
	kiv_kbi_id int not null,
	kiv_create_datetime timestamp,
	kiv_update_datetime timestamp,
	constraint PK_KB_KIV_ID primary key (kiv_usr_ent_id, kiv_kbi_id)
);




/**
 * 增加管理员端知识管理菜单
 */
insert into acRoleFunction select rol_id,ftn_id,'slu3',sysDate from acRole,acFunction where rol_ste_uid in ('SADM') and ftn_ext_id='KB_MGT';
insert into acHomePage select null,rol_ext_id,'KB_MGT','s1u3',sysDate from acRole where rol_ste_uid in ('SADM');



/**
 * 宣传栏增加手机端app启动图片显示
 */
alter table siteposter add guide_file1 nvarchar2(255);
alter table siteposter add guide_file2 nvarchar2(255);
alter table siteposter add guide_file3 nvarchar2(255);
GO
/*
 * 知识表添加浏览量字段
 */
alter table kb_item add kbi_access_count int
GO

/*
 * 系统配置
 */
insert into SystemSetting values ('SYS_LDAP_URL', 'ldap://host:port', sysdate, 's1u3', sysdate, 's1u3' );
insert into SystemSetting values ('SYS_LDAP_SUFFIX', '@cwngz.local', sysdate, 's1u3', sysdate, 's1u3' );

insert into SystemSetting values ('SYS_OPENOFFICE_ENABLED', 'true', sysdate, 's1u3', sysdate, 's1u3' );
insert into SystemSetting values ('SYS_OPENOFFICE_ENVIRONMENT', 'Windows', sysdate, 's1u3', sysdate, 's1u3' );
insert into SystemSetting values ('SYS_OPENOFFICE_PATH', 'C:/Program Files (x86)/OpenOffice 4/program/soffice', sysdate, 's1u3', sysdate, 's1u3' );
insert into SystemSetting values ('SYS_OPENOFFICE_HOST', 'localhost', sysdate, 's1u3', sysdate, 's1u3' );
insert into SystemSetting values ('SYS_OPENOFFICE_PORT', '8100', sysdate, 's1u3', sysdate, 's1u3' );

insert into SystemSetting values ('SYS_MAIL_SERVER_HOST', 'host', sysdate, 's1u3', sysdate, 's1u3' );
insert into SystemSetting values ('SYS_MAIL_SERVER_AUTH_ENABLED', 'false', sysdate, 's1u3', sysdate, 's1u3' );
insert into SystemSetting values ('SYS_MAIL_SERVER_ACCOUNT_TYPE', 'EMAIL', sysdate, 's1u3', sysdate, 's1u3' );
insert into SystemSetting values ('SYS_MAIL_SERVER_USER', 'user', sysdate, 's1u3', sysdate, 's1u3' );
insert into SystemSetting values ('SYS_MAIL_SERVER_PASSWORD', 'pass', sysdate, 's1u3', sysdate, 's1u3' );

insert into SystemSetting values ('SYS_MAIL_SCHEDULER_DOMAIN', 'http://localhost:8081/', sysdate, 's1u3', sysdate, 's1u3' );

insert into SystemSetting values ('SYS_WECHAT_DOMAIN', 'http://121.8.157.22', sysdate, 's1u3', sysdate, 's1u3' );
insert into SystemSetting values ('SYS_WECHAT_MOBILE_DOMAIN', 'http://121.8.157.22/mobile/', sysdate, 's1u3', sysdate, 's1u3' );
insert into SystemSetting values ('SYS_WECHAT_SERVER_ID', 'offline', sysdate, 's1u3', sysdate, 's1u3' );
insert into SystemSetting values ('SYS_WECHAT_PORT', '3', sysdate, 's1u3', sysdate, 's1u3' );
insert into SystemSetting values ('SYS_WECHAT_MAX_MESSAGE', '80', sysdate, 's1u3', sysdate, 's1u3' );
insert into SystemSetting values ('SYS_WECHAT_TRIAL_ACCOUNT_ENABLED', 'false', sysdate, 's1u3', sysdate, 's1u3' );

/*
 * mobile端站内信连接修改
 */
update MessageTemplate set mtp_content_mobile_link = 'javascript:clicked(''../approval/approval.html'',true)' where mtp_type = 'ENROLLMENT_NO_SUPERVISOR';
update MessageTemplate set mtp_content_mobile_link = 'javascript:clicked(''../approval/approval.html'',true)' where mtp_type = 'ENROLLMENT_NEXT_APPROVERS';
update MessageTemplate set mtp_content_mobile_link = 'javascript:clicked(''../course/signup.html'',true)' where mtp_type = 'ENROLLMENT_CONFIRMED';

/**
 * 2015-2-6
 * 整理oracle环境知识中心所缺sql
 */
alter table tag add constraint FK_TAG_TCR_ID foreign key (tag_tcr_id) references tcTrainingCenter (tcr_id);
alter table kb_catalog add constraint FK_KB_KBC_TCR_ID foreign key (kbc_tcr_id) references tcTrainingCenter (tcr_id);
alter table kb_item_attachment add constraint PK_KB_KIA_KBI_ID foreign key (kia_kbi_id) references kb_item (kbi_id);
alter table kb_item_attachment add constraint PK_KB_KIA_KBA_ID foreign key (kia_kba_id) references kb_attachment (kba_id);
alter table kb_item_cat add constraint FK_KB_KIC_KBI_ID foreign key (kic_kbi_id) references kb_item (kbi_id);
alter table kb_item_cat add constraint FK_KB_KIC_KBC_ID foreign key (kic_kbc_id) references kb_catalog (kbc_id);
alter table kb_item_tag add constraint FK_KB_KIT_KBI_ID foreign key (kit_kbi_id) references kb_item (kbi_id);
alter table kb_item_tag add constraint FK_KB_KIT_TAG_ID foreign key (kit_tag_id) references tag (tag_id);
alter table kb_item_view add constraint FK_KB_KIV_KBI_ID foreign key (kiv_kbi_id) references kb_item (kbi_id);
alter table kb_item add kbi_download nvarchar2(50);
alter table kb_item add kbi_download_count int;

/**
 * 评论删除时删除所产生的动态
 */
create or replace trigger delCommentRel
  after delete on sns_comment
  for each row
       declare
     PRAGMA AUTONOMOUS_TRANSACTION;
  begin
      delete from sns_comment where s_cmt_target_id = :old.s_cmt_id or s_cmt_reply_to_id = :old.s_cmt_id;
      delete from sns_doing where (:old.s_cmt_id = s_doi_reply_id or :old.s_cmt_id = s_doi_act_id ) and s_doi_module = :old.s_cmt_module;
      delete from sns_doing where :old.s_cmt_id = s_doi_target_id and s_doi_target_type = '1' and s_doi_module = 'Comment';
      delete from sns_valuation_log where :old.s_cmt_id = s_vtl_target_id and s_vtl_module = :old.s_cmt_module;
      delete from sns_count where :old.s_cmt_id = s_cnt_target_id and s_cnt_module = :old.s_cmt_module;            
      commit;
  end;
  

/*
* 重建知识表触发器
*/
CREATE OR REPLACE TRIGGER kb_item_trigger BEFORE INSERT ON kb_item
	FOR EACH ROW
WHEN (new.kbi_id IS NULL)
BEGIN
 SELECT kb_item_seq.NEXTVAL INTO :new.kbi_id FROM DUAL;
END;

/*
 * 知识目录表添加特殊目录“未分类”标识
 * TEMP  未分类
 */
alter table kb_catalog add kbc_type nvarchar2(255);

---分享知识积分
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_period,cty_hit)
VALUES('KB_SHARE_KNOWLEDGE','KB_SHARE_KNOWLEDGE',0,0,0,1,'KB',1,2,'s1u3',sysdate,'s1u3',sysdate,'MONTH',2);

update creditsType set cty_tcr_id =(select tcr_id from  tcTrainingCenter where tcr_parent_tcr_id is null)
where cty_tcr_id is null;
GO

/**
 *添加首页欢迎词管理字段 
 */
alter table siteposter add index_text varchar(255);
GO
ALTER TABLE siteposter RENAME COLUMN index_text TO sp_welcome_word;
GO

/**
 * 添加公告是否回执字段
 * */
alter table Message add MSG_RECEIPT NUMBER(*,0) DEFAULT (0);

/**
 * 公告回执表
 * */
create table Receipt (
	"REC_ID" NUMBER(10,0) NOT NULL ENABLE,
	"REC_MSG_ID" NUMBER(10,0) NOT NULL ENABLE,
	"REC_ENT_ID" NUMBER(10,0) NOT NULL ENABLE,
	"REC_USG_ID" NVARCHAR2(20),
	"RECEIPT_DATE" TIMESTAMP (6) NOT NULL ENABLE,
	 CONSTRAINT "PK_RECEIPT" PRIMARY KEY ("REC_ID")
	  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 204800 NEXT 204800 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "LMS"  ENABLE
)
create sequence  receipt_seq;

CREATE OR REPLACE TRIGGER receipt_trigger BEFORE INSERT ON receipt
FOR EACH ROW
WHEN (new.rec_id IS NULL)
BEGIN
 SELECT receipt_seq.NEXTVAL INTO :new.rec_id FROM DUAL;
END;


update kb_item_view set kiv_update_datetime=kiv_create_datetime where kiv_update_datetime is null;

 

/*
Auth : Jimmy
Date : 2015-02-15
Desc : 首页欢迎词管理字段 ，添加移动端欢迎词
*/

  alter table SitePoster modify  sp_welcome_word nvarchar2(255);
  alter table siteposter add mb_welcome_word nvarchar2(255);
/*
Auth : Myron
Date : 2015-02-25
Desc : 系统默认图片初始化
*/
update Siteposter set sp_media_file = 'ban01.jpg',  sp_media_file1 = '', sp_media_file2 = '', sp_media_file3 = '', sp_media_file4 = '', sp_logo_file_cn = 'logo3.png', sp_logo_file_hk = 'logo2.png', sp_logo_file_us = 'logo.jpg', login_bg_file1 = 'adv62.jpg',login_bg_file2 = '',login_bg_file3 = '',login_bg_file4 = '',login_bg_file5 = '' where sp_ste_id=1 and sp_mobile_ind = 1;
update Siteposter set sp_media_file = 'b1.png',  sp_media_file1 = '', sp_media_file2 = '', sp_media_file3 = '', sp_media_file4 = '', sp_logo_file_cn = 'wizbang.png', sp_logo_file_hk = 'wizbang.png', sp_logo_file_us = 'wizbang.png', login_bg_file1 = 'banner01.jpg',login_bg_file2 = 'banner02.jpg',login_bg_file3 = 'banner03.jpg',login_bg_file4 = '',login_bg_file5 = '' where sp_ste_id=1 and sp_mobile_ind = 0;

/*
Auth : Leepec
Date : 2015-02-26
Desc : 知识中心附件表增加路径字段
*/
alter table kb_attachment add kba_url nvarchar2(255);
update kb_attachment set kba_url = 'attachment/'||kba_id||'/'||kba_file;

update kb_attachment set kba_url = '/attachment/'||kba_id||'/'||kba_file;
/**
Auth : Myron
Date : 2015-02-26
Desc : 添加权限配置 
 */
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml) values ('FTN_LRN_ANN_MANAGE', 'FUNCTION', null,  sysdate, null);
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp) select rol_id, ftn_id,  sysdate from acRole, acFunction where rol_ste_uid in ('LRNR') and ftn_ext_id = 'FTN_LRN_ANN_MANAGE';
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml) values ('FTN_LRN_ART_MANAGE', 'FUNCTION', null,  sysdate, null);
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp) select rol_id, ftn_id,  sysdate from acRole, acFunction where rol_ste_uid in ('LRNR') and ftn_ext_id = 'FTN_LRN_ART_MANAGE';
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml) values ('FTN_TA_ART_VIEW', 'FUNCTION', null,  sysdate, null);
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp) select rol_id, ftn_id,  sysdate from acRole, acFunction where rol_ste_uid in ('TADM') and ftn_ext_id = 'FTN_TA_ART_VIEW';
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml) values ('FTN_LRN_LEARNING_CATALOG', 'FUNCTION', null,  sysdate, null);
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp) select rol_id, ftn_id,  sysdate from acRole, acFunction where rol_ste_uid in ('LRNR') and ftn_ext_id = 'FTN_LRN_LEARNING_CATALOG';
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml) values ('FTN_LRN_LEARNING_RECOMMEND', 'FUNCTION', null,  sysdate, null);
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp) select rol_id, ftn_id,  sysdate from acRole, acFunction where rol_ste_uid in ('LRNR') and ftn_ext_id = 'FTN_LRN_LEARNING_RECOMMEND';
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml) values ('FTN_LRN_LEARNING_SIGNUP', 'FUNCTION', null,  sysdate, null);
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp) select rol_id, ftn_id,  sysdate from acRole, acFunction where rol_ste_uid in ('LRNR') and ftn_ext_id = 'FTN_LRN_LEARNING_SIGNUP';
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml) values ('FTN_LRN_LEARNING_OPEN', 'FUNCTION', null,  sysdate, null);
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp) select rol_id, ftn_id,  sysdate from acRole, acFunction where rol_ste_uid in ('LRNR') and ftn_ext_id = 'FTN_LRN_LEARNING_OPEN';
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml) values ('FTN_LRN_LEARNING_MAP', 'FUNCTION', null,  sysdate, null);
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp) select rol_id, ftn_id,  sysdate from acRole, acFunction where rol_ste_uid in ('LRNR') and ftn_ext_id = 'FTN_LRN_LEARNING_MAP';
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml) values ('FTN_LRN_LEARNING_PROFESSION', 'FUNCTION', null,  sysdate, null);
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp) select rol_id, ftn_id,  sysdate from acRole, acFunction where rol_ste_uid in ('LRNR') and ftn_ext_id = 'FTN_LRN_LEARNING_PROFESSION';
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml) values ('FTN_LRN_EXAM_RECOMMEND', 'FUNCTION', null,  sysdate, null);
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp) select rol_id, ftn_id,  sysdate from acRole, acFunction where rol_ste_uid in ('LRNR') and ftn_ext_id = 'FTN_LRN_EXAM_RECOMMEND';
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml) values ('FTN_LRN_EXAM_SIGNUP', 'FUNCTION', null,  sysdate, null);
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp) select rol_id, ftn_id,  sysdate from acRole, acFunction where rol_ste_uid in ('LRNR') and ftn_ext_id = 'FTN_LRN_EXAM_SIGNUP';
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml) values ('FTN_LRN_EXAM_CATALOG', 'FUNCTION', null,  sysdate, null);
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp) select rol_id, ftn_id,  sysdate from acRole, acFunction where rol_ste_uid in ('LRNR') and ftn_ext_id = 'FTN_LRN_EXAM_CATALOG';
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml) values ('FTN_LRN_GROUP_MANAGE', 'FUNCTION', null,  sysdate, null);
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp) select rol_id, ftn_id,  sysdate from acRole, acFunction where rol_ste_uid in ('LRNR') and ftn_ext_id = 'FTN_LRN_GROUP_MANAGE';
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml) values ('FTN_TA_GROUP_MANAGE', 'FUNCTION', null,  sysdate, null);
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp) select rol_id, ftn_id,  sysdate from acRole, acFunction where rol_ste_uid in ('TADM') and ftn_ext_id = 'FTN_TA_GROUP_MANAGE';
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml) values ('FTN_LRN_KNOW_MANAGE', 'FUNCTION', null,  sysdate, null);
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp) select rol_id, ftn_id,  sysdate from acRole, acFunction where rol_ste_uid in ('LRNR') and ftn_ext_id = 'FTN_LRN_KNOW_MANAGE';
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml) values ('FTN_TA_KNOW_MANAGE', 'FUNCTION', null,  sysdate, null);
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp) select rol_id, ftn_id,  sysdate from acRole, acFunction where rol_ste_uid in ('TADM') and ftn_ext_id = 'FTN_TA_KNOW_MANAGE';
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml) values ('FTN_LRN_KB_VIEW', 'FUNCTION', null,  sysdate, null);
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp) select rol_id, ftn_id,  sysdate from acRole, acFunction where rol_ste_uid in ('LRNR') and ftn_ext_id = 'FTN_LRN_KB_VIEW';
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml) values ('FTN_LRN_DOING_MANAGE', 'FUNCTION', null,  sysdate, null);
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp) select rol_id, ftn_id,  sysdate from acRole, acFunction where rol_ste_uid in ('LRNR') and ftn_ext_id = 'FTN_LRN_DOING_MANAGE';
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml) values ('FTN_LRN_RANK_COURSE', 'FUNCTION', null,  sysdate, null);
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp) select rol_id, ftn_id,  sysdate from acRole, acFunction where rol_ste_uid in ('LRNR') and ftn_ext_id = 'FTN_LRN_RANK_COURSE';
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml) values ('FTN_LRN_RANK_LEARNING', 'FUNCTION', null,  sysdate, null);
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp) select rol_id, ftn_id,  sysdate from acRole, acFunction where rol_ste_uid in ('LRNR') and ftn_ext_id = 'FTN_LRN_RANK_LEARNING';
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml) values ('FTN_LRN_RANK_CREDIT', 'FUNCTION', null,  sysdate, null);
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp) select rol_id, ftn_id,  sysdate from acRole, acFunction where rol_ste_uid in ('LRNR') and ftn_ext_id = 'FTN_LRN_RANK_CREDIT';

/**
Auth : Myron
Date : 2015-02-27
Desc : 添加权限配置 
 */
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml) values ('FTN_LRN_HOME_VIEW', 'FUNCTION', null, sysdate, null);
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp) select rol_id, ftn_id, sysdate from acRole, acFunction where rol_ste_uid in ('LRNR') and ftn_ext_id = 'FTN_LRN_HOME_VIEW';
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml) values ('FTN_LRN_LEARNING_COURSEVIEW', 'FUNCTION', null, sysdate, null);
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp) select rol_id, ftn_id, sysdate from acRole, acFunction where rol_ste_uid in ('LRNR') and ftn_ext_id = 'FTN_LRN_LEARNING_COURSEVIEW';


/**
Auth : Leepec
Date : 2015-03-03
DESC : 添加是否是在线视频标识
 */
alter table kb_item add kbi_online nvarchar2(20);


/**
Auth : Leepec
Date : 2015-03-06
DESC : 将邮件帐号类型固定为'EMAIL'
 */
update SystemSetting set sys_cfg_value='EMAIL' where sys_cfg_type='SYS_MAIL_SERVER_ACCOUNT_TYPE'

/**
Auth : Leepec
Date : 2015-03-10
DESC : 将学习概括中用到的知识中心数据缓存到LearningSituation
 */
alter table LearningSituation add ls_share_count int;
alter table LearningSituation add ls_access_count int;


/**
Auth : lance
Date : 2015-03-12
DESC : 添加培训管理员课程评论权限
 */
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml) values ('FTN_TA_ITEM_COMMENT', 'FUNCTION', null,  sysdate, null);
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp) select rol_id, ftn_id,  sysdate from acRole, acFunction where rol_ste_uid in ('TADM') and ftn_ext_id = 'FTN_TA_ITEM_COMMENT';


/**
Auth : lance
Date : 2015-03-14
DESC : 添加邮件头部和底部图片
 */
insert into messageparamname (mpn_mtp_id, mpn_name, mpn_name_desc) select mtp_id,'[Header image]','lab_msg_header_image' from messagetemplate;
insert into messageparamname (mpn_mtp_id, mpn_name, mpn_name_desc) select mtp_id,'[Footer image]','lab_msg_footer_image' from messagetemplate;
alter table messagetemplate add mtp_header_img nvarchar2(200);
alter table messagetemplate add mtp_footer_img nvarchar2(200);
update messageTemplate set mtp_content = '[Header image]'||mtp_content||'[Footer image]';
update messageTemplate set mtp_header_img = '/msg/emailHeader.jpg';
update messageTemplate set mtp_footer_img = '/msg/emailFooter.jpg';

/**
 * myron
 * 2015-03-20
 * 添加系统管理员行业资讯详情页面权限
 */
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml) values ('FTN_SYS_ART_MANAGE', 'FUNCTION', null,  sysdate, null);
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp) select rol_id, ftn_id,  sysdate from acRole, acFunction where rol_ste_uid in ('SADM') and ftn_ext_id = 'FTN_SYS_ART_MANAGE';

/**
Auth : lacne
Date : 2015-03-20
DESC : 将邮件帐号类型恢复为'EMAIL'
 */
update SystemSetting set sys_cfg_value='EMAIL' where sys_cfg_type='SYS_MAIL_SERVER_ACCOUNT_TYPE';

/**
Auth : Myron.liu
Date : 2015-03-23
DESC : 扩大在线图片等等信息字段的长度
 */
update MODULETEMPFILE set MTF_URL = '';
alter table MODULETEMPFILE modify (MTF_URL varchar2(4000));

/**
Auth : Leon
Date : 2015-03-25
DESC : 更改删除评论及其附属评论的时候出现死锁问题
 */
create or replace trigger delCommentRel
  after delete on sns_comment
  for each row
       declare
     PRAGMA AUTONOMOUS_TRANSACTION;
  begin
      delete from sns_comment where s_cmt_target_id = :old.s_cmt_id or s_cmt_reply_to_id = :old.s_cmt_id;
      delete from sns_doing where (:old.s_cmt_id = s_doi_reply_id or :old.s_cmt_id = s_doi_act_id ) and s_doi_module = :old.s_cmt_module;
      delete from sns_doing where :old.s_cmt_id = s_doi_target_id and s_doi_target_type = '1' and s_doi_module = 'Comment';
      delete from sns_valuation_log where :old.s_cmt_id = s_vtl_target_id and s_vtl_module = :old.s_cmt_module;
      delete from sns_count where :old.s_cmt_id = s_cnt_target_id and s_cnt_module = :old.s_cmt_module;            
      commit;
  end;

/**
 * myron.liu
 * 2015-03-26
 * 删除单点登录链接功能
 */
delete from acrolefunction where rfn_FTN_ID = (select FTN_ID from acfunction where ftn_ext_id='SSO_LINK_QUERY');
delete from acfunction where ftn_ext_id='SSO_LINK_QUERY';
delete from achomepage where ac_hom_ftn_ext_id = 'SSO_LINK_QUERY';

/**
 * leon.li
 * 2015-04-1
 * 删除触发器，由程序完成该操作
 */
drop trigger delCommentRel;

/**
Auth: myron	
Date: 2014-11-20
Desc: 添加问答赏金的积分类型
 */
INSERT INTO creditsType
(cty_code, cty_title, cty_deduction_ind, cty_manual_ind, cty_deleted_ind, 
cty_relation_total_ind,cty_relation_type,cty_default_credits_ind, cty_default_credits,
 cty_create_usr_id, cty_create_timestamp, cty_update_usr_id, cty_update_timestamp, cty_tcr_id) 
VALUES 
('SYS_ANWSER_BOUNTY','SYS_ANWSER_BOUNTY','0','1','0','1','SYS','0','0','s1u3',sysdate,'s1u3',sysdate,1);

INSERT INTO creditsType
(cty_code, cty_title, cty_deduction_ind, cty_manual_ind, cty_deleted_ind, 
cty_relation_total_ind,cty_relation_type,cty_default_credits_ind, cty_default_credits,
 cty_create_usr_id, cty_create_timestamp, cty_update_usr_id, cty_update_timestamp, cty_tcr_id) 
VALUES 
('SYS_QUESTION_BOUNTY', 'SYS_QUESTION_BOUNTY','1','1','0','1','SYS','0','0','s1u3',sysdate,'s1u3',sysdate,1);
/*
 * 在提问表内加入悬赏积分字段
 */
alter table knowQuestion add que_bounty float default 0 null;

/* r52225 from r46105 */

/*
 *leon ,菜单整理
  2015-4-29
*/
delete from acRoleFunction;
delete from acFunction;
alter table acfunction drop column ftn_xml;
alter table acfunction add ftn_assign nvarchar2(10) default 1;
alter table acfunction add ftn_tc_related nvarchar2(10) default 'N';
alter table acfunction add ftn_status nvarchar2(10) default 0;
alter table acfunction add ftn_order int;
alter table acfunction add ftn_parent_id int;
alter table acrolefunction add rfn_ftn_favorite nvarchar2(10) default 0;
alter table acrolefunction add rfn_ftn_order int;  -- 标记 内容 成绩 处理报名 这种不在菜单显示的
alter table acrolefunction add rfn_ftn_parent_id int;
update acRole set rol_url_home = 'app/admin/home' where rol_id != 5

/*
 *leon ,首页添加删除常用功能
  2015-05-06
*/
create table userFavoriteFunction(
  uff_usr_ent_id int not null,
  uff_role_ext_id nvarchar2(32),
  uff_fun_id int not null,
  uff_create_datetime timestamp
);
ALTER TABLE userFavoriteFunction ADD CONSTRAINT
  PK_userFavoriteFunction PRIMARY KEY 
  (
  uff_usr_ent_id,
  uff_fun_id
);

/*
*签到记录表 
*id
*学员id
*日程id
*课程id
*app_id
*签到状态  1正常 2迟到 3缺勤
*签到时间
*创建时间
* @GUYU
*/
alter table aeitemlesson add  ils_date timestamp null;
alter table aeitemlesson add  ils_qiandao int default 0 not null;
alter table aeitemlesson add  ils_qiandao_chidao int null;
alter table aeitemlesson add  ils_qiandao_queqin int null;
alter table aeitemlesson add  ils_qiandao_youxiaoqi int null;

alter table aeitemlesson add  ils_qiandao_chidao_time timestamp null;
alter table aeitemlesson add  ils_qiandao_queqin_time timestamp null;
alter table aeitemlesson add  ils_qiandao_youxiaoqi_time timestamp null;
create table aeItemLessonQianDao(
	ilsqd_id int not null,
	ilsqd_usr_ent_id int not null,
	ilsqd_ils_id int not null,
	ilsqd_ils_itm_id int not null,
	ilsqd_app_id int not null,
	ilsqd_status int not null,
	ilsqd_date timestamp not null,
	ilsqd_create_timestamp timestamp
);
create sequence aeItemLessonQianDao_seq;
CREATE OR REPLACE TRIGGER aeItemLessonQianDao_trigger BEFORE INSERT ON aeItemLessonQianDao
FOR EACH ROW
WHEN (new.ilsqd_id IS NULL)
BEGIN
 SELECT aeItemLessonQianDao_seq.NEXTVAL INTO :new.ilsqd_id FROM DUAL;
END;

ALTER TABLE aeItemLessonQianDao ADD CONSTRAINT
PK_aeItemLessonQianDao PRIMARY KEY (
	ilsqd_id
);
/*
* 邀请讲师回答
* @leon.li
*/
alter table knowQuestion add que_ask_ent_ids nvarchar2(1000);


CREATE TABLE voting(
	vot_id INTEGER primary key,
	vot_title VARCHAR2(255) NOT NULL,
	vot_content BLOB NULL,
	vot_status VARCHAR2(20) NOT NULL,
	vot_tcr_id INTEGER NULL,
	vot_eff_date_from DATE NULL,
	vot_eff_date_to DATE NULL,
	vot_create_timestamp DATE NULL,
	vot_create_usr_id VARCHAR2(20) NULL,
	vot_update_timestamp DATE NULL,
	vot_update_usr_id VARCHAR2(20) NULL
);

CREATE SEQUENCE voting_seq;
CREATE OR REPLACE TRIGGER voting_trigger BEFORE INSERT ON voting
FOR EACH ROW
WHEN (new.vot_id IS NULL)
BEGIN
 SELECT voting_seq.NEXTVAL INTO :new.vot_id FROM DUAL;
END;

CREATE TABLE votequestion(
	vtq_id INTEGER primary key,
	vtq_vot_id INTEGER NULL,
	vtq_title VARCHAR2(255) NULL,
	vtq_contnet BLOB NULL,
	vtq_type VARCHAR2(20) DEFAULT('MC'),
	vtq_status VARCHAR2(20) DEFAULT('ON'),
	vtq_order INTEGER DEFAULT(1) NULL,
	vtq_create_timestamp DATE NOT NULL,
	vtq_create_usr_id VARCHAR2(20) NULL,
	vtq_update_timestamp DATE NULL,
	vtq_update_usr_id VARCHAR2(20) NULL
);

CREATE SEQUENCE votequestion_seq;
CREATE OR REPLACE TRIGGER votequestion_trigger BEFORE INSERT ON votequestion
FOR EACH ROW
WHEN (new.vtq_id IS NULL)
BEGIN
 SELECT votequestion_seq.NEXTVAL INTO :new.vtq_id FROM DUAL;
END;

CREATE TABLE voteoption(
	vto_id INTEGER primary key,
	vto_vtq_id INTEGER NULL,
	vto_desc VARCHAR2(500) NULL,
	vto_order INTEGER NULL
);

CREATE SEQUENCE voteoption_seq;
CREATE OR REPLACE TRIGGER voteoption_trigger BEFORE INSERT ON voteoption
FOR EACH ROW
WHEN (new.vto_id IS NULL)
BEGIN
 SELECT voteoption_seq.NEXTVAL INTO :new.vto_id FROM DUAL;
END;

CREATE TABLE voteresponse(
	vrp_usr_ent_id INTEGER NOT NULL,
	vrp_vot_id INTEGER NOT NULL,
	vrp_vtq_id INTEGER NOT NULL,
	vrp_vto_id INTEGER NULL,
	vrp_respone_time DATE NULL
);


/**
 * Andrew.xiao
 * 2015-6-18
 * 添加【投票】的设置
 */
alter table sns_setting add s_set_voting int null;


/**
 * Karl
 * 站内信表
 * wmsg_send_type 发件箱状态
 * wmsg_rec_type 收件箱状态
 * 2015-06-18
 * */
alter table webMessage add wmsg_send_type nvarchar2(20) default 'SEND';
alter table webMessage add wmsg_rec_type nvarchar2(20) default 'REC';

/**
 * lance
 * 2015-06-19
 * 为sitePoster表增加是否是否显示登录页面header和所有页面footer字段
 */
alter table sitePoster add sp_login_show_header_ind number;
alter table sitePoster add sp_all_show_footer_ind number;

/**
 * leon.li
 * 2015-06-23
 * 讲师加上推荐字段
 */
alter table InstructorInf add iti_recommend int;   

/**
 * randy
 * 2015-06-24
 * 默认应该是显示登录页面header和所有页面footer字段
 */

update sitePoster set sp_login_show_header_ind = 1;
update sitePoster set sp_all_show_footer_ind = 1;


/**
 * andrew,xiao
 * 2015-07-10
 * 将系统参数配置中微信部分移动新的tab页中
 */
alter table SystemSetting rename column sys_cfg_value to sys_cfg_value_tmp;
update entity set ent_ste_uid = (select ugr_code from userGrade where ugr_ent_id = ent_id and ent_id > 7 and ent_type = 'UGR');

alter table SystemSetting add sys_cfg_value nvarchar2(2000);

update SystemSetting set sys_cfg_value=trim(sys_cfg_value_tmp);

commit;

alter table SystemSetting drop column sys_cfg_value_tmp;

insert into SystemSetting(sys_cfg_type,sys_cft_update_usr_id,sys_cfg_create_timestamp,sys_cfg_create_usr_id,sys_cfg_update_timestamp,sys_cfg_value) values('WECHAT_MENU','s1u3',SYSDATE,'s1u3',SYSDATE,
	'{
		"button":[
			{
				"name":"资讯",
				"sub_button":[
					{"type":"click","name":"公告","key":"1"},
					{"type":"click","name":"行业资讯","key":"2"}
				]			
			},	
			{
				"name":"我的学习",
				"sub_button":[
					{"type":"click","name":"已报名课程","key":"3"},
					{"type":"click","name":"已报名考试","key":"4"},
					{"type":"click","name":"推荐课程","key":"5"},
					{"type":"click","name":"公开课","key":"6"}
				]			
			},	
			{
				"name":"更多",
				"sub_button":[
					{"type":"click","name":"绑定学习平台","key":"11"},
					{"type":"click","name":"进入到学习平台","key":"7"},
					{"type":"click","name":"解除绑定","key":"8"},
					{"type":"click","name":"我的积分","key":"9"},
					{"type":"click","name":"学习概括","key":"10"}
				]
			}
		]
	}'
);




 /**
 *职务表增加职务编号字段(nat.li)
 */
 alter table UserGrade add ugr_code nvarchar2(255);
  update userGrade set ugr_code = 'P'||ugr_ent_id where ugr_ent_id > 7;
  update userGrade set ugr_code = 'Unspecified'  where ugr_display_bil ='Unspecified' and ugr_default_ind = 1  ;
  update entity set ent_ste_uid = (select ugr_code from userGrade where ugr_ent_id = ent_id and ent_type = 'UGR');
 commit;

 /*
 *修改学习信息表的学分列类型为Float(nat.li 2015-10-13)
 */
 alter table LearningSituation modify ls_learn_credit float;
 commit;

alter table IMSLog add ilg_tcr_id int;
commit;
update IMSLog set ilg_tcr_id=1;
commit;


/**
 * randy
 * 2015-15-15
 * 删除报名状态为cancel的报名记录
 */
delete from aeItemComments where exists (select tkh_id from trackinghistory where tkh_id = ict_tkh_id and tkh_id in(select app_tkh_id from aeapplication where app_status = 'Withdrawn'));
delete from aeItemMessage where exists (select app_id from aeapplication where app_id = img_app_id and app_status = 'Withdrawn');
delete from aeitemlessonqiandao where exists (select app_id from aeapplication where app_id = ilsqd_app_id and app_status = 'Withdrawn');
delete from scoRecord where exists(select tkh_id from trackinghistory where tkh_id = srd_tkh_id and tkh_id in(select app_tkh_id from aeapplication where app_status = 'Withdrawn'));

delete from accomplishment where exists(select tkh_id from trackinghistory where tkh_id = apm_tkh_id and tkh_id in(select app_tkh_id from aeapplication where app_status = 'Withdrawn'));
delete from courseEvaluation where exists(select tkh_id from trackinghistory where tkh_id = cov_tkh_id and tkh_id in(select app_tkh_id from aeapplication where app_status = 'Withdrawn'));
delete from ModuleEvaluationHistory where exists(select tkh_id from trackinghistory where tkh_id = mvh_tkh_id and tkh_id in(select app_tkh_id from aeapplication where app_status = 'Withdrawn'));
delete from ModuleEvaluation where exists(select tkh_id from trackinghistory where tkh_id = mov_tkh_id  and tkh_id in(select app_tkh_id from aeapplication where app_status = 'Withdrawn'));

delete from aeappnopenitem where exists (select app_id from aeapplication where app_id = aoi_app_id and app_status = 'Withdrawn');
delete from aeappncommhistory where exists (select app_id from aeapplication where app_id = ach_app_id  and app_status = 'Withdrawn') ;
delete from aeappnapprovallist where exists (select app_id from aeapplication where app_id = aal_app_id  and app_status = 'Withdrawn');
delete from aeAppnActnHistory where exists (select app_id from aeapplication where app_id = aah_app_id and app_status = 'Withdrawn');
delete from aeattendance where exists (select app_id from aeapplication where app_id = att_app_id  and app_status = 'Withdrawn'); 
delete from aeappntargetentity where exists (select app_id from aeapplication where app_id = ate_app_id  and app_status = 'Withdrawn');
delete from progressattempt where exists (select tkh_id from trackinghistory where tkh_id = atm_tkh_id and tkh_id in(select app_tkh_id from aeapplication where app_status = 'Withdrawn'));
delete from progressAttachment where exists (select tkh_id from trackinghistory where tkh_id = pat_tkh_id and tkh_id in(select app_tkh_id from aeapplication where app_status = 'Withdrawn'));
delete from attachment where not exists(select pat_att_id from progressattachment where att_id = pat_att_id);
delete from progress where exists (select tkh_id from trackinghistory where tkh_id = pgr_tkh_id and tkh_id in(select app_tkh_id from aeapplication where app_status = 'Withdrawn'));
delete from Aeitemcomments where exists (select tkh_id from trackinghistory where tkh_id = ict_tkh_id and tkh_id in(select app_tkh_id from aeapplication where app_status = 'Withdrawn'));
delete from trackinghistory where  tkh_id in(select app_tkh_id from aeapplication where app_status = 'Withdrawn');
delete from aeapplication where app_status = 'Withdrawn';
commit;
 
<<<<<<< .working·


/* 
 * 知识中心--表增加一个存放附件类型的字段 
 * bill.lai 2015-12-31
 * 添加了列kbi_filetype后，访问 http://ip:port/app/kb/admin/updateOldData 以更新旧数据 
 * */
ALTER TABLE kb_item ADD kbi_filetype VARCHAR2(10);
commit;	

/*
*修复积分历史数据（BUG4521）
*/
 	update userCreditsDetail set ucd_cty_id=(SELECT cty_id FROM creditsType WHERE cty_code = 'ITM_IMPORT_CREDIT' and cty_tcr_id=1)
	where ucd_cty_id in(SELECT cty_id FROM creditsType WHERE cty_code = 'ITM_IMPORT_CREDIT');
	commit;

	
	
/*
 * nat.li
 * 2016/1/8
 * 防止删除课程死锁
 */
create or replace trigger delDoingRel
 after delete on  sns_doing
  for each row
     declare   
     PRAGMA AUTONOMOUS_TRANSACTION;
     isNotice int;  
     module VARCHAR2(20);  
  begin
  isNotice:=:old.s_doi_target_type;
  module:=:old.s_doi_module;
    if isNotice < 1 and module!='Course'  then
       delete from sns_doing
       where :old.s_doi_id = s_doi_target_id;
       delete from sns_doing
       where s_doi_target_id = :old.s_doi_id and s_doi_target_type = '1';
       delete from sns_valuation_log
	     where s_vtl_target_id = :old.s_doi_id and s_vtl_module = :old.s_doi_module;
	     delete from sns_count
       where :old.s_doi_id = s_cnt_target_id and s_cnt_module = :old.s_doi_module;
       commit;
    end if;
  end;
/* r54129 */


  /**
 * 新增siteposter两列，存放视频地址以及登陆页面显示类型、
 * 更新以前PC端的登陆页面默认为轮播图类型（PIC）
 */
ALTER TABLE siteposter ADD login_bg_video VARCHAR2(200);
ALTER TABLE siteposter ADD login_bg_type VARCHAR2(10);
update SitePoster set login_bg_type = 'PIC' where sp_mobile_ind = 0;
commit;

/**
 * 学习足迹新增字段
 */
ALTER TABLE LearningSituation ADD Is_first_course_date timestamp;
ALTER TABLE LearningSituation ADD Is_first_praised_usr nvarchar2(255);
ALTER TABLE LearningSituation ADD Is_first_praised_date timestamp;
ALTER TABLE LearningSituation ADD Is_first_fans_usr nvarchar2(255);
ALTER TABLE LearningSituation ADD Is_first_fans_date timestamp;
ALTER TABLE LearningSituation ADD Is_first_share_date timestamp;
ALTER TABLE LearningSituation ADD Is_first_que_date timestamp;
ALTER TABLE LearningSituation ADD Is_first_helper_usr nvarchar2(255);
ALTER TABLE LearningSituation ADD Is_signup_date timestamp;

/**
 * 移动端banner图片替换
 */
update siteposter set sp_media_file = 'banner-a1.jpg',sp_media_file1 = 'banner-a2.jpg',sp_media_file2 = 'banner-a3.jpg',sp_media_file3 = 'banner-a4.jpg' where sp_tcr_id = 1 and SP_MOBILE_IND = 1;
commit;

/**
 * 删除表SUPPLIERCOMMENT外键
 */
ALTER TABLE SUPPLIERCOMMENT DROP CONSTRAINT FK_SCM_ENT_ID;
ALTER TABLE SUPPLIERCOMMENT DROP CONSTRAINT FK_SCM_SPL_ID;
commit;


/**
 * 岗位类型
 */
create table UserPositionCatalog
(
upc_id   int  not null,
upc_title nvarchar2(255) not null,
upc_desc  nclob  null,
upc_status   int ,
upc_tcr_id   int ,
upc_create_datetime timestamp not null,
upc_create_user_id int not null,
upc_update_datetime  timestamp not null,
upc_update_user_id  int not null
);  

ALTER TABLE UserPositionCatalog ADD 
    CONSTRAINT PK_upclog PRIMARY KEY   
    (
        upc_id
    )  
;
commit;

create sequence userPositionCatalog_seq;  
CREATE OR REPLACE TRIGGER userPositionCatalog_trigger BEFORE INSERT ON UserPositionCatalog
FOR EACH ROW
WHEN (new.upc_id IS NULL)
BEGIN
 SELECT userPositionCatalog_seq.NEXTVAL INTO :new.upc_id FROM DUAL;
END;  

/**
 * 岗位地图
 */
create table UserPositionLrnMap
(
upm_id   int  not null,
upm_upt_id  int not null,
upm_seq_no  int not null,
upm_tcr_id   int not null ,
upm_img   nvarchar2(255) not null ,
upm_create_time timestamp not null,
upm_create_usr_id int not null,
upm_update_time  timestamp not null,
upm_update_usr_id  int not null,
upm_status  int not null
);  

ALTER TABLE UserPositionLrnMap ADD 
    CONSTRAINT PK_uplMap PRIMARY KEY   
    (
        upm_id
    )  
;
commit;

create sequence userPositionLrnMap_seq;  
CREATE OR REPLACE TRIGGER userPositionLrnMap_trigger BEFORE INSERT ON UserPositionLrnMap
FOR EACH ROW
WHEN (new.upm_id IS NULL)
BEGIN
 SELECT userPositionLrnMap_seq.NEXTVAL INTO :new.upm_id FROM DUAL;
END;  

/**
 * 岗位地图课程关系
 */
create table UserPositionLrnItem
(
upi_id   int  not null,
upi_upm_id  int not null,
upi_itm_id  int not null
);  
ALTER TABLE UserPositionLrnItem ADD 
    CONSTRAINT PK_uplItem PRIMARY KEY   
    (
        upi_id
    )  
;
commit;

create sequence userPositionLrnItem_seq;  
CREATE OR REPLACE TRIGGER userPositionLrnItem_trigger BEFORE INSERT ON UserPositionLrnItem
FOR EACH ROW
WHEN (new.upi_id IS NULL)
BEGIN
 SELECT userPositionLrnItem_seq.NEXTVAL INTO :new.upi_id FROM DUAL;
END;  

/**
 * 发展序列新增字段
 */
ALTER TABLE Profession ADD pfs_status int;
ALTER TABLE Profession ADD pfs_tcr_id int;

/**
 * 课程目标学员明细表新增字段  数据来源职级/岗位学习地图
 */
ALTER TABLE itemTargetLrnDetail ADD itd_fromprofession int;
ALTER TABLE itemTargetLrnDetail ADD itd_fromposition int;

/**
 * 岗位新增字段
 */
ALTER TABLE UserPosition ADD upt_upc_id int;
commit;

/**
 * 新增列：eip_tcr_id；根据列去做企业登录数统计计算
 * */
ALTER TABLE CurrentActiveUser ADD
cau_eip_tcr_id int
commit;
/**
 * 新增列：max_peak_count；企业管理中所允许的最大在线人数
 * */
ALTER TABLE EnterpriseInfoPortal ADD
eip_max_peak_count int
commit;

/**
 * 专题课程关系
 */
create table UserSpecialItem
(
usi_id   int  not null,
ust_utc_id  int not null,
usi_itm_id  int not null
);  
ALTER TABLE UserSpecialItem ADD 
    CONSTRAINT PK_usiItem PRIMARY KEY   
    (
        usi_id
    )  
;
commit;

create sequence userSpecialItem_seq;  
CREATE OR REPLACE TRIGGER userSpecialItem_trigger BEFORE INSERT ON UserSpecialItem
FOR EACH ROW
WHEN (new.usi_id IS NULL)
BEGIN
 SELECT userSpecialItem_seq.NEXTVAL INTO :new.usi_id FROM DUAL;
END;  
/**
 * 专题专家关系
 */
create table UserSpecialExpert
(
use_id   int  not null,
use_ust_id  int not null,
use_ent_id  int not null
);  
ALTER TABLE UserSpecialExpert ADD 
    CONSTRAINT PK_useExpert PRIMARY KEY   
    (
        use_id
    )  
;
commit;

create sequence userSpecialExpert_seq;  
CREATE OR REPLACE TRIGGER userSpecialExpert_trigger BEFORE INSERT ON UserSpecialExpert
FOR EACH ROW
WHEN (new.use_id IS NULL)
BEGIN
 SELECT userSpecialExpert_seq.NEXTVAL INTO :new.use_id FROM DUAL;
END;
/**
 * 专题培训
 */
create table UserSpecialTopic
(
ust_id   int  not null,
ust_title  nvarchar2(100) not null,
ust_img   nvarchar2(200) not null,
ust_tcr_id   int not null,
ust_summary nvarchar2(500) not null,
ust_content nclob null,
ust_showindex int not null,
ust_hits int not null,
ust_create_time timestamp not null,
ust_create_usr_id int not null,
ust_update_time  timestamp not null,
ust_update_usr_id  int not null,
ust_status  int not null
);  
ALTER TABLE UserSpecialTopic ADD 
    CONSTRAINT PK_ustTopic PRIMARY KEY   
    (
        ust_id
    );
commit;

create sequence userSpecialTopic_seq;  
CREATE OR REPLACE TRIGGER userSpecialTopic_trigger BEFORE INSERT ON UserSpecialTopic
FOR EACH ROW
WHEN (new.ust_id IS NULL)
BEGIN
 SELECT userSpecialTopic_seq.NEXTVAL INTO :new.ust_id FROM DUAL;
END;  


/**
 * 后台导航学习地图添加功能项
 * 注意:插入acRoleFunction时需根据前面插入acfuntion里的ftn_id新增数据
 */
declare ftn_id_parent int;
        ftn_id int;
begin

select ftn_id into ftn_id_parent from acFunction where ftn_ext_id = 'FTN_AMD_STUDY_MAP_MGT';

INSERT into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_tc_related, ftn_status, ftn_assign, ftn_order, ftn_parent_id) 
VALUES ('FTN_AMD_SPECIALTOPIC_MAIN', '1', NULL, sysdate, 'N', '1', '1', 30, ftn_id_parent);

INSERT into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_tc_related, ftn_status, ftn_assign, ftn_order, ftn_parent_id) 
VALUES ('FTN_AMD_POSITION_MAP_MAIN', '1', NULL, sysdate, 'N', '1', '1', 27, ftn_id_parent);

/*插入acRoleFunction时需根据前面插入acfuntion里的ftn_id新增数据*/
select FTN_ID into ftn_id from acFunction where ftn_ext_id='FTN_AMD_POSITION_MAP_MAIN';
INSERT into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_usr_id, RFN_CREATE_TIMESTAMP, 
rfn_ftn_parent_id, rfn_ftn_order)  VALUES (1, ftn_id, NULL, sysdate, ftn_id_parent, 9);

select  FTN_ID into ftn_id from acfunction  where ftn_ext_id='FTN_AMD_SPECIALTOPIC_MAIN';
INSERT into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_usr_id, RFN_CREATE_TIMESTAMP, 
rfn_ftn_parent_id, rfn_ftn_order)  VALUES (1, ftn_id, NULL, sysdate, ftn_id_parent, 12);
end;
commit;



/*
Auth: Bill
Date: 2016-04-29
Desc: 在线问答站内信
*/
insert into messageTemplate(mtp_type, mtp_subject, mtp_content, mtp_remark_label, mtp_web_message_ind, mtp_active_ind, mtp_tcr_id, mtp_update_timestamp, mtp_update_ent_id) 
values ('KNOW_ADD_MESSAGE',N'来自[User name]的站内信',N'来自[User name]的站内信','lab_KNOW_ADD_MESSAGE',1,1,1,sysdate,3);
commit;
/**
Auth: Bill
Date: 2016-04-29
Desc: 在线问答站内信(初始化邮件模板邮件内容)
*/
update messageTemplate set mtp_content = N'亲爱的 [Learner name]： <br /> 	学员[User name]提出了问题"[Question title]"需要你的帮助，点击链接 <span style="color:#0000FF;font-family:Verdana, Geneva, Arial, Helvetica, sans-serif;line-height:normal;">[Link to KnowDetail]</span> 可了解问题更多详情...' where mtp_type='KNOW_ADD_MESSAGE';
insert into messageParamName(mpn_mtp_id,MPN_NAME,MPN_NAME_desc) select mtp_id, '[User name]', 'lab_know_user_name' from messageTemplate where mtp_type='KNOW_ADD_MESSAGE';
insert into messageParamName(mpn_mtp_id,MPN_NAME,MPN_NAME_desc) select mtp_id, '[Learner name]', 'lab_msg_learner_name' from messageTemplate where mtp_type='KNOW_ADD_MESSAGE';
insert into messageParamName(mpn_mtp_id,MPN_NAME,MPN_NAME_desc) select mtp_id, '[Question title]', 'lab_msg_question_title' from messageTemplate where mtp_type='KNOW_ADD_MESSAGE';
insert into messageParamName(mpn_mtp_id,MPN_NAME,MPN_NAME_desc) select mtp_id, '[Link to KnowDetail]', 'lab_msg_link_to_know_detail' from messageTemplate where mtp_type='KNOW_ADD_MESSAGE';

update messageTemplate set mtp_content_pc_link = 'app/know/detail/' where mtp_type = 'KNOW_ADD_MESSAGE' and mtp_remark_label = 'lab_KNOW_ADD_MESSAGE';
commit;

/**
 * Auth: Jacky 
 * Date: 2016-05-07
 * INFO: 自动积分管理 //新增五项（参加公共调查问卷、创建群组、参与群组、被点赞、点赞）限制积分次数
 * 
 */
UPDATE creditsType SET cty_period =  'MONTH'  WHERE cty_code = 'SYS_SUBMIT_SVY'	or cty_code = 'SYS_CREATE_GROUP' or cty_code = 'SYS_JION_GROUP' or cty_code = 'SYS_GET_LIKE' or cty_code = 'SYS_CLICK_LIKE'
commit;

/**
Auth: Bill
Date: 2016-05-07
Desc: 站内信增加一列后台内容
*/
ALTER TABLE webMessage ADD wmsg_admin_content_pc nclob;
commit;

/**
Auth: Halo
Date: 2016-05-09
Desc: 职级发展序列关联课程表
*/
CREATE TABLE ProfessionLrnItem(
  psi_id int NOT NULL,
  psi_pfs_id int NOT NULL,
  psi_ugr_id nvarchar2(1000) NULL,
  psi_itm_id nvarchar2(1000) NULL);
    

ALTER TABLE ProfessionLrnItem ADD CONSTRAINT
	PK_ProfessionLrnItem PRIMARY KEY 
	(
	psi_id
	);  
commit;

create sequence professionLrnItem_seq;

CREATE OR REPLACE TRIGGER ProfessionLrnItem_trigger BEFORE INSERT ON ProfessionLrnItem
FOR EACH ROW
WHEN (new.psi_id IS NULL)
BEGIN
 SELECT professionLrnItem_seq.NEXTVAL INTO :new.psi_id FROM DUAL;
END;
 /* 新增列：pfs_template；选用模版
 * */
ALTER TABLE profession ADD pfs_template int;
commit;

/**
 * Auth : Lucky
 * Date: 2016-05-10
 * Desc : 帮助中心功能  帮助问题表
 */
CREATE TABLE helpQuestion(
	hq_id int NOT NULL ,
	hq_type_id int NOT NULL,
	hq_title nvarchar2(255) NOT NULL,
	hq_content blob NOT NULL,
	hq_create_timestamp timestamp NOT NULL,
	hq_update_timestamp timestamp NULL,
	hq_top_index int NOT NULL,
	hq_is_hot int NOT NULL,
	hq_is_publish int NOT NULL,
	hq_language nvarchar2(50) NOT NULL
);

ALTER TABLE helpQuestion ADD 
    CONSTRAINT PK_helpQuestion PRIMARY KEY   
    (
        hq_id
    );
commit;

create sequence helpQuestion_seq;  
CREATE OR REPLACE TRIGGER helpQuestion_trigger BEFORE INSERT ON helpQuestion
FOR EACH ROW
WHEN (new.hq_id IS NULL)
BEGIN
 SELECT helpQuestion_seq.NEXTVAL INTO :new.hq_id FROM DUAL;
END;  


/**
 * Auth : Lucky
 * Date: 2016-05-10
 * Desc : 帮助中心功能  帮助问题分类表
 */
CREATE TABLE helpQuestionType(
	hqt_id int NOT NULL,
	hqt_type_name nvarchar2(100) NOT NULL,
	hqt_pid int NOT NULL,
	hqt_top_index int NOT NULL,
	hqt_is_publish int NOT NULL,
	hqt_create_timestamp timestamp NOT NULL,
	hqt_language nvarchar2(50) NOT NULL
);

ALTER TABLE helpQuestionType ADD 
    CONSTRAINT PK_helpQuestionType PRIMARY KEY   
    (
        hqt_id
    );
commit;

create sequence helpQuestionType_seq;  
CREATE OR REPLACE TRIGGER helpQuestionType_trigger BEFORE INSERT ON helpQuestionType
FOR EACH ROW
WHEN (new.hqt_id IS NULL)
BEGIN
 SELECT helpQuestionType_seq.NEXTVAL INTO :new.hqt_id FROM DUAL;
END;  


/**
Auth: Bill
Date: 2016-05-11
Desc: 在线问答站内信(更新移动端链接地址)
*/
update messageTemplate set mtp_content_mobile_link = '/mobile/views/know/detail.html' where mtp_type = 'KNOW_ADD_MESSAGE' and mtp_remark_label = 'lab_KNOW_ADD_MESSAGE';
commit;

/**
Auth: leaf
Date: 2016-05-23
Desc: 考试管理，处理报名，报名日志，备注允许为空
 */
ALTER TABLE aeAppnCommhistory MODIFY ach_content NULL;
commit;

/**
Auth: lucky
Date: 2016-06-15
Desc: InstructorInf表之前没有将iti_tcr_id数据插入，现在将之前为null的都改成1
 */
update InstructorInf set iti_tcr_id =1 where iti_tcr_id is null;
commit;

/**
 * Desc: 系统数据统计表，实时统计（注：一开始是用线程更新，后来改为实时统计）
 * Author:andrew.xiao
 * Date : 2016-5-25
 */
CREATE TABLE SystemStatistics (
	ssc_eip_tcr_id int NOT NULL,
	ssc_update_time timestamp NOT NULL,
	ssc_web_base_couse_count int DEFAULT (0),
	ssc_classroom_course_count int DEFAULT (0),
	ssc_integrated_course_count int DEFAULT (0),
	ssc_web_base_exam_count int DEFAULT (0),
	ssc_classroom_exam_count int DEFAULT (0),
	ssc_open_course_count int DEFAULT (0),
	ssc_special_topic_count int DEFAULT (0),
	ssc_admin_know_share_count int DEFAULT (0),
	ssc_learner_know_share_count int DEFAULT (0),
	ssc_user_online_count int DEFAULT (0),
	ssc_user_count int DEFAULT (0),
	ssc_user_group_count int DEFAULT (0),
	ssc_mobile_app_user_count int DEFAULT (0),
	ssc_wechat_user_count int DEFAULT (0)
);
alter table SystemStatistics allocate extent;
commit;

/**
 * Desc: 更新supplier添加培训中心ID
 * Author:lucky.liang
 * Date : 2016-5-31
 */
alter table supplier add spl_tcr_id int
commit;


/**
Auth: Bill
Date: 2016-06-23
Desc: 问答添加未分类 类型
*/
insert into knowCatalog(kca_id,kca_tcr_id,kca_title,kca_type,kca_public_ind,kca_que_count,kca_create_usr_id,kca_create_timestamp,kca_update_usr_id,kca_update_timestamp) 
  values(-1,1,N'未分类','CATALOG',1,0,'s1u3',sysdate,'s1u3',sysdate);
commit;

/**
Auth: lucky
Date: 2016-06-23
Desc: 插入初始化数据到SystemSetting，用于记录禁止上传的文件格式
 */
INSERT INTO SystemSetting 
(sys_cfg_type , sys_cfg_value , sys_cfg_create_timestamp , sys_cfg_create_usr_id ,
sys_cfg_update_timestamp,sys_cft_update_usr_id)
VALUES('SYS_UPLOAD_FORBIDDEN','jsp,asp,exe,bat,bin,php,sys,com,Mach-O,ELF,dll,reg',
to_date('2016-06-20 15:08:00','yyyy-mm-dd hh24:mi:ss') , 's1u3' , to_date('2016-06-20 15:08:00','yyyy-mm-dd hh24:mi:ss') , 's1u3');
commit;

/**
 * andrew
 * 2016-07-5
 * 修改表voting中部分字段的数据类型，解决添加投票报错的问题
 * 注意：添加投票没报错的环境，不用执行此SQL，因为下面的SQL在之前的SVN版本已经提及了，之后被其他人提交的版本给覆盖了，
 * 所以新部署的环境都有问题
 */
alter table voting drop column vot_content;
alter table voting add vot_content nclob;

alter table votequestion drop column vtq_contnet;
alter table votequestion add vtq_contnet nclob;

/**
Auth: andrew.xiao
Date: 2016-07-25
Desc: 课程表添加是否发布到移动端的标志
 */
alter table aeItem add itm_mobile_ind varchar2(5);


/**
Auth: andrew.xiao
Date: 2016-07-26
Desc: 课程表添加是否发布到移动端的标志，需要更新之前的数据
 */
update aeItem set itm_mobile_ind = 'no';		
update aeItem set itm_mobile_ind = 'yes'
where itm_id in
(
	select cos_itm_id from Course where cos_res_id in
	(
		select rcn_res_id from ResourceContent inner join Resources on res_id = rcn_res_id_content
		INNER JOIN Module On ( rcn_res_id_content = mod_res_id )
		where res_status = 'ON' and mod_mobile_ind = 1
	)
	
	union all
	
	select
		parent.itm_id
		from aeItem itm
		join aeItemRelation on itm.itm_id = ire_child_itm_id
		join aeItem parent on parent.itm_id = ire_parent_itm_id
		where
		itm.itm_id in
		(
		        select cos_itm_id from Course where cos_res_id in
				(
					select rcn_res_id from ResourceContent inner join Resources on res_id = rcn_res_id_content
					INNER JOIN Module On ( rcn_res_id_content = mod_res_id )
					where res_status = 'ON' and mod_mobile_ind = 1
				)
		)
		
	union all
	
	select itm_id from aeItem where itm_integrated_ind = 1
);
commit;

/**
Auth: andrew.xiao
Date: 2016-07-29
Desc: 将公开课“是否发布到移动端”都默认为“yes”
 */
update aeItem set itm_mobile_ind = 'yes' where itm_ref_ind = 1;
commit;

/**
Auth: lucky
Date: 2016-08-1
Desc: 插入初始化数据到SystemSetting，用于整合i doc view 预览插件
 */
INSERT INTO SystemSetting 
(sys_cfg_type , sys_cfg_value , sys_cfg_create_timestamp , sys_cfg_create_usr_id ,
sys_cfg_update_timestamp,sys_cft_update_usr_id)
VALUES('SYS_I_DOC_VEIW_HOST','http://127.0.0.1/',
to_date('2016-06-20 15:08:00','yyyy-mm-dd hh24:mi:ss') , 's1u3' , to_date('2016-06-20 15:08:00','yyyy-mm-dd hh24:mi:ss') , 's1u3');

INSERT INTO SystemSetting 
(sys_cfg_type , sys_cfg_value , sys_cfg_create_timestamp , sys_cfg_create_usr_id ,
sys_cfg_update_timestamp,sys_cft_update_usr_id)
VALUES('SYS_I_DOC_VIEW_PREVIEW_FILE_HOST','http://localhost:8080/',
to_date('2016-06-20 15:08:00','yyyy-mm-dd hh24:mi:ss') , 's1u3' , to_date('2016-06-20 15:08:00','yyyy-mm-dd hh24:mi:ss') , 's1u3');

INSERT INTO SystemSetting 
(sys_cfg_type , sys_cfg_value , sys_cfg_create_timestamp , sys_cfg_create_usr_id ,
sys_cfg_update_timestamp,sys_cft_update_usr_id)
VALUES('SYS_I_DOC_VIEW_TOKEN','cyberwisdomsupport',
to_date('2016-06-20 15:08:00','yyyy-mm-dd hh24:mi:ss') , 's1u3' , to_date('2016-06-20 15:08:00','yyyy-mm-dd hh24:mi:ss') , 's1u3');

INSERT INTO SystemSetting 
(sys_cfg_type , sys_cfg_value , sys_cfg_create_timestamp , sys_cfg_create_usr_id ,
sys_cfg_update_timestamp,sys_cft_update_usr_id)
VALUES('SYS_I_DOC_VIEW_PREVIEW_IP','',
to_date('2016-06-20 15:08:00','yyyy-mm-dd hh24:mi:ss') , 's1u3' , to_date('2016-06-20 15:08:00','yyyy-mm-dd hh24:mi:ss') , 's1u3');

commit;

/**
Auth: andrew.xiao
Date: 2016-09-3
Desc: app推送，客户端记录表
 */
CREATE TABLE appClient(
	mobileInd varchar(10) NOT NULL,
	usrEntId int NULL,
	clientId varchar(100) NULL,
	appId varchar(50) NULL,
	status varchar(10) NULL
);
alter table appClient allocate extent;



/**
Auth: Randy
Date: 2016-09-6
Desc: 把专题培训放到“培训管理下”
 */
update acfunction set ftn_parent_id = (select ftn_id from acfunction where ftn_ext_id ='FTN_AMD_TRAINING_MGT'), ftn_order=31 where ftn_ext_id = 'FTN_AMD_SPECIALTOPIC_MAIN' ;
commit;

/**
Auth: Randy
Date: 2016-09-6
Desc: 删除培训管理角色下的“PC样式管理，移动样式管理”两个功能, 即培训管理默认没有两个功能的操作权限。
     对于LNOW模式下的环境，如果原来已有数据，请不要执行句SQL。

 */
delete from acrolefunction where rfn_rol_id = (select rol_id from acrole where rol_ext_id = 'TADM_1') 
and rfn_ftn_id in(
select ftn_id from acfunction where ftn_ext_id ='FTN_AMD_POSTER_MAIN' or ftn_ext_id ='FTN_AMD_MOBILE_POSTER_MAIN'
)

/**
Auth: Randy
Date: 2016-09-6
Desc: 更新邮件模板，由于邮件模板有变更，所以需求执行以下操作更新数据库中的邮件模板。

 */

   初始化邮件内容，在浏览器地址栏输入以下URL：
   http://host:port/app/admin/role/initEmail?dirPath=D:\dev\CORE\trunk\test\com\cwn\wizbank\resources\email\
   注：dirPath： 指向patch中的initData 目录下存放初始邮件内容文件目录


/**
Auth: Leaf
Date: 2016-09-27
Desc: 屏蔽“项目式培训”。
 */
update aeItemType set ity_owner_ent_id = 0 where ity_id = 'INTEGRATED';

/**
Auth: Randy
Date: 2016-10-6
Desc: 把积分管理放到“社区管理下”
 */
   
   update acfunction set ftn_parent_id = (select ftn_id from acfunction where ftn_ext_id ='FTN_AMD_SNS_MGT'), ftn_order=31 where ftn_ext_id = 'FTN_AMD_CREDIT_SETTING_MAIN' ;
commit


/**
Auth: bill
Date: 2016-10-13
Desc: 更改課程表圖片名字字段的長度
 */
alter table aeitem modify(itm_icon nvarchar2(200));
commit;


/**
Auth: Randy
Date: 2016-10-6
      把讲师管理从系统管理员下删除。
 */
delete from acrolefunction where rfn_rol_id = (select rol_id from acrole where rol_ext_id = 'ADM_1') 
and rfn_ftn_id in(
select ftn_id from acfunction where ftn_ext_id ='FTN_AMD_BASE_DATA_MGT'
);

/**
Auth: Randy
Date: 2016-10-12
Desc: 视始化角色权限 用邮件。

 */
      启动平台后，用admin 帐号登录到平台，用切换到“系统管理员角色”，然后执行以下两个操作。
      注意： 如果人原来的数据库中已有自定议的角色，请执行完这里两个请求后，到解色管理中为自定义好的角色重新分配置权限。
	  
   由于各个角色的默认权限有变更，所以需要在备份空数据库时，在空数库下启动平台然后在URL中输入以下URL初始下角色权限：
   http://host:port/app/admin/role/initRolePermission?dirPath=D:\dev\CORE\trunk\test\com\cwn\wizbank\resources\copy\
   注：dirPath： 指向patch中的initData 目录下存放初始权限文件目录
   
  初始化邮件内容，在浏览器地址栏输入以下URL：
   http://host:port/app/admin/role/initEmail?dirPath=D:\dev\CORE\trunk\test\com\cwn\wizbank\resources\email\
   注：dirPath： 指向patch中的initData 目录下存放初始邮件内容文件目录
   
   
·


/**
Auth: BILL
Date: 2016-10-14
Desc: 增加api消费积分。
 */
INSERT INTO creditsType
(cty_code, cty_title, cty_deduction_ind, cty_manual_ind, cty_deleted_ind, 
cty_relation_total_ind,cty_relation_type,cty_default_credits_ind, cty_default_credits,
 cty_create_usr_id, cty_create_timestamp, cty_update_usr_id, cty_update_timestamp, cty_tcr_id) 
VALUES 
('API_UPDATE_CREDITS','API_UPDATE_CREDITS','1','1','0','1','API','0','0','s1u3',sysdate,'s1u3',sysdate,1);



 /** 
 * 在emailMessage表增加emsg_itm_id（课程的id）字段
 * archer
 * 2016-10-11
 */
alter table emailMessage add emsg_itm_id int null;


/** 
 * 资讯表中art_content字段类型修改为nclob
 * leaf
 * 2016-11-4
 */
alter table article
add temporary nclob default '0' not null;
commit;

update article set temporary = art_content;
commit;

alter table article
drop column art_content;
commit;

alter table article rename column temporary to art_content;
commit;

/**
Auth: Leaf
Date: 2016-11-14
Desc: 问答管理分类数据维护。
 */	 
   启动平台后，用切换到拥有“问答管理”权限的角色，然后执行以下操作。	 
   
   问答管理模块中，旧数据，在处于二级类别下的数据，可能在关系表中没有关联到一级类别，
   导致在一级类别查询不出处于该类别下子类别下的问题内容。
   现在修复了处于二级类别下的数据，在关系表中，既与一级类别关联，也与二级类别关联。
   
   http://host:port/app/admin/know/maintenance

   
   
   
   
/**
Auth: Randy
Date: 2016-11-19
Desc: AICC 和scorm课件最大分及合格分可以邮管理员修改。及所引的逻辑及页面修改。
 */	 
 update DisplayOption set dpo_max_score_ind = 1, dpo_pass_score_ind = 1
 where dpo_res_type = 'MOD' and (dpo_res_subtype='AICC_AU' or dpo_res_subtype='SCO');
 commit;
  

 
 /**
 * Auth: andrew.xiao
 * Date: 2016-11-23
 * Desc:emailMessage 和 webMessage的中间关联表
 */ 
CREATE TABLE emsgRwmsg(
	emsg_id int NOT NULL,
	wmsg_id int NOT NULL
);
alter table emsgRwmsg allocate extent;
commit;




/**
Auth: LEAF
Date: 2016-10-28
Desc: 提示信息维护功能
*/
DROP TABLE helpQuestionType;

create table helpQuestionType
(
    hqt_id int NOT NULL,
    hqt_type_name nvarchar2(100) NOT NULL,
	hqt_pid int NOT NULL,
	hqt_top_index int NOT NULL,
	hqt_is_publish int ,
	hqt_create_timestamp timestamp NOT NULL,
	hqt_language nvarchar2(50) NOT NULL
)
ALTER TABLE helpQuestionType ADD 
    CONSTRAINT PK_helpQuestionType PRIMARY KEY   
    (
        hqt_id
    );
commit;

DROP SEQUENCE helpQuestionType_seq;
DROP TRIGGER helpQuestionType_trigger;

create sequence helpQuestionType_seq;  
CREATE OR REPLACE TRIGGER helpQuestionType_trigger BEFORE INSERT ON helpQuestionType
FOR EACH ROW
WHEN (new.hqt_id IS NULL)
BEGIN
 SELECT helpQuestionType_seq.NEXTVAL INTO :new.hqt_id FROM DUAL;
END;  
commit;
	


DROP TABLE helpQuestion;

CREATE TABLE helpQuestion(
	hq_id int NOT NULL,
	hq_type_id int NOT NULL,
    hq_title nvarchar2(255) NOT NULL,
	hq_content_cn nclob  NULL,
	hq_create_timestamp timestamp NOT NULL,
	hq_update_timestamp timestamp NULL,
	hq_top_index int NOT NULL,
	hq_width int NOT NULL,
	hq_height int NOT NULL,
	hq_Template nvarchar2(50) NOT NULL,
	hqt_number nvarchar2(50) NULL,
	hq_content_us nclob NULL
)



ALTER TABLE helpQuestion ADD 
    CONSTRAINT PK_helpQuestion PRIMARY KEY   
    (
        hq_id
    );
commit;

DROP SEQUENCE helpQuestion_seq;
DROP TRIGGER helpQuestion_trigger;

create sequence helpQuestion_seq;  
CREATE OR REPLACE TRIGGER helpQuestion_trigger BEFORE INSERT ON helpQuestion
FOR EACH ROW
WHEN (new.hq_id IS NULL)
BEGIN
 SELECT helpQuestion_seq.NEXTVAL INTO :new.hq_id FROM DUAL;
END;  
commit;


/**
Auth: lucky
Date: 2016-11-15
Desc: 重要对像详细操作日志数据表
 */	 
CREATE TABLE objectActionLog (
  object_id int NOT NULL,
  object_code nvarchar2(255) NULL,
  object_title nvarchar2(255) NULL,
  object_type nvarchar2(50) NOT NULL,
  object_action nvarchar2(50) NOT NULL,
  object_action_type nvarchar2(50) NOT NULL,
  object_action_time TIMESTAMP NOT NULL,
  object_opt_user_id int NOT NULL
) 
commit;

CREATE TABLE gourpLoginReport(
  gplr_grp_id int NOT NULL,  
  gplr_year int NOT NULL,     
  gplr_month int NOT NULL,
  gplr_login_mode int NOT NULL, 
  gplr_totle_login_number int NOT NULL,
  gplr_last_update_date TIMESTAMP NOT NULL
); 
commit;

CREATE TABLE gradeLoginReport(
  gdlr_ugr_id int NOT NULL,  
  gdlr_year int NOT NULL,     
  gdlr_month int NOT NULL,
  gdlr_login_mode int NOT NULL, 
  gdlr_totle_login_number int NOT NULL,
  gdlr_last_update_date TIMESTAMP NOT NULL
);
commit;

CREATE TABLE positionLoginReport(
  pslr_upt_id int NOT NULL,   
  pslr_year int NOT NULL,     
  pslr_month int NOT NULL,
  pslr_login_mode int NOT NULL, 
  pslr_totle_login_number int NOT NULL,
  pslr_last_update_date TIMESTAMP NOT NULL  
);
commit;

/**
Auth: andrew
Date: 2016-11-16
Desc: 系统设置添加是否允许多账号登录
 */	 
 INSERT INTO SystemSetting 
(sys_cfg_type , sys_cfg_value , sys_cfg_create_timestamp , sys_cfg_create_usr_id ,
sys_cfg_update_timestamp,sys_cft_update_usr_id)
VALUES('MULTIPLE_LOGIN_IND','1',
sysdate , 's1u3' , sysdate , 's1u3');
commit;

/**Auth: andrew.xiao
 * Date : 2016-11-28
 * Desc: 保存用户密码历史记录
 */
CREATE TABLE UserPasswordHistory(
  uph_usr_ent_id int NOT NULL, --用户ID
  uph_pwd nvarchar2(30) NOT NULL, --用户历史密码（已加密的）
  uph_update_usr_ent_id int NOT NULL, --修改人id
  uph_client_type nvarchar2(10) NOT NULL, --PC或者Mobile
  uph_create_time timestamp NOT NULL --创建时间
);
alter table UserPasswordHistory allocate extent;
commit;

/**Auth: andrew.xiao
 * Date : 2016-11-28
 * Desc: 系统设置，密码安全策略
 */
INSERT INTO SystemSetting 
(sys_cfg_type , sys_cfg_value , sys_cfg_create_timestamp , sys_cfg_create_usr_id ,
sys_cfg_update_timestamp,sys_cft_update_usr_id)
VALUES('PASSWORD_POLICY_COMPARE_COUNT','',
sysdate , 's1u3' , sysdate , 's1u3');

INSERT INTO SystemSetting 
(sys_cfg_type , sys_cfg_value , sys_cfg_create_timestamp , sys_cfg_create_usr_id ,
sys_cfg_update_timestamp,sys_cft_update_usr_id)
VALUES('PASSWORD_POLICY_PERIOD','',
sysdate , 's1u3' , sysdate , 's1u3');

INSERT INTO SystemSetting 
(sys_cfg_type , sys_cfg_value , sys_cfg_create_timestamp , sys_cfg_create_usr_id ,
sys_cfg_update_timestamp,sys_cft_update_usr_id)
VALUES('PASSWORD_POLICY_PERIOD_FORCE','',
sysdate , 's1u3' , sysdate , 's1u3');
commit;




/*
Auth: bill
Date: 2016-10-27（更新于12-13）
Desc:直播管理
*/
/*
Auth: bill
Date: 2016-10-27（更新于12-13）
Desc:直播管理
*/
--删除直播管理权限（没有执行过直播相关的sql。不用执行）
declare live_old_ftn_id int;
begin
  select ftn_id into live_old_ftn_id from acFunction where ftn_ext_id='FTN_AMD_LIVE_MAIN';
  delete acRoleFunction where rfn_ftn_id = live_old_ftn_id;
  delete acFunction where ftn_ext_id = 'FTN_AMD_LIVE_MAIN';
  commit;
end;


--增加直播管理权限
declare max_ftn_order_id int;
begin
  select max(ftn_order) + 1 into max_ftn_order_id from acFunction;
  insert into acFunction (ftn_ext_id, ftn_level, ftn_creation_timestamp,ftn_assign, ftn_tc_related,ftn_status,ftn_order)
  values ('FTN_AMD_LIVE_MAIN', 0, sysdate, 1, 'Y', 1, max_ftn_order_id);
  commit;
end;


--默认把直播功能的权限分给“培训管理员”
declare tadm_rol_id int;
    live_ftn_id int;
    live_ftn_order int;
begin
  select rol_id into tadm_rol_id from acRole where rol_ext_id = 'TADM_1';
  select ftn_id into live_ftn_id from acFunction where ftn_ext_id='FTN_AMD_LIVE_MAIN';
  select ftn_order into live_ftn_order from acFunction where ftn_ext_id='FTN_AMD_LIVE_MAIN';
  
  insert into acRoleFunction values(tadm_rol_id, live_ftn_id, 's1u3', sysdate, 0, live_ftn_order,null);
  commit;
end;


--判断是否存在相关表
declare
      num number;
begin
    select count(1) into num from user_tables where table_name = upper('liveItem');
    if num > 0 then
        execute immediate 'drop table liveItem' ;
    end if;
    num := 0;
    select count(1) into num from user_tables where table_name = upper('liveRecords');
    if num > 0 then
        execute immediate 'drop table liveRecords' ;
    end if;
    num := 0;  
    ----判断序列 SEQ_LIVEITEM 是否存在（区分大小写）
    select count(0) into num from user_sequences where sequence_name = 'SEQ_LIVEITEM'; 
    ----如果存在立即删除  
    if num > 0 then   
    	execute immediate 'DROP SEQUENCE SEQ_LIVEITEM';   
    end if;
    commit;
end;

/**
 * Auth: bill
 * Date: 2016-10-27（更新于12-13）
 * 直播
 */
create table liveItem  
(  
  lv_id int,  --ID
  lv_title nvarchar2(50),          --标题
  lv_create_datetime TIMESTAMP,  --创建时间
  lv_start_datetime TIMESTAMP,  --开始时间
  lv_end_datetime TIMESTAMP,  --结束时间
  lv_create_usr_id nvarchar2(20), --创建人
  lv_upd_datetime TIMESTAMP, --更新时间
  lv_upd_usr_id nvarchar2(20), --更新人ID
  lv_url nvarchar2(500),
  lv_desc nvarchar2(1000), --简介
  lv_pwd nchar(20),  -- 直播密码
  lv_webinar_id int, --活动ID
  lv_record_id int, --回放ID
  lv_remark nvarchar2(500), --备注
  lv_instr_desc nvarchar2(500), --讲师简介
  lv_image nvarchar2(100),  --图片
  lv_status nvarchar2(20), --状态
  lv_real_start_datetime TIMESTAMP, --真实开播时间
  lv_need_pwd int,--是否需要密码
  lv_tcr_id int,--培训中心ID
  lv_people_num int, --限制人数
  lv_type int, --状态（2-预告 1-直播中 3-已结束）
  lv_had_live int --是否直播过    
);
commit;
--创建序列
create sequence SEQ_LIVEITEM minvalue 1 nomaxvalue start with 1 increment by 1 nocycle nocache;
--创建触发器
CREATE OR REPLACE TRIGGER TRI_LIVEITEM
BEFORE INSERT ON LIVEITEM FOR EACH ROW WHEN (NEW.LV_ID is null)
begin
select SEQ_LIVEITEM.nextval into :NEW.LV_ID from dual;
end;

/**
 * Auth: bill
 * Date: 2016-10-27（更新于12-13）
 * 直播观看记录
 */
create table liveRecords
(
  lr_usr_id  nvarchar2(20),  --观看人ID
  lr_create_time  TIMESTAMP,  --记录创建时间
  lr_live_id  int,  --直播ID
  lr_status int--状态
)
commit;

/*
Auth: leaf
Date: 2016-12-01 （更新于12-13）
Desc:系统运作日志
*/
--删除系统运作日志功能（没有执行过系统运作日志相关的sql。不用执行）
declare rftn_ftn_id int;
begin
  select ftn_id into rftn_ftn_id from acFunction where ftn_ext_id='FTN_AMD_SYS_SETTING_LOG';
  delete acRoleFunction where rfn_ftn_id = rftn_ftn_id;
  delete acFunction where ftn_ext_id = 'FTN_AMD_SYS_SETTING_LOG';
  commit;
end;

--新增系统运作日志功能
declare ftn_id_parent int;
        ftn_order int;
begin

select ftn_id into ftn_id_parent from acFunction where ftn_ext_id = 'FTN_AMD_SYSTEM_SETTING_MGT';
select max(ftn_order)+1 into ftn_order from acFunction;
INSERT into acFunction (ftn_ext_id, ftn_level, ftn_creation_timestamp,ftn_assign, ftn_tc_related,ftn_status,ftn_order,ftn_parent_id)
VALUES ('FTN_AMD_SYS_SETTING_LOG', 1, sysdate, 0, 'N', 1,ftn_order, ftn_id_parent);

INSERT into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_usr_id, RFN_CREATE_TIMESTAMP, rfn_ftn_parent_id, rfn_ftn_order) 
select rol_id, child.ftn_id, 's1u3', sysdate,parent.ftn_id, 1  from acRole, acFunction child, acFunction parent
where rol_ext_id like 'ADM_%' and child.ftn_ext_id = 'FTN_AMD_SYS_SETTING_LOG' and parent.ftn_ext_id = 'FTN_AMD_SYSTEM_SETTING_MGT';
commit;
end;

/**
 * Auth: leaf
 * Date: 2016-12-13
 * 提示信息内容表 修改hq_type_id,hq_content_cn字段允许为空
 */
alter table helpQuestion modify hq_type_id null;
alter table helpQuestion modify hq_content_cn  null;
commit;

/**
 * Auth: leaf
 * Date: 2016-12-14
 * 用户默认密码  初始：abc123
 */
INSERT INTO SystemSetting 
(sys_cfg_type , sys_cfg_value , sys_cfg_create_timestamp , sys_cfg_create_usr_id ,
sys_cfg_update_timestamp,sys_cft_update_usr_id)
VALUES('SYS_DEFAULT_USER_PASSWORD','abc123',
sysdate , 's1u3' , sysdate , 's1u3');
commit;


/**
 * Auth: bill
 * Date: 2016-12-15
 * 新增列：eip_live_max_count；直播并发数
 */
ALTER TABLE EnterpriseInfoPortal ADD eip_live_max_count int;
commit;

/**
 * Auth: leaf
 * Date: 2016-12-19
 * 修改提示信息表字段类型。（未执行或在2016-12-19之后执行提示信息维护功能相关sql的不用执行。）
 */
alter table helpQuestion
add (temporary_cn nclob default '0' null,
     temporary_us nclob default '0'  null);
commit;

update helpQuestion set temporary_cn = HQ_CONTENT_CN;
update helpQuestion set temporary_us = HQ_CONTENT_US;
commit;
--注意：执行到这来，请检查temporary_cn,temporary_us是否已经保存了HQ_CONTENT_CN,HQ_CONTENT_CN的内容。

alter table helpQuestion
drop (HQ_CONTENT_CN,HQ_CONTENT_US);
commit;

alter table helpQuestion rename column temporary_cn to HQ_CONTENT_CN;
alter table helpQuestion rename column temporary_us to HQ_CONTENT_US;
commit;




/**
 * Auth: Randy
 * Date: 2016-12-28
 * 把未分类改新为英文
 */
update knowcatalog set kca_title = 'Unspecified' where kca_id = -1;
commit;	   


/**
 * Auth: lucky
 * Date: 2017-1-19
 * 修改角色管理中课程管理和考试管理下的所有菜单字体颜色改成蓝色.	
 */
update acFunction set ftn_tc_related = 'Y' where  ftn_parent_id = (select distinct(ftn_id)  from acFunction where ftn_ext_id = 'FTN_AMD_EXAM_MGT');
update acFunction set ftn_tc_related = 'Y' where  ftn_parent_id = (select distinct(ftn_id)  from acFunction where ftn_ext_id = 'FTN_AMD_ITM_COS_MAIN');
commit;

/**
 * Auth: lucky
 * Date: 2017-2-15
 * 修改knowQuestion表里的QUE_CONTENT长度为2000
 */
alter table knowQuestion modify (QUE_CONTENT NVARCHAR2(2000));
commit;


/**
 * Auth: Archer
 * Date: 2017-2-16
 * 关闭Learner activity report (old) link、learning activity report by course link、learning activity report by learner link
 */
update ReportTemplate set rte_owner_ent_id = 0 where rte_type in ('LEARNER','LEARNING_ACTIVITY_BY_COS','LEARNING_ACTIVITY_LRN');
commit;



/**
 * Auth: Archer
 * Date: 2017-2-28
 * 修改link
 */
update messageTemplate set mtp_content_email_link = 'app/user/userLogin/$?course=' where mtp_type in ('ENROLLMENT_CONFIRMED','ENROLLMENT_NEW','ENROLLMENT_NEXT_APPROVERS')
commit;

update messageTemplate set mtp_content_pc_link = '/app/course/detail/'  where mtp_type in ('ENROLLMENT_CONFIRMED','ENROLLMENT_NEW')
commit;




/**
 * Auth: Archer
 * Date: 2017-3-1
 * 修改邮件模板内容；
 */
 update messageTemplate set mtp_content = '  [Header image]<p>	Dear&nbsp;<span style="color:blue;">[Learner name]</span>,</p><p align="left">	Thank you for your recent enrollment of the course<span style="color:blue;">&nbsp;</span><span style="color:blue;">[Course name]([Course code])</span>. We have reviewed and approved your enrollment request. You can start your course by logging into the system using the link below:</p><p align="left">	<span style="color:blue;">[Link to login page]</span></p><p align="left">	Regards,<br />Training &amp; Development&nbsp;&nbsp;</p><span></span><span>[Footer image]</span> ' 
 where mtp_type = 'ENROLLMENT_CONFIRMED';
 commit;

update messageTemplate set mtp_content = ' [Header image]<p>	Dear&nbsp;<span style="color:#337FE5;"><span style="color:#4C33E5;">[Learner name]</span><span style="color:#4C33E5;">,</span></span> </p><p>	Thank you for your interest in applying for the course<span style="color:#4C33E5;">&nbsp;[Course name]&nbsp;</span><span style="color:#4C33E5;">([Course code]).&nbsp;</span>We are reviewing your enrollment request. Once the enrollment is confirmed. You will receive the confirmation by email.&nbsp;</p><p>	Regards,<br />Training &amp; Development</p><span></span> <p>	<span>[Footer image]</span> </p><p>	<br /></p>' 
where mtp_type = 'ENROLLMENT_NEW';
commit;

·
/**
 * Auth: bill
 * Date: 2017-03-06
 * 系统管理员，运行参数，上传文件约束，加一个默认不允许文件jspx在数据库中
 */
update  SystemSetting set sys_cfg_value ='jsp,asp,exe,bat,bin,php,sys,com,Mach-O,ELF,dll,reg,jspx,aspx' where sys_cfg_type= 'SYS_UPLOAD_FORBIDDEN';
commit;

·
/**
 * Auth: randy
 * Date: 2017-3-9
 * 修改reguser表里的密码长度为255
 */
alter table reguser modify (usr_pwd NVARCHAR2(255));
commit;

/**
 * Auth: bill
 * Date: 2017-3-21
 * Bug 10494 - newbie面授课程测验模块删不了
 */
update Resources set res_status='OFF' where res_id in (
	select res_id from Resources where res_type = 'MOD' and res_status = 'ON' and RES_ID IN(
	SELECT rcn_res_id_content from ResourceContent where rcn_res_id in(select cos_res_id from aeItem,Course where cos_itm_id = itm_id and itm_create_run_ind = 1))
)

/**
 * Auth: bill
 * Date: 2017-3-22
 * 更改报名成功邮件移动端的访问地址
 */
update messageTemplate set mtp_content_mobile_link = 'javascript:clicked(''../learning/courseCatalog.html?show=myCourse'',true)' where mtp_type = 'ENROLLMENT_CONFIRMED';
commit;
		  

/**
 * Auth: lucky
 * Date: 2017-2-14
 * 修改重要日志添加操作者登陆时间，IP
 */
alter table objectActionLog add object_opt_user_login_time TIMESTAMP NULL;
alter table objectActionLog add object_opt_user_login_ip nvarchar2(100) NULL;
commit;


/**
 * Auth: leaf
 * Date: 2017-2-16
 * 只用于在2017年2月运行的6.5版本。
 * 用户登录日志数据表为每月初时自动创建，相对与6.3版本，6.5新增了登录状态和登录ip。
 * 如果使用的数据库运行了6.3版本，则需要删除对应月份的用户登录日志数据表。
 * 运行6.5时会自动创建新的表。
 */
BEGIN 
    EXECUTE IMMEDIATE 'DROP TABLE loginLog20172'; 
    EXCEPTION WHEN OTHERS THEN NULL;
    END;
commit;

/**
 * Auth: leaf
 * Date: 2017-03-07
 * 新增CPT/D 管理 功能
 */
--新增一级功能  CPT/D 管理  
declare ftn_order int;
begin

select max(ftn_order) into ftn_order from acFunction where ftn_level = 0;

INSERT into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_tc_related, ftn_status, ftn_assign, ftn_order, ftn_parent_id) 
VALUES ('FTN_AMD_CPT_D_MGT', '0', NULL, sysdate, 'N', '1', '1', ftn_order+1, null);
commit;
end;

--新增4个二级功能
--CPT/D 牌照管理 --CPT/D 牌照注册管理 --历史保存记录 --CPT/D报表备注维护

declare ftn_order int;
        ftn_id_parent int;
begin

select max(ftn_order) into ftn_order from acFunction where ftn_level = 0;
select ftn_id  into ftn_id_parent  from acFunction where ftn_ext_id = 'FTN_AMD_CPT_D_MGT';

INSERT into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_tc_related, ftn_status, ftn_assign, ftn_order, ftn_parent_id)  
VALUES ('FTN_AMD_CPT_D_LIST', '1', NULL, sysdate, 'N', '1', '1', ftn_order+1, ftn_id_parent);

INSERT into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_tc_related, ftn_status, ftn_assign, ftn_order, ftn_parent_id)  
VALUES ('FTN_AMD_CPT_D_LICENSE_LIST', '1', NULL, sysdate, 'N', '1', '1', ftn_order+2, ftn_id_parent);

INSERT into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_tc_related, ftn_status, ftn_assign, ftn_order, ftn_parent_id)  
VALUES ('FTN_AMD_CPT_D_HISTORY', '1', NULL, sysdate, 'N', '1', '1', ftn_order+3, ftn_id_parent);

INSERT into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_tc_related, ftn_status, ftn_assign, ftn_order, ftn_parent_id)  
VALUES ('FTN_AMD_CPT_D_NOTE', '1', NULL, sysdate, 'N', '1', '1', ftn_order+4, ftn_id_parent);
commit;
end;

--CPT/D 管理功能，及其四个下级子功能默认分配给“系统管理员”角色
INSERT into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_usr_id, RFN_CREATE_TIMESTAMP, rfn_ftn_parent_id, rfn_ftn_order) 
select rol_id, child.ftn_id, 's1u3', sysdate,null, 1  from acRole, acFunction child, acFunction parent
where rol_ext_id like 'ADM_%' and child.ftn_ext_id = 'FTN_AMD_CPT_D_MGT' and parent.ftn_ext_id = 'FTN_AMD_CPT_D_MGT';


INSERT into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_usr_id, RFN_CREATE_TIMESTAMP, rfn_ftn_parent_id, rfn_ftn_order) 
select rol_id, child.ftn_id, 's1u3', sysdate,parent.ftn_id, 1  from acRole, acFunction child, acFunction parent
where rol_ext_id like 'ADM_%' and child.ftn_ext_id = 'FTN_AMD_CPT_D_LIST' and parent.ftn_ext_id = 'FTN_AMD_CPT_D_MGT';


INSERT into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_usr_id, RFN_CREATE_TIMESTAMP, rfn_ftn_parent_id, rfn_ftn_order) 
select rol_id, child.ftn_id, 's1u3', sysdate,parent.ftn_id, 1  from acRole, acFunction child, acFunction parent
where rol_ext_id like 'ADM_%' and child.ftn_ext_id = 'FTN_AMD_CPT_D_LICENSE_LIST' and parent.ftn_ext_id = 'FTN_AMD_CPT_D_MGT';


INSERT into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_usr_id, RFN_CREATE_TIMESTAMP, rfn_ftn_parent_id, rfn_ftn_order) 
select rol_id, child.ftn_id, 's1u3', sysdate,parent.ftn_id, 1  from acRole, acFunction child, acFunction parent
where rol_ext_id like 'ADM_%' and child.ftn_ext_id = 'FTN_AMD_CPT_D_HISTORY' and parent.ftn_ext_id = 'FTN_AMD_CPT_D_MGT';


INSERT into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_usr_id, RFN_CREATE_TIMESTAMP, rfn_ftn_parent_id, rfn_ftn_order) 
select rol_id, child.ftn_id, 's1u3', sysdate,parent.ftn_id, 1  from acRole, acFunction child, acFunction parent
where rol_ext_id like 'ADM_%' and child.ftn_ext_id = 'FTN_AMD_CPT_D_NOTE' and parent.ftn_ext_id = 'FTN_AMD_CPT_D_MGT';
commit;



/**
 * Auth: bill
 * Date: 2017-02-13
 * 新增列：eip_live_mode；直播模式
 */
alter table EnterpriseInfoPortal add eip_live_mode nvarchar2(255);
commit;

/**
 * Auth: bill
 * Date: 2017-02-15
 * 新增列：lv_upstream_address；推流地址
 * 新增列：lv_hls_downstream_address；hls观看地址
 * 新增列：lv_rtmp_downstream_address；rtmp观看地址
 * 新增列：lv_flv_downstream_address；flv观看地址
 * 删除列：lv_remark
 * 删除列：lv_instr_desc
 */
alter table liveItem add lv_mode_type nvarchar2(255);
alter table liveItem add lv_upstream_address nvarchar2(255);
alter table liveItem add lv_hls_downstream_address nvarchar2(255);
alter table liveItem add lv_rtmp_downstream_address nvarchar2(255);
alter table liveItem add lv_flv_downstream_address nvarchar2(255);
alter table liveItem add lv_channel_id nvarchar2(255);
alter table liveItem drop column lv_remark;
alter table liveItem drop column lv_instr_desc;
commit;


/**
 * Auth: bill
 * Date: 2017-02-28
 * 新增列：eip_live_qcloud_secretid；腾讯云直播id
 * 新增列：eip_live_qcloud_secretkey；腾讯云直播key
 */
alter table EnterpriseInfoPortal add eip_live_qcloud_secretid nvarchar2(255);
alter table EnterpriseInfoPortal add eip_live_qcloud_secretkey nvarchar2(255);
commit;

/**
 * Auth: andrew
 * Date: 2017-03-13
 * 统计用户在线人数，添加登录标识
 */
alter table CurrentActiveUser add cau_login_type varchar2(255);


/**
 * Auth: lucky
 * Date: 2017-3-22
 * cpd功能新增表
 */
CREATE TABLE cpdType(
  ct_id int NOT NULL,
  ct_license_type nvarchar2(100) NOT NULL,
  ct_license_alias nvarchar2(255) NOT NULL,
  ct_starting_month int NOT NULL,
  ct_display_order int NOT NULL,
  ct_award_hours_type int NOT NULL,
  ct_cal_before_ind int NOT NULL,
  ct_trigger_email_type int NOT NULL,
  ct_trigger_email_month_1 int NULL,
  ct_trigger_email_date_1 int NULL,
  ct_trigger_email_month_2 int NULL,
  ct_trigger_email_date_2 int NULL,
  ct_trigger_email_month_3 int NULL,
  ct_trigger_email_date_3 int NULL,
  ct_recover_hours_period int NULL,
  ct_last_email_send_time timestamp null,
  ct_create_usr_ent_id int NOT NULL,
  ct_create_datetime timestamp NOT NULL,
  ct_update_usr_ent_id int NULL,
  ct_update_datetime timestamp NULL,
  ct_status nvarchar2(50) NOT NULL
);
commit;
ALTER TABLE cpdType ADD 
    CONSTRAINT PK_cpdType PRIMARY KEY   
    (
        ct_id
    );
commit;

create sequence cpdType_seq;  
CREATE OR REPLACE TRIGGER cpdType_trigger BEFORE INSERT ON cpdType
FOR EACH ROW
WHEN (new.ct_id IS NULL)
BEGIN
 SELECT cpdType_seq.NEXTVAL INTO :new.ct_id FROM DUAL;
END;  

ALTER TABLE cpdType ADD UNIQUE (ct_license_type);
commit;

CREATE TABLE cpdGroup(
  cg_id int NOT NULL,
  cg_code nvarchar2(100) NOT NULL,
  cg_alias nvarchar2(255) NOT NULL,
  cg_display_order int NULL,
  cg_contain_non_core_ind int NOT NULL,
  cg_display_in_report_ind int NOT NULL,
  cg_ct_id int NOT NULL,
  cg_create_usr_ent_id int NOT NULL,
  cg_create_datetime timestamp NOT NULL,
  cg_update_usr_ent_id int NULL,
  cg_update_datetime timestamp NULL,
  cg_status nvarchar2(50) NOT NULL
);
commit;

ALTER TABLE cpdGroup ADD 
    CONSTRAINT PK_cpdGroup PRIMARY KEY   
    (
        cg_id
    );
commit;

create sequence cpdGroup_seq;  
CREATE OR REPLACE TRIGGER cpdGroup_trigger BEFORE INSERT ON cpdGroup
FOR EACH ROW
WHEN (new.cg_id IS NULL)
BEGIN
 SELECT cpdGroup_seq.NEXTVAL INTO :new.cg_id FROM DUAL;
END;  

ALTER TABLE cpdGroup ADD UNIQUE (cg_code);
commit;

CREATE TABLE cpdGroupPeriod(
	cgp_id int not NULL,
	cgp_effective_time date NOT NULL,
	cgp_ct_id int NOT NULL,
	cgp_cg_id int NOT NULL,
	cgp_create_usr_ent_id int NOT NULL,
	cgp_create_datetime timestamp NOT NULL,
	cgp_update_usr_ent_id int NULL,
	cgp_update_datetime timestamp NULL,
	cgp_status nvarchar2(100) NOT NULL
 );
 commit;
 ALTER TABLE cpdGroupPeriod ADD 
    CONSTRAINT PK_cpdGroupPeriod PRIMARY KEY   
    (
        cgp_id
    );
commit;
create sequence cpdGroupPeriod_seq;  
CREATE OR REPLACE TRIGGER cpdGroupPeriod_trigger BEFORE INSERT ON cpdGroupPeriod
FOR EACH ROW
WHEN (new.cgp_id IS NULL)
BEGIN
 SELECT cpdGroupPeriod_seq.NEXTVAL INTO :new.cgp_id FROM DUAL;
END;  

CREATE TABLE cpdGroupHours(
	cgh_id int not NULL,
	cgh_cgp_id int NOT NULL,
	cgh_declare_month int NOT NULL,
	cgh_core_hours float NOT NULL,
	cgh_non_core_hours float NULL,
	cgh_create_usr_ent_id int NOT NULL,
	cgh_create_datetime timestamp NOT NULL,
	cgh_update_usr_ent_id int NULL,
	cgh_update_datetime timestamp NULL,
	cgh_status nvarchar2(50) NOT NULL
);
commit;
ALTER TABLE cpdGroupHours ADD 
    CONSTRAINT PK_cpdGroupHours PRIMARY KEY   
    (
        cgh_id
    );
commit;
create sequence cpdGroupHours_seq;  
CREATE OR REPLACE TRIGGER cpdGroupHours_trigger BEFORE INSERT ON cpdGroupHours
FOR EACH ROW
WHEN (new.cgh_id IS NULL)
BEGIN
 SELECT cpdGroupHours_seq.NEXTVAL INTO :new.cgh_id FROM DUAL;
END;  


CREATE TABLE cpdRegistration(
	cr_id int NOT NULL,
	cr_usr_ent_id int NOT NULL,
	cr_ct_id int NOT NULL,
	cr_reg_number nvarchar2(255) not NULL,
	cr_reg_datetime date NOT NULL,
	cr_de_reg_datetime date NULL,
	cr_create_usr_ent_id int NOT NULL,
	cr_create_datetime timestamp NOT NULL,
	cr_update_usr_ent_id int NULL,
	cr_update_datetime timestamp NULL,
	cr_status nvarchar2(100) NOT NULL
);
commit;
ALTER TABLE cpdRegistration ADD 
    CONSTRAINT PK_cpdRegistration PRIMARY KEY   
    (
        cr_id
    );
commit;
create sequence cpdRegistration_seq;  
CREATE OR REPLACE TRIGGER cpdRegistration_trigger BEFORE INSERT ON cpdRegistration
FOR EACH ROW
WHEN (new.cr_id IS NULL)
BEGIN
 SELECT cpdRegistration_seq.NEXTVAL INTO :new.cr_id FROM DUAL;
END;  


CREATE TABLE cpdGroupRegistration(
	cgr_id int not null,
	cgr_usr_ent_id int NOT NULL,
	cgr_cr_id int NOT NULL,
	cgr_cg_id int not NULL,
	cgr_initial_date date NOT NULL,
	cgr_expiry_date date  NULL,
	cgr_first_ind int NOT NULL,
	cgr_actual_date date NOT NULL,
	cgr_create_usr_ent_id int NOT NULL,
	cgr_create_datetime timestamp NOT NULL,
	cgr_update_usr_ent_id int NULL,
	cgr_update_datetime timestamp NULL,
	cgr_status nvarchar2(100) NULL
);
commit;
ALTER TABLE cpdGroupRegistration ADD 
    CONSTRAINT PK_cpdGroupRegistration PRIMARY KEY   
    (
        cgr_id
    );
commit;
create sequence cpdGroupRegistration_seq;  
CREATE OR REPLACE TRIGGER cpdGroupRegistration_trigger BEFORE INSERT ON cpdGroupRegistration
FOR EACH ROW
WHEN (new.cgr_id IS NULL)
BEGIN
 SELECT cpdGroupRegistration_seq.NEXTVAL INTO :new.cgr_id FROM DUAL;
END;  


CREATE TABLE cpdGroupRegHours(
	cgrh_id int not null,
	cgrh_cgp_id int  null,
	cgrh_usr_ent_id int NOT NULL,
	cgrh_cgr_id int NOT NULL,
	cgrh_cr_id int NOT NULL,
	cgrh_cgr_period int NOT NULL,
	cgrh_cal_month int not NULL,
	cgrh_cal_start_date date NOT NULL,
	cgrh_cal_end_date date NOT NULL,
	cgrh_manul_core_hours float NULL,
	cgrh_manul_non_core_hours float NULL,
	cgrh_manul_ind int NOT NULL,
	cgrh_req_core_hours float NOT NULL,
	cgrh_req_non_core_hours float  NULL,
	cgrh_execute_core_hours float  NULL,
	cgrh_execute_non_core_hours float  NULL,
	cgrh_create_usr_ent_id int NOT NULL,
	cgrh_create_datetime timestamp NOT NULL,
	cgrh_update_usr_ent_id int NULL,
	cgrh_update_datetime timestamp NULL
);
commit;
ALTER TABLE cpdGroupRegHours ADD 
    CONSTRAINT PK_cpdGroupRegHours PRIMARY KEY   
    (
        cgrh_id
    );
commit;
create sequence cpdGroupRegHours_seq;  
CREATE OR REPLACE TRIGGER cpdGroupRegHours_trigger BEFORE INSERT ON cpdGroupRegHours
FOR EACH ROW
WHEN (new.cgrh_id IS NULL)
BEGIN
 SELECT cpdGroupRegHours_seq.NEXTVAL INTO :new.cgrh_id FROM DUAL;
END;  


CREATE TABLE aeItemCPDItem(
	aci_id int not null,
	aci_itm_id int NOT NULL,
	aci_accreditation_code nvarchar2(255) NULL,
	aci_hours_end_date DATE NOT NULL,
	aci_create_usr_ent_id int NOT NULL,
	aci_create_datetime timestamp NOT NULL,
	aci_update_usr_ent_id int NULL,
	aci_update_datetime timestamp NULL
);
commit;
ALTER TABLE aeItemCPDItem ADD 
    CONSTRAINT PK_aeItemCPDItem PRIMARY KEY   
    (
        aci_id
    );
commit;
create sequence aeItemCPDItem_seq;  
CREATE OR REPLACE TRIGGER aeItemCPDItem_trigger BEFORE INSERT ON aeItemCPDItem
FOR EACH ROW
WHEN (new.aci_id IS NULL)
BEGIN
 SELECT aeItemCPDItem_seq.NEXTVAL INTO :new.aci_id FROM DUAL;
END;  


CREATE TABLE aeItemCPDGourpItem(
	acgi_id int NOT NULL,
	acgi_cg_id int NOT NULL,
	acgi_aci_id int NOT NULL,
	acgi_itm_id int NOT NULL,
	acgi_award_core_hours float NOT NULL,
	acgi_award_non_core_hours float NOT NULL,
	acgi_create_usr_ent_id int NOT NULL,
	acgi_create_datetime timestamp NOT NULL,
	acgi_update_usr_ent_id int NULL,
	acgi_update_datetime timestamp NULL
);
commit;
ALTER TABLE aeItemCPDGourpItem ADD 
    CONSTRAINT PK_aeItemCPDGourpItem PRIMARY KEY   
    (
        acgi_id
    );
commit;
create sequence aeItemCPDGourpItem_seq;  
CREATE OR REPLACE TRIGGER aeItemCPDGourpItem_trigger BEFORE INSERT ON aeItemCPDGourpItem
FOR EACH ROW
WHEN (new.acgi_id IS NULL)
BEGIN
 SELECT aeItemCPDGourpItem_seq.NEXTVAL INTO :new.acgi_id FROM DUAL;
END;  


CREATE TABLE cpdLrnAwardRecord(
	clar_id int NOT NULL,
	clar_usr_ent_id int NOT NULL,
	clar_itm_id int NOT NULL,
	clar_app_id int NOT NULL,
	clar_manul_ind int NOT NULL,
	clar_ct_id int NOT NULL,
	clar_cg_id int NOT NULL,
	clar_acgi_id int NOT NULL,
	clar_award_core_hours float NOT NULL,
	clar_award_non_core_hours float NULL,
	clar_award_datetime timestamp NOT NULL,
	clar_create_usr_ent_id int NOT NULL,
	clar_create_datetime timestamp NOT NULL,
	clar_update_usr_ent_id int NULL,
	clar_update_datetime timestamp NULL
);
commit;
ALTER TABLE cpdLrnAwardRecord ADD 
    CONSTRAINT PK_cpdLrnAwardRecord PRIMARY KEY   
    (
        clar_id
    );
commit;
create sequence cpdLrnAwardRecord_seq;  
CREATE OR REPLACE TRIGGER cpdLrnAwardRecord_trigger BEFORE INSERT ON cpdLrnAwardRecord
FOR EACH ROW
WHEN (new.clar_id IS NULL)
BEGIN
 SELECT cpdLrnAwardRecord_seq.NEXTVAL INTO :new.clar_id FROM DUAL;
END;  


CREATE TABLE cpdGroupRegHoursHistory(
	cghi_id int NOT NULL,
	cghi_usr_ent_id int NOT NULL,
	cghi_ct_id int NOT NULL,
	cghi_cg_id int NOT NULL,
	cghi_cr_reg_number nvarchar2(255) NULL,
	cghi_cgp_id int  NULL,
	cghi_cg_contain_non_core_ind int  NULL,
	cghi_license_type nvarchar2(100) NULL,
	cghi_license_alias nvarchar2(200) NULL,
	cghi_cal_month int not NULL,
	cghi_cal_before_ind int NULL,
	cghi_recover_hours_period int NULL,
	cghi_code nvarchar2(100) NULL,
	cghi_alias nvarchar2(200) NULL,
	cghi_initial_date date NULL,
	cghi_expiry_date date NULL,
	cghi_cr_reg_date date NULL,
	cghi_cr_de_reg_date date NULL,
	cghi_period int NULL,
	cghi_first_ind int NULL,
	cghi_actual_date date NULL,
	cghi_ct_starting_month int NULL,
	cghi_cgp_effective_time date NULL,
	cghi_cal_start_date date NULL,
	cghi_cal_end_date date NULL,
	cghi_manul_core_hours float NULL,
	cghi_manul_non_core_hours float NULL,
	cghi_manul_ind int NULL,
	cghi_req_core_hours float NULL,
	cghi_req_non_core_hours float NULL,
	cghi_execute_core_hours float NULL,
	cghi_execute_non_core_hours float NULL,
	cghi_award_core_hours float NULL,
	cghi_award_non_core_hours float NULL,
	cghi_create_datetime timestamp NOT NULL,
	cghi_update_datetime timestamp NULL
);
commit;
ALTER TABLE cpdGroupRegHoursHistory ADD 
    CONSTRAINT PK_cpdGroupRegHoursHistory PRIMARY KEY   
    (
        cghi_id
    );
commit;
create sequence cpdGroupRegHoursHistory_seq;  
CREATE OR REPLACE TRIGGER cgrhh_trigger BEFORE INSERT ON cpdGroupRegHoursHistory
FOR EACH ROW
WHEN (new.cghi_id IS NULL)
BEGIN
 SELECT cpdGroupRegHoursHistory_seq.NEXTVAL INTO :new.cghi_id FROM DUAL;
END;  


CREATE TABLE cpdGroupRegCourseHistory(
	crch_id int NOT NULL,
	crch_cghi_id int NOT NULL,
	crch_aci_id int NULL,
	crch_aci_hours_end_date timestamp NULL,
	crch_aci_accreditation_code nvarchar2(100) NULL,
	crch_itm_id int NOT NULL,
	crch_itm_title nvarchar2(255) NULL,
	crch_itm_code nvarchar2(255) NULL,
	crch_period int NULL,
	crch_first_ind int NULL,
	crch_app_id int NOT NULL,
	crch_cgr_id int NOT NULL,
	crch_cr_id int NOT NULL,
	crch_ct_id int NOT NULL,
	crch_ct_license_type nvarchar2(100) NULL,
	crch_ct_license_alias nvarchar2(200) NULL,
	crch_cg_id int NOT NULL,
	crch_cg_code nvarchar2(100) NULL,
	crch_cg_alias nvarchar2(200) NULL,
	crch_award_core_hours float NULL,
	crch_award_non_core_hours float NULL,
	crch_award_datetime timestamp NULL,
	crch_create_datetime timestamp NULL,
	crch_update_datetime timestamp NULL
);
commit;
ALTER TABLE cpdGroupRegCourseHistory ADD 
    CONSTRAINT PK_cpdGroupRegCourseHistory PRIMARY KEY   
    (
        crch_id
    );
commit;
create sequence cpdGroupRegCourseHistory_seq;  
CREATE OR REPLACE TRIGGER cgrch_trigger BEFORE INSERT ON cpdGroupRegCourseHistory
FOR EACH ROW
WHEN (new.crch_id IS NULL)
BEGIN
 SELECT cpdGroupRegCourseHistory_seq.NEXTVAL INTO :new.crch_id FROM DUAL;
END;  


CREATE TABLE cpdReportRemark(
	crpm_id int NOT NULL,
	crpm_report_code nvarchar2(255) NOT NULL,
	crpm_report_desc nvarchar2(255) NULL,
	crpm_report_remark nvarchar2(2000) NOT NULL,
	crpm_create_datetime timestamp NOT NULL,
	crpm_create_usr_ent_id int NOT NULL,
	crpm_update_datetime timestamp NULL,
	crpm_update_usr_ent_id int NULL
);
commit;
ALTER TABLE cpdReportRemark ADD 
    CONSTRAINT cpdReportRemark PRIMARY KEY   
    (
        crpm_id
    );
commit;
create sequence cpdReportRemark_seq;  
CREATE OR REPLACE TRIGGER cpdReportRemark_trigger BEFORE INSERT ON cpdReportRemark
FOR EACH ROW
WHEN (new.crpm_id IS NULL)
BEGIN
 SELECT cpdReportRemark_seq.NEXTVAL INTO :new.crpm_id FROM DUAL;
END;  


CREATE TABLE cpdReportRemarkHistory(
	crmh_id int NOT NULL,
	crmh_crpm_id int NOT NULL,
	crmh_report_code nvarchar2(255) NOT NULL,
	crmh_report_desc nvarchar2(255) NULL,
	crmh_report_remark nvarchar2(2000) NOT NULL,
	crpm_create_datetime timestamp NOT NULL,
	crpm_last_update_datetime timestamp NULL,
	crpm_his_create_datetime timestamp NULL
);
commit;
ALTER TABLE cpdReportRemarkHistory ADD 
    CONSTRAINT cpdReportRemarkHistory PRIMARY KEY   
    (
        crmh_id
    );
commit;
create sequence cpdReportRemarkHistory_seq;  
CREATE OR REPLACE TRIGGER crrh_seq_trigger BEFORE INSERT ON cpdReportRemarkHistory
FOR EACH ROW
WHEN (new.crmh_id IS NULL)
BEGIN
 SELECT cpdReportRemarkHistory_seq.NEXTVAL INTO :new.crmh_id FROM DUAL;
END;  


/**
 * Auth: leaf
 * Date: 2017-03-28
 * aeItemCPDItem 表中 aci_hours_end_date字段可以为空。
 */
ALTER TABLE  aeItemCPDItem MODIFY aci_hours_end_date NULL;

/**
 * Auth: lucky
 * Date: 2017-03-30
 * 修改cpdReportRemark的crpm_report_desc字段
 * 修改cpdReportRemarkHistory的crpm_create_datetime字段
 */
alter table cpdReportRemark drop column crpm_report_desc;
commit;

alter table cpdReportRemarkHistory drop column crmh_report_desc;
commit;

alter table cpdReportRemarkHistory drop column crpm_create_datetime;
commit;

alter table cpdReportRemarkHistory add crpm_his_period int not Null;;
commit;

alter table cpdReportRemarkHistory drop column crpm_last_update_datetime;
commit;

alter table cpdReportRemarkHistory add crpm_his_save_month int not Null;;
commit;

alter table cpdGroupRegHoursHistory add cghi_cgr_id int not Null;;
commit;


/**
 * Auth: Bill
 * Date: 2017-03-23
 * 新增列：lv_student_token；Web 端学员口令
 * 新增列：lv_teacher_token；老师口令
 * 新增列：lv_student_client_token；学生客户端口令
 * 新增列：lv_teacher_join_url；老师和助教加入URL
 * 新增列：lv_student_join_url；学员加入URL
 * 新增列：lv_gensee_online_user 保存展示互动在线人数
 * 新增列：lv_real_end_datetime 真正的结束时间
 * 新增列：lv_gensee_record_url 展示互动回放地址
 */
alter table liveItem add lv_student_token nvarchar2(255);
alter table liveItem add lv_teacher_token nvarchar2(255);
alter table liveItem add lv_student_client_token nvarchar2(255);
alter table liveItem add lv_teacher_join_url nvarchar2(255);
alter table liveItem add lv_student_join_url nvarchar2(255);
alter table liveItem add lv_gensee_online_user int;
alter table liveItem add lv_real_end_datetime timestamp;
alter table liveItem add lv_gensee_record_url nvarchar2(255);
commit;

/**
 * Auth: leaf
 * Date: 2017-04-19
 * 小牌的组别编号在不同大牌下可以重复
 */
alter table cpdGroup drop unique (cg_code);
commit; 




/**
 * Auth: Archer
 * Date: 2017-04-17
 *增加新邮件CPT/D Outstanding Hours Email Alert (Learner)邮件内容及参数；
 */
insert into messageTemplate(mtp_type, mtp_subject, mtp_content, mtp_remark_label, mtp_web_message_ind, mtp_active_ind, mtp_tcr_id, 
mtp_update_timestamp, mtp_update_ent_id) values ('CPTD_OUTSTANDING_LEARNER','CPT/D Outstanding Hours – [License Alias]',
'<p>
	<p>
		<span style="color:#4C33E5;">[Header image]</span>
	</p>
	<p>
		<span style="color:#4C33E5;"><br />
</span>
	</p>
	<p>
		Dear <span style="color:#4C33E5;">[Learner name],</span>
	</p>
	<p>
		<span style="color:#4C33E5;"><br />
</span>
	</p>
	<p>
		Please kindly note that you have not yet fulfilled the CPT/D requirement for the license of <span style="color:#4C33E5;">[License Alias]. </span>
	</p>
	<p>
		Attached is your Individual CPT/D Hours Report for your review.
	</p>
	<p>
		<br />
	</p>
	<p>
		Please contact us if you have any queries regarding this email.
	</p>
	<p>
		<br />
	</p>
	<p>
		Regards,<br />
Training & Development
	</p>
	<p>
		<br />
	</p>
	<p>
		<span style="color:#4C33E5;">[Footer image]</span>
	</p>
</p>',
'lab_CPTD_OUTSTANDING_LEARNER',1,1,1,sysdate,3);
commit;



insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) select mtp_id,'[Learner name]','lab_msg_learner_name' from messageTemplate where mtp_type='CPTD_OUTSTANDING_LEARNER';
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) select mtp_id,'[Header image]','lab_msg_header_image' from messageTemplate where mtp_type='CPTD_OUTSTANDING_LEARNER';
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) select mtp_id,'[Footer image]','lab_msg_footer_image' from messageTemplate where mtp_type='CPTD_OUTSTANDING_LEARNER';
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) select mtp_id,'[License Alias]','lab_license_alias' from messageTemplate where mtp_type='CPTD_OUTSTANDING_LEARNER';
commit;



/**
 * Auth: Archer
 * Date: 2017-04-18
 *增加新邮件CPT/D Outstanding Hours Email Alert (Group Supervisor)邮件内容及参数；
 */
insert into messageTemplate(mtp_type, mtp_subject, mtp_content, mtp_remark_label, mtp_web_message_ind, mtp_active_ind, mtp_tcr_id, 
mtp_update_timestamp, mtp_update_ent_id) values ('CPTD_OUTSTANDING_SUPERVISOR','CPT/D Outstanding Hours – [License Alias]',
'<p>
  <span style="color:#4C33E5;">[Header image]</span>
</p>
<p>
  <br />
</p>
<p>
  Dear <span style="color:#4C33E5;">[Learner name],</span>
</p>
<p>
  <br />
</p>
<p>
  Please kindly note that your subordinates have not yet fulfilled the CPT/D requirement for the license of<span style="color:#4C33E5;"> [License Alias]</span>. Attached is the CPT/D Outstanding Hours Report for your reference.
</p>
<p>
  <br />
</p>
<p>
  Please kindly review with your subordinates and arrange follow-up actions.
</p>
<p>
  <br />
</p>
<p>
  Regards,<br />
Training & Development
</p>
<p>
  <br />
</p>
<p>
  <span style="color:#4C33E5;">[Footer image]</span>
</p>',
'lab_CPTD_OUTSTANDING_SUPERVISOR',1,1,1,sysdate,3);
commit;


insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) select mtp_id,'[Learner name]','lab_msg_learner_name' from messageTemplate where mtp_type='CPTD_OUTSTANDING_SUPERVISOR';
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) select mtp_id,'[Header image]','lab_msg_header_image' from messageTemplate where mtp_type='CPTD_OUTSTANDING_SUPERVISOR';
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) select mtp_id,'[Footer image]','lab_msg_footer_image' from messageTemplate where mtp_type='CPTD_OUTSTANDING_SUPERVISOR';
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) select mtp_id,'[License Alias]','lab_license_alias' from messageTemplate where mtp_type='CPTD_OUTSTANDING_SUPERVISOR';
commit;





/**
Auth: leaf
Date: 2017-05-9
Desc: 屏蔽CPT/D功能
 */
update acFunction set ftn_status = '0' where ftn_ext_id = 'FTN_AMD_CPT_D_MGT';
commit;

/**
Auth: leaf
Date: 2017-05-9
Desc: 开启CPT/D功能
 */
update acFunction set ftn_status = '1' where ftn_ext_id = 'FTN_AMD_CPT_D_MGT';
commit;


/**
Auth: Archer
Date: 2017-05-10
Desc: cpd 保存邮件oracle语句去掉编译符号
 */
update messageTemplate set mtp_content = '<p>
  <span style="color:#4C33E5;">[Header image]</span>
</p>
<p>
  <br />
</p>
<p>
  Dear <span style="color:#4C33E5;">[Learner name],</span>
</p>
<p>
  <br />
</p>
<p>
  Please kindly note that your subordinates have not yet fulfilled the CPT/D requirement for the license of<span style="color:#4C33E5;"> [License Alias]</span>. Attached is the CPT/D Outstanding Hours Report for your reference.
</p>
<p>
  <br />
</p>
<p>
  Please kindly review with your subordinates and arrange follow-up actions.
</p>
<p>
  <br />
</p>
<p>
  Regards,<br />
Training & Development
</p>
<p>
  <br />
</p>
<p>
  <span style="color:#4C33E5;">[Footer image]</span>
</p>'  where mtp_type = 'CPTD_OUTSTANDING_SUPERVISOR' ;
commit;


update messageTemplate set mtp_content = '<p>
	<p>
		<span style="color:#4C33E5;">[Header image]</span>
	</p>
	<p>
		<span style="color:#4C33E5;"><br />
</span>
	</p>
	<p>
		Dear <span style="color:#4C33E5;">[Learner name],</span>
	</p>
	<p>
		<span style="color:#4C33E5;"><br />
</span>
	</p>
	<p>
		Please kindly note that you have not yet fulfilled the CPT/D requirement for the license of <span style="color:#4C33E5;">[License Alias]. </span>
	</p>
	<p>
		Attached is your Individual CPT/D Hours Report for your review.
	</p>
	<p>
		<br />
	</p>
	<p>
		Please contact us if you have any queries regarding this email.
	</p>
	<p>
		<br />
	</p>
	<p>
		Regards,<br />
Training & Development
	</p>
	<p>
		<br />
	</p>
	<p>
		<span style="color:#4C33E5;">[Footer image]</span>
	</p>
</p>' where mtp_type = 'CPTD_OUTSTANDING_LEARNER' ;
commit;


   
/**
Auth: leaf
Date: 2017-05-12
Desc: 仅用于低版本（6.5以下）升级
      6.5版本对用户登录日志进行了扩展，故要删除旧版本所有用户登录日志表
 */
declare
    tab_name  VARCHAR2(20);
    num       number;
    now_year  number;
    now_month number;
    i         number;
  begin
    i := 0; 
    WHILE i < 12 LOOP
      tab_name  := 'loginlog';
      now_year  := extract(year from sysdate);
      now_month := extract(month from sysdate);
    
      if now_month > i then
        now_month := now_month - i;
      else
        now_month := now_month - i + 12;
        now_year  := now_year - 1;
      end if;
    
      tab_name := 'loginlog' || now_year || now_month;
    
      select count(1)
        into num
        from user_tables
       where lower(table_name) = tab_name;
      if num = 1 then
        execute immediate 'DROP TABLE ' || tab_name;
      end if;
      i := i + 1;
    END LOOP;
  end;
  
commit;


/**
Auth: leaf
Date: 2017-06-01
Desc: 去除cpdType 中 ct_license_type的唯一约束。
 */
declare
    uqname  VARCHAR2(100);
  begin
    select CONSTRAINT_NAME into uqname from  user_cons_columns where table_name='CPDTYPE' and COLUMN_NAME='CT_LICENSE_TYPE' and position = 1;
    execute immediate 'alter table cpdType drop CONSTRAINT ' || uqname;
  end;
  
commit;

/**
Auth: jesse
Date: 2017-06-01
Desc: 导入cpd注册记录邮件
 */
insert into messageTemplate(mtp_type, mtp_subject, mtp_content,mtp_content_email_link
,mtp_content_pc_link,mtp_content_mobile_link, mtp_remark_label
 ,mtp_web_message_ind, mtp_active_ind, mtp_tcr_id,mtp_update_ent_id, mtp_update_timestamp
 ,mtp_header_img,mtp_footer_img) 
 values ('CPD_REGISTRATION_IMPORT_SUCCESS','import CPD Registration record','','/login'
 ,null,null,'lab_CPD_REGISTRATION_IMPORT_SUCCESS'
 ,1,1,1,3,getdate()
 ,'/msg/emailHeader.jpg','/msg/emailFooter.jpg');
 go

 update messageTemplate set mtp_content=
(select m.mtp_content from messageTemplate m where m.mtp_type='USER_IMPORT_SUCCESS')
 where mtp_type='CPD_REGISTRATION_IMPORT_SUCCESS'
 go
 insert into messageParamName(mpn_mtp_id,mpn_name,mpn_name_desc)
 values ((select mtp_id from messageTemplate where mtp_type='CPD_REGISTRATION_IMPORT_SUCCESS'),
 '[User name]','lab_msg_user_name');
 go
 insert into messageParamName(mpn_mtp_id,mpn_name,mpn_name_desc)
 values ((select mtp_id from messageTemplate where mtp_type='CPD_REGISTRATION_IMPORT_SUCCESS'),
 '[Src file]','lab_msg_src_file');
 go
 insert into messageParamName(mpn_mtp_id,mpn_name,mpn_name_desc)
 values ((select mtp_id from messageTemplate where mtp_type='CPD_REGISTRATION_IMPORT_SUCCESS'),
 '[Import start date]','lab_msg_import_start_date');
 go
  insert into messageParamName(mpn_mtp_id,mpn_name,mpn_name_desc)
 values ((select mtp_id from messageTemplate where mtp_type='CPD_REGISTRATION_IMPORT_SUCCESS'),
 '[Success total]','lab_msg_success_total');
 go
  insert into messageParamName(mpn_mtp_id,mpn_name,mpn_name_desc)
 values ((select mtp_id from messageTemplate where mtp_type='CPD_REGISTRATION_IMPORT_SUCCESS'),
 '[Unsuccess total]','lab_msg_unscuccess_total');
 go
  insert into messageParamName(mpn_mtp_id,mpn_name,mpn_name_desc)
 values ((select mtp_id from messageTemplate where mtp_type='CPD_REGISTRATION_IMPORT_SUCCESS'),
 '[Import end date]','lab_msgimport_end_date');
 go
  insert into messageParamName(mpn_mtp_id,mpn_name,mpn_name_desc)
 values ((select mtp_id from messageTemplate where mtp_type='CPD_REGISTRATION_IMPORT_SUCCESS')
 ,'[Header image]','lab_msg_header_image')
 go
 insert into messageParamName(mpn_mtp_id,mpn_name,mpn_name_desc)
 values ((select mtp_id from messageTemplate where mtp_type='CPD_REGISTRATION_IMPORT_SUCCESS')
 ,'[Footer image]','lab_msg_footer_image')

GO

/**
Auth: randy
Date: 2017-06-09
Desc: 未发布的问答及目录不要在不员端显示。
 */
update knowCatalog set  kca_public_ind = 0 
where kca_type = 'NORMAL' and kca_id in(
select kcr_child_kca_id from knowCatalog,knowCatalogRelation where kca_id  = kcr_ancestor_kca_id and kca_public_ind = 0 
and kca_type = 'CATALOG' and kcr_type = 'KCA_PARENT_KCA' );		 

commit;


/**
Auth: Archer
Date: 2017-06-14
Desc: 增加一个cpt/d二级功能并默认分配给系统管理员角色
 */
declare ftn_order int;
        ftn_id_parent int;
begin

select max(ftn_order) into ftn_order from acFunction where ftn_level = 0;
select ftn_id  into ftn_id_parent  from acFunction where ftn_ext_id = 'FTN_AMD_CPT_D_MGT';

INSERT into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_tc_related, ftn_status, ftn_assign, ftn_order, ftn_parent_id)  
VALUES ('FTN_AMD_CPT_D_IMPORT_AWARDED_HOURS', '1', NULL, sysdate, 'N', '1', '1', ftn_order+1, ftn_id_parent);
commit;
end;

--CPT/D 管理功能，导入用户获得时数子功能默认分配给“系统管理员”角色
INSERT into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_usr_id, RFN_CREATE_TIMESTAMP, rfn_ftn_parent_id, rfn_ftn_order )  
select rol_id, child.ftn_id, 's1u3', sysdate,null, 1  from acRole, acFunction child, acFunction parent
where rol_ext_id like 'ADM_%' and child.ftn_ext_id = 'FTN_AMD_CPT_D_IMPORT_AWARDED_HOURS' and parent.ftn_ext_id = 'FTN_AMD_CPT_D_MGT';
commit;



/**
Auth: Archer
Date: 2017-06-14
Desc: 增加一个cpt/d导入用户获得CPT/D时数成功邮件
 */

insert into messageTemplate(mtp_type, mtp_subject, mtp_content, mtp_remark_label, mtp_web_message_ind, mtp_active_ind, mtp_tcr_id, mtp_update_timestamp, mtp_update_ent_id) values ('CPD_AWARDED_HOURS_IMPORT_SUCCESS','Import user awarded CPT/D hours Success Notification', 'Import user awarded CPT/D hours Success Notification','label_CPD_AWARDED_HOURS_IMPORT_SUCCESS',1,1,1,sysdate,3);

update messageTemplate set mtp_content = '<p>   <span style="color:black;font-family:宋体;font-size:11pt;">Dear </span><span style="color:blue;font-family:宋体;font-size:11pt;">[User name]</span><span style="color:black;font-family:宋体;font-size:11pt;">,<br />  <br />  This email is to notify you that the import of user awarded CPT/D hours file </span><span style="color:blue;font-family:宋体;font-size:11pt;">[Src file]</span><span style="color:black;font-family:宋体;font-size:11pt;">, which you uploaded at </span><span style="color:blue;font-family:宋体;font-size:11pt;">[Import start date]</span><span style="color:black;font-family:宋体;font-size:11pt;">, has been completed.<br />  <br />  Success Entries : </span><span style="color:blue;font-family:宋体;font-size:11pt;">[Success total]</span><span style="color:black;font-family:宋体;font-size:11pt;"><br />  Unsuccessful Entries : </span><span style="color:blue;font-family:宋体;font-size:11pt;">[Unsuccess total]<br />  </span><span style="color:black;font-family:宋体;font-size:11pt;"><br />  The import process was completed at </span><span style="color:blue;font-family:宋体;font-size:11pt;">[Import end date]. </span><span style="font-family:宋体;font-size:11pt;">You  may login to the system to view the log file(s) in the import history. <br />  Thank you for your attention.<br />  <br />  Regards,<br />  Training & Development</span>  </p>' where mtp_type='CPD_AWARDED_HOURS_IMPORT_SUCCESS';

insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) select mtp_id,'[User name]','lab_msg_user_name' from messageTemplate where mtp_type='CPD_AWARDED_HOURS_IMPORT_SUCCESS';
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) select mtp_id,'[Src file]','lab_msg_src_file' from messageTemplate where mtp_type='CPD_AWARDED_HOURS_IMPORT_SUCCESS';
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) select mtp_id,'[Import start date]','lab_msg_import_start_date' from messageTemplate where mtp_type='CPD_AWARDED_HOURS_IMPORT_SUCCESS';
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) select mtp_id,'[Success total]','lab_msg_success_total' from messageTemplate where mtp_type='CPD_AWARDED_HOURS_IMPORT_SUCCESS';
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) select mtp_id,'[Unsuccess total]','lab_msg_unscuccess_total' from messageTemplate where mtp_type='CPD_AWARDED_HOURS_IMPORT_SUCCESS';
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) select mtp_id,'[Import end date]','lab_msgimport_end_date' from messageTemplate where mtp_type='CPD_AWARDED_HOURS_IMPORT_SUCCESS';
commit;


/**
Auth: Archer
Date: 2017-06-15
Desc: 导入注册记录：修改邮件字体（邮件的部分字体显示蓝色需改为黑色）; 导入用户获得CPT/D时数增加头部和尾部图片
 */
update messageTemplate set mtp_content = '<span style="color:#0000FF;line-height:28px;font-family:&quot;Microsoft YaHei UI&quot;, &quot;Microsoft YaHei&quot;, DengXian, SimSun, &quot;Segoe UI&quot;, Tahoma, Helvetica, sans-serif;font-size:14px;background-color:#FFFFFF;">[Header image]</span>  <p>   <span style="color:black;font-family:宋体;font-size:11pt;">Dear <span style="color:#0000FF;font-family:"Microsoft YaHei UI", "Microsoft YaHei", DengXian, SimSun, "Segoe UI", Tahoma, Helvetica, sans-serif;font-size:14px;line-height:28px;background-color:#FFFFFF;">[User name]</span></span><span style="color:blue;font-family:宋体;font-size:11pt;"></span><span style="color:black;font-family:宋体;font-size:11pt;">,<br />  <br />  This email is to notify you that the import of user profile    file </span><span style="color:blue;font-family:宋体;font-size:11pt;"><span style="color:#0000FF;font-family:"Microsoft YaHei UI", "Microsoft YaHei", DengXian, SimSun, "Segoe UI", Tahoma, Helvetica, sans-serif;font-size:14px;line-height:28px;background-color:#FFFFFF;">[Src file]</span></span><span style="color:black;font-family:宋体;font-size:11pt;">,    which you uploaded at </span><span style="color:blue;font-family:宋体;font-size:11pt;"><span style="color:#0000FF;font-family:"Microsoft YaHei UI", "Microsoft YaHei", DengXian, SimSun, "Segoe UI", Tahoma, Helvetica, sans-serif;font-size:14px;line-height:28px;background-color:#FFFFFF;">[Import start date]</span></span><span style="color:black;font-family:宋体;font-size:11pt;">, has    been completed.<br />  <br />  Success Entries : </span><span style="color:blue;font-family:宋体;font-size:11pt;"><span style="color:#0000FF;font-family:"Microsoft YaHei UI", "Microsoft YaHei", DengXian, SimSun, "Segoe UI", Tahoma, Helvetica, sans-serif;font-size:14px;line-height:28px;background-color:#FFFFFF;">[Success total]</span></span><span style="color:black;font-family:宋体;font-size:11pt;"><br />  Unsuccessful Entries : </span><span style="color:blue;font-family:宋体;font-size:11pt;"><span style="color:#0000FF;font-family:"Microsoft YaHei UI", "Microsoft YaHei", DengXian, SimSun, "Segoe UI", Tahoma, Helvetica, sans-serif;font-size:14px;line-height:28px;background-color:#FFFFFF;">[Unsuccess total]</span><br />  </span><span style="color:black;font-family:宋体;font-size:11pt;"><br />  The import process was completed at </span><span style="color:blue;font-family:宋体;font-size:11pt;"><span style="color:#0000FF;font-family:"Microsoft YaHei UI", "Microsoft YaHei", DengXian, SimSun, "Segoe UI", Tahoma, Helvetica, sans-serif;font-size:14px;line-height:28px;background-color:#FFFFFF;"><span style="color:#0000FF;font-family:"Microsoft YaHei UI", "Microsoft YaHei", DengXian, SimSun, "Segoe UI", Tahoma, Helvetica, sans-serif;font-size:14px;line-height:28px;background-color:#FFFFFF;">[Import end date]</span></span>. </span><span style="color:black;font-family:宋体;font-size:11pt;">You    may login to the system to view the log file(s) in the import history.  <br />  Regards,<br />  Training &amp; Development </span>   </p>    <span style="color:#0000FF;font-family:"Microsoft YaHei UI", "Microsoft YaHei", DengXian, SimSun, "Segoe UI", Tahoma, Helvetica, sans-serif;font-size:14px;line-height:28px;background-color:#FFFFFF;">[Footer image]</span>'
where mtp_type = 'CPD_REGISTRATION_IMPORT_SUCCESS';

update messageTemplate set mtp_content = '<span style="color:#0000FF;line-height:28px;font-family:&quot;Microsoft YaHei UI&quot;, &quot;Microsoft YaHei&quot;, DengXian, SimSun, &quot;Segoe UI&quot;, Tahoma, Helvetica, sans-serif;font-size:14px;background-color:#FFFFFF;">[Header image]</span>  <p>   <span style="color:black;font-family:宋体;font-size:11pt;">Dear </span><span style="color:blue;font-family:宋体;font-size:11pt;">[User name]</span><span style="color:black;font-family:宋体;font-size:11pt;">,<br />  <br />  This email is to notify you that the import of user awarded CPT/D hours file </span><span style="color:blue;font-family:宋体;font-size:11pt;">[Src file]</span><span style="color:black;font-family:宋体;font-size:11pt;">, which you uploaded at </span><span style="color:blue;font-family:宋体;font-size:11pt;">[Import start date]</span><span style="color:black;font-family:宋体;font-size:11pt;">, has been completed.<br />  <br />  Success Entries : </span><span style="color:blue;font-family:宋体;font-size:11pt;">[Success total]</span><span style="color:black;font-family:宋体;font-size:11pt;"><br />  Unsuccessful Entries : </span><span style="color:blue;font-family:宋体;font-size:11pt;">[Unsuccess total]<br />  </span><span style="color:black;font-family:宋体;font-size:11pt;"><br />  The import process was completed at </span><span style="color:blue;font-family:宋体;font-size:11pt;">[Import end date]. </span><span style="color:black;font-family:宋体;font-size:11pt;">You  may login to the system to view the log file(s) in the import history. <br />  Thank you for your attention.<br />  <br />  Regards,<br />  Training &amp; Development</span>  </p> <span style="color:#0000FF;font-family:"Microsoft YaHei UI", "Microsoft YaHei", DengXian, SimSun, "Segoe UI", Tahoma, Helvetica, sans-serif;font-size:14px;line-height:28px;background-color:#FFFFFF;">[Footer image]</span>' where mtp_type='CPD_AWARDED_HOURS_IMPORT_SUCCESS';
commit;


/**
Auth: Archer
Date: 2017-06-21
Desc: 删除导入用户获得CPT/D时数邮件
 */
delete from messageParamName where mpn_mtp_id =  (select mtp_id from messageTemplate where mtp_type='CPD_AWARDED_HOURS_IMPORT_SUCCESS');
delete from emailMsgRecHistory where emrh_emsg_id in  (select emsg_id from emailMessage where emsg_mtp_id =  (select mtp_id from messageTemplate where mtp_type='CPD_AWARDED_HOURS_IMPORT_SUCCESS'));
delete from emailMessage where emsg_mtp_id =  (select mtp_id from messageTemplate where mtp_type='CPD_AWARDED_HOURS_IMPORT_SUCCESS');
delete from webMsgReadHistory where wmrh_wmsg_id in (select wmsg_id from webMessage where wmsg_mtp_id =  (select mtp_id from messageTemplate where mtp_type='CPD_AWARDED_HOURS_IMPORT_SUCCESS'));
delete from webMessage where wmsg_mtp_id =  (select mtp_id from messageTemplate where mtp_type='CPD_AWARDED_HOURS_IMPORT_SUCCESS');
delete from messageTemplate where mtp_type = 'CPD_AWARDED_HOURS_IMPORT_SUCCESS';
commit;


/**
Auth: Archer
Date: 2017-06-21
Desc: 删除导入用户注册牌照邮件
 */
delete from messageParamName where mpn_mtp_id =  (select mtp_id from messageTemplate where mtp_type='CPD_REGISTRATION_IMPORT_SUCCESS');
delete from emailMsgRecHistory where emrh_emsg_id in  (select emsg_id from emailMessage where emsg_mtp_id =  (select mtp_id from messageTemplate where mtp_type='CPD_REGISTRATION_IMPORT_SUCCESS'));
delete from emailMessage where emsg_mtp_id =  (select mtp_id from messageTemplate where mtp_type='CPD_REGISTRATION_IMPORT_SUCCESS');
delete from webMsgReadHistory where wmrh_wmsg_id in (select wmsg_id from webMessage where wmsg_mtp_id =  (select mtp_id from messageTemplate where mtp_type='CPD_REGISTRATION_IMPORT_SUCCESS'));
delete from webMessage where wmsg_mtp_id =  (select mtp_id from messageTemplate where mtp_type='CPD_REGISTRATION_IMPORT_SUCCESS');
delete from messageTemplate where mtp_type = 'CPD_REGISTRATION_IMPORT_SUCCESS';
commit;



/**
Auth: leaf
Date: 2017-06-23
Desc: 去除cpdGroup 中 cg_code的唯一约束。
 */
declare
    uqname  VARCHAR2(100);
  begin
    select CONSTRAINT_NAME into uqname from  user_cons_columns  where table_name='CPDGROUP' and COLUMN_NAME='CG_CODE';
    execute immediate 'alter table cpdGroup drop CONSTRAINT ' || uqname;
  end;
  
commit;
/**
Auth: leaf
Date: 2017-06-28
Desc: 【CPT/D】去除CPD功能中离线课程下班级的aci_accreditation_code,导出报表时取课程设置的aci_accreditation_code。
 */
update aeItemCPDItem
   set aci_accreditation_code = (select a.aci_accreditation_code
                                   from aeItemCPDItem a
                                  where a.aci_itm_id =
                                        (select ire_parent_itm_id
                                           from aeItemRelation
                                          where ire_child_itm_id =
                                                aeItemCPDItem.aci_itm_id))
 where (select COUNT(1)
          from aeItemRelation
         where ire_child_itm_id = aci_itm_id) > 0;
         
commit;




/**
Auth: steven
Date: 2017-07-07（实际增加时间为2017-08-08，属于漏加后来补上的语句，与其他db_changes.sql统一）
Desc: 添加测试题，解析online video中api返回的资源link
 */
ALTER TABLE Resources ADD res_src_online_link nvarchar2(255);

commit;


/**
Auth: Archer
Date: 2017-07-27（实际增加时间为2017-08-08，属于漏加后来补上的语句，与其他db_changes.sql统一）
Desc: 给"CPT/D Outstanding Hours – [License Alias]"邮件添加头部和尾部的默认图片
 */
update messageTemplate set mtp_header_img = '/msg/emailHeader.jpg' where mtp_type = 'CPTD_OUTSTANDING_LEARNER';
commit;
update messageTemplate set mtp_footer_img = '/msg/emailFooter.jpg' where mtp_type = 'CPTD_OUTSTANDING_LEARNER';
commit;

update messageTemplate set mtp_header_img = '/msg/emailHeader.jpg' where mtp_type = 'CPTD_OUTSTANDING_SUPERVISOR';
commit;
update messageTemplate set mtp_footer_img = '/msg/emailFooter.jpg' where mtp_type = 'CPTD_OUTSTANDING_SUPERVISOR';
commit;



/**
Auth: steven
Date: 2017-07-28
Desc: 更新忘记密码邮件模板（实际增加时间为2017-08-08，属于漏加后来补上的语句，与其他db_changes.sql统一）
 */
UPDATE messageTemplate SET mtp_content = ' [Header image] Dear Sir/Madam: <br /> <br /> We have received your reset password request on [Request time].<br/><br/>Please click below link to reset your password:<br/><br/>[Link to reset password]<br/><br/>The above link will be expired after [Max days] day(s) so please proceed to reset your password as soon as possible.<br/><br/>Regards,<br />Training & Development</p><span></span> <p> <span>[Footer image]</span> </p><p> <br /></p>'
	WHERE mtp_type = 'USER_PWD_RESET_NOTIFY';

commit;


/**
 * Auth: Jacky
 * Date: 2017-08-11
 * Desc: 更新邮件模板subject为英文 
 */

UPDATE messageTemplate SET mtp_subject = 'Course Enrollment Request Received'
WHERE mtp_id = 1
commit;
UPDATE messageTemplate SET mtp_subject = 'Course Enrollment Confirmed'
WHERE mtp_id = 2
commit;
UPDATE messageTemplate SET mtp_subject = 'Course Enrollment Waiting List'
WHERE mtp_id = 3
commit;
UPDATE messageTemplate SET mtp_subject = 'Course Enrollment Request Declined'
WHERE mtp_id = 4
commit;
UPDATE messageTemplate SET mtp_subject = 'Course Enrollment Request Cancelled'
WHERE mtp_id = 5
commit;
UPDATE messageTemplate SET mtp_subject = 'Course Enrollment Request Cancelled'
WHERE mtp_id = 6
commit;
UPDATE messageTemplate SET mtp_subject = 'Course Enrollment Approval'
WHERE mtp_id = 7
commit;
UPDATE messageTemplate SET mtp_subject = 'Course Enrollment Notification'
WHERE mtp_id = 8
commit;
UPDATE messageTemplate SET mtp_subject = 'Course Enrollment Notification'
WHERE mtp_id = 9
commit;
UPDATE messageTemplate SET mtp_subject = 'Course Enrollment Notification'
WHERE mtp_id = 10
commit;
UPDATE messageTemplate SET mtp_subject = 'Course Enrollment Notification'
WHERE mtp_id = 11
commit;
UPDATE messageTemplate SET mtp_subject = 'Course Enrollment Approval'
WHERE mtp_id = 12
commit;
UPDATE messageTemplate SET mtp_subject = 'Course Completion Reminder'
WHERE mtp_id = 13
commit;
UPDATE messageTemplate SET mtp_subject = 'Joining Instruction'
WHERE mtp_id = 14
commit;
UPDATE messageTemplate SET mtp_subject = 'Reminder'
WHERE mtp_id = 15
commit;
UPDATE messageTemplate SET mtp_subject = 'USR_REG_APPROVE'
WHERE mtp_id = 16
commit;
UPDATE messageTemplate SET mtp_subject = 'USR_REG_DISAPPROVE'
WHERE mtp_id = 17
commit;
UPDATE messageTemplate SET mtp_subject = 'User Import Success Notification'
WHERE mtp_id = 18
commit;
UPDATE messageTemplate SET mtp_subject = 'Enrollment Import Success Notification'
WHERE mtp_id = 19
commit;
UPDATE messageTemplate SET mtp_subject = 'System Performance Notify'
WHERE mtp_id = 20
commit;
UPDATE messageTemplate SET mtp_subject = 'empty subject'
WHERE mtp_id = 21
commit;
UPDATE messageTemplate SET mtp_subject = 'Reset Password Notify'
WHERE mtp_id = 22
commit;
UPDATE messageTemplate SET mtp_subject = '来自[User name]的站内信'
WHERE mtp_id = 23
commit;
UPDATE messageTemplate SET mtp_subject = 'CPT/D Outstanding Hours – [License Alias]'
WHERE mtp_id = 24
commit;
UPDATE messageTemplate SET mtp_subject = 'CPT/D Outstanding Hours – [License Alias]'
WHERE mtp_id = 25
commit;

/*
Auth: jack
Date: 2017-08-23
Desc:上传视频提醒，增加视频总时度
 */
ALTER table Resources add res_total_time int NULL;

/*
 * Auth: jacky
 * Date: 2017-09-07
 * Desc: 删除视频总时度（HKO版本删除 上传视频提示）
 */
ALTER TABLE Resources DROP COLUMN res_total_time;
GO

/**
Auth: Archer
Date: 2017-08-30
Desc: 修改CPT/D小牌注册时数记录历史快照表的番生指数字段类型为int，此问题只有Sql Server存在，oracle数据库和Mysql统一已用int类型
 */
alter table cpdGroupRegHoursHistory alter column cghi_recover_hours_period int null;
go

/**
Auth: rofe
Date: 2017-09-11
Desc: 修改职级中编号类型为varchar避免输入中文乱码
 */
alter table UserGrade alter column ugr_code varchar(255)
go



/**
Auth: Archer
Date: 2017-10-12 
Desc: 清除Learning activity report 的旧模板数据，将LEARNER类型的报表改为可使用状态
 */
delete ReportSpec where rsp_rte_id in(select rte_id from ReportTemplate where rte_type = 'LEARNER');
commit;
update ReportTemplate set rte_owner_ent_id = 1 where rte_type = 'LEARNER';
commit;

/**
Auth: steven 
Date: 2017-10-25
Desc: 清空"设置手动扣分点"数据，默认为空
 */
UPDATE creditsType 
	SET cty_deleted_ind = 1, cty_update_timestamp = GETDATE()
	WHERE cty_id in (
		SELECT cty_id FROM creditstype 
		WHERE cty_deduction_ind = 1 AND cty_manual_ind = 1 
		AND cty_deleted_ind = 0 AND (cty_relation_type is null or cty_relation_type = '') 
		and  cty_tcr_id = 1
	);
commit;



/*
Date: 2017-11-9
Desc: 培训积分清空
*/
 INSERT INTO creditsType 
 (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,
 cty_deleted_ind,cty_relation_total_ind,cty_relation_type,
 cty_default_credits_ind,cty_default_credits,cty_create_usr_id,
 cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,CTY_TCR_ID)
VALUES('ITM_INTEGRAL_EMPTY','ITM_INTEGRAL_EMPTY',1,1,0,1,'COS',0,0,'s1u3',sysdate,'s1u3',sysdate,1);

/**
Auth: steven 
Date: 2017-11-21
Desc: mobile端登录页面logo重置为默认图片
*/
update SitePoster set sp_logo_file_cn = 'logo3.png' where sp_ste_id = 1 and SP_MOBILE_IND = 1
commit;

/**
(补充此SQL到DB_CHANGE)
Auth: Jacky
Date: 2017-11-17
Desc: 屏蔽学习地图功能
 */
update acFunction set ftn_status = '0' where ftn_ext_id = 'FTN_AMD_STUDY_MAP_MGT';
commit; 

/**
(补充此SQL到DB_CHANGE)
Auth: Jacky
Date: 2017-11-17
Desc: 屏蔽 live management
 */
update acFunction set ftn_status = '0' where ftn_ext_id = 'FTN_AMD_LIVE_MAIN';
commit;

/**
Auth: Jacky
Date: 2017-11-17
Desc: 屏蔽 vendor management
 */
update acFunction set ftn_status = '0' where ftn_ext_id = 'FTN_AMD_SUPPLIER_MAIN';
commit;

/*
Auth: Jasper
Date: 2018-01-22
Desc: 增加回 积分清空 code
*/
 INSERT INTO creditsType 
 (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,
 cty_deleted_ind,cty_relation_total_ind,cty_relation_type,
 cty_default_credits_ind,cty_default_credits,cty_create_usr_id,
 cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,CTY_TCR_ID)
VALUES('INTEGRAL_EMPTY','INTEGRAL_EMPTY',1,1,0,1,'',0,0,'s1u3',sysdate,'s1u3',sysdate,1);
commit;


/**
Auth: Albert
Date: 2017-11-17
Desc: 屏蔽 点赞，被点赞和评论的的得分项
 */
update creditsType set cty_deleted_ind = 0 where cty_id=34;
commit;
update creditsType set cty_deleted_ind = 0 where cty_id=35;
commit;
update creditsType set cty_deleted_ind = 0 where cty_id=36;
commit;

/**
Auth: Jasper
Date: 2017-12-18
Desc: 修改aeItemExtension中的ies_credit字段类型
 */
alter table aeItemExtension alter column ies_credit decimal(8,2);
commit;

/*
Auth: Hajar
Date: 2018-02-09
Desc: 修改 题目分数数据结构
 */
alter table Question alter column que_score number(5);
alter table RawQuestion alter column raq_score number(5);
commit;

/**
Auth: Steven
Date: 2018-3-13
Desc: 表emailMessage新增抄送电邮地址字段
 */
alter table emailMessage add emsg_cc_email varchar(2000) NULL;
commit;

/**
Auth: Jaren
Date: 2018-04-02
Desc: 更新导入报名记录已完成模板
 */
update messageTemplate set mtp_content = '[Header image] <p>  <span> </span><span   style="color: black; font-family: 宋体; font-size: 11pt;">Dear </span><span   style="color: blue; font-family: 宋体; font-size: 11pt;">[User name]</span><span style="color: black; font-family: 宋体; font-size: 11pt;">,<br />   <br /> This email is to notify you that the import of enrollment record  </span><span style="color: blue; font-family: 宋体; font-size: 11pt;">[Src file]</span><span style="color: black; font-family: 宋体; font-size: 11pt;">,   which you uploaded at </span><span   style="color: blue; font-family: 宋体; font-size: 11pt;">[Import start date]</span><span   style="color: black; font-family: 宋体; font-size: 11pt;">, has   been completed.<br /> <br /> Success Entries :  </span><span style="color: blue; font-family: 宋体; font-size: 11pt;">[Success total]</span><span style="color: black; font-family: 宋体; font-size: 11pt;"><br />   Unsuccessful Entries : </span><span   style="color: blue; font-family: 宋体; font-size: 11pt;">[Unsuccess total]<br />  </span><span style="color: black; font-family: 宋体; font-size: 11pt;"><br />   The import process was completed at </span><span   style="color: blue; font-family: 宋体; font-size: 11pt;">[Import end date]. </span><span style="font-family: 宋体; font-size: 11pt;">You   may login to the system to view the log file(s) in the import history.   <br /> Thank you for your attention.<br /> <br /> Regards,<br />   Training &amp; Development  </span> </p> <span> </span>  [Footer image] '
where mtp_type = 'ENROLLMENT_IMPORT_SUCCESS' and mtp_tcr_id = 1;
commit;

/**
Auth: Jaren
Date: 2018-04-02
Desc: 面授課程繁體 label 問題20180424
 */
update aeTemplateView set
tvw_xml='<template_view>	<section id="1">		<title>			<desc lan="ISO-8859-1" name="General information"/>			<desc lan="Big5" name="基本資訊"/>			<desc lan="GB2312" name="基本信息"/>		</title>		<itm_type type="itm_type" paramname="itm_type">			<title>				<desc lan="ISO-8859-1" name="Type"/>				<desc lan="Big5" name="類型"/>				<desc lan="GB2312" name="类型"/>			</title>		</itm_type>		<field51>			<title>				<desc lan="ISO-8859-1" name="Code"/>				<desc lan="Big5" name="編號"/>				<desc lan="GB2312" name="编号"/>			</title>		</field51>		<field52>			<title>				<desc lan="ISO-8859-1" name="Title"/>				<desc lan="Big5" name="名稱"/>				<desc lan="GB2312" name="名称"/>			</title>		</field52>		<training_center type="tcr_pickup" paramname="itm_tcr_id" required="yes" external_field="yes">			<title>				<desc lan="ISO-8859-1" name="Training center"/>				<desc lan="Big5" name="培訓中心"/>				<desc lan="GB2312" name="培训中心"/>			</title>		</training_center>		<field53>			<title>				<desc lan="ISO-8859-1" name="Class period"/>				<desc lan="Big5" name="面授期間"/>				<desc lan="GB2312" name="面授期间"/>			</title>			<subfield_list>				<subfield id="1">					<title>						<desc lan="ISO-8859-1" name="From"/>						<desc lan="Big5" name="由"/>						<desc lan="GB2312" name="由"/>					</title>				</subfield>				<subfield id="2">					<title>						<desc lan="ISO-8859-1" name="To"/>						<desc lan="Big5" name="至"/>						<desc lan="GB2312" name="至"/>					</title>				</subfield>			</subfield_list>		</field53>			<!--		<field61 blend_ind="true">			<title>				<desc lan="ISO-8859-1" name="Online content period"/>				<desc lan="Big5" name="網上內容期限"/>				<desc lan="GB2312" name="网上内容期限"/>			</title>			<subfield_list>				<subfield id="1">					<title>						<desc lan="ISO-8859-1" name="From"/>						<desc lan="Big5" name="由"/>						<desc lan="GB2312" name="由"/>					</title>				</subfield>				<subfield id="2">					<title>						<desc lan="ISO-8859-1" name="To"/>						<desc lan="Big5" name="至"/>						<desc lan="GB2312" name="至"/>					</title>				</subfield>			</subfield_list>		</field61>		-->		<field55>			<title>				<desc lan="ISO-8859-1" name="Enrollment period"/>				<desc lan="Big5" name="報名期限"/>				<desc lan="GB2312" name="报名期限"/>			</title>			<subfield_list>				<subfield id="1">					<title>						<desc lan="ISO-8859-1" name="From"/>						<desc lan="Big5" name="由"/>						<desc lan="GB2312" name="由"/>					</title>				</subfield>				<subfield id="2">					<title>						<desc lan="ISO-8859-1" name="To"/>						<desc lan="Big5" name="至"/>						<desc lan="GB2312" name="至"/>					</title>				</subfield>			</subfield_list>		</field55>		<field06>			<title>				<desc lan="ISO-8859-1" name="Description"/>				<desc lan="Big5" name="簡介"/>				<desc lan="GB2312" name="简介"/>			</title>		</field06>		<!--		<rsv_main_room type="constant" marked="no" required="no" external_field="yes">			<title>				<desc lan="ISO-8859-1" name="Main room"/>				<desc lan="Big5" name="主房間"/>				<desc lan="GB2312" name="主房间"/>			</title>		</rsv_main_room>		<rsv_venue type="constant" marked="no" required="no" external_field="yes">			<title>				<desc lan="ISO-8859-1" name="Venue"/>				<desc lan="Big5" name="地點"/>				<desc lan="GB2312" name="地点"/>			</title>		</rsv_venue>		-->	</section>	<section id="2">		<title>			<desc lan="ISO-8859-1" name="Administrative information"/>			<desc lan="Big5" name="管理資訊"/>			<desc lan="GB2312" name="管理信息"/>		</title>		<field54>			<title>				<desc lan="ISO-8859-1" name="Address"/>				<desc lan="Big5" name="地址"/>				<desc lan="GB2312" name="地址"/>			</title>		</field54>		<field59>			<title>				<desc lan="ISO-8859-1" name="Fee"/>				<desc lan="Big5" name="報名費"/>				<desc lan="GB2312" name="报名费"/>			</title>		</field59>		<field114>			<title>				<desc lan="ISO-8859-1" name="Credit"/>				<desc lan="Big5" name="學分"/>				<desc lan="GB2312" name="学分"/>			</title>		</field114>		<field56>			<title>				<desc lan="ISO-8859-1" name="Remarks"/>				<desc lan="Big5" name="備註"/>				<desc lan="GB2312" name="备注"/>			</title>		</field56>		<item_access type="item_access_pickup" paramname="iac_id_lst" id="TADM" dependant="training_center" arrayparam="true">			<title>				<desc lan="ISO-8859-1" name="Training administrator"/>				<desc lan="Big5" name="培訓管理員"/>				<desc lan="GB2312" name="培训管理员"/>			</title>		</item_access>		<field160>			<title>				<desc lan="ISO-8859-1" name="Lecturer type"/>				<desc lan="Big5" name="講師類型"/>				<desc lan="GB2312" name="讲师类型"/>			</title>			<field160 id="1">				<title>					<desc lan="ISO-8859-1" name="Internal lecturer"/>					<desc lan="Big5" name="内部講師"/>					<desc lan="GB2312" name="内部讲师"/>				</title>			</field160>			<field160 id="2">				<title>					<desc lan="ISO-8859-1" name="External lecturer"/>					<desc lan="Big5" name="外部講師"/>					<desc lan="GB2312" name="外部讲师"/>				</title>			</field160>		</field160>		<item_access type="item_access_pickup" paramname="iac_id_lst" id="INSTR" arrayparam="true" dependant="field160" >		</item_access>		<item_access exam_ind="true" blend_ind="true" type="item_access_pickup" paramname="iac_id_lst" id="EXA" arrayparam="true" dependant="training_center"/>		<item_status type="radio" value="OFF" paramname="itm_status" required="yes">			<title>				<desc lan="ISO-8859-1" name="Publish to catalog"/>				<desc lan="Big5" name="在目錄中發佈"/>				<desc lan="GB2312" name="在目录中发布"/>			</title>			<item_status id="1" value="ALL">				<title>					<desc lan="ISO-8859-1" name="Yes"/>					<desc lan="Big5" name="是"/>					<desc lan="GB2312" name="是"/>				</title>			</item_status>			<item_status id="2" value="OFF">				<title>					<desc lan="ISO-8859-1" name="No"/>					<desc lan="Big5" name="否"/>					<desc lan="GB2312" name="否"/>				</title>			</item_status>		</item_status>		<field57>			<title>				<desc lan="ISO-8859-1" name="Quota"/>				<desc lan="Big5" name="名額限制"/>				<desc lan="GB2312" name="名额限制"/>			</title>		</field57>		<field21>			<title>				<desc lan="ISO-8859-1" name="Send enrollment notification email"/>				<desc lan="Big5" name="發送報名通知郵件"/>				<desc lan="GB2312" name="发送报名通知邮件"/>			</title>			<field21 id="1">				<title>					<desc lan="ISO-8859-1" name="Yes, send to learner only"/>					<desc lan="Big5" name="是，只發送給學員"/>					<desc lan="GB2312" name="是，只发送给学员"/>				</title>			</field21>			<field21 id="2">				<title>					<desc lan="ISO-8859-1" name="Yes, send to learner and Direct Supervisor(s)"/>					<desc lan="Big5" name="是，發送給學員及其所有直屬上司"/>					<desc lan="GB2312" name="是，发送给学员及其所有直属上司"/>				</title>			</field21>			<field21 id="3">				<title>					<desc lan="ISO-8859-1" name="No"/>					<desc lan="Big5" name="否"/>					<desc lan="GB2312" name="否"/>				</title>			</field21>		</field21>		<!--		<field111 dependant="training_center">			<title>				<desc lan="ISO-8859-1" name="Certificate Of Completion"/>				<desc lan="Big5" name="證書"/>				<desc lan="GB2312" name="证书"/>			</title>			<field111 id="1">				<title>					<desc lan="ISO-8859-1" name="Disable"/>					<desc lan="Big5" name="不適用"/>					<desc lan="GB2312" name="不适用"/>				</title>			</field111>			<field111 id="2">				<title>					<desc lan="ISO-8859-1" name="Enable, use the certificate:"/>					<desc lan="Big5" name="使用該證書:"/>					<desc lan="GB2312" name="使用该证书:"/>				</title>			</field111>		</field111>		-->		<!-- <field58>			<title>				<desc lan="ISO-8859-1" name="Enrollment confirmation remarks"/>				<desc lan="Big5" name="確認報名備註"/>				<desc lan="GB2312" name="确认报名备注"/>			</title>		</field58> -->		<field23 type="notify_email_limited" external_field="yes" paramname="itm_notify_days">			<title>				<desc lan="ISO-8859-1" name="Send course end date notification email"/>				<desc lan="Big5" name="發送結束提醒郵件"/>				<desc lan="GB2312" name="发送结束提醒邮件"/>			</title>		</field23>		<field24 type="notify_support_email" external_field="yes" paramname="itm_notify_email">			<title>				<desc lan="ISO-8859-1" name="Support email"/>				<desc lan="Big5" name="支持郵件"/>				<desc lan="GB2312" name="支持邮件"/>			</title>		</field24>		<!--		<targeted_lrn type="targeted_lrn_pickup" paramname="target_ent_group_lst" required="yes" label="lab_all_learners">			<title>				<desc lan="ISO-8859-1" name="Target enrollments"/>				<desc lan="Big5" name="可報名學員"/>				<desc lan="GB2312" name="可报名学员"/>			</title>		</targeted_lrn>		-->		<!--		<field60>			<title>				<desc lan="ISO-8859-1" name="Course related materials"/>				<desc lan="Big5" name="相關資料"/>				<desc lan="GB2312" name="相关资料"/>			</title>			<subfield_list>				<subfield id="1">					<title>						<desc lan="ISO-8859-1" name="File 1"/>						<desc lan="Big5" name="文件1"/>						<desc lan="GB2312" name="文件1"/>					</title>				</subfield>				<subfield id="2">					<title>						<desc lan="ISO-8859-1" name="File 2"/>						<desc lan="Big5" name="文件2"/>						<desc lan="GB2312" name="文件2"/>					</title>				</subfield>				<subfield id="3">					<title>						<desc lan="ISO-8859-1" name="File 3"/>						<desc lan="Big5" name="文件3"/>						<desc lan="GB2312" name="文件3"/>					</title>				</subfield>				<subfield id="4">					<title>						<desc lan="ISO-8859-1" name="File 4"/>						<desc lan="Big5" name="文件4"/>						<desc lan="GB2312" name="文件4"/>					</title>				</subfield>				<subfield id="5">					<title>						<desc lan="ISO-8859-1" name="File 5"/>						<desc lan="Big5" name="文件5"/>						<desc lan="GB2312" name="文件5"/>					</title>				</subfield>				<subfield id="6">					<title>						<desc lan="ISO-8859-1" name="File 6"/>						<desc lan="Big5" name="文件6"/>						<desc lan="GB2312" name="文件6"/>					</title>				</subfield>				<subfield id="7">					<title>						<desc lan="ISO-8859-1" name="File 7"/>						<desc lan="Big5" name="文件7"/>						<desc lan="GB2312" name="文件7"/>					</title>				</subfield>				<subfield id="8">					<title>						<desc lan="ISO-8859-1" name="File 8"/>						<desc lan="Big5" name="文件8"/>						<desc lan="GB2312" name="文件8"/>					</title>				</subfield>				<subfield id="9">					<title>						<desc lan="ISO-8859-1" name="File 9"/>						<desc lan="Big5" name="文件9"/>						<desc lan="GB2312" name="文件9"/>					</title>				</subfield>				<subfield id="10">					<title>						<desc lan="ISO-8859-1" name="File 10"/>						<desc lan="Big5" name="文件10"/>						<desc lan="GB2312" name="文件10"/>					</title>				</subfield>			</subfield_list>		</field60>	-->		<field22>			<title>				<desc lan="ISO-8859-1" name="Completion settle-date extension"/>				<desc lan="Big5" name="延長結訓結算時間"/>				<desc lan="GB2312" name="延长结训结算时间"/>			</title>			<suffix>				<desc lan="ISO-8859-1" name="days after class end date"/>				<desc lan="Big5" name="天(從結束日期算起)"/>				<desc lan="GB2312" name="天(从结束日期算起)"/>			</suffix>		</field22>		<field25>			<title>				<desc lan="ISO-8859-1" name="Activate Point Calculation"/>				<desc lan="Big5" name="自動積分"/>				<desc lan="GB2312" name="自动积分"/>			</title>			<field25 id="1">				<title>					<desc lan="ISO-8859-1" name="Yes"/>					<desc lan="Big5" name="是"/>					<desc lan="GB2312" name="是"/>				</title>			</field25>			<field25 id="2">				<title>					<desc lan="ISO-8859-1" name="No"/>					<desc lan="Big5" name="否"/>					<desc lan="GB2312" name="否"/>				</title>			</field25>		</field25>		<field26>			<title>				<desc lan="ISO-8859-1" name="Difficulty factor"/>				<desc lan="Big5" name="難度係數"/>				<desc lan="GB2312" name="难度系数"/>			</title>		</field26>		<!--  <field98>			<title>				<desc lan="ISO-8859-1" name="Plan code"/>				<desc lan="Big5" name="計劃編號"/>				<desc lan="GB2312" name="计划编号"/>			</title>		</field98>  -->				<create_date type="read_datetime" external_field="yes">			<title>				<desc lan="ISO-8859-1" name="Created"/>				<desc lan="Big5" name="創建日期"/>				<desc lan="GB2312" name="创建日期"/>			</title>		</create_date>		<created_by type="read" external_field="yes">			<title>				<desc lan="ISO-8859-1" name="Created by"/>				<desc lan="Big5" name="創建者"/>				<desc lan="GB2312" name="创建者"/>			</title>		</created_by>		<update_date type="read_datetime" external_field="yes">			<title>				<desc lan="ISO-8859-1" name="Modifed"/>				<desc lan="Big5" name="修改日期"/>				<desc lan="GB2312" name="修改日期"/>			</title>		</update_date>		<updated_by type="read" external_field="yes">			<title>				<desc lan="ISO-8859-1" name="Modified by"/>				<desc lan="Big5" name="修改者"/>				<desc lan="GB2312" name="修改者"/>			</title>		</updated_by>	</section>	<hidden>		<field150/>		<!--		<field151/>		<field152/>		<field153/>		<field154/>		-->		<field155/>	</hidden></template_view>'
where tvw_tpl_id='13' and tvw_id='DETAIL_VIEW'

commit;

/**
Auth: Jasper
Date: 2018-05-30
Desc: 屏蔽 考试管理，知识管理（知识库，知识目录，知识标签，知识审批），社区管理（问答管理，群组管理），将积分管理变成一级模块
*/
update acFunction set ftn_status = 0 where ftn_id in(633,635,636,663,665,667,668,669,670);
commit;
update acFunction set ftn_level = 0,ftn_parent_id = null where ftn_id =666;
commit;

/**
Auth: Jasper
Date: 2018-05-31
Desc: 调整积分管理位置，原来的积分管理ftn_order为52，现改成跟社区管理同一order，为了页面显示排序，往后开放社区改回
*/
update acFunction set ftn_order = 8 where ftn_id = 666;
commit;

/**
Auth: Jasper
Date: 2018-05-31
Desc: 屏蔽社区相关的自动积分：知识分享，创建群组，参与群组，在线回答得分，回答被采纳为最佳答案得分，取消提问，在线提问得分
	（并非按顺序对应）'KB_SHARE_KNOWLEDGE','SYS_CREATE_GROUP','SYS_JION_GROUP','ZD_COMMIT_ANS','ZD_RIGHT_ANS','ZD_CANCEL_QUE','ZD_NEW_QUE'
*/
update creditsType set cty_deleted_ind = 1 where cty_id in(2,3,4,6,32,33,39);
commit;

/**
Auth: Peng
Date: 2018-07-10
Desc：屏蔽CPT/D管理子栏目CPT/D历史保存管理功能（Bug 19068 - 歷史記錄功能要刪走, 不會再用 (安排空閒時間再做)）
*/
update acFunction set ftn_status = 0 where ftn_id = 725;
commit;


/**
Auth: Peng
Date: 2018-08-07
Desc：Bug 19372 - 郵件模板類型為 “USR_REG_DISAPPROVE” 發出的郵件里 Learner name 沒有被置換）
*/
update 

messageTemplate set mtp_content = '[Header image]
<p>
	<span> </span><span style="color:black;font-family:宋体;font-size:11pt;">Dear </span><span style="color:blue;font-family:宋体;font-size:11pt;"><span style="color:#0000FF;font-family:宋体;font-size:14.6667px;">[Learner name]</span></span><span style="color:black;font-family:宋体;font-size:11pt;">,<br />
<br />
Please be informed that your student account registration has   been declined. The reason given by your local faculty is as follows: </span><span style="color:blue;font-family:宋体;font-size:11pt;">[Refuse reason]<br />
<br />
</span><span style="color:black;font-family:宋体;font-size:11pt;">If   you have any questions, please contact your local faculty.</span><span style="color:blue;font-family:宋体;font-size:11pt;"><br />
<br />
</span><span style="color:black;font-family:宋体;font-size:11pt;">Regards,<br />
Training &amp; Development </span> 
</p>
<span> </span> [Footer image]'

where mtp_type = 'USR_REG_DISAPPROVE' and mtp_tcr_id = (select tcr_id from  tcTrainingCenter where tcr_parent_tcr_id is null)
commit;

/**
Auth: Peng
Date: 2018-08-07
Desc：Bug 19369 - 所有課程類相關的郵件,當使用自定義的支援電郵時,送件人名稱不應該是System
*/
insert into Entity (ent_type, ent_upd_date, ent_syn_date, ent_ste_uid, ent_syn_ind)
values ('USR', getdate(), NULL, NULL, 1)
declare @usr_ent_id int
select @usr_ent_id = max(ent_id) from Entity

insert into regUser (usr_id, usr_ent_id, usr_display_bil, usr_signup_date, usr_last_login_date, usr_status, usr_upd_date, usr_ste_ent_id, usr_ste_usr_id,usr_syn_rol_ind)
values
('s1u'+cast(@usr_ent_id as varchar), @usr_ent_id, 'Course support', getdate(), getdate(), 'SYS', getdate(), 1, 'peng',1)
commit;


/**
Auth: Jacky
Date: 2018-08-20
Desc: Bug 19488 - Joining Instruction 邮件默認模板有重複的內容，请删掉
 */
UPDATE messageTemplate SET mtp_content = '[Header image]
<p>
	<span> </span><span style="font-family:&amp;font-size:10pt;">Dear   all, <br />
<br />
I am pleased to offer you a place in our In-house   Training Course, <span style="color:blue;">[Course name]</span>: <span style="color:blue;">[Class name]</span>, scheduled for <span style="color:blue;">[Course period start date]</span> to <span style="color:blue;">[Course period end date]</span>.<br />
<br />
You will be required to complete the assigned activities/exercises,   submit the course assignments and/or attend examinations, if any,   through the E-learning portal Link. By the end of the course, you   should complete and return an online course evaluation to us through   the portal. A Certificate of Completion will be issued to you if you   satisfy all the requirements including passing of the   assignments/examination. To recognize your effort, information on your   attendance and/or assessment results will be reported to your head   after the Course. <br />
<br />
Kindly let me know in advance if you   are unable to attend the above course. <br />
<br />
The names of   participants are listed below for your reference:- <br />
<span style="color:blue;">[Learner(s)]</span><br />
<br />
<br />
<span style="color:blue;">[Training Administrator]</span><br />
Training   Team<br />
Human Resources Unit </span> 
</p>
<p>
	<br />
</p>
<span> </span> [Footer image]' WHERE mtp_type='JI';
commit;


/**
Auth: Jacky
Date: 2018-08-20
Desc: Bug 19385 - 删除“JI” 及 “REMINDER” 邮件模板里的参数 [Learner(s)]用户名单及邮件里的内容
 */
UPDATE messageTemplate SET mtp_content = '[Header image]
<p>
	<span> </span><span style="font-family:&amp;font-size:10pt;">Dear   all, <br />
<br />
I am pleased to offer you a place in our In-house   Training Course, <span style="color:blue;">[Course name]</span>: <span style="color:blue;">[Class name]</span>, scheduled for <span style="color:blue;">[Course period start date]</span> to <span style="color:blue;">[Course period end date]</span>.<br />
<br />
You will be required to complete the assigned activities/exercises,   submit the course assignments and/or attend examinations, if any,   through the E-learning portal Link. By the end of the course, you   should complete and return an online course evaluation to us through   the portal. A Certificate of Completion will be issued to you if you   satisfy all the requirements including passing of the   assignments/examination. To recognize your effort, information on your   attendance and/or assessment results will be reported to your head   after the Course. <br />
<br />
Kindly let me know in advance if you   are unable to attend the above course. <br />
</span>
</p>
<p>
	<span style="font-family:&amp;font-size:10pt;"><br />
<span style="color:blue;">[Training Administrator]</span><br />
Training   Team<br />
Human Resources Unit </span> 
</p>
<p>
	<br />
</p>
<span> </span> [Footer image]' WHERE mtp_type='JI';
commit;

UPDATE messageTemplate SET mtp_content = '﻿﻿[Header image]
<p>
	<span> </span><span style="font-family:&amp;font-size:10pt;">Dear   all, <br />
<br />
I am pleased to offer you a place in our In-house   Training Course, <span style="color:blue;">[Course name]</span>: <span style="color:blue;">[Class name]</span>, scheduled for <span style="color:blue;">[Course period start date]</span> to <span style="color:blue;">[Course period end date]</span>.<br />
<br />
You will be required to complete the assigned activities/exercises,   submit the course assignments and/or attend examinations, if any,   through the E-learning portal Link. By the end of the course, you   should complete and return an online course evaluation to us through   the portal. A Certificate of Completion will be issued to you if you   satisfy all the requirements including passing of the   assignments/examination. To recognize your effort, information on your   attendance and/or assessment results will be reported to your head   after the Course. <br />
<br />
Kindly let me know in advance if you   are unable to attend the above course. <br />
<br />
<br />
<span style="color:blue;">[Training Administrator]</span><br />
Training   Team<br />
Human Resources Unit </span> 
</p>
<span> </span> [Footer image]' WHERE mtp_type='REMINDER';
commit;

DELETE FROM messageParamName  WHERE mpn_mtp_id IN (select mtp_id from messageTemplate where mtp_type='JI') AND mpn_name = '[Learner(s)]';
commit;

DELETE FROM messageParamName WHERE mpn_mtp_id IN (select mtp_id from messageTemplate where mtp_type='REMINDER') AND mpn_name = '[Learner(s)]';
commit;


/*   请保持该段话在本文件的最后;（如果谁不更新，抓到后，打PP）；
 *   以后谁更新这个文件时，一定要接着下面所指定的版本号之后的必动顺序更新，并同时更新最后版本, 即你更新后这个文件后，会与db_changes.sql文件的哪个版本同步；
 *   如果要把SQL写在这里，要先确保db_changes.sql(即符合MSSQL数据库语法的SQL)已有对应的SQL。

Dear All, 
为了防止Oracle11g新增表在制作空库时漏掉，开发人员必需在新增表后增加以下脚本
alter table [TABLE_NAME] allocate extent;

 */
