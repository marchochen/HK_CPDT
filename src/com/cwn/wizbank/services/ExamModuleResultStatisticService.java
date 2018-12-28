package com.cwn.wizbank.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.entity.vo.ExamModuleResultStatistic;
import com.cwn.wizbank.entity.vo.ExamModuleResultStatistic.Learner;
import com.cwn.wizbank.persistence.ExamModuleResultStatisticMapper;
import com.cwn.wizbank.utils.FormatUtil;
import com.cwn.wizbank.utils.LabelContent;

/**
 * 课程考试模块结果统计信息服务，报表模块比较独立，所以该服务不依赖其他service，数据的获取都在该服务层获取，使模块紧凑独立
 * @author andrew.xiao
 *
 */
@Service
public class ExamModuleResultStatisticService {

	@Autowired
	private ExamModuleResultStatisticMapper eaxmModuleResultStatisticMapper;
	
	@Autowired
	private UserGroupService userGroupService;
	
	/**
	 * 导出结果统计信息
	 * @param mod_id
	 * @param 语言（做国际化）
	 * @throws ParseException 
	 */
	public byte[] exportResult(long mod_id,String cur_lan) throws ParseException {
		//一获取mod_id模块考试结果统计信息模型
		ExamModuleResultStatistic statistic = getEaxmModuleResultStatistic(mod_id);
		//二生成Excel
		return genWorkbook(statistic,cur_lan);
	}

	/**
	 * 生成Excel表
	 * @param statistic
	 */
	private byte[] genWorkbook(ExamModuleResultStatistic statistic,String cur_lan) {
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet();
		
		//设置列宽
		sheet.setColumnWidth(0, 22 * 256);
		sheet.setColumnWidth(1, 20 * 256);
		sheet.setColumnWidth(2, 20 * 256);
		sheet.setColumnWidth(4, 20 * 256);
		sheet.setColumnWidth(7, 20 * 256);
		
		XSSFRow row1 = sheet.createRow(0);
		//合并单元格
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0,6));
		XSSFCell row1_cell1 = row1.createCell(0);
		
		row1_cell1.setCellValue(LabelContent.get(cur_lan, "label_core_report_1"));//测验统计报告：
		setStyle(workbook,row1_cell1,10,false,false,(short)0,true,null);
		
		XSSFRow row2 = sheet.createRow(1);
		XSSFCell row2_cell1 = row2.createCell(0);
		row2_cell1.setCellValue(LabelContent.get(cur_lan, "label_core_report_2"));//课程
		setCommonStyle(workbook,row2_cell1);
		XSSFCell row2_cell2 = row2.createCell(1);
		row2_cell2.setCellValue(statistic.getItem_title());
		setCommonStyle(workbook,row2_cell2);
		
		XSSFRow row3 = sheet.createRow(2);
		XSSFCell row3_cell1 = row3.createCell(0);
		row3_cell1.setCellValue(LabelContent.get(cur_lan, "label_core_report_3"));//测验
		setCommonStyle(workbook, row3_cell1);
		XSSFCell row3_cell2 = row3.createCell(1);
		row3_cell2.setCellValue(statistic.getModule_title());
		setCommonStyle(workbook, row3_cell2);
		
		XSSFRow row5 = sheet.createRow(4);
		XSSFCell row5_cell2 = row5.createCell(1);
		row5_cell2.setCellValue(LabelContent.get(cur_lan, "label_core_report_4"));//数量
		setStyle(workbook,row5_cell2,10,false,false,(short)0,true,null);
		XSSFCell row5_cell3 = row5.createCell(2);
		row5_cell3.setCellValue(LabelContent.get(cur_lan, "label_core_report_5"));//百分比
		setStyle(workbook,row5_cell3,10,false,false,(short)0,true,null);
		XSSFCell row5_cell6 = row5.createCell(5);
		row5_cell6.setCellValue(LabelContent.get(cur_lan, "label_core_report_4"));//数量
		setStyle(workbook,row5_cell6,10,false,false,(short)0,true,null);
		
		XSSFRow row6 = sheet.createRow(5);
		XSSFCell row6_cell1 = row6.createCell(0);
		row6_cell1.setCellValue(LabelContent.get(cur_lan, "label_core_report_6"));//学员总数
		setStyle(workbook,row6_cell1,10,false,false,(short)0,true,null);
		XSSFCell row6_cell2 = row6.createCell(1);
		row6_cell2.setCellValue(statistic.getEnroll_count());
		setStyle(workbook,row6_cell2,11,false,false,(short)0,false,null);
		XSSFCell row6_cell5 = row6.createCell(4);
		row6_cell5.setCellValue(LabelContent.get(cur_lan, "label_core_report_7"));//提交总人次
		setStyle(workbook,row6_cell5,10,false,false,(short)0,true,null);
		XSSFCell row6_cell6 = row6.createCell(5);
		row6_cell6.setCellValue(statistic.getCommit_count());
		setStyle(workbook,row6_cell6,11,false,false,(short)0,false,null);
		XSSFCell row6_cell8 = row6.createCell(7);
		row6_cell8.setCellValue(LabelContent.get(cur_lan, "label_core_report_8"));//试卷总分
		setStyle(workbook,row6_cell8,10,false,false,(short)0,true,null);
		XSSFCell row6_cell9 = row6.createCell(8);
		row6_cell9.setCellValue(statistic.getScore());
		setStyle(workbook,row6_cell9,11,false,false,(short)0,false,null);
		
		XSSFRow row7 = sheet.createRow(6);
		XSSFCell row7_cell1 = row7.createCell(0);
		row7_cell1.setCellValue(LabelContent.get(cur_lan, "label_core_report_9"));//已提交人数
		setStyle(workbook,row7_cell1,10,false,false,(short)0,true,null);
		XSSFCell row7_cell2 = row7.createCell(1);
		row7_cell2.setCellValue(statistic.getCommit_learner_count());
		setStyle(workbook,row7_cell2,11,false,false,(short)0,false,null);
		XSSFCell row7_cell3 = row7.createCell(2);
		row7_cell3.setCellValue((float)statistic.getCommit_learner_count() / statistic.getEnroll_count());
		setStyle(workbook,row7_cell3,11,false,false,(short)0,false,workbook.createDataFormat().getFormat("0%"));
		XSSFCell row7_cell5 = row7.createCell(4);
		row7_cell5.setCellValue(LabelContent.get(cur_lan, "label_core_report_10"));//人均提交次数
		setStyle(workbook,row7_cell5,10,false,false,(short)0,true,null);
		XSSFCell row7_cell6 = row7.createCell(5);
		row7_cell6.setCellValue((float)statistic.getCommit_count() / statistic.getCommit_learner_count());
		setStyle(workbook,row7_cell6,11,false,false,(short)0,false,workbook.createDataFormat().getFormat("0.00"));
		XSSFCell row7_cell8 = row7.createCell(7);
		row7_cell8.setCellValue(LabelContent.get(cur_lan, "label_core_report_11"));//最高分
		setStyle(workbook,row7_cell8,10,false,false,(short)0,true,null);
		XSSFCell row7_cell9 = row7.createCell(8);
		row7_cell9.setCellValue(statistic.getMax_score());
		setStyle(workbook,row7_cell9,11,false,false,(short)0,false,null);
		
		XSSFRow row8 = sheet.createRow(7);
		XSSFCell row8_cell1 = row8.createCell(0);
		row8_cell1.setCellValue(LabelContent.get(cur_lan, "label_core_report_16"));//未提交人数
		setStyle(workbook,row8_cell1,10,false,false,(short)0,true,null);
		XSSFCell row8_cell2 = row8.createCell(1);
		row8_cell2.setCellValue(statistic.getUn_commit_count());
		setStyle(workbook,row8_cell2,11,false,false,(short)0,false,null);
		XSSFCell row8_cell3 = row8.createCell(2);
		row8_cell3.setCellValue((float)statistic.getUn_commit_count() / statistic.getEnroll_count());
		setStyle(workbook,row8_cell3,11,false,false,(short)0,false,workbook.createDataFormat().getFormat("0%"));
		XSSFCell row8_cell8 = row8.createCell(7);
		row8_cell8.setCellValue(LabelContent.get(cur_lan, "label_core_report_13"));//最低分
		setStyle(workbook,row8_cell8,10,false,false,(short)0,true,null);
		XSSFCell row8_cell9 = row8.createCell(8);
		row8_cell9.setCellValue(statistic.getMin_score());
		setStyle(workbook,row8_cell9,11,false,false,(short)0,false,null);
		
		XSSFRow row9 = sheet.createRow(8);
		XSSFCell row9_cell1 = row9.createCell(0);
		row9_cell1.setCellValue(LabelContent.get(cur_lan, "label_core_report_14"));//不合格人数
		setStyle(workbook,row9_cell1,10,false,false,(short)0,true,null);
		XSSFCell row9_cell2 = row9.createCell(1);
		row9_cell2.setCellValue(statistic.getNo_pass_count());
		setStyle(workbook,row9_cell2,11,false,false,(short)0,false,null);
		XSSFCell row9_cell3 = row9.createCell(2);
		row9_cell3.setCellValue((float)statistic.getNo_pass_count() / statistic.getEnroll_count());
		setStyle(workbook,row9_cell3,11,false,false,(short)0,false,workbook.createDataFormat().getFormat("0%"));
		XSSFCell row9_cell8 = row9.createCell(7);
		row9_cell8.setCellValue(LabelContent.get(cur_lan, "label_core_report_15"));//平均分
		setStyle(workbook,row9_cell8,10,false,false,(short)0,true,null);
		XSSFCell row9_cell9 = row9.createCell(8);
		row9_cell9.setCellValue(statistic.getAverage());
		setStyle(workbook,row9_cell9,11,false,false,(short)0,false,workbook.createDataFormat().getFormat("0.00"));
		
		XSSFRow row10 = sheet.createRow(9);
		XSSFCell row10_cell1 = row10.createCell(0);
		row10_cell1.setCellValue(LabelContent.get(cur_lan, "label_core_report_12"));//合格人数
		setStyle(workbook,row10_cell1,10,false,false,(short)0,true,null);
		XSSFCell row10_cell2 = row10.createCell(1);
		row10_cell2.setCellValue(statistic.getPass_count());
		setStyle(workbook,row10_cell2,11,false,false,(short)0,false,null);
		XSSFCell row10_cell3 = row10.createCell(2);
		row10_cell3.setCellValue((float)statistic.getPass_count() / statistic.getEnroll_count());
		setStyle(workbook,row10_cell3,11,false,false,(short)0,false,workbook.createDataFormat().getFormat("0%"));
		XSSFCell row10_cell8 = row10.createCell(7);
		row10_cell8.setCellValue(LabelContent.get(cur_lan, "label_core_report_17"));//已提交人数平均分
		setStyle(workbook,row10_cell8,10,false,false,(short)0,true,null);
		XSSFCell row10_cell9 = row10.createCell(8);
		row10_cell9.setCellValue(statistic.getAverage_for_commited_learner());
		setStyle(workbook,row10_cell9,11,false,false,(short)0,false,workbook.createDataFormat().getFormat("0.00"));
		
		XSSFRow row12 = sheet.createRow(11);
		XSSFCell row12_cell1 = row12.createCell(0);
		row12_cell1.setCellValue(LabelContent.get(cur_lan, "label_core_report_18"));//统计说明：
		setStyle(workbook,row12_cell1,10,false,true,IndexedColors.GREY_25_PERCENT.getIndex(),true,null);
		
		XSSFRow row13 = sheet.createRow(12);
		XSSFCell row13_cell1 = row13.createCell(0);
		row13_cell1.setCellValue(LabelContent.get(cur_lan, "label_core_report_19"));//学员总数：
		setStyle(workbook,row13_cell1,10,false,true,IndexedColors.GREY_25_PERCENT.getIndex(),false,null);
		XSSFCell row13_cell2 = row13.createCell(1);
		row13_cell2.setCellValue(LabelContent.get(cur_lan, "label_core_report_20"));//已成功报名的学员
		setStyle(workbook,row13_cell2,10,false,true,IndexedColors.GREY_25_PERCENT.getIndex(),false,null);
		XSSFCell row13_cell5 = row13.createCell(4);
		row13_cell5.setCellValue(LabelContent.get(cur_lan, "label_core_report_21"));//已提人数百分比 = 已提人数/学员总数
		setStyle(workbook,row13_cell5,10,false,true,IndexedColors.GREY_25_PERCENT.getIndex(),false,null);
		
		XSSFRow row14 = sheet.createRow(13);
		XSSFCell row14_cell1 = row14.createCell(0);
		row14_cell1.setCellValue(LabelContent.get(cur_lan, "label_core_report_22"));//已提交人数：
		setStyle(workbook,row14_cell1,10,false,true,IndexedColors.GREY_25_PERCENT.getIndex(),false,null);
		XSSFCell row14_cell2 = row14.createCell(1);
		row14_cell2.setCellValue(LabelContent.get(cur_lan, "label_core_report_23"));//为已提交考试的人数
		setStyle(workbook,row14_cell2,10,false,true,IndexedColors.GREY_25_PERCENT.getIndex(),false,null);
		XSSFCell row14_cell5 = row14.createCell(4);
		row14_cell5.setCellValue(LabelContent.get(cur_lan, "label_core_report_24"));//合格人数百分比 = 合格人数/学员总数
		setStyle(workbook,row14_cell5,10,false,true,IndexedColors.GREY_25_PERCENT.getIndex(),false,null);
		
		XSSFRow row15 = sheet.createRow(14);
		XSSFCell row15_cell1 = row15.createCell(0);
		row15_cell1.setCellValue(LabelContent.get(cur_lan, "label_core_report_25"));//未已提交人数：
		setStyle(workbook,row15_cell1,10,false,true,IndexedColors.GREY_25_PERCENT.getIndex(),false,null);
		XSSFCell row15_cell2 = row15.createCell(1);
		row15_cell2.setCellValue(LabelContent.get(cur_lan, "label_core_report_26"));//已成功报名，但还没提交测验的人数
		setStyle(workbook,row15_cell2,10,false,true,IndexedColors.GREY_25_PERCENT.getIndex(),false,null);
		XSSFCell row15_cell5 = row15.createCell(4);
		row15_cell5.setCellValue(LabelContent.get(cur_lan, "label_core_report_27"));//不合格人数百分比 = 合格人数/学员总数
		setStyle(workbook,row15_cell5,10,false,true,IndexedColors.GREY_25_PERCENT.getIndex(),false,null);
		
		XSSFRow row16 = sheet.createRow(15);
		XSSFCell row16_cell1 = row16.createCell(0);
		row16_cell1.setCellValue(LabelContent.get(cur_lan, "label_core_report_11"));//最高分
		setStyle(workbook,row16_cell1,10,false,true,IndexedColors.GREY_25_PERCENT.getIndex(),false,null);
		XSSFCell row16_cell2 = row16.createCell(1);
		row16_cell2.setCellValue(LabelContent.get(cur_lan, "label_core_report_28"));//所有已提交考试中的最高得分
		setStyle(workbook,row16_cell2,10,false,true,IndexedColors.GREY_25_PERCENT.getIndex(),false,null);
		XSSFCell row16_cell5 = row16.createCell(4);
		row16_cell5.setCellValue(LabelContent.get(cur_lan, "label_core_report_29"));//人均提交次数 =  提交总人次/已提交人数
		setStyle(workbook,row16_cell5,10,false,true,IndexedColors.GREY_25_PERCENT.getIndex(),false,null);
		
		XSSFRow row17 = sheet.createRow(16);
		XSSFCell row17_cell1 = row17.createCell(0);
		row17_cell1.setCellValue(LabelContent.get(cur_lan, "label_core_report_13"));//最低分
		setStyle(workbook,row17_cell1,10,false,true,IndexedColors.GREY_25_PERCENT.getIndex(),false,null);
		XSSFCell row17_cell2 = row17.createCell(1);
		row17_cell2.setCellValue(LabelContent.get(cur_lan, "label_core_report_30"));//所有已提交考试人中的最低得分
		setStyle(workbook,row17_cell2,10,false,true,IndexedColors.GREY_25_PERCENT.getIndex(),false,null);
		XSSFCell row17_cell5 = row17.createCell(4);
		row17_cell5.setCellValue(LabelContent.get(cur_lan, "label_core_report_31"));//人均提交次数 =  提交总人次/已提交人数
		setStyle(workbook,row17_cell5,10,false,true,IndexedColors.GREY_25_PERCENT.getIndex(),false,null);
		
		XSSFRow row18 = sheet.createRow(17);
		XSSFCell row18_cell5 = row18.createCell(4);
		row18_cell5.setCellValue(LabelContent.get(cur_lan, "label_core_report_32"));//提交总人次： 所有人提交测验次数加总。
		setStyle(workbook,row18_cell5,10,false,true,IndexedColors.GREY_25_PERCENT.getIndex(),false,null);
		
		XSSFRow row19 = sheet.createRow(18);
		XSSFCell row19_cell5 = row19.createCell(4);
		row19_cell5.setCellValue(LabelContent.get(cur_lan, "label_core_report_33"));//平均分 = 每个人的最好成绩加总/学员总数
		setStyle(workbook,row19_cell5,10,false,true,IndexedColors.GREY_25_PERCENT.getIndex(),false,null);
		
		XSSFRow row20 = sheet.createRow(19);
		XSSFCell row20_cell5 = row20.createCell(4);
		row20_cell5.setCellValue(LabelContent.get(cur_lan, "label_core_report_34"));//已提交人数平均分 = 每个人的最好成绩加总/已提交人数
		setStyle(workbook,row20_cell5,10,false,true,IndexedColors.GREY_25_PERCENT.getIndex(),false,null);
		
		XSSFRow row21 = sheet.createRow(20);
		
		//填充12到21行的颜色
		for(int i=11;i<=20;i++){
			fillRowBg(workbook,sheet,i,IndexedColors.GREY_25_PERCENT.getIndex());
		}
		
		List<Learner> learnerList = statistic.getLearnerList();
		
		if(learnerList!=null && learnerList.size()>0){
			
			XSSFRow row24 = sheet.createRow(23);
			
			XSSFCell row24_cell1 = row24.createCell(0);
			row24_cell1.setCellValue(LabelContent.get(cur_lan, "label_core_report_36"));//用户名
			setStyle(workbook,row24_cell1,11,false,true,IndexedColors.GREY_50_PERCENT.getIndex(),true,null);
			XSSFCell row24_cell2 = row24.createCell(1);
			row24_cell2.setCellValue(LabelContent.get(cur_lan, "label_core_report_37"));//全名
			setStyle(workbook,row24_cell2,11,false,true,IndexedColors.GREY_50_PERCENT.getIndex(),true,null);
			XSSFCell row24_cell3 = row24.createCell(2);
			row24_cell3.setCellValue(LabelContent.get(cur_lan, "label_core_report_38"));//用户组
			setStyle(workbook,row24_cell3,11,false,true,IndexedColors.GREY_50_PERCENT.getIndex(),true,null);
			XSSFCell row24_cell4 = row24.createCell(3);
			row24_cell4.setCellValue(LabelContent.get(cur_lan, "label_core_report_11"));//最高分
			setStyle(workbook,row24_cell4,11,false,true,IndexedColors.GREY_50_PERCENT.getIndex(),true,null);
			XSSFCell row24_cell5 = row24.createCell(4);
			row24_cell5.setCellValue(LabelContent.get(cur_lan, "label_core_report_42"));//是否合格
			setStyle(workbook,row24_cell5,11,false,true,IndexedColors.GREY_50_PERCENT.getIndex(),true,null);
			XSSFCell row24_cell6 = row24.createCell(5);
			row24_cell6.setCellValue(LabelContent.get(cur_lan, "label_core_report_39"));//提交次数
			setStyle(workbook,row24_cell6,11,false,true,IndexedColors.GREY_50_PERCENT.getIndex(),true,null);
			
			int rowNum=25;
			
			XSSFRow row23 = sheet.createRow(22);
			
			for(int i=0;i<learnerList.size();i++){
				
				Learner learner = learnerList.get(i);
				
				XSSFRow row = sheet.createRow(rowNum-1+i);//row是从0开始
				XSSFCell cell1 = row.createCell(0);
				cell1.setCellValue(learner.usr_ste_usr_id);
				setCommonStyle(workbook,cell1);
				XSSFCell cell2 = row.createCell(1);
				cell2.setCellValue(learner.usr_display_bil);
				setCommonStyle(workbook,cell2);
				XSSFCell cell3 = row.createCell(2);
				cell3.setCellValue(learner.usr_group);
				setCommonStyle(workbook,cell3);
				XSSFCell cell4 = row.createCell(3);
				cell4.setCellValue(learner.max_score);
				setCommonStyle(workbook,cell4);
				XSSFCell cell5 = row.createCell(4);
				cell5.setCellValue(learner.is_passed);
				setCommonStyle(workbook,cell5);
				XSSFCell cell6 = row.createCell(5);
				List<ExamModuleResultStatistic.Learner.CommitInfo> commitInfoList = learner.commitInfoList;
				cell6.setCellValue(commitInfoList == null ? 0 : commitInfoList.size());
				setCommonStyle(workbook,cell6);
				
				int cellNum = 7;
				for(int j=0;commitInfoList!=null && j<commitInfoList.size(); j++){
					ExamModuleResultStatistic.Learner.CommitInfo commitInfo = commitInfoList.get(j);
					
					XSSFCell cell = row23.getCell(cellNum+j*3-1);
					if(cell == null || StringUtils.isEmpty(cell.getStringCellValue())){
						
						/*
						 * 合并单元格
						 */
						CellRangeAddress cra =new CellRangeAddress(22, 22, cellNum+j*3-1,cellNum+j*3+1);
						sheet.addMergedRegion(cra);
						
						cell = row23.createCell(cellNum+j*3-1);
						cell.setCellValue(LabelContent.get(cur_lan, "label_core_report_40").replace("{{n}}", (j+1)+""));//"第"+(j+1)+"次提交"
						setStyle(workbook,cell,11,true,true,IndexedColors.LIGHT_TURQUOISE.getIndex(),true,null);
					}
					
					XSSFCell row24_cell_n1_header = row24.createCell(cellNum+j*3-1);
					row24_cell_n1_header.setCellValue(LabelContent.get(cur_lan, "label_core_report_41"));//提交时间
					
					//设置列宽
					sheet.setColumnWidth(cellNum+j*3-1, 22 * 256);
					
					setStyle(workbook,row24_cell_n1_header,11,false,true,IndexedColors.GREY_50_PERCENT.getIndex(),true,null);
					XSSFCell row24_cell_n2_header = row24.createCell(cellNum+j*3);
					row24_cell_n2_header.setCellValue(LabelContent.get(cur_lan, "label_core_report_42"));//是否合格
					setStyle(workbook,row24_cell_n2_header,11,false,true,IndexedColors.GREY_50_PERCENT.getIndex(),true,null);
					XSSFCell row24_cell_n3_header = row24.createCell(cellNum+j*3+1);
					row24_cell_n3_header.setCellValue(LabelContent.get(cur_lan, "label_core_report_43"));//分数
					setStyle(workbook,row24_cell_n3_header,11,false,true,IndexedColors.GREY_50_PERCENT.getIndex(),true,null);
					
					XSSFCell row24_cell_n1_value = row.createCell(7+j*3-1);
					row24_cell_n1_value.setCellValue(cwUtils.format2ymdhms(commitInfo.commit_time));
					setCommonStyle(workbook,row24_cell_n1_value);
					
					XSSFCell row24_cell_n2_value = row.createCell(7+j*3);
					row24_cell_n2_value.setCellValue(commitInfo.is_passed);
					setCommonStyle(workbook,row24_cell_n2_value);
					
					XSSFCell row24_cell_n3_value = row.createCell(7+j*3+1);
					row24_cell_n3_value.setCellValue(commitInfo.score);
					setCommonStyle(workbook,row24_cell_n3_value);
				}
				
			}
			
			//填充23到24行的颜色
			for(int i=22;i<=23;i++){
				fillRowBg(workbook,sheet,i,IndexedColors.GREY_50_PERCENT.getIndex());
			}
		}
		
		
		byte[] byteArray = null;
		
		try {
			workbook.write(out);
			byteArray = out.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(out != null){
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				out = null;
			}
		}
		return byteArray;
		
	}

	/**
	 * 获取mod_id模块考试结果统计信息模型
	 * @param mod_id
	 * @return
	 * @throws ParseException 
	 */
	private ExamModuleResultStatistic getEaxmModuleResultStatistic(long mod_id) throws ParseException {
		
		ExamModuleResultStatistic statistic = new ExamModuleResultStatistic();
		//通过模块id获取报名该模块对应课程的人数
		int enroll_count = eaxmModuleResultStatisticMapper.selectAeApplicationCount(mod_id);
		if(enroll_count == 0){//如果没有人报名，直接返回
			return statistic;
		}
		
		statistic.setEnroll_count(enroll_count);
		
		//获取课程标题，如：xxx课程标题  > xxx班级标题 
		String item_title = getItemTitle(mod_id);
		statistic.setItem_title(item_title);
		
		//查询resources表获取模块标题
		String module_title = eaxmModuleResultStatisticMapper.selectModuleTitle(mod_id);
		statistic.setModule_title(module_title);
		
		//根据模块id获取提交的总次数和提交的人数
		Map<String,Number> commitCountInfo = eaxmModuleResultStatisticMapper.selectCommitCountInfo(mod_id);
		//总的提交次数
		int commit_count = commitCountInfo.get("commit_count").intValue();
		
		if(commit_count == 0){//如果没有人提交考试,直接返回
			return statistic;
		}
		
		statistic.setCommit_count(commit_count);
		//提交的学员数，一个学员提交多次只算一次
		int commit_learner_count = commitCountInfo.get("commit_learner_count").intValue();
		statistic.setCommit_learner_count(commit_learner_count);
		
		//人均提交次数
		statistic.setPer_learner_commit_count((float)commit_count / commit_learner_count);
		
		/**
		 * 获取模块信息，包括 mod_max_score（试卷总分）,mod_pass_score（通过分数的百分比）
		 */
		Map<String,Number> moduleInfo = eaxmModuleResultStatisticMapper.selectModuleInfo(mod_id);
		statistic.setScore(moduleInfo.get("mod_max_score").floatValue());
		
		//通过的分数（合格分）
		float pass_score = moduleInfo.get("mod_max_score").floatValue() * (moduleInfo.get("mod_pass_score").floatValue() / (float)100);
		//保留两位小数
		pass_score = FormatUtil.getInstance().scaleFloat(pass_score, 2, BigDecimal.ROUND_HALF_UP);
		
		//未提交人数
		statistic.setUn_commit_count(enroll_count - commitCountInfo.get("commit_learner_count").intValue());
		
		//查询Progress列表，获取模块用户每次提交的情况列表
		//查询示例：pgr_completion_status=F, usg_display_bil=学员用户组1, usr_display_bil=andrew1, pgr_score=5.0000, max_pgr_score=5.0000, usr_ste_usr_id=andrew1, pgr_complete_datetime=2016-12-07 16:37:31.127
		List<Map<String,Object>> commitInfoList = eaxmModuleResultStatisticMapper.selectcommitInfoList(mod_id);
		
		//提取考试信息，包括最高分，最低分，通过人数等
		Map<String,Object> examInfoMap = extractExamInfo(commitInfoList,pass_score,enroll_count);
		
		statistic.setLearnerList((List<Learner>) examInfoMap.get("learnerList"));
		//最高分
		statistic.setMax_score((Float)examInfoMap.get("exam_max_score"));
		//最低分
		statistic.setMin_score((Float)examInfoMap.get("exam_min_score"));
		//合格人数
		statistic.setPass_count((Integer)examInfoMap.get("pass_count"));
		//不合格人数
		statistic.setNo_pass_count((Integer)examInfoMap.get("no_pass_count"));
		//平均分
		statistic.setAverage((Float)examInfoMap.get("average"));
		//已提交人数平均分 = 每个人的最好成绩加总/已提交人数
		statistic.setAverage_for_commited_learner((Float)examInfoMap.get("average_for_commited_learner"));
		
		return statistic;
	}

	
	private String getItemTitle(long mod_id) {
		//获取课程信息
		Map<String,Object> map = eaxmModuleResultStatisticMapper.selectItemInfo(mod_id);
		String item_title = map.get("itm_title").toString();
		Object itm_run_ind = map.get("itm_run_ind");
		Long itm_id = ((Number)map.get("itm_id")).longValue();
		
		if(itm_run_ind!=null && (itm_run_ind.toString().equals("1") || itm_run_ind.toString().equals("true"))){//如果为班级
			String parentTitle = eaxmModuleResultStatisticMapper.selectItemParentTitle(itm_id);
			item_title += ("  >   " + parentTitle);
		}
		return item_title;
	}

	/**
	 * @param commitInfoList 考试提交信息集合
	 * @param passScore 及格分
	 * @param enroll_count 报名人数
	 * @return
	 * @throws ParseException 
	 */
	private Map<String,Object> extractExamInfo(List<Map<String, Object>> commitInfoList,float passScore,int enroll_count) throws ParseException {
		
		Map<String,Object> resultMap = new HashMap<String, Object>();
		
		//分数最高分
		float exam_max_score = -1;
		//分数最低分
		float exam_min_score = -1;
		//合格人数
		int pass_count = 0;
		//不合格人数
		int no_pass_count = 0;
		//平均分： 每个人的最好成绩加总/学员总数
		float average = 0;
		//已提交人数平均分 = 每个人的最好成绩加总/已提交人数
		float average_for_commited_learner = 0;
		
		//参与考试的学员个数（注：一个学员可以提交多次考试，算一个）
		int learner_count = 0;
		
		//每个人的最好成绩加总
		int sum = 0;
		
		//保存了参与考试的学员信息，key为usr_ste_usr_id，value为ExamModuleResultStatistic.Learner
		Map<String,ExamModuleResultStatistic.Learner> learnerMap = new HashMap<String, ExamModuleResultStatistic.Learner>();
		//学员集合
		List<ExamModuleResultStatistic.Learner> learnerList = new ArrayList<ExamModuleResultStatistic.Learner>();
		for(Map<String, Object> map : commitInfoList){
			
			String usr_ste_usr_id = map.get("usr_ste_usr_id").toString();
			ExamModuleResultStatistic.Learner learner = learnerMap.get(usr_ste_usr_id);
			
			if(learner == null){
				
				//该学员提交记录中，最高分数
				float max_pgr_score = ((Number)map.get("max_pgr_score")).floatValue();
				
				learner = new ExamModuleResultStatistic().new Learner();
				learner.usr_ste_usr_id = usr_ste_usr_id;
				learner.usr_display_bil = map.get("usr_display_bil").toString();
				long usg_ent_id = ((Number)map.get("usg_ent_id")).longValue();
				learner.usr_group = userGroupService.getGroupLevelString(usg_ent_id);
				learner.max_score = max_pgr_score;
				learner.is_passed = max_pgr_score >= passScore ? "Y" : "N";
				learnerMap.put(usr_ste_usr_id,learner);
				
				learnerList.add(learner);

				//统计合格人数
				if(max_pgr_score >= passScore){
					pass_count++;
				}
				
				//统计不合格人数
				if(max_pgr_score < passScore){
					no_pass_count++;
				}

				//统计最高分
				if(exam_max_score == -1 || exam_max_score < max_pgr_score){
					exam_max_score = max_pgr_score;
				}
				
				//统计最低分
				if(exam_min_score == -1 || exam_min_score > max_pgr_score){
					exam_min_score = max_pgr_score;
				}
				
				//总分
				sum += max_pgr_score;
				
				//参与人数总和
				learner_count ++;
			}
			
			if(learner.commitInfoList == null){
				learner.commitInfoList = new ArrayList<ExamModuleResultStatistic.Learner.CommitInfo>();
			}
			
			ExamModuleResultStatistic.Learner.CommitInfo commitInfo = new ExamModuleResultStatistic().new Learner().new CommitInfo();
			
			
			commitInfo.commit_time = cwUtils.parse((String) map.get("pgr_complete_datetime"));
			
			commitInfo.is_passed = (map.get("pgr_completion_status") == null || map.get("pgr_completion_status").toString().equals("F")) ? "N" : "Y";
			
			commitInfo.score = ((Number) map.get("pgr_score")).floatValue();
			
			//记录学员每次提交记录的集合
			learner.commitInfoList.add(commitInfo);
		}
		
		average = (float)sum / enroll_count;
		average_for_commited_learner = (float)sum / learner_count;
		
		resultMap.put("exam_max_score",exam_max_score);
		resultMap.put("exam_min_score",exam_min_score);
		resultMap.put("pass_count",pass_count);
		resultMap.put("no_pass_count",no_pass_count);
		resultMap.put("average",average);
		resultMap.put("average_for_commited_learner",average_for_commited_learner);
		resultMap.put("learnerList",learnerList);
		
		return resultMap;
		
	}
	
	/**
	 * 设置通用字体样式
	 */
	public void setCommonStyle(XSSFWorkbook book,XSSFCell cell){
		setStyle(book,cell,10,false,false,(short)0,false,null);
	}
	
	/**
	 * 设置单元格样式
	 * @param book
	 * @param cell
	 * @param size 字体大小
	 * @param centerAlignment 是否居中
	 * @param fill 是否填充
	 * @param fg 颜色值
	 * @param bold 是否加粗
	 * @param fmt 数据格式
	 */
	private XSSFCellStyle setStyle(XSSFWorkbook book,XSSFCell cell,int size,boolean centerAlignment,boolean fill,short fg,boolean bold,Short fmt){
		XSSFFont font = book.createFont();
		font.setBold(bold);
		font.setFontHeightInPoints((short) size);//字号  
		
		XSSFCellStyle style = book.createCellStyle();
		
		if(centerAlignment){
			style.setAlignment(XSSFCellStyle.ALIGN_CENTER); // 居中
		}
		
		style.setFont(font);
		
		if(fill){
			style.setFillForegroundColor(fg);
			style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		}
		
		if(fmt != null){
			style.setDataFormat(fmt);
		}
		
		cell.setCellStyle(style);
		
		return style;
	}
	
	
	/**
	 * 填充行的颜色
	 * @param book
	 * @param sheet
	 * @param rowNum
	 * @param fg
	 */
	private void fillRowBg(XSSFWorkbook book,XSSFSheet sheet,int rowNum,short fg){
		XSSFRow row = sheet.getRow(rowNum);
		if(row != null){
			XSSFCellStyle style = book.createCellStyle();
			style.setFillForegroundColor(fg);
			style.setFillPattern(CellStyle.SOLID_FOREGROUND);
			row.setRowStyle(style);
		}
	}

}
