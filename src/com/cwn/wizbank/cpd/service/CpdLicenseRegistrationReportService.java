package com.cwn.wizbank.cpd.service;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.cpd.vo.CpdPeriodVO;
import com.cwn.wizbank.entity.CpdGroup;
import com.cwn.wizbank.entity.CpdGroupRegistration;
import com.cwn.wizbank.entity.CpdRegistration;
import com.cwn.wizbank.entity.CpdReportRemark;
import com.cwn.wizbank.entity.CpdType;
import com.cwn.wizbank.persistence.CpdGroupMapper;
import com.cwn.wizbank.persistence.CpdGroupRegHoursHistoryMapper;
import com.cwn.wizbank.persistence.CpdRegistrationMapper;
import com.cwn.wizbank.persistence.CpdTypeMapper;
import com.cwn.wizbank.persistence.RegUserMapper;
import com.cwn.wizbank.services.BaseService;
import com.cwn.wizbank.utils.DateUtil;
import com.cwn.wizbank.utils.LabelContent;

@Service
public class CpdLicenseRegistrationReportService  extends BaseService<CpdRegistration>{
	
	@Autowired
	RegUserMapper regUserMapper;
	@Autowired
	CpdGroupRegHoursHistoryMapper cpdGroupRegHoursHistoryMapper;
	@Autowired
	CpdGroupMapper cpdGroupMapper;
	
	@Autowired
	CpdRegistrationMapper cpdRegistrationMapper;
	
	@Autowired
	CpdUtilService cpdUtilService;
	
	@Autowired
	CpdTypeMapper cpdTypeMapper;
	
	
	/**
	 * 导出报表  返回报表保存的路径
	 * @param prof
	 * @param cr
	 * @param wizbini
	 * @return
	 * @throws SQLException
	 */
	public String export(loginProfile prof, CpdRegistration cr, WizbiniLoader wizbini) throws SQLException{
		String sheetName = "Sheet1";
		// 创建一个webbook
        HSSFWorkbook wb = new HSSFWorkbook(); 
        //导出  
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
        
        //写入报表名称 以及导出时间等信息
        int rowIndex = setConditionToheard(sheet,style,titleStyle,prof.cur_lan);
        
		//获取所有大牌以及大牌下的小牌照。（小牌属性cg_display_in_report_ind（是否在报表中显示） 为否也要显示）
        Map<String ,Object> map = new HashMap<String, Object>();
        map.put("cpdGroupIds", cr.getExportCpdTypeIds());
		List<CpdType>  cpdTypelist  = cpdTypeMapper.getAllCptTypeAndGroup(map);
		
		//生成报表的表格头部
		createListHeader(cpdTypelist,sheet,rowIndex,wb,prof.cur_lan);
		
		//条件
        Map<String,Object> cr_map =new HashMap<String, Object>();
        cr_map.put("exportUserIds", cr.getExportUserIds());
        cr_map.put("exportGroupIds", cr.getExportGroupIds());
        cr_map.put("tc_independent", wizbini.cfgSysSetupadv.isTcIndependent());
        cr_map.put("my_top_tc_id", prof.my_top_tc_id);
        cr_map.put("usr_ent_id", prof.usr_ent_id);
        cr_map.put("current_role", prof.current_role);
        //当前角色是否与培训中心关联
        cr_map.put("is_role_tc_ind", AccessControlWZB.isRoleTcInd(prof.current_role));
        //获取报表所需的主体内容
        List<CpdRegistration> list = cpdRegistrationMapper.getCpdLicenseRegistrationReport(cr_map);
        //写入
        //当前年份   周期
        int now_period = DateUtil.getInstance().getDateYear(DateUtil.getInstance().getDate());
        writeReportContent(list, rowIndex+1, cpdTypelist, sheet, now_period,wb);
        
		//生成文件名   并 将文件存到指定位置  
        String fileName = "cpdt_license_registration_report_"+new SimpleDateFormat("yyyyMMddHHmmssSSS") .format(new Date())+".xls";
         
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
	
	
	
	
	
	/**
	 * 处理报表主体内容 并写入
	 * @param list  学员信息集合
	 * @param rowIndex  从第rowIndex+1行开始写入数据
	 * @param cpdTypelist 所需要显示的列（大牌 A+ A下所有小牌 + 大牌 B+ B下所有小牌    ）
	 * @param sheet
	 * @param period 当前年份的周期  取报表remark
	 */
	private void writeReportContent(List<CpdRegistration> list,int rowIndex,List<CpdType>  cpdTypelist,HSSFSheet sheet,int period,HSSFWorkbook wb){
		 for(CpdRegistration cr : list){  //遍历学员集合  每个学员写入一行
			 
			 List<CpdRegistration> new_cr_list = new ArrayList<CpdRegistration>();  
			 //报表显示的大牌
			 for(CpdType cpdType : cpdTypelist){  //列头 按照牌照的排序规则依次显示
				 CpdRegistration crt = getUseCpdRegistration(cpdType,cr);
				// List<CpdGroupRegistration> new_cgr_list = new ArrayList<CpdGroupRegistration>();
				// crt.setCpdGroupRegistrationList(new_cgr_list);
				 new_cr_list.add(crt);
			 }
			
			cr.setCpdRegistrationList(new_cr_list);
			
			boolean hasCr = false;//判断该用户是否存在注册记录
            for(CpdRegistration ct : cr.getCpdRegistrationList()){
                if(null != ct.getCr_reg_datetime()){
                    for(CpdGroupRegistration cg : ct.getCpdGroupRegistrationList()){
                        if(null != cg.getCgr_initial_date()){
                            hasCr = true;
                            break;
                        }
                    }
                }

                
            }
            //如果hasCr为true，就说明该用户在牌照中有符合条件的注册记录
            if(hasCr){
                HSSFRow row = sheet.createRow(++rowIndex);  
                //创建单元格，并设置值  
                createAndSetCellNowReport(row,cr);
            }
	    	
		 }	
		
		    //写入报表备注信息
	        String remaek = cpdUtilService.getRemarkByPeriod(period,CpdReportRemark.LICENSE_REGISTRATION_CODE);
	        if(null != remaek && !remaek.equals("")){
	        	HSSFRow row = sheet.createRow(rowIndex+2);
	           // row.createCell(0).setCellValue(remaek); 
	        	HSSFCellStyle cellStyle=wb.createCellStyle();   
	        	cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
	        	cellStyle.setWrapText(true);  
	        	
	        	HSSFCell  cellRemark = row.createCell(0);  
	            cellRemark.setCellValue(new HSSFRichTextString(remaek));   
	            cellRemark.setCellStyle(cellStyle); 
	             
	            sheet.addMergedRegion(new Region(rowIndex+2,(short)0,rowIndex+10,(short)10)); 
	        }
			
		}
	
	
	
	/**
	 *  获取报表列表头部内容 
	 * @param sheet
	 * @param rowIndex
	 * @param wb
	 * @param labels
	 * @param topIndex  第topIndex层头部列表
	 */
	private void getCell(HSSFSheet sheet,int rowIndex,HSSFWorkbook wb ,String[] labels,int topIndex){
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
   	  if(topIndex != 1){
   		 for(int i = 0; i<labels.length; i++){
   		     sheet.setColumnWidth(4+i, 4000);  //设置牌照列宽度
   			 HSSFCell cell = row.createCell(i);  
		         cell.setCellValue(labels[i]);  
		         cell.setCellStyle(style);  
   	    }
   	  }else{
   		for(int i = 0,j = 4,h = 0; i<labels.length*2+4; i++){//i 创建的第i个单元格  j 在第j个单元格中写入数据   h 当前labels中第h个值 
		     sheet.setColumnWidth(4+i, 4000);  //设置牌照列宽度
			 HSSFCell cell = row.createCell(i);
			 if(i == j){   
				cell.setCellValue(labels[h]); 
				h++;
				j = j+2;
			    sheet.addMergedRegion(new Region(3,(short)(i),3,(short)(i+1))); 
			 }
	         cell.setCellStyle(style);  
	    }
   	  }
   	  
	}
 
	


	/**
	 * 创建单元格 写入数据  
	 * @param row
	 * @param cr
	 */
	private void createAndSetCellNowReport(HSSFRow row,CpdRegistration cr){
            int index = 0;
            row.createCell(index++).setCellValue(cr.getUser().getUsr_ste_usr_id());  
            row.createCell(index++).setCellValue(cr.getUser().getUsr_display_bil()); 
            if(null == cr.getUser().getUpt_title() || cr.getUser().getUpt_title().equals("")){
                row.createCell(index++).setCellValue("--");
            }else{
                row.createCell(index++).setCellValue(cr.getUser().getUpt_title());
            }
            row.createCell(index++).setCellValue(cr.getUser().getUsg_display_bil());
            for(CpdRegistration ct : cr.getCpdRegistrationList()){
                if(null != ct.getCr_reg_number()){
                    row.createCell(index++).setCellValue(ct.getCr_reg_number()); //大牌注册编号
                }else{
                    row.createCell(index++).setCellValue("--"); 
                }
                
                if(null != ct.getCr_reg_datetime()){
                    row.createCell(index++).setCellValue(DateUtil.getInstance().dateToString(ct.getCr_reg_datetime(), DateUtil.patternH)); //大牌注册时间
                }else{
                    row.createCell(index++).setCellValue("--"); 
                }
                //该大牌下所显示的小牌
                    for(CpdGroupRegistration cgr : ct.getCpdGroupRegistrationList()){
                        if(null != cgr.getCgr_initial_date()){
                            row.createCell(index++).setCellValue(DateUtil.getInstance().dateToString(cgr.getCgr_initial_date(), DateUtil.patternH)); //小牌注册时间
                        }else{
                            row.createCell(index++).setCellValue("--"); 
                        }
                        if(null != cgr.getCgr_expiry_date()){
                            row.createCell(index++).setCellValue(DateUtil.getInstance().dateToString(cgr.getCgr_expiry_date(), DateUtil.patternH)); //小牌过期时间 
                        }else{
                            row.createCell(index++).setCellValue("--"); 
                        }
                    }
                
            }
			
	        
		}
	
	/**
	 * 报表页头部说明部分
	 * @param sheet
	 * @param style
	 * @param titleStyle
	 * @param lan
	 * @return
	 */
	private Integer setConditionToheard(HSSFSheet sheet,HSSFCellStyle style,HSSFCellStyle titleStyle,String lan){
			int rowIndex = 0;
			HSSFRow row = sheet.createRow(rowIndex++);
			row.setHeightInPoints(20);
			HSSFCell cell = row.createCell(0);  
	        cell.setCellValue(LabelContent.get(lan, "label_core_cpt_d_management_163"));   // License Registration Report
	        cell.setCellStyle(titleStyle); 
	        rowIndex++;
			row = sheet.createRow(rowIndex++); 
			HSSFCell explainCell = row.createCell(0);  
			explainCell.setCellValue(LabelContent.get(lan, "label_core_cpt_d_management_145"));   //Report Printing Date
			explainCell.setCellStyle(style); 
			row.createCell(1).setCellValue( DateUtil.getInstance().dateToString(DateUtil.getInstance().getDate(), DateUtil.patternH) );
		return rowIndex;	
	}
	
	
	/**
	 * 生成报表的表格头部
	 * @param cpdTypelist  所需要显示的牌照集合
	 * @param sheet
	 * @param rowIndex
	 * @param wb
	 * @param cur_lan 语言
	 */
	private void createListHeader(List<CpdType>  cpdTypelist,HSSFSheet sheet,int rowIndex,HSSFWorkbook wb,String cur_lan){
		//生成列表头部 1部分 （所有的大牌以及小牌名称）
		 List<String> toplabelsList = new ArrayList<String>();
		 for(CpdType ct : cpdTypelist){
			 toplabelsList.add(ct.getCt_license_alias());  //大牌的名称
			 if(null != ct.getCpdGrouplist() && ct.getCpdGrouplist().size() > 0){  //当前大牌下所有小牌的名称
				 for(CpdGroup cg : ct.getCpdGrouplist()){
					 toplabelsList.add(cg.getCg_alias());   
				 }
			 }
		 }
	       
       String[] labels = new String[toplabelsList.size()] ;
       labels = toplabelsList.toArray(labels);
       getCell(sheet, rowIndex, wb,labels,1); 
		
       //生成列表头部 2部分 （用户信息列部分）
       List<String> labelsList = new ArrayList<String>();
       labelsList.add(LabelContent.get(cur_lan, "label_core_cpt_d_management_58"));  //用户名;
       labelsList.add(LabelContent.get(cur_lan, "label_core_cpt_d_management_59"));  //全名
       labelsList.add(LabelContent.get(cur_lan, "label_core_cpt_d_management_147")); //岗位
       labelsList.add(LabelContent.get(cur_lan, "label_core_cpt_d_management_148")); //用户组
       for(CpdType ct : cpdTypelist){
       	labelsList.add(LabelContent.get(cur_lan, "label_core_cpt_d_management_60"));  //大牌  注册号码
       	labelsList.add(LabelContent.get(cur_lan, "label_core_cpt_d_management_61")); //注册时间
			 if(null != ct.getCpdGrouplist() && ct.getCpdGrouplist().size() > 0){  //当前大牌下所有小牌
				 for(CpdGroup cg : ct.getCpdGrouplist()){
					 labelsList.add(LabelContent.get(cur_lan, "label_core_cpt_d_management_70")); //开始时间  
					 labelsList.add(LabelContent.get(cur_lan, "label_core_cpt_d_management_71")); //结束时间
				 }
			 }
		 }
       String[] secondlabels = new String[labelsList.size()] ;
       secondlabels = labelsList.toArray(labels);
       getCell(sheet, rowIndex+1, wb,secondlabels,2); 
	}
	
	/**
	 * 学员当前报表需要显示的注册记录信息
	 * @param cpdType  需要显示的牌照
	 * @param cr  注册信息
	 * @return
	 */
	private CpdRegistration getUseCpdRegistration(CpdType cpdType,CpdRegistration cr){
		CpdRegistration crt = new CpdRegistration();
		List<CpdGroupRegistration> new_cgr_list = new ArrayList<CpdGroupRegistration>();
		for(CpdGroup cg : cpdType.getCpdGrouplist()){  //初始为空，当用户没有注册该牌照时，仍然保留
			 CpdGroupRegistration cgr = new CpdGroupRegistration();
			 new_cgr_list.add(cgr);
		 }
		 for(CpdRegistration  usr_cr : cr.getCpdRegistrationList()){  //循环当前学学员是否注册了该大牌
		     try {
                 CpdPeriodVO cpdPeriodVO= cpdUtilService.getCurrentPeriod(cpdType.getCt_id());
    			 if(usr_cr.getCr_ct_id() == cpdType.getCt_id()){   //学员注册了大牌，则替换大牌对象以及对应的小牌集合
    			     
    			     //如果大牌的注册除牌时间为null，则这个大牌注册记录一定有效
    			     //如果大牌的注册除牌时间不为null，并且除牌时间大于该大牌当前周期的开始时间，则说明该牌照有效（可能在当前周期会除牌，但是依然显示，只有在以往周期除牌才不显示记录）
    			     if(usr_cr.getCr_de_reg_datetime() == null  || cpdPeriodVO.getStartTime().getTime() < usr_cr.getCr_de_reg_datetime().getTime()){
                             boolean bool = true;
                             //如果大牌的注册时间大于当前周期的结束时间，则说明该注册记录是未来才生效，不包含当前周期，不进行统计
                             if(usr_cr.getCr_reg_datetime().getTime()>cpdPeriodVO.getEndTime().getTime()) {
                                 bool = false;
                             }
                             if(bool){
                                 crt.setCr_reg_number(usr_cr.getCr_reg_number());
                                 crt.setCr_reg_datetime(usr_cr.getCr_reg_datetime());
                                 new_cgr_list = new ArrayList<CpdGroupRegistration>();
                                 for(CpdGroup cg : cpdType.getCpdGrouplist()){  //当前大牌所需要显示的所有小牌
                                      CpdGroupRegistration cgr = new CpdGroupRegistration();
                                      for(CpdGroupRegistration  usr_cri : usr_cr.getCpdGroupRegistrationList()){  //循环当前学学员是否注册了该小牌
                                          if(cg.getCg_id().equals(usr_cri.getCgr_cg_id())){
                                              //如果小牌的注册除牌时间为null，则这个小牌注册记录一定有效
                                              //如果小牌的注册除牌时间不为null，既是有除牌时间，但是除牌时间大于当前周期的开始时间，就说明这个注册记录不是在以往周期除牌，可以在报表显示
                                              if(usr_cri.getCgr_expiry_date() == null || cpdPeriodVO.getStartTime().getTime() < usr_cri.getCgr_expiry_date().getTime()){
                                                  if(usr_cri.getCgr_initial_date().getTime()<cpdPeriodVO.getEndTime().getTime()) {
                                                      cgr.setCgr_initial_date(usr_cri.getCgr_initial_date()); 
                                                      cgr.setCgr_expiry_date(usr_cri.getCgr_expiry_date());
                                                      break;
                                                  }
                                                  
                                              }
                                              
                                          }
                                      }
                                      new_cgr_list.add(cgr);
                                 }
                                 break;
                             }
    			            
    			     }
    			 }
		     } catch (Exception e) {
	                e.printStackTrace();
	         }
			 
   	   }
	  crt.setCpdGroupRegistrationList(new_cgr_list);
	  return crt;
	}
	
}
