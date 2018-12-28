package com.cw.wizbank.dataMigrate.imp;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.dataMigrate.imp.bean.CPDRegistrationBean;
import com.cw.wizbank.dataMigrate.imp.bean.ImportObject;
import com.cw.wizbank.qdb.dbCPDType;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.util.cwException;
import com.cwn.wizbank.cpd.service.CpdRegistrationMgtService;
import com.cwn.wizbank.cpd.service.CpdUtilService;
import com.cwn.wizbank.cpd.utils.CpdUtils;
import com.cwn.wizbank.cpd.vo.CpdPeriodVO;
import com.cwn.wizbank.entity.CpdGroup;
import com.cwn.wizbank.entity.CpdGroupPeriod;
import com.cwn.wizbank.entity.CpdGroupRegistration;
import com.cwn.wizbank.entity.CpdRegistration;
import com.cwn.wizbank.entity.CpdType;
import com.cwn.wizbank.entity.ObjectActionLog;
import com.cwn.wizbank.entity.RegUser;
import com.cwn.wizbank.persistence.CpdGroupRegistrationMapper;
import com.cwn.wizbank.persistence.CpdRegistrationMapper;
import com.cwn.wizbank.persistence.CpdTypeMapper;
import com.cwn.wizbank.persistence.RegUserMapper;
import com.cwn.wizbank.services.RegUserService;
import com.cwn.wizbank.systemLog.SystemLogContext;
import com.cwn.wizbank.systemLog.SystemLogTypeEnum;
import com.cwn.wizbank.utils.CommonLog;
import com.cwn.wizbank.utils.DateUtil;
import com.cwn.wizbank.web.WzbApplicationContext;
public class ImpCPDRegistration extends Imp {

	public static final String UGR_LEVEL_1_CODE_PARTTIME = "PARTTIME";
	public static final String UGR_LEVEL_1_TITLE_PARTTIME = "Part Time";
	public static final String UGR_LEVEL_1_CODE_FULLTIME = "FULLTIME";
	public static final String UGR_LEVEL_1_TITLE_FULLTIME = "Full Time";
	public static String log_properties;
	public static int usg_level_total = 1;
	public static int ugr_level_total = 1;
	
	public static String input_file_name = "lms_cpd_intranet.txt";

	public ImpCPDRegistration(Connection conn, WizbiniLoader wizbiniLoader, String logDir, String file_path) {
		con = conn;
		wizbini = wizbiniLoader;
		warning_p = 1000;
		log_dir = logDir;
		input_file = file_path;

		this.int_file_pre = "fsm_agent";

		this.log_file_name = "import_cpd_";

		log = Logger.getLogger("LOG");
		logSuccess = Logger.getLogger("SUCCESS_LOG");
		logFailure=Logger.getLogger("FAILURE_LOG");
		fieldLabel = new String[]{"label_core_cpt_d_management_222","label_core_cpt_d_management_223","label_core_cpt_d_management_224", "label_core_cpt_d_management_225", 
				"label_core_cpt_d_management_226", "label_core_cpt_d_management_227", "label_core_cpt_d_management_228", "label_core_cpt_d_management_229"}; 
		isCPDRestrationImport = true;
		data_long = 8;
        allowEmpty = new int[]{0, 0, 0, 0, 1, 0, 0, 1};//  
                           //  1  2  3  4  5  6  7  8
        
        //allowEmpty = new int[]{0, 1, 1, 1, 1, 1, 1, 1, 1, 1,  1,  1,  1,  1,  1,  1, 1,  1,  1,  1,  1,  1,  1,  1,  1 , 1,  1,  1,  1,  1,  1,  1};//  
        colMaxLen = new int[]{20, 20, 80, 10, 10, 20, 10,10};
                           //  1  2     3   4   5    6    7   8
        isDate = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
                       //  1  2  3  4  5  6  7  8 
        isEmail = new int[]{0, 0, 0, 1, 1, 0, 1, 1};
                        //  1  2  3  4  5  6  7  8
        isNumber = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
	}
	public void finalCheck(Vector userVec, ImportStatus importUserStatus, boolean clear) throws Exception {
		
	}
    CpdRegistrationMgtService cpdRegistrationMgtService = (CpdRegistrationMgtService) WzbApplicationContext.getBean("cpdRegistrationMgtService");
    CpdUtilService cpdUtilService = (CpdUtilService) WzbApplicationContext.getBean("cpdUtilService");
    RegUserService regUserService = (RegUserService) WzbApplicationContext.getBean("regUserService");
    CpdGroupRegistrationMapper cpdGroupRegistrationMapper = (CpdGroupRegistrationMapper) WzbApplicationContext.getBean("cpdGroupRegistrationMapper");
    CpdRegistrationMapper cpdRegistrationMapper = (CpdRegistrationMapper) WzbApplicationContext.getBean("cpdRegistrationMapper");
    RegUserMapper regUserMapper = (RegUserMapper) WzbApplicationContext.getBean("regUserMapper");
    CpdTypeMapper cpdTypeMapper = (CpdTypeMapper) WzbApplicationContext.getBean("cpdTypeMapper");
    
	public void importData(Vector userVec, loginProfile prof, ImportStatus importStatus,boolean clear) throws Exception {
		
		// 判断是添加还是修改注册记录
		for(int h=0;h<userVec.size();h++){
			CPDRegistrationBean crb = (CPDRegistrationBean) userVec.get(h);
			// 如果用户大牌有注册信息就修改
			// 如果用户大牌没有注册信息就添加
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("usr_ste_usr_id", crb.user_id);
			map.put("License_type", crb.License_type);
            map.put("reg_date", crb.Reg_date);
			CpdRegistration cm = cpdGroupRegistrationMapper.getGroupRegistration(map);
			boolean is_add = true;
			if(cm != null){
				is_add = false;
			}
			importStatus.cnt_success++;
			RegUser regUser = regUserService.getUserDetailByUserSteUsrId(crb.user_id);
			CpdRegistration cpdRegistration = new CpdRegistration();
			convertData(crb,cpdRegistration);
			cpdRegistration.setCr_usr_ent_id(regUser.getUsr_ent_id());
			cpdRegistration.setCr_ct_id(cpdRegistrationMapper.getCpdRegistrationByType(crb.License_type));
			Map<String, Object> cr_map = new HashMap<String, Object>();
			cr_map.put("group_code", crb.CPD_group_code);
			cr_map.put("type_code", crb.License_type);
			cpdRegistration.setCr_cg_id(cpdRegistrationMapper.getCpdGroupRegistrationByType(cr_map));
			cpdRegistration.setCr_reg_number(crb.Reg_no);
			if(cm != null){
				cpdRegistration.setCr_id(cm.getCr_id());
			}
			if(is_add){
				// 添加数据
				logSuccess("User ID "+crb.user_id+" License type "+crb.License_type+" CPT/D group "+crb.CPD_group_code+" add success ");
				addData(cpdRegistration,prof,importStatus,clear);
			}else{
				// 修改数据
				logSuccess("User ID "+crb.user_id+" License type "+crb.License_type+" CPT/D group "+crb.CPD_group_code+" update success ");
				updateData(cpdRegistration,prof,importStatus,clear);
			}
		}
	}
	
	private void convertData(CPDRegistrationBean crb,
			CpdRegistration cr) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		cr.setCr_reg_number(crb.Reg_no);
		if(crb.Reg_date != null){
			cr.cr_reg_datetime = sdf.parse(crb.Reg_date);
		}
		if(crb.De_reg_date != null && crb.De_reg_date.length()>0){
			cr.setCr_de_reg_datetime(sdf.parse(crb.De_reg_date));
		}
		cr.CPD_group_code = crb.CPD_group_code;
		cr.license_type = crb.License_type;
		cr.setCgr_initial_datetime(sdf.parse(crb.Initial_date));
		if(crb.Expiry_date != null && crb.Expiry_date.length()>0){
			cr.setCgr_expiry_datetime(sdf.parse(crb.Expiry_date));
		}
	}

	private void addData(CpdRegistration cpdRegistration, loginProfile prof,
			ImportStatus importStatus, boolean clear) {
		// save
		save(cpdRegistration, prof);
        // saveCpdGroupRegi
        saveCpdGroupRegi(cpdRegistration,prof);
	}
	private void updateData(CpdRegistration cpdRegistration, loginProfile prof,
			ImportStatus importStatus, boolean clear) {
		// updCpdRegistration
        updCpdRegistration(cpdRegistration,prof);
         
         // updCpdGroupRegistration
        updCpdGroupRegistration(cpdRegistration,prof);
	}

	private void updCpdGroupRegistration(CpdRegistration cr, loginProfile prof) {
		long cgr_cr_id =  0;
		 long cgr_cg_id =  0;
		 List<CpdGroupRegistration> cpdGroupRegistrationList = null;//查询大牌下的小牌注册记录
		 List<CpdGroupRegistration> cpdGroupRegistrations = new ArrayList<CpdGroupRegistration>();
		 
	    CpdGroupRegistration cgr = new CpdGroupRegistration();
	    Map<String, String> map = new HashMap<String, String>();
		map.put("CPD_group_code", cr.CPD_group_code);
		map.put("License_type", cr.license_type);
		Long cg_id = cpdGroupRegistrationMapper.getGroupId(map);
		cgr.setCgr_cg_id(cg_id);
		cgr.setCgr_cr_id(cr.getCr_id());
		cgr.setCgr_usr_ent_id(cr.getCr_usr_ent_id());
		cgr.setCgr_initial_date(cr.getCgr_initial_datetime());
		cgr.setCgr_expiry_date(cr.getCgr_expiry_datetime());
		cpdGroupRegistrations.add(cgr); 
		
		 if(null!=cpdGroupRegistrations && cpdGroupRegistrations.size()>0){
		     cgr_cr_id = cpdGroupRegistrations.get(0).getCgr_cr_id();
		     cpdGroupRegistrationList =  cpdRegistrationMgtService.getGroupRegiByCrId(0,cgr_cr_id);
		     for (int i = 0; i < cpdGroupRegistrations.size(); i++) {
		         List<CpdGroupRegistration> cpdGroupRegistrationList1 = null;//查询大牌下的小牌注册记录（根据小牌ID）
		         cgr_cg_id = cpdGroupRegistrations.get(i).getCgr_cg_id();
		         cpdGroupRegistrationList1=  cpdRegistrationMgtService.getGroupRegiByCrId(cgr_cg_id,cgr_cr_id);
		         if(cpdGroupRegistrationList1.size()==0){//该小牌没注册
		             //增加小牌注册记录
		             int cgr_first_ind = 0;
		             CpdGroupRegistration cpdGroupRegistration = cpdGroupRegistrations.get(i);
		             if(cpdUtilService.cpdIsFirstInd(cpdGroupRegistration.getCgr_cg_id(), cpdGroupRegistration.getCgr_usr_ent_id(), cpdGroupRegistration.getCgr_initial_date())){
		                 cgr_first_ind = 1;//是首次挂牌
		             }
		             //大牌注册信息
		             CpdRegistration cpdRegistration = new CpdRegistration();    
		             cpdRegistration = cpdRegistrationMgtService.getDetail(cpdGroupRegistration.getCgr_cr_id());
		             //大牌信息
		             CpdType cpdType = new CpdType();
		             cpdType = cpdRegistrationMgtService.getCpdTypeByid(cpdRegistration.getCr_ct_id());
		             //小牌信息
		             CpdGroup  cpdGroup = new CpdGroup();
		             cpdGroup = cpdRegistrationMgtService.getCpdGroupById(cpdGroupRegistration.getCgr_cg_id());
		             
		             CpdGroupPeriod req_period = cpdUtilService.getReqHoursList(cpdGroup.getCg_id());
		             
		             
		             //上一个周期
		             CpdGroupRegistration preCpdGroupRegistration =  cpdUtilService.getPreGroupRegistration(cpdGroupRegistration.getCgr_cg_id(), cpdGroupRegistration.getCgr_usr_ent_id(), cpdGroupRegistration.getCgr_initial_date());
		             //实际开始时间
		             cpdGroupRegistration.setCgr_actual_date(cpdUtilService.getCgrActualDate(cpdGroupRegistration, preCpdGroupRegistration, cpdType));
		             
		             cpdGroupRegistration.setCgr_first_ind(cgr_first_ind);
		             cpdGroupRegistration.setCgr_create_usr_ent_id(prof.getUsr_ent_id());
		             cpdGroupRegistration.setCgr_create_datetime(cpdRegistrationMgtService.getDate());
		             cpdGroupRegistration.setCgr_update_usr_ent_id(prof.getUsr_ent_id());
		             cpdGroupRegistration.setCgr_update_datetime(cpdRegistrationMgtService.getDate());
		             cpdGroupRegistration.setCgr_status("OK");     
		             
		           //获取周期信息
		             CpdPeriodVO prePeriod = null;
		             if(preCpdGroupRegistration!=null){
		                 prePeriod = cpdUtilService.getPeriod(preCpdGroupRegistration.getCgr_initial_date(), cpdType.getCt_id());
		             }
		             
		             //增加小牌注册记录
		             cpdRegistrationMgtService.insertGroupRegi( cpdGroupRegistration);

		             if(req_period!=null){//CpdGroupPeriod为null时不进行重算
		                 //中间表记录变动计算方法
		                 cpdUtilService.calReqHours(cpdGroupRegistration, cpdType, cpdGroup, req_period, CpdUtils.REQUIRE_HOURS_ACTION_ADD, prePeriod, cpdGroupRegistration.getCgr_usr_ent_id());
		             }
		             
		             try {
		                 ObjectActionLog log = new ObjectActionLog();
		                 log.setObjectId(cpdGroupRegistration.getCgr_cg_id());//小牌ID
		                 log.setObjectCode(cpdRegistration.getCr_reg_number());//注册号码
		                 log.setObjectTitle(cpdRegistration.getUser().getUsr_ste_usr_id()+"-"+cpdRegistration.getCpdType().getCt_license_alias()
		                         +"-->"+cpdGroup.getCg_alias());//标题、全名
		                 log.setObjectType(ObjectActionLog.OBJECT_TYPE_CPD_REG);
		                 log.setObjectAction(ObjectActionLog.OBJECT_ACTION_ADD);
		                 log.setObjectActionType(ObjectActionLog.OBJECT_ACTION_TYPE_IMPORT);
		                 log.setObjectOptUserId(prof.getUsr_ent_id());
		                 log.setObjectActionTime(DateUtil.getCurrentTime());
		                 log.setObjectOptUserLoginTime(prof.login_date);
		                 log.setObjectOptUserLoginIp(prof.ip);
		                 SystemLogContext.saveLog(log, SystemLogTypeEnum.OBJECT_ACTION_LOG);
		             } catch (Exception e) {
		                 CommonLog.error("CPT/D Registration  exception:"+e.getMessage(),e);
		             }
		             
		             }
		     }
		     
		     List<Long> list = new ArrayList<Long>();//获取被删除的小牌cpd_cg_id
		     List<Long> list1 = new ArrayList<Long>();//获取被修改或者新增的小牌
		     if(!cpdGroupRegistrations.containsAll(cpdGroupRegistrationList)){//存在已注册的小牌被删除
		         for (int i = 0; i < cpdGroupRegistrationList.size(); i++) {
		             CpdGroupRegistration cpdGroupRegistration =  cpdGroupRegistrationList.get(i);
		             boolean isRegi = true;//判断该小牌是否被删除
		             for (int j = 0; j < cpdGroupRegistrations.size(); j++) {
		                 if(cpdGroupRegistrations.get(j).getCgr_cg_id().equals(cpdGroupRegistration.getCgr_cg_id())){
		                     isRegi = false;//没被删除，进行修改
		                     cpdGroupRegistration.setCgr_initial_date(cpdGroupRegistrations.get(j).getCgr_initial_date());
		                     cpdGroupRegistration.setCgr_expiry_date(cpdGroupRegistrations.get(j).getCgr_expiry_date());
		                     

		                     //大牌注册信息
		                     CpdRegistration cpdRegistration = new CpdRegistration();    
		                     cpdRegistration = cpdRegistrationMgtService.getDetail(cpdGroupRegistration.getCgr_cr_id());
		                     //大牌信息
		                     CpdType cpdType = new CpdType();
		                     cpdType = cpdRegistrationMgtService.getCpdTypeByid(cpdRegistration.getCr_ct_id());
		                     //小牌信息
		                     CpdGroup  cpdGroup = new CpdGroup();
		                     cpdGroup = cpdRegistrationMgtService.getCpdGroupById(cpdGroupRegistration.getCgr_cg_id());
		                     CpdGroupPeriod req_period = cpdUtilService.getReqHoursList(cpdGroup.getCg_id());
		                     
		                     //上一个周期
		                     CpdGroupRegistration preCpdGroupRegistration =  cpdUtilService.getPreGroupRegistrationNoId(cpdGroupRegistration.getCgr_cg_id(), cpdGroupRegistration.getCgr_usr_ent_id(), cpdGroupRegistration.getCgr_initial_date(),cpdGroupRegistration.getCgr_id());
		                     //实际开始时间
		                     cpdGroupRegistration.setCgr_actual_date(cpdUtilService.getCgrActualDate(cpdGroupRegistration, preCpdGroupRegistration, cpdType));
		                     //获取周期信息
		                     CpdPeriodVO prePeriod = null;
		                     if(preCpdGroupRegistration!=null){
		                         prePeriod = cpdUtilService.getPeriod(preCpdGroupRegistration.getCgr_initial_date(), cpdType.getCt_id());
		                     }

		                     //修改小牌注册信息
		                     cpdRegistrationMgtService.updCpdGroupRegistration(prof,cpdGroupRegistration);
		                     
		                     if(req_period!=null){//CpdGroupPeriod为null时不进行重算
		                         //中间表记录变动计算方法
		                         cpdUtilService.calReqHours(cpdGroupRegistration, cpdType, cpdGroup, req_period, CpdUtils.REQUIRE_HOURS_ACTION_UPD, prePeriod, cpdGroupRegistration.getCgr_usr_ent_id());
		                     }
		                     try {
		                         ObjectActionLog log = new ObjectActionLog();
		                         log.setObjectId(cpdGroupRegistration.getCgr_cg_id());//小牌ID
		                         log.setObjectCode(cpdRegistration.getCr_reg_number());//注册号码
		                         log.setObjectTitle(cpdRegistration.getUser().getUsr_ste_usr_id()+"-"+cpdRegistration.getCpdType().getCt_license_alias()
		                                 +"-->"+cpdGroup.getCg_alias());//标题、全名
		                         log.setObjectType(ObjectActionLog.OBJECT_TYPE_CPD_REG);
		                         log.setObjectAction(ObjectActionLog.OBJECT_ACTION_UPD);
		                         log.setObjectActionType(ObjectActionLog.OBJECT_ACTION_TYPE_IMPORT);
		                         log.setObjectOptUserId(prof.getUsr_ent_id());
		                         log.setObjectActionTime(DateUtil.getCurrentTime());
		                         log.setObjectOptUserLoginTime(prof.login_date);
		                         log.setObjectOptUserLoginIp(prof.ip);
		                         SystemLogContext.saveLog(log, SystemLogTypeEnum.OBJECT_ACTION_LOG);
		                     } catch (Exception e) {
		                         CommonLog.error("CPT/D Registration  exception:"+e.getMessage(),e);
		                     }
		                    
		                     break;
		                 }
		             }
		             /*if(isRegi){//进行删除
		                 //大牌注册信息
		                 CpdRegistration cpdRegistration = new CpdRegistration();    
		                 cpdRegistration = cpdRegistrationMgtService.getDetail(cpdGroupRegistration.getCgr_cr_id());
		                 //大牌信息
		                 CpdType cpdType = new CpdType();
		                 cpdType = cpdRegistrationMgtService.getCpdTypeByid(cpdRegistration.getCr_ct_id());
		                 //小牌信息
		                 CpdGroup  cpdGroup = new CpdGroup();
		                 cpdGroup = cpdRegistrationMgtService.getCpdGroupById(cpdGroupRegistration.getCgr_cg_id());
		                 CpdGroupPeriod req_period = cpdUtilService.getReqHoursList(cpdGroup.getCg_id());
		                 //上一个周期
		                 CpdGroupRegistration preCpdGroupRegistration =  cpdUtilService.getPreGroupRegistration(cpdGroupRegistration.getCgr_cg_id(), cpdGroupRegistration.getCgr_usr_ent_id(), cpdGroupRegistration.getCgr_initial_date());
		                //获取周期信息
		                 CpdPeriodVO prePeriod = null;
		                 if(preCpdGroupRegistration!=null){
		                     prePeriod = cpdUtilService.getPeriod(preCpdGroupRegistration.getCgr_initial_date(), cpdType.getCt_id());
		                 }

		                 //删除注册信息
		                 cpdRegistrationMgtService.delCpdGroupRegistration(prof, cpdGroupRegistration.getCgr_id(),0);
		                 
		                 //中间表记录变动计算方法
		                 if(req_period!=null){//CpdGroupPeriod为null时不进行重算
		                     cpdUtilService.calReqHours(cpdGroupRegistration, cpdType, cpdGroup, req_period, CpdUtils.REQUIRE_HOURS_ACTION_DEL, prePeriod, cpdGroupRegistration.getCgr_usr_ent_id());
		                 }
		                 try {
		                     ObjectActionLog log = new ObjectActionLog();
		                     log.setObjectId(cpdGroupRegistration.getCgr_cg_id());//小牌ID
		                     log.setObjectCode(cpdRegistration.getCr_reg_number());//注册号码
		                     log.setObjectTitle(cpdRegistration.getUser().getUsr_ste_usr_id()+"-"+cpdRegistration.getCpdType().getCt_license_alias()
		                             +"-->"+cpdGroup.getCg_alias());//标题、全名
		                     log.setObjectType(ObjectActionLog.OBJECT_TYPE_CPD_REG);
		                     log.setObjectAction(ObjectActionLog.OBJECT_ACTION_DEL);
		                     log.setObjectActionType(ObjectActionLog.OBJECT_ACTION_TYPE_IMPORT);
		                     log.setObjectOptUserId(prof.getUsr_ent_id());
		                     log.setObjectActionTime(DateUtil.getCurrentTime());
		                     log.setObjectOptUserLoginTime(prof.login_date);
		                     log.setObjectOptUserLoginIp(prof.ip);
		                     SystemLogContext.saveLog(log, SystemLogTypeEnum.OBJECT_ACTION_LOG);
		                 } catch (Exception e) {
		                     CommonLog.error("CPT/D Registration  exception:"+e.getMessage(),e);
		                 }
		                 
		                 list.add(cpdGroupRegistration.getCgr_cg_id());//被删除的小牌Id
		             }else{*/
		                 list1.add(cpdGroupRegistration.getCgr_cg_id());//需要修改的小牌id
//		             }
		         }
		     }else{//不存在被删除的小牌
		         for (int i = 0; i < cpdGroupRegistrationList.size(); i++) {
		             CpdGroupRegistration cpdGroupRegistration =  cpdGroupRegistrationList.get(i);
		             for (int j = 0; j < cpdGroupRegistrations.size(); j++) {
		                 if(cpdGroupRegistrations.get(j).getCgr_cg_id().equals(cpdGroupRegistration.getCgr_cg_id())){
		                     cpdGroupRegistrations.get(j).setCgr_id(cpdGroupRegistration.getCgr_id());
		                     cpdRegistrationMgtService.updCpdGroupRegistration(prof,cpdGroupRegistrations.get(j));
		                     break;
		                 }
		             }
		         }
		     }
		 }
	}

	private void updCpdRegistration(CpdRegistration cpdRegistration, loginProfile prof) {
        cpdRegistrationMgtService.update(prof, cpdRegistration);  
        try {
            ObjectActionLog log = new ObjectActionLog();
            log.setObjectId(cpdRegistration.getCr_ct_id());//大牌ID
            log.setObjectCode(cpdRegistration.getCr_reg_number());//注册号码
            RegUser regUser = regUserService.getUserDetail(cpdRegistration.getCr_usr_ent_id());
            CpdType cpdType = cpdRegistrationMgtService.getCpdTypeByid(cpdRegistration.getCr_ct_id());
            log.setObjectTitle(regUser.getUsr_ste_usr_id()+"-"+cpdType.getCt_license_alias());//标题、全名
            log.setObjectType(ObjectActionLog.OBJECT_TYPE_CPD_REG);
            log.setObjectAction(ObjectActionLog.OBJECT_ACTION_UPD);
            log.setObjectActionType(ObjectActionLog.OBJECT_ACTION_TYPE_IMPORT);
            log.setObjectOptUserId(prof.getUsr_ent_id());
            log.setObjectActionTime(DateUtil.getCurrentTime());
            log.setObjectOptUserLoginTime(prof.login_date);
            log.setObjectOptUserLoginIp(prof.ip);
            SystemLogContext.saveLog(log, SystemLogTypeEnum.OBJECT_ACTION_LOG);
        } catch (Exception e) {
            CommonLog.error("CPT/D Registration  exception:"+e.getMessage(),e);
        }
	}
	private void saveCpdGroupRegi(CpdRegistration cr, loginProfile prof) {
		int cgr_first_ind = 0;
		CpdType cpdType = null;
		CpdGroup cpdGroup = null;
	    cgr_first_ind = 0;
	    
	    CpdGroupRegistration cpdGroupRegistration = new CpdGroupRegistration();
	    Map<String, String> map = new HashMap<String, String>();
		map.put("CPD_group_code", cr.CPD_group_code);
		map.put("License_type", cr.license_type);
		Long cg_id = cpdGroupRegistrationMapper.getGroupId(map);
		cpdGroupRegistration.setCgr_cg_id(cg_id);
		cpdGroupRegistration.setCgr_cr_id(cr.getCr_id());
	    cpdGroupRegistration.setCgr_usr_ent_id(cr.getCr_usr_ent_id());
	    cpdGroupRegistration.setCgr_initial_date(cr.getCgr_initial_datetime());
	    cpdGroupRegistration.setCgr_expiry_date(cr.getCgr_expiry_datetime());
	    
	    if(cpdUtilService.cpdIsFirstInd(cpdGroupRegistration.getCgr_cg_id(), cpdGroupRegistration.getCgr_usr_ent_id(), cpdGroupRegistration.getCgr_initial_date())){
	        cgr_first_ind = 1;//是首次挂牌
	    }
	    cpdGroupRegistration.setCgr_first_ind(cgr_first_ind);
	    cpdGroupRegistration.setCgr_create_usr_ent_id(prof.getUsr_ent_id());
	    cpdGroupRegistration.setCgr_create_datetime(cpdRegistrationMgtService.getDate());
	    cpdGroupRegistration.setCgr_update_usr_ent_id(prof.getUsr_ent_id());
	    cpdGroupRegistration.setCgr_update_datetime(cpdRegistrationMgtService.getDate());
	    cpdGroupRegistration.setCgr_status(CpdUtils.STATUS_OK);        
	    //大牌注册信息
	    CpdRegistration cpdRegistration = new CpdRegistration();    
	    cpdRegistration = cpdRegistrationMgtService.getDetail(cpdGroupRegistration.getCgr_cr_id());
	    //大牌信息
	    cpdType = new CpdType();
	    cpdType = cpdRegistrationMgtService.getCpdTypeByid(cpdRegistration.getCr_ct_id());
	    //小牌信息
	    cpdGroup = new CpdGroup();
	    cpdGroup = cpdRegistrationMgtService.getCpdGroupById(cpdGroupRegistration.getCgr_cg_id());
	    
	    CpdGroupPeriod req_period = cpdUtilService.getReqHoursList(cpdGroup.getCg_id());
	    
	    
	    //上一个周期
	    CpdGroupRegistration preCpdGroupRegistration =  cpdUtilService.getPreGroupRegistration(cpdGroupRegistration.getCgr_cg_id(), cpdGroupRegistration.getCgr_usr_ent_id(), cpdGroupRegistration.getCgr_initial_date());
	    //实际开始时间
	    cpdGroupRegistration.setCgr_actual_date(cpdUtilService.getCgrActualDate(cpdGroupRegistration, preCpdGroupRegistration, cpdType));
	    

	    //获取周期信息
	    CpdPeriodVO prePeriod = null;
	    if(preCpdGroupRegistration!=null){
	        prePeriod = cpdUtilService.getPeriod(preCpdGroupRegistration.getCgr_initial_date(), cpdType.getCt_id());
	    }
	    
	    //增加小牌注册记录
	    cpdRegistrationMgtService.insertGroupRegi( cpdGroupRegistration);


	    if(req_period!=null){//CpdGroupPeriod为null时不进行重算
	      //中间表记录变动计算方法
	        cpdUtilService.calReqHours(cpdGroupRegistration, cpdType, cpdGroup, req_period, CpdUtils.REQUIRE_HOURS_ACTION_ADD, prePeriod, cpdGroupRegistration.getCgr_usr_ent_id());
	    }
	    
	    try {
	        ObjectActionLog log = new ObjectActionLog();
	        log.setObjectId(cpdGroupRegistration.getCgr_cg_id());//小牌ID
	        log.setObjectCode(cpdRegistration.getCr_reg_number());//注册号码
	        log.setObjectTitle(cpdRegistration.getUser().getUsr_ste_usr_id()+"-"+cpdRegistration.getCpdType().getCt_license_alias()
	                +"-->"+cpdGroup.getCg_alias());//标题、全名
	        log.setObjectType(ObjectActionLog.OBJECT_TYPE_CPD_REG);
	        log.setObjectAction(ObjectActionLog.OBJECT_ACTION_ADD);
	        log.setObjectActionType(ObjectActionLog.OBJECT_ACTION_TYPE_IMPORT);
	        log.setObjectOptUserId(prof.getUsr_ent_id());
	        log.setObjectActionTime(DateUtil.getCurrentTime());
	        log.setObjectOptUserLoginTime(prof.login_date);
	        log.setObjectOptUserLoginIp(prof.ip);
	        SystemLogContext.saveLog(log, SystemLogTypeEnum.OBJECT_ACTION_LOG);
	    } catch (Exception e) {
	        CommonLog.error("CPT/D Registration  exception:"+e.getMessage(),e);
	    }
	}

	private void save(CpdRegistration cpdRegistration, loginProfile prof) {
        cpdRegistrationMgtService.insert(prof,cpdRegistration);   
        try {
            ObjectActionLog log = new ObjectActionLog();
            log.setObjectId(cpdRegistration.getCr_ct_id());//大牌ID
            log.setObjectCode(cpdRegistration.getCr_reg_number());//注册号码
            RegUser regUser = regUserService.getUserDetail(cpdRegistration.getCr_usr_ent_id());
            CpdType cpdType = cpdRegistrationMgtService.getCpdTypeByid(cpdRegistration.getCr_ct_id());
            log.setObjectTitle(regUser.getUsr_ste_usr_id()+"-"+cpdType.getCt_license_alias());//标题、全名
            log.setObjectType(ObjectActionLog.OBJECT_TYPE_CPD_REG);
            log.setObjectAction(ObjectActionLog.OBJECT_ACTION_ADD);
            log.setObjectActionType(ObjectActionLog.OBJECT_ACTION_TYPE_IMPORT);
            log.setObjectOptUserId(prof.getUsr_ent_id());
            log.setObjectActionTime(DateUtil.getCurrentTime());
            log.setObjectOptUserLoginTime(prof.login_date);
            log.setObjectOptUserLoginIp(prof.ip);
            SystemLogContext.saveLog(log, SystemLogTypeEnum.OBJECT_ACTION_LOG);
        } catch (Exception e) {
            CommonLog.error("CPT/D Registration  exception:"+e.getMessage(),e);
        }
	}




	@Override
    public boolean checkRecord(String[] row, int lineno, ImportObject record,loginProfile prof) throws cwException, SQLException, ParseException {
        Vector<String> log = new Vector<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        boolean flag = true;
		String[] regs = row;
		CPDRegistrationBean cr = new CPDRegistrationBean();
		for(int j=0;j<regs.length;j++){
			switch (j) {
				case 0:
					cr.user_id = regs[j];
					String temp = regs[j];
					if(temp == null || temp.length() ==0){
						logFailure("[Error] Line " + lineno + ": User ID cannot be null.");
						flag = false;
						break;
					}
					dbRegUser user = new dbRegUser();
					user.usr_ste_usr_id = temp;
					if(!user.checkSiteUsrIdExist(con, 1)){
						logFailure("[Error] Line " + lineno + ": User "+temp+" does not exist.");
						flag = false;
						break;
					}
					break;
				case 1:
					cr.License_type = regs[j];
					temp = regs[j];
					if(temp == null || temp.length() ==0){
						logFailure("[Error] Line " + lineno + ": License type cannot be null.");
						flag = false;
						break;
					}
					if(!dbCPDType.isTypeExist(con,temp)){
						logFailure("[Error] Line " + lineno + ": License type "+temp+" does not exist.");
						flag = false;
						break;
					}							
					break;
				case 2:
					cr.Reg_no = regs[j];
					temp = regs[j];
					if(temp == null || temp.length() ==0){
						logFailure("[Error] Line " + lineno + ": Reg no. cannot be null.");
						flag = false;
						break;
					}
					break;
				case 3:
					cr.Reg_date = regs[j];
					temp = regs[j];
					if(temp == null || temp.length() ==0){
						logFailure("[Error] Line " + lineno + ": Reg date cannot be null.");
						flag = false;
						break;
					}else if(Imp.getTimestamp(temp) == null){
						logFailure("[Error] Line " + lineno + ": Reg date Validation fails.");
						flag = false;
						break;
					}
					break;
				case 4:
					cr.De_reg_date = regs[j];
					temp = regs[j];
					if(cr.De_reg_date != null && cr.De_reg_date.length() > 0){
						if(sdf.parse(cr.Reg_date).getTime() >  sdf.parse(cr.De_reg_date).getTime()){
							logFailure("[Error] Line " + lineno + ": De-reg date cannot be earlier Reg date.");
							flag = false;
							break;
						}
						if(Imp.getTimestamp(temp) == null){
							logFailure("[Error] Line " + lineno + ": De-reg date Validation fails.");
							flag = false;
							break;
						}
					}
					break;
				case 5:
					cr.CPD_group_code = regs[j];
					temp = regs[j];
					if(temp == null || temp.length() ==0){
						logFailure("[Error] Line " + lineno + ": CPT/D group code cannot be null");
						flag = false;
						break;
					}
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("CPD_group_code", temp);
					map.put("License_type", cr.License_type);
					if(!dbCPDType.isGroupExist(con,temp,cr.License_type)){
						logFailure("[Error] Line " + lineno + ": CPT/D group code "+temp+" does not belong to the specified license type.");
						flag = false;
						break;
					}	
					break;
				case 6:
					cr.Initial_date = regs[j];
					temp = regs[j];
					map = new HashMap<String, Object>();
					if(temp == null || temp.length() ==0){
						logFailure("[Error] Line " + lineno + ": Initial date cannot be null");
						flag = false;
						break;
					}else if(Imp.getTimestamp(temp) == null){
						logFailure("[Error] Line " + lineno + ": Initial date Validation fails.");
						flag = false;
						break;
					}
					map.put("usr_ste_usr_id", cr.user_id);
					map.put("License_type", cr.License_type);
					CpdRegistration cm = dbCPDType.getGroupRegistration(con,cr.user_id,cr.License_type);
					/*if(cm != null && sdf.parse(cr.Initial_date).getTime() !=  cm.cr_reg_datetime.getTime()){
						logFailure("[Error] Line " + lineno + ": User "+cr.user_id+" already registers in license "+cr.License_type+" (Registration date: "+cr.Initial_date+").");
						flag = false;
						break;
					}*/
					if(sdf.parse(cr.Reg_date).getTime() >  sdf.parse(cr.Initial_date).getTime()){
						logFailure("[Error] Line " + lineno + ": Initial date cannot be earlier than reg date.");
						flag = false;
						break;
					}
					break;
				case 7:
					cr.Expiry_date = regs[j];
					temp = regs[j];
					if(cr.Expiry_date != null && cr.Expiry_date.length() > 0){
						if(sdf.parse(cr.Initial_date).getTime() >  sdf.parse(cr.Expiry_date).getTime()){
							logFailure("[Error] Line " + lineno + ": Expiry date cannot be earlier initial date.");
							flag = false;
							break;
						}
						if(cr.De_reg_date != null && cr.De_reg_date.length() > 0){
							if(sdf.parse(cr.De_reg_date).getTime() <  sdf.parse(cr.Expiry_date).getTime()){
								logFailure("[Error] Line " + lineno + ": Expiry date cannot be earlier De-reg date.");
								flag = false;
								break;
							}
						}else{
							//logFailure("[Error] Line " + lineno + ": Expiry date of all CPT/D groups cannot be null if reg date is null.");
							//flag = false;
							//break;
						}
						if(Imp.getTimestamp(temp) == null){
							logFailure("[Error] Line " + lineno + ": Expiry date Validation fails.");
							flag = false;
							break;
						}
					}else{
						if(cr.De_reg_date != null && cr.De_reg_date.length() > 0){
							logFailure("[Error] Line " + lineno + ": Expiry date of all CPT/D groups cannot be null if De-reg date is not null.");
							flag = false;
							break;
						}
					}
					break;
				default:
					break;
			}
			if(!flag){
				break;
			}
		}
		//时间是否重叠
        if(cr.user_id!=null && cr.user_id.length()>0  && cr.License_type!=null && cr.License_type.length()>0 && cr.Reg_date!=null && cr.Reg_date.length()>0){
            // 如果用户大牌有注册信息就修改
            // 如果用户大牌没有注册信息就添加
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("usr_ste_usr_id", cr.user_id);
            map.put("License_type", cr.License_type);
            map.put("reg_date",  Imp.getTimestamp(cr.Reg_date) );
            CpdRegistration cm = cpdGroupRegistrationMapper.getGroupRegistration(map);
            boolean is_add = true;
            if(cm != null){
                is_add = false;
            }
            boolean isInfoRegistrationBydate = false;
            if(is_add){
                CpdRegistration cpdRegistration2 = new CpdRegistration();
                if(regUserMapper.getUserBySteId(cr.user_id)!=null){
                    cpdRegistration2.setCr_usr_ent_id(regUserMapper.getUserBySteId(cr.user_id).getUsr_ent_id());
                }else{
                    cpdRegistration2.setCr_usr_ent_id((long)0);
                }
                CpdType cpdType = new CpdType();
                cpdType.setCt_license_type(cr.License_type);
                if(cpdTypeMapper.getTypeByCode(cpdType)!=null){
                    cpdRegistration2.setCr_ct_id(cpdTypeMapper.getTypeByCode(cpdType).getCt_id());
                }else{
                    cpdRegistration2.setCr_ct_id((long)0);
                }
                cpdRegistration2.setCr_reg_datetime(Imp.getTimestamp(cr.Reg_date));
                if(cr.De_reg_date!=null && cr.De_reg_date.length()>0){
                    cpdRegistration2.setCr_de_reg_datetime(Imp.getTimestamp(cr.De_reg_date));
                }
                
                isInfoRegistrationBydate = cpdRegistrationMgtService.isInfoRegistrationBydate(prof,cpdRegistration2);//检查大牌报名记录时间是否重叠
                if(isInfoRegistrationBydate){//已重叠
                    log.add("[Error] Line " + lineno + ": User "+cr.user_id+" already registers in license "+cr.License_type+" (Registration date: "+cr.Reg_date+" ).");
                    flag = false;
                }
            }
        }
        
		return flag;
    }
	
	/**
	 * 赋值
	 */
	@Override
	public ImportObject putField(String[] value, ImportObject record_, int colIdx,loginProfile prof) {
		CPDRegistrationBean cr = (CPDRegistrationBean)record_;
		for(int i=0;i<value.length;i++){
			switch (i) {
			case 0:
				cr.user_id = value[i];
				break;
			case 1:
				cr.License_type = value[i];
				break;
			case 2:
				cr.Reg_no = value[i];
				break;
			case 3:
				cr.Reg_date = value[i];
				break;
			case 4:
				cr.De_reg_date = value[i];
				break;
			case 5:
				cr.CPD_group_code = value[i];
				break;
			case 6:
				cr.Initial_date = value[i];
				break;
			case 7:
				cr.Expiry_date = value[i];
				break;

			default:
				break;
			}
		}
		
		return cr;
	}

	public ImportObject getNewRecordBean() {
		ImportObject record = new CPDRegistrationBean();
		return record;
	}
	

	



}
