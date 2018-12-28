package com.cw.wizbank.qdb;

import java.sql.*;
import java.util.*;

import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;

public class QStat extends Object
{

    public static int percentageByObj(Connection con, long modId, String usrId, long objId)
        throws qdbException
    {
        try {
            int percentage = 0;
            int total_percent = 0;
            int count = 0;
                
            int i;
            
            Vector objVec = dbObjective.getSelfAndChildsObjId(con,objId); 

            String obj_lst = cwUtils.vector2list(objVec);
            
            StringBuffer sqlBuf = new StringBuffer();
            sqlBuf.append(" SELECT SUM(atm_score),SUM(atm_max_score) ")
                  .append(" FROM ProgressAttempt, ResourceObjective ")
                  .append(" WHERE atm_pgr_res_id = ? ")
                  .append(" AND atm_pgr_usr_id = ? ")
                  .append(" AND rob_obj_id IN ").append(obj_lst)
                  .append(" AND rob_res_id = atm_int_res_id ")
                  .append(" GROUP BY rob_obj_id ");
            PreparedStatement stmt = con.prepareStatement(sqlBuf.toString());
            stmt.setLong(1, modId);
            stmt.setString(2, usrId);
            ResultSet rs = stmt.executeQuery();
                        
            while (rs.next()) {
                if ( rs.getFloat(2) > 0.0) {
                    count ++;                
                    percentage = Math.round(rs.getFloat(1)/ rs.getFloat(2)*100); 
                    total_percent += percentage; 
                }
            }
            stmt.close();

            
            int result = 0; 
            if (count ==0) 
                result = -1;
            else 
                result = Math.round(total_percent / count); 
                
            return result;
            
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }
    }

    /*
    public static int percentageByObj(Connection con, long modId, String usrId, long objId)
        throws qdbException
    {
        try {
            int percentage = 0;
            int total_percent = 0;
            int count = 0;
                
            int i;
            String qId = "";
            
            Vector objVec = new Vector(); 
            dbObjective dbobj = new dbObjective();
        
            objVec = dbObjective.getSelfAndChildsObjId(con,objId); 
            
            for (i=0; i<objVec.size(); i++) {
                long objId = ((dbObjective) objVec.elementAt(i)).obj_id;
                qId = dbProgressAttempt.getQueId(con,modId,objId);

                if (qId != "(0)") {
                    String SQL =  " SELECT SUM(atm_score),SUM(atm_max_score) from ProgressAttempt " ; 
                    SQL += " where atm_pgr_res_id = " + modId ;
                    SQL += "       AND atm_int_res_id IN " + qId;
                    SQL += "       AND atm_pgr_usr_id = ? ";
                    //SQL += "       AND atm_pgr_attempt_nbr = " + atm_number; 
                    PreparedStatement stmt = con.prepareStatement(SQL);
                    stmt.setString(1, usrId);
                    ResultSet rs = stmt.executeQuery();
                        
                    if (rs.next()) {
                        if ( rs.getFloat(2) > 0.0) {
                            count ++;                
                            percentage = Math.round(rs.getFloat(1)/ rs.getFloat(2)*100); 
                            total_percent += percentage; 
                        }
                    }
                    stmt.close();
                }
            }
            int result = 0; 
            if (count ==0) 
                result = -1;
            else 
                result = Math.round(total_percent / count); 
                
            
            return result;
            
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }
    }
*/

    
    public static Hashtable modQueRespAvg(Connection con, long modId, Hashtable qHash, long attempt_nbr, String[] group_lst)
        throws qdbException, cwSysMessage, SQLException
    {
     
        String qLst = dbProgressAttempt.getQueLst(qHash);
        String SQL = "SELECT atm_int_res_id QRESID, atm_int_order QORDER, " ;
        SQL  +=    "    atm_response_bil RESP, count(*) CNT ";
		SQL  +=    "  FROM ProgressAttempt, RegUser, Progress "; 
        SQL  +=    " WHERE  atm_pgr_usr_id = usr_id ";
        SQL  +=    "   AND  atm_pgr_res_id = " + modId;
        SQL  +=     "  AND  atm_int_res_id IN " + qLst;
        SQL  +=    " AND pgr_res_id = atm_pgr_res_id ";
        SQL  +=    " AND pgr_usr_id = atm_pgr_usr_id ";
        SQL  +=    " AND pgr_tkh_id = atm_tkh_id ";
        SQL  +=    " AND pgr_status <> ? ";
        // for a specified attempt number
    	if (attempt_nbr > 0 ) {
            SQL  +=     "  AND  atm_pgr_attempt_nbr = " + attempt_nbr ;
            SQL  +=     " AND pgr_attempt_nbr = atm_pgr_attempt_nbr ";
        }
            
        // for a specified group of student
        if (group_lst != null && group_lst.length > 0) {
            SQL +=      "  AND usr_ent_id in (select ern_child_ent_id from EntityRelation where ern_parent_ind = ? AND ern_ancestor_ent_id IN " + dbUtils.array2list(group_lst) + " )";
        }
        
        SQL  +=     " GROUP BY atm_int_res_id, atm_int_order, atm_response_bil "; 
        SQL  +=     " ORDER BY atm_int_res_id, atm_int_order, atm_response_bil ";
        
        PreparedStatement stmt = con.prepareStatement(SQL); 
		stmt.setString(1, dbProgress.PGR_STATUS_NOT_GRADED); 
        
        if (group_lst != null && group_lst.length > 0) {
        	stmt.setBoolean(2, true);
        }

        ResultSet rs = stmt.executeQuery();
        long prv_qid = -1;
        long cur_qid = -1;
        qdbQueInstance qinst = null;
        Hashtable queHash = new Hashtable();
        dbProgressAttempt patm = null;
        while (rs.next()) {
            cur_qid = rs.getLong("QRESID");
            if (cur_qid != prv_qid) {
                if (prv_qid > 0) {
                    queHash.put(new Long(prv_qid), qinst);
                }
                qinst = new qdbQueInstance();
                qinst.dbque.que_res_id = cur_qid;
            }
            
            patm = new dbProgressAttempt();
            patm.atm_int_order = rs.getLong("QORDER");
            patm.atm_response_bil = rs.getString("RESP");
            patm.response_cnt = rs.getLong("CNT");
            
            qinst.atms.addElement(patm);
            prv_qid = cur_qid;
        }
        stmt.close();
        // put the last one
        if (prv_qid > 0) {
            queHash.put(new Long(prv_qid), qinst);
        }            
        
        
        Hashtable queRespHash = new Hashtable();
        dbQuestion q = null;
		Enumeration qkeys = queHash.keys();
        while (qkeys.hasMoreElements()) {
            StringBuffer queResp = new StringBuffer();
            queResp.append("<response_stat>").append(dbUtils.NEWL);
            q = (dbQuestion) qHash.get(qkeys.nextElement());
            if(q.ints.size()>0)
            {
                dbInteraction intr = null; 
                Vector conds = null;
                Vector feedback = new Vector();
                Hashtable h = null;
                Hashtable h_conds = new Hashtable(); // NEW
                Hashtable h_score = new Hashtable(); // NEW
                dbProgressAttempt atm = null;
                String resp = "";
                float cnt = 0;
                float totalCnt = 0;
                Enumeration enumeration = null;
                Object obj = null;
                float avg = 0;
                String int_type="";
                for(int i=0;i<q.ints.size();i++)
                {
                    intr = (dbInteraction) q.ints.elementAt(i);   
                    int_type=intr.getIntType();
                    feedback = intr.getFeedback();
                    conds = (Vector) feedback.elementAt(0);
                    //if(resps.size()==0) 
                    //   throw new qdbException("No interaction responses: que=" + intr.int_res_id + " iorder=" + intr.int_order); 
                    h = new Hashtable();
                    h_conds.put (Integer.toString(i+1), conds);                 // NEW
                    h_score.put (Integer.toString(i+1), feedback.elementAt(1)); // NEW
                    for(int j=0;j<conds.size();j++) {
                        String unesc_resp = (String) conds.elementAt(j);
                        unesc_resp = dbUtils.unEscXML(unesc_resp);
                        h.put( unesc_resp, new Float(0));
                    }
                    // run query
                    //stmt1.setLong(1, intr.int_order);
                    //rs1 = stmt1.executeQuery();
                    totalCnt = 0;
                    qinst = (qdbQueInstance) queHash.get(new Long(q.que_res_id));
                    for (int j=0;j<qinst.atms.size();j++) {
                        atm = (dbProgressAttempt) qinst.atms.elementAt(j);
                        if (atm.atm_int_order == intr.int_order) {
                            //resp = rs1.getString("RESP");
                            resp = atm.atm_response_bil;
                            String resps_[] = dbUtils.split(resp , dbProgressAttempt.RESPONSE_DELIMITER); 
                            //cnt  = rs1.getFloat("CNT");
                            cnt = atm.response_cnt;
                            totalCnt += cnt;
                            
                            // resp may be null
                            if (resps_ != null && resps_.length > 0 && resps_[0] !=null) {
                                for (int k=0 ; k<resps_.length; k ++) {
                                    if (resps_[k] !=null && resps_[k].length() >0) {
                                        float ecnt_ = cnt;
                                        if (h.containsKey(resps_[k])) {
                                            ecnt_ += ((Float) h.get(resps_[k])).floatValue();
                                        }
                                        h.put(resps_[k], new Float(ecnt_)); 
                                    }
                                }
                            }// end null resp
                        }// end correct interaction order
                    }// end loop for each interaction and response
                     
                    // format xml
                    enumeration = h.keys();
                    while(enumeration.hasMoreElements())
                    {
                    obj = enumeration.nextElement();
                    avg = (totalCnt==0) ? 0: ((Float) h.get(obj) ).floatValue() / totalCnt * 100;
                       
                    if (avg > 100)
                            avg = 100; 

                    queResp.append("<item que_id=\"").append(q.que_res_id).append("\" int_order=\"") 
                          .append(intr.int_order).append("\" int_type=\"").append(int_type).append("\" response=\"")
                          .append(dbUtils.esc4XML((String) obj)).append("\" percentage=\"").append(Math.round(avg))
                          .append("\"></item>").append(dbUtils.NEWL);
                    }
                }// end for each question's interaction
               
            }// end if question's interaction is > 0
            
            queResp.append("</response_stat>").append(dbUtils.NEWL);
            queRespHash.put(new Long(q.que_res_id), queResp);
        }
        
        return queRespHash;
        
    }
    public static String modAvgByObj(Connection con, long modId, long attempt_nbr, String[] group_lst, dbModule dbmod)
        throws qdbException, cwSysMessage
    {
        try {
            /*randy
            dbModule dbmod = new dbModule(); 
            dbmod.res_id = modId; 
            dbmod.mod_res_id = modId; 
            dbmod.get(con); 
            */
            StringBuffer sqlBuf = new StringBuffer();
            String groupBy = new String();
            
                       
            sqlBuf.append("SELECT SUM(atm_score),SUM(atm_max_score), rob_obj_id ")
            .append(" FROM ProgressAttempt, RegUser, ResourceObjective, Progress ")
            .append(" WHERE rob_res_id = atm_int_res_id ")
            .append(" And pgr_res_id = atm_pgr_res_id ")
            .append(" And pgr_attempt_nbr = atm_pgr_attempt_nbr ")
            .append(" And pgr_usr_id = atm_pgr_usr_id ")
            .append(" And pgr_tkh_id = atm_tkh_id ")
            .append(" And pgr_status <> ? ");

            groupBy = "GROUP BY rob_obj_id";

            /*
            if (dbmod.mod_type.equalsIgnoreCase(dbModule.MOD_TYPE_DXT) ||
                dbmod.mod_type.equalsIgnoreCase(dbModule.MOD_TYPE_STX)) {
                sqlBuf.append("SELECT SUM(atm_score),SUM(atm_max_score), rob_obj_id ")
                      .append(" FROM ProgressAttempt, RegUser, ResourceObjective ")
                      .append(" WHERE rob_res_id = atm_int_res_id ");

                groupBy = "GROUP BY rob_obj_id";
    
            }else  {
                sqlBuf.append("SELECT SUM(atm_score),SUM(atm_max_score), rcn_obj_id_content ")
                      .append(" FROM ProgressAttempt, RegUser, ResourceContent ")
                      .append(" WHERE rcn_res_id = atm_pgr_res_id ")
                      .append(" AND rcn_res_id_content = atm_int_res_id ");

                groupBy = "GROUP BY rcn_obj_id_content ";
            }
            */
            
            sqlBuf.append(" AND atm_pgr_usr_id = usr_id ")
                  .append(" AND atm_pgr_res_id = ? ")
                  .append(" AND atm_score != ? ");

            // for a specified attempt number result 
            if (attempt_nbr > 0) 
				sqlBuf.append("       AND atm_pgr_attempt_nbr = ").append(attempt_nbr).append(" "); 
            // for a specified group of student
            if (group_lst !=null && group_lst.length > 0)
                sqlBuf.append("       AND usr_ent_id in (select ern_child_ent_id from EntityRelation where ern_parent_ind = ? AND ern_ancestor_ent_id IN " + dbUtils.array2list(group_lst) + " )");

            sqlBuf.append(groupBy);

            //if mod contain a SC question,the sub que of SC can get obj_id by SC question.
            sqlBuf.append(" union ")
            .append(" SELECT SUM(atm_score),SUM(atm_max_score), rob_obj_id ")
            .append(" FROM ProgressAttempt, RegUser, ResourceObjective, Progress, ResourceContent, Resources ")
            .append(" WHERE Atm_Int_Res_Id = rcn_res_id_content ")
            .append(" And rob_res_id = rcn_res_id ")
            .append(" And pgr_res_id = atm_pgr_res_id ")
            .append(" and rcn_res_id = res_id ")
            .append(" and (res_subtype = ? or res_subtype = ? ) ")
            .append(" And pgr_attempt_nbr = atm_pgr_attempt_nbr ")
            .append(" And pgr_usr_id = atm_pgr_usr_id ")
            .append(" And pgr_tkh_id = atm_tkh_id ")
            .append(" And pgr_status <> ? ")
            .append(" AND atm_pgr_usr_id = usr_id ")
                  .append(" AND atm_pgr_res_id = ? ")
                  .append(" AND atm_score != ? ");

            // for a specified attempt number result 
            if (attempt_nbr > 0) 
                sqlBuf.append("       AND atm_pgr_attempt_nbr = ").append(attempt_nbr).append(" "); 
            // for a specified group of student
            if (group_lst !=null && group_lst.length > 0)
                sqlBuf.append("       AND usr_ent_id in (select ern_child_ent_id from EntityRelation where ern_parent_ind = ? AND ern_ancestor_ent_id IN " + dbUtils.array2list(group_lst) + " )");

            sqlBuf.append(groupBy);

                
            int percentage = 0;
            long objId = 0;
            PreparedStatement stmt = con.prepareStatement(sqlBuf.toString());
            int index = 1;
            stmt.setString(index++, dbProgress.PGR_STATUS_NOT_GRADED);
            stmt.setLong(index++, modId);
            stmt.setLong(index++, -1);
            if (group_lst !=null && group_lst.length > 0)
            	stmt.setBoolean(index++, true);
            
            stmt.setString(index++, dbResource.RES_SUBTYPE_FSC);
            stmt.setString(index++, dbResource.RES_SUBTYPE_DSC);
            stmt.setString(index++, dbProgress.PGR_STATUS_NOT_GRADED);
            stmt.setLong(index++, modId);
            stmt.setLong(index++, -1);
            if (group_lst !=null && group_lst.length > 0)
            	stmt.setBoolean(index++, true);
            ResultSet rs = stmt.executeQuery();
            StringBuffer xmlBuf = new StringBuffer();
			boolean flag = false;
            Hashtable objTotalScoreHash = new Hashtable();
            Hashtable objTotalMaxScoreHash = new Hashtable();
            float sumTotal = 0;
            float sumMax = 0;
            while (rs.next()) {
				flag = true;
                objId = rs.getLong(3);
                /*
                float sumTotal = rs.getFloat(1);
                float sumMax = rs.getFloat(2);
*/                
                
                if (objTotalScoreHash.containsKey(new Long(objId))) {
                    float tempTotal = ((Float)objTotalScoreHash.get(new Long(objId))).floatValue();
                    float tempTotalMax = ((Float)objTotalMaxScoreHash.get(new Long(objId))).floatValue();
                    sumTotal = rs.getFloat(1) + tempTotal;
                    sumMax = rs.getFloat(2) + tempTotalMax;
                } else {
                    sumTotal = rs.getFloat(1);
                    sumMax = rs.getFloat(2);
                }
                objTotalScoreHash.put(new Long(objId), new Float(sumTotal));
                objTotalMaxScoreHash.put(new Long(objId), new Float(sumMax));
                
//                percentage = Math.round(rs.getFloat(1)/ rs.getFloat(2)*100); 
//				xmlBuf.append("<objective id=\"").append(objId).append("\" percentage=\"")
//					.append(percentage).append("\"></objective>").append(dbUtils.NEWL);
            }
            
            if (!objTotalScoreHash.isEmpty()) {
                Enumeration emKey = objTotalScoreHash.keys();
                while (emKey.hasMoreElements()) {
                    Long obj_id = (Long)emKey.nextElement();
                    sumTotal =  ((Float)objTotalScoreHash.get(obj_id)).floatValue();
                    sumMax =  ((Float)objTotalMaxScoreHash.get(obj_id)).floatValue();
                    percentage = Math.round(sumTotal/ sumMax*100); 
                    xmlBuf.append("<objective id=\"").append(obj_id.longValue()).append("\" percentage=\"")
                        .append(percentage).append("\"></objective>").append(dbUtils.NEWL);
                }
            }
            
            stmt.close();
			if( !flag ) {
				String SQL = " Select Distinct rob_obj_id " +
					"From ProgressAttempt, ResourceObjective " +
					"Where atm_pgr_res_id = ? " +
					"And atm_int_res_id = rob_res_id ";
				stmt = con.prepareStatement(SQL);
				stmt.setLong(1, modId);
				rs = stmt.executeQuery();
				while(rs.next()){
					xmlBuf.append("<objective id=\"").append(rs.getLong(1)).append("\"/>"); 
				}
				cwSQL.cleanUp(rs, stmt);
			}
            
            return xmlBuf.toString();
            
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }
    }

    //public static String modQueRespAvg(Connection con, long modId, long queId, long attempt_nbr, Vector usrVec)
    public static String modQueRespAvg(Connection con, long modId, long queId, long attempt_nbr, String[] group_lst)
        throws qdbException, cwSysMessage
    {
      try {
        
        String result="<response_stat>" + dbUtils.NEWL;
        // for MC
		String SQL = "SELECT atm_response_bil RESP, count(*) CNT FROM ProgressAttempt, RegUser, Progress "; 
        SQL  +=    " WHERE  atm_pgr_usr_id = usr_id ";
        SQL  +=    "   AND  atm_pgr_res_id = " + modId; 
        SQL  +=     "  AND  atm_int_res_id = " + queId; 
		SQL  +=		" And pgr_usr_id = atm_pgr_usr_id ";
		SQL  +=		" And pgr_res_id = atm_pgr_res_id ";
		SQL  +=		" And pgr_tkh_id = atm_tkh_id ";
		SQL  +=		" And pgr_status <> ? ";
        SQL  +=     "  AND  atm_int_order  = ? "; 
        // for a specified attempt number
		if (attempt_nbr > 0 ) {
            SQL  +=     "  AND  atm_pgr_attempt_nbr = " + attempt_nbr ; 
			SQL  +=		" And pgr_attempt_nbr = atm_pgr_attempt_nbr ";
		} 
        // for a specified group of student
        if (group_lst != null && group_lst.length > 0) {
            SQL +=      "  AND usr_ent_id in (select ern_child_ent_id from EntityRelation where ern_parent_ind = ? AND ern_ancestor_ent_id IN " + dbUtils.array2list(group_lst) + " )";
        }
        
        SQL  +=     " GROUP BY atm_response_bil "; 
        
        PreparedStatement stmt1 = con.prepareStatement(SQL); 

        ResultSet rs1 = null;
                
        dbQuestion q = new dbQuestion();
        q.que_res_id = queId;
        q.get(con);
        if(q.ints.size()>0)
        {
           dbInteraction intr = null; 
           Vector conds = null;
           Vector feedback = new Vector();
           Hashtable h = null;
           Hashtable h_conds = new Hashtable(); // NEW
           Hashtable h_score = new Hashtable(); // NEW           
           String resp = "";
           float cnt = 0;
           float totalCnt = 0;
           Enumeration enumeration = null;
           Object obj = null;
           float avg = 0;
           String int_type="";
           for(int i=0;i<q.ints.size();i++)
           {
             intr = (dbInteraction) q.ints.elementAt(i);   
             int_type=intr.getIntType();
             feedback = intr.getFeedback();
             conds = (Vector) feedback.elementAt(0);
             //if(resps.size()==0) 
             //   throw new qdbException("No interaction responses: que=" + intr.int_res_id + " iorder=" + intr.int_order); 
             h = new Hashtable();
             h_conds.put (Integer.toString(i+1), conds);                 // NEW
             h_score.put (Integer.toString(i+1), feedback.elementAt(1)); // NEW
             for(int j=0;j<conds.size();j++) {
                String unesc_resp = (String) conds.elementAt(j);
                unesc_resp = dbUtils.unEscXML(unesc_resp);
                h.put( unesc_resp, new Float(0));
             }
             // run query
			stmt1.setString(1, dbProgress.PGR_STATUS_NOT_GRADED);
			stmt1.setLong(2, intr.int_order);
             
             if (group_lst != null && group_lst.length > 0) {
            	 stmt1.setBoolean(3, true);
             }
            
             rs1 = stmt1.executeQuery();
             totalCnt = 0;
             while(rs1.next())
             {
                resp = rs1.getString("RESP");
                String resps_[] = dbUtils.split(resp , dbProgressAttempt.RESPONSE_DELIMITER); 
                cnt  = rs1.getFloat("CNT");
                totalCnt += cnt;
                
                // resp may be null
                if (resps_ != null && resps_.length > 0 && resps_[0] !=null) {
                    for (int k=0 ; k<resps_.length; k ++) {
                        if (resps_[k] !=null && resps_[k].length() >0) {
                            float ecnt_ = cnt;
                            if (h.containsKey(resps_[k])) {
                                ecnt_ += ((Float) h.get(resps_[k])).floatValue();
                            }
                            h.put(resps_[k], new Float(ecnt_)); 
                        }
                    }
                }
             }
             
             // format xml
             enumeration = h.keys();
             while(enumeration.hasMoreElements())
             {
               obj = enumeration.nextElement();
               avg = (totalCnt==0) ? 0: ((Float) h.get(obj) ).floatValue() / totalCnt * 100;
               
               if (avg > 100)
                    avg = 100; 

               result += "<item que_id=\"" + queId + "\" int_order=\"" 
                 + intr.int_order + "\" int_type=\"" + int_type + "\" response=\""
                 + dbUtils.esc4XML((String) obj) + "\" percentage=\"" + Math.round(avg) + "\"></item>" + dbUtils.NEWL;
             }
           }
           stmt1.close();
// Enhancement begin
       // adding the following condition can improve the performence (but need to change XSL) 
       //      if (q.ints.size() > 1 ||
       //         (((Vector) h_conds.get("1")).size() > 1)) {

		   SQL  = "SELECT atm_response_bil RESP, atm_int_order INT_ORDER, atm_score SCORE FROM ProgressAttempt, Progress " +
                    " WHERE atm_pgr_res_id = " + modId + 
                    " AND atm_int_res_id = " + queId +
					" AND atm_pgr_usr_id = ? " +
					"And pgr_res_id = atm_pgr_res_id " +
					"And pgr_tkh_id = atm_tkh_id " +
					"And pgr_usr_id = atm_pgr_usr_id And pgr_status <> ?";
                
            // for a specified attempt number
			if (attempt_nbr > 0 ) {
                SQL  += "  AND  atm_pgr_attempt_nbr = " + attempt_nbr ; 
				SQL  += " And pgr_attempt_nbr = atm_pgr_attempt_nbr ";
			}

            stmt1 = con.prepareStatement(SQL); 
            rs1 = null;
                           
            int flag_wrong = 0;
            int flag_givenup = 0;
            int correct = 0;
            int wrong = 0;
            int half_correct = 0;
            int givenup = 0;
			int not_graded = 0;
            long score = 0;
            float total_score = 0;

            String order = "";
            String user = null;
            Vector v_conds = null;
            Vector v_scores = null;

			String SQL2 = "SELECT DISTINCT atm_pgr_usr_id " +
							"FROM ProgressAttempt, RegUser, Progress " + 
                            " WHERE atm_pgr_usr_id = usr_id " +
                            "  AND atm_pgr_res_id = " + modId +
							" AND atm_int_res_id = " + queId +
							" And pgr_res_id = atm_pgr_res_id " +
							" And pgr_usr_id = atm_pgr_usr_id " +
							" And pgr_tkh_id = atm_tkh_id " +
							" And pgr_status <> ? ";
                              
            if (group_lst != null && group_lst.length > 0) {
                SQL2 += "  AND usr_ent_id in (select ern_child_ent_id from EntityRelation where ern_parent_ind = ? AND ern_ancestor_ent_id IN " + dbUtils.array2list(group_lst) + " )";
            }
                
            if (attempt_nbr > 0) {
                SQL2 += " AND atm_pgr_attempt_nbr = " + attempt_nbr;
				SQL2 += " AND pgr_attempt_nbr = atm_pgr_attempt_nbr ";
            }
                    
            PreparedStatement stmt2 = con.prepareStatement(SQL2);
			stmt2.setString(1, dbProgress.PGR_STATUS_NOT_GRADED);
            if (group_lst != null && group_lst.length > 0) {
            	stmt2.setBoolean(2, true);
            }
            ResultSet rs2 = stmt2.executeQuery();        
                    
            while (rs2.next()) {
                user = rs2.getString("atm_pgr_usr_id");
                stmt1.setString(1, user); 
				stmt1.setString(2, dbProgress.PGR_STATUS_NOT_GRADED);
                                   
                rs1 = stmt1.executeQuery();

                flag_wrong = -1;
                flag_givenup = -1;
                score = 0;
                
                // for each interaction
                while (rs1.next()) {
                    order = rs1.getString("INT_ORDER");
                    resp = rs1.getString("RESP");
					if(!q.que_type.equalsIgnoreCase(dbQuestion.QUE_TYPE_ESSAY)) {
                        v_conds = (Vector) h_conds.get(order);
                        v_scores = (Vector) h_score.get(order);

                        String resps_[] = dbUtils.split(resp, dbProgressAttempt.RESPONSE_DELIMITER);

                        if (resp == null &&
                            !(resps_ != null && resps_.length > 0 && resps_[0] != null)) {
                            if (flag_wrong == -1) {
                                flag_givenup = 1;
                            }
                        } else {
                            Hashtable cond_score_pair = new Hashtable();

                            for (int i = 0; i < v_conds.size(); i++) {
                                String unesc_resp = (String) v_conds.elementAt(i);
                                unesc_resp = dbUtils.unEscXML(unesc_resp);
                                cond_score_pair.put(unesc_resp, v_scores.elementAt(i));
                            }

                            for (int k = 0; k  < resps_.length; k++) {
                                if (cond_score_pair.containsKey(resps_[k])) {
                                    score += ((Long)cond_score_pair.get(resps_[k])).longValue();
                                } else {
                                    flag_wrong = 1;
                                }    
                            }
                        }
                    } else {
                        score = rs1.getLong("SCORE");
                        if(score == 0) {
                            if(resp.equals("")) {
                                flag_wrong = 0;
                            } else {
                                flag_wrong = 1;
                            }
                        }
                    }
                }
                
                if (score == q.que_score) {
                    correct++;
                    total_score += score;
                } else if (score == -1) {
                    not_graded++;
                } else if (score != 0) {
                    half_correct++;
                    total_score += score;
                } else if (score == 0 && flag_wrong == 1) {
                    wrong++;
                } else { // i.e. score == 0 && flag_givenup == 1
                    givenup++;
                }
            }
            
            stmt1.close();
            stmt2.close();
            
            result += "<count " + 
                    "correct=\""        + correct      + "\" " +
                    "wrong=\""          + wrong        + "\" " +
                    "half_correct=\""   + half_correct + "\" " +
                    "given_up=\""       + givenup      + "\" " +
                    "not_graded=\""     + not_graded   + "\" ";
                    if((correct+wrong+half_correct+givenup)==0) {
                        result +="average_score=\"--\"/>";
                    } else {
                        result +="average_score=\""  + total_score/(correct+wrong+half_correct+givenup) + "\"/>";
                    }
        //      }  // for the above condition
    
// Enhancement End                           
        }
        else
            throw new qdbException("Question with no interaction: " + queId);
        
        // close
        result += "</response_stat>";

        //if (usrVec != null) {
        //    cwSQL.dropTempTable(con, tableName);
        //}
        
        return result;    
      } catch(SQLException e) {
          throw new qdbException("SQL Error: " + e.getMessage());   
      }
    }
    
    // Get results of a list of questions
    
     // get the score average (percentage) of a test from a list of student
     public static String modAvgByGroup(Connection con, long res_id, long pgr_attempt_nbr, String[] group_lst)
        throws qdbException, qdbErrMessage
    {
        try {
            
            String SQL = "SELECT  pgr_score PSCORE, pgr_max_score PMSCORE " +
						" FROM Progress, RegUser, Module " +
						" WHERE pgr_usr_id = usr_id And pgr_score != -1 " +
						" And pgr_res_id = mod_res_id " +
						" And pgr_status <> ? " +
                         " AND pgr_res_id =" + res_id;
                         
            if (pgr_attempt_nbr > 0 ){
                SQL +=  " AND pgr_attempt_nbr = " + pgr_attempt_nbr;    
            }
            if (group_lst != null && group_lst.length > 0) {
                SQL += "  AND usr_ent_id in (select ern_child_ent_id from EntityRelation where ern_parent_ind = ? AND ern_ancestor_ent_id IN " + dbUtils.array2list(group_lst) + " )";
            }

            PreparedStatement stmt = con.prepareStatement(SQL);
			stmt.setString(1, dbProgress.PGR_STATUS_NOT_GRADED);
            if (group_lst != null && group_lst.length > 0) {
                stmt.setBoolean(2, true);
            }
            ResultSet rs = stmt.executeQuery();
            
            float total_average = 0;
            long num_taken = 0;
            
            while (rs.next()) {
                total_average += rs.getFloat("PSCORE")/rs.getFloat("PMSCORE");
                num_taken++;
            }
            stmt.close();
            /*
            if (num_taken==0) {
                // No statistic for these group of learners
                throw new qdbErrMessage("PGR007");
            }
            */
			String xml = null;
			if( num_taken > 0 ) {
				xml = "<group percentage=\"" + Math.round((total_average/num_taken)*100) + "\"/>";
			}
			return xml;
             
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());   
        }
    }        
}