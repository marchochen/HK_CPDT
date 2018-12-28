/*
 * Created on 2006-2-23
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.cw.wizbank.export;

import java.io.IOException;
import java.util.Iterator;

import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WriteException;

import com.cw.wizbank.qdb.dbQuestion;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.util.cwUtils;

public class ExportQueHelper extends ExportHelper{
    static String OutputFileName = "wb_export_que";
    static String SheetName = "Data";
    static String NODATA = "--";
    static String COLUMN_FOLDER_ID = "Folder ID";
    static String COLUMN_RESOURCE_ID = "Resource ID";
    static String COLUMN_TITLE = "Title";
    static String COLUMN_QUESTION = "Question";
    static String COLUMN_SHUFFLE = "Shuffle";
    static String COLUMN_ANSWER = "Answer";
    static String COLUMN_ASHTML = "AsHTML";
    static String COLUMN_OPTION = "Option";
    static String COLUMN_BLANK = "Blank";
    static String COLUMN_SCORE = "Score";
    static String COLUMN_DIFFICULTY = "Difficulty";
    static String COLUMN_DURATION = "Duration";
    static String COLUMN_STATUS = "Status";
    static String COLUMN_EXPLAIN = "Explanation";
    static String COLUMN_DESC = "Description";
    String[] filehead;
    String thisQueType;
    protected ExportQue que;

    public ExportQueHelper(String tempDir, String tempDirName, String winName, String encoding, int processUnit, String[] head, String que_type) throws WriteException, IOException {
        super(tempDir, tempDirName, winName, encoding, processUnit, OutputFileName, SheetName);
        filehead = head;
        thisQueType = que_type;
    }
    
    
    public ExportQueHelper(String tempDir, String tempDirName, String winName, String encoding, int processUnit, String[] head, String que_type, ExportQue que) throws WriteException, IOException {
    	super(tempDir, tempDirName, winName, encoding, processUnit, OutputFileName, SheetName);
    	filehead = head;
        thisQueType = que_type;
        this.que = que;
    }

    public void writeContent(ExportQue.QueData queData, String[] head, ExportControllerCommon exportProgresser, String que_type) 
        throws WriteException, qdbErrMessage, IOException {
        String columnName = null;
        getNewRow();
        short columnPos = 0;
        for (int i = 0; i < head.length; i++) {
            columnName = head[i];
            if (columnName.equals(que.COL_CATEGORY_ID)) { //obj_id
                if (queData.folder_id_common > 0) {
                    setCellContent(queData.folder_id_common, columnPos++);
                }
                else {
                    setCellContent("", columnPos++);
                }
            }
            else if (columnName.equals(que.COL_RESOURCE_ID)) { //system_id
                setCellContent(queData.que_res_id_common, columnPos++);
            }
            else if (columnName.equals(que.COL_TITLE)) { //title
                setCellContent(queData.que_title_common, columnPos++);
            }
            else if (columnName.equals(que.COL_QUESTION)) { //question
                StringBuffer fbQueTxt = new StringBuffer();
                if (que_type.equals(dbQuestion.QUE_TYPE_FILLBLANK) && queData.FBQueText.size() > 1) {
                    Iterator fbtext = queData.FBQueText.iterator();
                    int blankCount = 1;
                    while (fbtext.hasNext()) {
                        String temp = (String)fbtext.next();
                        if (temp.length() == 0) {
                            fbQueTxt.append("[blank").append(blankCount++).append("]");
                        }
                        else {
                            fbQueTxt.append(temp);
                        }
                    }
                    setCellContent(cwUtils.unescHTML(cwUtils.removeTag(fbQueTxt.toString())), columnPos++);
                }
                else {
                    setCellContent(cwUtils.unescHTML(cwUtils.removeTag(queData.que_text_common)), columnPos++);
                }
            }
            else if (columnName.equals(que.COL_SHUFFLE)) { //shuffle
                if (queData.que_type_common.equals(dbQuestion.RES_SUBTYPE_FSC) 
                    || queData.que_type_common.equals(dbQuestion.RES_SUBTYPE_DSC)) {
                    if (queData.sc_allow_shuffle_ind > 0) {
                        setCellContent("Y", columnPos++);
                    }
                    else {
                        setCellContent("N", columnPos++);
                    }
                }
                else if (queData.folder_id_common == 0) {
                    setCellContent("", columnPos++);
                }
                else {
                    setCellContent(queData.shuffle, columnPos++);
                }

            }
            else if (columnName.startsWith(que.COL_ANSWER)) { //answer
                if (que_type.equals(dbQuestion.QUE_TYPE_MATCHING) && queData.MTAnswer != null && queData.MTAnswer.length() > 0) {
                    setCellContent(queData.MTAnswer.substring(0, queData.MTAnswer.indexOf("|")), columnPos++);
                }
                else if (que_type.equals(dbQuestion.QUE_TYPE_TRUEFALSE)) {
                    if (queData.score_lst[0] > 0) {
                        setCellContent("TRUE", columnPos++);
                    }
                    else {
                        setCellContent("FALSE", columnPos++);
                    }
                }
                else if (queData.score_lst != null && queData.score_lst.length > 0) {
                    StringBuffer score_list = new StringBuffer();
                    boolean hasPrv = false;
                    int score = 0;
                    for (int score_count = 0; score_count < queData.score_lst.length; score_count++) {
                        score = queData.score_lst[score_count];
                        if (score > 0) {
                            if (hasPrv) {
                                score_list.append(",");
                            }
                            score_list.append(score_count + 1);
                            hasPrv = true;
                        }
                    }
                    setCellContent(score_list.toString(), columnPos++);
                }
                else {
                    columnPos++;
                }
            }
            else if (columnName.equals(que.COL_AS_HTML)) { //asHtml
                setCellContent(queData.asHtmlCommon, columnPos++);
            }
            else if (columnName.equals(que.COL_MODEL_ANSWER)) { //model answer
                if (queData.explain != null && queData.explain.length() > 0) {
                    setCellContent(queData.explain, columnPos++);
                } else {
                    setCellContent("", columnPos++);
                }
            }
            else if (columnName.equals(que.COL_OPTION)) { //option
                for (int j = 0; j < 10; j++) {
                    if (queData.que_condition_text != null && queData.que_condition_text.length > j 
                        && queData.que_condition_text[j].length() > 0) {
                        setCellContent(cwUtils.unescHTML(cwUtils.removeTag(queData.que_condition_text[j])), columnPos++);
                    }
                    else {
                        columnPos++;
                    }
                }
            }
            else if (columnName.equals(que.COL_BLANK)) { //blank
                for (int j = 0; j < 10; j++) {
                    if (queData.FBAnswer.size() > j && queData.FBAnswer.get(j) != null 
                        && ((String)queData.FBAnswer.get(j)).length() > 0) {
                        setCellContent((String)queData.FBAnswer.get(j), columnPos++);
                        setCellContent(((Integer)queData.FBScore.get(j)).intValue(), columnPos++);
                    }
                    else {
                        columnPos += 2;
                    }
                }
            }
            else if (columnName.equals(que.COL_SOURCE)) { //source & target
                for (int j = 0; j < 10; j++) {
                    if (queData.MTSource.size() > j && queData.MTSource.get(j) != null 
                        && ((String)queData.MTSource.get(j)).length() > 0) {
                        setCellContent((String)queData.MTSource.get(j), columnPos++);
                    }
                    else {
                        columnPos++;
                    }
                }
                for (int j = 0; j < 10; j++) {
                    if (queData.MTTarget.size() > j && queData.MTTarget.get(j) != null 
                        && ((String)queData.MTTarget.get(j)).length() > 0) {
                        setCellContent((String)queData.MTTarget.get(j), columnPos++);
                    }
                    else {
                        columnPos++;
                    }
                }
            }
            else if (columnName.startsWith(que.COL_SCORE)) {
                if (que_type.equals(dbQuestion.QUE_TYPE_MATCHING) && queData.MTAnswer != null && queData.MTAnswer.length() > 0) {
                    setCellContent(queData.MTAnswer.substring(queData.MTAnswer.indexOf("|") + 1, queData.MTAnswer.length()), columnPos++);
                }
                else {
                    if (queData.que_score_common <= 0) {
                        setCellContent("", columnPos++);
                    }
                    else {
                        setCellContent(queData.que_score_common, columnPos++);
                    }
                }

            }
            else if (columnName.equals(que.COL_DIFFICULTY)) {
                setCellContent(queData.que_difficulty_common, columnPos++);
            }
            else if (columnName.equals(que.COL_DURATION)) {
                if (queData.duration > 0) {
                    setCellContent(queData.duration, columnPos++);
                }
                else {
                    setCellContent("", columnPos++);
                }
            }
            else if (columnName.equals(que.COL_STATUS)) {
                setCellContent(queData.que_res_status_common, columnPos++);
            }
            else if (columnName.equals(que.COL_DESCRIPTION)) {
                setCellContent(queData.que_desc_common, columnPos++);
            }
            else if (columnName.equals(que.COL_EXPLAIN)) {
                setCellContent(queData.explain, columnPos++);
            }
            else if (columnName.equals(que.COL_SUBMIT_FILE)) {
                if (queData.submit_file > 0) {
                    setCellContent("Y", columnPos++);
                }
                else {
                    setCellContent("N", columnPos++);
                }
            }
            else if (columnName.equals(que.COL_SHUFFLE_OPTION)) {
                if (queData.folder_id_common == 0) {
                    setCellContent(queData.shuffle, columnPos++);
                }
                else {
                    setCellContent("", columnPos++);
                }
            }
            else if (columnName.equals(que.COL_CRITERIA)) {
                if (queData.que_type_common.equals(dbQuestion.RES_SUBTYPE_DSC)) {
                    if (queData.sc_criteria != null && queData.sc_criteria.length() > 0) {
                        setCellContent(queData.sc_criteria, columnPos++);
                    }
                    else {
                        setCellContent("", columnPos++);
                    }
                }
                else {
                    setCellContent("", columnPos++);
                }
            }
        }
    }

    public void writeHead() throws WriteException, IOException {
        getNewRow();
        short columnNum = 0;
        WritableCellFormat cf = getStyleByName(STYLE_TITLE_WITH_FILLBACK);
        for (int i = 0; i < filehead.length; i++) {
            if (filehead[i].startsWith(que.COL_OPTION)) {
                for (int j = 0; j < 10; j++) {
                    setCellContent(que.COL_OPTION + " " + (j + 1), columnNum++, cf);
                }
                i++;
            }
            if (filehead[i].startsWith(que.COL_BLANK)) {
                for (int j = 0; j < 10; j++) {
                    setCellContent(que.COL_BLANK + " " + (j + 1) + " " + que.COL_ANSWER, columnNum++, cf);
                    setCellContent(que.COL_BLANK + " " + (j + 1) + " " + que.COL_SCORE, columnNum++, cf);
                }
                i++;
            }
            if (filehead[i].startsWith(que.COL_SOURCE)) {
                for (int j = 0; j < 10; j++) {
                    setCellContent(que.COL_SOURCE + " " + (j + 1), columnNum++, cf);
                }
                for (int j = 0; j < 10; j++) {
                    setCellContent(que.COL_TARGET + " " + (j + 1), columnNum++, cf);
                }
                i++;
            }
            if (thisQueType.equals(dbQuestion.RES_SUBTYPE_FSC) && filehead[i].equals(que.COL_CRITERIA)) {
            }
            else {
                setCellContent(filehead[i], columnNum++, cf);
            }
        }
    }
    
    public void getNewRow() throws WriteException, IOException {
        if (nowRowNum == process_unit - 1) {
            getNewFile();
            nowRowNum = -1;
            errorRowNum = -1;
            writeHead();
        }
        nowRowNum++;
    }

    public void changeFile() throws WriteException, IOException {
        getNewFile();
        nowRowNum = -1;
        errorRowNum = -1;
        writeHead();
    }

    public void writeError(int error_code, String error_content) throws WriteException {
        if (errorSheet == null) {
            errorSheet = wb.createSheet("Error", SHEET_POSTION_SECOND);
        }
        errorRowNum++;
        Label label = new Label(0, errorRowNum, error_content);
        errorSheet.addCell(label);
    }
}
