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
public class MCQue{
    
    //Log filename
    public final static String[] FILENAME_LIST = { UploadUtils.SUCCESS_FILE, UploadUtils.FAILURE_FILE, UploadUtils.ERROR_FILE};
    public final static String[] LOG_LIST = {"SUCCESS", "UNSUCCESS", "ERROR" };
    
    //History Session Key
    private static final String SESS_LOG_HISTORY_LOG_ID = "SESS_LOG_HISTORY_LOG_ID";
    private static final String SESS_LOG_HISTORY_TIMESTAMP = "SESS_LOG_HISTORY_TIMESTAMP";
    private static final String SESS_LOG_HISTORY_TOTAL_REC = "SESS_LOG_HISTORY_TOTAL_REC";
    private static final String SESS_LOG_HISTORY_TOTAL_PAGE = "SESS_LOG_HISTORY_TOTAL_PAGE";
        
    
    
    private final static int ROW_INVALID_CATEGORY_ID = 1;
    private final static int ROW_INVALID_OPTION = 2;
    private final static int ROW_INVALID_ANSWER = 3;
    private final static int ROW_INVALID_SHUFFLE = 4;
    private final static int ROW_INVALID_ASHTML = 5;
    private final static int ROW_INVALID_SCORE = 6;
    private final static int ROW_INVALID_DIFFICULTY = 7;
    private final static int ROW_INVALID_DURATION = 8;
    private final static int ROW_INVALID_STATUS = 9;
    private final static int ROW_INVALID_TITLE = 10;
    private final static int ROW_INVALID_CONTENT = 11;
    private final static int ROW_INVALID_DESC = 12;
    private final static int ROW_INVALID_SYSTEM_ID = 13;
    private final static int ROW_INVALID_PERMISSION = 14;
    
    
    
    
    
    private Vector invalidCategoryIdVec = null;
    private Vector invalidResourceIdVec = null;
    private Vector invalidOptionVec = null;
    private Vector invalidAnswer = null;
    private Vector invalidShuffle = null;
    private Vector invalidAshtml = null;
    private Vector invalidScore = null;
    private Vector invalidDifficulty = null;
    private Vector invalidDuration = null;
    private Vector invalidStatus = null;
    private Vector invalidColumn = null;
    private Vector invalidTitle = null;
    private Vector invalidContent = null;
    private Vector invalidDesc = null;
    private Vector invalidPermission = null;
    private Vector dupQueId = null;
    
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
        "Title",
        "Question",
        //"Option ",
        "Shuffle",
        "Answer",
        "AsHTML",
        "Score",
        "Difficulty",
        "Duration",
        "Status",
        "Description",
        "Explanation",
        "Resource ID"
    };
    
    private String[] REQUIRED_COLUMN = {
    	"Folder ID",
    	"Title",
    	"Question",
    	"Option 1",
    	"Option 2",
    	"Shuffle",
    	"Answer",
    	//"AsHTML",
    	"Score",
    	"Difficulty",
    	"Status"
    };
    
    
    private String StdOptionColName = "Option ";
	private static final int MAX_OPTION_NUM = 100;
	//UnicodeLittle:excel unicode encoding
    private final static String DEFAULT_ENC = "UnicodeLittle";

    private final static String DEFAULT_LANGUAGE = "ISO-8859-1";
    private String DEFAULT_FOLDER = "CW";

    private Vector inColHeader = null;
    
    public MCQue(Connection con, qdbEnv static_env, loginProfile prof){
        // set label
        this.StdColName = this.getLabelArray(prof);
        this.REQUIRED_COLUMN = this.getRequiredLabelArray(prof);
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
        invalidShuffle = new Vector();
        invalidAshtml = new Vector();
        invalidScore = new Vector();
        invalidDifficulty = new Vector();
        invalidDuration = new Vector();
        invalidStatus = new Vector();
        invalidColumn = new Vector();
        invalidTitle = new Vector();
        invalidContent = new Vector();
        invalidDesc = new Vector();
        invalidResourceIdVec = new Vector();
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
            long ulg_id = UploadUtils.insUploadLog(con, this.usr_id, ulg_desc, srcFile.getName(), static_env, dbQuestion.QUE_TYPE_MULTI);
            BufferedReader in = new BufferedLineReader(new InputStreamReader(new FileInputStream(srcFile), DEFAULT_ENC)); 
			String colHeader = in.readLine();
            if (colHeader == null) {
                in.close();
                throw new cwSysMessage(UploadUtils.SMSG_ULG_INVALID_FILE);
            }
            Vector invalidColHeader = parseInValidColHeader(colHeader);            
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

            String row = null;
            int rowNum = 1;
            boolean errorFlag = false;
            //Vector validRowVec = new Vector();
            int rowLimit = wizbini.cfgSysSetupadv.getResBatchUpload().getMaxUploadCount();
            while ((row = in.readLine()) != null) {
				if( row.trim().length() == 0 ){
					continue;
				}
                rowNum++;
                
                //if number of records more than row limit,throw a message.
                if (rowNum > rowLimit) {
                    throw new cwSysMessage(UploadUtils.SMSG_ULG_UPLOAD_COUNT_EXCEED_LIMIT, new Integer(rowLimit).toString());
                }
                extendQue parsedQue = new extendQue(1, 1, this.optionNum, 0, 0);
                if(row != null){
                	//由于excel文件有些现在还不知道的格式另存为txt文件后，会使格内容换行，所惟去掉
            		row = row.replaceAll(UploadUtils.colDelimiter_ + "\"" +UploadUtils.colDelimiter_ ,  UploadUtils.colDelimiter_  + "\"");
            	}
                switch ( populate(parsedQue, row, allow_upd, wizbini.cfgTcEnabled) ) {
                    case 0:
                        try{
                        	parsedQue.queType = dbQuestion.QUE_TYPE_MULTI;
                            RawQuestion.uploadQue(con, parsedQue, ulg_id, rowNum, 1);
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
                    case 4:
                        errorFlag = true;
                        invalidShuffle.addElement(new Integer(rowNum));
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
                    case 12:
                    	errorFlag = true;
                    	invalidDesc.addElement(new Integer(rowNum));
                    	break;
                    case 13:
                        errorFlag = true;
                        invalidResourceIdVec.addElement(new Integer(rowNum));
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
        
        //StringTokenizer tokens = new StringTokenizer( row, (new Character(UploadUtils.colDelimiter_)).toString(), true );
        String[] tokens = cwUtils.splitToString(row, (new Character(UploadUtils.colDelimiter_)).toString());
        int col = 0;
        int[] ans = null;
        int queOptionNum = 0;
        String asHTML = UploadUtils.YES;
        int que_score = 0;
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
                
            } else if( colName.equalsIgnoreCase(StdColName[3]) ) { // Shuffle
                try{
                    que.inter[0].shuffle = UploadUtils.checkYesNo(tokens[i]);
                }catch(cwException e){
                    return ROW_INVALID_SHUFFLE;
                }
                
            } else if( colName.equalsIgnoreCase(StdColName[4]) ) { // Answer
                try{
                    ans = UploadUtils.string2IntArray(UploadUtils.escString(tokens[i]), UploadUtils.COMMA_DELIMITER);
                }catch(Exception e){
                    return ROW_INVALID_ANSWER;
                }
                if( ans == null || ans.length == 0 )
                    return ROW_INVALID_ANSWER;
                
            } /*else if( colName.equalsIgnoreCase(StdColName[5]) ) { // AsHTML
                try{
                    asHTML = UploadUtils.checkYesNo(tokens[i]);
                }catch(cwException e){
                    return ROW_INVALID_ASHTML;
                }
                
            }*/ else if( colName.equalsIgnoreCase(StdColName[6]) ) { // Score
                try{
                    que_score = Integer.parseInt(tokens[i]);
					if( que_score < 1 || que_score > 100 ){
                    	return ROW_INVALID_SCORE;
                    }
                }catch(Exception e){
                    return ROW_INVALID_SCORE;
                }
                
            } else if( colName.equalsIgnoreCase(StdColName[7]) ) { // Difficulty
                try{
                    que.diff = Integer.parseInt( tokens[i] );
                    if( que.diff < 1 || que.diff > 3){
						return ROW_INVALID_DIFFICULTY;
                    }
                }catch(Exception e){
                    return ROW_INVALID_DIFFICULTY;
                }

            } else if( colName.equalsIgnoreCase(StdColName[8]) ) { // Duration
                try{
                	if( !UploadUtils.checkDecimal(tokens[i], 2)) {
                		return ROW_INVALID_DURATION;
                	}
                    que.dur = (Float.valueOf( tokens[i] )).floatValue();
					if( que.dur <= 0 ) {
						return ROW_INVALID_DURATION;
					}
                }catch(Exception e){
                    return ROW_INVALID_DURATION;
                }
            } else if( colName.equalsIgnoreCase(StdColName[9]) ) { // Status
                try{
                    que.onoff = UploadUtils.checkOnOff( tokens[i] );
                }catch(Exception e){
                    return ROW_INVALID_STATUS;
                }
            } else if( colName.equalsIgnoreCase(StdColName[10]) ) { // Description
                que.desc = UploadUtils.escString(tokens[i]);
                if( que.desc.length() > 1000 ) {
                	return ROW_INVALID_DESC;
                }
            } else if( colName.equalsIgnoreCase(StdColName[11]) ) { // Explanation
               for (int j=0; j<queOptionNum; j++) {
                    que.inter[0].opt[j].exp = cwUtils.esc4XML( UploadUtils.escString(tokens[i]));
               }
            } else if( colName.startsWith(StdOptionColName) ) { //Option
                int optNum = Integer.parseInt( (colName.substring(StdOptionColName.length(), colName.length())).trim() );                	
                try{
                    if( tokens[i] != null && tokens[i].trim().length() > 0 ) {
                    	tokens[i]=tokens[i].replaceAll("\n", " ");
                        que.inter[0].opt[optNum-1].cont = cwUtils.esc4XML(UploadUtils.escString(tokens[i]));
                        if( UploadUtils.containsNewLine(que.inter[0].opt[optNum-1].cont) ) {
							throw new Exception("Option with new line");
                        }
                        queOptionNum++;
                    }
                }catch(Exception e){
                	CommonLog.error(e.getMessage(),e);
                    return ROW_INVALID_OPTION;
                }
            } else if ( colName.equalsIgnoreCase(StdColName[12]) ) { //system_id
                try {
                    String tempToken = tokens[i];
                    if (tempToken!=null && tempToken.length() > 0) {
                        que.resource_id = Long.parseLong(tokens[i]);
                        if (que.resource_id <= 0) {
                            return ROW_INVALID_SYSTEM_ID;
                        }
                        if ((!dbQuestion.queIDExist(con, que.resource_id, dbQuestion.QUE_TYPE_MULTI) || !allow_upd) 
                            && que.resource_id > 0) {
                            return ROW_INVALID_SYSTEM_ID;
                        }
                        //check if it will cover a sub_que of sc
                        if (dbQuestion.IsQueInSc(con, que.resource_id)) {
                            return ROW_INVALID_SYSTEM_ID;
                        } else {
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
        
        if( queOptionNum < 2 )
            return ROW_INVALID_OPTION;
            
        for(int i=queOptionNum; i<optionNum; i++){
            if( que.inter[0].opt[i].cont != null ){
                return ROW_INVALID_OPTION;
            }
        }
        
        for(int i=0; i<ans.length; i++){
            if( ans[i] > queOptionNum )
                return ROW_INVALID_ANSWER;
            else {
                que.inter[0].opt[ans[i] - 1].score = que_score;
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
        que.inter[0].type = dbQuestion.QUE_TYPE_MULTI;
        if( ans.length == 1 )
            que.inter[0].logic = extendQue.MC_LOGIC_SINGLE;
        else 
            que.inter[0].logic = extendQue.MC_LOGIC_AND;
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
    
    private Vector parseInValidColHeader(String colHeader)
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
                if( tokens[i].startsWith(StdOptionColName) ){
					boolean flag = true;
					try{
						Integer.parseInt( (tokens[i].substring(StdOptionColName.length(), tokens[i].length())).trim() );
					}catch(NumberFormatException e){
						flag = false;
					}
                    if( flag ) {
	                    this.inColHeader.addElement(tokens[i]);
    		            this.optionNum++;
            	        continue nextCol;
                    }
                }
                invalidColVec.addElement(tokens[i]);
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
		String foldId = "Folder ID", title = "Title", question = "Question", shuffle = "Shuffle";
		String answer = "Answer", asHtml = "AsHTML", score = "Score";
		String difficulty = "Difficulty", duration = "Duration", status = "Status";
		String description = "Description", explanation = "Explanation", resourceID = "Resource ID";

		String lang = prof.cur_lan;  
		foldId = ImportTemplate.getLabelValue("wb_imp_tem_folder_id", lang, foldId).trim();
		title = ImportTemplate.getLabelValue("wb_imp_tem_title", lang, title).trim();
		question = ImportTemplate.getLabelValue("wb_imp_tem_question", lang, question).trim();
		shuffle = ImportTemplate.getLabelValue("wb_imp_tem_shuffle", lang, shuffle).trim();
		answer = ImportTemplate.getLabelValue("wb_imp_tem_answer", lang, answer).trim();
		asHtml = ImportTemplate.getLabelValue("wb_imp_tem_as_html", lang, asHtml).trim();
		score = ImportTemplate.getLabelValue("wb_imp_tem_score", lang, score).trim();
		difficulty = ImportTemplate.getLabelValue("wb_imp_tem_difficulty", lang, difficulty).trim();
		duration = ImportTemplate.getLabelValue("wb_imp_tem_option", lang, duration).trim();
		status = ImportTemplate.getLabelValue("wb_imp_tem_status", lang, status).trim();
		description = ImportTemplate.getLabelValue("wb_imp_tem_description", lang, description).trim();
		explanation = ImportTemplate.getLabelValue("wb_imp_tem_explanation", lang, explanation).trim();
		resourceID = ImportTemplate.getLabelValue("wb_imp_tem_resource_id", lang, resourceID).trim();

		String[] labels = new String[] { foldId, title, question, shuffle, answer, asHtml, score,
				difficulty, duration, status, description, explanation, resourceID};
		return labels;
	}  
    
    private String[] getRequiredLabelArray(loginProfile prof) {   	    	       	    
		String foldId = "Folder ID", title = "Title", question = "Question";
		String option1 = "Option 1", option2 = "Option 2", shuffle = "Shuffle";
		String answer = "Answer", score = "Score";//, asHtml = "AsHTML"
		String difficulty = "Difficulty", status = "Status";

		String lang = prof.cur_lan;  
		foldId = ImportTemplate.getLabelValue("wb_imp_tem_folder_id", lang, foldId).trim();
		title = ImportTemplate.getLabelValue("wb_imp_tem_title", lang, title).trim();
		question = ImportTemplate.getLabelValue("wb_imp_tem_question", lang, question).trim();
		option1 = ImportTemplate.getLabelValue("wb_imp_tem_option_1", lang, option1).trim();
		option2 = ImportTemplate.getLabelValue("wb_imp_tem_option_2", lang, option2).trim();
		shuffle = ImportTemplate.getLabelValue("wb_imp_tem_shuffle", lang, shuffle).trim();
		answer = ImportTemplate.getLabelValue("wb_imp_tem_answer", lang, answer).trim();
		//asHtml = ImportTemplate.getLabelValue("wb_imp_tem_as_html", lang, asHtml).trim();
		score = ImportTemplate.getLabelValue("wb_imp_tem_score", lang, score).trim();
		difficulty = ImportTemplate.getLabelValue("wb_imp_tem_difficulty", lang, difficulty).trim();
		status = ImportTemplate.getLabelValue("wb_imp_tem_status", lang, status).trim();

		String[] labels = new String[] { foldId, title, question, option1, option2, shuffle, answer,  score,//asHtml,
				difficulty, status};
		return labels;
	}    
    
    private void setOtherLabel(loginProfile prof) {	
		String optionStr = "Option ";
		String lang = prof.cur_lan;
		optionStr = ImportTemplate.getLabelValue("wb_imp_tem_option", lang, optionStr).trim() + " ";
	    StdOptionColName = optionStr;
	}
}