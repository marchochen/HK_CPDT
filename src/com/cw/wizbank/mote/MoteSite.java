package com.cw.wizbank.mote;
import java.sql.*;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.util.cwSQL;

public class MoteSite{
    public long ims_ent_id_root;
    public int ims_attend_ind;
    public int ims_rating_ind;
    public int ims_cost_budget_ind;
    public int ims_time_budget_ind;
    public int ims_comment_ind;
    public String ims_comment_xml;
    
    public void getByRoot(Connection con) throws SQLException{
        String SQL = "SELECT ims_attend_ind , ims_rating_ind , ims_cost_budget_ind , ims_time_budget_ind , ims_comment_ind , ims_comment_xml "
                + " FROM aeItemMoteSite WHERE ims_ent_id_root = ? ";
        
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, ims_ent_id_root);        
        
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()){
            ims_attend_ind = rs.getInt("ims_attend_ind");
            ims_rating_ind = rs.getInt("ims_rating_ind");
            ims_cost_budget_ind = rs.getInt("ims_cost_budget_ind");
            ims_time_budget_ind = rs.getInt("ims_time_budget_ind");
            ims_comment_ind = rs.getInt("ims_comment_ind");
            //ims_comment_xml = rs.getString("ims_comment_xml");
            ims_comment_xml=cwSQL.getClobValue(rs,"ims_comment_xml");
        }
        stmt.close();
        
    }
    
    public boolean bit2Boolean(int theBit){
        if (theBit == 0){
            return false;     
        }else{
            return true;     
        }    
    }
    
    public StringBuffer siteAttrAsXML(){
        StringBuffer result = new StringBuffer(250);
        result.append("<mote_attribute_list ");
        result.append(" hasAttendance=\"").append(bit2Boolean(ims_attend_ind));
        result.append("\" hasRating=\"").append(bit2Boolean(ims_rating_ind));
        result.append("\" hasCostBudget=\"").append(bit2Boolean(ims_cost_budget_ind));
        result.append("\" hasTimeBudget=\"").append(bit2Boolean(ims_time_budget_ind));
        result.append("\" >").append(cwUtils.NEWL);
        result.append(ims_comment_xml);
        result.append("</mote_attribute_list>");
//        System.out.println("moteSite L53:" + result.length());

        return result;
    }
        
}

    
    
    
    
