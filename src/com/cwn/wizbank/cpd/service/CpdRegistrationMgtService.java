package com.cwn.wizbank.cpd.service;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.dataMigrate.imp.Imp;
import com.cw.wizbank.dataMigrate.imp.ImpCPDRegistration;
import com.cw.wizbank.dataMigrate.imp.bean.CPDRegistrationBean;
import com.cw.wizbank.db.DbIMSLog;
import com.cw.wizbank.enterprise.IMSLog;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbEnv;
import com.cw.wizbank.upload.UploadUtils;
import com.cw.wizbank.util.LangLabel;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.cpd.utils.CpdUtils;
import com.cwn.wizbank.entity.AcRole;
import com.cwn.wizbank.entity.CpdGroup;
import com.cwn.wizbank.entity.CpdGroupRegistration;
import com.cwn.wizbank.entity.CpdRegistration;
import com.cwn.wizbank.entity.CpdType;
import com.cwn.wizbank.entity.ImsLog;
import com.cwn.wizbank.persistence.CpdGroupMapper;
import com.cwn.wizbank.persistence.CpdGroupRegistrationMapper;
import com.cwn.wizbank.persistence.CpdRegistrationMapper;
import com.cwn.wizbank.persistence.CpdTypeMapper;
import com.cwn.wizbank.persistence.ImsLogMapper;
import com.cwn.wizbank.persistence.RegUserMapper;
import com.cwn.wizbank.scheduled.ImportCPDFromFile;
import com.cwn.wizbank.services.BaseService;
import com.cwn.wizbank.utils.CommonLog;
import com.cwn.wizbank.utils.LabelContent;
import com.cwn.wizbank.utils.Page;
@Service
public class CpdRegistrationMgtService extends BaseService<CpdRegistrationMapper> {

	@Autowired
	CpdRegistrationMapper cpdRegistrationMapper;
	
    @Autowired
    CpdGroupRegistrationMapper cpdGroupRegistrationMapper;
	
	@Autowired
	CpdTypeMapper cpdTypeMapper;
	@Autowired
	ImsLogMapper imsLogMapper;
	
	@Autowired
	CpdGroupMapper cpdGroupMapper;
	
	@Autowired
	RegUserMapper regUserMapper;	
	/**
	 * 牌照管理页面列表
	 * @param page
	 * @param prof
	 * @param ct_id
	 * @param searchText
	 * @return
	 */
    public Page<CpdRegistration> searchAll(Page<CpdRegistration> page, loginProfile prof, long ct_id,String searchText) {
        if(!AccessControlWZB.isRoleTcInd(prof.current_role)){
            page.getParams().put("usr_ent_id", prof.root_ent_id);
            page.getParams().put("current_role", AcRole.ROLE_ADM_1);
        }else{
            page.getParams().put("usr_ent_id", prof.usr_ent_id);
            page.getParams().put("current_role", prof.current_role);
        }
        if(ct_id!=0){
            page.getParams().put("ct_id", ct_id);
        }
        if(searchText!=null){
            page.getParams().put("searchText", searchText.trim());
        }
        //当前角色是否与培训中心关联
        page.getParams().put("is_role_tc_ind", AccessControlWZB.isRoleTcInd(prof.current_role));
        page.getParams().put("my_top_tc_id", prof.my_top_tc_id);
        
        cpdRegistrationMapper.searchAll(page);
        return page;
    }
    

    /**
     * 获取大牌列表
     * @return
     */
    public List<CpdType> getCpdType(){
        List<CpdType> cpdType = cpdRegistrationMapper.getCpdType();
        return cpdType;
    }
    
    
    /**
     * 根据大牌ID获取大牌
     * @return
     */
    public CpdType getCpdTypeByid(long ct_id){
        CpdType cpdType = cpdRegistrationMapper.getCpdTypeByid(ct_id);
        return cpdType;
    }
    
    
    /**
     * 根据小牌ID获取小牌
     * @return
     */
    public CpdGroup getCpdGroupById(long cg_id){
        CpdGroup cpdGroup = cpdRegistrationMapper.getCpdGroupById(cg_id);
        return cpdGroup;
    }
    
    
    
    /**
     * 获取大牌下的小牌列表
     * @param id
     * @return
     */
    public List<CpdGroup> getCpdGroupMap(long id){
        List<CpdGroup> cpdType = cpdRegistrationMapper.getCpdGroupMap(id);
        return cpdType;
    }
    
    /**
     * 获取用户大牌详细报名记录
     * @param cr_id
     * @return
     */
    public CpdRegistration getDetail(long cr_id){
        CpdRegistration cpdRegistration = null;
        cpdRegistration =cpdRegistrationMapper.getDetail(cr_id);
        if(cpdRegistration != null){
        	if(cpdRegistration.getCpdType().getCt_recover_hours_period()==null){
                cpdRegistration.getCpdType().setCt_recover_hours_period(0);
            }
        }
        return cpdRegistration;
    }
    
    /**
     * 获取用户某牌照下的小牌详细报名记录及该牌照下未报名的小牌
     * @param cr_id
     * @return
     */
    public List<CpdGroup> getUserGroupRegi(long cg_ct_id,long cgr_cr_id,long cgr_usr_ent_id){
        List<CpdGroup> cList = null;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cg_ct_id", cg_ct_id);
        params.put("cgr_usr_ent_id", cgr_usr_ent_id);
        params.put("cgr_cr_id", cgr_cr_id);
        cList =cpdGroupMapper.getUserGroupRegi(params);
        return cList;
    }
    
    
    /**
     * 保存大牌报名记录
     * @param prof
     * @param cpdRegistration
     */
    public void insert( loginProfile prof,CpdRegistration cpdRegistration){
        cpdRegistration.setCr_create_usr_ent_id(prof.getUsr_ent_id());
        cpdRegistration.setCr_create_datetime(getDate());
        cpdRegistration.setCr_update_usr_ent_id(prof.getUsr_ent_id());
        cpdRegistration.setCr_update_datetime(getDate());
        cpdRegistration.setCr_status("OK");
        cpdRegistrationMapper.insert(cpdRegistration);
    };
    
    /**
     * 更新大牌报名记录
     * @param prof
     * @param cpdRegistration
     */
    public void update( loginProfile prof,CpdRegistration cpdRegistration){
        cpdRegistration.setCr_update_usr_ent_id(prof.getUsr_ent_id());
        cpdRegistration.setCr_update_datetime(getDate());
        cpdRegistrationMapper.update(cpdRegistration);
    };
    
    
    /**
     * 保存小牌牌报名记录
     * @param prof
     * @param cpdRegistration
     */
    public void insertGroupRegi(CpdGroupRegistration cpdGroupRegistrations){
            cpdRegistrationMapper.insertGroupRegi(cpdGroupRegistrations);
    };
    
    
    /**
     * 检查大牌报名记录是否重复
     * @param prof
     * @param cpdRegistration
     */
    public boolean isExistForRegistration( loginProfile prof,CpdRegistration cpdRegistration){
        boolean equalId = false;
        List<CpdRegistration> cpdRegistrations = cpdRegistrationMapper.isExistForRegistration(cpdRegistration);
        if(cpdRegistrations!=null && cpdRegistrations.size()>0){
            for (int i = 0; i < cpdRegistrations.size(); i++) {
                if(cpdRegistrations.get(i).getCr_id().equals(cpdRegistration.getCr_id())){
                    equalId = true;
                    break;
                }
            }
            if(equalId){
                return false;
            }else{
                return true;//存在数据，重复
            }
        }else{
            return false;
        }
    };
    
    
    /**
     * 检查大牌报名记录时间是否重叠
     * @param prof
     * @param cpdRegistration
     */
    public boolean isInfoRegistrationBydate( loginProfile prof,CpdRegistration cpdRegistration){
        List<CpdRegistration> cpdRegistrations = null;
        if(cpdRegistration.getCr_de_reg_datetime()!=null){
            cpdRegistrations = cpdRegistrationMapper.isInfoRegistrationBydate(cpdRegistration);
        }else{
            cpdRegistrations = cpdRegistrationMapper.isInfoRegistrationBydateNul(cpdRegistration);
        }

        boolean equalId = false;
        if(cpdRegistrations!=null && cpdRegistrations.size()>0){
            if(cpdRegistrations.size()==1){
                for (int i = 0; i < cpdRegistrations.size(); i++) {
                    if(cpdRegistrations.get(i).getCr_id().equals(cpdRegistration.getCr_id())){
                        equalId = true;
                        break;
                    }
                }
                if(equalId){
                    return false;
                }else{
                    return true;//存在数据，时间重叠
                }
            }else{
                return true;//存在数据，时间重叠
            }
            
        }else{
            return false;
        }
    };
    
    
    public List<CpdRegistration> getCpdRegistrationByCtId(Long ctId){
    	Map map = new HashMap<String,Object>();
    	map.put("ct_id", ctId);
    	return cpdRegistrationMapper.getCpdRegistration(map);
    }
    
    /**
     * 判断牌照是否有学员注册
     * @param cpdType
     * @return
     */
	public boolean getCountByCtID(long ct_id) {
		boolean flg =false;
		int count = cpdRegistrationMapper.getCountByCtID(ct_id);
		if(count > 0){
			flg = true;
		}
		return flg;
	}
    
    public CpdRegistration getCpdRegistrationById(Long crId){
    	CpdRegistration cr = null;
    	cr =  cpdRegistrationMapper.get(crId);
    	if(null!=cr){
    		cr.setCpdType(cpdTypeMapper.get(cr.getCr_ct_id()));
    	}
    	return cr;
    }


    /**
     * 查询大牌报名记录ID下的小牌报名记录
     * @param cgr_cr_id
     * @return
     */
    public List<CpdGroupRegistration> getGroupRegiByCrId(long cgr_cg_id,long cgr_cr_id){
        List<CpdGroupRegistration> cList = null;
        Map<String, Object> params = new HashMap<String, Object>();
        if(cgr_cg_id!=0){
            params.put("cgr_cg_id", cgr_cg_id);
        }
        if(cgr_cr_id!=0){
            params.put("cgr_cr_id", cgr_cr_id);
        }
        cList =cpdGroupRegistrationMapper.getCpdGroupReg(params);
        return cList;
    }
	
    
    /**
     * 删除用户小牌注册记录
     * @param cgr_cr_id
     * @return
     */
    public void delCpdGroupRegistration(loginProfile prof,long cgr_id,long cgr_cr_id){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cgr_status", CpdUtils.STATUS_DEL);
        params.put("cgr_update_usr_ent_id", prof.getUsr_ent_id());
        params.put("cgr_update_datetime", getDate());
        if(cgr_id!=0){
            params.put("cgr_id", cgr_id);
        }
        if(cgr_cr_id!=0){
            params.put("cgr_cr_id", cgr_cr_id);
        }
        cpdRegistrationMapper.delCpdGroupRegistration(params);
    }
    
    
    /**
     * 删除用户大牌注册记录
     * @param cr_id
     * @return
     */
    public void delCpdRegistration(loginProfile prof,long cr_id){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cr_status", CpdUtils.STATUS_DEL);
        params.put("cr_update_usr_ent_id", prof.getUsr_ent_id());
        params.put("cr_update_datetime", getDate());
        if(cr_id!=0){
            params.put("cr_id", cr_id);
        }
        cpdRegistrationMapper.delCpdRegistration(params);
    }
    
    /**
     * 修改用户小牌注册记录
     * @param cgr_cr_id
     * @return
     */
    public void updCpdGroupRegistration(loginProfile prof,CpdGroupRegistration cpdGroupRegistration){
        cpdGroupRegistration.setCgr_update_usr_ent_id(prof.getUsr_ent_id());
        cpdGroupRegistration.setCgr_update_datetime(getDate());
        cpdRegistrationMapper.updCpdGroupRegistration(cpdGroupRegistration);
    }
    
    
    public CpdRegistration getCpdRegistration(Long ctId,Long userID,Integer period){
    	Map map = new HashMap<String,Object>();
    	map.put("ct_id", ctId);
    	map.put("usr_ent_id", userID);
    	map.put("period", period);
    	List<CpdRegistration>  list = cpdRegistrationMapper.getCpdRegistration(map);
    	if(null!=list && list.size()>0){
    		return list.get(0);
    	}
    	return null;
    }
    
    public CpdRegistration getCpdRegistrationByPeriod(Long ctId,Long userID,Integer period){
        Map map = new HashMap<String,Object>();
        map.put("ct_id", ctId);
        map.put("usr_ent_id", userID);
        map.put("period", period);
        List<CpdRegistration>  list = cpdRegistrationMapper.getCpdRegistrationByPeriod(map);
        if(null!=list && list.size()>0){
            return list.get(0);
        }
        return null;
    }
    
    
    /**
     * 查询某大牌注册记录下的小牌注册记录及小牌别名
     * @param cgr_cr_id
     * @return
     */
    public  List<CpdGroupRegistration> getCpdCrGroupExist(loginProfile prof,long cgr_cr_id){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cgr_cr_id", cgr_cr_id);
        return cpdGroupRegistrationMapper.getCpdCrGroupExist(params);
    }


	public void cpdRegistrationMgtService(Model model, MultipartFile file, WizbiniLoader wizbini, loginProfile prof, qdbEnv static_env, CpdRegistration cpdRegistration) throws cwSysMessage, IOException {
		 String fileName = file.getName();
		 fileName = file.getOriginalFilename();
		 String tmpUploadPath = static_env.INI_MSG_DIR_UPLOAD_TMP;
		 File srcFile = new File (tmpUploadPath, fileName);
		 Imp imp=new ImpCPDRegistration(null,wizbini,tmpUploadPath+cwUtils.SLASH,srcFile.getAbsolutePath());
         Vector<String> log = new Vector<String>();
         Vector<CPDRegistrationBean> crs = new Vector<CPDRegistrationBean>();
         int lineno = 2;
         SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
         int errorNumber = 0;
         int successNumber = 0;
         int totalNumber = 0;
         boolean flag = true;
         try{
        	file.transferTo(srcFile);
         	Imp.ImportStatus importStatus=imp.initLog(prof);
			Vector recVec = imp.parseSourseFile(srcFile,prof);
			if(recVec == null || recVec.size() == 1){
				log.add(LangLabel.getValue(prof.cur_lan, "parse_file_err")+srcFile.getName()+LangLabel.getValue(prof.cur_lan, "no_record"));
			}
			for(int i=1;i<recVec.size();i++){
				String[] regs = (String[]) recVec.get(i);
				CPDRegistrationBean cr = new CPDRegistrationBean();
				for(int j=0;j<regs.length;j++){
					switch (j) {
						case 0:
							cr.user_id = regs[j];
							String temp = regs[j];
							dbRegUser user = new dbRegUser();
							user.usr_ste_usr_id = temp;
							if(temp == null || temp.length() ==0){
								log.add("[Error] Line " + lineno + ": User ID cannot be null.");
								flag = false;
							}else if(temp.length() < 3){
								log.add("[Error] Line " + lineno + ": User ID should not be less than 3 characters");
							}else if(temp.length() > 20){
								log.add("[Error] Line " + lineno + ": User ID should not exceed 20 characters");
							}
							else if(!regUserMapper.isUserExist(temp)){
								log.add("[Error] Line " + lineno + ": User "+temp+" does not exist.");
								flag = false;
							}
							break;
						case 1:
							cr.License_type = regs[j];
							temp = regs[j];
							if(temp == null || temp.length() ==0){
								log.add("[Error] Line " + lineno + ": License type cannot be null.");
								flag = false;
							}else if(temp.length() > 20){
								log.add("[Error] Line " + lineno + ": License type should not exceed 20 characters");
							}
							else if(!cpdTypeMapper.isTypeExist(temp)){
								log.add("[Error] Line " + lineno + ": License type "+temp+" does not exist.");
								flag = false;
							}							
							break;
						case 2:
							cr.Reg_no = regs[j];
							temp = regs[j];
							if(temp == null || temp.length() ==0){
								log.add("[Error] Line " + lineno + ": Reg no. cannot be null.");
								flag = false;
							}else if(temp.length() > 80){
								log.add("[Error] Line " + lineno + ": Reg no. should not exceed 80 characters");
							}else{
								String usr_id = temp;
								String regex = "[A-Za-z0-9-_]*";
								if(!Pattern.matches(regex, usr_id)) {
									log.add("[Error] Line " + lineno + ": Please enter only english characters, numbers in the 'Reg no'.");
									flag = false;
					            	break;
								}
								/*
								//检查牌照号码是否重复
								boolean isExistForRegistration = this.isExistForRegistration(prof,cpdRegistration);
								if(!isExistForRegistration) {
									log.add("[Error] Line " + lineno + ": Registration record repeat.");
									flag = false;
									break;
								}
								*/
								/*
						        for (int h=0;h<usr_id.length(); h++) {
						            if ((usr_id.charAt(h) >= 'A' && usr_id.charAt(h) <= 'Z') ||(usr_id.charAt(h) >= 'a' && usr_id.charAt(h) <= 'z') || (usr_id.charAt(h) >= '0' && usr_id.charAt(h) <= '9')
						                ){
						                
						            }else{
						            	log.add("[Error] Line " + lineno + ": Please enter only english characters, numbers in the 'Reg no'.");
						            	break;
						            }
						        }
						        */
							}
							break;
						case 3:
							cr.Reg_date = regs[j];
							temp = regs[j];
							if(temp == null || temp.length() ==0){
								log.add("[Error] Line " + lineno + ": Reg date cannot be null.");
								flag = false;
							}else if(Imp.getTimestamp(temp) == null){
								log.add("[Error] Line " + lineno + ": Reg date format is invalid.");
								flag = false;
							}
							break;
						case 4:
							cr.De_reg_date = regs[j];
							temp = regs[j];
							if(cr.Reg_date != null && cr.Reg_date.length() > 0 && cr.De_reg_date != null && cr.De_reg_date.length() > 0){
								if(Imp.getTimestamp(temp) != null && sdf.parse(cr.Reg_date).getTime() >=  sdf.parse(cr.De_reg_date).getTime()){
									log.add("[Error] Line " + lineno + ": De-reg date cannot be earlier Reg date.");
									flag = false;
								}
								else if(Imp.getTimestamp(temp) == null){
									log.add("[Error] Line " + lineno + ": De-reg date format is invalid.");
									flag = false;
								}
							}
							break;
						case 5:
							cr.CPD_group_code = regs[j];
							temp = regs[j];
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("CPD_group_code", temp);
							map.put("License_type", cr.License_type);
							if(temp == null || temp.length() ==0){
								log.add("[Error] Line " + lineno + ": CPT/D group code cannot be null");
								flag = false;
							}else if(temp.length() > 20){
								log.add("[Error] Line " + lineno + ": CPT/D group code should not exceed 20 characters");
							}
							else if(!cpdTypeMapper.isGroupExist(map)){
								log.add("[Error] Line " + lineno + ": CPT/D group code "+temp+" does not exist.");
								flag = false;
							}	
							else if(cr.License_type != null && cr.License_type.length() > 0 && !cpdTypeMapper.isGroupBelongLicense(map)){
							    if(cpdTypeMapper.isTypeExist(cr.License_type)){
							        log.add("[Error] Line " + lineno + ": CPT/D group code "+temp+" does not belong to the specified license type.");
	                                flag = false;
							    }
								
							}	
							break;
						case 6:
							cr.Initial_date = regs[j];
							temp = regs[j];
							map = new HashMap<String, Object>();
							if(temp == null || temp.length() ==0){
								log.add("[Error] Line " + lineno + ": Initial date cannot be null");
								flag = false;
							}
							else if(Imp.getTimestamp(temp) == null){
								log.add("[Error] Line " + lineno + ": Initial date format is invalid.");
								flag = false;
							}
							else if(cr.Reg_date != null && cr.Reg_date.length() > 0 && cr.Reg_date.indexOf("/") != -1 && Imp.getTimestamp(cr.Reg_date) != null &&  sdf.parse(cr.Reg_date).getTime() >  sdf.parse(cr.Initial_date).getTime()){
								log.add("[Error] Line " + lineno + ": Initial date cannot be earlier than reg date.");
								flag = false;
							}
							else if(cr.Initial_date != null && cr.Initial_date.length() > 0  && cr.De_reg_date != null && cr.De_reg_date.length() > 0  && cr.De_reg_date.indexOf("/") != -1 && Imp.getTimestamp(cr.De_reg_date) != null &&  sdf.parse(cr.De_reg_date).getTime() <  sdf.parse(cr.Initial_date).getTime()){
								log.add("[Error] Line " + lineno + ": De-reg date cannot be earlier than Initial date.");
								flag = false;
							}
							break;
						case 7:
							cr.Expiry_date = regs[j];
							temp = regs[j];
							if(cr.Expiry_date != null && cr.Expiry_date.length() > 0){
								if(Imp.getTimestamp(temp) == null){
									log.add("[Error] Line " + lineno + ": Expiry date format is invalid.");
									flag = false;
								}
								else if(cr.Initial_date != null && cr.Initial_date.length() > 0 && cr.Initial_date.indexOf("/") != -1 && Imp.getTimestamp(cr.Initial_date) != null &&  sdf.parse(cr.Initial_date).getTime() >=  sdf.parse(cr.Expiry_date).getTime()){
									log.add("[Error] Line " + lineno + ": Expiry date cannot be earlier or equal to initial date.");
									flag = false;
								}
								else if(cr.Reg_date != null && cr.Reg_date.length() > 0 && cr.Reg_date.indexOf("/") != -1 && Imp.getTimestamp(cr.Reg_date) != null &&  sdf.parse(cr.Reg_date).getTime() >  sdf.parse(cr.Expiry_date).getTime()){
									log.add("[Error] Line " + lineno + ": Expiry date cannot be earlier reg date.");
									flag = false;
								}
								if(cr.De_reg_date != null && cr.De_reg_date.length() > 0){
									if(Imp.getTimestamp(cr.De_reg_date) != null && sdf.parse(cr.De_reg_date).getTime() <  sdf.parse(cr.Expiry_date).getTime()){
										log.add("[Error] Line " + lineno + ": De-reg date cannot be earlier Expiry date.");
										flag = false;
									}
								}
								
							}else{
								if(cr.De_reg_date != null && cr.De_reg_date.length() > 0){
									log.add("[Error] Line " + lineno + ": Expiry date of all CPT/D groups cannot be null if De-reg date is not null.");
									flag = false;
								}
							}
							break;
						default:
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
                    map.put("reg_date",  imp.getTimestamp(cr.Reg_date) );
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
                        cpdRegistration2.setCr_reg_datetime(imp.getTimestamp(cr.Reg_date));
                        if(cr.De_reg_date!=null && cr.De_reg_date.length()>0){
                            cpdRegistration2.setCr_de_reg_datetime(imp.getTimestamp(cr.De_reg_date));
                        }
                        
                        isInfoRegistrationBydate = isInfoRegistrationBydate(prof,cpdRegistration2);//检查大牌报名记录时间是否重叠
                        if(isInfoRegistrationBydate){//已重叠
                            log.add("[Error] Line " + lineno + ": User "+cr.user_id+" already registers in license "+cr.License_type+" (Registration date: "+cr.Reg_date+" ).");
                            flag = false;
                        }
                    }
                }
                
				if(flag){
					successNumber++;
				}else{
					flag = true;
					errorNumber++;
				}
				totalNumber++;
				lineno++;
				crs.add(cr);
			}
			model.addAttribute("errorLog", log);
			model.addAttribute("errorNumber", errorNumber);
			model.addAttribute("totalNumber", totalNumber);
			model.addAttribute("successNumber", successNumber);
			model.addAttribute("scrFile", srcFile.getAbsolutePath());
			model.addAttribute("down_file", "/temp/" + srcFile.getName());
			model.addAttribute("upload_desc", cpdRegistration.upload_desc);
         }catch(Exception e){
        	 srcFile.delete();
        	 e.printStackTrace();
         	CommonLog.error(e.getMessage(),e);
         	throw new cwSysMessage(e.getMessage());
         }
         //获取上传限制用户最大数
     	int maxUploadCount = wizbini.cfgSysSetupadv.getUserBatchUpload().getMaxUploadCount();
     	//如果大于上传用户最大数
     	if (crs.size() > maxUploadCount){
     		throw new cwSysMessage(UploadUtils.SMSG_ULG_UPLOAD_COUNT_EXCEED_LIMIT, new Integer(maxUploadCount).toString());
     	}
		
	}


	public Map<String, Object> upload_result(Model model, WizbiniLoader wizbini,
			loginProfile prof, qdbEnv static_env,
			CpdRegistration cpdRegistration) throws IOException, cwException, NamingException, SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
    	File toFolder=new File(static_env.DOC_ROOT+cwUtils.SLASH+static_env.LOG_FOLDER+cwUtils.SLASH+IMSLog.FOLDER_CPDLOG);
    	if(!toFolder.exists()){
    		toFolder.mkdir();
    	}
    	Map<String, Object> map = new HashMap<String, Object>();
    	File uploadFile = new File(cpdRegistration.srcFile);
    	File toFile=new File(toFolder,uploadFile.getName());
    	ImportCPDFromFile.uploadFile.put(prof,toFile.getName());
    	ImportCPDFromFile.uploadDesc.put(prof, cpdRegistration.upload_desc);
    	//将上传文件剪切到日志的文件夹下
    	dbUtils.moveFile(uploadFile.getPath(), toFile.getPath());
    	// 导入注册记录
		cwSQL sqlCon = new cwSQL();
		sqlCon.setParam(wizbini);
		Connection con = sqlCon.openDB(false);
		ImportCPDFromFile ImportCPDFromFile = new ImportCPDFromFile();
		ImportCPDFromFile.static_env = static_env;
		ImportCPDFromFile.wizbini = wizbini;
		ImportCPDFromFile.run(con);
		map = ImportCPDFromFile.resultData;
		con.commit();
		con.close();
		return map;
	}


	public void upload_model(Model model, WizbiniLoader wizbini,
			loginProfile prof, qdbEnv static_env,
			CpdRegistration cpdRegistration) {
		
		
	}


	public void upload_check(Model model, WizbiniLoader wizbini,
			loginProfile prof, qdbEnv static_env,
			CpdRegistration cpdRegistration) {
		
		List<DbIMSLog> dms = cpdRegistrationMapper.getIMSLog();
		IMSLog iLog = new IMSLog();
		iLog.CPD=true;
		for (int i = 0; i < dms.size(); i++) {
		    DbIMSLog  imsLog = dms.get(i);
            try {
                Hashtable h_file_uri = IMSLog.getLogFilesURI(imsLog.ilg_id, static_env);
                imsLog.success_txt =  (String)h_file_uri.get("success.txt");
                imsLog.failure_txt =  (String)h_file_uri.get("failure.txt");
                
            } catch (cwException e) {
                e.printStackTrace();
            }
            
        }
		iLog.CPD=false;
		model.addAttribute("dms", dms);
	}


    public Page<com.cwn.wizbank.entity.ImsLog> searchAll(Page<ImsLog> page,loginProfile prof,qdbEnv static_env) {
        page.getParams().put("ilg_type", "CPD");
        page.getParams().put("ilg_process", "IMPORT");
        page.getParams().put("current_role", prof.current_role);
        page.getParams().put("root_ent_id", prof.root_ent_id);
        page.getParams().put("my_top_tc_id", prof.my_top_tc_id);
        imsLogMapper.searchAll(page);
        List<ImsLog> list =  page.getResults();
        IMSLog log = new IMSLog();//日志工具类
        log.CPD=true;
        for (int i = 0; i < list.size(); i++) {
            ImsLog  imsLog = list.get(i);
            try {
                imsLog.file_uri = log.getUploadedFileURI(imsLog.ilg_id,imsLog.ilg_filename, static_env);

                Hashtable h_file_uri = log.getLogFilesURI(imsLog.ilg_id, static_env);
                for(int k=0; k<3; k++){
                    if(log.LOG_LIST[k].equals("SUCCESS")){
                        imsLog.success_file_uri = (String)h_file_uri.get(log.FILENAME_LIST[k]);
                    }else if(log.LOG_LIST[k].equals("UNSUCCESS")){
                        imsLog.unsuccess_file_uri = (String)h_file_uri.get(log.FILENAME_LIST[k]);
                    }else if(log.LOG_LIST[k].equals("ERROR")){
                        imsLog.error_file_uri = (String)h_file_uri.get(log.FILENAME_LIST[k]);
                    }
                }
                
            } catch (cwException e) {
                e.printStackTrace();
            }
        }
        log.CPD=false;
        return page;
    }
    
    
    
    
    
    
    
    
    
}
