package com.cwn.wizbank.scheduled;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cw.wizbank.ScheduledTask;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.message.MessageScheduler;
import com.cw.wizbank.qdb.qdbAction;
import com.cw.wizbank.util.cwSQL;
import com.cwn.wizbank.cpd.service.AeItemCPDGourpItemService;
import com.cwn.wizbank.cpd.service.CpdGroupPeriodService;
import com.cwn.wizbank.cpd.service.CpdGroupRegCourseHistoryService;
import com.cwn.wizbank.cpd.service.CpdGroupRegHoursHistoryService;
import com.cwn.wizbank.cpd.service.CpdGroupRegistrationService;
import com.cwn.wizbank.cpd.service.CpdGroupService;
import com.cwn.wizbank.cpd.service.CpdLrnAwardRecordService;
import com.cwn.wizbank.cpd.service.CpdManagementService;
import com.cwn.wizbank.cpd.service.CpdRegistrationMgtService;
import com.cwn.wizbank.cpd.service.CpdReportRemarkService;
import com.cwn.wizbank.cpd.service.CpdUtilService;
import com.cwn.wizbank.cpd.utils.CpdUtils;
import com.cwn.wizbank.cpd.vo.CpdHourVO;
import com.cwn.wizbank.cpd.vo.CpdPeriodVO;
import com.cwn.wizbank.entity.AeAttendance;
import com.cwn.wizbank.entity.AeItem;
import com.cwn.wizbank.entity.AeItemCPDGourpItem;
import com.cwn.wizbank.entity.CpdGroup;
import com.cwn.wizbank.entity.CpdGroupPeriod;
import com.cwn.wizbank.entity.CpdGroupRegCourseHistory;
import com.cwn.wizbank.entity.CpdGroupRegHours;
import com.cwn.wizbank.entity.CpdGroupRegHoursHistory;
import com.cwn.wizbank.entity.CpdGroupRegistration;
import com.cwn.wizbank.entity.CpdLrnAwardRecord;
import com.cwn.wizbank.entity.CpdRegistration;
import com.cwn.wizbank.entity.CpdReportRemark;
import com.cwn.wizbank.entity.CpdReportRemarkHistory;
import com.cwn.wizbank.entity.CpdType;
import com.cwn.wizbank.services.AeAttendanceService;
import com.cwn.wizbank.services.AeItemService;
import com.cwn.wizbank.utils.CommonLog;
import com.cwn.wizbank.utils.DateUtil;
import com.cwn.wizbank.web.WzbApplicationContext;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class CpdHistoryScheduler extends ScheduledTask implements Job{
		
	CpdUtilService cpdUtilService = (CpdUtilService) WzbApplicationContext.getBean("cpdUtilService");
	
	CpdManagementService cpdManagementService = (CpdManagementService) WzbApplicationContext.getBean("cpdManagementService"); 
	
	CpdRegistrationMgtService cpdRegistrationMgtService = (CpdRegistrationMgtService) WzbApplicationContext.getBean("cpdRegistrationMgtService"); 
	
	CpdGroupRegistrationService cpdGroupRegistrationService = (CpdGroupRegistrationService) WzbApplicationContext.getBean("cpdGroupRegistrationService");
	
	CpdGroupRegHoursHistoryService cpdGroupRegHoursHistoryService  = (CpdGroupRegHoursHistoryService) WzbApplicationContext.getBean("cpdGroupRegHoursHistoryService");
	
	CpdGroupService cpdGroupService = (CpdGroupService) WzbApplicationContext.getBean("cpdGroupService");
	
	CpdGroupPeriodService cpdGroupPeriodService = (CpdGroupPeriodService) WzbApplicationContext.getBean("cpdGroupPeriodService");
	
	CpdLrnAwardRecordService cpdLrnAwardRecordService = (CpdLrnAwardRecordService) WzbApplicationContext.getBean("cpdLrnAwardRecordService");
	
	AeItemCPDGourpItemService aeItemCPDGourpItemService = (AeItemCPDGourpItemService) WzbApplicationContext.getBean("aeItemCPDGourpItemService");
	
	AeItemService aeItemService = (AeItemService) WzbApplicationContext.getBean("aeItemService");
	
	CpdGroupRegCourseHistoryService cpdGroupRegCourseHistoryService = (CpdGroupRegCourseHistoryService) WzbApplicationContext.getBean("cpdGroupRegCourseHistoryService");

	AeAttendanceService aeAttendanceService  = (AeAttendanceService) WzbApplicationContext.getBean("aeAttendanceService");
	
	CpdReportRemarkService cpdReportRemarkService  = (CpdReportRemarkService) WzbApplicationContext.getBean("cpdReportRemarkService");
	
	public CpdHistoryScheduler(){

	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void process() {
		try{
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			//获取当前月份（需要+1因为从0开始算）
			int currentMon = cal.get(Calendar.MONTH)+1;
			int currentDate = cal.get(Calendar.DAY_OF_MONTH);
			int currentYear = cal.get(Calendar.YEAR);
			//如果开放了cpd功能
			if(AccessControlWZB.hasCPDFunction()){
			    /*try {
			    	//保存报表历史记录
			    	saveRemarkHistory(currentYear,currentMon,currentDate);
				} catch (Exception e) {
					CommonLog.error("CpdHistoryScheduler saveRemarkHistory exception:"+e.getMessage(),e);
				}*/
			    try {
					//保存CPD历史快照
					saveCpdHistory(currentMon,currentDate);
                } catch (Exception e) {
                	CommonLog.error("CpdHistoryScheduler saveCpdHistory exception:"+e.getMessage(),e);
				}
			    
				try {
					//生成未来的CPD牌照
					createFutureCpd();
				} catch (Exception e) {
					CommonLog.error("CpdHistoryScheduler createFutureCpd exception:"+e.getMessage(),e);
				}
					
			}
		}catch(Exception e){
			e.printStackTrace();
			CommonLog.error(e.getMessage(),e);
			e.printStackTrace();
           try {
               if (con != null && !con.isClosed()) {
                   con.rollback();
               }
           } catch (SQLException e1) {
        	   CommonLog.error(e.getMessage(),e);
           }
		} finally {
			if (this.con != null) {
				try {
					con.close();
				} catch (SQLException e) {
                    CommonLog.error(e.getMessage(),e);
				}
			}
		}
	}
	
	//保存报表备注历史记录
	private void saveRemarkHistory(int period ,int currentMon,int currentDate){
		List<CpdType> typeList = cpdManagementService.getAll();
		 Map<String , CpdReportRemark> ramarkMap = getCpdReportRemarkMap();
		 Date today = new Date();
		for(CpdType type : typeList){
			//如果到了每个大牌的开始时间 保存上一周期的报表remark
			if(currentMon >= type.getCt_starting_month() ){
				saveRemarkHistoryDo(CpdReportRemark.OUTSTANDING_REMARK_CODE,period-1,currentMon,
						ramarkMap.get(CpdReportRemark.OUTSTANDING_REMARK_CODE),today);
				
				saveRemarkHistoryDo(CpdReportRemark.AWARDED_REMARK_CODE,period-1,currentMon,
						ramarkMap.get(CpdReportRemark.AWARDED_REMARK_CODE),today);
				
				saveRemarkHistoryDo(CpdReportRemark.INDIVIDUAL_REMARK_CODE,period-1,currentMon,
						ramarkMap.get(CpdReportRemark.INDIVIDUAL_REMARK_CODE),today);
				
				saveRemarkHistoryDo(CpdReportRemark.LICENSE_REGISTRATION_CODE,period-1,currentMon,
						ramarkMap.get(CpdReportRemark.LICENSE_REGISTRATION_CODE),today);
			}
		}
	}
	
	private void saveRemarkHistoryDo(String code , int period ,int month ,CpdReportRemark remark,Date today){
		List<CpdReportRemarkHistory> histList =cpdReportRemarkService.getCpdReportRemarkHistory(code, period, month);
		if(null==histList || histList.size()<=0){
			if(null != remark){
				CpdReportRemarkHistory his = new CpdReportRemarkHistory(remark,period-1,month,today);
				cpdReportRemarkService.saveHistory(remark, period, month);
			}
		
		}
	}
	
	private Map<String , CpdReportRemark> getCpdReportRemarkMap(){
		List<CpdReportRemark> list = cpdReportRemarkService.findAll();
		Map<String , CpdReportRemark> map = new HashMap<String,CpdReportRemark>();
    	if(null!=list){
    		for(CpdReportRemark remark : list){
    			map.put(remark.getCrpm_report_code(), remark);
    		}
    	}
    	return map;
	}

	//生成未来的CPD牌照
	private void createFutureCpd(){
		//根据当前时间获取到所有小牌注册信息
		List<CpdGroupRegistration> list = cpdGroupRegistrationService.getByNowDate();
		if(null!=list){
			for(int i=0;i<list.size();i++){
				CpdGroupRegistration cpdGroupReg =list.get(i);
				//获取用户注册对应的小牌
				CpdGroup cpdGroup = cpdGroupService.get(cpdGroupReg.getCgr_cg_id());
				//获取用户注册对应的大牌
				CpdType cpdType = cpdManagementService.get(cpdGroup.getCg_ct_id());
				//获取当前周期            
				CpdPeriodVO cpdGroupPeriod  = cpdUtilService.getCurrentPeriod(cpdType.getCt_id());
				//获取设置所需时数
				CpdGroupPeriod groupPeriod = cpdUtilService.getReqHoursList(cpdGroup.getCg_id());
				//System.out.println("执行生成所需学时.....");
				//执行（是否需要新增周期记录）
				if(groupPeriod!=null){//当所需时数为null时不新增记录
	                cpdUtilService.calReqHours(cpdGroupReg,cpdType ,cpdGroup , groupPeriod,CpdUtils.REQUIRE_HOURS_ACTION_FUTURE, cpdGroupPeriod , (long) 0);
				}
			}
		}
	}
	
	//保存CPD历史快照
	private void saveCpdHistory(Integer  currentMon,Integer  currentDate){
		List<CpdGroupRegistration> allCgrList = cpdGroupRegistrationService.getAllCpdGroupReg();
		
		for(CpdGroupRegistration cgr : allCgrList){
				//获取大牌注册信息
				CpdRegistration cr =cpdRegistrationMgtService.getCpdRegistrationById(cgr.getCgr_cr_id());
				if(null!=cr){
					//获取当前的周期
					CpdPeriodVO currentPeriod = cpdUtilService.getCurrentPeriod(cr.getCr_ct_id());
					//获取小牌注册冗余表数据
					List<CpdGroupRegHours> cgrhList = cpdGroupRegistrationService.getCpdGroupRegHours(cgr.getCgr_id());
					//获取大牌
					CpdType cpdType = cpdManagementService.getById(cr.getCr_ct_id());
					for(CpdGroupRegHours cgrh : cgrhList){					
						//获取当前注册记录的 新增时间 cgrh.getCgrh_create_datetime() 
						//当小牌时数记录（cgrh） 中的创建时间，在当前周期内，则表示是在当前周期新增了 跨以往周期的注册记录，该记录不记入历史记录表。
					    //A.after(B),当A大于B时，返回TRUE，当小于等于时，返回false
                        if(!cgrh.getCgrh_create_datetime().before(currentPeriod.getStartTime())){
                            continue;
                        }
                        //当前年份周期
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(new Date());
                        int curYear = cal.get(Calendar.YEAR);
						
						//如果小牌注册的周期小于当前周期 且 是当前周期的上一个周期 且 是当前周期的开始时间
						//那么判断下历史记录有没保存没有就保存
				        //旧的判断逻辑
						if(cgrh.getCgrh_cgr_period() < currentPeriod.getPeriod() 
								&& (currentMon >= cpdType.getCt_starting_month() )
								&& cgrh.getCgrh_cgr_period()==currentPeriod.getPeriod()-1){
				        
				        //if(cgrh.getCgrh_cgr_period() < curYear && cgrh.getCgrh_cgr_period() == curYear - 1 ){
							List hisList = cpdGroupRegHoursHistoryService.getCpdGroupRegHoursHistory(cgrh.getCgrh_usr_ent_id(), cgr.getCgr_cg_id(),
									cgrh.getCgrh_cgr_period(),cgrh.getCgrh_cgr_id());
							if(null!=hisList && hisList.size()>0 ){
								//如果已经有数据了就不管
								continue;
							}else{
								//获取小牌
								CpdGroup cpdGroup = cpdGroupService.get(cgr.getCgr_cg_id());
								//获取生效周期
								CpdGroupPeriod cgPeriod = cpdGroupPeriodService.get(cgrh.getCgrh_cgp_id());
								CpdGroupRegHoursHistory hist = new CpdGroupRegHoursHistory();
								hist.setCghi_usr_ent_id(cgrh.getCgrh_usr_ent_id());
								hist.setCghi_ct_id(cpdType.getCt_id());
								hist.setCghi_cg_id(cpdGroup.getCg_id());
								hist.setCghi_license_type(cpdType.getCt_license_type());
								hist.setCghi_license_alias(cpdType.getCt_license_alias());
								hist.setCghi_cal_before_ind(cpdType.getCt_cal_before_ind());
								hist.setCghi_recover_hours_period(cpdType.getCt_recover_hours_period());
								hist.setCghi_code(cpdGroup.getCg_code());
								hist.setCghi_alias(cpdGroup.getCg_alias());
								hist.setCghi_cgr_id(cgrh.getCgrh_cgr_id());
								hist.setCghi_initial_date(cgr.getCgr_initial_date());
								hist.setCghi_expiry_date(cgr.getCgr_expiry_date());
								hist.setCghi_cr_reg_date(cr.getCr_reg_datetime());
								hist.setCghi_cr_de_reg_date(cr.getCr_de_reg_datetime());
								hist.setCghi_cr_reg_number(cr.getCr_reg_number());
								hist.setCghi_period(cgrh.getCgrh_cgr_period());
								hist.setCghi_first_ind(cgr.getCgr_first_ind());
								hist.setCghi_actual_date(cgr.getCgr_actual_date());
								hist.setCghi_ct_starting_month(cpdType.getCt_starting_month());
								hist.setCghi_cgp_id(cgPeriod.getCgp_id());
								hist.setCghi_cgp_effective_time(cgPeriod.getCgp_effective_time());
								hist.setCghi_cal_start_date(cgrh.getCgrh_cal_start_date());
								//获取的时间缺少  时分秒，在周期结束的一天  获取的数据无法统计
								hist.setCghi_cal_end_date(DateUtil.getInstance().getEndDateDay(cgrh.getCgrh_cal_end_date()));
								hist.setCghi_manul_core_hours(cgrh.getCgrh_manul_core_hours());
								hist.setCghi_manul_non_core_hours(cgrh.getCgrh_manul_non_core_hours());
								hist.setCghi_manul_ind(cgrh.getCgrh_manul_ind());
								hist.setCghi_req_core_hours(cgrh.getCgrh_req_core_hours());
								hist.setCghi_req_non_core_hours(cgrh.getCgrh_req_non_core_hours());
								hist.setCghi_execute_core_hours(cgrh.getCgrh_execute_core_hours());
								hist.setCghi_execute_non_core_hours(cgrh.getCgrh_execute_non_core_hours());
								hist.setCghi_cal_month(cgrh.getCgrh_cal_month());
								hist.setCghi_cg_contain_non_core_ind(cpdGroup.getCg_contain_non_core_ind());
								Date now = new Date();
								hist.setCghi_create_datetime(now);
								hist.setCghi_update_datetime(now);
								hist.setCghi_cgr_id(cgr.getCgr_id());
								CpdHourVO hour = cpdLrnAwardRecordService.getTotleAwardHoursByCgId(cgrh.getCgrh_usr_ent_id(), cpdGroup.getCg_id(), 
											cpdGroup.getCg_contain_non_core_ind(),cgrh.getCgrh_cal_start_date(),cgrh.getCgrh_cal_end_date());
								if(null != hour){
									hist.setCghi_award_core_hours(hour.getTotle_award_core_hours());
									hist.setCghi_award_non_core_hours(hour.getTotle_award_non_core_hours());
								}else{
									hist.setCghi_award_core_hours(0f);
									hist.setCghi_award_non_core_hours(0f);
								}
								cpdGroupRegHoursHistoryService.add(hist);
								//获取学员在该课程下获得的时数记录
								List<CpdLrnAwardRecord> lrnRecords = cpdLrnAwardRecordService.getCpdLrnAwardRecord(cgrh.getCgrh_usr_ent_id(), cpdGroup.getCg_id(),cgrh.getCgrh_cgr_period(),cgr.getCgr_id());
								for(CpdLrnAwardRecord record : lrnRecords){
									AeItemCPDGourpItem cptItem = aeItemCPDGourpItemService.getAeItemCPDGourpItemBy(record.getClar_itm_id());
									if(null != cptItem){
										AeItem aeItem = aeItemService.get(record.getClar_itm_id());
										AeAttendance att = aeAttendanceService.get(record.getClar_app_id());
										CpdGroupRegCourseHistory cpdGroupRegCourseHistory = new CpdGroupRegCourseHistory();
										long cghi_id = cpdGroupRegHoursHistoryService.getCghiID(hist);
										cpdGroupRegCourseHistory.setCrch_cghi_id(cghi_id);
										cpdGroupRegCourseHistory.setCrch_aci_id(cptItem.getAeItemCPDItem().getAci_id());
										cpdGroupRegCourseHistory.setCrch_aci_hours_end_date(cptItem.getAeItemCPDItem().getAci_hours_end_date());
										cpdGroupRegCourseHistory.setCrch_aci_accreditation_code(cptItem.getAeItemCPDItem().getAci_accreditation_code());
										cpdGroupRegCourseHistory.setCrch_itm_id(aeItem.getItm_id());
										cpdGroupRegCourseHistory.setCrch_itm_title(aeItem.getItm_title());
										cpdGroupRegCourseHistory.setCrch_itm_code(aeItem.getItm_code());
										cpdGroupRegCourseHistory.setCrch_period(cgrh.getCgrh_cgr_period());
										cpdGroupRegCourseHistory.setCrch_first_ind(cgr.getCgr_first_ind());
										cpdGroupRegCourseHistory.setCrch_app_id(record.getClar_app_id());
										cpdGroupRegCourseHistory.setCrch_cgr_id(cgr.getCgr_id());
										cpdGroupRegCourseHistory.setCrch_cr_id(cr.getCr_id());
										cpdGroupRegCourseHistory.setCrch_ct_id(cpdType.getCt_id());
										cpdGroupRegCourseHistory.setCrch_cg_id(cpdGroup.getCg_id());
										cpdGroupRegCourseHistory.setCrch_ct_license_type(cpdType.getCt_license_type());
										cpdGroupRegCourseHistory.setCrch_ct_license_alias(cpdType.getCt_license_alias());
										cpdGroupRegCourseHistory.setCrch_cg_code(cpdGroup.getCg_code());
										cpdGroupRegCourseHistory.setCrch_cg_alias(cpdGroup.getCg_alias());
										cpdGroupRegCourseHistory.setCrch_award_core_hours(record.getClar_award_core_hours());
										cpdGroupRegCourseHistory.setCrch_award_non_core_hours(record.getClar_award_non_core_hours());
										cpdGroupRegCourseHistory.setCrch_award_datetime(att.getAtt_timestamp());
										cpdGroupRegCourseHistory.setCrch_create_datetime(now);
										cpdGroupRegCourseHistory.setCrch_update_datetime(now);
										cpdGroupRegCourseHistoryService.add(cpdGroupRegCourseHistory);
									}
								}
								
							}
						}
					}
				}
			}
	}

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		init();
		process();
	}
}
