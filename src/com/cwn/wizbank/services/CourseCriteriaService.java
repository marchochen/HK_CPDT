package com.cwn.wizbank.services;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cw.wizbank.qdb.dbModule;
import com.cw.wizbank.qdb.dbProgress;
import com.cwn.wizbank.entity.CourseCriteria;
import com.cwn.wizbank.entity.CourseMeasurement;
import com.cwn.wizbank.entity.Module;
import com.cwn.wizbank.persistence.CourseCriteriaMapper;
import com.cwn.wizbank.persistence.CourseMeasurementMapper;
import com.cwn.wizbank.persistence.ModuleMapper;
/**
 *  service 实现
 */
@Service
public class CourseCriteriaService extends BaseService<CourseCriteria> {

	@Autowired
	CourseCriteriaMapper courseCriteriaMapper;
	
	@Autowired
	CourseMeasurementMapper courseMeasurementMapper;
	
	@Autowired
	ModuleService moduleService;
	
	@Autowired
	ProgressService progressService;

	public void setCourseCriteriaMapper(CourseCriteriaMapper courseCriteriaMapper){
		this.courseCriteriaMapper = courseCriteriaMapper;
	}
	
    public CourseCriteria getItmCcrWithLrn(long itm_id, long tkh_id){
    	//取课程下的结训条件及积分项目
    	CourseCriteria ccr = this.courseCriteriaMapper.getByItm(itm_id);
    	List<CourseMeasurement> cmt_lst_all = null;
    	
    	Map<String, Object> map = new HashMap<String, Object>();
    	if(tkh_id > 0){
    		map.put("itm_id", itm_id);
    		map.put("tkh_id", tkh_id);
    		map.put("ccr_id", ccr.getCcr_id());
    		//取课程下的结训条件细项和积分项目，及学员在该细项目的结果
    		cmt_lst_all = courseMeasurementMapper.getCourseMeasurementWithLrn(map);
    	}else{
    		//取课程下的结训条件细项和积分项目，
    		cmt_lst_all = courseMeasurementMapper.getCourseMeasurement(ccr.getCcr_id());
    	}
    	
    	if(cmt_lst_all != null && cmt_lst_all.size() > 0 ){
    		List<CourseMeasurement> cmt_lst_condition = new ArrayList<CourseMeasurement>();
        	List<CourseMeasurement> score_itm_lst = new ArrayList<CourseMeasurement>();
    		for(CourseMeasurement cmt :cmt_lst_all){
    			if(cmt != null && cmt.getCmt_is_contri_by_score() > 0){
    				//该设置是否一个积分项目
    				if(tkh_id > 0){
    					if(cmt.getMov() != null && cmt.getCmt_pass_score() != null && cmt.getMov().getMov_score() != null 
    							&& cmt.getMov().getMov_score().floatValue() >= cmt.getCmt_pass_score().floatValue() ){
    						//在该积分项目下已及格
    						cmt.setCmt_lrn_pass_ind(true);
    					}
    				}
    				score_itm_lst.add(cmt);
    			} else if(cmt != null && cmt.getCmt_status() != null && cmt.getCmt_status().length() > 0){
    				//该设置是完成条件中的一个细项
    				if(tkh_id > 0){
    					int mod_id = 0;
    					if(cmt.getMov() != null && cmt.getMov().getMov_mod_id() != null){
    						mod_id = cmt.getMov().getMov_mod_id();
    					}
    					if(cmt.getMov() != null && cmt.getMov().getMov_status() != null && cmt.getMov().getMov_status().length() > 0 ){
    						//设置是否已通过了该细项的要求
    						cmt.setCmt_lrn_pass_ind(compStatus(cmt.getCmt_status(),cmt.getMov().getMov_status(),mod_id, tkh_id));
    					}
    					if(cmt.getMov() != null && StringUtils.isNotEmpty(cmt.getMov().getMov_status())){
    						//设置是否已通过了该细项的要求
    						cmt.setCmt_lrn_pass_ind(compStatus(cmt.getCmt_status(),cmt.getMov().getMov_status(), mod_id, tkh_id));
    					}
    				}
    				cmt_lst_condition.add(cmt);
    			}
    		}
    		//完成条件中的参求要求
    		ccr.setCmt_lst(cmt_lst_condition);
    		//完成条件中的积分项目
    		ccr.setScore_itm_lst(score_itm_lst);
    	}
    	
    	return ccr;
    }
    
	private boolean compStatus(String criteria,String current,long mod_id,long tkh_id){
		
		if( mod_id > 0){
			Module module = moduleService.getModTypeById(mod_id);
			
			if(module != null && module.getMod_type() != null && (dbModule.MOD_TYPE_TST.equalsIgnoreCase(module.getMod_type()) || dbModule.MOD_TYPE_DXT.equalsIgnoreCase(module.getMod_type()) || dbModule.MOD_TYPE_ASS.equalsIgnoreCase(module.getMod_type()))){
				if (criteria != null && criteria.indexOf(current)== -1){
					return false;
				}else{
					if("IFCP".equalsIgnoreCase(criteria)){
						if(progressService.getMaxAttemptNbr(mod_id, tkh_id) > 0){
							return true;
						}else{
							return false;
						}
					}else{
						return true;
					}
				}
			}
		}
		
		if (criteria != null && criteria.indexOf(current)== -1){
			return false;                   
		}else{
			return true;
		}
		
	}
}