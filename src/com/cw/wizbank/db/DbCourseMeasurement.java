package com.cw.wizbank.db;
/**
 * @author vincent
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
import java.sql.*;
import java.util.HashMap;
import java.util.Vector;
import java.util.List;
import java.util.ArrayList;

import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;


public class DbCourseMeasurement {

	private long cmt_id;
	private String cmt_title;
	private long cmt_ccr_id;
	private long cmt_cmr_id;
	private float cmt_max_score;
	private float cmt_pass_score;
	private String cmt_status;
	private float cmt_contri_rate;
	private boolean cmt_is_contri_by_score;
	private Timestamp cmt_create_timestamp;
	private String cmt_create_usr_id;
	private Timestamp cmt_update_timestamp;
	private String cmt_update_usr_id;
	private Timestamp cmt_delete_timestamp;
	private String cmt_status_desc_option;
    private int cmt_order;
	private long res_id;
	private long cmt_cmt_id_parent ;
    public static final String[] online_cond = new String[]{"IFCP", "IFCP", "CP", "IFCP", "CP", "IFCP", "IFCP", "IFCP"};
    public static final String[] scoring_cond = new String[]{"IFCP", "CP"};

	static final String GET_BY_CMT_ID_SQL =
		"SELECT cmt_id, cmt_title, cmt_ccr_id, cmt_cmr_id, cmt_max_score, cmt_pass_score, cmt_status, cmt_contri_rate , cmt_is_contri_by_score, cmt_create_timestamp, cmt_create_usr_id, cmt_update_timestamp, cmt_update_usr_id, cmt_delete_timestamp, cmt_status_desc_option, cmt_order, cmt_cmt_id_parent "
			+ " FROM CourseMeasurement "
			+ " WHERE cmt_id = ? AND cmt_delete_timestamp is null ";
	static final String INS_SQL =
		" INSERT INTO CourseMeasurement (cmt_title, cmt_ccr_id, cmt_cmr_id, cmt_max_score, cmt_pass_score, cmt_status, cmt_contri_rate , cmt_is_contri_by_score, cmt_create_timestamp, cmt_create_usr_id, cmt_update_timestamp, cmt_update_usr_id, cmt_delete_timestamp, cmt_status_desc_option, cmt_order, cmt_cmt_id_parent) "
			+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
	static final String UPD_SQL =
		" UPDATE CourseMeasurement SET cmt_title = ? , cmt_max_score = ? , cmt_pass_score = ? , cmt_status = ? , cmt_contri_rate = ? , cmt_is_contri_by_score = ?, cmt_update_timestamp = ? , cmt_update_usr_id = ? WHERE cmt_id = ? ";

	static final String UPD_CMT_SQL =
		" UPDATE CourseMeasurement SET cmt_title = ? , cmt_cmr_id = ?, cmt_max_score = ? , cmt_pass_score = ? , cmt_status = ? , cmt_contri_rate = ? , cmt_is_contri_by_score = ?, cmt_update_timestamp = ? , cmt_update_usr_id = ? WHERE cmt_id = ? and cmt_update_timestamp = ?";
		
	static final String DEL_BY_CMT_ID_SQL =
		" DELETE From CourseMeasurement WHERE  cmt_cmt_id_parent = ? or cmt_id = ?" ;

	static final String SOFT_DEL_BY_CMT_ID_SQL =
		" UPDATE CourseMeasurement SET cmt_update_timestamp = ? , cmt_delete_timestamp = ? , cmt_update_usr_id = ? WHERE cmt_id = ? ";

	static final String IS_TITLE_EXIST_SQL = " select cmt_title from CourseMeasurement "
											+" where cmt_ccr_id = ? and cmt_title = ? and cmt_is_contri_by_score = 1 and cmt_delete_timestamp is null";
	static final String GET_CMTIDS_BY_ITMID_SQL = "select * from CourseMeasurement "
												 +" where cmt_ccr_id = ? and cmt_delete_timestamp is null and cmt_is_contri_by_score = 1 order by cmt_id";
    static final String GET_CMT_BY_ITMID_SQL = "select * from CourseMeasurement "
                                                     +" where cmt_ccr_id = ? and cmt_delete_timestamp is null";
                                                     
	static final String CHECK_TITLE_FOR_UPD_SQL = " select cmt_title from CourseMeasurement "
											+" where cmt_ccr_id = ? and cmt_title = ? and cmt_id != ? and cmt_is_contri_by_score = 1 and cmt_delete_timestamp is null";
                                            
	static final String UPD_COS_ONLINE_COND_SQL = "update CourseMeasurement set cmt_cmr_id = ?, cmt_title = ?, cmt_status=?, cmt_update_timestamp=?,cmt_update_usr_id=?,cmt_status_desc_option=? where cmt_id=?";
	static final String UPD_COS_SCORING_COND_SQL = "update CourseMeasurement set cmt_cmr_id = ?, cmt_status=?, cmt_update_timestamp=?, cmt_update_usr_id=?, cmt_status_desc_option=? where cmt_id=?";
	static final String DEL_BY_CCR_ID_SQL = "delete from CourseMeasurement where cmt_ccr_id=?";
    static final String GET_NEXT_CMT_ORDER = "select max(cmt_order)+1 from courseMeasurement where cmt_ccr_id = ? ";
	public DbCourseMeasurement getByCmtId(Connection con,long cmt_id) throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		this.setCmt_id(cmt_id);
		try {
			stmt = con.prepareStatement(GET_BY_CMT_ID_SQL);
			stmt.setLong(1, this.getCmt_id());
			rs = stmt.executeQuery();
			while (rs.next()) {

				this.cmt_id = rs.getLong("cmt_id");
				this.cmt_title = rs.getString("cmt_title");
				this.cmt_ccr_id = rs.getLong("cmt_ccr_id");
				this.cmt_cmr_id = rs.getLong("cmt_cmr_id");
				this.cmt_max_score = rs.getFloat("cmt_max_score");
				this.cmt_pass_score = rs.getFloat("cmt_pass_score");
				this.cmt_status = rs.getString("cmt_status");
				this.cmt_contri_rate = rs.getFloat("cmt_contri_rate");
				this.cmt_is_contri_by_score = rs.getBoolean("cmt_is_contri_by_score");
				this.cmt_create_timestamp =
					rs.getTimestamp("cmt_create_timestamp");
				this.cmt_update_timestamp =
					rs.getTimestamp("cmt_update_timestamp");
				this.cmt_update_usr_id = rs.getString("cmt_update_usr_id");
				this.cmt_create_usr_id = rs.getString("cmt_create_usr_id");
				this.cmt_delete_timestamp =
					rs.getTimestamp("cmt_delete_timestamp");
				this.cmt_status_desc_option = rs.getString("cmt_status_desc_option");
                this.cmt_order = rs.getInt("cmt_order");
                this.cmt_cmt_id_parent = rs.getLong("cmt_cmt_id_parent");
			}
			return this;
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}
	
	
	public DbCourseMeasurement(long id){
		this.cmt_id = id;
	}
	
	public DbCourseMeasurement(){
	}

	public void ins(Connection con) throws SQLException {
		PreparedStatement stmt = con.prepareStatement(INS_SQL, PreparedStatement.RETURN_GENERATED_KEYS);
		PreparedStatement stmt2 = null;
	    ResultSet rs = null;
		int index = 1;
		stmt.setString(index++, this.getCmt_title());
		stmt.setLong(index++, this.getCmt_ccr_id());
		if ( this.getCmt_cmr_id() != 0){
			stmt.setLong(index++, this.getCmt_cmr_id());
		} else {
			stmt.setNull(index++, Types.INTEGER);
		}
		stmt.setFloat(index++, this.getCmt_max_score());
		stmt.setFloat(index++, this.getCmt_pass_score());
		stmt.setString(index++, this.getCmt_status());
		stmt.setFloat(index++, this.getCmt_contri_rate());
		if (this.getCmt_is_contri_by_score())
			stmt.setInt(index++, 1);
		else stmt.setInt(index++, 0);
		stmt.setTimestamp(index++, this.getCmt_create_timestamp());
		stmt.setString(index++, this.getCmt_create_usr_id());
		stmt.setTimestamp(index++, this.getCmt_update_timestamp());
		stmt.setString(index++, this.getCmt_update_usr_id());
		stmt.setTimestamp(index++, this.getCmt_delete_timestamp());    
		if(this.cmt_status_desc_option == null || this.cmt_status_desc_option.equalsIgnoreCase("")){
		    stmt.setNull(index++, Types.VARCHAR);
		}else{
		    stmt.setString(index++, this.getCmt_status_desc_option());
		}       
        if ( this.getCmt_cmr_id() != 0){
			stmt.setLong(index++, this.getCmt_cmr_id());
		} else {
			stmt.setNull(index++, Types.INTEGER);
		}
        if(this.cmt_cmt_id_parent!=0) {
            stmt.setLong(index++, this.cmt_cmt_id_parent);
        } else {
            stmt.setNull(index++, Types.INTEGER);
        }
        
		stmt.executeUpdate();
		cmt_id = cwSQL.getAutoId(con, stmt, "CourseMeasurement", "cmt_id");
		stmt.close();
	}

	public void upd(Connection con) throws SQLException {
		PreparedStatement stmt = con.prepareStatement(UPD_SQL);
		int index = 1;
		stmt.setString(index++, this.getCmt_title());
		stmt.setFloat(index++, this.getCmt_max_score());
		stmt.setFloat(index++, this.getCmt_pass_score());
		stmt.setString(index++, this.getCmt_status());
		stmt.setFloat(index++, this.getCmt_contri_rate());
		stmt.setBoolean(index++, this.getCmt_is_contri_by_score());
		stmt.setTimestamp(index++, this.getCmt_update_timestamp());
		stmt.setString(index++, this.getCmt_update_usr_id());
		stmt.setLong(index++, this.getCmt_id());
		stmt.executeUpdate();
		stmt.close();
	}
	
	public int upd(Connection con, Timestamp upd_timestamp) throws SQLException {
		PreparedStatement stmt = con.prepareStatement(UPD_CMT_SQL);
		int index = 1;
		stmt.setString(index++, this.getCmt_title());
		if (this.getCmt_cmr_id() == 0){
			stmt.setNull(index++, Types.INTEGER);
		} else {
			stmt.setLong(index++, this.getCmt_cmr_id());		
		}
		stmt.setFloat(index++, this.getCmt_max_score());
		stmt.setFloat(index++, this.getCmt_pass_score());
		stmt.setString(index++, this.getCmt_status());
		stmt.setFloat(index++, this.getCmt_contri_rate());
		stmt.setBoolean(index++, this.getCmt_is_contri_by_score());
		stmt.setTimestamp(index++, this.getCmt_update_timestamp());
		stmt.setString(index++, this.getCmt_update_usr_id());
		stmt.setLong(index++, this.getCmt_id());
		stmt.setTimestamp(index++,upd_timestamp);
		int i = stmt.executeUpdate();
		stmt.close();
		return i;
	}

	public void delByCmtId(Connection con) throws SQLException {
		PreparedStatement stmt = con.prepareStatement(DEL_BY_CMT_ID_SQL);
		stmt.setLong(1, this.getCmt_id());
		stmt.setLong(2, this.getCmt_id());
		stmt.executeUpdate();
		stmt.close();
	}

    public static void delByCcrId(Connection con, long ccr_id) throws SQLException {
    	/*String sql = "delete from CourseMeasurement where cmt_cmt_id_parent in (select cmt_id from (select cmt_id From CourseMeasurement WHERE cmt_ccr_id = ? ) tmp )";
    	PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setLong(1, ccr_id);
        stmt.executeUpdate();*/
    	
    	PreparedStatement stmt = con.prepareStatement(DEL_BY_CCR_ID_SQL);
        int index = 1;
        stmt.setLong(index++, ccr_id);
        stmt.executeUpdate();
        stmt.close();
    }
	public void softDelByCmtId(Connection con) throws SQLException {
		PreparedStatement stmt = con.prepareStatement(SOFT_DEL_BY_CMT_ID_SQL);
		int index = 1;
		Timestamp curTime = cwSQL.getTime(con);
		stmt.setTimestamp(index++, curTime);
		stmt.setTimestamp(index++, curTime);
		stmt.setString(index++, this.getCmt_update_usr_id());
		stmt.setLong(index++, this.getCmt_id());
		stmt.executeUpdate();
		stmt.close();
	}
	
	public void updCosOnlineCond(Connection con) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(UPD_COS_ONLINE_COND_SQL);
        int index = 1;
        if (this.getCmt_cmr_id() == 0) {
            stmt.setNull(index++, Types.INTEGER);
        } else {
            stmt.setLong(index++, this.getCmt_cmr_id());
        }
        stmt.setString(index++, this.getCmt_title());
        stmt.setString(index++, this.getCmt_status());
        stmt.setTimestamp(index++, this.getCmt_update_timestamp());
        stmt.setString(index++, this.getCmt_update_usr_id());
        if (this.cmt_status_desc_option == null
                || this.cmt_status_desc_option.equalsIgnoreCase("")) {
            stmt.setNull(index++, Types.VARCHAR);
        } else {
            stmt.setString(index++, this.getCmt_status_desc_option());
        }
        stmt.setLong(index++, this.getCmt_id());
        stmt.executeUpdate();
        stmt.close();
    }
	
	public void updCosScoringCond(Connection con) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(UPD_COS_SCORING_COND_SQL);
        int index = 1;
        if (this.getCmt_cmr_id() == 0) {
            stmt.setNull(index++, Types.INTEGER);
        } else {
            stmt.setLong(index++, this.getCmt_cmr_id());
        }
        stmt.setString(index++, this.getCmt_status());
        stmt.setTimestamp(index++, this.getCmt_update_timestamp());
        stmt.setString(index++, this.getCmt_update_usr_id());
        if (this.cmt_status_desc_option == null
                || this.cmt_status_desc_option.equalsIgnoreCase("")) {
            stmt.setNull(index++, Types.VARCHAR);
        } else {
            stmt.setString(index++, this.getCmt_status_desc_option());
        }
        stmt.setLong(index++, this.getCmt_id());
        stmt.executeUpdate();
        stmt.close();
    }
	
	public boolean isTitleExist(Connection con) throws SQLException {
		PreparedStatement stmt = con.prepareStatement(IS_TITLE_EXIST_SQL);
		int index = 1;
		ResultSet rs = null;
		stmt.setLong(index++, this.getCmt_ccr_id());
		stmt.setString(index++, this.getCmt_title());
		rs = stmt.executeQuery();
		boolean flag = false;
		if (rs.next()){
			flag =true;
		}
		stmt.close();
		return flag;
		
	}
	public long[] getCmtLstByItmId(Connection con, long ccr_id) throws SQLException{
		long[] cmtIds = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			Vector cmtIdsVec = new Vector();
			stmt = con.prepareStatement(GET_CMTIDS_BY_ITMID_SQL);
			stmt.setLong(1,ccr_id);
			rs = stmt.executeQuery();
			while (rs.next()) {
				cmtIdsVec.addElement(new Long(rs.getLong("cmt_id")));
			}
			cmtIds = cwUtils.vec2longArray(cmtIdsVec);
		} finally {
			if(stmt != null){
				stmt.close();
			}
		}
		return cmtIds;
	}
	public boolean checkTitleForUpd(Connection con,String title,long ccr_id, long cmt_id) throws SQLException{
		PreparedStatement stmt = null;
		ResultSet rs = null;
		stmt = con.prepareStatement(CHECK_TITLE_FOR_UPD_SQL);
		stmt.setLong(1,ccr_id);
		stmt.setString(2,title);
		stmt.setLong(3,cmt_id);
		rs = stmt.executeQuery();
		if(rs.next()){
			stmt.close();
			return false;
		} else {
			stmt.close();
			return true;		
		}
	}

    /**
     * 
     * @param con the Database Connection Object
     * @param ccr_id 
     * @param cmtType 0:Online Item;1:Scoring Item
     * @return Vector of DbCourseMeasurement
     * @throws SQLException
     * @throws cwSysMessage
     */
    public static Vector getCmtLstByType(Connection con, long ccr_id, int cmtType) throws SQLException, cwSysMessage{
        Vector cmtVec = new Vector();
        StringBuffer sql = new StringBuffer(DbCourseMeasurement.GET_CMT_BY_ITMID_SQL);
        sql.append(" and cmt_delete_timestamp is null and cmt_cmr_id is null");
        if(cmtType == 0){
           sql.append(" and cmt_is_contri_by_score = 0");
        }else if(cmtType == 1){
            sql.append(" and cmt_is_contri_by_score = 1");
        }
        sql.append(" order by cmt_id");
		PreparedStatement stmt = con.prepareStatement(sql.toString());
		int index = 1;
		stmt.setLong(index++, ccr_id);
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
            DbCourseMeasurement cmt = new DbCourseMeasurement();
            cmt.cmt_id = rs.getLong("cmt_id");
            cmt.cmt_title = rs.getString("cmt_title");
            cmt.cmt_ccr_id = rs.getLong("cmt_ccr_id");
            cmt.cmt_cmr_id = rs.getLong("cmt_cmr_id");
            cmt.cmt_max_score = rs.getFloat("cmt_max_score");
            cmt.cmt_pass_score = rs.getFloat("cmt_pass_score");
            cmt.cmt_status = rs.getString("cmt_status");
            cmt.cmt_contri_rate = rs.getFloat("cmt_contri_rate");
            cmt.cmt_is_contri_by_score = rs.getBoolean("cmt_is_contri_by_score");
            cmt.cmt_create_timestamp = rs.getTimestamp("cmt_create_timestamp");
            cmt.cmt_update_timestamp = rs.getTimestamp("cmt_update_timestamp");
            cmt.cmt_update_usr_id = rs.getString("cmt_update_usr_id");
            cmt.cmt_create_usr_id = rs.getString("cmt_create_usr_id");
            cmt.cmt_delete_timestamp = rs.getTimestamp("cmt_delete_timestamp");
            cmt.cmt_status_desc_option = rs.getString("cmt_status_desc_option");
            cmt.cmt_order = rs.getInt("cmt_order");
            cmt.cmt_cmt_id_parent = rs.getLong("cmt_cmt_id_parent");
            cmtVec.addElement(cmt);
		}
		rs.close();
		stmt.close();
		return cmtVec;
	}
    
    public void setCmt_statusByOption(String option, String[] status){
        if(option != null && (!option.equalsIgnoreCase(""))){
            int index;
            try{
                index = Integer.parseInt(option);
                cmt_status = status[index];
            }catch(NumberFormatException e){
                cmt_status = null;
            }
        }else{
            cmt_status = null;
        }
    }
    
	public long getCmt_ccr_id() {
		return cmt_ccr_id;
	}

	public long getCmt_cmr_id() {
		return cmt_cmr_id;
	}

	public float getCmt_contri_rate() {
		return cmt_contri_rate;
	}

	public Timestamp getCmt_create_timestamp() {
		return cmt_create_timestamp;
	}

	public String getCmt_create_usr_id() {
		return cmt_create_usr_id;
	}
	
	public Timestamp getCmt_delete_timestamp() {
		return cmt_delete_timestamp;
	}

	public long getCmt_id() {
		return cmt_id;
	}

	public boolean getCmt_is_contri_by_score() {
		return cmt_is_contri_by_score;
	}

	public float getCmt_max_score() {
		return cmt_max_score;
	}

	public float getCmt_pass_score() {
		return cmt_pass_score;
	}

	public String getCmt_status() {
		return cmt_status;
	}

	public String getCmt_title() {
		return cmt_title;
	}

	public Timestamp getCmt_update_timestamp() {
		return cmt_update_timestamp;
	}

	public String getCmt_update_usr_id() {
		return cmt_update_usr_id;
	}
	
	
	public long getCmt_cmt_id_parent() {
		return cmt_cmt_id_parent;
	}

	public void setCmt_ccr_id(long ccr_id) {
		cmt_ccr_id = ccr_id;
	}

	public void setCmt_cmr_id(long cmr_id) {
		cmt_cmr_id = cmr_id;
	}
	


	public void setCmt_contri_rate(float rate) {
		cmt_contri_rate = rate;
	}

	public void setCmt_create_timestamp(Timestamp create_timestamp) {
		cmt_create_timestamp = create_timestamp;
	}

	public void setCmt_create_usr_id(String usr_id) {
		cmt_create_usr_id = usr_id;
	}

	public void setCmt_delete_timestamp(Timestamp delete_timestamp) {
		cmt_delete_timestamp = delete_timestamp;
	}

	public void setCmt_id(long id) {
		cmt_id = id;
	}

	public void setCmt_is_contri_by_score(boolean is_contri_by_score) {
		cmt_is_contri_by_score = is_contri_by_score;
	}

	public void setCmt_max_score(float max_score) {
		cmt_max_score = max_score;
	}

	public void setCmt_pass_score(float pass_score) {
		cmt_pass_score = pass_score;
	}

	public void setCmt_status(String status) {
	        cmt_status = status;
	}

	public void setCmt_title(String title) {
		cmt_title = title;
	}

	public void setCmt_update_timestamp(Timestamp update_timestamp) {
		cmt_update_timestamp = update_timestamp;
	}

	public void setCmt_update_usr_id(String update_usr_id) {
		cmt_update_usr_id = update_usr_id;
	}

	public long getRes_id() {
		return res_id;
	}

	public void setRes_id(long l) {
		res_id = l;
	}

	public String getCmt_status_desc_option() {
		return cmt_status_desc_option;
	}

	public void setCmt_status_desc_option(String option) {
		cmt_status_desc_option = option;
	}
    
    public int getCmt_order() {
        return cmt_order;
    }

    public void setCmt_order(int order) {
        cmt_order = order;
    }
    
	public void setCmt_cmt_id_parent(long cmt_id_parent) {
		cmt_cmt_id_parent = cmt_id_parent;
	}
    
	/**
	 * @return
	 */
	public static String getCourseTypeByCmtId(Connection con,long cmt_id) throws SQLException {
        String type = "offline";
        long cmt_ccr_id = 0;
        String sql = "select cmt_cmr_id from CourseMeasurement where cmt_id = ?";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setLong(1,cmt_id);
        ResultSet rs = pstmt.executeQuery();
        if(rs.next()){
            cmt_ccr_id = rs.getLong("cmt_cmr_id");
        }
        if(cmt_ccr_id>0){
            type="online";
        }
        pstmt.close();
        return type;
	}
    public static String getCmtTitleByCmtId(Connection con ,long cmt_id) throws SQLException {
        String cmt_title = null;
        String sql = "select cmt_title from CourseMeasurement where cmt_id = "+cmt_id;
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next()){
            cmt_title = rs.getString(1);
        }
        stmt.close();
        return cmt_title;
    }
    public static long getCmtIdByCmtTitle(Connection con ,String cmt_title,long cmt_ccr_id) throws SQLException {
        long cmt_id = 0;
        String sql = " select cmt_id from CourseMeasurement where cmt_delete_timestamp is null and cmt_is_contri_by_score = 1 and cmt_title = ? and cmt_ccr_id = ? ";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1,cmt_title);
        pstmt.setLong(2,cmt_ccr_id);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            cmt_id = rs.getLong(1);
        }
        pstmt.close();
        return cmt_id;
    }

	/**
	 * @param con
	 * @param mtv_cmt_id
	 * @return
	 */
	public static float getCmtMaxScore(Connection con, long mtv_cmt_id) throws SQLException {
		// TODO Auto-generated method stub
        float cmt_max_score = 0;
        String sql = " select cmt_max_score from CourseMeasurement where cmt_id = ? ";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setLong(1,mtv_cmt_id);
        ResultSet rs = pstmt.executeQuery();
        if(rs.next()){
            cmt_max_score = rs.getFloat(1);
        }
        pstmt.close();
        return cmt_max_score;
	}

	public static float getCmtPassScore(Connection con, long mtv_cmt_id) throws SQLException {
		// TODO Auto-generated method stub
		float cmt_pass_score = 0;
		String sql = " select cmt_pass_score from CourseMeasurement where cmt_id = ? ";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setLong(1,mtv_cmt_id);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()){
			cmt_pass_score = rs.getFloat(1);
		}
		pstmt.close();
		return cmt_pass_score;
	}
    
    private static int getNextOrder(Connection con, long ccr_id) throws SQLException {
        int nextOrder = 1;
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(GET_NEXT_CMT_ORDER);
            stmt.setLong(1, ccr_id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                nextOrder = rs.getInt(1);
                if(rs.wasNull()) {
                    nextOrder = 1;
                }
            }
        }finally {
            if(stmt!=null) stmt.close();
        }
        return nextOrder;
    }
    public List getChCmtIdList(Connection con, long cmt_id)throws SQLException {
    	List ch_id_list = new ArrayList();
    	String sql = " select cmt_id from CourseMeasurement where cmt_cmt_id_parent = ? ";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setLong(1,cmt_id);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()){
			ch_id_list.add(new Long(rs.getLong("cmt_id")));
		}
		pstmt.close();
		return ch_id_list;
    	
    }
    
    public long getChCmtIdByCcrIDCmtParentID(Connection con, long ccr_id, long parent_cmt_id)throws SQLException {
    	long ch_id = 0;
    	String sql = " select cmt_id from CourseMeasurement where cmt_ccr_id = ? and cmt_cmt_id_parent = ?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setLong(1,ccr_id);
		pstmt.setLong(2,parent_cmt_id);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()){
			ch_id=rs.getLong("cmt_id");
		}
		pstmt.close();
		return ch_id;
    	
     }
    public void copyCmt2ChItm(Connection con, long parent_ccr_id, long ccr_id, HashMap cmr_id_map,Timestamp curTime)throws SQLException , cwSysMessage{
		Vector all_cmt = getCmtLstByCcrID( con,  parent_ccr_id);
		for (int i = 0; i<all_cmt.size(); i++) {
			DbCourseMeasurement cmt = (DbCourseMeasurement)all_cmt.get(i);
			cmt.cmt_ccr_id = ccr_id;
			cmt.cmt_cmt_id_parent = cmt.cmt_id;
            long cmr_id = 0;
            if(cmt.cmt_cmr_id > 0) {
                cmr_id = ((Long)cmr_id_map.get(new Long(cmt.cmt_cmr_id))).longValue();
            }
            cmt.cmt_cmr_id = cmr_id;
			cmt.cmt_update_timestamp = curTime;
			cmt.cmt_create_timestamp = curTime;
			cmt.ins( con);
		}
        /*
		all_cmt = getCmtLstByType( con,  parent_ccr_id,  1);
		for (int i = 0; i<all_cmt.size(); i++) {
			DbCourseMeasurement cmt = (DbCourseMeasurement)all_cmt.get(i);
			cmt.cmt_ccr_id = ccr_id;
			cmt.cmt_update_timestamp = curTime;
			cmt.cmt_create_timestamp = curTime;
			cmt.cmt_cmt_id_parent = cmt.cmt_id;
			cmt.ins( con);
		}
        */
    }
    
    public static Vector getCmtLstByCcrID(Connection con, long ccr_id) throws SQLException, cwSysMessage{
        Vector cmtVec = new Vector();
        StringBuffer sql = new StringBuffer(DbCourseMeasurement.GET_CMT_BY_ITMID_SQL);
       
        sql.append(" order by cmt_id");
        PreparedStatement stmt = con.prepareStatement(sql.toString());
        int index = 1;
        stmt.setLong(index++, ccr_id);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            DbCourseMeasurement cmt = new DbCourseMeasurement();
            cmt.cmt_id = rs.getLong("cmt_id");
            cmt.cmt_title = rs.getString("cmt_title");
            cmt.cmt_ccr_id = rs.getLong("cmt_ccr_id");
            cmt.cmt_cmr_id = rs.getLong("cmt_cmr_id");
            cmt.cmt_max_score = rs.getFloat("cmt_max_score");
            cmt.cmt_pass_score = rs.getFloat("cmt_pass_score");
            cmt.cmt_status = rs.getString("cmt_status");
            cmt.cmt_contri_rate = rs.getFloat("cmt_contri_rate");
            cmt.cmt_is_contri_by_score = rs.getBoolean("cmt_is_contri_by_score");
            cmt.cmt_create_timestamp = rs.getTimestamp("cmt_create_timestamp");
            cmt.cmt_update_timestamp = rs.getTimestamp("cmt_update_timestamp");
            cmt.cmt_update_usr_id = rs.getString("cmt_update_usr_id");
            cmt.cmt_create_usr_id = rs.getString("cmt_create_usr_id");
            cmt.cmt_delete_timestamp = rs.getTimestamp("cmt_delete_timestamp");
            cmt.cmt_status_desc_option = rs.getString("cmt_status_desc_option");
            cmt.cmt_order = rs.getInt("cmt_order");
            cmt.cmt_cmt_id_parent = rs.getLong("cmt_cmt_id_parent");
            cmtVec.addElement(cmt);
        }
        rs.close();
        stmt.close();
        return cmtVec;
    }
    public Timestamp getLastUpdTimestamp(Connection con, long cmt_id) throws SQLException{
        String sql = "select cmt_update_timestamp from CourseMeasurement where cmt_id = ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        Timestamp upd_timestamp = null;
        int index = 1;
        stmt.setLong(index++,cmt_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()){
            upd_timestamp = rs.getTimestamp("cmt_update_timestamp");
        }
        rs.close();
        stmt.close();
        return upd_timestamp;
    }
    
    
}