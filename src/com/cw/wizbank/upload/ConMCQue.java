package com.cw.wizbank.upload;

import java.io.*;
import java.sql.*;
import java.util.*;

import com.cw.wizbank.qdb.*;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.db.DbUploadLog;
import com.cw.wizbank.db.DbRawQuestion;
import com.cw.wizbank.util.*;
import com.cwn.wizbank.utils.CommonLog;
public class ConMCQue{
    
    //Log filename
    public final static String[] FILENAME_LIST = { UploadUtils.SUCCESS_FILE, UploadUtils.FAILURE_FILE, UploadUtils.ERROR_FILE};
    public final static String[] LOG_LIST = {"SUCCESS", "UNSUCCESS", "ERROR" };
    
        
    
    private final static int ROW_INVALID_OPTION = 7;
    private final static int ROW_INVALID_MULC = 3;
    private final static int ROW_INVALID_SCORE = 8;
    private final static int ROW_INVALID_TITLE = 1;
    private final static int ROW_INVALID_CONTENT = 2;
    private final static int ROW_INVALID_DESC = 4;
    
    
    
    
    private Vector invalidOptionVec = null;
    private Vector invalidScore = null;
    private Vector invalidColumn = null;
    private Vector invalidTitle = null;
    private Vector invalidContent = null;
    private Vector invalidDesc = null;
    private Vector dupQueId = null;
    private Vector invalidMulc = null;
    private Connection con = null;
    private long site_id;
    private String usr_id = null;
    private qdbEnv static_env = null;
    private String cur_role = null;
    
    private int optionNum = 0;

    
    private String[] StdColName = {
        "Title",
        "Question",
        "As Multiple Choice"
    };
    
    private String[] REQUIRED_COLUMN = {
    	
    	"Title",
    	"Question",
    	"Option 1",
    	"Option 2",
    	"Score 1",
    	"Score 2",
    	"As Multiple Choice"
    };
    
    
    private String StdOptionColName = "Option ";
    private String StdScoreColName = "Score ";
	private static final int MAX_OPTION_NUM = 100;
	//UnicodeLittle:excel unicode encoding
    private final static String DEFAULT_ENC = "UnicodeLittle";

    private final static String DEFAULT_LANGUAGE = "ISO-8859-1";
    private String DEFAULT_FOLDER = "CW";

    private Vector inColHeader = null;
    
    public ConMCQue(Connection con, qdbEnv static_env, loginProfile prof){
        // set label
        this.StdColName = this.getLabelArray(prof);
        this.REQUIRED_COLUMN = this.getRequiredLabelArray(prof);
        this.setOtherLabel(prof);
        this.setOtheScorerLabel(prof);
        this.con = con;
        this.site_id = prof.root_ent_id;
        this.usr_id = prof.usr_id;
        this.static_env = static_env;
        this.inColHeader = new Vector();
        this.cur_role = prof.current_role;

        invalidOptionVec = new Vector();
        invalidScore = new Vector();
        invalidColumn = new Vector();
        invalidTitle = new Vector();
        invalidContent = new Vector();
        invalidDesc = new Vector();
        invalidMulc = new Vector();
        
        dupQueId = new Vector();
        return;
    }
    
    
    public String uploadQue(File srcFile, String ulg_desc, boolean allow_upd, WizbiniLoader wizbini,long obj_id,String mod_type)
        throws cwException, IOException, cwSysMessage, SQLException, qdbException {
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
            Vector invalidColHeader = parseInValidColHeader(colHeader);          //对标题进行检测  
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
            int rowLimit = 200;
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
                parsedQue.obj_id[0]=obj_id;
                switch ( populate(parsedQue, row, allow_upd, wizbini.cfgTcEnabled,mod_type) ) { //对输入的内容进行检测
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
                        invalidTitle.addElement(new Integer(rowNum));
                        break;
                    case 2:
                        errorFlag = true;
                        invalidContent.addElement(new Integer(rowNum));
                        break;
                    case 3:
                        errorFlag = true;
                        invalidMulc.addElement(new Integer(rowNum));
                        break;
                    case 4:
                        errorFlag = true;
                        invalidDesc.addElement(new Integer(rowNum));
                        break;
                    case 7:
                        errorFlag = true;
                        invalidOptionVec.addElement(new Integer(rowNum));
                        break;
                    case 8:
                        errorFlag = true;
                        invalidScore.addElement(new Integer(rowNum));
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

    private int populate(extendQue que, String row, boolean allow_upd, boolean tc_enabled,String mod_type) throws SQLException{
        
        //StringTokenizer tokens = new StringTokenizer( row, (new Character(UploadUtils.colDelimiter_)).toString(), true );
        String[] tokens = cwUtils.splitToString(row, (new Character(UploadUtils.colDelimiter_)).toString());
        int col = 0;
        int[] ans = null;
        int queOptionNum = 0;
        int queScoreNum = 0;
        String asHTML = UploadUtils.NO;
        int que_score = 0;
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
                if( que.title == null || que.title.trim().length() == 0 || que.title.length() > 255 || UploadUtils.containsNewLine(que.title))
                    return ROW_INVALID_TITLE;

            } else if( colName.equalsIgnoreCase(StdColName[1]) ) { // Question
                que.cont = cwUtils.esc4XML(UploadUtils.escString(tokens[i]));
                if( que.cont == null || que.cont.trim().length() == 0)
                    return ROW_INVALID_CONTENT;
                
            } else if( colName.equalsIgnoreCase(StdColName[2]) ) { // 是否多选
                try{
                    String s = UploadUtils.checkYesNo(tokens[i]);
                    if(s.toUpperCase().equals("Y")){
                    	  que.inter[0].logic ="OR";
                    }else if(s.toUpperCase().equals("N")){
                    	  que.inter[0].logic ="SINGLE";
                    }else{
                    	return ROW_INVALID_MULC;
                    }
                }catch(cwException e){
                    return ROW_INVALID_MULC;
                }
                
           }/*else if( colName.equalsIgnoreCase(StdColName[3]) ) { // Description
                que.desc = UploadUtils.escString(tokens[i]);
                if( que.desc.length() > 1000 ) {
                	return ROW_INVALID_DESC;
                }
          }*/ else if( colName.startsWith(StdOptionColName) ) { //Option
                int optNum = Integer.parseInt( (colName.substring(StdOptionColName.length(), colName.length())).trim() );                	
                try{
                    if( tokens[i] != null && tokens[i].length() > 0 ) {
                        que.inter[0].opt[optNum-1].cont = cwUtils.esc4XML(UploadUtils.escString(tokens[i]));
                        if( UploadUtils.containsNewLine(que.inter[0].opt[optNum-1].cont) ) {
							throw new Exception("Option with new line");
                        }
                        queOptionNum++;
                    }
                }catch(Exception e){
                    return ROW_INVALID_OPTION;
                }
            }else if( colName.startsWith(StdScoreColName) ) { //Option
                int optNum = Integer.parseInt( (colName.substring(StdScoreColName.length(), colName.length())).trim() );                	
                try{
                	//当时课程问卷导入题目时，如果没有输入分数就默认为0
                	if(dbModule.MOD_TYPE_SVY.equalsIgnoreCase(mod_type)){
                		if( tokens[i] != null && tokens[i].length() > 0 ) {
                			int score = Integer.parseInt(tokens[i]);
                			que.inter[0].opt[optNum-1].score = score;
                		}else{
                			que.inter[0].opt[optNum-1].score = 0;
                		}
                		queScoreNum++;
                	}else{
                		if( tokens[i] != null && tokens[i].length() > 0 ) {
                			int score = Integer.parseInt(tokens[i]);
                			que.inter[0].opt[optNum-1].score =score;
                			queScoreNum++;
                		}
                	}
                }catch(Exception e){
                    return ROW_INVALID_SCORE;
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
        
        if( queScoreNum < 2 )
            return ROW_INVALID_SCORE;
            
        for(int i=queScoreNum; i<optionNum; i++){
            if( que.inter[0].opt[i].cont != null ){
                return ROW_INVALID_SCORE;
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
        
        que.onoff ="ON";
        que.lan = DEFAULT_LANGUAGE;
        que.folder = DEFAULT_FOLDER;
        que.inter[0].type = dbQuestion.QUE_TYPE_MULTI;
        que.owner_id = this.usr_id;
        return 0;

    }

    
    
        

    private String invalidRowXML(){
        StringBuffer xml = new StringBuffer(512);
        xml.append("<invalid_list>");
        if( !invalidMulc.isEmpty() ){
            xml.append("<invalid_mulc_lines>");
            for(int i=0; i<invalidMulc.size(); i++)
                xml.append("<line num=\"").append(invalidMulc.elementAt(i)).append("\"/>");
             xml.append("</invalid_mulc_lines>");
        }
       
        if( !invalidOptionVec.isEmpty() ){
            xml.append("<invalid_option_lines>");
            for(int i=0; i<invalidOptionVec.size(); i++)
                xml.append("<line num=\"").append(invalidOptionVec.elementAt(i)).append("\"/>");
             xml.append("</invalid_option_lines>");
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
        if( !invalidScore.isEmpty() ){
            xml.append("<invalid_score_lines>");
            for(int i=0; i<invalidScore.size(); i++)
                xml.append("<line num=\"").append(invalidScore.elementAt(i)).append("\"/>");
             xml.append("</invalid_score_lines>");
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
                if( tokens[i].startsWith(StdScoreColName) ){
					boolean flag = true;
					try{
						Integer.parseInt( (tokens[i].substring(StdScoreColName.length(), tokens[i].length())).trim() );
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
        Hashtable h_cnt = rawQue.save2DB(this.con, this.static_env, this.site_id, ulg.ulg_id, prof, tc_enabled, true);
        ulg.ulg_status = DbUploadLog.STATUS_COMPLETED;
        ulg.ulg_upd_datetime = cwSQL.getTime(this.con);
        ulg.updStatus(this.con);
        DbRawQuestion.del(this.con, ulg.ulg_id);
        
        return UploadUtils.getResultXML(static_env, ulg.ulg_id, h_cnt);
        
    }

    
    private String[] getLabelArray(loginProfile prof) {   	    	       	    
		String  title = "Title", question = "Question";
		String  asMulC = "As Multiple Choice";
		String description = "Description" ;
		String lang = prof.cur_lan;  
		title = ImportTemplate.getLabelValue("wb_imp_tem_title", lang, title).trim();
		question = ImportTemplate.getLabelValue("wb_imp_tem_question", lang, question).trim();
		asMulC = ImportTemplate.getLabelValue("wb_imp_tem_as_mulc", lang, asMulC).trim();
		description = ImportTemplate.getLabelValue("wb_imp_tem_description", lang, description).trim();
		
		String[] labels = new String[] {  title, question, asMulC};
		return labels;
	}  
    
    private String[] getRequiredLabelArray(loginProfile prof) {   	    	       	    
		String option1 = "Option 1", option2 = "Option 2";
		String score1 = "Score 1", score2 = "Score 2";
		String  title = "Title", question = "Question";
		String asMulC = "As Multiple Choice";
		String lang = prof.cur_lan;  
		title = ImportTemplate.getLabelValue("wb_imp_tem_title", lang, title).trim();
		question = ImportTemplate.getLabelValue("wb_imp_tem_question", lang, question).trim();
		option1 = ImportTemplate.getLabelValue("wb_imp_tem_option_1", lang, option1).trim();
		option2 = ImportTemplate.getLabelValue("wb_imp_tem_option_2", lang, option2).trim();
		score1 = ImportTemplate.getLabelValue("wb_imp_tem_score_1", lang, option1).trim();
		score2 = ImportTemplate.getLabelValue("wb_imp_tem_score_2", lang, option2).trim();
		asMulC = ImportTemplate.getLabelValue("wb_imp_tem_as_mulc", lang, asMulC).trim();
		String[] labels = new String[] {  title, question, option1, option2 ,score1,score2,asMulC};
		return labels;
	}    
    
    private void setOtherLabel(loginProfile prof) {	
		String optionStr = "Option ";
		String lang = prof.cur_lan;
		optionStr = ImportTemplate.getLabelValue("wb_imp_tem_option", lang, optionStr).trim() + " ";
	    StdOptionColName = optionStr;
	}
    private void setOtheScorerLabel(loginProfile prof) {	
		String optionStr = "Score";
		String lang = prof.cur_lan;
		optionStr = ImportTemplate.getLabelValue("wb_imp_tem_score", lang, optionStr).trim() + " ";
	    StdScoreColName = optionStr;
	}
}