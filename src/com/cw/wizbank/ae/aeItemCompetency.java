package com.cw.wizbank.ae;

import java.sql.*;
import java.util.*;
import com.cw.wizbank.db.view.ViewCmSkill;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.ae.db.*;
import com.cw.wizbank.util.*;

public class aeItemCompetency {
    
    
    
    
    /**
    Get the item skills relation as XML
    @param long value of item id
    @param sort_by ASC/DESC
    @param order_by skl_skb_id(skill id), skl_title(skill title), itc_skl_level(skill level of the item)
    @return String of XML
    */
    public String asXML(Connection con, long itm_id, String sort_by, String order_by)
        throws SQLException {
            
            ViewCmSkill viewSkill = new ViewCmSkill();
            ViewCmSkill.ViewItemSkill[] itmSkill = viewSkill.getItemSkills(con, itm_id, sort_by, order_by);
            StringBuffer xmlBody = new StringBuffer();
            xmlBody.append("<competency>").append(dbUtils.NEWL)
                   .append("<skill_list>").append(dbUtils.NEWL);
            for (int i = 0; i < itmSkill.length; i++) {
                xmlBody.append("<skill id=\"").append(itmSkill[i].skb_id).append("\" ")
                       .append(" level=\"").append(itmSkill[i].itc_skl_level).append("\" ")
                       .append(" title=\"").append(cwUtils.esc4XML(aeUtils.escNull(itmSkill[i].skb_title))).append("\"/>")
                       .append(dbUtils.NEWL);            
            }
            xmlBody.append("</skill_list>").append(dbUtils.NEWL)
                   .append("</competency>").append(dbUtils.NEWL);
            
            return xmlBody.toString();
            
        }
    
    
    /**
    Get the item related skills as the XML format
    @param hashtable containing the skill id and the require level
    @param user entity id used to show the enrollment status of the item
    @param sort by ASC/DESC
    @return String of XML
    */
    public String itmSkillXML(Connection con, Hashtable skillLevel, long usr_ent_id, String sort_by)
        throws SQLException {

            StringBuffer xmlBody = new StringBuffer();
            Enumeration enumeration = skillLevel.keys();
            Vector skillIdVec = new Vector();
            while( enumeration.hasMoreElements() )
                skillIdVec.addElement( enumeration.nextElement() );
            
            DbItemCompetency dbItmCm = new DbItemCompetency();
            ResultSet rs = dbItmCm.getItmSkillRelation(con, skillIdVec);
            
            long sklLevel;
            Long sklId;
            Vector itmIdVec = new Vector();
            while( rs.next() ) {
                
                sklId = new Long(rs.getLong("itc_skl_skb_id"));
                sklLevel = rs.getLong("itc_skl_level");
                if( sklLevel >= ((Float)skillLevel.get(sklId)).floatValue() )
                    itmIdVec.addElement(new Long(rs.getLong("itc_itm_id")));

            }
            rs.close();
            
            
            
            
            xmlBody.append("<course_recommend>").append(cwUtils.NEWL);
            if( !itmIdVec.isEmpty() ) {
                
                aeApplication aeApp = new aeApplication();
                Hashtable itmStatus = aeApp.getUserItemStatus(con, itmIdVec, usr_ent_id);
                Long itmId;
                String status;

                String sql_get_items_title = " SELECT itm_id, itm_title FROM aeItem "
                                            + " WHERE itm_id IN "
                                            + cwUtils.vector2list(itmIdVec)
                                            + "  ORDER BY itm_id  "
                                            + sort_by;
                PreparedStatement stmt2 = null;
                ResultSet rs2 = null;
                try {
                    stmt2 = con.prepareStatement(sql_get_items_title);
                    rs2 = stmt2.executeQuery();
                    while( rs2.next() ) {
                        
                        itmId = new Long(rs2.getLong("itm_id"));
                        xmlBody.append("<course id=\"").append(itmId).append("\" ");
                        status = (String)itmStatus.get(itmId);
                        if( status != null )
                            xmlBody.append(" appn_status=\"").append(status).append("\" ");
                        else
                            xmlBody.append(" appn_status=\"--\" ");
                        xmlBody.append(" title=\"").append(cwUtils.esc4XML(rs2.getString("itm_title"))).append("\"/>")
                               .append(cwUtils.NEWL);
                                    
                    }
                } finally {
                    if (stmt2 != null) {
                        stmt2.close();
                    }
                }
            }
            xmlBody.append("</course_recommend>").append(cwUtils.NEWL);
            
            return xmlBody.toString();
            
        }
    
    
    
    
    
}