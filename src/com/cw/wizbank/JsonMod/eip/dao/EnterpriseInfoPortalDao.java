package com.cw.wizbank.JsonMod.eip.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Vector;

import com.cw.wizbank.JsonMod.commonBean.OptionBean;
import com.cw.wizbank.JsonMod.eip.EIPModuleParam;
import com.cw.wizbank.JsonMod.eip.EnterpriseInfoPortal;
import com.cw.wizbank.JsonMod.eip.bean.EnterpriseDynamicPriSetBean;
import com.cw.wizbank.JsonMod.eip.bean.EnterpriseInfoPortalBean;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.db.view.ViewTrainingCenter;
import com.cw.wizbank.qdb.dbEntityRelation;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.util.cwPagination;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;

public class EnterpriseInfoPortalDao {
	
	public static final String STATUS_ON="ON";
	public static final String STATUS_OFF="OFF";
	
//	private static final String GET_EIP_SQL = " select eip_id, eip_code, eip_name, eip_tcr_id, tcr_title, eip_account_num, account_used, eip_status, eip_domain, eip_update_timestamp, usr_display_bil " 
//		 + " from EnterpriseInfoPortal "
//		 + " inner join RegUser on (eip_update_usr_id = usr_id) "
//		 + " inner join tcTrainingCenter on (eip_tcr_id = tcr_id) "
//		 + " left join (select tce_tcr_id, count(distinct ern_child_ent_id) account_used from tcTrainingCenterTargetEntity,EntityRelation " 
//		 		     + " where tce_ent_id = ern_ancestor_ent_id and  ern_type = ? group by tce_tcr_id ) account_stat " 
//		 + " on (tcr_id = tce_tcr_id) ";
	
	private static final String GET_EIP_SQL = "select eip_id, eip_code, eip_name, eip_tcr_id, tcr_title, eip_account_num, account_used, eip_status, eip_domain, eip_update_timestamp, usr_display_bil,peak_count,eip_max_peak_count,eip_live_max_count,eip_live_mode,eip_live_qcloud_secretid,eip_live_qcloud_secretkey "  
			+ " from EnterpriseInfoPortal  inner join RegUser on "
 + " (eip_update_usr_id = usr_id)  inner join tcTrainingCenter on "
 + "  (eip_tcr_id = tcr_id) "
 + "  left join "
 + " (select tce_tcr_id,count(distinct ern_child_ent_id) account_used "
 + " from tcTrainingCenterTargetEntity,EntityRelation"
 + "  where tce_ent_id = ern_ancestor_ent_id and  ern_type = ? group by tce_tcr_id ) account_stat  on (tcr_id = tce_tcr_id)  "
 + " left join "
 + "  (select COUNT(cau_eip_tcr_id) peak_count,cau_eip_tcr_id from currentActiveUser group by cau_eip_tcr_id) currentActiveUser "
 + " on (cau_eip_tcr_id = eip_tcr_id)";
	
	/**
	 * 分页返回所有企业
	 * @param con
	 * @param cwPage
	 * @return
	 * @throws SQLException
	 */
	public static Vector getAllEIP(Connection con, cwPagination cwPage) throws SQLException{
		Vector eipVec = new Vector();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		if (cwPage.sortCol == null || cwPage.sortCol.length() == 0) {
			cwPage.sortCol = "eip_update_timestamp";
		}
		if (cwPage.sortOrder == null || cwPage.sortOrder.length() == 0) {
			cwPage.sortOrder = "desc";
		}
		String sql = GET_EIP_SQL;

	    sql += " order by " + cwPage.sortCol + " " + cwPage.sortOrder;
	    try {
		    stmt = con.prepareStatement(sql);
		    int index = 1; 
		    stmt.setString(index, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
		    rs = stmt.executeQuery();
		    int count = 0;
		    while(rs.next()){
		    	if(count >= ((cwPage.curPage-1) * cwPage.pageSize) && count < (cwPage.curPage * cwPage.pageSize) ) {
			    	EnterpriseInfoPortalBean eipBean = new EnterpriseInfoPortalBean();
			    	setEIPBean(eipBean, rs);
			    	eipVec.add(eipBean);
		    	}
		    	count++;
		    }
		    cwPage.totalRec = count;
		    cwPage.totalPage = (int)Math.ceil((float)cwPage.totalRec/(float)cwPage.pageSize);
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return eipVec;
	}
	
	/**
	 * 根据企业ID获取企业信息
	 * @param con
	 * @param eip_id
	 * @return
	 * @throws SQLException
	 */
	public static EnterpriseInfoPortalBean getEIPByID(Connection con, long eip_id) throws SQLException {
		EnterpriseInfoPortalBean eipBean = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
	    try {
		    stmt = con.prepareStatement(GET_EIP_SQL + " where eip_id = ? ");
		    int index = 1; 
		    stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
		    stmt.setLong(index++, eip_id);
		    rs = stmt.executeQuery();
		    if(rs.next()){
		    	eipBean = new EnterpriseInfoPortalBean();
		    	setEIPBean(eipBean, rs);
		    }
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return eipBean;
	}
	
	/**
	 * 赋值于企业BEAN
	 * @param eipBean
	 * @param rs
	 * @throws SQLException
	 */
	private static void setEIPBean(EnterpriseInfoPortalBean eipBean, ResultSet rs) throws SQLException{
		eipBean.setEip_id(rs.getLong("eip_id"));
    	eipBean.setEip_code(rs.getString("eip_code"));
    	eipBean.setEip_name(rs.getString("eip_name"));
    	eipBean.setEip_tcr_id(rs.getLong("eip_tcr_id"));
    	eipBean.setTcr_title(rs.getString("tcr_title"));
    	eipBean.setEip_account_num(rs.getLong("eip_account_num"));
    	eipBean.setAccount_used(rs.getLong("account_used"));
    	eipBean.setEip_status(rs.getString("eip_status"));
    	eipBean.setEip_domain(rs.getString("eip_domain"));
    	eipBean.setEip_update_display_bil(rs.getString("usr_display_bil"));
    	eipBean.setEip_update_timestamp(rs.getTimestamp("eip_update_timestamp"));
    	eipBean.setPeak_count(rs.getLong("peak_count"));
    	eipBean.setEip_max_peak_count(rs.getLong("eip_max_peak_count"));
    	eipBean.setEip_live_max_count(rs.getLong("eip_live_max_count"));
    	eipBean.setEip_live_mode(rs.getString("eip_live_mode"));
    	eipBean.setEip_live_qcloud_secretid(rs.getString("eip_live_qcloud_secretid"));
    	eipBean.setEip_live_qcloud_secretkey(rs.getString("eip_live_qcloud_secretkey"));
    	//eipBean.setEip_price(rs.getString("eip_price"));
	}
	
	/**
	 * 插入新的企业记录，返回企业自增ID
	 * @param con
	 * @param modParam
	 * @param usr_id
	 * @return
	 * @throws SQLException
	 */
	public static long ins(Connection con, EIPModuleParam modParam, String usr_id) throws SQLException{
		Timestamp cur_time = modParam.getCur_time();
		PreparedStatement stmt = null;
		String sql = " insert into EnterpriseInfoPortal (eip_code, eip_name, eip_tcr_id, eip_account_num, eip_status, eip_domain,  eip_create_timestamp, eip_create_usr_id, eip_update_timestamp, eip_update_usr_id,eip_max_peak_count,eip_live_max_count,eip_live_mode,eip_live_qcloud_secretid,eip_live_qcloud_secretkey) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
        try {
			stmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			int index = 1;
			stmt.setString(index++, modParam.getEip_code());
			stmt.setString(index++, modParam.getEip_name());
			stmt.setLong(index++, modParam.getEip_tcr_id());
			stmt.setLong(index++, modParam.getEip_account_num());
			stmt.setString(index++, modParam.getEip_status());
			if (modParam.getEip_domain() != null){
	        	stmt.setString(index++, modParam.getEip_domain());
	        }else{
	        	stmt.setNull(index++, java.sql.Types.VARCHAR);
	        }
			stmt.setTimestamp(index++, cur_time);
			stmt.setString(index++, usr_id);
			stmt.setTimestamp(index++, cur_time);
			stmt.setString(index++, usr_id);
			stmt.setLong(index++, modParam.getEip_max_peak_count());
			stmt.setLong(index++, modParam.getEip_live_max_count());
			stmt.setString(index++, modParam.getEip_live_mode());
			stmt.setString(index++, modParam.getEip_live_qcloud_secretid());
			stmt.setString(index++, modParam.getEip_live_qcloud_secretkey());
			
			stmt.execute();
			
			long eipId = cwSQL.getAutoId(con, stmt, "EnterpriseInfoPortal", "eip_id");
			
			return eipId;
        } finally {
        	cwSQL.closePreparedStatement(stmt);
		}
	}
	
	/**
	 * 更新企业信息
	 * @param con
	 * @param modParam
	 * @param usr_id
	 * @throws SQLException
	 */
	public static void upd(Connection con, EIPModuleParam modParam, String usr_id) throws SQLException{
		Timestamp cur_time = modParam.getCur_time();
		PreparedStatement stmt = null;
		String sql = " update EnterpriseInfoPortal set eip_code = ? "
												   + ", eip_name = ? "
												   + ", eip_tcr_id = ? "
												   + ", eip_account_num = ? "
												   + ", eip_status = ? "
												   + ", eip_domain = ? "
												   + ", eip_update_timestamp = ? "
												   + ", eip_update_usr_id = ? "
												   + ", eip_max_peak_count = ? "
												   + ",eip_live_max_count = ? "
												   + ",eip_live_mode = ? "
												   + ",eip_live_qcloud_secretid = ? "
												   + ",eip_live_qcloud_secretkey = ? ";
		
		sql += " where eip_id = ? ";
        try {
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setString(index++, modParam.getEip_code());
			stmt.setString(index++, modParam.getEip_name());
			stmt.setLong(index++, modParam.getEip_tcr_id());
			stmt.setLong(index++, modParam.getEip_account_num());
			stmt.setString(index++, modParam.getEip_status());
			if(modParam.getEip_domain() != null){
	        	stmt.setString(index++, modParam.getEip_domain());
	        }else{
	        	stmt.setNull(index++, java.sql.Types.VARCHAR);
	        }		
			stmt.setTimestamp(index++, cur_time);
			stmt.setString(index++, usr_id);
			stmt.setLong(index++, modParam.getEip_max_peak_count());
			stmt.setLong(index++, modParam.getEip_live_max_count());
			stmt.setString(index++, modParam.getEip_live_mode());
			stmt.setString(index++, modParam.getEip_live_qcloud_secretid());
			stmt.setString(index++, modParam.getEip_live_qcloud_secretkey());
			stmt.setLong(index++, modParam.getEip_id());
			stmt.execute();
        } finally {
        	cwSQL.closePreparedStatement(stmt);
		}
	}
	
	/**
	 * 检查更新时间， 看是否已经被人修改过
	 * @param con
	 * @param eipId
	 * @param upd_time
	 * @throws cwSysMessage
	 * @throws SQLException
	 */
	public static void checkTimeStamp(Connection con, long eipId, Timestamp upd_time) throws cwSysMessage, SQLException
	{
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
	        stmt = con.prepareStatement(" select eip_update_timestamp from EnterpriseInfoPortal where eip_id = ? " );
	        stmt.setLong(1, eipId);
	        rs = stmt.executeQuery();
	        boolean bTSOk = false;
	        if(rs.next())
	        {
	            Timestamp tTmp = rs.getTimestamp(1);
	            if (upd_time == null) {
	                bTSOk = false;
	            }else {
	                tTmp.setNanos(upd_time.getNanos());
	                if(tTmp.equals(upd_time))
	                    bTSOk = true;
	            }
	        }
	
	        if(!bTSOk) {
	            con.rollback();
	            throw new cwSysMessage(EnterpriseInfoPortal.EIP_UPD_FAIL);
	        }
		}finally {
			cwSQL.cleanUp(rs, stmt);	
		}
	}
	
	private static final String SQL_SET_STATUS = "update EnterpriseInfoPortal set eip_status = ?, eip_update_timestamp = ?, eip_update_usr_id = ? where eip_id = ?";
	/**
	 * 发布或取消发布企业
	 * @param con
	 * @param modParam
	 * @param usr_id
	 * @throws SQLException
	 */
	public static void setStatus(Connection con, EIPModuleParam modParam, String usr_id) throws SQLException{
		Timestamp cur_time = modParam.getCur_time();
		PreparedStatement stmt = null;
        try {
			stmt = con.prepareStatement(SQL_SET_STATUS);
			int index = 1;
			stmt.setString(index++, modParam.getEip_status());
			stmt.setTimestamp(index++, cur_time);
			stmt.setString(index++, usr_id);
			stmt.setLong(index++, modParam.getEip_id());
			stmt.execute();
        } finally {
        	cwSQL.closePreparedStatement(stmt);
		}
	}
	
	private static final String SQL_DEL = "delete from EnterpriseInfoPortal where eip_id = ?";
	/**
	 * 删除企业
	 * @param con
	 * @param eip_id
	 * @throws SQLException
	 */
	public static void del(Connection con, long eip_id) throws SQLException{
		PreparedStatement stmt = null;
        try {
			stmt = con.prepareStatement(SQL_DEL);
			stmt.setLong(1, eip_id);
			stmt.execute();
        } finally {
        	cwSQL.closePreparedStatement(stmt);
		}
	}
	
	/**
	 * 检查企业编号是否已经被用
	 * @param con
	 * @param eip_id
	 * @param eip_code
	 * @throws cwSysMessage
	 * @throws SQLException
	 */
	public static void checkEIPCodeExist(Connection con, long eip_id, String eip_code) throws cwSysMessage, SQLException
	{
		boolean isExist = false;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = " select eip_id from EnterpriseInfoPortal where lower(eip_code) = ? ";
		if(eip_id > 0){
			sql += " and eip_id <> ? ";
		}
		try {
	        stmt = con.prepareStatement(sql);
	        stmt.setString(1, eip_code.toLowerCase());
	        if(eip_id > 0){
	        	stmt.setLong(2, eip_id);
			}
	        rs = stmt.executeQuery();	        
	        if(rs.next()){
	        	isExist = true;
	        }
	
	        if(isExist){
	        	throw new cwSysMessage(EnterpriseInfoPortal.EIP_CODE_EXIST);
	        }
		}finally {
			cwSQL.cleanUp(rs, stmt);	
		}
	}
	
	/**
	 * 检查企业名称是否已被使用
	 * @param con
	 * @param eip_id
	 * @param eip_name
	 * @throws cwSysMessage
	 * @throws SQLException
	 */
	public static void checkEIPNameExist(Connection con, long eip_id, String eip_name) throws cwSysMessage, SQLException
	{
		boolean isExist = false;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = " select eip_id from EnterpriseInfoPortal where lower(eip_name) = ? ";
		if(eip_id > 0){
			sql += " and eip_id <> ? ";
		}
		try {
	        stmt = con.prepareStatement(sql);
	        stmt.setString(1, eip_name.toLowerCase());
	        if(eip_id > 0){
	        	stmt.setLong(2, eip_id);
			}
	        rs = stmt.executeQuery();	        
	        if(rs.next()){
	        	isExist = true;
	        }
	
	        if(isExist){
	        	throw new cwSysMessage(EnterpriseInfoPortal.EIP_NAME_EXIST);
	        }
		}finally {
			cwSQL.cleanUp(rs, stmt);	
		}
	}
	
	/**
	 * 检查企业域名是否已被使用
	 * @param con
	 * @param eip_id
	 * @param eip_domain
	 * @throws cwSysMessage
	 * @throws SQLException
	 */
	public static void checkEIPDomainExist(Connection con, long eip_id, String eip_domain) throws cwSysMessage, SQLException
	{
		boolean isExist = false;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = " select eip_id from EnterpriseInfoPortal where lower(eip_domain) = ? ";
		if(eip_id > 0){
			sql += " and eip_id <> ? ";
		}
		try {
	        stmt = con.prepareStatement(sql);
	        stmt.setString(1, eip_domain.toLowerCase());
	        if(eip_id > 0){
	        	stmt.setLong(2, eip_id);
			}
	        rs = stmt.executeQuery();	        
	        if(rs.next()){
	        	isExist = true;
	        }
	
	        if(isExist){
	        	throw new cwSysMessage(EnterpriseInfoPortal.EIP_DOMAIN_EXIST);
	        }
		}finally {
			cwSQL.cleanUp(rs, stmt);	
		}
	}
	
	/**
	 * 检查当前企业的培训中心是否已经被其他企业关联（一个培训中心只能被一个企业关联）
	 * @param con
	 * @param eip_id
	 * @param tcr_id
	 * @throws cwSysMessage
	 * @throws SQLException
	 */
	public static void checkTcrExist(Connection con, long eip_id, long tcr_id) throws cwSysMessage, SQLException
	{
		boolean isExist = false;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = " select eip_id from EnterpriseInfoPortal where eip_tcr_id = ?  ";
		if(eip_id > 0){
			sql += " and eip_id <> ? ";
		}
		try {
	        stmt = con.prepareStatement(sql);
	        stmt.setLong(1, tcr_id);
	        if(eip_id > 0){
	        	stmt.setLong(2, eip_id);
			}
	        rs = stmt.executeQuery();	        
	        if(rs.next()){
	        	isExist = true;
	        }
	
	        if(isExist){
	        	throw new cwSysMessage(EnterpriseInfoPortal.EIP_TCR_EXIST);
	        }
		}finally {
			cwSQL.cleanUp(rs, stmt);	
		}
	}
	
	/**
	 * 根据培训中心查询企业
	 * @param con
	 * @param tcr_id
	 * @return
	 * @throws SQLException
	 */
	public static long getEIPByTcrID(Connection con, long tcr_id) throws SQLException {
		long eip_id = 0;
		PreparedStatement stmt = null;
		ResultSet rs = null;
	    try {
		    stmt = con.prepareStatement(" select eip_id from EnterpriseInfoPortal where eip_tcr_id = ? ");
		    stmt.setLong(1, tcr_id);
		    rs = stmt.executeQuery();
		    if(rs.next()){
		    	eip_id = rs.getLong(1);
		    }
		    return eip_id;
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
	}

	/**
	 * 更新登陆页面背景图片
	 * @param con
	 * @param modParam
	 * @param usr_id
	 * @throws SQLException
	 */
	/**	
	public static void setLoginBg(Connection con, EIPModuleParam modParam, String usr_id) throws SQLException{
		Timestamp cur_time = modParam.getCur_time();
		PreparedStatement stmt = null;
		String sql = " update EnterpriseInfoPortal set eip_update_timestamp = ? "
												   + ", eip_update_usr_id = ? ";
		if(modParam.getLogin_bg_type() > 0){
			sql += ", eip_login_bg = ? ";
		}
		
		if(modParam.getMobile_login_bg_type() > 0){
			sql += ", eip_mobile_login_bg = ? ";
		}
		
		sql += " where eip_id = ? ";
        try {
			stmt = con.prepareStatement(sql);
			int index = 1;	
			stmt.setTimestamp(index++, cur_time);
			stmt.setString(index++, usr_id);
			if(modParam.getLogin_bg_type() > 0){
				if(modParam.getEip_login_bg() != null){
		        	stmt.setString(index++, modParam.getEip_login_bg());
		        }else{
		        	stmt.setNull(index++, java.sql.Types.VARCHAR);
		        }
			}
			if(modParam.getMobile_login_bg_type() > 0){
				if(modParam.getEip_mobile_login_bg() != null){
		        	stmt.setString(index++, modParam.getEip_mobile_login_bg());
		        }else{
		        	stmt.setNull(index++, java.sql.Types.VARCHAR);
		        }
			}
			stmt.setLong(index++, modParam.getEip_id());
			stmt.execute();
        } finally {
        	cwSQL.closePreparedStatement(stmt);
		}
	}
	*/
	/**
	 * 根据用户或者用户组 检查企业的租用帐号数目是否已经达到限制
	 * @param con
	 * @param usg_ent_id
	 * @param usr_ent_id
	 * @param add_num  		用在上传用户时一次导入多个用户到一个用户组中的情况
	 * @throws cwSysMessage
	 * @throws SQLException
	 * @throws qdbErrMessage 
	 */
	public static void checkAccountLimited(Connection con, long usg_ent_id, long usr_ent_id, int add_num) throws SQLException, qdbErrMessage
	{
		boolean over_limited = false;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = " select eip_id, eip_account_num,  count(distinct usr.ern_child_ent_id) account_used from EnterpriseInfoPortal "
				   + " inner join tcTrainingCenterTargetEntity tc_ent on (tc_ent.tce_tcr_id=eip_tcr_id) "
				   + " left join EntityRelation grp on (grp.ern_ancestor_ent_id=tc_ent.tce_ent_id and grp.ern_type = ?) "
				   + " inner join tcTrainingCenterTargetEntity tc on (tc.tce_tcr_id=eip_tcr_id) "
				   + " left join EntityRelation usr "
				   				+ " on( tc.tce_ent_id = usr.ern_ancestor_ent_id and  usr.ern_type = ? ) "
				   + " where grp.ern_child_ent_id = ? or tc_ent.tce_ent_id = ? "
				   //+ " and eip_status = ? "
				   + " group by eip_id, eip_account_num  ";
		try {
	        stmt = con.prepareStatement(sql);
	        int index = 1;
	        if(usg_ent_id > 0){
		        stmt.setString(index++, dbEntityRelation.ERN_TYPE_USG_PARENT_USG);
		        stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
		        stmt.setLong(index++, usg_ent_id);
		        stmt.setLong(index++, usg_ent_id);
	        }else if(usr_ent_id > 0){
	        	stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
		        stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
		        stmt.setLong(index++, usr_ent_id);
		        stmt.setLong(index++, usr_ent_id);
	        }
	        //stmt.setString(index++, STATUS_ON);
	        rs = stmt.executeQuery();	        
	        if(rs.next()){
	        	long account_max = rs.getLong("eip_account_num");
	        	long account_used = rs.getLong("account_used");
	        	if(add_num > 0 || add_num == -1){//add_num为-1是恢复用户的情况，因为判断是在恢复用户之后的， 所以统计使用人数的时候得减一 
	        		account_used += add_num;
	        	}
	        	if(account_used >= account_max){
	        		over_limited = true;
	        	}
	        }
	
	        if(over_limited){
	        	con.rollback();
	        	throw new qdbErrMessage(EnterpriseInfoPortal.EIP_OVER_ACCOUNT_LIMITED);
	        }
		}finally {
			cwSQL.cleanUp(rs, stmt);	
		}
	}
	
	/**
	 * 根据用户组或用户 查询出企业
	 * @param con
	 * @param usg_ent_id
	 * @param usr_ent_id
	 * @return
	 * @throws cwSysMessage
	 * @throws SQLException
	 */
	public static long getEIPByUserOrGroup(Connection con, long usg_ent_id, long usr_ent_id) throws SQLException
	{
		long eip_id = 0;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = " select distinct eip_id, eip_tcr_id from EnterpriseInfoPortal "
				   + " inner join tcTrainingCenterTargetEntity on tce_tcr_id=eip_tcr_id "
				   + " left join EntityRelation on ern_ancestor_ent_id=tce_ent_id and ern_type = ? "
				   + " where ern_child_ent_id = ? or tce_ent_id = ? "
				   ;//+ " and eip_status = ? ";
		try {
	        stmt = con.prepareStatement(sql);
	        int index = 1;
	        if(usg_ent_id > 0){
		        stmt.setString(index++, dbEntityRelation.ERN_TYPE_USG_PARENT_USG);
		        stmt.setLong(index++, usg_ent_id);
		        stmt.setLong(index++, usg_ent_id);
	        }else if(usr_ent_id > 0){
	        	stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
		        stmt.setLong(index++, usr_ent_id);
		        stmt.setLong(index++, usr_ent_id);
	        }
	        //stmt.setString(index++, STATUS_ON);
	        rs = stmt.executeQuery();	        
	        if(rs.next()){
	        	eip_id = rs.getLong("eip_id");
	        }
	
	        return eip_id;
		}finally {
			cwSQL.cleanUp(rs, stmt);	
		}
	}
	

	/*public static void checkEIPUsrIdExist(Connection con, long tcr_id, String usr_id, long usr_ent_id, long site_id) throws qdbErrMessage, SQLException
	{
		boolean isExist = false;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = " select * from reguser " 
				   + " inner join EntityRelation on usr_ent_id = ern_child_ent_id "
				   + " inner join tcTrainingCenterTargetEntity on tce_ent_id=ern_ancestor_ent_id "
				   + " and ern_type = ? "
				   + " and usr_ste_usr_id = ?  "
				   + " and usr_ste_ent_id = ? "
				   + " and usr_status != ? "
				   + " and tce_tcr_id= ?  ";
		if(usr_ent_id > 0){
			sql += " and usr_ent_id <> ? ";
		}
		try {
	        stmt = con.prepareStatement(sql);
	        int index = 1;
	        stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
	        stmt.setString(index++, usr_id);
	        stmt.setLong(index++, site_id);
	        stmt.setString(index++, dbRegUser.USR_STATUS_DELETED);
	        stmt.setLong(index++, tcr_id);
	        if(usr_ent_id > 0){
	        	stmt.setLong(index++, usr_ent_id);
			}
	        rs = stmt.executeQuery();	        
	        if(rs.next()){
	        	isExist = true;
	        }
	
	        if(isExist){
	        	throw new qdbErrMessage(EnterpriseInfoPortal.EIP_USER_ID_EXIST);
	        }
		}finally {
			cwSQL.cleanUp(rs, stmt);	
		}
	}*/
	
	/**
	 * 检查企业用户是否已存在
	 * @param con
	 * @param eip_id  当前用户的企业id
	 * @param usr_id
	 * @param usr_ent_id
	 * @param site_id
	 * @throws qdbErrMessage
	 * @throws SQLException
	 */
	public static void checkEIPUsrIdExist(Connection con, long eip_id,  String usr_id, long usr_ent_id, long site_id) throws qdbErrMessage, SQLException
	{
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = " select usr_ent_id from reguser " 
				   + " where usr_ste_usr_id = ?  "
				   + " and usr_ste_ent_id = ? "
				   + " and usr_status != ? ";
		if(usr_ent_id > 0){
			sql += " and usr_ent_id <> ? ";
		}
		try {
	        stmt = con.prepareStatement(sql);
	        int index = 1;
	        stmt.setString(index++, usr_id);
	        stmt.setLong(index++, site_id);
	        stmt.setString(index++, dbRegUser.USR_STATUS_DELETED);
	        if(usr_ent_id > 0){
	        	stmt.setLong(index++, usr_ent_id);
			}
	        rs = stmt.executeQuery();	        
	        while(rs.next()){
	        	long usrEntId = rs.getLong(1);
	        	long cur_eip_id = getEIPByUserOrGroup(con, 0, usrEntId);
	        	if(cur_eip_id == 0){
	        		//如果用户为非企业用户，则不允许重名
	        		throw new qdbErrMessage("USR001", usr_id);
	        	}else if(cur_eip_id == eip_id){
	        		//如果用户的企业与当前用户的企业一样，则不允许重名
	        		throw new qdbErrMessage(EnterpriseInfoPortal.EIP_USER_ID_EXIST);
	        	}
	        }
		}finally {
			cwSQL.cleanUp(rs, stmt);	
		}
	}
	
	/**
	 * 根据地址栏解析出相应域名所在的企业信息
	 * @param con
	 * @param domain
	 * @return
	 * @throws SQLException
	 */
	public static EnterpriseInfoPortalBean getEIPByDomain(Connection con ,String domain) throws SQLException{
		EnterpriseInfoPortalBean EIPBean = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		String sql = " select eip_id, eip_code ,eip_name  , eip_tcr_id "
				   + " from EnterpriseInfoPortal "
			       + " where eip_domain= ? and eip_status= ? ";
		try{
			stmt = con.prepareStatement(sql);
			stmt.setString(1, domain);
			stmt.setString(2, STATUS_ON);
			rs = stmt.executeQuery();
			if(rs.next()){
				EIPBean = new EnterpriseInfoPortalBean();
				EIPBean.setEip_id(rs.getLong("eip_id"));
				EIPBean.setEip_code(rs.getString("eip_code"));
				EIPBean.setEip_name(rs.getString("eip_name"));
				EIPBean.setEip_tcr_id(rs.getLong("eip_tcr_id"));
			}
			return EIPBean;
		}finally {
			cwSQL.cleanUp(rs, stmt);	
		}
	}
	
	/**
	 * 查询所有企业的培训中心编号与企业名称
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public static Vector getSelectEipAll(Connection con)throws SQLException{
		Vector v = new Vector();
		String SQL = "select eip_tcr_id ,eip_name from enterpriseInfoPortal where eip_status = ?";
		PreparedStatement stmt = con.prepareStatement(SQL);
		stmt.setString(1, STATUS_ON);
		ResultSet rs = stmt.executeQuery();
		while(rs.next()){
			OptionBean option = new OptionBean();
			option.setId(rs.getLong("eip_tcr_id"));
			option.setText(rs.getString("eip_name"));
			v.add(option);
		}
		cwSQL.cleanUp(rs, stmt);
		return v;
	}		
	
	/**
	 * 查出企业信息
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public static Vector getAllEenterprise(Connection con) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Vector vc = new Vector();
		String sql = null;
		try {
			sql = "select eip_id, eip_tcr_id from EnterpriseInfoPortal";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				EnterpriseInfoPortalBean  entBean = new EnterpriseInfoPortalBean ();
				entBean.setEip_id(rs.getLong("eip_id"));
				entBean.setEip_tcr_id(rs.getLong("eip_tcr_id"));
				vc.add(entBean);
			}
		} finally {
			cwSQL.cleanUp(rs, ps);	
		}
		return vc;
	}
	
	/**
	 * 检查租用账户数目是否小于已经存在的账户数目
	 * @param con
	 * @param eip_tcr_id
	 * @param eip_account_num
	 * @throws SQLException
	 * @throws cwSysMessage
	 */
	public static void checkInsertOrUpdateEIPAccountNum(Connection con,long eip_tcr_id,long eip_account_num)throws SQLException,cwSysMessage{
		PreparedStatement stmt = null;
		ResultSet res = null;
		String sql = null;
		try{
			sql = " select count(distinct ern_child_ent_id) account_used "
				+" from tcTrainingCenterTargetEntity,EntityRelation "
				+" where  tce_ent_id = ern_ancestor_ent_id "
				+" and  ern_type = ? "
				+" and tce_tcr_id = ? ";
			stmt = con.prepareStatement(sql);
			stmt.setString(1, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
			stmt.setLong(2, eip_tcr_id);
			res = stmt.executeQuery();
			if(res.next()){
				long account_used = res.getLong("account_used");
				if(eip_account_num < account_used){
					throw new cwSysMessage("ENTERPRISE01");
				}
			}
		}finally{
			cwSQL.cleanUp(res, stmt);
		}
	}
	
	
	public static void updateEipNum(Connection con ,long eip_id,long num) throws SQLException{
		PreparedStatement stmt = null;
		String sql = " update EnterpriseInfoPortal set eip_account_num = ? ";
		sql += " where eip_id = ? ";
        try {
        	int index = 1;
			stmt = con.prepareStatement(sql);
			stmt.setLong(index++, num);
			stmt.setLong(index++, eip_id);
			stmt.execute();
        } finally {
        	cwSQL.closePreparedStatement(stmt);
		}
	}
	
	
	/**
	 * 企业用户信息是否共享
	 * @param con
	 * @param trc_id
	 * @return
	 * @throws SQLException
	 */
	public static EnterpriseDynamicPriSetBean getDynamicPri(Connection con, long trc_id) throws SQLException {
		EnterpriseDynamicPriSetBean eipDpBean = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql ="select dps_tcr_id,dps_share_usr_inf_ind,dps_upd_date_time,dps_upd_usr_id from tcDynamicPriSet where dps_tcr_id = ?";
	    try {
		    stmt = con.prepareStatement(sql);
		    int index = 1; 
		    stmt.setLong(index++, trc_id);
		    rs = stmt.executeQuery();
		    if(rs.next()){
		    	eipDpBean = new EnterpriseDynamicPriSetBean();
		    	setDPBean(eipDpBean, rs);
		    }
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return eipDpBean;
	}
	
	/**
	 * 赋值于企业用户信息共享BEAN
	 * @param eipDpBean
	 * @param rs
	 * @throws SQLException
	 */
	private static void setDPBean(EnterpriseDynamicPriSetBean eipDpBean, ResultSet rs) throws SQLException{
		eipDpBean.setEip_dps_tcr_id(rs.getLong("dps_tcr_id"));
		eipDpBean.setEip_dps_upd_usr_id(rs.getString("dps_upd_usr_id"));
		eipDpBean.setEip_dps_share_usr_inf_ind(rs.getLong("dps_share_usr_inf_ind"));
		eipDpBean.setEip_dps_upd_date_time(rs.getTimestamp("dps_upd_date_time"));
	}
	
	/**
	 * 更新或插入企业用户信息是否共享
	 * @param con
	 * @param modParam
	 * @param usr_id
	 * @throws SQLException
	 */
	public static void ins_updDynamicPr(Connection con, EIPModuleParam modParam, String usr_id,boolean bln) throws SQLException{
		Timestamp cur_time = cwSQL.getTime(con);
		PreparedStatement stmt = null;
		String sql = " update tcDynamicPriSet set dps_upd_date_time = ?, dps_share_usr_inf_ind = ? ,dps_upd_usr_id = ? "
												   + "where dps_tcr_id = ? ";
		if(bln)
			sql="insert into tcDynamicPriSet(dps_tcr_id,dps_share_usr_inf_ind,dps_upd_date_time,dps_upd_usr_id) values(?,?,?,?) ";
		
		
        try {
			stmt = con.prepareStatement(sql);
			
			int index = 1;	
				if(bln){
					stmt.setLong(index++, modParam.getEip_tcr_id());
					stmt.setLong(index++, modParam.getEip_dps_share_usr_inf_ind());
					stmt.setTimestamp(index++, cur_time);
					stmt.setString(index++, usr_id);
				}else{
					stmt.setTimestamp(index++, cur_time);
					stmt.setLong(index++, modParam.getEip_dps_share_usr_inf_ind());
					stmt.setString(index++, usr_id);
					stmt.setLong(index++, modParam.getEip_tcr_id());
				}
				stmt.executeUpdate();
        } finally {
        	cwSQL.closePreparedStatement(stmt);
		}
	}
	
	/**
	 * 企业用户信息是否共享
	 * @param con
	 * @param trc_id
	 * @return
	 * @throws SQLException
	 */
	public Long getEipEmptyUserSize(Connection con, long eip_id) throws SQLException {
		EnterpriseDynamicPriSetBean eipDpBean = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql ="select eip_account_num from EnterpriseInfoPortal where eip_id = ?";
	    try {
		    stmt = con.prepareStatement(sql);
		    int index = 1; 
		    stmt.setLong(index++, eip_id);
		    rs = stmt.executeQuery();
		    if(rs.next()){
		    	return rs.getLong("eip_account_num");
		    }
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return 0l;
	}
	
	public static long getMyeipId(Connection con, long usr_ent_id, WizbiniLoader wizbini) throws SQLException {
        EnterpriseDynamicPriSetBean eipDpBean = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql ="select eip_id from enterpriseInfoPortal where eip_tcr_id in("+ViewTrainingCenter.getLrnFliter( wizbini)+") and eip_status ='ON' order by eip_id";
        try {
            stmt = con.prepareStatement(sql);
            int index = 1; 
            stmt.setLong(index++, usr_ent_id);
            stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
            rs = stmt.executeQuery();
            
            if(rs.next()){
                return rs.getLong("eip_id");
            }
        } finally {
            cwSQL.cleanUp(rs, stmt);
        }
        return 0l;
    }
	
	
	public static long getTcrByRipID(Connection con, long eip_id) throws SQLException {
        long eip_tcr_id = 0;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(" select eip_tcr_id from EnterpriseInfoPortal where eip_id = ? ");
            stmt.setLong(1, eip_id);
            rs = stmt.executeQuery();
            if(rs.next()){
                eip_tcr_id = rs.getLong(1);
            }
            return eip_tcr_id;
        } finally {
            cwSQL.cleanUp(rs, stmt);
        }
    }
	/**
	 *通过培训中心ID取得企业信息 
	 * @param con
	 * @param eip_tcr_id
	 * @return
	 * @throws SQLException
	 */
	public static EnterpriseInfoPortalBean getEipByTcrID(Connection con,long eip_tcr_id)throws SQLException{
		EnterpriseInfoPortalBean eip=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql=GET_EIP_SQL+" where eip_tcr_id=?";
		try{
			ps=con.prepareStatement(sql);
			int index = 1; 
		    ps.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
		    ps.setLong(index++, eip_tcr_id);
			rs=ps.executeQuery();
			if(rs.next()){
				eip=new EnterpriseInfoPortalBean();
				setEIPBean(eip,rs);
			}
		}finally{
			cwSQL.cleanUp(rs, ps);
		}
		return eip;
	}
	
	/**
	 * 检查培训中心是否与企业关联
	 * @param tcr_id
	 */
	public static boolean checkTcrOccupancy(Connection con, long tcr_id) throws cwSysMessage, SQLException {
		boolean isOccupancy = false;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = " select eip_id from EnterpriseInfoPortal where eip_tcr_id = ? ";
		try{
	        stmt = con.prepareStatement(sql);
	        stmt.setLong(1, tcr_id);
	        rs = stmt.executeQuery();	        
	        if(rs.next()){
	        	isOccupancy = true;
	        }
	        return isOccupancy;
		} finally {
			cwSQL.cleanUp(rs, stmt);	
		}
	}
	
	/**
	 * 情况数据后，更新企业信息记录的修改人
	 * @param con
	 * @param usr_id
	 * @param eip_id 企业id
	 */
	public static void updateEmptyDataUser(Connection con, String usr_id, long eip_id) throws SQLException{
		String sql = " update EnterpriseInfoPortal set eip_update_timestamp = ?, eip_update_usr_id = ? where eip_id = ? ";
		Timestamp cur_time = cwSQL.getTime(con);
		PreparedStatement stmt = null;
        try {
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setTimestamp(index++, cur_time);
			stmt.setString(index++, usr_id);
			stmt.setLong(index++, eip_id);
			stmt.execute();
        } finally {
        	cwSQL.closePreparedStatement(stmt);
		}
	}
	
}
