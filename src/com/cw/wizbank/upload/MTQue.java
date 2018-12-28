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

public class MTQue{
	//UnicodeLittle:excel unicode encoding
    private final static String DEFAULT_ENC = "UnicodeLittle";
    private final static String DEFAULT_LANGUAGE = "ISO-8859-1";
    private String DEFAULT_FOLDER = "CW";

    // delimiter for scores/answers column
    private final static String DELIMITER = ",";
    
    // hashmap key
    private static final String KEY_VECTOR = "VECTOR";
    private static final String KEY_TYPE = "TYPE";
    
    //Log filename
    public final static String[] FILENAME_LIST = { UploadUtils.SUCCESS_FILE, UploadUtils.FAILURE_FILE, UploadUtils.ERROR_FILE};
    public final static String[] LOG_LIST = {"SUCCESS", "UNSUCCESS", "ERROR" };
    
    //History Session Key
    private static final String SESS_LOG_HISTORY_LOG_ID = "SESS_LOG_HISTORY_LOG_ID";
    private static final String SESS_LOG_HISTORY_TIMESTAMP = "SESS_LOG_HISTORY_TIMESTAMP";
    private static final String SESS_LOG_HISTORY_TOTAL_REC = "SESS_LOG_HISTORY_TOTAL_REC";
    private static final String SESS_LOG_HISTORY_TOTAL_PAGE = "SESS_LOG_HISTORY_TOTAL_PAGE";
    
    // Key to identify the parsing error
    private final static int VALID_QUE = 0;
    private final static int INVALID_CATEGORY_ID = 1;
    private final static int INVALID_TITLE = 2;
    private final static int INVALID_QUESTION = 3;
    private final static int INVALID_ASHTML = 4;
    private final static int INVALID_SOURCE = 5;
    private final static int INVALID_TARGET = 6;
    private final static int INVALID_ANSWERS = 7;
    private final static int INVALID_SCORES = 8;
    private final static int INVALID_DIFFICULTY = 9;
    private final static int INVALID_DURATION = 10;
    private final static int INVALID_STATUS = 11;
    private final static int INVALID_DESCRIPTION = 12;
    private final static int INVALID_RESOURCE_ID = 13;
    private final static int INVALID_PERMISSION = 14;
    
    private final static int OPT_NUMBER_ALLOWED = 10; 
    
    // Hashmap to store the incorrect row and its error
    private HashMap invalidMap = new HashMap();
    private Connection con = null;
    private long site_id;
    private String cur_role = null;
    private String usr_id = null;
    private qdbEnv static_env = null;
    private int numOption[] = new int[2];   // this is refer to optColName

    private Vector cachedValidCategoryIdVec = null;
    private Vector cachedInvalidCategoryIdVec = null;

    private static final String COL_CATEGORY_ID = "Folder ID";
    private static final String COL_RESOURCE_ID = "Resource ID";
	private static final String COL_TITLE = "Title";
	private static final String COL_QUESTION = "Question";
	private static final String COL_AS_HTML = "AsHTML";
	private static final String COL_ANSWERS = "Answers";
	private static final String COL_SCORES = "Scores";
	private static final String COL_DIFFICULTY = "Difficulty";
	private static final String COL_STATUS = "Status";
	private static final String COL_DESCRIPTION = "Description";
	
    private String[] stdColName = {
    	COL_CATEGORY_ID, 
    	COL_TITLE, 
    	COL_QUESTION, 
    	//COL_AS_HTML, 
    	COL_ANSWERS, 
    	COL_SCORES, 
    	COL_DIFFICULTY, 
    	COL_STATUS, 
    	COL_DESCRIPTION,
        COL_RESOURCE_ID
        };
    
    private String[] reqColName = {
		COL_CATEGORY_ID, 
		COL_TITLE, 
		COL_QUESTION, 
		COL_AS_HTML, 
		COL_ANSWERS, 
		COL_SCORES, 
		COL_DIFFICULTY, 
		COL_STATUS};
    
    private String[] optColName = {
        "Source ",
        "Target "
    };
	
    private Vector colHeader = null;
    private Vector dupQueId = null;
    
    public MTQue(Connection con, qdbEnv static_env, loginProfile prof){
    	//set label
    	this.stdColName = this.getLabelArray(prof);
        this.reqColName = this.getReqLabelArray(prof);
        this.optColName = this.getOptLabelArray(prof);
    	
        this.con = con;
        this.site_id = prof.root_ent_id;
        this.usr_id = prof.usr_id;
        this.cur_role = prof.current_role;
        this.static_env = static_env;
        this.colHeader = new Vector();

        this.invalidMap.put(new Integer(INVALID_CATEGORY_ID), new Vector());
        this.invalidMap.put(new Integer(INVALID_TITLE), new Vector());
        this.invalidMap.put(new Integer(INVALID_QUESTION), new Vector());
        this.invalidMap.put(new Integer(INVALID_ASHTML), new Vector());
        this.invalidMap.put(new Integer(INVALID_SOURCE), new Vector());
        this.invalidMap.put(new Integer(INVALID_TARGET), new Vector());
        this.invalidMap.put(new Integer(INVALID_ANSWERS), new Vector());
        this.invalidMap.put(new Integer(INVALID_SCORES), new Vector());
        this.invalidMap.put(new Integer(INVALID_DIFFICULTY), new Vector());
        this.invalidMap.put(new Integer(INVALID_DURATION), new Vector());
        this.invalidMap.put(new Integer(INVALID_STATUS), new Vector());
        this.invalidMap.put(new Integer(INVALID_DESCRIPTION), new Vector());
        this.invalidMap.put(new Integer(INVALID_RESOURCE_ID), new Vector());
        this.invalidMap.put(new Integer(INVALID_PERMISSION), new Vector());
        
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
            long ulg_id = UploadUtils.insUploadLog(con, this.usr_id, ulg_desc, srcFile.getName(), static_env, dbQuestion.QUE_TYPE_MATCHING);
            BufferedReader in = new BufferedLineReader(new InputStreamReader(new FileInputStream(srcFile), DEFAULT_ENC)); 
            
            // parse and validate the column header
			String colHeaderLine = in.readLine();
            if (colHeaderLine == null) {
                in.close();
                throw new cwSysMessage(UploadUtils.SMSG_ULG_INVALID_FILE);
            }
			HashMap invalidHeaderMap = parseColHeader(colHeaderLine);
			if(!invalidHeaderMap.isEmpty()) {
				Vector invalidColHeader = (Vector) invalidHeaderMap.get(KEY_VECTOR);
				String type = (String) invalidHeaderMap.get(KEY_TYPE);
				return UploadUtils.invalidColXML(invalidColHeader, type);
			}

            // parse for every question
            int rowNum = 1;
            String row = null;
            boolean errorFlag = false;
            int rowLimit = wizbini.cfgSysSetupadv.getResBatchUpload().getMaxUploadCount();
            while ((row = in.readLine()) != null) {
                if(row!="" && row.length()>0 ) {
                    Vector invalidVector = new Vector();
                
                    //if number of records more than row limit,throw a message.
                    if (rowNum > rowLimit + 1) {
                        throw new cwSysMessage(UploadUtils.SMSG_ULG_UPLOAD_COUNT_EXCEED_LIMIT, new Integer(rowLimit).toString());
                    }

                    rowNum++;
                    extendQue parsedQue = new extendQue(1, numInt(row), numInt(row), numOption[0], numOption[1]);
                    parsedQue.media_width = 80;
                    parsedQue.media_height = 80;
                    parsedQue.cont_hint = true;
                    if(row != null){
                    	//由于excel文件有些现在还不知道的格式另存为txt文件后，会使格内容换行，所惟去掉
                		row = row.replaceAll(UploadUtils.colDelimiter_ + "\"" +UploadUtils.colDelimiter_ ,  UploadUtils.colDelimiter_  + "\"");
                	}
                    switch ( populate(parsedQue, row, allow_upd,wizbini.cfgTcEnabled) ) {
                        case VALID_QUE:
                            try{
                        	    parsedQue.queType = dbQuestion.QUE_TYPE_MATCHING;
                                RawQuestion.uploadQue(con, parsedQue, ulg_id, rowNum, numInt(row));
                            }catch(Exception e){
                            	CommonLog.error(e.getMessage(),e);
                            }
                            break;
                        case INVALID_CATEGORY_ID:
                            errorFlag = true;
                            invalidVector = (Vector) invalidMap.get(new Integer(INVALID_CATEGORY_ID));
                            invalidVector.addElement(new Integer(rowNum));
                            invalidMap.put(new Integer(INVALID_CATEGORY_ID), invalidVector);
                            break;
                        case INVALID_TITLE:
                            errorFlag = true;
                            invalidVector = (Vector) invalidMap.get(new Integer(INVALID_TITLE));
                            invalidVector.addElement(new Integer(rowNum));
                            invalidMap.put(new Integer(INVALID_TITLE), invalidVector);
                            break;
                        case INVALID_QUESTION:
                            errorFlag = true;
                            invalidVector = (Vector) invalidMap.get(new Integer(INVALID_QUESTION));
                            invalidVector.addElement(new Integer(rowNum));
                            invalidMap.put(new Integer(INVALID_QUESTION), invalidVector);
                            break;
                        case INVALID_ASHTML:
                            errorFlag = true;
                            invalidVector = (Vector) invalidMap.get(new Integer(INVALID_ASHTML));
                            invalidVector.addElement(new Integer(rowNum));
                            invalidMap.put(new Integer(INVALID_ASHTML), invalidVector);
                            break;
                        case INVALID_SOURCE:
                            errorFlag = true;
                            invalidVector = (Vector) invalidMap.get(new Integer(INVALID_SOURCE));
                            invalidVector.addElement(new Integer(rowNum));
                            invalidMap.put(new Integer(INVALID_SOURCE), invalidVector);
                            break;
                        case INVALID_TARGET:
                            errorFlag = true;
                            invalidVector = (Vector) invalidMap.get(new Integer(INVALID_TARGET));
                            invalidVector.addElement(new Integer(rowNum));
                            invalidMap.put(new Integer(INVALID_TARGET), invalidVector);
                            break;
                        case INVALID_ANSWERS:
                            errorFlag = true;
                            invalidVector = (Vector) invalidMap.get(new Integer(INVALID_ANSWERS));
                            invalidVector.addElement(new Integer(rowNum));
                            invalidMap.put(new Integer(INVALID_ANSWERS), invalidVector);
                            break;
                        case INVALID_SCORES:
                            errorFlag = true;
                            invalidVector = (Vector) invalidMap.get(new Integer(INVALID_SCORES));
                            invalidVector.addElement(new Integer(rowNum));
                            invalidMap.put(new Integer(INVALID_SCORES), invalidVector);
                            break;
                        case INVALID_DIFFICULTY:
                            errorFlag = true;
                            invalidVector = (Vector) invalidMap.get(new Integer(INVALID_DIFFICULTY));
                            invalidVector.addElement(new Integer(rowNum));
                            invalidMap.put(new Integer(INVALID_DIFFICULTY), invalidVector);
                            break;
                        case INVALID_DURATION:
                            errorFlag = true;
                            invalidVector = (Vector) invalidMap.get(new Integer(INVALID_DURATION));
                            invalidVector.addElement(new Integer(rowNum));
                            invalidMap.put(new Integer(INVALID_DURATION), invalidVector);
                            break;
                        case INVALID_STATUS:
                            errorFlag = true;
                            invalidVector = (Vector) invalidMap.get(new Integer(INVALID_STATUS));
                            invalidVector.addElement(new Integer(rowNum));
                            invalidMap.put(new Integer(INVALID_STATUS), invalidVector);
                            break;
                        case INVALID_DESCRIPTION:
                            errorFlag = true;
                            invalidVector = (Vector) invalidMap.get(new Integer(INVALID_DESCRIPTION));
                            invalidVector.addElement(new Integer(rowNum));
                            invalidMap.put(new Integer(INVALID_DESCRIPTION), invalidVector);
                            break;
                        case INVALID_RESOURCE_ID:
                            errorFlag = true;
                            invalidVector = (Vector) invalidMap.get(new Integer(INVALID_RESOURCE_ID));
                            invalidVector.addElement(new Integer(rowNum));
                            invalidMap.put(new Integer(INVALID_RESOURCE_ID), invalidVector);
                            break;
                        case INVALID_PERMISSION:
                            errorFlag = true;
                            invalidVector = (Vector) invalidMap.get(new Integer(INVALID_PERMISSION));
                            invalidVector.addElement(new Integer(rowNum));
                            invalidMap.put(new Integer(INVALID_PERMISSION), invalidVector);
                            break;
                        default:
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

    private int numAnswers(String row) throws SQLException{
        String[] tokens = cwUtils.splitToString(row, (new Character(UploadUtils.colDelimiter_)).toString());
        int col=0;
        int num=0;
        Vector answersVec = new Vector();
        for(int i=0; i<tokens.length; i++){
            String colName = (String)colHeader.elementAt(col);
            if( colName.startsWith(stdColName[4]) ) { // answers
                if( tokens[i] != null && tokens[i].length() > 0 ) {
                    answersVec = parseAnsScoreColumnMt(UploadUtils.escString(tokens[i]), false);
                    num=answersVec.size();
                }
            }
            col++;
        }
        return num;
    }

    private int numInt(String row) throws SQLException {
        if(numSource(row) > numTarget(row)) {
            return numSource(row);
        } else {
            return numTarget(row);
        }
    }
    private int numSource(String row) throws SQLException{
        String[] tokens = cwUtils.splitToString(row, (new Character(UploadUtils.colDelimiter_)).toString());
        int col=0;
        int num=0;
        for(int i=0; i<tokens.length; i++){
            String colName = (String)colHeader.elementAt(col);
            if( colName.startsWith(optColName[0]) ) { // source
                if( tokens[i] != null && tokens[i].trim().length() > 0 ) {
                    num++;
                }
            }
            col++;
        }
        return num;
    }
    
    private int numTarget(String row) throws SQLException{
        String[] tokens = cwUtils.splitToString(row, (new Character(UploadUtils.colDelimiter_)).toString());
        int col=0;
        int num=0;
        for(int i=0; i<tokens.length; i++){
            String colName = (String)colHeader.elementAt(col);
            if( colName.startsWith(optColName[1]) ) { // target
                if( tokens[i] != null && tokens[i].trim().length() > 0 ) {
                    num++;
                }
            }
            col++;
        }
        return num;
    }
    

    private int populate(extendQue que, String row, boolean allow_upd, boolean tc_enabled) throws SQLException{
        String[] tokens = cwUtils.splitToString(row, (new Character(UploadUtils.colDelimiter_)).toString());
        int col = 0;
        int numSource = 0;
        int numTarget = 0;
        int numAnswers = 0;
        int numScores = 0;
        HashSet sourceSet = new HashSet();
        HashSet targetSet = new HashSet();
        String asHTML = UploadUtils.YES;
        int que_score = 0;
        boolean sourceEndFlag = false;
        boolean targetEndFlag = false;
        Vector answersVec = new Vector();
        Vector scoresVec = new Vector();

        long usr_ent_id = 0;
        try {
            usr_ent_id = dbRegUser.getEntId(con, usr_id);
        } catch (qdbException e) {
            throw new SQLException();
        }

        for(int i=0; i<tokens.length; i++){
            String colName = (String)colHeader.elementAt(col);
            String token = tokens[i].trim();
            if( (colName).equalsIgnoreCase(stdColName[0]) ) { //Category ID
                try {
                    if( categoryIdExist(Long.parseLong(token)) ) {
                        que.obj_id[0] = Long.parseLong(token);
                        if(tc_enabled) {
                        	AcTrainingCenter acTc = new AcTrainingCenter(con);
                        	if(!acTc.hasObjInMgtTc(usr_ent_id, que.obj_id[0], cur_role).equals(dbObjective.CAN_MGT_OBJ)) {
                        		  return INVALID_PERMISSION;
                        	}
                        }
                        AcResources acRes = new AcResources(con);
                        if (!acRes.hasManagePrivilege(usr_ent_id, que.obj_id[0], cur_role)) {
                            return INVALID_PERMISSION;
                        }
                    }
                    else
                        return INVALID_CATEGORY_ID;
                } catch (Exception e) {
                    return INVALID_CATEGORY_ID;
                }

            } else if( colName.equalsIgnoreCase(stdColName[1]) ) { // Title
                que.title = UploadUtils.escString(token);
                if( que.title == null || que.title.length() == 0 || CharUtils.getStringLength(que.title) > 80 ||UploadUtils.containsNewLine(que.title)) {
                    return INVALID_TITLE;
				}
            } else if( colName.equalsIgnoreCase(stdColName[2]) ) { // Question
                que.cont = cwUtils.esc4XML(UploadUtils.escString(token));
                if( que.cont == null || que.cont.length() == 0)
                    return INVALID_QUESTION;
            } /*else if( colName.equalsIgnoreCase(stdColName[3]) ) { // AsHTML
                try{
                    asHTML = UploadUtils.checkYesNo(tokens[i]);
                }catch(cwException e){
                    return INVALID_ASHTML;
                }
            }*/ else if( colName.equalsIgnoreCase(stdColName[3]) ) { // Answers
                try{
                    if( numSource==0 || sourceSet.size()!=numSource) {
                        return INVALID_SOURCE;
                    }
                    if( numTarget==0 || targetSet.size()!=numTarget) {
                        return INVALID_TARGET;
                    }
                    answersVec = parseAnsScoreColumnMt(UploadUtils.escString(token), false);

                    numAnswers = answersVec.size();
                    // check the existense of a valid answer (not zero)
                    boolean invalidAnswerflag = false;
                    for(int k=0;k<answersVec.size();k++) {
                        String[] answers = (String[])answersVec.get(k);
                        for (int l=0;l<answers.length;l++) {
                            int interNum = Integer.parseInt(answers[l]);
                            if(interNum<0 || interNum>OPT_NUMBER_ALLOWED) {
                                invalidAnswerflag = true;
                                break;
                            }
                            int sum_answers = 0;
                            for(int s=0;s<answersVec.size();s++){
                                String[] u_answers = (String[])answersVec.get(s);
                            	for (int m=0;m<u_answers.length;m++) {
                                    int u_interNum = Integer.parseInt(u_answers[m]);
                                    if(interNum == u_interNum) {
                                    	sum_answers++;
                                    }
                            	 }
                              }
                            if(sum_answers > 1){
                        		invalidAnswerflag = true;
                                break;
                        	}
                            
                        }
                    }
                    
                    if(invalidAnswerflag) {
                        return INVALID_ANSWERS;
                    }
                        
                    if(numAnswers>this.numOption[0] || numAnswers == 0) {
                        return INVALID_ANSWERS;
                    }
                    HashSet aSet = new HashSet();
                    HashMap aMap = new HashMap();
                    int oNum = 0;
                    for(int k=0;k<answersVec.size();k++) {
                        String[] answers = (String[])answersVec.get(k);
                        for (int l=0;l<answers.length;l++) {
                            int interNum = Integer.parseInt(answers[l]);
                            if(interNum > numTarget(row)) {
                                return INVALID_ANSWERS;
                            }
                            if(interNum!=0) {
                                if(aSet.contains(new Integer(interNum))) {
                                    oNum = ((Integer) aMap.get(new Integer(interNum))).intValue();
                                    oNum++;
                                    que.inter[interNum-1].opt[oNum].cont = "" + (k+1);
                                    que.inter[interNum-1].opt[oNum].id = (k+1);
                                    aMap.put(new Integer(interNum), new Integer(oNum));
                                } else {
                                    oNum=0;
                                    que.inter[interNum-1].opt[oNum].cont = "" + (k+1);
                                    que.inter[interNum-1].opt[oNum].id = (k+1);
                                    aMap.put(new Integer(interNum), new Integer(oNum));
                                }
                            }
                            aSet.add(new Integer(interNum));
                        }
                    }
                }catch(Exception e){
                    return INVALID_ANSWERS;
                }
                
            } else if( colName.equalsIgnoreCase(stdColName[4]) ) { // Scores
                try{
                    scoresVec = parseAnsScoreColumnMt(UploadUtils.escString(token), true);
                    numScores = scoresVec.size();
                    if(numScores>this.numOption[0] || numScores == 0) {
                        return INVALID_SCORES;
                    }
                    
                    if(numScores != numAnswers) {
                        return INVALID_SCORES;
                    }

                    HashSet aSet = new HashSet();
                    HashMap aMap = new HashMap();
                    int oNum = 0;
                    for(int k=0;k<scoresVec.size();k++) {
                        String[] answers = (String[])scoresVec.get(k);
                        String[] temp = null;
                        for (int l=0;l<answers.length;l++) {
                            int score = Integer.parseInt(answers[l]);
                            if(score < 0 || score > 100) {
                                return INVALID_SCORES;
                            }
                            temp = (String[])answersVec.elementAt(k);
                            if(score != 0) {
                                int answer = Integer.parseInt(temp[l]);
                                if(answer==0) {
                                    return INVALID_SCORES;
                                }
                            }
                            int interNum = Integer.parseInt(temp[l]);
                            if(interNum!=0) {
                                if(aSet.contains(new Integer(interNum))) {
                                    oNum = ((Integer) aMap.get(new Integer(interNum))).intValue();
                                    oNum++;
                                    que.inter[interNum-1].opt[oNum].score = score;
                                    aMap.put(new Integer(interNum), new Integer(oNum));
                                } else {
                                    oNum=0;
                                    aMap.put(new Integer(interNum), new Integer(oNum));
                                    que.inter[interNum-1].opt[oNum].score = score;
                                }
                            }
                            aSet.add(new Integer(interNum));
                        }
                    }
                }catch(Exception e){
                    return INVALID_SCORES;
                }

            } else if( colName.equalsIgnoreCase(stdColName[5]) ) { // Difficulty
                try{
                    que.diff = Integer.parseInt( token );
                    if(que.diff < 1 ||que.diff > 3) {
                        return INVALID_DIFFICULTY;
                    }
                }catch(Exception e){
                    return INVALID_DIFFICULTY;
                }
                
            } else if( colName.equalsIgnoreCase(stdColName[6]) ) { // Status
                try{
                    que.onoff = UploadUtils.checkOnOff( token );
                }catch(Exception e){
                    return INVALID_STATUS;
                }
                
            } else if( colName.equalsIgnoreCase(stdColName[7]) ) { // Description
            	
                que.desc = UploadUtils.escString(token);
                if(que.desc.length()>1000) {
                	return INVALID_DESCRIPTION;
                }
                
            } else if( colName.startsWith(optColName[0]) ) { // source
                int optNum = Integer.parseInt( (colName.substring(optColName[0].length(), colName.length())).trim() );
                if(optNum <=0 || optNum > OPT_NUMBER_ALLOWED) {
                	return INVALID_SOURCE;
                }
                try{
                    if(sourceEndFlag && (token != null && token.trim().length() > 0)) {
                        return INVALID_SOURCE;
                    } else if(UploadUtils.containsNewLine(token)) {
						return INVALID_SOURCE;
                    } else if( token != null && token.trim().length() > 0 ) {
                        que.src_obj[optNum-1].text = cwUtils.esc4XML(UploadUtils.escString(token));
                        // the set is used to check for duplication
                        sourceSet.add(cwUtils.esc4XML(UploadUtils.escString(token)));
                        numSource++;
                    } else if (token.trim().length()==0) {
                        sourceEndFlag = true;
                        ;//skip
                    } else {
                        return INVALID_SOURCE;
                    }
                }catch(Exception e){
//                    e.printStackTrace(System.out);
                    return INVALID_SOURCE;
                }
            } else if( colName.startsWith(optColName[1]) ) { // target
                int optNum = Integer.parseInt( (colName.substring(optColName[0].length(), colName.length())).trim() );
				if(optNum <=0 || optNum > OPT_NUMBER_ALLOWED) {
					return INVALID_TARGET;
				}
                try{
                    if(targetEndFlag && (token != null && token.trim().length() > 0)) {
                        return INVALID_TARGET;
					} else if(UploadUtils.containsNewLine(token)) {
						return INVALID_TARGET;
                    } else if( token != null && token.trim().length() > 0 ) {
                        que.tgt_obj[optNum-1].text = cwUtils.esc4XML(UploadUtils.escString(token));
                        // the set is used to check for duplication
                        targetSet.add(cwUtils.esc4XML(UploadUtils.escString(token)));
                        numTarget++;
                    } else if (token.trim().length()==0) {
                        targetEndFlag = true;
                        ;//skip
                    } else {
                        return INVALID_TARGET;
                    }
                }catch(Exception e){
//                    e.printStackTrace(System.out);
                    return INVALID_TARGET;
                }
            } else if ( colName.equalsIgnoreCase(stdColName[8]) ) { //system_id
                try {
                    String tempToken = token;
                    if (tempToken!=null && tempToken.length() > 0) {
                        que.resource_id = Long.parseLong(token);
                        if (que.resource_id <= 0) {
                            return INVALID_RESOURCE_ID;
                        }
                        if ((!dbQuestion.queIDExist(con, que.resource_id, dbQuestion.QUE_TYPE_MATCHING) || !allow_upd) 
                            && que.resource_id > 0) {
                            return INVALID_RESOURCE_ID;
                        }
                        //check if have right to modify this question
                      	if(tc_enabled) {
             	        	AcTrainingCenter acTc = new AcTrainingCenter(con);
             	        	if(!acTc.hasResInMgtTc(usr_ent_id, que.resource_id, cur_role).equals(dbResource.CAN_MGT_RES)) {
             	        		return INVALID_PERMISSION;	
             	        	}
                      	}
                        AcResources acRes = new AcResources(con);
                        if (!acRes.hasResPrivilege(usr_ent_id, que.resource_id, cur_role)) {
                            return INVALID_PERMISSION;
                        }
                        //check if there are more than one question have the same resource_id.
                        Long temp_que_id_obj = new Long(que.resource_id);
                        if (dupQueId.contains(temp_que_id_obj)) {
                            return INVALID_RESOURCE_ID;
                        } else {
                            dupQueId.addElement(new Long(que.resource_id));
                        }
                    } else {
                        que.resource_id = 0;
                    }
                } catch (NumberFormatException e) {
                	CommonLog.error(e.getMessage(),e);
                    return INVALID_RESOURCE_ID;
                }
            } else {
                return -1;
            }
            col++;
        }
		
		

        if( numSource==0 || sourceSet.size()!=numSource) {
            return INVALID_SOURCE;
        }
        if( numTarget==0 || targetSet.size()!=numTarget) {
            return INVALID_TARGET;
        }
        if( asHTML.equalsIgnoreCase(UploadUtils.YES) ){
            que.cont_html = true;
            for(int i=0; i<numAnswers; i++) {
                for(int j=0; j<numInt(row); j++) {
                    que.inter[i].opt[j].cont_html = true;
                }
            }
        } else {
            que.cont_html = false;
            for(int i=0; i<numAnswers; i++) {
                for(int j=0; j<numInt(row); j++) {
                    que.inter[i].opt[j].cont_html = false;
                }
            }
        }
        
        
        que.lan = DEFAULT_LANGUAGE;
        que.folder = DEFAULT_FOLDER;
        for(int i=0; i<numInt(row); i++) {
            que.inter[i].type = dbQuestion.QUE_TYPE_MATCHING;
        }
        que.owner_id = this.usr_id;
        return VALID_QUE;

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

    // dump xml for the invalid rows
    private String invalidRowXML(){
        Vector invalidVec = new Vector();
        StringBuffer xml = new StringBuffer(512);
        xml.append("<invalid_list>");
        
        // INVALID_CATEGORY_ID
        invalidVec = (Vector)invalidMap.get(new Integer(INVALID_CATEGORY_ID));
        if( !invalidVec.isEmpty() ){
            xml.append("<invalid_category_lines>");
            for(int i=0; i<invalidVec.size(); i++)
                xml.append("<line num=\"").append(invalidVec.elementAt(i)).append("\"/>");
             xml.append("</invalid_category_lines>");
        }
        // INVALID_RESOURCE_ID
        invalidVec = (Vector)invalidMap.get(new Integer(INVALID_RESOURCE_ID));
        if( !invalidVec.isEmpty() ){
            xml.append("<invalid_resource_lines>");
            for(int i=0; i<invalidVec.size(); i++)
                xml.append("<line num=\"").append(invalidVec.elementAt(i)).append("\"/>");
             xml.append("</invalid_resource_lines>");
        }
        // INVALID_TITLE
        invalidVec = (Vector)invalidMap.get(new Integer(INVALID_TITLE));
        if( !invalidVec.isEmpty() ){
            xml.append("<invalid_title_lines>");
            for(int i=0; i<invalidVec.size(); i++)
                xml.append("<line num=\"").append(invalidVec.elementAt(i)).append("\"/>");
             xml.append("</invalid_title_lines>");
        }
        // INVALID_QUESTION
        invalidVec = (Vector)invalidMap.get(new Integer(INVALID_QUESTION));
        if( !invalidVec.isEmpty() ){
            xml.append("<invalid_question_lines>");
            for(int i=0; i<invalidVec.size(); i++)
                xml.append("<line num=\"").append(invalidVec.elementAt(i)).append("\"/>");
             xml.append("</invalid_question_lines>");
        }
        // INVALID_ASHTML
        invalidVec = (Vector)invalidMap.get(new Integer(INVALID_ASHTML));
        if( !invalidVec.isEmpty() ){
            xml.append("<invalid_ashtml_lines>");
            for(int i=0; i<invalidVec.size(); i++)
                xml.append("<line num=\"").append(invalidVec.elementAt(i)).append("\"/>");
             xml.append("</invalid_ashtml_lines>");
        }
        // INVALID_SOURCE
        invalidVec = (Vector)invalidMap.get(new Integer(INVALID_SOURCE));
        if( !invalidVec.isEmpty() ){
            xml.append("<invalid_source_lines>");
            for(int i=0; i<invalidVec.size(); i++)
                xml.append("<line num=\"").append(invalidVec.elementAt(i)).append("\"/>");
             xml.append("</invalid_source_lines>");
        }
        // INVALID_TARGET
        invalidVec = (Vector)invalidMap.get(new Integer(INVALID_TARGET));
        if( !invalidVec.isEmpty() ){
            xml.append("<invalid_target_lines>");
            for(int i=0; i<invalidVec.size(); i++)
                xml.append("<line num=\"").append(invalidVec.elementAt(i)).append("\"/>");
             xml.append("</invalid_target_lines>");
        }
        // INVALID_ANSWERS
        invalidVec = (Vector)invalidMap.get(new Integer(INVALID_ANSWERS));
        if( !invalidVec.isEmpty() ){
            xml.append("<invalid_answers_lines>");
            for(int i=0; i<invalidVec.size(); i++)
                xml.append("<line num=\"").append(invalidVec.elementAt(i)).append("\"/>");
             xml.append("</invalid_answers_lines>");
        }
        // INVALID_SCORES
        invalidVec = (Vector)invalidMap.get(new Integer(INVALID_SCORES));
        if( !invalidVec.isEmpty() ){
            xml.append("<invalid_scores_lines>");
            for(int i=0; i<invalidVec.size(); i++)
                xml.append("<line num=\"").append(invalidVec.elementAt(i)).append("\"/>");
             xml.append("</invalid_scores_lines>");
        }
        // INVALID_DIFFICULTY
        invalidVec = (Vector)invalidMap.get(new Integer(INVALID_DIFFICULTY));
        if( !invalidVec.isEmpty() ){
            xml.append("<invalid_difficulty_lines>");
            for(int i=0; i<invalidVec.size(); i++)
                xml.append("<line num=\"").append(invalidVec.elementAt(i)).append("\"/>");
             xml.append("</invalid_difficulty_lines>");
        }
        // INVALID_DURATION
        invalidVec = (Vector)invalidMap.get(new Integer(INVALID_DURATION));
        if( !invalidVec.isEmpty() ){
            xml.append("<invalid_duration_lines>");
            for(int i=0; i<invalidVec.size(); i++)
                xml.append("<line num=\"").append(invalidVec.elementAt(i)).append("\"/>");
             xml.append("</invalid_duration_lines>");
        }
        // INVALID_STATUS
        invalidVec = (Vector)invalidMap.get(new Integer(INVALID_STATUS));
        if( !invalidVec.isEmpty() ){
            xml.append("<invalid_status_lines>");
            for(int i=0; i<invalidVec.size(); i++)
                xml.append("<line num=\"").append(invalidVec.elementAt(i)).append("\"/>");
             xml.append("</invalid_status_lines>");
        }
        // INVALID_DESCRIPTION
        invalidVec = (Vector)invalidMap.get(new Integer(INVALID_DESCRIPTION));
        if( !invalidVec.isEmpty() ){
            xml.append("<invalid_description_lines>");
            for(int i=0; i<invalidVec.size(); i++)
                xml.append("<line num=\"").append(invalidVec.elementAt(i)).append("\"/>");
             xml.append("</invalid_description_lines>");
        }
        invalidVec = (Vector)invalidMap.get(new Integer(INVALID_PERMISSION));
        if( !invalidVec.isEmpty() ){
            xml.append("<invalid_permission_lines>");
            for(int i=0; i<invalidVec.size(); i++)
                xml.append("<line num=\"").append(invalidVec.elementAt(i)).append("\"/>");
             xml.append("</invalid_permission_lines>");
        }
        xml.append("</invalid_list>");
        return xml.toString();
    }
        
    private String uploadedQueXML(long ulg_id) throws cwSysMessage, SQLException{
        StringBuffer xml = new StringBuffer();
        xml.append("<used_column>");
        for (int k = 0; k < this.colHeader.size(); k++){
            xml.append("<column id=\"" + k + "\">").append(this.colHeader.elementAt(k)).append("</column>");
        }      
        xml.append("</used_column>");
        xml.append(RawQuestion.asXML(con, ulg_id));
        return xml.toString();
    }

    // to parse the colHeader 
    private HashMap parseColHeader(String colHeaderLine)
            throws cwException, IOException {
            	
        HashMap invalidMap = new HashMap();
        Vector invalidColVec = new Vector();
        String[] tokens = cwUtils.splitToString(colHeaderLine, (new Character(UploadUtils.colDelimiter_)).toString());

        nextCol:            
        for(int i=0; i<tokens.length; i++){
            for(int j=0; j<stdColName.length; j++) {
                if( tokens[i].equalsIgnoreCase(stdColName[j]) ){
                    this.colHeader.addElement(tokens[i].trim());
                    continue nextCol;
                }
            }
            for(int j=0; j<optColName.length; j++) {
                if( tokens[i].startsWith(optColName[j]) ){

					int colOptNum = Integer.parseInt( (tokens[i].substring(optColName[j].length(), tokens[i].length())).trim() );
					if(colOptNum <1 || colOptNum > OPT_NUMBER_ALLOWED) {
						// check to ensure the option column are within range
						if(!invalidColVec.contains(tokens[i])) {
							invalidColVec.addElement(tokens[i]);
						}
					} else {
						this.colHeader.addElement(tokens[i].trim());
						this.numOption[j]++;
						continue nextCol;
					}
                }
            }
			if(!invalidColVec.contains(tokens[i])) {
	            invalidColVec.addElement(tokens[i]);
			}
        }
        if(!invalidColVec.isEmpty()) {
        	invalidMap.put(KEY_VECTOR, invalidColVec);
        	invalidMap.put(KEY_TYPE, UploadUtils.INVALID_COL_TYPE_UNRECOGNIZED);
        	return invalidMap;
        }
         
        // loop every type of option column, ie.: source , target
        // check option column for duplicate 
        for(int j=0; j<optColName.length; j++) {
            Vector existedOptionNum = new Vector();
            for(int i=0; i<this.colHeader.size(); i++){
                String colName = (String)this.colHeader.elementAt(i);
                if( (colName).startsWith(optColName[j]) ){
                    // the option number in the column
                    int colOptNum = Integer.parseInt( (colName.substring(optColName[j].length(), colName.length())).trim() );
                    if( colOptNum > numOption[j] ) {
						if(!invalidColVec.contains(colName)) {
	                        invalidColVec.addElement(colName);
						}
                    } else if( existedOptionNum.indexOf(new Integer(colOptNum)) != -1 ) {
						if(!invalidColVec.contains(colName)) {
	                        invalidColVec.addElement(colName);
						}
                    } else {
                        existedOptionNum.addElement(new Integer(colOptNum));
                    }
                }
            }
        }

		if(!invalidColVec.isEmpty()) {
			invalidMap.put(KEY_VECTOR, invalidColVec);
			invalidMap.put(KEY_TYPE, UploadUtils.INVALID_COL_TYPE_DUPLICATE);
			return invalidMap;
		}
        
        // check for consistence between opt column
        int numOpt = -1;
        for(int j=0; j<optColName.length; j++) {
            if(numOpt == -1) {
                numOpt = numOption[j];
            }
            if(numOpt != numOption[j]) {
				if(!invalidColVec.contains(optColName[j])) {
	                invalidColVec.addElement(optColName[j]);
				}
            }
        }
		if(!invalidColVec.isEmpty()) {
			invalidMap.put(KEY_VECTOR, invalidColVec);
			invalidMap.put(KEY_TYPE, UploadUtils.INVALID_COL_TYPE_UNRECOGNIZED);
			return invalidMap;
		}
        
        // check for required column;
        for(int j=0; j<reqColName.length; j++) {
        	if(!colHeader.contains(reqColName[j])) {
        		invalidColVec.addElement(reqColName[j]);
        	}
        }
		if(!invalidColVec.isEmpty()) {
			invalidMap.put(KEY_VECTOR, invalidColVec);
			invalidMap.put(KEY_TYPE, UploadUtils.INVALID_COL_TYPE_MISSING);
			return invalidMap;
		}
        
        return invalidMap;
    }


    // to parse the answer/score column
    // return empty if exception
    private Vector parseAnsScoreColumnMt(String col, boolean allowDuplicate) {
        Vector retVec = new Vector();
        Vector ansVec = new Vector();
        ansVec = cwUtils.splitToVecString(col, UploadUtils.COMMA_DELIMITER);
        Iterator ans = ansVec.iterator();
        Vector tempAns = null;
        while(ans.hasNext()) {
            String aToken = ((String)ans.next()).trim();
            if (aToken.indexOf(UploadUtils.MT_PLUS_DELIMITER) != -1) {
                tempAns = cwUtils.splitToVecString(aToken, UploadUtils.MT_PLUS_DELIMITER);
            } else {
                tempAns = new Vector();
                tempAns.addElement(aToken);
            }
            
            try {
                Iterator tempAnsIter = tempAns.iterator();
                Vector nonDupAns = new Vector();
                while (tempAnsIter.hasNext()) {
                    String token = ((String)tempAnsIter.next()).trim();
                    if (token.length() != 0 && !nonDupAns.contains(token)) {
                        nonDupAns.addElement(token);
                    } else {
                        if (allowDuplicate) {
                            nonDupAns.addElement(token);
                        } else {
                            nonDupAns.addElement(new String("0"));
                        }
                    }
                }
                retVec.addElement(cwUtils.vec2strArray(nonDupAns));
            } catch (Exception e) {
                return (new Vector());                
            }
        }
        return retVec;
    }

    public String save2DB(DbUploadLog ulg, loginProfile prof, boolean tc_enabled)
            throws cwException, SQLException, IOException {
        
        RawQuestion rawQue = new RawQuestion();
        Hashtable h_cnt = rawQue.save2DB(this.con, this.static_env, this.site_id, ulg.ulg_id, prof, tc_enabled);
        ulg.ulg_status = DbUploadLog.STATUS_COMPLETED;
        ulg.ulg_upd_datetime = cwSQL.getTime(this.con);
        ulg.updStatus(this.con);
        DbRawQuestion.del(this.con, ulg.ulg_id);
        
        return UploadUtils.getResultXML(static_env, ulg.ulg_id, h_cnt);
        
    }
    
    private String[] getLabelArray(loginProfile prof) {
		String foldId = "Folder ID", title = "Title", question = "Question"; //, asHtml = "AsHTML"
		String answer = "Answers", scores = "Scores", difficulty = "Difficulty";
		String status = "Status", description = "Description", resourceID = "Resource ID";

		String lang = prof.cur_lan;
		foldId = ImportTemplate.getLabelValue("wb_imp_tem_folder_id", lang, foldId).trim();
		title = ImportTemplate.getLabelValue("wb_imp_tem_title", lang, title).trim();
		question = ImportTemplate.getLabelValue("wb_imp_tem_question", lang, question).trim();
		//asHtml = ImportTemplate.getLabelValue("wb_imp_tem_as_html", lang, asHtml).trim();
		answer = ImportTemplate.getLabelValue("wb_imp_tem_answers", lang, answer).trim();
		scores = ImportTemplate.getLabelValue("wb_imp_tem_scores", lang, scores).trim();
		difficulty = ImportTemplate.getLabelValue("wb_imp_tem_difficulty", lang, difficulty).trim();
		status = ImportTemplate.getLabelValue("wb_imp_tem_status", lang, status).trim();
		description = ImportTemplate.getLabelValue("wb_imp_tem_description", lang, description).trim();
		resourceID = ImportTemplate.getLabelValue("wb_imp_tem_resource_id", lang, resourceID).trim();

		String[] labels = new String[] { foldId, title, question, answer, scores, difficulty, status, description, resourceID };
		return labels;
	}

	private String[] getReqLabelArray(loginProfile prof) {
		String foldId = "Folder ID", title = "Title", question = "Question"; //, asHtml = "AsHTML"
		String answer = "Answers", scores = "Scores", difficulty = "Difficulty";
		String status = "Status";

		String lang = prof.cur_lan;
		foldId = ImportTemplate.getLabelValue("wb_imp_tem_folder_id", lang, foldId).trim();
		title = ImportTemplate.getLabelValue("wb_imp_tem_title", lang, title).trim();
		question = ImportTemplate.getLabelValue("wb_imp_tem_question", lang, question).trim();
		//asHtml = ImportTemplate.getLabelValue("wb_imp_tem_as_html", lang, asHtml).trim();
		answer = ImportTemplate.getLabelValue("wb_imp_tem_answers", lang, answer).trim();
		scores = ImportTemplate.getLabelValue("wb_imp_tem_scores", lang, scores).trim();
		difficulty = ImportTemplate.getLabelValue("wb_imp_tem_difficulty", lang, difficulty).trim();
		status = ImportTemplate.getLabelValue("wb_imp_tem_status", lang, status).trim();

		String[] labels = new String[] { foldId, title, question, answer, scores, difficulty, status };
		return labels;
	}

	private String[] getOptLabelArray(loginProfile prof) {
		String target = "Target ", scource = "Source ";

		String lang = prof.cur_lan;
		target = ImportTemplate.getLabelValue("wb_imp_tem_target", lang, target).trim() + " ";
		scource = ImportTemplate.getLabelValue("wb_imp_tem_scource", lang, scource).trim() + " ";

		String[] labels = new String[] {scource, target};
		return labels;
	}
}