package com.cwn.wizbank.cpd.service;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwSysMessage;
import com.cwn.wizbank.cpd.vo.CpdHourVO;
import com.cwn.wizbank.cpd.vo.CpdLrnAwardRecordVO;
import com.cwn.wizbank.cpd.vo.IndividualReportVo;
import com.cwn.wizbank.entity.AeItemCPDGourpItem;
import com.cwn.wizbank.entity.CourseEvaluation;
import com.cwn.wizbank.entity.CpdGroup;
import com.cwn.wizbank.entity.CpdLrnAwardRecord;
import com.cwn.wizbank.persistence.CpdLrnAwardRecordMapper;
import com.cwn.wizbank.services.BaseService;
import com.cwn.wizbank.utils.CommonLog;
import com.cwn.wizbank.utils.DateUtil;
import com.cwn.wizbank.utils.LabelContent;
import com.cwn.wizbank.utils.Page;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CpdLrnAwardRecordService extends BaseService<CpdLrnAwardRecord> {
	
	@Autowired
	CpdLrnAwardRecordMapper cpdLrnAwardRecordMapper;
	@Autowired
	CpdGroupService cpdGroupService;
	@Autowired
	AeItemCPDItemService aeItemCPDItemService;
	/**
	 * 计算获得时数的总和
	 * @param entId 学员ID
	 * @param cgId 小牌id
	 * @param contInd 是否包含非核心时数
	 */
	public CpdHourVO  getTotleAwardHoursByCgId(Long entId , Long cgId ,int contInd , Date calStartTime , Date calEndTime){
		DateUtil dateUtil = DateUtil.getInstance();
		Map param = new HashMap<String,Object>();
		param.put("usr_ent_id", entId);
		param.put("cg_id", cgId);
		param.put("start_time", dateUtil.getDate(calStartTime,0,0,0) );
		param.put("end_time", dateUtil.getDate(calEndTime,23,59,59));
		CpdHourVO vo = cpdLrnAwardRecordMapper.sumAwardHours(param);
		if(1!=contInd && null!=vo){
			vo.setTotle_award_non_core_hours(0f);
		}
		return vo;
	}
	
	public List<CpdLrnAwardRecord>  getCpdLrnAwardRecord(Long entId , Long cgId,Integer period,Long cgrId){
		Map param = new HashMap<String,Object>();
		param.put("clar_usr_ent_id", entId);
		param.put("clar_cg_id", cgId);
        param.put("cgrh_cgr_id", cgrId);
		param.put("period", period);
		return cpdLrnAwardRecordMapper.getCpdLrnAwardRecord(param);
	}
	
	
    public static void delForOld(Connection con, long itm_id , long usr_ent_id , Long app_id)
		throws SQLException, qdbException ,cwSysMessage {
        StringBuffer SQLBuf = new StringBuffer(200);
        SQLBuf.append(" delete From cpdLrnAwardRecord ");
        SQLBuf.append(" Where clar_usr_ent_id = ? ");
        SQLBuf.append(" and clar_itm_id = ? ");
        if(null!=app_id){
        	SQLBuf.append(" and clar_app_id = ? ");
        }
        String SQL = new String(SQLBuf);
        PreparedStatement stmt = con.prepareStatement(SQL);
        try{
            stmt.setLong(1, usr_ent_id);
            stmt.setLong(2, itm_id);
            if(null!=app_id){
            	stmt.setLong(3, app_id);
            }
            stmt.executeUpdate();
        }catch(Exception e){
        	CommonLog.error(e.getMessage(),e);
        }finally {
			if(null!=stmt)
				stmt.close();
		}
    }

    
    /**
     * 获取指定小牌在当前课程中获取的最大核心时数
     * @param itm_id
     * @param cr_id
     * @param clar_acgi_id
     * @return
     */
	public List<CpdLrnAwardRecord> getMaxCPDCoreHours(long itm_id,long cg_id){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("clar_itm_id", itm_id);
		map.put("clar_cg_id", cg_id);
		return cpdLrnAwardRecordMapper.getMaxCPDCoreHours(map);
	}

	 /**
     * 获取指定小牌在当前课程中获取的最大非核心时数
     * @param itm_id
     * @param cg_id
     * @param clar_acgi_id
     * @return
     */
	public List<CpdLrnAwardRecord> getMaxCPDNonCoreHours(long itm_id,long cg_id){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("clar_itm_id", itm_id);
		map.put("clar_cg_id", cg_id);
		return cpdLrnAwardRecordMapper.getMaxCPDNonCoreHours(map);
	}
	
	/**
	 * cpt获得时数
	 * @param page
	 * @return
	 */
	public  Page<CpdLrnAwardRecord> searchAll(Page<CpdLrnAwardRecord> page,long itm_id ,String searchTxt) {
		page.getParams().put("itm_id", itm_id);
		page.getParams().put("searchTxt", searchTxt);
		page.getParams().put("status", CourseEvaluation.Completed);
		List<CpdLrnAwardRecord> results = cpdLrnAwardRecordMapper.searchAll(page);
		for (int i = 0; i < results.size(); i++) {
		    CpdLrnAwardRecord cpdLrnAwardRecord = results.get(i);
		    if( cpdLrnAwardRecord.getCpdLrnAwardRecordList() != null){
		        for (int j = 0; j < cpdLrnAwardRecord.getCpdLrnAwardRecordList().size(); j++) {
		            CpdLrnAwardRecord cpdLrnAwardRecordLit = cpdLrnAwardRecord.getCpdLrnAwardRecordList().get(j);
		            if(cpdLrnAwardRecordLit!=null){
		                if(cpdLrnAwardRecordLit.getClar_manul_ind()==1){
	                        cpdLrnAwardRecord.setClar_manul_ind(1);
	                        break;
	                    }
		            }
	                
	            }
		    }
		    
        }
		page.setResults(results);
		return page;
	}
	
	/**
	 * 通过appID获取用户获得时数
	 * @param app_id
	 * @param usr_ent_id
	 * @param itm_id
	 * @return
	 * @throws SQLException
	 * @throws qdbException
	 * @throws cwSysMessage
	 */
	public  List<CpdLrnAwardRecord> searchAllByAppId(long app_id,long usr_ent_id,long itm_id) throws SQLException, qdbException, cwSysMessage {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("app_id", app_id);
		map.put("itm_id", itm_id);
		map.put("usr_ent_id", usr_ent_id);
		map.put("status", CourseEvaluation.Completed);
		List<CpdLrnAwardRecord> results = cpdLrnAwardRecordMapper.searchAllByAppId(map);
		List<CpdGroup> list = cpdGroupService.getAllOrder(0);
		List<CpdLrnAwardRecord> cpdLrnAwardRecordList = new ArrayList<CpdLrnAwardRecord>();
		Map awardCPDGourpItemMap = aeItemCPDItemService.getCPDGourpItemMap(itm_id);
		for(CpdGroup cg : list){
			//该课程注册了小牌且需要时数不为0才显示
			if(awardCPDGourpItemMap.containsKey(cg.getCg_id())){
				//把该课程关联的小牌信息保存到集合
				CpdLrnAwardRecord clar = new CpdLrnAwardRecord();
				clar.setCg_alias(cg.getCg_alias());
				clar.setShow_award_core_hours(false);
				clar.setShow_award_non_core_hours(false);
				
				AeItemCPDGourpItem acgi =  (AeItemCPDGourpItem)awardCPDGourpItemMap.get(cg.getCg_id());
				if((null == acgi.getAcgi_award_core_hours() || acgi.getAcgi_award_core_hours() == 0) && (null==acgi.getAcgi_award_non_core_hours() || acgi.getAcgi_award_non_core_hours() == 0  || cg.getCg_contain_non_core_ind() == 0)){
					continue;
				}
				clar.setAeItemCPDGourpItem(acgi);
				clar.setClar_cg_id(cg.getCg_id());
				clar.setClar_ct_id(cg.getCg_ct_id());
				clar.setClar_acgi_id(acgi.getAcgi_id());
				//设置的核心时数是否大于0
				if(null!=acgi.getAcgi_award_core_hours() && acgi.getAcgi_award_core_hours() > 0){
					clar.setClar_award_core_hours((float) 0.0);
					clar.setShow_award_core_hours(true);
				}
				//设置的非核心时数是否大于0  且牌照设置拥有非核心时数
                if(null!=acgi.getAcgi_award_non_core_hours() && acgi.getAcgi_award_non_core_hours() > 0  && cg.getCg_contain_non_core_ind() != 0){
                	clar.setClar_award_non_core_hours((float) 0.0);
                	clar.setShow_award_non_core_hours(true);
				}
				//循环当前用户获得的牌照时数
                if(results.size() > 0){
					for(CpdLrnAwardRecord ur : results.get(0).getCpdLrnAwardRecordList()){
						//该记录对应课程的CPD牌照ID
						if(cg.getCg_id().equals(ur.getClar_cg_id())){
							clar.setClar_id(ur.getClar_id());
							//设置的核心时数是否大于0
							if(null!=acgi.getAcgi_award_core_hours() && acgi.getAcgi_award_core_hours() > 0){
								clar.setClar_award_core_hours(ur.getClar_award_core_hours());
							}
							//设置的非核心时数是否大于0  且牌照设置拥有非核心时数
			                if(null!=acgi.getAcgi_award_non_core_hours() && acgi.getAcgi_award_non_core_hours() > 0  && cg.getCg_contain_non_core_ind() != 0){
			                	clar.setClar_award_non_core_hours(ur.getClar_award_non_core_hours());
							}
						}//该记录对应课程的CPD牌照ID end
					}//循环当前用户获得的牌照时数 end
                }
				cpdLrnAwardRecordList.add(clar);
			}
		}
		
		results.get(0).setCpdLrnAwardRecordList(cpdLrnAwardRecordList);
		
		return results;
	}
	
	/**
	 * 手动修改学员获得时数
	 * @param prof
	 * @param cpdLrnAwardRecord
	 */
	public void updateCpdLrnAwardRecord(loginProfile prof,CpdLrnAwardRecord cpdLrnAwardRecord){
		for(int i = 0;i< cpdLrnAwardRecord.getCpdLrnAwardRecordList().size();i++){
			CpdLrnAwardRecord clar = new CpdLrnAwardRecord();
			clar = cpdLrnAwardRecord.getCpdLrnAwardRecordList().get(i);
			clar.setClar_manul_ind(1);
			clar.setClar_update_datetime(super.getDate());
			clar.setClar_update_usr_ent_id(prof.usr_ent_id);
			saveOrUpdate(prof,cpdLrnAwardRecord,clar);
			//cpdLrnAwardRecordMapper.updateManulInd(cpdLrnAwardRecord);
			//cpdLrnAwardRecordMapper.updateById(clar);
		}
		//更新当前学员所有牌照获得时数记录为 “已手动修改过”
		cpdLrnAwardRecord.setClar_manul_ind(1);
		cpdLrnAwardRecord.setClar_update_datetime(super.getDate());
		cpdLrnAwardRecord.setClar_update_usr_ent_id(prof.usr_ent_id);
		cpdLrnAwardRecordMapper.updateById(cpdLrnAwardRecord);
	}
	
	public List<IndividualReportVo> getRecordForIndividualReport(Long usr_ent_id , Long ct_id,int sort ,Integer period,Long cr_id ){
        Map<String ,Object> map = new HashMap<String, Object>();
        map.put("usr_ent_id", usr_ent_id);
        map.put("ct_id", ct_id);
        map.put("period", period);
        map.put("cr_id", cr_id);
        if(0==sort){
        	map.put("orderTitle", 1);
        }else{
        	map.put("orderAwardTime", 1);
        }
        
        return cpdLrnAwardRecordMapper.getRecordForIndividualReport(map);

	}
	
	/**
	 * 新增/修改  手动操作学员cpd时数
	 * @param prof
	 * @param pclar
	 * @param clar
	 */
	public void saveOrUpdate(loginProfile prof,CpdLrnAwardRecord pclar,CpdLrnAwardRecord clar){
		if(null != clar.getClar_id() && clar.getClar_id() != 0 ){
			//id存在 执行update
			cpdLrnAwardRecordMapper.updateById(clar);
		}else{
			//执行新增
			clar.setClar_create_datetime(super.getDate());
			clar.setClar_create_usr_ent_id(prof.usr_ent_id);
			clar.setClar_usr_ent_id(pclar.getClar_usr_ent_id());
			clar.setClar_itm_id(pclar.getClar_itm_id());
			clar.setClar_app_id(pclar.getClar_app_id());
			clar.setClar_manul_ind(1);
			//clar.setClar_ct_id(pclar.getClar_ct_id());
			//clar.setClar_cg_id(pclar.getClar_cg_id());
			//clar.setClar_acgi_id(pclar.getClar_acgi_id());
			clar.setClar_award_datetime(pclar.getClar_award_datetime());
			if(clar.getClar_award_core_hours() == null){
				clar.setClar_award_core_hours((float) 0);
			}
            if(clar.getClar_award_non_core_hours() == null){
            	clar.setClar_award_non_core_hours((float) 0);
			}
			super.add(clar);
		}
	}
	
	/**
	 * 导出报表
	 * @param prof
	 * @param itm_id
	 * @param search_txt
	 * @param cur_lan
	 * @param wizbini
	 * @return
	 * @throws SQLException
	 * @throws qdbException
	 * @throws cwSysMessage
	 */
	public String export(loginProfile prof,long itm_id,String search_txt,String cur_lan, WizbiniLoader wizbini) throws SQLException, qdbException, cwSysMessage{
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
		
        //获取列头部
        List<String>  labelsList = getTableColumn(itm_id);
        labelsList.add(0, LabelContent.get(cur_lan, "label_core_cpt_d_management_58"));  //用户名;
        labelsList.add(1, LabelContent.get(cur_lan, "label_core_cpt_d_management_59"));  //全名
        labelsList.add(2, LabelContent.get(cur_lan, "label_core_cpt_d_management_148")); //用户组
        //获取主体内容
        List<CpdLrnAwardRecordVO> listAllLrnAward =  getAllLrnAward(itm_id,search_txt);
        
        int rowIndex = 0;
        //获取列表头部
        getCell(sheet,rowIndex,wb,labelsList);
        
        //写入主体数据
        
        HSSFRow row = sheet.createRow(++rowIndex);  
        //写入主体内容
        writeContent(rowIndex,listAllLrnAward,sheet);
        
		//生成文件名   并 将文件存到指定位置  
        String fileName = "cpd_hours_award_"+new SimpleDateFormat("yyyyMMddHHmmssSSS") .format(new Date())+".xls";
         
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
	 * 获取课程显示的小牌列（课程必须设置牌照所需时数大于0）
	 * @param itm_id
	 * @return
	 * @throws SQLException
	 * @throws qdbException
	 * @throws cwSysMessage
	 */
	public List<String> getTableColumn(long itm_id) throws SQLException, qdbException, cwSysMessage{
		   //获取到当前所有生效小牌牌照
			List<CpdGroup> list = cpdGroupService.getAllOrder(0);
			Map awardCPDGourpItemMap = aeItemCPDItemService.getCPDGourpItemMap(itm_id);
			List<String> tableColumn = null;
			if(null!=list && list.size()>0){
				tableColumn = new ArrayList<String>();
				for(CpdGroup cg : list){
					//如果该课程注册了小牌且需要时数不为0才显示
					if(awardCPDGourpItemMap.containsKey(cg.getCg_id())){
						AeItemCPDGourpItem acgi =  (AeItemCPDGourpItem)awardCPDGourpItemMap.get(cg.getCg_id());
						if(null!=acgi.getAcgi_award_core_hours() && acgi.getAcgi_award_core_hours()>0f){
							String coreColumn = cg.getCg_alias() + " Core Hours("+ acgi.getAcgi_award_core_hours() +")";
							tableColumn.add(coreColumn);
						}
						if(cg.getCg_contain_non_core_ind()==1){
							if(null!=acgi.getAcgi_award_non_core_hours() && acgi.getAcgi_award_non_core_hours()>0f){
								String nonCoreColumn = cg.getCg_alias() + " Non-core Hours("+ acgi.getAcgi_award_non_core_hours() +")";
								tableColumn.add(nonCoreColumn);
							}
						}
					}

				}
			}
		return 	tableColumn;	
	}
	
	/**
	 * 获取到课程所有学员获得cpt/d时数 （不分页）
	 * @param itm_id
	 * @param searchTxt
	 * @return
	 * @throws cwSysMessage 
	 * @throws qdbException 
	 * @throws SQLException 
	 */
	private List<CpdLrnAwardRecordVO> getAllLrnAward(long itm_id,String searchTxt) throws SQLException, qdbException, cwSysMessage{
		//拿出该课程的报名记录
			List<CpdGroup> list = cpdGroupService.getAllOrder(0);
			Map awardCPDGourpItemMap = aeItemCPDItemService.getCPDGourpItemMap(itm_id);//返回课程小牌MAP
			//获得课程里所有用户的获得CPD记录
			Map<String, Object> map =new HashMap<String, Object>();
			map.put("itm_id", itm_id);
			map.put("searchTxt", searchTxt);
			map.put("status", CourseEvaluation.Completed);
			List<CpdLrnAwardRecord> recordlist = cpdLrnAwardRecordMapper.searchAllLrnAward(map);
			List<CpdLrnAwardRecordVO> returnList = null;
			if(null!=recordlist && recordlist.size()>0){
				returnList = new ArrayList<CpdLrnAwardRecordVO>();
				for(CpdLrnAwardRecord record : recordlist){//用户记录
					CpdLrnAwardRecordVO vo = CpdLrnAwardRecordVO.entity2Vo(record);
					Map<String,Float> hoursMap = new HashMap<String,Float>();
					int hours_index = 0;
					for(CpdGroup cg : list){
						//如果该课程注册了小牌且需要时数不为0才显示
						if(awardCPDGourpItemMap.containsKey(cg.getCg_id())){
							AeItemCPDGourpItem acgi =  (AeItemCPDGourpItem)awardCPDGourpItemMap.get(cg.getCg_id());
							if(null!=acgi.getAcgi_award_core_hours()){
								String columnKey = "display_hours_";
								String coreColumnKey = columnKey+hours_index;
								String nonCoreColumnKey = null;
								Float coreHours = 0f;
								Float nonCoreHours = 0f;
								//获得当前循环用户的CPD记录
								List<CpdLrnAwardRecord> userAwardList = record.getCpdLrnAwardRecordList();
								for(CpdLrnAwardRecord ur : userAwardList){
									//如果该记录对应课程的CPD牌照ID
									if(ur.getClar_cg_id() .equals( acgi.getAcgi_cg_id())){
										coreHours = ur.getClar_award_core_hours();
										if(cg.getCg_contain_non_core_ind()==1 && acgi.getAcgi_award_non_core_hours()>0){
											if(null!=ur.getClar_award_non_core_hours()){
												nonCoreHours =ur.getClar_award_non_core_hours();
											}
										}
									}
								}
								
								//当核心时数大于0才显示
								if(acgi.getAcgi_award_core_hours() > 0){
									hoursMap.put(coreColumnKey, coreHours);
								}else{
									hours_index--;
								}
								
								if(cg.getCg_contain_non_core_ind()==1 && acgi.getAcgi_award_non_core_hours()>0){
									hours_index++;
									nonCoreColumnKey=columnKey+hours_index;
									hoursMap.put(nonCoreColumnKey, nonCoreHours);
								}
								hours_index++;
							}

						}
					}
					vo.setHoursMap(hoursMap);
					returnList.add(vo);
				}
			}
			return returnList;
	}
	
	/**
	 *  获取导出报表的列表头部内容 
	 * @param sheet
	 * @param rowIndex
	 * @param wb
	 * @param labels
	 */
	private void getCell(HSSFSheet sheet,int rowIndex,HSSFWorkbook wb ,List<String> labels){
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
	        
	        
   		 for(int i = 0; i<labels.size(); i++){
   		     sheet.setColumnWidth(3+i, 5000);  //设置牌照列宽度
   			 HSSFCell cell = row.createCell(i);  
		         cell.setCellValue(labels.get(i));  
		         cell.setCellStyle(style);  
   	    }
	}

	/**
	 * 写入主体内容
	 * @param rowIndex
	 * @param cpdAwardlist
	 * @param sheet
	 */
	private void writeContent(int rowIndex,List<CpdLrnAwardRecordVO>  cpdAwardlist,HSSFSheet sheet){
		if(null != cpdAwardlist &&cpdAwardlist.size() > 0){
			for(CpdLrnAwardRecordVO cv : cpdAwardlist){
				HSSFRow row = sheet.createRow(rowIndex++);  
		        //创建单元格，并设置值  
				createAndSetCell(row,cv);
			}
		}
	}
    
	/**
	 * 创建单元格 写入数据  
	 * @param row
	 * @param cr
	 */
	private void createAndSetCell(HSSFRow row,CpdLrnAwardRecordVO cv){
			int index = 0;
			if(cv.getClar_manul_ind() == 1){
				row.createCell(index++).setCellValue("* "+cv.getUsr_ste_usr_id());  
        	}else{
        		row.createCell(index++).setCellValue(cv.getUsr_ste_usr_id());
        	}
			row.createCell(index++).setCellValue(cv.getUsr_display_bil()); 
	        row.createCell(index++).setCellValue(cv.getUsr_group());
	        for(int i =0;i<cv.getHoursMap().size();i++){
	        	String key = "display_hours_"+i;
	        	row.createCell(index++).setCellValue(cv.getHoursMap().get(key) == null ? "0":cv.getHoursMap().get(key).toString());
	        }
		}
}
