package com.cw.wizbank.db;

import java.sql.*;
import java.util.Vector;
import java.util.Hashtable;

import com.cw.wizbank.util.*;
import com.cw.wizbank.db.sql.*;
import com.cw.wizbank.db.view.ViewTrainingCenter;
import com.cw.wizbank.qdb.dbEntity;
import com.cw.wizbank.qdb.dbEntityRelation;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbAction;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cwn.wizbank.utils.CommonLog;


public class DbUserGrade extends dbEntity {
    
    public static final String UGR_TYPE_MANAGER = "MANAGER";
    public static final String UGR_TYPE_ROOT = "ROOT";

	public static final String GRADE_CODE_ALREADY_EXISTS = "UGR001";
	
    public long ugr_ent_id;
    public String ugr_display_bil;
    public long ugr_ent_id_root;
    public String ugr_type;                  
    public boolean ugr_default_ind;
	public long ugr_seq_no;
	public long ugr_tcr_id;
	public String ugr_tcr_title;
	public String ugr_code;
	
	public Vector vChild; // Vector of DbUserGrade

    public DbUserGrade(){
        ent_type = "UGR";
        vChild = new Vector();
    }
    
    public void ins(Connection con) throws qdbException, qdbErrMessage{
        try{
            super.ins(con);

            // if ok.
            ugr_ent_id = ent_id;
            
            if(ugr_tcr_id < 1){
                ugr_tcr_id =ViewTrainingCenter.getRootTcId( con);
            }

            PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_ins_usergrade);
            int index = 1;
            if(ugr_code==null){

            	ugr_code = "P"+cwUtils.putAssignZero(ent_id, 4);
            }
            
            
            stmt.setLong(index++, ugr_ent_id);
            stmt.setString(index++, ugr_display_bil);
            stmt.setLong(index++, ugr_ent_id_root);
            stmt.setString(index++, ugr_type);
			stmt.setInt(index++, this.getMaxSeqNo(con)+1);
            stmt.setBoolean(index++, ugr_default_ind);
            stmt.setLong(index++, ugr_tcr_id);
            stmt.setString(index++, ugr_code);
            if (stmt.executeUpdate()!= 1) {
            	stmt.close();            	
                con.rollback();
                throw new SQLException("Failed to insert usergrade.");
            }
            stmt.close();
            ent_ste_uid=ugr_code;
            super.upd3(con);
            return;
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }
    }
    
    public void updDesc(Connection con, String usr_id)
    	throws qdbException, qdbErrMessage, cwException {
        try {
            ent_id = ugr_ent_id;
			super.checkTimeStamp(con);
            super.upd2(con);
          

            PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_upd_desc_usergrade);
            stmt.setString(1, ugr_display_bil);
            stmt.setString(2,ugr_code);
            stmt.setLong(3, ugr_ent_id);
            if(stmt.executeUpdate() != 1 ) {
                // update fails, rollback
            	stmt.close();
                con.rollback();
                throw new qdbException("Fails to update grade record.");
			}
            stmt.close();
            con.commit();
	        EntityFullPath entityfullpath = EntityFullPath.getInstance(con);
	        entityfullpath.updateChildFullPath(con, ugr_ent_id, ugr_display_bil, dbEntityRelation.ERN_TYPE_UGR_PARENT_UGR);
            return;
        }catch(SQLException e){
            throw new cwException(e.getMessage());     
        }
    }
    
    // get EntId by ste ugr id and the organisation
    public boolean getBySteUid(Connection con) throws SQLException{
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_usergrade_by_ste_uid);
        stmt.setString(1, ent_ste_uid);
        stmt.setLong(2, ugr_ent_id_root);
                                
        ResultSet rs = stmt.executeQuery();
        if (rs.next()){
            ugr_ent_id = rs.getLong("ugr_ent_id");
            ugr_display_bil = rs.getString("ugr_display_bil");
            ugr_ent_id_root = rs.getLong("ugr_ent_id_root");
            ugr_type = rs.getString("ugr_type");            
        }else{
        	stmt.close();
            return false;
        }
        stmt.close();
        return true;
    }

    
    public Vector search(Connection con, String s_display_bil) throws SQLException{
        Vector vtGrade = new Vector();
        ugr_display_bil = "%" + s_display_bil + "%";

        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_usergrade_by_display);
		stmt.setString(1, ugr_display_bil.toLowerCase());
        stmt.setLong(2, ugr_ent_id_root);
                                
        ResultSet rs = stmt.executeQuery();
        while (rs.next()){
            DbUserGrade grade = new DbUserGrade();
            grade.ugr_ent_id = rs.getLong("ugr_ent_id");
            grade.ugr_display_bil = rs.getString("ugr_display_bil");
            grade.ugr_ent_id_root = rs.getLong("ugr_ent_id_root");
            grade.ugr_type = rs.getString("ugr_type");
            grade.ugr_default_ind = rs.getBoolean("ugr_default_ind");
            vtGrade.addElement(grade); 
        }
        stmt.close();
        return vtGrade;
    }
    
    public static long getGradeRoot(Connection con, long siteId) throws SQLException{
        long gradeRoot = 0;
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_grade_root);
        stmt.setLong(1, siteId);
                                    
        ResultSet rs = stmt.executeQuery();
        if (rs.next()){
            gradeRoot = rs.getLong("ugr_ent_id");
        }else{
            throw new SQLException("no grade root!!");    
        }
        stmt.close();
        return gradeRoot;
    }
    
	public static Vector getusrGradeEntIds(Connection con, long ent_id) throws SQLException {
		Vector vec = new Vector();
		PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_my_grade);
		stmt.setLong(1, ent_id);
		stmt.setString(2, dbEntityRelation.ERN_TYPE_USR_CURRENT_UGR);
        stmt.setBoolean(3, true);
		ResultSet rs = stmt.executeQuery();
        
		if (rs.next()) { 
			vec.addElement(new Long(rs.getLong("ern_ancestor_ent_id")));
		}
        
		stmt.close();
		return vec;
	}    
    
    public static long getGradeEntId(Connection con, long ent_id) throws SQLException {
        long grade_ent_id = 0;
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_my_grade);
        stmt.setLong(1, ent_id);
        stmt.setString(2, dbEntityRelation.ERN_TYPE_USR_CURRENT_UGR);
        stmt.setBoolean(3, true);
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            grade_ent_id = rs.getLong("ern_ancestor_ent_id");   
        }/* else {
            throw new SQLException("SQL Error: Grade has not been assigned to " + ent_id);
        }*/
        rs.close();
        stmt.close();
        return grade_ent_id;
    }
    
    public static void getPeers(Connection con, long ent_id, Vector order_grade_lst, Hashtable peers) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_peer_ent_id);
        stmt.setLong(1, ent_id);
        stmt.setBoolean(2, true);
        stmt.setBoolean(2, true);
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next()) {
            order_grade_lst.addElement(new Long(rs.getLong("ern_child_ent_id")));
            peers.put(new Long(rs.getLong("ern_child_ent_id")), rs.getString("ugr_display_bil"));
        }
        rs.close();
        stmt.close();
    }

    public static Hashtable getDisplayName(Connection con, String ent_id_lst) throws SQLException {
        Hashtable hash = new Hashtable();
        PreparedStatement stmt = con.prepareStatement("SELECT ugr_ent_id, ugr_display_bil FROM UserGrade WHERE ugr_ent_id IN " + ent_id_lst);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            hash.put(rs.getString("ugr_ent_id"), rs.getString("ugr_display_bil"));   
        }
        rs.close();
        stmt.close();
        
        return hash;
    }
    
    public static boolean checkExist(Connection con, long ent_id) throws SQLException{
        boolean bExist = false;
        PreparedStatement stmt = con.prepareStatement("SELECT ugr_ent_id FROM UserGrade WHERE ugr_ent_id = ? ");
        stmt.setLong(1, ent_id);
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            bExist = true;
        }else{
            bExist = false;    
        }
        return bExist;
    }
    
    public static DbUserGrade getDefaultGrade(Connection con, long siteId) throws SQLException {
        DbUserGrade grade = null;
        PreparedStatement stmt = null;
        try{
            stmt = con.prepareStatement(SqlStatements.SQL_GET_DEFAULT_UGR);
            stmt.setBoolean(1, true);
            stmt.setLong(2, siteId);
            
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                grade = new DbUserGrade();
                grade.ugr_ent_id = rs.getLong("ugr_ent_id");
                grade.ugr_display_bil = rs.getString("ugr_display_bil");
                grade.ugr_ent_id_root = rs.getLong("ugr_ent_id_root");
                grade.ugr_type = rs.getString("ugr_type");
            }
        }finally{
            if (stmt!=null)     stmt.close();    
        }
        
        return grade;
    }
    
    public static StringBuffer getDefaultGradeAsXML(Connection con, long siteId) throws SQLException{
        StringBuffer result = new StringBuffer();
        DbUserGrade defaultUgr = getDefaultGrade(con, siteId);
        if(defaultUgr!=null) {
            result.append("<default_grade>");
            result.append("<attribute_list type=\"" + defaultUgr.ent_type + "\">");    
            result.append("<entity relation_type=\"" + dbEntityRelation.ERN_TYPE_USR_CURRENT_UGR + "\" id=\"" + defaultUgr.ugr_ent_id + "\" display_bil=\"" + defaultUgr.ugr_display_bil + "\"/>");
            result.append("</attribute_list>");    
            result.append("</default_grade>");
        }
        return result;
    }
    
	public static DbUserGrade getAllUserGrades(Connection con, boolean ugr_default_ind, loginProfile prof) throws SQLException {
		DbUserGrade root = new DbUserGrade();
		DbUserGrade ugr;
		Hashtable hs = new Hashtable();
		
		StringBuffer sql1 = new StringBuffer();
		sql1.append("SELECT ugr_ent_id, ugr_display_bil, ugr_type ")
			.append("FROM UserGrade ")
			.append(" where ugr_ent_id_root = ?")
			.append(" ORDER BY ugr_ent_id");
		PreparedStatement stmt1 = con.prepareStatement(sql1.toString());
		stmt1.setLong(1, prof.root_ent_id);
		ResultSet rs1 = stmt1.executeQuery(); 

		while (rs1.next()) {
			ugr = new DbUserGrade();
    	
			ugr.ugr_ent_id = rs1.getLong("ugr_ent_id");
			ugr.ugr_display_bil = rs1.getString("ugr_display_bil");
			ugr.ugr_type = rs1.getString("ugr_type");
    	
			if ( ugr.ugr_type!=null && ugr.ugr_type.equals(UGR_TYPE_ROOT) ) {
				root = ugr; // Store the reference to root node
			}
    	
			hs.put(new Long(ugr.ugr_ent_id), ugr);
		}
		stmt1.close();

		//
		//stmt1.setInt(1, ugr_default_ind);
    
		
		StringBuffer sql2 = new StringBuffer();
		sql2.append("SELECT ug1.ugr_type, ug1.ugr_ent_id, ug1.ugr_display_bil, ")
			.append("ern_child_ent_id, ug2.ugr_seq_no ")
			.append("from UserGrade ug1, EntityRelation, UserGrade ug2 WHERE ")
			.append("ug1.ugr_ent_id=ern_ancestor_ent_id AND ")
			.append("ug2.ugr_ent_id=ern_child_ent_id AND ")
			.append("ug1.ugr_default_ind=? AND ")
			.append("ug2.ugr_default_ind=? AND ")
			.append("ug1.ugr_ent_id_root = ? AND ")
			.append("ug2.ugr_ent_id_root = ? AND ")
			.append("ern_type=? AND ")
			.append("ern_parent_ind=? ");
		
		
//		String tcrListStr = "";
//		if(!qdbAction.wizbini.cfgSysSetupadv.isTcIndependent()
//		        || prof.current_role.equals("ADM_1")
//		        ||  ViewTrainingCenter.isSuperTA(con, prof.root_ent_id, prof.usr_ent_id, prof.current_role)
//		        ){
//		    
//		}else{
//
//                Vector canMgtTopTcrList = ViewTrainingCenter.getTopLevelTCList(con, prof);
//                if(canMgtTopTcrList != null && canMgtTopTcrList.size() > 0){
//                    tcrListStr = cwUtils.vector2list(canMgtTopTcrList);
//                }else{
//                    tcrListStr = "(0)";
//                }
                
                sql2.append(" AND (ug2.ugr_tcr_id in (").append("select tcn_child_tcr_id from tcRelation inner join tcTrainingCenter on (tcr_id = tcn_child_tcr_id) where tcn_ancestor = ? and tcr_status = ? ").append(") or ug2.ugr_tcr_id =?)");
//        }
        
		sql2.append(" order by ug2.ugr_seq_no, ug1.ugr_ent_id ");
		
		PreparedStatement stmt2 = con.prepareStatement(sql2.toString());
		int index=1;
		stmt2.setBoolean(index++, ugr_default_ind);
		stmt2.setBoolean(index++, ugr_default_ind);
		stmt2.setLong(index++, prof.root_ent_id);
		stmt2.setLong(index++, prof.root_ent_id);
		stmt2.setString(index++, dbEntityRelation.ERN_TYPE_UGR_PARENT_UGR);
		stmt2.setBoolean(index++, true);
		
		stmt2.setLong(index++, prof.my_top_tc_id);
		stmt2.setString(index++, DbTrainingCenter.STATUS_OK);
		stmt2.setLong(index++, prof.my_top_tc_id);
		ResultSet rs2 = stmt2.executeQuery(); 
    
		while (rs2.next()) {
			long parent_id = rs2.getLong("ugr_ent_id");
			long child_id = rs2.getLong("ern_child_ent_id");
			DbUserGrade parent = (DbUserGrade) hs.get(new Long(parent_id));
			DbUserGrade child = (DbUserGrade) hs.get(new Long(child_id));
			parent.addChild(child);
		}
		stmt2.close();
   
		return root;
	}    
 
	public void addChild(DbUserGrade ugr) {
		vChild.add(ugr);
	}
	
	/**
	  * ugr_ent_id should be set.
	  */
	public void get(Connection con) throws qdbException {
		StringBuffer sql = new StringBuffer();
		if(qdbAction.wizbini.cfgSysSetupadv.isTcIndependent()){
    		sql.append("SELECT ugr_ent_id, ugr_display_bil, ugr_type, ")
    			.append("ent_ste_uid, ent_upd_date, ugr_seq_no, ugr_tcr_id, tcr_title, ugr_code ")
    			.append("FROM UserGrade, Entity, tcTrainingCenter WHERE ")
    			.append("ugr_ent_id=ent_id AND tcr_id = ugr_tcr_id AND ugr_ent_id=?");
		}else{
		    sql.append("SELECT ugr_ent_id, ugr_display_bil, ugr_type, ")
            .append("ent_ste_uid, ent_upd_date, ugr_seq_no, ugr_tcr_id, '' as tcr_title, ugr_code ")
            .append("FROM UserGrade, Entity WHERE ")
            .append("ugr_ent_id=ent_id AND ugr_ent_id=?");
		}


		PreparedStatement stmt;
		try {
			stmt = con.prepareStatement(sql.toString());
			stmt.setLong(1, this.ugr_ent_id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				this.ugr_display_bil = rs.getString("ugr_display_bil");
				this.ugr_type = rs.getString("ugr_type");
				this.ent_ste_uid = rs.getString("ent_ste_uid");
				this.ent_upd_date = rs.getTimestamp("ent_upd_date");
				this.ugr_seq_no = rs.getLong("ugr_seq_no");
				this.ugr_tcr_id = rs.getLong("ugr_tcr_id");
				this.ugr_tcr_title = rs.getString("tcr_title");
				this.ugr_code = rs.getString("ugr_code");
			}
			stmt.close();
		} catch (SQLException e) {
			CommonLog.error(e.getMessage(),e);
		}
	}
	
	public void updSeqNo(Connection con) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append("update UserGrade set ugr_seq_no=? ")
			.append("where ugr_ent_id=?");
		PreparedStatement stmt = con.prepareStatement(sql.toString());
		stmt.setLong(1, this.ugr_seq_no);
		stmt.setLong(2, this.ugr_ent_id);
		if (stmt.executeUpdate() != 1) {
			con.rollback();
			throw new SQLException("Failed to update order.");
		}
		stmt.close();
	}
	
	public int getMaxSeqNo(Connection con) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT max(ug2.ugr_seq_no) ")
			.append("FROM UserGrade ug1, EntityRelation, UserGrade ug2 WHERE ")
			.append("ug1.ugr_ent_id=ern_ancestor_ent_id AND ")
			.append("ug2.ugr_ent_id=ern_child_ent_id AND ")
			.append("ern_type=? AND ern_parent_ind = ?");
		
		PreparedStatement stmt = con.prepareStatement(sql.toString());
		stmt.setString(1, dbEntityRelation.ERN_TYPE_UGR_PARENT_UGR);
		stmt.setBoolean(2, true);
		ResultSet rs = stmt.executeQuery();
		int seqNo = 0; 
		if (rs.next()) {
			seqNo = rs.getInt(1);
		}
		stmt.close();
		return seqNo;
	}
	
	public boolean exists(Connection con) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT count(*) FROM Entity, UserGrade, EntityRelation")
			.append(" WHERE ent_id=ugr_ent_id AND ern_child_ent_id=ent_id")
			.append(" AND ern_type=?")
			.append(" AND ern_parent_ind=?")
			.append(" AND ugr_ent_id_root=? ")	
			.append(" AND ent_ste_uid=?")
			.append(" or ugr_code=? ");
		PreparedStatement stmt = con.prepareStatement(sql.toString());
		int index=1;
		stmt.setString(index++, dbEntityRelation.ERN_TYPE_UGR_PARENT_UGR);
		stmt.setBoolean(index++, true);
		stmt.setLong(index++, this.ugr_ent_id_root);
		stmt.setString(index++, this.ent_ste_uid);
		stmt.setString(index++, this.ugr_code);
		ResultSet rs = stmt.executeQuery();
		int count=0;
		if (rs.next()) {
			count = rs.getInt(1);
		}
		stmt.close();
		if (count>0) {
			return true;
		}
		return false;
	}
	
	public boolean exists2(Connection con) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT count(*) FROM Entity, UserGrade, EntityRelation")
			.append(" WHERE ent_id=ugr_ent_id AND ern_child_ent_id=ent_id")
			.append(" AND ern_type=?")
			.append(" AND ern_parent_ind=?")
			.append(" AND ugr_ent_id_root=? ")	
			.append(" AND ent_ste_uid=?")
			.append(" AND ugr_ent_id!=?");
		PreparedStatement stmt = con.prepareStatement(sql.toString());
		int index=1;
		stmt.setString(index++, dbEntityRelation.ERN_TYPE_UGR_PARENT_UGR);
		stmt.setBoolean(index++, true);
		stmt.setLong(index++, this.ugr_ent_id_root);
		stmt.setString(index++, this.ent_ste_uid);
		stmt.setLong(index++, this.ugr_ent_id);
		ResultSet rs = stmt.executeQuery();
		int count=0;
		if (rs.next()) {
			count = rs.getInt(1);
		}
		stmt.close();
		if (count>0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 判断是否有职务维护的权限（是否是二级培训中心管理员）
	 * @param con
	 * @param prof
	 * @return
	 * @throws SQLException
	 */
	public static boolean hasPrivilege(Connection con, loginProfile prof) throws SQLException {
		boolean havePrivilege = false;
		boolean isSuperTA = ViewTrainingCenter.isSuperTA(con, prof.root_ent_id, prof.usr_ent_id, prof.current_role);
		if(prof.current_role.equals("ADM_1") || isSuperTA){
			return true;
		}
		Vector<Long> canMgtTopList = ViewTrainingCenter.getTopLevelTCList(con, prof);
		if(canMgtTopList != null && canMgtTopList.size() > 0){
			for (int i = 0; i < canMgtTopList.size(); i++) {
				if(DbTrainingCenter.getParentTcId(con, canMgtTopList.get(i)) == 1){
					havePrivilege = true;
					break;
				}
			}
		}
		return havePrivilege;
	}
	
	 public void upd(Connection con, loginProfile prof)
	            throws qdbException, qdbErrMessage {
	        try {
	            if(checkUIdDuplicate(con, prof.root_ent_id)){
	                throw new qdbErrMessage("USG008", ent_ste_uid);
	            }

	            super.checkTimeStamp(con);

	            String sqlStr = null;
	            sqlStr = " update userGrade set ugr_display_bil = ? where ugr_ent_id = ? ";

	            PreparedStatement stmt = con.prepareStatement(sqlStr);

	            int i = 1;

	            stmt.setString(i++, ugr_display_bil);
	            stmt.setLong(i++, ugr_ent_id);

	            int stmtResult = stmt.executeUpdate();
	            stmt.close();

	            if(stmtResult!=1){
	                throw new qdbException("Fails to update user group record.");
	            }

	            super.upd(con);
	            super.updUid(con);
	            con.commit();
	        } catch(SQLException e) {
	            throw new qdbException("SQL Error: " + e.getMessage());
	        }

	        return;
	    }
	 
	 public boolean checkUIdDuplicate(Connection con, long site_id) throws qdbException{
	        try {
	            if (ent_ste_uid==null){
	                return false;
	            }
	            
	            String sql = " select count(*) from entity inner join userGrade on ugr_ent_id = ent_id"
	            			+ " where ent_ste_uid = ? and ugr_ent_id_root = ? and ugr_ent_id != ? "
	            			+ " and ent_delete_timestamp is null and ent_delete_timestamp is null ";
	            PreparedStatement stmt = con.prepareStatement(sql);

	            stmt.setString(1, ent_ste_uid);
	            stmt.setLong(2, site_id);
	            stmt.setLong(3, ugr_ent_id);

	            ResultSet rs = stmt.executeQuery();
	            boolean bExist = false;
	            if(rs.next()) {
	                if(rs.getInt(1) > 0){
	                	bExist = true;
	                }
	            }else {
	                stmt.close();
	                throw new qdbException("Failed to get grade info.");
	            }
	            stmt.close();
	            return bExist;
	        } catch (SQLException e) {
	            throw new qdbException("SQL Error: " + e.getMessage());
	        }
	    }
	 
		public void delEntityRelationAsChild(Connection con, String usr_id, Timestamp deleteTime) throws SQLException {
	        dbEntityRelation dbEr = new dbEntityRelation();
	        dbEr.ern_child_ent_id = ugr_ent_id;
	    	dbEr.ern_type = dbEntityRelation.ERN_TYPE_UGR_PARENT_UGR;
	    	dbEr.delAsChild(con, usr_id, deleteTime);
	    }
		
		public void insEntityRelation(Connection con, long ancestor_id, String usr_id) throws qdbException, SQLException{
	    	dbEntityRelation dbEr = new dbEntityRelation();
	    	dbEr.ern_ancestor_ent_id = ancestor_id;
	    	dbEr.ern_child_ent_id = ugr_ent_id;
	    	dbEr.ern_type = dbEntityRelation.ERN_TYPE_UGR_PARENT_UGR;
	    	dbEr.insEr(con, usr_id);
	    }
		 public void ins(Connection con, long parent_id, String usr_id)throws qdbException, qdbErrMessage, SQLException{
		        this.ins(con);
		        insEntityRelation(con, parent_id, usr_id);
		        con.commit();
		        EntityFullPath entityfullpath = EntityFullPath.getInstance(con);
		        entityfullpath.enclose(con, this.ugr_ent_id);
			}
			
		  
		    
}
