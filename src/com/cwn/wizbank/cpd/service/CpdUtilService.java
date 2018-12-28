package com.cwn.wizbank.cpd.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.cpd.utils.CpdUtils;
import com.cwn.wizbank.cpd.vo.CpdPeriodVO;
import com.cwn.wizbank.entity.AeApplication;
import com.cwn.wizbank.entity.AeItem;
import com.cwn.wizbank.entity.AeItemCPDGourpItem;
import com.cwn.wizbank.entity.AeItemCPDItem;
import com.cwn.wizbank.entity.CourseEvaluation;
import com.cwn.wizbank.entity.CpdGroup;
import com.cwn.wizbank.entity.CpdGroupHours;
import com.cwn.wizbank.entity.CpdGroupPeriod;
import com.cwn.wizbank.entity.CpdGroupRegHours;
import com.cwn.wizbank.entity.CpdGroupRegHoursHistory;
import com.cwn.wizbank.entity.CpdGroupRegistration;
import com.cwn.wizbank.entity.CpdLrnAwardRecord;
import com.cwn.wizbank.entity.CpdReportRemark;
import com.cwn.wizbank.entity.CpdReportRemarkHistory;
import com.cwn.wizbank.entity.CpdType;
import com.cwn.wizbank.persistence.AeItemCPDGourpItemMapper;
import com.cwn.wizbank.persistence.AeItemCPDItemMapper;
import com.cwn.wizbank.persistence.AeItemMapper;
import com.cwn.wizbank.persistence.CourseEvaluationMapper;
import com.cwn.wizbank.persistence.CpdGroupHoursMapper;
import com.cwn.wizbank.persistence.CpdGroupMapper;
import com.cwn.wizbank.persistence.CpdGroupPeriodMapper;
import com.cwn.wizbank.persistence.CpdGroupRegHoursHistoryMapper;
import com.cwn.wizbank.persistence.CpdGroupRegHoursMapper;
import com.cwn.wizbank.persistence.CpdGroupRegistrationMapper;
import com.cwn.wizbank.persistence.CpdLrnAwardRecordMapper;
import com.cwn.wizbank.persistence.CpdRegistrationMapper;
import com.cwn.wizbank.persistence.CpdTypeMapper;
import com.cwn.wizbank.services.AeApplicationService;
import com.cwn.wizbank.utils.DateUtil;
import com.cwn.wizbank.utils.FormatUtil;

@Service
public class CpdUtilService {
	
	@Autowired
	CpdGroupRegistrationMapper cpdGroupRegistrationMapper;
	
	@Autowired
	CpdTypeMapper cpdTypeMapper;
	
	@Autowired
	CpdGroupPeriodMapper cpdGroupPeriodMapper;
	
	@Autowired
	CpdGroupHoursMapper cpdGroupHoursMapper;
	
	@Autowired
	CpdGroupRegHoursMapper cpdGroupRegHoursMapper;
	
	@Autowired
	CourseEvaluationMapper courseEvaluationMapper;
	
	@Autowired
	CpdRegistrationMapper cpdRegistrationMapper;
	
	@Autowired
	CpdLrnAwardRecordMapper cpdLrnAwardRecordMapper;
	
	@Autowired
	AeItemCPDGourpItemMapper aeItemCPDGourpItemMapper;
	
	@Autowired
	CpdGroupMapper cpdGroupMapper;
	
	@Autowired
	AeItemMapper aeItemMapper;
	
	@Autowired
	AeItemCPDItemMapper aeItemCPDItemMapper;
	
	@Autowired
	CpdGroupRegHoursHistoryMapper cpdGroupRegHoursHistoryMapper;
	
	@Autowired
	CpdReportRemarkService cpdReportRemarkService;
	
	@Autowired
	CpdGroupService cpdGroupService;
	
	@Autowired
	AeApplicationService aeApplicationService;

	/**
	 * 获取上一个周期的挂牌信息
	 * @param cgId
	 * @param usrEntId
	 * @param initial_date
	 * @return 如果是首次挂牌返回null  如果不是则返回上一条挂牌记录
	 */
	public CpdGroupRegistration getPreGroupRegistration(Long cgId , Long usrEntId , Date initial_date){
		Map param = new HashMap<String , Object>();
		param.put("usr_ent_id", usrEntId);
		param.put("cgr_cg_id", cgId);
		param.put("initial_date", initial_date);
		List<CpdGroupRegistration> list = cpdGroupRegistrationMapper.getCpdGroupRegBeforeTime(param);
		if(null!=list && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	   /**
     * 修改小牌报名记录是获取非当前小牌报名记录的上一个周期的挂牌信息
     * @param cgId
     * @param usrEntId
     * @param initial_date
     * @return 如果是首次挂牌返回null  如果不是则返回上一条挂牌记录
     */
    public CpdGroupRegistration getPreGroupRegistrationNoId(Long cgId , Long usrEntId , Date initial_date,Long cgr_id){
        Map param = new HashMap<String , Object>();
        param.put("usr_ent_id", usrEntId);
        param.put("cgr_cg_id", cgId);
        param.put("initial_date", initial_date);
        param.put("cgr_id", cgr_id);
        List<CpdGroupRegistration> list = cpdGroupRegistrationMapper.getPreGroupRegistrationNoId(param);
        if(null!=list && list.size()>0){
            return list.get(0);
        }
        return null;
    }
	
	/**
	 * 判断是否首次挂牌
	 * @param cgId
	 * @param usrEntId
	 * @param initial_date
	 * @return
	 */
	public boolean cpdIsFirstInd(Long cgId , Long usrEntId , Date initial_date){ 
		if(null==getPreGroupRegistration( cgId ,  usrEntId ,  initial_date)){
			return true;
		}
		return false;
	}
	
	/**
	 * 获取周期信息
	 * @param date 需要获取的周期
	 * @param cpdTypeId 需要获取的大牌
	 * @return
	 */
	public CpdPeriodVO getPeriod(Date date , Long cpdTypeId){

		CpdType type = new CpdType();
		type.setCt_id(cpdTypeId);
		type.setCt_status(CpdUtils.STATUS_OK);
		List<CpdType> typeList = cpdTypeMapper.getCpdType(type); //获取大牌
		if(null!=typeList && typeList.size()>0){
			type = typeList.get(0);
		}
		return getPeriodDo(date,type);
	}
	
	/**
	 * 获取周期信息
	 * @param date 需要获取的周期
	 * @param cpdTypeId 需要获取的大牌
	 * @return
	 * @throws SQLException 
	 */
	public CpdPeriodVO getPeriod(Date date , Long cpdTypeId ,Connection con) throws SQLException{
		CpdType type = CpdDbUtilsForOld.getCpdTypeById(cpdTypeId, con);
		return getPeriodDo(date,type);
	}
	
	private CpdPeriodVO getPeriodDo(Date date ,CpdType type){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int period = c.get(Calendar.YEAR);
		int periodMonth = c.get(Calendar.MONTH)+1;
		CpdPeriodVO vo = null;
		if(null!=type){
			int startMonth = type.getCt_starting_month()-1; //获取大牌的开始时间
			if(periodMonth<=startMonth){
				period--;
			}
			vo = new CpdPeriodVO();
			vo.setPeriod(period);
			c.set(period, startMonth, c.getActualMinimum(Calendar.DAY_OF_MONTH),0,0,0); //设置大牌的开始时间月份的1号为周期开始日期
			Date startDate = c.getTime();
			vo.setStartTime(startDate);
			c.add(Calendar.MONTH, 11);
			c.set(Calendar.DATE, c.getActualMaximum(Calendar.DAY_OF_MONTH)); //设置大牌的开始时间月份的12个月之后的月份的最后一天为周期结束日期
			c.set(Calendar.HOUR,23);
			c.set(Calendar.MINUTE,59);
			c.set(Calendar.SECOND,59);
			Date endDate = c.getTime();
			vo.setEndTime(endDate);
		}
		return vo;
	}
	
	public CpdPeriodVO getPeriod(Integer period , Long cpdTypeId){
		CpdType type = new CpdType();
		type.setCt_id(cpdTypeId);
		type.setCt_status(CpdUtils.STATUS_OK);
		List<CpdType> typeList = cpdTypeMapper.getCpdType(type); //获取大牌
		if(null!=typeList && typeList.size()>0){
			type = typeList.get(0);
		}
		return getPeriodDo(period,type);
	}
	
	private CpdPeriodVO getPeriodDo(Integer period ,CpdType type){
        Calendar c = Calendar.getInstance();
        CpdPeriodVO vo = null;
        if(null!=type){
            int startMonth = type.getCt_starting_month()-1; //获取大牌的开始时间
            vo = new CpdPeriodVO();
            vo.setPeriod(period);
            c.set(period, startMonth, c.getActualMinimum(Calendar.DAY_OF_MONTH),0,0,0); //设置大牌的开始时间月份的1号为周期开始日期
            Date startDate = c.getTime();
            vo.setStartTime(startDate);
            c.add(Calendar.MONTH, 11);
            c.set(Calendar.DATE, c.getActualMaximum(Calendar.DAY_OF_MONTH)); //设置大牌的开始时间月份的12个月之后的月份的最后一天为周期结束日期
            c.set(Calendar.HOUR,23);
            c.set(Calendar.MINUTE,59);
            c.set(Calendar.SECOND,59);
            Date endDate = c.getTime();
            vo.setEndTime(endDate);
        }
        return vo;
	}
	
	   /**
     * 获取某个年份的周期信息
     * @param date 需要获取的周期
     * @param period 需要获取的周期年份
     * @param cpdTypeId 需要获取的大牌
     * @return
     */
    public CpdPeriodVO getPeriodByYear(loginProfile prof,int period, Long cpdTypeId){
        Calendar c = Calendar.getInstance();
        CpdType type = new CpdType();
        SimpleDateFormat sdf = null;  
        if("en-us".equals(prof.getCur_lan())){
            sdf = new SimpleDateFormat("dd MMM yyyy",   Locale.ENGLISH);  
        }else{
            sdf = new SimpleDateFormat("yyyy 年 MM 月 dd 日");  
        }
        type.setCt_id(cpdTypeId);
        type.setCt_status(CpdUtils.STATUS_OK);
        List<CpdType> typeList = cpdTypeMapper.getCpdType(type); //获取大牌
        if(null!=typeList && typeList.size()>0){
            type = typeList.get(0);
        }
        CpdPeriodVO vo = null;
        if(null!=type){
            int startMonth = type.getCt_starting_month()-1; //获取大牌的开始时间
            vo = new CpdPeriodVO();
            vo.setPeriod(period);
            c.set(period, startMonth, c.getActualMinimum(Calendar.DAY_OF_MONTH),0,0,0); //设置大牌的开始时间月份的1号为周期开始日期
            Date startDate = c.getTime();
            vo.setStartTime(startDate);
            //c.add(Calendar.YEAR, 1);
            c.add(Calendar.MONTH, 11);
            c.set(Calendar.DATE, c.getActualMaximum(Calendar.DAY_OF_MONTH)); //设置大牌的开始时间月份的12个月之后的月份的最后一天为周期结束日期
            c.set(Calendar.HOUR,23);
            c.set(Calendar.MINUTE,59);
            c.set(Calendar.SECOND,59);
            Date endDate = c.getTime();
            vo.setEndTime(endDate);
            vo.setStartDate(sdf.format(startDate));
            vo.setEndDate(sdf.format(endDate));
        }

        return vo;
    }
    
    public CpdPeriodVO getPeriodByYear(int period, Long cpdTypeId){
        Calendar c = Calendar.getInstance();
        CpdType type = new CpdType();
        type.setCt_id(cpdTypeId);
        type.setCt_status(CpdUtils.STATUS_OK);
        List<CpdType> typeList = cpdTypeMapper.getCpdType(type); //获取大牌
        if(null!=typeList && typeList.size()>0){
            type = typeList.get(0);
        }
        CpdPeriodVO vo = null;
        if(null!=type){
            int startMonth = type.getCt_starting_month()-1; //获取大牌的开始时间
            vo = new CpdPeriodVO();
            vo.setPeriod(period);
            c.set(period, startMonth, c.getActualMinimum(Calendar.DAY_OF_MONTH),0,0,0); //设置大牌的开始时间月份的1号为周期开始日期
            Date startDate = c.getTime();
            vo.setStartTime(startDate);
            //c.add(Calendar.YEAR, 1);
            c.add(Calendar.MONTH, 11);
            c.set(Calendar.DATE, c.getActualMaximum(Calendar.DAY_OF_MONTH)); //设置大牌的开始时间月份的12个月之后的月份的最后一天为周期结束日期
            c.set(Calendar.HOUR,23);
            c.set(Calendar.MINUTE,59);
            c.set(Calendar.SECOND,59);
            Date endDate = c.getTime();
            vo.setEndTime(endDate);
        }

        return vo;
    }
	
	/**
	 * 获取当前周期信息
	 * @param cpdTypeId 需要获取的大牌
	 * @return
	 */
	public CpdPeriodVO getCurrentPeriod(Long cpdTypeId){
		return getPeriod(new Date(),cpdTypeId);
	}
	
	/**
	 * 计算小牌的实际开始时间
	 * @param cpdGroupRegistration    要注册的周期
	 * @param preCpdGroupRegistration 上一周期
	 * @param type 大牌信息
	 * @return
	 */
	public Date getCgrActualDate(CpdGroupRegistration cpdGroupRegistration , 
			CpdGroupRegistration preCpdGroupRegistration , CpdType type ){
		//是否首次挂牌
		if(1!=cpdGroupRegistration.getCgr_first_ind()){
			if(null!=type.getCt_recover_hours_period() && 0!=type.getCt_recover_hours_period() 
					&& null!=preCpdGroupRegistration) {
				//获取注册开始时间所在的周期
				CpdPeriodVO regPeriod = getPeriod(cpdGroupRegistration.getCgr_initial_date(),type.getCt_id());
				DateUtil dateUtil = new DateUtil();
				//cpdGroupRegistration.initial_date与上一条记录的小牌cgr_expiry_date距离的天数
				int diffDay = dateUtil.getDays(preCpdGroupRegistration.getCgr_expiry_date(), cpdGroupRegistration.getCgr_initial_date());
				//上一条记录的小牌cgr_expiry_date <= 注册开始时间所在的周期的结束时间（确保在同一周期内） )
				boolean isSamePeriod = false;
				if (preCpdGroupRegistration.getCgr_expiry_date() != null && preCpdGroupRegistration.getCgr_expiry_date().getTime() <= regPeriod.getEndTime().getTime() 
						&& regPeriod.getStartTime().getTime()<=preCpdGroupRegistration.getCgr_expiry_date().getTime() ){
					isSamePeriod = true;
				}
				if(diffDay <= type.getCt_recover_hours_period() && isSamePeriod){
					//Set实际开始时间为上一条记录的initial_date
					return preCpdGroupRegistration.getCgr_initial_date();
				}
			}
			
		}
		//实际开始时间为initial_date
		return cpdGroupRegistration.getCgr_initial_date();
	}
	
	/**
	 * 根据周期年份获取CpdGroupRegHours
	 * @param cpdGroupReg
	 * @param periodYear
	 */
	private CpdGroupRegHours getCpdGroupRegHoursByCgrId(CpdGroupRegistration cpdGroupReg , int periodYear){
		//根据小牌注册ID获取中间表信息
		List<CpdGroupRegHours>  records = cpdGroupRegHoursMapper.getByCgrId(cpdGroupReg.getCgr_id());
		//获取当前周期中间表的数据
		CpdGroupRegHours record = null;
		for(CpdGroupRegHours cpdGroupRegHours : records){
			if(cpdGroupRegHours.getCgrh_cgr_period()==periodYear ){
				return cpdGroupRegHours;
			}
		}
		return null;
	}
	
	/**
	 * 获取用于计算的理当前时间最近的需要时数周期
	 * @param cgId
	 * @return
	 */
	public CpdGroupPeriod getReqHoursList(Long cgId){
		Map map = new HashMap<String,Object>();
		map.put("cg_id", cgId);
		map.put("status", CpdUtils.STATUS_OK);
		List<CpdGroupPeriod> periods = cpdGroupPeriodMapper.getPeriod(map);
		CpdGroupPeriod calPeriod = null;
		if(null!=periods&&periods.size()>0){
			for(CpdGroupPeriod period : periods){
				Long effTimeSec = CpdUtils.converCpdTimeToMilliseconds(period.getCgp_effective_time());
				Long nowSec = CpdUtils.converCpdTimeToMilliseconds(new Date());
				//获取小于等于当前时间的有效周期
				if(effTimeSec <= nowSec){
					calPeriod = period;
					break;
				}
			}
		}
		if(null!=calPeriod){
			List<CpdGroupHours> hours = cpdGroupHoursMapper.getByCghCgpID(calPeriod.getCgp_id());
			calPeriod.setCpdGroupHours(hours);
		}
		return calPeriod;
	}
	
	/**
	 * 中间表记录变动计算方法
	 * cpdType 大 牌信息
	 * cpdGroup  小牌信息
	 * cpdGroupReg 注册信息
	 * req_lst  小牌要求时数列表 调用getReqHoursList()函数可以获取
	 * action:
	 *   RECAL : 如果是修改牌照的设置,需要当前周期重算要求
	 *   ADD : 添加注册信息
	 *   UPD : 修改注册记录
	 *   DEL ：删除
	 *   FUTURE : 生成未来的数据 一般用于线程构建超出当前周期的周期所需要时数
	 * prePeriod 上一个周期
	 * usr_ent_id 当前操作的用户ID 用于打日志
	 * 生成CpdGroupRegHours记录
	 */
	public void calReqHours(CpdGroupRegistration cpdGroupReg,CpdType cpdType ,CpdGroup cpdGroup , 
			CpdGroupPeriod req_period, String action, CpdPeriodVO prePeriod , Long opt_usr_ent_id){
		 //通过注册信息中大小牌的ID以及当前时间拿到 当前周期
		CpdPeriodVO currentPeriod = getCurrentPeriod(cpdType.getCt_id());
		List<CpdGroupHours> req_lst = null;
		if(null!=req_period && req_period.getCpdGroupHours()!=null){
			//有效的要求时数记录
			req_lst = req_period.getCpdGroupHours();
		}
		//如果是重算逻辑
		if(action.equalsIgnoreCase(CpdUtils.REQUIRE_HOURS_ACTION_RECAL) ){
			//只需要根据CPD要时数设置，重算当前周期中间表记录的要求时数
			//获取当前周期中间表的数据
			CpdGroupRegHours record = getCpdGroupRegHoursByCgrId(cpdGroupReg, currentPeriod.getPeriod());
			//是否有手动修改过
			if(null!=record && record.getCgrh_manul_ind()==0){
				//在修改牌照要求时数及小牌基本信息时会执行下面的逻辑
				//拿到当前周期需要时数集合
				//List<CpdGroupHours> currentPeriodReqHours = getGroupHoursInReqList(req_lst,currentPeriod.getPeriod());
				if(null!=req_lst){
					//获取计算月数
					int calMonth = record.getCgrh_cal_month();
					//获取当前周期对应计算月数需求的时数
					CpdGroupHours currentReqHours = null;
					for(CpdGroupHours reqHours : req_lst){
						if(null!=reqHours.getCgh_declare_month() 
								&& reqHours.getCgh_declare_month() == calMonth){
							currentReqHours = reqHours;
							break;
						}
					}
					//设置核心时数
					if(null!=currentReqHours){
						//设置核心时数
						record.setCgrh_req_core_hours(currentReqHours.getCgh_core_hours());
						//设置运算核心时数
						record.setCgrh_execute_core_hours(currentReqHours.getCgh_core_hours());
						//如果小牌需要非核实时数
						if(cpdGroup.getCg_contain_non_core_ind()==1){
							//设置非核心时数
							record.setCgrh_req_non_core_hours(currentReqHours.getCgh_non_core_hours());
							//设置运算非核心时数
							record.setCgrh_execute_non_core_hours(currentReqHours.getCgh_non_core_hours());
						}else{
							//设置非核心时数为0
							record.setCgrh_req_non_core_hours(0f);
							record.setCgrh_execute_non_core_hours(0f);
						}
					}else{
						record.setCgrh_req_core_hours(0f);
						record.setCgrh_req_non_core_hours(0f);
						record.setCgrh_execute_core_hours(0f);
						record.setCgrh_execute_non_core_hours(0f);
					}
					record.setCgrh_update_datetime(new Date());
					record.setCgrh_update_usr_ent_id(opt_usr_ent_id);
					cpdGroupRegHoursMapper.update(record);
				}
			}
		}else if(action.equalsIgnoreCase(CpdUtils.REQUIRE_HOURS_ACTION_ADD) || 
			action.equalsIgnoreCase(CpdUtils.REQUIRE_HOURS_ACTION_UPD) ||
			action.equalsIgnoreCase(CpdUtils.REQUIRE_HOURS_ACTION_DEL) 
		){
			//把注册开始结束时间转成毫秒 方便比较
			Long cpdGroupRegInitialDateSeconds = CpdUtils.converCpdTimeToMilliseconds(cpdGroupReg.getCgr_initial_date());
			Long cpdGroupRegExpiryDateSeconds = CpdUtils.converCpdTimeToMilliseconds(cpdGroupReg.getCgr_expiry_date());
			//把上一周期开始结束时间转成毫秒 方便比较
            Long prePeriodInitialDateSeconds = null;
            Long prePeriodExpiryDateSeconds = null;
            if(prePeriod!=null){
                prePeriodInitialDateSeconds = CpdUtils.converCpdTimeToMilliseconds(prePeriod.getStartTime());
                prePeriodExpiryDateSeconds = CpdUtils.converCpdTimeToMilliseconds(prePeriod.getEndTime());
            }
			
			//把当前周期开始结束时间转成毫秒 方便比较
			Long currentPeriodInitialDateSeconds = CpdUtils.converCpdTimeToMilliseconds(currentPeriod.getStartTime());
			Long currentPeriodExpiryDateSeconds = CpdUtils.converCpdTimeToMilliseconds(currentPeriod.getEndTime());
			//如果是更新操作 且
			//(注册周期和上一周期的开始时间，结束时间相同(即等于没修改过))
			//不需要改
			if(action.equalsIgnoreCase(CpdUtils.REQUIRE_HOURS_ACTION_UPD) && 
				( 	
					(prePeriod!=null && cpdGroupRegInitialDateSeconds == prePeriodInitialDateSeconds && prePeriodExpiryDateSeconds == cpdGroupRegExpiryDateSeconds) ||
					(prePeriod!=null && prePeriodExpiryDateSeconds > currentPeriodExpiryDateSeconds && cpdGroupRegExpiryDateSeconds != null  && cpdGroupRegExpiryDateSeconds > currentPeriodExpiryDateSeconds)||
					(prePeriod!=null && prePeriodExpiryDateSeconds > currentPeriodExpiryDateSeconds && cpdGroupRegExpiryDateSeconds != null  && cpdGroupRegExpiryDateSeconds==null)
				)
			){
				return;
			}else{
				//如果不是添加操作 物理删除该注册记录在中间表的数据
				if(!action.equalsIgnoreCase(CpdUtils.REQUIRE_HOURS_ACTION_ADD)){
					cpdGroupRegHoursMapper.deleteByCgrId(cpdGroupReg.getCgr_id());
				}
				//如果不是删除操作
				if(!action.equalsIgnoreCase(CpdUtils.REQUIRE_HOURS_ACTION_DEL)){
					//对注册信息每个周期生成一条记录，插入到中间表
					Date calEndDate =  null;
					Date calStartDate = null;
					if( cpdGroupRegExpiryDateSeconds != null  && cpdGroupRegExpiryDateSeconds > currentPeriodExpiryDateSeconds ){
					 	calEndDate = currentPeriod.getEndTime();
					 }else{
						 if(null == cpdGroupReg.getCgr_expiry_date() 
								 || (null != cpdGroupReg.getCgr_expiry_date() 
								 && (cpdGroupRegExpiryDateSeconds != null &&  cpdGroupRegExpiryDateSeconds > currentPeriodExpiryDateSeconds))){
							 calEndDate = currentPeriod.getEndTime();
						 }else{
							 calEndDate = cpdGroupReg.getCgr_expiry_date();
						 }
					 }
					calStartDate = cpdGroupReg.getCgr_initial_date();
					
					if(CpdUtils.converCpdTimeToMilliseconds(calStartDate) < CpdUtils.converCpdTimeToMilliseconds(calEndDate)){
						//根据calStartDate和 calEndDate生成 中间冗余表信息
						if(null!=req_period){
							calCpdGroupRegHoursAndSave(calStartDate,calEndDate,currentPeriod,cpdType,cpdGroupReg,req_period.getCgp_id(),req_lst,opt_usr_ent_id);
						}
					}else{
						//这种情况是未来的记录，不需求生成中间表数据
					}
				}
			}
		}else if(action.equalsIgnoreCase(CpdUtils.REQUIRE_HOURS_ACTION_FUTURE)){//如果是FUTURE 则是线程执行的构造未来周期的需要的时数要求
			//因为是生成未来的数据 所以这里的currentPeriod 应该就是未来的周期
			if(null!=currentPeriod){
				//把注册开始结束时间转成毫秒 方便比较
				Long cpdGroupRegExpiryDateSeconds = CpdUtils.converCpdTimeToMilliseconds(cpdGroupReg.getCgr_expiry_date());
				//把当前周期开始结束时间转成毫秒 方便比较
				Long currentPeriodExpiryDateSeconds = CpdUtils.converCpdTimeToMilliseconds(currentPeriod.getEndTime());
				//对注册信息每个周期生成一条记录，插入到中间表
				Date calEndDate =  null;
				//设置计算时间为未来的周期的开始时间
				Date calStartDate = currentPeriod.getStartTime();
				if(null!=cpdGroupReg.getCgr_expiry_date() && cpdGroupRegExpiryDateSeconds > currentPeriodExpiryDateSeconds  ){
				 	calEndDate = currentPeriod.getEndTime();
				 }else{
					 if(null == cpdGroupReg.getCgr_expiry_date() 
							 || (null != cpdGroupReg.getCgr_expiry_date() 
							 && cpdGroupRegExpiryDateSeconds > currentPeriodExpiryDateSeconds)){
						 calEndDate = currentPeriod.getEndTime();
					 }else{
						 calEndDate = cpdGroupReg.getCgr_expiry_date();
					 }
				 }
				CpdGroupRegHours cpdGroupRegHours = new CpdGroupRegHours();
				cpdGroupRegHours.setCgrh_usr_ent_id(cpdGroupReg.getCgr_usr_ent_id());
				cpdGroupRegHours.setCgrh_cgr_id(cpdGroupReg.getCgr_id());
				cpdGroupRegHours.setCgrh_cr_id(cpdGroupReg.getCgr_cr_id());
				cpdGroupRegHours.setCgrh_cgr_period(prePeriod.getPeriod());
				//判断是否已经存在记录
				if(!cpdGroupRegHoursMapper.isExist(cpdGroupRegHours)){
				  //根据calStartDate和 calEndDate生成 中间冗余表信息
				  calCpdGroupRegHoursAndSave(calStartDate,calEndDate,currentPeriod,cpdType,cpdGroupReg,req_period.getCgp_id(),req_lst,opt_usr_ent_id);
				}
		    }
		}
		
	}
	
	/**
	 * 根据calStartDate和 calEndDate生成 中间冗余表信息
	 * @param calStartDate
	 * @param calEndDate
	 * @param cpdType
	 * @param cpdGroupReg
	 * @param req_lst
	 */
	private void calCpdGroupRegHoursAndSave(Date calStartDate , Date calEndDate , CpdPeriodVO currentPeriod,
			CpdType cpdType ,CpdGroupRegistration cpdGroupReg ,Long reqPeriodId ,List<CpdGroupHours> req_lst ,Long opt_usr_ent_id){
		Calendar calendar = Calendar.getInstance();
		int priod_index = 0; //一段大周期内的小周期循环索引
		 //循环逐个周期算出需求时数 
		 while(CpdUtils.converCpdTimeToMilliseconds(calStartDate) < CpdUtils.converCpdTimeToMilliseconds(calEndDate)){
			 priod_index++;
			 //循环中每个周期需要的计算的开始时间可以结束时间
			 Date recordStartDate = calStartDate;
			 Date recordEndDate = null;
			 
			 //获取cal_start_date所属周期
			 CpdPeriodVO periodRecord = getPeriod(recordStartDate,cpdType.getCt_id()); //通过cal_start_date取到cal_start_date所在周期信息
			 
			if(null != cpdGroupReg.getCgr_expiry_date()
					&& CpdUtils.converCpdTimeToMilliseconds(periodRecord.getStartTime()) >=  CpdUtils.converCpdTimeToMilliseconds(currentPeriod.getEndTime())){
				break;
			}
		 	if(null != cpdGroupReg.getCgr_expiry_date() 
		 			&& CpdUtils.converCpdTimeToMilliseconds(periodRecord.getEndTime()) >  CpdUtils.converCpdTimeToMilliseconds(cpdGroupReg.getCgr_expiry_date())){
		 		recordEndDate = cpdGroupReg.getCgr_expiry_date();
		 	}else{
				 if(null == periodRecord.getEndTime()
					|| (null !=  periodRecord.getEndTime()
					&& CpdUtils.converCpdTimeToMilliseconds( periodRecord.getEndTime() )
					> CpdUtils.converCpdTimeToMilliseconds( currentPeriod.getEndTime() ))){
					 recordEndDate = currentPeriod.getEndTime();
				 }else{
					 recordEndDate = periodRecord.getEndTime();
				 }
		 		
		 	}
		 	//这里会有一段逻辑，去算记录其它值。构造CpdGroupRegHours记录
		 	CpdGroupRegHours record = new CpdGroupRegHours();
		 	record.setCgrh_cgr_period(periodRecord.getPeriod());
		 	DateUtil dateUtil = new DateUtil();
		 	
			//判断之前是否首次挂牌
			if(1!=cpdGroupReg.getCgr_first_ind()){//不是首次挂牌
				if(priod_index==1){
					record.setCgrh_cal_start_date(cpdGroupReg.getCgr_actual_date());
				}else{
					record.setCgrh_cal_start_date(periodRecord.getStartTime());
				}
					
			}else{
				//挂牌之前是否可以计入时数
				if(1==cpdType.getCt_cal_before_ind()){
					//设置为周期的开始时间
					record.setCgrh_cal_start_date(periodRecord.getStartTime());
				}else{
					//如果是首次挂牌且是循环周期内的第一个周期 
					if(priod_index==1){
						record.setCgrh_cal_start_date(cpdGroupReg.getCgr_actual_date());
					}else{
						record.setCgrh_cal_start_date(periodRecord.getStartTime());
					}
				}
			}
			//如果计算时间比当前循环周期的开始时间还早 设置计算时间为当前循环周期的开始时间
		 	 if(CpdUtils.converCpdTimeToMilliseconds(record.getCgrh_cal_start_date()) 
		 			 < CpdUtils.converCpdTimeToMilliseconds(periodRecord.getStartTime())){
		 		record.setCgrh_cal_start_date( periodRecord.getStartTime());
			 }
		 	 //设置计算结束时间
		 	 record.setCgrh_cal_end_date(recordEndDate);
		 	 //设置周期
		 	 record.setCgrh_cgr_period(periodRecord.getPeriod());

		 	 // 计算时数月份减开始时数月份+1	
		 	if(priod_index==1){
		 		//循环的第一个周期应该用实际开始时间来计算
		 		//record.setCgrh_cal_month(dateUtil.getMonths(cpdGroupReg.getCgr_actual_date() ,recordEndDate)+1);
		 		int cal_month = dateUtil.getMonths(cpdGroupReg.getCgr_actual_date() ,recordEndDate)+1;
                if(cal_month>12){
                   cal_month = dateUtil.getMonths(record.getCgrh_cal_start_date() ,recordEndDate)+1;
                }
                record.setCgrh_cal_month(cal_month);
		 	}else{
		 		record.setCgrh_cal_month(dateUtil.getMonths(recordStartDate ,recordEndDate)+1);
		 	}
		 	
		 	 //如果在当前周期内已除牌，要求时数应该为0
		 	 if((null != cpdGroupReg.getCgr_expiry_date() && CpdUtils.converCpdTimeToMilliseconds(cpdGroupReg.getCgr_expiry_date())
		 			 < CpdUtils.converCpdTimeToMilliseconds(periodRecord.getEndTime()))
		 	     || CpdUtils.converCpdTimeToMilliseconds(periodRecord.getEndTime())<CpdUtils.converCpdTimeToMilliseconds(new Date())
		 	     ){
		 	 	record.setCgrh_req_core_hours(0f);
				record.setCgrh_req_non_core_hours(0f);
				record.setCgrh_execute_core_hours(0f);
				record.setCgrh_execute_non_core_hours(0f);
		 	 }else{
		 	 	//通过计算出来的月数取到相应的时数
		 		//获得对应月份的时数
		 		CpdGroupHours reqHours = null;
		 		if(null!=req_lst){
			 		for(CpdGroupHours hour : req_lst){
			 			if(hour.getCgh_declare_month()==record.getCgrh_cal_month()){
			 				reqHours = hour;
			 			}
			 		}
		 		}

		 		if(null!=reqHours){
			 	 	record.setCgrh_req_core_hours(reqHours.getCgh_core_hours());
					record.setCgrh_req_non_core_hours(reqHours.getCgh_non_core_hours());
					record.setCgrh_execute_core_hours(reqHours.getCgh_core_hours());
					record.setCgrh_execute_non_core_hours(reqHours.getCgh_non_core_hours());
		 		}else{
			 	 	record.setCgrh_req_core_hours(0f);
					record.setCgrh_req_non_core_hours(0f);
					record.setCgrh_execute_core_hours(0f);
					record.setCgrh_execute_non_core_hours(0f);
		 		}
		 	 }

		 	//设置为非人工改过
		 	record.setCgrh_manul_ind(0);
		 	record.setCgrh_cr_id(cpdGroupReg.getCgr_cr_id());
		 	record.setCgrh_cgr_id(cpdGroupReg.getCgr_id());
		 	record.setCgrh_usr_ent_id(cpdGroupReg.getCgr_usr_ent_id());
		 	record.setCgrh_create_datetime(new Date());
		 	record.setCgrh_create_usr_ent_id(opt_usr_ent_id);
		 	record.setCgrh_cgp_id(reqPeriodId);
		 	record.setCgrh_update_datetime(new Date());
		 	record.setCgrh_update_usr_ent_id(opt_usr_ent_id);
		 	if(null == record.getCgrh_req_non_core_hours()){
		 		record.setCgrh_req_non_core_hours((float) 0);
		 	}
		 	cpdGroupRegHoursMapper.insert(record);
		 	calendar.setTime(periodRecord.getStartTime());
		 	 //加上一年
		 	calendar.add(Calendar.YEAR, 1);
		 	calStartDate =  calendar.getTime();
		 }
	}
	
	/**
	 * 重算一个人在当前课程中所有已完在学习记录获得CPD时数
	 * @param usr_ent_id
	 * @param itm_id
	 * @param opt_usr_ent_id
	 * @throws SQLException
	 */
	public void reCalUserAward(long usr_ent_id ,long itm_id,Long opt_usr_ent_id) throws SQLException{
		
		Map param = new HashMap();
		param.put("itm_id", itm_id);
		param.put("usr_ent_id", usr_ent_id);
		//先删除这人在这个课程下所有获得CPD时数的记录。
		cpdLrnAwardRecordMapper.deleteCpdLrnAwardRecord(param);
		
		//搜索这个人在这个课程下所有“已完成”状态的学习记录，以完成时间排序。最先完成的排在前面。 然后每个记录算一次。
		param.clear();
		param.put("itmId", itm_id);
		param.put("userEntId", usr_ent_id);
		param.put("covStatus", CourseEvaluation.Completed);
		List<CourseEvaluation> courseEvaluationList = courseEvaluationMapper.getCourseEvaluationByApp(param);
		AeItem aeItem = aeItemMapper.get(itm_id);
		param.clear();
		param.put("itm_id", itm_id);
		List<AeItemCPDItem> cpdItemList = aeItemCPDItemMapper.getAeItemCPDItem(param);
		Date hours_end_date = null;
		if(null!=cpdItemList && cpdItemList.size()>0){
			hours_end_date = cpdItemList.get(0).getAci_hours_end_date();
		}
		for(CourseEvaluation ce : courseEvaluationList ){
			calAwardHours(aeItem.getItm_id(),aeItem.getItm_ref_ind(), aeItem.getItm_type(),ce.getAeApplication().getApp_id(),ce.getCov_ent_id(),hours_end_date,ce.getCov_total_time().floatValue(),
					ce.getAtt().getAtt_timestamp(),CpdUtils.AWARD_HOURS_ACTION_RECAL_AW,opt_usr_ent_id,null );
		}
	} 
	
	/**
	 * 重算所有人在当前课程中所有已完在学习记录获得CPD时数
	 * @param itm_id
	 * @param opt_usr_ent_id
	 * @throws SQLException
	 */
	public void reCalAllAward(long itm_id,Long opt_usr_ent_id) throws SQLException{
		//Map param = new HashMap();
		//param.put("itmId", itm_id);
		List<AeApplication> records = aeApplicationService.getCItem(itm_id);
		//List<CpdLrnAwardRecord> records = cpdLrnAwardRecordMapper.findCpdAwardUser(param);
		if(null!=records && records.size()>0){
			for(AeApplication record : records){
				reCalUserAward(record.getApp_ent_id(),itm_id,opt_usr_ent_id);
			}
		}
	}
	
	
	/*
	 * 管理员修改完成准则及计分项目时，要求重算所有学习记录的完成状态。
	        如果学习记录的状态从其它状态变为“已完成”，或从“已完成”变为其它状态。
	        如果选择同时更新完成时间，那些原来获得得CPD时数的时间也要同时修改
	 */
	public void calOtherAward(long usr_ent_id ,long itm_id,Long opt_usr_ent_id ,Connection con) throws SQLException{
		//搜索这个人在这个课程下所有“已完成”状态的学习记录，以完成时间排序。最先完成的排在前面。 然后每个记录算一次。
		Map param = new HashMap();
		param.clear();
		param.put("itmId", itm_id);
		param.put("userEntId", usr_ent_id);
		param.put("covStatus", CourseEvaluation.Completed);
		List<CourseEvaluation> courseEvaluationList = courseEvaluationMapper.getCourseEvaluationByApp(param);
		AeItem aeItem = aeItemMapper.get(itm_id);
		param.put("itm_id", itm_id);
		List<AeItemCPDItem> cpdItemList = aeItemCPDItemMapper.getAeItemCPDItem(param);
		Date hours_end_date = null;
		if(null!=cpdItemList && cpdItemList.size()>0){
			hours_end_date = cpdItemList.get(0).getAci_hours_end_date();
		}
		for(CourseEvaluation ce : courseEvaluationList ){
			calAwardHoursForOld(aeItem.getItm_id(),aeItem.getItm_ref_ind(),aeItem.getItm_type(),ce.getAeApplication().getApp_id(),ce.getAeApplication().getApp_ent_id(),hours_end_date,ce.getCov_total_time().floatValue(),
					ce.getAtt().getAtt_timestamp(),CpdUtils.AWARD_HOURS_ACTION_RECAL_AW,opt_usr_ent_id,null );
		}
	}
	
	//获取在该评估周期内的每人在该课程已获取的时数记录
	public List<CpdLrnAwardRecord> getPeriodOnceList(Date att_timestamp,Long ctId ,List<CpdLrnAwardRecord> records,Connection con) throws SQLException{
		List<CpdLrnAwardRecord> returnRecords = new ArrayList<CpdLrnAwardRecord>();
		//获取att_timestamp所在周期
		CpdPeriodVO period = null;
		if(null!=con){
			 period = getPeriod(att_timestamp,ctId,con);
		}else{
			 period = getPeriod(att_timestamp,ctId);
		}
		
		Long periodInitialDateSeconds = CpdUtils.converCpdTimeToMilliseconds(period.getStartTime());
		Long periodExpiryDateSeconds = CpdUtils.converCpdTimeToMilliseconds(period.getEndTime());
		//看看在att_timestamp所在周期内是否有记录
		if(null!=records && records.size()>0){
			//当前报名记录有相同的APPID就更新没有就返回
			for(CpdLrnAwardRecord aRecord : records){
				Long awardTime = CpdUtils.converCpdTimeToMilliseconds(aRecord.getClar_award_datetime());
				if( periodInitialDateSeconds <= awardTime && awardTime <=periodExpiryDateSeconds){
					returnRecords.add(aRecord);
				}
			}
		}
		return returnRecords;
	}
	
	/**
	 *  计算获得时数
	 * @param itm_id 课程ID
	 * @param itm_ref_ind 课程是否公开课
	 * @param itm_type 课程类型
	 * @param app_id 报名记录ID
	 * @param app_ent_id 报名用户id
	 * @param hours_end_date 能获得时数的截止日期
	 * @param cov_total_time 学习时长
	 * @param att_timestamp 课程结束时间
	 * @param action
	 * 		LRN_AW:	     学员学习时获得得CPD时数计算
	 * 	    RECAL_AW：重算每个人在当前课程中所有已完在学习记录获得CPD时数
	 *      OTHER_AW: 管理员修改完成准则及计分项目时，要求重算所有学习记录的完成状态。
	 * 				      如果学习记录的状态从其它状态变为“已完成”，或从“已完成”变为其它状态。
	 * 				      如果选择同时更新完成时间，那些原来获得得CPD时数的时间也要同时修改
	 * @param opt_usr_ent_id 修改操作人
	 * @param con 用于旧代码传过来的Connection
	 * @throws SQLException
	 */
	private void calAwardHours(Long itm_id ,Long itm_ref_ind ,String itm_type ,Long app_id ,Long app_ent_id ,Date hours_end_date ,
			Float cov_total_time, Date att_timestamp , String action ,Long opt_usr_ent_id , Connection con) throws SQLException{
		 
		//如果学习时长小于30分钟不计算
		 if(AeItem.SELFSTUDY.equals(itm_type) ){
			if(null==cov_total_time || (cov_total_time<CpdUtils.AWARD_HOUR_TIME_CELL*60f)){
				return;
			}
		 }
		
		//获取课程相关的牌照
		List<AeItemCPDGourpItem> aeItemCPDGourpItemList = null;
		Map param = new HashMap<String,Object>();
		if(null==con){
			param.put("itm_id", itm_id);
			aeItemCPDGourpItemList = aeItemCPDGourpItemMapper.getAeItemCPDGourpItem(param);
		}else{
			aeItemCPDGourpItemList = CpdDbUtilsForOld.getAeItemCPDGourpItem(itm_id,null,con);
		}

		
		Long tmpCptTypeId = 0l;
		CpdType tmpCpdType = null;
		CpdGroup cpdGroup = null;
		//如果课程完成时间大于能获得时数的截止日期，返回不能获得时数
		if(null!=hours_end_date && 
				CpdUtils.converCpdTimeToMilliseconds(att_timestamp)>CpdUtils.converCpdTimeToMilliseconds(hours_end_date)){
			return;
		}
		
		for(AeItemCPDGourpItem aeCpdItem : aeItemCPDGourpItemList){
			//获取小牌信息
			if(null==con){
				cpdGroup =  cpdGroupMapper.get(aeCpdItem.getAcgi_cg_id());
			}else{
				cpdGroup =  CpdDbUtilsForOld.getCpdGroupById(aeCpdItem.getAcgi_cg_id(), con);
			}
			//有可能注册的小牌已经被删除
			if(null==cpdGroup){
				continue;
			}
			
			//获取大牌信息
			if(tmpCptTypeId!=cpdGroup.getCg_ct_id()){
				tmpCptTypeId = cpdGroup.getCg_ct_id();
				if(null==con){
					tmpCpdType = cpdTypeMapper.get(tmpCptTypeId);
				}else{
					tmpCpdType = CpdDbUtilsForOld.getCpdTypeById(tmpCptTypeId,con);
				}
			}

			param.clear();
			if(null==con){
				param.put("clar_usr_ent_id", app_ent_id);
				param.put("clar_itm_id", itm_id);
				param.put("clar_cg_id", aeCpdItem.getAcgi_cg_id());
			}
			//获取每人在该课程已获取的时数记录
			List<CpdLrnAwardRecord> records =  null;
			
			//对每个牌照计算时数，如果有的，且没有手动改过，就更新。没有就插入
	 		//计算出获得的核心时数
	 		float awardCoreHours = calAward( itm_id , itm_ref_ind , itm_type,aeCpdItem.getAcgi_award_core_hours(),cov_total_time);
	 		float awardNonCoreHours = 0f;
	 		//如果小牌设置为包含非核心时数
	 		if(1==cpdGroup.getCg_contain_non_core_ind()){
	 			//计算出获得的非核心时数
	 			awardNonCoreHours = calAward( itm_id , itm_ref_ind , itm_type,aeCpdItem.getAcgi_award_non_core_hours(),cov_total_time);
	 		}
	 		
	 		//获取每人在该课程已获取的时数记录
	 		if(null==con){
	 			records =  cpdLrnAwardRecordMapper.getCpdLrnAwardRecord(param);
	 		}else{
	 			records = CpdDbUtilsForOld.getCpdLrnAwardRecord(app_ent_id, itm_id, aeCpdItem.getAcgi_cg_id(), con);
	 		}
	 		
			 //每人每门课程只可获取一次时数
	 		if(null!=tmpCpdType &&  CpdType.AWARD_HOURS_TYPE_PERIOD_ONCE==tmpCpdType.getCt_award_hours_type()){ //每门课程在该评估周期内可获取一次时数
				//获取在该评估周期内的每人在该课程已获取的时数记录
				records = getPeriodOnceList(att_timestamp,tmpCpdType.getCt_id(),records,con);
			}
			
			//学员学习时获得CPD时数计算
			if(CpdUtils.AWARD_HOURS_ACTION_LRN_AW.equalsIgnoreCase(action)){
				//想要更新的获得课程时数
				CpdLrnAwardRecord record  = null;
				//查看之前是否有记录
				if(null!=records && records.size()>0){
					//查看每人在该课程已获取的时数记录里面有没与传入的appid相同的
					for(CpdLrnAwardRecord arecord : records){
						if(arecord.getClar_app_id()==app_id){
							record = arecord;
						}
					}
					//如果有相同appid的获得时数记录，更新
					if(null!=record){
			  	 		if(1!=record.getClar_manul_ind()){
			  	 			record.setClar_update_usr_ent_id(opt_usr_ent_id);
			  	 			record.setClar_update_datetime(new Date());
			  	 			record.setClar_award_core_hours(awardCoreHours);
			  	 			record.setClar_award_non_core_hours(awardNonCoreHours);
			  	 			if(null==con){
			  	 				cpdLrnAwardRecordMapper.update(record);
			  	 			}else{
			  	 				CpdDbUtilsForOld.updateCpdLrnAwardRecord(record, con);
			  	 			}
			  	 		}
			  	 		continue;
			  	 	}

				}
				//没有记录就插入
				if(awardCoreHours>0f || ( cpdGroup.getCg_contain_non_core_ind()==1 && awardNonCoreHours>0f) ){
					record  = new CpdLrnAwardRecord(app_ent_id , itm_id ,app_id , 0 ,
			  	 				tmpCpdType.getCt_id() , cpdGroup.getCg_id(),aeCpdItem.getAcgi_id(),awardCoreHours ,awardNonCoreHours 
			  	 				,att_timestamp ,opt_usr_ent_id,opt_usr_ent_id);
			 		//如果是每门课程完成就可以获得，插入
			 		if(null!=tmpCpdType && CpdType.AWARD_HOURS_TYPE_FINAL==tmpCpdType.getCt_award_hours_type()){
						if(null==con){
							cpdLrnAwardRecordMapper.insert(record);
						}else{
							CpdDbUtilsForOld.insertCpdLrnAwardRecord(record, con);
						}
			 		}else{
			 			//如果之前已经有记录了，就不插入
			 			if(null!= records && records.size()>0){
			 				continue;
			 			}else{
							if(null==con){
								cpdLrnAwardRecordMapper.insert(record);
							}else{
								CpdDbUtilsForOld.insertCpdLrnAwardRecord(record, con);
							}
			 			}
			 		}
				}	
			}else if(CpdUtils.AWARD_HOURS_ACTION_RECAL_AW.equalsIgnoreCase(action)){
				if(awardCoreHours>0f || ( cpdGroup.getCg_contain_non_core_ind()==1 && awardNonCoreHours>0f) ){
			 		CpdLrnAwardRecord record  = new CpdLrnAwardRecord(app_ent_id , itm_id ,app_id , 0 ,
		  	 				tmpCpdType.getCt_id() , cpdGroup.getCg_id(),aeCpdItem.getAcgi_id(),awardCoreHours ,awardNonCoreHours 
		  	 				,att_timestamp ,opt_usr_ent_id,opt_usr_ent_id);
			 		//如果是每门课程完成就可以获得，插入
			 		if(null!=tmpCpdType && CpdType.AWARD_HOURS_TYPE_FINAL==tmpCpdType.getCt_award_hours_type()){
						if(null==con){
							cpdLrnAwardRecordMapper.insert(record);
						}else{
							CpdDbUtilsForOld.insertCpdLrnAwardRecord(record, con);
						}
			 		}else{
			 			//如果之前已经有记录了，就不插入
			 			if(null!= records && records.size()>0){
			 				continue;
			 			}else{
							if(null==con){
								cpdLrnAwardRecordMapper.insert(record);
							}else{
								CpdDbUtilsForOld.insertCpdLrnAwardRecord(record, con);
							}
			 			}
			 		}
				}
			}else if(CpdUtils.AWARD_HOURS_ACTION_OTHER_AW.equalsIgnoreCase(action)){
				if(awardCoreHours>0f || ( cpdGroup.getCg_contain_non_core_ind()==1 && awardNonCoreHours>0f) ){
					CpdLrnAwardRecord record  = new CpdLrnAwardRecord(app_ent_id ,itm_id ,app_id , 0 ,
		  	 				tmpCpdType.getCt_id() , cpdGroup.getCg_id(),aeCpdItem.getAcgi_id(),awardCoreHours ,awardNonCoreHours 
		  	 				,att_timestamp ,opt_usr_ent_id,opt_usr_ent_id);
			 		//如果是每门课程完成就可以获得，插入
			 		if(null!=tmpCpdType && CpdType.AWARD_HOURS_TYPE_FINAL==tmpCpdType.getCt_award_hours_type()){
						if(null==con){
							cpdLrnAwardRecordMapper.insert(record);
						}else{
							CpdDbUtilsForOld.insertCpdLrnAwardRecord(record, con);
						}
			 		}else{
			 			//如果之前已经有记录了，就不插入
			 			if(null!= records && records.size()>0){
			 				continue;
			 			}else{
							if(null==con){
								cpdLrnAwardRecordMapper.insert(record);
							}else{
								CpdDbUtilsForOld.insertCpdLrnAwardRecord(record, con);
							}
			 			}
			 		}
				}

			}
		}
	}
	
	/**
	 * 计算出获得的时数(核心 或者 非核心)
	 * @param aeItem
	 * @param itmAwardHours
	 * @param cov_total_time
	 * @return
	 */
	private Float calAward( Long itm_id ,long itm_ref_ind ,String itm_type ,float itmAwardHours, float cov_total_time){
		//公开课不能获取时数
		if(1l ==itm_ref_ind){
			return 0f;
		}
		float award_hours = 0f;
		if(AeItem.INTEGRATED.equals(itm_type)){ //项目式培训不能获取时数
			return 0f;
		}else if(AeItem.SELFSTUDY.equals(itm_type)){ //网上课程/考试
			//计算出用了多少分钟
			float total_min = cov_total_time/60f;
			//按30分钟获得0.5时数算 不到30分钟就不获得
			Float totalAwardPoint = total_min/CpdUtils.AWARD_HOUR_TIME_CELL;
			award_hours = (totalAwardPoint.intValue())*CpdUtils.AWARD_HOUR_PER_CELL;
			//保留2位小数
			award_hours = FormatUtil.getInstance().scaleFloat(award_hours, 2,  BigDecimal.ROUND_DOWN);
		}else if(AeItem.CLASSROOM.equals(itm_type)){
			//如果是离线课程/考试 获得课程设置小牌的最大时数
			award_hours = itmAwardHours;
		}
		//如果获得时数大于课程设置小牌的最大时数
		if(Float.compare(award_hours, itmAwardHours)>0){
			award_hours = itmAwardHours;
		}
		return award_hours;
	}
	
	/**
	 * 计算获得时数 供springMVC用
	 * @param itm_id
	 * @param itm_ref_ind
	 * @param itm_type
	 * @param app_id
	 * @param app_ent_id
	 * @param hours_end_date
	 * @param cov_total_time
	 * @param att_timestamp
	 * @param action
	 * @param opt_usr_ent_id
	 * @throws SQLException
	 */
	public void calAwardHours(Long itm_id ,Long itm_ref_ind , String itm_type , Long app_id,Long app_ent_id,Date hours_end_date ,
			Float cov_total_time, Date att_timestamp , String action ,Long opt_usr_ent_id) throws SQLException{
		calAwardHours( itm_id , itm_ref_ind ,itm_type ,app_id , app_ent_id,hours_end_date,cov_total_time,att_timestamp,action,opt_usr_ent_id,null);
	}
	
	/**
	 * 计算获得时数 供旧代码用
	 * @param itm_id
	 * @param itm_ref_ind
	 * @param itm_type
	 * @param app_id
	 * @param app_ent_id
	 * @param hours_end_date
	 * @param cov_total_time
	 * @param att_timestamp
	 * @param action
	 * @param opt_usr_ent_id
	 * @param con
	 * @throws SQLException
	 */
	public void calAwardHoursForOld(Long itm_id ,Long itm_ref_ind  ,String itm_type ,Long app_id,Long app_ent_id ,Date hours_end_date ,
			Float cov_total_time, Date att_timestamp , String action ,Long opt_usr_ent_id , Connection con) throws SQLException{
		calAwardHours( itm_id , itm_ref_ind ,itm_type ,app_id , app_ent_id,hours_end_date,cov_total_time,att_timestamp,action,opt_usr_ent_id,con);
	}
	
	/**
	 * 	供旧代码用
	 * 管理员修改完成准则及计分项目时，要求重算所有学习记录的完成状态。
	 * 如果学习记录的状态从其它状态变为“已完成”，或从“已完成”变为其它状态。
	 * 如果选择同时更新完成时间，那些原来获得得CPD时数的时间也要同时修改
	 * @param usr_ent_id
	 * @param itm_id
	 * @param opt_usr_ent_id
	 * @param con
	 * @throws SQLException
	 */
	public void calOtherAwardForOld(long usr_ent_id ,long itm_id,Long opt_usr_ent_id ,Connection con) throws SQLException{
		//搜索这个人在这个课程下所有“已完成”状态的学习记录，以完成时间排序。最先完成的排在前面。 然后每个记录算一次。
		List<CourseEvaluation> courseEvaluationList = CpdDbUtilsForOld.getCourseEvaluationByApp(itm_id, usr_ent_id, CourseEvaluation.Completed, con);
		AeItem aeItem = CpdDbUtilsForOld.getAeItem(itm_id, con);
		List<AeItemCPDItem> cpdItemList = CpdDbUtilsForOld.getAeItemCPDItem(itm_id, con);
		Date hours_end_date = null;
		if(null!=cpdItemList && cpdItemList.size()>0){
			hours_end_date = cpdItemList.get(0).getAci_hours_end_date();
		}
		for(CourseEvaluation ce : courseEvaluationList ){
			calAwardHoursForOld(aeItem.getItm_id(),aeItem.getItm_ref_ind(),aeItem.getItm_type(),ce.getAeApplication().getApp_id(),ce.getAeApplication().getApp_ent_id(),hours_end_date,ce.getCov_total_time().floatValue(),
					ce.getAtt().getAtt_timestamp(),CpdUtils.AWARD_HOURS_ACTION_RECAL_AW,opt_usr_ent_id,con );
		}
	}
	
	/**
	 * 获取报名备注信息
	 * @param period 要获取的周期
	 * @param code   报表的唯一标识 (CpdReportRemark类里面有定义)
	 * @return remark
	 */
	public String getRemarkByPeriod(int period,String code){
		String remark = null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int curYear = cal.get(Calendar.YEAR);
		//if(curYear == period){
			CpdReportRemark  cpdReportRemark = cpdReportRemarkService.getCpdReportRemarkByCode(code);
			if(null!=cpdReportRemark){
				remark = cpdReportRemark.getCrpm_report_remark();
			}
			//修改为 只取现在保存的remark
		/*}else{
			List<CpdReportRemarkHistory> histList =cpdReportRemarkService.getCpdReportRemarkHistory(code, period, null);
			if(null!=histList && histList.size()>0){
				CpdReportRemarkHistory hist = histList.get(0);
				remark =hist.getCrmh_report_remark();
			}
		}*/
		return remark;
	}    
	
	/**
	 * 获取cpdGroupRegHoursHistory中可选择的年份
	 * cpdGroupRegHoursHistory表中保存的不包括当前年份的数据，故要加上当前年份
	 * @return
	 */
	public List<Integer> getReportYear(){
		List<Integer> list = new ArrayList<Integer>();
		list = cpdGroupRegHoursHistoryMapper.getCpdGroupRegHoursHistoryYear();
		int nowYear = DateUtil.getInstance().getDateYear(DateUtil.getInstance().getDate());	
		boolean check = true;
		for(int i = 0; i < list.size(); i++){
			if(list.get(i) == nowYear){
				check = false;
			}
		}
		if(check){
			list.add(0, nowYear);
		}
		
		return list;
	}
	
	/**
	 * 通过指定的周期获得可选择的牌照
	 * @param prof
	 * @param period 周期
	 * @return
	 */
	public List<CpdType> getReportType(loginProfile prof,int period){
		CpdType ct = new CpdType();
		ct.setCt_status(CpdUtils.STATUS_OK);
		List<CpdType> cpdType =  cpdTypeMapper.getCpdType(ct);
		for(int i = 0;i<cpdType.size();i++){
             CpdPeriodVO cpdPeriodVO = getPeriodByYear(prof,period,cpdType.get(i).getCt_id()); 
             cpdType.get(i).setCpdPeriodVO(cpdPeriodVO);
         }
		return cpdType;
	}
	
	/**
	 * 通过当前时间获得可选择的牌照
	 * @param prof
	 * @return
	 */
	public List<CpdType> getReportTypeByNowDate(loginProfile prof){
		CpdType ct = new CpdType();
		ct.setCt_status(CpdUtils.STATUS_OK);
		List<CpdType> cpdType =  cpdTypeMapper.getCpdType(ct);
		for(int i = 0;i<cpdType.size();i++){
			CpdPeriodVO cpdPeriodVOs = getPeriod(new Date(),cpdType.get(i).getCt_id());
             CpdPeriodVO cpdPeriodVO = getPeriodByYear(prof,cpdPeriodVOs.getPeriod(),cpdType.get(i).getCt_id()); 
             cpdType.get(i).setCpdPeriodVO(cpdPeriodVO);
         }
		return cpdType;
	}
	
	//getPeriod
	
	/**
	 * 获取所有注册历史记录的年份
	 * @return
	 */
    public List<CpdGroupRegHoursHistory>   getCpdGroupRegHoursHistoryPeriod(){
        Map<String, Object> param = new HashMap<String,Object>();
        return cpdGroupRegHoursHistoryMapper.getCpdGroupRegHoursHistoryPeriod(param);
    }	
    
}
