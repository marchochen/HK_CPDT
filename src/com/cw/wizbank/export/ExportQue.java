/*
 * Created on 2006-2-22
 */
package com.cw.wizbank.export;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.Vector;

import jxl.write.WriteException;

import com.cw.wizbank.accesscontrol.AcRegUser;
import com.cw.wizbank.accesscontrol.AcTrainingCenter;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.db.DbTrainingCenter;
import com.cw.wizbank.instructor.InstructorManager;
import com.cw.wizbank.qdb.dbEntityRelation;
import com.cw.wizbank.qdb.dbQuestion;
import com.cw.wizbank.qdb.dbSyllabus;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.upload.ImportTemplate;
import com.cw.wizbank.util.LangLabel;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwXSL;
import com.cwn.wizbank.security.AclFunction;

public class ExportQue {
    Connection con = null;
    loginProfile prof = null;
    ExportControllerCommon exportProgresser;
    public static String fileSheetName = "Data";

    public String COL_CATEGORY_ID = "Folder ID";
    public String COL_RESOURCE_ID = "Resource ID";
    public String COL_TITLE = "Title";
    public String COL_QUESTION = "Question";
    public String COL_AS_HTML = "AsHTML";
    public String COL_ANSWERS = "Answers";
    public String COL_SCORES = "Scores";
    public String COL_SCORE = "Score";
    public String COL_DIFFICULTY = "Difficulty";
    public String COL_STATUS = "Status";
    public String COL_DESCRIPTION = "Description";
    public String COL_SHUFFLE = "Shuffle";
    public String COL_ANSWER = "Answer";
    public String COL_MODEL_ANSWER = "Model Answer";
    public String COL_OPTION = "Option";
    public String COL_EXPLAIN = "Explanation";
    public String COL_BLANK = "Blank";
    public String COL_SOURCE = "Source";
    public String COL_SUBMIT_FILE = "Submit File";
    public String COL_SHUFFLE_OPTION = "Shuffle Option";
    public String COL_CRITERIA = "Criteria";
    public String COL_DURATION  = "Duration";
    public String COL_TARGET = "Target";
    public String mt_ans_parser_xsl = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\"><xsl:output method=\"text\"/><xsl:template match=\"/question\"><xsl:for-each select=\"body/source/item\"><xsl:variable name=\"src_id\"><xsl:value-of select=\"@id\"/></xsl:variable><xsl:if test=\"position()!=1\"><xsl:text>,</xsl:text></xsl:if><xsl:choose><xsl:when test=\"count(/question/outcome/feedback[@condition = $src_id]) = 0\"><xsl:text>0</xsl:text></xsl:when><xsl:otherwise><xsl:for-each select=\"/question/outcome/feedback[@condition = $src_id]\"><xsl:if test=\"position()!=1\"><xsl:text>+</xsl:text></xsl:if><xsl:value-of select=\"../@order\"/></xsl:for-each></xsl:otherwise></xsl:choose></xsl:for-each><xsl:text>|</xsl:text><xsl:for-each select=\"body/source/item\"><xsl:variable name=\"src_id\"><xsl:value-of select=\"@id\"/></xsl:variable><xsl:if test=\"position()!=1\"><xsl:text>,</xsl:text></xsl:if><xsl:choose><xsl:when test=\"count(/question/outcome/feedback[@condition = $src_id]) = 0\"><xsl:text>0</xsl:text></xsl:when><xsl:otherwise><xsl:for-each select=\"/question/outcome/feedback[@condition = $src_id]\"><xsl:if test=\"position()!=1\"><xsl:text>+</xsl:text></xsl:if><xsl:value-of select=\"@score\"/></xsl:for-each></xsl:otherwise></xsl:choose></xsl:for-each></xsl:template></xsl:stylesheet>";

    public String[] mcHead = {
        COL_CATEGORY_ID,
        COL_RESOURCE_ID,
        COL_TITLE,
        COL_QUESTION,
        COL_SHUFFLE,
        COL_ANSWER,
        COL_AS_HTML,
        COL_OPTION,
        COL_SCORE,
        COL_DIFFICULTY,
        COL_STATUS,
        COL_DESCRIPTION,
        COL_EXPLAIN
    };

    public String[] fbHead = {
        COL_CATEGORY_ID,
        COL_RESOURCE_ID,
        COL_TITLE,
        COL_QUESTION,
        COL_AS_HTML,
        COL_BLANK,
        COL_DIFFICULTY,
        COL_STATUS,
        COL_DESCRIPTION
    };

    private String[] mtHead = {
        COL_CATEGORY_ID,
        COL_RESOURCE_ID,
        COL_TITLE,
        COL_QUESTION,
        COL_AS_HTML,
        COL_SOURCE,
        COL_ANSWERS,
        COL_SCORES,
        COL_DIFFICULTY,
        COL_STATUS,
        COL_DESCRIPTION
        };

    private String[] tfHead = {
        COL_CATEGORY_ID,
        COL_RESOURCE_ID,
        COL_TITLE,
        COL_QUESTION,
        COL_AS_HTML,
        COL_ANSWER,
        COL_SCORE,
        COL_DIFFICULTY,
        COL_STATUS,
        COL_DESCRIPTION//,
        //COL_EXPLAIN
    };

    private String[] esHead = {
        COL_CATEGORY_ID,
        COL_RESOURCE_ID,
        COL_TITLE,
        COL_QUESTION,
        COL_AS_HTML,
        COL_MODEL_ANSWER,
        COL_SCORE,
        COL_DIFFICULTY,
        COL_STATUS,
        COL_DESCRIPTION//,
        //COL_SUBMIT_FILE
    };

    private String[] fscHead = {
        COL_CATEGORY_ID,
        COL_RESOURCE_ID,
        COL_TITLE,
        COL_QUESTION,
        COL_AS_HTML,
        COL_SHUFFLE,
        COL_DIFFICULTY,
        COL_STATUS,
        COL_DESCRIPTION,
        COL_OPTION,
        COL_SHUFFLE_OPTION,
        COL_ANSWER,
        COL_SCORE
    };
    
    private String[] dscHead = {
        COL_CATEGORY_ID,
        COL_RESOURCE_ID,
        COL_TITLE,
        COL_QUESTION,
        COL_AS_HTML,
        COL_SHUFFLE,
        COL_DIFFICULTY,
        COL_STATUS,
        COL_CRITERIA,
        COL_DESCRIPTION,
        COL_OPTION,
        COL_SHUFFLE_OPTION,
        COL_ANSWER,
        COL_SCORE
    };
    
    class QueData {
        public String asHtmlCommon;
        int que_score_common;
        long folder_id_common;
        long que_res_id_common;
        int que_difficulty_common;
        String que_type_common; 
        public String que_text_common;
        String que_desc_common;
        String que_res_status_common;
        String que_title_common;
        public String shuffle;
        public String explain;
        public String[] condition;
        public int[] score_lst;
        public String[] que_condition_text;
        double duration;
        public String sc_criteria;
        public int sc_allow_shuffle_ind;
        public int submit_file;
        Vector FBAnswer = new Vector();
        Vector FBScore = new Vector();
        Vector FBQueText = new Vector();
        Vector MTSource = new Vector();
        Vector MTTarget = new Vector();
        String mtBody;
        String MTAnswer;
    }

    public ExportQue(Connection inCon, ExportControllerCommon inExportProgresser, loginProfile inProf) {
    	setLabelValue(inProf);
    	
        this.con = inCon;
        this.prof = inProf;
        this.exportProgresser = inExportProgresser;
        
        
    }
    
    public void exportQue(ExportParam exp, WizbiniLoader wizbini, String que_type) throws SQLException, cwException, WriteException, qdbErrMessage, IOException  {
        Vector que_ids = null;
        int totalQueCount = 0;
        
        
        String tableName = null;
        String colName = "res_tnd_id";
        Vector tnd_id = null;
        if (exp.res_tnd_id_lst != null &&exp.res_tnd_id_lst.length > 0) {
            tnd_id = getResTndId(exp.res_tnd_id_lst, exp.include_sub);
        } else {
            tnd_id = getDefultTndId(exp.include_sub, prof, wizbini.cfgTcEnabled);
        }
        if(tnd_id == null || tnd_id.size() == 0) {
        	tnd_id = new Vector();
        	tnd_id.add(new Long(0));
        }
        
        if (tnd_id != null && tnd_id.size() > 0) {
            tableName = cwSQL.createSimpleTemptable(con, colName, cwSQL.COL_TYPE_LONG, 0);
            cwSQL.insertSimpleTempTable(con, tableName, tnd_id, cwSQL.COL_TYPE_LONG);
        }
        
        PreparedStatement psmt = getQueData(con, exp, true, tableName, colName);
        ResultSet rs;
        rs = psmt.executeQuery();
        if (rs.next()) {
            totalQueCount = rs.getInt("que_count");
        }

        exportProgresser.setTotalRow(totalQueCount);
        if (totalQueCount > 0) {
        
            String[] head = null;
            if (que_type.equals(dbQuestion.QUE_TYPE_MULTI)) {
                head = mcHead;
            } else if (que_type.equals(dbQuestion.QUE_TYPE_FILLBLANK)) {
                head = fbHead;
            } else if (que_type.equals(dbQuestion.QUE_TYPE_MATCHING)) {
                head = mtHead;
            } else if (que_type.equals(dbQuestion.QUE_TYPE_TRUEFALSE)) {
                head = tfHead;
            } else if (que_type.startsWith(dbQuestion.QUE_TYPE_ESSAY)) {
                head = esHead;
            } else if (que_type.equals(dbQuestion.RES_SUBTYPE_FSC)) {
                head = fscHead;
            } else if (que_type.equals(dbQuestion.RES_SUBTYPE_DSC)) {
                head = dscHead;
            }
            ExportQueHelper exHelper = new ExportQueHelper(wizbini.getFileUploadTmpDirAbs(),  
                                           wizbini.cfgSysSetupadv.getFileUpload().getTmpDir().getName(), 
                                           exp.window_name, 
                                           wizbini.cfgSysSetupadv.getEncoding(), 
                                           wizbini.cfgSysSetupadv.getRptProcessUnit(), 
                                           head, que_type, this);

            exHelper.writeHead();
            
            psmt = getQueData(con, exp, false, tableName, colName);
            try {
                rs = psmt.executeQuery();
                handleData(rs, head, exHelper);
            } finally {
                if (tableName != null) {
                    cwSQL.dropTempTable(con, tableName);
                }
                if (psmt != null) {
                    psmt.close();
                }
            }
        }
    }
    
    private Vector getDefultTndId(boolean include_sub, loginProfile prof, boolean tc_enabled) throws SQLException {
    	StringBuffer sql = new StringBuffer();
    	Vector sylVec = dbSyllabus.getSybVec(con, null, prof.root_ent_id);
    	StringBuffer syl_lst = new StringBuffer();
    	if(sylVec != null && sylVec.size() > 0) {
    		syl_lst.append("(0 ");
    		for(int i = 0; i < sylVec.size(); i++) {
    			syl_lst.append(",").append(((dbSyllabus)sylVec.get(i)).syl_id);
    		}
    		syl_lst.append(" )");
    	}
    	boolean isRoleTcInd = AccessControlWZB.isRoleTcInd(prof.current_role);
    	if(tc_enabled) {
    		
    		
    		AcTrainingCenter actc = new AcTrainingCenter(con);
        	if(AccessControlWZB.hasRolePrivilege(prof.current_role, AclFunction.FTN_AMD_RES_MAIN) && !isRoleTcInd){
                sql.append(" SELECT distinct(obj_id) oac_obj_id FROM Objective ") 
                   .append(" WHERE obj_syl_id in ").append(syl_lst.toString())
                   .append(" AND obj_obj_id_parent is null ");
        	} else if(AccessControlWZB.hasRolePrivilege(prof.current_role, AclFunction.FTN_AMD_RES_MAIN)) {				
        		sql.append(" select distinct oac_obj_id from objectiveAccess ")
				   .append(" INNER JOIN objective on ( obj_id = oac_obj_id ) ")
				   .append(" inner join (select distinct(child.tcr_id) as tcr_id, child.tcr_title as tcr_title from tcTrainingCenter ancestor ")
				   .append(" inner join tcTrainingCenterOfficer on (tco_tcr_id = ancestor.tcr_id) ")
				   .append(" Left join tcRelation on (tcn_ancestor = ancestor.tcr_id) ")
				   .append(" inner join tcTrainingCenter child on (child.tcr_id = tcn_child_tcr_id or child.tcr_id = ancestor.tcr_id) ")
				   .append(" where tco_usr_ent_id =? ")
				   .append(" and child.tcr_status = ? ")
				   .append(" and ancestor.tcr_status = ?) A on (obj_tcr_id = tcr_id) ")
				   .append(" WHERE  1=1 and (oac_ent_id = ? or oac_ent_id is null) and obj_syl_id in ").append(syl_lst.toString());
        	} else if (AccessControlWZB.isIstRole(prof.current_role)) {
        		sql.append(" select distinct oac_obj_id from objectiveAccess ")
     		       .append(" INNER JOIN objective on ( obj_id = oac_obj_id ) ")
     		       .append(" inner join tcTrainingCenter on ( tcr_id = obj_tcr_id) ")
     		       .append(" inner join tcTrainingCenterTargetEntity on (tce_tcr_id = tcr_id) ")
				   .append(" inner join EntityRelation on ( ern_ancestor_ent_id = tce_ent_id) ")
				   .append(" WHERE  tcr_ste_ent_id =?  and tcr_status = ? ")
				   .append(" and ern_type =? ")
				   .append(" and ern_child_ent_id = ? ")
     		       .append(" and (oac_ent_id = ? or oac_ent_id is null) and obj_syl_id in ").append(syl_lst.toString());
        	}    		
    	} else {
    		if(AccessControlWZB.hasRolePrivilege(prof.current_role, AclFunction.FTN_AMD_RES_MAIN)){
                sql.append(" SELECT distinct(obj_id)  oac_obj_id FROM Objective ") 
                   .append(" WHERE obj_syl_id in ").append(syl_lst.toString())
                   .append(" AND obj_obj_id_parent is null ");
        	} else {				
        		sql.append(" select distinct oac_obj_id from objectiveAccess ")
				   .append(" INNER JOIN objective on ( obj_id = oac_obj_id ) ")
				   .append(" WHERE  1=1 and (oac_ent_id = ? or oac_ent_id is null) and obj_syl_id in ").append(syl_lst.toString());
        	}
    	}
        PreparedStatement psmt = con.prepareStatement(sql.toString());
    	int index = 1;
        if(tc_enabled) {
    		AcTrainingCenter actc = new AcTrainingCenter(con);
        	if(AccessControlWZB.hasRolePrivilege(prof.current_role, AclFunction.FTN_AMD_RES_MAIN) && !isRoleTcInd){

        	} else if(AccessControlWZB.hasRolePrivilege(prof.current_role, AclFunction.FTN_AMD_RES_MAIN) ) {				
        		psmt.setLong(index++, prof.usr_ent_id);
        		psmt.setString(index++, DbTrainingCenter.STATUS_OK);
        		psmt.setString(index++, DbTrainingCenter.STATUS_OK);
        		psmt.setLong(index++, prof.usr_ent_id);
        	} else if (AccessControlWZB.isIstRole(prof.current_role)) {
    			psmt.setLong(index++, prof.root_ent_id);
    			psmt.setString(index++, DbTrainingCenter.STATUS_OK);
    			psmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
    			psmt.setLong(index++, prof.usr_ent_id);
    			psmt.setLong(index++, prof.usr_ent_id);
        	}    		
    	} else {
    		if(AccessControlWZB.hasRolePrivilege(prof.current_role, AclFunction.FTN_AMD_RES_MAIN)){
        
        	} else {				
        		psmt.setLong(index++, prof.usr_ent_id);
        	}
    	}
        Vector Ids = new Vector();
        Vector sub_ids = new Vector();
        
        try {
            ResultSet rs = psmt.executeQuery();
            long temp_id;
            while (rs.next()) {
                temp_id = rs.getLong("oac_obj_id");
                if (!Ids.contains(new Long(temp_id))) {
                    Ids.addElement(new Long(temp_id));
                }
            }

            if (include_sub) {
                Iterator iter = Ids.iterator();
                while (iter.hasNext()) {
                    sub_ids.addAll(getSubTndIds(((Long)iter.next()).longValue()));
                }
                Ids.addAll(sub_ids);
            }
        } finally {
            if (psmt != null) {
                psmt.close();
            }
        }
        return Ids;
    }

    private String getDSCCriteria(long dsc_que_res_id) throws SQLException {
        StringBuffer criteria = new StringBuffer();
        String sql = "select qcs_score, qcs_qcount from QueContainerSpec where qcs_res_id = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setLong(1, dsc_que_res_id);
        try {
            ResultSet rs = ps.executeQuery();

            boolean hasPrv = false;
            while (rs.next()) {
                if (hasPrv) {
                    criteria.append(",");
                }
                criteria.append(rs.getInt("qcs_score")).append("(").append(rs.getInt("qcs_qcount")).append(")");
                hasPrv = true;
            }
        }
        finally {
            if (ps != null) {
                ps.close();
            }
        }
        return criteria.toString();
    }

    private PreparedStatement getSCSubQue(long sc_que_res_id) throws SQLException {
        
        String sql = "select res_id, res_subtype, res_title, que_xml, int_xml_outcome, res_subtype, " +            " que_score, res_difficulty, res_duration, res_status, res_desc,int_xml_explain, " +            " que_submit_file_ind " +            " from Resources, Question, Interaction " +            " where res_id = que_res_id " +            " and res_id = int_res_id" +            " and res_id in (select rcn_res_id_content from ResourceContent where rcn_res_id = ? )";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setLong(1, sc_que_res_id);
        return ps;
    }

    public PreparedStatement getQueData(Connection con, ExportParam exp, boolean getCount, String tableName, String colName) throws SQLException {
        StringBuffer sqlBuf = new StringBuffer();
        sqlBuf.append("select ");
        if (getCount) {
            sqlBuf.append(" count(distinct res_id) que_count");
        } else {
            sqlBuf.append(" rob_obj_id, res_id, res_subtype, res_title, que_xml, int_xml_outcome, res_subtype, ")
            .append(" que_score, res_difficulty, res_duration, res_status, res_desc,int_xml_explain,")
            .append(" que_submit_file_ind, qct_allow_shuffle_ind ");
        }
        
        sqlBuf.append(" from Question, RegUser, ResourceObjective, Resources ")
        .append(" left join Interaction on (res_id = int_res_id)")
        .append(" left join ResourceContent on (res_id = rcn_res_id_content)")
        .append(" left join QueContainer on (res_id = qct_res_id)")
        
        
        .append(" where res_id = que_res_id ")
        .append(" and res_id = rob_res_id ")
        .append(" and rcn_res_id is null ")
        .append(" and res_usr_id_owner = usr_id ")
        .append(" and res_res_id_root is null ")
        .append(" and res_type = ? ")
        .append(" and res_subtype = ? ");
        //res_type = 'QUE'
        
        if (exp.search_id > 0) {
            sqlBuf.append(" and res_id = ? ");
        }
        if (exp.search_id_after > 0) {
            sqlBuf.append(" and res_id >= ? ");
        }
        if (exp.search_id_before > 0) {
            sqlBuf.append(" and res_id <= ? ");
        }
        
        String tempStat = "";
        Vector v_tmp_param = new Vector();
        String search_sql = new String();
        Vector v_sql_param_title = new Vector();
        
        if (exp.search_title != null && exp.search_title.length() > 0) {
            for( int i=0; i< exp.search_title_lst.length; i++ ){
                if ( exp.search_title_lst[i] != null && exp.search_title_lst[i].length() > 0 )
                {
                    if ( ((exp.search_title_lst[i]).substring(0,1)).equalsIgnoreCase("+")){
                        sqlBuf.append(" AND lower(res_title) like ? ");
                        search_sql = "%" + exp.search_title_lst[i].substring(1,exp.search_title_lst[i].length()).toLowerCase() + "%";
                        v_sql_param_title.addElement("%" + exp.search_title_lst[i].substring(1,exp.search_title_lst[i].length()).toLowerCase() + "%");
                    }
                    else if ( ((exp.search_title_lst[i]).substring(0,1)).equalsIgnoreCase("-")){
                        sqlBuf.append(" AND lower(res_title) not like ? ");
                        search_sql = "%" + exp.search_title_lst[i].substring(1,exp.search_title_lst[i].length()).toLowerCase() + "%";
                        v_sql_param_title.addElement("%" + exp.search_title_lst[i].substring(1,exp.search_title_lst[i].length()).toLowerCase() + "%");
                    }else{
                        if ( tempStat == "" ){
                            tempStat += " lower(res_title) like ? ";
                        }else{
                            tempStat += " OR lower(res_title) like ?  ";
                        }
                        v_tmp_param.addElement("%" + exp.search_title_lst[i].toLowerCase() + "%");
                    }
                }
            }
            if( tempStat != "" ){
                sqlBuf.append(" AND ( ").append(tempStat).append(" ) ");
                for (int i=0; i<v_tmp_param.size();i++){
                    v_sql_param_title.addElement((String)v_tmp_param.elementAt(i));
                }
            }
        }

        tempStat = "";
        v_tmp_param = new Vector();
        search_sql = new String();
        Vector v_sql_param_desc = new Vector();
        if (exp.search_desc != null && exp.search_desc.length() > 0) {
            for( int i=0; i< exp.search_desc_lst.length; i++ ){
                if ( exp.search_desc_lst[i] != null && exp.search_desc_lst[i].length() > 0 )
                {
                    if ( ((exp.search_desc_lst[i]).substring(0,1)).equalsIgnoreCase("+")){
                        sqlBuf.append(" AND lower(res_desc) like ? ");
                        search_sql = "%" + exp.search_desc_lst[i].substring(1,exp.search_desc_lst[i].length()).toLowerCase() + "%";
                        v_sql_param_desc.addElement("%" + exp.search_desc_lst[i].substring(1,exp.search_desc_lst[i].length()).toLowerCase() + "%");
                    }
                    else if ( ((exp.search_desc_lst[i]).substring(0,1)).equalsIgnoreCase("-")){
                        sqlBuf.append(" AND lower(res_desc) not like ? ");
                        search_sql = "%" + exp.search_desc_lst[i].substring(1,exp.search_desc_lst[i].length()).toLowerCase() + "%";
                        v_sql_param_desc.addElement("%" + exp.search_desc_lst[i].substring(1,exp.search_desc_lst[i].length()).toLowerCase() + "%");
                    }else{
                        if ( tempStat == "" ){
                            tempStat += " lower(res_desc) like ? ";
                        }else{
                            tempStat += " OR lower(res_desc) like ?  ";
                        }
                        v_tmp_param.addElement("%" + exp.search_desc_lst[i].toLowerCase() + "%");
                    }
                }
            }
            if( tempStat != "" ){
                sqlBuf.append(" AND ( ").append(tempStat).append(" ) ");
                for (int i=0; i<v_tmp_param.size();i++){
                    v_sql_param_desc.addElement((String)v_tmp_param.elementAt(i));
                }
            }
        }
        
        if (exp.search_create_time_after != null) {
            sqlBuf.append(" and res_create_date >= ? ");
        }
        if (exp.search_create_time_before != null) {
            sqlBuf.append(" and res_create_date <= ? ");
        }
        if (exp.search_update_time_after != null) {
            sqlBuf.append(" and res_upd_date >= ? ");
        }
        if (exp.search_update_time_before != null) {
            sqlBuf.append(" and res_upd_date <= ? ");
        }
        
        if (exp.difficulty != null && exp.difficulty.length > 0) {
            if (exp.difficulty.length == 1) {
                sqlBuf.append(" and res_difficulty = ").append(exp.difficulty[0]);
            }
        
            if (exp.difficulty.length == 2) {
                String diff_temp = "(" + exp.difficulty[0] + "," + exp.difficulty[1] + ")";
                sqlBuf.append(" and res_difficulty in ").append(diff_temp);
            }
        }
        
        if (exp.search_owner != null && exp.search_owner.length() > 0) {
            sqlBuf.append(" and ( usr_ste_usr_id like ?  or lower(usr_display_bil) like ? )");
        }
        
        if (exp.search_status != null && exp.search_status.length() > 0) {
            sqlBuf.append(" and res_status = ? ");
        }
        if (tableName != null) {
            sqlBuf.append(" and rob_obj_id in ")
            .append("( select ").append(colName).append(" from ").append(tableName).append(" )");
        }
        
        if (!getCount) {
            sqlBuf.append(" order by res_id ");
        }
        

        PreparedStatement pstm = con.prepareStatement(sqlBuf.toString());
        int index = 1;
        pstm.setString(index++, dbQuestion.RES_TYPE_QUE);
        pstm.setString(index++, exp.que_type);
        
        if (exp.search_id > 0) {
            pstm.setLong(index++, exp.search_id);
        }
        if (exp.search_id_after > 0) {
            pstm.setLong(index++, exp.search_id_after);
        }
        if (exp.search_id_before > 0) {
            pstm.setLong(index++, exp.search_id_before);
        }
        if (exp.search_title!=null && exp.search_title.length() > 0) {
            for (int i = 0;i < v_sql_param_title.size();i++) {
                pstm.setString(index++, (String)v_sql_param_title.get(i));
            }
        }
        if (exp.search_desc!=null && exp.search_desc.length() > 0) {
            for (int i = 0;i < v_sql_param_desc.size();i++) {
                pstm.setString(index++, (String)v_sql_param_desc.get(i));
            }
        }
        if (exp.search_create_time_after != null) {
            pstm.setTimestamp(index++, exp.search_create_time_after);
        }
        if (exp.search_create_time_before != null) {
            pstm.setTimestamp(index++, exp.search_create_time_before);
        }
        if (exp.search_update_time_after != null) {
            pstm.setTimestamp(index++, exp.search_update_time_after);
        }
        if (exp.search_update_time_before != null) {
            pstm.setTimestamp(index++, exp.search_update_time_before);
        }
        
        if (exp.search_owner != null && exp.search_owner.length() > 0) {
            String temp = "%" + exp.search_owner + "%";
            pstm.setString(index++, temp);
            pstm.setString(index++, temp);
        }
        
        if (exp.search_status != null && exp.search_status.length() > 0) {
            pstm.setString(index++, exp.search_status);
        }
        return pstm;
    }

    private Vector getResTndId(String[] id_lst, boolean include_sub) throws SQLException {
        Vector Ids = new Vector();
        Vector sub_tnd_ids = new Vector();
        for (int i = 0; i< id_lst.length;i++) {
            Ids.addElement(new Long(id_lst[i]));
        }

        if (include_sub) {
            Iterator iter = Ids.iterator();
            while (iter.hasNext()) {
                sub_tnd_ids.addAll(getSubTndIds(((Long)iter.next()).longValue()));
            }
            Ids.addAll(sub_tnd_ids);
        }
        return Ids;
    }

    private Vector getSubTndIds(long ids) throws SQLException {
        String sql = " select obj_id from objective where obj_ancester like ? ";
        PreparedStatement stmt = con.prepareStatement(sql);
        Vector temp_sub_res_tnd_id = new Vector();
        String temp_var = "% " + ids + " %";
        stmt.setString(1, temp_var);
        try {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                temp_sub_res_tnd_id.addElement(new Long(rs.getLong("obj_id")));
            }
        }
        finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return temp_sub_res_tnd_id;
    }

    public void handleData(ResultSet rs, String[] head, ExportQueHelper exHelper) throws SQLException, cwException, WriteException, qdbErrMessage, IOException {
        Vector que_data = new Vector();
        Vector dup_que_id = new Vector();
        QueXmlParser xmlParser = new QueXmlParser();
        StringBuffer mtOutcome = new StringBuffer();

        while (rs.next() && !exportProgresser.isCancelled()) {
            QueData queData = new QueData();
            queData.que_type_common = rs.getString("res_subtype");
            queData.que_res_id_common = rs.getLong("res_id");

            if (que_data.size() > 0 && queData.que_res_id_common > ((QueData)que_data.lastElement()).que_res_id_common) {
                QueData qData = (QueData)que_data.lastElement();

                if (queData.que_type_common.equals(dbQuestion.QUE_TYPE_MATCHING)) {
                    StringBuffer xml = new StringBuffer();
                    xml.append("<question>").append(qData.mtBody)
                    .append(mtOutcome).append("</question>");
                    qData.MTAnswer = cwXSL.processFromString(xml.toString(), mt_ans_parser_xsl);
                    mtOutcome = new StringBuffer();
                }
                if (qData.que_type_common.equals(dbQuestion.RES_SUBTYPE_FSC) || qData.que_type_common.equals(dbQuestion.RES_SUBTYPE_DSC)) {
                    writeSCQue(head, exHelper, qData);
                }
                else {
                    exHelper.writeContent(qData, head, exportProgresser, qData.que_type_common);
                    exportProgresser.next();
                }
            }

            queData.folder_id_common = rs.getLong("rob_obj_id");
            queData.sc_allow_shuffle_ind = rs.getInt("qct_allow_shuffle_ind");
            queData.que_title_common = rs.getString("res_title");
            queData.que_difficulty_common = rs.getInt("res_difficulty");
            queData.que_res_status_common = rs.getString("res_status");
            queData.que_desc_common = rs.getString("res_desc");
            queData.duration = rs.getDouble("res_duration");
            queData.submit_file = rs.getInt("que_submit_file_ind");
            if (queData.que_type_common.equals(dbQuestion.RES_SUBTYPE_DSC)) {
                queData.sc_criteria = getDSCCriteria(queData.que_res_id_common);
            }
            
            String que_xml = cwSQL.getClobValue(rs, "que_xml").replaceAll("\n", " ");
            String int_xml_outcome = cwSQL.getClobValue(rs, "int_xml_outcome");
            xmlParser.parseString(que_xml, queData.que_type_common);
            xmlParser.parseString(int_xml_outcome, queData.que_type_common);
            if (queData.que_type_common.equals(dbQuestion.QUE_TYPE_MATCHING)) {
                queData.mtBody = que_xml;
                mtOutcome.append(int_xml_outcome);
            }
            buildQueData(xmlParser, queData, dup_que_id, rs, que_data);
        }
        QueData qData = (QueData)que_data.lastElement();
        if (qData.que_type_common.equals(dbQuestion.QUE_TYPE_MATCHING)) {
            StringBuffer xml = new StringBuffer();
            xml.append("<question>").append(qData.mtBody)
            .append(mtOutcome).append("</question>");
            qData.MTAnswer = cwXSL.processFromString(xml.toString(), mt_ans_parser_xsl);
            mtOutcome = new StringBuffer();
        }

        if (qData.que_type_common.equals(dbQuestion.RES_SUBTYPE_FSC) || qData.que_type_common.equals(dbQuestion.RES_SUBTYPE_DSC)) {
            writeSCQue(head, exHelper, qData);
        }
        else {
            exHelper.writeContent(qData, head, exportProgresser, qData.que_type_common);
            exportProgresser.next();
        }
        exportProgresser.setFile(exHelper.finalizeFile());
    }

    private void writeSCQue(String[] head, ExportQueHelper exHelper, QueData qData) throws SQLException, WriteException, qdbErrMessage, IOException, cwException {
        int subqueCount = getScSubQueCount(qData.que_res_id_common);

        //if a sc question and its sub questions just cross the limit of per file line,put it to a new file
        String no_str = LangLabel.getValue(prof.cur_lan, "lab_no");
        String error_desc = LangLabel.getValue(prof.cur_lan, "lab_export_sc_subque_overload");
        String errorcontent = no_str + qData.que_res_id_common + error_desc;

        if ((subqueCount + 2 + exHelper.nowRowNum) > exHelper.process_unit && exHelper.nowRowNum == 0) {
            exHelper.writeError(ExportHelper.ERROR_CODE_SC_SUBQUE_OVERLOAD, errorcontent);
            exportProgresser.next();

        }
        else if ((subqueCount + 2 + exHelper.nowRowNum) > exHelper.process_unit && exHelper.nowRowNum != 0) {
            exHelper.changeFile();
            if ((subqueCount + 2 + exHelper.nowRowNum) > exHelper.process_unit) {
                exHelper.writeError(ExportHelper.ERROR_CODE_SC_SUBQUE_OVERLOAD, errorcontent);
                exportProgresser.next();
            }
            else {
                exHelper.writeContent(qData, head, exportProgresser, qData.que_type_common);
                getSCDetail(head, exHelper, qData.que_type_common, qData.que_res_id_common);
                exportProgresser.next();
            }
        }
        else {
            exHelper.writeContent(qData, head, exportProgresser, qData.que_type_common);
            getSCDetail(head, exHelper, qData.que_type_common, qData.que_res_id_common);
            exportProgresser.next();
        }
    }

    private void getSCDetail(String[] head, ExportQueHelper exHelper, String que_type, long container_res_id) throws SQLException, cwException, WriteException, qdbErrMessage, IOException {
        Vector sub_que = new Vector();
        Vector dup_que_id = new Vector();
        QueXmlParser xmlParser = new QueXmlParser();

        PreparedStatement psmt = getSCSubQue(container_res_id);
        ResultSet rs = null;
        try {
            rs = psmt.executeQuery();
            while (rs.next()) {
                QueData queData = new QueData();
                queData.que_type_common = rs.getString("res_subtype");
                queData.que_res_id_common = rs.getLong("res_id");

                queData.que_title_common = rs.getString("res_title");
                queData.que_difficulty_common = rs.getInt("res_difficulty");
                queData.que_res_status_common = rs.getString("res_status");
                queData.que_desc_common = rs.getString("res_desc");
                queData.duration = rs.getDouble("res_duration");
                queData.submit_file = rs.getInt("que_submit_file_ind");

                xmlParser.parseString(cwSQL.getClobValue(rs, "que_xml"), queData.que_type_common);
                xmlParser.parseString(cwSQL.getClobValue(rs, "int_xml_outcome"), queData.que_type_common);
                buildQueData(xmlParser, queData, dup_que_id, rs, sub_que);
                sub_que.addElement(new Long(queData.que_res_id_common));
                exHelper.writeContent(queData, head, exportProgresser, queData.que_type_common);
            }
        }
        finally {
            if (psmt != null) {
                psmt.close();
            }
        }

    }
    public Vector buildQueData(QueXmlParser xmlParser, QueData queData, Vector dup_que_id, ResultSet rs, Vector que_data) throws cwException, SQLException {
        queData.score_lst = xmlParser.score;
        if (xmlParser.condition != null && xmlParser.condition.length > 0) {
            queData.FBAnswer.addElement(xmlParser.condition[0]);
        }
        if (xmlParser.score != null && xmlParser.score.length > 0) {
            queData.FBScore.addElement(new Integer(xmlParser.score[0]));
        }

        queData.que_score_common = xmlParser.highScore;
        queData.que_condition_text = xmlParser.condition_text;
        queData.asHtmlCommon = xmlParser.asHtml;
        xmlParser.asHtml = "N";
        queData.shuffle = xmlParser.shuffle;

        queData.MTSource = xmlParser.mt_source;
        queData.MTTarget = xmlParser.mt_target;
        xmlParser.parseString(cwSQL.getClobValue(rs, "int_xml_explain"), queData.que_type_common);
        queData.explain = xmlParser.explain;
        xmlParser.explain = null;
        if ((queData.que_type_common.equals(dbQuestion.QUE_TYPE_FILLBLANK)) 
            && dup_que_id.contains(new Long(queData.que_res_id_common))) {
            QueData data = (QueData)que_data.lastElement();
            data.FBAnswer.addElement(xmlParser.condition[0]);
            data.FBScore.addElement(new Integer(xmlParser.score[0]));
        }else {
            if (queData.que_type_common.equals(dbQuestion.RES_SUBTYPE_FSC) 
                || queData.que_type_common.equals(dbQuestion.RES_SUBTYPE_DSC)) {
                queData.que_text_common = xmlParser.sc_que_text;
            }
            else {
                queData.que_text_common = xmlParser.que_text;
            }
            queData.FBQueText = xmlParser.fbQueText;
            que_data.addElement(queData);
            if (!dup_que_id.contains(new Long(queData.que_res_id_common))) {
                dup_que_id.addElement(new Long(queData.que_res_id_common));
            }
        }
        return que_data;
    }
    
    public int getScSubQueCount(long container_res_id) throws SQLException {
        String sql = " select count(rcn_res_id_content) from resourcecontent where rcn_res_id = ?  ";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setLong(1, container_res_id);
        int subque_count = 0;
        try {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                subque_count = rs.getInt(1);
            }
        }
        finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return subque_count;
    }
    
    private void setLabelValue(loginProfile prof) {
		String lang = prof.cur_lan;
		this.COL_CATEGORY_ID = ImportTemplate.getLabelValue("wb_imp_tem_folder_id", lang, "Folder ID");
		this.COL_RESOURCE_ID = ImportTemplate.getLabelValue("wb_imp_tem_resource_id", lang, "Resource ID");
		this.COL_TITLE = ImportTemplate.getLabelValue("wb_imp_tem_title", lang, "Title");
		this.COL_QUESTION = ImportTemplate.getLabelValue("wb_imp_tem_question", lang, "Question");
		//this.COL_AS_HTML = ImportTemplate.getLabelValue("wb_imp_tem_as_html", lang, "AsHTML");
		this.COL_ANSWERS = ImportTemplate.getLabelValue("wb_imp_tem_answers", lang, "Answers");
		this.COL_SCORES = ImportTemplate.getLabelValue("wb_imp_tem_scores", lang, "Scores");
		this.COL_SCORE = ImportTemplate.getLabelValue("wb_imp_tem_score", lang, "Score");
		this.COL_DIFFICULTY = ImportTemplate.getLabelValue("wb_imp_tem_difficulty", lang, "Difficulty");
		this.COL_STATUS = ImportTemplate.getLabelValue("wb_imp_tem_status", lang, "Status");
		this.COL_DESCRIPTION = ImportTemplate.getLabelValue("wb_imp_tem_description", lang, "Description");
		this.COL_SHUFFLE = ImportTemplate.getLabelValue("wb_imp_tem_shuffle", lang, "Shuffle");
		this.COL_ANSWER = ImportTemplate.getLabelValue("wb_imp_tem_answer", lang, "Answer");
		this.COL_MODEL_ANSWER = ImportTemplate.getLabelValue("wb_imp_tem_model_answer", lang, "Model Answer");
		this.COL_OPTION = ImportTemplate.getLabelValue("wb_imp_tem_option", lang, "Option");
		this.COL_EXPLAIN = ImportTemplate.getLabelValue("wb_imp_tem_explanation", lang, "Explanation");
		this.COL_BLANK = ImportTemplate.getLabelValue("wb_imp_tem_blank", lang, "Blank");
		this.COL_SOURCE = ImportTemplate.getLabelValue("wb_imp_tem_scource", lang, "Source");
		//this.COL_SUBMIT_FILE = ImportTemplate.getLabelValue("wb_imp_tem_submit_file", lang, "Submit File");
		this.COL_SHUFFLE_OPTION = ImportTemplate.getLabelValue("wb_imp_tem_shuffle_option", lang, "Shuffle Option");
		this.COL_CRITERIA = ImportTemplate.getLabelValue("wb_imp_tem_criteria", lang, "Criteria");
		this.COL_DURATION = ImportTemplate.getLabelValue("wb_imp_tem_duration", lang, "Duration");
		this.COL_TARGET = ImportTemplate.getLabelValue("wb_imp_tem_target", lang, "Target");

		this.mcHead = new String[] { 
				COL_CATEGORY_ID, 
				COL_RESOURCE_ID, 
				COL_TITLE, 
				COL_QUESTION, 
				COL_SHUFFLE, 
				COL_ANSWER, 
				//COL_AS_HTML, 
				COL_OPTION,
				COL_SCORE, 
				COL_DIFFICULTY, 
				COL_STATUS, 
				COL_DESCRIPTION, 
				COL_EXPLAIN 
				};

		this.fbHead = new String[] { 
				COL_CATEGORY_ID, 
				COL_RESOURCE_ID, 
				COL_TITLE, 
				COL_QUESTION, 
				//COL_AS_HTML,
				COL_BLANK, 
				COL_DIFFICULTY, 
				COL_STATUS,
				COL_DESCRIPTION 
				};

		this.mtHead = new String[] { 
				COL_CATEGORY_ID, 
				COL_RESOURCE_ID, 
				COL_TITLE, 
				COL_QUESTION, 
				//COL_AS_HTML, 
				COL_SOURCE, 
				COL_ANSWERS, 
				COL_SCORES,
				COL_DIFFICULTY, 
				COL_STATUS, 
				COL_DESCRIPTION 
				};

		this.tfHead = new String[] { 
				COL_CATEGORY_ID, 
				COL_RESOURCE_ID, 
				COL_TITLE, 
				COL_QUESTION, 
				//COL_AS_HTML, 
				COL_ANSWER, 
				COL_SCORE, 
				COL_DIFFICULTY,
				COL_STATUS, 
				COL_DESCRIPTION//, 
				//COL_EXPLAIN 
				};

		this.esHead = new String[] { 
				COL_CATEGORY_ID, 
				COL_RESOURCE_ID, 
				COL_TITLE, 
				COL_QUESTION, 
				//COL_AS_HTML, 
				COL_MODEL_ANSWER, 
				COL_SCORE,
				COL_DIFFICULTY, 
				COL_STATUS, 
				COL_DESCRIPTION//, 
				//COL_SUBMIT_FILE 
				};

		this.fscHead = new String[] { 
				COL_CATEGORY_ID, 
				COL_RESOURCE_ID, 
				COL_TITLE, 
				COL_QUESTION, 
				//COL_AS_HTML, 
		//		COL_SHUFFLE, 
				COL_DIFFICULTY,
				COL_STATUS, 
				COL_DESCRIPTION,
				COL_OPTION, 
				COL_SHUFFLE_OPTION,
				COL_ANSWER, 
				COL_SCORE 
				};

		dscHead = new String[] { 
				COL_CATEGORY_ID, 
				COL_RESOURCE_ID, 
				COL_TITLE, 
				COL_QUESTION, 
				//COL_AS_HTML, 
		//		COL_SHUFFLE, 
				COL_DIFFICULTY, 
				COL_STATUS,
				COL_CRITERIA, 
				COL_DESCRIPTION,
				COL_OPTION, 
				COL_SHUFFLE_OPTION, 
				COL_ANSWER, 
				COL_SCORE 
				};

	}
}
