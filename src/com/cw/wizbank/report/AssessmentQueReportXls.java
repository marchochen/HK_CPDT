package com.cw.wizbank.report;

/**
 * Created on 2006-4-3
 * @author joyce
 */
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.WritableCellFormat;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeItemRelation;
import com.cw.wizbank.qdb.dbEntity;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbResource;
import com.cw.wizbank.qdb.dbUserGroup;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.report.AssessmentQueReport.Attempt_times;
import com.cw.wizbank.util.LangLabel;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;
import com.cwn.wizbank.utils.FormatUtil;

public class AssessmentQueReportXls extends ReportXlsFileCreator {
    Connection con;
    String cur_lan;
    String encoding;
    String rsp_title;
    long itm_id;
    long mod_id;
    String[] ent_id_lst;
    boolean all_user_ind;
    boolean answer_for_lrn;
    boolean answer_for_course_lrn;
    Timestamp att_create_start_datetime;
    Timestamp att_create_end_datetime;
    Timestamp attempt_start_datetime;
    Timestamp attempt_end_datetime;
    Vector content_vec;
    String group_by;
    String[] attempt_type;
   

    public AssessmentQueReportXls(Connection incon,
                                  String rsp_title, long itm_id, long mod_id, String[] ent_id_lst,
                                  Timestamp att_create_start_datetime, Timestamp att_create_end_datetime,
                                  Timestamp attempt_start_datetime, Timestamp attempt_end_datetime,
                                  Vector content_vec, String group_by, String[] attempt_type,       
                                  String curLan, String xlsFilePath, String encoding,
                                  boolean all_user_ind, boolean answer_for_lrn, boolean answer_for_course_lrn) {
        con = incon;
        cur_lan = curLan;
        this.encoding = encoding;
        this.xlsFilePath = xlsFilePath;
        this.rsp_title = rsp_title;
        columnWidth = new int[0];
        
        this.itm_id = itm_id;
        this.mod_id = mod_id;
        this.ent_id_lst = ent_id_lst;
        this.att_create_start_datetime = att_create_start_datetime;
        this.att_create_end_datetime = att_create_end_datetime;
        this.attempt_start_datetime = attempt_start_datetime;
        this.attempt_end_datetime = attempt_end_datetime;
        this.content_vec = content_vec;
        this.group_by = group_by;
        this.attempt_type = attempt_type;
        this.all_user_ind = all_user_ind;
        this.answer_for_lrn = answer_for_lrn;
        this.answer_for_course_lrn = answer_for_course_lrn;
    }

    public String outputSurveyQueReport(Hashtable attempt_hash, Vector id_vec,boolean showSelection) throws SQLException, qdbErrMessage, IOException, cwException {
        try {
            File xlsFile = new File(xlsFilePath);
            if (!xlsFile.exists()) {
                xlsFile.mkdirs();
            }
            wb = Workbook.createWorkbook(new File(xlsFilePath + cwUtils.SLASH + OutputFileName + ".xls"));
            sheet = wb.createSheet("First Sheet", 0);
            setColumnWidth(columnWidth);
            wbSetting = new WorkbookSettings();
            wbSetting.setEncoding(encoding);
            wbSetting.setGCDisabled(true);
            
            //set report title
            String reportTitle = null;
            if (rsp_title != null) {
                reportTitle = rsp_title;
            }
            else {
                reportTitle = ExportHelper.getRptTitle(cur_lan, Report.ASSESSMENT_QUE_GRP);
            }
            setTitleText(reportTitle);
    
            //write the head of report to workbook
            setRptHeadData(con,showSelection);
            setBlankRow(ColumnH);
            //write report detail to workbook
            writeTableHead();
            setReportDetail(attempt_hash, id_vec);
            String outputName = null;
            setColumnWidth(columnWidth);
            wb.write();
            wb.close();
            if (fileCount > 1) {
                zipXlsRpt(xlsFilePath);
                outputName = OutputFileName + ".zip";
            }
            else {
                outputName = OutputFileName + ".xls";
            }
            return outputName;
        }
        catch (RowsExceededException e) {
            CommonLog.error(e.getMessage(),e);
            throw new cwException(e.getMessage());
        }
        catch (WriteException e) {
            CommonLog.error(e.getMessage(),e);
            throw new cwException(e.getMessage());
        }
    }

    /**
     * @param attempt_hash
     * @param id_vec
     */
    private void setReportDetail(Hashtable attempt_hash, Vector id_vec) throws RowsExceededException, WriteException {
        Vector que = null;
        Vector que_attempt_vec = null;
        Vector attempt_type_vec = cwUtils.String2vector(attempt_type);
        
        for(int i=0; i<id_vec.size(); i++) {            
            que = (Vector)id_vec.get(i);
            if((group_by.equalsIgnoreCase(AssessmentQueReport.GROUP_BY_QUESTION) && attempt_hash.get(que.elementAt(0)) != null) || !group_by.equalsIgnoreCase(AssessmentQueReport.GROUP_BY_QUESTION)) {
                if(group_by.equalsIgnoreCase(AssessmentQueReport.GROUP_BY_QUESTION)) {
                    que_attempt_vec = (Vector)attempt_hash.get(que.elementAt(0));
                }else if(group_by.equalsIgnoreCase(AssessmentQueReport.GROUP_BY_QUESTION_TYPE)) {
                    que_attempt_vec = (Vector)attempt_hash.get(que.elementAt(2));
                }else{
                    que_attempt_vec = (Vector)attempt_hash.get(((Long)que.elementAt(4)).toString());
                }
                
                AssessmentQueReport.Attempt_times times = null;
                AssessmentQueReport.Attempt_times all = null;
    
                long attempt_cnt = 0;
                long total_score = 0;
                long usr_total_score = 0;
                long perfect_attempts = 0;
    
                long cor_percent = 0;
                long incor_percent = 0;
                long par_cor_percent = 0;
                long not_graded_percent = 0;
                double avg_score_precent = 0;
                
                long all_total_score = 0;
                long all_usr_total_score = 0;
                long all_perfect_attempts = 0;
    
                if(attempt_type_vec.contains(AssessmentQueReport.ALL_ATTEMPTS_TYPE)){
                    AssessmentQueReport aqr = new AssessmentQueReport();
                    all = aqr.new Attempt_times();
                    all.times = 0;
                }
                for(int j=0; j<que_attempt_vec.size(); j++) {
                    times = (AssessmentQueReport.Attempt_times)que_attempt_vec.elementAt(j);
                    attempt_cnt = times.attempt_cnt;
                    perfect_attempts = times.correct_learner.size();
                    usr_total_score = times.usr_total_score;
                    if(group_by.equalsIgnoreCase(AssessmentQueReport.GROUP_BY_QUESTION)) {
                        total_score = times.atm_max_score * attempt_cnt;
                    }else{
                        total_score = times.atm_max_score;
                    }
                    
                    cor_percent = Math.round(new Float(times.correct_cnt).floatValue()/new Float(attempt_cnt).floatValue()*100);
                    incor_percent = Math.round(new Float(times.incorrect_cnt).floatValue()/new Float(attempt_cnt).floatValue()*100);
                    par_cor_percent = Math.round(new Float(times.partial_correct_cnt).floatValue()/new Float(attempt_cnt).floatValue()*100);
                    not_graded_percent = Math.round(new Float(times.not_graded_cnt).floatValue()/new Float(attempt_cnt).floatValue()*100);
                    avg_score_precent = FormatUtil.getInstance().scaleDouble(new Double(usr_total_score).doubleValue()/new Double(total_score).doubleValue(), 2);
                    if(attempt_type_vec.contains(AssessmentQueReport.ALL_ATTEMPTS_TYPE)){
                        all.attempt_cnt = all.attempt_cnt + attempt_cnt;
                        all.correct_cnt = all.correct_cnt + times.correct_cnt;
                        all.incorrect_cnt = all.incorrect_cnt + times.incorrect_cnt;
                        all.partial_correct_cnt = all.partial_correct_cnt +  times.partial_correct_cnt;
                        all.not_graded_cnt = all.not_graded_cnt + times.not_graded_cnt;
                        all.learner_vec = cwUtils.unionVectors(all.learner_vec, times.learner_vec, false);
                        all_total_score = all_total_score + total_score;
                        all_usr_total_score = all_usr_total_score + usr_total_score;
                        all.que_res_id = cwUtils.unionVectors(all.que_res_id, times.que_res_id, false); 
                        all_perfect_attempts = all_perfect_attempts + perfect_attempts;
                    }
                    if(attempt_type_vec.contains(AssessmentQueReport.NUMBERED_ATTEMPTS_TYPE)) {
                        writeData(j, que, times, cor_percent, incor_percent, par_cor_percent, not_graded_percent, avg_score_precent, perfect_attempts);
                    }
                }
                if(attempt_type_vec.contains(AssessmentQueReport.ALL_ATTEMPTS_TYPE)){
                    long all_cor_percent = Math.round(new Float(all.correct_cnt).floatValue()/new Float(all.attempt_cnt).floatValue()*100);
                    long all_incor_percent = Math.round(new Float(all.incorrect_cnt).floatValue()/new Float(all.attempt_cnt).floatValue()*100);
                    long all_par_cor_percent = Math.round(new Float(all.partial_correct_cnt).floatValue()/new Float(all.attempt_cnt).floatValue()*100);
                    long all_not_graded_percent = Math.round(new Float(all.not_graded_cnt).floatValue()/new Float(all.attempt_cnt).floatValue()*100);
                    double all_avg_score_precent = FormatUtil.getInstance().scaleDouble(new Double(all_usr_total_score).doubleValue()/new Double(all_total_score).doubleValue(), 2);
                    int j = -1;
                    if(attempt_type_vec.size() == 1) {
                        j=0;
                    }
                    writeData(j, que, all, all_cor_percent, all_incor_percent, all_par_cor_percent, all_not_graded_percent, all_avg_score_precent, all_perfect_attempts);
                }
            }
        }
    }

    /**
     * @param que_attempt_vec
     * @param times
     * @param cor_percent
     * @param incor_percent
     * @param par_cor_percent
     * @param not_graded_percent
     */
    private void writeData(int j, Vector que, Attempt_times times, long cor_percent, long incor_percent, long par_cor_percent, long not_graded_percent, 
                           double avg_score_precent, long perfect_attempts) throws RowsExceededException, WriteException {
        short index = 0;
        getNewRow();
        if(j==0 && group_by.equalsIgnoreCase(AssessmentQueReport.GROUP_BY_QUESTION)) {
            setCellContent((String)que.elementAt(1), index++);
            
            String que_xml = (que.size() >= 8 ?  (String)que.elementAt(7) : "");
            /*if(null != que.elementAt(2) && ((String)que.elementAt(2)).equals("TF")){
            	que_xml = (String)que.elementAt(1);
            }*/
            setCellContent(que_xml, index++);
            
            if(content_vec.contains("res_fdr")) {
                setCellContent((String)que.elementAt(5), index++);
            }
            if(content_vec.contains("res_type")) {
                String res_type = getResType((String)que.elementAt(2), cur_lan);
                setCellContent(res_type, index++);
            }
            
            int que_score = (que.size() >= 7 ?  (Integer)que.elementAt(6) : 0);
            setCellContent(que_score, index++);
            
            if(content_vec.contains("res_diff")) {
                String res_diff = getResDiff(((Long)que.elementAt(3)).longValue(), cur_lan);
                setCellContent(res_diff, index++);
            }
        }else if(j!=0 && group_by.equalsIgnoreCase(AssessmentQueReport.GROUP_BY_QUESTION)) {
            setCellContent("", index++);
            setCellContent("", index++);
            if(content_vec.contains("res_fdr")) {
                setCellContent("", index++);
            } 
            if(content_vec.contains("res_type")) {
                setCellContent("", index++);
            } 
            setCellContent("", index++);
            if(content_vec.contains("res_diff")) {
                setCellContent("", index++);
            } 
        }else if (j==0 && group_by.equalsIgnoreCase(AssessmentQueReport.GROUP_BY_QUESTION_TYPE)) {
            String res_type = getResType((String)que.elementAt(2), cur_lan);
            setCellContent(res_type, index++);
        }else if (j==0 && group_by.equalsIgnoreCase(AssessmentQueReport.GROUP_BY_RESOURCE_FOLDER)) {
            setCellContent((String)que.elementAt(5), index++);
            setCellContent(((Long)que.elementAt(4)).longValue(), index++);
        }else if (j!=0 && group_by.equalsIgnoreCase(AssessmentQueReport.GROUP_BY_RESOURCE_FOLDER)) {
            setCellContent("", index++);
            setCellContent("", index++);
        }else if (j!=0 && group_by.equalsIgnoreCase(AssessmentQueReport.GROUP_BY_QUESTION_TYPE)) {
            setCellContent("", index++);
        }
        
        if(times.times == 0) {
            setCellContent(LangLabel.getValue(cur_lan, "lab_all_atm_type"), index++);
        }else{
            setCellContent(LangLabel.getValue(cur_lan, "lab_total_attempt")+" #"+times.times, index++);
        }
        if (content_vec.contains("attempt_cnt")) {
            setCellContent(times.attempt_cnt, index++);
        }
        if (content_vec.contains("correct")) {
            setCellContent(times.correct_cnt+"（"+cor_percent+"%）", index++);
          //  setCellContent(cor_percent, index++);
        }
        if (content_vec.contains("incorrect")) {
            setCellContent(times.incorrect_cnt+"（"+incor_percent+"%）", index++);
           // setCellContent(incor_percent, index++);
        }
        if (content_vec.contains("partial_correct")) {
            setCellContent(times.partial_correct_cnt+"（"+par_cor_percent+"%）", index++);
          //  setCellContent(par_cor_percent, index++);
        }
        if (content_vec.contains("not_graded")) {
            setCellContent(times.not_graded_cnt+"（"+not_graded_percent+"%）", index++);
           // setCellContent(not_graded_percent, index++);
        }
        if (content_vec.contains("avg_sore")) {
            setCellContent(avg_score_precent, index++);
        }
        if (content_vec.contains("perfect_attempts")) {
            setCellContent(perfect_attempts, index++);
        }
        if (content_vec.contains("questions")) {
            setCellContent(times.que_res_id.size(), index++);
        }
        if (content_vec.contains("learners")) {
            setCellContent(times.learner_vec.size(), index++);
        }
   }

    private String getResDiff(long res_diff, String cur_lan) {
        String cur_res_diff = "";
        String lab_res_diff = "";
        
        if (res_diff == 1) {
            lab_res_diff = "lab_easy";
        }
        if (res_diff == 2) {
            lab_res_diff = "lab_normal";
        }
        if (res_diff == 3) {
            lab_res_diff = "lab_hard";
        }
        
        cur_res_diff = LangLabel.getValue(cur_lan, lab_res_diff);
        return cur_res_diff;                        
    }

    private String getResType(String res_type, String cur_lan) {
        String cur_res_type = "";
        String lab_res_type = "";
        if(res_type.equals("WCT")){
            lab_res_type = "lab_wct";
        }else if(res_type.equals("SSC")) {
            lab_res_type = "lab_ssc";
        }else if(res_type.equals("QUE")) {
            lab_res_type = "lab_que";
        }else if(res_type.equals("MC")) {
            lab_res_type = "lab_mc";
        }else if(res_type.equals("FB")) {
            lab_res_type = "lab_fb";
        }else if(res_type.equals("MT")) {
            lab_res_type = "lab_mt";
        }else if(res_type.equals("TF")) {
            lab_res_type = "lab_tf";
        }else if(res_type.equals("ES")) {
            lab_res_type = "lab_es";
        }else if(res_type.equals("FSC")) {
            lab_res_type = "lab_fsc";
        }else if(res_type.equals("DSC")) {
            lab_res_type = "lab_dsc";
        }else if(res_type.equals("ASM")) {
            lab_res_type = "lab_asm";
        }else if(res_type.equals("FAS")) {
            lab_res_type = "lab_fas";
        }else if(res_type.equals("DAS")) {
            lab_res_type = "lab_das";
        }else{
            lab_res_type = "lab_na";
        }
        
        cur_res_type = LangLabel.getValue(cur_lan, lab_res_type);
        return cur_res_type;
    }

    private void writeTableHead() throws RowsExceededException, WriteException {
        Iterator iter_content = content_vec.iterator();
        short index = 0;
        getNewRow();
        
        WritableCellFormat cellFormatWithBold = getCellFormatWithBold(10);
        
        if(group_by.equalsIgnoreCase(AssessmentQueReport.GROUP_BY_QUESTION)) {
        	setCellContent(LangLabel.getValue(cur_lan, "lab_que_title"), index++,cellFormatWithBold);
            setCellContent(LangLabel.getValue(cur_lan, "lab_que"), index++ ,cellFormatWithBold);
        }else if (group_by.equalsIgnoreCase(AssessmentQueReport.GROUP_BY_QUESTION_TYPE)) {
            setCellContent(LangLabel.getValue(cur_lan, "lab_que_type"), index++ ,cellFormatWithBold);
        }else{
            setCellContent(LangLabel.getValue(cur_lan, "lab_que_fdr_title"), index++,cellFormatWithBold);
            setCellContent(LangLabel.getValue(cur_lan, "lab_que_fdr_id"), index++,cellFormatWithBold);
        }

        if (content_vec.contains("res_fdr")) {
            setCellContent(LangLabel.getValue(cur_lan, "lab_que_fdr_title"), index++,cellFormatWithBold);
        }
        if (content_vec.contains("res_type")) {
            setCellContent(LangLabel.getValue(cur_lan, "lab_que_type"), index++,cellFormatWithBold);
        }
        
        if(group_by.equalsIgnoreCase(AssessmentQueReport.GROUP_BY_QUESTION)) {
        	setCellContent(LangLabel.getValue(cur_lan, "lab_score"), index++,cellFormatWithBold);
        }
        
        if (content_vec.contains("res_diff")) {
            setCellContent(LangLabel.getValue(cur_lan, "lab_que_diff"), index++,cellFormatWithBold);
        }
        setCellContent(LangLabel.getValue(cur_lan, "lab_atm_type"), index++,cellFormatWithBold);
        if (content_vec.contains("attempt_cnt")) {
            setCellContent(LangLabel.getValue(cur_lan, "lab_atm"), index++,cellFormatWithBold);
        }
        if (content_vec.contains("correct")) {
            setCellContent(LangLabel.getValue(cur_lan, "lab_correct"), index++,cellFormatWithBold);
            //setCellContent("%", index++);
        }
        if (content_vec.contains("incorrect")) {
            setCellContent(LangLabel.getValue(cur_lan, "lab_incorrect"), index++,cellFormatWithBold);
            //setCellContent("%", index++);
        }
        if (content_vec.contains("partial_correct")) {
            setCellContent(LangLabel.getValue(cur_lan, "lab_partial_correct"), index++,cellFormatWithBold);
            //setCellContent("%", index++);
        }
        if (content_vec.contains("not_graded")) {
            setCellContent(LangLabel.getValue(cur_lan, "lab_not_graded_cnt"), index++,cellFormatWithBold);
            //setCellContent("%", index++);
        }
        if (content_vec.contains("avg_sore")) {
            setCellContent(LangLabel.getValue(cur_lan, "lab_avg_score1"), index++,cellFormatWithBold);
        }
        if (content_vec.contains("perfect_attempts")) {
            setCellContent(LangLabel.getValue(cur_lan, "lab_perfect_attempts"), index++,cellFormatWithBold);
        }
        if (content_vec.contains("questions")) {
            setCellContent(LangLabel.getValue(cur_lan, "lab_questions"), index++,cellFormatWithBold);
        }
        if (content_vec.contains("learners")) {
            setCellContent(LangLabel.getValue(cur_lan, "lab_learners"), index++,cellFormatWithBold);
        }
    }


    /*
     * set report head data from a hashMap to workbook
     */
    public void setRptHeadData(Connection con,boolean showSelection) throws cwException, RowsExceededException, WriteException, SQLException {
        //itm title
        getNewRow();
        
        StringBuffer itm_title = new StringBuffer();
        aeItem aeItm = new aeItem();
        aeItm.itm_id = itm_id;
        
        Vector itm_id_vec = new Vector();
        itm_id_vec.add(new Long(itm_id));
        Hashtable itm_id_title = aeItem.getItemTitle(con, itm_id_vec);
        aeItm.itm_title = (String)itm_id_title.get(new Long(itm_id));
        aeItm.itm_code = aeItm.getItemCode(con);
        
        if(aeItm.getRunInd(con)) {
            aeItemRelation itr = new aeItemRelation();
            itr.ire_child_itm_id = aeItm.itm_id;
            aeItem p_itm = itr.getParentInfo(con);
            itm_title.append(p_itm.itm_title).append("(").append(p_itm.itm_code).append(")").append(">");
        }
        
        WritableCellFormat cellFormatWithBold = getCellFormatWithBold(10);
        
        itm_title.append(aeItm.itm_title).append("(").append(aeItm.itm_code).append(")");
        setCellContent(LangLabel.getValue(cur_lan, "lab_cos"), ColumnA , cellFormatWithBold);
        setCellContent(itm_title.toString(), ColumnB);
        sheet.mergeCells(ColumnB, nowRowNum, ColumnH, nowRowNum);

        //mod title
        getNewRow();
        String mod_title = dbResource.getResTitle(con, mod_id);
        setCellContent(LangLabel.getValue(cur_lan, "lab_mod_title"), ColumnA ,cellFormatWithBold);
        setCellContent(mod_title, ColumnB);
        sheet.mergeCells(ColumnB, nowRowNum, ColumnH, nowRowNum);
        
        if(showSelection){
	        //ent id 
	        getNewRow();
	        setCellContent(LangLabel.getValue(cur_lan, "lab_lrn_group"), ColumnA ,cellFormatWithBold);
	        if(ent_id_lst != null && !all_user_ind){
	            String entIdLst = dbUtils.array2list(ent_id_lst);
	            if(dbEntity.getEntityType(con, (new Long(ent_id_lst[0])).longValue()).equals(dbEntity.ENT_TYPE_USER)) {
	                Hashtable user_hash = dbRegUser.getDisplayName(con, entIdLst);
	                StringBuffer usr_diplay_bil = new StringBuffer();
	                for(int i=0; i<ent_id_lst.length; i++) {
	                    usr_diplay_bil.append(user_hash.get(ent_id_lst[i]));
	                    if (i != ent_id_lst.length - 1) {
	                        usr_diplay_bil.append(", ");
	                    }
	                }
	                setCellContent(usr_diplay_bil.toString(), ColumnB);
	            }else{
	                Hashtable user_group_hash = dbUserGroup.getDisplayName(con, entIdLst);
	                StringBuffer usg_diplay_bil = new StringBuffer();
	                for(int i=0; i<ent_id_lst.length; i++) {
	                    usg_diplay_bil.append(user_group_hash.get(ent_id_lst[i]));
	                    if (i != ent_id_lst.length - 1) {
	                        usg_diplay_bil.append(", ");
	                    }
	                }
	                setCellContent(usg_diplay_bil.toString(), ColumnB);
	            }
	        }else {
	            if(all_user_ind && answer_for_lrn && !answer_for_course_lrn) {
	            	setCellContent(LangLabel.getValue(cur_lan, "lab_answer_for_lrn"), ColumnB);
	            } else if (all_user_ind && !answer_for_lrn && answer_for_course_lrn) {
	            	setCellContent(LangLabel.getValue(cur_lan, "lab_answer_for_course_lrn"), ColumnB);
	            } else  {
	            	setCellContent(LangLabel.getValue(cur_lan, "lab_by_all_user"), ColumnB);
	            }
	        }
	        sheet.mergeCells(ColumnB, nowRowNum, ColumnH, nowRowNum);
	        // attendance date
	        if (att_create_start_datetime != null || att_create_end_datetime != null) {
	            getNewRow();
	            StringBuffer att_period = new StringBuffer();
	            att_period.append(LangLabel.getValue(cur_lan, "lab_from")).append(" ");
	            if (att_create_start_datetime != null) {
	                att_period.append(cwUtils.escNull(att_create_start_datetime)).append(" ");
	            }else {
	                att_period.append(LangLabel.getValue(cur_lan, "lab_na"));
	            }
	            att_period.append(LangLabel.getValue(cur_lan, "lab_to")).append(" ");
	            if (att_create_end_datetime != null) {
	                att_period.append(cwUtils.escNull(att_create_end_datetime)).append(" ");
	            }else {
	                att_period.append(LangLabel.getValue(cur_lan, "lab_na"));
	            }
	            setCellContent(LangLabel.getValue(cur_lan, "lab_period"), ColumnA,cellFormatWithBold);
	            setCellContent(att_period.toString(), ColumnB);
	            sheet.mergeCells(ColumnB, nowRowNum, ColumnH, nowRowNum);
	        }
	        // attempt date
	        if (!attempt_start_datetime.equals(Timestamp.valueOf(cwUtils.MIN_TIMESTAMP)) || !attempt_end_datetime.equals(Timestamp.valueOf(cwUtils.MAX_TIMESTAMP))) {
	            getNewRow();
	            StringBuffer attempt_period = new StringBuffer();
	            attempt_period.append(LangLabel.getValue(cur_lan, "lab_from")).append(" ");
	            if (!attempt_start_datetime.equals(Timestamp.valueOf(cwUtils.MIN_TIMESTAMP))) {
	                attempt_period.append(cwUtils.escNull(attempt_start_datetime)).append(" ");
	            }else {
	                attempt_period.append(LangLabel.getValue(cur_lan, "lab_na"));
	            }
	            attempt_period.append(LangLabel.getValue(cur_lan, "lab_to")).append(" ");
	            if (!attempt_end_datetime.equals(Timestamp.valueOf(cwUtils.MAX_TIMESTAMP))) {
	                attempt_period.append(cwUtils.escNull(attempt_end_datetime)).append(" ");
	            }else{
	                attempt_period.append(LangLabel.getValue(cur_lan, "lab_na"));
	            }
	            setCellContent(LangLabel.getValue(cur_lan, "lab_atm_period"), ColumnA,cellFormatWithBold);
	            setCellContent(attempt_period.toString(), ColumnB);
	            sheet.mergeCells(ColumnB, nowRowNum, ColumnH, nowRowNum);
	        }
	        // attempt type
	        getNewRow();
	        
	        Vector attempt_type_vec = cwUtils.String2vector(attempt_type);
	        StringBuffer attempt_types = new StringBuffer();
	        if(attempt_type_vec.size() == 2){
	            attempt_types.append(LangLabel.getValue(cur_lan, "lab_num_atm_type")).append(", ")
	                         .append(LangLabel.getValue(cur_lan, "lab_all_atm_type"));
	        }else if (attempt_type_vec.contains(AssessmentQueReport.ALL_ATTEMPTS_TYPE) ) {
	            attempt_types.append(LangLabel.getValue(cur_lan, "lab_all_atm_type"));
	        }else if (attempt_type_vec.contains(AssessmentQueReport.NUMBERED_ATTEMPTS_TYPE) ) {
	            attempt_types.append(LangLabel.getValue(cur_lan, "lab_num_atm_type"));
	        }
	        setCellContent(LangLabel.getValue(cur_lan, "lab_atm_type"), ColumnA,cellFormatWithBold);
	        setCellContent(attempt_types.toString(), ColumnB);
	        sheet.mergeCells(ColumnB, nowRowNum, ColumnH, nowRowNum);
	        // group by
	        getNewRow();
	        setCellContent(LangLabel.getValue(cur_lan, "lab_group_by"), ColumnA,cellFormatWithBold);
	        if (group_by.equalsIgnoreCase(AssessmentQueReport.GROUP_BY_QUESTION) ) {
	            setCellContent(LangLabel.getValue(cur_lan,"lab_que"), ColumnB);
	        }else if(group_by.equalsIgnoreCase(AssessmentQueReport.GROUP_BY_QUESTION_TYPE) ){
	            setCellContent(LangLabel.getValue(cur_lan,"lab_que_type"), ColumnB);
	        }else{
	            setCellContent(LangLabel.getValue(cur_lan,"lab_group_by_res_fdr"), ColumnB);
	        }
	        sheet.mergeCells(ColumnB, nowRowNum, ColumnH, nowRowNum);
        
        }

    }
}
