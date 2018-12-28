package com.cw.wizbank.ae.db.view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Vector;

import com.cw.wizbank.ae.db.DbItemRequirement;
import com.cw.wizbank.ae.db.sql.OuterJoinSqlStatements;
import com.cw.wizbank.util.cwSQL;

public class ViewItemRequirement {

    public ViewItemRequirement() {;}

    public DbItemRequirement ire;
    public String   pos_actn_type;
    public String   neg_actn_type;
    
    /* aeItemRequirement.java
    Get the requirements that are not processed and the due date is passed
    @param checkPoint get the record which due date is before this check point
    */
    public Vector getOverdueItemReq(Connection con, Timestamp checkPoint, String actionType) throws SQLException {
        Vector itemVec = new Vector();
        PreparedStatement stmt = null;
        try{
            String select_overdue_item = OuterJoinSqlStatements.getOverdueItemReq();
            stmt = con.prepareStatement(select_overdue_item);
            stmt.setString(1, actionType);
            stmt.setString(2, actionType);
            stmt.setTimestamp(3, checkPoint);
            
            ResultSet rs = stmt.executeQuery();
           
            while (rs.next()) {
                ViewItemRequirement vire = new ViewItemRequirement();
                vire.ire = new DbItemRequirement();
                vire.ire.itrItmId = rs.getLong("itr_itm_id");
                vire.ire.itrOrder = rs.getLong("itr_order");
                vire.ire.itrRequirementDueDate = rs.getTimestamp("itr_requirement_due_date");
                vire.ire.itrConditionType = rs.getString("itr_condition_type");
                vire.ire.itrConditionRule = cwSQL.getClobValue(rs, "itr_condition_rule");
                vire.ire.itrPositiveIatId = rs.getLong("itr_positive_iat_id");
                vire.ire.itrNegativeIatId = rs.getLong("itr_negative_iat_id");
                vire.pos_actn_type = rs.getString("POSTYPE");
                vire.neg_actn_type = rs.getString("NEGTYPE");
                itemVec.addElement(vire);
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return itemVec;
    }
    
    private static String sql = "select Distinct itm_title,itm_code from aeItemRequirement,aeItem where itr_itm_id = itm_id and itr_condition_rule like ";
    public static List isWhoseReq(Connection con,long itm_id){
    	List list = new Vector();
    	PreparedStatement pst = null;
    	ResultSet rs = null;
    	try{
    		String s = sql + " '% ITM_" + itm_id + " %' ";
    		pst = con.prepareStatement(s);
    		rs = pst.executeQuery();
    		while(rs.next()){
                String tempStr = rs.getString("itm_title") + "(" + rs.getString("itm_code") +")";
    			list.add(tempStr);
    		}
    	}catch(SQLException e){
    		throw new RuntimeException(e.getMessage());
    	}finally{
    		cwSQL.cleanUp(rs,pst);
    	}
    	return list;
    }
}