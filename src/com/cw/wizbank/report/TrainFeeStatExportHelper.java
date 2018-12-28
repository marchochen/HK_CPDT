package com.cw.wizbank.report;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.cw.wizbank.ae.aeItemDummyType;
import com.cw.wizbank.ae.aeUtils;
import com.cw.wizbank.db.DbTrainingCenter;
import com.cw.wizbank.report.TrainFeeStatReport.CostData;
import com.cw.wizbank.report.TrainFeeStatReport.ItmData;
import com.cw.wizbank.util.LangLabel;
import com.cw.wizbank.util.cwSysMessage;

import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class TrainFeeStatExportHelper  extends ExportHelper {
	private static final String EMPTY = "";
	private static final String SEPCHARACTER = "/";
	
    public TrainFeeStatExportHelper(String tempDir, String tempDirName, String winName, String encoding, int processUnit) throws WriteException, IOException {
        super(tempDir, tempDirName, winName, encoding, processUnit);
    } 
    
    public void writeCondition(Connection con, ExamPaperStatExporter.SpecData specData) throws IOException, WriteException, SQLException, cwSysMessage {
        String reportTitle = null;
        if (specData.rsp_title != null) {
            reportTitle = specData.rsp_title;
        }
        else {
            reportTitle = getRptTitle(specData.cur_lang, Report.TRAIN_FEE_STAT);
        }
        setTitleText(reportTitle);
 
        if (specData.tcr_id > 0) {
        	getNewRow();
        	setCellContent(LangLabel.getValue(specData.cur_lang, "lab_tc"), ColumnA);
            setCellContent(DbTrainingCenter.getTcrTitle(con, specData.tcr_id), ColumnB);
        }
        
        if (specData.start_datetime != null || specData.end_datetime != null) {
            getNewRow();
            StringBuffer timeStr = new StringBuffer();
            timeStr.append(LangLabel.getValue(specData.cur_lang, "lab_from")).append(" ");
            if (specData.start_datetime != null) {
                timeStr.append(aeUtils.escNull(simple.format(specData.start_datetime)));
            }
            else {
                timeStr.append(LangLabel.getValue(specData.cur_lang, "lab_na"));
            }
            timeStr.append(" ").append(LangLabel.getValue(specData.cur_lang, "lab_to")).append(" ");
            if (specData.end_datetime != null) {
                timeStr.append(aeUtils.escNull(simple.format(specData.end_datetime)));
            }
            else {
                timeStr.append(LangLabel.getValue(specData.cur_lang, "lab_na"));
            }

            setCellContent(LangLabel.getValue(specData.cur_lang, "711"), ColumnA);
            setCellContent(timeStr.toString(), ColumnB);
        }
        
        if (specData.dummy_type != null && specData.dummy_type.length > 0) {
        	getNewRow();
        	StringBuffer itmType = new StringBuffer();
        	for (int i = 0; i < specData.dummy_type.length; i++) {
        		itmType.append(aeItemDummyType.getItemLabelByDummyType(specData.cur_lang, specData.dummy_type[i]));
	            if (i != specData.dummy_type.length - 1) {
	            	itmType.append(", ");
	            }
        	}
        	setCellContent(LangLabel.getValue(specData.cur_lang, "wb_imp_tp_plan_type"), ColumnA);
        	setCellContent(itmType.toString(), ColumnB);
        }
        
        if (specData.train_scope != null && specData.train_scope.length() > 0) {
        	getNewRow();
        	String scope = null;
        	if (specData.train_scope.equals(TrainFeeStatReport.TRAIN_SCOPE_PLAN)) {
        		scope = LangLabel.getValue(specData.cur_lang, "714");
        	} else if(specData.train_scope.equals(TrainFeeStatReport.TRAIN_SCOPE_UNPLAN)) {
        		scope = LangLabel.getValue(specData.cur_lang, "715");
        	} else {
        		scope = LangLabel.getValue(specData.cur_lang, "64");
        	}
        	setCellContent(LangLabel.getValue(specData.cur_lang, "713"), ColumnA);
        	setCellContent(scope, ColumnB);
        }
        getNewRow();
    }
    
    public void writeSummaryData(Map summaryHash, Map totalHash, Map costXmlHash, String cur_lang) throws WriteException {
    	List typeOrder = (List)costXmlHash.get("typeOrder");
    	
    	setBlankRow(1);
    	setCellContent(LangLabel.getValue(cur_lang,"716"), ColumnA, getStyleByName(STYLE_TITLE_TEXT));
    	getNewRow();
    	short index = 0;
    	setCellContent(EMPTY, index++, getStyleByName(STYLE_BOLD_FONT));
    	setCellContent(LangLabel.getValue(cur_lang,"718"), index++, getStyleByName(STYLE_BOLD_FONT));
    	setCellContent(LangLabel.getValue(cur_lang,"719"), index++, getStyleByName(STYLE_BOLD_FONT));
    	String exceRate = LangLabel.getValue(cur_lang,"720") + "(=" + LangLabel.getValue(cur_lang,"719") + SEPCHARACTER 
    					+ LangLabel.getValue(cur_lang,"718") + ")";
    	setCellContent(exceRate, index++, getStyleByName(STYLE_BOLD_FONT));
    	
    	String type = null;
    	CostData summaryCos = null;
    	for (int i = 0; i < typeOrder.size(); i++) {
    		getNewRow();
    		index = 0;
    		type = (String)typeOrder.get(i);
    		setCellContent((String)costXmlHash.get(type), index++, getStyleByName(STYLE_BOLD_FONT));
    		if (summaryHash.containsKey(type)) {
	    		summaryCos = (CostData)summaryHash.get(type);
	    		setValue(summaryCos.ito_budget, index++);
	    		setValue(summaryCos.ito_actual, index++);
	        	setRateValue(summaryCos.ito_actual, summaryCos.ito_budget, index++);
    		} else {
    			setCellContent(EMPTY, index++);
    			setCellContent(EMPTY, index++);
    			setCellContent(EMPTY, index++);
    		}
    	}
    	getNewRow();
    	double totalBudget = ((Double)totalHash.get("totalBudget")).doubleValue();
		double totalActual = ((Double)totalHash.get("totalActual")).doubleValue();
		index = 0;
		setCellContent(LangLabel.getValue(cur_lang,"728"), index++, getStyleByName(STYLE_BOLD_FONT));
		setCellContent(TrainFeeStatReport.df.format(totalBudget), index++);
		setCellContent(TrainFeeStatReport.df.format(totalActual), index++);
		if (totalBudget == 0 || totalActual == 0) {
			setCellContent("0%", index++);
		} else {
			setRateValue(totalActual, totalBudget, index++);
		}
    }
    
    public void writeTableHead(String cur_lang, Map costXmlHash) throws WriteException  {
    	List typeOrder = (List)costXmlHash.get("typeOrder");
    	setBlankRow(1);
    	setCellContent(LangLabel.getValue(cur_lang,"717"), ColumnA, getStyleByName(STYLE_TITLE_TEXT));
    	getNewRow();
    	setCellContent(LangLabel.getValue(cur_lang, "718") + SEPCHARACTER + LangLabel.getValue(cur_lang, "719"), (short)8, getStyleByName(STYLE_BOLD_FONT));
    	sheet.mergeCells(8, nowRowNum, 8 + typeOrder.size() - 1, nowRowNum);
    	
    	short index = 0;
    	getNewRow();
    	setCellContent(LangLabel.getValue(cur_lang, "804"), index++, getStyleByName(STYLE_BOLD_FONT));
    	setCellContent(LangLabel.getValue(cur_lang, "562"), index++, getStyleByName(STYLE_BOLD_FONT));
    	setCellContent(LangLabel.getValue(cur_lang, "lab_c_code"), index++, getStyleByName(STYLE_BOLD_FONT));
    	setCellContent(LangLabel.getValue(cur_lang, "lab_c_title"), index++, getStyleByName(STYLE_BOLD_FONT));
    	setCellContent(LangLabel.getValue(cur_lang, "lab_type"), index++, getStyleByName(STYLE_BOLD_FONT));
    	setCellContent(LangLabel.getValue(cur_lang, "718"), index++, getStyleByName(STYLE_BOLD_FONT));
    	setCellContent(LangLabel.getValue(cur_lang, "719"), index++, getStyleByName(STYLE_BOLD_FONT));
    	
    	String exceRate = LangLabel.getValue(cur_lang,"720") + "(=" + LangLabel.getValue(cur_lang,"719") + SEPCHARACTER 
						+ LangLabel.getValue(cur_lang,"718") + ")";
    	setCellContent(exceRate, index++, getStyleByName(STYLE_BOLD_FONT));
    	
    	String type = null;
    	for (int i = 0; i < typeOrder.size(); i++) {
    		type = (String)typeOrder.get(i);
    		setCellContent((String)costXmlHash.get(type), index++, getStyleByName(STYLE_BOLD_FONT));
    	}
    }
    
    public void writeData(String cur_lang, ItmData itmData, List typeOrder) throws RowsExceededException, WriteException {
    	short index = 0;
    	getNewRow();
    	setCellContent(itmData.p_itm_code, index++);
    	setCellContent(itmData.p_itm_title, index++);
    	setValue(itmData.c_itm_code, index++);
    	setValue(itmData.c_itm_title, index++);
    	setCellContent(aeItemDummyType.getItemLabelByDummyType(cur_lang, itmData.itm_dummy_type), index++);
    	setValue(itmData.total_budget, index++);
    	setValue(itmData.total_actual, index++);
    	setRateValue(itmData.total_actual, itmData.total_budget, index++);
    	
    	CostData costData = null;
    	String type = null;
    	
    	int i, j;
    	int s = itmData.cosLst.size();
    	for (i = 0; i < typeOrder.size(); i++) {
    		type = (String)typeOrder.get(i);
    		for (j = 0; j < s; j++) {
    			costData = (CostData)itmData.cosLst.get(j);
    			if (costData.ito_type.equals(type)) {
    				if (costData.ito_budget > 0 || costData.ito_actual > 0) {
    					setCellContent(TrainFeeStatReport.df.format(costData.ito_budget) + SEPCHARACTER + 
    									TrainFeeStatReport.df.format(costData.ito_actual), index++);
    				} else {
    					setCellContent(EMPTY, index++);
    				}
    				break;
    			}
    			if (j == s - 1) {
    				setCellContent(EMPTY, index++);
    			}
    		}
    	}
    }
    
    public void setRateValue(double actual, double budget, short cellNum) throws RowsExceededException, WriteException {
    	if (budget == 0) {
    		setCellContent(EMPTY, cellNum);
    	} else {
    		setCellContent(TrainFeeStatReport.nf.format(actual / budget), cellNum);
    	}
    }
    
    public void setValue(double value, short cellNum) throws RowsExceededException, WriteException {
    	if (value > 0) {
    		setCellContent(TrainFeeStatReport.df.format(value), cellNum);
    	} else {
    		setCellContent(EMPTY, cellNum);
    	}
    }
    
    public void setValue(String value, short cellNum) throws RowsExceededException, WriteException {
    	if (value != null) {
    		setCellContent(value, cellNum);
    	} else {
    		setCellContent(EMPTY, cellNum);
    	}
    }
}
