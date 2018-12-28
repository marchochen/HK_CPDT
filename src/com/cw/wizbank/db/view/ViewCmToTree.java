package com.cw.wizbank.db.view;

import java.util.Vector;
import java.util.Hashtable;
import java.sql.*;

import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.util.*;
import com.cw.wizbank.competency.CmSkillSetManager;
import com.cw.wizbank.db.DbCmSkillBase;
import com.cw.wizbank.db.DbTrainingCenter;

public class ViewCmToTree {
    public class skillInfo {
        public long id;
        public String title;
        public String type;
    }
    
    public Vector skillListAsVector(Connection con, long skb_id) throws SQLException {
        Vector lst = new Vector();
        StringBuffer sql = new StringBuffer();

        sql.append(" select skb_id, skb_title, skb_type ")
           .append(" from cmSkillBase ")
           .append(" where skb_parent_skb_id = ? ")
           .append(" and skb_delete_timestamp is null ")//filter the deleted skill
           .append(" order by skb_type asc, skb_order asc, skb_title asc");

        PreparedStatement stmt = con.prepareStatement(sql.toString());
        stmt.setLong(1, skb_id);

        ResultSet rs = stmt.executeQuery();
        skillInfo info;
        
        while (rs.next()) {
            info = new skillInfo();
            info.id = rs.getLong("skb_id");
            info.title = rs.getString("skb_title");
            info.type = rs.getString("skb_type");
            lst.addElement(info);            
        }
        
        stmt.close();                
        return lst;
    }


	public Hashtable SkillFolderChildCount(Connection con, Vector lst) throws SQLException {
		StringBuffer str = new StringBuffer();
		StringBuffer sql = new StringBuffer();
		Vector result = new Vector();
		skillInfo info;
        
		str.append("(0 ");
        
		for (int i=0; i<lst.size(); i++) {
			info = (skillInfo)lst.elementAt(i);  
			str.append(", ").append(info.id);
		}
        
		str.append(")");
        
		sql.append(" Select skb_parent_skb_id, Count(*) ")
		   .append(" from cmSkillBase ")
		   .append(" where skb_parent_skb_id in ").append(str.toString())
		   .append(" and skb_delete_timestamp is null ")//filter the deleted skill
		   .append(" Group BY skb_parent_skb_id ");
           
		PreparedStatement stmt = con.prepareStatement(sql.toString());
		ResultSet rs = stmt.executeQuery();
		Hashtable h_child_count = new Hashtable();
		while(rs.next()){
			h_child_count.put(new Long(rs.getLong("skb_parent_skb_id")), new Integer(rs.getInt(2)));
		}
		stmt.close();
		return h_child_count;

	}

    public Vector SkillFolderHasChild(Connection con, Vector lst) throws SQLException {
        StringBuffer str = new StringBuffer();
        StringBuffer sql = new StringBuffer();
        Vector result = new Vector();
        skillInfo info;
        
        str.append("(0 ");
        
        for (int i=0; i<lst.size(); i++) {
            info = (skillInfo)lst.elementAt(i);  
            str.append(", ").append(info.id);
        }
        
        str.append(")");
        
        sql.append("select distinct skb_parent_skb_id as skb_id ")
           .append(" from cmSkillBase ")
           .append(" where skb_parent_skb_id in ").append(str.toString())
           .append(" and skb_delete_timestamp is null ");//filter the deleted skill
           
        PreparedStatement stmt = con.prepareStatement(sql.toString());
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            long id = rs.getLong("skb_id");
            
            for (int i=0; i<lst.size(); i++) {
                info = (skillInfo)lst.elementAt(i);
                
                if (info.id == id) {
                    result.addElement(info);
                }
            }
        }
        
        stmt.close();
        return result;
    }

    public Vector SkillRootListAsVector(Connection con, long org_ent_id) throws SQLException {
        Vector lst = new Vector();
        String SQL = " select skb_id, skb_title, skb_type "
                   + " from cmSkillBase where skb_parent_skb_id is null "
                   + " and skb_owner_ent_id = ? "
                   + " and skb_delete_timestamp is null "//filter the deleted skill
                   + " order by skb_type asc, skb_order asc, skb_title asc";
        
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, org_ent_id);
        ResultSet rs = stmt.executeQuery();
        skillInfo info;
        
        while (rs.next()) {
            info = new skillInfo();
            info.id = rs.getLong("skb_id");
            info.title = rs.getString("skb_title");
            info.type = rs.getString("skb_type");
            lst.addElement(info);
        }

        stmt.close();
        return lst;        
    }
    
    public String getSkills(Connection con, String node_id_lst, String node_type_lst, long root_ent_id) throws SQLException, cwException{
        StringBuffer result = new StringBuffer();
        Vector node_id_vec  = cwUtils.splitToVec(node_id_lst, "~");
        Vector node_type_vec = cwUtils.splitToVecString(node_type_lst, "~");
        Vector skill_id_vec = new Vector();
        
        if (node_id_vec.size() != node_type_vec.size()) {
            throw new cwException("# of ids doesn't match with # of types");
        }

        for (int i=0; i<node_id_vec.size(); i++) {
            if (((String)node_type_vec.elementAt(i)).equals(DbCmSkillBase.COMPETENCY_COMPOSITE_SKILL)) {
                skill_id_vec.addElement((Long)node_id_vec.elementAt(i));
            } else if (((String)node_type_vec.elementAt(i)).equals("ROOT_0_0_0")) {
                Vector rootList = SkillRootListAsVector(con, root_ent_id);
                
                for (int j=0; j<rootList.size(); j++) {
                    skillInfo info = (skillInfo)rootList.elementAt(j);
                    getSkillsHelper(con, info.id, skill_id_vec);
                }
            } else {
                getSkillsHelper(con, ((Long)node_id_vec.elementAt(i)).longValue(), skill_id_vec);
            }
        }

        cwUtils.removeDuplicate(skill_id_vec);
//System.out.println("skill ids = " + cwUtils.vector2list(skill_id_vec));
        PreparedStatement stmt = con.prepareStatement("select skb_id, skb_title, skb_type from cmSkillBase where skb_id in " + cwUtils.vector2list(skill_id_vec) + " order by skb_title");
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            result.append(rs.getLong("skb_id")).append(":_:_:").append(rs.getString("skb_title")).append(":_:_:").append(rs.getString("skb_type")).append(":_:_:");
        }
        
        stmt.close();

        return result.toString();
    }

    public void getSkillsHelper(Connection con, long node_id, Vector skill_id_vec) throws SQLException, cwException {
        Vector skill_list = skillListAsVector(con, node_id);
        
        for (int i=0; i<skill_list.size(); i++) {
            skillInfo info = (skillInfo)skill_list.elementAt(i);
            
            if (info.type.equals(DbCmSkillBase.COMPETENCY_GROUP)) {
                getSkillsHelper(con, info.id, skill_id_vec);
            } else {
                skill_id_vec.addElement(new Long(info.id));
            }
        }
    }
    
    public Vector skpListAsVector(Connection con, loginProfile prof, boolean tc_independent) throws SQLException {
    	Vector skp = new Vector();
        StringBuffer sql = new StringBuffer();
        sql.append(" select upt_id, upt_title ")
           .append(" from UserPosition ")
           .append(" where 1 = 1 ");

        	sql.append(" and (upt_tcr_id in (select tcn_child_tcr_id from tcRelation inner join tcTrainingCenter on (tcr_id = tcn_child_tcr_id) where tcn_ancestor = ? and tcr_status = ? ")
        	.append(") or upt_tcr_id =?) order by upt_title");
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
        	stmt = con.prepareStatement(sql.toString());
	        	int index = 1;
	        	stmt.setLong(index++, prof.my_top_tc_id);
	        	stmt.setString(index++, DbTrainingCenter.STATUS_OK);
	        	stmt.setLong(index++, prof.my_top_tc_id);
	        rs = stmt.executeQuery();
	        skillInfo info;        
	        while (rs.next()) {
	            info = new skillInfo();
	            info.id = rs.getLong("upt_id");
	            info.title = rs.getString("upt_title");
	            info.type = "SKP";
	            skp.addElement(info);            
	        }        
	        stmt.close();
        } finally {
        	cwSQL.cleanUp(rs, stmt);
        }
    	return skp;
    }
}
