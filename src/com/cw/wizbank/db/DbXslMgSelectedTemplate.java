package com.cw.wizbank.db;

import java.sql.*;
import java.util.*;
import com.cw.wizbank.util.*;

public class DbXslMgSelectedTemplate {

    public long mst_msg_id;
    public long mst_xtp_id;
    public String mst_type;

    // insert a message template
    public void ins(Connection con)
        throws SQLException, cwException {
            
            String DbXslMgSelectedTemplate_INS  =   " INSERT INTO xslmgSelectedTemplate "
                                                 +   " ( mst_msg_id, mst_xtp_id, mst_type ) " 
                                                +   " VALUES( ?, ?, ? ) ";
            
            PreparedStatement stmt = con.prepareStatement(DbXslMgSelectedTemplate_INS);
            stmt.setLong(1, mst_msg_id);
            stmt.setLong(2, mst_xtp_id);
            if( mst_type == null )
                mst_type = "MAIN";
            stmt.setString(3, mst_type);

            if( stmt.executeUpdate() != 1 ){
            	stmt.close();
                throw new cwException("Failed to insert a Message Template.");       
            }
            stmt.close();
            return;
        }



    // Get the xsl template id of the specified message id
     public static long[] getXtpIds(Connection con, long msg_id, String type) 
        throws SQLException, cwException {

            Vector idsVec = new Vector();
            
            String DbXslMgSelectedTemplate_GET_XTP_IDS = " SELECT mst_xtp_id FROM xslmgSelectedTemplate "
                                                        + " WHERE mst_msg_id = ? AND mst_type = ? ";

            PreparedStatement stmt = con.prepareStatement(DbXslMgSelectedTemplate_GET_XTP_IDS);
            stmt.setLong(1, msg_id);
            stmt.setString(2, type);

            ResultSet rs = stmt.executeQuery();
            while( rs.next() )
                idsVec.addElement(new Long(rs.getLong("mst_xtp_id")));

            long[] ids = new long[idsVec.size()];
            for(int i=0; i<ids.length; i++)
                ids[i] = ((Long)idsVec.elementAt(i)).longValue();


            stmt.close();
            return ids;

        }



    public static void delRecords(Connection con, long msg_id)
        throws SQLException, cwException {
            
            String DbXslMgSelectedTemplate_DEL_RECORDS = " DELETE From xslmgSelectedTemplate "
                                                       + " WHERE mst_msg_id = ? ";

            PreparedStatement stmt = con.prepareStatement(DbXslMgSelectedTemplate_DEL_RECORDS);
            stmt.setLong(1, msg_id);
            stmt.executeUpdate();
            stmt.close();
            return;
        }

    public static void delRecords(Connection con, long[] msg_id)
        throws SQLException, cwException {
            
            String DbXslMgSelectedTemplate_DEL_RECORDS = " DELETE From xslmgSelectedTemplate "
                                                       + " WHERE mst_msg_id IN " + cwUtils.array2list(msg_id);

            PreparedStatement stmt = con.prepareStatement(DbXslMgSelectedTemplate_DEL_RECORDS);
            stmt.executeUpdate();
            stmt.close();
            return;
        }

}