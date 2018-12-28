package com.cw.wizbank.db;

import java.sql.*;
import java.util.*;

import com.cw.wizbank.util.*;
import com.cwn.wizbank.utils.CommonLog;

public class DbMgXslParamValue {
    
    public final static String STATIC   =   "STATIC";
    public final static String DYNAMIC  =   "DYNAMIC";
    
    public long xpv_mst_msg_id;
    public long xpv_mst_xtp_id;
    public long xpv_xpn_id;
    public String xpv_type;
    public String xpv_value;
    
    
    //insert a new param value
    public void ins(Connection con)
        throws SQLException, cwException {
                        
            String DbMgXslParamValue_INS    =    " INSERT INTO mgxslParamValue "
                                            +    " ( xpv_mst_msg_id, xpv_mst_xtp_id, "
                                            +    "   xpv_xpn_id, xpv_type, "
                                            +    "   xpv_value ) "
                                            +    " VALUES ( ?, ?, ?, ?, ? ) ";
                                            
            PreparedStatement stmt = con.prepareStatement(DbMgXslParamValue_INS);
            stmt.setLong(1, xpv_mst_msg_id);
            stmt.setLong(2, xpv_mst_xtp_id);
            stmt.setLong(3, xpv_xpn_id);
            stmt.setString(4, xpv_type);
            stmt.setString(5,xpv_value);            
            
            if( stmt.executeUpdate() != 1){
            	stmt.close();
                throw new cwException("Failed to insert a new vlue of praramter.");
            }
            stmt.close();
            return;
            
        }
        
        

        
    /**
    Update param value
    */
    public void upd(Connection con)
        throws SQLException, cwException {

            String DbMgXslParamValue_UPD = " UPDATE mgxslParamValue SET xpv_type = ?, xpv_value = ? "
                                         + " WHERE xpv_mst_msg_id = ? AND xpv_mst_xtp_id = ? AND xpv_xpn_id = ? ";

            PreparedStatement stmt = con.prepareStatement(DbMgXslParamValue_UPD);
            stmt.setString(1, xpv_type);
            stmt.setString(2, xpv_value);
            stmt.setLong(3, xpv_mst_msg_id);
            stmt.setLong(4, xpv_mst_xtp_id);
            stmt.setLong(5, xpv_xpn_id);

            if( stmt.executeUpdate() != 1 )
                throw new cwException("Failed to update a param value , xpv_xpn_id = " + xpv_xpn_id);

            stmt.close();
            return;

        }
        
        
        
        
    
    // return param value , type and related paramname id in vector format
    public static Vector getParams(Connection con, long msg_id, long xtp_id)
        throws SQLException, cwException {
            
            Vector vec = new Vector();
            Vector idVec = new Vector();
            Vector typeVec = new Vector();
            Vector valueVec = new Vector();
            
            String DbMgXslParamValue_GET_PARAMS = " SELECT xpv_xpn_id, xpv_type, xpv_value "
                                                + " FROM mgxslParamValue "
                                                + " WHERE xpv_mst_msg_id = ? "
                                                + " AND   xpv_mst_xtp_id = ? ";

            PreparedStatement stmt = con.prepareStatement(DbMgXslParamValue_GET_PARAMS);
            stmt.setLong(1, msg_id);
            stmt.setLong(2, xtp_id);
            
            ResultSet rs = stmt.executeQuery();
            while( rs.next() ) {
                idVec.addElement(new Long(rs.getLong("xpv_xpn_id")));
                typeVec.addElement(rs.getString("xpv_type"));
                valueVec.addElement(rs.getString("xpv_value"));
            }
            
            vec.addElement(idVec);
            vec.addElement(typeVec);
            vec.addElement(valueVec);
            
            stmt.close();
            return vec;
            
        }
        
        
    public static void delRecords(Connection con, long msg_id)
        throws SQLException, cwException {
            
            String DbMgXslParamValue_DEL_RECORDS = " DELETE From mgxslParamValue "
                                                 + " WHERE xpv_mst_msg_id = ? ";

            PreparedStatement stmt = con.prepareStatement(DbMgXslParamValue_DEL_RECORDS);
            stmt.setLong(1, msg_id);
            stmt.executeUpdate();
            stmt.close();
            return;
        }        

    public static void delRecords(Connection con, long[] msg_id)
        throws SQLException, cwException {
            
            String DbMgXslParamValue_DEL_RECORDS = " DELETE From mgxslParamValue "
                                                 + " WHERE xpv_mst_msg_id IN " + cwUtils.array2list(msg_id);

            PreparedStatement stmt = con.prepareStatement(DbMgXslParamValue_DEL_RECORDS);
            stmt.executeUpdate();
            stmt.close();
            return;
        }    
      
      // add by tim
      public static void upd(Connection con, long xpv_mst_msg_id, String xtp_type, String xpn_name, String xpv_value)
        throws SQLException, cwException {
    	  CommonLog.debug("in upd param: xpv_mst_msg_id " + xpv_mst_msg_id + "xtp_type: "+ xtp_type + " xpn_name" + xpn_name + " xpv_value" + xpv_value);
            String sql = "UPDATE mgxslparamvalue " +
                " SET xpv_value = ? " +
                " WHERE xpv_mst_msg_id = ? AND xpv_mst_xtp_id IN " +
                " (SELECT xtp_id FROM xslTemplate " +
                " WHERE xtp_type = ?) AND xpv_xpn_id IN " +
                " (SELECT xpn_id FROM xslparamName " +
                " WHERE xpn_name = ?)" ;
            CommonLog.debug("in upd param sql: " + sql);
            /*
            String DbMgXslParamValue_UPD = " UPDATE mgxslParamValue SET xpv_type = ?, xpv_value = ? "
                                         + " WHERE xpv_mst_msg_id = ? AND xpv_mst_xtp_id = ? AND xpv_xpn_id = ? ";

            */
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, xpv_value);
            stmt.setLong(2, xpv_mst_msg_id);
            stmt.setString(3, xtp_type);
            stmt.setString(4, xpn_name);
            stmt.executeUpdate();
            /*
            if( stmt.executeUpdate() != 1 )
                throw new cwException("Failed to update a param value");
            */
            stmt.close();
            return;

        }   
}
