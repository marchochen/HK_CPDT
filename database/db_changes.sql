/*
Author: Dennis Yip
Date: 2001-12-14
Desc: Create Index for performance tuning
      Note that the script for MS SQL and Oracle are different
*/
--for MS SQL
CREATE INDEX [IX_ResourceContent] ON [dbo].[ResourceContent] ([rcn_res_id_content])
WITH FILLFACTOR = 90
ON [PRIMARY]

CREATE CLUSTERED INDEX [IX_Assignment] ON [dbo].[Assignment] ([ass_due_datetime])
WITH FILLFACTOR = 90
ON [PRIMARY]

CREATE CLUSTERED INDEX [IX_Event] ON [dbo].[Event] ([evt_datetime])
WITH FILLFACTOR = 90
ON [PRIMARY]

CREATE CLUSTERED INDEX [IX_Module] ON [dbo].[Module] ([mod_eff_start_datetime])
WITH FILLFACTOR = 90
ON [PRIMARY]

CREATE CLUSTERED INDEX [IX_Course] ON [dbo].[Course] ([cos_eff_start_datetime], [cos_eff_end_datetime])
WITH FILLFACTOR = 90
ON [PRIMARY]

CREATE INDEX [IX_ModuleEvaluation] ON [dbo].[ModuleEvaluation] ([mov_ent_id])
WITH FILLFACTOR = 90
ON [PRIMARY]
--for Oracle
CREATE INDEX "SA"."IDX_RCN_RES_ID_CONTENT" ON
"SA"."RESOURCECONTENT"("RCN_RES_ID_CONTENT")

CREATE INDEX "SA"."IDX_ASS_DUE_DATETIME" ON
"SA"."ASSIGNMENT"("ASS_DUE_DATETIME")


CREATE INDEX "SA"."IDX_EVT_DATETIME" ON
"SA"."EVENT"("EVT_DATETIME")

CREATE INDEX "SA"."IDX_MOD_EFF_START_DATETIME" ON
"SA"."MODULE"("MOD_EFF_START_DATETIME")

CREATE INDEX "SA"."IDX_COS_EFF_DATETIME" ON
"SA"."COURSE"("COS_EFF_START_DATETIME", "COS_EFF_END_DATETIME")

CREATE INDEX "SA"."IDX_MOV_ENT_ID" ON
"SA"."MODULEEVALUATION"("MOV_ENT_ID")


/*
Author: Dennis Yip
Date: 2001-12-14
Desc: new column ite_type added for TARGETED_ENROLMENT and TARGETED_LEARNER for 3.5
*/
alter table aeItemTargetEntity add ite_type varchar(50) NULL
update aeItemTargetEntity set ite_type = 'TARGETED_LEARNER'
alter table aeItemTargetEntity alter column ite_type varchar(50)

/*
Author: Dennis Yip
Date: 2001-12-14
Desc: Create new System Message for item and queue manager
*/
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('AEIT08', 'ISO-8859-1','Item has already been approved')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('AEIT08', 'Big5','Item has already been approved')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('AEIT08', 'GB2312','Item has already been approved')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('AEIT09', 'ISO-8859-1','Run cannot has new version')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('AEIT09', 'Big5','Run cannot has new version')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('AEIT09', 'GB2312','Run cannot has new version')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('AEIT10', 'ISO-8859-1','Item has not been completed')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('AEIT10', 'Big5','Item has not been completed')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('AEIT10', 'GB2312','Item has not been completed')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('AEIT11', 'ISO-8859-1','Item has already been cancelled')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('AEIT11', 'Big5','Item has already been cancelled')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('AEIT11', 'GB2312','Item has already been cancelled')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('AEQM06', 'ISO-8859-1','No application selected')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('AEQM06', 'Big5','No application selected')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('AEQM06', 'GB2312','No application selected')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('AEQM07', 'ISO-8859-1', 'Item not ready to be applied')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('AEQM07', 'Big5','Item not ready to be applied')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('AEQM07', 'GB2312','Item not ready to be applied')


/*
Author: Chris Lo
Date: 2001-12-15
Desc: new column obj_ancester for finding ancester of an objective
*/
ALTER TABLE dbo.Objective ADD obj_ancester varchar(255) NULL
CREATE  INDEX [IX_Objective_ancester] ON [dbo].[Objective] ([obj_ancester])
WITH    FILLFACTOR = 90




/*
Author: Alan Kam
Date: 2001-12-17
Desc: Create new Table QSequence, QPointer, Assessment
*/

if exists (select * from sysobjects where id = object_id(N'[dbo].[QPointer]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [dbo].[QPointer]
GO

if exists (select * from sysobjects where id = object_id(N'[dbo].[QSequence]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [dbo].[QSequence]
GO

CREATE TABLE [dbo].[QPointer] (
	[qpt_qse_mod_id] [int] NOT NULL ,
	[qpt_qse_order_ptr] [int] NOT NULL ,
	[qpt_max_ptr] [int] NOT NULL
)
GO

CREATE TABLE [dbo].[QSequence] (
	[qse_mod_id] [int] NOT NULL ,
	[qse_order] [int] NOT NULL ,
	[qse_que_res_id] [int] NOT NULL ,
	[qse_obj_id_ancestor] [int] NOT NULL ,
	[qse_obj_id] [int] NOT NULL
)
GO

ALTER TABLE [dbo].[QPointer] WITH NOCHECK ADD
	CONSTRAINT [PK_QPOINTER_MOD_ID] PRIMARY KEY  CLUSTERED
	(
		[qpt_qse_mod_id]
	) WITH  FILLFACTOR = 90  ON [PRIMARY]
GO

ALTER TABLE [dbo].[QSequence] WITH NOCHECK ADD
	CONSTRAINT [PK_QSEQUENCE_ID] PRIMARY KEY  CLUSTERED
	(
		[qse_mod_id],
		[qse_order]
	) WITH  FILLFACTOR = 90  ON [PRIMARY]
GO

ALTER TABLE [dbo].[QPointer] ADD
	CONSTRAINT [FK_QPOINTER_RESOURCES_ID] FOREIGN KEY
	(
		[qpt_qse_mod_id]
	) REFERENCES [dbo].[Resources] (
		[res_id]
	)
GO

ALTER TABLE [dbo].[QSequence] ADD
	CONSTRAINT [FK_QSEQUENCE_OBJECTIVE_ID] FOREIGN KEY
	(
		[qse_obj_id]
	) REFERENCES [dbo].[Objective] (
		[obj_id]
	),
	CONSTRAINT [FK_QSEQUENCE_OBJECTIVE_ID_ANCESTOR] FOREIGN KEY
	(
		[qse_obj_id_ancestor]
	) REFERENCES [dbo].[Objective] (
		[obj_id]
	),
	CONSTRAINT [FK_QSEQUENCE_QUESTION_QUE_ID] FOREIGN KEY
	(
		[qse_que_res_id]
	) REFERENCES [dbo].[Question] (
		[que_res_id]
	)
GO



CREATE TABLE [dbo].[Assessment] (
	asm_res_id int NOT NULL ,
	asm_que_num smallint NOT NULL ,
	asm_que_src char (10) NOT NULL ,
	asm_que_duration smallint NOT NULL ,
	asm_que_hint_ind bit NOT NULL ,
	asm_round_duration smallint NOT NULL,
	asm_neg_score_ind bit NOT NULL ,
	asm_save_score_ind bit NOT NULL ,
	asm_trump_num smallint NOT NULL ,
	asm_trump_multiplier smallint NOT NULL ,
	asm_diff_multiplier smallint NOT NULL ,
	asm_daily_start_datetime datetime NOT NULL ,
	asm_daily_end_datetime datetime NOT NULL ,
	asm_score_dec_ind bit NOT NULL ,
	asm_show_ans_ind bit NOT NULL ,
	asm_type char (10) NOT NULL ,
	asm_mode char (10) NULL ,
	asm_rom_host char (50) NULL ,
	asm_rom_port smallint NULL ,
	asm_www_port smallint NULL,
	asm_chat_ind bit NOT NULL,
	CONSTRAINT PK_ASSESSMENT_ID PRIMARY KEY ( asm_res_id ),
	CONSTRAINT FK_ASSESSMENT_RESOURCES_ID FOREIGN KEY ( asm_res_id )
	REFERENCES Resources ( res_id )
)

/*
Author: Dennis Yip
Date: 2001-12-17
Desc: Update gpm_ancester from '123 ,456, 789 ' to ' 123 , 456 , 789 '
*/
update GroupMember set gpm_ancester = replace(gpm_ancester, ' ,', '*') where gpm_ancester is not null
update GroupMember set gpm_ancester = replace(gpm_ancester, '*', ' , ') where gpm_ancester is not null
update GroupMember set gpm_ancester = ' ' + gpm_ancester where gpm_ancester is not null


/*
Author Alan Kam
Date: 2001-12-18
Desc: Extend mod_is_public field, check the public module need to enroll before access
*/
alter table Module add mod_public_need_enrol bit NULL
UPDATE Module SET mod_public_need_enrol = 0
alter table Module alter column mod_public_need_enrol bit NOT NULL


/*
Author Alan Kam
Date: 2001-12-27
Desc: Modify the request param of the
*/
UPDATE xslParamName SET xpn_name = 'cc_ent_ids' where xpn_id = 391
UPDATE xslParamName SET xpn_name = 'id_type' where xpn_id = 392
DELETE xslParamName WHERE xpn_id in ( 393, 394, 395 )

UPDATE xslParamName SET xpn_name = 'cc_ent_ids' where xpn_id = 400
UPDATE xslParamName SET xpn_name = 'id_type' where xpn_id = 401
DELETE xslParamName WHERE xpn_id in ( 402, 403, 404 )


/*
Author: Dennis Yip
Date: 2001-12-27
Desc: Put the initial itm_life_status into aeItemType for create item
*/
alter table aeItemType add ity_init_life_status varchar(50) NULL
update aeItemType set ity_init_life_status = 'IN_PROGRESS' where ity_id in ('CLASSROOM', 'SELFSTUDY')

/*
Author: Stanley Lam
Date: 2002-01-03
Desc: Add ste_targeted_entity_lst into acSite (for the tree view of Targeted Learner)
*/
alter table acSite add ste_targeted_entity_lst varchar(50) NULL

/*
Author: Dennis Yip
Date: 2002-01-03
Desc: new system messages
*/
insert into systemmessage values ('ENR002', 'ISO-8859-1', 'Not Enough Licence')
insert into systemmessage values ('ENR002', 'Big5', 'Not Enough Licence')
insert into systemmessage values ('ENR002', 'GB2312', 'Not Enough Licence')

/*
Author: Dennis Yip
Date: 2002-01-03
Desc: new table for licence management
*/
CREATE TABLE [dbo].[ctLicence] (
	[lic_key] [varchar] (50) NOT NULL ,
	[lic_ent_id_root] [int] NOT NULL ,
	[lic_quota] [int] NOT NULL ,
	[lic_desc] [varchar] (255) NULL
) ON [PRIMARY]
GO
-- make lic_key and lic_ent_id_root as key
alter table course add cos_lic_key varchar(50)

/*
Author: Chris
Date: 2002-01-08
Desc: Update resourceobjective of standard test question
*/
insert resourceobjective
select rcn_res_id_content, rcn_obj_id_content from ResourceContent , Resources
where res_id = rcn_res_id_content and res_type='QUE' and res_id not in (select rob_res_id from resourceobjective)
and rcn_obj_id_content > 0

/*
Author: Dennis Yip
Date: 2002-01-08
Desc: Rename aeCodeType, aeCodeTable to CodeType and CodeTable
*/
CREATE TABLE [dbo].[CodeType] (
	[ctp_id] [nvarchar] (50) NOT NULL ,
	[ctp_sys_ind] [bit] NOT NULL ,
	[ctp_title] [nvarchar] (50) NOT NULL ,
	[ctp_create_timestamp] [datetime] NOT NULL ,
	[ctp_create_usr_id] [varchar] (20) NOT NULL
) ON [PRIMARY]
CREATE TABLE [dbo].[CodeTable] (
	[ctb_type] [nvarchar] (50) NOT NULL ,
	[ctb_id] [nvarchar] (50) NOT NULL ,
	[ctb_title] [nvarchar] (50) NULL ,
	[ctb_xml] [ntext] NULL ,
	[ctb_create_timestamp] [datetime] NOT NULL ,
	[ctb_create_usr_id] [varchar] (20) NOT NULL ,
	[ctb_upd_timestamp] [datetime] NOT NULL ,
	[ctb_upd_usr_id] [varchar] (20) NOT NULL
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
ALTER TABLE [dbo].[CodeType] WITH NOCHECK ADD
	CONSTRAINT [PK_CodeType] PRIMARY KEY  NONCLUSTERED
	(
		[ctp_id]
	) WITH  FILLFACTOR = 90  ON [PRIMARY]
ALTER TABLE [dbo].[CodeTable] ADD
	CONSTRAINT [FK_CodeTable_CodeType] FOREIGN KEY
	(
		[ctb_type]
	) REFERENCES [dbo].[CodeType] (
		[ctp_id]
	)
insert into CodeType select * from aeCodeType
insert into CodeTable SElect * from aeCodeTable
drop table aeCodeTable
drop table aeCodeType

/*
Author: Dennis Yip
Date: 2002-01-08
Desc: New System Messages
*/
insert into SystemMessage values ('AEQM08', 'ISO-8859-1', 'Enrolment email is sent')
insert into SystemMessage values ('AEQM08', 'Big5', 'Enrolment email is sent')
insert into SystemMessage values ('AEQM08', 'GB2312', 'Enrolment email is sent')

/*
Author: Kim
Date: 2002-01-15
Desc: New column for GroupMember
*/
alter table groupmember add gpm_remain_on_syn bit NULL
update groupmember set gpm_remain_on_syn = 0
alter table groupmember alter column gpm_remain_on_syn bit NOT NULL


/*
Author: Chris
Date: 2002-01-15
Desc: Updated tables for Question upload
*/

ALTER TABLE [dbo].[RawQuestion] DROP CONSTRAINT FK_RawQuestion_UploadLog

if exists (select * from sysobjects where id = object_id(N'[dbo].[UploadLog]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [dbo].[UploadLog]

if exists (select * from sysobjects where id = object_id(N'[dbo].[RawQuestion]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [dbo].[RawQuestion]

CREATE TABLE [dbo].[UploadLog] (
	[ulg_id] [int] IDENTITY (1, 1) NOT NULL ,
	[ulg_type] [varchar] (50) NOT NULL ,
	[ulg_subtype] [varchar] (50) NULL ,
	[ulg_usr_id_owner] [varchar] (50) NOT NULL ,
	[ulg_path] [nvarchar] (255) NOT NULL ,
	[ulg_file_name] [nvarchar] (50) NOT NULL ,
	[ulg_file_lastmod] [varchar] (50) NOT NULL ,
	[ulg_file_size] [int] NOT NULL ,
	[ulg_rec_cnt] [smallint] NOT NULL ,
	[ulg_success_cnt] [smallint] NOT NULL ,
	[ulg_status] [varchar] (50) NOT NULL ,
	[ulg_create_datetime] [datetime] NOT NULL ,
	[ulg_upd_datetime] [datetime] NOT NULL
) ON [PRIMARY]

CREATE TABLE [dbo].[RawQuestion] (
	[raq_ulg_id] [int] NOT NULL ,
	[raq_line_num] [smallint] NOT NULL ,
	[raq_title] [nvarchar] (255) NOT NULL ,
	[raq_lan] [varchar] (50) NOT NULL ,
	[raq_type] [varchar] (50) NOT NULL ,
	[raq_status] [varchar] (50) NOT NULL ,
	[raq_difficulty] [smallint] NOT NULL ,
	[raq_score] [smallint] NOT NULL ,
	[raq_duration] [float] NOT NULL ,
	[raq_privilege] [varchar] (10) NOT NULL ,
	[raq_usr_id_owner] [varchar] (20) NOT NULL ,
	[raq_obj_id] [int] NOT NULL ,
	[raq_que_xml] [ntext] NOT NULL ,
	[raq_int_cnt] [smallint] NOT NULL ,
	[raq_outcome_xml_1] [ntext] NOT NULL ,
	[raq_explain_xml_1] [ntext] NOT NULL
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

ALTER TABLE [dbo].[UploadLog] WITH NOCHECK ADD
	CONSTRAINT [PK_UploadLog] PRIMARY KEY  NONCLUSTERED
	(
		[ulg_id]
	) WITH  FILLFACTOR = 90  ON [PRIMARY]

ALTER TABLE [dbo].[RawQuestion] WITH NOCHECK ADD
	CONSTRAINT [PK_RawQuestion] PRIMARY KEY  NONCLUSTERED
	(
		[raq_ulg_id],
		[raq_line_num]
	) WITH  FILLFACTOR = 90  ON [PRIMARY]

ALTER TABLE [dbo].[RawQuestion] ADD
	CONSTRAINT [FK_RawQuestion_UploadLog] FOREIGN KEY
	(
		[raq_ulg_id]
	) REFERENCES [dbo].[UploadLog] (
		[ulg_id]
	)


/*
Author : Lun
DESC : Add a column in mgMessage for BCC prupose
DATE : 2002-01-22
*/
alter table mgMessage add msg_bcc_sys_ind bit NULL
UPDATE mgMessage SET msg_bcc_sys_ind = 0
alter table mgMessage alter column msg_bcc_sys_ind bit NOT NULL

/*
Author: Stanley
DESC : Add a column in acRole for changing skin
DATE : 2002-01-22
*/
alter table acRole add rol_skin_root varchar(50) NULL
UPDATE acRole set rol_skin_root = 'skin1'
alter table acRole alter column rol_skin_root varchar NOT NULL

/*
Author: Kawai
Date: 2001-01-24
Desc: Create tables and index for Booking system

**Please refer to booking_system.sql
*/

/*
Author: Dennis Yip
Date : 2002-01-25
Desc : Add a column in aeItem to refer to Booking system
*/
ALTER TABLE aeItem ADD
itm_rsv_id INT NULL
ALTER TABLE aeItem ADD
CONSTRAINT FK_aeItem_fmReservation FOREIGN KEY
(itm_rsv_id) REFERENCES fmReservation (rsv_id)

/*
Author: Dennis Yip
Date: 2002-01-28
Desc: Add a column for template view to refer to booking system
*/
alter table aeTemplateView add tvw_rsv_ind bit
update aeTemplateView set tvw_rsv_ind = 0
alter table aeTemplateView alter column tvw_rsv_ind bit NOT NULL

/*
Author: Chris
Date: 2002-01-31
Desc: Add system message for updating question
*/
insert into SystemMessage values ('QUE002', 'ISO-8859-1', 'The question has been attempted.')
insert into SystemMessage values ('QUE002', 'Big5', 'The question has been attempted.')
insert into SystemMessage values ('QUE002', 'GB2312', 'The question has been attempted.')

/*
Author: Kim
Date: 2002-02-05
Desc: Add system message to replace the server error message : no mote report
*/

insert into systemmessage values ('MOT005', 'ISO-8859-1', 'The Learning Solution does not have a MOTE report.')
insert into systemmessage values ('MOT005', 'BIG5', '(BIG5)The Learning Solution does not have a MOTE report.')
insert into systemmessage values ('MOT005', 'GB2312', '(GB2312)The Learning Solution does not have a MOTE report.')

/*
Author: Stanley
DESC : Add a column in userGrade for ordering
DATE : 2002-02-07
*/
alter table usergrade add ugr_seq_no int NULL

/*
Author: Dennis Yip
DESC : Add function for reading of pre-approve item in item search
DATE : 2002-02-07
*/
Insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('ITM_PRE_APPR_READ', 'FUNCTION', 'ITEM', '2002-02-07', NULL)

/*
Author: Dennis Yip
DESC : Add index for booking system
DATE : 2002-02-19
*/
CREATE  INDEX [IX_fmFacilitySchedule_fsh_rsv_id] ON [dbo].[fmFacilitySchedule]([fsh_rsv_id]) ON [PRIMARY]
GO

CREATE  INDEX [IX_fmFacilitySchedule_fsh_date] ON [dbo].[fmFacilitySchedule]([fsh_date]) ON [PRIMARY]
GO

CREATE  INDEX [IX_fmFacilitySchedule_fsh_status_start_end] ON [dbo].[fmFacilitySchedule]([fsh_start_time], [fsh_end_time], [fsh_status]) ON [PRIMARY]
GO

CREATE  INDEX [IX_fmFacility_fac_ftp_id] ON [dbo].[fmFacility]([fac_ftp_id]) ON [PRIMARY]
GO

/*
Author: Chris
DESC : Add code for catalog
DATE : 2002-02-21
*/
ALTER TABLE dbo.aeCatalog ADD
	cat_code nvarchar(50) NULL


/*
Author: Dennis Yip
Desc: Add new column for aeItemType
DATE: 2002-02-21
*/
ALTER TABLE [dbo].[aeCatalogItemType] DROP CONSTRAINT FK_aeCatalogItemType_aeItemType1
Go
alter table aeItemType add ity_run_ind int NULL
Go
Update aeItemType set ity_run_ind = 0
Go
alter table aeItemType alter column ity_run_ind int NOT NULL
Go
ALTER TABLE [dbo].[aeItemType] DROP CONSTRAINT PK_aeItemType
Go
ALTER TABLE [dbo].[aeItemType] WITH NOCHECK ADD
	CONSTRAINT [PK_aeItemType] PRIMARY KEY  NONCLUSTERED
	(
		[ity_owner_ent_id],
		[ity_id],
		[ity_run_ind]
	) WITH  FILLFACTOR = 90  ON [PRIMARY]
GO
Update aeItemType set ity_title_xml = '<item_type id="CLASSROOM" create_run_ind="true">		<desc lan="ISO-8859-1" name="Classroom"/>		<desc lan="Big5" name="Big5 Classroom"/>		<desc lan="GB2312" name="GB Classroom"/>	</item_type>'
where ity_id = 'CLASSROOM'

/*
Author: Dennis Yip
Desc: Add new column for RegUser for remarks
DATE: 2002-02-25
*/
alter table RegUser add usr_remark_xml ntext NULL
GO
update reguser set usr_remark_xml = '<deletion_remark timestamp="' + convert(varchar, usr_upd_date, 121) + '"/>'
where usr_status = 'DELETED'



/*
Author: Wai Lun
Desc: Alter the column(app_notify_status) datatype in table(aeApplication) from varchar to smallint
DATE: 2002-02-27
*/
UPDATE aeApplication SET app_notify_status = '0' WHERE app_notify_status = '-0'
GO
ALTER TABLE aeApplication ALTER COLUMN app_notify_status SMALLINT



/*
Author: Wai Lun
Desc: Add a column in aeItem to store the cancellation type of the run
DATE: 2002-03-01
*/
ALTER TABLE aeItem ADD itm_cancellation_type nvarchar(255) NULL

/*
Author: Dennis Yip
Desc: Update the templates location from "common" to "htm"
DATE: 2002-03-01
*/
Update template set tpl_thumbnail_url = replace(tpl_thumbnail_url,'../common','../htm')

/*
Author: Kim
Desc: Typo in attendanceStatus xml
DATE: 2002-03-05
*/
update aeattendanceStatus set ats_title_xml = '<status id="2"><desc lan="ISO-8859-1" name="In Progress"/><desc lan="Big5" name="???"/><desc lan="GB2312" name="???"/></status>' where ats_type = 'PROGRESS' and ats_id = 2

/*
Author: Dennis
DESC : Add role status to acRole ('OK' or 'HIDDEN')
DATE : 2002-03-05
*/
ALTER TABLE dbo.acRole ADD rol_status varchar(20) NULL
GO
update acRole set rol_status='OK'
ALTER TABLE dbo.acRole ALTER COLUMN rol_status varchar(20) NOT NULL

/*
Author: Kim
Desc: Add for Sign-on Link
DATE: 2002-03-06
*/

CREATE TABLE [dbo].[acSignonLink] (
	[slk_id] [int] IDENTITY (1, 1) NOT NULL ,
	[slk_ste_id_owner] [int] NULL ,
	[slk_link_title] [nvarchar] (100) NOT NULL ,
	[slk_base_url] [varchar] (255) NOT NULL ,
	[slk_ste_id] [int] NOT NULL ,
	[slk_usr_id] [varchar] (20) NOT NULL ,
	[slk_usr_role] [nvarchar] (255) NULL ,
	[slk_label_lan] [nvarchar] (20) NOT NULL ,
	[slk_url_success] [nvarchar] (1000) NOT NULL ,
	[slk_window_target] [varchar] (10) NOT NULL
) ON [PRIMARY]
GO


insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('SLK_EXEC', 'FUNCTION', 'SIGNON', getDate(), null)

insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('SLK_READ', 'FUNCTION', 'SIGNON', getDate(), null)


/*
Author: Chris
DESC : Increase the column length of ste_domain in acSite
DATE : 2002-03-06
*/
ALTER TABLE dbo.acSite ALTER COLUMN ste_domain nvarchar(255) NOT NULL


/*
Author: Dennis
DESC : New HomePage Function for Signon Link
DATE : 2002-03-06
*/
insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values
('SIGNON_LINK', 'FUNCTION', 'HOMEPAGE', getdate(), '<function id="SIGNON_LINK"><desc lan="ISO-8859-1" name="Links to other wizBanks"/><desc lan="Big5" name="Links to other wizBanks"/><desc lan="GB2312" name="Links to other wizBanks"/></function>')

/*
Author: Kim Chan
DESC : Change the datatype of the display_bil of UserGrade
DATE : 2002-03-08
*/
ALTER TABLE usergrade ALTER COLUMN ugr_display_bil nvarchar(50) NOT NULL

/*
Author: Kim Chan
DESC : Change the datatype of the extra_1 of Reguser
DATE : 2002-03-08
*/
ALTER TABLE reguser ALTER COLUMN usr_extra_1 nvarchar(50) NULL

/*
Author: Kim Chan
DESC : Add extra column in reguser
DATE : 2002-03-08
*/
ALTER TABLE reguser ADD  usr_extra_2 nvarchar(50) NULL
ALTER TABLE reguser ADD  usr_extra_3 nvarchar(50) NULL
ALTER TABLE reguser ADD  usr_extra_4 nvarchar(50) NULL
ALTER TABLE reguser ADD  usr_extra_5 nvarchar(50) NULL
ALTER TABLE reguser ADD  usr_extra_6 nvarchar(50) NULL
ALTER TABLE reguser ADD  usr_extra_7 nvarchar(50) NULL
ALTER TABLE reguser ADD  usr_extra_8 nvarchar(50) NULL
ALTER TABLE reguser ADD  usr_extra_9 nvarchar(50) NULL
ALTER TABLE reguser ADD  usr_extra_10 nvarchar(50) NULL

/*
Author: Chris
DESC : Enhancement of Catalogue and Category
DATE : 2002-03-11
*/
ALTER TABLE dbo.aeTreeNode ADD
	tnd_desc nvarchar(4000) NULL,
	tnd_order int NULL

/*
Author: Chris
DESC : Set the supported language of a site
DATE : 2002-03-12
*/

ALTER TABLE dbo.acSite ADD ste_lan_xml varchar(255) NULL
GO
update acSite SET ste_lan_xml='<encoding id="ISO-8859-1"/>'
ALTER TABLE dbo.acSite ALTER COLUMN ste_lan_xml varchar(255) NOT NULL

/*
Author: Dennis Yip
DESC : Push meta data to aeItemType
DATE : 2002-03-12
*/
alter table aeItemType add ity_create_run_ind bit NULL
alter table aeItemType add ity_apply_ind bit NULL
alter table aeItemType add ity_qdb_ind bit NULL
alter table aeItemType add ity_auto_enrol_qdb_ind bit NULL
GO

update aeItemType set ity_create_run_ind = 1, ity_apply_ind = 0, ity_qdb_ind = 1, ity_auto_enrol_qdb_ind = 0
where ity_id = 'CLASSROOM'
update aeItemType set ity_create_run_ind = 0, ity_apply_ind = 1, ity_qdb_ind = 1, ity_auto_enrol_qdb_ind = 0
where ity_id = 'SELFSTUDY'
update aeItemType set ity_create_run_ind = 0, ity_apply_ind = 1, ity_qdb_ind = 0, ity_auto_enrol_qdb_ind = 0
where ity_id = 'PROGRAM'
update aeItemType set ity_create_run_ind = 0, ity_apply_ind = 0, ity_qdb_ind = 0, ity_auto_enrol_qdb_ind = 0
where ity_id = 'BOOK'
update aeItemType set ity_create_run_ind = 0, ity_apply_ind = 0, ity_qdb_ind = 0, ity_auto_enrol_qdb_ind = 0
where ity_id = 'VIDEO'
update aeItemType set ity_create_run_ind = 0, ity_apply_ind = 0, ity_qdb_ind = 0, ity_auto_enrol_qdb_ind = 0
where ity_id = 'WEBSITE'
update aeItemType set ity_create_run_ind = 0, ity_apply_ind = 0, ity_qdb_ind = 0, ity_auto_enrol_qdb_ind = 0
where ity_id = 'EXTCOURSE'
update aeItemType set ity_create_run_ind = 0, ity_apply_ind = 0, ity_qdb_ind = 1, ity_auto_enrol_qdb_ind = 1
where ity_id = 'SELFSTUDYINT'

/*Create run record if necessary*/
insert into aeItemType
(ity_owner_ent_id, ity_id, ity_seq_id, ity_title_xml, ity_icon_url, ity_create_timestamp, ity_create_usr_id, ity_init_life_status, ity_run_ind, ity_create_run_ind, ity_apply_ind, ity_qdb_ind, ity_auto_enrol_qdb_ind)
values
(1, 'CLASSROOM', 8, '<item_type id="CLASSROOM"><desc lan="ISO-8859-1" name="Classroom"/><desc lan="Big5" name="Big5 Classroom"/><desc lan="GB2312" name="GB Classroom"/></item_type>', null, getdate(), 's1u3', null, 1, 0, 1, 0, 0)

alter table aeItemType alter column ity_create_run_ind bit
alter table aeItemType alter column ity_apply_ind bit
alter table aeItemType alter column ity_qdb_ind bit
alter table aeItemType alter column ity_auto_enrol_qdb_ind bit

/*
Author: Kim Chan
DESC : for reminder criteria
DATE : 2002-03-15
*/

alter table courseCriteria add 	[ccr_type] [varchar] (50) NULL
alter table courseCriteria add 	[ccr_cos_id] [int] NULL
GO
update courseCriteria set ccr_type = 'COMPLETION'

begin tran
declare @cos_id int
declare @ccr_id nvarchar(20)

declare @now int

		select  @now  =  0
	declare
		cur2 CURSOR for
		select cos_res_id, cos_ccr_id
		from course where cos_ccr_id is not null

	open cur2
	FETCH cur2
	INTO @cos_id, @ccr_id

		/* if error */
 	IF ( @@FETCH_STATUS != 0 )
	BEGIN
		print "hihi"
		DEALLOCATE cur2
	END


	/* while successful */
	WHILE (@@FETCH_STATUS = 0 )
	BEGIN
		select  @now = @now +1
		print 'NOW is ' +  convert(varchar(20), @now) + " cos: " + convert(varchar(20), @cos_id)

			begin
				update courseCriteria set ccr_cos_id = @cos_id where ccr_id = @ccr_id
			end
		FETCH	cur2
	INTO @cos_id, @ccr_id

	END /* while */


commit
/*
rollback
*/

alter table course DROP CONSTRAINT FK_Course_CourseCriteria

ALTER TABLE [dbo].[courseCriteria] ADD
	CONSTRAINT [FK_CourseCriteria_Course] FOREIGN KEY
	(
		[ccr_cos_id]
	) REFERENCES [dbo].[Course] (
		[cos_res_id]
	)
GO

alter table course drop column [cos_ccr_id]

insert into xslTemplate
values
('COURSE_LATE_ATTEND_REMINDER', 'HTML_MAIL', 'SMTP', null, 'course_late_attend_reminder.xsl', 'http://202.66.159.182:83/servlet/Dispatcher?module=course.CourseCriteriaModule&')

declare @xtp_id int
	declare
		cur1 CURSOR for
		select max(xtp_id) from xslTemplate

	open cur1
	FETCH cur1
	INTO @xtp_id

	insert into xslparamname values (@xtp_id, 1, 'cmd')
	insert into xslparamname values (@xtp_id, 2, 'ent_id')
	insert into xslparamname values (@xtp_id, 3, 'title')
	insert into xslparamname values (@xtp_id, 4, 'appn_date')
	insert into xslparamname values (@xtp_id, 5, 'last_date')
	insert into xslparamname values (@xtp_id, 6, 'sender_id')

alter table mgitmselectedMessage
alter column [ism_type] [varchar] (100)

/*
Author: Chris
DESC : Allow guest login for the organization
DATE : 2002-03-15
*/

ALTER TABLE dbo.acSite ADD ste_guest_ent_id int NULL

/*
Author: Dennis Yip
DESC : Add column for sub-system Item Accreditation
DATE : 2002-03-16
*/
ALTER TABLE acSite ADD ste_iad_ind bit NULL
GO
Update acSite set ste_iad_ind = 0
ALTER TABLE dbo.acSite alter column ste_iad_ind bit NOT NULL


/*
Author : Wai Lun
Desc : Table for Item Accreditation
Date : 2002-03-19
*/
CREATE TABLE [dbo].[aeItemCredit] (
	[ict_id] [int] IDENTITY (1, 1) NOT NULL ,
	[ict_type] [varchar] (50) NOT NULL ,
	[ict_subtype] [varchar] (50) NULL ,
	[ict_code] [varchar] (50) NOT NULL ,
	[ict_owner_ent_id] [int] NOT NULL
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[aeItemCredit] WITH NOCHECK ADD
	CONSTRAINT [PK_aeItemCredit] PRIMARY KEY  NONCLUSTERED
	( [ict_id] )  ON [PRIMARY]
GO


CREATE TABLE [dbo].[aeItemCreditValue] (
	[icv_id] [int] IDENTITY (1, 1) NOT NULL ,
	[icv_ict_id] [int] NOT NULL ,
	[icv_code] [varchar] (50) NOT NULL ,
	[icv_value] [int] NOT NULL ,
	[icv_eff_start_datetime] [datetime] NULL ,
	[icv_eff_end_datetime] [datetime] NULL ,
	[icv_upd_usr_id] [varchar] (20) NOT NULL ,
	[icv_upd_timestamp] [datetime] NOT NULL ,
	[icv_create_usr_id] [varchar] (20) NOT NULL ,
	[icv_create_timestamp] [datetime] NOT NULL
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[aeItemCreditValue] WITH NOCHECK ADD
	CONSTRAINT [PK_aeItemCreditValue] PRIMARY KEY  NONCLUSTERED
	([icv_id])  ON [PRIMARY]
GO

ALTER TABLE [dbo].[aeItemCreditValue] ADD
	CONSTRAINT [FK_aeItemCreditValue_aeItemCredit] FOREIGN KEY
	([icv_ict_id]) REFERENCES [dbo].[aeItemCredit] ([ict_id])
GO


CREATE TABLE [dbo].[aeItemAccreditation] (
	[iad_itm_id] [int] NOT NULL ,
	[iad_icv_id] [int] NOT NULL
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[aeItemAccreditation] WITH NOCHECK ADD
	CONSTRAINT [PK_aeItemAccreditation] PRIMARY KEY  NONCLUSTERED
	([iad_itm_id],	[iad_icv_id])  ON [PRIMARY]
GO

ALTER TABLE [dbo].[aeItemAccreditation] ADD
	CONSTRAINT [FK_aeItemAccreditation_aeItem] FOREIGN KEY
	([iad_itm_id]) REFERENCES [dbo].[aeItem] ([itm_id]),
	CONSTRAINT [FK_aeItemAccreditation_aeItemCreditValue1] FOREIGN KEY
	([iad_icv_id]) REFERENCES [dbo].[aeItemCreditValue] ([icv_id])
GO

/*
Author : Dennis Yip
Desc : Add new column in aeItemType to indicate which columns needs to be updated to run cascadely when parent is updated
              Example: itm_capacity~itm_min_capacity~itm_unit
Date : 2002-03-19
*/
alter table aeItemType add ity_cascade_inherit_col varchar(1024)
GO

/*
Author: Dennis Yip
Desc: New System Message for FM (If you need multi-lingual, need to import unicdoe characters by Enterprise Manager)
Date: 2002-03-20
*/
insert into systemMessage values ('FMT008', 'ISO-8859-1', 'A facility named $data already exists.')
insert into systemMessage values ('FMT008', 'Big5', 'A facility named $data already exists.')
insert into systemMessage values ('FMT008', 'GB2312', 'A facility named $data already exists.')

/*
Author: Dennis Yip
Desc: Change the Item Accreditation indicator from acSite to aeItemType
Date: 2002-03-20
*/
alter table acSite drop column ste_iad_ind
GO
alter table aeItemType add ity_iad_ind bit
GO
update aeItemType set ity_iad_ind = 0
GO
alter table aeItemType alter column ity_iad_ind bit not null
GO

/*
Author: Kim
Desc: Change the data type of reguser extra_1, extra_2, extra_3, extra_4
Date: 2002-03-20
*/
alter table reguser
alter column [usr_extra_1] [nvarchar] (255)
alter table reguser
alter column [usr_extra_2] [nvarchar] (255)
alter table reguser
alter column [usr_extra_3] [nvarchar] (255)
alter table reguser
alter column [usr_extra_4] [nvarchar] (255)

/**
Author: Dennis Yip
Desc: New acFunction for group enrollemnt
Date: 2002-03-21
*/
insert into acFunction
(ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values
('QM_USG_ADMIN', 'FUNCTION', 'QUEUE MANAGER', getdate(), null)

/**
Author: Dennis Yip
Desc: New Template View column for item accreditation
Date: 2002-03-21
*/
alter table aeTemplateView add tvw_iad_ind bit
GO
update aeTemplateView set tvw_iad_ind = 0
GO
alter table aeTemplateView alter column tvw_iad_ind bit not null
GO

/**
Author: Chris Lo
Desc: Set the status of a usergroup
Date: 2002-03-21
*/
alter table usergroup add usg_status varchar(10) null


/**
Author: Chris Lo
Desc: Add an index to GroupMember
Date: 2002-03-23
*/
CREATE NONCLUSTERED INDEX IX_GroupMember ON dbo.GroupMember
	(
	gpm_ent_id_member,
	gpm_type
	) ON [PRIMARY]
GO

/*
Author: Dennis Yip
Desc: Add new acFunction for item search, getting all item in user's tray and item search
Date: 2002-03-24
*/
insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('ITM_SEARCH_RESPON_ALL', 'FUNCTION', 'ITEM', GETDATE(), NULL)



/*Author: Wai Lun
Desc: Change the icv_value datatype from int to decimal
Date: 2002-03-28
*/
alter table aeItemCreditValue alter column icv_value decimal(18,2) NOT NULL

/**
Author: Chris Lo
Desc: Add more function  and systemmessage
Date: 2002-03-28
*/
insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values
('RPT_APPROVED_LINK', 'FUNCTION', 'HOMEPAGE', getdate(),
'<function id="RPT_APPROVED_LINK"><desc lan="ISO-8859-1" name="Group Enrollment Status" /> <desc lan="Big5" name="Group Enrollment Status" /> <desc lan="GB2312" name="Group Enrollment Status" /> </function>')

insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values
('RPT_OWN_CREDIT_LINK', 'FUNCTION', 'HOMEPAGE', getdate(),
'<function id="RPT_OWN_CREDIT_LINK"><desc lan="ISO-8859-1" name="My Accreditation Report" /> <desc lan="Big5" name="My Accreditation Report" /> <desc lan="GB2312" name="My Accreditation Report" /> </function>')

insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values
('RPT_APPROVED_CREDIT_LINK', 'FUNCTION', 'HOMEPAGE', getdate(),
'<function id="RPT_APPROVED_CREDIT_LINK"><desc lan="ISO-8859-1" name="Group Accreditation Report" /> <desc lan="Big5" name="Group Accreditation Report" /> <desc lan="GB2312" name="Group Accreditation Report" /> </function>')


insert into systemmessage values ('USR012', 'Big5', 'Your account is successfully registered.');
insert into systemmessage values ('USR012', 'ISO-8859-1', 'Your account is successfully registered.');
insert into systemmessage values ('USR012', 'GB2312', 'Your account is successfully registered.');




/**
Author: Wai Lun
Desc: Add column used by WizCase
Date: 2002-04-03
*/
ALTER TABLE ModuleEvaluation ADD mov_data_xml ntext null
ALTER TABLE Accomplishment ADD apm_data_xml ntext null

/**
Author: Dennis Yip
Desc: New homepage function for competency
Date: 2002-04-08
*/
insert into acFunction
(ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values
('CM_ASS_MAIN', 'FUNCTION', 'HOMEPAGE', GETDATE(), '<function id="CM_ASS_MAIN"><desc lan="ISO-8859-1" name="Competency Assessment"/><desc lan="Big5" name="Competency Assessment"/><desc lan="GB2312" name="Competency Assessment"/></function>')
insert into acFunction
(ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values
('CM_SKL_ANALYSIS', 'FUNCTION', 'HOMEPAGE', GETDATE(), '<function id="CM_SKL_ANALYSIS"><desc lan="ISO-8859-1" name="Skill Inventory"/><desc lan="Big5" name="Skill Inventory"/><desc lan="GB2312" name="Skill Inventory"/></function>')

/**
Author: Dennis Yip
Desc: New SystemMessage for Approver to read staff's Learning Plan
*/
insert into systemmessage
values ('AELS03', 'ISO-8859-1', 'You do not have privillege to read this user''s Learning Plan')
insert into systemmessage
values ('AELS03', 'Big5', 'You do not have privillege to read this user''s Learning Plan')
insert into systemmessage
values ('AELS03', 'GB2312', 'You do not have privillege to read this user''s Learning Plan')

/*
Author : Kim
Desc : New colum for target participant cnt for MOTE
Date : 2002-04-09

*/
alter table aeitemmotedefault add imd_participant_target int null
alter table aeitemmote add imt_participant_target int null

/*
Author : Kim
Desc : Change wording in rating question
Date : 2002-04-09
*/

update aeitemratingDefination set ird_q_xml = '<question>
 <header status="ON">
  <title>  Please enter the rating for the course</title>
  </header>
 <body>
 <interaction order="1" type="MC" shuffle="N" logic="SINGLE">
 <option id="1">1=Excellent</option>
  <option id="2">2</option>
 <option id="3">3</option>
  <option id="4">4</option>
 <option id="5">5=Poor</option>
  </interaction>
  </body>
 <outcome order="1" type="MC" score="2" logic="SINGLE">
  <feedback condition="1" score="1" />
  <feedback condition="2" score="2" />
  <feedback condition="3" score="3" />
  <feedback condition="4" score="4" />
  <feedback condition="5" score="5" />
  </outcome>
  </question>'

/*
Author : Stanley
Desc : New Access Control on which role can see detail view
Date : 2002-05-29
*/
insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp)
values ('ITM_DETAIL_VIEW', 'FUNCTION', 'ITEM', getdate())

/*
Author : Stanley
Desc : Add a field to store the last post date of a forum topic
Date : 2002-06-03
*/
ALTER TABLE ForumTopic ADD fto_last_post_datetime Datetime
GO

DECLARE fto_cursor CURSOR FOR
select fto_id from ForumTopic

OPEN fto_cursor
declare @fto_id int

FETCH NEXT FROM fto_cursor INTO @fto_id

WHILE @@FETCH_STATUS = 0
BEGIN
	declare @fmg_create_datetime Datetime
	select @fmg_create_datetime = max(fmg_create_datetime) from ForumMessage where fmg_fto_id = @fto_id group by fmg_fto_id
--	print @fto_id
--	print @fmg_create_datetime
	update ForumTopic set fto_last_post_datetime = @fmg_create_datetime where fto_id = @fto_id
	set @fmg_create_datetime = NULL
	FETCH NEXT FROM fto_cursor INTO @fto_id
END
CLOSE fto_cursor
DEALLOCATE fto_cursor

/*
Author : Stanley
Desc : Add a system message for new version
Date : 2002-06-12
*/
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('AEIT14', 'ISO-8859-1', 'Course cannot be updated due to at least one of the following reasons:<p>
&nbsp;&nbsp;1. At least one run of the course is in progress<p>
&nbsp;&nbsp;2. Enrolment of the course/run is in progress<p>
&nbsp;&nbsp;3. Attendance record has not been updated after the course/run is completed<p>
&nbsp;&nbsp;4. Incomplete MOTE report<p>
Please check and ensure that all of the above issues are properly dealt with prior to discontinue the course.');
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('AEIT14', 'Big5', 'Course cannot be updated due to at least one of the following reasons:<p>
&nbsp;&nbsp;1. At least one run of the course is in progress<p>
&nbsp;&nbsp;2. Enrolment of the course/run is in progress<p>
&nbsp;&nbsp;3. Attendance record has not been updated after the course/run is completed<p>
&nbsp;&nbsp;4. Incomplete MOTE report<p>
Please check and ensure that all of the above issues are properly dealt with prior to discontinue the course.');
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('AEIT14', 'GB2312', 'Course cannot be updated due to at least one of the following reasons:<p>
&nbsp;&nbsp;1. At least one run of the course is in progress<p>
&nbsp;&nbsp;2. Enrolment of the course/run is in progress<p>
&nbsp;&nbsp;3. Attendance record has not been updated after the course/run is completed<p>
&nbsp;&nbsp;4. Incomplete MOTE report<p>
Please check and ensure that all of the above issues are properly dealt with prior to discontinue the course.');

/*
Author : Stanley
Desc : Add a system message for same title of course/run
Date : 2002-06-12
*/
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('AEIT15', 'ISO-8859-1', 'Title already exists.')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('AEIT15', 'Big5', 'Title already exists.')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('AEIT15', 'GB2312', 'Title already exists.')

/*
Author : Stanley
Desc : Add a system message.  (It happens When a user try to paste a user record which has been trashed)
Date : 2002-06-12
*/
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('USG009', 'ISO-8859-1', 'Cannot read from the source record.')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('USG009', 'Big5', 'Cannot read from the source record.')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('USG009', 'GB2312', 'Cannot read from the source record.')

/*
Author : Kim Chan
Desc : Add a AccessControl function for viewing non target item. (ususally for learner)
Date : 2002-06-13
*/
insert into acfunction values ('ITM_NON_TARGET_READ', 'FUNCTION', 'ITEM', GETdATE(), NULL)
insert into acrolefunction
select rol_id, ftn_id , getDate() from acFunction, acRole where ftn_ext_id = 'ITM_NON_TARGET_READ'

/*
Author : Stanley
Desc : Remove the 'EMPTY_STRING' from the table Resources
Date : 2002-06-24
*/
update Resources set res_desc = null where res_desc like 'EMPTY_STRING'

/*
Author : Stanley
Desc : Update module Template
Date : 2002-06-24
*/
update Template set tpl_desc = 'The default web browser of your computer, with bookmark button at the top' where
tpl_name in ('Bookmark Browser(Tutorial)', 'Bookmark Browser(Reading)', 'Bookmark Browser(Lecture)')

update Template set tpl_name = 'Self Test 1' where tpl_name = 'Self Test1'
update Template set tpl_name = 'Self Test 2' where tpl_name = 'Self Test2'
update Template set tpl_name = 'Self Test 3' where tpl_name = 'Self Test3'
update Resources set res_tpl_name = 'Self Test 1' where res_tpl_name = 'Self Test1'
update Resources set res_tpl_name = 'Self Test 2' where res_tpl_name = 'Self Test2'
update Resources set res_tpl_name = 'Self Test 3' where res_tpl_name = 'Self Test3'

update Template set tpl_name = 'wizPack Browser 1(Experiment)' where tpl_name = 'WizPack Browser1(Experiment)'
update Template set tpl_name = 'wizPack Browser 1(Lecture)' where tpl_name = 'WizPack Browser1(Lecture)'
update Template set tpl_name = 'wizPack Browser 1(Reading)' where tpl_name = 'WizPack Browser1(Reading)'
update Template set tpl_name = 'wizPack Browser 1(Tutorial)' where tpl_name = 'WizPack Browser1(Tutorial)'
update Template set tpl_name = 'wizPack Browser 2(Experiment)' where tpl_name = 'WizPack Browser2(Experiment)'
update Template set tpl_name = 'wizPack Browser 2(Lecture)' where tpl_name = 'WizPack Browser2(Lecture)'
update Template set tpl_name = 'wizPack Browser 2(Reading)' where tpl_name = 'WizPack Browser2(Reading)'
update Template set tpl_name = 'wizPack Browser 2(Tutorial)' where tpl_name = 'WizPack Browser2(Tutorial)'
update Resources set res_tpl_name = 'wizPack Browser 1(Experiment)' where res_tpl_name = 'WizPack Browser1(Experiment)'
update Resources set res_tpl_name = 'wizPack Browser 1(Lecture)' where res_tpl_name = 'WizPack Browser1(Lecture)'
update Resources set res_tpl_name = 'wizPack Browser 1(Reading)' where res_tpl_name = 'WizPack Browser1(Reading)'
update Resources set res_tpl_name = 'wizPack Browser 1(Tutorial)' where res_tpl_name = 'WizPack Browser1(Tutorial)'
update Resources set res_tpl_name = 'wizPack Browser 2(Experiment)' where res_tpl_name = 'WizPack Browser2(Experiment)'
update Resources set res_tpl_name = 'wizPack Browser 2(Lecture)' where res_tpl_name = 'WizPack Browser2(Lecture)'
update Resources set res_tpl_name = 'wizPack Browser 2(Reading)' where res_tpl_name = 'WizPack Browser2(Reading)'
update Resources set res_tpl_name = 'wizPack Browser 2(Tutorial)' where res_tpl_name = 'WizPack Browser2(Tutorial)'

/*
Author : Stanley
Desc : Update ProgressAttempt
Date : 2002-06-24
*/
alter table ProgressAttempt alter column atm_response_bil nvarchar(1000) NULL

/*
Author : Stanley
Desc : Update module
Date : 2002-06-24
IMPORTANT NOTE: Error may occur when you execute the following script since there is a known bug in MSSQL 7.0.  Please refer to http://support.microsoft.com/default.aspx?scid=kb;en-us;Q290992 for more information.
*/
alter table Module alter column mod_instruct nvarchar(3000) NULL


/*
Author : Stanley
Desc : Delete Bookmark Template
Date : 2002-06-24
*/
update Resources set res_tpl_name = 'Annotated Browser(Lecture)' where res_tpl_name = 'Bookmark Browser(Lecture)'
update Resources set res_tpl_name = 'Annotated Browser(Reading)' where res_tpl_name = 'Bookmark Browser(Reading)'
update Resources set res_tpl_name = 'Annotated Browser(Tutorial)' where res_tpl_name = 'Bookmark Browser(Tutorial)'

Delete Template where tpl_name like 'Bookmark Browser%'

/*
Author : Wai Lun
Desc : System message for the same catalog/category name
Date : 2002-06-25
*/
INSERT INTO SystemMessage Values('AETN07', 'Big5', '(Big5)Category of the same name exists. Please specify another name.')
INSERT INTO SystemMessage Values('AETN07', 'GB2312', '(GB2312)Category of the same name exists. Please specify another name')
INSERT INTO SystemMessage Values('AETN07', 'ISO-8859-1', 'Category of the same name exists. Please specify another name')

INSERT INTO SystemMessage Values('AECA03', 'Big5', '(Big5)Catalog of the same name exists. Please specify another name.')
INSERT INTO SystemMessage Values('AECA03', 'GB2312', '(GB2312)Catalog of the same name exists. Please specify another name')
INSERT INTO SystemMessage Values('AECA03', 'ISO-8859-1', 'Catalog of the same name exists. Please specify another name')

/*
Author : Dennis Yip
Desc : Add Primary key for table ctLicence, no need to run if the table already have this key
Date : 2002-06-28
*/
ALTER TABLE [dbo].[ctLicence] WITH NOCHECK ADD
	CONSTRAINT [PK_ctLicence] PRIMARY KEY  NONCLUSTERED
	(
		[lic_key],
		[lic_ent_id_root]
	) WITH  FILLFACTOR = 90  ON [PRIMARY]
GO

/*
Author : Dennis Yip
Desc : Update ALL ISO-8859-1 SystemMessage
Date : 2002-06-28
*/
Update SystemMessage Set sms_desc = 'Access control records are updated successfully.' Where sms_id = 'ACL001' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'You do not have enough permission.' Where sms_id = 'ACL002' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'You do not have the permission to access this catalog.' Where sms_id = 'AECA01' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The catalog is in hidden status.' Where sms_id = 'AECA02' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Catalog of the same name exists. Please specify another name.' Where sms_id = 'AECA03' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Unknown Learning Solution type.' Where sms_id = 'AEIT01' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The Learning Solution is in hidden status.' Where sms_id = 'AEIT02' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The Learning Solution has been enrolled.' Where sms_id = 'AEIT03' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'You have not enrolled in this Learning Solution.' Where sms_id = 'AEIT04' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The Learning Solution does not contain online content.' Where sms_id = 'AEIT05' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Learning Solution code already exists.' Where sms_id = 'AEIT06' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Learning Solution is added successfully.' Where sms_id = 'AEIT07' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Learning Solution has already been approved.' Where sms_id = 'AEIT08' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Cannot create new version for Run.' Where sms_id = 'AEIT09' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The Learning Solution has not completed.' Where sms_id = 'AEIT10' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The Learning Solution has already been cancelled.' Where sms_id = 'AEIT11' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Learning Solution is updated successfully.' Where sms_id = 'AEIT12' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'New version is added successfully.' Where sms_id = 'AEIT13' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Course cannot be updated due to at least one of the following reasons:<p>
&nbsp;&nbsp;1. At least one run of the course is in progress<p>
&nbsp;&nbsp;2. Enrolment of the course/run is in progress<p>
&nbsp;&nbsp;3. Attendance record has not been updated after the course/run is completed<p>
&nbsp;&nbsp;4. Incomplete MOTE report<p>
Please check and ensure that all of the above issues are properly dealt with prior to discontinue the course.' Where sms_id = 'AEIT14' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Title already exists.' Where sms_id = 'AEIT15' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The course is successfully added to the Learning Solution.' Where sms_id = 'AELS01' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The course is already added to the Learning Solution.' Where sms_id = 'AELS02' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'You do not have privillege to read this user''s Learning Plan' Where sms_id = 'AELS03' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Your enrolment has been received.' Where sms_id = 'AEQM01' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The record has just been updated by another user.' Where sms_id = 'AEQM02' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Enrolment date has not started yet.' Where sms_id = 'AEQM03' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Enrolment date has passed.' Where sms_id = 'AEQM04' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Cannot perform this action on the enrollment.' Where sms_id = 'AEQM05' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'No enrollment is selected.' Where sms_id = 'AEQM06' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The Learning Solution is not ready for enrollment.' Where sms_id = 'AEQM07' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Your enrolment is successfully submitted. You will be notified for any progress.' Where sms_id = 'AEQM08' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Cannot remove it since there are categories attached to it.' Where sms_id = 'AETN01' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Cannot move the category into its sub-category.' Where sms_id = 'AETN02' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The category is in hidden status.' Where sms_id = 'AETN03' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The target category has been removed.' Where sms_id = 'AETN04' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The Learning Solution is removed from the category and put into the Recycle Bin.' Where sms_id = 'AETN05' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The Learning Solution is removed from the category. However, it is not put into the Recycle Bin since it is associated with other categories.' Where sms_id = 'AETN06' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Category of the same name exists. Please specify another name.' Where sms_id = 'AETN07' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Unexpected errors occured when loading the AICC course structure files. Please check the format of the files and try again.' Where sms_id = 'AICC01' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The AICC course has been imported successfully.' Where sms_id = 'AICC02' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Assessment Not Ready' Where sms_id = 'ASM001' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'There are not enough questions in the assessment.' Where sms_id = 'ASM002' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'There are not enough questions in the assessment.' Where sms_id = 'ASM003' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The chatroom session has started. You cannot remove it nor update the starting time.' Where sms_id = 'CHT001' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The virtual classroom session has started. You cannot remove it nor update the starting time.' Where sms_id = 'CHT002' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The record is trashed from the Recycle Bin.' Where sms_id = 'CMP001' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The skill group is not empty.' Where sms_id = 'CMP002' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The skill is in use.' Where sms_id = 'CMP003' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The scale is in use.' Where sms_id = 'CMP004' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The survey is selected by an assessment.' Where sms_id = 'CMP005' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The survey cannot be updated because rators have started to do the assessment.' Where sms_id = 'CMP006' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Your survey has been submitted.' Where sms_id = 'CMP007' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Your survey is successfully submitted.' Where sms_id = 'CMP008' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Your survey is saved successfully.' Where sms_id = 'CMP009' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The assessment is removed.' Where sms_id = 'CMP012' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The skill is removed.' Where sms_id = 'CMP013' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Some modules have been attempted by learners.' Where sms_id = 'COS001' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The learner/group has already been enrolled.' Where sms_id = 'ENR001' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'There is not enough license.' Where sms_id = 'ENR002' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = '$data is removed successfully.' Where sms_id = 'ENT001' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = '$data is updated successfully.' Where sms_id = 'ENT002' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = '$data is added successfully.' Where sms_id = 'ENT003' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The record is successfully pasted.' Where sms_id = 'ENT004' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Failed to update the record. The record has just been updated by another user.' Where sms_id = 'FMT001' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Failed to remove the record. The record has just been updated by another user.' Where sms_id = 'FMT002' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Failed to get the configuration parameters for Facility Management.' Where sms_id = 'FMT003' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Specified reservation cannot be found.' Where sms_id = 'FMT004' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Specified facility schedule cannot be found.' Where sms_id = 'FMT005' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Specified facility type cannot be found.' Where sms_id = 'FMT006' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Specified facility cannot be found.' Where sms_id = 'FMT007' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'A facility named $data already exists.' Where sms_id = 'FMT008' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Failed to process your request.' Where sms_id = 'GEN000' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The record is added successfully.' Where sms_id = 'GEN001' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The record is removed successfully.' Where sms_id = 'GEN002' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The record is updated successfully.' Where sms_id = 'GEN003' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The record is added successfully.' Where sms_id = 'GEN004' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The record does not exist. $data.' Where sms_id = 'GEN005' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The record has just been updated by another user.' Where sms_id = 'GEN006' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The Keyword is added successfully.' Where sms_id = 'GLO001' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The Keyword is updated successfully.' Where sms_id = 'GLO002' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The Keyword is removed successfully.' Where sms_id = 'GLO003' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The Industry Code cannot be removed since there are Industry Codes/users attaching to it.' Where sms_id = 'IDC001' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The license of your organization is invalid.' Where sms_id = 'LIC001' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Insufficient license for additional user registration.' Where sms_id = 'LIC002' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The license of your organization has expired.' Where sms_id = 'LIC003' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The questions are not in correct order.' Where sms_id = 'MOD001' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Failed to add the following questions into the module. <br>Question IDs : $data <br>Reason : Questions already exist.' Where sms_id = 'MOD002' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Failed to remove the following questions from the module. <br>Question IDs: $data <br>Reason: Questions were attempted.' Where sms_id = 'MOD003' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The module has been attempted.' Where sms_id = 'MOD004' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'You do not have the permission to modify the module.' Where sms_id = 'MOD005' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'MOTE report is added successfully.' Where sms_id = 'MOT001' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'MOTE report is updated successfully.' Where sms_id = 'MOT002' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'MOTE report is removed successfully.' Where sms_id = 'MOT003' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The Learning Solution must have at least one MOTE report.' Where sms_id = 'MOT004' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The Learning Solution does not have a MOTE report.' Where sms_id = 'MOT005' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The record has just been updated by another user.' Where sms_id = 'MSG001' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'You do not have the permission to remove the record.' Where sms_id = 'MSG002' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'You do not have the permission to add the record.' Where sms_id = 'MSG003' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'You do not have the permission to update the record.' Where sms_id = 'MSG004' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The message is added successfully.' Where sms_id = 'MSG005' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The message is updated successfully.' Where sms_id = 'MSG006' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The message is removed successfully.' Where sms_id = 'MSG007' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'There are not enough questions to fulfill the criteria.' Where sms_id = 'MSP001' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'You do not have the permission to do the modification.' Where sms_id = 'OBJ001' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Cannot remove the category  since there are categories/Knowledge Objects attaching to it.' Where sms_id = 'OBJ002' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Cannot remove the category since there are assessments referencing it.' Where sms_id = 'OBJ003' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Cannot trash the category since it is not in Recycle Bin.' Where sms_id = 'OBJ004' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Knowledge Object has been put into Recycle Bin.' Where sms_id = 'OBJ005' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Cannot remove the Knowledge Object. <br>It is not under the category: $data.' Where sms_id = 'OBJ006' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Knowledge Object is successfully trashed.' Where sms_id = 'OBJ007' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Knowledge Object is successfully pasted into the personal folder.' Where sms_id = 'OBJ008' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Knowledge Object is successfully pasted into the public folder.' Where sms_id = 'OBJ009' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Cannot paste the category into its sub-category.' Where sms_id = 'OBJ010' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Your assignment has been submitted/graded.' Where sms_id = 'PGR001' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The module has been attempted.' Where sms_id = 'PGR002' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The module has not been attempted yet.' Where sms_id = 'PGR003' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The number of respondents exceeds the limit.' Where sms_id = 'PGR004' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The number of attempts exceeds the limit.' Where sms_id = 'PGR005' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The assignment is successfully graded.' Where sms_id = 'PGR006' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The selected users/user groups have not attempted yet.' Where sms_id = 'PGR007' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'You do not have the permission to view the record.' Where sms_id = 'PRM001' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The question has been used by other authors.' Where sms_id = 'QUE001' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The question has been attempted.' Where sms_id = 'QUE002' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The reference is added successfully.' Where sms_id = 'REF001' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Failed to add the reference.' Where sms_id = 'REF002' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The reference is successfully updated.' Where sms_id = 'REF003' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Failed to update the reference.' Where sms_id = 'REF004' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The reference is successfully removed.' Where sms_id = 'REF005' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Failed to remove the reference.' Where sms_id = 'REF006' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The record has just been updated by another user.' Where sms_id = 'RES001' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'You do not have the permission to do the modification.' Where sms_id = 'RES002' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'You do not have the permission to add Knowledge Object in public folder.' Where sms_id = 'RES003' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The selected Knowledge Objects have no associated files to download.' Where sms_id = 'RES004' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'You do not have the permission to view the record.' Where sms_id = 'RPM001' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'You do not have the permission to do the modification.' Where sms_id = 'RPM002' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'You do not have the permission to attempt the module.' Where sms_id = 'RPM003' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'You do not have the permission to download the record.' Where sms_id = 'RPM004' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'You do not have the permission to add Knowledge Object.' Where sms_id = 'RPM005' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'You do not have the permission to do the modification.' Where sms_id = 'SYB001' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Cannot remove the catalog since there are categories under it.' Where sms_id = 'SYB002' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Failed to upload the file.' Where sms_id = 'ULG001' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The format of header row is not correct.' Where sms_id = 'ULG002' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'No upload records were found.' Where sms_id = 'ULG003' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The objective is not correct.' Where sms_id = 'ULG004' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The file is successfully uploaded.' Where sms_id = 'ULG005' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = '$data questions are successfully added.' Where sms_id = 'ULG006' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The upload system is used by other user concurrently.' Where sms_id = 'ULG007' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = '$data user records are successfully added.' Where sms_id = 'ULG008' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'The record has just been updated by other user.' Where sms_id = 'USG001' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'You do not have the permission to modify the user group.' Where sms_id = 'USG002' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Cannot move user group into its sub-group.' Where sms_id = 'USG003' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Cannot remove the group since there are groups/users attaching to it.' Where sms_id = 'USG004' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'User group is successfully added.' Where sms_id = 'USG005' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'User group is successfully trashed.' Where sms_id = 'USG006' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'User information is successfully updated.' Where sms_id = 'USG007' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = '$data exists. Please specify another group name / group code.' Where sms_id = 'USG008' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Cannot read from the source record.' Where sms_id = 'USG009' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = '$data exists. Please specify another user ID.' Where sms_id = 'USR001' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Please login and try again.' Where sms_id = 'USR002' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Only expert administrator can add user with administrator role.' Where sms_id = 'USR003' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'You do not have the permission to modify the user record.' Where sms_id = 'USR004' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Cannot trash the user since he/she is not in Recycle Bin.' Where sms_id = 'USR005' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'User is successfully added.' Where sms_id = 'USR006' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'User is successfully removed from user group.' Where sms_id = 'USR007' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'User is successfully trashed.' Where sms_id = 'USR008' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'User information is successfully updated.' Where sms_id = 'USR009' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Your account is being used by another login session. Please contact the system administrator if it is not you who are logging in twice.' Where sms_id = 'USR010' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'User does not belong to this role. $data.' Where sms_id = 'USR011' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Your account is successfully registered.' Where sms_id = 'USR012' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Message is processed successfully.' Where sms_id = 'XMG001' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Failed to process the message.' Where sms_id = 'XMG002' and sms_lan = 'ISO-8859-1'
Update SystemMessage Set sms_desc = 'Message is updated successfully.' Where sms_id = 'XMG003' and sms_lan = 'ISO-8859-1'

/*
Author : Stanley
Desc : Update the size of aeTreeNode Description
Date : 2002-06-28
*/
alter table aeTreeNode alter column tnd_desc nvarchar(1000) NULL

/*
Author : Dennis Yip
Desc : Insert CodeType and CodeTable data for FM. Need not run this script if already add these data before
Date : 2002-07-03
*/
insert into CodeType
(ctp_id, ctp_sys_ind, ctp_title, ctp_create_timestamp, ctp_create_usr_id)
values
('fmCancelType', 1, 'Cancellation Type', '2002-01-29', 's1u3')

insert into CodeTable (ctb_type, ctb_id, ctb_title, ctb_xml, ctb_create_timestamp, ctb_create_usr_id, ctb_upd_timestamp, ctb_upd_usr_id)
values
('fmCancelType', '001', 'Unavailability of instructors', NULL, '2002-01-29', 's1u3', '2002-01-29', 's1u3')

insert into CodeTable (ctb_type, ctb_id, ctb_title, ctb_xml, ctb_create_timestamp, ctb_create_usr_id, ctb_upd_timestamp, ctb_upd_usr_id)
values
('fmCancelType', '002', 'Clash with participants', NULL, '2002-01-29', 's1u3', '2002-01-29', 's1u3')

insert into CodeTable (ctb_type, ctb_id, ctb_title, ctb_xml, ctb_create_timestamp, ctb_create_usr_id, ctb_upd_timestamp, ctb_upd_usr_id)
values
('fmCancelType', '003', 'Insufficient number of participants', NULL, '2002-01-29', 's1u3', '2002-01-29', 's1u3')

insert into CodeTable (ctb_type, ctb_id, ctb_title, ctb_xml, ctb_create_timestamp, ctb_create_usr_id, ctb_upd_timestamp, ctb_upd_usr_id)
values
('fmCancelType', '004', 'Weather', NULL, '2002-01-29', 's1u3', '2002-01-29', 's1u3')

insert into CodeTable (ctb_type, ctb_id, ctb_title, ctb_xml, ctb_create_timestamp, ctb_create_usr_id, ctb_upd_timestamp, ctb_upd_usr_id)
values
('fmCancelType', '005', 'Change in company policy', NULL, '2002-01-29', 's1u3', '2002-01-29', 's1u3')

insert into CodeTable (ctb_type, ctb_id, ctb_title, ctb_xml, ctb_create_timestamp, ctb_create_usr_id, ctb_upd_timestamp, ctb_upd_usr_id)
values
('fmCancelType', '006', 'Booking by mistakes', NULL, '2002-01-29', 's1u3', '2002-01-29', 's1u3')

insert into CodeTable (ctb_type, ctb_id, ctb_title, ctb_xml, ctb_create_timestamp, ctb_create_usr_id, ctb_upd_timestamp, ctb_upd_usr_id)
values
('fmCancelType', '007', 'Others', NULL, '2002-01-29', 's1u3', '2002-01-29', 's1u3')


/*
Author : Wai Lun
Desc : new column xtp_mail_merge_ind for xslTemplate
Date : 2002-07-09
*/
alter table xslTemplate add xtp_mailmerge_ind int
GO
update xslTemplate Set xtp_mailmerge_ind = 1
alter table xslTemplate alter column xtp_mailmerge_ind int not null
GO
update xslTemplate Set xtp_mailmerge_ind = 0 where xtp_id in ( 4, 17 )

/*
Author : Dennis Yip
Desc : Missing Index from aeItemTargetEntity, need not to run if this index already exists
Date : 2002-07-10
*/
CREATE  INDEX [IX_aeItemTargetEntity] ON
[dbo].[aeItemTargetEntity]([ite_ent_id]) ON [PRIMARY]

/*
Author : Clifford
Desc : Start of DB changes after merging with BJ's code
Date : 2002-07-22
*/

/*
Author : Clifford
Desc : for storing the max no. of online active item allowed to be created(encrypted)
Date : 2002-07-22
*/
ALTER TABLE dbo.acSite ADD
	ste_ctl_4 varchar(100) NULL
GO

/*
Author : Clifford
Desc : initialize this column with an encrypted string of pattern "ITEMnITEM" where n is the value
Date : 2002-07-22
*/
update acSite set ste_ctl_4 = '074090084105102077102085072079085069080310'

/*
Author : Clifford
Desc : for foreign key relation with Certificate
Date : 2002-07-22
*/
ALTER TABLE dbo.aeItem ADD
	itm_ctf_id int NULL
GO

/*
Author : Clifford
Desc : for indicating if this item type uses certificate function
Date : 2002-07-22
*/
ALTER TABLE dbo.aeItemType ADD
	ity_certificate_ind int NULL
GO

/*
Author : Clifford
Desc : for indicating the level of importance of a message
Date : 2002-07-22
*/
ALTER TABLE dbo.Message ADD
	msg_level varchar(50) NULL
GO

/*
Author : Clifford
Desc : for certificate function
Date : 2002-07-22
*/
CREATE TABLE [dbo].[cfCertificate] (
	[ctf_id] [int] IDENTITY (1, 1) NOT NULL ,
	[ctf_title] [nvarchar] (255) NOT NULL ,
	[ctf_status] [varchar] (50) NOT NULL ,
	[ctf_link] [varchar] (100) NOT NULL ,
	[ctf_owner_ent_id] [int] NOT NULL ,
	[ctf_create_timestamp] [datetime] NOT NULL ,
	[ctf_create_usr_id] [varchar] (20) NOT NULL ,
	[ctf_upd_timestamp] [datetime] NOT NULL ,
	[ctf_upd_usr_id] [varchar] (20) NOT NULL
)
GO
CREATE TABLE [dbo].[cfCertification] (
	[cfn_ctf_id] [int] NOT NULL ,
	[cfn_ent_id] [int] NOT NULL ,
	[cfn_qualification_ind] [int] NULL ,
	[cfn_status] [varchar] (50) NOT NULL ,
	[cfn_owner_ent_id] [int] NOT NULL ,
	[cfn_create_timestamp] [datetime] NOT NULL ,
	[cfn_create_usr_id] [varchar] (20) NOT NULL ,
	[cfn_upd_timestamp] [datetime] NOT NULL ,
	[cfn_upd_usr_id] [varchar] (20) NOT NULL
) ON [PRIMARY]
GO
ALTER TABLE [dbo].[cfCertificate] WITH NOCHECK ADD
	CONSTRAINT [PK_cfCertificate] PRIMARY KEY  CLUSTERED
	(
		[ctf_id]
	) WITH  FILLFACTOR = 90  ON [PRIMARY]
GO
ALTER TABLE [dbo].[cfCertification] ADD
	CONSTRAINT [FK_cfCertification_cfCertificate] FOREIGN KEY
	(
		[cfn_ctf_id]
	) REFERENCES [dbo].[cfCertificate] (
		[ctf_id]
	),
	CONSTRAINT [FK_cfCertification_Entity] FOREIGN KEY
	(
		[cfn_ent_id]
	) REFERENCES [dbo].[Entity] (
		[ent_id]
	)
GO

/*
Author : Clifford
Desc : for oracle database migration, need a primary key for transferring unicode data
Date : 2002-07-22
*/
ALTER TABLE [dbo].[CodeTable] WITH NOCHECK ADD
	CONSTRAINT [PK_CodeTable] PRIMARY KEY  NONCLUSTERED
	(
		[ctb_type],
		[ctb_id]
	) WITH  FILLFACTOR = 90  ON [PRIMARY]
GO

/*
Author : Clifford
Desc : update package name of the URL in the field "xtp_xml_url" of the table "xslTemplate"
Date : 2002-07-22
*/
UPDATE xslTemplate SET xtp_xml_url = REPLACE(xtp_xml_url, 'cw.ae.aeAction', 'aeAction') where xtp_xml_url like '%cw.ae.aeAction%'

/*
Author : Clifford
Desc : update package name of the URL in the field "ste_rsv_link" of the table "acSite"
Date : 2002-07-23
*/
UPDATE acSite SET ste_rsv_link = REPLACE(ste_rsv_link, 'cw.ae.', 'com.cw.wizbank.ae.') where ste_rsv_link like '%cw.ae.%'

/*
Author : Clifford
Desc : End of DB changes after merging with BJ's code
Date : 2002-07-22
*/


/*
Author : Chris
Desc : Delete the Single Player X template which are the same as Test Player X
Date : 2002-07-26
*/

update resources set res_tpl_name='Test Player 1' where res_tpl_name='Single Player 1'
update resources set res_tpl_name='Test Player 2' where res_tpl_name='Single Player 2'
update resources set res_tpl_name='Test Player 3' where res_tpl_name='Single Player 3'
update resources set res_tpl_name='Test Player 4' where res_tpl_name='Single Player 4'
update resources set res_tpl_name='Test Player 5' where res_tpl_name='Single Player 5'

delete from Template where tpl_type='TST' and tpl_name like 'Single Player%'

/*
Author : Clifford
Desc : Insert DisplayOption records for NETg and SCORM courses
Date : 2002-07-26
*/
insert into DisplayOption (dpo_res_id,dpo_res_type,dpo_res_subtype,dpo_view,dpo_icon_ind,dpo_title_ind,dpo_lan_ind,dpo_desc_ind,dpo_instruct_ind,dpo_eff_start_datetime_ind,dpo_eff_end_datetime_ind,dpo_difficulty_ind,dpo_time_limit_ind,dpo_suggested_time_ind,dpo_duration_ind,dpo_max_score_ind,dpo_pass_score_ind,dpo_pgr_last_acc_datetime_ind,dpo_pgr_start_datetime_ind,dpo_pgr_complete_datetime_ind,dpo_pgr_attempt_nbr_ind,dpo_instructor_ind,dpo_organization_ind,dpo_moderator_ind,dpo_evt_datetime_ind,dpo_evt_venue_ind,dpo_status_ind)
values (0,'MOD','NETG_COK','LRN_READ',1,1,1,1,0,0,0,1,0,0,1,0,1,0,0,0,0,0,0,0,0,0,0)
insert into DisplayOption (dpo_res_id,dpo_res_type,dpo_res_subtype,dpo_view,dpo_icon_ind,dpo_title_ind,dpo_lan_ind,dpo_desc_ind,dpo_instruct_ind,dpo_eff_start_datetime_ind,dpo_eff_end_datetime_ind,dpo_difficulty_ind,dpo_time_limit_ind,dpo_suggested_time_ind,dpo_duration_ind,dpo_max_score_ind,dpo_pass_score_ind,dpo_pgr_last_acc_datetime_ind,dpo_pgr_start_datetime_ind,dpo_pgr_complete_datetime_ind,dpo_pgr_attempt_nbr_ind,dpo_instructor_ind,dpo_organization_ind,dpo_moderator_ind,dpo_evt_datetime_ind,dpo_evt_venue_ind,dpo_status_ind)
values (0,'MOD','NETG_COK','IST_READ',1,1,1,1,0,1,1,1,0,0,1,0,1,0,0,0,0,0,0,0,0,0,1)
insert into DisplayOption (dpo_res_id,dpo_res_type,dpo_res_subtype,dpo_view,dpo_icon_ind,dpo_title_ind,dpo_lan_ind,dpo_desc_ind,dpo_instruct_ind,dpo_eff_start_datetime_ind,dpo_eff_end_datetime_ind,dpo_difficulty_ind,dpo_time_limit_ind,dpo_suggested_time_ind,dpo_duration_ind,dpo_max_score_ind,dpo_pass_score_ind,dpo_pgr_last_acc_datetime_ind,dpo_pgr_start_datetime_ind,dpo_pgr_complete_datetime_ind,dpo_pgr_attempt_nbr_ind,dpo_instructor_ind,dpo_organization_ind,dpo_moderator_ind,dpo_evt_datetime_ind,dpo_evt_venue_ind,dpo_status_ind)
values (0,'MOD','NETG_COK','IST_EDIT',1,1,1,1,0,1,1,1,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1)
insert into DisplayOption (dpo_res_id,dpo_res_type,dpo_res_subtype,dpo_view,dpo_icon_ind,dpo_title_ind,dpo_lan_ind,dpo_desc_ind,dpo_instruct_ind,dpo_eff_start_datetime_ind,dpo_eff_end_datetime_ind,dpo_difficulty_ind,dpo_time_limit_ind,dpo_suggested_time_ind,dpo_duration_ind,dpo_max_score_ind,dpo_pass_score_ind,dpo_pgr_last_acc_datetime_ind,dpo_pgr_start_datetime_ind,dpo_pgr_complete_datetime_ind,dpo_pgr_attempt_nbr_ind,dpo_instructor_ind,dpo_organization_ind,dpo_moderator_ind,dpo_evt_datetime_ind,dpo_evt_venue_ind,dpo_status_ind)
values (0,'MOD','SCO','LRN_READ',1,1,1,1,0,0,0,1,0,0,1,0,1,0,0,0,0,0,0,0,0,0,0)
insert into DisplayOption (dpo_res_id,dpo_res_type,dpo_res_subtype,dpo_view,dpo_icon_ind,dpo_title_ind,dpo_lan_ind,dpo_desc_ind,dpo_instruct_ind,dpo_eff_start_datetime_ind,dpo_eff_end_datetime_ind,dpo_difficulty_ind,dpo_time_limit_ind,dpo_suggested_time_ind,dpo_duration_ind,dpo_max_score_ind,dpo_pass_score_ind,dpo_pgr_last_acc_datetime_ind,dpo_pgr_start_datetime_ind,dpo_pgr_complete_datetime_ind,dpo_pgr_attempt_nbr_ind,dpo_instructor_ind,dpo_organization_ind,dpo_moderator_ind,dpo_evt_datetime_ind,dpo_evt_venue_ind,dpo_status_ind)
values (0,'MOD','SCO','IST_READ',1,1,1,1,0,1,1,1,0,0,1,0,1,0,0,0,0,0,0,0,0,0,1)
insert into DisplayOption (dpo_res_id,dpo_res_type,dpo_res_subtype,dpo_view,dpo_icon_ind,dpo_title_ind,dpo_lan_ind,dpo_desc_ind,dpo_instruct_ind,dpo_eff_start_datetime_ind,dpo_eff_end_datetime_ind,dpo_difficulty_ind,dpo_time_limit_ind,dpo_suggested_time_ind,dpo_duration_ind,dpo_max_score_ind,dpo_pass_score_ind,dpo_pgr_last_acc_datetime_ind,dpo_pgr_start_datetime_ind,dpo_pgr_complete_datetime_ind,dpo_pgr_attempt_nbr_ind,dpo_instructor_ind,dpo_organization_ind,dpo_moderator_ind,dpo_evt_datetime_ind,dpo_evt_venue_ind,dpo_status_ind)
values (0,'MOD','SCO','IST_EDIT',1,1,1,1,0,1,1,1,1,0,0,0,1,0,0,0,0,0,0,0,0,0,1)

/*
Author : Clifford
Desc : Insert a Access Control Function for "Saving Progress Tracking"
Date : 2002-07-26
*/
insert into acFunction (ftn_ext_id,ftn_level,ftn_type,ftn_creation_timestamp)
values ('SAVE_PROGRESS_TRACKING','FUNCTION','MODULE',getDate())

insert into acRoleFunction (rfn_rol_id,rfn_ftn_id,rfn_creation_timestamp)
select rol_id, ftn_id , getDate() from acFunction, acRole where ftn_ext_id = 'SAVE_PROGRESS_TRACKING' and rol_ext_id like '%NLRN%'

/*
Author : Clifford
Desc : Insert System Message records for NETg and SCORM courses
Date : 2002-07-26
*/
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('SCO01', 'ISO-8859-1', 'The SCORM course has been imported successfully.')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('SCO01', 'Big5', 'The SCORM course has been imported successfully.')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('SCO01', 'GB2312', 'The SCORM course has been imported successfully.')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('SCO02', 'ISO-8859-1', 'Unexpected errors occured when loading the Manifest file.  Please check the format of the file and try again.')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('SCO02', 'Big5', 'Unexpected errors occured when loading the Manifest file.  Please check the format of the file and try again.')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('SCO02', 'GB2312', 'Unexpected errors occured when loading the Manifest file.  Please check the format of the file and try again.')


/*
Author : Chris
Desc : Update system message to correct syntax
Date : 2002-07-29
*/
update systemmessage set sms_desc = 'The user group is successfully added.' where sms_id='USG005' and sms_lan='ISO-8859-1'
update systemmessage set sms_desc = 'The user group is successfully trashed.' where sms_id='USG006' and sms_lan='ISO-8859-1'
update systemmessage set sms_desc = 'The user is successfully added.' where sms_id='USR006' and sms_lan='ISO-8859-1'
update systemmessage set sms_desc = 'The user is successfully removed from the user group.' where sms_id='USR007' and sms_lan='ISO-8859-1'
update systemmessage set sms_desc = 'The user is successfully trashed.' where sms_id='USR008' and sms_lan='ISO-8859-1'
update systemmessage set sms_desc = 'The user does not belong to this role. $data.' where sms_id='USR011' and sms_lan='ISO-8859-1'

/*
Author : Kim Chan
Desc : Add syn timestamp columns, code for enhancement in imsenterprise
Date: 2002-07-29
*/

ALTER table aeItem add itm_syn_timestamp datetime NULL
ALTER table aeItemAccess add iac_syn_timestamp datetime NULL
ALTER table usrRoleTargetEntity add rte_syn_timestamp datetime NULL
ALTER TABLE aeTreeNode ADD tnd_owner_ent_id int NULL
ALTER TABLE aeTreeNode ADD tnd_code nvarchar(50) NULL
ALTER TABLE aeTreeNode ADD tnd_syn_timestamp datetime NULL

UPDATE aeTreeNode
SET tnd_owner_ent_id =
(SELECT cat_owner_ent_id FROM aeCatalog WHERE cat_id = tnd_cat_id)

ALTER TABLE aeTreeNode ALTER COLUMN tnd_owner_ent_id int NOT NULL

ALTER TABLE aeTreeNode ADD
CONSTRAINT [FK_TreeNode_Site] FOREIGN KEY
( [tnd_owner_ent_id] ) REFERENCES acSite ( [ste_ent_id] )

/*
Author : Kim Chan
Desc : Change ste uid to ROOT for all organisation, changes for Enterprise integration
Date : 2002-08-12
*/
UPDATE entity SET ent_ste_uid = 'ROOT' WHERE ent_id IN
(select usg_ent_id from usergroup where usg_role = 'root')

/*
Author : Kim Chan
Desc : Add column in acRole, acEntityRole for Enterprise integration
Date : 2002-08-12
*/
ALTER TABLE acRole ADD rol_ste_uid varchar (20) NULL
ALTER TABLE acEntityRole ADD erl_syn_timestamp datetime NULL

/*
Author : Kim Chan
Desc : Add FOREIGN key CONSTRAINT in table UsrRoleTargetEntity
Date : 2002-08-12
*/
ALTER TABLE [dbo].[UsrRoleTargetEntity] ADD
 CONSTRAINT [FK_USRROLETARGETENTITY_USER_ENTITY] FOREIGN KEY
 (
  [rte_usr_ent_id]
 ) REFERENCES [dbo].[RegUser] (
  [usr_ent_id]
 )
ALTER TABLE [dbo].[UsrRoleTargetEntity] ADD
 CONSTRAINT [FK_USRROLETARGETENTITY_ROL_EXT_ID] FOREIGN KEY
 (
  [rte_rol_ext_id]
 ) REFERENCES [dbo].[acRole] (
  [rol_ext_id]
 )
ALTER TABLE [dbo].[UsrRoleTargetEntity] ADD
 CONSTRAINT [FK_USRROLETARGETENTITY_ENTITY] FOREIGN KEY
 (
  [rte_ent_id]
 ) REFERENCES [dbo].[Entity] (
  [ent_id]
 )

/*
Author: Dennis Yip
Desc: Competency Enhance Step 1: remove cmSkillTreenode and cmSkillBase
Date: 2002-08-14
*/
-- Scripts For Remove cmSkillBase and cmSkillTreeNode

-- add columns for cmSkill
alter table cmSkill add skl_type varchar(20)
GO
alter table cmSkill add skl_owner_ent_id int
GO
alter table cmSkill add skl_create_usr_id varchar(20)
GO
alter table cmSkill add skl_create_timestamp datetime
GO
alter table cmSkill add skl_update_usr_id varchar(20)
GO
alter table cmSkill add skl_update_timestamp datetime
GO
alter table cmSkill add skl_parent_skl_skb_id int
GO
alter table cmSkill alter column skl_ssl_id int NULL
GO

-- move skl_stn_skb_id to skl_parent_skl_skb_id
update cmSkill set skl_parent_skl_skb_id = skl_stn_skb_id

-- move cmSkillTreeNode to cmSkill
insert into cmSkill
select stn_skb_id, stn_title, null, null, stn_stn_skb_id_parent, null, 'COMPOSITE_SKILL', NULL, NULL, NULL, NULL, NULL, stn_stn_skb_id_parent
from cmSkillTreeNode

-- move cmSkillBase to cmSkill
declare cur_skb cursor for
select skb_id, skb_type, skb_owner_ent_id, skb_create_usr_id, skb_create_timestamp, skb_update_usr_id, skb_update_timestamp
from cmSkillBase
declare @skb_id int
declare @skb_type varchar(20)
declare @skb_owner_ent_id int
declare @skb_create_usr_id varchar(20)
declare @skb_create_timestamp datetime
declare @skb_update_usr_id varchar(20)
declare @skb_update_timestamp datetime

open cur_skb
fetch next from cur_skb into @skb_id, @skb_type, @skb_owner_ent_id, @skb_create_usr_id, @skb_create_timestamp, @skb_update_usr_id, @skb_update_timestamp
while @@FETCH_STATUS = 0
begin
	update cmSkill set skl_type = @skb_type,
			   skl_owner_ent_id = @skb_owner_ent_id,
			   skl_create_usr_id = @skb_create_usr_id,
			   skl_create_timestamp = @skb_create_timestamp,
			   skl_update_usr_id = @skb_update_usr_id,
			   skl_update_timestamp = @skb_update_timestamp
	where skl_skb_id = @skb_id
	fetch next from cur_skb into @skb_id, @skb_type, @skb_owner_ent_id, @skb_create_usr_id, @skb_create_timestamp, @skb_update_usr_id, @skb_update_timestamp
end
close cur_skb
deallocate cur_skb

-- alter new columns to NOT NULL
alter table cmSkill alter column skl_type varchar(20) NOT NULL
GO
alter table cmSkill alter column skl_owner_ent_id int NOT NULL
GO
alter table cmSkill alter column skl_create_usr_id varchar(20) NOT NULL
GO
alter table cmSkill alter column skl_create_timestamp datetime NOT NULL
GO
alter table cmSkill alter column skl_update_usr_id varchar(20) NOT NULL
GO
alter table cmSkill alter column skl_update_timestamp datetime NOT NULL
GO

-- add columns for cmSkillSet
alter table cmSkillSet add sks_owner_ent_id int
GO
alter table cmSkillSet add sks_create_usr_id varchar(20)
GO
alter table cmSkillSet add sks_create_timestamp datetime
GO
alter table cmSkillSet add sks_update_usr_id varchar(20)
GO
alter table cmSkillSet add sks_update_timestamp datetime
GO

-- move cmSkillBase to cmSkillSet
declare cur_sks cursor for
select skb_id, skb_owner_ent_id, skb_create_usr_id, skb_create_timestamp, skb_update_usr_id, skb_update_timestamp
from cmSkillBase
declare @sks_id int
declare @sks_owner_ent_id int
declare @sks_create_usr_id varchar(20)
declare @sks_create_timestamp datetime
declare @sks_update_usr_id varchar(20)
declare @sks_update_timestamp datetime

open cur_sks
fetch next from cur_sks into @sks_id, @sks_owner_ent_id, @sks_create_usr_id, @sks_create_timestamp, @sks_update_usr_id, @sks_update_timestamp
while @@FETCH_STATUS = 0
begin
	update cmSkillSet set
			   sks_owner_ent_id = @sks_owner_ent_id,
			   sks_create_usr_id = @sks_create_usr_id,
			   sks_create_timestamp = @sks_create_timestamp,
			   sks_update_usr_id = @sks_update_usr_id,
			   sks_update_timestamp = @sks_update_timestamp
	where sks_skb_id = @sks_id
	fetch next from cur_sks into @sks_id, @sks_owner_ent_id, @sks_create_usr_id, @sks_create_timestamp, @sks_update_usr_id, @sks_update_timestamp
end
close cur_sks
deallocate cur_sks
update cmSkill set skl_type = 'COMPOSITE_SKILL' where skl_type = 'NODE'

-- alter new columns to NOT NULL
alter table cmSkillSet alter column sks_owner_ent_id int NOT NULL
GO
alter table cmSkillSet alter column sks_create_usr_id varchar(20) NOT NULL
GO
alter table cmSkillSet alter column sks_create_timestamp datetime NOT NULL
GO
alter table cmSkillSet alter column sks_update_usr_id varchar(20) NOT NULL
GO
alter table cmSkillSet alter column sks_update_timestamp datetime NOT NULL
GO


-- Drop cmSkillBase and cmSkillTreeNode and create new relation
ALTER TABLE cmSkill DROP CONSTRAINT FK_cmSkill_cmSkillBase
GO
ALTER TABLE cmSkillSetCoverage DROP CONSTRAINT FK_cmSkillSetCoverage_cmSkillBase
GO
drop table cmSkillTreeNode
GO
drop table cmSkillBase
GO

ALTER TABLE [dbo].[cmSkill] ADD
	CONSTRAINT [FK_cmSkill_cmSkill] FOREIGN KEY
	(
		[skl_parent_skl_skb_id]
	) REFERENCES [dbo].[cmSkill] (
		[skl_skb_id]
	)
GO

ALTER TABLE [dbo].[cmSkillSetCoverage] ADD
	CONSTRAINT [FK_cmSkillSetCoverage_cmSkill] FOREIGN KEY
	(
		[ssc_skb_id]
	) REFERENCES [dbo].[cmSkill] (
		[skl_skb_id]
	)
GO

ALTER TABLE [dbo].[aeItemCompetency] ADD
	CONSTRAINT [FK_aeItemCompetency_cmSkill] FOREIGN KEY
	(
		[itc_skl_skb_id]
	) REFERENCES [dbo].[cmSkill] (
		[skl_skb_id]
	)
GO

-- Facilitate rollup method of Composite Skill
alter table cmSkill add skl_derive_rule varchar(20) NULL
GO

-- Update all dervie rule to 'MIM'
update cmSkill set skl_derive_rule = 'MIN' where skl_type = 'COMPOSITE_SKILL'

/*
Author: Dennis Yip
Desc: Competency Enhance Step 2: change identity column
Date: 2002-08-15
*/
Change CmSkill.skl_skb_id and CmSkillSet.sks_skb_id to Identity column

/*
Author: Dennis Yip
Desc: Competency Enhance Step 3: Insert new System Message
Date: 2002-08-15
*/
Insert into SystemMessage Values ( 'CMP010', 'ISO-8859-1', 'The assessment is not ready.' )
Insert into SystemMessage Values ( 'CMP010', 'Big5', '(Big5)The assessment is not ready.' )
Insert into SystemMessage Values ( 'CMP010', 'GB2312', '(GB2312)The assessment is not ready.' )
Insert into SystemMessage Values ( 'CMP011', 'ISO-8859-1', 'The assessment has expired.' )
Insert into SystemMessage Values ( 'CMP011', 'Big5', '(Big5)The assessment has expired.' )
Insert into SystemMessage Values ( 'CMP011', 'GB2312', '(GB2312)The assessment has expired.' )

insert into SystemMessage (sms_id, sms_lan, sms_desc)
values
('CMP014', 'ISO-8859-1', 'Cannot add the skill since the selected scale is not the same as the scale of the upper level skill.')
insert into SystemMessage (sms_id, sms_lan, sms_desc)
values
('CMP014', 'Big5', 'Cannot add the skill since the selected scale is not the same as the scale of the upper level skill.')
insert into SystemMessage (sms_id, sms_lan, sms_desc)
values
('CMP014', 'GB2312', 'Cannot add the skill since the selected scale is not the same as the scale of the upper level skill.')

insert into SystemMessage (sms_id, sms_lan, sms_desc)
values
('CMP015', 'ISO-8859-1', 'Cannot update the skill since only the scale of the top level skill can be changed.')
insert into SystemMessage (sms_id, sms_lan, sms_desc)
values
('CMP015', 'Big5', 'Cannot update the skill since only the scale of the top level skill can be changed.')
insert into SystemMessage (sms_id, sms_lan, sms_desc)
values
('CMP015', 'GB2312', 'Cannot update the skill since only the scale of the top level skill can be changed.')

insert into SystemMessage (sms_id, sms_lan, sms_desc)
values
('CMP016', 'ISO-8859-1', 'Cannot change the scale since the skill, or its elementary skill(s), is being used in assessment.')
insert into SystemMessage (sms_id, sms_lan, sms_desc)
values
('CMP016', 'Big5', 'Cannot change the scale since the skill, or its elementary skill(s), is being used in assessment.')
insert into SystemMessage (sms_id, sms_lan, sms_desc)
values
('CMP016', 'GB2312', 'Cannot change the scale since the skill, or its elementary skill(s), is being used in assessment.')

Insert into SystemMessage Values('CMP018','ISO-8859-1',	'Cannot set the collection date before today.')
Insert into SystemMessage Values('CMP018','Big5', '(Big5)Cannot set the collection date before today')
Insert into SystemMessage Values('CMP018','GB2312', '(GB2312)Cannot set the collection date before today')

Insert into SystemMessage Values('CMP017','ISO-8859-1',	'Cannot set the notification date before today.')
Insert into SystemMessage Values('CMP017','Big5', '(Big5)Cannot set the notification date before today')
Insert into SystemMessage Values('CMP017','GB2312', '(GB2312)Cannot set the notification date before today')

insert into SystemMessage (sms_id, sms_lan, sms_desc)
values
('CMP019', 'ISO-8859-1', 'Composite skill not empty.')
insert into SystemMessage (sms_id, sms_lan, sms_desc)
values
('CMP019', 'Big5', 'Composite skill not empty.')
insert into SystemMessage (sms_id, sms_lan, sms_desc)
values
('CMP019', 'GB2312', 'Composite skill not empty.')

insert into SystemMessage (sms_id, sms_lan, sms_desc)
values
('CMP020', 'ISO-8859-1', 'Cannot add/remove scale level since the scale is being used in assessment.')
insert into SystemMessage (sms_id, sms_lan, sms_desc)
values
('CMP020', 'Big5', 'Cannot add/remove scale level since the scale is being used in assessment.')
insert into SystemMessage (sms_id, sms_lan, sms_desc)
values
('CMP020', 'GB2312', 'Cannot add/remove scale level since the scale is being used in assessment.')

/*
Author: Dennis Yip
Desc: Competency Enhance Step 4: Insert new message template
Date: 2002-08-15
*/
insert into xslTemplate
values
('ASSESSMENT_NOTIFICATION', 'HTML_MAIL', 'SMTP', null, 'assessment_notification.xsl', 'http://host:port/servlet/Dispatcher?module=competency.CompetencyModule&', 1)
declare @xtp_id int
select @xtp_id = max(xtp_id) from xslTemplate
insert into xslparamname values (@xtp_id, 1, 'ent_ids')
insert into xslparamname values (@xtp_id, 2, 'sender_id')
insert into xslparamname values (@xtp_id, 3, 'cmd')
insert into xslparamname values (@xtp_id, 4, 'asm_id')
insert into xslparamname values (@xtp_id, 5, 'label_lan')
insert into xslparamname values (@xtp_id, 6, 'site_id')
insert into xslparamname values (@xtp_id, 7, 'style')

insert into xslTemplate
values
('ASSESSMENT_COLLECTION_NOTIFICATION', 'HTML_MAIL', 'SMTP', null, 'assessment_collection_notification.xsl', 'http://host:port/servlet/Dispatcher?module=competency.CompetencyModule&', 1)
select @xtp_id = max(xtp_id) from xslTemplate
insert into xslparamname values (@xtp_id, 1, 'ent_ids')
insert into xslparamname values (@xtp_id, 2, 'sender_id')
insert into xslparamname values (@xtp_id, 3, 'cmd')
insert into xslparamname values (@xtp_id, 4, 'asm_id')
insert into xslparamname values (@xtp_id, 5, 'label_lan')
insert into xslparamname values (@xtp_id, 6, 'site_id')
insert into xslparamname values (@xtp_id, 7, 'style')

CREATE TABLE [dbo].[cmAssessmentNotify] (
 [asn_asm_id] [int] NOT NULL ,
 [asn_msg_id] [int] NOT NULL ,
 [asn_type] [varchar] (20) NOT NULL
) ON [PRIMARY]

ALTER TABLE [dbo].[cmAssessmentNotify] ADD
CONSTRAINT [FK_cmAssessmentNotify_cmAssessment]
FOREIGN KEY ( [asn_asm_id] ) REFERENCES [dbo].[cmAssessment] ( [asm_id] ),
CONSTRAINT [FK_cmAssessmentNotify_mgMessage]
FOREIGN KEY ( [asn_msg_id] ) REFERENCES [dbo].[mgMessage] ( [msg_id])

/*
Author: Dennis Yip
Desc: Competency Enhance Step 5: new acFunction for ToDoList, and grand right to decided roles into acRoleFunction and acHomepage
Date: 2002-08-15
*/
insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('TO_DO_LIST', 'FUNCTION', 'HOMEPAGE', '2002-07-12', '<function id="TO_DO_LIST"> <desc lan="ISO-8859-1" name="To Do List"/> <desc lan="Big5" name="To Do List"/> <desc lan="GB2312" name="To Do List"/></function>')

/*
Author: Dennis Yip
Desc: Competency Enhance Step 6: Skill enhancement
Date: 2002-08-15
*/
alter table cmSkill add skl_ancestor varchar(255) null
GO
alter table cmSkill add skl_delete_usr_id varchar(20) null
GO
alter table cmSkill add skl_delete_timestamp datetime null
GO

/*
Author: Dennis Yip
Desc: Competency Enhance Step 7: Miscellaneous
Date: 2002-08-15
*/
-- Allow Scale Level description to be NULL
alter table cmSkillLevel alter column sle_description nvarchar(4000) NULL
GO

-- Changes linking cmAssessment and Module
alter table cmAssessment add asm_type varchar(20) NULL
GO
update cmAssessment set asm_type = 'SVY'
GO
alter table cmAssessment alter column asm_type varchar(20) NOT NULL
alter table cmAssessment add asm_mod_res_id int NULL
alter table cmAssessment add asm_marking_scheme_xml ntext NULL
GO

-- Facilitate soft delete of Scale
alter table cmSkillScale add ssl_delete_usr_id varchar(50)
alter table cmSkillScale add ssl_delete_timestamp datetime
GO

alter table cmAssessment add asm_auto_resolved_ind int NULL
GO
update cmAssessment set asm_auto_resolved_ind = 0
GO
alter table cmAssessment alter column asm_auto_resolved_ind int NOT NULL
GO

update cmSkill set skl_derive_rule = 'MIN' where skl_type = 'COMPOSITE_SKILL'
alter table cmSkill drop column skl_stn_skb_id
GO
update cmSkill set skl_ssl_id = 1 where skl_ssl_id is null

/*
Author: Dennis Yip
Desc: Competency Enhance Step 8: clone assessment's survey (SKT) to assessment survey (ASY)
Date: 2002-08-15
*/
-- clone assessment's survey (SKT) to assessment survey (ASY)
declare cur_skt cursor for
select sks_skb_id from cmSkillSet where sks_type = 'SKT'
and exists (Select * from cmAssessment where asm_sks_skb_id = sks_skb_id)
declare @max_sks_id int
declare @sks_id int

open cur_skt
fetch next from cur_skt into @sks_id

while @@FETCH_STATUS = 0
begin
	insert into cmSkillSet
	(sks_type, sks_title, sks_xml, sks_owner_ent_id, sks_create_usr_id, sks_create_timestamp, sks_update_usr_id, sks_update_timestamp)
	select 'ASY', sks_title, sks_xml, sks_owner_ent_id, sks_create_usr_id, sks_create_timestamp, sks_update_usr_id, sks_update_timestamp
	from cmSkillSet
	where sks_skb_id = @sks_id

	select @max_sks_id = max(sks_skb_id) from cmSkillSet

	insert into cmSkillSetCoverage
	select @max_sks_id, ssc_skb_id, ssc_level, ssc_priority, ssc_xml
	from cmSkillSetCoverage
	where ssc_sks_skb_id = @sks_id

	update cmAssessment set asm_sks_skb_id = @max_sks_id where asm_sks_skb_id = @sks_id

	fetch next from cur_skt into @sks_id
end
close cur_skt
deallocate cur_skt

/*
Author: Dennis Yip
Desc: Access Control on Report Template Step 1: Add new access control function
Date: 2002-08-15
*/
insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('RTE_READ', 'INSTANCE', 'REPORT_TEMPLATE', getdate(), null)

/*
Author: Dennis Yip
Desc: Access Control on Report Template Step 2: Add access control table
Date: 2002-08-15
*/
CREATE TABLE [dbo].[acReportTemplate] (
	[ac_rte_id] [int] NOT NULL ,
	[ac_rte_ent_id] [int] NULL ,
	[ac_rte_rol_ext_id] [varchar] (255) NULL ,
	[ac_rte_ftn_ext_id] [varchar] (255) NOT NULL ,
	[ac_rte_owner_ind] [int] NOT NULL ,
	[ac_rte_create_usr_id] [varchar] (20) NOT NULL ,
	[ac_rte_create_timestamp] [datetime] NOT NULL
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[acReportTemplate] ADD
	CONSTRAINT [FK_acReportTemplate_ReportTemplate] FOREIGN KEY
	(
		[ac_rte_id]
	) REFERENCES [dbo].[ReportTemplate] (
		[rte_id]
	)
GO

/*
Author: Dennis Yip
Desc: Access Control on Report Template Step 3: Grand right
Date: 2002-08-15
*/
-- Grand right to decided roles by inserting acRoleFunction and acReportTemplate

/*
Author: Dennis Yip
Desc: Access Control on Report Template Step 4: Update title of Management Report and Training Report
Date: 2002-08-15
*/
update ReportTemplate
set rte_title_xml = '<title> <desc lan="ISO-8859-1" name="Management Report"/> <desc lan="Big5" name="Management Report in BIG 5"/> <desc lan="GB2312" name="Management Report in GB"/></title>'
where rte_type = 'LEARNER'

update ReportTemplate
set rte_title_xml = '<title> <desc lan="ISO-8859-1" name="Training Report"/> <desc lan="Big5" name="Training Report in BIG5"/> <desc lan="GB2312" name="Training Report in GB"/></title>'
where rte_type = 'COURSE'

/*
Author: Dennis Yip
Desc: Access Control on Staff Learning Plan
Date: 2002-08-15
*/
insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('STAFF_SOLN_LINK', 'FUNCTION', 'HOMEPAGE', GETDATE(), '<function id="STAFF_SOLN_LINK"><desc lan="ISO-8859-1" name="Staff Learning Plan"/><desc lan="Big5" name="Staff Learning Plan"/><desc lan="GB2312" name="Staff Learning Plan"/></function>')
-- Grand right to decided roles by inserting into acRoleFunction and acHomepage

/*
Author: Lun
Desc: New configurable param in acSite to indicate if this site need user grade peer logic
Date: 2002-08-16
*/
alter table acSite add ste_target_by_peer_ind int NULL
GO
Update acSite Set ste_target_by_peer_ind = 0
GO
alter table acSite alter column ste_target_by_peer_ind int NOT NULL
GO

/*
Author: Lun
Desc: New columns for sending attachment in Messaging Module
Date: 2002-08-16
*/
ALTER TABLE xslTemplate ADD xtp_title varchar(255) NULL
GO
ALTER TABLE xslMgSelectedTemplate ADD mst_type varchar(50) NULL
GO
Update xslMgSelectedTemplate Set mst_type = 'MAIN'
GO
ALTER TABLE xslMgSelectedTemplate ALTER COLUMN mst_type varchar(50) NOT NULL
GO
ALTER TABLE xslTemplate ALTER COLUMN xtp_channel_type varchar(20) NULL
GO
ALTER TABLE xslTemplate ALTER COLUMN xtp_xml_url varchar(100) NULL
GO

/*
Author: Dennis Yip
Date: 2002-09-16
Desc: Add new acFunction entries to seperate Online Content Authoring and
      Item Maintenance
*/
--Step 1: Add new acFunction entries
insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('COS_PERMISSION_READ', 'FUNCTION', 'COURSE', GETDATE(), NULL)
insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('COS_PERMISSION_WRITE', 'FUNCTION', 'COURSE', GETDATE(), NULL)
insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('COS_PERMISSION_EXEC', 'FUNCTION', 'COURSE', GETDATE(), NULL)

--Step 2: Assign the new functions to Roles originally has
--        RES_PERMISSION_READ, RES_PERMISSION_WRITE, RES_PERMISSION_EXEC

declare @res_read int
declare @res_write int
declare @res_exec int
declare @cos_read int
declare @cos_write int
declare @cos_exec int

select @res_read = ftn_id from acFunction
where ftn_ext_id = 'RES_PERMISSION_READ'
select @res_write = ftn_id from acFunction
where ftn_ext_id = 'RES_PERMISSION_WRITE'
select @res_exec = ftn_id from acFunction
where ftn_ext_id = 'RES_PERMISSION_EXEC'

select @cos_read = ftn_id from acFunction
where ftn_ext_id = 'COS_PERMISSION_READ'
select @cos_write = ftn_id from acFunction
where ftn_ext_id = 'COS_PERMISSION_WRITE'
select @cos_exec = ftn_id from acFunction
where ftn_ext_id = 'COS_PERMISSION_EXEC'

insert into acRoleFunction select rfn_rol_id, @cos_read, getdate() from acRoleFunction where rfn_ftn_id = @res_read
insert into acRoleFunction select rfn_rol_id, @cos_write, getdate() from acRoleFunction where rfn_ftn_id = @res_write
insert into acRoleFunction select rfn_rol_id, @cos_exec, getdate() from acRoleFunction where rfn_ftn_id = @res_exec

--Step 3: If you want to seperate Online Content Authoring, configure the newly added acFunction entries accordingly



/*
Author: Wai Lun
Date: 2002-09-23
Desc: Add user_id and update_timestamp column for concurrency checking
*/
Alter Table ModuleEvaluation Add mov_create_usr_id varchar(20)
Alter Table ModuleEvaluation Add mov_create_timestamp datetime
Alter Table ModuleEvaluation Add mov_update_usr_id varchar(20)
Alter Table ModuleEvaluation Add mov_update_timestamp datetime

/*
Author: Wai Lun
Date: 2002-09-23
Desc: System message for submit svy failure because of the concurrency
*/
Insert Into SystemMessage Values( 'AEQM09', 'ISO-8859-1', 'The record has just been updated by another user. $data')
Insert Into SystemMessage Values( 'AEQM09', 'GB2312', 'The record has just been updated by another user. $data')
Insert Into SystemMessage Values( 'AEQM09', 'Big5', 'The record has just been updated by another user. $data')

/*
Author: Dennis
Date: 2002-09-24
Desc: JI and Reminder xsl file names have been changed, so as the corresponsing database values
	   NOTE: If your database is copied from CORE before 2002-09-24 and you want to use the JI and Reminder configuration in CORE,
                     you need to execute the SQL written in incidient CORE_0240.doc as well.
			Otherwise you need to configurate JI and Reminder yourself.
*/
update xslTemplate set xtp_xsl = 'msg_ji_notes.xsl' where xtp_type = 'JI'
update xslTemplate set xtp_xsl = 'msg_reminder_notes.xsl' where xtp_type = 'REMINDER'

/*
Author: Kim
Date: 2002-09-26
Desc: resize of db column in table Entity and reguser, usergrade, aeItem  to match with IMS spec
*/

alter table Entity alter column ent_ste_uid nvarchar (255) null

alter table Reguser drop column usr_bplace_bil
alter table Reguser drop column usr_city_bil
alter table Reguser drop column usr_occupation_bil
alter table Reguser drop column usr_income_level
alter table Reguser drop column usr_edu_role
alter table Reguser drop column usr_edu_level
alter table Reguser drop column usr_school_bil
alter table Reguser drop column usr_special_date_1
alter table Reguser drop column usr_webganizer_date

alter table RegUser alter column usr_full_name_bil nvarchar (255) null
alter table RegUser alter column usr_display_bil nvarchar (255) not null
alter table RegUser alter column usr_last_name_bil nvarchar (255) null

alter table RegUser alter column usr_first_name_bil nvarchar (255) null
alter table RegUser alter column usr_initial_name_bil nvarchar (255) null
alter table RegUser alter column usr_email varchar (255) null
alter table RegUser alter column usr_extra_1 nvarchar (255) null
alter table RegUser alter column usr_extra_2 nvarchar (255) null
alter table RegUser alter column usr_extra_3 nvarchar (50) null
alter table RegUser alter column usr_extra_4 nvarchar (50) null
alter table RegUser alter column usr_extra_5 nvarchar (50) null
alter table RegUser alter column usr_extra_6 nvarchar (50) null
alter table RegUser alter column usr_extra_7 nvarchar (50) null
alter table RegUser alter column usr_extra_8 nvarchar (50) null
alter table RegUser alter column usr_extra_9 nvarchar (50) null
alter table RegUser alter column usr_extra_10 nvarchar (50) null

alter table UserGrade alter column ugr_display_bil nvarchar (255) not null
alter table aeItem alter column itm_code nvarchar (255) null
GO

/*
Author: Kim
Date: 2002-09-26
Desc: add column for IMS spec, to support user, usergroup syn or not, role syn or not , role with timeframe and roleTargetEntity with timeframe
*/
alter table Entity add ent_syn_ind bit null
GO
update Entity set ent_syn_ind = 1 where ent_syn_date is not null and (ent_ste_uid <> 'ROOT' or ent_ste_uid is null)
update Entity set ent_syn_ind = 0 where ent_syn_date is null or ent_ste_uid = 'ROOT'
alter table Entity alter column ent_syn_ind bit not null
GO

alter table usrRoleTargetEntity add rte_eff_start_datetime datetime null
alter table usrRoleTargetEntity add rte_eff_end_datetime datetime null
GO
update usrRoleTargetEntity set rte_eff_start_datetime = '1753-01-01 00:00:00.000' where rte_eff_start_datetime is null
update usrRoleTargetEntity set rte_eff_end_datetime = '9999-12-31 23:59:59.000' where rte_eff_end_datetime is null

alter table acEntityRole add erl_eff_start_datetime datetime null
alter table acEntityRole add erl_eff_end_datetime datetime null
GO
update acEntityRole set erl_eff_start_datetime = '1753-01-01 00:00:00.000' , erl_eff_end_datetime = '9999-12-31 23:59:59.000'

alter table entity add ent_syn_rol_ind bit
GO
update entity set ent_syn_rol_ind = 1
alter table entity alter column ent_syn_rol_ind bit not null

/*
Author: Kim
Date: 2002-09-26
Desc: add column in aeAttendance to support rate, att_timestamp and attendance without application on the same itm
	add column in courseEvaluation to support update_timestamp
*/

alter table aeAttendance add att_rate decimal (18, 4) NULL

alter table aeAttendance add att_itm_id int NULL
GO
update aeAttendance set att_itm_id =
(select app_itm_id from aeApplication where app_id = att_app_id )
alter table aeAttendance alter column att_itm_id int NOT NULL

ALTER TABLE [dbo].[aeAttendance] DROP CONSTRAINT PK_aeAttendance

ALTER TABLE [dbo].[aeAttendance] WITH NOCHECK ADD
 CONSTRAINT [PK_aeAttendance] PRIMARY KEY  NONCLUSTERED
 (
  [att_app_id],
  [att_itm_id]
 ) WITH  FILLFACTOR = 90  ON [PRIMARY]
GO

alter table aeAttendance add att_timestamp datetime NULL
GO
update aeAttendance set att_timestamp = att_update_timestamp
update aeAttendance set att_update_timestamp  = getDate() where att_update_timestamp > getDate()

alter table courseEvaluation add cov_update_timestamp datetime NULL
GO
update courseEvaluation set cov_update_timestamp = cov_last_acc_datetime
update courseEvaluation set cov_update_timestamp = getDate() where cov_update_timestamp is null
alter table courseEvaluation alter column cov_update_timestamp datetime not NULL
GO

/*
Author: Kim
Date: 2002-09-26
Desc: add column in aeItemType, aeItem , aeItemTypeTemplate to support session
*/

alter table aeItemType add ity_session_ind [int] NULL
alter table aeItemType add ity_create_session_ind [int] NULL
alter table aeItemType add ity_has_attendance_ind [int] NULL
GO
update aeItemType set ity_session_ind = 0, ity_create_session_ind = 0
update aeItemType set ity_has_attendance_ind = ity_run_ind  where ity_id = 'classroom'
update aeItemType set ity_has_attendance_ind = 1 where ity_id = 'selfstudy'
update aeItemType set ity_has_attendance_ind = 0 where ity_id not in ('selfstudy' , 'classroom' )

alter table aeItemType alter column ity_session_ind [int] NOT NULL
alter table aeItemType alter column ity_create_session_ind [int] NOT NULL
alter table aeItemType alter column ity_has_attendance_ind [int] NOT NULL

alter table aeItem add itm_create_session_ind [int] NULL
alter table aeItem add itm_session_ind [int] NULL
alter table aeItem add itm_has_attendance_ind [int] NULL
GO
update aeItem set itm_create_session_ind = 0, itm_session_ind  = 0
update aeItem set itm_has_attendance_ind = itm_run_ind  where itm_type = 'classroom'
update aeItem set itm_has_attendance_ind = 1 where itm_type = 'selfstudy'
update aeItem set itm_has_attendance_ind = 0 where itm_type not in ('selfstudy' , 'classroom' )

alter table aeItem alter column  itm_create_session_ind [int] NOT NULL
alter table aeItem alter column  itm_session_ind [int] NOT NULL
alter table aeItem alter column  itm_has_attendance_ind [int] NOT NULL

ALTER TABLE aeItemType DROP CONSTRAINT PK_aeItemType
ALTER TABLE aeItemType WITH NOCHECK ADD
 CONSTRAINT [PK_aeItemType] PRIMARY KEY  NONCLUSTERED
 (
  [ity_owner_ent_id],
  [ity_id],
  [ity_run_ind],
  [ity_session_ind]
 ) WITH  FILLFACTOR = 90  ON [PRIMARY]
GO

INSERT INTO SYSTEMMESSAGE VALUES ('AEIT16', 'ISO-8859-1', 'Cannot create new version for a Session.')
INSERT INTO SYSTEMMESSAGE VALUES ('AEIT16', 'Big5', 'Cannot create new version for a Session.')
INSERT INTO SYSTEMMESSAGE VALUES ('AEIT16', 'GB2312', 'Cannot create new version for a Session.')

alter table aeItemTypeTemplate add itt_session_tpl_ind bit
GO
update aeItemTypeTemplate set  itt_session_tpl_ind = 0
alter table aeItemTypeTemplate alter column itt_session_tpl_ind bit not null

alter table aeItemtype add ity_ji_ind [int] NULL
GO
update aeItemType set ity_ji_ind = ity_run_ind
alter table aeItemtype alter column ity_ji_ind [int] not NULL

alter table aeItemtype add ity_completion_criteria_ind [int] NULL
GO
update aeItemType set ity_completion_criteria_ind = ity_create_run_ind  where ity_id = 'classroom'
update aeItemType set ity_completion_criteria_ind = 1 where ity_id = 'selfstudy'
update aeItemType set ity_completion_criteria_ind = 0 where ity_completion_criteria_ind is null
alter table aeItemtype alter column ity_completion_criteria_ind [int] not NULL

alter table aeItem add itm_ji_ind [int] NULL
GO
update aeItem set itm_ji_ind = itm_run_ind
alter table aeItem alter column  itm_ji_ind [int] not NULL

alter table aeItem add itm_completion_criteria_ind [int] NULL
GO
update aeItem set itm_completion_criteria_ind = itm_create_run_ind  where itm_type = 'classroom'
update aeItem set itm_completion_criteria_ind = 1 where itm_type = 'selfstudy'
update aeItem set itm_completion_criteria_ind = 0 where itm_completion_criteria_ind is null
alter table aeItem alter column itm_completion_criteria_ind [int] not NULL
GO

/*
Author: Kim
Date: 2002-09-26
Desc: add column in acSite to support ldap authentication
*/
alter table acSite add ste_ldap_host nvarchar (255) NULL
alter table acSite add ste_ldap_dn nvarchar (100) NULL
GO

/*
Author: Wai Lun
Date: 2002-09-26
Desc: add column into aeTemplateView to indicate the view show workflow template or not
*/
Alter Table aeTemplateView Add tvw_wrk_tpl_ind int null
GO
Update aeTemplateView Set tvw_wrk_tpl_ind = 0
Alter Table aeTemplateView Alter Column tvw_wrk_tpl_ind int not null

/*
Author: Dennis Yip
Date: 2002-10-04
Desc: New table to store view for Report Template
*/
CREATE TABLE [dbo].[ObjectView] (
	[ojv_owner_ent_id] [int] NOT NULL ,
	[ojv_type] [varchar] (50) NOT NULL ,
	[ojv_subtype] [varchar] (50) NOT NULL ,
	[ojv_option_xml] [ntext] NOT NULL ,
	[ojv_create_usr_id] [varchar] (20) NOT NULL ,
	[ojv_create_timestamp] [datetime] NOT NULL ,
	[ojv_update_usr_id] [varchar] (50) NOT NULL ,
	[ojv_update_timestamp] [datetime] NOT NULL
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO

ALTER TABLE [dbo].[ObjectView] WITH NOCHECK ADD
	CONSTRAINT [PK_ObjectView] PRIMARY KEY  CLUSTERED
	(
		[ojv_owner_ent_id],
		[ojv_type],
		[ojv_subtype]
	)  ON [PRIMARY]
GO

ALTER TABLE [dbo].[ObjectView] ADD
	CONSTRAINT [FK_ObjectView_acSite] FOREIGN KEY
	(
		[ojv_owner_ent_id]
	) REFERENCES [dbo].[acSite] (
		[ste_ent_id]
	)
GO

/*
Author: Richard Shi
Desc: Application Processing;add column "app_ext4" in Table aeApplication
  Training Contract detail
Date: 2002-08-28
*/
ALTER TABLE dbo.aeApplication ADD
app_ext4 ntext NULL
GO

/*
Author: Richard Shi
Desc: Application Processing;add column "app_usr_prof_xml" in Table aeApplication
  capture user profile in the application record when the user is put in the admitted queue
Date: 2002-08-28
*/
ALTER TABLE dbo.aeApplication ADD
app_usr_prof_xml ntext NULL
GO


/*
Author: Kim Chan
Desc: To support multi timeframe for same role, same user
Date: 2002-10-05
*/

ALTER TABLE acEntityRole DROP CONSTRAINT PK_acl_EntityRole

ALTER TABLE acEntityRole alter column erl_eff_start_datetime datetime not null
ALTER TABLE acEntityRole alter column erl_eff_end_datetime datetime not null
GO

ALTER TABLE [dbo].[acEntityRole] WITH NOCHECK ADD
 CONSTRAINT [PK_acl_EntityRole] PRIMARY KEY  NONCLUSTERED
 (
  [erl_ent_id],
  [erl_rol_id],
  [erl_eff_start_datetime],
  [erl_eff_end_datetime]
 ) WITH  FILLFACTOR = 90  ON [PRIMARY]
GO

/*
Author: kawai tse
Desc: add one more column to store course fee related data
Date: 2002.09.02
*/

ALTER TABLE aeItem ADD itm_fee_1 decimal(9, 2)
GO

/*
Author: Emily Li
Desc: add an indicator for cost center group list
Date: 2002-09-24
*/
ALTER TABLE aeTemplateView ADD tvw_cost_center_ind int NULL
GO
UPDATE aeTemplateView SET tvw_cost_center_ind = 0
ALTER TABLE aeTemplateView ALTER COLUMN tvw_cost_center_ind int NOT NULL
GO

/*
Author: Emily
Desc: add colummn for user count & traning budget
Date: 2002-10-12
*/
ALTER TABLE UserGroup ADD usg_usr_cnt int NULL
GO
UPDATE UserGroup SET usg_usr_cnt = 0
ALTER TABLE UserGroup ADD usg_budget int NULL
GO


/*
Author: Stanley
Desc: add Param Value to xslTemplate
Date: 2002-10-23
*/
/* ENROLLMENT_WITHDRAWAL_APPROVED */
declare @xtp_id int
declare @xpn_pos int
select @xtp_id = xtp_id from xslTemplate where xtp_type = 'ENROLLMENT_WITHDRAWAL_APPROVED'
select @xpn_pos = max(xpn_pos)+1 from xslParamName where xpn_xtp_id = @xtp_id

insert xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, @xpn_pos, 'bcc_ent_ids')

declare @xpn_id int
select @xpn_id = xpn_id from xslParamName where xpn_xtp_id = @xtp_id and xpn_pos = @xpn_pos

DECLARE msg_cursor CURSOR FOR
select xpv_mst_msg_id from mgXslParamValue where xpv_mst_xtp_id = @xtp_id group by xpv_mst_msg_id

OPEN msg_cursor
declare @msg_id int

FETCH NEXT FROM msg_cursor INTO @msg_id

WHILE @@FETCH_STATUS = 0
BEGIN
	insert mgXslParamValue (xpv_mst_msg_id, xpv_mst_xtp_id, xpv_xpn_id, xpv_type, xpv_value) values (@msg_id, @xtp_id, @xpn_id, 'DYNAMIC', 'GET_BCC_ENT_ID')
	FETCH NEXT FROM msg_cursor INTO @msg_id
END
CLOSE msg_cursor
DEALLOCATE msg_cursor

/* ENROLLMENT_WITHDRAWAL_NOT_APPROVED */
declare @xtp_id int
declare @xpn_pos int
select @xtp_id = xtp_id from xslTemplate where xtp_type = 'ENROLLMENT_WITHDRAWAL_NOT_APPROVED'
select @xpn_pos = max(xpn_pos)+1 from xslParamName where xpn_xtp_id = @xtp_id

insert xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, @xpn_pos, 'bcc_ent_ids')

declare @xpn_id int
select @xpn_id = xpn_id from xslParamName where xpn_xtp_id = @xtp_id and xpn_pos = @xpn_pos

DECLARE msg_cursor CURSOR FOR
select xpv_mst_msg_id from mgXslParamValue where xpv_mst_xtp_id = @xtp_id group by xpv_mst_msg_id

OPEN msg_cursor
declare @msg_id int

FETCH NEXT FROM msg_cursor INTO @msg_id

WHILE @@FETCH_STATUS = 0
BEGIN
	insert mgXslParamValue (xpv_mst_msg_id, xpv_mst_xtp_id, xpv_xpn_id, xpv_type, xpv_value) values (@msg_id, @xtp_id, @xpn_id, 'DYNAMIC', 'GET_BCC_ENT_ID')
	FETCH NEXT FROM msg_cursor INTO @msg_id
END
CLOSE msg_cursor
DEALLOCATE msg_cursor

/* ENROLLMENT_LATE_WITHDRAWAL_REQUEST */
declare @xtp_id int
declare @xpn_pos int
select @xtp_id = xtp_id from xslTemplate where xtp_type = 'ENROLLMENT_LATE_WITHDRAWAL_REQUEST'
select @xpn_pos = max(xpn_pos)+1 from xslParamName where xpn_xtp_id = @xtp_id

insert xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, @xpn_pos, 'cc_ent_ids')

declare @xpn_id int
select @xpn_id = xpn_id from xslParamName where xpn_xtp_id = @xtp_id and xpn_pos = @xpn_pos

DECLARE msg_cursor CURSOR FOR
select xpv_mst_msg_id from mgXslParamValue where xpv_mst_xtp_id = @xtp_id group by xpv_mst_msg_id

OPEN msg_cursor
declare @msg_id int

FETCH NEXT FROM msg_cursor INTO @msg_id

WHILE @@FETCH_STATUS = 0
BEGIN
	insert mgXslParamValue (xpv_mst_msg_id, xpv_mst_xtp_id, xpv_xpn_id, xpv_type, xpv_value) values (@msg_id, @xtp_id, @xpn_id, 'DYNAMIC', 'GET_CC_ENT_ID')
	FETCH NEXT FROM msg_cursor INTO @msg_id
END
CLOSE msg_cursor
DEALLOCATE msg_cursor

/* ENROLLMENT_LATE_WITHDRAWAL_REQUEST */
declare @xtp_id int
declare @xpn_pos int
select @xtp_id = xtp_id from xslTemplate where xtp_type = 'ENROLLMENT_LATE_WITHDRAWAL_REQUEST'
select @xpn_pos = max(xpn_pos)+1 from xslParamName where xpn_xtp_id = @xtp_id

insert xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, @xpn_pos, 'app_id')

declare @xpn_id int
select @xpn_id = xpn_id from xslParamName where xpn_xtp_id = @xtp_id and xpn_pos = @xpn_pos

DECLARE msg_cursor CURSOR FOR
select xpv_mst_msg_id from mgXslParamValue where xpv_mst_xtp_id = @xtp_id group by xpv_mst_msg_id

OPEN msg_cursor
declare @msg_id int

FETCH NEXT FROM msg_cursor INTO @msg_id

WHILE @@FETCH_STATUS = 0
BEGIN
	insert mgXslParamValue (xpv_mst_msg_id, xpv_mst_xtp_id, xpv_xpn_id, xpv_type, xpv_value) values (@msg_id, @xtp_id, @xpn_id, 'STATIC', '0')
	FETCH NEXT FROM msg_cursor INTO @msg_id
END
CLOSE msg_cursor
DEALLOCATE msg_cursor

/*
Author: Stanley Lam
Desc: Set att_timestamp to null when the status is 'IN PROGRESS'
Date: 2002-10-23
*/
declare @ats_id int
select @ats_id = ats_id from aeAttendanceStatus where ats_type = 'PROGRESS'
print @ats_id
update aeAttendance set att_timestamp = null where att_ats_id = @ats_id and att_timestamp is not null

/*
Author: Kim
Desc: Fix for LDAP authentication
Date: 2002-11-01
*/
alter table acsite alter column ste_ldap_dn nvarchar (200) null

/*
Author: Tim Lo
Desc: Add displayOption for External Assessment System (EAS)
Date: 2002-10-23
*/
insert into DisplayOption (dpo_res_id,dpo_res_type,dpo_res_subtype,dpo_view,dpo_icon_ind,dpo_title_ind,dpo_lan_ind,dpo_desc_ind,dpo_instruct_ind,dpo_eff_start_datetime_ind,dpo_eff_end_datetime_ind,dpo_difficulty_ind,dpo_time_limit_ind,dpo_suggested_time_ind,dpo_duration_ind,dpo_max_score_ind,dpo_pass_score_ind,dpo_pgr_last_acc_datetime_ind,dpo_pgr_start_datetime_ind,dpo_pgr_complete_datetime_ind,dpo_pgr_attempt_nbr_ind,dpo_instructor_ind,dpo_organization_ind,dpo_moderator_ind,dpo_evt_datetime_ind,dpo_evt_venue_ind,dpo_status_ind)
values (0,'MOD','EAS','IST_EDIT',1,1,1,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,1)

insert into DisplayOption (dpo_res_id,dpo_res_type,dpo_res_subtype,dpo_view,dpo_icon_ind,dpo_title_ind,dpo_lan_ind,dpo_desc_ind,dpo_instruct_ind,dpo_eff_start_datetime_ind,dpo_eff_end_datetime_ind,dpo_difficulty_ind,dpo_time_limit_ind,dpo_suggested_time_ind,dpo_duration_ind,dpo_max_score_ind,dpo_pass_score_ind,dpo_pgr_last_acc_datetime_ind,dpo_pgr_start_datetime_ind,dpo_pgr_complete_datetime_ind,dpo_pgr_attempt_nbr_ind,dpo_instructor_ind,dpo_organization_ind,dpo_moderator_ind,dpo_evt_datetime_ind,dpo_evt_venue_ind,dpo_status_ind)
values (0,'MOD','EAS','IST_READ',1,1,1,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,1)

insert into DisplayOption (dpo_res_id,dpo_res_type,dpo_res_subtype,dpo_view,dpo_icon_ind,dpo_title_ind,dpo_lan_ind,dpo_desc_ind,dpo_instruct_ind,dpo_eff_start_datetime_ind,dpo_eff_end_datetime_ind,dpo_difficulty_ind,dpo_time_limit_ind,dpo_suggested_time_ind,dpo_duration_ind,dpo_max_score_ind,dpo_pass_score_ind,dpo_pgr_last_acc_datetime_ind,dpo_pgr_start_datetime_ind,dpo_pgr_complete_datetime_ind,dpo_pgr_attempt_nbr_ind,dpo_instructor_ind,dpo_organization_ind,dpo_moderator_ind,dpo_evt_datetime_ind,dpo_evt_venue_ind,dpo_status_ind)
values (0,'MOD','EAS','LRN_READ',1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0)

/*
Author: Dennis
Desc: New columns for RegUser for Self Registration Approval
Date: 2002-11-05
*/
alter table RegUser add usr_approve_usr_id varchar(50) NULL
alter table RegUser add usr_approve_timestamp datetime NULL
alter table RegUser add usr_approve_reason nvarchar(500) NULL
GO


/*
Author: Dennis
Desc: New acFunction entries for Self Registration Approval
Date: 2002-11-05
*/
insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values
('USR_REG_APPROVAL_BOX', 'FUNCTION', 'HOMEPAGE', getdate(), '<function id="USR_REG_APPROVAL_BOX"><desc lan="ISO-8859-1" name="User Registration Approval"/><desc lan="Big5" name="User Registration Approval"/><desc lan="GB2312" name="User Registration Approval"/></function>')



/*
Author: Dennis
Desc: New xslTemplate for user account registration approval
Date: 2002-11-05
Note: please put the correct values of HOST and PORT
*/
declare @xtp_id int

insert into xslTemplate (xtp_type, xtp_subtype, xtp_channel_type, xtp_channel_api, xtp_xsl, xtp_xml_url, xtp_mailmerge_ind, xtp_title)
values ('USR_REG_APPROVE', 'HTML', 'SMTP', NULL, 'usr_reg_approve_notes.xsl', 'http://HOST:PORT/servlet/qdbAction?', 1, NULL)

select @xtp_id = xtp_id from xslTemplate where xtp_type = 'USR_REG_APPROVE'

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 1, 'cmd')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 2, 'usr_ent_id')




/*
Author: Dennis
Desc: New xslTemplate for user account registration disapproval
Date: 2002-11-05
Note: please put the correct values of HOST and PORT
*/
declare @xtp_id int

insert into xslTemplate (xtp_type, xtp_subtype, xtp_channel_type, xtp_channel_api, xtp_xsl, xtp_xml_url, xtp_mailmerge_ind, xtp_title)
values ('USR_REG_DISAPPROVE', 'HTML', 'SMTP', NULL, 'usr_reg_disapprove_notes.xsl', 'http://HOST:PORT/servlet/qdbAction?', 1, NULL)

select @xtp_id = xtp_id from xslTemplate where xtp_type = 'USR_REG_DISAPPROVE'

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 1, 'cmd')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 2, 'usr_ent_id')



/*
Author: Dennis
Desc: New xslTemplate for user account creation
Date: 2002-11-05
Note: please put the correct values of HOST and PORT
*/
declare @xtp_id int

insert into xslTemplate (xtp_type, xtp_subtype, xtp_channel_type, xtp_channel_api, xtp_xsl, xtp_xml_url, xtp_mailmerge_ind, xtp_title)
values ('USR_CREATION', 'HTML', 'SMTP', NULL, 'usr_creation_notes.xsl', 'http://HOST:PORT/servlet/qdbAction?', 1, NULL)

select @xtp_id = xtp_id from xslTemplate where xtp_type = 'USR_CREATION'

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 1, 'cmd')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 2, 'usr_ent_id')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 3, 'sender_id')

/*
Author: Dennis
Desc: New column to store a type of planning
      original should be type LEARNING_PLAN
      new type USER_REGISTRATION responses for showing interest in items in user account registration process
Date: 2002-11-05
*/
alter table aeLearningSoln add lsn_type varchar(20)
GO
update aeLearningSoln set lsn_type = 'LEARNING_PLAN'
alter table aeLearningSoln alter column lsn_type varchar(20) NOT NULL
GO

alter table aeLearningSoln drop constraint PK_aeLearningSoln
GO
alter table aeLearningSoln add constraint PK_aeLearningSoln primary key nonclustered
 (lsn_ent_id, lsn_itm_id, lsn_ent_id_lst, lsn_type) with fillfactor = 90
GO


/*
Author: Dennis
Desc: New System Message for user account registration
Date: 2002-11-05
*/
insert into systemmessage values
('USR013', 'ISO-8859-1', 'User account is successfully approved.')
insert into systemmessage values
('USR013', 'GB2312', 'User account is successfully approved.')
insert into systemmessage values
('USR013', 'Big5', 'User account is successfully approved.')

insert into systemmessage values
('USR014', 'ISO-8859-1', 'User account is successfully disapproved.')
insert into systemmessage values
('USR014', 'GB2312', 'User account is successfully disapproved.')
insert into systemmessage values
('USR014', 'Big5', 'User account is successfully disapproved.')

/*
Author: Dennis
Desc: New System Message for user deletion checking
Date: 2002-11-05
*/
insert into systemmessage values
('USR015', 'ISO-8859-1', 'Cannot delete the user record because he/she has some subordinates in the system. To delete the user record, you need to first re-assign new supervisor(s) to the subordinates. Click the button below to find out the subordinates from his/her profile.')
insert into systemmessage values
('USR015', 'GB2312', 'Cannot delete the user record because he/she has some subordinates in the system. To delete the user record, you need to first re-assign new supervisor(s) to the subordinates. Click the button below to find out the subordinates from his/her profile.')
insert into systemmessage values
('USR015', 'Big5', 'Cannot delete the user record because he/she has some subordinates in the system. To delete the user record, you need to first re-assign new supervisor(s) to the subordinates. Click the button below to find out the subordinates from his/her profile.')


/*
Author: Dennis
Desc: New target_ent_type column to store the target entity of the approver
      4 possible values for rol_target_ent_type:
      NULL - the role is not an "APPROVER"
      'USR' - the role is an "APPROVER" and should be assigned to user only
      'USG' - the role is an "APPROVER" and should be assigned to user group only
      'USR_OR_USG' - the role is an "APPROVER" and can be assigned to both user and user group.
Date: 2002-11-05
*/
alter table acRole add rol_target_ent_type varchar(50) NULL
GO
update acRole set rol_target_ent_type = 'USG' where rol_target_ent_ind = 1
GO
alter table acRole drop column rol_target_ent_ind
GO

/*
Author: Dennis
Desc: Set site default role for Batch User Upload
Date: 2002-11-06
*/
update acRole set rol_ste_uid = '01'
where rol_ste_default_ind = 1

/*
Author: Dennis
Desc: Set Guset Role for Self Registration
	This is only an example. Please update the correct data before executing it
Date: 2002-11-06
*/
insert into acRole (rol_ext_id, rol_seq_id, rol_ste_ent_id, rol_url_home, rol_creation_timestamp, rol_xml, rol_ste_default_ind, rol_report_ind, rol_skin_root, rol_status, rol_ste_uid, rol_target_ent_type)
values ('VISTR_1', 7, 1, 'jsp/home.jsp', getdate(), '<role id="VISTR_1"><desc lan="ISO-8859-1" name="Visitor"/><desc lan="Big5" name="Visitor"/><desc lan="GB2312" name="Visitor"/></role>', 0, 0, 'skin2', 'HIDDEN', NULL, NULL)

/*
Author: Dennis
Desc: Create a Guest Role User for Self Registration
Date: 2002-11-06
Note: Please retrieve the value of the guest_rol_id and ste_ent_id from DB and put them to the below scripts
*/
declare @ste_ent_id int
declare @guest_rol_id int
declare @usr_ent_id int

set @guest_rol_id = ???
set @ste_ent_id = ???

insert into Entity (ent_type, ent_upd_date, ent_syn_date, ent_ste_uid, ent_syn_ind, ent_syn_rol_ind)
values ('USR', getdate(), NULL, NULL, 1, 1)
select @usr_ent_id = max(ent_id) from Entity

insert into regUser (usr_id, usr_ent_id, usr_display_bil, usr_signup_date, usr_last_login_date, usr_status, usr_upd_date, usr_ste_ent_id, usr_ste_usr_id)
values
('s'+cast(@ste_ent_id as varchar)+'u'+cast(@usr_ent_id as varchar), @usr_ent_id, 'Guest', getdate(), getdate(), 'SYS', getdate(), @ste_ent_id, 'guest')

insert into acEntityRole values (@usr_ent_id, @guest_rol_id, getdate(), null, '1753-01-01 00:00:00.000', '9999-12-31 23:59:59.000')

insert into GroupMember values (@ste_ent_id, @usr_ent_id, 'USR_PARENT_USG', ' '+cast(@ste_ent_id as varchar)+' ', NULL, 1)

update acSite set ste_guest_ent_id = @usr_ent_id

/*
Author: Kim
Desc: Modification in completion criteria
Date: 2002-11-06
*/

alter table aeitem add itm_content_eff_start_datetime DATETIME NULL
alter table aeitem add itm_content_eff_end_datetime DATETIME NULL
GO
update aeItem set itm_content_eff_start_datetime = (select cos_eff_start_datetime from course where cos_itm_id = itm_id ) where itm_id in (select cos_itm_id from course)
update aeItem set itm_content_eff_end_datetime = (select cos_eff_end_datetime from course where cos_itm_id = itm_id ) where itm_id in (select cos_itm_id from course)

update aeItem set itm_content_eff_start_datetime = (select cos_eff_start_datetime from course , aeitemrelation where ire_parent_itm_id = cos_itm_id and ire_child_itm_id = itm_id ) where itm_run_ind = 1 and itm_id in (select ire_child_itm_id from course , aeitemrelation where ire_parent_itm_id = cos_itm_id )
update aeItem set itm_content_eff_end_datetime = (select cos_eff_end_datetime from course , aeitemrelation where ire_parent_itm_id = cos_itm_id and ire_child_itm_id = itm_id ) where itm_run_ind = 1 and itm_id in (select ire_child_itm_id from course , aeitemrelation where ire_parent_itm_id = cos_itm_id )
GO

alter table aeitem add itm_content_eff_duration int null
GO
update aeItem set itm_content_eff_duration = (select ccr_duration from course, courseCriteria where cos_itm_id = itm_id and ccr_cos_id = cos_res_id ) where itm_content_eff_start_datetime is null and itm_content_eff_end_datetime is null
GO

alter table CourseCriteria add ccr_upd_method VARCHAR (20) NULL
GO
alter table CourseCriteria add ccr_itm_id int null
GO
update CourseCriteria set ccr_itm_id = (select cos_itm_id from course where cos_res_id = ccr_cos_id)
alter table CourseCriteria alter column ccr_itm_id int not null
alter table CourseCriteria drop constraint FK_CourseCriteria_Course
GO
alter table CourseCriteria drop column ccr_cos_id
ALTER TABLE [dbo].[CourseCriteria] ADD CONSTRAINT [FK_CourseCriteria_aeItem] FOREIGN KEY ([ccr_itm_id]) REFERENCES [dbo].[aeItem] ([itm_id])
GO

update CourseCriteria set ccr_upd_method = 'MANUAL' where ccr_duration = 0 and ccr_itm_id in (select itm_id from aeitem where itm_content_eff_start_datetime is null)

update CourseCriteria set ccr_upd_method = 'AUTO' where  ccr_upd_method is null

alter table CourseCriteria alter column ccr_upd_method VARCHAR (20) not NULL

alter table CourseCriteria drop column ccr_relation_xml
alter table CourseCriteria drop constraint DF_CourseCriteria_ccr_max_score
GO

alter table CourseCriteria drop column ccr_max_score
alter table CourseCriteria add ccr_attendance_rate int null

/*
Author: Stanley
Desc: Modification in completion criteria
Date: 2002-11-07
*/
Please modify the DETAIL_VIEW of the table 'aeTemplateView' to remove the 'online content start date', 'online content end date', 'Online Content' button, 'Completion Criteria' button, 'Tracking Report' button, 'Announcement' if exists

/*
Author : Stanley
Desc : Update module
Date : 2002-11-08
IMPORTANT NOTE: Error may occur when you execute the following script since there is a known bug in MSSQL 7.0.  Please refer to http://support.microsoft.com/default.aspx?scid=kb;en-us;Q290992 for more information.
*/
alter table Module alter column mod_instruct nvarchar(4000) NULL

/*
Author : Tim Lo
Desc : Create an indicator into aeItem, aeItemType that indicates whether it can be cancel or not
Date : 2002-11-08
*/

ALTER TABLE aeItem ADD itm_can_cancel_ind bit NULL;
GO
UPDATE aeItem set itm_can_cancel_ind= 0 where itm_run_ind = 0 and itm_session_ind = 0;
UPDATE aeItem set itm_can_cancel_ind= 1 where itm_run_ind = 1 or itm_session_ind = 1;

ALTER TABLE aeItemType ADD ity_can_cancel_ind bit NULL;
GO
UPDATE aeItemType set ity_can_cancel_ind = 0 where ity_run_ind = 0 and ity_session_ind = 0;
UPDATE aeItemType set ity_can_cancel_ind = 1 where ity_run_ind = 1 or ity_session_ind = 1;

/*
Author: Emily
Desc:   Update the gb display function name in acFunction
Date:   2002-11-13
if need this change, please use xml file to do it
*/
ftn_ext_id    ftn_xml
COS_MAIN   <function id="COS_MAIN">	<desc lan="ISO-8859-1" name="Course Administration"/>	<desc lan="Big5" name="Course Administration"/>	<desc lan="GB2312" name="璜哄€ワ骏a?蹇戝?/></function>
ITM_MAIN    <function id="ITM_MAIN">	<desc lan="ISO-8859-1" name="Maintain Learning Solution"/>	<desc lan="Big5" name="Maintain Learning Solution"/>	<desc lan="GB2312" name="璜哄€ワ骏a?/></function>

/*
Author: Stanley
Desc: 	Update big5 and gb systemmessage and acFunction
	Details please refer to the [WIZBANK UPDATES] email
Date:	2002-11-14
*/

/*
Author: Chris
Desc: 	Added evaluation module to wizBank. Evaluation behave like survey except that it is not related to any course
Remarks: Update the Big5 and GB name in the function xml
Date:	2002-11-20
*/
insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('EVN_MAIN', 'FUNCTION','HOMEPAGE',getdate(), '<function id="EVN_MAIN"><desc lan="ISO-8859-1" name="Maintain Evaluation"/><desc lan="Big5" name="Maintain Evaluation"/><desc lan="GB2312" name="Maintain Evaluation"/></function>');
insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('EVN_LIST', 'FUNCTION','HOMEPAGE',getdate(), '<function id="EVN_LIST"><desc lan="ISO-8859-1" name="Evaluation List"/><desc lan="Big5" name="Evaluation List"/><desc lan="GB2312" name="Evaluation List"/></function>');

declare @adm_id  int
declare @lrn_id int
declare @eval_main_id int
declare @eval_list_id int

select @adm_id = rol_id from acRole where rol_ext_id like 'ADM_1';
select @lrn_id = rol_id from acRole where rol_ext_id like 'NLRN_1';

select @eval_main_id = ftn_id from acFunction where ftn_ext_id = 'EVN_MAIN'
select @eval_list_id = ftn_id from acFunction where ftn_ext_id = 'EVN_LIST'

insert into acRoleFunction values (@adm_id, @eval_main_id, getdate())
insert into acRoleFunction values (@lrn_id, @eval_list_id, getdate())

insert into acHomePage values (null, 'ADM_1','EVN_MAIN', 's1u3', getdate())
insert into acHomePage values (null, 'NLRN_1','EVN_LIST', 's1u3', getdate())

delete from DisplayOption where dpo_res_subtype='EVN'

insert into DisplayOption values (0,'MOD','EVN','IST_EDIT',1,1,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1)
insert into DisplayOption values (0,'MOD','EVN','LRN_READ',1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
insert into DisplayOption values (0,'MOD','EVN','IST_READ',1,1,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1)

/*
Author: Tim
Desc: 	Create a table aeItemMessage for new JI
Date:	2002-11-22
*/

if exists (select * from sysobjects where id = object_id(N'[dbo].[aeItemMessage]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [dbo].[aeItemMessage]
GO

CREATE TABLE [dbo].[aeItemMessage] (
	[img_itm_id] [int] NOT NULL ,
	[img_msg_id] [int] NOT NULL ,
	[img_type] [varchar] (20) NOT NULL ,
	[img_create_usr_id] [varchar] (20) NOT NULL ,
	[img_create_timestamp] [datetime] NOT NULL ,
	[img_update_usr_id] [varchar] (20) NOT NULL ,
	[img_update_timestamp] [datetime] NOT NULL
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[aeItemMessage] WITH NOCHECK ADD
	CONSTRAINT [PK_aeItemMessage] PRIMARY KEY  NONCLUSTERED
	(
		[img_itm_id],
		[img_msg_id]
	)  ON [PRIMARY]
GO

ALTER TABLE [dbo].[aeItemMessage] ADD
	CONSTRAINT [FK_aeItemMessage_aeItem] FOREIGN KEY
	(
		[img_itm_id]
	) REFERENCES [dbo].[aeItem] (
		[itm_id]
	),
	CONSTRAINT [FK_aeItemMessage_mgMessage] FOREIGN KEY
	(
		[img_msg_id]
	) REFERENCES [dbo].[mgMessage] (
		[msg_id]
	)
GO


/*
Author: Tim
Desc: 	Add param value for JI and reminder in xslParamName
Date:	2002-11-22
Note: If you want to make the JI work on the existing client with 'Confirmed' application, two things you have to do:
1) a batch program needed to be run
2) update the workflow template
*/

declare @xtp_id int
declare @xpn_pos int

select @xtp_id = xtp_id from xslTemplate where xtp_type = 'JI'
select @xpn_pos = max(xpn_pos)+1 from xslParamName where xpn_xtp_id = @xtp_id

insert xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, @xpn_pos, 'cc_to_approver_ind')

select @xtp_id = xtp_id from xslTemplate where xtp_type = 'JI'
select @xpn_pos = max(xpn_pos)+1 from xslParamName where xpn_xtp_id = @xtp_id

insert xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, @xpn_pos, 'cc_to_approver_ex_id')

select @xtp_id = xtp_id from xslTemplate where xtp_type = 'JI'
select @xpn_pos = max(xpn_pos)+1 from xslParamName where xpn_xtp_id = @xtp_id

insert xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, @xpn_pos, 'bcc_ent_ids')

select @xtp_id = xtp_id from xslTemplate where xtp_type = 'REMINDER'
select @xpn_pos = max(xpn_pos)+1 from xslParamName where xpn_xtp_id = @xtp_id

insert xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, @xpn_pos, 'cc_to_approver_ind')

select @xtp_id = xtp_id from xslTemplate where xtp_type = 'REMINDER'
select @xpn_pos = max(xpn_pos)+1 from xslParamName where xpn_xtp_id = @xtp_id

insert xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, @xpn_pos, 'cc_to_approver_ex_id')

select @xtp_id = xtp_id from xslTemplate where xtp_type = 'REMINDER'
select @xpn_pos = max(xpn_pos)+1 from xslParamName where xpn_xtp_id = @xtp_id

insert xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, @xpn_pos, 'bcc_ent_ids')

/*
Author: Tim
Desc: 	Change the colume 'mgh_status' to accept null
Date:	2002-11-22
*/

alter table mgRecHistory alter column mgh_Status varchar(20) NULL


/*
Author: Tim
Desc: Update the mailmerge_ind for JI and REMINDER types
Date:	2002-11-22
*/

update dbo.xslTemplate set xtp_mailmerge_ind = 0 where xtp_type in ('JI', 'REMINDER')


/*
Author: Tim
Desc: 	Add a column  ste_appr_staff_role in acSite
Date:	2002-11-22
*/

BEGIN TRANSACTION
ALTER TABLE dbo.acSite ADD
	ste_appr_staff_role nvarchar(255) NULL
GO
COMMIT

/*
Author: Tim
Desc: 	Add a new function 'My Staff' into acFucntion, acRoleFunction, acHomepage
Date:	2002-11-22
Note:   Please use the updxml batch for the label 'My Staff'
*/

insert acFunction values ('STAFF_INFO', 'FUNCTION', 'HOMEPAGE', getDate(), '<function id="STAFF_INFO">	<desc lan="ISO-8859-1" name="My Staff"/>	<desc lan="Big5" name="?????/>	<desc lan="GB2312" name="????"/></function>')

declare @ftn_id int
select @ftn_id = ftn_id from acFunction where ftn_ext_id = 'STAFF_INFO'

DECLARE rol_cursor CURSOR FOR
SELECT rol_id FROM acRole WHERE rol_target_ent_type IS NOT NULL
OPEN rol_cursor
declare @rol_id int

FETCH NEXT FROM rol_cursor INTO @rol_id

WHILE @@FETCH_STATUS = 0
BEGIN
	insert acRoleFunction values (@rol_id, @ftn_id, getDate())
	FETCH NEXT FROM rol_cursor INTO @rol_id
END
CLOSE rol_cursor
DEALLOCATE rol_cursor

/* If show this function in homepage, insert this column*/
--insert acHomepage values (null, XXX', 'STAFF_INFO', 's1u3', getDate())

/*
Author: Dennis  Yip
Desc: 	New Table to store the specific attribute for different assessment unit type
Date:	2002-11-22
*/
create table cmAsmUnitTypeAttr (
	aua_asm_id int NOT NULL,
	aua_asu_type varchar(20) NOT NULL,
	aua_eff_start_timestamp datetime NOT NULL,
	aua_eff_end_timestamp datetime NOT NULL
)
GO

alter table cmAsmUnitTypeAttr with nocheck add constraint PK_cmAsmUnitTypeAttr
primary key clustered (
	aua_asm_id,
	aua_asu_type
)
GO

alter table cmAsmUnitTypeAttr add constraint FK_cmAsmUnitTypeAttr_cmAssessment
foreign key (aua_asm_id) references cmAssessment (asm_id)
GO

insert  into  cmAsmUnitTypeAttr
select distinct asu_asm_id, asu_type, asm_eff_start_datetime, asm_eff_end_datetime
from  cmAssessment, cmAssessmentUnit
where asm_id = asu_asm_id


/*
Author: Dennis  Yip
Desc: 	New column added  to  feature  different  email  messages  for  different  assessment  unit  type
Date:	2002-11-22
*/
alter table cmAssessmentNotify add asn_asu_type varchar(20) NULL
GO
Update  cmAssessmentNotify  set asn_asu_type = 'RESOLVED'
alter table cmAssessmentNotify alter column asn_asu_type varchar(20) NOT NULL


/*
Author: Dennis  Yip
Desc: 	Change existing assessment message templates to 'RESOLVED' assessment unit
Date:	2002-11-22
*/
update xslTemplate set xtp_type = 'ASSESSMENT_NOTIFICATION_RESOLVED'
where xtp_type = 'ASSESSMENT_NOTIFICATION'

update xslTemplate set xtp_type = 'ASSESSMENT_COLLECTION_NOTIFICATION_RESOLVED'
where xtp_type = 'ASSESSMENT_COLLECTION_NOTIFICATION'



/*
Author: Dennis  Yip
Desc: 	New  xslTemplate  for  CLIENTS assessment  notification
Date:	2002-11-22
*/
declare @xtp_id int

insert into xslTemplate (xtp_type, xtp_subtype, xtp_channel_type, xtp_channel_api, xtp_xsl, xtp_xml_url, xtp_mailmerge_ind, xtp_title)
values ('ASSESSMENT_NOTIFICATION_CLIENTS', 'HTML', 'SMTP', NULL, 'assessment_notification_clients.xsl', 'http://host:port/servlet/Dispatcher?module=competency.CompetencyModule&', 1, NULL)

select @xtp_id = xtp_id from xslTemplate where xtp_type = 'ASSESSMENT_NOTIFICATION_CLIENTS'

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 1, 'ent_ids')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 2, 'sender_id')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 3, 'cmd')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 4, 'asm_id')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 5, 'label_lan')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 6, 'site_id')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 7, 'style')



/*
Author: Dennis  Yip
Desc: 	New  xslTemplate  for  CLIENTS assessment  collection notification
Date:	2002-11-22
*/
declare @xtp_id int

insert into xslTemplate (xtp_type, xtp_subtype, xtp_channel_type, xtp_channel_api, xtp_xsl, xtp_xml_url, xtp_mailmerge_ind, xtp_title)
values ('ASSESSMENT_COLLECTION_NOTIFICATION_CLIENTS', 'HTML', 'SMTP', NULL, 'assessment_collection_notification_clients.xsl', 'http://host:port/servlet/Dispatcher?module=competency.CompetencyModule&', 1, NULL)

select @xtp_id = xtp_id from xslTemplate where xtp_type = 'ASSESSMENT_COLLECTION_NOTIFICATION_CLIENTS'

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 1, 'ent_ids')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 2, 'sender_id')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 3, 'cmd')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 4, 'asm_id')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 5, 'label_lan')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 6, 'site_id')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 7, 'style')




/*
Author: Dennis  Yip
Desc: 	New  xslTemplate  for  REPORTS assessment  notification
Date:	2002-11-22
*/
declare @xtp_id int

insert into xslTemplate (xtp_type, xtp_subtype, xtp_channel_type, xtp_channel_api, xtp_xsl, xtp_xml_url, xtp_mailmerge_ind, xtp_title)
values ('ASSESSMENT_NOTIFICATION_REPORTS', 'HTML', 'SMTP', NULL, 'assessment_notification_reports.xsl', 'http://host:port/servlet/Dispatcher?module=competency.CompetencyModule&', 1, NULL)

select @xtp_id = xtp_id from xslTemplate where xtp_type = 'ASSESSMENT_NOTIFICATION_REPORTS'

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 1, 'ent_ids')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 2, 'sender_id')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 3, 'cmd')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 4, 'asm_id')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 5, 'label_lan')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 6, 'site_id')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 7, 'style')



/*
Author: Dennis  Yip
Desc: 	New  xslTemplate  for  REPORTS  assessment  collection notification
Date:	2002-11-22
*/
declare @xtp_id int

insert into xslTemplate (xtp_type, xtp_subtype, xtp_channel_type, xtp_channel_api, xtp_xsl, xtp_xml_url, xtp_mailmerge_ind, xtp_title)
values ('ASSESSMENT_COLLECTION_NOTIFICATION_REPORTS', 'HTML', 'SMTP', NULL, 'assessment_collection_notification_reports.xsl', 'http://host:port/servlet/Dispatcher?module=competency.CompetencyModule&', 1, NULL)

select @xtp_id = xtp_id from xslTemplate where xtp_type = 'ASSESSMENT_COLLECTION_NOTIFICATION_REPORTS'

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 1, 'ent_ids')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 2, 'sender_id')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 3, 'cmd')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 4, 'asm_id')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 5, 'label_lan')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 6, 'site_id')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 7, 'style')


/*
Author: Dennis  Yip
Desc: 	New  xslTemplate  for  MANAGEMENT assessment  notification
Date:	2002-11-22
*/
declare @xtp_id int

insert into xslTemplate (xtp_type, xtp_subtype, xtp_channel_type, xtp_channel_api, xtp_xsl, xtp_xml_url, xtp_mailmerge_ind, xtp_title)
values ('ASSESSMENT_NOTIFICATION_MGMT', 'HTML', 'SMTP', NULL, 'assessment_notification_mgmt.xsl', 'http://host:port/servlet/Dispatcher?module=competency.CompetencyModule&', 1, NULL)

select @xtp_id = xtp_id from xslTemplate where xtp_type = 'ASSESSMENT_NOTIFICATION_MGMT'

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 1, 'ent_ids')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 2, 'sender_id')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 3, 'cmd')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 4, 'asm_id')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 5, 'label_lan')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 6, 'site_id')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 7, 'style')



/*
Author: Dennis  Yip
Desc: 	New  xslTemplate  for  MANAGEMENT assessment  collection notification
Date:	2002-11-22
*/
declare @xtp_id int

insert into xslTemplate (xtp_type, xtp_subtype, xtp_channel_type, xtp_channel_api, xtp_xsl, xtp_xml_url, xtp_mailmerge_ind, xtp_title)
values ('ASSESSMENT_COLLECTION_NOTIFICATION_MGMT', 'HTML', 'SMTP', NULL, 'assessment_collection_notification_mgmt.xsl', 'http://host:port/servlet/Dispatcher?module=competency.CompetencyModule&', 1, NULL)

select @xtp_id = xtp_id from xslTemplate where xtp_type = 'ASSESSMENT_COLLECTION_NOTIFICATION_MGMT'

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 1, 'ent_ids')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 2, 'sender_id')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 3, 'cmd')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 4, 'asm_id')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 5, 'label_lan')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 6, 'site_id')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 7, 'style')



/*
Author: Dennis  Yip
Desc: 	New  xslTemplate  for  PEERS assessment  notification
Date:	2002-11-22
*/
declare @xtp_id int

insert into xslTemplate (xtp_type, xtp_subtype, xtp_channel_type, xtp_channel_api, xtp_xsl, xtp_xml_url, xtp_mailmerge_ind, xtp_title)
values ('ASSESSMENT_NOTIFICATION_PEERS', 'HTML', 'SMTP', NULL, 'assessment_notification_peers.xsl', 'http://host:port/servlet/Dispatcher?module=competency.CompetencyModule&', 1, NULL)

select @xtp_id = xtp_id from xslTemplate where xtp_type = 'ASSESSMENT_NOTIFICATION_PEERS'

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 1, 'ent_ids')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 2, 'sender_id')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 3, 'cmd')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 4, 'asm_id')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 5, 'label_lan')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 6, 'site_id')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 7, 'style')



/*
Author: Dennis  Yip
Desc: 	New  xslTemplate  for  PEERS assessment  collection notification
Date:	2002-11-22
*/
declare @xtp_id int

insert into xslTemplate (xtp_type, xtp_subtype, xtp_channel_type, xtp_channel_api, xtp_xsl, xtp_xml_url, xtp_mailmerge_ind, xtp_title)
values ('ASSESSMENT_COLLECTION_NOTIFICATION_PEERS', 'HTML', 'SMTP', NULL, 'assessment_collection_notification_peers.xsl', 'http://host:port/servlet/Dispatcher?module=competency.CompetencyModule&', 1, NULL)

select @xtp_id = xtp_id from xslTemplate where xtp_type = 'ASSESSMENT_COLLECTION_NOTIFICATION_PEERS'

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 1, 'ent_ids')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 2, 'sender_id')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 3, 'cmd')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 4, 'asm_id')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 5, 'label_lan')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 6, 'site_id')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 7, 'style')



/*
Author: Dennis  Yip
Desc: 	New  xslTemplate  for  SELF assessment  notification
Date:	2002-11-22
*/
declare @xtp_id int

insert into xslTemplate (xtp_type, xtp_subtype, xtp_channel_type, xtp_channel_api, xtp_xsl, xtp_xml_url, xtp_mailmerge_ind, xtp_title)
values ('ASSESSMENT_NOTIFICATION_SELF', 'HTML', 'SMTP', NULL, 'assessment_notification_self.xsl', 'http://host:port/servlet/Dispatcher?module=competency.CompetencyModule&', 1, NULL)

select @xtp_id = xtp_id from xslTemplate where xtp_type = 'ASSESSMENT_NOTIFICATION_SELF'

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 1, 'ent_ids')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 2, 'sender_id')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 3, 'cmd')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 4, 'asm_id')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 5, 'label_lan')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 6, 'site_id')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 7, 'style')



/*
Author: Dennis  Yip
Desc: 	New  xslTemplate  for  SELF assessment  notification
Date:	2002-11-22
*/
declare @xtp_id int

insert into xslTemplate (xtp_type, xtp_subtype, xtp_channel_type, xtp_channel_api, xtp_xsl, xtp_xml_url, xtp_mailmerge_ind, xtp_title)
values ('ASSESSMENT_COLLECTION_NOTIFICATION_SELF', 'HTML', 'SMTP', NULL, 'assessment_collection_notification_self.xsl', 'http://host:port/servlet/Dispatcher?module=competency.CompetencyModule&', 1, NULL)

select @xtp_id = xtp_id from xslTemplate where xtp_type = 'ASSESSMENT_COLLECTION_NOTIFICATION_SELF'

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 1, 'ent_ids')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 2, 'sender_id')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 3, 'cmd')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 4, 'asm_id')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 5, 'label_lan')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 6, 'site_id')

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 7, 'style')



/*
Author: Emily
Desc: 	add "app_id" parameter for xslTemplate "ENROLLMENT_NEW" and "ENROLLEMENT_CONFIRMED"
Date:	2002-11-25
*/
DECLARE @xtp_id INT
DECLARE @xpn_pos INT
SELECT @xtp_id = xtp_id FROM xslTemplate WHERE xtp_type = 'ENROLLMENT_NEW'
SELECT @xpn_pos = MAX(xpn_pos) FROM xslParamName WHERE xpn_xtp_id = @xtp_id

-- ENROLLMENT_NEW
INSERT xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
VALUES (@xtp_id, (@xpn_pos+1), 'app_id')

-- ENROLLMENT_CONFIRMED
SELECT @xtp_id = xtp_id FROM xslTemplate WHERE xtp_type = 'ENROLLMENT_CONFIRMED'
SELECT @xpn_pos = MAX(xpn_pos) FROM xslParamName WHERE xpn_xtp_id = @xtp_id

INSERT xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
VALUES (@xtp_id, (@xpn_pos+1), 'app_id')

/*
Author: Fai
Desc: New System Message for user password update
Date: 2002-11-26
*/
insert into systemmessage values
('USR016', 'ISO-8859-1', 'User password is successfully updated.')
insert into systemmessage values
('USR016', 'Big5', 'User password is successfully updated.')
insert into systemmessage values
('USR016', 'GB2312', 'User password is successfully updated.')

insert into systemmessage values
('USR017', 'ISO-8859-1', 'Old password is incorrect.')
insert into systemmessage values
('USR017', 'Big5', 'Old password is incorrect.')
insert into systemmessage values
('USR017', 'GB2312', 'Old password is incorrect.')

/*
Author: Chris
Desc: RoleFunction for Change Password
Date: 2002-11-28
*/

insert into acFunction (ftn_ext_id,ftn_level,ftn_type,ftn_creation_timestamp,ftn_xml)
values ('USR_PWD_UPD','FUNCTION','HOMEPAGE',getdate(),'<function id="USR_PWD_UPD"> <desc lan="ISO-8859-1" name="Change Password"/> <desc lan="Big5" name="Change Password"/> <desc lan="GB2312" name="Change Password"/></function>')

declare @ftn_id int
select @ftn_id = ftn_id from acFunction where ftn_ext_id='USR_PWD_UPD'

insert into acRoleFunction select rol_id, @ftn_id, getdate() from acRole
insert into acHomePage select null, rol_ext_id, 'USR_PWD_UPD', 's1u3', getdate() from acRole

/*
Author: Chris
Desc: Password expiry and Change password at next logon
Date: 2002-11-28
*/

ALTER TABLE dbo.acSite ADD ste_usr_pwd_valid_period int NULL
GO
ALTER TABLE dbo.RegUser ADD usr_pwd_need_change_ind int NULL
GO
ALTER TABLE dbo.RegUser ADD usr_pwd_upd_timestamp datetime NULL
GO


/*
Author: Tim
Desc: 	Update the module effective start and end date which are null
Date:	2002-12-03
*/

UPDATE module SET mod_eff_start_datetime = getdate()
where mod_eff_start_datetime is null

UPDATE module SET mod_eff_end_datetime = '12/31/9999 11:59:59 PM'
where mod_eff_end_datetime is null

/*
Author:	Clifford
Desc: 	PRUU to CORE roll-in
Date:	2002-12-03
*/
ALTER TABLE UserGroup ADD usg_desc nvarchar(500) NULL
GO

update SystemMessage set sms_desc = 'Your enrollment has been received.' where sms_id='AEQM01' and sms_lan='ISO-8859-1'
update systemmessage set sms_desc = 'Enrollment date has not started yet.' where sms_id='AEQM03' and sms_lan='ISO-8859-1'
update systemmessage set sms_desc = 'Enrollment date has passed.' where sms_id='AEQM04' and sms_lan='ISO-8859-1'
update systemmessage set sms_desc = 'Your enrollment is successfully submitted. You will be notified for any progress.' where sms_id='AEQM08' and sms_lan='ISO-8859-1'
update systemmessage set sms_desc = 'Course cannot be updated due to at least one of the following reasons:<p>&nbsp;&nbsp;1. At least one run of the course is in progress<p>&nbsp;&nbsp;2. Enrollment of the course/run is in progress<p>&nbsp;&nbsp;3. Attendance record has not been updated after the course/run is completed<p>&nbsp;&nbsp;4. Incomplete MOTE report<p>Please check and ensure that all of the above issues are properly dealt with prior to discontinue the course.' where sms_id='AEIT14' and sms_lan='ISO-8859-1'

update SystemMessage set sms_desc='User registration is successfully approved.' where sms_id='USR013' and sms_lan='ISO-8859-1'
update SystemMessage set sms_desc='User registration is successfully declined.' where sms_id='USR014' and sms_lan='ISO-8859-1'

/*
Author:	Clifford
Desc: 	System message for assignment submission when there is error in the SMTP server setting
Date:	2002-12-04
*/
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('ASN001', 'ISO-8859-1','The file(s) are successfully submitted. However, the notification email is not sent because of improper mail server settings. Contact your system administrator if the problem persists.')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('ASN001', 'Big5','The file(s) are successfully submitted. However, the notification email is not sent because of improper mail server settings. Contact your system administrator if the problem persists.')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('ASN001', 'GB2312','The file(s) are successfully submitted. However, the notification email is not sent because of improper mail server settings. Contact your system administrator if the problem persists.')


/*
Author: Tim
Desc: 	Add new function 'ENR_APP_COS_LINK' into database
Date:	2002-12-09
*/
insert acFunction values ('ENR_APP_COS_LINK', 'FUNCTION', 'HOMEPAGE', getDate(), '<function id="ENR_APP_COS_LINK"><desc lan="ISO-8859-1" name="Course Enrollment Approval"/><desc lan="Big5" name="瑾插娇卤???/><desc lan="GB2312" name="璇惧娇锟????/></function>')

declare @ftn_id int
select @ftn_id = ftn_id from acFunction where ftn_ext_id = 'ENR_APP_COS_LINK'

DECLARE role_cursor CURSOR FOR
select rol_id from acRole where rol_target_ent_type is not null

OPEN role_cursor
declare @rol_id int

FETCH NEXT FROM role_cursor INTO @rol_id

WHILE @@FETCH_STATUS = 0
BEGIN
 insert acRoleFunction values (@rol_id, @ftn_id, getDate())
 FETCH NEXT FROM role_cursor INTO @rol_id
END
CLOSE role_cursor
DEALLOCATE role_cursor

/* insert acHomepage if needed */
insert acHomepage values (null, APPROVER_ROLE_ID, 'ENR_APP_COS_LINK', 's1u3', getDate())

/*
Author: Tim
Desc: 	Add a column app_priority in aeApplication
Date:	2002-12-09
*/

ALTER TABLE dbo.aeApplication ADD
	app_priority nvarchar(20) NULL


/*
Author : Wai Lun
Desc: Add table for storing the target application id for the approver
Date: 2002-12-10
*/
CREATE TABLE [dbo].[aeAppnTargetEntity] (
 [ate_app_id] [int] NOT NULL ,
 [ate_usr_ent_id] [int] NOT NULL ,
 [ate_rol_ext_id] [nvarchar] (255) NOT NULL ,
 [ate_create_timestamp] [datetime] NOT NULL ,
 [ate_create_usr_id] [varchar] (50) NOT NULL
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[aeAppnTargetEntity] WITH NOCHECK ADD
 CONSTRAINT [PK_aeAppnManager] PRIMARY KEY  CLUSTERED
 (
  [ate_app_id],
  [ate_usr_ent_id],
  [ate_rol_ext_id]
 )  ON [PRIMARY]
GO

ALTER TABLE [dbo].[aeAppnTargetEntity] ADD
 CONSTRAINT [FK_aeAppnManager_acRole] FOREIGN KEY
 (
  [ate_rol_ext_id]
 ) REFERENCES [dbo].[acRole] (
  [rol_ext_id]
 ),
 CONSTRAINT [FK_aeAppnManager_aeApplication] FOREIGN KEY
 (
  [ate_app_id]
 ) REFERENCES [dbo].[aeApplication] (
  [app_id]
 ),
 CONSTRAINT [FK_aeAppnManager_RegUser] FOREIGN KEY
 (
  [ate_usr_ent_id]
 ) REFERENCES [dbo].[RegUser] (
  [usr_ent_id]
 )
GO


/*
Author:	Kim
Desc: 	Add Course Evaluation Report
Date:	2002-12-17
*/
/* add if needed */
/*
insert into reportTemplate values ('<title><desc lan="ISO-8859-1" name="Evaluation Submission Report"/><desc lan="Big5" name="Evaluation Submission Report (BIG5)"/><desc lan="GB2312" name="璋冩??????/></title>' , 'SURVEY_IND', null, null, null, null,  '5', 1, 's1u3', getDate(), 's1u3', getDate())
insert into reportTemplate values ('<title><desc lan="ISO-8859-1" name="Course Evaluation Report"/><desc lan="Big5" name="Course Evaluation Report (BIG5)"/><desc lan="GB2312" name="璇炬€?????/></title>' , 'SURVEY_COS_GRP', 'rpt_svy_cos_srh.xsl', 'rpt_svy_cos_grp_res.xsl', 'rpt_dl_survey.xsl', null,  '4', 1, 's1u3', getDate(), 's1u3', getDate())

INSERT INTO objectview
SELECT 1, 'SURVEY_COURSE_REPORT', 'ITM', ojv_option_xml, 's1u3', getDate(), 's1u3', getDate() FROM objectview WHERE ojv_type = 'COURSE_REPORT' and ojv_subtype = 'ITM'
INSERT INTO objectview
SELECT 1, 'SURVEY_COURSE_REPORT', 'RUN', ojv_option_xml, 's1u3', getDate(), 's1u3', getDate() FROM objectview WHERE ojv_type = 'COURSE_REPORT' and ojv_subtype = 'RUN'
INSERT INTO objectview
SELECT 1, 'SURVEY_COURSE_REPORT', 'OTHER', '<object_view><attribute>question</attribute><attribute>overall</attribute></object_view>', 's1u3', getDate(), 's1u3', getDate() FROM objectview WHERE ojv_type = 'COURSE_REPORT' and ojv_subtype = 'OTHER'

insert into acReportTemplate
select rte_id, null, 'TADM_1','RTE_READ', 0, 's1u3', getDate()   from reporttemplate where rte_type = 'SURVEY_COS_GRP'
*/

insert into systemmessage values ('MOD006', 'ISO-8859-1', 'This Course Evaluation Template has been used in this course.')
insert into systemmessage values ('MOD006', 'Big5', 'This Course Evaluation Template has been used in this course.')
insert into systemmessage values ('MOD006', 'GB2312', '姝よ绋嬪凡缁忎?篓姝よ鎬???鍗锋ā?)

ALTER TABLE module add mod_mod_id_root int null

 /*
Author:	Tim
Desc: 	Add coluum in aeItemType and aeItemTargetEntity
Date:	2002-12-19
*/

ALTER TABLE aeItemType ADD
	 ity_target_method nvarchar(30) NULL

ALTER TABLE aeItemTargetEntity ADD
	ite_apply_method nvarchar(20) NULL
GO
update aeItemType set ity_target_method = 'ELECTIVE' where ity_target_method is null
update aeItemTargetEntity set ite_apply_method = 'ELECTIVE' where ite_apply_method is null

/*
Author: Wai Lun
Desc: Drop ict_code in aeItemCredit and add Column icv_code into aeItemCreditValue
Add ict_xml into aeItemCredit to store the credit title
Add table aeUserAccreditation to save the relation among user , item and credit
Date : 2002-12-19
*/
Alter Table aeItemCredit Drop Column [ict_code]
Alter Table aeItemCreditValue Alter Column icv_code varchar(50) null
Alter Table aeItemCredit Add ict_xml ntext

CREATE TABLE [dbo].[aeUserAccreditation] (
	[uad_itm_id] [int] NOT NULL ,
	[uad_usr_ent_id] [int] NOT NULL ,
	[uad_icv_id] [int] NOT NULL
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[aeUserAccreditation] WITH NOCHECK ADD
	CONSTRAINT [PK_aeUserAccreitation] PRIMARY KEY  NONCLUSTERED
	(
		[uad_itm_id],
		[uad_usr_ent_id],
		[uad_icv_id]
	)  ON [PRIMARY]
GO

ALTER TABLE [dbo].[aeUserAccreditation] ADD
	CONSTRAINT [FK_aeUserAccreditation_aeItem] FOREIGN KEY
	(
		[uad_itm_id]
	) REFERENCES [dbo].[aeItem] (
		[itm_id]
	),
	CONSTRAINT [FK_aeUserAccreditation_aeItemCreditValue] FOREIGN KEY
	(
		[uad_icv_id]
	) REFERENCES [dbo].[aeItemCreditValue] (
		[icv_id]
	),
	CONSTRAINT [FK_aeUserAccreditation_RegUser] FOREIGN KEY
	(
		[uad_usr_ent_id]
	) REFERENCES [dbo].[RegUser] (
		[usr_ent_id]
	)
GO



Insert Into SystemMessage Values ('IAD001','Big5','(Big5)The credit type already exists')
Insert Into SystemMessage Values ('IAD001','GB2312','(GB)The credit type already exists')
Insert Into SystemMessage Values ('IAD001','ISO-8859-1','The credit type already exists')

/*
Author:	Dennis Yip
Desc: 	Add coluum in aeTemplateView for linking Item Template with CodeTable
Date:	2002-12-19
*/
alter table aeTemplateView add tvw_ctb_ind int null
GO
update aeTemplateView set tvw_ctb_ind = 0
GO
alter table aeTemplateView alter column tvw_ctb_ind int not null
GO

/*
Author: Dennis
Desc: New acFunction entries for suspended user account re-activiation
Date: 2002-11-12
*/
insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values
('USR_REACTIVATE', 'FUNCTION', 'USER', getdate(), NULL)

/*
Author: Lun
Desc: New XSL template for email sent when user account is suspended
Date: 2002-11-12
*/
Insert into xslTemplate values ('USER_ACCOUNT_SUSPENSION_NOTIFICATION',
				'HTML',
				'SMTP',
				null,
				'user_account_suspension_notification.xsl',
				'http://host:port/servlet/aeAction?',
				1,
				'Account Suspended: $data')
declare @xtp_id int
select @xtp_id = max(xtp_id) from xslTemplate
insert into xslparamname values (@xtp_id, 1, 'ent_ids')
insert into xslparamname values (@xtp_id, 2, 'sender_id')
insert into xslparamname values (@xtp_id, 3, 'cmd')
insert into xslparamname values (@xtp_id, 4, 'site_id')
insert into xslparamname values (@xtp_id, 5, 'style')
insert into xslparamname values (@xtp_id, 6, 'id')
insert into xslparamname values (@xtp_id, 7, 'id_type')

/*
Author: Fai
Desc: New system message for user account re-activation
Date: 2002-11-14
*/
insert into systemMessage (sms_id, sms_lan, sms_desc) values ('USR018', 'Big5', 'User account is successfully reactivated.')
insert into systemMessage (sms_id, sms_lan, sms_desc) values ('USR018', 'GB2312', 'User account is successfully reactivated.')
insert into systemMessage (sms_id, sms_lan, sms_desc) values ('USR018', 'ISO-8859-1', 'User account is successfully reactivated.')

/* Author: Wai Lun
Desc: New XSL template for email sent when user account is suspended
Date: 2002-11-15
*/
Insert into xslTemplate values ('USER_ACCOUNT_SUSPENSION_SELF_NOTIFICATION',
				'HTML',
				'SMTP',
				null,
				'user_account_suspension_self_notification.xsl',
				'http://host:port/servlet/aeAction?',
				1,
				'Account Suspended: $data')
declare @xtp_id int
select @xtp_id = max(xtp_id) from xslTemplate
insert into xslparamname values (@xtp_id, 1, 'ent_ids')
insert into xslparamname values (@xtp_id, 2, 'sender_id')
insert into xslparamname values (@xtp_id, 3, 'cmd')

/*
Author:Wai Lun
Desc: Increase the column size of msg_subject in mgMessage table
Date: 2002-11-22
*/
ALTER TABLE mgMessage ALTER COLUMN msg_subject nvarchar(255) NULL

/*
Author:Kim Chan
Desc: Add Reminder IND in itemtype for button in UI
Date: 2002-12-24
*/
alter table aeItemType add ity_reminder_criteria_ind bit
go
update aeItemType set ity_reminder_criteria_ind = 0
go
alter table aeItemType alter column ity_reminder_criteria_ind bit not null
go


/*
Author:Dennis Yip
Desc: Table SystemMessage does not contains primary key in database wizbank_v3_5_core_clean.
      create the primary key if your database does not contain primary key neither.
Date: 2003-01-02
*/
ALTER TABLE [dbo].[SystemMessage] WITH NOCHECK ADD
	CONSTRAINT [PK_SystemMessage] PRIMARY KEY  CLUSTERED
	(
		[sms_id],
		[sms_lan]
	)  ON [PRIMARY]
GO

/*
Author:Kim
Desc: Fix an incident in evaluation submission report
Date: 2003-01-16
*/
update reporttemplate set rte_exe_xsl = 'rpt_svy_ind_res.xsl' where rte_type = 'SURVEY_IND'

/*
Author:Kim
Desc: Add more column in CodeTable, used in Import CodeTable
Date: 2003-01-16
*/
alter table codetype add ctp_update_timestamp datetime null
alter table codetype add ctp_update_usr_id varchar (50) null
go
update codetype set ctp_update_timestamp = ctp_create_timestamp
update codetype set ctp_update_usr_id = ctp_create_usr_id
go
alter table codetype alter column ctp_update_timestamp datetime not null
alter table codetype alter column ctp_update_usr_id varchar (50) not null
go

/*
Author : Fai
Desc : for course prerequisite and exemption
Date: 2003-01-17
*/
CREATE TABLE [dbo].[aeItemActn] (
	[iat_id] [int] IDENTITY (1, 1) NOT NULL ,
	[iat_type] [varchar] (20) NOT NULL
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[aeItemActn] WITH NOCHECK ADD
	CONSTRAINT [PK_aeItemActn] PRIMARY KEY  NONCLUSTERED
	( [iat_id] )  ON [PRIMARY]
GO

CREATE TABLE [dbo].[aeItemAttActn] (
	[iaa_iat_id] [int] NOT NULL ,
	[iaa_to_att_status] [varchar] (20) NOT NULL
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[aeItemAttActn] WITH NOCHECK ADD
	CONSTRAINT [PK_aeItemAttActn] PRIMARY KEY  NONCLUSTERED
	( [iaa_iat_id] )  ON [PRIMARY]
GO

ALTER TABLE [dbo].[aeItemAttActn] ADD
	CONSTRAINT [FK_aeItemAttActn_aeItemActn] FOREIGN KEY
	(
		[iaa_iat_id]
	) REFERENCES [dbo].[aeItemActn] (
		[iat_id]
	)
GO

CREATE TABLE [dbo].[aeItemRequirement] (
	[itr_itm_id] [int] NOT NULL,
	[itr_order] [int] NOT NULL,
	[itr_requirement_type] [varchar] (20) NOT NULL,
	[itr_requirement_subtype] [varchar] (20),
	[itr_requirement_restriction] [varchar] (20),
	[itr_requirement_due_date] [datetime],
	[itr_appn_footnote_ind] [int] NOT NULL,
	[itr_condition_type] [varchar] (20) NOT NULL,
	[itr_condition_rule] [ntext] NOT NULL,
	[itr_positive_iat_id] [int] NOT NULL,
	[itr_negative_iat_id] [int] NOT NULL,
	[itr_proc_execute_timestamp] [datetime]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[aeItemRequirement] WITH NOCHECK ADD
	CONSTRAINT [PK_aeItemRequirement] PRIMARY KEY  NONCLUSTERED
	( [itr_itm_id], [itr_order] )  ON [PRIMARY]
GO

ALTER TABLE [dbo].[aeItemRequirement] ADD
	CONSTRAINT [FK_aeItemRequirement_positive_aeItemActn] FOREIGN KEY
	(
		[itr_positive_iat_id]
	) REFERENCES [dbo].[aeItemActn] (
		[iat_id]
	)
GO

ALTER TABLE [dbo].[aeItemRequirement] ADD
	CONSTRAINT [FK_aeItemRequirement_negative_aeItemActn] FOREIGN KEY
	(
		[itr_negative_iat_id]
	) REFERENCES [dbo].[aeItemActn] (
		[iat_id]
	)
GO



/*
Author : Dennis Yip
Desc : new table to store due dates for prerequisite and exemption
Date: 2003-01-17
*/
CREATE TABLE [dbo].[aeItemReqDueDate] (
	[ird_itr_itm_id] [int] NOT NULL ,
	[ird_itr_order] [int] NOT NULL ,
	[ird_child_itm_id] [int] NOT NULL ,
	[ird_requirement_due_date] [datetime] NOT NULL
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[aeItemReqDueDate] WITH NOCHECK ADD
	CONSTRAINT [PK_aeItemReqDueDate] PRIMARY KEY  CLUSTERED
	(
		[ird_itr_itm_id],
		[ird_itr_order],
		[ird_child_itm_id]
	)  ON [PRIMARY]
GO

ALTER TABLE [dbo].[aeItemReqDueDate] ADD
	CONSTRAINT [FK_aeItemReqDueDate_aeItem] FOREIGN KEY
	(
		[ird_child_itm_id]
	) REFERENCES [dbo].[aeItem] (
		[itm_id]
	),
	CONSTRAINT [FK_aeItemReqDueDate_aeItemRequirement] FOREIGN KEY
	(
		[ird_itr_itm_id],
		[ird_itr_order]
	) REFERENCES [dbo].[aeItemRequirement] (
		[itr_itm_id],
		[itr_order]
	)
GO

/*
Author : Dennis Yip
Desc : change the positive action and negative action to be nullable columns
Date: 2003-01-17
*/
alter table aeItemRequirement alter column itr_positive_iat_id int NULL
alter table aeItemRequirement alter column itr_negative_iat_id int NULL


/***************************************************************
Author: Stanley Lam
Date: 2003-01-20
Desc: KM & e-Library ROLL TO CORE DB CHANGES START HERE
vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv*/
/*
Author: Stanley Lam
Date: 2002-12-31
Desc: KM Roll in
*/
CREATE TABLE [dbo].[kmNode] (
	[nod_id] [int] IDENTITY (1, 1) NOT NULL ,
	[nod_type] [varchar] (20) NOT NULL ,
	[nod_order] [int] NULL ,
	[nod_parent_nod_id] [int] NULL ,
	[nod_ancestor] [varchar] (500) NULL ,
	[nod_create_timestamp] [datetime] NOT NULL ,
	[nod_create_usr_id] [varchar] (20) NOT NULL ,
	[nod_owner_ent_id] [int] NOT NULL ,
	[nod_acl_inherit_ind] [int] NOT NULL
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[kmObjectHistory] (
	[ojh_obj_bob_nod_id] [int] NOT NULL ,
	[ojh_version] [varchar] (50) NOT NULL ,
	[ojh_publish_ind] [int] NOT NULL ,
	[ojh_latest_ind] [int] NOT NULL ,
	[ojh_type] [varchar] (20) NOT NULL ,
	[ojh_title] [nvarchar] (200) NOT NULL ,
	[ojh_desc] [nvarchar] (1000) NULL ,
	[ojh_status] [varchar] (50) NOT NULL ,
	[ojh_keywords] [nvarchar] (50) NULL ,
	[ojh_comment] [nvarchar] (1000) NULL ,
	[ojh_author] [nvarchar] (100) NULL ,
	[ojh_ttp_id] [int] NOT NULL ,
	[ojh_tpl_id] [int] NOT NULL ,
	[ojh_xml] [ntext] NULL ,
	[ojh_update_usr_id] [varchar] (20) NOT NULL ,
	[ojh_update_timestamp] [datetime] NOT NULL
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO

CREATE TABLE [dbo].[kmObjectType] (
	[oty_owner_ent_id] [int] NOT NULL ,
	[oty_code] [varchar] (20) NOT NULL ,
	[oty_nature] [varchar] (20) NULL ,
	[oty_seq_no] [int] NOT NULL ,
	[oty_create_usr_id] [varchar] (20) NOT NULL ,
	[oty_create_timestamp] [datetime] NOT NULL
)
GO

CREATE TABLE [dbo].[kmFolder] (
	[fld_nod_id] [int] NOT NULL ,
	[fld_type] [varchar] (50) NULL ,
	[fld_nature] [varchar] (50) NULL ,
	[fld_title] [nvarchar] (200) NOT NULL ,
	[fld_desc] [nvarchar] (1000) NULL ,
	[fld_obj_cnt] [int] NOT NULL ,
	[fld_update_usr_id] [varchar] (20) NOT NULL ,
	[fld_update_timestamp] [datetime] NOT NULL
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[kmBaseObject] (
	[bob_nod_id] [int] NOT NULL ,
	[bob_nature] [varchar] (50) NULL ,
	[bob_code] [nvarchar] (50) NOT NULL ,
	[bob_delete_usr_id] [varchar] (20) NULL ,
	[bob_delete_timestamp] [datetime] NULL
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[kmLink] (
	[lnk_nod_id] [int] NOT NULL ,
	[lnk_type] [varchar] (20) NOT NULL ,
	[lnk_title] [nvarchar] (200) NOT NULL ,
	[lnk_target_nod_id] [int] NOT NULL
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[kmNodeAccess] (
	[nac_nod_id] [int] NOT NULL ,
	[nac_access_type] [varchar] (20) NOT NULL ,
	[nac_ent_id] [int] NOT NULL
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[kmNodeActnHistory] (
	[nah_id] [int] IDENTITY (1, 1) NOT NULL ,
	[nah_nod_id] [int] NOT NULL ,
	[nah_type] [varchar] (20) NOT NULL ,
	[nah_update_timestamp] [datetime] NOT NULL ,
	[nah_xml] [nvarchar] (2000) NOT NULL
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[kmNodeAssignment] (
	[nam_nod_id] [int] NOT NULL ,
	[nam_ent_id] [int] NOT NULL ,
	[nam_type] [varchar] (20) NOT NULL ,
	[nam_create_usr_id] [varchar] (20) NOT NULL ,
	[nam_create_timestamp] [datetime] NOT NULL
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[kmNodeForum] (
	[nfr_nod_id] [int] NOT NULL ,
	[nfr_fto_res_id] [int] NOT NULL ,
	[nfr_fto_id] [int] NULL
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[kmNodeSubscription] (
	[nsb_nod_id] [int] NOT NULL ,
	[nsb_usr_ent_id] [int] NOT NULL ,
	[nsb_type] [varchar] (20) NOT NULL ,
	[nsb_email_send_type] [varchar] (20) NOT NULL ,
	[nsb_email_from_timestamp] [datetime] NULL ,
	[nsb_from_timestamp] [datetime] NOT NULL ,
	[nsb_create_timestamp] [datetime] NOT NULL
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[kmObject] (
	[obj_bob_nod_id] [int] NOT NULL ,
	[obj_version] [varchar] (50) NOT NULL ,
	[obj_publish_ind] [int] NOT NULL ,
	[obj_latest_ind] [int] NOT NULL ,
	[obj_type] [varchar] (20) NOT NULL ,
	[obj_title] [nvarchar] (200) NOT NULL ,
	[obj_desc] [nvarchar] (1000) NULL ,
	[obj_status] [varchar] (50) NOT NULL ,
	[obj_keywords] [nvarchar] (50) NULL ,
	[obj_author] [nvarchar] (100) NULL ,
	[obj_comment] [nvarchar] (1000) NULL ,
	[obj_xml] [ntext] NULL ,
	[obj_update_usr_id] [varchar] (20) NOT NULL ,
	[obj_update_timestamp] [datetime] NOT NULL
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO

CREATE TABLE [dbo].[kmObjectAttachment] (
	[oat_obj_bob_nod_id] [int] NOT NULL ,
	[oat_obj_version] [varchar] (50) NOT NULL ,
	[oat_att_id] [int] NOT NULL
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[kmObjectTemplate] (
	[ojt_obj_bob_nod_id] [int] NOT NULL ,
	[ojt_obj_version] [varchar] (50) NOT NULL ,
	[ojt_ttp_id] [int] NOT NULL ,
	[ojt_tpl_id] [int] NOT NULL
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[kmObjectTypeTemplate] (
	[ott_oty_owner_ent_id] [int] NOT NULL ,
	[ott_oty_code] [varchar] (20) NOT NULL ,
	[ott_ttp_id] [varchar] (20) NOT NULL ,
	[ott_tpl_id] [int] NOT NULL
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[kmObjectType] WITH NOCHECK ADD
	CONSTRAINT [PK_kmObjectType] PRIMARY KEY  CLUSTERED
	(
		[oty_owner_ent_id],
		[oty_code]
	)  ON [PRIMARY]
GO

ALTER TABLE [dbo].[kmNode] WITH NOCHECK ADD
	CONSTRAINT [PK_kmNode] PRIMARY KEY  NONCLUSTERED
	(
		[nod_id]
	)  ON [PRIMARY]
GO

ALTER TABLE [dbo].[kmObjectHistory] WITH NOCHECK ADD
	CONSTRAINT [PK_kmObjectHistory] PRIMARY KEY  NONCLUSTERED
	(
		[ojh_obj_bob_nod_id],
		[ojh_version]
	)  ON [PRIMARY]
GO

ALTER TABLE [dbo].[kmFolder] WITH NOCHECK ADD
	CONSTRAINT [PK_kmFolder] PRIMARY KEY  NONCLUSTERED
	(
		[fld_nod_id]
	)  ON [PRIMARY]
GO

ALTER TABLE [dbo].[kmLink] WITH NOCHECK ADD
	CONSTRAINT [PK_kmLink] PRIMARY KEY  NONCLUSTERED
	(
		[lnk_nod_id]
	)  ON [PRIMARY]
GO

ALTER TABLE [dbo].[kmNodeAccess] WITH NOCHECK ADD
	CONSTRAINT [PK_kmNodeAccess] PRIMARY KEY  NONCLUSTERED
	(
		[nac_nod_id],
		[nac_access_type],
		[nac_ent_id]
	)  ON [PRIMARY]
GO

ALTER TABLE [dbo].[kmNodeActnHistory] WITH NOCHECK ADD
	CONSTRAINT [PK_kmNodeNotification] PRIMARY KEY  NONCLUSTERED
	(
		[nah_id]
	)  ON [PRIMARY]
GO

ALTER TABLE [dbo].[kmNodeAssignment] WITH NOCHECK ADD
	CONSTRAINT [PK_kmNodeAssignment] PRIMARY KEY  NONCLUSTERED
	(
		[nam_nod_id],
		[nam_ent_id],
		[nam_type]
	)  ON [PRIMARY]
GO

ALTER TABLE [dbo].[kmNodeForum] WITH NOCHECK ADD
	CONSTRAINT [PK_kmNodeForum] PRIMARY KEY  NONCLUSTERED
	(
		[nfr_nod_id]
	)  ON [PRIMARY]
GO

ALTER TABLE [dbo].[kmNodeSubscription] WITH NOCHECK ADD
	CONSTRAINT [PK_kcNodeSubscribe] PRIMARY KEY  NONCLUSTERED
	(
		[nsb_nod_id],
		[nsb_usr_ent_id]
	)  ON [PRIMARY]
GO

ALTER TABLE [dbo].[kmBaseObject] WITH NOCHECK ADD
	CONSTRAINT [PK_kmBaseObject] PRIMARY KEY  NONCLUSTERED
	(
		[bob_nod_id]
	)  ON [PRIMARY]
GO

ALTER TABLE [dbo].[kmObject] WITH NOCHECK ADD
	CONSTRAINT [PK_kmObject] PRIMARY KEY  NONCLUSTERED
	(
		[obj_bob_nod_id],
		[obj_version]
	)  ON [PRIMARY]
GO

ALTER TABLE [dbo].[kmNode] ADD
	CONSTRAINT [FK_kmNode_kmNode] FOREIGN KEY
	(
		[nod_parent_nod_id]
	) REFERENCES [dbo].[kmNode] (
		[nod_id]
	)
GO

ALTER TABLE [dbo].[kmFolder] ADD
	CONSTRAINT [FK_kmFolder_kmNode] FOREIGN KEY
	(
		[fld_nod_id]
	) REFERENCES [dbo].[kmNode] (
		[nod_id]
	)
GO

ALTER TABLE [dbo].[kmLink] ADD
	CONSTRAINT [FK_kmLink_kmNode] FOREIGN KEY
	(
		[lnk_nod_id]
	) REFERENCES [dbo].[kmNode] (
		[nod_id]
	),
	CONSTRAINT [FK_kmLink_kmNode1] FOREIGN KEY
	(
		[lnk_target_nod_id]
	) REFERENCES [dbo].[kmNode] (
		[nod_id]
	)
GO

ALTER TABLE [dbo].[kmNodeAccess] ADD
	CONSTRAINT [FK_kmNodeAccess_kmNode] FOREIGN KEY
	(
		[nac_nod_id]
	) REFERENCES [dbo].[kmNode] (
		[nod_id]
	)
GO

ALTER TABLE [dbo].[kmNodeActnHistory] ADD
	CONSTRAINT [FK_kmNodeNotification_kmNode1] FOREIGN KEY
	(
		[nah_nod_id]
	) REFERENCES [dbo].[kmNode] (
		[nod_id]
	)
GO

ALTER TABLE [dbo].[kmNodeAssignment] ADD
	CONSTRAINT [FK_kmNodeAssignment_kmNode] FOREIGN KEY
	(
		[nam_nod_id]
	) REFERENCES [dbo].[kmNode] (
		[nod_id]
	)
GO

ALTER TABLE [dbo].[kmNodeForum] ADD
	CONSTRAINT [FK_kmNodeForum_kmNode] FOREIGN KEY
	(
		[nfr_nod_id]
	) REFERENCES [dbo].[kmNode] (
		[nod_id]
	)
GO

ALTER TABLE [dbo].[kmNodeSubscription] ADD
	CONSTRAINT [FK_kcNodeSubscribe_kmNode] FOREIGN KEY
	(
		[nsb_nod_id]
	) REFERENCES [dbo].[kmNode] (
		[nod_id]
	)
GO

ALTER TABLE [dbo].[kmBaseObject] ADD
	CONSTRAINT [FK_kmBaseObject_kmNode] FOREIGN KEY
	(
		[bob_nod_id]
	) REFERENCES [dbo].[kmNode] (
		[nod_id]
	)
GO

ALTER TABLE [dbo].[kmObject] ADD
	CONSTRAINT [FK_kmObject_kmBaseObject] FOREIGN KEY
	(
		[obj_bob_nod_id]
	) REFERENCES [dbo].[kmBaseObject] (
		[bob_nod_id]
	)
GO

ALTER TABLE [dbo].[kmObjectAttachment] ADD
	CONSTRAINT [FK_kmObjectAttachment_Attachment] FOREIGN KEY
	(
		[oat_att_id]
	) REFERENCES [dbo].[Attachment] (
		[att_id]
	)
GO

ALTER TABLE [dbo].[kmObjectTemplate] ADD
	CONSTRAINT [FK_kmObjectTemplate_aeTemplate] FOREIGN KEY
	(
		[ojt_tpl_id]
	) REFERENCES [dbo].[aeTemplate] (
		[tpl_id]
	),
	CONSTRAINT [FK_kmObjectTemplate_aeTemplateType] FOREIGN KEY
	(
		[ojt_ttp_id]
	) REFERENCES [dbo].[aeTemplateType] (
		[ttp_id]
	),
	CONSTRAINT [FK_kmObjectTemplate_kmObject] FOREIGN KEY
	(
		[ojt_obj_bob_nod_id],
		[ojt_obj_version]
	) REFERENCES [dbo].[kmObject] (
		[obj_bob_nod_id],
		[obj_version]
	)
GO

ALTER TABLE [dbo].[kmObjectTypeTemplate] ADD
	CONSTRAINT [FK_kmObjectTypeTemplate_aeTemplate] FOREIGN KEY
	(
		[ott_tpl_id]
	) REFERENCES [dbo].[aeTemplate] (
		[tpl_id]
	),
	CONSTRAINT [FK_kmObjectTypeTemplate_kmObjectType] FOREIGN KEY
	(
		[ott_oty_owner_ent_id],
		[ott_oty_code]
	) REFERENCES [dbo].[kmObjectType] (
		[oty_owner_ent_id],
		[oty_code]
	)
GO

alter table aeTemplateView add tvw_filesize_ind int NULL
alter table aeTemplateView add tvw_km_domain_ind int NULL
alter table aeTemplateView add tvw_km_published_version_ind int NULL
GO


/*
Author: Stanley Lam
Date: 2002-12-31
Desc: KM Roll in (SystemMessage)
*/
insert SystemMessage values ('KMM001', 'ISO-8859-1', 'You do not have the permission to access this record.')
insert SystemMessage values ('KMM001', 'Big5', '(Big 5) You do not have the permission to access this record.')
insert SystemMessage values ('KMM001', 'GB2312', '(GB2312) You do not have the permission to access this record.')

insert SystemMessage values ('KMM002', 'ISO-8859-1', 'You do not have the permission to update this record.')
insert SystemMessage values ('KMM002', 'Big5', '(Big 5) You do not have the permission to update this record.')
insert SystemMessage values ('KMM002', 'GB2312', '(GB2312) You do not have the permission to update this record.')

insert SystemMessage values ('KMM003', 'ISO-8859-1', 'The item has been subscribed successfully.')
insert SystemMessage values ('KMM003', 'Big5', '(Big 5) The item has been subscribed successfully.')
insert SystemMessage values ('KMM003', 'GB2312', '(GB2312) The item has been subscribed successfully.')

insert SystemMessage values ('KMM004', 'ISO-8859-1', 'You had already subscribed to this item.')
insert SystemMessage values ('KMM004', 'Big5', '(Big 5) You had already subscribed to this item.')
insert SystemMessage values ('KMM004', 'GB2312', '(GB2312) You had already subscribed to this item.')

insert SystemMessage values ('KMM005', 'ISO-8859-1', 'The knowledge object has been checked-in successfully.')
insert SystemMessage values ('KMM005', 'Big5', '(Big 5) The knowledge object has been checked-in successfully.')
insert SystemMessage values ('KMM005', 'GB2312', '(GB2312) The knowledge object has been checked-in successfully.')

insert SystemMessage values ('KMM006', 'ISO-8859-1', 'The knowledge object is currently checked out by other users.')
insert SystemMessage values ('KMM006', 'Big5', '(Big 5) The knowledge object is currently checked out by other users.')
insert SystemMessage values ('KMM006', 'GB2312', '(GB2312) The knowledge object is currently checked out by other users.')

insert SystemMessage values ('KMM007', 'ISO-8859-1', 'You have not checked out the knowledge object.')
insert SystemMessage values ('KMM007', 'Big5', '(Big 5) You have not checked out the knowledge object.')
insert SystemMessage values ('KMM007', 'GB2312', '(GB2312) You have not checked out the knowledge object.')

insert SystemMessage values ('KMM008', 'ISO-8859-1', 'The item has been added to your workplace.')
insert SystemMessage values ('KMM008', 'Big5', '(Big 5) The item has been added to your workplace.')
insert SystemMessage values ('KMM008', 'GB2312', '(GB2312) The item has been added to your workplace.')

insert SystemMessage values ('KMM009', 'ISO-8859-1', 'The item has been assigned successfully.')
insert SystemMessage values ('KMM009', 'Big5', '(Big 5) The item has been assigned successfully.')
insert SystemMessage values ('KMM009', 'GB2312', '(GB2312) The item has been assigned successfully.')


/*
Author: Stanley Lam
Date: 2002-12-31
Desc: e-Library
*/
CREATE TABLE [dbo].[kmLibraryObject] (
	[lio_bob_nod_id] [int] NOT NULL ,
	[lio_num_copy] [int] NOT NULL ,
	[lio_num_copy_available] [int] NOT NULL ,
	[lio_num_copy_in_stock] [int] NOT NULL
)
GO

CREATE TABLE [dbo].[kmLibraryObjectCopy] (
	[loc_lio_bob_nod_id] [int] NOT NULL ,
	[loc_id] [int] IDENTITY (1, 1) NOT NULL ,
	[loc_copy] [nvarchar] (50) NOT NULL ,
	[loc_desc] [ntext] NULL ,
	[loc_create_usr_id] [varchar] (20) NOT NULL ,
	[loc_create_timestamp] [datetime] NOT NULL ,
	[loc_update_usr_id] [varchar] (20) NOT NULL ,
	[loc_update_timestamp] [datetime] NOT NULL ,
	[loc_delete_usr_id] [varchar] (20) NULL ,
	[loc_delete_timestamp] [datetime] NULL
)
GO

CREATE TABLE [dbo].[kmLibraryObjectBorrow] (
	[lob_id] [int] IDENTITY (1, 1) NOT NULL ,
	[lob_lio_bob_nod_id] [int] NOT NULL ,
	[lob_usr_ent_id] [int] NOT NULL ,
	[lob_status] [varchar] (20) NOT NULL ,
	[lob_loc_id] [int] NULL ,
	[lob_renew_no] [int] NULL ,
	[lob_due_timestamp] [datetime] NULL ,
	[lob_create_usr_id] [varchar] (20) NOT NULL ,
	[lob_create_timestamp] [datetime] NOT NULL ,
	[lob_update_usr_id] [varchar] (20) NOT NULL ,
	[lob_update_timestamp] [datetime] NOT NULL ,
	[lob_latest_ind] [int] NOT NULL
)
GO

ALTER TABLE [dbo].[kmLibraryObject] WITH NOCHECK ADD
	CONSTRAINT [PK_kmLibraryObject] PRIMARY KEY  CLUSTERED
	(
		[lio_bob_nod_id]
	)  ON [PRIMARY]
GO

ALTER TABLE [dbo].[kmLibraryObjectBorrow] WITH NOCHECK ADD
	CONSTRAINT [PK_kmLibraryObjectBorrower] PRIMARY KEY  CLUSTERED
	(
		[lob_id],
		[lob_lio_bob_nod_id]
	)  ON [PRIMARY]
GO

ALTER TABLE [dbo].[kmLibraryObjectCopy] WITH NOCHECK ADD
	CONSTRAINT [PK_kmLibraryObjectCopy] PRIMARY KEY  CLUSTERED
	(
		[loc_lio_bob_nod_id],
		[loc_id]
	)  ON [PRIMARY]
GO

ALTER TABLE [dbo].[kmLibraryObject] ADD
	CONSTRAINT [FK_kmLibraryObject_kmBaseObject] FOREIGN KEY
	(
		[lio_bob_nod_id]
	) REFERENCES [dbo].[kmBaseObject] (
		[bob_nod_id]
	)
GO

ALTER TABLE [dbo].[kmLibraryObjectBorrow] ADD
	CONSTRAINT [FK_kmBorrow_kmLibraryObject] FOREIGN KEY
	(
		[lob_lio_bob_nod_id]
	) REFERENCES [dbo].[kmLibraryObject] (
		[lio_bob_nod_id]
	)
GO

ALTER TABLE [dbo].[kmLibraryObjectCopy] ADD
	CONSTRAINT [FK_kmLibraryObjectCopy_kmLibraryObject] FOREIGN KEY
	(
		[loc_lio_bob_nod_id]
	) REFERENCES [dbo].[kmLibraryObject] (
		[lio_bob_nod_id]
	)
GO

 CREATE  INDEX [i_kmlibraryobjectborrow_lob_latest_ind] ON [dbo].[kmLibraryObjectBorrow]([lob_latest_ind]) ON [PRIMARY]
GO

/*
Author: Fai
Desc: Create the new table for Transaction Log (roll from polyu)
Date: 2002-12-31
*/
CREATE TABLE [dbo].[wbTransactionLog] (
	[tlg_id] [int] Identity(1,1) NOT NULL,
	[tlg_ref_id_1] [varchar] (50) NULL,
	[tlg_ref_id_2] [varchar] (50) NULL,
	[tlg_ref_id_3] [varchar] (50) NULL,
	[tlg_type] [varchar] (50) NOT NULL,
	[tlg_detail_xml] [ntext] NOT NULL,
	[tlg_create_usr_id] [varchar] (50) NOT NULL,
	[tlg_create_timestamp] [datetime] NOT NULL)

GO


/*
Author: Stanley
Desc: Create acFunction for the new KM
Date: 2003-01-02
*/
insert acFunction values ('KM_MGT', 'FUNCTION', null, getDate(), null)

declare @ftn_id int
select @ftn_id = ftn_id from acFunction where ftn_ext_id = 'KM_MGT'
insert acRoleFunction values (1, @ftn_id, getDate())

/*
Author: Fai
Desc: New system message for kmlibrary checkin
Date:  2003-01-03
*/
insert into systemMessage (sms_id, sms_lan, sms_desc) values ('KML001', 'Big5', '!!!Invalid call number.')
insert into systemMessage (sms_id, sms_lan, sms_desc) values ('KML001', 'GB2312', '!!!Invalid call number.')
insert into systemMessage (sms_id, sms_lan, sms_desc) values ('KML001', 'ISO-8859-1', 'Invalid call number.')

insert into systemMessage (sms_id, sms_lan, sms_desc) values ('KML002', 'Big5', '!!!Invalid copy number.')
insert into systemMessage (sms_id, sms_lan, sms_desc) values ('KML002', 'GB2312', '!!!Invalid copy number.')
insert into systemMessage (sms_id, sms_lan, sms_desc) values ('KML002', 'ISO-8859-1', 'Invalid copy number.')

insert into systemMessage (sms_id, sms_lan, sms_desc) values ('KML003', 'Big5', '!!!Item is not checked out.')
insert into systemMessage (sms_id, sms_lan, sms_desc) values ('KML003', 'GB2312', '!!!Item is not checked out.')
insert into systemMessage (sms_id, sms_lan, sms_desc) values ('KML003', 'ISO-8859-1', 'Item is not checked out.')

insert into systemMessage (sms_id, sms_lan, sms_desc) values ('KML004', 'Big5', '!!!Item is checked in successfully.')
insert into systemMessage (sms_id, sms_lan, sms_desc) values ('KML004', 'GB2312', '!!!Item is checked in successfully.')
insert into systemMessage (sms_id, sms_lan, sms_desc) values ('KML004', 'ISO-8859-1', 'Item is checked in successfully.')

/*
Author: Fai
Desc: New system message for kmlibrary checkout
Date:  2003-01-06
*/
insert into systemMessage (sms_id, sms_lan, sms_desc) values ('KML005', 'Big5', '!!!Item is checked out successfully.')
insert into systemMessage (sms_id, sms_lan, sms_desc) values ('KML005', 'GB2312', '!!!Item is checked out successfully.')
insert into systemMessage (sms_id, sms_lan, sms_desc) values ('KML005', 'ISO-8859-1', 'Item is checked out successfully.')

insert into systemMessage (sms_id, sms_lan, sms_desc) values ('KML006', 'Big5', '!!!User violate the policy. Item cannot be checkout.')
insert into systemMessage (sms_id, sms_lan, sms_desc) values ('KML006', 'GB2312', '!!!User violate the policy. Item cannot be checkout.')
insert into systemMessage (sms_id, sms_lan, sms_desc) values ('KML006', 'ISO-8859-1', 'User violate the policy. Item cannot be checkout.')

insert into systemMessage (sms_id, sms_lan, sms_desc) values ('KML007', 'Big5', '!!!No copy in stock.')
insert into systemMessage (sms_id, sms_lan, sms_desc) values ('KML007', 'GB2312', '!!!No copy in stock.')
insert into systemMessage (sms_id, sms_lan, sms_desc) values ('KML007', 'ISO-8859-1', 'No copy in stock.')

/*
Author: Fai
Desc: Create acFunction for the new KMLibrary
Date: 2003-01-06
*/
insert acFunction values ('KML_CHECKIN', 'FUNCTION', null, getDate(), null)

declare @ftn_id int
select @ftn_id = ftn_id from acFunction where ftn_ext_id = 'KML_CHECKIN'
insert acRoleFunction values (1, @ftn_id, getDate())
--insert acRoleFunction values (5, @ftn_id, getDate())

insert acFunction values ('KML_CHECKOUT', 'FUNCTION', null, getDate(), null)

declare @ftn_id int
select @ftn_id = ftn_id from acFunction where ftn_ext_id = 'KML_CHECKOUT'
insert acRoleFunction values (1, @ftn_id, getDate())
--insert acRoleFunction values (5, @ftn_id, getDate())

/*
Author: Fai
Desc: New system message for kmlibrary privilege
Date:  2003-01-06
*/
insert into systemMessage (sms_id, sms_lan, sms_desc) values ('KML008', 'Big5', '!!!You do not have the permission to check in.')
insert into systemMessage (sms_id, sms_lan, sms_desc) values ('KML008', 'GB2312', '!!!You do not have the permission to check in.')
insert into systemMessage (sms_id, sms_lan, sms_desc) values ('KML008', 'ISO-8859-1', 'You do not have the permission to check in.')

insert into systemMessage (sms_id, sms_lan, sms_desc) values ('KML009', 'Big5', '!!!You do not have the permission to check out.')
insert into systemMessage (sms_id, sms_lan, sms_desc) values ('KML009', 'GB2312', '!!!You do not have the permission to check out.')
insert into systemMessage (sms_id, sms_lan, sms_desc) values ('KML009', 'ISO-8859-1', 'You do not have the permission to check out.')

/*
Author: Stanley
Desc: New system message for kmlibrary
Date:  2003-01-07
*/
insert SystemMessage values ('KML010', 'ISO-8859-1', 'Copy Number already exists.')
insert SystemMessage values ('KML010', 'Big5', '!!!Copy Number already exists.')
insert SystemMessage values ('KML010', 'GB2312', '!!!Copy Number already exists.')

insert SystemMessage values ('KML011', 'ISO-8859-1', 'The Copy is on loan.')
insert SystemMessage values ('KML011', 'Big5', '!!!The Copy is on loan.')
insert SystemMessage values ('KML011', 'GB2312', '!!!The Copy is on loan.')

insert SystemMessage values ('KML012', 'ISO-8859-1', 'The Item has outstanding request(s).')
insert SystemMessage values ('KML012', 'Big5', '!!!The Item has outstanding request(s).')
insert SystemMessage values ('KML012', 'GB2312', '!!!The Item has outstanding request(s).')

insert SystemMessage values ('KML013', 'ISO-8859-1', 'The Item has outstanding copy(s).')
insert SystemMessage values ('KML013', 'Big5', '!!!The Item has outstanding copy(s).')
insert SystemMessage values ('KML013', 'GB2312', '!!!The Item has outstanding copy(s).')

insert SystemMessage values ('KML014', 'ISO-8859-1', 'Call Number already exists.')
insert SystemMessage values ('KML014', 'Big5', '!!!Call Number already exists.')
insert SystemMessage values ('KML014', 'GB2312', '!!!Ca;ll Number already exists.')

/*
Author: Fai
Desc: New system message for kmlibrary
Date:  2003-01-09
*/

insert SystemMessage values ('KML015', 'ISO-8859-1', 'Overdue policy is violated.')
insert SystemMessage values ('KML015', 'Big5', '!!!Overdue policy is violated.')
insert SystemMessage values ('KML015', 'GB2312', '!!!Overdue policy is violated.')

insert SystemMessage values ('KML016', 'ISO-8859-1', 'Renew policy is violated.')
insert SystemMessage values ('KML016', 'Big5', '!!!Renew policy is violated.')
insert SystemMessage values ('KML016', 'GB2312', '!!!Renew policy is violated.')

insert SystemMessage values ('KML017', 'ISO-8859-1', 'Item is reserved by other user. Item cannot be renewed.')
insert SystemMessage values ('KML017', 'Big5', '!!!Item is reserved by other user. Item cannot be renewed.')
insert SystemMessage values ('KML017', 'GB2312', '!!!Item is reserved by other user. Item cannot be renewed.')

insert SystemMessage values ('KML018', 'ISO-8859-1', 'Item is renewed successfully.')
insert SystemMessage values ('KML018', 'Big5', '!!!Item is renewed successfully.')
insert SystemMessage values ('KML018', 'GB2312', '!!!Item is renewed successfully.')

/*
Author: Fai
Desc: New system message for kmlibrary
Date:  2003-01-10
*/
insert SystemMessage values ('KML019', 'ISO-8859-1', 'You have borrowed the same item already.')
insert SystemMessage values ('KML019', 'Big5', '!!!You have borrowed the same item already.')
insert SystemMessage values ('KML019', 'GB2312', '!!!You have borrowed the same item already.')

insert SystemMessage values ('KML020', 'ISO-8859-1', 'You have reserve the item already.')
insert SystemMessage values ('KML020', 'Big5', '!!!You have reserve the item already.')
insert SystemMessage values ('KML020', 'GB2312', '!!!You have reserve the item already.')

insert SystemMessage values ('KML021', 'ISO-8859-1', 'You have checked out the same item already.')
insert SystemMessage values ('KML021', 'Big5', '!!!You have checked out the same item already.')
insert SystemMessage values ('KML021', 'GB2312', '!!!You have checked out the same item already.')

insert SystemMessage values ('KML022', 'ISO-8859-1', 'You can borrow the item. You cannot reserve.')
insert SystemMessage values ('KML022', 'Big5', '!!!You can borrow the item. You cannot reserve.')
insert SystemMessage values ('KML022', 'GB2312', '!!!You can borrow the item. You cannot reserve.')

insert SystemMessage values ('KML023', 'ISO-8859-1', 'Borrow policy is violated.')
insert SystemMessage values ('KML023', 'Big5', '!!!Borrow policy is violated.')
insert SystemMessage values ('KML023', 'GB2312', '!!!Borrow policy is violated.')

insert SystemMessage values ('KML024', 'ISO-8859-1', 'No available copies.')
insert SystemMessage values ('KML024', 'Big5', '!!!No available copies.')
insert SystemMessage values ('KML024', 'GB2312', '!!!No available copies.')

insert SystemMessage values ('KML025', 'ISO-8859-1', 'The item has been reserved. You cannot borrow the item.')
insert SystemMessage values ('KML025', 'Big5', '!!!The item has been reserved. You cannot borrow the item.')
insert SystemMessage values ('KML025', 'GB2312', '!!!The item has been reserved. You cannot borrow the item.')

/*
Author: Fai
Desc: Create acFunction for the new KMLibrary
Date: 2003-01-13
*/
insert acFunction values ('KML_RENEW', 'FUNCTION', null, getDate(), null)

declare @ftn_id int
select @ftn_id = ftn_id from acFunction where ftn_ext_id = 'KML_RENEW'
insert acRoleFunction values (1, @ftn_id, getDate())
insert acRoleFunction values (*LEARNER_ROLE_ID*, @ftn_id, getDate())

insert acFunction values ('KML_RESERVE', 'FUNCTION', null, getDate(), null)

declare @ftn_id int
select @ftn_id = ftn_id from acFunction where ftn_ext_id = 'KML_RESERVE'
insert acRoleFunction values (1, @ftn_id, getDate())
insert acRoleFunction values (*LEARNER_ROLE_ID*, @ftn_id, getDate())

insert acFunction values ('KML_BORROW', 'FUNCTION', null, getDate(), null)

declare @ftn_id int
select @ftn_id = ftn_id from acFunction where ftn_ext_id = 'KML_BORROW'
insert acRoleFunction values (1, @ftn_id, getDate())
insert acRoleFunction values (*LEARNER_ROLE_ID*, @ftn_id, getDate())

insert acFunction values ('KML_USER_HISTORY', 'FUNCTION', null, getDate(), null)

declare @ftn_id int
select @ftn_id = ftn_id from acFunction where ftn_ext_id = 'KML_USER_HISTORY'
insert acRoleFunction values (1, @ftn_id, getDate())
--insert acRoleFunction values (7, @ftn_id, getDate())


insert acFunction values ('KML_COPY_HISTORY', 'FUNCTION', null, getDate(), null)

declare @ftn_id int
select @ftn_id = ftn_id from acFunction where ftn_ext_id = 'KML_COPY_HISTORY'
insert acRoleFunction values (1, @ftn_id, getDate())
--insert acRoleFunction values (7, @ftn_id, getDate())

/*
Author: Fai
Desc: New system message for kmlibrary privilege
Date:  2003-01-13
*/
insert into systemMessage (sms_id, sms_lan, sms_desc) values ('KML026', 'Big5', '!!!You do not have the permission to renew.')
insert into systemMessage (sms_id, sms_lan, sms_desc) values ('KML026', 'GB2312', '!!!You do not have the permission to renew.')
insert into systemMessage (sms_id, sms_lan, sms_desc) values ('KML026', 'ISO-8859-1', 'You do not have the permission to renew.')

insert into systemMessage (sms_id, sms_lan, sms_desc) values ('KML027', 'Big5', '!!!You do not have the permission to reserve.')
insert into systemMessage (sms_id, sms_lan, sms_desc) values ('KML027', 'GB2312', '!!!You do not have the permission to reserve.')
insert into systemMessage (sms_id, sms_lan, sms_desc) values ('KML027', 'ISO-8859-1', 'You do not have the permission to reserve.')

insert into systemMessage (sms_id, sms_lan, sms_desc) values ('KML028', 'Big5', '!!!You do not have the permission to borrow.')
insert into systemMessage (sms_id, sms_lan, sms_desc) values ('KML028', 'GB2312', '!!!You do not have the permission to borrow.')
insert into systemMessage (sms_id, sms_lan, sms_desc) values ('KML028', 'ISO-8859-1', 'You do not have the permission to borrow.')

insert into systemMessage (sms_id, sms_lan, sms_desc) values ('KML029', 'Big5', '!!!You do not have the permission to view user history.')
insert into systemMessage (sms_id, sms_lan, sms_desc) values ('KML029', 'GB2312', '!!!You do not have the permission to view user history.')
insert into systemMessage (sms_id, sms_lan, sms_desc) values ('KML029', 'ISO-8859-1', 'You do not have the permission to view user history.')

insert into systemMessage (sms_id, sms_lan, sms_desc) values ('KML030', 'Big5', '!!!You do not have the permission to view copy history.')
insert into systemMessage (sms_id, sms_lan, sms_desc) values ('KML030', 'GB2312', '!!!You do not have the permission to view copy history.')
insert into systemMessage (sms_id, sms_lan, sms_desc) values ('KML030', 'ISO-8859-1', 'You do not have the permission to view copy history.')

/*
Author: Fai
Desc: New message Template for kmlibrary reserve / borrow
Date:  2003-01-13
*/
insert into xslTemplate
values ('RESERVE_NOTIFICATION', 'HTML', 'SMTP', NULL, 'km_lib_reserve_notification.xsl', 'http://host:port/servlet/Dispatcher?env=wizb&module=km.library.KMLibraryModule&', 1, NULL)
declare @xtp_id int
select @xtp_id = max(xtp_id) from xslTemplate
insert into xslparamname values (@xtp_id, 1, 'cmd')
insert into xslparamname values (@xtp_id, 2, 'usr_ent_id')
insert into xslparamname values (@xtp_id, 3, 'sender_id')
insert into xslparamname values (@xtp_id, 4, 'lob_id')

insert into xslTemplate
values ('BORROW_NOTIFICATION', 'HTML', 'SMTP', NULL, 'km_lib_borrow_notification.xsl', 'http://host:port/servlet/Dispatcher?env=wizb&module=km.library.KMLibraryModule&', 1, NULL)
declare @xtp_id int
select @xtp_id = max(xtp_id) from xslTemplate
insert into xslparamname values (@xtp_id, 1, 'cmd')
insert into xslparamname values (@xtp_id, 2, 'usr_ent_id')
insert into xslparamname values (@xtp_id, 3, 'sender_id')
insert into xslparamname values (@xtp_id, 4, 'lob_id')

/*
Author: Stanley
Desc: New acFunction for e-Library browsing
Date: 2003-01-14
*/
insert acFunction values ('KM_LINK', 'FUNCTION', 'HOMEPAGE', getDate(), '<function id="KM_LINK">	<desc lan="ISO-8859-1" name="e-Library"/>	<desc lan="Big5" name="e-Library"/>	<desc lan="GB2312" name="e-Library"/></function>')
declare @ftn_id int
select @ftn_id = ftn_id from acFunction where ftn_ext_id = 'KM_LINK'
insert acRoleFunction values (*FOR LEARNER ROLE*, @ftn_id, getDate())

update acFunction set ftn_type = 'HOMEPAGE', ftn_xml = '<function id="KM_MGT">	<desc lan="ISO-8859-1" name="e-Library"/>	<desc lan="Big5" name="e-Library"/>	<desc lan="GB2312" name="e-Library"/></function>' where ftn_ext_id = 'KM_MGT'


/*
Author: Fai
Desc: New system message for kmlibrary cancel
Date:  2003-01-15
*/
insert into systemMessage (sms_id, sms_lan, sms_desc) values ('KML031', 'Big5', '!!!You do not have the permission to cancel.')
insert into systemMessage (sms_id, sms_lan, sms_desc) values ('KML031', 'GB2312', '!!!You do not have the permission to cancel.')
insert into systemMessage (sms_id, sms_lan, sms_desc) values ('KML031', 'ISO-8859-1', 'You do not have the permission to cancel.')

/*
Author: Fai
Desc: Create acFunction for the new KMLibrary
Date: 2003-01-15
*/
insert acFunction values ('KML_CANCEL', 'FUNCTION', null, getDate(), null)

declare @ftn_id int
select @ftn_id = ftn_id from acFunction where ftn_ext_id = 'KML_CANCEL'
insert acRoleFunction values (1, @ftn_id, getDate())
insert acRoleFunction values (*LEARNER_ROLE_ID*, @ftn_id, getDate())

/*^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
Author: Stanley Lam
Date: 2003-01-20
Desc: KM & e-Library ROLL TO CORE DB CHANGES END HERE
****************************************************************/


/***************************************************************
Author: Chris Lo
Date: 2003-01-20
Desc: Quick Reference/ Tracking History ROLL TO CORE START HERE
vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv*/

/*
Author: Chris Lo
Desc: Create the new table for the tracking history
Date: 2003-01-20
*/

CREATE TABLE dbo.TrackingHistory
	(
	tkh_id int NOT NULL IDENTITY (1, 1),
	tkh_type varchar(20) NOT NULL,
	tkh_usr_ent_id int NOT NULL,
	tkh_cos_res_id int NOT NULL,
	tkh_create_timestamp datetime NOT NULL
	)  ON [PRIMARY]
GO
ALTER TABLE dbo.TrackingHistory ADD CONSTRAINT
	PK_TrackingHistory PRIMARY KEY CLUSTERED
	(
	tkh_id
	) ON [PRIMARY]

/*
Author: Chris Lo
Desc: Link the application to its tracking history
Date: 2003-01-20
*/

ALTER TABLE dbo.aeApplication ADD
	app_tkh_id int NULL

/*
Author: Chris Lo
Desc: Modify the composite keys of the existing tables to include the tracking id
Date: 2003-01-20
*/

ALTER TABLE dbo.CourseEvaluation
	DROP CONSTRAINT PK_ResourceEvaluation
GO

ALTER TABLE dbo.CourseEvaluation ADD cov_tkh_id int NULL
GO

update CourseEvaluation set cov_tkh_id=0

ALTER TABLE dbo.CourseEvaluation ALTER COLUMN cov_tkh_id int NOT NULL
GO
ALTER TABLE dbo.CourseEvaluation ADD CONSTRAINT
	PK_CourseEvaluation PRIMARY KEY CLUSTERED
	(
	cov_cos_id,
	cov_ent_id,
	cov_tkh_id
	) ON [PRIMARY]

GO

ALTER TABLE dbo.ModuleEvaluation
	DROP CONSTRAINT PK_ModuleEvaluation
GO

ALTER TABLE dbo.ModuleEvaluation ADD
	mov_tkh_id int NULL
GO
update ModuleEvaluation set mov_tkh_id=0
ALTER TABLE dbo.ModuleEvaluation ALTER COLUMN mov_tkh_id int NOT NULL
GO
ALTER TABLE dbo.ModuleEvaluation ADD CONSTRAINT
	PK_ModuleEvaluation PRIMARY KEY CLUSTERED
	(
	mov_ent_id,
	mov_mod_id,
	mov_tkh_id
	) ON [PRIMARY]

GO

ALTER TABLE dbo.ProgressAttachment
	DROP CONSTRAINT FK_ProgressAttachment_Progress
GO
ALTER TABLE dbo.ProgressAttempt
	DROP CONSTRAINT FK_ProgressAttempt_Progress
GO
ALTER TABLE dbo.Progress
	DROP CONSTRAINT PK_Progress
GO

ALTER TABLE dbo.Progress ADD pgr_tkh_id int NULL
GO
update Progress set pgr_tkh_id=0
ALTER TABLE dbo.Progress ALTER COLUMN pgr_tkh_id int NOT NULL
GO

ALTER TABLE dbo.Progress ADD CONSTRAINT
	PK_Progress PRIMARY KEY CLUSTERED
	(
	pgr_usr_id,
	pgr_res_id,
	pgr_attempt_nbr,
	pgr_tkh_id
	) ON [PRIMARY]

GO

ALTER TABLE dbo.ProgressAttachment
	DROP CONSTRAINT PK_ProgressAttachment
GO
ALTER TABLE dbo.ProgressAttachment ADD pat_tkh_id int NULL
GO
update ProgressAttachment set pat_tkh_id=0
ALTER TABLE dbo.ProgressAttachment ALTER COLUMN pat_tkh_id int NOT NULL
GO
ALTER TABLE dbo.ProgressAttachment ADD CONSTRAINT
	PK_ProgressAttachment PRIMARY KEY CLUSTERED
	(
	pat_prg_usr_id,
	pat_prg_res_id,
	pat_prg_attempt_nbr,
	pat_att_id,
	pat_tkh_id
	) ON [PRIMARY]

GO


ALTER TABLE dbo.ProgressAttempt
	DROP CONSTRAINT PK_ProgressAttempt
GO

ALTER TABLE dbo.ProgressAttempt ADD atm_tkh_id int NULL
GO
update ProgressAttempt set atm_tkh_id=0
ALTER TABLE dbo.ProgressAttempt ALTER COLUMN atm_tkh_id int NOT NULL
GO
ALTER TABLE dbo.ProgressAttempt ADD CONSTRAINT
	PK_ProgressAttempt PRIMARY KEY CLUSTERED
	(
	atm_pgr_usr_id,
	atm_pgr_res_id,
	atm_pgr_attempt_nbr,
	atm_int_res_id,
	atm_int_order,
	atm_tkh_id
	) ON [PRIMARY]

GO


ALTER TABLE dbo.Accomplishment ADD
	apm_tkh_id int NULL
GO
ALTER TABLE dbo.Accomplishment
	DROP CONSTRAINT PK_Accomplishment
GO

update Accomplishment set apm_tkh_id=0
ALTER TABLE dbo.Accomplishment ALTER COLUMN apm_tkh_id int NOT NULL
GO

/*
Author: Chris Lo
Desc: Creat the tracking history from the existing course evaluation table
      Link the application to the tracking history
Date: 2003-01-20
*/

BEGIN TRAN
declare @cos_res_id int
declare @cos_itm_id int
declare @usr_ent_id int
declare @tkh_id int
declare @max_tkh_id int

DECLARE enroll_cursor CURSOR FOR
	SELECT cov_ent_id, cos_res_id, cos_itm_id
	FROM Course, CourseEvaluation
	Where cov_cos_id = cos_res_id
	and cov_tkh_id = 0
	ORDER BY cos_res_id

OPEN enroll_cursor

FETCH NEXT FROM enroll_cursor
	INTO @usr_ent_id, @cos_res_id, @cos_itm_id

WHILE @@FETCH_STATUS = 0
BEGIN
	insert into TrackingHistory (tkh_type, tkh_usr_ent_id, tkh_cos_res_id, tkh_create_timestamp) values
		('APP', @usr_ent_id, @cos_res_id, getdate())
	select @max_tkh_id = max(tkh_id) from TrackingHistory
	update aeApplication set app_tkh_id=@max_tkh_id where app_itm_id=@cos_itm_id and app_ent_id = @usr_ent_id

	FETCH NEXT FROM enroll_cursor
		INTO @usr_ent_id, @cos_res_id, @cos_itm_id
END
CLOSE enroll_cursor
DEALLOCATE enroll_cursor
COMMIT

/*
Author: Chris Lo
Desc: Update the tracking id in CourseEvaluation, ModuleEvaluation,
      Accomplishment, Progress, ProgressAttempt, ProgressAttachment
Date: 2003-01-20
*/

BEGIN TRAN
declare @cos_res_id int
declare @usr_id varchar(20)
declare @usr_ent_id int
declare @tkh_id int
declare @mod_res_id int
declare @obj_id int

DECLARE tracking_cursor CURSOR FOR
	SELECT tkh_id, tkh_usr_ent_id, usr_id,  tkh_cos_res_id
	FROM TrackingHistory, RegUser
	Where usr_ent_id = tkh_usr_ent_id
	and tkh_type='APP'
	ORDER BY tkh_cos_res_id

OPEN tracking_cursor

FETCH NEXT FROM tracking_cursor
	INTO  @tkh_id, @usr_ent_id, @usr_id, @cos_res_id

WHILE @@FETCH_STATUS = 0
BEGIN

	update CourseEvaluation set cov_tkh_id = @tkh_id where cov_cos_id = @cos_res_id and cov_ent_id=@usr_ent_id and cov_tkh_id = 0

	DECLARE module_cursor CURSOR FOR
	SELECT rcn_res_id_content From ResourceContent where rcn_res_id = @cos_res_id Order by rcn_res_id_content

	OPEN module_cursor
	FETCH NEXT FROM module_cursor INTO @mod_res_id
	WHILE @@FETCH_STATUS = 0
	BEGIN
		update ModuleEvaluation set mov_tkh_id = @tkh_id where mov_mod_id = @mod_res_id and mov_ent_id=@usr_ent_id and mov_tkh_id = 0
		update Progress set pgr_tkh_id = @tkh_id where pgr_res_id = @mod_res_id and pgr_usr_id=@usr_id and pgr_tkh_id = 0
		update ProgressAttempt set atm_tkh_id = @tkh_id where atm_pgr_res_id = @mod_res_id and atm_pgr_usr_id=@usr_id and atm_tkh_id = 0
		update ProgressAttachment set pat_tkh_id = @tkh_id where pat_prg_res_id = @mod_res_id and pat_prg_usr_id=@usr_id and pat_tkh_id = 0


		DECLARE objective_cursor CURSOR FOR
		SELECT rob_obj_id From ResourceObjective where rob_res_id = @mod_res_id Order by rob_obj_id

		OPEN objective_cursor
		FETCH NEXT FROM objective_cursor INTO @obj_id
		WHILE @@FETCH_STATUS = 0
		BEGIN
			update Accomplishment set apm_tkh_id = @tkh_id where apm_ent_id  = @usr_ent_id and apm_obj_id=@obj_id and apm_tkh_id = 0

			FETCH NEXT FROM objective_cursor INTO @obj_id
		END
		CLOSE objective_cursor
		DEALLOCATE objective_cursor

		FETCH NEXT FROM module_cursor INTO @mod_res_id
	END
	CLOSE module_cursor
	DEALLOCATE module_cursor
	FETCH NEXT FROM tracking_cursor
		INTO  @tkh_id, @usr_ent_id, @usr_id, @cos_res_id
END
CLOSE tracking_cursor
DEALLOCATE tracking_cursor

COMMIT

/*
Author: Chris Lo
Desc: Add the foriegn keys for the Progress, ProgressAttempt, ProgressAttachment
Date: 2003-01-20
*/

ALTER TABLE dbo.ProgressAttempt ADD CONSTRAINT
	FK_ProgressAttempt_Progress FOREIGN KEY
	(
	atm_pgr_usr_id,
	atm_pgr_res_id,
	atm_pgr_attempt_nbr,
	atm_tkh_id
	) REFERENCES dbo.Progress
	(
	pgr_usr_id,
	pgr_res_id,
	pgr_attempt_nbr,
	pgr_tkh_id
	)
GO

ALTER TABLE dbo.ProgressAttachment ADD CONSTRAINT
	FK_ProgressAttachment_Progress FOREIGN KEY
	(
	pat_prg_usr_id,
	pat_prg_res_id,
	pat_prg_attempt_nbr,
	pat_tkh_id
	) REFERENCES dbo.Progress
	(
	pgr_usr_id,
	pgr_res_id,
	pgr_attempt_nbr,
	pgr_tkh_id
	)
GO

/*
Author: Chris Lo
Desc: Defined the method of saving tracking history
	  'COMBINED'  means same tracking data for course retake
	  'SEPARTED'  means new tracking data for course retake
Date: 2003-01-20
*/

ALTER TABLE dbo.aeItemType ADD
	ity_tkh_method varchar(20) NULL
GO

Update aeItemType set ity_tkh_method='COMBINED'


/*
Author: Chris Lo
Desc: Add an indicator to indicate if the item can be quick referenced by learners
	  Define the type of modules that can be quick referenced
Date: 2003-01-20
*/

ALTER TABLE dbo.aeItem ADD
	itm_can_qr_ind bit NULL
GO

ALTER TABLE dbo.acSite ADD
	ste_qr_mod_types varchar(200) NULL
GO

update acSite set ste_qr_mod_types='EXC~VOD~LCT~TUT~RDG~AICC_AU~REF~GLO~NETG_COK~FOR~FAQ~EVT~GAG'

/*^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
Author: Chris
Date: 2003-01-20
Desc: Quick Reference ROLL TO CORE DB CHANGES END HERE
****************************************************************/

/*
Author: Kim
Desc: Increase the lenght of itm_title
Date: 2003-01-20
*/

alter table aeItem alter column itm_title nvarchar (255) null

/*
Author: Kim
Desc: New message for course reminder (late attend)
Date: 2003-01-20
*/

insert into xslTemplate values ('COURSE_LATE_ATTEND_REMINDER',  'HTML', 'SMTP', NULL, 'course_late_attend_reminder.xsl', 'http://host:port/servlet/Dispatcher?module=course.CourseCriteriaModule&', 1, null)
insert into xslParamName
select xtp_id , 1, 'cmd' from xslTemplate where xtp_type = 'COURSE_LATE_ATTEND_REMINDER'
insert into xslParamName
select xtp_id , 2, 'ent_id' from xslTemplate where xtp_type = 'COURSE_LATE_ATTEND_REMINDER'
insert into xslParamName
select xtp_id , 3, 'title' from xslTemplate where xtp_type = 'COURSE_LATE_ATTEND_REMINDER'
insert into xslParamName
select xtp_id , 4, 'appn_date' from xslTemplate where xtp_type = 'COURSE_LATE_ATTEND_REMINDER'
insert into xslParamName
select xtp_id , 5, 'last_date' from xslTemplate where xtp_type = 'COURSE_LATE_ATTEND_REMINDER'
insert into xslParamName
select xtp_id , 6, 'sender_id' from xslTemplate where xtp_type = 'COURSE_LATE_ATTEND_REMINDER'


/*
Author : Lun
Desc: New message for eLibrary
Date: 2003-01-23
*/
Insert into xslTemplate Values ( 'CHECKOUT_NOTIFICATION', 'HTML', 'SMTP', null, 'km_lib_checkout_notification.xsl', 'http://host:port/servlet/Dispatcher?env=wizb&module=km.library.KMLibraryModule&', 0, '$data Available / 1wˉd??y / ??g?v·?w￥i-?\')

declare @xtp_id int
select @xtp_id = max(xtp_id) from xslTemplate
Insert into xslParamName Values ( @xtp_id, 1, 'cmd' )
Insert into xslParamName Values ( @xtp_id, 2, 'usr_ent_id' )
Insert into xslParamName Values ( @xtp_id, 3, 'sender_id' )
Insert into xslParamName Values ( @xtp_id, 4, 'lob_id' )
Insert into xslParamName Values ( @xtp_id, 5, 'site_id' )

select @xtp_id = xtp_id from xslTemplate where xtp_type = 'RESERVE_NOTIFICATION'
Insert into xslParamName Values ( @xtp_id, 5, 'site_id' )

select @xtp_id = xtp_id from xslTemplate where xtp_type = 'BORROW_NOTIFICATION'
Insert into xslParamName Values ( @xtp_id, 5, 'site_id' )

Update xslTemplate Set xtp_title = 'Reserve Library Item Request' where xtp_type = 'RESERVE_NOTIFICATION'
Update xslTemplate Set xtp_title = 'Borrow Library Item Request ' where xtp_type = 'BORROW_NOTIFICATION'

/*
Author : Kim
Desc: move column ent_rol_rol_ind to usr_syn_rol_ind
	add column usr_not_syn_gpm_type to store the gpm type that not want to change during synchronization
Date: 2003-01-30
*/

alter table reguser add usr_syn_rol_ind bit  null
go
UPDATE RegUser
SET usr_syn_rol_ind =
(SELECT ent_syn_rol_ind FROM Entity WHERE ent_id = usr_ent_id)
go
alter table reguser alter column usr_syn_rol_ind bit not null

alter table Entity drop column ent_syn_rol_ind
go

alter table RegUser add usr_not_syn_gpm_type varchar (50)

/*
Author : Kim
Desc: add UserClassification
Date: 2003-01-30
*/
CREATE TABLE [dbo].[UserClassification] (
 [ucf_ent_id] [int] NOT NULL ,
 [ucf_display_bil] [nvarchar] (255) NOT NULL ,
 [ucf_ent_id_root] [int] NOT NULL ,
 [ucf_type] [varchar] (50) NULL ,
 [ucf_seq_no] [int] NULL
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[UserClassification] WITH NOCHECK ADD
 CONSTRAINT [PK_UserExtGroup] PRIMARY KEY  NONCLUSTERED
 (
  [ucf_ent_id]
 ) WITH  FILLFACTOR = 90  ON [PRIMARY]
GO

ALTER TABLE [dbo].[UserClassification] ADD
 CONSTRAINT [FK_UserClassification_Entity] FOREIGN KEY
 (
  [ucf_ent_id]
 ) REFERENCES [dbo].[Entity] (
  [ent_id]
 )
GO

/*
run when needed
add the root for user classification
*/
/*
declare @max_ent_id int
declare @ste_ent_id int
set @ste_ent_id=1
insert into Entity (ent_type, ent_upd_date, ent_syn_date, ent_ste_uid, ent_syn_ind)
values ('CLASS1', getdate(), null, 'ROOT', 0)
select @max_ent_id = max(ent_id) from Entity
insert into UserClassification (ucf_ent_id, ucf_display_bil, ucf_ent_id_root, ucf_type)
values (@max_ent_id, 'All User Classification 1', @ste_ent_id, 'ROOT')
*/


/*
Author : Chris
Desc: Messaging Enhancement
Date: 2003-02-13
*/
insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
select xtp_id, 7, 'app_id' from xslTemplate where xtp_type='ENROLLMENT_NOT_APPROVED' and xtp_subtype='HTML'
insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
select xtp_id, 11,  'cc_ent_ids' from xslTemplate where xtp_type='ENROLLMENT_CONFIRMED' and xtp_subtype='HTML'
insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
select xtp_id, 11,  'cc_ent_ids' from xslTemplate where xtp_type='ENROLLMENT_NOT_CONFIRMED'  and xtp_subtype='HTML'
insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
select xtp_id, 10,  'cc_ent_ids' from xslTemplate where xtp_type='ENROLLMENT_WITHDRAWAL_REQUEST'  and xtp_subtype='HTML'

/*
Author : Chris
Desc: Storing the type of message are sent on which type of object
Date: 2003-02-13
*/
CREATE TABLE dbo.NotificationHistory
	(
	nfh_target_id int NOT NULL,
	nfh_target_type varchar(50) NOT NULL,
	nfh_type varchar (100) NOT NULL,
	nfh_sent_timestamp datetime NOT NULL
	)  ON [PRIMARY]
GO

ALTER TABLE dbo.NotificationHistory ADD CONSTRAINT
	PK_TNotificationHistory PRIMARY KEY CLUSTERED
	(
	nfh_target_id,
	nfh_target_type,
	nfh_type
	) ON [PRIMARY]

GO

/*
Author : Chris
Desc: Add an identity column to the table aeItemReqDueDate
Date: 2003-02-13
*/
ALTER TABLE dbo.aeItemReqDueDate
	DROP CONSTRAINT FK_aeItemReqDueDate_aeItemRequirement
GO

ALTER TABLE dbo.aeItemReqDueDate
	DROP CONSTRAINT FK_aeItemReqDueDate_aeItem
GO

CREATE TABLE dbo.Tmp_aeItemReqDueDate
	(
	ird_id int NOT NULL IDENTITY (1, 1),
	ird_itr_itm_id int NOT NULL,
	ird_itr_order int NOT NULL,
	ird_child_itm_id int NOT NULL,
	ird_requirement_due_date datetime NOT NULL
	)  ON [PRIMARY]
GO
SET IDENTITY_INSERT dbo.Tmp_aeItemReqDueDate OFF
GO
IF EXISTS(SELECT * FROM dbo.aeItemReqDueDate)
	 EXEC('INSERT INTO dbo.Tmp_aeItemReqDueDate (ird_itr_itm_id, ird_itr_order, ird_child_itm_id, ird_requirement_due_date)
		SELECT ird_itr_itm_id, ird_itr_order, ird_child_itm_id, ird_requirement_due_date FROM dbo.aeItemReqDueDate TABLOCKX')
GO
DROP TABLE dbo.aeItemReqDueDate
GO
EXECUTE sp_rename N'dbo.Tmp_aeItemReqDueDate', N'aeItemReqDueDate', 'OBJECT'
GO
ALTER TABLE dbo.aeItemReqDueDate ADD CONSTRAINT
	PK_aeItemReqDueDate PRIMARY KEY CLUSTERED
	(
	ird_itr_itm_id,
	ird_itr_order,
	ird_child_itm_id
	) ON [PRIMARY]

GO
ALTER TABLE dbo.aeItemReqDueDate WITH NOCHECK ADD CONSTRAINT
	FK_aeItemReqDueDate_aeItem FOREIGN KEY
	(
	ird_child_itm_id
	) REFERENCES dbo.aeItem
	(
	itm_id
	)
GO
ALTER TABLE dbo.aeItemReqDueDate WITH NOCHECK ADD CONSTRAINT
	FK_aeItemReqDueDate_aeItemRequirement FOREIGN KEY
	(
	ird_itr_itm_id,
	ird_itr_order
	) REFERENCES dbo.aeItemRequirement
	(
	itr_itm_id,
	itr_order
	)
GO


/*
Author : Chris
Desc: Updating the tracking id of the application which is class-level
	  This is a fix for the scripts dated 2003-01-20 , Line 5261
Date: 2003-02-13
*/
BEGIN TRAN
declare @cos_res_id int
declare @cos_itm_id int
declare @usr_ent_id int
declare @tkh_id int
declare @max_tkh_id int

DECLARE enroll_cursor CURSOR FOR
 SELECT cov_tkh_id, cov_ent_id, cos_res_id, cos_itm_id
 FROM Course, CourseEvaluation
 Where cov_cos_id = cos_res_id
 and cov_tkh_id > 0
 ORDER BY cos_res_id

OPEN enroll_cursor

FETCH NEXT FROM enroll_cursor
 INTO @tkh_id, @usr_ent_id, @cos_res_id, @cos_itm_id

WHILE @@FETCH_STATUS = 0
BEGIN

 update aeApplication set app_tkh_id=@tkh_id From aeApplication, aeItemRelation where
  app_itm_id=ire_child_itm_id and ire_parent_itm_id=@cos_itm_id and app_ent_id = @usr_ent_id

 FETCH NEXT FROM enroll_cursor
  INTO @tkh_id, @usr_ent_id, @cos_res_id, @cos_itm_id
END
CLOSE enroll_cursor
DEALLOCATE enroll_cursor

COMMIT

/*
Author: Fai
Date: 2003-02-14
Desc: alter entity table to support soft delete
*/
ALTER TABLE entity ADD ent_delete_usr_id varchar(20)
GO
ALTER TABLE entity ADD ent_delete_timestamp datetime
GO

/*
Author: Fai
Date: 2003-02-14
Desc: transfer all user in lost&found to soft delete
*/
declare @usg_ent_id int
select @usg_ent_id = usg_ent_id from userGroup where usg_display_bil ='LOST&FOUND'

update entity set ent_delete_usr_id = 's1u3', ent_delete_timestamp = getDate() where ent_id in
(
select gpm_ent_id_member from groupMember where gpm_ent_id_group=@usg_ent_id and gpm_type='USR_PARENT_USG' and
gpm_ent_id_member in (select usr_ent_id from regUser where usr_status='DELETED')
)

/*
Author: Fai
Date: 2003-02-14
Desc: alter groupmember table to store historical data
	add column gpm_start_timestamp(PK), gpm_end_timestamp(PK), gpm_group_name, gpm_full_path, gpm_create_usr_id, gpm_create_timestamp
*/

ALTER TABLE groupmember ADD gpm_start_timestamp datetime NULL
GO
UPDATE groupmember SET gpm_start_timestamp = '1753-01-01 00:00:00.000'
GO
ALTER TABLE groupmember ALTER COLUMN gpm_start_timestamp datetime NOT NULL
GO

ALTER TABLE groupmember ADD gpm_end_timestamp datetime NULL
GO
UPDATE groupmember SET gpm_end_timestamp = '9999-12-31 23:59:59.000'
GO
ALTER TABLE groupmember ALTER COLUMN gpm_end_timestamp datetime NOT NULL
GO

ALTER TABLE groupmember ADD gpm_group_name nvarchar(60) NULL
GO
DECLARE cur_grp_name CURSOR for
SELECT gpm_ent_id_group, idc_display_bil FROM groupmember, entity, industrycode where gpm_ent_id_group=ent_id and ent_type='IDC' and ent_id = idc_ent_id
DECLARE @group_name nvarchar(60)
DECLARE @group_id int
OPEN cur_grp_name
FETCH next from cur_grp_name into @group_id, @group_name

WHILE @@FETCH_STATUS = 0
BEGIN
	update groupmember set gpm_group_name = @group_name where gpm_ent_id_group = @group_id
	fetch next from cur_grp_name into @group_id, @group_name
END
CLOSE cur_grp_name
DEALLOCATE cur_grp_name

DECLARE cur_grp_name CURSOR for
SELECT gpm_ent_id_group, usg_display_bil FROM groupmember, entity, usergroup where gpm_ent_id_group=ent_id and ent_type='USG' and ent_id = usg_ent_id
DECLARE @group_name nvarchar(60)
DECLARE @group_id int
OPEN cur_grp_name
FETCH next from cur_grp_name into @group_id, @group_name

WHILE @@FETCH_STATUS = 0
BEGIN
	update groupmember set gpm_group_name = @group_name where gpm_ent_id_group = @group_id
	fetch next from cur_grp_name into @group_id, @group_name
END
CLOSE cur_grp_name
DEALLOCATE cur_grp_name

DECLARE cur_grp_name CURSOR for
SELECT gpm_ent_id_group, ugr_display_bil FROM groupmember, entity, usergrade where gpm_ent_id_group=ent_id and ent_type='UGR' and ent_id = ugr_ent_id
DECLARE @group_name nvarchar(60)
DECLARE @group_id int
OPEN cur_grp_name
FETCH next from cur_grp_name into @group_id, @group_name

WHILE @@FETCH_STATUS = 0
BEGIN
	update groupmember set gpm_group_name = @group_name where gpm_ent_id_group = @group_id
	fetch next from cur_grp_name into @group_id, @group_name
END
CLOSE cur_grp_name
DEALLOCATE cur_grp_name

ALTER TABLE groupmember ALTER COLUMN gpm_group_name nvarchar(60) NOT NULL
GO
ALTER TABLE groupmember ADD gpm_full_path nvarchar(4000) NULL
GO
UPDATE groupmember set gpm_full_path = gpm_ancester + ', ' + convert(varchar(20), gpm_ent_id_member) + ' '

DECLARE cur_grp_name CURSOR for
select ent_id, usg_display_bil as display_bil from entity, usergroup where ent_id=usg_ent_id union select ent_id, ugr_display_bil as display_bil from entity, usergrade where ent_id=ugr_ent_id union select ent_id, idc_display_bil as display_bil from entity, industrycode where ent_id=idc_ent_id union select ent_id, usr_display_bil as display_bil from entity, regUser where ent_id=usr_ent_id
DECLARE @group_name nvarchar(60)
DECLARE @group_id int
OPEN cur_grp_name
FETCH next from cur_grp_name into @group_id, @group_name

WHILE @@FETCH_STATUS = 0
BEGIN
	update groupmember set gpm_full_path = replace(gpm_full_path, ' '+convert(varchar(20),@group_id)+' ', ' '+@group_name+' ')
	fetch next from cur_grp_name into @group_id, @group_name
END
CLOSE cur_grp_name
DEALLOCATE cur_grp_name
GO
UPDATE groupmember set gpm_full_path = replace (gpm_full_path, ' , ', '/')
GO

ALTER TABLE groupmember ADD gpm_create_usr_id varchar(20) NULL
GO
UPDATE groupmember set gpm_create_usr_id = 's1u3'
GO
ALTER TABLE groupmember ALTER COLUMN gpm_create_usr_id varchar(20) NOT NULL
GO

ALTER TABLE groupmember ADD gpm_create_timestamp datetime NULL
GO
UPDATE groupmember set gpm_create_timestamp = getDate()
GO
ALTER TABLE groupmember ALTER COLUMN gpm_create_timestamp datetime NOT NULL
GO

ALTER TABLE groupmember DROP CONSTRAINT PK_groupmember
GO
ALTER TABLE groupmember WITH NOCHECK ADD
 CONSTRAINT [PK_groupmember] PRIMARY KEY  NONCLUSTERED
 (
  [gpm_ent_id_group],
  [gpm_ent_id_member],
  [gpm_start_timestamp],
  [gpm_end_timestamp],
  [gpm_type]
 ) WITH  FILLFACTOR = 90  ON [PRIMARY]
GO

declare @usg_ent_id int
select @usg_ent_id = usg_ent_id from userGroup where usg_display_bil ='LOST&FOUND'

update groupMember set gpm_end_timestamp = getDate() where gpm_type='USR_CURRENT_UGR' and
gpm_ent_id_member in (select usr_ent_id from regUser where usr_status='DELETED')

update groupMember set gpm_end_timestamp = getDate() where gpm_ent_id_group=@usg_ent_id and gpm_type='USR_PARENT_USG' and
gpm_ent_id_member in (select usr_ent_id from regUser where usr_status='DELETED')

update groupMember set gpm_ent_id_member = 1, gpm_group_name = 'All User Groups',
gpm_full_path = replace(gpm_full_path, 'LOST&FOUND','All User Groups')
where gpm_ent_id_group=@usg_ent_id and gpm_type='USR_PARENT_USG' and
gpm_ent_id_member in (select usr_ent_id from regUser where usr_status='DELETED')

delete from groupMember where gpm_ent_id_member = @usg_ent_id
/*
Author: Fai
Desc: New system message for user account restore
Date: 2003-02-14
*/
insert into systemMessage (sms_id, sms_lan, sms_desc) values ('USR019', 'Big5', '!!!User account is successfully restored.')
insert into systemMessage (sms_id, sms_lan, sms_desc) values ('USR019', 'GB2312', '!!!User account is successfully restored.')
insert into systemMessage (sms_id, sms_lan, sms_desc) values ('USR019', 'ISO-8859-1', 'User account is successfully restored.')

insert into systemMessage (sms_id, sms_lan, sms_desc) values ('USR020', 'Big5', '!!!User group does not exist. User account cannot be restored.')
insert into systemMessage (sms_id, sms_lan, sms_desc) values ('USR020', 'GB2312', '!!!User group does not exist. User account cannot be restored.')
insert into systemMessage (sms_id, sms_lan, sms_desc) values ('USR020', 'ISO-8859-1', 'User group does not exist. User account cannot be restored.')

/*
Author : Fai
Desc: add Module Evaluation History
Date: 2003-02-19
*/
CREATE TABLE [dbo].[ModuleEvaluationHistory] (
 [mvh_ent_id] [int] NOT NULL ,
 [mvh_mod_id] [int] NOT NULL ,
 [mvh_last_acc_datetime] [datetime] NOT NULL ,
 [mvh_tkh_id] [int] NOT NULL ,
 [mvh_ele_loc] [nvarchar] (255) ,
 [mvh_total_attempt]	[int] NOT NULL,
 [mvh_total_time] [float] NOT NULL ,
 [mvh_status] [varchar] (10) ,
 [mvh_score] [decimal] (18,4) ,
 [mvh_create_usr_id] [varchar] (20) NOT NULL ,
 [mvh_create_timestamp] [datetime] NOT NULL
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[ModuleEvaluationHistory] WITH NOCHECK ADD
 CONSTRAINT [PK_ModEvaHistory] PRIMARY KEY  NONCLUSTERED
 (
 [mvh_ent_id],
 [mvh_mod_id],
 [mvh_last_acc_datetime],
 [mvh_tkh_id]
 ) WITH  FILLFACTOR = 90  ON [PRIMARY]
GO

ALTER TABLE [dbo].[ModuleEvaluationHistory] ADD
 CONSTRAINT [FK_ModEvaHistory_ent_id] FOREIGN KEY
 (
  [mvh_ent_id]
 ) REFERENCES [dbo].[Entity] (
  [ent_id]
 )
GO

ALTER TABLE [dbo].[ModuleEvaluationHistory] ADD
 CONSTRAINT [FK_ModEvaHistory_mod_id] FOREIGN KEY
 (
  [mvh_mod_id]
 ) REFERENCES [dbo].[Resources] (
  [res_id]
 )
GO

ALTER TABLE [dbo].[ModuleEvaluationHistory] ADD
 CONSTRAINT [FK_ModEvaHistory_tkh_id] FOREIGN KEY
 (
  [mvh_tkh_id]
 ) REFERENCES [dbo].[TrackingHistory] (
  [tkh_id]
 )
GO

/*
Author : Kim
Desc: User Exemption, divide Exemption into course exemption and user exemption
Date: 2003-02-21
*/
update aeItemRequirement set  itr_requirement_subtype = 'COURSE' WHERE itr_requirement_type = 'EXEMPTION' and itr_requirement_subtype ='ENROLLMENT'

/*
Author : Kim
Desc: Add UserClassificationType to store exemption_ind, which indicate it can be choose in defining User Exemption
Date: 2003-02-21
*/
Create table [dbo].[UserClassificationType](
[uct_type]  [varchar] (50) NOT NULL ,
[uct_ent_id_root] [int] not null,
[uct_exemption_ind] [int] not null,
[uct_create_timestamp] [datetime] not null
) ON [PRIMARY]
GO


ALTER TABLE [dbo].[UserClassificationType]  WITH NOCHECK ADD
 CONSTRAINT [PK_UserClassificationType] PRIMARY KEY  NONCLUSTERED
 (
  [uct_type]
 ) WITH  FILLFACTOR = 90  ON [PRIMARY]
GO

/*
Author : Kim
Desc: Assign all ClassificationType to the table UserClassificationType , assume all classification type can be choose from User Exemption
	RUN IF NEEDED
Date: 2003-02-21
*/

/*
insert into UserClassificationType
select distinct ent_type, 1, 1, getDate() from UserClassification, entity  where ent_id = ucf_ent_id
*/

/*
Author : Kim
Desc: Add table aeItemTreeNodePath to store the full treenode path for item
	Notice: itm_id is NOT a foreign key to aeItem in order to avoid key error when deleting the record in aeItem
Date: 2003-02-21
*/
CREATE TABLE [dbo].[aeItemTreeNodePath] (
 [inp_itm_id] [int] NOT NULL ,
 [inp_full_path] [nvarchar] (2000) NOT NULL ,
 [inp_create_timestamp] [datetime] NOT NULL
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[aeItemTreeNodePath]  WITH NOCHECK ADD
 CONSTRAINT [PK_aeItemTreeNodePath] PRIMARY KEY  NONCLUSTERED
 (
  [inp_itm_id]
 ) WITH  FILLFACTOR = 90  ON [PRIMARY]
GO

/*
Author : Lun
Desc: Add a new target report and assign role TADM_1 have permission to read the report
Date: 2003-02-26
*/
Insert Into ReportTemplate Values (
'<title>
	<desc lan="ISO-8859-1" name="Targeted Learner Report Template"/>
	<desc lan="Big5" name="!!!Targeted Learner Report Template"/>
	<desc lan="GB2312" name="!!!Targeted Learner Report Template"/>
</title>'
, 'TARGET_LEARNER', 'rpt_target_lrn_srh.xsl',
'rpt_target_lrn_res.xsl', 'rpt_dl_target_lrn_xls.xsl', null, 3, 1, 's1u3', getDate(), 's1u3', getDate())

declare @rte_id int
Select @rte_id = Max(rte_id) From ReportTemplate
Insert into acReportTemplate Values ( @rte_id, null, 'TADM_1', 'RTE_READ', 0, 's1u3', getDate())

Insert Into ObjectView Values ( 1, 'TARGET_LEARNER_REPORT', 'USR',
'<object_view>
	<attribute>usr_id</attribute>
	<attribute>usr_ent_id</attribute>
	<attribute>usr_pwd</attribute>
	<attribute>usr_email</attribute>
	<attribute>usr_email_2</attribute>
	<attribute>usr_full_name_bil</attribute>
	<attribute>usr_initial_name_bil</attribute>
	<attribute>usr_last_name_bil</attribute>
	<attribute>usr_first_name_bil</attribute>
	<attribute>usr_display_bil</attribute>
	<attribute>usr_gender</attribute>
	<attribute>usr_bday</attribute>
	<attribute>usr_bplace_bil</attribute>
	<attribute>usr_hkid</attribute>
	<attribute>usr_other_id_no</attribute>
	<attribute>usr_other_id_type</attribute>
	<attribute>usr_tel_1</attribute>
	<attribute>usr_tel_2</attribute>
	<attribute>usr_fax_1</attribute>
	<attribute>usr_country_bil</attribute>
	<attribute>usr_postal_code_bil</attribute>
	<attribute>usr_state_bil</attribute>
	<attribute>usr_city_bil</attribute>
	<attribute>usr_address_bil</attribute>
	<attribute>usr_occupation_bil</attribute>
	<attribute>usr_income_level</attribute>
	<attribute>usr_edu_role</attribute>
	<attribute>usr_edu_level</attribute>
	<attribute>usr_school_bil</attribute>
	<attribute>usr_class</attribute>
	<attribute>usr_class_number</attribute>
	<attribute>usr_signup_date</attribute>
	<attribute>usr_special_date_1</attribute>
	<attribute>usr_status</attribute>
	<attribute>usr_upd_date</attribute>
	<attribute>usr_extra_1</attribute>
	<attribute>usr_cost_center</attribute>
	<attribute>usr_extra_2</attribute>
	<attribute>usr_extra_3</attribute>
	<attribute>usr_extra_4</attribute>
	<attribute>usr_extra_5</attribute>
	<attribute>usr_extra_6</attribute>
	<attribute>usr_extra_7</attribute>
	<attribute>usr_extra_8</attribute>
	<attribute>usr_extra_9</attribute>
	<attribute>usr_extra_10</attribute>
</object_view>',
's1u3', getDate(),'s1u3', getDate() )

Insert Into ObjectView Values ( 1, 'TARGET_LEARNER_REPORT', 'USR_CLASSIFY',
'<object_view>
	<attribute>USR_PARENT_USG</attribute>
	<attribute>USR_CURRENT_UGR</attribute>
	<attribute>IDC_PARENT_IDC</attribute>
</object_view>',
's1u3', getDate(),'s1u3', getDate() )



/*
Author : Lun
Desc: Add table for item figure
Date: 2003-02-26
*/
CREATE TABLE [dbo].[aeAttendanceFigureType] (
	[afg_ats_id] [int] NOT NULL ,
	[afg_fgt_id] [int] NOT NULL ,
	[afg_multiplier] [decimal](18, 2) NOT NULL
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[aeFigure] (
	[fig_id] [int] IDENTITY (1, 1) NOT NULL ,
	[fig_fgt_id] [int] NOT NULL ,
	[fig_value] [decimal](18, 2) NOT NULL ,
	[fig_update_usr_id] [varchar] (20) NOT NULL ,
	[fig_update_timestamp] [datetime] NOT NULL ,
	[fig_create_usr_id] [varchar] (20) NOT NULL ,
	[fig_create_timestamp] [datetime] NOT NULL
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[aeFigureType] (
	[fgt_id] [int] IDENTITY (1, 1) NOT NULL ,
	[fgt_type] [varchar] (50) NOT NULL ,
	[fgt_subtype] [varchar] (50) NOT NULL ,
	[fgt_owner_ent_id] [int] NOT NULL ,
	[fgt_xml] [ntext] NULL
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO

CREATE TABLE [dbo].[aeItemFigure] (
	[ifg_itm_id] [int] NOT NULL ,
	[ifg_fig_id] [int] NOT NULL
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[aeUserFigure] (
	[ufg_att_app_id] [int] NOT NULL ,
	[ufg_fig_id] [int] NOT NULL
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[aeAttendanceFigureType] WITH NOCHECK ADD
	CONSTRAINT [PK_aeAttendanceFigureType] PRIMARY KEY  CLUSTERED
	(
		[afg_ats_id],
		[afg_fgt_id]
	)  ON [PRIMARY]
GO

ALTER TABLE [dbo].[aeFigure] WITH NOCHECK ADD
	CONSTRAINT [PK_aeFigure] PRIMARY KEY  CLUSTERED
	(
		[fig_id]
	)  ON [PRIMARY]
GO

ALTER TABLE [dbo].[aeFigureType] WITH NOCHECK ADD
	CONSTRAINT [PK_aeFigureType] PRIMARY KEY  CLUSTERED
	(
		[fgt_id]
	)  ON [PRIMARY]
GO

ALTER TABLE [dbo].[aeItemFigure] WITH NOCHECK ADD
	CONSTRAINT [PK_aeItemFigure] PRIMARY KEY  CLUSTERED
	(
		[ifg_itm_id],
		[ifg_fig_id]
	)  ON [PRIMARY]
GO

ALTER TABLE [dbo].[aeUserFigure] WITH NOCHECK ADD
	CONSTRAINT [PK_aeUserFigure] PRIMARY KEY  CLUSTERED
	(
		[ufg_att_app_id],
		[ufg_fig_id]
	)  ON [PRIMARY]
GO

ALTER TABLE [dbo].[aeAttendanceFigureType] ADD
	CONSTRAINT [FK_aeAttendanceFigureType_aeAttendanceStatus] FOREIGN KEY
	(
		[afg_ats_id]
	) REFERENCES [dbo].[aeAttendanceStatus] (
		[ats_id]
	),
	CONSTRAINT [FK_aeAttendanceFigureType_aeFigureType] FOREIGN KEY
	(
		[afg_fgt_id]
	) REFERENCES [dbo].[aeFigureType] (
		[fgt_id]
	)
GO

ALTER TABLE [dbo].[aeFigure] ADD
	CONSTRAINT [FK_aeFigure_aeFigureType] FOREIGN KEY
	(
		[fig_fgt_id]
	) REFERENCES [dbo].[aeFigureType] (
		[fgt_id]
	)
GO

ALTER TABLE [dbo].[aeItemFigure] ADD
	CONSTRAINT [FK_aeItemFigure_aeFigure] FOREIGN KEY
	(
		[ifg_fig_id]
	) REFERENCES [dbo].[aeFigure] (
		[fig_id]
	),
	CONSTRAINT [FK_aeItemFigure_aeItem] FOREIGN KEY
	(
		[ifg_itm_id]
	) REFERENCES [dbo].[aeItem] (
		[itm_id]
	)
GO

ALTER TABLE [dbo].[aeUserFigure] ADD
	CONSTRAINT [FK_aeUserFigure_aeFigure] FOREIGN KEY
	(
		[ufg_fig_id]
	) REFERENCES [dbo].[aeFigure] (
		[fig_id]
	)
GO

/*Insert sample record(s) to database ???
declare @fgt_id int
Insert into aeFigureType Values ( 'COST', 'MAN_DAY', 1, '<title><desc lan="ISO-8859-1" name="Man-day"/><desc lan="Big5" name="Man-day"/><desc lan="GB2312" name="Man-day"/></title>')
select @fgt_id = max(fgt_id) from aeFigureType
insert into aeAttendanceFigureType values (1, @fgt_id, 1)
insert into aeAttendanceFigureType values (2, @fgt_id, 0)
insert into aeAttendanceFigureType values (3, @fgt_id, 0)
insert into aeAttendanceFigureType values (4, @fgt_id, 0)

Insert into aeFigureType Values ( 'CREDIT', 'CREDIT', 1, '<title><desc lan="ISO-8859-1" name="Credit"/><desc lan="Big5" name="Credit"/><desc lan="GB2312" name="Credit"/></title>')
select @fgt_id = max(fgt_id) from aeFigureType
insert into aeAttendanceFigureType values (1, @fgt_id, 1)
insert into aeAttendanceFigureType values (2, @fgt_id, 0)
insert into aeAttendanceFigureType values (3, @fgt_id, 0)
insert into aeAttendanceFigureType values (4, @fgt_id, 0)
*/

/*
Author : Lun
Desc: Remove item accreditation related table
Date: 2003-02-26
*/
Delete From aeUserAccreditation
Delete From aeItemAccreditation
Delete From aeItemCreditValue
Delete From aeItemCredit
Drop Table aeUserAccreditation
Drop Table aeItemAccreditation
Drop Table aeItemCreditValue
Drop Table aeItemCredit


/*
Author : Lun
Desc: Remove column from aeTemplateView and aeItemType (use template view to configure the item to support figure)
Date: 2003-02-26
*/
alter table aeTemplateView drop column [tvw_iad_ind]
alter table aeItemType drop column [ity_iad_ind]

/*
Author:Lun
Desc:New xsl for xls report
Date:2003-02-26
*/
Update ReportTemplate Set rte_dl_xsl = 'rpt_dl_cos_xls.xsl' where rte_type = 'COURSE'
Update ReportTemplate Set rte_dl_xsl = 'rpt_dl_lrn_xls.xsl' where rte_type = 'LEARNER'
Update ReportTemplate Set rte_dl_xsl = 'rpt_dl_survey_xls.xsl' where rte_type = 'SURVEY_COS_GRP'

/*
Author : Fai
Desc: To remove the ROOT group(in all case) and itself(when the gpm_type is user related) from the gpm_full_path
	e.g. "All User Groups/Testing Group1/Student User1" -> "Testing Group1"
	     "All User Groups/Student User2" -> ""
	     "All User Groups/Testing Group2" -> "Testing Group2"
Date: 2003-02-26
*/

DECLARE cur_grp_name CURSOR for
SELECT DISTINCT gpm_ent_id_group, ucf_display_bil FROM groupmember, userClassification where gpm_ent_id_group = ucf_ent_id
DECLARE @group_name nvarchar(60)
DECLARE @group_id int
OPEN cur_grp_name
FETCH next from cur_grp_name into @group_id, @group_name
WHILE @@FETCH_STATUS = 0
BEGIN
	update groupmember set gpm_group_name = @group_name where gpm_ent_id_group = @group_id
	fetch next from cur_grp_name into @group_id, @group_name
END
CLOSE cur_grp_name
DEALLOCATE cur_grp_name

UPDATE groupmember set gpm_full_path = gpm_ancester

DECLARE cur_grp_name CURSOR for
select ent_id, usg_display_bil as display_bil from entity, usergroup where ent_id=usg_ent_id
union select ent_id, ugr_display_bil as display_bil from entity, usergrade where ent_id=ugr_ent_id
union select ent_id, idc_display_bil as display_bil from entity, industrycode where ent_id=idc_ent_id
union select ent_id, usr_display_bil as display_bil from entity, regUser where ent_id=usr_ent_id
union select ent_id, ucf_display_bil as display_bil from entity, userclassification where ent_id=ucf_ent_id
DECLARE @group_name nvarchar(60)
DECLARE @group_id int
OPEN cur_grp_name
FETCH next from cur_grp_name into @group_id, @group_name
WHILE @@FETCH_STATUS = 0
BEGIN
	update groupmember set gpm_full_path = replace(gpm_full_path, ' '+convert(varchar(20),@group_id)+' ', ' '+@group_name+' ')
	fetch next from cur_grp_name into @group_id, @group_name
END
CLOSE cur_grp_name
DEALLOCATE cur_grp_name
GO
UPDATE groupmember set gpm_full_path = replace (gpm_full_path, ' , ', '/')
GO

DECLARE cur_grp_name CURSOR for
select distinct gpm_group_name from groupMember where gpm_ent_id_group not in
(select distinct gpm_ent_id_member from groupMember)  and gpm_group_name is not null
DECLARE @group_name nvarchar(60)
OPEN cur_grp_name
FETCH next from cur_grp_name into @group_name
WHILE @@FETCH_STATUS = 0
BEGIN
	UPDATE groupmember set gpm_full_path = replace(gpm_full_path, ' '+@group_name+'/', '')
	UPDATE groupmember set gpm_full_path = replace(gpm_full_path, ' '+@group_name+' ', '')
	fetch next from cur_grp_name into @group_name
END
CLOSE cur_grp_name
DEALLOCATE cur_grp_name
GO

/*
Author : Fai
Desc: New paramname to store the app_id in the ENROLLMENT_WITHDRAWAL_REQUEST message
Date: 2003-02-26
*/
declare @xtp_id int
declare @xpn_pos int
SELECT @xtp_id = xtp_id FROM xslTemplate WHERE xtp_type='ENROLLMENT_WITHDRAWAL_REQUEST'
SELECT @xpn_pos = max(xpn_pos)+1 FROM xslparamname WHERE xpn_xtp_id = @xtp_id

INSERT INTO xslparamname VALUES (@xtp_id, @xpn_pos, 'app_id')

/*
Author : Kim
Desc: Fix incident in evaluation template
Date: 2003-02-28
*/
update resources set res_tpl_name = 'Evaluation Template' where res_subtype = 'EVN'

/*
Author : Kim
Desc: Fix incident in saving moduleEvluation history for Public Module
	For Public Module, no Tracking History record can be defined , save '0' as its tkh_id
Date: 2003-03-06
*/

ALTER TABLE [dbo].[ModuleEvaluationHistory] DROP CONSTRAINT FK_ModEvaHistory_tkh_id

/*
Author : Kim
Desc: Add column ancester to store the ancester of the item, in format " id , id [|] id , id "
Date: 2003-03-07
*/
alter table aeitemtreenodepath add inp_ancester varchar (255)

/*
Author : Fai
Desc: New system message for quick reference
Date: 2003-03-19
*/
insert SystemMessage values ('AEIT17', 'ISO-8859-1', 'Settings are updated successfully.')
insert SystemMessage values ('AEIT17', 'Big5', '!!!Settings are updated successfully.')
insert SystemMessage values ('AEIT17', 'GB2312', '!!!Settings are updated successfully.')

/*
Author : Kim Chan
Desc: For enhancement in upload enrollment and upload user
Date: 2003-04-28
*/
CREATE TABLE [dbo].[IMSLog] (
 [ilg_id] [int] IDENTITY (1, 1) NOT NULL ,
 [ilg_method] [varchar] (20) NOT NULL ,
 [ilg_type] [varchar] (20) NOT NULL ,
 [ilg_process] [varchar] (20) NOT NULL ,
 [ilg_filename] [nvarchar] (50) NULL ,
 [ilg_desc] [nvarchar] (1000) NULL ,
 [ilg_create_usr_id] [varchar] (20) NOT NULL ,
 [ilg_create_timestamp] [datetime] NOT NULL
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[IMSLog] WITH NOCHECK ADD
 CONSTRAINT [PK_IMSLog] PRIMARY KEY  CLUSTERED
 (
  [ilg_id]
 )  ON [PRIMARY]
GO

Insert Into acFunction Values ( 'IMS_DATA_MGT', 'FUNCTION', 'HOMEPAGE', getDate(),
'<function id="IMS_DATA_MGT">
<desc lan="ISO-8859-1" name="System Data Import/Export"/>
<desc lan="Big5" name="System Data Import/Export"/>
<desc lan="GB2312" name="System Data Import/Export"/>
</function>')

declare @ftn_id int
select @ftn_id = max(ftn_id) from acFunction
Insert into acRoleFunction Values ( 1, @ftn_id, getDate())

alter table aefiguretype add fgt_seq_id int null
go
update aefiguretype set fgt_seq_id = fgt_id
go
alter table aefiguretype alter column fgt_seq_id int not null
go

/*
if upload enrollment is needed, update the workflow
*/

/*
Author : Lun
Desc: Add and remove column from table UploadLog, for enhancement in upload question
Date: 2003-04-28
*/
Alter table UploadLog drop column [ulg_path]

Alter table UploadLog drop column [ulg_file_lastmod]

Alter table UploadLog drop column [ulg_file_size]

Alter table UploadLog drop column [ulg_rec_cnt]

Alter table UploadLog drop column [ulg_success_cnt]

Alter table UploadLog add ulg_desc ntext null

Alter table UploadLog add ulg_process varchar(20) null
Update UploadLog set ulg_process = 'IMPORT'
Alter table UploadLog alter column ulg_process varchar(20) not null

ALTER TABLE UploadLog ADD ulg_method varchar(20) null
Update UploadLog set ulg_method = 'UI'
Alter table UploadLog alter column ulg_method varchar(20) not null

/*
Author : kim
Desc: translate systemmessage
Date: 2003-05-16
*/
update SystemMessage	set sms_desc = '鐢ㄦ埛瀵嗙爜宸蹭慨鏀规垚鍔熴? where	sms_id='USR016' and sms_lan='GB2312'
update SystemMessage	set sms_desc = '鏃у瘑鐮佷笉姝ｇ‘銆? where	sms_id='USR017' and sms_lan='GB2312'
update SystemMessage	set sms_desc = '鐢ㄦ埛甯愬彿宸叉縺娲汇€? where	sms_id='USR018' and sms_lan='GB2312'

/*
Author : kim
Desc: learner report template for standard deploy
Date: 2003-05-19
*/
update ObjectView set ojv_option_xml = '
<object_view>
	<attribute>usr_id</attribute>
	<attribute checked="yes">usr_display_bil</attribute>
	<attribute checked="yes">USR_PARENT_USG</attribute>
	<attribute>USR_CURRENT_UGR</attribute>
	<attribute>usr_email</attribute>
	<attribute>usr_tel_1</attribute>
</object_view>'
where ojv_type = 'LEARNER_REPORT' and ojv_subtype = 'USR'

update ObjectView set ojv_option_xml = '
<object_view>
	<attribute>cov_last_acc_datetime</attribute>
	<attribute>cov_commence_datetime</attribute>
	<attribute>total_attempt</attribute>
	<attribute>cov_total_time</attribute>
	<attribute>att_status</attribute>
	<attribute>att_timestamp</attribute>
	<attribute>cov_score</attribute>
</object_view>'
where ojv_type = 'LEARNER_REPORT' and ojv_subtype = 'OTHER'


update ObjectView set ojv_option_xml = '
<template_view>
	<section id="1">
		<field05 marked="no">
			<title>
				<desc lan="ISO-8859-1" name="Course ID"/>
				<desc lan="Big5" name="課程編號"/>
				<desc lan="GB2312" name="课程 ID"/>
			</title>
		</field05>
		<field02 marked="no" checked="yes">
			<title>
				<desc lan="ISO-8859-1" name="Course Title"/>
				<desc lan="Big5" name="課程標題"/>
				<desc lan="GB2312" name="课程名称"/>
			</title>
		</field02>
		<field01 marked="no" ext_value_label_tag="item_type_list" checked="yes">
			<title>
				<desc lan="ISO-8859-1" name="Course Type"/>
				<desc lan="Big5" name="課程類型"/>
				<desc lan="GB2312" name="课程类型"/>
			</title>
		</field01>
		<catalog marked="no" type="catalog_attachment" paramname="tnd_id_lst" required="yes" checked="yes">
			<title>
				<desc lan="ISO-8859-1" name="Categories"/>
				<desc lan="Big5" name="瀛愮洰閷?/>
				<desc lan="GB2312" name="类别"/>
			</title>
		</catalog>
	</section>
</template_view>'
where ojv_type = 'LEARNER_REPORT' and ojv_subtype = 'ITM'

delete from ObjectView
where ojv_type = 'LEARNER_REPORT' and ojv_subtype = 'RUN'

update ReportTemplate set rte_title_xml = '
<title>	<desc lan="ISO-8859-1" name="Learner Activity Report"/>	<desc lan="Big5" name="!!!Learner Activity Report"/>	<desc lan="GB2312" name="!!!Learner Activity Report"/></title>'
where rte_id = 2

update aeAttendanceStatus set ats_title_xml = '<status id="1" type="ATTEND">	<desc lan="ISO-8859-1" name="Completed"/>	<desc lan="Big5" name="宸插畬鎴?/>	<desc lan="GB2312" name="宸插畬鎴?/></status>'
where ats_id = 1

update aeAttendanceStatus set ats_title_xml = '<status id="2" type="PROGRESS">	<desc lan="ISO-8859-1" name="In Progress"/>	<desc lan="Big5" name="閫茶涓?/>	<desc lan="GB2312" name="杩涜涓?/></status>'
where ats_id = 2

update aeAttendanceStatus set ats_title_xml = '<status id="3" type="INCOMPLETE">	<desc lan="ISO-8859-1" name="Failed"/>	<desc lan="Big5" name="未按預定完成"/>	<desc lan="GB2312" name="未按预定完成"/></status>'
where ats_id = 3

update aeAttendanceStatus set ats_title_xml = '<status id="4" type="NOSHOW">	<desc lan="ISO-8859-1" name="No Show"/>	<desc lan="Big5" name="缺席"/>	<desc lan="GB2312" name="缺席"/></status>'
where ats_id = 4


/*
Author : Clifford
Desc : Add the column "lsn_status_ind" for aeLearningSoln
Date : 2003-05-20
*/
BEGIN TRANSACTION
ALTER TABLE dbo.aeLearningSoln
	DROP CONSTRAINT PK_aeLearningSoln
GO
ALTER TABLE dbo.aeLearningSoln ADD CONSTRAINT
	PK_aeLearningSoln PRIMARY KEY NONCLUSTERED
	(
	lsn_ent_id,
	lsn_itm_id,
	lsn_ent_id_lst,
	lsn_create_timestamp,
	lsn_type
	) WITH FILLFACTOR = 90 ON [PRIMARY]
GO
COMMIT

ALTER TABLE dbo.aeLearningSoln ADD
	lsn_status_ind int NULL
GO

update aeLearningSoln set lsn_status_ind = 0

update aeLearningSoln set lsn_status_ind = 1 where lsn_create_timestamp = (select max(lsn_create_timestamp) from aeLearningSoln as Temp1 where Temp1.lsn_itm_id=aeLearningSoln.lsn_itm_id and Temp1.lsn_ent_id=aeLearningSoln.lsn_ent_id)

update aeLearningSoln set lsn_status_ind = 0 where aeLearningSoln.lsn_itm_id in
(select app_itm_id from aeApplication,aeAttendance where app_ent_id=aeLearningSoln.lsn_ent_id and app_itm_id=aeLearningSoln.lsn_itm_id and att_app_id = app_id and (att_ats_id = 1 or att_ats_id = 3 or att_ats_id = 4)
and app_id = (select max(app_id) from aeApplication where app_ent_id = aeLearningSoln.lsn_ent_id and app_itm_id=aeLearningSoln.lsn_itm_id))
and aeLearningSoln.lsn_create_timestamp = (select max(lsn_create_timestamp) from aeLearningSoln as Temp1 where Temp1.lsn_itm_id=aeLearningSoln.lsn_itm_id and Temp1.lsn_ent_id=aeLearningSoln.lsn_ent_id)

ALTER TABLE dbo.aeLearningSoln ALTER COLUMN
	lsn_status_ind int NOT NULL
GO


/*
Author: Clifford
Date: 2003-05-20
Desc : Relative due day for Assignment
*/
alter table Assignment add ass_due_date_day int NULL


/*
Author: Clifford
Date: 2003-05-21
Desc : Add max user attempt for Standard and Dynamice test's display option
*/
alter table DisplayOption add dpo_max_usr_attempt_ind bit NOT NULL DEFAULT (0)
GO

update displayoption set dpo_max_usr_attempt_ind = 1 where dpo_res_subtype = 'TST' or dpo_res_subtype = 'DXT'
update displayoption set dpo_pgr_attempt_nbr_ind = 1 where dpo_view = 'LRN_READ' and (dpo_res_subtype = 'TST' or dpo_res_subtype = 'DXT')


/*
Author: Dennis Yip
Date: 2003-05-27
Desc : Drop all existing CM tables and re-build the new tables
*/
if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[FK_cmAsmUnitTypeAttr_cmAssessment]') and OBJECTPROPERTY(id, N'IsForeignKey') = 1)
ALTER TABLE [dbo].[cmAsmUnitTypeAttr] DROP CONSTRAINT FK_cmAsmUnitTypeAttr_cmAssessment
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[FK_cmAssessmentNotify_cmAssessment]') and OBJECTPROPERTY(id, N'IsForeignKey') = 1)
ALTER TABLE [dbo].[cmAssessmentNotify] DROP CONSTRAINT FK_cmAssessmentNotify_cmAssessment
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[FK_aeItemCompetency_cmSkill]') and OBJECTPROPERTY(id, N'IsForeignKey') = 1)
ALTER TABLE [dbo].[aeItemCompetency] DROP CONSTRAINT FK_aeItemCompetency_cmSkill
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[FK_cmSkillSetCoverage_cmSkill]') and OBJECTPROPERTY(id, N'IsForeignKey') = 1)
ALTER TABLE [dbo].[cmSkillSetCoverage] DROP CONSTRAINT FK_cmSkillSetCoverage_cmSkill
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[FK_cmSkill_cmSkillBase]') and OBJECTPROPERTY(id, N'IsForeignKey') = 1)
ALTER TABLE [dbo].[cmSkill] DROP CONSTRAINT FK_cmSkill_cmSkillBase
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[FK_cmSkillTreeNode_cmSkillBase]') and OBJECTPROPERTY(id, N'IsForeignKey') = 1)
ALTER TABLE [dbo].[cmSkillTreeNode] DROP CONSTRAINT FK_cmSkillTreeNode_cmSkillBase
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[FK_cmSkillBase_cmSkillScale]') and OBJECTPROPERTY(id, N'IsForeignKey') = 1)
ALTER TABLE [dbo].[cmSkillBase] DROP CONSTRAINT FK_cmSkillBase_cmSkillScale
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[FK_cmSkillLevel_cmSkillScale]') and OBJECTPROPERTY(id, N'IsForeignKey') = 1)
ALTER TABLE [dbo].[cmSkillLevel] DROP CONSTRAINT FK_cmSkillLevel_cmSkillScale
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[FK_cmAssessment_cmSkillSet]') and OBJECTPROPERTY(id, N'IsForeignKey') = 1)
ALTER TABLE [dbo].[cmAssessment] DROP CONSTRAINT FK_cmAssessment_cmSkillSet
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[FK_cmAssessmentUnit_cmSkillSet]') and OBJECTPROPERTY(id, N'IsForeignKey') = 1)
ALTER TABLE [dbo].[cmAssessmentUnit] DROP CONSTRAINT FK_cmAssessmentUnit_cmSkillSet
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[FK_cmSkillSetCoverage_cmSkillSet]') and OBJECTPROPERTY(id, N'IsForeignKey') = 1)
ALTER TABLE [dbo].[cmSkillSetCoverage] DROP CONSTRAINT FK_cmSkillSetCoverage_cmSkillSet
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[cmAsmUnitTypeAttr]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [dbo].[cmAsmUnitTypeAttr]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[cmAssessment]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [dbo].[cmAssessment]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[cmAssessmentNotify]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [dbo].[cmAssessmentNotify]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[cmAssessmentUnit]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [dbo].[cmAssessmentUnit]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[cmSkill]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [dbo].[cmSkill]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[cmSkillBase]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [dbo].[cmSkillBase]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[cmSkillLevel]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [dbo].[cmSkillLevel]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[cmSkillScale]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [dbo].[cmSkillScale]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[cmSkillSet]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [dbo].[cmSkillSet]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[cmSkillSetCoverage]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [dbo].[cmSkillSetCoverage]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[cmSkillTreeNode]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [dbo].[cmSkillTreeNode]
GO

CREATE TABLE [dbo].[cmAsmUnitTypeAttr] (
	[aua_asm_id] [int] NOT NULL ,
	[aua_asu_type] [varchar] (20) NOT NULL ,
	[aua_eff_start_timestamp] [datetime] NOT NULL ,
	[aua_eff_end_timestamp] [datetime] NOT NULL
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[cmAssessment] (
	[asm_id] [int] IDENTITY (1, 1) NOT NULL ,
	[asm_ent_id] [int] NOT NULL ,
	[asm_title] [nvarchar] (50) NOT NULL ,
	[asm_sks_skb_id] [int] NULL ,
	[asm_status] [varchar] (20) NULL ,
	[asm_review_start_datetime] [datetime] NOT NULL ,
	[asm_review_end_datetime] [datetime] NOT NULL ,
	[asm_eff_start_datetime] [datetime] NOT NULL ,
	[asm_eff_end_datetime] [datetime] NOT NULL ,
	[asm_create_usr_id] [varchar] (20) NOT NULL ,
	[asm_create_timestamp] [datetime] NOT NULL ,
	[asm_update_usr_id] [varchar] (20) NOT NULL ,
	[asm_update_timestamp] [datetime] NOT NULL ,
	[asm_type] [varchar] (20) NOT NULL ,
	[asm_mod_res_id] [int] NULL ,
	[asm_marking_scheme_xml] [ntext] NULL ,
	[asm_auto_resolved_ind] [int] NOT NULL
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO

CREATE TABLE [dbo].[cmAssessmentNotify] (
	[asn_asm_id] [int] NOT NULL ,
	[asn_msg_id] [int] NOT NULL ,
	[asn_type] [varchar] (20) NOT NULL ,
	[asn_asu_type] [varchar] (20) NOT NULL
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[cmAssessmentUnit] (
	[asu_asm_id] [int] NOT NULL ,
	[asu_ent_id] [int] NOT NULL ,
	[asu_attempt_nbr] [int] NOT NULL ,
	[asu_type] [varchar] (20) NOT NULL ,
	[asu_weight] [int] NOT NULL ,
	[asu_sks_skb_id] [int] NULL ,
	[asu_submit_ind] [bit] NOT NULL
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[cmSkill] (
	[skl_skb_id] [int] NOT NULL ,
	[skl_type] [varchar] (20) NOT NULL ,
	[skl_xml] [nvarchar] (4000) NULL ,
	[skl_derive_rule] [varchar] (20) NULL ,
	[skl_rating_ind] [int] NOT NULL
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[cmSkillBase] (
	[skb_id] [int] IDENTITY (1, 1) NOT NULL ,
	[skb_type] [varchar] (20) NOT NULL ,
	[skb_title] [nvarchar] (100) NULL ,
	[skb_description] [nvarchar] (2000) NULL ,
	[skb_owner_ent_id] [int] NOT NULL ,
	[skb_parent_skb_id] [int] NULL ,
	[skb_ancestor] [varchar] (255) NULL ,
	[skb_ssl_id] [int] NULL ,
	[skb_order] [int] NULL ,
	[skb_create_usr_id] [varchar] (20) NOT NULL ,
	[skb_create_timestamp] [datetime] NOT NULL ,
	[skb_update_usr_id] [varchar] (20) NOT NULL ,
	[skb_update_timestamp] [datetime] NOT NULL ,
	[skb_delete_usr_id] [varchar] (20) NULL ,
	[skb_delete_timestamp] [datetime] NULL
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[cmSkillLevel] (
	[sle_ssl_id] [int] NOT NULL ,
	[sle_level] [smallint] NOT NULL ,
	[sle_label] [nvarchar] (50) NOT NULL ,
	[sle_description] [nvarchar] (4000) NULL
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[cmSkillScale] (
	[ssl_id] [int] IDENTITY (1, 1) NOT NULL ,
	[ssl_title] [nvarchar] (100) NOT NULL ,
	[ssl_share_ind] [bit] NOT NULL ,
	[ssl_owner_ent_id] [int] NOT NULL ,
	[ssl_create_usr_id] [varchar] (20) NOT NULL ,
	[ssl_create_timestamp] [datetime] NOT NULL ,
	[ssl_update_usr_id] [varchar] (20) NOT NULL ,
	[ssl_update_timestamp] [datetime] NOT NULL ,
	[ssl_delete_usr_id] [varchar] (50) NULL ,
	[ssl_delete_timestamp] [datetime] NULL
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[cmSkillSet] (
	[sks_skb_id] [int] IDENTITY (1, 1) NOT NULL ,
	[sks_type] [varchar] (20) NOT NULL ,
	[sks_title] [nvarchar] (100) NULL ,
	[sks_xml] [nvarchar] (4000) NULL ,
	[sks_owner_ent_id] [int] NOT NULL ,
	[sks_create_usr_id] [varchar] (20) NOT NULL ,
	[sks_create_timestamp] [datetime] NOT NULL ,
	[sks_update_usr_id] [varchar] (20) NOT NULL ,
	[sks_update_timestamp] [datetime] NOT NULL
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[cmSkillSetCoverage] (
	[ssc_sks_skb_id] [int] NOT NULL ,
	[ssc_skb_id] [int] NOT NULL ,
	[ssc_level] [decimal](18, 2) NULL ,
	[ssc_priority] [int] NULL ,
	[ssc_xml] [nvarchar] (4000) NULL
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[cmSkillTreeNode] (
	[stn_skb_id] [int] NOT NULL
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[cmAsmUnitTypeAttr] WITH NOCHECK ADD
	CONSTRAINT [PK_cmAsmUnitTypeAttr] PRIMARY KEY  CLUSTERED
	(
		[aua_asm_id],
		[aua_asu_type]
	)  ON [PRIMARY]
GO

ALTER TABLE [dbo].[cmSkillBase] WITH NOCHECK ADD
	CONSTRAINT [PK_cmSkillBase] PRIMARY KEY  CLUSTERED
	(
		[skb_id]
	)  ON [PRIMARY]
GO

ALTER TABLE [dbo].[cmSkillTreeNode] WITH NOCHECK ADD
	CONSTRAINT [PK_cmSkillTreeNode] PRIMARY KEY  CLUSTERED
	(
		[stn_skb_id]
	)  ON [PRIMARY]
GO

ALTER TABLE [dbo].[cmAssessment] WITH NOCHECK ADD
	CONSTRAINT [PK_cmAssessment] PRIMARY KEY  NONCLUSTERED
	(
		[asm_id]
	) WITH  FILLFACTOR = 90  ON [PRIMARY]
GO

ALTER TABLE [dbo].[cmAssessmentUnit] WITH NOCHECK ADD
	CONSTRAINT [PK_cmAssessmentUnit] PRIMARY KEY  NONCLUSTERED
	(
		[asu_asm_id],
		[asu_ent_id],
		[asu_type]
	) WITH  FILLFACTOR = 90  ON [PRIMARY]
GO

ALTER TABLE [dbo].[cmSkill] WITH NOCHECK ADD
	CONSTRAINT [PK_cmSkill] PRIMARY KEY  NONCLUSTERED
	(
		[skl_skb_id]
	) WITH  FILLFACTOR = 90  ON [PRIMARY]
GO

ALTER TABLE [dbo].[cmSkillLevel] WITH NOCHECK ADD
	CONSTRAINT [PK_cmSkillLevel] PRIMARY KEY  NONCLUSTERED
	(
		[sle_ssl_id],
		[sle_level]
	) WITH  FILLFACTOR = 90  ON [PRIMARY]
GO

ALTER TABLE [dbo].[cmSkillScale] WITH NOCHECK ADD
	CONSTRAINT [PK_cmSkillScale] PRIMARY KEY  NONCLUSTERED
	(
		[ssl_id]
	) WITH  FILLFACTOR = 90  ON [PRIMARY]
GO

ALTER TABLE [dbo].[cmSkillSet] WITH NOCHECK ADD
	CONSTRAINT [PK_cmSkillSet] PRIMARY KEY  NONCLUSTERED
	(
		[sks_skb_id]
	) WITH  FILLFACTOR = 90  ON [PRIMARY]
GO

ALTER TABLE [dbo].[cmSkillSetCoverage] WITH NOCHECK ADD
	CONSTRAINT [PK_cmSkillSetCoverage] PRIMARY KEY  NONCLUSTERED
	(
		[ssc_sks_skb_id],
		[ssc_skb_id]
	) WITH  FILLFACTOR = 90  ON [PRIMARY]
GO

ALTER TABLE [dbo].[cmAsmUnitTypeAttr] ADD
	CONSTRAINT [FK_cmAsmUnitTypeAttr_cmAssessment] FOREIGN KEY
	(
		[aua_asm_id]
	) REFERENCES [dbo].[cmAssessment] (
		[asm_id]
	)
GO

ALTER TABLE [dbo].[cmAssessment] ADD
	CONSTRAINT [FK_cmAssessment_cmSkillSet] FOREIGN KEY
	(
		[asm_sks_skb_id]
	) REFERENCES [dbo].[cmSkillSet] (
		[sks_skb_id]
	),
	CONSTRAINT [FK_cmAssessment_Entity] FOREIGN KEY
	(
		[asm_ent_id]
	) REFERENCES [dbo].[Entity] (
		[ent_id]
	)
GO

ALTER TABLE [dbo].[cmAssessmentNotify] ADD
	CONSTRAINT [FK_cmAssessmentNotify_cmAssessment] FOREIGN KEY
	(
		[asn_asm_id]
	) REFERENCES [dbo].[cmAssessment] (
		[asm_id]
	),
	CONSTRAINT [FK_cmAssessmentNotify_mgMessage] FOREIGN KEY
	(
		[asn_msg_id]
	) REFERENCES [dbo].[mgMessage] (
		[msg_id]
	)
GO

ALTER TABLE [dbo].[cmAssessmentUnit] ADD
	CONSTRAINT [FK_cmAssessmentUnit_cmSkillSet] FOREIGN KEY
	(
		[asu_sks_skb_id]
	) REFERENCES [dbo].[cmSkillSet] (
		[sks_skb_id]
	),
	CONSTRAINT [FK_cmAssessmentUnit_Entity] FOREIGN KEY
	(
		[asu_ent_id]
	) REFERENCES [dbo].[Entity] (
		[ent_id]
	)
GO

ALTER TABLE [dbo].[cmSkill] ADD
	CONSTRAINT [FK_cmSkill_cmSkillBase] FOREIGN KEY
	(
		[skl_skb_id]
	) REFERENCES [dbo].[cmSkillBase] (
		[skb_id]
	)
GO

ALTER TABLE [dbo].[cmSkillBase] ADD
	CONSTRAINT [FK_cmSkillBase_cmSkillScale] FOREIGN KEY
	(
		[skb_ssl_id]
	) REFERENCES [dbo].[cmSkillScale] (
		[ssl_id]
	)
GO

ALTER TABLE [dbo].[cmSkillLevel] ADD
	CONSTRAINT [FK_cmSkillLevel_cmSkillScale] FOREIGN KEY
	(
		[sle_ssl_id]
	) REFERENCES [dbo].[cmSkillScale] (
		[ssl_id]
	)
GO

ALTER TABLE [dbo].[cmSkillSetCoverage] ADD
	CONSTRAINT [FK_cmSkillSetCoverage_cmSkill] FOREIGN KEY
	(
		[ssc_skb_id]
	) REFERENCES [dbo].[cmSkill] (
		[skl_skb_id]
	),
	CONSTRAINT [FK_cmSkillSetCoverage_cmSkillSet] FOREIGN KEY
	(
		[ssc_sks_skb_id]
	) REFERENCES [dbo].[cmSkillSet] (
		[sks_skb_id]
	)
GO

ALTER TABLE [dbo].[cmSkillTreeNode] ADD
	CONSTRAINT [FK_cmSkillTreeNode_cmSkillBase] FOREIGN KEY
	(
		[stn_skb_id]
	) REFERENCES [dbo].[cmSkillBase] (
		[skb_id]
	)
GO

/*
Author: Lun
Date: 2003-05-27
Desc : New and update SystemMessage for CM
*/
Insert Into SystemMessage Values ( 'CMP021', 'ISO-8859-1', '$data exists. Please specify another competency group name.')
Insert Into SystemMessage Values ( 'CMP021', 'Big5', '!!!$data exists. Please specify another competency group name.')
Insert Into SystemMessage Values ( 'CMP021', 'GB2312', '!!!$data exists. Please specify another competency group name.')


Update SystemMessage
Set sms_desc = 'The competency group is not empty'
Where sms_id = 'CMP002' And sms_lan = 'ISO-8859-1'

/*
Author: Dennis Yip
Date: 2003-05-30
Desc: New table SuperviseTargetEntity for "My Staff"
*/
CREATE TABLE [dbo].[SuperviseTargetEntity] (
	[spt_source_usr_ent_id] [int] NOT NULL ,
	[spt_type] [varchar] (20) NOT NULL ,
	[spt_target_ent_id] [int] NOT NULL ,
	[spt_create_timestamp] [datetime] NOT NULL ,
	[spt_create_usr_id] [varchar] (20) NOT NULL ,
	[spt_syn_timestamp] [datetime] NULL ,
	[spt_eff_start_datetime] [datetime] NOT NULL ,
	[spt_eff_end_datetime] [datetime] NOT NULL
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[SuperviseTargetEntity] WITH NOCHECK ADD
	CONSTRAINT [PK_SuperviseTargetEntity] PRIMARY KEY  CLUSTERED
	(
		[spt_source_usr_ent_id],
		[spt_type],
		[spt_target_ent_id]
	)  ON [PRIMARY]
GO

ALTER TABLE [dbo].[SuperviseTargetEntity] ADD
	CONSTRAINT [FK_SuperviseTargetEntity_Entity] FOREIGN KEY
	(
		[spt_target_ent_id]
	) REFERENCES [dbo].[Entity] (
		[ent_id]
	),
	CONSTRAINT [FK_SuperviseTargetEntity_RegUser] FOREIGN KEY
	(
		[spt_source_usr_ent_id]
	) REFERENCES [dbo].[RegUser] (
		[usr_ent_id]
	)
GO

/*
Author: Dennis Yip
Date: 2003-05-30
Desc: Migrate data from usrRoleTargetEntity to SuperviseTargetEntity
*/
insert into superviseTargetEntity
(spt_source_usr_ent_id, spt_type, spt_target_ent_id, spt_create_timestamp, spt_create_usr_id, spt_syn_timestamp, spt_eff_start_datetime, spt_eff_end_datetime)
select distinct rte_usr_ent_id, 'DIRECT_SUPERVISE', rte_ent_id, rte_create_timestamp, rte_create_usr_id, rte_syn_timestamp, rte_eff_start_datetime, rte_eff_end_datetime
from usrRoleTargetEntity, RegUser
where rte_ent_id = usr_ent_id

insert into superviseTargetEntity
(spt_source_usr_ent_id, spt_type, spt_target_ent_id, spt_create_timestamp, spt_create_usr_id, spt_syn_timestamp, spt_eff_start_datetime, spt_eff_end_datetime)
select distinct rte_usr_ent_id, 'SUPERVISE', rte_ent_id, rte_create_timestamp, rte_create_usr_id, rte_syn_timestamp, rte_eff_start_datetime, rte_eff_end_datetime
from usrRoleTargetEntity, UserGroup
where rte_ent_id = usg_ent_id


/*
Author: Dennis Yip
Date: 2003-05-30
Desc: New SystemMessage for "My Staff"
*/
insert into systemMessage (sms_id, sms_lan, sms_desc)
values ('SPT001', 'ISO-8859-1', 'You are not the supervisor of $data')
insert into systemMessage (sms_id, sms_lan, sms_desc)
values ('SPT001', 'GB2312', '!!!You are not the supervisor of $data')
insert into systemMessage (sms_id, sms_lan, sms_desc)
values ('SPT001', 'Big5', '!!!You are not the supervisor of $data')


/*
Auth: Lun
Date: 2003-05-30
Desc: Add a new column for storing the auth. level
*/
ALTER TABLE acRole ADD rol_auth_level int NULL
Update acRole Set rol_auth_level = 0
ALTER TABLE acRole ALTER COLUMN rol_auth_level int


/*
Auth: Lun
Date: 2003-05-30
Desc: New acFunction for user management
*/
Insert into acFunction Values ( 'USR_MGT_LOWER_ROLE', 'FUNCTION', 'USER', getDate(), null)
Insert into acFunction Values ( 'USR_MGT_SAME_AND_LOWER_ROLE', 'FUNCTION', 'USER', getDate(), null)

/*
Auth: Kim
Date: 2003-05-30
Desc: New table for preference
*/
CREATE TABLE [dbo].[psnPreference] (
	[pfr_ent_id] [int] NOT NULL ,
	[pfr_skin_id] [varchar] (20) NOT NULL ,
	[pfr_lang] [varchar] (10) NOT NULL ,
	[pfr_create_timestamp] [datetime] NOT NULL ,
	[pfr_create_usr_id] [varchar] (20) NOT NULL ,
	[pfr_update_timestamp] [datetime] NOT NULL ,
	[pfr_update_usr_id] [varchar] (20) NOT NULL
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[psnPreference] ADD
 CONSTRAINT [FK_PSNPREFERENCE_ENTITY] FOREIGN KEY
 (
  [pfr_ent_id]
 ) REFERENCES [dbo].[Entity] (
  [ent_id]
 )

/*
Auth: Kim
Date: 2003-05-30
Desc: Add Column ste_id for organization config
*/
alter table acSite add ste_id varchar (10) null
update acsite set ste_id = 'cw'

/*
Auth: Kim
Date: 2003-05-30
Desc: Skin column retired. set in system config xml
*/
alter table acSite  drop column ste_skin_root
alter table acSite drop column ste_xsl_root

/*
Auth: Kim
Date: 2003-05-30
Desc: Add acFunction for course Evaluation and my preference
*/

Insert Into acFunction Values ( 'COS_EVN_MAIN', 'FUNCTION', 'HOMEPAGE', getDate(),
'<function id="COS_EVN_MAIN">
<desc lan="ISO-8859-1" name="Course Evaluation"/>
<desc lan="Big5" name="!!!Course Evaluation"/>
<desc lan="GB2312" name="!!!Course Evaluation"/>
</function>')

/* data setup , assign access control course evalation to TADM_1 */
declare @ftn_id int
select @ftn_id = max(ftn_id) from acFunction
Insert into acRoleFunction Values ( 7, @ftn_id, getDate())

Insert Into acFunction Values ( 'USR_OWN_PREFER', 'FUNCTION', 'HOMEPAGE', getDate(),
'<function id="USR_OWN_PREFER">
<desc lan="ISO-8859-1" name="My Preference"/>
<desc lan="Big5" name="!!!My Preference"/>
<desc lan="GB2312" name="!!!My Preference"/>
</function>')

/* data setup , assign access control my preference to NLRN_1 */
declare @ftn_id int
select @ftn_id = max(ftn_id) from acFunction
Insert into acRoleFunction Values ( 5, @ftn_id, getDate())

update displayoption set dpo_eff_start_datetime_ind = 0, dpo_eff_end_datetime_ind = 0 where dpo_res_subtype = 'svy'

update displayoption set dpo_desc_ind = 0 where dpo_res_subtype = 'svy'
update displayoption set dpo_lan_ind = 0 where dpo_res_subtype = 'svy'

/*
Auth: Kim
Date: 2003-05-30
Desc: Add column in RegUser
*/
alter table reguser add usr_join_datetime datetime null
alter table reguser add usr_job_title nvarchar (255) null

/*
Auth: Lun
Date: 2003-06-11
Desc: Add itm_retake_ind(int)  to table aeItem , to indicate item is retakable
*/
alter table aeItem add itm_retake_ind int NULL

/*
Auth: Kim
Date: 2003-06-16
Desc: add column approval_ind in aeTemplate, set approval ind for each template
*/
alter table aeTemplate add tpl_approval_ind int
go
update aeTemplate set tpl_approval_ind = 0 where tpl_id <> 15
update aeTemplate set tpl_approval_ind = 1 where tpl_id = 15
go
alter table aeTemplate alter column tpl_approval_ind int not null
go

/*
Auth: kawai
Date: 2003-06-25
Desc: change the data type of following columns from ntext to nvarchar
*/
alter table aeAppnCommHistory add ach_content_new nvarchar(2000) null
go
update aeAppnCommHistory set ach_content_new = ach_content
alter table aeAppnCommHistory drop column ach_content
go
alter table aeAppnCommHistory add ach_content nvarchar(2000) null
go
update aeAppnCommHistory set ach_content = ach_content_new
alter table aeAppnCommHistory drop column ach_content_new
go
alter table aeAppnCommHistory alter column ach_content nvarchar(2000) not null
go

alter table Assignment add ass_submission_new nvarchar(2000) null
go
update Assignment set ass_submission_new = ass_submission
alter table Assignment drop column ass_submission
go
alter table Assignment add ass_submission nvarchar(2000) null
go
update Assignment set ass_submission = ass_submission_new
alter table Assignment drop column ass_submission_new
go

alter table ForumMessage add fmg_title_new nvarchar(500) null
go
update ForumMessage set fmg_title_new = fmg_title
alter table ForumMessage drop column fmg_title
go
alter table ForumMessage add fmg_title nvarchar(500) null
go
update ForumMessage set fmg_title = fmg_title_new
alter table ForumMessage drop column fmg_title_new
go

alter table Resources add res_annotation_new nvarchar(2000) null
go
update Resources set res_annotation_new = res_annotation
alter table Resources drop column res_annotation
go
alter table Resources add res_annotation nvarchar(2000) null
go
update Resources set res_annotation = res_annotation_new
alter table Resources drop column res_annotation_new
go

/*
Auth: Fai
Date: 2003/7/8
Desc: insert new acfunction for Knowledge Management
(not assign to any specific roles yet for standard edition)
*/
INSERT INTO acFunction (
ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml
) values (
'KB_MGT', 'FUNCTION', 'HOMEPAGE', '2003-07-08', null
)
INSERT INTO acFunction (
ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml
) values (
'KB_MGT_ADMIN', 'FUNCTION', 'HOMEPAGE', '2003-07-08', null
)

call %UPDXMLCMD% xml\acFunction\ftn_kb_mgt.xml acFunction ftn_xml "ftn_ext_id = 'KB_MGT'"
call %UPDXMLCMD% xml\acFunction\ftn_kb_mgt_admin.xml acFunction ftn_xml "ftn_ext_id = 'KB_MGT_ADMIN'"

/*
Auth: Fai
Date: 2003/7/8
Desc: alter kmnode table to store the display option status
	1 - display (if kmNodeAccess is empty, ie. display to all user; otherwise, display to partial user)
	0 - do not display
*/
alter table kmNode add nod_display_option_ind int
go
update kmNode set nod_display_option_ind = 1
go
alter table kmNode alter column nod_display_option_ind int not null
go

/*
Auth: kawai
Date: 2003-07-09
Desc: add a new message template for "enrollment removed"
*/
insert into xslTemplate(
xtp_type, xtp_subtype, xtp_channel_type, xtp_channel_api, xtp_xsl, xtp_xml_url, xtp_mailmerge_ind, xtp_title
) values (
'ENROLLMENT_REMOVED','HTML','SMTP',null,'enrollment_removed_notes.xsl','http://host:port/servlet/aeAction?',0,null
);
insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
select xtp_id, 1, 'ent_ids' from xslTemplate where xtp_type = 'ENROLLMENT_REMOVED';
insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
select xtp_id, 2, 'sender_id' from xslTemplate where xtp_type = 'ENROLLMENT_REMOVED';
insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
select xtp_id, 3, 'cmd' from xslTemplate where xtp_type = 'ENROLLMENT_REMOVED';
insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
select xtp_id, 4, 'cc_ent_ids' from xslTemplate where xtp_type = 'ENROLLMENT_REMOVED';
insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
select xtp_id, 5, 'id' from xslTemplate where xtp_type = 'ENROLLMENT_REMOVED';
insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
select xtp_id, 6, 'id_type' from xslTemplate where xtp_type = 'ENROLLMENT_REMOVED';

/*
Auth: kawai
Date: 2003-07-09
Desc: use one mail for "To" and "CC" together
*/
update xslTemplate
set xtp_mailmerge_ind = 0
where xtp_type in (
'ENROLLMENT_NEW',
'ENROLLMENT_CONFIRMED',
'ENROLLMENT_REMOVED',
'ENROLLMENT_WAITLISTED',
'ENROLLMENT_NOT_CONFIRMED',
'ENROLLMENT_WITHDRAWAL_APPROVED'
);

/*
Auth: kawai
Date: 2003-07-09
Desc: duplicate new messaging templates for auto-workflow
*/
insert into xslTemplate(xtp_type, xtp_subtype, xtp_channel_type, xtp_channel_api, xtp_xsl, xtp_xml_url, xtp_mailmerge_ind, xtp_title)
select xtp_type+'_AUTO', xtp_subtype, xtp_channel_type, xtp_channel_api, replace(xtp_xsl,'_notes.xsl','_auto_notes.xsl'), xtp_xml_url, xtp_mailmerge_ind, xtp_title
from xslTemplate where xtp_type = 'ENROLLMENT_WAITLISTED';

insert into xslTemplate(xtp_type, xtp_subtype, xtp_channel_type, xtp_channel_api, xtp_xsl, xtp_xml_url, xtp_mailmerge_ind, xtp_title)
select xtp_type+'_AUTO', xtp_subtype, xtp_channel_type, xtp_channel_api, replace(xtp_xsl,'_notes.xsl','_auto_notes.xsl'), xtp_xml_url, xtp_mailmerge_ind, xtp_title
from xslTemplate where xtp_type = 'ENROLLMENT_CONFIRMED';

insert into xslTemplate(xtp_type, xtp_subtype, xtp_channel_type, xtp_channel_api, xtp_xsl, xtp_xml_url, xtp_mailmerge_ind, xtp_title)
select xtp_type+'_AUTO', xtp_subtype, xtp_channel_type, xtp_channel_api, replace(xtp_xsl,'_notes.xsl','_auto_notes.xsl'), xtp_xml_url, xtp_mailmerge_ind, xtp_title
from xslTemplate where xtp_type = 'ENROLLMENT_NOT_CONFIRMED';

insert into xslTemplate(xtp_type, xtp_subtype, xtp_channel_type, xtp_channel_api, xtp_xsl, xtp_xml_url, xtp_mailmerge_ind, xtp_title)
select xtp_type+'_AUTO', xtp_subtype, xtp_channel_type, xtp_channel_api, replace(xtp_xsl,'_notes.xsl','_auto_notes.xsl'), xtp_xml_url, xtp_mailmerge_ind, xtp_title
from xslTemplate where xtp_type = 'ENROLLMENT_WITHDRAWAL_APPROVED';

insert into xslTemplate(xtp_type, xtp_subtype, xtp_channel_type, xtp_channel_api, xtp_xsl, xtp_xml_url, xtp_mailmerge_ind, xtp_title)
select xtp_type+'_AUTO', xtp_subtype, xtp_channel_type, xtp_channel_api, replace(xtp_xsl,'_notes.xsl','_auto_notes.xsl'), xtp_xml_url, xtp_mailmerge_ind, xtp_title
from xslTemplate where xtp_type = 'ENROLLMENT_REMOVED';

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
select xtp_id, xpn_pos, xpn_name from xslParamName, xslTemplate where
xpn_xtp_id in (select xtp_id from xslTemplate where xtp_type = 'ENROLLMENT_WAITLISTED')
and xtp_type = 'ENROLLMENT_WAITLISTED_AUTO';

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
select xtp_id, xpn_pos, xpn_name from xslParamName, xslTemplate where
xpn_xtp_id in (select xtp_id from xslTemplate where xtp_type = 'ENROLLMENT_CONFIRMED')
and xtp_type = 'ENROLLMENT_CONFIRMED_AUTO';

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
select xtp_id, xpn_pos, xpn_name from xslParamName, xslTemplate where
xpn_xtp_id in (select xtp_id from xslTemplate where xtp_type = 'ENROLLMENT_NOT_CONFIRMED')
and xtp_type = 'ENROLLMENT_NOT_CONFIRMED_AUTO';

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
select xtp_id, xpn_pos, xpn_name from xslParamName, xslTemplate where
xpn_xtp_id in (select xtp_id from xslTemplate where xtp_type = 'ENROLLMENT_WITHDRAWAL_APPROVED')
and xtp_type = 'ENROLLMENT_WITHDRAWAL_APPROVED_AUTO';

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
select xtp_id, xpn_pos, xpn_name from xslParamName, xslTemplate where
xpn_xtp_id in (select xtp_id from xslTemplate where xtp_type = 'ENROLLMENT_REMOVED')
and xtp_type = 'ENROLLMENT_REMOVED_AUTO';

/*
Auth: Dennis Yip
Date: 2003-07-14
Desc: Create new access control functions for Global Catalog and Global Management Report
*/
insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('GLB_CAT_MAIN', 'FUNCTION', 'HOMEPAGE', getdate(), null)

insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('GLB_CAT_MGT', 'FUNCTION', 'CATALOG', getdate(), null)

insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('GLB_RPT_LINK', 'FUNCTION', 'HOMEPAGE', getdate(), null)

update acFunction set ftn_ext_id = 'CAT_MGT'
where ftn_ext_id = 'CAT_NEW'

call %UPDXMLCMD% xml\acFunction\ftn_glb_cat_main.xml	acFunction ftn_xml "ftn_ext_id = 'GLB_CAT_MAIN'"
call %UPDXMLCMD% xml\acFunction\ftn_glb_rpt_link.xml	acFunction ftn_xml "ftn_ext_id = 'GLB_RPT_LINK'"

/*
Auth: Dennis Yip
Date: 2003-07-14
Desc: Create new Report Template and Standard Report for Global Enrollment Report
      You need to insert access control record(s) to acReportTemplate for the roles can read this report
*/
declare @rte_id int

insert into ReportTemplate
(rte_title_xml, rte_type, rte_get_xsl, rte_exe_xsl, rte_dl_xsl, rte_meta_data_url, rte_seq_no, rte_owner_ent_id, rte_create_usr_id, rte_create_timestamp, rte_upd_usr_id, rte_upd_timestamp)
values ('', 'GLOBAL_ENROLLMENT', 'rpt_glb_enrol_srh.xsl', 'rpt_glb_enrol_res.xsl', 'rpt_dl_glb_enrol_xls.xsl', null, 6, 1, 's1u3', getdate(), 's1u3', getdate())

select @rte_id = rte_id from ReportTemplate where rte_type = 'GLOBAL_ENROLLMENT'

insert into ReportSpec
(rsp_rte_id, rsp_ent_id, rsp_title, rsp_xml, rsp_create_usr_id, rsp_create_timestamp, rsp_upd_usr_id, rsp_upd_timestamp)
values (@rte_id, null, 'Standard Global Enrollment Report', '<data_list><data name="all_org_ind" value="1"/></data_list>', 's1u3', getdate(), 's1u3', getdate())

call %UPDXMLCMD% xml\ReportTemplate\rte_title_global_enrollment.xml	ReportTemplate rte_title_xml "rte_type = 'GLOBAL_ENROLLMENT'"
call %UPDXMLCMD% xml\ReportSpec\rsp_std_global_enrollment.xml		ReportSpec rsp_xml "rsp_rte_id in (select rte_id from ReportTemplate where rte_type = 'GLOBAL_ENROLLMENT') and rsp_ent_id is null"

/*
Auth: Dennis Yip
Date: 2003-07-14
Desc: Create new SystemMessage which will be shown when user delete a Learning Solution(aeItem) having enrollments(Tracking History)
*/
insert into SystemMessage (sms_id, sms_lan, sms_desc)
values ('AEIT18', 'ISO-8859-1', 'You cannot remove the Learning Solution because it has already been successfully enrolled by a learner.')

insert into SystemMessage (sms_id, sms_lan, sms_desc)
values ('AEIT18', 'Big5', '!!!You cannot remove the Learning Solution because it has already been successfully enrolled by a learner.')

insert into SystemMessage (sms_id, sms_lan, sms_desc)
values ('AEIT18', 'GB2312', '!!!You cannot remove the Learning Solution because it has already been successfully enrolled by a learner.')

call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_aeit18_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'AEIT18' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_aeit18_gb2312.txt	SystemMessage sms_desc "sms_id = 'AEIT18' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_aeit18_big5.txt	SystemMessage sms_desc "sms_id = 'AEIT18' and sms_lan = 'Big5'"

/*
Auth: Dennis Yip
Date: 2003-07-14
Desc: Alter aeCatalogAccess schema for Multiple Organizations
*/
alter table aeCatalogAccess drop PK_aeCatalogAccess
GO

alter table aeCatalogAccess add cac_id int IDENTITY(1, 1)
GO

alter table aeCatalogAccess alter column cac_ent_id int null
GO

alter table aeCatalogAccess with nocheck add constraint PK_aeCatalogAccess primary key clustered
(cac_id) on [primary]
GO

delete from aeCatalogAccess where cac_cat_id in (select cat_id from aeCatalog where cat_public_ind = 0)

insert into aeCatalogAccess (cac_ent_id, cac_cat_id, cac_create_timestamp, cac_create_usr_id)
select cat_owner_ent_id, cat_id, getdate(), 's1u3' from aeCatalog

/*
Auth: Dennis Yip
Date: 2003-07-14
Desc: Alter aeCatalogItemType schema for Multiple Organizations
*/
alter table aeCatalogItemType drop PK_aeCatalogItemType
GO

alter table aeCatalogItemType add cit_id int IDENTITY(1, 1)
GO

alter table aeCatalogItemType alter column cit_ity_owner_ent_id int null
GO

alter table aeCatalogItemType alter column cit_ity_id varchar(20) null
GO

alter table aeCatalogItemType with nocheck add constraint PK_aeCatalogItemType primary key clustered
(cit_id) on [primary]
GO

delete from aeCatalogItemType

insert into aeCatalogItemType (cit_cat_id, cit_ity_owner_ent_id, cit_ity_id, cit_create_timestamp, cit_create_usr_id)
select cat_id, cat_owner_ent_id, NULL, getdate(), 's1u3' from aeCatalog

/*
Auth: kawai
Date: 2003-07-14
Desc: update some gb translation
*/
call %UPDXMLCMD% xml\aeTemplateView\tvw_appnform_selfstudy_detail.xml	aeTemplateview tvw_xml "tvw_id = 'DETAIL_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Self Study Enrollment Form Template')"

call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_acl001_gb2312.txt	SystemMessage sms_desc "sms_id = 'ACL001' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_aeit17_gb2312.txt	SystemMessage sms_desc "sms_id = 'AEIT17' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_cmp021_gb2312.txt	SystemMessage sms_desc "sms_id = 'CMP021' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_sco01_gb2312.txt 	SystemMessage sms_desc "sms_id = 'SCO01'  and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_sco02_gb2312.txt 	SystemMessage sms_desc "sms_id = 'SCO02'  and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_spt001_gb2312.txt	SystemMessage sms_desc "sms_id = 'SPT001' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_usr014_gb2312.txt	SystemMessage sms_desc "sms_id = 'USR014' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_usr016_gb2312.txt	SystemMessage sms_desc "sms_id = 'USR016' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_usr017_gb2312.txt	SystemMessage sms_desc "sms_id = 'USR017' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_usr018_gb2312.txt	SystemMessage sms_desc "sms_id = 'USR018' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_usr019_gb2312.txt	SystemMessage sms_desc "sms_id = 'USR019' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_usr020_gb2312.txt	SystemMessage sms_desc "sms_id = 'USR020' and sms_lan = 'GB2312'"

/*
Auth: kim
Date: 2003-07-17
Desc: new system message for user import comfirmation page
*/

insert into SystemMessage (sms_id, sms_lan, sms_desc)
values ('ULG009', 'Big5', '!!!The format of header row is not correct. Column "$data" must be supplied.')
insert into SystemMessage (sms_id, sms_lan, sms_desc)
values ('ULG009', 'GB2312', '!!!The format of header row is not correct. Column "$data" must be supplied.')
insert into SystemMessage (sms_id, sms_lan, sms_desc)
values ('ULG009', 'ISO-8859-1', 'The format of header row is not correct. Column "$data" must be supplied.')

call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_ulg009_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'ULG009' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_ulg009_gb2312.txt	SystemMessage sms_desc "sms_id = 'ULG009' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_ulg009_big5.txt	SystemMessage sms_desc "sms_id = 'ULG009' and sms_lan = 'Big5'"

insert into SystemMessage (sms_id, sms_lan, sms_desc)
values ('ULG010', 'Big5', '!!!The format of header row is not correct. Duplicated column name "$data" at column $data.')
insert into SystemMessage (sms_id, sms_lan, sms_desc)
values ('ULG010', 'GB2312', '!!!The format of header row is not correct. Duplicated column name "$data" at column $data.')
insert into SystemMessage (sms_id, sms_lan, sms_desc)
values ('ULG010', 'ISO-8859-1', 'The format of header row is not correct. Duplicated column name "$data" at column $data.')

call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_ulg010_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'ULG010' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_ulg010_gb2312.txt	SystemMessage sms_desc "sms_id = 'ULG010' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_ulg010_big5.txt	SystemMessage sms_desc "sms_id = 'ULG010' and sms_lan = 'Big5'"

/*
Auth: kawai
Date: 2003-07-18
Desc: redo this sql due to an error of a previous bug
      (that newly created catalogs do not insert aeCatalogAccess)
*/
delete from aeCatalogAccess where cac_cat_id in (select cat_id from aeCatalog where cat_public_ind = 0)

insert into aeCatalogAccess (cac_ent_id, cac_cat_id, cac_create_timestamp, cac_create_usr_id)
select cat_owner_ent_id, cat_id, getdate(), 's1u3' from aeCatalog

/*
Auth: kawai
Date: 2003-07-18
Desc: SystemMessage updated
*/
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_aetn01_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'AETN01' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_aetn01_gb2312.txt	SystemMessage sms_desc "sms_id = 'AETN01' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_aetn01_big5.txt	SystemMessage sms_desc "sms_id = 'AETN01' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_usg007_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'USG007' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_usg008_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'USG008' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_usg007_gb2312.txt	SystemMessage sms_desc "sms_id = 'USG007' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_usg008_gb2312.txt	SystemMessage sms_desc "sms_id = 'USG008' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_usg008_big5.txt	SystemMessage sms_desc "sms_id = 'USG008' and sms_lan = 'Big5'"

/*
Auth: Dennis Yip
Date: 2003-08-01
Desc: Update Big5 SystemMessage
*/
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_aeit01_big5.txt	SystemMessage sms_desc "sms_id = 'AEIT01' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_aeit02_big5.txt	SystemMessage sms_desc "sms_id = 'AEIT02' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_aeit03_big5.txt	SystemMessage sms_desc "sms_id = 'AEIT03' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_aeit04_big5.txt	SystemMessage sms_desc "sms_id = 'AEIT04' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_aeit05_big5.txt	SystemMessage sms_desc "sms_id = 'AEIT05' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_aeit06_big5.txt	SystemMessage sms_desc "sms_id = 'AEIT06' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_aeit07_big5.txt	SystemMessage sms_desc "sms_id = 'AEIT07' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_aeit08_big5.txt	SystemMessage sms_desc "sms_id = 'AEIT08' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_aeit10_big5.txt	SystemMessage sms_desc "sms_id = 'AEIT10' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_aeit11_big5.txt	SystemMessage sms_desc "sms_id = 'AEIT11' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_aeit12_big5.txt	SystemMessage sms_desc "sms_id = 'AEIT12' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_aeit18_big5.txt	SystemMessage sms_desc "sms_id = 'AEIT18' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_aeqm07_big5.txt	SystemMessage sms_desc "sms_id = 'AEQM07' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_aetn01_big5.txt	SystemMessage sms_desc "sms_id = 'AETN01' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_aetn05_big5.txt	SystemMessage sms_desc "sms_id = 'AETN05' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_aetn06_big5.txt	SystemMessage sms_desc "sms_id = 'AETN06' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_mot004_big5.txt	SystemMessage sms_desc "sms_id = 'MOT004' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_mot005_big5.txt	SystemMessage sms_desc "sms_id = 'MOT005' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_ulg009_big5.txt	SystemMessage sms_desc "sms_id = 'ULG004' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_ulg010_big5.txt	SystemMessage sms_desc "sms_id = 'ULG005' and sms_lan = 'Big5'"

/*
Auth: kawai
Date: 2003-08-06
Desc: Update SystemMessage
*/
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_aeit17_big5.txt	SystemMessage sms_desc "sms_id = 'AEIT17' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_cmp021_big5.txt	SystemMessage sms_desc "sms_id = 'CMP021' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_sco01_big5.txt	SystemMessage sms_desc "sms_id = 'SCO01' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_sco02_big5.txt	SystemMessage sms_desc "sms_id = 'SCO02' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_spt001_big5.txt	SystemMessage sms_desc "sms_id = 'SPT001' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_aeit18_gb2312.txt	SystemMessage sms_desc "sms_id = 'AEIT18' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_iad001_gb2312.txt	SystemMessage sms_desc "sms_id = 'IAD001' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_kml001_gb2312.txt	SystemMessage sms_desc "sms_id = 'KML001' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_kml002_gb2312.txt	SystemMessage sms_desc "sms_id = 'KML002' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_kml003_gb2312.txt	SystemMessage sms_desc "sms_id = 'KML003' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_kml004_gb2312.txt	SystemMessage sms_desc "sms_id = 'KML004' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_kml005_gb2312.txt	SystemMessage sms_desc "sms_id = 'KML005' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_kml006_gb2312.txt	SystemMessage sms_desc "sms_id = 'KML006' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_kml007_gb2312.txt	SystemMessage sms_desc "sms_id = 'KML007' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_kml008_gb2312.txt	SystemMessage sms_desc "sms_id = 'KML008' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_kml009_gb2312.txt	SystemMessage sms_desc "sms_id = 'KML009' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_kml010_gb2312.txt	SystemMessage sms_desc "sms_id = 'KML010' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_kml011_gb2312.txt	SystemMessage sms_desc "sms_id = 'KML011' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_kml012_gb2312.txt	SystemMessage sms_desc "sms_id = 'KML012' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_kml013_gb2312.txt	SystemMessage sms_desc "sms_id = 'KML013' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_kml014_gb2312.txt	SystemMessage sms_desc "sms_id = 'KML014' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_kml015_gb2312.txt	SystemMessage sms_desc "sms_id = 'KML015' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_kml016_gb2312.txt	SystemMessage sms_desc "sms_id = 'KML016' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_kml017_gb2312.txt	SystemMessage sms_desc "sms_id = 'KML017' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_kml018_gb2312.txt	SystemMessage sms_desc "sms_id = 'KML018' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_kml019_gb2312.txt	SystemMessage sms_desc "sms_id = 'KML019' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_kml020_gb2312.txt	SystemMessage sms_desc "sms_id = 'KML020' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_kml021_gb2312.txt	SystemMessage sms_desc "sms_id = 'KML021' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_kml022_gb2312.txt	SystemMessage sms_desc "sms_id = 'KML022' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_kml023_gb2312.txt	SystemMessage sms_desc "sms_id = 'KML023' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_kml024_gb2312.txt	SystemMessage sms_desc "sms_id = 'KML024' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_kml025_gb2312.txt	SystemMessage sms_desc "sms_id = 'KML025' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_kml026_gb2312.txt	SystemMessage sms_desc "sms_id = 'KML026' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_kml027_gb2312.txt	SystemMessage sms_desc "sms_id = 'KML027' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_kml028_gb2312.txt	SystemMessage sms_desc "sms_id = 'KML028' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_kml029_gb2312.txt	SystemMessage sms_desc "sms_id = 'KML029' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_kml030_gb2312.txt	SystemMessage sms_desc "sms_id = 'KML030' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_kml031_gb2312.txt	SystemMessage sms_desc "sms_id = 'KML031' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_kmm001_gb2312.txt	SystemMessage sms_desc "sms_id = 'KMM001' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_kmm002_gb2312.txt	SystemMessage sms_desc "sms_id = 'KMM002' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_kmm003_gb2312.txt	SystemMessage sms_desc "sms_id = 'KMM003' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_kmm004_gb2312.txt	SystemMessage sms_desc "sms_id = 'KMM004' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_kmm005_gb2312.txt	SystemMessage sms_desc "sms_id = 'KMM005' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_kmm006_gb2312.txt	SystemMessage sms_desc "sms_id = 'KMM006' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_kmm007_gb2312.txt	SystemMessage sms_desc "sms_id = 'KMM007' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_kmm008_gb2312.txt	SystemMessage sms_desc "sms_id = 'KMM008' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_kmm009_gb2312.txt	SystemMessage sms_desc "sms_id = 'KMM009' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_ulg009_gb2312.txt	SystemMessage sms_desc "sms_id = 'ULG009' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_ulg010_gb2312.txt	SystemMessage sms_desc "sms_id = 'ULG010' and sms_lan = 'GB2312'"

/*
Auth: Kim
Date: 2003-08-11
Desc: add system message for checking upload file size
*/
insert into SystemMessage values ('GEN007', 'Big5', '!!!Total size of upload file must be less than $data MB.')
insert into SystemMessage values ('GEN007', 'GB2312', '!!!Total size of upload file must be less than $data MB.')
insert into SystemMessage values ('GEN007', 'ISO-8859-1', 'Total size of upload file must be less than $data MB.')

call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_gen007_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'GEN007' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_gen007_gb2312.txt	SystemMessage sms_desc "sms_id = 'GEN007' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_gen007_big5.txt	SystemMessage sms_desc "sms_id = 'GEN007' and sms_lan = 'Big5'"

/*
Auth: Kim
Date: 2003-08-12
Desc: script to recover empty title of item tree node
*/
begin tran
DECLARE tnd_cursor CURSOR FOR
select distinct tnd_itm_id from aetreenode where tnd_type = 'item' and tnd_title is null

OPEN tnd_cursor
declare @itm_id int

FETCH NEXT FROM tnd_cursor INTO @itm_id

WHILE @@FETCH_STATUS = 0
BEGIN
	declare @itm_title nvarchar (255)
	select @itm_title  = itm_title  from aeItem where itm_id = @itm_id
	update aeTreeNode set tnd_title = @itm_title where tnd_itm_id = @itm_id
	FETCH NEXT FROM tnd_cursor INTO @itm_id
END
CLOSE tnd_cursor
DEALLOCATE tnd_cursor
commit

/*
Auth: Dennis Yip
Date: 2003-08-13
Desc: Create record to support CC in ENROLLMENT_NEW message
*/
insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
select xpn_xtp_id, max(xpn_pos)+1, 'cc_ent_ids' from xslTemplate, xslParamName
where xtp_id = xpn_xtp_id and xtp_type = 'ENROLLMENT_NEW'
group by xpn_xtp_id

/*
Auth: Kim
Date: 2003-08-14
Desc: add system message for checking upload count in upload user
*/
insert into SystemMessage values ('ULG011', 'Big5', null)
insert into SystemMessage values ('ULG011', 'GB2312', null)
insert into SystemMessage values ('ULG011', 'ISO-8859-1', null)

call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_ulg011_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'ULG011' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_ulg011_gb2312.txt	SystemMessage sms_desc "sms_id = 'ULG011' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_ulg011_big5.txt	SystemMessage sms_desc "sms_id = 'ULG011' and sms_lan = 'Big5'"

/*
Auth: Kim
Date: 2003-08-20
Desc: Add column "default_ind" for UserGrade
*/
alter table UserGrade add ugr_default_ind int
go
update UserGrade set ugr_default_ind = 0
go
update  UserGrade set ugr_default_ind = 1 where ugr_ent_id in (select ent_id from Entity where ent_type = 'UGR' and ent_ste_uid = 'Unspecified')
go
alter table UserGrade alter column ugr_default_ind int NOT NULL
go

/*
Auth: Dennis
Date: 2003-08-20
Desc: Remove "Passing Score" from Netg Module
*/
update DisplayOption set dpo_pass_score_ind = 0 where dpo_res_subtype = 'NETG_COK'

/*
Auth: Dennis
Date: 2003-08-20
Desc: Update incorrect data that the res_title is set to NULL by setting Completion Criteria
*/
update Resources set res_title =
(select itm_title from Course, aeItem where cos_res_id = res_id and cos_itm_id = itm_id)
where res_title is null

/*
Auth: Dennis
Date: 2003-08-20
Desc: Set the fields cat_title and tnd_title to nvarchar(255), which is consistent with itm_title
*/
alter table aeCatalog alter column cat_title nvarchar(255)
go
alter table aeTreeNode alter column tnd_title nvarchar(255)
go

/*
Auth: kawai
Date: 2003-08-21
Desc: updated some system messages
*/
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_cmp007_gb2312.txt	SystemMessage sms_desc "sms_id = 'CMP007' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_cmp008_gb2312.txt	SystemMessage sms_desc "sms_id = 'CMP008' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_cmp009_gb2312.txt	SystemMessage sms_desc "sms_id = 'CMP009' and sms_lan = 'GB2312'"

/*
Auth: kawai
Date: 2003-09-04
Desc: changed the name of the xsl "svy_utils.xsl"
*/
update Template set tpl_stylesheet = 'svy_player.xsl' where tpl_stylesheet = 'svy_utils.xsl'

/*
Auth: Kim
Date: 2003-09-04
Desc: add coulmns and table course approval
*/

alter table aeItem add itm_approval_status varchar (20)
alter table aeItem add itm_approval_action varchar (20)
alter table aeItem add itm_approve_timestamp datetime
alter table aeItem add itm_approve_usr_id varchar (20)
alter table aeItem add itm_submit_action varchar (20)
alter table aeItem add itm_submit_timestamp datetime
alter table aeItem add itm_submit_usr_id varchar (20)

CREATE TABLE [dbo].[acItemPageVariant] (
	[ipv_name] [varchar] (40) NOT NULL ,
	[ipv_itm_approval_status] [varchar] (20) NOT NULL ,
	[ipv_run_ind] [int] NOT NULL ,
	[ipv_mgt_assistant_ind] [int] NOT NULL ,
	[ipv_auth_ind] [int] NOT NULL ,
	[ipv_create_timestamp] [datetime] NOT NULL
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[acItemPageVariant] ADD
	CONSTRAINT [PK_acItemPageVariant] PRIMARY KEY  CLUSTERED
	(
		[ipv_name],
		[ipv_itm_approval_status],
		[ipv_run_ind],
		[ipv_mgt_assistant_ind]
	)  ON [PRIMARY]
GO

insert into acfunction values ('ITM_MGT_ASSISTANT', 'FUNCTION', 'ITEM', getDate(), null)

insert into acfunction values ('APPR_ITM_LIST', 'FUNCTION', 'HOMEPAGE', getDate(), null)

call %UPDXMLCMD% xml\acFunction\ftn_appr_itm_list.xml acFunction ftn_xml "ftn_ext_id = 'APPR_ITM_LIST'"

/*
Auth: Kim
Date: 2003-09-04
Desc: add table for user biography
*/

CREATE TABLE [dbo].[psnBiography] (
	[pbg_ent_id] [int] NOT NULL ,
	[pbg_option] [varchar] (400) NULL ,
	[pbg_self_desc] [ntext] NULL ,
	[pbg_create_timestamp] [datetime] NOT NULL ,
	[pbg_create_usr_id] [varchar] (20) NOT NULL ,
	[pbg_update_timestamp] [datetime] NOT NULL ,
	[pbg_update_usr_id] [varchar] (20) NOT NULL
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO

ALTER TABLE [dbo].[psnBiography] ADD
	CONSTRAINT [PK_psnBiography] PRIMARY KEY  CLUSTERED
	(
		[pbg_ent_id]
	)  ON [PRIMARY]
GO

ALTER TABLE [dbo].[psnBiography] ADD
	CONSTRAINT [FK_PSNBIOGRAPHY_ENTITY] FOREIGN KEY
	(
		[pbg_ent_id]
	) REFERENCES [dbo].[Entity] (
		[ent_id]
	)
GO

/*
Auth: kawai
Date: 2003-09-10
Desc: update the label of a function
*/
call %UPDXMLCMD% xml\acFunction\ftn_lrn_res_main.xml    acFunction ftn_xml "ftn_ext_id = 'LRN_RES_MAIN'"

/*
Auth: kim
Date: 2003-09-10
Desc: add systemmessage for user biography
*/
insert into SystemMessage (sms_id, sms_lan, sms_desc)
values ('PSN001', 'ISO-8859-1', null)

insert into SystemMessage (sms_id, sms_lan, sms_desc)
values ('PSN001', 'Big5', null)

insert into SystemMessage (sms_id, sms_lan, sms_desc)
values ('PSN001', 'GB2312', null)

call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_psn001_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'PSN001' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_psn001_gb2312.txt	SystemMessage sms_desc "sms_id = 'PSN001' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_psn001_big5.txt	SystemMessage sms_desc "sms_id = 'PSN001' and sms_lan = 'Big5'"

/*
Auth: Fai
Date: 2003-09-08
Desc: add "Group Supervisors for Approval" field in regUser table, fk to usg_ent_id
*/
ALTER TABLE RegUser ADD usr_app_approval_usg_ent_id INT
GO

ALTER TABLE [dbo].[RegUser] ADD
	CONSTRAINT [FK_UsrAppApproval_USG_Entity] FOREIGN KEY
	(
		[usr_app_approval_usg_ent_id]
	) REFERENCES [dbo].[UserGroup] (
		[usg_ent_id]
	)
GO

/*
Auth: Dennis Yip
Date: 2003-09-09
Desc: Remove Auto-approved workflow template so that only each organization only has one multi-level workflow template
*/
delete from aeItemTypeTemplate where itt_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Auto-approval Workflow Template')
update aeTemplate set tpl_title = 'Multi-level-approval Workflow Template' where tpl_title = 'One-level-approval Workflow Template'
GO
update aeTemplate set tpl_owner_ent_id = 0 where tpl_title = 'Auto-approval Workflow Template'

call %UPDXMLCMD% xml\aeTemplate\tpl_workflow_multilevel_en_us.xml	aeTemplate tpl_xml "tpl_title = 'Multi-level-approval Workflow Template'"


/*
Auth: Dennis Yip
Date: 2003-09-09
Desc: Add new column to aeItem to store the application approval type selected
*/
alter table aeItem add itm_app_approval_type varchar(50) NULL
GO

/*
Auth: Dennis Yip
Date: 2003-09-09
Desc: Add new xslTemplate for multi-level workflow
*/
-- Next Approver
insert into xslTemplate
(xtp_type, xtp_subtype, xtp_channel_type ,xtp_channel_api, xtp_xsl ,xtp_xml_url ,xtp_mailmerge_ind, xtp_title)
values
('ENROLLMENT_NEXT_APPROVERS', 'HTML', 'SMTP', NULL, 'enrollment_next_approvers_notes.xsl', 'http://host:port/servlet/aeAction?', 0, null)

declare @xtp_id int

select @xtp_id = xtp_id from xslTemplate where xtp_type = 'ENROLLMENT_NEXT_APPROVERS'

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name) select @xtp_id, xpn_pos, xpn_name from xslParamName where xpn_xtp_id = 21


/*
Auth: Dennis Yip
Date: 2003-09-09
Desc: Add new xslTemplate for multi-level workflow
*/
-- No Supervisor
insert into xslTemplate
(xtp_type, xtp_subtype, xtp_channel_type ,xtp_channel_api, xtp_xsl ,xtp_xml_url ,xtp_mailmerge_ind, xtp_title)
values
('ENROLLMENT_NO_SUPERVISOR', 'HTML', 'SMTP', NULL, 'enrollment_no_supervisor_notes.xsl', 'http://host:port/servlet/aeAction?', 0, null)

declare @xtp_id int

select @xtp_id = xtp_id from xslTemplate where xtp_type = 'ENROLLMENT_NO_SUPERVISOR'

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name) select @xtp_id, xpn_pos, xpn_name from xslParamName where xpn_xtp_id = 21


/*
Auth: Dennis Yip
Date: 2003-09-09
Desc: Add new xslTemplate for multi-level workflow
*/
-- Approved Reminder
insert into xslTemplate
(xtp_type, xtp_subtype, xtp_channel_type ,xtp_channel_api, xtp_xsl ,xtp_xml_url ,xtp_mailmerge_ind, xtp_title)
values
('ENROLLMENT_APPROVED_REMINDER', 'HTML', 'SMTP', NULL, 'enrollment_approved_reminder_notes.xsl', 'http://host:port/servlet/aeAction?', 0, null)

declare @xtp_id int

select @xtp_id = xtp_id from xslTemplate where xtp_type = 'ENROLLMENT_APPROVED_REMINDER'

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name) select @xtp_id, xpn_pos, xpn_name from xslParamName where xpn_xtp_id = 21

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name) values (@xtp_id, 12, 'action_taker_ent_id')



/*
Auth: Dennis Yip
Date: 2003-09-09
Desc: Add new xslTemplate for multi-level workflow
*/
-- Withdrawal Reminder
insert into xslTemplate
(xtp_type, xtp_subtype, xtp_channel_type ,xtp_channel_api, xtp_xsl ,xtp_xml_url ,xtp_mailmerge_ind, xtp_title)
values
('ENROLLMENT_WITHDRAWAL_REMINDER', 'HTML', 'SMTP', NULL, 'enrollment_withdrawal_reminder_notes.xsl', 'http://host:port/servlet/aeAction?', 0, null)

declare @xtp_id int

select @xtp_id = xtp_id from xslTemplate where xtp_type = 'ENROLLMENT_WITHDRAWAL_REMINDER'

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name) select @xtp_id, xpn_pos, xpn_name from xslParamName where xpn_xtp_id = (SELECT xtp_id ,* FROM xslTemplate WHERE xtp_type = 'ENROLLMENT_NEW')


/*
Auth: Dennis Yip
Date: 2003-09-09
Desc: Add new xslTemplate for multi-level workflow
*/
-- Removed Reminder
insert into xslTemplate
(xtp_type, xtp_subtype, xtp_channel_type ,xtp_channel_api, xtp_xsl ,xtp_xml_url ,xtp_mailmerge_ind, xtp_title)
values
('ENROLLMENT_REMOVED_REMINDER', 'HTML', 'SMTP', NULL, 'enrollment_removed_reminder_notes.xsl', 'http://host:port/servlet/aeAction?', 0, null)

declare @xtp_id int

select @xtp_id = xtp_id from xslTemplate where xtp_type = 'ENROLLMENT_REMOVED_REMINDER'

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name) select @xtp_id, xpn_pos, xpn_name from xslParamName where xpn_xtp_id = (SELECT xtp_id FROM xslTemplate WHERE  xtp_type = 'ENROLLMENT_REMOVED_AUTO')


/*
Auth: Dennis Yip
Date: 2003-09-09
Desc: Add new xslTemplate for multi-level workflow
*/
-- Confirmed Reminder
insert into xslTemplate
(xtp_type, xtp_subtype, xtp_channel_type ,xtp_channel_api, xtp_xsl ,xtp_xml_url ,xtp_mailmerge_ind, xtp_title)
values
('ENROLLMENT_CONFIRMED_REMINDER', 'HTML', 'SMTP', NULL, 'enrollment_confirmed_reminder_notes.xsl', 'http://host:port/servlet/aeAction?', 0, null)

declare @xtp_id int

select @xtp_id = xtp_id from xslTemplate where xtp_type = 'ENROLLMENT_CONFIRMED_REMINDER'

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name) select @xtp_id, xpn_pos, xpn_name from xslParamName where xpn_xtp_id = (SELECT xtp_id ,* FROM xslTemplate WHERE xtp_type = 'ENROLLMENT_NEW')


/*
Auth: Dennis Yip
Date: 2003-09-09
Desc: Create new table aeAppnApprovalList for Pending Application Approval
*/
CREATE TABLE [dbo].[aeAppnApprovalList] (
	[aal_id] [int] IDENTITY (1, 1) NOT NULL ,
	[aal_usr_ent_id] [int] NOT NULL ,
	[aal_app_id] [int] NOT NULL ,
	[aal_app_ent_id] [int] NOT NULL ,
	[aal_approval_role] [varchar] (50) NOT NULL ,
	[aal_approval_usg_ent_id] [int] NULL ,
	[aal_status] [varchar] (50) NOT NULL ,
	[aal_create_timestamp] [datetime] NOT NULL ,
	[aal_action_taker_usr_ent_id] [int] NULL ,
	[aal_action_taker_approval_role] [varchar] (50) NULL ,
	[aal_action_taken] [varchar] (50) NULL ,
	[aal_action_timestamp] [datetime] NULL
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[aeAppnApprovalList] WITH NOCHECK ADD
	CONSTRAINT [PK_aeAppnApprovalList] PRIMARY KEY  CLUSTERED
	(
		[aal_id]
	)  ON [PRIMARY]
GO

ALTER TABLE [dbo].[aeAppnApprovalList] ADD
	CONSTRAINT [FK_aeAppnApprovalList_aeApplication] FOREIGN KEY
	(
		[aal_app_id]
	) REFERENCES [dbo].[aeApplication] (
		[app_id]
	),
	CONSTRAINT [FK_aeAppnApprovalList_RegUser] FOREIGN KEY
	(
		[aal_usr_ent_id]
	) REFERENCES [dbo].[RegUser] (
		[usr_ent_id]
	),
	CONSTRAINT [FK_aeAppnApprovalList_RegUser1] FOREIGN KEY
	(
		[aal_app_ent_id]
	) REFERENCES [dbo].[RegUser] (
		[usr_ent_id]
	),
	CONSTRAINT [FK_aeAppnApprovalList_RegUser2] FOREIGN KEY
	(
		[aal_action_taker_usr_ent_id]
	) REFERENCES [dbo].[RegUser] (
		[usr_ent_id]
	),
	CONSTRAINT [FK_aeAppnApprovalList_UserGroup] FOREIGN KEY
	(
		[aal_approval_usg_ent_id]
	) REFERENCES [dbo].[UserGroup] (
		[usg_ent_id]
	)
GO

/*
Auth: Dennis Yip
Date: 2003-09-09
Desc: Update Application form template for classroom and selfstudy
*/
update aeTemplate set tpl_title = 'Classroom Enrollment Form Template' where tpl_title = 'Approval Enrollment Form Template'

call %UPDXMLCMD% xml\aeTemplate\tpl_appnform_selfstudy.xml	aeTemplate tpl_xml "tpl_title = 'Self Study Enrollment Form Template'"
call %UPDXMLCMD% xml\aeTemplate\tpl_appnform_classroom.xml	aeTemplate tpl_xml "tpl_title = 'Classroom Enrollment Form Template'"
call %UPDXMLCMD% xml\aeTemplateView\tvw_appnform_classroom_detail.xml	aeTemplateview tvw_xml "tvw_id = 'DETAIL_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Classroom Enrollment Form Template')"

/*
Auth: kim
Date: 2003-09-10
Desc: add homepage function pending approval
*/
insert into acfunction values ('APPR_APP_LIST', 'FUNCTION', 'HOMEPAGE', getDate(), null)

call %UPDXMLCMD% xml\acFunction\ftn_appr_app_list.xml acFunction ftn_xml "ftn_ext_id = 'APPR_APP_LIST'"


/*
Auth: Fai
Date: 9/10/2003
Desc: add system message for group supervisor for approval
*/
insert into SystemMessage (sms_id, sms_lan, sms_desc)
values ('USR021', 'Big5', '!!!Cannot change the Group in the profile because the user has pending course enrollment(s).')
insert into SystemMessage (sms_id, sms_lan, sms_desc)
values ('USR021', 'GB2312', '!!!Cannot change the Group in the profile because the user has pending course enrollment(s).')
insert into SystemMessage (sms_id, sms_lan, sms_desc)
values ('USR021', 'ISO-8859-1', 'Cannot change the Group in the profile because the user has pending course enrollment(s).')

insert into SystemMessage (sms_id, sms_lan, sms_desc)
values ('USR022', 'Big5', '!!!Cannot change the Direct Supervisor in the profile because the user has pending course enrollment(s).')
insert into SystemMessage (sms_id, sms_lan, sms_desc)
values ('USR022', 'GB2312', '!!!Cannot change the Direct Supervisor in the profile because the user has pending course enrollment(s).')
insert into SystemMessage (sms_id, sms_lan, sms_desc)
values ('USR022', 'ISO-8859-1', 'Cannot change the Direct Supervisor in the profile because the user has pending course enrollment(s).')

insert into SystemMessage (sms_id, sms_lan, sms_desc)
values ('USR023', 'Big5', '!!!Cannot change the Group Supervisors for Approval in the profile because the user has pending course enrollment(s).')
insert into SystemMessage (sms_id, sms_lan, sms_desc)
values ('USR023', 'GB2312', '!!!Cannot change the Group Supervisors for Approval in the profile because the user has pending course enrollment(s).')
insert into SystemMessage (sms_id, sms_lan, sms_desc)
values ('USR023', 'ISO-8859-1', 'Cannot change the Group Supervisors for Approval in the profile because the user has pending course enrollment(s).')

insert into SystemMessage (sms_id, sms_lan, sms_desc)
values ('USR024', 'Big5', '!!!Cannot change the Supervised Group in the profile because there are course enrollment(s) pending for the user''s approval.')
insert into SystemMessage (sms_id, sms_lan, sms_desc)
values ('USR024', 'GB2312', '!!!Cannot change the Supervised Group in the profile because there are course enrollment(s) pending for the user''s approval.')
insert into SystemMessage (sms_id, sms_lan, sms_desc)
values ('USR024', 'ISO-8859-1', 'Cannot change the Supervised Group in the profile because there are course enrollment(s) pending for the user''s approval.')

call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_usr021_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'USR021' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_usr022_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'USR022' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_usr023_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'USR023' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_usr024_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'USR024' and sms_lan = 'ISO-8859-1'"

call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_usr021_gb2312.txt	SystemMessage sms_desc "sms_id = 'USR021' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_usr022_gb2312.txt	SystemMessage sms_desc "sms_id = 'USR022' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_usr023_gb2312.txt	SystemMessage sms_desc "sms_id = 'USR023' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_usr024_gb2312.txt	SystemMessage sms_desc "sms_id = 'USR024' and sms_lan = 'GB2312'"

call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_usr021_big5.txt	SystemMessage sms_desc "sms_id = 'USR021' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_usr022_big5.txt	SystemMessage sms_desc "sms_id = 'USR022' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_usr023_big5.txt	SystemMessage sms_desc "sms_id = 'USR023' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_usr024_big5.txt	SystemMessage sms_desc "sms_id = 'USR024' and sms_lan = 'Big5'"


/*
Auth: Dennis Yip
Date: 2003-09-15
Desc: grant acFunction APPR_APP_LIST (Pending Approval List) to TADM and LRNR
*/
declare @rol_id int
declare @rol_ext_id varchar(50)
declare @ftn_id int
declare rol_cur cursor for
select rol_id, rol_ext_id from acRole where rol_ste_uid in ('TADM', 'LRNR')

select @ftn_id = ftn_id from acFunction where ftn_ext_id = 'APPR_APP_LIST'

open rol_cur
fetch next from rol_cur into @rol_id, @rol_ext_id
while @@fetch_status = 0
begin
	insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_creation_timestamp) values (@rol_id, @ftn_id, getdate())
	insert into acHomepage (ac_hom_ent_id, ac_hom_rol_ext_id, ac_hom_ftn_ext_id, ac_hom_create_usr_id, ac_hom_create_timestamp)
	values (NULL, @rol_ext_id, 'APPR_APP_LIST', 's1u3', getdate())

	fetch next from rol_cur into @rol_id, @rol_ext_id
end
close rol_cur
deallocate rol_cur

/*
Auth: Dennis Yip
Date: 2003-09-15
Desc: Update existing item's workflow template to the new multi-level workflow template
*/
update aeItem set itm_app_approval_type = 'TADM'
where itm_id in (select itp_itm_id from aeITemTemplate where itm_id = itp_itm_id and itp_ttp_id = 1 and itp_tpl_id in (select tpl_id from aeTemplate where tpl_ttp_id = 1 and tpl_title = 'Multi-level-approval Workflow Template'))

update aeItemTemplate set itp_tpl_id = 15
where itp_tpl_id = 7


/*
Auth: Dennis Yip
Date: 2003-10-02
Desc: Add new column to ForumMessage for case-insensitive search
*/
alter table forumMessage add fmg_msg_searchable_content ntext default 'temp' not null
GO
--execute a java program (ForumMessageConverter) to insert values into the new column

/*
Auth: Dennis Yip
Date: 2003-10-02
Desc: Shorten column res_annotation to make room for res_desc to be changed from ntext to nvarchar to support case-insensitive search
*/
alter table resources alter column res_annotation nvarchar(1000) null
GO
alter table resources add res_long_desc ntext null
GO
update resources set res_long_desc = res_desc
GO
alter table resources drop column res_desc
GO
alter table resources add res_desc nvarchar(1000) null
GO
update resources set res_desc = convert(nvarchar(1000),res_long_desc )

/*
Auth: Dennis Yip
Date: 2003-10-20
Desc: insert records into aeAppnApprovalList for the 'Pending Approval' appliactions
      that does not have any record in aeAppnApprovalList since the applications are
      created before the multi-level workflow is deployed
*/
declare @app_id int
declare @app_ent_id int
declare @app_itm_id int
declare @iac_ent_id int

declare app_cursor cursor for
select app_id, app_ent_id, app_itm_id from aeApplication
where app_process_status = 'Pending Approval'
and app_id not in (select aal_app_id from aeAppnApprovalList where aal_status = 'PENDING')
order by app_id

open app_cursor
fetch next from app_cursor into @app_id, @app_ent_id, @app_itm_id
while @@fetch_status = 0
begin
	declare iac_cursor cursor for
	select iac_ent_id from aeItemAccess where iac_itm_id = @app_itm_id and iac_access_id like 'TADM%'
	open iac_cursor
	fetch next from iac_cursor into @iac_ent_id
	while @@fetch_status = 0
	begin
		insert into aeAppnApprovalList (aal_usr_ent_id, aal_app_id, aal_app_ent_id, aal_approval_role, aal_status, aal_create_timestamp)
		values (@iac_ent_id, @app_id, @app_ent_id, 'TADM', 'PENDING', getdate())

		fetch next from iac_cursor into @iac_ent_id
	end
	close iac_cursor
	deallocate iac_cursor
	fetch next from app_cursor into @app_id, @app_ent_id, @app_itm_id
end
close app_cursor
deallocate app_cursor


/*
Auth: Dennis Yip
Date: 2003-10-21
Desc: change the system message from "user''s" to "user's"
*/
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_usr024_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'USR024' and sms_lan = 'ISO-8859-1'"

/*
Auth: Lun
Date: 29/10/2003
Desc: add system message for catagory not exist
*/
Insert Into SystemMessage Values ( 'AETN08', 'ISO-8859-1', 'The category does not exist: $data')
Insert Into SystemMessage Values ( 'AETN08', 'Big5', '!!!The category does not exist: $data')
Insert Into SystemMessage Values ( 'AETN08', 'GB2312', '!!!The category does not exist: $data.')
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_aetn08_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'AETN08' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_aetn08_big5.txt	SystemMessage sms_desc "sms_id = 'AETN08' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_aetn08_gb2312.txt	SystemMessage sms_desc "sms_id = 'AETN08' and sms_lan = 'GB2312'"

/*
Author: Dennis Yip
Date: 2003-11-06
Desc: Changes for adding "show answer option" and "submission after passed option" for standard test and dynamic test
*/
ALTER TABLE module
	ADD mod_show_answer_ind int NULL
GO
UPDATE module SET mod_show_answer_ind = -1
UPDATE module SET mod_show_answer_ind = 0 WHERE mod_type in ('TST','DXT')

ALTER TABLE module
	ADD mod_sub_after_passed_ind int NULL
GO
UPDATE module SET mod_sub_after_passed_ind = -1
UPDATE module SET mod_sub_after_passed_ind = 0 WHERE mod_type in ('TST','DXT')

insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('MOD007', 'ISO-8859-1', 'The record is updated successfully.<BR/>Since the test does not have any question/criterion defined, it cannot be turned to Online.')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('MOD007', 'Big5', '!!!The record is updated successfully.<BR/>Since the test does not have any question/criterion defined, it cannot be turned to Online.')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('MOD007', 'GB2312', '!!!The record is updated successfully.<BR/>Since the test does not have any question/criterion defined, it cannot be turned to Online.')

insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('MOD008', 'ISO-8859-1', 'The test has no question/criterion defined.')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('MOD008', 'Big5', '!!!The test has no question/criterion defined.')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('MOD008', 'GB2312', '!!!The test has no question/criterion defined.')

insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('PGR008', 'ISO-8859-1', 'You cannot submit the answers because you have already passed the test. This test does not allow further attempts when a passed status is obtained.')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('PGR008', 'Big5', '!!!You cannot submit the answers because you have already passed the test. This test does not allow further attempts when a passed status is obtained.')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('PGR008', 'GB2312', '!!!You cannot submit the answers because you have already passed the test. This test does not allow further attempts when a passed status is obtained.')

call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_mod007_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'MOD007' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_mod007_big5.txt		SystemMessage sms_desc "sms_id = 'MOD007' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_mod007_gb2312.txt		SystemMessage sms_desc "sms_id = 'MOD007' and sms_lan = 'GB2312'"

call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_mod008_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'MOD008' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_mod008_big5.txt		SystemMessage sms_desc "sms_id = 'MOD008' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_mod008_gb2312.txt		SystemMessage sms_desc "sms_id = 'MOD008' and sms_lan = 'GB2312'"

call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_pgr008_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'PGR008' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_pgr008_big5.txt		SystemMessage sms_desc "sms_id = 'PGR008' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_pgr008_gb2312.txt		SystemMessage sms_desc "sms_id = 'PGR008' and sms_lan = 'GB2312'"

call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_pgr005_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'PGR005' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_pgr005_big5.txt		SystemMessage sms_desc "sms_id = 'PGR005' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_pgr005_gb2312.txt		SystemMessage sms_desc "sms_id = 'PGR005' and sms_lan = 'GB2312'"

call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_mod004_big5.txt		SystemMessage sms_desc "sms_id = 'MOD004' and sms_lan = 'Big5'"



/*
Author: Dennis Yip
Date: 2003-11-06
Desc: Changes for tracking user attemp even the user does not submit test result for standard test and dynamic test
*/
alter table Progress add pgr_completion_status varchar(10) null
GO
update Progress set pgr_completion_status = 'C' where pgr_res_id in (select res_id from Resources where res_subtype in ('SVY', 'ASS'))
update Progress set pgr_completion_status = 'P' where pgr_completion_status is null and pgr_score/pgr_max_score*100 >= (select mod_pass_score from module where mod_res_id = pgr_res_id)
update Progress set pgr_completion_status = 'F' where pgr_completion_status is null and pgr_score/pgr_max_score*100 < (select mod_pass_score from module where mod_res_id = pgr_res_id)

alter table ModuleEvaluationHistory add mvh_id int identity(1,1)
GO
alter table ModuleEvaluationHistory drop constraint PK_ModEvaHistory
GO
ALTER TABLE [dbo].[ModuleEvaluationHistory] WITH NOCHECK ADD
	CONSTRAINT [PK_ModEvaHistory] PRIMARY KEY  NONCLUSTERED
	(
		[mvh_id]
	) WITH  FILLFACTOR = 90  ON [PRIMARY]
GO

/*
Author: Dennis Yip
Date: 2003-11-06
Desc: Changes for tracking user attemp even the user does not submit test result for standard test and dynamic test
*/
call %UPDXMLCMD% xml\aeTemplate\tpl_workflow_multilevel_en_us.xml	aeTemplate tpl_xml "tpl_title = 'Multi-level-approval Workflow Template'"

/*
Author: Emily Li
Date: 2003-11-07
Desc: gb translation error fixed
*/
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_aeit18_gb2312.txt	SystemMessage sms_desc "sms_id = 'AEIT18' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_ulg010_gb2312.txt	SystemMessage sms_desc "sms_id = 'ULG010' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_usr010_gb2312.txt	SystemMessage sms_desc "sms_id = 'USR010' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_usr012_gb2312.txt	SystemMessage sms_desc "sms_id = 'USR012' and sms_lan = 'GB2312'"

call %UPDXMLCMD% xml\fmFacilityType\ftp_title_1.xml	fmFacilityType	ftp_title_xml	"ftp_id=1"
call %UPDXMLCMD% xml\fmFacilityType\ftp_title_2.xml	fmFacilityType	ftp_title_xml	"ftp_id=2"
call %UPDXMLCMD% xml\fmFacilityType\ftp_title_3.xml	fmFacilityType	ftp_title_xml	"ftp_id=3"
call %UPDXMLCMD% xml\fmFacilityType\ftp_title_4.xml	fmFacilityType	ftp_title_xml	"ftp_id=4"
call %UPDXMLCMD% xml\fmFacilityType\ftp_title_5.xml	fmFacilityType	ftp_title_xml	"ftp_id=5"
call %UPDXMLCMD% xml\fmFacilityType\ftp_title_6.xml	fmFacilityType	ftp_title_xml	"ftp_id=6"
call %UPDXMLCMD% xml\fmFacilityType\ftp_title_7.xml	fmFacilityType	ftp_title_xml	"ftp_id=7"
call %UPDXMLCMD% xml\fmFacilityType\ftp_title_8.xml	fmFacilityType	ftp_title_xml	"ftp_id=8"
call %UPDXMLCMD% xml\fmFacilityType\ftp_title_9.xml	fmFacilityType	ftp_title_xml	"ftp_id=9"


/*
Auth: Lun
Date: 11/11/2003
Desc: add system message for invalid timestamp of updating item
*/
Insert Into SystemMessage Values ( 'AEIT19', 'ISO-8859-1', 'Your modification cannot be saved because the record has just been modified by another user. You are advised to:<br>
<li>Note down any important modification you intend to make below by e.g. copying into a text file</li>
<li>Click OK at the bottom of the page to review the latest information of the record</li>
<li>Make necessary modification again using the notes you made above</li>')

Insert Into SystemMessage Values ( 'AEIT19', 'Big5', '!!!Your modification cannot be saved because the record has just been modified by another user. You are advised to:<br>
<li>Note down any important modification you intend to make below by e.g. copying into a text file</li>
<li>Click OK at the bottom of the page to review the latest information of the record</li>
<li>Make necessary modification again using the notes you made above</li>')

Insert Into SystemMessage Values ( 'AEIT19', 'GB2312', '!!!Your modification cannot be saved because the record has just been modified by another user. You are advised to:<br>
<li>Note down any important modification you intend to make below by e.g. copying into a text file</li>
<li>Click OK at the bottom of the page to review the latest information of the record</li>
<li>Make necessary modification again using the notes you made above</li>')

call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_aeit19_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'AEIT19' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_aeit19_big5.txt	SystemMessage sms_desc "sms_id = 'AEIT19' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_aeit19_gb2312.txt	SystemMessage sms_desc "sms_id = 'AEIT19' and sms_lan = 'GB2312'"


/*
Auth: Lun
Date: 11/11/2003
Desc: add system message for checking unicode file
*/
Insert Into SystemMessage Values ( 'GEN008', 'ISO-8859-1', 'The file uploaded is not encoded in Unicode.')

Insert Into SystemMessage Values ( 'GEN008', 'Big5', '!!!The file uploaded is not encoded in Unicode.')

Insert Into SystemMessage Values ( 'GEN008', 'GB2312', '!!!The file uploaded is not encoded in Unicode.')

call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_gen008_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'GEN008' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_gen008_big5.txt	SystemMessage sms_desc "sms_id = 'GEN008' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_gen008_gb2312.txt	SystemMessage sms_desc "sms_id = 'GEN008' and sms_lan = 'GB2312'"

/*
Auth: kawai
Date: 2003-11-12
Desc: take away the unnecessary learning resource manage public resource function,
      this function has to be removed from all roles because this function is not role specific
*/
delete from acRoleFunction where
rfn_ftn_id in (select ftn_id from acFunction where ftn_ext_id in ('RES_MGT_PUBLIC'))

/*
Auth: Lun
Date: 11/11/2003
Desc: Update system message for checking user password
*/
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_usr017_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'USR017' and sms_lan = 'ISO-8859-1'"

/*
Auth: Lun
Date: 12/11/2003
Desc: Update system message for user has pending approval
*/
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_usr023_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'USR023' and sms_lan = 'ISO-8859-1'"

/*
Auth: Dennis Yip
Date: 2003-11-14
Desc: Add new column aal_aah_id to join aeAppnApprovalList to aeAppnActnHistory
*/
alter table aeAppnApprovalList add aal_aah_id int null
GO

update aeAppnApprovalList set aal_aah_id =
(select aah_id
from aeAppnActnHistory, RegUser
where aal_app_id = aah_app_id
and aah_create_usr_id = usr_id
and aal_action_taker_usr_ent_id = usr_ent_id
and abs(datediff(s,aah_create_timestamp,aal_action_timestamp))  <= 3)
where aal_status = 'HISTORY'

ALTER TABLE [dbo].[aeAppnApprovalList] ADD
	CONSTRAINT [FK_aeAppnApprovalList_aeAppnActnHistory] FOREIGN KEY
	(
		[aal_aah_id]
	) REFERENCES [dbo].[aeAppnActnHistory] (
		[aah_id]
	)
GO

/*
Auth: Dennis Yip
Date: 2003-11-14
Desc: Create new system messages for change of enrollment workflow
*/
Insert Into SystemMessage Values ( 'AEIT20', 'ISO-8859-1', 'Enrollment workflow cannot be changed in class level. It can only be changed in course level.')
Insert Into SystemMessage Values ( 'AEIT20', 'Big5', '!!!Enrollment workflow cannot be changed in class level. It can only be changed in course level.')
Insert Into SystemMessage Values ( 'AEIT20', 'GB2312', '!!!Enrollment workflow cannot be changed in class level. It can only be changed in course level.')

Insert Into SystemMessage Values ( 'AEIT21', 'ISO-8859-1', 'Enrollment workflow cannot be changed. The Learning Solution does not support enrollment workflow.')
Insert Into SystemMessage Values ( 'AEIT21', 'Big5', '!!!Enrollment workflow cannot be changed. The Learning Solution does not support enrollment workflow.')
Insert Into SystemMessage Values ( 'AEIT21', 'GB2312', '!!!Enrollment workflow cannot be changed. The Learning Solution does not support enrollment workflow.')

Insert Into SystemMessage Values ( 'AEIT22', 'ISO-8859-1', 'Enrollment workflow cannot be changed. Please close all pending applications of this Learning Solution (or its classes) first.')
Insert Into SystemMessage Values ( 'AEIT22', 'Big5', '!!!Enrollment workflow cannot be changed. Please close all pending applications of this Learning Solution (or its classes) first.')
Insert Into SystemMessage Values ( 'AEIT22', 'GB2312', '!!!Enrollment workflow cannot be changed. Please close all pending applications of this Learning Solution (or its classes) first.')

Insert Into SystemMessage Values ( 'AEIT23', 'ISO-8859-1', 'Enrollment workflow is updated successfully.')
Insert Into SystemMessage Values ( 'AEIT23', 'Big5', '!!!Enrollment workflow is updated successfully.')
Insert Into SystemMessage Values ( 'AEIT23', 'GB2312', '!!!Enrollment workflow is updated successfully.')

call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_aeit20_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'AEIT20' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_aeit20_big5.txt	SystemMessage sms_desc "sms_id = 'AEIT20' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_aeit20_gb2312.txt	SystemMessage sms_desc "sms_id = 'AEIT20' and sms_lan = 'GB2312'"

call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_aeit21_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'AEIT21' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_aeit21_big5.txt	SystemMessage sms_desc "sms_id = 'AEIT21' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_aeit21_gb2312.txt	SystemMessage sms_desc "sms_id = 'AEIT21' and sms_lan = 'GB2312'"

call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_aeit22_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'AEIT22' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_aeit22_big5.txt	SystemMessage sms_desc "sms_id = 'AEIT22' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_aeit22_gb2312.txt	SystemMessage sms_desc "sms_id = 'AEIT22' and sms_lan = 'GB2312'"

call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_aeit23_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'AEIT23' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_aeit23_big5.txt	SystemMessage sms_desc "sms_id = 'AEIT23' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_aeit23_gb2312.txt	SystemMessage sms_desc "sms_id = 'AEIT23' and sms_lan = 'GB2312'"

/*
Auth: Dennis Yip
Date: 2003-11-14
Desc: Change all existing personal resources (res_privilege='AUTHOR') to public (res_privilege='CW')
      as we do not have personal resource any more
*/
update resources set res_privilege = 'CW' where res_privilege = 'AUTHOR'

/*
Auth: Dennis Yip
Date: 2003-11-14
Desc: Add warning messages to workflow template and change the all the remove actions to "-1"
*/
call %UPDXMLCMD% xml\aeTemplate\tpl_workflow_multilevel_en_us.xml	aeTemplate tpl_xml "tpl_title = 'Multi-level-approval Workflow Template'"


/*
Auth: Lun
Date: 2003-11-17
Desc: Update warning message of updating item
*/
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_aetn08_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'AETN08' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_aetn08_big5.txt	SystemMessage sms_desc "sms_id = 'AETN08' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_aetn08_gb2312.txt	SystemMessage sms_desc "sms_id = 'AETN08' and sms_lan = 'GB2312'"

call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_aeit06_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'AEIT06' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_aeit06_big5.txt	SystemMessage sms_desc "sms_id = 'AEIT06' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_aeit06_gb2312.txt	SystemMessage sms_desc "sms_id = 'AEIT06' and sms_lan = 'GB2312'"

/*
Auth: kawai
Date: 2003-11-17
Desc: update the display name of self-study
*/
call %UPDXMLCMD% xml\aeItemType\ity_title_selfstudy.xml		aeItemType ity_title_xml "ity_id = 'SELFSTUDY'"

/*
Auth: Dennis Yip
Date: 2003-11-18
Desc: Add system message to be shown when the system cannot track the learners' progress on Netg module
*/
Insert Into SystemMessage Values ( 'PGR009', 'ISO-8859-1', 'Cannot save your progress. $data')
Insert Into SystemMessage Values ( 'PGR009', 'Big5', '!!!Cannot save your progress. $data')
Insert Into SystemMessage Values ( 'PGR009', 'GB2312', '!!!Cannot save your progress. $data')

call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_pgr009_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'PGR009' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_pgr009_big5.txt	SystemMessage sms_desc "sms_id = 'PGR009' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_pgr009_gb2312.txt	SystemMessage sms_desc "sms_id = 'PGR009' and sms_lan = 'GB2312'"

/*
Auth: Dennis Yip
Date: 2003-11-18
Desc: Add system message to be shown when the system cannot track the learners' progress on Netg module
*/
Insert Into SystemMessage Values ( 'MOD009', 'ISO-8859-1', 'The interface file is not in correct format. Please contact your system administrator for more information.')
Insert Into SystemMessage Values ( 'MOD009', 'Big5', '!!!The interface file is not in correct format. Please contact your system administrator for more information.')
Insert Into SystemMessage Values ( 'MOD009', 'GB2312', '!!!The interface file is not in correct format. Please contact your system administrator for more information.')

call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_mod009_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'MOD009' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_mod009_big5.txt	SystemMessage sms_desc "sms_id = 'MOD009' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_mod009_gb2312.txt	SystemMessage sms_desc "sms_id = 'MOD009' and sms_lan = 'GB2312'"

/*
Auth: Dennis Yip
Date: 2003-11-18
Desc: Enlarge the field length from varchar(10) to varchar(20) to support multiple selection of question types (e.g. 'MT~FB~MC~TF')
*/
alter table ModuleSpec alter column msp_type varchar(20) null

/*
Auth: Dennis Yip
Date: 2003-11-18
Desc: Change the workflow action "Enroll" to "Confirm" and its verb from "Enrolled" to "Confirmed"
*/
call %UPDXMLCMD% xml\aeTemplate\tpl_workflow_multilevel_en_us.xml	aeTemplate tpl_xml "tpl_title = 'Multi-level-approval Workflow Template'"

/*
Auth: Dennis Yip
Date: 2003-11-18
Desc: Update system message for changing learning solution workflow
*/
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_aeit22_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'AEIT22' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_aeit22_big5.txt	SystemMessage sms_desc "sms_id = 'AEIT22' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_aeit22_gb2312.txt	SystemMessage sms_desc "sms_id = 'AEIT22' and sms_lan = 'GB2312'"

/*
Auth: Fai
Date: 2003-11-19
Desc: Alter the lenght of usr_pwd in reguser for encrypted pwd
*/
alter table reguser alter column usr_pwd varchar(30)

/*
Auth: Dennis Yip
Date: 2003-11-20
Desc: Update ObjectView for Management Report > Course Evaluation Report > Item Object View
*/
call %UPDXMLCMD% xml\ObjectView\ojv_option_survey_course_report_item.xml	ObjectView ojv_option_xml "ojv_owner_ent_id = 1 AND ojv_type = 'SURVEY_COURSE_REPORT' AND ojv_subtype = 'ITM'"

/*
Auth: Dennis Yip
Date: 2003-11-21
Desc: Grant permission to update, delete all public resources in Resource Management to TA
*/
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_creation_timestamp)
(select rol_id, ftn_id, getdate() from acRole, acFunction where rol_ext_id = 'TADM_1' and ftn_ext_id = 'RES_MGT_PUBLIC')

/*
Auth: Dennis Yip
Date: 2003-11-21
Desc: Change the name of "Category" to "Folder" in Resource Management
*/
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_obj002_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'OBJ002' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_obj003_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'OBJ003' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_obj004_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'OBJ004' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_obj006_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'OBJ006' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_obj010_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'OBJ010' and sms_lan = 'ISO-8859-1'"

call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_obj002_big5.txt	SystemMessage sms_desc "sms_id = 'OBJ002' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_obj003_big5.txt	SystemMessage sms_desc "sms_id = 'OBJ003' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_obj004_big5.txt	SystemMessage sms_desc "sms_id = 'OBJ004' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_obj006_big5.txt	SystemMessage sms_desc "sms_id = 'OBJ006' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_obj010_big5.txt	SystemMessage sms_desc "sms_id = 'OBJ010' and sms_lan = 'Big5'"

call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_obj002_gb2312.txt	SystemMessage sms_desc "sms_id = 'OBJ002' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_obj003_gb2312.txt	SystemMessage sms_desc "sms_id = 'OBJ003' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_obj006_gb2312.txt	SystemMessage sms_desc "sms_id = 'OBJ006' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_obj010_gb2312.txt	SystemMessage sms_desc "sms_id = 'OBJ010' and sms_lan = 'GB2312'"

/*
Auth: Dennis Yip
Date: 2003-11-21
Desc: Add new param name to xslTemplate 'ENROLLMENT_NOT_CONFIRMED' for printing approver name to the email
*/
insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name) values (33, 12, 'action_taker_ent_id')

/*
Auth: Norman
Date: 2003-11-21
Desc: Update system messages
*/
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_usr021_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'USR021' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_usr022_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'USR022' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_usr023_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'USR023' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_acl002_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'ACL002' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_mod004_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'MOD004' and sms_lan = 'ISO-8859-1'"

call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_usr022_big5.txt	SystemMessage sms_desc "sms_id = 'USR022' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_usr023_big5.txt	SystemMessage sms_desc "sms_id = 'USR023' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_mod004_big5.txt	SystemMessage sms_desc "sms_id = 'MOD004' and sms_lan = 'Big5'"


/**
Author: Lun
Desc: Add the identity field msp_id to ModuleSpec
Date: 2003-06-20
*/
ALTER TABLE ModuleSpec add msp_id INT NOT NULL IDENTITY (1, 1)
ALTER TABLE dbo.ModuleSpec
	DROP CONSTRAINT PK_ModuleSpec
GO
ALTER TABLE dbo.ModuleSpec ADD CONSTRAINT
	PK_ModuleSpec PRIMARY KEY NONCLUSTERED
	(
	msp_id
	) ON [PRIMARY]
GO


/**
Author: Lun
Desc: Update system message
Date: 2003-11-24
*/
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_aeit19_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'AEIT19' and sms_lan = 'ISO-8859-1'"

/**
Author: Marcus
Desc: Update system message
Date: 2003-11-27
*/
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_aeit06_big5.txt	SystemMessage sms_desc "sms_id = 'AEIT06' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_aeit19_big5.txt	SystemMessage sms_desc "sms_id = 'AEIT19' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_gen008_big5.txt	SystemMessage sms_desc "sms_id = 'GEN008' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_aetn08_big5.txt	SystemMessage sms_desc "sms_id = 'AETN08' and sms_lan = 'Big5'"

/**
Author: Emily
Desc: Update system message
Date: 2003-11-27
*/
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_aeit06_gb2312.txt	SystemMessage sms_desc "sms_id = 'AEIT06' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_aeit19_gb2312.txt	SystemMessage sms_desc "sms_id = 'AEIT19' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_aeit20_gb2312.txt	SystemMessage sms_desc "sms_id = 'AEIT20' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_aeit21_gb2312.txt	SystemMessage sms_desc "sms_id = 'AEIT21' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_aeit22_gb2312.txt	SystemMessage sms_desc "sms_id = 'AEIT22' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_aeit23_gb2312.txt	SystemMessage sms_desc "sms_id = 'AEIT23' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_aetn08_gb2312.txt	SystemMessage sms_desc "sms_id = 'AETN08' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_mod007_gb2312.txt	SystemMessage sms_desc "sms_id = 'MOD007' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_mod008_gb2312.txt	SystemMessage sms_desc "sms_id = 'MOD008' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_mod009_gb2312.txt	SystemMessage sms_desc "sms_id = 'MOD009' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_pgr005_gb2312.txt	SystemMessage sms_desc "sms_id = 'PGR005' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_pgr008_gb2312.txt	SystemMessage sms_desc "sms_id = 'PGR008' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_pgr009_gb2312.txt	SystemMessage sms_desc "sms_id = 'PGR009' and sms_lan = 'GB2312'"

/**
Author: Emily
Desc: Update multi-lingual xml
Date: 2003-11-27
*/
call %UPDXMLCMD% xml\ReportTemplate\rte_title_global_enrollment.xml	ReportTemplate rte_title_xml "rte_type = 'GLOBAL_ENROLLMENT'"
call %UPDXMLCMD% xml\ReportTemplate\rte_title_survey_cos_grp.xml	ReportTemplate rte_title_xml "rte_type = 'SURVEY_COS_GRP'"
call %UPDXMLCMD% xml\acFunction\ftn_cos_evn_main.xml	acFunction ftn_xml "ftn_ext_id = 'COS_EVN_MAIN'"
call %UPDXMLCMD% xml\acFunction\ftn_glb_cat_main.xml	acFunction ftn_xml "ftn_ext_id = 'GLB_CAT_MAIN'"
call %UPDXMLCMD% xml\acFunction\ftn_glb_rpt_link.xml	acFunction ftn_xml "ftn_ext_id = 'GLB_RPT_LINK'"

/**
Author: Emily
Desc: Update system message
Date: 2003-11-27 (pm)
*/
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_gen008_gb2312.txt	SystemMessage sms_desc "sms_id = 'GEN008' and sms_lan = 'GB2312'"

/**
Author: Kim
Desc: alter the field : lang and skin of the table psnpreference, to support null when set language/skin only in my preference
Date: 2003-12-02
*/
ALTER TABLE psnPreference ALTER COLUMN pfr_skin_id  varchar (20) null
ALTER TABLE psnPreference ALTER COLUMN pfr_lang  varchar (10) null

/**
Author: kawai
Desc: Update views of classroom
Date: 2003-12-11
*/
call %UPDXMLCMD% xml\aeTemplateView\tvw_item_classroom_apply.xml	aeTemplateview tvw_xml "tvw_id = 'APPLY_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Classroom Item Template')"
call %UPDXMLCMD% xml\aeTemplateView\tvw_item_classroom_run_apply.xml	aeTemplateview tvw_xml "tvw_id = 'APPLY_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Classroom Run ItemTemplate')"
call %UPDXMLCMD% xml\aeTemplateView\tvw_item_classroom_run_lrn.xml	aeTemplateview tvw_xml "tvw_id = 'LRN_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Classroom Run ItemTemplate')"
call %UPDXMLCMD% xml\aeTemplateView\tvw_item_selfstudy_apply.xml	aeTemplateview tvw_xml "tvw_id = 'APPLY_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Self Study Item Template')"

/**
Author: kawai
Desc: this is a data conversion for password expiry
      set the default last update date of password to the sign-up date
Date: 2003-12-11
*/
UPDATE RegUser
SET usr_pwd_upd_timestamp = usr_signup_date
WHERE usr_pwd_upd_timestamp IS NULL

/**
Author: Dennis
Desc: Add new SystemMessage
Date: 2003-12-16
*/
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('USR025', 'ISO-8859-1', 'Cannot delete the user because there are course enrollment(s) pending for the user''s approval.')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('USR025', 'Big5', '!!!Cannot delete the user because there are course enrollment(s) pending for the user''s approval.')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('USR025', 'GB2312', '!!!Cannot delete the user because there are course enrollment(s) pending for the user''s approval.')

insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('USR026', 'ISO-8859-1', 'Cannot delete the user because he/she is the sole $data of the following course(s):<br/>$data')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('USR026', 'Big5', '!!!Cannot delete the user because he/she is the sole $data of the following course(s):<br/>$data')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('USR026', 'GB2312', '!!!Cannot delete the user because he/she is the sole $data of the following course(s):<br/>$data')

call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_usr025_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'USR025' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_usr025_gb2312.txt	SystemMessage sms_desc "sms_id = 'USR025' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_usr025_big5.txt	SystemMessage sms_desc "sms_id = 'USR025' and sms_lan = 'Big5'"

call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_usr026_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'USR026' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_usr026_gb2312.txt	SystemMessage sms_desc "sms_id = 'USR026' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_usr026_big5.txt	SystemMessage sms_desc "sms_id = 'USR026' and sms_lan = 'Big5'"

/**
Author: kawai
Desc: Updated the label of some functions: Forum, Knowledge Management, and Announcement
Date: 2003-12-16
*/
call %UPDXMLCMD% xml\acFunction\ftn_for_link.xml	acFunction ftn_xml "ftn_ext_id = 'FOR_LINK'"
call %UPDXMLCMD% xml\acFunction\ftn_for_main.xml	acFunction ftn_xml "ftn_ext_id = 'FOR_MAIN'"
call %UPDXMLCMD% xml\acFunction\ftn_kb_mgt.xml		acFunction ftn_xml "ftn_ext_id = 'KB_MGT'"
call %UPDXMLCMD% xml\acFunction\ftn_kb_mgt_admin.xml	acFunction ftn_xml "ftn_ext_id = 'KB_MGT_ADMIN'"
call %UPDXMLCMD% xml\acFunction\ftn_sys_msg_list.xml	acFunction ftn_xml "ftn_ext_id = 'SYS_MSG_LIST'"

/**
Author: Lun
Desc: Modify column type
Date: 2004-01-14
*/
[SQL]
Alter Table kmObjectTypeTemplate Alter Column ott_ttp_id int not null

[Oracle]
Alter Table KmObjectTypeTemplate ADD TEMP_COL number(10);
Update KmObjectTypeTemplate Set TEMP_COL = OTT_TTP_ID;
Update KmObjectTypeTemplate Set OTT_TTP_ID = null;
Alter Table KmObjectTypeTemplate MODIFY OTT_TTP_ID number(10);
Update KmObjectTypeTemplate Set OTT_TTP_ID = TEMP_COL;
Alter Table KmObjectTypeTemplate Modify OTT_TTP_ID number(10) not null;
Alter Table KmObjectTypeTemplate Drop COLUMN TEMP_COL;
Commit;

/**
Author: Kim
Desc: Add new SystemMessage
Date: 2004-01-16
*/
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('MOD010', 'ISO-8859-1', null)
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('MOD010', 'Big5', null)
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('MOD010', 'GB2312', null)

insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('MOD011', 'ISO-8859-1', null)
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('MOD011', 'Big5', null)
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('MOD011', 'GB2312', null)

call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_mod010_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'MOD010' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_mod010_gb2312.txt	SystemMessage sms_desc "sms_id = 'MOD010' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_mod010_big5.txt	SystemMessage sms_desc "sms_id = 'MOD010' and sms_lan = 'Big5'"

call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_mod011_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'MOD011' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_mod011_gb2312.txt	SystemMessage sms_desc "sms_id = 'MOD011' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_mod011_big5.txt	SystemMessage sms_desc "sms_id = 'MOD011' and sms_lan = 'Big5'"

/**
Author: Kim
Desc: Disable completion criteria for classroom item
Date: 2004-01-16
*/
update aeItemType set ity_completion_criteria_ind = 0 where ity_id = 'CLASSROOM'
update aeItem set itm_completion_criteria_ind = 0 where itm_type = 'CLASSROOM'

/*
Author: Dennis Yip
Date: 2004-03-09
Desc: Update all roles' homepage to use home.xsl, remove home.jsp
*/
update acRole set rol_url_home = 'servlet/qdbAction?cmd=home&stylesheet=home.xsl'

/*
Author: Dennis Yip
Date: 2004-03-15
Desc: create primary key for psnPerference
*/
ALTER TABLE [dbo].[psnPreference] WITH NOCHECK ADD
	CONSTRAINT [PK_psnPreference] PRIMARY KEY  CLUSTERED
	(
		[pfr_ent_id]
	)  ON [PRIMARY]
GO

/*
Author: Dennis Yip
Date: 2004-03-15
Desc: create index for Message
*/
 CREATE  INDEX [I_Message] ON [dbo].[Message]([msg_type], [msg_status]) ON [PRIMARY]
GO

/*
Author: Dennis Yip
Date: 2004-03-15
Desc: create index for acHomepage
*/
 CREATE  INDEX [I_acHomePage_rol_ext_id] ON [dbo].[acHomePage]([ac_hom_rol_ext_id]) ON [PRIMARY]
GO

/*
Author: Emily Li
Date: 2004-03-16
Desc: replace the index (gpm_ent_id_member, ) with (gpm_ent_id_member) to improve the performance of report viewing
*/
BEGIN TRANSACTION
SET QUOTED_IDENTIFIER ON
SET TRANSACTION ISOLATION LEVEL SERIALIZABLE
COMMIT
BEGIN TRANSACTION
DROP INDEX dbo.GroupMember.IX_GroupMember
GO
CREATE NONCLUSTERED INDEX IX_GroupMember ON dbo.GroupMember
 (
 gpm_ent_id_member
 ) ON [PRIMARY]
COMMIT
GO

/*
Author: Dennis Yip
Date: 2004-03-17
Desc: Update workflow template so that the "Enrollment Received" email will not be sent to learners applying "no-approval" courses
*/
call %UPDXMLCMD% xml\aeTemplate\tpl_workflow_multilevel_en_us.xml	aeTemplate tpl_xml "tpl_title = 'Multi-level-approval Workflow Template'"

/*
Author: kawai
Date: 2004-03-24
Desc: create an index for tnd_parent_tnd_id such that queries by this column can be faster
*/
CREATE
  INDEX [IX_aeTreeNode_tnd_parent_tnd_id] ON [dbo].[aeTreeNode] ([tnd_parent_tnd_id])

/*
Author: kawai
Date: 2004-03-24
Desc: convert all varchar columns to nvarchar (other column attributes remain the same)
      this change is needed for MSSQL Server ONLY!!!
*/

-- directly alter the column data type for those columns which are not referred by other objects
alter table Accomplishment alter column apm_aicc_score nvarchar(255) NULL
alter table Accomplishment alter column apm_status nvarchar(50) NULL
alter table acEntity alter column ac_ent_create_usr_id nvarchar(20) NOT NULL
alter table acFunction alter column ftn_level nvarchar(50) NULL
alter table acFunction alter column ftn_type nvarchar(50) NULL
alter table acReportTemplate alter column ac_rte_create_usr_id nvarchar(20) NOT NULL
alter table acReportTemplate alter column ac_rte_ftn_ext_id nvarchar(255) NOT NULL
alter table acReportTemplate alter column ac_rte_rol_ext_id nvarchar(255) NULL
alter table acResources alter column ac_res_create_usr_id nvarchar(20) NOT NULL
alter table acRole alter column rol_skin_root nvarchar(50) NOT NULL
alter table acRole alter column rol_status nvarchar(20) NOT NULL
alter table acRole alter column rol_ste_uid nvarchar(20) NULL
alter table acRole alter column rol_target_ent_type nvarchar(50) NULL
alter table acSignonLink alter column slk_base_url nvarchar(255) NOT NULL
alter table acSignonLink alter column slk_usr_id nvarchar(20) NOT NULL
alter table acSignonLink alter column slk_window_target nvarchar(10) NOT NULL
alter table acSite alter column ste_ctl_4 nvarchar(100) NULL
alter table acSite alter column ste_eff_end_date nvarchar(100) NULL
alter table acSite alter column ste_eff_start_date nvarchar(100) NULL
alter table acSite alter column ste_id nvarchar(10) NULL
alter table acSite alter column ste_lan_xml nvarchar(255) NOT NULL
alter table acSite alter column ste_login_url nvarchar(255) NOT NULL
alter table acSite alter column ste_max_users nvarchar(100) NULL
alter table acSite alter column ste_qr_mod_types nvarchar(200) NULL
alter table acSite alter column ste_rsv_link nvarchar(1000) NULL
alter table acSite alter column ste_status nvarchar(10) NOT NULL
alter table acSite alter column ste_targeted_entity_lst nvarchar(50) NULL
alter table aeAccount alter column acn_create_usr_id nvarchar(20) NOT NULL
alter table aeAccount alter column acn_type nvarchar(20) NULL
alter table aeAccount alter column acn_upd_usr_id nvarchar(20) NOT NULL
alter table aeAccountTransaction alter column axn_create_usr_id nvarchar(20) NOT NULL
alter table aeAccountTransaction alter column axn_method nvarchar(20) NOT NULL
alter table aeAccountTransaction alter column axn_ref nvarchar(255) NOT NULL
alter table aeAccountTransaction alter column axn_status nvarchar(20) NOT NULL
alter table aeAccountTransaction alter column axn_type nvarchar(20) NOT NULL
alter table aeApplication alter column app_create_usr_id nvarchar(20) NOT NULL
alter table aeApplication alter column app_status nvarchar(20) NOT NULL
alter table aeApplication alter column app_upd_usr_id nvarchar(20) NOT NULL
alter table aeAppnActnHistory alter column aah_create_usr_id nvarchar(20) NOT NULL
alter table aeAppnActnHistory alter column aah_upd_usr_id nvarchar(20) NOT NULL
alter table aeAppnApprovalList alter column aal_action_taken nvarchar(50) NULL
alter table aeAppnApprovalList alter column aal_action_taker_approval_role nvarchar(50) NULL
alter table aeAppnApprovalList alter column aal_approval_role nvarchar(50) NOT NULL
alter table aeAppnApprovalList alter column aal_status nvarchar(50) NOT NULL
alter table aeAppnCommHistory alter column ach_create_usr_id nvarchar(20) NOT NULL
alter table aeAppnCommHistory alter column ach_upd_usr_id nvarchar(20) NOT NULL
alter table aeAppnEnrolRelation alter column aer_create_usr_id nvarchar(20) NOT NULL
alter table aeAppnEnrolRelation alter column aer_status nvarchar(50) NOT NULL
alter table aeAppnEnrolRelation alter column aer_upd_usr_id nvarchar(20) NOT NULL
alter table aeAppnOpenItem alter column aoi_ref nvarchar(50) NOT NULL
alter table aeAppnOpenItem alter column aoi_src nvarchar(20) NOT NULL
alter table aeAppnTargetEntity alter column ate_create_usr_id nvarchar(50) NOT NULL
alter table aeAttendance alter column att_create_usr_id nvarchar(20) NOT NULL
alter table aeAttendance alter column att_update_usr_id nvarchar(20) NULL
alter table aeAttendanceStatus alter column ats_cov_status nvarchar(1) NULL
alter table aeAttendanceStatus alter column ats_type nvarchar(10) NOT NULL
alter table aeCatalog alter column cat_create_usr_id nvarchar(20) NOT NULL
alter table aeCatalog alter column cat_status nvarchar(20) NOT NULL
alter table aeCatalog alter column cat_upd_usr_id nvarchar(20) NOT NULL
alter table aeCatalogAccess alter column cac_create_usr_id nvarchar(20) NOT NULL
alter table aeCatalogItemType alter column cit_create_usr_id nvarchar(20) NOT NULL
alter table aeCatalogItemType alter column cit_ity_id nvarchar(20) NULL
alter table aeFigure alter column fig_create_usr_id nvarchar(20) NOT NULL
alter table aeFigure alter column fig_update_usr_id nvarchar(20) NOT NULL
alter table aeFigureType alter column fgt_subtype nvarchar(50) NOT NULL
alter table aeFigureType alter column fgt_type nvarchar(50) NOT NULL
alter table aeItem alter column itm_app_approval_type nvarchar(50) NULL
alter table aeItem alter column itm_apply_method nvarchar(50) NULL
alter table aeItem alter column itm_approval_action nvarchar(20) NULL
alter table aeItem alter column itm_approval_status nvarchar(20) NULL
alter table aeItem alter column itm_approve_usr_id nvarchar(20) NULL
alter table aeItem alter column itm_create_usr_id nvarchar(20) NOT NULL
alter table aeItem alter column itm_life_status nvarchar(50) NULL
alter table aeItem alter column itm_status nvarchar(20) NOT NULL
alter table aeItem alter column itm_submit_action nvarchar(20) NULL
alter table aeItem alter column itm_submit_usr_id nvarchar(20) NULL
alter table aeItem alter column itm_type nvarchar(20) NOT NULL
alter table aeItem alter column itm_upd_usr_id nvarchar(20) NOT NULL
alter table aeItemAccess alter column iac_access_type nvarchar(30) NOT NULL
alter table aeItemActn alter column iat_type nvarchar(20) NOT NULL
alter table aeItemAttActn alter column iaa_to_att_status nvarchar(20) NOT NULL
alter table aeItemCompetency alter column itc_create_usr_id nvarchar(20) NOT NULL
alter table aeItemMessage alter column img_create_usr_id nvarchar(20) NOT NULL
alter table aeItemMessage alter column img_type nvarchar(20) NOT NULL
alter table aeItemMessage alter column img_update_usr_id nvarchar(20) NOT NULL
alter table aeItemMote alter column imt_create_usr_id nvarchar(50) NOT NULL
alter table aeItemMote alter column imt_status nvarchar(15) NOT NULL
alter table aeItemMote alter column imt_upd_usr_id nvarchar(50) NOT NULL
alter table aeItemMoteDefault alter column imd_create_usr_id nvarchar(50) NOT NULL
alter table aeItemMoteDefault alter column imd_upd_usr_id nvarchar(50) NOT NULL
alter table aeItemRating alter column irt_create_usr_id nvarchar(50) NOT NULL
alter table aeItemRating alter column irt_type nvarchar(50) NOT NULL
alter table aeItemRating alter column irt_update_usr_id nvarchar(50) NOT NULL
alter table aeItemRatingDefination alter column ird_update_usr_id nvarchar(50) NOT NULL
alter table aeItemRelation alter column ire_create_usr_id nvarchar(20) NOT NULL
alter table aeItemRequirement alter column itr_condition_type nvarchar(20) NOT NULL
alter table aeItemRequirement alter column itr_requirement_restriction nvarchar(20) NULL
alter table aeItemRequirement alter column itr_requirement_subtype nvarchar(20) NULL
alter table aeItemRequirement alter column itr_requirement_type nvarchar(20) NOT NULL
alter table aeItemResources alter column ire_type nvarchar(20) NULL
alter table aeItemTargetEntity alter column ite_create_usr_id nvarchar(20) NOT NULL
alter table aeItemTargetEntity alter column ite_type nvarchar(50) NULL
alter table aeItemTemplate alter column itp_create_usr_id nvarchar(20) NOT NULL
alter table aeItemTreeNodePath alter column inp_ancester nvarchar(255) NULL
alter table aeItemType alter column ity_cascade_inherit_col nvarchar(1024) NULL
alter table aeItemType alter column ity_create_usr_id nvarchar(20) NOT NULL
alter table aeItemType alter column ity_icon_url nvarchar(50) NULL
alter table aeItemType alter column ity_init_life_status nvarchar(50) NULL
alter table aeItemType alter column ity_tkh_method nvarchar(20) NULL
alter table aeItemTypeTemplate alter column itt_create_usr_id nvarchar(20) NOT NULL
alter table aeLearningSoln alter column lsn_create_usr_id nvarchar(20) NOT NULL
alter table aeLearningSoln alter column lsn_upd_usr_id nvarchar(20) NULL
alter table aeLearningSolnTemplate alter column snt_create_usr_id nvarchar(20) NOT NULL
alter table aeLearningSolnTemplate alter column snt_upd_usr_id nvarchar(20) NULL
alter table aeNotifyHistory alter column nhs_create_usr_id nvarchar(20) NOT NULL
alter table aeOpenItem alter column oim_create_usr_id nvarchar(50) NOT NULL
alter table aeOpenItem alter column oim_ref nvarchar(50) NOT NULL
alter table aeOpenItem alter column oim_src nvarchar(20) NOT NULL
alter table aeOpenItem alter column oim_status nvarchar(20) NOT NULL
alter table aeOpenItem alter column oim_type nvarchar(20) NOT NULL
alter table aeOpenItem alter column oim_upd_usr_id nvarchar(50) NOT NULL
alter table aeProgramDetails alter column pdt_create_usr_id nvarchar(20) NOT NULL
alter table aeProgramDetails alter column pdt_upd_usr_id nvarchar(20) NOT NULL
alter table aeProgramEvaluation alter column pgv_status nvarchar(10) NOT NULL
alter table aeTemplate alter column tpl_create_usr_id nvarchar(20) NOT NULL
alter table aeTemplate alter column tpl_upd_usr_id nvarchar(20) NOT NULL
alter table aeTemplateSup alter column tps_url nvarchar(50) NOT NULL
alter table aeTemplateType alter column ttp_title nvarchar(50) NOT NULL
alter table aeTemplateView alter column tvw_create_usr_id nvarchar(20) NOT NULL
alter table aeTreeNode alter column tnd_create_usr_id nvarchar(20) NOT NULL
alter table aeTreeNode alter column tnd_status nvarchar(20) NULL
alter table aeTreeNode alter column tnd_type nvarchar(20) NULL
alter table aeTreeNode alter column tnd_upd_usr_id nvarchar(20) NOT NULL
alter table aeTreeNodeSubscribe alter column tsb_create_usr_id nvarchar(20) NOT NULL
alter table AiccPath alter column acp_status nvarchar(10) NOT NULL
alter table AiccPath alter column acp_why_left nvarchar(10) NULL
alter table Assignment alter column ass_email nvarchar(50) NULL
alter table Attachment alter column att_type nvarchar(10) NOT NULL
alter table cfCertificate alter column ctf_create_usr_id nvarchar(20) NOT NULL
alter table cfCertificate alter column ctf_link nvarchar(100) NOT NULL
alter table cfCertificate alter column ctf_status nvarchar(50) NOT NULL
alter table cfCertificate alter column ctf_upd_usr_id nvarchar(20) NOT NULL
alter table cfCertification alter column cfn_create_usr_id nvarchar(20) NOT NULL
alter table cfCertification alter column cfn_status nvarchar(50) NOT NULL
alter table cfCertification alter column cfn_upd_usr_id nvarchar(20) NOT NULL
alter table cmAssessment alter column asm_create_usr_id nvarchar(20) NOT NULL
alter table cmAssessment alter column asm_status nvarchar(20) NULL
alter table cmAssessment alter column asm_type nvarchar(20) NOT NULL
alter table cmAssessment alter column asm_update_usr_id nvarchar(20) NOT NULL
alter table cmAssessmentNotify alter column asn_asu_type nvarchar(20) NOT NULL
alter table cmAssessmentNotify alter column asn_type nvarchar(20) NOT NULL
alter table cmSkill alter column skl_derive_rule nvarchar(20) NULL
alter table cmSkill alter column skl_type nvarchar(20) NOT NULL
alter table cmSkillBase alter column skb_ancestor nvarchar(255) NULL
alter table cmSkillBase alter column skb_create_usr_id nvarchar(20) NOT NULL
alter table cmSkillBase alter column skb_delete_usr_id nvarchar(20) NULL
alter table cmSkillBase alter column skb_type nvarchar(20) NOT NULL
alter table cmSkillBase alter column skb_update_usr_id nvarchar(20) NOT NULL
alter table cmSkillScale alter column ssl_create_usr_id nvarchar(20) NOT NULL
alter table cmSkillScale alter column ssl_delete_usr_id nvarchar(50) NULL
alter table cmSkillScale alter column ssl_update_usr_id nvarchar(20) NOT NULL
alter table cmSkillSet alter column sks_create_usr_id nvarchar(20) NOT NULL
alter table cmSkillSet alter column sks_type nvarchar(20) NOT NULL
alter table cmSkillSet alter column sks_update_usr_id nvarchar(20) NOT NULL
alter table CodeTable alter column ctb_create_usr_id nvarchar(20) NOT NULL
alter table CodeTable alter column ctb_upd_usr_id nvarchar(20) NOT NULL
alter table CodeType alter column ctp_create_usr_id nvarchar(20) NOT NULL
alter table CodeType alter column ctp_update_usr_id nvarchar(50) NOT NULL
alter table Course alter column cos_aicc_version nvarchar(50) NULL
alter table Course alter column cos_lic_key nvarchar(50) NULL
alter table CourseCriteria alter column ccr_create_usr_id nvarchar(50) NOT NULL
alter table CourseCriteria alter column ccr_type nvarchar(50) NULL
alter table CourseCriteria alter column ccr_upd_method nvarchar(20) NOT NULL
alter table CourseCriteria alter column ccr_upd_usr_id nvarchar(50) NOT NULL
alter table CourseEvaluation alter column cov_status nvarchar(10) NULL
alter table CourseModuleCriteria alter column cmr_create_usr_id nvarchar(50) NOT NULL
alter table CourseModuleCriteria alter column cmr_status nvarchar(10) NULL
alter table CourseModuleCriteria alter column cmr_upd_usr_id nvarchar(50) NOT NULL
alter table CpeHour alter column cpe_create_usr_id nvarchar(20) NOT NULL
alter table CpeHour alter column cpe_update_usr_id nvarchar(20) NULL
alter table ctGlossary alter column glo_create_usr_id nvarchar(20) NOT NULL
alter table ctGlossary alter column glo_update_usr_id nvarchar(20) NOT NULL
alter table ctLicence alter column lic_desc nvarchar(255) NULL
alter table ctReference alter column ref_create_usr_id nvarchar(20) NOT NULL
alter table ctReference alter column ref_type nvarchar(10) NULL
alter table ctReference alter column ref_update_usr_id nvarchar(20) NOT NULL
alter table Enrolment alter column enr_create_usr_id nvarchar(50) NOT NULL
alter table Enrolment alter column enr_status nvarchar(10) NOT NULL
alter table Entity alter column ent_delete_usr_id nvarchar(20) NULL
alter table Entity alter column ent_type nvarchar(10) NOT NULL
alter table fmFacility alter column fac_create_usr_id nvarchar(20) NOT NULL
alter table fmFacility alter column fac_status nvarchar(50) NOT NULL
alter table fmFacility alter column fac_upd_usr_id nvarchar(20) NOT NULL
alter table fmFacility alter column fac_url_type nvarchar(50) NULL
alter table fmFacilitySchedule alter column fsh_cancel_usr_id nvarchar(20) NULL
alter table fmFacilitySchedule alter column fsh_create_usr_id nvarchar(20) NOT NULL
alter table fmFacilitySchedule alter column fsh_upd_usr_id nvarchar(20) NOT NULL
alter table fmFacilityType alter column ftp_class_name nvarchar(50) NULL
alter table fmFacilityType alter column ftp_xsl_prefix nvarchar(50) NULL
alter table fmLocation alter column loc_create_usr_id nvarchar(20) NOT NULL
alter table fmLocation alter column loc_upd_usr_id nvarchar(20) NOT NULL
alter table fmReservation alter column rsv_cancel_usr_id nvarchar(20) NULL
alter table fmReservation alter column rsv_create_usr_id nvarchar(20) NOT NULL
alter table fmReservation alter column rsv_status nvarchar(50) NOT NULL
alter table fmReservation alter column rsv_upd_usr_id nvarchar(20) NOT NULL
alter table fmTimeSlot alter column tsl_end_time nvarchar(50) NOT NULL
alter table fmTimeSlot alter column tsl_start_time nvarchar(50) NOT NULL
alter table ForumMessage alter column fmg_image nvarchar(50) NULL
alter table ForumMessage alter column fmg_usr_id nvarchar(20) NOT NULL
alter table ForumTopic alter column fto_usr_id nvarchar(20) NOT NULL
alter table GroupMember alter column gpm_ancester nvarchar(255) NULL
alter table GroupMember alter column gpm_create_usr_id nvarchar(20) NOT NULL
alter table IMSLog alter column ilg_create_usr_id nvarchar(20) NOT NULL
alter table IMSLog alter column ilg_method nvarchar(20) NOT NULL
alter table IMSLog alter column ilg_process nvarchar(20) NOT NULL
alter table IMSLog alter column ilg_type nvarchar(20) NOT NULL
alter table IndustryCode alter column idc_type nvarchar(50) NULL
alter table InteractionStatistics alter column ist_population_type nvarchar(10) NOT NULL
alter table InteractionStatistics alter column ist_type nvarchar(10) NOT NULL
alter table KCRCclassCode alter column kcc_class_code nvarchar(10) NOT NULL
alter table kmBaseObject alter column bob_delete_usr_id nvarchar(20) NULL
alter table kmBaseObject alter column bob_nature nvarchar(50) NULL
alter table kmFolder alter column fld_nature nvarchar(50) NULL
alter table kmFolder alter column fld_type nvarchar(50) NULL
alter table kmFolder alter column fld_update_usr_id nvarchar(20) NOT NULL
alter table kmLibraryObjectBorrow alter column lob_create_usr_id nvarchar(20) NOT NULL
alter table kmLibraryObjectBorrow alter column lob_status nvarchar(20) NOT NULL
alter table kmLibraryObjectBorrow alter column lob_update_usr_id nvarchar(20) NOT NULL
alter table kmLibraryObjectCopy alter column loc_create_usr_id nvarchar(20) NOT NULL
alter table kmLibraryObjectCopy alter column loc_delete_usr_id nvarchar(20) NULL
alter table kmLibraryObjectCopy alter column loc_update_usr_id nvarchar(20) NOT NULL
alter table kmLink alter column lnk_type nvarchar(20) NOT NULL
alter table kmNode alter column nod_ancestor nvarchar(500) NULL
alter table kmNode alter column nod_create_usr_id nvarchar(20) NOT NULL
alter table kmNode alter column nod_type nvarchar(20) NOT NULL
alter table kmNodeActnHistory alter column nah_type nvarchar(20) NOT NULL
alter table kmNodeAssignment alter column nam_create_usr_id nvarchar(20) NOT NULL
alter table kmNodeSubscription alter column nsb_email_send_type nvarchar(20) NOT NULL
alter table kmNodeSubscription alter column nsb_type nvarchar(20) NOT NULL
alter table kmObject alter column obj_status nvarchar(50) NOT NULL
alter table kmObject alter column obj_type nvarchar(20) NOT NULL
alter table kmObject alter column obj_update_usr_id nvarchar(20) NOT NULL
alter table kmObjectAttachment alter column oat_obj_version nvarchar(50) NOT NULL
alter table kmObjectHistory alter column ojh_status nvarchar(50) NOT NULL
alter table kmObjectHistory alter column ojh_type nvarchar(20) NOT NULL
alter table kmObjectHistory alter column ojh_update_usr_id nvarchar(20) NOT NULL
alter table kmObjectType alter column oty_create_usr_id nvarchar(20) NOT NULL
alter table kmObjectType alter column oty_nature nvarchar(20) NULL
alter table Message alter column msg_level nvarchar(50) NULL
alter table mgitmSelectedMessage alter column ism_type nvarchar(100) NULL
alter table mgMessage alter column msg_create_usr_id nvarchar(20) NOT NULL
alter table mgMessage alter column msg_send_usr_id nvarchar(20) NOT NULL
alter table mgMessage alter column msg_update_usr_id nvarchar(20) NULL
alter table mgRecHistory alter column mgh_status nvarchar(20) NULL
alter table mgRecipient alter column rec_type nvarchar(20) NOT NULL
alter table mgxslParamValue alter column xpv_type nvarchar(20) NOT NULL
alter table mgxslParamValue alter column xpv_value nvarchar(255) NULL
alter table Module alter column mod_aicc_version nvarchar(50) NULL
alter table Module alter column mod_logic nvarchar(10) NULL
alter table Module alter column mod_password nvarchar(255) NULL
alter table Module alter column mod_time_limit_action nvarchar(255) NULL
alter table Module alter column mod_type nvarchar(10) NOT NULL
alter table ModuleEvaluation alter column mov_aicc_score nvarchar(255) NULL
alter table ModuleEvaluation alter column mov_create_usr_id nvarchar(20) NULL
alter table ModuleEvaluation alter column mov_status nvarchar(10) NOT NULL
alter table ModuleEvaluation alter column mov_status_flag nvarchar(10) NULL
alter table ModuleEvaluation alter column mov_update_usr_id nvarchar(20) NULL
alter table ModuleEvaluationHistory alter column mvh_create_usr_id nvarchar(20) NOT NULL
alter table ModuleEvaluationHistory alter column mvh_status nvarchar(10) NULL
alter table ModuleSpec alter column msp_privilege nvarchar(20) NULL
alter table ModuleSpec alter column msp_type nvarchar(20) NULL
alter table Objective alter column obj_developer_id nvarchar(255) NULL
alter table Objective alter column obj_type nvarchar(10) NOT NULL
alter table ObjectView alter column ojv_create_usr_id nvarchar(20) NOT NULL
alter table ObjectView alter column ojv_update_usr_id nvarchar(50) NOT NULL
alter table Progress alter column pgr_completion_status nvarchar(10) NULL
alter table Progress alter column pgr_grade nvarchar(10) NULL
alter table Progress alter column pgr_status nvarchar(10) NOT NULL
alter table psnBiography alter column pbg_create_usr_id nvarchar(20) NOT NULL
alter table psnBiography alter column pbg_option nvarchar(400) NULL
alter table psnBiography alter column pbg_update_usr_id nvarchar(20) NOT NULL
alter table psnPreference alter column pfr_create_usr_id nvarchar(20) NOT NULL
alter table psnPreference alter column pfr_lang nvarchar(10) NULL
alter table psnPreference alter column pfr_skin_id nvarchar(20) NULL
alter table psnPreference alter column pfr_update_usr_id nvarchar(20) NOT NULL
alter table Question alter column que_prog_lang nvarchar(30) NULL
alter table Question alter column que_type nvarchar(10) NULL
alter table RawQuestion alter column raq_lan nvarchar(50) NOT NULL
alter table RawQuestion alter column raq_privilege nvarchar(10) NOT NULL
alter table RawQuestion alter column raq_status nvarchar(50) NOT NULL
alter table RawQuestion alter column raq_type nvarchar(50) NOT NULL
alter table RawQuestion alter column raq_usr_id_owner nvarchar(20) NOT NULL
alter table RegUser alter column usr_approve_usr_id nvarchar(50) NULL
alter table RegUser alter column usr_cost_center nvarchar(50) NULL
alter table RegUser alter column usr_email nvarchar(255) NULL
alter table RegUser alter column usr_email_2 nvarchar(50) NULL
alter table RegUser alter column usr_fax_1 nvarchar(50) NULL
alter table RegUser alter column usr_hkid nvarchar(50) NULL
alter table RegUser alter column usr_not_syn_gpm_type nvarchar(50) NULL
alter table RegUser alter column usr_other_id_no nvarchar(50) NULL
alter table RegUser alter column usr_other_id_type nvarchar(10) NULL
alter table RegUser alter column usr_pwd nvarchar(30) NULL
alter table RegUser alter column usr_status nvarchar(20) NOT NULL
alter table RegUser alter column usr_tel_1 nvarchar(50) NULL
alter table RegUser alter column usr_tel_2 nvarchar(50) NULL
alter table ReportSpec alter column rsp_create_usr_id nvarchar(20) NOT NULL
alter table ReportSpec alter column rsp_upd_usr_id nvarchar(20) NULL
alter table ReportTemplate alter column rte_create_usr_id nvarchar(20) NOT NULL
alter table ReportTemplate alter column rte_dl_xsl nvarchar(256) NULL
alter table ReportTemplate alter column rte_exe_xsl nvarchar(256) NULL
alter table ReportTemplate alter column rte_get_xsl nvarchar(256) NULL
alter table ReportTemplate alter column rte_meta_data_url nvarchar(1024) NULL
alter table ReportTemplate alter column rte_type nvarchar(50) NOT NULL
alter table ReportTemplate alter column rte_upd_usr_id nvarchar(20) NULL
alter table Resources alter column res_code nvarchar(30) NULL
alter table Resources alter column res_format nvarchar(10) NULL
alter table Resources alter column res_lan nvarchar(50) NULL
alter table Resources alter column res_privilege nvarchar(10) NULL
alter table Resources alter column res_src_type nvarchar(50) NULL
alter table Resources alter column res_status nvarchar(10) NULL
alter table Resources alter column res_subtype nvarchar(50) NULL
alter table Resources alter column res_tpl_name nvarchar(50) NULL
alter table Resources alter column res_type nvarchar(10) NOT NULL
alter table Resources alter column res_upd_user nvarchar(20) NULL
alter table Resources alter column res_usr_id_owner nvarchar(20) NULL
alter table Results alter column ent_type nvarchar(10) NOT NULL
alter table SuperviseTargetEntity alter column spt_create_usr_id nvarchar(20) NOT NULL
alter table Syllabus alter column syl_locale nvarchar(10) NULL
alter table Syllabus alter column syl_privilege nvarchar(30) NULL
alter table Template alter column tpl_stylesheet nvarchar(50) NULL
alter table Template alter column tpl_thumbnail_url nvarchar(100) NULL
alter table Template alter column tpl_type nvarchar(10) NOT NULL
alter table TrackingHistory alter column tkh_type nvarchar(20) NOT NULL
alter table UploadLog alter column ulg_method nvarchar(20) NOT NULL
alter table UploadLog alter column ulg_process nvarchar(20) NOT NULL
alter table UploadLog alter column ulg_status nvarchar(50) NOT NULL
alter table UploadLog alter column ulg_subtype nvarchar(50) NULL
alter table UploadLog alter column ulg_type nvarchar(50) NOT NULL
alter table UploadLog alter column ulg_usr_id_owner nvarchar(50) NOT NULL
alter table UserClassification alter column ucf_type nvarchar(50) NULL
alter table UserGrade alter column ugr_type nvarchar(50) NULL
alter table UserGroup alter column usg_code nvarchar(50) NULL
alter table UserGroup alter column usg_level nvarchar(10) NULL
alter table UserGroup alter column usg_role nvarchar(10) NULL
alter table UserGroup alter column usg_status nvarchar(10) NULL
alter table UserGroup alter column usg_subg_code nvarchar(50) NULL
alter table UserGroup alter column usg_usr_id_admin nvarchar(20) NULL
alter table usrRoleTargetEntity alter column rte_create_usr_id nvarchar(50) NOT NULL
alter table wbTransactionLog alter column tlg_create_usr_id nvarchar(50) NOT NULL
alter table wbTransactionLog alter column tlg_ref_id_1 nvarchar(50) NULL
alter table wbTransactionLog alter column tlg_ref_id_2 nvarchar(50) NULL
alter table wbTransactionLog alter column tlg_ref_id_3 nvarchar(50) NULL
alter table wbTransactionLog alter column tlg_type nvarchar(50) NOT NULL
alter table xslmgSelectedTemplate alter column mst_type nvarchar(50) NOT NULL
alter table xslParamName alter column xpn_name nvarchar(20) NOT NULL
alter table xslTemplate alter column xtp_channel_api nvarchar(100) NULL
alter table xslTemplate alter column xtp_channel_type nvarchar(20) NULL
alter table xslTemplate alter column xtp_subtype nvarchar(20) NOT NULL
alter table xslTemplate alter column xtp_title nvarchar(255) NULL
alter table xslTemplate alter column xtp_type nvarchar(100) NOT NULL
alter table xslTemplate alter column xtp_xml_url nvarchar(100) NULL
alter table xslTemplate alter column xtp_xsl nvarchar(50) NULL

-- for those columns which are referred by other objects, remove the reference first
alter table UserClassificationType drop CONSTRAINT PK_UserClassificationType
alter table Template drop CONSTRAINT PK_Template
alter table SystemMessage drop CONSTRAINT PK_SystemMessage
alter table SuperviseTargetEntity drop CONSTRAINT PK_SuperviseTargetEntity
alter table Progress drop CONSTRAINT FK_Progress_RegUser
alter table ProgressAttempt drop CONSTRAINT FK_ProgressAttempt_Progress
alter table ProgressAttachment drop CONSTRAINT FK_ProgressAttachment_Progress
alter table ProgressAttempt drop CONSTRAINT PK_ProgressAttempt
alter table ProgressAttachment drop CONSTRAINT PK_ProgressAttachment
alter table Progress drop CONSTRAINT PK_Progress
alter table ObjectView drop CONSTRAINT PK_ObjectView
drop INDEX Objective.IX_Objective_ancester
alter table NotificationHistory drop CONSTRAINT PK_TNotificationHistory
alter table Module drop CONSTRAINT FK_Module_RegUser
drop INDEX Message.I_Message
alter table Message drop CONSTRAINT FK_Message_RegUser
alter table RegUser drop CONSTRAINT PK_User
alter table kmObjectTypeTemplate drop CONSTRAINT FK_kmObjectTypeTemplate_kmObjectType
alter table kmObjectType drop CONSTRAINT PK_kmObjectType
alter table kmObjectHistory drop CONSTRAINT PK_kmObjectHistory
alter table kmObjectTemplate drop CONSTRAINT FK_kmObjectTemplate_kmObject
alter table kmObject drop CONSTRAINT PK_kmObject
alter table kmNodeAssignment drop CONSTRAINT PK_kmNodeAssignment
alter table kmNodeAccess drop CONSTRAINT PK_kmNodeAccess
drop INDEX GroupMember.IX_GroupMember
alter table GroupMember drop CONSTRAINT PK_groupmember
alter table ForumMarkMsg drop CONSTRAINT PK_ForumRead
alter table fmTimeSlot drop CONSTRAINT PK_fmTimeSlot
drop INDEX fmFacilitySchedule.IX_fmFacilitySchedule_fsh_status_start_end
alter table DisplayOption drop CONSTRAINT PK_DisplayOption
alter table ctLicence drop CONSTRAINT PK_ctLicence
alter table cmAssessmentUnit drop CONSTRAINT PK_cmAssessmentUnit
alter table cmAsmUnitTypeAttr drop CONSTRAINT PK_cmAsmUnitTypeAttr
alter table aeTemplateView drop CONSTRAINT PK_aeTemplateView
alter table aeLearningSoln drop CONSTRAINT PK_aeLearningSoln
alter table aeItemTypeTemplate drop CONSTRAINT PK_aeItemTypeTemplate
alter table aeItemType drop CONSTRAINT PK_aeItemType
alter table aeItemAccess drop CONSTRAINT PK_aeItemAccess
alter table acItemPageVariant drop CONSTRAINT PK_acItemPageVariant

-- after removing the references, alter the column data type
alter table acItemPageVariant alter column ipv_itm_approval_status nvarchar(20) NOT NULL
alter table acItemPageVariant alter column ipv_name nvarchar(40) NOT NULL
alter table aeItemAccess alter column iac_access_id nvarchar(30) NOT NULL
alter table aeItemType alter column ity_id nvarchar(20) NOT NULL
alter table aeItemTypeTemplate alter column itt_ity_id nvarchar(20) NOT NULL
alter table aeLearningSoln alter column lsn_ent_id_lst nvarchar(255) NOT NULL
alter table aeLearningSoln alter column lsn_type nvarchar(20) NOT NULL
alter table aeTemplateView alter column tvw_id nvarchar(20) NOT NULL
alter table cmAsmUnitTypeAttr alter column aua_asu_type nvarchar(20) NOT NULL
alter table cmAssessmentUnit alter column asu_type nvarchar(20) NOT NULL
alter table ctLicence alter column lic_key nvarchar(50) NOT NULL
alter table DisplayOption alter column dpo_res_subtype nvarchar(10) NOT NULL
alter table DisplayOption alter column dpo_res_type nvarchar(10) NOT NULL
alter table DisplayOption alter column dpo_view nvarchar(10) NOT NULL
alter table fmFacilitySchedule alter column fsh_status nvarchar(50) NOT NULL
alter table fmTimeSlot alter column tsl_id nvarchar(10) NOT NULL
alter table ForumMarkMsg alter column fmm_usr_id nvarchar(20) NOT NULL
alter table GroupMember alter column gpm_type nvarchar(50) NOT NULL
alter table kmNodeAccess alter column nac_access_type nvarchar(20) NOT NULL
alter table kmNodeAssignment alter column nam_type nvarchar(20) NOT NULL
alter table kmObject alter column obj_version nvarchar(50) NOT NULL
alter table kmObjectHistory alter column ojh_version nvarchar(50) NOT NULL
alter table kmObjectTemplate alter column ojt_obj_version nvarchar(50) NOT NULL
alter table kmObjectType alter column oty_code nvarchar(20) NOT NULL
alter table kmObjectTypeTemplate alter column ott_oty_code nvarchar(20) NOT NULL
alter table Message alter column msg_status nvarchar(15) NOT NULL
alter table Message alter column msg_type nvarchar(5) NOT NULL
alter table Message alter column msg_usr_id nvarchar(20) NOT NULL
alter table Module alter column mod_usr_id_instructor nvarchar(20) NULL
alter table NotificationHistory alter column nfh_target_type nvarchar(50) NOT NULL
alter table NotificationHistory alter column nfh_type nvarchar(100) NOT NULL
alter table Objective alter column obj_ancester nvarchar(255) NULL
alter table ObjectView alter column ojv_subtype nvarchar(50) NOT NULL
alter table ObjectView alter column ojv_type nvarchar(50) NOT NULL
alter table Progress alter column pgr_usr_id nvarchar(20) NOT NULL
alter table ProgressAttachment alter column pat_prg_usr_id nvarchar(20) NOT NULL
alter table ProgressAttempt alter column atm_pgr_usr_id nvarchar(20) NOT NULL
alter table RegUser alter column usr_id nvarchar(20) NOT NULL
alter table SuperviseTargetEntity alter column spt_type nvarchar(20) NOT NULL
alter table SystemMessage alter column sms_lan nvarchar(50) NOT NULL
alter table Template alter column tpl_lan nvarchar(50) NOT NULL
alter table Template alter column tpl_name nvarchar(50) NOT NULL
alter table UserClassificationType alter column uct_type nvarchar(50) NOT NULL

-- after that, recreate the references removed previously
ALTER TABLE [dbo].[acItemPageVariant] WITH NOCHECK ADD
	CONSTRAINT [PK_acItemPageVariant] PRIMARY KEY  CLUSTERED
	(
		[ipv_name],
		[ipv_itm_approval_status],
		[ipv_run_ind],
		[ipv_mgt_assistant_ind]
	)  ON [PRIMARY]
GO

ALTER TABLE [dbo].[aeItemAccess] ADD
	CONSTRAINT [PK_aeItemAccess] PRIMARY KEY  NONCLUSTERED
	(
		[iac_itm_id],
		[iac_ent_id],
		[iac_access_id]
	) WITH  FILLFACTOR = 90  ON [PRIMARY]
GO

ALTER TABLE [dbo].[aeItemType] ADD
	CONSTRAINT [PK_aeItemType] PRIMARY KEY  NONCLUSTERED
	(
		[ity_owner_ent_id],
		[ity_id],
		[ity_run_ind],
		[ity_session_ind]
	) WITH  FILLFACTOR = 90  ON [PRIMARY]
GO

ALTER TABLE [dbo].[aeItemTypeTemplate] ADD
	CONSTRAINT [PK_aeItemTypeTemplate] PRIMARY KEY  NONCLUSTERED
	(
		[itt_ity_owner_ent_id],
		[itt_ity_id],
		[itt_ttp_id],
		[itt_seq_id]
	) WITH  FILLFACTOR = 90  ON [PRIMARY]
GO

ALTER TABLE [dbo].[aeLearningSoln] ADD
	CONSTRAINT [PK_aeLearningSoln] PRIMARY KEY  NONCLUSTERED
	(
		[lsn_ent_id],
		[lsn_itm_id],
		[lsn_ent_id_lst],
		[lsn_create_timestamp],
		[lsn_type]
	) WITH  FILLFACTOR = 90  ON [PRIMARY]
GO

ALTER TABLE [dbo].[aeTemplateView] ADD
	CONSTRAINT [PK_aeTemplateView] PRIMARY KEY  NONCLUSTERED
	(
		[tvw_tpl_id],
		[tvw_id]
	) WITH  FILLFACTOR = 90  ON [PRIMARY]
GO

ALTER TABLE [dbo].[cmAsmUnitTypeAttr] WITH NOCHECK ADD
	CONSTRAINT [PK_cmAsmUnitTypeAttr] PRIMARY KEY  CLUSTERED
	(
		[aua_asm_id],
		[aua_asu_type]
	)  ON [PRIMARY]
GO

ALTER TABLE [dbo].[cmAssessmentUnit] ADD
	CONSTRAINT [PK_cmAssessmentUnit] PRIMARY KEY  NONCLUSTERED
	(
		[asu_asm_id],
		[asu_ent_id],
		[asu_type]
	) WITH  FILLFACTOR = 90  ON [PRIMARY]
GO

ALTER TABLE [dbo].[ctLicence] ADD
	CONSTRAINT [PK_ctLicence] PRIMARY KEY  NONCLUSTERED
	(
		[lic_key],
		[lic_ent_id_root]
	) WITH  FILLFACTOR = 90  ON [PRIMARY]
GO

ALTER TABLE [dbo].[DisplayOption] ADD
	CONSTRAINT [PK_DisplayOption] PRIMARY KEY  NONCLUSTERED
	(
		[dpo_res_id],
		[dpo_res_type],
		[dpo_res_subtype],
		[dpo_view]
	) WITH  FILLFACTOR = 90  ON [PRIMARY]
GO

 CREATE  INDEX [IX_fmFacilitySchedule_fsh_status_start_end] ON [dbo].[fmFacilitySchedule]([fsh_start_time], [fsh_end_time], [fsh_status]) ON [PRIMARY]
GO

ALTER TABLE [dbo].[fmTimeSlot] ADD
	CONSTRAINT [PK_fmTimeSlot] PRIMARY KEY  NONCLUSTERED
	(
		[tsl_id],
		[tsl_owner_ent_id]
	) WITH  FILLFACTOR = 90  ON [PRIMARY]
GO

ALTER TABLE [dbo].[ForumMarkMsg] ADD
	CONSTRAINT [PK_ForumRead] PRIMARY KEY  NONCLUSTERED
	(
		[fmm_fmg_id],
		[fmm_fmg_fto_id],
		[fmm_fmg_fto_res_id],
		[fmm_usr_id]
	) WITH  FILLFACTOR = 90  ON [PRIMARY]
GO

ALTER TABLE [dbo].[GroupMember] ADD
	CONSTRAINT [PK_groupmember] PRIMARY KEY  NONCLUSTERED
	(
		[gpm_ent_id_group],
		[gpm_ent_id_member],
		[gpm_start_timestamp],
		[gpm_end_timestamp],
		[gpm_type]
	) WITH  FILLFACTOR = 90  ON [PRIMARY]
GO

 CREATE  INDEX [IX_GroupMember] ON [dbo].[GroupMember]([gpm_ent_id_member], [gpm_type]) ON [PRIMARY]
GO

ALTER TABLE [dbo].[kmNodeAccess] ADD
	CONSTRAINT [PK_kmNodeAccess] PRIMARY KEY  NONCLUSTERED
	(
		[nac_nod_id],
		[nac_access_type],
		[nac_ent_id]
	)  ON [PRIMARY]
GO

ALTER TABLE [dbo].[kmNodeAssignment] ADD
	CONSTRAINT [PK_kmNodeAssignment] PRIMARY KEY  NONCLUSTERED
	(
		[nam_nod_id],
		[nam_ent_id],
		[nam_type]
	)  ON [PRIMARY]
GO

ALTER TABLE [dbo].[kmObject] ADD
	CONSTRAINT [PK_kmObject] PRIMARY KEY  NONCLUSTERED
	(
		[obj_bob_nod_id],
		[obj_version]
	)  ON [PRIMARY]
GO

ALTER TABLE [dbo].[kmObjectTemplate] ADD
	CONSTRAINT [FK_kmObjectTemplate_kmObject] FOREIGN KEY
	(
		[ojt_obj_bob_nod_id],
		[ojt_obj_version]
	) REFERENCES [dbo].[kmObject] (
		[obj_bob_nod_id],
		[obj_version]
	)
GO

ALTER TABLE [dbo].[kmObjectHistory] ADD
	CONSTRAINT [PK_kmObjectHistory] PRIMARY KEY  NONCLUSTERED
	(
		[ojh_obj_bob_nod_id],
		[ojh_version]
	)  ON [PRIMARY]
GO

ALTER TABLE [dbo].[kmObjectType] WITH NOCHECK ADD
	CONSTRAINT [PK_kmObjectType] PRIMARY KEY  CLUSTERED
	(
		[oty_owner_ent_id],
		[oty_code]
	)  ON [PRIMARY]
GO

ALTER TABLE [dbo].[kmObjectTypeTemplate] ADD
	CONSTRAINT [FK_kmObjectTypeTemplate_kmObjectType] FOREIGN KEY
	(
		[ott_oty_owner_ent_id],
		[ott_oty_code]
	) REFERENCES [dbo].[kmObjectType] (
		[oty_owner_ent_id],
		[oty_code]
	)
GO

ALTER TABLE [dbo].[RegUser] ADD
	CONSTRAINT [PK_User] PRIMARY KEY  NONCLUSTERED
	(
		[usr_id]
	) WITH  FILLFACTOR = 90  ON [PRIMARY]
GO

ALTER TABLE [dbo].[Message] ADD
	CONSTRAINT [FK_Message_RegUser] FOREIGN KEY
	(
		[msg_usr_id]
	) REFERENCES [dbo].[RegUser] (
		[usr_id]
	)
GO

 CREATE  INDEX [I_Message] ON [dbo].[Message]([msg_type], [msg_status]) ON [PRIMARY]
GO

ALTER TABLE [dbo].[Module] ADD
	CONSTRAINT [FK_Module_RegUser] FOREIGN KEY
	(
		[mod_usr_id_instructor]
	) REFERENCES [dbo].[RegUser] (
		[usr_id]
	)
GO

ALTER TABLE [dbo].[NotificationHistory] WITH NOCHECK ADD
	CONSTRAINT [PK_TNotificationHistory] PRIMARY KEY  CLUSTERED
	(
		[nfh_target_id],
		[nfh_target_type],
		[nfh_type]
	)  ON [PRIMARY]
GO

 CREATE  INDEX [IX_Objective_ancester] ON [dbo].[Objective]([obj_ancester]) WITH  FILLFACTOR = 90 ON [PRIMARY]
GO

ALTER TABLE [dbo].[ObjectView] WITH NOCHECK ADD
	CONSTRAINT [PK_ObjectView] PRIMARY KEY  CLUSTERED
	(
		[ojv_owner_ent_id],
		[ojv_type],
		[ojv_subtype]
	)  ON [PRIMARY]
GO

ALTER TABLE [dbo].[Progress] WITH NOCHECK ADD
	CONSTRAINT [PK_Progress] PRIMARY KEY  CLUSTERED
	(
		[pgr_usr_id],
		[pgr_res_id],
		[pgr_attempt_nbr],
		[pgr_tkh_id]
	)  ON [PRIMARY]
GO

ALTER TABLE [dbo].[ProgressAttachment] WITH NOCHECK ADD
	CONSTRAINT [PK_ProgressAttachment] PRIMARY KEY  CLUSTERED
	(
		[pat_prg_usr_id],
		[pat_prg_res_id],
		[pat_prg_attempt_nbr],
		[pat_att_id],
		[pat_tkh_id]
	)  ON [PRIMARY]
GO

ALTER TABLE [dbo].[ProgressAttempt] WITH NOCHECK ADD
	CONSTRAINT [PK_ProgressAttempt] PRIMARY KEY  CLUSTERED
	(
		[atm_pgr_usr_id],
		[atm_pgr_res_id],
		[atm_pgr_attempt_nbr],
		[atm_int_res_id],
		[atm_int_order],
		[atm_tkh_id]
	)  ON [PRIMARY]
GO

ALTER TABLE [dbo].[ProgressAttachment] ADD
	CONSTRAINT [FK_ProgressAttachment_Progress] FOREIGN KEY
	(
		[pat_prg_usr_id],
		[pat_prg_res_id],
		[pat_prg_attempt_nbr],
		[pat_tkh_id]
	) REFERENCES [dbo].[Progress] (
		[pgr_usr_id],
		[pgr_res_id],
		[pgr_attempt_nbr],
		[pgr_tkh_id]
	)
GO

ALTER TABLE [dbo].[ProgressAttempt] ADD
	CONSTRAINT [FK_ProgressAttempt_Progress] FOREIGN KEY
	(
		[atm_pgr_usr_id],
		[atm_pgr_res_id],
		[atm_pgr_attempt_nbr],
		[atm_tkh_id]
	) REFERENCES [dbo].[Progress] (
		[pgr_usr_id],
		[pgr_res_id],
		[pgr_attempt_nbr],
		[pgr_tkh_id]
	)
GO

ALTER TABLE [dbo].[Progress] ADD
	CONSTRAINT [FK_Progress_RegUser] FOREIGN KEY
	(
		[pgr_usr_id]
	) REFERENCES [dbo].[RegUser] (
		[usr_id]
	)
GO

ALTER TABLE [dbo].[SuperviseTargetEntity] WITH NOCHECK ADD
	CONSTRAINT [PK_SuperviseTargetEntity] PRIMARY KEY  CLUSTERED
	(
		[spt_source_usr_ent_id],
		[spt_type],
		[spt_target_ent_id]
	)  ON [PRIMARY]
GO

ALTER TABLE [dbo].[SystemMessage] WITH NOCHECK ADD
	CONSTRAINT [PK_SystemMessage] PRIMARY KEY  CLUSTERED
	(
		[sms_id],
		[sms_lan]
	)  ON [PRIMARY]
GO

ALTER TABLE [dbo].[Template] ADD
	CONSTRAINT [PK_Template] PRIMARY KEY  NONCLUSTERED
	(
		[tpl_name],
		[tpl_lan]
	) WITH  FILLFACTOR = 90  ON [PRIMARY]
GO

ALTER TABLE [dbo].[UserClassificationType] ADD
	CONSTRAINT [PK_UserClassificationType] PRIMARY KEY  NONCLUSTERED
	(
		[uct_type]
	) WITH  FILLFACTOR = 90  ON [PRIMARY]
GO

/**
Author: Lun
Desc: Allow leaner submit file indicator of essay type question
Date: 2004-03-25
*/
Alter Table Question Add que_submit_file_ind int

/**
Author: Fai
Desc: Add new system message for progress
Date: 3/30/2004
*/
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('PGR010', 'ISO-8859-1', null)
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('PGR010', 'Big5', null)
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('PGR010', 'GB2312', null)

insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('PGR011', 'ISO-8859-1', null)
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('PGR011', 'Big5', null)
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('PGR011', 'GB2312', null)

call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_pgr010_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'PGR010' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_pgr010_gb2312.txt	SystemMessage sms_desc "sms_id = 'PGR010' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_pgr010_big5.txt	SystemMessage sms_desc "sms_id = 'PGR010' and sms_lan = 'Big5'"

call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_pgr011_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'PGR011' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_pgr011_gb2312.txt	SystemMessage sms_desc "sms_id = 'PGR011' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_pgr011_big5.txt	SystemMessage sms_desc "sms_id = 'PGR011' and sms_lan = 'Big5'"

/*
Author: Kim
Desc: lengthen the field: filename for ims upload
Date: 2004/04/06
*/
alter table imslog  alter column ilg_filename nvarchar (100) null


/*
Auther: Lun
Desc: New column for question upload
Date: 2004/04/13
*/
Alter Table RawQuestion Add raq_desc ntext
Alter Table RawQuestion Add raq_shuffle int

/*
Auther: Kelvin
Desc: New column for question upload
Date: 2004/04/15
*/
Alter Table RawQuestion Add raq_submit_file_ind int

/*
Author: Kim
Desc: lengthen the field: filename for question upload
Date: 2004/04/16
*/
alter table UploadLog alter column ulg_file_name nvarchar (100) not null

/*
Auth: kawai
Date: 2004-04-20
Desc: take away magician template from Standard Test and Dynamic Test module
      (because that template does not support all types of question)
*/
update Resources
set res_tpl_name = 'Test Player 1'
where res_type = 'MOD' and res_subtype = 'TST' and res_tpl_name = 'Test Player 5'
update Resources
set res_tpl_name = 'Dynamic Player 1'
where res_type = 'MOD' and res_subtype = 'DXT' and res_tpl_name = 'Dynamic Player 3'

update Template
set tpl_type = 'TST_EX'
where tpl_name = 'Test Player 5'
update Template
set tpl_type = 'DXT_EX'
where tpl_name = 'Dynamic Player 3'

/*
Auth: kawai
Date: 2004-04-20
Desc: grant Learning Resource Management to System Administrator
      (because question import function is given to system admin)
*/
insert into acRoleFunction
select rol_id, ftn_id, '2004-04-20' from acRole, acFunction where rol_ext_id = 'ADM_1' and ftn_ext_id = 'LRN_RES_MAIN'
insert into acRoleFunction
select rol_id, ftn_id, '2004-04-20' from acRole, acFunction where rol_ext_id = 'ADM_1' and ftn_ext_id = 'RES_MGT_PUBLIC'
insert into acHomePage (
ac_hom_ent_id, ac_hom_rol_ext_id, ac_hom_ftn_ext_id, ac_hom_create_usr_id, ac_hom_create_timestamp
) values (
null, 'ADM_1', 'LRN_RES_MAIN', 's1u3', '2004-04-20'
)

/*
Auth: kawai
Date: 2004-04-21
Desc: update login url to a relative path rather than an absolute path
      (because there may be virtual path in the url)
      (!!!make sure no customized path is overwritten!!!)
*/
update acSite
set ste_login_url = '../login/index.htm'

/*
Auth: kawai
Date: 2004-04-21
Desc: grant Learning Resource Management to Training Administrator
      (previously missed)
*/
insert into acRoleFunction
select rol_id, ftn_id, '2004-04-21' from acRole, acFunction where rol_ext_id = 'TADM_1' and ftn_ext_id = 'LRN_RES_MAIN'
insert into acHomePage (
ac_hom_ent_id, ac_hom_rol_ext_id, ac_hom_ftn_ext_id, ac_hom_create_usr_id, ac_hom_create_timestamp
) values (
null, 'TADM_1', 'LRN_RES_MAIN', 's1u3', '2004-04-21'
)


/*
Auth: kelvin
Date: 2004-05-10
Desc: Email notification template for user profile import
*/
insert into xslTemplate
(xtp_type, xtp_subtype, xtp_channel_type, xtp_channel_api, xtp_xsl, xtp_xml_url, xtp_mailmerge_ind, xtp_title)
values
('USER_IMPORT_SUCCESS', 'HTML', 'SMTP', null, 'notify_user_import.xsl', 'http://cw01:201/servlet/aeAction?', 0, null)

declare @xtp_id int
select @xtp_id = max(xtp_id) from xslTemplate

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 1, 'ent_ids')
insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 2, 'sender_id')
insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 3, 'cmd')
insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 4, 'cc_ent_ids')
insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 5, 'src_file')
insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 6, 'start_time')
insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 7, 'end_time')
insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 8, 'success_total')
insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 9, 'unsuccess_total')

/**
Auth: kelvin
Date: 2004-05-21
Desc: Update System Message
*/
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_usr022_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'USR022' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_usr024_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'USR024' and sms_lan = 'ISO-8859-1'"

call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_usr021_gb2312.txt	SystemMessage sms_desc "sms_id = 'USR021' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_usr022_gb2312.txt	SystemMessage sms_desc "sms_id = 'USR022' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_usr024_gb2312.txt	SystemMessage sms_desc "sms_id = 'USR024' and sms_lan = 'GB2312'"

call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_usr022_big5.txt	SystemMessage sms_desc "sms_id = 'USR022' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_usr024_big5.txt	SystemMessage sms_desc "sms_id = 'USR024' and sms_lan = 'Big5'"

/*
Author: Dennis Yip
Desc: Update object view for learner report
      1. pre-select all columns by default
      2. added sortable attribute
Date: 2004-04-14
*/
call %UPDXMLCMD% xml\ObjectView\ojv_option_learner_item.xml			ObjectView ojv_option_xml "ojv_owner_ent_id = 1 AND ojv_type = 'LEARNER_REPORT' AND ojv_subtype = 'ITM'"
call %UPDXMLCMD% xml\ObjectView\ojv_option_learner_other.xml			ObjectView ojv_option_xml "ojv_owner_ent_id = 1 AND ojv_type = 'LEARNER_REPORT' AND ojv_subtype = 'OTHER'"
call %UPDXMLCMD% xml\ObjectView\ojv_option_learner_usr.xml			ObjectView ojv_option_xml "ojv_owner_ent_id = 1 AND ojv_type = 'LEARNER_REPORT' AND ojv_subtype = 'USR'"

/*
Author: Donald
Desc: Update object view for learner report, change "Title" and "Code" to "Course Title" and "Course Code"
Date: 2004-04-15
*/
call %UPDXMLCMD% xml\ObjectView\ojv_option_learner_item.xml			ObjectView ojv_option_xml "ojv_owner_ent_id = 1 AND ojv_type = 'LEARNER_REPORT' AND ojv_subtype = 'ITM'"

/*
Author: Dennis Yip
Desc: Update object view for learner report, add paramname to "Course Title" and "Course Code"
Date: 2004-04-16
*/
call %UPDXMLCMD% xml\ObjectView\ojv_option_learner_item.xml			ObjectView ojv_option_xml "ojv_owner_ent_id = 1 AND ojv_type = 'LEARNER_REPORT' AND ojv_subtype = 'ITM'"

/*
Author: Christ Qiu
Desc: Create new ReportTemplate for Learner Activity Report by Course
Date: 2004-04-20
*/
call %UPDXMLCMD% xml\ReportTemplate\rte_title_learner_course.xml			ReportTemplate rte_title_xml "rte_type = 'LEARNING_ACTIVITY_COS'"

/*
Author: Larry Liu
Desc: Create new ReportTemplate for Learner Activity Report by Course
Date: 2004-04-20
*/
if not exists (select * from ReportTemplate where rte_type = 'LEARNING_ACTIVITY_COS')
begin
INSERT INTO ReportTemplate( rte_title_xml, rte_type, rte_get_xsl, rte_exe_xsl, rte_dl_xsl, rte_meta_data_url, rte_seq_no, rte_owner_ent_id, rte_create_usr_id, rte_create_timestamp, rte_upd_usr_id, rte_upd_timestamp)
VALUES( 'XML', 'LEARNING_ACTIVITY_COS', 'rpt_lrn_cos_srh.xsl', 'rpt_lrn_cos_res.xsl', 'rpt_dl_lrn_cos_xls.xsl', NULL, 3, 1, 'slu3', '2004-06-10', 'slu3', '2004-06-10')
end
if not exists (select * from ReportTemplate where rte_type = 'LEARNING_ACTIVITY_LRN')
begin
INSERT INTO ReportTemplate( rte_title_xml, rte_type, rte_get_xsl, rte_exe_xsl, rte_dl_xsl, rte_meta_data_url, rte_seq_no, rte_owner_ent_id, rte_create_usr_id, rte_create_timestamp, rte_upd_usr_id, rte_upd_timestamp)
VALUES( 'XML', 'LEARNING_ACTIVITY_LRN', 'rpt_lrn_lrn_srh.xsl', 'rpt_lrn_lrn_res.xsl', 'rpt_dl_lrn_lrn_xls.xsl', NULL, 4, 1, 'slu3', '2004-06-10', 'slu3', '2004-06-10')
end

call %UPDXMLCMD% xml\ReportTemplate\rte_title_learner_course.xml			ReportTemplate rte_title_xml "rte_type = 'LEARNING_ACTIVITY_COS'"
call %UPDXMLCMD% xml\ReportTemplate\rte_title_learner_learner.xml			ReportTemplate rte_title_xml "rte_type = 'LEARNING_ACTIVITY_LRN'"

/*
Author: Larry Liu
Desc: Create new ObjectView for Learner Activity Report by Course
Date: 2004-04-20
*/
if not exists(select * from ObjectView where ojv_type = 'LEARNING_ACTIVITY_COS' and ojv_subtype = 'ITM')
begin
INSERT INTO ObjectView(ojv_owner_ent_id, ojv_type, ojv_subtype, ojv_option_xml, ojv_create_usr_id, ojv_create_timestamp, ojv_update_usr_id, ojv_update_timestamp)
VALUES(1, 'LEARNING_ACTIVITY_COS', 'ITM', 'XML', 'slu3', '2004-06-10', 'slu3', '2004-06-10')
end
if not exists(select * from ObjectView where ojv_type = 'LEARNING_ACTIVITY_COS' and ojv_subtype = 'OTHER')
begin
INSERT INTO ObjectView(ojv_owner_ent_id, ojv_type, ojv_subtype, ojv_option_xml, ojv_create_usr_id, ojv_create_timestamp, ojv_update_usr_id, ojv_update_timestamp)
VALUES(1, 'LEARNING_ACTIVITY_COS', 'OTHER', 'XML', 'slu3', '2004-06-10', 'slu3', '2004-06-10')
end
if not exists(select * from ObjectView where ojv_type = 'LEARNING_ACTIVITY_COS' and ojv_subtype = 'USR')
begin
INSERT INTO ObjectView(ojv_owner_ent_id, ojv_type, ojv_subtype, ojv_option_xml, ojv_create_usr_id, ojv_create_timestamp, ojv_update_usr_id, ojv_update_timestamp)
VALUES(1, 'LEARNING_ACTIVITY_COS', 'USR', 'XML', 'slu3', '2004-06-10', 'slu3', '2004-06-10')
end

call %UPDXMLCMD% xml\ObjectView\ojv_option_learner_course_usr.xml			ObjectView ojv_option_xml "ojv_owner_ent_id = 1 AND ojv_type = 'LEARNING_ACTIVITY_COS' AND ojv_subtype = 'USR'"
call %UPDXMLCMD% xml\ObjectView\ojv_option_learner_course_other.xml			ObjectView ojv_option_xml "ojv_owner_ent_id = 1 AND ojv_type = 'LEARNING_ACTIVITY_COS' AND ojv_subtype = 'OTHER'"

/*
Author: Donald Liu
Desc: Create new ObjectView for Learner Activity Report by Learner
Date: 2004-04-21
*/
if not exists(select * from ObjectView where ojv_type = 'LEARNING_ACTIVITY_LRN' and ojv_subtype = 'ITM')
begin
INSERT INTO ObjectView(ojv_owner_ent_id, ojv_type, ojv_subtype, ojv_option_xml, ojv_create_usr_id, ojv_create_timestamp, ojv_update_usr_id, ojv_update_timestamp)
VALUES(1, 'LEARNING_ACTIVITY_LRN', 'ITM', 'XML', 'slu3', '2004-06-10', 'slu3', '2004-06-10')
end
if not exists(select * from ObjectView where ojv_type = 'LEARNING_ACTIVITY_LRN' and ojv_subtype = 'OTHER')
begin
INSERT INTO ObjectView(ojv_owner_ent_id, ojv_type, ojv_subtype, ojv_option_xml, ojv_create_usr_id, ojv_create_timestamp, ojv_update_usr_id, ojv_update_timestamp)
VALUES(1, 'LEARNING_ACTIVITY_LRN', 'OTHER', 'XML', 'slu3', '2004-06-10', 'slu3', '2004-06-10')
end
if not exists(select * from ObjectView where ojv_type = 'LEARNING_ACTIVITY_LRN' and ojv_subtype = 'USR')
begin
INSERT INTO ObjectView(ojv_owner_ent_id, ojv_type, ojv_subtype, ojv_option_xml, ojv_create_usr_id, ojv_create_timestamp, ojv_update_usr_id, ojv_update_timestamp)
VALUES(1, 'LEARNING_ACTIVITY_LRN', 'USR', 'XML', 'slu3', '2004-06-10', 'slu3', '2004-06-10')
end

call %UPDXMLCMD% xml\ObjectView\ojv_option_learner_learner_item.xml   objectview ojv_option_xml "ojv_type='LEARNING_ACTIVITY_LRN' and ojv_subtype='itm'"
call %UPDXMLCMD% xml\ObjectView\ojv_option_learner_learner_other.xml   objectview ojv_option_xml "ojv_type='LEARNING_ACTIVITY_LRN' and ojv_subtype='other'"

/*
Author: Larry Liu
Desc: insert acreportTemplate respord to the new data of reportTemplate.
Date: 2004-04-20
*/
if not exists(select * from acReportTemplate where ac_rte_id in (select rte_id from ReportTemplate where rte_type = 'LEARNING_ACTIVITY_COS'))
begin
INSERT INTO acReportTemplate(ac_rte_id, ac_rte_ent_id, ac_rte_rol_ext_id, ac_rte_ftn_ext_id, ac_rte_owner_ind, ac_rte_create_usr_id, ac_rte_create_timestamp)
select rte_id, null, 'TADM_1', 'RTE_READ', 0, 's1u3', '2004-06-10'
from ReportTemplate where rte_type = 'LEARNING_ACTIVITY_COS'
end
if not exists(select * from acReportTemplate where ac_rte_id in (select rte_id from ReportTemplate where rte_type = 'LEARNING_ACTIVITY_LRN'))
begin
INSERT INTO acReportTemplate(ac_rte_id, ac_rte_ent_id, ac_rte_rol_ext_id, ac_rte_ftn_ext_id, ac_rte_owner_ind, ac_rte_create_usr_id, ac_rte_create_timestamp)
select rte_id, null, 'TADM_1', 'RTE_READ', 0, 's1u3', '2004-06-10'
from ReportTemplate where rte_type = 'LEARNING_ACTIVITY_LRN'
end

/*
Author: Larry Liu
Desc:Change ReportTemplates' order
Date:2004-05-08
*/
update ReportTemplate set rte_seq_no = 5 where rte_type = 'SURVEY_COS_GRP'

/**
Auth: kelvin
Date: 2004-06-14
Desc: Update System Message
*/
if not exists(select * from SystemMessage where sms_id = 'UGR001' and sms_lan = 'ISO-8859-1')
begin
insert into SystemMessage (sms_id, sms_lan, sms_desc)
values ('UGR001', 'ISO-8859-1', null)
end
if not exists(select * from SystemMessage where sms_id = 'UGR001' and sms_lan = 'GB2312')
begin
insert into SystemMessage (sms_id, sms_lan, sms_desc)
values ('UGR001', 'GB2312', null)
end
if not exists(select * from SystemMessage where sms_id = 'UGR001' and sms_lan = 'Big5')
begin
insert into SystemMessage (sms_id, sms_lan, sms_desc)
values ('UGR001', 'Big5', null)
end

call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_ugr001_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'UGR001' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_ugr001_gb2312.txt	SystemMessage sms_desc "sms_id = 'UGR001' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_ugr001_big5.txt	SystemMessage sms_desc "sms_id = 'UGR001' and sms_lan = 'Big5'"

/*
Author: Dennis Yip
Date: 2004-06-10
Desc: Add new table for access control on Learning Resource Management
*/
CREATE TABLE [dbo].[ObjectiveAccess] (
	[oac_obj_id] [int] NOT NULL ,
	[oac_access_type] [nvarchar] (20) NOT NULL ,
	[oac_ent_id] [int] NULL
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[ObjectiveAccess] ADD
	CONSTRAINT [FK_ObjectiveAccess_Entity] FOREIGN KEY
	(
		[oac_ent_id]
	) REFERENCES [dbo].[Entity] (
		[ent_id]
	),
	CONSTRAINT [FK_ObjectiveAccess_Objective] FOREIGN KEY
	(
		[oac_obj_id]
	) REFERENCES [dbo].[Objective] (
		[obj_id]
	)
GO

/*
Author: Dennis Yip
Date: 2004-06-11
Desc: Add new acFunction for Learning Resource Administration
      And assign it to System Administrator
*/
declare @lrn_res_admin int
declare @lrn_res_main int
declare @admin_id int
declare @admin_ext_id nvarchar(255)

declare cur_admin cursor for
select rol_id, rol_ext_id from acRole where rol_ste_uid = 'SADM'

insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('LRN_RES_ADMIN', 'FUNCTION', 'HOMEPAGE', getdate(), '<function id="LRN_RES_ADMIN"/>"')

select @lrn_res_admin = ftn_id from acFunction where ftn_ext_id = 'LRN_RES_ADMIN'
select @lrn_res_main = ftn_id from acFunction where ftn_ext_id = 'LRN_RES_MAIN'

open cur_admin
fetch next from cur_admin into @admin_id, @admin_ext_id
while @@fetch_status = 0
begin
 delete from acHomepage where ac_hom_rol_ext_id = @admin_ext_id and ac_hom_ftn_ext_id = 'LRN_RES_MAIN'
 delete from acRoleFunction where rfn_rol_id = @admin_id and rfn_ftn_id = @lrn_res_main
 insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_creation_timestamp) values (@admin_id, @lrn_res_admin, getdate())
 insert into acHomepage (ac_hom_ent_id, ac_hom_rol_ext_id, ac_hom_ftn_ext_id, ac_hom_create_usr_id, ac_hom_create_timestamp)
   values (NULL, @admin_ext_id, 'LRN_RES_ADMIN', 's1u3', getdate())

 fetch next from cur_admin into @admin_id, @admin_ext_id
end
close cur_admin
deallocate cur_admin

/*
Author: Dennis Yip
Date: 2004-06-11
Desc: Update label for Learning Resource Management/Administration
*/
call %UPDXMLCMD% xml\acFunction\ftn_lrn_res_main.xml	acFunction ftn_xml "ftn_ext_id = 'LRN_RES_MAIN'"
call %UPDXMLCMD% xml\acFunction\ftn_lrn_res_admin.xml	acFunction ftn_xml "ftn_ext_id = 'LRN_RES_ADMIN'"

/**
Auth: kelvin
Date: 2004-06-14
Desc: For lazy deletion of objectives.
*/
ALTER TABLE Objective ADD obj_status nvarchar(20)

/*
Auth: kelvin
Date: 2004-06-14
Desc: DROP CONSTRAINT of Resources
*/
ALTER TABLE dbo.Resources DROP CONSTRAINT FK_Resources_Resources

/*
Auth: kelvin
Date: 2004-06-14
Desc: SET the obj_status for LOST&FOUND to DELETED so as to hide the recycle bin.
*/
UPDATE Objective set obj_status = 'DELETED' WHERE obj_id=1;

/*
Auth: Dennis Yip
Date: 2004-06-14
Desc: Update the existing Objective to 'OK'
*/
update objective set obj_status = 'OK' where obj_type = 'SYB'

/*
Auth: Dennis Yip
Date: 2004-06-14
Desc: Create Access Control records for Learning Resource Management into ObjectiveAccess
*/
declare @obj_id int
declare @ent_id int
declare @rol_id int

declare obj_cursor cursor for
select obj_id  from objective where obj_type = 'SYB' and obj_obj_id_parent is null and obj_status = 'OK'

declare rol_cursor cursor for
select rol_id from acFunction, acRoleFunction, acRole where ftn_ext_id = 'LRN_RES_MAIN' and rfn_ftn_id = ftn_id and rfn_rol_id = rol_id

open obj_cursor
fetch next from obj_cursor into @obj_id
while @@fetch_status = 0
begin
	if not exists (select oac_obj_id from ObjectiveAccess where oac_obj_id = @obj_id and oac_access_type = 'READER')
	begin
		insert into ObjectiveAccess (oac_obj_id, oac_access_type, oac_ent_id)
		values (@obj_id, 'READER', NULL)
	end

	if not exists (select oac_obj_id from ObjectiveAccess where oac_obj_id = @obj_id and oac_access_type = 'AUTHOR')
	begin
		insert into ObjectiveAccess (oac_obj_id, oac_access_type, oac_ent_id)
		values (@obj_id, 'AUTHOR', NULL)
	end
	fetch next from obj_cursor into @obj_id
end
close obj_cursor

open rol_cursor
fetch next from rol_cursor into @rol_id
while @@fetch_status = 0
begin
	declare ent_cursor cursor for
	select erl_ent_id from acEntityRole where erl_rol_id = @rol_id

	open ent_cursor
	fetch next from ent_cursor into @ent_id
	while @@fetch_status = 0
	begin
		open obj_cursor
		fetch next from obj_cursor into @obj_id
		while @@fetch_status = 0
		begin
			if not exists (select oac_obj_id from ObjectiveAccess where oac_obj_id = @obj_id and oac_access_type = 'OWNER' and oac_ent_id = @ent_id)
			begin
				insert into ObjectiveAccess (oac_obj_id, oac_access_type, oac_ent_id)
				values (@obj_id, 'OWNER', @ent_id)
			end
			fetch next from obj_cursor into @obj_id
		end
		close obj_cursor
		fetch next from ent_cursor into @ent_id
	end
	close ent_cursor
	deallocate ent_cursor
	fetch next from rol_cursor into @rol_id
end
close rol_cursor
deallocate rol_cursor
deallocate obj_cursor

/*
Auth: Dennis Yip
Date: 2004-06-14
Desc: Remove recourses in Recycle Bin and Remove the Recycle Bin
*/
declare @obj_id int
declare obj_cursor cursor for
select obj_id from objective where obj_type = 'SYS'

open obj_cursor
fetch next from obj_cursor into @obj_id
while @@fetch_status = 0
begin
	delete from Interaction where int_res_id in (select rob_res_id from ResourceObjective where rob_obj_id = @obj_id)
	delete from Question where que_res_id in (select rob_res_id from ResourceObjective where rob_obj_id = @obj_id)
	delete from ResourcePermission where rpm_res_id in (select rob_res_id from ResourceObjective where rob_obj_id = @obj_id)
	update Resources set res_status = 'DELETED' where res_id in (select rob_res_id from ResourceObjective where rob_obj_id = @obj_id)
	delete from ResourceObjective where rob_obj_id = @obj_id
	delete from Resources where res_status = 'DELETED'
	delete from Objective where obj_id = @obj_id
	fetch next from obj_cursor into @obj_id
end
close obj_cursor
deallocate obj_cursor

/*
Auth: Dennis Yip
Date: 2004-06-14
Desc: Remove old access control resources (ResourcePermission) for resources inside Learning Resources Management
*/
delete from ResourcePermission where rpm_res_id in (select res_id from Resources where res_res_id_root is null and res_type in ('GEN', 'AICC', 'QUE') )

/*
Auth: Dennis Yip
Date: 2004-06-21
Desc: Update SystemMessage as Recycle Bin for Resource is no longer exist
*/
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_obj005_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'OBJ005' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_obj005_gb2312.txt		SystemMessage sms_desc "sms_id = 'OBJ005' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_obj005_big5.txt		SystemMessage sms_desc "sms_id = 'OBJ005' and sms_lan = 'Big5'"

/*
Auth: Donald Liu
Date: 2004-07-13
Desc: update aeItemType change ClassRoom and Class's "qdb_ind" ;remove the default value in aeTemplate and aeTemplateView
*/
update aeItemType set ity_qdb_ind = 0 where ity_id ='classroom' and ity_run_ind = 0
update aeItemType set ity_qdb_ind = 1 where ity_id ='classroom' and ity_run_ind = 1

call %UPDXMLCMD% xml\aeTemplate\tpl_item_classroom.xml		aeTemplate tpl_xml "tpl_title = 'Classroom Item Template'"
call %UPDXMLCMD% xml\aeTemplate\tpl_item_classroom_run.xml	aeTemplate tpl_xml "tpl_title = 'Classroom Run ItemTemplate'"

/*
Auth: Christ Qiu
Date: 2004-07-13
Desc: Create new role Instructor
*/
insert into acRole(rol_ext_id,rol_seq_id,rol_ste_ent_id,rol_url_home,rol_creation_timestamp,rol_ste_default_ind,rol_report_ind,rol_skin_root,rol_status,rol_ste_uid,rol_target_ent_type,rol_auth_level)
	values('INSTR_1',8,1,'servlet/qdbAction?cmd=home&stylesheet=home.xsl',getDate(),0 ,0,'skin3','OK' ,'INSTR' ,NULL,3)

call %UPDXMLCMD% xml\acRole\rol_instr_1.xml	acRole rol_xml "rol_ext_id = 'INSTR_1'"

/*
Auth: Christ Qiu
Date: 2004-07-13
Desc: Create new function TEACHING_COURSE_LIST (My Teaching Course), and INSTR_IN_ITM_ACC
*/
insert into acFunction(ftn_ext_id,ftn_level,ftn_type,ftn_creation_timestamp)
values('TEACHING_COURSE_LIST','FUNCTION','HOMEPAGE',getdate())

insert into acFunction(ftn_ext_id,ftn_level,ftn_type,ftn_creation_timestamp)
values('INSTR_IN_ITM_ACC','FUNCTION','ITEM',getdate())

call %UPDXMLCMD% xml\acFunction\ftn_teaching_course_list.xml acFunction ftn_xml "ftn_ext_id = 'TEACHING_COURSE_LIST'"


/*
Auth: Christ Qiu
Date: 2004-07-13
Desc: Assign new functions to Instructor
*/
declare @instr int
declare @teching_course_list int
declare @instr_in_itm_acc int

select @instr = rol_id from acRole where rol_ext_id = 'INSTR_1'
select @teching_course_list = ftn_id from acFunction where ftn_ext_id = 'TEACHING_COURSE_LIST'
select @instr_in_itm_acc = ftn_id from acFunction where ftn_ext_id = 'INSTR_IN_ITM_ACC'

insert into acRoleFunction values(@instr,@teching_course_list,getdate())
insert into acRoleFunction values(@instr,@instr_in_itm_acc,getdate())

insert into acHomepage values(NULL,'INSTR_1','TEACHING_COURSE_LIST','s1u3',getdate())

/*
Auth: Christ Qiu
Date: 2004-07-14
Desc: Assign new functions to Instructor
*/
call %UPDXMLCMD% xml\aeTemplateView\tvw_item_selfstudy_detail.xml	aeTemplateview tvw_xml "tvw_id = 'DETAIL_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Self Study Item Template')"

/*
Auth: Christ Qiu
Date: 2004-07-14
Desc: add new role "Instructor" to class
*/
call %UPDXMLCMD% xml\aeTemplateView\tvw_item_classroom_run_detail.xml	aeTemplateview tvw_xml "tvw_id = 'DETAIL_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Classroom Run ItemTemplate')"



/*
Auth: Christ Qiu
Date: 2004-07-14
Desc: Assign new functions to Instructor
*/
declare @instr int
declare @cos_permission_read int
declare @cos_permission_write int

select @instr = rol_id from acRole where rol_ext_id = 'INSTR_1'
select @cos_permission_read = ftn_id from acFunction where ftn_ext_id = 'COS_PERMISSION_READ'
select @cos_permission_write = ftn_id from acFunction where ftn_ext_id = 'COS_PERMISSION_WRITE'

insert into acRoleFunction values(@instr,@cos_permission_read,getdate())
insert into acRoleFunction values(@instr,@cos_permission_write,getdate())



/*
Auth: Christ Qiu
Date: 2004-07-16
Desc: Assign new homepage functions to Instructor
*/

insert into acHomePage(ac_hom_ent_id,ac_hom_rol_ext_id,ac_hom_ftn_ext_id,ac_hom_create_usr_id,ac_hom_create_timestamp)values(NULL,'INSTR_1','APPR_APP_LIST','s1u3',getdate())
insert into acHomePage(ac_hom_ent_id,ac_hom_rol_ext_id,ac_hom_ftn_ext_id,ac_hom_create_usr_id,ac_hom_create_timestamp)values(NULL,'INSTR_1','USR_OWN_MAIN','s1u3',getdate())
insert into acHomePage(ac_hom_ent_id,ac_hom_rol_ext_id,ac_hom_ftn_ext_id,ac_hom_create_usr_id,ac_hom_create_timestamp)values(NULL,'INSTR_1','USR_PWD_UPD','s1u3',getdate())
insert into acHomePage(ac_hom_ent_id,ac_hom_rol_ext_id,ac_hom_ftn_ext_id,ac_hom_create_usr_id,ac_hom_create_timestamp)values(NULL,'INSTR_1','USR_OWN_PREFER','s1u3',getdate())
insert into acHomePage(ac_hom_ent_id,ac_hom_rol_ext_id,ac_hom_ftn_ext_id,ac_hom_create_usr_id,ac_hom_create_timestamp)values(NULL,'INSTR_1','LRN_RES_MAIN','s1u3',getdate())
insert into acHomePage(ac_hom_ent_id,ac_hom_rol_ext_id,ac_hom_ftn_ext_id,ac_hom_create_usr_id,ac_hom_create_timestamp)values(NULL,'INSTR_1','SYS_MSG_LIST','s1u3',getdate())
insert into acHomePage(ac_hom_ent_id,ac_hom_rol_ext_id,ac_hom_ftn_ext_id,ac_hom_create_usr_id,ac_hom_create_timestamp)values(NULL,'INSTR_1','FOR_LINK','s1u3',getdate())


go
declare @instr int
declare @APPR_APP_LIST int
declare @USR_OWN_MAIN int
declare @USR_PWD_UPD int
declare @USR_OWN_PREFER int
declare @LRN_RES_MAIN int
declare @SYS_MSG_LIST int
declare @FOR_LINK int

select @instr = rol_id from acRole where rol_ext_id = 'INSTR_1'

select @APPR_APP_LIST = ftn_id from acFunction where ftn_ext_id = 'APPR_APP_LIST'

select @USR_OWN_MAIN = ftn_id from acFunction where ftn_ext_id = 'USR_OWN_MAIN'

select @USR_PWD_UPD = ftn_id from acFunction where ftn_ext_id = 'USR_PWD_UPD'

select @USR_OWN_PREFER = ftn_id from acFunction where ftn_ext_id = 'USR_OWN_PREFER'

select @LRN_RES_MAIN = ftn_id from acFunction where ftn_ext_id = 'LRN_RES_MAIN'

select @SYS_MSG_LIST = ftn_id from acFunction where ftn_ext_id = 'SYS_MSG_LIST'

select @FOR_LINK = ftn_id from acFunction where ftn_ext_id = 'FOR_LINK'

insert into acRoleFunction values(@instr,@APPR_APP_LIST,getdate())
insert into acRoleFunction values(@instr,@USR_OWN_MAIN,getdate())
insert into acRoleFunction values(@instr,@USR_PWD_UPD,getdate())
insert into acRoleFunction values(@instr,@USR_OWN_PREFER,getdate())
insert into acRoleFunction values(@instr,@LRN_RES_MAIN,getdate())
insert into acRoleFunction values(@instr,@SYS_MSG_LIST,getdate())
insert into acRoleFunction values(@instr,@FOR_LINK,getdate())

/*
Auth: Dennis Yip
Date: 2004-07-16
Desc: Assign acFunction "ITM_OFF_READ" to role Instructor
*/
declare @rol_id int
declare @ftn_id int

declare instr_cursor cursor for
select rol_id from acRole where rol_ste_uid = 'INSTR'

select @ftn_id = ftn_id from acFunction where ftn_ext_id = 'ITM_OFF_READ'

open instr_cursor
fetch next from instr_cursor into @rol_id
while @@fetch_status = 0
begin
	insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_creation_timestamp)
	values (@rol_id, @ftn_id, getdate())

	fetch next from instr_cursor into @rol_id
end
close instr_cursor
deallocate instr_cursor


/*
Auth: Christ Qiu
Date: 2004-07-19
Desc: Assign new functions to Instructor
*/

declare @instr int
declare @ftn_mod_mgt_in_cos int
declare @ftn_res_permission_read int
declare @ftn_res_permission_write int

select @instr = rol_id from acRole where rol_ext_id='INSTR_1'
select @ftn_mod_mgt_in_cos = ftn_id from acFunction where ftn_ext_id = 'MOD_MGT_IN_COS'
select @ftn_res_permission_read = ftn_id from acFunction where ftn_ext_id = 'RES_PERMISSION_READ'
select @ftn_res_permission_write = ftn_id from acFunction where ftn_ext_id = 'RES_PERMISSION_WRITE'


insert into acRoleFunction (rfn_rol_id,rfn_ftn_id,rfn_creation_timestamp)values(@instr,@ftn_mod_mgt_in_cos,getdate())
insert into acRoleFunction (rfn_rol_id,rfn_ftn_id,rfn_creation_timestamp)values(@instr,@ftn_res_permission_read,getdate())
insert into acRoleFunction (rfn_rol_id,rfn_ftn_id,rfn_creation_timestamp)values(@instr,@ftn_res_permission_write,getdate())

/*
Auth: Dennis Yip
Date: 2004-07-21
Desc: Update aeTemplateView indicator so that Instructor can be shown in Run
*/
update aeTemplateView set tvw_itm_acc_ind = 1
where tvw_id = 'DETAIL_VIEW'
and tvw_tpl_id = (select tpl_id from aeTemplate where tpl_title = 'Classroom Run ItemTemplate')


/*
Auth: Donald Liu
Date: 2004-07-21
Desc: delete some inds sun as qdb_ind,itm_create_run_ind,itm_apply_ind,itm_auto_enrol_qdb_ind
*/

call %UPDXMLCMD% xml\aeTemplate\tpl_item_classroom_run.xml	aeTemplate tpl_xml "tpl_title = 'Classroom Run ItemTemplate'"
call %UPDXMLCMD% xml\aeTemplateView\tvw_item_classroom_run_detail.xml	aeTemplateview tvw_xml "tvw_id = 'DETAIL_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Classroom Run ItemTemplate')"

call %UPDXMLCMD% xml\aeTemplate\tpl_item_classroom.xml			aeTemplate tpl_xml "tpl_title = 'Classroom Item Template'"
call %UPDXMLCMD% xml\aeTemplateView\tvw_item_classroom_detail.xml	aeTemplateview tvw_xml "tvw_id = 'DETAIL_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Classroom Item Template')"


/*
Auth: Dennis Yip
Date: 2004-07-26
Desc: Assign new functions to Instructor
*/
insert into acRoleFunction (rfn_rol_id,rfn_ftn_id,rfn_creation_timestamp)
select rol_id, ftn_id, getdate() from acRole, acFunction where rol_ste_uid = 'INSTR' and ftn_ext_id = 'MSG_MGT_IN_COS'

/*
Auth: Christ Qiu
Date: 2004-08-11
Desc: create new table RegUserExtension
*/
CREATE TABLE [dbo].[RegUserExtension] (
	[urx_usr_ent_id] [int] NOT NULL ,
	[urx_extra_datetime_11] [datetime] NULL ,
	[urx_extra_datetime_12] [datetime] NULL ,
	[urx_extra_datetime_13] [datetime] NULL ,
	[urx_extra_datetime_14] [datetime] NULL ,
	[urx_extra_datetime_15] [datetime] NULL ,
	[urx_extra_datetime_16] [datetime] NULL ,
	[urx_extra_datetime_17] [datetime] NULL ,
	[urx_extra_datetime_18] [datetime] NULL ,
	[urx_extra_datetime_19] [datetime] NULL ,
	[urx_extra_datetime_20] [datetime] NULL
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[RegUserExtension] ADD
	CONSTRAINT [PK_RegUserExtension] PRIMARY KEY  CLUSTERED
	(
		[urx_usr_ent_id]
	)  ON [PRIMARY]
GO

ALTER TABLE [dbo].[RegUserExtension] ADD
	CONSTRAINT [FK_RegUserExtension_RegUser] FOREIGN KEY
	(
		[urx_usr_ent_id]
	) REFERENCES [dbo].[RegUser] (
		[usr_ent_id]
	)
GO



/*
Auth: Christ Qiu
Date: 2004-08-17
Desc: create new table RegUserExtension
*/
declare @urx_ent_id int
declare user_cursor cursor for select usr_ent_id from RegUser where usr_ent_id not in (select urx_usr_ent_id from RegUserExtension) order by usr_ent_id

open user_cursor

fetch next from user_cursor into @urx_ent_id

while @@fetch_status = 0

begin

insert into RegUserExtension(urx_usr_ent_id) values(@urx_ent_id)

fetch next from user_cursor into @urx_ent_id

end

close user_cursor


/*
Author : Christ Qiu
Desc: New system message for upload zip file
Date: 2004-08-26
*/
insert SystemMessage values ('RES005', 'ISO-8859-1', NULL)
insert SystemMessage values ('RES005', 'Big5', NULL)
insert SystemMessage values ('RES005', 'GB2312', NULL)
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_RES005_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'RES005' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_RES005_gb2312.txt	SystemMessage sms_desc "sms_id = 'RES005' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_RES005_big5.txt	SystemMessage sms_desc "sms_id = 'RES005' and sms_lan = 'Big5'"
/*
Author : Donald Liu
Desc: new table: aeItemCost
Date: 2004-09-20
*/

CREATE TABLE [dbo].[aeItemCost] (
	[ito_itm_id] [int] NOT NULL ,
	[ito_type] [int] NOT NULL ,
	[ito_budget] [decimal](18, 2) NULL ,
	[ito_actual] [decimal](18, 2) NULL ,
	[ito_create_timestamp] [datetime] NOT NULL ,
	[ito_create_usr_id] [nvarchar] (20) COLLATE Chinese_Taiwan_Stroke_CI_AS NOT NULL ,
	[ito_update_timestamp] [datetime] NOT NULL ,
	[ito_update_usr_id] [nvarchar] (20) COLLATE Chinese_Taiwan_Stroke_CI_AS NOT NULL
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[aeItemCost] ADD
	CONSTRAINT [PK_aeItemCost] PRIMARY KEY  CLUSTERED
	(
		[ito_itm_id],
		[ito_type]
	)  ON [PRIMARY]



/*
Author: William.weng
Desc  : create the relation between Class and Reservation
Date  : 2004-09-21
*/
update aeTemplateView set tvw_rsv_ind=1 where tvw_id='DETAIL_VIEW' and tvw_tpl_id = (select tpl_id from aeTemplate where tpl_title='Classroom Run ItemTemplate')

call %UPDXMLCMD% xml\aeTemplateView\tvw_item_classroom_run_detail.xml	aeTemplateview tvw_xml "tvw_id = 'DETAIL_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Classroom Run ItemTemplate')"


/*
Author : Donald Liu
Desc: new table: tcTrainingCenter,tcTrainingCenterOfficer,tcTrainingCenterTargetEntity
Date: 2004-09-29
*/
CREATE TABLE [dbo].[tcTrainingCenter] (
	[tcr_id] [int] IDENTITY (1, 1) NOT NULL ,
	[tcr_code] [nvarchar] (50) COLLATE Chinese_Taiwan_Stroke_CI_AS NOT NULL ,
	[tcr_title] [nvarchar] (255) COLLATE Chinese_Taiwan_Stroke_CI_AS NOT NULL ,
	[tcr_ste_ent_id] [int] NOT NULL ,
	[tcr_status] [nvarchar] (10) COLLATE Chinese_Taiwan_Stroke_CI_AS NOT NULL ,
	[tcr_create_timestamp] [datetime] NOT NULL ,
	[tcr_create_usr_id] [nvarchar] (20) COLLATE Chinese_Taiwan_Stroke_CI_AS NOT NULL ,
	[tcr_update_timestamp] [datetime] NOT NULL ,
	[tcr_update_usr_id] [nvarchar] (20) COLLATE Chinese_Taiwan_Stroke_CI_AS NOT NULL
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[tcTrainingCenterOfficer] (
	[tco_tcr_id] [int] NOT NULL ,
	[tco_usr_ent_id] [int] NOT NULL ,
	[tco_rol_ext_id] [nvarchar] (255) COLLATE Chinese_Taiwan_Stroke_CI_AS NULL ,
	[tco_create_timestamp] [datetime] NOT NULL ,
	[tco_create_usr_id] [nvarchar] (20) COLLATE Chinese_Taiwan_Stroke_CI_AS NOT NULL
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[tcTrainingCenterTargetEntity] (
	[tce_tcr_id] [int] NOT NULL ,
	[tce_ent_id] [int] NOT NULL ,
	[tce_create_timestamp] [datetime] NOT NULL ,
	[tce_create_usr_id] [nvarchar] (20) COLLATE Chinese_Taiwan_Stroke_CI_AS NOT NULL
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[tcTrainingCenter] ADD
	CONSTRAINT [PK_tcTrainingCenter] PRIMARY KEY  CLUSTERED
	(
		[tcr_id]
	)  ON [PRIMARY]
GO

ALTER TABLE [dbo].[tcTrainingCenterOfficer] ADD
	CONSTRAINT [PK_tcTrainingCenterOfficer] PRIMARY KEY  CLUSTERED
	(
		[tco_tcr_id],
		[tco_usr_ent_id]
	)  ON [PRIMARY]
GO

ALTER TABLE [dbo].[tcTrainingCenterTargetEntity] ADD
	CONSTRAINT [PK_tcTrainingCenterTargetEntity] PRIMARY KEY  CLUSTERED
	(
		[tce_tcr_id],
		[tce_ent_id]
	)  ON [PRIMARY]
GO

ALTER TABLE [dbo].[tcTrainingCenterOfficer] ADD
	CONSTRAINT [FK_tcTrainingCenterOfficer_RegUser] FOREIGN KEY
	(
		[tco_usr_ent_id]
	) REFERENCES [dbo].[RegUser] (
		[usr_ent_id]
	),
	CONSTRAINT [FK_tcTrainingCenterOfficer_tcTrainingCenter] FOREIGN KEY
	(
		[tco_tcr_id]
	) REFERENCES [dbo].[tcTrainingCenter] (
		[tcr_id]
	)
GO

ALTER TABLE [dbo].[tcTrainingCenterTargetEntity] ADD
	CONSTRAINT [FK_tcTrainingCenterTargetEntity_Entity] FOREIGN KEY
	(
		[tce_ent_id]
	) REFERENCES [dbo].[Entity] (
		[ent_id]
	),
	CONSTRAINT [FK_tcTrainingCenterTargetEntity_tcTrainingCenter] FOREIGN KEY
	(
		[tce_tcr_id]
	) REFERENCES [dbo].[tcTrainingCenter] (
		[tcr_id]
	)

/*
Author : Donald Liu
Desc: add new column "rol_tc_ind" in acRole,and "itm_tcr_id" in aeItem
Date: 2004-09-30
*/
insert into acFunction (ftn_ext_id,ftn_level,ftn_type,ftn_creation_timestamp) values('TC_MAIN','FUNCTION','HOMEPAGE',getdate())
alter table acRole add rol_tc_ind int
go
update acRole set rol_tc_ind = 0
update acRole set rol_tc_ind = 1 where rol_ste_uid = 'TADM'
alter table acRole alter column rol_tc_ind INT not null
alter table aeItem add itm_tcr_id int

/*
Author : Donald Liu
Desc: update ftn_xml for function "TC_MAIN"; and assign  "TC_MAIN" to role "SADM"
Date: 2004-09-30
*/

call %UPDXMLCMD% xml\acFunction\ftn_trainingcenter_mgt.xml acFunction ftn_xml "ftn_ext_id='TC_MAIN' and ftn_type='HOMEPAGE'"

insert into acRoleFunction  (rfn_rol_id,rfn_ftn_id,rfn_creation_timestamp)
select rol_id,ftn_id,getdate() from acRole,acFunction  where rol_ste_uid = 'SADM' and  ftn_ext_id = 'TC_MAIN'

insert into acHomepage (ac_hom_ent_id, ac_hom_rol_ext_id, ac_hom_ftn_ext_id, ac_hom_create_usr_id, ac_hom_create_timestamp)
select null, rol_ext_id, 'TC_MAIN', 's1u3', getdate() from acRole where rol_ste_uid = 'SADM'

/*
Author : Christ Qiu
Desc: Add new Extension fields to RegUserExtension
Date: 2004-09-30
*/

ALTER TABLE RegUserExtension
	ADD urx_extra_singleoption_21 nvarchar(255),urx_extra_singleoption_22 nvarchar(255),
    	urx_extra_singleoption_23 nvarchar(255),urx_extra_singleoption_24 nvarchar(255),
	    urx_extra_singleoption_25 nvarchar(255),urx_extra_singleoption_26 nvarchar(255),
	    urx_extra_singleoption_27 nvarchar(255),urx_extra_singleoption_28 nvarchar(255),
	    urx_extra_singleoption_29 nvarchar(255),urx_extra_singleoption_30 nvarchar(255)


ALTER TABLE RegUserExtension
	ADD urx_extra_multipleoption_31 nvarchar(255),urx_extra_multipleoption_32 nvarchar(255),
	    urx_extra_multipleoption_33 nvarchar(255),urx_extra_multipleoption_34 nvarchar(255),
	    urx_extra_multipleoption_35 nvarchar(255),urx_extra_multipleoption_36 nvarchar(255),
	    urx_extra_multipleoption_37 nvarchar(255),urx_extra_multipleoption_38 nvarchar(255),
        urx_extra_multipleoption_39 nvarchar(255),urx_extra_multipleoption_40 nvarchar(255)



/*
Author : Christ Qiu
Desc: Add new HomePage Function('INSTR_MAIN')to Instructor
Date: 2004-09-30
*/

/* First:add the new function */
insert into acFunction(ftn_ext_id,ftn_level,ftn_type,ftn_creation_timestamp)
values('INSTR_MAIN','FUNCTION','HOMEPAGE',getdate())

/* Second:assign the function to the corresponding role*/
declare @instr int
declare @instr_main int

select @instr_main = ftn_id from acFunction where ftn_ext_id = 'INSTR_MAIN'

select @instr = rol_id from acRole where rol_ext_id = 'TADM_1'
insert into acRoleFunction values(@instr,@instr_main,getdate())

select @instr = rol_id from acRole where rol_ext_id = 'ADM_1'
insert into acRoleFunction values(@instr,@instr_main,getdate())

/* Third:add it to HomePage(to show in the HomePage)*/
insert into
       acHomePage(ac_hom_ent_id,ac_hom_rol_ext_id,ac_hom_ftn_ext_id,ac_hom_create_usr_id,ac_hom_create_timestamp)
       values    (NULL,         'TADM_1',        'INSTR_MAIN',     's1u3',              getdate())

insert into
       acHomePage(ac_hom_ent_id,ac_hom_rol_ext_id,ac_hom_ftn_ext_id,ac_hom_create_usr_id,ac_hom_create_timestamp)
       values    (NULL,         'ADM_1',        'INSTR_MAIN',     's1u3',              getdate())

call %UPDXMLCMD% xml\acFunction\ftn_instr_main.xml acFunction ftn_xml "ftn_ext_id='INSTR_MAIN'"
/*TO Comfirm the changes:
	select * from acFunction where ftn_ext_id='INSTR_MAIN'
	select * from acRoleFunction where rfn_ftn_id=(select ftn_id from acFunction where ftn_ext_id='INSTR_MAIN')
	select * from acHomePage where ac_hom_rol_ext_id='INSTR_1'
*/

/*
Author : Donald Liu
Desc: add new column :itr_create_usr_id,itr_create_timestamp,itr_update_usr_id,itr_update_timestamp
Date: 2004-09-30
*/
alter table aeItemRequirement add itr_create_usr_id nvarchar(20)
alter table  aeItemRequirement add itr_create_timestamp datetime
alter table  aeItemRequirement add itr_update_usr_id nvarchar(20)
alter table  aeItemRequirement add itr_update_timestamp datetime

alter table aeItemRequirement alter column itr_create_usr_id nvarchar(20) not null
alter table aeItemRequirement alter column itr_create_timestamp datetime not null
alter table aeItemRequirement alter column itr_update_usr_id nvarchar(20) not null
alter table aeItemRequirement alter column itr_update_timestamp datetime not null


/*
Author : Dennis Yip
Desc: connecting training center with item
Date: 2004-10-04
*/

alter table aeTemplateView add tvw_tcr_ind int
go
update aeTemplateView set tvw_tcr_ind = 0
update aeTemplateView set tvw_tcr_ind = 1 where tvw_id = 'DETAIL_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Classroom Item Template')
update aeTemplateView set tvw_tcr_ind = 1 where tvw_id = 'DETAIL_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Classroom Run ItemTemplate')
update aeTemplateView set tvw_tcr_ind = 1 where tvw_id = 'DETAIL_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Self Study Item Template')
update aeTemplateView set tvw_tcr_ind = 1 where tvw_id = 'DETAIL_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Book Item Template')
update aeTemplateView set tvw_tcr_ind = 1 where tvw_id = 'DETAIL_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Audio/Video Item Template')
update aeTemplateView set tvw_tcr_ind = 1 where tvw_id = 'DETAIL_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Website Item Template')
alter table aeTemplateView alter column tvw_tcr_ind int not null

call %UPDXMLCMD% xml\aeTemplateView\tvw_item_classroom_detail.xml	aeTemplateview tvw_xml "tvw_id = 'DETAIL_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Classroom Item Template')"
call %UPDXMLCMD% xml\aeTemplateView\tvw_item_classroom_run_detail.xml	aeTemplateview tvw_xml "tvw_id = 'DETAIL_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Classroom Run ItemTemplate')"
call %UPDXMLCMD% xml\aeTemplateView\tvw_item_selfstudy_detail.xml	aeTemplateview tvw_xml "tvw_id = 'DETAIL_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Self Study Item Template')"
call %UPDXMLCMD% xml\aeTemplateView\tvw_item_website_detail.xml		aeTemplateview tvw_xml "tvw_id = 'DETAIL_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Website Item Template')"
call %UPDXMLCMD% xml\aeTemplateView\tvw_item_book_detail.xml		aeTemplateview tvw_xml "tvw_id = 'DETAIL_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Book Item Template')"
call %UPDXMLCMD% xml\aeTemplateView\tvw_item_audiovideo_detail.xml	aeTemplateview tvw_xml "tvw_id = 'DETAIL_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Audio/Video Item Template')"

/*
Author : Christ Qiu
Desc: Add new SystemMessage 'USR027'
Date: 2004-10-12
*/
insert into SystemMessage(sms_id,sms_lan)
	values('USR027','Big5')
insert into SystemMessage(sms_id,sms_lan)
	values('USR027','GB2312')
insert into SystemMessage(sms_id,sms_lan)
	values('USR027','ISO-8859-1')

call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_usr027_ISO-8859-1.txt SystemMessage sms_desc "sms_id = 'USR027' and sms_lan = 'ISO-8859-1'"

call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_usr027_GB2312.txt SystemMessage sms_desc "sms_id = 'USR027' and sms_lan = 'GB2312'"

call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_usr027_Big5.txt SystemMessage sms_desc "sms_id = 'USR027' and sms_lan = 'Big5'"


/*
Author : Donald Liu
Desc: migrate data for TraningCenter.assign all item's tc to "default trainingcenter" and assign entities whose role is "Tadm_1" to be the admin of the tc.
Date: 2004-10-12
*/

insert into tcTrainingCenter (tcr_code,tcr_title,tcr_ste_ent_id,tcr_status,tcr_create_timestamp,tcr_create_usr_id,tcr_update_timestamp,tcr_update_usr_id )
values ('0001','Default Training Center',1,'OK',getdate(),'s1u3',getdate(),'s1u3')

declare @ent_id int
declare @tc_id int
declare @root_group_id int

declare app_cursor cursor for
select erl_ent_id from acRole,acEntityRole,reguser
where rol_ste_uid = 'TADM' and rol_id = erl_rol_id and erl_ent_id = usr_ent_id
order by 1

declare app_cursor_1 cursor for
select gpm_ent_id_member  from userGroup,groupMember
where usg_ent_id = gpm_ent_id_group and gpm_type = 'USG_PARENT_USG' and usg_role = 'ROOT'
and getdate() between gpm_start_timestamp and gpm_end_timestamp order by 1

--insert into tcTrainingCenter
open app_cursor
fetch next from app_cursor into @ent_id
select @tc_id=tcr_id from tcTrainingCenter where tcr_title = 'Default Training Center'
while @@fetch_status = 0
begin
   insert into tcTrainingCenterOfficer values(@tc_id,@ent_id,'TADM_1',getdate(),'s1u3')
   fetch next from app_cursor into @ent_id
end
close app_cursor
deallocate app_cursor

--insert into tcTrainingCenterTargetEntity
open app_cursor_1
fetch next from app_cursor_1 into @root_group_id
while @@fetch_status = 0
begin
   insert into tcTrainingCenterTargetEntity values(@tc_id,@root_group_id,getdate(),'s1u3')
   fetch next from app_cursor_1 into @root_group_id
end
close app_cursor_1
deallocate app_cursor_1

update aeItem set itm_tcr_id = @tc_id where itm_tcr_id is null


/*
Author: Terry
Desc: add Functions for Learning Blueprint View & Admin
date: 2004-09-22
*/
INSERT INTO acFunction( [ftn_ext_id], [ftn_level], [ftn_type], [ftn_creation_timestamp], [ftn_xml])
VALUES( 'LRN_BLUEPRINT_VIEW', 'FUNCTION', 'HOMEPAGE', '2004-9-21 18:00:00', null)

INSERT INTO acRoleFunction
select rol_id, ftn_id, getdate() from acRole, acFunction where rol_ste_uid in ('LRNR', 'INSTR') and ftn_ext_id = 'LRN_BLUEPRINT_VIEW'

INSERT INTO acHomepage
select null, rol_ext_id, 'LRN_BLUEPRINT_VIEW', 's1u3', getdate() from acRole where rol_ste_uid in ('LRNR', 'INSTR')

INSERT INTO acFunction( [ftn_ext_id], [ftn_level], [ftn_type], [ftn_creation_timestamp], [ftn_xml])
VALUES( 'LRN_BLUEPRINT_ADMIN', 'FUNCTION', 'HOMEPAGE', '2004-9-21 18:00:00', null)

INSERT INTO acRoleFunction
select rol_id, ftn_id, getdate() from acRole, acFunction where rol_ste_uid in ('SADM', 'TADM') and ftn_ext_id = 'LRN_BLUEPRINT_ADMIN'

INSERT INTO acHomepage
select null, rol_ext_id, 'LRN_BLUEPRINT_ADMIN', 's1u3', getdate() from acRole where rol_ste_uid in ('SADM', 'TADM')

call %UPDXMLCMD% xml\acFunction\ftn_lrn_blueprint_view.xml	acFunction ftn_xml "ftn_ext_id = 'LRN_BLUEPRINT_VIEW'"
call %UPDXMLCMD% xml\acFunction\ftn_lrn_blueprint_admin.xml	acFunction ftn_xml "ftn_ext_id = 'LRN_BLUEPRINT_ADMIN'"

/*
Author: Terry
Desc: new Table bpBlueprint
date: 2004-09-24
*/
CREATE TABLE [dbo].[bpBlueprint] (
	[blp_ste_ent_id] [int] NOT NULL ,
	[blp_src_type] [nvarchar] (50) NOT NULL ,
	[blp_source] [nvarchar] (255) NULL ,
	[blp_path] [nvarchar] (255) COLLATE Chinese_Taiwan_Stroke_CI_AS NULL ,
	[blp_create_timestamp] [datetime] NOT NULL ,
	[blp_create_usr_id] [nvarchar] (20) COLLATE Chinese_Taiwan_Stroke_CI_AS NOT NULL ,
	[blp_update_timestamp] [datetime] NULL ,
	[blp_update_usr_id] [nvarchar] (20) COLLATE Chinese_Taiwan_Stroke_CI_AS NULL
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[bpBlueprint] ADD
	CONSTRAINT [PK_bpBlueprint] PRIMARY KEY  CLUSTERED
	(
		[blp_ste_ent_id]
	)  ON [PRIMARY]
GO

ALTER TABLE [dbo].[bpBlueprint] ADD
	CONSTRAINT [FK_bpBlueprint_acSite] FOREIGN KEY
	(
		[blp_ste_ent_id]
	) REFERENCES [acSite] (
		[ste_ent_id]
	)
GO

/*
Author: Terry
Desc: new SystemMessage
date: 2004-10-09
*/
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('BLP001', 'Big5','No Blueprint Present!')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('BLP001', 'GB2312','No Blueprint Present!')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('BLP001', 'ISO-8859-1','No Blueprint Present!')
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_blp001_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'BLP001' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_blp001_gb2312.txt	SystemMessage sms_desc "sms_id = 'BLP001' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_blp001_big5.txt	SystemMessage sms_desc "sms_id = 'BLP001' and sms_lan = 'Big5'"

/*
Author:William Weng
Desc:add a new acFunction "LRN_CALENDAR";Connect the acFunction "LRN_CALENDAR" to the acRole "NLRN_1" ,"TDAM_1"(The roles of Learner and Training Administrator)
date:2004-10-13
*/
insert into acFunction (ftn_ext_id,ftn_level,ftn_type,ftn_creation_timestamp) values ('LRN_CALENDAR','FUNCTION','HOMEPAGE',getdate())

insert into acRoleFunction (rfn_rol_id,rfn_ftn_id,rfn_creation_timestamp) select rol_id,ftn_id,getdate() from acRole,acFunction where rol_ext_id='NLRN_1'and ftn_ext_id='LRN_CALENDAR'

insert into acRoleFunction (rfn_rol_id,rfn_ftn_id,rfn_creation_timestamp) select rol_id,ftn_id,getdate() from acRole,acFunction where rol_ext_id='TADM_1'and ftn_ext_id='LRN_CALENDAR'

call %UPDXMLCMD% xml\acFunction\ftn_lrn_calendar.xml	acFunction ftn_xml "ftn_ext_id='LRN_CALENDAR'"

insert into acHomePage (ac_hom_rol_ext_id,ac_hom_ftn_ext_id,ac_hom_create_usr_id,ac_hom_create_timestamp) values
('TADM_1','LRN_CALENDAR','s1u3',getdate())

insert into acHomePage (ac_hom_rol_ext_id,ac_hom_ftn_ext_id,ac_hom_create_usr_id,ac_hom_create_timestamp) values ('NLRN_1','LRN_CALENDAR','s1u3',getdate())
/*
Author: Vincent
Desc: Update aeItemType,for auto insert CourseCriteria
date: 2004-10-21
*/
update aeItemType set ity_completion_criteria_ind = 1 where ity_id = 'CLASSROOM'

/*
Author: Dennis Yip
Desc: updated item template for training center
date: 2004-10-19
*/
call %UPDXMLCMD% xml\aeTemplateView\tvw_item_audiovideo_detail.xml	aeTemplateview tvw_xml "tvw_id = 'DETAIL_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Audio/Video Item Template')"
call %UPDXMLCMD% xml\aeTemplateView\tvw_item_book_detail.xml		aeTemplateview tvw_xml "tvw_id = 'DETAIL_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Book Item Template')"
call %UPDXMLCMD% xml\aeTemplateView\tvw_item_selfstudy_detail.xml	aeTemplateview tvw_xml "tvw_id = 'DETAIL_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Self Study Item Template')"
call %UPDXMLCMD% xml\aeTemplateView\tvw_item_website_detail.xml		aeTemplateview tvw_xml "tvw_id = 'DETAIL_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Website Item Template')"

/*
Author: Terry
Desc: add table aeItemLesson, aeItemLessonInstructor
		 new SystemMessage for ItemLesson
date: 2004-10-19
*/
CREATE TABLE [dbo].[aeItemLesson] (
	[ils_id] [int] IDENTITY (1, 1) NOT NULL ,
	[ils_itm_id] [int] NULL ,
	[ils_title] [nvarchar] (50) NULL ,
	[ils_day] [int] NULL ,
	[ils_start_time] [datetime] NULL ,
	[ils_end_time] [datetime] NULL ,
	[ils_create_timestamp] [datetime] NULL ,
	[ils_create_usr_id] [nvarchar] (20) NULL ,
	[ils_update_timestamp] [datetime] NULL ,
	[ils_update_usr_id] [nvarchar] (20) NULL
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[aeItemLesson] WITH NOCHECK ADD
	CONSTRAINT [PK_aeItemLesson] PRIMARY KEY  CLUSTERED
	(
		[ils_id]
	)  ON [PRIMARY]
GO

ALTER TABLE [dbo].[aeItemLesson] ADD
	CONSTRAINT [FK_aeItemLesson_aeItem] FOREIGN KEY
	(
		[ils_itm_id]
	) REFERENCES [dbo].[aeItem] (
		[itm_id]
	)
GO

CREATE TABLE [dbo].aeItemLessonInstructor (
	ili_ils_id int NOT NULL ,
	ili_usr_ent_id int NOT NULL ,
	ili_create_usr_id nvarchar (20) NULL ,
	ili_create_timestamp datetime NULL
)
GO

ALTER TABLE [dbo].aeItemLessonInstructor ADD
	CONSTRAINT FK_aeItemLessonInstructor_aeItemLesson FOREIGN KEY
	(
		ili_ils_id
	) REFERENCES [dbo].aeItemLesson (
		ils_id
	),
	CONSTRAINT FK_aeItemLessonInstructor_RegUser FOREIGN KEY
	(
		ili_usr_ent_id
	) REFERENCES [dbo].RegUser (
		usr_ent_id
	)
GO

insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('ILS001', 'Big5','Lesson added successfully.')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('ILS001', 'GB2312','Lesson added successfully.')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('ILS001', 'ISO-8859-1','Lesson added successfully.')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('ILS002', 'Big5','Lesson updated successfully.')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('ILS002', 'GB2312','Lesson updated successfully.')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('ILS002', 'ISO-8859-1','Lesson updated successfully.')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('ILS003', 'Big5','Failed to add lesson! ')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('ILS003', 'GB2312','Failed to add lesson! ')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('ILS003', 'ISO-8859-1','Failed to add lesson! ')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('ILS004', 'Big5','Failed to update lesson!')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('ILS004', 'GB2312','Failed to update lesson!')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('ILS004', 'ISO-8859-1','Failed to update lesson!')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('ILS005', 'Big5','Lesson deleted successfully!')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('ILS005', 'GB2312','Lesson deleted successfully!')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('ILS005', 'ISO-8859-1','Lesson deleted successfully!')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('ILS006', 'Big5','Failed to delete lesson!')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('ILS006', 'GB2312','Failed to delete lesson!')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('ILS006', 'ISO-8859-1','Failed to delete lesson!')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('ILS007', 'Big5','TimePeriod conflicted,please check the time!')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('ILS007', 'GB2312','TimePeriod conflicted,please check the time!')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('ILS007', 'ISO-8859-1','TimePeriod conflicted,please check the time!')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('ILS008', 'Big5','Date setted successfully!')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('ILS008', 'GB2312','Date setted successfully!')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('ILS008', 'ISO-8859-1','Date setted successfully!')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('ILS009', 'Big5','Failed to set date!')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('ILS009', 'GB2312','Failed to set date!')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('ILS009', 'ISO-8859-1','Failed to set date!')

call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_ils001_gb2312.txt	SystemMessage sms_desc "sms_id = 'ILS001' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_ils002_gb2312.txt	SystemMessage sms_desc "sms_id = 'ILS002' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_ils003_gb2312.txt	SystemMessage sms_desc "sms_id = 'ILS003' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_ils004_gb2312.txt	SystemMessage sms_desc "sms_id = 'ILS004' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_ils005_gb2312.txt	SystemMessage sms_desc "sms_id = 'ILS005' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_ils006_gb2312.txt	SystemMessage sms_desc "sms_id = 'ILS006' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_ils007_gb2312.txt	SystemMessage sms_desc "sms_id = 'ILS007' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_ils008_gb2312.txt	SystemMessage sms_desc "sms_id = 'ILS008' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_ils009_gb2312.txt	SystemMessage sms_desc "sms_id = 'ILS009' and sms_lan = 'GB2312'"

call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_ils001_big5.txt	SystemMessage sms_desc "sms_id = 'ILS001' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_ils002_big5.txt	SystemMessage sms_desc "sms_id = 'ILS002' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_ils003_big5.txt	SystemMessage sms_desc "sms_id = 'ILS003' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_ils004_big5.txt	SystemMessage sms_desc "sms_id = 'ILS004' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_ils005_big5.txt	SystemMessage sms_desc "sms_id = 'ILS005' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_ils006_big5.txt	SystemMessage sms_desc "sms_id = 'ILS006' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_ils007_big5.txt	SystemMessage sms_desc "sms_id = 'ILS007' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_ils008_big5.txt	SystemMessage sms_desc "sms_id = 'ILS008' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_ils009_big5.txt	SystemMessage sms_desc "sms_id = 'ILS009' and sms_lan = 'Big5'"

call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_ils001_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'ILS001' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_ils002_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'ILS002' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_ils003_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'ILS003' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_ils004_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'ILS004' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_ils005_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'ILS005' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_ils006_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'ILS006' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_ils007_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'ILS007' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_ils008_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'ILS008' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_ils009_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'ILS009' and sms_lan = 'ISO-8859-1'"
/*
Author:William Weng
Desc:add a new column "att_rate_remark" to table "aeAttendance"
date:2004-10-20
*/

alter table aeAttendance add att_rate_remark nvarchar(200)

/*
Author: Vincent
Desc: Create table
date: 2004-10-21
*/
if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[FK_MeasurementEvaluation_CourseMeasurement]') and OBJECTPROPERTY(id, N'IsForeignKey') = 1)
ALTER TABLE [dbo].[MeasurementEvaluation] DROP CONSTRAINT FK_MeasurementEvaluation_CourseMeasurement
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[CourseMeasurement]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [dbo].[CourseMeasurement]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[MeasurementEvaluation]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [dbo].[MeasurementEvaluation]
GO

CREATE TABLE [dbo].[CourseMeasurement] (
	[cmt_id] [int] IDENTITY (1, 1) NOT NULL ,
	[cmt_title] [nvarchar] (255) COLLATE Chinese_Taiwan_Stroke_CI_AS NOT NULL ,
	[cmt_ccr_id] [int] NOT NULL ,
	[cmt_cmr_id] [int] NULL ,
	[cmt_max_score] [decimal](18, 0) NOT NULL ,
	[cmt_status] [nvarchar] (10) COLLATE Chinese_Taiwan_Stroke_CI_AS NULL ,
	[cmt_contri_rate] [float] NOT NULL ,
	[cmt_is_contri_by_score] [int] NOT NULL ,
	[cmt_create_timestamp] [datetime] NOT NULL ,
	[cmt_create_usr_id] [nvarchar] (50) COLLATE Chinese_Taiwan_Stroke_CI_AS NOT NULL ,
	[cmt_update_timestamp] [datetime] NOT NULL ,
	[cmt_update_usr_id] [nvarchar] (50) COLLATE Chinese_Taiwan_Stroke_CI_AS NOT NULL ,
	[cmt_delete_timestamp] [datetime] NULL ,
	[cmt_pass_score] [decimal](18, 0) NOT NULL
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[MeasurementEvaluation] (
	[mtv_cos_id] [int] NOT NULL ,
	[mtv_ent_id] [int] NOT NULL ,
	[mtv_cmt_id] [int] NOT NULL ,
	[mtv_status] [nvarchar] (10) COLLATE Chinese_Taiwan_Stroke_CI_AS NULL ,
	[mtv_score] [decimal](18, 0) NULL ,
	[mtv_create_timestamp] [datetime] NOT NULL ,
	[mtv_create_usr_id] [nvarchar] (50) COLLATE Chinese_Taiwan_Stroke_CI_AS NOT NULL ,
	[mtv_update_timestamp] [datetime] NOT NULL ,
	[mtv_update_usr_id] [nvarchar] (50) COLLATE Chinese_Taiwan_Stroke_CI_AS NOT NULL ,
	[mtv_tkh_id] [int] NOT NULL
) ON [PRIMARY]
GO

insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('CTB001', 'Big5','??This Code Type isn''t exist! Please input again!')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('CTB001', 'GB2312','??This Code Type isn''t exist! Please input again!')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('CTB001', 'ISO-8859-1','??This Code Type isn''t exist! Please input again!')

call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_ctb001_iso-8859-1.txt   SystemMessage sms_desc "sms_id = 'CTB001' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_ctb001_GB2312.txt SystemMessage sms_desc "sms_id = 'CTB001' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_ctb001_Big5.txt SystemMessage sms_desc "sms_id = 'CTB001' and sms_lan = 'Big5'"


insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('CTB002', 'Big5','?There is a same id in this Code Type!Please input again!')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('CTB002', 'GB2312','?There is a same id in this Code Type!Please input again!')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('CTB002', 'ISO-8859-1','?There is a same id in this Code Type!Please input again!')

call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_ctb002_iso-8859-1.txt   SystemMessage sms_desc "sms_id = 'CTB002' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_ctb002_GB2312.txt SystemMessage sms_desc "sms_id = 'CTB002' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_ctb002_Big5.txt SystemMessage sms_desc "sms_id = 'CTB002' and sms_lan = 'Big5'"


/*
Author: Dennis Yip
Desc: update for cancel run
date: 2004-10-22
*/
update aeItemType set ity_can_cancel_ind = 1
where ity_run_ind = 1

call %UPDXMLCMD% xml\aeTemplateView\tvw_item_classroom_run_cancelled.xml	aeTemplateview tvw_xml "tvw_id = 'CANCELLED_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Classroom Run ItemTemplate')"
call %UPDXMLCMD% xml\aeTemplate\tpl_workflow_multilevel_en_us.xml	aeTemplate tpl_xml "tpl_title = 'Multi-level-approval Workflow Template'"



/*
Author : Donald Liu
Desc:  update table DisplayOption set 'NETG-COK' 'S  dpo_pass_score_ind = 1 . And you will see 'grading_policy' in the page.
Date: 2004-10-25
*/
update displayOption set dpo_pass_score_ind = 1 where dpo_res_type = 'MOD' AND          dpo_res_subtype = 'NETG_COK'


/*
Author : Dennis Yip
Desc:  updates for targeted enrollments
Date: 2004-10-30
*/
update aeTemplateView set tvw_target_ind = 1 where tvw_tpl_id = 13 and tvw_id = 'DETAIL_VIEW'
update aeTemplateView set tvw_target_ind = 1 where tvw_tpl_id = 13 and tvw_id = 'LRN_VIEW'

/*
Author : Dennis Yip
Desc:  new message for application conflicts
Date: 2004-10-30
*/
insert into SystemMessage values ('AEQM10', 'ISO-8859-1', 'XXX')
insert into SystemMessage values ('AEQM10', 'Big5', 'XXX')
insert into SystemMessage values ('AEQM10', 'GB2312', 'XXX')

call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_aeqm10_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'AEQM10' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_aeqm10_gb2312.txt	SystemMessage sms_desc "sms_id = 'AEQM10' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_aeqm10_big5.txt	SystemMessage sms_desc "sms_id = 'AEQM10' and sms_lan = 'Big5'"

/*
Author : Dennis Yip
Desc:  updates for class level completion criteria
Date: 2004-10-30
*/
update aeItem set itm_content_eff_start_datetime = itm_eff_start_datetime
where itm_run_ind = 1
and itm_content_eff_start_datetime is null
and (itm_content_eff_duration is null or itm_content_eff_duration = 0)

update aeItem set itm_content_eff_end_datetime = dateadd(day, 30, itm_eff_end_datetime)
where itm_run_ind = 1
and itm_content_eff_end_datetime is null
and (itm_content_eff_duration is null or itm_content_eff_duration = 0)

/*
Author : Dennis Yip
Desc:  updates for Class Management
Date: 2004-10-30
*/
insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('RUN_MAIN', 'FUNCTION', 'HOMEPAGE', getdate(), null)

insert into acRoleFunction select rol_id, ftn_id, getdate() from acRole, acFunction where rol_ste_uid = 'TADM' and ftn_ext_id = 'RUN_MAIN'

insert into acHomepage select null, rol_ext_id, 'RUN_MAIN', 's1u3', getdate() from acRole where rol_ste_uid = 'TADM'

call %UPDXMLCMD% xml\acFunction\ftn_run_main.xml	acFunction ftn_xml "ftn_ext_id = 'RUN_MAIN'"

/*
Author : Dennis Yip
Desc:  updates for XML templates
Date: 2004-10-30
*/
call %UPDXMLCMD% xml\aeTemplate\tpl_item_classroom.xml		aeTemplate tpl_xml "tpl_title = 'Classroom Item Template'"
call %UPDXMLCMD% xml\aeTemplate\tpl_item_classroom_run.xml	aeTemplate tpl_xml "tpl_title = 'Classroom Run ItemTemplate'"
call %UPDXMLCMD% xml\aeTemplate\tpl_item_selfstudy.xml		aeTemplate tpl_xml "tpl_title = 'Self Study Item Template'"

call %UPDXMLCMD% xml\aeTemplateView\tvw_item_classroom_detail.xml	aeTemplateview tvw_xml "tvw_id = 'DETAIL_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Classroom Item Template')"

call %UPDXMLCMD% xml\aeTemplateView\tvw_item_classroom_run_detail.xml	aeTemplateview tvw_xml "tvw_id = 'DETAIL_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Classroom Run ItemTemplate')"
call %UPDXMLCMD% xml\aeTemplateView\tvw_item_classroom_run_cancelled.xml	aeTemplateview tvw_xml "tvw_id = 'CANCELLED_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Classroom Run ItemTemplate')"
call %UPDXMLCMD% xml\aeTemplateView\tvw_item_classroom_run_lrn.xml	aeTemplateview tvw_xml "tvw_id = 'LRN_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Classroom Run ItemTemplate')"

call %UPDXMLCMD% xml\aeTemplateView\tvw_item_selfstudy_detail.xml	aeTemplateview tvw_xml "tvw_id = 'DETAIL_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Self Study Item Template')"
call %UPDXMLCMD% xml\aeTemplateView\tvw_item_selfstudy_lrn.xml		aeTemplateview tvw_xml "tvw_id = 'LRN_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Self Study Item Template')"

/*
Author : Terry
Desc: add columns and systemmessages
*/
ALTER TABLE dbo.CourseMeasurement ADD
	cmt_status_desc_option varchar(5) NULL
GO

Alter Table CourseCriteria ADD
	ccr_offline_condition nvarchar(2000) NULL
GO

Alter Table CourseModuleCriteria ADD
	cmr_status_desc_option varchar(5) NULL

insert into SystemMessage values ('CND001', 'ISO-8859-1', 'XXX')
insert into SystemMessage values ('CND001', 'Big5', 'XXX')
insert into SystemMessage values ('CND001', 'GB2312', 'XXX')
insert into SystemMessage values ('CND002', 'ISO-8859-1', 'XXX')
insert into SystemMessage values ('CND002', 'Big5', 'XXX')
insert into SystemMessage values ('CND002', 'GB2312', 'XXX')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('AEIT24', 'Big5', null)
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('AEIT24', 'GB2312', null)
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('AEIT24', 'ISO-8859-1', null)
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('ILS010', 'Big5', null)
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('ILS010', 'GB2312', null)
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('ILS010', 'ISO-8859-1', null)
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('ILS011', 'Big5', null)
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('ILS011', 'GB2312', null)
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('ILS011', 'ISO-8859-1', null)

call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_cnd001_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'CND001' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_cnd001_gb2312.txt	SystemMessage sms_desc "sms_id = 'CND001' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_cnd001_big5.txt	SystemMessage sms_desc "sms_id = 'CND001' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_cnd002_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'CND002' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_cnd002_gb2312.txt	SystemMessage sms_desc "sms_id = 'CND002' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_cnd002_big5.txt	SystemMessage sms_desc "sms_id = 'CND002' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_aeit24_gb2312.txt	SystemMessage sms_desc "sms_id = 'AEIT24' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_aeit24_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'AEIT24' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_aeit24_big5.txt	SystemMessage sms_desc "sms_id = 'AEIT24' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_ils010_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'ILS010' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_ils010_gb2312.txt	SystemMessage sms_desc "sms_id = 'ILS010' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_ils010_big5.txt	SystemMessage sms_desc "sms_id = 'ILS010' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_ils011_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'ILS011' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_ils011_gb2312.txt	SystemMessage sms_desc "sms_id = 'ILS011' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_ils011_big5.txt	SystemMessage sms_desc "sms_id = 'ILS011' and sms_lan = 'Big5'"
/*
Author : Donald
Desc: add new message to check duplicate "tcr_code"
*/
insert into systemmessage (sms_id,sms_lan)  values ('TC001','ISO-8859-1')
insert into systemmessage (sms_id,sms_lan)  values ('TC001','Big5')
insert into systemmessage (sms_id,sms_lan)  values ('TC001','GB2312')

call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_tc001_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'TC001' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_tc001_gb2312.txt	SystemMessage sms_desc "sms_id = 'TC001' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_tc001_big5.txt	SystemMessage sms_desc "sms_id = 'TC001' and sms_lan = 'Big5'"

/*
Author : Dennis Yip
Desc:  create training administrator records for classes that do not have training administrator assigned.
Date: 2004-11-01
*/
insert into aeItemAccess
select itm_id, iac_ent_id , iac_access_type, iac_access_id, null from aeItem, aeItemAccess, aeItemRelation
where itm_run_ind = 1
and itm_id = ire_child_itm_id
and iac_itm_id = ire_parent_itm_id
and iac_access_type = 'ROLE'
and iac_access_id = (select rol_ext_id from acRole where rol_ste_uid = 'TADM')
and not exists (select * from aeItemAccess b where b.iac_itm_id = itm_id and b.iac_access_type = 'ROLE' and b.iac_access_id = (select rol_ext_id from acRole where rol_ste_uid = 'TADM'))

/*
Author : Dennis Yip
Desc:  update item templates
Date: 2004-11-04
*/

update aeTemplateView set tvw_target_ind = 1, tvw_itm_acc_ind = 1, tvw_rsv_ind = 1
where tvw_id = 'CANCELLED_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Classroom Run ItemTemplate')

call %UPDXMLCMD% xml\aeTemplateView\tvw_item_classroom_detail.xml	aeTemplateview tvw_xml "tvw_id = 'DETAIL_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Classroom Item Template')"
call %UPDXMLCMD% xml\aeTemplateView\tvw_item_classroom_run_lrn.xml	aeTemplateview tvw_xml "tvw_id = 'LRN_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Classroom Run ItemTemplate')"
call %UPDXMLCMD% xml\aeTemplateView\tvw_item_classroom_run_detail.xml	aeTemplateview tvw_xml "tvw_id = 'DETAIL_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Classroom Run ItemTemplate')"
call %UPDXMLCMD% xml\aeTemplateView\tvw_item_classroom_run_cancelled.xml	aeTemplateview tvw_xml "tvw_id = 'CANCELLED_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Classroom Run ItemTemplate')"
call %UPDXMLCMD% xml\aeTemplateView\tvw_item_selfstudy_detail.xml	aeTemplateview tvw_xml "tvw_id = 'DETAIL_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Self Study Item Template')"

/*
Author : Christ Qiu
Desc: add new SystemMessage
Date: 2004-11-4
*/

insert into SystemMessage(sms_id,sms_lan)
	values('CND003','Big5')

insert into SystemMessage(sms_id,sms_lan)
	values('CND003','GB2312')

insert into SystemMessage(sms_id,sms_lan)
	values('CND003','ISO-8859-1')

insert into SystemMessage(sms_id,sms_lan)
	values('CND004','Big5')

insert into SystemMessage(sms_id,sms_lan)
	values('CND004','GB2312')

insert into SystemMessage(sms_id,sms_lan)
	values('CND004','ISO-8859-1')
insert into SystemMessage(sms_id,sms_lan)
	values('CND005','Big5')

insert into SystemMessage(sms_id,sms_lan)
	values('CND005','GB2312')

insert into SystemMessage(sms_id,sms_lan)
	values('CND005','ISO-8859-1')
insert into SystemMessage(sms_id,sms_lan)
	values('CND006','Big5')

insert into SystemMessage(sms_id,sms_lan)
	values('CND006','GB2312')

insert into SystemMessage(sms_id,sms_lan)
	values('CND006','ISO-8859-1')
insert into SystemMessage(sms_id,sms_lan)
	values('CND007','Big5')

insert into SystemMessage(sms_id,sms_lan)
	values('CND007','GB2312')

insert into SystemMessage(sms_id,sms_lan)
	values('CND007','ISO-8859-1')
insert into SystemMessage(sms_id,sms_lan)
	values('CND008','Big5')

insert into SystemMessage(sms_id,sms_lan)
	values('CND008','GB2312')

insert into SystemMessage(sms_id,sms_lan)
	values('CND008','ISO-8859-1')
insert into SystemMessage(sms_id,sms_lan)
	values('CND009','Big5')

insert into SystemMessage(sms_id,sms_lan)
	values('CND009','GB2312')

insert into SystemMessage(sms_id,sms_lan)
	values('CND009','ISO-8859-1')
insert into SystemMessage(sms_id,sms_lan)
	values('CND010','Big5')

insert into SystemMessage(sms_id,sms_lan)
	values('CND010','GB2312')

insert into SystemMessage(sms_id,sms_lan)
	values('CND010','ISO-8859-1')

call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_cnd003_iso-8859-1.txt SystemMessage sms_desc "sms_id = 'CND003' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_cnd004_iso-8859-1.txt SystemMessage sms_desc "sms_id = 'CND004' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_cnd005_iso-8859-1.txt SystemMessage sms_desc "sms_id = 'CND005' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_cnd006_iso-8859-1.txt SystemMessage sms_desc "sms_id = 'CND006' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_cnd007_iso-8859-1.txt SystemMessage sms_desc "sms_id = 'CND007' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_cnd008_iso-8859-1.txt SystemMessage sms_desc "sms_id = 'CND008' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_cnd009_iso-8859-1.txt SystemMessage sms_desc "sms_id = 'CND009' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_cnd010_iso-8859-1.txt SystemMessage sms_desc "sms_id = 'CND010' and sms_lan = 'ISO-8859-1'"

call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_cnd003_big5.txt SystemMessage sms_desc "sms_id = 'CND003' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_cnd004_big5.txt SystemMessage sms_desc "sms_id = 'CND004' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_cnd005_big5.txt SystemMessage sms_desc "sms_id = 'CND005' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_cnd006_big5.txt SystemMessage sms_desc "sms_id = 'CND006' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_cnd007_big5.txt SystemMessage sms_desc "sms_id = 'CND007' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_cnd008_big5.txt SystemMessage sms_desc "sms_id = 'CND008' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_cnd009_big5.txt SystemMessage sms_desc "sms_id = 'CND009' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_cnd010_big5.txt SystemMessage sms_desc "sms_id = 'CND010' and sms_lan = 'Big5'"


call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_cnd003_gb2312.txt SystemMessage sms_desc "sms_id = 'CND003' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_cnd004_gb2312.txt SystemMessage sms_desc "sms_id = 'CND004' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_cnd005_gb2312.txt SystemMessage sms_desc "sms_id = 'CND005' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_cnd006_gb2312.txt SystemMessage sms_desc "sms_id = 'CND006' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_cnd007_gb2312.txt SystemMessage sms_desc "sms_id = 'CND007' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_cnd008_gb2312.txt SystemMessage sms_desc "sms_id = 'CND008' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_cnd009_gb2312.txt SystemMessage sms_desc "sms_id = 'CND009' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_cnd010_gb2312.txt SystemMessage sms_desc "sms_id = 'CND010' and sms_lan = 'GB2312'"

/*
Author : Donald Liu
Desc:  new message for sole administrator of trainingCenter.
Date: 2004-11-05
*/
insert into systemmessage (sms_id,sms_lan)  values ('USR028','ISO-8859-1')
insert into systemmessage (sms_id,sms_lan)  values ('USR028','Big5')
insert into systemmessage (sms_id,sms_lan)  values ('USR028','GB2312')

call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_usr028_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'USR028' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_usr028_gb2312.txt	SystemMessage sms_desc "sms_id = 'USR028' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_usr028_big5.txt	SystemMessage sms_desc "sms_id = 'USR028' and sms_lan ='Big5'"

/*
Author : Dennis Yip
Desc:  update item template
Date: 2004-11-05
*/
call %UPDXMLCMD% xml\aeTemplateView\tvw_item_classroom_run_detail.xml	aeTemplateview tvw_xml "tvw_id = 'DETAIL_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Classroom Run ItemTemplate')"
call %UPDXMLCMD% xml\aeTemplateView\tvw_item_classroom_run_cancelled.xml	aeTemplateview tvw_xml "tvw_id = 'CANCELLED_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Classroom Run ItemTemplate')"

/*
Author : Dennis Yip
Desc:  update homepage function name
Date: 2004-11-08
*/
call %UPDXMLCMD% xml\acFunction\ftn_code_data_main.xml	acFunction ftn_xml "ftn_ext_id = 'CODE_DATA_MAIN'"
call %UPDXMLCMD% xml\acFunction\ftn_lrn_blueprint_admin.xml	acFunction ftn_xml "ftn_ext_id = 'LRN_BLUEPRINT_ADMIN'"
call %UPDXMLCMD% xml\acFunction\ftn_lrn_blueprint_view.xml	acFunction ftn_xml "ftn_ext_id = 'LRN_BLUEPRINT_VIEW'"
call %UPDXMLCMD% xml\acFunction\ftn_instr_main.xml 	acFunction ftn_xml "ftn_ext_id='INSTR_MAIN'"

/*
Author : Christ Qiu
Desc:  update acFunction XML for CODE_DATA_MAIN
Date: 2004-11-22
*/
call %UPDXMLCMD% xml\acFunction\ftn_code_data_main.xml acFunction ftn_xml "ftn_ext_id='CODE_DATA_MAIN'"

/*
Author : Dennis Yip
Desc:  update workflow template to cancel application after confirmation
Date: 2004-11-23
*/
call %UPDXMLCMD% xml\aeTemplate\tpl_workflow_multilevel_en_us.xml	aeTemplate tpl_xml "tpl_title = 'Multi-level-approval Workflow Template'"

/*
Author : Dennis Yip
Desc:  add "order" column for CourseMeasurement
Date: 2004-11-23
*/
alter table CourseMeasurement add cmt_order int null
GO

declare @ccr_id int
declare @prev_ccr_id int
declare @cmt_id int
declare @order int

declare cmt_cursor cursor for
select cmt_ccr_id, cmt_id from courseMeasurement
where cmt_is_contri_by_score = 1
order by cmt_ccr_id, cmt_create_timestamp

set @ccr_id = 0
set @prev_ccr_id = 0
set @cmt_id = 0
set @order = 0

open cmt_cursor

fetch next from cmt_cursor into @ccr_id, @cmt_id

while @@fetch_status = 0
begin
	if(@ccr_id = @prev_ccr_id)
	begin
		set @order = @order + 1
	end
	else
	begin
		set @order = 1
	end
	update courseMeasurement set cmt_order = @order where cmt_id = @cmt_id
	set @prev_ccr_id = @ccr_id
	fetch next from cmt_cursor into @ccr_id, @cmt_id
end
close cmt_cursor
deallocate cmt_cursor

/**
Author: Kim
Desc: Change the foreign key relation "Resources" => "Module" to "Resources" => "Resources"
Date: 2004-11-17
*/
ALTER TABLE [dbo].[Resources] DROP
	CONSTRAINT FK_Resources_Module
GO

ALTER TABLE [dbo].[Resources] ADD
	CONSTRAINT [FK_Resources_Module] FOREIGN KEY
	(
		[res_mod_res_id_test]
	) REFERENCES [dbo].[Resources] (
		[res_id]
	)
GO


/**
Author: Kim
Desc: Add a new table "QueContainerSpec" for Dynamic Question Container
Date: 2004-11-17
*/
CREATE TABLE [dbo].[QueContainerSpec] (
	[qcs_id] [int] IDENTITY (1, 1) NOT NULL ,
	[qcs_res_id] [int] NOT NULL ,
	[qcs_obj_id] [int] NULL ,
	[qcs_type] [nvarchar] (50) NULL ,
	[qcs_score] [int] NOT NULL ,
	[qcs_difficulty] [int] NULL ,
	[qcs_privilege] [nvarchar] (50) NULL ,
	[qcs_duration] [decimal] NULL ,
	[qcs_qcount] [int] NOT NULL ,
	[qcs_create_timestamp] [datetime] NOT NULL,
	[qcs_create_usr_id] [nvarchar] (50) NOT NULL,
	[qcs_update_timestamp] [datetime] NOT NULL,
	[qcs_update_usr_id] [nvarchar] (50) NOT NULL
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[QueContainerSpec] ADD
	CONSTRAINT [FK_QueContainerSpec_Resources] FOREIGN KEY
	(
		[qcs_res_id]
	) REFERENCES [dbo].[Resources] (
		[res_id]
	)


/**
Author: Kim
Desc: Change ModuleSpec's column type to match QueContainerSpec
Date: 2004-11-17
*/
ALTER TABLE ModuleSpec ALTER COLUMN msp_type nvarchar(50)
ALTER TABLE ModuleSpec ALTER COLUMN msp_privilege nvarchar(50)
ALTER TABLE ModuleSpec ALTER COLUMN msp_score int


/**
Author: Kim
Desc: Add the new table QueContainer
Date: 2004-11-17
*/
CREATE TABLE [dbo].[QueContainer] (
	[qct_res_id] [int] NOT NULL ,
	[qct_select_logic] [nvarchar] (50) NULL ,
	[qct_allow_shuffle_ind] [int] NULL
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[QueContainer] WITH NOCHECK ADD
	CONSTRAINT [PK_QueContainer] PRIMARY KEY  NONCLUSTERED
	(
		[qct_res_id]
	) WITH  FILLFACTOR = 90  ON [PRIMARY]

/**
Author: Kim
Desc: System Messages for Test
Date: 2004-11-17
*/
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('CRT001', 'ISO-8859-1','Criterion has been added successfully.')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('CRT001', 'Big5','Criterion has been added successfully.')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('CRT001', 'GB2312','Criterion has been added successfully.')

insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('CRT002', 'ISO-8859-1','Criterion has been updated successfully.')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('CRT002', 'Big5','Criterion has been updated successfully.')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('CRT002', 'GB2312','Criterion has been updated successfully.')

insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('CRT003', 'ISO-8859-1','Criterion has been removed successfully.')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('CRT003', 'Big5','Criterion has been removed successfully.')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('CRT003', 'GB2312','Criterion has been removed successfully.')

insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('ASM004', 'ISO-8859-1','There are no selection criteria defined for the test.')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('ASM004', 'Big5','There are no selection criteria defined for the test.')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('ASM004', 'GB2312','There are no selection criteria defined for the test.')

insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('ASM005', 'ISO-8859-1','Validation succeeded.  The test contains enough number of questions.')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('ASM005', 'Big5','Validation succeeded.  The test contains enough number of questions.')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('ASM005', 'GB2312','Validation succeeded.  The test contains enough number of questions.')

insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('ASM006', 'ISO-8859-1','Validation failed.  The test does not contain enough number of questions.')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('ASM006', 'Big5','Validation failed.  The test does not contain enough number of questions.')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('ASM006', 'GB2312','Validation failed.  The test does not contain enough number of questions.')

Insert Into SystemMessage (sms_id, sms_lan, sms_desc) Values ('ASM007', 'Big5', 'The folder has been removed successfully.' )
Insert Into SystemMessage (sms_id, sms_lan, sms_desc) Values ('ASM007', 'GB2312', 'The folder has been removed successfully.' )
Insert Into SystemMessage (sms_id, sms_lan, sms_desc) Values ('ASM007', 'ISO-8859-1', 'The folder has been removed successfully.' )

Insert Into SystemMessage (sms_id, sms_lan, sms_desc) Values ('ASM008', 'Big5', 'There are no questions defined for the test.' )
Insert Into SystemMessage (sms_id, sms_lan, sms_desc) Values ('ASM008', 'GB2312', 'There are no questions defined for the test.' )
Insert Into SystemMessage (sms_id, sms_lan, sms_desc) Values ('ASM008', 'ISO-8859-1', 'There are no questions defined for the test.' )

Insert Into SystemMessage (sms_id, sms_lan, sms_desc) Values ('ASM009', 'Big5', 'Test has been added successfully.' )
Insert Into SystemMessage (sms_id, sms_lan, sms_desc) Values ('ASM009', 'GB2312', 'Test has been added successfully.' )
Insert Into SystemMessage (sms_id, sms_lan, sms_desc) Values ('ASM009', 'ISO-8859-1', 'Test has been added successfully.' )

Insert Into SystemMessage (sms_id, sms_lan, sms_desc) Values ('ASM010', 'Big5', 'Test has been updated successfully.' )
Insert Into SystemMessage (sms_id, sms_lan, sms_desc) Values ('ASM010', 'GB2312', 'Test has been updated successfully.' )
Insert Into SystemMessage (sms_id, sms_lan, sms_desc) Values ('ASM010', 'ISO-8859-1', 'Test has been updated successfully.' )

insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('ASM011', 'ISO-8859-1','Test has been removed successfully.')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('ASM011', 'Big5','Test has been removed successfully.')
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('ASM011', 'GB2312','Test has been removed successfully.')

Insert Into SystemMessage (sms_id, sms_lan, sms_desc) Values ('MOD012', 'Big5', 'The record is added successfully. <BR/>However, since the test does not have any question/criterion defined, it cannot be turned to Online.' )
Insert Into SystemMessage (sms_id, sms_lan, sms_desc) Values ('MOD012', 'GB2312', 'The record is added successfully. <BR/>However, since the test does not have any question/criterion defined, it cannot be turned to Online.' )
Insert Into SystemMessage (sms_id, sms_lan, sms_desc) Values ('MOD012', 'ISO-8859-1', 'The record is added successfully. <BR/>However, since the test does not have any question/criterion defined, it cannot be turned to Online.' )

call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_crt001_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'CRT001' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_crt001_gb2312.txt	SystemMessage sms_desc "sms_id = 'CRT001' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_crt001_big5.txt	SystemMessage sms_desc "sms_id = 'CRT001' and sms_lan ='Big5'"

call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_crt002_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'CRT002' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_crt002_gb2312.txt	SystemMessage sms_desc "sms_id = 'CRT002' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_crt002_big5.txt	SystemMessage sms_desc "sms_id = 'CRT002' and sms_lan ='Big5'"

call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_crt003_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'CRT003' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_crt003_gb2312.txt	SystemMessage sms_desc "sms_id = 'CRT003' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_crt003_big5.txt	SystemMessage sms_desc "sms_id = 'CRT003' and sms_lan ='Big5'"

call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_asm004_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'ASM004' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_asm004_gb2312.txt	SystemMessage sms_desc "sms_id = 'ASM004' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_asm004_big5.txt	SystemMessage sms_desc "sms_id = 'ASM004' and sms_lan ='Big5'"

call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_asm005_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'ASM005' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_asm005_gb2312.txt	SystemMessage sms_desc "sms_id = 'ASM005' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_asm005_big5.txt	SystemMessage sms_desc "sms_id = 'ASM005' and sms_lan ='Big5'"

call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_asm006_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'ASM006' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_asm006_gb2312.txt	SystemMessage sms_desc "sms_id = 'ASM006' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_asm006_big5.txt	SystemMessage sms_desc "sms_id = 'ASM006' and sms_lan ='Big5'"

call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_asm007_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'ASM007' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_asm007_gb2312.txt	SystemMessage sms_desc "sms_id = 'ASM007' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_asm007_big5.txt	SystemMessage sms_desc "sms_id = 'ASM007' and sms_lan ='Big5'"

call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_asm008_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'ASM008' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_asm008_gb2312.txt	SystemMessage sms_desc "sms_id = 'ASM008' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_asm008_big5.txt	SystemMessage sms_desc "sms_id = 'ASM008' and sms_lan ='Big5'"

call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_asm009_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'ASM009' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_asm009_gb2312.txt	SystemMessage sms_desc "sms_id = 'ASM009' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_asm009_big5.txt	SystemMessage sms_desc "sms_id = 'ASM009' and sms_lan ='Big5'"

call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_asm010_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'ASM010' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_asm010_gb2312.txt	SystemMessage sms_desc "sms_id = 'ASM010' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_asm010_big5.txt	SystemMessage sms_desc "sms_id = 'ASM010' and sms_lan ='Big5'"

call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_asm011_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'ASM011' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_asm011_gb2312.txt	SystemMessage sms_desc "sms_id = 'ASM011' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_asm011_big5.txt	SystemMessage sms_desc "sms_id = 'ASM011' and sms_lan ='Big5'"

call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_mod012_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'MOD012' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_mod012_gb2312.txt	SystemMessage sms_desc "sms_id = 'MOD012' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_mod012_big5.txt	SystemMessage sms_desc "sms_id = 'MOD012' and sms_lan ='Big5'"

call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_msp001_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'MSP001' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_msp001_gb2312.txt	SystemMessage sms_desc "sms_id = 'MSP001' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_msp001_big5.txt	SystemMessage sms_desc "sms_id = 'MSP001' and sms_lan ='Big5'"


/**
Author: Dennis Yip
Desc: assgin system administrator function to edit all items
Date: 2004-12-07
*/
insert into acRoleFunction select rol_id, ftn_id, getdate() from acRole, acFunction where rol_ste_uid = 'SADM' and ftn_ext_id = 'ITM_MGT_IN_ORG'

/**
Author: Dennis Yip
Desc: add error message to warning:this item is other's prerequisite item.
Date: 2004-12-14
*/
insert into systemmessage (sms_id,sms_lan) values ('AEIT25','ISO-8859-1')
insert into systemmessage (sms_id,sms_lan) values ('AEIT25','Big5')
insert into systemmessage (sms_id,sms_lan) values ('AEIT25','GB2312')

call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_aeit25_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'AEIT25' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_aeit25_gb2312.txt	SystemMessage sms_desc "sms_id = 'AEIT25' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_aeit25_big5.txt	SystemMessage sms_desc "sms_id = 'AEIT25' and sms_lan ='Big5'"

/**
Author: Kim
Desc: add primary key for the table "QueContainerSpec"
Date: 2004-12-29
*/
ALTER TABLE [dbo].[QueContainerSpec] WITH NOCHECK ADD
	CONSTRAINT [PK_QueContainerSpec] PRIMARY KEY  CLUSTERED
	(
		[qcs_id]
	)  ON [PRIMARY]
GO

/*
Author : Christ Qiu
Desc:  update SystemMessage for 'CND003'
Date: 2005-01-07
*/
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_cnd003_big5.txt	SystemMessage sms_desc "sms_id = 'CND003' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_cnd003_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'CND003' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_cnd003_gb2312.txt	SystemMessage sms_desc "sms_id = 'CND003' and sms_lan = 'GB2312'"

/**
Author: Donlad Liu
Desc: add error message to warning:'Prerequisite records cannot be updated. The Learning Solution cannot be the the prerequisite of itself'
Date: 2005-01-07
*/
insert into systemmessage (sms_id,sms_lan) values ('AEIT26','ISO-8859-1')
insert into systemmessage (sms_id,sms_lan) values ('AEIT26','Big5')
insert into systemmessage (sms_id,sms_lan) values ('AEIT26','GB2312')

call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_aeit26_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'AEIT26' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_aeit26_gb2312.txt	SystemMessage sms_desc "sms_id = 'AEIT26' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_aeit26_big5.txt	SystemMessage sms_desc "sms_id = 'AEIT26' and sms_lan ='Big5'"

/*
Author : Dennis Yip
Desc:  update Big5 SystemMessage for 'CND003'
Date: 2005-01-12
*/
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_cnd003_big5.txt	SystemMessage sms_desc "sms_id = 'CND003' and sms_lan = 'Big5'"


/*
Author : Christ Qiu
Desc:  update SystemMessage for 'CND003'
Date: 2005-01-12
*/
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_cnd003_big5.txt	SystemMessage sms_desc "sms_id = 'CND003' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_cnd003_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'CND003' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_cnd003_gb2312.txt	SystemMessage sms_desc "sms_id = 'CND003' and sms_lan = 'GB2312'"

/*
Author : Christ Qiu
Desc:  update SystemMessage :'CND003'  for big5
Date: 2005-01-17
*/
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_cnd003_big5.txt	SystemMessage sms_desc "sms_id = 'CND003' and sms_lan = 'Big5'"

/*
Author: Terry Luo
Desc: update the record in Completion Criteria where an online module has two criterias in online module and calculated item.
		set the record deleted for online module.
Date: 2005-03-09
*/
update CourseMeasurement set cmt_delete_timestamp=getdate() where cmt_id in
(
    select cmt_id from CourseMeasurement, CourseModuleCriteria cmr1
    where
     cmt_cmr_id = cmr1.cmr_id and cmt_is_contri_by_score = 0
     and cmt_delete_timestamp is null and cmr1.cmr_del_timestamp is null
     and exists
        (
            select * from CourseMeasurement, CourseModuleCriteria
            where
                cmt_cmr_id = cmr_id and cmt_is_contri_by_score = 1
                and cmt_delete_timestamp is null and cmr_del_timestamp is null
                and cmr_res_id = cmr1.cmr_res_id and cmr_ccr_id = cmr1.cmr_ccr_id
        )
)

update CourseModuleCriteria set cmr_del_timestamp=getdate()
where exists
(
    select * from CourseMeasurement where cmt_cmr_id = cmr_id
    and cmt_delete_timestamp is not null
)
and cmr_del_timestamp is null

/*
Author: Dixson Zhu
Desc: insert a new report template for Course Evaluation Report By Question
Date: 2005-05-18
*/
insert ReportTemplate (
rte_title_xml,
rte_type, rte_get_xsl, rte_exe_xsl, rte_dl_xsl,
rte_meta_data_url, rte_seq_no, rte_owner_ent_id,
rte_create_usr_id, rte_create_timestamp, rte_upd_usr_id, rte_upd_timestamp
) values (
'<title></title>',
'SURVEY_QUE_GRP', 'rpt_svy_que_srh.xsl', 'rpt_svy_que_grp_res.xsl', 'rpt_dl_survey_que_xls.xsl',
NULL, 7, 1,
's1u3', '2005-05-11', 's1u3', '2005-05-11'
)

insert into acReportTemplate (
ac_rte_id, ac_rte_ent_id, ac_rte_rol_ext_id, ac_rte_ftn_ext_id, ac_rte_owner_ind,
ac_rte_create_usr_id, ac_rte_create_timestamp
)
select rte_id, NULL, 'TADM_1', 'RTE_READ', 0,
's1u3', '2005-05-11' from ReportTemplate where rte_type = 'SURVEY_QUE_GRP'

call %UPDXMLCMD% xml\ReportTemplate\rte_title_survey_que_grp.xml	ReportTemplate rte_title_xml "rte_type = 'SURVEY_QUE_GRP'"

/*
author: Dennis Yip
date: 2005-07-28
desc: create foreign key relation between tcTrainingCenter and aeItem
*/
ALTER TABLE [dbo].[aeItem] ADD
	CONSTRAINT [FK_aeItem_tcTrainingCenter] FOREIGN KEY
	(
		[itm_tcr_id]
	) REFERENCES [dbo].[tcTrainingCenter] (
		[tcr_id]
	)
GO

/*
author: Dennis Yip
date: 2005-07-28
desc: remove incorrect ResourceContent records that questions in the assessment module will be duplicated when it is picked into a learning module.
*/
delete resourcecontent
where exists (	select * from resources
		where res_id = rcn_res_id_content
		and res_mod_res_id_test is not null
		and res_type = 'QUE'
		and res_mod_res_id_test <> rcn_res_id)

/*
Author : Kim
Desc:  update SystemMessage for 'USR015'
Date: 2005-08-05
*/
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_usr015_big5.txt	SystemMessage sms_desc "sms_id = 'USR015' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_usr015_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'USR015' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_usr015_gb2312.txt	SystemMessage sms_desc "sms_id = 'USR015' and sms_lan = 'GB2312'"

/*
Author : Dennis Yip
Desc: show training center information in class cancelled view
Date: 2005-08-08
*/
update aeTemplateView set tvw_tcr_ind = 1
where tvw_tpl_id = (select tpl_id from aeTemplate where tpl_title = 'Classroom Run ItemTemplate')
and tvw_id = 'CANCELLED_VIEW'

/**
Auth: Dennis
Date: 2005-08-15
Desc: Update System Message
*/
update SystemMessage set sms_id = 'SCO001' where sms_id = 'SCO01'
update SystemMessage set sms_id = 'SCO002' where sms_id = 'SCO02'

/*
Author : Dixson
Desc: add back missing system message
Date: 2005-11-25
*/
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('OBJ011', 'ISO-8859-1', null)
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('OBJ011', 'GB2312', null)
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('OBJ011', 'Big5', null)

/*
Author : Dixson
Desc: update system text
Date: 2005-11-25
*/
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_tc001_gb2312.txt	SystemMessage sms_desc "sms_id = 'TC001' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_usr027_gb2312.txt	SystemMessage sms_desc "sms_id = 'USR027' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_usr028_gb2312.txt	SystemMessage sms_desc "sms_id = 'USR028' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_obj010_gb2312.txt	SystemMessage sms_desc "sms_id = 'OBJ010' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_aeit15_gb2312.txt	SystemMessage sms_desc "sms_id = 'AEIT15' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_mod011_gb2312.txt	SystemMessage sms_desc "sms_id = 'MOD011' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_cnd005_gb2312.txt	SystemMessage sms_desc "sms_id = 'CND005' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_aeit25_gb2312.txt	SystemMessage sms_desc "sms_id = 'AEIT25' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_obj011_gb2312.txt	SystemMessage sms_desc "sms_id = 'OBJ011' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_aeqm10_gb2312.txt	SystemMessage sms_desc "sms_id = 'AEQM10' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_aeit24_gb2312.txt	SystemMessage sms_desc "sms_id = 'AEIT24' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_ils008_gb2312.txt	SystemMessage sms_desc "sms_id = 'ILS008' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_gen001_gb2312.txt	SystemMessage sms_desc "sms_id = 'GEN001' and sms_lan = 'GB2312'"

call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_tc001_big5.txt	SystemMessage sms_desc "sms_id = 'TC001' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_usr027_big5.txt 	SystemMessage sms_desc "sms_id = 'USR027' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_usr028_big5.txt	SystemMessage sms_desc "sms_id = 'USR028' and sms_lan = 'BIG5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_obj010_big5.txt	SystemMessage sms_desc "sms_id = 'OBJ010' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_aeit15_big5.txt	SystemMessage sms_desc "sms_id = 'AEIT15' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_mod011_big5.txt	SystemMessage sms_desc "sms_id = 'MOD011' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_cnd005_big5.txt	SystemMessage sms_desc "sms_id = 'CND005' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_aeit25_big5.txt	SystemMessage sms_desc "sms_id = 'AEIT25' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_obj011_big5.txt	SystemMessage sms_desc "sms_id = 'OBJ011' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_aeqm10_big5.txt	SystemMessage sms_desc "sms_id = 'AEQM10' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_aeit24_big5.txt	SystemMessage sms_desc "sms_id = 'AEIT24' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_ils008_big5.txt	SystemMessage sms_desc "sms_id = 'ILS008' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_gen001_big5.txt	SystemMessage sms_desc "sms_id = 'GEN001' and sms_lan = 'Big5'"

call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_tc001_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'TC001' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_gen005_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'GEN005' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_usr027_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'USR027' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_usr028_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'USR028' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_aeit15_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'AEIT15' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_mod011_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'MOD011' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_cnd005_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'CND005' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_aeit25_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'AEIT25' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_obj011_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'OBJ011' and sms_lan = 'ISO-8859-1'"

call %UPDXMLCMD% xml\acFunction\ftn_lrn_calendar.xml	acFunction ftn_xml "ftn_ext_id = 'LRN_CALENDAR'"
call %UPDXMLCMD% xml\acFunction\ftn_code_data_main.xml	acFunction ftn_xml "ftn_ext_id = 'CODE_DATA_MAIN'"
call %UPDXMLCMD% xml\acFunction\ftn_trainingcenter_mgt.xml	acFunction ftn_xml "ftn_ext_id = 'TC_MAIN'"
call %UPDXMLCMD% xml\acFunction\ftn_instr_main.xml 	acFunction ftn_xml "ftn_ext_id = 'INSTR_MAIN'"

/*
Author : Dixson
Desc: add back missing facility management function to system administrator
Date: 2005-11-25
*/
insert into acHomePage (
ac_hom_ent_id, ac_hom_rol_ext_id, ac_hom_ftn_ext_id, ac_hom_create_usr_id, ac_hom_create_timestamp
) values (
null, 'ADM_1', 'BOOK_MAIN', 's1u3', '2005-11-25'
)
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_creation_timestamp)
select rol_id, ftn_id, '2005-11-25' from acRole, acFunction
where rol_ext_id like 'ADM_%' and ftn_ext_id in ('BOOK_MAIN','RSV_MGT_OWN')

/*
Author : Dixson
Desc: add user prof update function to inistructor role
Date: 2005-11-25
*/
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_creation_timestamp)
select rol_id, ftn_id, '2005-11-25' from acRole, acFunction
where rol_ext_id like 'INSTR_%' and ftn_ext_id = 'USR_MGT_OWN'

/*
Author : Dixson
Desc: add forum access function to instructor role
Date: 2005-11-25
*/
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_creation_timestamp)
select rol_id, ftn_id, '2005-11-25' from acRole, acFunction
where rol_ext_id like 'INSTR_%' and ftn_ext_id = 'MOD_PUB_WRITE'


/*
Author : randy
Desc: add table to do the Module-level prerequisite
Date: 2005-11-29
*/

CREATE TABLE [dbo].[ResourceRequirement] (
	rrq_res_id int NOT NULL ,
	rrq_req_res_id int NOT NULL ,
	rrq_status nvarchar(50) NOT NULL,
	rrq_create_timestamp  datetime NOT NULL,
	rrq_create_usr_id nvarchar(20) NOT NULL,

	CONSTRAINT PK_ResourceRequirement_ID PRIMARY KEY ( rrq_res_id,rrq_req_res_id ),
	CONSTRAINT FK_ResourceRequirement_1 FOREIGN KEY ( rrq_res_id ) REFERENCES Resources ( res_id ),
	CONSTRAINT FK_ResourceRequirement_2 FOREIGN KEY ( rrq_req_res_id ) REFERENCES Resources ( res_id )
)

insert into SystemMessage values('MOD013','ISO-8859-1',null)
insert into SystemMessage values('MOD013','GB2312',null)
insert into SystemMessage values('MOD013','Big5',null)

call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_mod013_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'MOD013' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_mod013_gb2312.txt	SystemMessage sms_desc "sms_id = 'MOD013' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_mod013_big5.txt	SystemMessage sms_desc "sms_id = 'MOD013' and sms_lan ='Big5'"

/*
Author : Amanda
Desc: add itm_content_def to aeItem
Date: 2005-11-29
*/
ALTER TABLE aeItem ADD
	itm_content_def nvarchar(50) NULL
GO

/*
Author : Amanda
Desc: make both course-level and class-level item have online content
Date: 2005-12-01
*/
update aeItemType set ity_qdb_ind = 1
where ity_id = 'CLASSROOM'


/*
Author : randy
Desc: add table to do Scoring Item and Completion Criteria
Date: 2005-12-7
*/
alter table CourseCriteria add ccr_ccr_id_parent int null
ALTER TABLE [dbo].[CourseCriteria] ADD
	CONSTRAINT [FK_CourseCriteria_CourseCriteria] FOREIGN KEY
	(
		[ccr_ccr_id_parent]
	) REFERENCES [dbo].[CourseCriteria] (
		[ccr_id]
	)


alter table CourseMeasurement add cmt_cmt_id_parent int null
ALTER TABLE [dbo].[CourseMeasurement] WITH NOCHECK ADD
	CONSTRAINT [PK_CourseMeasurement] PRIMARY KEY  NONCLUSTERED
	( [cmt_id] )  ON [PRIMARY]

ALTER TABLE [dbo].[CourseMeasurement] ADD
CONSTRAINT [FK_CourseMeasurement_CourseMeasurement] FOREIGN KEY
	(
		[cmt_cmt_id_parent]
	) REFERENCES [dbo].[CourseMeasurement] (
		[cmt_id]
	)

alter table CourseModuleCriteria add cmr_cmr_id_parent int null
ALTER TABLE [dbo].[CourseModuleCriteria] ADD
	CONSTRAINT [FK_CourseModuleCriteria_CourseModuleCriteria] FOREIGN KEY
	(
		[cmr_cmr_id_parent]
	) REFERENCES [dbo].[CourseModuleCriteria] (
		[cmr_id]
	)

/*
Author : Dixson
Desc: add system message for DSC question
Date: 2005-11-25
*/
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('DSC001', 'Big5', null)
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('DSC001', 'GB2312', null)
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('DSC001', 'ISO-8859-1', null)
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('ASM012', 'Big5', null)
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('ASM012', 'GB2312', null)
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('ASM012', 'ISO-8859-1', null)

call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_dsc001_big5.txt	SystemMessage sms_desc "sms_id = 'DSC001' and sms_lan ='Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_dsc001_gb2312.txt	SystemMessage sms_desc "sms_id = 'DSC001' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_dsc001_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'DSC001' and sms_lan = 'ISO-8859-1'"

call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_asm012_big5.txt	SystemMessage sms_desc "sms_id = 'ASM012' and sms_lan ='Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_asm012_gb2312.txt	SystemMessage sms_desc "sms_id = 'ASM012' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_asm012_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'ASM012' and sms_lan = 'ISO-8859-1'"

/*
Author : Dixson
Desc: add usr_source in RegUser,and update all user data with ent_syn_ind=1 including muti org.
Date: 2005-11-25
*/
alter table RegUser add usr_source nvarchar(50) NULL
GO
update RegUser set usr_source = 'wizBank' where usr_ent_id in(select ent_id from Entity where ent_syn_ind = 1)

/*
Author : Dixson
Desc: add sso link query function and add it to tadm role.
Date: 2005-11-25
*/
insert acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('SSO_LINK_QUERY', 'FUNCTION', 'HOMEPAGE', getdate(), null)

insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_creation_timestamp)
select rol_id, ftn_id, '2005-11-25' from acRole, acFunction
where rol_ext_id like 'TADM%' and ftn_ext_id = 'SSO_LINK_QUERY'

call %UPDXMLCMD% xml\acFunction\ftn_sso_link.xml	acFunction ftn_xml "ftn_ext_id = 'SSO_LINK_QUERY'"

insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('MOD014', 'GB2312', null)
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('MOD014', 'ISO-8859-1', null)
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('MOD014', 'Big5', null)

insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('MOD015', 'GB2312', null)
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('MOD015', 'ISO-8859-1', null)
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('MOD015', 'Big5', null)

insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('MOD016', 'GB2312', null)
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('MOD016', 'ISO-8859-1', null)
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('MOD016', 'Big5', null)

insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('MOD017', 'ISO-8859-1', null)
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('MOD017', 'Big5', null)
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('MOD017', 'GB2312', null)

insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('USR029', 'ISO-8859-1', null)
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('USR029', 'Big5', null)
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('USR029', 'GB2312', null)

insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('MSP002', 'GB2312', null)
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('MSP002', 'ISO-8859-1', null)
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('MSP002', 'Big5', null)

insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('MSP003', 'GB2312', null)
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('MSP003', 'ISO-8859-1', null)
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('MSP003', 'Big5', null)

insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('AECA04', 'GB2312', null)
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('AECA04', 'ISO-8859-1', null)
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('AECA04', 'Big5', null)

insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('AEIT27', 'GB2312', null)
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('AEIT27', 'ISO-8859-1', null)
insert into SystemMessage (sms_id, sms_lan, sms_desc) values ('AEIT27', 'Big5', null)

call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_aeca04_big5.txt	SystemMessage sms_desc "sms_id = 'AECA04' and sms_lan ='Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_aeit27_big5.txt	SystemMessage sms_desc "sms_id = 'AEIT27' and sms_lan ='Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_mod014_big5.txt	SystemMessage sms_desc "sms_id = 'MOD014' and sms_lan ='Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_mod015_big5.txt	SystemMessage sms_desc "sms_id = 'MOD015' and sms_lan ='Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_mod016_big5.txt	SystemMessage sms_desc "sms_id = 'MOD016' and sms_lan ='Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_mod017_big5.txt	SystemMessage sms_desc "sms_id = 'MOD017' and sms_lan ='Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_usr029_big5.txt	SystemMessage sms_desc "sms_id = 'USR029' and sms_lan ='Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_msp002_big5.txt	SystemMessage sms_desc "sms_id = 'MSP002' and sms_lan ='Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_msp003_big5.txt	SystemMessage sms_desc "sms_id = 'MSP003' and sms_lan ='Big5'"

call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_aeca04_gb2312.txt	SystemMessage sms_desc "sms_id = 'AECA04' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_aeit27_gb2312.txt	SystemMessage sms_desc "sms_id = 'AEIT27' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_mod014_gb2312.txt	SystemMessage sms_desc "sms_id = 'MOD014' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_mod015_gb2312.txt	SystemMessage sms_desc "sms_id = 'MOD015' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_mod016_gb2312.txt	SystemMessage sms_desc "sms_id = 'MOD016' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_mod017_gb2312.txt	SystemMessage sms_desc "sms_id = 'MOD017' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_usr029_gb2312.txt	SystemMessage sms_desc "sms_id = 'USR029' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_msp002_gb2312.txt	SystemMessage sms_desc "sms_id = 'MSP002' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_msp003_gb2312.txt	SystemMessage sms_desc "sms_id = 'MSP003' and sms_lan = 'GB2312'"

call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_aeca04_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'AECA04' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_aeit27_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'AEIT27' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_mod014_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'MOD014' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_mod015_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'MOD015' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_mod016_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'MOD016' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_mod017_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'MOD017' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_usr029_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'USR029' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_msp002_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'MSP002' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_msp003_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'MSP003' and sms_lan = 'ISO-8859-1'"


/*
Author : Terry
Desc : new columns for item
Date : 2005-11-28
*/
ALTER TABLE aeItem ADD
	itm_enroll_type nvarchar(50) NULL,
	itm_send_enroll_email_ind int NULL,
	itm_quota_exceed_notify_timestamp datetime NULL,
	itm_access_type nvarchar(50) NULL


ALTER TABLE aeAppnActnHistory add aah_actn_type nvarchar(50) NULL

call %UPDXMLCMD% xml\aeTemplate\tpl_workflow_multilevel_en_us.xml	aeTemplate tpl_xml "tpl_title = 'Multi-level-approval Workflow Template'"
call %UPDXMLCMD% xml\aeTemplate\tpl_item_selfstudy.xml		aeTemplate tpl_xml "tpl_title = 'Self Study Item Template' and tpl_owner_ent_id =1"
call %UPDXMLCMD% xml\aeTemplateView\tvw_item_selfstudy_detail.xml	aeTemplateview tvw_xml "tvw_id = 'DETAIL_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Self Study Item Template' and tpl_owner_ent_id =1)"
call %UPDXMLCMD% xml\aeTemplateView\tvw_item_classroom_run_detail.xml	aeTemplateview tvw_xml "tvw_id = 'DETAIL_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Classroom Run ItemTemplate' and tpl_owner_ent_id =1)"
call %UPDXMLCMD% xml\aeTemplate\tpl_item_classroom_run.xml	aeTemplate tpl_xml "tpl_title = 'Classroom Run ItemTemplate' and tpl_owner_ent_id =1"


/*
Author : Terry
Desc : remove "cancel" function for all item types and existing items
Date : 2005-12-20
*/
update aeItemType set ity_can_cancel_ind = 0
where ity_id = 'CLASSROOM'

update aeItem set itm_can_cancel_ind = 0
where itm_type = 'CLASSROOM'

/*
Author: Terry
Desc: move accessible user field from classroom-class to classroom.
Date: 2006-1-6
*/
call %UPDXMLCMD% xml\aeTemplate\tpl_item_classroom.xml		aeTemplate tpl_xml "tpl_title = 'Classroom Item Template' and tpl_owner_ent_id =1"
call %UPDXMLCMD% xml\aeTemplateView\tvw_item_classroom_detail.xml	aeTemplateview tvw_xml "tvw_id = 'DETAIL_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Classroom Item Template' and tpl_owner_ent_id =1)"

call %UPDXMLCMD% xml\aeTemplate\tpl_item_classroom_run.xml	aeTemplate tpl_xml "tpl_title = 'Classroom Run ItemTemplate' and tpl_owner_ent_id =1"
call %UPDXMLCMD% xml\aeTemplateView\tvw_item_classroom_run_detail.xml	aeTemplateview tvw_xml "tvw_id = 'DETAIL_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Classroom Run ItemTemplate' and tpl_owner_ent_id =1)"

/*
Author : Amanda
Desc: Add a new column to indicate the parent module of the corresponding module.
Date: 2005-12-12
*/
ALTER TABLE Module ADD
	mod_mod_res_id_parent int NULL
GO

ALTER TABLE Module ADD CONSTRAINT
	FK_mod_mod_res_id_parent FOREIGN KEY
	(
	mod_mod_res_id_parent
	) REFERENCES Module
	(
	mod_res_id
	)
GO

CREATE
  INDEX [IX_mod_mod_res_id_parent] ON [dbo].[Module] ([mod_mod_res_id_parent])
GO

/*
Author : Amanda
Desc: a new option "Unspecified" is added for Assignment Due Date,
      need to set the previous default value of ass_due_datetime to null
      because if not set, it will be shown in the UI
Date: 2006-01-03
*/
update Assignment set ass_due_datetime = null where ass_due_date_day > 0


/*
Author : Randy
Desc: display the content button in the class level
Date: 2006-01-23
*/
update aeItem set itm_qdb_ind = 1 where itm_type = 'CLASSROOM'

/*
Author : Dixson
Desc: add a field "Completion settle-date extension" in item details
Date: 2006-01-24
*/
call %UPDXMLCMD% xml\aeTemplate\tpl_item_selfstudy.xml		aeTemplate tpl_xml
"tpl_title = 'Self Study Item Template'"
call %UPDXMLCMD% xml\aeTemplate\tpl_item_classroom_run.xml	aeTemplate tpl_xml "tpl_title = 'Classroom Run ItemTemplate'"
call %UPDXMLCMD% xml\aeTemplateView\tvw_item_selfstudy_detail.xml	aeTemplateview tvw_xml "tvw_id = 'DETAIL_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where
tpl_title = 'Self Study Item Template')"
call %UPDXMLCMD% xml\aeTemplateView\tvw_item_classroom_run_detail.xml	aeTemplateview tvw_xml "tvw_id = 'DETAIL_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Classroom Run ItemTemplate')"

ALTER TABLE aeItem ADD
	itm_mark_buffer_day int NULL
GO

/*
Author: Randy
Desc: for clp upload enrollment.
Date: 2006-2-22
*/
alter table IMSLog add ilg_target_id  nvarchar (50) null

/*
Author: Randy
Desc: enrollmentthread notification email when the thread  end .
Date: 2006-1-11
*/
insert into xslTemplate (xtp_type,xtp_subtype,xtp_channel_type,xtp_xsl,xtp_xml_url,xtp_mailmerge_ind)
values('ENROLLMENT_IMPORT_SUCCESS','HTML','SMTP','bp_notify_enrollment_import.xsl','http://host:port/servlet/aeAction?',0)

declare @xtp_id int
select @xtp_id = max(xtp_id) from xslTemplate

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 1, 'ent_ids')
insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 2, 'sender_id')
insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 3, 'cmd')
insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 4, 'cc_ent_ids')
insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 5, 'src_file')
insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 6, 'start_time')
insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 7, 'end_time')
insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 8, 'success_total')
insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
values (@xtp_id, 9, 'unsuccess_total')
GO

/*
Author: Randy
Desc: add message for Excel format file
Date: 2006-02-24
*/
insert into systemmessage (sms_id,sms_lan,sms_desc)
values('GEN009','Big5',null)
insert into systemmessage (sms_id,sms_lan,sms_desc)
values('GEN009','GB2312',null)
insert into systemmessage (sms_id,sms_lan,sms_desc)
values('GEN009','ISO-8859-1',null)
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_gen009_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'GEN009' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_gen009_GB2312.txt	SystemMessage sms_desc "sms_id = 'GEN009' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_gen009_big5.txt	SystemMessage sms_desc "sms_id = 'GEN009' and sms_lan = 'Big5'"

/*
Author : Joyce Jiang
Desc: New system message for target learner access item.
Date: 2006-02-20
*/
insert SystemMessage values ('AEIT28', 'ISO-8859-1', NULL)
insert SystemMessage values ('AEIT28', 'Big5', NULL)
insert SystemMessage values ('AEIT28', 'GB2312', NULL)
call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_aeit28_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'AEIT28' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_aeit28_gb2312.txt	SystemMessage sms_desc "sms_id = 'AEIT28' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_aeit28_big5.txt	SystemMessage sms_desc "sms_id = 'AEIT28' and sms_lan = 'Big5'"



/*
Author : Terry Luo
Desc: New mail template for quota exceed notification.
Date: 2006-02-22
*/
insert into xslTemplate
(xtp_type, xtp_subtype, xtp_channel_type, xtp_channel_api, xtp_xsl, xtp_xml_url, xtp_mailmerge_ind, xtp_title)
values
('QUOTA_EXCEED_NOTIFY', 'HTML', 'SMTP', null, 'quota_exceed_notify.xsl', 'http://host:port/servlet/aeAction?', 0, null)

declare @xtp_id int
select @xtp_id = max(xtp_id) from xslTemplate

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
	values (@xtp_id, 1, 'cmd')
insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
	values (@xtp_id, 2, 'itm_id')
insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
	values (@xtp_id, 3, 'appn_wait_count')
insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
	values (@xtp_id, 4, 'ent_ids')
insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
	values (@xtp_id, 5, 'sender_id')


/*
Author : Terry Luo
Desc: udpate item template
Date: 2006-03-02
*/
call %UPDXMLCMD% xml\aeTemplateView\tvw_item_selfstudy_detail.xml	aeTemplateview tvw_xml "tvw_id = 'DETAIL_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Self Study Item Template')"
call %UPDXMLCMD% xml\aeTemplateView\tvw_item_classroom_detail.xml	aeTemplateview tvw_xml "tvw_id = 'DETAIL_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Classroom Item Template')"
call %UPDXMLCMD% xml\aeTemplateView\tvw_item_classroom_run_detail.xml	aeTemplateview tvw_xml "tvw_id = 'DETAIL_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Classroom Run ItemTemplate')"

call %UPDXMLCMD% xml\aeTemplate\tpl_item_selfstudy.xml		aeTemplate tpl_xml "tpl_title = 'Self Study Item Template'"
call %UPDXMLCMD% xml\aeTemplate\tpl_item_classroom_run.xml	aeTemplate tpl_xml "tpl_title = 'Classroom Run ItemTemplate'"


/*
Author : Terry
Desc: update item template for selfstudy and update workflow xml.
Date: 2006-03-07
*/
call %UPDXMLCMD% xml\aeTemplate\tpl_item_selfstudy.xml		aeTemplate tpl_xml "tpl_title = 'Self Study Item Template'"
call %UPDXMLCMD% xml\aeTemplate\tpl_workflow_multilevel_en_us.xml	aeTemplate tpl_xml "tpl_title = 'Multi-level-approval Workflow Template'"


/*
Author : Randy
Desc : Add a AccessControl function for import user
Date : 2006-03-07
*/
insert into acFunction values ('USR_IMPORT', 'FUNCTION', 'USER', GETdATE(), NULL)
insert into acRoleFunction
select rol_id, ftn_id, '2006-03-07' from acFunction, acRole
where ftn_ext_id = 'USR_IMPORT'
and rol_id in (
	select rfn_rol_id from acRoleFunction where rfn_ftn_id in (
		select ftn_id from acFunction where ftn_ext_id = 'IMS_DATA_MGT'
	)
)

/*
Author : Amanda
Desc: import module
Date: 2006-02-21
*/
insert SystemMessage values ('COS002', 'ISO-8859-1', NULL)
insert SystemMessage values ('COS002', 'Big5', NULL)
insert SystemMessage values ('COS002', 'GB2312', NULL)

insert SystemMessage values ('COS003', 'ISO-8859-1', NULL)
insert SystemMessage values ('COS003', 'Big5', NULL)
insert SystemMessage values ('COS003', 'GB2312', NULL)

call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_cos002_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'COS002' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_cos002_gb2312.txt	SystemMessage sms_desc "sms_id = 'COS002' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_cos002_big5.txt	SystemMessage sms_desc "sms_id = 'COS002' and sms_lan = 'Big5'"

call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_cos003_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'COS003' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_cos003_gb2312.txt	SystemMessage sms_desc "sms_id = 'COS003' and sms_lan = 'GB2312'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_cos003_big5.txt	SystemMessage sms_desc "sms_id = 'COS003' and sms_lan = 'Big5'"

/*
Author : Dixson
Desc: add a field "raq_res_id" in table RawQuestion
Date: 2006-03-08
*/

ALTER TABLE RawQuestion ADD
	raq_res_id int NULL
GO

ALTER TABLE RawQuestion ADD
	raq_criteria nvarchar(500) NULL
GO

/*
Author : Dixson
Desc: remove data import function
Date: 2006-03-08
*/
delete from acHomePage where ac_hom_ftn_ext_id = 'IMS_DATA_MGT'
delete from acRoleFunction where rfn_ftn_id in (
select ftn_id from acFunction where ftn_ext_id = 'IMS_DATA_MGT'
)


/*
Author: Joyce Jiang
Desc: insert a new report template for Assessment Report By Question
Date: 2006-03-19
*/

insert into ReportTemplate
(rte_title_xml,rte_type,rte_get_xsl,rte_exe_xsl,rte_dl_xsl,rte_meta_data_url,rte_seq_no,rte_owner_ent_id,rte_create_usr_id,rte_create_timestamp,rte_upd_usr_id,rte_upd_timestamp)
select '', 'ASSESSMENT_QUE_GRP', 'rpt_assessment_que_srh.xsl', 'rpt_assessment_que_grp_res.xsl', null, null, 9, ste_ent_id, 's1u3', getDate(), 's1u3', getDate() from acSite

insert into acReportTemplate
(ac_rte_id,ac_rte_ent_id,ac_rte_rol_ext_id,ac_rte_ftn_ext_id,ac_rte_owner_ind,ac_rte_create_usr_id,ac_rte_create_timestamp)
select rte_id, null, rol_ext_id, 'RTE_READ', 0, 's1u3', getDate()
from acRole, acHomePage, ReportTemplate
where rol_ste_ent_id <> 0
and rol_ext_id = ac_hom_rol_ext_id
and ac_hom_ftn_ext_id = 'RPT_LINK'
and rol_ste_ent_id = rte_owner_ent_id
and rte_type = 'ASSESSMENT_QUE_GRP'

call %UPDXMLCMD% xml\ReportTemplate\rte_title_assessment_que_grp.xml ReportTemplate rte_title_xml "rte_type = 'ASSESSMENT_QUE_GRP'"

/*
Author : Terry
Desc: remove message rec history record for "REPLYTO" recipient
Date: 2006-04-11
*/
delete from mgRecHistory where mgh_rec_id in (select rec_id from mgRecipient where rec_type = 'REPLYTO')

/*
Author : Amanda
Desc: LEARNING_MODULE report
Date: 2006-04-12
*/
insert into ReportTemplate
(rte_title_xml,rte_type,rte_get_xsl,rte_exe_xsl,rte_dl_xsl,rte_meta_data_url,rte_seq_no,rte_owner_ent_id,rte_create_usr_id,rte_create_timestamp,rte_upd_usr_id,rte_upd_timestamp)
select '', 'LEARNING_MODULE', 'rpt_mod_srh.xsl', 'rpt_mod_res.xsl', null, null, 5, ste_ent_id, 's1u3', '2006-3-16', 's1u3', '2006-3-16' from acSite

insert into acReportTemplate
(ac_rte_id,ac_rte_ent_id,ac_rte_rol_ext_id,ac_rte_ftn_ext_id,ac_rte_owner_ind,ac_rte_create_usr_id,ac_rte_create_timestamp)
select rte_id, null, rol_ext_id, 'RTE_READ', 0, 's1u3', '2006-3-16'
from acRole, acHomePage, ReportTemplate
where rol_ste_ent_id <> 0
and rol_ext_id = ac_hom_rol_ext_id
and ac_hom_ftn_ext_id = 'RPT_LINK'
and rol_ste_ent_id = rte_owner_ent_id
and rte_type = 'LEARNING_MODULE'

insert into ObjectView
(ojv_owner_ent_id,ojv_type,ojv_subtype,ojv_option_xml,ojv_create_usr_id,ojv_create_timestamp,ojv_update_usr_id,ojv_update_timestamp)
select ste_ent_id, 'LEARNING_MODULE', 'USR', '', 's1u3', '2006-3-16', 's1u3', '2006-3-16' from acSite

insert into ObjectView
(ojv_owner_ent_id,ojv_type,ojv_subtype,ojv_option_xml,ojv_create_usr_id,ojv_create_timestamp,ojv_update_usr_id,ojv_update_timestamp)
select ste_ent_id, 'LEARNING_MODULE', 'OTHER', '', 's1u3', '2006-3-16', 's1u3', '2006-3-16' from acSite

update ReportTemplate
set rte_seq_no = 6
where rte_type ='SURVEY_COS_GRP' or rte_type = 'SURVEY_IND'

update ReportTemplate
set rte_seq_no = 7
where rte_type ='GLOBAL_ENROLLMENT'

update ReportTemplate
set rte_seq_no = 8
where rte_type ='SURVEY_QUE_GRP'

call %UPDXMLCMD% xml\ReportTemplate\rte_title_learning_module.xml  ReportTemplate rte_title_xml "rte_type = 'LEARNING_MODULE'"
call %UPDXMLCMD% xml\ObjectView\ojv_option_learning_module_other.xml ObjectView ojv_option_xml "ojv_type = 'LEARNING_MODULE' and ojv_subtype = 'OTHER'"
call %UPDXMLCMD% xml\ObjectView\ojv_option_learning_module_usr.xml ObjectView ojv_option_xml "ojv_type = 'LEARNING_MODULE' and ojv_subtype = 'USR'"

/*
Author : Amanda
Desc: 给讲师分配制作试卷的权限
Date: 2006-05-10
*/
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_creation_timestamp)
select rol_id, ftn_id, '2006-05-10' from acRole, acFunction
where rol_ext_id like 'INSTR%' and ftn_ext_id = 'RES_OFF_READ'

/*
Auth: randy
Date: 2006-05-30
Desc: optimize test module
*/

CREATE INDEX IX_RPM_1 ON RESOURCEPERMISSION
(RPM_RES_ID, RPM_ENT_ID)


/**
Auth: randy
Date: 2006-06-07
Desc: merge from b24
      insert into a increment field into Accomplishment and delete apm_attempt_nbr field;
*/
-- must stop wizbank first and then backup database

--create a temp table
CREATE TABLE dbo.Accomplishment_new
	(
	apm_id int NOT NULL IDENTITY (1, 1),
	apm_ent_id int NOT NULL,
	apm_tkh_id int NOT NULL,
	apm_obj_id int NOT NULL,
	apm_score decimal(18, 4) NULL,
	apm_max_score decimal(18, 4) NULL,
	apm_min_score decimal(18, 4) NULL,
	apm_status nvarchar(50) NULL,
	apm_aicc_score nvarchar(255) NULL,
	apm_data_xml ntext NULL
	)  ON [PRIMARY]
	 TEXTIMAGE_ON [PRIMARY]
GO

--copy Accomplishment records to Accomplishment_new table
insert into Accomplishment_new (apm_ent_id, apm_tkh_id, apm_obj_id, apm_score, apm_max_score, apm_min_score, apm_status, apm_aicc_score, apm_data_xml)
select apm_ent_id, apm_tkh_id, apm_obj_id, apm_score, apm_max_score, apm_min_score, apm_status, apm_aicc_score, apm_data_xml
from Accomplishment order by apm_ent_id , apm_tkh_id, apm_obj_id, apm_attempt_nbr

--delete Accomplishment table
DROP TABLE Accomplishment

--update Accomplishment_new name to Accomplishment

EXECUTE sp_rename N'dbo.Accomplishment_new', N'Accomplishment', 'OBJECT'
GO

----create Primary Key and Foreign Key
ALTER TABLE dbo.Accomplishment ADD CONSTRAINT
	PK_Accomplishment PRIMARY KEY CLUSTERED
	(
	apm_id
	) ON [PRIMARY]

GO


ALTER TABLE dbo.Accomplishment ADD CONSTRAINT
	FK_Accomplishment_Entity1 FOREIGN KEY
	(
	apm_ent_id
	) REFERENCES dbo.Entity
	(
	ent_id
	)
GO
ALTER TABLE dbo.Accomplishment ADD CONSTRAINT
	FK_Accomplishment_Objective1 FOREIGN KEY
	(
	apm_obj_id
	) REFERENCES dbo.Objective
	(
	obj_id
	)
GO
--create index for table
CREATE NONCLUSTERED INDEX IX_Accomplishment ON dbo.Accomplishment
	(
	apm_ent_id,
	apm_tkh_id,
	apm_obj_id
	) ON [PRIMARY]
GO

/**
Auth: Terry
Date: 2006-06-28
Desc: change column name
*/
EXECUTE sp_rename N'dbo.aeItem.itm_quota_exceed_notify_timestamp', N'itm_qte_notify_timestamp', 'COLUMN'


/**
Auth: Terry
Date: 2006-07-20
Desc: fix the bug when using zh_cn workflow , item quota function will not work
*/
update zh_cn workflow xml if you are using zh_ch workflow.

/**
Auth: Randy
Date: 2006-09-11
Desc: 修改优化学员测试部分的BUG 学员在动态测试在答过某条题后 培训管理在资源管理中不能对该题做修改
*/
update ProgressAttempt set atm_int_res_id =
  (select max (res_id) from resources where res_mod_res_id_test is null  and res_res_id_root = atm_int_res_id )
  where atm_int_res_id  in ( select res_id from ProgressAttempt,resources  where atm_int_res_id = res_id and res_res_id_root is null and res_mod_res_id_test is null)

/*
author: Terry
date: 2006-04-12
desc: add index for performance tunning
*/
if not exists
(select * from dbo.sysindexes where name = 'IX_aeApplication_app_itm_id')
	CREATE CLUSTERED INDEX IX_aeApplication_app_itm_id ON aeApplication
	(
	app_itm_id
	)
GO

if not exists
(select * from dbo.sysindexes where name = 'IX_aeAttendance_att_ats_id')
	CREATE CLUSTERED INDEX IX_aeAttendance_att_ats_id ON aeAttendance
	(
	att_ats_id
	)
GO

if not exists
(select * from dbo.sysindexes where name = 'IX_ModuleEvaluation_mov_tkh_id')
	CREATE INDEX IX_ModuleEvaluation_mov_tkh_id ON ModuleEvaluation
	(
	mov_tkh_id
	)
GO

/*
author: Amanda
date: 2006-09-13
desc: 飬ʾģûĿʱ?
*/

call %UPDXMLCMD% text\SystemMessage\en_us\sms_desc_mod008_iso-8859-1.txt	SystemMessage sms_desc "sms_id = 'MOD008' and sms_lan = 'ISO-8859-1'"
call %UPDXMLCMD% text\SystemMessage\zh_hk\sms_desc_mod008_big5.txt		SystemMessage sms_desc "sms_id = 'MOD008' and sms_lan = 'Big5'"
call %UPDXMLCMD% text\SystemMessage\zh_cn\sms_desc_mod008_gb2312.txt		SystemMessage sms_desc "sms_id = 'MOD008' and sms_lan = 'GB2312'"

/*
author: kawai
date: 2006-09-11
desc: move all system messages from database to label
*/
drop table SystemMessage
GO

/*
author: Amanda
date: 2006-09-19
desc: for fixing bug1202
*/
update Module
set mod_mod_res_id_parent = null
where mod_res_id in (
	select mod_res_id from Module where mod_res_id in (
		select rcn_res_id_content from ResourceContent where rcn_res_id in (
			select cos_res_id from Course where cos_itm_id not in (
				select itm_id from aeItem where itm_content_def ='PARENT' and itm_run_ind = 1
			)
		)
	)
	and mod_mod_res_id_parent is not null
)

update ResourceContent
set rcn_rcn_res_id_parent = null,
rcn_rcn_sub_nbr_parent = null
where rcn_res_id in (
	select rcn_res_id from ResourceContent
	where rcn_rcn_res_id_parent is not null
	and rcn_rcn_sub_nbr_parent is not null
	and rcn_res_id in (
		select mod_res_id from Module where mod_mod_res_id_parent is null
	)
)


/*
author: Amanda
date: 2006-09-20
desc: for fixing bug1207
*/
declare @ccr_ccr_id_parent int
declare @ccr_id int
declare ccr_id_cursor cursor for
select p.ccr_id, c.ccr_id
from CourseCriteria c, aeItem, aeItemRelation, CourseCriteria p
where c.ccr_type = 'COMPLETION' and c.ccr_ccr_id_parent is null
and c.ccr_itm_id = itm_id
and itm_run_ind = 1 and itm_content_def = 'PARENT'
and itm_id = ire_child_itm_id and ire_parent_itm_id = p.ccr_itm_id
and p.ccr_type = 'COMPLETION'
order by p.ccr_id, c.ccr_id

open ccr_id_cursor
fetch next from ccr_id_cursor into @ccr_ccr_id_parent, @ccr_id
while @@fetch_status = 0
begin
	update CourseCriteria
	set ccr_ccr_id_parent = @ccr_ccr_id_parent
	where ccr_id = @ccr_id
	fetch next from ccr_id_cursor into @ccr_ccr_id_parent, @ccr_id
end
close ccr_id_cursor
deallocate ccr_id_cursor

/*
author: kawai
date: 2006-11-02
desc: for fixing bug1741
*/
execute the script wizbank\batch\bugfix\bugfix1741.bat

update aeItem
set itm_send_enroll_email_ind = 1
where itm_id in (
	select itm_id
	from aeItemType, aeItem
	where ity_apply_ind = 1
	and ity_id = itm_type
	and ity_run_ind = itm_run_ind
	and itm_send_enroll_email_ind is null
)

call %UPDXMLCMD% xml\aeTemplate\tpl_item_selfstudy.xml		aeTemplate tpl_xml "tpl_title = 'Self Study Item Template'"
call %UPDXMLCMD% xml\aeTemplate\tpl_item_classroom_run.xml	aeTemplate tpl_xml "tpl_title = 'Classroom Run ItemTemplate'"
call %UPDXMLCMD% xml\aeTemplateView\tvw_item_selfstudy_detail.xml	aeTemplateview tvw_xml "tvw_id = 'DETAIL_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Self Study Item Template')"
call %UPDXMLCMD% xml\aeTemplateView\tvw_item_classroom_run_detail.xml	aeTemplateview tvw_xml "tvw_id = 'DETAIL_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Classroom Run ItemTemplate')"

/*
author: randy
date: 2006-11-13
desc: for fixing bug1623
*/

/* for DB2
 DB2༭ִһSQLʱȽַֹĳɡ^
     ݿִпҪӦ޸
*/
CREATE PROCEDURE CYBER.P_upd_score ()
LANGUAGE SQL
P1: BEGIN
declare res_id_ int;
DECLARE at_end INT DEFAULT 0;
declare res_id_cursor cursor for
select res_id from resources where res_subtype ='FSC' and res_type = 'QUE';
DECLARE CONTINUE HANDLER FOR NOT FOUND
set at_end = 1;
open res_id_cursor;
set at_end = 0;
fetch res_id_cursor into res_id_ ;
while at_end = 0 DO
update question
set que_score = (select sum(que_score) from question  where que_res_id in (select rcn_res_id_content from ResourceContent where rcn_res_id = res_id_ )) where que_res_id = res_id_ ;
fetch res_id_cursor into res_id_;
end while;
close res_id_cursor;
commit;
END P1 ^
call CYBER.P_upd_score()^
drop procedure CYBER.P_upd_score ()^

/* for MSSQL*/

declare @res_id_ int
declare res_id_cursor cursor for
select res_id from resources where res_subtype ='FSC' and res_type = 'QUE'
open res_id_cursor
fetch next from res_id_cursor into @res_id_
while @@fetch_status = 0
begin
update question
set que_score = (select sum(que_score) from question  where que_res_id in (select rcn_res_id_content from ResourceContent where rcn_res_id = @res_id_ )) where que_res_id = @res_id_

fetch next from res_id_cursor into @res_id_
end
close res_id_cursor
deallocate res_id_cursor



/*
author: randy
date: 2006-11-13
desc: for fixing bug1780
*/
insert into acRoleFunction select rol_id,ftn_id,getdate() from  acRole,acFunction
where  ftn_ext_id ='RES_MGT_PUBLIC' and  rol_ext_id = 'INSTR_1';

/*
author: Shelley
date: 2006-09-29
desc: Create new table for auto-save when a learner is doing a test
*/
CREATE TABLE [dbo].[ProgressAttemptSave] (
	[pas_tkh_id] [int] NOT NULL ,
	[pas_res_id] [int] NOT NULL ,
	[pas_time_remain] [int] NOT NULL ,
	[pas_flag] [ntext] COLLATE Chinese_Taiwan_Stroke_CI_AS NULL ,
	[pas_create_datetime] [datetime] NOT NULL ,
	[pas_create_usr_id] [nvarchar] (20) COLLATE Chinese_Taiwan_Stroke_CI_AS NOT NULL ,
	[pas_update_datetime] [datetime] NOT NULL ,
	[pas_update_usr_id] [nvarchar] (20) COLLATE Chinese_Taiwan_Stroke_CI_AS NOT NULL
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO

ALTER TABLE [dbo].[ProgressAttemptSave] ADD
	CONSTRAINT [PK_ProgressAttemptSave] PRIMARY KEY  CLUSTERED
	(
		[pas_tkh_id],
		[pas_res_id]
	)  ON [PRIMARY]
GO

CREATE TABLE [dbo].[ProgressAttemptSaveAnswer] (
	[psa_tkh_id] [int] NOT NULL ,
	[psa_res_id] [int] NOT NULL ,
	[psa_int_res_id] [int] NOT NULL ,
	[psa_int_order] [int] NOT NULL ,
	[psa_response_bil] [nvarchar] (1000) COLLATE Chinese_Taiwan_Stroke_CI_AS NULL ,
	[psa_create_datetime] [datetime] NOT NULL ,
	[psa_create_usr_id] [nvarchar] (20) COLLATE Chinese_Taiwan_Stroke_CI_AS NOT NULL ,
	[psa_update_datetime] [datetime] NOT NULL ,
	[psa_update_usr_id] [nvarchar] (20) COLLATE Chinese_Taiwan_Stroke_CI_AS NOT NULL
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[ProgressAttemptSaveAnswer] ADD
	CONSTRAINT [PK_ProgressAttemptSaveAnswer] PRIMARY KEY  CLUSTERED
	(
		[psa_tkh_id],
		[psa_res_id],
		[psa_int_res_id],
		[psa_int_order]
	)  ON [PRIMARY]
GO

alter table Module add mod_auto_save_ind bit NOT NULL default 0
GO

/*
author: Shelley
date: 2006-11-06
desc: Update the ResourceContent for the auto-save in dynamic test.
*/
ALTER TABLE dbo.ResourceContent ADD
	rcn_tkh_id int NOT NULL CONSTRAINT DF_ResourceContent_rcn_tkh_id DEFAULT -1
GO
-- the following foreign key needs to be dropped because the primary key is changed.
-- it is not added back because there is not enough time to investigate the impact.
-- (2006-11-23 kawai)
ALTER TABLE dbo.ResourceContent
	DROP CONSTRAINT FK_ResourceContent_ResourceContent
GO
ALTER TABLE dbo.ResourceContent
	DROP CONSTRAINT PK_ResourceContent
GO
ALTER TABLE dbo.ResourceContent ADD CONSTRAINT
	PK_ResourceContent PRIMARY KEY CLUSTERED
	(
	rcn_res_id,
	rcn_sub_nbr,
	rcn_tkh_id
	)

GO

/*
author: Shelley
date: 2007-04-10
desc: will not use aeAppnEnrolRelation from now on.
*/
drop table aeAppnEnrolRelation
GO

/*
author: Shelley
date: 2007-04-29
desc:
*/
--do this db_change if you use zh_cn workflow xml
call %UPDXMLCMD% xml\aeTemplate\tpl_workflow_multilevel_zh_cn.xml	aeTemplate tpl_xml "tpl_title = 'Multi-level-approval Workflow Template'"

/*
author: Shelley
date: 2007-04-28
desc: do some db_changes for Multi-level TC management.
*/
--Alter table tcTrainingCenter for Multi-level TC management
ALTER TABLE dbo.tcTrainingCenter ADD
	tcr_parent_tcr_id int NULL
GO
ALTER TABLE dbo.tcTrainingCenter ADD CONSTRAINT
	FK_tcTrainingCenter_tcTrainingCenter FOREIGN KEY
	(
	tcr_parent_tcr_id
	) REFERENCES dbo.tcTrainingCenter
	(
	tcr_id
	)
GO
CREATE NONCLUSTERED INDEX IX_tcTrainingCenter ON dbo.tcTrainingCenter
	(
	tcr_parent_tcr_id
	) ON [PRIMARY]
GO


--Add new table tcRelation for Multi-level TC management
CREATE TABLE dbo.tcRelation
	(
	tcn_child_tcr_id int NOT NULL,
	tcn_ancestor int NOT NULL,
	tcn_order int NOT NULL,
	tcn_create_usr_id nvarchar(20) NOT NULL,
	tcn_create_timestamp datetime NOT NULL
	)  ON [PRIMARY]
GO
ALTER TABLE dbo.tcRelation ADD CONSTRAINT
	PK_Table1 PRIMARY KEY CLUSTERED
	(
	tcn_child_tcr_id,
	tcn_ancestor
	) ON [PRIMARY]

GO
ALTER TABLE dbo.tcRelation ADD CONSTRAINT
	FK_tcRelation_tcTrainingCenter FOREIGN KEY
	(
	tcn_child_tcr_id
	) REFERENCES dbo.tcTrainingCenter
	(
	tcr_id
	)
GO
ALTER TABLE dbo.tcRelation ADD CONSTRAINT
	FK_tcRelation_tcTrainingCenter1 FOREIGN KEY
	(
	tcn_ancestor
	) REFERENCES dbo.tcTrainingCenter
	(
	tcr_id
	)
GO

--Alter table tcTrainingCenterOfficer for Multi-level TC management
ALTER TABLE dbo.tcTrainingCenterOfficer ADD
	tco_major_ind int NOT NULL CONSTRAINT DF_tcTrainingCenterOfficer_tco_major_ind DEFAULT 0,
	tco_update_timestamp datetime NULL,
	tco_update_usr_id nvarchar(20) NULL
GO

/*
Author : Shelley
Desc: Assign  "TC_MAIN" to role "TADM"
Date: 2007-05-08
*/
insert into acRoleFunction  (rfn_rol_id,rfn_ftn_id,rfn_creation_timestamp)
select rol_id,ftn_id,getdate() from acRole,acFunction  where rol_ste_uid = 'TADM' and  ftn_ext_id = 'TC_MAIN';

insert into acHomepage (ac_hom_ent_id, ac_hom_rol_ext_id, ac_hom_ftn_ext_id, ac_hom_create_usr_id, ac_hom_create_timestamp)
select null, rol_ext_id, 'TC_MAIN', 's1u3', getdate() from acRole where rol_ste_uid = 'TADM';

/*
Author: Shelley
Desc:
Date: 2007-05-08
*/
--Add a new functions 'TC_MAIN_IN_TCR' and assign  "TC_MAIN_IN_TCR" to role "TADM";
Insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml) values ('TC_MAIN_IN_TCR', 'FUNCTION', 'TRAININGCENTER', getdate(), NULL);
Insert into acRoleFunction  (rfn_rol_id,rfn_ftn_id,rfn_creation_timestamp)
select rol_id,ftn_id,getdate() from acRole,acFunction  where rol_ste_uid = 'TADM' and  ftn_ext_id = 'TC_MAIN_IN_TCR';

--Add a new functions 'TC_MAIN_IN_ORG' and assign  "TC_MAIN_IN_ORG" to role "SADM";
Insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml) values ('TC_MAIN_IN_ORG', 'FUNCTION', 'TRAININGCENTER', getdate(), NULL);
Insert into acRoleFunction  (rfn_rol_id,rfn_ftn_id,rfn_creation_timestamp)
select rol_id,ftn_id,getdate() from acRole,acFunction  where rol_ste_uid = 'SADM' and  ftn_ext_id = 'TC_MAIN_IN_ORG';

/*
Author: Shelley
Desc: add a new cloum for tcTrainingCenter to indicate if the TA can manage the users and usr groups
Date: 2007-05-08
*/
ALTER TABLE dbo.tcTrainingCenter ADD
	tcr_user_mgt_ind bit NOT NULL CONSTRAINT DF_tcTrainingCenter_tcr_user_mgt_ind DEFAULT 1
GO

/*
Author: Shelley
Desc:
Date: 2007-05-15
*/
--Delete the Instructor Management in system.
delete from acHomePage where ac_hom_ftn_ext_id = 'INSTR_MAIN';
delete from acRoleFunction where rfn_ftn_id = (select ftn_id from acFunction where ftn_ext_id = 'INSTR_MAIN');
delete from acFunction where ftn_ext_id = 'INSTR_MAIN';

--Add a new functions 'USR_MGT_GRADE' for grade management and assigned it to "SADM";
Insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml) values ('USR_MGT_GRADE', 'FUNCTION', 'USER', getdate(), NULL);
Insert into acRoleFunction  (rfn_rol_id,rfn_ftn_id,rfn_creation_timestamp)
select rol_id,ftn_id,getdate() from acRole,acFunction  where rol_ste_uid = 'SADM' and  ftn_ext_id = 'USR_MGT_GRADE';

--Remove the incorrect functions which were assigned to SA and TA before.
Delete from acRoleFunction
where rfn_rol_id in (select rol_id from acRole where rol_ste_uid = 'SADM')
and rfn_ftn_id = (select ftn_id from acFunction where ftn_ext_id = 'USR_MGT_NON_ADMIN');

Delete from acRoleFunction
where rfn_rol_id in (select rol_id from acRole where rol_ste_uid = 'TADM')
and rfn_ftn_id = (select ftn_id from acFunction where ftn_ext_id = 'USR_MGT_ADMIN')


/*
Author:Dixson
date:2007-05-29
desc:add column to table message
*/
alter table MESSAGE add msg_tcr_id int;

ALTER TABLE [MESSAGE] ADD
	CONSTRAINT [FK_Msg_Tcr] FOREIGN KEY
	(
		[msg_tcr_id]
	) REFERENCES [tcTrainingCenter] (
		[tcr_id]
	)
go

CREATE INDEX [IX_Message_msg_tcr_id] ON [dbo].[MESSAGE] ([msg_tcr_id])
WITH FILLFACTOR = 90
ON [PRIMARY]
go

update acFunction set ftn_type = 'HOMEPAGE' where ftn_ext_id = 'MSG_MGT_SYS';
insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values
('MSG_MGT_SYS_IN_TCR', 'FUNCTION', 'HOMEPAGE', getdate(), '');

call %UPDXMLCMD% xml\acFunction\ftn_msg_mgt_sys.xml acFunction ftn_xml "ftn_ext_id = 'MSG_MGT_SYS'"
call %UPDXMLCMD% xml\acFunction\ftn_msg_mgt_sys_in_tcr.xml acFunction ftn_xml "ftn_ext_id = 'MSG_MGT_SYS_IN_TCR'"

delete acRoleFunction where rfn_rol_id in (select rol_id from acRole where rol_ext_id like 'CDN_%' or rol_ext_id like 'TUT_%' or rol_ext_id like 'TADM_%')
and rfn_ftn_id in (select ftn_id from acFunction where ftn_ext_id = 'MSG_MGT_SYS');

declare @adm_id  int
declare @ta_id int
declare @msg_mgt_in_tcr int
declare @msg_mgt_sys int

select @adm_id = rol_id from acRole where rol_ext_id like 'ADM_1';
select @ta_id = rol_id from acRole where rol_ext_id like 'TADM_1';

select @msg_mgt_in_tcr = ftn_id from acFunction where ftn_ext_id = 'MSG_MGT_SYS_IN_TCR';
select @msg_mgt_sys = ftn_id from acFunction where ftn_ext_id = 'MSG_MGT_SYS';

insert into acRoleFunction values (@ta_id, @msg_mgt_in_tcr, getdate());

insert into acHomePage values (null, 'ADM_1','MSG_MGT_SYS', 's1u3', getdate());
insert into acHomePage values (null, 'TADM_1','MSG_MGT_SYS_IN_TCR', 's1u3', getdate());

/*
Author:Dixson
date:2007-05-29
desc:add column to table aeCatalog
*/

alter table aeCatalog add cat_tcr_id int;

ALTER TABLE [aeCatalog] ADD
	CONSTRAINT [FK_Cat_Tcr] FOREIGN KEY
	(
		[cat_tcr_id]
	) REFERENCES [tcTrainingCenter] (
		[tcr_id]
	)
go

CREATE INDEX [IX_aeCatalog_cat_tcr_id] ON [dbo].[aeCatalog] ([cat_tcr_id])
WITH FILLFACTOR = 90
ON [PRIMARY]

/*
Author:Shelley
date:2007-05-31
desc:
*/
call %UPDXMLCMD% xml\aeTemplateView\tvw_item_selfstudy_detail.xml	aeTemplateview tvw_xml "tvw_id = 'DETAIL_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Self Study Item Template')"

call %UPDXMLCMD% xml\aeTemplateView\tvw_item_classroom_detail.xml	aeTemplateview tvw_xml "tvw_id = 'DETAIL_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Classroom Item Template')"

/*
Auth: Jackyx
Date: 2007-04-28
Desc: add colunm for Resources and Training Center
*/
Alter Table Objective add obj_tcr_id int null
Alter Table Objective ADD
 CONSTRAINT [FK_OBJ_TC] FOREIGN KEY
 (
  [obj_tcr_id]
 ) REFERENCES tcTrainingCenter (
  [tcr_id]
 )
/*
Auth: Jacky xiao
Date: 2007-05-08
Desc: Add index for objective
*/
BEGIN TRANSACTION
CREATE NONCLUSTERED INDEX IX_Objective ON dbo.Objective
	(
	obj_tcr_id
	) ON [PRIMARY]
GO
COMMIT

/*
Auth: Jacky xiao
Date: 2007-06-05
Desc:
*/
/* change the item template */
call %UPDXMLCMD% xml\aeTemplateView\tvw_item_audiovideo_detail.xml	aeTemplateview tvw_xml "tvw_id = 'DETAIL_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Audio/Video Item Template')"
call %UPDXMLCMD% xml\aeTemplateView\tvw_item_book_detail.xml		aeTemplateview tvw_xml "tvw_id = 'DETAIL_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Book Item Template')"
call %UPDXMLCMD% xml\aeTemplateView\tvw_item_selfstudy_detail.xml	aeTemplateview tvw_xml "tvw_id = 'DETAIL_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Self Study Item Template')"
call %UPDXMLCMD% xml\aeTemplateView\tvw_item_website_detail.xml		aeTemplateview tvw_xml "tvw_id = 'DETAIL_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Website Item Template')"
call %UPDXMLCMD% xml\aeTemplateView\tvw_item_classroom_detail.xml	aeTemplateview tvw_xml "tvw_id = 'DETAIL_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Classroom Item Template')"
call %UPDXMLCMD% xml\aeTemplateView\tvw_item_classroom_run_detail.xml	aeTemplateview tvw_xml "tvw_id = 'DETAIL_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Classroom Run ItemTemplate')"
/*add column for reports*/
call %UPDXMLCMD% xml\ObjectView\ojv_option_learner_item.xml			ObjectView ojv_option_xml "ojv_owner_ent_id = 1 AND ojv_type = 'LEARNER_REPORT' AND ojv_subtype = 'ITM'"
call %UPDXMLCMD% xml\ObjectView\ojv_option_learner_learner_item.xml		ObjectView ojv_option_xml "ojv_owner_ent_id = 1 AND ojv_type = 'LEARNING_ACTIVITY_LRN' AND ojv_subtype = 'ITM'"
call %UPDXMLCMD% xml\ObjectView\ojv_option_survey_course_report_item.xml	ObjectView ojv_option_xml "ojv_owner_ent_id = 1 AND ojv_type = 'SURVEY_COURSE_REPORT' AND ojv_subtype = 'ITM'"

/*
Auth: Jacky xiao
Date: 2007-06-06
Desc: add training center in learner cousre information
*/
update aeTemplateView set tvw_tcr_ind = 1 where tvw_id = 'LRN_VIEW'

call %UPDXMLCMD% xml\aeTemplateView\tvw_item_audiovideo_lrn.xml		aeTemplateview tvw_xml "tvw_id = 'LRN_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Audio/Video Item Template')"
call %UPDXMLCMD% xml\aeTemplateView\tvw_item_book_lrn.xml		aeTemplateview tvw_xml "tvw_id = 'LRN_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Book Item Template')"
call %UPDXMLCMD% xml\aeTemplateView\tvw_item_selfstudy_lrn.xml		aeTemplateview tvw_xml "tvw_id = 'LRN_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Self Study Item Template')"
call %UPDXMLCMD% xml\aeTemplateView\tvw_item_website_lrn.xml		aeTemplateview tvw_xml "tvw_id = 'LRN_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Website Item Template')"
call %UPDXMLCMD% xml\aeTemplateView\tvw_item_classroom_lrn.xml		aeTemplateview tvw_xml "tvw_id = 'LRN_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Classroom Item Template')"
call %UPDXMLCMD% xml\aeTemplateView\tvw_item_classroom_run_lrn.xml	aeTemplateview tvw_xml "tvw_id = 'LRN_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Classroom Run ItemTemplate')"

/*
Auth: Joyce Jiang
Date: 2007-06-25
Desc: alert aeAttendance pk to app_id
*/
ALTER TABLE aeAttendance
	DROP CONSTRAINT PK_aeAttendance
ALTER TABLE aeAttendance ADD CONSTRAINT
	PK_aeAttendance PRIMARY KEY NONCLUSTERED
	(
	att_app_id
	) ON [PRIMARY]


/*
Auth: Sandy Xie
Date: 2007-08-30
Desc: alter table module,set column mod_auto_save_ind to int
*/
alter table Module add mod_auto_save_ind_bak int
GO

update Module set mod_auto_save_ind_bak = mod_auto_save_ind

ALTER TABLE Module DROP CONSTRAINT DF__Module__mod_auto__636EBA21
alter table Module drop column mod_auto_save_ind
alter table Module add mod_auto_save_ind int
go

update Module set mod_auto_save_ind = mod_auto_save_ind_bak
update Module set mod_auto_save_ind = -1 where mod_type not in ('TST','DXT')

alter table Module drop column mod_auto_save_ind_bak
go

/*
Auth: Amanda
Date: 2007-10-10
Desc: fix bug 3028
*/
call %UPDXMLCMD% xml\aeTemplateView\tvw_item_classroom_run_lrn.xml	aeTemplateview tvw_xml "tvw_id = 'LRN_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Classroom Run ItemTemplate')"
update aeTemplateView set tvw_rsv_ind = 1 where tvw_id = 'LRN_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Classroom Run ItemTemplate')

/*
Auth: kawai
Date: 2007-08-29
Desc: (bug3589)update res owner who are already deleted
*/
update Resources
set res_usr_id_owner = 's1u3'
where res_id in (
	select res_id from Resources where res_usr_id_owner in (
		select usr_id from RegUser where usr_status = 'DELETED'
	)
)

/*
Auth: Amanda
Date: 2007-11-28
Desc: fix bug 61
*/
insert into acHomePage (ac_hom_rol_ext_id,ac_hom_ftn_ext_id, ac_hom_create_usr_id, ac_hom_create_timestamp)
select rol_ext_id, 'APPR_APP_LIST', 's1u3', '2008-03-25 00:00:00' from acRole where rol_ext_id like 'ADM_%'

/*
Auth: Terry
Date: 2007-11-15
Desc: import scorm and netg courseware as a resource
*/
create table ScormResource
(
  srs_res_id        int not null,
  srs_structure_xml ntext,
  srs_aicc_version  nvarchar(50),
  srs_vendor        nvarchar(255),
  srs_max_normal    int
);
alter table ScormResource
  add constraint PK_srs primary key clustered (srs_res_id);
alter table ScormResource
  add constraint FK_srs_res1 foreign key (srs_res_id)
  references Resources (res_id)
go

insert into DisplayOption (dpo_res_id, dpo_res_type, dpo_res_subtype, dpo_view, dpo_icon_ind, dpo_title_ind, dpo_lan_ind, dpo_desc_ind, dpo_instruct_ind, dpo_eff_start_datetime_ind, dpo_eff_end_datetime_ind, dpo_difficulty_ind, dpo_time_limit_ind, dpo_suggested_time_ind, dpo_duration_ind, dpo_max_score_ind, dpo_pass_score_ind, dpo_pgr_last_acc_datetime_ind, dpo_pgr_start_datetime_ind, dpo_pgr_complete_datetime_ind, dpo_pgr_attempt_nbr_ind, dpo_instructor_ind, dpo_organization_ind, dpo_moderator_ind, dpo_evt_datetime_ind, dpo_evt_venue_ind, dpo_status_ind, dpo_max_usr_attempt_ind, dpo_moderator_ind_bak, dpo_instructor_ind_bak, dpo_lan_ind_bak, dpo_difficulty_ind_bak, dpo_suggested_time_ind_bak)
values (0, 'MOD', 'SCORM', 'LRN_READ', 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0);

insert into DisplayOption (dpo_res_id, dpo_res_type, dpo_res_subtype, dpo_view, dpo_icon_ind, dpo_title_ind, dpo_lan_ind, dpo_desc_ind, dpo_instruct_ind, dpo_eff_start_datetime_ind, dpo_eff_end_datetime_ind, dpo_difficulty_ind, dpo_time_limit_ind, dpo_suggested_time_ind, dpo_duration_ind, dpo_max_score_ind, dpo_pass_score_ind, dpo_pgr_last_acc_datetime_ind, dpo_pgr_start_datetime_ind, dpo_pgr_complete_datetime_ind, dpo_pgr_attempt_nbr_ind, dpo_instructor_ind, dpo_organization_ind, dpo_moderator_ind, dpo_evt_datetime_ind, dpo_evt_venue_ind, dpo_status_ind, dpo_max_usr_attempt_ind, dpo_moderator_ind_bak, dpo_instructor_ind_bak, dpo_lan_ind_bak, dpo_difficulty_ind_bak, dpo_suggested_time_ind_bak)
values (0, 'MOD', 'SCORM', 'IST_READ', 1, 1, 0, 1, 0, 1, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0);

insert into DisplayOption (dpo_res_id, dpo_res_type, dpo_res_subtype, dpo_view, dpo_icon_ind, dpo_title_ind, dpo_lan_ind, dpo_desc_ind, dpo_instruct_ind, dpo_eff_start_datetime_ind, dpo_eff_end_datetime_ind, dpo_difficulty_ind, dpo_time_limit_ind, dpo_suggested_time_ind, dpo_duration_ind, dpo_max_score_ind, dpo_pass_score_ind, dpo_pgr_last_acc_datetime_ind, dpo_pgr_start_datetime_ind, dpo_pgr_complete_datetime_ind, dpo_pgr_attempt_nbr_ind, dpo_instructor_ind, dpo_organization_ind, dpo_moderator_ind, dpo_evt_datetime_ind, dpo_evt_venue_ind, dpo_status_ind, dpo_max_usr_attempt_ind, dpo_moderator_ind_bak, dpo_instructor_ind_bak, dpo_lan_ind_bak, dpo_difficulty_ind_bak, dpo_suggested_time_ind_bak)
values (0, 'MOD', 'SCORM', 'IST_EDIT', 1, 1, 0, 1, 0, 1, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0);

update DisplayOption set dpo_title_ind = 0,dpo_desc_ind=0  where dpo_res_subtype='NETG_COK' and dpo_view='IST_EDIT';

/*
Auth: Harvey
Date: 2007-12-12
Desc: create table EntityRelation & EntityRelationHistory
*/
CREATE TABLE EntityRelation
	(
	ern_child_ent_id int NOT NULL,
	ern_ancestor_ent_id int NOT NULL,
	ern_order int NOT NULL,
	ern_type nvarchar(50) NOT NULL,
	ern_parent_ind int NOT NULL,
	ern_syn_timestamp datetime,
	ern_remain_on_syn int NOT NULL,
	ern_create_timestamp datetime NOT NULL,
	ern_create_usr_id nvarchar(20) NOT NULL
	)
ALTER TABLE EntityRelation ADD CONSTRAINT
	PK_ern PRIMARY KEY CLUSTERED
	(
	ern_child_ent_id,
	ern_ancestor_ent_id
	)
ALTER TABLE EntityRelation WITH NOCHECK ADD CONSTRAINT
	FK_ern_ent1 FOREIGN KEY
	(
	ern_child_ent_id
	) REFERENCES Entity
	(
	ent_id
	)
ALTER TABLE EntityRelation WITH NOCHECK ADD CONSTRAINT
	FK_ern_ent2 FOREIGN KEY
	(
	ern_ancestor_ent_id
	) REFERENCES Entity
	(
	ent_id
	)
CREATE
  INDEX IX_ern1 ON EntityRelation (ern_ancestor_ent_id, ern_parent_ind)
CREATE TABLE EntityRelationHistory
	(
	erh_id int NOT NULL IDENTITY (1, 1),
	erh_child_ent_id int NOT NULL,
	erh_ancestor_ent_id int NOT NULL,
	erh_order int NOT NULL,
	erh_type nvarchar(50) NOT NULL,
	erh_parent_ind int NOT NULL,
	erh_start_timestamp datetime NOT NULL,
	erh_end_timestamp datetime NOT NULL,
	erh_create_timestamp datetime NOT NULL,
	erh_create_usr_id nvarchar(20) NOT NULL
	)
ALTER TABLE EntityRelationHistory ADD CONSTRAINT
	PK_erh PRIMARY KEY CLUSTERED
	(
	erh_id
	)
ALTER TABLE EntityRelationHistory WITH NOCHECK ADD CONSTRAINT
	FK_erh_ent1 FOREIGN KEY
	(
	erh_child_ent_id
	) REFERENCES Entity
	(
	ent_id
	)
ALTER TABLE EntityRelationHistory WITH NOCHECK ADD CONSTRAINT
	FK_erh_ent2 FOREIGN KEY
	(
	erh_ancestor_ent_id
	) REFERENCES Entity
	(
	ent_id
	)
CREATE
  INDEX IX_erh1 ON EntityRelationHistory (erh_child_ent_id)
GO

execute the script wizbank\batch\gpm2enr\bin\gpm2enr.cmd

/*
Auth: Harvey
Date: 2008-02-20
Desc: (bug4618) update Fixed Scenario score which is wrong.
*/
BEGIN TRAN
declare @real_que_score int
declare @test_res_id int

DECLARE que_cursor CURSOR FOR
select res_mod_res_id_test as test_res_id, sum(que_score) as score from Resources, Question
	where res_mod_res_id_test in
		(select res_id from Resources where res_type ='Que' and res_subType = 'FSC' and  res_status='ON' )
	and que_res_id = res_id
group by res_mod_res_id_test
union
select res_id as test_res_id, que_score as score from Resources, Question
where que_res_id =res_id and res_id not in (
select distinct res_mod_res_id_test from Resources
	where res_mod_res_id_test in
		(select res_id from Resources  where res_type ='Que' and res_subType = 'FSC' and res_status='ON' )
) and  res_type ='Que' and res_subType = 'FSC' and res_status='ON'
order by test_res_id asc
OPEN que_cursor

FETCH NEXT FROM que_cursor
	INTO @test_res_id, @real_que_score

WHILE @@FETCH_STATUS = 0
BEGIN
	update Question set que_score = @real_que_score
	where que_res_id = @test_res_id and que_score != @real_que_score
	FETCH NEXT FROM que_cursor
		INTO @test_res_id, @real_que_score
END
CLOSE que_cursor
DEALLOCATE que_cursor
COMMIT

/*
Auth: Robin
Date: 2008-02-21
Desc: Increases row 'mov_not_mark_ind', the judgment question whether in grading.
*/
alter table ModuleEvaluation add mov_not_mark_ind int;
go

/*
Auth: Elvea
Date: 2008-02-26
Desc: Complete end date notification email
*/
alter table aeItem add itm_notify_days int
alter table aeItem add itm_notify_email nvarchar(255)
go

call %UPDXMLCMD% xml\aeTemplate\tpl_item_selfstudy.xml		aeTemplate tpl_xml "tpl_title = 'Self Study Item Template'"
call %UPDXMLCMD% xml\aeTemplateView\tvw_item_selfstudy_detail.xml	aeTemplateview tvw_xml "tvw_id = 'DETAIL_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Self Study Item Template')"

call %UPDXMLCMD% xml\aeTemplate\tpl_item_classroom_run.xml	aeTemplate tpl_xml "tpl_title = 'Classroom Run ItemTemplate'"
call %UPDXMLCMD% xml\aeTemplateView\tvw_item_classroom_run_detail.xml	aeTemplateview tvw_xml "tvw_id = 'DETAIL_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Classroom Run ItemTemplate')"

insert into xslTemplate (xtp_type, xtp_subtype, xtp_channel_type, xtp_channel_api, xtp_xsl, xtp_xml_url, xtp_mailmerge_ind, xtp_title)
values ('COURSE_NOTIFY', 'HTML', 'SMTP', NULL, 'course_due_date_info.xsl', 'http://host:port/servlet/aeAction?', 0, null);

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
select xtp_id, 1, 'cmd' from xslTemplate where xtp_type = 'COURSE_NOTIFY';
insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
select xtp_id, 2, 'app_id' from xslTemplate where xtp_type = 'COURSE_NOTIFY';

/*
Auth: Elvea
Date: 2008-03-04
Desc: cancel self-created enrollments
*/
call %UPDXMLCMD% xml\aeTemplate\tpl_workflow_multilevel_zh_cn.xml	aeTemplate tpl_xml "tpl_title = 'Multi-level-approval Workflow Template'"
call %UPDXMLCMD% xml\aeTemplate\tpl_workflow_multilevel_zh_cn.xml	aeTemplate tpl_xml "tpl_title = 'Multi-level-approval Workflow Template'"

/*
Auth: Harvey
Date: 2008-02-26
Desc: create table SystemSetting to save the Configure threshold values
*/
CREATE TABLE SystemSetting
	(
	sys_cfg_type nvarchar(50) NOT NULL,
	sys_cfg_value nvarchar(255),
	sys_cfg_create_timestamp datetime NOT NULL,
	sys_cfg_create_usr_id nvarchar(20) NOT NULL,
	sys_cfg_update_timestamp datetime NOT NULL,
	sys_cft_update_usr_id nvarchar(20) NOT NULL
	)
ALTER TABLE SystemSetting ADD CONSTRAINT
	PK_SystemSetting PRIMARY KEY CLUSTERED
	(
	sys_cfg_type
	)

GO
--insert default values
insert into SystemSetting values ('THR_WARN', null, '2008-03-25 00:00:00', 's1u3', '2008-03-25 00:00:00', 's1u3' )
insert into SystemSetting values ('THR_BLOCK', null, '2008-03-25 00:00:00', 's1u3', '2008-03-25 00:00:00', 's1u3' )
insert into SystemSetting values ('THR_SPT_EMAIL', null, '2008-03-25 00:00:00', 's1u3', '2008-03-25 00:00:00', 's1u3' )

--for save the active users
CREATE TABLE CurrentActiveUser
	(
	cau_usr_ent_id int NOT NULL,
	cau_login_date datetime NOT NULL,
	cau_sess_id nvarchar(80) NOT NULL
	)

/*
Auth: Harvey
Date: 2008-02-26
Desc: add new function 'SYS_SETTING'
*/
insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('SYS_SETTING', 'FUNCTION', 'HOMEPAGE', '2008-03-25 00:00:00', null);

insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_creation_timestamp)
select rol_id, ftn_id, '2008-03-25 00:00:00' from acRole, acFunction
where rol_ext_id like 'ADM_%' and ftn_ext_id = 'SYS_SETTING';

insert into acHomePage (ac_hom_rol_ext_id,ac_hom_ftn_ext_id, ac_hom_create_usr_id, ac_hom_create_timestamp)
select rol_ext_id, 'SYS_SETTING', 's1u3', '2008-03-25 00:00:00' from acRole where rol_ext_id like 'ADM_%';

call %UPDXMLCMD% xml\acFunction\ftn_sys_setting.xml	acFunction ftn_xml "ftn_ext_id = 'SYS_SETTING'"

/*
Auth: Harvey
Date: 2008-02-29
Desc: create table for record longin users when the system is reach block/warn threshold
*/
CREATE TABLE UserLoginTracking (
	ult_usr_ent_id int not null,
 	ult_type nvarchar(20),
	ult_login_timestamp datetime
)

/*
Auth: Tim
Date: 2008-03-03
Desc: create table for the log of record Synchronization job
*/
CREATE TABLE DataImportLog(
	dil_start_timestamp datetime  ,
	dil_end_timestamp datetime  ,
	dil_type nvarchar (20) NOT NULL ,
	dil_succ_count int ,
	dil_fail_count int
)

/*
Auth: Terry
Date: 2008-03-08
Desc: add performance warning support email
*/
insert into xslTemplate
(xtp_type, xtp_subtype, xtp_channel_type, xtp_channel_api, xtp_xsl, xtp_xml_url, xtp_mailmerge_ind, xtp_title)
values
('SYS_PERFORMANCE_NOTIFY', 'HTML', 'SMTP', null, 'sys_performance_notify.xsl', 'http://host:port/servlet/aeAction?', 0, null)

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
select xtp_id, 1, 'cmd' from xslTemplate where xtp_type = 'SYS_PERFORMANCE_NOTIFY'
insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
select xtp_id, 2, 'active_user' from xslTemplate where xtp_type = 'SYS_PERFORMANCE_NOTIFY'
insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
select xtp_id, 3, 'warning_user' from xslTemplate where xtp_type = 'SYS_PERFORMANCE_NOTIFY'
insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
select xtp_id, 4, 'blocking_user' from xslTemplate where xtp_type = 'SYS_PERFORMANCE_NOTIFY'
insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
select xtp_id, 5, 'gen_time' from xslTemplate where xtp_type = 'SYS_PERFORMANCE_NOTIFY'
insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
select xtp_id, 6, 'sender_id' from xslTemplate where xtp_type = 'SYS_PERFORMANCE_NOTIFY'

/*
Auth: Robin
Date: 2008-03-11
Desc: fix DSC error record
*/
INSERT INTO QueContainer(qct_res_id, qct_select_logic, qct_allow_shuffle_ind)
	select res_id, qct_select_logic, qct_allow_shuffle_ind
	from resources,QueContainer where res_res_id_root is not null and
		res_subtype = 'FSC' and res_id not in (select qct_res_id from QueContainer) and res_res_id_root = qct_res_id

INSERT INTO QueContainerSpec(qcs_res_id, qcs_obj_id, qcs_type,
qcs_score, qcs_difficulty, qcs_privilege, qcs_duration, qcs_qcount,
qcs_create_timestamp, qcs_create_usr_id, qcs_update_timestamp,
qcs_update_usr_id)
	select res_id, qcs_obj_id, qcs_type, qcs_score, qcs_difficulty,
	qcs_privilege, qcs_duration, qcs_qcount, qcs_create_timestamp,
	qcs_create_usr_id, qcs_update_timestamp, qcs_update_usr_id
	from resources,QueContainerSpec where res_res_id_root is not null and
	res_subtype = 'DSC' and res_id not in (select qcs_res_id from QueContainerSpec) and res_res_id_root = qcs_res_id

/*
Auth: Tim
Date: 2008-03-10
Desc: logs remark of record Synchronization job
*/
alter table DataImportLog add dil_remark nvarchar(255)
go

/*
Auth: Shelley
Date: 2008-02-27
Desc: add allow waitlist for item
*/
ALTER TABLE aeItem ADD
	itm_not_allow_waitlist_ind int
go

/*
Auth: Shelley
Date: 2008-02-28
Desc: add for target learner and target enrollment
*/
CREATE TABLE aeItemTargetRule
	(
	itr_id int NOT NULL IDENTITY (1, 1),
	itr_itm_id int NOT NULL,
	itr_group_id nvarchar(2000) NOT NULL,
	itr_grade_id nvarchar(2000) NOT NULL,
	itr_type nvarchar(20) NOT NULL,
	itr_create_usr_id nvarchar(20) NOT NULL,
	itr_create_timestamp datetime NOT NULL,
	itr_update_usr_id nvarchar(20) NOT NULL,
	itr_update_timestamp datetime NOT NULL
	)
ALTER TABLE aeItemTargetRule ADD CONSTRAINT
	PK_itr PRIMARY KEY CLUSTERED
	(
	itr_id
	)
CREATE TABLE aeItemTargetRuleDetail
	(
	ird_itm_id int NOT NULL,
	ird_itr_id int NOT NULL,
	ird_group_id int NOT NULL,
	ird_grade_id int NOT NULL,
	ird_type nvarchar(20) NOT NULL,
	ird_create_usr_id nvarchar(20) NOT NULL,
	ird_create_timestamp datetime NOT NULL,
	ird_update_usr_id nvarchar(20) NOT NULL,
	ird_update_timestamp datetime NOT NULL
	)
ALTER TABLE aeItemTargetRuleDetail  ADD CONSTRAINT
	PK_ird PRIMARY KEY CLUSTERED
	(
	ird_itm_id,
	ird_itr_id,
	ird_group_id,
	ird_grade_id
	)
CREATE INDEX IX_ird1 ON aeItemTargetRuleDetail
	(
	ird_group_id,
	ird_grade_id
	)
ALTER TABLE aeItemTargetRuleDetail ADD CONSTRAINT
	FK_ird_itr1 FOREIGN KEY
	(
	ird_itr_id
	) REFERENCES aeItemTargetRule
	(
	itr_id
	)
ALTER TABLE aeItem ADD
	itm_target_enrol_type nvarchar(20)
go

call %UPDXMLCMD% xml\aeTemplateView\tvw_item_selfstudy_detail.xml	aeTemplateview tvw_xml "tvw_id = 'DETAIL_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Self Study Item Template')"
call %UPDXMLCMD% xml\aeTemplateView\tvw_item_classroom_detail.xml	aeTemplateview tvw_xml "tvw_id = 'DETAIL_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Classroom Item Template')"
call %UPDXMLCMD% xml\aeTemplateView\tvw_item_classroom_run_detail.xml	aeTemplateview tvw_xml "tvw_id = 'DETAIL_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Classroom Run ItemTemplate')"

execute the script wizbank\batch\ite2itr\bin\ite2itr.cmd

/*
Auth: Shelley
Date: 2008-03-18
Desc:
*/
call %UPDXMLCMD% xml\aeTemplateView\tvw_item_book_detail.xml		aeTemplateview tvw_xml "tvw_id = 'DETAIL_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Book Item Template')"
call %UPDXMLCMD% xml\aeTemplateView\tvw_item_audiovideo_detail.xml	aeTemplateview tvw_xml "tvw_id = 'DETAIL_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Audio/Video Item Template')"
call %UPDXMLCMD% xml\aeTemplateView\tvw_item_website_detail.xml		aeTemplateview tvw_xml "tvw_id = 'DETAIL_VIEW' and tvw_tpl_id in (select tpl_id from aeTemplate where tpl_title = 'Website Item Template')"

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

INSERT INTO ReportTemplate (rte_title_xml, rte_type, rte_get_xsl, rte_exe_xsl, rte_dl_xsl, rte_meta_data_url, rte_seq_no, rte_owner_ent_id, rte_create_usr_id, rte_create_timestamp, rte_upd_usr_id, rte_upd_timestamp)
SELECT rte_title_xml, 'STAFF', rte_get_xsl, rte_exe_xsl, rte_dl_xsl, rte_meta_data_url, rte_seq_no, rte_owner_ent_id, rte_create_usr_id, GETDATE(), rte_upd_usr_id, GETDATE() FROM ReportTemplate WHERE rte_type = 'LEARNER';

alter table ReportSpec add rsp_content ntext
go

/*
Auth: Tim
Date: 2008-08-02
Desc: create table  aeTreeNodeRelation
*/
create table aeTreeNodeRelation
(
tnr_child_tnd_id int not null,
tnr_ancestor_tnd_id int not null,
tnr_type nvarchar(20) not null,
tnr_order int not null,
tnr_parent_ind int not null,
tnr_remain_on_syn int not null,
tnr_create_timestamp datetime not null,
tnr_create_usr_id nvarchar(20) not null
)
ALTER TABLE aeTreeNodeRelation ADD CONSTRAINT
	PK_tnr PRIMARY KEY CLUSTERED
	(
	tnr_child_tnd_id,
	tnr_ancestor_tnd_id
	)
ALTER TABLE aeTreeNodeRelation WITH NOCHECK ADD CONSTRAINT
	FK_tnr_ent1 FOREIGN KEY
	(
	tnr_child_tnd_id
	) REFERENCES aeTreeNode
	(
	tnd_id
	)
ALTER TABLE aeTreeNodeRelation WITH NOCHECK ADD CONSTRAINT
	FK_tnr_ent2 FOREIGN KEY
	(
	tnr_ancestor_tnd_id
	) REFERENCES aeTreeNode
	(
	tnd_id
	)
CREATE
  INDEX IX_tnr1 ON aeTreeNodeRelation (tnr_ancestor_tnd_id)

CREATE
  INDEX IX_tnr2 ON aeTreeNodeRelation (tnr_parent_ind)
GO

/*
Auth: Tim
Date: 2008-08-05
Desc: for study group
*/
create table studyGroup
(
sgp_id int IDENTITY (1,1) not null ,
sgp_tcr_id int not null,
sgp_title  nvarchar(100) not null,
sgp_desc nvarchar(255),
sgp_public_type int not null,
sgp_create_usr_id nvarchar(20) not null,
sgp_create_timestamp datetime not null,
sgp_upd_timestamp datetime not null
)
ALTER TABLE studyGroup ADD CONSTRAINT
	PK_sgp PRIMARY KEY CLUSTERED
	(
	sgp_id
	)
ALTER TABLE studyGroup WITH NOCHECK ADD CONSTRAINT
	FK_tc_id FOREIGN KEY
	(
	sgp_tcr_id
	) REFERENCES tcTrainingCenter
	(
	tcr_id
	)

create table studyGroupMember
(
sgm_id  int IDENTITY (1,1) not null,
sgm_sgp_id int not null,
sgm_ent_id  int not null,
sgm_type  nvarchar(20) not null,
sgm_status  nvarchar(20) not null,
sgm_create_timestamp datetime not null,
sgm_create_usr_id nvarchar(20) not null,
sgm_upd_timestamp datetime not null,
sgm_upd_usr_id nvarchar(20) not null

)
ALTER TABLE studyGroupMember ADD CONSTRAINT
	PK_sgm PRIMARY KEY CLUSTERED
	(
	sgm_id
	)
ALTER TABLE studyGroupMember WITH NOCHECK ADD CONSTRAINT
	FK_sgp_id FOREIGN KEY
	(
	sgm_sgp_id
	) REFERENCES studyGroup
	(
	sgp_id
	)

CREATE
  INDEX IX_sgm_ent_id ON studyGroupMember (sgm_ent_id )
CREATE
  INDEX IX_sgm_type ON studyGroupMember (sgm_type )
CREATE
  INDEX IX_sgm_status ON studyGroupMember (sgm_status )

create table studyGroupRelation
(
sgr_sgp_id int  not null ,
sgr_ent_id  int not null,
sgr_type   nvarchar(20) not null,
sgr_create_timestamp datetime not null,
sgr_create_usr_id nvarchar(20) not null

)
ALTER TABLE studyGroupRelation ADD CONSTRAINT
	PK_sgr PRIMARY KEY CLUSTERED
	(
	sgr_sgp_id,
	sgr_ent_id,
	sgr_type
	)
ALTER TABLE studyGroupRelation WITH NOCHECK ADD CONSTRAINT
	FK_sgr_sgp_id FOREIGN KEY
	(
	sgr_sgp_id
	) REFERENCES studyGroup
	(
	sgp_id
	)

create table studyGroupResources
(
sgs_id   int IDENTITY (1,1) not null,
sgs_title  nvarchar(50) not null,
sgs_type  nvarchar(20) not null,
sgs_content   nvarchar(255) ,
sgs_desc   nvarchar(255) ,
sgs_create_timestamp datetime not null,
sgs_create_usr_id nvarchar(20) not null,
sgs_upd_timestamp  datetime not null,
sgs_upd_usr_id  nvarchar(20) not null

)
ALTER TABLE studyGroupResources ADD CONSTRAINT
	PK_sgs PRIMARY KEY CLUSTERED
	(
	sgs_id
	)
GO

/*
Auth: Tim
Date: 2008-08-12
Desc: for study group forum
*/

alter table Module add mod_sgp_ind int  DEFAULT 0
go
update Module set mod_sgp_ind=0
go

/*
Auth: Dean
Date: 2008-07-31
Desc: module,know
*/
--table knowCatalog
CREATE TABLE knowCatalog (
	kca_id int IDENTITY (1, 1) NOT NULL ,
	kca_tcr_id int NOT NULL ,
	kca_code nvarchar (50) NULL ,
	kca_title nvarchar (255) NOT NULL ,
	kca_type nvarchar (50) NOT NULL ,
	kca_public_ind int NOT NULL ,
	kca_que_count int NULL ,
	kca_create_usr_id nvarchar (20)  NOT NULL ,
	kca_create_timestamp datetime NOT NULL ,
	kca_update_usr_id nvarchar (20)  NOT NULL ,
	kca_update_timestamp datetime NOT NULL
)
GO

ALTER TABLE knowCatalog ADD
	CONSTRAINT PK_knowCatalog PRIMARY KEY  CLUSTERED
	(
		kca_id
	)
GO

 CREATE  INDEX IX_kca_tcr_code_title ON knowCatalog(kca_tcr_id, kca_code, kca_title)
GO

CREATE  INDEX IX_kca_type_public ON knowCatalog(kca_type, kca_public_ind)
go

ALTER TABLE knowCatalog ADD
	CONSTRAINT FK_knowCatalog_tcTrainingCenter FOREIGN KEY
	(
		kca_tcr_id
	) REFERENCES tcTrainingCenter (
		tcr_id
	)
GO

--the table of question
CREATE TABLE knowQuestion (
	que_id int IDENTITY (1, 1) NOT NULL ,
	que_kca_id int NOT NULL ,
	que_title nvarchar (255) NOT NULL ,
	que_content ntext NULL ,
	que_answered_ind int NOT NULL ,
	que_answered_timestamp datetime NULL ,
	que_popular_ind int NOT NULL ,
	que_popular_timestamp datetime NULL ,
	que_reward_credits int NOT NULL ,
	que_status nvarchar (20) NOT NULL ,
	que_create_ent_id int NOT NULL ,
	que_create_timestamp datetime NOT NULL ,
	que_update_ent_id int NOT NULL ,
	que_update_timestamp datetime NOT NULL
)
GO

ALTER TABLE knowQuestion  ADD
	CONSTRAINT PK_knowQuestion PRIMARY KEY  CLUSTERED
	(
		que_id
	)
GO

 CREATE  INDEX IX_knowQuestion_kca_id ON knowQuestion(que_kca_id)
GO

 CREATE  INDEX IX_knowQuestion_title ON knowQuestion(que_title)
GO

ALTER TABLE knowQuestion ADD
	CONSTRAINT FK_kque_kca1 FOREIGN KEY
	(
		que_kca_id
	) REFERENCES knowCatalog (
		kca_id
	)
GO

--the table of answer
CREATE TABLE knowAnswer (
	ans_id int IDENTITY (1, 1) NOT NULL ,
	ans_que_id int NOT NULL ,
	ans_content ntext NOT NULL ,
	ans_refer_content nvarchar(510) NULL,
	ans_right_ind int NOT NULL ,
	ans_vote_total int NULL ,
	ans_vote_for int NULL ,
	ans_vote_down int NULL ,
	ans_temp_vote_total int NULL,
	ans_temp_vote_for int NULL,
	ans_temp_vote_for_down_diff int NULL,
	ans_status nvarchar (20) NOT NULL ,
	ans_create_ent_id int NOT NULL ,
	ans_create_timestamp datetime NOT NULL ,
	ans_update_ent_id int NOT NULL ,
	ans_update_timestamp datetime NOT NULL
)
GO

ALTER TABLE knowAnswer ADD
	CONSTRAINT PK_knowAnswer PRIMARY KEY  CLUSTERED
	(
		ans_id
	)
GO

 CREATE  INDEX IX_knowAnswer_que_id ON knowAnswer(ans_que_id)
GO

ALTER TABLE knowAnswer ADD
	CONSTRAINT FK_kans_kque1 FOREIGN KEY
	(
		ans_que_id
	) REFERENCES knowQuestion (
		que_id
	),
	CONSTRAINT FK_kans_usr FOREIGN KEY
	(
		ans_create_ent_id
	) REFERENCES RegUser (
		usr_ent_id
	)
GO

--table creditsType
CREATE TABLE creditsType (
	cty_id int IDENTITY (1, 1) NOT NULL ,
	cty_code nvarchar (20) NOT NULL ,
	cty_title nvarchar (100) NOT NULL ,
	cty_deduction_ind int NOT NULL ,
	cty_manual_ind int NOT NULL ,
	cty_deleted_ind int NOT NULL ,
	cty_relation_total_ind int NOT NULL ,
	cty_relation_type nvarchar (10) NULL ,
	cty_default_credits_ind int NOT NULL ,
	cty_default_credits int NOT NULL ,
	cty_create_usr_id nvarchar (20) NOT NULL ,
	cty_create_timestamp datetime NOT NULL
)
GO

ALTER TABLE creditsType ADD
	CONSTRAINT PK_creditsType PRIMARY KEY  CLUSTERED
	(
		cty_id
	)
GO

 CREATE  INDEX IX_creditsType_complex_1 ON creditsType(cty_deduction_ind, cty_manual_ind, cty_deleted_ind, cty_relation_total_ind, cty_relation_type)
GO

--initialization credit
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp)
VALUES('ZD_INIT','ZD_INIT',0,0,0,0,'ZD',1,50,'s1u3',GETDATE());

--the credit that respondent can get, when he has answered one question.
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp)
VALUES('ZD_COMMIT_ANS','ZD_COMMIT_ANS',0,0,0,0,'ZD',1,2,'s1u3',GETDATE());

--the credit that respondent can get, when his answer has been set optimal answer.
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp)
VALUES('ZD_RIGHT_ANS','ZD_RIGHT_ANS',0,0,0,0,'ZD',1,20,'s1u3',GETDATE());

--the credit that user can get, when he has cancelled his question.
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp)
VALUES('ZD_CANCEL_QUE','ZD_CANCEL_QUE',0,0,0,0,'ZD',1,5,'s1u3',GETDATE());

--the credit that requestor can get, when he has choosed the optimal answer.
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp)
VALUES('ZD_CHOOSE_ANS','ZD_CHOOSE_ANS',0,0,0,0,'ZD',1,5,'s1u3',GETDATE());

--the credit that requestor can get, when he submits one question.
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp)
VALUES('ZD_NEW_QUE','ZD_NEW_QUE',1,0,0,0,'ZD',1,-5,'s1u3',GETDATE());

--table userCreditsDetail
CREATE TABLE userCreditsDetail (
	ucd_ent_id int NOT NULL ,
	ucd_cty_id int NOT NULL ,
	ucd_itm_id int NULL ,
	ucd_total int NULL ,
	ucd_hit int NULL ,
	ucd_hit_temp int NULL ,
	ucd_create_timestamp datetime NOT NULL ,
	ucd_create_usr_id nvarchar (20) NOT NULL ,
	ucd_update_timestamp datetime NOT NULL ,
	ucd_update_usr_id nvarchar (20) NOT NULL
)
GO

ALTER TABLE userCreditsDetail ADD
	CONSTRAINT PK_userCreditsDetail PRIMARY KEY  CLUSTERED
	(
		ucd_ent_id,
		ucd_cty_id
	)
GO

 CREATE  INDEX IX_userCreditsDetail ON userCreditsDetail(ucd_ent_id)
GO

 CREATE  INDEX IX_cty_ent_itm_1 ON userCreditsDetail(ucd_ent_id, ucd_cty_id, ucd_itm_id)
GO

ALTER TABLE userCreditsDetail ADD
	CONSTRAINT FK_ucd_cty_1 FOREIGN KEY
	(
		ucd_cty_id
	) REFERENCES creditsType (
		cty_id
	),
	CONSTRAINT FK_ucd_usr_1 FOREIGN KEY
	(
		ucd_ent_id
	) REFERENCES RegUser (
		usr_ent_id
	)
GO

--table userCredits
CREATE TABLE userCredits (
	uct_ent_id int NOT NULL ,
	uct_total int NULL ,
	uct_update_timestamp datetime NOT NULL ,
	uct_zd_total int NULL
)
GO

ALTER TABLE userCredits ADD
	CONSTRAINT PK_userCredits PRIMARY KEY  CLUSTERED
	(
		uct_ent_id
	)
GO

 CREATE  INDEX IX_uct_total ON userCredits(uct_total)
GO

 CREATE  INDEX IX_uct_zd_total ON userCredits(uct_zd_total)
GO

ALTER TABLE userCredits ADD
	CONSTRAINT FK_userCredits_RegUser FOREIGN KEY
	(
		uct_ent_id
	) REFERENCES RegUser (
		usr_ent_id
	)
GO

--table knowCatalogRelation
CREATE TABLE knowCatalogRelation (
	kcr_child_kca_id int NOT NULL ,
	kcr_ancestor_kca_id int NOT NULL ,
	kcr_type nvarchar (50) NOT NULL ,
	kcr_order int NOT NULL ,
	kcr_parent_ind int NOT NULL ,
	kcr_syn_timestamp datetime NULL ,
	kcr_remain_on_syn int NULL ,
	kcr_create_usr_id nvarchar (20) NOT NULL ,
	kcr_create_timestamp datetime NOT NULL
)
GO

ALTER TABLE knowCatalogRelation ADD
	CONSTRAINT PK_knowCatalogRelation PRIMARY KEY  CLUSTERED
	(
		kcr_child_kca_id,
		kcr_ancestor_kca_id,
		kcr_type
	)
GO

 CREATE  INDEX IX_kcr_ancestor_parent_ind ON knowCatalogRelation(kcr_ancestor_kca_id, kcr_parent_ind)
GO

ALTER TABLE knowCatalogRelation ADD
	CONSTRAINT FK_kcr_kca_1 FOREIGN KEY
	(
		kcr_ancestor_kca_id
	) REFERENCES knowCatalog (
		kca_id
	)
GO

--table userCreditsDetailLog
CREATE TABLE userCreditsDetailLog (
	ucl_usr_ent_id int NOT NULL,
	ucl_bpt_id int NOT NULL,
	ucl_relation_type nvarchar (10) NULL,
	ucl_source_id int NULL,
	ucl_point int NOT NULL,
	ucl_create_timestamp datetime NOT NULL,
	ucl_create_usr_id nvarchar (20) NOT NULL
)
GO

ALTER TABLE userCreditsDetailLog ADD
	CONSTRAINT FK_ucl_cty_1 FOREIGN KEY
	(
		ucl_bpt_id
	) REFERENCES creditsType (
		cty_id
	),
	CONSTRAINT FK_ucl_usr_1 FOREIGN KEY
	(
		ucl_usr_ent_id
	) REFERENCES RegUser (
		usr_ent_id
	)
GO

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
	kvd_create_usr_id nvarchar (20)  NOT NULL ,
	kvd_create_timestamp datetime NOT NULL
)
GO

ALTER TABLE knowVoteDetail ADD
	CONSTRAINT PK_knowVoteDetail PRIMARY KEY  NONCLUSTERED
	(
		kvd_que_id,
		kvd_ans_id,
		kvd_ent_id
	)
GO

ALTER TABLE knowVoteDetail ADD
	CONSTRAINT FK_kvd_kans_1 FOREIGN KEY
	(
		kvd_ans_id
	) REFERENCES knowAnswer (
		ans_id
	),
	CONSTRAINT FK_kvd_kque_1 FOREIGN KEY
	(
		kvd_que_id
	) REFERENCES knowQuestion (
		que_id
	),
	CONSTRAINT FK_kvd_usr_1 FOREIGN KEY
	(
		kvd_ent_id
	) REFERENCES RegUser (
		usr_ent_id
	)
GO

--for access control
INSERT INTO acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
VALUES ('KNOW_MGT', 'FUNCTION', 'KNOW', '2008-08-22 00:00:00', null);

INSERT INTO acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_creation_timestamp)
SELECT rol_id, ftn_id , getDate() FROM acFunction, acRole
WHERE ftn_ext_id = 'KNOW_MGT' AND rol_ext_id LIKE 'TADM_%';
GO

/**
Auth: Tim
Date: 2008-08-15
Desc: 	  add for course muodule
*/
alter table aeItemLesson add ils_place nvarchar(200)

alter table Course add  cos_structure_json ntext

alter table CourseMeasurement add cmt_duration decimal(9,2),cmt_place nvarchar(510)

create table aeItemComments
(
	ict_itm_id int not null,
	ict_ent_id int not null,
	ict_tkh_id int not null,
	ict_score int not null,
	ict_comment nvarchar(510),
	ict_create_timestamp datetime not null
)

ALTER TABLE aeItemComments ADD CONSTRAINT
	PK_ict PRIMARY KEY CLUSTERED
	(
	ict_itm_id,
	ict_ent_id,
	ict_tkh_id
	)

ALTER TABLE aeItemComments WITH NOCHECK
	ADD CONSTRAINT
	FK_ict_itm_id FOREIGN KEY
	(
	ict_itm_id
	) REFERENCES aeItem
	(
	itm_id
	),
	CONSTRAINT
	FK_ict_ent_id FOREIGN KEY
	(
	ict_ent_id
	) REFERENCES reguser
	(
	usr_ent_id
	),
	CONSTRAINT
	FK_ict_tkh_id FOREIGN KEY
	(
	ict_tkh_id
	) REFERENCES TrackingHistory
	(
	tkh_id
	)

alter table aeItem add itm_comment_avg_score decimal(9,2),itm_comment_total_count int,itm_comment_total_score int
go
CREATE  INDEX IX_itm_comment_avg_score ON aeItem(itm_comment_avg_score)

CREATE  INDEX IX_itm_comment_total_count ON aeItem(itm_comment_total_count)

alter table aeApplication add app_note ntext

alter table CourseEvaluation add cov_progress decimal(9,2)
go

/**
Auth: canding
Date: 2008-08-26
Desc: 	  add tow fields to  aeItem  for Sequence of the course
*/
alter table aeItem add itm_publish_timestamp datetime null
go

/**
Auth: canding
Date: 2008-08-26
Desc: 	  add tow fields to  aeItem  for Sequence of the course
*/
alter table aeLearningSoln add lsn_start_datetime  datetime null
go
alter table aeLearningSoln add lsn_end_datetime datetime null
go

/**
Auth: Kim
Date: 2008-08-27
*/
ALTER TABLE aeItem ADD
	itm_srh_content ntext NULL
GO

/**
Auth: Terry
Date: 2008-08-29
*/
ALTER TABLE aeItem ADD
	itm_desc nvarchar(4000) NULL,
	itm_plan_code nvarchar(50) NULL,
	itm_icon nvarchar(50) NULL

GO

/**
Auth: Tim
Date: 2008-08-26
Desc: add for item type
*/

alter table aeItemType
add
ity_exam_ind int  DEFAULT 0 not null,
ity_blend_ind int  DEFAULT 0 not null,
ity_ref_ind int  DEFAULT 0 not null

alter table aeItem
add
itm_exam_ind int  DEFAULT 0 ,
itm_blend_ind int  DEFAULT 0,
itm_ref_ind int  DEFAULT 0
go

update aeItemType
set ity_exam_ind=0,ity_blend_ind=0,ity_ref_ind=0
update aeItem
set itm_exam_ind=0,itm_blend_ind=0,itm_ref_ind=0

update aeItemType set ity_ref_ind = 1 where ity_id in ('BOOK','AUDIOVIDEO','WEBSITE')
update aeItem set itm_ref_ind = 1 where itm_type in ('BOOK','AUDIOVIDEO','WEBSITE')

alter table aeItemType  drop
CONSTRAINT PK_aeItemType

alter table aeItemType  ADD
CONSTRAINT PK_aeItemType  PRIMARY KEY CLUSTERED
(
	ity_owner_ent_id,
	ity_id,
	ity_run_ind,
	ity_exam_ind ,
	ity_blend_ind ,
	ity_ref_ind
)
go

update aeItemType
set ity_seq_id=1
where ity_id='SELFSTUDY' and ity_run_ind=0

update aeItemType
set ity_seq_id=2
where ity_id='CLASSROOM' and ity_run_ind=0

update aeItemType
set ity_seq_id=10
where ity_id='CLASSROOM' and ity_run_ind=1

update aeItemType
set ity_seq_id=7
where ity_id='BOOK' and ity_run_ind=0

update aeItemType
set ity_seq_id=8
where ity_id='AUDIOVIDEO' and ity_run_ind=0

update aeItemType
set ity_seq_id=9
where ity_id='WEBSITE' and ity_run_ind=0

insert into aeItemType
select ity_owner_ent_id,ity_id,ity_run_ind,3,
  '',null,getDate(),'slu3',null,ity_create_run_ind,ity_apply_ind,ity_qdb_ind,
  0,null,ity_certificate_ind,0,0,ity_has_attendance_ind,ity_ji_ind,ity_completion_criteria_ind,
  0,ity_target_method,0,ity_tkh_method,0,1,0
from aeItemType where ity_id='CLASSROOM' and ity_run_ind=0

insert into aeItemType
select ity_owner_ent_id,ity_id,ity_run_ind,11,
  '',null,getDate(),'slu3',null,ity_create_run_ind,ity_apply_ind,ity_qdb_ind,
  0,null,ity_certificate_ind,0,0,ity_has_attendance_ind,ity_ji_ind,ity_completion_criteria_ind,
  0,ity_target_method,0,ity_tkh_method,0,1,0
from aeItemType where ity_id='CLASSROOM' and ity_run_ind=1

insert into aeItemType
select ity_owner_ent_id,ity_id,ity_run_ind,4,
  '',null,getDate(),'slu3',null,ity_create_run_ind,ity_apply_ind,ity_qdb_ind,
  0,null,ity_certificate_ind,0,0,ity_has_attendance_ind,ity_ji_ind,ity_completion_criteria_ind,
  0,ity_target_method,0,ity_tkh_method,1,0,0
from aeItemType where ity_id='SELFSTUDY'

insert into aeItemType
select ity_owner_ent_id,ity_id,ity_run_ind,5,
  '',null,getDate(),'slu3',null,ity_create_run_ind,ity_apply_ind,ity_qdb_ind,
  0,null,ity_certificate_ind,0,0,ity_has_attendance_ind,ity_ji_ind,ity_completion_criteria_ind,
  0,ity_target_method,0,ity_tkh_method,1,0,0
from aeItemType where ity_id='CLASSROOM' and ity_run_ind=0 and ity_seq_id=2

insert into aeItemType
select ity_owner_ent_id,ity_id,ity_run_ind,12,
  '',null,getDate(),'slu3',null,ity_create_run_ind,ity_apply_ind,ity_qdb_ind,
  0,null,ity_certificate_ind,0,0,ity_has_attendance_ind,ity_ji_ind,ity_completion_criteria_ind,
  0,ity_target_method,0,ity_tkh_method,1,0,0
from aeItemType where ity_id='CLASSROOM' and ity_run_ind=1  and ity_seq_id=10

insert into aeItemType
select ity_owner_ent_id,ity_id,ity_run_ind,6,
  '',null,getDate(),'slu3',null,ity_create_run_ind,ity_apply_ind,ity_qdb_ind,
  0,null,ity_certificate_ind,0,0,ity_has_attendance_ind,ity_ji_ind,ity_completion_criteria_ind,
  0,ity_target_method,0,ity_tkh_method,1,1,0
from aeItemType where ity_id='CLASSROOM' and ity_run_ind=0  and ity_seq_id=2

insert into aeItemType
select ity_owner_ent_id,ity_id,ity_run_ind,13,
  '',null,getDate(),'slu3',null,ity_create_run_ind,ity_apply_ind,ity_qdb_ind,
  0,null,ity_certificate_ind,0,0,ity_has_attendance_ind,ity_ji_ind,ity_completion_criteria_ind,
  0,ity_target_method,0,ity_tkh_method,1,1,0
from aeItemType where ity_id='CLASSROOM' and ity_run_ind=1  and ity_seq_id=10

insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('ITM_EXAM_MAIN', 'FUNCTION', 'HOMEPAGE', getDate(), null)

insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('ITM_REF_MAIN', 'FUNCTION', 'HOMEPAGE', getDate(), null)

INSERT INTO acRoleFunction
select rol_id, ftn_id, getdate() from acRole, acFunction where rol_ste_uid in ('TADM') and ftn_ext_id = 'ITM_EXAM_MAIN'

INSERT INTO acRoleFunction
select rol_id, ftn_id, getdate() from acRole, acFunction where rol_ste_uid in ('TADM') and ftn_ext_id = 'ITM_REF_MAIN'

INSERT INTO acHomepage
select null, rol_ext_id, 'ITM_EXAM_MAIN', 's1u3', getdate() from acRole where rol_ste_uid in ('TADM')

INSERT INTO acHomepage
select null, rol_ext_id, 'ITM_REF_MAIN', 's1u3', getdate() from acRole where rol_ste_uid in ('TADM')

delete from acHomepage where ac_hom_ftn_ext_id='RUN_MAIN'

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
values ('STUDY_GROUP_MAIN', 'FUNCTION', 'HOMEPAGE', getDate(), null)

INSERT INTO acRoleFunction
select rol_id, ftn_id, getdate() from acRole, acFunction where rol_ste_uid in ('TADM') and ftn_ext_id = 'STUDY_GROUP_MAIN'

INSERT INTO acHomepage
select null, rol_ext_id, 'STUDY_GROUP_MAIN', 's1u3', getdate() from acRole where rol_ste_uid in ('TADM')

/**
Auth: Elvea
Date: 2008-09-02
Desc: Know
*/
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('KNOW_MAIN', 'FUNCTION', 'HOMEPAGE', getDate(), null);

INSERT INTO acRoleFunction
select rol_id, ftn_id, getdate() from acRole, acFunction where rol_ste_uid in ('TADM') and ftn_ext_id = 'KNOW_MAIN';

INSERT INTO acHomepage
select null, rol_ext_id, 'KNOW_MAIN', 's1u3', getdate() from acRole where rol_ste_uid in ('TADM');

/**
Auth: canding
Date: 2008-09-04
Desc: change the user file
*/
alter table regUser add  usr_nickname nvarchar(20) null;
alter table ReguserExtension add  urx_extra_41  nvarchar(500) null
alter table ReguserExtension add  urx_extra_42  nvarchar(500) null
alter table ReguserExtension add  urx_extra_43  nvarchar(500) null
alter table ReguserExtension add  urx_extra_44  nvarchar(500) null
alter table ReguserExtension add  urx_extra_45  nvarchar(500) null
go

/**
Auth: Tim
Date: 2008-09-03
Desc: add for cmSkillEntity
*/
create table cmSkillEntity
(
ske_id int  IDENTITY (-1,1) not null,
ske_type nvarchar(20) not null,
)

ALTER TABLE cmSkillEntity ADD CONSTRAINT
	PK_skt PRIMARY KEY CLUSTERED
	(
	ske_id
	)

alter table cmSkillBase add skb_ske_id int
ALTER TABLE cmSkillBase ADD
	CONSTRAINT FK_skb_ske_id FOREIGN KEY
	(
		skb_ske_id
	) REFERENCES cmSkillEntity (
		ske_id
	)
alter table cmSkillSet add sks_ske_id int
ALTER TABLE cmSkillSet ADD
	CONSTRAINT FK_sks_ske_id FOREIGN KEY
	(
		sks_ske_id
	) REFERENCES cmSkillEntity (
		ske_id
	)
go
insert into cmSkillEntity values('SYS_UNSPECIFIED');
insert into cmSkillEntity values('SYS_ALL');

alter table aeItemTargetRule add itr_skill_id nvarchar(2000)

alter table aeItemTargetRuleDetail add ird_skill_id int default -1 not null
alter table aeItemTargetRuleDetail drop CONSTRAINT PK_ird
ALTER TABLE aeItemTargetRuleDetail ADD CONSTRAINT
	PK_ird PRIMARY KEY CLUSTERED
	(
		ird_itm_id,
		ird_itr_id,
		ird_group_id,
		ird_grade_id,
		ird_skill_id
	)
go

create table RegUserSkillSet
(
uss_ent_id  int  not null,
uss_ske_id  int not null
)
go

ALTER TABLE RegUserSkillSet ADD CONSTRAINT
 PK_ent_ske PRIMARY KEY CLUSTERED
(uss_ent_id,
uss_ske_id)
 go

ALTER TABLE RegUserSkillSet WITH NOCHECK
ADD CONSTRAINT
	FK_uss_usr_id FOREIGN KEY
	(
		uss_ent_id
	) REFERENCES RegUser
	(
	usr_ent_id
	)
	go

ALTER TABLE RegUserSkillSet WITH NOCHECK
ADD CONSTRAINT
	FK_uss_ske_id FOREIGN KEY
	(
		uss_ske_id
	) REFERENCES cmSkillEntity
	(
	ske_id
	)
	go

/*
Auth: Dean
Date: 2008-09-05
Desc: for module: public evaluation survey
*/
insert into acRoleFunction (rfn_rol_id,rfn_ftn_id,rfn_creation_timestamp)
select rol_id, ftn_id , '2008-09-05 00:00:00' from acFunction, acRole where ftn_ext_id = 'EVN_MAIN' and rol_ext_id like 'TADM_%'

insert into acRoleFunction (rfn_rol_id,rfn_ftn_id,rfn_creation_timestamp)
select rol_id, ftn_id , '2008-09-05 00:00:00' from acFunction, acRole where ftn_ext_id = 'EVN_LIST' and rol_ext_id like 'NLRN_%'

insert into acHomePage (ac_hom_rol_ext_id,ac_hom_ftn_ext_id, ac_hom_create_usr_id, ac_hom_create_timestamp)
select rol_ext_id, 'EVN_LIST', 's1u3', '2008-09-05 00:00:00' from acRole where rol_ext_id like 'NLRN_%';

insert into acHomePage (ac_hom_rol_ext_id,ac_hom_ftn_ext_id, ac_hom_create_usr_id, ac_hom_create_timestamp)
select rol_ext_id, 'EVN_MAIN', 's1u3', '2008-09-05 00:00:00' from acRole where rol_ext_id like 'TADM_%';

CREATE TABLE EvalAccess (
	eac_res_id int NOT NULL ,
	eac_target_ent_id int NOT NULL ,
	eac_order int NOT NULL ,
	eac_create_timestamp datetime NOT NULL ,
	eac_create_usr_id nvarchar (20) NOT NULL ,
	CONSTRAINT PK_EvalAccess PRIMARY KEY  CLUSTERED
	(
		eac_res_id,
		eac_target_ent_id
	)
)
GO

CREATE TABLE moduleTrainingCenter (
	mtc_mod_id int NOT NULL,
	mtc_tcr_id int NOT NULL,
	mtc_create_timestamp datetime NOT NULL,
	mtc_create_usr_id nvarchar(20) NOT NULL
)
GO

ALTER TABLE moduleTrainingCenter WITH NOCHECK ADD
	CONSTRAINT PK_moduleTrainingCenter PRIMARY KEY CLUSTERED
	(
		mtc_mod_id,
		mtc_tcr_id
	)
GO

ALTER TABLE moduleTrainingCenter ADD
	CONSTRAINT FK_mtc_mod_1 FOREIGN KEY
	(
		mtc_mod_id
	) REFERENCES Module (
		mod_res_id
	),
	CONSTRAINT FK_mtc_tcr_1 FOREIGN KEY
	(
		mtc_tcr_id
	) REFERENCES tcTrainingCenter (
		tcr_id
	)
go

/**
Auth: Tim
Date: 2008-09-03
Desc: open CM_ASS_MAIN、CM_MAIN、CM_SKL_ANALYSIS
*/
-- CM related functions

insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_creation_timestamp)
select rol_id, ftn_id, '2008-09-03' from acRole, acFunction
where
(rol_ext_id like 'TADM_%' and ftn_ext_id in ('CM_ASS_MAIN','CM_MAIN','CM_SKL_ANALYSIS'))
or
(rol_ext_id like 'TADM_%' or rol_ext_id like 'ADM_%' or rol_ext_id like 'NLRN_%') and ftn_ext_id in ('TO_DO_LIST')

insert into acHomePage (ac_hom_rol_ext_id, ac_hom_ftn_ext_id, ac_hom_create_usr_id, ac_hom_create_timestamp)
select rol_ext_id, ftn_ext_id, 's1u3', '2008-09-03' from acRole, acFunction
where
(rol_ext_id like 'TADM_%' and ftn_ext_id in ('CM_ASS_MAIN','CM_MAIN','CM_SKL_ANALYSIS'))
or
(rol_ext_id like 'TADM_%' or rol_ext_id like 'ADM_%' or rol_ext_id like 'NLRN_%') and ftn_ext_id in ('TO_DO_LIST')

/*
Auth: Jacky xiao
Date: 2008-09-05
Desc: add function to management training plan
*/
Insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('TRA_PLA_MGT', 'FUNCTION', 'HOMEPAGE', getDate(), NULL);

insert into acrolefunction
select rol_id, ftn_id , getDate() from acFunction, acRole where ftn_ext_id = 'TRA_PLA_MGT' and rol_ste_uid in ('TADM');

/* PLAN_CARRY_OUT */
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('PLAN_CARRY_OUT', 'FUNCTION', 'HOMEPAGE', getDate(), null);

INSERT INTO acRoleFunction
select rol_id, ftn_id, getdate() from acRole, acFunction where rol_ste_uid in ('TADM')
and ftn_ext_id = 'PLAN_CARRY_OUT';

INSERT INTO acHomepage
select null, rol_ext_id, 'PLAN_CARRY_OUT', 's1u3', getdate() from acRole where rol_ste_uid in ('TADM');
/* YEAR_PALN */
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('YEAR_PALN', 'FUNCTION', 'HOMEPAGE', getDate(), null);

INSERT INTO acRoleFunction
select rol_id, ftn_id, getdate() from acRole, acFunction where rol_ste_uid in ('TADM')
and ftn_ext_id = 'YEAR_PALN';

INSERT INTO acHomepage
select null, rol_ext_id, 'YEAR_PALN', 's1u3', getdate() from acRole where rol_ste_uid in ('TADM');
/* MAKEUP_PLAN */
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('MAKEUP_PLAN', 'FUNCTION', 'HOMEPAGE', getDate(), null);

INSERT INTO acRoleFunction
select rol_id, ftn_id, getdate() from acRole, acFunction where rol_ste_uid in ('TADM')
and ftn_ext_id = 'MAKEUP_PLAN';

INSERT INTO acHomepage
select null, rol_ext_id, 'MAKEUP_PLAN', 's1u3', getdate() from acRole where rol_ste_uid in ('TADM');
/* YEAR_PLAN_APPR */
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('YEAR_PLAN_APPR', 'FUNCTION', 'HOMEPAGE', getDate(), null);

INSERT INTO acRoleFunction
select rol_id, ftn_id, getdate() from acRole, acFunction where rol_ste_uid in ('TADM')
and ftn_ext_id = 'YEAR_PLAN_APPR';

INSERT INTO acHomepage
select null, rol_ext_id, 'YEAR_PLAN_APPR', 's1u3', getdate() from acRole where rol_ste_uid in ('TADM');
/* MAKEUP_PLAN_APPR */
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('MAKEUP_PLAN_APPR', 'FUNCTION', 'HOMEPAGE', getDate(), null);

INSERT INTO acRoleFunction
select rol_id, ftn_id, getdate() from acRole, acFunction where rol_ste_uid in ('TADM')
and ftn_ext_id = 'MAKEUP_PLAN_APPR';

INSERT INTO acHomepage
select null, rol_ext_id, 'MAKEUP_PLAN_APPR', 's1u3', getdate() from acRole where rol_ste_uid in ('TADM');
/* YEAR_SETTING */
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('YEAR_SETTING', 'FUNCTION', 'HOMEPAGE', getDate(), null);

INSERT INTO acRoleFunction
select rol_id, ftn_id, getdate() from acRole, acFunction where rol_ste_uid in ('TADM')
and ftn_ext_id = 'YEAR_SETTING';

INSERT INTO acHomepage
select null, rol_ext_id, 'YEAR_SETTING', 's1u3', getdate() from acRole where rol_ste_uid in ('TADM');

/*
Auth: Jacky xiao
Date: 2008-09-05
Desc: new some tables for training plan management
*/
CREATE TABLE tpTrainingPlan
	(
	tpn_id int IDENTITY (1, 1) NOT NULL,
	tpn_tcr_id int NOT NULL,
	tpn_date datetime NOT NULL,
	tpn_code nvarchar(50) NOT NULL,
	tpn_name nvarchar(200) NULL,
	tpn_cos_type nvarchar(255) NOT NULL,
	tpn_tnd_title nvarchar(100) NULL,
	tpn_introduction ntext NULL,
	tpn_aim ntext NULL,
	tpn_target ntext NULL,
	tpn_responser nvarchar(255) NULL,
	tpn_lrn_count int NULL,
	tpn_duration ntext NULL,
	tpn_wb_start_date datetime NULL,
	tpn_wb_end_date datetime NULL,
	tpn_ftf_start_date datetime NULL,
	tpn_ftf_end_date datetime NULL,
	tpn_type nvarchar(20) NOT NULL,
	tpn_fee float NULL,
	tpn_remark ntext NULL,
	tpn_status nvarchar(20) NOT NULL,
	tpn_approve_usr_id nvarchar(20) NULL,
	tpn_approve_timestamp datetime NULL,
	tpn_create_usr_id nvarchar(20) NOT NULL,
	tpn_create_timestamp datetime NOT NULL,
	tpn_update_usr_id nvarchar(20) NOT NULL,
	tpn_update_timestamp datetime NOT NULL,
	tpn_submit_usr_id nvarchar(20) NULL,
	tpn_submit_timestamp datetime NULL
	)
GO
ALTER TABLE tpTrainingPlan ADD CONSTRAINT
	PK_tpTrainingPlan PRIMARY KEY
	(
	tpn_id
	)

GO

CREATE TABLE tpYearPlan
	(
	ypn_year int NOT NULL,
	ypn_tcr_id int NOT NULL,
	ypn_file_name nvarchar(255) NOT NULL,
	ypn_status nvarchar(20) NOT NULL,
	ypn_approve_usr_id nvarchar(20) NULL,
	ypn_approve_timestamp datetime NULL,
	ypn_submit_usr_id nvarchar(20) NULL,
	ypn_submit_timestamp datetime NULL,
	ypn_create_usr_id nvarchar(20) NULL,
	ypn_create_timestamp datetime NULL,
	ypn_update_usr_id nvarchar(20) NULL,
	ypn_update_timestamp datetime NULL
	)
GO
ALTER TABLE tpYearPlan ADD CONSTRAINT
	PK_tpYearPlan PRIMARY KEY
	(
	ypn_year,
	ypn_tcr_id
	)

GO

CREATE TABLE tpYearSetting
	(
	ysg_tcr_id int NOT NULL,
	ysg_year int NOT NULL,
	ysg_child_tcr_id_lst nvarchar(4000) NOT NULL,
	ysg_submit_start_datetime datetime NOT NULL,
	ysg_submit_end_datetime datetime NOT NULL,
	ysg_create_timestamp datetime NOT NULL,
	ysg_create_usr_id nvarchar(20) NOT NULL,
	ysg_update_timestamp datetime NOT NULL,
	ysg_update_usr_id nvarchar(20) NOT NULL
	)
GO
ALTER TABLE tpYearSetting ADD CONSTRAINT
	PK_tpYearSetting PRIMARY KEY
	(
	ysg_tcr_id,
	ysg_year
	)

GO

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

alter table cmAssessment  alter column  asm_title nvarchar(100);
go

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
and res.res_id = sgr_ent_id;

/**
Auth: Kim
Date: 2008-12-11
Desc: Bug 5879 SA："指派功能"中有的功能"可选功能"中没有
*/
insert into acRoleFunction(rfn_rol_id, rfn_ftn_id, rfn_creation_timestamp)
select rol_id, ftn_id, '2008-12-11'
from acRole, acFunction
where rol_ste_uid='SADM' and ftn_ext_id='APPR_APP_LIST';

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
--将knowQuestion表中que_content字段类型由ntext改为nvarchar(500)
alter table knowquestion add que_content_bak nvarchar(500);
go
update knowquestion set que_content_bak=cast(que_content as NVARCHAR (500));
alter table knowquestion drop column que_content;
EXEC sp_rename 'knowquestion.que_content_bak','que_content','column';

--在表knowAnswer中增加ans_content对应的搜索字段ans_content_search
ALTER TABLE knowAnswer ADD ans_content_search ntext NOT NULL DEFAULT '';
GO

--将ans_content字段中的值复制到ans_content_search字段中(注：由于SQL2000中模糊查询中不区分大小写，故直接复制)
UPDATE knowAnswer
SET ans_content_search = ans_content
WHERE ans_content_search IS NULL AND ans_content IS NOT NULL;
GO


/**
Auth: Tim
Date: 2009-01-13
Desc: forget password  function
*/

create table usrPwdResetHis
(
   prh_id int not null IDENTITY (1, 1),
   prh_ent_id int not null,
   prh_ip nvarchar(50) not null,
   prh_status nvarchar(40) not null,
   prh_attempted int not null,
   prh_create_timestamp datetime not null

);

ALTER TABLE usrPwdResetHis ADD CONSTRAINT
	PK_prh PRIMARY KEY CLUSTERED
	(
	   prh_id
	);

ALTER TABLE usrPwdResetHis WITH NOCHECK ADD CONSTRAINT
	FK_prh_ent_id FOREIGN KEY
	(
	   prh_ent_id
	) REFERENCES regUser
	(
	   usr_ent_id
	);

insert into xslTemplate
values
('USER_PWD_RESET_NOTIFY', 'HTML', 'SMTP', null, 'usr_pwd_reset_notify.xsl', 'http://host:port/servlet/Dispatcher?module=JsonMod.user.UserModule&',0,null)

declare @xtp_id int
select @xtp_id = max(xtp_id) from xslTemplate
insert into xslparamname values (@xtp_id, 1, 'cmd')
insert into xslparamname values (@xtp_id, 2, 'sender_id')
insert into xslparamname values (@xtp_id, 3, 'sid')
insert into xslparamname values (@xtp_id, 4, 'usr_ent_id')


/*
Auth: Robin
Date: 2009-02-16
Desc: add a new role to manage the exam function.
*/
declare @rol_seq_id int
select @rol_seq_id = max(rol_seq_id) + 1 from acRole
INSERT INTO acRole (rol_ext_id,rol_seq_id,rol_ste_ent_id,rol_url_home,rol_creation_timestamp,rol_xml,rol_ste_default_ind,rol_report_ind,rol_skin_root,rol_status,rol_ste_uid,rol_target_ent_type,rol_auth_level, rol_tc_ind)
SELECT 'EXA_1', @rol_seq_id, 1, rol_url_home, getdate(), '', 0, 0, rol_skin_root, 'OK', 'EXA', null, 3, 0 FROM acRole WHERE rol_ext_id = 'TADM_1'

INSERT INTO acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('EXAM_ITEM_MAIN', 'FUNCTION', 'HOMEPAGE', getdate(), null);

INSERT INTO acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_creation_timestamp)
SELECT rol_id, ftn_id, getdate() FROM acRole, acFunction WHERE rol_ste_uid = 'EXA' AND ftn_ext_id = 'EXAM_ITEM_MAIN';

INSERT INTO acHomepage (ac_hom_ent_id,ac_hom_rol_ext_id,ac_hom_ftn_ext_id,ac_hom_create_usr_id,ac_hom_create_timestamp)
SELECT null, rol_ext_id, 'EXAM_ITEM_MAIN', 's1u3', getdate() FROM acRole WHERE rol_ste_uid = 'EXA';

INSERT INTO acHomePage (ac_hom_ent_id,ac_hom_rol_ext_id,ac_hom_ftn_ext_id,ac_hom_create_usr_id,ac_hom_create_timestamp)
SELECT null, rol_ext_id, 'USR_OWN_MAIN', 's1u3', getdate() FROM acRole WHERE rol_ste_uid = 'EXA';

INSERT INTO acHomePage (ac_hom_ent_id,ac_hom_rol_ext_id,ac_hom_ftn_ext_id,ac_hom_create_usr_id,ac_hom_create_timestamp)
SELECT null, rol_ext_id, 'USR_PWD_UPD', 's1u3', getdate() FROM acRole WHERE rol_ste_uid = 'EXA';

INSERT INTO acHomePage (ac_hom_ent_id,ac_hom_rol_ext_id,ac_hom_ftn_ext_id,ac_hom_create_usr_id,ac_hom_create_timestamp)
SELECT null, rol_ext_id, 'USR_OWN_PREFER', 's1u3', getdate() FROM acRole WHERE rol_ste_uid = 'EXA';


/*
Auth: Dean
Date: 2009-02-26
Desc: modify column "que_answered_ind" to "que_type"
*/
alter table knowQuestion add que_type nvarchar(20)
go

update knowQuestion set que_type = 'UNSOLVED' where que_answered_ind = 0;
update knowQuestion set que_type = 'SOLVED' where que_answered_ind = 1;

alter table knowQuestion drop column que_answered_ind
go

/*
Auth: Robin
Date: 2009-02-24
Desc: Test Paper Statistics Report
*/
if not exists (select * from ReportTemplate where rte_type = 'EXAM_PAPER_STAT')
begin
declare @rte_seq_no int
select @rte_seq_no = max(rte_seq_no) + 1 from ReportTemplate
INSERT INTO ReportTemplate( rte_title_xml, rte_type, rte_get_xsl, rte_exe_xsl, rte_dl_xsl, rte_meta_data_url,
              rte_seq_no, rte_owner_ent_id, rte_create_usr_id, rte_create_timestamp, rte_upd_usr_id, rte_upd_timestamp)
VALUES( 'XML', 'EXAM_PAPER_STAT', 'rpt_get_exam_paper_stat.xsl', 'rpt_exe_exam_paper_stat.xsl', 'rpt_dl_progress.xsl',
        NULL, @rte_seq_no, 1, 'slu3', getdate(), 'slu3', getdate())
end

INSERT INTO acReportTemplate (ac_rte_id,ac_rte_rol_ext_id,ac_rte_ftn_ext_id,ac_rte_owner_ind,ac_rte_create_usr_id,ac_rte_create_timestamp)
SELECT rte_id, 'TADM_1','RTE_READ', 0, 's1u3', getdate() FROM ReportTemplate WHERE rte_type = 'EXAM_PAPER_STAT';

if not exists(select * from ObjectView where ojv_type = 'EXAM_PAPER_STAT' and ojv_subtype = 'USR')
begin
INSERT INTO ObjectView(ojv_owner_ent_id, ojv_type, ojv_subtype, ojv_option_xml, ojv_create_usr_id, ojv_create_timestamp, ojv_update_usr_id, ojv_update_timestamp)
SELECT ste_ent_id, 'EXAM_PAPER_STAT', 'USR', 'XML', 'slu3', getdate(), 'slu3', getdate() FROM acSite
end

if not exists(select * from ObjectView where ojv_type = 'EXAM_PAPER_STAT' and ojv_subtype = 'ITM')
begin
INSERT INTO ObjectView(ojv_owner_ent_id, ojv_type, ojv_subtype, ojv_option_xml, ojv_create_usr_id, ojv_create_timestamp, ojv_update_usr_id, ojv_update_timestamp)
SELECT ste_ent_id, 'EXAM_PAPER_STAT', 'ITM', 'XML', 'slu3', getdate(), 'slu3', getdate() FROM acSite
end


/*
Auth: Joyce
Date: 2009-02-24
Desc: add Friendship Links function to System Administrator.
*/
insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('FS_LINK_MAIN', 'FUNCTION', 'HOMEPAGE', getdate(), null);

insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_creation_timestamp)
select rol_id, ftn_id,getdate() from acRole, acFunction
where rol_ext_id like 'ADM_%' and ftn_ext_id = 'FS_LINK_MAIN';

insert into acHomePage (ac_hom_rol_ext_id,ac_hom_ftn_ext_id, ac_hom_create_usr_id, ac_hom_create_timestamp)
select rol_ext_id, 'FS_LINK_MAIN', 's1u3', getdate() from acRole where rol_ext_id like 'ADM_%';

/*
Auth: Joyce
Date: 2009-02-24
Desc: add Friendship Link Table.
*/
CREATE TABLE FriendshipLink
	(
	fsl_id int NOT NULL IDENTITY (1, 1),
	fsl_title nvarchar(50) NOT NULL,
	fsl_url nvarchar(255) NOT NULL,
	fsl_status nvarchar(20) NOT NULL,
	fsl_create_usr_id nvarchar(20) NOT NULL,
	fsl_create_timestamp datetime NOT NULL,
	fsl_update_usr_id nvarchar(20) NOT NULL,
	fsl_update_timestamp datetime NOT NULL
	)
GO
ALTER TABLE FriendshipLink ADD CONSTRAINT
	PK_FriendshipLink PRIMARY KEY CLUSTERED
	(
	fsl_id
	)
GO


/*
Auth: Joyce
Date: 2009-03-05
Desc: add Poster Management function to System Administrator.
*/
insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('POSTER_MAIN', 'FUNCTION', 'HOMEPAGE', getdate(), null);

insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_creation_timestamp)
select rol_id, ftn_id,getdate() from acRole, acFunction
where rol_ext_id like 'ADM_%' and ftn_ext_id = 'POSTER_MAIN';

insert into acHomePage (ac_hom_rol_ext_id,ac_hom_ftn_ext_id, ac_hom_create_usr_id, ac_hom_create_timestamp)
select rol_ext_id, 'POSTER_MAIN', 's1u3', getdate() from acRole where rol_ext_id like 'ADM_%';

/*
Auth: Joyce
Date: 2009-03-05
Desc: add Poster Management Tabel.
*/
CREATE TABLE SitePoster (
	sp_ste_id int NOT NULL ,
	sp_media_file nvarchar(50) NULL ,
	sp_url nvarchar(255) NULL ,
	sp_status nvarchar(50) NULL
)
GO

ALTER TABLE SitePoster ADD CONSTRAINT
	PK_SitePoster PRIMARY KEY CLUSTERED
	(
	sp_ste_id
	)
GO

ALTER TABLE SitePoster ADD
	CONSTRAINT FK_SitePoster_acSite FOREIGN KEY
	(
		sp_ste_id
	) REFERENCES acSite (
		ste_ent_id
	)
GO


/*
Auth: Robin
Date: 2009-02-24
Desc: Training Expense Statistics Report
*/
if not exists (select * from ReportTemplate where rte_type = 'TRAIN_FEE_STAT')
begin
declare @rte_seq_no int
select @rte_seq_no = max(rte_seq_no) + 1 from ReportTemplate
INSERT INTO ReportTemplate( rte_title_xml, rte_type, rte_get_xsl, rte_exe_xsl, rte_dl_xsl, rte_meta_data_url,
              rte_seq_no, rte_owner_ent_id, rte_create_usr_id, rte_create_timestamp, rte_upd_usr_id, rte_upd_timestamp)
VALUES( 'XML', 'TRAIN_FEE_STAT', 'rpt_get_train_fee_stat.xsl', 'rpt_exe_train_fee_stat.xsl', 'rpt_dl_progress.xsl',
        NULL, @rte_seq_no, 1, 'slu3', getdate(), 'slu3', getdate())
end

INSERT INTO acReportTemplate (ac_rte_id,ac_rte_rol_ext_id,ac_rte_ftn_ext_id,ac_rte_owner_ind,ac_rte_create_usr_id,ac_rte_create_timestamp)
SELECT rte_id, 'TADM_1','RTE_READ', 0, 's1u3', getdate() FROM ReportTemplate WHERE rte_type = 'TRAIN_FEE_STAT';

if not exists(select * from ObjectView where ojv_type = 'TRAIN_FEE_STAT' and ojv_subtype = 'ITM')
begin
INSERT INTO ObjectView(ojv_owner_ent_id, ojv_type, ojv_subtype, ojv_option_xml, ojv_create_usr_id, ojv_create_timestamp, ojv_update_usr_id, ojv_update_timestamp)
SELECT ste_ent_id, 'TRAIN_FEE_STAT', 'ITM', 'XML', 'slu3', getdate(), 'slu3', getdate() FROM acSite
end


/*
Auth: Joyce
Date: 2009-03-09
Desc: add send email indicate for study group.
*/
ALTER TABLE studyGroup ADD
	sgp_send_email_ind int DEFAULT 0
GO

/*
Auth: Joyce
Date: 2009-03-09
Desc: add new xsl template for study group email.
*/
insert into xslTemplate
values
('JOIN_STUDYGROUP_REMINDER', 'HTML', 'SMTP', null, 'study_group_join_reminder.xsl', 'http://host:port/servlet/Dispatcher?module=JsonMod.studyGroup.StudyGroupModule&',1,null)

declare @xtp_id int
select @xtp_id = max(xtp_id) from xslTemplate
	insert into xslparamname values (@xtp_id, 1, 'cmd')
	insert into xslparamname values (@xtp_id, 2, 'ent_ids')
	insert into xslparamname values (@xtp_id, 3, 'sender_id')
	insert into xslparamname values (@xtp_id, 4, 'sgp_id')


/*
Auth: Robin
Date: 2009-03-09
Desc: Training Cost Statistics Report
*/
if not exists (select * from ReportTemplate where rte_type = 'TRAIN_COST_STAT')
begin
declare @rte_seq_no int
select @rte_seq_no = max(rte_seq_no) + 1 from ReportTemplate
INSERT INTO ReportTemplate( rte_title_xml, rte_type, rte_get_xsl, rte_exe_xsl, rte_dl_xsl, rte_meta_data_url,
              rte_seq_no, rte_owner_ent_id, rte_create_usr_id, rte_create_timestamp, rte_upd_usr_id, rte_upd_timestamp)
VALUES( 'XML', 'TRAIN_COST_STAT', 'rpt_get_train_cost_stat.xsl', 'rpt_exe_train_cost_stat.xsl', 'rpt_dl_progress.xsl',
        NULL, @rte_seq_no, 1, 'slu3', getdate(), 'slu3', getdate())
end

INSERT INTO acReportTemplate (ac_rte_id,ac_rte_rol_ext_id,ac_rte_ftn_ext_id,ac_rte_owner_ind,ac_rte_create_usr_id,ac_rte_create_timestamp)
SELECT rte_id, 'TADM_1','RTE_READ', 0, 's1u3', getdate() FROM ReportTemplate WHERE rte_type = 'TRAIN_COST_STAT';

if not exists(select * from ObjectView where ojv_type = 'TRAIN_COST_STAT' and ojv_subtype = 'OTHER')
begin
INSERT INTO ObjectView(ojv_owner_ent_id, ojv_type, ojv_subtype, ojv_option_xml, ojv_create_usr_id, ojv_create_timestamp, ojv_update_usr_id, ojv_update_timestamp)
SELECT ste_ent_id, 'TRAIN_COST_STAT', 'OTHER', 'XML', 'slu3', getdate(), 'slu3', getdate() FROM acSite
end


/*
Auth: Robin
Date: 2009-03-12
Desc: Facility Management
*/
declare @ftp_id int
select @ftp_id = max(ftp_id) + 1 from fmFacilityType
INSERT INTO fmFacilityType(ftp_id, ftp_title_xml, ftp_main_indc, ftp_parent_ftp_id, ftp_owner_ent_id) VALUES(@ftp_id, '', 0, 2, 1)

ALTER TABLE fmFacility ADD fac_fee decimal(18,2) NULL


/*
Auth: Robin
Date: 2009-03-13
Desc: Search Training Equipment Expense
*/
declare @ftn_id int
select @ftn_id = ftn_id from acFunction where ftn_ext_id = 'TC_MAIN_IN_TCR'
insert into acRoleFunction select rol_id, @ftn_id, getdate() from acRole where rol_ext_id like 'ADM_%';

if not exists (select * from ReportTemplate where rte_type = 'FM_FEE')
begin
declare @rte_seq_no int
select @rte_seq_no = max(rte_seq_no) + 1 from ReportTemplate
INSERT INTO ReportTemplate( rte_title_xml, rte_type, rte_get_xsl, rte_exe_xsl, rte_dl_xsl, rte_meta_data_url,
              rte_seq_no, rte_owner_ent_id, rte_create_usr_id, rte_create_timestamp, rte_upd_usr_id, rte_upd_timestamp)
VALUES( 'XML', 'FM_FEE', null, 'rpt_exe_fm_fee.xsl', 'rpt_dl_progress.xsl',
        NULL, @rte_seq_no, 1, 'slu3', getdate(), 'slu3', getdate())
end


/*
Auth: Dean
Date: 2009-03-19
Desc: module - survey evaluation
*/
alter table progressAttempt add atm_response_bil_ext nvarchar(255) null;


/*
Auth: Joyce
Date: 2009-03-30
Desc: 积分管理
*/
-- 给ADM添加“积分管理”权限
insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('CREDIT_SETTING_MAIN', 'FUNCTION', 'HOMEPAGE', getdate(), null);

insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_creation_timestamp)
select rol_id, ftn_id,getdate() from acRole, acFunction
where rol_ext_id like 'ADM_%' and ftn_ext_id = 'CREDIT_SETTING_MAIN';

insert into acHomePage (ac_hom_rol_ext_id,ac_hom_ftn_ext_id, ac_hom_create_usr_id, ac_hom_create_timestamp)
select rol_ext_id, 'CREDIT_SETTING_MAIN', 's1u3', getdate() from acRole where rol_ext_id like 'ADM_%';

-- 给TADM添加“积分管理”权限
insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('CREDIT_OTHER_MAIN', 'FUNCTION', 'HOMEPAGE', getdate(), null);

insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_creation_timestamp)
select rol_id, ftn_id,getdate() from acRole, acFunction
where rol_ext_id like 'TADM_%' and ftn_ext_id = 'CREDIT_OTHER_MAIN';

insert into acHomePage (ac_hom_rol_ext_id,ac_hom_ftn_ext_id, ac_hom_create_usr_id, ac_hom_create_timestamp)
select rol_ext_id, 'CREDIT_OTHER_MAIN', 's1u3', getdate() from acRole where rol_ext_id like 'TADM_%';

-- 给积分点类型新增字段
ALTER TABLE creditsType ADD
	cty_update_usr_id nvarchar(20) NOT NULL CONSTRAINT DF_creditsType_cty_update_usr_id DEFAULT N's1u3',
	cty_update_timestamp datetime NOT NULL CONSTRAINT DF_creditsType_cty_update_timestamp DEFAULT getdate(),
	cty_hit int NULL,
	cty_period nvarchar(20) NULL
GO

--修改知道积分点属性
UPDATE creditsType SET cty_relation_total_ind = 1 WHERE (cty_code = 'ZD_INIT');
UPDATE creditsType SET cty_relation_total_ind = 1, cty_period = 'MONTH', cty_hit = 10 WHERE (cty_code = 'ZD_COMMIT_ANS');
UPDATE creditsType SET cty_relation_total_ind = 1, cty_period = 'MONTH', cty_hit = 10 WHERE (cty_code = 'ZD_RIGHT_ANS');
UPDATE creditsType SET cty_relation_total_ind = 1, cty_period = 'MONTH', cty_hit = 10 WHERE (cty_code = 'ZD_CANCEL_QUE');
UPDATE creditsType SET cty_deduction_ind = 0, cty_relation_total_ind = 1, cty_default_credits =2 ,cty_period = 'MONTH', cty_hit = 10 WHERE (cty_code = 'ZD_NEW_QUE');
GO

--插入自动积分点
--用户非首次登录
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_period,cty_hit)
VALUES('SYS_NORMAL_LOGIN','SYS_NORMAL_LOGIN',0,0,0,1,'SYS',1,2,'s1u3',getdate(),'s1u3',getdate(),'DAY',2);

--更改个人资料
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_period,cty_hit)
VALUES('SYS_UPD_MY_PROFILE','SYS_UPD_MY_PROFILE',0,0,0,1,'SYS',1,5,'s1u3',getdate(),'s1u3',getdate(),'MONTH',2);

--参加公共调查问卷
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_period,cty_hit)
VALUES('SYS_SUBMIT_SVY','SYS_SUBMIT_SVY',0,0,0,1,'SYS',1,10,'s1u3',getdate(),'s1u3',getdate(),null,null);

--论坛发贴得分
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_period,cty_hit)
VALUES('SYS_INS_TOPIC','SYS_INS_TOPIC',0,0,0,1,'SYS',1,10,'s1u3',getdate(),'s1u3',getdate(),'MONTH',10);

--论坛回贴得分
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_period,cty_hit)
VALUES('SYS_INS_MSG','SYS_INS_MSG',0,0,0,1,'SYS',1,5,'s1u3',getdate(),'s1u3',getdate(),'MONTH',10);

--论坛共享资料得分
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_period,cty_hit)
VALUES('SYS_MSG_UPLOAD_RES','SYS_MSG_UPLOAD_RES',0,0,0,1,'SYS',1,5,'s1u3',getdate(),'s1u3',getdate(),'MONTH',10);

--成功报读培训
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_period,cty_hit)
VALUES('ITM_ENROLLED','ITM_ENROLLED',0,0,0,1,'COS',1,5,'s1u3',getdate(),'s1u3',getdate(),null,null);

--成绩达到60分或以上
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_period,cty_hit)
VALUES('ITM_SCORE_PAST_60','ITM_SCORE_PAST_60',0,0,0,1,'COS',1,5,'s1u3',getdate(),'s1u3',getdate(),null,null);

--成绩达到70分或以上
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_period,cty_hit)
VALUES('ITM_SCORE_PAST_70','ITM_SCORE_PAST_70',0,0,0,1,'COS',1,10,'s1u3',getdate(),'s1u3',getdate(),null,null);

--成绩达到80分或以上
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_period,cty_hit)
VALUES('ITM_SCORE_PAST_80','ITM_SCORE_PAST_80',0,0,0,1,'COS',1,15,'s1u3',getdate(),'s1u3',getdate(),null,null);

--成绩达到90分或以上
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_period,cty_hit)
VALUES('ITM_SCORE_PAST_90','ITM_SCORE_PAST_90',0,0,0,1,'COS',1,20,'s1u3',getdate(),'s1u3',getdate(),null,null);

--测验已通过
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_period,cty_hit)
VALUES('ITM__PAST_TEST','ITM_TEST_PAST',0,0,0,1,'COS',1,5,'s1u3',getdate(),'s1u3',getdate(),null,null);

--作业已提交
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_period,cty_hit)
VALUES('ITM_SUBMIT_ASS','ITM_SUBMIT_ASS',0,0,0,1,'COS',1,5,'s1u3',getdate(),'s1u3',getdate(),null,null);

--调查问卷已提交
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_period,cty_hit)
VALUES('ITM_SUBMIT_SVY','ITM_SUBMIT_SVY',0,0,0,1,'COS',1,5,'s1u3',getdate(),'s1u3',getdate(),null,null);

--教材已浏览
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_period,cty_hit)
VALUES('ITM_VIEW_RDG','ITM_VIEW_RDG',0,0,0,1,'COS',1,5,'s1u3',getdate(),'s1u3',getdate(),null,null);

--“课件”已浏览
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_period,cty_hit)
VALUES('ITM_VIEW_COURSEWARE','ITM_VIEW_COURSEWARE',0,0,0,1,'COS',1,5,'s1u3',getdate(),'s1u3',getdate(),null,null);

--“课件”已完成
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_period,cty_hit)
VALUES('ITM_PAST_COURSEWARE','ITM_PAST_COURSEWARE',0,0,0,1,'COS',1,5,'s1u3',getdate(),'s1u3',getdate(),null,null);

--参考已浏览
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_period,cty_hit)
VALUES('ITM_VIEW_REF','ITM_VIEW_REF',0,0,0,1,'COS',1,5,'s1u3',getdate(),'s1u3',getdate(),null,null);

--视频点播已浏览
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_period,cty_hit)
VALUES('ITM_VIEW_VOD','ITM_VIEW_VOD',0,0,0,1,'COS',1,5,'s1u3',getdate(),'s1u3',getdate(),null,null);

--答疑栏已参与
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_period,cty_hit)
VALUES('ITM_VIEW_FAQ','ITM_VIEW_FAQ',0,0,0,1,'COS',1,5,'s1u3',getdate(),'s1u3',getdate(),null,null);

--培训论坛发贴得分
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_period,cty_hit)
VALUES('ITM_INS_TOPIC','ITM_INS_TOPIC',0,0,0,1,'COS',1,10,'s1u3',getdate(),'s1u3',getdate(),'MONTH',10);

--论坛回贴得分
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_period,cty_hit)
VALUES('ITM_INS_MSG','ITM_INS_MSG',0,0,0,1,'COS',1,5,'s1u3',getdate(),'s1u3',getdate(),'MONTH',10);

--论坛共享资料得分
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_period,cty_hit)
VALUES('ITM_MSG_UPLOAD_RES','ITM_MSG_UPLOAD_RES',0,0,0,1,'COS',1,5,'s1u3',getdate(),'s1u3',getdate(),'MONTH',10);

--导入课程积分
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_period,cty_hit)
VALUES('ITM_IMPORT_CREDIT','ITM_IMPORT_CREDIT',0,1,0,1,'COS',0,0,'s1u3',getdate(),'s1u3',getdate(),null,null);


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
alter table creditstype alter column cty_code nvarchar(255)


/*
Auth: Robin
Date: 2009-03-27
Desc: for Auto Credits
*/
ALTER TABLE aeItem ADD itm_bonus_ind bit NULL;
ALTER TABLE aeItem ADD itm_diff_factor DECIMAL(8,2) NULL;

ALTER TABLE UserCreditsDetail ADD ucd_app_id int NULL
DROP INDEX UserCreditsDetail.IX_cty_ent_itm_1
CREATE  INDEX IX_cty_ent_itm_app_1 ON userCreditsDetail(ucd_ent_id, ucd_cty_id, ucd_itm_id, ucd_app_id)


/*
Auth: Dean
Date: 2009-03-19
Desc: module credit - credit report, my credit and credit chart
*/
insert into ReportTemplate
(rte_title_xml,rte_type,rte_get_xsl,rte_exe_xsl,rte_dl_xsl,rte_meta_data_url,rte_seq_no,rte_owner_ent_id,rte_create_usr_id,rte_create_timestamp,rte_upd_usr_id,rte_upd_timestamp)
select 'XML', 'CREDIT', 'rpt_credit_srh.xsl', 'rpt_credit_res.xsl', null, null, 14, ste_ent_id, 's1u3', '2009-03-30 09:00:00.000', 's1u3', '2009-03-30 09:00:00.000' from acSite;

insert into acReportTemplate
(ac_rte_id,ac_rte_ent_id,ac_rte_rol_ext_id,ac_rte_ftn_ext_id,ac_rte_owner_ind,ac_rte_create_usr_id,ac_rte_create_timestamp)
select rte_id, null, rol_ext_id, 'RTE_READ', 0, 's1u3', '2009-03-30 09:00:00.000'
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
ALTER TABLE acSite ADD ste_type nvarchar(50) NULL;
go
update acSite set ste_type = '070084099097127100127101098075338'
go
ALTER TABLE acSite alter column ste_type nvarchar(50) NOT NULL;
go

/*
Auth: Harvey
Date: 2009-04-07
Desc: add table bereavedFunction
*/
CREATE TABLE bereavedFunction
	(
	brf_type nvarchar(20) NOT NULL,
	brf_rol_ext_id nvarchar(20) NOT NULL,
	brf_ftn_id int NOT NULL
	)
GO
ALTER TABLE bereavedFunction ADD CONSTRAINT
	PK_bereavedFunction PRIMARY KEY CLUSTERED
	(
	brf_type,
	brf_rol_ext_id,
	brf_ftn_id
	)
GO


/*
Auth: Harvey
Date: 2009-04-08
Desc: add function "STUDY_GROUP_VIEW" & "CM_CENTER_VIEW" from learner
*/
insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('STUDY_GROUP_VIEW', 'FUNCTION', 'HOMEPAGE', getdate(), null);

insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_creation_timestamp)
select rol_id, ftn_id,getdate() from acRole, acFunction
where rol_ext_id like 'NLRN_%' and ftn_ext_id = 'STUDY_GROUP_VIEW';

insert into acHomePage (ac_hom_rol_ext_id,ac_hom_ftn_ext_id, ac_hom_create_usr_id, ac_hom_create_timestamp)
select rol_ext_id, 'STUDY_GROUP_VIEW', 's1u3', getdate() from acRole where rol_ext_id like 'NLRN_%';


insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('CM_CENTER_VIEW', 'FUNCTION', 'HOMEPAGE', getdate(), null);

insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_creation_timestamp)
select rol_id, ftn_id,getdate() from acRole, acFunction
where rol_ext_id like 'NLRN_%' and ftn_ext_id = 'CM_CENTER_VIEW';

insert into acHomePage (ac_hom_rol_ext_id,ac_hom_ftn_ext_id, ac_hom_create_usr_id, ac_hom_create_timestamp)
select rol_ext_id, 'CM_CENTER_VIEW', 's1u3', getdate() from acRole where rol_ext_id like 'NLRN_%';

go

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
SELECT rol_id, ftn_id, getdate() FROM acRole, acFunction WHERE rol_ste_uid = 'EXA' AND ftn_ext_id = 'USR_OWN_MAIN';

INSERT INTO acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_creation_timestamp)
SELECT rol_id, ftn_id, getdate() FROM acRole, acFunction WHERE rol_ste_uid = 'EXA' AND ftn_ext_id = 'USR_PWD_UPD';

INSERT INTO acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_creation_timestamp)
SELECT rol_id, ftn_id, getdate() FROM acRole, acFunction WHERE rol_ste_uid = 'EXA' AND ftn_ext_id = 'USR_OWN_PREFER';


/*
Auth: Robin
Date: 2009-04-21
Desc: fix the bug 8774
*/
INSERT INTO acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_creation_timestamp)
SELECT rol_id, ftn_id, getdate() FROM acRole, acFunction WHERE rol_ste_uid = 'EXA' AND ftn_ext_id = 'USR_MGT_OWN';

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
go


/*
Auth: Robin
Date: 2009-04-28
Desc: fix the bug 8818
*/
ALTER TABLE UserCreditsDetail DROP CONSTRAINT PK_userCreditsDetail

/*
Auth: Shelley
Date: 2009-04-30
Desc: 删除knowQuestion表中的que_kca_id字段
*/
DROP INDEX knowQuestion.IX_knowQuestion_kca_id
alter table knowQuestion drop constraint FK_kque_kca1;
alter table knowQuestion drop column que_kca_id;

/*
Auth: Shelley
Date: 2009-05-26
*/
--Bug 8164:当学员所在的多个培训中心同级时，不是按标题排列，如图
--ALTER TABLE tcTrainingCenter ALTER COLUMN tcr_title nvarchar(255) COLLATE Chinese_PRC_CI_AS NOT NULL;

--修改数据库的COLLATE为按照中文简体的拼音排序。(需要sa登录执行此sql)
--note: altering the default collation of a database does not change the collations of the columns in any existing user-defined tables.
--ALTER DATABASE your db name COLLATE Chinese_PRC_CI_AS;

/*
Auth: Terry
Date: 2009-06-08
*/
update userCreditsDetail set ucd_total=ucd_hit*2 where ucd_cty_id in (select cty_id from creditsType where cty_code = 'ZD_NEW_QUE');

update userCreditsDetailLog set ucl_point=2 where ucl_bpt_id in (select cty_id from creditsType where cty_code = 'ZD_NEW_QUE');

/*
Auth: Shelley
Date: 2009-06-18
desc: 新增集成培训的function给TA
*/
insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('INTEGRATED_ITEM_MAIN', 'FUNCTION', 'HOMEPAGE', getdate(), null);

insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_creation_timestamp)
select rol_id, ftn_id,getdate() from acRole, acFunction
where rol_ext_id like 'TADM_%' and ftn_ext_id = 'INTEGRATED_ITEM_MAIN';

insert into acHomePage (ac_hom_rol_ext_id,ac_hom_ftn_ext_id, ac_hom_create_usr_id, ac_hom_create_timestamp)
select rol_ext_id, 'INTEGRATED_ITEM_MAIN', 's1u3', getdate() from acRole where rol_ext_id like 'TADM_%';

/*
Auth: Shelley
Date: 2009-06-19
desc: 新增集成培训的课程类型
*/
insert into aeItemType
	select ity_owner_ent_id,'INTEGRATED',ity_run_ind,14,'',null,getDate(),'slu3',null,ity_create_run_ind,ity_apply_ind,ity_qdb_ind,0,null,ity_certificate_ind,0,0,ity_has_attendance_ind,ity_ji_ind,ity_completion_criteria_ind,0,ity_target_method,0,ity_tkh_method,0,0,0
	from aeItemType
	where ity_id='SELFSTUDY'
	and ity_exam_ind = 0;

insert into aeTemplate(tpl_ttp_id,tpl_title,tpl_xml,tpl_owner_ent_id,tpl_create_timestamp,tpl_create_usr_id,tpl_upd_timestamp,tpl_upd_usr_id,tpl_approval_ind)
	select tpl_ttp_id, 'Integrated Item Template', '', tpl_owner_ent_id, getdate(), 's1u3', getdate(), 's1u3', tpl_approval_ind
	from aeTemplate
	where tpl_title = 'Self Study Item Template';

insert into aeTemplate(tpl_ttp_id,tpl_title,tpl_xml,tpl_owner_ent_id,tpl_create_timestamp,tpl_create_usr_id,tpl_upd_timestamp,tpl_upd_usr_id,tpl_approval_ind)
	select tpl_ttp_id, 'Integrated Enrollment Form Template', '', tpl_owner_ent_id, getdate(), 's1u3', getdate(), 's1u3', tpl_approval_ind
	from aeTemplate
	where tpl_title = 'Self Study Enrollment Form Template';

insert into aeTemplateView
	select a.tpl_id, tvw_id, '', tvw_cat_ind, tvw_target_ind, tvw_cm_ind, tvw_mote_ind, tvw_itm_acc_ind, tvw_res_ind, tvw_rsv_ind, getdate(), 's1u3', tvw_wrk_tpl_ind, tvw_cost_center_ind, tvw_ctb_ind, tvw_filesize_ind, tvw_km_domain_ind, tvw_km_published_version_ind, tvw_tcr_ind
	from aeTemplate a, aeTemplateView, aeTemplate b
	where tvw_tpl_id = b.tpl_id
	and tvw_id <> 'CANCELLED_VIEW'
	and a.tpl_title = 'Integrated Item Template'
	and b.tpl_title='Self Study Item Template'
	and a.tpl_owner_ent_id = b.tpl_owner_ent_id;

insert into aeTemplateView
	select a.tpl_id, tvw_id, '', tvw_cat_ind, tvw_target_ind, tvw_cm_ind, tvw_mote_ind, tvw_itm_acc_ind, tvw_res_ind, tvw_rsv_ind, getdate(), 's1u3', tvw_wrk_tpl_ind, tvw_cost_center_ind, tvw_ctb_ind, tvw_filesize_ind, tvw_km_domain_ind, tvw_km_published_version_ind, tvw_tcr_ind
	from aeTemplate a, aeTemplateView, aeTemplate b
	where tvw_tpl_id = b.tpl_id
	and a.tpl_title = 'Integrated Enrollment Form Template'
	and b.tpl_title = 'Self Study Enrollment Form Template'
	and a.tpl_owner_ent_id = b.tpl_owner_ent_id;

insert into aeItemTypeTemplate
	select itt_ity_owner_ent_id, 'INTEGRATED', itt_ttp_id, itt_seq_id, tpl_id, itt_run_tpl_ind, getdate(), itt_create_usr_id ,itt_session_tpl_ind
	from aeItemTypeTemplate, aeTemplate
	where itt_ity_id = 'SELFSTUDY'
	and tpl_title = 'Multi-level-approval Workflow Template'
	and tpl_id = itt_tpl_id
	and itt_ity_owner_ent_id = tpl_owner_ent_id;

insert into aeItemTypeTemplate
	select itt_ity_owner_ent_id, 'INTEGRATED', itt_ttp_id, itt_seq_id, a.tpl_id, itt_run_tpl_ind, getdate(), itt_create_usr_id ,itt_session_tpl_ind
	from aeItemTypeTemplate, aeTemplate a, aeTemplate b
	where itt_ity_id = 'SELFSTUDY'
	and itt_tpl_id = b.tpl_id
	and b.tpl_title = 'Self Study Item Template'
	and a.tpl_title = 'Integrated Item Template'
	and a.tpl_owner_ent_id = b.tpl_owner_ent_id;

insert into aeItemTypeTemplate
	select itt_ity_owner_ent_id, 'INTEGRATED', itt_ttp_id, itt_seq_id, a.tpl_id, itt_run_tpl_ind, getdate(), itt_create_usr_id ,itt_session_tpl_ind
	from aeItemTypeTemplate, aeTemplate a, aeTemplate b
	where itt_ity_id = 'SELFSTUDY'
	and itt_tpl_id = b.tpl_id
	and b.tpl_title = 'Self Study Enrollment Form Template'
	and a.tpl_title = 'Integrated Enrollment Form Template'
	and a.tpl_owner_ent_id = b.tpl_owner_ent_id;

ALTER TABLE aeItem ADD itm_integrated_ind int NOT NULL DEFAULT 0;

/*
Auth: Shelley
Date: 2009-06-22
desc: 增加表保存集成培训的课程准则
*/
--保存每个集成培训的课程准则
CREATE TABLE dbo.IntegCourseCriteria
	(
	icc_id int NOT NULL IDENTITY (1, 1),
	icc_itm_id int NOT NULL,
	icc_completed_elective_count int NOT NULL,
	icc_create_timestamp datetime NOT NULL,
	icc_create_usr_id nvarchar(20) NOT NULL,
	icc_update_timestamp datetime NOT NULL,
	icc_update_usr_id nvarchar(20) NOT NULL
	);

ALTER TABLE IntegCourseCriteria ADD CONSTRAINT
	DF_IntegCourseCriteria_icc_completed_elective_count DEFAULT 0 FOR icc_completed_elective_count;

ALTER TABLE IntegCourseCriteria ADD CONSTRAINT
	PK_IntegCourseCriteria PRIMARY KEY
	(
	icc_id
	);

ALTER TABLE IntegCourseCriteria ADD CONSTRAINT
	FK_IntegCourseCriteria_aeItem FOREIGN KEY
	(
	icc_itm_id
	) REFERENCES aeItem
	(
	itm_id
	);

CREATE INDEX IX_IntegCourseCriteria ON IntegCourseCriteria
	(
	icc_itm_id
	);

--保存集成培训中每条课程准则的完成条件
CREATE TABLE dbo.IntegCompleteCondition
	(
	icd_id int NOT NULL IDENTITY (1, 1),
	icd_icc_id int NOT NULL,
	icd_completed_item_count int NOT NULL,
	icd_type nvarchar(20) NOT NULL,
	icd_create_timestamp datetime NOT NULL,
	icd_create_usr_id nvarchar(20) NOT NULL,
	icd_update_timestamp datetime NOT NULL,
	icd_update_usr_id nvarchar(20) NOT NULL
	);

ALTER TABLE IntegCompleteCondition ADD CONSTRAINT
	DF_IntegCompleteCondition_icd_completed_item DEFAULT 0 FOR icd_completed_item_count;

ALTER TABLE IntegCompleteCondition ADD CONSTRAINT
	PK_IntegCompleteCondition PRIMARY KEY
	(
	icd_id
	);

ALTER TABLE IntegCompleteCondition ADD CONSTRAINT
	FK_IntegCompleteCondition_IntegCourseCriteria FOREIGN KEY
	(
	icd_icc_id
	) REFERENCES IntegCourseCriteria
	(
	icc_id
	);

CREATE INDEX IX_IntegCompleteCondition ON IntegCompleteCondition
	(
	icd_icc_id
	);

--保存集成培训与集成其它培训的关系
CREATE TABLE dbo.IntegRelationItem
	(
	iri_icd_id int NOT NULL,
	iri_relative_itm_id int NOT NULL
	);

ALTER TABLE IntegRelationItem ADD CONSTRAINT
	PK_IntegRelationItem PRIMARY KEY
	(
	iri_icd_id,
	iri_relative_itm_id
	);

ALTER TABLE IntegRelationItem ADD CONSTRAINT
	FK_IntegRelationItem_IntegCompleteCondition FOREIGN KEY
	(
	iri_icd_id
	) REFERENCES IntegCompleteCondition
	(
	icd_id
	);

ALTER TABLE IntegRelationItem ADD CONSTRAINT
	FK_IntegRelationItem_aeItem FOREIGN KEY
	(
	iri_relative_itm_id
	) REFERENCES aeItem
	(
	itm_id
	);


/*
Auth: Elvea
Date: 2009-07-14
desc: Bug 9301
*/
alter table aeItemType add ity_integ_ind int  DEFAULT 0 not null;
GO
update aeItemType set ity_integ_ind = 1 where ity_id = 'INTEGRATED' and ity_seq_id = 14;

/*
Auth: Terry
Date: 2009-07-29
desc: share resource folder
*/
ALTER TABLE objective ADD obj_share_ind int NULL CONSTRAINT DF_Objective_obj_share_ind DEFAULT 0;

/*
Auth: Terry
Date: 2009-07-29
desc: managed start in test
*/
ALTER TABLE Module ADD	mod_managed_ind bit NOT NULL CONSTRAINT DF_Module_mod_managed_ind DEFAULT 0;
ALTER TABLE Module ADD	mod_started_ind bit NOT NULL CONSTRAINT DF_Module_mod_started_ind DEFAULT 0;

/*
Auth: Terry
Date: 2009-07-30
desc: managed start in test
*/
ALTER TABLE aeItem ADD itm_share_ind bit NOT NULL CONSTRAINT DF_aeItem_itm_share_ind DEFAULT 0;

/*
Auth: Joyce
Date: 2009-12-05
desc: add index for increase performance.
*/
CREATE INDEX IX_mvh_thk_mod ON MODULEEVALUATIONHISTORY (MVH_MOD_ID, MVH_TKH_ID);
CREATE INDEX IX_aal_app_usr ON aeAppnApprovalList (aal_usr_ent_id, aal_app_id);

CREATE INDEX IX_ent_cat ON aeCatalogAccess (cac_ent_id, cac_cat_id);
CREATE INDEX IX_tnd_itm_id ON aeTreeNode (tnd_itm_id);
CREATE INDEX IX_asv_ent_mod ON AssessmentEvaluation (asv_ent_id, asv_mod_id);

CREATE INDEX IX_cos_itm_id ON Course (cos_itm_id);
CREATE INDEX IX_ccr_itm_id ON CourseCriteria (ccr_itm_id);
CREATE INDEX IX_cmt_ccr_cmr ON CourseMeasurement (cmt_ccr_id, cmt_cmr_id);


CREATE INDEX IX_cmr_ccr_res ON CourseModuleCriteria (cmr_ccr_id, cmr_res_id);
CREATE INDEX IX_MeasurementEvaluation ON MeasurementEvaluation (mtv_cos_id, mtv_ent_id, mtv_cmt_id, mtv_tkh_id);
CREATE INDEX IX_atm_tkh_id ON ProgressAttempt (atm_tkh_id);
CREATE INDEX IX_msg_usr_id ON Message (msg_usr_id);


/*
Auth: randy
Date: 2010-01-27
Desc: meger from bch bug fix 10215
*/
update aeItem set itm_content_eff_start_datetime =
CAST (
 CONVERT(varchar(10) , itm_content_eff_start_datetime, 120 ) + ' 23:59:59.000'
 AS datetime)
where itm_run_ind =1 and itm_type = 'CLASSROOM'



/*
Auth: randy
Date: 2010-08-2
Desc: Bug 10835
*/
insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
select xtp_id, 7, 'ent_id' from xslTemplate where xtp_type = 'ENROLLMENT_REMOVED';

insert into xslParamName (xpn_xtp_id, xpn_pos, xpn_name)
select xtp_id, 7, 'ent_id' from xslTemplate where xtp_type = 'ENROLLMENT_REMOVED_REMINDER';

/*
Auth: Terry
Date: 2010-06-01
Desc: add a new indicator for different scorm version(1.2,2004)
*/
ALTER TABLE Resources ADD res_sco_version nvarchar(10) NULL;
GO
update resources set res_sco_version = '1.2' where res_subtype = 'SCO' or res_type = 'SCORM';

alter table module alter column mod_web_launch ntext null;
go



/*
Auth: Wrren
Date: 2010-09-15
Desc: add defined project function
*/

insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('DEFINED_PROJECT', 'FUNCTION', 'HOMEPAGE', getDate(), null)
GO
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_creation_timestamp)
select rol_id, ftn_id, getdate() from acRole, acFunction where rol_ste_uid in ('SADM')
and ftn_ext_id = 'DEFINED_PROJECT'
GO
INSERT INTO acHomepage
select null, rol_ext_id, 'DEFINED_PROJECT', 's1u3', getdate() from acRole where rol_ste_uid in ('SADM')
GO
create table DefinedProject(
dpt_id int identity primary key,
dpt_tcr_id int not null,
dpt_code nvarchar(255) unique not null,
dpt_title nvarchar(200) not null,
dpt_status nvarchar(20) default 'OFF',
dpt_way nvarchar(20) default 'LEFT',
dpt_create_timestamp datetime not null,
dpt_create_usr_id nvarchar(20) not null,
dpt_update_timestamp datetime not null,
dpt_update_usr_id nvarchar(20) not null
)
GO

--add foreign key constarint
alter table DefinedProject add constraint defind_for_cons foreign key (dpt_tcr_id)
  references tcTrainingCenter (tcr_id)
GO
---add checek constraint
alter table DefinedProject add constraint check_status check(dpt_status in('ON','OFF'))
GO
alter table DefinedProject add constraint check_dpt_way check(dpt_way in('LEFT','RIGHT'))
GO

/*
Desc: support defined project link
*/

create table projectLink(
pjl_id int identity primary key,
pjl_dpt_id int not null,
pjl_code nvarchar(255) unique not null,
pjl_title nvarchar(255) not null,
pjl_status nvarchar(20) default 'ON' not null,
pjl_url nvarchar(255) not null,
pjl_create_timestamp datetime not null,
pjl_create_usr_id nvarchar(20) not null,
pjl_update_timestamp datetime not null,
pjl_update_usr_id nvarchar(20) not null,
foreign key (pjl_dpt_id) references DefinedProject(dpt_id),
check (pjl_status in ('ON','OFF')))


/*
Auth: Wrren
Date: 2010-09-19
Desc: support function of student home page display
*/
create table TcrModule(
tm_id int identity constraint zt_id_constaint primary key,
tm_tcr_id int not null,
tm_code nvarchar(255) unique not null,
tm_title_gb nvarchar(255) not null,
tm_title_ch nvarchar(255) not null,
tm_title_en nvarchar(255) not null,
tm_is_centre int default 0 not null,
constraint tm_is_centre_cons check (tm_is_centre in (0,1))
)
GO
create table AdditivedTcrModule(
atm_id int constraint atm_id_constaint primary key,
atm_x_mod_value int not null,
atm_added_ind nvarchar(10) not null,
constraint atm_id_cons foreign key(atm_id) references TcrModule (tm_id),
constraint atm_x_value_cons check(atm_x_mod_value in (0, 1, 2)),
constraint atm_added_ind_cons check(atm_added_ind in ('UNADDED', 'ADDED'))
)
GO
create table TcrTemplate(
tt_id int identity constraint tt_id_pri_constaint primary key,
tt_tcr_id int unique not null,
tt_dis_fun_navigation_ind int default 1 not null,
tt_create_timestamp datetime not null,
tt_create_usr_id nvarchar(20) not null,
tt_update_timestamp datetime not null,
tt_update_usr_id nvarchar(20) not null,
constraint tt_for_tcr foreign key (tt_tcr_id) references tcTrainingCenter (tcr_id)
)
GO
create table TcrTemplateModule(
ttm_id int identity constraint ttm_id_pri_constaint primary key,
ttm_tm_id int not null,
ttm_tt_id int not null,
ttm_mod_x_value int not null,
ttm_mod_y_value int not null,
ttm_mod_status int default 1,
constraint ttm_tm_id_for_cons foreign key (ttm_tm_id) references TcrModule(tm_id),
constraint ttm_tt_id_for_cons foreign key (ttm_tt_id) references TcrTemplate(tt_id),
constraint ttm_mod_status_cons check (ttm_mod_status in (0,1))
)
GO
--init TcrModule data
insert into TcrModule (tm_tcr_id, tm_code, tm_title_gb, tm_title_ch, tm_title_en, tm_is_centre)
values (0, 'APPROVE_DEMAND', '', '', '', 0)
GO
insert into TcrModule (tm_tcr_id, tm_code, tm_title_gb, tm_title_ch, tm_title_en, tm_is_centre)
values (0, 'RESEARCH_QUESTIONNAIRE', '', '', '', 0)
GO
insert into TcrModule (tm_tcr_id, tm_code, tm_title_gb, tm_title_ch, tm_title_en, tm_is_centre)
values (0, 'ABILITY_EVALUATE_QUESTIONNAIRE', '', '', '', 0)
GO
insert into TcrModule (tm_tcr_id, tm_code, tm_title_gb, tm_title_ch, tm_title_en, tm_is_centre)
values (0, 'PUBLIC_NOTICE', '', '', '', 0)
GO
insert into TcrModule (tm_tcr_id, tm_code, tm_title_gb, tm_title_ch, tm_title_en, tm_is_centre)
values (0, 'TRANING_CATALOG', '', '', '', 0)
GO
insert into TcrModule (tm_tcr_id, tm_code, tm_title_gb, tm_title_ch, tm_title_en, tm_is_centre)
values (0, 'ONELINE_ANSWERS', '', '', '', 0)
GO
insert into TcrModule (tm_tcr_id, tm_code, tm_title_gb, tm_title_ch, tm_title_en, tm_is_centre)
values (0, 'KNOWLEDGE_CENTER', '', '', '', 0)
GO
insert into TcrModule (tm_tcr_id, tm_code, tm_title_gb, tm_title_ch, tm_title_en, tm_is_centre)
values (0, 'USER_POSTER', '', '', '', 1)
GO
insert into TcrModule (tm_tcr_id, tm_code, tm_title_gb, tm_title_ch, tm_title_en, tm_is_centre)
values (0, 'INTEGRATED_TRANING', '', '', '', 1)
GO
insert into TcrModule (tm_tcr_id, tm_code, tm_title_gb, tm_title_ch, tm_title_en, tm_is_centre)
values (0, 'LEARNING_CENTRE', '', '', '', 1)
GO
insert into TcrModule (tm_tcr_id, tm_code, tm_title_gb, tm_title_ch, tm_title_en, tm_is_centre)
values (0, 'EXAM_CENTRE', '', '', '', 1)
GO
insert into TcrModule (tm_tcr_id, tm_code, tm_title_gb, tm_title_ch, tm_title_en, tm_is_centre)
values (0, 'STUDY_GROUP', '', '', '', 1)
GO
insert into TcrModule (tm_tcr_id, tm_code, tm_title_gb, tm_title_ch, tm_title_en, tm_is_centre)
values (0, 'SYSTEM_INFORS', '', '', '', 0)
GO
insert into TcrModule (tm_tcr_id, tm_code, tm_title_gb, tm_title_ch, tm_title_en, tm_is_centre)
values (0, 'PRIVATE_INFORS', '', '', '', 0)
GO
insert into TcrModule (tm_tcr_id, tm_code, tm_title_gb, tm_title_ch, tm_title_en, tm_is_centre)
values (0, 'CREDIT_RANKING', '', '', '', 0)
GO
insert into TcrModule (tm_tcr_id, tm_code, tm_title_gb, tm_title_ch, tm_title_en, tm_is_centre)
values (0, 'TRANING_RANKING', '', '', '', 0)
GO
insert into TcrModule (tm_tcr_id, tm_code, tm_title_gb, tm_title_ch, tm_title_en, tm_is_centre)
values (0, 'FRIEND_LINK', '', '', '', 0)

GO
alter table reguser add usr_choice_tcr_id int default 0


/*
Auth: John
Date: 2010-10-11
desc: role management function
*/
insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('SYS_ROLE_MAIN', 'FUNCTION', 'HOMEPAGE', getdate(), null);

insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_creation_timestamp)
select rol_id, ftn_id,getdate() from acRole, acFunction
where rol_ext_id like 'ADM_%' and ftn_ext_id = 'SYS_ROLE_MAIN';

insert into acHomePage (ac_hom_rol_ext_id,ac_hom_ftn_ext_id, ac_hom_create_usr_id, ac_hom_create_timestamp)
select rol_ext_id, 'SYS_ROLE_MAIN', 's1u3', getdate() from acRole where rol_ext_id like 'ADM_%';


alter table acRole add rol_title nvarchar(255);
alter table acRole add rol_type nvarchar(20);
update acRole set rol_type = 'SYSTEM' where rol_ste_ent_id = 1;
update acRole set rol_title = N'系统管理员' where rol_ext_id = 'ADM_1';
update acRole set rol_title = N'学员'       where rol_ext_id = 'NLRN_1';
update acRole set rol_title = N'培训管理员' where rol_ext_id = 'TADM_1';
update acRole set rol_title = N'讲师'       where rol_ext_id = 'INSTR_1';
update acRole set rol_title = N'考试监考员' where rol_ext_id = 'EXA_1';


alter table acrole add rol_create_usr_id nvarchar(20);
alter table acrole add rol_update_timestamp datetime;
alter table acrole add rol_update_usr_id nvarchar(20);
alter table acrole add rol_create_timestamp datetime;
update acrole set rol_create_timestamp = rol_creation_timestamp;
alter table acrole drop column rol_creation_timestamp;
alter table acrolefunction add rfn_create_usr_id nvarchar(20);
alter table acrolefunction add RFN_CREATE_TIMESTAMP datetime;
update acrolefunction set RFN_CREATE_TIMESTAMP = RFN_CREATION_TIMESTAMP;
alter table acrolefunction drop column RFN_CREATION_TIMESTAMP;
update acrolefunction set rfn_create_usr_id = 's1u3';
update acRole set rol_create_usr_id='s1u3',rol_update_usr_id='s1u3',rol_update_timestamp=rol_create_timestamp;
update acRole set rol_tc_ind=1 where rol_type = 'SYSTEM';

update acFunction set ftn_ext_id = 'ITM_INTEGRATED_MAIN' where ftn_ext_id = 'INTEGRATED_ITEM_MAIN';
update acHomePage set ac_hom_ftn_ext_id = 'ITM_INTEGRATED_MAIN' where ac_hom_ftn_ext_id = 'INTEGRATED_ITEM_MAIN';

insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('BOOK_QUERY', 'FUNCTION', 'HOMEPAGE', getdate(), null);
insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('ITM_COS_MAIN', 'FUNCTION', 'HOMEPAGE', getdate(), null);
insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('ITM_MGT', 'FUNCTION', 'ITEM', getdate(), null);
insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('ITM_MGT_PERFORMANCE', 'FUNCTION', 'ITEM', getdate(), null);
insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('ITM_MGT_APPLICATION', 'FUNCTION', 'ITEM', getdate(), null);
insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('ITM_MGT_CONTENT', 'FUNCTION', 'ITEM', getdate(), null);
insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('RPT_MGT', 'FUNCTION', 'HOMEPAGE', getdate(), null);

insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp)
select rol_id, ftn_id,getdate() from acRole, acFunction
where rol_ext_id like 'TADM_%' and ftn_ext_id = 'ITM_MGT_CONTENT';

insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp)
select rol_id, ftn_id,getdate() from acRole, acFunction
where rol_ext_id like 'TADM_%' and ftn_ext_id = 'ITM_MGT_PERFORMANCE';

insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp)
select rol_id, ftn_id,getdate() from acRole, acFunction
where rol_ext_id like 'TADM_%' and ftn_ext_id = 'ITM_MGT_APPLICATION';

insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp)
select rol_id, ftn_id,getdate() from acRole, acFunction
where rol_ext_id like 'TADM_%' and ftn_ext_id = 'ITM_COS_MAIN';

insert into acHomePage (ac_hom_rol_ext_id,ac_hom_ftn_ext_id, ac_hom_create_usr_id, ac_hom_create_timestamp)
select rol_ext_id, 'ITM_COS_MAIN', 's1u3', getdate() from acRole where rol_ext_id like 'TADM_%';

update acRole set rol_tc_ind = 0 where rol_type='SYSTEM' and rol_ext_id not like 'TADM_%';
alter table TCTRAININGCENTEROFFICER alter column TCO_ROL_EXT_ID nvarchar(255) not null;
alter table TCTRAININGCENTEROFFICER
  drop constraint PK_TCTRAININGCENTEROFFICER;
alter table TCTRAININGCENTEROFFICER
  add constraint PK_TCTRAININGCENTEROFFICER primary key (TCO_TCR_ID, TCO_USR_ENT_ID, TCO_ROL_EXT_ID);


/*
Auth: Wrren
Date: 2010-09-20
Desc: perfect defined project function
*/

GO
INSERT INTO acRoleFunction
select rol_id, ftn_id, 'slu3', getdate() from acRole, acFunction where rol_ste_uid in ('TADM')
and ftn_ext_id = 'DEFINED_PROJECT'
GO
INSERT INTO acHomepage
select null, rol_ext_id, 'DEFINED_PROJECT', 's1u3', getdate() from acRole where rol_ste_uid in ('TADM')


/*
Auth: Wrren
Date: 2010-09-20
Desc: Perfect the report lists homepage administrator
*/
delete from ObjectView where ojv_type in ('COURSE', 'TARGET_LEARNER', 'SURVEY_IND', 'GLOBAL_ENROLLMENT', 'FM_FEE');
delete from acReportTemplate where exists (select rte_id from ReportTemplate where rte_type in
('COURSE', 'TARGET_LEARNER', 'SURVEY_IND', 'GLOBAL_ENROLLMENT', 'FM_FEE') and ac_rte_id = rte_id);
delete from ReportSpec where exists (select rte_id from ReportTemplate where rte_type in
('COURSE', 'TARGET_LEARNER', 'SURVEY_IND', 'GLOBAL_ENROLLMENT', 'FM_FEE') and rsp_rte_id = rte_id);
delete from ReportTemplate where rte_type in ('COURSE', 'TARGET_LEARNER', 'SURVEY_IND', 'GLOBAL_ENROLLMENT', 'FM_FEE');
GO

/*
Auth: wrren
Date: 2010-10-28
desc: Perfect role manager function
*/
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp)
select rol_id, ftn_id,getdate() from acRole, acFunction where rol_ext_id like 'TADM_%' and ftn_ext_id = 'ITM_MGT'
GO
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('NEED_MANAGER', 'FUNCTION', 'HOMEPAGE', getDate(), null)


/*
Author : john
Desc: Create index
Date: 2010-10-29
*/
create index IX_RESOURCECONTENT_RES on resourcecontent(rcn_res_id);
create index IX_RESOURCECONTENT_RES_CONTENT on resourcecontent(rcn_res_id, rcn_res_id_content);
create index IX_RESOURCECONTENT_OBJ_CONTENT on resourcecontent(rcn_obj_id_content);
create index IX_RESOURCECONTENT_RES_TKH on resourcecontent(rcn_res_id, rcn_tkh_id);
create index IX_RESOURCECONTENT_RES_TKH_CONTENT on resourcecontent(rcn_res_id, rcn_tkh_id, rcn_res_id_content);

create index IX_PROGRESSATTEMPT_PGR_RES on ProgressAttempt(atm_pgr_res_id);
create index IX_PROGRESSATTEMPT_INT_RES on ProgressAttempt(atm_int_res_id);
create index IX_PROGRESSATTEMPT_RES_ORDER on ProgressAttempt(atm_int_res_id, atm_int_order);
create index IX_PROGRESSATTEMPT_PGR_TKH on ProgressAttempt(atm_pgr_usr_id, atm_pgr_res_id, atm_pgr_attempt_nbr, atm_tkh_id);

create index IX_PROGRESS_USR on Progress(pgr_usr_id);
create index IX_PROGRESS_TKH on Progress(pgr_tkh_id);

create index IX_COURSEEVALUATION_COS on CourseEvaluation(cov_cos_id);
create index IX_COURSEEVALUATION_ENT on CourseEvaluation(cov_ent_id);
create index IX_COURSEEVALUATION_TKH on CourseEvaluation(cov_tkh_id);

create index IX_MODULEEVALUATION_COS on ModuleEvaluation(mov_cos_id);
create index IX_MODULEEVALUATION_MOD on ModuleEvaluation(mov_mod_id);

create index IX_MODULEEVALUATIONHISTORY_ENT on ModuleEvaluationHistory(mvh_ent_id);
create index IX_MODULEEVALUATIONHISTORY_MOD on ModuleEvaluationHistory(mvh_mod_id);
create index IX_MODULEEVALUATIONHISTORY_TKH on ModuleEvaluationHistory(mvh_tkh_id);

create index IX_PROGRESSATTEMPTSAVE on PROGRESSATTEMPTSAVE(pas_tkh_id);

create index IX_INTERACTION_RES on INTERACTION(int_res_id);
create index IX_INTERACTION_RES_REFER on INTERACTION(int_res_id_refer);
create index IX_INTERACTION_RES_EXPLAIN on INTERACTION(int_res_id_explain);

create index IX_MODULESPEC_OBJ on MODULESPEC(msp_obj_id);
create index IX_MODULESPEC_RES on MODULESPEC(msp_res_id);

create index IX_AETREENODE_CAT_ID on aeTreeNode(tnd_cat_id);
create index IX_AETREENODE_LINK_TND_ID on aeTreeNode(tnd_link_tnd_id);

create index IX_TNR_CHILD_TND on aeTreeNodeRelation(tnr_child_tnd_id);

create index IX_AEITEMRELATION_PARENT on aeItemRelation(ire_parent_itm_id);
create index IX_AEITEMRELATION_CHILD on aeItemRelation(ire_child_itm_id);

create index IX_COURSECRITERIA_PARENT on COURSECRITERIA(ccr_ccr_id_parent);

create index IX_AEAPPLICATION_TKH_ID on AEAPPLICATION(app_tkh_id);

create index IX_CourseModuleCriteria_CCR on CourseModuleCriteria(cmr_ccr_id);
create index IX_CourseModuleCriteria_CMR on CourseModuleCriteria(cmr_cmr_id_parent);
create index IX_CourseModuleCriteria_RES on CourseModuleCriteria(cmr_res_id);

create index IX_COURSEMEASUREMENT_PARENT on COURSEMEASUREMENT(cmt_cmt_id_parent);
create index IX_COURSEMEASUREMENT_CCR on COURSEMEASUREMENT(cmt_ccr_id);
create index IX_COURSEMEASUREMENT_CMR on COURSEMEASUREMENT(cmt_cmr_id);

create index IX_MEASUREMENTEVALUATION_CMT_TKH on MEASUREMENTEVALUATION(mtv_cmt_id, mtv_tkh_id);
create index IX_MEASUREMENTEVALUATION_COS_ENT on MEASUREMENTEVALUATION(mtv_cos_id, mtv_ent_id);

create index IX_MGRECHISTORY_REC on MGRECHISTORY(mgh_rec_id);
create index IX_MGRECHISTORY_MSG_XTP on MGRECHISTORY(mgh_mst_msg_id, mgh_mst_xtp_id);
create index IX_MGRECIPIENT_MSG on MGRECIPIENT(rec_msg_id);
create index IX_MGRECIPIENT_ENT on MGRECIPIENT(rec_ent_id);
create index IX_MGTNASELECTEDMESSAGE on MGTNASELECTEDMESSAGE(tsm_msg_id);
create index IX_MGXSLPARAMVALUE_MSG_XTP on MGXSLPARAMVALUE(xpv_mst_msg_id, xpv_mst_xtp_id);
create index IX_MGXSLPARAMVALUE_XPN on MGXSLPARAMVALUE(xpv_xpn_id);
create index IX_XSLPARAMNAME on XSLPARAMNAME(xpn_xtp_id);

create index IX_AECATALOGACCESS_CAT on AECATALOGACCESS(cac_cat_id);
create index IX_AECATALOGACCESS_ENT on AECATALOGACCESS(cac_ent_id);

create index IX_RESOURCES_RES_STATUS on RESOURCES(res_id, res_status);
create index IX_RESOURCES_RES_TEST on RESOURCES(res_mod_res_id_test);

create index IX_IRD_ITM_ID on AEITEMTARGETRULEDETAIL(ird_itm_id);
create index IX_IRD_ITR_ID on AEITEMTARGETRULEDETAIL(ird_itr_id);
create index IX_IRD_GROUP_ID on AEITEMTARGETRULEDETAIL(ird_group_id);
create index IX_IRD_GRADE_ID on AEITEMTARGETRULEDETAIL(ird_grade_id);
create index IX_IRD_SKILL_ID on AEITEMTARGETRULEDETAIL(ird_skill_id);
create index IX_ACENTITYROLE_ENT_ID on ACENTITYROLE(erl_ent_id);

create index IX_TRACKINGHISTORY on TRACKINGHISTORY(tkh_type, tkh_usr_ent_id, tkh_cos_res_id);
create index IX_TRACKINGHISTORY_USR_ENT_ID on TRACKINGHISTORY(tkh_usr_ent_id);
create index IX_TRACKINGHISTORY_COS_RES_ID on TRACKINGHISTORY(tkh_cos_res_id);

create index IX_USERCREDITSDETAILlLOG_ENT on userCreditsDetailLog(ucl_usr_ent_id);

create index IX_AEAPPNAPPROVALLIST_ENT ON aeAppnApprovalList (aal_usr_ent_id);
create index IX_AEAPPNAPPROVALLIST_APP ON aeAppnApprovalList (aal_app_id);
create index IX_AEAPPNAPPROVALLIST_APP_ENT ON aeAppnApprovalList (aal_app_ent_id);

create index IX_AEATTENDANCE_ITM ON aeAttendance (att_itm_id);

/*
Auth: Harvey
Date: 2010-11-01
Desc: import item offline module package
*/
-- Add/modify columns
alter table AEITEM add itm_offline_pkg nvarchar(255);
alter table AEITEM add itm_offline_pkg_file nvarchar(255);

/*
Auth: Harvey
Date: 2010-11-01
Desc: add dereaved function for STANDARD edition (LCMS)
*/

insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('LCMS_MAIN', 'FUNCTION', null, getdate(), null);

insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('LCMS_READ', 'FUNCTION', 'HOMEPAGE', getdate(), null);
GO

insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp)
select rol_id, ftn_id,getdate() from acRole, acFunction where rol_ext_id like 'TADM_%' and ftn_ext_id = 'LCMS_MAIN'

insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp)
select rol_id, ftn_id,getdate() from acRole, acFunction where rol_ext_id like 'NLRN_%' and ftn_ext_id = 'LCMS_READ'

INSERT INTO acHomepage
select null, rol_ext_id, 'LCMS_READ', 's1u3', getdate() from acRole where rol_ext_id like 'NLRN_%'

GO

insert into bereavedFunction
select 'STANDARD', rol_ext_id, ftn_id from acRole, acFunction where rol_ext_id like 'TADM_%' and ftn_ext_id = 'LCMS_MAIN';

insert into bereavedFunction
select 'STANDARD', rol_ext_id, ftn_id from acRole, acFunction where rol_ext_id like 'NLRN_%' and ftn_ext_id = 'LCMS_READ';
GO

/*
Auth: John
Date: 2010-11-02
Desc: Add training center template function
*/
Insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
	values ('TC_TEMPLATE_MAIN', 'FUNCTION', 'TRAININGCENTER', getdate(), NULL);
Insert into acRoleFunction  (rfn_rol_id,rfn_ftn_id,rfn_create_timestamp)
	select rol_id,ftn_id,getdate() from acRole,acFunction  where rol_ste_uid in ('TADM', 'SADM') and  ftn_ext_id = 'TC_TEMPLATE_MAIN';


/*
Auth: Willy
Date: 2011-03-04
Desc: modity the column type
*/
alter table userCreditsDetailLog alter column ucl_point float
DROP  INDEX  ix_uct_total  ON  userCredits ;
DROP  INDEX  ix_uct_zd_total  ON  userCredits ;
alter table userCredits alter column uct_total float
alter table userCreditsDetail alter column ucd_total float


/*
Auth: Wrren
Date: 2010-10-18
Desc: perfect function of student home page display
*/
drop table AdditivedTcrModule;
create table AdditivedTcrModule(
atm_tm_code nvarchar(255) unique not null,
atm_added_ind nvarchar(10) not null,
constraint atm_tm_code_cons foreign key(atm_tm_code) references TcrModule (tm_code),
constraint atm_added_ind_cons check(atm_added_ind in ('UNADDED', 'ADDED'))
);





/*
Auth: Wrren
Date: 2011-3-16
Desc: add function of student qq consultation
*/
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('QQ_CONSULTATION', 'FUNCTION', 'HOMEPAGE', getdate(), null);

INSERT INTO acRoleFunction
select rol_id, ftn_id, 'slu3', getdate() from acRole, acFunction where rol_ste_uid in ('SADM')
and ftn_ext_id = 'QQ_CONSULTATION';

INSERT INTO acRoleFunction
select rol_id, ftn_id, 'slu3', getdate() from acRole, acFunction where rol_ste_uid in ('TADM')
and ftn_ext_id = 'QQ_CONSULTATION';

insert into TcrModule (tm_tcr_id, tm_code, tm_title_gb, tm_title_ch, tm_title_en, tm_is_centre)
values (0, 'QQ_CONSULTATION', 'title', 'title', 'title', 0);
insert into AdditivedTcrModule(atm_tm_code, atm_added_ind) values ('QQ_CONSULTATION', 'UNADDED');

create table companyQQ(
cpq_id int identity primary key not null,
cpq_code nvarchar(255) unique,
cpq_title nvarchar(255),
cpq_number nvarchar(50),
cpq_desc nvarchar(500),
cpq_status nvarchar(20) default 'ON',
cpq_create_timestamp datetime default getdate(),
cpq_create_ent_id int,
cpq_update_timestamp datetime default getdate(),
cpq_update_ent_id int,
constraint cpp_status_cons check (cpq_status in ('ON', 'OFF'))
);

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
Auth: randy
Date: 2013-5-14
Desc: 添加取目标学员存储过程，
    //对DB2数据库暂时还没有处理
*/

CREATE PROCEDURE [dbo].[getItemsForTargetUser]
@UserId int 
AS
SET NOCOUNT ON;
SELECT DISTINCT itm_id FROM aeItem, aeItemTargetRuleDetail 
WHERE   itm_access_type = ird_type  
AND itm_id = ird_itm_id 
AND ird_group_id in (Select ern_ancestor_ent_id From EntityRelation Where ern_type = 'USR_PARENT_USG' And ern_child_ent_id = @UserId) 
AND ird_grade_id in (Select ern_ancestor_ent_id From EntityRelation Where ern_type = 'USR_CURRENT_UGR' And ern_child_ent_id = @UserId)

AND (ird_skill_id =-1 or ird_skill_id in (Select uss_ske_id From RegUserSkillSet Where uss_ent_id= @UserId))    
UNION 
SELECT itm_id FROM aeItem
WHERE  itm_access_type = 'ALL' OR itm_access_type is NULL;




 /*
Auth: randy
Date: 2013-05-15
Desc: 
*/
sp_rename   'reguser.usr_login_status_xml', 'usr_login_status', 'column'



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
CREATE TABLE [dbo].[certificate](
	cfc_id int IDENTITY(1,1) NOT NULL ,
	cfc_title nvarchar (100) NOT NULL ,
	cfc_img nvarchar (200) NOT NULL ,
	cfc_tcr_id int NOT NULL,
	cfc_status nvarchar(10) NOT NULL,	
	cfc_create_datetime datetime,
	cfc_create_user_id nvarchar(50),
  	cfc_update_datetime datetime,
 	cfc_update_user_id nvarchar(50),
	cfc_delete_datetime datetime,
	cfc_delete_user_id nvarchar(50),
	cfc_code nvarchar(255) not NULL,
	cfc_end_date datetime,
	
	CONSTRAINT PK_CFC_ID PRIMARY KEY (cfc_id),
   	CONSTRAINT FK_CFC_ID  FOREIGN KEY (cfc_tcr_id) references tcTrainingCenter(tcr_id)

)
/*
Author: walker liu
Date: 2010-05-18
Desc: Create certificate:添加一个FUNC，并分配权限给TADM 
*/
insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('CERTIFICATE_MAIN', 'FUNCTION', 'HOMEPAGE', getdate(), null);

insert into acRoleFunction (rfn_rol_id, rfn_ftn_id)
select rol_id, ftn_id from acRole, acFunction
where rol_ext_id like 'TADM_%' and ftn_ext_id = 'CERTIFICATE_MAIN';

insert into acHomePage (ac_hom_rol_ext_id,ac_hom_ftn_ext_id, ac_hom_create_usr_id, ac_hom_create_timestamp)
select rol_ext_id, 'CERTIFICATE_MAIN', 's1u3', getdate() from acRole where rol_ext_id like 'TADM_%';


/*
Auth: randy
Date: 2010-05-10
Desc: 课程证书 
*/
ALTER TABLE dbo.aeItem ADD
	itm_cfc_id int NULL
 


/*
Auth: randy
Date: 2013-5-20
Desc: 修改用户返应回来的SQL慢问题
*/
IF EXISTS (SELECT name FROM sysobjects WHERE name = 'SP_getCMTAndOfflineRs' AND type = 'P')
	DROP PROCEDURE [SP_getCMTAndOfflineRs]
GO
CREATE PROCEDURE SP_getCMTAndOfflineRs
	@tkh_id int,
        @ccr_id int
AS
begin
   select cmt_is_contri_by_score,cmt_id,cmt_title,cmt_max_score,cmt_contri_rate,cmt_pass_score,cmt_duration, cmt_place,
   cmt_status,cmt_status_desc_option,mtv_status,mtv_score,res_status,res_id,res_title,res_subtype,mov_status,
   cmr_ccr_id ,cmr_id,cmr_contri_rate, cmr_status,cmr_status_desc_option,res_id,res_type,res_title,res_status,res_subtype,
	res_desc,mod_max_score,mod_pass_score,mov_score,mov_status,mov_tkh_id 
   From CourseMeasurement  
   left join (  select cmr_ccr_id ,cmr_id,cmr_contri_rate, cmr_status,cmr_status_desc_option,res_id,res_type,res_title,res_status,res_subtype, 
             res_desc,mod_max_score,mod_pass_score,mov_score,mov_status,mov_tkh_id 
              From CourseModuleCriteria, Resources, Module 
                 left join ModuleEvaluation on (mov_mod_id = mod_res_id and mov_tkh_id =@tkh_id ) 
                  where cmr_del_timestamp is null    and cmr_res_id = res_id and res_id = mod_res_id   )
   cmr on (cmt_cmr_id = cmr.cmr_id) left join measurementEvaluation on (cmt_id = mtv_cmt_id and mtv_tkh_id = @tkh_id) 
  where cmt_delete_timestamp is null and cmt_ccr_id = @ccr_id  
  end
  
  
  
 
/* 
 Auth: leon
添加供应商管理TADM的权限
*/
insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('SUPPLIER_MAIN', 'FUNCTION', 'HOMEPAGE', getdate(), null);

insert into acRoleFunction (rfn_rol_id, rfn_ftn_id)
select rol_id, ftn_id from acRole, acFunction
where rol_ext_id like 'TADM_%' and ftn_ext_id = 'SUPPLIER_MAIN';

insert into acHomePage (ac_hom_rol_ext_id,ac_hom_ftn_ext_id, ac_hom_create_usr_id, ac_hom_create_timestamp)
select rol_ext_id, 'SUPPLIER_MAIN', 's1u3', getdate() from acRole where rol_ext_id like 'TADM_%';

/*
Auth: leon
对象:  Table [dbo].[supplier]    脚本日期: 06/04/2013 17:09:42 
*/
CREATE TABLE [dbo].[supplier](
	[spl_id] [int] IDENTITY(1,1) NOT NULL,
	[spl_name] [nvarchar](255) NOT NULL,
	[spl_established_date] [datetime] NULL,
	[spl_registered_capital] [float] NULL,
	[spl_type] [nvarchar](255)   NULL,
	[spl_address] [nvarchar](255)   NULL,
	[spl_post_num] [nvarchar](255)   NULL,
	[spl_contact] [nvarchar](255)   NULL,
	[spl_tel] [nvarchar](255)   NULL,
	[spl_mobile] [nvarchar](255)   NULL,
	[spl_email] [nvarchar](255)   NULL,
	[spl_total_staff] [int] NULL,
	[spl_full_time_inst] [int] NULL,
	[spl_part_time_inst] [int] NULL,
	[spl_expertise] [nvarchar](1024)   NULL,
	[spl_course] [nvarchar](1024)   NULL,
	[spl_status] [nvarchar](50)   NULL,
	[spl_attachment_1] [nvarchar](255)   NULL,
	[spl_attachment_2] [nvarchar](255)   NULL,
	[spl_attachment_3] [nvarchar](255)   NULL,
	[spl_attachment_4] [nvarchar](255)   NULL,
	[spl_attachment_5] [nvarchar](255)   NULL,
	[spl_attachment_6] [nvarchar](255)   NULL,
	[spl_attachment_7] [nvarchar](255)   NULL,
	[spl_attachment_8] [nvarchar](255)   NULL,
	[spl_attachment_9] [nvarchar](255)   NULL,
	[spl_attachment_10] [nvarchar](255)   NULL,
	[spl_create_datetime] [datetime] NULL,
	[spl_create_usr_id] [nvarchar](50)   NULL,
	[spl_update_datetime] [datetime] NULL,
	[spl_update_usr_id] [nvarchar](50)   NULL,
	[spl_representative] [nchar](255)   NULL,
	CONSTRAINT [PK_SUPPLIER] PRIMARY KEY CLUSTERED ([spl_id] ASC)
)



/*
Auth: leon
对象:  Table [dbo].[supplierComment]
*/
CREATE TABLE [dbo].[supplierComment](
	[scm_id] [int] IDENTITY(1,1)  NOT NULL,
	[scm_spl_id] [int] NOT NULL,
	[scm_ent_id] [int] NOT NULL,
	[scm_design_score] [float] NULL,
	[scm_teaching_score] [float] NULL,
	[scm_price_score] [float] NULL,
	[scm_comment] [nvarchar](500)   NULL,
	[scm_update_datetime] [datetime] NULL,
	[scm_create_datetime] [datetime] NULL,
	[scm_management_score] [float] NULL,
 CONSTRAINT [PK_SUPPLIER_SCM] PRIMARY KEY CLUSTERED ([scm_id] ASC)
) 

ALTER TABLE [dbo].[supplierComment]  WITH CHECK ADD  CONSTRAINT [FK_SUPPLIER_SCM] FOREIGN KEY([scm_ent_id])
REFERENCES [dbo].[Entity] ([ent_id])

ALTER TABLE [dbo].[supplierComment]  WITH CHECK ADD  CONSTRAINT [FK_FK_SUPPLIER_SCM_ENT] FOREIGN KEY([scm_spl_id])
REFERENCES [dbo].[supplier] ([spl_id])


CREATE INDEX SUPPLIER_SCM_INDEX ON supplierComment("scm_ent_id", "scm_spl_id")


/*
Auth: leon
对象:  Table [dbo].[supplierCooperationExperience]
*/
CREATE TABLE [dbo].[supplierCooperationExperience](
	[sce_id] [int] IDENTITY(1,1) NOT NULL,
	[sce_spl_id] [int] NULL,
	[sce_itm_name] [nvarchar](255)  NULL,
	[sce_start_date] [datetime] NULL,
	[sce_end_date] [datetime] NULL,
	[sce_desc] [nvarchar](500)  NULL,
	[sce_dpt] [nvarchar](255)  NULL,
	[sce_update_datetime] [datetime] NULL,
	[sce_update_usr_id] [nvarchar](50)  NULL,
 CONSTRAINT [PK_SUPPLIER_SCE] PRIMARY KEY CLUSTERED (	[sce_id] ASC)
)
ALTER TABLE [dbo].[supplierCooperationExperience]  WITH CHECK ADD  CONSTRAINT [FK_SUPPLIER_SCE] FOREIGN KEY([sce_spl_id])
REFERENCES [dbo].[supplier] ([spl_id])

CREATE INDEX SUPPLIER_CE_INDEX ON supplierCooperationExperience("sce_spl_id")

/* 
对象:  Table [dbo].[supplierMainCourse]   
 脚本日期: 06/04/2013 17:12:15 
 */
CREATE TABLE [dbo].[supplierMainCourse](
	[smc_id] [int] IDENTITY(1,1) NOT NULL,
	[smc_spl_id] [int] NOT NULL,
	[smc_name] [nvarchar](255)  NULL,
	[smc_inst] [nvarchar](255)  NULL,
	[smc_price] [float] NULL,
	[smc_update_datetime] [datetime] NULL,
	[smc_update_usr_id] [nvarchar](50)  NULL,
 CONSTRAINT [PK_SUPPLIER_MC] PRIMARY KEY CLUSTERED ([smc_id] ASC)
) 
ALTER TABLE [dbo].[supplierMainCourse]  WITH CHECK ADD  CONSTRAINT [FK_SUPPLIER_MC] FOREIGN KEY([smc_spl_id])
REFERENCES [dbo].[supplier] ([spl_id])

CREATE INDEX SUPPLIER_MC_INDEX ON supplierMainCourse("smc_spl_id")


/*
Auth: Elvea
Date: 2013-05-24
Desc: 讲师维护
*/
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('EXT_INSTRUCTOR', 'FUNCTION', 'HOMEPAGE', getdate(), null);

INSERT INTO acRoleFunction
select rol_id, ftn_id, 'slu3', getdate() from acRole, acFunction where rol_ste_uid in ('TADM')
and ftn_ext_id = 'EXT_INSTRUCTOR';

insert into acHomePage (ac_hom_rol_ext_id,ac_hom_ftn_ext_id, ac_hom_create_usr_id, ac_hom_create_timestamp)
select rol_ext_id, 'EXT_INSTRUCTOR', 's1u3', getdate() from acRole where rol_ste_uid in ('TADM');
GO

insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('INT_INSTRUCTOR', 'FUNCTION', 'HOMEPAGE', getdate(), null);

INSERT INTO acRoleFunction
select rol_id, ftn_id, 'slu3', getdate() from acRole, acFunction where rol_ste_uid in ('TADM')
and ftn_ext_id = 'INT_INSTRUCTOR';

insert into acHomePage (ac_hom_rol_ext_id,ac_hom_ftn_ext_id, ac_hom_create_usr_id, ac_hom_create_timestamp)
select rol_ext_id, 'INT_INSTRUCTOR', 's1u3', getdate() from acRole where rol_ste_uid in ('TADM');
GO

create table InstructorInf (
	iti_ent_id int not null,
	iti_name nvarchar(255),
	iti_gender nvarchar(20),
	iti_bday datetime,
	iti_mobile nvarchar(255),
	iti_email nvarchar(255),
	iti_img	nvarchar(255),
	iti_introduction nvarchar(1024),
	iti_level nvarchar(20),
	iti_cos_type nvarchar(20),
	iti_main_course	nvarchar(1024),
	iti_type nvarchar(255),
	iti_property nvarchar(255),
	iti_highest_educational nvarchar(255),
	iti_graduate_institutions nvarchar(255),
	iti_address nvarchar(255),
	iti_work_experience nvarchar(255),
	iti_education_experience nvarchar(255),
	iti_training_experience nvarchar(255),
	iti_expertise_areas nvarchar(255),
	iti_good_industry nvarchar(255),
	iti_training_company nvarchar(255),
	iti_training_contacts nvarchar(255),
	iti_training_tel nvarchar(255),
	iti_training_email nvarchar(255),
	iti_training_address nvarchar(255),
	iti_status nvarchar(20),
	iti_type_mark nvarchar(20),
	iti_score float,
	iti_create_datetime datetime,
	iti_create_user_id nvarchar(50),
	iti_update_datetime datetime,
	iti_update_user_id nvarchar(50),
	constraint PK_ITI_ID primary key (iti_ent_id)
);	
GO

create table InstructorCos (
	ics_id int identity(1,1) not null,
	ics_iti_ent_id int not null,
	ics_title nvarchar(255),
	ics_fee float,
	ics_hours float,
	ics_target	nvarchar(255),
	ics_content	nvarchar(255),
	constraint PK_ICS_ID primary key (ics_id),
	constraint PK_ICS_ENT_ID foreign key (ics_iti_ent_id) references Entity(ent_id)
);	
GO

create table InstructorComment (
	itc_id int identity(1,1) not null,
	itc_itm_id int not null,
	itc_ent_id int not null,
	itc_iti_ent_id int not null,
	itc_style_score float,
	itc_quality_score float,
	itc_structure_score float,
	itc_interaction_score float,
	itc_score float,
	itc_comment	nvarchar(255),
	itc_create_datetime datetime,
	itc_create_user_id nvarchar(50),
	itc_update_datetime datetime,
	itc_update_user_id nvarchar(50),
	constraint PK_ITC_ID primary key (itc_id),
	constraint PK_ITC_ITM_ID foreign key (itc_itm_id) references aeItem(itm_id),
	constraint PK_ITC_ENT_ID foreign key (itc_ent_id) references Entity(ent_id),
	constraint PK_ITC_ITI_ENT_ID foreign key (itc_iti_ent_id) references Entity(ent_id)
);				


/*
Auth: randy
Date: 2013-06-13
Desc: 培训单元讲师
*/
ALTER TABLE dbo.aeItem ADD  itm_inst_type nvarchar(52) NULL;
ALTER TABLE dbo.aeItemLessonInstructor DROP CONSTRAINT FK_aeItemLessonInstructor_RegUser;
ALTER TABLE [dbo].aeItemLessonInstructor ADD
	CONSTRAINT FK_aeItemLessonInstructor_Entity FOREIGN KEY
	(
		ili_usr_ent_id
	) REFERENCES [dbo].Entity (
		ent_id
	)
GO

/*
Auth: kevin
Date: 2013-06-13
Desc: 供应商记录总分
*/
ALTER TABLE dbo.supplierComment ADD scm_score float NULL;

/*
Auth: Elvea
Date: 2013-06-17
Desc: 
*/
alter table InstructorInf alter column iti_expertise_areas nvarchar(1024);
alter table InstructorInf alter column iti_good_industry nvarchar(1024);
alter table InstructorInf alter column iti_address nvarchar(1024);
alter table InstructorInf alter column iti_work_experience nvarchar(1024);
alter table InstructorInf alter column iti_education_experience nvarchar(1024);
alter table InstructorInf alter column iti_training_experience nvarchar(1024);

/*
Auth: leon
Date: 2013-06-19
Desc: 
*/
alter table supplier alter column spl_registered_capital nvarchar(255);



/*
Auth: kevin
Date: 2013-7-5
Desc: 添加宣告栏
*/
ALTER TABLE SitePoster ADD sp_media_file1 nvarchar(50);
ALTER TABLE SitePoster ADD sp_url1 nvarchar(255);
ALTER TABLE SitePoster ADD sp_status1 nvarchar(50);
ALTER TABLE SitePoster ADD sp_media_file2 nvarchar(50);
ALTER TABLE SitePoster ADD sp_url2 nvarchar(255);
ALTER TABLE SitePoster ADD sp_status2 nvarchar(50);
ALTER TABLE SitePoster ADD sp_media_file3 nvarchar(50);
ALTER TABLE SitePoster ADD sp_url3 nvarchar(255);
ALTER TABLE SitePoster ADD sp_status3 nvarchar(50);
ALTER TABLE SitePoster ADD sp_media_file4 nvarchar(50);
ALTER TABLE SitePoster ADD sp_url4 nvarchar(255);
ALTER TABLE SitePoster ADD sp_status4 nvarchar(50);

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
Auth: jun
Date: 2012-09-27
Desc: 实现把当前的所有职务编号跟实体的职务编号建立关系
*/
create   PROCEDURE  grade_entity_relationship as
DECLARE  cur_depart Cursor  for
       select ent_id, ugr_seq_no 
       from  entity 
       inner join UserGrade  on ugr_ent_id=ent_id
       order by ugr_ent_id desc;
declare @temp_ent_id int;
declare @temp_ent_ste_uid int;
begin
 open cur_depart
    fetch next from cur_depart into @temp_ent_id,@temp_ent_ste_uid
    while(@@Fetch_status=0)
          begin 
               update entity set ent_ste_uid=@temp_ent_ste_uid where ent_type='UGR' and ent_id=@temp_ent_id;
               fetch from cur_depart19 into  @temp_ent_id,@temp_ent_ste_uid;
          end;
end;

exec grade_entity_relationship;


/*
Auth: 
Date: 2013-07-25
Desc: 积分规则可填写小数,把原来的数据类型改为float
*/
ALTER TABLE creditstype ALTER column cty_default_credits float



/*
Auth: Mike
Date: 2012-03-27
Desc: 培训管理员添加职务维护权限
*/
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_usr_id, rfn_create_timestamp)
select rol_id, ftn_id, 's1u3', getdate() from acRole, acFunction where ftn_ext_id = 'USR_MGT_GRADE' and rol_ext_id = 'TADM_1';

-- 职务维护添加与培训中心关联字段
ALTER TABLE usergrade ADD ugr_tcr_id INT;

update usergrade set ugr_tcr_id = 1;

/*
Auth: Harvey
Date: 2012-5-8
Desc: ApiToken
*/
create table APIToken(
	atk_id nvarchar(64) not null,
	atk_usr_id nvarchar(64) not null,
	atk_usr_ent_id int,
	atk_create_timestamp datetime,
	atk_expiry_timestamp datetime,
	atk_developer_id nvarchar(128) not null
);


alter table acSite add ste_developer_id nvarchar(128);

update acSite set ste_developer_id='MOBILE';

alter table regUser add usr_weixin_id nvarchar(128);

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
 null, 3, ste_ent_id, 's1u3', '2013-07-24', 
's1u3', '2013-07-24' 
from acSite

GO
insert into acReportTemplate
(ac_rte_id,ac_rte_ent_id,ac_rte_rol_ext_id,ac_rte_ftn_ext_id,ac_rte_owner_ind,
ac_rte_create_usr_id,ac_rte_create_timestamp)
select rte_id, null, rol_ext_id, 'RTE_READ', 0,
's1u3', '2013-07-24 12:00:00.000' 
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
select app_itm_id t_id, isnull(ire_parent_itm_id,itm_id) p_itm_id ,app_ent_id,app_id,att_ats_id,cov_score, cov_total_time,attempts_user,total_attempt
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
alter table SitePoster add sp_tcr_id int not null default 1,
sp_upd_usr_id nvarchar(20),sp_upd_timestamp datetime; 

ALTER TABLE [dbo].[SitePoster] DROP CONSTRAINT [FK_SitePoster_acSite]

/*
Auth: Elvea
Date: 2013-8-1
Desc: 课程信息保存到数据库中
*/
create table aeItemExtension (
	ies_itm_id int not null,
	ies_lang ntext,
	ies_objective ntext,
	ies_contents ntext,
	ies_duration ntext,
	ies_audience ntext,
	ies_prerequisites ntext,
	ies_exemptions ntext,
	ies_remarks ntext,
	ies_enroll_confirm_remarks ntext,
	ies_schedule ntext,
	ies_itm_ref_materials_1 nvarchar(500),
	ies_itm_ref_materials_2 nvarchar(500),
	ies_itm_ref_materials_3 nvarchar(500),
	ies_itm_ref_materials_4 nvarchar(500),
	ies_itm_ref_materials_5 nvarchar(500),
	ies_itm_ref_url_1 nvarchar(500),
	ies_itm_ref_url_2 nvarchar(500),
	ies_itm_ref_url_3 nvarchar(500),
	ies_itm_ref_url_4 nvarchar(500),
	ies_itm_ref_url_5 nvarchar(500),
	ies_itm_rel_materials_1 nvarchar(500),
	ies_itm_rel_materials_2 nvarchar(500),
	ies_itm_rel_materials_3 nvarchar(500),
	ies_itm_rel_materials_4 nvarchar(500),
	ies_itm_rel_materials_5 nvarchar(500),
	ies_itm_rel_materials_6 nvarchar(500),
	ies_itm_rel_materials_7 nvarchar(500),
	ies_itm_rel_materials_8 nvarchar(500),
	ies_itm_rel_materials_9 nvarchar(500),
	ies_itm_rel_materials_10 nvarchar(500),
	ies_top_ind int null,
	ies_top_icon varchar(55) null,
 	constraint FK_aeItemExtension foreign key (ies_itm_id) REFERENCES aeItem (itm_id)
)

create index IX_IES ON aeItemExtension(ies_itm_id);

insert into aeItemExtension (ies_itm_id) 
select itm_id from aeItem

/* 社区化 */
/* 收藏 */
create table sns_collect(
	s_clt_id				int identity(1,1) primary key not null,
	s_clt_title				nvarchar(255),
	s_clt_url				nvarchar(255),
	s_clt_create_datetime	datetime,
	s_clt_uid				int,
	s_clt_module			nvarchar(50),
	s_clt_target_id			int
)
GO

/* 分享 */
create table sns_share(
	s_sha_id				int identity(1,1) not null,
	s_sha_title				nvarchar(255),
	s_sha_url				nvarchar(255),
	s_sha_create_datetime	datetime,
	s_sha_uid				int,
	s_sha_module			nvarchar(50),
	s_sha_target_id			int
)
GO

/* 评论 */
create table sns_comment(
	s_cmt_id				int identity(1,1) primary key,
	s_cmt_uid				int,
	s_cmt_content			ntext,
	s_cmt_is_reply			nvarchar(3),
	s_cmt_reply_to_id		int,
	s_cmt_create_datetime	datetime,
	s_cmt_anonymous			int,
	s_cmt_module			nvarchar(50),
	s_cmt_target_id			int
)
GO

/* 关注 */
create table sns_attention(
	s_att_id				int identity(1,1) primary key,
	s_att_source_uid		int,
	s_att_target_uid		int,
	s_att_create_datetime	datetime
)
GO

/* 评价 */
create table sns_valuation_log(
	s_vtl_log_id			int identity(1,1) primary key,
	s_vtl_type				nvarchar(50),
	s_vtl_score				int,
	s_vtl_create_datetime	datetime,
	s_vtl_uid				int,
	s_vtl_module			nvarchar(50),
	s_vtl_target_id			int
)
GO

create table sns_valuation(
	s_vlt_id				int identity(1,1) primary key, 
	s_vlt_type				nvarchar(50),
	s_vlt_score				int,
	s_vlt_module			nvarchar(50),
	s_vlt_target_id			int
)
GO

/* 动态 */
create table sns_doing (
	s_doi_id				int identity(1,1) primary key,
	s_doi_act				nvarchar(50),
	s_doi_title				ntext,
	s_doi_uid				int not null,
	s_doi_create_datetime	datetime,
	s_doi_url				nvarchar(255) not null,
	s_doi_module			nvarchar(50),
	s_doi_target_id			int
)
GO

/* 隐私设置 */
create table sns_setting (
	s_set_id				int identity(1,1) primary key,
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
	s_set_create_datetime	datetime,
	s_set_update_uid		int null,
	s_set_update_datetime	datetime
)
GO

/* 群组 */
create table sns_group (
	s_grp_id				int identity(1,1) primary key,
	s_grp_uid				int,
	s_grp_title				nvarchar(255),
	s_grp_desc				nvarchar(255),
	s_grp_private			int,
	s_grp_create_uid		int null,
	s_grp_create_datetime	datetime,
	s_grp_update_uid		int null,
	s_grp_update_datetime	datetime
)
GO

create table sns_group_member (
	s_gpm_id				int identity(1,1) primary key,
	s_gpm_grp_id			int,
	s_gpm_usr_id			int,
	s_gpm_join_datetime		datetime,
	s_gpm_status			int,
	s_gpm_type				int
)
GO 

/* 用户关系视图 */
IF EXISTS (SELECT name FROM sysobjects WHERE name = 'V_usrTcRelation' AND type = 'V')
	DROP view [V_usrTcRelation]
GO
create view V_usrTcRelation as
select ern_child_ent_id as u_id, ern_ancestor_ent_id as usg_id, ern_parent_ind, ern_order, tcr_id, tcr_code, tcr_title
from tctrainingcentertargetentity
inner join entityrelation on(ern_ancestor_ent_id =tce_ent_id)
inner join tcTrainingcenter on( tcr_id =tce_tcr_id) 
where  ern_type = 'USR_PARENT_USG'
GO

/* 课程视图 */
IF EXISTS (SELECT name FROM sysobjects WHERE name = 'V_item' AND type = 'V')
	DROP view [V_item]
GO
create view V_item as
select itm_id, itm_code, itm_title, itm_icon, itm_fee, itm_type, itm_tcr_id, tnd_title from aeItem left join (
    select tnd_itm_id, cat_title as tnd_title from aeTreeNode,aeCatalog where tnd_cat_id = cat_id and tnd_type = 'ITEM' and cat_id in(select max(tnd_cat_id) from  aeTreeNode where tnd_type = 'ITEM' group by tnd_itm_id)
) node on (itm_id = tnd_itm_id)
where itm_status = 'ON'
GO

/* 课程统计信息视图 */
IF EXISTS (SELECT name FROM sysobjects WHERE name = 'v_item_info' AND type = 'V')
	DROP view [v_item_info]
GO
create view v_item_info as
select itm_id iti_itm_id, isnull(collect_count, 0) iti_collect_count, isnull(share_count, 0) iti_share_count, 
	isnull(star_count, 0) iti_star_count, isnull(like_count, 0) iti_like_count, isnull(cmt_count, 0) iti_cmt_count, 
	isnull(s_vlt_score, 0) iti_score
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
) val on (itm_id = val.s_vlt_target_id)
GO

/*
Auth: leon
Date: 2013-9-5
Desc: 删除动态后删除其下的评论
*/
create trigger del_doing_comment 
on sns_doing
for delete
as
declare @id int;
select @id = s_doi_id from deleted where s_doi_module = 'Doing';
if(@id >0 )
	delete from sns_comment where s_cmt_target_id = @id;
go



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
VALUES('INTEGRAL_EMPTY','INTEGRAL_EMPTY',1,1,0,1,'',0,0,'s1u3',getdate(),'s1u3',getdate(),1);

/*
Auth: 
Date: 2013-07-25
Desc: 积分规则可填写小数,把原来的数据类型改为float
*/
ALTER TABLE creditstype ALTER column cty_default_credits float

/* 课程视图 */
IF EXISTS (SELECT name FROM sysobjects WHERE name = 'V_item' AND type = 'V')
	DROP view [V_item]
GO
create view V_item as
select itm_id, itm_code, itm_title, itm_icon, itm_fee, itm_type, itm_tcr_id, itm_publish_timestamp, itm_appn_start_datetime, itm_appn_end_datetime, tnd_title from aeItem left join (
    select tnd_itm_id, cat_title as tnd_title from aeTreeNode,aeCatalog where tnd_cat_id = cat_id and tnd_type = 'ITEM' and cat_id in(select max(tnd_cat_id) from  aeTreeNode where tnd_type = 'ITEM' group by tnd_itm_id)
) node on (itm_id = tnd_itm_id)
where itm_status = 'ON'
GO

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
GO


/*
Auth: leon
Date: 2013-12-30
Desc: 加申请时间,审核时间,审批人
*/
alter table sns_group_member add s_gpm_apply_datetime datetime
alter table sns_group_member add s_gpm_check_datetime datetime
alter table sns_group_member add s_gpm_check_user  int

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
Go

IF EXISTS (SELECT name FROM sysobjects WHERE name = 'V_user' AND type = 'V')
	DROP view [V_user]
GO
create view V_user as
select usr_ent_id u_id, usr_display_bil, usr_ste_usr_id, usr_email, usr_tel_1, usr_bday, 
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
and usg.ern_parent_ind = 1 
GO

/* 移除培训管理员维护职务的权限 */
delete from acRoleFunction where rfn_ftn_id in ( select ftn_id from acFunction where ftn_ext_id = 'USR_MGT_GRADE')
and rfn_rol_id in (select rol_id from acRole where rol_ste_uid = 'TADM')


/*
Auth: Leon
Date: 2014-2-20
Desc: 供应商法人
*/
ALTER TABLE supplier ALTER COLUMN spl_representative nvarchar(255)


/*
Auth: walker
Date: 2014-2-25
Desc: 修改学员进展报告分组试图名称
*/
drop VIEW v_itm_by_cos;
GO
create view view_lrn_activity_group as
select app_itm_id t_id, isnull(ire_parent_itm_id,itm_id) p_itm_id ,app_ent_id,app_id,att_ats_id,cov_score, cov_total_time,attempts_user,total_attempt
from aeItem
left join aeItemRelation ON (itm_id  = ire_child_itm_id)   
inner join aeApplication on (app_itm_id = itm_id)
INNER JOIN aeAttendance ON (app_id = att_app_id)
inner join CourseEvaluation on ( app_tkh_id = cov_tkh_id )
LEFT JOIN ( 
	SELECT DISTINCT mov_tkh_id, 1 AS  attempts_user, SUM(mov_total_attempt) AS total_attempt FROM ModuleEvaluation 
	WHERE mov_total_attempt IS NOT NULL AND mov_total_attempt>0  Group By mov_tkh_id  
) attempt ON (app_tkh_id= attempt.mov_tkh_id)
GO

/*
Auth: Walker
Date: 2014-01-15
取培训管理员所有顶层用户组。(用户搜索性能慢)
*/

IF EXISTS (SELECT name FROM sysobjects WHERE name = 'sp_get_top_level_group_id' AND type = 'P')
	DROP PROCEDURE [sp_get_top_level_group_id]
GO

CREATE PROCEDURE dbo.sp_get_top_level_group_id
	@usr_ent_id int
AS
begin
	select distinct tce_ent_id   From tcTrainingCenterOfficer,
	 tcTrainingCenterTargetEntity  tp,
	 tcTrainingCenter,
	 Entity
	 where tco_usr_ent_id = @usr_ent_id and tco_tcr_id = tcr_id 
	 and tcr_status = 'OK' and tce_tcr_id = tcr_id 

	 and tce_ent_id = ent_id and ent_delete_usr_id IS NULL and ent_delete_timestamp IS NULL
	 and   not exists(
	     select ern_child_ent_id from EntityRelation tem where ern_type = 'USG_PARENT_USG' and
	     tp.tce_ent_id= tem.ern_child_ent_id and 
	     exists(
			 select distinct ern_child_ent_id From tcTrainingCenterOfficer,
			 tcTrainingCenterTargetEntity,
			 tcTrainingCenter,
			 Entity,
			 EntityRelation ern
			 where tco_usr_ent_id = @usr_ent_id and tco_tcr_id = tcr_id 
				and tcr_status = 'OK'
                                and tce_tcr_id = tcr_id 
				and tce_ent_id = ern_child_ent_id 
				and tce_ent_id = ent_id and ent_delete_usr_id IS NULL and ent_delete_timestamp IS NULL
				and tem.ern_ancestor_ent_id = ern.ern_child_ent_id
	     )
	
	) 
end
GO

/*
Auth: leon
Date: 2014-2-25
Desc: 去掉访问控制里的学习小组模块
*/
delete from acroleFunction where rfn_ftn_id in (
	select ftn_id from acFunction where ftn_ext_id = 'STUDY_GROUP_MAIN'
)


/*--------------------------------------------------------------------------------------------------------------------*/
/*
Auth: walker
Date: 2013-1-29
Desc: 添加视频课程
*/

/* VIDEO */
insert into aeItemType
select ity_owner_ent_id, 'VIDEO', ity_run_ind, 17, '', null, getDate(), 'slu3', null, ity_create_run_ind,
	ity_apply_ind, ity_qdb_ind, 0, null, ity_certificate_ind, 0, 0, ity_has_attendance_ind, ity_ji_ind, 
	ity_completion_criteria_ind, 0, ity_target_method, 0, ity_tkh_method, 0, 0, 0, 0
from aeItemType 
where ity_id='SELFSTUDY' and ity_exam_ind = 0;
GO

/* */
insert into aeTemplate(tpl_ttp_id, tpl_title, tpl_xml, tpl_owner_ent_id, tpl_create_timestamp, tpl_create_usr_id, tpl_upd_timestamp, tpl_upd_usr_id, tpl_approval_ind)
select tpl_ttp_id, 'VIDEO Item Template', '', tpl_owner_ent_id, getdate(), 's1u3', getdate(), 's1u3', tpl_approval_ind 
from aeTemplate 
where tpl_title = 'Self Study Item Template';
GO

insert into aeTemplate(tpl_ttp_id, tpl_title, tpl_xml, tpl_owner_ent_id, tpl_create_timestamp, tpl_create_usr_id, tpl_upd_timestamp, tpl_upd_usr_id, tpl_approval_ind)
select tpl_ttp_id, 'VIDEO Enrollment Form Template', '', tpl_owner_ent_id, getdate(), 's1u3', getdate(), 's1u3', tpl_approval_ind 
from aeTemplate 
where tpl_title = 'Self Study Enrollment Form Template';
GO

/**/
insert into aeTemplateView 
select a.tpl_id, tvw_id, '', tvw_cat_ind, tvw_target_ind, tvw_cm_ind, tvw_mote_ind, tvw_itm_acc_ind, tvw_res_ind, tvw_rsv_ind, getdate(), 's1u3', tvw_wrk_tpl_ind, tvw_cost_center_ind, tvw_ctb_ind, tvw_filesize_ind, tvw_km_domain_ind, tvw_km_published_version_ind, tvw_tcr_ind
from aeTemplate a, aeTemplateView, aeTemplate b
where tvw_tpl_id = b.tpl_id 
and a.tpl_title = 'VIDEO Item Template' 
and b.tpl_title='Self Study Item Template' 
and a.tpl_owner_ent_id = b.tpl_owner_ent_id;
GO

insert into aeTemplateView 
select a.tpl_id, tvw_id, '', tvw_cat_ind, tvw_target_ind, tvw_cm_ind, tvw_mote_ind, tvw_itm_acc_ind, tvw_res_ind, tvw_rsv_ind, getdate(), 's1u3', tvw_wrk_tpl_ind, tvw_cost_center_ind, tvw_ctb_ind, tvw_filesize_ind, tvw_km_domain_ind, tvw_km_published_version_ind, tvw_tcr_ind
from aeTemplate a, aeTemplateView, aeTemplate b
where tvw_tpl_id = b.tpl_id 
and a.tpl_title = 'VIDEO Enrollment Form Template' 
and b.tpl_title = 'Self Study Enrollment Form Template' 
and a.tpl_owner_ent_id = b.tpl_owner_ent_id;
GO

/**/
insert into aeItemTypeTemplate 
select itt_ity_owner_ent_id, 'VIDEO', itt_ttp_id, itt_seq_id, tpl_id, itt_run_tpl_ind, getdate(), itt_create_usr_id ,itt_session_tpl_ind
from aeItemTypeTemplate, aeTemplate
where itt_ity_id = 'SELFSTUDY'
and tpl_title = 'Multi-level-approval Workflow Template'
and tpl_id = itt_tpl_id
and itt_ity_owner_ent_id = tpl_owner_ent_id;
GO

insert into aeItemTypeTemplate 
select itt_ity_owner_ent_id, 'VIDEO', itt_ttp_id, itt_seq_id, a.tpl_id, itt_run_tpl_ind, getdate(), itt_create_usr_id ,itt_session_tpl_ind
from aeItemTypeTemplate, aeTemplate a, aeTemplate b
where itt_ity_id = 'SELFSTUDY'
and itt_tpl_id = b.tpl_id
and b.tpl_title = 'Self Study Item Template'
and a.tpl_title = 'VIDEO Item Template'
and a.tpl_owner_ent_id = b.tpl_owner_ent_id;
GO

insert into aeItemTypeTemplate 
select itt_ity_owner_ent_id, 'VIDEO', itt_ttp_id, itt_seq_id, a.tpl_id, itt_run_tpl_ind, getdate(), itt_create_usr_id ,itt_session_tpl_ind
from aeItemTypeTemplate, aeTemplate a, aeTemplate b
where itt_ity_id = 'SELFSTUDY'
and itt_tpl_id = b.tpl_id
and b.tpl_title = 'Self Study Enrollment Form Template'
and a.tpl_title = 'VIDEO Enrollment Form Template'
and a.tpl_owner_ent_id = b.tpl_owner_ent_id;
GO

update aeItemType set ity_seq_id = 4 where ity_id = 'VIDEO';
GO
update aeItemType set ity_seq_id = ity_seq_id + 1 where ity_seq_id >=4 and ity_id <> 'VIDEO';
GO



/*
Auth: waler
Date: 2011-12-08
Desc: 视频点播模块添加视频时长,视频截图
*/
alter table resources add res_vod_duration int;
GO
alter table resources add res_img_link varchar(200);
GO

/*
Auth: waler
Date: 2011-12-21
Desc: 增加记录视频学习跟踪记录表
*/
CREATE TABLE vodLearnRecord
(
vlr_id int NOT NULL IDENTITY (1, 1),
vlr_tkh_id int NOT NULL,
vlr_chapter_id nvarchar(50) NOT NULL,
vlr_node_id nvarchar(50) NOT NULL,
vlr_node_vod_res_id int,
vlr_create_usr_id nvarchar(20) NOT NULL,
vlr_create_timestamp datetime NOT NULL,
vlr_update_usr_id nvarchar(20) NOT NULL,
vlr_update_timestamp datetime NOT NULL
)


/*
Auth: waler
Date: 20123-22
Desc: 视频课程要点
*/
alter table resources add res_vod_main ntext;
GO


/*
Auth: leon
Date: 2014-2-28
Desc: 去掉访问控制里的无效数据
*/
delete from acroleFunction where rfn_ftn_id in (
	select ftn_id from acFunction where ftn_ext_id = 'COURSE_MAIN'
)
delete from acroleFunction where rfn_ftn_id in (
	select ftn_id from acFunction where ftn_ext_id = 'EIP_MAIN'
)
delete from acFunction where ftn_ext_id = 'COURSE_MAIN';
delete from acFunction where ftn_ext_id = 'EIP_MAIN';


/*
Auth: Randy
Date: 2014-02-27
*/ 
ALTER TABLE COURSEMODULECRITERIA ALTER COLUMN CMR_STATUS_DESC_OPTION nvarchar(100);

/*
Auth: leon
Date: 2014-3-3
Desc: 去掉添加角色中的学习小组
*/
delete from acFunction where ftn_ext_id = 'STUDY_GROUP_MAIN';


/*
Auth: leon
Date: 2014-3-6
Desc: 课程指派  企业管理
*/
insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('COURSE_MAIN', 'FUNCTION', 'HOMEPAGE', getdate(), null); 
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp)
select rol_id, ftn_id,getdate() from acRole, acFunction
where rol_ext_id like 'ADM_%' and ftn_ext_id = 'COURSE_MAIN';

insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('EIP_MAIN', 'FUNCTION', 'HOMEPAGE', getdate(), null); 
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp)
select rol_id, ftn_id,getdate() from acRole, acFunction
where rol_ext_id like 'ADM_%' and ftn_ext_id = 'EIP_MAIN';


/*
Auth: leon
Date: 2014-3-10
Desc: 删除课程指派
*/
delete from acroleFunction where rfn_ftn_id in (
	select ftn_id from acFunction where ftn_ext_id = 'COURSE_MAIN'
)
delete from acFunction where ftn_ext_id = 'COURSE_MAIN';


/*
Auth: walker
Date: 2014-3-31
Desc: 修改学员进展报告分组试图字段名称(同lrnActivityReport表字段名称一样,便于维护)
*/
drop VIEW view_lrn_activity_group;
GO	
create view view_lrn_activity_group as
select app_itm_id lar_c_itm_id, isnull(ire_parent_itm_id,itm_id) lar_p_itm_id ,app_ent_id lar_usr_ent_id,app_id lar_app_id,app_tkh_id lar_tkh_id,
att_ats_id lar_att_ats_id,cov_score lar_cov_score, cov_total_time lar_cov_total_time,attempts_user lar_attempts_user,total_attempt lar_total_attempt
from aeItem
left join aeItemRelation ON (itm_id  = ire_child_itm_id)   
inner join aeApplication on (app_itm_id = itm_id)
INNER JOIN aeAttendance ON (app_id = att_app_id)
inner join CourseEvaluation on ( app_tkh_id = cov_tkh_id )
LEFT JOIN ( 
	SELECT DISTINCT mov_tkh_id, 1 AS  attempts_user, SUM(mov_total_attempt) AS total_attempt FROM ModuleEvaluation 
	WHERE mov_total_attempt IS NOT NULL AND mov_total_attempt>0  Group By mov_tkh_id  
) attempt ON (app_tkh_id= attempt.mov_tkh_id)
GO
/*
Auth: walker
Date: 2014-3-31
Desc: 添加表记录学习记录(通过线程把上面视图数据写入)
*/
CREATE TABLE lrnActivityReport
(
	lar_id int NOT NULL IDENTITY (1, 1),
	lar_c_itm_id int,
	lar_p_itm_id int,
	lar_usr_ent_id int,
	lar_app_id int,
	lar_tkh_id int,
	lar_att_ats_id int,
	lar_cov_score decimal(18,4),
	lar_cov_total_time float,
	lar_attempts_user int,
	lar_total_attempt int
)
GO
CREATE NONCLUSTERED INDEX IX_lrnActivityReport ON lrnActivityReport
	(
	lar_c_itm_id,
	lar_p_itm_id,
	lar_usr_ent_id,
	lar_app_id
	) ON [PRIMARY]
GO

/*
Auth: randy
Date: 2014-2-2
Desc: 删除论坛相关积分项
*/
delete from userCreditsDetailLog where ucl_bpt_id in(select cty_id from creditsType where cty_code in('SYS_INS_TOPIC','SYS_MSG_UPLOAD_RES','SYS_INS_MSG','ITM_INS_TOPIC','ITM_INS_MSG','ITM_MSG_UPLOAD_RES'))

delete from userCreditsDetail where ucd_cty_id in(select cty_id from creditsType where cty_code in('SYS_INS_TOPIC','SYS_MSG_UPLOAD_RES','SYS_INS_MSG','ITM_INS_TOPIC','ITM_INS_MSG','ITM_MSG_UPLOAD_RES'));

delete from creditsType  where cty_code in('SYS_INS_TOPIC','SYS_INS_MSG','SYS_MSG_UPLOAD_RES','ITM_INS_TOPIC','ITM_INS_MSG','ITM_MSG_UPLOAD_RES');

/*
Auth: walker
Date: 2014-4-2
Desc: 学员进展报告修改
*/
drop VIEW view_lrn_activity_group;
GO
create view view_lrn_activity_group as
select app_itm_id lar_c_itm_id, isnull(ire_parent_itm_id,itm_id) lar_p_itm_id ,app_ent_id lar_usr_ent_id,app_id lar_app_id,app_tkh_id lar_tkh_id,
att_ats_id lar_att_ats_id,cov_score lar_cov_score, cov_total_time lar_cov_total_time,attempts_user lar_attempts_user,total_attempt lar_total_attempt,
app_create_timestamp lar_app_create_timestamp, app_status lar_app_status, app_process_status lar_app_process_status,
att_timestamp lar_att_timestamp,att_create_timestamp lar_att_create_timestamp, att_remark lar_att_remark,att_rate lar_att_rate,
cov_cos_id lar_cov_cos_id,cov_commence_datetime	lar_cov_commence_datetime, cov_last_acc_datetime	lar_cov_last_acc_datetime
from aeItem
left join aeItemRelation on (itm_id  = ire_child_itm_id)   
inner join aeApplication on (app_itm_id = itm_id)
inner join RegUser on (usr_ent_id = app_ent_id and usr_status = 'OK')
inner join aeAttendance on (app_id = att_app_id)
inner join CourseEvaluation on ( app_tkh_id = cov_tkh_id )
left join ( 
	select DISTINCT mov_tkh_id, 1 AS  attempts_user, SUM(mov_total_attempt) AS total_attempt from ModuleEvaluation 
	where mov_total_attempt IS NOT NULL and mov_total_attempt>0  Group By mov_tkh_id  
) attempt on (app_tkh_id= attempt.mov_tkh_id)
GO

DROP TABLE lrnActivityReport;
GO
CREATE TABLE lrnActivityReport
(
	lar_id int NOT NULL IDENTITY (1, 1),
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
	lar_app_create_timestamp datetime, 
	lar_app_status nvarchar(20), 
	lar_app_process_status nvarchar(50), 
	lar_att_timestamp datetime,
	lar_att_create_timestamp datetime, 
	lar_att_remark nvarchar(2000), 
	lar_att_rate decimal(18,4),
	lar_cov_cos_id int,
	lar_cov_commence_datetime datetime, 
	lar_cov_last_acc_datetime datetime
)
GO
CREATE NONCLUSTERED INDEX IX_lrnActivityReport ON lrnActivityReport
	(
	lar_c_itm_id,
	lar_p_itm_id,
	lar_usr_ent_id,
	lar_app_id,
	lar_tkh_id
	) ON [PRIMARY]
GO

/*
Auth: Barry
Date: 2014-1-27
Desc: 公告增加缩略图字段
*/
ALTER TABLE message ADD msg_icon VARCHAR(50);

/* 
Auth: Kenry
Date: 2014-1-27
Desc: Mobile2.0API 移动宣传栏添加字段,0 pc端 1 移动端
*/
alter table SITEPOSTER add SP_MOBILE_IND INTEGER default 0 not null;
alter table SITEPOSTER drop constraint PK_SITEPOSTER;
ALTER TABLE [dbo].[SITEPOSTER] WITH NOCHECK ADD
	CONSTRAINT [PK_SITEPOSTER] PRIMARY KEY  NONCLUSTERED
	(
		[SP_STE_ID],
		[SP_MOBILE_IND]
	) WITH  FILLFACTOR = 90  ON [PRIMARY]
insert into SitePoster (SP_STE_ID, SP_MEDIA_FILE, SP_URL, SP_STATUS, SP_MEDIA_FILE1, SP_URL1, SP_STATUS1, SP_MEDIA_FILE2, SP_URL2, SP_STATUS2, SP_MEDIA_FILE3, SP_URL3, SP_STATUS3, SP_MEDIA_FILE4, SP_URL4, SP_STATUS4,SP_MOBILE_IND)
values (1, '', '', 'OFF', '', '', 'OFF', '', '', 'OFF', '', '', 'OFF', '', '', 'OFF',1);

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
Select Ity_Owner_Ent_Id,'MOBILE',Ity_Run_Ind,4,'XML',Ity_Icon_Url,getdate(),Ity_Create_Usr_Id,Ity_Init_Life_Status,Ity_Create_Run_Ind,Ity_Apply_Ind,Ity_Qdb_Ind,Ity_Auto_Enrol_Qdb_Ind,Ity_Cascade_Inherit_Col,Ity_Certificate_Ind,Ity_Session_Ind,Ity_Create_Session_Ind,Ity_Has_Attendance_Ind,Ity_Ji_Ind,Ity_Completion_Criteria_Ind,Ity_Can_Cancel_Ind,Ity_Target_Method,Ity_Reminder_Criteria_Ind,Ity_Tkh_Method,ity_exam_ind,ity_blend_ind,ity_ref_ind,ity_integ_ind From Aeitemtype
where Ity_Id = 'SELFSTUDY' and ity_exam_ind=0;

update aeItemType set ity_seq_id=ity_seq_id+1 where ity_seq_id>7;

insert into aeItemType 
Select Ity_Owner_Ent_Id,'MOBILE',Ity_Run_Ind,8,'XML',Ity_Icon_Url,getdate(),Ity_Create_Usr_Id,Ity_Init_Life_Status,Ity_Create_Run_Ind,Ity_Apply_Ind,Ity_Qdb_Ind,Ity_Auto_Enrol_Qdb_Ind,Ity_Cascade_Inherit_Col,Ity_Certificate_Ind,Ity_Session_Ind,Ity_Create_Session_Ind,Ity_Has_Attendance_Ind,Ity_Ji_Ind,Ity_Completion_Criteria_Ind,Ity_Can_Cancel_Ind,Ity_Target_Method,Ity_Reminder_Criteria_Ind,Ity_Tkh_Method,ity_exam_ind,ity_blend_ind,ity_ref_ind,ity_integ_ind From Aeitemtype
where Ity_Id = 'SELFSTUDY' and ity_exam_ind=1;
select * from aeItemType  order by ity_seq_id asc;

insert into aeTemplate(tpl_ttp_id,tpl_title,tpl_xml,tpl_owner_ent_id,tpl_create_timestamp,tpl_create_usr_id,tpl_upd_timestamp,tpl_upd_usr_id,tpl_approval_ind)
	select tpl_ttp_id, 'Mobile Learning Item Template', 'XML', tpl_owner_ent_id, getdate(), tpl_create_usr_id, getdate(),tpl_upd_usr_id, tpl_approval_ind 
	from aeTemplate 
	where tpl_title = 'Self Study Item Template';

insert into aeTemplate(tpl_ttp_id,tpl_title,tpl_xml,tpl_owner_ent_id,tpl_create_timestamp,tpl_create_usr_id,tpl_upd_timestamp,tpl_upd_usr_id,tpl_approval_ind)
	select tpl_ttp_id, 'Mobile Learning Enrollment Form Template', 'XML', tpl_owner_ent_id,getdate(), tpl_create_usr_id, getdate(), tpl_upd_usr_id, tpl_approval_ind 
	from aeTemplate 
	where tpl_title = 'Self Study Enrollment Form Template';

insert into aeTemplateView 
	select a.tpl_id, tvw_id, 'XML', tvw_cat_ind, tvw_target_ind, tvw_cm_ind, tvw_mote_ind, tvw_itm_acc_ind, tvw_res_ind, tvw_rsv_ind, getdate(), 's1u3', tvw_wrk_tpl_ind, tvw_cost_center_ind, tvw_ctb_ind, tvw_filesize_ind, tvw_km_domain_ind, tvw_km_published_version_ind, tvw_tcr_ind
	from aeTemplate a, aeTemplateView, aeTemplate b
	where tvw_tpl_id = b.tpl_id 
	and tvw_id <> 'CANCELLED_VIEW' 
	and a.tpl_title = 'Mobile Learning Item Template' 
	and b.tpl_title='Self Study Item Template'-- and b.tpl_exam
	and a.tpl_owner_ent_id = b.tpl_owner_ent_id;

insert into aeTemplateView 
	select a.tpl_id, tvw_id, 'XML', tvw_cat_ind, tvw_target_ind, tvw_cm_ind, tvw_mote_ind, tvw_itm_acc_ind, tvw_res_ind, tvw_rsv_ind, getdate(), 's1u3', tvw_wrk_tpl_ind, tvw_cost_center_ind, tvw_ctb_ind, tvw_filesize_ind, tvw_km_domain_ind, tvw_km_published_version_ind, tvw_tcr_ind
	from aeTemplate a, aeTemplateView, aeTemplate b
	where tvw_tpl_id = b.tpl_id 
	and a.tpl_title = 'Mobile Learning Enrollment Form Template' 
	and b.tpl_title = 'Self Study Enrollment Form Template' 
	and a.tpl_owner_ent_id = b.tpl_owner_ent_id;

insert into aeItemTypeTemplate 
	select itt_ity_owner_ent_id, 'MOBILE', itt_ttp_id, itt_seq_id, tpl_id, itt_run_tpl_ind, getdate(), itt_create_usr_id ,itt_session_tpl_ind
	from aeItemTypeTemplate, aeTemplate
	where itt_ity_id = 'SELFSTUDY'
	and tpl_title = 'Multi-level-approval Workflow Template'
	and tpl_id = itt_tpl_id
	and itt_ity_owner_ent_id = tpl_owner_ent_id;

insert into aeItemTypeTemplate 
	select itt_ity_owner_ent_id, 'MOBILE', itt_ttp_id, itt_seq_id, a.tpl_id, itt_run_tpl_ind, getdate(), itt_create_usr_id ,itt_session_tpl_ind
	from aeItemTypeTemplate, aeTemplate a, aeTemplate b
	where itt_ity_id = 'SELFSTUDY'
	and itt_tpl_id = b.tpl_id
	and b.tpl_title = 'Self Study Item Template'
	and a.tpl_title = 'Mobile Learning Item Template'
	and a.tpl_owner_ent_id = b.tpl_owner_ent_id;

insert into aeItemTypeTemplate 
	select itt_ity_owner_ent_id, 'MOBILE', itt_ttp_id, itt_seq_id, a.tpl_id, itt_run_tpl_ind, getdate(), itt_create_usr_id ,itt_session_tpl_ind
	from aeItemTypeTemplate, aeTemplate a, aeTemplate b
	where itt_ity_id = 'SELFSTUDY'
	and itt_tpl_id = b.tpl_id
	and b.tpl_title = 'Self Study Enrollment Form Template'
	and a.tpl_title = 'Mobile Learning Enrollment Form Template'
	and a.tpl_owner_ent_id = b.tpl_owner_ent_id;


insert into DisplayOption values (0,'MOD','MBL','LRN_READ',1,1,0,1,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0);
insert into DisplayOption values (0,'MOD','MBL','IST_READ',1,1,0,1,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,1,1,0,0);
insert into DisplayOption values (0,'MOD','MBL','IST_EDIT',1,1,0,1,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,1,1,0,0);

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
	ict_comment nvarchar(510),
	ict_create_timestamp datetime not null
)
insert into  aeItemComments_temp(ict_itm_id,ict_ent_id,ict_tkh_id,ict_score,ict_comment,ict_create_timestamp)
select ict_itm_id,ict_ent_id,ict_tkh_id,ict_score,ict_comment,ict_create_timestamp from aeItemComments;
drop table aeItemComments;
create table aeItemComments
(   ict_id int NOT NULL IDENTITY (1, 1),
	ict_itm_id int not null,
	ict_ent_id int not null,
	ict_tkh_id int not null,
	ict_score int not null,
	ict_comment nvarchar(510),
	ict_create_timestamp datetime not null
);
ALTER TABLE aeItemComments ADD CONSTRAINT
	PK_ict PRIMARY KEY CLUSTERED
	(
	ict_id
	);

insert into  aeItemComments(ict_itm_id,ict_ent_id,ict_tkh_id,ict_score,ict_comment,ict_create_timestamp)
select ict_itm_id,ict_ent_id,ict_tkh_id,ict_score,ict_comment,ict_create_timestamp from aeItemComments_temp;
GO



/**
 * weixin 用户表
 */
CREATE TABLE [dbo].[weixin_user](   
	[id] [varchar](32) NOT NULL,
	[weixin_open_id] [varchar](255) NULL,
	[fwh_code] [varchar](32) NULL,
	[wizbank_token] [varchar](32) NULL,
	[wizbank_user_id] [varchar](32) NULL,
	[wizbank_user_account] [varchar](32) NULL,
	[wizbank_user_name] [varchar](32) NULL,
	[create_time] [datetime] NULL,
	[modify_time] [datetime] NULL,
	[state] [varchar](1) NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

SET ANSI_PADDING OFF
GO

ALTER TABLE [dbo].[weixin_user] ADD  DEFAULT ('s') FOR [state]
GO


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
GO

/*
Auth: Randy 
Date: 2014-5-6
Desc: 删除论坛管理功能
*/
delete from acrolefunction where rfn_ftn_id in( select ftn_id from acfunction where ftn_ext_id ='FOR_MAIN');
delete from achomepage where ac_hom_ftn_ext_id = 'FOR_MAIN';
delete from acfunction where ftn_ext_id ='FOR_MAIN';



 
/*
Auth: randy
Date: 2014-05-13
Desc: 
*/
CREATE TABLE [dbo].[scoRecord](
	[srd_id] [int] primary key IDENTITY(1,1) NOT NULL,
	[srd_tkh_id] int NOT NULL,
	[srd_mod_id] int NOT NULL,
	[srd_itemId] [nvarchar](255) NOT NULL,
	[srd_courseStyle] [nvarchar](20) NOT NULL,
	[srd_courseStatus] [nvarchar](20) NULL,
	[srd_courseCompletion] [nvarchar](20) NULL,
	[srd_courseCredit] [nvarchar](20) NULL,
	[srd_courseLaunchData] [nvarchar](2048) NULL,
	[srd_courseCount] [int] NULL,
	[srd_courseLastDate] [datetime] NULL,
	[srd_courseTimeLength] [int] NULL,
	[srd_courseTimeLimit] [int] NULL,
	[srd_courseRawScore] [float] NULL,
	[srd_courseMaxScore] [float] NULL,
	[srd_courseMinScore] [float] NULL,
	[srd_coursePassScore] [float] NULL,
	[srd_courseLocation][ntext] NULL ,
	[srd_courseSusData] [ntext] NULL ,
	[srd_courseInteractions] [ntext] NULL ,
	[srd_courseObjective] [ntext] NULL ,
	[srd_courseData1] [ntext] NULL 
)
GO

CREATE 
  INDEX [IX_scoRecord_tkh_res] ON [dbo].[scoRecord] ([srd_mod_id],[srd_tkh_id]);
  
  GO
  
  
  alter table resources add  res_scor_identifier nvarchar(255) null;
alter table resources add  res_first_res_id int null;

CREATE 
  INDEX [IX_resources_first_res_id] ON [dbo].[resources] ([res_first_res_id]);

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

/************************************ END *********************************************************************/

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
GO


/*把离线课程改为混合式课程, 并删除离线课程这种类型*/
update aeItem set itm_blend_ind = 1 where itm_type = 'CLASSROOM' and itm_blend_ind = 0;
delete from aeItemType where ity_id = 'CLASSROOM' and ity_blend_ind = 0;


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


/*班级管理功能*/
delete from acRoleFunction where rfn_ftn_id in(select ftn_id  from acfunction  where ftn_ext_id in('RUN_MAIN'));
delete from acfunction  where ftn_ext_id in('RUN_MAIN');
delete from achomepage where ac_hom_ftn_ext_id  in('RUN_MAIN');




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
GO
commit;

/*
Auth: Mike
Date: 2014-07-14
Desc: 职业发展学习任务管理
*/
insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('PROFESSION_MAIN', 'FUNCTION', 'HOMEPAGE', getdate(), null);

insert into acRoleFunction (rfn_rol_id, rfn_ftn_id)
select rol_id, ftn_id from acRole, acFunction
where rol_ext_id like 'ADM_%' and ftn_ext_id = 'PROFESSION_MAIN';

insert into acHomePage (ac_hom_rol_ext_id,ac_hom_ftn_ext_id, ac_hom_create_usr_id, ac_hom_create_timestamp)
select rol_ext_id, 'PROFESSION_MAIN', 's1u3', getdate() from acRole where rol_ext_id like 'ADM_%';

--发展序列表
CREATE TABLE [dbo].[Profession](
	[pfs_id] [int] IDENTITY(1,1) NOT NULL,
	[pfs_title] [nvarchar](50) NOT NULL,
	[pfs_create_time] [datetime] NULL,
	[pfs_create_usr_id] [int] NULL,
	[pfs_update_time] [datetime] NULL,
	[pfs_update_usr_id] [int] NULL,
 CONSTRAINT [PK_Profession] PRIMARY KEY CLUSTERED 
(
	[pfs_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

--发展序列关联课程表
CREATE TABLE [dbo].[ProfessionItem](  
	[psi_id] [int] IDENTITY(1,1) NOT NULL,
	[psi_pfs_id] [int] NULL,
	[psi_ugr_id] [nvarchar](1000) NULL,
	[psi_itm] [nvarchar](1000) NULL,
 CONSTRAINT [PK_ProfessionItem] PRIMARY KEY CLUSTERED 
(
	[psi_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]

GO	

 /*
Auth: Randy
Date: 2014-08-13
Desc: 目标学员修改
*/

/*修改目标学员的设置， 每天会有一个线程，根据每个课程目标学习设置，把课程的目标学员加载到这个表中，以提高平时应用中的查询效率。
   如果用户对应课程在这个表中有记录，那表示用户是该课程的目标学员
   以后查课程的目标学员都查这个表的数据，不要再去查aeItemTargetRuleDetail表
*/
CREATE TABLE dbo.itemTargetLrnDetail
	(
	itd_itm_id int NOT NULL,
	itd_usr_ent_id int NOT NULL,           
	itd_group_ind int NOT NULL default 0,  --用户组关键维度
	itd_grade_ind int NOT NULL default 0, --职级关键维度
	itd_position_ind int NOT NULL default 0, --岗位关键
	itd_compulsory_ind int NOT NULL default 0   --是否为必修
	)
GO
ALTER TABLE itemTargetLrnDetail ADD CONSTRAINT
	PK_itemTargetLrnDetail PRIMARY KEY
	(
	itd_itm_id,
	itd_usr_ent_id
	)

GO

alter table aeitemtargetrule add itr_group_ind int; --用户组关键维度
alter table aeitemtargetrule add itr_grade_ind int; --职级关键维度
alter table aeitemtargetrule add itr_position_ind int; --岗位关键
alter table aeitemtargetrule add itr_compulsory_ind int; --是否为必修
alter table aeApplication add app_nominate_type nvarchar(20) NULL; --报名推荐类型，SUP：上司推推荐，TADM:公司推荐
--drop table aeItemTartgetRuleDetail;


/*用户职位表*/
create table UserPosition(
	upt_id		int identity(1,1) primary key,
	upt_code	nvarchar(255) not null,
	upt_title	nvarchar(255) not null,
	upt_desc	ntext null,
	upt_tcr_id	int not null default 1,
	pfs_update_usr_id int null,
	pfs_update_time	datetime null
)
GO

/*用户与职位关系表
 */
CREATE TABLE UserPositionRelation
	(
	upr_upt_id int NOT NULL,
	upr_usr_ent_id int NOT NULl
	)
GO
ALTER TABLE UserPositionRelation ADD CONSTRAINT
	PK_UserPositionRelation PRIMARY KEY CLUSTERED
	(
	upr_upt_id,
	upr_usr_ent_id
	)
GO

/*更改目标学员设置表中的列名，指定到用户岗位*/
sp_rename   'aeitemtargetrule.itr_skill_id', 'itr_upt_id', 'column';
sp_rename   'aeitemtargetruledetail.ird_skill_id', 'ird_upt_id', 'column';
   
/*由于能力模型这一块功能将会删除，所以清空原来目标学员中的岗位设置，
   如果旧项目升级且要保留原来用户的岗位信息，则不需要执行以下两条SQL，但需要写程序把旧的岗位信息及用户与岗位的关系，
   迁移到UserPosition，UserPositionRelation， 并更新 aeitemtargetrule， aeitemtargetruledetail中对应的upt_id的值
*/
update aeitemtargetrule set itr_upt_id = -1;
update aeitemtargetruledetail set ird_upt_id = -1;
GO

/*不再需要这个存贮过程*/
drop PROCEDURE getItemsForTargetUser;


/*删除能力模型这一功能模块*/
delete from  achomepage where ac_hom_ftn_ext_id in('CM_MAIN','CM_SKL_ANALYSIS','CM_ASS_MAIN');
delete from acrolefunction where rfn_ftn_id in(select ftn_id from acfunction where ftn_ext_id in('CM_MAIN','CM_SKL_ANALYSIS','CM_ASS_MAIN'));
delete from acfunction where  ftn_ext_id in('CM_MAIN','CM_SKL_ANALYSIS','CM_ASS_MAIN');


/*==============================================================*/
/* Table: 文章 	                                               */
/*==============================================================*/
create table article (  
   art_id               int                  identity,
   art_title            varchar(255)         null,
   art_introduction     varchar(2000)        null,
   art_keywords         varchar(255)         null,
   art_content          ntext        null,
   art_user_id          int                  null,
   art_create_datetime  datetime             null,
   art_update_datetime  datetime             null,
   art_update_user_id   int                  null,
   art_location         varchar(255)         null,
   art_type             varchar(10)          null,
   art_status           int                  null,
   art_tcr_id           int                  null,
   art_push_mobile      int                  null,
   art_is_html          int                  null,
   constraint PK_ARTICLE primary key (art_id)
)
go


/* 
 Auth: leon
添加文章管理TADM的权限
*/
insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('ARTICLE_MAIN', 'FUNCTION', 'HOMEPAGE', getdate(), null);

insert into acRoleFunction (rfn_rol_id, rfn_ftn_id)
select rol_id, ftn_id from acRole, acFunction
where rol_ext_id like 'TADM_%' and ftn_ext_id = 'ARTICLE_MAIN';

insert into acHomePage (ac_hom_rol_ext_id,ac_hom_ftn_ext_id, ac_hom_create_usr_id, ac_hom_create_timestamp)
select rol_ext_id, 'ARTICLE_MAIN', 's1u3', getdate() from acRole where rol_ext_id like 'TADM_%';

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
alter table SitePoster add sp_logo_file_cn nvarchar (100) NOT NULL default 'logo.jpg';
alter table SitePoster add sp_logo_file_hk nvarchar (100) NOT NULL default 'logo.jpg';
alter table SitePoster add sp_logo_file_us nvarchar (100) NOT NULL default 'logo.jpg';

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
insert into acRoleFunction select rol_id,ftn_id,'slu3',getdate() from acRole,acFunction where rol_ext_id like 'TADM_%' and ftn_ext_id='POSTER_MAIN';
insert into acHomePage select null,rol_ext_id,'POSTER_MAIN','s1u3',getdate() from acRole where rol_ext_id like 'TADM_%';

/* 
 Auth: lance
  为系统管理员增加在线问答管理权限
*/
insert into acRoleFunction select rol_id,ftn_id,'slu3',getdate() from acRole,acFunction where rol_ext_id like 'ADM_%' and ftn_ext_id='KNOW_MAIN';
insert into acHomePage select null,rol_ext_id,'KNOW_MAIN','s1u3',getdate() from acRole where rol_ext_id like 'ADM_%';

/* 
 Auth: lance
  增加文章类型表
*/
create table articleType(
	aty_id               	int        			identity,
	aty_title            	varchar(255)        null,
	aty_tcr_id				int 				null,
	aty_create_user_id  	nvarchar(50)        null,
	aty_create_datetime  	datetime            null,
	aty_update_user_id   	nvarchar(50)        null,
	aty_update_datetime  	datetime            null,
	constraint PK_ARTICLETYPE primary key (aty_id)
)
go

/* 
 Auth: lance
  为系统管理员增加文章管理权限
*/
insert into acRoleFunction select rol_id,ftn_id,'slu3',getdate() from acRole,acFunction where rol_ext_id like 'ADM_%' and ftn_ext_id='ARTICLE_MAIN';
insert into acHomePage select null,rol_ext_id,'ARTICLE_MAIN','s1u3',getdate() from acRole where rol_ext_id like 'ADM_%';

/*
Auth: Lance
Date: 2014-9-5
Desc: 为个人信息隐私设置增加我的档案和我的收藏设置
*/
alter table sns_setting add s_set_my_files int;  
alter table sns_setting add s_set_my_collection int;
Go


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
VALUES('SYS_CREATE_GROUP','SYS_CREATE_GROUP',0,0,0,1,'SYS',1,10,'s1u3',getdate(),'s1u3',getdate(),null,null);

--参与群组积分
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_period,cty_hit)
VALUES('SYS_JION_GROUP','SYS_JION_GROUP',0,0,0,1,'SYS',1,10,'s1u3',getdate(),'s1u3',getdate(),null,null);

--被点赞积分
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_period,cty_hit)
VALUES('SYS_GET_LIKE','SYS_GET_LIKE',0,0,0,1,'SYS',1,10,'s1u3',getdate(),'s1u3',getdate(),null,null);

--点赞积分
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_period,cty_hit)
VALUES('SYS_CLICK_LIKE','SYS_CLICK_LIKE',0,0,0,1,'SYS',1,10,'s1u3',getdate(),'s1u3',getdate(),null,null);

--发表课程评论积分
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_period,cty_hit)
VALUES('SYS_COS_COMMENT','SYS_COS_COMMENT',0,0,0,1,'SYS',1,5,'s1u3',getdate(),'s1u3',getdate(),'MONTH',10);

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
	s_cnt_id int primary key identity(1,1),
	s_cnt_target_id int default(0),
	s_cnt_collect_count int default(0),
	s_cnt_share_count int default(0),
	s_cnt_like_count int default(0),
	s_cnt_update_time datetime,
	s_cnt_create_time datetime,
	s_cnt_create_usr_id int,
	s_cnt_update_usr_id int,
	s_cnt_module varchar(32)
)

/* 
 Auth: lance
  为群组表加上培训中心id字段
*/
alter table sns_group add s_grp_tcr_id int;
alter table sns_group add s_grp_card nvarchar (100);

/* 
 Auth: Jimmy
  试卷显示方式增加"一屏多题"
*/
alter table module add  mod_test_style nvarchar(10) default 'many';

/* 
 Auth: Joyce
  新邮件模板及发送邮件相关表
*/

--邮件模板
CREATE TABLE messageTemplate
	(
	mtp_id int primary key identity(1,1),
	mtp_type nvarchar(255) NOT NULL, --模板类型
	mtp_subject nvarchar(255) NOT NULL,--邮件默认主题
	mtp_content ntext NOT NULL,--邮件内容
	mtp_content_email_link nvarchar(255) NULL,--内容url(邮件)
	mtp_content_pc_link nvarchar(255) NULL,--内容url(站内信电脑)
	mtp_content_mobile_link nvarchar(255) NULL,--内容url(站内信手机)
	mtp_remark_label nvarchar(255) NULL,--备注Label
	mtp_web_message_ind bit NOT NULL default(1),--是否站内信
	mtp_active_ind bit NOT NULL default(1),--是否启用
	mtp_tcr_id int NOT NULL,--关联培训中心ID(LN启用)
	mtp_update_ent_id int NOT NULL,
	mtp_update_timestamp datetime NOT NULL
	);

--邮件模板参数
CREATE TABLE messageParamName
	(
	mpn_id int primary key identity(1,1),
	mpn_mtp_id int NOT NULL, --模板类型
	mpn_name nvarchar(255) NOT NULL, --参数名
	mpn_name_desc nvarchar(255) NOT NULL, --参数名描述
	);
ALTER TABLE messageParamName ADD CONSTRAINT FK_mpn_mtp 
FOREIGN KEY (mpn_mtp_id) REFERENCES dbo.messageTemplate (mtp_id);

--邮件
CREATE TABLE emailMessage
	(
	emsg_id int primary key identity(1,1),
	emsg_mtp_id int NULL, --模板ID
	emsg_send_ent_id int NOT NULL, --发件人
	emsg_rec_ent_ids nvarchar(2000) NOT NULL, --收件人
	emsg_cc_ent_ids nvarchar(2000) NULL, --抄送人
	emsg_subject nvarchar(255) NOT NULL,--邮件主题
	emsg_content ntext NOT NULL,--邮件内容
	emsg_target_datetime datetime NULL,--定时发送时间
	emsg_create_ent_id  int NOT NULL, --创建人
	emsg_create_timestamp datetime NOT NULL,--创建时间
	);
ALTER TABLE emailMessage ADD CONSTRAINT FK_emsg_mtp 
FOREIGN KEY (emsg_mtp_id) REFERENCES dbo.messageTemplate (mtp_id);

--站内信
CREATE TABLE webMessage
	(
	wmsg_id int primary key identity(1,1),
	wmsg_mtp_id int NULL, --模板ID
	wmsg_send_ent_id int NOT NULL, --发件人
	wmsg_rec_ent_id int NOT NULL, --收件人
	wmsg_subject nvarchar(255) NOT NULL,--主题
	wmsg_content_pc ntext NOT NULL,--内容(电脑)
	wmsg_content_mobile ntext NULL,--内容(手机)
	wmsg_target_datetime datetime NULL,--定时发送时间
	wmsg_type nvarchar(64) NOT NULL default 'SYS',--信息类型
	wmsg_create_ent_id  int NOT NULL, --创建人
	wmsg_create_timestamp datetime NOT NULL,--创建时间
	);
ALTER TABLE webMessage ADD CONSTRAINT FK_wmsg_mtp 
FOREIGN KEY (wmsg_mtp_id) REFERENCES dbo.messageTemplate (mtp_id);

--邮件发送历史
CREATE TABLE emailMsgRecHistory
	(
	emrh_emsg_id  int NOT NULL, --邮件ID
	emrh_status nvarchar(20) NOT NULL,--Y:发送成功 N:发送失败
	emrh_sent_datetime datetime NULL,--发送时间
    emrh_attempted int NOT NULL default 0 --发送次数
	);
ALTER TABLE emailMsgRecHistory ADD CONSTRAINT FK_emrh_wmsg 
FOREIGN KEY (emrh_emsg_id) REFERENCES dbo.emailMessage(emsg_id);

CREATE INDEX IX_emailMsgRecHistory ON emailMsgRecHistory(emrh_status,emrh_attempted)

--站内信阅读历史
CREATE TABLE webMsgReadHistory
	(
	wmrh_wmsg_id  int NOT NULL, --消息ID
	wmrh_status nvarchar(20) NOT NULL,--Y:已读 N或NULL: 未读
	wmrh_read_datetime datetime NULL--阅读时间
	);

ALTER TABLE webMsgReadHistory ADD CONSTRAINT FK_wmrh_emsg 
FOREIGN KEY (wmrh_wmsg_id) REFERENCES dbo.webMessage(wmsg_id);


CREATE INDEX IX_webMsgReadHistory ON webMsgReadHistory(wmrh_status)




/**
 * Leon
 * 动态的附件
 */
create table moduleTempFile(
	mtf_id int primary key identity(1,1),
	mtf_target_id int,
	mtf_module nvarchar(32),
	mtf_usr_id int not null,
	mtf_file_type nvarchar(32),
	mtf_file_name nvarchar(255),
	mtf_file_rename nvarchar(255),
	mtf_file_size bigint,
	mtf_create_time datetime,
	mtf_url nvarchar(255), 	
	mtf_type nvarchar(32)
)

/**
 * lance
 * 为群组添加状态字段
 */
alter table sns_group add s_grp_status nvarchar(20) default 'OK';

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
	ls_id int primary key identity(1,1),
	ls_ent_id int NOT NULL,						--学习概况者
	ls_learn_duration int NOT NULL,				--学习总时长
	ls_learn_credit int NOT NULL,				--总学分
	ls_total_integral int NOT NULL,				--总积分
	ls_total_courses int NOT NULL,				--课程总数
	ls_course_completed_num int NOT NULL,		--已完成课程总数
	ls_course_fail_num int NOT NULL,			--未完成课程总数
	ls_course_inprogress_num int NOT NULL,		--进行中课程总数
	ls_course_pending_num int NOT NULL,			--审批中课程总数
	ls_total_exams int NOT NULL,				--考试总数
	ls_exam_completed_num int NOT NULL,			--已完成考试总数
	ls_exam_fail_num int NOT NULL, 				--未完成考试总数
	ls_exam_inprogress_num int NOT NULL,		--进行中考试总数
	ls_exam_pending_num int NOT NULL,			--审批中考试总数
	ls_fans_num int NOT NULL,					--粉丝
	ls_attention_num int NOT NULL,				--关注
	ls_praised_num int NOT NULL,				--被赞
	ls_praise_others_num int NOT NULL,			--赞他人
	ls_collect_num int NOT NULL,				--收藏
	ls_share_num int NOT NULL,					--分享
	ls_create_group_num int NOT NULL,			--创建群组
	ls_join_group_num int NOT NULL,				--参与群组
	ls_group_speech_num int NOT NULL,			--群组发言
	ls_question_num int NOT NULL,				--提问数
	ls_answer_num int NOT NULL,					--回答数
	ls_update_time datetime NOT NULL			--更新时间 
);

/*
Auth: Joyce
Date: 2014-10-4
Desc: add new function 邮件模板管理
*/
insert into acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('MESSAGE_TEMPLATE', 'FUNCTION', 'HOMEPAGE', getdate(), null);

insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp)
select rol_id, ftn_id, getdate() from acRole, acFunction
where rol_ext_id like 'ADM_%' and ftn_ext_id = 'MESSAGE_TEMPLATE';

insert into acHomePage (ac_hom_rol_ext_id,ac_hom_ftn_ext_id, ac_hom_create_usr_id, ac_hom_create_timestamp)
select rol_ext_id, 'MESSAGE_TEMPLATE', 's1u3', getdate() from acRole where rol_ext_id like 'ADM_%';

-- 初始化邮件模板及模板参数
insert into messageTemplate(mtp_type, mtp_subject, mtp_content, mtp_remark_label, mtp_web_message_ind, mtp_active_ind, mtp_tcr_id, mtp_update_timestamp, mtp_update_ent_id) values ('ENROLLMENT_NEW','Course Enrollment Request Received','Course Enrollment Request Received','label_ENROLLMENT_NEW',1,1,1,getdate(),3);
insert into messageTemplate(mtp_type, mtp_subject, mtp_content, mtp_remark_label, mtp_web_message_ind, mtp_active_ind, mtp_tcr_id, mtp_update_timestamp, mtp_update_ent_id) values ('ENROLLMENT_CONFIRMED','Course Enrollment Confirmed','Course Enrollment Confirmed','label_ENROLLMENT_CONFIRMED',1,1,1,getdate(),3);
insert into messageTemplate(mtp_type, mtp_subject, mtp_content, mtp_remark_label, mtp_web_message_ind, mtp_active_ind, mtp_tcr_id, mtp_update_timestamp, mtp_update_ent_id) values ('ENROLLMENT_WAITLISTED','Course Enrollment: Waiting List','Course Enrollment: Waiting List','label_ENROLLMENT_WAITLISTED',1,1,1,getdate(),3);
insert into messageTemplate(mtp_type, mtp_subject, mtp_content, mtp_remark_label, mtp_web_message_ind, mtp_active_ind, mtp_tcr_id, mtp_update_timestamp, mtp_update_ent_id) values ('ENROLLMENT_NOT_CONFIRMED','Course Enrollment Request Declined','Course Enrollment Request Declined','label_ENROLLMENT_NOT_CONFIRMED',1,1,1,getdate(),3);
insert into messageTemplate(mtp_type, mtp_subject, mtp_content, mtp_remark_label, mtp_web_message_ind, mtp_active_ind, mtp_tcr_id, mtp_update_timestamp, mtp_update_ent_id) values ('ENROLLMENT_WITHDRAWAL_APPROVED','Course Enrollment Request Cancelled','Course Enrollment Request Cancelled','label_ENROLLMENT_WITHDRAWAL_APPROVED',1,1,1,getdate(),3);
insert into messageTemplate(mtp_type, mtp_subject, mtp_content, mtp_remark_label, mtp_web_message_ind, mtp_active_ind, mtp_tcr_id, mtp_update_timestamp, mtp_update_ent_id) values ('ENROLLMENT_REMOVED','Course Enrollment Request Cancelled','Course Enrollment Request Cancelled','label_ENROLLMENT_REMOVED',1,1,1,getdate(),3);
insert into messageTemplate(mtp_type, mtp_subject, mtp_content, mtp_remark_label, mtp_web_message_ind, mtp_active_ind, mtp_tcr_id, mtp_update_timestamp, mtp_update_ent_id) values ('ENROLLMENT_NEXT_APPROVERS','Course Enrollment Approval','Course Enrollment Approval','label_ENROLLMENT_NEXT_APPROVERS',1,1,1,getdate(),3);
insert into messageTemplate(mtp_type, mtp_subject, mtp_content, mtp_remark_label, mtp_web_message_ind, mtp_active_ind, mtp_tcr_id, mtp_update_timestamp, mtp_update_ent_id) values ('ENROLLMENT_REMOVED_REMINDER','Course Enrollment Notification','Course Enrollment Notification','label_ENROLLMENT_REMOVED_REMINDER',1,1,1,getdate(),3);
insert into messageTemplate(mtp_type, mtp_subject, mtp_content, mtp_remark_label, mtp_web_message_ind, mtp_active_ind, mtp_tcr_id, mtp_update_timestamp, mtp_update_ent_id) values ('ENROLLMENT_WITHDRAWAL_REMINDER','Course Enrollment Notification','Course Enrollment Notification','label_ENROLLMENT_WITHDRAWAL_REMINDER',1,1,1,getdate(),3);
insert into messageTemplate(mtp_type, mtp_subject, mtp_content, mtp_remark_label, mtp_web_message_ind, mtp_active_ind, mtp_tcr_id, mtp_update_timestamp, mtp_update_ent_id) values ('ENROLLMENT_CONFIRMED_REMINDER','Course Enrollment Notification','Course Enrollment Notification','label_ENROLLMENT_CONFIRMED_REMINDER',1,1,1,getdate(),3);
insert into messageTemplate(mtp_type, mtp_subject, mtp_content, mtp_remark_label, mtp_web_message_ind, mtp_active_ind, mtp_tcr_id, mtp_update_timestamp, mtp_update_ent_id) values ('ENROLLMENT_APPROVED_REMINDER','Course Enrollment Notification','Course Enrollment Notification','label_ENROLLMENT_APPROVED_REMINDER',1,1,1,getdate(),3);
insert into messageTemplate(mtp_type, mtp_subject, mtp_content, mtp_remark_label, mtp_web_message_ind, mtp_active_ind, mtp_tcr_id, mtp_update_timestamp, mtp_update_ent_id) values ('ENROLLMENT_NO_SUPERVISOR','Course Enrollment Approval','Course Enrollment Approval','label_ENROLLMENT_NO_SUPERVISOR',1,1,1,getdate(),3);
insert into messageTemplate(mtp_type, mtp_subject, mtp_content, mtp_remark_label, mtp_web_message_ind, mtp_active_ind, mtp_tcr_id, mtp_update_timestamp, mtp_update_ent_id) values ('COURSE_NOTIFY','Course Completion Reminder','Course Completion Reminder','label_COURSE_NOTIFY',1,1,1,getdate(),3);
insert into messageTemplate(mtp_type, mtp_subject, mtp_content, mtp_remark_label, mtp_web_message_ind, mtp_active_ind, mtp_tcr_id, mtp_update_timestamp, mtp_update_ent_id) values ('JI','Joining Instruction','Joining Instruction','label_JI',1,1,1,getdate(),3);
insert into messageTemplate(mtp_type, mtp_subject, mtp_content, mtp_remark_label, mtp_web_message_ind, mtp_active_ind, mtp_tcr_id, mtp_update_timestamp, mtp_update_ent_id) values ('REMINDER','Reminder','Reminder','label_REMINDER',1,1,1,getdate(),3);
insert into messageTemplate(mtp_type, mtp_subject, mtp_content, mtp_remark_label, mtp_web_message_ind, mtp_active_ind, mtp_tcr_id, mtp_update_timestamp, mtp_update_ent_id) values ('USR_REG_APPROVE','USR_REG_APPROVE','USR_REG_APPROVE','label_USR_REG_APPROVE',1,1,1,getdate(),3);
insert into messageTemplate(mtp_type, mtp_subject, mtp_content, mtp_remark_label, mtp_web_message_ind, mtp_active_ind, mtp_tcr_id, mtp_update_timestamp, mtp_update_ent_id) values ('USR_REG_DISAPPROVE','USR_REG_DISAPPROVE','USR_REG_DISAPPROVE','label_USR_REG_DISAPPROVE',1,1,1,getdate(),3);
insert into messageTemplate(mtp_type, mtp_subject, mtp_content, mtp_remark_label, mtp_web_message_ind, mtp_active_ind, mtp_tcr_id, mtp_update_timestamp, mtp_update_ent_id) values ('USER_IMPORT_SUCCESS','User Import Success Notification','User Import Success Notification','label_USER_IMPORT_SUCCESS',1,1,1,getdate(),3);
insert into messageTemplate(mtp_type, mtp_subject, mtp_content, mtp_remark_label, mtp_web_message_ind, mtp_active_ind, mtp_tcr_id, mtp_update_timestamp, mtp_update_ent_id) values ('ENROLLMENT_IMPORT_SUCCESS','Enrollment Import Success Notification','Enrollment Import Success Notification','label_ENROLLMENT_IMPORT_SUCCESS',1,1,1,getdate(),3);
insert into messageTemplate(mtp_type, mtp_subject, mtp_content, mtp_remark_label, mtp_web_message_ind, mtp_active_ind, mtp_tcr_id, mtp_update_timestamp, mtp_update_ent_id) values ('SYS_PERFORMANCE_NOTIFY','System Performance Notify','System Performance Notify','label_SYS_PERFORMANCE_NOTIFY',1,1,1,getdate(),3);

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
insert into messageTemplate(mtp_type, mtp_subject, mtp_content, mtp_remark_label, mtp_web_message_ind, mtp_active_ind, mtp_tcr_id, mtp_update_timestamp, mtp_update_ent_id) values ('EMPTY_TYPE','empty subject','empty content','label_empty_type',1,0,1,getdate(),3);

insert into messageParamName select mtp_id, '[Login id]', 'lab_msg_login_id' from messageTemplate where mtp_type='USR_REG_APPROVE';

update messageParamName set mpn_name='[Course period end date]' where mpn_name='[Course period end date] ';


/*
Auth: Joyce
Date: 2014-10-4
Desc: 开课通知
*/
Drop Table aeItemMessage;
create table aeItemMessage(
	[img_itm_id] [int] NOT NULL, --班级ID
	[img_app_id] [int] NOT NULL, --App ID
	[img_msg_id] [int] NOT NULL, --Message ID
	[img_msg_type] [nvarchar](20) NOT NULL,--Message Type (email|web)
	[img_mtp_type] [nvarchar](20) NOT NULL, --(JI|REMINDER)
	[img_create_usr_id] [nvarchar](20) NOT NULL,
	[img_create_timestamp] [datetime] NOT NULL,
	[img_update_usr_id] [nvarchar](20) NOT NULL,
	[img_update_timestamp] [datetime] NOT NULL,
	constraint PK_aeItemMessage primary key (img_itm_id, img_msg_id, img_msg_type)
)

alter table aeItem add itm_ji_send_datetime datetime;
alter table aeItem add itm_reminder_send_datetime datetime;

/**
 * Randy
 * 重建视图
 */
/* 用户关系视图 */
IF EXISTS (SELECT name FROM sysobjects WHERE name = 'V_usrTcRelation' AND type = 'V')
	DROP view [V_usrTcRelation]
GO
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
alter table EmailMessage add emsg_attachment nvarchar(2000);--附件，多个逗号分隔
alter table webMessage add wmsg_attachment nvarchar(2000);--附件，多个逗号分隔


/**
 * 动态通知类型的回复对象id
*/
alter table sns_doing add s_doi_reply_id int	--回复动态
alter table sns_doing add s_doi_operator_uid int	--操作的人
alter table sns_doing add s_doi_target_type nvarchar(20)	--1 ,表示为通知，0不是

/**
* 删除评论的时候，删除与之相关联的评论，赞，以及统计信息
**/
  Create trigger delCommentRel
  On sns_comment
  for Delete
  As
  begin
  Delete sns_comment From sns_comment sc , deleted d
  Where sc.s_cmt_target_id = d.s_cmt_id;
  delete sns_valuation_log from sns_valuation_log, deleted d
  where d.s_cmt_id = s_vtl_target_id and s_vtl_module = d.s_cmt_module
  delete sns_count from sns_count,deleted d
  where d.s_cmt_id = s_cnt_target_id and s_cnt_module = d.s_cmt_module
  end
  
  /**
  删除动态的时候删除附带的评论，赞一记统计信息
  */
  drop trigger delDoingRel;
  create trigger delDoingRel
  on sns_doing
  for delete
  as
  begin
  delete sns_comment from sns_comment sc, deleted d
  where sc.s_cmt_target_id = d.s_doi_id
  delete sns_doing from sns_doing sd, deleted d
  where sd.s_doi_target_id = d.s_doi_id and sd.s_doi_target_type = '1'
  delete sns_valuation_log from sns_valuation_log,deleted d
  where s_vtl_target_id = d.s_doi_id and s_vtl_module = d.s_doi_module
  delete sns_count from sns_count,deleted d
  where d.s_doi_id = s_cnt_target_id and s_cnt_module = d.s_doi_module
  end

/*
Auth: Joyce
Date: 2014-10-4
Desc: 新增找回密码邮件
*/

insert into messageTemplate(mtp_type, mtp_subject, mtp_content, mtp_remark_label, mtp_web_message_ind, mtp_active_ind, mtp_tcr_id, mtp_update_timestamp, mtp_update_ent_id) 
values ('USER_PWD_RESET_NOTIFY',N'找回密码',N'找回密码','lab_USER_PWD_RESET_NOTIFY',0,1,1,getdate(),3);

insert into messageParamName select mtp_id, '[Request time]', 'lab_msg_request_time' from messageTemplate where mtp_type='USER_PWD_RESET_NOTIFY';
insert into messageParamName select mtp_id, '[Max days]', 'lab_msg_max_days' from messageTemplate where mtp_type='USER_PWD_RESET_NOTIFY';
insert into messageParamName select mtp_id, '[Link to reset password]', 'lab_msg_link_to_reset_password' from messageTemplate where mtp_type='USER_PWD_RESET_NOTIFY';

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
update messageTemplate set mtp_content = N'<span><span style="color:black;font-family:宋体;font-size:11pt;">当前活动用户量<span> : </span></span><span style="color:blue;font-family:宋体;font-size:11pt;">[Active user]<br />  </span><span style="color:black;font-family:宋体;font-size:11pt;">Performance warning level : </span><span style="color:blue;font-family:宋体;font-size:11pt;">[Warning user]<br />  </span><span style="font-family:宋体;font-size:11pt;">最大活动用户数 :<span>&nbsp;<span style="color:#0000FF;font-family:Verdana, Geneva, Arial, Helvetica, sans-serif;line-height:normal;"><span style="color:#0000FF;font-family:宋体;font-size:15px;line-height:22px;">[Blocking user]</span></span></span></span><span style="color:black;font-family:宋体;font-size:11pt;"><br />  </span><span style="color:black;font-family:宋体;font-size:11pt;">发生时间<span> : </span></span><span style="color:#0000CC;font-family:宋体;font-size:11pt;"><span style="color:#0000FF;font-family:宋体;font-size:15px;line-height:22px;">[Warning date]</span></span></span>   <p>   <br />  </p>' where mtp_type='SYS_PERFORMANCE_NOTIFY';
update messageTemplate set mtp_content = N'亲爱的 先生/女士:<br />  <br />  你的“忘记密码”的要求已在<span style="color:#0000FF;font-family:Verdana, Geneva, Arial, Helvetica, sans-serif;line-height:normal;">[Request time]</span>接收。<br />  <p>   请点击以下链接重置你的密码：  </p>  <p>   <span style="color:#0000FF;font-family:Verdana, Geneva, Arial, Helvetica, sans-serif;line-height:normal;">[Link to reset password]</span><span style="line-height:1.5;">，此链接将在</span><span style="color:#0000FF;font-family:Verdana, Geneva, Arial, Helvetica, sans-serif;line-height:normal;">[Max days]</span><span style="line-height:1.5;">天后失效。</span>  </p>' where mtp_type='USER_PWD_RESET_NOTIFY';


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
values ('SMS_GROUP_MANAGE', 'FUNCTION', 'HOMEPAGE', getdate(), null);

insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp)
select rol_id, ftn_id, getdate() from acRole, acFunction
where rol_ext_id like 'TADM_%' and ftn_ext_id = 'SMS_GROUP_MANAGE';

insert into acHomePage (ac_hom_rol_ext_id,ac_hom_ftn_ext_id, ac_hom_create_usr_id, ac_hom_create_timestamp)
select rol_ext_id, 'SMS_GROUP_MANAGE', 's1u3', getdate() from acRole where rol_ext_id like 'TADM_%';



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
  eip_id int NOT NULL IDENTITY (1, 1), 
  eip_code nvarchar(50) NOT NULL,
  eip_name nvarchar(255) NOT NULL, 
  eip_tcr_id int NOT NULL,   
  eip_account_num int NOT NULL,  
  eip_status nvarchar(10) NOT NULL,  
  eip_domain nvarchar(50) NULL,    
  eip_login_bg nvarchar(50) NULL,  
  eip_create_timestamp datetime NOT NULL,  
  eip_create_usr_id nvarchar(20) NOT NULL, 
  eip_update_timestamp datetime NOT NULL, 
  eip_update_usr_id nvarchar(20) NOT NULL  
);

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
values ('EIP_MAIN', 'FUNCTION', 'HOMEPAGE', getdate(), null); 

insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp)
select rol_id, ftn_id,getdate() from acRole, acFunction
where rol_ext_id like 'ADM_%' and ftn_ext_id = 'EIP_MAIN';

insert into acHomePage (ac_hom_rol_ext_id,ac_hom_ftn_ext_id, ac_hom_create_usr_id, ac_hom_create_timestamp)
select rol_ext_id, 'EIP_MAIN', 's1u3', getdate() from acRole where rol_ext_id like 'ADM_%';

/*
Auth: myron
Date: 2014-10-31
Desc: 移动课程背景
*/
alter table EnterpriseInfoPortal add eip_mobile_login_bg nvarchar(255) null;

/**
 * 删除企业信息表中关于登陆图片的字段
 */
alter table EnterpriseInfoPortal drop column eip_mobile_login_bg
alter table EnterpriseInfoPortal drop column eip_login_bg

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
alter table SitePoster add login_bg_file1 nvarchar(255) null;
alter table SitePoster add login_bg_file2 nvarchar(255) null;
alter table SitePoster add login_bg_file3 nvarchar(255) null;
alter table SitePoster add login_bg_file4 nvarchar(255) null;
alter table SitePoster add login_bg_file5 nvarchar(255) null;
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
values ('COURSE_MAIN', 'FUNCTION', 'HOMEPAGE', getdate(), null); 

insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp)
select rol_id, ftn_id,getdate() from acRole, acFunction
where rol_ext_id like 'ADM_%' and ftn_ext_id = 'COURSE_MAIN';

insert into acHomePage (ac_hom_rol_ext_id,ac_hom_ftn_ext_id, ac_hom_create_usr_id, ac_hom_create_timestamp)
select rol_ext_id, 'COURSE_MAIN', 's1u3', getdate() from acRole where rol_ext_id like 'ADM_%';

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
  dui_usr_ste_usr_id nvarchar(255) NOT NULL ,
  dui_usr_display_bil nvarchar(255) NOT NULL ,
  dui_delete_timestamp datetime NOT NULL 
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
('SYS_ANWSER_BOUNTY','SYS_ANWSER_BOUNTY','0','1','0','1','SYS','0','0','s1u3',getdate(),'s1u3',getDate(),1);

INSERT INTO creditsType
(cty_code, cty_title, cty_deduction_ind, cty_manual_ind, cty_deleted_ind, 
cty_relation_total_ind,cty_relation_type,cty_default_credits_ind, cty_default_credits,
 cty_create_usr_id, cty_create_timestamp, cty_update_usr_id, cty_update_timestamp, cty_tcr_id) 
VALUES 
('SYS_QUESTION_BOUNTY', 'SYS_QUESTION_BOUNTY','1','1','0','1','SYS','0','0','s1u3',getdate(),'s1u3',getDate(),1)
/*
 * 在提问表内加入悬赏积分字段
 */
alter table dbo.knowQuestion add que_bounty float null default 0


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
alter table article alter column art_title  nvarchar(255);
alter table article alter column art_introduction  nvarchar(2000);
alter table article alter column art_keywords  nvarchar(255);
alter table article alter column art_location  nvarchar(255);
alter table article alter column art_type  nvarchar(10);
alter table articleType alter column aty_title  nvarchar(255);


 /**
 Auth: leon	
 Date: 2014-12-2
 Desc: 删除评论的时候删除赞，统计，通知，回复
 */
  drop trigger delCommentRel;
  GO
  Create trigger delCommentRel
  On sns_comment
  for Delete
  As
  begin
  Delete sns_comment From sns_comment sc , deleted d
  Where sc.s_cmt_target_id = d.s_cmt_id or sc.s_cmt_reply_to_id = d.s_cmt_id
  Delete sns_doing from sns_doing, deleted d
  where (d.s_cmt_id = s_doi_reply_id or d.s_cmt_id = s_doi_act_id) and s_doi_target_type = '1' and s_doi_module = d.s_cmt_module
  Delete sns_doing from sns_doing, deleted d
  where d.s_cmt_id = s_doi_target_id and s_doi_target_type = '1' and s_doi_module = 'Comment'
  delete sns_valuation_log from sns_valuation_log, deleted d
  where d.s_cmt_id = s_vtl_target_id and s_vtl_module = d.s_cmt_module
  delete sns_count from sns_count,deleted d
  where d.s_cmt_id = s_cnt_target_id and s_cnt_module = d.s_cmt_module
  end
  
  
  /**
   Auth: leon	
   Date: 2014-12-2
   Desc:删除动态的时候删除附带的评论，赞一记统计信息
  */
  drop trigger delDoingRel;
  go
  create trigger delDoingRel
  on sns_doing
  for delete
  as
  begin
  declare @isNotice int
  select @isNotice = s_doi_target_type from deleted;
  if(@isNotice < 1)
	  begin
	  delete sns_doing from sns_doing,deleted d
	  where d.s_doi_id = sns_doing.s_doi_target_id
	  delete sns_doing from sns_doing sd, deleted d
	  where sd.s_doi_target_id = d.s_doi_id and sd.s_doi_target_type = '1'
	  delete sns_valuation_log from sns_valuation_log,deleted d
	  where s_vtl_target_id = d.s_doi_id and s_vtl_module = d.s_doi_module
	  delete sns_count from sns_count,deleted d
	  where d.s_doi_id = s_cnt_target_id and s_cnt_module = d.s_doi_module
	  end
  end
  
  /**
   Auth: joe		
   Date: 2014-12-5
   Desc:文章图示类型
   */
  alter table article add art_icon_file varchar (100);
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
    pcr_last_acc datetime null,
    pcr_note ntext null,
    primary key(pcr_usr_ent_id,pcr_itm_id,pcr_mod_id)
  );
  /*
   Auth: myron
   Date: 2014-12-15
   Desc: 文章中添加访问次数字段 
   */
  alter table article add art_access_count int default 0
  
/*
Auth: elvea
Date: 2014-01-05
Desc: 标签管理
*/
/* 标签*/
create table Tag (
	tag_id int not null identity(1,1),
	tag_title nvarchar(255),
	tag_tcr_id int,
	tag_create_datetime datetime,
	tag_create_user_id nvarchar(50),
	tag_update_datetime datetime,
	tag_update_user_id nvarchar(50),
	constraint PK_TAG_ID primary key (tag_id)
);
Go

alter table tag add constraint FK_TAG_TCR_ID foreign key (tag_tcr_id) references tcTrainingCenter (tcr_id);
GO

/*
Auth: elvea
Date: 2014-01-05
Desc: 知识库管理
*/
delete from acfunction where ftn_ext_id = 'KB_MGT';
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml)
values ('KB_MGT', 'FUNCTION', 'HOMEPAGE', getdate(), null);

INSERT INTO acRoleFunction
select rol_id, ftn_id, 'slu3', getdate() from acRole, acFunction where rol_ste_uid in ('TADM')
and ftn_ext_id = 'KB_MGT';

insert into acHomePage (ac_hom_rol_ext_id,ac_hom_ftn_ext_id, ac_hom_create_usr_id, ac_hom_create_timestamp)
select rol_ext_id, 'KB_MGT', 's1u3', getdate() from acRole where rol_ste_uid in ('TADM');

/**
 * 增加管理员端知识管理菜单
 */
insert into acRoleFunction select rol_id,ftn_id,'slu3',getdate() from acRole,acFunction where rol_ste_uid in ('SADM') and ftn_ext_id='KB_MGT';
insert into acHomePage select null,rol_ext_id,'KB_MGT','s1u3',getdate() from acRole where rol_ste_uid in ('SADM');

GO

/* 知识中心 - 目录 */
create table kb_catalog (
	kbc_id int not null identity(1,1),
	kbc_title nvarchar(255),
	kbc_desc nvarchar(255),
	kbc_status nvarchar(255),
	kbc_tcr_id int,
	kbc_create_datetime datetime,
	kbc_create_user_id nvarchar(50),
	kbc_update_datetime datetime,
	kbc_update_user_id nvarchar(50),
	constraint PK_KB_KBC_ID primary key (kbc_id)
);

alter table kb_catalog add constraint FK_KB_KBC_TCR_ID foreign key (kbc_tcr_id) references tcTrainingCenter (tcr_id);
GO

/* 知识中心 - 知识 */
create table kb_item (
	kbi_id int not null identity(1,1),
	kbi_title nvarchar(255),
	kbi_image nvarchar(255),
	kbi_desc nvarchar(255),
	kbi_content ntext,
	kbi_type nvarchar(255),
	kbi_status nvarchar(255),
	kbi_app_status nvarchar(255),
	kbi_approve_datetime datetime,
	kbi_approve_user_id nvarchar(50),
	kbi_publish_datetime datetime,
	kbi_publish_user_id nvarchar(50),
	kbi_create_datetime datetime,
	kbi_create_user_id nvarchar(50),
	kbi_update_datetime datetime,
	kbi_update_user_id nvarchar(50),
	constraint PK_KB_KBI_ID primary key (kbi_id)
);
GO

/* 知识中心 - 附件 */
create table kb_attachment (
	kba_id int not null identity(1,1),
	kba_filename nvarchar(255),
	kba_file nvarchar(255),
	kba_remark nvarchar(255),
	kba_create_datetime datetime,
	kba_create_user_id nvarchar(50),
	kba_update_datetime datetime,
	kba_update_user_id nvarchar(50),
	constraint PK_KB_KBA_ID primary key (kba_id)
);
GO

/* 知识中心 - 知识和附件关联 */
create table kb_item_attachment (
	kia_kbi_id int not null,
	kia_kba_id int not null,
	kia_create_datetime datetime,
	kia_create_user_id nvarchar(50),
	constraint PK_KB_KIA_ID primary key (kia_kbi_id, kia_kba_id)
);
GO

alter table kb_item_attachment add constraint PK_KB_KIA_KBI_ID foreign key (kia_kbi_id) references kb_item (kbi_id);

alter table kb_item_attachment add constraint PK_KB_KIA_KBA_ID foreign key (kia_kba_id) references kb_attachment (kba_id);
GO

/* 知识中心 - 知识和目录关联 */
create table kb_item_cat (
	kic_kbi_id int not null,
	kic_kbc_id int not null,
	kic_create_datetime datetime,
	kic_create_user_id nvarchar(50),
	constraint PK_KB_KIC_ID primary key (kic_kbi_id, kic_kbc_id)
);
Go

alter table kb_item_cat add constraint FK_KB_KIC_KBI_ID foreign key (kic_kbi_id) references kb_item (kbi_id);

alter table kb_item_cat add constraint FK_KB_KIC_KBC_ID foreign key (kic_kbc_id) references kb_catalog (kbc_id);
GO

/* 知识中心 - 知识和标签关联 */
create table kb_item_tag (
	kit_kbi_id int not null,
	kit_tag_id int not null,
	kit_create_datetime datetime,
	kit_create_user_id nvarchar(50),
	constraint PK_KB_KIT_ID primary key (kit_kbi_id, kit_tag_id)
);

alter table kb_item_tag add constraint FK_KB_KIT_KBI_ID foreign key (kit_kbi_id) references kb_item (kbi_id);

alter table kb_item_tag add constraint FK_KB_KIT_TAG_ID foreign key (kit_tag_id) references tag (tag_id);
GO

/* 知识中心 - 知识浏览表 */
create table kb_item_view (
	kiv_usr_ent_id int not null,
	kiv_kbi_id int not null,
	kiv_create_datetime datetime,
	kiv_update_datetime datetime,
	constraint PK_KB_KIV_ID primary key (kiv_usr_ent_id, kiv_kbi_id)
);
Go

alter table kb_item_view add constraint FK_KB_KIV_KBI_ID foreign key (kiv_kbi_id) references kb_item (kbi_id);
GO
/*
drop table kb_item_attachment;
drop table kb_item_tag;
drop table kb_item_cat;
drop table kb_item_view;
drop table kb_attachment;
drop table kb_item;
drop table kb_catalog;
drop table tag;
*/

/*
 微信表跟apitoken合并
 *
*/
alter table apitoken add atk_wechat_open_id nvarchar(100)

/**
 * 宣传栏增加手机端app启动图片显示
 */
alter table siteposter add guide_file1 varchar(255);
alter table siteposter add guide_file2 varchar(255);
alter table siteposter add guide_file3 varchar(255);
GO
/*
 * 知识表添加浏览量字段
 */
alter table kb_item add kbi_access_count int
GO

/*
 * Auth: Leepec
 * Date: 2014-01-29
 * 知识表添加是否允许下载字段
 * ALLOW  允许
 * INTERDICT 禁止
 */
alter table kb_item add kbi_download nvarchar(50)
GO

/*
 * 知识表添加是下载次数统计字段
 * ALLOW  允许
 * INTERDICT 禁止
 */
alter table kb_item add kbi_download_count int
GO

/*
 * 系统配置
 */
insert into SystemSetting values ('SYS_LDAP_URL', 'ldap://host:port', '2008-03-25 00:00:00', 's1u3', '2008-03-25 00:00:00', 's1u3' );
insert into SystemSetting values ('SYS_LDAP_SUFFIX', '@cwngz.local', '2008-03-25 00:00:00', 's1u3', '2008-03-25 00:00:00', 's1u3' );

insert into SystemSetting values ('SYS_OPENOFFICE_ENABLED', 'true', '2008-03-25 00:00:00', 's1u3', '2008-03-25 00:00:00', 's1u3' );
insert into SystemSetting values ('SYS_OPENOFFICE_ENVIRONMENT', 'Windows', '2008-03-25 00:00:00', 's1u3', '2008-03-25 00:00:00', 's1u3' );
insert into SystemSetting values ('SYS_OPENOFFICE_PATH', 'C:/Program Files (x86)/OpenOffice 4/program/soffice', '2008-03-25 00:00:00', 's1u3', '2008-03-25 00:00:00', 's1u3' );
insert into SystemSetting values ('SYS_OPENOFFICE_HOST', 'localhost', '2008-03-25 00:00:00', 's1u3', '2008-03-25 00:00:00', 's1u3' );
insert into SystemSetting values ('SYS_OPENOFFICE_PORT', '8100', '2008-03-25 00:00:00', 's1u3', '2008-03-25 00:00:00', 's1u3' );

insert into SystemSetting values ('SYS_MAIL_SERVER_HOST', 'host', '2008-03-25 00:00:00', 's1u3', '2008-03-25 00:00:00', 's1u3' );
insert into SystemSetting values ('SYS_MAIL_SERVER_AUTH_ENABLED', 'false', '2008-03-25 00:00:00', 's1u3', '2008-03-25 00:00:00', 's1u3' );
insert into SystemSetting values ('SYS_MAIL_SERVER_ACCOUNT_TYPE', 'EMAIL', '2008-03-25 00:00:00', 's1u3', '2008-03-25 00:00:00', 's1u3' );
insert into SystemSetting values ('SYS_MAIL_SERVER_USER', 'user', '2008-03-25 00:00:00', 's1u3', '2008-03-25 00:00:00', 's1u3' );
insert into SystemSetting values ('SYS_MAIL_SERVER_PASSWORD', 'pass', '2008-03-25 00:00:00', 's1u3', '2008-03-25 00:00:00', 's1u3' );

insert into SystemSetting values ('SYS_MAIL_SCHEDULER_DOMAIN', 'http://localhost:8081/', '2008-03-25 00:00:00', 's1u3', '2008-03-25 00:00:00', 's1u3' );

insert into SystemSetting values ('SYS_WECHAT_DOMAIN', 'http://121.8.157.22', '2008-03-25 00:00:00', 's1u3', '2008-03-25 00:00:00', 's1u3' );
insert into SystemSetting values ('SYS_WECHAT_MOBILE_DOMAIN', 'http://121.8.157.22/mobile/', '2008-03-25 00:00:00', 's1u3', '2008-03-25 00:00:00', 's1u3' );
insert into SystemSetting values ('SYS_WECHAT_SERVER_ID', 'offline', '2008-03-25 00:00:00', 's1u3', '2008-03-25 00:00:00', 's1u3' );
insert into SystemSetting values ('SYS_WECHAT_PORT', '3', '2008-03-25 00:00:00', 's1u3', '2008-03-25 00:00:00', 's1u3' );
insert into SystemSetting values ('SYS_WECHAT_MAX_MESSAGE', '80', '2008-03-25 00:00:00', 's1u3', '2008-03-25 00:00:00', 's1u3' );
insert into SystemSetting values ('SYS_WECHAT_TRIAL_ACCOUNT_ENABLED', 'false', '2008-03-25 00:00:00', 's1u3', '2008-03-25 00:00:00', 's1u3' );
GO

/*
 * mobile端站内信连接修改
 */
update MessageTemplate set mtp_content_mobile_link = 'javascript:clicked(''../approval/approval.html'',true)' where mtp_type = 'ENROLLMENT_NO_SUPERVISOR';
update MessageTemplate set mtp_content_mobile_link = 'javascript:clicked(''../approval/approval.html'',true)' where mtp_type = 'ENROLLMENT_NEXT_APPROVERS';
update MessageTemplate set mtp_content_mobile_link = 'javascript:clicked(''../course/signup.html'',true)' where mtp_type = 'ENROLLMENT_CONFIRMED';
GO

/**
 Auth: leon	
 Date: 2014-12-2
 Desc: 删除评论的时候删除赞，统计，通知，回复
 */
  drop trigger delCommentRel;
  GO
  Create trigger delCommentRel
  On sns_comment
  for Delete
  As
  begin
  Delete sns_comment From sns_comment sc , deleted d
  Where sc.s_cmt_target_id = d.s_cmt_id or sc.s_cmt_reply_to_id = d.s_cmt_id
  Delete sns_doing from sns_doing, deleted d
  where (d.s_cmt_id = s_doi_reply_id or d.s_cmt_id = s_doi_act_id) and s_doi_module = d.s_cmt_module
  Delete sns_doing from sns_doing, deleted d
  where d.s_cmt_id = s_doi_target_id and s_doi_target_type = '1' and s_doi_module = 'Comment'
  delete sns_valuation_log from sns_valuation_log, deleted d
  where d.s_cmt_id = s_vtl_target_id and s_vtl_module = d.s_cmt_module
  delete sns_count from sns_count,deleted d
  where d.s_cmt_id = s_cnt_target_id and s_cnt_module = d.s_cmt_module
  end
  GO
  
  
/*
 * 知识目录表添加特殊目录“未分类”标识
 * TEMP  未分类
 */
alter table kb_catalog add kbc_type nvarchar(255)
GO


---分享知识积分
INSERT INTO creditsType (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,cty_deleted_ind,cty_relation_total_ind,cty_relation_type,cty_default_credits_ind,cty_default_credits,cty_create_usr_id,cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,cty_period,cty_hit)
VALUES('KB_SHARE_KNOWLEDGE','KB_SHARE_KNOWLEDGE',0,0,0,1,'KB',1,2,'s1u3',getdate(),'s1u3',getdate(),'MONTH',2);

update creditsType set cty_tcr_id =(select tcr_id from  tcTrainingCenter where tcr_parent_tcr_id is null)
where cty_tcr_id is null;
GO

/**
 *添加首页欢迎词管理字段 
 */
alter table siteposter add index_text varchar(255);
GO
exec sp_rename 'siteposter.index_text','sp_welcome_word','column';
GO

/**------以下两个SQL语句在r51279版本是用oracle写的放错了位置，所以请不要执行----------**/
/**
 * 添加公告是否回执字段
 * */
ALTER TABLE Message ADD msg_receipt INT DEFAULT (0);
GO

/**
 * 公告回执表
 * */
create table Receipt (
	"REC_ID" int NOT NULL identity(1,1),
	"REC_MSG_ID" int NOT NULL ,
	"REC_ENT_ID" int NOT NULL ,
	"REC_USG_ID" nvarchar(20),
	"RECEIPT_DATE" datetime ,
	constraint PK_RECEIPT primary key (REC_ID)
)
GO



update kb_item_view set kiv_update_datetime=kiv_create_datetime where kiv_update_datetime is null;
GO

/*
Auth : Jimmy
Date : 2015-02-15
Desc : 欢迎词管理字段,添加移动端欢迎词
*/
 alter table siteposter alter column sp_welcome_word nvarchar(255) 
 alter table siteposter add mb_welcome_word nvarchar(255);
GO

/*
Auth : Myron
Date : 2015-02-25
Desc : 系统默认图片初始化
*/
update Siteposter set sp_media_file = 'ban01.jpg',  sp_media_file1 = '', sp_media_file2 = '', sp_media_file3 = '', sp_media_file4 = '', sp_logo_file_cn = 'logo3.png', sp_logo_file_hk = 'logo2.png', sp_logo_file_us = '', login_bg_file1 = 'adv62.jpg',login_bg_file2 = '',login_bg_file3 = '',login_bg_file4 = '',login_bg_file5 = '' where sp_ste_id=1 and sp_mobile_ind = 1;
update Siteposter set sp_media_file = 'b1.png',  sp_media_file1 = '', sp_media_file2 = '', sp_media_file3 = '', sp_media_file4 = '', sp_logo_file_cn = 'wizbang.png', sp_logo_file_hk = 'wizbang.png', sp_logo_file_us = 'wizbang.png', login_bg_file1 = 'banner01.jpg',login_bg_file2 = 'banner02.jpg',login_bg_file3 = 'banner03.jpg',login_bg_file4 = '',login_bg_file5 = '' where sp_ste_id=1 and sp_mobile_ind = 0;
GO


/**
Auth : Myron
Date : 2015-02-25
DESC : 添加权限配置 
 */
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml) values ('FTN_LRN_ANN_MANAGE', 'FUNCTION', null, getDate(), null)
GO
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp) select rol_id, ftn_id, getdate() from acRole, acFunction where rol_ste_uid in ('LRNR') and ftn_ext_id = 'FTN_LRN_ANN_MANAGE'
GO
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml) values ('FTN_LRN_ART_MANAGE', 'FUNCTION', null, getDate(), null)
GO
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp) select rol_id, ftn_id, getdate() from acRole, acFunction where rol_ste_uid in ('LRNR') and ftn_ext_id = 'FTN_LRN_ART_MANAGE'
GO
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml) values ('FTN_TA_ART_VIEW', 'FUNCTION', null, getDate(), null)
GO
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp) select rol_id, ftn_id, getdate() from acRole, acFunction where rol_ste_uid in ('TADM') and ftn_ext_id = 'FTN_TA_ART_VIEW'
GO
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml) values ('FTN_LRN_LEARNING_CATALOG', 'FUNCTION', null, getDate(), null)
GO
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp) select rol_id, ftn_id, getdate() from acRole, acFunction where rol_ste_uid in ('LRNR') and ftn_ext_id = 'FTN_LRN_LEARNING_CATALOG'
GO
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml) values ('FTN_LRN_LEARNING_RECOMMEND', 'FUNCTION', null, getDate(), null)
GO
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp) select rol_id, ftn_id, getdate() from acRole, acFunction where rol_ste_uid in ('LRNR') and ftn_ext_id = 'FTN_LRN_LEARNING_RECOMMEND'
GO
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml) values ('FTN_LRN_LEARNING_SIGNUP', 'FUNCTION', null, getDate(), null)
GO
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp) select rol_id, ftn_id, getdate() from acRole, acFunction where rol_ste_uid in ('LRNR') and ftn_ext_id = 'FTN_LRN_LEARNING_SIGNUP'
GO
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml) values ('FTN_LRN_LEARNING_OPEN', 'FUNCTION', null, getDate(), null)
GO
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp) select rol_id, ftn_id, getdate() from acRole, acFunction where rol_ste_uid in ('LRNR') and ftn_ext_id = 'FTN_LRN_LEARNING_OPEN'
GO
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml) values ('FTN_LRN_LEARNING_MAP', 'FUNCTION', null, getDate(), null)
GO
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp) select rol_id, ftn_id, getdate() from acRole, acFunction where rol_ste_uid in ('LRNR') and ftn_ext_id = 'FTN_LRN_LEARNING_MAP'
GO
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml) values ('FTN_LRN_LEARNING_PROFESSION', 'FUNCTION', null, getDate(), null)
GO
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp) select rol_id, ftn_id, getdate() from acRole, acFunction where rol_ste_uid in ('LRNR') and ftn_ext_id = 'FTN_LRN_LEARNING_PROFESSION'
GO
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml) values ('FTN_LRN_EXAM_RECOMMEND', 'FUNCTION', null, getDate(), null)
GO
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp) select rol_id, ftn_id, getdate() from acRole, acFunction where rol_ste_uid in ('LRNR') and ftn_ext_id = 'FTN_LRN_EXAM_RECOMMEND'
GO
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml) values ('FTN_LRN_EXAM_SIGNUP', 'FUNCTION', null, getDate(), null)
GO
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp) select rol_id, ftn_id, getdate() from acRole, acFunction where rol_ste_uid in ('LRNR') and ftn_ext_id = 'FTN_LRN_EXAM_SIGNUP'
GO
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml) values ('FTN_LRN_EXAM_CATALOG', 'FUNCTION', null, getDate(), null)
GO
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp) select rol_id, ftn_id, getdate() from acRole, acFunction where rol_ste_uid in ('LRNR') and ftn_ext_id = 'FTN_LRN_EXAM_CATALOG'
GO
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml) values ('FTN_LRN_GROUP_MANAGE', 'FUNCTION', null, getDate(), null)
GO
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp) select rol_id, ftn_id, getdate() from acRole, acFunction where rol_ste_uid in ('LRNR') and ftn_ext_id = 'FTN_LRN_GROUP_MANAGE'
GO
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml) values ('FTN_TA_GROUP_MANAGE', 'FUNCTION', null, getDate(), null)
GO
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp) select rol_id, ftn_id, getdate() from acRole, acFunction where rol_ste_uid in ('TADM') and ftn_ext_id = 'FTN_TA_GROUP_MANAGE'
GO
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml) values ('FTN_LRN_KNOW_MANAGE', 'FUNCTION', null, getDate(), null)
GO
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp) select rol_id, ftn_id, getdate() from acRole, acFunction where rol_ste_uid in ('LRNR') and ftn_ext_id = 'FTN_LRN_KNOW_MANAGE'
GO
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml) values ('FTN_TA_KNOW_MANAGE', 'FUNCTION', null, getDate(), null)
GO
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp) select rol_id, ftn_id, getdate() from acRole, acFunction where rol_ste_uid in ('TADM') and ftn_ext_id = 'FTN_TA_KNOW_MANAGE'
GO
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml) values ('FTN_LRN_KB_VIEW', 'FUNCTION', null, getDate(), null)
GO
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp) select rol_id, ftn_id, getdate() from acRole, acFunction where rol_ste_uid in ('LRNR') and ftn_ext_id = 'FTN_LRN_KB_VIEW'
GO
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml) values ('FTN_LRN_DOING_MANAGE', 'FUNCTION', null, getDate(), null)
GO
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp) select rol_id, ftn_id, getdate() from acRole, acFunction where rol_ste_uid in ('LRNR') and ftn_ext_id = 'FTN_LRN_DOING_MANAGE'
GO
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml) values ('FTN_LRN_RANK_COURSE', 'FUNCTION', null, getDate(), null)
GO
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp) select rol_id, ftn_id, getdate() from acRole, acFunction where rol_ste_uid in ('LRNR') and ftn_ext_id = 'FTN_LRN_RANK_COURSE'
GO
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml) values ('FTN_LRN_RANK_LEARNING', 'FUNCTION', null, getDate(), null)
GO
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp) select rol_id, ftn_id, getdate() from acRole, acFunction where rol_ste_uid in ('LRNR') and ftn_ext_id = 'FTN_LRN_RANK_LEARNING'
GO
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml) values ('FTN_LRN_RANK_CREDIT', 'FUNCTION', null, getDate(), null)
GO
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp) select rol_id, ftn_id, getdate() from acRole, acFunction where rol_ste_uid in ('LRNR') and ftn_ext_id = 'FTN_LRN_RANK_CREDIT'
GO


/*
Auth : Leepec
Date : 2015-02-26
Desc : 知识中心附件表增加路径字段
*/
alter table kb_attachment add kba_url nvarchar(255);
GO
update kb_attachment set kba_url = 'attachment/'+cast(kba_id as varchar(50))+'/'+kba_file;
GO

update kb_attachment set kba_url = '/attachment/'+cast(kba_id as varchar(50))+'/'+kba_file;
GO

/**
Auth : Myron
Date : 2015-02-27
DESC : 添加权限配置 
 */
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml) values ('FTN_LRN_HOME_VIEW', 'FUNCTION', null, getDate(), null)
GO
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp) select rol_id, ftn_id, getdate() from acRole, acFunction where rol_ste_uid in ('LRNR') and ftn_ext_id = 'FTN_LRN_HOME_VIEW'
GO

insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml) values ('FTN_LRN_LEARNING_COURSEVIEW', 'FUNCTION', null, getDate(), null)
GO
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp) select rol_id, ftn_id, getdate() from acRole, acFunction where rol_ste_uid in ('LRNR') and ftn_ext_id = 'FTN_LRN_LEARNING_COURSEVIEW'
GO

/**
Auth : Leepec
Date : 2015-03-03
DESC : 添加是否是在线视频标识
 */
alter table kb_item add kbi_online nvarchar(20);

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
alter table LearningSituation add ls_share_count int
GO
alter table LearningSituation add ls_access_count int
GO

/**
Auth : lance
Date : 2015-03-12
DESC : 添加培训管理员课程评论权限
 */
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml) values ('FTN_TA_ITEM_COMMENT', 'FUNCTION', null,  getdate(), null);
GO
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp) select rol_id, ftn_id,  getdate() from acRole, acFunction where rol_ste_uid in ('TADM') and ftn_ext_id = 'FTN_TA_ITEM_COMMENT';
GO

/**
Auth : lance
Date : 2015-03-14
DESC : 添加邮件头部和底部图片
 */
insert into messageparamname (mpn_mtp_id, mpn_name, mpn_name_desc) select mtp_id,'[Header image]','lab_msg_header_image' from messagetemplate;
GO
insert into messageparamname (mpn_mtp_id, mpn_name, mpn_name_desc) select mtp_id,'[Footer image]','lab_msg_footer_image' from messagetemplate;
GO
alter table messagetemplate add mtp_header_img nvarchar(200);
GO
alter table messagetemplate add mtp_footer_img nvarchar(200);
GO
update messageTemplate set mtp_content = '[Header image]' + convert(varchar(5000),mtp_content) + '[Footer image]';
GO
update messageTemplate set mtp_header_img = '/msg/emailHeader.jpg';
GO
update messageTemplate set mtp_footer_img = '/msg/emailFooter.jpg';
GO

/**
 * myron
 * 2015-03-20
 * 添加系统管理员行业资讯详情页面权限
 */
insert into acfunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_xml) values ('FTN_SYS_ART_MANAGE', 'FUNCTION', null, getDate(), null)
GO
insert into acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp) select rol_id, ftn_id, getdate() from acRole, acFunction where rol_ste_uid in ('SADM') and ftn_ext_id = 'FTN_SYS_ART_MANAGE'
GO

/**
Auth : lacne
Date : 2015-03-20
DESC : 将邮件帐号类型恢复为'EMAIL'
 */
update SystemSetting set sys_cfg_value='EMAIL' where sys_cfg_type='SYS_MAIL_SERVER_ACCOUNT_TYPE';
GO

/**
Auth : Myron.liu
Date : 2015-03-23
DESC : 扩大在线图片等等信息字段的长度
 */
alter table MODULETEMPFILE alter column MTF_URL ntext
GO

/**
 * myron.liu
 * 2015-03-26
 * 删除单点登录链接功能
 */
delete from acrolefunction where rfn_FTN_ID = (select FTN_ID from acfunction where ftn_ext_id='SSO_LINK_QUERY');
delete from acfunction where ftn_ext_id='SSO_LINK_QUERY';
delete from achomepage where ac_hom_ftn_ext_id = 'SSO_LINK_QUERY';
GO

/**
 * leon.li
 * 2015-04-1
 * 删除触发器，由程序完成该操作
 */
drop trigger delCommentRel;

/**
 *  6.1 start
*/
/*
 *leon ,菜单整理
  2015-4-29
*/
delete from acRoleFunction
delete from acFunction
alter table acfunction drop column ftn_xml;
alter table acfunction add ftn_assign nvarchar(10) default 1;
alter table acfunction add ftn_tc_related nvarchar(10) default 'N';
alter table acfunction add ftn_status nvarchar(10) default 0;
alter table acfunction add ftn_order int;
alter table acfunction add ftn_parent_id int;
alter table acrolefunction add rfn_ftn_favorite nvarchar(10) default 0;
alter table acrolefunction add rfn_ftn_order int;  -- 标记 内容 成绩 处理报名 这种不在菜单显示的
alter table acrolefunction add rfn_ftn_parent_id int;
update acRole set rol_url_home = 'app/admin/home' where rol_id != 5

/*
 *leon ,首页添加删除常用功能
  2015-05-06
*/
create table userFavoriteFunction(
	uff_usr_ent_id int not null,
	uff_role_ext_id nvarchar(32),
	uff_fun_id int not null,
	uff_create_datetime datetime
);
ALTER TABLE userFavoriteFunction ADD CONSTRAINT
	PK_userFavoriteFunction PRIMARY KEY CLUSTERED
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
alter table aeitemlesson add  ils_date datetime null;
alter table aeitemlesson add  ils_qiandao int  not null default 0;
alter table aeitemlesson add  ils_qiandao_chidao int null;
alter table aeitemlesson add  ils_qiandao_queqin int null;
alter table aeitemlesson add  ils_qiandao_youxiaoqi int null;

alter table aeitemlesson add  ils_qiandao_chidao_time datetime null;
alter table aeitemlesson add  ils_qiandao_queqin_time datetime null;
alter table aeitemlesson add  ils_qiandao_youxiaoqi_time datetime null;

create table aeitemlessonqiandao(
	ilsqd_id int not null identity(1,1),
	ilsqd_usr_ent_id int not null,
	ilsqd_ils_id int not null,
	ilsqd_ils_itm_id int not null,
	ilsqd_app_id int not null,
	ilsqd_status int not null,
	ilsqd_date datetime not null,
	ilsqd_create_timestamp datetime,
	constraint PK_ILS_QD_ID primary key (ilsqd_id)
);
go

/*
* 邀请讲师回答
* @leon.li
*/
alter table knowQuestion add que_ask_ent_ids nvarchar(1000);



/**
 *投票活动创建表sql 
 *@Andrew.xiao
 */
CREATE TABLE [dbo].[voting](
	[vot_id] [int] IDENTITY(1,1) NOT NULL,
	[vot_title] [nvarchar](255) NOT NULL,
	[vot_content] [ntext] NULL,
	[vot_status] [nvarchar](20) NOT NULL,
	[vot_tcr_id] [int] NULL,
	[vot_eff_date_from] [datetime] NULL,
	[vot_eff_date_to] [datetime] NULL,
	[vot_create_timestamp] [datetime] NULL,
	[vot_create_usr_id] [nvarchar](20) NULL,
	[vot_update_timestamp] [datetime] NULL,
	[vot_update_usr_id] [nvarchar](20) NULL
)

GO

CREATE TABLE [dbo].[votequestion](
	[vtq_id] [int] IDENTITY(1,1) NOT NULL,
	[vtq_vot_id] [int] NULL,
	[vtq_title] [nvarchar](255) NULL,
	[vtq_contnet] [ntext] NULL,
	[vtq_type] [nvarchar](20) DEFAULT('MC') NULL,
	[vtq_status] [nvarchar](20) DEFAULT('ON') NULL,
	[vtq_order] [int] DEFAULT(1) NULL,
	[vtq_create_timestamp] [datetime] NOT NULL,
	[vtq_create_usr_id] [nvarchar](20) NULL,
	[vtq_update_timestamp] [datetime] NULL,
	[vtq_update_usr_id] [nvarchar](20) NULL
)

GO

CREATE TABLE [dbo].[voteoption](
	[vto_id] [int] IDENTITY(1,1) NOT NULL,
	[vto_vtq_id] [int] NULL,
	[vto_desc] [nvarchar](500) NULL,
	[vto_order] [int] NULL
)

GO

CREATE TABLE [dbo].[voteresponse](
	[vrp_usr_ent_id] [int] NOT NULL,
	[vrp_vot_id] [int] NOT NULL,
	[vrp_vtq_id] [int] NOT NULL,
	[vrp_vto_id] [int] NULL,
	[vrp_respone_time] [datetime] NULL
)

GO

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
alter table webMessage add wmsg_send_type nvarchar(20) default 'SEND';
alter table webMessage add wmsg_rec_type nvarchar(20) default 'REC';

/**
 * lance
 * 2015-06-19
 * 为sitePoster表增加是否是否显示登录页面header和所有页面footer字段
 */
alter table sitePoster add sp_login_show_header_ind bit;
alter table sitePoster add sp_all_show_footer_ind bit;

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
这句不用执行， 添加菜单的例子
INSERT [dbo].[acFunction] ([ftn_ext_id], [ftn_level], [ftn_type], [ftn_creation_timestamp], [ftn_tc_related], [ftn_status], [ftn_assign], [ftn_order], [ftn_parent_id]) 
VALUES ('COMMON_USERD_MANAGEMENT', '0', NULL, GETDATE(), 'N', '1', '1', 1, NULL)
INSERT [dbo].[acRoleFunction] ([rfn_rol_id], [rfn_ftn_id], [rfn_create_usr_id], [RFN_CREATE_TIMESTAMP], 
[rfn_ftn_parent_id], [rfn_ftn_order]) 
select rol_id, child.ftn_id, 's1u3', getdate(),parent.ftn_id, 1  from acRole, acFunction child, acFunction parent
where rol_ext_id like 'ADM_%' and child.ftn_ext_id = 'FS_LINK_MAIN' and parent.ftn_ext_id = 'COMMON_USERD_MANAGEMENT'

*/
·
/**
 * andrew,xiao
 * 2015-07-10
 * 将系统参数配置中微信部分移动新的tab页中
 */
ALTER TABLE SystemSetting ALTER column sys_cfg_value nvarchar(2000);
insert into SystemSetting(sys_cfg_type,sys_cft_update_usr_id,sys_cfg_create_timestamp,sys_cfg_create_usr_id,sys_cfg_update_timestamp,sys_cfg_value) values('WECHAT_MENU','s1u3',GETDATE(),'s1u3',GETDATE(),
	N'{
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
update entity set ent_ste_uid = ugr_code from userGrade where ugr_ent_id = ent_id and ent_id > 7 and ent_type = 'UGR';
go	  


/**
 *职务表增加职务编号字段(nat.li)
 */
 alter table UserGrade add ugr_code nvarchar2(255);
 
 
  update userGrade set ugr_code = 'P' + cast(ugr_ent_id as varchar(20)) where ugr_ent_id > 7;
  update userGrade set ugr_code = 'Unspecified'  where ugr_display_bil ='Unspecified' and ugr_default_ind = 1  ;
  update entity set ent_ste_uid = ugr_code from userGrade where ugr_ent_id = ent_id and ent_type = 'UGR';
go
 
 
 /*
 *修改学习信息表的学分列类型为Float(nat.li 2015-10-13)
 */
 alter table LearningSituation alter column ls_learn_credit float;


  /*
 *上传日志记录表添加培训中心中字段(nat.li 2015-10-30)
 */
alter table IMSLog add ilg_tcr_id int;
go
update IMSLog set ilg_tcr_id=1;
go


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
GO

/*
 * 知识中心--表增加一个存放附件类型的字段 
 * bill.lai 2015-12-31
 * 添加了列kbi_filetype后，访问 http://ip:port/app/kb/admin/updateOldData 以更新旧数据 
 */
alter table kb_item add kbi_filetype varchar(10);
GO

/*
*修复积分历史数据（BUG4521）
*/
	update userCreditsDetail set ucd_cty_id=(SELECT cty_id FROM creditsType WHERE cty_code = 'ITM_IMPORT_CREDIT' and cty_tcr_id=1)
	where ucd_cty_id in(SELECT cty_id FROM creditsType WHERE cty_code = 'ITM_IMPORT_CREDIT');

/**
 * 新增siteposter两列，存放视频地址以及登陆页面显示类型、
 * 更新以前PC端的登陆页面默认为轮播图类型（PIC）
 */
ALTER TABLE siteposter ADD login_bg_video nvarchar(200);
ALTER TABLE siteposter ADD login_bg_type nvarchar(10);
update SitePoster set login_bg_type = 'PIC' where sp_mobile_ind = 0;
GO

/**
 * 学习足迹新增字段
 */
ALTER TABLE LearningSituation ADD Is_first_course_date datetime;
ALTER TABLE LearningSituation ADD Is_first_praised_usr nvarchar(255);
ALTER TABLE LearningSituation ADD Is_first_praised_date datetime;
ALTER TABLE LearningSituation ADD Is_first_fans_usr nvarchar(255);
ALTER TABLE LearningSituation ADD Is_first_fans_date datetime;
ALTER TABLE LearningSituation ADD Is_first_share_date datetime;
ALTER TABLE LearningSituation ADD Is_first_que_date datetime;
ALTER TABLE LearningSituation ADD Is_first_helper_usr nvarchar(255);
ALTER TABLE LearningSituation ADD Is_signup_date datetime;
GO

/**
 * 移动端banner图片替换
 */
update siteposter set sp_media_file = 'banner-a1.jpg',sp_media_file1 = 'banner-a2.jpg',sp_media_file2 = 'banner-a3.jpg',sp_media_file3 = 'banner-a4.jpg' where sp_tcr_id = 1 and SP_MOBILE_IND = 1;
GO


/**
 * 发展序列新增字段
 */
ALTER TABLE Profession ADD pfs_status int;
ALTER TABLE Profession ADD pfs_tcr_id int;
GO
/*
Author: damon
Desc: new Table UserPositionCatalog
date: 2016-03-24
*/
CREATE TABLE [dbo].[UserPositionCatalog](
	[upc_id] [int] IDENTITY(1,1) NOT NULL,
	[upc_title] [nvarchar](255) COLLATE Chinese_Taiwan_Stroke_CI_AS NULL,
	[upc_desc] [nvarchar](255) COLLATE Chinese_Taiwan_Stroke_CI_AS NULL,
	[upc_status] [nvarchar](255) COLLATE Chinese_Taiwan_Stroke_CI_AS NULL,
	[upc_tcr_id] [int] NULL,
	[upc_create_datetime] [datetime] NULL,
	[upc_create_user_id] [int] NULL,
	[upc_update_datetime] [datetime] NULL,
	[upc_update_user_id] [int] NULL,
 CONSTRAINT [PK_UserPositionCatalog] PRIMARY KEY CLUSTERED 
(
	[upc_id] ASC
)WITH (IGNORE_DUP_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/*
Author: damon
Desc: new Table UserPositionLrnMap
date: 2016-03-24
*/
CREATE TABLE [dbo].[UserPositionLrnMap](
	[upm_id] [int] IDENTITY(1,1) NOT NULL,
	[upm_upt_id] [int] NULL,
	[upm_seq_no] [int] NULL,
    [upm_tcr_id] [int] NULL,
	[upm_img] [nvarchar](200) COLLATE Chinese_Taiwan_Stroke_CI_AS NULL,
	[upm_create_time] [datetime] NULL,
	[upm_create_usr_id] [int] NULL,
	[upm_update_time] [datetime] NULL,
	[upm_update_usr_id] [int] NULL,
	[upm_status] [int] NULL,
 CONSTRAINT [PK_UserPositionLrnMap] PRIMARY KEY CLUSTERED 
(
	[upm_id] ASC
)WITH (IGNORE_DUP_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/*
Author: damon
Desc: new Table UserPositionLrnItem
date: 2016-03-24
*/
CREATE TABLE [dbo].[UserPositionLrnItem](
	[upi_id] [int] IDENTITY(1,1) NOT NULL,
	[upi_upm_id] [int] NULL,
	[upi_itm_id] [int] NULL,
 CONSTRAINT [PK_UserPositionLrnItem] PRIMARY KEY CLUSTERED 
(
	[upi_id] ASC
)WITH (IGNORE_DUP_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO

/**
 * 岗位新增字段
 */
ALTER TABLE UserPosition ADD upt_upc_id int;
GO

/**
 * 课程目标学员明细表新增字段  数据来源职级/岗位学习地图
 */
ALTER TABLE itemTargetLrnDetail ADD itd_fromprofession int;
ALTER TABLE itemTargetLrnDetail ADD itd_fromposition int;
GO

/**
 * 新增列：eip_tcr_id；根据列去做企业登录数统计计算
 * */
ALTER TABLE CurrentActiveUser ADD
cau_eip_tcr_id int NOT NULL
GO

/**
 * 新增列：max_peak_count；企业管理中所允许的最大在线人数
 * */
ALTER TABLE EnterpriseInfoPortal ADD
eip_max_peak_count int
GO

/** damon0421
 * 专题对应专家
 */
CREATE TABLE [dbo].[UserSpecialExpert](
	[use_id] [int] IDENTITY(1,1) NOT NULL,
	[use_ust_id] [int] NOT NULL,
	[use_ent_id] [int] NOT NULL,
 CONSTRAINT [PK_UserSpecialExpert] PRIMARY KEY CLUSTERED 
(
	[use_id] ASC
)WITH (IGNORE_DUP_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/**
 * 专题对应课题
 */
CREATE TABLE [dbo].[UserSpecialItem](
	[usi_id] [int] IDENTITY(1,1) NOT NULL,
	[ust_utc_id] [int] NOT NULL,
	[usi_itm_id] [int] NOT NULL,
 CONSTRAINT [PK_UserSpecialItem] PRIMARY KEY CLUSTERED 
(
	[usi_id] ASC
)WITH (IGNORE_DUP_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/**
 * 专题培训
 */
CREATE TABLE [dbo].[UserSpecialTopic](
	[ust_id] [int] IDENTITY(1,1) NOT NULL,
	[ust_title] [nchar](100) COLLATE Chinese_Taiwan_Stroke_CI_AS NOT NULL,
	[ust_img] [nchar](200) COLLATE Chinese_Taiwan_Stroke_CI_AS NOT NULL,
	[ust_tcr_id] [int] NULL,
	[ust_summary] [nchar](500) COLLATE Chinese_Taiwan_Stroke_CI_AS NULL,
	[ust_content] [ntext] COLLATE Chinese_Taiwan_Stroke_CI_AS NULL,
	[ust_showindex] [int] NOT NULL,
	[ust_hits] [int] NOT NULL,
	[ust_create_time] [datetime] NULL,
	[ust_create_usr_id] [int] NULL,
	[ust_update_time] [datetime] NULL,
	[ust_update_usr_id] [int] NULL,
	[ust_status] [int] NULL,
 CONSTRAINT [PK_UserSpecialTopic] PRIMARY KEY CLUSTERED 
(
	[ust_id] ASC
)WITH (IGNORE_DUP_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/**
 * 后台导航学习地图添加功能项
 * 插入acRoleFunction时需根据前面插入acfuntion里的ftn_id新增数据
 */
declare @ftn_id_parent int
select @ftn_id_parent= ftn_id from acFunction where ftn_ext_id = 'FTN_AMD_STUDY_MAP_MGT'

INSERT [dbo].[acFunction] ([ftn_ext_id], [ftn_level], [ftn_type], [ftn_creation_timestamp], [ftn_tc_related], [ftn_status], [ftn_assign], [ftn_order], [ftn_parent_id]) 
VALUES ('FTN_AMD_SPECIALTOPIC_MAIN', '1', NULL, GETDATE(), 'N', '1', '1', 30, @ftn_id_parent)

INSERT [dbo].[acFunction] ([ftn_ext_id], [ftn_level], [ftn_type], [ftn_creation_timestamp], [ftn_tc_related], [ftn_status], [ftn_assign], [ftn_order], [ftn_parent_id]) 
VALUES ('FTN_AMD_POSITION_MAP_MAIN', '1', NULL, GETDATE(), 'N', '1', '1', 27, @ftn_id_parent)

/*插入acRoleFunction时需根据前面插入acfuntion里的ftn_id新增数据*/
declare @ftn_id int
select @ftn_id = FTN_ID from acFunction where ftn_ext_id='FTN_AMD_POSITION_MAP_MAIN';
INSERT [dbo].[acRoleFunction] ([rfn_rol_id], [rfn_ftn_id], [rfn_create_usr_id], [RFN_CREATE_TIMESTAMP], 
[rfn_ftn_parent_id], [rfn_ftn_order])  VALUES (1, @ftn_id, NULL, GETDATE(), @ftn_id_parent, 9)

select @ftn_id = FTN_ID from acfunction  where ftn_ext_id='FTN_AMD_SPECIALTOPIC_MAIN';
INSERT [dbo].[acRoleFunction] ([rfn_rol_id], [rfn_ftn_id], [rfn_create_usr_id], [RFN_CREATE_TIMESTAMP], 
[rfn_ftn_parent_id], [rfn_ftn_order])  VALUES (1, @ftn_id, NULL, GETDATE(), @ftn_id_parent, 12);


/*
Auth: Bill
Date: 2016-04-29
Desc: 在线问答站内信
*/
insert into messageTemplate(mtp_type, mtp_subject, mtp_content, mtp_remark_label, mtp_web_message_ind, mtp_active_ind, mtp_tcr_id, mtp_update_timestamp, mtp_update_ent_id) 
values ('KNOW_ADD_MESSAGE',N'来自[User name]的站内信',N'来自[User name]的站内信','lab_KNOW_ADD_MESSAGE',0,1,1,getdate(),3);
go
/**
Auth: Bill
Date: 2016-04-29
Desc: 在线问答站内信(初始化邮件模板邮件内容)
*/
update messageTemplate set mtp_content = N'亲爱的 [Learner name]： <br />		学员[User name]提出了问题"[Question title]"需要您的帮助，点击链接  <span style="color:#0000FF;font-family:Verdana, Geneva, Arial, Helvetica, sans-serif;line-height:normal;">[Link to KnowDetail]</span> 可了解问题更多详情...' where mtp_type='KNOW_ADD_MESSAGE';
insert into messageParamName select mtp_id, '[Learner name]', 'lab_msg_learner_name' from messageTemplate where mtp_type='KNOW_ADD_MESSAGE';
insert into messageParamName select mtp_id, '[Question title]', 'lab_msg_question_title' from messageTemplate where mtp_type='KNOW_ADD_MESSAGE';
insert into messageParamName select mtp_id, '[Link to KnowDetail]', 'lab_msg_link_to_know_detail' from messageTemplate where mtp_type='KNOW_ADD_MESSAGE';
insert into messageParamName select mtp_id, '[User name]', 'lab_know_user_name' from messageTemplate where mtp_type='KNOW_ADD_MESSAGE';

update messageTemplate set mtp_content_pc_link = '/app/know/detail/' where mtp_type = 'KNOW_ADD_MESSAGE' and mtp_remark_label = 'lab_KNOW_ADD_MESSAGE';
go

/**
 * Auth: Jacky 
 * Date: 2016-05-07
 * INFO: 自动积分管理 //新增五项（参加公共调查问卷、创建群组、参与群组、被点赞、点赞）限制积分次数
 * 
 */
UPDATE creditsType SET cty_period =  'MONTH'  WHERE cty_code = 'SYS_SUBMIT_SVY'	or cty_code = 'SYS_CREATE_GROUP' or cty_code = 'SYS_JION_GROUP' or cty_code = 'SYS_GET_LIKE' or cty_code = 'SYS_CLICK_LIKE'
go

/**
Auth: Bill
Date: 2016-05-07
Desc: 站内信增加一列后台内容
*/
ALTER TABLE webMessage ADD wmsg_admin_content_pc ntext;
go


/**
 * 职级发展序列新增字段(模版属性)
 */
ALTER TABLE profession ADD pfs_template int;
go
/**
Auth: halo
Date: 2016-05-9
Desc: 职级发展序列对应课程
*/
CREATE TABLE [dbo].[ProfessionLrnItem](
	[psi_id] [int] IDENTITY(1,1) NOT NULL,
	[psi_pfs_id] [int] NULL,
	[psi_ugr_id] [nchar](1000) COLLATE Chinese_Taiwan_Stroke_CI_AS NULL,
	[psi_itm_id] [nchar](1000) COLLATE Chinese_Taiwan_Stroke_CI_AS NULL
) ON [PRIMARY]

/**
 * Auth : Lucky
 * Date: 2016-05-10
 * Desc : 帮助中心功能  帮助问题表
 */
CREATE TABLE [dbo].[helpQuestion](
	[hq_id] [int] IDENTITY(1,1) NOT NULL,
	[hq_type_id] [int] NOT NULL,
	[hq_title] [nvarchar](255) NOT NULL,
	[hq_content] [ntext] NOT NULL,
	[hq_create_timestamp] [datetime] NOT NULL,
	[hq_update_timestamp] [datetime] NULL,
	[hq_top_index] [int] NOT NULL,
	[hq_is_hot] [tinyint] NOT NULL,
	[hq_is_publish] [tinyint] NOT NULL,
	[hq_language] [nvarchar](50) NOT NULL,
 CONSTRAINT [PK_helpQuestion] PRIMARY KEY CLUSTERED 
(
	[hq_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
go

/**
 * Auth : Lucky
 * Date: 2016-05-10
 * Desc : 帮助中心功能  帮助问题分类表
 */
CREATE TABLE [dbo].[helpQuestionType](
	[hqt_id] [int] IDENTITY(1,1) NOT NULL,
	[hqt_type_name] [nvarchar](100) NOT NULL,
	[hqt_pid] [int] NOT NULL,
	[hqt_top_index] [int] NOT NULL,
	[hqt_is_publish] [tinyint] NOT NULL,
	[hqt_create_timestamp] [datetime] NOT NULL,
	[hqt_language] [nvarchar](50) NOT NULL,
 CONSTRAINT [PK_helpQuestionType] PRIMARY KEY CLUSTERED 
(
	[hqt_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
go

/**
Auth: Bill
Date: 2016-05-11
Desc: 在线问答站内信(更新移动端链接地址)
*/
update messageTemplate set mtp_content_mobile_link = '/mobile/views/know/detail.html' where mtp_type = 'KNOW_ADD_MESSAGE' and mtp_remark_label = 'lab_KNOW_ADD_MESSAGE';
go


/**
Auth: leaf
Date: 2016-05-23
Desc: 考试管理，处理报名，报名日志，备注允许为空
 */
alter table [dbo].[aeAppnCommhistory] 
alter column [ach_content]  [nvarchar](2000) NULL
go

/**
 * Desc: 系统数据统计表，实时统计（注：一开始是用线程更新，后来改为实时统计）
 * Author:andrew.xiao
 * Date : 2016-5-25
 */
CREATE TABLE [dbo].[SystemStatistics] (
	[ssc_eip_tcr_id] [int] NOT NULL,
	[ssc_update_time] [datetime] NOT NULL,
	[ssc_web_base_couse_count] [int] DEFAULT (0),
	[ssc_classroom_course_count] [int] DEFAULT (0),
	[ssc_integrated_course_count] [int] DEFAULT (0),
	[ssc_web_base_exam_count] [int] DEFAULT (0),
	[ssc_classroom_exam_count] [int] DEFAULT (0),
	[ssc_open_course_count] [int] DEFAULT (0),
	[ssc_special_topic_count] [int] DEFAULT (0),
	[ssc_admin_know_share_count] [int] DEFAULT (0),
	[ssc_learner_know_share_count] [int] DEFAULT (0),
	[ssc_user_online_count] [int] DEFAULT (0),
	[ssc_user_count] [int] DEFAULT (0),
	[ssc_user_group_count] [int] DEFAULT (0),
	[ssc_mobile_app_user_count] [int] DEFAULT (0),
	[ssc_wechat_user_count] [int] DEFAULT (0)
);
GO

/**
 * Desc: 更新supplier添加培训中心ID
 * Author:lucky.liang
 * Date : 2016-5-31
 */
alter table [dbo].[supplier] add spl_tcr_id int ;
GO

/**
Auth: lucky
Date: 2016-06-15
Desc: InstructorInf表之前没有将iti_tcr_id数据插入，现在将之前为null的都改成1
 */
update InstructorInf set iti_tcr_id =1 where iti_tcr_id is null;
GO


/**
Auth: Bill
Date: 2016-06-23
Desc: 问答添加未分类 类型
*/
set identity_insert knowCatalog ON --允许对自增列Id插入指定数据
insert into knowCatalog(kca_id,kca_tcr_id,kca_title,kca_type,kca_public_ind,kca_que_count,kca_create_usr_id,kca_create_timestamp,kca_update_usr_id,kca_update_timestamp) 
	values(-1,1,N'未分类','CATALOG',1,0,3,GETDATE(),3,GETDATE()); 
set identity_insert knowCatalog OFF --关闭对自增列Id插入指定数据

/**
Auth: lucky
Date: 2016-06-23
Desc: 插入初始化数据到SystemSetting，用于记录禁止上传的文件格式
 */
INSERT INTO SystemSetting 
(sys_cfg_type , sys_cfg_value , sys_cfg_create_timestamp , sys_cfg_create_usr_id ,
sys_cfg_update_timestamp,sys_cft_update_usr_id)
VALUES('SYS_UPLOAD_FORBIDDEN','jsp,asp,exe,bat,bin,php,sys,com,Mach-O,ELF,dll,reg',
'2016-06-20 15:08:00' , 's1u3' , '2016-06-20 15:08:00' , 's1u3');
GO

/**
Auth: andrew.xiao
Date: 2016-07-25
Desc: 课程表添加是否发布到移动端的标志
 */
alter table aeItem add itm_mobile_ind varchar(5);
GO

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
GO

/**
Auth: andrew.xiao
Date: 2016-07-29
Desc: 将公开课“是否发布到移动端”都默认为“yes”
 */
update aeItem set itm_mobile_ind = 'yes' where itm_ref_ind = 1;
GO

/**
Auth: lucky
Date: 2016-08-1
Desc: 插入初始化数据到SystemSetting，用于整合i doc view 预览插件
 */
INSERT INTO SystemSetting 
(sys_cfg_type , sys_cfg_value , sys_cfg_create_timestamp , sys_cfg_create_usr_id ,
sys_cfg_update_timestamp,sys_cft_update_usr_id)
VALUES('SYS_I_DOC_VEIW_HOST','http://127.0.0.1/',
'2016-06-20 15:08:00' , 's1u3' , '2016-06-20 15:08:00' , 's1u3');

INSERT INTO SystemSetting 
(sys_cfg_type , sys_cfg_value , sys_cfg_create_timestamp , sys_cfg_create_usr_id ,
sys_cfg_update_timestamp,sys_cft_update_usr_id)
VALUES('SYS_I_DOC_VIEW_PREVIEW_FILE_HOST','http://localhost:8080/',
'2016-06-20 15:08:00' , 's1u3' , '2016-06-20 15:08:00' , 's1u3');

INSERT INTO SystemSetting 
(sys_cfg_type , sys_cfg_value , sys_cfg_create_timestamp , sys_cfg_create_usr_id ,
sys_cfg_update_timestamp,sys_cft_update_usr_id)
VALUES('SYS_I_DOC_VIEW_TOKEN','cyberwisdomsupport',
'2016-06-20 15:08:00' , 's1u3' , '2016-06-20 15:08:00' , 's1u3');

INSERT INTO SystemSetting 
(sys_cfg_type , sys_cfg_value , sys_cfg_create_timestamp , sys_cfg_create_usr_id ,
sys_cfg_update_timestamp,sys_cft_update_usr_id)
VALUES('SYS_I_DOC_VIEW_PREVIEW_IP','',
'2016-06-20 15:08:00' , 's1u3' , '2016-06-20 15:08:00' , 's1u3');

GO

/**
Auth: andrew.xiao
Date: 2016-09-3
Desc: app推送，客户端记录表
 */
CREATE TABLE [dbo].appClient(
	[mobileInd] [varchar](10) NOT NULL,
	[usrEntId] [int] NULL,
	[clientId] [varchar](100) NULL,
	[appId] [varchar](50) NULL,
	[status] [varchar](10) NULL
);

GO


/**
Auth: Randy
Date: 2016-09-6
Desc: 把专题培训放到“培训管理下”
 */
update acfunction set ftn_parent_id = (select ftn_id from acfunction where ftn_ext_id ='FTN_AMD_TRAINING_MGT'), ftn_order=31 where ftn_ext_id = 'FTN_AMD_SPECIALTOPIC_MAIN' ;
GO

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
update aeItemType set ity_owner_ent_id = 0 where ity_id = 'INTEGRATED'

/**
Auth: Randy
Date: 2016-10-6
Desc: 把积分管理放到“社区管理下”
 */
   
   update acfunction set ftn_parent_id = (select ftn_id from acfunction where ftn_ext_id ='FTN_AMD_SNS_MGT'), ftn_order=31 where ftn_ext_id = 'FTN_AMD_CREDIT_SETTING_MAIN' ;
GO
/**
Auth: bill
Date: 2016-10-13
Desc: 更改課程表圖片名字字段的長度
 */
alter table aeitem alter column itm_icon nvarchar(200);
go


/**
Auth: Randy
Date: 2016-10-6
      把讲师管理从系统管理员下删除。
 */
delete from acrolefunction where rfn_rol_id = (select rol_id from acrole where rol_ext_id = 'ADM_1') 
and rfn_ftn_id in(
select ftn_id from acfunction where ftn_ext_id ='FTN_AMD_BASE_DATA_MGT'
)

/**
Auth: Randy
Date: 2016-10-12
Desc: 视始化角色权限 及邮件。

 */	 

   启动平台后，用admin 帐号登录到平台，用切换到“系统管理员角色”，然后执行以下两个操作。	 
   注意： 如果人原来的数据库中已有自定议的角色，请执行完这里两个请求后，到解色管理中为自定义好的角色重新分配置权限。

   由于各个角色的默认权限有变更，所以需要在备份空数据库时，在空数库下启动平台然后在URL中输入以下URL初始下角色权限：
   http://host:port/app/admin/role/initRolePermission?dirPath=D:\dev\CORE\trunk\test\com\cwn\wizbank\resources\copy\
   注：dirPath： 指向patch中的initData 目录下存放初始权限内容文件目录
   
  初始化邮件内容，在浏览器地址栏输入以下URL：
   http://host:port/app/admin/role/initEmail?dirPath=D:\dev\CORE\trunk\test\com\cwn\wizbank\resources\email\
   注：dirPath： 指向patch中的initData 目录下存放初始邮件内容文件目录


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
('API_UPDATE_CREDITS','API_UPDATE_CREDITS','1','1','0','1','API','0','0','s1u3',getdate(),'s1u3',getDate(),1);   




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
('API_UPDATE_CREDITS','API_UPDATE_CREDITS','1','1','0','1','API','0','0','s1u3',getdate(),'s1u3',getDate(),1);   
   

/**
Auth: LEAF
Date: 2016-10-28
Desc: 提示信息维护功能
*/
DROP TABLE [dbo].[helpQuestionType]

create table helpQuestionType
(
    [hqt_id] [int] IDENTITY(1,1) primary key,
    [hqt_type_name] [nvarchar](100) NOT NULL,
	[hqt_pid] [int] NOT NULL,
	[hqt_top_index] [int] NOT NULL,
	[hqt_is_publish] [int] ,
	[hqt_create_timestamp] [datetime] NOT NULL,
	[hqt_language] [nvarchar](50) NOT NULL
);
go


DROP TABLE [dbo].[helpQuestion]
GO

CREATE TABLE [dbo].[helpQuestion](
	[hq_id] [int] IDENTITY(1,1) primary key,
	[hq_type_id] [int] NOT NULL,
	[hq_title] [nvarchar](255) NOT NULL,
	[hq_content_cn] [ntext] NOT NULL,
	[hq_create_timestamp] [datetime] NOT NULL,
	[hq_update_timestamp] [datetime] NULL,
	[hq_top_index] [int] NOT NULL,
	[hq_width] int NOT NULL,
	[hq_height] int NOT NULL,
	[hq_Template] [nvarchar](50) NOT NULL,
	[hqt_number] [nvarchar](50) NULL,
	[hq_content_us] [ntext] NULL,
);

   
 /** 
 * 在emailMessage表增加emsg_itm_id（课程的id）字段
 * archer
 * 2016-10-11
 */
alter table emailMessage add emsg_itm_id int null;

   

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
 GO

/**
 * Auth: andrew.xiao
 * Date: 2016-11-23
 * Desc:emailMessage 和 webMessage的中间关联表
 */ 
CREATE TABLE [dbo].[emsgRwmsg](
	[emsg_id] [int] NOT NULL,
	[wmsg_id] [int] NOT NULL
);
GO


 
  
/**
Auth: lucky
Date: 2016-11-15
Desc: 重要对像详细操作日志数据表
 */	 
CREATE TABLE objectActionLog (
	object_id int NOT NULL,
	object_code nvarchar(255) NULL,
	object_title nvarchar(255) NULL,
	object_type nvarchar(50) NOT NULL,
	object_action nvarchar(50) NOT NULL,
	object_action_type nvarchar(50) NOT NULL,
	object_action_time datetime NOT NULL,
	object_opt_user_id int NOT NULL
) 
GO

CREATE TABLE gourpLoginReport(
	gplr_grp_id int NOT NULL,   --用户组id
	gplr_year int NOT NULL,     
	gplr_month int NOT NULL,
	gplr_login_mode int NOT NULL,  --登录入口
	gplr_totle_login_number int NOT NULL,  --登录人次
	gplr_last_update_date datetime NOT NULL
) 
GO

CREATE TABLE gradeLoginReport(
	gdlr_ugr_id int NOT NULL,   --职级id
	gdlr_year int NOT NULL,     
	gdlr_month int NOT NULL,
	gdlr_login_mode int NOT NULL,  --登录入口
	gdlr_totle_login_number int NOT NULL,  --登录人次
    gdlr_last_update_date datetime NOT NULL
) 
GO

CREATE TABLE positionLoginReport(
	pslr_upt_id int NOT NULL,   --岗位id
	pslr_year int NOT NULL,     
	pslr_month int NOT NULL,
	pslr_login_mode int NOT NULL,  --登录入口
	pslr_totle_login_number int NOT NULL,  --登录人次
	pslr_last_update_date datetime NOT NULL
)
GO

/**
Auth: andrew
Date: 2016-11-16
Desc: 系统设置添加是否允许多账号登录
 */	 
INSERT INTO SystemSetting 
(sys_cfg_type , sys_cfg_value , sys_cfg_create_timestamp , sys_cfg_create_usr_id ,
sys_cfg_update_timestamp,sys_cft_update_usr_id)
VALUES('MULTIPLE_LOGIN_IND','1',
GETDATE() , 's1u3' , GETDATE() , 's1u3');
GO

  
  


/**Auth: andrew.xiao
 * Date : 2016-11-28
 * Desc: 保存用户密码历史记录
 */
CREATE TABLE [dbo].[UserPasswordHistory](
  [uph_usr_ent_id] [int] NOT NULL, --用户ID
  [uph_pwd] [nvarchar](30) NOT NULL, --用户历史密码（已加密的）
  [uph_update_usr_ent_id] [int] NOT NULL, --修改人id
  [uph_client_type] [nvarchar](10) NOT NULL, --PC或者Mobile
  [uph_create_time] [datetime] NOT NULL --创建时间
);
GO

/**Auth: andrew.xiao
 * Date : 2016-11-28
 * Desc: 系统设置，密码安全策略
 */
INSERT INTO SystemSetting 
(sys_cfg_type , sys_cfg_value , sys_cfg_create_timestamp , sys_cfg_create_usr_id ,
sys_cfg_update_timestamp,sys_cft_update_usr_id)
VALUES('PASSWORD_POLICY_COMPARE_COUNT','',
GETDATE() , 's1u3' , GETDATE() , 's1u3');

INSERT INTO SystemSetting 
(sys_cfg_type , sys_cfg_value , sys_cfg_create_timestamp , sys_cfg_create_usr_id ,
sys_cfg_update_timestamp,sys_cft_update_usr_id)
VALUES('PASSWORD_POLICY_PERIOD','',
GETDATE() , 's1u3' , GETDATE() , 's1u3');

INSERT INTO SystemSetting 
(sys_cfg_type , sys_cfg_value , sys_cfg_create_timestamp , sys_cfg_create_usr_id ,
sys_cfg_update_timestamp,sys_cft_update_usr_id)
VALUES('PASSWORD_POLICY_PERIOD_FORCE','',
GETDATE() , 's1u3' , GETDATE() , 's1u3');
GO


/*
Auth: bill
Date: 2016-10-27（更新于12-13）
Desc:直播管理
*/
--删除直播管理权限（没有执行过直播相关的sql。不用执行）
declare @live_old_ftn_id int
select @live_old_ftn_id  = ftn_id from acFunction where ftn_ext_id='FTN_AMD_LIVE_MAIN';
delete acRoleFunction where rfn_ftn_id = @live_old_ftn_id;
delete acFunction where ftn_ext_id = 'FTN_AMD_LIVE_MAIN';

--增加直播管理权限
declare @max_ftn_order_id int
select @max_ftn_order_id = max(ftn_order) + 1 from acFunction;
insert into acFunction (ftn_ext_id, ftn_level, ftn_creation_timestamp,ftn_assign, ftn_tc_related,ftn_status,ftn_order)
values ('FTN_AMD_LIVE_MAIN', 0, getdate(), 1, 'Y', 1, @max_ftn_order_id);

--默认把直播功能的权限分给“培训管理员”
declare @tadm_rol_id int
select @tadm_rol_id = rol_id from acRole where rol_ext_id = 'TADM_1';
declare @live_ftn_id int
select @live_ftn_id = ftn_id from acFunction where ftn_ext_id='FTN_AMD_LIVE_MAIN';
declare @live_ftn_order int
select @live_ftn_order = ftn_order from acFunction where ftn_ext_id='FTN_AMD_LIVE_MAIN';
insert into acRoleFunction values(@tadm_rol_id, @live_ftn_id,'s1u3',GETDATE(),0, @live_ftn_order,null);

--判断是否存在相关表
if exists(select * from sys.tables where name = 'liveItem')
	drop table liveItem;
if exists(select * from sys.tables where name = 'liveRecords')
	drop table liveRecords;
	
/**
 * Auth: bill
 * Date: 2016-10-27（更新于12-13）
 * 直播
 */
create table liveItem  
(  
	lv_id int identity(1,1) primary key,  --ID
	lv_title nvarchar(50),					--标题
	lv_create_datetime datetime,  --创建时间
	lv_start_datetime datetime,	--开始时间
	lv_end_datetime datetime,	--结束时间
	lv_create_usr_id nvarchar(20), --创建人
	lv_upd_datetime datetime, --更新时间
	lv_upd_usr_id nvarchar(20), --
	lv_url nvarchar(500),
	lv_desc nvarchar(1000),
	lv_pwd nchar(20),	--	密码
	lv_webinar_id int, --活动ID
	lv_record_id int default((1)), --回放ID
	lv_remark nvarchar(500),
	lv_instr_desc nvarchar(500), --讲师简介
	lv_image nvarchar(100),  --图片
	lv_status nvarchar(20), --状态（2-预告 1-直播中 3-已结束）
	lv_real_start_datetime datetime, --真实开播时间
	lv_need_pwd int,--是否需要密码
	lv_tcr_id int,--培训中心ID
	lv_people_num int, --限制人数
	lv_type int, --状态（2-预告 1-直播中 3-已结束）
	lv_had_live int --是否直播过
);
go

/**
 * Auth: bill
 * Date: 2016-10-27（更新于12-13）
 * 直播观看记录
 */
create table liveRecords
(
	lr_usr_id nvarchar(20),	--观看人ID
	lr_create_time datetime,	--记录创建时间
	lr_live_id	int,	--直播ID
	lr_status int--状态
);
go


/**
 * Auth: leaf
 * Date: 2016-12-01（更新于12-13）
 * 系统运作日志
 */
--删除系统运作日志功能（没有执行过系统运作日志相关的sql。不用执行）
declare @rfn_ftn_id int;
select @rfn_ftn_id=ftn_id from acFunction where ftn_ext_id='FTN_AMD_SYS_SETTING_LOG';
delete from  acRoleFunction where rfn_ftn_id = @rfn_ftn_id;
delete from acFunction where ftn_ext_id = 'FTN_AMD_SYS_SETTING_LOG';
go

--新增系统运作日志功能
declare @ftn_id_parent int;
declare @order int;
select @ftn_id_parent= ftn_id from acFunction where ftn_ext_id = 'FTN_AMD_SYSTEM_SETTING_MGT';
select @order= max(ftn_order) +1  from acFunction;
INSERT acFunction (ftn_ext_id, ftn_level, ftn_creation_timestamp,ftn_assign, ftn_tc_related,ftn_status,ftn_order,ftn_parent_id)
VALUES ('FTN_AMD_SYS_SETTING_LOG', 1, getdate(), 0, 'N', 1, @order, @ftn_id_parent);

INSERT acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_usr_id, RFN_CREATE_TIMESTAMP,rfn_ftn_parent_id, rfn_ftn_order) 
select rol_id, child.ftn_id, 's1u3', getdate(),parent.ftn_id, 1  from acRole, acFunction child, acFunction parent
where rol_ext_id like 'ADM_%' and child.ftn_ext_id = 'FTN_AMD_SYS_SETTING_LOG' and parent.ftn_ext_id = 'FTN_AMD_SYSTEM_SETTING_MGT';
go 

/**
 * Auth: leaf
 * Date: 2016-12-13
 * 提示信息内容表 修改hq_type_id,hq_content_cn字段允许为空
 */
alter table helpQuestion alter column hq_type_id int null;
alter table helpQuestion alter column hq_content_cn ntext null;
go

/**
 * Auth: leaf
 * Date: 2016-12-14
 * 用户默认密码  初始：abc123
 */
INSERT INTO SystemSetting 
(sys_cfg_type , sys_cfg_value , sys_cfg_create_timestamp , sys_cfg_create_usr_id ,
sys_cfg_update_timestamp,sys_cft_update_usr_id)
VALUES('SYS_DEFAULT_USER_PASSWORD','abc123',
GETDATE()  , 's1u3' , GETDATE()  , 's1u3');
go


/**
 * Auth: bill
 * Date: 2016-12-15
 * 新增列：eip_live_max_count；直播并发数
 */
alter table EnterpriseInfoPortal add eip_live_max_count int;
go

/**
 * Auth: Randy
 * Date: 2016-12-28
 * 把未分类改新为英文
 */
update knowcatalog set kca_title = 'Unspecified' where kca_id = -1;
GO

/**
 * Auth: lucky
 * Date: 2017-1-19
 * 修改角色管理中课程管理和考试管理下的所有菜单字体颜色改成蓝色.	
 */
update acFunction set ftn_tc_related = 'Y' where  ftn_parent_id = (select distinct(ftn_id)  from acFunction where ftn_ext_id = 'FTN_AMD_EXAM_MGT');
update acFunction set ftn_tc_related = 'Y' where  ftn_parent_id = (select distinct(ftn_id)  from acFunction where ftn_ext_id = 'FTN_AMD_ITM_COS_MAIN');
go



/**
 * Auth: Archer
 * Date: 2017-2-13
 * 关闭Learner activity report (old) link、learning activity report by course link、learning activity report by learner link
 */
update ReportTemplate set rte_owner_ent_id = 0 where rte_type in ('LEARNER','LEARNING_ACTIVITY_BY_COS','LEARNING_ACTIVITY_LRN');
go

/**
 * Auth: lucky
 * Date: 2017-2-15
 * 修改knowQuestion表里的QUE_CONTENT长度为2000
 */
alter table knowQuestion alter column QUE_CONTENT  nvarchar(2000);
go


/**
 * Auth: Archer
 * Date: 2017-2-28
 * 修改link
 */
update messageTemplate set mtp_content_email_link = 'app/user/userLogin/$?course=' where mtp_type in ('ENROLLMENT_CONFIRMED','ENROLLMENT_NEW','ENROLLMENT_NEXT_APPROVERS')
go

update messageTemplate set mtp_content_pc_link = '/app/course/detail/'  where mtp_type in ('ENROLLMENT_CONFIRMED','ENROLLMENT_NEW')
go


/**
 * Auth: Archer
 * Date: 2017-3-1
 * 修改邮件模板内容；
 */
 update messageTemplate set mtp_content = '  [Header image]<p>	Dear&nbsp;<span style="color:blue;">[Learner name]</span>,</p><p align="left">	Thank you for your recent enrollment of the course<span style="color:blue;">&nbsp;</span><span style="color:blue;">[Course name]([Course code])</span>. We have reviewed and approved your enrollment request. You can start your course by logging into the system using the link below:</p><p align="left">	<span style="color:blue;">[Link to login page]</span></p><p align="left">	Regards,<br />Training &amp; Development&nbsp;&nbsp;</p><span></span><span>[Footer image]</span> ' 
 where mtp_type = 'ENROLLMENT_CONFIRMED';
 go

update messageTemplate set mtp_content = ' [Header image]<p>	Dear&nbsp;<span style="color:#337FE5;"><span style="color:#4C33E5;">[Learner name]</span><span style="color:#4C33E5;">,</span></span> </p><p>	Thank you for your interest in applying for the course<span style="color:#4C33E5;">&nbsp;[Course name]&nbsp;</span><span style="color:#4C33E5;">([Course code]).&nbsp;</span>We are reviewing your enrollment request. Once the enrollment is confirmed. You will receive the confirmation by email.&nbsp;</p><p>	Regards,<br />Training &amp; Development</p><span></span> <p>	<span>[Footer image]</span> </p><p>	<br /></p>' 
where mtp_type = 'ENROLLMENT_NEW';
go



/**
 * Auth: bill
 * Date: 2017-03-06
 * 系统管理员，运行参数，上传文件约束，加一个默认不允许文件jspx在数据库中
 */
update  SystemSetting set sys_cfg_value ='jsp,asp,exe,bat,bin,php,sys,com,Mach-O,ELF,dll,reg,jspx,aspx' where sys_cfg_type= 'SYS_UPLOAD_FORBIDDEN';
go




/**
 * Auth: randy
 * Date: 2017-3-09
 * 修改reguser表里的密码长度为255
 */
alter table reguser alter column usr_pwd  nvarchar(255);
go

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
go

 /**
 * Auth: lucky
 * Date: 2017-2-14
 * 修改重要日志添加操作者登陆时间，IP
 */
alter table objectActionLog add object_opt_user_login_time datetime NULL;
alter table objectActionLog add object_opt_user_login_ip nvarchar(100) NULL;
go	  


/**
 * Auth: leaf
 * Date: 2017-2-16
 * 只用于在2017年2月运行的6.5版本。
 * 用户登录日志数据表为每月初时自动创建，相对与6.3版本，6.5新增了登录状态和登录ip。
 * 如果使用的数据库运行了6.3版本，则需要删除对应月份的用户登录日志数据表。
 * 运行6.5时会自动创建新的表。
 */
if exists(select * from sys.tables where name = 'loginLog20172')
	drop table loginLog20172;
go



/**
 * Auth: lucky
 * Date: 2017-3-07
 * cpd功能新增表
 */
if exists(select * from sys.tables where name = 'cpdType') 
	drop table cpdType;
GO

CREATE TABLE cpdType(
	ct_id int IDENTITY(1,1) primary key,
	ct_license_type nvarchar(100) NOT NULL,
	ct_license_alias nvarchar(255) NOT NULL,
	ct_starting_month tinyint NOT NULL,
	ct_display_order int NOT NULL,
	ct_award_hours_type int NOT NULL,
	ct_cal_before_ind tinyint NOT NULL,
	ct_trigger_email_type tinyint NOT NULL,
	ct_trigger_email_month_1 tinyint NULL,
	ct_trigger_email_date_1 tinyint NULL,
	ct_trigger_email_month_2 tinyint NULL,
	ct_trigger_email_date_2 tinyint NULL,
	ct_trigger_email_month_3 tinyint NULL,
	ct_trigger_email_date_3 tinyint NULL,
	ct_recover_hours_period int NULL,
	cg_last_email_send_time datetime NOT NULL,
	ct_create_usr_ent_id int NOT NULL,
	ct_create_datetime datetime NOT NULL,
	ct_update_usr_ent_id int NULL,
	ct_update_datetime datetime NULL,
	ct_status nvarchar(50) NOT NULL
);
GO

ALTER TABLE cpdType ADD UNIQUE (ct_license_type);
GO

ALTER TABLE cpdType ADD  CONSTRAINT DF_cpdType_ct_starting_month  DEFAULT ((1)) FOR ct_starting_month
GO

ALTER TABLE cpdType ADD  CONSTRAINT DF_cpdType_ct_award_hours_type  DEFAULT ((1)) FOR ct_award_hours_type
GO

ALTER TABLE cpdType ADD  CONSTRAINT DF_cpdType_ct_cal_before_ind  DEFAULT ((1)) FOR ct_cal_before_ind
GO

if exists(select * from sys.tables where name = 'cpdGroup') 
	drop table cpdGroup;
GO

CREATE TABLE cpdGroup(
	cg_id int IDENTITY(1,1) primary key,
	cg_code nvarchar(100) NOT NULL,
	cg_alias nvarchar(255) NOT NULL,
	cg_display_order int NULL,
	cg_contain_non_core_ind tinyint NOT NULL,
	cg_display_in_report_ind tinyint NOT NULL,
	cg_ct_id int NOT NULL,
	cg_create_usr_ent_id int NOT NULL,
	cg_create_datetime datetime NOT NULL,
	cg_update_usr_ent_id int NULL,
	cg_update_datetime datetime NULL,
	cg_status nvarchar(50) NOT NULL
);
GO

ALTER TABLE cpdGroup ADD UNIQUE (cg_code);
GO

ALTER TABLE cpdGroup ADD  CONSTRAINT DF_cpdGroup_cg_contain_non_core_ind  DEFAULT ((1)) FOR cg_contain_non_core_ind
GO

ALTER TABLE cpdGroup ADD  CONSTRAINT DF_cpdGroup_cg_display_in_report_ind  DEFAULT ((1)) FOR cg_display_in_report_ind
GO

if exists(select * from sys.tables where name = 'cpdGroupPeriod') 
	drop table cpdGroupPeriod;
GO

CREATE TABLE cpdGroupPeriod(
	cgp_id int IDENTITY(1,1) primary key,
	cgp_effective_time datetime NOT NULL,
	cgp_ct_id int NOT NULL,
	cgp_cg_id int NOT NULL,
	cgp_create_usr_ent_id int NOT NULL,
	cgp_create_datetime datetime NOT NULL,
	cgp_update_usr_ent_id int NULL,
	cgp_update_datetime datetime NULL,
	cgp_status varchar(100) NOT NULL
 );
GO

if exists(select * from sys.tables where name = 'cpdGroupHours') 
	drop table cpdGroupHours;
GO

CREATE TABLE cpdGroupHours(
	cgh_id int IDENTITY(1,1) primary key,
	cgh_cgp_id int NOT NULL,
	cgh_declare_month tinyint NOT NULL,
	cgh_core_hours float NOT NULL,
	cgh_non_core_hours float NULL,
	cgh_create_usr_ent_id int NOT NULL,
	cgh_create_datetime datetime NOT NULL,
	cgh_update_usr_ent_id int NULL,
	cgh_update_datetime datetime NULL,
	cgh_status nvarchar(50) NOT NULL
);
GO

if exists(select * from sys.tables where name = 'cpdRegistration') 
	drop table cpdRegistration;
GO

CREATE TABLE cpdRegistration(
	cr_id int IDENTITY(1,1) primary key,
	cr_usr_ent_id int NOT NULL,
	cr_ct_id int NOT NULL,
	cr_reg_datetime datetime NOT NULL,
	cr_de_reg_datetime datetime NULL,
	cr_create_usr_ent_id int NOT NULL,
	cr_create_datetime datetime NOT NULL,
	cr_update_usr_ent_id int NULL,
	cr_update_datetime datetime NULL,
	cr_status nvarchar(100) NOT NULL
);
GO

if exists(select * from sys.tables where name = 'cpdGroupRegistration') 
	drop table cpdGroupRegistration;
GO


CREATE TABLE cpdGroupRegistration(
	cgr_id int IDENTITY(1,1) primary key,
	cgr_usr_ent_id int NOT NULL,
	cgr_cr_id int NOT NULL,
	cgr_initial_date datetime NOT NULL,
	cgr_expiry_date datetime NOT NULL,
	cgr_first_ind tinyint NOT NULL,
	cgr_actual_date datetime NOT NULL,
	cgr_create_usr_ent_id int NOT NULL,
	cgr_create_datetime datetime NOT NULL,
	cgr_update_usr_ent_id int NULL,
	cgr_update_datetime datetime NULL,
	cgr_status varchar(100) NOT NULL
);
GO

if exists(select * from sys.tables where name = 'cpdGroupRegHours') 
	drop table cpdGroupRegHours;
GO

CREATE TABLE cpdGroupRegHours(
	cgrh_id int IDENTITY(1,1) primary key,
	cgrh_usr_ent_id int NOT NULL,
	cgrh_cgr_id int NOT NULL,
	cgrh_cr_id int NOT NULL,
	cgrh_cgr_period int NOT NULL,
	cgrh_cal_start_date datetime NOT NULL,
	cgrh_cal_end_date datetime NOT NULL,
	cgrh_manul_core_hours float NULL,
	cgrh_manul_non_core_hours float NULL,
	cgrh_manul_ind int NOT NULL,
	cgrh_req_core_hours float NOT NULL,
	cgrh_req_non_core_hours float NOT NULL,
	cgrh_execute_core_hours float NOT NULL,
	cgrh_execute_non_core_hours float NOT NULL,
	cgrh_create_usr_ent_id int NOT NULL,
	cgrh_create_datetime datetime NOT NULL,
	cgrh_update_usr_ent_id int NULL,
	cgrh_update_datetime datetime NULL,
	cgrh_status nvarchar(100) NOT NULL
);
GO

if exists(select * from sys.tables where name = 'aeItemCPDItem') 
	drop table aeItemCPDItem;
GO

CREATE TABLE aeItemCPDItem(
	aci_id int IDENTITY(1,1) primary key,
	aci_itm_id int NOT NULL,
	aci_ct_id int NOT NULL,
	aci_accreditation_code nvarchar(255) NULL,
	aci_hours_end_date datetime NOT NULL,
	aci_create_usr_ent_id int NOT NULL,
	aci_create_datetime datetime NOT NULL,
	aci_update_usr_ent_id int NULL,
	aci_update_datetime datetime NULL
);
GO

if exists(select * from sys.tables where name = 'aeItemCPDGourpItem') 
	drop table aeItemCPDGourpItem;
GO

CREATE TABLE aeItemCPDGourpItem(
	acgi_id int IDENTITY(1,1) primary key,
	acgi_cg_id int NOT NULL,
	acgi_aci_id int NOT NULL,
	acgi_itm_id int NOT NULL,
	acgi_award_core_hours float NOT NULL,
	acgi_award_non_core_hours float NOT NULL,
	acgi_create_usr_ent_id int NOT NULL,
	acgi_create_datetime datetime NOT NULL,
	acgi_update_usr_ent_id int NULL,
	acgi_update_datetime datetime NULL
);
GO

if exists(select * from sys.tables where name = 'cpdLrnAwardRecord') 
	drop table cpdLrnAwardRecord;
GO

CREATE TABLE cpdLrnAwardRecord(
	clar_id int IDENTITY(1,1) primary key,
	clar_usr_ent_id int NOT NULL,
	clar_itm_id int NOT NULL,
	clar_app_id int NOT NULL,
	clar_manul_ind tinyint NOT NULL,
	clar_cgr_id int NOT NULL,
	clar_cr_id int NOT NULL,
	clar_ct_id int NOT NULL,
	clar_cg_id int NOT NULL,
	clar_aci_id int NOT NULL,
	clar_award_core_hours float NOT NULL,
	clar_award_non_core_hours float NULL,
	clar_award_datetime datetime NOT NULL,
	clar_create_usr_ent_id int NOT NULL,
	clar_create_datetime datetime NOT NULL,
	clar_update_usr_ent_id int NULL,
	clar_update_datetime datetime NULL
);
GO

if exists(select * from sys.tables where name = 'cpdGroupRegHoursHistory') 
	drop table cpdGroupRegHoursHistory;
GO

CREATE TABLE cpdGroupRegHoursHistory(
	cghi_id int IDENTITY(1,1) primary key,
	cghi_usr_ent_id int NOT NULL,
	cghi_ct_id int NOT NULL,
	cghi_cg_id int NOT NULL,
	cghi_license_type nvarchar(100) NULL,
	cghi_license_alias nvarchar(200) NULL,
	cghi_cal_before_ind tinyint NULL,
	cghi_recover_hours_period tinyint NULL,
	cghi_code nvarchar(100) NULL,
	cghi_alias nvarchar(200) NULL,
	cghi_initial_date datetime NULL,
	cghi_expiry_date datetime NULL,
	cghi_cr_reg_date datetime NULL,
	cghi_cr_de_reg_date datetime NULL,
	cghi_period int NULL,
	cghi_first_ind tinyint NULL,
	cghi_actual_date datetime NULL,
	cghi_ct_starting_month int NULL,
	cghi_cgp_effective_time datetime NULL,
	cghi_cal_start_date datetime NULL,
	cghi_cal_end_date datetime NULL,
	cghi_manul_core_hours float NULL,
	cghi_manul_non_core_hours float NULL,
	cghi_manul_ind tinyint NULL,
	cghi_req_core_hours float NULL,
	cghi_req_non_core_hours float NULL,
	cghi_execute_core_hours float NULL,
	cghi_execute_non_core_hours float NULL,
	cghi_award_core_hours float NULL,
	cghi_award_non_core_hours float NULL,
	clar_aci_accreditation_code nvarchar(200) NULL,
	cghi_create_datetime datetime NOT NULL,
	cghi_update_datetime datetime NULL
);
GO

if exists(select * from sys.tables where name = 'cpdGroupRegCourseHistory') 
	drop table cpdGroupRegCourseHistory;
GO

CREATE TABLE cpdGroupRegCourseHistory(
	crch_id int IDENTITY(1,1) primary key,
	crch_cghi_id int NOT NULL,
	clar_aci_accreditation_code nvarchar(100) NULL,
	cgch_itm_id int NOT NULL,
	cgch_itm_title nvarchar(255) NULL,
	cgch_itm_code nvarchar(255) NULL,
	cgch_period int NULL,
	cgch_first_ind tinyint NULL,
	cgch_app_id int NOT NULL,
	cgch_cgr_id int NOT NULL,
	cgch_cr_id int NOT NULL,
	cgch_ct_id int NOT NULL,
	cgch_cg_id int NOT NULL,
	cgch_license_type nvarchar(100) NULL,
	cgch_license_alias nvarchar(200) NULL,
	cgch_code nvarchar(100) NULL,
	cgch_alias nvarchar(200) NULL,
	cgch_award_core_hours float NULL,
	cgch_award_non_core_hours float NULL,
	cgch_award_datetime datetime NULL,
	cgch_create_datetime datetime NULL,
	cgch_update_datetime datetime NULL
);
GO

/**
 * Auth: leaf
 * Date: 2017-03-07
 * 新增CPT/D 管理 功能
 */

--新增一级功能  CPT/D 管理  
declare @order int;
select @order = max(ftn_order) from acFunction where ftn_level = 0;
INSERT [dbo].[acFunction] ([ftn_ext_id], [ftn_level], [ftn_type], [ftn_creation_timestamp], [ftn_tc_related], [ftn_status], [ftn_assign], [ftn_order], [ftn_parent_id]) 
VALUES ('FTN_AMD_CPT_D_MGT', '0', NULL, GETDATE(), 'N', '1', '1', @order+1, null);
GO

--新增4个二级功能
declare @ftn_id_parent int
declare @ftn_order int

select @ftn_order = max(ftn_order) from acFunction where ftn_level = 1
select @ftn_id_parent= ftn_id from acFunction where ftn_ext_id = 'FTN_AMD_CPT_D_MGT'
--CPT/D 牌照管理
INSERT [dbo].[acFunction] ([ftn_ext_id], [ftn_level], [ftn_type], [ftn_creation_timestamp], [ftn_tc_related], [ftn_status], [ftn_assign], [ftn_order], [ftn_parent_id]) 
VALUES ('FTN_AMD_CPT_D_LIST', '1', NULL, GETDATE(), 'N', '1', '1', @ftn_order+1, @ftn_id_parent);
--CPT/D 牌照注册管理
INSERT [dbo].[acFunction] ([ftn_ext_id], [ftn_level], [ftn_type], [ftn_creation_timestamp], [ftn_tc_related], [ftn_status], [ftn_assign], [ftn_order], [ftn_parent_id]) 
VALUES ('FTN_AMD_CPT_D_LICENSE_LIST', '1', NULL, GETDATE(), 'N', '1', '1', @ftn_order+2, @ftn_id_parent);
--历史保存记录
INSERT [dbo].[acFunction] ([ftn_ext_id], [ftn_level], [ftn_type], [ftn_creation_timestamp], [ftn_tc_related], [ftn_status], [ftn_assign], [ftn_order], [ftn_parent_id]) 
VALUES ('FTN_AMD_CPT_D_HISTORY', '1', NULL, GETDATE(), 'N', '1', '1', @ftn_order+3, @ftn_id_parent);
--CPT/D报表备注维护
INSERT [dbo].[acFunction] ([ftn_ext_id], [ftn_level], [ftn_type], [ftn_creation_timestamp], [ftn_tc_related], [ftn_status], [ftn_assign], [ftn_order], [ftn_parent_id]) 
VALUES ('FTN_AMD_CPT_D_NOTE', '1', NULL, GETDATE(), 'N', '1', '1', @ftn_order+4, @ftn_id_parent);
GO

--CPT/D 管理功能，及其四个下级子功能默认分配给“系统管理员”角色
INSERT acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_usr_id, RFN_CREATE_TIMESTAMP,rfn_ftn_parent_id, rfn_ftn_order) 
select rol_id, child.ftn_id, 's1u3', getdate(),null, 1  from acRole, acFunction child, acFunction parent
where rol_ext_id like 'ADM_%' and child.ftn_ext_id = 'FTN_AMD_CPT_D_MGT' and parent.ftn_ext_id = 'FTN_AMD_CPT_D_MGT';

 
INSERT acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_usr_id, RFN_CREATE_TIMESTAMP,rfn_ftn_parent_id, rfn_ftn_order) 
select rol_id, child.ftn_id, 's1u3', getdate(),parent.ftn_id, 1  from acRole, acFunction child, acFunction parent
where rol_ext_id like 'ADM_%' and child.ftn_ext_id = 'FTN_AMD_CPT_D_LIST' and parent.ftn_ext_id = 'FTN_AMD_CPT_D_MGT';

INSERT acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_usr_id, RFN_CREATE_TIMESTAMP,rfn_ftn_parent_id, rfn_ftn_order) 
select rol_id, child.ftn_id, 's1u3', getdate(),parent.ftn_id, 1  from acRole, acFunction child, acFunction parent
where rol_ext_id like 'ADM_%' and child.ftn_ext_id = 'FTN_AMD_CPT_D_LICENSE_LIST' and parent.ftn_ext_id = 'FTN_AMD_CPT_D_MGT';

INSERT acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_usr_id, RFN_CREATE_TIMESTAMP,rfn_ftn_parent_id, rfn_ftn_order) 
select rol_id, child.ftn_id, 's1u3', getdate(),parent.ftn_id, 1  from acRole, acFunction child, acFunction parent
where rol_ext_id like 'ADM_%' and child.ftn_ext_id = 'FTN_AMD_CPT_D_HISTORY' and parent.ftn_ext_id = 'FTN_AMD_CPT_D_MGT';

INSERT acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_usr_id, RFN_CREATE_TIMESTAMP,rfn_ftn_parent_id, rfn_ftn_order) 
select rol_id, child.ftn_id, 's1u3', getdate(),parent.ftn_id, 1  from acRole, acFunction child, acFunction parent
where rol_ext_id like 'ADM_%' and child.ftn_ext_id = 'FTN_AMD_CPT_D_NOTE' and parent.ftn_ext_id = 'FTN_AMD_CPT_D_MGT';
GO


/**
 * Auth: bill
 * Date: 2017-02-13
 * 新增列：eip_live_mode；直播模式
 */
alter table EnterpriseInfoPortal add eip_live_mode nvarchar(255);
go

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
alter table liveItem add lv_mode_type nvarchar(255);
alter table liveItem add lv_upstream_address nvarchar(255);
alter table liveItem add lv_hls_downstream_address nvarchar(255);
alter table liveItem add lv_rtmp_downstream_address nvarchar(255);
alter table liveItem add lv_flv_downstream_address nvarchar(255);
alter table liveItem add lv_channel_id nvarchar(255);
alter table liveItem drop column lv_remark;
alter table liveItem drop column lv_instr_desc;
go


/**
 * Auth: bill
 * Date: 2017-02-28
 * 新增列：eip_live_qcloud_secretid；腾讯云直播id
 * 新增列：eip_live_qcloud_secretkey；腾讯云直播key
 */
alter table EnterpriseInfoPortal add eip_live_qcloud_secretid nvarchar(255);
alter table EnterpriseInfoPortal add eip_live_qcloud_secretkey nvarchar(255);
go


/**
 * Auth: lucky
 * Date: 2017-03-08
 * 报表备注表  报表备注历史表
 */
if exists(select * from sys.tables where name = 'cpdReportRemark') 
	drop table cpdReportRemark;
GO

CREATE TABLE cpdReportRemark(
	crpm_id int IDENTITY(1,1) primary key,
	crpm_report_code nvarchar(255) NOT NULL,
	crpm_report_desc nvarchar(255) NULL,
	crpm_report_remark nvarchar(2000) NOT NULL,
	crpm_create_datetime datetime NOT NULL,
	crpm_create_usr_ent_id int NOT NULL,
	crpm_update_datetime datetime NULL,
	crpm_update_usr_ent_id int NULL
);
GO

ALTER TABLE cpdReportRemark ADD UNIQUE (crpm_report_code);
GO

if exists(select * from sys.tables where name = 'cpdReportRemarkHistory') 
	drop table cpdReportRemarkHistory;
GO

CREATE TABLE cpdReportRemarkHistory(
	crmh_id int IDENTITY(1,1) primary key,
	crmh_crpm_id int NOT NULL,
	crmh_report_code nvarchar(255) NOT NULL,
	crmh_report_desc nvarchar(255) NULL,
	crmh_report_remark nvarchar(2000) NOT NULL,
	crpm_create_datetime datetime NOT NULL,
	crpm_last_update_datetime datetime NULL,
	crpm_his_create_datetime datetime NULL
);
GO

/**
 * Auth: lucky
 * Date: 2017-03-08
 * cpdGroupRegistration添加小牌id
 * cpdGroupRegHours cpdGroupRegHoursHistory 添加计算需要时数月份
 */
alter table cpdGroupRegistration add cgr_cg_id int not Null;
GO
alter table cpdGroupRegHours add cgrh_cal_month int not Null;
GO
alter table cpdGroupRegHoursHistory add cghi_cal_month int not Null;
GO

/**
 * Auth: lucky
 * Date: 2017-03-09
 * 修改 cpdType.cg_last_email_send_time为ct_last_email_send_time
 * cpdGroupRegHours cpdGroupRegHoursHistory 添加计算需要时数月份
 */
alter table cpdType drop column cg_last_email_send_time   ;
GO

alter table  cpdType add ct_last_email_send_time datetime  null;
GO

alter table cpdGroupRegistration alter column cgr_expiry_date datetime  null;
GO

alter table cpdGroupRegistration alter column cgr_status nvarchar(100)  null;
GO

/**
 * Auth: lucky
 * Date: 2017-03-10
 * 添加大牌报名记录注册号码字段
 */
alter table cpdRegistration add cr_reg_number nvarchar(255) not Null;
GO

/**
 * Auth: andrew
 * Date: 2017-03-13
 * 统计用户在线人数，添加登录标识
 */
alter table CurrentActiveUser add cau_login_type varchar(255);
GO

/**
 * Auth: lucky
 * Date: 2017-03-14
 * 删除cpdGroupRegHours的cgrh_status字段
 */
alter table cpdGroupRegHours drop column cgrh_status;
GO

/**
 * Auth: lucky
 * Date: 2017-03-14
 * 删除cpdLrnAwardRecord的clar_aci_id字段为clar_acgi_id
 */
alter table cpdLrnAwardRecord drop column clar_aci_id;
GO
alter table cpdLrnAwardRecord add clar_acgi_id int not Null;
GO

/**
 * Auth: lucky
 * Date: 2017-03-15
 * 删除cpdLrnAwardRecord的clar_cgr_id和clar_cr_id 字段
 */
alter table cpdLrnAwardRecord drop column clar_cgr_id;
GO
alter table cpdLrnAwardRecord drop column clar_cr_id;
GO

/**
 * Auth: lucky
 * Date: 2017-03-16
 * 删除aeItemCPDItem的aci_ct_id字段
 */
alter table aeItemCPDItem drop column aci_ct_id;
GO

/**
 * Auth: lucky
 * Date: 2017-03-17
 * cpdGroupRegHours中字段可以为空
 */
alter table cpdGroupRegHours alter column cgrh_execute_core_hours float  null;
alter table cpdGroupRegHours alter column cgrh_execute_non_core_hours float  null;
GO


/**
 * Auth: lucky
 * Date: 2017-03-18
 * 
 */
alter table cpdGroupRegHoursHistory add cghi_cr_reg_number nvarchar(255)  Null;
GO
alter table cpdGroupRegHoursHistory add cghi_cgp_id int  Null;
GO
alter table cpdGroupRegHoursHistory add cghi_cg_contain_non_core_ind int  Null;
GO

alter table cpdGroupRegHours add cgrh_cgp_id int  Null;
GO

if exists(select * from sys.tables where name = 'cpdGroupRegCourseHistory') 
	drop table cpdGroupRegCourseHistory;
GO

CREATE TABLE cpdGroupRegCourseHistory(
	crch_id int IDENTITY(1,1) primary key,
	crch_cghi_id int NOT NULL,
	crch_aci_id int NULL,
	crch_aci_hours_end_date datetime NULL,
	crch_aci_accreditation_code nvarchar(100) NULL,
	crch_itm_id int NOT NULL,
	crch_itm_title nvarchar(255) NULL,
	crch_itm_code nvarchar(255) NULL,
	crch_period int NULL,
	crch_first_ind tinyint NULL,
	crch_app_id int NOT NULL,
	crch_cgr_id int NOT NULL,
	crch_cr_id int NOT NULL,
	crch_ct_id int NOT NULL,
	crch_ct_license_type nvarchar(100) NULL,
	crch_ct_license_alias nvarchar(200) NULL,
	crch_cg_id int NOT NULL,
	crch_cg_code nvarchar(100) NULL,
	crch_cg_alias nvarchar(200) NULL,
	crch_award_core_hours float NULL,
	crch_award_non_core_hours float NULL,
	crch_award_datetime datetime NULL,
	crch_create_datetime datetime NULL,
	crch_update_datetime datetime NULL
);
GO


/**
 * Auth: Archer
 * Date: 2017-03-21
 * 修改cpdGroupRegistration的cgr_expiry_date字段
 */
alter table cpdGroupRegistration alter column cgr_expiry_date datetime  null;
GO

/**
 * Auth: lucky
 * Date: 2017-03-30
 * 修改cpdReportRemark的crpm_report_desc字段
 * 修改cpdReportRemarkHistory的crpm_create_datetime字段
 */
alter table cpdReportRemark drop column crpm_report_desc;
GO

alter table cpdReportRemarkHistory drop column crmh_report_desc;
GO

alter table cpdReportRemarkHistory drop column crpm_create_datetime;
GO

alter table cpdReportRemarkHistory add crpm_his_period int not Null;;
GO

alter table cpdReportRemarkHistory drop column crpm_last_update_datetime;
GO

alter table cpdReportRemarkHistory add crpm_his_save_month int not Null;
GO

alter table cpdGroupRegHoursHistory add cghi_cgr_id int not Null;
GO


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
alter table liveItem add lv_student_token nvarchar(255);
alter table liveItem add lv_teacher_token nvarchar(255);
alter table liveItem add lv_student_client_token nvarchar(255);
alter table liveItem add lv_teacher_join_url nvarchar(255);
alter table liveItem add lv_student_join_url nvarchar(255);
alter table liveItem add lv_gensee_online_user int;
alter table liveItem add lv_real_end_datetime datetime;
alter table liveItem add lv_gensee_record_url nvarchar(255);
go






/**
 * Auth: Archer
 * Date: 2017-04-17
 *增加新邮件CPT/D Outstanding Hours Email Alert (Learner)邮件内容及参数；
 */
insert into messageTemplate(mtp_type, mtp_subject, mtp_content, mtp_remark_label, mtp_web_message_ind, mtp_active_ind, mtp_tcr_id, 
mtp_update_timestamp, mtp_update_ent_id) values ('CPTD_OUTSTANDING_LEARNER','CPT/D Outstanding Hours – [License Alias]',
'<span> </span>
<p style="text-indent:0cm;">
	<span style="color:#0070C0;"><span>[Header image]</span></span>
</p>
<span> </span>
<p style="text-indent:0cm;">
	<span><span>&nbsp;</span></span>
</p>
<span> </span>
<p style="text-indent:0cm;">
	<span><span><span>Dear <span style="color:#0070C0;">[Learner name]</span></span><span>,</span></span></span>
</p>
<span> </span>
<p style="text-indent:0cm;">
	<span><span>&nbsp;</span></span>
</p>
<span> </span>
<p style="background:white;">
	<span style="color:#222222;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;;font-size:11pt;">Please
kindly note that you have not yet fulfilled the<span>&nbsp;</span><span>CPT</span>/<span>D</span><span>&nbsp;</span>requirement for
the license of </span><span style="color:#0070C0;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;;font-size:11pt;">[License Alias]</span><span style="color:#222222;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;;font-size:11pt;">.
Attached is your Individual<span>&nbsp;</span><span>CPT</span>/<span>D</span><span>&nbsp;</span><span>Hours</span><span>&nbsp;</span>Report for
your review.</span>
</p>
<span> </span>
<p style="background:white;">
	<span style="color:#222222;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;;font-size:11pt;">&nbsp;</span>
</p>
<span> </span>
<p style="background:white;">
	<span style="color:#222222;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;;font-size:11pt;">Please
contact us if you have any queries regarding this email.</span>
</p>
<span> </span>
<p style="background:white;">
	<span style="color:#222222;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;;font-size:11pt;">&nbsp;</span>
</p>
<span> </span>
<p style="text-indent:0cm;">
	<span><span>Regards,</span></span>
</p>
<span> </span>
<p style="text-indent:0cm;">
	<span><span>Training &amp; Development</span></span>
</p>
<span> </span>
<p style="text-indent:0cm;">
	<span><span>&nbsp;</span></span>
</p>
<span> </span><span style="color:#0070C0;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;;font-size:10pt;">[Footer image]</span>',
'lab_CPTD_OUTSTANDING_LEARNER',1,1,1,getdate(),3);
GO



insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) select mtp_id,'[Learner name]','lab_msg_learner_name' from messageTemplate where mtp_type='CPTD_OUTSTANDING_LEARNER';
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) select mtp_id,'[Header image]','lab_msg_header_image' from messageTemplate where mtp_type='CPTD_OUTSTANDING_LEARNER';
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) select mtp_id,'[Footer image]','lab_msg_footer_image' from messageTemplate where mtp_type='CPTD_OUTSTANDING_LEARNER';
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) select mtp_id,'[License Alias]','lab_license_alias' from messageTemplate where mtp_type='CPTD_OUTSTANDING_LEARNER';
GO



/**
 * Auth: Archer
 * Date: 2017-04-18
 *增加新邮件CPT/D Outstanding Hours Email Alert (Group Supervisor)邮件内容及参数；
 */
insert into messageTemplate(mtp_type, mtp_subject, mtp_content, mtp_remark_label, mtp_web_message_ind, mtp_active_ind, mtp_tcr_id, 
mtp_update_timestamp, mtp_update_ent_id) values ('CPTD_OUTSTANDING_SUPERVISOR','CPT/D Outstanding Hours – [License Alias]',
'<span> </span>
<p style="text-indent:0cm;">
	<span style="color:#0070C0;"><span>[Header image]</span></span>
</p>
<span> </span>
<p style="text-indent:0cm;">
	<span><span>&nbsp;</span></span>
</p>
<span> </span>
<p style="text-indent:0cm;">
	<span><span><span>Dear <span style="color:#0070C0;">[Learner name]</span></span><span>,</span></span></span>
</p>
<span> </span>
<p style="text-indent:0cm;">
	<span><span>&nbsp;</span></span>
</p>
<span> </span>
<p style="background:white;">
	<span style="color:#222222;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;;font-size:11pt;">Please
kindly note that your subordinates have not yet fulfilled the CPT/D requirement
for the license of </span><span style="color:#0070C0;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;;font-size:11pt;">[License Alias]</span><span style="color:#222222;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;;font-size:11pt;">.
Attached is the CPT/D Outstanding Hours Report for your reference.</span>
</p>
<span> </span>
<p style="background:white;">
	<span style="color:#222222;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;;font-size:11pt;">&nbsp;</span>
</p>
<span> </span>
<p style="background:white;">
	<span style="color:#222222;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;;font-size:11pt;">Please
kindly review with your subordinates and arrange follow-up actions.</span>
</p>
<span> </span>
<p style="background:white;">
	<span style="color:#222222;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;;font-size:11pt;">&nbsp;</span>
</p>
<span> </span>
<p style="text-indent:0cm;">
	<span><span>Regards,</span></span>
</p>
<span> </span>
<p style="text-indent:0cm;">
	<span><span>Training &amp; Development</span></span>
</p>
<span> </span>
<p style="text-indent:0cm;">
	<span><span>&nbsp;</span></span>
</p>
<span> </span><span style="color:#0070C0;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;;font-size:10pt;">[Footer image]</span>',
'lab_CPTD_OUTSTANDING_SUPERVISOR',1,1,1,getdate(),3);
GO


insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) select mtp_id,'[Learner name]','lab_msg_learner_name' from messageTemplate where mtp_type='CPTD_OUTSTANDING_SUPERVISOR';
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) select mtp_id,'[Header image]','lab_msg_header_image' from messageTemplate where mtp_type='CPTD_OUTSTANDING_SUPERVISOR';
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) select mtp_id,'[Footer image]','lab_msg_footer_image' from messageTemplate where mtp_type='CPTD_OUTSTANDING_SUPERVISOR';
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) select mtp_id,'[License Alias]','lab_license_alias' from messageTemplate where mtp_type='CPTD_OUTSTANDING_SUPERVISOR';
GO




  /**
Auth: leaf
Date: 2017-05-9
Desc: 屏蔽CPT/D功能
 */
update acFunction set ftn_status = '0' where ftn_ext_id = 'FTN_AMD_CPT_D_MGT';
go
  /**
Auth: leaf
Date: 2017-05-9
Desc: 开启CPT/D功能
 */
update acFunction set ftn_status = '1' where ftn_ext_id = 'FTN_AMD_CPT_D_MGT';
go

  /**
Auth: leaf
Date: 2017-05-9
Desc: 修改aeItemCPDItem的aci_hours_end_date字段
 */
alter table aeItemCPDItem alter column aci_hours_end_date datetime null;
go



/**
Auth: leaf
Date: 2017-05-12
Desc: 仅用于低版本（6.5以下）升级
      6.5版本对用户登录日志进行了扩展，故要删除旧版本所有用户登录日志表
 */

declare @tablelog  varchar(100);
declare @year int;
declare @month  int;
declare @name  varchar(100);
declare @i int;
set @i=0;
set @tablelog='loginLog';

while @i<12
begin
    set @year=YEAR(GETDATE());
    set @month=MONTH(GETDATE());
    if((@month-@i)>0)
		begin
		 set @month = @month - @i;
		end
     else
		begin
		 set @month = @month - @i + 12;
		 set @year=@year - 1;
		end
	set @name=@tablelog+convert(varchar,@year)+convert(varchar,@month);
	
	Declare @SQLText varchar(1000);
	If Exists(Select Top 1 Name From Sysobjects Where Name=@name And XType='U') 
	Begin
	  Set @SQLText='Drop Table ' + @name;
	  Exec(@SQLText);
   end
 set @i=@i+1;
End

GO

/**
Auth: leaf
Date: 2017-06-01
Desc: 去除cpdType 中 ct_license_type的唯一约束。
 */
declare @name nvarchar(100);
declare @SQLText nvarchar(200);
--UQ__cpdType 为需要删除的约束名称前缀
select @name=b.name from syscolumns a,sysobjects b 
 where a.id=object_id('cpdType') and a.name='ct_license_type' and b.name like 'UQ__cpdType%';
--删除约束
--alter table cpdType drop CONSTRAINT @name;
Set @SQLText='alter table cpdType drop CONSTRAINT ' + @name;
Exec(@SQLText);

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
Auth: Archer
Date: 2017-05-6-6
Desc: 增加一个cpt/d二级功能并默认分配给系统管理员角色
 */
declare @ftn_id_parent int
declare @ftn_order int

select @ftn_order = max(ftn_order) from acFunction where ftn_level = 1
select @ftn_id_parent= ftn_id from acFunction where ftn_ext_id = 'FTN_AMD_CPT_D_MGT'
--导入用户获得CPT/D时数
INSERT [dbo].[acFunction] ([ftn_ext_id], [ftn_level], [ftn_type], [ftn_creation_timestamp], [ftn_tc_related], [ftn_status], [ftn_assign], [ftn_order], [ftn_parent_id]) 
VALUES ('FTN_AMD_CPT_D_IMPORT_AWARDED_HOURS', '1', NULL, GETDATE(), 'N', '1', '1', @ftn_order+1, @ftn_id_parent);
GO


INSERT acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_usr_id, RFN_CREATE_TIMESTAMP,rfn_ftn_parent_id, rfn_ftn_order) 
select rol_id, child.ftn_id, 's1u3', getdate(),parent.ftn_id, 1  from acRole, acFunction child, acFunction parent
where rol_ext_id like 'ADM_%' and child.ftn_ext_id = 'FTN_AMD_CPT_D_IMPORT_AWARDED_HOURS' and parent.ftn_ext_id = 'FTN_AMD_CPT_D_IMPORT_AWARDED_HOURS';
GO

/**
Auth: Archer
Date: 2017-05-6-6
Desc: 增加一个cpt/d导入用户获得CPT/D时数成功邮件
 */

insert into messageTemplate(mtp_type, mtp_subject, mtp_content, mtp_remark_label, mtp_web_message_ind, mtp_active_ind, mtp_tcr_id, mtp_update_timestamp, mtp_update_ent_id) values ('CPD_AWARDED_HOURS_IMPORT_SUCCESS','Import user awarded CPT/D hours Success Notification', 'Import user awarded CPT/D hours Success Notification','label_CPD_AWARDED_HOURS_IMPORT_SUCCESS',1,1,1,getdate(),3);

update messageTemplate set mtp_content = '<p>   <span style="color:black;font-family:宋体;font-size:11pt;">Dear </span><span style="color:blue;font-family:宋体;font-size:11pt;">[User name]</span><span style="color:black;font-family:宋体;font-size:11pt;">,<br />  <br />  This email is to notify you that the import of user awarded CPT/D hours file </span><span style="color:blue;font-family:宋体;font-size:11pt;">[Src file]</span><span style="color:black;font-family:宋体;font-size:11pt;">, which you uploaded at </span><span style="color:blue;font-family:宋体;font-size:11pt;">[Import start date]</span><span style="color:black;font-family:宋体;font-size:11pt;">, has been completed.<br />  <br />  Success Entries : </span><span style="color:blue;font-family:宋体;font-size:11pt;">[Success total]</span><span style="color:black;font-family:宋体;font-size:11pt;"><br />  Unsuccessful Entries : </span><span style="color:blue;font-family:宋体;font-size:11pt;">[Unsuccess total]<br />  </span><span style="color:black;font-family:宋体;font-size:11pt;"><br />  The import process was completed at </span><span style="color:blue;font-family:宋体;font-size:11pt;">[Import end date]. </span><span style="font-family:宋体;font-size:11pt;">You  may login to the system to view the log file(s) in the import history. <br />  Thank you for your attention.<br />  <br />  Regards,<br />  Training &amp; Development</span>  </p>' where mtp_type='CPD_AWARDED_HOURS_IMPORT_SUCCESS';

insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) select mtp_id,'[User name]','lab_msg_user_name' from messageTemplate where mtp_type='CPD_AWARDED_HOURS_IMPORT_SUCCESS';
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) select mtp_id,'[Src file]','lab_msg_src_file' from messageTemplate where mtp_type='CPD_AWARDED_HOURS_IMPORT_SUCCESS';
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) select mtp_id,'[Import start date]','lab_msg_import_start_date' from messageTemplate where mtp_type='CPD_AWARDED_HOURS_IMPORT_SUCCESS';
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) select mtp_id,'[Success total]','lab_msg_success_total' from messageTemplate where mtp_type='CPD_AWARDED_HOURS_IMPORT_SUCCESS';
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) select mtp_id,'[Unsuccess total]','lab_msg_unscuccess_total' from messageTemplate where mtp_type='CPD_AWARDED_HOURS_IMPORT_SUCCESS';
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) select mtp_id,'[Import end date]','lab_msgimport_end_date' from messageTemplate where mtp_type='CPD_AWARDED_HOURS_IMPORT_SUCCESS';
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
GO



/**
Auth: Archer
Date: 2017-06-15
Desc: 导入注册记录：修改邮件字体（邮件的部分字体显示蓝色需改为黑色）; 导入用户获得CPT/D时数增加头部和尾部图片
 */
update messageTemplate set mtp_content = '<span style="color:#0000FF;line-height:28px;font-family:&quot;Microsoft YaHei UI&quot;, &quot;Microsoft YaHei&quot;, DengXian, SimSun, &quot;Segoe UI&quot;, Tahoma, Helvetica, sans-serif;font-size:14px;background-color:#FFFFFF;">[Header image]</span>  <p>   <span style="color:black;font-family:宋体;font-size:11pt;">Dear <span style="color:#0000FF;font-family:"Microsoft YaHei UI", "Microsoft YaHei", DengXian, SimSun, "Segoe UI", Tahoma, Helvetica, sans-serif;font-size:14px;line-height:28px;background-color:#FFFFFF;">[User name]</span></span><span style="color:blue;font-family:宋体;font-size:11pt;"></span><span style="color:black;font-family:宋体;font-size:11pt;">,<br />  <br />  This email is to notify you that the import of user profile    file </span><span style="color:blue;font-family:宋体;font-size:11pt;"><span style="color:#0000FF;font-family:"Microsoft YaHei UI", "Microsoft YaHei", DengXian, SimSun, "Segoe UI", Tahoma, Helvetica, sans-serif;font-size:14px;line-height:28px;background-color:#FFFFFF;">[Src file]</span></span><span style="color:black;font-family:宋体;font-size:11pt;">,    which you uploaded at </span><span style="color:blue;font-family:宋体;font-size:11pt;"><span style="color:#0000FF;font-family:"Microsoft YaHei UI", "Microsoft YaHei", DengXian, SimSun, "Segoe UI", Tahoma, Helvetica, sans-serif;font-size:14px;line-height:28px;background-color:#FFFFFF;">[Import start date]</span></span><span style="color:black;font-family:宋体;font-size:11pt;">, has    been completed.<br />  <br />  Success Entries : </span><span style="color:blue;font-family:宋体;font-size:11pt;"><span style="color:#0000FF;font-family:"Microsoft YaHei UI", "Microsoft YaHei", DengXian, SimSun, "Segoe UI", Tahoma, Helvetica, sans-serif;font-size:14px;line-height:28px;background-color:#FFFFFF;">[Success total]</span></span><span style="color:black;font-family:宋体;font-size:11pt;"><br />  Unsuccessful Entries : </span><span style="color:blue;font-family:宋体;font-size:11pt;"><span style="color:#0000FF;font-family:"Microsoft YaHei UI", "Microsoft YaHei", DengXian, SimSun, "Segoe UI", Tahoma, Helvetica, sans-serif;font-size:14px;line-height:28px;background-color:#FFFFFF;">[Unsuccess total]</span><br />  </span><span style="color:black;font-family:宋体;font-size:11pt;"><br />  The import process was completed at </span><span style="color:blue;font-family:宋体;font-size:11pt;"><span style="color:#0000FF;font-family:"Microsoft YaHei UI", "Microsoft YaHei", DengXian, SimSun, "Segoe UI", Tahoma, Helvetica, sans-serif;font-size:14px;line-height:28px;background-color:#FFFFFF;"><span style="color:#0000FF;font-family:"Microsoft YaHei UI", "Microsoft YaHei", DengXian, SimSun, "Segoe UI", Tahoma, Helvetica, sans-serif;font-size:14px;line-height:28px;background-color:#FFFFFF;">[Import end date]</span></span>. </span><span style="color:black;font-family:宋体;font-size:11pt;">You    may login to the system to view the log file(s) in the import history.  <br />  Regards,<br />  Training &amp; Development </span>   </p>    <span style="color:#0000FF;font-family:"Microsoft YaHei UI", "Microsoft YaHei", DengXian, SimSun, "Segoe UI", Tahoma, Helvetica, sans-serif;font-size:14px;line-height:28px;background-color:#FFFFFF;">[Footer image]</span>'
where mtp_type = 'CPD_REGISTRATION_IMPORT_SUCCESS';

update messageTemplate set mtp_content = '<span style="color:#0000FF;line-height:28px;font-family:&quot;Microsoft YaHei UI&quot;, &quot;Microsoft YaHei&quot;, DengXian, SimSun, &quot;Segoe UI&quot;, Tahoma, Helvetica, sans-serif;font-size:14px;background-color:#FFFFFF;">[Header image]</span>  <p>   <span style="color:black;font-family:宋体;font-size:11pt;">Dear </span><span style="color:blue;font-family:宋体;font-size:11pt;">[User name]</span><span style="color:black;font-family:宋体;font-size:11pt;">,<br />  <br />  This email is to notify you that the import of user awarded CPT/D hours file </span><span style="color:blue;font-family:宋体;font-size:11pt;">[Src file]</span><span style="color:black;font-family:宋体;font-size:11pt;">, which you uploaded at </span><span style="color:blue;font-family:宋体;font-size:11pt;">[Import start date]</span><span style="color:black;font-family:宋体;font-size:11pt;">, has been completed.<br />  <br />  Success Entries : </span><span style="color:blue;font-family:宋体;font-size:11pt;">[Success total]</span><span style="color:black;font-family:宋体;font-size:11pt;"><br />  Unsuccessful Entries : </span><span style="color:blue;font-family:宋体;font-size:11pt;">[Unsuccess total]<br />  </span><span style="color:black;font-family:宋体;font-size:11pt;"><br />  The import process was completed at </span><span style="color:blue;font-family:宋体;font-size:11pt;">[Import end date]. </span><span style="color:black;font-family:宋体;font-size:11pt;">You  may login to the system to view the log file(s) in the import history. <br />  Thank you for your attention.<br />  <br />  Regards,<br />  Training &amp; Development</span>  </p> <span style="color:#0000FF;font-family:"Microsoft YaHei UI", "Microsoft YaHei", DengXian, SimSun, "Segoe UI", Tahoma, Helvetica, sans-serif;font-size:14px;line-height:28px;background-color:#FFFFFF;">[Footer image]</span>' where mtp_type='CPD_AWARDED_HOURS_IMPORT_SUCCESS';
GO

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
GO



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
GO




/**
Auth: leaf
Date: 2017-06-23
Desc: 去除cpdGroup 中 cg_code的唯一约束。
 */
declare @name nvarchar(100);
declare @SQLText nvarchar(200);

select @name=b.name from syscolumns a,sysobjects b 
 where a.id=object_id('cpdGroup') and a.name='cg_code' and b.name like 'UQ__cpdGroup%';

Set @SQLText='alter table cpdGroup drop CONSTRAINT ' + @name;
Exec(@SQLText);

GO

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

GO       


/**
Auth: steven
Date: 2017-07-07
Desc: 添加测试题，解析online video中api返回的资源link
 */
ALTER TABLE Resources ADD res_src_online_link nvarchar(255)
GO   

/**
Auth: Archer
Date: 2017-07-27
Desc: 给"CPT/D Outstanding Hours – [License Alias]"邮件添加头部和尾部的默认图片
 */
update messageTemplate set mtp_header_img = '/msg/emailHeader.jpg' where mtp_type = 'CPTD_OUTSTANDING_LEARNER';
GO
update messageTemplate set mtp_footer_img = '/msg/emailFooter.jpg' where mtp_type = 'CPTD_OUTSTANDING_LEARNER';
GO

update messageTemplate set mtp_header_img = '/msg/emailHeader.jpg' where mtp_type = 'CPTD_OUTSTANDING_SUPERVISOR';
GO
update messageTemplate set mtp_footer_img = '/msg/emailFooter.jpg' where mtp_type = 'CPTD_OUTSTANDING_SUPERVISOR';
GO

/**
Auth: steven
Date: 2017-07-28
Desc: 更新忘记密码邮件模板
 */
UPDATE messageTemplate SET mtp_content = ' [Header image] Dear Sir/Madam: <br /> <br /> We have received your reset password request on [Request time].<br/><br/>Please click below link to reset your password:<br/><br/>[Link to reset password]<br/><br/>The above link will be expired after [Max days] day(s) so please proceed to reset your password as soon as possible.<br/><br/>Regards,<br />Training &amp; Development</p><span></span> <p> <span>[Footer image]</span> </p><p> <br /></p>'
	WHERE mtp_type = 'USER_PWD_RESET_NOTIFY'
GO 

/**
 * Auth: Jacky
 * Date: 2017-08-11
 * Desc: 更新邮件模板subject为英文 
 */

UPDATE messageTemplate SET mtp_subject = 'Course Enrollment Request Received'
WHERE mtp_id = 1
go
UPDATE messageTemplate SET mtp_subject = 'Course Enrollment Confirmed'
WHERE mtp_id = 2
go
UPDATE messageTemplate SET mtp_subject = 'Course Enrollment Waiting List'
WHERE mtp_id = 3
go
UPDATE messageTemplate SET mtp_subject = 'Course Enrollment Request Declined'
WHERE mtp_id = 4
go
UPDATE messageTemplate SET mtp_subject = 'Course Enrollment Request Cancelled'
WHERE mtp_id = 5
go
UPDATE messageTemplate SET mtp_subject = 'Course Enrollment Request Cancelled'
WHERE mtp_id = 6
go
UPDATE messageTemplate SET mtp_subject = 'Course Enrollment Approval'
WHERE mtp_id = 7
go
UPDATE messageTemplate SET mtp_subject = 'Course Enrollment Notification'
WHERE mtp_id = 8
go
UPDATE messageTemplate SET mtp_subject = 'Course Enrollment Notification'
WHERE mtp_id = 9
go
UPDATE messageTemplate SET mtp_subject = 'Course Enrollment Notification'
WHERE mtp_id = 10
go
UPDATE messageTemplate SET mtp_subject = 'Course Enrollment Notification'
WHERE mtp_id = 11
go
UPDATE messageTemplate SET mtp_subject = 'Course Enrollment Approval'
WHERE mtp_id = 12
go
UPDATE messageTemplate SET mtp_subject = 'Course Completion Reminder'
WHERE mtp_id = 13
go
UPDATE messageTemplate SET mtp_subject = 'Joining Instruction'
WHERE mtp_id = 14
go
UPDATE messageTemplate SET mtp_subject = 'Reminder'
WHERE mtp_id = 15
go
UPDATE messageTemplate SET mtp_subject = 'USR_REG_APPROVE'
WHERE mtp_id = 16
go
UPDATE messageTemplate SET mtp_subject = 'USR_REG_DISAPPROVE'
WHERE mtp_id = 17
go
UPDATE messageTemplate SET mtp_subject = 'User Import Success Notification'
WHERE mtp_id = 18
go
UPDATE messageTemplate SET mtp_subject = 'Enrollment Import Success Notification'
WHERE mtp_id = 19
go
UPDATE messageTemplate SET mtp_subject = 'System Performance Notify'
WHERE mtp_id = 20
go
UPDATE messageTemplate SET mtp_subject = 'empty subject'
WHERE mtp_id = 21
go
UPDATE messageTemplate SET mtp_subject = 'Reset Password Notify'
WHERE mtp_id = 22
go
UPDATE messageTemplate SET mtp_subject = '来自[User name]的站内信'
WHERE mtp_id = 23
go
UPDATE messageTemplate SET mtp_subject = 'CPT/D Outstanding Hours – [License Alias]'
WHERE mtp_id = 24
go
UPDATE messageTemplate SET mtp_subject = 'CPT/D Outstanding Hours – [License Alias]'
WHERE mtp_id = 25
go

/*
 出于安全的验证的考虑，要使LMS接收第三方发过来请求，必须在预先在LMS中设定好第三方系统的域名（如果有多个，则用[|]分开），设置方法如下：
 例如： update acsite set ste_trusted = 1 , ste_domain = 'https://7-elevenuat.cyberwisdom.com.hk' where ste_ent_id = 1;
*/
update acsite set ste_trusted = 1 , ste_domain = 'https://host:port' where ste_ent_id = 1;

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
Desc: 修改职级中编号类型为nvarchar避免输入中文乱码
 */
alter table UserGrade alter column ugr_code nvarchar(255)
go


/**
Auth: Archer 
Date: 2017-10-12
Desc: 清除Learning activity report 的旧模板数据，将LEARNER类型的报表改为可使用状态
 */
delete ReportSpec where rsp_rte_id in(select rte_id from ReportTemplate where rte_type = 'LEARNER');
GO
update ReportTemplate set rte_owner_ent_id = 1 where rte_type = 'LEARNER';
GO

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
GO

	


/*
Date: 2017-11-9
Desc: 培训积分清空
*/
 INSERT INTO creditsType 
 (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,
 cty_deleted_ind,cty_relation_total_ind,cty_relation_type,
 cty_default_credits_ind,cty_default_credits,cty_create_usr_id,
 cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,CTY_TCR_ID)
VALUES('ITM_INTEGRAL_EMPTY','ITM_INTEGRAL_EMPTY',1,1,0,1,'COS',0,0,'s1u3',getdate(),'s1u3',getdate(),1);

/**
(补充此SQL到DB_CHANGE)
Auth: Jacky
Date: 2017-11-17
Desc: 屏蔽学习地图功能
 */
update acFunction set ftn_status = '0' where ftn_ext_id = 'FTN_AMD_STUDY_MAP_MGT';
go 

/**
(补充此SQL到DB_CHANGE)
Auth: Jacky
Date: 2017-11-17
Desc: 屏蔽 live management
 */
update acFunction set ftn_status = '0' where ftn_ext_id = 'FTN_AMD_LIVE_MAIN';
go

/**
Auth: Jacky
Date: 2017-11-17
Desc: 屏蔽 vendor management
 */
update acFunction set ftn_status = '0' where ftn_ext_id = 'FTN_AMD_SUPPLIER_MAIN';
go

/**
Auth: Albert
Date: 2017-11-17
Desc: 屏蔽 点赞，被点赞和评论的的得分项
 */
update creditsType set cty_deleted_ind = 0 where cty_id=34;
go
update creditsType set cty_deleted_ind = 0 where cty_id=35;
go
update creditsType set cty_deleted_ind = 0 where cty_id=36;
go

/**
Auth: steven 
Date: 2017-11-21
Desc: mobile端登录页面logo重置为默认图片
*/
update SitePoster set sp_logo_file_cn = 'logo3.png' where sp_ste_id = 1 and SP_MOBILE_IND = 1
go

/**
Auth: Jasper
Date: 2017-12-18
Desc: 修改aeItemExtension中的ies_credit字段类型
 */
alter table aeItemExtension alter column ies_credit decimal(8,2);
go


/**
Auth: tim 
Date: 2018-1-12
Desc: 修改新用户注册"全名"字段长度为255.
 */
alter table dbo.RegUser alter column usr_display_bil nvarchar(255);

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
VALUES('INTEGRAL_EMPTY','INTEGRAL_EMPTY',1,1,0,1,'',0,0,'s1u3',getdate(),'s1u3',getdate(),1);
GO

/*
Auth: Hajar
Date: 2018-02-09
Desc: 修改 题目分数数据结构
 */
alter table Question alter column que_score int;
alter table RawQuestion alter column raq_score int;

/**
Auth: Steven
Date: 2018-3-13
Desc: 表emailMessage新增抄送电邮地址字段
 */
alter table emailMessage add emsg_cc_email nvarchar(2000) NULL;
GO

/**
Auth: Jaren
Date: 2018-04-02
Desc: 更新导入报名记录已完成模板
 */
update messageTemplate set mtp_content = '[Header image] <p>  <span> </span><span   style="color: black; font-family: 宋体; font-size: 11pt;">Dear </span><span   style="color: blue; font-family: 宋体; font-size: 11pt;">[User name]</span><span style="color: black; font-family: 宋体; font-size: 11pt;">,<br />   <br /> This email is to notify you that the import of enrollment record  </span><span style="color: blue; font-family: 宋体; font-size: 11pt;">[Src file]</span><span style="color: black; font-family: 宋体; font-size: 11pt;">,   which you uploaded at </span><span   style="color: blue; font-family: 宋体; font-size: 11pt;">[Import start date]</span><span   style="color: black; font-family: 宋体; font-size: 11pt;">, has   been completed.<br /> <br /> Success Entries :  </span><span style="color: blue; font-family: 宋体; font-size: 11pt;">[Success total]</span><span style="color: black; font-family: 宋体; font-size: 11pt;"><br />   Unsuccessful Entries : </span><span   style="color: blue; font-family: 宋体; font-size: 11pt;">[Unsuccess total]<br />  </span><span style="color: black; font-family: 宋体; font-size: 11pt;"><br />   The import process was completed at </span><span   style="color: blue; font-family: 宋体; font-size: 11pt;">[Import end date]. </span><span style="font-family: 宋体; font-size: 11pt;">You   may login to the system to view the log file(s) in the import history.   <br /> Thank you for your attention.<br /> <br /> Regards,<br />   Training &amp; Development  </span> </p> <span> </span>  [Footer image] '
where mtp_type = 'ENROLLMENT_IMPORT_SUCCESS' and mtp_tcr_id = 1;
GO


/**
Auth: Jaren
Date: 2018-04-02
Desc: 面授課程繁體 label 問題20180424
 */
update aeTemplateView set
tvw_xml=N'<template_view>	<section id="1">		<title>			<desc lan="ISO-8859-1" name="General information"/>			<desc lan="Big5" name="基本資訊"/>			<desc lan="GB2312" name="基本信息"/>		</title>		<itm_type type="itm_type" paramname="itm_type">			<title>				<desc lan="ISO-8859-1" name="Type"/>				<desc lan="Big5" name="類型"/>				<desc lan="GB2312" name="类型"/>			</title>		</itm_type>		<field51>			<title>				<desc lan="ISO-8859-1" name="Code"/>				<desc lan="Big5" name="編號"/>				<desc lan="GB2312" name="编号"/>			</title>		</field51>		<field52>			<title>				<desc lan="ISO-8859-1" name="Title"/>				<desc lan="Big5" name="名稱"/>				<desc lan="GB2312" name="名称"/>			</title>		</field52>		<training_center type="tcr_pickup" paramname="itm_tcr_id" required="yes" external_field="yes">			<title>				<desc lan="ISO-8859-1" name="Training center"/>				<desc lan="Big5" name="培訓中心"/>				<desc lan="GB2312" name="培训中心"/>			</title>		</training_center>		<field53>			<title>				<desc lan="ISO-8859-1" name="Class period"/>				<desc lan="Big5" name="面授期間"/>				<desc lan="GB2312" name="面授期间"/>			</title>			<subfield_list>				<subfield id="1">					<title>						<desc lan="ISO-8859-1" name="From"/>						<desc lan="Big5" name="由"/>						<desc lan="GB2312" name="由"/>					</title>				</subfield>				<subfield id="2">					<title>						<desc lan="ISO-8859-1" name="To"/>						<desc lan="Big5" name="至"/>						<desc lan="GB2312" name="至"/>					</title>				</subfield>			</subfield_list>		</field53>			<!--		<field61 blend_ind="true">			<title>				<desc lan="ISO-8859-1" name="Online content period"/>				<desc lan="Big5" name="網上內容期限"/>				<desc lan="GB2312" name="网上内容期限"/>			</title>			<subfield_list>				<subfield id="1">					<title>						<desc lan="ISO-8859-1" name="From"/>						<desc lan="Big5" name="由"/>						<desc lan="GB2312" name="由"/>					</title>				</subfield>				<subfield id="2">					<title>						<desc lan="ISO-8859-1" name="To"/>						<desc lan="Big5" name="至"/>						<desc lan="GB2312" name="至"/>					</title>				</subfield>			</subfield_list>		</field61>		-->		<field55>			<title>				<desc lan="ISO-8859-1" name="Enrollment period"/>				<desc lan="Big5" name="報名期限"/>				<desc lan="GB2312" name="报名期限"/>			</title>			<subfield_list>				<subfield id="1">					<title>						<desc lan="ISO-8859-1" name="From"/>						<desc lan="Big5" name="由"/>						<desc lan="GB2312" name="由"/>					</title>				</subfield>				<subfield id="2">					<title>						<desc lan="ISO-8859-1" name="To"/>						<desc lan="Big5" name="至"/>						<desc lan="GB2312" name="至"/>					</title>				</subfield>			</subfield_list>		</field55>		<field06>			<title>				<desc lan="ISO-8859-1" name="Description"/>				<desc lan="Big5" name="簡介"/>				<desc lan="GB2312" name="简介"/>			</title>		</field06>		<!--		<rsv_main_room type="constant" marked="no" required="no" external_field="yes">			<title>				<desc lan="ISO-8859-1" name="Main room"/>				<desc lan="Big5" name="主房間"/>				<desc lan="GB2312" name="主房间"/>			</title>		</rsv_main_room>		<rsv_venue type="constant" marked="no" required="no" external_field="yes">			<title>				<desc lan="ISO-8859-1" name="Venue"/>				<desc lan="Big5" name="地點"/>				<desc lan="GB2312" name="地点"/>			</title>		</rsv_venue>		-->	</section>	<section id="2">		<title>			<desc lan="ISO-8859-1" name="Administrative information"/>			<desc lan="Big5" name="管理資訊"/>			<desc lan="GB2312" name="管理信息"/>		</title>		<field54>			<title>				<desc lan="ISO-8859-1" name="Address"/>				<desc lan="Big5" name="地址"/>				<desc lan="GB2312" name="地址"/>			</title>		</field54>		<field59>			<title>				<desc lan="ISO-8859-1" name="Fee"/>				<desc lan="Big5" name="報名費"/>				<desc lan="GB2312" name="报名费"/>			</title>		</field59>		<field114>			<title>				<desc lan="ISO-8859-1" name="Credit"/>				<desc lan="Big5" name="學分"/>				<desc lan="GB2312" name="学分"/>			</title>		</field114>		<field56>			<title>				<desc lan="ISO-8859-1" name="Remarks"/>				<desc lan="Big5" name="備註"/>				<desc lan="GB2312" name="备注"/>			</title>		</field56>		<item_access type="item_access_pickup" paramname="iac_id_lst" id="TADM" dependant="training_center" arrayparam="true">			<title>				<desc lan="ISO-8859-1" name="Training administrator"/>				<desc lan="Big5" name="培訓管理員"/>				<desc lan="GB2312" name="培训管理员"/>			</title>		</item_access>		<field160>			<title>				<desc lan="ISO-8859-1" name="Lecturer type"/>				<desc lan="Big5" name="講師類型"/>				<desc lan="GB2312" name="讲师类型"/>			</title>			<field160 id="1">				<title>					<desc lan="ISO-8859-1" name="Internal lecturer"/>					<desc lan="Big5" name="内部講師"/>					<desc lan="GB2312" name="内部讲师"/>				</title>			</field160>			<field160 id="2">				<title>					<desc lan="ISO-8859-1" name="External lecturer"/>					<desc lan="Big5" name="外部講師"/>					<desc lan="GB2312" name="外部讲师"/>				</title>			</field160>		</field160>		<item_access type="item_access_pickup" paramname="iac_id_lst" id="INSTR" arrayparam="true" dependant="field160" >		</item_access>		<item_access exam_ind="true" blend_ind="true" type="item_access_pickup" paramname="iac_id_lst" id="EXA" arrayparam="true" dependant="training_center"/>		<item_status type="radio" value="OFF" paramname="itm_status" required="yes">			<title>				<desc lan="ISO-8859-1" name="Publish to catalog"/>				<desc lan="Big5" name="在目錄中發佈"/>				<desc lan="GB2312" name="在目录中发布"/>			</title>			<item_status id="1" value="ALL">				<title>					<desc lan="ISO-8859-1" name="Yes"/>					<desc lan="Big5" name="是"/>					<desc lan="GB2312" name="是"/>				</title>			</item_status>			<item_status id="2" value="OFF">				<title>					<desc lan="ISO-8859-1" name="No"/>					<desc lan="Big5" name="否"/>					<desc lan="GB2312" name="否"/>				</title>			</item_status>		</item_status>		<field57>			<title>				<desc lan="ISO-8859-1" name="Quota"/>				<desc lan="Big5" name="名額限制"/>				<desc lan="GB2312" name="名额限制"/>			</title>		</field57>		<field21>			<title>				<desc lan="ISO-8859-1" name="Send enrollment notification email"/>				<desc lan="Big5" name="發送報名通知郵件"/>				<desc lan="GB2312" name="发送报名通知邮件"/>			</title>			<field21 id="1">				<title>					<desc lan="ISO-8859-1" name="Yes, send to learner only"/>					<desc lan="Big5" name="是，只發送給學員"/>					<desc lan="GB2312" name="是，只发送给学员"/>				</title>			</field21>			<field21 id="2">				<title>					<desc lan="ISO-8859-1" name="Yes, send to learner and Direct Supervisor(s)"/>					<desc lan="Big5" name="是，發送給學員及其所有直屬上司"/>					<desc lan="GB2312" name="是，发送给学员及其所有直属上司"/>				</title>			</field21>			<field21 id="3">				<title>					<desc lan="ISO-8859-1" name="No"/>					<desc lan="Big5" name="否"/>					<desc lan="GB2312" name="否"/>				</title>			</field21>		</field21>		<!--		<field111 dependant="training_center">			<title>				<desc lan="ISO-8859-1" name="Certificate Of Completion"/>				<desc lan="Big5" name="證書"/>				<desc lan="GB2312" name="证书"/>			</title>			<field111 id="1">				<title>					<desc lan="ISO-8859-1" name="Disable"/>					<desc lan="Big5" name="不適用"/>					<desc lan="GB2312" name="不适用"/>				</title>			</field111>			<field111 id="2">				<title>					<desc lan="ISO-8859-1" name="Enable, use the certificate:"/>					<desc lan="Big5" name="使用該證書:"/>					<desc lan="GB2312" name="使用该证书:"/>				</title>			</field111>		</field111>		-->		<!-- <field58>			<title>				<desc lan="ISO-8859-1" name="Enrollment confirmation remarks"/>				<desc lan="Big5" name="確認報名備註"/>				<desc lan="GB2312" name="确认报名备注"/>			</title>		</field58> -->		<field23 type="notify_email_limited" external_field="yes" paramname="itm_notify_days">			<title>				<desc lan="ISO-8859-1" name="Send course end date notification email"/>				<desc lan="Big5" name="發送結束提醒郵件"/>				<desc lan="GB2312" name="发送结束提醒邮件"/>			</title>		</field23>		<field24 type="notify_support_email" external_field="yes" paramname="itm_notify_email">			<title>				<desc lan="ISO-8859-1" name="Support email"/>				<desc lan="Big5" name="支持郵件"/>				<desc lan="GB2312" name="支持邮件"/>			</title>		</field24>		<!--		<targeted_lrn type="targeted_lrn_pickup" paramname="target_ent_group_lst" required="yes" label="lab_all_learners">			<title>				<desc lan="ISO-8859-1" name="Target enrollments"/>				<desc lan="Big5" name="可報名學員"/>				<desc lan="GB2312" name="可报名学员"/>			</title>		</targeted_lrn>		-->		<!--		<field60>			<title>				<desc lan="ISO-8859-1" name="Course related materials"/>				<desc lan="Big5" name="相關資料"/>				<desc lan="GB2312" name="相关资料"/>			</title>			<subfield_list>				<subfield id="1">					<title>						<desc lan="ISO-8859-1" name="File 1"/>						<desc lan="Big5" name="文件1"/>						<desc lan="GB2312" name="文件1"/>					</title>				</subfield>				<subfield id="2">					<title>						<desc lan="ISO-8859-1" name="File 2"/>						<desc lan="Big5" name="文件2"/>						<desc lan="GB2312" name="文件2"/>					</title>				</subfield>				<subfield id="3">					<title>						<desc lan="ISO-8859-1" name="File 3"/>						<desc lan="Big5" name="文件3"/>						<desc lan="GB2312" name="文件3"/>					</title>				</subfield>				<subfield id="4">					<title>						<desc lan="ISO-8859-1" name="File 4"/>						<desc lan="Big5" name="文件4"/>						<desc lan="GB2312" name="文件4"/>					</title>				</subfield>				<subfield id="5">					<title>						<desc lan="ISO-8859-1" name="File 5"/>						<desc lan="Big5" name="文件5"/>						<desc lan="GB2312" name="文件5"/>					</title>				</subfield>				<subfield id="6">					<title>						<desc lan="ISO-8859-1" name="File 6"/>						<desc lan="Big5" name="文件6"/>						<desc lan="GB2312" name="文件6"/>					</title>				</subfield>				<subfield id="7">					<title>						<desc lan="ISO-8859-1" name="File 7"/>						<desc lan="Big5" name="文件7"/>						<desc lan="GB2312" name="文件7"/>					</title>				</subfield>				<subfield id="8">					<title>						<desc lan="ISO-8859-1" name="File 8"/>						<desc lan="Big5" name="文件8"/>						<desc lan="GB2312" name="文件8"/>					</title>				</subfield>				<subfield id="9">					<title>						<desc lan="ISO-8859-1" name="File 9"/>						<desc lan="Big5" name="文件9"/>						<desc lan="GB2312" name="文件9"/>					</title>				</subfield>				<subfield id="10">					<title>						<desc lan="ISO-8859-1" name="File 10"/>						<desc lan="Big5" name="文件10"/>						<desc lan="GB2312" name="文件10"/>					</title>				</subfield>			</subfield_list>		</field60>	-->		<field22>			<title>				<desc lan="ISO-8859-1" name="Completion settle-date extension"/>				<desc lan="Big5" name="延長結訓結算時間"/>				<desc lan="GB2312" name="延长结训结算时间"/>			</title>			<suffix>				<desc lan="ISO-8859-1" name="days after class end date"/>				<desc lan="Big5" name="天(從結束日期算起)"/>				<desc lan="GB2312" name="天(从结束日期算起)"/>			</suffix>		</field22>		<field25>			<title>				<desc lan="ISO-8859-1" name="Activate Point Calculation"/>				<desc lan="Big5" name="自動積分"/>				<desc lan="GB2312" name="自动积分"/>			</title>			<field25 id="1">				<title>					<desc lan="ISO-8859-1" name="Yes"/>					<desc lan="Big5" name="是"/>					<desc lan="GB2312" name="是"/>				</title>			</field25>			<field25 id="2">				<title>					<desc lan="ISO-8859-1" name="No"/>					<desc lan="Big5" name="否"/>					<desc lan="GB2312" name="否"/>				</title>			</field25>		</field25>		<field26>			<title>				<desc lan="ISO-8859-1" name="Difficulty factor"/>				<desc lan="Big5" name="難度係數"/>				<desc lan="GB2312" name="难度系数"/>			</title>		</field26>		<!--  <field98>			<title>				<desc lan="ISO-8859-1" name="Plan code"/>				<desc lan="Big5" name="計劃編號"/>				<desc lan="GB2312" name="计划编号"/>			</title>		</field98>  -->				<create_date type="read_datetime" external_field="yes">			<title>				<desc lan="ISO-8859-1" name="Created"/>				<desc lan="Big5" name="創建日期"/>				<desc lan="GB2312" name="创建日期"/>			</title>		</create_date>		<created_by type="read" external_field="yes">			<title>				<desc lan="ISO-8859-1" name="Created by"/>				<desc lan="Big5" name="創建者"/>				<desc lan="GB2312" name="创建者"/>			</title>		</created_by>		<update_date type="read_datetime" external_field="yes">			<title>				<desc lan="ISO-8859-1" name="Modifed"/>				<desc lan="Big5" name="修改日期"/>				<desc lan="GB2312" name="修改日期"/>			</title>		</update_date>		<updated_by type="read" external_field="yes">			<title>				<desc lan="ISO-8859-1" name="Modified by"/>				<desc lan="Big5" name="修改者"/>				<desc lan="GB2312" name="修改者"/>			</title>		</updated_by>	</section>	<hidden>		<field150/>		<!--		<field151/>		<field152/>		<field153/>		<field154/>		-->		<field155/>	</hidden></template_view>'
where tvw_tpl_id='13' and tvw_id='DETAIL_VIEW'

go

/**
Auth: Jasper
Date: 2018-05-30
Desc: 屏蔽 考试管理，知识管理（知识库，知识目录，知识标签，知识审批），社区管理（问答管理，群组管理），将积分管理变成一级模块
*/
update acFunction set ftn_status = 0 where ftn_id in(633,635,636,663,665,667,668,669,670);
go
update acFunction set ftn_level = 0,ftn_parent_id = null where ftn_id =666;
go
/**
Auth: Jasper
Date: 2018-05-31
Desc: 调整积分管理位置，原来的积分管理ftn_order为52，现改成跟社区管理同一order，为了页面显示排序，往后开放社区改回
*/
update acFunction set ftn_order = 8 where ftn_id = 666;
go

/**
Auth: Jasper
Date: 2018-05-31
Desc: 屏蔽社区相关的自动积分：知识分享，创建群组，参与群组，在线回答得分，回答被采纳为最佳答案得分，取消提问，在线提问得分
	（并非按顺序对应）'KB_SHARE_KNOWLEDGE','SYS_CREATE_GROUP','SYS_JION_GROUP','ZD_COMMIT_ANS','ZD_RIGHT_ANS','ZD_CANCEL_QUE','ZD_NEW_QUE'
*/
update creditsType set cty_deleted_ind = 1 where cty_id in(2,3,4,6,32,33,39);
go


/**
Auth: Peng
Date: 2018-07-10
Desc：屏蔽CPT/D管理子栏目CPT/D历史保存管理功能（Bug 19068 - 歷史記錄功能要刪走, 不會再用 (安排空閒時間再做)）
*/
update acFunction set ftn_status = 0 where ftn_id = 725;
go


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
go


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
go


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
Go

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
Go

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
Go

DELETE FROM messageParamName  WHERE mpn_mtp_id IN (select mtp_id from messageTemplate where mtp_type='JI') AND mpn_name = '[Learner(s)]';
GO

DELETE FROM messageParamName WHERE mpn_mtp_id IN (select mtp_id from messageTemplate where mtp_type='REMINDER') AND mpn_name = '[Learner(s)]';
GO


/*   请保持该段话在本文件的最后;
     db_change 修改规则， 
 *   1、如果修改表结构，新增表，一定要同时修改core数据库详细设计文档.xls , 并说明表的用途，每列是保存什么数据，作什么用。
        如果抓到谁不同时更新，就抓出来鞭PP
 */
