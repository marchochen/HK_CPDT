/*
 * Created on 2004-9-15
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.cw.wizbank.ae;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.cw.wizbank.ae.db.DbItemCost;
import com.cw.wizbank.ae.db.view.ViewItemCost;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.config.WizbiniLoader;



/**
 * @author donaldl
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class aeItemCost {
	private static final int OTHER_CHANGED = -1;
	private static final int INS = 0;
	private static final int UPD = 1;
	
	private String getItemXml(Connection con,aeItem item)throws SQLException,cwSysMessage{
			item.getItem(con);
			Timestamp cur_time = cwSQL.getTime(con);
			StringBuffer xmlBuf = new StringBuffer(512);
			xmlBuf.append(item.object2Xml(cur_time));
			xmlBuf.append("<title>").append(dbUtils.esc4XML(aeUtils.escNull(item.itm_title))).append("</title>");
			return xmlBuf.toString();
		}
	
	private String endItemXml(){
			String end = "</item>";
			return end;
		}
		
	/* if int[0]==-1:otherbody have changed the data 
	 * if int[i]==1:this record will be changed
	 */	
	private int[] isNeed2Upd(List newCost,Map oldCost){
		int[] needUpd = new int[newCost.size()];
		ViewItemCost viewCost = new ViewItemCost();
		
		DbItemCost tmp_obj1 = null;
		DbItemCost tmp_obj2 = null;
		
		for(int i=0,n=newCost.size();i<n;i++){
			tmp_obj1 = (DbItemCost)newCost.get(i);
			tmp_obj2 = (DbItemCost)oldCost.get(new Long(tmp_obj1.getIto_type()));
			if(tmp_obj2 == null){
				if(tmp_obj1.getIto_actual() == null && tmp_obj1.getIto_budget() == null){
					continue;
				}else{
					needUpd[i] = 1;
				}
			}else if(tmp_obj1.isChangedByOther(tmp_obj2)){
				needUpd[0] = OTHER_CHANGED;
				break;
			}else if(tmp_obj1.isValueChanged(tmp_obj2)){
				needUpd[i] = 1;
			}
		}
		return needUpd;
	}
	
	private void updItemCostData(Connection con,DbItemCost itemCost,loginProfile prof){
		try{
		    Timestamp time = cwSQL.getTime(con);
		    itemCost.setIto_update_timestamp(time);
		    itemCost.setIto_update_usr_id(prof.usr_id);
		    itemCost.upd(con);
		}catch(SQLException e){
			throw new RuntimeException(e.getMessage());
		}		
	}
	
	private void insItemCostData(Connection con,DbItemCost itemCost,loginProfile prof){
		try{
			Timestamp time = cwSQL.getTime(con);
		    itemCost.setIto_create_timestamp(time);
		    itemCost.setIto_update_timestamp(time);
		    itemCost.setIto_create_usr_id(prof.usr_id);
		    itemCost.setIto_update_usr_id(prof.usr_id);
		    
		    itemCost.ins(con);
		}catch(SQLException e){
			throw new RuntimeException(e.getMessage());
		}
	}
		
	public String getItemsCostXml(Connection con,long itm_id){		
		ViewItemCost viewItemCost = new ViewItemCost();
		StringBuffer sb = new StringBuffer(128);
		List list = null;
		List config_type = null;
		//data in the db
		list = viewItemCost.getItemCostsWithItem(con,itm_id);
		sb.append("<item_cost>");
		DbItemCost dbItemCost = null;
		for(int i=0,n=list.size();i<n;i++){
			dbItemCost = (DbItemCost)list.get(i);
			sb.append(dbItemCost.object2Xml(con));
		}
		sb.append("</item_cost>");
		return sb.toString();
	}
	
	
	public String getItemCostPageXml(Connection con,aeItem item)throws SQLException,cwSysMessage{
		StringBuffer sb = new StringBuffer(1024);
		
		sb.append(getItemXml(con,item));
		sb.append(aeItem.getNavAsXML(con, item.itm_id));
		//item type info
		sb.append(aeUtils.escNull(item.getItemTypeTitle(con)));
		sb.append(getItemsCostXml(con,item.itm_id));
		sb.append(endItemXml());
		return sb.toString();
	}
	
	
	
	public boolean commitCostData(Connection con,List list,aeItem item,loginProfile prof){
	   ViewItemCost viewCost = new ViewItemCost();
	   DbItemCost itemCost = null;
	   Map oldCost = viewCost.getItemCostsMapWithItem(con,item.itm_id);
	   int[] need2Upd = isNeed2Upd(list,oldCost);
	   if(need2Upd[0] == OTHER_CHANGED){
	   	   return false;
	   }
	   for(int i=0,n=need2Upd.length;i<n;i++){
	   	   if(need2Upd[i] == 0){
	   	   	  continue;
	   	   }else{
	   	   	  //update or insert it!!!
	   	   	  itemCost = (DbItemCost)list.get(i);
	   	   	  if(itemCost.getChang_type() == INS){
				insItemCostData(con,itemCost,prof);
	   	   	  }else if(itemCost.getChang_type() == UPD){
				updItemCostData(con,itemCost,prof);
	   	   	  }
	   	   }
	   	   
	   }
	   return true;
	}
	
	public static void delRedundantItemCost(Connection con,WizbiniLoader wizbini){
		ViewItemCost viewItemCost = new ViewItemCost();
		viewItemCost.delRedundantData(con,wizbini);
	}
}
