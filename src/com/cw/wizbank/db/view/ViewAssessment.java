package com.cw.wizbank.db.view;

import java.sql.*;
import java.util.*;

import com.cw.wizbank.util.*;
import com.cw.wizbank.db.sql.OuterJoinSqlStatements;
import com.cwn.wizbank.utils.CommonLog;

public class ViewAssessment {

    private final static String RUNNING         =   "RUNNING";
    

    /**
     * Get Assessment List
     * @param database connection
     * @param root entity id (organization id)
     * @param assessment mode to be shown
     * @param assessment id to be shown
     * @param sort column in the list
     * @param sort order in the list
     * @param start time
     * @param end time
     * @return result set of required field
     */
    public static ResultSet getAsmList(Connection con, long root_ent_id, String[] asm_modes, Vector asmIdVec, String sort_col, String sort_order, Timestamp start_datetime, Timestamp end_datetime)
        throws SQLException, cwException {
    	CommonLog.debug("Get asm mode");
		  String SQL = OuterJoinSqlStatements.viewAssessmentGetAsmList();
//" SELECT res_id, res_title, usr_display_bil, mod_eff_start_datetime, mod_eff_end_datetime, asm_mode, asm_type, tpl_stylesheet, mod_max_usr_attempt "
//					   + " FROM Resources, Module, Assessment, RegUser, Template "
//					   + " WHERE asm_res_id = res_id AND asm_res_id = mod_res_id "
//					   + " AND tpl_name " + cwSQL.get_right_join(con) + " res_tpl_name "
//					   + " AND res_status = ? "
//					   + " AND res_usr_id_owner = usr_id AND usr_ste_ent_id = ? ";
                       if( asmIdVec != null && !asmIdVec.isEmpty() )
                            SQL += " AND asm_res_id IN " + cwUtils.vector2list(asmIdVec);
                       if( asm_modes != null && asm_modes.length > 0 ) {
                            SQL += " AND asm_mode IN ( ? ";
                            for(int i=1; i<asm_modes.length; i++)
                                SQL += " ,? ";
                            SQL += " ) ";
                       }
                        if( start_datetime != null && end_datetime != null ) {
                            SQL += " AND (   ( mod_eff_start_datetime > ? AND mod_eff_start_datetime < ? ) "
                                + "      OR  ( mod_eff_end_datetime > ? AND mod_eff_end_datetime < ? ) ";
                                //if( asm_modes == null )
                            SQL += " OR ( asm_mode = ? ) ";
                            SQL += " ) ";
                        }
                        else if( start_datetime != null ) {
                            SQL += " AND ( ( mod_eff_start_datetime > ? OR mod_eff_end_datetime > ? ) ";
                            if( asm_modes == null )
                                SQL += " OR ( asm_mode = ? ) ";
                            SQL += " ) ";    
                        }
                        else if( end_datetime != null ) {
                            SQL += " AND ( ( mod_eff_start_datetime < ? OR mod_effend_datetime < ? ) ";
                            if( asm_modes == null )
                                SQL += " OR ( asm_mode = ? ) ";
                            SQL += " ) ";
                        }
                        
                        if( sort_col != null & sort_col.length() > 0 )
                            SQL += " ORDER BY " + sort_col;
                        else
                            SQL += " ORDER BY res_title ";
                        if( sort_order != null && sort_order.length() > 0 )
                            SQL += " " + sort_order;
                        else
                            SQL += " ASC ";
//System.out.println("SQL = " + SQL);
            PreparedStatement stmt = con.prepareStatement(SQL);
            int index = 1;
            stmt.setString(index++, "ON");
            stmt.setLong(index++, root_ent_id);
            if( asm_modes != null && asm_modes.length > 0 ) {
                stmt.setString(index++, asm_modes[0]);                
                for(int i=1; i<asm_modes.length; i++) {                    
                    stmt.setString(index++, asm_modes[i]);
                }
            }
            if( start_datetime != null && end_datetime != null ) {
                stmt.setTimestamp(index++, start_datetime);
                stmt.setTimestamp(index++, end_datetime);
                stmt.setTimestamp(index++, start_datetime);
                stmt.setTimestamp(index++, end_datetime);
                //if( asm_modes == null )
                stmt.setString(index++, RUNNING);
            } else if( start_datetime != null ) {
                stmt.setTimestamp(index++, start_datetime);
                stmt.setTimestamp(index++, start_datetime);
                if( asm_modes == null )
                    stmt.setString(index++, RUNNING);
            } else if( end_datetime != null ) {
                stmt.setTimestamp(index++, end_datetime);
                stmt.setTimestamp(index++, end_datetime);
                if( asm_modes == null )
                    stmt.setString(index++, RUNNING);
            }
            
            ResultSet rs = stmt.executeQuery();
            return rs;

        }


    /**
     * Get Objective info which assessment selected
     * @param database connection
     * @param assessment id
     * @return result set of the required field
     */
    public static ResultSet getAsmObj(Connection con, long asm_res_id)
        throws SQLException, cwException {

            String SQL = " SELECT obj_id, obj_desc, obj_ancester, msp_qcount, obj_syl_id "
                       + " FROM Objective, ModuleSpec "
                       + " WHERE msp_res_id = ? AND msp_obj_id = obj_id ";

            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, asm_res_id);
            ResultSet rs = stmt.executeQuery();
            return rs;

        }

    /**
    * Get group info which have read, write or exec permission on the resource
    * @param resource id
    * @return Resultset with usg_ent_id, usg_display_bil
    */
        
    public static ResultSet getGroupPermissionInfo(Connection con, long res_id)
        throws SQLException, cwException {
            
            String SQL = " SELECT usg_ent_id, usg_display_bil "
                       + " FROM ResourcePermission, UserGroup , Entity "
                       + " WHERE rpm_ent_id = usg_ent_id "
                       + " AND rpm_res_id = ? And usg_ent_id = ent_id " 
                       + " And ent_delete_usr_id is null";
            
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, res_id);
            ResultSet rs = stmt.executeQuery();
            return rs;
        }


    /**
    * Get user execution permission on the resources
    * @param database connection
    * @param resources id
    * @param groups id
    */
    public static ResultSet getUserExecPermission(Connection con, Vector resIdVec, Vector gpIdVec)
        throws SQLException, cwException {
            
            String SQL = " SELECT rpm_res_id "
                       + " FROM ResourcePermission "                       
                       + " WHERE rpm_ent_id IN " + cwUtils.vector2list(gpIdVec)
                       + " AND rpm_execute = 1 ";

            if (resIdVec!=null && resIdVec.size()!=0){
                SQL += " AND rpm_res_id IN " + cwUtils.vector2list(resIdVec);
            }
            PreparedStatement stmt = con.prepareStatement(SQL);
            ResultSet rs = stmt.executeQuery();
            return rs;
            
        }

        
        
        
    /**
    * Get user execution permission on the resource
    * @param database connection
    * @param resource id
    * @param groups id
    */
    public static boolean getUserExecPermission(Connection con, long res_id, Vector gpIdVec)
        throws SQLException, cwException {
            
            String SQL = " SELECT rpm_res_id "
                       + " FROM ResourcePermission "                       
                       + " WHERE rpm_ent_id IN " + cwUtils.vector2list(gpIdVec)
                       + " AND rpm_res_id = ? "
                       + " AND rpm_execute = 1 ";
            
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, res_id);
            ResultSet rs = stmt.executeQuery();
            boolean flag = false;
            if( rs.next() )
                flag = true;
            stmt.close();
            return flag;
            
        }
        
        
        
    public static long getObjQNum(Connection con, long obj_id, String[] que_type, int difficulty, String privilege)
        throws SQLException, cwException {
        
            String SQL = " SELECT COUNT(*) "
                       + " FROM Objective , ResourceObjective, Resources "
                       + " WHERE ( obj_ancester like ? OR obj_id = ? ) "
                       + " AND res_type = ? "
                       + " AND rob_res_id = res_id AND rob_obj_id = obj_id AND res_res_id_root is null ";
                        if( privilege != null && privilege.length() > 0 )
                            SQL += " AND res_privilege = ? ";
                        if( difficulty > 0 )
                            SQL += " AND res_difficulty = ? ";
                        if( que_type != null && que_type.length > 0 ) {
                            SQL += " AND res_subtype IN ( ? ";
                            for(int i=1; i<que_type.length; i++)
                                SQL += " ,? ";
                            SQL += " ) ";
                        }
//System.out.println("SQL = " + SQL);
            PreparedStatement stmt = con.prepareStatement(SQL);
            int index = 1;
            stmt.setString(index++, "% " + obj_id + " %");
            stmt.setLong(index++, obj_id);
            stmt.setString(index++, "QUE");
            if( privilege != null && privilege.length() > 0 )
                stmt.setString(index++, privilege);
            if( difficulty > 0 )
                stmt.setLong(index++, difficulty);
            
            if( que_type != null && que_type.length > 0 ) {
                stmt.setString(index++, que_type[0]);
                for(int i=1; i<que_type.length; i++)
                    stmt.setString(index++, que_type[i]);
            }
            
            ResultSet rs = stmt.executeQuery();
            long qNum;
            if( rs.next() )
                qNum = rs.getLong(1);
            else
                qNum = 0;
            stmt.close();
            return qNum;
        
        }




    public static String getTplStylesheet(Connection con, long res_id)
        throws SQLException {
            
            String SQL = " select tpl_stylesheet "
                       + " from Resources, Template "
                       + " where res_tpl_name = tpl_name "
                       + " AND res_id = ? ";
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, res_id);
            ResultSet rs = stmt.executeQuery();
            String tpl_stylesheet = new String();
            if(rs.next())
                tpl_stylesheet = rs.getString("tpl_stylesheet");
            stmt.close();
            return tpl_stylesheet;
            
            
        }

}
