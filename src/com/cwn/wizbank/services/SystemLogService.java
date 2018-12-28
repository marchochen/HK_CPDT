package com.cwn.wizbank.services;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.dbThresholdSynLog;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.util.LangLabel;
import com.cwn.wizbank.entity.LoginLog;
import com.cwn.wizbank.entity.ObjectActionLog;
import com.cwn.wizbank.entity.RegUser;
import com.cwn.wizbank.entity.SuperviseTargetEntity;
import com.cwn.wizbank.persistence.LoginActionLogMapper;
import com.cwn.wizbank.persistence.ObjectActionLogMapper;
import com.cwn.wizbank.systemLog.service.LoginActionLogService;
import com.cwn.wizbank.utils.CommonLog;
import com.cwn.wizbank.utils.DateUtil;
import com.cwn.wizbank.utils.LabelContent;

/**
 * service 实现
 */
@Service
public class SystemLogService  extends BaseService<LoginLog>{
	
	@Autowired
	LoginActionLogMapper  loginActionLogMapper;
	
	@Autowired
	LoginActionLogService loginActionLogService;
	
	@Autowired
	ObjectActionLogMapper objectActionLogMapper;
	/**
	 * 导出系统运行日志
	 * @param prof
	 * @param wizbini
	 * @param lastdays
	 * @param starttime
	 * @param endtime
	 * @param selectall
	 * @return
	 */
	public String  expor(loginProfile prof, WizbiniLoader wizbini,int lastdays,
			String starttime,String endtime,String type){
		
		// 创建一个webbook，对应一个Excel文件  
        HSSFWorkbook wb = new HSSFWorkbook();  
        // 在webbook中添加一个sheet,对应Excel文件中的sheet  
        HSSFSheet sheet = wb.createSheet("Sheet1");  
        // 在sheet中添加表头第0行
        HSSFRow row = sheet.createRow((int) 0);  
        // 创建单元格，并设置值表头 设置表头居中  
        HSSFCellStyle style = wb.createCellStyle();  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        sheet.createRow((int) 0).setHeight((short) 400);
        //边框颜色 黑色
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框    
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框    
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框    
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框 
       
        //条件
        Map<String,Object> map =new HashMap<String, Object>();
        //过去n天
        Date beforedate = null;
        SimpleDateFormat sdf_all=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  
        if(lastdays >= 0 && ((starttime==null ||starttime.equals(""))  && (endtime==null || endtime.equals("")))){
        	Calendar  cal=Calendar.getInstance();
        	try {
				cal.setTime(sdf_all.parse(sdf_all.format(new Date())));
			} catch (ParseException e) {
				CommonLog.error(e.getMessage(),e);
			}   
            cal.add(Calendar.DATE,-lastdays); 
            beforedate = cal.getTime();
            map.put("beforedate","'"+DateUtil.getInstance().formateDate(beforedate)+"'");
		}else{
			if(endtime != null && !endtime.equals("")){
				map.put("endtime",  "'"+endtime+"'");
			}
			
			if(starttime != null && !starttime.equals("")){
				map.put("starttime",  "'"+starttime+"'");
			}
		}
       
        if(type.equalsIgnoreCase(dbThresholdSynLog.LAB_LOG_USER_LOGIN)){ 
        	//用户登录日志 
          
            //登录日志按月分表，需要按条件获取表名
        	List<String> tableNameList = getLoginLogTableNames(starttime,endtime,lastdays);//loginActionLogService.getTableName(getDate());
            map.put("tableNameList", tableNameList);
        	
        	//设置日期列宽度
        	sheet.setColumnWidth(3, 6000);
        	//获取列表头名称
            getCell(row, style,prof.cur_lan); 
        	
            //没有需要导出的日志表名时  不写入内容
            if(null != tableNameList && tableNameList.size() > 0){
            	List<Map> infoList =loginActionLogMapper.getUserLoginLog(map);
     	        for (int i = 0; i < infoList.size(); i++)  
     	        {  
     	            row = sheet.createRow((int) i + 1);  //创建单元格，并设置值  
     	            createAndSetCell(row,infoList.get(i),prof.cur_lan);
     	        }  
            }
            
        }else if(type.equalsIgnoreCase(dbThresholdSynLog.LAB_LOG_USER_OPERATION)){
        	
        	//获取列表头名称
        	getUserOperationLogCell(row, style,prof.cur_lan); 
        	//重要功能操作日志
        	List<Map> infoList =objectActionLogMapper.getObjectActionLog(map);
 	        for (int i = 0; i < infoList.size(); i++)  
 	        {  
 	            row = sheet.createRow((int) i + 1);  //创建单元格，并设置值  
 	            createAndSetUserOperationLogCell(row,infoList.get(i),prof.cur_lan);
 	        }
        }else if(type.equalsIgnoreCase(dbThresholdSynLog.LOG_TYPE_THRESHOLD)){
        	//获取列表头名称
        	//row = sheet.createRow((int) 2);
        	//设置日期列宽度
        	sheet.setColumnWidth(2, 6000);
        	
        	getPerfWarningLogCell(row, style,prof.cur_lan); 
        	//用量警告日志
        	List<Map> infoList =loginActionLogMapper.getPerfWarningLog(map);
 	        for (int i = 0; i < infoList.size(); i++)  
 	        {  
 	            row = sheet.createRow((int) i + 1);  //创建单元格，并设置值  
 	            createPerfWarningLogCell(row,infoList.get(i),prof.cur_lan);
 	        }
        }
        
        //生成文件名   并 将文件存到指定位置  
        String fileName = type + new SimpleDateFormat("yyyyMMddHHmmssSSS") .format(new Date())+".xls";
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

    public void getCell(HSSFRow row,HSSFCellStyle style,String labellang)
    {
    	 String[] labels = new String[] {
    			 LabelContent.get(labellang, "label_core_system_setting_161"), 
    			 LabelContent.get(labellang, "label_core_system_setting_162"),
    			 LabelContent.get(labellang, "label_core_system_setting_163"),
    			 LabelContent.get(labellang, "label_core_system_setting_164"),
    			 LabelContent.get(labellang, "label_core_system_setting_211"),
    			 LabelContent.get(labellang, "label_core_system_setting_212")
    	 };
    	 
    	 for(int i = 0; i<labels.length; i++){
    			 HSSFCell cell = row.createCell(i);  
		         cell.setCellValue(labels[i]);  
		         cell.setCellStyle(style);  
    	 }
    }

    public void createAndSetCell(HSSFRow row, Map map ,String labellang){
    	 row.createCell(0).setCellValue(map.get("usr_display_bil".toUpperCase()).toString());  
         row.createCell(1).setCellValue(checkIsNull(map.get("usr_full_name_bil".toUpperCase()))); 
         if(Integer.parseInt(map.get("login_mode".toUpperCase()).toString()) == LoginLog.MODE_PC){
        	 row.createCell(2).setCellValue(LabelContent.get(labellang, "label_core_system_setting_174"));
 		 }else if(Integer.parseInt(map.get("login_mode".toUpperCase()).toString()) == LoginLog.MODE_APP){
 			 row.createCell(2).setCellValue(LabelContent.get(labellang, "label_core_system_setting_175"));
 		 }else if(Integer.parseInt(map.get("login_mode".toUpperCase()).toString()) == LoginLog.MODE_WECHAT){
 			 row.createCell(2).setCellValue(LabelContent.get(labellang, "label_core_system_setting_176"));
 		 }
         row.createCell(3).setCellValue(map.get("login_time".toUpperCase()).toString());
         row.createCell(4).setCellValue(checkIsNull(map.get("LOGIN_IP".toUpperCase())));
         if(null == map.get("usr_login_status".toUpperCase())){
        	 row.createCell(5).setCellValue("--");
         }else{
	         if(map.get("usr_login_status".toUpperCase()).toString().equals(LoginLog.USR_LOGIN_STATUS_SUCCESS)){
	        	 row.createCell(5).setCellValue(LabelContent.get(labellang, "label_core_system_setting_213"));
	 		 }else if(map.get("usr_login_status".toUpperCase()).toString().equals(LoginLog.USR_LOGIN_STATUS_FAIL)){
	 			 row.createCell(5).setCellValue(LabelContent.get(labellang, "label_core_system_setting_214"));
	 		 }
         }
    }
    
    public void getUserOperationLogCell(HSSFRow row,HSSFCellStyle style,String labellang)
    {
    	 String[] labels = new String[] {
    			 LabelContent.get(labellang, "label_core_system_setting_165"), 
    			 LabelContent.get(labellang, "label_core_system_setting_166"),
    			 LabelContent.get(labellang, "label_core_system_setting_167"),
    			 LabelContent.get(labellang, "label_core_system_setting_168"), 
    			 LabelContent.get(labellang, "label_core_system_setting_169"),
    			 LabelContent.get(labellang, "label_core_system_setting_170"),
    			 LabelContent.get(labellang, "label_core_system_setting_171"), 
    			 LabelContent.get(labellang, "label_core_system_setting_172"),
    			 LabelContent.get(labellang, "label_core_system_setting_173"),
    			 LabelContent.get(labellang, "label_core_system_setting_210"),
				 LabelContent.get(labellang, "label_core_system_setting_211")
    	 };
    	 
    	 for(int i = 0; i<labels.length; i++){
    			 HSSFCell cell = row.createCell(i);  
		         cell.setCellValue(labels[i]);  
		         cell.setCellStyle(style);  
    	 }
    }
    
    public void createAndSetUserOperationLogCell(HSSFRow row, Map map ,String labellang){
    	row.createCell(0).setCellValue(checkIsNull(map.get("object_title".toUpperCase()))); 
    	row.createCell(1).setCellValue(checkIsNull(map.get("object_code".toUpperCase()))); 
    	row.createCell(2).setCellValue(getObjectType(labellang,map.get("object_type".toUpperCase()).toString())); 
        row.createCell(3).setCellValue(map.get("object_id".toUpperCase()).toString());
        row.createCell(4).setCellValue(getObjectAction(labellang,map.get("object_action".toUpperCase()).toString()));
        row.createCell(5).setCellValue(getObjectActionType(labellang,map.get("object_action_type".toUpperCase()).toString()));
        row.createCell(6).setCellValue(map.get("object_action_time".toUpperCase()).toString());
        row.createCell(7).setCellValue(map.get("usr_ste_usr_id".toUpperCase()).toString());
        row.createCell(8).setCellValue(map.get("usr_display_bil".toUpperCase()).toString());
        if(null!=map.get("object_opt_user_login_time".toUpperCase())){
        	row.createCell(9).setCellValue(map.get("object_opt_user_login_time".toUpperCase()).toString());
        }else{
        	row.createCell(9).setCellValue("--");
        }
        row.createCell(10).setCellValue(checkIsNull(map.get("object_opt_user_login_ip".toUpperCase())));
   }
    
    private String getObjectType(String lang , String val){
		if(StringUtils.isEmpty(val)){
			return null;
		}
		String type = null;
		
		if(ObjectActionLog.OBJECT_TYPE_USR.equalsIgnoreCase(val)){
			type = LabelContent.get(lang, "label_core_system_setting_177");
		}else if(ObjectActionLog.OBJECT_TYPE_GRP.equalsIgnoreCase(val)){
			type = LabelContent.get(lang, "label_core_system_setting_178");
		}else if(ObjectActionLog.OBJECT_TYPE_UPT.equalsIgnoreCase(val)){
			type = LabelContent.get(lang, "label_core_system_setting_179");
		}else if(ObjectActionLog.OBJECT_TYPE_UGR.equalsIgnoreCase(val)){
			type = LabelContent.get(lang, "label_core_system_setting_180");
		}else if(ObjectActionLog.OBJECT_TYPE_COS.equalsIgnoreCase(val)){
			type = LabelContent.get(lang, "label_core_system_setting_181");
		}else if(ObjectActionLog.OBJECT_TYPE_CLASS.equalsIgnoreCase(val)){
			type = LabelContent.get(lang, "label_core_system_setting_182");
		}else if(ObjectActionLog.OBJECT_TYPE_CC.equalsIgnoreCase(val)){
			type = LabelContent.get(lang, "label_core_system_setting_183");
		}else if(ObjectActionLog.OBJECT_TYPE_CREDITS.equalsIgnoreCase(val)){
			type = LabelContent.get(lang, "label_core_system_setting_184");
		}else if(ObjectActionLog.OBJECT_TYPE_KB.equalsIgnoreCase(val)){
			type = LabelContent.get(lang, "label_core_system_setting_185");
		}else if(ObjectActionLog.OBJECT_TYPE_EL.equalsIgnoreCase(val)){
			type = LabelContent.get(lang, "label_core_system_setting_186");
		}else if(ObjectActionLog.OBJECT_TYPE_VT.equalsIgnoreCase(val)){
			type = LabelContent.get(lang, "label_core_system_setting_187");
		}else if(ObjectActionLog.OBJECT_TYPE_AN.equalsIgnoreCase(val)){
			type = LabelContent.get(lang, "label_core_system_setting_188");
		}else if(ObjectActionLog.OBJECT_TYPE_INFO.equalsIgnoreCase(val)){
			type = LabelContent.get(lang, "label_core_system_setting_189");
		}else if(ObjectActionLog.OBJECT_TYPE_TC.equalsIgnoreCase(val)){
			type = LabelContent.get(lang, "label_core_system_setting_202");
		}else if(ObjectActionLog.OBJECT_TYPE_ONLINE_EXAM.equalsIgnoreCase(val)){
			type = LabelContent.get(lang, "label_core_system_setting_207");
		}else if(ObjectActionLog.OBJECT_TYPE_OFFLINE_EXAM.equalsIgnoreCase(val)){
			type = LabelContent.get(lang, "label_core_system_setting_208");
		}else if(ObjectActionLog.OBJECT_TYPE_CPD_TYPE.equalsIgnoreCase(val)){
            type = LabelContent.get(lang, "label_core_system_setting_222");//牌照类型
        }else if(ObjectActionLog.OBJECT_TYPE_CPD_GROUP.equalsIgnoreCase(val)){
            type = LabelContent.get(lang, "label_core_system_setting_223");//牌照组别
        }else if(ObjectActionLog.OBJECT_TYPE_CPD_GROUP_HOURS.equalsIgnoreCase(val)){
            type = LabelContent.get(lang, "label_core_system_setting_224");//时数要求
        }else if(ObjectActionLog.OBJECT_TYPE_CPD_REG.equalsIgnoreCase(val)){
            type = LabelContent.get(lang, "label_core_system_setting_225");//牌照注册信息
        }else if(ObjectActionLog.OBJECT_TYPE_CPD_COURSE_HOURS.equalsIgnoreCase(val)){
            type = LabelContent.get(lang, "label_core_system_setting_226");//CPT/D时数设置
        }
		return type;
	}
    
    private String getObjectAction(String lang , String val){
		if(StringUtils.isEmpty(val)){
			return null;
		}
		String action = null;
		
		if(ObjectActionLog.OBJECT_ACTION_ADD.equalsIgnoreCase(val)){
			action = LabelContent.get(lang, "label_core_system_setting_190");
		}else if(ObjectActionLog.OBJECT_ACTION_UPD.equalsIgnoreCase(val)){
			action = LabelContent.get(lang, "label_core_system_setting_191");
		}else if(ObjectActionLog.OBJECT_ACTION_ACTIVE.equalsIgnoreCase(val)){
			action = LabelContent.get(lang, "label_core_system_setting_192");
		}else if(ObjectActionLog.OBJECT_ACTION_DEL.equalsIgnoreCase(val)){
			action = LabelContent.get(lang, "label_core_system_setting_193");
		}else if(ObjectActionLog.OBJECT_ACTION_UPD_PWD.equalsIgnoreCase(val)){
			action = LabelContent.get(lang, "label_core_system_setting_194");
		}else if(ObjectActionLog.OBJECT_ACTION_PUB.equalsIgnoreCase(val)){
			action = LabelContent.get(lang, "label_core_system_setting_195");
		}else if(ObjectActionLog.OBJECT_ACTION_CANCLE_PUB.equalsIgnoreCase(val)){
			action = LabelContent.get(lang, "label_core_system_setting_196");
		}else if(ObjectActionLog.OBJECT_ACTION_APPR.equalsIgnoreCase(val)){
			action = LabelContent.get(lang, "label_core_system_setting_197");
		}else if(ObjectActionLog.OBJECT_ACTION_CANCEL_APPR.equalsIgnoreCase(val)){
			action = LabelContent.get(lang, "label_core_system_setting_198");
		}else if(ObjectActionLog.OBJECT_ACTION_RESTORE.equalsIgnoreCase(val)){
			action = LabelContent.get(lang, "label_core_system_setting_206");
		}else if(ObjectActionLog.OBJECT_ACTION_EXPIRE.equalsIgnoreCase(val)){
			action = LabelContent.get(lang, "label_core_system_setting_209");
		}
		return action;
	}
 
    private String getObjectActionType(String lang , String val){
		if(StringUtils.isEmpty(val)){
			return null;
		}
		String action = null;

		if(ObjectActionLog.OBJECT_ACTION_TYPE_WEB.equalsIgnoreCase(val)){
			action = LabelContent.get(lang, "label_core_system_setting_199");
		}else if(ObjectActionLog.OBJECT_ACTION_TYPE_BATCH.equalsIgnoreCase(val)){
			action = LabelContent.get(lang, "label_core_system_setting_200");
		}else if(ObjectActionLog.OBJECT_ACTION_TYPE_IMPORT.equalsIgnoreCase(val)){
			action = LabelContent.get(lang, "label_core_system_setting_201");
		}	

		return action;
	}

    public void getPerfWarningLogCell(HSSFRow row,HSSFCellStyle style,String labellang)
    {
    	 String[] labels = new String[] {
    			 LangLabel.getValue(labellang, "lab_user_id"), 
    			 LangLabel.getValue(labellang, "lab_threshold_log_type"),
    			 LangLabel.getValue(labellang, "lab_threshold_log_date")
    	 };
    	 
    	 for(int i = 0; i<labels.length; i++){
    			 HSSFCell cell = row.createCell(i);  
		         cell.setCellValue(labels[i]);  
		         cell.setCellStyle(style);  
    	 }
    }

    public void createPerfWarningLogCell(HSSFRow row, Map map ,String labellang){
    	row.createCell(0).setCellValue(checkIsNull(map.get("USR_STE_USR_ID"))); 
    	row.createCell(1).setCellValue(checkIsNull(map.get("ULT_TYPE"))); 
    	row.createCell(2).setCellValue(checkIsNull(map.get("ULT_LOGIN_TIMESTAMP")));
    }

    private String checkIsNull(Object str){
    	if(null == str){
    		str = "--";
    	}
    	return str.toString();
    }
    
    /**
     * 通过开始 - 结束 / lastdays天以前   获取需要导出数据的表名集合
     * @param starttime
     * @param endtime
     * @param lastdays
     * @return
     */
    private List<String> getLoginLogTableNames(String starttime,String endtime ,int lastdays){
    	List<String> tableNames = new ArrayList<String>();
    	Date beforedate = null;
    	Date sta_date = new Date();
    	Date end_date = new Date();
        SimpleDateFormat sdf_all=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        
        //选择过去lastdays天 
    	if(lastdays >= 0 && ((starttime==null ||starttime.equals(""))  && (endtime==null || endtime.equals("")))){
        	Calendar  cal=Calendar.getInstance();
        	try {
				cal.setTime(sdf_all.parse(sdf_all.format(new Date())));
			} catch (ParseException e) {
				CommonLog.error(e.getMessage(),e);
			}   
            cal.add(Calendar.DATE,-lastdays); 
            sta_date = cal.getTime();
            end_date = DateUtil.getInstance().getDate(); //结束时间为当前时间
            //starttime = DateUtil.getInstance().formateDate(beforedate);
            //endtime = DateUtil.getInstance().formateDate(DateUtil.getInstance().getDate());
		}
    	
    	//当前时间
    	Date now_date = DateUtil.getInstance().getDate();
    	
    	//开始时间为空  则默认给当前时间的上一年 （用户登录日志默认只保存一年内数据）
    	if(null == starttime || starttime == ""){
    		starttime = DateUtil.getInstance().formateDate(DateUtil.getInstance().getDate(DateUtil.getInstance().getDateYear(now_date)-1,DateUtil.getInstance().getDateMonth(now_date),DateUtil.getInstance().getDateDay(now_date)));
    		sta_date = DateUtil.getInstance().getDate(DateUtil.getInstance().getDateYear(now_date)-1,DateUtil.getInstance().getDateMonth(now_date),DateUtil.getInstance().getDateDay(now_date));
    	}
    	//未指定结束时间  则默认为当前时间
    	if(null == endtime || endtime == ""){
    		endtime = DateUtil.getInstance().formateDate(DateUtil.getInstance().getDate());
    		end_date = now_date;
    	}
    	
    	int str_year = 0;
    	int str_mon = 0;
    	int str_day = 0;
    	
    	if(starttime.length() >= 10){
    		//条件有选择指定开始时间
    		str_year = Integer.parseInt(starttime.substring(0, 4));
       	    str_mon = Integer.parseInt(starttime.substring(5, 7));
       	    str_day = Integer.parseInt(starttime.substring(8, 10));
    	}else if(null != sta_date){
    		//starttime长度不够时，且sta_date不为空时
    		str_year = DateUtil.getInstance().getDateYear(sta_date);
       	    str_mon = DateUtil.getInstance().getDateMonth(sta_date);
       	    str_day = DateUtil.getInstance().getDateDay(sta_date);
    	}
    	
    	int end_year = 0;
    	int end_mon = 0;
    	int end_day = 0;
    	
	    if(endtime.length() >= 10){
	    	//条件有选择指定结束时间
	    	end_year =Integer.parseInt(endtime.substring(0, 4));
	    	end_mon = Integer.parseInt(endtime.substring(5, 7));
	    	end_day = Integer.parseInt(endtime.substring(8, 10));
	    }else if(null != end_date){
	    	//endtime长度不够时，且sta_date不为空时
	    	end_year = DateUtil.getInstance().getDateYear(end_date);
	    	end_mon = DateUtil.getInstance().getDateMonth(end_date);
	    	end_day = DateUtil.getInstance().getDateDay(end_date);
	    }
	    
	    //选择导出时间  跨年份
    	if(end_year - str_year >0){
    		//现用户登录日志之保存一年数据，但这里未加限制。      通过选择的时间循环判断对应月份表是否存在。
    		for(int i=1;str_year<end_year;str_year++,i++){
    			if(end_year - str_year > 1 && i !=1){
    				for(int j=1;j<=12;j++){
    					String tabName = loginActionLogService.getTableName(DateUtil.getInstance().getDate(str_year,j,str_day));
		    	    	if(loginActionLogService.existTable(tabName) > 0){
		    	    		tableNames.add(tabName);
		    	    	}
    				}
    			}else{
    				if(i != 1 || end_year - str_year == 1){
	    				for(;1<=end_mon;end_mon--){
							String tabName = loginActionLogService.getTableName(DateUtil.getInstance().getDate(end_year,end_mon,end_day));
			    	    	if(loginActionLogService.existTable(tabName) > 0){
			    	    		tableNames.add(tabName);
			    	    	}
	    				}
    				}
    				for(;12>=str_mon;str_mon++){
						String tabName = loginActionLogService.getTableName(DateUtil.getInstance().getDate(str_year,str_mon,str_day));
		    	    	if(loginActionLogService.existTable(tabName) > 0){
		    	    		tableNames.add(tabName);
		    	    	}
    				}
    			}
    		}
    	}else{
    		//选择的时间在同一年  只需要比较月份
    		for(;str_mon<=end_mon;str_mon++){
    			String tabName = loginActionLogService.getTableName(DateUtil.getInstance().getDate(str_year,str_mon,str_day));
    	    	if(loginActionLogService.existTable(tabName) > 0){
    	    		tableNames.add(tabName);
    	    	}
    		}
    	}	
    	return tableNames;
    }
    
}