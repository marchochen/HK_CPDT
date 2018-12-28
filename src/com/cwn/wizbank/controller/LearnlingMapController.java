package com.cwn.wizbank.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.entity.AeItem;
import com.cwn.wizbank.entity.Profession;
import com.cwn.wizbank.entity.RegUser;
import com.cwn.wizbank.entity.UserPosition;
import com.cwn.wizbank.entity.UserPositionCatalog;
import com.cwn.wizbank.entity.UserPositionLrnMap;
import com.cwn.wizbank.entity.UserPositionRelation;
import com.cwn.wizbank.entity.UserSpecialExpert;
import com.cwn.wizbank.entity.UserSpecialTopic;
import com.cwn.wizbank.entity.vo.AeItemVo;
import com.cwn.wizbank.entity.vo.GradeNodesVo;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.security.annotation.HasPermission;
import com.cwn.wizbank.services.AeItemService;
import com.cwn.wizbank.services.LiveItemService;
import com.cwn.wizbank.services.ProfessionLrnItemService;
import com.cwn.wizbank.services.ProfessionService;
import com.cwn.wizbank.services.UserPositionCatalogService;
import com.cwn.wizbank.services.UserPositionLrnMapService;
import com.cwn.wizbank.services.UserPositionRelationService;
import com.cwn.wizbank.services.UserPositionService;
import com.cwn.wizbank.services.UserSpecialExpertService;
import com.cwn.wizbank.services.UserSpecialTopicService;
import com.cwn.wizbank.utils.DateUtil;
import com.cwn.wizbank.utils.EncryptUtil;
import com.cwn.wizbank.utils.ImageUtil;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.LabelContent;
import com.cwn.wizbank.utils.Page;
import com.cwn.wizbank.utils.StringUtils;

/**
 * 前端 学习地图
 * <p>Title:LearnlingMapController</p>
 * <p>Description: </p>
 * @author halo.pan
 * @date 2016年4月12日 下午12:41:11
 *
 */
@Controller
@RequestMapping("learningmap")
@HasPermission(value={AclFunction.FTN_LRN_LEARNING_PROFESSION,AclFunction.FTN_AMD_SPECIALTOPIC_MAIN})
public class LearnlingMapController {
	@Autowired
	private UserPositionRelationService userPositionRelationService;
	@Autowired
	private UserPositionService userPositionService;
	@Autowired
	private UserPositionLrnMapService userPositionLrnMapService;
	@Autowired
	private UserPositionCatalogService userPositionCatalogService;
	@Autowired
	private AeItemService aeItemService;
	@Autowired
	private UserSpecialExpertService userSpecialExpertService;
	@Autowired
	private UserSpecialTopicService userSpecialTopicService;
	@Autowired
	private ProfessionLrnItemService professionLrnItemService;
	@Autowired
	private ProfessionService professionService;
	
	@Autowired 
	LiveItemService liveService;
	
	@RequestMapping(value = "index", method = RequestMethod.GET)
	public String index(Model model, loginProfile prof){
		
		return "learningmap/position";
	}
	
	@RequestMapping(value = "positionMapInfoJson")
	@ResponseBody
	 public String positionMapInfo(Model model, WizbiniLoader wizbini, loginProfile prof){
		String title="";
		boolean flag=false;//是否是关键岗位
		UserPositionRelation userPositionRelation=userPositionRelationService.get(prof.usr_ent_id);
		//关键岗位
		int cnt=userPositionLrnMapService.getCountById(null,"1",wizbini.cfgSysSetupadv.isTcIndependent(),prof.my_top_tc_id);
		
		if(userPositionRelation!=null){
		int recoed=userPositionLrnMapService.getCountById(String.valueOf(userPositionRelation.getUpr_upt_id()),"1",wizbini.cfgSysSetupadv.isTcIndependent(),prof.my_top_tc_id);
		if(recoed>0){   
			flag=true;
			model.addAttribute("upt_id",userPositionRelation.getUpr_upt_id());
		}
		UserPosition position=userPositionService.get(userPositionRelation.getUpr_upt_id());
			title=position.getUpt_title();
		}
		
		model.addAttribute("title",title);
		model.addAttribute("usr_img",prof.getUsr_photo());
		model.addAttribute("flag",flag);
		model.addAttribute("count",cnt);
		return JsonFormat.format(model);
	}
	
	@RequestMapping(value = "positionMapListJson")
	@ResponseBody
	public List<UserPositionLrnMap> positionMapList(Model model,WizbiniLoader wizbini,  loginProfile prof,Page<UserPositionLrnMap> page){
		page.setPageNo(1);
		page.setPageSize(6);
		page.setSortname("upm_seq_no");
		page.setSortorder("desc");
		page.getParams().put("upm_status", 1);
		page.getParams().put("usr_ent_id", prof.usr_ent_id);
		if(wizbini.cfgSysSetupadv.isTcIndependent()){
            page.getParams().put("top_tcr_id", prof.my_top_tc_id);
            }
		userPositionLrnMapService.getPositionMapFrontList(page);
		List<UserPositionLrnMap> list=page.getResults();
		for (UserPositionLrnMap userPositionLrnMap : list) {
			ImageUtil.combineImagePath(userPositionLrnMap);
		}
		return list;
	}
	
	@RequestMapping(value = "positionCatalogListJson")
	@ResponseBody
	public List<UserPositionCatalog> positionCatalogMapList(Model model, WizbiniLoader wizbini,loginProfile prof){
		List<String> uptIds=new ArrayList<String>(); 
		List<UserPositionLrnMap> positionLrnMaps=userPositionLrnMapService.getPositionFrontMap("1",null,wizbini.cfgSysSetupadv.isTcIndependent(),prof.my_top_tc_id);
		List<UserPositionCatalog> catalogs=userPositionCatalogService.getPositionMapCatalogList(wizbini.cfgSysSetupadv.isTcIndependent(),prof.my_top_tc_id);
		List<UserPosition> otherPositions=new ArrayList<UserPosition>();
		for (UserPositionLrnMap userPositionLrnMap : positionLrnMaps) {
			
			if(userPositionLrnMap.getUpc_id()==0){
				UserPosition other=userPositionService.get(userPositionLrnMap.getUpt_id());
				//对象不等于null 则添加
				if(other != null){
					otherPositions.add(other);
				}
			}else{
			uptIds.add(String.valueOf(userPositionLrnMap.getUpt_id()));
			}
		}
		String ids=null;
		if(uptIds.size()>0){
			 ids=StringUtils.listToString(uptIds,",");
		}
		 
		
		for (UserPositionCatalog userPositionCatalog : catalogs) {
			List<UserPosition> positions=userPositionService.getList(userPositionCatalog.getUpc_id(),ids);
			userPositionCatalog.setItems(positions);
		}
		if(otherPositions.size()>0){
			UserPositionCatalog catalog=new UserPositionCatalog();
			catalog.setItems(otherPositions);
			catalog.setUpc_title(LabelContent.get(prof.cur_lan, "label_lm.label_core_learning_map_84"));
			catalogs.add(catalog);
		}
		
		for (UserPositionCatalog userPositionCatalog : catalogs ) {
			userPositionCatalog.setNum(catalogs.size());
		}
		return catalogs;
	}
	
	@RequestMapping(value = "detail", method = RequestMethod.GET)
	 public String detail(WizbiniLoader wizbini,Model model, loginProfile prof,@RequestParam(value = "id", required = false, defaultValue = "0") String id){
		long encryp_id = 0;
		if(id != null && !id.equals(""))
        {
			encryp_id = EncryptUtil.cwnDecrypt(id);
        }
		
		UserPositionLrnMap lrnMap=userPositionLrnMapService.getByFieldName("upt_id", encryp_id);
		ImageUtil.combineImagePath(lrnMap);
		if(lrnMap.getUpt_desc()!=null&&lrnMap.getUpt_desc()!="")
		{
			lrnMap.setUpt_desc(lrnMap.getUpt_desc().replaceAll("\r\n", "<br/>").replaceAll("\n", "<br/>"));
		}
		List<UserPositionLrnMap> list=userPositionLrnMapService.getPositionMapNotOneList(lrnMap.getUpm_id(),wizbini.cfgSysSetupadv.isTcIndependent(),prof.my_top_tc_id );
		int otherNum = userPositionLrnMapService.getPositionMapNotOneListSize(lrnMap.getUpm_id(),wizbini.cfgSysSetupadv.isTcIndependent(),prof.my_top_tc_id );
		model.addAttribute("lrnMap",lrnMap);
		model.addAttribute("otherNum",otherNum);
          return "learningmap/positionDetail";
	 }
	
	/**
	 * 详细信息
	 * @param model
	 * @param prof
	 * @param page
	 * @param upm_id
	 * @return
	 */
	@RequestMapping(value = "positionMapDetailJson")
	@ResponseBody
	 public UserPositionLrnMap positionMapDetailJson(Model model, loginProfile prof,@RequestParam(value = "upm_id", required = false, defaultValue = "0") long upm_id){
		UserPositionLrnMap lrnMap=userPositionLrnMapService.getByFieldName("upm_id", upm_id);
		ImageUtil.combineImagePath(lrnMap);
		return lrnMap;
	}
	
	/**
	 * 岗位课程信息
	 * @param model
	 * @param prof
	 * @param page
	 * @param upm_id
	 * @return
	 */
	@RequestMapping(value = "positionCourseJson")
	@ResponseBody
	 public String positionCourseJson(Model model, WizbiniLoader wizbini, loginProfile prof,Page<AeItem> page,@RequestParam(value = "upm_id", required = false, defaultValue = "0") long upm_id,@RequestParam(value = "tkhId", required = false, defaultValue = "0") long tkhId){
		 if(wizbini.cfgSysSetupadv.isTcIndependent()){
	            page.getParams().put("top_tcr_id", prof.my_top_tc_id);
	            }
		aeItemService.getAeItemByPositionMap(page,upm_id,tkhId,prof.usr_ent_id,prof.my_top_tc_id,prof.current_role);
		return JsonFormat.format(model, page);
	}
	
	/**
	 * 相关岗位地图信息
	 * @param model
	 * @param prof
	 * @param upm_id
	 * @return
	 */
	@RequestMapping(value = "otherPositionJson")
	@ResponseBody
	public List<UserPositionLrnMap> otherPositionJson(Model model,WizbiniLoader wizbini, loginProfile prof,@RequestParam(value = "upm_id", required = false, defaultValue = "0") long upm_id){
		List<UserPositionLrnMap> list=userPositionLrnMapService.getPositionMapNotOneList(upm_id,wizbini.cfgSysSetupadv.isTcIndependent(),prof.my_top_tc_id );
		for (UserPositionLrnMap userPositionLrnMap : list) {
			ImageUtil.combineImagePath(userPositionLrnMap);
		}
		return list;
	}
	
	/**
	 *  学习地图:专题培训
	 */
	@RequestMapping(value = "specialTopic", method = RequestMethod.GET)
	 public String special(Model model, WizbiniLoader wizbini, loginProfile prof,Page<UserSpecialTopic> page){
		page.setPageNo(1);
		page.setPageSize(20);
		page.getParams().put("ust_status", 1);
		page.getParams().put("ust_showindex", 1);
		page.setSortname("ust_update_time");
		page.setSortorder("desc");
		page.getParams().put("usr_ent_id", prof.usr_ent_id);
		if(wizbini.cfgSysSetupadv.isTcIndependent()){
            page.getParams().put("top_tcr_id", prof.my_top_tc_id);
            }
		userSpecialTopicService.getSpecialTopicFrontList(page);
		List<UserSpecialTopic> specialTopics=page.getResults();
		for(UserSpecialTopic topic : specialTopics){
			topic.setEncrypt_ust_id(EncryptUtil.cwnEncrypt(topic.getUst_id()));
		}
		model.addAttribute("specialTopics", specialTopics);
         return "learningmap/specialTopic";
	 }
	
	@RequestMapping(value = "specialListJson")
	@ResponseBody
	 public String specialListJson(Model model,WizbiniLoader wizbini, loginProfile prof,Page<UserSpecialTopic> page){
		page.getParams().put("ust_status", 1);
		page.getParams().put("usr_ent_id", prof.usr_ent_id);
		if(wizbini.cfgSysSetupadv.isTcIndependent()){
            page.getParams().put("top_tcr_id", prof.my_top_tc_id);
            }
		userSpecialTopicService.getSpecialTopicFrontList(page);
         return JsonFormat.format(model, page);
	 }
	
	/**
	 * 学习地图:专题培训详细
	 */
	@RequestMapping(value = "specialDetail", method = RequestMethod.GET)
	public String specialDetail(Model model, loginProfile prof,@RequestParam(value = "id", required = false, defaultValue = "0") String id){
		long ust_id = 0;
		if(!StringUtils.isEmpty(id)){
			ust_id = EncryptUtil.cwnDecrypt(id);
		}
		model.addAttribute("ust_id", ust_id);
		return "learningmap/specialDetail";
	}
	
	/**
	 * 学习地图:专题岗位课程信息
	 * @param model
	 * @param prof
	 * @param page
	 * @param upm_id
	 * @return
	 */
	@RequestMapping(value = "specialCourseJson")
	@ResponseBody
	 public String specialCourseJson(Model model, WizbiniLoader wizbini,loginProfile prof,Page<AeItem> page,@RequestParam(value = "ust_id", required = false, defaultValue = "0") String ust_id,@RequestParam(value = "tkhId", required = false, defaultValue = "0") long tkhId){
		if(wizbini.cfgSysSetupadv.isTcIndependent()){
            page.getParams().put("top_tcr_id", prof.my_top_tc_id);
            }
		aeItemService.getAeItemBySpecialPage(page, EncryptUtil.cwnDecrypt(ust_id),tkhId,prof.usr_ent_id,prof.my_top_tc_id,prof.current_role);
		return JsonFormat.format(model, page);
	}
	
	/**
	 * 学习地图:专题培训详细信息
	 * @param model
	 * @param prof
	 * @param page
	 * @param upm_id
	 * @return
	 */
	@RequestMapping(value = "specialDetailJson")
	@ResponseBody
	 public UserSpecialTopic specialDetailJson(Model model, loginProfile prof,@RequestParam(value = "ust_id", required = false, defaultValue = "0") String ust_id){
		
		long ustId = EncryptUtil.cwnDecrypt(ust_id);
		
		UserSpecialTopic specialTopic=userSpecialTopicService.get(ustId);
		if(specialTopic!=null){
			userSpecialTopicService.updateHits(ustId);
		}
		specialTopic.setDate(new DateUtil().formateDate(new Date(specialTopic.getUst_update_time().getTime())));  
		ImageUtil.combineImagePath(specialTopic);
		return specialTopic;
	}
	
	/**
	 * 学习地图 专题培训专家
	 * @param model
	 * @param prof
	 * @param page
	 * @param ust_id
	 * @return
	 */
	@RequestMapping(value = "specialExpertsJson")
	@ResponseBody
	 public List<UserSpecialExpert> specialExpertsJson(WizbiniLoader wizbini,Model model, loginProfile prof,Page<RegUser> page,@RequestParam(value = "ust_id", required = false, defaultValue = "0") String ust_id){
		
		List<UserSpecialExpert> specialExperts=userSpecialExpertService.getExpertsByUstId(wizbini,EncryptUtil.cwnDecrypt(ust_id),prof.my_top_tc_id);
         return specialExperts;
	 }
	
	/**
	 * 移动端根据岗位类型获取对应的关键岗位
	 * @param model
	 * @param upc_id 岗位类型id
	 * @return
	 */
	@RequestMapping(value = "getPostMapListByUpcId")
	@ResponseBody
	public String getPostMapListByUpcId(Model model,WizbiniLoader wizbini,loginProfile prof,@RequestParam(value = "upc_id", required = true) String upc_id){
		List<UserPositionLrnMap> positionLrnMaps = userPositionLrnMapService.getPositionFrontMap("1",EncryptUtil.cwnDecrypt(upc_id),wizbini.cfgSysSetupadv.isTcIndependent(),prof.my_top_tc_id);
		for (UserPositionLrnMap userPositionLrnMap : positionLrnMaps) {
			ImageUtil.combineImagePath(userPositionLrnMap);
		}
		model.addAttribute("positionLrnMaps", positionLrnMaps);
		return JsonFormat.format(model);
	 }
	
	
	/**
	 * 移动端根据关键岗位id获取对应的学习地图
	 * @param model
	 * @param upt_id
	 * @return
	 */
	@RequestMapping(value = "getPostMapByUptId", method = RequestMethod.GET)
	@ResponseBody
	 public String getPostMapByUptId(Model model,@RequestParam(value = "upt_id", required = false, defaultValue = "0") String upt_id){
		UserPositionLrnMap lrnMap=userPositionLrnMapService.getByFieldName("upt_id", EncryptUtil.cwnDecrypt(upt_id));
		ImageUtil.combineImagePath(lrnMap);
		model.addAttribute("PostMap",lrnMap);
		return JsonFormat.format(model);
	 }
	
	/**
	 *  学习地图:关键岗位地图随机取关键岗位列表
	 * @param model
	 * @param prof
	 * @param page
	 * @param upm_id
	 * @return
	 */
	@RequestMapping(value = "getRecomendCourseByUptId")
	@ResponseBody
	 public String getRecomendCourseByUptId(Model model,WizbiniLoader wizbini,Page<AeItem> page, loginProfile prof,@RequestParam(value = "upt_id", required = false, defaultValue = "0") String upt_id,@RequestParam(value = "tkhId", required = false, defaultValue = "0") long tkhId){
		UserPositionLrnMap lrnMap=userPositionLrnMapService.getByFieldName("upt_id", EncryptUtil.cwnDecrypt(upt_id));
	    if(wizbini.cfgSysSetupadv.isTcIndependent()){
            page.getParams().put("top_tcr_id", prof.my_top_tc_id);
            }
		aeItemService.getAeItemByPositionMap(page,lrnMap.getUpm_id(),tkhId,prof.usr_ent_id,prof.my_top_tc_id,prof.current_role);
		return JsonFormat.format(model, page);
	}
	
	
	/**
	 * 获取推送到首页的专题培训列表
	 * @param page
	 */
	@RequestMapping("getSpecialListIndex")
	@ResponseBody
	public List<UserSpecialTopic> getSpecialListIndex(WizbiniLoader wizbini,loginProfile prof,Page<UserSpecialTopic> page)
			throws ParseException {
		List<UserSpecialTopic> specialTopics = userSpecialTopicService.getUserSpecialTopicListIndex(wizbini.cfgSysSetupadv.isTcIndependent(),prof.my_top_tc_id);
		for (UserSpecialTopic userSpecialTopic : specialTopics) {
			ImageUtil.combineImagePath(userSpecialTopic);
		}
		return specialTopics;
	}
	
	
	/**
	 * 移动端获取已发布专题培训列表个数
	 * @param page
	 */
	@RequestMapping("getSpecialListTotalCount")
	@ResponseBody
	public int getSpecialListTotalCount(WizbiniLoader wizbini,loginProfile prof)
			throws ParseException {
		return userSpecialTopicService.getSpecialListTotalCount(wizbini.cfgSysSetupadv.isTcIndependent(),prof.my_top_tc_id);
	}
	
	
	/**
	 * 获取已发布专题培训分页列表JSON
	 * @param page
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("getSpecialPageJson")
	@ResponseBody
	public String getSpecialPageJson(WizbiniLoader wizbini, loginProfile prof,Page<UserSpecialTopic> page,Model model){
		page.getParams().put("ust_status", 1);
		page.setSortname("ust_update_time");
		page.setSortorder("desc");
		page.getParams().put("usr_ent_id", prof.usr_ent_id);
		if(wizbini.cfgSysSetupadv.isTcIndependent()){
            page.getParams().put("top_tcr_id", prof.my_top_tc_id);
            }
		userSpecialTopicService.getSpecialTopicFrontList(page);
		return JsonFormat.format(model, page);
	}
	
	
	/**
	 * 获取关键岗位统计数
	 * @param page
	 */
	@RequestMapping(value = "getPostionMapCount")
	@ResponseBody
	public int getPostionMapCount(Model model,
			loginProfile prof,
			WizbiniLoader wizbini) {
		int cnt=userPositionLrnMapService.getCountById(null,"1",wizbini.cfgSysSetupadv.isTcIndependent(),prof.my_top_tc_id);
		return cnt;
	}
	/**
	 * 获取首页的关键岗位列表
	 * @param page
	 */
	@RequestMapping(value = "getPositionMapListIndex")
	@ResponseBody
	public List<UserPositionLrnMap> getPositionMapListIndex(Model model, Page<UserPositionLrnMap> page,
			loginProfile prof,
			WizbiniLoader wizbini) {
		page.setPageNo(1);
		page.setPageSize(4);
		page.setSortname("upm_seq_no");
		page.setSortorder("desc");
		page.getParams().put("upm_status", 1);
		page.getParams().put("usr_ent_id", prof.usr_ent_id);
	    if(wizbini.cfgSysSetupadv.isTcIndependent()){
            page.getParams().put("top_tcr_id", prof.my_top_tc_id);
            }
		userPositionLrnMapService.getPositionMapFrontList(page);
		List<UserPositionLrnMap> list=page.getResults();
		for (UserPositionLrnMap userPositionLrnMap : list) {
			ImageUtil.combineImagePath(userPositionLrnMap);
		}
		return list;
	}
	/**
	 * 获取推送到首页的PC端专题培训列表
	 * @param page
	 */
	@RequestMapping("getSpecialListForPCIndex")
	@ResponseBody
	public String getSpecialListForPCIndex(Model model, WizbiniLoader wizbini, loginProfile prof,Page<UserSpecialTopic> page){
		page.setPageNo(1);
		page.setPageSize(20);
		page.getParams().put("ust_status", 1);
		page.getParams().put("ust_showindex", 1);
		page.setSortname("ust_update_time");
		page.setSortorder("desc");
		page.getParams().put("usr_ent_id", prof.usr_ent_id);
		 if(wizbini.cfgSysSetupadv.isTcIndependent()){
	            page.getParams().put("top_tcr_id", prof.my_top_tc_id);
	            }
		userSpecialTopicService.getSpecialTopicFrontList(page);
		return JsonFormat.format(model, page);
	}
	/**
	 * 获取职级发展序列课程列表
	 * @param model
	 * @param prof
	 * @param page
	 *    职级id psi_ugr_id
	 * @param id  
	 * @param tkhId
	 * @return
	 */
	@RequestMapping("grade/courselist")
	@ResponseBody
	public String professionLrnItemCourseJson(Model model,WizbiniLoader wizbini, loginProfile prof,Page<AeItemVo> page,@RequestParam(value = "id",required = true) String id,@RequestParam(value = "pfs_id",required = true) String pfs_id,@RequestParam(value = "tkhId", required = false, defaultValue = "0") long tkhId){
		if(wizbini.cfgSysSetupadv.isTcIndependent()){
            page.getParams().put("top_tcr_id", prof.my_top_tc_id);
            }
		List<AeItemVo> list=aeItemService.getAeItemByProfessionLrnItemPage(EncryptUtil.cwnDecrypt(id),EncryptUtil.cwnDecrypt(pfs_id), tkhId, prof.usr_ent_id, prof.my_top_tc_id, prof.current_role);
		model.addAttribute("data", list);
		return JsonFormat.format(model);
	}
	
	/**
	 *  获取职级发展序列列表
	 * @param model
	 * @param prof
	 * @param wizbini
	 * @return
	 */
	@RequestMapping("grade/nodelist")
	@ResponseBody
	public String professionLrnItemNodelistJson(Model model, loginProfile prof,WizbiniLoader wizbini,@RequestParam(value = "id", required = true) String id) {
		List<GradeNodesVo> professionLrnItems=professionLrnItemService.getItemByfFrontPfsId(EncryptUtil.cwnDecrypt(id));
		model.addAttribute("data", professionLrnItems);
		return JsonFormat.format(model);
	}
	
	/**
	 *  获取职级发展列表
	 * @param model
	 * @param prof
	 * @param wizbini
	 * @return
	 */
	@RequestMapping("grade/orderlist")
	@ResponseBody
	public String professionOrderlistJson(Model model, loginProfile prof,WizbiniLoader wizbini) {
		List<Profession> professions=professionService.getAllFront(wizbini.cfgSysSetupadv.isTcIndependent(),prof.my_top_tc_id);
		model.addAttribute("data", professions);
		return JsonFormat.format(model);
	}
		
	@RequestMapping(value = "professionMap", method = RequestMethod.GET)
	public String orderlist(Model model, loginProfile prof){
		
		return "learningmap/profession/orderlist";
	}
	@RequestMapping(value = "nodelist", method = RequestMethod.GET)
	public String nodelist(Model model, loginProfile prof,@RequestParam(value = "id", required = true) String id){
		return "learningmap/profession/nodelist";
	}
	
	/**
	 * 移动端获取 学习地图相关 数量
	 * @param model
	 * @param wizbini
	 * @param prof
	 * @return
	 */
	@RequestMapping("getMapCount")
	@ResponseBody
	public String getMapCount(Model model, WizbiniLoader wizbini, loginProfile prof){
		//岗位学习地图个数
		int cnt=userPositionLrnMapService.getCountById(null,"1",wizbini.cfgSysSetupadv.isTcIndependent(),prof.my_top_tc_id);
		model.addAttribute("positionMapCount", cnt);
		
		//专题培训个数
		cnt = userSpecialTopicService.getSpecialListTotalCount(wizbini.cfgSysSetupadv.isTcIndependent(),prof.my_top_tc_id);
		model.addAttribute("SpecialTopic", cnt);
		
		//职级发展个数
		cnt = professionService.getAllFrontCount(wizbini.cfgSysSetupadv.isTcIndependent(),prof.my_top_tc_id);
		model.addAttribute("gradeMapCont", cnt);
		
		cnt=liveService.getCount(wizbini, prof);
		model.addAttribute("liveCont", cnt);
		
		return JsonFormat.format(model);
	}
	
	/**
	 * 获取发布到前台职级发展列表个数
	 * @param prof
	 * @param wizbini
	 * @return
	 */
	@RequestMapping("gradeMap/count")
	@ResponseBody
	public int getGradeMapCount(loginProfile prof,WizbiniLoader wizbini) {
		List<Profession> list = professionService.getAllFront(wizbini.cfgSysSetupadv.isTcIndependent(),prof.my_top_tc_id);
		return list == null ? 0 : list.size();
	}
	
}
