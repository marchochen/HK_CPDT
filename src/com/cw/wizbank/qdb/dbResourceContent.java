package com.cw.wizbank.qdb;

import java.sql.*;
import java.util.*;

import com.cw.wizbank.util.*;

public class dbResourceContent
{
    public long rcn_res_id;
    public long rcn_sub_nbr;
    public String rcn_desc;
    public long rcn_order;
    public long rcn_res_id_content;
    public long rcn_obj_id_content;
    public long rcn_rcn_res_id_parent;
    public long rcn_rcn_sub_nbr_parent;
    public long rcn_score_multiplier;
    public long rcn_tkh_id;
    
    public String rcn_temp_res_type;

    public dbResourceContent() {;}

    public boolean ins(Connection con)
        throws qdbException
    {
        try {
            // calculate the max sub_nbr and order
            String sql = null;
            if (cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType())) {
                sql = "SELECT MAX(rcn_sub_nbr),MAX(rcn_order) from ResourceContent with(nolock) "
                    + " where rcn_res_id = ? and rcn_tkh_id = ?";
            } else if (cwSQL.DBVENDOR_DB2.equalsIgnoreCase(cwSQL.getDbType())) {
                sql = "SELECT MAX(rcn_sub_nbr),MAX(rcn_order) from ResourceContent "
                    + " where rcn_res_id = ? and rcn_tkh_id = ? for read only";
            } else {
                sql = "SELECT MAX(rcn_sub_nbr),MAX(rcn_order) from ResourceContent "
                    + " where rcn_res_id = ? and rcn_tkh_id = ?";
            }
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setLong(1, rcn_res_id);
            if (rcn_tkh_id > 0) {
            	stmt.setLong(2, rcn_tkh_id);
            } else {
            	stmt.setLong(2, -1);
            }
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                rcn_sub_nbr = 1;
                rcn_order = 1;
            }else  {
                rcn_sub_nbr = rs.getInt(1) +1;
                rcn_order = rs.getInt(2)+1;
            }
            stmt.close();

            // check whether the question was added to the module
            String sql1 = null;
            if (cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType())) {
                sql1 = "SELECT rcn_res_id from ResourceContent  with(nolock) "
                    + " where rcn_res_id = ? and rcn_res_id_content = ? and rcn_tkh_id = ?";
            } else if (cwSQL.DBVENDOR_DB2.equalsIgnoreCase(cwSQL.getDbType())) {
                sql1 = "SELECT rcn_res_id from ResourceContent "
                    + " where rcn_res_id = ? and rcn_res_id_content = ? and rcn_tkh_id = ? for read only";
            } else {
                sql1 = "SELECT rcn_res_id from ResourceContent "
                    + " where rcn_res_id = ? and rcn_res_id_content = ? and rcn_tkh_id = ?";
            }
            PreparedStatement stmt1 = con.prepareStatement(sql1);

            stmt1.setLong(1, rcn_res_id);
            stmt1.setLong(2, rcn_res_id_content);
            if (rcn_tkh_id > 0) {
            	stmt1.setLong(3, rcn_tkh_id);
            } else {
            	stmt1.setLong(3, -1);
            }
            ResultSet rs1 = stmt1.executeQuery();

            if(rs1.next())
            {
                stmt1.close();
                return false;
            }
            stmt1.close();

            PreparedStatement stmt2 = con.prepareStatement(
                " INSERT INTO ResourceContent "
                + " ( rcn_res_id "
                + " , rcn_sub_nbr "
                + " , rcn_desc "
                + " , rcn_order "
                + " , rcn_res_id_content "
                + " , rcn_obj_id_content "
                + " , rcn_score_multiplier "
                + " , rcn_rcn_res_id_parent "
                + " , rcn_rcn_sub_nbr_parent " 
                + " , rcn_tkh_id) "
                + " VALUES (?,?,NULL,?,?,?,?,?,?,?)" );

            rcn_score_multiplier = 1;
            stmt2.setLong(1, rcn_res_id);
            stmt2.setLong(2, rcn_sub_nbr);
            stmt2.setLong(3, rcn_order);
            stmt2.setLong(4, rcn_res_id_content);
            if (rcn_obj_id_content == 0)
                stmt2.setNull(5, java.sql.Types.INTEGER);
            else
                stmt2.setLong(5, rcn_obj_id_content);
            stmt2.setLong(6, rcn_score_multiplier);
            if (rcn_rcn_res_id_parent > 0) {
                stmt2.setLong(7, rcn_rcn_res_id_parent);
            } else {
                stmt2.setNull(7, java.sql.Types.INTEGER);
            }
            if (rcn_rcn_sub_nbr_parent > 0) {
                stmt2.setLong(8, rcn_rcn_sub_nbr_parent);
            } else {
                stmt2.setNull(8, java.sql.Types.INTEGER);
            }
            if (rcn_tkh_id != 0) {
            	stmt2.setLong(9, rcn_tkh_id);
            } else {
            	stmt2.setLong(9, -1);
            }
            int stmtResult=stmt2.executeUpdate();
            stmt2.close();
            if ( stmtResult!=1)                            
            {
                con.rollback();
                throw new qdbException("Failed to add a question to the module.");
            }            
            return true;

        } catch(SQLException e) {
                throw new qdbException("SQL Error: " + e.getMessage());
        }

    }

    public boolean del(Connection con)
        throws qdbException
    {
        try {
            PreparedStatement stmt = con.prepareStatement(
            "   DELETE From ResourceContent "
            + "         where rcn_res_id = ? and rcn_res_id_content = ?" );

            stmt.setLong(1, rcn_res_id);
            stmt.setLong(2, rcn_res_id_content);
            int stmtResult=stmt.executeUpdate();
            stmt.close();
            if ( stmtResult!=1)                            
                return false;
            else
                return true;

        } catch(SQLException e) {
                throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    public void get(Connection con)
        throws qdbException
    {
        try {
          PreparedStatement stmt = con.prepareStatement(
            " SELECT rcn_res_id, rcn_sub_nbr, rcn_desc, "
          + " rcn_order, rcn_res_id_content, rcn_rcn_res_id_parent, "
          + " rcn_rcn_sub_nbr_parent, rcn_score_multiplier, rcn_obj_id_content "
          + "  FROM ResourceContent "
          + " WHERE rcn_res_id = ?  and rcn_res_id_content = ?" );

          stmt.setLong(1,rcn_res_id);
          stmt.setLong(2,rcn_res_id_content);
          ResultSet rs = stmt.executeQuery();
          if(rs.next())
          {
             rcn_res_id = rs.getLong("rcn_res_id");
             rcn_sub_nbr = rs.getLong("rcn_sub_nbr");
             rcn_desc = rs.getString("rcn_desc");
             rcn_order = rs.getLong("rcn_order");
             rcn_res_id_content = rs.getLong("rcn_res_id_content");
             rcn_obj_id_content = rs.getLong("rcn_obj_id_content");
             rcn_rcn_res_id_parent = rs.getLong("rcn_rcn_res_id_parent");
             rcn_rcn_sub_nbr_parent = rs.getLong("rcn_rcn_sub_nbr_parent");
             rcn_score_multiplier = rs.getLong("rcn_score_multiplier");

          }

          stmt.close();
        } catch(SQLException e) {
                throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    public static Vector getChildAss(Connection con, long resId)
        throws qdbException
    {
        try {
          PreparedStatement stmt = con.prepareStatement(
            " SELECT rcn_res_id, rcn_sub_nbr, rcn_desc, "
          + " rcn_order, rcn_res_id_content, rcn_rcn_res_id_parent, "
          + " rcn_rcn_sub_nbr_parent, rcn_score_multiplier, rcn_obj_id_content "
          + "  FROM ResourceContent "
          + " WHERE rcn_res_id = ? and rcn_tkh_id = -1 order by rcn_order ASC" );

          stmt.setLong(1,resId);
          ResultSet rs = stmt.executeQuery();
          Vector result = new Vector();
          dbResourceContent rcn = null;
          while(rs.next())
          {
             rcn = new dbResourceContent();
             rcn.rcn_res_id = rs.getLong("rcn_res_id");
             rcn.rcn_sub_nbr = rs.getLong("rcn_sub_nbr");
             rcn.rcn_desc = rs.getString("rcn_desc");
             rcn.rcn_order = rs.getLong("rcn_order");
             rcn.rcn_res_id_content = rs.getLong("rcn_res_id_content");
             rcn.rcn_obj_id_content = rs.getLong("rcn_obj_id_content");
             rcn.rcn_rcn_res_id_parent = rs.getLong("rcn_rcn_res_id_parent");
             rcn.rcn_rcn_sub_nbr_parent = rs.getLong("rcn_rcn_sub_nbr_parent");
             rcn.rcn_score_multiplier = rs.getLong("rcn_score_multiplier");

             result.addElement(rcn);
          }

          stmt.close();
          return result;

        } catch(SQLException e) {
                throw new qdbException("SQL Error: " + e.getMessage());
        }

    }

    
    public static Vector getChildAss(Connection con, long resId,String usr_id, long pgr_attempt_nbr, long tkh_id)
            throws qdbException
        {
            try {
              PreparedStatement stmt = con.prepareStatement(
                " SELECT distinct(rcn_res_id), rcn_sub_nbr, rcn_desc, "
              + " rcn_order, rcn_res_id_content, rcn_rcn_res_id_parent, "
              + " rcn_rcn_sub_nbr_parent, rcn_score_multiplier, rcn_obj_id_content ,atm_order"
              + "  FROM ResourceContent "
              + "  inner join ProgressAttempt on (atm_int_res_id = rcn_res_id_content and atm_pgr_attempt_nbr = ? and atm_pgr_res_id = ? and atm_tkh_id = ? and atm_pgr_usr_id =? ) "
              + " WHERE rcn_res_id = ? and rcn_tkh_id = -1 order by atm_order ASC" );

              stmt.setLong(1,pgr_attempt_nbr);
              stmt.setLong(2,resId);
              stmt.setLong(3,tkh_id);
              stmt.setString(4,usr_id);
              stmt.setLong(5,resId);
              ResultSet rs = stmt.executeQuery();
              Vector result = new Vector();
              dbResourceContent rcn = null;
              while(rs.next())
              {
                 rcn = new dbResourceContent();
                 rcn.rcn_res_id = rs.getLong("rcn_res_id");
                 rcn.rcn_sub_nbr = rs.getLong("rcn_sub_nbr");
                 rcn.rcn_desc = rs.getString("rcn_desc");
                 rcn.rcn_order = rs.getLong("rcn_order");
                 rcn.rcn_res_id_content = rs.getLong("rcn_res_id_content");
                 rcn.rcn_obj_id_content = rs.getLong("rcn_obj_id_content");
                 rcn.rcn_rcn_res_id_parent = rs.getLong("rcn_rcn_res_id_parent");
                 rcn.rcn_rcn_sub_nbr_parent = rs.getLong("rcn_rcn_sub_nbr_parent");
                 rcn.rcn_score_multiplier = rs.getLong("rcn_score_multiplier");

                 result.addElement(rcn);
              }

              stmt.close();
              return result;

            } catch(SQLException e) {
                    throw new qdbException("SQL Error: " + e.getMessage());
            }
        }
    
    public static Vector getChildInfo(Connection con, long resId)
        throws qdbException
    {
        try {
          PreparedStatement stmt = con.prepareStatement(
            " select rcn_res_id_content, res_subtype " +
            " from ResourceContent,Resources " +            " where rcn_res_id_content = res_id" +            " and rcn_res_id = ? order by rcn_order ASC");

          stmt.setLong(1,resId);
          ResultSet rs = stmt.executeQuery();
          Vector result = new Vector();
          dbResourceContent rcn = null;
          while(rs.next())
          {
             rcn = new dbResourceContent();
             rcn.rcn_res_id_content = rs.getLong("rcn_res_id_content");
             rcn.rcn_temp_res_type = rs.getString("res_subtype");

             result.addElement(rcn);
          }

          stmt.close();
          return result;

        } catch(SQLException e) {
                throw new qdbException("SQL Error: " + e.getMessage());
        }

    }

    public static String getQueId(Connection con, long resId, long objId)
        throws qdbException
    {
        try {
          PreparedStatement stmt = con.prepareStatement(
            " SELECT rcn_res_id_content "
          + "  FROM ResourceContent "
          + " WHERE rcn_obj_id_content = ? "
          + "    and rcn_res_id = ? order by rcn_order ASC" );

          stmt.setLong(1,objId);
          stmt.setLong(2,resId);
          ResultSet rs = stmt.executeQuery();
          String result = "(0 ";
          int i=0;
          while(rs.next())
          {
             i++;
             result += ", ";
             result += rs.getLong("rcn_res_id_content");
          }

          if(i==0)
                result = "(0)";
          else
                result += ")";
          stmt.close();
          return result;

        } catch(SQLException e) {
                throw new qdbException("SQL Error: " + e.getMessage());
        }

    }
    
    public static Vector getDynQueId(Connection con, long resId, long tkhId)
    	throws qdbException, SQLException
	{
    	String sql = " SELECT rcn_res_id_content FROM ResourceContent WHERE rcn_res_id = ? " +
    			" and rcn_tkh_id = ? order by rcn_order ASC";
    	PreparedStatement stmt = null;
	    try {
	        stmt = con.prepareStatement(sql);
	        int index = 1;
	        stmt.setLong(index++, resId);
	        stmt.setLong(index++, tkhId);
	        ResultSet rs = stmt.executeQuery();
	        Vector queId = new Vector();
	        while(rs.next())
	        {  
	        	queId.add(new Long(rs.getLong("rcn_res_id_content")));
	        }
	        return queId;	
	   } catch(SQLException e) {
		   throw new qdbException("SQL Error: " + e.getMessage());
	   } finally {
		   if (stmt != null) {
			   stmt.close();
		   }
	   }	
	}

    public void updOrder(Connection con)
        throws qdbException
    {
        try {
            PreparedStatement stmt = con.prepareStatement(
            "   UPDATE ResourceContent SET rcn_order = ? "
            + "      where rcn_res_id = ? and rcn_res_id_content = ?" );


            stmt.setLong(1, rcn_order);
            stmt.setLong(2, rcn_res_id);
            stmt.setLong(3, rcn_res_id_content);
            int stmtResult=stmt.executeUpdate();
            stmt.close();
            if ( stmtResult!=1)                            
            {
                con.rollback();
                //throw new qdbException("Failed to rearrange the question.");
            }
        } catch(SQLException e) {
                throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    public int getScore(Connection con)
        throws qdbException
    {
        try {
            PreparedStatement stmt = con.prepareStatement(
            "   SELECT rcn_score_multiplier from  ResourceContent "
            + "      where rcn_res_id = ? and rcn_res_id_content = ?" );

            stmt.setLong(1, rcn_res_id);
            stmt.setLong(2, rcn_res_id_content);
            ResultSet rs = stmt.executeQuery();
            int result =0;
            if (rs.next()) {
                int score = dbQuestion.getScore(con,rcn_res_id_content) ;
                result = score * rs.getInt("rcn_score_multiplier");
            }else {
            	stmt.close();
                throw new qdbException("Cannot get the score of the question.");
            }

            stmt.close();
            return result;
        } catch(SQLException e) {
                throw new qdbException("SQL Error: " + e.getMessage());
        }

    }

    public void updMultiplier(Connection con)
        throws qdbException
    {
        try {
            PreparedStatement stmt = con.prepareStatement(
            "   UPDATE ResourceContent SET rcn_score_multiplier = ? "
            + "      where rcn_res_id = ? and rcn_res_id_content = ?" );

            stmt.setLong(1, rcn_score_multiplier);
            stmt.setLong(2, rcn_res_id);
            stmt.setLong(3, rcn_res_id_content);
            int stmtResult=stmt.executeUpdate();
            stmt.close();
            if ( stmtResult!=1)                            
            {
                con.rollback();
                throw new qdbException("Failed to change the multiplier of the question.");
            }
        } catch(SQLException e) {
                throw new qdbException("SQL Error: " + e.getMessage());
        }
    }


    /**
     * Remove the specified remove from the module
     * @param database connection
     * @param questions id
     */
    public void delRes(Connection con, String[] que_id_lst)
        throws SQLException {
        if(que_id_lst!=null) {
            int[] ids = new int[que_id_lst.length];
            for(int i=0;i<que_id_lst.length;i++){ 
                ids[i] = Integer.parseInt(que_id_lst[i].trim());
            }
            String SQL = " DELETE FROM ResourceContent "
                       + " WHERE rcn_res_id = ? "
                       + " AND rcn_res_id_content IN "
                       + cwUtils.array2list(ids);

            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, rcn_res_id);
            stmt.executeUpdate();
            stmt.close();
        }
            return;

        }



    /**
     * Get Resource info belong to the module
     * @param database
     * @return resultset
     */
    public dbResource[] getResInfo(Connection con)
        throws SQLException, cwException {
            Vector vTmp = new Vector();
            
            String SQL = " SELECT res_id, res_title, res_difficulty, res_subtype "
                       + " FROM Resources, ResourceContent "
                       + " WHERE res_id = rcn_res_id_content "
                       + " AND rcn_res_id = ? "
                       + " ORDER BY res_title ";

            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, rcn_res_id);
            ResultSet rs = stmt.executeQuery();
            dbResource  res = null;
            while (rs.next()){
                res = new dbResource();
                res.res_id = rs.getLong("res_id");
                res.res_title = rs.getString("res_title");
                res.res_difficulty = rs.getInt("res_difficulty");
                res.res_subtype = rs.getString("res_subtype");
                vTmp.addElement(res);
            }
            stmt.close();
            dbResource result[] = new dbResource[vTmp.size()];
            result = (dbResource[])vTmp.toArray(result);
            return result;              
        }



    /**
    * Get number of Questions belong to the resources
    * @return long 
    */
    public long getQnum(Connection con)
        throws SQLException {
            
            String SQL = " SELECT COUNT(*) AS TOTALQ "
                       + " FROM ResourceContent "
                       + " WHERE rcn_res_id = ? ";
                       
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, rcn_res_id);
            ResultSet rs = stmt.executeQuery();
            long totalQ = 0;
            if( rs.next() )
                totalQ = rs.getLong("TOTALQ");
            stmt.close();
            return totalQ;
        }



    public static Hashtable getModuleCount(Connection con, Vector id_list)
    throws SQLException {
	
    	String colName = "tmp_cos_res_id";
        String tableName = cwSQL.createSimpleTemptable(con, colName, cwSQL.COL_TYPE_LONG, 0);
        cwSQL.insertSimpleTempTable(con, tableName, id_list, cwSQL.COL_TYPE_LONG);
    
        String SQL = " Select rcn_res_id, Count(rcn_res_id) "
                   + " From ResourceContent, " + tableName
                   + " Where rcn_res_id = " + colName
                   + " Group By rcn_res_id ";
        
        PreparedStatement stmt = con.prepareStatement(SQL);
        ResultSet rs = stmt.executeQuery();
        Hashtable table = new Hashtable();
        while(rs.next()){
            table.put(new Long(rs.getLong("rcn_res_id")), new Long(rs.getLong(2)));
        }
        stmt.close();
        cwSQL.dropTempTable(con, tableName);
        return table;
    }

    private static final String SQL_GET_RCN_COUNT = " select count(*) as CNT from ResourceContent "
                                                    + " where rcn_res_id = ? ";
    /**
    Get the number of content recourses of the input resource
    @param con Connection to database
    @rcn_res_id resource id of the input resource
    @return number of content resources of the input resource
    */    
    static int getResourceContentCount(Connection con, long rcn_res_id) throws SQLException {
        PreparedStatement stmt = null;
        int count = 0;
        try {
            stmt = con.prepareStatement(SQL_GET_RCN_COUNT);
            stmt.setLong(1, rcn_res_id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                count = rs.getInt("CNT");
            }
        } finally {
            if(stmt!=null) {stmt.close();}
        }
        return count;
    }

    public static Hashtable getChildQueRoot(Connection con, long rcn_res_id) throws SQLException {

        String SQL =
            " SELECT res_id, res_res_id_root "
                + "From ResourceContent, Resources "
                + "Where rcn_res_id = ? "
                + "And res_id = rcn_res_id_content ";
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, rcn_res_id);
        ResultSet rs = stmt.executeQuery();
        Hashtable h_res_id = new Hashtable();
        while (rs.next()) {
            h_res_id.put(new Long(rs.getLong("res_res_id_root")), new Long(rs.getLong("res_id")));
        }
        stmt.close();
        return h_res_id;

    }
    public static Vector getCourseModule(Connection con, long cos_id, String status, Vector id_lst)
        throws SQLException {

        String SQL =
            " Select res_id, res_subtype, res_title "
                + "From ResourceContent, Resources "
                + "Where rcn_res_id = ? "
                + "And rcn_res_id_content = res_id ";
        if (status != null && status.length() > 0) {
            SQL += " And res_status = ? ";
        }
        if (id_lst != null && !id_lst.isEmpty()) {
            SQL += " And res_id In " + cwUtils.vector2list(id_lst);
        }
        SQL += " Order By res_title asc";
        PreparedStatement stmt = con.prepareStatement(SQL);
        int index = 1;
        stmt.setLong(index++, cos_id);
        if (status != null && status.length() > 0) {
            stmt.setString(index++, status);
        }
        ResultSet rs = stmt.executeQuery();
        dbResource dbRes;
        Vector v_res = new Vector();
        while (rs.next()) {
            dbRes = new dbResource();
            dbRes.res_id = rs.getLong("res_id");
            dbRes.res_subtype = rs.getString("res_subtype");
            dbRes.res_title = rs.getString("res_title");
            v_res.addElement(dbRes);
        }
        stmt.close();
        return v_res;
    }

    public static Vector getCourseModule(Connection con, long cos_res_id) throws SQLException, qdbException, cwSysMessage {
        String SQL = "select rcn_res_id_content from ResourceContent where rcn_res_id = ?";
        Vector vtModInFor = new Vector();
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(SQL);
            stmt.setLong(1, cos_res_id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                dbModule dbmod = new dbModule();
                dbmod.mod_res_id = rs.getLong(1);
                dbmod.get(con);
                dbmod.mod_in_eff_start_datetime = dbmod.mod_eff_start_datetime;
                dbmod.mod_in_eff_end_datetime = dbmod.mod_eff_end_datetime;
                vtModInFor.addElement(dbmod);
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return vtModInFor;
    }

    public void del_res_by_content_id(Connection con, String[] ids_str) throws SQLException {
        String SQL = "Delete ResourceContent where rcn_res_id_content in " + cwUtils.array2list(ids_str);
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(SQL);
            stmt.execute();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }

    }
    
    public static String[] getResIdByContentId (Connection con, long contentId) throws SQLException {
    	String sql = "select rcn_res_id , res_type from ResourceContent, Resources where rcn_res_id_content = ? and rcn_res_id = res_id";
    	String [] result = new String[2];
    	PreparedStatement stmt = null;
    	try {
    		stmt = con.prepareStatement(sql);
    		stmt.setLong(1, contentId);
    		ResultSet rs = stmt.executeQuery();
    		if (rs.next()) {
    			result[0] = rs.getString("rcn_res_id");
    			result[1] = rs.getString("res_type");
    		}
    	}finally {
    		if (stmt != null) {
    			stmt.close();
    		}
    	}
    	return result;
    }
    
    public static void DelForDxt (Connection con, long res_id, long tkh_id) throws SQLException {
    	PreparedStatement pstmt = null;
    	try {
    	    String sql = "select rcn_res_id_content, res_subtype ";
            if (cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType())) {
                sql += "from ResourceContent with(nolock), Resources with(nolock) ";
            } else {
                sql+= "from ResourceContent , Resources ";
            }
            sql += "where rcn_res_id = ? and rcn_tkh_id = ? and rcn_res_id_content = res_id";
            if (cwSQL.DBVENDOR_DB2.equalsIgnoreCase(cwSQL.getDbType())) {
                sql += " for read only";
            }
	    	pstmt = con.prepareStatement(sql);	
			pstmt.setLong(1, res_id);
			pstmt.setLong(2, tkh_id);
			ResultSet rs = pstmt.executeQuery();
			ArrayList lst = new ArrayList();
	        while(rs.next()){
	        	if (rs.getString("res_subtype").equals(dbResource.RES_SUBTYPE_DSC)) {
	        		lst.add(new Long(rs.getLong("rcn_res_id_content")));
	        	}
		    }
	        lst.add(new Long(res_id));
	        long[] res_id_lst = new long[lst.size()];
	        for (int i = 0;i < res_id_lst.length; i++) {
	            res_id_lst[i]  = ((Long)lst.get(i)).longValue();
	        }
	        delDxt(con, res_id_lst, tkh_id);
    	} finally {
    		if (pstmt != null) {
    			pstmt.close();
    		}
    	}
    }
    
    private static void delDxt(Connection con, long[] res_ids, long tkh_id) throws SQLException {
        if (res_ids ==null || res_ids.length == 0) {
            return;
        }
    	PreparedStatement stmt = null; 
    	try {
    		String sql = "delete from ResourceContent where rcn_res_id =? and rcn_tkh_id = ?";
			stmt = con.prepareStatement(sql);
			for (int i = 0; i < res_ids.length; i++) {
    			stmt.setLong(1, res_ids[i]);
    			stmt.setLong(2, tkh_id);
    			stmt.addBatch();
			}
			stmt.executeBatch();
    	} finally {
    		if (stmt != null) {
    			stmt.close();
    		}
    	}
    }
    
    public static Vector getChildAssForDyn(Connection con, long resId, long tkh_id)
    throws qdbException
{
    try {
      PreparedStatement stmt = con.prepareStatement(
        " SELECT rcn_res_id, rcn_sub_nbr, rcn_desc, "
      + " rcn_order, rcn_res_id_content, rcn_rcn_res_id_parent, "
      + " rcn_rcn_sub_nbr_parent, rcn_score_multiplier, rcn_obj_id_content, "
      +	" rcn_tkh_id"
      + "  FROM ResourceContent "
      + " WHERE rcn_res_id = ? and rcn_tkh_id = ? order by rcn_order ASC" );

      stmt.setLong(1,resId);
      stmt.setLong(2, tkh_id);
      ResultSet rs = stmt.executeQuery();
      Vector result = new Vector();
      dbResourceContent rcn = null;
      while(rs.next())
      {
         rcn = new dbResourceContent();
         rcn.rcn_res_id = rs.getLong("rcn_res_id");
         rcn.rcn_sub_nbr = rs.getLong("rcn_sub_nbr");
         rcn.rcn_desc = rs.getString("rcn_desc");
         rcn.rcn_order = rs.getLong("rcn_order");
         rcn.rcn_res_id_content = rs.getLong("rcn_res_id_content");
         rcn.rcn_obj_id_content = rs.getLong("rcn_obj_id_content");
         rcn.rcn_rcn_res_id_parent = rs.getLong("rcn_rcn_res_id_parent");
         rcn.rcn_rcn_sub_nbr_parent = rs.getLong("rcn_rcn_sub_nbr_parent");
         rcn.rcn_score_multiplier = rs.getLong("rcn_score_multiplier");
         rcn.rcn_tkh_id = rs.getLong("rcn_tkh_id");
         result.addElement(rcn);
      }

      stmt.close();
      return result;

    } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
    }

}

    private static final String sql_get_child_res = "select res_id,res_type,res_subtype,res_title,res_src_link,res_upd_date from Resources,ResourceContent where rcn_res_id = ? and res_id = rcn_res_id_content";
    /**
     * Get all child resources for given res_id.
     * @param con
     * @param rcn_res_id
     * @return
     * @throws SQLException
     */
    public static Vector getChildResources(Connection con, long rcn_res_id) throws SQLException {
    	Vector result = new Vector();
    	PreparedStatement pstmt = null;
    	ResultSet rs = null;
    	try {
    		pstmt = con.prepareStatement(sql_get_child_res);
    		pstmt.setLong(1, rcn_res_id);
    		rs = pstmt.executeQuery();
    		while (rs.next()) {
    			dbResource dbres = new dbResource();
    			dbres.res_id = rs.getLong("res_id");
    			dbres.res_type = rs.getString("res_type");
    			dbres.res_subtype = rs.getString("res_subtype");
    			dbres.res_title = rs.getString("res_title");
    			dbres.res_src_link = rs.getString("res_src_link");
    			dbres.res_upd_date = rs.getTimestamp("res_upd_date");
    			result.add(dbres);
    		}
    	} finally {
    		if (pstmt != null) pstmt.close();
    	}
    	return result;
    }
}