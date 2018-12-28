/**
Auth: andrew.xiao
Date: 2016-07-25
Desc: 课程表添加是否发布到移动端的标志
 */
alter table aeItem add itm_mobile_ind varchar(5);


/**
Auth: andrew.xiao
Date: 2016-07-26
Desc: 课程表添加是否发布到移动端的标志，需要更新之前的数据
 */
update aeItem set itm_mobile_ind = 'no';		
update aeItem set itm_mobile_ind = 'yes'
where itm_id in (
SELECT cos_itm_id FROM
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
)
 TEMP);
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

/**
Auth: Randy
Date: 2016-09-6
Desc: 把专题培训放到“培训管理下”
 */
update acfunction set ftn_parent_id = (
	select ftn_id from( select ftn_id from acfunction where ftn_ext_id ='FTN_AMD_TRAINING_MGT')AS tmp
), 
ftn_order=31 
where 
ftn_ext_id = 'FTN_AMD_SPECIALTOPIC_MAIN' ;

/**
Auth: Randy
Date: 2016-09-6
Desc: 删除培训管理角色下的“PC样式管理，移动样式管理”两个功能, 即培训管理默认没有两个功能的操作权限。
     对于LNOW模式下的环境，如果原来已有数据，请不要执行句SQL。

 */
delete from acrolefunction where rfn_rol_id = (select rol_id from acrole where rol_ext_id = 'TADM_1') 
and rfn_ftn_id in(
select ftn_id from acfunction where ftn_ext_id ='FTN_AMD_POSTER_MAIN' or ftn_ext_id ='FTN_AMD_MOBILE_POSTER_MAIN'
);



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
   
update acfunction set ftn_parent_id = (
	select ftn_id from( select ftn_id from acfunction where ftn_ext_id ='FTN_AMD_SNS_MGT')AS tmp
), 
ftn_order=31 
where 
ftn_ext_id = 'FTN_AMD_CREDIT_SETTING_MAIN' ;



/**
Auth: bill
Date: 2016-10-13
Desc: 更改課程表圖片名字字段的長度
 */
ALTER TABLE aeItem MODIFY itm_icon varchar(400);



/**
Auth: Randy
Date: 2016-10-6
      把“邮件模板管理”放到“系统设置”下
 */
update acfunction set ftn_parent_id = (
	select ftn_id from( select ftn_id from acfunction where ftn_ext_id ='FTN_AMD_SYSTEM_SETTING_MGT')AS tmp
)
where 
ftn_ext_id = 'FTN_AMD_MESSAGE_TEMPLATE_MAIN' ;

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
('API_UPDATE_CREDITS','API_UPDATE_CREDITS','1','1','0','1','API','0','0','s1u3',localtime(),'s1u3',localtime(),1);


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
/**
 * Auth: andrew.xiao
 * Date: 2016-11-23
 * Desc:emailMessage 和 webMessage的中间关联表
 */ 
CREATE TABLE emsgRwmsg(
	emsg_id int NOT NULL,
	wmsg_id int NOT NULL
);



/**
Auth: LEAF
Date: 2016-10-28
Desc: 提示信息维护功能
*/
DROP TABLE IF EXISTS helpQuestionType;
commit;

create table helpQuestionType
(
    hqt_id int NOT NULL AUTO_INCREMENT,
    hqt_type_name nvarchar(100) NOT NULL,
	hqt_pid int NOT NULL,
	hqt_top_index int NOT NULL,
	hqt_is_publish int ,
	hqt_create_timestamp datetime NOT NULL,
	hqt_language nvarchar(50) NOT NULL,
	 PRIMARY KEY ( hqt_id )
)
commit;


DROP TABLE IF EXISTS helpQuestion;
commit;

CREATE TABLE helpQuestion(
	hq_id int NOT NULL AUTO_INCREMENT,
	hq_type_id int NOT NULL,
    hq_title nvarchar(255) NOT NULL,
	hq_content_cn text NOT NULL,
	hq_create_timestamp datetime NOT NULL,
	hq_update_timestamp datetime NULL,
	hq_top_index int NOT NULL,
	hq_width int NOT NULL,
	hq_height int NOT NULL,
	hq_Template nvarchar(50) NOT NULL,
	hqt_number nvarchar(50) NULL,
	hq_content_us text NULL,
  PRIMARY KEY ( hq_id )
)
commit;





/**

Auth: lucky
Date: 2016-11-15
Desc: 重要对像详细操作日志数据表
 */	 
CREATE TABLE objectActionLog (
	object_id int NOT NULL,
	object_code varchar(255) NULL,
	object_title varchar(255) NULL,
	object_type varchar(50) NOT NULL,
	object_action varchar(50) NOT NULL,
	object_action_type varchar(50) NOT NULL,
	object_action_time datetime NOT NULL,
	object_opt_user_id int NOT NULL
);

CREATE TABLE gourpLoginReport(
	gplr_grp_id int NOT NULL,  
	gplr_year int NOT NULL,     
	gplr_month int NOT NULL,
	gplr_login_mode int NOT NULL, 
	gplr_totle_login_number int NOT NULL,
	gplr_last_update_date datetime NOT NULL
); 

CREATE TABLE gradeLoginReport(
	gdlr_ugr_id int NOT NULL,  
	gdlr_year int NOT NULL,     
	gdlr_month int NOT NULL,
	gdlr_login_mode int NOT NULL, 
	gdlr_totle_login_number int NOT NULL,
	gdlr_last_update_date datetime NOT NULL
);

CREATE TABLE positionLoginReport(
	pslr_upt_id int NOT NULL,   
	pslr_year int NOT NULL,     
	pslr_month int NOT NULL,
	pslr_login_mode int NOT NULL, 
	pslr_totle_login_number int NOT NULL,
	pslr_last_update_date datetime NOT NULL  
); 


/**
Auth: andrew
Date: 2016-11-16
Desc: 系统设置添加是否允许多账号登录
 */	 
 INSERT INTO SystemSetting 
(sys_cfg_type , sys_cfg_value , sys_cfg_create_timestamp , sys_cfg_create_usr_id ,
sys_cfg_update_timestamp,sys_cft_update_usr_id)
VALUES('MULTIPLE_LOGIN_IND','1',
'2016-11-16 16:00:00' , 's1u3' , '2016-11-16 16:00:00' , 's1u3');
commit;



/**Auth: andrew.xiao
 * Date : 2016-11-28
 * Desc: 保存用户密码历史记录
 */
CREATE TABLE UserPasswordHistory(
  uph_usr_ent_id int NOT NULL,
  uph_pwd nvarchar(30) NOT NULL,
  uph_update_usr_ent_id int NOT NULL,
  uph_client_type nvarchar(10) NOT NULL,
  uph_create_time datetime NOT NULL
);


/**Auth: andrew.xiao
 * Date : 2016-11-28
 * Desc: 系统设置，密码安全策略
 */
INSERT INTO SystemSetting 
(sys_cfg_type , sys_cfg_value , sys_cfg_create_timestamp , sys_cfg_create_usr_id ,
sys_cfg_update_timestamp,sys_cft_update_usr_id)
VALUES('PASSWORD_POLICY_COMPARE_COUNT','',
sysdate() , 's1u3' , sysdate() , 's1u3');

INSERT INTO SystemSetting 
(sys_cfg_type , sys_cfg_value , sys_cfg_create_timestamp , sys_cfg_create_usr_id ,
sys_cfg_update_timestamp,sys_cft_update_usr_id)
VALUES('PASSWORD_POLICY_PERIOD','',
sysdate() , 's1u3' , sysdate() , 's1u3');

INSERT INTO SystemSetting 
(sys_cfg_type , sys_cfg_value , sys_cfg_create_timestamp , sys_cfg_create_usr_id ,
sys_cfg_update_timestamp,sys_cft_update_usr_id)
VALUES('PASSWORD_POLICY_PERIOD_FORCE','',
sysdate() , 's1u3' , sysdate() , 's1u3');
commit;


/*
Auth: bill
Date: 2016-10-27（更新于12-13）
Desc:直播管理
*/
--删除直播管理权限（没有执行过直播相关的sql。不用执行）
select @live_old_ftn_id:=ftn_id from acFunction where ftn_ext_id='FTN_AMD_LIVE_MAIN';
delete acRoleFunction where rfn_ftn_id = @live_old_ftn_id;
delete acFunction where ftn_ext_id = 'FTN_AMD_LIVE_MAIN';

--增加直播管理权限
select @max_ftn_order_id := max(ftn_order) + 1 from acFunction;
insert into acFunction (ftn_ext_id, ftn_level, ftn_creation_timestamp,ftn_assign, ftn_tc_related,ftn_status,ftn_order)
values ('FTN_AMD_LIVE_MAIN', 0, CURDATE(), 1, 'Y', 1, @max_ftn_order_id);

--默认把直播功能的权限分给“培训管理员”
select @tadm_rol_id := rol_id from acRole where rol_ext_id = 'TADM_1';
select @live_ftn_id := ftn_id from acFunction where ftn_ext_id='FTN_AMD_LIVE_MAIN';
select @live_ftn_order := ftn_order from acFunction where ftn_ext_id='FTN_AMD_LIVE_MAIN';
insert into acRoleFunction values(@tadm_rol_id, @live_ftn_id,'s1u3',sysdate(),0, @live_ftn_order,null);

  
/**
 * 直播表
 */
create table liveItem  
(  
	lv_id INT NOT NULL AUTO_INCREMENT,  
	lv_title nvarchar(50),				
	lv_create_datetime datetime, 
	lv_start_datetime datetime,
	lv_end_datetime datetime,	
	lv_create_usr_id nvarchar(20), 
	lv_upd_datetime datetime,
	lv_upd_usr_id nvarchar(20), 
	lv_url nvarchar(500),
	lv_desc nvarchar(1000),
	lv_pwd nchar(20),
	lv_webinar_id int, 
	lv_record_id int default'1', 
	lv_remark nvarchar(500),
	lv_instr_desc nvarchar(500), 
	lv_image nvarchar(100),  
	lv_status nvarchar(20), 
	lv_real_start_datetime datetime,
	lv_need_pwd int,
  	lv_tcr_id int,
  	lv_people_num int, 
  	lv_type int, 
  	lv_had_live int, 
  PRIMARY KEY ( lv_id )
);
/**
 * 直播观看表
 */
create table liveRecords
(
	lr_usr_id nvarchar(20),	
	lr_create_time datetime,	
	lr_live_id	int,	
	lr_status int
);


/**
 * Auth: leaf
 * Date: 2016-12-01(更新于12-13)
 * 系统运作日志
 */
--删除系统运作日志功能（没有执行过系统运作日志相关的sql。不用执行）
select @rfn_ftn_id:=ftn_id from acFunction where ftn_ext_id='FTN_AMD_SYS_SETTING_LOG';
delete from  acRoleFunction where rfn_ftn_id = @rfn_ftn_id;
delete from acFunction where ftn_ext_id = 'FTN_AMD_SYS_SETTING_LOG';
COMMIT;

--新增系统运作日志功能
select @ftn_id_parent:= ftn_id from acFunction where ftn_ext_id = 'FTN_AMD_SYSTEM_SETTING_MGT';
select @order:= max(ftn_order)+1  from acFunction;
INSERT acFunction (ftn_ext_id, ftn_level, ftn_creation_timestamp,ftn_assign, ftn_tc_related,ftn_status,ftn_order,ftn_parent_id)
VALUES ('FTN_AMD_SYS_SETTING_LOG', 1,CURDATE(), 0, 'N', 1, @order, @ftn_id_parent);

INSERT acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_usr_id, RFN_CREATE_TIMESTAMP,rfn_ftn_parent_id, rfn_ftn_order) 
select rol_id, child.ftn_id, 's1u3', CURDATE(),parent.ftn_id, 1  from acRole, acFunction child, acFunction parent
where rol_ext_id like 'ADM_%' and child.ftn_ext_id = 'FTN_AMD_SYS_SETTING_LOG' and parent.ftn_ext_id = 'FTN_AMD_SYSTEM_SETTING_MGT';
COMMIT;

/**
 * Auth: leaf
 * Date: 2016-12-13
 * 提示信息内容表 修改hq_type_id,hq_content_cn字段允许为空
 */
alter table helpQuestion modify hq_type_id int null;
alter table helpQuestion modify hq_content_cn text null;
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
sysdate() , 's1u3' , sysdate() , 's1u3');
commit;


/**
 * Auth: bill
 * Date: 2016-12-15
 * 新增列：eip_live_max_count；直播并发数
 */
alter table EnterpriseInfoPortal add column eip_live_max_count int;


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
update acFunction set ftn_tc_related = 'Y' where  ftn_parent_id = (select * from (select distinct(ftn_id)  from acFunction where ftn_ext_id = 'FTN_AMD_EXAM_MGT')as tmp);
update acFunction set ftn_tc_related = 'Y' where  ftn_parent_id = (select * from (select distinct(ftn_id)  from acFunction where ftn_ext_id = 'FTN_AMD_ITM_COS_MAIN')as tmp);



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







/**
 * Auth: bill
 * Date: 2017-03-06
 * 系统管理员，运行参数，上传文件约束，加一个默认不允许文件jspx在数据库中
 */
update  SystemSetting set sys_cfg_value ='jsp,asp,exe,bat,bin,php,sys,com,Mach-O,ELF,dll,reg,jspx,aspx' where sys_cfg_type= 'SYS_UPLOAD_FORBIDDEN';


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

/**
 * Auth: lucky
 * Date: 2017-2-14
 * 修改重要日志添加操作者登陆时间，IP
 */
alter table objectActionLog add object_opt_user_login_time datetime NULL;
alter table objectActionLog add object_opt_user_login_ip varchar(100) NULL;

/**
 * Auth: leaf
 * Date: 2017-2-16
 * 只用于在2017年2月运行的6.5版本。
 * 用户登录日志数据表为每月初时自动创建，相对与6.3版本，6.5新增了登录状态和登录ip。
 * 如果使用的数据库运行了6.3版本，则需要删除对应月份的用户登录日志数据表。
 * 运行6.5时会自动创建新的表。
 */
DROP TABLE IF EXISTS loginLog20172;




/**
 * Auth: leaf
 * Date: 2017-03-07
 * 新增CPT/D 管理 功能
 */

--新增一级功能  CPT/D 管理  
select @order := max(ftn_order) from acFunction where ftn_level = 0;
INSERT acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_tc_related, ftn_status, ftn_assign, ftn_order, ftn_parent_id) 
VALUES ('FTN_AMD_CPT_D_MGT', '0', NULL, CURDATE(), 'N', '1', '1', @order+1, null);


--新增4个二级功能
--CPT/D 牌照管理 --CPT/D 牌照注册管理 --历史保存记录 --CPT/D报表备注维护

select @ftn_order := max(ftn_order)+1 from acFunction where ftn_level = 1;
select @ftn_id_parent := ftn_id from acFunction where ftn_ext_id = 'FTN_AMD_CPT_D_MGT';

INSERT acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_tc_related, ftn_status, ftn_assign, ftn_order, ftn_parent_id)  
VALUES ('FTN_AMD_CPT_D_LIST', '1', NULL, CURDATE(), 'N', '1', '1', @ftn_order, @ftn_id_parent);

select @ftn_order := max(ftn_order)+1 from acFunction where ftn_level = 1;

INSERT acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_tc_related, ftn_status, ftn_assign, ftn_order, ftn_parent_id)  
VALUES ('FTN_AMD_CPT_D_LICENSE_LIST', '1', NULL, CURDATE(), 'N', '1', '1', @ftn_order, @ftn_id_parent);

select @ftn_order := max(ftn_order)+1 from acFunction where ftn_level = 1;

INSERT acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_tc_related, ftn_status, ftn_assign, ftn_order, ftn_parent_id) 
VALUES ('FTN_AMD_CPT_D_HISTORY', '1', NULL, CURDATE(), 'N', '1', '1', @ftn_order, @ftn_id_parent);

select @ftn_order := max(ftn_order)+1 from acFunction where ftn_level = 1;

INSERT acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_tc_related, ftn_status, ftn_assign, ftn_order, ftn_parent_id) 
VALUES ('FTN_AMD_CPT_D_NOTE', '1', NULL, CURDATE(), 'N', '1', '1', @ftn_order, @ftn_id_parent);


--CPT/D 管理功能，及其四个下级子功能默认分配给“系统管理员”角色
INSERT acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_usr_id, RFN_CREATE_TIMESTAMP,rfn_ftn_parent_id, rfn_ftn_order) 
select rol_id, child.ftn_id, 's1u3', CURDATE(),null, 1  from acRole, acFunction child, acFunction parent
where rol_ext_id like 'ADM_%' and child.ftn_ext_id = 'FTN_AMD_CPT_D_MGT' and parent.ftn_ext_id = 'FTN_AMD_CPT_D_MGT';

INSERT acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_usr_id, RFN_CREATE_TIMESTAMP,rfn_ftn_parent_id, rfn_ftn_order) 
select rol_id, child.ftn_id, 's1u3', CURDATE(),parent.ftn_id, 1  from acRole, acFunction child, acFunction parent
where rol_ext_id like 'ADM_%' and child.ftn_ext_id = 'FTN_AMD_CPT_D_LIST' and parent.ftn_ext_id = 'FTN_AMD_CPT_D_MGT';

INSERT acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_usr_id, RFN_CREATE_TIMESTAMP,rfn_ftn_parent_id, rfn_ftn_order) 
select rol_id, child.ftn_id, 's1u3', CURDATE(),parent.ftn_id, 1  from acRole, acFunction child, acFunction parent
where rol_ext_id like 'ADM_%' and child.ftn_ext_id = 'FTN_AMD_CPT_D_LICENSE_LIST' and parent.ftn_ext_id = 'FTN_AMD_CPT_D_MGT';

INSERT acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_usr_id, RFN_CREATE_TIMESTAMP,rfn_ftn_parent_id, rfn_ftn_order) 
select rol_id, child.ftn_id, 's1u3', CURDATE(),parent.ftn_id, 1  from acRole, acFunction child, acFunction parent
where rol_ext_id like 'ADM_%' and child.ftn_ext_id = 'FTN_AMD_CPT_D_HISTORY' and parent.ftn_ext_id = 'FTN_AMD_CPT_D_MGT';

INSERT acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_usr_id, RFN_CREATE_TIMESTAMP,rfn_ftn_parent_id, rfn_ftn_order) 
select rol_id, child.ftn_id, 's1u3', CURDATE(),parent.ftn_id, 1  from acRole, acFunction child, acFunction parent
where rol_ext_id like 'ADM_%' and child.ftn_ext_id = 'FTN_AMD_CPT_D_NOTE' and parent.ftn_ext_id = 'FTN_AMD_CPT_D_MGT';



/**
 * Auth: bill
 * Date: 2017-02-13
 * 新增列：eip_live_mode；直播模式
 */
alter table EnterpriseInfoPortal add column eip_live_mode varchar(255);

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
alter table liveItem add column lv_mode_type varchar(255);
alter table liveItem add column lv_upstream_address varchar(255);
alter table liveItem add column lv_hls_downstream_address varchar(255);
alter table liveItem add column lv_rtmp_downstream_address varchar(255);
alter table liveItem add column lv_flv_downstream_address varchar(255);
alter table liveItem add column lv_channel_id varchar(255);

alter table liveItem drop column lv_remark;
alter table liveItem drop column lv_instr_desc;

/**
 * Auth: bill
 * Date: 2017-02-28
 * 新增列：eip_live_qcloud_secretid；腾讯云直播id
 * 新增列：eip_live_qcloud_secretkey；腾讯云直播key
 */
alter table EnterpriseInfoPortal add column eip_live_qcloud_secretid varchar(255);
alter table EnterpriseInfoPortal add column eip_live_qcloud_secretkey varchar(255);

/**
 * Auth: andrew
 * Date: 2017-03-13
 * 统计用户在线人数，添加登录标识
 */
alter table CurrentActiveUser add column cau_login_type varchar(255);

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
alter table liveItem add lv_student_token varchar(255);
alter table liveItem add lv_teacher_token varchar(255);
alter table liveItem add lv_student_client_token varchar(255);
alter table liveItem add lv_teacher_join_url varchar(255);
alter table liveItem add lv_student_join_url varchar(255);
alter table liveItem add lv_gensee_online_user int;
alter table liveItem add lv_real_end_datetime datetime;
alter table liveItem add lv_gensee_record_url varchar(255);


/**
 * Auth: lucky
 * Date: 2017-4-26
 * cpd功能新增表
 */
CREATE TABLE cpdType(
  ct_id int primary key NOT NULL auto_increment ,
  ct_license_type varchar(100) NOT NULL,
  ct_license_alias varchar(255) NOT NULL,
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
  ct_status varchar(50) NOT NULL
);

ALTER TABLE cpdType ADD UNIQUE (ct_license_type);

CREATE TABLE cpdGroup(
  cg_id int primary key NOT NULL auto_increment,
  cg_code varchar(100) NOT NULL,
  cg_alias varchar(255) NOT NULL,
  cg_display_order int NULL,
  cg_contain_non_core_ind int NOT NULL,
  cg_display_in_report_ind int NOT NULL,
  cg_ct_id int NOT NULL,
  cg_create_usr_ent_id int NOT NULL,
  cg_create_datetime timestamp NOT NULL,
  cg_update_usr_ent_id int NULL,
  cg_update_datetime timestamp NULL,
  cg_status varchar(50) NOT NULL
);

CREATE TABLE cpdGroupPeriod(
	cgp_id int primary key not NULL auto_increment,
	cgp_effective_time date NOT NULL,
	cgp_ct_id int NOT NULL,
	cgp_cg_id int NOT NULL,
	cgp_create_usr_ent_id int NOT NULL,
	cgp_create_datetime timestamp NOT NULL,
	cgp_update_usr_ent_id int NULL,
	cgp_update_datetime timestamp NULL,
	cgp_status varchar(100) NOT NULL
 );


CREATE TABLE cpdGroupHours(
	cgh_id int primary key not NULL auto_increment,
	cgh_cgp_id int NOT NULL,
	cgh_declare_month int NOT NULL,
	cgh_core_hours float NOT NULL,
	cgh_non_core_hours float NULL,
	cgh_create_usr_ent_id int NOT NULL,
	cgh_create_datetime timestamp NOT NULL,
	cgh_update_usr_ent_id int NULL,
	cgh_update_datetime timestamp NULL,
	cgh_status varchar(50) NOT NULL
);

CREATE TABLE cpdRegistration(
	cr_id int primary key NOT NULL auto_increment,
	cr_usr_ent_id int NOT NULL,
	cr_ct_id int NOT NULL,
	cr_reg_number varchar(255) not NULL,
	cr_reg_datetime date NOT NULL,
	cr_de_reg_datetime date NULL,
	cr_create_usr_ent_id int NOT NULL,
	cr_create_datetime timestamp NOT NULL,
	cr_update_usr_ent_id int NULL,
	cr_update_datetime timestamp NULL,
	cr_status varchar(100) NOT NULL
);

CREATE TABLE cpdGroupRegistration(
	cgr_id int primary key not null auto_increment,
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
	cgr_status varchar(100) NULL
);

CREATE TABLE cpdGroupRegHours(
	cgrh_id int primary key not null auto_increment,
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

CREATE TABLE aeItemCPDItem(
	aci_id int primary key not null auto_increment,
	aci_itm_id int NOT NULL,
	aci_accreditation_code varchar(255) NULL,
	aci_hours_end_date DATE NULL,
	aci_create_usr_ent_id int NOT NULL,
	aci_create_datetime timestamp NOT NULL,
	aci_update_usr_ent_id int NULL,
	aci_update_datetime timestamp NULL
);

CREATE TABLE aeItemCPDGourpItem(
	acgi_id int primary key NOT NULL auto_increment,
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


CREATE TABLE cpdLrnAwardRecord(
	clar_id int primary key NOT NULL auto_increment,
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

CREATE TABLE cpdGroupRegHoursHistory(
	cghi_id int primary key NOT NULL auto_increment,
	cghi_usr_ent_id int NOT NULL,
	cghi_ct_id int NOT NULL,
	cghi_cg_id int NOT NULL,
	cghi_cr_reg_number VARCHAR(255) NULL,
	cghi_cgp_id int  NULL,
	cghi_cg_contain_non_core_ind int  NULL,
	cghi_license_type VARCHAR(100) NULL,
	cghi_license_alias VARCHAR(200) NULL,
	cghi_cal_month int not NULL,
	cghi_cal_before_ind int NULL,
	cghi_recover_hours_period int NULL,
	cghi_code VARCHAR(100) NULL,
	cghi_alias VARCHAR(200) NULL,
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

CREATE TABLE cpdGroupRegCourseHistory(
	crch_id int primary key  NOT NULL auto_increment,
	crch_cghi_id int NOT NULL,
	crch_aci_id int NULL,
	crch_aci_hours_end_date timestamp NULL,
	crch_aci_accreditation_code VARCHAR(100) NULL,
	crch_itm_id int NOT NULL,
	crch_itm_title VARCHAR(255) NULL,
	crch_itm_code VARCHAR(255) NULL,
	crch_period int NULL,
	crch_first_ind int NULL,
	crch_app_id int NOT NULL,
	crch_cgr_id int NOT NULL,
	crch_cr_id int NOT NULL,
	crch_ct_id int NOT NULL,
	crch_ct_license_type VARCHAR(100) NULL,
	crch_ct_license_alias VARCHAR(200) NULL,
	crch_cg_id int NOT NULL,
	crch_cg_code VARCHAR(100) NULL,
	crch_cg_alias VARCHAR(200) NULL,
	crch_award_core_hours float NULL,
	crch_award_non_core_hours float NULL,
	crch_award_datetime timestamp NULL,
	crch_create_datetime timestamp NULL,
	crch_update_datetime timestamp NULL
);


CREATE TABLE cpdReportRemark(
	crpm_id int primary key NOT NULL auto_increment,
	crpm_report_code VARCHAR(255) NOT NULL,
	crpm_report_desc VARCHAR(255) NULL,
	crpm_report_remark VARCHAR(2000) NOT NULL,
	crpm_create_datetime timestamp NOT NULL,
	crpm_create_usr_ent_id int NOT NULL,
	crpm_update_datetime timestamp NULL,
	crpm_update_usr_ent_id int NULL
);

CREATE TABLE cpdReportRemarkHistory(
	crmh_id int primary key NOT NULL auto_increment,
	crmh_crpm_id int NOT NULL,
	crmh_report_code VARCHAR(255) NOT NULL,
	crmh_report_desc VARCHAR(255) NULL,
	crmh_report_remark VARCHAR(2000) NOT NULL,
	crpm_create_datetime timestamp NOT NULL,
	crpm_last_update_datetime timestamp NULL,
	crpm_his_create_datetime timestamp NULL
);

/**
 * Auth: lucky
 * Date: 2017-03-30
 * 修改cpdReportRemark的crpm_report_desc字段
 * 修改cpdReportRemarkHistory的crpm_create_datetime字段
 */
alter table cpdReportRemark drop column crpm_report_desc;

alter table cpdReportRemarkHistory drop column crmh_report_desc;

alter table cpdReportRemarkHistory drop column crpm_create_datetime;

alter table cpdReportRemarkHistory add crpm_his_period int not Null;;

alter table cpdReportRemarkHistory drop column crpm_last_update_datetime;

alter table cpdReportRemarkHistory add crpm_his_save_month int not Null;;

alter table cpdGroupRegHoursHistory add cghi_cgr_id int not Null;;

/**
 * Auth: Archer
 * Date: 2017-04-26
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
Training &amp; Development
	</p>
	<p>
		<br />
	</p>
	<p>
		<span style="color:#4C33E5;">[Footer image]</span>
	</p>
</p>',
'lab_CPTD_OUTSTANDING_LEARNER',1,1,1,NOW(),3);

insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) select mtp_id,'[Learner name]','lab_msg_learner_name' from messageTemplate where mtp_type='CPTD_OUTSTANDING_LEARNER';
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) select mtp_id,'[Header image]','lab_msg_header_image' from messageTemplate where mtp_type='CPTD_OUTSTANDING_LEARNER';
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) select mtp_id,'[Footer image]','lab_msg_footer_image' from messageTemplate where mtp_type='CPTD_OUTSTANDING_LEARNER';
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) select mtp_id,'[License Alias]','lab_license_alias' from messageTemplate where mtp_type='CPTD_OUTSTANDING_LEARNER';

/**
 * Auth: Archer
 * Date: 2017-04-26
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
Training &amp; Development
</p>
<p>
  <br />
</p>
<p>
  <span style="color:#4C33E5;">[Footer image]</span>
</p>',
'lab_CPTD_OUTSTANDING_SUPERVISOR',1,1,1,NOW(),3);

insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) select mtp_id,'[Learner name]','lab_msg_learner_name' from messageTemplate where mtp_type='CPTD_OUTSTANDING_SUPERVISOR';
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) select mtp_id,'[Header image]','lab_msg_header_image' from messageTemplate where mtp_type='CPTD_OUTSTANDING_SUPERVISOR';
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) select mtp_id,'[Footer image]','lab_msg_footer_image' from messageTemplate where mtp_type='CPTD_OUTSTANDING_SUPERVISOR';
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) select mtp_id,'[License Alias]','lab_license_alias' from messageTemplate where mtp_type='CPTD_OUTSTANDING_SUPERVISOR';

/**
Auth: leaf
Date: 2017-05-9
Desc: 屏蔽CPT/D功能
 */
update acFunction set ftn_status = '0' where ftn_ext_id = 'FTN_AMD_CPT_D_MGT';


/**
Auth: leaf
Date: 2017-05-9
Desc: 开启CPT/D功能
 */
update acFunction set ftn_status = '1' where ftn_ext_id = 'FTN_AMD_CPT_D_MGT';

/**
Auth: leaf
Date: 2017-05-12
Desc: 仅用于低版本（6.5以下）升级
      6.5版本对用户登录日志进行了扩展，故要删除旧版本所有用户登录日志表
 */

CREATE PROCEDURE p1 ()   
begin
declare tablelog  nvarchar(100);
declare now_year int;
declare now_month  int;
declare tab_name  nvarchar(100);
declare i int;
set i=0;
set tablelog='loginLog';

set @dt = now();

while i < 12 do 
set now_year = year(@dt);
set now_month =  month(@dt);

 IF (now_month > i)  THEN 
    set now_month =  now_month - i;
ELSE 
    set now_year = now_year - 1; 
    set now_month =  now_month - i + 12;
END IF; 

SET tab_name = CONCAT(tablelog,now_year,now_month);
set @sql=concat('DROP TABLE IF EXISTS`',tab_name,'`');
    PREPARE stmt FROM @sql;
    EXECUTE stmt ;
    DEALLOCATE PREPARE stmt;
set i = i+1;
end while;

end;
call p1 ();
drop procedure p1;



/**
Auth: leaf
Date: 2017-06-01
Desc: 去除cpdType 中 ct_license_type的唯一约束。
 */
ALTER TABLE cpdType
DROP INDEX ct_license_type;

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




/**
Auth: Archer
Date: 2017-06-14
Desc: 增加一个cpt/d二级功能并默认分配给系统管理员角色
 */
select @ftn_order := max(ftn_order)+1 from acFunction where ftn_level = 1;
select @ftn_id_parent := ftn_id from acFunction where ftn_ext_id = 'FTN_AMD_CPT_D_MGT';

INSERT acFunction (ftn_ext_id, ftn_level, ftn_type, ftn_creation_timestamp, ftn_tc_related, ftn_status, ftn_assign, ftn_order, ftn_parent_id)  
VALUES ('FTN_AMD_CPT_D_IMPORT_AWARDED_HOURS', '1', NULL, CURDATE(), 'N', '1', '1', @ftn_order, @ftn_id_parent);


--CPT/D 管理功能，导入用户获得时数子功能默认分配给“系统管理员”角色
INSERT acRoleFunction (rfn_rol_id, rfn_ftn_id, rfn_create_usr_id, RFN_CREATE_TIMESTAMP,rfn_ftn_parent_id, rfn_ftn_order) 
select rol_id, child.ftn_id, 's1u3', CURDATE(),parent.ftn_id, 1  from acRole, acFunction child, acFunction parent
where rol_ext_id like 'ADM_%' and child.ftn_ext_id = 'FTN_AMD_CPT_D_IMPORT_AWARDED_HOURS' and parent.ftn_ext_id = 'FTN_AMD_CPT_D_MGT';


/**
Auth: Archer
Date: 2017-06-14
Desc: 增加一个cpt/d导入用户获得CPT/D时数成功邮件
 */

insert into messageTemplate(mtp_type, mtp_subject, mtp_content, mtp_remark_label, mtp_web_message_ind, mtp_active_ind, mtp_tcr_id, mtp_update_timestamp, mtp_update_ent_id) values ('CPD_AWARDED_HOURS_IMPORT_SUCCESS','Import user awarded CPT/D hours Success Notification', 'Import user awarded CPT/D hours Success Notification','label_CPD_AWARDED_HOURS_IMPORT_SUCCESS',1,1,1,NOW(),3);

update messageTemplate set mtp_content = '<p>   <span style="color:black;font-family:宋体;font-size:11pt;">Dear </span><span style="color:blue;font-family:宋体;font-size:11pt;">[User name]</span><span style="color:black;font-family:宋体;font-size:11pt;">,<br />  <br />  This email is to notify you that the import of user awarded CPT/D hours file </span><span style="color:blue;font-family:宋体;font-size:11pt;">[Src file]</span><span style="color:black;font-family:宋体;font-size:11pt;">, which you uploaded at </span><span style="color:blue;font-family:宋体;font-size:11pt;">[Import start date]</span><span style="color:black;font-family:宋体;font-size:11pt;">, has been completed.<br />  <br />  Success Entries : </span><span style="color:blue;font-family:宋体;font-size:11pt;">[Success total]</span><span style="color:black;font-family:宋体;font-size:11pt;"><br />  Unsuccessful Entries : </span><span style="color:blue;font-family:宋体;font-size:11pt;">[Unsuccess total]<br />  </span><span style="color:black;font-family:宋体;font-size:11pt;"><br />  The import process was completed at </span><span style="color:blue;font-family:宋体;font-size:11pt;">[Import end date]. </span><span style="font-family:宋体;font-size:11pt;">You  may login to the system to view the log file(s) in the import history. <br />  Thank you for your attention.<br />  <br />  Regards,<br />  Training &amp; Development</span>  </p>' where mtp_type='CPD_AWARDED_HOURS_IMPORT_SUCCESS';

insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) select mtp_id,'[User name]','lab_msg_user_name' from messageTemplate where mtp_type='CPD_AWARDED_HOURS_IMPORT_SUCCESS';
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) select mtp_id,'[Src file]','lab_msg_src_file' from messageTemplate where mtp_type='CPD_AWARDED_HOURS_IMPORT_SUCCESS';
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) select mtp_id,'[Import start date]','lab_msg_import_start_date' from messageTemplate where mtp_type='CPD_AWARDED_HOURS_IMPORT_SUCCESS';
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) select mtp_id,'[Success total]','lab_msg_success_total' from messageTemplate where mtp_type='CPD_AWARDED_HOURS_IMPORT_SUCCESS';
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) select mtp_id,'[Unsuccess total]','lab_msg_unscuccess_total' from messageTemplate where mtp_type='CPD_AWARDED_HOURS_IMPORT_SUCCESS';
insert into messageParamName(mpn_mtp_id, mpn_name, mpn_name_desc) select mtp_id,'[Import end date]','lab_msgimport_end_date' from messageTemplate where mtp_type='CPD_AWARDED_HOURS_IMPORT_SUCCESS';



/**
Auth: Archer
Date: 2017-06-15
Desc: 导入注册记录：修改邮件字体（邮件的部分字体显示蓝色需改为黑色）; 导入用户获得CPT/D时数增加头部和尾部图片
 */
update messageTemplate set mtp_content = '<span style="color:#0000FF;line-height:28px;font-family:&quot;Microsoft YaHei UI&quot;, &quot;Microsoft YaHei&quot;, DengXian, SimSun, &quot;Segoe UI&quot;, Tahoma, Helvetica, sans-serif;font-size:14px;background-color:#FFFFFF;">[Header image]</span>  <p>   <span style="color:black;font-family:宋体;font-size:11pt;">Dear <span style="color:#0000FF;font-family:"Microsoft YaHei UI", "Microsoft YaHei", DengXian, SimSun, "Segoe UI", Tahoma, Helvetica, sans-serif;font-size:14px;line-height:28px;background-color:#FFFFFF;">[User name]</span></span><span style="color:blue;font-family:宋体;font-size:11pt;"></span><span style="color:black;font-family:宋体;font-size:11pt;">,<br />  <br />  This email is to notify you that the import of user profile    file </span><span style="color:blue;font-family:宋体;font-size:11pt;"><span style="color:#0000FF;font-family:"Microsoft YaHei UI", "Microsoft YaHei", DengXian, SimSun, "Segoe UI", Tahoma, Helvetica, sans-serif;font-size:14px;line-height:28px;background-color:#FFFFFF;">[Src file]</span></span><span style="color:black;font-family:宋体;font-size:11pt;">,    which you uploaded at </span><span style="color:blue;font-family:宋体;font-size:11pt;"><span style="color:#0000FF;font-family:"Microsoft YaHei UI", "Microsoft YaHei", DengXian, SimSun, "Segoe UI", Tahoma, Helvetica, sans-serif;font-size:14px;line-height:28px;background-color:#FFFFFF;">[Import start date]</span></span><span style="color:black;font-family:宋体;font-size:11pt;">, has    been completed.<br />  <br />  Success Entries : </span><span style="color:blue;font-family:宋体;font-size:11pt;"><span style="color:#0000FF;font-family:"Microsoft YaHei UI", "Microsoft YaHei", DengXian, SimSun, "Segoe UI", Tahoma, Helvetica, sans-serif;font-size:14px;line-height:28px;background-color:#FFFFFF;">[Success total]</span></span><span style="color:black;font-family:宋体;font-size:11pt;"><br />  Unsuccessful Entries : </span><span style="color:blue;font-family:宋体;font-size:11pt;"><span style="color:#0000FF;font-family:"Microsoft YaHei UI", "Microsoft YaHei", DengXian, SimSun, "Segoe UI", Tahoma, Helvetica, sans-serif;font-size:14px;line-height:28px;background-color:#FFFFFF;">[Unsuccess total]</span><br />  </span><span style="color:black;font-family:宋体;font-size:11pt;"><br />  The import process was completed at </span><span style="color:blue;font-family:宋体;font-size:11pt;"><span style="color:#0000FF;font-family:"Microsoft YaHei UI", "Microsoft YaHei", DengXian, SimSun, "Segoe UI", Tahoma, Helvetica, sans-serif;font-size:14px;line-height:28px;background-color:#FFFFFF;"><span style="color:#0000FF;font-family:"Microsoft YaHei UI", "Microsoft YaHei", DengXian, SimSun, "Segoe UI", Tahoma, Helvetica, sans-serif;font-size:14px;line-height:28px;background-color:#FFFFFF;">[Import end date]</span></span>. </span><span style="color:black;font-family:宋体;font-size:11pt;">You    may login to the system to view the log file(s) in the import history.  <br />  Regards,<br />  Training &amp; Development </span>   </p>    <span style="color:#0000FF;font-family:"Microsoft YaHei UI", "Microsoft YaHei", DengXian, SimSun, "Segoe UI", Tahoma, Helvetica, sans-serif;font-size:14px;line-height:28px;background-color:#FFFFFF;">[Footer image]</span>'
where mtp_type = 'CPD_REGISTRATION_IMPORT_SUCCESS';

update messageTemplate set mtp_content = '<span style="color:#0000FF;line-height:28px;font-family:&quot;Microsoft YaHei UI&quot;, &quot;Microsoft YaHei&quot;, DengXian, SimSun, &quot;Segoe UI&quot;, Tahoma, Helvetica, sans-serif;font-size:14px;background-color:#FFFFFF;">[Header image]</span>  <p>   <span style="color:black;font-family:宋体;font-size:11pt;">Dear </span><span style="color:blue;font-family:宋体;font-size:11pt;">[User name]</span><span style="color:black;font-family:宋体;font-size:11pt;">,<br />  <br />  This email is to notify you that the import of user awarded CPT/D hours file </span><span style="color:blue;font-family:宋体;font-size:11pt;">[Src file]</span><span style="color:black;font-family:宋体;font-size:11pt;">, which you uploaded at </span><span style="color:blue;font-family:宋体;font-size:11pt;">[Import start date]</span><span style="color:black;font-family:宋体;font-size:11pt;">, has been completed.<br />  <br />  Success Entries : </span><span style="color:blue;font-family:宋体;font-size:11pt;">[Success total]</span><span style="color:black;font-family:宋体;font-size:11pt;"><br />  Unsuccessful Entries : </span><span style="color:blue;font-family:宋体;font-size:11pt;">[Unsuccess total]<br />  </span><span style="color:black;font-family:宋体;font-size:11pt;"><br />  The import process was completed at </span><span style="color:blue;font-family:宋体;font-size:11pt;">[Import end date]. </span><span style="color:black;font-family:宋体;font-size:11pt;">You  may login to the system to view the log file(s) in the import history. <br />  Thank you for your attention.<br />  <br />  Regards,<br />  Training &amp; Development</span>  </p> <span style="color:#0000FF;font-family:"Microsoft YaHei UI", "Microsoft YaHei", DengXian, SimSun, "Segoe UI", Tahoma, Helvetica, sans-serif;font-size:14px;line-height:28px;background-color:#FFFFFF;">[Footer image]</span>' where mtp_type='CPD_AWARDED_HOURS_IMPORT_SUCCESS';



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



/**
Auth: leaf
Date: 2017-06-23
Desc: 去除cpdGroup 中 cg_code的唯一约束。
 */
ALTER TABLE cpdGroup
DROP INDEX cg_code;	  

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


/**
Auth: steven
Date: 2017-07-07（实际增加时间为2017-08-08，属于漏加后来补上的语句，与其他db_changes.sql统一）
Desc: 添加测试题，解析online video中api返回的资源link
 */
ALTER TABLE Resources ADD res_src_online_link varchar(255);


/**
Auth: Archer
Date: 2017-07-27（实际增加时间为2017-08-08，属于漏加后来补上的语句，与其他db_changes.sql统一）
Desc: 给"CPT/D Outstanding Hours – [License Alias]"邮件添加头部和尾部的默认图片
 */
update messageTemplate set mtp_header_img = '/msg/emailHeader.jpg' where mtp_type = 'CPTD_OUTSTANDING_LEARNER';

update messageTemplate set mtp_footer_img = '/msg/emailFooter.jpg' where mtp_type = 'CPTD_OUTSTANDING_LEARNER';


update messageTemplate set mtp_header_img = '/msg/emailHeader.jpg' where mtp_type = 'CPTD_OUTSTANDING_SUPERVISOR';

update messageTemplate set mtp_footer_img = '/msg/emailFooter.jpg' where mtp_type = 'CPTD_OUTSTANDING_SUPERVISOR';


/**
Auth: steven
Date: 2017-07-28（实际增加时间为2017-08-08，属于漏加后来补上的语句，与其他db_changes.sql统一）
Desc: 更新忘记密码邮件模板
 */
UPDATE messageTemplate SET mtp_content = ' [Header image] Dear Sir/Madam: <br /> <br /> We have received your reset password request on [Request time].<br/><br/>Please click below link to reset your password:<br/><br/>[Link to reset password]<br/><br/>The above link will be expired after [Max days] day(s) so please proceed to reset your password as soon as possible.<br/><br/>Regards,<br />Training &amp; Development</p><span></span> <p> <span>[Footer image]</span> </p><p> <br /></p>'
	WHERE mtp_type = 'USER_PWD_RESET_NOTIFY';

/**
 * Auth: Jacky
 * Date: 2017-08-11
 * Desc: 更新邮件模板subject为英文 
 */

UPDATE messageTemplate SET mtp_subject = 'Course Enrollment Request Received'
WHERE mtp_id = 1

UPDATE messageTemplate SET mtp_subject = 'Course Enrollment Confirmed'
WHERE mtp_id = 2

UPDATE messageTemplate SET mtp_subject = 'Course Enrollment Waiting List'
WHERE mtp_id = 3

UPDATE messageTemplate SET mtp_subject = 'Course Enrollment Request Declined'
WHERE mtp_id = 4

UPDATE messageTemplate SET mtp_subject = 'Course Enrollment Request Cancelled'
WHERE mtp_id = 5

UPDATE messageTemplate SET mtp_subject = 'Course Enrollment Request Cancelled'
WHERE mtp_id = 6

UPDATE messageTemplate SET mtp_subject = 'Course Enrollment Approval'
WHERE mtp_id = 7

UPDATE messageTemplate SET mtp_subject = 'Course Enrollment Notification'
WHERE mtp_id = 8

UPDATE messageTemplate SET mtp_subject = 'Course Enrollment Notification'
WHERE mtp_id = 9

UPDATE messageTemplate SET mtp_subject = 'Course Enrollment Notification'
WHERE mtp_id = 10

UPDATE messageTemplate SET mtp_subject = 'Course Enrollment Notification'
WHERE mtp_id = 11

UPDATE messageTemplate SET mtp_subject = 'Course Enrollment Approval'
WHERE mtp_id = 12

UPDATE messageTemplate SET mtp_subject = 'Course Completion Reminder'
WHERE mtp_id = 13

UPDATE messageTemplate SET mtp_subject = 'Joining Instruction'
WHERE mtp_id = 14

UPDATE messageTemplate SET mtp_subject = 'Reminder'
WHERE mtp_id = 15

UPDATE messageTemplate SET mtp_subject = 'USR_REG_APPROVE'
WHERE mtp_id = 16

UPDATE messageTemplate SET mtp_subject = 'USR_REG_DISAPPROVE'
WHERE mtp_id = 17

UPDATE messageTemplate SET mtp_subject = 'User Import Success Notification'
WHERE mtp_id = 18

UPDATE messageTemplate SET mtp_subject = 'Enrollment Import Success Notification'
WHERE mtp_id = 19

UPDATE messageTemplate SET mtp_subject = 'System Performance Notify'
WHERE mtp_id = 20

UPDATE messageTemplate SET mtp_subject = 'empty subject'
WHERE mtp_id = 21

UPDATE messageTemplate SET mtp_subject = 'Reset Password Notify'
WHERE mtp_id = 22

UPDATE messageTemplate SET mtp_subject = '来自[User name]的站内信'
WHERE mtp_id = 23

UPDATE messageTemplate SET mtp_subject = 'CPT/D Outstanding Hours – [License Alias]'
WHERE mtp_id = 24

UPDATE messageTemplate SET mtp_subject = 'CPT/D Outstanding Hours – [License Alias]'
WHERE mtp_id = 25

	
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
update ReportTemplate set rte_owner_ent_id = 1 where rte_type = 'LEARNER';

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


/**         
Date: 2017-11-9
Desc: 培训积分清空
*/
 INSERT INTO creditsType 
 (cty_code,cty_title,cty_deduction_ind,cty_manual_ind,
 cty_deleted_ind,cty_relation_total_ind,cty_relation_type,
 cty_default_credits_ind,cty_default_credits,cty_create_usr_id,
 cty_create_timestamp,cty_update_usr_id,cty_update_timestamp,CTY_TCR_ID)
VALUES('ITM_INTEGRAL_EMPTY','ITM_INTEGRAL_EMPTY',1,1,0,1,'COS',0,0,'s1u3',sysdate(),'s1u3',sysdate(),1);

/**
Auth: steven 
Date: 2017-11-21
Desc: mobile端登录页面logo重置为默认图片
*/
update SitePoster set sp_logo_file_cn = 'logo3.png' where sp_ste_id = 1 and SP_MOBILE_IND = 1

/**
(补充此SQL到DB_CHANGE)
Auth: Jacky
Date: 2017-11-17
Desc: 屏蔽学习地图功能
 */
update acFunction set ftn_status = '0' where ftn_ext_id = 'FTN_AMD_STUDY_MAP_MGT';

/**
(补充此SQL到DB_CHANGE)
Auth: Jacky
Date: 2017-11-17
Desc: 屏蔽 live management
 */
update acFunction set ftn_status = '0' where ftn_ext_id = 'FTN_AMD_LIVE_MAIN';

/**
Auth: Jacky
Date: 2017-11-17
Desc: 屏蔽 vendor management
 */
update acFunction set ftn_status = '0' where ftn_ext_id = 'FTN_AMD_SUPPLIER_MAIN';

/**
Auth: Albert
Date: 2017-11-17
Desc: 屏蔽 点赞，被点赞和评论的的得分项
 */
update creditsType set cty_deleted_ind = 0 where cty_id=34;
update creditsType set cty_deleted_ind = 0 where cty_id=35;
update creditsType set cty_deleted_ind = 0 where cty_id=36;

/**
Auth: Jasper
Date: 2017-12-18
Desc: 修改aeItemExtension中的ies_credit字段类型
 */
alter table aeItemExtension  modify column ies_credit decimal(8,2);
go
 
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
VALUES('INTEGRAL_EMPTY','INTEGRAL_EMPTY',1,1,0,1,'',0,0,'s1u3',sysdate(),'s1u3',sysdate(),1);

/*
Auth: Hajar
Date: 2018-02-09
Desc: 修改 题目分数数据结构
 */
alter table Question modify column que_score int(5);
alter table RawQuestion modify column raq_score int(5);

/**
Auth: Jaren
Date: 2018-04-02
Desc: 更新导入报名记录已完成模板
 */
update messageTemplate set mtp_content = '[Header image] <p>  <span> </span><span   style="color: black; font-family: 宋体; font-size: 11pt;">Dear </span><span   style="color: blue; font-family: 宋体; font-size: 11pt;">[User name]</span><span style="color: black; font-family: 宋体; font-size: 11pt;">,<br />   <br /> This email is to notify you that the import of enrollment record  </span><span style="color: blue; font-family: 宋体; font-size: 11pt;">[Src file]</span><span style="color: black; font-family: 宋体; font-size: 11pt;">,   which you uploaded at </span><span   style="color: blue; font-family: 宋体; font-size: 11pt;">[Import start date]</span><span   style="color: black; font-family: 宋体; font-size: 11pt;">, has   been completed.<br /> <br /> Success Entries :  </span><span style="color: blue; font-family: 宋体; font-size: 11pt;">[Success total]</span><span style="color: black; font-family: 宋体; font-size: 11pt;"><br />   Unsuccessful Entries : </span><span   style="color: blue; font-family: 宋体; font-size: 11pt;">[Unsuccess total]<br />  </span><span style="color: black; font-family: 宋体; font-size: 11pt;"><br />   The import process was completed at </span><span   style="color: blue; font-family: 宋体; font-size: 11pt;">[Import end date]. </span><span style="font-family: 宋体; font-size: 11pt;">You   may login to the system to view the log file(s) in the import history.   <br /> Thank you for your attention.<br /> <br /> Regards,<br />   Training &amp; Development  </span> </p> <span> </span>  [Footer image] '
where mtp_type = 'ENROLLMENT_IMPORT_SUCCESS' and mtp_tcr_id = 1;
commit;

/**
Auth: Steven
Date: 2018-3-13
Desc: 表emailMessage新增抄送电邮地址字段
 */
alter table emailMessage add emsg_cc_email nvarchar(2000) NULL;

/**
Auth: Jaren
Date: 2018-05-07
Desc: 面授課程繁體 label 問題20180424
 */
update aeTemplateView set
tvw_xml='<template_view>	<section id="1">		<title>			<desc lan="ISO-8859-1" name="General information"/>			<desc lan="Big5" name="基本資訊"/>			<desc lan="GB2312" name="基本信息"/>		</title>		<itm_type type="itm_type" paramname="itm_type">			<title>				<desc lan="ISO-8859-1" name="Type"/>				<desc lan="Big5" name="類型"/>				<desc lan="GB2312" name="类型"/>			</title>		</itm_type>		<field51>			<title>				<desc lan="ISO-8859-1" name="Code"/>				<desc lan="Big5" name="編號"/>				<desc lan="GB2312" name="编号"/>			</title>		</field51>		<field52>			<title>				<desc lan="ISO-8859-1" name="Title"/>				<desc lan="Big5" name="名稱"/>				<desc lan="GB2312" name="名称"/>			</title>		</field52>		<training_center type="tcr_pickup" paramname="itm_tcr_id" required="yes" external_field="yes">			<title>				<desc lan="ISO-8859-1" name="Training center"/>				<desc lan="Big5" name="培訓中心"/>				<desc lan="GB2312" name="培训中心"/>			</title>		</training_center>		<field53>			<title>				<desc lan="ISO-8859-1" name="Class period"/>				<desc lan="Big5" name="面授期間"/>				<desc lan="GB2312" name="面授期间"/>			</title>			<subfield_list>				<subfield id="1">					<title>						<desc lan="ISO-8859-1" name="From"/>						<desc lan="Big5" name="由"/>						<desc lan="GB2312" name="由"/>					</title>				</subfield>				<subfield id="2">					<title>						<desc lan="ISO-8859-1" name="To"/>						<desc lan="Big5" name="至"/>						<desc lan="GB2312" name="至"/>					</title>				</subfield>			</subfield_list>		</field53>			<!--		<field61 blend_ind="true">			<title>				<desc lan="ISO-8859-1" name="Online content period"/>				<desc lan="Big5" name="網上內容期限"/>				<desc lan="GB2312" name="网上内容期限"/>			</title>			<subfield_list>				<subfield id="1">					<title>						<desc lan="ISO-8859-1" name="From"/>						<desc lan="Big5" name="由"/>						<desc lan="GB2312" name="由"/>					</title>				</subfield>				<subfield id="2">					<title>						<desc lan="ISO-8859-1" name="To"/>						<desc lan="Big5" name="至"/>						<desc lan="GB2312" name="至"/>					</title>				</subfield>			</subfield_list>		</field61>		-->		<field55>			<title>				<desc lan="ISO-8859-1" name="Enrollment period"/>				<desc lan="Big5" name="報名期限"/>				<desc lan="GB2312" name="报名期限"/>			</title>			<subfield_list>				<subfield id="1">					<title>						<desc lan="ISO-8859-1" name="From"/>						<desc lan="Big5" name="由"/>						<desc lan="GB2312" name="由"/>					</title>				</subfield>				<subfield id="2">					<title>						<desc lan="ISO-8859-1" name="To"/>						<desc lan="Big5" name="至"/>						<desc lan="GB2312" name="至"/>					</title>				</subfield>			</subfield_list>		</field55>		<field06>			<title>				<desc lan="ISO-8859-1" name="Description"/>				<desc lan="Big5" name="簡介"/>				<desc lan="GB2312" name="简介"/>			</title>		</field06>		<!--		<rsv_main_room type="constant" marked="no" required="no" external_field="yes">			<title>				<desc lan="ISO-8859-1" name="Main room"/>				<desc lan="Big5" name="主房間"/>				<desc lan="GB2312" name="主房间"/>			</title>		</rsv_main_room>		<rsv_venue type="constant" marked="no" required="no" external_field="yes">			<title>				<desc lan="ISO-8859-1" name="Venue"/>				<desc lan="Big5" name="地點"/>				<desc lan="GB2312" name="地点"/>			</title>		</rsv_venue>		-->	</section>	<section id="2">		<title>			<desc lan="ISO-8859-1" name="Administrative information"/>			<desc lan="Big5" name="管理資訊"/>			<desc lan="GB2312" name="管理信息"/>		</title>		<field54>			<title>				<desc lan="ISO-8859-1" name="Address"/>				<desc lan="Big5" name="地址"/>				<desc lan="GB2312" name="地址"/>			</title>		</field54>		<field59>			<title>				<desc lan="ISO-8859-1" name="Fee"/>				<desc lan="Big5" name="報名費"/>				<desc lan="GB2312" name="报名费"/>			</title>		</field59>		<field114>			<title>				<desc lan="ISO-8859-1" name="Credit"/>				<desc lan="Big5" name="學分"/>				<desc lan="GB2312" name="学分"/>			</title>		</field114>		<field56>			<title>				<desc lan="ISO-8859-1" name="Remarks"/>				<desc lan="Big5" name="備註"/>				<desc lan="GB2312" name="备注"/>			</title>		</field56>		<item_access type="item_access_pickup" paramname="iac_id_lst" id="TADM" dependant="training_center" arrayparam="true">			<title>				<desc lan="ISO-8859-1" name="Training administrator"/>				<desc lan="Big5" name="培訓管理員"/>				<desc lan="GB2312" name="培训管理员"/>			</title>		</item_access>		<field160>			<title>				<desc lan="ISO-8859-1" name="Lecturer type"/>				<desc lan="Big5" name="講師類型"/>				<desc lan="GB2312" name="讲师类型"/>			</title>			<field160 id="1">				<title>					<desc lan="ISO-8859-1" name="Internal lecturer"/>					<desc lan="Big5" name="内部講師"/>					<desc lan="GB2312" name="内部讲师"/>				</title>			</field160>			<field160 id="2">				<title>					<desc lan="ISO-8859-1" name="External lecturer"/>					<desc lan="Big5" name="外部講師"/>					<desc lan="GB2312" name="外部讲师"/>				</title>			</field160>		</field160>		<item_access type="item_access_pickup" paramname="iac_id_lst" id="INSTR" arrayparam="true" dependant="field160" >		</item_access>		<item_access exam_ind="true" blend_ind="true" type="item_access_pickup" paramname="iac_id_lst" id="EXA" arrayparam="true" dependant="training_center"/>		<item_status type="radio" value="OFF" paramname="itm_status" required="yes">			<title>				<desc lan="ISO-8859-1" name="Publish to catalog"/>				<desc lan="Big5" name="在目錄中發佈"/>				<desc lan="GB2312" name="在目录中发布"/>			</title>			<item_status id="1" value="ALL">				<title>					<desc lan="ISO-8859-1" name="Yes"/>					<desc lan="Big5" name="是"/>					<desc lan="GB2312" name="是"/>				</title>			</item_status>			<item_status id="2" value="OFF">				<title>					<desc lan="ISO-8859-1" name="No"/>					<desc lan="Big5" name="否"/>					<desc lan="GB2312" name="否"/>				</title>			</item_status>		</item_status>		<field57>			<title>				<desc lan="ISO-8859-1" name="Quota"/>				<desc lan="Big5" name="名額限制"/>				<desc lan="GB2312" name="名额限制"/>			</title>		</field57>		<field21>			<title>				<desc lan="ISO-8859-1" name="Send enrollment notification email"/>				<desc lan="Big5" name="發送報名通知郵件"/>				<desc lan="GB2312" name="发送报名通知邮件"/>			</title>			<field21 id="1">				<title>					<desc lan="ISO-8859-1" name="Yes, send to learner only"/>					<desc lan="Big5" name="是，只發送給學員"/>					<desc lan="GB2312" name="是，只发送给学员"/>				</title>			</field21>			<field21 id="2">				<title>					<desc lan="ISO-8859-1" name="Yes, send to learner and Direct Supervisor(s)"/>					<desc lan="Big5" name="是，發送給學員及其所有直屬上司"/>					<desc lan="GB2312" name="是，发送给学员及其所有直属上司"/>				</title>			</field21>			<field21 id="3">				<title>					<desc lan="ISO-8859-1" name="No"/>					<desc lan="Big5" name="否"/>					<desc lan="GB2312" name="否"/>				</title>			</field21>		</field21>		<!--		<field111 dependant="training_center">			<title>				<desc lan="ISO-8859-1" name="Certificate Of Completion"/>				<desc lan="Big5" name="證書"/>				<desc lan="GB2312" name="证书"/>			</title>			<field111 id="1">				<title>					<desc lan="ISO-8859-1" name="Disable"/>					<desc lan="Big5" name="不適用"/>					<desc lan="GB2312" name="不适用"/>				</title>			</field111>			<field111 id="2">				<title>					<desc lan="ISO-8859-1" name="Enable, use the certificate:"/>					<desc lan="Big5" name="使用該證書:"/>					<desc lan="GB2312" name="使用该证书:"/>				</title>			</field111>		</field111>		-->		<!-- <field58>			<title>				<desc lan="ISO-8859-1" name="Enrollment confirmation remarks"/>				<desc lan="Big5" name="確認報名備註"/>				<desc lan="GB2312" name="确认报名备注"/>			</title>		</field58> -->		<field23 type="notify_email_limited" external_field="yes" paramname="itm_notify_days">			<title>				<desc lan="ISO-8859-1" name="Send course end date notification email"/>				<desc lan="Big5" name="發送結束提醒郵件"/>				<desc lan="GB2312" name="发送结束提醒邮件"/>			</title>		</field23>		<field24 type="notify_support_email" external_field="yes" paramname="itm_notify_email">			<title>				<desc lan="ISO-8859-1" name="Support email"/>				<desc lan="Big5" name="支持郵件"/>				<desc lan="GB2312" name="支持邮件"/>			</title>		</field24>		<!--		<targeted_lrn type="targeted_lrn_pickup" paramname="target_ent_group_lst" required="yes" label="lab_all_learners">			<title>				<desc lan="ISO-8859-1" name="Target enrollments"/>				<desc lan="Big5" name="可報名學員"/>				<desc lan="GB2312" name="可报名学员"/>			</title>		</targeted_lrn>		-->		<!--		<field60>			<title>				<desc lan="ISO-8859-1" name="Course related materials"/>				<desc lan="Big5" name="相關資料"/>				<desc lan="GB2312" name="相关资料"/>			</title>			<subfield_list>				<subfield id="1">					<title>						<desc lan="ISO-8859-1" name="File 1"/>						<desc lan="Big5" name="文件1"/>						<desc lan="GB2312" name="文件1"/>					</title>				</subfield>				<subfield id="2">					<title>						<desc lan="ISO-8859-1" name="File 2"/>						<desc lan="Big5" name="文件2"/>						<desc lan="GB2312" name="文件2"/>					</title>				</subfield>				<subfield id="3">					<title>						<desc lan="ISO-8859-1" name="File 3"/>						<desc lan="Big5" name="文件3"/>						<desc lan="GB2312" name="文件3"/>					</title>				</subfield>				<subfield id="4">					<title>						<desc lan="ISO-8859-1" name="File 4"/>						<desc lan="Big5" name="文件4"/>						<desc lan="GB2312" name="文件4"/>					</title>				</subfield>				<subfield id="5">					<title>						<desc lan="ISO-8859-1" name="File 5"/>						<desc lan="Big5" name="文件5"/>						<desc lan="GB2312" name="文件5"/>					</title>				</subfield>				<subfield id="6">					<title>						<desc lan="ISO-8859-1" name="File 6"/>						<desc lan="Big5" name="文件6"/>						<desc lan="GB2312" name="文件6"/>					</title>				</subfield>				<subfield id="7">					<title>						<desc lan="ISO-8859-1" name="File 7"/>						<desc lan="Big5" name="文件7"/>						<desc lan="GB2312" name="文件7"/>					</title>				</subfield>				<subfield id="8">					<title>						<desc lan="ISO-8859-1" name="File 8"/>						<desc lan="Big5" name="文件8"/>						<desc lan="GB2312" name="文件8"/>					</title>				</subfield>				<subfield id="9">					<title>						<desc lan="ISO-8859-1" name="File 9"/>						<desc lan="Big5" name="文件9"/>						<desc lan="GB2312" name="文件9"/>					</title>				</subfield>				<subfield id="10">					<title>						<desc lan="ISO-8859-1" name="File 10"/>						<desc lan="Big5" name="文件10"/>						<desc lan="GB2312" name="文件10"/>					</title>				</subfield>			</subfield_list>		</field60>	-->		<field22>			<title>				<desc lan="ISO-8859-1" name="Completion settle-date extension"/>				<desc lan="Big5" name="延長結訓結算時間"/>				<desc lan="GB2312" name="延长结训结算时间"/>			</title>			<suffix>				<desc lan="ISO-8859-1" name="days after class end date"/>				<desc lan="Big5" name="天(從結束日期算起)"/>				<desc lan="GB2312" name="天(从结束日期算起)"/>			</suffix>		</field22>		<field25>			<title>				<desc lan="ISO-8859-1" name="Activate Point Calculation"/>				<desc lan="Big5" name="自動積分"/>				<desc lan="GB2312" name="自动积分"/>			</title>			<field25 id="1">				<title>					<desc lan="ISO-8859-1" name="Yes"/>					<desc lan="Big5" name="是"/>					<desc lan="GB2312" name="是"/>				</title>			</field25>			<field25 id="2">				<title>					<desc lan="ISO-8859-1" name="No"/>					<desc lan="Big5" name="否"/>					<desc lan="GB2312" name="否"/>				</title>			</field25>		</field25>		<field26>			<title>				<desc lan="ISO-8859-1" name="Difficulty factor"/>				<desc lan="Big5" name="難度係數"/>				<desc lan="GB2312" name="难度系数"/>			</title>		</field26>		<!--  <field98>			<title>				<desc lan="ISO-8859-1" name="Plan code"/>				<desc lan="Big5" name="計劃編號"/>				<desc lan="GB2312" name="计划编号"/>			</title>		</field98>  -->				<create_date type="read_datetime" external_field="yes">			<title>				<desc lan="ISO-8859-1" name="Created"/>				<desc lan="Big5" name="創建日期"/>				<desc lan="GB2312" name="创建日期"/>			</title>		</create_date>		<created_by type="read" external_field="yes">			<title>				<desc lan="ISO-8859-1" name="Created by"/>				<desc lan="Big5" name="創建者"/>				<desc lan="GB2312" name="创建者"/>			</title>		</created_by>		<update_date type="read_datetime" external_field="yes">			<title>				<desc lan="ISO-8859-1" name="Modifed"/>				<desc lan="Big5" name="修改日期"/>				<desc lan="GB2312" name="修改日期"/>			</title>		</update_date>		<updated_by type="read" external_field="yes">			<title>				<desc lan="ISO-8859-1" name="Modified by"/>				<desc lan="Big5" name="修改者"/>				<desc lan="GB2312" name="修改者"/>			</title>		</updated_by>	</section>	<hidden>		<field150/>		<!--		<field151/>		<field152/>		<field153/>		<field154/>		-->		<field155/>	</hidden></template_view>'
where tvw_tpl_id='13' and tvw_id='DETAIL_VIEW'

commit

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


