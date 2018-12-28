/*
 * Created on 2004-9-22
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.cw.wizbank.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.cw.wizbank.qdb.dbEntityRelation;
import com.cw.wizbank.qdb.dbObjective;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbResourceObjective;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwUtils;

/**
 * @author donaldl
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class DbTrainingCenter {
	public long tcr_id;
	public String tcr_code;
	public String tcr_title;
	private long tcr_ste_ent_id;
	private String tcr_status;
	private Timestamp tcr_create_timestamp;
	private String tcr_create_usr_id;
	private Timestamp tcr_update_timestamp;
	private String tcr_update_usr_id;
	private List relate_officer;
	private List relate_entity_group;
	public boolean major_tcr_ind;
	public boolean tcr_user_mgt_ind;
	public long parent_tcr_id;
	
	private static String TADM_1 = "TADM_1";
	public static String STATUS_OK = "OK";
	public static String STATUS_OFF = "DELETED";
	
	
	private static String getInstance = "select tcr_id,tcr_code,tcr_title,tcr_ste_ent_id,tcr_status,tcr_create_timestamp,tcr_create_usr_id,tcr_update_timestamp,tcr_update_usr_id, tcr_user_mgt_ind "
	                            +" from tcTrainingCenter where tcr_id = ?";
	private String insert = "insert into tcTrainingCenter (tcr_code,tcr_title,tcr_ste_ent_id,tcr_status,tcr_create_timestamp,tcr_create_usr_id,tcr_update_timestamp,tcr_update_usr_id, tcr_parent_tcr_id, tcr_user_mgt_ind ) "
	                        +" values (?,?,?,?,?,?,?,?,?,?)";
	
	private String upd = "update tcTrainingCenter set tcr_code = ?,tcr_title = ?,tcr_update_timestamp = ?,tcr_update_usr_id = ?, tcr_user_mgt_ind = ?  where tcr_id = ?";
	
	private String softDele = "update tcTrainingCenter set tcr_status = ?,tcr_update_timestamp = ?,tcr_update_usr_id = ? where tcr_id = ?";
	
	private String del = "delete from tcTrainingCenter where tcr_id = ?";
	
	private String getRelateEntity = "select tce.tce_tcr_id,tce.tce_ent_id,tce.tce_create_timestamp,tce.tce_create_usr_id,usg_display_bil from tcTrainingCenterTargetEntity tce,userGroup ,entity  where tce_tcr_id = ? and tce_ent_id = usg_ent_id and ent_id = usg_ent_id and ent_delete_timestamp is null";
	private String insRelateEntity = "insert into tcTrainingCenterTargetEntity(tce_tcr_id,tce_ent_id,tce_create_timestamp,tce_create_usr_id) values (?,?,?,?) ";
	private static String delRelateEntity = "delete from tcTrainingCenterTargetEntity where tce_tcr_id = ?";
	
	private String getRelateOfficer = "select tco_tcr_id,tco_usr_ent_id,tco_rol_ext_id,tco_create_timestamp,tco_create_usr_id,usr_display_bil from tcTrainingCenterOfficer,reguser where tco_tcr_id = ? and tco_usr_ent_id = usr_ent_id";
	private String insRelateOfficer = "insert into tcTrainingCenterOfficer(tco_tcr_id,tco_usr_ent_id,tco_rol_ext_id,tco_create_timestamp,tco_create_usr_id) values(?,?,?,?,?)";
	private static String delRelateOfficer = "delete from tcTrainingCenterOfficer where tco_tcr_id = ? ";
	private static String delTcRelation = "delete from tcRelation where tcn_child_tcr_id = ? ";
	private static String delRelateRoleOfficer = "delete from tcTrainingCenterOfficer where tco_tcr_id = ? and tco_rol_ext_id = ";
	
	private static final String UPD = "UPD";
	public static final String DEL = "DEL";
	public static final String SOFTDEL = "SOFTDEL";
	
	//id = 0 to contrust a new obj have null value except id;
	public DbTrainingCenter(long id){
		this.tcr_id = id;
	}
	public DbTrainingCenter(){}
	
	private void update(Connection con,String type){
		PreparedStatement pst = null;
		int i = 1;
		try{
			if(UPD.equalsIgnoreCase(type)){
				pst = con.prepareStatement(upd);
				pst.setString(i++,this.getTcr_code());
				pst.setString(i++,this.getTcr_title());
			}else if(SOFTDEL.equalsIgnoreCase(type)){
				pst = con.prepareStatement(softDele);
				pst.setString(i++,this.getTcr_status());
			}
			pst.setTimestamp(i++,cwSQL.getTime(con));
			pst.setString(i++,this.getTcr_update_usr_id());
			pst.setBoolean(i++,this.tcr_user_mgt_ind);
			pst.setLong(i++,this.getTcr_id());
			
			pst.executeUpdate();
		}catch(SQLException e){
			throw new RuntimeException("System error; "+e.getMessage());
		}finally{
			cwSQL.closePreparedStatement(pst);
		}
	}
	
	private static void delObj(Connection con,long id,String delSql){
				PreparedStatement pst = null;
				try{
					pst = con.prepareStatement(delSql);
					pst.setLong(1,id);
					pst.executeUpdate();
				}catch(SQLException e){
					throw new RuntimeException("System error: "+e.getMessage());
				}finally{
					cwSQL.closePreparedStatement(pst);
				}
			}
	
	public static DbTrainingCenter getInstance(Connection con,long id){
		DbTrainingCenter obj = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try{
			pst = con.prepareStatement(getInstance);
			pst.setLong(1,id);
			rs = pst.executeQuery();
			while(rs.next()){
				obj = new DbTrainingCenter(id);
				
				obj.setTcr_code(rs.getString("tcr_code"));
				obj.setTcr_create_timestamp(rs.getTimestamp("tcr_create_timestamp"));
				obj.setTcr_create_usr_id(rs.getString("tcr_create_usr_id"));
				obj.setTcr_status(rs.getString("tcr_status"));
				obj.setTcr_ste_ent_id(rs.getLong("tcr_ste_ent_id"));
				obj.setTcr_title(rs.getString("tcr_title"));
				obj.setTcr_update_timestamp(rs.getTimestamp("tcr_update_timestamp"));
				obj.setTcr_update_usr_id(rs.getString("tcr_update_usr_id"));
				obj.setTcr_user_mgt_ind(rs.getBoolean("tcr_user_mgt_ind"));
			}
		}catch(SQLException e){
			throw new RuntimeException("System error: "+e.getMessage());
		}finally{
			cwSQL.closeResultSet(rs);
			cwSQL.closePreparedStatement(pst);
		}
		return obj;
	}
	public static Vector getChildTc(Connection con, long tcr_id) throws SQLException {
		String sql = "SELECT tcn_child_tcr_id FROM tcRelation ,tcTrainingCenter WHERE tcn_ancestor = ? AND  tcn_child_tcr_id = tcr_id   AND tcr_status = ? ";
		PreparedStatement stmt = con.prepareStatement(sql);
		int index = 1;
		stmt.setLong(index++, tcr_id);
		stmt.setString(index++, STATUS_OK);
		ResultSet rs = stmt.executeQuery();
		Vector vec = new Vector();
		while(rs.next()) {
			vec.add(new Long(rs.getLong("tcn_child_tcr_id")));
		}
		stmt.close();
		return vec;
	}
	public static Vector getTcTaOfficer(Connection con, long tcr_id, String rol_ext_id) throws SQLException {
		String sql_get_tc_officer = "Select tco_usr_ent_id From tcTrainingCenterOfficer where tco_tcr_id =  ? and tco_rol_ext_id = ? ";
		PreparedStatement stmt = con.prepareStatement(sql_get_tc_officer);
		int index = 1;
		stmt.setLong(index++, tcr_id);
		stmt.setString(index++, rol_ext_id);
		ResultSet rs = stmt.executeQuery();
		Vector vec = new Vector();
		while(rs.next()) {
			vec.add(new Long(rs.getLong("tco_usr_ent_id")));
		}
		stmt.close();
		return vec;
	}
	
	// 查询当前培训中心下所有管理员
	public static Vector getTcOfficer(Connection con, long tcr_id,long []target_ent_id_lst) throws SQLException {
		String sql_get_tc_officer = "Select tco_usr_ent_id From tcTrainingCenterOfficer where tco_tcr_id =  ? and tco_usr_ent_id in" + cwUtils.vector2list(cwUtils.long2vector(target_ent_id_lst));
		PreparedStatement stmt = con.prepareStatement(sql_get_tc_officer);
		Vector vec = new Vector();
		stmt.setLong(1, tcr_id);

		ResultSet rs = stmt.executeQuery();
	
		while(rs.next()) {
			vec.add(new Long(rs.getLong("tco_usr_ent_id")));
		}
		stmt.close();
		return vec;
	}
	public static Vector getTcUserEntId(Connection con, long tcr_id) throws SQLException {
		StringBuffer sql = new StringBuffer(); 
		sql.append(" SELECT ern_child_ent_id From EntityRelation ")
		   .append(" INNER JOIN tcTrainingCenterTargetEntity ON (ern_ancestor_ent_id =tce_ent_id)")
		   .append(" WHERE ern_type = ?")
		   .append(" AND tce_tcr_id =? ");
		PreparedStatement stmt = con.prepareStatement(sql.toString());
		int index = 1;
		stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
		stmt.setLong(index++, tcr_id);
		ResultSet rs = stmt.executeQuery();
		Vector vec = new Vector();
		while(rs.next()) {
			vec.add(new Long(rs.getLong("ern_child_ent_id")));
		}
		stmt.close();
		return vec;
	}
	public static Vector getMgtResoursUsrEntId(Connection con, Vector role_types, long root_ent_id, long tcr_id) throws SQLException {
//    	Vector usr_id_vec = getTcUserEntId(con, tcr_id);
//    	String tableName = cwSQL.createSimpleTemptable(con, "tmp_ent_ids", cwSQL.COL_TYPE_LONG, 0);
//    	cwSQL.insertSimpleTempTable(con, tableName, usr_id_vec, cwSQL.COL_TYPE_LONG);
    	String list = "''";
    	if(role_types != null && role_types.size() > 0) {
    		for(int i = 0; i < role_types.size(); i++) {
    			list += ", '" + role_types.get(i) + "'";
    		}
    	}
    	String sql = "SELECT distinct (erl_ent_id) FROM acEntityRole " 
    		+" where ? between erl_eff_start_datetime and erl_eff_end_datetime"
    		+" AND erl_rol_id in ( SELECT rol_id FROM acRole WHERE rol_ste_ent_id = ?  AND rol_ext_id in (" + list+" ))" 
    		+" AND exists ( SELECT ern_child_ent_id From EntityRelation "  
    		+"  INNER JOIN tcTrainingCenterTargetEntity ON (ern_ancestor_ent_id =tce_ent_id)  WHERE ern_type = ?" 
    		+"    AND tce_tcr_id =? and erl_ent_id=ern_child_ent_id)";
    	PreparedStatement stmt = con.prepareStatement(sql);
    	Timestamp curTime = cwSQL.getTime(con);
    	int index = 1;
    	stmt.setTimestamp(index++, curTime);
    	stmt.setLong(index++, root_ent_id);
    	stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
    	stmt.setLong(index++, tcr_id);
    	ResultSet rs = stmt.executeQuery();
    	Vector vec_id = new Vector();
    	while(rs.next()) {
    		vec_id.add(new Long(rs.getLong("erl_ent_id")));
    	}
    	stmt.close();
//		cwSQL.dropTempTable(con, tableName);
    	return vec_id;
    }
	public void fillData(Connection con){
		//return getInstance(con,getTcr_id());
		PreparedStatement pst = null;
		ResultSet rs = null;
		try{
			pst = con.prepareStatement(getInstance);
			if(getTcr_id()==0){
				throw new SQLException("Cannot get the primary key of tcTrainingCenter");
			}
			pst.setLong(1,getTcr_id());
			rs = pst.executeQuery();
			while(rs.next()){
			   setTcr_code(rs.getString("tcr_code"));
			   setTcr_create_timestamp(rs.getTimestamp("tcr_create_timestamp"));
			   setTcr_create_usr_id(rs.getString("tcr_create_usr_id"));
			   setTcr_status(rs.getString("tcr_status"));
			   setTcr_ste_ent_id(rs.getLong("tcr_ste_ent_id"));
			   setTcr_title(rs.getString("tcr_title"));
			   setTcr_update_timestamp(rs.getTimestamp("tcr_update_timestamp"));
			   setTcr_update_usr_id(rs.getString("tcr_update_usr_id"));
			   tcr_user_mgt_ind = rs.getBoolean("tcr_user_mgt_ind");
			}
		}catch(SQLException e){
			throw new RuntimeException("System error: "+e.getMessage());
		}finally{
			cwSQL.closeResultSet(rs);
			cwSQL.closePreparedStatement(pst);
		}
	}
	
	public boolean equalData(DbTrainingCenter db){
		boolean flag = false;
		if(getTcr_update_timestamp().equals(db.getTcr_update_timestamp())){
			flag = true;
		}
		return flag;
	}
	
    public String obj2Xml(Connection con, boolean withUpdXml){
    	StringBuffer sb = new StringBuffer(64);
    	try{
    	  sb.append("<training_center id=\"").append(this.getTcr_id()).append("\" code=\"").append(cwUtils.esc4XML(this.getTcr_code())).append("\">");
    	  sb.append("<name>").append(cwUtils.esc4XML(getTcr_title())).append("</name>");
    	  if (withUpdXml) {
    		  sb.append(updXml(con,getTcr_update_usr_id(),getTcr_update_timestamp()));
    	  }
    	  sb.append("</training_center>");
    	}catch(SQLException e){
    		throw new RuntimeException("Cannot get the user's display bil at obj2Xml() in DbTrainingCenter");
    	}
    	return sb.toString();
    }
    
    public String objDetailXml(Connection con){
    	StringBuffer sb = new StringBuffer(64);
    	try{
		  sb.append("<name>").append(cwUtils.esc4XML(getTcr_title())).append("</name>");
		  sb.append(updXml(con,getTcr_update_usr_id(),getTcr_update_timestamp()));
    	}catch(SQLException e){
			throw new RuntimeException("Cannot get the user's display bil at objDetailXml() in DbTrainingCenter");
    	}
    	return sb.toString();
    }

    private String updXml(Connection con,String usrid,Timestamp time)throws SQLException{
    	return "<last_updated usr_id=\""+usrid+"\" timestamp=\""+time+"\">"+cwUtils.esc4XML(dbRegUser.getDisplayBil(con,getTcr_update_usr_id()))+"</last_updated>";
    }
	
	public void dbstore(Connection con, Timestamp cur_time){
		PreparedStatement pst = null;
		try{
			pst = con.prepareStatement(insert, PreparedStatement.RETURN_GENERATED_KEYS);
			pst.setString(1,this.getTcr_code());
			pst.setString(2,this.getTcr_title());
			pst.setLong(3,this.getTcr_ste_ent_id());
			pst.setString(4,this.getTcr_status());
			pst.setTimestamp(5, cur_time);
			pst.setString(6,this.getTcr_create_usr_id());
			pst.setTimestamp(7, cur_time);
			pst.setString(8,this.getTcr_update_usr_id());
			pst.setLong(9, this.parent_tcr_id);
			pst.setBoolean(10, this.tcr_user_mgt_ind);
			pst.executeUpdate();
			tcr_id = cwSQL.getAutoId(con, pst, "tcTrainingCenter", "tcr_id");
		}catch(SQLException e){
			throw new RuntimeException("System error: "+e.getMessage());
		}finally{
			cwSQL.closePreparedStatement(pst);
		}
	}
	
	public void dbupdate(Connection con){
		update(con,UPD);
	}
	
	public void delete(Connection con,String type){
		if(DEL.equalsIgnoreCase(type)){
			delObj(con,getTcr_id(),del);
		}else if(SOFTDEL.equalsIgnoreCase(type)){
			update(con,SOFTDEL);
		}
	}
	
	
	

	
	public List getRelateTargetEntity(Connection con,long id){
		List entity = new ArrayList();
		DbTrainTargetEntity obj = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try{
			pst = con.prepareStatement(getRelateEntity);
			pst.setLong(1,id);
			rs = pst.executeQuery();
			while(rs.next()){
				obj = new DbTrainTargetEntity(rs.getLong("tce_tcr_id"),rs.getLong("tce_ent_id"));
				obj.setTce_create_timestamp(rs.getTimestamp("tce_create_timestamp"));
				obj.setTce_create_usr_id(rs.getString("tce_create_usr_id"));
				obj.setEnt_display_bil(rs.getString("usg_display_bil"));
				entity.add(obj);
			}
		}catch(SQLException e){
			throw new RuntimeException("System error: "+e.getMessage());
		}finally{
			cwSQL.closeResultSet(rs);
			cwSQL.closePreparedStatement(pst);
		}
		return entity;
	}
	
	public void storeTargetEntity(Connection con,long[] entity_ids){
		PreparedStatement pst = null;
		try{
			Timestamp time = cwSQL.getTime(con);
			pst = con.prepareStatement(insRelateEntity);
			pst.setLong(1,this.getTcr_id());
			pst.setTimestamp(3,time);
			pst.setString(4,this.getTcr_update_usr_id());
			for(int i=0,n=entity_ids.length;i<n;i++){
				pst.setLong(2,entity_ids[i]);
				pst.addBatch();
			}
			pst.executeBatch();
		}catch(Exception e){
			throw new RuntimeException("System error: "+e.getMessage());
		}finally{
			cwSQL.closePreparedStatement(pst);
		}
	}
	
	public void delRelateEntiry(Connection con){
		delObj(con,getTcr_id(),delRelateEntity);
	}
	
	public List getRelateOfficer(Connection con,long id){
		ArrayList list = new ArrayList();
		PreparedStatement pst = null;
		ResultSet rs = null;
		DbTrainOfficer obj = null;
		try{
			pst = con.prepareStatement(this.getRelateOfficer);
			pst.setLong(1,id);
			rs = pst.executeQuery();
			while(rs.next()){
				obj = new DbTrainOfficer(rs.getLong("tco_tcr_id"),rs.getLong("tco_usr_ent_id"));
				obj.setEnt_display_bil(rs.getString("usr_display_bil"));
				obj.setTco_rol_ext_id(rs.getString("tco_rol_ext_id"));
				obj.setTco_create_timestamp(rs.getTimestamp("tco_create_timestamp"));
				obj.setTco_create_usr_id(rs.getString("tco_create_usr_id"));
				
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
	
	public void storeRelateOfficer(Connection con,long[] ent_id,String role){
		PreparedStatement pst = null;
		try{
			Timestamp time = cwSQL.getTime(con);
			pst = con.prepareStatement(insRelateOfficer);
			pst.setLong(1,this.getTcr_id());
			pst.setString(3,role);
			pst.setTimestamp(4,time);
			pst.setString(5,this.getTcr_update_usr_id());
			for(int i=0,n=ent_id.length;i<n;i++){
				if(ent_id[i]==0){
					continue;
				}
				pst.setLong(2,ent_id[i]);
				pst.addBatch();
			}
			pst.executeBatch();
		}catch(SQLException e){
			throw new RuntimeException("System error: "+e.getMessage());
		}finally{
		    cwSQL.closePreparedStatement(pst);
	    }
	}
	public static long getResTopFolderTcrId(Connection con, long res_id) throws SQLException {
		StringBuffer sql = new StringBuffer(); 
	    String dbproduct = cwSQL.getDbProductName();
	    if (dbproduct.indexOf(cwSQL.ProductName_DB2) >= 0) {
	    	long obj_id =dbResourceObjective.getObjIdByResId(con, res_id);
	    	String ancester = dbObjective.getObjAncester(con, obj_id);
	    	if(ancester !=null && ancester.length() ==0) {
	    		ancester = "(" + obj_id +")";
	    	} else {
	    		ancester = "(" + ancester +")";
	    	}
	    	sql.append(" select").append(cwSQL.replaceNull("ancestor.obj_tcr_id", "child.obj_tcr_id"))
	    	   .append(" tcr_id  from ResourceObjective")
	    	   .append(" inner join Objective child on (child.obj_id = rob_obj_id)")
	    	   .append(" left join Objective ancestor on (ancestor.obj_id in ").append(ancester) 
	    	   .append(" and  ancestor.obj_tcr_id is not null )")
	    	   .append(" inner join tcTrainingCenter on (tcr_id = ancestor.obj_tcr_id or tcr_id = child.obj_tcr_id)")
	    	   .append(" where rob_res_id =? and tcr_status = ? ");
	    	
	    } else {
	    	String operator = cwSQL.getConcatOperator();
	    	String expression = cwSQL.convertInt2String("ancestor.obj_id");
	    	sql.append(" select").append(cwSQL.replaceNull("ancestor.obj_tcr_id", "child.obj_tcr_id"))
	    	   .append(" tcr_id  from ResourceObjective")
	    	   .append(" inner join Objective child on (child.obj_id = rob_obj_id)")
	    	   .append(" left join Objective ancestor on ( child.obj_ancester like '% '").append(operator).append(expression).append(operator).append("' %'")
	    	   .append(" and  ancestor.obj_tcr_id is not null )")
	    	   .append(" inner join tcTrainingCenter on (tcr_id = ancestor.obj_tcr_id or tcr_id = child.obj_tcr_id)")
	    	   .append(" where rob_res_id =? and tcr_status = ? ");
        }
	    PreparedStatement stmt = con.prepareStatement(sql.toString());
	    int index = 1;
	    stmt.setLong(index++, res_id);
	    stmt.setString(index++, STATUS_OK);
	    ResultSet rs = stmt.executeQuery();
	    long tcr_id = 0;
	    if(rs.next()) {
	    	tcr_id = rs.getLong("tcr_id");
	    }
	    stmt.close();
	    return tcr_id;
	}
	public static long getObjTopTcrId(Connection con, long obj_id) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" select tcr_id from Objective child ")
		   .append(" left join Objective ancestor on (")
		   .append(cwSQL.subFieldLocation("child.obj_ancester", "ancestor.obj_id", true))
		   .append(" and  ancestor.obj_tcr_id is not null) ")
		   .append(" inner join tcTrainingCenter on (tcr_id = ancestor.obj_tcr_id or tcr_id = child.obj_tcr_id) ")
		   .append(" where child.obj_id =? and tcr_status = ? ");
	    PreparedStatement stmt = con.prepareStatement(sql.toString());
	    int index = 1;
	    stmt.setLong(index++, obj_id);
	    stmt.setString(index++, STATUS_OK);
	    ResultSet rs = stmt.executeQuery();
	    long tcr_id = 0;
	    if(rs.next()) {
	    	tcr_id = rs.getLong("tcr_id");
	    }
	    stmt.close();
	    return tcr_id;
	}
	public void delRelateOfficer(Connection con){
		delObj(con,getTcr_id(),delRelateOfficer);
	}
	
	public void delRelateOfficer(Connection con,String rol){
		delObj(con,getTcr_id(),delRelateRoleOfficer+" '"+rol+"' ");
	}
		
	public List getRelate_entity_group(Connection con) {
		if(relate_entity_group == null){
			relate_entity_group = this.getRelateTargetEntity(con,this.getTcr_id());
		}
		 return relate_entity_group;  
	}

	
	public List getRelate_officer(Connection con) {
		if(relate_officer == null){
			relate_officer = this.getRelateOfficer(con,this.getTcr_id());
		}
		return relate_officer;
	}
	
	
////////////////////////////////////////////////////////////////////////////////////////////////

	public String getTcr_code() {
		return tcr_code;
	}


	public Timestamp getTcr_create_timestamp() {
		return tcr_create_timestamp;
	}


	public String getTcr_create_usr_id() {
		return tcr_create_usr_id;
	}

	public void setTcr_id(long tcr_id) {
		this.tcr_id = tcr_id;
	}

	public long getTcr_id() {
		return tcr_id;
	}


	public String getTcr_status() {
		return tcr_status;
	}


	public long getTcr_ste_ent_id() {
		return tcr_ste_ent_id;
	}


	public String getTcr_title() {
		return tcr_title;
	}


	public Timestamp getTcr_update_timestamp() {
		return tcr_update_timestamp;
	}


	public String getTcr_update_usr_id() {
		return tcr_update_usr_id;
	}


	public void setTcr_code(String string) {
		tcr_code = string;
	}


	public void setTcr_create_timestamp(Timestamp timestamp) {
		tcr_create_timestamp = timestamp;
	}


	public void setTcr_create_usr_id(String string) {
		tcr_create_usr_id = string;
	}


	public void setTcr_status(String string) {
		tcr_status = string;
	}


	public void setTcr_ste_ent_id(long i) {
		tcr_ste_ent_id = i;
	}


	public void setTcr_title(String string) {
		tcr_title = string;
	}


	public void setTcr_update_timestamp(Timestamp timestamp) {
		tcr_update_timestamp = timestamp;
	}

	public void setTcr_update_usr_id(String string) {
		tcr_update_usr_id = string;
	}
	
	public void setTcr_user_mgt_ind(boolean user_mgt_ind) {
		tcr_user_mgt_ind = user_mgt_ind;
	}
	
	public void setParent_tcr_id(long tcr_id) {
		parent_tcr_id = tcr_id;
	}
	
	public static long getSuperTcId (Connection con, long tcr_ste_ent_id) throws SQLException {
		long sup_tc_id = 0;
		String sql = "select tcr_id from tcTrainingCenter where tcr_ste_ent_id = ? and tcr_status = ? and tcr_parent_tcr_id is null";
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setLong(1, tcr_ste_ent_id);
		stmt.setString(2, STATUS_OK);
		ResultSet rs = stmt.executeQuery();
		if(rs.next()) {
			sup_tc_id = rs.getLong("tcr_id");
		}
		stmt.close();
		return sup_tc_id;
	}
	
	public Vector getChildTcList(Connection con) throws SQLException{
		Vector vec = new Vector();
		String sql = "select tcr_id, tcr_title from tcTrainingCenter where tcr_parent_tcr_id = ? and tcr_status = ? and tcr_ste_ent_id = ?";
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setLong(1, this.tcr_id);
		stmt.setString(2, STATUS_OK);
		stmt.setLong(3, this.tcr_ste_ent_id);
		ResultSet rs = stmt.executeQuery();
		while(rs.next()) {
			DbTrainingCenter tc = new DbTrainingCenter();
			tc.tcr_id = rs.getLong("tcr_id");
			tc.tcr_title = rs.getString("tcr_title");
			vec.add(tc);
		}
		stmt.close();
        return vec;
	}
	
	public static long getParentTcId(Connection con, long tcr_id) throws SQLException {
		long parent_tcr_id = 0;
		String sql = "select tcr_parent_tcr_id from tcTrainingCenter where tcr_id = ? and tcr_status = ?";
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setLong(1, tcr_id);
		stmt.setString(2, STATUS_OK);
		ResultSet rs = stmt.executeQuery();
		if(rs.next()) {
			parent_tcr_id = rs.getLong("tcr_parent_tcr_id");
		}
		stmt.close();
		return parent_tcr_id;
	}
	
	public static boolean chkHasChildTc(Connection con, long tcr_id) throws SQLException {
		boolean result = false;
		String sql = "select tcr_id from tcTrainingCenter where tcr_parent_tcr_id = ? and tcr_status = ?";
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setLong(1, tcr_id);
		stmt.setString(2, STATUS_OK);
		ResultSet rs = stmt.executeQuery();
		if(rs.next()) {
			result = true;
		}
		stmt.close();
		return result;
	}
	
	/**
	 * 判断是否有资源
	 * @author lucky
	 * @param con
	 * @param tcr_id
	 * @return
	 * @throws SQLException
	 */
	public static boolean chkHasObjective(Connection con, long tcr_id) throws SQLException {
		boolean result = false;
		String sql = "SELECT obj_id FROM Objective WHERE obj_tcr_id in ( select tcn_child_tcr_id from tcRelation where tcn_ancestor = ? ) OR obj_tcr_id = ? ";
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setLong(1, tcr_id);
		stmt.setLong(2, tcr_id);
		ResultSet rs = stmt.executeQuery();
		if(rs.next()) {
			result = true;
		}
		stmt.close();
		return result;
	}
	
	/**
	 * 判断是否有标签
	 * @author lucky
	 * @param con
	 * @param tcr_id
	 * @return
	 * @throws SQLException
	 */
	public static boolean chkHasTag(Connection con, long tcr_id) throws SQLException {
		boolean result = false;
		String sql = "SELECT tag_id FROM Tag WHERE tag_tcr_id in ( select tcn_child_tcr_id from tcRelation where tcn_ancestor = ? ) OR tag_tcr_id = ? ";
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setLong(1, tcr_id);
		stmt.setLong(2, tcr_id);
		ResultSet rs = stmt.executeQuery();
		if(rs.next()) {
			result = true;
		}
		stmt.close();
		return result;
	}
	
	/**
	 * 判断是否有直播
	 * @author lucky
	 * @param con
	 * @param tcr_id
	 * @return
	 * @throws SQLException
	 */
	public static boolean chkHasLive(Connection con, long tcr_id) throws SQLException {
		boolean result = false;
		String sql = "SELECT lv_id FROM liveItem WHERE lv_tcr_id in ( select tcn_child_tcr_id from tcRelation where tcn_ancestor = ? ) OR lv_tcr_id = ? ";
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setLong(1, tcr_id);
		stmt.setLong(2, tcr_id);
		ResultSet rs = stmt.executeQuery();
		if(rs.next()) {
			result = true;
		}
		stmt.close();
		return result;
	}
	
	/**
	 * 判断是否有知识目录
	 * @author lucky
	 * @param con
	 * @param tcr_id
	 * @return
	 * @throws SQLException
	 */
	public static boolean chkHasKBcatalog(Connection con, long tcr_id) throws SQLException {
		boolean result = false;
		String sql = "SELECT kbc_id FROM kb_catalog WHERE kbc_tcr_id in ( select tcn_child_tcr_id from tcRelation where tcn_ancestor = ? ) OR kbc_tcr_id = ? ";
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setLong(1, tcr_id);
		stmt.setLong(2, tcr_id);
		ResultSet rs = stmt.executeQuery();
		if(rs.next()) {
			result = true;
		}
		stmt.close();
		return result;
	}
	
	/**
	 * 判断是否有投票
	 * @author lucky
	 * @param con
	 * @param tcr_id
	 * @return
	 * @throws SQLException
	 */
	public static boolean chkHasVoting(Connection con, long tcr_id) throws SQLException {
		boolean result = false;
		String sql = "SELECT vot_tcr_id FROM Voting WHERE vot_status != 'DEL' and (vot_tcr_id in ( select tcn_child_tcr_id from tcRelation where tcn_ancestor = ? ) OR vot_tcr_id = ?) ";
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setLong(1, tcr_id);
		stmt.setLong(2, tcr_id);
		ResultSet rs = stmt.executeQuery();
		if(rs.next()) {
			result = true;
		}
		stmt.close();
		return result;
	}
	
	public void delTcRelation(Connection con){
		delObj(con,getTcr_id(), delTcRelation);
	}
	
	public static void updTimeAndUser(Connection con, long tcr_id, String usr_id, Timestamp cur_time) throws SQLException {
		String sql = "update tcTrainingCenter set tcr_update_timestamp = ?, tcr_update_usr_id = ? where tcr_id = ?";
		PreparedStatement stmt = con.prepareStatement(sql);
		int index = 1;
		stmt.setTimestamp(index++, cur_time);
		stmt.setString(index++, usr_id);
		stmt.setLong(index++, tcr_id);
		stmt.execute();
		stmt.close();
	}
	
	public void get(Connection con) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from tcTrainingCenter where tcr_id = ? ");
		
		PreparedStatement pst = null;
		ResultSet rs = null;
		try{
			pst = con.prepareStatement(sql.toString());
			pst.setLong(1, tcr_id);
			rs = pst.executeQuery();
			if(rs.next()){
				this.setTcr_code(rs.getString("tcr_code"));
				this.setTcr_create_timestamp(rs.getTimestamp("tcr_create_timestamp"));
				this.setTcr_create_usr_id(rs.getString("tcr_create_usr_id"));
				this.setTcr_status(rs.getString("tcr_status"));
				this.setTcr_ste_ent_id(rs.getLong("tcr_ste_ent_id"));
				this.setTcr_title(rs.getString("tcr_title"));
				this.setTcr_update_timestamp(rs.getTimestamp("tcr_update_timestamp"));
				this.setTcr_update_usr_id(rs.getString("tcr_update_usr_id"));
			}
		}finally{
			cwSQL.closePreparedStatement(pst);
		}
	}
	
	static final String ins_super_tc_copy = "insert into tcTrainingCenter " +
	   " (tcr_code, tcr_title, tcr_ste_ent_id, tcr_status, tcr_create_timestamp, tcr_create_usr_id, tcr_update_timestamp, tcr_update_usr_id, tcr_parent_tcr_id, tcr_user_mgt_ind) " +
	   " select tcr_code, tcr_title, ?, tcr_status, ?, ?, ?, " +
	   " ?, tcr_parent_tcr_id, tcr_user_mgt_ind " +
	   " from tcTrainingcenter where tcr_ste_ent_id = ? and tcr_status = ? and tcr_parent_tcr_id is null ";
	public static long copyHeadTcToAnotherOrg(Connection con, long src_ste_ent_id, long target_ste_ent_id, String new_admin_usr_id) throws SQLException {
		Timestamp cur_time = cwSQL.getTime(con);
		PreparedStatement pst = null;
		long head_tc_id = 0;
		try {
			pst = con.prepareStatement(ins_super_tc_copy);
			int index = 1;
			pst.setLong(index++, target_ste_ent_id);
			pst.setTimestamp(index++, cur_time);
			pst.setString(index++, new_admin_usr_id);
			pst.setTimestamp(index++, cur_time);
			pst.setString(index++, new_admin_usr_id);
			pst.setLong(index++, src_ste_ent_id);
			pst.setString(index++, STATUS_OK);
			int ins_count = pst.executeUpdate();
			if (ins_count == 1) {
				head_tc_id = getSuperTcId(con, target_ste_ent_id);
			}
		} finally {
			if (pst != null) {
				pst.close();
			}
		}
		return head_tc_id;
	}
    public static String getTcrTitle(Connection con, long tcr_id)  throws SQLException{
        String tcr_title= "";
        String sql = "select tcr_title from tcTrainingCenter where tcr_id = ? and tcr_status = ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setLong(1, tcr_id);
        stmt.setString(2, STATUS_OK);
        ResultSet rs = stmt.executeQuery();
        while(rs.next()) {
        	tcr_title = rs.getString("tcr_title");
        }
        stmt.close();
        return tcr_title;
    }
    public static long getMaxTcrId(Connection con) throws SQLException {
    	String SQL = "SELECT MAX(tcr_id) FROM tcTrainingCenter";
    	PreparedStatement pstmt = con.prepareStatement(SQL);
    	ResultSet rs = pstmt.executeQuery();
    	int tcr_id = 0;
    	if(rs.next()){
    		tcr_id = rs.getInt(1);
    	}
    	return tcr_id;
    }
    
}
