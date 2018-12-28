/*
 * Created on 2004-9-15
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.cw.wizbank.ae.db.view;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cw.wizbank.accesscontrol.acSite;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.db.DbItemCost;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.config.organization.itemcostmgt.ItemCostType;
import com.cw.wizbank.util.cwSQL;

/**
 * @author donaldl
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ViewItemCost {
	 private String getItemCosts  ="select * from aeItemCost where ito_itm_id = ? order by ito_type";
	 private String getItemCostsStatistic = "select ito_itm_id,sum(ito_budget) ito_sum_budget,avg(ito_budget) ito_avg_budget,sum(ito_actual) ito_sum_actual,avg(ito_actual) ito_avg_actual "
                                           +" from aeItemCost group by ito_itm_id order by ito_itm_id";

     private StringBuffer delRedundantData = new StringBuffer("Delete from aeItemCost where ito_itm_id in (select itm_id from aeItem,acSite where itm_type= ? and itm_run_ind = ? and itm_owner_ent_id = ste_ent_id and ste_id = ?) and ito_type not in ");
	 
	 public List getItemCostsWithItem(Connection con,long itm_id){
	 	List list = new ArrayList();
	 	DbItemCost obj = null;
	 	PreparedStatement pst = null;
	 	ResultSet rs = null;
	 	try{
	 		pst = con.prepareStatement(getItemCosts);
	 		pst.setLong(1,itm_id);
	 		rs = pst.executeQuery();
	 		while(rs.next()){
	 			obj = new DbItemCost(rs.getLong("ito_itm_id"),rs.getLong("ito_type"));
	 			obj.setIto_budget(rs.getBigDecimal("ito_budget"));
	 			obj.setIto_actual(rs.getBigDecimal("ito_actual"));
	 			obj.setIto_create_timestamp(rs.getTimestamp("ito_create_timestamp"));
	 			obj.setIto_create_usr_id(rs.getString("ito_create_usr_id"));
	 			obj.setIto_update_timestamp(rs.getTimestamp("ito_update_timestamp"));
	 			obj.setIto_update_usr_id(rs.getString("ito_update_usr_id"));
	 			
	 			list.add(obj);
	 		}
	 	}catch(Exception e){
	 		throw new RuntimeException(e.getMessage());
	 	}finally{
			cwSQL.closeResultSet(rs);
			cwSQL.closePreparedStatement(pst);
	 	}
	 	return list;
	 }
	 
	public Map getItemCostsMapWithItem(Connection con,long itm_id){
			Map map = new HashMap();
			DbItemCost obj = null;
			PreparedStatement pst = null;
			ResultSet rs = null;
			try{
				pst = con.prepareStatement(getItemCosts);
				pst.setLong(1,itm_id);
				rs = pst.executeQuery();
				while(rs.next()){
					obj = new DbItemCost(rs.getLong("ito_itm_id"),rs.getLong("ito_type"));
					obj.setIto_budget(rs.getBigDecimal("ito_budget"));
					obj.setIto_actual(rs.getBigDecimal("ito_actual"));
					obj.setIto_create_timestamp(rs.getTimestamp("ito_create_timestamp"));
					obj.setIto_create_usr_id(rs.getString("ito_create_usr_id"));
					obj.setIto_update_timestamp(rs.getTimestamp("ito_update_timestamp"));
					obj.setIto_update_usr_id(rs.getString("ito_update_usr_id"));
	 			
					map.put(new Long(obj.getIto_type()),obj);
				}
			}catch(Exception e){
				throw new RuntimeException(e.getMessage());
			}finally{
				cwSQL.closeResultSet(rs);
				cwSQL.closePreparedStatement(pst);
			}
			return map;
		 }
		 
    public Map getItemCostStatistic(Connection con){
    	Map map = new HashMap();
        BigDecimal[] cost = new BigDecimal[4];
        PreparedStatement pst = null;
        ResultSet rs = null;
        try{
        	pst = con.prepareStatement(getItemCostsStatistic);
        	rs = pst.executeQuery();
        	while(rs.next()){
        		cost[0] = rs.getBigDecimal("ito_sum_budget");
				cost[1] = rs.getBigDecimal("ito_avg_budget");
				cost[2] = rs.getBigDecimal("ito_sum_actual");
				cost[3] = rs.getBigDecimal("ito_avg_actual");
				
				map.put(new Long(rs.getLong("ito_itm_id")),cost);
        	}
        	pst.close();
        }catch(Exception e){
        	throw new RuntimeException("DataBaseSystem error:"+e.getMessage());
        }
    	return map;
    }
    
    public void delRedundantData(Connection con,WizbiniLoader wizbini){
    	aeItem item = new aeItem();
    	String ste_id = null;
    	PreparedStatement pst = null;
    	String clause = null;
    	int len = delRedundantData.length();
    	try{
			List list = acSite.getAllSites(con);
			//pst = con.prepareStatement(delRedundantData)
			for(int i=0,n=list.size();i<n;i++){
				ste_id = list.get(i).toString();
				List inOneOrg = item.getItemCostAttriList(wizbini,ste_id);
				for(int j=0,m=inOneOrg.size();j<m;j++){
					ItemCostType costType = (ItemCostType)inOneOrg.get(j);
					clause = item.getCostTypesClause(costType);
					delRedundantData.append(clause);
					pst = con.prepareStatement(delRedundantData.toString());
					pst.setString(1,costType.getItmType());
					pst.setBoolean(2,costType.isItmRunInd());
					pst.setString(3,ste_id);
					
					pst.executeUpdate();
					pst.close();
					delRedundantData = new StringBuffer(delRedundantData.substring(0,len+1));
				}
			}
			con.commit();
    	}catch(Exception e){
    		throw new RuntimeException("System error: "+e.getMessage());
    	}finally{
    		cwSQL.closePreparedStatement(pst);
    	}
    	
    }
	
}
