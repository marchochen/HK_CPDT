package com.cw.wizbank.db.view;

import java.util.Vector;
import java.util.Hashtable;
import java.sql.*;

import com.cw.wizbank.lcms.LcmsModule;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbAction;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.db.sql.SqlStatements;
import com.cw.wizbank.db.view.ViewAcAbsInstance;

/**
Database Layer Access Control on Login Home Page<BR>
Since HomePage is not an instance, this class does not extend ViewAcInstance
Access Control Table: acHomePage<BR>
*/
public class ViewAcHomePage extends ViewAcAbsInstance {
    
    /**
    Hashtable key used in getGrantedPrivilege()
    */
    public static String FTN_EXT_ID = "FTN_EXT_ID";

    /**
    Hashtable key used in getGrantedPrivilege()
    */
    public static String FTN_XML = "FTN_XML";
    
    /**
    ftn_type of this view
    */
    private static final String FtnType = "HOMEPAGE";
    
    /**
    all the thing need to do in the constructor is to initialize a set of super class variables<BR>
    see ViewAcInstance for details
    */
    public ViewAcHomePage(Connection con) {
        super(con);
        
        //access control table name
        dbTableName = "acHomePage";
        
        //entity id column name of the access control table
        colEntityId = "ac_hom_ent_id";
        
        //role ext id column name of the access control table
        colRoleExtId = "ac_hom_rol_ext_id";
        
        //function ext id column of the access control table
        colFunctionExtId = "ac_hom_ftn_ext_Id";
        
        //create user id column name of the access control table
        colCreateUsrId = "ac_hom_create_usr_id";
        
        //create timestamp column name of the access control table
        colCreateTimestamp = "ac_hom_create_timestamp";
        
        //funtion types of this instance will have
        ftn_types.addElement(FtnType);
    }    
    
    /**
    find out granted home page privilege for a role, return a Hashtable containing 2 Vectors<BR>
    <ul>
    <li>vector of ftn_ext_id
    <li>vector of ftn_xml
    </ul>
    */
   
    
    /**
    find out all home page privilege for a role, return a Hashtable containing 2 Vectors<BR>
    <ul>
    <li>vector of ftn_ext_id
    <li>vector of ftn_xml
    </ul>
    */
    public Hashtable getAllPrivilege(String rol_ext_id) 
        throws SQLException {
        
//        Vector v_ftn_id = new Vector();
//        Vector v_ftn_xml = new Vector();
        Hashtable h_result = new Hashtable();
//        
//        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_all_homepage_privilege);
//        int index = 1;
//        stmt.setString(index++, rol_ext_id);
//        stmt.setString(index++, FtnType);
//        ResultSet rs = stmt.executeQuery();
//        while(rs.next()) {            	
//        String curId = rs.getString("ftn_ext_id");
//	    	
//        	if(curId.equalsIgnoreCase(LcmsModule.FTN_LCMS_READ)//如果是离线助手功能
//	    			&& (!AccessControlWZB.hasRoleFunction(curId, LcmsModule.FTN_LCMS_READ)//企业版才有离线助手功能
//	    			|| !qdbAction.getWizbini().cfgSysSetupadv.getLcms().isEnabled())) {//必须在配置文件setupadv.xml中配置了lcms 为 TRUE
//	    		continue;
//	    	}
//        	if((curId.equalsIgnoreCase(AcHomePage.FTN_COURSE_MAIN)||curId.equalsIgnoreCase(AcHomePage.FTN_EIP_MAIN))&& !qdbAction.wizbini.cfgSysSetupadv.isTcIndependent()) {
//        		continue;
//            }
//        	//String curId = rs.getString("ftn_ext_id");
//            v_ftn_id.addElement(curId);
//            v_ftn_xml.addElement(getFunctionXml(curId));
//       }
//       stmt.close();
//       h_result.put(FTN_EXT_ID,v_ftn_id);
//       h_result.put(FTN_XML,v_ftn_xml);
       return h_result;
    }
    
    /**
     combine ftn_xml by ftn_ext_id
     */
    public static String getFunctionXml(String ftn_ext_id) {
    	String ftn_xml = "<function id=\"" + cwUtils.escNull(ftn_ext_id) + "\"/>";
    	return ftn_xml;
    }
    
}