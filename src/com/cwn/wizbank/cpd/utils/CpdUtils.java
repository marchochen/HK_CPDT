package com.cwn.wizbank.cpd.utils;

import java.util.Calendar;
import java.util.Date;

import org.springframework.util.StringUtils;

import com.cwn.wizbank.cpd.vo.CpdGroupSumVo;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;

public class CpdUtils {
	
	static public String STATUS_OK="OK";
	
	static public String STATUS_DEL="DEL";
	
	static public String REQUIRE_HOURS_ACTION_RECAL = "RECAL";
	
	static public String REQUIRE_HOURS_ACTION_ADD = "ADD";
	
	static public String REQUIRE_HOURS_ACTION_UPD = "UPD";
	
	static public String REQUIRE_HOURS_ACTION_DEL = "DEL";
	
	static public String REQUIRE_HOURS_ACTION_FUTURE = "FUTURE"; //生成未来的数据 一般用于线程构建超出当前周期的周期所需要时数
	
	static public String AWARD_HOURS_ACTION_LRN_AW = "LRN_AW";  //学员学习时获得得CPD时数计算
	
	static public String AWARD_HOURS_ACTION_RECAL_AW = "RECAL_AW"; //重算每个人在当前课程中所有已完在学习记录获得CPD时数
	
	static public String AWARD_HOURS_ACTION_OTHER_AW = "OTHER_AW";
	
	static public float AWARD_HOUR_TIME_CELL = 30f; //获取时数的时间单位 30分钟/0.5时数
	
	static public float AWARD_HOUR_PER_CELL = 0.5f;  //获取时数的换算单位 30分钟/0.5时数
	
	static public String CPD_CORE_HEADER_KEY = "_CORE";
	
	static public String CPD_NON_CORE_HEADER_KEY = "_NONE_CORE";
	
	
	public static Long converCpdTimeToMilliseconds(Date date){
		if(null==date){
			return null;
		}
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date);
	
		c1.set(Calendar.HOUR,0);
		c1.set(Calendar.MINUTE,0);
		c1.set(Calendar.SECOND,0);
		c1.set(Calendar.MILLISECOND,0);
		return c1.getTime().getTime();
	}
	
	
	public static PdfPCell createNoBorderCell(String txt , Font font,Integer align,boolean showEmpty){
		Paragraph paragraph = null;
		if(!StringUtils.isEmpty(txt)){
			 paragraph = new Paragraph(txt,font);
		 }else{
			 if(showEmpty){
				 paragraph = new Paragraph("",font);
			 }else{
				 paragraph = new Paragraph("--",font);
			 }
		 }
		PdfPCell cell = new PdfPCell(paragraph);
		cell.setBorder(Rectangle.NO_BORDER);
		if(null==align){
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		}else{
			cell.setHorizontalAlignment(align);
		}
		
		return cell;
	}
	
	/**
	 * 核心时数满足后可以借给非核心时数
	 * @param sumVo
	 */
	public static void calBorrowHours(CpdGroupSumVo sumVo){
		
		if(sumVo.isContain_non_core_ind()){
			if(sumVo.getSum_award_core_hours()>sumVo.getReq_core()){
				if(null!=sumVo.getSum_award_non_core_hours() 
						&& sumVo.getSum_award_non_core_hours()<sumVo.getReq_non_core()){
					float more = sumVo.getSum_award_core_hours() - sumVo.getReq_core();
					float less = sumVo.getReq_non_core() - sumVo.getSum_award_non_core_hours();
					if(more>=less){
						sumVo.setSum_award_core_hours(sumVo.getReq_core()+(more-less));
						sumVo.setSum_award_non_core_hours(sumVo.getReq_non_core());
					}else{
						sumVo.setSum_award_core_hours(sumVo.getReq_core());
						sumVo.setSum_award_non_core_hours(sumVo.getSum_award_non_core_hours()+more);
					}
					
				}
			}
		}
		//计算相差时数
		if(null!=sumVo.getSum_award_core_hours()){
			if(sumVo.getSum_award_core_hours()>=sumVo.getReq_core()){
				sumVo.setOutStanding_core(0f);
			}else{
				sumVo.setOutStanding_core(sumVo.getReq_core()-sumVo.getSum_award_core_hours());
			}
		}else{
			sumVo.setOutStanding_core(sumVo.getReq_core());
		}

		if(null!=sumVo.getSum_award_non_core_hours()){
			if(null == sumVo.getReq_non_core() || sumVo.getSum_award_non_core_hours()>=sumVo.getReq_non_core()){
				sumVo.setOutStanding_non_core(0f);
			}else{
				sumVo.setOutStanding_non_core(sumVo.getReq_non_core()-sumVo.getSum_award_non_core_hours());
			}
		}else{
			sumVo.setOutStanding_non_core(sumVo.getReq_non_core());
		}

	}
	


}
