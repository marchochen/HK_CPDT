package com.cw.wizbank.report;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;

import com.cw.wizbank.JsonMod.credit.Credit;
import com.cw.wizbank.ae.aeUtils;
import com.cw.wizbank.config.organization.usermanagement.LabelType;
import com.cw.wizbank.config.organization.usermanagement.UserManagement;
import com.cw.wizbank.util.LangLabel;
import com.cw.wizbank.util.cwSysMessage;

import jxl.write.WriteException;

public class CreditReportHelper extends ExportHelper {
	private static final String SEPCHARACTER = ":"; 	
	
	public CreditReportHelper(String tempDir, String tempDirName, String winName, String encoding, int processUnit) throws WriteException,
			IOException {
		super(tempDir, tempDirName, winName, encoding, processUnit);
	}

	public void writeCondition(Connection con, ReportExporter.SpecData specData) throws IOException, WriteException, SQLException, cwSysMessage {
        String reportTitle = null;
        if (specData.isDetail) {
            reportTitle = LangLabel.getValue(specData.cur_lang, "835");
        }
        else {
            reportTitle = ExportHelper.getRptTitle(specData.cur_lang, Report.CREDIT);
        }
        setTitleText(reportTitle);
                    
        if (specData.ent_id_lst != null && specData.all_usg_ind == false) {
            getNewRow();
            setCellContent(LangLabel.getValue(specData.cur_lang, "lab_group"), ColumnA);
            String ent_name = CreditReportExporter.getEntName(con, specData.ent_id_lst);
            setCellContent(ent_name, ColumnB);
        }
        else {
            getNewRow();
            setCellContent(LangLabel.getValue(specData.cur_lang, "lab_group"), ColumnA);
            if(specData.all_usg_ind) {
            	setCellContent(LangLabel.getValue(specData.cur_lang, "834"), ColumnB);
            }
        }
        
        getNewRow();
        setCellContent(LangLabel.getValue(specData.cur_lang, "832"), ColumnA);        
        if (specData.include_del_usr){
        	setCellContent(LangLabel.getValue(specData.cur_lang, "lab_yes"), ColumnB);
        } else {
        	setCellContent(LangLabel.getValue(specData.cur_lang, "lab_no_1"), ColumnB);
        }
        
        getNewRow();
        setCellContent(LangLabel.getValue(specData.cur_lang, "833"), ColumnA);        
        if (specData.isDetail){
        	setCellContent(LangLabel.getValue(specData.cur_lang, "lab_yes"), ColumnB);
        } else {
        	setCellContent(LangLabel.getValue(specData.cur_lang, "lab_no_1"), ColumnB);
        }
        
        if (specData.att_create_start_datetime != null || specData.att_create_end_datetime != null) {
            getNewRow();
            StringBuffer timeStr = new StringBuffer();
            timeStr.append(LangLabel.getValue(specData.cur_lang, "lab_from")).append(" ");
            if (specData.att_create_start_datetime != null) {
                timeStr.append(aeUtils.escNull(specData.att_create_start_datetime));
            }
            else {
                timeStr.append(LangLabel.getValue(specData.cur_lang, "lab_na"));
            }
            timeStr.append(LangLabel.getValue(specData.cur_lang, "lab_to")).append(" ");
            if (specData.att_create_end_datetime != null) {
                timeStr.append(aeUtils.escNull(specData.att_create_end_datetime));
            }
            else {
                timeStr.append(LangLabel.getValue(specData.cur_lang, "lab_na"));
            }

            setCellContent(LangLabel.getValue(specData.cur_lang, "848"), ColumnA);
            setCellContent(timeStr.toString(), ColumnB);
        }
                
        getNewRow();
    }
	
	public void writeUserTableHead(String cur_lang, UserManagement um, boolean isDetail) throws WriteException, IOException {
        short index = 0;
        Iterator lt = null;
        LabelType labelType;
        getNewRow();
        lt = um.getUserProfile().getProfileAttributes().getUserId().getLabel().iterator();
        while (lt.hasNext()) {
            labelType = (LabelType)lt.next();
            if (labelType.getLang().equals(cur_lang)) {
                setCellContent(labelType.getValue(), index++, getStyleByName(STYLE_TITLE_WITH_FILLBACK));
            }
        }
        
        lt = um.getUserProfile().getProfileAttributes().getName().getLabel().iterator();
        while (lt.hasNext()) {
            labelType = (LabelType)lt.next();
            if (labelType.getLang().equals(cur_lang)) {
                setCellContent(labelType.getValue(), index++, getStyleByName(STYLE_TITLE_WITH_FILLBACK));
            }
        }


        lt = um.getUserProfile().getProfileAttributes().getGroup().getLabel().iterator();
        while (lt.hasNext()) {
            labelType = (LabelType)lt.next();
            if (labelType.getLang().equals(cur_lang)) {
                setCellContent(labelType.getValue(), index++, getStyleByName(STYLE_TITLE_WITH_FILLBACK));
            }
        }

        lt = um.getUserProfile().getProfileAttributes().getGrade().getLabel().iterator();
        while (lt.hasNext()) {
            labelType = (LabelType)lt.next();
            if (labelType.getLang().equals(cur_lang)) {
                setCellContent(labelType.getValue(), index++, getStyleByName(STYLE_TITLE_WITH_FILLBACK));
            }
        }
        
        if (isDetail){
        	setCellContent(LangLabel.getValue(cur_lang, "563"), index++, getStyleByName(STYLE_TITLE_WITH_FILLBACK));
            setCellContent(LangLabel.getValue(cur_lang, "820"), index++, getStyleByName(STYLE_TITLE_WITH_FILLBACK));
            setCellContent(LangLabel.getValue(cur_lang, "821"), index++, getStyleByName(STYLE_TITLE_WITH_FILLBACK));
            setCellContent(LangLabel.getValue(cur_lang, "822"), index++, getStyleByName(STYLE_TITLE_WITH_FILLBACK));          
            setCellContent(LangLabel.getValue(cur_lang, "823"), index++, getStyleByName(STYLE_TITLE_WITH_FILLBACK));
            setCellContent(LangLabel.getValue(cur_lang, "824"), index++, getStyleByName(STYLE_TITLE_WITH_FILLBACK));
            setCellContent(LangLabel.getValue(cur_lang, "825"), index++, getStyleByName(STYLE_TITLE_WITH_FILLBACK));
        }else{
            setCellContent(LangLabel.getValue(cur_lang, "829"), index++, getStyleByName(STYLE_TITLE_WITH_FILLBACK));
            setCellContent(LangLabel.getValue(cur_lang, "828"), index++, getStyleByName(STYLE_TITLE_WITH_FILLBACK));
            setCellContent(LangLabel.getValue(cur_lang, "836"), index++, getStyleByName(STYLE_TITLE_WITH_FILLBACK));
         }
    }    

    public void writegroupTableHead(String cur_lang, UserManagement um) throws WriteException, IOException {
        short index = 0;
        Iterator lt = null;
        LabelType labelType;
        getNewRow();
        lt = um.getUserProfile().getProfileAttributes().getGroup().getLabel().iterator();
        while (lt.hasNext()) {
            labelType = (LabelType)lt.next();
            if (labelType.getLang().equals(cur_lang)) {
                setCellContent(labelType.getValue(), index++, getStyleByName(STYLE_TITLE_WITH_FILLBACK));
            }
        }
        setCellContent(LangLabel.getValue(cur_lang, "lab_total_lrn"), index++, getStyleByName(STYLE_TITLE_WITH_FILLBACK));
        setCellContent(LangLabel.getValue(cur_lang, "829"), index++, getStyleByName(STYLE_TITLE_WITH_FILLBACK));
        setCellContent(LangLabel.getValue(cur_lang, "828"), index++, getStyleByName(STYLE_TITLE_WITH_FILLBACK));
        setCellContent(LangLabel.getValue(cur_lang, "836"), index++, getStyleByName(STYLE_TITLE_WITH_FILLBACK));        
    }
    
    //混合式组的统计
    public void writeStatisticDataHead(Credit.dataByGroup resData, String cur_lang)throws WriteException {
    	setBlankRow(1);
    	setCellContent(resData.group_name, ColumnA,getStyleByName(STYLE_TITLE_TEXT));
    	if(resData.totalRecode > 0){
        	getNewRow();
        	setCellContent(LangLabel.getValue(cur_lang, "lab_total_lrn") + SEPCHARACTER, ColumnA);
        	setCellContent(resData.usrcount, ColumnB, getStyleByName(STYLE_BLUE_FONT));
        	getNewRow();
        	setCellContent(LangLabel.getValue(cur_lang, "829") + SEPCHARACTER, ColumnA);
        	setCellContent(resData.trainscore, ColumnB, getStyleByName(STYLE_BLUE_FONT));
        	getNewRow();
        	setCellContent(LangLabel.getValue(cur_lang, "828") + SEPCHARACTER, ColumnA);
        	setCellContent(resData.activityscore, ColumnB, getStyleByName(STYLE_BLUE_FONT));
        	getNewRow();
        	setCellContent(LangLabel.getValue(cur_lang, "836")+SEPCHARACTER, ColumnA);
        	setCellContent(resData.totalscore, ColumnB, getStyleByName(STYLE_BLUE_FONT));    
    	} else {
    		getNewRow();
    		setCellContent(LangLabel.getValue(cur_lang, "lab_no_record"), ColumnA);  
    	}
    }
    
    public void writeData(Credit.resultData resData, String cur_lang) throws WriteException {
        short index = 0;
        if (resData.totalRecode > 0){
            getNewRow();
            setCellContent(resData.usr_ste_usr_id, index++);
            setCellContent(resData.usr_display_bil, index++);
            setCellContent(resData.group_name, index++);
            setCellContent(resData.grade_name, index++);
            setCellContent(resData.trainscore, index++);
            setCellContent(resData.activityscore, index++);
            setCellContent(resData.activityscore + resData.trainscore, index++); 
        }else{
    		getNewRow();
    		setCellContent(LangLabel.getValue(cur_lang, "lab_no_record"), ColumnA);     	
        }
    }
    
    public void writegroupData(com.cw.wizbank.JsonMod.credit.Credit.dataByGroup data, String cur_lang) throws WriteException {
        short index = 0;
        if (data.totalRecode > 0){
            getNewRow();
            setCellContent(data.group_name, index++);
            setCellContent(data.usrcount, index++);
            setCellContent(data.trainscore, index++);
            setCellContent(data.activityscore, index++);
            setCellContent(data.totalscore, index++);        	
        }else{
    		getNewRow();
    		setCellContent(LangLabel.getValue(cur_lang, "lab_no_record"), ColumnA);          	
        }
    }
    
    public void writeDtailData(Connection con, Credit.resultData resData, String cur_lang) throws WriteException, SQLException {
        short index = 0;
        getNewRow();
        setCellContent(resData.usr_ste_usr_id, index++);
        setCellContent(resData.usr_display_bil, index++);
        setCellContent(resData.group_name, index++);
        setCellContent(resData.grade_name, index++);
        //积分分数
        if (resData.jifen_type.equals(Credit.ACTIVITY_SCORE)){
        	setCellContent(resData.activityscore, index++);
        }else{      	
            setCellContent(resData.trainscore, index++);
        }
        //加分/扣分
        if (resData.act_ind > 0){
            setCellContent(LangLabel.getValue(cur_lang, "831"), index++);
        }else{
            setCellContent(LangLabel.getValue(cur_lang, "830"), index++);        	
        }
        //手动/自动
        if (resData.jifen_manual_ind){
        	setCellContent(LangLabel.getValue(cur_lang, "826"), index++);
        }else {
        	setCellContent(LangLabel.getValue(cur_lang, "827"), index++);
        }
        
        //积分类型
        String name = null;
        if (resData.jifen_type.equals(Credit.ACTIVITY_SCORE)){
            setCellContent(LangLabel.getValue(cur_lang, "828"), index++);
        }else{
            setCellContent(LangLabel.getValue(cur_lang, "829"), index++);        	
        }
        
        //积分名称
        if(resData.jifen_manual_ind && !resData.cty_code.equals(Credit.ITM_IMPORT_CREDIT) && !resData.cty_code.equals(Credit.SYS_ANWSER_BOUNTY) && !resData.cty_code.equals(Credit.INTEGRAL_EMPTY) && !resData.cty_code.equals(Credit.ITM_INTEGRAL_EMPTY) ) {
        	setCellContent(resData.cty_code, index++);
        } else {
        	setCellContent(LangLabel.getValue(cur_lang, "lab_cty_" + resData.cty_code), index++);
        }
        
    	//积分来源
        if(resData.jifen_source != null) {
        	setCellContent(resData.jifen_source, index++);
        } else {
        	setCellContent("--", index++);
        }
        //积分时间
        setCellContent(resData.ucl_create_timestamp, index++); 
    }
	
}
