package com.cwn.wizbank.cpd.service;

import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.message.Message;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.cpd.vo.CpdGroupVO;
import com.cwn.wizbank.cpd.vo.CpdHourVO;
import com.cwn.wizbank.cpd.vo.CpdPeriodVO;
import com.cwn.wizbank.entity.*;
import com.cwn.wizbank.persistence.*;
import com.cwn.wizbank.services.BaseService;
import com.cwn.wizbank.utils.DateUtil;
import com.cwn.wizbank.utils.FormatUtil;
import com.cwn.wizbank.utils.LabelContent;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CpdOutstandingReportService  extends BaseService<CpdGroupRegHoursHistory>{
	
	@Autowired
	CpdGroupRegHoursHistoryMapper cpdGroupRegHoursHistoryMapper;

    @Autowired
    CpdGroupRegistrationMapper cpdGroupRegistrationMapper;
    
    @Autowired
    CpdGroupMapper cpdGroupMapper;
    
    @Autowired
    CpdLrnAwardRecordMapper cpdLrnAwardRecordMapper;
	
    @Autowired
    CpdReportRemarkMapper cpdReportRemarkMapper;
    
    @Autowired
    CpdReportRemarkHistoryMapper cpdReportRemarkHistoryMapper;

    @Autowired
    RegUserMapper regUserMapper;
    
    @Autowired
    CpdReportRemarkService cpdReportRemarkService;

    @Autowired
    CpdUtilService cpdUtilService;
    
    
    
    
    
	/**
	 * 获取当前周期用户的所有注册历史记录
	 * @param usrEntId
	 * @param period
	 * @return
	 */
	public List<CpdGroupRegHoursHistory>   getCpdGroupRegHoursHistory(Long usrEntId,int period){
		Map<String, Object> param = new HashMap<String,Object>();
		param.put("usr_ent_id", usrEntId);
        param.put("period", period);
		return cpdGroupRegHoursHistoryMapper.getCpdGroupRegHoursHistory(param);
	}
	
	/**
	 * 获取所有注册历史记录的年份
	 * @return
	 */
    public List<CpdGroupRegHoursHistory>   getCpdGroupRegHoursHistoryPeriod(){
        Map<String, Object> param = new HashMap<String,Object>();
        return cpdGroupRegHoursHistoryMapper.getCpdGroupRegHoursHistoryPeriod(param);
    }	
    
    
    /**
     * 导出excel的用户（TA页面培训管理员报表）
     * @return
     */
    public long[] getUserForExcelTa(long[] usertIdArray,long[] exportGroupIds, int exportUser,WizbiniLoader wizbini, loginProfile prof){
        Map<String,Object> paramUser = new HashMap<String,Object>();
        long[] usr_ent_id = null;
        paramUser.put("tc_independent", wizbini.cfgSysSetupadv.isTcIndependent());
        paramUser.put("my_top_tc_id", prof.my_top_tc_id);
        paramUser.put("usr_ent_id", prof.usr_ent_id);
        paramUser.put("current_role", prof.current_role);
        paramUser.put("exportGroupIds", exportGroupIds);
        paramUser.put("exportUser", exportUser);
        //当前角色是否与培训中心关联
        paramUser.put("is_role_tc_ind", AccessControlWZB.isRoleTcInd(prof.current_role));
        if(exportUser==0){//全部用户
            List<RegUser> regUsers  = regUserMapper.findUserListForCpdExcelTa(paramUser);
            long usertIdArrays[]=new long[regUsers.size()];
            for (int i = 0; i < regUsers.size(); i++) {
                usertIdArrays[i] = regUsers.get(i).getUsr_ent_id();
            }
            usr_ent_id = usertIdArrays;
        }else if(exportUser == 2){//指定用户组
            List<RegUser> regUsers  = regUserMapper.findUserListForCpdExcelTa(paramUser);
            long usertIdArrays[]=new long[regUsers.size()];
            for (int i = 0; i < regUsers.size(); i++) {
                usertIdArrays[i] = regUsers.get(i).getUsr_ent_id();
            }
            usr_ent_id = usertIdArrays;
        }else{
            if(null!=usertIdArray && usertIdArray.length>0){
                usr_ent_id = usertIdArray;
            }
        }
        return usr_ent_id;
    }   
    
    
    
    /**
     * 导出excel的用户（Supervisor页面我的下属）
     * @return
     */
    public long[] getUserForExcel(long[] usertIdArray, int exportUser,WizbiniLoader wizbini, loginProfile prof){
        //Map<String, Object> param = new HashMap<String,Object>();
        Map<String,Object> paramUser = new HashMap<String,Object>();
        long[] usr_ent_id = null;
        paramUser.put("tc_independent", wizbini.cfgSysSetupadv.isTcIndependent());
        paramUser.put("my_top_tc_id", prof.my_top_tc_id);//
        paramUser.put("usr_ent_id", prof.usr_ent_id);
        paramUser.put("current_role", prof.current_role);//
        //当前角色是否与培训中心关联
        paramUser.put("is_role_tc_ind", AccessControlWZB.isRoleTcInd(prof.current_role));
        if(exportUser==0){
            paramUser.put("supervise", "SUPERVISE");//所有下属
            List<RegUser> regUsers  = regUserMapper.findUserListForCpdExcel(paramUser);
            long usertIdArrays[]=new long[regUsers.size()];
            for (int i = 0; i < regUsers.size(); i++) {
                usertIdArrays[i] = regUsers.get(i).getUsr_ent_id();
            }
            usr_ent_id = usertIdArrays;
        }else if(exportUser == 1){//直属下属
            List<RegUser> regUsers  = regUserMapper.findUserListForCpdExcel(paramUser);
            long usertIdArrays[]=new long[regUsers.size()];
            for (int i = 0; i < regUsers.size(); i++) {
                usertIdArrays[i] = regUsers.get(i).getUsr_ent_id();
            }
            usr_ent_id = usertIdArrays;
        }else{
            if(null!=usertIdArray && usertIdArray.length>0){
                usr_ent_id = usertIdArray;
            }
        }
        return usr_ent_id;
    }   
	
    
    /**
     * 导出Excel的数据处理
     * @param usertIdArray
     * @param cghiCtIdArray
     * @param period
     * @return
     */
    public Map<Integer, List> excelData(long[] usr_ent_id, long[] cghiCtIdArray,int exportUser,int period,WizbiniLoader wizbini, loginProfile prof){
        Map<String, Object> param = new HashMap<String,Object>();
        Map<Integer, List> map = new HashMap<Integer, List>();
        param.put("usr_ent_id",  usr_ent_id);
        param.put("cghiCtIds",  cghiCtIdArray);
        param.put("exportUser", exportUser);
        param.put("period", period);
        List<RegUser> userlist = cpdGroupRegHoursHistoryMapper.getUserDetailForExcel(param);
        List<CpdGroup> cpdGroupList = cpdGroupMapper.searchByType(param);
        List<CpdGroupVO> cpdGroupVOList = new ArrayList<CpdGroupVO>();

        //当前年份   周期
        int now_period = DateUtil.getInstance().getDateYear(DateUtil.getInstance().getDate());
        
        for (int i = 0; i < cpdGroupList.size(); i++) {
            CpdGroupVO cpdGroupVO = new CpdGroupVO();
            cpdGroupVO.setCg_id(cpdGroupList.get(i).getCg_id());
            cpdGroupVO.setCg_alias(cpdGroupList.get(i).getCg_alias());
            cpdGroupVOList.add(cpdGroupVO);
        }
        
        List<List> list = new ArrayList<List>();
        for (int j = 0; j < userlist.size(); j++) {
            RegUser regUser = userlist.get(j);
            int rownum = 0;
            
            //学员信息和未完成核心时数与非核心时数
            List<Object> list2 = new ArrayList<Object>();
            list2.add(regUser.getUsr_display_bil());
            list2.add(regUser.getUsr_ste_usr_id());
            list2.add(regUser.getUpt_title()==null ? "--": regUser.getUpt_title());
            list2.add(regUser.getUsg_display_bil());

            boolean isNotOutStanding = false; //是否存在超时
            for (int i = 0; i <cpdGroupList.size(); i++) {
                Map<String, Object> param1 = new HashMap<String,Object>();
                param1.put("usr_ent_id", regUser.getUsr_ent_id());
                param1.put("period", period);
                param1.put("cg_id", cpdGroupList.get(i).getCg_id());
                CpdGroupRegistration cpdGroupRegistration =  null;
                CpdPeriodVO cpdPeriodVO1 = cpdUtilService.getCurrentPeriod(cpdGroupList.get(i).getCpdType().getCt_id());
                if(period == now_period || cpdPeriodVO1.getPeriod()==period){
                    //当前周期
                    cpdGroupRegistration =  cpdGroupRegistrationMapper.getHoursDate(param1);
                }else{
                    //历史记录
                    CpdPeriodVO cpdPeriodVO = cpdUtilService.getPeriodByYear(period,cpdGroupList.get(i).getCpdType().getCt_id());
                    cpdGroupRegistration =  cpdGroupRegistrationMapper.getHoursDateHistory(param1);//去历史记录查
                    /*if(cpdGroupRegistration==null){
                      在历史记录查不到，可能是确实没有数据，也可能是选中的评估年份中该周期的时间包含到了当前时间，比如当前时间是2017-7-31，选中的评估年份是2016，
                      而大牌的开始月份是8月，则大牌在2016评估年份的周期是2016-8-1到2017-7-31，刚好包含了当前时间，这时候就应该去当前周期表查，而不是历史表
                      cpdGroupRegistration =  cpdGroupRegistrationMapper.getHoursDate(param1); //当前周期
                      if(cpdGroupRegistration!=null){
                          if(!cpdGroupRegistration.getCpdGroupRegHours().getCgrh_create_datetime().before(cpdPeriodVO.getStartTime()) ){
                              cpdGroupRegistration = null;//如果该注册记录的创建时间在选中周期的开始时间之后，那么这条数据在历史记录是不可能存在的，所以在当前周期查出来也无效
                         }
                      }
                    }*/
                }
                
                if(cpdGroupRegistration!=null){
                    param1.put("start_time", cpdGroupRegistration.getCpdGroupRegHours().getCgrh_cal_start_date());
                    param1.put("end_time", cpdGroupRegistration.getCpdGroupRegHours().getCgrh_cal_end_date());
                    
                    CpdHourVO cpdHourVO = cpdLrnAwardRecordMapper.sumAwardHoursOutding(param1);
                    //计算核心时数是否足够
                    float coreHousrOut = 0f;
                    float coreHousrOutForNom = 0f;//增加一个核心时数的复制品，给非核心时数进行借时数使用；
                    if(cpdHourVO!=null){
                        coreHousrOut =  FormatUtil.getInstance().scaleFloat(cpdGroupRegistration.getCpdGroupRegHours().getCgrh_execute_core_hours()-cpdHourVO.getTotle_award_core_hours(), 2, BigDecimal.ROUND_HALF_DOWN);
                        coreHousrOutForNom = FormatUtil.getInstance().scaleFloat(cpdGroupRegistration.getCpdGroupRegHours().getCgrh_execute_core_hours()-cpdHourVO.getTotle_award_core_hours(), 2, BigDecimal.ROUND_HALF_DOWN);
                    }else{
                        coreHousrOut = cpdGroupRegistration.getCpdGroupRegHours().getCgrh_execute_core_hours();
                        coreHousrOutForNom = cpdGroupRegistration.getCpdGroupRegHours().getCgrh_execute_core_hours();
                    }
                    if(coreHousrOut<=0){
                        coreHousrOut = 0.0f;
                    }else{
                        for (int k = 0; k <cpdGroupVOList.size(); k++) {
                            if(cpdGroupVOList.get(k).getCg_id().equals(cpdGroupList.get(i).getCg_id())){
                                int learnCount = cpdGroupVOList.get(k).getLearncount()==null? 0:cpdGroupVOList.get(k).getLearncount();
                                cpdGroupVOList.get(k).setLearncount(learnCount+1);
                                break;
                            }
                        }
                        isNotOutStanding = true;
                    }
                    list2.add(coreHousrOut);
                    
                    float nonCoreHousrOut = 0f;
                    if(cpdGroupRegistration.getCpdGroup().getCg_contain_non_core_ind()==1){
                        //计算非核心时数是否足够
                        if(cpdHourVO!=null){
                            if(null!=cpdGroupRegistration.getCpdGroupRegHours().getCgrh_execute_non_core_hours() && cpdGroupRegistration.getCpdGroupRegHours().getCgrh_execute_non_core_hours()!=0.0f){
                                nonCoreHousrOut = FormatUtil.getInstance().scaleFloat( cpdGroupRegistration.getCpdGroupRegHours().getCgrh_execute_non_core_hours()-cpdHourVO.getTotle_award_non_core_hours(), 2, BigDecimal.ROUND_HALF_DOWN);
                            }else{
                                nonCoreHousrOut = 0;
                            }
                           
                        }else{
                            nonCoreHousrOut = cpdGroupRegistration.getCpdGroupRegHours().getCgrh_execute_non_core_hours()==null? 0.0f:cpdGroupRegistration.getCpdGroupRegHours().getCgrh_execute_non_core_hours();
                        }
                        if(nonCoreHousrOut<=0){
                            nonCoreHousrOut = 0.0f;
                        }else if(nonCoreHousrOut>0 && coreHousrOutForNom<=0){//当完成的非核心时数不足时，并且完成的核心时数有多，从完成的核心时数借
                            nonCoreHousrOut = nonCoreHousrOut+coreHousrOutForNom;
                            if(nonCoreHousrOut<=0){
                                nonCoreHousrOut = 0.0f;
                            }else{
                                if(coreHousrOutForNom<=0){
                                    for (int k = 0; k <cpdGroupVOList.size(); k++) {
                                        if(cpdGroupVOList.get(k).getCg_id().equals(cpdGroupList.get(i).getCg_id())){
                                            int learnCount = cpdGroupVOList.get(k).getLearncount()==null? 0:cpdGroupVOList.get(k).getLearncount();
                                            cpdGroupVOList.get(k).setLearncount(learnCount+1);
                                            break;
                                        }
                                    }
                                }
                                isNotOutStanding = true;
                            }
                        }
                        list2.add(nonCoreHousrOut);
                    }
                }else{
                    list2.add("--");
                    if(cpdGroupList.get(i).getCg_contain_non_core_ind()==1){
                        list2.add("--");
                    }
                }
                
            }
            //list.add(obj);
            if(isNotOutStanding == true){
                list.add(list2);
            }
            isNotOutStanding = false;
        }
        map.put(0, list);
        map.put(1, cpdGroupVOList);
        return map;
        
    }
    
    

    /**
     * 进行数据导出
     * @param prof
     * @param wizbini
     * @param fileNameEmail  //当fileNameEmail为null的时候，是从页面上导出报表，当fileNameEmail不为null的时候，为发送邮件时生成报表，先给报表起名
     * @param emsg_id    //当emsg_id为0的时候，是从页面上导出报表，当emsg_id不为0的时候，为发送邮件时生成报表，获取邮件内容id
     * @return
     * @throws SQLException
     */
    public String expor(loginProfile prof, WizbiniLoader wizbini,Map<Integer, List> map, long[] cghiCtIdArray,int period,String fileNameEmail,long emsg_id) throws SQLException{
        String sheetName = LabelContent.get(prof.cur_lan, "label_core_cpt_d_management_162");
        // 创建一个webbook
        HSSFWorkbook wb = new HSSFWorkbook(); 
        
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
        titlefont.setFontHeightInPoints((short) 14);// 设置字体大小
        titlefont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); //居中
        titleStyle.setFont(titlefont);
        
        
        
        int rowIndex = 0;
        HSSFRow row = sheet.createRow(rowIndex++);
        HSSFCell cellPrivate = row.createCell(0);  
        cellPrivate.setCellValue(LabelContent.get(prof.cur_lan, "label_core_cpt_d_management_141")); 

        row = sheet.createRow(rowIndex++); 
        row.setHeightInPoints(30);
        HSSFCell cell = row.createCell(0);  
        cell.setCellValue(LabelContent.get(prof.cur_lan, "label_core_cpt_d_management_142")); 
        cell.setCellStyle(titleStyle); 
        
        
        row = sheet.createRow(rowIndex++); 
        row.setHeightInPoints(20);
        HSSFCell  cell1 = row.createCell(0);  
        cell1.setCellValue(LabelContent.get(prof.cur_lan, "label_core_cpt_d_management_143"));   
        cell1.setCellStyle(titleStyle); 

        row = sheet.createRow(rowIndex++); 
        row.setHeightInPoints(20);
        HSSFCell  cellYear = row.createCell(0);  
        cellYear.setCellValue(LabelContent.get(prof.cur_lan, "label_core_cpt_d_management_144")+period);   
        cellYear.setCellStyle(titleStyle); 
        
        row = sheet.createRow(rowIndex++); 
        HSSFCell  cell2 = row.createCell(0);  
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        cell2.setCellValue(LabelContent.get(prof.cur_lan, "label_core_cpt_d_management_145")+format.format(getDate()));   
        cell2.setCellStyle(style); 

        sheet.addMergedRegion(new Region(0,(short)0,0,(short)10)); 
        sheet.addMergedRegion(new Region(1,(short)0,1,(short)10)); 
        sheet.addMergedRegion(new Region(2,(short)0,2,(short)10)); 
        sheet.addMergedRegion(new Region(3,(short)0,3,(short)10)); 
        sheet.addMergedRegion(new Region(4,(short)0,4,(short)10)); 
        
        Map<String, Object> param = new HashMap<String,Object>();
        param.put("cghiCtIds", cghiCtIdArray);
        List<CpdGroup> cpdGroupList = cpdGroupMapper.searchByType(param);
        
      //生成统计信息列表 头部
        List<String> labelsList = new ArrayList<String>();
        //labelsList.add(LabelContent.get(prof.cur_lan, "label_core_cpt_d_management_123"));
        labelsList.add(LabelContent.get(prof.cur_lan, "label_core_cpt_d_management_59"));
        labelsList.add(LabelContent.get(prof.cur_lan, "label_core_cpt_d_management_58"));
        labelsList.add(LabelContent.get(prof.cur_lan, "label_core_cpt_d_management_147"));
        labelsList.add(LabelContent.get(prof.cur_lan, "label_core_cpt_d_management_148"));
        for (int i = 0; i < cpdGroupList.size(); i++) {
            if(cpdGroupList.get(i).getCg_contain_non_core_ind()==1){
                labelsList.add(cpdGroupList.get(i).getCg_alias()+"\r\n Core");
                labelsList.add(cpdGroupList.get(i).getCg_alias()+"\r\n Non-Core");
            }else{
                labelsList.add(cpdGroupList.get(i).getCg_alias()+"\r\n Core");
            }
        }
        String[] labels = new String[labelsList.size()] ;
        labels = labelsList.toArray(labels);
        getCell(sheet, rowIndex++, wb ,prof.cur_lan,labels); 
        
        //未完成时数学员数据
        List<List> list = map.get(0);
        for (int i = 0; i < list.size(); i++) {
            HSSFRow rowContent = sheet.createRow(rowIndex++);  
            //Object[] obj = list.get(i);
            List<Object> list1 = list.get(i);
            for (int j = 0; j < list1.size(); j++) {
                HSSFCell cellContent = rowContent.createCell(j);  
                cellContent.setCellValue(list1.get(j).toString());  
            }
        }
        

        row = sheet.createRow(rowIndex++); 
        row = sheet.createRow(rowIndex++); 
        row = sheet.createRow(rowIndex++); 
        row.setHeightInPoints(20);
        HSSFCell  cell3 = row.createCell(0);  
        cell3.setCellValue(LabelContent.get(prof.cur_lan, "label_core_cpt_d_management_146"));   
        cell3.setCellStyle(style); 
        sheet.addMergedRegion(new Region(rowIndex-1,(short)0,rowIndex-1,(short)10)); 
        //小牌的未完成时数学员数量
        List<CpdGroupVO> cpdGroupVOList = map.get(1);
        for (int i = 0; i < cpdGroupVOList.size(); i++) {
            HSSFRow rowContent = sheet.createRow(rowIndex++);  
            
            HSSFCell cellContent1 = rowContent.createCell(0);  
            cellContent1.setCellValue(cpdGroupVOList.get(i).getCg_alias());  
            
            HSSFCell cellContent2 = rowContent.createCell(1);  
            cellContent2.setCellValue(cpdGroupVOList.get(i).getLearncount()==null? "0":cpdGroupVOList.get(i).getLearncount().toString());  
        }
        
        //备注
        //CpdUtilService cpdUtilService = new CpdUtilService();
        Calendar c = Calendar.getInstance();
        int nowYear = c.get(Calendar.YEAR);
        String remark = "";
        remark = cpdUtilService.getRemarkByPeriod(period, CpdReportRemark.OUTSTANDING_REMARK_CODE);
        row = sheet.createRow(rowIndex++); 
        row = sheet.createRow(rowIndex++); 

        HSSFCellStyle styleRemark = wb.createCellStyle();  
        styleRemark.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
        styleRemark.setWrapText(true);//先设置为自动换行   
        HSSFCell  cellRemark = row.createCell(0);  
        cellRemark.setCellValue(new HSSFRichTextString(remark));   
        cellRemark.setCellStyle(styleRemark); 
        sheet.addMergedRegion(new Region(rowIndex-1,(short)0,rowIndex+10,(short)10)); 
        
        
        
        //生成文件名   并 将文件存到指定位置  
        String fileName = "";
        if(null != fileNameEmail){
            fileName = fileNameEmail;
            try{  
                String basePath =  wizbini.getWebDocRoot()+dbUtils.SLASH + Message.MSG_ATTACHMENT_PATH + dbUtils.SLASH + emsg_id;
                File distPath = new File(basePath);
                if (!distPath.exists()) {
                    distPath.mkdirs();
                }
                FileOutputStream fout = new FileOutputStream(basePath+ dbUtils.SLASH +fileName);  
                wb.write(fout);  
                fout.close();  
            }catch (Exception e) {  
                e.printStackTrace();  
            }
        }else{
            fileName = "CPTD_Outstanding_Hours_Report_"+new SimpleDateFormat("yyyyMMddHHmmssSSS") .format(new Date())+".xls";
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
        }
        
        return fileName;
        
    }
    
    
    //写入报表头部
    private void getCell(HSSFSheet sheet,int rowIndex,HSSFWorkbook wb ,String labellang,String[] labels){
        HSSFRow row = sheet.createRow(rowIndex);  
        HSSFCellStyle style = wb.createCellStyle();  
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
            style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            style.setWrapText(true);//先设置为自动换行   
            style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
            //style.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);
            sheet.createRow(rowIndex).setHeight((short) 800);
            //边框颜色 黑色
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框    
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框    
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框    
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框 
             for(int i = 0; i<labels.length; i++){
                     HSSFCell cell = row.createCell(i);  
                     sheet.setColumnWidth(i, (short) 5000);
                         cell.setCellValue(new HSSFRichTextString(labels[i]));  
                         cell.setCellStyle(style);  
                      /*if(labels[i]==""){
                          sheet.addMergedRegion(new Region(rowIndex,(short)(i-1),rowIndex,(short)i)); 
                      }*/
             }
    }
    
    
	
}
