package com.cwn.wizbank.cpd.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.cpd.service.AeItemCPDItemService;
import com.cwn.wizbank.cpd.service.CpdGroupService;
import com.cwn.wizbank.cpd.service.CpdLrnAwardRecordService;
import com.cwn.wizbank.cpd.service.CpdUtilService;
import com.cwn.wizbank.cpd.utils.CpdUtils;
import com.cwn.wizbank.cpd.vo.CpdLrnAwardRecordVO;
import com.cwn.wizbank.entity.AeItemCPDGourpItem;
import com.cwn.wizbank.entity.CpdGroup;
import com.cwn.wizbank.entity.CpdLrnAwardRecord;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.security.annotation.HasPermission;
import com.cwn.wizbank.utils.CommonLog;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.Page;


/**
 * cpd获得时数
 * @author Nat
 *
 */
@RequestMapping("admin/cpdLrnAwardRecord")
@Controller
public class CpdLrnAwardRecordController {

	@Autowired
	CpdLrnAwardRecordService cpdLrnAwardRecordService;
	
	@Autowired
	AeItemCPDItemService aeItemCPDItemService;
	
	@Autowired
	CpdGroupService cpdGroupService;
	
	@Autowired
	CpdUtilService cpdUtilService;
	
	/**
	 * cpt/d获得时数集合
	 * @param prof
	 * @param model
	 * @param page
	 * @param itm_id
	 * @param search_txt
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("listJson")
	@HasPermission(AclFunction.FTN_AMD_ITM_COS_MAIN_VIEW)
	@ResponseBody
	public String listJson(loginProfile prof, Model model,Page<CpdLrnAwardRecord> page,
			@RequestParam(required=true , value="itm_id")long itm_id ,
			@RequestParam(required=false , value="search_txt")String search_txt) throws Exception {
		//拿出该课程的报名记录
		cpdLrnAwardRecordService.searchAll(page,itm_id,search_txt);
		List<CpdGroup> list = cpdGroupService.getAllOrder(0);
		Map awardCPDGourpItemMap = aeItemCPDItemService.getCPDGourpItemMap(itm_id);//返回课程小牌MAP
		//获得课程里所有用户的获得CPD记录
		List<CpdLrnAwardRecord> recordlist = page.getResults();
		List<CpdLrnAwardRecordVO> returnList = null;
		if(null!=recordlist && recordlist.size()>0){
			returnList = new ArrayList<CpdLrnAwardRecordVO>();
			for(CpdLrnAwardRecord record : recordlist){//用户记录
				CpdLrnAwardRecordVO vo = CpdLrnAwardRecordVO.entity2Vo(record);
				Map<String,Float> hoursMap = new HashMap<String,Float>();
				//int hours_index = 0;
				for(CpdGroup cg : list){
					//如果该课程注册了小牌且需要时数不为0才显示
					if(awardCPDGourpItemMap.containsKey(cg.getCg_id())){
						AeItemCPDGourpItem acgi =  (AeItemCPDGourpItem)awardCPDGourpItemMap.get(cg.getCg_id());
						if(null!=acgi.getAcgi_award_core_hours() && 
								(acgi.getAcgi_award_core_hours()>0f 
										|| (cg.getCg_contain_non_core_ind()==1 && acgi.getAcgi_award_non_core_hours()>0f))
							){
							Float coreHours = 0f;
							Float nonCoreHours = 0f;
							//获得当前循环用户的CPD记录
							List<CpdLrnAwardRecord> userAwardList = record.getCpdLrnAwardRecordList();
							for(CpdLrnAwardRecord ur : userAwardList){
								
								//如果该记录对应课程的CPD牌照ID
								if(ur.getClar_cg_id().equals(acgi.getAcgi_cg_id())){
									coreHours = ur.getClar_award_core_hours();
									if(cg.getCg_contain_non_core_ind()==1 && acgi.getAcgi_award_non_core_hours()>0){
										if(null!=ur.getClar_award_non_core_hours()){
											nonCoreHours =ur.getClar_award_non_core_hours();
										}
									}
								}
							}
							
							//当核心时数大于0才显示
							if(coreHours>0f){
								hoursMap.put(cg.getCg_id()+CpdUtils.CPD_CORE_HEADER_KEY, coreHours);
							}
							
							if(cg.getCg_contain_non_core_ind()==1 && acgi.getAcgi_award_non_core_hours()>0){
								hoursMap.put(cg.getCg_id()+CpdUtils.CPD_NON_CORE_HEADER_KEY, nonCoreHours);
							}
						}

					}
				}
				vo.setHoursMap(hoursMap);
				returnList.add(vo);
			}
		}
		Page<CpdLrnAwardRecordVO> reurnPage = new Page<CpdLrnAwardRecordVO>();
		reurnPage.setResults(returnList);
		reurnPage.setPageNo(page.getPageNo());
		reurnPage.setPageSize(page.getPageSize());
		reurnPage.setParams(page.getParams());
		reurnPage.setSortname(page.getSortname());
		reurnPage.setSortorder(page.getSortorder());
		reurnPage.setTotalPage(page.getTotalPage());
		reurnPage.setTotalRecord(page.getTotalRecord());

		return JsonFormat.format(model, reurnPage);
	}
	
	
	
	@RequestMapping("reCalAllAwardCpd")
	@HasPermission(AclFunction.FTN_AMD_ITM_COS_MAIN_VIEW)
	@ResponseBody
	public String reCalAllAwardCpd(loginProfile prof, 
			@RequestParam(required=true , value="itm_id")long itm_id ){
		Map result = new HashMap<String,String>();
		try{
			cpdUtilService.reCalAllAward(itm_id, prof.getUsr_ent_id());
			result.put("result", "OK");
		}catch(Exception ex){
			CommonLog.error(ex.getMessage(),ex);
			ex.printStackTrace();
			result.put("result", "FAIL");
		}
		return JsonFormat.format(result);
	}

	/**
	 * 通过报名记录获取学员获得时数
	 * @param prof
	 * @param model
	 * @param page
	 * @param itm_id
	 * @param app_id
	 * @param usr_id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getInfoByAppID")
	@HasPermission(AclFunction.FTN_AMD_ITM_COS_MAIN_VIEW)
	@ResponseBody
	public String getInfoByAppID(loginProfile prof, Model model,Page<CpdLrnAwardRecord> page,
			@RequestParam(required=true , value="itm_id")long itm_id ,
			@RequestParam(required=true , value="app_id")long app_id,
			@RequestParam(required=true , value="usr_id")long usr_id) throws Exception {
		Map<String,Object> result = new HashMap<String,Object>();
		List<CpdLrnAwardRecord>  list = cpdLrnAwardRecordService.searchAllByAppId(app_id, usr_id, itm_id);
		result.put("info", list.get(0));	
		return JsonFormat.format(result);
	}
	
	/**
	 * 手动修改学员获得cpt/d时数
	 * @param model
	 * @param prof
	 * @param cpdLrnAwardRecord
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("updateCpdLrnAwardRecord")
	@HasPermission(AclFunction.FTN_AMD_ITM_COS_MAIN_VIEW)
	@ResponseBody
	public String updateCpdLrnAwardRecord(Model model, loginProfile prof,CpdLrnAwardRecord cpdLrnAwardRecord) throws Exception{
		Map<String, Object> result = new HashMap<String,Object>();
		try {
			cpdLrnAwardRecordService.updateCpdLrnAwardRecord(prof, cpdLrnAwardRecord);
			result.put("success", true);
		} catch (Exception e) {
			result.put("success", false);
			e.printStackTrace();
		}
		
		
		return JsonFormat.format(result);
	}
	
	

    /**
     * 导出
     * @param prof
     * @param cpdLrnAwardRecord
     * @param wizbini
     * @param model
     * @return
     * @throws Exception
     */
	@RequestMapping("export")
	@ResponseBody
	public Model export(loginProfile prof, @RequestParam(required=true , value="itm_id")long itm_id ,
			@RequestParam(required=false , value="search_txt")String search_txt, WizbiniLoader wizbini
			,Model model)throws Exception{
		
		String fileName = cpdLrnAwardRecordService.export(prof,itm_id,search_txt,prof.cur_lan,wizbini);
		model.addAttribute("fileUri","/temp/"+fileName);
		return model;
	}
	
}
