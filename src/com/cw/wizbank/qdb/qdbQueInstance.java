package com.cw.wizbank.qdb;

import java.sql.*;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.report.ExportController;
import com.cw.wizbank.util.cwSysMessage;

public class qdbQueInstance extends Object
{    
    public dbQuestion dbque;
    public int        score_multiplier;
    public boolean    flag_ind;
    public int        que_score;
    public int        que_max_score; // NEW
    public Vector     atms;   // vector of ProgressAttempt 
        
    public qdbQueInstance() 
    {   
        dbque = new dbQuestion();
        atms = new Vector();
    }
    
    public void get(Connection con, long tst_id, boolean isAsm)
        throws qdbException, cwSysMessage
    {
        dbque.get(con);
        // get score multipler
        try {
            if (isAsm){
                  PreparedStatement stmt = con.prepareStatement(
                    "SELECT res_difficulty from Resources "
                      + " WHERE res_id = ? ");

                  stmt.setLong(1, dbque.que_res_id);
                  ResultSet rs = stmt.executeQuery();          
                  if(rs.next())
                  {
                        int sm = rs.getInt("res_difficulty");
                        score_multiplier = (sm>0) ? sm : 1;
                  }
                  else
                        score_multiplier = 1; 
        
                  stmt.close();
            }else{
                String sql = "SELECT rcn_score_multiplier from ResourceContent";
                if (cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType())) {
                    sql += " with(nolock) ";
                }
                sql += " WHERE rcn_res_id = ? AND rcn_res_id_content = ? ";
                if (cwSQL.DBVENDOR_DB2.equalsIgnoreCase(cwSQL.getDbType())) {
                    sql += " for read only";
                }
                PreparedStatement stmt = con.prepareStatement(sql);
                stmt.setLong(1, tst_id);
                stmt.setLong(2, dbque.que_res_id);
                ResultSet rs = stmt.executeQuery();          
                if(rs.next())
                {
                    int sm = rs.getInt("rcn_score_multiplier");
                    score_multiplier = (sm>0) ? sm : 1;
                }
                else
                    score_multiplier = 1; 
                    //throw new qdbException("Failed to get ResourceContent: tst id=" + tst_id + ", que id=" + dbque.que_res_id);
                stmt.close();
            }
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());   
        }
    }
    
    private void validate()
        throws qdbException
    {
        if(dbque.ints==null || dbque.ints.size()==0)
            throw new qdbException("Interactions not available");
        // check if atms ready
        if(atms==null || atms.size()!=dbque.ints.size() )
            throw new qdbException("Attempts not available");
    }
    
    //core 4.6
    public void mark(boolean negScore, int diffMultiplier)
        throws qdbException
    {
    	mark(negScore, diffMultiplier, null);
    }
    
    public void mark(boolean negScore, int diffMultiplier, Hashtable hs_int_score)
    	throws qdbException
	{
	    validate();
	    // for each interaction
	    int isize = dbque.ints.size();
	    dbInteraction intr = null;
	    dbProgressAttempt atmi = null;
	    dbProgressAttempt atmm = null;
	    Hashtable intScore = null;
	    boolean isMatch=true;
	    // reset que_score
	    que_score = 0;
	    que_max_score = 0;
	    score_multiplier *= diffMultiplier; 
	
	    for(int i=0;i<isize;i++)
	    {
	        intr = (dbInteraction) dbque.ints.elementAt(i);
	        atmi = (dbProgressAttempt)atms.elementAt(i);
//	        if(intr.int_order!=atmi.atm_int_order)
//	            throw new qdbException("Interaction and Attempt order not match");
	        int k=i;
	        if(intr.int_order!=atmi.atm_int_order){
	        	//让配对题结果和题号相匹配
			    for(int j=0;j<isize;j++){
			     	atmi = (dbProgressAttempt)atms.elementAt(j);
			        if(intr.int_order==atmi.atm_int_order){
			        	k=j;
			        	isMatch=false;
			        	break;
			        }
			    }
			    if(isMatch){
			        throw new qdbException("Interaction and Attempt order not match");
			    }
		    }
	        // mark interaction and replace atmi with atmm
	        if (hs_int_score != null) {
	        	intScore = (Hashtable)hs_int_score.get(new Long(intr.int_res_id) + "_" + intr.int_order);
	        } else {
	        	intScore = null;
	        }
	        atmm = intr.mark(atmi.atm_response_bil, atmi.atm_responses, atmi.atm_response_bil_ext, score_multiplier, flag_ind, negScore, intScore);
	        atms.setElementAt(atmm, k);
	        
	        que_score += atmm.atm_score;
	        que_max_score += atmm.atm_max_score;
	    }
	    return;
	}
    
    public static void get_test(Connection con, Vector qdbQues, ExportController controller)
    throws SQLException 
    {
        PreparedStatement stmt = null;
        try {
            dbQuestion dbq ;
            Hashtable HS_Ques_ins = new Hashtable();
            Vector V_Ques =new Vector();
            long QueId ;
            qdbQueInstance q = null;
            StringBuffer temp_sql = new StringBuffer("(");
            for(int i=0;i<qdbQues.size();i++)
            {
              q = (qdbQueInstance) qdbQues.elementAt(i);  
              temp_sql.append(q.dbque.que_res_id).append(",");
              Long QueId_obj = new Long(q.dbque.que_res_id);
              HS_Ques_ins.put(QueId_obj,q);
            }
            temp_sql.append("0)");
            
            StringBuffer SQL = new StringBuffer();
            SQL.append("select ");
            SQL.append(" curQue.res_id, curQue.res_lan ,curQue.res_title ,curQue.res_desc ,curQue.res_type ,curQue.res_subtype ,curQue.res_annotation ,curQue.res_format ,curQue.res_difficulty ,curQue.res_duration ,curQue.res_privilege ,curQue.res_status ,curQue.res_usr_id_owner ,curQue.res_create_date ,curQue.res_tpl_name ,curQue.res_res_id_root ,curQue.res_mod_res_id_test ,curQue.res_upd_user ,curQue.res_upd_date ,curQue.res_src_type ,curQue.res_src_link, res_instructor_name, res_instructor_organization, ");
            SQL.append(" Question.que_xml, Question.que_score, Question.que_type, Question.que_int_count, Question.que_prog_lang, Question.que_media_ind, Question.que_submit_file_ind,");
            SQL.append(" Interaction.int_res_id, Interaction.int_label, Interaction.int_order, Interaction.int_xml_outcome,  Interaction.int_xml_explain, Interaction.int_res_id_explain, Interaction.int_res_id_refer");
            SQL.append(" from Resources curQue inner join Question on (curQue.res_id = que_res_id)");
            SQL.append(" inner join Interaction on (curQue.res_id = int_res_id)");
            SQL.append(" where curQue.res_id in ").append(temp_sql.toString());
            SQL.append(" order by Question.que_type, curQue.res_id, Interaction.int_order");
            stmt = con.prepareStatement(SQL.toString());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                QueId = rs.getLong("res_id");
                Long QueId_obj = new Long(QueId);
                if(HS_Ques_ins.containsKey(QueId_obj)){
                    q = (qdbQueInstance)HS_Ques_ins.get(QueId_obj);
                    dbq = q.dbque;
                    if(!V_Ques.contains(QueId_obj)){
                        V_Ques.add(QueId_obj);
                        dbq.ints = new Vector();
                        dbq.res_id = QueId;
                        dbq.que_res_id = QueId;
                        dbq.res_lan = rs.getString("res_lan");
                        dbq.res_title = rs.getString("res_title");
                        dbq.res_desc = rs.getString("res_desc");
                        dbq.res_type = rs.getString("res_type");
                        dbq.res_subtype = rs.getString("res_subtype");
                        dbq.res_annotation = rs.getString("res_annotation");
                        dbq.res_format = rs.getString("res_format");
                        dbq.res_difficulty = rs.getInt("res_difficulty");
                        dbq.res_duration = rs.getFloat("res_duration");
                        dbq.res_privilege = rs.getString("res_privilege");
                        dbq.res_status = rs.getString("res_status");
                        dbq.res_usr_id_owner = rs.getString("res_usr_id_owner");
                        dbq.res_create_date = rs.getTimestamp("res_create_date");
                        dbq.res_tpl_name = rs.getString("res_tpl_name");
                        dbq.res_res_id_root = rs.getLong("res_res_id_root");
                        dbq.res_mod_res_id_test = rs.getLong("res_mod_res_id_test");
                        dbq.res_upd_user = rs.getString("res_upd_user");
                        dbq.res_upd_date = rs.getTimestamp("res_upd_date");
                        dbq.res_src_type = rs.getString("res_src_type");
                        dbq.res_src_link = rs.getString("res_src_link");
                        dbq.res_instructor_name = rs.getString("res_instructor_name");
                        dbq.res_instructor_organization = rs.getString("res_instructor_organization");
                        dbq.que_xml         = cwSQL.getClobValue(rs, "que_xml");
                        dbq.que_score       = rs.getInt("que_score");
                        dbq.que_type        = rs.getString("que_type");
                        dbq.que_int_count   = rs.getInt("que_int_count");
                        dbq.que_prog_lang   = rs.getString("que_prog_lang");
                        dbq.que_media_ind   = rs.getBoolean("que_media_ind");
                        dbq.que_submit_file_ind = rs.getBoolean("que_submit_file_ind");
                        q.score_multiplier = 1;
                        
                        if(controller != null) {
                        	controller.next();
                        }                       
                    }else{
                       // dbq = (dbQuestion)HS_Ques.get(QueId_obj);
                    }
                    dbInteraction intObj = new dbInteraction();
                    intObj.int_res_id = rs.getLong("int_res_id");
                    intObj.int_label = rs.getString("int_label");
                    intObj.int_order = rs.getInt("int_order");
                    intObj.int_xml_outcome  = cwSQL.getClobValue(rs, "int_xml_outcome");
                    intObj.int_xml_explain  = cwSQL.getClobValue(rs, "int_xml_explain");
                    intObj.int_res_id_explain   = rs.getLong("int_res_id_explain");
                    intObj.int_res_id_refer     = rs.getLong("int_res_id_refer");
                    dbq.ints.add(intObj);
                    //System.out.println(dbq.ints.size());
                }
            }
            
        }
        finally {
            if(stmt!=null) {stmt.close();}
        }
    }
    
    public static void get_test(Vector qdbQues, Hashtable HS_Ques) {
    	qdbQueInstance q = null;
    	Long que_res_id = null;
    	for (int i = 0; i < qdbQues.size(); i++) {
            q = (qdbQueInstance) qdbQues.elementAt(i); 
            que_res_id = new Long(q.dbque.que_res_id);
            if (HS_Ques.containsKey(que_res_id)) {
            	//主题目
            	q.dbque = (dbQuestion)HS_Ques.get(que_res_id);
            } else {
            	//取对应res_id的子题目
            	q.dbque = getSubQue(HS_Ques, que_res_id);
            }
        }
    }
    
    private static dbQuestion getSubQue(Hashtable HS_Ques, Long que_res_id) {
    	Enumeration enu = HS_Ques.keys();
    	dbQuestion dbq = null;
    	Hashtable ques = null;
    	Object obj = null;
    	Long que_id = null;
    	int i;
    	while (enu.hasMoreElements()) {
    		obj = enu.nextElement();
    		if (obj instanceof Long) {
    			que_id = (Long)obj;
    			dbq = (dbQuestion)HS_Ques.get(que_id);
    			if (((Vector)dbq.sub_que_vec).size() > 0) {
    				ques = (Hashtable)dbq.sub_que_vec.get(0);
        			if (ques.containsKey(que_res_id)) {
        				return (dbQuestion)ques.get(que_res_id);
        			}
    			}	
    		}
    	}
    	return null;
    }
}