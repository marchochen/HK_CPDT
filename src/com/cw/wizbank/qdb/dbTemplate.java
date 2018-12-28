package com.cw.wizbank.qdb;

import java.sql.*;

import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.db.DbTrainingCenter;
import com.cw.wizbank.db.view.ViewTrainingCenter;
import com.cw.wizbank.util.cwUtils;

public class dbTemplate
{
    public String    tpl_name; 
    public String    tpl_lan;
    public String    tpl_desc;
    public String    tpl_note;
    public String    tpl_type; 
    public String    tpl_thumbnail_url;
    public String    tpl_stylesheet;
    
    public dbTemplate () {;}
    
    
    public void get(Connection con)
            throws qdbException
    {
        try {
            PreparedStatement stmt = con.prepareStatement(
            "SELECT  " 
            + " tpl_desc, "           
            + " tpl_note, "
            + " tpl_type, "
            + " tpl_thumbnail_url, "
            + " tpl_stylesheet "
            + " from Template "
            + " where tpl_name = ?  "
            + "   and tpl_lan = ? ");
            
            // set the values for prepared statements
            stmt.setString(1, tpl_name);
            stmt.setString(2, tpl_lan);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                tpl_desc    = rs.getString("tpl_desc"); 
                tpl_note    = rs.getString("tpl_note"); 
                tpl_type    = rs.getString("tpl_type"); 
                tpl_thumbnail_url   = rs.getString("tpl_thumbnail_url"); 
                tpl_stylesheet      = rs.getString("tpl_stylesheet"); 
            }
            else
            {
            	stmt.close();
                throw new qdbException( "No data for template. name = " + tpl_name );
            }
            
            stmt.close();    
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }
    }
    
    
    private String asXML()
    {
        String result = ""; 
        
        result = "<template name=\"" + tpl_name + "\">" + dbUtils.NEWL ; 
        result += "<desc>" + dbUtils.esc4XML(tpl_desc) + "</desc>" + dbUtils.NEWL ; 
        result += "<note>" + dbUtils.esc4XML(tpl_note) + "</note>" + dbUtils.NEWL ; 
        result += "<type>" + tpl_type + "</type>" + dbUtils.NEWL ; 
        result += "<url>" + tpl_thumbnail_url + "</url>" + dbUtils.NEWL ; 
        result += "<stylesheet>" + tpl_stylesheet + "</stylesheet>" + dbUtils.NEWL ; 
        result += "</template>" + dbUtils.NEWL ; 
        
        return result ; 
    }
    
    
    // Get the display option 
    
    public String getDisplayOption(Connection con, String view)
            throws qdbException
    {
        String xml = "";
        dbDisplayOption dpo = new dbDisplayOption();
        dpo.dpo_res_id = 0;
        dpo.dpo_res_type = dbResource.RES_TYPE_MOD;
        dpo.dpo_res_subtype = tpl_type;
        dpo.dpo_view = view;
        //xml = dpo.allViewAsXML(con);
        xml = dpo.getViewAsXML(con);
        return xml;
        
    }
    
    
    public String tplListAsXML(Connection con , loginProfile prof, long cosId, String dpo_view)
        throws qdbException, SQLException
    {
        return tplListAsXML(con, prof, cosId, dpo_view, null, null , null);
    }
    
    public String tplListAsXML(Connection con, loginProfile prof, long cosId, String dpo_view, String type, String subtype, String extendedXML)
			throws qdbException, SQLException {
		return tplListAsXML(con, prof, cosId, dpo_view, type, subtype, extendedXML, false,null, 0L);
	}
    
    public String tplListAsXML(Connection con , loginProfile prof, long cosId, String dpo_view, String type, String subtype , String extendedXML, boolean tcEnabled,String cos_type, Long res_tcr_id)
        throws qdbException, SQLException
    {
        StringBuffer result = new StringBuffer(); 
        Timestamp cur_time = dbUtils.getTime(con);

        result.append(dbUtils.xmlHeader);
        
        result.append("<template_list>").append(dbUtils.NEWL);
        
        // author's information
        result.append(prof.asXML());
        dbCourse dbcourse = new dbCourse();
        dbcourse.res_id = cosId;
        // author's email
        dbRegUser user = new dbRegUser();
        user.usr_id = prof.usr_id;
        user.usr_ent_id = prof.usr_ent_id;
        user.get(con);
        result.append("<email>").append(dbUtils.esc4XML(user.usr_email)).append("</email>").append(dbUtils.NEWL);
        result.append("<cur_time>").append(cur_time).append("</cur_time>").append(dbUtils.NEWL);
        result.append("<cos_type>").append(aeItem.getItemTypeByCosId( con, cosId)).append("</cos_type>");
        result.append("<type>").append(type).append("</type>");
        result.append("<subtype>").append(subtype).append("</subtype>");
        long tcr_id = 0;
        if(cosId>0){
        	tcr_id = dbCourse.getCosItmTcrId(con, cosId);
        }else{
        	tcr_id = ViewTrainingCenter.getDefaultTc(con, prof);
        }
        result.append("<training_center id=\"").append(res_tcr_id).append("\"/>");
        result.append(tplListContentXML(con, prof, tpl_type));
        result.append(dbCourse.getInstructorList(con, cosId, null, prof.root_ent_id));
        result.append(getDisplayOption(con, dpo_view));
        result.append(dbcourse.getIsEnrollmentRelatedAsXML(con));
        if (extendedXML!=null){
			result.append(extendedXML);
        }
        
        //tc_enabled
        if(tcEnabled) {
        	result.append("<tc_enabled>").append(tcEnabled).append("</tc_enabled>").append(dbUtils.NEWL);
        }
        
        //get default tc and tc of module, for module public evaluation survey
        if(dbModule.MOD_TYPE_FOR.equalsIgnoreCase(type) && dbModule.MOD_TYPE_EVN.equalsIgnoreCase(subtype)) {
        	//default tc
        	/*long modTcrId = 0;
        	if(tcEnabled) {
        		modTcrId = ViewTrainingCenter.getDefaultTc(con, prof);
            } else {
            	modTcrId =DbTrainingCenter.getSuperTcId(con, prof.root_ent_id);
            }*/
        	DbTrainingCenter tcr = DbTrainingCenter.getInstance(con, res_tcr_id);
        	if(tcr != null) {
            	StringBuffer xmlBuf = new StringBuffer();
                xmlBuf.append("<default_training_center id =\"").append(tcr.getTcr_id()).append("\">");
                xmlBuf.append("<title>").append(cwUtils.esc4XML(tcr.getTcr_title())).append("</title>");
                xmlBuf.append("</default_training_center>");
                result.append(xmlBuf.toString());
            }
        }
        
        result.append("</template_list>").append(dbUtils.NEWL);
        return result.toString(); 
            
    }

    public static String getOneTpl(Connection con, loginProfile prof, String type) throws qdbException {
        try {
            dbTemplate tpl = new dbTemplate(); 
            String result = ""; 

            String SQL = " SELECT tpl_name "
                         + " FROM Template WHERE tpl_lan = ? " ; 
            
            //if (!type.equalsIgnoreCase("0") && type.length() != 0) {
            if (type != null && type.length() != 0) {
                SQL += " AND tpl_type= ? " ; 
            }
            
            SQL += " order by tpl_name ASC " ; 
            
            PreparedStatement stmt = con.prepareStatement(SQL); 
            
            stmt.setString(1,prof.label_lan);
            
            if (!type.equalsIgnoreCase("0") && type.length() != 0) {
                stmt.setString(2,type); 
            }
            
            ResultSet rs = stmt.executeQuery(); 
            if (rs.next()) 
                tpl.tpl_name = rs.getString("tpl_name");
            
            stmt.close();
            return tpl.tpl_name; 
            
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());   
        }      
    }

    public static String tplListContentXML(Connection con, loginProfile prof, String type)
        throws qdbException
    {
        
        try {
            dbTemplate tpl = new dbTemplate(); 
            String result = ""; 

            String SQL = " SELECT tpl_name "
                         + "      ,tpl_desc, tpl_note,  tpl_type "
                         + "      ,tpl_thumbnail_url,tpl_stylesheet "
                         + " FROM Template WHERE tpl_lan = ? " ; 
            
            //if (!type.equalsIgnoreCase("0") && type.length() != 0) {
            if (type != null && type.length() != 0) {
                SQL += " AND tpl_type= ? " ; 
            }
            
            SQL += " order by tpl_name ASC " ; 
            
            PreparedStatement stmt = con.prepareStatement(SQL); 
            
            stmt.setString(1,prof.label_lan);
            
            if (!type.equalsIgnoreCase("0") && type.length() != 0) {
                stmt.setString(2,type); 
            }
            
            ResultSet rs = stmt.executeQuery(); 
            while (rs.next()) {
                tpl.tpl_name = rs.getString("tpl_name");
                tpl.tpl_desc = rs.getString("tpl_desc"); 
                tpl.tpl_note = rs.getString("tpl_note"); 
                tpl.tpl_type = rs.getString("tpl_type"); 
                tpl.tpl_thumbnail_url = rs.getString("tpl_thumbnail_url"); 
                tpl.tpl_stylesheet = rs.getString("tpl_stylesheet"); 
                result += tpl.asXML(); 
            }
            
            stmt.close();
            return result; 
            
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());   
        }      
    }


}