package com.cw.wizbank.db;

import java.sql.*;
import java.util.*;
import com.cw.wizbank.util.*;

public class DbXslParamName {
    
    public long xpn_id;
    public long xpn_xtp_id;
    public int xpn_pos;
    public String xpn_name;
    
    //insert a new param name
    public void ins(Connection con)
        throws SQLException, cwException {
                        
            String DbXslParamName_INS       =    " INSERT INTO xslParamName "
                                            +    " ( xpn_xtp_id, "
                                            +    "  xpn_pos, xpn_name ) "
                                            +    " VALUES ( ?, ?, ? ) ";
                                            
            PreparedStatement stmt = con.prepareStatement(DbXslParamName_INS);
            stmt.setLong(1, xpn_xtp_id);
            stmt.setInt(2, xpn_pos);
            stmt.setString(3, xpn_name);
            
            if( stmt.executeUpdate() != 1)
                throw new cwException("Failed to insert a new name of parameter.");

            stmt.close();
            return;
            
        }


    /**
    Get name of all parameters in hashtable format
    @param long value of template id
    @param flag boolean value ( true : param name as key, false : param name id as key)
    @return hashtable
    */
    public static Hashtable getParamsName(Connection con, long xtp_id, boolean flag) 
        throws SQLException, cwException {
            
            Hashtable paramsTable = new Hashtable();
            String DbXslParamName_GET_PARAMS_NAME   =   " SELECT xpn_name , xpn_id "
                                                    +   " FROM xslParamName "
                                                    +   " WHERE xpn_xtp_id = ? order by xpn_pos ";
            
            PreparedStatement stmt = con.prepareStatement(DbXslParamName_GET_PARAMS_NAME);
            stmt.setLong(1, xtp_id);

            ResultSet rs = stmt.executeQuery();
            while( rs.next() )
                if(flag)
                    paramsTable.put( (String)rs.getString("xpn_name"), new Long(rs.getLong("xpn_id")) );
                else
                    paramsTable.put( new Long(rs.getLong("xpn_id")), (String)rs.getString("xpn_name") );
            rs.close();
            stmt.close();
            return paramsTable;
        }




    // Get number of params
    public static int getNumParams(Connection con, long xtp_id)
        throws SQLException, cwException {
         
            String DbXslParamName_GET_NUM_PARAMS = " SELECT COUNT(xpn_name) FROM xslParamName " 
                                                 + " WHERE xpn_xtp_id = ? ";

            PreparedStatement stmt = con.prepareStatement(DbXslParamName_GET_NUM_PARAMS);
            stmt.setLong(1, xtp_id);
            ResultSet rs = stmt.executeQuery();
            int id;
            if( rs.next() )
                id = rs.getInt(1);
            else
                throw new cwException("Failed to get the param number of template id : " + xtp_id);
            
            stmt.close();
            return id;
        }
        
    public static long[] getParamsId(Connection con, long xtp_id)
        throws SQLException, cwException {
            
            Vector idVec = new Vector();
            
            String DbXslParamName_GET_PARAMS_ID = " SELECT xpn_id FROM xslParamName "
                                                + " WHERE xpn_xtp_id = ? ";

            PreparedStatement stmt = con.prepareStatement(DbXslParamName_GET_PARAMS_ID);
            stmt.setLong(1, xtp_id);
            ResultSet rs = stmt.executeQuery();
            while( rs.next() ) 
                idVec.addElement(new Long(rs.getLong("xtp_id")));
                
            stmt.close();
            long[] ids = new long[idVec.size()];
            for(int i=0; i<ids.length; i++)
                ids[i] = ((Long)idVec.elementAt(i)).longValue();
            
            return ids;
        }

}
