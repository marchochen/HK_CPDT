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

public class ESQue{
    
    //Log filename
    public final static String[] FILENAME_LIST = { UploadUtils.SUCCESS_FILE, UploadUtils.FAILURE_FILE, UploadUtils.ERROR_FILE};
    public final static String[] LOG_LIST = {"SUCCESS", "UNSUCCESS", "ERROR" };
    
    //History Session Key
    private static final String SESS_LOG_HISTORY_LOG_ID = "SESS_LOG_HISTORY_LOG_ID";
    private static final String SESS_LOG_HISTORY_TIMESTAMP = "SESS_LOG_HISTORY_TIMESTAMP";
    private static final String SESS_LOG_HISTORY_TOTAL_REC = "SESS_LOG_HISTORY_TOTAL_REC";
    private static final String SESS_LOG_HISTORY_TOTAL_PAGE = "SESS_LOG_HISTORY_TOTAL_PAGE";
            
    private static final int maxObjID_ = 1;
    private static final int maxOpt_   = 1;
    private static final int maxInter_ = 2;
    private static final int max_att_len   = 5000;
    
    private final static int ROW_INVALID_CATEGORY_ID = 1;
    private final static int ROW_INVALID_OPTION = 2;
    private final static int ROW_INVALID_ANSWER = 3;
    private final static int ROW_INVALID_ASHTML = 5;
    private final static int ROW_INVALID_SCORE = 6;
    private final static int ROW_INVALID_DIFFICULTY = 7;
    private final static int ROW_INVALID_DURATION = 8;
    private final static int ROW_INVALID_STATUS = 9;
    private final static int ROW_INVALID_TITLE = 10;
    private final static int ROW_INVALID_CONTENT = 11;
    private final static int ROW_EMPTY_BLANK = 12;
    private final static int ROW_INVALID_SUBMIT_FILE = 13;
    private final static int ROW_INVALID_RESOURCE_ID = 14;
    private final static int ROW_INVALID_PERMISSION = 15;
    private final static int ROW_INVALID_TITLE_TOO_LONG = 16;
	//private final static int ROW_INVALID_DESCRIPTION= 14;
    
    private Vector invalidCategoryIdVec = null;
    private Vector invalidOptionVec = null;
    private Vector invalidAnswer = null;
    private Vector invalidAshtml = null;
    private Vector invalidScore = null;
    private Vector invalidDifficulty = null;
    private Vector invalidDuration = null;
    private Vector invalidStatus = null;
    private Vector invalidColumn = null;
    private Vector invalidTitle = null;
    private Vector invalidTitleTooLong = null;
    private Vector invalidContent = null;
    private Vector invalidSubmitFile = null;
    private Vector invalidResource = null;
    private Vector invalidPermission = null;
    private Vector dupQueId = null;
    
    private Connection con = null;
    private long site_id;
    private String cur_role;
    private String usr_id = null;
    private qdbEnv static_env = null;
    
    private int blankNum = 0;

    private Vector cachedValidCategoryIdVec = null;
    private Vector cachedInvalidCategoryIdVec = null;

    private String[] StdColName = {
        "Folder ID",
        "Title",
        "Question",
        //"AsHTML",
        "Model Answer",
        "Score",
        "Difficulty",
        "Status",
        "Description",
        //"Submit File",
        "Resource ID"
    };

	private String[] RequiredStdColName = {
			"Folder ID",
			"Title",
			"Question",
			//"AsHTML",
			"Model Answer",
			"Score",
			"Difficulty",
			"Status"//,
			//"Submit File"
	};
	
	//UnicodeLittle:excel unicode encoding
    private final static String DEFAULT_ENC = "UnicodeLittle";

    private final static String DEFAULT_LANGUAGE = "ISO-8859-1";
    private String DEFAULT_FOLDER = "CW";

    private Vector inColHeader = null;
    
    public ESQue(Connection con, qdbEnv static_env, loginProfile prof){
    	// set label
        this.StdColName = this.getLabelArray(prof);
        this.RequiredStdColName = this.getRequiredLabelArray(prof);   	
    	
        this.con = con;
        this.site_id = prof.root_ent_id;
        this.usr_id = prof.usr_id;
        this.cur_role = prof.current_role;
        this.static_env = static_env;
        this.inColHeader = new Vector();

        invalidCategoryIdVec = new Vector();
        invalidOptionVec = new Vector();
        invalidAnswer = new Vector();
        invalidAshtml = new Vector();
        invalidScore = new Vector();
        invalidDifficulty = new Vector();
        invalidDuration = new Vector();
        invalidStatus = new Vector();
        invalidColumn = new Vector();
        invalidTitle = new Vector();
        invalidTitleTooLong = new Vector();
        invalidContent = new Vector();
        invalidSubmitFile = new Vector();
        invalidResource = new Vector();
        invalidPermission = new Vector();
        
        cachedValidCategoryIdVec = new Vector();
        cachedInvalidCategoryIdVec = new Vector();
        
        dupQueId = new Vector();
        return;
    }
    
	
	public String uploadQue(File srcFile, String ulg_desc, String que_type, boolean allow_upd, WizbiniLoader wizbini)
		throws cwException, IOException, cwSysMessage, SQLException {
			if( !UploadUtils.isUnicodeFile(srcFile) ){
				return UploadUtils.INVALID_ENCODING_XML;
			}            

            long ulg_id = UploadUtils.insUploadLog(con, this.usr_id, ulg_desc, srcFile.getName(), static_env, que_type);
            BufferedReader in = new BufferedLineReader(new InputStreamReader(new FileInputStream(srcFile), DEFAULT_ENC)); 
			String colHeader = in.readLine();
            if (colHeader == null) {
                in.close();
                throw new cwSysMessage(UploadUtils.SMSG_ULG_INVALID_FILE);
            }
            Vector invalidColHeader = parseInColHeader(colHeader);            
            if( !invalidColHeader.isEmpty() ){
                return UploadUtils.invalidColXML(invalidColHeader);
            }
            
            // Check for duplicated fields
			Vector vDuplicatedColHeader = getDuplicatedColHeader(this.inColHeader);
			for (int i=0; i<vDuplicatedColHeader.size(); i++) {
				CommonLog.info("Duplicated ## " + vDuplicatedColHeader.elementAt(i));
			}
			if( !vDuplicatedColHeader.isEmpty() ){
				return UploadUtils.invalidColXML(vDuplicatedColHeader, UploadUtils.INVALID_COL_TYPE_DUPLICATE);
			}
			
			// Check for missing fields
			Vector vMissingColheader = this.getMissingColHeader(this.inColHeader);
			for (int i=0; i<vMissingColheader.size(); i++) {
				CommonLog.info("Missing ## " + vMissingColheader.elementAt(i));
			}
			if( !vMissingColheader.isEmpty() ){
				return UploadUtils.invalidColXML(vMissingColheader, UploadUtils.INVALID_COL_TYPE_MISSING);
			}
			
            String row = null;
            int rowNum = 1;
            boolean errorFlag = false;
            //Vector validRowVec = new Vector();
            int rowLimit = wizbini.cfgSysSetupadv.getResBatchUpload().getMaxUploadCount();
            while ((row = in.readLine()) != null) {
                if (row.trim().length() == 0) {
                    continue;
                }
                
                //if number of records more than row limit,throw a message.
                if (rowNum > rowLimit) {
                    throw new cwSysMessage(UploadUtils.SMSG_ULG_UPLOAD_COUNT_EXCEED_LIMIT, new Integer(rowLimit).toString());
                }

                rowNum++;
                extendQue parsedQue = new extendQue(maxObjID_, maxInter_, maxOpt_,0,0);
                if(row != null){
                	//由于excel文件有些现在还不知道的格式另存为txt文件后，会使格内容换行，所惟去掉
            		row = row.replaceAll(UploadUtils.colDelimiter_ + "\"" +UploadUtils.colDelimiter_ ,  UploadUtils.colDelimiter_  + "\"");
            	}
                switch ( populate(parsedQue, row, que_type, allow_upd, wizbini.cfgTcEnabled) ) {
                    case 0:
                        try{
                            RawQuestion.uploadQue(con, parsedQue, ulg_id, rowNum, parsedQue.int_count);
                        }catch(Exception e){
                        	CommonLog.error(e.getMessage(),e);
                        }
                        break;
                    case 1:
                        errorFlag = true;
                        invalidCategoryIdVec.addElement(new Integer(rowNum));
                        break;
                    case 2:
                        errorFlag = true;
                        invalidOptionVec.addElement(new Integer(rowNum));
                        break;
                    case 3:
                        errorFlag = true;
                        invalidAnswer.addElement(new Integer(rowNum));
                        break;
                    case 5:
                        errorFlag = true;
                        invalidAshtml.addElement(new Integer(rowNum));
                        break;
                    case 6:
                        errorFlag = true;
                        invalidScore.addElement(new Integer(rowNum));
                        break;
                    case 7:
                        errorFlag = true;
                        invalidDifficulty.addElement(new Integer(rowNum));
                        break;
                    case 8:
                        errorFlag = true;
                        invalidDuration.addElement(new Integer(rowNum));
                        break;
                    case 9:
                        errorFlag = true;
                        invalidStatus.addElement(new Integer(rowNum));
                        break;
                    case 10:
                        errorFlag = true;
                        invalidTitle.addElement(new Integer(rowNum));
                        break;
                    case 11:
                        errorFlag = true;
                        invalidContent.addElement(new Integer(rowNum));
                        break;
                    case ROW_INVALID_SUBMIT_FILE:
                        errorFlag = true;
                        invalidSubmitFile.addElement(new Integer(rowNum));
                        break;
                    case ROW_INVALID_RESOURCE_ID:
                        errorFlag = true;
                        invalidResource.addElement(new Integer(rowNum));
                        break;
                    case ROW_INVALID_PERMISSION:
                        errorFlag = true;
                        invalidPermission.addElement(new Integer(rowNum));
                        break;
                    case ROW_INVALID_TITLE_TOO_LONG:
                    	errorFlag = true;
                    	invalidTitleTooLong.addElement(new Integer(rowNum));
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

    private int populate(extendQue que, String row, String que_type, boolean allow_upd, boolean tc_enabled) throws SQLException{
        
        //StringTokenizer tokens = new StringTokenizer( row, (new Character(UploadUtils.colDelimiter_)).toString(), true );
        String[] tokens = cwUtils.splitToString(row, (new Character(UploadUtils.colDelimiter_)).toString());
        int col = 0;
        int[] ans = null;
        int queBlankNum = 0;
        String asHTML = UploadUtils.YES;
        String submit_file_ind = null;
        long usr_ent_id = 0;
        try {
            usr_ent_id = dbRegUser.getEntId(con, usr_id);
        } catch (qdbException e) {
            throw new SQLException();
        }
        
        int que_score = 0;
        for(int i=0; i<tokens.length; i++){
            String colName = (String)inColHeader.elementAt(col);
            if( (colName).equalsIgnoreCase(StdColName[0]) ) { //Category ID
                try {
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
                } catch (Exception e) {
                    return ROW_INVALID_CATEGORY_ID;
                }
            } else if( colName.equalsIgnoreCase(StdColName[1]) ) { // Title
                que.title = UploadUtils.escString(tokens[i]);
                if( que.title == null || que.title.length() == 0) {
                    return ROW_INVALID_TITLE;
                }else if(CharUtils.getStringLength(que.title) > 80){
                	return ROW_INVALID_TITLE_TOO_LONG;
                }
                else {
					if (que.title.indexOf("\n")>-1) {
						return ROW_INVALID_TITLE;	
					}
                }
            } else if( colName.equalsIgnoreCase(StdColName[2]) ) { // Question
                que.cont = cwUtils.esc4XML(UploadUtils.escString(tokens[i]));
                if( que.cont == null || que.cont.length() == 0) {
                    return ROW_INVALID_CONTENT;
                }
                
            } /*else if( colName.equalsIgnoreCase(StdColName[3]) ) { // AsHTML
                try{
                    asHTML = UploadUtils.checkYesNo(tokens[i]);
                }catch(cwException e){
                    return ROW_INVALID_ASHTML;
                }
                
            }*/ else if( colName.equalsIgnoreCase(StdColName[3]) ) { // Model Answer
				que.int_count = 1;
                que.inter[0].type = que_type;
                que.inter[0].shuffle = null;                
                que.inter[0].att_len = max_att_len;
                que.inter[0].opt[0].case_sense = "N";
                que.inter[0].opt[0].spc_sense = "N";
                que.inter[0].opt[0].type = "Text";
                que.inter[0].opt[0].exp = cwUtils.esc4XML(UploadUtils.escString(tokens[i]));
            } else if( colName.equalsIgnoreCase(StdColName[4]) ) { // Score
				que.int_count = 1;
                que.inter[0].type = que_type;
                que.inter[0].shuffle = null;                
                que.inter[0].att_len = max_att_len;
                que.inter[0].opt[0].case_sense = "N";
                que.inter[0].opt[0].spc_sense = "N";
                que.inter[0].opt[0].type = "Text";

                try {
                    Integer.parseInt(tokens[i]);
                } catch (Exception e) {
                    return ROW_INVALID_SCORE;
                }
                
                que.inter[0].opt[0].score = Integer.parseInt(tokens[i]);
                
                if (que.inter[0].opt[0].score <= 0 || que.inter[0].opt[0].score > 100) {
                    return ROW_INVALID_SCORE;
                }
            } else if( colName.equalsIgnoreCase(StdColName[5]) ) { // Difficulty
                try{
                    que.diff = Integer.parseInt( tokens[i] );
                    
                    if (que.diff < 1 || que.diff > 3) {
                        return ROW_INVALID_DIFFICULTY;
                    }
                }catch(Exception e){
                    return ROW_INVALID_DIFFICULTY;
                }

            }
            /*
            else if( colName.equalsIgnoreCase(StdColName[7]) ) { // Duration
                try{
					if( !UploadUtils.checkDecimal(tokens[i], 2)) {
						return ROW_INVALID_DURATION;
					}                	
                    que.dur = (Float.valueOf( tokens[i] )).floatValue();
                    
                    if (que.dur <= 0) {
                        return ROW_INVALID_DURATION;
                    }
                }catch(Exception e){
                    return ROW_INVALID_DURATION;
                }
                
            }
            */
            else if( colName.equalsIgnoreCase(StdColName[6]) ) { // Status
                try{
                    que.onoff = UploadUtils.checkOnOff( tokens[i] );
                }catch(Exception e){
                    return ROW_INVALID_STATUS;
                }
                
            }
            else if( colName.equalsIgnoreCase(StdColName[7]) ) { // Description
                que.desc = UploadUtils.escString(tokens[i]);
                /*
				if( que.desc == null || que.desc.length() == 0 || que.desc.length() > 1000)
					return ROW_INVALID_CONTENT;
				*/
            }
            /*else if( colName.equalsIgnoreCase(StdColName[8]) ) { //File Submit
            	try{
	            	if( UploadUtils.checkYesNo(tokens[i]).equalsIgnoreCase(UploadUtils.YES) ) {
						submit_file_ind = "true";
	            	}else{
						submit_file_ind = "false";
	            	}
            	}catch(Exception e){
            		return ROW_INVALID_SUBMIT_FILE;
            	}
            } */else if ( colName.equalsIgnoreCase(StdColName[8]) ) { //system_id
                try {
                    String tempToken = tokens[i];
                    if (tempToken!=null && tempToken.length() > 0) {
                        que.resource_id = Long.parseLong(tokens[i]);
                        if (que.resource_id <= 0) {
                            return ROW_INVALID_RESOURCE_ID;
                        }
                        if ((!dbQuestion.queIDExist(con, que.resource_id, dbQuestion.QUE_TYPE_ESSAY) || !allow_upd) 
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
        
        if( asHTML.equalsIgnoreCase(UploadUtils.YES) ){
            que.cont_html = true;
        } else {
            que.cont_html = false;
        }
        
       /* if (submit_file_ind==null) {
        	return ROW_INVALID_SUBMIT_FILE;
        }
		if (submit_file_ind.equals("true")) {
			que.submit_file_ind = true;
		}
		else {
			que.submit_file_ind = false;
		}
*/
        que.lan = DEFAULT_LANGUAGE;
        que.folder = DEFAULT_FOLDER;
        que.owner_id = this.usr_id;
        que.queType = que_type;
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
        if( !invalidResource.isEmpty() ){
            xml.append("<invalid_resource_lines>");
            for(int i=0; i<invalidResource.size(); i++)
                xml.append("<line num=\"").append(invalidResource.elementAt(i)).append("\"/>");
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
        if( !invalidDuration.isEmpty() ){
            xml.append("<invalid_duration_lines>");
            for(int i=0; i<invalidDuration.size(); i++)
                xml.append("<line num=\"").append(invalidDuration.elementAt(i)).append("\"/>");
             xml.append("</invalid_duration_lines>");
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
        if( !invalidTitleTooLong.isEmpty() ){
            xml.append("<invalid_title_too_long_lines>");
            for(int i=0; i<invalidTitleTooLong.size(); i++)
                xml.append("<line num=\"").append(invalidTitleTooLong.elementAt(i)).append("\"/>");
             xml.append("</invalid_title_too_long_lines>");
        }
        if( !invalidContent.isEmpty() ){
            xml.append("<invalid_content_lines>");
            for(int i=0; i<invalidContent.size(); i++)
                xml.append("<line num=\"").append(invalidContent.elementAt(i)).append("\"/>");
             xml.append("</invalid_content_lines>");
        }
		if( !invalidSubmitFile.isEmpty() ){
			xml.append("<invalid_submit_file_lines>");
			for(int i=0; i<invalidSubmitFile.size(); i++)
				xml.append("<line num=\"").append(invalidSubmitFile.elementAt(i)).append("\"/>");
			 xml.append("</invalid_submit_file_lines>");
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


    
    private Vector parseInColHeader(String colHeader)
        throws cwException, IOException {
        Vector invalidColVec = new Vector();
        
        int ansInd = 1;
        int scoreInd = 2;
        int validInd = 3;
        Hashtable htBlankFlag = new Hashtable();
        String htLabelColName = "COLUMN_INDEX";
        String htLabelFlag = "FLAG";
        
        //StringTokenizer tokens = new StringTokenizer( colHeader, (new Character(UploadUtils.colDelimiter_)).toString(), true );
        String[] tokens = cwUtils.splitToString(colHeader, (new Character(UploadUtils.colDelimiter_)).toString());
nextCol:            
        for(int i=0; i<tokens.length; i++){
            for(int j=0; j<StdColName.length; j++) {
                if( tokens[i].equalsIgnoreCase(StdColName[j]) ){
                    this.inColHeader.addElement(tokens[i]);
                    continue nextCol;
                }
            }
            invalidColVec.addElement(tokens[i]);
        }
  
        return invalidColVec;
    }

	private Vector getDuplicatedColHeader(Vector vInColHeader) {
		Vector vDuplicatedColheader = new Vector();
		for (int i=0; i<vInColHeader.size(); i++) {
			for (int j=0; j<vInColHeader.size(); j++) {
				if (i!=j) {
					String str1 = (String) vInColHeader.elementAt(i);
					String str2 = (String) vInColHeader.elementAt(j);
					if (str1.equals(str2)) {
						if (!vDuplicatedColheader.contains(str1)) {
							vDuplicatedColheader.add(str1);
						}
					}
				}
			}
		}
		return vDuplicatedColheader;
	}
	
	private Vector getMissingColHeader(Vector vInColHeader) {
		Vector vMissingColheader = new Vector();
		for (int i=0; i<RequiredStdColName.length; i++) {
			if (!vInColHeader.contains(RequiredStdColName[i])) {
				vMissingColheader.add(RequiredStdColName[i]);
			}
		}
		return vMissingColheader;
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
		String foldId = "Folder ID", title = "Title", question = "Question";//, asHtml = "AsHTML"
		String modelAnswer = "Model Answer", score = "Score";
		String difficulty = "Difficulty", status = "Status", description = "Description";
		String  resourceID = "Resource ID"; //submitFile = "Submit File",

		String lang = prof.cur_lan;
		foldId = ImportTemplate.getLabelValue("wb_imp_tem_folder_id", lang, foldId).trim();
		title = ImportTemplate.getLabelValue("wb_imp_tem_title", lang, title).trim();
		question = ImportTemplate.getLabelValue("wb_imp_tem_question", lang, question).trim();
//		asHtml = ImportTemplate.getLabelValue("wb_imp_tem_as_html", lang, asHtml).trim();
		modelAnswer = ImportTemplate.getLabelValue("wb_imp_tem_model_answer", lang, modelAnswer).trim();
		score = ImportTemplate.getLabelValue("wb_imp_tem_score", lang, score).trim();
		difficulty = ImportTemplate.getLabelValue("wb_imp_tem_difficulty", lang, difficulty).trim();
		status = ImportTemplate.getLabelValue("wb_imp_tem_status", lang, status).trim();
		description = ImportTemplate.getLabelValue("wb_imp_tem_description", lang, description).trim();
//		submitFile = ImportTemplate.getLabelValue("wb_imp_tem_submit_file", lang, submitFile).trim();
		resourceID = ImportTemplate.getLabelValue("wb_imp_tem_resource_id", lang, resourceID).trim();

		String[] labels = new String[] { foldId, title, question, modelAnswer, score, difficulty, status, description, resourceID };
		return labels;
	}

	private String[] getRequiredLabelArray(loginProfile prof) {
		String foldId = "Folder ID", title = "Title", question = "Question"; //, asHtml = "AsHTML"
		String modelAnswer = "Model Answer", score = "Score";
		String difficulty = "Difficulty", status = "Status"; //, submitFile = "Submit File"

		String lang = prof.cur_lan;
		foldId = ImportTemplate.getLabelValue("wb_imp_tem_folder_id", lang, foldId).trim();
		title = ImportTemplate.getLabelValue("wb_imp_tem_title", lang, title).trim();
		question = ImportTemplate.getLabelValue("wb_imp_tem_question", lang, question).trim();
//		asHtml = ImportTemplate.getLabelValue("wb_imp_tem_as_html", lang, asHtml).trim();
		modelAnswer = ImportTemplate.getLabelValue("wb_imp_tem_model_answer", lang, modelAnswer).trim();
		score = ImportTemplate.getLabelValue("wb_imp_tem_score", lang, score).trim();
		difficulty = ImportTemplate.getLabelValue("wb_imp_tem_difficulty", lang, difficulty).trim();
		status = ImportTemplate.getLabelValue("wb_imp_tem_status", lang, status).trim();
//		submitFile = ImportTemplate.getLabelValue("wb_imp_tem_submit_file", lang, submitFile).trim();

		String[] labels = new String[] { foldId, title, question, modelAnswer, score, difficulty, status};
		return labels;
	}
    

}