package com.cw.wizbank.JsonMod.Ann;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Vector;

import com.cw.wizbank.Dispatcher;
import com.cw.wizbank.JsonMod.Ann.bean.AnnBean;
import com.cw.wizbank.JsonMod.Ann.bean.ReceiptBean;
import com.cw.wizbank.JsonMod.commonBean.TCBean;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.dbMessage;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwUtils;

public class Ann {
	
	public static final int ANNOUNCEMENT_SHOW_BODY_LENGTH = 25;
	
    /**
     * 获取某个培训中心下的公告
     * @param con
     * @param prof
     * @param param
     * @param tc_id
     * @param isShowMsgDesc
     * @return Ann Vector
     * @throws SQLException
     * @throws qdbException
     */
	public Vector getAnnByTc_ID(Connection con, loginProfile prof,AnnModuleParam param,long tc_id, boolean isShowMsgDesc, WizbiniLoader wizbini) throws SQLException, qdbException{
		dbMessage msg=new dbMessage();
		msg.msg_tcr_id=tc_id;
		PreparedStatement stmt=msg.getAnnForRead(con, prof, param.getSort(), param.getDir(), null,  wizbini,null);
		ResultSet rs=stmt.executeQuery();
		Vector vc=new Vector();
		int count=0;
		while(rs.next()){
			if(count>=param.getStart() &&count<(param.getLimit()+param.getStart())){    
				AnnBean ann=new AnnBean();
				ann.setMsg_id(rs.getLong("msg_id"));
				ann.setMsg_title(rs.getString("msg_title"));
				ann.setMsg_begin_date(rs.getTimestamp("msg_begin_date"));
				String msgContent = rs.getString("msg_body");
				if(isShowMsgDesc && msgContent != null) {
					String msgDesc = cwUtils.getContentFromHtmlStr(msgContent);
					if (msgDesc!=null && msgDesc.length() > ANNOUNCEMENT_SHOW_BODY_LENGTH) {
						msgDesc = msgDesc.substring(0, ANNOUNCEMENT_SHOW_BODY_LENGTH) + "...";
					}
					ann.setMsg_desc(msgDesc); 
				} else {
					ann.setMsg_body(msgContent);
				}
				ann.setUsr_display_bil(rs.getString("usr_display_bil"));
				ann.setNewest_ind(isNewestAnn(param.getCur_time(), ann.getMsg_begin_date(), Dispatcher.getWizbini().getNewestAnnDuration()));
				vc.add(ann);
			}
			count++;
		}
		param.setTotal_rec(count);
		if(stmt!=null) stmt.close();
		return vc;		
	}
	
	public static boolean isNewestAnn(Timestamp curTime, Timestamp targetTime, int fixedDays) {
		boolean isNewest = false;
		if(curTime != null && targetTime != null) {
			long timeDiff = curTime.getTime() - targetTime.getTime();
			long targetTimeDiff = fixedDays * 24 * 60 * 60 * 1000;
			if(timeDiff <= targetTimeDiff) {
				isNewest = true;
			}
		}
		return isNewest;
	}
	
	/**
	 * 获取培训中心的公告，如果前台没传递tc_id，则取第一个培训中心。
	 * @param con
	 * @param prof
	 * @param param
	 * @param tc_lst
	 * @return
	 * @throws SQLException
	 * @throws qdbException
	 */
	public Vector getAnnByTC(Connection con, loginProfile prof,AnnModuleParam param ,Vector tc_lst, WizbiniLoader wizbini) throws SQLException, qdbException{
		if(param.getTcr_id() == 0 && tc_lst != null && tc_lst.size() > 0){
			TCBean dbtc = (TCBean)tc_lst.get(0);
			param.setTcr_id(dbtc.getTcr_id());
		}
		if(param.getSort() ==null){
			param.setSort("msg_upd_date");
			param.setDir("desc");
		}
		Vector vc = getAnnByTc_ID(con,  prof, param, param.getTcr_id(), true,  wizbini);
		
		return vc;
	}
	
	public AnnBean getAnnDetail(Connection con,long msg_id)throws SQLException, qdbException{
		String sql="select msg_id,msg_title,msg_type,msg_body,msg_begin_date,msg_status,usr_display_bil from Message left join regUser on usr_id = msg_usr_id where msg_id = ?";
		PreparedStatement stmt = null; 
		stmt = con.prepareStatement(sql);
		stmt.setLong(1, msg_id);
		ResultSet rs = stmt.executeQuery();
		AnnBean ann=new AnnBean();
		if(rs.next()){
			ann.setMsg_id(rs.getLong("msg_id"));
			ann.setMsg_title(rs.getString("msg_title"));
			ann.setMsg_begin_date(rs.getTimestamp("msg_begin_date"));
			String msgContent = rs.getString("msg_body");
			ann.setMsg_body(msgContent);
			ann.setUsr_display_bil(rs.getString("usr_display_bil"));
		}
		cwSQL.cleanUp(rs, stmt);
		return ann;
	}
	
	
	/**
	 * 获取培训中心的公告，如果前台没传递tc_id，则取第一个培训中心。
	 * @param con
	 * @param prof
	 * @param param
	 * @param tc_lst
	 * @return
	 * @throws SQLException
	 * @throws qdbException
	 */
	public Vector getAnnByTC(Connection con, loginProfile prof,AnnModuleParam param , WizbiniLoader wizbini) throws SQLException, qdbException{
		if(param.getSort() ==null){
			param.setSort("msg_upd_date");
			param.setDir("desc");
		}
		Vector vc = getAnnByTc_ID(con,  prof, param, param.getTcr_id(), true,  wizbini);
		
		return vc;
	}
	
	public Long getAnnListCount(Connection con,Long msg_id) throws SQLException{
		String sql = "select msg_id from Message where msg_id <= ? and msg_type='SYS' ORDER BY msg_upd_date desc";
		Long count = 0l;
		PreparedStatement stmt = null; 
		stmt = con.prepareStatement(sql);
		stmt.setLong(1, msg_id);
		ResultSet rs = stmt.executeQuery();
		boolean isFind = false;
		while(rs.next()){
			++count;
			if( msg_id == rs.getLong("msg_id") ){
				isFind = true;
				break;
			}
		}
		cwSQL.cleanUp(rs, stmt);
		if(!isFind){
			count = 0l;
		}
		return count;
	}

	public StringBuffer getReceiptViews(Connection con, long msg_id) throws SQLException{
		StringBuffer sql = new StringBuffer();
		sql .append("SELECT REC_ID,USR_DISPLAY_BIL,USG_DISPLAY_BIL,RECEIPT_DATE FROM Receipt rec ");
		sql.append(" INNER JOIN Reguser reg ON (rec.REC_ENT_ID = reg.usr_ent_id)  ");
		sql.append(" INNER JOIN UserGroup usg ON (rec.REC_USG_ID = usg.usg_ent_id)  ");
		sql.append(" WHERE REC_MSG_ID = ? ");
		PreparedStatement stmt = null; 
		stmt = con.prepareStatement(sql.toString());
		stmt.setLong(1, msg_id);
		ResultSet rs = stmt.executeQuery();
		StringBuffer xmlBuf = new StringBuffer(1024);
		xmlBuf.append(" <receipt>");
		while(rs.next()){
			xmlBuf.append(" <rec_list rec_id=\"").append(rs.getLong("REC_ID"))
						.append("\" usr_name=\"").append(rs.getString("USR_DISPLAY_BIL"))
						.append("\" usg_name=\"").append(rs.getString("USG_DISPLAY_BIL"))
						.append("\" receipt_date=\"").append(rs.getTimestamp("RECEIPT_DATE"))
						.append("\"/>");
		}
		xmlBuf.append("</receipt>");
		cwSQL.cleanUp(rs, stmt);
		return xmlBuf;
		
	}


}
