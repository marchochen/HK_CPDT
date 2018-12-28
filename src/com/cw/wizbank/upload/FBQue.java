package com.cw.wizbank.upload;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cw.wizbank.accesscontrol.AcResources;
import com.cw.wizbank.accesscontrol.AcTrainingCenter;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.db.DbRawQuestion;
import com.cw.wizbank.db.DbUploadLog;
import com.cw.wizbank.qdb.dbObjective;
import com.cw.wizbank.qdb.dbQuestion;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbResource;
import com.cw.wizbank.qdb.extendQue;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbEnv;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CharUtils;
import com.cwn.wizbank.utils.CommonLog;

public class FBQue{
    
    //Log filename
    public final static String[] FILENAME_LIST = { UploadUtils.SUCCESS_FILE, UploadUtils.FAILURE_FILE, UploadUtils.ERROR_FILE};
    public final static String[] LOG_LIST = {"SUCCESS", "UNSUCCESS", "ERROR" };
    
    //History Session Key
    private static final String SESS_LOG_HISTORY_LOG_ID = "SESS_LOG_HISTORY_LOG_ID";
    private static final String SESS_LOG_HISTORY_TIMESTAMP = "SESS_LOG_HISTORY_TIMESTAMP";
    private static final String SESS_LOG_HISTORY_TOTAL_REC = "SESS_LOG_HISTORY_TOTAL_REC";
    private static final String SESS_LOG_HISTORY_TOTAL_PAGE = "SESS_LOG_HISTORY_TOTAL_PAGE";
            
    private static final int maxObjID_ = 1;
    private static final int maxOpt_   = 10;
    private int maxInter_ = 50;
    
    private final static int ROW_INVALID_CATEGORY_ID = 1;
    private final static int ROW_INVALID_OPTION = 2;
    private final static int ROW_INVALID_ANSWER = 3;
    private final static int ROW_INVALID_ASHTML = 5;
    private final static int ROW_INVALID_SCORE = 6;
    private final static int ROW_INVALID_DIFFICULTY = 7;
//    private final static int ROW_INVALID_DURATION = 8;
    private final static int ROW_INVALID_DESC = 8;
    private final static int ROW_INVALID_STATUS = 9;
    private final static int ROW_INVALID_TITLE = 10;
    private final static int ROW_INVALID_CONTENT = 11;
    private final static int ROW_EMPTY_BLANK = 12;
    private final static int ROW_INVALID_BLANK_ORDER = 13;
    private final static int ROW_INVALID_SYSTEM_ID = 14;
    private final static int ROW_INVALID_PERMISSION = 15;
    
    
    
    
    private Vector invalidCategoryIdVec = null;
    private Vector invalidOptionVec = null;
    private Vector invalidAnswer = null;
    private Vector invalidAshtml = null;
    private Vector invalidScore = null;
    private Vector invalidDifficulty = null;
//    private Vector invalidDuration = null;
    private Vector invalidDesc = null;
    private Vector invalidStatus = null;
    private Vector invalidColumn = null;
    private Vector invalidTitle = null;
    private Vector invalidContent = null;
    private Vector emptyBlank = null;
    private Vector invalidBlankOrder = null;
    private Vector invalidSystemID = null;
    private Vector invalidPermission = null;
    private Vector dupQueId = null;
    
    private Connection con = null;
    private long site_id;
    private String cur_role;
    private String usr_id = null;
    private qdbEnv static_env = null;
    
    private int blankNum = 0;
    private int currRowBlankNum = 0;

    private Vector cachedValidCategoryIdVec = null;
    private Vector cachedInvalidCategoryIdVec = null;

    private String[] StdColName = {
        "Folder ID",
        "Title",
        "Question",
        //"Option ",
        //"Shuffle",
        //"Answer",
        //"AsHTML",
        //"Score",
        "Difficulty",
        //"Duration",
        "Status",
        "Description",
        //"Explanation"
        "Resource ID"
    };
    
    private static final boolean[] StdColRequired = {true, true, true, true, true, true, false, false} ;
    
    private boolean StdColUsed_[] = new boolean[StdColName.length];

    private static String StdOptionColName = "Blank ";
    private static String StdOptionColNameSuffix1 = " Answer";
    private static String StdOptionColNameSuffix2 = " Score";
	
	//UnicodeLittle:excel unicode encoding
    private final static String DEFAULT_ENC = "UnicodeLittle";

    private final static String DEFAULT_LANGUAGE = "ISO-8859-1";
    private String DEFAULT_FOLDER = "CW";

    private Vector inColHeader = null;
    
    public FBQue(Connection con, qdbEnv static_env, loginProfile prof){
    	// set label 
        this.StdColName = this.getLabelArray(prof);
        this.StdColUsed_ = new boolean[this.StdColName.length];
        this.setOtherLabel(prof);
        
        this.con = con;
        this.site_id = prof.root_ent_id;
        this.usr_id = prof.usr_id;
        this.static_env = static_env;
        this.inColHeader = new Vector();
        this.cur_role = prof.current_role;

        invalidCategoryIdVec = new Vector();
        invalidOptionVec = new Vector();
        invalidAnswer = new Vector();
        invalidAshtml = new Vector();
        invalidScore = new Vector();
        invalidDifficulty = new Vector();
//        invalidDuration = new Vector();
        invalidDesc = new Vector();
        invalidStatus = new Vector();
        invalidColumn = new Vector();
        invalidTitle = new Vector();
        invalidContent = new Vector();
        emptyBlank = new Vector();
        invalidBlankOrder = new Vector();
        invalidSystemID = new Vector();
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
            long ulg_id = UploadUtils.insUploadLog(con, this.usr_id, ulg_desc, srcFile.getName(), static_env, dbQuestion.QUE_TYPE_FILLBLANK);
            BufferedReader in = new BufferedLineReader(new InputStreamReader(new FileInputStream(srcFile), DEFAULT_ENC)); 
			String colHeader = in.readLine();
            if (colHeader == null) {
                in.close();
                throw new cwSysMessage(UploadUtils.SMSG_ULG_INVALID_FILE);
            }
            Vector duplicateColHeader = new Vector();
            Vector invalidColHeader = parseInColHeader(colHeader, duplicateColHeader);    
            Vector missingColHeader = new Vector();
            for (int i=0; i<StdColName.length; i++){
                if (!StdColUsed_[i] && StdColRequired[i]){
                   missingColHeader.addElement(StdColName[i]);
                }                        
            }
            maxInter_ = blankNum;
            
            if( !invalidColHeader.isEmpty()){
                return UploadUtils.invalidColXML(invalidColHeader, UploadUtils.INVALID_COL_TYPE_UNRECOGNIZED);
            }
            if (!duplicateColHeader.isEmpty()){
                return UploadUtils.invalidColXML(duplicateColHeader, UploadUtils.INVALID_COL_TYPE_DUPLICATE);
            }
            if (!missingColHeader.isEmpty()){
                return UploadUtils.invalidColXML(missingColHeader, UploadUtils.INVALID_COL_TYPE_MISSING);
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
                switch ( populate(parsedQue, row, allow_upd, wizbini.cfgTcEnabled) ) {
                    case 0:
                        try{
                            RawQuestion.uploadQue(con, parsedQue, ulg_id, rowNum, parsedQue.int_count);
                        }catch(Exception e){
                            errorFlag = true;
                            invalidColumn.addElement(new Integer(rowNum));
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
                        invalidDesc.addElement(new Integer(rowNum));
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
                    case 12:
                        errorFlag = true;
                        emptyBlank.addElement(new Integer(rowNum));
                        break;
                    case 13:
                        errorFlag = true;
                        invalidBlankOrder.addElement(new Integer(rowNum));
                        break;
                    case 14:
                        errorFlag = true;
                        invalidSystemID.addElement(new Integer(rowNum));
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
        int[] ans = null;
        int queBlankNum = 0;
        String asHTML = UploadUtils.YES;
        float que_score = 0;
        long usr_ent_id = 0;
        try {
            usr_ent_id = dbRegUser.getEntId(con, usr_id);
        } catch (qdbException e) {
            throw new SQLException();
        }
        for(int i=0; i<tokens.length; i++){
        	if(i >= inColHeader.size()){
        		continue;
        	} 
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
                if( que.title == null || que.title.length() == 0 || CharUtils.getStringLength(que.title) > 80 || UploadUtils.containsNewLine(que.title))
                    return ROW_INVALID_TITLE;
                    
                
            } else if( colName.equalsIgnoreCase(StdColName[2]) ) { // Question
				que.cont = cwUtils.esc4XML(UploadUtils.escString(tokens[i]));
				if (que.cont == null || que.cont.length() == 0) {
					return ROW_INVALID_CONTENT;
				}

				// validate whether the blank is in order or not
				int curr_order = 1;
				String blank_prefix = "[blank";
				String que_cont = que.cont;
				while (que_cont.indexOf(blank_prefix) != -1 && checkBlank(que_cont, blank_prefix)) {
					/*if (que_cont.charAt(que_cont.indexOf(blank_prefix) + blank_prefix.length()) != ' ') {
						return ROW_INVALID_CONTENT;
					}*/
					String temp_que_cont = que_cont.substring(que_cont.indexOf(blank_prefix));
					int blank_close_index = que_cont.indexOf(blank_prefix) + temp_que_cont.indexOf("]");
					String src_pattern = que_cont.substring(que_cont.indexOf(blank_prefix), blank_close_index + 1);

					// find the blank order
					int int_order = 0;
					for (int k = src_pattern.length() - 2; k >= 0; k--) {
						if (src_pattern.charAt(k) == 'k') {
							int_order = Integer.parseInt(src_pattern.substring(k + 1, src_pattern.length() - 1));
							if (int_order != curr_order || int_order > blankNum) {
								return ROW_INVALID_BLANK_ORDER;
							}
							curr_order++;
							break;
						}
					}
					que_cont = que_cont.substring(blank_close_index + 1);
				}
                currRowBlankNum = curr_order-1;
                if(currRowBlankNum == 0) {
                	return ROW_INVALID_CONTENT;
                }
                
                /*
                else {
                    // replace [blank x] to [blank]
                    String blank_prefix = "[blank";
                    String que_cont = que.cont;
                    que.cont = "";
                    while (que_cont.indexOf(blank_prefix) != -1 && que_cont.charAt(que_cont.indexOf(blank_prefix) + blank_prefix.length()) != ']') {
                        String temp_que_cont = que_cont.substring(que_cont.indexOf(blank_prefix));
                        int blank_close_index = que_cont.indexOf(blank_prefix) + temp_que_cont.indexOf("]");
                        String src_pattern = que_cont.substring(que_cont.indexOf(blank_prefix), blank_close_index+1);
                        que.cont += replaceStr(que_cont.substring(0, blank_close_index+1), src_pattern, "[blank]");
                        que_cont = que_cont.substring(blank_close_index+1);
                    }
                    que.cont += que_cont;
                }
                */
                
            } /*else if( colName.equalsIgnoreCase(StdColName[3]) ) { // AsHTML
                try{
                    asHTML = UploadUtils.checkYesNo(tokens[i]);
                }catch(cwException e){
                    return ROW_INVALID_ASHTML;
                }
                
            } */else if( colName.equalsIgnoreCase(StdColName[3]) ) { // Difficulty
                try{
                    que.diff = Integer.parseInt( tokens[i] );
                    
                    if (que.diff < 1 || que.diff > 3) {
                        return ROW_INVALID_DIFFICULTY;
                    }
                }catch(Exception e){
                    return ROW_INVALID_DIFFICULTY;
                }

/*            } else if( colName.equalsIgnoreCase(StdColName[5]) ) { // Duration
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
                */
            } else if( colName.equalsIgnoreCase(StdColName[4]) ) { // Status
                try{
                    que.onoff = UploadUtils.checkOnOff( tokens[i] );
                }catch(Exception e){
                    return ROW_INVALID_STATUS;
                }
                
            } else if( colName.equalsIgnoreCase(StdColName[5]) ) { // Description
                que.desc = UploadUtils.escString(tokens[i]);
                if (que.desc != null && que.desc.length() > 1000){
                    return ROW_INVALID_DESC;
                }
            } else if( colName.startsWith(StdOptionColName) ) { //Option
                try{
                    if( tokens[i] != null && tokens[i].length() > 0 ) {
                        String[] temp_tokens = cwUtils.splitToString(colName, " ");
                        int blankNum = Integer.parseInt(temp_tokens[1]);
                        if (blankNum > currRowBlankNum) {
                            if( colName.endsWith(StdOptionColNameSuffix1) ) {                            
                                return ROW_INVALID_ANSWER;
                            }
                            else {
                                return ROW_INVALID_SCORE;
                            }
                        }
                        
                        if (blankNum > queBlankNum) {
                            queBlankNum = blankNum;
                            que.int_count = queBlankNum;
                        }
                        
                        que.inter[blankNum-1].type = dbQuestion.QUE_TYPE_FILLBLANK;
                        que.inter[blankNum-1].shuffle = null;                
                        
                        int optNum = 1;
                        for (int j=0; j<que.inter[blankNum-1].opt.length; j++) {
                            if (que.inter[blankNum-1].opt[j].cont != null && que.inter[blankNum-1].opt[j].cont.trim().length() > 0 && que.inter[blankNum-1].opt[j].score > 0) {
                                optNum = j+2;
                                break;
                            }
                        }
                        if( colName.endsWith(StdOptionColNameSuffix1) ) {                            
                            que.inter[blankNum-1].opt[optNum-1].cont = cwUtils.esc4XML(UploadUtils.escString(tokens[i]));
                            if (UploadUtils.containsNewLine(que.inter[blankNum-1].opt[optNum-1].cont)){
                                return ROW_INVALID_ANSWER;
                            }
                            if (tokens[i].length() > que.inter[blankNum-1].att_len) {
                                que.inter[blankNum-1].att_len = tokens[i].length();
                            }
                        }
                        else {                            
                            try {
                                que_score = Float.parseFloat(tokens[i]);
					            if( que_score < 1 || que_score > 100 ){
                    	            return ROW_INVALID_SCORE;
                                }
                            } catch (Exception e) {
                                return ROW_INVALID_SCORE;
                            }
                            que.inter[blankNum-1].opt[optNum-1].score = Integer.parseInt(tokens[i]);
                        }
                        que.inter[blankNum-1].opt[optNum-1].case_sense = "N";
                        que.inter[blankNum-1].opt[optNum-1].spc_sense = "N";
                        que.inter[blankNum-1].opt[optNum-1].type = "Text";
                    }
                }catch(Exception e){
                	CommonLog.error(e.getMessage(),e);
                    return ROW_INVALID_ANSWER;
                }
            } else if ( colName.equalsIgnoreCase(StdColName[6]) ) { //system_id
                try {
                    String tempToken = tokens[i];
                    if (tempToken!=null && tempToken.length() > 0) {
                        que.resource_id = Long.parseLong(tokens[i]);
                        if (que.resource_id <= 0) {
                            return ROW_INVALID_SYSTEM_ID;
                        }
                        if ((!dbQuestion.queIDExist(con, que.resource_id, dbQuestion.QUE_TYPE_FILLBLANK) || !allow_upd) 
                            && que.resource_id > 0) {
                            return ROW_INVALID_SYSTEM_ID;
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
                            return ROW_INVALID_SYSTEM_ID;
                        } else {
                            dupQueId.addElement(new Long(que.resource_id));
                        }
                    } else {
                        que.resource_id = 0;
                    }
                } catch (NumberFormatException e) {
                	CommonLog.error(e.getMessage(),e);
                    return ROW_INVALID_SYSTEM_ID;
                }
            } else {
                return -1;
            }
            col++;
        }
        if( queBlankNum < 1 )
            return ROW_EMPTY_BLANK;
            
        for (int i=0; i<queBlankNum; i++) {
            if ((que.inter[i].opt[0].cont == null || que.inter[i].opt[0].cont.trim().length() == 0) && (que.inter[i].opt[0].score <= 0)) {
                return ROW_INVALID_ANSWER;
            }
            for (int j=0; j<que.inter[i].opt.length; j++) {
                if (que.inter[i].opt[j].cont != null && que.inter[i].opt[j].cont.trim().length() > 0) {
                    if (que.inter[i].opt[j].score <= 0) {
                        return ROW_INVALID_SCORE;
                    }
                }
                if (que.inter[i].opt[j].score > 0) {
                    if (que.inter[i].opt[j].cont == null || que.inter[i].opt[j].cont.trim().length() == 0) {
                        return ROW_INVALID_ANSWER;
                    }
                }
            }
        }
        if (queBlankNum!=currRowBlankNum){
            return ROW_INVALID_ANSWER;
        }
                    
        if( asHTML.equalsIgnoreCase(UploadUtils.YES) ){
            que.cont_html = true;
            /*
            for(int i=0; i<optionNum; i++)
                que.inter[0].opt[i].cont_html = true;
                */
        } else {
            que.cont_html = false;
            /*
            for(int i=0; i<queOptionNum; i++)
                que.inter[0].opt[i].cont_html = false;
                */
        }
        
        
        que.lan = DEFAULT_LANGUAGE;
        que.folder = DEFAULT_FOLDER;
        que.owner_id = this.usr_id;
        que.queType = dbQuestion.QUE_TYPE_FILLBLANK;
        return 0;

    }

	private boolean checkBlank(String que_cont, String blank_prefix) {
		if (que_cont != null && que_cont.indexOf(blank_prefix) != -1) {
			String temp = que_cont.substring(que_cont.indexOf(blank_prefix));
			Pattern pattern = Pattern.compile("\\[blank(\\d+)\\](.*)");
			Matcher matcher = pattern.matcher(temp);
			return matcher.find();
		}
		return false;
	}

	private boolean categoryIdExist(long id) throws SQLException {
		if (cachedValidCategoryIdVec.indexOf(new Long(id)) != -1)
			return true;
		else if (cachedInvalidCategoryIdVec.indexOf(new Long(id)) != -1)
			return false;
		else {
			if (UploadUtils.validObjective(this.con, this.site_id, id)) {
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
//        if( !invalidDuration.isEmpty() ){
//            xml.append("<invalid_duration_lines>");
//            for(int i=0; i<invalidDuration.size(); i++)
//                xml.append("<line num=\"").append(invalidDuration.elementAt(i)).append("\"/>");
//             xml.append("</invalid_duration_lines>");
//        }
        if( !invalidDesc.isEmpty() ){
            xml.append("<invalid_desc_lines>");
            for(int i=0; i<invalidDesc.size(); i++)
                xml.append("<line num=\"").append(invalidDesc.elementAt(i)).append("\"/>");
             xml.append("</invalid_desc_lines>");
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
        if( !emptyBlank.isEmpty() ){
            xml.append("<empty_blank_lines>");
            for(int i=0; i<emptyBlank.size(); i++)
                xml.append("<line num=\"").append(emptyBlank.elementAt(i)).append("\"/>");
             xml.append("</empty_blank_lines>");
        }
        if( !invalidBlankOrder.isEmpty() ){
            xml.append("<invalid_blank_order_lines>");
            for(int i=0; i<invalidBlankOrder.size(); i++)
                xml.append("<line num=\"").append(invalidBlankOrder.elementAt(i)).append("\"/>");
             xml.append("</invalid_blank_order_lines>");
        }
        if( !invalidSystemID.isEmpty() ){
            xml.append("<invalid_resource_id>");
            for(int i=0; i<invalidSystemID.size(); i++)
                xml.append("<line num=\"").append(invalidSystemID.elementAt(i)).append("\"/>");
             xml.append("</invalid_resource_id>");
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


    
    private Vector parseInColHeader(String colHeader, Vector duplicateColVec)
        throws cwException, IOException {
            Vector invalidColVec = new Vector();
            Vector usedColVec = new Vector();
            int ansInd = 1;
            int scoreInd = 2;
            int validInd = 3;
            Hashtable htBlankFlag = new Hashtable();
            String htLabelColName = "COLUMN_INDEX";
            String htLabelFlag = "FLAG";
            
            for (int i = 0; i < StdColUsed_.length; i++)
                StdColUsed_[i] = false;

            //StringTokenizer tokens = new StringTokenizer( colHeader, (new Character(UploadUtils.colDelimiter_)).toString(), true );
            String[] tokens = cwUtils.splitToString(colHeader, (new Character(UploadUtils.colDelimiter_)).toString());
nextCol:            
            for(int i=0; i<tokens.length; i++){
                if (tokens[i].trim().length() == 0) {
                    continue nextCol;
                }
                if (usedColVec.contains(tokens[i])){
                    duplicateColVec.addElement(tokens[i]);
                }else{
                    usedColVec.addElement(tokens[i]);
                }
                for(int j=0; j<StdColName.length; j++) {
                    if( tokens[i].equalsIgnoreCase(StdColName[j]) ){
                        StdColUsed_[j] = true;
                        this.inColHeader.addElement(tokens[i]);
                        continue nextCol;
                    }
                }
                if( tokens[i].startsWith(StdOptionColName) && (tokens[i].endsWith(StdOptionColNameSuffix1) || tokens[i].endsWith(StdOptionColNameSuffix2))){
                    this.inColHeader.addElement(tokens[i]);
                    String[] temp_tokens = cwUtils.splitToString(tokens[i], " ");
                    int temp_num_of_blank = 0;
                    try{
                        temp_num_of_blank = Integer.parseInt(temp_tokens[1]);
                    }catch(NumberFormatException e){
                        invalidColVec.addElement(tokens[i]);
                        continue nextCol;
                    }
                    if (temp_num_of_blank > this.blankNum) {
                        this.blankNum = temp_num_of_blank;
                    }
                    
                    if (htBlankFlag.get(Integer.toString(temp_num_of_blank)) == null) {
                        Hashtable htBlankFlagDetail = new Hashtable();
                        htBlankFlagDetail.put(htLabelColName, tokens[i]);
                        if (tokens[i].endsWith(StdOptionColNameSuffix1)) {
                            htBlankFlagDetail.put(htLabelFlag, new Integer(1));
                        }
                        else if (tokens[i].endsWith(StdOptionColNameSuffix2)) {
                            htBlankFlagDetail.put(htLabelFlag, new Integer(2));
                        }
                        else {
                            htBlankFlagDetail.put(htLabelFlag, new Integer(0));
                        }
                        htBlankFlag.put(Integer.toString(temp_num_of_blank), htBlankFlagDetail);
                    }
                    else {
                        Hashtable htBlankFlagDetail = (Hashtable)htBlankFlag.get(Integer.toString(temp_num_of_blank));
                        int flagVaue = ((Integer)htBlankFlagDetail.get(htLabelFlag)).intValue();
                        if (tokens[i].endsWith(StdOptionColNameSuffix1)) {
                            if (flagVaue == 2) {
                                htBlankFlag.remove(Integer.toString(temp_num_of_blank));
                            }
                        }
                        else if (tokens[i].endsWith(StdOptionColNameSuffix2)) {
                            if (flagVaue == 1) {
                                htBlankFlag.remove(Integer.toString(temp_num_of_blank));
                            }
                        }
                    }
                                        
                    continue nextCol;
                }
                
                invalidColVec.addElement(tokens[i]);
            }
            
            // check whether there are non-pair Blank Score or Answer
            for (Enumeration e = htBlankFlag.keys() ; e.hasMoreElements() ;) {
                String key = (String)e.nextElement();
                Hashtable htBlankFlagDetail = (Hashtable)htBlankFlag.get(key);
                invalidColVec.addElement((String)htBlankFlagDetail.get(htLabelColName));
            }
             
            return invalidColVec;
        }


    public String save2DB(DbUploadLog ulg, loginProfile prof, boolean tc_enabled)
        throws cwException, SQLException, IOException
    {
        
        RawQuestion rawQue = new RawQuestion();
        Hashtable h_cnt = rawQue.save2DB(this.con, this.static_env, this.site_id, ulg.ulg_id, prof,tc_enabled);
        ulg.ulg_status = DbUploadLog.STATUS_COMPLETED;
        ulg.ulg_upd_datetime = cwSQL.getTime(this.con);
        ulg.updStatus(this.con);
        DbRawQuestion.del(this.con, ulg.ulg_id);
        
        return UploadUtils.getResultXML(static_env, ulg.ulg_id, h_cnt);
        
    }

   	private String replaceStr(String inStr, String src_pattern, String target_pattern) {
        String result = "";
   	    while (inStr.indexOf(src_pattern) != -1) {
   	        int index = inStr.indexOf(src_pattern);
   	        result += inStr.substring(0, index) + target_pattern;
   	        inStr = inStr.substring(index + src_pattern.length());
   	    }
   	    
   	    return result+inStr;
   	}
    
	private String[] getLabelArray(loginProfile prof) {
		String foldId = "Folder ID", title = "Title", question = "Question";//, asHtml = "AsHTML"
		String difficulty = "Difficulty";
		String status = "Status", description = "Description", resourceID = "Resource ID";
		
		String lang = prof.cur_lan;
		foldId = ImportTemplate.getLabelValue("wb_imp_tem_folder_id", lang, foldId).trim();
		title = ImportTemplate.getLabelValue("wb_imp_tem_title", lang, title).trim();
		question = ImportTemplate.getLabelValue("wb_imp_tem_question", lang, question).trim();
		//asHtml = ImportTemplate.getLabelValue("wb_imp_tem_as_html", lang, asHtml).trim();
		difficulty = ImportTemplate.getLabelValue("wb_imp_tem_difficulty", lang, difficulty).trim();
		status = ImportTemplate.getLabelValue("wb_imp_tem_status", lang, status).trim();
		description = ImportTemplate.getLabelValue("wb_imp_tem_description", lang, description).trim();
		resourceID = ImportTemplate.getLabelValue("wb_imp_tem_resource_id", lang, resourceID).trim();

		String[] labels = new String[] { foldId, title, question, difficulty, status, description, resourceID };
		return labels;
	}
	
	private void setOtherLabel(loginProfile prof) {	
		String blankStr = "Blank ", answerStr = " Answer", scoreStr = " Score";
		
		String lang = prof.cur_lan;
		blankStr = ImportTemplate.getLabelValue("wb_imp_tem_blank", lang, blankStr).trim() + " ";
		answerStr = " " + ImportTemplate.getLabelValue("wb_imp_tem_answer", lang, answerStr).trim();
		scoreStr = " " + ImportTemplate.getLabelValue("wb_imp_tem_score", lang, scoreStr).trim();
		
	    StdOptionColName = blankStr;
	    StdOptionColNameSuffix1 = answerStr;
	    StdOptionColNameSuffix2 = scoreStr;	
	}
        
}