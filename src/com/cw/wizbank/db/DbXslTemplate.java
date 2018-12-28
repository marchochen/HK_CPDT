package com.cw.wizbank.db;

import java.sql.*;
import java.util.*;
import com.cw.wizbank.util.*;

public class DbXslTemplate {
    
    public final static String COMMA        =   ",";
    
    public long xtp_id;
    public String xtp_type;
    public String xtp_xsl;    
    public String xtp_xml_url;
    public String xtp_title;
    
    //insert a new param value
    public void ins(Connection con)
        throws SQLException, cwException {            
            
            String DbXslTemplate_INS    =    " INSERT INTO xslTemplate "
                                        +    " ( xtp_id, xtp_type, "
                                        +    "   xtp_xsl, xtp_xml_url ) "
                                        +    " VALUES ( ?, ?, ?, ? ) ";
                                            
            PreparedStatement stmt = con.prepareStatement(DbXslTemplate_INS);
            stmt.setLong(1, xtp_id);
            stmt.setString(2, xtp_type);
            stmt.setString(3, xtp_xsl);
            stmt.setString(4, xtp_xml_url);
            
            if( stmt.executeUpdate() != 1)
                throw new cwException("Failed to insert a new XSLTemplate.");

            stmt.close();
            return;
            
        }
    
    
    // Get xml url
    public static String getUrl(Connection con, long xtp_id)
        throws SQLException, cwException {
            
            String DbXslTemplate_GET_URL = " SELECT xtp_xml_url FROM xslTemplate "
                                         + " WHERE xtp_id = ? ";

            PreparedStatement stmt = con.prepareStatement(DbXslTemplate_GET_URL);
            stmt.setLong(1, xtp_id);

            ResultSet rs = stmt.executeQuery();
            String url;
            if( rs.next() )
                url = rs.getString("xtp_xml_url");
            else
                throw new cwException ("Failed to get xml url.");

            stmt.close();
            return url;
        }

/*
    //get sub type(send method) of the template
    public static String getSubtype(Connection con, long xtp_id)
        throws SQLException, cwException {

            String DbXslTemplate_GET_SUBTYPE = " SELECT xtp_subtype FROM xslTemplate " 
                                             + " WHERE xtp_id = ? " ;
                                             
            PreparedStatement stmt = con.prepareStatement(DbXslTemplate_GET_SUBTYPE);
            stmt.setLong(1, xtp_id);
            ResultSet rs = stmt.executeQuery();
            String subtype;
            if( rs.next() )
                subtype = rs.getString("xtp_subtype");                
            else
                throw new cwException("Failed to get send method of the template.");

            stmt.close();
            return subtype;
        }
*/        
    //get type of the template
    public static String getType(Connection con, long xtp_id)
        throws SQLException, cwException {

            String DbXslTemplate_GET_TYPE = " SELECT xtp_type FROM xslTemplate " 
                                          + " WHERE xtp_id = ? " ;
                                             
            PreparedStatement stmt = con.prepareStatement(DbXslTemplate_GET_TYPE);
            stmt.setLong(1, xtp_id);
            ResultSet rs = stmt.executeQuery();
            String type;
            if( rs.next() )
                type = rs.getString("xtp_type");
            else
                throw new cwException("Failed to get send method of the template.");
            
            stmt.close();
            return type;
        }
        
        

    public static Vector getDetail(Connection con, long[] xtp_id)
        throws SQLException, cwException {
            
            Vector xtpDetail = new Vector();
            Vector xtpMethod = new Vector();
            Vector xtpChannel = new Vector();
            Vector xtpApi = new Vector();
            Vector xtpXsl = new Vector();
            Vector xtpMailmerge = new Vector();
            
            StringBuffer xtpIdStr = new StringBuffer().append("(0");
            for(int i=0; i<xtp_id.length; i++)
                xtpIdStr.append(COMMA).append(xtp_id[i]);
            xtpIdStr.append(")");    

            String DbXslTemplate_GET_DETIAL = " SELECT xtp_subtype, xtp_channel_type, xtp_channel_api, xtp_xsl, xtp_mailmerge_ind "
                                            + " FROM xslTemplate "
                                            + " WHERE xtp_id IN "
                                            + xtpIdStr.toString();
                                            
            PreparedStatement stmt = con.prepareStatement(DbXslTemplate_GET_DETIAL);            
            ResultSet rs = stmt.executeQuery();

            while( rs.next() ) {

                xtpMethod.addElement(rs.getString("xtp_subtype"));
                xtpChannel.addElement(rs.getString("xtp_channel_type"));
                xtpApi.addElement(rs.getString("xtp_channel_api"));
                xtpXsl.addElement(rs.getString("xtp_xsl"));
                xtpMailmerge.addElement(new Boolean(rs.getBoolean("xtp_mailmerge_ind")));
                
            }
            
            xtpDetail.addElement(xtpMethod);            
            xtpDetail.addElement(xtpChannel);            
            xtpDetail.addElement(xtpApi);            
            xtpDetail.addElement(xtpXsl);            
            xtpDetail.addElement(xtpMailmerge);            
            
            stmt.close();
            
            return xtpDetail;
            
        }

    public static long getXtpId(Connection con, String type, String subtype)
        throws SQLException, cwException {
            
            String DbXslTemplate_GET_XTP_ID = " SELECT xtp_id FROM xslTemplate "
                                            + " WHERE xtp_type = ? "
                                            + " AND   xtp_subtype = ? ";

            PreparedStatement stmt = con.prepareStatement(DbXslTemplate_GET_XTP_ID);
            stmt.setString(1, type);
            stmt.setString(2, subtype);
            ResultSet rs = stmt.executeQuery();
            long xtp_id;
            if( rs.next() )
                xtp_id = rs.getLong("xtp_id");
            else
                throw new cwException("Failed to get template id of type = " + type + " , subtype = " + subtype );
                
            stmt.close();
            return xtp_id;
            
        }



    public static long[] getXtpIds(Connection con, String type, String[] subtype)
        throws SQLException, cwException {
                            
            String DbXslTemplate_GET_XTP_IDS = " SELECT xtp_id FROM xslTemplate "
                                            + " WHERE xtp_type = ? "
                                            + " AND xtp_subtype IN ( ? , ? ) ";
                                            

            PreparedStatement stmt = con.prepareStatement(DbXslTemplate_GET_XTP_IDS);
            stmt.setString(1, type);
            stmt.setString(2, subtype[0]);
            if( subtype.length == 2 )
                stmt.setString(3, subtype[1]);
            else
                stmt.setString(3, "");
            ResultSet rs = stmt.executeQuery();

            Vector vec = new Vector();

            while( rs.next() )
                vec.addElement( new Long(rs.getLong("xtp_id")) );
            rs.close();
            stmt.close();

            long[] xtpIds = new long[vec.size()];

            for(int i=0; i<xtpIds.length; i++)
                xtpIds[i] = ((Long)vec.elementAt(i)).longValue();

            return xtpIds;
            
        }

    
    
    
    public static String getTemplateTitle(Connection con, String type, String subtype)
        throws SQLException{
            
            String SQL = " SELECT xtp_title "
                       + " FROM xslTemplate "
                       + " WHERE xtp_type = ? "
                       + " AND xtp_subtype = ? ";
            
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setString(1, type);
            stmt.setString(2, subtype);
            ResultSet rs = stmt.executeQuery();
            String title = null;
            if( rs.next() )
                title = rs.getString("xtp_title");
            rs.close();
            stmt.close();
            return title;
        }

    public static String getTemplateTitle(Connection con, long xtp_id)
        throws SQLException{
            
            String SQL = " SELECT xtp_title "
                       + " FROM xslTemplate "
                       + " WHERE xtp_id = ? ";
            
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, xtp_id);
            ResultSet rs = stmt.executeQuery();
            String title = null;
            if( rs.next() )
                title = rs.getString("xtp_title");
            stmt.close();
            return title;
        }
}
