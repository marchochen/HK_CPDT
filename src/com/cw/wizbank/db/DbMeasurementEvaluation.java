package com.cw.wizbank.db;

/**
 * @author vincent
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import com.cw.wizbank.ae.aeApplication;
import com.cw.wizbank.course.CourseCriteria;
import com.cw.wizbank.db.sql.OuterJoinSqlStatements;
import com.cw.wizbank.qdb.dbCourse;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.*;

public class DbMeasurementEvaluation {
	private long mtv_cos_id;
	private long mtv_ent_id;
	private long mtv_cmt_id;
	private String mtv_status;
	private float mtv_score = 0;
	private Timestamp mtv_create_timestamp;
	private String mtv_create_usr_id;
	private Timestamp mtv_update_timestamp;
	private String mtv_update_usr_id;
	private long mtv_tkh_id;
	private float cmt_max_score;
    public long app_id;
	
	private static String oneRecord = "select * from MeasurementEvaluation where mtv_ent_id = ? and mtv_cmt_id = ? and mtv_tkh_id = ?"; 
	public static final String GET_EVAL_ITEM = "select distinct app_id,usr_display_bil,usr_ent_id,mtv_ent_id,mtv_score,mtv_tkh_id from aeApplication,RegUser,MeasurementEvaluation " +        "where app_tkh_id=mtv_tkh_id and usr_ent_id = mtv_ent_id and mtv_cmt_id = ? ";
	static final String DEL_BY_CMT_ID = "delete from MeasurementEvaluation where mtv_cmt_id = ? ";	
	static final String CHECK_EVAL_BY_CMT_ID = "select * from MeasurementEvaluation where mtv_cmt_id = ? and mtv_score is not null";
    public static final String UPD_SCORE = "update MeasurementEvaluation set mtv_score = ? " +        "where mtv_cmt_id =? and mtv_ent_id=? and mtv_tkh_id = ?";
    public static final String UPD_SCORE_BY_TKH_ID = "update MeasurementEvaluation set mtv_score = ? , mtv_update_timestamp = ? , mtv_update_usr_id = ? " +
        "where mtv_ent_id = ?  and mtv_cmt_id = ? and mtv_tkh_id = ?  ";
    public static final String INSERT_RECORD = "insert into MeasurementEvaluation " +        "(mtv_cos_id,mtv_ent_id,mtv_cmt_id,mtv_score,mtv_create_timestamp,mtv_update_timestamp,mtv_create_usr_id,mtv_update_usr_id,mtv_tkh_id) " +        "values(?,?,?,?,?,?,?,?,?)";
    public static final String RESET_SCORE = "update MeasurementEvaluation set mtv_score = null , mtv_status = null " +
        "where mtv_cmt_id =? ";
    
	public boolean fillData(Connection con){
		if(this.getMtv_ent_id()==0 || this.getMtv_cmt_id()==0 || this.getMtv_tkh_id()==0){
			throw new RuntimeException("Sever error: primary key in MeasurementEvaluation cannot be null");
		}
		PreparedStatement pst = null;
		ResultSet rs = null;
		try{
			pst = con.prepareStatement(oneRecord);
			pst.setLong(1,this.getMtv_ent_id());
			pst.setLong(2,this.getMtv_cmt_id());
			pst.setLong(3,this.getMtv_tkh_id());
			
			rs = pst.executeQuery();
			while(rs.next()){
				this.setMtv_cos_id(rs.getLong("Mtv_cos_id"));
				this.setMtv_create_timestamp(rs.getTimestamp("Mtv_create_timestamp"));
				this.setMtv_create_usr_id(rs.getString("Mtv_create_usr_id"));
				this.setMtv_score(rs.getFloat("Mtv_score"));
				this.setMtv_status(rs.getString("Mtv_status"));
				this.setMtv_update_timestamp(rs.getTimestamp("Mtv_update_timestamp"));
				this.setMtv_update_usr_id(rs.getString("Mtv_update_usr_id"));
				
				return true;
			}
		}catch(SQLException e){
			throw new RuntimeException("Sever error: "+e.getMessage());
		}finally{
			cwSQL.cleanUp(rs,pst);
		}
		return false;
	}
	
//	static final String 
	public void ins(Connection con,boolean has_score) throws SQLException, qdbException {
        PreparedStatement pstmt = con.prepareStatement(INSERT_RECORD);
        pstmt.setLong(1,mtv_cos_id);
        pstmt.setLong(2,mtv_ent_id);
        pstmt.setLong(3,mtv_cmt_id);
        if(has_score){
            pstmt.setFloat(4,mtv_score);
        }else{
            pstmt.setNull(4,java.sql.Types.DECIMAL);
        }
        pstmt.setTimestamp(5,dbUtils.getTime(con));
        pstmt.setTimestamp(6,dbUtils.getTime(con));
        pstmt.setString(7,mtv_create_usr_id);
        pstmt.setString(8,mtv_create_usr_id);
        pstmt.setLong(9,mtv_tkh_id);
        pstmt.executeUpdate();
        pstmt.close();
        return;
	}
	
	
	/**
	 * @return
	 */
	public long getMtv_cmt_id() {
		return mtv_cmt_id;
	}

	/**
	 * @return
	 */
	public long getMtv_cos_id() {
		return mtv_cos_id;
	}

	/**
	 * @return
	 */
	public Timestamp getMtv_create_timestamp() {
		return mtv_create_timestamp;
	}

	/**
	 * @return
	 */
	public String getMtv_create_usr_id() {
		return mtv_create_usr_id;
	}

	/**
	 * @return
	 */
	public long getMtv_ent_id() {
		return mtv_ent_id;
	}

	/**
	 * @return
	 */
	public float getMtv_score() {
		return mtv_score;
	}

	/**
	 * @return
	 */
	public String getMtv_status() {
		return mtv_status;
	}

	/**
	 * @return
	 */
	public long getMtv_tkh_id() {
		return mtv_tkh_id;
	}

	/**
	 * @return
	 */
	public Timestamp getMtv_update_timestamp() {
		return mtv_update_timestamp;
	}

	/**
	 * @return
	 */
	public String getMtv_update_usr_id() {
		return mtv_update_usr_id;
	}

	/**
	 * @param l
	 */
	public void setMtv_cmt_id(long cmt_id) {
		mtv_cmt_id = cmt_id;
	}

	/**
	 * @param l
	 */
	public void setMtv_cos_id(long cos_id) {
		mtv_cos_id = cos_id;
	}

	/**
	 * @param timestamp
	 */
	public void setMtv_create_timestamp(Timestamp create_timestamp) {
		mtv_create_timestamp = create_timestamp;
	}

	/**
	 * @param string
	 */
	public void setMtv_create_usr_id(String create_usr_id) {
		mtv_create_usr_id = create_usr_id;
	}

	/**
	 * @param l
	 */
	public void setMtv_ent_id(long ent_id) {
		mtv_ent_id = ent_id;
	}

	/**
	 * @param f
	 */
	public void setMtv_score(float score) {
		mtv_score = score;
	}

	/**
	 * @param string
	 */
	public void setMtv_status(String status) {
		mtv_status = status;
	}

	/**
	 * @param l
	 */
	public void setMtv_tkh_id(long tkh_id) {
		mtv_tkh_id = tkh_id;
	}

	/**
	 * @param timestamp
	 */
	public void setMtv_update_timestamp(Timestamp update_timestamp) {
		mtv_update_timestamp = update_timestamp;
	}

	/**
	 * @param string
	 */
	public void setMtv_update_usr_id(String update_usr_id) {
		mtv_update_usr_id = update_usr_id;
	}
	
    public float getCmt_max_score() {
		return cmt_max_score;
	}

	public void setCmt_max_score(float cmt_max_score) {
		this.cmt_max_score = cmt_max_score;
	}

	/**
	 * @param con
	 * @return
	 */
	private void getMtvCosId(Connection con) throws SQLException {
        // TODO Auto-generated method stub
        String sql = "select mtv_cos_id from MeasurementEvaluation " +                "where mtv_ent_id = ? and mtv_cmt_id = ? and mtv_tkh_id = ? " ;
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setLong(1,mtv_ent_id);
        pstmt.setLong(2,mtv_cmt_id);
        pstmt.setLong(3,mtv_tkh_id);
        ResultSet rs = pstmt.executeQuery();
        if(rs.next()){
            mtv_cos_id = rs.getLong(1);
        }
        pstmt.close();
	}

    public void updScore(Connection con,boolean has_score,loginProfile prof) throws SQLException, cwException, cwSysMessage, qdbException, qdbErrMessage{      
        mtv_tkh_id = aeApplication.getTkhId(con,app_id);
        boolean exist_mtv = DbMeasurementEvaluation.exist(con,mtv_ent_id,mtv_cmt_id,mtv_tkh_id);
        if(exist_mtv){
            updScore(con, has_score);
        }else{
           // mtv_tkh_id = aeApplication.getTkhId(con,app_id);//get the tkh_id from aeApplication
            mtv_cos_id = dbCourse.getCosResId(con,DbCourseCriteria.getCcrItmIdByCmtId(con,mtv_cmt_id));
            setMtv_cos_id(mtv_cos_id);
            Timestamp now = dbUtils.getTime(con);
            setMtv_create_timestamp(now);
            setMtv_update_timestamp(now);
            setMtv_create_usr_id(prof.usr_id);
            setMtv_update_usr_id(prof.usr_id);
            ins(con,has_score);
            updStatus(con, has_score);
        }
        getMtvCosId(con);
        long app_id = aeApplication.getAppIdByTkhId(con,mtv_tkh_id);
        CourseCriteria.setAttendOhter(con, prof, mtv_cos_id, mtv_ent_id, mtv_tkh_id, app_id, true, false, false, false);
    }
    
	public void updScore(Connection con,boolean has_score) throws SQLException, qdbException{
		PreparedStatement pstmt = con.prepareStatement(UPD_SCORE);
        if(has_score){
            pstmt.setFloat(1,mtv_score);     
        }else{
            pstmt.setNull(1,java.sql.Types.FLOAT);
        }
        pstmt.setLong(2,mtv_cmt_id);
        pstmt.setLong(3,mtv_ent_id);
        pstmt.setLong(4,mtv_tkh_id);
        pstmt.executeUpdate();
        pstmt.close();
        updStatus(con,has_score);
    }
    public void updScoreByTkhId(Connection con,boolean has_score) throws SQLException, qdbException{
        PreparedStatement pstmt = con.prepareStatement(UPD_SCORE_BY_TKH_ID);
        if(has_score){
            pstmt.setFloat(1,mtv_score);
        }else{
            pstmt.setNull(1,java.sql.Types.DECIMAL);
        }
        pstmt.setTimestamp(2,mtv_update_timestamp);
        pstmt.setString(3,mtv_update_usr_id);
        pstmt.setLong(4,mtv_ent_id);
        pstmt.setLong(5,mtv_cmt_id);
        pstmt.setLong(6,mtv_tkh_id);
        pstmt.executeUpdate();
        pstmt.close();
        return;
    }

    public void updStatus(Connection con, boolean has_score) throws SQLException, qdbException{
        float cmt_pass_score = DbCourseMeasurement.getCmtPassScore(con,mtv_cmt_id);
        String sql = "update MeasurementEvaluation set mtv_status = ? " +            " where mtv_ent_id = ? and mtv_cmt_id = ? and mtv_tkh_id = ? ";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setLong(2,mtv_ent_id);
        pstmt.setLong(3,mtv_cmt_id);
        pstmt.setLong(4,mtv_tkh_id);
        if(has_score){
            if(mtv_score>=cmt_pass_score){
               pstmt.setString(1,"C");             
            }else{
               pstmt.setString(1,"F");
            }
        }else{
            pstmt.setNull(1,java.sql.Types.VARCHAR);
        }
        pstmt.executeUpdate();
        pstmt.close();
    }
    
    public static void resetScore(Connection con,long cmt_id) throws SQLException{
        PreparedStatement pstmt = con.prepareStatement(RESET_SCORE);
        //pstmt.setNull(1,java.sql.Types.FLOAT);
        pstmt.setLong(1,cmt_id);
        pstmt.executeUpdate();
        pstmt.close();
        return;
    }
    
    /**
     * 重置某个计分项目中学员的分数信息
     * @param con
     * @param cmt_id
     * @param prof
     * @throws SQLException
     * @throws cwException
     * @throws cwSysMessage
     * @throws qdbException
     * @throws qdbErrMessage 
     */
    public void resetScore(Connection con, long cmt_id, loginProfile prof)
    	throws SQLException, cwException, cwSysMessage, qdbException, qdbErrMessage {
    	
		Vector usrVec = this.getUsrInfByCmtId(con, cmt_id);// 获取该计分项目中对应的学员集
		for (Iterator iter = usrVec.iterator(); iter.hasNext();) {
			HashMap usrMap = (HashMap)iter.next();
			
			this.setMtv_ent_id(((Long)usrMap.get("usr_ent_id")).longValue());
			this.setMtv_cmt_id(((Long)usrMap.get("cmt_id")).longValue());
			this.setMtv_tkh_id(((Long)usrMap.get("mtv_tkh_id")).longValue());
			this.setMtv_score(((Float)usrMap.get("mtv_score")).floatValue());
			this.app_id = ((Long)usrMap.get("app_id")).longValue();
			
			// 重置
			this.updScore(con, false, prof);
		}
    }

	/**
	 * @param con
	 * @param app_id
	 * @return
	 */
	public static boolean exist(Connection con,long mtv_ent_id,long mtv_cmt_id,long mtv_tkh_id) throws SQLException {
		// TODO Auto-generated method stub
        boolean exist = false;
        String sql = "select mtv_cmt_id from MeasurementEvaluation where " +            "mtv_ent_id = ? and mtv_cmt_id = ? and mtv_tkh_id = ?" ;        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setLong(1,mtv_ent_id);
        pstmt.setLong(2,mtv_cmt_id);
        pstmt.setLong(3,mtv_tkh_id);
        ResultSet rs = pstmt.executeQuery();
        if(rs.next()){
           exist = true;
        }
        pstmt.close();
        return exist;
    }
    public boolean checkEvalByCmtId(Connection con, long cmt_id) throws SQLException{
    	PreparedStatement stmt = con.prepareStatement(CHECK_EVAL_BY_CMT_ID);
    	ResultSet rs = null;
    	stmt.setLong(1,cmt_id);
    	rs = stmt.executeQuery();
		if (rs.next()){
			return true;    	
		} else return false;
    }
    public void delByCmtId(Connection con, long cmt_id) throws SQLException {
    	PreparedStatement stmt = con.prepareStatement(DEL_BY_CMT_ID);
    	stmt.setLong(1,cmt_id);
    	stmt.executeUpdate();
    	stmt.close();
    	return;
    }
    
    public Vector getUsrInfByCmtId(Connection con, long cmt_id) 
		throws SQLException {
	
		Vector usrVec = new Vector();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			String course_type = DbCourseMeasurement.getCourseTypeByCmtId(con, cmt_id);
	        long itm_id = DbCourseCriteria.getCcrItmIdByCmtId(con, cmt_id);
			if (course_type.equals("online")) {
				pstmt = con.prepareStatement(OuterJoinSqlStatements.getOnlineEvalItem(itm_id));
			} else {
				pstmt = con.prepareStatement(OuterJoinSqlStatements.getOfflineEvalItem(itm_id));
			}
			
			pstmt.setLong(1, cmt_id);
			rs = pstmt.executeQuery();
			 
			while (rs.next()) {
				HashMap usrMap = new HashMap();
				
				usrMap.put("app_id", new Long(rs.getLong("app_id")));
				usrMap.put("cmt_id", new Long(cmt_id));
				usrMap.put("usr_ent_id", new Long(rs.getLong("usr_ent_id")));
				usrMap.put("mtv_score", new Float(rs.getFloat("mtv_score")));
				usrMap.put("mtv_tkh_id", new Long(rs.getLong("mtv_tkh_id")));
				
				usrVec.addElement(usrMap);
			}
		} finally {
			// 关闭相关的数据库操作
			if (rs != null) {
				rs.close();
				rs = null;
			}
			
			if (pstmt != null) {
				pstmt.close();
				pstmt = null;
			}
		}
		
		return usrVec;
	}
    
}
