package com.cw.wizbank.ae;

import java.sql.*;
import java.io.*;
import java.util.*;
import javax.servlet.http.*;
import com.cw.wizbank.qdb.*;
import com.cw.wizbank.util.*;
//import com.cw.wizbank.accesscontrol.AccessControlWZB;

public class aeAccountTransaction {

    //Source of Open Item
    private final static String APPLYEASY = "APPLYEASY";

    //transaction method
    private final static String CHEQUE = "CHEQUE";
    private final static String BYPOST = "BYPOST";
    private final static String CASH = "CASH";
    //transaction status
    private final static String PENDING = "PENDING";
    private final static String SUCCESS = "SUCCESS";
    private final static String FAILURE = "FAILURE";
    //open item type
    private final static String INVOICE = "INVOICE";
    private final static String ONACCOUNT = "ONACCOUNT";
    //transaction type
    private final static String OFFSET = "OFFSET";
    private final static String PAYMENT = "PAYMENT";
    //open item status
    private final static String OUTSTANDING = "OUTSTANDING";
    private final static String PROCESSED = "PROCESSED";
    private final static String CANCELLED = "CANCELLED";
    //private final static String ONHOLD = "ONHOLD";

    //transaction detial
    private final static String message1 = "SUCCESSFULLY";
    private final static String message2 = "Amount assigned more than the item require.";
    private final static String message3 = "Invalid type of the item.";
    private final static String message4 = "This item is processed.";
    private final static String message5 = "This item is cancelled.";
    //private final static String message6 = "This item is on hold.";

    private final static String INVALID_TRANSACTION = "CANNOT PASS THE PAYMENT VALIDATION.";
    private final static String PROCESSING_ONLINE_PAYMENT = "PROCESSING ONLINE PAYMENT.";
    private final static String UPDATE_SUCCESSFULLY_ADMIN = "UPDATE SUCCESSFULLY BY ADMIN.";

    private final static String NO_RIGHT_TO_PAY                 =   "PAY001";
    private final static String NOT_ENOUGH_MONEY_ON_ACCOUNT     =   "PAY002";
    private final static String NOT_ENOUGH_MOMEY                =   "PAY003";
    private final static String EXCEED_TODAY_TRANSACTION        =   "PAY004";

    private final static String GET_OPENITEM
    = " SELECT oim_id OIMID, oim_ccy OIMCCY, oim_org_amt OIMORGAMT, oim_os_amt OIMOSAMT, "
    + " oim_status OIMSTATUS, oim_type OIMTYPE, oim_desc OIMDESC, oim_create_usr_id OIMUSRID, "
    + " oim_due_datetime OIMDUEDATE, oim_upd_usr_id OIMUPDUSRID, oim_upd_timestamp OIMUPDDATE, "
    + " oim_src OIMSRC, oim_ref OIMREF "
    + " FROM aeOpenItem "
    + " WHERE oim_id = ? ";

    private final static String GET_ONACCOUNT_OPENITEM
    = " SELECT oim_id OIMID, oim_os_amt OSAMT, oim_status OIMSTATUS FROM aeOpenItem "
    + " WHERE oim_acn_id = ? AND oim_type= ? AND oim_ccy = ? ";

    private final static String UPDATE_OPENITEM_STATUS
    = " UPDATE aeOpenItem SET oim_status = ? WHERE oim_id = ? ";

    private final static String UPDATE_ONACCOUNT_AMOUNT
    = " UPDATE aeOpenItem SET oim_os_amt = oim_os_amt + ? WHERE oim_id = ? ";

    private final static String GET_ONACCOUNT_ID
    = " SELECT oim_id OIMID FROM aeOpenItem WHERE oim_acn_id = ? AND oim_ccy = ? AND oim_type = ? ";

    private final static String UPDATE_OPENITEM_OS
    = " UPDATE aeOpenItem SET oim_os_amt = ? , oim_status = ? WHERE oim_id = ? ";

    private final static String ROLLBACK_OPENITEMS_STATUS
    = " UPDATE aeOpenItem SET oim_status = ? WHERE oim_status = ? AND oim_id IN ";


    private final static String CREATE_OPENITEM_ALLOCATION
    = " INSERT INTO aeOpenItemAllocation ( oal_oim_id, oal_axn_id, oal_amt_applied, oal_discnt_taken ) "
    + " VALUES ( ?, ?, ?, ? ) ";

    private final static String GET_USER_DETIAL
    = " SELECT usr_id USRID, usr_ste_usr_id USRSID, usr_first_name_bil Fname, usr_last_name_bil Lname, usr_display_bil Dname "
    + " FROM RegUser WHERE usr_ent_id = ? ";


    private final static String UPDATE_ACCOUNTTRANSACTION_DESC
    //= " UPDATE aeAccountTransaction SET axn_desc = ? WHERE axn_id = ? ";
    = " UPDATE aeAccountTransaction SET axn_desc = ? , axn_status = ? WHERE axn_id = ? ";

    private final static String GET_TRANSACTION_REF
    = " SELECT axn_ref AXNREF, axn_create_usr_id AXNUSRID FROM aeAccountTransaction WHERE axn_id = ? ";

    private final static String GET_OPENITEMS
    = " SELECT oim_os_amt, oim_src, oim_ref FROM aeOpenItem "
    + " WHERE oim_id IN ";

    public int axn_id;
    public int axn_acn_id;
    public String axn_ccy;
    public float axn_amt;
    public String axn_type;
    public String axn_desc;
    public String axn_method;
    public String axn_method_xml;
    public String axn_ref;
    public Timestamp axn_create_timestamp;
    public String axn_create_usr_id;

    // for axn_method_xml
    public String ref_no;
    public String card_type;
    public String holder;
    public String exp_month;
    public String exp_year;

    public int[] item_assigned;
    public float[] amount_assigned;
    public String acn_type;
    public long ent_id;
    public String label_lan;
    //public boolean transaction_status;

    //
    public float[] open_item_os;
    public String[] open_item_src;
    public String[] open_item_ref;
    public int on_account_item;

    aeAccountTransaction() {
        axn_desc="";
        axn_ref="";
        axn_method="";
        ref_no="";
        return;
    }

    public boolean paymentValidation(Connection con, loginProfile prof, StringBuffer result, boolean flag)
        throws cwSysMessage, cwException, SQLException, IOException, qdbException {

        PreparedStatement stmt = null;
        ResultSet rs = null;
        //StringBuffer result = new StringBuffer();
        boolean transaction_status = true;
        float total_os = 0;
        float total_org = 0;

        //check item assigned and amount assigned
        if(item_assigned.length != amount_assigned.length)
            throw new cwException("Number of Amount Assigned not match Number of Open Item Assigned.");


        //check permission
        /*
        if( prof != null ) { // if online payment, validation called from bank is no prof
            AccessControlWZB acl = new AccessControlWZB();
            if(!acl.hasUserPrivilege(con, prof.usr_ent_id, prof.current_role, aeOpenItem.FTN_OI_MGT_ADMIN)) {
            //if(!(prof.sysRoles.contains(dbUserGroup.USG_ROLE_ADMIN))) {
                if(!(aeOpenItem.checkOpenItemPermission(con, prof.usr_ent_id, item_assigned)))
                    // message not correct , correct it later
                    throw new cwSysMessage("NO_RIGHT_UPDATE_MSG");
            }
        }
        */

        //calculate require amount
        float require_amt = 0;
        for(int i=0; i<item_assigned.length; i++) {
            if(amount_assigned[i]<0)
                throw new cwException("Cannot assign -ve to Open Item.");
            else
                require_amt += amount_assigned[i];
        }
        require_amt = aeUtils.roundingFloat(require_amt,2);


        stmt = con.prepareStatement(GET_USER_DETIAL);
        stmt.setLong(1, ent_id);
        rs = stmt.executeQuery();
        if(rs.next()) {
            result.append("<account_owner ent_id=\"").append(ent_id).append("\" ");
            result.append("usr_id=\"").append(dbUtils.esc4XML(rs.getString("USRSID"))).append("\" ");
            result.append("first_name=\"").append(dbUtils.esc4XML(rs.getString("Fname"))).append("\" ");
            result.append("last_name=\"").append(dbUtils.esc4XML(rs.getString("Lname"))).append("\" ");
            result.append("display_name=\"").append(dbUtils.esc4XML(rs.getString("Dname"))).append("\" ");
            result.append("/>").append(dbUtils.NEWL);
        }
        stmt.close();





        // check on account amount of offset transaction
        float on_acc_amt = 0;
        if(axn_type.equalsIgnoreCase(OFFSET)) {
            axn_acn_id = aeAccount.getAccountId(con, ent_id, acn_type);

            float OSAMT = 0;
            stmt = con.prepareStatement(GET_ONACCOUNT_OPENITEM);//,ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stmt.setInt(1, axn_acn_id);
            stmt.setString(2,ONACCOUNT);
            stmt.setString(3,axn_ccy);
            rs = stmt.executeQuery();
            if(rs.next()) {
                on_account_item = rs.getInt("OIMID");
                OSAMT = rs.getFloat("OSAMT");
                on_acc_amt = aeUtils.roundingFloat(0-OSAMT,2);
            }
            stmt.close();

            if( require_amt > on_acc_amt )
                throw new cwSysMessage(NOT_ENOUGH_MONEY_ON_ACCOUNT);
        }


        if( axn_type.equalsIgnoreCase(PAYMENT) )
            if( require_amt > axn_amt )
                throw new cwSysMessage(NOT_ENOUGH_MOMEY);



        //Get detial of the open item and check the amount assigned
        float remain_amt = 0;
        String OIMCCY;
        float OIMORGAMT;
        float OIMOSAMT;
        String OIMSTATUS;
        String OIMTYPE;
        String OIMDESC;
        Timestamp OIMDUEDATE;
        String OIMUSRID;
        String OIMUPDUSRID;
        String OIMSRC;
        String OIMREF;
        Timestamp OIMUPDDATE;

        result.append("<open_items>").append(dbUtils.NEWL);

        open_item_os = new float[item_assigned.length];
        open_item_ref = new String[item_assigned.length];
        open_item_src = new String[item_assigned.length];

        for(int i=0; i<item_assigned.length; i++) {
            stmt = con.prepareStatement(GET_OPENITEM);//,ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stmt.setInt(1, item_assigned[i]);
            rs = stmt.executeQuery();
            if(rs.next()) {
                OIMCCY = rs.getString("OIMCCY");
                OIMORGAMT = rs.getFloat("OIMORGAMT");
                OIMOSAMT = rs.getFloat("OIMOSAMT");
                OIMSTATUS = rs.getString("OIMSTATUS");
                OIMTYPE = rs.getString("OIMTYPE");
                OIMDESC = rs.getString("OIMDESC");
                OIMDUEDATE = rs.getTimestamp("OIMDUEDATE");
                OIMUSRID = rs.getString("OIMUSRID");
                OIMUPDDATE = rs.getTimestamp("OIMUPDDATE");
                OIMUPDUSRID = rs.getString("OIMUPDUSRID");
                OIMSRC = rs.getString("OIMSRC");
                OIMREF = rs.getString("OIMREF");
                open_item_os[i] = OIMOSAMT;
                open_item_src[i] = OIMSRC;
                open_item_ref[i] = OIMREF;
                total_os += OIMOSAMT;
                total_org += OIMORGAMT;

                if(OIMTYPE.equalsIgnoreCase(ONACCOUNT))
                    throw new cwException(message3);

                result.append("<open_item id=\"").append(item_assigned[i]).append("\" ");
                result.append("ccy=\"").append(OIMCCY).append("\" ");
                result.append("org_amount=\"").append(aeUtils.twoDecPt(OIMORGAMT)).append("\" ");
                result.append("status=\"").append(OIMSTATUS).append("\" ");
                result.append("desc=\"").append(dbUtils.esc4XML(OIMDESC)).append("\" ");
                result.append("due_date=\"").append(OIMDUEDATE).append("\" ");
                result.append("oim_create_usr_id=\"").append(OIMUSRID).append("\" ");
                result.append("oim_upd_usr_id=\"").append(OIMUPDUSRID).append("\" ");
                if( OIMSTATUS.equalsIgnoreCase(OUTSTANDING) ) {
                    if(OIMOSAMT >= amount_assigned[i]) {
                        result.append("pay_amount=\"").append(aeUtils.twoDecPt(amount_assigned[i])).append("\" ");
                        result.append("os_amount=\"").append(aeUtils.twoDecPt(aeUtils.roundingFloat(OIMOSAMT-amount_assigned[i],2))).append("\" ");
                        result.append("transaction_detial=\"").append(message1).append("\" />").append(dbUtils.NEWL);
                    }
                    else {
                        result.append("pay_amount=\"0.00\" ").append(dbUtils.NEWL);
                        result.append("os_amount=\"").append(aeUtils.twoDecPt(OIMOSAMT)).append("\" ");
                        result.append("transaction_detial=\"").append(message2).append("\" />").append(dbUtils.NEWL);
                        axn_desc = message2;
                        remain_amt += amount_assigned[i];
                        transaction_status = false;
                    }
                }else {
                    result.append("pay_amount=\"0.00\" ").append(dbUtils.NEWL);
                    result.append("os_amount=\"").append(aeUtils.twoDecPt(OIMOSAMT-amount_assigned[i])).append("\" ");
                    result.append("transaction_detial=\"");
                    if( OIMSTATUS.equalsIgnoreCase(PROCESSED) ) {
                        result.append(message4);
                        axn_desc = message4;
                    }
                    else if( OIMSTATUS.equalsIgnoreCase(CANCELLED) ) {
                        result.append(message5);
                        axn_desc = message5;
                    }
                    //else if( OIMSTATUS.equalsIgnoreCase(ONHOLD) )
                    //    result.append(message6);
                    result.append("\" />").append(dbUtils.NEWL);
                    remain_amt += amount_assigned[i];
                    transaction_status = false;
                }
            }
            stmt.close();
        }
        result.append("</open_items>").append(dbUtils.NEWL);


        result.append("<transaction ");
        if(transaction_status) {
            if(flag)
                result.append(" status=\"DONE\" ");
            else
                result.append(" status=\"SUCCESSFULLY\" ");
        }
        else
            result.append(" status=\"FAILURE\" ");

        result.append("remain_amount=\"").append(aeUtils.twoDecPt(remain_amt)).append("\" ");
        result.append(" pay_amount=\"").append(aeUtils.twoDecPt(require_amt)).append("\" ");
        result.append(" on_account_amount=\"").append(aeUtils.twoDecPt(aeUtils.roundingFloat(axn_amt-require_amt,2))).append("\" ");
        result.append(" total_os=\"").append(aeUtils.twoDecPt(aeUtils.roundingFloat(total_os-require_amt,2))).append("\" ");
        result.append(" total_org=\"").append(aeUtils.twoDecPt(aeUtils.roundingFloat(total_org,2))).append("\" />").append(dbUtils.NEWL);


        return transaction_status;
    }



    public void createTransaction(Connection con, loginProfile prof, boolean flag)
        throws cwSysMessage, cwException, SQLException, qdbException, IOException {

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try{
            axn_create_timestamp = dbUtils.getTime(con);
        }catch(qdbException e) {
            throw new cwException(e.getMessage());
        }

        // Create transaction method XML
        StringBuffer method_xml = new StringBuffer();
        method_xml.append("<method_detial>");
        if( axn_method.equalsIgnoreCase(CHEQUE) || axn_method.equalsIgnoreCase(BYPOST) ) {
                method_xml.append("<ref_no>").append(ref_no).append("</ref_no>").append(dbUtils.NEWL);
            }
        method_xml.append("</method_detial>");
        axn_method_xml = method_xml.toString();

        axn_acn_id = aeAccount.getAccountId(con, ent_id, acn_type);

        String CREATE_ACCOUNT_TRANSACTION
            = " INSERT INTO aeAccountTransaction ( axn_acn_id, axn_ccy, axn_amt, axn_type, "
            + " axn_desc, axn_method, axn_method_xml, axn_ref, axn_create_timestamp, axn_create_usr_id, axn_status ) "
            + " VALUES ( ?, ?, ?, ?, ?, ?," + cwSQL.getClobNull() + ", ?, ?, ? , ?) ";



        stmt = con.prepareStatement(CREATE_ACCOUNT_TRANSACTION, PreparedStatement.RETURN_GENERATED_KEYS);
        stmt.setInt(1, axn_acn_id);
        stmt.setString(2, axn_ccy);
        stmt.setFloat(3, axn_amt);
        stmt.setString(4, axn_type);
        stmt.setString(5, axn_desc);
        stmt.setString(6, axn_method);
        //stmt.setString(7, axn_method_xml);
        stmt.setString(7, axn_ref);
        stmt.setTimestamp(8, axn_create_timestamp);
        stmt.setString(9, prof.usr_id);
        if(flag) // flag == true if created by admin
            stmt.setString(10, SUCCESS);
        else
            stmt.setString(10, PENDING);
        if( stmt.executeUpdate() != 1 ) {
            stmt.close();
            throw new SQLException("Failed to add an Account Transaction.");
        }
        axn_id =(int) cwSQL.getAutoId(con, stmt, "aeAccountTransaction", "axn_id");
        stmt.close();
        
        // Update axn_method_xml
        // for oracle clob
        String condition = "axn_id = " + axn_id;
        String tableName = "aeAccountTransaction";
        String[] colName = {"axn_method_xml"};
        String[] colValue = {axn_method_xml};
        cwSQL.updateClobFields(con, tableName, colName, colValue, condition);
        
        return;
    }

    public boolean commitOnlinePayment(Connection con,loginProfile prof,StringBuffer result)
    throws cwSysMessage, cwException, SQLException, qdbException, IOException {
        PreparedStatement stmt = null;

        axn_desc = PROCESSING_ONLINE_PAYMENT;
        createTransaction(con,prof,false);
        if(!paymentValidation(con, prof, result, true)) {

            return false;
        } else {

            return true;
        }
    }


    public boolean commitOfflinePayment(Connection con,loginProfile prof,StringBuffer result)
    throws cwSysMessage, cwException, SQLException, qdbException, IOException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        axn_desc = INVALID_TRANSACTION;
        createTransaction(con,prof,true);

        if(!paymentValidation(con, prof, result, true)) {
            return false;
        } else {
            // update the os amount on the open items
            float require_amount = 0;
            for(int i=0; i<item_assigned.length; i++) {
                stmt = con.prepareStatement(UPDATE_OPENITEM_OS);
                stmt.setFloat(1, open_item_os[i] - amount_assigned[i]);
                require_amount += amount_assigned[i];
                require_amount = aeUtils.roundingFloat(require_amount,2);
                stmt.setInt(3, item_assigned[i]);
                // if os amount is 0 , admit the item
                if(open_item_os[i] == amount_assigned[i]) {
                    aeAdapter.updAppn(con, prof.usr_id, open_item_src[i], open_item_ref[i]);
                    stmt.setString(2,PROCESSED);
                }
                else
                    stmt.setString(2,OUTSTANDING);

                if( stmt.executeUpdate() != 1 ) {
                    stmt.close();
                    throw new SQLException("Failed to update the open item . ");
                }
                stmt.close();
            }

            if( axn_type.equalsIgnoreCase(OFFSET) ) {
                updateOnAccountAmount(con,on_account_item,require_amount);
            }
            else {
                // if amount in the transaction more than the amount assigned to the item
                // put the money to on account item
                if(axn_amt > require_amount) {
                    stmt = con.prepareStatement(GET_ONACCOUNT_ID);
                    stmt.setInt(1,axn_acn_id);
                    stmt.setString(2,axn_ccy);
                    stmt.setString(3,ONACCOUNT);
                    rs = stmt.executeQuery();
                    if(rs.next()) {
                        int OIMID = rs.getInt("OIMID");
                        updateOnAccountAmount(con,OIMID,aeUtils.roundingFloat(0 - (axn_amt - require_amount),2));
                    }
                    // if on account item not exist, create for the user
                    else {
                        aeOpenItem oItem = new aeOpenItem(con, ent_id, acn_type);
                        oItem.oim_ccy=axn_ccy;
                        oItem.oim_due_datetime = Timestamp.valueOf(dbUtils.MAX_TIMESTAMP);
                        oItem.oim_src = APPLYEASY;
                        oItem.oim_ref = "0";
                        oItem.oim_org_amt = 0;
                        oItem.oim_os_amt = aeUtils.roundingFloat(0 - (axn_amt - require_amount),2);
                        oItem.oim_type = ONACCOUNT;
                        oItem.oim_create_usr_id = prof.usr_id;
                        oItem.ins();
                    }
                    stmt.close();
                }
            }

            // create open item allocation
            for(int i=0; i<item_assigned.length; i++)
                aeAccountTransaction.createAllocation(con,item_assigned[i],axn_id,amount_assigned[i],false);


            // Update transaction description
            stmt = con.prepareStatement(UPDATE_ACCOUNTTRANSACTION_DESC);
            stmt.setString(1, UPDATE_SUCCESSFULLY_ADMIN);
            stmt.setString(2, SUCCESS);
            stmt.setInt(3, axn_id);
            if( stmt.executeUpdate() != 1 ) {
                stmt.close();
                throw new SQLException("Failed to update the account transaction ");
            }
            stmt.close();

            return true;
        }
    }


    public static void createAllocation(Connection connection, int oal_oim_id, int oal_axn_id, float oal_amt_applied, boolean oal_discnt_taken)
        throws SQLException {
            PreparedStatement stmt = null;
            stmt = connection.prepareStatement(CREATE_OPENITEM_ALLOCATION);
            stmt.setInt(1, oal_oim_id);
            stmt.setInt(2, oal_axn_id);
            stmt.setFloat(3, oal_amt_applied);
            stmt.setBoolean(4, oal_discnt_taken);
            if( stmt.executeUpdate() != 1) {
                stmt.close();
                throw new SQLException("Failed to add data to Open Item Allocation Table.");
            }
            stmt.close();
        }



    public boolean updateOnlinePayment(Connection con, int axn_axn_id, float axn_axn_amt, String axn_axn_desc, StringBuffer result, boolean flag)
        throws cwSysMessage, cwException, SQLException, qdbException, IOException , UnsupportedEncodingException{
            axn_type=PAYMENT;
            axn_id = axn_axn_id;
            axn_amt = axn_axn_amt;
            axn_desc = dbUtils.unicodeFrom(axn_axn_desc,"GB2312","GB2312");
            // Get the data stroed in account transaction reference
            PreparedStatement stmt = null;
            ResultSet rs = null;
            String axn_axn_ref[];
            stmt = con.prepareStatement(GET_TRANSACTION_REF);
            stmt.setInt(1,axn_id);
            rs = stmt.executeQuery();
            if(rs.next()) {
                axn_create_usr_id = rs.getString("AXNUSRID");
                axn_axn_ref = aeReqParam.split(rs.getString("AXNREF"),"&");
                ent_id = Long.parseLong(axn_axn_ref[0]);
                axn_ccy = axn_axn_ref[1];
                acn_type = axn_axn_ref[2];
                item_assigned = aeReqParam.String2int(aeReqParam.split(axn_axn_ref[3],","));
                amount_assigned = aeReqParam.Stringto2decPtfloat(aeReqParam.split(axn_axn_ref[4],"~"));
                label_lan = axn_axn_ref[5];
            }
            stmt.close();

            if(paymentValidation(con, null, result, true)) {
                // update the os amount on the open items
                float require_amount = 0;
                for(int i=0; i<item_assigned.length; i++) {
                    stmt = con.prepareStatement(UPDATE_OPENITEM_OS);
                    stmt.setFloat(1, open_item_os[i] - amount_assigned[i]);
                    require_amount += amount_assigned[i];
                    require_amount = aeUtils.roundingFloat(require_amount,2);
                    stmt.setInt(3, item_assigned[i]);

                    // if os amount is 0 , admit the item
                    if(open_item_os[i] == amount_assigned[i]) {
                        aeAdapter.updAppn(con, axn_create_usr_id, open_item_src[i], open_item_ref[i]);
                        stmt.setString(2,PROCESSED);
                    }
                    else
                        stmt.setString(2,OUTSTANDING);

                    if( stmt.executeUpdate() != 1 ) {
                        stmt.close();
                        throw new SQLException("Failed to update the open item . ");
                    }
                    stmt.close();

                }
                // create open item allocation
                for(int i=0; i<item_assigned.length; i++)
                    aeAccountTransaction.createAllocation(con,item_assigned[i],axn_id,amount_assigned[i],false);

                // Update transaction description
                stmt = con.prepareStatement(UPDATE_ACCOUNTTRANSACTION_DESC);
                stmt.setString(1, axn_desc);
                stmt.setString(2, SUCCESS);
                stmt.setInt(3, axn_id);
                if( stmt.executeUpdate() != 1 ) {
                    stmt.close();
                    throw new SQLException("Failed to update the account transaction ");
                }
                stmt.close();

                return true;
            } else {
                stmt = con.prepareStatement(GET_ONACCOUNT_ID);
                stmt.setInt(1,axn_acn_id);
                stmt.setString(2,axn_ccy);
                stmt.setString(3,ONACCOUNT);
                rs = stmt.executeQuery();
                if(rs.next()) {
                    int OIMID = rs.getInt("OIMID");
                    updateOnAccountAmount(con,OIMID,aeUtils.roundingFloat(0 - axn_amt, 2));
                }
                // if on account item not exist, create for the user
                else {
                    aeOpenItem oItem = new aeOpenItem(con, ent_id, acn_type);
                    oItem.oim_ccy=axn_ccy;
                    oItem.oim_org_amt = 0;
                    oItem.oim_os_amt = aeUtils.roundingFloat(0 - axn_amt, 2);
                    oItem.oim_type = ONACCOUNT;
                    oItem.oim_create_usr_id = axn_create_usr_id;
                    oItem.ins();
                }
                stmt.close();

                // Update transaction description
                stmt = con.prepareStatement(UPDATE_ACCOUNTTRANSACTION_DESC);
                stmt.setString(1, axn_desc);
                stmt.setString(2, FAILURE);
                stmt.setInt(3, axn_id);
                if( stmt.executeUpdate() != 1 ) {
                    stmt.close();
                    throw new SQLException("Failed to update the account transaction ");
                }
                stmt.close();


                return false;
            }

        }


    public static void updateTransactionDesc(Connection con, int axn_axn_id, String axn_axn_desc)
    throws SQLException , UnsupportedEncodingException{
        PreparedStatement stmt = null;
        ResultSet rs = null;
        stmt = con.prepareStatement(UPDATE_ACCOUNTTRANSACTION_DESC);
        stmt.setString(1, dbUtils.unicodeFrom(axn_axn_desc,"GB2312","GB2312"));
        stmt.setString(2, FAILURE);
        stmt.setInt(3, axn_axn_id);
        if( stmt.executeUpdate() != 1 ) {
            stmt.close();
            throw new SQLException("Failed to update the account transaction ");
        }
        stmt.close();

    }

    public void updateOnAccountAmount(Connection con, int oim_id, float oim_os)
        throws SQLException {
        PreparedStatement stmt = null;
        stmt = con.prepareStatement(UPDATE_ONACCOUNT_AMOUNT);
        stmt.setFloat(1, oim_os );
        stmt.setInt(2,oim_id);
        if( stmt.executeUpdate() != 1 ) {
            stmt.close();
            throw new SQLException("Failed to update the on account open item . ");
        }
        stmt.close();
    }


    public long getEntId(Connection con, int axn_axn_id)
        throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        axn_id = axn_axn_id;
        long axn_ent_id = 0;
        String axn_axn_ref[];
        stmt = con.prepareStatement(GET_TRANSACTION_REF);
        stmt.setInt(1,axn_id);
        rs = stmt.executeQuery();
        if(rs.next()) {
            axn_axn_ref = aeReqParam.split(rs.getString("AXNREF"),"&");
            ent_id = Long.parseLong(axn_axn_ref[0]);
            label_lan = axn_axn_ref[5];
        }
        stmt.close();
        return axn_ent_id;
    }

    public String formData(Connection con, String CoNo)
        throws qdbException {
        StringBuffer result = new StringBuffer();
        String Url = "https://www.bj.cmbchina.com/netpayment/BaseHttp.dll?PrePay1";
        String MerchantUrl = "";//"http://cw01:130/servlet/com.cw.wizbank.ae.aeAction?env=wizb&cmd=commit_online_payment&stylesheet_fail=ae_adm_pay_search.xsl&stylesheet_success=ae_adm_pay_search.xsl";
        String cur_time = (dbUtils.getTime(con)).toString();
        String yy = cur_time.substring(0, 4);
        String mm = cur_time.substring(5, 7);
        String dd = cur_time.substring(8, 10);
        result.append("<form_data action=\"").append(Url).append("\">").append(dbUtils.NEWL);
        result.append("<input name=\"CoNo\">").append(CoNo).append("</input>").append(dbUtils.NEWL);
        result.append("<input name=\"BillNo\">").append(sixLength(axn_id)).append("</input>").append(dbUtils.NEWL);
        result.append("<input name=\"Date\">").append(yy+mm+dd).append("</input>").append(dbUtils.NEWL);
        result.append("<input name=\"Amount\">").append(axn_amt).append("</input>").append(dbUtils.NEWL);
        //result.append("<input name=\"MerchantUrl\">").append(MerchantUrl).append("</input>").append(dbUtils.NEWL);
        result.append("</form_data>");

        return result.toString();
    }


    public static String sixLength(int axn_axn_id) {
        String idStr = Integer.toString(axn_axn_id);
        int length = idStr.length();
        while(length < 6) {
            idStr = "0" + idStr;
            length = idStr.length();
        }
        return "4" + idStr.substring(1);
    }

    public void setSessionValue(Connection connection, HttpSession sess, loginProfile prof)
        throws SQLException,cwException {
        sess.setAttribute("AXNCCY", axn_ccy);
        // online payment
        if(axn_type == null)
            axn_type = PAYMENT;
        sess.setAttribute("AXNTYPE", axn_type);
        if(axn_method == null)
            axn_method = CASH;
        sess.setAttribute("AXNMETHOD", axn_method);
        sess.setAttribute("REFNO", ref_no);
        sess.setAttribute("ACNTYPE", acn_type);
        sess.setAttribute("ITEM", aeUtils.intArray2Vec(item_assigned));
        sess.setAttribute("ENTID", new Long(ent_id));
        int axnacnid = aeAccount.getAccountId(connection, ent_id, acn_type);
        sess.setAttribute("AXNACNID", new Integer(axnacnid));


        StringBuffer AXNREF = new StringBuffer();
        AXNREF.append(ent_id).append("&").append(axn_ccy).append("&").append(acn_type).append("&");

        //concate open items id to string
        StringBuffer AMT = new StringBuffer();
        for(int i=0; i<item_assigned.length; i++) {
            AMT.append(item_assigned[i]);
            if(i+1 < item_assigned.length)
                AMT.append(",");
        }
        AXNREF.append(AMT.toString());

        // online payment, no partial payment, calculate the require amount of each open item
        float require_amt = 0;
        if(amount_assigned == null) {
            PreparedStatement stmt = connection.prepareStatement("SELECT oim_id, oim_os_amt FROM aeOpenItem WHERE oim_id IN ( " + AMT.toString() + ") ");
            ResultSet rs = stmt.executeQuery();
            Hashtable oimAmt = new Hashtable();
            while(rs.next()) {
                oimAmt.put(new Long(rs.getLong("oim_id")), new Float(rs.getFloat("oim_os_amt")));
                require_amt += rs.getFloat("oim_os_amt");
            }
            stmt.close();
            require_amt = aeUtils.roundingFloat(require_amt,2);
            amount_assigned = new float[oimAmt.size()];
            for(int i=0; i<item_assigned.length; i++)
                if( oimAmt.containsKey(new Long(item_assigned[i])) )
                    amount_assigned[i] = ((Float)oimAmt.get(new Long(item_assigned[i]))).floatValue();
                else
                    throw new cwException("item not exists , id = " + item_assigned[i]);
        }
        sess.setAttribute("AMOUNT", aeUtils.floatArray2Vec(amount_assigned));


        if(axn_amt == 0)
            axn_amt = require_amt;
        sess.setAttribute("AXNAMT", new Float(axn_amt));

        AXNREF.append("&");
        for(int i=0; i<amount_assigned.length; i++) {
            AXNREF.append(amount_assigned[i]);
            if(i+1 < amount_assigned.length)
                AXNREF.append("~");
        }

        AXNREF.append("&").append(prof.label_lan);


        sess.setAttribute("AXNREF", AXNREF.toString());

        return;
    }

    public void getSessionValue(HttpSession sess) {
        axn_acn_id = ((Integer)sess.getAttribute("AXNACNID")).intValue();
        axn_ccy = (String)sess.getAttribute("AXNCCY");
        axn_amt = ((Float)sess.getAttribute("AXNAMT")).floatValue();
        axn_type = (String)sess.getAttribute("AXNTYPE");
        axn_method = (String)sess.getAttribute("AXNMETHOD");
        ref_no = (String)sess.getAttribute("REFNO");
        acn_type = (String)sess.getAttribute("ACNTYPE");
        item_assigned = aeUtils.vec2intArray((Vector)sess.getAttribute("ITEM"));
        amount_assigned = aeUtils.vec2floatArray((Vector)sess.getAttribute("AMOUNT"));
        ent_id = ((Long)sess.getAttribute("ENTID")).longValue();
        //axn_ref = "RESERVED";
        axn_desc = "RESERVED";
        axn_ref = (String)sess.getAttribute("AXNREF");
        return;
    }



}


