package com.cw.wizbank.ae;

import java.sql.*;
import java.util.*;
import com.cw.wizbank.qdb.*;
import com.cw.wizbank.util.*;
import com.cw.wizbank.ae.db.DbAppnTargetEntity;
/**
*Assign the application to the manager(s) in the specified role(s)
*/    
public class aeAppnTargetEntity{
    private static final String ENROLLED_ALREADY = "ENR001";
    
    /*
    *Insert recored(s) to the db table
    *@param app_id application id
    *@param entIdRole The assigned manager(s) in the specified role(s),
    *                 approver user entity id as a key, role_ext_id in string vector as a value
    *@param prof The user profile used to create the record(s)
    */
    public void ins(Connection con, long app_id, Hashtable entIdRole, loginProfile prof)
        throws SQLException, cwSysMessage, cwException {
            
            DbAppnTargetEntity dbAte = new DbAppnTargetEntity();
            dbAte.ate_app_id = app_id;
            dbAte.ate_create_usr_id = prof.usr_id;
            dbAte.ate_create_timestamp = cwSQL.getTime(con);
            Enumeration enumeration = entIdRole.keys();
            
            //Loop the keys(approver user entity id) in hashtable
            while(enumeration.hasMoreElements()){
                dbAte.ate_usr_ent_id = ((Long)enumeration.nextElement()).longValue();
                Vector roleVec = (Vector)entIdRole.get(new Long(dbAte.ate_usr_ent_id));
                if( !roleVec.isEmpty() ){
                    //insert a record of the approver for each role specified
                    for(int i=0; i<roleVec.size(); i++){
                        dbAte.ate_rol_ext_id = (String)roleVec.elementAt(i);
                        if(dbAte.isExist(con)) {
                            throw new cwSysMessage(ENROLLED_ALREADY);
                        }
                        dbAte.ins(con);
                    }
                }
            }
            return;
        }

    /*
    *Get the target entities of an application
    *@param app_id application id
    *@param entIdRole The assigned manager(s) in the specified role(s),
    *                 approver user entity id as a key, role_ext_id in string vector as a value
    *
    */
    private static final String sql_get_appn_target_ent_ids = 
        " Select ate_usr_ent_id From aeAppnTargetEntity Where ate_app_id = ? and ate_rol_ext_id = ? order by ate_usr_ent_id asc ";

    public static Vector getAppnTargetEntIds(Connection con, long app_id, String rol_ext_id)
        throws SQLException, cwException {
            
        PreparedStatement stmt = con.prepareStatement(sql_get_appn_target_ent_ids);
        stmt.setLong(1, app_id);
        stmt.setString(2, rol_ext_id);
        ResultSet rs = stmt.executeQuery();
        Vector entVec = new Vector();
        while (rs.next()) {
            entVec.addElement(new Long(rs.getLong("ate_usr_ent_id")));
        }
        rs.close();
        stmt.close();
        return entVec;

    }
    
    /*
    *Get the current approvers of an item
    *@param itm_id item id
    *@param rol_ext_id The assigned manager(s) in the specified role(s),
    *                 approver user entity id as a key, role_ext_id in string vector as a value
    *
    */
    private static final String sql_get_approver = 
        " select distinct (ate_usr_ent_id) from aeappntargetentity, aeApplication "
    +   " Where ate_app_id = app_id and app_itm_id= ? and ate_rol_ext_id = ? " ;

    public static Vector getApproverIds(Connection con, long itm_id, String rol_ext_id) throws SQLException{
        PreparedStatement stmt = con.prepareStatement(sql_get_approver);
        stmt.setLong(1, itm_id);
        stmt.setString(2, rol_ext_id);
        Vector entVec = new Vector();
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            entVec.addElement(new Long(rs.getLong("ate_usr_ent_id")));
        }
        rs.close();
        stmt.close();
        return entVec;
    
    }
        
}