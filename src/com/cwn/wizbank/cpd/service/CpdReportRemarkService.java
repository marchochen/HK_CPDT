package com.cwn.wizbank.cpd.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.cpd.vo.CpdPeriodVO;
import com.cwn.wizbank.cpd.vo.CpdReportRemarkVO;
import com.cwn.wizbank.entity.CpdReportRemark;
import com.cwn.wizbank.entity.CpdReportRemarkHistory;
import com.cwn.wizbank.persistence.CpdReportRemarkHistoryMapper;
import com.cwn.wizbank.persistence.CpdReportRemarkMapper;
import com.cwn.wizbank.services.BaseService;

@Service
public class CpdReportRemarkService   extends BaseService<CpdReportRemark>{
	
	@Autowired
	CpdReportRemarkMapper cpdReportRemarkMapper;
	
	@Autowired
	CpdReportRemarkHistoryMapper cpdReportRemarkHistoryMapper;
	
	@Autowired
	CpdUtilService cpdUtilService;
	
	public List<CpdReportRemark> findAll(){
		return cpdReportRemarkMapper.findAll();
	}
	
	public void saveOrUpdate(CpdReportRemark remark){
		Map map = new HashMap<String,Object>();
		map.put("code", remark.getCrpm_report_code());
		CpdReportRemark oriRemark = cpdReportRemarkMapper.getCpdReportRemark(map);
		if(null==oriRemark){
			cpdReportRemarkMapper.insert(remark);
		}else{
			oriRemark.setCrpm_report_code(remark.getCrpm_report_code());
			oriRemark.setCrpm_report_remark(remark.getCrpm_report_remark());
			oriRemark.setCrpm_update_usr_ent_id(remark.getCrpm_update_usr_ent_id());
			oriRemark.setCrpm_update_datetime(remark.getCrpm_update_datetime());
			cpdReportRemarkMapper.update(oriRemark);
		}
	}
	
	public void saveFromCpdReportRemarkVO(CpdReportRemarkVO vo ,Long optUsrId){
		try{
		saveOrUpdate(CpdReportRemarkVO.conver2Po(CpdReportRemark.OUTSTANDING_REMARK_CODE, vo.getOutstandingRemark(), optUsrId));
		saveOrUpdate(CpdReportRemarkVO.conver2Po(CpdReportRemark.INDIVIDUAL_REMARK_CODE, vo.getIndividualRemark(), optUsrId));
		saveOrUpdate(CpdReportRemarkVO.conver2Po(CpdReportRemark.AWARDED_REMARK_CODE, vo.getAwardedRemark(), optUsrId));
		saveOrUpdate(CpdReportRemarkVO.conver2Po(CpdReportRemark.LICENSE_REGISTRATION_CODE, vo.getLicenseRegRemark(), optUsrId));
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public List<CpdReportRemarkHistory> getCpdReportRemarkHistory(String code , Integer period,Integer month){
		Map map = new HashMap<String,Object>();
		map.put("code", code);
		map.put("period", period);
		map.put("month", month);
		return cpdReportRemarkHistoryMapper.getCpdReportRemarkHistory(map);
	}
	
	public void saveHistory(CpdReportRemark remark,int period,int month){
		CpdReportRemarkHistory hist = new CpdReportRemarkHistory();
		hist.setCrmh_crpm_id(remark.getCrpm_id());
		hist.setCrmh_report_code(remark.getCrpm_report_code());
		hist.setCrmh_report_remark(remark.getCrpm_report_remark());
		hist.setCrpm_his_create_datetime(new Date());
		hist.setCrpm_his_period(period);
		hist.setCrpm_his_save_month(month);
		cpdReportRemarkHistoryMapper.insert(hist);
	}
	
	public CpdReportRemark getCpdReportRemarkByCode(String code){
		if(StringUtils.isEmpty(code)){
			return null;
		}
		Map param = new HashMap<String,Object>();
		param.put("code", code);
		return cpdReportRemarkMapper.getCpdReportRemark(param);
	}
}
