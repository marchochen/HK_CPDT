package com.cw.wizbank.JsonMod.tcrCommon;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import com.cw.wizbank.dao.SQLMapClientFactory;
import com.cw.wizbank.dao.SqlMapClientDataSource;

public class TcrLogic {
	private final SqlMapClientDataSource sqlMapClient = SQLMapClientFactory.getSqlMapClient();
	private final String getUsrInTcrInforSql = new StringBuffer()
	.append("select distinct tcr_id, tcr_title from reguser")
	.append(" inner join entityrelation on (usr_ent_id = ern_child_ent_id and ern_type = 'USR_PARENT_USG' and ern_parent_ind = 1)")
	.append(" inner join tcTrainingCenterTargetEntity on (ern_ancestor_ent_id = tce_ent_id)")
	.append(" inner join tcTrainingCenter on (tcr_id = tce_tcr_id and tcr_status = 'OK')")
	.append(" where usr_ent_id = ?").toString();
	
	private final String getUsrInTcrListInforSql = new StringBuffer()
	.append("select tcr_id, tcr_title from tcTrainingCenter")
	.append(" inner join")
	.append(" (select distinct tce_tcr_id usr_tcr_id from reguser")
	.append(" inner join entityrelation on (usr_ent_id = ern_child_ent_id and ern_type = 'USR_PARENT_USG')")
	.append(" inner join tcTrainingCenterTargetEntity on (ern_ancestor_ent_id = tce_ent_id)")
	.append(" where usr_ent_id = ?")
	.append(" union")
	.append(" select distinct tcn_ancestor usr_tcr_id from reguser")
	.append(" inner join entityrelation on (usr_ent_id = ern_child_ent_id and ern_type = 'USR_PARENT_USG')")
	.append(" inner join tcTrainingCenterTargetEntity on (ern_ancestor_ent_id = tce_ent_id)")
	.append(" inner join tcRelation on (tcn_child_tcr_id = tce_tcr_id)")
	.append(" where usr_ent_id = ?)")
	.append(" usr_tcr_tab on (tcr_id = usr_tcr_tab.usr_tcr_id and tcr_status = 'OK')").toString();
	
	
	
	private static TcrLogic tcrLogic = null;
	
	private TcrLogic (){}
	
	public static TcrLogic getInstance(){
		if(tcrLogic == null){
			tcrLogic = new TcrLogic();
		}
		return tcrLogic;
	}
	
	/*
	 * 获取用户所在的培训中心
	 */
    public Map getUsrInTcrInfor(Connection con, long usr_ent_id){
	    Map map = sqlMapClient.getObject(con, getUsrInTcrInforSql, new Object[]{new Long(usr_ent_id)});
	    map = (map == null) ? sqlMapClient.getObject(con, "select tcr_id, tcr_title from tcTrainingCenter where tcr_id = ?", new Object[]{new Integer(1)}) : map;
	    return map;
	}
    
	/*
	 * 获取用户所在的所有培训中心列表
	 */
	public List getUsrInTcrInforList(Connection con, long usr_ent_id){
		List list = sqlMapClient.getObjectList(con, getUsrInTcrListInforSql, new Object[]{new Long(usr_ent_id), new Long(usr_ent_id)});
		list = (list.isEmpty()) ? sqlMapClient.getObjectList(con, "select tcr_id, tcr_title from tcTrainingCenter where tcr_id = ?", new Object[]{new Integer(1)}) : list;
		
		return list;
	}
}
