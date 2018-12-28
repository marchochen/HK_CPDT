package com.cwn.wizbank.systemLog.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cw.wizbank.qdb.dbEntityRelation;
import com.cw.wizbank.util.cwSQL;
import com.cwn.wizbank.entity.GourpLoginReport;
import com.cwn.wizbank.entity.GradeLoginReport;
import com.cwn.wizbank.entity.LoginLog;
import com.cwn.wizbank.entity.PositionLoginReport;
import com.cwn.wizbank.persistence.GourpLoginReportMapper;
import com.cwn.wizbank.persistence.GradeLoginReportMapper;
import com.cwn.wizbank.persistence.LoginActionLogMapper;
import com.cwn.wizbank.persistence.PositionLoginReportMapper;
import com.cwn.wizbank.utils.DateUtil;

@Service
public class LoginActionLogService extends SystemActionLogService<LoginLog>{

	@Autowired
	LoginActionLogMapper  loginActionLogMapper;
	
	@Autowired
	GourpLoginReportMapper gourpLoginReportMapper;
	
	@Autowired
	GradeLoginReportMapper gradeLoginReportMapper;
	
	@Autowired
	PositionLoginReportMapper  positionLoginReportMapper;
	
	@Override
	void init() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 保存登录日志数据
	 */
	public void saveLog(LoginLog loginLog) {
		int isExist = existTable(getTableName(getDate()));
		if(null == loginLog.getLoginTime()){
			loginLog.setLoginTime(getDate());
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(loginLog.getLoginTime());
		Map<String ,Object> map = new HashMap<String, Object>();
		
		map.put("tableName", getTableName(getDate()));
		map.put("entId", loginLog.getEntId());
		map.put("usrDisplayBil", loginLog.getUsrDisplayBil());
		map.put("usrFullNameBil", loginLog.getUsrFullNameBil());
		map.put("loginMode", loginLog.getLoginMode());
		map.put("loginIP", loginLog.getLoginIP());
		map.put("loginFailureCode",loginLog.getLogin_failure_code());
		//针对oracle数据库时间类型处理
		if(cwSQL.DBVENDOR_ORACLE.equalsIgnoreCase(cwSQL.getDbType())){
			String time = loginLog.getLoginTime().toString();
			if(time.indexOf('.')>0){
				time = time.substring(0,time.indexOf('.'));
			}
		map.put("loginTime", time);	
		}else{
		map.put("loginTime", loginLog.getLoginTime());	
		}
		map.put("month", cal.get(Calendar.MONTH)+1);
		map.put("year", cal.get(Calendar.YEAR));
		map.put("usrLoginStatus", loginLog.getUsrLoginStatus());
		if(isExist > 0)
		{
			loginActionLogMapper.saveLog(map);
		}else{
			createLoginLogTableByDate();
			loginActionLogMapper.saveLog(map);
		}
		//测试创建表
		/*Map<String ,Object> map = new HashMap<String, Object>();
		map.put("tableName", "login9999");
		loginActionLogMapper.createLoginLogTableByDate(map);*/
		//测试生成报表数据
		/*saveGourpLoginReport();
		savePositionLoginReport();
		saveGradeLoginReport();*/
		//测试删除表
		/*Map<String ,Object> map = new HashMap<String, Object>();
		map.put("tableName", "login9999");
		loginActionLogMapper.delOneYear(map);*/
	}
	
	/**
	 * 查询表是否存在
	 * @param date
	 * @return
	 */
	public int existTable(String tableName){
		int isExist = loginActionLogMapper.existTable(tableName);
		return isExist;
	}
	
	/**
	 * 删除一年前的数据
	 * @param tableName
	 */
	public void delOneYear()
	{
		Calendar date=Calendar.getInstance();
		String tableName = "loginLog"+(date.get(Calendar.YEAR)-1) +""+(date.get(Calendar.MONTH)+1);
		Map<String ,Object> map = new HashMap<String, Object>();
		map.put("tableName", tableName);
		loginActionLogMapper.delOneYear(map);
	}
	
	/**
	 * 通过表名 创建loginLogXXXXXX数据表 
	 * @param tableName
	 */
	public void createLoginLogTableByDate()
	{
		String tableName = getTableName(getDate());
		int isExistTable = loginActionLogMapper.existTable(tableName);
		if(isExistTable == 0){
			Map<String ,Object> map = new HashMap<String, Object>();
			map.put("tableName", tableName);
			loginActionLogMapper.createLoginLogTableByDate(map);	
		}
	}
	
	/**
	 * 按用户组保存登陆日志数据
	 * @param gourpLoginReport
	 */
	public void saveGourpLoginReport()
	{
		String tableName = getTableName(getDate());
		if(existTable(tableName) > 0){   //在跨月时，平台启动可能未执行到生成对应月份日志表的操作。
			Map<String ,Object> map = new HashMap<String, Object>();
			map.put("tableName", tableName);
			map.put("ernType", dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
			List<LoginLog> list = loginActionLogMapper.selectByGrpIdOrUgrId(map);
			for(int i=0;i<list.size();i++)
			{
				GourpLoginReport gourpLoginReport = new GourpLoginReport();
				gourpLoginReport.setGplrLastUpdateDate(getDate());
				gourpLoginReport.setGplrLoginMode(list.get(i).getLoginMode());
				gourpLoginReport.setGplrTotleLoginNumber(list.get(i).getTotleLoginNumber());
				gourpLoginReport.setGplrGrpId(list.get(i).getGrpId());
				gourpLoginReport.setGplrYear(list.get(i).getYear());
				gourpLoginReport.setGplrMonth(list.get(i).getMonth());
				insertGourpLoginReport(gourpLoginReport);
			}
			
		}
	}
	
	/**
	 * 按职级保存登陆日志数据
	 * @param gourpLoginReport
	 */
	public void saveGradeLoginReport()
	{
		String tableName = getTableName(getDate());
		if(existTable(tableName) > 0){   //在跨月时，平台启动可能未执行到生成对应月份日志表的操作。
			Map<String ,Object> map = new HashMap<String, Object>();
			map.put("tableName", tableName);
			map.put("ernType", dbEntityRelation.ERN_TYPE_USR_CURRENT_UGR);
			List<LoginLog> list = loginActionLogMapper.selectByGrpIdOrUgrId(map);
			for(int i=0;i<list.size();i++)
			{
				GradeLoginReport gradeLoginReport = new GradeLoginReport();
				gradeLoginReport.setGdlrLastUpdateDate(getDate());
				gradeLoginReport.setGdlrLoginMode(list.get(i).getLoginMode());
				gradeLoginReport.setGdlrTotleLoginNumber(list.get(i).getTotleLoginNumber());
				gradeLoginReport.setGdlrUgrId(list.get(i).getGrpId());
				gradeLoginReport.setGdlrYear(list.get(i).getYear());
				gradeLoginReport.setGdlrMonth(list.get(i).getMonth());
				insertGradeLoginReport(gradeLoginReport);
			}
		}
	}
	
	/**
	 * 按岗位保存登陆日志数据
	 * @param positionLogin
	 */
	public void savePositionLoginReport(){
		String tableName = getTableName(getDate());
		if(existTable(tableName) > 0){   //在跨月时，平台启动可能未执行到生成对应月份日志表的操作。
			Map<String ,Object> map = new HashMap<String, Object>();
			map.put("tableName", tableName);
			List<LoginLog> list = loginActionLogMapper.selectByUptId(map);
			for(int i=0;i<list.size();i++)
			{
				PositionLoginReport positionLogin = new PositionLoginReport();
				positionLogin.setPslrLastUpdateDate(getDate());
				positionLogin.setPslrLoginMode(list.get(i).getLoginMode());
				positionLogin.setPslrTotleLoginNumber(list.get(i).getTotleLoginNumber());
				positionLogin.setPslrUptId(list.get(i).getUptId());
				positionLogin.setPslrYear(list.get(i).getYear());
				positionLogin.setPslrMonth(list.get(i).getMonth());
				insertPositionLoginReport(positionLogin);
			}
		}
	}
	
	/**
	 * 根据年月生成表名
	 * @return
	 */
	public String getTableName(Date date){
		String tableName = "loginLog"+DateUtil.getInstance().getDateYear(date) +""+DateUtil.getInstance().getDateMonth(date);
		return tableName;
	}

	/**
	 * 更新职级—登录报表数据
	 * @param gradeLoginReport
	 */
	public void insertGradeLoginReport(GradeLoginReport gradeLoginReport)
	{
		//检查是否存在相同记录
		  int checkNumber =	gradeLoginReportMapper.checkNumber(gradeLoginReport);
		  if(checkNumber > 0){
			  gradeLoginReportMapper.update(gradeLoginReport);
		  }else{
			  gradeLoginReportMapper.insert(gradeLoginReport);
		  }
	}
	
	/**
	 * 更新岗位—登录报表数据
	 * @param gradeLoginReport
	 */
	public void insertPositionLoginReport(PositionLoginReport positionLogin)
	{
		//检查是否存在相同记录
		  int checkNumber =	positionLoginReportMapper.checkNumber(positionLogin);
		  if(checkNumber > 0){
			  positionLoginReportMapper.update(positionLogin);
		  }else{
			  positionLoginReportMapper.insert(positionLogin);
		  }
	}
	
	/**
	 * 更新用户组—登录报表数据
	 * @param gradeLoginReport
	 */
	public void insertGourpLoginReport(GourpLoginReport gourpLoginReport)
	{
		  //检查是否存在相同记录
		  int checkNumber =	gourpLoginReportMapper.checkNumber(gourpLoginReport);
		  if(checkNumber > 0){
			  gourpLoginReportMapper.update(gourpLoginReport);
		  }else{
			  gourpLoginReportMapper.insert(gourpLoginReport);
		  }
	}
	
}
