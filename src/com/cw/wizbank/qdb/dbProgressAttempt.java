package com.cw.wizbank.qdb;

import java.sql.*;
import java.util.*;

import com.cw.wizbank.util.cwUtils;

public class dbProgressAttempt
{
    public static final String RESPONSE_DELIMITER = "[|]"; 
    
    public String       atm_pgr_usr_id;
    public long         atm_pgr_res_id;
    public long         atm_pgr_attempt_nbr;
    public long         atm_int_res_id;
    public long         atm_int_order;
    public String       atm_response_bil;
    public String       atm_response_bil_ext;
    public boolean      atm_correct_ind;
    public boolean      atm_flag_ind;
    public long         atm_score;
    public long         atm_max_score;
    public Timestamp    atm_upd_datetime;
    public long         atm_order;
    public long         atm_tkh_id;
    
    public String[]     atm_responses;
    public Timestamp    pgr_complete_datetime;
    
    public long         response_cnt;
    
    public dbProgressAttempt() {;}
    
    public void get(Connection con,long pgr_tkh_id)
        throws qdbException
    {
        // Given atm_pgr_usr_id, atm_pgr_res_id, atm_pgr_attempt_nbr , atm_int_res_id
        try{
            PreparedStatement stmt = con.prepareStatement( 
                " SELECT atm_int_order, atm_response_bil, atm_correct_ind, "
              + "    atm_flag_ind, atm_score, "
              + "    atm_max_score, atm_upd_datetime, atm_order " 
              + " FROM ProgressAttempt "
              + " WHERE  "
              + "        atm_pgr_usr_id = ? AND "
              + "        atm_pgr_res_id = ? AND "
              + "        atm_int_res_id = ? AND "
              + "        atm_pgr_attempt_nbr = ? AND"
              + "        atm_tkh_id = ? ");  
              
            stmt.setString(1,atm_pgr_usr_id);
            stmt.setLong(2,atm_pgr_res_id);
            stmt.setLong(3,atm_int_res_id);
            stmt.setLong(4,atm_pgr_attempt_nbr);
            stmt.setLong(5,pgr_tkh_id);
            
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                this.atm_int_order = rs.getInt("atm_int_order");
                this.atm_response_bil = rs.getString("atm_response_bil");
                this.atm_correct_ind = rs.getBoolean("atm_correct_ind");
                this.atm_flag_ind = rs.getBoolean("atm_flag_ind");
                this.atm_score = rs.getLong("atm_score");
                this.atm_max_score = rs.getLong("atm_max_score");
                this.atm_upd_datetime = rs.getTimestamp("atm_upd_datetime");
                this.atm_order = rs.getLong("atm_order");
                
                if (this.atm_response_bil == null) {
                   this.atm_response_bil = "";
                }
            }
            stmt.close();
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
       
    }

    public Vector getQattempt(Connection con)
        throws qdbException
    {
        // Given atm_pgr_usr_id, atm_pgr_res_id, atm_pgr_attempt_nbr 
        // Get the list of interaction record.
        try{
            String SQL =  " SELECT atm_int_order, atm_response_bil, atm_correct_ind, "
                + "    atm_flag_ind, atm_score, "
                + "    atm_max_score, atm_upd_datetime " 
                + " FROM ProgressAttempt "
                + " WHERE  "
                + "        atm_pgr_usr_id = ? AND "
                + "        atm_pgr_res_id = ? AND "
                + "        atm_int_res_id = ? AND "
                + "        atm_pgr_attempt_nbr = ? " ;
            if(atm_tkh_id > 0){
                SQL+= "  AND atm_tkh_id = ? " ;
            }
            SQL+= "        order by atm_int_order ";
            PreparedStatement stmt = con.prepareStatement(SQL );  
              
            stmt.setString(1,atm_pgr_usr_id);
            stmt.setLong(2,atm_pgr_res_id);
            stmt.setLong(3,atm_int_res_id);
            stmt.setLong(4,atm_pgr_attempt_nbr);
            if(atm_tkh_id > 0){
                stmt.setLong(5,atm_tkh_id);
            }
            
            Vector result = new Vector();
            dbProgressAttempt atm = null;
            
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
            {
                atm = new dbProgressAttempt();
                atm.atm_pgr_usr_id = atm_pgr_usr_id; 
                atm.atm_pgr_res_id = atm_pgr_res_id; 
                atm.atm_int_res_id = atm_int_res_id; 
                
                atm.atm_int_order = rs.getInt("atm_int_order");
                atm.atm_response_bil = rs.getString("atm_response_bil");
                atm.atm_correct_ind = rs.getBoolean("atm_correct_ind");
                atm.atm_flag_ind = rs.getBoolean("atm_flag_ind");
                atm.atm_score = rs.getLong("atm_score");
                atm.atm_max_score = rs.getLong("atm_max_score");
                atm.atm_upd_datetime = rs.getTimestamp("atm_upd_datetime");
                
                if (atm.atm_response_bil == null) {
                   atm.atm_response_bil = "";
                }
                
                result.addElement(atm);
            }
            
            stmt.close();
            return result;
          
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
       
    }
    /*
        public Vector getTstAss(Connection con, Vector usr_id_lst, long pgr_res_id, long pgr_attempt_nbr)
        throws qdbException
    {
        try{
            // ** TEMP TABLE
//            String tableName = cwSQL.createSimpleTemptable(con, "tmp_usr_id", cwSQL.COL_TYPE_STRING, 30);
//            cwSQL.insertSimpleTempTable(con, tableName, usr_id_lst, cwSQL.COL_TYPE_STRING);

            PreparedStatement stmt = con.prepareStatement( 
                " SELECT atm_pgr_usr_id ,atm_int_res_id, atm_int_order, atm_response_bil, atm_correct_ind, "
              + "    atm_flag_ind, atm_score, "
              + "    atm_max_score, atm_upd_datetime " 
              + " FROM ProgressAttempt "
              + " WHERE  "
              + "        atm_pgr_usr_id IN ( SELECT tmp_usr_id FROM " + tableName + " ) AND "
              + "        atm_pgr_res_id = ? "
              + "        order by atm_int_order, atm_pgr_attempt_nbr ");  


            stmt.setLong(1, pgr_res_id);

            int order = 1; 
            
            Vector result = new Vector();
            dbProgressAttempt atm = null;
            Vector usrIds = new Vector();

            ResultSet rs = stmt.executeQuery();
            while(rs.next())
            {
                atm = new dbProgressAttempt();
                atm.atm_pgr_res_id = atm_pgr_res_id;
                atm_pgr_attempt_nbr = atm_pgr_attempt_nbr;

                atm.atm_pgr_usr_id = rs.getString("atm_pgr_usr_id"); 
                
                if (!usrIds.contains(atm.atm_pgr_usr_id)){
                    atm.atm_int_res_id = rs.getInt("atm_int_res_id"); 
                    atm.atm_int_order = rs.getInt("atm_int_order");
                    atm.atm_response_bil = rs.getString("atm_response_bil");
                    atm.atm_correct_ind = rs.getBoolean("atm_correct_ind");
                    atm.atm_flag_ind = rs.getBoolean("atm_flag_ind");
                    atm.atm_score = rs.getLong("atm_score");
                    atm.atm_max_score = rs.getLong("atm_max_score");
                    atm.atm_upd_datetime = rs.getTimestamp("atm_upd_datetime");
                    
                    if (atm.atm_response_bil == null) {
                    atm.atm_response_bil = "";
                    }
                    result.addElement(atm);
                    usrIds.addElement(atm.atm_pgr_usr_id);
                }
            }
            stmt.close();
            
            cwSQL.dropTempTable(con, tableName);
            
            return result;
          
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
       
    }
    */
    public static Vector getChildAss(Connection con,String usr_id, long pgr_res_id, long pgr_attempt_nbr, long tkh_id)
        throws qdbException
    {
        try{
            String sql = " SELECT  distinct(atm_int_res_id), atm_order, rob_obj_id "
            + " from ProgressAttempt, ResourceContent, ResourceObjective "
            + " WHERE atm_pgr_res_id = ? "
            + " and atm_int_res_id = rcn_res_id_content "
            + " and rcn_res_id = rob_res_id "
            + " AND atm_pgr_usr_id = ? "
            + " AND atm_pgr_attempt_nbr = ? "
            + " AND atm_tkh_id = ? "
            + " union "
            + " SELECT  distinct(atm_int_res_id), atm_order, rob_obj_id "
            + " FROM ProgressAttempt, ResourceObjective "
            + " WHERE atm_int_res_id = rob_res_id "
            + " and atm_pgr_res_id = ? "
            + " AND atm_pgr_usr_id = ? "
            + " AND atm_pgr_attempt_nbr = ? "
            + " AND atm_tkh_id = ? ";

            PreparedStatement stmt = con.prepareStatement(sql + " ORDER BY atm_order");
            
            int index = 1;
            stmt.setLong(index++, pgr_res_id);
            stmt.setString(index++, usr_id);
            stmt.setLong(index++, pgr_attempt_nbr);
            stmt.setLong(index++, tkh_id);
            stmt.setLong(index++, pgr_res_id);
            stmt.setString(index++, usr_id);
            stmt.setLong(index++, pgr_attempt_nbr);
            stmt.setLong(index++, tkh_id);
            
            int order = 1; 
            
            Vector result = new Vector();
            dbResourceContent resCon = null;
            
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
            {
                resCon = new dbResourceContent(); 
                resCon.rcn_res_id = pgr_res_id; 
                resCon.rcn_res_id_content = rs.getLong("atm_int_res_id");
                resCon.rcn_obj_id_content = rs.getLong("rob_obj_id");
                resCon.rcn_order =order;
                
                order ++; 
                result.addElement(resCon);
            }
            stmt.close();
            
            return result;
          
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }
    
    public static Vector getChildAss(Connection con,String[] group_lst, long pgr_res_id, long pgr_attempt_nbr)
        throws qdbException
    {
        try{
              String SQL = " SELECT  distinct(atm_int_res_id), atm_order, rob_obj_id  "
                    + " FROM ProgressAttempt, EntityRelation, RegUser, ResourceObjective WHERE "
                    + "        atm_pgr_usr_id = usr_id "
                    + "        AND usr_ent_id = ern_child_ent_id "
                    + "        AND atm_int_res_id = rob_res_id "
                    + "        AND atm_pgr_res_id = ? "
                    + "        AND atm_pgr_attempt_nbr = ? "
                    + "        AND ern_parent_ind = ? ";
                    
            if (group_lst !=null && group_lst.length > 0) {
                SQL += " AND ern_ancestor_ent_id IN " + dbUtils.array2list(group_lst);
            }                    

            PreparedStatement stmt = con.prepareStatement(SQL + "ORDER BY atm_order");
              
            stmt.setLong(1, pgr_res_id);
            stmt.setLong(2, pgr_attempt_nbr);
            stmt.setBoolean(3, true);

            int order = 1; 
            
            Vector result = new Vector();
            dbResourceContent resCon = null;
            
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
            {
                resCon = new dbResourceContent(); 
                resCon.rcn_res_id = pgr_res_id; 
                resCon.rcn_res_id_content = rs.getLong("atm_int_res_id");
                resCon.rcn_obj_id_content = rs.getLong("rob_obj_id"); 
                resCon.rcn_order =order;
                
                boolean bExists = false; 
                for  (int i=0;i<result.size() ; i++) {
                    dbResourceContent rcon = (dbResourceContent) result.elementAt(i) ;
                    if (resCon.rcn_res_id_content == rcon.rcn_res_id_content)
                        bExists = true; 
                }
                    
                if (!bExists) {
                    order ++; 
                    result.addElement(resCon);
                }
                
            }
            stmt.close();
            
            return result;
          
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
       
    }
    
    public void ins(Connection con)
        throws qdbException
    {
        try {
          PreparedStatement stmt = con.prepareStatement(
            "INSERT INTO ProgressAttempt "
          + " ( atm_pgr_usr_id "
          + "  ,atm_pgr_res_id "
          + "  ,atm_tkh_id "
          + "  ,atm_pgr_attempt_nbr "
          + "  ,atm_int_res_id "
          + "  ,atm_int_order "
          + "  ,atm_response_bil "
          + "  ,atm_response_bil_ext "
          + "  ,atm_correct_ind "
          + "  ,atm_score "
          + "  ,atm_max_score "
          + "  ,atm_flag_ind "
          + "  ,atm_upd_datetime "
          + "  ,atm_order ) "
          + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,? ) " );  
            
         int index =1;
         stmt.setString(index++, atm_pgr_usr_id);
         stmt.setLong(index++, atm_pgr_res_id);
         stmt.setLong(index++, atm_tkh_id);
         stmt.setLong(index++, atm_pgr_attempt_nbr);
         stmt.setLong(index++, atm_int_res_id);
         stmt.setLong(index++, atm_int_order);
         stmt.setString(index++, atm_response_bil);
         stmt.setString(index++, atm_response_bil_ext);
         stmt.setBoolean(index++, atm_correct_ind);
         stmt.setLong(index++, atm_score);
         stmt.setLong(index++, atm_max_score);
         stmt.setBoolean(index++, atm_flag_ind);
         Timestamp curTime = dbUtils.getTime(con);
         stmt.setTimestamp(index++,curTime);
         stmt.setLong(index++,atm_order);
         int stmtResult=stmt.executeUpdate();
         stmt.close();
         if ( stmtResult!=1)                            
         {                 
            con.rollback();
            throw new qdbException("Failed to insert ProgressAttempt");
         }

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
       }

    }
    public static boolean checkQStatExist(Connection con, long modId, long queId)
        throws qdbException
    {
        try {
            PreparedStatement stmt = con.prepareStatement(
              " SELECT COUNT(*) from ProgressAttempt "
            + " WHERE atm_pgr_res_id= ? and atm_int_res_id = ? " );
            
            stmt.setLong(1, modId);
            stmt.setLong(2, queId);
            
            ResultSet rs = stmt.executeQuery();
            
            int cnt = 0; 
            if(rs.next())
            {
                cnt = rs.getInt(1); 
            }else {
            	stmt.close();
                con.rollback(); 
                throw new qdbException("Error : Cannot get the statistic. ");
            }

            stmt.close();
            if (cnt > 0) 
               return true; 
            else
                return false; 

       } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
       }

    }
    
    
   
    
    public static Vector getAttemptedResIds(Connection con, String usr_id)
        throws qdbException
    {
        try {
            Vector resIdVec = new Vector();
              
            StringBuffer SQLBuf = new StringBuffer(128);
            SQLBuf.append(" SELECT distinct(atm_int_res_id) AS RES_ID from ProgressAttempt ")
                  .append(" WHERE atm_pgr_usr_id = '").append(usr_id).append("' order by RES_ID");
            
            PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
            ResultSet rs = stmt.executeQuery();
            
            while(rs.next())
            {
                resIdVec.addElement(new Long(rs.getLong("RES_ID")));
            }
            
            stmt.close();
            return resIdVec;
            
       } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
       }

    }

    public Hashtable getQAttempt(Connection con,Hashtable qHash)
        throws qdbException
    {
        String qList = dbProgressAttempt.getQueLst (qHash);
        // Given atm_pgr_usr_id, atm_pgr_res_id, atm_pgr_attempt_nbr 
        // Get the list of interaction record.
        try{
            PreparedStatement stmt = con.prepareStatement( 
                " SELECT atm_int_res_id, atm_int_order, atm_response_bil, atm_correct_ind, "
              + "    atm_flag_ind, atm_score, "
              + "    atm_max_score, atm_upd_datetime " 
              + " FROM ProgressAttempt "
              + " WHERE  "
              + "        atm_pgr_usr_id = ? AND "
              + "        atm_pgr_res_id = ? AND "
              + "        atm_pgr_attempt_nbr = ? AND " 
              + "        atm_tkh_id = ? AND " 
              + "        atm_int_res_id IN " + qList 
              + "        order by atm_int_res_id ASC, atm_int_order ASC ");  
              
            stmt.setString(1,atm_pgr_usr_id);
            stmt.setLong(2,atm_pgr_res_id);
            stmt.setLong(3,atm_pgr_attempt_nbr);
            stmt.setLong(4,atm_tkh_id);
            
            Hashtable attemptHash = new Hashtable();
            Vector result = null;
            dbProgressAttempt atm = null;
            long prv_que_id = 0;
            long cur_que_id = 0;
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
            {
                
                cur_que_id = rs.getLong("atm_int_res_id");
                
                if (cur_que_id != prv_que_id) {
                    if (prv_que_id > 0) {
                        attemptHash.put(new Long(prv_que_id), result);
                    }
                    result = new Vector();
                }
                
                atm = new dbProgressAttempt();
                atm.atm_pgr_usr_id = atm_pgr_usr_id; 
                atm.atm_pgr_res_id = atm_pgr_res_id; 
                atm.atm_int_res_id = cur_que_id; 
                atm.atm_int_order = rs.getInt("atm_int_order");
                atm.atm_response_bil = rs.getString("atm_response_bil");
                if (atm.atm_response_bil == null) {
                   atm.atm_response_bil = "";
                }
                atm.atm_correct_ind = rs.getBoolean("atm_correct_ind");
                atm.atm_flag_ind = rs.getBoolean("atm_flag_ind");
                atm.atm_score = rs.getLong("atm_score");
                atm.atm_max_score = rs.getLong("atm_max_score");
                atm.atm_upd_datetime = rs.getTimestamp("atm_upd_datetime");
                result.addElement(atm);

                prv_que_id = cur_que_id;
            }
            // put the last one
            if (prv_que_id > 0) {
                attemptHash.put(new Long(prv_que_id), result);
            }

            stmt.close();
            return attemptHash;

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }

    }
    
    public static String getQueLst(Hashtable qHash) {
        if (qHash == null || qHash.size()==0) {
            return ("(0)");
        }
        
        String qLst = "(0";
        Enumeration qkeys = qHash.keys();
        while (qkeys.hasMoreElements()) {

            qLst += "," + qkeys.nextElement();
        }
        qLst += ")";
        return qLst;
    }
    
 
     
    /**
    * Get number of question of attempted in the module
    * @param database
    * @return long 
    */
   
 
     
     /**
     * Delete user all attempted records on the specified resource
     */
     public void del(Connection con)
        throws SQLException {
            
            String SQL = " DELETE FROM ProgressAttempt "
                       + " WHERE atm_pgr_usr_id = ? AND atm_pgr_res_id = ? AND atm_tkh_id = ? ";
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setString(1, this.atm_pgr_usr_id);
            stmt.setLong(2, this.atm_pgr_res_id);
            stmt.setLong(3, this.atm_tkh_id);
            stmt.executeUpdate();
            stmt.close();
            return;
        }
     
     public static void delByResId(Connection con, long intResId) throws SQLException {
    	String SQL = " DELETE FROM ProgressAttempt WHERE atm_int_res_id = ?";
		PreparedStatement stmt = con.prepareStatement(SQL);
		int index = 1;
		stmt.setLong(index++, intResId);
		stmt.executeUpdate();
		stmt.close();
		return;
     }
     
     public String getUserAttemptedResultAsXML(Connection con, long mod_id, long usr_ent_id, long tkh_id)
        throws SQLException, qdbErrMessage, qdbException{
            
            String usr_id = dbRegUser.usrEntId2UsrId(con,usr_ent_id);
            StringBuffer buf = null;
            long atm_num = dbProgress.getLastAttemptNbr(con, mod_id, tkh_id);
            if(atm_num == 0 )
                return "<result usr_ent_id=\"" + usr_ent_id + "\"/>";
            else{
                dbProgress dbpgr = new dbProgress();
                dbpgr.pgr_usr_id = usr_id;
                dbpgr.pgr_res_id = mod_id;
                dbpgr.pgr_tkh_id = tkh_id;
                dbpgr.get(con, atm_num);
                buf = new StringBuffer();
                Hashtable resultHash = getUserResponse(con, mod_id, usr_id, atm_num, tkh_id);
                buf.append("<result usr_ent_id=\"").append(usr_ent_id).append("\" completion_date=\"").append(cwUtils.escNull(dbpgr.pgr_complete_datetime)).append("\">");
                Enumeration enumeration = resultHash.keys();
                String response_bil = null;
                while(enumeration.hasMoreElements()){
                    Long id = (Long)enumeration.nextElement();
                    buf.append("<response ")
                       .append(" id=\"").append(id).append("\">");
                    response_bil = (String)resultHash.get(id);
                    if( response_bil != null ) {
	                    buf.append(dbUtils.esc4XML(response_bil));
                    }
                    buf.append("</response>");
                }
                buf.append("</result>");
            }
            return buf.toString();
        }
        
        
    private Hashtable getUserResponse(Connection con, long mod_id, String usr_id, long atm_num, long tkh_id)
        throws SQLException{
            
            Hashtable resultHash = new Hashtable();
            String SQL = " Select atm_int_res_id, atm_response_bil "
                       + " from ProgressAttempt "
                       + " where atm_pgr_res_id = ? "
                       + " and atm_pgr_usr_id = ? "
                       + " and atm_pgr_attempt_nbr = ? "
                       + " and atm_tkh_id = ? ";
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, mod_id);
            stmt.setString(2, usr_id);
            stmt.setLong(3, atm_num);
            stmt.setLong(4, tkh_id);
            ResultSet rs = stmt.executeQuery();
            String response_bil = null;
            while(rs.next()){
            	response_bil = rs.getString("atm_response_bil");
            	if( response_bil != null ) {
					resultHash.put(new Long(rs.getLong("atm_int_res_id")), response_bil);
            	}
            }
            stmt.close();
            return resultHash;
        }
        
     public void updateScore(Connection con) throws qdbException {
        String SQL = " UPDATE progressAttempt SET atm_score = ?, "
                   + " atm_correct_ind = ? "
                   + " WHERE atm_pgr_usr_id = ? "
                   + " AND atm_pgr_res_id = ? "
                   + " AND atm_pgr_attempt_nbr = ? "
                   + " AND atm_tkh_id = ? "
                   + " AND atm_int_res_id = ? ";
        try {
            PreparedStatement stmt = con.prepareStatement(SQL);
            int index = 1;
            stmt.setLong(index++, atm_score);
            stmt.setBoolean(index++, atm_correct_ind);
            stmt.setString(index++, atm_pgr_usr_id);
            stmt.setLong(index++, atm_pgr_res_id);
            stmt.setLong(index++, atm_pgr_attempt_nbr);
            stmt.setLong(index++, atm_tkh_id);
            stmt.setLong(index++, atm_int_res_id);
            if(stmt.executeUpdate()!=1) {
                con.rollback();
                throw new qdbException("Failed to updateScore in ProgressAttempt");
            }
            stmt.close();
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
     }
     
     public static PreparedStatement getInsStmt(Connection con)throws qdbException{
         try {
         PreparedStatement stmt = con.prepareStatement(
	         "INSERT INTO ProgressAttempt "
	         + " ( atm_pgr_usr_id "
	         + "  ,atm_pgr_res_id "
	         + "  ,atm_tkh_id "
	         + "  ,atm_pgr_attempt_nbr "
	         + "  ,atm_int_res_id "
	         + "  ,atm_int_order "
	         + "  ,atm_response_bil "
	         + "  ,atm_response_bil_ext "
	         + "  ,atm_correct_ind "
	         + "  ,atm_score "
	         + "  ,atm_max_score "
	         + "  ,atm_flag_ind "
	         + "  ,atm_upd_datetime "
	         + "  ,atm_order ) "
	         + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,? ) " );  
         return stmt;
         } catch(SQLException e) {
             throw new qdbException("SQL Error: " + e.getMessage());
        }
     }
     
     public void insBatch(Connection con, PreparedStatement stmt, Timestamp curTime) throws qdbException {
         try {

             int index = 1;
             stmt.setString(index++, atm_pgr_usr_id);
             stmt.setLong(index++, atm_pgr_res_id);
             stmt.setLong(index++, atm_tkh_id);
             stmt.setLong(index++, atm_pgr_attempt_nbr);
             stmt.setLong(index++, atm_int_res_id);
             stmt.setLong(index++, atm_int_order);
             stmt.setString(index++, atm_response_bil);
             stmt.setString(index++, atm_response_bil_ext);
             stmt.setBoolean(index++, atm_correct_ind);
             stmt.setLong(index++, atm_score);
             stmt.setLong(index++, atm_max_score);
             stmt.setBoolean(index++, atm_flag_ind);
             stmt.setTimestamp(index++,curTime);
             stmt.setLong(index++,atm_order);
             stmt.addBatch();
         } catch (SQLException e) {
             throw new qdbException("SQL Error: " + e.getMessage());
         }

     }
	public boolean chkforSubmit2(Connection con) throws qdbException {
		boolean isSubmit = false;
		try {
			PreparedStatement stmt = null;
			ResultSet rs = null;
			String sql = "select count(*) from ProgressAttempt where atm_pgr_usr_id = ? and atm_tkh_id = ?";
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setString(index++, atm_pgr_usr_id);
			stmt.setLong(index++, atm_tkh_id);
			rs = stmt.executeQuery();
			while(rs.next()) {
				if(rs.getLong(1) > 0) {
					isSubmit = true;
				}
			}
		} catch (SQLException e) {
			throw new qdbException("SQL Error: " + e.getMessage());
		}
		
		return isSubmit;
	}
}