package com.cwn.wizbank.services;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;

import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.ae.aeItem;
import com.cwn.wizbank.common.SNS;
import com.cwn.wizbank.entity.AeApplication;
import com.cwn.wizbank.entity.AeAttendance;
import com.cwn.wizbank.entity.AeItem;
import com.cwn.wizbank.entity.AeItemAccess;
import com.cwn.wizbank.entity.AeItemLesson;
import com.cwn.wizbank.entity.AeTreeNode;
import com.cwn.wizbank.entity.Certificate;
import com.cwn.wizbank.entity.Course;
import com.cwn.wizbank.entity.CourseCriteria;
import com.cwn.wizbank.entity.CourseEvaluation;
import com.cwn.wizbank.entity.CourseMeasurement;
import com.cwn.wizbank.entity.InstructorComment;
import com.cwn.wizbank.entity.ItemTargetLrnDetail;
import com.cwn.wizbank.entity.Message;
import com.cwn.wizbank.entity.Resources;
import com.cwn.wizbank.entity.vo.AeItemVo;
import com.cwn.wizbank.entity.vo.LearningMapVo;
import com.cwn.wizbank.entity.vo.ModuleStructureVo;
import com.cwn.wizbank.exception.AuthorityException;
import com.cwn.wizbank.exception.DataNotFoundException;
import com.cwn.wizbank.persistence.AeApplicationMapper;
import com.cwn.wizbank.persistence.AeAttendanceMapper;
import com.cwn.wizbank.persistence.AeCatalogMapper;
import com.cwn.wizbank.persistence.AeItemExtensionMapper;
import com.cwn.wizbank.persistence.AeItemLessonMapper;
import com.cwn.wizbank.persistence.AeItemMapper;
import com.cwn.wizbank.persistence.AeTreeNodeMapper;
import com.cwn.wizbank.persistence.CertificateMapper;
import com.cwn.wizbank.persistence.CourseMapper;
import com.cwn.wizbank.persistence.CourseMeasurementMapper;
import com.cwn.wizbank.persistence.MessageMapper;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.utils.CommonLog;
import com.cwn.wizbank.utils.CwnUtil;
import com.cwn.wizbank.utils.DateUtil;
import com.cwn.wizbank.utils.HttpClientUtils;
import com.cwn.wizbank.utils.ImageUtil;
import com.cwn.wizbank.utils.LabelContent;
import com.cwn.wizbank.utils.Page;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
/**
 * service 实现
 */
@Service
public class AeItemService extends BaseService<AeItem> {

	private static final Logger logger = LoggerFactory.getLogger(AeItemService.class);
	
	@Autowired
	AeItemMapper aeItemMapper;
	
	@Autowired
	CourseMapper courseMapper;
	
	@Autowired
	MessageMapper messageMapper;
	
	@Autowired
	CertificateMapper certificateMapper;
	
	@Autowired
	CourseEvaluationService courseEvaluationService;
	
	@Autowired
	AeApplicationService aeApplicationService;
	
	@Autowired
	ResourcesService resourcesService;
	
	@Autowired
	CourseCriteriaService courseCriteriaService;
	
	@Autowired
	CourseMeasurementMapper courseMeasurementMapper;
	
	@Autowired
	AeItemAccessService aeItemAccessService;
	
	@Autowired
	AeItemLessonMapper aeItemLessonMapper;
	
	@Autowired
	InstructorCommentService instructorCommentService;
	
	@Autowired
	ForCallOldAPIService forCallOldAPIService;
	
	@Autowired
	AeItemExtensionMapper aeItemExtensionMapper;
	
	@Autowired
	AeCatalogMapper aeCatalogMapper;
	
	@Autowired
	SnsCountService snsCountService;
	
	@Autowired
	SnsValuationLogService snsValuationLogService;
	
	@Autowired
	ItemTargetLrnDetailService itemTargetLrnDetailService;

	
	@Autowired
	SnsCommentService snsCommentService;
	
	@Autowired
	AeTreeNodeMapper aeTreeNodeMapper;
	
	@Autowired
	TcTrainingCenterService tcTrainingCenterService;
	
	@Autowired
	AcRoleService acRoleService;
	
	@Autowired
	AeAttendanceMapper aeAttendanceMapper;
	
	@Autowired
	AeAttendanceService aeAttendanceService;

	
	@Autowired
	AeApplicationMapper aeApplicationMapper;


	/**
	 * 获取课程详情
	 * @param model
	 * @param itmId
	 * @param appId
	 * @param userEntId
	 * @param label_lan
	 * @throws DataNotFoundException 
	 * @throws AuthorityException 
	 */
	@SuppressWarnings("deprecation")
	public void getDetial(Model model, long tcrId, long itmId, long appTkhId, long userEntId, String label_lan, boolean hasStaff, int mod_mobile_ind, String usr_id) throws AuthorityException,Exception {
		Date curDate = getDate();
		AeItem item = getPermission(tcrId, itmId, userEntId);
		if(appTkhId > 0 && item == null) {
			item = aeItemMapper.get(itmId);
		}
		if (item == null)
			throw new AuthorityException(LabelContent.get(label_lan, "error_no_authority"));
		
		//必须，选修情况
		item.setItd(itemTargetLrnDetailService.getByUserItemId(item.getItm_id(), userEntId));
		
		// 如果是集成培训就查出子课程，离线课程就查出班级
		if (AeItem.CLASSROOM.equals(item.getItm_type())) {
			item.setChildrens(getChildrens(itmId, userEntId));			
			List<AeItemLesson> lessons = aeItemLessonMapper.getList(itmId);
			for(AeItemLesson aeItemLesson : lessons){
				Date start = aeItemLesson.getIls_start_time();
				Date end = aeItemLesson.getIls_end_time();
				if(start.getYear() == 0 && end.getYear() == 0){
					aeItemLesson.setIls_start_time(new Date(item.getItm_content_eff_end_time().getYear(), item.getItm_content_eff_end_time().getMonth(), item.getItm_content_eff_end_time().getDate(), start.getHours(), start.getMinutes()));
					aeItemLesson.setIls_end_time(new Date(item.getItm_content_eff_end_time().getYear(), item.getItm_content_eff_end_time().getMonth(), item.getItm_content_eff_end_time().getDate(), end.getHours(), end.getMinutes()));
				}
			}
			model.addAttribute("lessons", lessons);
		} 
		if(!AeItem.INTEGRATED.equals(item.getItm_type())) {
			item.setParent(aeItemMapper.getParent(itmId));
		}
		
		Course course = courseMapper.getCourseByItmId(itmId);
		long resId = 0;
		if(course != null && course.getCos_res_id() != null){
			resId = course.getCos_res_id();
		}
		
		AeApplication app =  null;
		CourseEvaluation cov = null;
		List<Resources> courseContentlist = null;
		//取出当前用户中该课程下的报名记录
		if(appTkhId > 0){
			//指定报名记录
			app = aeApplicationService.getAeApplicationMapper().getByTkhId(appTkhId);
			//防止在URL中改ID到到别人的学习记录
			if(app==null || app.getApp_itm_id()!= itmId || app.getApp_ent_id() !=userEntId){
				app = aeApplicationService.getMaxAppByUser(userEntId, itmId);
			}
			
		} else {
			//没有指定报名记录,取出最后一条报名记录
			app = aeApplicationService.getMaxAppByUser(userEntId, itmId);
		}
		
		CourseCriteria ccr = null;
		if(app != null && app.getApp_tkh_id() > 0){

			// 获取学习进度
			cov = courseEvaluationService.getCourseEvaluationMapper().getCourseEvaluationByThkId(app.getApp_tkh_id());
			//我的分数四舍五入处理
			if(null!=cov && null!=cov.getCov_score()){
				BigDecimal bValue = new  BigDecimal(cov.getCov_score());
				cov.setCov_score(bValue.setScale(2,  BigDecimal.ROUND_HALF_UP).doubleValue());
			}
			model.addAttribute("courseEvaluation", cov);

			//如果已成功报名,刚取出学习记录,课程下的模块及在各模块下的学习结果
			if (item.getItm_cfc_id() != null && item.getItm_cfc_id() != 0  && cov!=null && "C".equals(cov.getCov_status())) {
				//取出该课程关联的证书  将过期的证书以cfc_expired作标记
				Certificate certificate = certificateMapper.get(item.getItm_cfc_id());
				Timestamp ts = new Timestamp(System.currentTimeMillis());   
				if(ts.after(certificate.getCfc_end_date())){
					model.addAttribute("cfc_expired", "OFF");
				}else{
					model.addAttribute("cfc_expired", "ON");
				}
				model.addAttribute("certificate", certificate);
			}
			//取出所有在线模块及学员在模块中的学习结果
			courseContentlist = resourcesService.getCosContentList(resId, app.getApp_tkh_id(), label_lan, item.getItm_create_run_ind()==1 ? true:false, item.getItm_eff_start_datetime() , cov.getAtt().getAtt_create_timestamp(), mod_mobile_ind, usr_id);
			
			//取课程下的结训条件及积分项目
	    	ccr = courseCriteriaService.getItmCcrWithLrn(itmId, app.getApp_tkh_id());
			
	    	//有报名,培训下的课程和报名状态
	    	if(AeItem.INTEGRATED.equals(item.getItm_type())) {
				List<AeItem> items = getIntegratedChildren(itmId, app.getApp_tkh_id(), userEntId);
				model.addAttribute("child",getIntegratedChildren(items, label_lan));
			}
	    	
	    	AeAttendance aeAttendance = null;
			Long att_app_id = app.getApp_id();
			aeAttendance = aeAttendanceService.getAeAttendanceMapper().getEnrollmentTime(att_app_id.intValue());
			Date att_create_timestamp = aeAttendance.getAtt_create_timestamp();
			model.addAttribute("att_create_timestamp", att_create_timestamp);
			model.addAttribute("aeAttendance", aeAttendance);
			
		} else {
			//如果还没有报名,则只取出集成培训下的课程
			if(AeItem.INTEGRATED.equals(item.getItm_type())) {
				List<AeItem> items = getIntegratedChildren(itmId, 0, userEntId);
				model.addAttribute("child",getIntegratedChildren(items, label_lan));
			}
			//如果还没有报名成功,则只取出模块信息
			courseContentlist = resourcesService.getCosContentList(resId, (long)0, label_lan, item.getItm_create_run_ind()==1 ? true:false, item.getItm_eff_start_datetime() , null, mod_mobile_ind, usr_id);
		}
		if(app != null){
			model.addAttribute("app", app);
		}
		List<ModuleStructureVo> moduleList = adapterCourseContent(courseContentlist, course);
		if(moduleList != null) {
			Collections.reverse(moduleList);
		}
		if(ccr != null && moduleList != null) {
			ccr = sortModuleOrderByCos(ccr, moduleList);
			model.addAttribute("ccr", ccr);
		}
		
		model.addAttribute("coscontent", moduleList);
		//model.addAttribute("coscontent", courseContentlist);


		//课程信息
		
		long parent_itm_res_id = 0;
		ImageUtil.combineImagePath(item);
		//当是班级的时候就显示课程的图片
		if(AeItem.CLASSROOM.equals(item.getItm_type()) && item.getItm_create_run_ind() == 0){
			AeItem parentItem = getParent(item.getItm_id());
			ImageUtil.combineImagePath(parentItem);
			item.setItm_icon(parentItem.getItm_icon());
			
			
			Course parent_course = courseMapper.getCourseByItmId(parentItem.getItm_id());
			if(parent_course != null && parent_course.getCos_res_id() != null){
				parent_itm_res_id = parent_course.getCos_res_id();
			}
			
		}
		item.setItm_comment_total_count(snsCommentService.getCommentCount((long)item.getItm_id(), SNS.MODULE_COURSE));
		if(item.getItm_content_eff_end_time() != null) {
			item.setItm_online_content_period(1);
		} else if(item.getItm_content_eff_duration() != null && item.getItm_content_eff_duration() != 0) {
			item.setItm_online_content_period(2);
			if(app != null && "Admitted".equals(app.getApp_status())) {
				item.setItm_is_enrol(1);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Calendar date = Calendar.getInstance();
				date.setTime(app.getApp_upd_timestamp());
				date.set(Calendar.DATE, date.get(Calendar.DATE) + Integer.parseInt(item.getItm_content_eff_duration().toString()));
				item.setItm_content_eff_end_time(sdf.parse(sdf.format(date.getTime())));
			}
		}

		model.addAttribute("item", item);
		model.addAttribute("resId", resId);
		model.addAttribute("curDate", curDate);
		model.addAttribute("userEntId", userEntId);
		

		//课程公告
		model.addAttribute("messages", getMessagesByResId(resId, parent_itm_res_id));
		
		//课程目录
		List<AeTreeNode> list = aeTreeNodeMapper.getItemCatalog(itmId);
		model.addAttribute("catalogs", list);
		
		//时间有效性
		boolean timeValide = checkItemContentDateVilade(item, app != null ? app.getApp_id() : 0, curDate);
		boolean appnTimeValide = checkItemAppnDateVilade(item, app != null ? app.getApp_id() : 0, curDate);
		model.addAttribute("timeValide", timeValide);
		model.addAttribute("appnTimeValide", appnTimeValide);
		model.addAttribute("snsCount", snsCountService.getByTargetInfo(itmId, SNS.MODULE_COURSE));
		model.addAttribute("sns", snsCountService.getUserSnsDetail(itmId, userEntId, SNS.MODULE_COURSE));
		try {
			model.addAttribute("btn", forCallOldAPIService.getButton(null, itmId, userEntId,  app != null ? app.getApp_id() : 0));
			model.addAttribute("canNorminate", forCallOldAPIService.canNorminate(null, hasStaff, label_lan, itmId));
			model.addAttribute("isFull", forCallOldAPIService.quotaIsFull(null, itmId));
		} catch (Exception e) {
			CommonLog.error(e.getMessage(),e);
		}
		
		//讲师
		List<AeItemAccess> instructors = aeItemAccessService.getAeItemAccessMapper().getInstructorsByItmId(itmId);
		model.addAttribute("instructors",instructors);
		if(instructors != null && instructors.size() > 0){
			
			for(AeItemAccess aa : instructors){
				ImageUtil.combineImagePath(aa.getUser());
			}
			
			//讲师评分		
			List<InstructorComment> instructorComments = instructorCommentService.getInstructorCommentScore(itmId, userEntId);
			if(instructorComments != null && instructorComments.size()>0){
				model.addAttribute("instructorComment", instructorComments.get(0));
			}
		}
		
		//更新访问时间
/*		if(app != null && app.getApp_tkh_id() > 0){
			courseEvaluationService.updateCommenceTime(resId, userEntId, app.getApp_tkh_id());
			courseEvaluationService.updateLastAccessTime(resId, userEntId, app.getApp_tkh_id());
		}*/
		
		//跟新点击率
		aeItemExtensionMapper.updateAccessCount(itmId);
	}
	
	private CourseCriteria sortModuleOrderByCos(CourseCriteria ccr, List<ModuleStructureVo> moduleList) {
		List<CourseMeasurement> cmt_lst = ccr.getCmt_lst();
		if(cmt_lst != null) {
			for(int i=0; i<cmt_lst.size(); i++) {
				for(int j=0; j<moduleList.size(); j++) {
					if(cmt_lst.get(i).getMov().getMov_mod_id() == moduleList.get(j).getId()) {
						cmt_lst.get(i).setCmt_order_res(j);
						break;
					}
				}
			}
			CourseMeasurement[] cmt_arr = new CourseMeasurement[cmt_lst.size()];
			cmt_lst.toArray(cmt_arr);
			CourseMeasurement temp = null;
			for (int i = 0; i < cmt_arr.length; i++) {
				for (int j = i+1; j < cmt_arr.length; j++) {
					if (cmt_arr[i].getCmt_order_res() < cmt_arr[j].getCmt_order_res()) {
						temp = cmt_arr[i];
						cmt_arr[i] = cmt_arr[j];
						cmt_arr[j] = temp;
					}
				}
			}
			ccr.setCmt_lst(Arrays.asList(cmt_arr));
		}
		return ccr;
	}

	/**
	 * 课程公告
	 * @param resId 资源id
	 * @return
	 */
	public List<Message> getMessagesByResId(long resId, long parent_itm_res_id){
		List<Long> resIds = new ArrayList<Long>();
		resIds.add(resId);
		resIds.add(parent_itm_res_id);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("resIds", resIds);
		map.put("curTime", getDate());
		List<Message> messages = messageMapper.getMessagesByResId(map);
		return messages;
	}
	
	/**
	 * 课程公告
	 * @param itmId 课程id
	 * @return
	 */
	public List<Message> getMessagesByItmId(long itmId){
		Course course = courseMapper.getCourseByItmId(itmId);
		long resId = 0;
		long parent_itm_res_id = 0;
		if(course != null && course.getCos_res_id() != null){
			resId = course.getCos_res_id();
			AeItem parentItem = getParent(itmId);
			if(parentItem != null && parentItem.getItm_id() > 0){
				Course parent_course = courseMapper.getCourseByItmId(parentItem.getItm_id());
				if(parent_course != null && parent_course.getCos_res_id() != null){
					parent_itm_res_id = parent_course.getCos_res_id();
				}
			}
		}
		return getMessagesByResId(resId,parent_itm_res_id);
	}
	
	/**
	 * 封装网上课程内容
	 * @param list
	 * @param itmId
	 * @return
	 */
	List<ModuleStructureVo>  adapterCourseContent(List<Resources> list, Course course){
		
		Map<String, Resources> result = new HashMap<String,Resources>();
		if(list == null || course == null){
			return null;
		}
		String key = "key_";
		
		for(Resources rs : list){
			result.put(key + rs.getRes_id(), rs);
		}
		
		String treeStr = course.getCos_structure_json();
		if(!StringUtils.isEmpty(treeStr)){
			try{
				JSONObject jsons = JSONObject.fromObject(treeStr);
				JsonConfig jsonConfig = new JsonConfig();
				jsonConfig.setRootClass(ModuleStructureVo.class);
				//Map<String, Class<?>> classMap = new HashMap<String, Class<?>>();	
				//classMap.put("children", ModuleVo.class);
				//jsonConfig.setClassMap(classMap);
				JSONArray jsonArray = jsons.getJSONArray("structure");
					
				List<ModuleStructureVo>  msList = new ArrayList<ModuleStructureVo>();
				
				adapterDir(result, msList, jsonArray, jsonConfig, key, true);
			
				msList = clearRepeatRecord(msList);
				return msList;
			} catch(Exception e){
				CommonLog.error(e.getMessage(),e);
			}
		}
		return null;
	}
	
	
	/**
	 * 清除重复记录
	 * @param msList
	 * @return
	 */
	private List<ModuleStructureVo> clearRepeatRecord(
			List<ModuleStructureVo> msList) {
		List<ModuleStructureVo> result = new ArrayList<ModuleStructureVo>();
		if(msList==null || msList.size()==0){
			return result;
		}
		
		Map<String,ModuleStructureVo> map = new HashMap<String, ModuleStructureVo>();
		
		for(ModuleStructureVo v : msList){
			String key = v.getIdentifier();
			if("SCO".equals(v.getRestype())){
				key = "SCO_"+v.getId();
			}
			if(!map.containsKey(key)){//借助map去重复
				map.put(key,v);
				result.add(v);
			}
		}
		
		return result;
	}

	/**
	 * 解析目录，二级以下的目录全都放到一级中
	 * @param result
	 * @param msList
	 * @param jsonArray
	 * @param jsonConfig
	 * @param key
	 * @param isAddSub
	 */
	public void adapterDir(Map<String, Resources> result, List<ModuleStructureVo>  msList,
			JSONArray jsonArray, JsonConfig jsonConfig, String key, boolean isAddSub){
		if(jsonArray != null && jsonArray.size() > 0){
			for(int i = jsonArray.size() - 1; i >= 0 ; i--){
				String jsonStr = jsonArray.getString(i);
				JSONObject json = JSONObject.fromObject(jsonStr);
				ModuleStructureVo ms = (ModuleStructureVo) JSONObject.toBean(json, jsonConfig);
				
				List<ModuleStructureVo> children = new ArrayList<ModuleStructureVo>();
				
				adapterModule(result, msList, children, json, jsonConfig, key);
				
				ms.setChildren(children);
				if(ms.getId() > 0){
					ms.setResources(result.get(key + ms.getId()));
				}
				if (children.size() > 0 || !"FDR".equals(ms.getItemtype()) && isAddSub) {
					msList.add(ms);
				}
				
			}
		}
	}
	
	/**
	 * 解析模块
	 * @param result
	 * @param msList
	 * @param children
	 * @param json
	 * @param jsonConfig
	 * @param key
	 */
	public void adapterModule(Map<String, Resources> result, List<ModuleStructureVo>  msList, List<ModuleStructureVo> children,
			JSONObject json, JsonConfig jsonConfig, String key){
		JSONArray moduleArray = json.getJSONArray("children");
		for(int j=0; j < moduleArray.size(); j++){
			String moduleStr = moduleArray.getString(j);
			JSONObject mjson = JSONObject.fromObject(moduleStr);
			ModuleStructureVo module = (ModuleStructureVo) JSONObject.toBean(mjson, jsonConfig);
			//System.out.println(module.getTitle());
			CommonLog.debug(module.getId() + module.getTitle());
			if("FDR".equals(module.getItemtype())){
				JSONArray subArray = json.getJSONArray("children");
				adapterDir(result, msList, subArray, jsonConfig, key, false);
			} else if( module.getId() > 0 && !"FDR".equals(module.getItemtype()) ){
				module.setResources(result.get(key + module.getId()));
				children.add(module);
			}
		}
	}
	
	/**
	 * 课程内容时间有效性
	 * @param item
	 * @param appId
	 * @param curDate
	 * @return
	 */
	public boolean checkItemContentDateVilade(AeItem item, long appId, Date curDate){
		boolean timeValide = true;
		if(AeItem.SELFSTUDY.equals(item.getItm_type())){
			if(item.getItm_content_eff_start_time() != null && curDate.before(item.getItm_content_eff_start_time())){
				timeValide = false;
			}
			if(item.getItm_content_eff_end_time() != null && curDate.after(item.getItm_content_eff_end_time())){
				timeValide = false;
			}
			if(item.getItm_content_eff_duration() != null && item.getItm_content_eff_duration() > 0 && appId > 0){
				AeAttendance att = aeAttendanceMapper.get(appId);
				
				if(att == null){
					timeValide = false;
				}else{
					Calendar cd = Calendar.getInstance();
					cd.setTime(att.getAtt_create_timestamp());
					cd.add(Calendar.DAY_OF_MONTH, item.getItm_content_eff_duration().intValue());
					if(cd.getTime().before(curDate)){
						timeValide = false;
					}
				}
			}
		}
		return timeValide;
	}

	/**
	 * 课程报名时间有效性
	 * @param item
	 * @param appId
	 * @param curDate
	 * @return
	 */
	public boolean checkItemAppnDateVilade(AeItem item, long appId, Date curDate){
		boolean timeValide = true;
		if(AeItem.SELFSTUDY.equals(item.getItm_type())){
			if(item.getItm_appn_start_datetime() != null && curDate.before(item.getItm_appn_start_datetime())){
				timeValide = false;
			}
			if(item.getItm_appn_end_datetime() != null && curDate.after(item.getItm_appn_end_datetime())){
				timeValide = false;
			}			
		}
		return timeValide;
	}
	
	/**
	 * 获取子类课程，或是班级
	 * @param itmId
	 * @param userEntId
	 * @return
	 */
	private List<AeItem> getChildrens(long itmId, long userEntId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("itmId", itmId);
		map.put("userEntId", userEntId);
		return aeItemMapper.getChildrens(map);
	}


	/**
	 * 
	 * @param model
	 * @param itmId
	 * @param appId
	 * @param userEntId
	 * @param label_lan
	 * @param hasStaff
	 * @throws AuthorityException 
	 */
	public void getOpenDetial(Model model, long tcrId, long itmId, long userEntId, String label_lan, boolean hasStaff, boolean isManage, int isMobile, String usr_id) throws AuthorityException,Exception {
		AeItem item = getOpenPermission(tcrId, itmId, userEntId, isManage);
		Long resId = courseMapper.getResIdByItmId(itmId);
		if (item == null)
			throw new AuthorityException(LabelContent.get(label_lan, "error_no_authority"));

		//课程信息
		ImageUtil.combineImagePath(item);
		item.setItm_comment_total_count(snsCommentService.getCommentCount((long)item.getItm_id(), SNS.MODULE_COURSE));

		model.addAttribute("item", item);
		model.addAttribute("resId", resId);
		model.addAttribute("curDate", getDate());
		model.addAttribute("userEntId", userEntId);
		
		model.addAttribute("snsCount", snsCountService.getByTargetInfo(itmId, SNS.MODULE_COURSE));		
		model.addAttribute("sns", snsCountService.getUserSnsDetail(itmId, userEntId, SNS.MODULE_COURSE));
		//如果还没有报名成功,则只取出模块信息
		model.addAttribute("coscontent",resourcesService.getCosContentList(resId, (long)0, label_lan, item.getItm_create_run_ind()==1 ? true:false, item.getItm_eff_start_datetime() , null, isMobile, usr_id));
		
		model.addAttribute("itmCatalog", aeCatalogMapper.getItemCatalogList(itmId));
		
		//跟新点击率
		aeItemExtensionMapper.updateAccessCount(itmId);		
		
	}

	/**
	 * 获取父类课程（离线课程，继承培训）
	 * 
	 * @param itmId
	 * @return
	 */
	public AeItem getParent(long itmId) {
		return aeItemMapper.getParent(itmId);
	}



	/**
	 * 按目录查询课程
	 * @param userEntId
	 * @param page
	 * @return
	 */
	public Page<AeItem> getCatalogCourse(long userEntId, String curLang, Page<AeItem> page) {
		page.getParams().put("userEntId", userEntId);
		//周期时间段
		String periods = (String) page.getParams().get("periods");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cld = Calendar.getInstance();
		cld.setTime(getDate());
		
		Date startTime = cld.getTime();
		Date endTime = null;
		if("week".equals(periods)) {
			cld.setTime(getDate());
			cld.add(Calendar.DAY_OF_MONTH, 7);	//未来七天
			endTime = cld.getTime();
		} else if("month".equals(periods)) {
			cld.setTime(getDate());
			cld.add(Calendar.MONTH, 1);	//未来一个月
			endTime = cld.getTime();
		} else if("quarter".equals(periods)) {
			cld.setTime(getDate());
			cld.add(Calendar.MONTH, 3);	//未来三个月
			endTime = cld.getTime();
		}
		
		if(endTime != null && endTime.after(startTime)) {
			page.getParams().put("startTime", startTime);
			page.getParams().put("endTime", endTime);
			logger.debug(" [startTime]:" + sdf.format(startTime) + " | [endTime]:" + sdf.format(endTime));
		}
		

		List<AeItem> list = aeItemMapper.getCatalogCourse(page);
		for(AeItem itm : list) {
			if(itm != null) {
				if(itm.getItm_content_eff_end_time() != null) {
					itm.setItm_online_content_period(1);
				} else if(itm.getItm_content_eff_duration() != null && itm.getItm_content_eff_duration() != 0) {
					itm.setItm_online_content_period(2);
				}
				ImageUtil.combineImagePath(itm);
				itm.setSnsCount(snsCountService.getByTargetInfo(itm.getItm_id(), SNS.MODULE_COURSE));
				long count = snsCommentService.getCommentCount(itm.getItm_id(), SNS.MODULE_COURSE);
				//面授课程的评论数要加上班级的评论数
				if(null != itm.getItm_run_ind() && itm.getItm_run_ind() == 0 && aeItem.ITM_TYPE_CLASSROOM.equals(itm.getItm_type())){
					count += snsCommentService.getClassCommentCount(itm.getItm_id(), SNS.MODULE_COURSE);
				}
				itm.setCnt_comment_count(count);
			}
		}
		
		page.setResults(list);
		return page;
	}
	
	/**
	 * 获取公开课详情，有权限
	 * @param tcrId
	 * @param itmId
	 * @return
	 */
	public AeItem getOpenPermission(long tcrId, long itmId, long userEntId, boolean isManage){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("tcrId", tcrId);
		map.put("itmId", itmId);
		map.put("userEntId", userEntId);
		map.put("isManage", isManage);
		return aeItemMapper.getOpenPermission(map);
	}
	
	public AeItem getPermission(long tcrId, long itmId, long userEntId){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("tcrId", tcrId);
		map.put("itmId", itmId);
		map.put("userEntId", userEntId);
		Page p = new Page();//为了使用aeItemMapper通用的功能，外面套了一层Page
		p.setParams(map);
		return aeItemMapper.getPermission(p);
	}
	
	
	
	/**
	 * 获取课程排行榜信息
	 * @param page
	 * @return
	 */
	public Page<AeItem> getAeitemRank(Page<AeItem> page, long tcrId, long usr_ent_id) {
		page.getParams().put("tcrId", tcrId);
		page.getParams().put("userEntId", usr_ent_id);
		List<AeItem> list = aeItemMapper.getAeitemRank(page);
		for(AeItem aeitem: list){
			if(aeitem != null && aeitem.getItm_id() != null) {
				
				AeItem parent = aeItemMapper.getParent(aeitem.getItm_id());
				if(parent != null && parent.getItm_id() != null) {
					ImageUtil.combineImagePath(parent);
					//当是班级的时候就显示课程的图片
					aeitem.setItm_icon(parent.getItm_icon());
				}else{
					ImageUtil.combineImagePath(aeitem);
				}
				
			}
		}
		return page;
	}
	
	/**
	 * 分组获取必须选修条件课程
	 * 一次性查出这个课程的选修必修条件的课程，然后内存分组
	 * @param items
	 * @return
	 */
	public Map<String,List<AeItem>> getIntegratedChildren(List<AeItem> items, String lang) {
		Map<String,List<AeItem>> map = new HashMap<String,List<AeItem>>();
		List<AeItem> lists = null;
		List<Object> counts = new ArrayList<Object>();
		AeItem item = null;
		//分成多少组
		for(int i = 0; i< items.size(); i++) {
			if(!counts.contains(items.get(i).getIcd().getIcd_id())){
				counts.add(items.get(i).getIcd().getIcd_id());
			}
		}
		//按组分配课程
		for(int i = 0; i < counts.size(); i++) {
			lists = new ArrayList<AeItem>();
			for(int j = 0; j < items.size(); j++) {
				if(counts.get(i).equals(items.get(j).getIcd().getIcd_id())) {
					item = items.get(j);
					if(item.getItm_exam_ind()==1)
					item.setItm_type(CwnUtil.getExamTypeStr(item.getItm_type(), lang));
					else
					item.setItm_type(CwnUtil.getCourseTypeStr(item.getItm_type(), lang));
					CourseEvaluation cov = item.getCov();
					if(cov != null) {
						cov.setCov_status(CwnUtil.getAppStatusStr(cov.getCov_status(), lang));
					}
					item.getIcd().setIcd_type(CwnUtil.getCompulsoryStr(item.getIcd().getIcd_type(), lang));
					lists.add(items.get(j));
				}
			}
			map.put("child" + i, lists);
		}
		return map;
	}
	
	/**
	 * 获取集成培训下的必修选修
	 * @param itemId
	 * @param tkhId
	 * @return
	 */
	public List<AeItem> getIntegratedChildren(long itmId, long tkhId, long userEntId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("itmId", itmId);
		map.put("tkhId", tkhId);
		map.put("userEntId", userEntId);
		return aeItemMapper.getIntegratedChildren(map);
	}


	/**
	 * 获取公开课
	 * @param page
	 * @return
	 */
	public Page<AeItem> getOpens(Page<AeItem> page, long tcrId, long userEntId) {
		page.getParams().put("userEntId", userEntId);
		page.getParams().put("tcrId", tcrId);
		List<AeItem> list = aeItemMapper.getOpens(page);
		if(list != null && !list.isEmpty()){
			for(AeItem item : list) {
				ImageUtil.combineImagePath(item);
				item.setSnsCount(snsCountService.getByTargetInfo(item.getItm_id(), SNS.MODULE_COURSE));			
				item.setUserLike(snsValuationLogService.getByUserId(item.getItm_id(), userEntId, SNS.MODULE_COURSE, 0));
				item.setCnt_comment_count(snsCommentService.getCommentCount(item.getItm_id(), SNS.MODULE_COURSE));
			}
		}
		return page;
	}
	
	
	public Map<String,List<AeItem>> adapterLearningGroup(List<AeItem> list, Date zeroDate, Date afterDate, String type, long userEntId){

		List<AeItem> groups = new ArrayList<AeItem>();	 //用户组推荐
		List<AeItem> grades = new ArrayList<AeItem>();   //职级推荐
		List<AeItem> positions = new ArrayList<AeItem>();//岗位推荐
		List<AeItem> sups = new ArrayList<AeItem>();	 //上司推荐
		List<AeItem> tadms = new ArrayList<AeItem>();	 //管理员推荐
		List<AeItem> others = new ArrayList<AeItem>();	 //其他
		Map<String,List<AeItem>> map = new HashMap<String,List<AeItem>>();
				
		if(list != null && list.size() > 0) {
			
			for(AeItem item : list) {
				//类型为班级的，找到课程
				if(item.getItm_run_ind() == 1){
					AeItem itm = getParent(item.getItm_id());
					if(itm != null) {
						ItemTargetLrnDetail it = itemTargetLrnDetailService.getByUserItemId(itm.getItm_id(), userEntId);
						if(it != null) {
							
							if(it.getItd_group_ind() != null && it.getItd_group_ind() == 1){
								groups.add(item);
							}
							
							if(it.getItd_grade_ind() != null && it.getItd_grade_ind() == 1){
								grades.add(item);
							}
							
							if(it.getItd_position_ind() != null && it.getItd_position_ind() == 1){
								positions.add(item);
							}
						}
						item.setParent(itm);
					}
				}
				if(item.getItm_eff_start_datetime() != null) {

					item.setMapVo(getLetfWidth(zeroDate, afterDate, item.getItm_eff_start_datetime(), item.getItm_eff_end_datetime(), type, item.getItm_title()));
				}
				
				if(item.getApp() != null){
					if("TADM".equals(item.getApp().getApp_nominate_type())){
						tadms.add(item);
					} else if("SUP".equals(item.getApp().getApp_nominate_type())){
						sups.add(item);
					} else {
						others.add(item);
					}
				} else {
					others.add(item);
				}
	
						
				if(item.getItd() != null) {
					
					boolean flag = false;
					if(item.getItd().getItd_group_ind() != null && item.getItd().getItd_group_ind() == 1){
						groups.add(item);
						flag = true;
					}
					
					if(item.getItd().getItd_grade_ind() != null && item.getItd().getItd_grade_ind() == 1){
						grades.add(item);
						flag = true;
					}
					
					if(item.getItd().getItd_position_ind() != null && item.getItd().getItd_position_ind() == 1){
						positions.add(item);
						flag = true;
					}
					
					if(others.contains(item) && flag){
						others.remove(item);
					}
				}
			}
			
			map.put("groups", groups);
			map.put("grades", grades);
			map.put("positions", positions);
			map.put("sups", sups);
			map.put("tadms", tadms);
			map.put("others", others);
		}
		return map;
	}
	
	
	public LearningMapVo getLetfWidth(Date zeroDate, Date afterDate, Date start , Date end, String type, String title){
		
		Calendar startDt = Calendar.getInstance();
		startDt.setTime(zeroDate);
		
		Calendar startCld = Calendar.getInstance();
		startCld.setTime(start);
		
		Calendar endCld = Calendar.getInstance();
		
		//半格的宽度
		float unitPx = LearningMapVo.unitMpx;
		if("Y".equals(type)) {
			unitPx = LearningMapVo.unitYpx;
		} 
		
		if(end != null) {
//			如果结束时间超过区域结束时间，设置结束时间为区域结束时间
			if(afterDate.before(end)){
				endCld.setTime(afterDate);
			} else {
				endCld.setTime(end);
			}
		} else {
			//如果是无限，设置成区域结束时间
			endCld.setTime(afterDate);
		}
		if(startCld.before(startDt)){
			//如果开始时间在区域开始时间之前，设置开始时间为区域开始时间
			startCld.setTime(zeroDate);
		}
		if(title.indexOf("包商银行1001班")>-1){
			//System.out.println();
		}
		
		float left = 0;
		float width = 0;
		float fullWidth = 0;
		if("M".equals(type)){
			
			fullWidth = unitPx * 4 * 5 * 2;	
			
			int leftDays = DateUtil.getInstance().getDays(startDt.getTime(), startCld.getTime());
			
			int lmonths = leftDays / 31;
			int lweeks = leftDays % 31 / 7;
			int lday = leftDays % 31 % 7;
			
			left = lmonths * unitPx * 2 * 5 + lweeks * 2 * unitPx + lday * (unitPx * 2 / 7);
			
			int widthDays = DateUtil.getInstance().getDays(startCld.getTime(), endCld.getTime());
			
			int wmonths = widthDays / 31;
			int wweeks = widthDays % 31 / 7;
			int wday = widthDays % 31 % 7;
			if( wmonths > 2 && widthDays % 31 >= 0 && widthDays % 31 <= 3) {
				wmonths = wmonths - 1;	//如果整除，则往后退一个月
				wweeks += wmonths;	//周补上 前几个月的周
			}
			
			width = wmonths * unitPx * 2 * 5 + wweeks * 2 * unitPx + wday * (unitPx * 2 / 7) + wmonths * 2 * unitPx;
			
		} else {
			
			fullWidth = unitPx * 12 * 2;

			left = (startCld.get(Calendar.MONTH) - startDt.get(Calendar.MONTH)) * unitPx * 2;
			float startLeft = startCld.get(Calendar.DAY_OF_MONTH) / (startCld.getActualMaximum(Calendar.DAY_OF_MONTH)+0f) * unitPx * 2;
			left += startLeft;
			width = (endCld.get(Calendar.MONTH) - startCld.get(Calendar.MONTH)) * unitPx * 2;
			
			float endWidth = endCld.get(Calendar.DAY_OF_MONTH) / (endCld.getActualMaximum(Calendar.DAY_OF_MONTH)+0f) * unitPx * 2;
			width += endWidth - startLeft;
			
		}
		
		if(width < 0){
			float absLeft = Math.abs(left);
			float absWidth = Math.abs(width);
			if(absLeft + absWidth > fullWidth){
				width = absWidth - (absLeft + absWidth - fullWidth);
			}
		} else {
			if(left + width > fullWidth){
				width = width - (left + width - fullWidth);
			}
		}
		if(width <= 1) width = 2;	//如果宽度为0，设置宽度为一天的
		if((end == null || end.after(afterDate)) && "M".equals(type)) width = fullWidth - left;	//最后一个月多了个星期，填不满
		
		LearningMapVo mapVo = new LearningMapVo();
		mapVo.setLeft((int)left);
		mapVo.setWidth((int)width);
		
		return mapVo;
	}

	
	/**
	 * 获取课程排行榜信息
	 * @param page
	 * @return
	 */
	public Page<AeItem> getHotCourse(Page<AeItem> page, long tcrId, long userEntId) {
		page.getParams().put("tcrId", tcrId);
		page.getParams().put("userEntId", userEntId);
		List<AeItem> list = aeItemMapper.getHotCourse(page);
		if(list!=null && list.size()>0){
			for(AeItem item : list) {
				ImageUtil.combineImagePath(item);
				item.setSnsCount(snsCountService.getByTargetInfo(item.getItm_id(), SNS.MODULE_COURSE));			
				item.setUserLike(snsValuationLogService.getByUserId(item.getItm_id(), userEntId, SNS.MODULE_COURSE, 0));
			}
		}
		return page;
	}
	
	public AeItemLessonMapper getAeItemLessonMapper() {
		return aeItemLessonMapper;
	}


	public void setAeItemLessonMapper(AeItemLessonMapper aeItemLessonMapper) {
		this.aeItemLessonMapper = aeItemLessonMapper;
	}

	public int getCatalogCount(long userEntId, int newestDuration,
			int isExam, long tcrId) {
		Calendar cld = Calendar.getInstance();
		cld.setTime(getDate());
		cld.set(Calendar.DAY_OF_MONTH, newestDuration);
		
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("userEntId", userEntId);
		map.put("beforeTime", cld.getTime());
		map.put("isExam", isExam);
		map.put("tcrId", tcrId);
		return aeItemMapper.getCatalogCount(map);
	}
	
	/**
	 * 没有该课程的学习权限就返回null
	 * @param itmId 课程id
	 * @param usrId 用户id
	 * @param tcrId 培训中心id
	 * @param page
	 */
	public AeItem getAeItem(Page<AeItem> page, long itmId, long usrId, long tcrId){
		page.getParams().put("itmId", itmId);
		page.getParams().put("userEntId", usrId);
		page.getParams().put("tcrId", tcrId);
		aeItemMapper.checkAeItemCompetence(page);
		if(page.getTotalRecord() == 0){
			return null;
		} else {
			return page.getResults().get(0);
		}
	}
	
	
	/**
	 * 管理员端
	 * @param page
	 * @param usrEntId
	 * @param tcrId
	 * @return
	 * @throws ParseException 
	 */
	public Page<AeItem> pageAdmin(Page<AeItem> page, long userEntId, String rolExtId){
		
		List<Long> tclist = tcTrainingCenterService.getTrainingCenterIdByOfficer(userEntId, rolExtId);//获取可以管理的培训中心
		page.getParams().put("tclist", tclist);
		page.getParams().put("userEntId", userEntId);
		boolean isRoleTcInd=AccessControlWZB.isRoleTcInd(rolExtId);
		page.getParams().put("isRoleTcInd", isRoleTcInd);
		
		String periods = (String) page.getParams().get("itm_periods");
		if(!StringUtils.isEmpty(periods)) {
			Calendar cld = Calendar.getInstance();
			Date startTime = null; //cld.getTime();
			Date endTime = null;
			//start
			cld.setTime(getDate());
			startTime = cld.getTime();
			//end
			cld.add(Calendar.DAY_OF_MONTH, Integer.parseInt(periods));
			endTime = cld.getTime();
			if(endTime != null && endTime.after(startTime)) {
				page.getParams().put("startTime", startTime);
				page.getParams().put("endTime", endTime);
			}
		}

		Object obj = page.getParams().get("itm_status");
		if(obj != null){
			if("ON_MOBILE".equals(obj.toString())){
				page.getParams().put("itm_status", "ON");
				page.getParams().put("itm_mobile_ind", "yes");
			}
		}
		if(null!= page.getParams().get("searchText")){
			String searchText = String.valueOf(page.getParams().get("searchText"));
			page.getParams().put("searchText",  "%" + searchText.trim().toLowerCase() + "%");
		}
		aeItemMapper.pageAdmin(page);
		List<AeItem> itmlist = page.getResults();
		if(itmlist == null) return page;
		for(AeItem itm : itmlist) {
			ImageUtil.combineImagePath(itm);
		}
		return page;
	}

	public Page<AeItem> getInstructorItemCosPage(Page<AeItem> page, long instrId, long userEntId) {
		page.getParams().put("instrId", instrId);
		this.aeItemMapper.pageInstrCos(page);
		List<AeItem> list = page.getResults();
		for(AeItem item : list) {
			ImageUtil.combineImagePath(item);
			item.setSnsCount(snsCountService.getByTargetInfo(item.getItm_id(), SNS.MODULE_COURSE));			
			item.setUserLike(snsValuationLogService.getByUserId(item.getItm_id(), userEntId, SNS.MODULE_COURSE, 0));
			item.setCnt_comment_count(snsCommentService.getCommentCount(item.getItm_id(), SNS.MODULE_COURSE));
		}
		return page;
	}
	
	public AeItem get(long itmId) {
		AeItem aeItem = aeItemMapper.get(itmId);
		if(aeItem !=null){
			if(aeItem.getItm_run_ind() == 1){
				aeItem.setParent(getParent(itmId));
			}
			if(aeItem.getItm_status() == null || "".equals(aeItem.getItm_status()) || "OFF".equals(aeItem.getItm_status())){
				Timestamp cur_time = getDate();
				if(aeItem.getItm_eff_end_datetime() == null || cur_time.before(aeItem.getItm_eff_end_datetime())){
					if(aeItem.getItm_content_eff_end_time() == null || cur_time.before(aeItem.getItm_content_eff_end_time())){
						aeItem.setItm_status_off(true);
					}
				}
			}
			
			boolean hasLesson = false;
			if(aeItem.getItm_run_ind() == 1){
				List<AeItemLesson> list = aeItemLessonMapper.getList(aeItem.getItm_id());
				if(list.size() > 0){
					hasLesson = true;
				}
			}
			aeItem.setItm_has_lesson(hasLesson);
			
		}
		return aeItem;
	}
	public Page<AeItem> getAeItemByPositionMap(Page<AeItem> page,long upi_upm_id, long appTkhId,long userEntId,long tcrId,String curLang){
		page.getParams().put("upi_upm_id", upi_upm_id);
		List<AeItem> aeItems=aeItemMapper.getAeItemByPositionMap(page);
		for (AeItem aeItem : aeItems) {
			AeItem item = getPermission(tcrId, aeItem.getItm_id(), userEntId);
			if(appTkhId > 0 && item == null) {
				item = aeItemMapper.get(aeItem.getItm_id());
			}
			if (item != null){
				aeItem.setItm_canread(1);
			}
			ImageUtil.combineImagePath(aeItem);
		}
		return page;
	}
	public Page<AeItem> getAeItemBySpecialPage(Page<AeItem> page,long ust_utc_id, long appTkhId,long userEntId,long tcrId,String curLang){
		page.getParams().put("ust_utc_id", ust_utc_id);
		aeItemMapper.getAeItemBySpecialPage(page);
		List<AeItem> itmlist = page.getResults();
		for (AeItem aeItem : itmlist) {
			AeItem item = getPermission(tcrId, aeItem.getItm_id(), userEntId);
			if(appTkhId > 0 && item == null) {
				item = aeItemMapper.get(aeItem.getItm_id());
			}
			if (item != null){
				aeItem.setItm_canread(1);
			}
			ImageUtil.combineImagePath(aeItem);
		}
		return page;
	}
	public List<AeItemVo> getAeItemByProfessionLrnItemPage(long psi_ugr_id,long psi_psf_id, long appTkhId,long userEntId,long tcrId,String curLang){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("psi_ugr_id", psi_ugr_id);
		params.put("psi_pfs_id", psi_psf_id);
		List<AeItemVo> itmlist=aeItemMapper.getAeItemByProfessionLrnItemPage(params);
		for (AeItemVo aeItemVo : itmlist) {
			AeItem item = getPermission(tcrId, aeItemVo.getItm_id(), userEntId);
			if(appTkhId > 0 && item == null) {
				item = aeItemMapper.get(aeItemVo.getItm_id());
			}
			if (item != null){
				aeItemVo.setItm_canread(1);
			}
			ImageUtil.combineImagePath(aeItemVo);
		}
		return itmlist;
	}
	/**
	 * 管理端选择课程(弹出层)
	 * @param page
	 * @param usrEntId
	 * @param tcrId
	 * @return
	 * @throws ParseException 
	 */
	public Page<AeItem> pageAdminCourse(Page<AeItem> page, long userEntId, String rolExtId,String type){
		
		List<Long> tclist = tcTrainingCenterService.getTrainingCenterIdByOfficer(userEntId, rolExtId);//获取可以管理的培训中心
		page.getParams().put("tclist", tclist);
		page.getParams().put("userEntId", userEntId);
		if(!StringUtils.isEmpty(type)){
			page.getParams().put("itm_type", type);
		}

//		page.getParams().put("itm_status", "ON");
/*		if(!page.getParams().containsKey("tcrId")){
			page.getParams().put("tcrId", tcrId);
		}*/
		if(!acRoleService.isRoleTcInd(rolExtId)){
			page.getParams().put("rolExtId", rolExtId);
			}
		String periods = (String) page.getParams().get("itm_periods");
		if(!StringUtils.isEmpty(periods)) {
			Calendar cld = Calendar.getInstance();
			Date startTime = null; //cld.getTime();
			Date endTime = null;
			//start
			cld.setTime(getDate());
			startTime = cld.getTime();
			//end
			cld.add(Calendar.DAY_OF_MONTH, Integer.parseInt(periods));
			endTime = cld.getTime();
			if(endTime != null && endTime.after(startTime)) {
				page.getParams().put("startTime", startTime);
				page.getParams().put("endTime", endTime);
			}
		}
		if(null!= page.getParams().get("searchText")){
			String searchText = String.valueOf(page.getParams().get("searchText"));
			page.getParams().put("searchText",  "%" + searchText.trim().toLowerCase() + "%");
		}
		aeItemMapper.pageAdminCourse(page);
		List<AeItem> itmlist = page.getResults();
		if(itmlist == null) return page;
		for(AeItem itm : itmlist) {
			ImageUtil.combineImagePath(itm);
		}
		return page;
	}
	
	
	/**
	 * 获取培训管理员可管理的课程的数量
	 * @param usrEntId 用户Id
	 * @param isOpen 是否为公开课
	 * @param isExam 是否是考试
	 * @param itemType 课程类型
	 * @return
	 */
	public long getAeItemTotalCountForTA(long usrEntId,int isOpen,int isExam,String itemType){
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("usrEntId", usrEntId);
		params.put("isOpen", isOpen);
		params.put("isExam", isExam);
		params.put("itemType", itemType);
		
		return this.aeItemMapper.getAeItemTotalCountForTA(params);
	}

	/**
	 * 获取日程表课程列表
	 * @param startDateStr 开始时间
	 * @param endDateStr 结束时间
	 * @param userEntId 用户ID
	 * @param itemType 课程类型
	 * @return 课程集合列表
	 */
	public List<AeItem> scheduleItemList(String startDateStr, String endDateStr,long userEntId,String itemType) {
		
		Date startDate = DateUtil.stringToDate(startDateStr);
		Date endDate = DateUtil.stringToDate(endDateStr);
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("start", startDate);
		params.put("end", endDate);
		params.put("userEntId", userEntId);
		params.put("itemType", itemType);
		List<AeItem> list =  aeItemMapper.getLearningMap(params);
		for(AeItem itm : list) {
			ImageUtil.combineImagePath(itm);
		}
		
		return list;
	}
	
	/**
	 * 当前角色是否拥有对应课程操作权限
	 * @param aeItem
	 * @param current_role
	 * @return
	 * @throws SQLException 
	 */
	public Map getHasRolePrivilege(AeItem aeItem,String current_role) throws SQLException{
		Map<String,Boolean> map =new HashMap<String,Boolean>();
		boolean exam_ind = false;
		boolean hasItmCosMain = false;
		boolean hasContentMain = false;
		boolean hasResultMain = false;
		boolean hasEnrollMain = false;
		boolean hasTeachingCourse = false;
		//是否开放CPT/D功能
		boolean hasCPT = AccessControlWZB.hasCPDFunction();
		if(aeItem.getItm_exam_ind() > 0){
			exam_ind = true;
		}
		boolean has_mod = false;
		if(null != aeItem.getCourse() && null != aeItem.getCourse().getCos_structure_xml()){
			has_mod = true;
		}
		//公开课程
		if(AeItem.AUDIOVIDEO.equalsIgnoreCase(aeItem.getItm_type())){
			hasItmCosMain = AccessControlWZB.hasRolePrivilege(current_role, AclFunction.FTN_AMD_OPEN_COS_MAIN );
			hasContentMain = AccessControlWZB.hasRolePrivilege(current_role, AclFunction.FTN_AMD_OPEN_COS_MAIN );
			hasResultMain = AccessControlWZB.hasRolePrivilege(current_role, AclFunction.FTN_AMD_OPEN_COS_MAIN );
			hasEnrollMain = AccessControlWZB.hasRolePrivilege(current_role, AclFunction.FTN_AMD_OPEN_COS_MAIN );
			hasTeachingCourse = AccessControlWZB.hasRolePrivilege(current_role, AclFunction.FTN_AMD_TEACHING_COURSE_LIST );
			//公开课程
		}else if(exam_ind){
			//考试
			hasItmCosMain = AccessControlWZB.hasRolePrivilege(current_role, AclFunction.FTN_AMD_EXAM_MAIN_VIEW );
			hasContentMain = AccessControlWZB.hasRolePrivilege(current_role, AclFunction.FTN_AMD_EXAM_MAIN_CONTENT );
			hasResultMain = AccessControlWZB.hasRolePrivilege(current_role, AclFunction.FTN_AMD_EXAM_MAIN_PERFORMANCE );
			hasEnrollMain = AccessControlWZB.hasRolePrivilege(current_role, AclFunction.FTN_AMD_EXAM_MAIN_APPLICATION );
			hasTeachingCourse = AccessControlWZB.hasRolePrivilege(current_role, AclFunction.FTN_AMD_TEACHING_COURSE_LIST );
			
		}else{
			//课程
			hasItmCosMain = AccessControlWZB.hasRolePrivilege(current_role, AclFunction.FTN_AMD_ITM_COS_MAIN_VIEW );
			hasContentMain = AccessControlWZB.hasRolePrivilege(current_role, AclFunction.FTN_AMD_ITM_COS_MAIN_CONTENT );
			hasResultMain = AccessControlWZB.hasRolePrivilege(current_role, AclFunction.FTN_AMD_ITM_COS_MAIN_PERFORMANCE );
			hasEnrollMain = AccessControlWZB.hasRolePrivilege(current_role, AclFunction.FTN_AMD_ITM_COS_MAIN_APPLICATION );
			hasTeachingCourse = AccessControlWZB.hasRolePrivilege(current_role, AclFunction.FTN_AMD_TEACHING_COURSE_LIST );
		}
		
		boolean has_run = false;
		Long itm_create_run_ind = aeItem.getItm_create_run_ind();
		if(itm_create_run_ind == 1){
			Map<String , Object> itm_map =  new HashMap<String, Object>();
			itm_map.put("itm_id", aeItem.getItm_id());
			List chril_itm_lst =  this.aeItemMapper.getChItemIDList(itm_map);//itm.getChItemIDList(con, itm_id); aeItemMapper.getLearningMap(params);
			if(chril_itm_lst== null || chril_itm_lst.size() < 1){
			    has_run = false;
			}else{
				has_run = true;
			}
		}
		
		map.put("has_run", has_run);
		map.put("has_mod", has_mod);
		map.put("hasItmCosMain", hasItmCosMain);
		map.put("hasContentMain", hasContentMain);
		map.put("hasResultMain", hasResultMain);
		map.put("hasEnrollMain", hasEnrollMain);
		map.put("hasTeachingCourse", hasTeachingCourse);
		map.put("hasCPT", hasCPT);
		return map;
	}

	/**
	 * 解析api
	 * @param model
	 * @param url
	 */
	public void parseOnlineAPI(Model model, String url) {
		try {
			url = url.substring(0, url.indexOf("?url=") + 5) + URLEncoder.encode(url.substring(url.indexOf("?url=") + 5), "UTF-8") + "&format=json";
			String jsonStr = HttpClientUtils.doGet(url, null);
			if(jsonStr != null && !"".equals(jsonStr)) {
				JSONObject jobj = JSONObject.fromObject(jsonStr);
				if("rich".equals(jobj.getString("type")) || "video".equals(jobj.getString("type"))) {
					String[] arr_jobj = jobj.getString("html").split(" ");
					String link = "";
					for(int i=0; i<arr_jobj.length; i++) {
						if(arr_jobj[i].indexOf("src=\"") != -1) {
							link = arr_jobj[i].substring(arr_jobj[i].indexOf("\"") + 1, arr_jobj[i].lastIndexOf("\""));
						}
					}
					model.addAttribute("success", true);
					model.addAttribute("link", link);
				} else {
					model.addAttribute("success", false);
				}
			} else {
				model.addAttribute("success", false);
			}
		} catch (UnsupportedEncodingException e) {
			model.addAttribute("success", false);
		}
	}


}