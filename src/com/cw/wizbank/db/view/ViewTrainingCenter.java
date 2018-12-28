/*
 * Created on 2004-9-24
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.cw.wizbank.db.view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.cw.wizbank.JsonMod.commonBean.TCBean;
import com.cw.wizbank.accesscontrol.AcTrainingCenter;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeItemAccess;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.db.DbTcRelation;
import com.cw.wizbank.db.DbTrainingCenter;
import com.cw.wizbank.db.sql.SqlStatements;
import com.cw.wizbank.instructor.InstructorManager;
import com.cw.wizbank.qdb.dbEntityRelation;
import com.cw.wizbank.qdb.dbModule;
import com.cw.wizbank.qdb.dbObjective;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.trainingcenter.TrainingCenterReqParam;
import com.cw.wizbank.util.EntityFullPath;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;

/**
 * @author donaldl
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ViewTrainingCenter {
	private static String getTcInOrg = "select tcr_id,tcr_code,tcr_title,tcr_ste_ent_id,tcr_status,tcr_create_timestamp ,tcr_create_usr_id,tcr_update_timestamp,tcr_update_usr_id from tcTrainingCenter where tcr_ste_ent_id = ? and tcr_status='OK'";

	private static String getAllTcInOrg = "select tcr_id,tcr_code,tcr_title,tcr_ste_ent_id,tcr_status,tcr_create_timestamp ,tcr_create_usr_id,tcr_update_timestamp,tcr_update_usr_id from tcTrainingCenter where tcr_ste_ent_id = ? order by tcr_code";
    private static String hasRelateItem = "select itm_id from aeItem where itm_tcr_id = ?";
    private static String itm_tc_officer="select tcr_id from aeItem,tcTrainingCenter,tcTrainingCenterOfficer "+
                                         "where (itm_tcr_id = tcr_id or itm_tcr_id in (select tcn_child_tcr_id from tcRelation where tcn_ancestor = tcr_id)) and itm_id = ? and tcr_ste_ent_id = ? and tcr_id = tco_tcr_id  and tco_usr_ent_id = ? and tco_rol_ext_id = ?";
    private static String itm_run_tc_officer="select tcr_id from aeItem,aeItemRelation,tcTrainingCenter,tcTrainingCenterOfficer "+
                                             " where itm_tcr_id = tcr_id and itm_id = ire_parent_itm_id and  ire_child_itm_id = ? and tcr_ste_ent_id = ? and tcr_id = tco_tcr_id  and tco_usr_ent_id = ? and tco_rol_ext_id = ?";
    private static String tc_relate_officer = "select distinct usr_ent_id,usr_display_bil,usr_ste_usr_id,usg_ent_id,ern_ancestor_ent_id "+
                                              " from tcTrainingCenter,tcTrainingCenterOfficer,EntityRelation,userGroup,reguser "+
                                              " where tcr_id = ? and tcr_ste_ent_id = ? and tcr_id = tco_tcr_id  and tco_usr_ent_id = usr_ent_id and usr_ent_id = ern_child_ent_id "+
                                              " and ern_ancestor_ent_id = usg_ent_id and ern_type = ? and tco_rol_ext_id = ? and ern_parent_ind = ? ";
    private static String already_officer = "select distinct tco_usr_ent_id from tcTrainingCenterOfficer where tco_rol_ext_id = ? and  tco_tcr_id <> ? and tco_usr_ent_id in ";
    private static String only_in_cur_tc = "select tco_usr_ent_id from tcTrainingCenterOfficer where tco_rol_ext_id = ? and tco_tcr_id = ? "+ 
                                           " and tco_usr_ent_id not in (select distinct tco_usr_ent_id from tcTrainingCenterOfficer where tco_rol_ext_id = ?  and tco_tcr_id <> ? )";
    private static String dup_tc_code = "select tcr_code from tcTrainingCenter where tcr_code = ? and tcr_ste_ent_id = ? and tcr_status = 'OK' ";
    
    private static String only_role_tc = "select tco_tcr_id,count(tco_usr_ent_id)officer_num,max(tcr_code)tcr_code,tcr_title from tcTrainingCenterOfficer,tcTrainingCenter "+
                                          " where tcr_id = tco_tcr_id and tco_rol_ext_id in (select rol_ext_id from acRole where rol_ste_uid = ?)" +                                          "group by tco_tcr_id,tcr_title having tco_tcr_id in "+
                                          " (select tco_tcr_id from tcTrainingCenterOfficer where tco_usr_ent_id = ? and tco_rol_ext_id in (select rol_ext_id from acRole where rol_ste_uid = ?)) order by tco_tcr_id";
    private static String del_officer_from_tc = " delete from tcTrainingCenterOfficer where tco_usr_ent_id = ? and tco_rol_ext_id = ? ";
    
    private static String GPM_TYPE = "USR_PARENT_USG";
	
    public static List getEffTrainingCenters(Connection con,long ste_id, WizbiniLoader wizbini) {
    	return getEffTrainingCenters(con, ste_id, 0, null,  wizbini);
    }
    
	public static String ta_fliter = " select distinct(child.tcr_id) from tcTrainingCenter ancestor " +
	   " inner join tcTrainingCenterOfficer on (tco_tcr_id = ancestor.tcr_id) " +
	   " left join tcRelation on (tcn_ancestor = ancestor.tcr_id) " +
	   " inner join tcTrainingCenter child on (child.tcr_id = tcn_child_tcr_id or child.tcr_id = ancestor.tcr_id) " +
	   " where tco_usr_ent_id =  ? " +
	   " and child.tcr_status = 'OK' " +
	   " and ancestor.tcr_status = 'OK' ";
	
	public static String role_fliter = " select distinct(child.tcr_id) from tcTrainingCenter ancestor " +
			   " inner join tcTrainingCenterOfficer on (tco_tcr_id = ancestor.tcr_id and tco_usr_ent_id = ? and tco_rol_ext_id = ?) " +
			   " left join tcRelation on (tcn_ancestor = ancestor.tcr_id) " +
			   " inner join tcTrainingCenter child on (child.tcr_id = tcn_child_tcr_id or child.tcr_id = ancestor.tcr_id) " +
			   " where child.tcr_status = 'OK' " +
			   " and ancestor.tcr_status = 'OK' ";

	public static String getLrnFliter( WizbiniLoader wizbini){
		StringBuffer lrn_filter = new StringBuffer();
		lrn_filter.append("select distinct(tcr_id) from tcTrainingCenter")
				  .append(" inner join tcTrainingCenterTargetEntity on (tce_tcr_id = tcr_id) ")
				  .append(" inner join EntityRelation on (ern_ancestor_ent_id = tce_ent_id) ")
				  .append(" where ern_child_ent_id = ? ") 
				  .append(" and ern_type = ? " )
				  .append(" and tcr_status = 'OK' ");
		
		if (wizbini != null && wizbini.cfgSysSetupadv.isTcIndependent()) {
		    lrn_filter.append(" and tcr_parent_tcr_id > 0 ");
		}
		return lrn_filter.toString();
	}
	public static List getEffTrainingCenters(Connection con,long ste_id, long usr_ent_id, String cur_role, WizbiniLoader wizbini){
		List list = new ArrayList();
		DbTrainingCenter obj = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try{
			String sql = getTcInOrg;
			AcTrainingCenter acTc = new AcTrainingCenter(con);
			String fliter_type = null;
			if (usr_ent_id > 0) {
				if (!AccessControlWZB.isLrnRole(cur_role)){
					sql += " and tcr_id in (" + getLrnFliter(wizbini) + ")";
					fliter_type = "lrn_filter";
			    }
				else if(!AccessControlWZB.isRoleTcInd(cur_role)) {
					//sysadmin,do nothing.
					fliter_type = "sys_fliter";
				} else {
					sql += " and tcr_id in (" + ta_fliter + ")";
					fliter_type = "ta_fliter";
				}  
			}
			sql += " order by tcr_code";
			
			pst = con.prepareStatement(sql);
			int index = 1;
			pst.setLong(index++, ste_id);
			if (fliter_type != null && fliter_type.equals("ta_fliter")) {
				pst.setLong(index++, usr_ent_id);
			} else if (fliter_type != null && fliter_type.equals("lrn_filter")) {
				pst.setLong(index++, usr_ent_id);
				pst.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
		    }
			rs = pst.executeQuery();
			while(rs.next()){
				obj = new DbTrainingCenter(rs.getLong("tcr_id"));
				obj.setTcr_code(rs.getString("tcr_code"));
				obj.setTcr_create_timestamp(rs.getTimestamp("tcr_create_timestamp"));
				obj.setTcr_create_usr_id(rs.getString("tcr_create_usr_id"));
				obj.setTcr_status(rs.getString("tcr_status"));
				obj.setTcr_ste_ent_id(rs.getLong("tcr_ste_ent_id"));
				obj.setTcr_title(rs.getString("tcr_title"));
				obj.setTcr_update_timestamp(rs.getTimestamp("tcr_update_timestamp"));
				obj.setTcr_update_usr_id(rs.getString("tcr_update_usr_id"));
				
				list.add(obj);
			}
		}catch(SQLException e){
			throw new RuntimeException("System error: "+e.getMessage());
		}finally{
			cwSQL.closeResultSet(rs);
			cwSQL.closePreparedStatement(pst);
		}
		return list;
	}
	
	public static List getAllTcLst(Connection con,long ste_id){
		List list = new ArrayList();
		DbTrainingCenter obj = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try{
			pst = con.prepareStatement(getAllTcInOrg);
			pst.setLong(1,ste_id);
			rs = pst.executeQuery();
			while(rs.next()){
				obj = new DbTrainingCenter(rs.getLong("tcr_id"));
				obj.setTcr_code(rs.getString("tcr_code"));
				obj.setTcr_create_timestamp(rs.getTimestamp("tcr_create_timestamp"));
				obj.setTcr_create_usr_id(rs.getString("tcr_create_usr_id"));
				obj.setTcr_status(rs.getString("tcr_status"));
				obj.setTcr_ste_ent_id(rs.getLong("tcr_ste_ent_id"));
				obj.setTcr_title(rs.getString("tcr_title"));
				obj.setTcr_update_timestamp(rs.getTimestamp("tcr_update_timestamp"));
				obj.setTcr_update_usr_id(rs.getString("tcr_update_usr_id"));
		
				list.add(obj);
			}
		}catch(SQLException e){
			throw new RuntimeException("System error: "+e.getMessage());
		}finally{
			cwSQL.closeResultSet(rs);
			cwSQL.closePreparedStatement(pst);
		}
		return list;
	}
	
	public static boolean hasRelateItem(Connection con,long tcr_id){
		PreparedStatement pst = null;
		ResultSet rs = null;
		boolean flag = false;
		try{
			pst = con.prepareStatement(hasRelateItem);
			pst.setLong(1,tcr_id);
			rs = pst.executeQuery();
			if(rs.next()){
				flag = true;
			}
		}catch(SQLException e){
			throw new RuntimeException("Server error: "+e.getMessage());
		}finally{
			cwSQL.closeResultSet(rs);
			cwSQL.closePreparedStatement(pst);
		}
		return flag;
	}
	
	public static boolean isItemOfficer(Connection con,aeItem itm,long ste_id,long ent_id,String rol_id,boolean r_flag){
		PreparedStatement pst = null;
		ResultSet rs = null;
		boolean flag = false;
		boolean hasNext = false;
		try{
			if(r_flag){
				pst = con.prepareStatement(itm_run_tc_officer);
			}else{
				pst = con.prepareStatement(itm_tc_officer);
			}
			pst.setLong(1,itm.itm_id);
			pst.setLong(2,ste_id);
			pst.setLong(3,ent_id);
			pst.setString(4,rol_id);
			rs = pst.executeQuery();
			if(rs.next()){
				return true;
			}
			if(itm.itm_run_ind && !r_flag){
				flag = isItemOfficer(con,itm,ste_id,ent_id,rol_id,itm.itm_run_ind);
			}
		}catch(SQLException e){
			throw new RuntimeException("Server error: "+e.getMessage());
		}finally{
			cwSQL.closeResultSet(rs);
			cwSQL.closePreparedStatement(pst);
		}
		return flag;
	}
	
	private static class DataObj{
		public long usr_ent_id;
		public String usr_display_bil;
		public String usr_ste_usr_id;
		public String gpm_full_path;
	}
	
	public static List getRelateOfficerInfo(Connection con,TrainingCenterReqParam urlp,long ste_id){
		List list = new ArrayList();
		PreparedStatement pst = null;
		ResultSet rs = null;
		DataObj obj = null;
		int count = 1;
		String tmpSql = null;
		try{
			tmpSql = tc_relate_officer + " order by " +urlp.cwPage.sortCol + "  "+ urlp.cwPage.sortOrder;
			pst =con.prepareStatement(tmpSql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			pst.setLong(1,urlp.getObj().getTcr_id());
			pst.setLong(2,ste_id);
			pst.setString(3,GPM_TYPE);
			pst.setString(4,urlp.getRol());
			pst.setBoolean(5, true);
			rs = pst.executeQuery();
			EntityFullPath entityfullpath = EntityFullPath.getInstance(con);
			while(rs.next() && count <= urlp.cwPage.curPage * urlp.cwPage.pageSize){
				if ((count > (urlp.cwPage.curPage - 1) * urlp.cwPage.pageSize) && (count <= urlp.cwPage.curPage * urlp.cwPage.pageSize)) {
					long ancestor_id = rs.getLong("ern_ancestor_ent_id");
					obj = new DataObj();
					obj.usr_ent_id = rs.getLong("usr_ent_id");
					obj.usr_display_bil = rs.getString("usr_display_bil");
					obj.usr_ste_usr_id = rs.getString("usr_ste_usr_id");
					obj.gpm_full_path = entityfullpath.getFullPath(con, ancestor_id);
				
				  list.add(obj);
				}
				count++;
			}
			rs.last();
			urlp.cwPage.totalRec = rs.getRow();
			pst.close();
			urlp.cwPage.totalPage =(int) Math.ceil((float) urlp.cwPage.totalRec / (float) urlp.cwPage.pageSize);
		}catch(SQLException e){
			throw new RuntimeException("Server error: "+e.getMessage());
		}finally{
			cwSQL.closeResultSet(rs);
			cwSQL.closePreparedStatement(pst);
		}
		return list;
	}
	
	public static String getSingleOfficerXml(Object obj){
		StringBuffer sb = new StringBuffer(64);
		if(obj instanceof DataObj){
			DataObj data = (DataObj)obj;
			sb.append("<user id=\"").append(data.usr_ste_usr_id).append("\" ent_id=\"").append(data.usr_ent_id).append("\">");
			sb.append("<name display_name=\"").append(data.usr_display_bil).append("\" />");
			sb.append("<full_path>").append(data.gpm_full_path).append("</full_path>");
			sb.append("</user>");
		}else{
			throw new RuntimeException("Server error: unknown data type");
		}
		return sb.toString();
	}
	
	
	
	
	//find out the entities who is already the officer(except the current tc)
	public static List isAlreadyOfficer(Connection con,long[] ent_id,String rol,long tcr_id){
		List list = new ArrayList();
		int i = 0;
		String exeSql = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try{
			exeSql = already_officer + dbUtils.array2list(ent_id);
			pst = con.prepareStatement(exeSql);
			pst.setString(1,rol);
			pst.setLong(2,tcr_id);
			rs = pst.executeQuery();
			while(rs.next()){
				list.add(new Long(rs.getLong(1)));
			}
		}catch(Exception e){
			throw new RuntimeException("Server error: "+e.getMessage());
		}finally{
			cwSQL.closeResultSet(rs);
			cwSQL.closePreparedStatement(pst);
		}
		return list;
	}
	
	//To see if the entity is only the role of the trainingcenter of the given tcr_id.
	public static List onlyInCurTc(Connection con,String rol,long tcr_id){
		List list = new ArrayList();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try{
			pst = con.prepareStatement(only_in_cur_tc);
			pst.setString(1,rol);
			pst.setLong(2,tcr_id);
			pst.setString(3,rol);
			pst.setLong(4,tcr_id);
			
			rs = pst.executeQuery();
			while(rs.next()){
				list.add(new Long(rs.getLong(1)));
			}
		}catch(SQLException e){
			throw new RuntimeException("Server error: "+e.getMessage());
		}finally{
			cwSQL.closeResultSet(rs);
			cwSQL.closePreparedStatement(pst);
		}
		return list;
	}

    // To be moved to ViewTrainingCenter
    public List getTrainingCenterByOfficer(Connection con, long usr_ent_id, String rol_ext_id, boolean withSubTc) throws SQLException {
        List v=null;
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(withSubTc ? SqlStatements.SQL_GET_TCR_BY_OFFICER_WITH_SUBTC : SqlStatements.SQL_GET_TCR_BY_OFFICER);
            stmt.setLong(1, usr_ent_id);
            stmt.setString(2, rol_ext_id);
            stmt.setString(3, DbTrainingCenter.STATUS_OK);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                if(v==null) {
                    v = new ArrayList();
                }
                DbTrainingCenter tcr = new DbTrainingCenter(rs.getLong("tcr_id"));
                tcr.setTcr_title(rs.getString("tcr_title"));
                tcr.setTcr_ste_ent_id(rs.getLong("tcr_ste_ent_id"));
                tcr.setTcr_status(rs.getString("tcr_status"));
                tcr.setTcr_create_timestamp(rs.getTimestamp("tcr_create_timestamp"));
                tcr.setTcr_create_usr_id(rs.getString("tcr_create_usr_id"));
                tcr.setTcr_update_timestamp(rs.getTimestamp("tcr_update_timestamp"));
                tcr.setTcr_update_usr_id(rs.getString("tcr_update_usr_id"));
                v.add(tcr);
            }
        } finally {
            if (stmt!=null) stmt.close();
        }
        return v;
    }
    
    public DbTrainingCenter getTrainingCenterById(Connection con, long tcr_id) throws SQLException {

        PreparedStatement stmt = null;
        DbTrainingCenter tcr = null;
        try {
            stmt = con.prepareStatement(SqlStatements.SQL_GET_TCR_BY_ID_SPEC);
            stmt.setLong(1, tcr_id);
            stmt.setString(2, DbTrainingCenter.STATUS_OK);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                tcr = new DbTrainingCenter(rs.getLong("tcr_id"));
                tcr.setTcr_title(rs.getString("tcr_title"));
                tcr.setTcr_ste_ent_id(rs.getLong("tcr_ste_ent_id"));
                tcr.setTcr_status(rs.getString("tcr_status"));
                tcr.setTcr_create_timestamp(rs.getTimestamp("tcr_create_timestamp"));
                tcr.setTcr_create_usr_id(rs.getString("tcr_create_usr_id"));
                tcr.setTcr_update_timestamp(rs.getTimestamp("tcr_update_timestamp"));
                tcr.setTcr_update_usr_id(rs.getString("tcr_update_usr_id"));
            }
        } finally {
            if (stmt!=null) stmt.close();
        }
        return tcr;
    }
    
    public List getTrainingCenterByTargetUser(Connection con, long usr_ent_id) throws SQLException {
        List l = null;
        PreparedStatement stmt = null;
        try {
            StringBuffer SQLBuf = new StringBuffer(SqlStatements.SQL_GET_TCR_BY_TARGET_USR);
            stmt = con.prepareStatement(SQLBuf.toString());
            stmt.setLong(1, usr_ent_id);
            stmt.setString(2, GPM_TYPE);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                if(l==null) {
                    l = new ArrayList();
                }
                DbTrainingCenter tcr = new DbTrainingCenter(rs.getLong("tcr_id"));
                tcr.setTcr_title(rs.getString("tcr_title"));
                tcr.setTcr_ste_ent_id(rs.getLong("tcr_ste_ent_id"));
                tcr.setTcr_status(rs.getString("tcr_status"));
                tcr.setTcr_create_timestamp(rs.getTimestamp("tcr_create_timestamp"));
                tcr.setTcr_create_usr_id(rs.getString("tcr_create_usr_id"));
                tcr.setTcr_update_timestamp(rs.getTimestamp("tcr_update_timestamp"));
                tcr.setTcr_update_usr_id(rs.getString("tcr_update_usr_id"));
                l.add(tcr);
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        return l;
    }
    
    private static final String DUP_CODE = "TC001";
    public static void isDupCode(Connection con, String code, long ste_ent_id)throws cwSysMessage{
    	boolean flag = false;
    	PreparedStatement pst = null;
    	ResultSet rs = null;
    	try{
    		pst = con.prepareStatement(dup_tc_code);
    		pst.setString(1,code);
            pst.setLong(2,ste_ent_id);
    		rs = pst.executeQuery();
			if(rs.next()){
				throw new cwSysMessage(DUP_CODE);
			}
	
    	}catch(SQLException e){
    		throw new RuntimeException("Server error: "+e.getMessage());
    	}finally{
    		cwSQL.cleanUp(rs,pst);
    	}
    }

    //To see if the role entity(have the given role)is the only one in all trainingcenter.
    public static List onlyRoleEntity4Tc(Connection con,long ent_id,String role){
    	boolean flag = false;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList list = new ArrayList();
        try{
        	pst = con.prepareStatement(only_role_tc);
			pst.setString(1,role);
        	pst.setLong(2,ent_id);
        	pst.setString(3,role);
        	rs = pst.executeQuery();
        	while(rs.next()){
        		if(rs.getInt("officer_num") == 1){
                    StringBuffer temp = new StringBuffer();
                    temp.append(rs.getString("tcr_title")).append("(").append(rs.getString("tcr_code")).append(")");
        			list.add(temp.toString());
                }
        	}
        }catch(SQLException e){
        	throw new RuntimeException("Server error: "+e.getMessage());
        }finally{
        	cwSQL.cleanUp(rs,pst);
        }
    	return list;
    }
    
    public static void delOfficerRoleFromTc(Connection con,long ent_id,String rol_id){
    	PreparedStatement pst = null;
    	try{
    		pst = con.prepareStatement(del_officer_from_tc);
    		pst.setLong(1,ent_id);
    		pst.setString(2,rol_id);
    		
    		pst.executeUpdate();
    	}catch(SQLException e){
    		throw new RuntimeException("Server error: "+e.getMessage());
    	}finally{
    		cwSQL.closePreparedStatement(pst);
    	}
    }
    /**
     * Get the item codes that will have no officer when the training center's officer list is updated
     * @param con Connection to database
     * @param tcr_id training center id
     * @param usr_ent_id_lst entity id list of the to be removed officers
     * @param rol_ext_id role external id of the officers
     * @return Vector of String item code  
     * @throws SQLException
     */
    public static Vector getSoleOfficerItem(Connection con, long tcr_id, long[] usr_ent_id_lst, String rol_ext_id) throws SQLException {
        Vector v = null;
        StringBuffer SQLBuf = new StringBuffer();
        SQLBuf.append("select distinct(itm_code) from aeItem, aeItemAccess a, tcTrainingCenterOfficer ")
              .append(" where tco_tcr_id = ? ")
              .append(" and tco_rol_ext_id = ? ");
	    if(usr_ent_id_lst != null && usr_ent_id_lst.length > 0 && rol_ext_id != null) {
	    	SQLBuf.append(" and tco_usr_ent_id not in ").append(cwUtils.array2list(usr_ent_id_lst)); 
	    }
	    SQLBuf.append(" and itm_tcr_id = tco_tcr_id ")
              .append(" and a.iac_itm_id = itm_id ")
              .append(" and a.iac_ent_id = tco_usr_ent_id ")
              .append(" and a.iac_access_type = ? ")
              .append(" and a.iac_access_id = tco_rol_ext_id ")
              .append(" and not exists ")
              .append(" (select b.iac_ent_id from aeItemAccess b ")
              .append(" where a.iac_itm_id = b.iac_itm_id ")
              .append(" and a.iac_access_type = b.iac_access_type ")
              .append(" and a.iac_access_id = b.iac_access_id ") 
              .append("and a.iac_ent_id <> b.iac_ent_id)");
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(SQLBuf.toString());
            stmt.setLong(1, tcr_id);
            stmt.setString(2, rol_ext_id);
            stmt.setString(3, aeItemAccess.ACCESS_TYPE_ROLE);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                if(v == null) {
                    v = new Vector();
                }
                v.addElement(rs.getString("itm_code"));
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        return v;
    }
    
    public static boolean isSuperTA(Connection con, long ste_ent_id, long usr_ent_id, String usr_rol_id) throws SQLException {
    	boolean result = false;
    	String sql = "select * from tcTrainingCenterOfficer "+ cwSQL.noLockTable() + ", tcTrainingCenter "+ cwSQL.noLockTable() + " where tcr_ste_ent_id = ? and tcr_status = ? and tcr_parent_tcr_id is null and tco_tcr_id = tcr_id and tco_usr_ent_id = ? and tco_rol_ext_id = ?";
    	PreparedStatement stmt = con.prepareStatement(sql);
    	int index = 1;
    	stmt.setLong(index++, ste_ent_id);
    	stmt.setString(index++, DbTrainingCenter.STATUS_OK);
    	stmt.setLong(index++, usr_ent_id);
    	stmt.setString(index++, usr_rol_id);
    	ResultSet rs = stmt.executeQuery();
    	if(rs.next()) {
    		result = true;
    	}
    	stmt.close();
    	
    	return result;
    }
    
    public static long getMajorTcrId(Connection con, long usr_ent_id, String usr_rol_id) throws SQLException {
    	long default_tcr_id = 0;
    	String sql = "select tco_tcr_id from tcTrainingCenterOfficer where tco_usr_ent_id = ? and tco_rol_ext_id = ? and tco_major_ind = ?";
    	PreparedStatement stmt = con.prepareStatement(sql);
    	int index = 1;
    	stmt.setLong(index++, usr_ent_id);
    	stmt.setString(index++, usr_rol_id);
    	stmt.setBoolean(index++, true);
    	ResultSet rs = stmt.executeQuery();
    	if(rs.next()) {
    		default_tcr_id = rs.getLong("tco_tcr_id");
    	}
    	stmt.close();
    	return default_tcr_id;
    }
    
    public static Vector getTopLevelTCList(Connection con, loginProfile prof) throws SQLException {
		Vector vec = new Vector();
		if(AccessControlWZB.isRoleTcInd(prof.current_role)){
			String sql = "select tcr_id " +
					"from tcTrainingCenterOfficer, tcTrainingCenter " +
					"where tco_usr_ent_id = ? and tco_rol_ext_id = ? " +
					"and tco_tcr_id = tcr_id and tcr_status = ? and tcr_ste_ent_id = ? order by tcr_title";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, prof.usr_ent_id);
			stmt.setString(2, prof.current_role);
			stmt.setString(3, DbTrainingCenter.STATUS_OK);
			stmt.setLong(4, prof.root_ent_id);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				vec.add(new Long(rs.getLong("tcr_id")));
			}
			stmt.close();
	    }else{
	    	vec.add(prof.my_top_tc_id );
	    }
		Vector toRemove = new Vector();
		for (int i = 0; i < vec.size(); i++) {
			Long cur_tcr_id = (Long)vec.elementAt(i);
			if (toRemove.contains(cur_tcr_id)) {
				continue;
			}
			Vector sub_tc = DbTcRelation.getSubTcList(con, cur_tcr_id.longValue());
			for(int j=0; j<sub_tc.size(); j++) {
				Long temp = (Long)sub_tc.elementAt(j);
				if(vec.contains(temp)){
					toRemove.add(temp);
				}
			}
		}
		for(int i=0; i<toRemove.size(); i++) {
			vec.remove((Long)toRemove.elementAt(i));
		}
		return vec;
	}
    
    public static boolean chkExceedParentTcUsgScope (Connection con, long parent_tcr_id, long tcr_ste_ent_id, long[] target_group) throws SQLException {
    	boolean result = false;
    	if(target_group != null && target_group.length != 0) {
    		Vector accessGroup = getParentTCRspGroup(con, parent_tcr_id, tcr_ste_ent_id);
    		Vector targetGroup = cwUtils.long2vector(target_group);
    		for (int i=0; i<targetGroup.size(); i++) {
    			if(!accessGroup.contains(targetGroup.elementAt(i))) {
    				result = true;
    				break;
    			}
    		}
    	}
    	return result;
    }
    
    public static boolean chkLessThanChildTcUsgScope(Connection con, long cur_tcr_id, long tcr_ste_ent_id, long[] target_group) throws SQLException {
    	boolean result = false;
    	Vector childTcGroup= getChildTCRspGroup(con, cur_tcr_id, tcr_ste_ent_id);
    	if(childTcGroup != null && childTcGroup.size() > 0) {
    		if(target_group != null && target_group.length > 0) {
    			Vector target_access_group = new Vector();
    			for (int i=0; i<target_group.length; i++) {
    				dbEntityRelation dber = new dbEntityRelation();
    				dber.ern_ancestor_ent_id = target_group[i];
    				dber.ern_type = dbEntityRelation.ERN_TYPE_USG_PARENT_USG;
    				Vector successorId = dber.getSuccessorID(con);
    				for (int j=0; j<successorId.size(); j++) {
    					target_access_group.add(successorId.elementAt(j));
    				}
    				target_access_group.add(new Long(target_group[i]));
    			}
    			for(int i=0; i<childTcGroup.size(); i++) {
    				if(!target_access_group.contains(childTcGroup.elementAt(i))) {
    					result = true;
    					break;
    				}
    			}
    		} else {
    			result = true;
    		}
    	}
    	return result;
    }
    
    public static Vector getParentTCRspGroup(Connection con, long parent_tcr_id, long tcr_ste_ent_id) throws SQLException {
    	Vector vec = new Vector();
    	StringBuffer sql = new StringBuffer();
    	sql.append(" select ern_child_ent_id from tcTrainingCenterTargetEntity, EntityRelation, tcTrainingCenter")
    		.append(" where (ern_ancestor_ent_id = tce_ent_id or tce_ent_id = ern_child_ent_id)")
    		.append(" and ern_type = ?")
    		//.append(" AND ern_parent_ind = ? ")
    		.append(" and tce_tcr_id = tcr_id")
    		.append(" and tcr_id = ?")
    		.append(" and tcr_status = ?")
    		.append(" and tcr_ste_ent_id = ?");
    	PreparedStatement stmt = con.prepareStatement(sql.toString());
    	int index = 1;
    	stmt.setString(index++, dbEntityRelation.ERN_TYPE_USG_PARENT_USG);
    //	stmt.setBoolean(index++, true);
    	stmt.setLong(index++, parent_tcr_id);
    	stmt.setString(index++, DbTrainingCenter.STATUS_OK);
    	stmt.setLong(index++, tcr_ste_ent_id);
    	ResultSet rs = stmt.executeQuery();
    	while(rs.next()) {
    		vec.add(new Long(rs.getLong("ern_child_ent_id")));
    	}
    	stmt.close();
    	return vec;
    }
    
    public static Vector getChildTCRspGroup(Connection con, long parent_tcr_id, long tcr_ste_ent_id) throws SQLException{
    	Vector vec = new Vector();
    	StringBuffer sql = new StringBuffer();
    	sql.append(" select distinct(tce_ent_id) from tcTrainingCenterTargetEntity, tcTrainingCenter, entity")
    		.append(" where tcr_parent_tcr_id = ?")
    		.append(" and tcr_id = tce_tcr_id")
    		.append(" and tcr_status = ?")
    		.append(" and tcr_ste_ent_id = ?")
    		.append(" and tce_ent_id = ent_id")
    		.append(" and ent_delete_timestamp is null");
    	PreparedStatement stmt = con.prepareStatement(sql.toString());
    	int index = 1;
    	stmt.setLong(index++, parent_tcr_id);
    	stmt.setString(index++, DbTrainingCenter.STATUS_OK);
    	stmt.setLong(index++, tcr_ste_ent_id);
    	ResultSet rs = stmt.executeQuery();
    	while(rs.next()) {
    		vec.add(new Long(rs.getLong("tce_ent_id")));
    	}
    	stmt.close();
    	return vec;
    }
    
	public static boolean hasRelateCatalog(Connection con,long tcr_id){
		String sql = "select cat_id from aeCatalog where cat_tcr_id = ?";
		PreparedStatement pst = null;
		ResultSet rs = null;
		boolean flag = false;
		try{
			pst = con.prepareStatement(sql);
			pst.setLong(1,tcr_id);
			rs = pst.executeQuery();
			if(rs.next()){
				flag = true;
			}
		}catch(SQLException e){
			throw new RuntimeException("Server error: "+e.getMessage());
		}finally{
			cwSQL.closeResultSet(rs);
			cwSQL.closePreparedStatement(pst);
		}
		return flag;
	}
	
	public static boolean hasRelateObjective(Connection con, long tcr_id) {
		String sql = "select obj_id from Objective where obj_tcr_id = ? and obj_status !='"+dbObjective.OBJ_STATUS_DELETED+"' ";
		PreparedStatement pst = null;
		ResultSet rs = null;
		boolean flag = false;
		try {
			pst = con.prepareStatement(sql);
			pst.setLong(1, tcr_id);
			rs = pst.executeQuery();
			if (rs.next()) {
				flag = true;
			}
		} catch (SQLException e) {
			throw new RuntimeException("Server error: " + e.getMessage());
		} finally {
			cwSQL.closeResultSet(rs);
			cwSQL.closePreparedStatement(pst);
		}
		return flag;
	}
	
	public static boolean hasRelateMessage(Connection con,long tcr_id){
		String sql = "select msg_id from Message where msg_tcr_id = ?";
		PreparedStatement pst = null;
		ResultSet rs = null;
		boolean flag = false;
		try{
			pst = con.prepareStatement(sql);
			pst.setLong(1,tcr_id);
			rs = pst.executeQuery();
			if(rs.next()){
				flag = true;
			}
		}catch(SQLException e){
			throw new RuntimeException("Server error: "+e.getMessage());
		}finally{
			cwSQL.closeResultSet(rs);
			cwSQL.closePreparedStatement(pst);
		}
		return flag;
	}
	
	public static boolean hasRelateArticle(Connection con,long tcr_id){
		String sql = "select art_id from article where art_tcr_id = ?";
		PreparedStatement pst = null;
		ResultSet rs = null;
		boolean flag = false;
		try{
			pst = con.prepareStatement(sql);
			pst.setLong(1,tcr_id);
			rs = pst.executeQuery();
			if(rs.next()){
				flag = true;
			}
		}catch(SQLException e){
			throw new RuntimeException("Server error: "+e.getMessage());
		}finally{
			cwSQL.closeResultSet(rs);
			cwSQL.closePreparedStatement(pst);
		}
		return flag;
	}
	
	public static boolean hasRelateCosEvaluation(Connection con,long tcr_id){
		String sql = "SELECT mod_res_id FROM module,resources WHERE mod_res_id = res_id AND mod_tcr_id = ? AND mod_type = ?";
		PreparedStatement pst = null;
		ResultSet rs = null;
		boolean flag = false;
		try{
			pst = con.prepareStatement(sql);
			pst.setLong(1,tcr_id);
			pst.setString(2,dbModule.MOD_TYPE_SVY);
			rs = pst.executeQuery();
			if(rs.next()){
				flag = true;
			}
		}catch(SQLException e){
			throw new RuntimeException("Server error: "+e.getMessage());
		}finally{
			cwSQL.closeResultSet(rs);
			cwSQL.closePreparedStatement(pst);
		}
		return flag;
	}
	
	public static boolean hasRelateEvaluation(Connection con,long tcr_id){
		String sql = "select mod_res_id from module,ModuleTrainingCenter where mtc_mod_id = mod_res_id and mtc_tcr_id = ? and mod_type=?";
		PreparedStatement pst = null;
		ResultSet rs = null;
		boolean flag = false;
		try{
			pst = con.prepareStatement(sql);
			pst.setLong(1,tcr_id);
			pst.setString(2,dbModule.MOD_TYPE_EVN);
			rs = pst.executeQuery();
			if(rs.next()){
				flag = true;
			}
		}catch(SQLException e){
			throw new RuntimeException("Server error: "+e.getMessage());
		}finally{
			cwSQL.closeResultSet(rs);
			cwSQL.closePreparedStatement(pst);
		}
		return flag;
	}
	public static boolean hasRelateStudyGroup(Connection con,long tcr_id){
		String sql = "select sgp_id from studyGroup where sgp_tcr_id = ?";
		PreparedStatement pst = null;
		ResultSet rs = null;
		boolean flag = false;
		try{
			pst = con.prepareStatement(sql);
			pst.setLong(1,tcr_id);
			rs = pst.executeQuery();
			if(rs.next()){
				flag = true;
			}
		}catch(SQLException e){
			throw new RuntimeException("Server error: "+e.getMessage());
		}finally{
			cwSQL.closeResultSet(rs);
			cwSQL.closePreparedStatement(pst);
		}
		return flag;
	}

    public static Vector getSoleItemWithTheOnlyOfficer(Connection con, long usr_ent_id, String rol_ext_id) throws SQLException {
        Vector v = new Vector();
        StringBuffer SQLBuf = new StringBuffer();
        SQLBuf.append("select distinct(itm_code) from aeItem, aeItemAccess a")
              .append(" where a.iac_itm_id = itm_id ")
              .append(" and a.iac_ent_id = ? ")
              .append(" and a.iac_access_type = ? ")
              .append(" and a.iac_access_id = ? ")
              .append(" and not exists ")
              .append(" (select b.iac_ent_id from aeItemAccess b ")
              .append(" where a.iac_itm_id = b.iac_itm_id ")
              .append(" and a.iac_access_type = b.iac_access_type ")
              .append(" and a.iac_access_id = b.iac_access_id ") 
              .append("and a.iac_ent_id <> b.iac_ent_id)");
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(SQLBuf.toString());
            int index = 1;
            stmt.setLong(index++, usr_ent_id);
            stmt.setString(index++, aeItemAccess.ACCESS_TYPE_ROLE);
            stmt.setString(index++, rol_ext_id);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                v.addElement(rs.getString("itm_code"));
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        return v;
    }
    
	//get the given user's responsible tc and sub tc.
	public static Vector getAllTcByOfficer (Connection con, long usr_ent_id) throws SQLException {
		Vector vec = new Vector();
		StringBuffer sql = new StringBuffer();
		sql.append(" select distinct child.tcr_id")
			.append(" from tcTrainingCenter ancestor")
			.append(" inner join tcTrainingCenterOfficer on (tco_tcr_id = ancestor.tcr_id)")
			.append(" left join tcRelation on (tcn_ancestor = ancestor.tcr_id)")
			.append(" inner join tcTrainingCenter child on (child.tcr_id = tcn_child_tcr_id or child.tcr_id = ancestor.tcr_id)")
			.append(" where tco_usr_ent_id = ?")
			.append(" and child.tcr_status = ?")
			.append(" and ancestor.tcr_status = ?");
		PreparedStatement stmt = con.prepareStatement(sql.toString());
		int index = 1;
		stmt.setLong(index++, usr_ent_id);
		stmt.setString(index++, DbTrainingCenter.STATUS_OK);
		stmt.setString(index++, DbTrainingCenter.STATUS_OK);
		ResultSet rs = stmt.executeQuery();
		while(rs.next()) {
			vec.add(new Long(rs.getLong("tcr_id")));
		}
		stmt.close();
		return vec;
	}
	
	public static void setMajorTcrId(Connection con, long usr_ent_id, String usr_rol_id, String update_usr_id, long major_tcr_id, Timestamp curTime) throws SQLException {
		
		delMajorTcrId(con, usr_ent_id, usr_rol_id, update_usr_id, curTime);
		
		String sql = "update tcTrainingCenterOfficer set tco_major_ind = 1, tco_update_timestamp = ?, tco_update_usr_id = ? where tco_usr_ent_id = ? and tco_rol_ext_id = ? and tco_tcr_id = ?";
		PreparedStatement stmt = con.prepareStatement(sql);		
		int index = 1;
		stmt.setTimestamp(index++, curTime);
		stmt.setString(index++, update_usr_id);
		stmt.setLong(index++, usr_ent_id);
		stmt.setString(index++, usr_rol_id);
		stmt.setLong(index++, major_tcr_id);
		stmt.executeUpdate();
		stmt.close();		
	}
	
	public static void delMajorTcrId(Connection con, long usr_ent_id, String usr_rol_id, String upd_usr_id, Timestamp curTime) throws SQLException {
		String sql = "update tcTrainingCenterOfficer set tco_major_ind = 0, tco_update_timestamp = ?, tco_update_usr_id = ? where tco_usr_ent_id = ? and tco_rol_ext_id = ? and tco_major_ind = 1 ";
		PreparedStatement stmt = con.prepareStatement(sql);
		int index = 1;
		stmt.setTimestamp(index++, curTime);
		stmt.setString(index++, upd_usr_id);
		stmt.setLong(index++, usr_ent_id);
		stmt.setString(index++, usr_rol_id);
		stmt.executeUpdate();	
		stmt.close();		
	}
	
	public static long getDefaultTc(Connection con, loginProfile prof) throws SQLException {
		long default_tc_id = 0;
		long sup_tcr_id = prof.my_top_tc_id;
		AcTrainingCenter actc = new AcTrainingCenter(con);
       	if (ViewTrainingCenter.isSuperTA(con, prof.root_ent_id, prof.usr_ent_id, prof.current_role)) {
       		default_tc_id = sup_tcr_id;
        } else if (AccessControlWZB.isRoleTcInd(prof.current_role)){
        	default_tc_id = ViewTrainingCenter.getMajorTcrId(con, prof.usr_ent_id, prof.current_role);
            if(default_tc_id == 0) {
            	Vector tcList = ViewTrainingCenter.getTopLevelTCList(con, prof);
            	if(tcList != null && (tcList.size() != 0)) {
            		default_tc_id = ((Long)tcList.elementAt(0)).longValue();
            	}
            }
        } else if(!AccessControlWZB.isRoleTcInd(prof.current_role)){
        	default_tc_id = sup_tcr_id;
        }
       	return default_tc_id;
	}
	
	public static void delRelateItemByUsr(Connection con, long tcr_id, long[] usr_ent_id_lst, String rol_ext_id) throws SQLException {
		String str_usr_ent_id = null;
		if(usr_ent_id_lst != null && usr_ent_id_lst.length > 0) {
			str_usr_ent_id = dbUtils.longArray2String(usr_ent_id_lst, ",");
		}
		StringBuffer sql = new StringBuffer();
		sql.append("delete from aeItemAccess")
			.append(" where iac_itm_id in(")
			.append(" select itm_id from aeItem where itm_tcr_id = ?)")
			.append(" and iac_access_id = ?")
			.append(" and iac_ent_id in(")
			.append(" select tco_usr_ent_id from tcTrainingCenterOfficer")
			.append(" where tco_tcr_id = ?")
			.append(" and tco_rol_ext_id = ?");
		if(str_usr_ent_id != null) {
			sql.append(" and tco_usr_ent_id not in (")
				.append(str_usr_ent_id)
				.append(")");
		}
		sql.append(")");
		PreparedStatement stmt = con.prepareStatement(sql.toString());
		int index = 1;
		stmt.setLong(index++, tcr_id);
		stmt.setString(index++, rol_ext_id);
		stmt.setLong(index++, tcr_id);
		stmt.setString(index++, rol_ext_id);
		stmt.execute();
		stmt.close();
	}
	public static long getDefaultTcId(Connection con, loginProfile prof) throws SQLException {
		long default_tc_id = 0;
		long sup_tcr_id = DbTrainingCenter.getSuperTcId(con, prof.root_ent_id);
		AcTrainingCenter actc = new AcTrainingCenter(con);
       	if (!AccessControlWZB.isRoleTcInd(prof.current_role)) {
       		default_tc_id = sup_tcr_id;
        } else{
        	default_tc_id = ViewTrainingCenter.getMajorTcrId(con, prof.usr_ent_id, prof.current_role);
        }
       	return default_tc_id;
	}

	public static boolean hasEffTc(Connection con, long usr_ent_id) throws SQLException {
        boolean hasEffTc = false;
        String sql = "select * from tcTrainingCenterOfficer "+ cwSQL.noLockTable() + " where tco_usr_ent_id = ? ";
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(sql);
            int index = 1;
            stmt.setLong(index++, usr_ent_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
            	hasEffTc = true;
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        return hasEffTc;
	}
    
    /**
     * to get id list of training center by user ent id
     * 获取用户所属的培训中心ID
     * @param con
     * @param usr_ent_id
     * @return id list of training center
     * @throws SQLException
     */
    public static Vector getTcIdsByUser(Connection con, long usr_ent_id,boolean isTcIndependent ) throws SQLException {
        Vector tcIdsVec = null;
        PreparedStatement stmt = null;
        try {
            
            String sql = SqlStatements.SQL_GET_TCR_BY_TARGET_USR + " and tcr_status = ? ";
            if(isTcIndependent){
                sql += " and tcr_parent_tcr_id > 0 " ;
            }
            sql += "  order by tcr_id " ;
            StringBuffer SQLBuf = new StringBuffer(sql);
            stmt = con.prepareStatement(SQLBuf.toString());
            int index = 1;
            stmt.setLong(index++, usr_ent_id);
            stmt.setString(index++, GPM_TYPE);
            stmt.setString(index++, DbTrainingCenter.STATUS_OK);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
            	if(tcIdsVec == null) {
            		tcIdsVec = new Vector();
            	}
                tcIdsVec.add(new Long(rs.getLong("tcr_id")));
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        return tcIdsVec;
    }
    

 
    //获取该用户所在的顶层培训中心, 如果第二级培训中心的独立,则取第二层,如果第二层不独立,则取根
    public static long getTopTc(Connection con, long usr_ent_id,boolean isTcIndependent) throws SQLException {
        long top_tcr_id = 0;
        
       
        if(isTcIndependent){
            Vector tcIdsVec =getTcIdsByUser( con,  usr_ent_id,isTcIndependent) ;
            if(tcIdsVec != null && tcIdsVec.size() > 0){
                top_tcr_id = ((Long)tcIdsVec.get(0)).longValue();
            }else{
                top_tcr_id=ViewTrainingCenter.getRootTcId(con);
            }
        }else{
            top_tcr_id=ViewTrainingCenter.getRootTcId(con);
        }
        return top_tcr_id;
    }
    

    
    //获取平台中根根即最顶层培训中心
    public static long getRootTcId(Connection con)throws SQLException {
    	long topTcId = 0;
        PreparedStatement stmt = null;
        ResultSet rs =null;
        try {
            stmt = con.prepareStatement("select tcr_id from  tcTrainingCenter where tcr_parent_tcr_id is null");
            rs= stmt.executeQuery();
            while(rs.next()) {
            	topTcId = rs.getLong(1);
            }
        } finally {
        	cwSQL.cleanUp(rs, stmt);
        }
        return topTcId;
    }
    
    //获取用户是否有所管理培训中心
    public static boolean hasManageTcInd(Connection con, long usr_ent_id) throws SQLException {
    	boolean man_tc_ind = false;
    	String sql = " select tco_tcr_id from tcTrainingCenterOfficer inner join tcTrainingCenter on tcr_id = tco_tcr_id "
    				+ " where tco_usr_ent_id = ? and tcr_status = 'OK' and (tcr_parent_tcr_id = 1 or tcr_parent_tcr_id is null) ";
    	PreparedStatement stmt = null;
        ResultSet rs =null;
        try{
        	stmt = con.prepareStatement(sql);
        	stmt.setLong(1, usr_ent_id);
        	rs = stmt.executeQuery();
        	if(rs.next()){
        		man_tc_ind = true;
        	}
        } finally {
        	cwSQL.cleanUp(rs, stmt);
        }
        return man_tc_ind;
        
    }
    
  //获取用户所管理培训中心
    public static long getUserManageTcrId(Connection con, long usr_ent_id) throws SQLException {
    	long tc_id = 1;
    	String sql = " select tco_tcr_id from tcTrainingCenterOfficer inner join tcTrainingCenter on tcr_id = tco_tcr_id "
    				+ " where tco_usr_ent_id = ? and tcr_status = 'OK' ";
    	PreparedStatement stmt = null;
        ResultSet rs =null;
        try{
        	stmt = con.prepareStatement(sql);
        	stmt.setLong(1, usr_ent_id);
        	rs = stmt.executeQuery();
        	if(rs.next()){
        		tc_id = rs.getLong("tco_tcr_id");
        	}
        } finally {
        	cwSQL.cleanUp(rs, stmt);
        }
        return tc_id;
        
    }
    public static Vector<TCBean> getTopLevelTrainingCenterList(Connection con) throws SQLException {
		Vector<TCBean> vec = new Vector<TCBean>();

		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			String sql = "";
			sql += " select tcr_id, tcr_code, tcr_title, tcr_parent_tcr_id ";
			sql += " from tcTrainingCenter";
			sql +=" where tcr_parent_tcr_id in ( ";
			sql += " 	select tcr_id from  tcTrainingCenter where tcr_parent_tcr_id is null ";
			sql += " )";
			sql += " order by tcr_title";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				TCBean tc = new TCBean();
				tc.setTcr_id(rs.getLong("tcr_id"));
				tc.setTcr_code(rs.getString("tcr_code"));
				tc.setTcr_title(rs.getString("tcr_title"));
				tc.setTcr_parent_tcr_id(rs.getInt("tcr_parent_tcr_id"));

				vec.add(tc);
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return vec;
	}
	/**
	 * 拿所有的下级培训中心
	 * @param con
	 * @param parent_tcr_id
	 * @return
	 * @throws SQLException
	 */
	public static Vector<TCBean> getSubLevelTrainingCenterList(Connection con, long parent_tcr_id) throws SQLException {
		Vector<TCBean> vec = new Vector<TCBean>();

		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			String sql = "";
			sql += " select tcr_id, tcr_code, tcr_title, tcr_parent_tcr_id ";
			sql += " from tcTrainingCenter";
			sql +=" where tcr_parent_tcr_id = ? ";
			sql += " order by tcr_title";

			stmt = con.prepareStatement(sql);
			stmt.setLong(1, parent_tcr_id);
			rs = stmt.executeQuery();
			while (rs.next()) {
				TCBean tc = new TCBean();
				tc.setTcr_id(rs.getLong("tcr_id"));
				tc.setTcr_code(rs.getString("tcr_code"));
				tc.setTcr_title(rs.getString("tcr_title"));
				tc.setTcr_parent_tcr_id(rs.getInt("tcr_parent_tcr_id"));

				vec.add(tc);
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return vec;
	}
}
