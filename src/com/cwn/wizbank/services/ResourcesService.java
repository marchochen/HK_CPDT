package com.cwn.wizbank.services;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.CourseMeasurement;
import com.cwn.wizbank.entity.CourseModuleCriteria;
import com.cwn.wizbank.entity.ModuleEvaluation;
import com.cwn.wizbank.entity.Resources;
import com.cwn.wizbank.persistence.CourseMeasurementMapper;
import com.cwn.wizbank.persistence.CourseModuleCriteriaMapper;
import com.cwn.wizbank.persistence.ResourcesMapper;
import com.cwn.wizbank.utils.LabelContent;
import com.cwn.wizbank.utils.Page;

/**
 *  service 实现
 */
@Service
public class ResourcesService extends BaseService<Resources> {

	@Autowired
	ForCallOldAPIService forCallOldAPIService;
	
	@Autowired
	ResourcesMapper resourcesMapper;
	@Autowired
	CourseMeasurementMapper courseMeasurementMapper;
	@Autowired
	CourseModuleCriteriaMapper courseModuleCriteriaMapper;
	@Autowired
	TcTrainingCenterService tcTrainingCenterService;
	public void setResourcesMapper(ResourcesMapper resourcesMapper){
		this.resourcesMapper = resourcesMapper;
	}
	
	
	
	public List<Resources> getCosContentList(long cos_res_id, long tkh_id, String label_lan, boolean itm_create_run_ind, Date itm_eff_start_datetime, Date att_create_timestamp, int mod_mobile_ind, String usr_id) throws Exception{

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("cos_res_id", cos_res_id);
		map.put("tkh_id", tkh_id);
		map.put("label_lan", LabelContent.getCurrentEncoding(label_lan));
		map.put("mod_mobile_ind", mod_mobile_ind);
		map.put("usr_id", usr_id);
		List<Resources> list  = null;
		// 取到该课程下的所有在线模块
		if( tkh_id > 0) {
			list = resourcesMapper.getCosContentWithLrn(map);
			
		} else {
			list = resourcesMapper.getCosContent(map);
		}
		for(int i=0; i<list.size(); i++) {
            // 判断课程是否为结训课程
            List<CourseMeasurement> courseMeasurementList = courseMeasurementMapper.getCourseMeasurementByCmtTitle(list.get(i).getRes_id() + "");
            List<CourseModuleCriteria> courseModuleCriteriaList = courseModuleCriteriaMapper.getCourseModuleCriteriaByResId(list.get(i).getRes_id() + "");
            if(courseMeasurementList.size() > 0) {
                // 是结训课程(网上 & 独立课程)
                String status = "";
                for(int j=0; j<courseMeasurementList.size(); j++) {
                    if(j != courseMeasurementList.size() - 1) status += "[" + courseMeasurementList.get(j).getCmt_cmr_status() + "], ";
                        else status += "[" + courseMeasurementList.get(j).getCmt_cmr_status() + "]";
                }
                list.get(i).setTrainingStatus(status);
                list.get(i).IsTrainingCourse(true);
            }
            if(courseModuleCriteriaList.size() > 0) {
                // 是结训课程(统一课程)
                String status = "";
                for(int j=0; j<courseModuleCriteriaList.size(); j++) {
                    if(j != courseModuleCriteriaList.size() - 1) status += "[" + courseModuleCriteriaList.get(j).getCmr_status() + "], ";
                    else status += "[" + courseModuleCriteriaList.get(j).getCmr_status() + "]";
                }
                list.get(i).setTrainingStatus(status);
                list.get(i).IsTrainingCourse(true);
            }
        }
		

		if (list != null && list.size() > 0) {
			if(tkh_id > 0){
				for (Resources res : list) {
					if (res.getRes_subtype() != null && res.getRes_subtype().equalsIgnoreCase("ASS")) {
						// 如果是作业,则先计算作业的提交期限
						int ass_due_date_day = res.getMod().getAss_due_date_day();
						Date ass_due_datetime = res.getMod().getAss_due_datetime();
						Date due_date = getDueDate(ass_due_date_day, ass_due_datetime, itm_create_run_ind, itm_eff_start_datetime, att_create_timestamp);
						if (due_date != null) {
							res.getMod().setAss_due_datetime(due_date);
						}
					}
	
					if (res.getMod().getRrq_req_res_id() != null && res.getMod().getRrq_req_res_id() > 0) {
						// 是否已完成先修模块
						res.getMod().setPre_mod_had_completed(hasCompletePreMod(res.getMod().getRrq_req_res_id(), res.getMod().getRrq_status(), res.getMod().getPre_mod_mov_status()));
					}
					
					if(res.getMov() != null){
						if(StringUtils.isNotEmpty(res.getMov().getMov_status())){
							//res.getMov().setMov_status(CwnUtil.getAppStatusStr(res.getMov().getMov_status(), label_lan));
						}
						if (tkh_id > 0 && res.getRes_subtype() != null && (res.getRes_subtype().equalsIgnoreCase("DXT") || res.getRes_subtype().equalsIgnoreCase("TST"))) {
							//如果是试卷，刚先判断是否是要还原原来保存的记录。
							if(forCallOldAPIService. chkforExist(null, tkh_id, res.getRes_id())){
								res.setIsRestore(true);
							}	
						}
					}
	
				}
			}
		}
		return list;

	}
	
	public Date getDueDate(long ass_due_date_day, Date ass_due_datetime, boolean itm_create_run_ind, Date itm_eff_start_datetime, Date att_create_timestamp) {
		Date result = null;
		if (ass_due_date_day > 0) {
			Date baseTs = null;
			if (itm_create_run_ind) {
				baseTs = itm_eff_start_datetime;
			} else {
				baseTs = att_create_timestamp;
			}
			if (baseTs != null) {
				Calendar dateCal = Calendar.getInstance();
				dateCal.setTime(baseTs);
				dateCal.add(Calendar.DAY_OF_MONTH, (int) ass_due_date_day);
				result = (java.util.Date) dateCal.getTime();
			}
		} else {
			result = ass_due_datetime;
		}
		return result;
	}
	
	public boolean hasCompletePreMod(long rrq_req_res_id, String pre_status, String mov_status)  {
        boolean result = true;
        if(rrq_req_res_id > 0){
            if (mov_status != null ) {
                if (pre_status != null) {
                    if (pre_status.equalsIgnoreCase(ModuleEvaluation.SING_PROGRESS)) {
                        if (mov_status.equalsIgnoreCase(ModuleEvaluation.SING_PROGRESS) || mov_status.equalsIgnoreCase(ModuleEvaluation.SING_FAIL) || mov_status.equalsIgnoreCase(ModuleEvaluation.SING_COMPLETE) || mov_status.equalsIgnoreCase(ModuleEvaluation.SING_PASS)) {

                        } else {
                       	 result = false;
                        }
                    }
                    if (pre_status.equalsIgnoreCase(ModuleEvaluation.SING_FAIL)) {
                        if (mov_status.equalsIgnoreCase(ModuleEvaluation.SING_FAIL) || mov_status.equalsIgnoreCase(ModuleEvaluation.SING_COMPLETE) || mov_status.equalsIgnoreCase(ModuleEvaluation.SING_PASS)) {

                        } else {
                       	 result = false;
                        }
                    }
                    if (pre_status.equalsIgnoreCase(ModuleEvaluation.SING_COMPLETE)) {
                        if (mov_status.equalsIgnoreCase(ModuleEvaluation.SING_COMPLETE) || mov_status.equalsIgnoreCase(ModuleEvaluation.SING_PASS)) {

                        } else {
                       	 result = false;
                        }
                    }
                    if (pre_status.equalsIgnoreCase(ModuleEvaluation.SING_PASS)) {
                        if ((mov_status.equalsIgnoreCase(ModuleEvaluation.SING_PASS))) {

                        } else {
                       	 result = false;
                        }
                    }
                }
            } else {
                result = false;
            }
   	 }
        return result;

    }
	
	/**
	 * 获取调查问卷列表
	 * @param usr_ent_id 用户id
	 * @return
	 */
	public Page<Resources> getMyEvaluation(long usr_ent_id,int status,Page<Resources> page){
		page.getParams().put("usr_ent_id", usr_ent_id);
		page.getParams().put("curTime", getDate());	
		if(page.getParams().get("status")==null){
			page.getParams().put("status", status);	
		}
		if(page.getParams().get("isMobile") != null && page.getParams().get("isMobile").equals("true")){			
			page.getParams().put("isMobile", 1);
		}
		resourcesMapper.selectMyEvaluation(page);		
		return page;
	}
	
	/**
	 * 获取调查问卷列表
	 * @param usr_ent_id 用户id
	 * @return
	 */
	public int getMyEvaluationCount(long usr_ent_id, int status, int isMobile){
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("usr_ent_id", usr_ent_id);
		map.put("curTime", getDate());	
		map.put("status", status);
		map.put("isMobile", isMobile);
		Integer count = resourcesMapper.selectMyEvaluationCount(map);
		if(count != null) {
			return count;
		}
		return 0;		
	}
	
	
	public Page<Resources> getModulePage(Page<Resources> page, long userEntId, String resSubType, String acRole){
		page.getParams().put("userEntId", userEntId);
		page.getParams().put("acRole", acRole);
		if(StringUtils.isNotEmpty(resSubType)) {
			page.getParams().put("resSubTypes", Arrays.asList(resSubType.split(",")));
		}
		this.resourcesMapper.selectModuleList(page);
		return page;
	}
	
	
	public long getModuleAppCount(long userEntId, String resSubType, String acRole) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userEntId", userEntId);
		map.put("acRole", acRole);
		if(StringUtils.isNotEmpty(resSubType)) {
			map.put("resSubTypes", Arrays.asList(resSubType.split(",")));
		}
		Page<Resources> page = new Page<Resources>();
		page.setParams(map);
		this.resourcesMapper.selectModuleList(page);
		
		return page.getTotalRecord();
	}
	
	public List<Resources> getSubmitNum(Map<String,Object> map){
	   return resourcesMapper.getSubmitNum(map);
	}
	
}