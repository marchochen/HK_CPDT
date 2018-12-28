package com.cw.wizbank.upload;

import java.io.*;
import java.sql.*;
import java.util.*;

import com.cw.wizbank.qdb.*;
import com.cw.wizbank.accesscontrol.AcResources;
import com.cw.wizbank.accesscontrol.AcTrainingCenter;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.db.DbUploadLog;
import com.cw.wizbank.db.DbRawQuestion;
import com.cw.wizbank.util.*;
import com.cwn.wizbank.utils.CharUtils;
import com.cwn.wizbank.utils.CommonLog;

public class TFQue{
    
    //Log filename
    public final static String[] FILENAME_LIST = { UploadUtils.SUCCESS_FILE, UploadUtils.FAILURE_FILE, UploadUtils.ERROR_FILE};
    public final static String[] LOG_LIST = {"SUCCESS", "UNSUCCESS", "ERROR" };
    
    //History Session Key
    private static final String SESS_LOG_HISTORY_LOG_ID = "SESS_LOG_HISTORY_LOG_ID";
    private static final String SESS_LOG_HISTORY_TIMESTAMP = "SESS_LOG_HISTORY_TIMESTAMP";
    private static final String SESS_LOG_HISTORY_TOTAL_REC = "SESS_LOG_HISTORY_TOTAL_REC";
    private static final String SESS_LOG_HISTORY_TOTAL_PAGE = "SESS_LOG_HISTORY_TOTAL_PAGE";
        
	private static final String ANSWER_TRUE = "True";
	private static final String ANSWER_FALSE = "False";    
    
    private final static int ROW_INVALID_CATEGORY_ID = 1;
    private final static int ROW_INVALID_ANSWER = 2;
    private final static int ROW_INVALID_ASHTML = 3;
    private final static int ROW_INVALID_SCORE = 4;
    private final static int ROW_INVALID_DIFFICULTY = 5;
    private final static int ROW_INVALID_STATUS = 6;
    private final static int ROW_INVALID_TITLE = 7;
    private final static int ROW_INVALID_CONTENT = 8;
    private final static int ROW_INVALID_DESC = 9;
    private final static int ROW_INVALID_RESOURCE_ID = 10;
    private final static int ROW_INVALID_PERMISSION = 11;
    
    private Vector invalidCategoryIdVec = null;
    private Vector invalidAnswer = null;
    private Vector invalidAshtml = null;
    private Vector invalidScore = null;
    private Vector invalidDifficulty = null;
    private Vector invalidStatus = null;
    private Vector invalidColumn = null;
    private Vector invalidTitle = null;
    private Vector invalidContent = null;
    private Vector invalidDesc = null;
    private Vector invalidResourceId = null;
    private Vector invalidPermission = null;
    private Vector dupQueId = null;
    
    private Connection con = null;
    private long site_id;
    private String cur_role;
    private String usr_id = null;
    private qdbEnv static_env = null;
    
    private int optionNum = 2;

    private Vector cachedValidCategoryIdVec = null;
    private Vector cachedInvalidCategoryIdVec = null;

    private String[] StdColName = {
        "Folder ID",
        "Title",
        "Question",
        "Answer",
        "AsHTML",
        "Score",
        "Difficulty",
        "Status",
        "Description",
        "Explanation",
        "Resource ID"
    };

	private String[] REQUIRED_COLUMN = {
		"Folder ID",
		"Title",
		"Question",
		"Answer",
		"AsHTML",
		"Score",
		"Difficulty",
		"Status"
	};
    
	//UnicodeLittle:excel unicode encoding
    private final static String DEFAULT_ENC = "UnicodeLittle";

    private final static String DEFAULT_LANGUAGE = "ISO-8859-1";
    private String DEFAULT_FOLDER = "CW";

    private Vector inColHeader = null;
    
    public TFQue(Connection con, qdbEnv static_env, loginProfile prof){
    	// set label
    	this.StdColName = this.getLabelArray(prof);
        this.REQUIRED_COLUMN = this.getRequiredLabelArray(prof);
        
        this.con = con;
        this.site_id = prof.root_ent_id;
        this.usr_id = prof.usr_id;
        this.cur_role = prof.current_role;
        this.static_env = static_env;
        this.inColHeader = new Vector();

        invalidCategoryIdVec = new Vector();
        invalidAnswer = new Vector();
        invalidAshtml = new Vector();
        invalidScore = new Vector();
        invalidDifficulty = new Vector();
        invalidStatus = new Vector();
        invalidColumn = new Vector();
        invalidTitle = new Vector();
        invalidContent = new Vector();
        invalidDesc = new Vector();
        invalidResourceId = new Vector();
        invalidPermission = new Vector();
        
        cachedValidCategoryIdVec = new Vector();
        cachedInvalidCategoryIdVec = new Vector();
        
        dupQueId = new Vector();
        return;
    }
    
    
    public String uploadQue(File srcFile, String ulg_desc, boolean allow_upd, WizbiniLoader wizbini)
        throws cwException, IOException, cwSysMessage, SQLException {
			if( !UploadUtils.isUnicodeFile(srcFile) ){
				return UploadUtils.INVALID_ENCODING_XML;
			}
            long ulg_id = UploadUtils.insUploadLog(con, this.usr_id, ulg_desc, srcFile.getName(), static_env, dbQuestion.QUE_TYPE_TRUEFALSE);
            BufferedReader in = new BufferedLineReader(new InputStreamReader(new FileInputStream(srcFile), DEFAULT_ENC)); 
			String colHeader = in.readLine();
            if (colHeader == null) {
                in.close();
                throw new cwSysMessage(UploadUtils.SMSG_ULG_INVALID_FILE);
            }
            Vector invalidColHeader = parseInColHeader(colHeader);            
            if( !invalidColHeader.isEmpty() ){
                return UploadUtils.invalidColXML(invalidColHeader, UploadUtils.INVALID_COL_TYPE_UNRECOGNIZED);
            }
			Vector duplicateColHeader = parseDuplicateColHeader();
			if( !duplicateColHeader.isEmpty() ){
				return UploadUtils.invalidColXML(duplicateColHeader, UploadUtils.INVALID_COL_TYPE_DUPLICATE);
			}
			Vector missingColHeader = parseMissingColHeader();            
			if( !missingColHeader.isEmpty() ){
				return UploadUtils.invalidColXML(missingColHeader,UploadUtils.INVALID_COL_TYPE_MISSING);
			}
            String row = null;
            int rowNum = 1;
            boolean errorFlag = false;
            int rowLimit = wizbini.cfgSysSetupadv.getResBatchUpload().getMaxUploadCount();
            while ((row = in.readLine()) != null) {
				if( row.trim().length() == 0 ){
					continue;
				}
                
                //if number of records more than row limit,throw a message.
                if (rowNum > rowLimit + 1) {
                    throw new cwSysMessage(UploadUtils.SMSG_ULG_UPLOAD_COUNT_EXCEED_LIMIT, new Integer(rowLimit).toString());
                }

                rowNum++;
                extendQue parsedQue = new extendQue(1, 1, this.optionNum, 0, 0);
                if(row != null){
                	//由于excel文件有些现在还不知道的格式另存为txt文件后，会使格内容换行，所惟去掉
            		row = row.replaceAll(UploadUtils.colDelimiter_ + "\"" +UploadUtils.colDelimiter_ ,  UploadUtils.colDelimiter_  + "\"");
            	}
                switch ( populate(parsedQue, row, allow_upd, wizbini.cfgTcEnabled) ) {
                    case 0:
                        try{
                        	parsedQue.queType = dbQuestion.QUE_TYPE_TRUEFALSE;
                            RawQuestion.uploadQue(con, parsedQue, ulg_id, rowNum, 1);
                        }catch(Exception e){
                        	CommonLog.error(e.getMessage(),e);
                        }
                        break;
                    case ROW_INVALID_CATEGORY_ID:
                        errorFlag = true;
                        invalidCategoryIdVec.addElement(new Integer(rowNum));
                        break;
                    case ROW_INVALID_ANSWER:
                        errorFlag = true;
                        invalidAnswer.addElement(new Integer(rowNum));
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
                    case ROW_INVALID_RESOURCE_ID:
                        errorFlag = true;
                        invalidResourceId.addElement(new Integer(rowNum));
                        break;
                    case ROW_INVALID_PERMISSION:
                        errorFlag = true;
                        invalidPermission.addElement(new Integer(rowNum));
                        break;
                    default:
                        errorFlag = true;
                        invalidColumn.addElement(new Integer(rowNum));
                        break;
                }
            }
            
            if( errorFlag ) {
            	con.rollback();
                return invalidRowXML();
            } else {
                return uploadedQueXML(ulg_id);
            }
        }

    private int populate(extendQue que, String row, boolean allow_upd, boolean tc_enabled) throws SQLException{
        
        String[] tokens = cwUtils.splitToString(row, (new Character(UploadUtils.colDelimiter_)).toString());
        int col = 0;
        int ans = -1;
        String asHTML = UploadUtils.YES;
        int que_score = 0;
        
		int optNum = 2;
		que.inter[0].opt[0].cont = ANSWER_TRUE;
		que.inter[0].opt[1].cont = ANSWER_FALSE;

        long usr_ent_id = 0;
        try {
            usr_ent_id = dbRegUser.getEntId(con, usr_id);
        } catch (qdbException e) {
            throw new SQLException();
        }

        for(int i=0; i<tokens.length; i++){
            String colName = (String)inColHeader.elementAt(col);
            if( (colName).equalsIgnoreCase(StdColName[0]) ) { //Category ID
            	try{
	                if( categoryIdExist(Long.parseLong(tokens[i])) ) {
                        que.obj_id[0] = Long.parseLong(tokens[i]);
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
	                }
	                else
	                    return ROW_INVALID_CATEGORY_ID;
            	}catch(Exception e){
            		return ROW_INVALID_CATEGORY_ID;
            	}

            } else if( colName.equalsIgnoreCase(StdColName[1]) ) { // Title
                que.title = UploadUtils.escString(tokens[i]);
                if( que.title == null || que.title.length() == 0 || CharUtils.getStringLength(que.title) > 80 || UploadUtils.containsNewLine(que.title))
                    return ROW_INVALID_TITLE;

            } else if( colName.equalsIgnoreCase(StdColName[2]) ) { // Question
                que.cont = cwUtils.esc4XML(UploadUtils.escString(tokens[i]));
                if( que.cont == null || que.cont.length() == 0)
                    return ROW_INVALID_CONTENT;
                
            } else if( colName.equalsIgnoreCase(StdColName[3]) ) { // Answer
            	if(null != tokens[i] && tokens[i].length() > 0){
            		tokens[i] = tokens[i].trim();
            	}
                if( ANSWER_TRUE.equalsIgnoreCase(UploadUtils.escString(tokens[i])) ) {
					ans = 0;
                } else if( ANSWER_FALSE.equalsIgnoreCase(UploadUtils.escString(tokens[i])) ){
					ans = 1;
                } else {
					return ROW_INVALID_ANSWER;
                }
            } /*else if( colName.equalsIgnoreCase(StdColName[4]) ) { // AsHTML
                try{
                    asHTML = UploadUtils.checkYesNo(tokens[i]);
                }catch(cwException e){
                    return ROW_INVALID_ASHTML;
                }
                
            }*/ else if( colName.equalsIgnoreCase(StdColName[4]) ) { // Score
                try{
                    que_score = Integer.parseInt(tokens[i]);
                    if( que_score < 1 || que_score > 100 ){
                    	return ROW_INVALID_SCORE;
                    }
                }catch(Exception e){
                    return ROW_INVALID_SCORE;
                }
                
            } else if( colName.equalsIgnoreCase(StdColName[5]) ) { // Difficulty
                try{
                    que.diff = Integer.parseInt( tokens[i] );
                    if( que.diff < 1 || que.diff > 3){
						return ROW_INVALID_DIFFICULTY;
                    }
                }catch(Exception e){
                    return ROW_INVALID_DIFFICULTY;
                }

            } else if( colName.equalsIgnoreCase(StdColName[6]) ) { // Status
                try{
                    que.onoff = UploadUtils.checkOnOff( tokens[i] );
                }catch(Exception e){
                    return ROW_INVALID_STATUS;
                }
                
            } else if( colName.equalsIgnoreCase(StdColName[7]) ) { // Description
                que.desc = UploadUtils.escString(tokens[i]);
                if( que.desc.length() > 1000 ) {
                	return ROW_INVALID_DESC;
                }
            } else if( colName.equalsIgnoreCase(StdColName[8]) ) { // Explanation
               for (int j=0; j<optionNum; j++) {
                    que.inter[0].opt[j].exp = cwUtils.esc4XML(UploadUtils.escString(tokens[i]));
               }
            } else if ( colName.equalsIgnoreCase(StdColName[9]) ) { //system_id
                try {
                    String tempToken = tokens[i];
                    if (tempToken!=null && tempToken.length() > 0) {
                        que.resource_id = Long.parseLong(tokens[i]);
                        if (que.resource_id <= 0) {
                            return ROW_INVALID_RESOURCE_ID;
                        }
                        if ((!dbQuestion.queIDExist(con, que.resource_id, dbQuestion.QUE_TYPE_TRUEFALSE) || !allow_upd) 
                            && que.resource_id > 0) {
                            return ROW_INVALID_RESOURCE_ID;
                        }
                        //check if have right to modify this question
                      	if(tc_enabled) {
             	        	AcTrainingCenter acTc = new AcTrainingCenter(con);
             	        	if(!acTc.hasResInMgtTc(usr_ent_id, que.resource_id, cur_role).equals(dbResource.CAN_MGT_RES)) {
             	        		return ROW_INVALID_PERMISSION;	
             	        	}
                      	}
                        AcResources acRes = new AcResources(con);
                        if (!acRes.hasResPrivilege(usr_ent_id, que.resource_id, cur_role)) {
                            return ROW_INVALID_PERMISSION;
                        }
                        //check if there are more than one question have the same resource_id.
                        Long temp_que_id_obj = new Long(que.resource_id);
                        if (dupQueId.contains(temp_que_id_obj)) {
                            return ROW_INVALID_RESOURCE_ID;
                        } else {
                            dupQueId.addElement(new Long(que.resource_id));
                        }
                    } else {
                        que.resource_id = 0;
                    }
                } catch (NumberFormatException e) {
                	CommonLog.error(e.getMessage(),e);
                    return ROW_INVALID_RESOURCE_ID;
                }
                
            } else {
                return -1;
            }
            col++;
        }
        que.inter[0].opt[ans].score = que_score;
        
        if( asHTML.equalsIgnoreCase(UploadUtils.YES) ){
            que.cont_html = true;
            for(int i=0; i<optionNum; i++)
                que.inter[0].opt[i].cont_html = true;
        } else {
            que.cont_html = false;
            for(int i=0; i<optionNum; i++)
                que.inter[0].opt[i].cont_html = false;
        }
        
        
        que.lan = DEFAULT_LANGUAGE;
        que.folder = DEFAULT_FOLDER;
        que.inter[0].type = dbQuestion.QUE_TYPE_TRUEFALSE;
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
        if( !invalidResourceId.isEmpty() ){
            xml.append("<invalid_resource_lines>");
            for(int i=0; i<invalidResourceId.size(); i++)
                xml.append("<line num=\"").append(invalidResourceId.elementAt(i)).append("\"/>");
             xml.append("</invalid_resource_lines>");
        }
        if( !invalidAnswer.isEmpty() ){
            xml.append("<invalid_answer_lines>");
            for(int i=0; i<invalidAnswer.size(); i++)
                xml.append("<line num=\"").append(invalidAnswer.elementAt(i)).append("\"/>");
             xml.append("</invalid_answer_lines>");
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
        if( !invalidPermission.isEmpty() ){
            xml.append("<invalid_permission_lines>");
            for(int i=0; i<invalidPermission.size(); i++)
                xml.append("<line num=\"").append(invalidPermission.elementAt(i)).append("\"/>");
             xml.append("</invalid_permission_lines>");
        }
        xml.append("</invalid_list>");
        return xml.toString();
    }
        
    private String uploadedQueXML(long ulg_id) throws cwSysMessage, SQLException{
        StringBuffer xml = new StringBuffer();
        xml.append("<used_column>");
        for (int k = 0; k < this.inColHeader.size(); k++){
            xml.append("<column id=\"" + k + "\">").append(this.inColHeader.elementAt(k)).append("</column>");
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
    
    private Vector parseInColHeader(String colHeader)
        throws cwException, IOException {
            Vector invalidColVec = new Vector();
            String[] tokens = cwUtils.splitToString(colHeader, (new Character(UploadUtils.colDelimiter_)).toString());
nextCol:            
            for(int i=0; i<tokens.length; i++){
                for(int j=0; j<StdColName.length; j++)
                    if( tokens[i].equalsIgnoreCase(StdColName[j]) ){
                        this.inColHeader.addElement(tokens[i]);
                        continue nextCol;
                    }
                invalidColVec.addElement(tokens[i]);
            }
            return invalidColVec;
        }


    public String save2DB(DbUploadLog ulg, loginProfile prof, boolean tc_enabled)
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
		String foldId = "Folder ID", title = "Title", question = "Question";
		String answer = "Answer", score = "Score"; //, asHtml = "AsHTML"
		String difficulty = "Difficulty", status = "Status";
		String description = "Description", explanation = "Explanation", resourceID = "Resource ID";

		String lang = prof.cur_lan;  
		foldId = ImportTemplate.getLabelValue("wb_imp_tem_folder_id", lang, foldId).trim();
		title = ImportTemplate.getLabelValue("wb_imp_tem_title", lang, title).trim();
		question = ImportTemplate.getLabelValue("wb_imp_tem_question", lang, question).trim();
		answer = ImportTemplate.getLabelValue("wb_imp_tem_answer", lang, answer).trim();
//		asHtml = ImportTemplate.getLabelValue("wb_imp_tem_as_html", lang, asHtml).trim();
		score = ImportTemplate.getLabelValue("wb_imp_tem_score", lang, score).trim();
		difficulty = ImportTemplate.getLabelValue("wb_imp_tem_difficulty", lang, difficulty).trim();
		status = ImportTemplate.getLabelValue("wb_imp_tem_status", lang, status).trim();
		description = ImportTemplate.getLabelValue("wb_imp_tem_description", lang, description).trim();
//		explanation = ImportTemplate.getLabelValue("wb_imp_tem_explanation", lang, explanation).trim();
		resourceID = ImportTemplate.getLabelValue("wb_imp_tem_resource_id", lang, resourceID).trim();

		String[] labels = new String[] { foldId, title, question, answer, score,
				difficulty, status, description, explanation, resourceID};
		return labels;
	} 
    
    private String[] getRequiredLabelArray(loginProfile prof) {   	    	       	    
		String foldId = "Folder ID", title = "Title", question = "Question";
		String answer = "Answer", score = "Score"; //, asHtml = "AsHTML"
		String difficulty = "Difficulty", status = "Status";

		String lang = prof.cur_lan;  
		foldId = ImportTemplate.getLabelValue("wb_imp_tem_folder_id", lang, foldId).trim();
		title = ImportTemplate.getLabelValue("wb_imp_tem_title", lang, title).trim();
		question = ImportTemplate.getLabelValue("wb_imp_tem_question", lang, question).trim();
		answer = ImportTemplate.getLabelValue("wb_imp_tem_answer", lang, answer).trim();
//		asHtml = ImportTemplate.getLabelValue("wb_imp_tem_as_html", lang, asHtml).trim();
		score = ImportTemplate.getLabelValue("wb_imp_tem_score", lang, score).trim();
		difficulty = ImportTemplate.getLabelValue("wb_imp_tem_difficulty", lang, difficulty).trim();
		status = ImportTemplate.getLabelValue("wb_imp_tem_status", lang, status).trim();

		String[] labels = new String[] { foldId, title, question, answer, score,
				difficulty, status};
		return labels;
	}  

    
    
        
}