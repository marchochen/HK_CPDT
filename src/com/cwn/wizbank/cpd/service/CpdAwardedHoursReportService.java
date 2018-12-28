package com.cwn.wizbank.cpd.service;

import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.cpd.vo.CpdPeriodVO;
import com.cwn.wizbank.entity.*;
import com.cwn.wizbank.persistence.*;
import com.cwn.wizbank.services.BaseService;
import com.cwn.wizbank.utils.DateUtil;
import com.cwn.wizbank.utils.LabelContent;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CpdAwardedHoursReportService  extends BaseService<CpdGroupRegHoursHistory>{
	
	@Autowired
	RegUserMapper regUserMapper;
	
	@Autowired
	CpdGroupRegHoursHistoryMapper cpdGroupRegHoursHistoryMapper;
	
	@Autowired
	CpdGroupMapper cpdGroupMapper;
	
	@Autowired
	CpdLrnAwardRecordMapper cpdLrnAwardRecordMapper;
	
	@Autowired
	CpdUtilService cpdUtilService;
	
	@Autowired
	CpdRegistrationMapper cpdRegistrationMapper;
	
	@Autowired
	CpdGroupRegistrationMapper cpdGroupRegistrationMapper;
	
	/**
	 * 导出报表  返回报表保存的路径
	 * @param prof
	 * @param cghi
	 * @param wizbini
	 * @return
	 * @throws SQLException
	 */
	public String export(loginProfile prof, CpdGroupRegHoursHistory cghi, WizbiniLoader wizbini) throws SQLException{
		String sheetName = "report";
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
		
        
        int rowIndex =  setConditionToheard(sheet,style,titleStyle,cghi,prof.cur_lan);
        
        //--------------
        //获取条件选择的所有生效小牌(cg_display_in_report_ind设置1可在报表中显示)
        Map<String ,Object> map = new HashMap<String, Object>();
        map.put("cpdGroupIds", cghi.getExportCpdTypeIds());
		List<CpdGroup>  grouplist = cpdGroupMapper.getAllOrder(map);
        
        //生成报表列头
        List<String> labelsList = new ArrayList<String>();
        labelsList.add(LabelContent.get(prof.cur_lan, "label_core_cpt_d_management_58"));  //用户名;
        labelsList.add(LabelContent.get(prof.cur_lan, "label_core_cpt_d_management_59"));  //全名
        labelsList.add(LabelContent.get(prof.cur_lan, "label_core_cpt_d_management_147")); //岗位
        labelsList.add(LabelContent.get(prof.cur_lan, "label_core_cpt_d_management_148")); //用户组
        for(int i =0; i < grouplist.size(); i++){
        	labelsList.add(grouplist.get(i).getCg_alias()+" "+LabelContent.get(prof.cur_lan, "label_core_cpt_d_management_273"));
        	if(grouplist.get(i).getCg_contain_non_core_ind() == 1){
        		labelsList.add(grouplist.get(i).getCg_alias()+" "+LabelContent.get(prof.cur_lan, "label_core_cpt_d_management_274"));
        	}
        }
        String[] labels = new String[labelsList.size()] ;
        labels = labelsList.toArray(labels);
        getCell(sheet, rowIndex, wb,labels); 
        
        //条件
        Map<String,Object> gr_map =new HashMap<String, Object>();
        gr_map.put("exportUserIds", cghi.getExportUserIds());
        gr_map.put("exportGroupIds", cghi.getExportGroupIds());
        gr_map.put("exportCpdTypeIds", cghi.getExportCpdTypeIds());
        gr_map.put("period", cghi.getCghi_period());
        gr_map.put("sortOrderName", cghi.getSortOrderName());
        gr_map.put("sortOrderBy", cghi.getSortOrderBy());
        gr_map.put("tc_independent", wizbini.cfgSysSetupadv.isTcIndependent());
        gr_map.put("my_top_tc_id", prof.my_top_tc_id);
        gr_map.put("usr_ent_id", prof.usr_ent_id);
        gr_map.put("current_role", prof.current_role);
        
        //当前角色是否与培训中心关联
        gr_map.put("is_role_tc_ind", AccessControlWZB.isRoleTcInd(prof.current_role));
        
        
       //当前年份   周期
        int now_period = DateUtil.getInstance().getDateYear(DateUtil.getInstance().getDate());
        
       //写入内容
       //if(cghi.getCghi_period() == now_period ){
        	//选中当前周期     
            List<CpdLrnAwardRecord> now_list = cpdLrnAwardRecordMapper.getNowGroupRegHoursReport(gr_map);
       //     rowIndex = writeNowReportContent(now_list,rowIndex,grouplist,sheet,cghi.getCghi_period(),wb);
       //}else{
       //当选择导出的周期 不是当前年份的周期是 需要查询历史记录表数据
            
        List<CpdGroupRegHoursHistory> his_list = new ArrayList<CpdGroupRegHoursHistory>();
        if(cghi.getCghi_period() != now_period ){
        	 //获取学员信息 history   
             his_list = cpdGroupRegHoursHistoryMapper.getGroupRegHoursReport(gr_map);
             //rowIndex = writeReportContent(his_list,rowIndex,grouplist,sheet,cghi.getCghi_period(),wb);
            }
       //}
        
        //
        if(null != his_list && his_list.size() > 0){
        	//处理  当前时间存在牌照周期不一致的数据
        	processingList(his_list,now_list,cghi.getCghi_period());
        	sortStringMethod(his_list,cghi.getSortOrderName(),cghi.getSortOrderBy());
        	//rowIndex = writeNowReportContent(now_list,rowIndex,grouplist,sheet,cghi.getCghi_period(),wb);
        	rowIndex = writeReportContent(his_list,rowIndex,grouplist,sheet,cghi.getCghi_period(),wb);
        }else{
        	//只打印当前年份周期数据
        	rowIndex = writeNowReportContent(now_list,rowIndex,grouplist,sheet,cghi.getCghi_period(),wb);
        }
        
        
        wRemaek(cghi.getCghi_period(),sheet,rowIndex,wb);
        
		//生成文件名   并 将文件存到指定位置  
        String fileName = "cpdt_awarded_hours_report_"+new SimpleDateFormat("yyyyMMddHHmmssSSS") .format(new Date())+".xls";
         
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
	 * 处理  按照list中对象某一指定属性排序
	 * @param list
	 * @param sortOrderName
	 * @param sortOrderBy
	 */
	@SuppressWarnings("unchecked")
	private void sortStringMethod(List list, final String sortName, final String sortOrderBy){
		 Collections.sort(list, new Comparator<Object>(){  
 	        @Override  
 	        public int compare(Object o1, Object o2) {  
 	        	CpdGroupRegHoursHistory cgrh_1=(CpdGroupRegHoursHistory)o1;  
 	        	CpdGroupRegHoursHistory cgrh_2=(CpdGroupRegHoursHistory)o2;
 	        	//默认usr_ste_usr_id  升序  排序。
 	        	String a = cgrh_1.getUsr_ste_usr_id() + "---" + cgrh_2.getUsr_ste_usr_id();
 	        	int order_ind = cgrh_1.getUsr_ste_usr_id().toLowerCase().compareTo(cgrh_2.getUsr_ste_usr_id().toLowerCase());
 	        	//usr_display_bil 升序 排序。
 	        	if(sortName.equals("usr_display_bil")){
 	        		order_ind = cgrh_1.getUsr_display_bil().toLowerCase().compareTo(cgrh_2.getUsr_display_bil().toLowerCase());
 	        	}
 	        	//选择 降序
 	        	if(sortOrderBy.equals("DESC")){
 	        		if(order_ind != 0){
 	        			order_ind = - order_ind;
 	        		}
 	        	}
 	        	
 	            return order_ind;  
 	        }             
 	    });  
	}
	
	
	/**
	 * 在当前时间，选择的牌照集合中，存在牌照周期不一致。
	 * 现对来源不一致的数据处理    统一保存到his_list
	 * @param his_list
	 * @param now_list
	 */
	private void processingList(List<CpdGroupRegHoursHistory> his_list, List<CpdLrnAwardRecord> now_list,int period){
		if(null != now_list && now_list.size() > 0){
			for(CpdLrnAwardRecord cpdLrnAwardRecord : now_list){
				boolean flg = true;
				for(int i = 0; i < his_list.size(); i++ ){
					//用户已经存在his_list 中
					if(cpdLrnAwardRecord.getClar_usr_ent_id().equals(his_list.get(i).getCghi_usr_ent_id())){
						List<CpdLrnAwardRecord> now_child_list = cpdLrnAwardRecord.getCpdLrnAwardRecordList();
						if(null != now_child_list && now_child_list.size() > 0){
							for(CpdLrnAwardRecord clar : now_child_list){
							    
		                        CpdPeriodVO cpdPeriodVO1 = cpdUtilService.getCurrentPeriod(clar.getClar_ct_id());
		                        if(cpdPeriodVO1.getPeriod()==period){
		                            CpdGroupRegHoursHistory cgha = new CpdGroupRegHoursHistory();
	                                cgha.setCghi_cg_id(clar.getClar_cg_id());
	                                cgha.setCghi_award_core_hours(clar.getClar_award_core_hours());
	                                cgha.setCghi_award_non_core_hours(clar.getClar_award_non_core_hours());
	                                his_list.get(i).getCpdGroupRegHoursHistoryList().add(cgha);
		                        }
								
							}
						}
						flg = false;
						break;
					}
				}
				//用户不存在，则添加到his_list
				if(flg){
					CpdGroupRegHoursHistory cghi = new CpdGroupRegHoursHistory();
                    cghi.setCghi_usr_ent_id(cpdLrnAwardRecord.getClar_usr_ent_id());
					cghi.setUsr_ste_usr_id(cpdLrnAwardRecord.getUsr_ste_usr_id());
					cghi.setUsr_display_bil(cpdLrnAwardRecord.getUsr_display_bil());
					cghi.setUpt_title(cpdLrnAwardRecord.getUpt_title());
					cghi.setUsg_name(cpdLrnAwardRecord.getUsg_name());
					cghi.setCpdGroupRegHoursHistoryList(new ArrayList<CpdGroupRegHoursHistory>());
					List<CpdLrnAwardRecord> now_child_list = cpdLrnAwardRecord.getCpdLrnAwardRecordList();
					if(null != now_child_list && now_child_list.size() > 0){
						for(CpdLrnAwardRecord clar : now_child_list){
							CpdGroupRegHoursHistory cgha = new CpdGroupRegHoursHistory();
							cgha.setCghi_cg_id(clar.getClar_cg_id());
							cgha.setCghi_award_core_hours(clar.getClar_award_core_hours());
							cgha.setCghi_award_non_core_hours(clar.getClar_award_non_core_hours());
							cghi.getCpdGroupRegHoursHistoryList().add(cgha);
						}
					}
					his_list.add(cghi);
				}
				
			}
			
		}
		
	}
	
	
	/**
	 * //报表主体内容 --history
	 * @param list
	 * @param rowIndex
	 * @param grouplist
	 * @param sheet
	 * @param period
	 */
	private int writeReportContent(List<CpdGroupRegHoursHistory> list,int rowIndex,List<CpdGroup>  grouplist,HSSFSheet sheet,int period,HSSFWorkbook wb){
        boolean hasRegistration = false;//是否存在未过期小牌注册记录
		for(int i=0; i<list.size(); i++){  //遍历学员集合 
        	CpdGroupRegHoursHistory cpdghi = list.get(i);
        	
        	List<CpdGroupRegHoursHistory> cpdlist = new  ArrayList<CpdGroupRegHoursHistory>();
        	//遍历需要显示的小牌列
        	for(int j =0; j < grouplist.size(); j++){
                boolean hasAwardHours = false;//是否获得时数
        		CpdGroupRegHoursHistory  cpdgrhh = new CpdGroupRegHoursHistory();
        		cpdgrhh.setCghi_award_core_hours((float) 0);   //若值为-1 则显示为--
        		cpdgrhh.setCghi_award_non_core_hours((float) 0);	
        		if(grouplist.get(j).getCg_contain_non_core_ind() == 0){
        		  cpdgrhh.setCghi_award_non_core_hours((float) -2);	 //非核心时数值为-2  表示不显示该列
        		} 
                Map<String, Object> cg_map = new HashMap<String, Object>();
                cg_map.put("cgr_cg_id", grouplist.get(j).getCg_id());
                cg_map.put("usr_ent_id", cpdghi.getCghi_usr_ent_id());
                cg_map.put("period", period);
                List<CpdGroupRegistration> cglist =  cpdGroupRegistrationMapper.getCpdGroupRegNotPast(cg_map);//查询周期是否存在未过期小牌注册记录
                
                if(cglist!=null && cglist.size() > 0){
                    for(CpdGroupRegHoursHistory  cpdgr : cpdghi.getCpdGroupRegHoursHistoryList()){  //循环当前学员是否有获得的当前牌照核心时数
                        if(cpdgr.getCghi_cg_id() ==grouplist.get(j).getCg_id() ){
                            hasRegistration = true;
                            hasAwardHours = true;
                            cpdgrhh.setCghi_award_core_hours(cpdgr.getCghi_award_core_hours());
                            if(grouplist.get(j).getCg_contain_non_core_ind() == 1){
                                  cpdgrhh.setCghi_award_non_core_hours(cpdgr.getCghi_award_non_core_hours());   
                                }
                        }
                    }
                }else{
                    cpdgrhh.setCghi_award_core_hours((float) -1);//若值为-1 则显示为--
                    if(grouplist.get(j).getCg_contain_non_core_ind() == 1){
                          cpdgrhh.setCghi_award_non_core_hours((float) -1);   
                        }
                }
                
                List<CpdGroupRegistration> cgRegHourslist =  cpdGroupRegistrationMapper.getCpdRegHoursNotPast(cg_map);//判断该报名记录是否是存数据到历史记录
                if(cgRegHourslist!=null && cgRegHourslist.size()>0){
                    hasRegistration = true;
                }else{
                    cpdgrhh.setCghi_award_core_hours((float) -1);//若值为-1 则显示为--
                    if(grouplist.get(j).getCg_contain_non_core_ind() == 1){
                          cpdgrhh.setCghi_award_non_core_hours((float) -1);   
                      }
                }

                hasAwardHours = false;
        		cpdlist.add(cpdgrhh);
            }
        	
        	cpdghi.setCpdGroupRegHoursHistoryList(cpdlist);
        	if(hasRegistration){
        	    HSSFRow row = sheet.createRow(++rowIndex);  
                //创建单元格，并设置值  
                createAndSetCellReport(row,cpdghi);
        	}
        	hasRegistration = false;
	    	
        }

		return rowIndex;
	}
	
	
	/**
	 * //报表主体内容 -- now
	 * @param list
	 * @param rowIndex
	 * @param grouplist
	 * @param sheet
	 * @param period
	 */
	private int writeNowReportContent(List<CpdLrnAwardRecord> list,int rowIndex,List<CpdGroup>  grouplist,HSSFSheet sheet,int period,HSSFWorkbook wb){
	        boolean hasRegistration = false;//是否存在未过期小牌注册记录
			for(int i=0; i<list.size(); i++){  //遍历学员集合 
				CpdLrnAwardRecord clar = list.get(i);
	        	//判断用户注册的小牌 牌照是否在大牌当前周期结束之前除牌。如果是，则按除牌处理
				boolean check_reg =  checkRegistration(clar.getPeriod(),clar.getClar_usr_ent_id());
				if(check_reg){
		        	List<CpdLrnAwardRecord> cpdlist = new  ArrayList<CpdLrnAwardRecord>();
		        	//遍历需要显示的小牌列
		        	for(int j =0; j < grouplist.size(); j++){
		        		CpdLrnAwardRecord  cpdgrhh = new CpdLrnAwardRecord();
		        		cpdgrhh.setClar_award_core_hours((float) 0);   
		        		cpdgrhh.setClar_award_non_core_hours((float) 0);	
		        		if(grouplist.get(j).getCg_contain_non_core_ind() == 0){
		        		  cpdgrhh.setClar_award_non_core_hours((float) -2);	 //非核心时数值为-2  表示不显示该列
		        		}
		                Map<String, Object> cg_map = new HashMap<String, Object>();
		                cg_map.put("cgr_cg_id", grouplist.get(j).getCg_id());
		                cg_map.put("usr_ent_id", clar.getClar_usr_ent_id());
		                cg_map.put("period", period);
		                List<CpdGroupRegistration> cglist =  cpdGroupRegistrationMapper.getCpdGroupRegNotPast(cg_map);//查询周期是否存在未过期小牌注册记录
		                CpdPeriodVO cpdPeriodVO1 = cpdUtilService.getCurrentPeriod(grouplist.get(j).getCg_ct_id());
		                if(cglist!=null && cglist.size() > 0 && cpdPeriodVO1.getPeriod()==period){
                            hasRegistration = true;
		                    for(CpdLrnAwardRecord  cpdgr : clar.getCpdLrnAwardRecordList()){  //循环当前学员是否有获得的当前牌照核心时数
	                            if(cpdgr.getClar_cg_id() .equals(grouplist.get(j).getCg_id()) ){
	                                cpdgrhh.setClar_award_core_hours(cpdgr.getClar_award_core_hours());
	                                if(grouplist.get(j).getCg_contain_non_core_ind() == 1){
	                                      cpdgrhh.setClar_award_non_core_hours(cpdgr.getClar_award_non_core_hours());   
	                                    }
	                            }
	                        }
		                }else{
		                    cpdgrhh.setClar_award_core_hours((float) -1);//若值为-1 则显示为--
                            if(grouplist.get(j).getCg_contain_non_core_ind() == 1){
                                  cpdgrhh.setClar_award_non_core_hours((float) -1);   
                                }
		                }
		                
		        		
		        		cpdlist.add(cpdgrhh);
		            }
		        	
		        	clar.setCpdLrnAwardRecordList(cpdlist);
		            if(hasRegistration){
		                HSSFRow row = sheet.createRow(++rowIndex);  
	                    //创建单元格，并设置值  
	                    createAndSetCellNowReport(row,clar);
		            }
		            hasRegistration = false;
		            
			    	
				}
	        }
			
			return rowIndex;
		}
	
	/**
	 * 获取报表列表头部内容
	 * @param sheet
	 * @param rowIndex
	 * @param wb
	 * @param labels
	 */
	//获取到报表头部
	private void getCell(HSSFSheet sheet,int rowIndex,HSSFWorkbook wb ,String[] labels){
		HSSFRow row = sheet.createRow(rowIndex);  
	    HSSFCellStyle style = wb.createCellStyle();  
	        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
	        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	        style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
	        sheet.createRow(rowIndex).setHeight((short) 800);
	        //边框颜色 黑色
	        style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框    
	        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框    
	        style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框    
	        style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框 
	        //强制内容自动换行
	        style.setWrapText(true); 
   	 for(int i = 0; i<labels.length; i++){
   		     sheet.setColumnWidth(4+i, 4000);  //设置牌照列宽度
   			 HSSFCell cell = row.createCell(i);  
		         cell.setCellValue(labels[i]);  
		         cell.setCellStyle(style);  
   	 }
	}

	//写入报表内容 
	

	/**
	 * 创建单元格 写入数据 （历史周期）
	 * @param row
	 * @param cghi
	 */
	private void createAndSetCellReport(HSSFRow row,CpdGroupRegHoursHistory cghi){
		int index = 0;
		row.createCell(index++).setCellValue(cghi.getUsr_ste_usr_id()); 
		row.createCell(index++).setCellValue(cghi.getUsr_display_bil());  
		//row.createCell(index++).setCellValue(cghi.getCghi_usr_ent_id().toString()); 
        if(null == cghi.getUpt_title() || cghi.getUpt_title().equals("")){
        	row.createCell(index++).setCellValue("--");
        }else{
        	row.createCell(index++).setCellValue(cghi.getUpt_title());
        }
        //row.createCell(index++).setCellValue(cghi.getUsg_name());
        if(null == cghi.getUsg_name() || cghi.getUsg_name().equals("")){
        	row.createCell(index++).setCellValue("--");
        }else{
        	row.createCell(index++).setCellValue(cghi.getUsg_name());
        }
        
        for(CpdGroupRegHoursHistory c : cghi.getCpdGroupRegHoursHistoryList()){
        	if(c.getCghi_award_core_hours() == -1){
        	 row.createCell(index++).setCellValue("--");
        	}else{
             row.createCell(index++).setCellValue(c.getCghi_award_core_hours().toString());	
        	}
        	
        	if(c.getCghi_award_non_core_hours() == -1){
        		row.createCell(index++).setCellValue("--");
        	}else if(c.getCghi_award_non_core_hours() != -2){
        		row.createCell(index++).setCellValue(c.getCghi_award_non_core_hours().toString());
        	}
        }
        	
        
	}

	/**
	 * 创建单元格 写入数据 now(当前年份周期)
	 * @param row
	 * @param cghi
	 */
	private void createAndSetCellNowReport(HSSFRow row,CpdLrnAwardRecord cghi){
			int index = 0;
			row.createCell(index++).setCellValue(cghi.getUsr_ste_usr_id()); 
			row.createCell(index++).setCellValue(cghi.getUsr_display_bil());  
	        //row.createCell(index++).setCellValue(cghi.getUpt_title());
			if(null == cghi.getUpt_title() || cghi.getUpt_title().equals("")){
	        	row.createCell(index++).setCellValue("--");
	        }else{
	        	row.createCell(index++).setCellValue(cghi.getUpt_title());
	        }
	        if(null == cghi.getUsg_name() || cghi.getUsg_name().equals("")){
	        	row.createCell(index++).setCellValue("--");
	        }else{
	        	row.createCell(index++).setCellValue(cghi.getUsg_name());
	        }
	        
	        
	        for(CpdLrnAwardRecord c : cghi.getCpdLrnAwardRecordList()){
	        	if(c.getClar_award_core_hours() == -1){
	        	 row.createCell(index++).setCellValue("--");
	        	}else{
	             row.createCell(index++).setCellValue(c.getClar_award_core_hours().toString());	
	        	}
	        	
	        	if(c.getClar_award_non_core_hours() == -1){
	        		row.createCell(index++).setCellValue("--");
	        	}else if(c.getClar_award_non_core_hours() != -2){
	        		row.createCell(index++).setCellValue(c.getClar_award_non_core_hours().toString());
	        	}
	        }
	        
		}
	
	/**
	 * 报表页头部说明部分
	 * @param sheet
	 * @param style
	 * @param titleStyle
	 * @param cghi
	 * @param lan
	 * @return
	 */
	private Integer setConditionToheard(HSSFSheet sheet,HSSFCellStyle style,HSSFCellStyle titleStyle,CpdGroupRegHoursHistory cghi,String lan){
		   titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); //居中
		
	     	int rowIndex = 0;
			HSSFRow row = sheet.createRow(rowIndex++);
			row.setHeightInPoints(20);
			HSSFCell cell = row.createCell(0);  
	        cell.setCellValue(LabelContent.get(lan, "label_core_cpt_d_management_141"));   // Strictly Private & Confidential
	        cell.setCellStyle(style); 
			rowIndex++;
			
			row = sheet.createRow(rowIndex++); 
			row.setHeightInPoints(30);
			HSSFCell explainCell = row.createCell(0);  
			explainCell.setCellValue(LabelContent.get(lan, "label_core_cpt_d_management_142"));   //Convoy Financial Services Limited
			explainCell.setCellStyle(titleStyle); 
			
			row = sheet.createRow(rowIndex++); 
			row.setHeightInPoints(20);
			HSSFCell  describeCell= row.createCell(0);  
			describeCell.setCellValue(LabelContent.get(lan, "label_core_cpt_d_management_220"));  //Continuing Professional Training/Development (CPD/T) Awarded Hours Report
			describeCell.setCellStyle(titleStyle); 
			
			row = sheet.createRow(rowIndex++); 
			row.setHeightInPoints(20);
			HSSFCell forYearCell = row.createCell(0);  
			forYearCell.setCellValue(LabelContent.get(lan, "label_core_cpt_d_management_144")+cghi.getCghi_period() );  //for the Assessment Year of XXXX
			forYearCell.setCellStyle(titleStyle); 
			rowIndex+=2;
			
	        sheet.addMergedRegion(new Region(2,(short)0,2,(short)10)); 
	        sheet.addMergedRegion(new Region(3,(short)0,3,(short)10)); 
	        sheet.addMergedRegion(new Region(4,(short)0,4,(short)10)); 
			
			row = sheet.createRow(rowIndex++); 
			HSSFCell reportDateCell = row.createCell(0);  
			reportDateCell.setCellValue(LabelContent.get(lan, "label_core_cpt_d_management_145"));  //Report Printing Date
			reportDateCell.setCellStyle(style); 
			row.createCell(1).setCellValue( DateUtil.getInstance().dateToString(DateUtil.getInstance().getDate(), DateUtil.patternA) );
			
		return rowIndex;	
	}
	
	/**
	 * 写入 报表备注
	 * @param period
	 * @param sheet
	 * @param rowIndex
	 * @param wb
	 */
	private void wRemaek(int period,HSSFSheet sheet,int rowIndex,HSSFWorkbook wb){
		 //写入报表备注信息
        String remaek = cpdUtilService.getRemarkByPeriod(period,CpdReportRemark.AWARDED_REMARK_CODE);
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
	 * 判断用户注册的小牌 牌照是否在大牌当前周期结束之前除牌。
	 * @param period
	 * @param usr_ent_id
	 * @return
	 */
	private boolean checkRegistration(int period,long usr_ent_id){
		boolean flag = false;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("usr_ent_id", usr_ent_id);
		List<CpdRegistration> list =  cpdRegistrationMapper.getCpdRegistration(map);
		if(list.size() > 0){
			for(int i = 0; i < list.size(); i++){
				CpdPeriodVO cpdPeriodVO = cpdUtilService.getPeriodByYear(period,list.get(i).getCr_ct_id());
				Map<String, Object> cg_map = new HashMap<String, Object>();
				
				cg_map.put("cgr_cr_id", list.get(i).getCr_id());
				cg_map.put("cgr_usr_ent_id", usr_ent_id);
				cg_map.put("end_time", cpdPeriodVO.getEndTime());
				List<CpdGroupRegistration> cglist =  cpdGroupRegistrationMapper.getCpdGroupReg(cg_map);
				if(cglist.size() > 0){
					return true;
				}
			}
		}
		return flag;
	}
	
}
