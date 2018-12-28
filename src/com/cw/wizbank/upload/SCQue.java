package com.cw.wizbank.upload;

import java.io.*;
import java.sql.*;
import java.util.*;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

import com.cw.wizbank.qdb.*;
import com.cw.wizbank.accesscontrol.AcResources;
import com.cw.wizbank.accesscontrol.AcTrainingCenter;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.db.DbUploadLog;
import com.cw.wizbank.db.DbRawQuestion;
import com.cw.wizbank.util.*;
import com.cwn.wizbank.utils.CharUtils;
import com.cwn.wizbank.utils.CommonLog;
public class SCQue{
    
    //Log filename
    public final static String[] FILENAME_LIST = { UploadUtils.SUCCESS_FILE, UploadUtils.FAILURE_FILE, UploadUtils.ERROR_FILE};
    public final static String[] LOG_LIST = {"SUCCESS", "UNSUCCESS", "ERROR" };
    
    
    public final static String DATA_SHEET_NAME = "Data";
    
    //History Session Key
    private static final String SESS_LOG_HISTORY_LOG_ID = "SESS_LOG_HISTORY_LOG_ID";
    private static final String SESS_LOG_HISTORY_TIMESTAMP = "SESS_LOG_HISTORY_TIMESTAMP";
    private static final String SESS_LOG_HISTORY_TOTAL_REC = "SESS_LOG_HISTORY_TOTAL_REC";
    private static final String SESS_LOG_HISTORY_TOTAL_PAGE = "SESS_LOG_HISTORY_TOTAL_PAGE";
        
//    "Folder ID",
//    "Title",
//    "Question",
//    "AsHTML",
//    "Shuffle",
//    "Difficulty",
//    "Status",
//    "Option 1",
//    "Option 2",
//    "Shuffle Option",
//    "Answer",
//    "Score"

    
    private final static int ROW_INVALID_CATEGORY_ID = 1;
    private final static int ROW_INVALID_SYSTEM_ID = 2;
    private final static int ROW_INVALID_TITLE = 3;
    private final static int ROW_INVALID_CONTENT = 4;
    private final static int ROW_INVALID_ASHTML = 5;
    private final static int ROW_INVALID_SHUFFLE = 6;
    private final static int ROW_INVALID_DIFFICULTY = 7;
    private final static int ROW_INVALID_STATUS = 8;
    private final static int ROW_INVALID_DESC = 9;
    private final static int ROW_INVALID_OPTION = 10;
    private final static int ROW_INVALID_SC_SUB_SHUFFLE = 11;
    private final static int ROW_INVALID_ANSWER = 12;
    private final static int ROW_INVALID_SCORE = 13;
    private final static int ROW_INVALID_CRITERIA = 14;
    private final static int ROW_INVALID_PERMISSION = 15;
    private final static int ROW_INVALID_SPEC = 16;
    
    
    
    
    private Vector invalidCategoryIdVec = null;
    private Vector invalidResourceIdVec = null;
    private Vector invalidTitle = null;
    private Vector invalidContent = null;
    private Vector invalidAshtml = null;
    private Vector invalidShuffle = null;
    private Vector invalidDifficulty = null;
    private Vector invalidStatus = null;
    private Vector invalidDesc = null;
    private Vector invalidOptionVec = null;
    private Vector invalidScSubShuffle = null;
    private Vector invalidAnswer = null;
    private Vector invalidScore = null;
    private Vector invalidColumn = null;
    private Vector invalidCriteria = null;
    private Vector invalidpermission = null;
    private Vector invalidSpec = null;
    private Vector dupQueId = null;
    private boolean invalidXlsFile = false;
    private boolean isLastScEmpty = true;
    
    private Connection con = null;
    private long site_id;
    private String usr_id = null;
    private qdbEnv static_env = null;
    private String cur_role = null;
    
    private int optionNum = 0;

    private Vector cachedValidCategoryIdVec = null;
    private Vector cachedInvalidCategoryIdVec = null;

    private String[] StdColName = {
        "Folder ID",
        "Resource ID",
        "Title",
        "Question",
        "AsHTML",
        "Difficulty",
        "Status",
        "Description",
        "Option ",
        "Shuffle Option",
        "Answer",
        "Score",
        "Criteria"
    };
    
    private String[] REQUIRED_COLUMN = {
        "Folder ID",
        "Title",
        "Question",
        "AsHTML",
        "Difficulty",
        "Status",
        "Option 1",
        "Option 2",
        "Shuffle Option",
        "Answer",
        "Score"
    };

    private String StdOptionColName = "Option ";
    private static final int MAX_OPTION_NUM = 100;
    //UnicodeLittle:excel unicode encoding
    private final static String DEFAULT_ENC = "UnicodeLittle";

    private final static String DEFAULT_LANGUAGE = "ISO-8859-1";
    private String DEFAULT_FOLDER = "CW";

    private Vector inColHeader = null;
    
    public long parent_sc_que_id = 0;
    
    public SCQue(Connection con, qdbEnv static_env, loginProfile prof){
    	// set label
        this.StdColName = this.getLabelArray(prof);
        this.REQUIRED_COLUMN = this.getRequiredLabelArray(prof);
        this.setOtherLabel(prof);
        
        this.con = con;
        this.site_id = prof.root_ent_id;
        this.usr_id = prof.usr_id;
        this.cur_role = prof.current_role;
        this.static_env = static_env;
        this.inColHeader = new Vector();
        invalidCategoryIdVec = new Vector();
        invalidResourceIdVec = new Vector();
        invalidTitle = new Vector();
        invalidContent = new Vector();
        invalidAshtml = new Vector();
        invalidShuffle = new Vector();
        invalidDifficulty = new Vector();
        invalidStatus = new Vector();
        invalidDesc = new Vector();
        invalidOptionVec = new Vector();
        invalidScSubShuffle = new Vector();
        invalidAnswer = new Vector();
        invalidScore = new Vector();
        invalidColumn = new Vector();
        invalidCriteria = new Vector();
        invalidpermission = new Vector();
        invalidSpec = new Vector();
        dupQueId = new Vector();
                
        cachedValidCategoryIdVec = new Vector();
        cachedInvalidCategoryIdVec = new Vector();
        
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
    
    public String uploadQue(File srcFile, String ulg_desc, boolean allow_upd, String que_type, WizbiniLoader wizbini)
        throws cwException, IOException, cwSysMessage, SQLException {
            Workbook workBook = null;
            Sheet sheet = null;
            boolean errorFlag = false;
            WorkbookSettings wbSetting = new WorkbookSettings();
            wbSetting.setIgnoreBlanks(true);
            
            try {
                workBook = Workbook.getWorkbook(srcFile, wbSetting);
            }
            catch (BiffException e) {
                throw new cwSysMessage("GEN009");
            }
            
            long ulg_id = 0;
            sheet = workBook.getSheet(DATA_SHEET_NAME);
            if (sheet == null) {
                errorFlag = true;
                invalidXlsFile = true;
            } else {
                ulg_id = UploadUtils.insUploadLog(con, this.usr_id, ulg_desc, srcFile.getName(), static_env, que_type);

                String[] head_column = readLineXls(sheet, 0, sheet.getColumns(), false);
            
                if (head_column == null || head_column.length == 0) {
                    throw new cwSysMessage(UploadUtils.SMSG_ULG_INVALID_FILE);
                }
                Vector invalidColHeader = parseInValidColHeader(head_column);            
                if( !invalidColHeader.isEmpty() ){
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
                //Vector validRowVec = new Vector();

                int rowLimit = wizbini.cfgSysSetupadv.getResBatchUpload().getMaxUploadCount();
                for (int i =0;i < sheet.getRows() - 1;i++) {
                    row = readLineXls(sheet, rowNum++, head_column.length, true);
                    if (UploadUtils.isRowEmpty(row)) {
                        continue;
                    }
                
                    //if number of records more than row limit,throw a message.
                    if (rowNum > rowLimit) {
                        throw new cwSysMessage(UploadUtils.SMSG_ULG_UPLOAD_COUNT_EXCEED_LIMIT, new Integer(rowLimit).toString());
                    }
                    extendQue parsedQue = new extendQue(1, 1, this.optionNum, 0, 0);
                    switch ( populate(parsedQue, row, allow_upd, que_type, wizbini.cfgTcEnabled) ) {
                        case 0:
                            try{
                                if (parsedQue.obj_id[0] > 0) {
                                    parsedQue.queType = que_type;
                                } else {
                                    parsedQue.queType = dbQuestion.QUE_TYPE_MULTI;
                                }
                                
                                int int_count = 0;
                                if (parsedQue.obj_id[0] == 0) {
                                    int_count = 1;
                                }
                                
                                RawQuestion.uploadQue(con, parsedQue, ulg_id, rowNum, int_count);
                            }catch(Exception e){
                            	CommonLog.error(e.getMessage(),e);
                            }
                            break;
                        case ROW_INVALID_CATEGORY_ID:
                            errorFlag = true;
                            invalidCategoryIdVec.addElement(new Integer(rowNum));
                            break;
                        case ROW_INVALID_OPTION:
                            errorFlag = true;
                            invalidOptionVec.addElement(new Integer(rowNum));
                            break;
                        case ROW_INVALID_SYSTEM_ID:
                            errorFlag = true;
                            invalidResourceIdVec.addElement(new Integer(rowNum));
                            break;
                        case ROW_INVALID_ANSWER:
                            errorFlag = true;
                            invalidAnswer.addElement(new Integer(rowNum));
                            break;
                        case ROW_INVALID_SHUFFLE:
                            errorFlag = true;
                            invalidShuffle.addElement(new Integer(rowNum));
                            break;
                        case ROW_INVALID_ASHTML:
                            errorFlag = true;
                            invalidAshtml.addElement(new Integer(rowNum));
                            break;
                        case ROW_INVALID_SCORE:
                            errorFlag = true;
                            invalidScore.addElement(new Integer(rowNum));
                            break;
                        case ROW_INVALID_DIFFICULTY:
                            errorFlag = true;
                            invalidDifficulty.addElement(new Integer(rowNum));
                            break;
                        case ROW_INVALID_STATUS:
                            errorFlag = true;
                            invalidStatus.addElement(new Integer(rowNum));
                            break;
                        case ROW_INVALID_TITLE:
                            errorFlag = true;
                            invalidTitle.addElement(new Integer(rowNum));
                            break;
                        case ROW_INVALID_CONTENT:
                            errorFlag = true;
                            invalidContent.addElement(new Integer(rowNum));
                            break;
                        case ROW_INVALID_DESC:
                            errorFlag = true;
                            invalidDesc.addElement(new Integer(rowNum));
                            break;
                        case ROW_INVALID_SC_SUB_SHUFFLE:
                            errorFlag = true;
                            invalidScSubShuffle.addElement(new Integer(rowNum));
                            break;
                        case ROW_INVALID_CRITERIA:
                            errorFlag = true;
                            invalidCriteria.addElement(new Integer(rowNum));
                            break;
                        case ROW_INVALID_PERMISSION:
                            errorFlag = true;
                            invalidpermission.addElement(new Integer(rowNum));
                            break;
                        case ROW_INVALID_SPEC:
                            errorFlag = true;
                            invalidSpec.addElement(new Integer(rowNum));
                            break;
                        default:
                            errorFlag = true;
                            invalidColumn.addElement(new Integer(rowNum));
                            break;
                    }
                }
            }
            
            
            if( errorFlag ) {
                con.rollback();
                return invalidRowXML();
            } else {
                return uploadedQueXML(ulg_id);
            }
        }


    private int populate(extendQue que, String[] row, boolean allow_upd, String que_type, boolean tc_enabled) throws SQLException {
        
        int col = 0;
        int[] ans = null;
        int queOptionNum = 0;
        String asHTML = UploadUtils.NO;
        int que_score = 0;
        long usr_ent_id = 0;
        try {
            usr_ent_id = dbRegUser.getEntId(con, usr_id);
        } catch (qdbException e) {
            throw new SQLException();
        }
        for(int i=0; i<row.length; i++){
            String colName = (String)inColHeader.elementAt(col);
            if(colName.equalsIgnoreCase(StdColName[0]) ) { //Category ID
                try{
                    String tempObjId = row[i];
                    if(tempObjId!= null && tempObjId.length() > 0) {
                        if (!categoryIdExist(Long.parseLong(row[i]))) {
                            return ROW_INVALID_CATEGORY_ID;
                        }
                        que.obj_id[0] = Long.parseLong(row[i]);
                        isLastScEmpty = false;
                        if(tc_enabled) {
                        	AcTrainingCenter acTc = new AcTrainingCenter(con);
                        	if(!acTc.hasObjInMgtTc(usr_ent_id, que.obj_id[0], cur_role).equals(dbObjective.CAN_MGT_OBJ)) {
                        		return ROW_INVALID_PERMISSION;
                        	}
                        }
                        AcResources acRes = new AcResources(con);
                        if (!acRes.hasManagePrivilege(usr_ent_id, que.obj_id[0], cur_role)) {
                            return ROW_INVALID_PERMISSION;
                        }
                    } else {
                        que.obj_id[0] = 0;
                    }
                }catch(Exception e){
                    return ROW_INVALID_CATEGORY_ID;
                }
            } else if ( colName.equalsIgnoreCase(StdColName[1]) ) { //resource_id
                try {
                    String tempToken = row[i];
                    if (tempToken!=null && tempToken.length() > 0) {
                        que.resource_id = Long.parseLong(row[i]);
                        //if system id is under 0
                        if (que.resource_id <= 0) {
                            return ROW_INVALID_SYSTEM_ID;
                        }
                        //sub_que
                        if (que.obj_id[0] == 0) {
                            if (isLastScEmpty) {
                                return ROW_INVALID_CATEGORY_ID;
                            }
                            //check if this que id exist as a sc_sub_que id
                            que_type = dbQuestion.QUE_TYPE_MULTI;
                            if ((!dbQuestion.subQueIDExist(con, que.resource_id, que_type) || !allow_upd) 
                                && que.resource_id > 0) {
                                return ROW_INVALID_SYSTEM_ID;
                            }
                            if( parent_sc_que_id > 0 ) {
                                //check if this sub_que was in cur_sc_que
                                dbQuestion dbque = new dbQuestion();
                                dbque.que_res_id = que.resource_id;
                                if (que.resource_id > 0 && !dbque.isSameParent(con, parent_sc_que_id)) {
                                    return ROW_INVALID_SYSTEM_ID;
                                }
                            } else {
                                //if sc_que id was empty,sub_que id must be empty too.
                                if (que.resource_id > 0) {
                                    return ROW_INVALID_SYSTEM_ID;
                                }
                            }
                        } else if (que.obj_id[0] > 0 ) {
                            parent_sc_que_id = que.resource_id;
                        }
                        //check if have right to modify this question
                        long permission_que_id = 0;
                        if (que.obj_id[0] == 0) {
                            permission_que_id = parent_sc_que_id;
                        } else {
                            permission_que_id = que.resource_id;
                        }
                      	if(tc_enabled) {
             	        	AcTrainingCenter acTc = new AcTrainingCenter(con);
             	        	if(!acTc.hasResInMgtTc(usr_ent_id, permission_que_id, cur_role).equals(dbResource.CAN_MGT_RES)) {
             	        		return ROW_INVALID_PERMISSION;
             	        	}
                      	}
                        AcResources acRes = new AcResources(con);
                        if (!acRes.hasResPrivilege(usr_ent_id, permission_que_id, cur_role)) {
                            return ROW_INVALID_PERMISSION;
                        }
                        //check if there are more than one question have the same resource_id.
                        Long temp_que_id_obj = new Long(que.resource_id);
                        if (dupQueId.contains(temp_que_id_obj)) {
                            return ROW_INVALID_SYSTEM_ID;
                        } else {
                            dupQueId.addElement(new Long(que.resource_id));
                        }
                    } else {
                        que.resource_id = 0;
                        if (que.obj_id[0] > 0) {
                            parent_sc_que_id = 0;
                        } else if (isLastScEmpty) {
                            return ROW_INVALID_CATEGORY_ID;
                        }
                    }
                } catch (NumberFormatException e) {
                	CommonLog.error(e.getMessage(),e);
                    return ROW_INVALID_SYSTEM_ID;
                }

            } else if( colName.equalsIgnoreCase(StdColName[2]) ) { // Title
                que.title = UploadUtils.escString(row[i]);
                if( que.title == null || que.title.length() == 0 || CharUtils.getStringLength(que.title) > 80 || UploadUtils.containsNewLine(que.title))
                    return ROW_INVALID_TITLE;

            } else if( colName.equalsIgnoreCase(StdColName[3]) ) { // Question
                que.cont = cwUtils.esc4XML(UploadUtils.escString(row[i]));
                if( que.cont == null || que.cont.length() == 0)
                    return ROW_INVALID_CONTENT;

            } else if( colName.equalsIgnoreCase(StdColName[4]) ) { // AsHTML
                try{
                    asHTML = UploadUtils.checkYesNo(row[i]);
                }catch(cwException e){
                    return ROW_INVALID_ASHTML;
                }
                
            } else if( colName.equalsIgnoreCase(StdColName[5]) ) { // Difficulty
                try{
                    if (que.obj_id[0] == 0 || row[i].length() == 0) {
                        que.diff = 2;
                    } else {
                        que.diff = Integer.parseInt( row[i] );
                    }
                    if( que.diff < 1 || que.diff > 3){
                        return ROW_INVALID_DIFFICULTY;
                    }
                    if (que.obj_id[0] > 0 && row[i].length() == 0) {
                        return ROW_INVALID_DIFFICULTY;
                    }
                }catch(Exception e){
                    return ROW_INVALID_DIFFICULTY;
                }

            } else if( colName.equalsIgnoreCase(StdColName[6]) ) { // Status
                try{
                    if (que.obj_id[0] > 0 && row[i].length() == 0) {
                        return ROW_INVALID_STATUS;
                    }
                    if (que.obj_id[0] == 0 || row[i].length() == 0) {
                        que.onoff = "ON";
                    } else {
                        que.onoff = UploadUtils.checkOnOff( row[i] );
                    }
                }catch(Exception e){
                    return ROW_INVALID_STATUS;
                }
            } else if( colName.equalsIgnoreCase(StdColName[7]) ) { // Description
                que.desc = UploadUtils.escString(row[i]);
                if( que.desc.length() > 1000 ) {
                    return ROW_INVALID_DESC;
                }
            } else if( colName.equalsIgnoreCase(StdColName[9]) ) { // Shuffle option
                try{
                    if (que.obj_id[0] == 0) {
                        if (row[i].length() > 0) {
                            que.inter[0].shuffle = UploadUtils.checkYesNo(row[i]);
                        } else {
                            return ROW_INVALID_SHUFFLE;
                        }
                    }
                }catch(cwException e){
                    return ROW_INVALID_SHUFFLE;
                }
                
            } else if( colName.equalsIgnoreCase(StdColName[10])) { // Answer
                try{
                    if (row[i].length() > 0 && que.obj_id[0] == 0) {
                        ans = UploadUtils.string2IntArray(UploadUtils.escString(row[i]), UploadUtils.COMMA_DELIMITER);
                    }
                }catch(Exception e){
                    return ROW_INVALID_ANSWER;
                }
                if( (ans == null || ans.length == 0) && que.obj_id[0] == 0 )
                    return ROW_INVALID_ANSWER;
                
            } else if( colName.equalsIgnoreCase(StdColName[11]) ) { // Score
                try{
                    if (que.obj_id[0] == 0) {
                        if (row[i].length() == 0 ) {
                            return ROW_INVALID_SCORE;
                        } else {
                            que_score = Integer.parseInt(row[i]);
                            if( que_score < 1 || que_score > 100 ){
                                return ROW_INVALID_SCORE;
                            }
                        }
                    } else if (row[i].length() > 0) {
                        return ROW_INVALID_SCORE;
                    }
                }catch(Exception e){
                    return ROW_INVALID_SCORE;
                }
                
            } else if( colName.equalsIgnoreCase(StdColName[12]) ) { // criteria
                if (que_type.equals(dbQuestion.RES_SUBTYPE_DSC) && que.obj_id[0] != 0) {
                    que.sc_criteria = cwUtils.esc4XML(UploadUtils.escString(row[i]));
                    if (que.sc_criteria != null && que.sc_criteria.length() > 0) {
                        if (que.sc_criteria.length() > 500) {
                            return ROW_INVALID_SPEC;
                        }
                        StringTokenizer st = new StringTokenizer(que.sc_criteria, ",");
                        Vector dup_score_vec = new Vector();
                        while (st.hasMoreTokens()) {
                            String tempSpec = st.nextToken();
                            if (tempSpec.length() < 4) {
                                return ROW_INVALID_SPEC;
                            }
                            Long score;
                            Long score_count;
                            int pos_end;
                            int pos_end_1;
                            try {
                                pos_end = tempSpec.indexOf("(");
                                score = new Long(tempSpec.substring(0, tempSpec.indexOf("(")));
                                pos_end_1 = tempSpec.indexOf(")");
                                score_count = new Long(tempSpec.substring(pos_end + 1, pos_end_1));
                                if (tempSpec.length() > pos_end_1 + 1) {
                                    return ROW_INVALID_SPEC;
                                }
                            } catch (Exception e) {
                                return ROW_INVALID_SPEC;
                            }
                            if (dup_score_vec.contains(score)) {
                            }
                            dup_score_vec.addElement(score);
                        }
                    }
                } else if (que_type.equals(dbQuestion.RES_SUBTYPE_FSC) && que.obj_id[0] != 0) {
                    return ROW_INVALID_SPEC;
                }
            } else if( colName.startsWith(StdOptionColName) ) { //Option
                int optNum = Integer.parseInt( (colName.substring(StdOptionColName.length(), colName.length())).trim() );                   
                try{
                    if( row[i] != null && row[i].length() > 0 ) {
                        que.inter[0].opt[optNum-1].cont = cwUtils.esc4XML(UploadUtils.escString(row[i]));
                        if( UploadUtils.containsNewLine(que.inter[0].opt[optNum-1].cont) ) {
                            throw new Exception("Option with new line");
                        }
                        queOptionNum++;
                    }
                }catch(Exception e){
                	CommonLog.error(e.getMessage(),e);
                    return ROW_INVALID_OPTION;
                }
            } else {
                return -1;
            }
            col++;
        }
        
        if( queOptionNum < 2 && que.obj_id[0] == 0)
            return ROW_INVALID_OPTION;
            
        for(int i=queOptionNum; i<optionNum; i++){
            if( que.inter[0].opt[i].cont != null ){
                return ROW_INVALID_OPTION;
            }
        }
        
        if (que.obj_id[0] == 0) {
            for(int i=0; i<ans.length; i++){
                if( ans[i] > queOptionNum )
                    return ROW_INVALID_ANSWER;
                else {
                    que.inter[0].opt[ans[i] - 1].score = que_score;
                }
            }
        }
        
        if( asHTML.equalsIgnoreCase(UploadUtils.YES) ){
            que.cont_html = true;
            for(int i=0; i<queOptionNum; i++)
                que.inter[0].opt[i].cont_html = true;
        } else {
            que.cont_html = false;
            for(int i=0; i<queOptionNum; i++)
                que.inter[0].opt[i].cont_html = false;
        }
        
        
        que.lan = DEFAULT_LANGUAGE;
        que.folder = DEFAULT_FOLDER;
        if (que.obj_id[0] == 0) {
            que.inter[0].type = dbQuestion.QUE_TYPE_MULTI;
            if( ans.length == 1 )
                que.inter[0].logic = extendQue.MC_LOGIC_SINGLE;
            else 
                que.inter[0].logic = extendQue.MC_LOGIC_AND;
        } else {
            que.inter[0].type = que_type;
        }
        que.owner_id = this.usr_id;
        return 0;

    }

    
    private boolean categoryIdExist(long id)
        throws SQLException {
            if( cachedValidCategoryIdVec.indexOf(new Long(id)) != -1 )
                return true;
            else if( cachedInvalidCategoryIdVec.indexOf(new Long(id)) != -1 ) 
                return false;
            else {
                if( UploadUtils.validObjective(this.con, this.site_id, id) ){
                    cachedValidCategoryIdVec.addElement(new Long(id));
                    return true;
                } else {
                    cachedInvalidCategoryIdVec.addElement(new Long(id));
                    return false;
                }
            }
        }
    
        

    private String invalidRowXML(){
        StringBuffer xml = new StringBuffer(512);
        xml.append("<invalid_list>");

        if( !invalidCategoryIdVec.isEmpty() ){
            xml.append("<invalid_category_lines>");
            for(int i=0; i<invalidCategoryIdVec.size(); i++)
                xml.append("<line num=\"").append(invalidCategoryIdVec.elementAt(i)).append("\"/>");
             xml.append("</invalid_category_lines>");
        }
        if( !invalidResourceIdVec.isEmpty() ){
            xml.append("<invalid_resource_lines>");
            for(int i=0; i<invalidResourceIdVec.size(); i++)
                xml.append("<line num=\"").append(invalidResourceIdVec.elementAt(i)).append("\"/>");
             xml.append("</invalid_resource_lines>");
        }
        if( !invalidOptionVec.isEmpty() ){
            xml.append("<invalid_option_lines>");
            for(int i=0; i<invalidOptionVec.size(); i++)
                xml.append("<line num=\"").append(invalidOptionVec.elementAt(i)).append("\"/>");
             xml.append("</invalid_option_lines>");
        }
        if( !invalidAnswer.isEmpty() ){
            xml.append("<invalid_answer_lines>");
            for(int i=0; i<invalidAnswer.size(); i++)
                xml.append("<line num=\"").append(invalidAnswer.elementAt(i)).append("\"/>");
             xml.append("</invalid_answer_lines>");
        }
        if( !invalidShuffle.isEmpty() ){
            xml.append("<invalid_shuffle_lines>");
            for(int i=0; i<invalidShuffle.size(); i++)
                xml.append("<line num=\"").append(invalidShuffle.elementAt(i)).append("\"/>");
             xml.append("</invalid_shuffle_lines>");
        }
        if( !invalidAshtml.isEmpty() ){
            xml.append("<invalid_ashtml_lines>");
            for(int i=0; i<invalidAshtml.size(); i++)
                xml.append("<line num=\"").append(invalidAshtml.elementAt(i)).append("\"/>");
             xml.append("</invalid_ashtml_lines>");
        }
        if( !invalidScore.isEmpty() ){
            xml.append("<invalid_score_lines>");
            for(int i=0; i<invalidScore.size(); i++)
                xml.append("<line num=\"").append(invalidScore.elementAt(i)).append("\"/>");
             xml.append("</invalid_score_lines>");
        }
        if( !invalidDifficulty.isEmpty() ){
            xml.append("<invalid_difficulty_lines>");
            for(int i=0; i<invalidDifficulty.size(); i++)
                xml.append("<line num=\"").append(invalidDifficulty.elementAt(i)).append("\"/>");
             xml.append("</invalid_difficulty_lines>");
        }
        if( !invalidStatus.isEmpty() ){
            xml.append("<invalid_status_lines>");
            for(int i=0; i<invalidStatus.size(); i++)
                xml.append("<line num=\"").append(invalidStatus.elementAt(i)).append("\"/>");
             xml.append("</invalid_status_lines>");
        }
        if( !invalidColumn.isEmpty() ){
            xml.append("<invalid_column_lines>");
            for(int i=0; i<invalidColumn.size(); i++)
                xml.append("<line num=\"").append(invalidColumn.elementAt(i)).append("\"/>");
             xml.append("</invalid_column_lines>");
        }
        if( !invalidTitle.isEmpty() ){
            xml.append("<invalid_title_lines>");
            for(int i=0; i<invalidTitle.size(); i++)
                xml.append("<line num=\"").append(invalidTitle.elementAt(i)).append("\"/>");
             xml.append("</invalid_title_lines>");
        }
        if( !invalidContent.isEmpty() ){
            xml.append("<invalid_content_lines>");
            for(int i=0; i<invalidContent.size(); i++)
                xml.append("<line num=\"").append(invalidContent.elementAt(i)).append("\"/>");
             xml.append("</invalid_content_lines>");
        }
        if( !invalidDesc.isEmpty() ){
            xml.append("<invalid_desc_lines>");
            for(int i=0; i<invalidDesc.size(); i++)
                xml.append("<line num=\"").append(invalidDesc.elementAt(i)).append("\"/>");
             xml.append("</invalid_desc_lines>");
        }
        if( !invalidScSubShuffle.isEmpty() ){
            xml.append("<invalid_sc_shuffle_lines>");
            for(int i=0; i<invalidScSubShuffle.size(); i++)
                xml.append("<line num=\"").append(invalidScSubShuffle.elementAt(i)).append("\"/>");
             xml.append("</invalid_sc_shuffle_lines>");
        }
        if( !invalidCriteria.isEmpty() ){
            xml.append("<invalid_criteria_lines>");
            for(int i=0; i<invalidCriteria.size(); i++)
                xml.append("<line num=\"").append(invalidCriteria.elementAt(i)).append("\"/>");
             xml.append("</invalid_criteria_lines>");
        }
        if( !invalidpermission.isEmpty() ){
            xml.append("<invalid_permission_lines>");
            for(int i=0; i<invalidpermission.size(); i++)
                xml.append("<line num=\"").append(invalidpermission.elementAt(i)).append("\"/>");
             xml.append("</invalid_permission_lines>");
        }
        if( !invalidSpec.isEmpty() ){
            xml.append("<invalid_spec_lines>");
            for(int i=0; i<invalidSpec.size(); i++)
                xml.append("<line num=\"").append(invalidSpec.elementAt(i)).append("\"/>");
             xml.append("</invalid_spec_lines>");
        }
        if (invalidXlsFile) {
            xml.append("<invalid_xls_sheet>");
            xml.append("<line num=\"0\"/>");
            xml.append("</invalid_xls_sheet>");
        }
        xml.append("</invalid_list>");
        return xml.toString();
    }
        
    private String uploadedQueXML(long ulg_id) throws cwSysMessage, SQLException{
        StringBuffer xml = new StringBuffer();
        xml.append("<used_column>");
        for (int k = 0; k < this.inColHeader.size(); k++){
            String exclusion = (String)this.inColHeader.elementAt(k);
            if (!exclusion.startsWith(StdOptionColName) 
                && !exclusion.equals(StdColName[9])
                && !exclusion.equals(StdColName[10])
                && !exclusion.equals(StdColName[11])) {
                xml.append("<column id=\"" + k + "\">").append(exclusion).append("</column>");
            }
        }      
        xml.append("</used_column>");
        xml.append(RawQuestion.asXML(con, ulg_id));
        return xml.toString();
    }
    
    private Vector parseMissingColHeader() {
        Vector missColVec = new Vector();
        for(int i=0; i<REQUIRED_COLUMN.length; i++){
            if( this.inColHeader.indexOf(REQUIRED_COLUMN[i]) == -1 ) {
                missColVec.addElement(REQUIRED_COLUMN[i]);
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
    
    private Vector parseInValidColHeader(String[] head_column)
        throws cwException, IOException {
            Vector invalidColVec = new Vector();
nextCol:
            for(int i=0; i<head_column.length; i++){
                for(int j=0; j<StdColName.length; j++)
                    if( head_column[i].equalsIgnoreCase(StdColName[j]) ){
                        this.inColHeader.addElement(head_column[i]);
                        continue nextCol;
                    }
                if( head_column[i].startsWith(StdOptionColName) ){
                    boolean flag = true;
                    try{
                        Integer.parseInt( (head_column[i].substring(StdOptionColName.length(), head_column[i].length())).trim() );
                    }catch(NumberFormatException e){
                        flag = false;
                    }
                    if( flag ) {
                        this.inColHeader.addElement(head_column[i]);
                        this.optionNum++;
                        continue nextCol;
                    }
                }
                invalidColVec.addElement(head_column[i]);
            }
            
            //Vector existedOptionNum = new Vector();
            for(int i=0; i<this.inColHeader.size(); i++){
                String colName = (String)this.inColHeader.elementAt(i);
                if( (colName).startsWith(StdOptionColName) ){
                    int optNum = Integer.parseInt( (colName.substring(StdOptionColName.length(), colName.length())).trim() );
                    if( optNum > optionNum || optNum > MAX_OPTION_NUM) {
                        invalidColVec.addElement(colName);
                    }/* else if( existedOptionNum.indexOf(new Integer(optNum)) != -1 ) {
                        invalidColVec.addElement(colName);
                    } else {
                        existedOptionNum.addElement(new Integer(optNum));
                    }*/
                }
            }
            return invalidColVec;               
        }


    public String save2DB(DbUploadLog ulg, loginProfile prof, String que_type, boolean tc_enabled)
        throws cwException, SQLException, IOException
    {
        
        RawQuestion rawQue = new RawQuestion();
        Hashtable h_cnt = rawQue.save2DB(this.con, this.static_env, this.site_id, ulg.ulg_id, prof, tc_enabled);
        ulg.ulg_status = DbUploadLog.STATUS_COMPLETED;
        ulg.ulg_upd_datetime = cwSQL.getTime(this.con);
        ulg.updStatus(this.con);
        DbRawQuestion.del(this.con, ulg.ulg_id);
        
        return UploadUtils.getResultXML(static_env, ulg.ulg_id, h_cnt);
        
    }

    private String[] getLabelArray(loginProfile prof) {
		String foldId = "Folder ID", resourceID = "Resource ID", title = "Title", question = "Question";
		String asHtml = "AsHTML";
		String difficulty = "Difficulty", status = "Status";
		String description = "Description", option = "Option", shuffleOption = "Shuffle Option", answer = "Answer";
		String score = "Score", criteria = "Criteria";

		String lang = prof.cur_lan;
		foldId = ImportTemplate.getLabelValue("wb_imp_tem_folder_id", lang, foldId).trim();
		resourceID = ImportTemplate.getLabelValue("wb_imp_tem_resource_id", lang, resourceID).trim();
		title = ImportTemplate.getLabelValue("wb_imp_tem_title", lang, title).trim();
		question = ImportTemplate.getLabelValue("wb_imp_tem_question", lang, question).trim();
		asHtml = ImportTemplate.getLabelValue("wb_imp_tem_as_html", lang, asHtml).trim();
		difficulty = ImportTemplate.getLabelValue("wb_imp_tem_difficulty", lang, difficulty).trim();
		status = ImportTemplate.getLabelValue("wb_imp_tem_status", lang, status).trim();
		description = ImportTemplate.getLabelValue("wb_imp_tem_description", lang, description).trim();
		option = ImportTemplate.getLabelValue("wb_imp_tem_option", lang, option).trim() + " ";
		shuffleOption = ImportTemplate.getLabelValue("wb_imp_tem_shuffle_option", lang, shuffleOption).trim();
		answer = ImportTemplate.getLabelValue("wb_imp_tem_answer", lang, answer).trim();
		score = ImportTemplate.getLabelValue("wb_imp_tem_score", lang, score).trim();
		criteria = ImportTemplate.getLabelValue("wb_imp_tem_criteria", lang, criteria).trim();

		String[] labels = new String[] { foldId, resourceID, title, question, asHtml, difficulty, status, description, option,
				shuffleOption, answer, score, criteria };
		return labels;
	}

	private String[] getRequiredLabelArray(loginProfile prof) {
		String foldId = "Folder ID", title = "Title", question = "Question";
		String asHtml = "AsHTML";
		String difficulty = "Difficulty", status = "Status";
		String option1 = "Option 1", option2 = "Option 2", shuffleOption = "Shuffle Option", answer = "Answer";
		String score = "Score";

		String lang = prof.cur_lan;
		foldId = ImportTemplate.getLabelValue("wb_imp_tem_folder_id", lang, foldId).trim();
		title = ImportTemplate.getLabelValue("wb_imp_tem_title", lang, title).trim();
		question = ImportTemplate.getLabelValue("wb_imp_tem_question", lang, question).trim();
		asHtml = ImportTemplate.getLabelValue("wb_imp_tem_as_html", lang, asHtml).trim();
		difficulty = ImportTemplate.getLabelValue("wb_imp_tem_difficulty", lang, difficulty).trim();
		status = ImportTemplate.getLabelValue("wb_imp_tem_status", lang, status).trim();
		option1 = ImportTemplate.getLabelValue("wb_imp_tem_option_1", lang, option1).trim();
		option2 = ImportTemplate.getLabelValue("wb_imp_tem_option_2", lang, option2).trim();
		shuffleOption = ImportTemplate.getLabelValue("wb_imp_tem_shuffle_option", lang, shuffleOption).trim();
		answer = ImportTemplate.getLabelValue("wb_imp_tem_answer", lang, answer).trim();
		score = ImportTemplate.getLabelValue("wb_imp_tem_score", lang, score).trim();

		String[] labels = new String[] { foldId, title, question, asHtml, difficulty, status, option1, option2, shuffleOption, answer, score };
		return labels;
	}
	
	private void setOtherLabel(loginProfile prof) {
		String option = "Option ";

		String lang = prof.cur_lan;
		option = ImportTemplate.getLabelValue("wb_imp_tem_option", lang, option).trim() + " ";

		this.StdOptionColName = option;
	}
    
        
}