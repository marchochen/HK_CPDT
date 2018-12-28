package com.cw.wizbank.qdb;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import com.cw.wizbank.Dispatcher;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.codetable.CodeTable;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.db.DbTrainingCenter;
import com.cw.wizbank.db.view.ViewTrainingCenter;
import com.cw.wizbank.trainingcenter.TrainingCenter;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwPagination;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.utils.CommonLog;

public class dbMessage
{
    public static final boolean MSG_POST_SUCCESS = true;
    public static final String MSG_STATUS_ON = "ON";
    public static final String MSG_STATUS_OFF = "OFF";
    public static final String MSG_TYPE_SYS = "SYS";
    public static final String MSG_TYPE_RES = "RES";
    public static final String MSG_TYPE_PUB = "PUB";
    public static final String MSG_TYPE_JOB = "JOB";


    public static final String INVALID_TIMESTAMP_MSG = "MSG001";    //"The record was updated by other user.";
    public static final String NO_RIGHT_DELETE_MSG = "MSG002";    //You don't have permission to delete the record.
    public static final String NO_RIGHT_INSERT_MSG = "MSG003";    //You don't have permission to insert the record.
    public static final String NO_RIGHT_UPDATE_MSG = "MSG004";    //You don't have permission to update the record.
    
    public static final String MSG_CATALOG_MOBILE_EXSITS = "msg_catalog_mobile_exsits";    // 移动课程目录已存在
    public static final String MSG_CATALOG_MOBILE_UPD_FAILED = "msg_catalog_mobile_upd_failed";    // 更新移动课程目录状态失败

    public static final String MSG_ICON_UPD_REMAIN = "msg_icon_remain";
    public static final String MSG_ICON_UPD_DEL = "msg_icon_del";
    public static final String MSG_ICON_UPD_CHANGE = "msg_icon_change";
    
    /*
    public static final String FCN_MSG_MGT_IN_COS   = "MSG_MGT_IN_COS";
    public static final String FCN_MSG_MGT_SYS      = "MSG_MGT_SYS";
    */
    public static final String MSG_TYPE_ANN = "ann";

    public long msg_id;
    public String msg_usr_id;
    public String msg_type;
    public String msg_title;
    public String msg_icon;			// 公告的缩略图标
    public String msg_icon_result;	// 修改公告时图标的修改结果
    public String msg_body;
    public Timestamp msg_begin_date;
    public Timestamp msg_end_date;
    public String msg_status;
    public long msg_res_id;
    public long msg_root_ent_id;
    public Timestamp msg_upd_date;
    // << BEGIN add "level"
    public String msg_level;
    // >> END
    public long msg_tcr_id;
    public boolean msg_readonly = false;
    
    public HashMap directory_info_map;
    public boolean msg_mobile_ind;
    public boolean msg_receipt;
    public boolean msg_belong_exam_ind;
    public String encrypt_msg_id;

    public dbMessage() {;}


    public static void delAllMsg(Connection con, long id) throws qdbException {
        //delete all the messages under a resource, when that resource is deleted
        try {
            String SQL = "Delete From Message "
                       + "Where msg_res_id  = ? ";

            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, id);
            stmt.executeUpdate();

            stmt.close();
            return;
        }
        catch(SQLException e) {
            throw new qdbException(e.getMessage());
        }
    }

    public void updateTimeStamp(Connection con) throws qdbException {
        try {
            Timestamp curTime = dbUtils.getTime(con);
            String SQL = "Update Message Set msg_upd_date = ? "
                       + "Where msg_id = ? ";

            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setTimestamp(1,curTime);
            stmt.setLong(2, msg_id);
            int stmtResult=stmt.executeUpdate();
            stmt.close();
            if ( stmtResult!=1)                            
            {
                throw new qdbException("Error updating update time of message");
            }
        }
        catch (SQLException e) {
            throw new qdbException(e.getMessage());
        }
    }

    public boolean checkTimeStamp(Connection con) throws qdbException {
        try {
            //assume msg_id has been set up
            boolean result=false;
            Timestamp DB_msg_upd_date;
            String SQL = "Select msg_upd_date From Message "
                       + "Where msg_id = ? ";

            if(msg_upd_date != null) {
                PreparedStatement stmt = con.prepareStatement(SQL);
                stmt.setLong(1, msg_id);
                ResultSet rs = stmt.executeQuery();

                if(rs.next()==true) {
                    DB_msg_upd_date = rs.getTimestamp("msg_upd_date");
                    DB_msg_upd_date.setNanos(msg_upd_date.getNanos());
                    if(DB_msg_upd_date.equals(msg_upd_date))
                        result = true;
                }
                else
                {
                	stmt.close();
                    throw new qdbException ("Cannot read the last update time of message");
                }

                stmt.close();
            }


            return result;
        }
        catch (SQLException e) {
            throw new qdbException(e.getMessage());
        }
    }

    /*
    public String msgAsXML(Connection con, loginProfile prof,
                            boolean default_ind, boolean checkStatus)
        throws qdbException, cwSysMessage {
        String xml = "<?xml version=\"1.0\" encoding=\"" + dbUtils.ENC_UTF + "\" standalone=\"no\" ?>" + dbUtils.NEWL;
        xml += prof.asXML() + dbUtils.NEWL;
        return(xml + msgAsXMLNoHeader(con, prof, default_ind, checkStatus));
    }
    */


    //if default_ind == true, get msg ends after the current DB time and begin before the current DB time
    //else get msg according to input msg_begin_date, msg_end_date
    //only use get all on/off messages if checkStatus is ture
    //else only get on messages
    public String msgAsXMLNoHeader(Connection con, loginProfile prof,
                                    boolean default_ind, boolean checkStatus,
                                    int page, int page_size,
                                    String sortCol, String sortOrder, boolean is_upd)
                                    throws qdbException, cwSysMessage {
        return msgAsXMLNoHeader(con, prof, default_ind, checkStatus, null, page, page_size, sortCol, sortOrder, false, is_upd,null);
    }
    public String msgAsXMLNoHeader(Connection con, loginProfile prof,
                                    boolean default_ind, boolean checkStatus,
                                    String wantStatus,
                                    int page, int page_size,
                                    String sortCol, String sortOrder, boolean is_upd,String title_code)
                                    throws qdbException, cwSysMessage {
        return msgAsXMLNoHeader(con, prof, default_ind, checkStatus, wantStatus, page, page_size, sortCol, sortOrder, true, is_upd,title_code);
    }

    //when login, ftn_ext_ids is the current user's granted privileges which are in 'HOMEPAGE' type
    //otherwise, is null
    
    public String getAnnAsXml(Connection con, loginProfile prof, 
    							int page, int page_size, 
    							String sortCol, String sortOrder, Vector ftn_ext_ids, boolean isMobile, WizbiniLoader wizbini,String title_code)
    							throws SQLException, qdbException {
        
        if (sortCol == null || sortCol.length() == 0) {
            sortCol = " msg_begin_date ";
        }
        if (sortOrder == null || sortOrder.length() == 0) {
            sortOrder = " desc ";
        }
        if (page == 0) {
            page = 1;
        }
        if (page_size == 0) {
            page_size = 10;
        }

    	PreparedStatement stmt = null;
        int start = page_size * (page-1);
        int count = 0;
        
    	try {
    		if (msg_readonly) {
                //get defult tcr_id
            	if (msg_tcr_id == -1) {
            		msg_tcr_id = DbTrainingCenter.getSuperTcId(con, prof.root_ent_id);
            	}
                stmt = getAnnForRead(con, prof, sortCol, sortOrder, ftn_ext_ids, isMobile,  wizbini,title_code);
    		} else {
                //get defult tcr_id
            	if (msg_tcr_id == -1) {
            		msg_tcr_id = ViewTrainingCenter.getDefaultTc(con, prof);
            	}
    			stmt = getAnnForMan(con,prof, sortCol, sortOrder, isMobile,title_code);
    		}
        	ResultSet rs = stmt.executeQuery();
    		
            String xml = null;
            xml += "<message type=\"" + msg_type + "\">";
            if (msg_tcr_id == 0) {
                xml += "<show_all>true</show_all>";
            }
            Timestamp eff_end_date;
            String tmp_end_date;
            while (rs.next()) {
                if (count >= start && count < start+page_size) {
                    eff_end_date = rs.getTimestamp("msg_end_date");
                    tmp_end_date = dbUtils.convertEndDate(eff_end_date);
                    long msg_id = rs.getLong("msg_id");
                    xml += "<item id=\"" + msg_id
                        //+ "\" usr_id=\"" + dbUtils.esc4XML(dbRegUser.usrId2SteUsrId(con,rs.getString("msg_usr_id")))
                        + "\" usr_display_bil=\"" + dbUtils.esc4XML(rs.getString("usr_display_bil"))
                        + "\" type=\"SYS"
                        + "\" begin_date=\"" + rs.getTimestamp("msg_begin_date")
                        + "\" end_date=\"" + tmp_end_date
                        + "\" status=\"" + rs.getString("msg_status")
                        + "\" isReceipt=\"" + rs.getBoolean("msg_receipt")
                        + "\" root_ent_id=\"" + prof.root_ent_id
                        + "\" res_id=\"" + rs.getLong("msg_res_id")
                        + "\" timestamp=\"" + rs.getTimestamp("msg_upd_date")
                        + "\" level=\"" + dbUtils.esc4XML(rs.getString("msg_level"))
                        + "\">" + dbUtils.NEWL
                        + "<title>" + dbUtils.esc4XML(rs.getString("msg_title")) + "</title>";
                    xml += "<body>" + dbUtils.esc4XML(cwSQL.getClobValue(rs, "msg_body")) + "</body>";
                    xml += "<training_center id=\"" + rs.getLong("tcr_id") + "\">";
                    xml += "<title>" + dbUtils.esc4XML(rs.getString("tcr_title")) + "</title>";
                    xml += "</training_center>";
                    xml += "</item>" + dbUtils.NEWL;
                }
                count++;
            }
            cwPagination pagn = new cwPagination();
            pagn.totalRec = count;
            pagn.totalPage = (int)Math.ceil((float)count/page_size);
            pagn.pageSize = page_size;
            pagn.curPage = page;
            pagn.sortCol = cwPagination.esc4SortSql(sortCol);
            pagn.sortOrder = cwPagination.esc4SortSql(sortOrder);
            pagn.ts = null;
            xml += pagn.asXML();
            if(ftn_ext_ids == null) {
            	xml += TrainingCenter.getTcrAsXml(con, msg_tcr_id, prof.root_ent_id);
            }
            xml += "<read_only>" + msg_readonly + "</read_only>";
            xml += "</message>" + dbUtils.NEWL;
            return xml;
    	} finally {
    		if(stmt != null) {
    			stmt.close();
    		}
    	}
    }
    
	String sql_ann 
    	= "SELECT"
        + " msg_id, msg_usr_id, usr_display_bil, msg_type, msg_level, "
        + " msg_title, msg_body, msg_res_id, msg_status, "
        + " msg_begin_date, msg_end_date, msg_upd_date, tcr_id, tcr_title, msg_icon,msg_receipt "
        + "FROM"
        + " Message, RegUser, tcTrainingCenter "
        + "WHERE"
        + " msg_type = 'SYS'"
        + " AND msg_usr_id = usr_id"
	    + " AND msg_tcr_id = tcr_id ";

	private PreparedStatement getAnnForMan(Connection con, loginProfile prof, String sortCol, String sortOrder, boolean isMobile ,String title_code) throws SQLException, qdbException {
		StringBuffer sql = new StringBuffer();
        PreparedStatement stmt = null;
		sql.append(sql_ann);
		//if user has MSG_MGT_SYS privilege, there is no tc limit.
        String search_by_tc = "";
        if (!AccessControlWZB.isRoleTcInd(prof.current_role)) {
        	search_by_tc = "no_filter";
        } else {
        	search_by_tc = "ta_filter";
        }
        
        if(title_code != null && !title_code.equals("")){
			sql.append(" and msg_title like N'%" + title_code + "%' ");
		}

        if (search_by_tc.equalsIgnoreCase("ta_filter")) {
			sql.append(" and msg_tcr_id in (")
			   .append(" select distinct tcr_id ")
			   .append(" from tcTrainingCenter, tcTrainingCenterOfficer left join tcRelation on (tco_tcr_id = tcn_ancestor) ")
			   .append(" where tco_usr_ent_id = ? ")
			   .append(" and (tcn_child_tcr_id = tcr_id or tco_tcr_id = tcr_id) ")
			   .append(" and tcr_status = ? ")
			   .append(")");
		}else{
			sql.append(" and msg_tcr_id in (")
			   .append(" select tcr_id  from tcTrainingCenter where tcr_id = ? or tcr_id in(select tcn_child_tcr_id from tcRelation where tcn_ancestor = ? ) ")
		
			   .append(")");
		}

        if (msg_tcr_id > 0) {
        	sql.append(" and msg_tcr_id = ? ");
        }
        
		sql.append(" AND msg_root_ent_id = ? ")
		   .append(" ORDER BY " + sortCol + " " + sortOrder);
		stmt = con.prepareStatement(sql.toString());
		int index = 1;
		if (search_by_tc.equalsIgnoreCase("ta_filter")) {
			stmt.setLong(index++, prof.usr_ent_id);
			stmt.setString(index++, "OK");
		}else{
			stmt.setLong(index++, prof.my_top_tc_id);
			stmt.setLong(index++, prof.my_top_tc_id);
		}
//		stmt.setBoolean(index++, isMobile);
        if (msg_tcr_id > 0) {
        	stmt.setLong(index++, msg_tcr_id);
        }
		stmt.setLong(index++, prof.root_ent_id);
		return stmt;
	}
	
	/**getAnnForRead兼容方法
	 * @param con
	 * @param prof
	 * @param sortCol
	 * @param sortOrder
	 * @param ftn_ext_ids
	 * @return
	 * @throws qdbException
	 * @throws SQLException
	 */
	public PreparedStatement getAnnForRead(Connection con, loginProfile prof,
			String sortCol, String sortOrder, Vector ftn_ext_ids, WizbiniLoader wizbini,String title_code) throws qdbException, SQLException {
		return getAnnForRead(con,  prof, sortCol,  sortOrder,  ftn_ext_ids, false,  wizbini, title_code) ;
	}
	
	public PreparedStatement getAnnForRead(Connection con, loginProfile prof, String sortCol, String sortOrder, Vector ftn_ext_ids, boolean isMobile, WizbiniLoader wizbini,String title_code) throws qdbException, SQLException {
		StringBuffer sql = new StringBuffer();
        Timestamp curTime = dbUtils.getTime(con);
        PreparedStatement stmt = null;
//        boolean isRoleTcInd = AccessControlWZB.isRoleTcInd(prof.current_role);
        
        String search_by_tc = "lrn_filter";
//        if(AccessControlWZB.isSysAdminRole(prof.current_role)){
//        	search_by_tc = "no_filter";
//        }else{
//        	if(!AccessControlWZB.isRoleTcInd(prof.current_role) && 
//        		(AccessControlWZB.hasRolePrivilege(prof.current_role, AclFunction.FTN_AMD_SYS_MSG_LIST) || 
//        		 AccessControlWZB.hasRolePrivilege(prof.current_role, AclFunction.FTN_AMD_MSG_MAIN))){
//        		search_by_tc = "no_filter";
//        	}else{
//        		search_by_tc = "ta_filter";
//        	}
//        }
        /*
        if(isLrnRole){
        	search_by_tc = "lrn_filter";
        }else if(isRoleTcInd){
        	search_by_tc = "ta_filter";
        }
      */
        /*
         * 
 else if (acWZB.hasUserPrivilege(con, prof.usr_ent_id, prof.current_role, AcMessage.FTN_MSG_MGT_SYS_IN_TCR)) {
        	search_by_tc = "ta_filter";
        }
        */
		sql.append(sql_ann)
           .append(" AND NOT(msg_end_date < ? )")
           .append(" AND NOT(msg_begin_date > ? )")
		   .append(" AND msg_root_ent_id = ? ")
		   .append(" AND msg_status = '" + MSG_STATUS_ON + "'");
//	       .append(" AND (msg_mobile_ind = ? ")
//	    
//		if(!isMobile) {
//	    	//控制平台不能看到手机平台的数据
//	    	sql.append(" or msg_mobile_ind is null ) ");
//	    } else {
//	    	sql.append(" ) ");
//	    }
		  
		/*if (msg_tcr_id > 0) {
			sql.append(" and msg_tcr_id = ? ");
		}*/
		if(title_code != null && !title_code.equals("")){
			sql.append(" and msg_title like N'%" + title_code + "%' ");
		}

		if (search_by_tc.equalsIgnoreCase("ta_filter")) {
			//sql.append(" and msg_tcr_id in (").append(ViewTrainingCenter.ta_fliter).append(")")
			sql.append(" and msg_tcr_id  in ( select tce_tcr_id  from tcTrainingCenterTargetEntity where tce_ent_id in ( select er.ern_ancestor_ent_id from Reguser usr left join entityrelation er on (ern_child_ent_id = usr_ent_id) where ern_type = 'USR_PARENT_USG' and usr.usr_ent_id = ?))");
		} else if (search_by_tc.equalsIgnoreCase("lrn_filter")) {
			sql.append(" and msg_tcr_id in (").append(ViewTrainingCenter.getLrnFliter( wizbini)).append(")");
		}
		
		sql.append(" and msg_tcr_id in (")
		   .append(" select tcr_id  from tcTrainingCenter where tcr_id = ? or tcr_id in(select tcn_child_tcr_id from tcRelation where tcn_ancestor = ? )" )
		   .append(")");
		
		if (sortCol != null && sortCol.length() > 0) {
			sql.append(" ORDER BY " )
			.append(sortCol)
			.append(" ")
			.append(sortOrder);
		} else {
			sql.append(" order by msg_begin_date desc");
		}
		stmt = con.prepareStatement(sql.toString());
		int index = 1;
		stmt.setTimestamp(index++, curTime);
		stmt.setTimestamp(index++, curTime);
		stmt.setLong(index++, prof.root_ent_id);
		//兼容原来的数据
//		stmt.setBoolean(index++, isMobile);
		
		/*if (msg_tcr_id > 0) {
			stmt.setLong(index++, msg_tcr_id);
		}*/
		if (search_by_tc.equalsIgnoreCase("ta_filter")) {
			//stmt.setLong(index++, prof.my_top_tc_id);
			stmt.setLong(index++, prof.usr_ent_id );
		} else if (search_by_tc.equalsIgnoreCase("lrn_filter")) {
			stmt.setLong(index++, prof.usr_ent_id);
			stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
		}
		
		stmt.setLong(index++, prof.my_top_tc_id );
		stmt.setLong(index++, prof.my_top_tc_id );
		

		

		return stmt;
	}


	//if default_ind == true, get msg ends after the current DB time and begin before the current DB time
    //else get msg according to input msg_begin_date, msg_end_date
    //only get on messages if checkStatus is ture
    //else get messages according to wantStatus
    private String msgAsXMLNoHeader(Connection con, loginProfile prof,
                                    boolean default_ind, boolean checkStatus,
                                    String wantStatus,
                                    int page, int page_size,
                                    String sortCol, String sortOrder, boolean hasBody, boolean is_upd,String title_code)
                                    throws qdbException, cwSysMessage {

        Timestamp curTime = dbUtils.getTime(con);
        String xml = "";
        xml += "<message type=\"" + msg_type + "\" ";
        if(default_ind) {
            msg_begin_date = curTime;
            msg_end_date = curTime;
        }
        if(msg_id != 0) {
            xml += ">";
            xml += oneMsgAsXML(con, prof, is_upd,title_code);
        }
        else if (msg_type != null) {
            if (msg_type.equalsIgnoreCase(MSG_TYPE_RES)) {
                xml += "res_id=\"" + msg_res_id + "\"";
            }
            xml += ">";
            xml += this.msgAsXML(con, prof, checkStatus, wantStatus, page, page_size, sortCol, sortOrder, msg_type, hasBody);
        }
        xml += "<cur_time>" + curTime + "</cur_time>" + dbUtils.NEWL;
        xml += "</message>" + dbUtils.NEWL;
        return xml;
    }
    

    public String oneMsgAsXML(Connection con, loginProfile prof, boolean is_upd,String title_code)
        throws qdbException, cwSysMessage {
        try {
            String xml = "";
            String SQL = "";

            SQL = "Select msg_usr_id, usr_display_bil, usr_ste_usr_id, msg_type, msg_title, msg_body"
                + " ,msg_begin_date, msg_end_date, msg_status, msg_res_id, msg_upd_date, msg_level, msg_icon,msg_receipt "
                + " ,tcr_id, tcr_title "
                + " From RegUser, Message "
                + " left join tcTrainingCenter on (msg_tcr_id = tcr_id)"
                + " Where msg_id = ? "
                + " And msg_usr_id = usr_id ";
            
            if(title_code != null && !title_code.equals("")){
            	SQL += " and msg_title like N'%" + title_code + "%' ";
    		}

            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, msg_id);
            ResultSet rs = stmt.executeQuery();
            Timestamp eff_end_date;
            String tmp_end_date;

            if(rs.next() == true) {

                eff_end_date = rs.getTimestamp("msg_end_date");
                tmp_end_date = dbUtils.convertEndDate(eff_end_date);
                
                String msg_type = rs.getString("msg_type");
                xml += "<item id=\"" + msg_id
                        + "\" usr_id=\"" + rs.getString("usr_ste_usr_id")
                        + "\" usr_display_bil=\"" + dbUtils.esc4XML(rs.getString("usr_display_bil"))
                        + "\" type=\"" + msg_type
                        + "\" begin_date=\"" + rs.getTimestamp("msg_begin_date")
                        + "\" end_date=\"" + tmp_end_date
                        + "\" status=\"" + rs.getString("msg_status")
                        + "\" isReceipt=\"" + rs.getBoolean("msg_receipt")
                        + "\" root_ent_id=\"" + prof.root_ent_id
                        + "\" res_id=\"" + rs.getLong("msg_res_id")
                        + "\" timestamp=\"" + rs.getTimestamp("msg_upd_date")
                        + "\" level=\"" + dbUtils.esc4XML(rs.getString("msg_level"))
                        + "\" msg_icon=\"" + dbUtils.esc4XML(rs.getString("msg_icon"))
                        + "\">" + dbUtils.NEWL
                        + "<title>" + dbUtils.esc4XML(rs.getString("msg_title")) + "</title>";
                
                 xml += "<body>" + dbUtils.esc4XML(cwSQL.getClobValue(rs, "msg_body")) + "</body>";
                
                if (msg_type.equalsIgnoreCase(this.MSG_TYPE_SYS)) {
                	xml += "<training_center id=\"" + rs.getLong("tcr_id") + "\">"
	                    + "<title>" + dbUtils.esc4XML(rs.getString("tcr_title")) + "</title>"
	                    + "</training_center>" + dbUtils.NEWL;
                }
                xml += "</item>" + dbUtils.NEWL;
                xml += this.getMsgLevelLst(con) + dbUtils.NEWL;
            }
            else {
            	stmt.close();
                //throw new qdbException ("Cannot find message. id = " + msg_id);
                throw new cwSysMessage(dbUtils.MSG_REC_NOT_FOUND, "Message ID = " + msg_id);
            }
            stmt.close();
            return xml;
        }
        catch (SQLException e) {
            throw new qdbException(e.getMessage());
        }
    }

    public void delMsg(Connection con, loginProfile prof)
      throws qdbException, qdbErrMessage , cwSysMessage{

        /*
        if(!(checkPermission(con, prof))) {
            throw new qdbErrMessage(NO_RIGHT_DELETE_MSG);
        }
        */

        if(!(checkTimeStamp(con)))
            throw new qdbErrMessage(INVALID_TIMESTAMP_MSG);

        del(con);
        
        //delete file of announ
    	String msgDirAbsPath = Dispatcher.getWizbini().getFileUploadAnnDirAbs() + cwUtils.SLASH + msg_id;
    	dbUtils.delDir(msgDirAbsPath);
    }

    public void delMsgList(Connection con, WizbiniLoader wizbini, loginProfile prof, String[] msg_id_lst)
      throws qdbException, qdbErrMessage , cwSysMessage, SQLException{

        /*
        if(!(checkPermission(con, prof))) {
            throw new qdbErrMessage(NO_RIGHT_DELETE_MSG);
        }
        */

        Vector undel_lst = new Vector(); 
        if (msg_id_lst !=null) {
            AccessControlWZB acWZB = new AccessControlWZB();
            for (int i=0;i<msg_id_lst.length;i++) {
                msg_id = Long.parseLong(msg_id_lst[i]);
//                if (acWZB.hasUserPrivilege(con, prof.usr_ent_id, prof.current_role, AcMessage.FTN_MSG_MGT_SYS_IN_TCR)) {
                    if (wizbini.cfgTcEnabled && !this.canEdit(con, prof)) {
                    	undel_lst.addElement(new Long(msg_id));
                    	continue;
                    }
//                }
                del(con);
                //delete file of announ
                String msgDirAbsPath = wizbini.getFileUploadAnnDirAbs() + cwUtils.SLASH + MSG_TYPE_ANN + msg_id;
                dbUtils.delDir(msgDirAbsPath);
            }
        }
        if (undel_lst.size() > 0) {
        	throw new cwSysMessage("MSG008", getUndelMsgTitleAsStr(con, undel_lst));
        }
    }

    private String getUndelMsgTitleAsStr(Connection con, Vector undel_lst) throws SQLException {
		boolean isFirst = true;
		String sql = " select msg_title from Message where msg_id in " + cwUtils.vector2list(undel_lst);
		PreparedStatement stmt = null;
		StringBuffer result = new StringBuffer();
		try {
			stmt = con.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				if (!isFirst) {
					result.append(",");
				}
				result.append(cwUtils.esc4XML(rs.getString("msg_title")));
				isFirst = false;
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return result.toString();
	}


	public void del(Connection con) throws qdbException, cwSysMessage {
        try {
            String SQL = "Delete From Message where msg_id = ? ";

            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, msg_id);
            int stmtResult=stmt.executeUpdate();
            stmt.close();
            if ( stmtResult!=1)                            
            {
                //throw new qdbException("Error deleting Message");
                throw new cwSysMessage(dbUtils.MSG_REC_NOT_FOUND, "Message ID = " + msg_id);
            }
        }
        catch(SQLException e) {
            throw new qdbException(e.getMessage());
        }
    }


    public void updMsg(Connection con, loginProfile prof)
      throws qdbException, qdbErrMessage, cwException, SQLException {
        msg_usr_id = prof.usr_id;
        msg_root_ent_id = prof.root_ent_id;
        /*
        if(!(checkPermission(con, prof))) {
            throw new qdbErrMessage(NO_RIGHT_UPDATE_MSG);
        }
        */
        if(!(checkTimeStamp(con)))
            throw new qdbErrMessage(INVALID_TIMESTAMP_MSG);

        String oldMsgBody = getMsgBodyByMsgId(con, msg_id);
        this.directory_info_map = getDirectoryInfoMap(Dispatcher.getWizbini());
        upd(con);
        moveInvalidMediaFile(oldMsgBody, msg_body, this.directory_info_map);
        
        updateTimeStamp(con);
    }

    public void upd(Connection con) throws qdbException, cwException {
        try {

            if(msg_begin_date != null)
                if(dbUtils.isMinTimestamp(msg_begin_date))
                    msg_begin_date = dbUtils.getTime(con);

            String SQL = "Update Message Set "
                       + "msg_usr_id = ?, "
                       // << BEGIN add "level"
                       + "msg_level = ?, "
                       // >> END
                       + "msg_title = ?, "
//                       + "msg_body = ?, "
                       + "msg_begin_date = ?, "
                       + "msg_end_date = ?, "
                       + "msg_status = ?, "
                       + "msg_receipt = ?, "
                       + "msg_root_ent_id = ? ";
            if (msg_tcr_id > 0) {
            	SQL += ", msg_tcr_id = ? ";
            }
            if(msg_icon != null){
            	SQL +=  ",msg_icon = ?";
            }
            
                  SQL += "Where msg_id = ? ";


            PreparedStatement stmt = con.prepareStatement(SQL);
            int i = 1;
            stmt.setString(i++, msg_usr_id);
            // << BEGIN add "level"
            stmt.setString(i++, msg_level);
            // >> END
            stmt.setString(i++, msg_title);
//            stmt.setString(i++, msg_body);
            stmt.setTimestamp(i++, msg_begin_date);
            stmt.setTimestamp(i++, msg_end_date);
            stmt.setString(i++, msg_status);
            stmt.setBoolean(i++, msg_receipt);
            stmt.setLong(i++, msg_root_ent_id);
            if (msg_tcr_id > 0) {
                stmt.setLong(i++, msg_tcr_id);
            }
            if(msg_icon != null){
            	stmt.setString(i++, msg_icon);
            }
            stmt.setLong(i++, msg_id);
            int rc = stmt.executeUpdate();
            stmt.close();
            if (rc != 1) {
                throw new qdbException("Error updating Message" );
            }
            
          //update msg_body for mutimedia
            msg_body = getMultimediaMsgBody(msg_body, MSG_TYPE_ANN + msg_id, directory_info_map);
            msg_body=msg_body.replace('\\', '/');
            String columnName[]={"msg_body"};
            String columnValue[]={msg_body};
            String condition = "msg_id= " + msg_id;
            cwSQL.updateClobFields(con, "Message",columnName,columnValue, condition);
        }
        catch(SQLException e) {
            throw new qdbException (e.getMessage());
        }
    }

    public void insMsg(Connection con, loginProfile prof)
      throws qdbException, qdbErrMessage, SQLException, cwException {
        msg_usr_id = prof.usr_id;
        msg_root_ent_id = prof.root_ent_id;
        if (!Dispatcher.getWizbini().cfgTcEnabled) {
        	msg_tcr_id = DbTrainingCenter.getSuperTcId(con, prof.root_ent_id);
        }
        this.directory_info_map = getDirectoryInfoMap(Dispatcher.getWizbini());
        ins(con);
    }

    public static HashMap getDirectoryInfoMap(WizbiniLoader wizbini) {
    	HashMap directoryInfoMap = null;
    	if(wizbini != null) {
    		directoryInfoMap = new HashMap();
	    	directoryInfoMap.put(dbMessage._TEMP_DIR_NAME, wizbini.cfgSysSetupadv.getFileUpload().getTmpDir().getName());
	    	directoryInfoMap.put(dbMessage._TEMP_ABS_PATH, wizbini.getFileUploadTmpDirAbs());
	    	directoryInfoMap.put(dbMessage._TARGET_DIR_NAME, wizbini.cfgSysSetupadv.getFileUpload().getAnnDir().getName());
	    	directoryInfoMap.put(dbMessage._TARGET_ABS_PATH, wizbini.getFileUploadAnnDirAbs());
    	}
    	return directoryInfoMap;
    }
    /*
    public boolean checkPermission(Connection con, loginProfile prof) throws qdbException {
        try {
            AccessControlWZB acl = new AccessControlWZB();
            boolean hasRight = false;

            if(msg_type != null && msg_type.equalsIgnoreCase(MSG_TYPE_SYS)) {
                hasRight = acl.hasUserPrivilege(con, prof.usr_ent_id, prof.current_role, FCN_MSG_MGT_SYS);
                //if(prof.sysRoles.contains(dbUserGroup.USG_ROLE_ADMIN))
                //    result = true;
            }
            else if(msg_type != null && msg_type.equalsIgnoreCase(MSG_TYPE_RES)) {
                boolean hasFunction = acl.hasUserPrivilege(con, prof.usr_ent_id, prof.current_role, FCN_MSG_MGT_IN_COS);

                if (hasFunction) {
                    // At least have read permission on the course
                    if (dbResourcePermission.hasPermission(con, msg_res_id, prof, dbResourcePermission.RIGHT_READ)) {
                        hasRight = true;
                    }
                }
                //if (dbResourcePermission.hasPermission(con, msg_res_id, prof,
                //                            dbResourcePermission.RIGHT_WRITE))
                //    result = true;
            }
            return hasRight;
        }catch (SQLException e) {
            throw new qdbException(e.getMessage());
        }
    }
    */
    /*
    private static boolean hasOffReadPrivilege(Connection con, loginProfile prof, String msg_type) throws qdbException {
        try {
            AccessControlWZB acl = new AccessControlWZB();
            boolean hasRight = false;

            if(msg_type != null && msg_type.equalsIgnoreCase(MSG_TYPE_SYS)) {
                hasRight = acl.hasUserPrivilege(con, prof.usr_ent_id, prof.current_role, FCN_MSG_MGT_SYS);
            }
            else if(msg_type != null && msg_type.equalsIgnoreCase(MSG_TYPE_RES)) {
                hasRight = acl.hasUserPrivilege(con, prof.usr_ent_id, prof.current_role, FCN_MSG_MGT_IN_COS);
            }
            return hasRight;
        }catch (SQLException e) {
            throw new qdbException(e.getMessage());
        }
    }
    */

    public void ins(Connection con)
      throws qdbException, cwException {
        try {
            Timestamp curTime = dbUtils.getTime(con);
            msg_upd_date = curTime;
            if(msg_begin_date != null)
                if(dbUtils.isMinTimestamp(msg_begin_date))
                    msg_begin_date = curTime;

            String SQL = "Insert into Message ( "
                       + "  msg_usr_id "
                       + " ,msg_type "
                       // << BEGIN add "level"
                       + " ,msg_level "
                       // >> END
                       + " ,msg_title "
                       + " ,msg_body "
                       + " ,msg_begin_date "
                       + " ,msg_end_date "
                       + " ,msg_status "
                       + " ,msg_res_id "
                       + " ,msg_root_ent_id "
                       + " ,msg_upd_date "
                       + " ,msg_tcr_id "
                       + " ,msg_icon "
                       + " ,msg_mobile_ind "
                       + " ,msg_receipt "
                       + " ) Values ( ?,?,?,?,"+ cwSQL.getClobNull() +",?,?,?,?,?,?, ?, ?,?,?)";

            PreparedStatement stmt = con.prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS);
            int i = 1;
            stmt.setString(i++, msg_usr_id);
            stmt.setString(i++, msg_type);
            // << BEGIN add "level"
            stmt.setString(i++, msg_level);
            // >> END
            stmt.setString(i++, msg_title);
//            stmt.setString(i++, msg_body);
            stmt.setTimestamp(i++, msg_begin_date);
            stmt.setTimestamp(i++, msg_end_date);
            stmt.setString(i++, msg_status);
            stmt.setLong(i++, msg_res_id);
            stmt.setLong(i++, msg_root_ent_id);
            stmt.setTimestamp(i++, msg_upd_date);
            if (msg_type.equalsIgnoreCase(MSG_TYPE_SYS)) {
                stmt.setLong(i++, msg_tcr_id);
            } else {
            	stmt.setNull(i++, java.sql.Types.INTEGER);
            }
            stmt.setString(i++, msg_icon);
            stmt.setBoolean(i++, msg_mobile_ind);
            stmt.setBoolean(i++, msg_receipt);
            int rc = stmt.executeUpdate();
            if (rc != 1) {
                stmt.close();
            	throw new qdbException ("Error inserting into Message.");
            }
            msg_id = cwSQL.getAutoId(con, stmt, "Message", "msg_id");
            stmt.close();
            
            //update msg_body for mutimedia
            msg_body = getMultimediaMsgBody(msg_body, MSG_TYPE_ANN + msg_id, this.directory_info_map);
            String columnName[]={"msg_body"};
            String columnValue[]={msg_body};
            String condition = "msg_id= " + msg_id;
            cwSQL.updateClobFields(con, "Message",columnName,columnValue, condition);
            
        }
        catch (SQLException e) {
            throw new qdbException(e.getMessage());
        }
    }
    
    // << BEGIN COMMENT
    // NOT in-use
    // Message posting
/*    public boolean postMsg(Connection con, String encoding, String title, String body, String begin_date, String end_date, String clientEnc)
      throws qdbException {

        PreparedStatement stmt;
        ResultSet rs;
        String sql_get_msg_id = "SELECT MAX(msg_id) FROM Message";
        String sql_insert_msg = "INSERT INTO Message VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        int col = 1;
        int last_msg_id = 0;

        //escape the stuff for XML
        title = dbUtils.esc4XML(title);
        body = dbUtils.esc4XML(body);

        try {
            //convert the title and message body into unicode
            title = dbUtils.unicodeFrom(title, clientEnc, encoding);
            body = dbUtils.unicodeFrom(body, clientEnc, encoding);

            stmt = con.prepareStatement(sql_get_msg_id);
            rs = stmt.executeQuery();

            if (rs.next())
                last_msg_id = rs.getInt(1) + 1;

            //Insert message to database
            stmt = con.prepareStatement(sql_insert_msg);
            stmt.setInt(col++, last_msg_id);
            stmt.setString(col++, "admin");
            stmt.setString(col++, "ANM");
            stmt.setString(col++, title);
            stmt.setString(col++, body);
            stmt.setString(col++, begin_date);
            stmt.setString(col++, end_date);
            stmt.setString(col++, "APPROVED");
            stmt.executeUpdate();
            con.commit();

            stmt.close();
            return MSG_POST_SUCCESS;
        } catch (SQLException e) {
            throw new qdbException("Failed to post message");
        } catch (java.io.UnsupportedEncodingException uee) {
            throw new qdbException ("UnsupportedEncodingException:" + uee.getMessage());
        }
      }

       public String getMsgAsXML(Connection con)
          throws qdbException {
        StringBuffer result = new StringBuffer("<?xml version=\"1.0\" encoding=\"" + dbUtils.ENC_UTF + "\" standalone=\"no\" ?>");
        String sql_get_msg = "SELECT * FROM Message WHERE msg_status = 'APPROVED'";
        String sql_archive_msg = "UPDATE Message "
                               + "SET msg_status = 'ARCHIVED' "
                               + "WHERE msg_id = ? ";
        PreparedStatement stmt, archive_stmt;
        ResultSet rs, archive_rs;

        try{
            //Get system date
            java.util.Date today = new java.util.Date();

            stmt = con.prepareStatement(sql_get_msg);
            rs = stmt.executeQuery();

            while (rs.next()){
                int archiveMsgId = rs.getInt("msg_id");
                java.util.Date end_date = rs.getDate("msg_end_date");

                if (today.after(end_date)){
                    archive_stmt = con.prepareStatement(sql_archive_msg);
                    archive_stmt.setInt(1, archiveMsgId);
                    archive_stmt.executeUpdate();
                }
            }
            stmt.close();
            //Get message
            stmt = con.prepareStatement(sql_get_msg);
            rs = stmt.executeQuery();

            result.append("<matrix>");

            while (rs.next()){
                //Get message start date
                java.util.Date start_date = rs.getDate("msg_begin_date");
                //Get message end date
                java.util.Date end_date = rs.getDate("msg_end_date");

                if (today.equals(start_date) || today.after(start_date) && today.before(end_date)){
                    result.append("<message id=\"" + rs.getInt("msg_id") + "\" ");
                    result.append("type=\"" + rs.getString("msg_type") + "\" ");
                    result.append("title=\"" + rs.getString("msg_title") + "\" ");
                    result.append("author=\"" + rs.getString("msg_usr_id") + "\" ");
                    result.append("start_date=\"" + rs.getString("msg_begin_date").substring(0, 10) + "\" ");
                    result.append("end_date=\"" + rs.getString("msg_end_date").substring(0, 10) + "\" ");
                    result.append("status=\"" + rs.getString("msg_status") + "\">");
                    result.append("<body>");
                    result.append(rs.getString("msg_body"));
                    result.append("</body>");
                    result.append("</message>");
                }
            }

            result.append("</matrix>");
            stmt.close();

            return result.toString();
        } catch (SQLException e) {
            throw new qdbException("Failed to get message");
        }
    }*/
    // >> END COMMENT

    // << BEGIN modified
    public String jobMsgAsXML(Connection con, loginProfile prof, boolean checkStatus,
                              String wantStatus, int page, int page_size,
                              String sortCol, String sortOrder)
        throws qdbException, cwSysMessage {
        return this.msgAsXML(con, prof, checkStatus, wantStatus, page, page_size,
                             sortCol, sortOrder, this.MSG_TYPE_JOB);
    }

    public String pubMsgAsXML(Connection con, loginProfile prof, boolean checkStatus,
                              String wantStatus, int page, int page_size,
                              String sortCol, String sortOrder)
        throws qdbException, cwSysMessage {
        return this.msgAsXML(con, prof, checkStatus, wantStatus, page, page_size,
                             sortCol, sortOrder, this.MSG_TYPE_PUB);
    }

    public String resMsgAsXML(Connection con, loginProfile prof, boolean checkStatus,
                              String wantStatus, int page, int page_size,
                              String sortCol, String sortOrder)
        throws qdbException, cwSysMessage {
        return this.msgAsXML(con, prof, checkStatus, wantStatus, page, page_size,
                             sortCol, sortOrder, this.MSG_TYPE_RES);
    }

    public String sysMsgAsXML(Connection con, loginProfile prof, boolean checkStatus,
                              String wantStatus, int page, int page_size,
                              String sortCol, String sortOrder)
        throws qdbException, cwSysMessage {
        return this.msgAsXML(con, prof, checkStatus, wantStatus, page, page_size,
                             sortCol, sortOrder, this.MSG_TYPE_SYS);
    }
    // >> END

    // << BEGIN added 2002-4-9
    /**
     * get level name for announcement
     * @param con
     * @param prof
     * @return
     */
    // << BEGIN added 2002-4-9
    /**
     * get level name for announcement
     * @param con
     * @param prof
     * @return
     */
    // for message level code
    public static final String MESSAGE_LEVEL = "MessageLevel";

    public String getMsgLevelLst(Connection con)
        throws SQLException, cwSysMessage {
        // get level list as XML
        String[] ctb_types = { MESSAGE_LEVEL };
        StringBuffer xmlBufLevelList = new StringBuffer(1024);
        // static method getAll(con ,ctb_types) in CodeTable
        xmlBufLevelList.append("<level_list>");
        xmlBufLevelList.append(CodeTable.getAll(con ,ctb_types));
        xmlBufLevelList.append("</level_list>");

        return xmlBufLevelList.toString();
    }
    
    public String getDefaultTcXML(Connection con, loginProfile prof) throws SQLException {
        long default_tcr_id = ViewTrainingCenter.getDefaultTc(con, prof);
        DbTrainingCenter tcr = DbTrainingCenter.getInstance(con, default_tcr_id);
        StringBuffer xmlBuf = new StringBuffer();
        if(tcr != null) {
        	xmlBuf.append("<training_center id=\"").append(tcr.tcr_id).append("\">")
        	.append("<title>").append(dbUtils.esc4XML(tcr.tcr_title)).append("</title>")
        	.append("</training_center>");
        }
        return xmlBuf.toString();
    }
    
    /**
     * the original method signature for backward compatibility
     * a new indicator is added to control if the message body should be shown
     * default is to show the message body in the xml
     * (2004.3.11 kawai)
     */
    private String msgAsXML(Connection con, loginProfile prof, boolean checkStatus,
                           String wantStatus, int page, int page_size,
                           String sortCol, String sortOrder, String type)
        throws qdbException, cwSysMessage {
        return msgAsXML(con, prof, checkStatus,
                        wantStatus, page, page_size,
                        sortCol, sortOrder, type, true);
    }
    private String msgAsXML(Connection con, loginProfile prof, boolean checkStatus,
                           String wantStatus, int page, int page_size,
                           String sortCol, String sortOrder, String type, boolean hasBody)
        throws qdbException, cwSysMessage {
        int i = 1;
        try {
            String xml = "";
            String SQL = "";
            PreparedStatement stmt;
            if (sortCol == null || sortCol.length() == 0) {
                sortCol = " msg_begin_date ";
            }
            if (sortOrder == null || sortOrder.length() == 0) {
                sortOrder = " desc ";
            }
            if (page == 0) {
                page = 1;
            }
            if (page_size == 0) {
                page_size = 10;
            }

            // only pick up messages have (begin_date, end_date) intersect with the input (begin_date, end_date)
            SQL = "SELECT"
                + " msg_id, msg_usr_id, msg_type, msg_level, "
                + " msg_title, msg_body, msg_res_id, msg_status, "
                + " msg_begin_date, msg_end_date, msg_upd_date, usr_display_bil "
                + "FROM"
                + " Message , RegUser "
                + "WHERE"
                + " msg_type = ?"
                + " AND msg_usr_id = usr_id";
            if (type.equalsIgnoreCase(MSG_TYPE_RES)) {
            	if (!hasBody) {
	                SQL += " AND (msg_res_id = ? or msg_res_id in (";
	                SQL += "select B.cos_res_id from aeitemrelation,aeitem,course A,course B where";
	                SQL += "  itm_id=ire_parent_itm_id and ire_parent_itm_id=B.cos_itm_id and A.cos_itm_id=ire_child_itm_id and A.cos_res_id=?  and itm_content_def=? and itm_create_run_ind=1)";
	                SQL += ")";
            	} else{
            		 SQL += " AND msg_res_id = ? ";
            	}
            } else {
                SQL += " AND msg_root_ent_id = ?";
            }

            if (msg_begin_date != null) {
                SQL += " AND NOT(msg_end_date < ?)";
            }
            if (msg_end_date != null) {
                SQL += " AND NOT(msg_begin_date > ?)";
            }
            /*
            if (prof.sysRoles.contains(dbUserGroup.USG_ROLE_STUDENT) ||
                prof.sysRoles.contains(dbUserGroup.USG_ROLE_TEACHER)) {
            */
            if (checkStatus) {
                SQL += " AND msg_status = '" + MSG_STATUS_ON + "'";
            } else
                if (wantStatus != null && wantStatus.length() > 0) {
                    SQL += " AND msg_status = '" + wantStatus + "'";
                }

            SQL += " ORDER BY " + sortCol + " " + sortOrder;

//System.out.println("dbMessage.msgAsXML sql:\n" + SQL);
            stmt = con.prepareStatement(SQL);
            stmt.setString(i++, type);
            if (type.equalsIgnoreCase(MSG_TYPE_RES)) {
                stmt.setLong(i++, msg_res_id);
                if (!hasBody) {
                	stmt.setLong(i++, msg_res_id);
                	stmt.setString(i++, aeItem.PARENT);
                }
                
            } else {
                stmt.setLong(i++, prof.root_ent_id);
            }
            if (msg_begin_date != null) {
                stmt.setTimestamp(i++, msg_begin_date);
            }
            if (msg_end_date != null) {
                stmt.setTimestamp(i++, msg_end_date);
            }

            ResultSet rs = stmt.executeQuery();
            Timestamp eff_end_date;
            String tmp_end_date;
            int start = page_size * (page-1);
            int count = 0;
            while (rs.next() == true) {
                if (count >= start && count < start+page_size) {
                    eff_end_date = rs.getTimestamp("msg_end_date");
                    tmp_end_date = dbUtils.convertEndDate(eff_end_date);
                    long msg_id = rs.getLong("msg_id");
                    xml += "<item id=\"" + msg_id
//                        + "\" usr_id=\"" + dbUtils.esc4XML(dbRegUser.usrId2SteUsrId(con,rs.getString("msg_usr_id")))
                        + "\" usr_display_bil=\"" + dbUtils.esc4XML(rs.getString("usr_display_bil"))
                        + "\" type=\"" + type
                        + "\" begin_date=\"" + rs.getTimestamp("msg_begin_date")
                        + "\" end_date=\"" + tmp_end_date
                        + "\" status=\"" + rs.getString("msg_status")
                        + "\" root_ent_id=\"" + prof.root_ent_id
                        + "\" res_id=\"" + rs.getLong("msg_res_id")
                        + "\" timestamp=\"" + rs.getTimestamp("msg_upd_date")
                        + "\" level=\"" + dbUtils.esc4XML(rs.getString("msg_level"))
                        + "\">" + dbUtils.NEWL
                        + "<title>" + dbUtils.esc4XML(rs.getString("msg_title")) + "</title>";
                    if (hasBody) {
                        xml += "<body>" + dbUtils.esc4XML(cwSQL.getClobValue(rs, "msg_body")) + "</body>";
                    }
                    xml += "</item>" + dbUtils.NEWL;
                }
                count++;
            }
            stmt.close();
            cwPagination pagn = new cwPagination();
            pagn.totalRec = count;
            pagn.totalPage = (int)Math.ceil((float)count/page_size);
            pagn.pageSize = page_size;
            pagn.curPage = page;
            pagn.sortCol = sortCol;
            pagn.sortOrder = sortOrder;
            pagn.ts = null;
            return pagn.asXML() + dbUtils.NEWL + xml + dbUtils.NEWL + this.getMsgLevelLst(con);
        } catch(SQLException e) {
            CommonLog.error(e.getMessage(),e);
            throw new qdbException (e.getMessage());
        }
    }
    // >> END
    
    // get just inserted message by usr_id, upd_date
    private long getMaxMsgId(Connection con) throws SQLException{
        String sql = "SELECT MAX(msg_id) AS msg_id FROM Message WHERE msg_usr_id = ? AND msg_upd_date = ? ";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, msg_usr_id);
        stmt.setTimestamp(2, msg_upd_date);
        
        ResultSet rs = stmt.executeQuery();
        if (rs.next()){
            msg_id = rs.getLong("msg_id");
        }
        stmt.close();
        return msg_id;
    }


	public boolean canEdit(Connection con, loginProfile prof) throws SQLException {
		boolean canEdit = false;
		if (!msg_type.equalsIgnoreCase(MSG_TYPE_SYS)) {
			return true;
		}
		boolean rolTcInd = AccessControlWZB.isRoleTcInd( prof.current_role);
		StringBuffer sql  = new StringBuffer();
		sql.append(" select msg_id from Message where msg_type = ?  and msg_id = ? ") ;
					 
		if(rolTcInd){
			sql.append( " and msg_tcr_id in ( " +
				 " select distinct(child.tcr_id) " +
				 " from tcTrainingCenter ancestor " +
				 " inner join tcTrainingCenterOfficer on (tco_tcr_id = ancestor.tcr_id) " +
				 " left join tcRelation on (tcn_ancestor = ancestor.tcr_id) " +
				 " inner join tcTrainingCenter child on (child.tcr_id = tcn_child_tcr_id or child.tcr_id = ancestor.tcr_id) " +
				 " where tco_usr_ent_id = ? " +
				 " )"
				 );
	     }else{
	    	 sql.append(" AND (msg_tcr_id in (").append("select tcn_child_tcr_id from tcRelation inner join tcTrainingCenter on (tcr_id = tcn_child_tcr_id) where tcn_ancestor = "+prof.my_top_tc_id+" and tcr_status = 'OK' ").append(") or msg_tcr_id ="+prof.my_top_tc_id+" )");
	     }
					 
					 
					 
					 
					
		PreparedStatement stmt = null;
		
		try {
			stmt = con.prepareStatement(sql.toString());
			int index = 1;
			stmt.setString(index++, msg_type);
			stmt.setLong(index++, msg_id);
			if(rolTcInd){
				stmt.setLong(index++, prof.usr_ent_id);
			}
			
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				canEdit = true;
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return canEdit;
	}


	public boolean hasPrivilegeReadMsg(Connection con, loginProfile prof) throws SQLException {
		boolean hasPrv = false;
		String sql = " select * from Message, tcTrainingCenterTargetEntity, EntityRelation " +
					 " where msg_tcr_id = tce_tcr_id " +
					 " and ern_ancestor_ent_id = tce_ent_id " +
					 " and ern_type = ? " +
					 " and ern_child_ent_id = ? " +
					 " and msg_type = ? " +
					 " and msg_id = ? ";
		PreparedStatement stmt = null;
		
		try {
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
			stmt.setLong(index++, prof.usr_ent_id);
			stmt.setString(index++, MSG_TYPE_SYS);
			stmt.setLong(index++, msg_id);
			
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				hasPrv = true;
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return hasPrv;
	}
	
    public static final String _IS_UPDATED_FILE = "isUpdated";
    public static final String _TAG_IMG = "<IMG";
    public static final String _TAG_HYPERLINK_START = "<A";
    public static final String _TAG_HYPERLINK_END = "</A>";
    public static final String _TAG_END = ">";
    public static final String _MARK_SINGLE_QUOTATION = "'";
    public static final String _MARK_DOUBLE_QUOTATION = "\"";
    public static final String _INTERVAL_DOMAIN_DIRECTORY = "/";
    
    
    
    public static String getMultimediaMsgBody(String msgBody, String msgId, HashMap directoryInfoMap) throws cwException   {
    	String resultMsgBody = "";
    	
    	//update image url
    	msgBody = unescImgHTML(msgBody);
    	resultMsgBody = getUpdatedMsgBodyByType(msgBody, directoryInfoMap, _MEDIA_TYPE_IMG, msgId, resultMsgBody);
    	resultMsgBody = escImgHTML(resultMsgBody);
    	
    	//update file url
    	resultMsgBody = getUpdatedMsgBodyByType(resultMsgBody, directoryInfoMap, _MEDIA_TYPE_FILE, msgId, "");
    	
    	return resultMsgBody;
    }


	public static String escImgHTML(String resultMsgBody) {
		resultMsgBody = resultMsgBody.replaceAll("this.width&gt;600", "this.width>600");
		return resultMsgBody;
	}


	public static String unescImgHTML(String msgBody) {
		msgBody = msgBody.replaceAll("this.width>600", "this.width&gt;600");
		return msgBody;
	}

    public static final String _TEMP_DIR_NAME = "tempDirName";
    public static final String _TEMP_ABS_PATH = "tempAbsPath";
    public static final String _TARGET_DIR_NAME = "targetDirName";
    public static final String _TARGET_ABS_PATH = "targetAbsPath";
    
    public static final String _MEDIA_TYPE_IMG = "image";
    public static final String _MEDIA_TYPE_FILE = "file";
    
	public static String getUpdatedMsgBodyByType(String noFilteredMsgBody, HashMap directoryInfoMap, String mediaType, String msgId, String resultMsgBody) throws cwException  {
		if(resultMsgBody == null) {
			resultMsgBody = "";
		}
		
		if(isExistMediaByType(noFilteredMsgBody, mediaType)) {
	    	resultMsgBody = getMsgBodyHeadByMediaType(resultMsgBody, noFilteredMsgBody, mediaType);
	    	String multimediaStr = getMediaHtml(noFilteredMsgBody, mediaType);
	    	noFilteredMsgBody = getNoFilteredMsgBodyByType(noFilteredMsgBody, mediaType);
	    	
	    	//get value of src or href
	    	String mediaUrlStr = getUrlPropertyValueByType(multimediaStr, mediaType);
	    	if(mediaUrlStr != null && mediaUrlStr.indexOf(directoryInfoMap.get(_TEMP_DIR_NAME).toString()) != -1) {
	    		moveAnnMediaFile(mediaUrlStr, directoryInfoMap, msgId);
	    		//replace value of src or href
	    		multimediaStr = getUpdatedUrl(directoryInfoMap, msgId, multimediaStr, mediaUrlStr);
	    	}
	    	
	    	resultMsgBody += multimediaStr;
	    	resultMsgBody = getUpdatedMsgBodyByType(noFilteredMsgBody, directoryInfoMap, mediaType, msgId, resultMsgBody);
		} else {
			//no image or hyperlink tag
			resultMsgBody += noFilteredMsgBody;
		}
		
    	return resultMsgBody;
	}


	private static boolean isExistMediaByType(String noFilteredMsgBody, String mediaType) {
		boolean isExists = false;
		if(_MEDIA_TYPE_IMG.equals(mediaType)) {
			isExists = (noFilteredMsgBody.indexOf(_TAG_IMG) != -1); 
		} else if(_MEDIA_TYPE_FILE.equals(mediaType)) {
			isExists = (noFilteredMsgBody.indexOf(_TAG_HYPERLINK_START) != -1);
		} 
		return isExists;
	}


	private static String getMsgBodyHeadByMediaType(String resultMsgBody, String noFilteredMsgBody, String mediaType) {
		String startTag = getStartTag(mediaType); 
		resultMsgBody += noFilteredMsgBody.substring(0, noFilteredMsgBody.indexOf(startTag));
		return resultMsgBody;
	}


	private static String getStartTag(String mediaType) {
		String startTag = "";
		if(_MEDIA_TYPE_IMG.equals(mediaType)) {
			startTag =  _TAG_IMG;
		} else if(_MEDIA_TYPE_FILE.equals(mediaType)) {
			startTag =  _TAG_HYPERLINK_START;
		}
		return startTag;
	}

	
	private static String getEndTag(String mediaType) {
		String endTag = "";
		if(_MEDIA_TYPE_IMG.equals(mediaType)) {
			endTag =  _TAG_END;
		} else if(_MEDIA_TYPE_FILE.equals(mediaType)) {
			endTag =  _TAG_HYPERLINK_END;
		}
		return endTag;
	}
	

	private static String getNoFilteredMsgBodyByType(String noFilteredMsgBody, String mediaType) {
		String startTag = getStartTag(mediaType);
		String endTag = getEndTag(mediaType);
		noFilteredMsgBody = noFilteredMsgBody.substring(noFilteredMsgBody.indexOf(startTag));	
		int tagEndIndex = noFilteredMsgBody.indexOf(endTag);
		noFilteredMsgBody = noFilteredMsgBody.substring(tagEndIndex + endTag.length());
		return noFilteredMsgBody;
	}


	private static String getMediaHtml(String noFilteredMsgBody, String mediaType) {
		String startTag = getStartTag(mediaType);
		String endTag = getEndTag(mediaType);
		noFilteredMsgBody = noFilteredMsgBody.substring(noFilteredMsgBody.indexOf(startTag));
		int tagEndIndex = noFilteredMsgBody.indexOf(endTag);
		String multimediaStr = noFilteredMsgBody.substring(0, tagEndIndex + endTag.length());
		return multimediaStr;
	}


	private static String getUpdatedUrl(HashMap directoryInfoMap, String msgId, String multimediaStr, String imgSrc) {
		String tempDirName = directoryInfoMap.get(_TEMP_DIR_NAME).toString();
		String localUrlPath = imgSrc.substring(imgSrc.indexOf(tempDirName));
		String fileName = imgSrc.substring(imgSrc.lastIndexOf(_INTERVAL_DOMAIN_DIRECTORY) + 1);
		multimediaStr = multimediaStr.replaceAll(localUrlPath, 
				directoryInfoMap.get(_TARGET_DIR_NAME).toString() + _INTERVAL_DOMAIN_DIRECTORY + msgId + _INTERVAL_DOMAIN_DIRECTORY + fileName);
		return multimediaStr;
	}
	
	private static void moveAnnMediaFile(String urlStr, HashMap directoryInfoMap, String msgId) throws cwException {
		String tempDirName = directoryInfoMap.get(_TEMP_DIR_NAME).toString();
		String tempAbsPath = directoryInfoMap.get(_TEMP_ABS_PATH).toString();
		String targetAbsPath = directoryInfoMap.get(_TARGET_ABS_PATH).toString();
		//move the file and modify the src value
		int fileNameIndex = urlStr.indexOf(tempDirName);
		if(fileNameIndex != -1) {
			String localFilePath = urlStr.substring(fileNameIndex + tempDirName.length() + 1);
			String tempSubDirName = localFilePath.substring(0, localFilePath.lastIndexOf(_INTERVAL_DOMAIN_DIRECTORY));
			String srcFilePath = tempAbsPath + cwUtils.SLASH + tempSubDirName;
			String targetFilePath = targetAbsPath + cwUtils.SLASH + msgId;
			try {
				dbUtils.copyDir(srcFilePath, targetFilePath);
			} catch(qdbException e) {
				throw new cwException("operation of file has error.");
			}
		}
	}
	
    public static final String _HTML_SRC = "src=";
    public static final String _HTML_HREF = "href=";
    public static final String _HTML_ISUPDATED = "isupdated";
    public static String getUrlPropertyValueByType(String htmlNodeStr, String mediaType) {
    	String propertyName = getUrlPropertyName(mediaType);
    	String mediaUrlStr = null;
    	if(htmlNodeStr != null && htmlNodeStr.indexOf(_HTML_ISUPDATED) != -1) {
    		mediaUrlStr = getUrlPropertyValue(htmlNodeStr, propertyName); 
    	}
    	return mediaUrlStr;
    }


	private static String getUrlPropertyName(String mediaType) {
		String propertyStr = "";
    	if(_MEDIA_TYPE_IMG.equals(mediaType)) {
    		propertyStr =  _HTML_SRC;
		} else if(_MEDIA_TYPE_FILE.equals(mediaType)) {
			propertyStr =  _HTML_HREF;
		}
		return propertyStr;
	}


	private static String getUrlPropertyValue(String htmlNodeStr, String propertyStr) {
		String mediaUrlStr = "";
		int srcStartIndex = htmlNodeStr.indexOf(propertyStr);
		if(srcStartIndex != -1) {
			mediaUrlStr = htmlNodeStr.substring(srcStartIndex);
			int urlStartIndex = mediaUrlStr.indexOf(_MARK_DOUBLE_QUOTATION) + 1;
			mediaUrlStr = mediaUrlStr.substring(urlStartIndex);
			int urlEndIndex = mediaUrlStr.indexOf(_MARK_DOUBLE_QUOTATION);
			mediaUrlStr = mediaUrlStr.substring(0, urlEndIndex);
		}
		return mediaUrlStr;
	}
    
    public static boolean isValidHyperkink(String hyperlinkStr) {
    	boolean isValidLink = false;
    	
    	if(hyperlinkStr != null && hyperlinkStr.trim().length() > 0) {
	    	int linkLabelStartIndex = hyperlinkStr.indexOf(_TAG_END) + _TAG_END.length();
	    	int linkEndIndex = hyperlinkStr.indexOf(_TAG_HYPERLINK_END);
	    	String labelStr = hyperlinkStr.substring(linkLabelStartIndex, linkEndIndex).trim();
	    	if(labelStr != null && labelStr.length() > 0) {
	    		isValidLink = true;
	    	}
    	}
    	
    	return isValidLink;
    }
    
    public static void moveInvalidMediaFile(String oldMsgBody, String newMsgBody, HashMap directoryInfoMap) {
    	//get all url of oldMsgBody
    	HashMap oldUrlMap = new HashMap();
    	oldUrlMap = getAllUrlByType(oldMsgBody, _MEDIA_TYPE_IMG, oldUrlMap);
    	oldUrlMap = getAllUrlByType(oldMsgBody, _MEDIA_TYPE_FILE, oldUrlMap);
    	
    	//get all ulr of newMsgBody
    	HashMap newUrlMap = new HashMap();
    	newUrlMap = getAllUrlByType(newMsgBody, _MEDIA_TYPE_IMG, newUrlMap);
    	newUrlMap = getAllUrlByType(newMsgBody, _MEDIA_TYPE_FILE, newUrlMap);
    	
    	//filter invaild url
    	Vector invaildUrlFileVec = getInvaildUrlList(oldUrlMap, newUrlMap, directoryInfoMap);
    	//remove invaild file
    	removeInvaildUrlFile(invaildUrlFileVec, directoryInfoMap);
    }
    
    public static HashMap getAllUrlByType(String noFilteredMsgBody, String mediaType, HashMap urlMap) {
		if (urlMap == null) {
			urlMap = new HashMap();
		}

		String urlPropertyName = getUrlPropertyName(mediaType);
		if (noFilteredMsgBody.indexOf(urlPropertyName) != -1) {
			urlMap.put(getUrlPropertyValue(noFilteredMsgBody, urlPropertyName), "");
			noFilteredMsgBody = noFilteredMsgBody.substring(noFilteredMsgBody.indexOf(urlPropertyName) + urlPropertyName.length());
			getAllUrlByType(noFilteredMsgBody, mediaType, urlMap);
		}

		return urlMap;
	}
    
    public static Vector getInvaildUrlList(HashMap oldUrlMap, HashMap newUrlMap, HashMap directoryInfoMap) {
		Vector invaildUrlVec = new Vector();
		if(oldUrlMap != null && newUrlMap != null) {
			Set urlMapKeys = oldUrlMap.keySet();
			for(Iterator iter = urlMapKeys.iterator(); iter.hasNext();) {
				String oldUrlStr = (String) iter.next();
				if(newUrlMap.get(oldUrlStr) == null) {
					invaildUrlVec.add(oldUrlStr);
				}
			}
		}
		return invaildUrlVec;
	}
    
    public static void removeInvaildUrlFile(Vector invaildUrlFileVec, HashMap directoryInfoMap) {
    	if(invaildUrlFileVec != null) {
    		for(int i = 0; i < invaildUrlFileVec.size(); i++) {
    			String fileUrl = (String) invaildUrlFileVec.elementAt(i);
    			String fileAbsPath = getFileAbsPathByUrl(fileUrl, directoryInfoMap);
    			if(fileAbsPath != null && fileAbsPath.length() > 0) {
	    			File mediaFile = new File(fileAbsPath);
	    			if(mediaFile.exists()) {
	    				mediaFile.delete();
	    			}
    			}
    		}
    	}
    }
    
    public static String getFileAbsPathByUrl(String urlStr, HashMap directoryInfoMap) { 
    	String fileAbsPath = null;
    	int lastItervalIndex = urlStr.lastIndexOf(_INTERVAL_DOMAIN_DIRECTORY);
    	if(lastItervalIndex != -1) {
    		String fileName = urlStr.substring(lastItervalIndex + _INTERVAL_DOMAIN_DIRECTORY.length());
    		if(fileName != null && fileName.trim().length() > 0) {
    			int lastSecondItervalIndex = urlStr.substring(0, lastItervalIndex).lastIndexOf(_INTERVAL_DOMAIN_DIRECTORY);
    			if(lastSecondItervalIndex != -1) {
    				String msgId = urlStr.substring(lastSecondItervalIndex + 1, lastItervalIndex);
    				fileAbsPath = directoryInfoMap.get(_TARGET_ABS_PATH).toString() + cwUtils.SLASH + msgId + cwUtils.SLASH + fileName;
    			}
    		}
    	}
		return fileAbsPath;
	}
    
    private static String getMsgBodyByMsgId(Connection con, long msgId) throws SQLException{
    	String sql = "SELECT msg_body FROM Message WHERE msg_id = ? ";
        PreparedStatement stmt = con.prepareStatement(sql);
        int index = 1;
        stmt.setLong(index++, msgId);
        
        ResultSet rs = stmt.executeQuery();
        String msgBody = "";
        if (rs.next()){
        	msgBody = rs.getString("msg_body");
        }
        cwSQL.closePreparedStatement(stmt);
        
        return msgBody;
    }
    
	public void delFile(Connection con, String saveDir) throws cwException {
		String saveDirPath = saveDir + dbUtils.SLASH + this.msg_id;
	    dbUtils.delDir(saveDirPath);
	}
	
	public void uploadedFile(String tmpSaveDirPath, String saveDir, WizbiniLoader wizbini, String msg_icon_name) throws cwException, IOException {
		String saveDirPath = saveDir + dbUtils.SLASH + this.msg_id;
		String msg_icon = saveDirPath + dbUtils.SLASH + msg_icon_name;
	    try {
	        dbUtils.moveDir(tmpSaveDirPath, saveDirPath);
	        cwUtils.resizeImage(msg_icon, wizbini.getItmImageWidth(), wizbini.getItmImageHeight());
	    } catch(qdbException e) {
	        throw new cwException(e.getMessage());
	    }
	}
	
    public static String getMsgTitleByMsgId(Connection con, long msgId) throws SQLException{
    	String sql = "SELECT msg_title FROM Message WHERE msg_id = ? ";
        PreparedStatement stmt = con.prepareStatement(sql);
        int index = 1;
        stmt.setLong(index++, msgId);
        
        ResultSet rs = stmt.executeQuery();
        String msgTitle = "";
        if (rs.next()){
        	msgTitle = rs.getString("msg_title");
        }
        cwSQL.closePreparedStatement(stmt);
        
        return msgTitle;
    }
}