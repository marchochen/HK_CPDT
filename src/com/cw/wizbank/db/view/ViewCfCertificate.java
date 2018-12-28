package    com.cw.wizbank.db.view;

import java.util.Hashtable;
import java.util.Vector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.db.sql.*;
/**
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public class ViewCfCertificate {
    private static boolean DEBUG;
    private Connection con;
    private static final String CTF_CERTIFICATE_STATUS_TYPE = "cfCertificateStatus";
    private static final String CTF_CERTIFICATION_STATUS_TYPE = "cfCertificationStatus";
    private static final String SMSG_GET_CTF_NO_EXIST = "No Certificate Found";
    private static final String SMSG_GET_CFN_NO_EXIST = "No Certification Found";
    private static final String SMSG_CTF_STATUS_NO_EXIST = "No Certification Status Found!";

    private static final String SMSG_CTF_UPD_FAIL = "Fail to Update the Certificate";
    private static final String SMSG_CTF_DEL_FAIL = "Fail to Delete the Certificate";
    private static final String SMSG_CFN_UPD_FAIL = "Fail to Update the Certification";
    private static final String SMSG_CFN_INS_FAIL = "Fail to Insert the Certification";
    private static final String SMSG_CFN_DEL_FAIL = "Fail to Delete the Certification";
    /**
     *
     * @param con
     * @throws SQLException
     */
    public ViewCfCertificate(Connection con) throws SQLException {
      if (con == null) {
      throw new SQLException("connection not available");
      } else {
    this.con = con;
      }
    }
    /**
     *
     */
    public ViewCfCertificate() {

    }
    public String getCtfTitle(int ctf_id) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_ctf_title);
        stmt.setInt(1,ctf_id);
        ResultSet rs = stmt.executeQuery();
        String ctf_title = "";
        while (rs.next()) {
            ctf_title = rs.getString("ctf_title");
        }
        stmt.close();
        return ctf_title;
    }
    /**
     *
     * @param ctf_id
     * @param ctf_title
     * @param ctf_status
     * @param ctf_link
     * @param owner_ent_id
     * @param usr_id
     * @throws SQLException
     * @throws cwSysMessage
     * @throws cwException
     */
    public long insertCertificate(String ctf_title,String ctf_status,String ctf_link,int owner_ent_id, String usr_id)
            throws SQLException  {
        //
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_ins_ctf, PreparedStatement.RETURN_GENERATED_KEYS);

        // get timestamp from database
        Timestamp timestampDb = cwSQL.getTime(con);
        //(ctf_title, ctf_status, ctf_link,ctf_owner_ent_id,ctf_create_timestamp,ctf_create_usr_id,ctf_upd_timestamp,ctf_upd_usr_id
        int count = 0;
        stmt.setString(++count,ctf_title);
        stmt.setString(++count,ctf_status);
        stmt.setString(++count,ctf_link);
        stmt.setInt(++count,owner_ent_id);
        stmt.setTimestamp(++count,timestampDb);
        stmt.setString(++count,usr_id);
        stmt.setTimestamp(++count,timestampDb);
        stmt.setString(++count,usr_id);
        //
        if (stmt.executeUpdate() < 1) {
//            throw new cwSysMessage(SMSG_CFN_INS_FAIL);
        }
        long ctf_id = cwSQL.getAutoId(con, stmt, "cfCertificate", "ctf_id");
        stmt.close();
        return ctf_id;
    }

    /**
     *
     * @param ctf_id
     * @param owner_ent_id
     * @throws SQLException
     * @throws cwSysMessage
     * @throws cwException
     */
    public void deleteCertificate(int ctf_id, int owner_ent_id) throws SQLException ,cwSysMessage,cwException {
        //first del cfn where cfn_ctf_id = ctf_id
        PreparedStatement stmt1 = con.prepareStatement(SqlStatements.sql_del_cfn_ctf_id);
        int count = 0;
        stmt1.setInt(++count,ctf_id);
        stmt1.setInt(++count,owner_ent_id);
        if (stmt1.executeUpdate() < 1) {
//            throw new cwSysMessage(SMSG_CTF_DEL_FAIL);
        }
        stmt1.close();
        // del ctf
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_del_ctf);
        count = 0;
        stmt.setInt(++count,ctf_id);
        stmt.setInt(++count,owner_ent_id);
        if (stmt.executeUpdate() < 1) {
//            throw new cwSysMessage(SMSG_CTF_DEL_FAIL);
        }
        stmt.close();
    }
    /**
     *
     * @param ctf_id
     * @param ctf_title
     * @param owner_ent_id
     * @param usr_id
     * @throws SQLException
     * @throws cwSysMessage
     * @throws cwException
     */
    public void updateCertificate(int ctf_id, String ctf_title, int owner_ent_id,String usr_id)
        throws SQLException ,cwSysMessage,cwException {
        StringBuffer sql = new StringBuffer(1024);
        sql.append(SqlStatements.sql_upd_ctf);

        PreparedStatement stmt = con.prepareStatement(sql.toString());

        // get timestamp from database
        Timestamp timestampDb = cwSQL.getTime(con);
        //set param
        int count = 0;
        stmt.setString(++count,ctf_title);
        stmt.setTimestamp(++count,timestampDb);
        stmt.setString(++count,usr_id);
        stmt.setInt(++count,owner_ent_id);
        stmt.setInt(++count,ctf_id);


        if (stmt.executeUpdate() < 1) {
//            throw new cwSysMessage(SMSG_CTF_UPD_FAIL);
        }
        stmt.close();

    }

    /**
     *
     * @param status
     * @param ctf_id_lst
     * @param owner_ent_id
     * @param usr_id
     * @throws SQLException
     * @throws cwSysMessage
     * @throws cwException
     */
    public void updateCertificateStatus(String status, String[] ctf_id_lst, int owner_ent_id,String usr_id)
        throws SQLException ,cwSysMessage,cwException {
        StringBuffer sql = new StringBuffer(1024);
        sql.append(SqlStatements.sql_upd_ctf_status);
        sql.append("(0 ");
        if ((ctf_id_lst != null)&&(ctf_id_lst.length >0)) {
            for (int i=0;i<ctf_id_lst.length;i++) {

                sql.append(",").append(ctf_id_lst[i]);
            }
        }
        sql.append(" )");
        //System.out.println("sql= "+sql+status+"\r");
        PreparedStatement stmt = con.prepareStatement(sql.toString());

        // get timestamp from database
        Timestamp timestampDb = cwSQL.getTime(con);
        //set param
        int count = 0;
        stmt.setString(++count,status);
        stmt.setTimestamp(++count,timestampDb);
        stmt.setString(++count,usr_id);
        stmt.setInt(++count,owner_ent_id);
        stmt.setString(++count,status);

        if (stmt.executeUpdate() < 1) {
//            throw new cwSysMessage(SMSG_CTF_UPD_FAIL);
//        } else {
//            System.out.print("upd success");
        }
        stmt.close();

    }

    /**
     * @param ctf_id
     * @param lrn_ent_id
     * @param owner_ent_id
     * @param usr_id
     */
     //insertCertification(cfn_ctf_id, cfn_ent_id, cfn_qualification_ind,cfn_status, owner_ent_id,usr_id);
    public void insertCertification(int cfn_ctf_id, int cfn_ent_id, int cfn_qualification_ind,String cfn_status,int owner_ent_id, String usr_id)
           throws SQLException ,cwSysMessage,cwException {
        //
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_ins_cfn);

        // get timestamp from database
        Timestamp timestampDb = cwSQL.getTime(con);
        //(cfn_ctf_id,cfn_ent_id,cfn_qualification_ind,cfn_status,cfn_owner_ent_id,
        // cfn_create_timestamp,cfn_create_usr_id,cfn_upd_timestamp,cfn_upd_usr_id)
        int count = 0;
        stmt.setInt(++count,cfn_ctf_id);
        stmt.setInt(++count,cfn_ent_id);
        stmt.setInt(++count,cfn_qualification_ind);
        stmt.setString(++count,cfn_status);
        stmt.setInt(++count,owner_ent_id);
        stmt.setTimestamp(++count,timestampDb);
        stmt.setString(++count,usr_id);
        stmt.setTimestamp(++count,timestampDb);
        stmt.setString(++count,usr_id);
        //
        if (stmt.executeUpdate() < 1) {
//            throw new cwSysMessage(SMSG_CFN_INS_FAIL);
        }
        stmt.close();

        //

    }

    /**
     * @param ctf_id
     * @param lrn_ent_id
     */
    public void deleteCertification(int cfn_ctf_id, int cfn_ent_id, int owner_ent_id)
           throws SQLException {

        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_del_cfn);
        int count = 0;
        stmt.setInt(++count,cfn_ctf_id);
        stmt.setInt(++count,cfn_ent_id);
        stmt.setInt(++count,owner_ent_id);
        if (stmt.executeUpdate() < 1) {
//            throw new cwSysMessage(SMSG_CFN_DEL_FAIL);
        }
        stmt.close();

    }
    public int getCertificateUsrNum(int owner_ent_id,int ctf_id,String ctf_status) throws SQLException {
        //
        StringBuffer sql = new StringBuffer(1024);
        if (ctf_status.equalsIgnoreCase("Certified")) {
            sql.append(SqlStatements.sql_get_ctf_usr_count_certified);
        } else {
            if (ctf_status.equalsIgnoreCase("Not Certified")) {
                sql.append(SqlStatements.sql_get_ctf_usr_count_not_certified);
            }
        }
        int ctf_num = 0;
        PreparedStatement stmt = con.prepareStatement(sql.toString());
        int count = 0;
        stmt.setInt(++count,owner_ent_id);
        stmt.setInt(++count,owner_ent_id);
        stmt.setInt(++count,ctf_id);

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            ctf_num = rs.getInt("count_cf");
        }
        stmt.close();
        return ctf_num;
    }
    /**
     *
     * @param ctf_id
     * @param order
     * @return
     * @throws SQLException
     * @throws cwSysMessage
     */
    public Hashtable[] getCertificateUsrLst(int ctf_id,String filter_status,String order)
           throws SQLException,cwSysMessage {
        // sql
        StringBuffer sql = new StringBuffer(1024);
        sql.append(SqlStatements.sql_get_ctf_usr_lst);
        if (filter_status != null) {
            if (filter_status.equalsIgnoreCase("Certified")) {
                sql.append(SqlStatements.sql_get_ctf_ust_lst_status_cert);
            } else {
                if ((filter_status != null)&&(filter_status.equalsIgnoreCase("Not Certified"))) {
                    sql.append(SqlStatements.sql_get_ctf_ust_lst_status_not_cert);
                }
            }
        }
        sql.append(SqlStatements.sql_get_ctf_ust_lst_order);
        if ((order != null) &&(order.equalsIgnoreCase("DESC"))) {
            sql.append(" DESC");
        }else {
            sql.append(" ASC");
        }
        PreparedStatement stmt = con.prepareStatement(sql.toString());
        stmt.setInt(1,ctf_id);
        ResultSet rs = stmt.executeQuery();

//        if (rs.isAfterLast() && rs.isBeforeFirst()) {
//            throw new cwSysMessage(SMSG_GET_CTF_NO_EXIST);
//        }

        Hashtable[] resultTb = procResult(rs);
        stmt.close();
        return resultTb;
    }
    public void updateCertificationStatus(String cfn_status,String[] cfn_ent_id_lst, String[] cfn_ctf_id_lst, int owner_ent_id,String usr_id)
           throws SQLException ,cwSysMessage,cwException {
        //
        StringBuffer sql = new StringBuffer(1024);
        sql.append(SqlStatements.sql_upd_cfn_status);
        sql.append("(0 ");
        if ((cfn_ctf_id_lst != null)&&(cfn_ctf_id_lst.length >0)) {
            for (int i=0;i<cfn_ctf_id_lst.length;i++) {
                sql.append(",").append(cfn_ctf_id_lst[i]);
            }
        }
        sql.append(" )");
        //
        sql.append(" and cfn_ent_id in (-1 ");
        if ((cfn_ent_id_lst != null)&&(cfn_ent_id_lst.length >0)) {
            for (int j=0;j<cfn_ent_id_lst.length;j++) {

                sql.append(",").append(cfn_ent_id_lst[j]);
            }
        }
        sql.append(" )");
        PreparedStatement stmt = con.prepareStatement(sql.toString());

        // get timestamp from database
        Timestamp timestampDb = cwSQL.getTime(con);
        //set param
        int count = 0;
        stmt.setString(++count,cfn_status);
        stmt.setTimestamp(++count,timestampDb);
        stmt.setString(++count,usr_id);
        stmt.setInt(++count,owner_ent_id);
        stmt.setString(++count,cfn_status);

        if (stmt.executeUpdate() < 1) {
//            throw new cwSysMessage(SMSG_CFN_UPD_FAIL);
        }
        stmt.close();

    }



    /**
     *
     * @param owner_ent_id
     * @param filter_status
     * @param order
     * @return
     * @throws SQLException
     * @throws cwSysMessage
     */
    public Hashtable[] getCertificateStatusList(String status_ind)
           throws SQLException ,cwSysMessage{
        //prepare sql

        StringBuffer sql = new StringBuffer(1024);
        sql.append(SqlStatements.sql_get_cfCertificateStatus);

        PreparedStatement stmt = con.prepareStatement(sql.toString());
        // set parameter
        int count = 0;
        String ind = "";
        if (status_ind.equalsIgnoreCase("ctf")) {
            ind = CTF_CERTIFICATE_STATUS_TYPE;
        } else if (status_ind.equalsIgnoreCase("cfn")) {
            ind = CTF_CERTIFICATION_STATUS_TYPE;
        }

        stmt.setString(++count, ind);

        ResultSet rs = stmt.executeQuery();

//        if (rs.isAfterLast() && rs.isBeforeFirst()) {
//            throw new cwSysMessage(SMSG_CTF_STATUS_NO_EXIST);
//        }

        Hashtable[] resultTb = procResult(rs);
        stmt.close();
        return resultTb;
    }
    //
    public int getCertificateNum(String ctf_status,int owner_ent_id) throws SQLException {
        StringBuffer sql = new StringBuffer(1024);
        if (ctf_status.equalsIgnoreCase("ON")) {
            sql.append(SqlStatements.sql_get_ctf_status_on_num);
        } else {
            if (ctf_status.equalsIgnoreCase("OFF")) {
                sql.append(SqlStatements.sql_get_ctf_status_off_num);
            }
        }
        int ctf_num = 0;
        PreparedStatement stmt = con.prepareStatement(sql.toString());
        stmt.setInt(1,owner_ent_id);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            ctf_num = rs.getInt("count_cf");
        }

        stmt.close();
        return ctf_num;

    }
    /**
     *
     * @param owner_ent_id
     * @param filter_status
     * @param order
     * @return
     * @throws SQLException
     * @throws cwSysMessage
     */
    public Hashtable[] getCertificateDtlList(int owner_ent_id, String filter_status, String order)
           throws SQLException ,cwSysMessage{
//System.out.println("BEGIN getCertificateDtlList");
        //prepare sql

        StringBuffer sql = new StringBuffer(1024);
        sql.append(OuterJoinSqlStatements.getCtfDtlList());
        if (filter_status != null) {
            if (filter_status.equalsIgnoreCase("ON")) {
               sql.append(SqlStatements.sql_ctf_status_on);
            } else {
                if (filter_status.equalsIgnoreCase("OFF")) {
                   sql.append(SqlStatements.sql_ctf_status_off);
                }
            }
        }
        sql.append(SqlStatements.sql_get_ctf_dtl_num_groupby);

        if ((order != null) &&(order.equalsIgnoreCase("DESC"))) {
            sql.append(" DESC");
        }else {
            sql.append(" ASC");
        }

//System.out.println("getCertificateDtlList : " + sql.toString());
        PreparedStatement stmt = con.prepareStatement(sql.toString());
        // set parameter
        int count = 0;
        stmt.setInt(++count, owner_ent_id);

        ResultSet rs = stmt.executeQuery();

//        if (rs.isAfterLast() && rs.isBeforeFirst()) {
//            throw new cwSysMessage(SMSG_GET_CTF_NO_EXIST);
//        }

        Hashtable[] resultTb = procResult(rs);
        stmt.close();
        return resultTb;
    }
    public Hashtable[] getCertificateAppliedList(int owner_ent_id, String filter_status, String order)
           throws SQLException ,cwSysMessage{
//System.out.println("BEGIN getCertificateAppliedList");
        //prepare sql

        StringBuffer sql = new StringBuffer(1024);
        sql.append(OuterJoinSqlStatements.getCtfAppliedNum());
        if (filter_status != null) {
            if (filter_status.equalsIgnoreCase("ON")) {
                sql.append(SqlStatements.sql_ctf_status_on);
            } else {
                if (filter_status.equalsIgnoreCase("OFF")) {
                    sql.append(SqlStatements.sql_ctf_status_off);
                }
            }
        }
        sql.append(SqlStatements.sql_get_ctf_applied_num_groupby);

        if ((order != null) &&(order.equalsIgnoreCase("DESC"))) {
            sql.append(" DESC");
        }else {
            sql.append("ASC");
        }

//System.out.println("getCertificateAppliedList : " + sql.toString());
        PreparedStatement stmt = con.prepareStatement(sql.toString());
        // set parameter
        int count = 0;
        stmt.setInt(++count, owner_ent_id);

        ResultSet rs = stmt.executeQuery();

//        if (rs.isAfterLast() && rs.isBeforeFirst()) {
//            throw new cwSysMessage(SMSG_GET_CTF_NO_EXIST);
//        }

        Hashtable[] resultTb = procResult(rs);
        stmt.close();
        return resultTb;
    }
//    /**
//     * @param ctf_id
//     * @param lrn_ent_id
//     * @param usr_id
//     * @param status
//     */
//    public void setCertificationStatus(int ctf_id, int lrn_ent_id, int usr_id, String status) {
//
//    }

    /**
     * @param ctf_id
     * @param lrn_ent_id
     * @return String
     */
    public String getCertificationStatus(int cfn_ctf_id, int cfn_ent_id)
           throws SQLException {
        //
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_cfn_usr_status);
        int count = 0;
        stmt.setInt(++count,cfn_ctf_id);
        stmt.setInt(++count,cfn_ent_id);
        ResultSet rs = stmt.executeQuery();
        String cfn_status = "";
        while (rs.next()) {
            cfn_status = rs.getString("cfn_status");
        }
        stmt.close();
        return cfn_status;
    }


    /**
     * from batch.QualifyBatch
     * @param ctf_id
     * @param lrn_ent_id
     * @param qualification
     */
    public void setQualificationInd(int ctf_id, int lrn_ent_id, boolean qualification)
        throws SQLException {
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_upd_qualify_ind);
        int count = 1;
//        System.out.println("in set qualification= "+qualification);
        if (qualification) {
            stmt.setInt(count++, 1);
        } else {
            stmt.setInt(count++, 0);
        }
        stmt.setInt(count++, ctf_id);
        stmt.setInt(count++, lrn_ent_id);
        stmt.execute();
        stmt.close();

        return;
    }

    /**
     * from batch.QualifyBatch
     * @param ctf_id
     * @param lrn_ent_id
     * @return boolean
     */
    public boolean getQualificationInd(int ctf_id, int lrn_ent_id) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_qualify_ind);
        int count = 1;
        stmt.setInt(count++, ctf_id);
        stmt.setInt(count++, lrn_ent_id);
        ResultSet rs = stmt.executeQuery();
        boolean cfn_qualification_ind = false;
        while (rs.next()) {
            cfn_qualification_ind = (rs.getInt("cfn_qualification_ind") == 1) ? true : false;
        }
        stmt.close();

        return cfn_qualification_ind;
    }
    // process the ResultSet
    // static for other use;maybe put into a new class , or util class
    // return Hashtable[]
    public static Hashtable[] procResult(ResultSet rs) throws SQLException {
//System.out.println("BEGIN procResult");
        Hashtable record = new Hashtable();
        Vector result = new Vector();

        ResultSetMetaData rsmd = rs.getMetaData();
        //for loop variable colName
        int columnCount = rsmd.getColumnCount();
        String colName = "";
        while (rs.next()) {
            record = new Hashtable();
            for(int i = 1 ;i <= columnCount; i++) {
                // getColumnName
                colName = rsmd.getColumnName(i).toLowerCase();
                // put into the Hashtable
                //System.out.println(""+colName+rs.getObject(colName));
                if ((rs.getObject(colName)) == null) {
                    // if null put a String object("") into the hashtable
                    // "" will be the value of the property or the content of the element
                    // to be confirmed from front(xsl,javascript.etc)
                    record.put(colName,"");
                } else {
                    record.put(colName,rs.getObject(colName));
                }
//System.out.println(colName+" = "+rs.getObject(colName));
            }
            result.addElement(record);
        }
        Hashtable[] resultArray = new Hashtable[result.size()];
//System.out.println("END procResult");
        return (Hashtable[])result.toArray(resultArray);
    }

    // added for aeLearningPlan!
    // for certification status
    private static String CERTIFICATION_LST_XML_CACHE;
    public String getCertificationLstAsXML() throws SQLException, cwSysMessage {
        if (CERTIFICATION_LST_XML_CACHE==null) {
            PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_cfCertificateStatus);
            // set parameter
            stmt.setString(1, CTF_CERTIFICATION_STATUS_TYPE);
    
            ResultSet rs = stmt.executeQuery();
    		boolean hasRecord = false;
    
            StringBuffer result = new StringBuffer(1024);
            result.append("<certificate_status_list>");
            while (rs.next()) {
    			hasRecord = true;
                result.append("<status id=\"").append(rs.getString("ctb_id"))
                      .append("\" title=\"").append(rs.getString("ctb_title"))
                      .append("\"/>");
            }
            result.append("</certificate_status_list>");
    		if (!hasRecord) {
    			throw new cwSysMessage(SMSG_CTF_STATUS_NO_EXIST);
    		}
    
            stmt.close();
            CERTIFICATION_LST_XML_CACHE = result.toString();
        }
        return CERTIFICATION_LST_XML_CACHE;
    }
    // add for certification aeItem linkage
    public Hashtable[] getItyCtfInd(int itm_id)
           throws SQLException ,cwSysMessage{
        //
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_ity_ctf_ind);
        // set parameter
        stmt.setInt(1, itm_id);

        ResultSet rs = stmt.executeQuery();
        Hashtable[] resultTb = procResult(rs);
        rs.close();
        stmt.close();
        return resultTb;
    }
    public Hashtable[] getItyCtfIndAppID(int app_id)
           throws SQLException ,cwSysMessage{
        //
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_ity_ctf_ind_appID);
        // set parameter
        stmt.setInt(1, app_id);

        ResultSet rs = stmt.executeQuery();
        Hashtable[] resultTb = procResult(rs);
        stmt.close();
        return resultTb;
    }

}
