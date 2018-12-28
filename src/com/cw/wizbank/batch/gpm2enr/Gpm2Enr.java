package com.cw.wizbank.batch.gpm2enr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.cw.wizbank.batch.batchUtil.BatchUtils;
import com.cw.wizbank.qdb.dbEntityRelation;
import com.cw.wizbank.qdb.dbEntityRelationHistory;
import com.cw.wizbank.util.cwSQL;
import com.cwn.wizbank.utils.CommonLog;

public class Gpm2Enr {

	
	public static void main(String[] args) {
		Connection con = null;
		if(args.length <= 0){
			System.err.println("Error : Please insert parameter !!");
		} else {
			try{
				con = BatchUtils.openDB(args[0]);
				Timestamp sTime = cwSQL.getTime(con);
				
				//---------------------------------------------------------------------
				String sql = "select count(*) from EntityRelation" ;
				PreparedStatement ps = con.prepareStatement(sql);
		    	ResultSet rs = ps.executeQuery();
		    	if(rs.next()){
		    		if(rs.getInt(1)>0){
		    			System.out.println("Table EntityRelation has data already, can't Transfer !! -- " + sTime);
		    			return;
		    		}
		    	}
		    	ps.close();
		    	//--------------------------------------------------------------------
		    	
				System.out.println("Transfer data from GroupMember to EntityRelation start. -- " + sTime);
	  	        transferGpm2Enr(con, sTime);
	  	        con.commit();
	  	        Timestamp eTime = cwSQL.getTime(con);
	  	        System.out.println("Transfer data from GroupMember to EntityRelation end. -- " + eTime);
			} catch (Exception e){
				System.err.println("Transfer data from GroupMember to EntityRelation error!!");
				try {
					CommonLog.error(e.getMessage(),e);
					con.rollback();
				} catch (SQLException e1) {
					//e1.printStackTrace();
					CommonLog.error(e1.getMessage(),e1);
					CommonLog.error("Error : Connection rollback error!!");
				}
				CommonLog.error(e.getMessage(),e);
				//System.err.println("Error : " + e.getMessage());
				CommonLog.error("Error : " + e.getMessage());
			}
		}

	}
public static void transferGpm2Enr(Connection con, Timestamp curTime) throws Exception{
    	
    	String sql = "select * from groupMember";
    	PreparedStatement ps = con.prepareStatement(sql);
    	ResultSet rs = ps.executeQuery();
		int count = 0;
		int commit_count = 1000;
    	while(rs.next()){
    		count++;
    		Timestamp gpm_end_timestam = rs.getTimestamp("gpm_end_timestamp");
    		long child_ent_id = rs.getLong("gpm_ent_id_member");
    		long ancestor_ent_id = rs.getLong("gpm_ent_id_group");
    		String type = rs.getString("gpm_type");
    		if(gpm_end_timestam != null && gpm_end_timestam.before(curTime)){
    			try{
    				ins2EnrHis(con, rs, gpm_end_timestam, child_ent_id, type);
    			} catch(SQLException e){
    				CommonLog.error("insert 2 EntityRelationHistory fail.--child="+child_ent_id+";ancestor="+ancestor_ent_id+";type="+type,e);
    			}
    		} else {
    			try{
    				ins2Enr(con, rs, child_ent_id, type);
    			} catch(SQLException e){
    				CommonLog.error("insert 2 EntityRelation fail.--child="+child_ent_id+";ancestor="+ancestor_ent_id+";type="+type,e);
        		}
    		}
			
			if(count == commit_count){
				con.commit();
				commit_count += 1000;
			}
    	}
    	
    	if(ps != null) ps.close();
    }
	public static void ins2Enr(Connection con, ResultSet rs, long child_ent_id, String type) throws SQLException{
		dbEntityRelation dber = new dbEntityRelation();
		dber.ern_child_ent_id = child_ent_id;
		dber.ern_type = type;
		dber.ern_syn_timestamp = rs.getTimestamp("gpm_syn_date");
		dber.ern_remain_on_syn = rs.getBoolean("gpm_remain_on_syn");
		dber.ern_create_timestamp = rs.getTimestamp("gpm_create_timestamp");
		dber.ern_create_usr_id = rs.getString("gpm_create_usr_id");
		
		String ancester = rs.getString("gpm_ancester").trim();
		int index = -1;
		int order = 1;
		
		while((index = ancester.indexOf(",")) > -1){
			long ancester_id = Long.parseLong(ancester.substring(0,index).trim());
			dber.ern_order = order++;
			dber.ern_ancestor_ent_id = ancester_id;
			dber.ern_parent_ind = false;
			dber.ins(con, "");
			ancester = ancester.substring(index+1);
		}
		
		long ancester_id = Long.parseLong(ancester.trim());
		dber.ern_order = order;
		dber.ern_ancestor_ent_id = ancester_id;
		dber.ern_parent_ind = true;
		
		dber.ins(con, "");
	}
	public static void ins2EnrHis(Connection con, ResultSet rs, Timestamp gpm_end_timestam, long child_ent_id, String type) throws SQLException{
		
		dbEntityRelationHistory dbErh = new dbEntityRelationHistory();
		dbErh.erh_child_ent_id = child_ent_id;
		dbErh.erh_type = type;
		dbErh.erh_start_timestamp = rs.getTimestamp("gpm_start_timestamp");
		dbErh.erh_end_timestamp = gpm_end_timestam;
		dbErh.erh_create_timestamp = gpm_end_timestam;
		dbErh.erh_create_usr_id = rs.getString("gpm_create_usr_id");
		
		String ancester = rs.getString("gpm_ancester").trim();
		int index = -1;
		int order = 0;
		while((index = ancester.indexOf(",")) > -1){
			long ancester_id = Long.parseLong(ancester.substring(0,index).trim());
			dbErh.erh_order = ++order;
			dbErh.erh_ancestor_ent_id = ancester_id;
			dbErh.erh_parent_ind = false;
			dbErh.ins(con);
			ancester = ancester.substring(index+1);
		}
		
		long ancester_id = Long.parseLong(ancester.trim());
		dbErh.erh_order = ++order;
		dbErh.erh_ancestor_ent_id = ancester_id;
		dbErh.erh_parent_ind = true;
		dbErh.ins(con);
	}
}
