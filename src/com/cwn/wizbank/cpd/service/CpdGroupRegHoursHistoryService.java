package com.cwn.wizbank.cpd.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.cpd.vo.IndividualReportVo;
import com.cwn.wizbank.entity.Certificate;
import com.cwn.wizbank.entity.CpdGroupRegHoursHistory;
import com.cwn.wizbank.persistence.CpdGroupRegHoursHistoryMapper;
import com.cwn.wizbank.services.BaseService;
import com.cwn.wizbank.utils.Page;

@Service
public class CpdGroupRegHoursHistoryService  extends BaseService<CpdGroupRegHoursHistory>{
	
	@Autowired
	CpdGroupRegHoursHistoryMapper cpdGroupRegHoursHistoryMapper;
	
	public List<CpdGroupRegHoursHistory>   getCpdGroupRegHoursHistory(Long usrEntId , Long cgId , int period,long cgrId){
		Map param = new HashMap<String,Object>();
		param.put("usr_ent_id", usrEntId);
		param.put("cg_id", cgId);
		param.put("period", period);
		param.put("cgr_id", cgrId);
		return cpdGroupRegHoursHistoryMapper.getCpdGroupRegHoursHistory(param);
	}
	
	public List<IndividualReportVo> getRecordForIndividualReport(Long usr_ent_id , Long ct_id,int sort ,Integer period ){
        Map<String ,Object> map = new HashMap<String, Object>();
        map.put("usr_ent_id", usr_ent_id);
        map.put("ct_id", ct_id);
        map.put("period", period);
        if(0==sort){
        	map.put("orderTitle", 1);
        }else{
        	map.put("orderAwardTime", 1);
        }
        
        return cpdGroupRegHoursHistoryMapper.getRecordForIndividualReport(map);
	}
	
	
	public List<CpdGroupRegHoursHistory> getHistoryView(Page<CpdGroupRegHoursHistory> page,String searchText ,long ct_id,int period){
		page.getParams().put("ct_id", ct_id);
		page.getParams().put("period", period);
		page.getParams().put("searchText", searchText);
		return cpdGroupRegHoursHistoryMapper.getHistoryView(page);
	}
	
	
	public Long getCghiID(CpdGroupRegHoursHistory cpdGroupRegHoursHistory){
		return cpdGroupRegHoursHistoryMapper.getCghiID(cpdGroupRegHoursHistory);
	}
	
}
