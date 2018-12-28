package com.cw.wizbank.ae;

import java.sql.*;
import java.util.*;
import javax.servlet.http.*;
import com.cw.wizbank.qdb.*;
import com.cw.wizbank.util.cwSQL;

public class aeOpenItem {

    public final static String FTN_OI_MGT_ADMIN = "OI_MGT_ADMIN";

    private final static int ACCOUNT_NOT_EXIST = -1;
    //private static boolean TRANSACTION_STATUS = true;
    private final static String SUCCESS = "SUCCESS";

    private final static String CREATE_OPENITEM
    = " INSERT INTO aeOpenItem ( oim_acn_id, oim_ccy, oim_org_amt, oim_os_amt, "
    + " oim_due_datetime, oim_type, oim_status, oim_src, oim_ref, oim_desc, "
    + " oim_discnt_date, oim_discnt_amt, oim_create_timestamp, oim_create_usr_id, "
    + " oim_upd_timestamp, oim_upd_usr_id ) "
    + " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,? ,? ,?, ?, ? ) ";


    private final static String UPDATE_OS_AMOUNT
    = " UPDATE aeOpenItem SET oim_os_amt = ? , oim_status = ? WHERE oim_id = ? AND oim_upd_timestamp = ? ";

    private final static String GET_SRC_AND_REF
    = " SELECT oim_src AS SRC, oim_ref AS REF FROM aeOpenItem WHERE oim_id = ? " ;

    /*
    private final static String CREATE_ACCOUNT_TRANSACTION
    = " INSERT INTO aeAccountTransaction ( axn_acn_id, axn_ccy, axn_amt, axn_type, "
    + " axn_desc, axn_method, axn_method_xml, axn_ref, axn_create_timestamp, axn_create_usr_id ) "
    + " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) ";
    */

    private final static String CREATE_OPENITEM_ALLOCATION
    = " INSERT INTO aeOpenItemAllocation ( oal_oim_id, oal_axn_id, oal_amt_applied, oal_discnt_taken ) "
    + " VALUES ( ?, ?, ?, ? ) ";


    private final static String GET_ONACCOUNT_OS
    = " SELECT oim_os_amt AS OSAMT FROM aeOpenItem WHERE oim_acn_id = ? AND oim_type = ? AND oim_ccy = ? ";


    private final static String GET_OS_OPENITEM
    = " SELECT oim_id AS ID, oim_ccy AS CCY, oim_org_amt AS ORGAMT, oim_os_amt AS OSAMT, oim_status AS STATUS, "
    + " oim_desc AS OIMDESC, oim_due_datetime AS DUEDATE, oim_upd_usr_id AS USRID, oim_upd_timestamp AS UPDDATE "
    + " FROM aeOpenItem WHERE oim_acn_id = ? AND oim_type = ? AND oim_status = ? order by oim_id ";


    private final static String GET_OPENITEM
    = " SELECT oim_id AS ID, oim_ccy AS CCY, oim_org_amt AS ORGAMT, oim_os_amt AS OSAMT, oim_status AS STATUS, oim_type AS TYPE, "
    + " oim_desc AS OIMDESC, oim_due_datetime AS DUEDATE, oim_upd_usr_id AS USRID, oim_upd_timestamp AS UPDDATE "
    + " FROM aeOpenItem WHERE oim_id = ? order by oim_id ";


    private final static String GET_ENTID_TYPE_FROM_ACCOUNT
    = " SELECT acn_ent_id AS ENTID, acn_type AS TYPE FROM aeAccount WHERE acn_id = ? ";


    private final static String GET_USER_AXN
    = " SELECT axn_id AS AXNID, axn_ccy AS CCY, axn_amt AS AMT, axn_method AS METHOD, axn_desc AS AXNDESC, "
    + " axn_create_timestamp AS AXNDATE , axn_type AS TYPE "
    + " FROM aeAccountTransaction "
    + " WHERE axn_acn_id = ? AND axn_status = ? order by axn_create_timestamp desc ";


    private final static String GET_OPENITEM_ALLOCATION
    = " SELECT oim_id AS OIMID, oim_ccy AS OIMCCY, oim_desc AS OIMDESC, oal_amt_applied AS APPAMT, "
    + " oim_org_amt AS ORGAMT, oim_os_amt AS OSAMT "
    + " FROM aeOpenItemAllocation, aeOpenItem "
    + " WHERE oal_axn_id = ? AND oal_oim_id = oim_id AND oim_type = ? order by oim_id ";

    private final static String GET_USER_DETIAL
    = " SELECT usr_id AS USRID, usr_ste_usr_id AS USRSID, usr_first_name_bil AS Fname, usr_last_name_bil AS Lname, usr_display_bil AS Dname "
    + " FROM RegUser WHERE usr_ent_id = ? ";


    private final static String CHECK_OPENITEM_PERMISSION
    = " SELECT oim_id AS OIMID FROM aeACCOUNT, aeOpenItem WHERE acn_ent_id = ? AND acn_id = oim_acn_id ";

    // item for database
    private int oim_id;
    private int oim_acn_id;
    public String oim_ccy;
    public float oim_org_amt;
    public float oim_os_amt;
    public Timestamp oim_due_datetime;
    public String oim_type;
    public String oim_status;
    public String oim_src;
    public String oim_ref;
    public String oim_desc;
    public Timestamp oim_discnt_date;
    public float oim_discnt_amt;
    public Timestamp oim_create_timestamp;
    public Timestamp oim_upd_timestamp;
    public String oim_create_usr_id;
    public String oim_upd_usr_id;

    //item for object
    private long usr_ent_id;
    private String acn_type;
    private Connection con;


    // Pass connection , user entity id and account type to constructor
    // and get the account id of the user
    aeOpenItem(Connection connection, long ent_id, String type)
        throws SQLException {
            con = connection;
            usr_ent_id = ent_id;
            acn_type = type;
            oim_acn_id = aeAccount.getAccountId(con, usr_ent_id, acn_type);
        }


    // Create an Open Item
    public int ins()
        throws SQLException, qdbException {

            // account not exist for the user, create one for the user and get the account id
            if( oim_acn_id == ACCOUNT_NOT_EXIST ) {
                aeAccount.ins(con, usr_ent_id, acn_type, oim_create_usr_id);
                oim_acn_id = aeAccount.getAccountId(con, usr_ent_id, acn_type);
            }

            // insert an open item for the user
            oim_create_timestamp = dbUtils.getTime(con);

            PreparedStatement stmt = con.prepareStatement(CREATE_OPENITEM, PreparedStatement.RETURN_GENERATED_KEYS);
            ResultSet rs = null;
            stmt.setInt(1, oim_acn_id);
            stmt.setString(2, oim_ccy);
            stmt.setFloat(3, oim_org_amt);
            stmt.setFloat(4, oim_os_amt);
            stmt.setTimestamp(5, oim_due_datetime);
            stmt.setString(6, oim_type);
            stmt.setString(7, "OUTSTANDING");
            stmt.setString(8, oim_src);
            stmt.setString(9, oim_ref);
            stmt.setString(10, oim_desc);
            stmt.setTimestamp(11, oim_discnt_date);
            stmt.setFloat(12, oim_discnt_amt);
            stmt.setTimestamp(13, oim_create_timestamp);
            stmt.setString(14, oim_create_usr_id);
            stmt.setTimestamp(15, oim_create_timestamp);
            stmt.setString(16, oim_create_usr_id);

            if( stmt.executeUpdate() != 1 ) {
                stmt.close();
                throw new SQLException("Failed to add an Open Item.");
            }
            oim_id = (int) cwSQL.getAutoId(con, stmt, "aeOpenItem", "oim_id");
            stmt.close();

            return oim_id;
        }



    // Get detail of the outstanding open item
    public static String getOSOpenItem(Connection connection, HttpSession sess, long ent_id, String type)
        throws SQLException {
            StringBuffer result = new StringBuffer();
            PreparedStatement stmt = null;
            ResultSet rs = null;
            Vector openItem = new Vector();
            float total_os = 0;
            float total_org = 0;

            stmt = connection.prepareStatement(GET_USER_DETIAL);
            stmt.setLong(1, ent_id);
            rs = stmt.executeQuery();
            if(rs.next()) {
                result.append("<account_owner ent_id=\"").append(ent_id).append("\" ");
                result.append("usr_id=\"").append(dbUtils.esc4XML(aeUtils.escNull(rs.getString("USRSID")))).append("\" ");
                result.append("first_name=\"").append(dbUtils.esc4XML(rs.getString("Fname"))).append("\" ");
                result.append("last_name=\"").append(dbUtils.esc4XML(rs.getString("Lname"))).append("\" ");
                result.append("display_name=\"").append(dbUtils.esc4XML(rs.getString("Dname"))).append("\" ");
                result.append("/>").append(dbUtils.NEWL);
            }
            stmt.close();


            // Get account Id by entity id and account type
            int acn_id = aeAccount.getAccountId(connection, ent_id, type);
            stmt = connection.prepareStatement(GET_OS_OPENITEM);
            stmt.setInt(1, acn_id);
            stmt.setString(2, "INVOICE");
            stmt.setString(3, "OUTSTANDING");

            rs = stmt.executeQuery();
            result.append("<open_items>").append(dbUtils.NEWL);

            while(rs.next()) {
                aeOpenItem sessOi = new aeOpenItem(connection, ent_id, type);
                sessOi.oim_id = rs.getInt("ID");
                sessOi.oim_ccy = dbUtils.esc4XML(rs.getString("CCY"));
                sessOi.oim_org_amt = rs.getFloat("ORGAMT");
                sessOi.oim_os_amt = rs.getFloat("OSAMT");
                sessOi.oim_desc = dbUtils.esc4XML(rs.getString("OIMDESC"));
                sessOi.oim_due_datetime = rs.getTimestamp("DUEDATE");
                sessOi.oim_upd_usr_id = rs.getString("USRID");
                sessOi.oim_upd_timestamp = rs.getTimestamp("UPDDATE");
                total_os += aeUtils.roundingFloat(sessOi.oim_os_amt,2);
                total_org += aeUtils.roundingFloat(sessOi.oim_org_amt,2);
                result.append("<open_item id=\"").append(sessOi.oim_id).append("\" ");
                result.append(" ccy=\"").append(sessOi.oim_ccy).append("\" ");
                result.append(" org_amount=\"").append(aeUtils.twoDecPt(aeUtils.roundingFloat(sessOi.oim_org_amt,2))).append("\" ");
                result.append(" os_amount=\"").append(aeUtils.twoDecPt(aeUtils.roundingFloat(sessOi.oim_os_amt,2))).append("\" ");
                result.append(" desc=\"").append(sessOi.oim_desc).append("\" ");
                result.append(" due_date=\"").append(sessOi.oim_due_datetime).append("\" ");
                result.append(" upd_usr=\"").append(sessOi.oim_upd_usr_id).append("\" ");
                result.append(" upd_date=\"").append(sessOi.oim_upd_timestamp).append("\" ");
                result.append(" />").append(dbUtils.NEWL);

                openItem.addElement(sessOi);
            }
            stmt.close();


            stmt = connection.prepareStatement(GET_ONACCOUNT_OS);
            stmt.setInt(1, acn_id);
            stmt.setString(2, "ONACCOUNT");
            stmt.setString(3, "RMB");

            rs = stmt.executeQuery();
            float OSAMT=0;
            if(rs.next()) {
                OSAMT = aeUtils.roundingFloat(rs.getFloat("OSAMT"),2);
            }
            stmt.close();
            result.append("<total_items on_account_amount=\"").append(aeUtils.twoDecPt(0-OSAMT)).append("\" ");
            result.append("total_org=\"").append(aeUtils.twoDecPt(aeUtils.roundingFloat(total_org,2))).append("\" ");
            result.append("total_os=\"").append(aeUtils.twoDecPt(aeUtils.roundingFloat(total_os,2))).append("\" ");
            result.append("/>").append(dbUtils.NEWL);

            result.append("</open_items>").append(dbUtils.NEWL);

            if(sess != null) {
                sess.setAttribute("SUMAMT", new Float(OSAMT));
                sess.setAttribute("OPENITEM", openItem);
            }
        return result.toString();
    }




    // Get back detail of the outstanding open item
    public static String getBackOSOpenItem(Connection connection, HttpSession sess)
        throws SQLException {
            StringBuffer result = new StringBuffer();
            PreparedStatement stmt = null;
            ResultSet rs = null;
            Vector openItem = new Vector();
            float total_pay = 0;
            float total_os = 0;
            float total_org = 0;

            long ent_id = ((Long)sess.getAttribute("ENTID")).longValue();
            String type = (String)sess.getAttribute("ACNTYPE");
            openItem = (Vector)sess.getAttribute("OPENITEM");

            aeOpenItem sessOi = new aeOpenItem(connection, ent_id, type);

            stmt = connection.prepareStatement(GET_USER_DETIAL);
            stmt.setLong(1, ent_id);
            rs = stmt.executeQuery();
            if(rs.next()) {
                result.append("<account_owner ent_id=\"").append(ent_id).append("\" ");
                result.append("usr_id=\"").append(dbUtils.esc4XML(aeUtils.escNull(rs.getString("USRSID")))).append("\" ");
                result.append("first_name=\"").append(dbUtils.esc4XML(rs.getString("Fname"))).append("\" ");
                result.append("last_name=\"").append(dbUtils.esc4XML(rs.getString("Lname"))).append("\" ");
                result.append("display_name=\"").append(dbUtils.esc4XML(rs.getString("Dname"))).append("\" ");
                result.append("/>").append(dbUtils.NEWL);
            }
            stmt.close();

//            item_assigned = (Vector)sess.getAttribute("ITEM");
            Vector amount_assigned = (Vector)sess.getAttribute("AMOUNT");


            // Get account Id by entity id and account type
            result.append("<open_items>").append(dbUtils.NEWL);
            for(int i=0; i<openItem.size(); i++) {
                sessOi = (aeOpenItem)openItem.elementAt(i);
                result.append("<open_item id=\"").append(sessOi.oim_id).append("\" ");
                result.append(" ccy=\"").append(sessOi.oim_ccy).append("\" ");
                result.append(" org_amount=\"").append(aeUtils.twoDecPt(aeUtils.roundingFloat(sessOi.oim_org_amt,2))).append("\" ");
                result.append(" os_amount=\"").append(aeUtils.twoDecPt(aeUtils.roundingFloat(sessOi.oim_os_amt,2))).append("\" ");
                result.append(" pay_amount=\"").append(aeUtils.twoDecPt(aeUtils.roundingFloat(((Float)amount_assigned.elementAt(i)).floatValue(),2))).append("\" ");
                total_pay += ((Float)amount_assigned.elementAt(i)).floatValue();
                total_os += sessOi.oim_os_amt;
                total_org += sessOi.oim_org_amt;
                result.append(" desc=\"").append(sessOi.oim_desc).append("\" ");
                result.append(" due_date=\"").append(sessOi.oim_due_datetime).append("\" ");
                result.append(" upd_usr=\"").append(sessOi.oim_upd_usr_id).append("\" ");
                result.append(" upd_date=\"").append(sessOi.oim_upd_timestamp).append("\" ");
                result.append(" />").append(dbUtils.NEWL);
            }
            stmt.close();


            float SUMAMT = ((Float)sess.getAttribute("SUMAMT")).floatValue();


            result.append("<total_items on_account_amount=\"").append(aeUtils.twoDecPt(0-SUMAMT)).append("\" ");
            result.append("total_org=\"").append(aeUtils.twoDecPt(aeUtils.roundingFloat(total_org,2))).append("\" ");
            result.append("total_os=\"").append(aeUtils.twoDecPt(aeUtils.roundingFloat(total_os,2))).append("\" ");
            result.append("/>").append(dbUtils.NEWL);


            result.append("</open_items>").append(dbUtils.NEWL);
            float axn_amt = ((Float)sess.getAttribute("AXNAMT")).floatValue();
            String axn_type = (String)sess.getAttribute("AXNTYPE");
            String axn_method = (String)sess.getAttribute("AXNMETHOD");
            String ref_no = (String)sess.getAttribute("REFNO");

            result.append("<transaction method=\"").append(axn_method).append("\" ");
            result.append("amount=\"").append(aeUtils.twoDecPt(aeUtils.roundingFloat(axn_amt,2))).append("\" ");
            result.append("type=\"").append(axn_type).append("\" ");
            result.append("ref_no=\"").append(ref_no).append("\" ");
            result.append("on_account_amount=\"").append(aeUtils.twoDecPt(aeUtils.roundingFloat(axn_amt-total_pay,2))).append("\" ");
            result.append("remain_amount=\"").append(aeUtils.twoDecPt(aeUtils.roundingFloat(0-SUMAMT,2))).append("\" ");
            result.append("/>").append(dbUtils.NEWL);


        return result.toString();
    }


    public static String checkTransactionHistory(Connection connection, long ent_id, String type)
        throws SQLException {
            PreparedStatement stmt1 = null;
            PreparedStatement stmt2 = null;
            ResultSet rs1 = null;
            ResultSet rs2 = null;
            StringBuffer result = new StringBuffer();

            int acn_id = aeAccount.getAccountId(connection, ent_id, type);

            stmt1 = connection.prepareStatement(GET_USER_AXN);
            stmt1.setInt(1, acn_id);
            stmt1.setString(2, SUCCESS);

            int AXNID;
            String CCY;
            float AMT;
            String TYPE;
            String METHOD;
            String DESC;
            Timestamp DATE;

            int OIMID;
            String OIMCCY;
            String OIMDESC;
            float APPAMT;
            float total_pay = 0;
            float total_os = 0;
            float total_org = 0;
            float ORGAMT = 0;
            float OSAMT = 0;
            rs1 = stmt1.executeQuery();
            result.append("<transactions>").append(dbUtils.NEWL);

            while(rs1.next()) {

                AXNID = rs1.getInt("AXNID");
                CCY = rs1.getString("CCY");
                AMT = rs1.getFloat("AMT");
                TYPE = rs1.getString("TYPE");
                METHOD = rs1.getString("METHOD");
                DESC = rs1.getString("AXNDESC");
                DATE = rs1.getTimestamp("AXNDATE");

                result.append("<transaction ");
                result.append("id=\"").append(AXNID).append("\" ");
                result.append("date=\"").append(DATE).append("\" ");
                result.append("ccy=\"").append(CCY).append("\" ");
                result.append("amount=\"").append(aeUtils.twoDecPt(AMT)).append("\" ");
                result.append("method=\"").append(METHOD).append("\" ");
                result.append("type=\"").append(TYPE).append("\" ");
                result.append("desc=\"").append(dbUtils.esc4XML(DESC)).append("\" ");
                result.append(">").append(dbUtils.NEWL);

                stmt2 = connection.prepareStatement(GET_OPENITEM_ALLOCATION);
                stmt2.setInt(1, AXNID);
                stmt2.setString(2, "INVOICE");
                rs2 = stmt2.executeQuery();
                total_pay = 0;
                total_os = 0;
                total_org = 0;
                while(rs2.next()) {
                    OIMID = rs2.getInt("OIMID");
                    OIMCCY = rs2.getString("OIMCCY");
                    OIMDESC = rs2.getString("OIMDESC");
                    APPAMT = rs2.getFloat("APPAMT");
                    ORGAMT = rs2.getFloat("ORGAMT");
                    OSAMT = rs2.getFloat("OSAMT");

                    result.append("<open_item ");
                    result.append("id=\"").append(OIMID).append("\" ");
                    result.append("ccy=\"").append(OIMCCY).append("\" ");
                    result.append("app_amount=\"").append(aeUtils.twoDecPt(APPAMT)).append("\" ");
                    result.append("desc=\"").append(dbUtils.esc4XML(OIMDESC)).append("\" ");
                    result.append("os_amount=\"").append(aeUtils.twoDecPt(OSAMT)).append("\" ");
                    result.append("org_amount=\"").append(aeUtils.twoDecPt(ORGAMT)).append("\" ");
                    result.append("/>").append(dbUtils.NEWL);
                    total_pay += APPAMT;
                    total_org += ORGAMT;
                    total_os += OSAMT;
                }
                result.append("<total_items total_pay=\"").append(aeUtils.twoDecPt(aeUtils.roundingFloat(total_pay,2))).append("\" ");
                result.append(" total_os=\"").append(aeUtils.twoDecPt(aeUtils.roundingFloat(total_os,2))).append("\" ");
                result.append(" total_org=\"").append(aeUtils.twoDecPt(aeUtils.roundingFloat(total_org,2))).append("\" />").append(dbUtils.NEWL);
                result.append("</transaction>");
            }
            if(stmt1 != null){
            	stmt1.close();
            }
            if(stmt2 != null){
            	stmt2.close();
            }
            result.append("</transactions>");

            return result.toString();
        }



    public static boolean checkOpenItemPermission(Connection connection, long ent_id, int[] oi_list)
    throws SQLException{
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Vector oiV = new Vector();

        stmt = connection.prepareStatement(CHECK_OPENITEM_PERMISSION);
        stmt.setLong(1, ent_id);
        rs = stmt.executeQuery();
        while(rs.next()) {
            oiV.addElement(new Integer(rs.getInt("OIMID")));
        }
        stmt.close();
        for(int i=0; i<oi_list.length; i++)
            if( oiV.indexOf(new Integer(oi_list[i])) == -1 )
                return false;

        return true;
    }
}