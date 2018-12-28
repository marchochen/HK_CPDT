package com.cw.wizbank.tpplan;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Vector;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.db.DbTrainingCenter;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.tpplan.db.dbTpTrainingPlan;
import com.cw.wizbank.upload.UploadUtils;
import com.cw.wizbank.util.LangLabel;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;

/**
 * @author jackyx
 * @date 2007-10-23
 */
public class tpPlanUpLoad {
	
	public final static String DATA_SHEET_NAME = "Sheet1";
    public final static String FIX_PATTERN_SIGHT = "制式化培训";


	
	private final static int EMPTY_COL = 1;
	private final static int TOO_LONG = 2;
	private final static int FORMAT_ERR = 3;
	private final static int INVALID_CNT = 4;
	private final static int NOT_FOUND = 5;

	private Vector invalidOther = null;
	public Vector invalidMsgVec = null;

	private final static String EMPTY_COLUMN_ERROR = "EMPTY_COLUMN_ERROR";
	private final static String FORMAT_ERROR = "FORMAT_ERROR";
	private final static String TOO_LONG_ERROR  = "TOO_LONG_ERROR";
	
	private boolean invalidXlsFile = false;

	private Connection con = null;
	private Vector inColHeader = null;
	public long tcr_id;
	public long year;
	public boolean passed;

    private String[] StdColName;

    public class YearPlan {
    	public long tpn_id;
    	public long tpn_year;
    	public long tpn_tcr_id;
    	public Timestamp tpn_date;
    	public String tpn_name;
    	public String tpn_cos_type;
    	public String tpn_tnd_title;
    	public String tpn_introduction;
    	public String tpn_target;
    	public String tpn_responser;
    	public String tpn_duration;
      	public int tpn_grade;
    	public long tpn_tnd_id;
    	public String tpn_content;
    	public String tpn_aim;
    	public long tpn_lrn_count;
    	public Timestamp tpn_wb_start_date;
    	public Timestamp tpn_wb_end_date;
    	public Timestamp tpn_ftf_start_date;
    	public Timestamp tpn_ftf_end_date;
    	public String tpn_type;
    	public float tpn_fee;
    	public String tpn_remark;
    	public String tpn_status;
    	public String tpn_approve_usr_id;
    	public String tpn_upd_usr_id;
    	
    	public boolean ypn_OK;
        String invalidFieldName = null;
    }
    public class ErrorMsg {
    	public int col;
    	public int row;
    	public String invalidFieldName;
    	public String error_type;
    }
    public tpPlanUpLoad(Connection con,  String lang, long tcr_id, long year) {
    	this.con = con;
    	this.inColHeader = new Vector();
    	this.tcr_id = tcr_id;
    	this.year = year;
    	passed = false;
    	invalidOther = new Vector();
    	invalidMsgVec = new Vector();
    	StdColName = new String[] { LangLabel.getValue(lang, "wb_imp_tp_plan_date"),
				LangLabel.getValue(lang, "wb_imp_tp_plan_name"), LangLabel.getValue(lang, "wb_imp_tp_plan_type"),
				LangLabel.getValue(lang, "wb_imp_tp_plan_tnd"), LangLabel.getValue(lang, "wb_imp_tp_plan_intr"),
				LangLabel.getValue(lang, "wb_imp_tp_plan_aim"), LangLabel.getValue(lang, "wb_imp_tp_plan_target"),
				LangLabel.getValue(lang, "wb_imp_tp_plan_responser"), LangLabel.getValue(lang, "wb_imp_tp_plan_duration"),
				LangLabel.getValue(lang, "wb_imp_tp_plan_ftf_start_time"), LangLabel.getValue(lang, "wb_imp_tp_plan_ftf_end_time"),
				LangLabel.getValue(lang, "wb_imp_tp_plan_wb_start_time"), LangLabel.getValue(lang, "wb_imp_tp_plan_wb_end_time"),
				LangLabel.getValue(lang, "wb_imp_tp_plan_lrn_count"), LangLabel.getValue(lang, "wb_imp_tp_plan_fee"),
				LangLabel.getValue(lang, "wb_imp_tp_plan_remark") };
    	return;
    }
    
    private String[] readLineXls(Sheet sheet, int rowNum, int columnNum, boolean emptyToNull) {
    	Vector tempColumn = new Vector();
    	Cell cell = null;
    	String content = null;
    	for (int pos = 0;pos < columnNum; pos++) {
    		cell = sheet.getCell(pos, rowNum);
    		content = cell.getContents();
    		if (content != null && content.length() > 0) {
    			tempColumn.addElement(content);
    		} else if (emptyToNull) {
    			tempColumn.addElement("");
    		}
    	}
    	return cwUtils.vec2strArray(tempColumn);
    }
    
	    
    public String uploadPlan(File srcFile, WizbiniLoader wizbini, boolean decompose, loginProfile prof, long year, long tcr_id) throws cwException, IOException, cwSysMessage, SQLException, qdbException {
        Workbook workBook = null;
        Sheet sheet = null;
        Vector plan_vec = new Vector();
        boolean errorFlag = false;
        WorkbookSettings wbSetting = new WorkbookSettings();
        wbSetting.setIgnoreBlanks(true);
        try {
            workBook = Workbook.getWorkbook(srcFile, wbSetting);
            
            sheet = workBook.getSheet(0);
            if (sheet == null) {
                errorFlag = true;
                invalidXlsFile = true;
            } else {
                String[] head_column = readLineXls(sheet, 0, sheet.getColumns(), false);
                if (head_column == null || head_column.length == 0) {
                    throw new cwSysMessage(UploadUtils.SMSG_ULG_INVALID_FILE);
                }
                Vector invalidColHeader = parseInValidColHeader(head_column);            
                if (!invalidColHeader.isEmpty()) {
                    return UploadUtils.invalidColXML(invalidColHeader, UploadUtils.INVALID_COL_TYPE_UNRECOGNIZED);
                }
                Vector duplicateColHeader = parseDuplicateColHeader();            
                if( !duplicateColHeader.isEmpty() ){
                    return UploadUtils.invalidColXML(duplicateColHeader, UploadUtils.INVALID_COL_TYPE_DUPLICATE);
                }
                Vector missingColHeader = parseMissingColHeader();            
                if( !missingColHeader.isEmpty() ){
                    return UploadUtils.invalidColXML(missingColHeader, UploadUtils.INVALID_COL_TYPE_MISSING);
                }
                String[] row = null;
                int rowNum = 1;
                int rowLimit = 999999999;
                if(wizbini != null){
                   rowLimit = wizbini.cfgSysSetupadv.getResBatchUpload().getMaxUploadCount();
                }
                for (int i =0;i < sheet.getRows() - 1;i++) {
                    row = readLineXls(sheet, rowNum++, head_column.length, true);
                    if (UploadUtils.isRowEmpty(row)) {
                        continue;
                    }
                    //if number of records more than row limit,throw a message.
                    if (rowNum > rowLimit) {
                        throw new cwSysMessage(UploadUtils.SMSG_ULG_UPLOAD_COUNT_EXCEED_LIMIT, new Integer(rowLimit).toString());
                    }
                    YearPlan plan = new YearPlan();
                    ErrorMsg msg = new ErrorMsg();
                    switch (populate(plan, msg, rowNum, year, row) ) {
                        case 0:
                        	plan_vec.add(plan);
                            break;
                        case EMPTY_COL:
                        	errorFlag = true;
                        	invalidMsgVec.add(msg);
                        	break;
                        case FORMAT_ERR:
                        	errorFlag = true;
                        	invalidMsgVec.add(msg);
                        	break;
                        case TOO_LONG:
                        	errorFlag = true;
                        	invalidMsgVec.add(msg);
                        	break;
                        case INVALID_CNT:
                        	errorFlag = true;
                        	invalidMsgVec.add(msg);
                         	break;
                        case NOT_FOUND:
                        	errorFlag = true;
                        	invalidMsgVec.add(msg);
                         	break;
                        default:
                            errorFlag = true;
                            invalidOther.addElement(new Integer(rowNum));
                            break;
                    }
                }
            }
            if(workBook != null){
                workBook.close();
            }  
        } catch (BiffException e) {
        	throw new cwSysMessage("GEN009");
        }
        if( errorFlag ) {
            this.passed = false;
        	return invalidRowXML();
        } else {
            if(decompose){
                if(plan_vec != null && plan_vec.size() > 0){
                    tpPlanManagement plan_mgt = new tpPlanManagement();
                    for(int i = 0; i < plan_vec.size(); i++){
                        YearPlan plan = (YearPlan)plan_vec.get(i);
                        dbTpTrainingPlan tp_plan = planMap( plan);
                        plan_mgt.DecomposeExec( con,  prof, tp_plan,  tcr_id);
                    }
                }
                this.passed = true;
                return null;
            }else{
            	String xml = uploadedPlanXML(plan_vec);
            	this.passed = true;
                return xml;
            }
        }
        
    }
    private dbTpTrainingPlan planMap(YearPlan plan) throws SQLException{
        dbTpTrainingPlan tp_plan = new dbTpTrainingPlan();
        tp_plan.tpn_date = plan.tpn_date;
        //tp_plan.tpn_code = genPlanCodeSuffix( con, code_prefix);
        tp_plan.tpn_name = plan.tpn_name;
        tp_plan.tpn_aim = plan.tpn_aim;
        tp_plan.tpn_lrn_count = plan.tpn_lrn_count;
        tp_plan.tpn_tcr_id = plan.tpn_tcr_id;
        tp_plan.tpn_cos_type = plan.tpn_cos_type;
        tp_plan.tpn_tnd_title = plan.tpn_tnd_title;
        tp_plan.tpn_introduction = plan.tpn_introduction;
        tp_plan.tpn_target = plan.tpn_target;
        tp_plan.tpn_responser = plan.tpn_responser;
        tp_plan.tpn_duration = plan.tpn_duration;
        tp_plan.tpn_lrn_count = plan.tpn_lrn_count;
        tp_plan.tpn_ftf_start_date = plan.tpn_ftf_start_date;
        tp_plan.tpn_ftf_end_date = plan.tpn_ftf_end_date;
        tp_plan.tpn_wb_start_date = plan.tpn_wb_start_date;
        tp_plan.tpn_wb_end_date = plan.tpn_wb_end_date;
        tp_plan.tpn_type = plan.tpn_type;
        tp_plan.tpn_fee = plan.tpn_fee;
        tp_plan.tpn_remark = plan.tpn_remark;
        tp_plan.tpn_status = plan.tpn_status;
        return tp_plan;
    }
    



    private int populate(YearPlan plan, ErrorMsg msg,  int row_num, long year, String[] row) throws SQLException, cwException, qdbException {
    	int col = 0;
    	plan.ypn_OK = true;
    	msg.row = row_num;
        for(int i=0; i<row.length; i++){
            String colName = (String)inColHeader.elementAt(col);
            if(colName.equalsIgnoreCase(StdColName[0]) ) {//plan date
            	if(row[i] !=null && !row[i].trim().equalsIgnoreCase("")) {
            		if(UploadUtils.checkLength(row[i].trim(), 1, 2)) {
            			try {
							int month = Integer.parseInt(row[i].trim());
							if(month >12 || month < 1) {
								plan.ypn_OK = false;
	            				msg.col = i +1;
	            				msg.invalidFieldName = colName;
	            				msg.error_type = FORMAT_ERROR;
	            				return FORMAT_ERR;
							}
            				String date = year + "-" + row[i].trim() + "-01 00:00:00";
            				if( row[i].trim().length() ==1 ) {
            					date = year + "-0" + row[i].trim() + "-01 00:00:00";
            				}
            				plan.tpn_date = Timestamp.valueOf(date);
            				
            			} catch (Exception e) {
            				CommonLog.error(e.getMessage(),e);
            				plan.ypn_OK = false;
            				msg.col = i +1;
            				msg.invalidFieldName = colName;
            				msg.error_type = FORMAT_ERROR;
            				return FORMAT_ERR;
            			}
            		} else {
            			plan.ypn_OK = false;
        				msg.col = i +1;
        				msg.invalidFieldName = colName;
        				msg.error_type = TOO_LONG_ERROR;
            			return TOO_LONG;
            		}
            	} else {
            		plan.ypn_OK = false;
    				msg.col = i +1;
    				msg.invalidFieldName = colName;
    				msg.error_type = EMPTY_COLUMN_ERROR;
            		return EMPTY_COL;
            	}
            } 
            else if ( colName.equalsIgnoreCase(StdColName[1])) {//plan name
            	if(row[i] != null && !row[i].trim().equalsIgnoreCase("")) {
            		if( UploadUtils.checkLength(row[i].trim(), 1, 200)) {
            			plan.tpn_name = row[i];
            		} else {
              			plan.ypn_OK = false;
        				msg.col = i +1;
        				msg.invalidFieldName = colName;
        				msg.error_type = TOO_LONG_ERROR;
            			return TOO_LONG;
            		}
            	} else {
            		plan.ypn_OK = false;
    				msg.col = i +1;
    				msg.invalidFieldName = colName;
    				msg.error_type = EMPTY_COLUMN_ERROR;
            		return EMPTY_COL;
            	}
            } else if(colName.equalsIgnoreCase(StdColName[2])) {//plan cos type
            	if(row[i] != null && !row[i].trim().equalsIgnoreCase("")) {
            		if( UploadUtils.checkLength(row[i].trim(), 1, 255)) {
            			plan.tpn_cos_type = row[i];
            		} else {
              			plan.ypn_OK = false;
        				msg.col = i +1;
        				msg.invalidFieldName = colName;
        				msg.error_type = TOO_LONG_ERROR;
            			return TOO_LONG;
            		}
            	} else {
            		plan.ypn_OK = false;
    				msg.col = i +1;
    				msg.invalidFieldName = colName;
    				msg.error_type = EMPTY_COLUMN_ERROR;
            		return EMPTY_COL;
            	}
            }
            else if(colName.equalsIgnoreCase(StdColName[3])) {// plan tnd title
            	if(row[i] != null && !row[i].trim().equalsIgnoreCase("")) {
            		if( UploadUtils.checkLength(row[i].trim(), 0, 100)) {
            			plan.tpn_tnd_title = row[i];
            		} else {
              			plan.ypn_OK = false;
        				msg.col = i +1;
        				msg.invalidFieldName = colName;
        				msg.error_type = TOO_LONG_ERROR;
            			return TOO_LONG;
            		}
            	} 
            }
            else if(colName.equalsIgnoreCase(StdColName[4])) {//plan introduction
            	if(row[i] != null && !row[i].trim().equalsIgnoreCase("")) {
            		if( UploadUtils.checkLength(row[i].trim(), 0, 2000)) {
            			plan.tpn_introduction = row[i];
            		} else {
              			plan.ypn_OK = false;
        				msg.col = i +1;
        				msg.invalidFieldName = colName;
        				msg.error_type = TOO_LONG_ERROR;
            			return TOO_LONG;
            		}
            	}
            }
            else if( colName.equalsIgnoreCase(StdColName[5]) ) { //plan aim
                if( UploadUtils.checkLength(row[i], 0, 4000)) {
                    plan.tpn_aim= row[i];
                } else {
                    plan.ypn_OK = false;
                    msg.col = i +1;
                    msg.invalidFieldName = colName;
                    msg.error_type = TOO_LONG_ERROR;
                    return TOO_LONG;
                }
            }
            else if( colName.equalsIgnoreCase(StdColName[6]) ) { // plan target
                if( UploadUtils.checkLength(row[i], 0, 4000)) {
                    plan.tpn_target= row[i];
                } else {
                    plan.ypn_OK = false;
                    msg.col = i +1;
                    msg.invalidFieldName = colName;
                    msg.error_type = TOO_LONG_ERROR;
                    return TOO_LONG;
                }
            }   
            else if( colName.equalsIgnoreCase(StdColName[7]) ) { // plan responser
             	if(row[i] != null && !row[i].trim().equalsIgnoreCase("")) {
            		if( UploadUtils.checkLength(row[i], 1, 255)) {
            			plan.tpn_responser= row[i];
            		} else {
            			plan.ypn_OK = false;
            			msg.col = i +1;
            			msg.invalidFieldName = colName;
            			msg.error_type = TOO_LONG_ERROR;
            			return TOO_LONG;
            		}
            	} else {
            		plan.ypn_OK = false;
    				msg.col = i +1;
    				msg.invalidFieldName = colName;
    				msg.error_type = EMPTY_COLUMN_ERROR;
            		return EMPTY_COL;
            	}
             
            }
            else if(colName.equalsIgnoreCase(StdColName[8])) {//plan duration
                if( UploadUtils.checkLength(row[i], 0, 4000)) {
                    plan.tpn_duration= row[i];
                } else {
                    plan.ypn_OK = false;
                    msg.col = i +1;
                    msg.invalidFieldName = colName;
                    msg.error_type = TOO_LONG_ERROR;
                    return TOO_LONG;
                }
            }   else if( colName.equalsIgnoreCase(StdColName[9]) ) { //plan face to face start time
            	if(UploadUtils.checkLength(row[i], 0, 10)) {
            		if (row[i] != null && row[i].trim().length() > 0) {
            			if(UploadUtils.isValidDate(row[i], UploadUtils.DF_YYYY_MM_DD)) {
            				plan.tpn_ftf_start_date = UploadUtils.toTimestamp(row[i], UploadUtils.DF_YYYY_MM_DD);
            			} else {
            				plan.ypn_OK = false;
            				msg.col = i +1;
            				msg.invalidFieldName = colName;
            				msg.error_type = FORMAT_ERROR;
            				return FORMAT_ERR;
            			}
            		}
            	} else {
            		plan.ypn_OK = false;
    				msg.col = i +1;
    				msg.invalidFieldName = colName;
    				msg.error_type = TOO_LONG_ERROR;
            		return TOO_LONG;
            	}
            }   else if( colName.equalsIgnoreCase(StdColName[10]) ) {//plan face to face end time
            	if(UploadUtils.checkLength(row[i], 0, 10)) {
            		if (row[i] != null && row[i].trim().length() > 0) {
            			if(UploadUtils.isValidDate(row[i], UploadUtils.DF_YYYY_MM_DD)) {
            				plan.tpn_ftf_end_date = UploadUtils.toTimestamp(row[i], UploadUtils.DF_YYYY_MM_DD);
            			} else {
            				plan.ypn_OK = false;
            				msg.col = i +1;
            				msg.invalidFieldName = colName;
            				msg.error_type = FORMAT_ERROR;
            				return FORMAT_ERR;
            			}
            		}
            	} else {
            		plan.ypn_OK = false;
    				msg.col = i +1;
    				msg.invalidFieldName = colName;
    				msg.error_type = TOO_LONG_ERROR;
            		return TOO_LONG;
            	}
            }   else if( colName.equalsIgnoreCase(StdColName[11]) ) { //plan web course start time
            	if(UploadUtils.checkLength(row[i], 0, 10)) {
            		if (row[i] != null && row[i].trim().length() > 0) {
            			if(UploadUtils.isValidDate(row[i], UploadUtils.DF_YYYY_MM_DD)) {
            				plan.tpn_wb_start_date = UploadUtils.toTimestamp(row[i], UploadUtils.DF_YYYY_MM_DD);
            			} else {
            				plan.ypn_OK = false;
            				msg.col = i +1;
            				msg.invalidFieldName = colName;
            				msg.error_type = FORMAT_ERROR;
            				return FORMAT_ERR;
            			}
            		}
            	} else {
            		plan.ypn_OK = false;
    				msg.col = i +1;
    				msg.invalidFieldName = colName;
    				msg.error_type = TOO_LONG_ERROR;
            		return TOO_LONG;
            	}
            }   else if( colName.equalsIgnoreCase(StdColName[12]) ) { //plan web course end time
            	if(UploadUtils.checkLength(row[i], 0, 10)) {
            		if (row[i] != null && row[i].trim().length() > 0) {
            			if(UploadUtils.isValidDate(row[i], UploadUtils.DF_YYYY_MM_DD)) {
            				plan.tpn_wb_end_date = UploadUtils.toTimestamp(row[i], UploadUtils.DF_YYYY_MM_DD);
            			} else {
            				plan.ypn_OK = false;
            				msg.col = i +1;
            				msg.invalidFieldName = colName;
            				msg.error_type = FORMAT_ERROR;
            				return FORMAT_ERR;
            			}
            		}
            	} 
            }  else if( colName.equalsIgnoreCase(StdColName[13]) ) { //plan learner count
                int lrn_count = 0;
                if(row[i] != null && !row[i].trim().equalsIgnoreCase("")){
                    try {
                        if(row[i] != null && row[i].length() != 0) {
                            lrn_count =  Integer.parseInt(row[i]);
                        }
                    } catch (NumberFormatException e) {
                        CommonLog.error(e.getMessage(),e);
                        plan.ypn_OK = false;
                        msg.col = i +1;
                        msg.invalidFieldName = colName;
                        msg.error_type = FORMAT_ERROR;
                        return FORMAT_ERR;
                    }
                }
                plan.tpn_lrn_count = lrn_count;
            }  else if(colName.equalsIgnoreCase(StdColName[14])) {//plan fee
        		float fee = 0;
                if(row[i] != null && !row[i].trim().equalsIgnoreCase("")){
            		try {
            			if(row[i] != null && row[i].trim().length() != 0) {
            				fee =  Float.parseFloat(row[i].trim());
            			}
            		} catch (NumberFormatException e) {
            			CommonLog.error(e.getMessage(),e);
        				plan.ypn_OK = false;
        				msg.col = i +1;
        				msg.invalidFieldName = colName;
        				msg.error_type = FORMAT_ERROR;
        				return FORMAT_ERR;
    				}
                }
        		plan.tpn_fee = fee;
            } else if (colName.equalsIgnoreCase(StdColName[15])) {//plan remarks
            	if(UploadUtils.checkLength(row[i], 0, 4000)) { 
            		plan.tpn_remark = row[i];
            	} else {
            		plan.ypn_OK = false;
    				msg.col = i +1;
    				msg.invalidFieldName = colName;
    				msg.error_type = TOO_LONG_ERROR;
            		return TOO_LONG;
            	}
            }  else {
                return -1;
            }
            col++;
        }
        plan.tpn_type = dbTpTrainingPlan.TPN_TYPE_YEAR;
        
        return 0;
    }

    private String invalidRowXML(){
        StringBuffer xml = new StringBuffer(512);
        xml.append("<invalid_list>");

        if( !invalidMsgVec.isEmpty() ){
            xml.append("<invalid_lines>");
            for(int i=0; i<invalidMsgVec.size(); i++) {
            	ErrorMsg msg = (ErrorMsg)invalidMsgVec.elementAt(i);
            	xml.append("<line col=\"").append(msg.col).append("\" row=\"").append(msg.row).append("\">");
            	xml.append("<invalid_field>").append(msg.invalidFieldName).append("</invalid_field>");
            	xml.append("<error_type>").append(msg.error_type).append("</error_type>");
            	xml.append("</line>");
            }
             xml.append("</invalid_lines>");
        }
        if( !invalidOther.isEmpty() ){
            xml.append("<invalid_other_lines>");
            for(int i=0; i<invalidOther.size(); i++)
                xml.append("<line num=\"").append(invalidOther.elementAt(i)).append("\"/>");
             xml.append("</invalid_other_lines>");
        }
        if (invalidXlsFile) {
            xml.append("<invalid_xls_sheet>");
            xml.append("<line num=\"0\"/>");
            xml.append("</invalid_xls_sheet>");
        }
        xml.append("</invalid_list>");
        return xml.toString();
    }
	        
    private String uploadedPlanXML(Vector plan_vec) throws cwSysMessage, SQLException{
        StringBuffer xml = new StringBuffer();
        xml.append("<plan_column>");
        for (int k = 0; k < this.inColHeader.size(); k++){
            String exclusion = (String)this.inColHeader.elementAt(k);
             xml.append("<column>").append(exclusion).append("</column>");
        }      
        xml.append("</plan_column>");
        xml.append(asXML(plan_vec));
        return xml.toString();
    }
	private String asXML(Vector plan_vec) throws SQLException {
    	StringBuffer xml = new StringBuffer();
    	xml.append("<plan_list tcr_id=\"").append(tcr_id)
    	   .append("\" year=\"").append(year)
    	   .append("\" type=\"").append(dbTpTrainingPlan.TPN_TYPE_YEAR)
    	   .append("\" >");
    	if (plan_vec != null && plan_vec.size() > 0) {
    		for(int i=0; i< plan_vec.size(); i++) {
    			YearPlan tpTp = (YearPlan)plan_vec.get(i);
    			xml.append("<plan id=\"").append(tpTp.tpn_id).append("\" type=\"").append(dbTpTrainingPlan.TPN_TYPE_YEAR).append("\">").append(cwUtils.NEWL);
    			xml.append("<plan_date>").append(tpTp.tpn_date).append("</plan_date>");
    			xml.append("<name>").append(cwUtils.esc4XML(cwUtils.escNull(tpTp.tpn_name))).append("</name>");
    			xml.append("<cos_type>").append(cwUtils.esc4XML(cwUtils.escNull(tpTp.tpn_cos_type))).append("</cos_type>");
    			xml.append("<tnd_title>").append(cwUtils.esc4XML(cwUtils.escNull(tpTp.tpn_tnd_title))).append("</tnd_title>");
    			xml.append("<aim>").append(cwUtils.esc4XML(cwUtils.escNull(tpTp.tpn_aim))).append("</aim>");
    			xml.append("<duration>").append(tpTp.tpn_duration).append("</duration>");
    			xml.append("<lrn_count>").append(tpTp.tpn_lrn_count).append("</lrn_count>");
    			xml.append("<wb_start_date>").append(cwUtils.escNull(tpTp.tpn_wb_start_date)).append("</wb_start_date>");
    			xml.append("<wb_end_date>").append(cwUtils.escNull(tpTp.tpn_wb_end_date)).append("</wb_end_date>");
    			xml.append("<ftf_start_date>").append(cwUtils.escNull(tpTp.tpn_ftf_start_date)).append("</ftf_start_date>");
    			xml.append("<ftf_end_date>").append(cwUtils.escNull(tpTp.tpn_ftf_end_date)).append("</ftf_end_date>");
    			xml.append("<responser>").append(cwUtils.esc4XML(cwUtils.escNull(tpTp.tpn_responser))).append("</responser>");
    			xml.append("<introduction>").append(cwUtils.esc4XML(cwUtils.escNull(tpTp.tpn_introduction))).append("</introduction>");
    			xml.append("<target>").append(cwUtils.esc4XML(cwUtils.escNull(tpTp.tpn_target))).append("</target>");
    			xml.append("<type>").append(cwUtils.escNull(tpTp.tpn_type)).append("</type>");
    			xml.append("<fee>").append(tpTp.tpn_fee).append("</fee>");
    			xml.append("<remarks>").append(cwUtils.esc4XML(cwUtils.escNull(tpTp.tpn_remark))).append("</remarks>");
    			xml.append("<training_center id=\"").append(tpTp.tpn_tcr_id).append("\">");
    			xml.append("<title>").append(cwUtils.esc4XML(cwUtils.escNull(DbTrainingCenter.getTcrTitle(con, tpTp.tpn_tcr_id)))).append("</title>").append("</training_center>");
    			xml.append("<status>").append(tpTp.tpn_status).append("</status>");
    			xml.append("</plan>");
    		}
    	}
    	xml.append("</plan_list>");
    	return xml.toString();
    }
	    
    private Vector parseMissingColHeader() {
        Vector missColVec = new Vector();
        for(int i=0; i<StdColName.length; i++){
            if( this.inColHeader.indexOf(StdColName[i]) == -1 ) {
                missColVec.addElement(StdColName[i]);
            }
        }
        return missColVec;
    }

	private Vector parseDuplicateColHeader(){
	    Vector dupColVec = new Vector();
	    for(int i=0; i<this.inColHeader.size(); i++){
	        if (this.inColHeader.indexOf(this.inColHeader.elementAt(i), i+1) != -1 ){
	            if(dupColVec.indexOf(this.inColHeader.elementAt(i)) == -1 ) { 
	                dupColVec.addElement(this.inColHeader.elementAt(i));
	            }
	        }
	    }
	    return dupColVec; 
	}
	    
    private Vector parseInValidColHeader(String[] head_column) throws cwException, IOException {
    	Vector invalidColVec = new Vector();
    	nextCol:
	    for(int i=0; i<head_column.length; i++){
	        for(int j=0; j<StdColName.length; j++) {
	        	if(head_column[i].trim().equalsIgnoreCase(StdColName[j].trim()) ){
	        		this.inColHeader.addElement(head_column[i].trim());
	        		continue nextCol;
	        	}
	        }
	        invalidColVec.addElement(head_column[i].trim());
	    }
	    return invalidColVec;               
    }
}
