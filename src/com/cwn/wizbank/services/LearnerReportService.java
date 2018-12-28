package com.cwn.wizbank.services;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.dbAiccPath;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.util.cwSQL;
import com.cwn.wizbank.entity.AeApplication;
import com.cwn.wizbank.entity.CourseEvaluation;
import com.cwn.wizbank.entity.vo.*;
import com.cwn.wizbank.persistence.LearnerReportMapper;
import com.cwn.wizbank.utils.CwnUtil;
import com.cwn.wizbank.utils.DateUtil;
import com.cwn.wizbank.utils.FormatUtil;
import com.cwn.wizbank.utils.LabelContent;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LearnerReportService {
	
	
	@Autowired
	TcTrainingCenterService tcTrainingCenterService;

	@Autowired
	LearnerReportMapper learnerReportMapper;
	
	@Autowired
	ForCallOldAPIService forCallOldAPIService;
	
	private static int course_type_online_course = 0; // 0:网上课程
	private static int course_type_offline_course = 1; // 0:网上课程
	private static int course_type_online_exam = 2; // 0:网上课程
	private static int course_type_offline_exam = 3; // 0:网上课程
	private static int course_type_integrated = 4; // 0:网上课程
	
	
	private void setChildCatalogName(List<CatalogTreeVo> parentList , CatalogTreeVo childCatalog ){
	
		if(null != childCatalog.getParentTndId()){
			long lastNextParent = 0;
			for(int i = 0 ; i < parentList.size();i++){
				CatalogTreeVo parent = parentList.get(i);
				if(parent.getNextParent()==childCatalog.getParentTndId()){
					if(lastNextParent != parent.getNextParent() ){
						lastNextParent = parent.getNextParent();
						parent.setTndTitle(parent.getTndTitle() + " > " +childCatalog.getTndTitle());
						parent.setNextParent(childCatalog.getTndId());
					}
				}
			}
		}
	}
	
	public Map getLearnerReportByUser(LearnerReportParamVo param) throws SQLException{
		List<Integer> ctList = param.getCourseType();
		if(null!=ctList){
			List courseTypeCondition = new ArrayList();
			for(Integer i : ctList){
				// 0:网上课程  1:面授课程  2：网上考试  3:离线考试 4:项目式培训
				if(0==i){
					courseTypeCondition.add("( itm_run_ind = 0 and itm_apply_ind=1 and itm_integrated_ind = 0 and itm_exam_ind = 0)");
				}else if(1==i){
					//因为关联的是班级 所以面授课程所以过滤的是班级
					courseTypeCondition.add("( itm_run_ind = 1 and itm_create_run_ind = 0 and itm_integrated_ind = 0 and itm_exam_ind = 0)");
				}else if(2==i){
					courseTypeCondition.add("( itm_run_ind = 0 and itm_apply_ind = 1 and itm_integrated_ind = 0 and itm_exam_ind = 1)");
				}else if(3==i){
					//因为关联的是班级 所以面授课程所以过滤的是班级
					courseTypeCondition.add("( itm_run_ind = 1 and itm_create_run_ind = 0 and itm_integrated_ind = 0 and itm_exam_ind = 1)");
				}else if(4==i){
					courseTypeCondition.add("( itm_integrated_ind = 1)");
				}
			}
			param.setCourseTypeCondition(courseTypeCondition);
		}
		List<LearnerDetailReportVo> reportList = learnerReportMapper.getLearnerReportByUser(param);
		Map<Long,LearnerUserStatisticalReportVo> statisMap = new LinkedHashMap<Long,LearnerUserStatisticalReportVo>();
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("includeDelUser", param.isIncludeDelUser());
		
		Map<String,Map> groupMap = new HashMap<String,Map>();
		Map<Integer,String> gradeMap = new HashMap<Integer,String>();
		Map<Integer,Integer> attempMap = new HashMap<Integer,Integer>();
		Map<Integer,List<CatalogTreeVo>> catalogMap = new HashMap<Integer,List<CatalogTreeVo>>();
		
		List<Map> groupsList = learnerReportMapper.getUserGroupData(map);
		for(Map g : groupsList){
			String uid = g.get("usrId").toString();
			String status = g.get("usrStatus").toString();
			groupMap.put(String.format("%s-%s", uid, status), g);
		}

		if(null!= param.getIsExportDetail() && true == param.getIsExportDetail() ){
			
			List<Map> gradesList = learnerReportMapper.getUserGradeData(map);
			if(null!=gradesList){
				for(Map g : gradesList){
					Integer uid = null;
					if(cwSQL.DBVENDOR_ORACLE.equalsIgnoreCase(cwSQL.getDbType())){
						uid = ((BigDecimal)g.get("usrId")).intValue();
					}else{
						uid = (Integer)g.get("usrId");
					}
					String gTitle = (String)g.get("ugrTitle");
					gradeMap.put(uid, gTitle);
				}
			}

			List<Map> attempList = learnerReportMapper.getAttemptCount();
			for(Map g : attempList){
				Integer tkhId = null;
				Integer attemptCount = null;
				if(cwSQL.DBVENDOR_ORACLE.equalsIgnoreCase(cwSQL.getDbType())){
					tkhId = ((BigDecimal)g.get("tkhId")).intValue();
				}else{
					tkhId = (Integer)g.get("tkhId");
				}
				if(cwSQL.DBVENDOR_ORACLE.equalsIgnoreCase(cwSQL.getDbType()) || cwSQL.DBVENDOR_MYSQL.equalsIgnoreCase(cwSQL.getDbType())){
					attemptCount = ((BigDecimal)g.get("attemptCount")).intValue();
				}else if(cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType())){
					attemptCount = (Integer)g.get("attemptCount");
				}
				attempMap.put(tkhId, attemptCount);
			}
			
			catalogMap = getCatalogTree();
		}
		
		int totleUserCount = 0; //总学习人数
		int enrollUserCount = 0 ;//报名人数
		Double totleCovTotleTime = 0d;//学习总时长
		int inProgressCount = 0; //进行中状态
		int failCount = 0; //不合格状态
		int withdrawnCount = 0; //放弃状态
		int completedCount = 0 ;//已完成
		int penddingCount = 0;//等待审批
		int waitingCount = 0;//等待队列
		int rejectedCount = 0; //拒绝队列
		Double avgEnrollUserCount = 0d; //人均报名次数
		Double avgCovTotleTime = 0d; //人均在线学习时长
		Double failPercentage = 0d;//不合格百分比
		Double withdrawnPercentage = 0d;//放弃百分比
		Double completedPercentage = 0d;//已完成百分比
		Double penddingPercentage = 0d;//等待审批百分比
		Double waitingPercentage = 0d;//等待百分比
		Double rejectedPercentage = 0d;//拒绝百分比
		Double inProgressPercentage = 0d;//进行中状态百分比
		List<Long> itemIdList = new ArrayList<Long>();//累加过学分的课程/考试 id数组
		
		Map<Long,Long> totleUserMap = new HashMap<Long,Long>();
		LearnerUserStatisticalReportVo tmp = null;
		boolean tkh = false;
		if(null!=param.getOtherInfo()){
			for(Object others : param.getOtherInfo()){
				String other = (String)others;
				//判断其他条件里是否有 "尝试次数"选项
				if(other.equalsIgnoreCase("tkh")){
					tkh = true;
				}
			}
		}
		for(LearnerDetailReportVo vo : reportList ){
			
			Integer userEntId = vo.getUserEntId().intValue();
			String groupKey = String.format("%s-%s", vo.getUserEntId().toString(), vo.getUsrStatus().toUpperCase());
			if(groupMap.size()>0 && null!=groupMap.get(groupKey)){
				Map group = groupMap.get(groupKey);
				Long gId = Long.valueOf(group.get("usgEntId").toString());

				if(null!=vo.getUsrStatus()&&vo.getUsrStatus().equalsIgnoreCase("DELETED")){
					vo.setGruopName(LabelContent.get(param.getLanguage(), "label_core_report_159"));
				}else{
					if(gId==1L){
						vo.setGruopName("--");
					}else{
						if( null == group.get("usgTitle")){
							vo.setGruopName("--");
						}else{
							String groupFullName = forCallOldAPIService.getEntityFullPath(null, gId);
							vo.setGruopName(groupFullName);
						}
					}
				}
				
			}
			
			if(null!= param.getIsExportDetail() && true == param.getIsExportDetail() ){
				if(gradeMap.size()>0 && null!=gradeMap.get(userEntId)){
					vo.setGradeName(gradeMap.get(userEntId));
				}
				
				if(attempMap.size()>0 ){
					if(null!=attempMap.get(vo.getCov_tkh_id())){
						vo.setAttemptCount(attempMap.get(vo.getCov_tkh_id()));
					}else{
						vo.setAttemptCount(0);
					}
				}
				
				if(catalogMap.size()>0){
					if(null!=vo.getItmId()){
						List<CatalogTreeVo> catalogList =null;
						if(this.isOfflineCourse(vo) || this.isOfflineExam(vo)){
							if(null!=vo.getP_itm_id()){
								catalogList = catalogMap.get(vo.getP_itm_id().intValue());
							}
						}else{
							if(null!=vo.getItmId()){
								catalogList = catalogMap.get(vo.getItmId().intValue());
							}
						}
						StringBuffer sb = null;
						if(null!=catalogList){
							sb = new StringBuffer();
							for(int i = 0 ;i<catalogList.size();i++){
								sb.append(catalogList.get(i).getTndTitle());
								if(i<catalogList.size()-1){
									sb.append("\r\n");
								}
							}
						}
						if(null!=sb){
							vo.setCatalog(sb.toString());
						}
					}
				}
			}

			//添加统计用户信息列表
			if(null==statisMap.get(vo.getUserEntId())){
				//置空list
				itemIdList = new ArrayList<Long>();
				LearnerUserStatisticalReportVo statVo = new LearnerUserStatisticalReportVo();
				statVo.setUserEntId(vo.getUserEntId());
				statVo.setUserDispalyName(vo.getUserDispalyName());
				statVo.setUserFullName(vo.getUserFullName());
				statVo.setTotleScore(0d);
				statVo.setMaxScore(0d);
				statVo.setTotalIesCredit(0d);
				//当vo.getCovScore()不等于null时，给出一个足够大的数(99999999)，以便下方分数操作可以对比出最小分数
				if(vo.getCovScore() != null){
					statVo.setMinScore(99999999d);
				}else{
					statVo.setMinScore(0d);
				}
				statVo.setGourpName(vo.getGruopName());
				statVo.setStaffNo(vo.getStaffNo());
				statVo.setEnrollCount(0);
				statisMap.put(vo.getUserEntId(), statVo);
				tmp = statVo;
			}else{
				tmp = statisMap.get(vo.getUserEntId());
			}
	
			
			//处理学习人数，userEntID相同的数据都算成一个人
			Long id = totleUserMap.get(vo.getUserEntId());
			if(null==id || id==0){
				totleUserCount++;
				totleUserMap.put(vo.getUserEntId(),vo.getUserEntId());
			}
			
			//处理报名人数
			if(null!=vo.getAppId() && vo.getAppId()>0){
				enrollUserCount++;
				tmp.setEnrollCount(tmp.getEnrollCount()+1);
			}
			
			//计算学习总时长
			if(null!=vo.getCovTotalTime()){
				totleCovTotleTime += vo.getCovTotalTime();
				tmp.setTotleCovTotleTime(tmp.getTotleCovTotleTime()+vo.getCovTotalTime());
				tmp.setAvgCovTotleTime(doubleDivide(tmp.getTotleCovTotleTime(),Double.valueOf(tmp.getEnrollCount()),false));
				if(tmp.getTotleCovTotleTime()!=0d){
					tmp.setTotleCovTotleDisplayTime(isNullOrZero(dbAiccPath.getTime(tmp.getTotleCovTotleTime().floatValue()),"",false));
				}else{
					tmp.setTotleCovTotleDisplayTime(null);
				}
				if(tmp.getAvgCovTotleTime()!=0d){
					tmp.setAvgCovTotleDisplayTime(isNullOrZero(dbAiccPath.getTime(tmp.getAvgCovTotleTime().floatValue()),"",false));
				}else{
					tmp.setAvgCovTotleDisplayTime(null);
				}
			}
			
			//课程状态统计
			if(null!=vo.getCovStatus()){
				if(vo.getCovStatus().equalsIgnoreCase(CourseEvaluation.InProgress)){//进行中状态统计
					inProgressCount++;
					tmp.setInProgressCount(tmp.getInProgressCount()+1);
				}else if(vo.getCovStatus().equalsIgnoreCase(CourseEvaluation.FAIL)){ 	//不合格状态统计
					failCount++;
					tmp.setFailCount(tmp.getFailCount()+1);
				}else if(vo.getCovStatus().equalsIgnoreCase(CourseEvaluation.Withdrawn)){ 	//放弃状态统计
					withdrawnCount++;
					tmp.setWithdrawnCount(tmp.getWithdrawnCount()+1);
				}else if(vo.getCovStatus().equalsIgnoreCase(CourseEvaluation.Completed)){ 	//已完成状态统计
					completedCount++;
					tmp.setCompletedCount(tmp.getCompletedCount()+1);
				}
			}
			
			if(null!=vo.getAppStatus()){
				if(vo.getAppStatus().equalsIgnoreCase(AeApplication.APP_STATUS_PENDING)){//等待审批队列
					penddingCount++;
					tmp.setPenddingCount(tmp.getPenddingCount()+1);
				}else if(vo.getAppStatus().equalsIgnoreCase(AeApplication.APP_STATUS_WAITING)){//等待队列
					waitingCount++;
					tmp.setWaitingCount(tmp.getWaitingCount()+1);
				}else if(vo.getAppStatus().equalsIgnoreCase(AeApplication.APP_STATUS_REJECTED)){//拒绝队列
					rejectedCount++;
					tmp.setRejectedCount(tmp.getRejectedCount()+1);
				}
			}
			if(null!=vo.getAppStatus() || null!=vo.getCovStatus()){
				tmp.setInProgressPercentage(doubleDivide(tmp.getInProgressCount(),tmp.getEnrollCount(),true));
				tmp.setFailPercentage(doubleDivide(tmp.getFailCount(),tmp.getEnrollCount(),true));
				tmp.setWithdrawnPercentage(doubleDivide(tmp.getWithdrawnCount(),tmp.getEnrollCount(),true));
				tmp.setCompletedPercentage(doubleDivide(tmp.getCompletedCount(),tmp.getEnrollCount(),true));
				tmp.setPenddingPercentage(doubleDivide(tmp.getPenddingCount(),tmp.getEnrollCount(),true));
				tmp.setWaitingPercentage(doubleDivide(tmp.getWaitingCount(),tmp.getEnrollCount(),true));
				tmp.setRejectedPercentage(doubleDivide(tmp.getRejectedCount(),tmp.getEnrollCount(),true));
			}
			
			//每个用户的出席率
			if(null!=vo.getAttRate()){
				tmp.setTotleAttRate(tmp.getTotleAttRate()+vo.getAttRate());
				tmp.setAvgAttRatePercentage(doubleDivide(tmp.getTotleAttRate(),Double.valueOf(tmp.getEnrollCount()),false));
			}

			//每个用户的分数操作
			if(null!=vo.getCovScore()){
				tmp.setTotleScore(tmp.getTotleScore()+vo.getCovScore());
				if(tmp.getMaxScore() == null || vo.getCovScore() > tmp.getMaxScore()){
					tmp.setMaxScore(new BigDecimal(vo.getCovScore()).divide(new BigDecimal(1), 1, BigDecimal.ROUND_HALF_UP).doubleValue());
				}
				if(tmp.getMinScore() == null || (vo.getCovScore()<tmp.getMinScore())){
					tmp.setMinScore(new BigDecimal(vo.getCovScore()).divide(new BigDecimal(1), 1, BigDecimal.ROUND_HALF_UP).doubleValue());
				}
				tmp.setAvgScore(doubleDivide(tmp.getTotleScore(),Double.valueOf(tmp.getEnrollCount()),false));
			}
			if(null!=vo.getIesCredit() && vo.getIesCredit()>0){//如果已完成课程并且该课程存在学分,并且该课程的学分未累加过
			    if(CourseEvaluation.Completed.equalsIgnoreCase(vo.getCovStatus()) && !itemIdList.contains(vo.getItmId())){
			    	itemIdList.add(vo.getItmId());
			    	if(null!=tmp.getTotalIesCredit()){
			    		tmp.setTotalIesCredit(tmp.getTotalIesCredit()+vo.getIesCredit());
			    	}else{
			    		tmp.setTotalIesCredit((double) 0+vo.getIesCredit());
			    	}
			        vo.setFinalCredit(vo.getIesCredit());
			    }else{
			    	if(null!=tmp.getTotalIesCredit()){
			    		tmp.setTotalIesCredit(tmp.getTotalIesCredit()+0);
			    	}else{
			    		tmp.setTotalIesCredit((double) 0);
			    	}
                    vo.setFinalCredit((double) 0);
			    }
			    tmp.setHasIesCredit(true);
                
			}
			//判断选项条件里面有没选对应的状态 如果没有给NULL 不显示
			if(!containStatus(param.getCourseStatus(),CourseEvaluation.InProgress)){
				tmp.setInProgressCount(null);
				tmp.setInProgressPercentage(null);
			}
			
			if(!containStatus(param.getCourseStatus(),CourseEvaluation.FAIL)){
				tmp.setFailCount(null);
				tmp.setFailPercentage(null);
			}
			
			if(!containStatus(param.getCourseStatus(),CourseEvaluation.Withdrawn)){
				tmp.setWithdrawnCount(null);
				tmp.setWithdrawnPercentage(null);
			}
			
			if(!containStatus(param.getCourseStatus(),CourseEvaluation.Completed)){
				tmp.setCompletedCount(null);
				tmp.setCompletedPercentage(null);
			}
			
			if(!containStatus(param.getAppStatus(),AeApplication.APP_STATUS_PENDING)){
				tmp.setPenddingCount(null);
				tmp.setPenddingPercentage(null);
			}
			
			if(!containStatus(param.getAppStatus(),AeApplication.APP_STATUS_WAITING)){
				tmp.setWaitingCount(null);
				tmp.setWaitingPercentage(null);
			}
			
			if(!containStatus(param.getAppStatus(),AeApplication.APP_STATUS_REJECTED)){
				tmp.setRejectedCount(null);
				tmp.setRejectedPercentage(null);
			}
			
			statisMap.put(tmp.getUserEntId(), tmp);
		}
		//计算概总信息
		avgEnrollUserCount = this.doubleDivide(enrollUserCount,totleUserCount,false);
		avgCovTotleTime = this.doubleDivide(totleCovTotleTime,Double.valueOf(totleUserCount),false);
		failPercentage = this.doubleDivide(failCount,enrollUserCount,true);
		withdrawnPercentage = this.doubleDivide(withdrawnCount,enrollUserCount,true);
		completedPercentage = this.doubleDivide(completedCount,enrollUserCount,true);
		penddingPercentage = this.doubleDivide(penddingCount,enrollUserCount,true);
		waitingPercentage = this.doubleDivide(waitingCount,enrollUserCount,true);
		rejectedPercentage = this.doubleDivide(rejectedCount,enrollUserCount,true);
		inProgressPercentage = this.doubleDivide(inProgressCount,enrollUserCount,true);
		
		//用户信息统计列表
		List<LearnerUserStatisticalReportVo> userStatiscticalReportList = new ArrayList<LearnerUserStatisticalReportVo>(statisMap.values()); 
		Map resultMap = new HashMap<String,Object>();
		resultMap.put("reportList", userStatiscticalReportList);//用户信息统计列表
		resultMap.put("detailReportList", reportList);//用户用户信息
		resultMap.put("totleUserCount", totleUserCount);
		resultMap.put("enrollUserCount", enrollUserCount);
		resultMap.put("totleCovTotleTime", dbAiccPath.getTime(totleCovTotleTime.floatValue()));
		resultMap.put("inProgressCount", inProgressCount);
		resultMap.put("failCount", failCount);
		resultMap.put("withdrawnCount", withdrawnCount);
		resultMap.put("completedCount", completedCount);
		resultMap.put("penddingCount", penddingCount);
		resultMap.put("waitingCount", waitingCount);
		resultMap.put("rejectedCount", rejectedCount);
		resultMap.put("avgEnrollUserCount", avgEnrollUserCount);
		resultMap.put("avgCovTotleTime", dbAiccPath.getTime(avgCovTotleTime.floatValue()));
		resultMap.put("failPercentage", failPercentage);
		resultMap.put("withdrawnPercentage", withdrawnPercentage);
		resultMap.put("completedPercentage", completedPercentage);
		resultMap.put("penddingPercentage", penddingPercentage);
		resultMap.put("waitingPercentage", waitingPercentage);
		resultMap.put("rejectedPercentage", rejectedPercentage);
		resultMap.put("inProgressPercentage", inProgressPercentage);
		
		//用于饼图显示
		List<PieVo> pieList = new ArrayList<PieVo>();
		if(null!=param.getCourseStatus()){
			for(int i = 0 ; i < param.getCourseStatus().size() ; i++){
				String status = (String)param.getCourseStatus().get(i);
				PieVo pvo = new PieVo();
				if(status.equalsIgnoreCase(CourseEvaluation.InProgress)){
					pvo.setLabel(LabelContent.get(param.getLanguage(), "label_core_report_65"));
					pvo.setValue(Double.valueOf(inProgressCount));
					pvo.setPercentage(inProgressPercentage);
				}
				if(status.equalsIgnoreCase(CourseEvaluation.FAIL)){
					pvo.setLabel(LabelContent.get(param.getLanguage(), "label_core_report_67"));
					pvo.setValue(Double.valueOf(failCount));
					pvo.setPercentage(failPercentage);
				}
				if(status.equalsIgnoreCase(CourseEvaluation.Completed)){
					pvo.setLabel(LabelContent.get(param.getLanguage(), "label_core_report_66"));
					pvo.setValue(Double.valueOf(completedCount));
					pvo.setPercentage(completedPercentage);
				}
				if(status.equalsIgnoreCase(CourseEvaluation.Withdrawn)){
					pvo.setLabel(LabelContent.get(param.getLanguage(), "label_core_report_68"));
					pvo.setValue(Double.valueOf(withdrawnCount));
					pvo.setPercentage(withdrawnPercentage);
				}
				pieList.add(pvo);
			}
		}
		if(null!=param.getAppStatus()){
			for(int i = 0 ; i < param.getAppStatus().size() ; i++){
				String status = (String)param.getAppStatus().get(i);
				PieVo pvo = new PieVo();
				if(status.equalsIgnoreCase(AeApplication.APP_STATUS_PENDING)){
					pvo.setLabel(LabelContent.get(param.getLanguage(), "label_core_report_62"));
					pvo.setValue(Double.valueOf(penddingCount));
					pvo.setPercentage(penddingPercentage);
				}
				if(status.equalsIgnoreCase(AeApplication.APP_STATUS_WAITING)){
					pvo.setLabel(LabelContent.get(param.getLanguage(), "label_core_report_63"));
					pvo.setValue(Double.valueOf(waitingCount));
					pvo.setPercentage(waitingPercentage);
				}
				if(status.equalsIgnoreCase(AeApplication.APP_STATUS_REJECTED)){
					pvo.setLabel(LabelContent.get(param.getLanguage(), "label_core_report_64"));
					pvo.setValue(Double.valueOf(rejectedCount));
					pvo.setPercentage(rejectedPercentage);
				}
				pieList.add(pvo);
			}
		}
		
		resultMap.put("pieDataTotleValue",
				inProgressCount + failCount + completedCount + withdrawnCount
				 + penddingCount + waitingCount + rejectedCount); //如果总数为0则在页面不显示导航
		resultMap.put("pieDataList", pieList);
		return resultMap;
	}
		
	
	private Double doubleDivide(Integer number1 ,Integer number2,boolean isPercentage){
		if(null==number2 || 0==number2 || null==number1 || 0==number1){
			return 0d;
		}
		return doubleDivide(Double.valueOf(number1) , Double.valueOf(number2),isPercentage);
	}
	
	private Double doubleDivide(Double number1 ,Double number2,boolean isPercentage){
		if(null==number2 || 0d==number2 || null==number1 || 0d==number1){
			return 0d;
		}
		FormatUtil formatUtil = FormatUtil.getInstance();
		Double value = new BigDecimal(number1).divide(new BigDecimal(number2), 1, BigDecimal.ROUND_HALF_UP).doubleValue();
		if(isPercentage){
			value = value * 100;
		}
		return formatUtil.scaleDouble(value, 2);
	}
	
	public Map getLearnerReportByCourse(LearnerReportParamVo param) throws SQLException{
		List<Integer> ctList = param.getCourseType();
		if(null!=ctList){
			List courseTypeCondition = new ArrayList();
			for(Integer i : ctList){
				// 0:网上课程  1:面授课程  2：网上考试  3:离线考试 4:项目式培训
				if(0==i){
					courseTypeCondition.add("( itm_run_ind = 0 and itm_apply_ind=1 and itm_integrated_ind = 0 and itm_exam_ind = 0 )");
				}else if(1==i){
					courseTypeCondition.add("( itm_run_ind = 0 and itm_create_run_ind = 1 and itm_integrated_ind = 0 and itm_exam_ind = 0)");
				}else if(2==i){
					courseTypeCondition.add("( itm_run_ind = 0 and itm_apply_ind = 1 and itm_integrated_ind = 0 and itm_exam_ind = 1)");
				}else if(3==i){
					courseTypeCondition.add("( itm_run_ind = 0 and itm_create_run_ind = 1 and itm_integrated_ind = 0 and itm_exam_ind = 1)");
				}else if(4==i){
					courseTypeCondition.add("( itm_integrated_ind = 1)");
				}
			}
			param.setCourseTypeCondition(courseTypeCondition);
		}
		
		//获取详细信息
		List<LearnerDetailReportVo> reportList = learnerReportMapper.getLearnerReportByCourse(param);
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("includeDelUser", param.isIncludeDelUser());
		
		Map<String,Map> groupMap = new HashMap<String,Map>();
		Map<Integer,String> gradeMap = new HashMap<Integer,String>();
		Map<Integer,Integer> attempMap = new HashMap<Integer,Integer>();
		Map<Integer,List<CatalogTreeVo>> catalogMap = new HashMap<Integer,List<CatalogTreeVo>>();
		
		List<Map> groupsList = learnerReportMapper.getUserGroupData(map);
		for(Map g : groupsList){
			String uid = g.get("usrId").toString();
			String status = g.get("usrStatus").toString();
			groupMap.put(String.format("%s-%s", uid, status), g);
		}

		if(null!= param.getIsExportDetail() && true == param.getIsExportDetail() ){
			
			List<Map> gradesList = learnerReportMapper.getUserGradeData(map);
			if(null!=gradesList){
				for(Map g : gradesList){
					Integer uid = null;
					if(cwSQL.DBVENDOR_ORACLE.equalsIgnoreCase(cwSQL.getDbType())){
						uid = ((BigDecimal)g.get("usrId")).intValue();
					}else{
						uid = (Integer)g.get("usrId");
					}
					String gTitle = (String)g.get("ugrTitle");
					gradeMap.put(uid, gTitle);
				}
			}

			List<Map> attempList = learnerReportMapper.getAttemptCount();
			for(Map g : attempList){
				Integer tkhId = null;
				Integer attemptCount = null;
				if(cwSQL.DBVENDOR_ORACLE.equalsIgnoreCase(cwSQL.getDbType())){
					tkhId = ((BigDecimal)g.get("tkhId")).intValue();
				}else{
					tkhId = (Integer)g.get("tkhId");
				}
				if(cwSQL.DBVENDOR_ORACLE.equalsIgnoreCase(cwSQL.getDbType()) || cwSQL.DBVENDOR_MYSQL.equalsIgnoreCase(cwSQL.getDbType())){
					attemptCount = ((BigDecimal)g.get("attemptCount")).intValue();
				}else if(cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType())){
					attemptCount = (Integer)g.get("attemptCount");
				}
				
				attempMap.put(tkhId, attemptCount);
			}
			
			catalogMap = this.getCatalogTree();
		}

		boolean tkh = false;
		if(null!=param.getOtherInfo()){
			for(Object others : param.getOtherInfo()){
				String other = (String)others;
				//判断其他条件里是否有 "尝试次数"选项
				if(other.equalsIgnoreCase("tkh")){
					tkh = true;
				}
			}
		}
		
		Double totleCovTotleTime = 0d;//学习总时长
		int courseExamTotle = 0; //课程/考试总量
		int enrollUserCount = 0 ;//报名人数
		int onlineCourseCount = 0; //网上课程 
		int offlineCourseCount = 0; //面授课程
		int onlineExamCount = 0; //网上考试
		int offlineExamCount = 0; //离线考试
		int integratedCount = 0; //项目式培训
		int onlineCourseEnrollCount = 0; //网上课程 报名人数
		int offlineCourseEnrollCount = 0; //面授课程 报名人数
		int onlineExamEnrollCount = 0; //网上考试 报名人数
		int offlineExamEnrollCount = 0; //离线考试 报名人数
		int integratedEnrollCount = 0; //面授课程 报名人数
		Double onlineCourseCovTime = 0d; //网上课程学习时长
		Double offlineCourseCovTime = 0d; //面授课程学习时长
		Double onlineExamCovTime = 0d; //网上考试 报名学习时长
		Double offlineExamCovTime = 0d; //离线考试 报名学习时长
		Double integratedCovTime = 0d; //面授课程 报名学习时长
		Double avgErollCount = 0d; //平均报名人次
		Double avgCovTotleTime = 0d; //平均学习时长
		List<Long> userEntIdList = new ArrayList<Long>();//累加过学分的/学员
		
		Map<Long,Long> totleCourseMap = new HashMap<Long,Long>();
		Map<Long,LearnerCourseStatisticalReportVo> statisMap = new LinkedHashMap<Long,LearnerCourseStatisticalReportVo>();//分析MAP
		LearnerCourseStatisticalReportVo tmp = null;
		for(LearnerDetailReportVo vo : reportList ){
			String groupKey = "";
			if(null != vo.getUserEntId() && null !=  vo.getUsrStatus()){
				groupKey = String.format("%s-%s", vo.getUserEntId(), vo.getUsrStatus().toUpperCase());
			}
			if(null!=vo.getUserEntId() && groupMap.size()>0 
					&& null!=groupMap.get(groupKey)){
				Map group = groupMap.get(groupKey);
				Long gId = Long.valueOf(group.get("usgEntId").toString());

				if(null!=vo.getUsrStatus()&&vo.getUsrStatus().equalsIgnoreCase("DELETED")){
					vo.setGruopName(LabelContent.get(param.getLanguage(), "label_core_report_159"));
				}else{
					if(gId==1L){
						vo.setGruopName("--");
					}else{
						if( null == group.get("usgTitle")){
							vo.setGruopName("--");
						}else{
							String groupFullName = forCallOldAPIService.getEntityFullPath(null, gId);
							vo.setGruopName(groupFullName);
						}
					}
				}
			}
			
			if(null!= param.getIsExportDetail() && true == param.getIsExportDetail() ){
				if(null!=vo.getUserEntId() && gradeMap.size()>0 
						&& null!=gradeMap.get(vo.getUserEntId().intValue())){
					vo.setGradeName(gradeMap.get(vo.getUserEntId().intValue()));
				}
				
				if(attempMap.size()>0 ){
					if(null!=attempMap.get(vo.getCov_tkh_id())){
						vo.setAttemptCount(attempMap.get(vo.getCov_tkh_id()));
					}else{
						vo.setAttemptCount(0);
					}
				}
				
				if(catalogMap.size()>0){

					if(null!=vo.getItmId()){
						List<CatalogTreeVo> catalogList =null;
						//由于以课程为主已经在SQL里面按照类型给itmId判断赋值是p_itm_id 还是 itm_id所以这里只要赋值itm_id就可以了
						if(null!=vo.getItmId()){
							catalogList = catalogMap.get(vo.getItmId().intValue());
						}
						StringBuffer sb = null;
						if(null!=catalogList){
							sb = new StringBuffer();
							for(int i = 0 ;i<catalogList.size();i++){
								sb.append(catalogList.get(i).getTndTitle());
								if(i<catalogList.size()-1){
									sb.append("\r\n");
								}
							}
						}
						if(null!=sb){
							vo.setCatalog(sb.toString());
						}
					}
				}
			}

			if(null==statisMap.get(vo.getItmId())){
				//置空list
				userEntIdList = new ArrayList<Long>();
				LearnerCourseStatisticalReportVo statVo = new LearnerCourseStatisticalReportVo();
				statVo.setItmId(vo.getItmId());
				statVo.setItmTitle(vo.getItmTitle());
				statVo.setItmCode(vo.getItmCode());
				statVo.setTcName(vo.getTcrTitle());
				statVo.setIesCredit(vo.getIesCredit()==null ? (double) 0 : vo.getIesCredit());
				statVo.setTotalIesCredit( (double) 0);
				if(isOnlineCourse(vo)){ //是否网上课程
					statVo.setCourseType(0);
				}else if(isOfflineCourse(vo)){//是否面授课程
					statVo.setCourseType(1);
				}else if(isOnlineExam(vo)){//是否网上考试
					statVo.setCourseType(2);
				}else if(isOfflineExam(vo)){//是否离线考试
					statVo.setCourseType(3);
				}else if(isIntegrated(vo)){//是否项目式培训
					statVo.setCourseType(4);
				}
				statVo.setEnrollCount(0);
				statisMap.put(vo.getItmId(), statVo);
				tmp = statVo;
			}else{
				tmp = statisMap.get(vo.getItmId());
			}
			
			
			//处理统计课程/考试总量，itemId相同的数据都算成一个人
			Long id = totleCourseMap.get(vo.getItmId());
			if(null==id || id==0){
				if(vo.getItmRunInd()==0){ //itm_run_ind为0才是考试或者测验
					courseExamTotle++;
					totleCourseMap.put(vo.getItmId(),vo.getItmId());
				}
			}
			
			//处理报名人数
			boolean enroll = false;
			if(vo.getItmRunInd()==0 && null!=vo.getAppId() && vo.getAppId()>0){
				enrollUserCount++;
				tmp.setEnrollCount(tmp.getEnrollCount()+1);
				enroll = true;
			}
			
			//计算学习总时长
			if(vo.getItmRunInd()==0 && null!=vo.getCovTotalTime()){
				totleCovTotleTime += vo.getCovTotalTime();
				tmp.setTotleCovTotleTime(tmp.getTotleCovTotleTime()+vo.getCovTotalTime());
				tmp.setAvgCovTotleTime(doubleDivide(tmp.getTotleCovTotleTime(),Double.valueOf(tmp.getEnrollCount()),false));
				tmp.setTotleCovTotleDisplayTime(dbAiccPath.getTime(tmp.getTotleCovTotleTime().floatValue()));
				tmp.setAvgCovTotleDisplayTime(dbAiccPath.getTime(tmp.getAvgCovTotleTime().floatValue()));
			}
			
			if(isOnlineCourse(vo)){ //是否网上课程
				if(null==id || id==0){
					onlineCourseCount++;
				}
				if(enroll){onlineCourseEnrollCount++;}
				if(null!=vo.getCovTotalTime()){
					onlineCourseCovTime+=vo.getCovTotalTime();
				}
			}else if(isOfflineCourse(vo)){//是否面授课程
				if(null==id || id==0){offlineCourseCount++;}
				if(enroll){offlineCourseEnrollCount++;}
				if(null!=vo.getCovTotalTime()){
					offlineCourseCovTime+=vo.getCovTotalTime();
				}
			}else if(isOnlineExam(vo)){//是否网上考试
				if(null==id || id==0){onlineExamCount++;}
				if(enroll){onlineExamEnrollCount++;}
				if(null!=vo.getCovTotalTime()){
					onlineExamCovTime+=vo.getCovTotalTime();
				}
			}else if(isOfflineExam(vo)){//是否离线考试
				if(null==id || id==0){offlineExamCount++;}
				if(enroll){offlineExamEnrollCount++;}
				if(null!=vo.getCovTotalTime()){
					offlineExamCovTime+=vo.getCovTotalTime();
				}
			}else if(isIntegrated(vo)){//是否项目式培训
				if(null==id || id==0){integratedCount++;}
				if(enroll){integratedEnrollCount++;}
				if(null!=vo.getCovTotalTime()){
					integratedCovTime+=vo.getCovTotalTime();
				}
			}
			
			//课程状态统计
			if(null!=vo.getCovStatus()){
				if(vo.getCovStatus().equalsIgnoreCase(CourseEvaluation.InProgress)){//进行中状态统计
					tmp.setInProgressCount(tmp.getInProgressCount()+1);
				}else if(vo.getCovStatus().equalsIgnoreCase(CourseEvaluation.FAIL)){ 	//不合格状态统计
					tmp.setFailCount(tmp.getFailCount()+1);
				}else if(vo.getCovStatus().equalsIgnoreCase(CourseEvaluation.Withdrawn)){ 	//放弃状态统计
					tmp.setWithdrawnCount(tmp.getWithdrawnCount()+1);
				}else if(vo.getCovStatus().equalsIgnoreCase(CourseEvaluation.Completed)){ 	//已完成状态统计
					tmp.setCompletedCount(tmp.getCompletedCount()+1);
				}
			}
			
			if(null!=vo.getAppStatus()){
				if(vo.getAppStatus().equalsIgnoreCase(AeApplication.APP_STATUS_PENDING)){//等待审批队列
					tmp.setPenddingCount(tmp.getPenddingCount()+1);
				}else if(vo.getAppStatus().equalsIgnoreCase(AeApplication.APP_STATUS_WAITING)){//等待队列
					tmp.setWaitingCount(tmp.getWaitingCount()+1);
				}else if(vo.getAppStatus().equalsIgnoreCase(AeApplication.APP_STATUS_REJECTED)){//拒绝队列
					tmp.setRejectedCount(tmp.getRejectedCount()+1);
				}
			}
			
			if(null!=vo.getAppStatus() || null!=vo.getCovStatus()){
				tmp.setInProgressPercentage(doubleDivide(tmp.getInProgressCount(),tmp.getEnrollCount(),true));
				tmp.setFailPercentage(doubleDivide(tmp.getFailCount(),tmp.getEnrollCount(),true));
				tmp.setWithdrawnPercentage(doubleDivide(tmp.getWithdrawnCount(),tmp.getEnrollCount(),true));
				tmp.setCompletedPercentage(doubleDivide(tmp.getCompletedCount(),tmp.getEnrollCount(),true));
				tmp.setPenddingPercentage(doubleDivide(tmp.getPenddingCount(),tmp.getEnrollCount(),true));
				tmp.setWaitingPercentage(doubleDivide(tmp.getWaitingCount(),tmp.getEnrollCount(),true));
				tmp.setRejectedPercentage(doubleDivide(tmp.getRejectedCount(),tmp.getEnrollCount(),true));
			}
			
			if(null!=vo.getIesCredit()){//如果已完成课程并且该课程存在学分,并且该课程的学分未累加过
			    if(CourseEvaluation.Completed.equalsIgnoreCase(vo.getCovStatus()) && !userEntIdList.contains(vo.getUserEntId())){
			    	userEntIdList.add(vo.getUserEntId());
			        tmp.setTotalIesCredit(tmp.getTotalIesCredit()+vo.getIesCredit());
			        vo.setFinalCredit(vo.getIesCredit());
			    }else{
                    tmp.setTotalIesCredit(tmp.getTotalIesCredit()+0);
                    vo.setFinalCredit( (double) 0);
			    }
                
			}
			
			//判断选项条件里面有没选对应的状态 如果没有给NULL 不显示
			if(!containStatus(param.getCourseStatus(),CourseEvaluation.InProgress)){
				tmp.setInProgressCount(null);
				tmp.setInProgressPercentage(null);
			}
			
			if(!containStatus(param.getCourseStatus(),CourseEvaluation.FAIL)){
				tmp.setFailCount(null);
				tmp.setFailPercentage(null);
			}
			
			if(!containStatus(param.getCourseStatus(),CourseEvaluation.Withdrawn)){
				tmp.setWithdrawnCount(null);
				tmp.setWithdrawnPercentage(null);
			}
			
			if(!containStatus(param.getCourseStatus(),CourseEvaluation.Completed)){
				tmp.setCompletedCount(null);
				tmp.setCompletedPercentage(null);
			}
			
			if(!containStatus(param.getAppStatus(),AeApplication.APP_STATUS_PENDING)){
				tmp.setPenddingCount(null);
				tmp.setPenddingPercentage(null);
			}
			
			if(!containStatus(param.getAppStatus(),AeApplication.APP_STATUS_WAITING)){
				tmp.setWaitingCount(null);
				tmp.setWaitingPercentage(null);
			}
			
			if(!containStatus(param.getAppStatus(),AeApplication.APP_STATUS_REJECTED)){
				tmp.setRejectedCount(null);
				tmp.setRejectedPercentage(null);
			}
			
			
		}
		avgErollCount = doubleDivide(enrollUserCount,courseExamTotle,false);
		avgCovTotleTime = doubleDivide(totleCovTotleTime,Double.valueOf(courseExamTotle),false);
		
		List<LearnerCourseStatisticalReportVo> courseStatiscticalReportList = new ArrayList<LearnerCourseStatisticalReportVo>(statisMap.values()); 
		Map resultMap = new HashMap<String,Object>();
		resultMap.put("reportList", courseStatiscticalReportList);//用户用户信息
		resultMap.put("detailReportList", reportList);//用户用户信息
		resultMap.put("courseExamTotle", courseExamTotle);
		resultMap.put("enrollUserCount", enrollUserCount);
		resultMap.put("onlineCourseCount", onlineCourseCount);
		resultMap.put("offlineCourseCount", offlineCourseCount);
		resultMap.put("onlineExamCount", onlineExamCount);
		resultMap.put("offlineExamCount", offlineExamCount);
		resultMap.put("integratedCount", integratedCount);
		
		resultMap.put("onlineCourseCountPercentage", doubleDivide(onlineCourseCount,courseExamTotle,true));
		resultMap.put("offlineCourseCountPercentage", doubleDivide(offlineCourseCount,courseExamTotle,true));
		resultMap.put("onlineExamCountPercentage", doubleDivide(onlineExamCount,courseExamTotle,true));
		resultMap.put("offlineExamCountPercentage", doubleDivide(offlineExamCount,courseExamTotle,true));
		resultMap.put("integratedCountPercentage", doubleDivide(integratedCount,courseExamTotle,true));
		
		resultMap.put("onlineCourseEnrollCount", onlineCourseEnrollCount);
		resultMap.put("offlineCourseEnrollCount", offlineCourseEnrollCount);
		resultMap.put("onlineExamEnrollCount", onlineExamEnrollCount);
		resultMap.put("offlineExamEnrollCount", offlineExamEnrollCount);
		resultMap.put("integratedEnrollCount", integratedEnrollCount);
		
		resultMap.put("onlineCourseEnrollCountPercentage", doubleDivide(onlineCourseEnrollCount,enrollUserCount,true));
		resultMap.put("offlineCourseEnrollCountPercentage", doubleDivide(offlineCourseEnrollCount,enrollUserCount,true));
		resultMap.put("onlineExamEnrollCountPercentage", doubleDivide(onlineExamEnrollCount,enrollUserCount,true));
		resultMap.put("offlineExamEnrollCountPercentage", doubleDivide(offlineExamEnrollCount,enrollUserCount,true));
		resultMap.put("integratedEnrollCountPercentage", doubleDivide(integratedEnrollCount,enrollUserCount,true));
		
		resultMap.put("avgErollCount", avgErollCount);
		resultMap.put("totleCovTotleTime", dbAiccPath.getTime(totleCovTotleTime.floatValue()));
		resultMap.put("avgCovTotleTime", dbAiccPath.getTime(avgCovTotleTime.floatValue()));
		resultMap.put("onlineCourseCovTime", onlineCourseCovTime);
		resultMap.put("offlineCourseCovTime", offlineCourseCovTime);
		resultMap.put("onlineExamCovTime", onlineExamCovTime);
		resultMap.put("offlineExamCovTime", offlineExamCovTime);
		resultMap.put("integratedCovTime", integratedCovTime);
		
		resultMap.put("onlineCourseCovTimePercentage", doubleDivide(onlineCourseCovTime,totleCovTotleTime,true));
		resultMap.put("offlineCourseCovTimePercentage", doubleDivide(offlineCourseCovTime,totleCovTotleTime,true));
		resultMap.put("onlineExamCovTimePercentage", doubleDivide(onlineExamCovTime,totleCovTotleTime,true));
		resultMap.put("offlineExamCovTimePercentage", doubleDivide(offlineExamCovTime,totleCovTotleTime,true));
		resultMap.put("integratedCovTimePercentage", doubleDivide(integratedCovTime,totleCovTotleTime,true));
		
		resultMap.put("onlineCourseCovDisplayTime", dbAiccPath.getTime(onlineCourseCovTime.floatValue()));
		resultMap.put("offlineCourseCovDisplayTime", dbAiccPath.getTime(offlineCourseCovTime.floatValue()));
		resultMap.put("onlineExamCovDisplayTime", dbAiccPath.getTime(onlineExamCovTime.floatValue()));
		resultMap.put("offlineExamCovDisplayTime", dbAiccPath.getTime(offlineExamCovTime.floatValue()));
		resultMap.put("integratedCovDisplayTime", dbAiccPath.getTime(integratedCovTime.floatValue()));
		
		List<PieVo> pieList = new ArrayList<PieVo>();
		// 0:网上课程  1:面授课程  2：网上考试  3:离线考试 4:项目式培训
		pieList.add(new PieVo(String.valueOf(0),Double.valueOf(onlineCourseCount),(Double)resultMap.get("onlineCourseCountPercentage")));
		pieList.add(new PieVo(String.valueOf(1),Double.valueOf(offlineCourseCount),(Double)resultMap.get("offlineCourseCountPercentage")));
		pieList.add(new PieVo(String.valueOf(2),Double.valueOf(onlineExamCount),(Double)resultMap.get("onlineExamCountPercentage")));
		pieList.add(new PieVo(String.valueOf(3),Double.valueOf(offlineExamCount),(Double)resultMap.get("offlineExamCountPercentage")));
		pieList.add(new PieVo(String.valueOf(4),Double.valueOf(integratedCount),(Double)resultMap.get("integratedCountPercentage")));
		List<PieVo>coursePieList = filterCoursePieData(param,pieList);
		resultMap.put("coursePieTotleValue",
				onlineCourseCount+offlineCourseCount+onlineExamCount+offlineExamCount+integratedCount ); //如果总数为0则在页面不显示导航
		
		pieList.clear();
		pieList.add(new PieVo(String.valueOf(0),Double.valueOf(onlineCourseEnrollCount),(Double)resultMap.get("onlineCourseEnrollCountPercentage")));
		pieList.add(new PieVo(String.valueOf(1),Double.valueOf(offlineCourseEnrollCount),(Double)resultMap.get("offlineCourseEnrollCountPercentage")));
		pieList.add(new PieVo(String.valueOf(2),Double.valueOf(onlineExamEnrollCount),(Double)resultMap.get("onlineExamEnrollCountPercentage")));
		pieList.add(new PieVo(String.valueOf(3),Double.valueOf(offlineExamEnrollCount),(Double)resultMap.get("offlineExamEnrollCountPercentage")));
		pieList.add(new PieVo(String.valueOf(4),Double.valueOf(integratedEnrollCount),(Double)resultMap.get("integratedEnrollCountPercentage")));
		List<PieVo>enrollPieList = filterCoursePieData(param,pieList);
		resultMap.put("enrollPieTotleValue",
				onlineCourseEnrollCount + offlineCourseEnrollCount 
				+ onlineExamEnrollCount + offlineExamEnrollCount+integratedEnrollCount ); //如果总数为0则在页面不显示导航
		
		pieList.clear();
		pieList.add(new PieVo(String.valueOf(0),Double.valueOf(onlineCourseCovTime),(Double)resultMap.get("onlineCourseCovTimePercentage")));
		pieList.add(new PieVo(String.valueOf(1),Double.valueOf(offlineCourseCovTime),(Double)resultMap.get("offlineCourseCovTimePercentage")));
		pieList.add(new PieVo(String.valueOf(2),Double.valueOf(onlineExamCovTime),(Double)resultMap.get("onlineExamCovTimePercentage")));
		pieList.add(new PieVo(String.valueOf(3),Double.valueOf(offlineExamCovTime),(Double)resultMap.get("offlineExamCovTimePercentage")));
		pieList.add(new PieVo(String.valueOf(4),Double.valueOf(integratedCovTime),(Double)resultMap.get("integratedCovTimePercentage")));
		List<PieVo>covTimePieList = filterCoursePieData(param,pieList);
	
		resultMap.put("covTimePieTotleValue",
				onlineCourseCovTime + offlineCourseCovTime 
				+ onlineExamCovTime + offlineExamCovTime + integratedCovTime ); //如果总数为0则在页面不显示导航
		
		resultMap.put("coursePieList", coursePieList);
		resultMap.put("enrollPieList", enrollPieList);
		resultMap.put("covTimePieList", covTimePieList);
		return resultMap;
	}
	

	/**
	 * 导出报表
	 * @param prof
	 * @param vo
	 * @return
	 * @throws SQLException 
	 */
	public String expor(loginProfile prof, LearnerReportParamVo vo,WizbiniLoader wizbini) throws SQLException{
		Map data;
        if(vo.getResultDataStatistic() == 0){
        	 data = getLearnerReportByCourse(vo);
        	 //sheetName = LabelContent.get(prof.cur_lan, "label_core_report_70");//"统计数据以课程为主";
        }else{
        	 data = getLearnerReportByUser(vo);
        	// sheetName = LabelContent.get(prof.cur_lan, "label_core_report_72");//"统计数据以学员为主";
        }
        Object object = data.get("reportList");
		String sheetName = LabelContent.get(prof.cur_lan, "label_core_report_44");
		// 创建一个webbook
        HSSFWorkbook wb = new HSSFWorkbook(); 
        //导出  统计数据以学员为主
        HSSFSheet sheet = wb.createSheet(sheetName);
        sheet.setColumnWidth(0, 20*256);
         // 生成一个字体加粗样式
        HSSFCellStyle style = wb.createCellStyle();
        HSSFFont font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
        style.setFont(font);
        
        //标题字体
        HSSFCellStyle titleStyle = wb.createCellStyle();
        HSSFFont titlefont = wb.createFont();
        titlefont.setFontHeightInPoints((short) 12);// 设置字体大小
        titlefont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
        titleStyle.setFont(titlefont);
        
        //选择的条件   相同部分  返回当前写入的行数
        int rowIndex = setConditionToheard(sheet,style,titleStyle,vo,prof.cur_lan);
        
        if(vo.getResultDataStatistic() == 0){
       	 //统计数据以课程为主 信息内容 
        	 //生成统计信息条件  返回当前行数   
             rowIndex = setReportHeardByCourse(sheet,style,data,rowIndex,prof.cur_lan,vo);
        	//生成统计信息列表 头部
             List<String> labelsList = new ArrayList<String>();
             labelsList.add(LabelContent.get(prof.cur_lan, "label_core_report_84"));
             labelsList.add(LabelContent.get(prof.cur_lan, "label_core_report_85"));
             labelsList.add(LabelContent.get(prof.cur_lan, "label_core_report_86"));
             labelsList.add(LabelContent.get(prof.cur_lan, "label_core_report_87"));
             labelsList.add(LabelContent.get(prof.cur_lan, "label_core_report_110"));
             if(null!=vo.getCourseStatus() && vo.getCourseStatus().contains(CourseEvaluation.Completed)){
            	 labelsList.add(LabelContent.get(prof.cur_lan, "label_core_report_66"));
            	 labelsList.add("(%)");
             }
             if(null!=vo.getCourseStatus() && vo.getCourseStatus().contains(CourseEvaluation.FAIL)){
            	 labelsList.add(LabelContent.get(prof.cur_lan, "label_core_report_111"));
            	 labelsList.add("(%)");
             }
             if(null!=vo.getCourseStatus() && vo.getCourseStatus().contains(CourseEvaluation.Withdrawn)){
            	 labelsList.add(LabelContent.get(prof.cur_lan, "label_core_report_68"));
            	 labelsList.add("(%)");
             }
             if(null!=vo.getCourseStatus() && vo.getCourseStatus().contains(CourseEvaluation.InProgress)){
            	 labelsList.add(LabelContent.get(prof.cur_lan, "label_core_report_65"));
            	 labelsList.add("(%)");
             }
             if(null!=vo.getAppStatus() && vo.getAppStatus().contains(AeApplication.APP_STATUS_PENDING)){
            	 labelsList.add(LabelContent.get(prof.cur_lan, "label_core_report_112"));
            	 labelsList.add("(%)");
             }
             if(null!=vo.getAppStatus() && vo.getAppStatus().contains(AeApplication.APP_STATUS_REJECTED)){
            	 labelsList.add(LabelContent.get(prof.cur_lan, "label_core_report_64"));
            	 labelsList.add("(%)");
             }
             if(null!=vo.getAppStatus() && vo.getAppStatus().contains(AeApplication.APP_STATUS_WAITING)){
            	 labelsList.add(LabelContent.get(prof.cur_lan, "label_core_report_113"));
            	 labelsList.add("(%)");
             }
             labelsList.add(LabelContent.get(prof.cur_lan, "label_core_report_114"));
             labelsList.add(LabelContent.get(prof.cur_lan, "label_core_report_115"));             
             labelsList.add(LabelContent.get(prof.cur_lan, "label_core_report_164"));             
             String[] labels = new String[labelsList.size()] ;
             labels = labelsList.toArray(labels);
		   /* String[] labels = new String[] {
		   			 "编号","名称","类型","培训中心","报名人次","已完成","%","未完成","%","已放弃","%","进行中","%",
		   			 "等待审批","%","已拒绝","%","等待队列","%","总学习时长","平均学习时长"
				};*/
		    getCell(sheet, rowIndex+2, wb ,prof.cur_lan,labels); 
		    
		    List<LearnerCourseStatisticalReportVo> learnerCourseStatisticalReportVoList = (List<LearnerCourseStatisticalReportVo>) data.get("reportList");
		    //生成报表信息内容
		    for (int i = 0; i < learnerCourseStatisticalReportVoList.size(); i++)  
		    {  
		    	LearnerCourseStatisticalReportVo learnerCourseStatisticalReportVo = learnerCourseStatisticalReportVoList.get(i);
		    	HSSFRow row = sheet.createRow(i+rowIndex+3);  
		        //创建单元格，并设置值  
		        createAndSetCellByCourse(row,learnerCourseStatisticalReportVo,prof.cur_lan,vo);
		    } 
        }else{
    	 //统计数据以学员为主  信息内容
        	 //生成统计信息条件  返回当前行数   
            rowIndex = setReportHeardByUser(sheet,style,data,rowIndex,prof.cur_lan,vo);
		    //生成统计信息列表 头部
            List<String> labelsList = new ArrayList<String>();
            labelsList.add(LabelContent.get(prof.cur_lan, "label_core_report_78"));
            labelsList.add(LabelContent.get(prof.cur_lan, "label_core_report_79"));
            labelsList.add(LabelContent.get(prof.cur_lan, "label_core_report_80"));
            labelsList.add(LabelContent.get(prof.cur_lan, "label_core_report_168"));
            labelsList.add(LabelContent.get(prof.cur_lan, "label_core_report_110"));
            
            if(null!=vo.getCourseStatus() && vo.getCourseStatus().contains(CourseEvaluation.Completed)){
           	 labelsList.add(LabelContent.get(prof.cur_lan, "label_core_report_66"));
           	 labelsList.add("(%)");
            }
            if(null!=vo.getCourseStatus() && vo.getCourseStatus().contains(CourseEvaluation.FAIL)){
           	 labelsList.add(LabelContent.get(prof.cur_lan, "label_core_report_67"));
           	 labelsList.add("(%)");
            }
            if(null!=vo.getCourseStatus() && vo.getCourseStatus().contains(CourseEvaluation.Withdrawn)){
           	 labelsList.add(LabelContent.get(prof.cur_lan, "label_core_report_68"));
           	 labelsList.add("(%)");
            }
            if(null!=vo.getCourseStatus() && vo.getCourseStatus().contains(CourseEvaluation.InProgress)){
           	 labelsList.add(LabelContent.get(prof.cur_lan, "label_core_report_65"));
           	 labelsList.add("(%)");
            }
            if(null!=vo.getAppStatus() && vo.getAppStatus().contains(AeApplication.APP_STATUS_PENDING)){
           	 labelsList.add(LabelContent.get(prof.cur_lan, "label_core_report_112"));
           	 labelsList.add("(%)");
            }
            if(null!=vo.getAppStatus() && vo.getAppStatus().contains(AeApplication.APP_STATUS_REJECTED)){
           	 labelsList.add(LabelContent.get(prof.cur_lan, "label_core_report_64"));
           	 labelsList.add("(%)");
            }
            if(null!=vo.getAppStatus() && vo.getAppStatus().contains(AeApplication.APP_STATUS_WAITING)){
           	 labelsList.add(LabelContent.get(prof.cur_lan, "label_core_report_63"));
           	 labelsList.add("(%)");
            }
            labelsList.add(LabelContent.get(prof.cur_lan, "label_core_report_114"));
            labelsList.add(LabelContent.get(prof.cur_lan, "label_core_report_115"));
            labelsList.add(LabelContent.get(prof.cur_lan, "label_core_report_116"));
            labelsList.add(LabelContent.get(prof.cur_lan, "label_core_report_117"));
            labelsList.add(LabelContent.get(prof.cur_lan, "label_core_report_118"));
            labelsList.add(LabelContent.get(prof.cur_lan, "label_core_report_119"));
            labelsList.add(LabelContent.get(prof.cur_lan, "label_core_report_165"));
            String[] labels = new String[labelsList.size()] ;
            labels = labelsList.toArray(labels);
		    /*String[] labels = new String[] {
		   			 "用户名","全名","用户组","报名次数","已完成","%","不合格","%","已放弃","%","进行中","%","等待审批","%","已拒绝","%","等待队列","%","总学习时长"
		   			,"平均学习时长","平均出席率（%）","最高分","最低分","平均分"
				};*/
		    getCell(sheet, rowIndex+2, wb ,prof.cur_lan,labels); 
		    
		    List<LearnerUserStatisticalReportVo> userStatiscticalReportList = (List<LearnerUserStatisticalReportVo>) data.get("reportList");
		    //生成报表信息内容
		    for (int i = 0; i < userStatiscticalReportList.size(); i++)  
		    {  
		    	LearnerUserStatisticalReportVo learnerUserStatisticalReportVo = userStatiscticalReportList.get(i);
		    	HSSFRow row = sheet.createRow(i+rowIndex+3);  
		        //创建单元格，并设置值  
		        createAndSetCellByUser(row,learnerUserStatisticalReportVo,vo);
		    } 
       }
        //是否导出明细
        if(vo.getIsExportDetail()){
        	//根据选择显示的列导出明细内容
        	 HSSFSheet detailSheet = wb.createSheet(LabelContent.get(prof.cur_lan, "label_core_report_74"));  //"明细记录"
        	 setExportDetail(detailSheet,wb,prof.cur_lan,vo,data);
        }
        
        
       //生成文件名   并 将文件存到指定位置  
        String fileName = "";
         if(vo.getResultDataStatistic() == 0){
        	 fileName = "learner_activity_report_by_course_"+new SimpleDateFormat("yyyyMMddHHmmssSSS") .format(new Date())+".xls";
         }else{
    	   fileName = "learner_activity_report_by_learner_"+new SimpleDateFormat("yyyyMMddHHmmssSSS") .format(new Date())+".xls";
         }
        try  
        {  
        	String basePath = wizbini.getFileUploadTmpDirAbs() + File.separator;
        	File distPath = new File(basePath);
			if (!distPath.exists()) {
				distPath.mkdirs();
			}
            FileOutputStream fout = new FileOutputStream(basePath+fileName);  
            wb.write(fout);  
            fout.close();  
        }  
        catch (Exception e)  
        {  
            e.printStackTrace();  
        }
		return fileName;
	}
	

	//对于饼图数据进行显示过滤
	private List<PieVo> filterCoursePieData(LearnerReportParamVo param,List<PieVo> pieVoList  ){
		if(null==pieVoList || pieVoList.size()<=0){
			return null;
		}
		List<PieVo> pieList = new ArrayList<PieVo>();
		for(PieVo vo : pieVoList ){
			
			Integer key = Integer.valueOf(vo.getKey());
			if(null!=param.getCourseStatus()){
				// 0:网上课程  1:面授课程  2：网上考试  3:离线考试 4:项目式培训
				for(int i = 0 ; i < param.getCourseType().size() ; i++){
					Integer type = param.getCourseType().get(i);
					if(type == 0 && key==0){
						PieVo pvo = new PieVo();
						pvo.setLabel(LabelContent.get(param.getLanguage(), "label_core_report_53"));
						pvo.setValue(vo.getValue());
						pvo.setPercentage(vo.getPercentage());
						pvo.setStr_value(dbAiccPath.getTime(vo.getValue().floatValue()));
						pieList.add(pvo);
					}
					if(type == 1 && key==1){
						PieVo pvo = new PieVo();
						pvo.setLabel(LabelContent.get(param.getLanguage(), "label_core_report_54"));
						pvo.setValue(vo.getValue());
						pvo.setPercentage(vo.getPercentage());
						pvo.setStr_value(dbAiccPath.getTime(vo.getValue().floatValue()));
						pieList.add(pvo);
					}
					if(type == 2 && key==2){
						PieVo pvo = new PieVo();
						pvo.setLabel(LabelContent.get(param.getLanguage(), "label_core_report_55"));
						pvo.setValue(vo.getValue());
						pvo.setPercentage(vo.getPercentage());
						pvo.setStr_value(dbAiccPath.getTime(vo.getValue().floatValue()));
						pieList.add(pvo);
					}
					if(type == 3 && key==3){
						PieVo pvo = new PieVo();
						pvo.setLabel(LabelContent.get(param.getLanguage(), "label_core_report_56"));
						pvo.setValue(vo.getValue());
						pvo.setPercentage(vo.getPercentage());
						pvo.setStr_value(dbAiccPath.getTime(vo.getValue().floatValue()));
						pieList.add(pvo);
					}
					if(type == 4 && key==4){
						PieVo pvo = new PieVo();
						pvo.setLabel(LabelContent.get(param.getLanguage(), "label_core_report_57"));
						pvo.setValue(vo.getValue());
						pvo.setPercentage(vo.getPercentage());
						pvo.setStr_value(dbAiccPath.getTime(vo.getValue().floatValue()));
						pieList.add(pvo);
					}
				}
			}
		}
		return pieList;
	}
	
	//是否网上课程
	private boolean isOnlineCourse(LearnerDetailReportVo vo){
		return vo.getItmRunInd() == 0 && vo.getItmApplyInd() == 1
				&& vo.getItmIntegratedInd() == 0 && vo.getItmExamInd() == 0;
	}
	
	//是否面授课程
	private boolean isOfflineCourse(LearnerDetailReportVo vo){
		return (vo.getItmRunInd() == 0 && vo.getItmCreateRunInd() == 1 && vo.getItmIntegratedInd() == 0 && vo.getItmExamInd() == 0)
				|| (vo.getItmRunInd() == 1 && vo.getItmCreateRunInd() == 0 && vo.getItmIntegratedInd() == 0 && vo.getItmExamInd() == 0);
	}
	
	//是否网上考试
	private boolean isOnlineExam(LearnerDetailReportVo vo){
		return vo.getItmRunInd() == 0 && vo.getItmApplyInd() == 1
				&& vo.getItmIntegratedInd() == 0 && vo.getItmExamInd() == 1;
	}
	
	//是否离线考试 
	private boolean isOfflineExam(LearnerDetailReportVo vo){
		return (vo.getItmRunInd() == 0 && vo.getItmCreateRunInd() == 1 && vo.getItmIntegratedInd() == 0 && vo.getItmExamInd() == 1)
				|| (vo.getItmRunInd() == 1 && vo.getItmCreateRunInd() == 0 && vo.getItmIntegratedInd() == 0 && vo.getItmExamInd() == 1);
	}
	
	//是否项目式培训 
	private boolean isIntegrated(LearnerDetailReportVo vo){
		return vo.getItmIntegratedInd() == 1;
	}

	//报表页面选择条件  相同部分
	private Integer setConditionToheard(HSSFSheet sheet,HSSFCellStyle style,HSSFCellStyle titleStyle,LearnerReportParamVo vo,String lan){
		int rowIndex = 0;
		HSSFRow row = sheet.createRow(rowIndex++);
		row.setHeightInPoints(20);
		HSSFCell cell = row.createCell(0);  
        cell.setCellValue(LabelContent.get(lan, "label_core_report_44"));   //"学员学习进展报告"
        cell.setCellStyle(titleStyle); 
		rowIndex++;
		row = sheet.createRow(rowIndex++); 
		HSSFCell learnersCell = row.createCell(0);  
		learnersCell.setCellValue(LabelContent.get(lan, "label_core_report_45") +"：");   //学员
		learnersCell.setCellStyle(style); 
		if(vo.getExportUser() == 0){
			if(vo.isIncludeDelUser()){
			  row.createCell(1).setCellValue(LabelContent.get(lan, "label_core_report_46")+"("+LabelContent.get(lan, "label_core_report_47")+")" );	 //"所有学员（包含已删除学员）"
			}else{
			  row.createCell(1).setCellValue(LabelContent.get(lan, "label_core_report_46"));	  //"所有学员"
			}  
		}else if(vo.getExportUser() == 1){
				if(null !=vo.getExportUserNames() && vo.getExportUserNames().size() > 0){
				    for(int i=0;i<vo.getExportUserNames().size();i++){
				    	  row.createCell(1).setCellValue(vo.getExportUserNames().get(i).toString());
						  row = sheet.createRow(rowIndex++); 
				  }
				}
		}else{
			if(null !=vo.getExportGroupNames() && vo.getExportGroupNames().size() > 0){
			    for(int i=0;i<vo.getExportGroupNames().size();i++){
			    	  row.createCell(1).setCellValue(vo.getExportGroupNames().get(i).toString());
					  row = sheet.createRow(rowIndex++); 
			  }
			}
		}
		row = sheet.createRow(rowIndex++); 
		HSSFCell courseCell = row.createCell(0);  
		courseCell.setCellValue(LabelContent.get(lan, "label_core_report_107")+"：");  //课程
		courseCell.setCellStyle(style); 
		if(vo.getExportCourse() == 0){
			  row.createCell(1).setCellValue(LabelContent.get(lan, "label_core_report_50"));	 //所有课程
		}else if(vo.getExportCourse() == 1){
			if(null !=vo.getExportCourseNames() && vo.getExportCourseNames().size() > 0){
			    for(int i=0;i<vo.getExportCourseNames().size();i++){
			    	  row.createCell(1).setCellValue(vo.getExportCourseNames().get(i).toString());
					  row = sheet.createRow(rowIndex++); 
			  }
			}
		}else{
			if(null !=vo.getExportCatalogNames() && vo.getExportCatalogNames().size() > 0){
			    for(int i=0;i<vo.getExportCatalogNames().size();i++){
			    	  row.createCell(1).setCellValue(vo.getExportCatalogNames().get(i).toString());
					  row = sheet.createRow(rowIndex++); 
			  }
			}
		}

		StringBuffer sb = null;
		if(!StringUtils.isEmpty(vo.getAppnStartDisplayDatetime()) 
				|| !StringUtils.isEmpty(vo.getAppnEndDisplayDatetime())){
			sb = new StringBuffer();
			row = sheet.createRow(rowIndex++); 
			HSSFCell statusCell = row.createCell(0);  
			statusCell.setCellValue(LabelContent.get(lan, "label_rp.label_core_report_58")+"：");   //
			statusCell.setCellStyle(style); 
			if(!StringUtils.isEmpty(vo.getAppnStartDisplayDatetime())){
				sb.append(vo.getAppnStartDisplayDatetime());
			}else{
				sb.append("--");
			}
			sb.append(" " + LabelContent.get(lan, "label_rp.label_core_report_59") + " ");
			if(!StringUtils.isEmpty(vo.getAppnEndDisplayDatetime())){
				sb.append(vo.getAppnEndDisplayDatetime());
			}else{
				sb.append("--");
			}
			row.createCell(1).setCellValue(sb.toString());
		}
		
		if(!StringUtils.isEmpty(vo.getAttStartDispalyTime()) 
				|| !StringUtils.isEmpty(vo.getAttEndDisplayTime())){
			sb = new StringBuffer();
			row = sheet.createRow(rowIndex++); 
			HSSFCell statusCell = row.createCell(0);  
			statusCell.setCellValue(LabelContent.get(lan, "label_rp.label_core_report_60")+"：");   //
			statusCell.setCellStyle(style); 
			if(!StringUtils.isEmpty(vo.getAttStartDispalyTime())){
				sb.append(vo.getAttStartDispalyTime());
			}else{
				sb.append("--");
			}
			sb.append(" " + LabelContent.get(lan, "label_rp.label_core_report_59") + " ");
			if(!StringUtils.isEmpty(vo.getAttEndDisplayTime())){
				sb.append(vo.getAttEndDisplayTime());
			}else{
				sb.append("--");
			}
			row.createCell(1).setCellValue(sb.toString());
		}
		
		sb = new StringBuffer();
		if(null!=vo.getCourseType()){
			for(int i = 0 ; i<vo.getCourseType().size() ; i++ ){
				Integer status = vo.getCourseType().get(i);
				if(status==0){
					sb.append(LabelContent.get(lan, "label_rp.label_core_report_53"));
				}else if(status==1){
					sb.append(LabelContent.get(lan, "label_rp.label_core_report_54"));
				}else if(status==2){
					sb.append(LabelContent.get(lan, "label_rp.label_core_report_55"));
				}else if(status==3){
					sb.append(LabelContent.get(lan, "label_rp.label_core_report_56"));
				}else if(status==4){
					sb.append(LabelContent.get(lan, "label_rp.label_core_report_57"));
				}
				if(i<vo.getCourseType().size()-1){
					sb.append(",");
				}
			}
		}
		
		if(!StringUtils.isEmpty(sb.toString())){
			row = sheet.createRow(rowIndex++); 
			HSSFCell statusCell = row.createCell(0);  
			statusCell.setCellValue(LabelContent.get(lan, "label_rp.label_core_report_103")+"：");   //考试类型
			statusCell.setCellStyle(style); 
			row.createCell(1).setCellValue(sb.toString());
		}
		
		sb = new StringBuffer();
		if(null!=vo.getCourseStatus()){
			for(int i = 0 ; i<vo.getCourseStatus().size() ; i++ ){
				String status = (String)vo.getCourseStatus().get(i);
				if(status.equalsIgnoreCase(CourseEvaluation.InProgress)){
					sb.append(LabelContent.get(lan, "label_rp.label_core_report_65"));
				}else if(status.equalsIgnoreCase(CourseEvaluation.Completed)){
					sb.append(LabelContent.get(lan, "label_rp.label_core_report_66"));
				}else if(status.equalsIgnoreCase(CourseEvaluation.FAIL)){
					sb.append(LabelContent.get(lan, "label_rp.label_core_report_67"));
				}else if(status.equalsIgnoreCase(CourseEvaluation.Withdrawn)){
					sb.append(LabelContent.get(lan, "label_rp.label_core_report_68"));
				}
				if(i<vo.getCourseStatus().size()-1 || 
						(i==vo.getCourseStatus().size()-1 && null!=vo.getAppStatus() && vo.getAppStatus().size()>0)){
					sb.append(",");
				}
			}
		}
	
		if(null!=vo.getAppStatus()){
			for(int i = 0 ; i<vo.getAppStatus().size() ; i++ ){
				String status = (String)vo.getAppStatus().get(i);
				if(status.equalsIgnoreCase(AeApplication.APP_STATUS_PENDING)){
					sb.append(LabelContent.get(lan, "label_rp.label_core_report_62"));
				}else if(status.equalsIgnoreCase(AeApplication.APP_STATUS_WAITING)){
					sb.append(LabelContent.get(lan, "label_rp.label_core_report_63"));
				}else if(status.equalsIgnoreCase(AeApplication.APP_STATUS_REJECTED)){
					sb.append(LabelContent.get(lan, "label_rp.label_core_report_64"));
				}
				if(i<vo.getAppStatus().size()-1){
					sb.append(",");
				}
			}
		}
		if(!StringUtils.isEmpty(sb.toString())){
			row = sheet.createRow(rowIndex++); 
			HSSFCell statusCell = row.createCell(0);  
			statusCell.setCellValue(LabelContent.get(lan, "label_rp.label_core_report_61")+"：");   //报名状态
			statusCell.setCellStyle(style); 
			row.createCell(1).setCellValue(sb.toString());
		}

		row = sheet.createRow(rowIndex++); 
		HSSFCell resultCell = row.createCell(0);  
		resultCell.setCellValue(LabelContent.get(lan, "label_core_report_69")+"：");   //统计结果方式
		resultCell.setCellStyle(style); 
		rowIndex = rowIndex+2;
		sb = new StringBuffer();
		if(vo.getResultDataStatistic() == 0){
			sb.append(LabelContent.get(lan, "label_core_report_70"));
			if(null!=vo.getIncludeNoDataCourse()&&1==vo.getIncludeNoDataCourse()){
				sb.append(LabelContent.get(lan, "label_core_report_71"));
			}
			row.createCell(1).setCellValue(sb.toString());	//"统计数据以课程为主（注：如果一个人在同一个课程中有多条学习记录，会当成多个人次）"
		}else{
			sb.append(LabelContent.get(lan, "label_core_report_72"));
			if(null!=vo.getIncludeNoDataUser()&&1==vo.getIncludeNoDataUser()){
				sb.append(LabelContent.get(lan, "label_core_report_73"));
			}
			row.createCell(1).setCellValue(sb.toString());	//"统计数据以学员为主（注：如果一个人在同一个课程中有多条学习记录，会当成多个人次）"
		}
		return rowIndex;
	}
	
	//写入报表统计信息    页面统计内容 --数据以学员为主
	private Integer setReportHeardByUser(HSSFSheet sheet,HSSFCellStyle style,Map data,int rowIndex,String lan ,LearnerReportParamVo param){
	    style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);//右对齐

	    HSSFRow row = sheet.createRow(rowIndex++); 
		HSSFCell cellE = row.createCell(0); 
		cellE.setCellValue(LabelContent.get(lan, "label_core_report_122"));  //学习记录统计
		cellE.setCellStyle(style); 
		row = sheet.createRow(rowIndex); 
		HSSFCell cellF = row.createCell(0);  
		cellF.setCellValue(LabelContent.get(lan, "label_core_report_123")+"：");   //"学习人数："
		cellF.setCellStyle(style); 
		row.createCell(1).setCellValue(isNullOrZero(data.get("totleUserCount"),"",true));

		row = sheet.createRow(rowIndex+1); 
		HSSFCell cellH = row.createCell(0);  
		cellH.setCellValue(LabelContent.get(lan, "label_core_report_110")+"：");  //报名人次：
		cellH.setCellStyle(style); 
		row.createCell(1).setCellValue(isNullOrZero(data.get("enrollUserCount"),"",true));   
		
		row = sheet.createRow(rowIndex+2); 
		HSSFCell cellJ = row.createCell(0);  
		cellJ.setCellValue(LabelContent.get(lan, "label_core_report_124")+"：");  //人均报名次数：
		cellJ.setCellStyle(style); 
		row.createCell(1).setCellValue(isNullOrZero(data.get("avgEnrollUserCount"),"",true));
		
		row = sheet.createRow(rowIndex+3); 
		HSSFCell cellL = row.createCell(0);  
		cellL.setCellValue(LabelContent.get(lan, "label_core_report_125")+"：");  //在线学习总时长：
		cellL.setCellStyle(style); 
		row.createCell(1).setCellValue(isNullOrZero(data.get("totleCovTotleTime"),"",false));
		
		row = sheet.createRow(rowIndex+4); 
		HSSFCell cellN = row.createCell(0);  
		cellN.setCellValue(LabelContent.get(lan, "label_core_report_126")+"：");  //人均在线学习时长：
		cellN.setCellStyle(style); 
		row.createCell(1).setCellValue(isNullOrZero(data.get("avgCovTotleTime"),"",false));
		
		row = sheet.createRow(rowIndex+5); 
		row.createCell(0).setCellValue("");
		row.createCell(1).setCellValue("");
		row.createCell(2).setCellValue("");
		
		row = sheet.createRow(rowIndex+6); 
		row.createCell(0).setCellValue("");
		row.createCell(1).setCellValue("");
		row.createCell(2).setCellValue("");
		
		int leftRowNum = rowIndex+6;
		if(containStatus(param.getCourseStatus(),CourseEvaluation.InProgress)){
			row = sheet.getRow(rowIndex++); 
			if(null==row){
				row = sheet.createRow(rowIndex++); 
			}
			HSSFCell cellM = row.createCell(3);  
			cellM.setCellValue(LabelContent.get(lan, "label_core_report_65")+"：");  //进行中：
			cellM.setCellStyle(style); 
			row.createCell(4).setCellValue(isNullOrZero(data.get("inProgressCount"),"",true));
			row.createCell(5).setCellValue(isNullOrZero(data.get("inProgressPercentage"),"%",false));
		}
		
		if(containStatus(param.getCourseStatus(),CourseEvaluation.Completed)){
			row = sheet.getRow(rowIndex++); 
			if(null==row){
				row = sheet.createRow(rowIndex++); 
			}
			HSSFCell cellO = row.createCell(3);  
			cellO.setCellValue(LabelContent.get(lan, "label_core_report_66")+"：");  //已完成：
			cellO.setCellStyle(style); 
			row.createCell(4).setCellValue(isNullOrZero(data.get("completedCount"),"",true));
			row.createCell(5).setCellValue(isNullOrZero(data.get("completedPercentage"),"%",false));
		}
		
		if(containStatus(param.getCourseStatus(),CourseEvaluation.FAIL)){
			row = sheet.getRow(rowIndex++); 
			if(null==row){
				row = sheet.createRow(rowIndex++); 
			}
			HSSFCell cellP = row.createCell(3);  
			cellP.setCellValue(LabelContent.get(lan, "label_core_report_67")+"：");  //不合格：
			cellP.setCellStyle(style); 
			row.createCell(4).setCellValue(isNullOrZero(data.get("failCount"),"",true));
			row.createCell(5).setCellValue(isNullOrZero(data.get("failPercentage"),"%",false));
		}
		
		if(containStatus(param.getCourseStatus(),CourseEvaluation.Withdrawn)){
			row = sheet.getRow(rowIndex++); 
			if(null==row){
				row = sheet.createRow(rowIndex++); 
			} 
			HSSFCell cellQ = row.createCell(3);  
			cellQ.setCellValue(LabelContent.get(lan, "label_core_report_68")+"：");  //已放弃：
			cellQ.setCellStyle(style); 
			row.createCell(4).setCellValue(isNullOrZero(data.get("withdrawnCount"),"",true));
			row.createCell(5).setCellValue(isNullOrZero(data.get("withdrawnPercentage"),"%",false));
		}
		

		
		if(containStatus(param.getAppStatus(),AeApplication.APP_STATUS_PENDING)){
			row = sheet.getRow(rowIndex++); 
			if(null==row){
				row = sheet.createRow(rowIndex++); 
			}
			HSSFCell cellG = row.createCell(3);  
			cellG.setCellValue(LabelContent.get(lan, "label_core_report_62")+"：");  //等待审批：
			cellG.setCellStyle(style); 
			row.createCell(4).setCellValue(isNullOrZero(data.get("penddingCount"),"",true));
			row.createCell(5).setCellValue(isNullOrZero(data.get("penddingPercentage"),"%",false));
		}
		
		if(containStatus(param.getAppStatus(),AeApplication.APP_STATUS_WAITING)){
			row = sheet.getRow(rowIndex++); 
			if(null==row){
				row = sheet.createRow(rowIndex++); 
			}
			HSSFCell cellI = row.createCell(3);  
			cellI.setCellValue(LabelContent.get(lan, "label_core_report_113")+"：");  //等待队列：
			cellI.setCellStyle(style); 
			row.createCell(4).setCellValue(isNullOrZero(data.get("waitingCount"),"",true));
			row.createCell(5).setCellValue(isNullOrZero(data.get("waitingPercentage"),"%",false));
		}
		
		if(containStatus(param.getAppStatus(),AeApplication.APP_STATUS_REJECTED)){
			row = sheet.getRow(rowIndex++); 
			if(null==row){
				row = sheet.createRow(rowIndex++); 
			}
			HSSFCell cellK = row.createCell(3);  
			cellK.setCellValue(LabelContent.get(lan, "label_core_report_64")+"：");  //已拒绝：
			cellK.setCellStyle(style); 
			row.createCell(4).setCellValue(isNullOrZero(data.get("rejectedCount"),"",true));
			row.createCell(5).setCellValue(isNullOrZero(data.get("rejectedPercentage"),"%",false));
		}
		rowIndex = leftRowNum>rowIndex?leftRowNum:rowIndex;
		return rowIndex;
	}
	
	//写入报表头部
	private void getCell(HSSFSheet sheet,int rowIndex,HSSFWorkbook wb ,String labellang,String[] labels){
		HSSFRow row = sheet.createRow(rowIndex);  
	    HSSFCellStyle style = wb.createCellStyle();  
	        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
	        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	        style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
	        sheet.createRow(rowIndex).setHeight((short) 400);
	        //边框颜色 黑色
	        style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框    
	        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框    
	        style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框    
	        style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框 
   	 for(int i = 0; i<labels.length; i++){
   			 HSSFCell cell = row.createCell(i);  
		         cell.setCellValue(labels[i]);  
		         cell.setCellStyle(style);  
   	 }
	}
	
	//写入报表内容  --数据以学员为主
	private void createAndSetCellByUser(HSSFRow row,LearnerUserStatisticalReportVo vo,LearnerReportParamVo param){
		int index = 0;
		row.createCell(index++).setCellValue(vo.getUserDispalyName());  
        row.createCell(index++).setCellValue(vo.getUserFullName()); 
        row.createCell(index++).setCellValue(vo.getGourpName());
        row.createCell(index++).setCellValue(vo.getStaffNo());
        row.createCell(index++).setCellValue(vo.getEnrollCount());
        
        if(null!=param.getCourseStatus() && param.getCourseStatus().contains(CourseEvaluation.Completed)){
            row.createCell(index++).setCellValue(isNullReturn(vo.getCompletedCount())); 
            row.createCell(index++).setCellValue(isNullReturn(vo.getCompletedPercentage())); 
        }
        if(null!=param.getCourseStatus() && param.getCourseStatus().contains(CourseEvaluation.FAIL)){
            row.createCell(index++).setCellValue(isNullReturn(vo.getFailCount()));
            row.createCell(index++).setCellValue(isNullReturn(vo.getFailPercentage())); 
        }
        if(null!=param.getCourseStatus() && param.getCourseStatus().contains(CourseEvaluation.Withdrawn)){
            row.createCell(index++).setCellValue(isNullReturn(vo.getWithdrawnCount()));
            row.createCell(index++).setCellValue(isNullReturn(vo.getWithdrawnPercentage()));
        }
        if(null!=param.getCourseStatus() && param.getCourseStatus().contains(CourseEvaluation.InProgress)){
            row.createCell(index++).setCellValue(isNullReturn(vo.getInProgressCount()));  
            row.createCell(index++).setCellValue(isNullReturn(vo.getInProgressPercentage())); 
        }
        if(null!=param.getAppStatus() && param.getAppStatus().contains(AeApplication.APP_STATUS_PENDING)){
            row.createCell(index++).setCellValue(isNullReturn(vo.getPenddingCount()));
            row.createCell(index++).setCellValue(isNullReturn(vo.getPenddingPercentage()));
        }
        if(null!=param.getAppStatus() && param.getAppStatus().contains(AeApplication.APP_STATUS_REJECTED)){
            row.createCell(index++).setCellValue(isNullReturn(vo.getRejectedCount()));
            row.createCell(index++).setCellValue(isNullReturn(vo.getRejectedPercentage())); 
        }
        if(null!=param.getAppStatus() && param.getAppStatus().contains(AeApplication.APP_STATUS_WAITING)){
            row.createCell(index++).setCellValue(isNullReturn(vo.getWaitingCount())); 
            row.createCell(index++).setCellValue(isNullReturn(vo.getWaitingPercentage()));
        }
        row.createCell(index++).setCellValue(isNullOrZero(vo.getTotleCovTotleDisplayTime(),"",false));
        row.createCell(index++).setCellValue(isNullOrZero(vo.getAvgCovTotleDisplayTime(),"",false));
        row.createCell(index++).setCellValue(isNullOrZero(vo.getAvgAttRatePercentage(),"",false));
        row.createCell(index++).setCellValue(isNullOrZero(vo.getMaxScore(),"",false));
        row.createCell(index++).setCellValue(isNullOrZero(vo.getMinScore(),"",false));
        row.createCell(index++).setCellValue(isNullOrZero(vo.getAvgScore(),"",false));
        /*String totalIesCredit="0.0";
        if(vo.getHasIesCredit()){
                totalIesCredit=vo.getTotalIesCredit().toString();
        }else{
            totalIesCredit = isNullOrZero(vo.getTotalIesCredit() == null ? null : vo.getTotalIesCredit().toString(),"",false);
        }*/
        row.createCell(index++).setCellValue(isNullOrZero(vo.getTotalIesCredit(),"",false));
	}
	
	//写入报表内容 --数据以课程为主
	private void createAndSetCellByCourse(HSSFRow row,LearnerCourseStatisticalReportVo vo,String lan ,LearnerReportParamVo param){
		int index = 0;
		row.createCell(index++).setCellValue(vo.getItmCode());  
        row.createCell(index++).setCellValue(vo.getItmTitle()); 
        if(vo.getCourseType() == LearnerDetailReportVo.COURSE_TYPE_WEB_BASED){
        	row.createCell(index++).setCellValue(LabelContent.get(lan, "label_core_report_53"));
        }else if(vo.getCourseType() == LearnerDetailReportVo.COURSE_TYPE_CLASSROOM){
        	row.createCell(index++).setCellValue(LabelContent.get(lan, "label_core_report_54"));
        }else if(vo.getCourseType() == LearnerDetailReportVo.EXAM_TYPE_WEB_BASED){
        	row.createCell(index++).setCellValue(LabelContent.get(lan, "label_core_report_55"));
        }else if(vo.getCourseType() == LearnerDetailReportVo.EXAM_TYPE_CLASSROOM){
        	row.createCell(index++).setCellValue(LabelContent.get(lan, "label_core_report_56"));
        }else if(vo.getCourseType() == LearnerDetailReportVo.COURSE_TYPE_INTEGRATED){
        	row.createCell(index++).setCellValue(LabelContent.get(lan, "label_core_report_57"));
        }
        row.createCell(index++).setCellValue(vo.getTcName());
        row.createCell(index++).setCellValue(vo.getEnrollCount());
        
        if(null!=param.getCourseStatus() && param.getCourseStatus().contains(CourseEvaluation.Completed)){
            row.createCell(index++).setCellValue(isNullReturn(vo.getCompletedCount())); 
            row.createCell(index++).setCellValue(isNullReturn(vo.getCompletedPercentage())); 
        }
        if(null!=param.getCourseStatus() && param.getCourseStatus().contains(CourseEvaluation.FAIL)){
            row.createCell(index++).setCellValue(isNullReturn(vo.getFailCount()));
            row.createCell(index++).setCellValue(isNullReturn(vo.getFailPercentage())); 
        }
        if(null!=param.getCourseStatus() && param.getCourseStatus().contains(CourseEvaluation.Withdrawn)){
            row.createCell(index++).setCellValue(isNullReturn(vo.getWithdrawnCount()));
            row.createCell(index++).setCellValue(isNullReturn(vo.getWithdrawnPercentage()));
        }
        if(null!=param.getCourseStatus() && param.getCourseStatus().contains(CourseEvaluation.InProgress)){
            row.createCell(index++).setCellValue(isNullReturn(vo.getInProgressCount()));  
            row.createCell(index++).setCellValue(isNullReturn(vo.getInProgressPercentage())); 
        }
        if(null!=param.getAppStatus() && param.getAppStatus().contains(AeApplication.APP_STATUS_PENDING)){
            row.createCell(index++).setCellValue(isNullReturn(vo.getPenddingCount()));
            row.createCell(index++).setCellValue(isNullReturn(vo.getPenddingPercentage()));
        }
        if(null!=param.getAppStatus() && param.getAppStatus().contains(AeApplication.APP_STATUS_REJECTED)){
            row.createCell(index++).setCellValue(isNullReturn(vo.getRejectedCount()));
            row.createCell(index++).setCellValue(isNullReturn(vo.getRejectedPercentage())); 
        }
        if(null!=param.getAppStatus() && param.getAppStatus().contains(AeApplication.APP_STATUS_WAITING)){
            row.createCell(index++).setCellValue(isNullReturn(vo.getWaitingCount())); 
            row.createCell(index++).setCellValue(isNullReturn(vo.getWaitingPercentage()));
        }
        row.createCell(index++).setCellValue(isNullOrZero(vo.getTotleCovTotleDisplayTime(),"",false));
        row.createCell(index++).setCellValue(isNullOrZero(vo.getAvgCovTotleDisplayTime(),"",false));
        row.createCell(index++).setCellValue(isNullOrZero(vo.getIesCredit().toString(),"",false));
	}
	
	//写入报表明细记录内容 
	private void detailedSetCell(HSSFRow row,LearnerDetailReportVo vo,String[] labelsList,String lan){
		int cellIndex = 0;
		if(Arrays.asList(labelsList).contains("userDispalyName")){
			row.createCell(cellIndex++).setCellValue(vo.getUserDispalyName()); 
		}
		if(Arrays.asList(labelsList).contains("userFullName")){
			row.createCell(cellIndex++).setCellValue(vo.getUserFullName()); 
		}
		if(Arrays.asList(labelsList).contains("gruopName")){
			row.createCell(cellIndex++).setCellValue(vo.getGruopName()); 
		}
		if(Arrays.asList(labelsList).contains("gradeName")){
			row.createCell(cellIndex++).setCellValue(vo.getGradeName()); 
		}
		if(Arrays.asList(labelsList).contains("userEmail")){
			row.createCell(cellIndex++).setCellValue(vo.getUserEmail()); 
		}
		if(Arrays.asList(labelsList).contains("userTel")){
			row.createCell(cellIndex++).setCellValue(vo.getUserTel()); 
		}
		if(Arrays.asList(labelsList).contains("staffNo")){
			row.createCell(cellIndex++).setCellValue(vo.getStaffNo()); 
		}//员工编号
		if(Arrays.asList(labelsList).contains("itmCode")){
			if(isOnlineCourse(vo) || isOnlineExam(vo)){
				row.createCell(cellIndex++).setCellValue(vo.getItmCode()); 
			}else if(isOfflineCourse(vo) || isOfflineExam(vo)){
				row.createCell(cellIndex++).setCellValue(vo.getP_itm_code()); 
			}else if(isIntegrated(vo)){
				row.createCell(cellIndex++).setCellValue(vo.getItmCode()); 
			}
		}
		if(Arrays.asList(labelsList).contains("itmTitle")){
			if(isOnlineCourse(vo) || isOnlineExam(vo)){
				row.createCell(cellIndex++).setCellValue(vo.getItmTitle()); 
			}else if(isOfflineCourse(vo) || isOfflineExam(vo)){
				row.createCell(cellIndex++).setCellValue(vo.getP_itm_title()); 
			}else if(isIntegrated(vo)){
				row.createCell(cellIndex++).setCellValue(vo.getItmTitle()); 
			}
		}
		if(Arrays.asList(labelsList).contains("itmType") ){
		    if(isOnlineCourse(vo)){
				  row.createCell(cellIndex++).setCellValue(LabelContent.get(lan, "label_core_report_53"));
		        }
		    else if(isOfflineCourse(vo)){
		          row.createCell(cellIndex++).setCellValue(LabelContent.get(lan, "label_core_report_54"));
		        }
		    else if(isOnlineExam(vo)){
		          row.createCell(cellIndex++).setCellValue(LabelContent.get(lan, "label_core_report_55"));
		        }
		    else if(isOfflineExam(vo)){
		          row.createCell(cellIndex++).setCellValue(LabelContent.get(lan, "label_core_report_56"));
		        }
		    else if(isIntegrated(vo)){
		          row.createCell(cellIndex++).setCellValue(LabelContent.get(lan, "label_core_report_57"));
		        }
		}
		if(Arrays.asList(labelsList).contains("tcrTitle") ){
			row.createCell(cellIndex++).setCellValue(vo.getTcrTitle()); 
		}
		if(Arrays.asList(labelsList).contains("classCode") ){
			if(!StringUtils.isEmpty(vo.getItmCode())&& (isOfflineCourse(vo) || isOfflineExam(vo))){
				row.createCell(cellIndex++).setCellValue(vo.getItmCode());
			}else {
				row.createCell(cellIndex++).setCellValue("--");
			}
		}
		if(Arrays.asList(labelsList).contains("classTitle") ){
			if(!StringUtils.isEmpty(vo.getItmTitle())&& (isOfflineCourse(vo) || isOfflineExam(vo))){
					row.createCell(cellIndex++).setCellValue(vo.getItmTitle());
			}else {
				row.createCell(cellIndex++).setCellValue("--");
			}
		}
		
		if(Arrays.asList(labelsList).contains("courseCatalog") ){
			if(!StringUtils.isEmpty(vo.getCatalog())){
				row.createCell(cellIndex++).setCellValue(vo.getCatalog());
			}else {
				row.createCell(cellIndex++).setCellValue("--");
			}
		}
		
		if(Arrays.asList(labelsList).contains("appCreateTime") ){ //报名日期
			if(null != vo.getAppCreateTime() && vo.getAppCreateTime().toString() != ""){
				row.createCell(cellIndex++).setCellValue(DateUtil.getInstance().dateToString(vo.getAppCreateTime(), DateUtil.patternA));  
			}else{
				row.createCell(cellIndex++).setCellValue("--");
			}
		}
		if(Arrays.asList(labelsList).contains("appStatus") ){
			if(null != vo.getAppStatus()){
				if(vo.getAppStatus().equals(AeApplication.APP_STATUS_PENDING)){
					row.createCell(cellIndex++).setCellValue(LabelContent.get(lan, "label_core_report_62")); 
				}else if(vo.getAppStatus().equals(AeApplication.APP_STATUS_ADMITTED)){
					row.createCell(cellIndex++).setCellValue(LabelContent.get(lan, "label_core_report_147")); 
				}else if(vo.getAppStatus().equals(AeApplication.APP_STATUS_REJECTED)){
					row.createCell(cellIndex++).setCellValue(LabelContent.get(lan, "label_core_report_64")); 
				}else if(vo.getAppStatus().equals(AeApplication.APP_STATUS_WAITING)){
					row.createCell(cellIndex++).setCellValue(LabelContent.get(lan, "label_core_report_63")); 
				}else if(vo.getAppStatus().equals(AeApplication.APP_STATUS_WITHDRAWN)){
					row.createCell(cellIndex++).setCellValue(LabelContent.get(lan, "label_core_report_148")); 
				}
			}else{
				row.createCell(cellIndex++).setCellValue(""); 
			}
		}
		if(Arrays.asList(labelsList).contains("attTime") ){ //结训日期
			if(null != vo.getAttTime() && vo.getAttTime().toString() != ""){
				row.createCell(cellIndex++).setCellValue(DateUtil.getInstance().dateToString(vo.getAttTime(), DateUtil.patternA));
			}else{
				row.createCell(cellIndex++).setCellValue("--");
			}
		}
		if(Arrays.asList(labelsList).contains("covStatus") ){//学习状态
			if(null != vo.getCovStatus()){
				if(vo.getCovStatus().equals(LearnerReportParamVo.COURSE_STATUS_I)){
					row.createCell(cellIndex++).setCellValue(LabelContent.get(lan, "label_core_report_65")); 
				}else if(vo.getCovStatus().equals(LearnerReportParamVo.COURSE_STATUS_C)){
					row.createCell(cellIndex++).setCellValue(LabelContent.get(lan, "label_core_report_66")); 
				}else if(vo.getCovStatus().equals(LearnerReportParamVo.COURSE_STATUS_F)){
					row.createCell(cellIndex++).setCellValue(LabelContent.get(lan, "label_core_report_67")); 
				}else if(vo.getCovStatus().equals(LearnerReportParamVo.COURSE_STATUS_W)){
					row.createCell(cellIndex++).setCellValue(LabelContent.get(lan, "label_core_report_68")); 
				}
			}else{
				row.createCell(cellIndex++).setCellValue("--"); 
			}
		}
		if(Arrays.asList(labelsList).contains("covCommenceDatetime") ){
			if(null != vo.getCovCommenceDatetime() && vo.getCovCommenceDatetime().toString() != ""){
				row.createCell(cellIndex++).setCellValue(DateUtil.getInstance().dateToString(vo.getCovCommenceDatetime(), DateUtil.patternA)); 
			}else{
				row.createCell(cellIndex++).setCellValue("--");
			}
		}
		if(Arrays.asList(labelsList).contains("covTotalTime") ){
			if(null != vo.getCovTotalTime() && vo.getCovTotalTime().toString() != "" && vo.getCovTotalTime()!=0d){
				row.createCell(cellIndex++).setCellValue(dbAiccPath.getTime(vo.getCovTotalTime().floatValue()));  
			}else{
				row.createCell(cellIndex++).setCellValue("--");
			}
		}
		if(Arrays.asList(labelsList).contains("covScore") ){
			if(null != vo.getCovScore() && vo.getCovScore() != 0){
				row.createCell(cellIndex++).setCellValue(CwnUtil.formatNumber(vo.getCovScore(), 2, BigDecimal.ROUND_DOWN));  
			}else{
				if(vo.getCovScore()==null){
					row.createCell(cellIndex++).setCellValue(0);
				}else{
					row.createCell(cellIndex++).setCellValue("--");
				}
			}
		}
		if(Arrays.asList(labelsList).contains("attRate") ){
			if(null != vo.getAttRate() && vo.getAttRate() != 0.0){
				row.createCell(cellIndex++).setCellValue(vo.getAttRate()+"%"); 
			}else{
				row.createCell(cellIndex++).setCellValue("--"); 
			}
		}
		if(Arrays.asList(labelsList).contains("attemptCount") ){
			if(null != vo.getAttemptCount() && vo.getAttemptCount() != 0){
				row.createCell(cellIndex++).setCellValue(vo.getAttemptCount()); 
			}else{
				row.createCell(cellIndex++).setCellValue("--"); 
			}
		}
        if(Arrays.asList(labelsList).contains("courseCredit") ){//学分
              if(null != vo.getIesCredit() && vo.getIesCredit() != 0){//如果已完成课程并且该课程存在学分
                  if(CourseEvaluation.Completed.equalsIgnoreCase(vo.getCovStatus())){
                      row.createCell(cellIndex++).setCellValue(CwnUtil.formatNumber(vo.getFinalCredit(), 2, BigDecimal.ROUND_DOWN));  
                  }else{
                      row.createCell(cellIndex++).setCellValue(CwnUtil.formatNumber(0, 2, BigDecimal.ROUND_DOWN));  
                  }
              }else{
                  row.createCell(cellIndex++).setCellValue("--"); 
              }
          }

	}
	
	//写入报表统计信息    页面统计内容 --数据以课程为主
	private Integer setReportHeardByCourse(HSSFSheet sheet,HSSFCellStyle style,Map data,int rowIndex,String lan ,LearnerReportParamVo param){
		style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);//右对齐
		
		HSSFRow row = sheet.createRow(rowIndex++); 
		row.setHeight((short) 400);
		HSSFCell cellEA = row.createCell(0); 
		cellEA.setCellValue(LabelContent.get(lan, "label_core_report_127"));  //课程/考试分布统计
		cellEA.setCellStyle(style);
		HSSFCell cellEB = row.createCell(4); 
		cellEB.setCellValue(LabelContent.get(lan, "label_core_report_128"));  //报名记录分布统计
		cellEB.setCellStyle(style);
		HSSFCell cellEC = row.createCell(8); 
		cellEC.setCellValue(LabelContent.get(lan, "label_core_report_129"));  //在线学习时长分布统计
		cellEC.setCellStyle(style);
		row = sheet.createRow(rowIndex++); 
		HSSFCell cellF = row.createCell(0);  
		cellF.setCellValue(LabelContent.get(lan, "label_core_report_130")+"：");  //课程/考试总数
		cellF.setCellStyle(style); 
		row.createCell(1).setCellValue(data.get("courseExamTotle").toString());
		HSSFCell cellFB = row.createCell(4);  
		cellFB.setCellValue(LabelContent.get(lan, "label_core_report_131")+"：");  //总报名人次
		cellFB.setCellStyle(style); 
		row.createCell(5).setCellValue(data.get("enrollUserCount").toString());
		HSSFCell cellFC = row.createCell(8);  
		cellFC.setCellValue(LabelContent.get(lan, "label_core_report_132")+"：");  //学习总时长：
		cellFC.setCellStyle(style); 
		row.createCell(9).setCellValue(data.get("totleCovTotleTime").toString());
		row = sheet.createRow(rowIndex++); 
		

		HSSFCell cellGB = row.createCell(4); 
		cellGB.setCellValue(LabelContent.get(lan, "label_core_report_133")+"：");  //平均报名人次：
		cellGB.setCellStyle(style); 
		row.createCell(5).setCellValue(data.get("avgErollCount").toString());
		HSSFCell cellGC = row.createCell(8);  
		cellGC.setCellValue(LabelContent.get(lan, "label_core_report_134")+"：");  //平均学习时长：
		cellGC.setCellStyle(style); 
		row.createCell(9).setCellValue(data.get("avgCovTotleTime").toString());
		row = sheet.createRow(rowIndex++); 
		
		if(containCourseType(param, course_type_online_course)){
			HSSFCell cellG = row.createCell(0);  
			cellG.setCellValue(LabelContent.get(lan, "label_core_report_53")+"：");  //网上课程
			cellG.setCellStyle(style); 
			row.createCell(1).setCellValue(data.get("onlineCourseCount").toString());
			row.createCell(2).setCellValue(isNullOrZero(data.get("onlineCourseCountPercentage").toString(),"%",false));
			
			HSSFCell cellHB = row.createCell(4);  
			cellHB.setCellValue(LabelContent.get(lan, "label_core_report_53")+"：");  //网上课程
			cellHB.setCellStyle(style); 
			row.createCell(5).setCellValue(data.get("onlineCourseEnrollCount").toString());
			row.createCell(6).setCellValue(isNullOrZero(data.get("onlineCourseEnrollCountPercentage").toString(),"%",false));
			
			HSSFCell cellHC = row.createCell(8);  
			cellHC.setCellValue(LabelContent.get(lan, "label_core_report_53")+"：");  //网上课程
			cellHC.setCellStyle(style); 
			row.createCell(9).setCellValue(data.get("onlineCourseCovDisplayTime").toString());
			row.createCell(10).setCellValue(isNullOrZero(data.get("onlineCourseCovTimePercentage").toString(),"%",false));
			
			row = sheet.createRow(rowIndex++); 
		}
		
		if(containCourseType(param, course_type_offline_course)){
			HSSFCell cellH = row.createCell(0);  
			cellH.setCellValue(LabelContent.get(lan, "label_core_report_54")+"：");  //面授课程
			cellH.setCellStyle(style); 
			row.createCell(1).setCellValue(data.get("offlineCourseCount").toString());
			row.createCell(2).setCellValue(isNullOrZero(data.get("offlineCourseCountPercentage").toString(),"%",false));
			
			HSSFCell cellIB = row.createCell(4);  
			cellIB.setCellValue(LabelContent.get(lan, "label_core_report_54")+"：");  //"离线课程："
			cellIB.setCellStyle(style); 
			row.createCell(5).setCellValue(data.get("offlineCourseEnrollCount").toString());
			row.createCell(6).setCellValue(isNullOrZero(data.get("offlineCourseEnrollCountPercentage").toString(),"%",false));
			
			HSSFCell cellIC = row.createCell(8);  
			cellIC.setCellValue(LabelContent.get(lan, "label_core_report_54")+"：");  //离线课程
			cellIC.setCellStyle(style); 
			row.createCell(9).setCellValue(data.get("offlineCourseCovDisplayTime").toString());
			row.createCell(10).setCellValue(isNullOrZero(data.get("offlineCourseCovTimePercentage").toString(),"%",false));
			
			row = sheet.createRow(rowIndex++);
		}
		
		if(containCourseType(param, course_type_integrated)){
			HSSFCell cellI = row.createCell(0);  
			cellI.setCellValue(LabelContent.get(lan, "label_core_report_57")+"：");  //"项目式培训："
			cellI.setCellStyle(style); 
			row.createCell(1).setCellValue(data.get("integratedCount").toString());
			row.createCell(2).setCellValue(isNullOrZero(data.get("integratedCountPercentage").toString(),"%",false));
			
			HSSFCell cellJB = row.createCell(4);  
			cellJB.setCellValue(LabelContent.get(lan, "label_core_report_57")+"：");  //项目式培训
			cellJB.setCellStyle(style); 
			row.createCell(5).setCellValue(data.get("integratedEnrollCount").toString());
			row.createCell(6).setCellValue(isNullOrZero(data.get("integratedEnrollCountPercentage").toString(),"%",false));
			
			HSSFCell cellJC = row.createCell(8);  
			cellJC.setCellValue(LabelContent.get(lan, "label_core_report_57")+"：");  //项目式培训
			cellJC.setCellStyle(style); 
			row.createCell(9).setCellValue(data.get("integratedCovDisplayTime").toString());
			row.createCell(10).setCellValue(isNullOrZero(data.get("integratedCovTimePercentage").toString(),"%",false));
			
			row = sheet.createRow(rowIndex++); 
		}
		
		if(containCourseType(param, course_type_offline_exam)){
			HSSFCell cellJ = row.createCell(0);  
			cellJ.setCellValue(LabelContent.get(lan, "label_core_report_56")+"：");  //"离线考试："
			cellJ.setCellStyle(style); 
			row.createCell(1).setCellValue(data.get("offlineExamCount").toString());
			row.createCell(2).setCellValue(isNullOrZero(data.get("offlineExamCountPercentage").toString(),"%",false));
			
			HSSFCell cellKB = row.createCell(4);  
			cellKB.setCellValue(LabelContent.get(lan, "label_core_report_56")+"：");  //离线考试
			cellKB.setCellStyle(style); 
			row.createCell(5).setCellValue(data.get("offlineExamEnrollCount").toString());
			row.createCell(6).setCellValue(isNullOrZero(data.get("offlineExamEnrollCountPercentage").toString(),"%",false));
			
			HSSFCell cellKC = row.createCell(8);  
			cellKC.setCellValue(LabelContent.get(lan, "label_core_report_56")+"：");  //离线考试
			cellKC.setCellStyle(style); 
			row.createCell(9).setCellValue(data.get("offlineExamCovDisplayTime").toString());
			row.createCell(10).setCellValue(isNullOrZero(data.get("offlineExamCovTimePercentage").toString(),"%",false));
			
			row = sheet.createRow(rowIndex++); 
		}

		if(containCourseType(param, course_type_online_exam)){
			HSSFCell cellK = row.createCell(0);  
			cellK.setCellValue(LabelContent.get(lan, "label_core_report_55")+"：");  //网上考试
			cellK.setCellStyle(style); 
			row.createCell(1).setCellValue(data.get("onlineExamCount").toString());
			row.createCell(2).setCellValue(isNullOrZero(data.get("onlineExamCountPercentage").toString(),"%",false));
			
			HSSFCell cellL = row.createCell(4);  
			cellL.setCellValue(LabelContent.get(lan, "label_core_report_55")+"：");  //网上考试
			cellL.setCellStyle(style); 
			row.createCell(5).setCellValue(data.get("onlineExamEnrollCount").toString());
			row.createCell(6).setCellValue(isNullOrZero(data.get("onlineExamEnrollCountPercentage").toString(),"%",false));
			
			HSSFCell cellLB = row.createCell(8);  
			cellLB.setCellValue(LabelContent.get(lan, "label_core_report_55")+"：");  //网上考试
			cellLB.setCellStyle(style); 
			row.createCell(9).setCellValue(data.get("onlineExamCovDisplayTime").toString());
			row.createCell(10).setCellValue(isNullOrZero(data.get("onlineExamCovTimePercentage").toString(),"%",false));
		}
		
		return rowIndex;
	}
	
	private boolean containCourseType(LearnerReportParamVo param , Integer containValue){
		if(null!=param.getCourseType() && param.getCourseType().size()>0){
			if(param.getCourseType().contains(containValue)){
				return true;
			}
		}
		return false;
	}
	
	//导出明细记录
	public void setExportDetail(HSSFSheet sheet,HSSFWorkbook wb ,final String labellang,LearnerReportParamVo vo,Map data){
		Map<String,String> map =new HashMap<String, String>(){
			{
				put("userDispalyName", LabelContent.get(labellang, "label_core_report_78"));put("userFullName", LabelContent.get(labellang, "label_core_report_79"));put("gruopName", LabelContent.get(labellang, "label_core_report_80"));
				put("gradeName", LabelContent.get(labellang, "label_core_report_81"));put("userEmail", LabelContent.get(labellang, "label_core_report_82"));put("userTel", LabelContent.get(labellang, "label_core_report_83"));put("staffNo", LabelContent.get(labellang, "label_core_report_168"));
				put("itmCode", LabelContent.get(labellang, "label_core_report_84"));put("itmTitle", LabelContent.get(labellang, "label_core_report_85"));put("itmType", LabelContent.get(labellang, "label_core_report_86"));
				put("tcrTitle", LabelContent.get(labellang, "label_core_report_87"));put("classCode", LabelContent.get(labellang, "label_core_report_88"));put("classTitle", LabelContent.get(labellang, "label_core_report_89"));
				put("appCreateTime", LabelContent.get(labellang, "label_core_report_90"));put("appStatus", LabelContent.get(labellang, "label_core_report_91"));put("attTime", LabelContent.get(labellang, "label_core_report_92"));
				put("covStatus", LabelContent.get(labellang, "label_core_report_93"));put("covCommenceDatetime", LabelContent.get(labellang, "label_core_report_94"));put("covTotalTime", LabelContent.get(labellang, "label_core_report_95"));
				put("covScore", LabelContent.get(labellang, "label_core_report_96"));put("attRate", LabelContent.get(labellang, "label_core_report_97"));put("attemptCount", LabelContent.get(labellang, "label_core_report_98"));
				put("courseCatalog", LabelContent.get(labellang, "label_core_report_160"));
				put("courseCredit", LabelContent.get(labellang, "label_core_report_164"));
				
			}
		};
		List<String> labelsList =new  ArrayList<String>();  //获取勾选所需要显示的列   excel显示用
		List<String> labelsKeyList =new  ArrayList<String>();  //获取勾选所需要显示的列   key
		if(null != vo.getUserInfo() && vo.getUserInfo().size() > 0){
			for(int i=0;i<vo.getUserInfo().size();i++){
				labelsKeyList.add(vo.getUserInfo().get(i).toString());
				labelsList.add(map.get(vo.getUserInfo().get(i)));
			}
		}
		if(null != vo.getCourseInfo() && vo.getCourseInfo().size() > 0){
			for(int i=0;i<vo.getCourseInfo().size();i++){
				labelsKeyList.add(vo.getCourseInfo().get(i).toString());
				labelsList.add(map.get(vo.getCourseInfo().get(i)));
			}
		}
		if(null != vo.getOtherInfo() && vo.getOtherInfo().size() > 0){
			for(int i=0;i<vo.getOtherInfo().size();i++){
				labelsKeyList.add(vo.getOtherInfo().get(i).toString());
				labelsList.add(map.get(vo.getOtherInfo().get(i)));
			}
		}
		String[] labels = new String[labelsList.size()];
		labelsList.toArray(labels);
		String[] keyLabels = new String[labelsKeyList.size()];
		labelsKeyList.toArray(keyLabels);
		//明细页面列表头部
		getCell(sheet,0,wb , labellang ,labels);
		//明细页面 详细内容
		List<LearnerDetailReportVo>  learnerDetailReportVoList= (List<LearnerDetailReportVo>) data.get("detailReportList");
		int rowIndex = 0;
		for(int i=0;i<learnerDetailReportVoList.size();i++){
			LearnerDetailReportVo learnerDetailReportVo = learnerDetailReportVoList.get(i);
			if(null!=learnerDetailReportVo.getAppId()&&0!=learnerDetailReportVo.getAppId()){//只显示有appID的记录
				rowIndex++;
	        	HSSFRow row = sheet.createRow(rowIndex);  
	            //创建单元格，并设置值  
	        	detailedSetCell(row,learnerDetailReportVo,keyLabels,labellang);
			}
		}
	}
	
	
	private String isNullOrZero(Object count,String symbol,Boolean isZero){
		String result = "--";
		if(isZero){
			result = "0";
		}
		if(null!=count && count != "" && !count.equals("0.0") )
		{
			if(!symbol.equals("")){
				result = count+symbol;
			}else{
				result = count.toString();
			}
		}
		return result;
	}

	private String isNullReturn(Object str){
		String result = "";
		if(null != str){
			result = str.toString();
		}
		return result;
	}
	
	private boolean containStatus(List lst , String status){
		if(null!=lst && lst.size()>0){
			for(int i=0;i<lst.size();i++){
				String val = (String)lst.get(i);
				if(val.equalsIgnoreCase(status)){
					return true;
				}
			}
		}
		return false;
	}
	
	public Map<Integer,List<CatalogTreeVo>> getCatalogTree(){
		ConcurrentHashMap<Integer,List<CatalogTreeVo>> catalogMap = new ConcurrentHashMap<Integer,List<CatalogTreeVo>>();
		List<CatalogTreeVo> catalogList = learnerReportMapper.getCourseCatalogs();
		Long preIndexId = 0L;
		List<CatalogTreeVo> tmpList = new ArrayList<CatalogTreeVo>();
		for(int i =0;i< catalogList.size();i++){
			CatalogTreeVo c  = catalogList.get(i);
			CatalogTreeVo c_  = catalogList.get(catalogList.size() - 1);
			if(preIndexId.intValue() != c.getTndItmId().intValue() || i==catalogList.size()-1){
				if(i==0){
					tmpList.add(c);
				}
				
				if(preIndexId.intValue() != c_.getTndItmId().intValue()) {
					if(i!=0){
						addToCatalogMao(c, tmpList, catalogMap, preIndexId);
					}
					if(i==catalogList.size() - 1) {
						addToCatalogMao(c, tmpList, catalogMap, c.getTndItmId());
					}
				} else  {
					tmpList.add(c);
					if(i==catalogList.size() - 1) {
						addToCatalogMao(c, tmpList, catalogMap, c.getTndItmId());
					}
				}
				preIndexId = c.getTndItmId();
			}else{
				tmpList.add(c);
			}
		}
		return catalogMap;
	}
	
	public Map loadInfo(LearnerReportParamVo vo,Hashtable spec_pairs){

        Map resultMap = new HashMap<String,Object>();
       try {
        // 用户信息
        Vector spec_values;
        Vector spec_values_user_name;
        Vector spec_values_user;
        StringBuffer result = null;
        StringBuffer result_user = null;
        StringBuffer result_users_text = null;
        StringBuffer result_users_id = null;
        String str = new String();
        List list = new ArrayList();
        if (spec_pairs.containsKey("exportUser")) {
            if (spec_pairs.containsKey("exportUserIds")) {
                spec_values = (Vector) spec_pairs.get("exportUserIds");
                spec_values_user_name = (Vector) spec_pairs.get("exportUserIdsName");
                spec_values_user = (Vector) spec_pairs.get("exportUser");

                result_user = new StringBuffer();
                result_user = result_user.append(spec_values_user.elementAt(0));
                vo.setExportUser(Integer.valueOf(result_user.toString()));

                result = new StringBuffer();
                str = result.append(spec_values.elementAt(0)).toString();
                result_users_id = result;
                list = Arrays.asList(str.split(","));
                vo.setExportUserIds(list);
                
                result = new StringBuffer();
                result_users_text=result.append(spec_values_user_name.elementAt(0).toString());
                str= result_users_text.toString();
                list = Arrays.asList(str.split(","));
                vo.setExportUserNames(list);
            } else if (spec_pairs.containsKey("exportGroupIds")) {
                spec_values = (Vector) spec_pairs.get("exportGroupIds");
                spec_values_user_name = (Vector) spec_pairs.get("exportGroupIdsName");
                spec_values_user = (Vector) spec_pairs.get("exportUser");

                result_user = new StringBuffer();
                result_user = result_user.append(spec_values_user.elementAt(0));
                vo.setExportUser(Integer.valueOf(result_user.toString()));

                result = new StringBuffer();
                str = result.append(spec_values.elementAt(0)).toString();
                result_users_id = result;
                list = Arrays.asList(str.split(","));
                vo.setExportGroupIds(list);
                
                result = new StringBuffer();
                result_users_text=result.append(spec_values_user_name.elementAt(0).toString());
                str = result_users_text.toString();
                list = Arrays.asList(str.split(","));
                vo.setExportGroupNames(list);
            } else {
                result_user = new StringBuffer();
                spec_values_user = (Vector) spec_pairs.get("exportUser");
                for (int i = 0; i < spec_values_user.size(); i++) {
                    result_user = result_user.append(spec_values_user.elementAt(i));
                }
                str=result_user.toString();
                list = Arrays.asList(str.split(","));
                vo.setExportUser(Integer.valueOf(list.get(0).toString()));
                if(list.size()>1){
                    if("true".equals(list.get(1).toString())){
                        vo.setIncludeDelUser(true); 
                    }
                }
                }
         }
     // 课程信息
        Vector spec_values_course;
        Vector spec_values_course_list;
        Vector spec_values_course_num;
        StringBuffer result_course = null;
        StringBuffer result_course_list = null;
        StringBuffer result_course_num = null;
        if (spec_pairs.containsKey("exportCourse")) {
            if (spec_pairs.containsKey("exportCourseIds")) {
                spec_values_course = (Vector) spec_pairs.get("exportCourseIds");
                spec_values_course_list = (Vector) spec_pairs.get("exportCourseIdsName");
                spec_values_course_num = (Vector) spec_pairs.get("exportCourse");

                result_course_num = new StringBuffer();
                result_course_num = result_course_num.append(spec_values_course_num.elementAt(0));
                vo.setExportCourse(Integer.valueOf(result_course_num.toString()));

                result_course = new StringBuffer();
                str = result_course.append(spec_values_course.elementAt(0)).toString();
                list = Arrays.asList(str.split(","));
                vo.setExportCourseIds(list);

                result_course_list = new StringBuffer();
                str = result_course_list.append(spec_values_course_list.elementAt(0)).toString();
                list = Arrays.asList(str.split(","));
                vo.setExportCourseNames(list);
            }else if (spec_pairs.containsKey("exportCatalogIds")) {
                spec_values_course = (Vector) spec_pairs.get("exportCatalogIds");
                spec_values_course_list = (Vector) spec_pairs.get("exportCatalogIdsName");
                spec_values_course_num = (Vector) spec_pairs.get("exportCourse");

                result_course_num = new StringBuffer();
                result_course_num = result_course_num.append(spec_values_course_num.elementAt(0));
                vo.setExportCourse(Integer.valueOf(result_course_num.toString()));

                result_course = new StringBuffer();
                str = result_course.append(spec_values_course.elementAt(0)).toString();
                list = Arrays.asList(str.split(","));
                vo.setExportCatalogIds(list);

                result_course_list = new StringBuffer();
                str = result_course_list.append(spec_values_course_list.elementAt(0)).toString();
                list = Arrays.asList(str.split(","));
                vo.setExportCatalogNames(list);
            } else {
                result = new StringBuffer();
                spec_values = (Vector) spec_pairs.get("exportCourse");
                for (int i = 0; i < spec_values.size(); i++) {
                    result = result.append(spec_values.elementAt(i));
                }
                vo.setExportCourse(Integer.valueOf(result.toString()));
            }
        }
        // 课程类型
        Vector spec_values_type;
        StringBuffer result_type = null;
        List<String> lists = new ArrayList();
        List<Integer> listi = new ArrayList();
        if (spec_pairs.containsKey("courseType")) {
            spec_values_type = (Vector) spec_pairs.get("courseType");

            result_type = new StringBuffer();
            str = result_type.append(spec_values_type.elementAt(0))
                    .toString();
            lists = Arrays.asList(str.split(","));
            for (int i = 0, j = lists.size(); i < j; i++) {
                listi.add(Integer.parseInt(lists.get(i)));
            }
            vo.setCourseType(listi);

        }
        Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
        // 报名开始时间
        Vector spec_values_sdate;
        String strTime = null;
        Date date = null;
        if (spec_pairs.containsKey("appnStartDatetime")) {
            spec_values_sdate = (Vector) spec_pairs.get("appnStartDatetime");
            strTime = spec_values_sdate.elementAt(0).toString();
            SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = time.parse(strTime);
            vo.setAppnStartDatetime(date);

            c.setTime(date);
            int year = c.get(Calendar.YEAR); 
            int month = c.get(Calendar.MONTH)+1; 
            int day = c.get(Calendar.DATE); 
            strTime = year + "," + month + "," + day ; 
            
        }
        // 报名结束时间
        Vector spec_values_edate;
        String streTime = null;
        Date dates = null;
        if (spec_pairs.containsKey("appnEndDatetime")) {
            spec_values_edate = (Vector) spec_pairs.get("appnEndDatetime");
            streTime = spec_values_edate.elementAt(0).toString();
            SimpleDateFormat times = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dates = times.parse(streTime);
            vo.setAppnEndDatetime(dates);
            
            c.setTime(dates);
            int year = c.get(Calendar.YEAR); 
            int month = c.get(Calendar.MONTH)+1; 
            int day = c.get(Calendar.DATE); 
            streTime = year + "," + month + "," + day ; 

        }
        // 培训开始时间
        Vector spec_values_att;
        String attsTime = null;
        Date attsdate = null;
        if (spec_pairs.containsKey("attStartTime")) {
            spec_values_att = (Vector) spec_pairs.get("attStartTime");
            attsTime = spec_values_att.elementAt(0).toString();
            SimpleDateFormat attTime = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            attsdate = attTime.parse(attsTime);
            vo.setAttStartTime(attsdate);
            
            c.setTime(attsdate);
            int year = c.get(Calendar.YEAR); 
            int month = c.get(Calendar.MONTH)+1; 
            int day = c.get(Calendar.DATE); 
            attsTime = year + "," + month + "," + day ; 

        }
        // 培训结束时间
        Vector spec_values_atte;
        String atteTime = null;
        Date attedate = null;
        if (spec_pairs.containsKey("attEndTime")) {
            spec_values_atte = (Vector) spec_pairs.get("attEndTime");
            atteTime = spec_values_atte.elementAt(0).toString();
            SimpleDateFormat eTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            attedate = eTime.parse(atteTime);
            vo.setAttEndTime(attedate);
            
            c.setTime(attedate);
            int year = c.get(Calendar.YEAR); 
            int month = c.get(Calendar.MONTH)+1; 
            int day = c.get(Calendar.DATE); 
            atteTime = year + "," + month + "," + day ; 

        }
        // 报名状态
        Vector spec_values_as;
        StringBuffer result_as = null;
        String appStatus = null;
        if (spec_pairs.containsKey("appStatus")) {
            spec_values_as = (Vector) spec_pairs.get("appStatus");
            result_as = new StringBuffer();
            appStatus = result_as.append(spec_values_as.elementAt(0))
                    .toString();
            list = Arrays.asList(appStatus.split(","));
            vo.setAppStatus(list);
        }
        // 课程状态
        String courseStatus = null;
        if (spec_pairs.containsKey("courseStatus")) {
            spec_values_as = (Vector) spec_pairs.get("courseStatus");
            result_as = new StringBuffer();
            courseStatus = result_as.append(spec_values_as.elementAt(0))
                    .toString();
            list = Arrays.asList(courseStatus.split(","));
            vo.setCourseStatus(list);
        }
        // 结果数据统计方式
        Vector date_values;
        StringBuffer date_result = null;
        if (spec_pairs.containsKey("resultDataStatistic")) {
            date_result = new StringBuffer();
            date_values = (Vector) spec_pairs.get("resultDataStatistic");
            date_result = date_result.append(date_values.elementAt(0));
            vo.setResultDataStatistic(Integer.valueOf(date_result.toString()));
            if (spec_pairs.containsKey("includeNoDataCourse")) {
                date_values = (Vector) spec_pairs.get("includeNoDataCourse");
                date_result = date_result.append(","+date_values.elementAt(0));
                vo.setIncludeNoDataCourse(Integer.valueOf(date_values.elementAt(0).toString()));
            }else if(spec_pairs.containsKey("includeNoDataUser")){
                date_values = (Vector) spec_pairs.get("includeNoDataUser");
                date_result = date_result.append(","+date_values.elementAt(0));
                vo.setIncludeNoDataUser(Integer.valueOf(date_values.elementAt(0).toString()));
            }
            
        }

        // 导出明细记录
        Vector spec_values_detail;
        StringBuffer detail_result = new StringBuffer();
        StringBuffer detail_result_userInfo = new StringBuffer();
        StringBuffer detail_result_courseInfo = new StringBuffer();
        StringBuffer detail_result_otherInfo = new StringBuffer();
        String detail_result_str = "";
        if (spec_pairs.containsKey("isExportDetail")) {
            detail_result = new StringBuffer();
            spec_values_detail = (Vector) spec_pairs.get("isExportDetail");
            detail_result.append(spec_values_detail.elementAt(0));
            vo.setIsExportDetail(Boolean.parseBoolean(detail_result.toString()));

            if (spec_pairs.containsKey("userInfo")) {
                spec_values_detail = (Vector) spec_pairs.get("userInfo");
                detail_result_userInfo.append(spec_values_detail.elementAt(0));
                
                detail_result_str = detail_result_userInfo.toString();
                list = Arrays.asList(detail_result_str.split(","));
                vo.setUserInfo(list);
            }

            if (spec_pairs.containsKey("courseInfo")) {
                spec_values_detail = (Vector) spec_pairs.get("courseInfo");
                detail_result_courseInfo.append(spec_values_detail.elementAt(0));
                
                detail_result_str = detail_result_courseInfo.toString();
                list = Arrays.asList(detail_result_str.split(","));
                vo.setCourseInfo(list);
            }

            if (spec_pairs.containsKey("otherInfo")) {
                spec_values_detail = (Vector) spec_pairs.get("otherInfo");
                detail_result_otherInfo.append(spec_values_detail.elementAt(0));
                
                detail_result_str = detail_result_otherInfo.toString();
                list = Arrays.asList(detail_result_str.split(","));
                vo.setOtherInfo(list);
            }
        }
        
        resultMap.put("pageResultDataStatistic", date_result);
        resultMap.put("pageAppnStartDatetime", strTime);
        resultMap.put("pageAppnEndDatetime", streTime);
        resultMap.put("pageAttStartTime", attsTime);
        resultMap.put("pageAttEndTime", atteTime);
        resultMap.put("pageCourseType", result_type);
        resultMap.put("pageExportCourse", result_course_num);
        resultMap.put("pageExportCourseIds", result_course);
        resultMap.put("pageExportCourseIdsText", result_course_list);
        resultMap.put("pageExportUser", result_user);
        resultMap.put("pageExportUserIds", result_users_id);
        resultMap.put("pageExportUserIdsText", result_users_text);
        resultMap.put("pageIsExportDetail", detail_result);
        resultMap.put("pageUserInfo", detail_result_userInfo);
        resultMap.put("pageCourseInfo", detail_result_courseInfo);
        resultMap.put("pageOtherInfo", detail_result_otherInfo);
        resultMap.put("pageAppStatus", appStatus);
        resultMap.put("pageCourseStatus", courseStatus);
        
        } catch (ParseException e) {
            e.printStackTrace();
        }
	    return resultMap;
	}
	
	private void addToCatalogMao(CatalogTreeVo c, List<CatalogTreeVo> tmpList,
			ConcurrentHashMap<Integer, List<CatalogTreeVo>> catalogMap, Long preIndexId) {
		List sameItmList = new ArrayList();
		for (Iterator<CatalogTreeVo> it = tmpList.iterator(); it.hasNext();) { 
			CatalogTreeVo vo = it.next();
			if(vo.getTnrOrder()==1L){
				vo.setNextParent(vo.getTndId());
				sameItmList.add(vo);
			}else{
				setChildCatalogName(sameItmList,vo);
			}
        }
		List sameItmListClone = new  ArrayList();
		CollectionUtils.addAll(sameItmListClone, new Object[sameItmList.size()]);
		Collections.copy(sameItmListClone, sameItmList);  
		catalogMap.put(preIndexId.intValue(), sameItmListClone);
		tmpList.clear();
		tmpList.add(c);
	}

}
