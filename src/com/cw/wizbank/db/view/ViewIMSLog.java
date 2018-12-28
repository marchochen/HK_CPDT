package com.cw.wizbank.db.view;

import java.util.*;
import java.sql.*;

import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.db.DbIMSLog;

public class ViewIMSLog {
    public static class ViewUsrIMSLog{
        public String usr_display_bil;
        public DbIMSLog dbIMSLog;
        
        public ViewUsrIMSLog(){
            dbIMSLog = new DbIMSLog();
        }
    }
    
    
    
    public static ViewUsrIMSLog[] getHistory(Connection con, String[] ilg_process, String ilg_type, Vector v_ilg_id, loginProfile prof, String sort_col, String sort_order, long itm_id)
        throws SQLException {
            Vector vTmp = new Vector();
            
            if( sort_order == null || sort_order.length() == 0 )
                sort_order = "DESC";
            if( sort_col == null || sort_col.length() == 0 )
                sort_col = "ilg_create_timestamp";
                
            
            String SQL = " SELECT usr_display_bil, ilg_id, ilg_process, ilg_type, ilg_filename, ilg_desc, ilg_create_timestamp, ilg_method " 
                       + " FROM RegUSer, IMSLog "
                       + " WHERE ilg_create_usr_id = usr_id "
                       + " AND ilg_type = ? ";
                       //如果当前不是系统管理员则需要按上传的人来查看
                       if(!prof.current_role.equals("SADM"))
                       {
                    	   SQL+= " AND usr_ste_ent_id = ? AND ilg_tcr_id=? ";
                       }
            		   
                       if( ilg_process != null && ilg_process.length > 0 ){
                            SQL += " AND ilg_process IN ( ? ";
                            for(int i=1; i<ilg_process.length; i++)
                                SQL += " , ? ";
                            SQL += " ) ";
                       }
                       if( v_ilg_id != null ) {
                            if( !v_ilg_id.isEmpty() )
                                SQL += " AND ilg_id IN " + cwUtils.vector2list(v_ilg_id);
                            else
                                SQL += " AND ilg_id = 0 "; //Return a empty resultset
                       } 
                       if( ilg_type != null && ilg_type.equalsIgnoreCase("ENROLLMENT")) {
                               SQL += " AND ilg_target_id = 'ITM_" + itm_id + "'";
                       } 
                   SQL += " Order By " + sort_col + " " + sort_order;
                    
            PreparedStatement stmt = con.prepareStatement(SQL);
            int index = 1;
            stmt.setString(index++, ilg_type);
            if(!prof.current_role.equals("SADM"))
            {
            	stmt.setLong(index++, prof.root_ent_id);
            	stmt.setLong(index++, prof.my_top_tc_id);
            }
            if( ilg_process != null ){
                for(int i=0; i<ilg_process.length; i++)
                    stmt.setString(index++, ilg_process[i]);
            }
            
            ResultSet rs = stmt.executeQuery();
            ViewUsrIMSLog log = null;
            while (rs.next()){
                log = new ViewUsrIMSLog();
                log.usr_display_bil = dbUtils.esc4XML(rs.getString("usr_display_bil"));
                log.dbIMSLog.ilg_id = rs.getLong("ilg_id");
                log.dbIMSLog.ilg_process = dbUtils.esc4XML(rs.getString("ilg_process"));
                log.dbIMSLog.ilg_type = rs.getString("ilg_type");
                log.dbIMSLog.ilg_filename = dbUtils.esc4XML(rs.getString("ilg_filename"));
                log.dbIMSLog.ilg_desc = dbUtils.esc4XML(rs.getString("ilg_desc"));
                log.dbIMSLog.ilg_create_timestamp = rs.getTimestamp("ilg_create_timestamp");
                log.dbIMSLog.ilg_method = rs.getString("ilg_method");
                vTmp.addElement(log);
            }
            stmt.close();
            ViewUsrIMSLog[] result = new ViewUsrIMSLog[vTmp.size()];
            result = (ViewUsrIMSLog[])vTmp.toArray(result);
            return result;              
        }
    
    
}