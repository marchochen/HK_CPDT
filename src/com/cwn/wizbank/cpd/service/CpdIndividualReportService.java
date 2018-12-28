package com.cwn.wizbank.cpd.service;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.message.Message;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.util.LangLabel;
import com.cw.wizbank.util.PdfPageEvent;
import com.cwn.wizbank.cpd.utils.CpdUtils;
import com.cwn.wizbank.cpd.vo.CpdGroupSumVo;
import com.cwn.wizbank.cpd.vo.CpdPeriodVO;
import com.cwn.wizbank.cpd.vo.IndividualReportVo;
import com.cwn.wizbank.entity.CpdGroup;
import com.cwn.wizbank.entity.CpdGroupRegHours;
import com.cwn.wizbank.entity.CpdGroupRegistration;
import com.cwn.wizbank.entity.CpdRegistration;
import com.cwn.wizbank.entity.CpdReportRemark;
import com.cwn.wizbank.entity.CpdType;
import com.cwn.wizbank.entity.RegUser;
import com.cwn.wizbank.persistence.RegUserMapper;
import com.cwn.wizbank.utils.CommonLog;
import com.cwn.wizbank.utils.DateUtil;
import com.cwn.wizbank.utils.FormatUtil;
import com.cwn.wizbank.utils.LabelContent;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@Service
public class CpdIndividualReportService {
	
    @Autowired
    RegUserMapper regUserMapper;
    
	@Autowired
	CpdGroupService cpdGroupService;
	
    @Autowired
    CpdUtilService cpdUtilService;
    
    @Autowired
    CpdRegistrationMgtService cpdRegistrationMgtService;
    
    @Autowired
    CpdLrnAwardRecordService cpdLrnAwardRecordService;
    
    @Autowired
    CpdGroupRegistrationService cpdGroupRegistrationService;
    
    @Autowired
    CpdGroupRegHoursHistoryService cpdGroupRegHoursHistoryService;
    
    public final static int FORMAT_TYPE_IN_ONE = 0;
    
    public final static int FORMAT_TYPE_EACH_ONE = 1;
    
    private BaseFont getBaseFont() throws DocumentException, IOException{
    	
    	return BaseFont.createFont(WizbiniLoader.getInstance().getFontRoot()+"/SURSONG.TTF"
    				,BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);    
    	
    }
    
    private Font getTableFont() throws DocumentException, IOException{
    	return new Font(getBaseFont(), 12, Font.NORMAL);
    }
    
    private Font getTitleFont() throws DocumentException, IOException{
    	return new Font(getBaseFont(), 15, Font.NORMAL);
    }
    
    /**
     * 打印报表标题
     * @param doc
     * @throws DocumentException 
     */
    private void exportPdfTitle(Document doc,int period,Font font,String lang) throws Exception{
    	font.setSize(12);
		Paragraph paragraph = new Paragraph(LabelContent.get(lang, "label_core_cpt_d_management_141"),font);
		paragraph.setAlignment(Element.ALIGN_LEFT);
        doc.add(paragraph);
        font.setSize(15);
		paragraph = new Paragraph(LabelContent.get(lang, "label_core_cpt_d_management_142"),font);
		paragraph.setAlignment(Element.ALIGN_CENTER);
        doc.add(paragraph);
        paragraph = new Paragraph(LabelContent.get(lang, "label_core_cpt_d_management_277"),font);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        doc.add(paragraph);
        paragraph = new Paragraph(LabelContent.get(lang, "label_core_cpt_d_management_144")+period,font);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        doc.add(paragraph);
    }
    
    /**
     * 打印用户信息
     * @param doc
     * @param font
     * @param user
     * @throws DocumentException
     */
    private void setPdfUserDetail(Document doc,Font font,RegUser user,String lang) throws DocumentException{
    	PdfPTable datatable = new PdfPTable(2);
    	datatable.setSpacingBefore(10f);//加入之前的空格
    	datatable.setTotalWidth(new float[]{130f,200f});
        datatable.setWidthPercentage(100);
        datatable.setLockedWidth(true);
        datatable.getDefaultCell().setPadding(2);//表格的padding设置
        datatable.getDefaultCell().setHorizontalAlignment( Element.ALIGN_LEFT);
        datatable.setHorizontalAlignment(Element.ALIGN_LEFT);
        
		datatable.addCell(CpdUtils.createNoBorderCell(LabelContent.get(lang, "label_core_cpt_d_management_58")+": ",font,Element.ALIGN_RIGHT,false));
		if(null!=user){
			datatable.addCell(CpdUtils.createNoBorderCell(user.getUsr_ste_usr_id(),font,null,false));
		}else{
			datatable.addCell(CpdUtils.createNoBorderCell("--",font,null,false));
		}
        
        datatable.addCell(CpdUtils.createNoBorderCell(LabelContent.get(lang, "label_core_cpt_d_management_59")+": ",font,Element.ALIGN_RIGHT,false));
        if(null!=user){
        	datatable.addCell(CpdUtils.createNoBorderCell(user.getUsr_display_bil(),font,null,false));
        }else{
        	datatable.addCell(CpdUtils.createNoBorderCell("--",font,null,false));
        }

        datatable.addCell(CpdUtils.createNoBorderCell(LabelContent.get(lang, "label_core_cpt_d_management_148")+": ",font,Element.ALIGN_RIGHT,false));
        if(null!=user){
        	datatable.addCell(CpdUtils.createNoBorderCell(user.getUsg_display_bil(),font,null,false));
        }else{
        	datatable.addCell(CpdUtils.createNoBorderCell("--",font,null,false));
        }
		
        datatable.addCell(CpdUtils.createNoBorderCell(LabelContent.get(lang, "label_core_cpt_d_management_147")+": ",font,Element.ALIGN_RIGHT,false));
        if(null!=user){
        	datatable.addCell(CpdUtils.createNoBorderCell(user.getUgr_display_bil(),font,null,false));
        }else{
        	datatable.addCell(CpdUtils.createNoBorderCell("--",font,null,false));
        }
		
        datatable.addCell(CpdUtils.createNoBorderCell(LabelContent.get(lang, "label_core_cpt_d_management_145"),font,Element.ALIGN_RIGHT,false));
        if(null!=user){
        	datatable.addCell(CpdUtils.createNoBorderCell(DateUtil.getInstance().dateToString(new Date(), DateUtil.patternH),font,null,false));
        }else{
        	datatable.addCell(CpdUtils.createNoBorderCell("--",font,null,false));
        }
        
        doc.add(datatable);
    }
    
    /**
     * 打印大牌信息
     * @param doc
     * @param font
     * @param type
     * @param cr
     * @param periodVo
     * @throws DocumentException
     */
    private void exportPdfCpdType(Document doc,Font font , CpdType type , CpdRegistration cr , CpdPeriodVO periodVo,String lang) throws DocumentException{
    	PdfPTable datatable = new PdfPTable(3);
    	datatable.setSpacingBefore(5f);//加入之前的空格
    	datatable.getDefaultCell().setPadding(2);//表格的padding设置
    	datatable.setTotalWidth(new float[]{130f,200f,200f});
        datatable.setWidthPercentage(100);
        datatable.setLockedWidth(true);
        datatable.getDefaultCell().setHorizontalAlignment( Element.ALIGN_LEFT);
        datatable.setHorizontalAlignment(Element.ALIGN_LEFT);
		datatable.addCell(CpdUtils.createNoBorderCell("",font,Element.ALIGN_LEFT,true));
        datatable.addCell(CpdUtils.createNoBorderCell(LabelContent.get(lang, "label_core_cpt_d_management_171")+": ",font,Element.ALIGN_LEFT,false));
        datatable.addCell(CpdUtils.createNoBorderCell(LabelContent.get(lang, "label_core_cpt_d_management_172")+": ",font,Element.ALIGN_LEFT,false));
        if(null!=type){
        	datatable.addCell(CpdUtils.createNoBorderCell(type.getCt_license_alias()+": ",font,Element.ALIGN_RIGHT,true));
        }
        if(null!=cr){
        	datatable.addCell(CpdUtils.createNoBorderCell(cr.getCr_reg_number(),font,Element.ALIGN_LEFT,false));
        }else{
        	datatable.addCell(CpdUtils.createNoBorderCell("--",font,Element.ALIGN_LEFT,false));
        }
        
        if(null!=periodVo){
        	
        	StringBuffer periodTime = new StringBuffer();
        	periodTime.append(DateUtil.getInstance().dateToString(periodVo.getStartTime(), DateUtil.patternH));
        	periodTime.append(" - ");
        	periodTime.append(DateUtil.getInstance().dateToString(periodVo.getEndTime(), DateUtil.patternH));
        	datatable.addCell(CpdUtils.createNoBorderCell(periodTime.toString()	,font,Element.ALIGN_LEFT,true));
        }else{
        	datatable.addCell(CpdUtils.createNoBorderCell("--"	,font,Element.ALIGN_LEFT,true));
        }
        
        doc.add(datatable);
    }
	
    /**打印PDF
     * @param userList
     * @param cghiCtIdArray
     * @param period
     * @param lang
     * @param wizbini
     * @param formatType
     * @param sortType
     * @param fileNameEmail  //当fileNameEmail为null的时候，是从页面上导出报表，当fileNameEmail不为null的时候，为发送邮件时生成报表，先给报表起名
     * @param emsg_id    //当emsg_id为0的时候，是从页面上导出报表，当emsg_id不为0的时候，为发送邮件时生成报表，获取邮件内容id
     * @return
     * @throws Exception
     */
	public String expor(List<RegUser> userList, Long[] cghiCtIdArray,int period,String lang,
			WizbiniLoader wizbini,int formatType, int sortType,String fileNameEmail,long emsg_id) throws Exception{
		
		LinkedHashMap<String, CpdGroup> header = getCpdGroupHeaderByTypeId(java.util.Arrays.asList(cghiCtIdArray), true);
		//获取大牌信息 由于需求改为大牌单选故只获取第一个值就好
		CpdType cpdType = null;
		if(null!=cghiCtIdArray && cghiCtIdArray.length>0){
			cpdType = cpdRegistrationMgtService.getCpdTypeByid(cghiCtIdArray[0]);
		}
		String basePath = wizbini.getFileUploadTmpDirAbs() + File.separator;
        Font tableFont = getTableFont();
        Font titleFont = getTitleFont();
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int curYear = cal.get(Calendar.YEAR);
        int headerCol = 0;//表头总列数
        
        if(null == header || header.size()<=0){
        	headerCol = 3;
        }else if(header.size()<=20){
        	headerCol = 3+header.size();
        }else{
        	headerCol = 23;
        }
        
        //减去超过十个的表头
        if(null != header && header.size()>0){
	        Iterator<Entry<String, CpdGroup>> iterator= header.entrySet().iterator();  
	        LinkedHashMap<String, CpdGroup> tmp = new LinkedHashMap<String, CpdGroup>();
	        int index = 1;
	        while(iterator.hasNext()){   
	        	if(index > headerCol-3){ //减去课程显示等3个固定cell
	        		break;
	        	}
	            Map.Entry<String, CpdGroup> entry = iterator.next();  
	            tmp.put(entry.getKey(), entry.getValue());
	            index++;
	        }  
	        header = tmp;
        }
        
        String download = null;
		//全部打印用户在一个PDF文件
		if(formatType==FORMAT_TYPE_IN_ONE){
			download = printInOnePdf(wizbini,basePath,userList,period,titleFont,
					tableFont,cpdType,lang,headerCol,header,sortType,curYear, fileNameEmail, emsg_id);
		}else{
			download = printForEachLearner(wizbini,basePath,userList,period,titleFont,
					tableFont,cpdType,lang,headerCol,header,sortType,curYear);
		}
		return download;
	}
	
	private String printForEachLearner(WizbiniLoader wizbini,String basePath,List<RegUser> userList
			,int period,Font titleFont ,Font tableFont ,CpdType cpdType,String lang , 
			int headerCol, LinkedHashMap<String, CpdGroup> header,int sortType,int curYear) throws Exception{
		
    	String download_pdf = null;
    	FileOutputStream fout = null;
    	Document doc = null;
    	String download_zip = null;
		try{
	    	Date currentTime = new Date();
	    	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	    	String zipDirPath = basePath+formatter.format(currentTime);
	    	String download_zip_name = "Individual_CPD_Hours_Report"+formatter.format(currentTime) + ".zip";
	    	download_zip = formatter.format(currentTime)+ File.separator + download_zip_name;
	    	String zipFilePath = zipDirPath + File.separator + download_zip_name;
	    	 File zipFile = new File(zipDirPath);
			 if (!zipFile.exists()) {
				 zipFile.mkdirs();
			 }
			if(null!=userList && userList.size()>0){
				for(RegUser user : userList){
					
			    	//创建pdf目录
					currentTime = new Date();
			    	download_pdf = formatter.format(currentTime)+".pdf";
			    	//创建Pdf的输出
			    	fout = new FileOutputStream(zipFile+File.separator+download_pdf);
			    	//创建pdf文件
				 	doc = new Document(PageSize.A4.rotate(),20,20,20,40);
				 	
		    		PdfWriter pdfWriter = PdfWriter.getInstance(doc,  fout);
		    		//设置自定义翻页事件，用于制定页头页脚
		    		PdfPageEvent pageEvent  =  new PdfPageEvent();  
		    		pdfWriter.setPageEvent(pageEvent);
		    		doc.open();
		    		doc.newPage();
					//打印Title
				 	exportPdfTitle(doc,period,titleFont,lang);
					//打印用户信息
					setPdfUserDetail(doc,tableFont,user,lang);
					//打印注册信息
					CpdRegistration cr = null;
					CpdPeriodVO periodVo = null;
			        if(null!=cpdType){
			        	cr = cpdRegistrationMgtService.getCpdRegistrationByPeriod(cpdType.getCt_id(),user.getUsr_ent_id(),period);
			        	periodVo =cpdUtilService.getPeriod(period, cpdType.getCt_id());
			        }
			        exportPdfCpdType(doc,tableFont,cpdType,cr,periodVo,lang);
					//打印表头
					PdfPTable datatable = exportHeader(headerCol,header,doc,lang,tableFont);
					//打印内容
					LinkedHashMap<Long , CpdGroupSumVo> totleMap 
						= exportAwardData(headerCol,datatable,user.getUsr_ent_id(),cpdType.getCt_id(),header,tableFont,sortType,period,curYear,cr,lang);
					
					doc.add(datatable);
					//打印remark
					exportRemark(doc,tableFont,headerCol,period);
					if(doc!=null){
						doc.close();
					}
					if(fout!=null){
						fout.close();
					}
				}
				
				this.zipRpt(zipDirPath, zipFilePath);
			}else{
		    	
				
				//如果没有相关用户打印一份没有no data的pdf
				//创建pdf目录
				currentTime = new Date();
		    	download_pdf = formatter.format(currentTime)+".pdf";
		    	//创建Pdf的输出
		    	fout = new FileOutputStream(zipFile+File.separator+download_pdf);
		    	//创建pdf文件
			 	doc = new Document(PageSize.A4.rotate(),20,20,20,40);
			 	
	    		PdfWriter pdfWriter = PdfWriter.getInstance(doc,  fout);
	    		//设置自定义翻页事件，用于制定页头页脚
	    		PdfPageEvent pageEvent  =  new PdfPageEvent();  
	    		pdfWriter.setPageEvent(pageEvent);
	    		doc.open();
	    		doc.newPage();
				//打印Title
			 	exportPdfTitle(doc,period,titleFont,lang);
			 	
			 	PdfPTable datatable = new PdfPTable(1);
		    	datatable.setSpacingBefore(50f);//加入之前的空格
		    	//打印用户信息
				setPdfUserDetail(doc,tableFont,null,lang);
				//打印注册信息
		        exportPdfCpdType(doc,tableFont,cpdType,null,null,lang);
    			datatable = exportHeader(headerCol,header,doc,lang,tableFont);
    			//打印内容
				LinkedHashMap<Long , CpdGroupSumVo> totleMap 
					= exportAwardData(headerCol,datatable,null,cpdType.getCt_id(),header,tableFont,sortType,period,curYear,null,lang);
							 	this.addNoDataCell(datatable, 1);
			 	doc.add(datatable);
			 	//打印remark
				exportRemark(doc,tableFont,headerCol,period);
				
				if(doc!=null){
					doc.close();
				}
				if(fout!=null){
					fout.close();
				}
				this.zipRpt(zipDirPath, zipFilePath);
			}
			
			
			
		}catch (Exception e) {
			//CommonLog.error(e.getMessage(),e);
			e.printStackTrace();
		}finally{
			if(doc!=null){
				doc.close();
			}
			if(fout!=null){
				fout.close();
			}
		}
		return download_zip;
	}
	
    /**
     * @param fileNameEmail  //当fileNameEmail为null的时候，是从页面上导出报表，当fileNameEmail不为null的时候，为发送邮件时生成报表，先给报表起名
     * @param emsg_id    //当emsg_id为0的时候，是从页面上导出报表，当emsg_id不为0的时候，为发送邮件时生成报表，获取邮件内容id
     * @return
     */
	private String printInOnePdf(WizbiniLoader wizbini,String basePath,List<RegUser> userList
			,int period,Font titleFont ,Font tableFont ,CpdType cpdType,String lang , 
			int headerCol, LinkedHashMap<String, CpdGroup> header,int sortType,int curYear,String fileNameEmail,long emsg_id) throws Exception{
    	//创建pdf目录
    	String download_pdf ="";
    	//创建Pdf的输出
    	FileOutputStream fout = null;
    	
    	//生成文件名   并 将文件存到指定位置  
        String fileName = "";
        if(null != fileNameEmail){
            basePath =  wizbini.getWebDocRoot()+dbUtils.SLASH + Message.MSG_ATTACHMENT_PATH + dbUtils.SLASH + emsg_id;
            download_pdf =fileNameEmail;
            File distPath = new File(basePath);
            if (!distPath.exists()) {
                distPath.mkdirs();
            }
            fout = new FileOutputStream(basePath+ dbUtils.SLASH +download_pdf);
        }else{
            download_pdf = createPdfDocDir(wizbini.getFileUploadTmpDirAbs() + File.separator);
            fout = new FileOutputStream(basePath+download_pdf);
        }
    	
    	//创建pdf文件
	 	Document doc = new Document(PageSize.A4.rotate(),20,20,20,40);
	   	try{
    		PdfWriter pdfWriter = PdfWriter.getInstance(doc,  fout);
    		//设置自定义翻页事件，用于制定页头页脚
    		PdfPageEvent pageEvent  =  new PdfPageEvent();  
    		pdfWriter.setPageEvent(pageEvent);
    		doc.open();
    		doc.newPage();
    		//exportPdfTitle(doc,period,titleFont,lang);
    		if(null!=userList && userList.size()>0){
    			for(RegUser user : userList){
    				exportPdfTitle(doc,period,titleFont,lang);
    				//打印用户信息
					setPdfUserDetail(doc,tableFont,user,lang);
					//打印大牌周期信息
					CpdRegistration cr = null;
					CpdPeriodVO periodVo = null;
			        if(null!=cpdType){
			        	cr = cpdRegistrationMgtService.getCpdRegistrationByPeriod(cpdType.getCt_id(),user.getUsr_ent_id(),period);
			        	periodVo =cpdUtilService.getPeriod(period, cpdType.getCt_id());
			        }
			        //打印注册信息
			        exportPdfCpdType(doc,tableFont,cpdType,cr,periodVo,lang);
					//打印表头
					PdfPTable datatable = exportHeader(headerCol,header,doc,lang,tableFont);
					//打印内容
					LinkedHashMap<Long , CpdGroupSumVo> totleMap 
						= exportAwardData(headerCol,datatable,user.getUsr_ent_id(),cpdType.getCt_id(),header,tableFont,sortType,period,curYear,cr,lang);
					
					doc.add(datatable);
					  //打印remark
					exportRemark(doc,tableFont,headerCol,period);
					doc.newPage();
				}
    		}else{
				//打印用户信息
				setPdfUserDetail(doc,tableFont,null,lang);
				//打印注册信息
		        exportPdfCpdType(doc,tableFont,cpdType,null,null,lang);
    			PdfPTable datatable = exportHeader(headerCol,header,doc,lang,tableFont);
    			//打印内容
				LinkedHashMap<Long , CpdGroupSumVo> totleMap 
					= exportAwardData(headerCol,datatable,null,cpdType.getCt_id(),header,tableFont,sortType,period,curYear,null,lang);
				
    	        doc.add(datatable);
    	      //打印remark
				exportRemark(doc,tableFont,headerCol,period);
    		}
            
    	}catch (Exception e) {
    		CommonLog.error(e.getMessage(),e);
			//e.printStackTrace();
		}finally{
			if(doc!=null){
				doc.close();
			}
			fout.close();
		}
	   	return download_pdf;
	}
	
	/***
	 * 获取下属用户
	 * @param usertIdArray
	 * @param exportUserType
	 * @param isTcIndependent
	 * @param prof
	 * @return
	 */
	public List<RegUser> getSubordinates(Long[] usertIdArray,int exportUserType,boolean isTcIndependent, loginProfile prof, int period, long ct_id){
		
        Map<String,Object> paramUser = new HashMap<String,Object>();
        paramUser.put("tc_independent", isTcIndependent);
        paramUser.put("my_top_tc_id", prof.my_top_tc_id);
        paramUser.put("usr_ent_id", prof.usr_ent_id);
        paramUser.put("current_role", prof.current_role);
        //当前角色是否与培训中心关联
        paramUser.put("is_role_tc_ind", AccessControlWZB.isRoleTcInd(prof.current_role));
        //
        CpdPeriodVO periodVo =cpdUtilService.getPeriod(period, ct_id);
        if(null != periodVo){
        	paramUser.put("period_end_time", periodVo.getEndTime());
        	paramUser.put("period_start_time", periodVo.getStartTime());
        }
        paramUser.put("ct_id", ct_id);
        paramUser.put("period", period);
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int curYear = cal.get(Calendar.YEAR);

        CpdPeriodVO cpdPeriodVOs = cpdUtilService.getCurrentPeriod(ct_id);
        
        //下属用户
        List<RegUser> subordinates  = null;
        if(exportUserType==0){
            paramUser.put("supervise", "SUPERVISE");//所有下属
            if(curYear == period || (null != cpdPeriodVOs && cpdPeriodVOs.getPeriod() == period) ){
                subordinates  = regUserMapper.findUserListForCpdExcel(paramUser);//当前表查询
            }else{
                subordinates  = regUserMapper.findUserListForCpdExcelHistory(paramUser);//历史表查询
            }
        }else if(exportUserType == 1){//直属下属
            if(curYear == period || (null != cpdPeriodVOs && cpdPeriodVOs.getPeriod() == period) ){
                subordinates  = regUserMapper.findUserListForCpdExcel(paramUser);//当前表查询
            }else{
                subordinates  = regUserMapper.findUserListForCpdExcelHistory(paramUser);//历史表查询
            }
        }else{
        	//如果是已经选择了用户,那么直接搜索选择的用户
            if(null!=usertIdArray && usertIdArray.length>0){
            	paramUser.clear();
            	paramUser.put("usrlist", java.util.Arrays.asList(usertIdArray));
                paramUser.put("ct_id", ct_id);
                paramUser.put("period", period);
                if(null != periodVo){
                    paramUser.put("period_end_time", periodVo.getEndTime());
                    paramUser.put("period_start_time", periodVo.getStartTime());
                }
                if(curYear == period || (null != cpdPeriodVOs && cpdPeriodVOs.getPeriod() == period) ){
                    subordinates = regUserMapper.findUserListByIds(paramUser);//当前表查询
                }else{
                    subordinates = regUserMapper.findUserListByIdsHistory(paramUser);//历史表查询
                }
            }
        }
        return subordinates;
	}
	
	/**
	 * 判断用户是否存在已注册且未过期记录
	 * @param usertIdArray
	 * @param period
	 * @param ct_id
	 * @return
	 */
	 public boolean isNotPastCpdReg(Long usertId, int period, Long[] ct_id){
	       boolean isNotPastCpdReg=false;
	        Map<String,Object> paramUser = new HashMap<String,Object>();
	        CpdPeriodVO periodVo =cpdUtilService.getPeriod(period, ct_id[0]);
	        if(null != periodVo){
	            paramUser.put("period_end_time", periodVo.getEndTime());
	            paramUser.put("period_start_time", periodVo.getStartTime());
	        }
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            int curYear = cal.get(Calendar.YEAR);
            
            CpdPeriodVO cpdPeriodVOs = cpdUtilService.getCurrentPeriod(ct_id[0]);
	        paramUser.put("ct_id", ct_id[0]);
	        paramUser.put("period", period);
            paramUser.put("usr_ent_id", usertId);
            List<RegUser> regUsers = null;
            if(curYear == period || (null != cpdPeriodVOs && cpdPeriodVOs.getPeriod() == period) ){
                regUsers = regUserMapper.findUserCpdRegListById(paramUser);//当前表查询
            }else{
                regUsers = regUserMapper.findUserCpdRegListByIdHistory(paramUser);//历史表查询
            }
            if(regUsers!=null && regUsers.size()>0){
                isNotPastCpdReg = true;//存在已注册且未过期记录
            }
	        return isNotPastCpdReg;
	    }
	
	
    /**
     * 获取报表 通过是否
     * @param typeIds 需要显示的大牌ID
     * @param display_in_report_ind 是否展示在报表 true or false （null默认与true相同）
     * @return
     */
    public LinkedHashMap<String,CpdGroup> getCpdGroupHeaderByTypeId(List<Long> typeIds,boolean display_in_report_ind ){
    	LinkedHashMap<String,CpdGroup> headerMap = new LinkedHashMap<String,CpdGroup>();
    	List<CpdGroup> groupList = cpdGroupService.searchByType(typeIds,display_in_report_ind);
    	if(null!=groupList && groupList.size()>0){
    		for(CpdGroup group : groupList){
    			headerMap.put(group.getCg_id()+CpdUtils.CPD_CORE_HEADER_KEY , group);
    			if(1==group.getCg_contain_non_core_ind()){
    				headerMap.put(group.getCg_id()+CpdUtils.CPD_NON_CORE_HEADER_KEY ,  group);
    			}
    		}
    	}
    	return headerMap;
    }

    private String createPdfDocDir(String fileUploadTmpDirAbs) throws FileNotFoundException{
    	Date currentTime = new Date();
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    	 String basePath = fileUploadTmpDirAbs;
    	 File distPath = new File(basePath);
		 if (!distPath.exists()) {
			distPath.mkdirs();
		 }
	 	String download_pdf = formatter.format(currentTime)+".pdf";
	 	return download_pdf;
    }
    
    /**
     * 打印表头
     * @param colNum
     * @param header
     * @param doc
     * @param lang
     * @param font
     * @return
     * @throws DocumentException
     * @throws IOException
     */
    private PdfPTable exportHeader(int colNum , LinkedHashMap<String, CpdGroup> header ,
    		Document doc ,String lang , Font font) throws DocumentException, IOException{
		 // 中文字体
		// 创建有colNumber(2)列的表格
        PdfPTable datatable = new PdfPTable(colNum);
        datatable.setSpacingBefore(10f);//加入之前的空格
        datatable.setTotalWidth(doc.right() - doc.left());  
        datatable.setLockedWidth(true);
        datatable.setWidthPercentage(100); // 表格的宽度百分比
        datatable.getDefaultCell().setPadding(2);//表格的padding设置
        datatable.getDefaultCell().setBorderWidth(2);
        datatable.getDefaultCell().setBackgroundColor(Color.WHITE);
        datatable.getDefaultCell().setHorizontalAlignment( Element.ALIGN_CENTER);
        datatable.addCell(new Paragraph(LabelContent.get(lang, "label_core_cpt_d_management_153"), font));
        datatable.addCell(new Paragraph(LabelContent.get(lang, "label_core_cpt_d_management_154"), font));
        datatable.addCell(new Paragraph(LabelContent.get(lang, "label_core_cpt_d_management_155"), font));
        // 添加表头元素
        Iterator<Entry<String, CpdGroup>> iterator= header.entrySet().iterator();  
        while(iterator.hasNext()){   
            Map.Entry<String, CpdGroup> entry = iterator.next();  
            if(entry.getKey().equalsIgnoreCase(entry.getValue().getCg_id()+CpdUtils.CPD_CORE_HEADER_KEY)){
            	datatable.addCell(new Paragraph(entry.getValue().getCg_alias()+" Core Hours", font));
            }else if(entry.getKey().equalsIgnoreCase(entry.getValue().getCg_id()+CpdUtils.CPD_NON_CORE_HEADER_KEY)){
            	datatable.addCell(new Paragraph(entry.getValue().getCg_alias()+" Non-core Hours", font));
            }
        }  
        datatable.setHeaderRows(1); 
        // 表头结束
        datatable.getDefaultCell().setBorderWidth(1);
        return datatable;
    }
    
    /**
     * 输入显示时数的单元CELL
     * @param award
     * @param datatable
     * @param font
     */
    private void setAwardCell(Float award , PdfPTable datatable,Font font){
		if(null!=award){
    		Float core = FormatUtil.getInstance().scaleFloat(award, 2, BigDecimal.ROUND_HALF_DOWN);
    		datatable.addCell(new Paragraph(String.valueOf(core), font));
		}else{
    		datatable.addCell(new Paragraph("0.0", font));
		}
    }
    
    /**
     * 打印汇总信息
     * @param datatable
     * @param font
     * @param header
     * @param totleMap
     */
	private void exportTotleData(PdfPTable datatable, Font font,
			LinkedHashMap<String, CpdGroup> header ,LinkedHashMap<Long , CpdGroupSumVo> totleMap ,String lang  ){
		
		Iterator<Entry<Long, CpdGroupSumVo>> totle_iterator= totleMap.entrySet().iterator(); 
		//计算借核心时数逻辑
        while(totle_iterator.hasNext()){   
        	Map.Entry<Long, CpdGroupSumVo> entry = totle_iterator.next();  
        	CpdUtils.calBorrowHours(entry.getValue());
        }
		
		Paragraph paragraph = new Paragraph(LabelContent.get(lang, "label_core_cpt_d_management_173")+": ", font);
		PdfPCell cell = new PdfPCell(paragraph);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setColspan(3);
		datatable.addCell(cell);
		Iterator<Entry<String, CpdGroup>> iterator= header.entrySet().iterator(); 
		//按顺序循环表头
        while(iterator.hasNext()){   
        	Map.Entry<String, CpdGroup> entry = iterator.next();  
        	CpdGroup cg = entry.getValue();
        	if(totleMap.containsKey(cg.getCg_id())){
             	String coreKey = cg.getCg_id()+CpdUtils.CPD_CORE_HEADER_KEY;
                String nonCoreKey = cg.getCg_id()+CpdUtils.CPD_NON_CORE_HEADER_KEY;
                CpdGroupSumVo sumVo =totleMap.get(cg.getCg_id());
         		if(null!=sumVo){
         			//如果用户没有注册的课程显示--
         			if(!sumVo.isReg_ind()){
         				datatable.addCell(new Paragraph("--", font));
         				continue;
         			}
         	    	if(coreKey.equalsIgnoreCase(entry.getKey())){
         	    		setAwardCell(sumVo.getSum_award_core_hours(),datatable,font);
                	}else if(nonCoreKey.equalsIgnoreCase(entry.getKey())){
                		setAwardCell(sumVo.getSum_award_non_core_hours(),datatable,font);
    	            }
         		}
        	}else{
        		datatable.addCell(new Paragraph("--", font));
        	}
        }
		paragraph = new Paragraph(LabelContent.get(lang, "label_core_cpt_d_management_174")+": ", font);
		cell = new PdfPCell(paragraph);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setColspan(3);
		datatable.addCell(cell);
		//按顺序循环表头
		iterator= header.entrySet().iterator(); 
        while(iterator.hasNext()){   
        	Map.Entry<String, CpdGroup> entry = iterator.next();  
        	CpdGroup cg = entry.getValue();
        	if(totleMap.containsKey(cg.getCg_id())){
             	String coreKey = cg.getCg_id()+CpdUtils.CPD_CORE_HEADER_KEY;
                String nonCoreKey = cg.getCg_id()+CpdUtils.CPD_NON_CORE_HEADER_KEY;
                CpdGroupSumVo sumVo =totleMap.get(cg.getCg_id());
         		if(null!=sumVo){
         			//如果用户没有注册的课程显示--
         			if(!sumVo.isReg_ind()){
         				datatable.addCell(new Paragraph("--", font));
         				continue;
         			}
         	    	if(coreKey.equalsIgnoreCase(entry.getKey())){
         	    		setAwardCell(sumVo.getReq_core(),datatable,font);
                	}else if(nonCoreKey.equalsIgnoreCase(entry.getKey())){
                		setAwardCell(sumVo.getReq_non_core(),datatable,font);
    	            }
         		}
        	}else{
        		datatable.addCell(new Paragraph("--", font));
        	}
        }
        
		paragraph = new Paragraph(LabelContent.get(lang, "label_core_cpt_d_management_175")+": ", font);
		cell = new PdfPCell(paragraph);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setColspan(3);
		datatable.addCell(cell);
		//按顺序循环表头
		iterator= header.entrySet().iterator(); 
        while(iterator.hasNext()){   
        	Map.Entry<String, CpdGroup> entry = iterator.next();  
        	CpdGroup cg = entry.getValue();
        	if(totleMap.containsKey(cg.getCg_id())){
             	String coreKey = cg.getCg_id()+CpdUtils.CPD_CORE_HEADER_KEY;
                String nonCoreKey = cg.getCg_id()+CpdUtils.CPD_NON_CORE_HEADER_KEY;
                CpdGroupSumVo sumVo =totleMap.get(cg.getCg_id());
         		if(null!=sumVo){
         			//如果用户没有注册的课程显示--
         			if(!sumVo.isReg_ind()){
         				datatable.addCell(new Paragraph("--", font));
         				continue;
         			}
         	    	if(coreKey.equalsIgnoreCase(entry.getKey())){
         	    		setAwardCell(sumVo.getOutStanding_core(),datatable,font);
                	}else if(nonCoreKey.equalsIgnoreCase(entry.getKey())){
                		setAwardCell(sumVo.getOutStanding_non_core(),datatable,font);
    	            }
         		}
        	}else{
        		datatable.addCell(new Paragraph("--", font));
        	}
        }
	}
    
	/**
	 * 打印获得时数
	 * @param colNum
	 * @param datatable
	 * @param usrEntId
	 * @param ctId
	 * @param header
	 * @param font
	 * @param sort
	 * @param period
	 * @param curYear
	 * @param cpdRegistration
	 * @return
	 */
	private LinkedHashMap<Long , CpdGroupSumVo> exportAwardData(int colNum ,PdfPTable datatable,Long usrEntId , Long ctId,LinkedHashMap<String, CpdGroup> header , Font font 
			,int sort ,int period ,int curYear , CpdRegistration cpdRegistration ,String lang){
		//合算map
		LinkedHashMap<Long , CpdGroupSumVo> totleMap = new LinkedHashMap<Long , CpdGroupSumVo>();
		List<IndividualReportVo> awardRecordList = null;
		
		CpdPeriodVO cpdPeriodVO =  cpdUtilService.getCurrentPeriod(ctId);
		
		//当年的数据到cpdLrnAwardRecord找 往年到历史记录表找 || cpdPeriodVO.getPeriod() == period
		if(period == curYear || cpdPeriodVO.getPeriod() == period){
			if(null!=cpdRegistration){
				awardRecordList = cpdLrnAwardRecordService.getRecordForIndividualReport(usrEntId, ctId,sort,period,cpdRegistration.getCr_id());
			}
		}else{
			awardRecordList = cpdGroupRegHoursHistoryService.getRecordForIndividualReport(usrEntId, ctId,sort,period);
		}
			//在当前年份周期中可能存在上一年份周期的数据 例如：当前20170505  A牌照开始月份：8  则当前A牌照数据对应的周期为 2016
			/*if(null!=awardRecordList && awardRecordList.size()>0){
				if(null!=awardHistoryRecordList && awardHistoryRecordList.size()>0){
					for(int i = 0;i < awardHistoryRecordList.size(); i++){
						awardRecordList.add(awardHistoryRecordList.get(i));
					}
				}
			}else{
				awardRecordList = awardHistoryRecordList;
			}*/
			
		 if(null!=awardRecordList && awardRecordList.size()>0 || null!=cpdRegistration){
			if(null!=awardRecordList && awardRecordList.size()>0){
				  for(IndividualReportVo record : awardRecordList){
					  if(null != record.getItm_title()){
						  if(record.getItm_title().indexOf(">") > 0){
							  record.setItm_title(record.getItm_title().substring(0,record.getItm_title().indexOf(">"))); 
						  }
					  }
		            	//课程名称
		                //datatable.addCell(new Paragraph(record.getItm_title(), font));
					   //判断所需时数是否全部为0
					    Iterator<Entry<String, CpdGroup>> iterators= header.entrySet().iterator(); 
		                boolean check_award = check_award(iterators,usrEntId ,period, record,totleMap,cpdRegistration);  
					    //if(check_award){
			                Paragraph classNameParagraph = new Paragraph(record.getItm_title(), font);
			        		PdfPCell classNameCell = new PdfPCell(classNameParagraph);
			        		classNameCell.setHorizontalAlignment(Element.ALIGN_LEFT);
			        		if(check_award){
			        		    datatable.addCell(classNameCell);
	                            //获得时间
	                            datatable.addCell(new Paragraph(DateUtil.getInstance().dateToString(record.getAward_datetime(), DateUtil.patternH), font));
	                            //编号
	                            datatable.addCell(new Paragraph(record.getAccreditation_code(), font));
			        		}
			        		
			                
			                Iterator<Entry<String, CpdGroup>> iterator= header.entrySet().iterator(); 
			                
			               //按顺序循环表头
			                while(iterator.hasNext()){   
			                	 Map.Entry<String, CpdGroup> entry = iterator.next();  
			                	 CpdGroup cg = entry.getValue();
			                	 List<CpdGroupRegistration>  cgrList = cpdGroupRegistrationService.getByCpdGroupRegistration(cg.getCg_id(),period,usrEntId);
			                	 //如果该用户有注册
			                	 if(cgrList.size()>0){
			     	                //循环赋值小牌获得时数
			     	                List<CpdGroupSumVo> groupList = record.getCpd_group_list();
			     	                Float award = 0.0f;
			     	            	if(null!=groupList && groupList.size()>0 ){
			     	            		for(CpdGroupSumVo vo : groupList){
			     	            			Long cgId = vo.getCg_id();
			 				            	String coreKey = cgId+CpdUtils.CPD_CORE_HEADER_KEY;
			 				            	String nonCoreKey = cgId+CpdUtils.CPD_NON_CORE_HEADER_KEY;
			 				            	if(coreKey.equalsIgnoreCase(entry.getKey())){
			 				            		award = vo.getSum_award_core_hours();
			 				            	}else if(nonCoreKey.equalsIgnoreCase(entry.getKey())){
			 				            		award = vo.getSum_award_non_core_hours();
			 				            	}
			     	            		}
			     	            	}
			 		            	String coreKey = cg.getCg_id()+CpdUtils.CPD_CORE_HEADER_KEY;
						            String nonCoreKey = cg.getCg_id()+CpdUtils.CPD_NON_CORE_HEADER_KEY;
			     	            	//计算出每个小牌获得积分的总和
			     	            	if(!totleMap.containsKey(cg.getCg_id())){
			     	            		CpdGroupRegHours cgrh = cpdGroupRegistrationService.getCpdGroupRegHours(cpdRegistration.getCr_id(), cg.getCg_id(), usrEntId, period);
			     	            		CpdGroupSumVo sumVo = new CpdGroupSumVo();
			     	            		sumVo.setReg_ind(true);
			     	            		sumVo.setContain_non_core_ind(cg.getCg_contain_non_core_ind()==1?true:false);
			     	            		sumVo.setCg_id(entry.getValue().getCg_id());
			     	            		if(null!=cgrh){
			     	            			sumVo.setReq_core(cgrh.getCgrh_execute_core_hours());
			     	            			sumVo.setReq_non_core(cgrh.getCgrh_execute_non_core_hours());
			     	            		}else{
			     	            			sumVo.setReq_core(0.0f);
			     	            			sumVo.setReq_non_core(0.0f);
			     	            		}
						            	if(coreKey.equalsIgnoreCase(entry.getKey())){
						            		sumVo.setSum_award_core_hours(award);
						            	}else if(nonCoreKey.equalsIgnoreCase(entry.getKey())){
						            		sumVo.setSum_award_non_core_hours(award);
							            }
			     	            		totleMap.put(cg.getCg_id(), sumVo);
			     	            	}else{
			     	            		CpdGroupSumVo sumVo = totleMap.get(entry.getValue().getCg_id());
			     	            		if(null!=sumVo){
			    			            	if(coreKey.equalsIgnoreCase(entry.getKey())){
			    			            		Float core_hours = null==sumVo.getSum_award_core_hours()?0f:sumVo.getSum_award_core_hours();
			    			            		sumVo.setSum_award_core_hours(core_hours+award);
			    			            	}else if(nonCoreKey.equalsIgnoreCase(entry.getKey())){
			    			            		Float non_core_hours = null==sumVo.getSum_award_non_core_hours()?0f:sumVo.getSum_award_non_core_hours();
			    			            		sumVo.setSum_award_non_core_hours(non_core_hours+award);
			    				            }
			     	            		}
			     	            	}
			     	            	if(check_award){
			     	            	   setAwardCell(award,datatable,font);
			     	            	}
			     	            	
			                	 }else{
			                		 //如果没注册显示 '--'
			                		 datatable.addCell(new Paragraph("--", font));
		    	                	 if(!totleMap.containsKey(cg.getCg_id())){
		    	     	            		CpdGroupSumVo sumVo = new CpdGroupSumVo();
		    	     	            		sumVo.setReg_ind(false);
		    	     	            		sumVo.setCg_id(entry.getValue().getCg_id());
		    	                		 if(null!=cpdRegistration){
		    		     	            		CpdGroupRegHours cgrh = cpdGroupRegistrationService.getCpdGroupRegHours(cpdRegistration.getCr_id(), cg.getCg_id(), usrEntId, period);
	
			    	     	            		if(null!=cgrh){
			    	     	            			sumVo.setReq_core(cgrh.getCgrh_execute_core_hours());
			    	     	            			sumVo.setReq_non_core(cgrh.getCgrh_execute_non_core_hours());
			    	     	            		}else{
			    	     	            			sumVo.setReq_core(0.0f);
			    	     	            			sumVo.setReq_non_core(0.0f);
			    	     	            		}
			    	     	            		totleMap.put(cg.getCg_id(), sumVo);
		    	                		 }else{
		    	                			 sumVo.setReq_core(0.0f);
	    	     	            			 sumVo.setReq_non_core(0.0f);
		     	            			     totleMap.put(cg.getCg_id(), sumVo);
		    	                		 }
		    	     	            }
			                	 }
			                }
					    //}// 检查所需时数是否全部为0   end
		            }
			}else{
				  Iterator<Entry<String, CpdGroup>> iterator= header.entrySet().iterator(); 
				  while(iterator.hasNext()){   
	                	 Map.Entry<String, CpdGroup> entry = iterator.next();  
	                	 CpdGroup cg = entry.getValue();
	                	 if(!totleMap.containsKey(cg.getCg_id())){
	     	            		CpdGroupRegHours cgrh = cpdGroupRegistrationService.getCpdGroupRegHours(cpdRegistration.getCr_id(), cg.getCg_id(), usrEntId, period);

                                List<CpdGroupRegistration>  cgrList = cpdGroupRegistrationService.getByCpdGroupRegistration(cg.getCg_id(),period,usrEntId);//小牌注册记录
                                
	     	            		CpdGroupSumVo sumVo = new CpdGroupSumVo();
	     	              		if(cgrList!=null && cgrList.size()>0){//小牌注册记录不为空并且不过期
	     	            			sumVo.setReg_ind(true);
	     	            		}else{
	     	            			sumVo.setReg_ind(false);
	     	            		}
	     	            		sumVo.setCg_id(entry.getValue().getCg_id());
	     	            		if(null!=cgrh){
	     	            			sumVo.setReq_core(cgrh.getCgrh_execute_core_hours());
	     	            			sumVo.setReq_non_core(cgrh.getCgrh_execute_non_core_hours());
	     	            		}else{
	     	            			sumVo.setReq_core(0.0f);
	     	            			sumVo.setReq_non_core(0.0f);
	     	            		}
	     	            		totleMap.put(cg.getCg_id(), sumVo);
	     	            	}
				  }
			}
          
          //打印总和
			exportTotleData(datatable,font,header,totleMap,lang);
		
		 }else{
 	       // addNoDataCell(datatable,colNum);
 	       //打印总和
			exportTotleData(datatable,font,header,totleMap,lang);

		 }
		 return totleMap;
	}
	
	/**
	 * 检查获得时数是否全部为0
	 * @param iterator
	 * @param usrEntId
	 * @param period
	 * @param record
	 * @param totleMap
	 * @param cpdRegistration
	 * @return
	 */
	private boolean check_award(Iterator<Entry<String, CpdGroup>> iterator,Long usrEntId ,int period, 
			      IndividualReportVo record,LinkedHashMap<Long , CpdGroupSumVo> totleMap, CpdRegistration cpdRegistration){
		boolean flg = false;
		
		 //按顺序循环表头
        while(iterator.hasNext()){   
        	 Map.Entry<String, CpdGroup> entry = iterator.next();  
        	 CpdGroup cg = entry.getValue();
        	 List<CpdGroupRegistration>  cgrList = cpdGroupRegistrationService.getByCpdGroupRegistration(cg.getCg_id(),period,usrEntId);
        	 //如果该用户有注册
        	 if(cgrList.size()>0){
	                //循环赋值小牌获得时数
	                List<CpdGroupSumVo> groupList = record.getCpd_group_list();
	                Float award = 0.0f;
	            	if(null!=groupList && groupList.size()>0 ){
	            		for(CpdGroupSumVo vo : groupList){
	            			Long cgId = vo.getCg_id();
			            	String coreKey = cgId+CpdUtils.CPD_CORE_HEADER_KEY;
			            	String nonCoreKey = cgId+CpdUtils.CPD_NON_CORE_HEADER_KEY;
			            	if(coreKey.equalsIgnoreCase(entry.getKey())){
			            		award = vo.getSum_award_core_hours();
			            	}else if(nonCoreKey.equalsIgnoreCase(entry.getKey())){
			            		award = vo.getSum_award_non_core_hours();
			            	}
			            	if(award > 0){
		            			return true;
		            		}
	            		}
	            	}
        	 }else{
        		 //如果没注册
        		 flg = true;
        	 }
        }
		
		return flg;
	}
	
	
	
	/**
	 * 打印报表remark
	 * @param datatable
	 * @param font
	 * @param colNum
	 * @param period
	 * @throws DocumentException 
	 */
	private void exportRemark(Document doc,Font font,int colNum,int period) throws DocumentException{
		
    	PdfPTable datatable = new PdfPTable(1);
    	datatable.setSpacingBefore(5f);//加入之前的空格
        datatable.setWidthPercentage(100);
        datatable.getDefaultCell().setPadding(2);//表格的padding设置
        datatable.getDefaultCell().setHorizontalAlignment( Element.ALIGN_LEFT);
        datatable.setHorizontalAlignment(Element.ALIGN_LEFT);
		
		String remark = cpdUtilService.getRemarkByPeriod(period, CpdReportRemark.INDIVIDUAL_REMARK_CODE);
		if(!StringUtils.isEmpty(remark)){
	        PdfPCell cell = new PdfPCell(new Paragraph(remark,font));
	        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	        cell.setColspan(colNum);
			cell.setBorder(Rectangle.NO_BORDER);
	        datatable.addCell(cell);
		}
		doc.add(datatable);
	}
	
	/**
	 * 添加没数据页面
	 * @param datatable
	 * @param colNum
	 */
	private void addNoDataCell(PdfPTable datatable , int colNum){
	        PdfPCell cell = new PdfPCell(new Paragraph("no data"));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setColspan(colNum);
	        datatable.addCell(cell);
	}
	
    private void zipRpt(String zipFilePath,String zipFileName) throws qdbErrMessage, IOException {
        File parentFile = new File(zipFilePath);
        File[] files = parentFile.listFiles();
        
        String[] pdfFiles = parentFile.list();

        //zip file name
        /*String[] filesName = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            filesName[i] = files[i].getName();
        }*/
        dbUtils.makeZip(zipFileName, zipFilePath, pdfFiles, false);
    }
}
