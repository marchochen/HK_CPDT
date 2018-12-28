package com.cwn.wizbank.cpd.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.cpd.utils.CpdUtils;
import com.cwn.wizbank.cpd.vo.CpdPeriodVO;
import com.cwn.wizbank.entity.CpdGroupRegHours;
import com.cwn.wizbank.entity.CpdGroupRegistration;
import com.cwn.wizbank.persistence.CpdGroupRegHoursMapper;
import com.cwn.wizbank.persistence.CpdGroupRegistrationMapper;
import com.cwn.wizbank.services.BaseService;
import com.cwn.wizbank.utils.DateUtil;
import com.cwn.wizbank.utils.Page;




@Service
public class CpdGroupRegistrationService extends BaseService<CpdGroupRegistration> {

	@Autowired
	CpdGroupRegistrationMapper cpdGroupRegistrationMapper;
	
	@Autowired
	CpdGroupRegHoursMapper cpdGroupRegHoursMapper;

	@Autowired
	CpdUtilService cpdUtilService;

	
    public List<CpdGroupRegistration> getCpdGroupRegByCrId(Long crId){
    	Map map = new HashMap<String,Object>();
    	map.put("cgr_cr_id", crId);
    	return cpdGroupRegistrationMapper.getCpdGroupReg(map);
    }


    public List<CpdGroupRegHours> getCpdGroupRegHours(Long cgrId){
    	return cpdGroupRegHoursMapper.getByCgrId(cgrId);
    }

	/**
	 * 获取当前时间，小牌注册信息
	 * @return
	 */
	public List<CpdGroupRegistration>  getByNowDate(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("now_datetime",DateUtil.getInstance().getDate(new Date(), 0, 0,0));
		List<CpdGroupRegistration> list = cpdGroupRegistrationMapper.getByNowDate(params);
		return list;
	}
    

	/**
	 * 查询用户在该大牌下注册的小牌列表
	 * @param page
	 * @param cgr_usr_ent_id
	 * @param cgr_cr_id
	 * @param ct_id
	 * @return
	 */
    public Page<CpdGroupRegistration> getByUsrEntIdAndCrId(Page<CpdGroupRegistration> page, long cgr_usr_ent_id,long cgr_cr_id,long ct_id) {
        page.getParams().put("usr_ent_id", cgr_usr_ent_id);
        page.getParams().put("cgr_cg_id", cgr_cr_id);
        page.getParams().put("status", CpdUtils.STATUS_OK);
        
        CpdPeriodVO cpdPeriodVO =cpdUtilService.getCurrentPeriod(ct_id);
        page.getParams().put("period", cpdPeriodVO.getPeriod());
        cpdGroupRegistrationMapper.getByUsrEntIdAndCrId(page);
        //page.getResults().get(0).getCpdGroup();
        return page;
    }

    public List<CpdGroupRegistration> getAllCpdGroupReg(){
    	return cpdGroupRegistrationMapper.getCpdGroupReg(new HashMap());
    }

    /**
     * 手动修改注册小牌用户 核心/非核心时数
     * @param prof
     * @param core_hours
     * @param non_core_hours
     * @param cgrh_id
     */
    public void updateGroupRegHours(loginProfile prof,Float core_hours,Float non_core_hours,long cgrh_id){
    	CpdGroupRegHours cpdGroupRegHours = new CpdGroupRegHours();
    	cpdGroupRegHours.setCgrh_id(cgrh_id);
    	cpdGroupRegHours.setCgrh_manul_core_hours(core_hours);
    	cpdGroupRegHours.setCgrh_manul_non_core_hours(non_core_hours);
    	cpdGroupRegHours.setCgrh_execute_core_hours(core_hours);
    	cpdGroupRegHours.setCgrh_execute_non_core_hours(non_core_hours);
    	cpdGroupRegHours.setCgrh_manul_ind(1);
    	
    	cpdGroupRegHoursMapper.updateHours(cpdGroupRegHours);
    }
    
    /**
     * 判断牌照是否有学员注册
     * @param cpdType
     * @return
     */
	public boolean getCountByCgID(long cg_id) {
		boolean flg =false;
		int count = cpdGroupRegistrationMapper.getCountByCgID(cg_id);
		if(count > 0){
			flg = true;
		}
		return flg;
	}
	
	/**
	 * 获取CpdGroupRegistration
	 * @param cg_id
	 * @param period
	 * @return
	 */
	public List<CpdGroupRegistration> getByCgId(long cg_id,int period){
		Map map = new HashMap<String,Object>();
		map.put("period", period);
		map.put("cg_id", cg_id);
		map.put("status", CpdUtils.STATUS_OK);
		return cpdGroupRegistrationMapper.getCpdGroupRegistration(map);
	}
	
	public List<CpdGroupRegistration> getByCpdGroupRegistration(long cg_id,int period,Long usrEntId){
		Map map = new HashMap<String,Object>();
		map.put("period", period);
		map.put("cg_id", cg_id);
		map.put("usr_ent_id", usrEntId);
		map.put("status", CpdUtils.STATUS_OK);
		return cpdGroupRegistrationMapper.getCpdGroupRegistration(map);
	}
	
	public CpdGroupRegHours getCpdGroupRegHours(Long crId , Long cgId , Long usrEntId , int period){
		Map map = new HashMap<String,Object>();
		map.put("cr_id", crId);
		map.put("cg_id", cgId);
		map.put("usr_ent_id", usrEntId);
		map.put("period", period);
		List<CpdGroupRegHours> hours = cpdGroupRegHoursMapper.getCpdGroupRegHours(map);
		if(null!=hours && hours.size()>0){
			return hours.get(0);
		}
		return null;
	}
	
}
