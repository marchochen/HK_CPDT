package com.cwn.wizbank.cpdt.service;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cw.wizbank.ae.aeApplication;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeQueueManager;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.dataMigrate.imp.Imp;
import com.cw.wizbank.dataMigrate.imp.ImpUser;
import com.cw.wizbank.db.DbIMSLog;
import com.cw.wizbank.enterprise.IMSLog;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbEnv;
import com.cw.wizbank.upload.UploadUtils;
import com.cw.wizbank.util.LangLabel;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.cpdt.entity.AeItemCpdtItem;
import com.cwn.wizbank.cpdt.entity.CpdtGroup;
import com.cwn.wizbank.cpdt.entity.CpdtGroupRegistration;
import com.cwn.wizbank.cpdt.entity.CpdtLrnAwardRecord;
import com.cwn.wizbank.cpdt.entity.CpdtRegistration;
import com.cwn.wizbank.cpdt.entity.CpdtType;
import com.cwn.wizbank.cpdt.vo.AeItemCpdtGourpItemVo;
import com.cwn.wizbank.cpdt.vo.CpdtImportAwardedHoursVo;
import com.cwn.wizbank.entity.AeApplication;
import com.cwn.wizbank.entity.AeAttendance;
import com.cwn.wizbank.entity.AeItem;
import com.cwn.wizbank.entity.CourseEvaluation;
import com.cwn.wizbank.entity.CpdType;
import com.cwn.wizbank.entity.ImsLog;
import com.cwn.wizbank.entity.RegUser;
import com.cwn.wizbank.persistence.ImsLogMapper;
import com.cwn.wizbank.utils.CommonLog;
import com.cwn.wizbank.utils.DateUtil;
import com.cwn.wizbank.utils.Page;

@Service
public class CpdtImportAwardedHoursService extends BaseService {
	
	 @Autowired
	 ImsLogMapper imsLogMapper;
	 private DateUtil  dateUtil = DateUtil.getInstance();
	 
	 // 导入报表格式，第一行格式
	 private String[][] REQUIRED_TARGET_COLUMN = {{
	            "User ID*",
	            "Course Code/Class Code*",
	            "License type*",
	            "CPT/D Group Code*",
	            "CPT/D Hours Award Date*",
	            "Core Hours Awarded*",
	            "Non-core Hours Awarded",
	        }};
	
	/** 导入用户获得CPT/D时数 - 日志数据 **/
    public Page<ImsLog> searchAll(Page<ImsLog> page,loginProfile prof,qdbEnv static_env) {
    	
    	// 从数据库中查询日志数据
        page.getParams().put("ilg_type", "CPDAWARDHOURS");
        page.getParams().put("ilg_process", "IMPORT");
        page.getParams().put("current_role", prof.current_role);
        page.getParams().put("root_ent_id", prof.root_ent_id);
        page.getParams().put("my_top_tc_id", prof.my_top_tc_id);
        List<ImsLog> imsLogList =  imsLogMapper.searchAll(page);
        
        // 封装日志路径 
        for(ImsLog imsLog : imsLogList) {
            try {
            	// 导入文件下载路径
                imsLog.file_uri = IMSLog.getUploadedFileURI(imsLog.ilg_id,imsLog.ilg_filename, static_env);
                // 日志文件，[成功,失败，错误]三类
                Hashtable<String,String> logFileUri = IMSLog.getLogFilesURI(imsLog.ilg_id, static_env);
                for(int i = 0 ; i < 3 ; i++){
                    if(IMSLog.LOG_LIST[i].equals("SUCCESS")){
                        imsLog.success_file_uri = (String)logFileUri.get(IMSLog.FILENAME_LIST[i]);
                    } else if (IMSLog.LOG_LIST[i].equals("UNSUCCESS")){
                        imsLog.unsuccess_file_uri = (String)logFileUri.get(IMSLog.FILENAME_LIST[i]);
                    } else if (IMSLog.LOG_LIST[i].equals("ERROR")){
                        imsLog.error_file_uri = (String)logFileUri.get(IMSLog.FILENAME_LIST[i]);
                    }
                }
            } catch (cwException e) {
                e.printStackTrace();
            }
        }
        
        return page;
    }


	/** 校验并保存导入的文件 **/
    public Map<String, Object> checkAndSaveFile(MultipartFile file,WizbiniLoader wizbini,qdbEnv static_env,loginProfile prof) {
		Map<String, Object> result = new HashMap<String, Object>();
	    String msg = "";
	    File srcFile = null;
        try {
            // 创建临时文件
            String fileName = file.getName();
            fileName = file.getOriginalFilename();
            String tmpUploadPath = static_env.INI_MSG_DIR_UPLOAD_TMP + cwUtils.SLASH + dateUtil.dateToString(new Date(), "SSSHHmmss") + cwUtils.SLASH + "cpdt_awarded_hours_upload";
            srcFile = new File(tmpUploadPath, fileName);
            if (!srcFile.getParentFile().exists()) {
            	srcFile.getParentFile().mkdirs();
            }
             
//            Imp imp=new ImpCPDRegistration(null,wizbini,tmpUploadPath+cwUtils.SLASH,srcFile.getAbsolutePath());
        	// 将原文件内容转移到临时文件
            file.transferTo(srcFile);
            // 空文件和空数据校验
            if(file.isEmpty()) {
            	result.put("msg", LangLabel.getValue(prof.cur_lan, "label_core_cpd_t_user_awarded_1").replace("XXX", fileName));
            	return result;
            }
            // 读取EXCEL文件数据，并加载到缓存中
            List<String[]> excelData = parseFile(srcFile.toString());
            if(excelData == null || excelData.size() <= 0){
            	result.put("msg", LangLabel.getValue(prof.cur_lan, "label_core_cpd_t_user_awarded_1").replace("XXX", fileName));
            	return result;
            }
            //获取上传限制用户最大数
            int maxUploadCount = wizbini.cfgSysSetupadv.getUserBatchUpload().getMaxUploadCount();
            //如果大于上传用户最大数
            if (excelData.size() > maxUploadCount){
                throw new cwSysMessage(UploadUtils.SMSG_ULG_UPLOAD_COUNT_EXCEED_LIMIT, new Integer(maxUploadCount).toString());
            }
            // 校验数据合法性
            result = checkData(excelData,fileName,prof.cur_lan);
            result.put("filePath", srcFile.toString());
            
        } catch (cwSysMessage e) {
            String encoding;
            if(prof == null){
                encoding = static_env.ENCODING;
            }else{
                encoding = prof.label_lan;
            }
            msg = ((cwSysMessage) e).getSystemMessage(encoding);
            result.put("msg", msg);
            if(srcFile.exists()){
                srcFile.delete();
            }
        } catch(Exception other){
        	other.printStackTrace();
        }
        return result;
    }
    
    
    /** 解析excel数据文件 **/
    public List<String[]> parseFile(String sourceFile) throws cwSysMessage, cwException{
    	Workbook wb = null;
    	List<String[]> vtEnrollment = null;
        try {
        	File file = new File(sourceFile);
            wb = Workbook.getWorkbook(file);
            Sheet sheet = wb.getSheet(0);
            if (sheet == null || sheet.getRows() <= 1) {
                 return null;
            }
            Cell cell[] = sheet.getRow(0);
            // 校验第一行标题列是否正确
            if(cell.length != 7 
            		|| !cell[0].getContents().trim().equals(REQUIRED_TARGET_COLUMN[0][0])
            		|| !cell[1].getContents().trim().equals(REQUIRED_TARGET_COLUMN[0][1])
            		|| !cell[2].getContents().trim().equals(REQUIRED_TARGET_COLUMN[0][2])
            		|| !cell[3].getContents().trim().equals(REQUIRED_TARGET_COLUMN[0][3])
            		|| !cell[4].getContents().trim().equals(REQUIRED_TARGET_COLUMN[0][4])
            		|| !cell[5].getContents().trim().equals(REQUIRED_TARGET_COLUMN[0][5])
            		|| !cell[6].getContents().trim().equals(REQUIRED_TARGET_COLUMN[0][6])
            ){
                return null;
            }
            

            vtEnrollment = new ArrayList<String[]>();
            int row = sheet.getRows();
            
            for (int i = 1; i < row; i++) {
                cell = sheet.getRow(i);
                String[] rowCellArray = new String[cell.length];
                boolean isContinue = true;
                for (int cellIndex = 0; cellIndex < cell.length; cellIndex++) {
                	isContinue = false;
                    String cellValue = cell[cellIndex].getContents();
                    if(!StringUtils.isEmpty(cellValue)) {
                    	rowCellArray[cellIndex] = cellValue;
                    }else{
                    	rowCellArray[cellIndex] = "";
                    }
                }
                if(isContinue){
                    continue;
                }
                vtEnrollment.add(rowCellArray);
		    }
            return vtEnrollment;
        } catch (BiffException e) {
            throw new cwSysMessage("GEN009");
        } catch (IOException e) {
            throw new cwException("read file error:" + e.getMessage());
        } finally {
        	if(wb != null) {
        		wb.close();
        	}
        }
    }
	

    /** 校验数据是否符合规范 **/
    public Map<String, Object> checkData(List<String[]> excelData,String fileName,String cur_lan) {
        Map<String, Object> result = new HashMap<String, Object>();
        List<String> errorMsg = new ArrayList<String>(); // 用于存放错误信息的容器
        List<String> successMsg = new ArrayList<String>();//成功提示信息
        List<CpdtImportAwardedHoursVo> successObj = new ArrayList<CpdtImportAwardedHoursVo>(); // 存放成功通过的对象
        int errorNumber = 0;
        int successNumber = 0;
        int totalNumber = 0;
        
        // 导入的数据临时备份，用于判断数据重复
        Map<String,List<Date>> existRecord = new HashMap<String,List<Date>>(); 
        // [Error] Line XX : 
        String errorLogMsgHead = LangLabel.getValue(cur_lan, "label_core_cpd_t_user_awarded_2");
        
        for (int i = 0; i < excelData.size(); i++) {
        	int rowIndex = (i+2);
        	String[] rowCellArray = excelData.get(i);
        	errorLogMsgHead = errorLogMsgHead.replace("XXX", rowIndex+"");
        	Map<String,Boolean> validOption = new HashMap<String,Boolean>();
        	validOption.put("UserId", false);
        	validOption.put("CourseCode", false);
        	validOption.put("LicenseType", false);
        	validOption.put("GroupCode", false);
        	validOption.put("HoursAwardDate", false);
        	validOption.put("CoreHoursAward", false);
        	validOption.put("NonCoreHoursAward", true);
        	validOption.put("AwardHoursType", true);
        	boolean noAwardHours = false; // 能导入，不能获取学分（学分为0）
        	CpdtImportAwardedHoursVo cpdtImportAwardedHoursVo = new CpdtImportAwardedHoursVo();
        	
            // 1.User ID
    		RegUser regUser = null;
            if(rowCellArray.length >= 1 && !StringUtils.isEmpty(rowCellArray[0])) {
            	String steUsrId = rowCellArray[0].trim();
            	if(steUsrId.length() < 3) {
            		errorMsg.add(errorLogMsgHead + LangLabel.getValue(cur_lan, "label_core_cpd_t_user_awarded_3"));
            	} else if (steUsrId.length() > 20) {
            		errorMsg.add(errorLogMsgHead + LangLabel.getValue(cur_lan, "label_core_cpd_t_user_awarded_4"));
            	} else {
            		regUser = regUserMapper.getUserBySteId(steUsrId);
                    if(regUser == null){
                    	errorMsg.add(errorLogMsgHead + LangLabel.getValue(cur_lan, "label_core_cpd_t_user_awarded_6").replace("{UserId}", steUsrId));
                    } else {
                    	cpdtImportAwardedHoursVo.setUserId(steUsrId);
                    	validOption.put("UserId", true);
                    }
            	}
            } else {
            	errorMsg.add(errorLogMsgHead + "User Id" + LangLabel.getValue(cur_lan, "label_core_cpd_t_user_awarded_5"));
            }
            
            
            // 2.Course code/class code 
            AeItem aeItem = null;
            if(rowCellArray.length >= 2 && !StringUtils.isEmpty(rowCellArray[1])) {
            	String courseCode = rowCellArray[1].trim();
            	if(courseCode.length() > 50) {
            		errorMsg.add(errorLogMsgHead + LangLabel.getValue(cur_lan, "label_core_cpd_t_user_awarded_7"));
            	} else {
            		aeItem =aeItemMapper.checkAeitemExist(courseCode);
            		 if(aeItem == null) {
            			 errorMsg.add(errorLogMsgHead + LangLabel.getValue(cur_lan, "label_core_cpd_t_user_awarded_8").replace("{courseCode}", courseCode));
            		 } else if(aeItem.getItm_create_run_ind()==1 && aeItem.getItm_run_ind()==0 && aeItem.getItm_integrated_ind() ==0){
            			 // 如果是classroom,需用班级的code
            			 errorMsg.add(errorLogMsgHead + LangLabel.getValue(cur_lan, "label_core_cpd_t_user_awarded_9"));
            		 } else if(!aeItem.getItm_type().equals("CLASSROOM") && !aeItem.getItm_type().equals("SELFSTUDY")){
            			 //只能是CLASSROOM和SELFSTUDY类型
            			 errorMsg.add(errorLogMsgHead + LangLabel.getValue(cur_lan, "label_core_cpd_t_user_awarded_10").replace("{courseCode}", courseCode));
            		 } else {
            			 cpdtImportAwardedHoursVo.setItmId(aeItem.getItm_id());
            			 cpdtImportAwardedHoursVo.setCourseCode(courseCode);
            			 validOption.put("CourseCode", true);
            			 if(validOption.get("UserId")){
            				 // 获取报名记录
                             AeApplication tempAeApplication = new AeApplication();
                             tempAeApplication.setApp_ent_id(regUser.getUsr_ent_id());
                             tempAeApplication.setApp_itm_id(aeItem.getItm_id());
                             List<AeApplication> aeApplication = aeApplicationMapper.getAeApplicationByitmAttend(tempAeApplication);
                             if(aeApplication != null && aeApplication.size() > 0){
                            	 // 學習狀態是已完成的不能導入
                            	 AeAttendance aeAttendance = new AeAttendance();
                                 aeAttendance = aeAttendanceMapper.get(aeApplication.get(0).getApp_id());
                                 if(aeAttendance.getAtt_ats_id().longValue() == 1) {
                                	 errorMsg.add(errorLogMsgHead + LangLabel.getValue(cur_lan, "label_core_cpd_t_user_awarded_9999"));
                                 } else {
                                	 validOption.put("CourseCode", true);
                                 }
                             } else {
                            	 validOption.put("CourseCode", true);
                             }
                         }
            		 }
            	}
            } else {
            	errorMsg.add(errorLogMsgHead + "Course code/class code" + LangLabel.getValue(cur_lan, "label_core_cpd_t_user_awarded_5"));
            }
            
            
            // 3.License type
            CpdtType cpdtType = null;
            if(rowCellArray.length >= 3 && !StringUtils.isEmpty(rowCellArray[2])) {
            	String licenseType = rowCellArray[2].trim();
            	if(licenseType.length() > 20) {
            		errorMsg.add(errorLogMsgHead + LangLabel.getValue(cur_lan, "label_core_cpd_t_user_awarded_13"));
            	} else {
            		cpdtType = cpdtTypeMapper.getTypeByCode(licenseType);
            		if(cpdtType == null) {
            			errorMsg.add(errorLogMsgHead + LangLabel.getValue(cur_lan, "label_core_cpd_t_user_awarded_14").replace("{licenseType}", licenseType));
            		} else {
            			if(validOption.get("UserId")) {
            				// 用户只要注册了大牌便可导入，如果大牌已经除牌了，那么便不可以获取时数（即：时数为0）
            				Map<String,Object> params = new HashMap<String,Object>();
            				params.put("crUsrEntId", regUser.getUsr_ent_id());
            				params.put("crCtId", cpdtType.getCt_id());
            				CpdtRegistration cpdtRegistration = cpdtRegistrationMapper.getNewestRegInfo(params);
            				if(cpdtRegistration != null) {
            					 if((cpdtRegistration.getCr_de_reg_datetime() != null && 
                                         (cpdtRegistration.getCr_de_reg_datetime().getTime() + (24*3600-1)) > System.currentTimeMillis()) || 
                                         (cpdtRegistration.getCr_reg_datetime().getTime() > System.currentTimeMillis())
                                     	){
            						 noAwardHours = true;
                                     errorMsg.add(errorLogMsgHead.replace("Error", "Warn") + LangLabel.getValue(cur_lan, "label_core_cpd_t_user_awarded_15").replace("{UserID}", regUser.getUsr_ste_usr_id()));
                                  }
                                  cpdtImportAwardedHoursVo.setLicenseType(licenseType);
                                  cpdtImportAwardedHoursVo.setCtId(cpdtType.getCt_id());
                                  validOption.put("LicenseType", true);
            				} else {
            					 errorMsg.add(errorLogMsgHead + LangLabel.getValue(cur_lan, "label_core_cpd_t_user_awarded_31"));
            				}
                            
            			}
            		}
            	}
            } else {
            	errorMsg.add(errorLogMsgHead + "License type" + LangLabel.getValue(cur_lan, "label_core_cpd_t_user_awarded_5"));
            }

            
            // 4.CPT/D group code
            CpdtGroupRegistration cpdtGroupRegistration = null;
            if(rowCellArray.length >= 4 && !StringUtils.isEmpty(rowCellArray[3])) {
            	String cgCode = rowCellArray[3].trim();
            	if(cgCode.length() > 20) {
            		errorMsg.add(errorLogMsgHead + LangLabel.getValue(cur_lan, "label_core_cpd_t_user_awarded_17"));
            	} else {
                     if(validOption.get("LicenseType")) {
                    	 //  小牌是否存在
                		 CpdtGroup cpdGroup = cpdtGroupMapper.getGroupCode(cgCode);
                         if(cpdGroup != null) {
                        	 if(cpdGroup.getCg_ct_id().longValue() == cpdtType.getCt_id().longValue()) {
                        		 // 判断用户是否有注册该小牌
                        		 Map<String,Object> params = new HashMap<String,Object>();
                        		 params.put("cgrUsrEntId", regUser.getUsr_ent_id());
                        		 params.put("cgrCgId", cpdGroup.getCg_id());
                        		 cpdtGroupRegistration = cpdtGroupRegistrationMapper.getNewestRegInfo(params);
                        		 if(cpdtGroupRegistration == null){
                        			 errorMsg.add(errorLogMsgHead + LangLabel.getValue(cur_lan, "label_core_cpd_t_user_awarded_19"));
                        		 } else {
                        			 cpdtImportAwardedHoursVo.setGroupCode(cgCode);
                        			 cpdtImportAwardedHoursVo.setCgId(cpdGroup.getCg_id());
                        			 validOption.put("GroupCode", true);
                        		 }
                        	 } else {
                        		 errorMsg.add(errorLogMsgHead + LangLabel.getValue(cur_lan, "label_core_cpd_t_user_awarded_18").replace("{groupCode}", cgCode));
                        	 }
                         } else {
                        	 errorMsg.add(errorLogMsgHead + LangLabel.getValue(cur_lan, "label_core_cpd_t_user_awarded_22").replace("{groupCode}", cgCode));
                         }
                     }
            	}
            } else {
            	errorMsg.add(errorLogMsgHead + "CPT/D group code" + LangLabel.getValue(cur_lan, "label_core_cpd_t_user_awarded_5"));
            }
            

            // 5.CPT/D Hours Award Date
            if(rowCellArray.length >= 5 && !StringUtils.isEmpty(rowCellArray[4])) {
            	// 日期格式验证
            	String dateString = rowCellArray[4].trim();
            	if(dateUtil.valid(dateString, "yyyy/MM/dd")) {
            		cpdtImportAwardedHoursVo.setStrHoursAwardDate(dateString);
            		cpdtImportAwardedHoursVo.setHoursAwardDate(DateUtil.stringToDate(dateString,"yyyy/MM/dd"));
            		validOption.put("HoursAwardDate", true);
            	} else {
            		errorMsg.add(errorLogMsgHead + LangLabel.getValue(cur_lan, "label_core_cpd_t_user_awarded_20"));
            	}
            } else {
            	errorMsg.add(errorLogMsgHead + "CPT/D hours award date" + LangLabel.getValue(cur_lan, "label_core_cpd_t_user_awarded_5"));
            }
            
            
          	// >> 判断该课程是否有对该小牌设置时数
            AeItemCpdtGourpItemVo aeItemCpdtGourpItemVo = null;
            if(validOption.get("CourseCode") && validOption.get("GroupCode")){
            	Map<String,Object> param = new HashMap<String,Object>();
                param.put("itmId", aeItem.getItm_id());
                param.put("cgId", cpdtGroupRegistration.getCgr_cg_id());
                aeItemCpdtGourpItemVo = aeItemCpdtGourpItemMapper.getAeItemCpdtGourpItemVo(param);
                if(aeItemCpdtGourpItemVo == null) {
                	errorMsg.add(errorLogMsgHead + LangLabel.getValue(cur_lan, "label_core_cpd_t_user_awarded_23").replace("{CourseCode}", aeItem.getItm_code()).replace("{CourseName}", aeItem.getItm_title()));
                	errorNumber++;
                	totalNumber++;
                	continue;
                }
            }
            cpdtImportAwardedHoursVo.setAcgiId(aeItemCpdtGourpItemVo.getAcgi_id());
            
            
            // 6.Core hours awarded
            if(rowCellArray.length >= 6 && !StringUtils.isEmpty(rowCellArray[5])) {
            	String coreHours = rowCellArray[5].trim();
            	// 0 <= x <= 999.99
            	if(!Pattern.matches("^[0-9]{1,3}(\\.[0-9]{1,2}$)?", coreHours)) {
            		errorMsg.add(errorLogMsgHead + LangLabel.getValue(cur_lan, "label_core_cpd_t_user_awarded_21"));
            	} else {
            		float floatCoreHours = Float.parseFloat(coreHours);
                	// 警告：导入文档设的核心时数大于课程设的能获取核心时数
                	if(aeItemCpdtGourpItemVo.getAcgi_award_core_hours() < floatCoreHours){
                		errorMsg.add(errorLogMsgHead.replace("Error", "Warn") + LangLabel.getValue(cur_lan, "label_core_cpd_t_user_awarded_25")
                				.replace("{UserID}", regUser.getUsr_ste_usr_id()).replace("{CourseCode}", aeItem.getItm_code()).replace("{CourseName}", aeItem.getItm_title()));
                    } else if (aeItemCpdtGourpItemVo.getAcgi_award_core_hours() > floatCoreHours) {
                    	errorMsg.add(errorLogMsgHead.replace("Error", "Warn") + LangLabel.getValue(cur_lan, "label_core_cpd_t_user_awarded_24")
                				.replace("{UserID}", regUser.getUsr_ste_usr_id()).replace("{CourseCode}", aeItem.getItm_code()).replace("{CourseName}", aeItem.getItm_title()));
                    }
                	cpdtImportAwardedHoursVo.setCoreHours(floatCoreHours);
                	validOption.put("CoreHoursAward", true);
            	}
            } else {
            	errorMsg.add(errorLogMsgHead + "Core hours awarded" + LangLabel.getValue(cur_lan, "label_core_cpd_t_user_awarded_5"));
            }
            
            
            // 7.Non-core Hours Awarded
            if(rowCellArray.length >= 7 && !StringUtils.isEmpty(rowCellArray[6])) {
            	String nonCoreHours = rowCellArray[6].trim();
            	// 0 <= x <= 999.99
            	if(!Pattern.matches("^[0-9]{1,3}(\\.[0-9]{1,2}$)?", nonCoreHours)) {
            		errorMsg.add(errorLogMsgHead + LangLabel.getValue(cur_lan, "label_core_cpd_t_user_awarded_26"));
            		validOption.put("NonCoreHoursAward", false);
            	} else {
            		float floatNonCoreHours = Float.parseFloat(nonCoreHours);
                	// 警告：导入文档设的核心时数大于课程设的能获取核心时数
                	if(aeItemCpdtGourpItemVo.getAcgi_award_non_core_hours() < floatNonCoreHours){
                		errorMsg.add(errorLogMsgHead.replace("Error", "Warn") + LangLabel.getValue(cur_lan, "label_core_cpd_t_user_awarded_25")
                				.replace("{UserID}", regUser.getUsr_ste_usr_id()).replace("{CourseCode}", aeItem.getItm_code()).replace("{CourseName}", aeItem.getItm_title()));
                    } else if (aeItemCpdtGourpItemVo.getAcgi_award_non_core_hours() > floatNonCoreHours) {
                    	errorMsg.add(errorLogMsgHead.replace("Error", "Warn") + LangLabel.getValue(cur_lan, "label_core_cpd_t_user_awarded_24")
                				.replace("{UserID}", regUser.getUsr_ste_usr_id()).replace("{CourseCode}", aeItem.getItm_code()).replace("{CourseName}", aeItem.getItm_title()));
                    }
                	cpdtImportAwardedHoursVo.setNonCoreHours(floatNonCoreHours);
            	}
            	
            }
            
            
            
            // 牌照设置的获取规则
            //检测要导入的数据中,是否存在重复的多条记录
            String key = "";
        	if(aeItem.getItm_parent_id() != null && aeItem.getItm_parent_id() > 0) {
        		// 面授课程
        		key = cpdtType.getCt_license_type() + aeItem.getItm_parent_id() + cpdtGroupRegistration.getCgr_cg_id() + regUser.getUsr_id();
        	} else {
        		// 网上课程
        		key = cpdtType.getCt_license_type() + aeItem.getItm_id() + cpdtGroupRegistration.getCgr_cg_id() + regUser.getUsr_id();
        	}
        	
            // >> 如果不是每门课程完成就可以获得学分
            if(cpdtType.getCt_award_hours_type().intValue() != CpdtType.AWARD_HOURS_TYPE_FINAL) {
            	
            	// 获取用户在该课程已获取的时数记录
            	Map<String,Object> params = new HashMap<String,Object>();
            	params.put("clarUsrEntId", regUser.getUsr_ent_id());
            	params.put("clarItmId", aeItem.getItm_id());
            	params.put("clarCgId", cpdtGroupRegistration.getCgr_cg_id());
            	List<CpdtLrnAwardRecord> records = cpdtLrnAwardRecordMapper.queryAlreadyAward(params);
            	
            	// 用户在该课程已获取过相关的时数记录
            	if((records != null && records.size() > 0) && !existRecord.containsKey(key)) {
            		// 每门课程在该评估周期内可获取一次时数
                	if(cpdtType.getCt_award_hours_type() == CpdType.AWARD_HOURS_TYPE_PERIOD_ONCE){
                		// >> 1.根据CPT/D Hours Award Date获取当前导入的数据的评估周期
                		String[] HoursAwardDates = rowCellArray[4].trim().split("/");
                		int year = Integer.parseInt(HoursAwardDates[0]);
                		String strStartDate = "";
                		if(Integer.parseInt(HoursAwardDates[1]) < cpdtType.getCt_starting_month()) {
                			strStartDate = year-1 + "-" + cpdtType.getCt_starting_month() + "-" + "01";
                		} else {
                			strStartDate = year + "-" + cpdtType.getCt_starting_month() + "-" + "01";
                		}
                		Date startDate = DateUtil.stringToDate(strStartDate, "yyyy-MM-dd");
                		Date endDate = dateUtil.addDateYear(startDate, 1);
                		long longStartDate = startDate.getTime();
                		long endStartDate = endDate.getTime();
                		// >> 2.筛选数据库查询的records是否有在该评估周期内的记录
                		for(CpdtLrnAwardRecord cpdtLrnAwardRecord : records) {
                			if(cpdtLrnAwardRecord.getClar_award_datetime().getTime() >= longStartDate &&
                					cpdtLrnAwardRecord.getClar_award_datetime().getTime() < endStartDate) {
                				validOption.put("AwardHoursType", false);
                				errorMsg.add(errorLogMsgHead + LangLabel.getValue(cur_lan, "label_core_cpd_t_user_awarded_28").replace("{licenseType}", cpdtType.getCt_license_type()));
                				break;
                			}
                		}
                		// >> 3.在不满足2的情况下，筛选导入的数据中是否有满足该评估周期内的记录
                		if(validOption.get("AwardHoursType")) {
                			List<Date> tempList = existRecord.get(key);
                			for(Date date : tempList) {
                				if(date.getTime() >= longStartDate && date.getTime() < endStartDate) {
                					validOption.put("AwardHoursType", false);
                					errorMsg.add(errorLogMsgHead + LangLabel.getValue(cur_lan, "label_core_cpd_t_user_awarded_28").replace("{licenseType}", cpdtType.getCt_license_type()));
                    				break;
                				}
                			}
                		}
                		// >> 4.前面3条都不满足的情况下视为成功
                	}else{
                		//每门课只可获取一次时数
                		errorMsg.add(errorLogMsgHead + LangLabel.getValue(cur_lan, "label_core_cpd_t_user_awarded_27").replace("{licenseType}", cpdtType.getCt_license_type()));
                		validOption.put("AwardHoursType", false);
                	}
            	}
            }
            
            
         // 1. 如果是网上课程：获取时数时间不能大于设置的时数获取限制日期
            if(aeItem.getItm_type().equals("SELFSTUDY")) {
            	AeItemCpdtItem aeItemCpdtItem = aeItemCpdtItemMapper.getByItmId(aeItem.getItm_id());
            	 if(aeItemCpdtItem.getAci_hours_end_date() != null) {
            		Date hourAwardDate = DateUtil.stringToDate(rowCellArray[4].trim(),"yyyy/MM/dd");
                 	if(hourAwardDate.getTime() > aeItemCpdtItem.getAci_hours_end_date().getTime()) {
                 		noAwardHours = true;
                 		errorMsg.add(errorLogMsgHead.replace("Error", "Warn") + LangLabel.getValue(cur_lan, "label_core_cpd_t_user_awarded_29")
                 				.replace("{UserID}", regUser.getUsr_ste_usr_id()).replace("{CourseCode}", aeItem.getItm_code()).replace("{CourseName}", aeItem.getItm_title()));
                 	}
                 }
            } else {
            // 2. 如果是班级：获取时数时间不能大于課程完結的日期
            	if(aeItem.getItm_run_ind() == 1) {
            		AeItem parentAeItem = aeItemMapper.getParent(aeItem.getItm_id());
            		if(parentAeItem.getItm_eff_end_datetime() != null) {
            			Date hourAwardDate = DateUtil.stringToDate(rowCellArray[4].trim(),"yyyy/MM/dd");
            			if(hourAwardDate.getTime() > parentAeItem.getItm_eff_end_datetime().getTime()) {
            				noAwardHours = true;
                     		errorMsg.add(errorLogMsgHead.replace("Error", "Warn") + LangLabel.getValue(cur_lan, "label_core_cpd_t_user_awarded_29")
                     				.replace("{UserID}", regUser.getUsr_ste_usr_id()).replace("{CourseCode}", aeItem.getItm_code()).replace("{CourseName}", aeItem.getItm_title()));
                     	}
            		}
            	}
            }
            
            
            // 允许导入，但是不能获取时分
            if(noAwardHours) {
            	cpdtImportAwardedHoursVo.setCoreHours(0.0f);
            	cpdtImportAwardedHoursVo.setNonCoreHours(0.0f);
            }
            
            // 验证是否成功导入
            boolean isSuccess = true;
            for(Map.Entry<String, Boolean> map : validOption.entrySet()) {
            	isSuccess = map.getValue();
            	if(!isSuccess) {
            		break;
            	}
            }
            
            if(isSuccess) {
            	successMsg.add(LangLabel.getValue(cur_lan, "label_core_cpd_t_user_awarded_30").replace("{Line}", rowIndex+"").replace("{UserId}", regUser.getUsr_ste_usr_id()));
            	successObj.add(cpdtImportAwardedHoursVo);
            	successNumber++;
            } else {
            	errorNumber++;
            }
            totalNumber++;
        }
        result.put("errorMsg", errorMsg);
        result.put("successMsg", successMsg);
        result.put("successObj", successObj);
        result.put("successNumber", successNumber);
        result.put("errorNumber", errorNumber);
        result.put("totalNumber", totalNumber);
        return result;
    }

    
    /** 确认导入 **/
    public Map<String, Object> comfirmUpload(String filePath,String upload_desc, loginProfile prof,qdbEnv static_env,WizbiniLoader wizbini) {
    	
        Connection con = null;
        Map<String, Object> map = new HashMap<String, Object>();
        try {
        	// 获取Connection
        	cwSQL sqlCon = new cwSQL();
            sqlCon.setParam(wizbini);
            con = sqlCon.openDB(false);
            // 剪切文件
            String dir = static_env.DOC_ROOT + cwUtils.SLASH + static_env.LOG_FOLDER + cwUtils.SLASH + IMSLog.CPD_FOLDER_IMSLOG + cwUtils.SLASH;
            File original = new File(filePath);
            String file_path = dir + original.getName();
            File destFile = new File(file_path);
            if (!destFile.getParentFile().exists()) {
            	destFile.getParentFile().mkdirs();
            }
			dbUtils.moveFile(original.getPath(), destFile.getPath());
			 if (original.exists()) {
				 original.delete();
            }
            //向数据库记录此次导入文件，日志信息
            DbIMSLog dbIlg=new DbIMSLog();
            dbIlg.ilg_type = IMSLog.IMSLOG_TYPE_CPDAWARDHOURS;
            dbIlg.ilg_process = IMSLog.IMSLOG_PROCESS_IMPORT;
            dbIlg.ilg_create_usr_id = prof.usr_id;
            dbIlg.ilg_create_timestamp = cwSQL.getTime(con);
            dbIlg.ilg_method = IMSLog.ACTION_TRIGGERED_BY_UI;
            dbIlg.ilg_desc = upload_desc;
            dbIlg.ilg_filename = destFile.getName();
            dbIlg.ilg_tcr_id = prof.my_top_tc_id;
            long logid = DbIMSLog.ins(con, dbIlg);
            con.commit();
            File logFile=new File(dir + logid);
            //如果目录存在就创建
            if(!logFile.exists()){
                logFile.mkdir();
            }
            //开始导入用户获得CPT/D时数
            long startTime = System.currentTimeMillis();
            Imp imp = new ImpUser(con, wizbini, logFile.getAbsolutePath() + cwUtils.SLASH , file_path ,false ,false ,false);
            Imp.ImportStatus importStatus= imp.doImpCpdtHours(prof , false , file_path , this , dbIlg.ilg_desc , static_env);
            long endTime = System.currentTimeMillis();
            System.out.println("import cptd awarded hours time consuming : " + ((endTime - startTime) / 1000) + "s");
            map.put("success_total", String.valueOf(importStatus.cnt_success));
            map.put("unsuccess_total", String.valueOf(importStatus.cnt_error));
            
            IMSLog.CPD = true;
            Hashtable<String,String> hFileUri = IMSLog.getLogFilesURI(dbIlg.ilg_id, static_env);
            map.put("success_href", hFileUri.get("success.txt"));

            con.commit();
        } catch (Exception e) {
        	CommonLog.error(e.getMessage(),e);
        }finally {
            if(con!=null){
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return map;
    }

    /** 保存数据到数据库（导入用户获得CPT/D时数） **/
    public void saveEnroll(Connection con,List<CpdtImportAwardedHoursVo> successData,loginProfile prof,WizbiniLoader wizbini,String filePath,String uploadDesc,qdbEnv staticEnv){
    	for (int i = 0 ; i < successData.size() ; i++) {
    		try {
    			Date currentDate = new Date();
            	CpdtImportAwardedHoursVo cpdtImportAwardedHoursVo = successData.get(i);
                RegUser regUser = regUserMapper.getUserBySteId(cpdtImportAwardedHoursVo.getUserId());
                
                // 获取报名记录
                AeApplication tempAeApplication = new AeApplication();
                tempAeApplication.setApp_ent_id(regUser.getUsr_ent_id());
                tempAeApplication.setApp_itm_id(cpdtImportAwardedHoursVo.getItmId());
                List<AeApplication> aeApplicationList = aeApplicationMapper.getAeApplicationByitmAttend(tempAeApplication);
                AeApplication aeApplication = null;

                if(aeApplicationList == null || aeApplicationList.size() <= 0) {
                    // 没有报名,导入报名
                	aeApplication = new AeApplication();
                	aeQueueManager aeQueueManager = new aeQueueManager();
                	aeApplication temp = aeQueueManager.insAppNoWorkflow(con, null, regUser.getUsr_ent_id(), cpdtImportAwardedHoursVo.getItmId(), null, null, prof, new aeItem());
                	con.commit();
                	aeApplication.setApp_id(temp.app_id);
                	aeApplication.setApp_tkh_id(temp.app_tkh_id);
                } else {
                	aeApplication = aeApplicationList.get(0);
                }
                
                // 更新学员为完成状态
                CourseEvaluation courseEvaluation = new CourseEvaluation();
                courseEvaluation = courseEvaluationMapper.getCourseEvaluationByThkId(aeApplication.getApp_tkh_id());
                courseEvaluation.setCov_status("C");
                courseEvaluation.setCov_progress(0.00);
                courseEvaluation.setCov_complete_datetime(cpdtImportAwardedHoursVo.getHoursAwardDate());
                courseEvaluationMapper.completeCourse(courseEvaluation);
                
                AeAttendance aeAttendance = new AeAttendance();
                aeAttendance = aeAttendanceMapper.get(aeApplication.getApp_id());
                aeAttendance.setAtt_ats_id(1);
                aeAttendance.setAtt_timestamp(cpdtImportAwardedHoursVo.getHoursAwardDate());
                aeAttendanceMapper.updateComplete(aeAttendance);
                
                //  导入用户获得CPT/D时数
                CpdtLrnAwardRecord cpdtLrnAwardRecord = new CpdtLrnAwardRecord();
                cpdtLrnAwardRecord.setClar_manul_ind(1);
                cpdtLrnAwardRecord.setClar_usr_ent_id(regUser.getUsr_ent_id());
                cpdtLrnAwardRecord.setClar_itm_id(cpdtImportAwardedHoursVo.getItmId());
                cpdtLrnAwardRecord.setClar_ct_id(cpdtImportAwardedHoursVo.getCtId());
                cpdtLrnAwardRecord.setClar_cg_id(cpdtImportAwardedHoursVo.getCgId());
                cpdtLrnAwardRecord.setClar_acgi_id(cpdtImportAwardedHoursVo.getAcgiId());
                cpdtLrnAwardRecord.setClar_award_core_hours(cpdtImportAwardedHoursVo.getCoreHours());
                cpdtLrnAwardRecord.setClar_award_non_core_hours(cpdtImportAwardedHoursVo.getNonCoreHours());
                cpdtLrnAwardRecord.setClar_create_usr_ent_id(prof.usr_ent_id);
                cpdtLrnAwardRecord.setClar_create_datetime(currentDate);
                cpdtLrnAwardRecord.setClar_update_usr_ent_id(prof.usr_ent_id);
                cpdtLrnAwardRecord.setClar_update_datetime(currentDate);
                cpdtLrnAwardRecord.setClar_app_id(aeApplication.getApp_id());
                cpdtLrnAwardRecord.setClar_award_datetime(currentDate);
                cpdtLrnAwardRecordMapper.insert(cpdtLrnAwardRecord);
                
			} catch (Exception e) {
				CommonLog.error(e.getMessage(),e);
			}
        }
        
    }
}
