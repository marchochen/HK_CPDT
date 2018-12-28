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
import com.cw.wizbank.upload.BufferedLineReader;
import com.cw.wizbank.upload.ImportTemplate;
import com.cw.wizbank.upload.RawQuestion;
import com.cw.wizbank.upload.UploadUtils;
import com.cw.wizbank.util.*;
import com.cwn.wizbank.utils.CommonLog;

public class ConESQue{
    
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
    
    private final static int ROW_INVALID_TITLE = 1;
    private final static int ROW_INVALID_CONTENT = 2;
    private final static int ROW_INVALID_DESC = 3;
	//private final static int ROW_INVALID_DESCRIPTION= 14;
    
    private Vector invalidTitle = null;
    private Vector invalidContent = null;
    private Vector invalidDesc = null;
    private Vector invalidColumn = null;
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
        "Title",
        "Question"
    };

	private String[] RequiredStdColName = {
			"Title",
			"Question"
	};
	
	//UnicodeLittle:excel unicode encoding
    private final static String DEFAULT_ENC = "UnicodeLittle";

    private final static String DEFAULT_LANGUAGE = "ISO-8859-1";
    private String DEFAULT_FOLDER = "CW";

    private Vector inColHeader = null;
    
    public ConESQue(Connection con, qdbEnv static_env, loginProfile prof){
    	// set label
        this.StdColName = this.getLabelArray(prof);
        this.RequiredStdColName = this.getRequiredLabelArray(prof);   	
    	
        this.con = con;
        this.site_id = prof.root_ent_id;
        this.usr_id = prof.usr_id;
        this.cur_role = prof.current_role;
        this.static_env = static_env;
        this.inColHeader = new Vector();

        invalidColumn = new Vector();
        invalidTitle = new Vector();
        invalidContent = new Vector();
        invalidDesc =new Vector();
        cachedValidCategoryIdVec = new Vector();
        cachedInvalidCategoryIdVec = new Vector();
        
        dupQueId = new Vector();
        return;
    }
    
	
	public String uploadQue(File srcFile, String ulg_desc, String que_type, boolean allow_upd, WizbiniLoader wizbini,long obj_id)
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
            int rowLimit = 200;
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
                parsedQue.obj_id[0]=obj_id;
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
                        invalidTitle.addElement(new Integer(rowNum));
                        break;
                    case 2:
                        errorFlag = true;
                        invalidContent.addElement(new Integer(rowNum));
                        break;
                    case 3:
                        errorFlag = true;
                        invalidDesc.addElement(new Integer(rowNum));
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
        long usr_ent_id = 0;
        try {
            usr_ent_id = dbRegUser.getEntId(con, usr_id);
        } catch (qdbException e) {
            throw new SQLException();
        }
        
        for(int i=0; i<tokens.length; i++){
            String colName = (String)inColHeader.elementAt(col);
             if( colName.equalsIgnoreCase(StdColName[0]) ) { // Title
                que.title = UploadUtils.escString(tokens[i]);
                if( que.title == null || que.title.trim().length() == 0 || que.title.length() > 255) {
                    return ROW_INVALID_TITLE;
                }
                else {
					if (que.title.indexOf("\n")>-1) {
						return ROW_INVALID_TITLE;	
					}
                }
            } else if( colName.equalsIgnoreCase(StdColName[1]) ) { // Question
                que.cont = cwUtils.esc4XML(UploadUtils.escString(tokens[i]));
                if( que.cont == null || que.cont.trim().length() == 0) {
                    return ROW_INVALID_CONTENT;
                }
                
            }
            else if( colName.equalsIgnoreCase(StdColName[2]) ) { // Description
                que.desc = UploadUtils.escString(tokens[i]);
                
				if( que.desc != null && que.desc.length() > 1000)
					return ROW_INVALID_DESC;
				
            }
           else {
                return -1;
            }
            col++;
        }
        que.int_count = 1;
        que.inter[0].type = "FB";
        que.inter[0].att_len = 0;
        que.inter[0].score = 0;
        que.cont_html = true;
		que.submit_file_ind = false;
        que.lan = DEFAULT_LANGUAGE;
        que.folder = DEFAULT_FOLDER;
        que.owner_id = this.usr_id;
        que.queType = "FB";
        que.onoff ="ON";
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
        Hashtable h_cnt = rawQue.save2DB(this.con, this.static_env, this.site_id, ulg.ulg_id, prof, tc_enabled,true);
        ulg.ulg_status = DbUploadLog.STATUS_COMPLETED;
        ulg.ulg_upd_datetime = cwSQL.getTime(this.con);
        ulg.updStatus(this.con);
        DbRawQuestion.del(this.con, ulg.ulg_id);
        
        return UploadUtils.getResultXML(static_env, ulg.ulg_id, h_cnt);
        
    }
    
    private String[] getLabelArray(loginProfile prof) {
		String  title = "Title", question = "Question";
		String  description = "Description";

		String lang = prof.cur_lan;
		title = ImportTemplate.getLabelValue("wb_imp_tem_title", lang, title).trim();
		question = ImportTemplate.getLabelValue("wb_imp_tem_question", lang, question).trim();
		description = ImportTemplate.getLabelValue("wb_imp_tem_description", lang, description).trim();

		String[] labels = new String[] {  title, question };
		return labels;
	}

	private String[] getRequiredLabelArray(loginProfile prof) {
		String  title = "Title", question = "Question";
	
	

		String lang = prof.cur_lan;
		title = ImportTemplate.getLabelValue("wb_imp_tem_title", lang, title).trim();
		question = ImportTemplate.getLabelValue("wb_imp_tem_question", lang, question).trim();

		String[] labels = new String[] { title, question };
		return labels;
	}
    

}