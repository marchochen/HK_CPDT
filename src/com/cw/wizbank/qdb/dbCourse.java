package com.cw.wizbank.qdb;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import com.cw.wizbank.accesscontrol.AcCourse;
import com.cw.wizbank.accesscontrol.AcResources;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.accesscontrol.acSite;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeUtils;
import com.cw.wizbank.ae.itemCompleteDelete;
import com.cw.wizbank.ae.db.sql.SqlStatements;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.db.DbCourseModuleCriteria;
import com.cw.wizbank.db.DbTrackingHistory;
import com.cw.wizbank.db.sql.OuterJoinSqlStatements;
import com.cw.wizbank.db.view.ViewModuleEvaluation;
import com.cw.wizbank.personalization.PsnBiography;
import com.cw.wizbank.tree.cwTree;
import com.cw.wizbank.util.EntityFullPath;
import com.cw.wizbank.util.MYSQLDbHelper;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwPagination;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.common.SNS;
import com.cwn.wizbank.services.SnsDoingService;
import com.cwn.wizbank.utils.CommonLog;
import com.cwn.wizbank.web.WzbApplicationContext;

public class dbCourse extends dbResource {
    /**
     * CLOB column
     * Table:       Course
     * Column:      cos_content_xml cos_structure_xml   cos_import_xml
     * Nullable:    YES             YES                 YES
     */
    public static final String COS_ENROLL_OK = "OK";
    public static final String ITEM_TYPE_FDR = "FDR";

    private static final String SESS_ASS_TIMESTAMP = "sess_ass_timestamp";
    private static final String SESS_ASS_QUEUE_HASH = "sess_ass_queue_hash";
    private static final String SESS_ASS_QUEUE_STAT = "sess_ass_queue_stat";
    private static final String SESS_TST_TIMESTAMP = "sess_tst_timestamp";
    private static final String SESS_TST_QUEUE_HASH = "sess_tst_queue_hash";
    private static final String SESS_TST_QUEUE_STAT = "sess_tst_queue_stat";
    private static final int PAGE_SIZE = 10;
	private final static String DEFAULT_ENC = "UnicodeLittle";
	private static final String NOT_SUBMITTED = "not_submitted";
	private static final String NOT_GRADED = "not_graded";
	private static final String GRADED = "graded";
	private static final String ALL = "all";
    public  static final String FCN_COS_MGT_IN_ORG = "COS_MGT_IN_ORG";

    // Lun
    static final String SUB_SQL_IN_getAllCosModAsXML
    //= " SELECT distinct res_id RID, res_type RTYPE, res_subtype RSUBTYPE, "
    = " SELECT distinct res_id RID, rcn_res_id COS_ID, res_type RTYPE, res_subtype RSUBTYPE, "
    + " rcn_sub_nbr RSUBNBR, rcn_score_multiplier RMUL, rcn_order RORDER, "
    + " res_privilege RPRIV, res_status RSTATUS, res_usr_id_owner ROWNER, "
    + " res_lan RLAN,res_difficulty RDIFF, res_duration RDUR, "
//    + " res_lan RLAN,res_difficulty RDIFF, res_duration RDUR, res_desc RDESC, "
    + " res_instructor_name RISTNAME, res_instructor_organization RISTORG, "
    + " res_tpl_name RTPLNAME , res_upd_date  RTIMESTAMP, res_title RTITLE, "
    + " mod_max_score MMAXSCORE, mod_pass_score MPASSSCORE, mod_instruct MINSTRUCT, "
    + " mod_eff_start_datetime EFF_START, mod_eff_end_datetime EFF_END, ";

    static final String stmt2_IN_getAllCosModAsXML
    = " SELECT COUNT(*) FROM ProgressAttempt where atm_pgr_res_id = ? or atm_int_res_id = ? " ;

    final static String GET_COS_TITLE
    = " SELECT res_title RTITLE FROM Resources WHERE res_id = ? ";
    String SQL_getIsEnrollmentRelatedAsXML = "select itm_run_ind from Course, aeItem where cos_res_id = ? and cos_itm_id = itm_id and itm_type = 'CLASSROOM' and (itm_content_def = 'PARENT' or itm_content_def is null) ";

    public long cos_res_id;
    public long cos_itm_id;
    public String cos_pre_test_req_bil ;
    public String cos_post_test_req_bil ;
    public Timestamp cos_eff_start_datetime;
    public Timestamp cos_eff_end_datetime;
    public String cos_content_xml;
    public String cos_structure_xml;
    public String cos_structure_json;
    public String cos_aicc_version;
    public String cos_vendor;
    public long cos_max_normal;
    public String cos_chapter_id;
	public String cos_node_id;
	public long vod_res_id;
	public boolean is_hkib_vod;
	public boolean is_click_node;
	public boolean is_complete_del;

//    public long cos_ccr_id;

    //For Content Licence
    public String cos_lic_key;
    public long lic_quota;

    private static final String SESS_LRN_COS_RPT = "sess_lrn_cos_rpt_";
    private static final String SESS_COS_LRN_LST = "sess_cos_lrn_lst_";
    private static final String HASH_COS_ID      = "cos_id_";
    private static final String HASH_GROUP_ID    = "group_id_";
    private static final String HASH_ENT_ID_VEC  = "ent_id_vector_";
    private static final String HASH_ORDER_BY    = "order_by_";
    private static final String HASH_SORT_BY  = "sort_by_";

    private static final int MAX_NORMAL = 1;

    public long tkh_id = DbTrackingHistory.TKH_ID_UNDEFINED;
    public dbCourse() {
        super.SQL_getIsEnrollmentRelatedAsXML = SQL_getIsEnrollmentRelatedAsXML;
    }
    public dbCourse(long inResID) {
        super();
        this.res_id = inResID;
        this.cos_res_id = inResID;
        super.SQL_getIsEnrollmentRelatedAsXML = SQL_getIsEnrollmentRelatedAsXML;
    }

    // for AICC import
    // by cliff, 2001/4/18
//    public Vector vtCosDescriptor;

    public static long getCosResId(Connection con, long itm_id) throws SQLException {
        long res_id;
        final String SQL = " Select cos_res_id From Course "
                         + " Where cos_itm_id = ? ";

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, itm_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next())
            res_id = rs.getLong(1);
        else
            res_id = 0;
        stmt.close();
        return res_id;
    }

    public static long getCosItemId(Connection con, long res_id) throws SQLException {
        long itm_id;
        final String SQL = " Select cos_itm_id From Course "
                         + " Where cos_res_id = ? ";

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, res_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next())
            itm_id = rs.getLong(1);
        else
        {
            stmt.close();
            throw new SQLException("Cannot find course. cos_res_id = " + res_id);
        }
        stmt.close();
        return itm_id;
    }
/*
    public static long getCosCriteriaId(Connection con, long res_id) throws SQLException {
        long ccr_id;
        final String SQL = " Select cos_ccr_id From Course "
                         + " Where cos_res_id = ? ";

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, res_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next())
            ccr_id = rs.getLong(1);
        else {
            stmt.close();
            throw new SQLException("Cannot find course. cos_res_id = " + res_id);
        }
        return ccr_id;
    }
*/
/*
    public static void updCosCriteriaId(Connection con, long res_id, long ccr_id) throws SQLException {
        final String SQL = " UPDATE Course SET cos_ccr_id = ? "
                         + " Where cos_res_id = ? ";

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, ccr_id);
        stmt.setLong(2, res_id);

        stmt.executeUpdate();
        stmt.close();
    }
*/
    public static String getOwnerName(Connection con, long res_id) throws qdbException {
        try {
            String OwnerName = "";
            String SQL = "SELECT usr_display_bil "
                       + "FROM RegUser, Resources "
                       + "WHERE res_usr_id_owner = usr_id "
                       + "AND res_id = ? ";

            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, res_id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()==true)
                OwnerName = rs.getString("usr_display_bil");
            else
            {
                stmt.close();
                throw new qdbException ("Cannot find course owner.");
            }

            stmt.close();
            return OwnerName;
        }
        catch(SQLException e) {
            throw new qdbException (e.getMessage());
        }
    }

    public void aeIns(Connection con, loginProfile prof)
        throws qdbException
    {
        ins(con, prof);
    }

    public void ins(Connection con, loginProfile prof)
        throws qdbException
    {
        try {
            res_type = RES_TYPE_COS;
            // should be implement at front end
            //res_status = RES_STATUS_ON;
            // calls dbResource.ins()
            //System.out.println(res_status);
            super.ins(con);
            // if ok.
            cos_res_id = res_id;

            //String clobNull = cwSQL.getClobNull(con);

            // << BEGIN for oracle migration!
            PreparedStatement stmt = con.prepareStatement(
                "INSERT INTO Course "
                + " ( cos_lic_key, cos_res_id, cos_itm_id ) "
                + " VALUES (?,?,?)" );
                //+ " , cos_post_test_req_bil "
                //+ " , cos_content_xml, cos_structure_xml ) "
                //+ " VALUES (?,?,?,NULL,NULL," + clobNull + "," + clobNull +")" );

            stmt.setString(1, this.cos_lic_key);
            stmt.setLong(2, cos_res_id);
            stmt.setLong(3, cos_itm_id);
            //stmt.setString(2, cos_content_xml);
            //stmt.setString(3, cos_structure_xml);
            // >> END
//add stmt.close()
//modified by Lear
            int stmtResult=stmt.executeUpdate();
            stmt.close();
            if ( stmtResult!=1)
            {
                con.rollback();
                throw new qdbException("Failed to add a Course.");
            }
            else
            {
                // << BEGIN for oracle migration!
/*                stmt = con.prepareStatement(
                      " SELECT cos_content_xml, cos_structure_xml FROM Course "
                    + "     WHERE cos_res_id = ? FOR UPDATE ",
                    ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE );


                stmt.setLong(1, cos_res_id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next())
                {
                    cwSQL.setClobValue(con, rs, "cos_content_xml", cos_content_xml);
                    cwSQL.setClobValue(con, rs, "cos_structure_xml", cos_structure_xml);
                    rs.updateRow();
                }
                stmt.close();
*/
                // Update cos_content_xml, cos_structure_xml
                // construct the condition
                String condition = "cos_res_id = " + cos_res_id;
                // construct the column & value
                String[] columnName = new String[2];
                String[] columnValue = new String[2];
                columnName[0] = "cos_content_xml";
                columnValue[0] = cos_content_xml;
                columnName[1] = "cos_structure_xml";
                columnValue[1] = cos_structure_xml;
                cwSQL.updateClobFields(con, "Course", columnName, columnValue, condition);
                // >> END

                // insert into Access Control Table
                AcCourse accos = new AcCourse(con);
                boolean read = true;
                boolean write = true;
                boolean exec = true;
                dbResourcePermission.save(con,cos_res_id,res_upd_user,prof.current_role,read,write,exec);

                // commit() at qdbAction.java
                updateEffDatetime(con);
                return;
            }

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    private static final String sql_upd_cos_eff_datetime =
        "update Course set cos_eff_start_datetime = ?, " +
        "cos_eff_end_datetime = ? " +
        "where cos_res_id = ? ";
    //Dennis, impl cos release control
    //it updates the cos_eff_start/end_datetime in Course
    //with the class valiables cos_in_eff_start/end_datetime
    public void updateEffDatetime(Connection con)
        throws qdbException
    {
        try {
            if(cos_eff_start_datetime != null && dbUtils.isMinTimestamp(cos_eff_start_datetime)) {
                cos_eff_start_datetime = dbUtils.getTime(con);
            }
            PreparedStatement stmt = con.prepareStatement(sql_upd_cos_eff_datetime);
            if(cos_eff_start_datetime == null) {
                stmt.setNull(1, Types.TIMESTAMP);
            } else {
                stmt.setTimestamp(1, cos_eff_start_datetime);
            }
            if(cos_eff_end_datetime == null) {
                stmt.setNull(2, Types.TIMESTAMP);
            } else {
                stmt.setTimestamp(2, cos_eff_end_datetime);
            }
            stmt.setLong(3,cos_res_id);

//add stmt.close()
//modified by Lear
            int stmtResult=stmt.executeUpdate();
            stmt.close();
            if ( stmtResult!=1) {
                con.rollback();
                throw new qdbException("Failed to update status.");
            }
        }
        catch(SQLException e) {
            throw new qdbException(e.getMessage());
        }
    }

    // cliff, AICC import timestamp
    public static Timestamp getCosImportDate(Connection con, long id)
        throws qdbException {
            try {
                String SQL;
                Timestamp curTime = null;

                SQL = "Select cos_import_datetime "
                    + "From Course "
                    + "Where cos_res_id = ?";

                PreparedStatement stmt = con.prepareStatement(SQL);
                stmt.setLong(1, id);
                ResultSet rs = stmt.executeQuery();
                if(rs.next() == true)
                    curTime = rs.getTimestamp(1);
                else
                {
                    stmt.close();
                    throw new qdbException("Cannot find the Course: " + id);
                }

                stmt.close();
                return curTime;

            }
            catch (SQLException e) {
                throw new qdbException(e.getMessage());
            }
    }

    //Dennis,  impl cos release control
    //it gets the cos_eff_start_datetime from Course
    public static Timestamp getCosStart(Connection con, long id)
        throws qdbException {
            try {
                String SQL;
                Timestamp curTime = null;

                SQL = "Select cos_eff_start_datetime "
                    + "From Course "
                    + "Where cos_res_id = ?";

                PreparedStatement stmt = con.prepareStatement(SQL);
                stmt.setLong(1, id);
                ResultSet rs = stmt.executeQuery();
                if(rs.next() == true)
                    curTime = rs.getTimestamp(1);
                else
                {
                    stmt.close();
                    throw new qdbException("Cannot find the Course: " + id);
                }

                stmt.close();
                return curTime;

            }
            catch (SQLException e) {
                throw new qdbException(e.getMessage());
            }
    }

    //Dennis, impl cos release control
    //it gets the cos_eff_end_datetime from Course
    public static Timestamp getCosEnd(Connection con, long id)
        throws qdbException {
            try {
                String SQL;
                Timestamp curTime = null;

                SQL = "Select cos_eff_end_datetime "
                    + "From Course "
                    + "Where cos_res_id = ?";

                PreparedStatement stmt = con.prepareStatement(SQL);
                stmt.setLong(1, id);
                ResultSet rs = stmt.executeQuery();
                if(rs.next() == true)
                    curTime = rs.getTimestamp(1);
                else
                {
                    stmt.close();
                    throw new qdbException("Cannot find the Module: " + id);
                }

                stmt.close();
                return curTime;
            }
            catch (SQLException e) {
                throw new qdbException(e.getMessage());
            }
    }

    public static String getCosStructureAsXML(Connection con, long cosId)
        throws qdbException
    {
        try {
            PreparedStatement stmt = con.prepareStatement(
            " SELECT  cos_structure_xml FROM Course "
            + " where cos_res_id = ? ");

            // set the values for prepared statements
            stmt.setLong(1, cosId);
            String xml = new String("");

            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                //xml = rs.getString("cos_structure_xml");
                xml = cwSQL.getClobValue(rs, "cos_structure_xml");
            }
            else
            {
                stmt.close();
                throw new qdbException( "No data for course. id = " + cosId );
            }

            stmt.close();

            return xml;

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    public void updCosImportStructure(Connection con, String cos_import_xml)
        throws qdbException
    {
        try {
/*
            PreparedStatement stmt = con.prepareStatement(
                " UPDATE Course  SET cos_import_xml = ?"
                + " WHERE cos_res_id = ? ");

            StringReader sr_cos_import_xml = new StringReader(cos_import_xml);

            stmt.setCharacterStream(1, sr_cos_import_xml, cos_import_xml.length());
            stmt.setLong(2, cos_res_id);
            stmt.executeUpdate();
            stmt.close();

            sr_cos_import_xml.close();

            PreparedStatement stmt = con.prepareStatement(
                " UPDATE Course  SET cos_import_xml= " + cwSQL.getClobNull(con)
                + " WHERE cos_res_id = ? ");

            stmt.setLong(1, cos_res_id);
            stmt.executeUpdate();
            stmt.close();

            stmt = con.prepareStatement(
                      " SELECT cos_import_xml FROM Course "
                    + "     WHERE cos_res_id = ? FOR UPDATE "
                    ,ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE );
                stmt.setLong(1, cos_res_id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next())
                {
                    cwSQL.setClobValue(con, rs, "cos_import_xml", cos_import_xml);
                    rs.updateRow();
                }
                stmt.close();
*/
            // << BEGIN for oracle migration!
            // Update cos_import_xml
            // construct the condition
            String condition = "cos_res_id = " + cos_res_id;
            // construct the column & value
            String[] columnName = new String[1];
            String[] columnValue = new String[1];
            columnName[0] = "cos_import_xml";
            columnValue[0] = cos_import_xml;
            cwSQL.updateClobFields(con, "Course", columnName, columnValue, condition);

            super.updateTimeStamp(con);
        }
        catch(SQLException e) {
            throw new qdbException(e.getMessage());
        }
    }

    public void updCosStructure(Connection con)
        throws qdbException, cwException
    {
        try {
            // << BEGIN for oracle migration!
/*            PreparedStatement stmt = con.prepareStatement(
                " UPDATE Course  SET cos_structure_xml= " + cwSQL.getClobNull(con)
                + " WHERE cos_res_id = ? ");

            stmt.setLong(1, cos_res_id);
            stmt.executeUpdate();
            stmt.close();

            stmt = con.prepareStatement(
                      " SELECT cos_structure_xml FROM Course "
                    + "     WHERE cos_res_id = ? FOR UPDATE "
                    ,ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE );
                stmt.setLong(1, cos_res_id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next())
                {
                    cwSQL.setClobValue(con, rs, "cos_structure_xml", cos_structure_xml);
                    rs.updateRow();
                }
                stmt.close();
*/
            // Update cos_structure_xml
            // construct the condition
            String condition = "cos_res_id = " + cos_res_id;
            // construct the column & value
            String[] columnName = new String[2];
            String[] columnValue = new String[2];
            columnName[0] = "cos_structure_xml";
            columnValue[0] = cos_structure_xml;
            columnName[1] = "cos_structure_json";
            if(cos_structure_json == null && cos_structure_xml != null) {
            	cos_structure_json = qdbAction.static_env.transformXML(cos_structure_xml.replaceAll("&quot;", " "), "cos_structure_json_js.xsl",null);
            }
            columnValue[1] = cos_structure_json;

            cwSQL.updateClobFields(con, "Course", columnName, columnValue, condition);
            // >> END

            super.updateTimeStamp(con);
        }
        catch(SQLException e) {
            throw new qdbException(e.getMessage());
        }
    }

    public void updCosStructureNoTimestampCheck(Connection con, loginProfile prof)
        throws qdbException, qdbErrMessage, cwException
    {
        res_id = cos_res_id;

        res_upd_user = prof.usr_id;

        // check User Right
        //if (!dbResourcePermission.hasPermission(con, cos_res_id, prof,
        //                               dbResourcePermission.RIGHT_WRITE)) {
        //    throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
        //}
//        checkModifyPermission(con, prof);
        updCosStructure(con);
    }

    public void updCosStructure(Connection con, loginProfile prof)
        throws qdbException, qdbErrMessage, cwException
    {
            res_id = cos_res_id;

            res_upd_user = prof.usr_id;

            // check User Right
            //if (!dbResourcePermission.hasPermission(con, cos_res_id, prof,
            //                                dbResourcePermission.RIGHT_WRITE)) {
            //    throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
            //}
            /*checkModifyPermission(con, prof);*/
            super.checkTimeStamp(con);

            updCosStructure(con);

    }

    public void get(Connection con)
            throws qdbException, cwSysMessage
    {
        try {
            res_id = cos_res_id;

            super.get(con);

            PreparedStatement stmt = con.prepareStatement(
            " SELECT  cos_itm_id "
            + " ,cos_pre_test_req_bil "
            + " ,cos_post_test_req_bil "
            + " ,cos_eff_start_datetime "
            + " ,cos_eff_end_datetime "
            + " ,cos_content_xml "
            + " ,cos_structure_xml "
//            + " ,cos_ccr_id "
            + " ,cos_aicc_version "
            + " ,cos_vendor "
            + " ,cos_max_normal "
            + " FROM Course "
            + " where cos_res_id =?");

            // set the values for prepared statements
            stmt.setLong(1, cos_res_id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                cos_itm_id              = rs.getLong("cos_itm_id");
                cos_pre_test_req_bil    = rs.getString("cos_pre_test_req_bil");
                cos_post_test_req_bil    = rs.getString("cos_post_test_req_bil");
                cos_eff_start_datetime  = rs.getTimestamp("cos_eff_start_datetime");
                cos_eff_end_datetime    = rs.getTimestamp("cos_eff_end_datetime");
                //cos_content_xml         = rs.getString("cos_content_xml");
                //cos_structure_xml       = rs.getString("cos_structure_xml");
                cos_content_xml         = cwSQL.getClobValue(rs, "cos_content_xml");
                cos_structure_xml       = cwSQL.getClobValue(rs, "cos_structure_xml");
                cos_aicc_version        = rs.getString("cos_aicc_version");
                cos_vendor              = rs.getString("cos_vendor");
                cos_max_normal          = rs.getLong("cos_max_normal");
//                cos_ccr_id       = rs.getLong("cos_ccr_id");
            }
            else
            {
                stmt.close();
                throw new qdbException( "No data for course. id = " + res_id );

            }

            stmt.close();
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    public void aeUpd(Connection con, loginProfile prof)
        throws qdbException, cwSysMessage
    {
        res_id = cos_res_id;
        res_type = dbResource.RES_TYPE_COS;
        super.aeUpd(con);
        updCos(con, prof);
    }

    public void upd(Connection con, loginProfile prof)
        throws qdbException, cwSysMessage
    {
        try {
            res_id = cos_res_id;
            super.checkTimeStamp(con);
            // check User Right
    /*        if (!dbResourcePermission.hasPermission(con, cos_res_id, prof,
                                            dbResourcePermission.RIGHT_WRITE)) {
                throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
            }
    */

            /*checkModifyPermission(con, prof);*/
            res_type = dbResource.RES_TYPE_COS;
            super.upd(con);
            // if ok.
            updCos(con,prof);
        }
        catch(qdbErrMessage e) {
            throw new cwSysMessage(e.getId());
        }
    }

    public void updCos(Connection con, loginProfile prof)
        throws qdbException, cwSysMessage
    {
        try {

            PreparedStatement stmt = con.prepareStatement(
                "UPDATE Course "
                + " SET cos_pre_test_req_bil = ? , cos_post_test_req_bil = ?, cos_lic_key = ? "
                + " WHERE cos_res_id = ? " );

            stmt.setString(1, cos_pre_test_req_bil);
            stmt.setString(2, cos_post_test_req_bil);
            stmt.setString(3, cos_lic_key);
            stmt.setLong(4, cos_res_id);
//add stmt.close()
//modified by Lear
            int stmtResult=stmt.executeUpdate();
            stmt.close();
            if ( stmtResult!=1)
            {
                con.rollback();
                throw new qdbException("Failed to update the Course.");
            }
            else
            {
                // commit() at qdbAction.java
                updateEffDatetime(con);
                return;
            }

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    public void updAiccCos(Connection con, loginProfile prof, String cos_import_xml, String cos_aicc_version, String cos_vendor, int cos_max_normal, boolean boolUpdCosStructure, boolean boolUpdTimeStamp) throws qdbException, qdbErrMessage {
        updAiccCos(con, prof, cos_import_xml, cos_aicc_version, cos_vendor, cos_max_normal, boolUpdCosStructure, boolUpdTimeStamp, true);
    }

    // update addititonal fields for Course which will only be used when importing AICC course or importing/updating AICC AUs
    public void updAiccCos(Connection con, loginProfile prof, String cos_import_xml, String cos_Aicc_Version, String cos_Vendor, int cos_Max_Normal, boolean boolUpdCosStructure, boolean boolUpdTimeStamp, boolean boolUpdImportTimeStamp)
        throws qdbException, qdbErrMessage
    {
        try {
            if (boolUpdCosStructure == true) {
                updCosImportStructure(con, cos_import_xml);
            }

            Timestamp curTime = dbUtils.getTime(con);

    		StringBuffer sql = new StringBuffer();
    		sql.append("UPDATE Course SET  cos_aicc_version = ? , cos_vendor = ? , cos_max_normal = ?");
    		if (boolUpdImportTimeStamp || cos_max_normal ==-1)
    			sql.append(" ,cos_import_datetime = ? ");
            sql.append(" WHERE cos_res_id = ? ");
            PreparedStatement stmt = con.prepareStatement(sql.toString());

            int index = 1;
            stmt.setString(index++, cos_Aicc_Version);
            stmt.setString(index++, cos_Vendor);
            stmt.setInt(index++, cos_Max_Normal);
            if(cos_max_normal == -1) {
            	 stmt.setNull(index++, java.sql.Types.TIMESTAMP);
            } else if (boolUpdImportTimeStamp) {
            	stmt.setTimestamp(index++, curTime);
            }



            stmt.setLong(index++, cos_res_id);

    //add stmt.close()
    //modified by Lear
	        int stmtResult=stmt.executeUpdate();
	        stmt.close();
	        if ( stmtResult!=1)
	        {
	            // rollback at qdbAction
	            //con.rollback();
	            throw new qdbException("Failed to update the Course.");
	        } else {
	            if (boolUpdTimeStamp == true) {
	                // commit() at qdbAction.java
	                updateTimeStamp(con, curTime);
	            }
	            return;
	        }
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    public String asXML(Connection con, loginProfile prof, String dpo_view,String itm_type, boolean is_new_cos)
        throws qdbException, SQLException, cwSysMessage
    {
            Timestamp eff_start_datetime = getCosStart(con, res_id);
            Timestamp eff_end_datetime = getCosEnd(con, res_id);
            Timestamp import_datetime = getCosImportDate(con, res_id);
            Timestamp cur_time = dbUtils.getTime(con);
            String tmp_end_datetime=dbUtils.convertEndDate(eff_end_datetime);
            if(itm_type == null || itm_type.trim().length() < 1){
    		    itm_type =getCosItemType(con, res_id) ;
    		}

            StringBuffer result = new StringBuffer();
            result.append(dbUtils.xmlHeader);
            result.append("<course id=\"").append(res_id).append("\" itm_id=\"").append(cos_itm_id).append("\" type=\"").append(itm_type).append("\" language=\"").append(res_lan).append("\" timestamp=\"").append(res_upd_date).append("\" ");
//            .append("\" criteria_id=\"").append(cos_ccr_id)
            // for AICC info.
            if (import_datetime != null) {
                result.append("AICC=\"true\" ");
                result.append("import_datetime=\"").append(import_datetime).append("\" ");
            }
            else {
                result.append("AICC=\"false\" ");
            }
            result.append(">").append(dbUtils.NEWL);
            // author's information
            result.append(prof.asXML()).append(dbUtils.NEWL);

            result.append("<header  privilege=\"" +  res_privilege) ;
            result.append("\" status=\"").append(res_status).append("\" type=\"").append(res_type);
            result.append("\" eff_start_datetime=\"").append(eff_start_datetime);
            result.append("\" eff_end_datetime=\"").append(tmp_end_datetime);
            result.append("\" cur_time=\"").append(cur_time).append("\">");
            result.append(dbUtils.NEWL);
            result.append("<title>").append(dbUtils.esc4XML(res_title)).append("</title>").append(dbUtils.NEWL);
            result.append("<desc>").append(dbUtils.esc4XML(res_desc)).append("</desc>").append(dbUtils.NEWL);
            result.append("<instructor>").append(dbUtils.esc4XML(res_instructor_name)).append("</instructor>").append(dbUtils.NEWL);
            result.append("<moderator>").append(dbUtils.esc4XML(getOwnerName(con))).append("</moderator>").append(dbUtils.NEWL);
            result.append("<organization>").append(dbUtils.esc4XML(res_instructor_organization)).append("</organization>").append(dbUtils.NEWL);
            result.append(getDisplayOption(con, dpo_view)).append(dbUtils.NEWL);
            result.append(getIsEnrollmentRelatedAsXML(con));
            result.append("</header>").append(dbUtils.NEWL);
            result.append(cos_structure_xml).append(dbUtils.NEWL);
            result.append("<enableAppletTree>").append(cwTree.enableAppletTree).append("</enableAppletTree>").append(dbUtils.NEWL);
            result.append("<itm_exam_ind>").append(aeItem.isExam(con, res_id)).append("</itm_exam_ind>").append(dbUtils.NEWL);


    		aeItem itm = new aeItem();
    		itm.itm_id = cos_itm_id;
    		itm.getItem(con);
    		result.append(itm.getContentDefXml(con, itm.itm_id));
    		result.append(aeItem.getNavAsXML(con, itm.itm_id));
    		result.append(aeItem.genItemActionNavXML(con, cos_itm_id, prof));
    		result.append("<is_new_cos>").append(is_new_cos).append("</is_new_cos>");

            result.append("</course>").append(dbUtils.NEWL);
            String xml = result.toString();

            return xml;
    }

    //折分xml
    public String CosStrtItmIDAndRef(String structure_xml, String frdStructure_xml, boolean is_first) {
    	int ad1 = structure_xml.indexOf("<item");
    	int lastad = structure_xml.lastIndexOf("</item>");
    	int ad = 0;
    	if (is_first){
        	ad = structure_xml.indexOf("<item");
        	frdStructure_xml = structure_xml.substring(0, ad);
    	}
    	String temp_xml = structure_xml.substring(ad1, lastad+7);
    	String tempmod = "";
    	String tempfrd = "";
    	int ad2 = temp_xml.indexOf("\">");
    	String headStr = temp_xml.substring(0, ad2);
    	int ad3 = 0;
    	int ad4 = 0;
    	if (headStr.indexOf("identifierref=")!= -1){
    		ad3 = temp_xml.indexOf("</item>")+7;
    		tempmod = temp_xml.substring(0, ad3);
    		ad4 = ad3;
        	if (is_first){
        		ad4 = ad3 + ad;
        	}
    	}else{
    		ad3 = temp_xml.indexOf("</item></item>")+14;
    		tempfrd = temp_xml.substring(0, ad3);
    		ad4 = ad3;
        	if (is_first){
        		ad4 = ad3 + ad;
        	}
    	}
    	if (tempmod.indexOf("<restype>SCO</restype>")!= -1){
    		frdStructure_xml += tempmod;
    	}
    	if (tempfrd.indexOf("<restype>SCO</restype>")!= -1){
    		frdStructure_xml += tempfrd;
    	}
    	structure_xml = structure_xml.substring(ad4, structure_xml.length());
    	if (ad4 < lastad){
    		CosStrtItmIDAndRef(structure_xml,frdStructure_xml,false);
    	}
    	frdStructure_xml += "</tableofcontents>";
        return frdStructure_xml;
    }

    public long getCosOwnerId(Connection con) throws SQLException {
        final String SQL = " Select itm_owner_ent_id From aeItem, Course "
                         + " Where cos_itm_id = itm_id "
                         + " And cos_res_id = ? ";

        long ownerId = 0;
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, cos_res_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next())
            ownerId = rs.getLong("itm_owner_ent_id");
        stmt.close();
        return ownerId;
    }

   // Check wether a user has right to modify a course according to ACL
   // Case 1 : User has write permission in the resource permission
   // Case 2 : User has COS_MGT_IN_ORG right in ACL
   /*
   public void checkModifyPermission(Connection con, loginProfile prof)
        throws qdbException, qdbErrMessage
   {
        try {

            boolean hasRight = false;
            if (dbResourcePermission.hasPermission(con, cos_res_id, prof,
                                            dbResourcePermission.RIGHT_WRITE)) {

                hasRight = true;
            }else {
                long cosOwnerId = getCosOwnerId(con);
                if(prof.root_ent_id == cosOwnerId) {
                    AccessControlWZB acl = new AccessControlWZB();
                    hasRight = acl.hasUserPrivilege(con, prof.usr_ent_id, FCN_COS_MGT_IN_ORG);
                }
            }

            if (!hasRight){
                throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
            }

        }catch (SQLException e) {
            throw new qdbException("SQL Error : " + e.getMessage());
        }
   }*/



    public void aeDel(Connection con, loginProfile prof)
        throws qdbException, qdbErrMessage, cwSysMessage {
         delCos(con, prof);
    }

    public void del(Connection con, loginProfile prof)
        throws qdbException, qdbErrMessage, cwSysMessage {
        // check User Right
/*        if (!dbResourcePermission.hasPermission(con, cos_res_id, prof,
                                        dbResourcePermission.RIGHT_WRITE)) {
            throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
        }
*/
        /*checkModifyPermission(con, prof);*/
        super.checkTimeStamp(con);
        delCos(con, prof);
    }

    public void delCos(Connection con, loginProfile prof)
        throws qdbException, qdbErrMessage, cwSysMessage
    {
        try {

            // Course Evaluation Checking
            /*
            long cnt = dbCourseEvaluation.attemptNum(con, cos_res_id);
            if (cnt > 0) {
                throw new qdbErrMessage("PGR002");
            }
            */

            // check dependency
            res_id = cos_res_id;
            Vector rcnVec = dbResourceContent.getChildAss(con, cos_res_id);

            for (int i=0;i<rcnVec.size();i++) {
                dbResourceContent dbrcn = (dbResourceContent) rcnVec.elementAt(i);


                long cnt = dbProgress.attemptNum(con, dbrcn.rcn_res_id_content);
                if (cnt > 0)
                        //Some of the module had been attempted.
                        throw new qdbErrMessage("COS001");

                // delete the module from the course
                dbModule dbmod = new dbModule();
                dbmod.res_id = dbrcn.rcn_res_id_content;
                dbmod.mod_res_id = dbrcn.rcn_res_id_content;
                dbmod.get(con);
                DbCourseModuleCriteria.delByModId(con, dbmod.mod_res_id);

                if (dbmod.res_subtype.equals("ASS")) {
                    dbAssignment ass = new dbAssignment();
                    ass.initialize(dbmod);
                    ass.del(con, prof);
                } else if (dbmod.res_subtype.equals("FOR")) {
                    dbForum forum = new dbForum(dbmod);
                    forum.del(con, prof);
                } else if (dbmod.res_subtype.equals("FAQ")) {
                    dbFaq faq = new dbFaq(dbmod);
                    faq.del(con, prof);
                } else if (dbEvent.isEventType(dbmod.res_subtype)) {
                    dbEvent evt = new dbEvent();
                    evt.initialize(dbmod);
                    evt.del(con, prof);
                } else {
                    dbmod.del(con, prof);
                }

                //System.out.println("Done.");
            }


            PreparedStatement stmt = con.prepareStatement(
                " DELETE From Enrolment "
            +   " WHERE enr_res_id = ? " );

            stmt.setLong(1, cos_res_id);
            stmt.executeUpdate();
            stmt.close();

            stmt = con.prepareStatement(
                 " DELETE From ResourceContent where rcn_res_id = ? ");

            stmt.setLong(1, cos_res_id);
            stmt.executeUpdate();
            stmt.close();

            // delete Resource Permission
            dbResourcePermission.delAll(con,cos_res_id);

            //delete Messages
            dbMessage.delAllMsg(con, cos_res_id);

            stmt = con.prepareStatement(
                " DELETE From ResourceObjective where rob_res_id = ?");

            stmt.setLong(1, cos_res_id);
            stmt.executeUpdate();
            stmt.close();

            //delete CourseEvaluation
            dbCourseEvaluation.delByCos(con, cos_res_id);

//            cos_ccr_id = getCosCriteriaId(con, cos_res_id);
            // delete course
            stmt = con.prepareStatement(
                "DELETE From Course where cos_res_id=? " );

            stmt.setLong(1, cos_res_id);

//add stmt.close()
//modified by Lear
            int stmtResult=stmt.executeUpdate();
            stmt.close();
            if ( stmtResult!=1)
            {
                con.rollback();
                throw new qdbException("Failed to delete Course. No such record.");
            }
            // kim:todo
//            Vector ccrIds = DbCourseCriteria.getCcrIdByCosId(con, cos_res_id);
//            DbCourseCriteria ccr = new DbCourseCriteria();
//            for (int i=0; i<ccrIds.size(); i++){
//                ccr.ccr_id = ((Long)ccrIds.elementAt(i)).longValue();
//                ccr.del(con);
//            }

            // delete CourseCriteria
            /*
            DbCourseCriteria ccr = new DbCourseCriteria();
            ccr.ccr_id = cos_ccr_id;
            if (ccr.ccr_id != 0){
                ccr.del(con);
            }
            */
            // calls dbResource.del()
            super.del(con);
            // if ok.
            // commit() at qdbAction.java
            return;

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    public Vector pastePublicSurvey(Connection con, long public_mod_id, String uploadDir, loginProfile prof, Vector vtClsId) throws cwException, cwSysMessage{
        try{
            if(!dbModule.checkModRootIdUniqueInCos(con, cos_res_id, public_mod_id)){
                throw new cwSysMessage("MOD006");
            }
            Vector vtModId = dbModule.dumpModNQ(con, public_mod_id, null, uploadDir, prof, vtClsId, true);
            dbResourceContent resCon = new dbResourceContent();

            resCon.rcn_res_id = cos_res_id;
            resCon.rcn_res_id_content = ((Long)vtModId.get(0)).longValue();
            resCon.rcn_obj_id_content = 0;

            if (!resCon.ins(con)) {
                    con.rollback();
                    throw new cwException("Failed to add a Course.");
            }
            else
            {
                super.updateTimeStamp(con);
                for (int i = 0; i < vtClsId.size(); i++) {
                    resCon.rcn_res_id = ((dbCourse) vtClsId.get(i)).cos_res_id;
                    resCon.rcn_res_id_content = ((Long) vtModId.get(i + 1)).longValue();
                    resCon.rcn_obj_id_content = 0;
                    if (!resCon.ins(con)) {
                        con.rollback();
                        throw new cwException("Failed to add a Course.");
                    }
                }
                return vtModId;
            }
        }catch(qdbException e){
            throw new cwException(e.getMessage());
        }catch(SQLException e){
            throw new cwException(e.getMessage());
        }
    }




    // Add a test and insert it into a course
    public long insModule(Connection con, dbModule dbmod, String domain, loginProfile prof)
        throws qdbException
    {
        try {
            // check User Right
            //if (!dbResourcePermission.hasPermission(con, cos_res_id, prof,
            //                                 dbResourcePermission.RIGHT_WRITE)) {
            //    throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
            //}

            res_upd_user  = prof.usr_id;

            dbmod.res_upd_user = prof.usr_id;
            dbmod.res_usr_id_owner = prof.usr_id;
            // calls dbModule.ins()
            dbmod.ins(con, prof);

            // if ok.

            dbResourceContent resCon = new dbResourceContent();


            // Lun : Check the effect start datetime, if module start datetime < course start datetime
            // set module start datetime = course start datetime
            /*
            Timestamp cos_start_time;
            PreparedStatement stmt = con.prepareStatement("SELECT cos_eff_start_datetime START WHERE cos_res_id = ? ");
            stmt.setLong(1, cos_res_id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next())
                cos_start_time = rs.getTimestamp("START");

            if((dbmod.mod_eff_start_datetime).before(cos_start_time))
                dbmod.mod_eff_start_datetime = cos_start_time;
            */

            resCon.rcn_res_id = cos_res_id;
            resCon.rcn_res_id_content = dbmod.mod_res_id;
            resCon.rcn_obj_id_content = 0;

            if (!resCon.ins(con)) {
                con.rollback();
                throw new qdbException("Failed to add a Course.");
            }
            else
            {
                super.updateTimeStamp(con);
                if (domain != null && domain.length() > 0) {
                    if(domain.equalsIgnoreCase(dbUtils.DOMAIN_PUBLIC))
                        dbResourcePermission.save(con,dbmod.mod_res_id,0,null,true,false,true);
                    else if (domain.equalsIgnoreCase(dbUtils.DOMAIN_SCHOOL))
                        dbResourcePermission.save(con,dbmod.mod_res_id,prof.root_ent_id,null,true,false,true);
                }
                //con.commit();                 //  con.commit() at qdbAction.java
                return dbmod.mod_res_id;
            }
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }


    // Add an assignment and insert it into a course
    public long insEvent(Connection con, dbEvent dbevt, String domain, loginProfile prof)
        throws qdbException, qdbErrMessage
    {
        try {
            // check User Right
            //if (!dbResourcePermission.hasPermission(con, cos_res_id, prof,
            //                                dbResourcePermission.RIGHT_WRITE)) {
            //    throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
            //}

            res_upd_user  = prof.usr_id;

            dbevt.res_upd_user = prof.usr_id;
            dbevt.res_usr_id_owner = prof.usr_id;
            // calls dbAssignment.ins()
            dbevt.ins(con, prof);
            // if ok.

            dbResourceContent resCon = new dbResourceContent();

            resCon.rcn_res_id = cos_res_id;
            resCon.rcn_res_id_content = dbevt.evt_res_id;
            resCon.rcn_obj_id_content = 0;
            if (!resCon.ins(con)) {
                con.rollback();
                throw new qdbException("Failed to add a Course.");
            }
            else
            {
                super.updateTimeStamp(con);
                if (domain != null && domain.length() > 0) {

                    if(domain.equalsIgnoreCase(dbUtils.DOMAIN_PUBLIC))
                        dbResourcePermission.save(con,dbevt.evt_res_id,0,null,true,false,true);
                    else if (domain.equalsIgnoreCase(dbUtils.DOMAIN_SCHOOL))
                        dbResourcePermission.save(con,dbevt.evt_res_id,prof.root_ent_id,null,true,false,true);
                }
                //con.commit();                 //  con.commit() at qdbAction.java
                return dbevt.evt_res_id;
            }
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }


    // Add an assignment and insert it into a course
    public long insAssignment(Connection con, dbAssignment dbass, String domain, loginProfile prof,
                              String[] files_desc_lst, String[] files)
        throws qdbException, qdbErrMessage
    {
        try {
            // check User Right
            //if (!dbResourcePermission.hasPermission(con, cos_res_id, prof,
            //                                dbResourcePermission.RIGHT_WRITE)) {
            //    throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
            //}

            res_upd_user  = prof.usr_id;

            dbass.res_upd_user = prof.usr_id;
            dbass.res_usr_id_owner = prof.usr_id;
            // calls dbAssignment.ins()
            dbass.ins(con, prof, files_desc_lst, files);
            // if ok.

            dbResourceContent resCon = new dbResourceContent();

            resCon.rcn_res_id = cos_res_id;
            resCon.rcn_res_id_content = dbass.ass_res_id;
            resCon.rcn_obj_id_content = 0;
            if (!resCon.ins(con)) {
                con.rollback();
                throw new qdbException("Failed to add a Course.");
            }
            else
            {
                super.updateTimeStamp(con);
                if (domain != null && domain.length() > 0) {

                    if(domain.equalsIgnoreCase(dbUtils.DOMAIN_PUBLIC))
                        dbResourcePermission.save(con,dbass.ass_res_id,0,null,true,false,true);
                    else if (domain.equalsIgnoreCase(dbUtils.DOMAIN_SCHOOL))
                        dbResourcePermission.save(con,dbass.ass_res_id,prof.root_ent_id,null,true,false,true);
                }
                //con.commit();                 //  con.commit() at qdbAction.java
                return dbass.ass_res_id;
            }
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }


public long insChat(Connection con, dbChat dbchat, String domain, loginProfile prof)
        throws qdbException, qdbErrMessage
    {
        try {
            // check User Right
            //if (!dbResourcePermission.hasPermission(con, cos_res_id, prof,
            //                                dbResourcePermission.RIGHT_WRITE)) {
            //    throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
            //}

            res_upd_user  = prof.usr_id;
            dbchat.res_upd_user = prof.usr_id;
            dbchat.res_usr_id_owner = prof.usr_id;

            dbchat.ins(con, prof);
            // if ok.
            //System.out.println("Assign Value OK");
            dbResourceContent resCon = new dbResourceContent();

            resCon.rcn_res_id = cos_res_id;
            resCon.rcn_res_id_content = dbchat.mod_res_id;
            resCon.rcn_obj_id_content = 0;
            if (!resCon.ins(con)) {
                //con.rollback();
                throw new qdbException("Failed to add a Chatroom.");
            }
            else
            {   //System.out.println("Update Time");
                super.updateTimeStamp(con);
                if (domain != null && domain.length() > 0) {
                    //System.out.println("Check Permissiom");
                    if(domain.equalsIgnoreCase(dbUtils.DOMAIN_PUBLIC))
                        dbResourcePermission.save(con,dbchat.mod_res_id,0,null,true,false,true);
                    else if (domain.equalsIgnoreCase(dbUtils.DOMAIN_SCHOOL))
                        dbResourcePermission.save(con,dbchat.mod_res_id,prof.root_ent_id,null,true,false,true);
                }
                //con.commit();                 //  con.commit() at qdbAction.java
                return dbchat.mod_res_id;
            }
        } catch(Exception e) {
            throw new qdbException("Insert Chat To Course Error: " + e.getMessage());
        }
    }

    private static final String sql_get_lic_cnt =
        " select count(distinct(enr_ent_id)) from enrolment, course " +
        " where cos_res_id = enr_res_id and cos_lic_key = ? ";
    //check course licence againist enrolment count
    private boolean isLicenceAvailable(Connection con) throws SQLException {
        boolean result;
        if(this.cos_lic_key == null) {
            result = true;
        }
        else {
            PreparedStatement stmt = con.prepareStatement(sql_get_lic_cnt);
            stmt.setString(1, this.cos_lic_key);
            ResultSet rs = stmt.executeQuery();
            long cnt = (rs.next()) ? rs.getLong(1) : 0;
            if(cnt >= this.lic_quota) result = false;
            else result = true;
            rs.close();
            stmt.close();
        }
        return result;
    }

    private static final String sql_is_usr_has_lic =
        " select enr_ent_id from enrolment, course " +
        " where cos_res_id = enr_res_id and cos_lic_key = ? and enr_ent_id = ? ";
    private boolean isUserHasLicence(Connection con, long entId) throws SQLException {
        boolean result;
        if(this.cos_lic_key == null) {
            result = true;
        }
        else {
            PreparedStatement stmt = con.prepareStatement(sql_is_usr_has_lic);
            stmt.setString(1, this.cos_lic_key);
            stmt.setLong(2, entId);
            ResultSet rs = stmt.executeQuery();
            result = rs.next();
            rs.close();
            stmt.close();
        }
        return result;
    }

    private static final String sql_get_lic =
        " select lic_key, lic_quota from ctLicence, course " +
        " where cos_lic_key = lic_key and lic_ent_id_root = ? and cos_res_id = ? ";
    //pre-defined variable: cos_res_id
    private void getLicenceKeyNQuota(Connection con, long ent_id_root) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(sql_get_lic);
        stmt.setLong(1, ent_id_root);
        stmt.setLong(2, this.cos_res_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            this.cos_lic_key = rs.getString("lic_key");
            this.lic_quota = rs.getLong("lic_quota");
        }
        else {
            this.cos_lic_key = null;
            this.lic_quota = 0;
        }
        rs.close();
        stmt.close();
        return;
    }

    // enroll an entity
    public void enroll(Connection con, long entId, loginProfile prof)
        throws qdbException, cwSysMessage, SQLException
    {
        // check User Right
        //if (!dbResourcePermission.hasPermission(con, cos_res_id, prof,
        //                                dbResourcePermission.RIGHT_WRITE)) {
        //    throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
        //}
        getLicenceKeyNQuota(con, prof.root_ent_id);
        if(!isUserHasLicence(con, entId) && !isLicenceAvailable(con)) {
            throw new cwSysMessage("ENR002");
        }

        dbEnrolment.enrol(con, entId, cos_res_id, prof);
        //SnsDoingService snsDoingService = (SnsDoingService)WzbApplicationContext.getBean("snsDoingService");

		//snsDoingService.add(cos_itm_id, 0, entId, 0, SNS.DOING_ACTION_ENROLL_COS, 0, SNS.MODULE_COURSE, "", 0);

        // commit() at qdbAction.java
    }


    public void unenroll(Connection con, long entId, long root_ent_id)
        throws SQLException
    {
        try {
            if (tkh_id==DbTrackingHistory.TKH_ID_UNDEFINED){
            	CommonLog.info("!!!!!!get tracking id in dbCourse.unenroll");
                tkh_id = DbTrackingHistory.getAppTrackingIDByCos(con, cos_res_id, entId);
            }
            
            //在删除报名时，同时删除详细学习记录
            itemCompleteDelete.delCompletedRecordByTkh_id( con, tkh_id);
        }catch (cwException e) {
            throw new SQLException (e.getMessage());
        }
        // check User Right
        //if (!dbResourcePermission.hasPermission(con, cos_res_id, prof,
        //                                dbResourcePermission.RIGHT_WRITE)) {
        //    throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
        //}
        //Want to keep the enrolment if user has attempted one of the modules
        if(dbModuleEvaluation.getCountByUsrNCos(con, entId, cos_res_id, tkh_id) == 0) {
            PreparedStatement stmt = con.prepareStatement(
                "DELETE FROM Enrolment "
            + " WHERE enr_res_id = ? "
            + "   AND enr_ent_id = ? "  );

            stmt.setLong(1, cos_res_id);
            stmt.setLong(2, entId);
            stmt.executeUpdate();
            stmt.close();

        }

        try {
            long lrnrSteEntId = dbRegUser.getSiteEntId(con, entId);
            // If no more application record for that course
            if (DbTrackingHistory.getAttemptCntFromCos(con, cos_res_id, DbTrackingHistory.TKH_TYPE_APPLICATION) == 0) {
                String[] lrn_role_lst = {AccessControlWZB.ROL_EXT_ID_NLRN};
                for(int i=0; i<lrn_role_lst.length; i++) {
                    String lrn_role = lrn_role_lst[i];
                    dbResourcePermission.del(con,cos_res_id,entId,lrn_role);
                }
            }
        }
        catch (qdbException e) {
            throw new SQLException (e.getMessage());
        }
    }

    public String enrolmentAsXML(Connection con, loginProfile prof)
        throws qdbException
    {
        String result = "<?xml version=\"1.0\" encoding=\"" + dbUtils.ENC_UTF + "\" standalone=\"no\" ?>" + dbUtils.NEWL;
        result += "<enrolment_list cos_id=\"" + cos_res_id + "\">" + dbUtils.NEWL;
        result += prof.asXML() + dbUtils.NEWL;

        String SQL =
              " SELECT enr_ent_id EID, ent_type ETYPE, NULL U_ID, NULL U_SID, usg_display_bil UDISP "
          +   "  FROM Enrolment, UserGroup, Entity "
          +   " WHERE enr_res_id = " + cos_res_id
          +   "   AND enr_ent_id = usg_ent_id "
          +   "   AND ent_id     = enr_ent_id "
          +   "   AND ent_delete_usr_id IS NULL "
          +   "   AND ent_delete_timestamp IS NULL "
          +   "UNION "
          +   " SELECT enr_ent_id EID, ent_type ETYPE, usr_id U_ID, usr_ste_usr_id U_SID, usr_display_bil UDISP "
          +   "   FROM Enrolment, RegUser, Entity "
          +   "  WHERE enr_res_id = " + cos_res_id
          +   "    AND enr_ent_id = usr_ent_id "
          +   "    AND ent_id     = enr_ent_id "
          +   "    AND ent_delete_usr_id IS NULL "
          +   "    AND ent_delete_timestamp IS NULL "
          // cl : order the enrolment by the usr/usg display name
          +   " order by UDISP ";

        try {
          PreparedStatement stmt = con.prepareStatement(SQL);
          ResultSet rs = stmt.executeQuery();
          String ETYPE, UDISP, UID, USID;
          long EID;
          String xmlBody = "";
          while(rs.next())
          {
             EID = rs.getLong("EID");
             ETYPE = rs.getString("ETYPE");
             UID = rs.getString("U_ID");
             USID = rs.getString("U_SID");
             UDISP = rs.getString("UDISP");

             xmlBody += "<entity id=\"" + EID + "\" type=\"" + ETYPE + "\" ";
             if(USID!=null)
                xmlBody += "usr_id=\"" + dbUtils.esc4XML(USID) + "\" ";
             xmlBody += ">" + UDISP + "</entity>" + dbUtils.NEWL;
          }

          result += xmlBody;
          result += "</enrolment_list>";

          stmt.close();
          return result;
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    // From 3.1 onward, only user can enrol to course
    // usergroup enrolment is not accepted.
    public String getRptCosXML(Connection con , HttpSession sess, long modId, long groupId, int cur_page, int pagesize)
        throws qdbException
    {
        Hashtable data = null;
        boolean useSess = false;
        if (sess !=null) {
            data = (Hashtable) sess.getAttribute(SESS_LRN_COS_RPT);
            if (data !=null) {
                long groupID = ((Long) data.get(HASH_GROUP_ID)).longValue();
                long cosID = ((Long) data.get(HASH_COS_ID)).longValue();
                if (cosID==cos_res_id && groupID == groupId) {
                    useSess = true;
                }
            }
        }

        int start = (cur_page-1) * pagesize + 1;
        int end  = cur_page * pagesize;

        String SQL = " SELECT usr_ent_id MEID, "
            + "       usr_id MUID, usr_ste_usr_id MUSID, usr_display_bil MUDISP, ent_upd_date MDATE"
            + "  FROM RegUser , Entity " ;

        Vector sessIdVec = new Vector();
        if (useSess) {
            sessIdVec = (Vector)data.get(HASH_ENT_ID_VEC);
            String ent_id_lst = new String();
            for (int i=start ; i<= sessIdVec.size() && (i <= end);i++) {
                if (i!=start) {
                    ent_id_lst += ",";
                }
                ent_id_lst += ((Long) sessIdVec.elementAt(i-1)).longValue();
            }

            if (ent_id_lst.length()==0)
                ent_id_lst = "-1";

            SQL +=  " WHERE ent_id = usr_ent_id "
            + " AND ent_delete_usr_id IS NULL "
            + " AND ent_delete_timestamp IS NULL "
            + " AND usr_ent_id IN (" + ent_id_lst + ")";

        }else {

            SQL += ", EntityRelation, Enrolment "
            + " WHERE ern_child_ent_id = usr_ent_id "
            + " AND ern_parent_ind = ? "
            + "   AND ent_id = usr_ent_id "
            + " AND ent_delete_usr_id IS NULL "
            + " AND ent_delete_timestamp IS NULL "
            +   "  AND enr_res_id = " + cos_res_id
            +   "    AND enr_ent_id = usr_ent_id ";

            if (groupId > 0) {
                SQL += " AND ern_ancestor_ent_id = " + groupId;
            }
        }

        SQL += " ORDER BY MUDISP, MUSID ";

        try {
            String MUDISP, MUID, MUSID;
            long MEID =  0;
            Vector entIdVec = new Vector();
            Vector usrIdVec = new Vector();

            StringBuffer xmlBody = new StringBuffer(1024);
            int index = 1;
            PreparedStatement stmt = con.prepareStatement(SQL);
            if (!useSess) {
            	stmt.setBoolean(index++, true);
            }
            ResultSet rs = stmt.executeQuery();

            int cnt = 0;
            while(rs.next())
            {
                cnt ++;
                MEID = rs.getLong("MEID");
                if (useSess || (cnt >=start && cnt <= end)) {
                    MUID = rs.getString("MUID");
                    MUSID = rs.getString("MUSID");
                    MUDISP = rs.getString("MUDISP");

                    xmlBody.append("<entity id=\"").append(MEID).append("\" type=\"").append(dbEntity.ENT_TYPE_USER).append("\" ");
                    xmlBody.append("usr_id=\"").append(dbUtils.esc4XML(MUSID)).append("\" ");
                    xmlBody.append(">").append(MUDISP).append("</entity>").append(dbUtils.NEWL);
                    usrIdVec.addElement(MUID);
                }

                entIdVec.addElement(new Long(MEID));
            }

            stmt.close();

            int count = 0;
            if (useSess) {
                count = sessIdVec.size();
            }else {
                count = entIdVec.size();
                Hashtable curData = new Hashtable();
                curData.put(HASH_COS_ID, new Long(cos_res_id));
                curData.put(HASH_GROUP_ID, new Long(groupId));
                curData.put(HASH_ENT_ID_VEC, entIdVec);
                sess.setAttribute(SESS_LRN_COS_RPT, curData);
            }

            if (usrIdVec.size() > 0)  {
                xmlBody.append(dbProgress.getResultGrpXML(con,modId,usrIdVec));
            }

            StringBuffer xml = new StringBuffer();
            xml.append("<group id=\"").append(groupId).append("\" count=\"")
               .append(count).append("\"/>").append(dbUtils.NEWL);
            xml.append(xmlBody.toString());
            return xml.toString();

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    public String learnerEnrolAsXML(Connection con , loginProfile prof, HttpSession sess, int cur_page, int pagesize, String order_by, String sort_by)
        throws qdbException, cwSysMessage{
        return learnerEnrolAsXML(con , prof, sess, cur_page, pagesize, order_by, sort_by, null);
    }
    public String learnerEnrolAsXML(Connection con , loginProfile prof, HttpSession sess, int cur_page, int pagesize, String order_by, String sort_by, String extXML)
        throws qdbException, cwSysMessage
    {
        return learnerEnrolAsXML(con , prof, sess, cur_page, pagesize, order_by, sort_by, extXML, null);
    }
    public String learnerEnrolAsXML(Connection con , loginProfile prof, HttpSession sess, int cur_page, int pagesize, String order_by, String sort_by, String extXML, WizbiniLoader wizbini)
        throws qdbException, cwSysMessage
    {
        try {
            get(con);

            if (order_by == null) {
                order_by = "usr_display_bil";
            }

            Hashtable data = null;
            boolean useSess = false;
            if (sess !=null) {
                data = (Hashtable) sess.getAttribute(SESS_COS_LRN_LST);
                if (data !=null) {
                    Long cosID_ = (Long) data.get(HASH_COS_ID);
                    String orderBy_ = (String) data.get(HASH_ORDER_BY);
                    String sortBy_ = (String) data.get(HASH_SORT_BY);
                    if (cosID_ != null && orderBy_ != null && sortBy_ != null) {
                        if (cosID_.longValue() == cos_res_id
                            && orderBy_.equalsIgnoreCase(order_by)
                            && sortBy_.equalsIgnoreCase(sort_by)) {
                            useSess = true;
                        }
                    }
                }
            }

            int start = (cur_page-1) * pagesize + 1;
            int end  = cur_page * pagesize;

            StringBuffer SQL = new StringBuffer();
            SQL.append(" SELECT usr_id, usr_ent_id, usr_ste_usr_id, usr_email, ")
	            .append(" usr_first_name_bil, usr_last_name_bil, usr_display_bil, usr_tel_1,")
	            .append(" ern_ancestor_ent_id ern_usg_id, erh_ancestor_ent_id erh_usg_id, ")
	            .append(" ste_name ")
	            .append(" FROM RegUser ")
	            .append(" inner join Entity on (ent_id = usr_ent_id and ent_delete_usr_id IS NULL AND ent_delete_timestamp IS NULL)")
	            .append(" inner join acSite on (ste_ent_id = usr_ste_ent_id) ")
	            .append(" left join EntityRelation on (ern_child_ent_id = usr_ent_id and ern_type = ? and ern_parent_ind = ?) ")
	            .append(" left join EntityRelationHistory on (erh_child_ent_id = usr_ent_id and erh_type = ? and erh_parent_ind = ? and erh_end_timestamp = ent_delete_timestamp)");
            Vector sessIdVec = new Vector();
            if (useSess) {
                sessIdVec = (Vector)data.get(HASH_ENT_ID_VEC);
                String ent_id_lst = new String();
                for (int i=start ; i<= sessIdVec.size() && (i <= end);i++) {
                    if (i!=start) {
                        ent_id_lst += ",";
                    }
                    ent_id_lst += ((Long) sessIdVec.elementAt(i-1)).longValue();
                }

                if (ent_id_lst.length()==0)
                    ent_id_lst = "-1";

                SQL.append(" where usr_ent_id IN (").append(ent_id_lst).append(")");

            }else {
                SQL.append(" inner join Enrolment on (enr_ent_id = usr_ent_id and enr_res_id = ")
	                .append(cos_res_id)
	                .append(")");
            }

            SQL.append("  ORDER BY ").append(order_by).append(" ").append(sort_by);

            if (!order_by.equalsIgnoreCase("usr_display_bil")){
                SQL.append(" , usr_display_bil ").append(sort_by);
            }

            long MEID =  0;
            Vector entIdVec = new Vector();

            StringBuffer xmlBody = new StringBuffer(1024);
            int index = 1;
            PreparedStatement stmt = con.prepareStatement(SQL.toString());
            stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
            stmt.setBoolean(index++, true);
            stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
            stmt.setBoolean(index++, true);
            ResultSet rs = stmt.executeQuery();
            EntityFullPath entityfullpath = EntityFullPath.getInstance(con);
            int cnt = 0;
            while(rs.next())
            {
                cnt ++;
                MEID = rs.getLong("usr_ent_id");
                if (useSess || (cnt >=start && cnt <= end)) {
                	long usg_id = rs.getLong("ern_usg_id");
                	if(usg_id == 0){
                		usg_id = rs.getLong("erh_usg_id");
                	}
                    String full_path = entityfullpath.getFullPath(con,usg_id);
                    if (wizbini!=null && wizbini.cfgSysSetupadv.getOrganization().isMultiple()){
                        full_path = rs.getString("ste_name") + wizbini.cfgSysSetupadv.getGroupMemberFullPathSeparator() + full_path;
                    }
                    xmlBody.append("<user id=\"").append(rs.getString("usr_id"))
                    .append("\" ent_id=\"").append(MEID)
                    .append("\" ste_usr_id=\"").append(rs.getString("usr_ste_usr_id"))
                    .append("\" biography=\"").append(PsnBiography.showBiography(con, MEID))
                    .append("\">").append(dbUtils.NEWL)
                    .append("<name first=\"").append(dbUtils.esc4XML(rs.getString("usr_first_name_bil")))
                    .append("\" last=\"").append(dbUtils.esc4XML(rs.getString("usr_last_name_bil")))
                    .append("\">").append(dbUtils.esc4XML(rs.getString("usr_display_bil")))
                    .append("</name>").append(dbUtils.NEWL)
                    .append("<email>").append(dbUtils.esc4XML(rs.getString("usr_email"))).append("</email>").append(dbUtils.NEWL)
                    .append("<tel tel_1=\"").append(dbUtils.esc4XML(rs.getString("usr_tel_1"))).append("\"/>")
                    .append("<full_path>").append(dbUtils.esc4XML(full_path)).append("</full_path>")
                    .append("</user>").append(dbUtils.NEWL);
                }
                entIdVec.addElement(new Long(MEID));
            }

            stmt.close();

            int count = 0;
            if (useSess) {
                count = sessIdVec.size();
            }else {
                count = entIdVec.size();
                Hashtable curData = new Hashtable();
                curData.put(HASH_COS_ID, new Long(cos_res_id));
                curData.put(HASH_ORDER_BY, new String(order_by));
                curData.put(HASH_SORT_BY, new String(sort_by));
                curData.put(HASH_ENT_ID_VEC, entIdVec);
                sess.setAttribute(SESS_COS_LRN_LST, curData);
            }

            StringBuffer xml = new StringBuffer();
            xml.append(dbUtils.xmlHeader)
               .append("<learner_list>").append(dbUtils.NEWL)
               .append(prof.asXML()).append(dbUtils.NEWL)
               .append(extXML).append(dbUtils.NEWL)
               .append("<course id=\"").append(cos_res_id)
               .append("\" cur_page=\"").append(cur_page).append("\" pagesize=\"")
               .append(pagesize).append("\" total=\"").append(count).append("\" order_by=\"")
               .append(order_by).append("\" sort_by=\"").append(sort_by).append("\">")
               .append(dbUtils.esc4XML(res_title)).append("</course>").append(dbUtils.NEWL);

            xml.append(xmlBody.toString());
            xml.append("</learner_list>").append(dbUtils.NEWL);

            return xml.toString();

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    public String tstEnrolAsXML(Connection con, loginProfile prof, HttpSession sess, long modId, long entId, int cur_page, int pagesize)
        throws qdbException, cwSysMessage
    {
        get(con);

        String result = "<?xml version=\"1.0\" encoding=\"" + dbUtils.ENC_UTF + "\" standalone=\"no\" ?>" + dbUtils.NEWL;
        result += "<enrolment_list cos_id=\"" + cos_res_id
                + "\" cur_page=\"" + cur_page + "\" pagesize=\"" + pagesize + "\">" + dbUtils.NEWL;
        result += prof.asXML() + dbUtils.NEWL;
        result += "<title>" + dbUtils.esc4XML(res_title) + "</title>" + dbUtils.NEWL;

        result += getRptCosXML(con, sess, modId, entId, cur_page, pagesize);

        result += "</enrolment_list>";

        return result;

    }

    public String assEnrolAsXML(Connection con, HttpSession sess, loginProfile prof, long modId, String queue, int page, Timestamp report_time)
        throws qdbException, qdbErrMessage, SQLException, cwSysMessage
    {
        return assEnrolAsXML(con, sess, prof, modId, queue, page, PAGE_SIZE, report_time, null);
    }

    public String assEnrolAsXML(Connection con, HttpSession sess, loginProfile prof, long modId, String queue, int page, int page_size, Timestamp report_time, String metaXML)
        throws qdbException, qdbErrMessage, SQLException, cwSysMessage
    {
        return assEnrolAsXML(con, sess, prof, modId, queue, page, PAGE_SIZE, report_time, metaXML, null);
    }

    public String assEnrolAsXML(Connection con, HttpSession sess, loginProfile prof, long modId, String queue, int page, int page_size, Timestamp report_time, String metaXML, WizbiniLoader wizbini)
        throws qdbException, qdbErrMessage, SQLException, cwSysMessage
    {
        if (page_size < 1) {
            page_size = PAGE_SIZE;
        }

        get(con);

        StringBuffer result = new StringBuffer();
        result.append("<?xml version=\"1.0\" encoding=\"").append(dbUtils.ENC_UTF).append("\" standalone=\"no\" ?>").append(dbUtils.NEWL);
        result.append("<enrolment_list cos_id=\"").append(cos_res_id).append("\" mod_id=\"").append(modId).append("\">").append(dbUtils.NEWL);
        result.append(prof.asXML()).append(dbUtils.NEWL);
        if (metaXML !=null){
            result.append(metaXML).append(dbUtils.NEWL);
        }
        result.append("<title>").append(dbUtils.esc4XML(res_title)).append("</title>").append(dbUtils.NEWL);

        dbAssignment dbass = new dbAssignment();
        dbass.ass_res_id = modId;
        dbass.get(con);

		Vector validAppTkhIds = ViewModuleEvaluation.getValidAppTkhIds(con, modId);

        Hashtable sess_h_queue = (Hashtable)sess.getAttribute(SESS_ASS_QUEUE_HASH);
        Timestamp sess_ass_time = (Timestamp)sess.getAttribute(SESS_ASS_TIMESTAMP);
        Hashtable sess_h_stat = (Hashtable)sess.getAttribute(SESS_ASS_QUEUE_STAT);
        Vector sess_graded = null;
        Vector sess_not_graded = null;
        Vector sess_not_submitted = null;
        Vector sess_all = null;
        PreparedStatement stmt = null;

        if (sess_h_queue != null && sess_h_stat != null && sess_ass_time.equals(report_time)) {
            sess_graded = (Vector)sess_h_queue.get(GRADED);
            sess_not_graded = (Vector)sess_h_queue.get(NOT_GRADED);
            sess_not_submitted = (Vector)sess_h_queue.get(NOT_SUBMITTED);
            sess_all = (Vector)sess_h_queue.get(ALL);
        } else {

            // wizBank 3.1 onward, only user can enrol to a course
            // we can get the enrolment list directly from enrolment
            // wizBank 3.5 , to handle course retake, different tracking records for course retake
            // Remarks : Assignment is not allowed for quick reference
            int idx = 1;
            stmt = con.prepareStatement(OuterJoinSqlStatements.getAssEnrolSQL());
            stmt.setLong(idx++, modId);
            stmt.setLong(idx++, 1);
            stmt.setLong(idx++, cos_res_id);
            stmt.setString(idx++, DbTrackingHistory.TKH_TYPE_APPLICATION);
            ResultSet rs = stmt.executeQuery();

            sess_graded = new Vector();
            sess_not_graded = new Vector();
            sess_not_submitted = new Vector();
            sess_all = new Vector();
            int i_graded = 0;
            int i_not_graded = 0;
            int i_not_submitted = 0;
            //作业提交情况页面统计
            while (rs.next()) {
            	long tkhId = rs.getLong("tkh_id");
                //过滤掉回收站用户
            	String userStatusSql = "select usr_status from RegUser,TrackingHistory where usr_ent_id = tkh_usr_ent_id and tkh_id = ?";
                PreparedStatement stmt_user = con.prepareStatement(userStatusSql);
                stmt_user.setLong(1,tkhId);
                ResultSet rs_user = stmt_user.executeQuery();
                String usr_status = null;
                while (rs_user.next()){
                    usr_status = rs_user.getString("usr_status");
                }
                rs_user.close();
                stmt_user.close();

            	if(validAppTkhIds.contains(new Long(tkhId)) && usr_status.equals("OK")) {
					String status = rs.getString("pgr_status");

					if (status == null) {
						sess_not_submitted.addElement(rs.getString("tkh_id"));
						i_not_submitted++;
					} else if (status.equals(dbProgress.PGR_STATUS_NOT_GRADED)) {
						sess_not_graded.addElement(rs.getString("tkh_id"));
						i_not_graded++;
					} else if (status.equals(dbProgress.PGR_STATUS_GRADED)) {
						sess_graded.addElement(rs.getString("tkh_id"));
						i_graded++;
					}

					sess_all.addElement(rs.getString("tkh_id"));
            	}
            }
            stmt.close();

            sess_h_queue = new Hashtable();
            sess_h_queue.put(GRADED, sess_graded);
            sess_h_queue.put(NOT_GRADED, sess_not_graded);
            sess_h_queue.put(NOT_SUBMITTED, sess_not_submitted);
            sess_h_queue.put(ALL, sess_all);
            sess_ass_time = dbUtils.getTime(con);

            sess_h_stat = new Hashtable();
            sess_h_stat.put(GRADED, new Integer(i_graded));
            sess_h_stat.put(NOT_GRADED, new Integer(i_not_graded));
            sess_h_stat.put(NOT_SUBMITTED, new Integer(i_not_submitted));
            sess_h_stat.put(ALL, new Integer(i_graded + i_not_graded + i_not_submitted));

            sess.setAttribute(SESS_ASS_TIMESTAMP, sess_ass_time);
            sess.setAttribute(SESS_ASS_QUEUE_HASH, sess_h_queue);
            sess.setAttribute(SESS_ASS_QUEUE_STAT, sess_h_stat);
        }

        result.append(dbass.getAssHeader(con, prof.usr_id, prof.root_ent_id));
        result.append("<stat time=\"").append(sess_ass_time).append("\" page_size=\"").append(page_size).append("\" cur_page=\"").append(page).append("\">");
        result.append("<queue name=\"all\" count=\"").append(sess_h_stat.get(ALL)).append("\" />");
        result.append("<queue name=\"graded\" count=\"").append(sess_h_stat.get(GRADED)).append("\" />");
        result.append("<queue name=\"not_graded\" count=\"").append(sess_h_stat.get(NOT_GRADED)).append("\" />");
        result.append("<queue name=\"not_submitted\" count=\"").append(sess_h_stat.get(NOT_SUBMITTED)).append("\" />");
        result.append("</stat>").append(dbUtils.NEWL);

        int count = 1;
        StringBuffer set = new StringBuffer();
        Vector ids;

        if (page == 0) {
            page = 1;
        }

        if (queue.equals(NOT_SUBMITTED)) {
            ids = sess_not_submitted;
        } else if (queue.equals(NOT_GRADED)) {
            ids = sess_not_graded;
        } else if (queue.equals(GRADED)) {
            ids = sess_graded;
        } else {
            ids = sess_all;
        }

        set.append("(0");

        if (ids == null) {
            ids = new Vector();
        }

        for (int i = (page-1)*page_size; i < page*page_size && i < ids.size(); i++) {
            set.append(", ").append(ids.elementAt(i));
        }

        set.append(")");

        Timestamp curTime = cwSQL.getTime(con);
        if (queue.equals(NOT_SUBMITTED)) {
            stmt = con.prepareStatement("SELECT tkh_id, usr_ste_usr_id, usr_ent_id, usr_last_name_bil, usr_first_name_bil, usr_display_bil, usg_display_bil, " + cwSQL.get_null_sql(cwSQL.COL_TYPE_STRING) + " AS pgr_status, " + cwSQL.get_null_sql(cwSQL.COL_TYPE_INTEGER) + " AS pgr_score, " + cwSQL.get_null_sql(cwSQL.COL_TYPE_INTEGER) + " AS pgr_max_score, ste_name, ern_ancestor_ent_id FROM EntityRelation, RegUser, UserGroup, TrackingHistory, acSite WHERE usg_ent_id = ern_ancestor_ent_id AND usr_ent_id = ern_child_ent_id  AND ern_parent_ind = ? AND tkh_usr_ent_id = usr_ent_id AND ste_ent_id = usr_ste_ent_id AND tkh_id IN " + set + " ORDER BY usr_display_bil, ste_name");
            stmt.setBoolean(1, true);
        } else if (queue.equals(NOT_GRADED)) {
            stmt = con.prepareStatement("SELECT tkh_id, usr_ste_usr_id, usr_ent_id, usr_last_name_bil, usr_first_name_bil, usr_display_bil, usg_display_bil, pgr_score, pgr_grade, pgr_max_score, pgr_rank_bil, pgr_schedule_datetime, pgr_start_datetime, pgr_complete_datetime, pgr_last_acc_datetime, pgr_status, pgr_attempt_nbr, ste_name, ern_ancestor_ent_id FROM EntityRelation, RegUser, UserGroup, Progress, TrackingHistory, acSite WHERE usg_ent_id = ern_ancestor_ent_id  AND usr_ent_id = ern_child_ent_id AND ern_parent_ind = ? AND tkh_id = pgr_tkh_id AND tkh_usr_ent_id = usr_ent_id AND tkh_id IN " + set + " AND pgr_usr_id = usr_id AND pgr_res_id = ? AND pgr_attempt_nbr = ? AND ste_ent_id = usr_ste_ent_id ORDER BY usr_display_bil, ste_name ");
            stmt.setBoolean(1, true);
            stmt.setLong(2, modId);
            stmt.setInt(3, 1);
        } else if (queue.equals(GRADED)) {
            stmt = con.prepareStatement("SELECT tkh_id, usr_ste_usr_id, usr_ent_id, usr_last_name_bil, usr_first_name_bil, usr_display_bil, usg_display_bil, pgr_score, pgr_grade, pgr_max_score, pgr_rank_bil, pgr_schedule_datetime, pgr_start_datetime, pgr_complete_datetime, pgr_last_acc_datetime, pgr_status, pgr_attempt_nbr, ste_name, ern_ancestor_ent_id FROM EntityRelation, RegUser, UserGroup, Progress, TrackingHistory, acSite WHERE usg_ent_id = ern_ancestor_ent_id  AND usr_ent_id = ern_child_ent_id AND ern_parent_ind = ? AND tkh_id = pgr_tkh_id AND tkh_usr_ent_id = usr_ent_id AND tkh_id IN " + set + " AND pgr_usr_id = usr_id AND pgr_res_id = ? AND pgr_attempt_nbr = ? AND ste_ent_id = usr_ste_ent_id ORDER BY usr_display_bil, ste_name ");
            stmt.setBoolean(1, true);
            stmt.setLong(2, modId);
            stmt.setInt(3, 1);
        } else {
            String sql = "SELECT"
                       + " tkh_id, usr_ste_usr_id, usr_ent_id, usr_last_name_bil, usr_first_name_bil, usr_display_bil, usg_display_bil,"
                       + " pgr_score, pgr_grade, pgr_max_score, pgr_rank_bil, pgr_schedule_datetime, pgr_start_datetime,"
                       + " pgr_complete_datetime, pgr_last_acc_datetime, pgr_status, pgr_attempt_nbr, ste_name, ern_ancestor_ent_id "
                       + "FROM"
                       + " EntityRelation, RegUser, UserGroup, Progress, TrackingHistory, acSite "
                       + "WHERE"
                       + " usg_ent_id = ern_ancestor_ent_id AND usr_ent_id = ern_child_ent_id AND"
                       + " ern_parent_ind = ? AND "
                       + " tkh_usr_ent_id = usr_ent_id AND tkh_id IN " + set + " AND pgr_usr_id = usr_id AND pgr_res_id = ? AND"
                       + " pgr_attempt_nbr = ? AND tkh_id = pgr_tkh_id AND ste_ent_id = usr_ste_ent_id "
                       + "UNION "
                       + "SELECT"
                       + " tkh_id, usr_ste_usr_id, usr_ent_id, usr_last_name_bil, usr_first_name_bil, usr_display_bil, usg_display_bil, "
                       + cwSQL.get_null_sql(cwSQL.COL_TYPE_DECIMAL) + " AS pgr_score, "
                       + cwSQL.get_null_sql(cwSQL.COL_TYPE_STRING) + " As pgr_grade, "
                       + cwSQL.get_null_sql(cwSQL.COL_TYPE_DECIMAL) + " AS pgr_max_score, "
                       + cwSQL.get_null_sql(cwSQL.COL_TYPE_STRING) + " AS pgr_rank_bil, "
                       + cwSQL.get_null_sql(cwSQL.COL_TYPE_TIMESTAMP) + " AS pgr_schedule_datetime, "
                       + cwSQL.get_null_sql(cwSQL.COL_TYPE_TIMESTAMP) + " AS pgr_start_datetime, "
                       + cwSQL.get_null_sql(cwSQL.COL_TYPE_TIMESTAMP) + " AS pgr_complete_datetime, "
                       + cwSQL.get_null_sql(cwSQL.COL_TYPE_TIMESTAMP) + " AS pgr_last_acc_datetime, "
                       + cwSQL.get_null_sql(cwSQL.COL_TYPE_STRING) + " AS pgr_status, "
                       + cwSQL.get_null_sql(cwSQL.COL_TYPE_INTEGER) + " AS pgr_attempt_nbr, "
                       + " ste_name, ern_ancestor_ent_id "
                       + "FROM"
                       + " EntityRelation, RegUser, UserGroup, TrackingHistory, acSite "
                       + "WHERE"
                       + " usg_ent_id = ern_ancestor_ent_id AND usr_ent_id = ern_child_ent_id AND"
                       + " ern_parent_ind = ? AND "
                       + " tkh_usr_ent_id = usr_ent_id AND ste_ent_id = usr_ste_ent_id AND "
                       + " tkh_id IN " + set + " AND tkh_id NOT IN"
                       + " (SELECT tkh_id FROM EntityRelation, RegUser, UserGroup, Progress, TrackingHistory "
                       + "  WHERE"
                       + "   usg_ent_id = ern_ancestor_ent_id AND usr_ent_id = ern_child_ent_id AND"
                       + "   ern_parent_ind = ? AND "
                       + "   tkh_usr_ent_id = usr_ent_id AND tkh_id IN " + set + " AND pgr_usr_id = usr_id AND pgr_res_id = ? AND"
                       + "   pgr_attempt_nbr = ? AND tkh_id = pgr_tkh_id) "
                       + "ORDER BY"
                       + " usr_display_bil, ste_name ";
            stmt = con.prepareStatement(sql);
            stmt.setBoolean(1, true);
            stmt.setLong(2, modId);
            stmt.setInt(3, 1);
            stmt.setBoolean(4, true);
            stmt.setBoolean(5, true);
            stmt.setLong(6, modId);
            stmt.setInt(7, 1);
        }

        ResultSet rs = stmt.executeQuery();
        Vector lst = new Vector();
        Hashtable record = new Hashtable();
        EntityFullPath entityfullpath = EntityFullPath.getInstance(con);
        while (rs.next()) {
        	long tkhId = rs.getLong("tkh_id");
			if(validAppTkhIds.contains(new Long(tkhId))) {
	            long tkh_id = rs.getLong("tkh_id");
	            long ancestor_id = rs.getLong("ern_ancestor_ent_id");
	            String fullpathStr = entityfullpath.getFullPath(con, ancestor_id);
	            String status = rs.getString("pgr_status");
	            String pgr_score = rs.getString("pgr_score");
	            String pgr_max_score = rs.getString("pgr_max_score");
	            StringBuffer entity = new StringBuffer();
	            StringBuffer progress = new StringBuffer();
	            Hashtable single = new Hashtable();
	            Hashtable temp = (Hashtable)record.get(rs.getString("tkh_id"));
	            Vector group = null;
	            Vector vFullPath = null;
	            String full_path = null;

	            if (temp != null) {
	                group = (Vector)temp.get("group");
	                vFullPath = (Vector)temp.get("full_path");
	            } else {
	                group = new Vector();
	                vFullPath = new Vector();
	            }

	            entity.append("<entity ent_id=\"").append(rs.getLong("usr_ent_id")).append("\" ");
	            entity.append("first_name=\"").append(dbUtils.esc4XML(rs.getString("usr_first_name_bil"))).append("\" ");
	            entity.append("last_name=\"").append(dbUtils.esc4XML(rs.getString("usr_last_name_bil"))).append("\">");
	            entity.append(dbUtils.esc4XML(rs.getString("usr_display_bil"))).append("</entity>").append(dbUtils.NEWL);

	            if (wizbini!=null && wizbini.cfgSysSetupadv.getOrganization().isMultiple()){
	                full_path = cwUtils.esc4XML(rs.getString("ste_name")) + wizbini.cfgSysSetupadv.getGroupMemberFullPathSeparator() + cwUtils.esc4XML(fullpathStr);
	            } else {
	                full_path = cwUtils.esc4XML(fullpathStr);
	            }
	            vFullPath.addElement(full_path);
	            group.addElement(cwUtils.esc4XML(rs.getString("usg_display_bil")));

	            progress.append("<progress tkh_id=\"").append(rs.getLong("tkh_id")).append("\" status=\"");

	            if (status != null) {
	                progress.append(status).append("\" attempt_nbr=\"").append(rs.getString(rs.getInt("pgr_attempt_nbr"))).append("\" pgr_score=\"");

	                if (pgr_score != null) {
	                    progress.append(new Float(pgr_score));
	                } else {
	                    progress.append("");
	                }

	                progress.append("\" grade=\"").append(rs.getString("pgr_grade")).append("\" pgr_max_score=\"");

	                if (pgr_max_score != null) {
	                    progress.append(new Float(pgr_max_score));
	                } else {
	                    progress.append("");
	                }

	                progress.append("\" complete_datetime=\"").append(rs.getTimestamp("pgr_complete_datetime"));

	                String due_date = dbass.getDueDateByAppTrackingID(con, rs.getLong("usr_ent_id"), tkh_id);
	                if (due_date == null) {
	                    due_date = "";
	                }
	                progress.append("\" due_datetime=\"").append(due_date).append("\"");
	            } else {
	                progress.append("Not Submitted Yet\"");
	            }

	            progress.append("/>").append(dbUtils.NEWL);

	            single.put("usrid", rs.getString("usr_ste_usr_id"));
	            single.put("entity", entity.toString());
	            single.put("progress", progress.toString());
	            single.put("group", group);
	            single.put("full_path", vFullPath);
	            single.put("usr_ent_id", new Long(rs.getLong("usr_ent_id")));
	            record.put(rs.getString("tkh_id"), single);

	            if (! lst.contains(rs.getString("tkh_id"))) {
	                lst.addElement(rs.getString("tkh_id"));
	            }

	            count++;
			}
        }

        for (int i=0; i<lst.size(); i++) {
            Hashtable single = (Hashtable)record.get(lst.elementAt(i));

            if (single != null) {
                String entity = (String)single.get("entity");
                String progress = (String)single.get("progress");
                Long usrEntIdObj = (Long) single.get("usr_ent_id");
                long usrEntId = usrEntIdObj.longValue();
                Vector group = (Vector)single.get("group");
                Vector vFullPath = (Vector)single.get("full_path");

                result.append("<record usr_id=\"").append(dbUtils.esc4XML((String)single.get("usrid"))).append("\">");
                result.append(entity);

                for (int j=0; j<group.size(); j++) {
                    result.append("<full_path>").append(vFullPath.elementAt(j)).append("</full_path>");
                    result.append("<group name=\"").append(group.elementAt(j)).append("\" />").append(dbUtils.NEWL);
                }

                result.append(progress);
                result.append("</record>").append(dbUtils.NEWL);
            }
        }

        result.append("</enrolment_list>");

        if (stmt != null) {
            stmt.close();
        }

        return result.toString();
    }

	public String submissionAsXML(Connection con, HttpSession sess, loginProfile prof, long modId, String queue, cwPagination cwPage, Timestamp report_time, String metaXML, String user_code)
        throws qdbException, qdbErrMessage, SQLException, cwSysMessage {
        get(con);

        StringBuffer result = new StringBuffer();
        result.append("<enrolment_list cos_id=\"").append(cos_res_id).append("\" mod_id=\"").append(modId).append("\">").append(dbUtils.NEWL);
        result.append(prof.asXML()).append(dbUtils.NEWL);
        if (metaXML !=null){
            result.append(metaXML).append(dbUtils.NEWL);
        }
        result.append("<title>").append(dbUtils.esc4XML(res_title)).append("</title>").append(dbUtils.NEWL);

        dbModule dbmod = new dbModule();
        dbmod.mod_res_id = modId;
        dbmod.get(con);

        Hashtable sess_h_queue = (Hashtable)sess.getAttribute(SESS_TST_QUEUE_HASH);
        Timestamp sess_tst_time = (Timestamp)sess.getAttribute(SESS_TST_TIMESTAMP);
        Hashtable sess_h_stat = (Hashtable)sess.getAttribute(SESS_TST_QUEUE_STAT);

		Vector vec_all = new Vector();
		Vector vec_graded = new Vector();
		Vector vec_not_graded = new Vector();
		Vector vec_not_submitted = new Vector();
        Vector vec_not_submitted_tkh = new Vector();

		int i_all = 0;
		int i_graded = 0;
		int i_not_graded = 0;
		int i_not_submitted = 0;
		Timestamp curTime = dbUtils.getTime(con);

		String SQL_GET_USR = OuterJoinSqlStatements.dbCourseSubmissionAsXML();
//		String SQL_GET_USR = " SELECT DISTINCT usr_id, usr_ent_id, pgr_usr_id , pgr_res_id, " +
//			" pgr_attempt_nbr, pgr_status, pgr_tkh_id, usr_display_bil " +
//			" FROM RegUser, Progress , Enrolment " +
//			" WHERE usr_ent_id = enr_ent_id " +
//			" AND enr_res_id = ? " +
//			" AND pgr_usr_id" + cwSQL.get_right_join(con) + "usr_id " +
//		 	" AND pgr_res_id = ? ";
        if(user_code == null){
        	user_code="";
        }
		PreparedStatement stmt = con.prepareStatement(SQL_GET_USR);
        int index=1;
        stmt.setLong(index++, modId);
        stmt.setLong(index++, cos_res_id);
        stmt.setString(index++, "%" + user_code +"%");

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            String status = rs.getString("pgr_status");
            String userStatus = rs.getString("usr_status");
            //过滤用户、回收站用户不参与统计(测验)
            Boolean userType = false;
            if(userStatus.equals("OK") || userStatus.equals("SYS")){
                userType = true;
            }
            // only collected the latest attempt
            if(rs.getLong("pgr_attempt_nbr") == dbProgress.getLastAttemptNbr(con, rs.getLong("pgr_res_id"), rs.getLong("pgr_tkh_id")) && userType) {
                if (status == null) {
                    //vec_not_submitted.addElement(new Long(rs.getString("usr_ent_id")));
                    vec_not_submitted.addElement(new Long(rs.getString("tkh_id")));
                    i_not_submitted++;//未提交
                } else if (status.equals(dbProgress.PGR_STATUS_NOT_GRADED)) {
                   // vec_not_graded.addElement(new Long(rs.getString("usr_ent_id")));
                    vec_not_graded.addElement(new Long(rs.getString("tkh_id")));
                    i_not_graded++;//为评分
                } else if (status.equals(dbProgress.PGR_STATUS_OK)) {
                    //vec_graded.addElement(new Long(rs.getString("usr_ent_id")));
                    vec_graded.addElement(new Long(rs.getString("tkh_id")));
                    i_graded++;//已评分
                }
                i_all++;//所有
                //vec_all.addElement(new Long(rs.getLong("usr_ent_id")));
                vec_all.addElement(new Long(rs.getLong("tkh_id")));
            }
        }

        result.append(dbmod.getModHeader(con, prof));
        result.append("<stat time=\"").append(curTime).append("\">");
        result.append("<queue name=\"all\" count=\"").append(i_all).append("\" />");
        result.append("<queue name=\"graded\" count=\"").append(i_graded).append("\" />");
        if(dbmod.hasEssay(con)) {
            result.append("<queue name=\"not_graded\" count=\"").append(i_not_graded).append("\" />");
        }
        result.append("<queue name=\"not_submitted\" count=\"").append(i_not_submitted).append("\" />");
        result.append("</stat>").append(dbUtils.NEWL);

        StringBuffer set = new StringBuffer();
        Vector ids = new Vector();

		String[] allowableCols = {"usr_display_bil", "usg_display_bil", "tkh_create_timestamp", "pgr_complete_datetime", "pgr_score"};
        if (cwPage.curPage == 0) {
            cwPage.curPage = 1;
        }
        if (cwPage.pageSize == 0) {
        	cwPage.pageSize = PAGE_SIZE;
        }
        if (cwPage.sortCol == null) {
        	cwPage.sortCol = " usr_display_bil ";
        } else {
        	boolean flag = false;
        	for(int k=0; k<allowableCols.length; k++) {
        		if(cwPage.sortCol.equalsIgnoreCase(allowableCols[k])) {
        			flag = true;
        		}
        	}
        	if(!flag) {
        		cwPage.sortCol = " usr_display_bil ";
        	}
        }
        if (cwPage.sortOrder == null) {
        	cwPage.sortOrder = " asc ";
        }
        cwPage.ts = curTime;

		//String colName = "tmp_usr_id";
        String colName = "tmp_tkh_id";
		String tableName = cwSQL.createSimpleTemptable(con, colName, cwSQL.COL_TYPE_LONG, 0);
		//由于mysql里面临时表在查询中只允许查询一次，故必要时需要将临时表装换成物理表
		String physicalTableName = null;
		MYSQLDbHelper mysqlDbHelper = null;
		boolean isMysql = false;
		if(cwSQL.DBVENDOR_MYSQL.equalsIgnoreCase(cwSQL.getDbType())){
			mysqlDbHelper = new MYSQLDbHelper();
			isMysql = true;
		}
		String orderBy = " ORDER BY " + cwPage.sortCol + " " + cwPage.sortOrder;
        String SQL = "";
        index = 1;
        if (queue.equalsIgnoreCase(NOT_SUBMITTED)) {
			cwSQL.insertSimpleTempTable(con, tableName, vec_not_submitted, cwSQL.COL_TYPE_LONG);
			if(isMysql){
				physicalTableName = mysqlDbHelper.tempTable2Physical(con, tableName);
			}
            SQL = " SELECT usr_id, usr_ent_id, usr_last_name_bil, usr_first_name_bil, usr_display_bil, usg_display_bil, tkh_create_timestamp,ern_ancestor_ent_id, " + cwSQL.get_null_sql(cwSQL.COL_TYPE_STRING) + " pgr_status, " + cwSQL.get_null_sql(cwSQL.COL_TYPE_DECIMAL) + " AS pgr_score, " + cwSQL.get_null_sql(cwSQL.COL_TYPE_DECIMAL) + " AS pgr_max_score, tkh_id AS pgr_tkh_id " //+ cwSQL.get_null_sql(con, cwSQL.COL_TYPE_INTEGER) + " AS pgr_tkh_id "
                + " FROM EntityRelation, RegUser, UserGroup, TrackingHistory," + (isMysql==true?physicalTableName:tableName)
                + " WHERE tkh_id = " + colName
                + " AND tkh_id NOT IN (SELECT pgr_tkh_id FROM Progress, " + (isMysql==true?physicalTableName:tableName)
                + " WHERE pgr_tkh_id = " + colName
                + " AND pgr_res_id = ? ) "
                + " AND usr_ent_id = tkh_usr_ent_id  "
				+ " AND usg_ent_id = ern_ancestor_ent_id  "
                + " AND usr_ent_id = ern_child_ent_id "
                + " AND ern_parent_ind = ? "
               // + " AND enr_ent_id = usr_ent_id "
                + " AND tkh_cos_res_id = ? "
                + orderBy;
            index = 1;
            stmt = con.prepareStatement(SQL);
            stmt.setLong(index++, modId);
            stmt.setBoolean(index++, true);
            stmt.setLong(index++, cos_res_id);
        } else if (queue.equalsIgnoreCase(NOT_GRADED)) {
			cwSQL.insertSimpleTempTable(con, tableName, vec_not_graded, cwSQL.COL_TYPE_LONG);
            SQL = " SELECT usr_id, usr_ent_id, usr_last_name_bil, usr_first_name_bil, usr_display_bil, usg_display_bil, tkh_create_timestamp , ern_ancestor_ent_id, pgr_score, pgr_grade, pgr_max_score, pgr_start_datetime, pgr_complete_datetime, pgr_status, pgr_attempt_nbr, pgr_tkh_id "
                + " FROM EntityRelation, RegUser, UserGroup, Progress T, TrackingHistory,"
                + " (SELECT pgr_usr_id AS pgr_usr_id_max, pgr_res_id AS pgr_res_id_max, max(pgr_attempt_nbr) AS pgr_attempt_nbr_max ,pgr_tkh_id  AS pgr_tkh_id_max "
                + " FROM progress A WHERE A.pgr_res_id = ? "// AND A.pgr_tkh_id = (SELECT MAX(pgr_tkh_id) from Progress B where A.pgr_res_id = B.pgr_res_id AND A.pgr_usr_id = B.pgr_usr_id) "
                + " GROUP BY pgr_usr_id, pgr_res_id,pgr_tkh_id) Q1, " + tableName
                + " WHERE tkh_id = " + colName
                + " AND pgr_tkh_id = tkh_id  "
                + " AND usr_ent_id = tkh_usr_ent_id  "
                + " AND usg_ent_id = ern_ancestor_ent_id  AND usr_ent_id = ern_child_ent_id "
                + " AND ern_parent_ind = ? "
                + " AND pgr_usr_id = usr_id "
                + " AND pgr_usr_id=pgr_usr_id_max AND pgr_res_id_max=pgr_res_id AND pgr_attempt_nbr = pgr_attempt_nbr_max "
                + " AND pgr_res_id = ? AND pgr_status = ? AND tkh_cos_res_id = ? "
                + " AND pgr_tkh_id = pgr_tkh_id_max "//(SELECT MAX(pgr_tkh_id) from Progress B where T.pgr_res_id = B.pgr_res_id AND T.pgr_usr_id = B.pgr_usr_id) "
				+ orderBy;

            stmt = con.prepareStatement(SQL);
            index = 1;
            stmt.setLong(index++, modId);
            stmt.setBoolean(index++, true);
            stmt.setLong(index++, modId);
            stmt.setString(index++, dbProgress.PGR_STATUS_NOT_GRADED);
            stmt.setLong(index++, cos_res_id);
        } else if (queue.equalsIgnoreCase(GRADED)) {
			cwSQL.insertSimpleTempTable(con, tableName, vec_graded, cwSQL.COL_TYPE_LONG);
            SQL = " SELECT usr_id, usr_ent_id, usr_last_name_bil, usr_first_name_bil, usr_display_bil, usg_display_bil, tkh_create_timestamp , ern_ancestor_ent_id, pgr_score, pgr_grade, pgr_max_score, pgr_start_datetime, pgr_complete_datetime, pgr_status, pgr_attempt_nbr, pgr_tkh_id "
                + " FROM EntityRelation, RegUser, UserGroup, Progress T, TrackingHistory,"
                + " (SELECT pgr_usr_id AS pgr_usr_id_max, pgr_res_id AS pgr_res_id_max, max(pgr_attempt_nbr) AS pgr_attempt_nbr_max ,pgr_tkh_id  AS pgr_tkh_id_max "
                + " FROM progress A WHERE A.pgr_res_id = ? "//AND A.pgr_tkh_id = (SELECT MAX(pgr_tkh_id) from Progress B where A.pgr_res_id = B.pgr_res_id AND A.pgr_usr_id = B.pgr_usr_id) "
                + " GROUP BY pgr_usr_id, pgr_res_id,pgr_tkh_id) Q1, " + tableName
                + " WHERE tkh_id = " + colName
                + " AND pgr_tkh_id = tkh_id  "
                + " AND usr_ent_id = tkh_usr_ent_id  "
                + " AND usg_ent_id = ern_ancestor_ent_id  AND usr_ent_id = ern_child_ent_id "
                + " AND ern_parent_ind = ? "
                + " AND pgr_usr_id = usr_id "
                + " AND pgr_usr_id=pgr_usr_id_max AND pgr_res_id_max=pgr_res_id AND pgr_attempt_nbr = pgr_attempt_nbr_max "
                + " AND pgr_res_id = ? AND pgr_status = ? AND tkh_cos_res_id = ? "
                + " AND pgr_tkh_id = pgr_tkh_id_max "//(SELECT MAX(pgr_tkh_id) from Progress B where T.pgr_res_id = B.pgr_res_id AND T.pgr_usr_id = B.pgr_usr_id) "
				+ orderBy;

            stmt = con.prepareStatement(SQL);
            index = 1;
            stmt.setLong(index++, modId);
            stmt.setBoolean(index++, true);
            stmt.setLong(index++, modId);
            stmt.setString(index++, dbProgress.PGR_STATUS_OK);
            stmt.setLong(index++, cos_res_id);
        } else if(queue.equalsIgnoreCase(ALL)) {
			cwSQL.insertSimpleTempTable(con, tableName, vec_all, cwSQL.COL_TYPE_LONG);
			if(isMysql){
				physicalTableName = mysqlDbHelper.tempTable2Physical(con, tableName);
			}
			SQL = " SELECT usr_id, usr_ent_id, usr_last_name_bil, usr_first_name_bil, usr_display_bil, usg_display_bil, tkh_create_timestamp , ern_ancestor_ent_id, " + cwSQL.get_null_sql(cwSQL.COL_TYPE_DECIMAL) + " AS pgr_score, " + cwSQL.get_null_sql(cwSQL.COL_TYPE_STRING) + " AS pgr_grade, " + cwSQL.get_null_sql(cwSQL.COL_TYPE_DECIMAL) + " AS pgr_max_score, " + cwSQL.get_null_sql(cwSQL.COL_TYPE_TIMESTAMP) + " AS pgr_start_datetime, " + cwSQL.get_null_sql(cwSQL.COL_TYPE_TIMESTAMP) + " AS pgr_complete_datetime, " + cwSQL.get_null_sql(cwSQL.COL_TYPE_STRING) + " AS pgr_status, " + cwSQL.get_null_sql(cwSQL.COL_TYPE_INTEGER) + " AS pgr_attempt_nbr, tkh_id AS pgr_tkh_id " //+ cwSQL.get_null_sql(con, cwSQL.COL_TYPE_INTEGER) + " AS pgr_tkh_id "
//            SQL = " SELECT usr_id, usr_ent_id, usr_last_name_bil, usr_first_name_bil, usr_display_bil, usg_display_bil, enr_create_timestamp, gpm_full_path, NULL AS pgr_score, NULL AS pgr_grade, NULL AS pgr_max_score, NULL AS pgr_start_datetime, NULL AS pgr_complete_datetime, NULL AS pgr_status, NULL AS pgr_attempt_nbr, NULL AS pgr_tkh_id "
                + " FROM EntityRelation, RegUser, UserGroup, TrackingHistory," + (isMysql==true?physicalTableName:tableName)
                + " WHERE tkh_id = " + colName
                + " AND tkh_id NOT IN (SELECT pgr_tkh_id FROM Progress, " + (isMysql==true?physicalTableName:tableName)
                + " WHERE pgr_tkh_id = " + colName
                + " AND pgr_res_id = ? )"
                + " AND usr_ent_id = tkh_usr_ent_id  "
                + " AND usg_ent_id = ern_ancestor_ent_id  "
                + " AND usr_ent_id = ern_child_ent_id "
                + " AND ern_parent_ind = ? "
                + " AND tkh_cos_res_id = ? ";
            SQL += " UNION ";
            SQL += " SELECT usr_id, usr_ent_id, usr_last_name_bil, usr_first_name_bil, usr_display_bil, usg_display_bil, tkh_create_timestamp, ern_ancestor_ent_id, pgr_score, pgr_grade, pgr_max_score, pgr_start_datetime, pgr_complete_datetime, pgr_status, pgr_attempt_nbr, pgr_tkh_id "
                + " FROM EntityRelation, RegUser, UserGroup, Progress T, TrackingHistory,"
                + " (SELECT pgr_usr_id AS pgr_usr_id_max, pgr_res_id AS pgr_res_id_max, max(pgr_attempt_nbr) AS pgr_attempt_nbr_max ,pgr_tkh_id  AS pgr_tkh_id_max "
                + " FROM progress A WHERE A.pgr_res_id = ? " //AND A.pgr_tkh_id = (SELECT MAX(pgr_tkh_id) from Progress B where A.pgr_res_id = B.pgr_res_id AND A.pgr_usr_id = B.pgr_usr_id) "
                + " GROUP BY pgr_usr_id, pgr_res_id, pgr_tkh_id) Q1, " + tableName
                + " WHERE tkh_id = " + colName
                + " AND pgr_tkh_id = tkh_id  "
                + " AND usr_ent_id = tkh_usr_ent_id  "
                + " AND usg_ent_id = ern_ancestor_ent_id  AND usr_ent_id = ern_child_ent_id "
                + " AND ern_parent_ind = ? "
                + " AND pgr_usr_id = usr_id "
                + " AND pgr_usr_id=pgr_usr_id_max AND pgr_res_id_max=pgr_res_id AND pgr_attempt_nbr = pgr_attempt_nbr_max "
                + " AND pgr_res_id = ? AND (pgr_status = ? OR pgr_status = ?) AND tkh_cos_res_id = ? "
                + " AND pgr_tkh_id = pgr_tkh_id_max "//(SELECT MAX(pgr_tkh_id) from Progress B where T.pgr_res_id = B.pgr_res_id AND T.pgr_usr_id = B.pgr_usr_id) "
				+ orderBy;

            index = 1;
            stmt = con.prepareStatement(SQL);
            stmt.setLong(index++, modId);
            stmt.setBoolean(index++, true);
            stmt.setLong(index++, cos_res_id);

            stmt.setLong(index++, modId);
            stmt.setBoolean(index++, true);
            stmt.setLong(index++, modId);
            stmt.setString(index++, dbProgress.PGR_STATUS_OK);
            stmt.setString(index++, dbProgress.PGR_STATUS_NOT_GRADED);
            stmt.setLong(index++, cos_res_id);
        }

        rs = stmt.executeQuery();
        Vector lst = new Vector();
        Hashtable record = new Hashtable();
        EntityFullPath entityfullpath = EntityFullPath.getInstance(con);
        int count = 0;
        while (rs.next()) {
            String  pgr_tkh_id_S=String.valueOf(rs.getLong("pgr_tkh_id"));
            if (! lst.contains(pgr_tkh_id_S)) {
                lst.addElement(pgr_tkh_id_S);
            	count++;
            }

        	if(count > (cwPage.curPage-1)*cwPage.pageSize && count<cwPage.curPage*cwPage.pageSize+1) {
        		long ancestor_id = rs.getLong("ern_ancestor_ent_id");
				String status = rs.getString("pgr_status");
				String pgr_score = rs.getString("pgr_score");
				String pgr_max_score = rs.getString("pgr_max_score");
				long pgr_tkh_id = rs.getLong("pgr_tkh_id");
				StringBuffer entity = new StringBuffer();
				StringBuffer progress = new StringBuffer();
				Timestamp enrolmentTimestamp = rs.getTimestamp("tkh_create_timestamp");
				Hashtable single = new Hashtable();
				Hashtable temp = (Hashtable)record.get(pgr_tkh_id_S);
				Vector group = null;
				/*if (temp != null) {
					group = (Vector)temp.get("group");
				} else {
					group = new Vector();
				}
                */
                group = new Vector();
				entity.append("<entity ent_id=\"").append(rs.getLong("usr_ent_id")).append("\" ");
				entity.append("first_name=\"").append(dbUtils.esc4XML(rs.getString("usr_first_name_bil"))).append("\" ");
				entity.append("last_name=\"").append(dbUtils.esc4XML(rs.getString("usr_last_name_bil"))).append("\">");
				entity.append(dbUtils.esc4XML(rs.getString("usr_display_bil"))).append("</entity>").append(dbUtils.NEWL);
				entity.append("<enrolment enr_create_timestamp=\"" + enrolmentTimestamp + "\"/>");

				//group.addElement(dbUtils.esc4XML(rs.getString("usg_display_bil")));
				group.addElement(dbUtils.esc4XML(entityfullpath.getFullPath(con, ancestor_id)));

				progress.append("<progress status=\"");

				if (status != null) {
					progress.append(status).append("\" attempt_nbr=\"").append(rs.getInt("pgr_attempt_nbr")).append("\" pgr_score=\"");

					if (pgr_score != null) {
						progress.append(new Float(pgr_score));
					} else {
						progress.append("");
					}

					progress.append("\" grade=\"").append(rs.getString("pgr_grade")).append("\" pgr_max_score=\"");

					if (pgr_max_score != null) {
						progress.append(new Float(pgr_max_score));
					} else {
						progress.append("");
					}
					progress.append("\"");

					progress.append(" complete_datetime=\"").append(rs.getTimestamp("pgr_complete_datetime")).append("\"");
					progress.append(" tkh_id=\"").append(rs.getLong("pgr_tkh_id")).append("\"");
				} else {
					progress.append("Not Submitted Yet\"");
				}

				progress.append("/>").append(dbUtils.NEWL);

				single.put("entity", entity.toString());
				single.put("progress", progress.toString());
				single.put("group", group);

				//record.put(rs.getString("usr_id"), single);

               /*
				if (! lst.contains(rs.getString("usr_id"))) {
					lst.addElement(rs.getString("usr_id"));
				}
                */

               // if (! lst.contains(pgr_tkh_id_S)) {
                    record.put(pgr_tkh_id_S, single);

               // }
            }
        }
        cwPage.totalRec = count;
		cwPage.totalPage = cwPage.totalRec / cwPage.pageSize;
		if (cwPage.totalRec % cwPage.pageSize != 0){
			cwPage.totalPage++;
		}
        rs.close();
		cwSQL.dropTempTable(con, tableName);
		if(isMysql){
			mysqlDbHelper.dropTable(con, physicalTableName);
		}

        result.append("<queue type=\"" + queue + "\"/>");
        for (int i=0; i<lst.size(); i++) {
            Hashtable single = (Hashtable)record.get(lst.elementAt(i));

            if (single != null) {
                String entity = (String)single.get("entity");
                String progress = (String)single.get("progress");
                Vector group = (Vector)single.get("group");

                result.append("<record tkh_id=\"").append(dbUtils.esc4XML(dbRegUser.usrId2SteUsrId(con,(String)lst.elementAt(i)))).append("\">");
                result.append(entity);

                for (int j=0; j<group.size(); j++) {
                    result.append("<group name=\"").append(group.elementAt(j)).append("\" />").append(dbUtils.NEWL);
                }

                result.append(progress);
                result.append("</record>").append(dbUtils.NEWL);
            }
        }

		result.append(cwPage.asXML());
        result.append("</enrolment_list>");
        if (stmt != null) {
            stmt.close();
        }

        return result.toString();
    }

    public static String getAllCosAsXML(Connection con)
        throws qdbException
    {
        String result =  "<?xml version=\"1.0\" encoding=\"" + dbUtils.ENC_UTF + "\" standalone=\"no\" ?>" + dbUtils.NEWL;

        result += "<course_list>" + dbUtils.NEWL;

        String SQL =
              " SELECT res_id ID, res_title TITLE, res_lan RLAN, "
            + " res_privilege PRIV, res_usr_id_owner OWNER, res_status RSTATUS, res_upd_date TS "
            + "  FROM Resources "
            + " WHERE res_type      = '" + RES_TYPE_COS + "'"
            + " AND res_status = '" + RES_STATUS_ON + "' "
            + " order by TITLE ";

        try {
            PreparedStatement stmt = con.prepareStatement(SQL);
            ResultSet rs = stmt.executeQuery();

            long id;
            String owner, priv, status, title, lan;
            Timestamp ts;
            Timestamp eff_start_datetime, eff_end_datetime, cur_time;

            cur_time = dbUtils.getTime(con);
            while(rs.next())
            {
                    id = rs.getLong("ID");
                    title = rs.getString("TITLE");
                    priv = rs.getString("PRIV");
                    lan  = rs.getString("RLAN");
                    owner = rs.getString("OWNER");
                    ts = rs.getTimestamp("TS");
                    status = rs.getString("RSTATUS");

                    title = dbUtils.esc4XML(title);
                    eff_start_datetime = dbCourse.getCosStart(con,id);
                    eff_end_datetime = dbCourse.getCosEnd(con,id);

                    result += "<course id=\"" + id + "\" language=\"" + lan + "\" privilege=\"" + priv
                    + "\" owner=\"" + dbUtils.esc4XML(dbRegUser.usrId2SteUsrId(con,owner)) + "\" status=\"" + status + "\" timestamp=\"" + ts
                    + "\" eff_start_datetime=\"" + eff_start_datetime
                    + "\" eff_end_datetime=\"" + eff_end_datetime
                    + "\" cur_time=\"" + cur_time + "\">"
                    + "<title>" + title + "</title>" + dbUtils.NEWL
                    + "</course>" + dbUtils.NEWL;
            }

            result += "</course_list>" + dbUtils.NEWL;

            stmt.close();
            return result;

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    public static String getCosListAsXMLNoHeader(Connection con, loginProfile prof, String order, String dpo_view, boolean checkStatus, Vector v_cos_res_id)
        throws qdbException
    {
        String xml = getCosListAsXML(con, prof, order, dpo_view, checkStatus, v_cos_res_id);
        return xml.substring(xml.indexOf("?>")+2);
    }


    // if teacher, get all course for the school
    // if student, get all enrolled course
    public static String getCosListAsXML(Connection con, loginProfile prof, String order, String dpo_view, boolean checkStatus, Vector v_cos_res_id)
        throws qdbException
    {
        /*String usr_role = ""; */
        // determine role of user
        // Dennis: don't know what's the usage or usr_role
        /*
        if(prof.sysRoles.contains(dbUserGroup.USG_ROLE_TEACHER))
            usr_role = dbUserGroup.USG_ROLE_TEACHER;
        if(prof.sysRoles.contains(dbUserGroup.USG_ROLE_ADMIN))
            usr_role = dbUserGroup.USG_ROLE_ADMIN;
        if(prof.sysRoles.contains(dbUserGroup.USG_ROLE_STUDENT))
            usr_role = dbUserGroup.USG_ROLE_STUDENT;
        */
        try {
            Vector rpmVec = (v_cos_res_id == null)
                                ? dbResourcePermission.getResPermission(con, dbResource.RES_TYPE_COS, prof)
                                : v_cos_res_id;
            //Vector rpmVec = dbResourcePermission.getResPermission(con, dbResource.RES_TYPE_COS, prof);
            String res_id_list = dbResourcePermission.convertId2List(rpmVec);

            String SQL =
                " SELECT res_id ID, res_title TITLE, res_lan RLAN, "
                + " res_privilege PRIV, res_usr_id_owner OWNER, res_status RSTATUS, res_upd_date TS, "
                + " res_instructor_name RISTNAME, res_instructor_organization RISTORG, "
                + " cos_eff_start_datetime STARTDATE, cos_eff_end_datetime ENDDATE, "
                + " cos_structure_xml , "
                + " res_desc "
                + "  FROM Resources, Course "
                + " WHERE res_type      = '" + RES_TYPE_COS + "'"
                + " AND res_id = cos_res_id ";
                //filter out the offline modules for non-admin
                /*if(!prof.sysRoles.contains(dbUserGroup.USG_ROLE_ADMIN)) */
                if(checkStatus)
                    SQL += " AND res_status <> '" + RES_STATUS_OFF + "' ";

            SQL += "   AND res_id IN " + res_id_list + " ORDER by cos_eff_start_datetime desc, " ;

            if (order != null && !order.equalsIgnoreCase("res_title")) {
                SQL += order + ",";
            }

            SQL += " TITLE " ;

        /*try {*/
            PreparedStatement stmt = con.prepareStatement(SQL);
            ResultSet rs = stmt.executeQuery();

            String sformatCosListXML=formatCosListXML(con,rs, prof, rpmVec, dpo_view);
            stmt.close();

            return sformatCosListXML;

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }


    private static String formatCosListXML(Connection con, ResultSet rs, loginProfile prof, Vector rpmVec, String dpo_view)
        throws qdbException
    {
        // format xml
        String result =  "<?xml version=\"1.0\" encoding=\"" + dbUtils.ENC_UTF + "\" standalone=\"no\" ?>" + dbUtils.NEWL;
        result += "<course_list>" + dbUtils.NEWL;
        result += prof.asXML() + dbUtils.NEWL;

        try {
          long id;
          String title = "";
          String owner, priv, status, lan, RISTNAME, RISTORG, CSTRUCT, RDESC;
          Timestamp ts;
          Timestamp eff_start_datetime, eff_end_datetime, cur_time;
          cur_time = dbUtils.getTime(con);
          String tmp_end_datetime;

          Vector cosVec = new Vector();

          while(rs.next())
          {
                id = rs.getLong("ID");
                title = dbUtils.esc4XML(rs.getString("TITLE"));
                priv = rs.getString("PRIV");
                lan = rs.getString("RLAN");
                owner = rs.getString("OWNER");
                ts = rs.getTimestamp("TS");
                status = rs.getString("RSTATUS");
                RISTNAME = dbUtils.esc4XML(rs.getString("RISTNAME"));
                RISTORG = dbUtils.esc4XML(rs.getString("RISTORG"));
                RDESC = dbUtils.esc4XML(rs.getString("RDESC"));
                //RDESC = dbUtils.esc4XML(cwSQL.getClobValue(con, rs, "res_desc"));

                //CSTRUCT = rs.getString("CSTRUCT");
                CSTRUCT = cwSQL.getClobValue(rs, "cos_structure_xml");

                eff_start_datetime = rs.getTimestamp("STARTDATE");
                eff_end_datetime = rs.getTimestamp("ENDDATE");
                tmp_end_datetime = dbUtils.convertEndDate(eff_end_datetime);
                RISTNAME = dbUtils.esc4XML(RISTNAME);
                RISTORG =  dbUtils.esc4XML(RISTORG);

                RDESC = dbUtils.esc4XML(RDESC);

                result += "<course id=\"" + id + "\" language=\"" + lan + "\" privilege=\"" + priv
                 + "\" owner=\"" + dbUtils.esc4XML(dbRegUser.usrId2SteUsrId(con,owner)) + "\" status=\"" + status + "\" timestamp=\"" + ts
                 + "\" eff_start_datetime=\"" + eff_start_datetime + "\" eff_end_datetime=\"" + tmp_end_datetime
                 + "\" cur_time=\"" + cur_time + "\">" + dbUtils.NEWL
                 // Course Structure
                 + CSTRUCT + dbUtils.NEWL
                 + "<title>" + title + "</title>" + dbUtils.NEWL
                 + "<desc>" + RDESC + "</desc>" + dbUtils.NEWL
                 + "<instructor>" + RISTNAME + "</instructor>" + dbUtils.NEWL
                 + "<moderator>" + dbUtils.esc4XML(getOwnerName(con,id)) + "</moderator>" + dbUtils.NEWL
                 + "<organization>" + RISTORG + "</organization>" + dbUtils.NEWL
                 + getCosDisplayOption(con, id, dpo_view) + dbUtils.NEWL
                 + "</course>" + dbUtils.NEWL;

                 cosVec.addElement(new Long(id));
          }

          // Get Aicc Data of the courses
          result += dbCourseEvaluation.getProgressStatus(con, prof.usr_ent_id, cosVec);

          result += dbResourcePermission.asXML(rpmVec);
          // close
          result += "</course_list>" + dbUtils.NEWL;
          return result;

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    public static String getCosDisplayOption(Connection con, long id, String view)
      throws qdbException {
        String xml = "";
        dbDisplayOption dpo = new dbDisplayOption();
        dpo.dpo_res_id = id;

        //get display option for a course
        dpo.dpo_res_type = RES_TYPE_COS;
        dpo.dpo_res_subtype = RES_TYPE_COS;

        dpo.dpo_view = view;
        //xml = dpo.allViewAsXML(con);
        xml = dpo.getViewAsXML(con);
        return xml;
    }

    public String getModListAsXML(Connection con, String domain, String mod_type, loginProfile prof)
        throws qdbException
    {
        Vector rpmVec = dbResourcePermission.getResPermission(con, dbResource.RES_TYPE_MOD, prof);
        String res_id_list = dbResourcePermission.convertId2List(rpmVec);

        String SQL =
            "SELECT distinct res_id RID, res_type RTYPE,  "
          + "       res_privilege RPRIV, res_status RSTATUS, res_usr_id_owner ROWNER, "
          + "       res_lan RLAN, "
          + "       res_upd_date  RTIMESTAMP, res_title RTITLE,  "
          + "       mod_type MTYPE, res_tpl_name RTEMPLATE "
          + "  FROM Resources, ResourceContent, Module, ResourcePermission "
          + " WHERE res_id = rcn_res_id_content "
          + "   AND rcn_res_id = " + cos_res_id
          + "   AND res_id    = mod_res_id "
          + "   AND res_id    = rpm_res_id "
          + "   AND res_id    IN " + res_id_list ;

          String tplList = "";
          if (mod_type != null && !mod_type.equalsIgnoreCase(dbModule.MOD_TYPE_ALL)) {
            tplList = dbTemplate.tplListContentXML(con, prof, mod_type);
            SQL += "   AND mod_type  = '" + mod_type + "' ";
          }else {
            tplList = dbTemplate.tplListContentXML(con,prof, null);
          }

          // show the share module only
          if (domain.equalsIgnoreCase(dbUtils.DOMAIN_PUBLIC)) {
            SQL += "   AND rpm_ent_id = " + dbResourcePermission.SHARE_RES;
          }else if (domain.equalsIgnoreCase(dbUtils.DOMAIN_SCHOOL)) {
            SQL += "   AND rpm_res_id NOT IN "
                +  "       (SELECT rpm_res_id from ResourcePermission "
                +  "          WHERE rpm_ent_id = " + dbResourcePermission.SHARE_RES + ")";
          }

          SQL +=  "  ORDER BY RTITLE ASC ";

        try {
            PreparedStatement stmt1 = con.prepareStatement(
                "SELECT res_title RTITLE, res_type RTYPE, res_status RSTATUS, "
              + "          res_privilege RPRIV, res_upd_date RTIMESTAMP "
              + "  FROM Resources, Course where res_id = ? AND cos_res_id = res_id "  );
            stmt1.setLong(1, cos_res_id);
            ResultSet rs1 = stmt1.executeQuery();
            String xmlHeader = "";
            if(rs1.next())
            {
                xmlHeader += "<course id=\"" + cos_res_id + "\" type=\"" + rs1.getString("RTYPE")
                    + "\" status=\"" + rs1.getString("RSTATUS") + "\" privilege=\"" + rs1.getString("RPRIV")
                    + "\" eff_start_datetime=\"" + getCosStart(con,cos_res_id)
                    + "\" eff_end_datetime=\"" + getCosEnd(con,cos_res_id)
                    + "\" cur_time=\"" + dbUtils.getTime(con)
                    + "\" timestamp=\"" + rs1.getTimestamp("RTIMESTAMP") + "\">"
                    + dbResourcePermission.aclAsXML(con,cos_res_id, prof)
                    + "<title>" + dbUtils.esc4XML(rs1.getString("RTITLE")) + "</title>" + dbUtils.NEWL
                    + "</course>" + dbUtils.NEWL;
            }
            else
            {
                stmt1.close();
                throw new qdbException("Failed to get resource header.");
            }
            stmt1.close();

            PreparedStatement stmt = con.prepareStatement(SQL);

            ResultSet rs = stmt.executeQuery();
            String xmlBody = "";
            Vector modList = new Vector();
            long RID;
            String RLAN, RTYPE, MTYPE, RTITLE, RPRIV, ROWNER, RSTATUS, RTEMPLATE;
            Timestamp RTIMESTAMP;
            while(rs.next())
            {
                RID = rs.getLong("RID");
                RTYPE = rs.getString("RTYPE");
                MTYPE = rs.getString("MTYPE");
                RLAN = rs.getString("RLAN");
                RTITLE = dbUtils.esc4XML(rs.getString("RTITLE"));
                RPRIV = rs.getString("RPRIV");
                ROWNER = rs.getString("ROWNER");
                RSTATUS = rs.getString("RSTATUS");
                RTIMESTAMP = rs.getTimestamp("RTIMESTAMP");
                RTEMPLATE = rs.getString("RTEMPLATE");

                long cnt  = dbProgress.totalAttemptNum(con, RID);

                xmlBody += "<module id=\"" + RID + "\" type=\"" + MTYPE
                    + "\" privilege=\"" + RPRIV
                    + "\" status=\"" + RSTATUS + "\" owner=\"" + dbUtils.esc4XML(dbRegUser.usrId2SteUsrId(con,ROWNER))
                    + "\" language=\"" + RLAN
                    + "\" attempt=\"" + cnt
                    + "\" eff_start_datetime=\"" + dbModule.getModStart(con,RID)
                    + "\" eff_end_datetime=\"" + dbModule.getModEnd(con,RID)
                    + "\" cur_time=\"" + dbUtils.getTime(con)
                    + "\" timestamp=\"" + RTIMESTAMP + "\">" + dbUtils.NEWL
                    + "<title>" + RTITLE + "</title>" + dbUtils.NEWL
                    + "<template>" + RTEMPLATE + "</template>" + dbUtils.NEWL
                    + "</module>" + dbUtils.NEWL;

                 modList.addElement(new Long(RID)) ;
            }
            stmt.close();

            String result = "<?xml version=\"1.0\" encoding=\"" + dbUtils.ENC_UTF + "\" standalone=\"no\" ?>" + dbUtils.NEWL;
            result += "<module_list domain=\"" + domain + "\">" + dbUtils.NEWL;
            result += prof.asXML() + dbUtils.NEWL;

            result += "<header>" + dbUtils.NEWL;
            result += xmlHeader;
            result += "</header>" + dbUtils.NEWL;
            result += xmlBody;

            Vector rpmVec2 = new Vector();
            for (int i=0;i<rpmVec.size();i++) {
                dbResourcePermission dbrpm = (dbResourcePermission) rpmVec.elementAt(i);
                if (modList.contains(new Long(dbrpm.rpm_res_id)))
                    rpmVec2.addElement(dbrpm);
            }
            result += dbResourcePermission.asXML(rpmVec2);
            result += tplList;
            result += "</module_list>" + dbUtils.NEWL;

            return result;

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }


    // Return all the module that belongs to a course
    public String getAllModAsXML(Connection con, String mod_type, loginProfile prof, String dpo_view)
        throws qdbException, qdbErrMessage, cwSysMessage
    {
        String SQL =
            "SELECT distinct res_id RID, res_type RTYPE,  "
          + "       res_privilege RPRIV, res_status RSTATUS, res_usr_id_owner ROWNER, "
          + "       res_lan RLAN, "
          + "       res_upd_date  RTIMESTAMP, res_title RTITLE,  "
          + "       mod_type MTYPE, res_tpl_name RTEMPLATE, "
          + "       res_difficulty RDIFF, res_duration RDUR, res_desc , "
          + "       res_instructor_name RISTNAME, res_instructor_organization RISTORG, "
          + "       mod_max_score MMAXSCORE, mod_pass_score MPASSSCORE, mod_instruct MINSTRUCT"
          + "  FROM Resources, ResourceContent, Module"
          + " WHERE res_id = rcn_res_id_content "
          + "   AND rcn_res_id = " + cos_res_id
          + "   AND res_id    = mod_res_id " ;


        if (mod_type != null && !mod_type.equalsIgnoreCase(dbModule.MOD_TYPE_ALL)) {
                SQL += "   AND mod_type  = '" + mod_type + "' ";
        }

        SQL += "  ORDER BY RTITLE ASC ";

        try {
            PreparedStatement stmt1 = con.prepareStatement(
                "SELECT res_title RTITLE, res_type RTYPE, res_status RSTATUS, "
              + "          res_privilege RPRIV, res_upd_date RTIMESTAMP "
              + "  FROM Resources, Course where res_id = ? AND cos_res_id = res_id "  );
            stmt1.setLong(1, cos_res_id);
            ResultSet rs1 = stmt1.executeQuery();
            String xmlHeader = "";
            if(rs1.next())
            {
                xmlHeader += "<course id=\"" + cos_res_id + "\" type=\"" + rs1.getString("RTYPE")
                    + "\" status=\"" + rs1.getString("RSTATUS") + "\" privilege=\"" + rs1.getString("RPRIV")
                    + "\" eff_start_datetime=\"" + getCosStart(con,cos_res_id)
                    + "\" eff_end_datetime=\"" + dbUtils.convertEndDate(getCosEnd(con,cos_res_id))
                    + "\" cur_time=\"" + dbUtils.getTime(con)
                    + "\" timestamp=\"" + rs1.getTimestamp("RTIMESTAMP") + "\">"
                    + "<title>" + dbUtils.esc4XML(rs1.getString("RTITLE")) + "</title>" + dbUtils.NEWL
                    + "</course>" + dbUtils.NEWL;
            }
            else
            {
                stmt1.close();
                throw new qdbException("Failed to get resource header.");
            }


            stmt1.close();
            PreparedStatement stmt = con.prepareStatement(SQL);

            ResultSet rs = stmt.executeQuery();
            String xmlBody = "";
            Vector modList = new Vector();
            long RID;
            String RLAN, RTYPE, MTYPE, RTITLE, RPRIV, ROWNER, RSTATUS, RTEMPLATE;
            Timestamp RTIMESTAMP;
            int RDIFF;
            float RDUR;
            float MMAXSCORE;
            int MPASSSCORE;
            Timestamp PSTART, PCOMPLETE, PLASTACC;
            String MINSTRUCT;
            String RDESC;
            long PATTEMPTNBR;
            String RISTNAME;
            String RISTORG;
            dbEvent dbevt;
            String evtBody;
            Timestamp eff_start_datetime, eff_end_datetime, cur_time;

            cur_time = dbUtils.getTime(con);
            while(rs.next())
            {
                RID = rs.getLong("RID");
                RTYPE = rs.getString("RTYPE");
                MTYPE = rs.getString("MTYPE");
                RLAN = rs.getString("RLAN");
                RTITLE = dbUtils.esc4XML(rs.getString("RTITLE"));
                RPRIV = rs.getString("RPRIV");
                ROWNER = rs.getString("ROWNER");
                RSTATUS = rs.getString("RSTATUS");
                RTIMESTAMP = rs.getTimestamp("RTIMESTAMP");
                RTEMPLATE = rs.getString("RTEMPLATE");
                RDIFF = rs.getInt("RDIFF");
                RDUR = rs.getFloat("RDUR");
                RDESC = dbUtils.esc4XML(rs.getString("res_desc"));
                //RDESC = dbUtils.esc4XML(cwSQL.getClobValue(con, rs, "res_desc"));
                RISTNAME = rs.getString("RISTNAME");
                RISTORG = rs.getString("RISTORG");
                MMAXSCORE = rs.getFloat("MMAXSCORE");
                MPASSSCORE = new Float(rs.getFloat("MPASSSCORE")).intValue();
                MINSTRUCT = dbUtils.esc4XML(rs.getString("MINSTRUCT"));
                PSTART = null;
                PCOMPLETE = null;
                PLASTACC = null;
                PATTEMPTNBR = 0;
                eff_start_datetime = dbModule.getModStart(con,RID);
                eff_end_datetime = dbModule.getModEnd(con,RID);

                long cnt  = dbProgress.totalAttemptNum(con, RID);


                //Dennis, display option, get the attributes that needed to be displayed
                dbProgress pgr = new dbProgress();
                pgr.pgr_usr_id = prof.usr_id;
                pgr.pgr_res_id = RID;
                PATTEMPTNBR = pgr.usrAttemptNum(con,RID,prof.usr_id);
                if(PATTEMPTNBR > 0) {
                    pgr.get(con,1);
                    PSTART = pgr.pgr_start_datetime;

                    pgr.get(con);
                    PCOMPLETE = pgr.pgr_complete_datetime;
                    PLASTACC = pgr.pgr_last_acc_datetime;
                }

                evtBody = "";
                if(dbEvent.isEventType(MTYPE) == true) {
                    dbevt = new dbEvent();
                    dbevt.evt_res_id = RID;
                    evtBody = dbevt.getEventAsXML(con);
                }


                xmlBody += "<module id=\"" + RID + "\" type=\"" + MTYPE
                    + "\" privilege=\"" + RPRIV
                    + "\" status=\"" + RSTATUS + "\" owner=\"" + dbUtils.esc4XML(dbRegUser.usrId2SteUsrId(con,ROWNER))
                    + "\" language=\"" + RLAN
                    + "\" attempt=\"" + cnt
                    + "\" eff_start_datetime=\"" + eff_start_datetime
                    + "\" eff_end_datetime=\"" + dbUtils.convertEndDate(eff_end_datetime)
                    + "\" cur_time=\"" + cur_time

                    + "\" difficulty=\"" + RDIFF
                    + "\" duration=\"" + RDUR
                    + "\" time_limit=\"" + RDUR
                    + "\" suggested_time=\"" + RDUR;

                    if (MTYPE != null && MTYPE.equalsIgnoreCase("ASS")) {
                        dbAssignment ass = new dbAssignment();
                        ass.ass_res_id = RID;
                        ass.get(con);
                        xmlBody += "\" due_datetime=\"" + ass.ass_due_datetime;
                    }

                xmlBody += "\" max_score=\"" + MMAXSCORE
                    + "\" pass_score=\"" + MPASSSCORE
                    + "\" attempt_nbr=\"" + PATTEMPTNBR
                    + "\" pgr_start=\"" + PSTART
                    + "\" pgr_complete=\"" + PCOMPLETE
                    + "\" pgr_last_acc=\"" + PLASTACC
                    + "\" timestamp=\"" + RTIMESTAMP + "\">" + dbUtils.NEWL
                    + "<title>" + RTITLE + "</title>" + dbUtils.NEWL
                    + getDisplayOption(con, dpo_view) + dbUtils.NEWL
                    + "<template>" + RTEMPLATE + "</template>" + dbUtils.NEWL
                    + "</module>" + dbUtils.NEWL;

                 modList.addElement(new Long(RID)) ;
            }
            stmt.close();

            String result = "<?xml version=\"1.0\" encoding=\"" + dbUtils.ENC_UTF + "\" standalone=\"no\" ?>" + dbUtils.NEWL;
            result += "<module_list>" + dbUtils.NEWL;

            result += "<header>" + dbUtils.NEWL;
            result += xmlHeader;
            result += "</header>" + dbUtils.NEWL;
            result += xmlBody;

            result += "</module_list>" + dbUtils.NEWL;

            return result;

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    public String getCalendarAsXMLNoHeader(Connection con, loginProfile prof, long course_id, String owner_id, String instructor_id, Timestamp start_datetime, Timestamp end_datetime, Vector v_cos_res_id)
        throws qdbException , cwSysMessage, SQLException
    {
        String xml = getCalendarAsXML(con, prof, course_id, owner_id, instructor_id, start_datetime, end_datetime, v_cos_res_id);
        return xml.substring(xml.indexOf("?>")+2);
    }

// Dennis : get Calendar, modified from getAllCosModAsXML
    public String getCalendarAsXML(Connection con, loginProfile prof, long course_id, String owner_id, String instructor_id, Timestamp start_datetime, Timestamp end_datetime, Vector v_cos_res_id)
        throws qdbException, cwSysMessage,  SQLException
    {
        StringBuffer SQL = new StringBuffer();
        String course_title = "";
        Timestamp curTime = dbUtils.getTime(con);
        if(start_datetime == null)
            start_datetime = curTime;
        if(end_datetime == null)
            end_datetime = curTime;
        //get course that has permission
        Vector rpmVec = (v_cos_res_id == null)
                            ? dbResourcePermission.getResPermission(con, dbResource.RES_TYPE_COS, prof)
                            : v_cos_res_id;
        String res_id_list = dbResourcePermission.convertId2List(rpmVec);
        SQL.append(" SELECT res_id ID, res_title TITLE FROM Resources, Course ");
        SQL.append(" WHERE res_id = cos_res_id ");
        if(course_id > 0)
            SQL.append(" AND res_id = ? ");
        SQL.append(" AND cos_eff_end_datetime >= ? ");
        SQL.append(" AND cos_eff_start_datetime <= ? ");
        if(owner_id != null && owner_id.length()>0 )
            SQL.append(" AND res_usr_id_owner = ? ");
        //filter out the offline course non admin
        AcResources acRes = new AcResources(con);

        SQL.append(" AND res_id IN " + res_id_list + " ORDER by cos_eff_start_datetime desc");
        try {
            PreparedStatement stmt = con.prepareStatement(SQL.toString());
            int index = 1;
            if(course_id > 0) {
                stmt.setLong(index, course_id);
                index++;
            }
            stmt.setTimestamp(index, start_datetime);
            stmt.setTimestamp(index+1, end_datetime);
            index += 2;
            if(owner_id != null && owner_id.length()>0 ) {
                stmt.setString(index, owner_id);
                index++;
            }


            ResultSet rs = stmt.executeQuery();
            Vector modList = new Vector();
            long RID, RSUBNBR, RMUL;
            int RORDER;
            String RLAN, RTYPE, RSUBTYPE, RTITLE, RPRIV, ROWNER, RSTATUS, RATTEMPTED;
            Timestamp RTIMESTAMP, ASS_DUEDATE;
            Timestamp EFF_START, EFF_END;
            String S_EFF_END;
            int RDIFF;
            float RDUR;
            float MMAXSCORE;
            int MPASSSCORE;
            Timestamp PSTART, PCOMPLETE, PLASTACC;
            String MINSTRUCT;
            String RDESC;
            long PATTEMPTNBR;
            String RISTNAME, RISTORG, RTPLNAME;
            dbEvent dbevt;
            String evtBody;
            dbProgress pgr;
            // for aicc only
            String MOD_WEB_LAUNCH;
            String RES_SRC_TYPE;
            String RES_SRC_LINK;

            StringBuffer SUB_SQL = new StringBuffer(1024);
            StringBuffer result = new StringBuffer(1024);
            result.append("<?xml version=\"1.0\" encoding=\"" + dbUtils.ENC_UTF + "\" standalone=\"no\" ?>" + dbUtils.NEWL);
            result.append("<resource_content>" + dbUtils.NEWL);
            result.append(prof.asXML() + dbUtils.NEWL);
            result.append("<cal ");
            result.append("start_datetime=\"").append(start_datetime).append("\" ");
            result.append("end_datetime=\"").append(end_datetime).append("\" ");
            result.append("/>").append(dbUtils.NEWL);
            String timestampNull = cwSQL.get_null_sql(cwSQL.COL_TYPE_TIMESTAMP);
            StringBuffer sql_cos_id_lst = new StringBuffer(512);
            sql_cos_id_lst.append("(0");
            //get the course as sql list that has recource permission and within the effective dates
            while(rs.next()) {
                cos_res_id = rs.getLong("ID");
                String cos_title = rs.getString("TITLE");
                if( course_id > 0 ) {
                    result.append("<course id=\"").append(course_id).append("\" ");
                    result.append("title=\"").append(dbUtils.esc4XML(course_title)).append("\"/>").append(dbUtils.NEWL);
                }
                sql_cos_id_lst.append(",").append(cos_res_id);
            }
            sql_cos_id_lst.append(")");
            stmt.close();
            //Get all modules belong to courses in sql_cos_id_lst
            SUB_SQL.append(SUB_SQL_IN_getAllCosModAsXML);
            SUB_SQL.append(timestampNull + " ASS_DUEDATE ," + timestampNull + " EVT_DATETIME ");
            SUB_SQL.append(" FROM Resources, ResourceContent, Module ");
            SUB_SQL.append(" WHERE res_id = rcn_res_id_content ");
            SUB_SQL.append(" AND rcn_res_id in ").append(sql_cos_id_lst.toString());
            SUB_SQL.append(" AND res_id = mod_res_id ");
            SUB_SQL.append(" AND ( mod_type = 'VCR' OR mod_type = 'FOR' ");
            SUB_SQL.append(" OR mod_type = 'CHT' OR mod_type = 'FAQ') ");
            if(instructor_id != null && instructor_id.length() > 0)
                SUB_SQL.append(" AND mod_usr_id_instructor = ? ");
            if(owner_id != null && owner_id.length() > 0)
                SUB_SQL.append(" AND res_usr_id_owner = ? ");
            if(start_datetime !=null && end_datetime != null) {
                SUB_SQL.append(" AND mod_eff_start_datetime >= ? ");
                SUB_SQL.append(" AND mod_eff_start_datetime <= ? ");
            }
            else if(start_datetime != null)
                SUB_SQL.append(" AND mod_eff_start_datetime >= ? ");
            else if(end_datetime != null)
                SUB_SQL.append(" AND mod_eff_start_datetime <= ? ");

            SUB_SQL.append(" UNION ");
            // Get Ass belong to this course
            SUB_SQL.append(SUB_SQL_IN_getAllCosModAsXML);
            SUB_SQL.append(" ass_due_datetime ASS_DUEDATE ," + timestampNull + " EVT_DATETIME ");
            SUB_SQL.append(" FROM Resources, ResourceContent, Module, Assignment ");
            SUB_SQL.append(" WHERE res_id = rcn_res_id_content ");
            SUB_SQL.append(" AND rcn_res_id in ").append(sql_cos_id_lst.toString());
            SUB_SQL.append(" AND res_id = mod_res_id ");
            SUB_SQL.append(" AND res_id = ass_res_id ");
            if(instructor_id != null && instructor_id.length() > 0)
                SUB_SQL.append(" AND mod_usr_id_instructor = ? ");
            if(owner_id != null && owner_id.length() > 0)
                SUB_SQL.append(" AND res_usr_id_owner = ? ");
            if(start_datetime !=null && end_datetime != null) {
                SUB_SQL.append(" AND ass_due_datetime >= ? ");
                SUB_SQL.append(" AND ass_due_datetime <= ? ");
            }
            else if(start_datetime != null)
                SUB_SQL.append(" AND ass_due_datetime >= ? ");
            else if(end_datetime != null)
                SUB_SQL.append(" AND ass_due_datetime <= ? ");
            SUB_SQL.append(" UNION ");
            // Get Events belong to this course
            SUB_SQL.append(SUB_SQL_IN_getAllCosModAsXML);
            SUB_SQL.append(timestampNull + " ASS_DUEDATE , evt_datetime EVT_DATETIME ");
            SUB_SQL.append(" FROM Resources, ResourceContent, Module, Event ");
            SUB_SQL.append(" WHERE res_id = rcn_res_id_content ");
            SUB_SQL.append(" AND rcn_res_id in ").append(sql_cos_id_lst.toString());
            SUB_SQL.append(" AND res_id = mod_res_id ");
            SUB_SQL.append(" AND res_id = evt_res_id ");
            if(instructor_id != null && instructor_id.length() > 0)
                SUB_SQL.append(" AND mod_usr_id_instructor = ? ");
            if(owner_id != null && owner_id.length() > 0)
                SUB_SQL.append(" AND res_usr_id_owner = ? ");
            if(start_datetime !=null && end_datetime != null) {
                SUB_SQL.append(" AND evt_datetime >= ? ");
                SUB_SQL.append(" AND evt_datetime <= ? ");
            }
            else if(start_datetime != null)
                SUB_SQL.append(" AND evt_datetime >= ? ");
            else if(end_datetime != null)
                SUB_SQL.append(" AND evt_datetime <= ? ");
            SUB_SQL.append(" ORDER BY RID ASC ");
            PreparedStatement sub_stmt = con.prepareStatement(SUB_SQL.toString());
            index = 1;
            for(int j=0; j<3; j++) {
                if(instructor_id != null && instructor_id.length() > 0) {
                    sub_stmt.setString(index, instructor_id);
                    index++;
                }
                if(owner_id != null && owner_id.length() > 0) {
                    sub_stmt.setString(index, owner_id);
                    index++;
                }
                if(start_datetime !=null && end_datetime != null) {
                    sub_stmt.setTimestamp(index, start_datetime);
                    sub_stmt.setTimestamp(index+1, end_datetime);
                    index += 2;
                }
                else if(start_datetime != null) {
                    sub_stmt.setTimestamp(index, start_datetime);
                    index++;
                }
                else if(end_datetime != null) {
                    sub_stmt.setTimestamp(index, end_datetime);
                    index++;
                }
            }
            ResultSet sub_rs = sub_stmt.executeQuery();
            while(sub_rs.next()) {
                RID = sub_rs.getLong("RID");
                cos_res_id = sub_rs.getLong("COS_ID");
                RTYPE = sub_rs.getString("RTYPE");
                RSUBTYPE = sub_rs.getString("RSUBTYPE");
                RORDER = sub_rs.getInt("RORDER");
                RLAN = sub_rs.getString("RLAN");
                RTITLE = dbUtils.esc4XML(sub_rs.getString("RTITLE"));
                RSUBNBR = sub_rs.getLong("RSUBNBR");
                RPRIV = sub_rs.getString("RPRIV");
                ROWNER = sub_rs.getString("ROWNER");
                RSTATUS = sub_rs.getString("RSTATUS");
                RTIMESTAMP = sub_rs.getTimestamp("RTIMESTAMP");
                RMUL   = sub_rs.getLong("RMUL");
                RDIFF = sub_rs.getInt("RDIFF");
                RDUR = sub_rs.getFloat("RDUR");
                RISTNAME = dbUtils.esc4XML(sub_rs.getString("RISTNAME"));
                RISTORG = dbUtils.esc4XML(sub_rs.getString("RISTORG"));
                RTPLNAME = sub_rs.getString("RTPLNAME");
                MMAXSCORE = sub_rs.getFloat("MMAXSCORE");
                MPASSSCORE = new Float(sub_rs.getFloat("MPASSSCORE")).intValue();
                MINSTRUCT = dbUtils.esc4XML(sub_rs.getString("MINSTRUCT"));
                ASS_DUEDATE = sub_rs.getTimestamp("ASS_DUEDATE");
                EFF_START = sub_rs.getTimestamp("EFF_START");
                EFF_END = sub_rs.getTimestamp("EFF_END");
                if(EFF_END != null)
                    if(dbUtils.isMaxTimestamp(EFF_END) == true)
                        S_EFF_END = dbUtils.UNLIMITED; //convert to String to "UNLIMITED"
                    else
                        S_EFF_END = EFF_END.toString();
                else
                    S_EFF_END = "";
                result.append("<item id=\"").append(RID).append("\" ");
                result.append(" cos_id=\"").append(cos_res_id).append("\" ");
                result.append(" type=\"").append(RTYPE).append("\" ");
                result.append(" subtype=\"").append(RSUBTYPE).append("\" ");
                result.append(" order=\"" ).append(RORDER).append("\" ");
                result.append(" sub_num=\"").append(RSUBNBR).append("\" ");
                result.append(" score_multiplier=\"").append(RMUL).append("\" ");
                result.append(" privilege=\"").append(RPRIV).append("\" ");
                result.append(" status=\"").append(RSTATUS).append("\" ");
                result.append(" language=\"").append(RLAN).append("\" ");
                result.append(" difficulty=\"").append(RDIFF).append("\" ");
                result.append(" duration=\"").append(RDUR).append("\" ");
                result.append(" time_limit=\"").append(RDUR).append("\" ");
                result.append(" suggested_time=\"").append(RDUR).append("\" ");
                result.append(" cur_time=\"").append(curTime).append("\" ");
                if(RSUBTYPE.equalsIgnoreCase("ASS"))
                    result.append(" due_datetime=\"").append(ASS_DUEDATE).append("\" ");
                result.append(" eff_start_datetime=\"").append(EFF_START).append("\" ");
                result.append(" eff_end_datetime=\"").append(S_EFF_END).append("\" ");
                result.append(" max_score=\"").append(MMAXSCORE).append("\" ");
                result.append(" pass_score=\"").append(MPASSSCORE).append("\" ");
                result.append(" timestamp=\"").append(RTIMESTAMP).append("\" >");
                result.append(dbUtils.NEWL);
                result.append("<title>").append(RTITLE).append("</title>").append(dbUtils.NEWL);
                result.append("<instructor>").append(RISTNAME).append("</instructor>").append(dbUtils.NEWL);
                result.append("<organization>").append(RISTORG).append("</organization>").append(dbUtils.NEWL);
                if(dbEvent.isEventType(RSUBTYPE)) {
                    dbevt = new dbEvent();
                    dbevt.evt_res_id = RID;
                    result.append(dbevt.getEventAsXML(con)).append(dbUtils.NEWL);
                }
                result.append("</item>").append(dbUtils.NEWL);
                modList.addElement(new Long(RID));
            }
            sub_stmt.close();
            result.append("</resource_content>" + dbUtils.NEWL);
            return result.toString();
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    public String getAllCosModAsXMLNoHeader(Connection con, loginProfile prof, String dpo_view, long course_id, String owner_id, String instructor_id, Timestamp start_datetime, Timestamp end_datetime, boolean checkStatus, Vector v_cos_res_id)
        throws qdbException , cwSysMessage
    {
        String xml = getAllCosModAsXML(con, prof, dpo_view, course_id, owner_id, instructor_id, start_datetime, end_datetime, checkStatus, v_cos_res_id);
        return xml.substring(xml.indexOf("?>")+2);
    }


// Lun : get all events belongs to the course
public String getAllCosModAsXML(Connection con, loginProfile prof, String dpo_view, long course_id, String owner_id, String instructor_id, Timestamp start_datetime, Timestamp end_datetime, boolean checkStatus, Vector v_cos_res_id)
        throws qdbException, cwSysMessage
    {
        StringBuffer SQL = new StringBuffer();
        String course_title = "";

        //usr_role has not used in this function?
        /*
        String usr_role = "";
        // determine role of user
        if(prof.sysRoles.contains(dbUserGroup.USG_ROLE_TEACHER))
            usr_role = dbUserGroup.USG_ROLE_TEACHER;
        if(prof.sysRoles.contains(dbUserGroup.USG_ROLE_ADMIN))
            usr_role = dbUserGroup.USG_ROLE_ADMIN;
        if(prof.sysRoles.contains(dbUserGroup.USG_ROLE_STUDENT))
            usr_role = dbUserGroup.USG_ROLE_STUDENT;
        */
        try {
            Vector rpmVec = (v_cos_res_id == null)
                                ? dbResourcePermission.getResPermission(con, dbResource.RES_TYPE_COS, prof)
                                : v_cos_res_id;
            //Vector rpmVec = dbResourcePermission.getResPermission(con, dbResource.RES_TYPE_COS, prof);
            String res_id_list = dbResourcePermission.convertId2List(rpmVec);

            SQL.append(" SELECT res_id ID FROM Resources, Course ");
            SQL.append(" WHERE res_id = cos_res_id ");

            if(course_id > 0)
                SQL.append(" AND res_id = ? ");

            //if(start_datetime !=null && end_datetime != null) {
                SQL.append(" AND cos_eff_end_datetime >= ? ");
                SQL.append(" AND cos_eff_start_datetime <= ? ");
            //}
            /*
            else if(start_datetime != null)
                SQL.append(" AND cos_eff_start_datetime >= ? ");
            else if(end_datetime != null)
                SQL.append(" AND cos_eff_start_datetime <= ? ");
            */
            if(owner_id != null && owner_id.length()>0 )
                SQL.append(" AND res_usr_id_owner = ? ");

            //filter out the offline course non admin
            /*if(!prof.sysRoles.contains(dbUserGroup.USG_ROLE_ADMIN)) */
            if(checkStatus)
                SQL.append(" AND res_status <> ? ");

            SQL.append(" AND res_id IN " + res_id_list + " ORDER by cos_eff_start_datetime desc");

        /*try {*/
            PreparedStatement stmt = con.prepareStatement(SQL.toString());

            int index = 1;
            if(course_id > 0) {
                stmt.setLong(index, course_id);
                index++;
            }


            Timestamp cur_time = dbUtils.getTime(con);
                stmt.setTimestamp(index, cur_time);
                stmt.setTimestamp(index+1, cur_time);
                index += 2;
            //}
            /*
            if(start_datetime !=null && end_datetime != null) {


            }
            else if(start_datetime != null) {
                stmt.setTimestamp(index, start_datetime);
                index++;
            }else if(end_datetime != null) {
                stmt.setTimestamp(index, end_datetime);
                index++;
            }
            */
            if(owner_id != null && owner_id.length()>0 ) {
                stmt.setString(index, owner_id);
                index++;
            }
            /*if(!prof.sysRoles.contains(dbUserGroup.USG_ROLE_ADMIN)) { */
            if(checkStatus) {
                stmt.setString(index, RES_STATUS_OFF);
                index++;
            }

            ResultSet rs = stmt.executeQuery();

            Vector modList = new Vector();
            long RID, RSUBNBR, RMUL;
            int RORDER;
            String RLAN, RTYPE, RSUBTYPE, RTITLE, RPRIV, ROWNER, RSTATUS, RATTEMPTED;
            Timestamp RTIMESTAMP, ASS_DUEDATE;
            Timestamp EFF_START, EFF_END;
            String S_EFF_END;
            int RDIFF;
            float RDUR;
            float MMAXSCORE;
            int MPASSSCORE;
            Timestamp PSTART, PCOMPLETE, PLASTACC;
            String MINSTRUCT;
            String RDESC;
            long PATTEMPTNBR;
            String RISTNAME, RISTORG, RTPLNAME;
            dbEvent dbevt;
            String evtBody;
            dbProgress pgr;

            // for aicc only
            String MOD_WEB_LAUNCH;
            String RES_SRC_TYPE;
            String RES_SRC_LINK;

            StringBuffer SUB_SQL = new StringBuffer();
            StringBuffer result = new StringBuffer();

            result.append("<?xml version=\"1.0\" encoding=\"" + dbUtils.ENC_UTF + "\" standalone=\"no\" ?>" + dbUtils.NEWL);
            result.append("<resource_content>" + dbUtils.NEWL);
            result.append(prof.asXML() + dbUtils.NEWL);

            //result.append("<cur_time>").append(dbUtils.getTime(con)).append("</cur_time>").append(dbUtils.NEWL);

            //if(start_datetime != null || end_datetime != null) {
                result.append("<cal ");
                if(start_datetime != null)
                    result.append("start_datetime=\"").append(start_datetime).append("\" ");
                else
                    result.append("start_datetime=\"").append(dbUtils.getTime(con)).append("\" ");
                if(end_datetime != null)
                    result.append("end_datetime=\"").append(end_datetime).append("\" ");
                else
                    result.append("end_datetime=\"\" ");
                result.append("/>").append(dbUtils.NEWL);
            //}

            if( course_id > 0 ) {
                PreparedStatement cos_stmt = con.prepareStatement(GET_COS_TITLE);
                cos_stmt.setLong(1, course_id);
                ResultSet cos_rs = cos_stmt.executeQuery();
                if(cos_rs.next()) {
                    course_title = cos_rs.getString("RTITLE");
                    result.append("<course id=\"").append(course_id).append("\" ");
                    result.append("title=\"").append(dbUtils.esc4XML(course_title)).append("\"/>").append(dbUtils.NEWL);
                }
            }

            String timestampNull = cwSQL.get_null_sql(cwSQL.COL_TYPE_TIMESTAMP);

            while(rs.next()) {
                SUB_SQL.setLength(0);
                cos_res_id = rs.getLong("ID");
                //Get all modules belong to this course
                SUB_SQL.append(SUB_SQL_IN_getAllCosModAsXML);
                SUB_SQL.append(timestampNull + " ASS_DUEDATE ," + timestampNull + " EVT_DATETIME ");
//                SUB_SQL.append("res_src_type RES_SRC_TYPE ," + "res_src_link RES_SRC_LINK ," + "mod_web_launch MOD_WEB_LAUNCH ");
                SUB_SQL.append(" FROM Resources, ResourceContent, Module ");
                SUB_SQL.append(" WHERE res_id = rcn_res_id_content ");
                SUB_SQL.append(" AND rcn_res_id = ? ");
                SUB_SQL.append(" AND res_id = mod_res_id ");
                SUB_SQL.append(" AND ( mod_type = 'VCR' OR mod_type = 'FOR' ");
                SUB_SQL.append(" OR mod_type = 'CHT' OR mod_type = 'FAQ') ");
                //SUB_SQL.append(" AND mod_eff_start_datetime <= ? ");
                //SUB_SQL.append(" AND mod_eff_end_datetime >= ? ");

                if(instructor_id != null && instructor_id.length() > 0)
                    SUB_SQL.append(" AND mod_usr_id_instructor = ? ");
                if(owner_id != null && owner_id.length() > 0)
                    SUB_SQL.append(" AND res_usr_id_owner = ? ");
                if(start_datetime !=null && end_datetime != null) {
                    SUB_SQL.append(" AND mod_eff_start_datetime >= ? ");
                    SUB_SQL.append(" AND mod_eff_start_datetime <= ? ");
                }
                else if(start_datetime != null)
                    SUB_SQL.append(" AND mod_eff_start_datetime >= ? ");
                else if(end_datetime != null)
                    SUB_SQL.append(" AND mod_eff_start_datetime <= ? ");

                SUB_SQL.append(" UNION ");
                // Get Ass belong to this course
                SUB_SQL.append(SUB_SQL_IN_getAllCosModAsXML);
                SUB_SQL.append(" ass_due_datetime ASS_DUEDATE ," + timestampNull + " EVT_DATETIME ");
                SUB_SQL.append(" FROM Resources, ResourceContent, Module, Assignment ");
                SUB_SQL.append(" WHERE res_id = rcn_res_id_content ");
                SUB_SQL.append(" AND rcn_res_id = ? ");
                SUB_SQL.append(" AND res_id = mod_res_id ");
                SUB_SQL.append(" AND res_id = ass_res_id ");
                //SUB_SQL.append(" AND mod_eff_start_datetime <= ? ");
                //SUB_SQL.append(" AND mod_eff_end_datetime >= ? ");


                if(instructor_id != null && instructor_id.length() > 0)
                    SUB_SQL.append(" AND mod_usr_id_instructor = ? ");
                if(owner_id != null && owner_id.length() > 0)
                    SUB_SQL.append(" AND res_usr_id_owner = ? ");
                if(start_datetime !=null && end_datetime != null) {
                    SUB_SQL.append(" AND ass_due_datetime >= ? ");
                    SUB_SQL.append(" AND ass_due_datetime <= ? ");
                }
                else if(start_datetime != null)
                    SUB_SQL.append(" AND ass_due_datetime >= ? ");
                else if(end_datetime != null)
                    SUB_SQL.append(" AND ass_due_datetime <= ? ");

                SUB_SQL.append(" UNION ");
                // Get Events belong to this course
                SUB_SQL.append(SUB_SQL_IN_getAllCosModAsXML);
                SUB_SQL.append(timestampNull + " ASS_DUEDATE , evt_datetime EVT_DATETIME ");
                SUB_SQL.append(" FROM Resources, ResourceContent, Module, Event ");
                SUB_SQL.append(" WHERE res_id = rcn_res_id_content ");
                SUB_SQL.append(" AND rcn_res_id = ? ");
                SUB_SQL.append(" AND res_id = mod_res_id ");
                SUB_SQL.append(" AND res_id = evt_res_id ");
                //SUB_SQL.append(" AND mod_eff_start_datetime <= ? ");
                //SUB_SQL.append(" AND mod_eff_end_datetime >= ? ");


                if(instructor_id != null && instructor_id.length() > 0)
                    SUB_SQL.append(" AND mod_usr_id_instructor = ? ");
                if(owner_id != null && owner_id.length() > 0)
                    SUB_SQL.append(" AND res_usr_id_owner = ? ");
                if(start_datetime !=null && end_datetime != null) {
                    SUB_SQL.append(" AND evt_datetime >= ? ");
                    SUB_SQL.append(" AND evt_datetime <= ? ");
                }
                else if(start_datetime != null)
                    SUB_SQL.append(" AND evt_datetime >= ? ");
                else if(end_datetime != null)
                    SUB_SQL.append(" AND evt_datetime <= ? ");


                SUB_SQL.append(" ORDER BY RID ASC ");
                //System.out.println("SUB_SQL : " + SUB_SQL.toString());
                PreparedStatement sub_stmt = con.prepareStatement(SUB_SQL.toString());

                index = 1;
                for(int j=0; j<3; j++) {
                    sub_stmt.setLong(index, cos_res_id);
                    //sub_stmt.setTimestamp(index+1, cur_time);
                    //sub_stmt.setTimestamp(index+2, cur_time);
                    index++;

                    if(instructor_id != null && instructor_id.length() > 0) {
                        sub_stmt.setString(index, instructor_id);
                        index++;
                    }
                    if(owner_id != null && owner_id.length() > 0) {
                        sub_stmt.setString(index, owner_id);
                        index++;
                    }
                    if(start_datetime !=null && end_datetime != null) {
                        sub_stmt.setTimestamp(index, start_datetime);
                        sub_stmt.setTimestamp(index+1, end_datetime);
                        index += 2;
                    }
                    else if(start_datetime != null) {
                        sub_stmt.setTimestamp(index, start_datetime);
                        index++;
                    }
                    else if(end_datetime != null) {
                        sub_stmt.setTimestamp(index, end_datetime);
                        index++;
                    }
                }

                ResultSet sub_rs = sub_stmt.executeQuery();

                while(sub_rs.next()) {

                    RID = sub_rs.getLong("RID");
                    RTYPE = sub_rs.getString("RTYPE");
                    RSUBTYPE = sub_rs.getString("RSUBTYPE");
                    RORDER = sub_rs.getInt("RORDER");
                    RLAN = sub_rs.getString("RLAN");
                    RTITLE = dbUtils.esc4XML(sub_rs.getString("RTITLE"));
                    RSUBNBR = sub_rs.getLong("RSUBNBR");
                    RPRIV = sub_rs.getString("RPRIV");
                    ROWNER = sub_rs.getString("ROWNER");
                    RSTATUS = sub_rs.getString("RSTATUS");
                    RTIMESTAMP = sub_rs.getTimestamp("RTIMESTAMP");
                    RMUL   = sub_rs.getLong("RMUL");
                    RDIFF = sub_rs.getInt("RDIFF");
                    RDUR = sub_rs.getFloat("RDUR");
//                    RDESC = dbUtils.esc4XML(sub_rs.getString("RDESC"));
                    // as the field "res_desc" is nText, so cannot select from union
                    RDESC = dbUtils.esc4XML(dbResource.getResDesc(con, cos_res_id));

                    RISTNAME = dbUtils.esc4XML(sub_rs.getString("RISTNAME"));
                    RISTORG = dbUtils.esc4XML(sub_rs.getString("RISTORG"));
                    RTPLNAME = sub_rs.getString("RTPLNAME");
                    MMAXSCORE = sub_rs.getFloat("MMAXSCORE");
                    MPASSSCORE = new Float(sub_rs.getFloat("MPASSSCORE")).intValue();
                    MINSTRUCT = dbUtils.esc4XML(sub_rs.getString("MINSTRUCT"));
                    ASS_DUEDATE = sub_rs.getTimestamp("ASS_DUEDATE");
                    EFF_START = sub_rs.getTimestamp("EFF_START");
                    EFF_END = sub_rs.getTimestamp("EFF_END");
                    if(EFF_END != null)
                        if(dbUtils.isMaxTimestamp(EFF_END) == true)
                            S_EFF_END = dbUtils.UNLIMITED; //convert to String to "UNLIMITED"
                        else
                            S_EFF_END = EFF_END.toString();
                    else
                        S_EFF_END = "";

                    // for AICC only
/*
                    MOD_WEB_LAUNCH = sub_rs.getString("MOD_WEB_LAUNCH");
                    if (MOD_WEB_LAUNCH == null) {
                        MOD_WEB_LAUNCH = "";
                    }
                    RES_SRC_TYPE = sub_rs.getString("RES_SRC_TYPE");
                    if (RES_SRC_TYPE == null) {
                        RES_SRC_TYPE = "";
                    }
                    RES_SRC_LINK = sub_rs.getString("RES_SRC_LINK");
                    if (RES_SRC_LINK == null) {
                        RES_SRC_LINK = "";
                    }
*/
                    PreparedStatement stmt2 = con.prepareStatement(stmt2_IN_getAllCosModAsXML);
                    stmt2.setLong(1, RID);
                    stmt2.setLong(2, RID);
                    ResultSet rs2 = stmt2.executeQuery();
                    if(rs2.next())
                    {
                        int cnt = rs2.getInt(1);

                        if (cnt!=0)
                            RATTEMPTED = RES_ATTEMPTED_TRUE;
                        else
                            RATTEMPTED = RES_ATTEMPTED_FALSE;
                    }
                    else
                    {
                        stmt2.close();
                        sub_stmt.close();
                        stmt.close();
                        throw new qdbException("Failed to get progress record.");
                    }
                    stmt2.close();

                    PSTART = null;
                    PCOMPLETE = null;
                    PLASTACC = null;

                    pgr = new dbProgress();
                    pgr.pgr_usr_id = prof.usr_id;
                    pgr.pgr_res_id = RID;
                    PATTEMPTNBR = pgr.usrAttemptNum(con,RID,prof.usr_id);
                    if(PATTEMPTNBR > 0) {
                        pgr.get(con,1);
                        PSTART = pgr.pgr_start_datetime;

                        pgr.get(con);
                        PCOMPLETE = pgr.pgr_complete_datetime;
                        PLASTACC = pgr.pgr_last_acc_datetime;
                    }



                    result.append("<item id=\"").append(RID).append("\" ");
                    result.append(" cos_id=\"").append(cos_res_id).append("\" ");
                    result.append(" type=\"").append(RTYPE).append("\" ");
                    result.append(" subtype=\"").append(RSUBTYPE).append("\" ");
                    result.append(" order=\"" ).append(RORDER).append("\" ");
                    result.append(" sub_num=\"").append(RSUBNBR).append("\" ");
                    result.append(" score_multiplier=\"").append(RMUL).append("\" ");
                    result.append(" privilege=\"").append(RPRIV).append("\" ");
                    result.append(" status=\"").append(RSTATUS).append("\" ");
                    result.append(" owner=\"").append(dbUtils.esc4XML(dbRegUser.usrId2SteUsrId(con,ROWNER))).append("\" ");
                    result.append(" language=\"").append(RLAN).append("\" ");
                    result.append(" attempted=\"").append(RATTEMPTED).append("\" ");
                    result.append(" difficulty=\"").append(RDIFF).append("\" ");
                    result.append(" duration=\"").append(RDUR).append("\" ");
                    result.append(" time_limit=\"").append(RDUR).append("\" ");
                    result.append(" suggested_time=\"").append(RDUR).append("\" ");
                    result.append(" cur_time=\"").append(cur_time).append("\" ");
                    if(RSUBTYPE.equalsIgnoreCase("ASS"))
                        result.append(" due_datetime=\"").append(ASS_DUEDATE).append("\" ");
                    result.append(" eff_start_datetime=\"").append(EFF_START).append("\" ");
                    result.append(" eff_end_datetime=\"").append(S_EFF_END).append("\" ");
                    result.append(" max_score=\"").append(MMAXSCORE).append("\" ");
                    result.append(" pass_score=\"").append(MPASSSCORE).append("\" ");
                    result.append(" attempt_nbr=\"").append(PATTEMPTNBR).append("\" ");
                    result.append(" pgr_start=\"").append(PSTART).append("\" ");
                    result.append(" pgr_complete=\"").append(PCOMPLETE).append("\" ");
                    result.append(" pgr_last_acc=\"").append(PLASTACC).append("\" ");

                    result.append(" timestamp=\"").append(RTIMESTAMP).append("\" >");
                    result.append(dbUtils.NEWL);
                    result.append("<title>").append(RTITLE).append("</title>").append(dbUtils.NEWL);
                    result.append("<desc>").append(RDESC).append("</desc>").append(dbUtils.NEWL);
                    result.append("<instructor>").append(RISTNAME).append("</instructor>").append(dbUtils.NEWL);
                    //result.append("<moderator>").append(getOwnerName(con)).append("</moderator>").append(dbUtils.NEWL);
                    result.append("<organization>").append(RISTORG).append("</organization>").append(dbUtils.NEWL);

                    if (RSUBTYPE != null && RSUBTYPE.equalsIgnoreCase("FOR")) {
                        long[] topicLst = dbForumTopic.getAllTopicIDs(con, RID, null, null);
                        long numMsg = 0;
                        long numRead = 0;

                        if (topicLst != null && topicLst.length != 0) {
                            for (int i=0; i<topicLst.length; i++) {
                                numMsg += dbForumMessage.numOfMsgFromTopic(con, topicLst[i]);
                                numRead += dbForumMarkMsg.numOfReadMsgFromTopic(con, topicLst[i], prof.usr_id);
                            }

                            result.append("<forum num_of_topic=\"").append(topicLst.length).append("\" ");
                            result.append(" num_of_msg=\"").append(numMsg).append("\" ");
                            result.append(" num_of_read=\"").append(numRead).append("\" />").append(dbUtils.NEWL);
                        }
                    }
                    if(dbEvent.isEventType(RSUBTYPE)) {
                        dbevt = new dbEvent();
                        dbevt.evt_res_id = RID;
                        result.append(dbevt.getEventAsXML(con)).append(dbUtils.NEWL);
                    }
                    result.append("<instruct>").append(MINSTRUCT).append("</instruct>").append(dbUtils.NEWL);
                    if(RTPLNAME != null && RTPLNAME.trim().length() > 0) {
                        dbTemplate dbtpl = new dbTemplate();
                        dbtpl.tpl_name = RTPLNAME;
                        dbtpl.tpl_lan = prof.label_lan;
                        dbtpl.get(con);
                        result.append("<stylesheet>").append(dbtpl.tpl_stylesheet).append("</stylesheet>");
                    }

                    dbDisplayOption dpo = new dbDisplayOption();
                    dpo.dpo_res_id = RID;
                    dpo.dpo_res_type = RTYPE;
                    dpo.dpo_res_subtype = RSUBTYPE;
                    dpo.dpo_view = dpo_view;
                    result.append(dpo.getViewAsXML(con));

                    result.append("</item>").append(dbUtils.NEWL);
                    modList.addElement(new Long(RID));
                }


                sub_stmt.close();
            }
            stmt.close();
            result.append(dbResourcePermission.aclAsXML(con, modList, prof));
            result.append("</resource_content>" + dbUtils.NEWL);

            return result.toString();

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        } catch(qdbErrMessage ee) {
            throw new qdbException(ee.getMessage());
        }
    }
    // deprecated, tracking history not supported
    /*
    public String getLastVisitModAsXMLNoHeader(Connection con, loginProfile prof, String dpo_view, long course_id, int num_of_mod, boolean checkStatus, Vector v_cos_res_id)
        throws qdbException{
        return getLastVisitModAsXMLNoHeader(con, prof, dpo_view, course_id, num_of_mod, checkStatus, v_cos_res_id, 0);
    }
    */

    public String getLastVisitModAsXMLNoHeader(Connection con, loginProfile prof, String dpo_view, long course_id, int num_of_mod, boolean checkStatus, Vector v_cos_res_id)
        throws qdbException, cwException
    {
        String xml = getLastVisitModAsXML(con, prof, dpo_view, course_id, num_of_mod, checkStatus, v_cos_res_id);
        return xml.substring(xml.indexOf("?>")+2);
    }
    // deprecated, tracking history not supported
    /*
    public String getLastVisitModAsXML(Connection con, loginProfile prof, String dpo_view, long course_id, int num_of_mod, boolean checkStatus, Vector v_cos_res_id)
        throws qdbException
    {
        return getLastVisitModAsXML(con, prof, dpo_view, course_id, num_of_mod, checkStatus, v_cos_res_id, 0);
    }
    */

    // Lun : get last visit modules
    public String getLastVisitModAsXML(Connection con, loginProfile prof, String dpo_view, long course_id, int num_of_mod, boolean checkStatus, Vector v_cos_res_id)
        throws qdbException, cwException
    {
        try{
            if (tkh_id == DbTrackingHistory.TKH_ID_UNDEFINED){
                if (course_id==0){
                    tkh_id = DbTrackingHistory.TKH_ID_NO_COURSE;
                }else{
                	CommonLog.info("!!!!!!get tracking id in dbCourse getLastVisitModAsXML");
                    tkh_id = DbTrackingHistory.getAppTrackingIDByCos(con, course_id, prof.usr_ent_id);
                }
            }
            Vector rpmVec = (v_cos_res_id == null)
                                ? dbResourcePermission.getResPermission(con, dbResource.RES_TYPE_COS, prof)
                                : v_cos_res_id;
            String res_id_list = dbResourcePermission.convertId2List(rpmVec);

            String SQL = OuterJoinSqlStatements.getNavCentre(res_id_list, course_id, checkStatus, tkh_id);
            Timestamp currentTime = dbUtils.getTime(con);
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setString(1, prof.label_lan);
            stmt.setTimestamp(2,currentTime);
            stmt.setTimestamp(3,currentTime);
            stmt.setLong(4,prof.usr_ent_id);
            int index = 5;
            if(course_id > 0) {
                stmt.setLong(index++, course_id);
                stmt.setLong(index++, tkh_id);
            }
            if(checkStatus)
                stmt.setString(index++, RES_STATUS_OFF);

            ResultSet rs = stmt.executeQuery();

            long cos_id;
            String cos_title;
            long mod_id;
            String mod_title;
            Timestamp acc_time;
            String mod_type;
            String rtplname;
            String rtplstylesheet;
            int count = 0;

            // for AICC only
            String mod_web_launch;
            String res_src_type;
            String res_src_link;

            StringBuffer result = new StringBuffer();

            result.append("<?xml version=\"1.0\" encoding=\"" + dbUtils.ENC_UTF + "\" standalone=\"no\" ?>" + dbUtils.NEWL);
            result.append("<resource_content>" + dbUtils.NEWL);
            result.append(prof.asXML() + dbUtils.NEWL);

            while(rs.next() && count<num_of_mod) {
                count++;
                cos_id = rs.getLong("COSID");
                cos_title = rs.getString("CTITLE");
                mod_id = rs.getLong("MODID");
                mod_title = rs.getString("MTITLE");
                mod_type = rs.getString("MTYPE");
                acc_time = rs.getTimestamp("ACCTIME");
                rtplname = rs.getString("RTPLNAME");
                rtplstylesheet = rs.getString("RTPLSTYLESHEET");

                // for AICC only
                mod_web_launch = rs.getString("MWEBLAUNCH");
                if (mod_web_launch == null) {
                    mod_web_launch = "";
                }
                res_src_type = rs.getString("MSRCTYPE");
                if (res_src_type == null) {
                    res_src_type = "";
                }
                res_src_link = rs.getString("MSRCLINK");
                if (res_src_link == null) {
                    res_src_link = "";
                }

                result.append(" <item mod_id=\"").append(mod_id).append("\" ");
                result.append(" mod_title=\"").append(dbUtils.esc4XML(mod_title)).append("\" ");
                result.append(" mod_type=\"").append(mod_type).append("\" ");
                result.append(" cos_id=\"").append(cos_id).append("\" ");
                result.append(" cos_title=\"").append(dbUtils.esc4XML(cos_title)).append("\" ");
                result.append(" last_acc_time=\"").append(acc_time).append("\" ");

                // for AICC only
                result.append(" mod_web_launch=\"").append(dbUtils.esc4XML(mod_web_launch)).append("\" ");
                result.append(" res_src_type=\"").append(res_src_type).append("\" ");
                result.append(" res_src_link=\"").append(dbUtils.esc4XML(res_src_link)).append("\" ");

                if (course_id>0 && tkh_id>0){
                    result.append(" tkh_id=\"").append(tkh_id).append("\" ");
                }
                if(rtplname != null && rtplname.trim().length() > 0) {
                    result.append(">").append(dbUtils.NEWL);
                    result.append("<stylesheet>").append(rtplstylesheet).append("</stylesheet>");
                    result.append(dbUtils.NEWL).append("</item>");
                }
                else
                    result.append("/>").append(dbUtils.NEWL);
            }
            result.append("</resource_content>" + dbUtils.NEWL);
            stmt.close();
            return result.toString();

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    public String getModBuilderXML(Connection con, loginProfile prof, int count, int current, String[] types)
        throws qdbException
    {
         // format xml
        StringBuffer  result = new StringBuffer();
        result.append(dbUtils.xmlHeader);
        result.append("<module_builder>").append(dbUtils.NEWL);
        // author's information
        result.append(prof.asXML());

        // get system time
        result.append("<cur_time>").append(dbUtils.getTime(con)).append("</cur_time>");
        result.append("<course id=\"").append(cos_res_id).append("\" timestamp=\"").append(res_upd_date).append("\"/>").append(dbUtils.NEWL);
        //result.append("<module count=\"").append(count).append("\" current=\"").append(current).append("\">").append(dbUtils.NEWL);

        StringBuffer typeList = new StringBuffer();
        if (types !=null) {
            for (int i=0;i<types.length;i++) {
                if (types[i] !=null && types[i].length() > 0) {
                    typeList.append("<type>").append(types[i]).append("</type>").append(dbUtils.NEWL);
                }
            }
        }


        for (int i=1;i<=count;i++) {
            result.append("<lesson");
            if (current == i)
                result.append(" current=\"true\"");
            result.append(">").append(dbUtils.NEWL);
            result.append("<module>").append(dbUtils.NEWL);
            result.append(typeList);
            result.append("</module>").append(dbUtils.NEWL);
            result.append("</lesson>").append(dbUtils.NEWL);
        }
        result.append("</module_builder>");


        return result.toString();
    }

    // hightable??
    // assume first element, must be fdr and other are "MOD"
    public String buildCosStructXML(Hashtable ht){
        Vector idref = (Vector) ht.get("IDREF");
        Vector title = (Vector) ht.get("TITLE");
        Vector type = (Vector) ht.get("TYPE");

        String item_prefix = "ITEM";
        String itemtype = "";
        StringBuffer xml_buf = new StringBuffer();

        boolean in_fdr = false;
        xml_buf.append("<tableofcontents identifier=\"" + "" + "\" title=\"" + "" + "\" >");

        for (int i=0; i<idref.size(); i++){
            long idref_ = ((Long) idref.elementAt(i)).longValue();
            String title_ = (String) title.elementAt(i);
            String type_ = (String) type.elementAt(i);

            if (type_.equals(ITEM_TYPE_FDR)){
                if (in_fdr){
                    xml_buf.append("</item>").append(dbUtils.NEWL);
                }
                itemtype = type_;
                xml_buf.append("<item identifier=\"" + item_prefix + i + "\" title=\"" + dbUtils.esc4XML(title_) + "\" >");
                xml_buf.append("<itemtype>").append(itemtype).append("</itemtype>").append(dbUtils.NEWL);
                in_fdr = true;
            }
            else{
                itemtype = "MOD";
                xml_buf.append("<item identifier=\"" + item_prefix + i + "\" identifierref=\"" + idref_ + "\" title=\"" + dbUtils.esc4XML(title_) + "\" >");
                xml_buf.append("<itemtype>").append(itemtype).append("</itemtype>").append(dbUtils.NEWL);
                xml_buf.append("<restype>").append(type_).append("</restype>").append(dbUtils.NEWL);
                xml_buf.append("</item>").append(dbUtils.NEWL);
            }
        }
        if (in_fdr){
            xml_buf.append("</item>").append(dbUtils.NEWL);
        }
        xml_buf.append("</tableofcontents>").append(dbUtils.NEWL);

        return xml_buf.toString();
    }

    public void saveWizard(Connection con, loginProfile prof, Hashtable data, qdbEnv env)
        throws qdbException, qdbErrMessage, cwException
    {
        Vector typeVec = (Vector)data.get("TYPE");
        Vector titleVec = (Vector)data.get("TITLE");
        Vector descVec = (Vector)data.get("DESC");
        Vector startVec = (Vector)data.get("START");
        Vector endVec = (Vector)data.get("END");
        Vector idVec = new Vector();

        String type_;
        String title_;
        String desc_;
        Timestamp start_;
        Timestamp end_;

        super.lock(con);

        super.checkTimeStamp(con);

        for (int i=0;i<typeVec.size();i++) {

            dbModule dbmod = new dbModule();
            dbmod.mod_type = (String) typeVec.elementAt(i);
            dbmod.res_lan = prof.label_lan;
            dbmod.res_type = RES_TYPE_MOD;
            dbmod.res_status = dbResource.RES_STATUS_OFF;
            dbmod.res_subtype = (String) typeVec.elementAt(i);
            long modId = 0;
            dbmod.res_tpl_name = dbTemplate.getOneTpl(con, prof, dbmod.res_subtype);

            if (!dbmod.res_subtype.equalsIgnoreCase(ITEM_TYPE_FDR)) {

                dbmod.res_title = (String) titleVec.elementAt(i);
                dbmod.res_desc = (String) descVec.elementAt(i);
                dbmod.mod_in_eff_start_datetime = (Timestamp) startVec.elementAt(i);
                dbmod.mod_in_eff_end_datetime = (Timestamp) endVec.elementAt(i);

                if ((dbmod.mod_type).equalsIgnoreCase("ASS")) {
                    dbAssignment dbass = new dbAssignment();
                    dbass.initialize(dbmod);
                    dbass.RES_FOLDER = qdbEnv.RES_FOLDER;
                    dbass.bFileUpload = false;
                    //dob.dbass.INI_DIR_UPLOAD = dob.cur_env.INI_DIR_UPLOAD;
                    //dob.dbass.INI_MAIL_SERVER = dob.cur_env. INI_MAIL_SERVER;
                    //dob.dbass.INI_NOTIFY_ASSIGNMENT_SUCCESS_TPL = dob.cur_env.INI_NOTIFY_ASSIGNMENT_SUCCESS_TPL;
                    //dob.dbass.tmpUploadDir = dob.cur_env.INI_DIR_UPLOAD_TMP + dbUtils.SLASH + sess.getId();

                    dbass.res_upd_user = prof.usr_id;
                    dbass.res_usr_id_owner = prof.usr_id;

                    modId = insAssignment(con, dbass, null, prof, null, null);

                }
                else if(dbEvent.isEventType(dbmod.mod_type)) {
                    dbEvent dbevt = new dbEvent();

                    dbevt.initialize(dbmod);

                    dbevt.res_upd_user = prof.usr_id;
                    dbevt.res_usr_id_owner = prof.usr_id;

                    modId = insEvent(con, dbevt, null, prof);
                }
                else if ((dbmod.mod_type).equalsIgnoreCase("CHT") || (dbmod.mod_type).equalsIgnoreCase("VCR") ) {

                    dbChat dbchat = new dbChat();
                    dbchat.initialize(dbmod, env.TSHOST, env.TSPORT, env.ROOMPORT, env.WWWPORT);

                    dbchat.res_upd_user = prof.usr_id;
                    dbchat.res_usr_id_owner = prof.usr_id;

                    modId = insChat(con, dbchat, null, prof);
                }
                else {
                    dbmod.res_upd_user = prof.usr_id;
                    dbmod.res_usr_id_owner = prof.usr_id;

                    modId = insModule(con, dbmod, null, prof);

                }

            }

            idVec.addElement(new Long(modId));
        }

        Hashtable struct = new Hashtable();
        struct.put("IDREF", idVec);
        struct.put("TYPE", typeVec);
        struct.put("TITLE", titleVec);

        cos_structure_xml = buildCosStructXML(struct);
        updCosStructureNoTimestampCheck(con, prof);

    }

    // Get the list of Instructor from a Course
    public static String getInstructorList(Connection con, long cosId, String instructorId, long root_ent_id)
        throws qdbException
    {
        try {
            StringBuffer xml = new StringBuffer();
                String SQL = " SELECT usr_id , usr_ent_id, usr_display_bil  "
                            + " FROM RegUser, Course, aeItemAccess WHERE "
                            + "     usr_ent_id = iac_ent_id "
                            + " AND iac_itm_id = cos_itm_id "
                            + " AND iac_access_type = ? "
                            + " AND iac_access_id ='INSTR_1' "
                            + " AND cos_res_id = ? ";

                PreparedStatement stmt = con.prepareStatement(SQL);

                stmt.setString(1,dbRegUser.ROLE_TYPE);
                stmt.setLong(2,cosId);

                xml.append("<instructor_list>").append(dbUtils.NEWL);
                ResultSet rs = stmt.executeQuery();
                while( rs.next()) {
                    xml.append("<user id=\"").append(rs.getString("usr_id"))
                    .append("\" ent_id=\"").append(rs.getLong("usr_ent_id"))
                    .append("\" display=\"").append(dbUtils.esc4XML(rs.getString("usr_display_bil")))
                    .append("\"");

                    if (instructorId !=null && rs.getString("usr_id").equals(instructorId)) {
                        xml.append(" selected=\"true\"");
                    }

                    xml.append("/>").append(dbUtils.NEWL);
                }
                xml.append("</instructor_list>").append(dbUtils.NEWL);
                stmt.close();
            return xml.toString();

        }catch (SQLException e) {
            throw new qdbException ("SQL Error: " + e.getMessage());
        }
    }

    public static void getAllEnrolledModIds(Connection con, loginProfile prof, Vector modIdVec, Vector assIdVec, Vector easIdVec)
        throws qdbException
    {
        try{
            String entList = dbUtils.vec2list(prof.usrGroups);
            String SQL =
                  " SELECT distinct(rcn_res_id_content) AS MOD_ID, res_subtype FROM ResourceContent, Resources "
                + " WHERE rcn_res_id IN "
                + " (SELECT enr_res_id FROM Enrolment WHERE "
                + "      enr_status = ? AND ("
                + "      enr_ent_id IN " + entList + " OR enr_ent_id = ? OR enr_ent_id = ?)) "
                + "  AND res_id = rcn_res_id_content ORDER BY MOD_ID ";

            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setString(1, dbCourse.COS_ENROLL_OK);
            stmt.setLong(2, dbResourcePermission.SHARE_RES);
            stmt.setLong(3, prof.usr_ent_id);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                if (rs.getString("res_subtype").equals("ASS")) {
                    assIdVec.addElement(new Long(rs.getString("MOD_ID")));
                } else if (rs.getString("res_subtype").equals("EAS")) {
                    easIdVec.addElement(new Long(rs.getString("MOD_ID")));
                } else {
                    modIdVec.addElement(new Long(rs.getString("MOD_ID")));
                }
            }
            stmt.close();

        }catch (SQLException e) {
            throw new qdbException ("SQL Error: " + e.getMessage());
        }
    }

    public static void getAllQuickReferenceModIds(Connection con, long site_id, Vector modIdVec, Vector assIdVec, Vector easIdVec)
        throws qdbException
    {
        try{
            String[] canQRTypes = acSite.getQRTypes(con, site_id);
            if (canQRTypes.length ==0) {
                return;
            }

            String SQL =
                  " SELECT distinct(rcn_res_id_content) AS MOD_ID, res_subtype FROM ResourceContent, Resources, Course, aeItem "
                + " WHERE rcn_res_id = cos_res_id "
                + " AND cos_itm_id = itm_id "
                + " AND itm_can_qr_ind = ? "
                + " AND rcn_res_id_content = res_id "
                + " AND res_type = ? and res_subtype IN "
                + dbUtils.array2SQLList(canQRTypes);

            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setBoolean(1, true);
            stmt.setString(2, dbResource.RES_TYPE_MOD);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                Long modID = new Long(rs.getLong("MOD_ID"));
                String modType = rs.getString("res_subtype");
                if (modType.equalsIgnoreCase(dbModule.MOD_TYPE_ASS)) {
                    if (!assIdVec.contains(modID)) {
                        assIdVec.addElement(modID);
                    }
                }else if (modType.equalsIgnoreCase(dbModule.MOD_TYPE_EAS)) {
                    if (!easIdVec.contains(modID)) {
                        easIdVec.addElement(modID);
                    }
                }else {
                    if (!modIdVec.contains(modID)) {
                        modIdVec.addElement(modID);
                    }
                }
            }
            stmt.close();

        }catch (SQLException e) {
            throw new qdbException ("SQL Error: " + e.getMessage());
        }
    }

    public static void getAccessibleModIds(Connection con, loginProfile prof, Vector modIdVec, Vector assIdVec, Vector easIdVec)
        throws qdbException
    {
        try{
            String[] canQRTypes = acSite.getQRTypes(con, prof.root_ent_id);

            /*String entList = dbUtils.vec2list(prof.usrGroups);*/
            StringBuffer SQLBuf = new StringBuffer(256);
            SQLBuf.append(" SELECT distinct(rcn_res_id_content) AS MOD_ID, res_subtype ")
                  .append(" FROM ResourceContent, Resources, Enrolment ")
                  .append(" WHERE rcn_res_id = enr_res_id ")
                  .append(" AND enr_status = '").append(COS_ENROLL_OK).append("'")
                  .append(" AND (enr_ent_id = ? OR enr_ent_id = ?) ")
                  .append(" AND res_id = rcn_res_id_content ");

            if (canQRTypes.length > 0) {
                  SQLBuf.append(" Union ");
                  SQLBuf.append(" SELECT distinct(rcn_res_id_content) AS MOD_ID, res_subtype ")
                        .append(" FROM ResourceContent, Resources, Course, aeItem ")
                        .append(" WHERE rcn_res_id = cos_res_id ")
                        .append(" AND cos_itm_id = itm_id ")
                        .append(" AND itm_can_qr_ind = ? ")
                        .append(" AND rcn_res_id_content = res_id ")
                        .append(" AND res_type = '").append(dbResource.RES_TYPE_MOD).append("'")
                        .append(" AND res_subtype IN ").append(dbUtils.array2SQLList(canQRTypes));
            }
            PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
            stmt.setLong(1, dbResourcePermission.SHARE_RES);
            stmt.setLong(2, prof.usr_ent_id);
            if(canQRTypes.length > 0) {
                stmt.setBoolean(3, true);
            }

            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                if (rs.getString("res_subtype").equals(dbModule.MOD_TYPE_ASS)) {
                    assIdVec.addElement(new Long(rs.getString("MOD_ID")));
                } else if (rs.getString("res_subtype").equals(dbModule.MOD_TYPE_EAS)) {
                    easIdVec.addElement(new Long(rs.getString("MOD_ID")));
                } else {
                    modIdVec.addElement(new Long(rs.getString("MOD_ID")));
                }
            }
            stmt.close();

        }catch (SQLException e) {
            throw new qdbException ("SQL Error: " + e.getMessage());
        }
    }

    public static void getAllEnrolledModIds(Connection con, loginProfile prof, Vector modIdVec, Vector assIdVec)
        throws qdbException
    {
        try{
            String entList = dbUtils.vec2list(prof.usrGroups);
            String SQL =
                  " SELECT distinct(rcn_res_id_content) AS MOD_ID, res_subtype FROM ResourceContent, Resources "
                + " WHERE rcn_res_id IN "
                + " (SELECT enr_res_id FROM Enrolment WHERE "
                + "      enr_status = ? AND ("
                + "      enr_ent_id IN " + entList + " OR enr_ent_id = ? OR enr_ent_id = ?)) "
                + "  AND res_id = rcn_res_id_content ORDER BY MOD_ID ";

            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setString(1, dbCourse.COS_ENROLL_OK);
            stmt.setLong(2, dbResourcePermission.SHARE_RES);
            stmt.setLong(3, prof.usr_ent_id);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                if (rs.getString("res_subtype").equals("ASS")) {
                    assIdVec.addElement(new Long(rs.getString("MOD_ID")));
                } else {
                    modIdVec.addElement(new Long(rs.getString("MOD_ID")));
                }
            }
            stmt.close();

        }catch (SQLException e) {
            throw new qdbException ("SQL Error: " + e.getMessage());
        }
    }

    public void getCosHeader(Connection con) throws SQLException {

        final String SQL = " Select res_upd_date From Resources "
                        + " Where res_id = ? ";

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, cos_res_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next())
            res_upd_date = rs.getTimestamp("res_upd_date");
        else
        {
            stmt.close();
            throw new SQLException("Cannot find course. cos_res_id = " + res_id);
        }

        stmt.close();
    }


    // for export AICC only
    // cliff, 2001/4/16
    public String getExportSystemID(String oldSystemID, Hashtable htExportSystemID, Hashtable htCurrCnt) {
        String exportSystemID = null;
        if (htExportSystemID.get(oldSystemID) != null) {
            exportSystemID = (String)htExportSystemID.get(oldSystemID);
        }
        else {
            if (oldSystemID.toLowerCase().startsWith("a") == true) {
                Integer temp = (Integer)htCurrCnt.get("au");
                int curAuCnt = temp.intValue();
                htExportSystemID.put(oldSystemID, "A" + Integer.toString(curAuCnt));
                exportSystemID = "A" + Integer.toString(curAuCnt);
                curAuCnt++;
                htCurrCnt.put("au", new Integer(curAuCnt));
            }
            else if (oldSystemID.toLowerCase().startsWith("b") == true) {
                Integer temp = (Integer)htCurrCnt.get("block");
                int curBkCnt = temp.intValue();
                htExportSystemID.put(oldSystemID, "B" + Integer.toString(curBkCnt));
                exportSystemID = "B" + Integer.toString(curBkCnt);
                curBkCnt++;
                htCurrCnt.put("block", new Integer(curBkCnt));
            }
            else if (oldSystemID.toLowerCase().startsWith("j") == true) {
                Integer temp = (Integer)htCurrCnt.get("objective");
                int curObjCnt = temp.intValue();
                htExportSystemID.put(oldSystemID, "J" + Integer.toString(curObjCnt));
                exportSystemID = "J" + Integer.toString(curObjCnt);
                curObjCnt++;
                htCurrCnt.put("objective", new Integer(curObjCnt));
            }
            else if (oldSystemID.toLowerCase().startsWith("root") == true) {
                exportSystemID = oldSystemID;
            }
        }
        return exportSystemID;
    }

    // for export AICC course
    // by cliff, 2001/4/24
    public String exportAiccCos(Connection con, String domain, loginProfile prof, String tmpUploadDir)
        throws IOException, SQLException, qdbException, qdbErrMessage, cwSysMessage {
        String cos_import_xml = null;
        String mod_import_xml = null;
        String obj_import_xml = null;

        String crsFileName = "aicc_course" + Long.toString(cos_res_id) + ".crs";
        String cstFileName = "aicc_course" + Long.toString(cos_res_id) + ".cst";
        String desFileName = "aicc_course" + Long.toString(cos_res_id) + ".des";
        String auFileName = "aicc_course" + Long.toString(cos_res_id) + ".au";
        String ortFileName = "aicc_course" + Long.toString(cos_res_id) + ".ort";

        String zipFileName = "aicc_course" + Long.toString(cos_res_id) + ".zip";

        Hashtable htExportSystemID = new Hashtable();
        Hashtable htCurrCnt = new Hashtable();
        htCurrCnt.put("au", new Integer(1));
        htCurrCnt.put("block", new Integer(1));
        htCurrCnt.put("objective", new Integer(1));

        cos_import_xml = getCosImportXML(con);
        aiccImportXMLParser cosImportParser = new aiccImportXMLParser(aiccImportXMLParser._COS_IMPORT_XML);
        Hashtable htCosImportXML = cosImportParser.parseXML(cos_import_xml);

        Hashtable htCourseGenDes = (Hashtable)htCosImportXML.get("course_gen_description");
        Hashtable htCourse = (Hashtable)htCourseGenDes.get("course");
        Hashtable htCourseBehavior = (Hashtable)htCourseGenDes.get("course_behavior");
        Hashtable htCourseDescription = (Hashtable)htCourseGenDes.get("course_description");
/*
        // testing
        System.out.println("course_creator:" + (String)htCourse.get("course_creator"));
        System.out.println("course_id:" + (String)htCourse.get("course_id"));
        System.out.println("course_system:" + (String)htCourse.get("course_system"));
        System.out.println("course_title:" + (String)htCourse.get("course_title"));
        System.out.println("level:" + (String)htCourse.get("level"));
        System.out.println("max_fields_cst:" + (String)htCourse.get("max_fields_cst"));
        System.out.println("max_fields_ort:" + (String)htCourse.get("max_fields_ort"));
        System.out.println("total_aus:" + (String)htCourse.get("total_aus"));
        System.out.println("total_blocks:" + (String)htCourse.get("total_blocks"));
        System.out.println("total_objectives:" + (String)htCourse.get("total_objectives"));
        System.out.println("total_complex_obj:" + (String)htCourse.get("total_complex_obj"));
        System.out.println("version:" + (String)htCourse.get("version"));

        System.out.println("max_normal:" + (String)htCourseBehavior.get("max_normal"));

        for (int i=0; i<htCourseDescription.size(); i++) {
            System.out.println("desc" + Integer.toString(i+1) + ":" + (String)htCourseDescription.get("desc" + Integer.toString(i+1)));
        }
*/
        // write out the physical CRS file
        String temp = "";
        BufferedWriter bwCRS = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tmpUploadDir + dbUtils.SLASH + crsFileName), cwUtils.ENC_UTF));

        CommonLog.info("File Absolute Path:" + (new File(tmpUploadDir + dbUtils.SLASH + "aicc.crs")).getAbsolutePath());

        bwCRS.write("[Course]");
        temp = (String)htCourse.get("course_creator");
        temp = dbUtils.unEscXML(temp);
        if (temp.length() > 0) {
            bwCRS.newLine();
            bwCRS.write("Course_Creator=" + temp);
        }
        temp = (String)htCourse.get("course_id");
        temp = dbUtils.unEscXML(temp);
        if (temp.length() > 0) {
            bwCRS.newLine();
            bwCRS.write("Course_ID=" + temp);
        }
        temp = (String)htCourse.get("course_system");
        temp = dbUtils.unEscXML(temp);
        if (temp.length() > 0) {
            bwCRS.newLine();
            bwCRS.write("Course_System=" + temp);
        }
        temp = (String)htCourse.get("course_title");
        temp = dbUtils.unEscXML(temp);
        if (temp.length() > 0) {
            bwCRS.newLine();
            bwCRS.write("Course_Title=" + temp);
        }
        temp = (String)htCourse.get("level");
        temp = dbUtils.unEscXML(temp);
        if (temp.length() > 0) {
            bwCRS.newLine();
            bwCRS.write("Level=" + temp);
        }
        temp = (String)htCourse.get("max_fields_cst");
        temp = dbUtils.unEscXML(temp);
        if (temp.length() > 0) {
            bwCRS.newLine();
            bwCRS.write("Max_Fields_CST=" + temp);
        }
        temp = (String)htCourse.get("max_fields_ort");
        temp = dbUtils.unEscXML(temp);
        if (temp.length() > 0) {
            bwCRS.newLine();
            bwCRS.write("Max_Fields_ORT=" + temp);
        }
        temp = (String)htCourse.get("total_aus");
        temp = dbUtils.unEscXML(temp);
        if (temp.length() > 0) {
            bwCRS.newLine();
            bwCRS.write("Total_AUs=" + temp);
        }
        temp = (String)htCourse.get("total_blocks");
        temp = dbUtils.unEscXML(temp);
        if (temp.length() > 0) {
            bwCRS.newLine();
            bwCRS.write("Total_Blocks=" + temp);
        }
        temp = (String)htCourse.get("total_objectives");
        temp = dbUtils.unEscXML(temp);
        if (temp.length() > 0) {
            bwCRS.newLine();
            bwCRS.write("Total_Objectives=" + temp);
        }
        temp = (String)htCourse.get("total_complex_obj");
        temp = dbUtils.unEscXML(temp);
        if (temp.length() > 0) {
            bwCRS.newLine();
            bwCRS.write("Total_Complex_Obj=" + temp);
        }
        temp = (String)htCourse.get("version");
        temp = dbUtils.unEscXML(temp);
        if (temp.length() > 0) {
            bwCRS.newLine();
            bwCRS.write("Version=" + temp);
        }

        bwCRS.newLine();
        bwCRS.newLine();
        bwCRS.write("[Course_Behavior]");
        temp = (String)htCourseBehavior.get("max_normal");
        temp = dbUtils.unEscXML(temp);
        if (temp.length() > 0) {
            bwCRS.newLine();
            bwCRS.write("Max_Normal=" + temp);
        }

        bwCRS.newLine();
        bwCRS.newLine();
        bwCRS.write("[Course_Description]");

        for (int i=0; i<htCourseDescription.size(); i++) {
            bwCRS.newLine();
            temp = (String)htCourseDescription.get("desc" + Integer.toString(i+1));
            temp = dbUtils.unEscXML(temp);
            bwCRS.write(temp);
        }

        bwCRS.close();

        // write out the physical CST file
        BufferedWriter bwCST = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tmpUploadDir + dbUtils.SLASH + cstFileName), cwUtils.ENC_UTF));
        // write the heading line
        bwCST.write("\"block\"");
        int max_fields_cst = Integer.parseInt((String)htCourse.get("max_fields_cst"));
        for (int i=0; i<max_fields_cst-1; i++) {
            bwCST.write(",\"member\"");
        }

        Hashtable htCourseStruct = (Hashtable)htCosImportXML.get("course_structure");
        Enumeration enumKeys = htCourseStruct.keys();
        String childSystemID = null;
        while (enumKeys.hasMoreElements()) {
            String keyName = (String)enumKeys.nextElement();
            Vector vtElement = (Vector)htCourseStruct.get(keyName);
            boolean boolHasChild = false;
            if (vtElement.size() > 0) {
                boolHasChild = true;
                bwCST.newLine();
                keyName = dbUtils.unEscXML(keyName);
                keyName = getExportSystemID(keyName, htExportSystemID, htCurrCnt);
                bwCST.write("\"" + keyName + "\"");
            }
            int childCount = 0;
            for (int i=0; i<vtElement.size(); i++) {
                childCount++;
                childSystemID = (String)vtElement.elementAt(i);
                childSystemID = dbUtils.unEscXML(childSystemID);
                childSystemID = getExportSystemID(childSystemID, htExportSystemID, htCurrCnt);
                bwCST.write(",\"" + childSystemID + "\"");
                CommonLog.info(keyName + ":" + childSystemID);
            }
            if (boolHasChild == true) {
                // the case where number of child elements smaller than max_fields_cst
                for (int i=childCount+2; i<=max_fields_cst; i++) {
                    bwCST.write(",\"" + "\"");
                }
            }
        }

        bwCST.close();


        // write out the physical DES file
        BufferedWriter bwDES = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tmpUploadDir + dbUtils.SLASH + desFileName), cwUtils.ENC_UTF));
        // write the heading line
        bwDES.write("\"system_id\",\"developer_id\",\"title\",\"description\"");

        Hashtable htDescriptor = (Hashtable)htCosImportXML.get("descriptor");
        Enumeration enumDescriptorKeys = htDescriptor.keys();
        while (enumDescriptorKeys.hasMoreElements()) {
            String keyName = (String)enumDescriptorKeys.nextElement();
            Hashtable htDescriptorRecord = (Hashtable)htDescriptor.get(keyName);
            bwDES.newLine();
            bwDES.write("\"");
            String systemID = (String)htDescriptorRecord.get("system_id");
            systemID = dbUtils.unEscXML(systemID);
            systemID = getExportSystemID(systemID, htExportSystemID, htCurrCnt);
            bwDES.write(systemID);
            bwDES.write("\",\"");
            bwDES.write(dbUtils.unEscXML((String)htDescriptorRecord.get("developer_id")));
            bwDES.write("\",\"");
            bwDES.write(dbUtils.unEscXML((String)htDescriptorRecord.get("title")));
            bwDES.write("\",\"");
            bwDES.write(dbUtils.unEscXML((String)htDescriptorRecord.get("description")));
            bwDES.write("\"");
/*
            // print it out
            System.out.println("system_id:" + (String)htDescriptorRecord.get("system_id"));
            System.out.println("developer_id:" + (String)htDescriptorRecord.get("developer_id"));
            System.out.println("title:" + (String)htDescriptorRecord.get("title"));
            System.out.println("description:" + (String)htDescriptorRecord.get("description"));
            System.out.println("external_id:" + (String)htDescriptorRecord.get("external_id"));
*/
        }

        bwDES.close();

        Hashtable htModImportXML = null;
        Vector rcnVec = dbResourceContent.getChildAss(con, cos_res_id);

        // write out the physical AU file
        BufferedWriter bwAU = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tmpUploadDir + dbUtils.SLASH + auFileName), cwUtils.ENC_UTF));
        // write the heading line
        bwAU.write("\"system_id\",\"type\",\"command_line\",\"Max_Time_Allowed\",\"time_limit_action\",\"file_name\",\"max_score\",\"mastery_score\",\"system_vendor\",\"core_vendor\",\"web_launch\",\"au_password\"");

        for (int i=0;i<rcnVec.size();i++) {
            dbResourceContent dbrcn = (dbResourceContent) rcnVec.elementAt(i);

            dbModule dbmod = new dbModule();
            dbmod.res_id = dbrcn.rcn_res_id_content;
            dbmod.mod_res_id = dbrcn.rcn_res_id_content;
            dbmod.get(con);

            mod_import_xml = dbmod.getModImportXML(con);
            aiccImportXMLParser modImportParser = new aiccImportXMLParser(aiccImportXMLParser._MOD_IMPORT_XML);
            htModImportXML = modImportParser.parseXML(mod_import_xml);

            bwAU.newLine();
            bwAU.write("\"");
            String systemID = (String)htModImportXML.get("system_id");
            systemID = dbUtils.unEscXML(systemID);
            systemID = getExportSystemID(systemID, htExportSystemID, htCurrCnt);
            bwAU.write(systemID);
            bwAU.write("\",\"");
            bwAU.write(dbUtils.unEscXML((String)htModImportXML.get("type")));
            bwAU.write("\",\"");
            bwAU.write(dbUtils.unEscXML((String)htModImportXML.get("command_line")));
            bwAU.write("\",\"");
            bwAU.write(dbUtils.unEscXML((String)htModImportXML.get("max_time_allowed")));
            bwAU.write("\",\"");
            bwAU.write(dbUtils.unEscXML((String)htModImportXML.get("time_limit_action")));
            bwAU.write("\",\"");
            bwAU.write(dbUtils.unEscXML((String)htModImportXML.get("file_name")));
            bwAU.write("\",\"");
            bwAU.write(dbUtils.unEscXML((String)htModImportXML.get("max_score")));
            bwAU.write("\",\"");
            bwAU.write(dbUtils.unEscXML((String)htModImportXML.get("mastery_score")));
            bwAU.write("\",\"");
            bwAU.write(dbUtils.unEscXML((String)htModImportXML.get("system_vendor")));
            bwAU.write("\",\"");
            bwAU.write(dbUtils.unEscXML((String)htModImportXML.get("core_vendor")));
            bwAU.write("\",\"");
            bwAU.write(dbUtils.unEscXML((String)htModImportXML.get("web_launch")));
            bwAU.write("\",\"");
            bwAU.write(dbUtils.unEscXML((String)htModImportXML.get("au_password")));
            bwAU.write("\"");

/*
            System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.println("system_id:" + (String)htModImportXML.get("system_id"));
            System.out.println("type:" + (String)htModImportXML.get("type"));
            System.out.println("command_line:" + (String)htModImportXML.get("command_line"));
            System.out.println("file_name:" + (String)htModImportXML.get("file_name"));
            System.out.println("max_score:" + (String)htModImportXML.get("max_score"));
            System.out.println("mastery_score:" + (String)htModImportXML.get("mastery_score"));
            System.out.println("max_time_allowed:" + (String)htModImportXML.get("max_time_allowed"));
            System.out.println("time_limit_action:" + (String)htModImportXML.get("time_limit_action"));
            System.out.println("system_vendor:" + (String)htModImportXML.get("system_vendor"));
            System.out.println("core_vendor:" + (String)htModImportXML.get("core_vendor"));
            System.out.println("au_password:" + (String)htModImportXML.get("au_password"));
            System.out.println("web_launch:" + (String)htModImportXML.get("web_launch"));
*/
        }

        bwAU.close();

        String[] zipFile = new String[4];
        zipFile[0] = new String(crsFileName);
        zipFile[1] = new String(cstFileName);
        zipFile[2] = new String(desFileName);
        zipFile[3] = new String(auFileName);
        dbUtils.makeZip(tmpUploadDir + dbUtils.SLASH + zipFileName, tmpUploadDir, zipFile, false);

        return (tmpUploadDir + dbUtils.SLASH + zipFileName);
    }

    public String getCosImportXML(Connection con) throws IOException, SQLException, qdbException, qdbErrMessage {
        String readerXML = null;

        try {
            PreparedStatement stmt = con.prepareStatement(
            " SELECT  cos_import_xml FROM Course "
            + " where cos_res_id = ? ");

            // set the values for prepared statements
            stmt.setLong(1, cos_res_id);
            String xml = new String("");

            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                readerXML = cwSQL.getClobValue(rs, "cos_import_xml");
//                readerXML = rs.getCharacterStream("cos_import_xml");
            }
            else
            {
                stmt.close();
                throw new qdbException( "No data for course. id = " + cos_res_id );
            }

            stmt.close();

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }

        return readerXML;
    }

    // for import AICC course
    // by cliff, 2001/4/18
    public long importAiccCos(Connection con, String domain, loginProfile prof, String aicc_crs_filename, String aicc_cst_filename, String aicc_des_filename, String aicc_au_filename, String aicc_ort_filename, Vector vtParentObj, Long mod_mod_res_id_parent, String isEnrollmentRelated)
        throws IOException, SQLException, qdbException, qdbErrMessage, cwSysMessage, cwException {
        //try {
            // delete the previous added module and their relations and mod-obj relations
            // cannot use this method as this will also delete the course enrollment also
            // delCourseMod(con, prof);

            String cos_import_xml = null;
            String mod_import_xml = null;

            Vector vtCosDescriptor = null;
            Vector vtCosStructure = null;
            Vector vtObjectiveRelation = null;

            Vector vtTemp = null;
            long mod_id = 0;

            crsIniFile iniFileCRS = null;
            /*
			if( !cwUtils.isUnicodeLittleFile(new File(aicc_crs_filename)) ) {
				throw new qdbErrMessage("GEN008");
			}
			*/
            try {
                iniFileCRS = new crsIniFile(aicc_crs_filename);
            } catch(IOException e) {
            	CommonLog.error("Error in loading the iniFileCRS:" + e.toString());
                throw new IOException(e.toString());
            }

            vtCosDescriptor = buildCosDescriptorVector(aicc_des_filename);
            vtCosStructure = getCosStructureVector(aicc_cst_filename);
            if (aicc_ort_filename != null) {
                vtObjectiveRelation = getCosObjectiveVector(aicc_ort_filename);
            }
            Hashtable htBlockElements = buildAiccBlockElements(getMemberRelationLst(aicc_cst_filename));

            Hashtable htAuModID = new Hashtable();
            Hashtable htObjObjID = new Hashtable();

            // insert the moudle
            dbModule dbmod = null;

            BufferedReader in = new BufferedReader(new InputStreamReader(cwUtils.openUTF8FileStream(new File(aicc_au_filename)), cwUtils.ENC_UTF));
            String line = null;
            String systemID = null;
            String type = null;
            String command_line = null;
            String file_name = null;
            String max_score = null;
            String mastery_score = null;
            String max_time_allowed = null;
            String time_limit_action = null;
            String system_vendor = null;
            String core_vendor = null;
            String web_launch = null;
            String au_password = null;

            String tempStr = null;

            Hashtable htAuFieldOrder = null;
            Hashtable htAuFieldValue = null;

            StringTokenizer st = null;
            // the first line is used to determine the field order
            for (;;) {
                try {
                    line = in.readLine();
                } catch(Exception e) {
                    throw new IOException(e.toString());
                }
                if (line == null) {
                    break;
                }
                else if (line.trim().length() > 0 && line.trim().equalsIgnoreCase("") == false) {
                    htAuFieldOrder = new Hashtable();

                    int index = 1;

                    Vector vtRecord = buildTableRecord(line);
                    String recordElement = null;
                    for (int i=0; i<vtRecord.size(); i++) {
                        recordElement = (String)vtRecord.elementAt(i);
                        htAuFieldOrder.put(recordElement.toLowerCase(), new Integer(index));
                        index++;
                    }

                    break;
                }
            }

            for (;;) {
                String mod_desc = null;

                try {
                    line = in.readLine();
                } catch(Exception e) {
                    throw new IOException(e.toString());
                }
                if (line == null) {
                    break;
                }
                else if (line.trim().length() == 0 || line.trim().equalsIgnoreCase("") == true) {
                    continue;
                }
                else {
                    htAuFieldValue = new Hashtable();

                    int index = 1;

                    Vector vtRecord = buildTableRecord(line);
                    String recordElement = null;
                    for (int i=0; i<vtRecord.size(); i++) {
                        recordElement = (String)vtRecord.elementAt(i);
                        htAuFieldValue.put(new Integer(index), recordElement);
                        index++;
                    }

                    Integer fieldIndex = null;

                    fieldIndex = (Integer)htAuFieldOrder.get("system_id");
                    systemID = (String)htAuFieldValue.get(fieldIndex);

                    fieldIndex = (Integer)htAuFieldOrder.get("type");
                    type = (String)htAuFieldValue.get(fieldIndex);

                    fieldIndex = (Integer)htAuFieldOrder.get("command_line");
                    command_line = (String)htAuFieldValue.get(fieldIndex);

                    fieldIndex = (Integer)htAuFieldOrder.get("file_name");
                    file_name = (String)htAuFieldValue.get(fieldIndex);

                    fieldIndex = (Integer)htAuFieldOrder.get("max_score");
                    max_score = (String)htAuFieldValue.get(fieldIndex);

                    fieldIndex = (Integer)htAuFieldOrder.get("mastery_score");
                    mastery_score = (String)htAuFieldValue.get(fieldIndex);

                    fieldIndex = (Integer)htAuFieldOrder.get("max_time_allowed");
                    max_time_allowed = (String)htAuFieldValue.get(fieldIndex);

                    fieldIndex = (Integer)htAuFieldOrder.get("time_limit_action");
                    time_limit_action = (String)htAuFieldValue.get(fieldIndex);

                    fieldIndex = (Integer)htAuFieldOrder.get("system_vendor");
                    system_vendor = (String)htAuFieldValue.get(fieldIndex);

                    fieldIndex = (Integer)htAuFieldOrder.get("core_vendor");
                    core_vendor = (String)htAuFieldValue.get(fieldIndex);

                    fieldIndex = (Integer)htAuFieldOrder.get("web_launch");
                    web_launch = (String)htAuFieldValue.get(fieldIndex);

                    fieldIndex = (Integer)htAuFieldOrder.get("au_password");
                    au_password = (String)htAuFieldValue.get(fieldIndex);

                    vtTemp = null;
                    for (int i=0; i<vtCosDescriptor.size(); i++) {
                        vtTemp = (Vector)vtCosDescriptor.elementAt(i);
                        if (systemID.equalsIgnoreCase((String)vtTemp.elementAt(0)) == true) {
                            break;
                        }
                        else {
                            vtTemp = null;
                        }
                    }

                    mod_import_xml = "<assignable_unit system_id=\"";
                    mod_import_xml += dbUtils.esc4XML(systemID);
                    mod_import_xml += "\" type=\"";
                    mod_import_xml += dbUtils.esc4XML(type);
                    mod_import_xml += "\" command_line=\"";
                    mod_import_xml += dbUtils.esc4XML(command_line);
                    mod_import_xml += "\" file_name=\"";
                    mod_import_xml += dbUtils.esc4XML(file_name);
                    mod_import_xml += "\" max_score=\"";
                    mod_import_xml += dbUtils.esc4XML(max_score);
                    mod_import_xml += "\" mastery_score=\"";
                    mod_import_xml += dbUtils.esc4XML(mastery_score);
                    mod_import_xml += "\" max_time_allowed=\"";
                    mod_import_xml += dbUtils.esc4XML(max_time_allowed);
                    mod_import_xml += "\" time_limit_action=\"";
                    mod_import_xml += dbUtils.esc4XML(time_limit_action);
                    mod_import_xml += "\" system_vendor=\"";
                    mod_import_xml += dbUtils.esc4XML(system_vendor);
                    mod_import_xml += "\" core_vendor=\"";
                    mod_import_xml += dbUtils.esc4XML(core_vendor);
                    mod_import_xml += "\" web_launch=\"";
                    mod_import_xml += web_launch;
                    mod_import_xml += "\" au_password=\"";
                    mod_import_xml += au_password;

                    mod_import_xml += "\" />";

                    dbmod = new dbModule();

                    if (max_time_allowed.equalsIgnoreCase("") == true || max_time_allowed.length() == 0) {
                    }
                    else {
                        int hr = 0;
                        int min = 0;
                        int sec = 0;
                        String strTime = null;
                        StringTokenizer stTime = new StringTokenizer(max_time_allowed,":");

                        strTime = stTime.nextToken();
                        strTime = strTime.trim();
                        hr = Integer.parseInt(strTime);

                        strTime = stTime.nextToken();
                        strTime = strTime.trim();
                        min = Integer.parseInt(strTime);

                        strTime = stTime.nextToken();
                        strTime = strTime.trim();
                        sec = Integer.parseInt(strTime);

                        dbmod.res_duration = hr*60 + min + sec/60;
                    }

                    if (vtTemp != null) {
                        String title = (String)vtTemp.elementAt(2);
                        if (title.length() > 0) {
                            dbmod.res_title = dbUtils.unEscXML(title);
                        }
                        else {
                            // do nothing
                        }
                        String desc = (String)vtTemp.elementAt(3);
                        if (desc != null && desc.length() > 0) {
                            mod_desc = dbUtils.unEscXML(desc);
                        }
                        else {
                            mod_desc = "";
                        }
                    }
                    else {
                        // do nothing
                    }

                    dbmod.res_type = "MOD";

                    if (max_score.length() > 0) {
                        dbmod.mod_max_score = Float.parseFloat(max_score);;
                    }
                    else {
                    }

                    if (mastery_score.length() > 0) {
                        dbmod.mod_pass_score = Float.parseFloat(mastery_score);
                    }
                    else {
                    }
                    if (isEnrollmentRelated.equals(ENROLLMENT_RELATED_ALL)) {
                        dbmod.res_status = dbResource.RES_STATUS_ON;
                    } else {
                        dbmod.res_status = dbResource.RES_STATUS_OFF;
                    }
                    dbmod.mod_type = "AICC_AU";
                    dbmod.res_subtype = "AICC_AU";
                    dbmod.mod_in_eff_end_datetime = Timestamp.valueOf(dbUtils.MAX_TIMESTAMP);
                    dbmod.mod_in_eff_start_datetime = Timestamp.valueOf(dbUtils.MIN_TIMESTAMP);
                    dbmod.res_src_type = "URL";
                    dbmod.res_src_link = file_name;

                    dbmod.res_upd_user = prof.usr_id;
                    dbmod.res_usr_id_owner = prof.usr_id;

                    if (vtParentObj.size() > 0) {
                        dbmod.mod_mod_res_id_parent = mod_mod_res_id_parent.longValue();
                    }
                    mod_id = insModule(con, dbmod, domain, prof);
                    dbmod.updAicc(con, prof, core_vendor, au_password, mod_import_xml, time_limit_action, web_launch, iniFileCRS.getValue("Course_Creator"), mod_desc, iniFileCRS.getValue("Version"));

                    htAuModID.put(systemID, new Long(mod_id));
                }

            }

            in.close();

            Vector vtElement = null;
            int idx = 0;

            // input AICC objectives into DB
            for (int i=0; i<vtCosDescriptor.size(); i++) {
                String objSystemID = null;
                String objDeveloperID = null;
                String objTitle = null;
                String objDesc = null;
                dbObjective dbObj = null;

                vtElement = (Vector)vtCosDescriptor.elementAt(i);
                objSystemID = (String)vtElement.elementAt(0);
                if (objSystemID.toLowerCase().startsWith("j") == true) {
                    objDeveloperID = (String)vtElement.elementAt(1);
                    objTitle = (String)vtElement.elementAt(2);
                    objDesc = (String)vtElement.elementAt(3);

                    dbObj = new dbObjective();
                    dbObj.obj_type = "AICC";
                    dbObj.obj_desc = trimObjDesc(objDesc);
                    // if the supplied parent is not empty, use it as the parent of the current objective
                    if (vtParentObj.size() > 0) {
                        dbObj.obj_obj_id_parent = ((Integer) vtParentObj.get(idx++)).longValue();
                    }
                    dbObj.obj_title = objTitle;
                    dbObj.obj_developer_id = objDeveloperID;
                    dbObj.obj_import_xml = "<objective developer_id=\"";
                    dbObj.obj_import_xml += objDeveloperID;
                    dbObj.obj_import_xml += "\" />";
                    int obj_id = (int)dbObj.insAicc(con, prof);
                    htObjObjID.put(objSystemID, new Integer(obj_id));
                }
            }
            if(vtParentObj.size()==0){
                for(int x = 1; x<vtCosDescriptor.size();x++){
                    vtElement = (Vector)vtCosDescriptor.elementAt(x);
                    vtParentObj.addElement((Integer)htObjObjID.get((String)((String)vtElement.elementAt(0))));
                }
            }

            if (vtObjectiveRelation != null) {
                vtElement = null;
                for (int i=0; i<vtObjectiveRelation.size(); i++) {
                    vtElement = (Vector)vtObjectiveRelation.elementAt(i);
                    String parentSystemID = (String)vtElement.elementAt(0);

                    Vector vtObjID = new Vector();
                    String childSystemID = null;
                    for (int j=1; j<vtElement.size(); j++) {
                        childSystemID = (String)vtElement.elementAt(j);
                        vtObjID.addElement((Integer)htObjObjID.get(childSystemID));
                    }

                    long[] intObjID = new long[vtObjID.size()];
                    for (int k=0; k<vtObjID.size(); k++) {
                        intObjID[k] = ((Integer)vtObjID.elementAt(k)).longValue();
                    }

                    long[] int_parent_mod_id_lst = null;
                    if (parentSystemID.startsWith("A") == true || parentSystemID.startsWith("a") == true) {
                        int_parent_mod_id_lst = new long[] { ((Long)htAuModID.get(parentSystemID)).longValue() };
                    }
                    // collect all AU's mod_id in that AICC Block
                    else if (parentSystemID.startsWith("B") == true || parentSystemID.startsWith("b") == true) {
                        Vector vtBlockElements = (Vector)htBlockElements.get(parentSystemID);
                        int_parent_mod_id_lst = new long[vtBlockElements.size()];
                        String tempSystemID = null;
                        for (int m=0; m<vtBlockElements.size(); m++) {
                            tempSystemID = (String)vtBlockElements.elementAt(m);
                            int_parent_mod_id_lst[m] = ((Long)htAuModID.get(tempSystemID)).longValue();
                        }
                    }
                    dbResourceObjective dbResObj = new dbResourceObjective();
                    dbResObj.insResObj(con, int_parent_mod_id_lst, intObjID);

                }
            }

            cos_import_xml = buildAiccCosImportXML(iniFileCRS, vtCosStructure, vtCosDescriptor, aicc_au_filename, htAuModID, htObjObjID);

            cos_structure_xml = buildAiccCosStructure(con, vtCosStructure, vtCosDescriptor, htAuModID);

            updCosStructureNoTimestampCheck(con, prof);

            int tmp_max_normal = this.MAX_NORMAL;
            try {
                tmp_max_normal = Integer.parseInt(iniFileCRS.getValue("Max_Normal"));
            } catch (NumberFormatException ne) {
                tmp_max_normal = this.MAX_NORMAL;
            }

            updAiccCos(con, prof, cos_import_xml, iniFileCRS.getValue("Version"), iniFileCRS.getValue("Course_Creator"), tmp_max_normal, true, true);

            return mod_id;
/*
        } catch(qdbErrMessage e) {
            // rollback at qdbAction
//            con.rollback();
            throw new qdbErrMessage(e.toString());
        }
*/
    }

    // by cliff, 8/6/2001
    // for inserting an module with aicc type
    public long insAiccAu(Connection con, dbModule dbmod, String domain, loginProfile prof, String aicc_crs_filename, String aicc_cst_filename, String aicc_des_filename, String aicc_au_filename, String aicc_ort_filename, Vector vtParentObj, boolean upd_src_link)
        throws IOException, SQLException, qdbException, qdbErrMessage, cwSysMessage {
        //try {
            long mod_id = 0;

            String mod_import_xml = null;

            Vector vtCosDescriptor = null;
            Vector vtCosStructure = null;
            Vector vtObjectiveRelation = null;

            Vector vtTemp = null;
			/*
			if( !cwUtils.isUnicodeLittleFile(new File(aicc_crs_filename)) ) {
				throw new qdbErrMessage("GEN008");
			}
			*/
            crsIniFile iniFileCRS = null;
            try {
                iniFileCRS = new crsIniFile(aicc_crs_filename);
            } catch(Exception e) {
                CommonLog.error(e.getMessage(),e);
                throw new qdbErrMessage("MOD009");
            }

            vtCosDescriptor = buildCosDescriptorVector(aicc_des_filename);
            vtCosStructure = getCosStructureVector(aicc_cst_filename);
            if (aicc_ort_filename != null) {
                vtObjectiveRelation = getCosObjectiveVector(aicc_ort_filename);
            }
            Hashtable htBlockElements = buildAiccBlockElements(getMemberRelationLst(aicc_cst_filename));

            Hashtable htAuModID = new Hashtable();
            Hashtable htObjObjID = new Hashtable();

            // insert the moudle
//            dbModule dbmod = null;

            BufferedReader in = null;
            /*
			if( !cwUtils.isUnicodeLittleFile(new File(aicc_au_filename)) ) {
				throw new qdbErrMessage("GEN008");
			}
            */
            try {
            	in = new BufferedReader(new InputStreamReader(cwUtils.openUTF8FileStream(new File(aicc_au_filename)), cwUtils.ENC_UTF));
                String line = null;
                String systemID = null;
                String type = null;
                String command_line = null;
                String file_name = null;
                String max_score = null;
                String mastery_score = null;
                String max_time_allowed = null;
                String time_limit_action = null;
                String system_vendor = null;
                String core_vendor = null;
                String web_launch = null;
                String au_password = null;

                String tempStr = null;

                Hashtable htAuFieldOrder = null;
                Hashtable htAuFieldValue = null;

                StringTokenizer st = null;
                // the first line is used to determine the field order
                for (;;) {
                    line = in.readLine();
                    if (line == null) {
                        break;
                    }
                    else if (line.trim().length() > 0 && line.trim().equalsIgnoreCase("") == false) {
                        htAuFieldOrder = new Hashtable();

                        int index = 1;

                        Vector vtRecord = buildTableRecord(line);
                        String recordElement = null;
                        for (int i=0; i<vtRecord.size(); i++) {
                            recordElement = (String)vtRecord.elementAt(i);
                            htAuFieldOrder.put(recordElement.toLowerCase(), new Integer(index));
                            index++;
                        }

                        break;
                    }
                }

                //actually there should be only one AU record in the .au file for skillsoft course
                for (;;) {
                    String mod_desc = null;
                    line = in.readLine();
                    if (line == null) {
                        break;
                    }
                    else if (line.trim().length() == 0 || line.trim().equalsIgnoreCase("") == true) {
                        continue;
                    }
                    else {
                        htAuFieldValue = new Hashtable();

                        int index = 1;

                        Vector vtRecord = buildTableRecord(line);
                        String recordElement = null;
                        for (int i=0; i<vtRecord.size(); i++) {
                            recordElement = (String)vtRecord.elementAt(i);
                            htAuFieldValue.put(new Integer(index), recordElement);
                            index++;
                        }

                        Integer fieldIndex = null;

                        fieldIndex = (Integer)htAuFieldOrder.get("system_id");
                        systemID = (String)htAuFieldValue.get(fieldIndex);

                        fieldIndex = (Integer)htAuFieldOrder.get("type");
                        type = (String)htAuFieldValue.get(fieldIndex);

                        fieldIndex = (Integer)htAuFieldOrder.get("command_line");
                        command_line = (String)htAuFieldValue.get(fieldIndex);

                        fieldIndex = (Integer)htAuFieldOrder.get("file_name");
                        file_name = (String)htAuFieldValue.get(fieldIndex);
                        if (upd_src_link && file_name != null && file_name.length() > 0) {
                            file_name = transAiccSrcUrl(aicc_au_filename,file_name);
                        }

                        fieldIndex = (Integer)htAuFieldOrder.get("max_score");
                        max_score = (String)htAuFieldValue.get(fieldIndex);

                        fieldIndex = (Integer)htAuFieldOrder.get("mastery_score");
                        mastery_score = (String)htAuFieldValue.get(fieldIndex);

                        fieldIndex = (Integer)htAuFieldOrder.get("max_time_allowed");
                        max_time_allowed = (String)htAuFieldValue.get(fieldIndex);

                        fieldIndex = (Integer)htAuFieldOrder.get("time_limit_action");
                        time_limit_action = (String)htAuFieldValue.get(fieldIndex);

                        fieldIndex = (Integer)htAuFieldOrder.get("system_vendor");
                        system_vendor = (String)htAuFieldValue.get(fieldIndex);

                        fieldIndex = (Integer)htAuFieldOrder.get("core_vendor");
                        core_vendor = (String)htAuFieldValue.get(fieldIndex);

                        fieldIndex = (Integer)htAuFieldOrder.get("web_launch");
                        web_launch = (String)htAuFieldValue.get(fieldIndex);

                        fieldIndex = (Integer)htAuFieldOrder.get("au_password");
                        au_password = (String)htAuFieldValue.get(fieldIndex);

                        vtTemp = null;
                        for (int i=0; i<vtCosDescriptor.size(); i++) {
                            vtTemp = (Vector)vtCosDescriptor.elementAt(i);
                            if (systemID.equalsIgnoreCase((String)vtTemp.elementAt(0)) == true) {
                                break;
                            }
                            else {
                                vtTemp = null;
                            }
                        }

                        mod_import_xml = "<assignable_unit system_id=\"";
                        mod_import_xml += dbUtils.esc4XML(systemID);
                        mod_import_xml += "\" type=\"";
                        mod_import_xml += dbUtils.esc4XML(type);
                        mod_import_xml += "\" command_line=\"";
                        mod_import_xml += dbUtils.esc4XML(command_line);
                        mod_import_xml += "\" file_name=\"";
                        mod_import_xml += dbUtils.esc4XML(file_name);
                        mod_import_xml += "\" max_score=\"";
                        mod_import_xml += dbUtils.esc4XML(max_score);
                        mod_import_xml += "\" mastery_score=\"";
                        mod_import_xml += dbUtils.esc4XML(mastery_score);
                        mod_import_xml += "\" max_time_allowed=\"";
                        mod_import_xml += dbUtils.esc4XML(max_time_allowed);
                        mod_import_xml += "\" time_limit_action=\"";
                        mod_import_xml += dbUtils.esc4XML(time_limit_action);
                        mod_import_xml += "\" system_vendor=\"";
                        mod_import_xml += dbUtils.esc4XML(system_vendor);
                        mod_import_xml += "\" core_vendor=\"";
                        mod_import_xml += dbUtils.esc4XML(core_vendor);
                        mod_import_xml += "\" web_launch=\"";
                        mod_import_xml += web_launch;
                        mod_import_xml += "\" au_password=\"";
                        mod_import_xml += au_password;

                        mod_import_xml += "\" />";

                        if (max_score.length() > 0) {
                            dbmod.mod_max_score = Float.parseFloat(max_score);;
                        }
                        else {
                        }

                        if (mastery_score.length() > 0) {
                            dbmod.mod_pass_score = Float.parseFloat(mastery_score);
                        }
                        else {
                        }

                        if (max_time_allowed.equalsIgnoreCase("") == true || max_time_allowed.length() == 0) {
                        }
                        else {
                            int hr = 0;
                            int min = 0;
                            int sec = 0;
                            String strTime = null;
                            StringTokenizer stTime = new StringTokenizer(max_time_allowed,":");

                            strTime = stTime.nextToken();
                            strTime = strTime.trim();
                            hr = Integer.parseInt(strTime);

                            strTime = stTime.nextToken();
                            strTime = strTime.trim();
                            min = Integer.parseInt(strTime);

                            strTime = stTime.nextToken();
                            strTime = strTime.trim();
                            sec = Integer.parseInt(strTime);

                            dbmod.res_duration = hr*60 + min + sec/60;
                        }

                        dbmod.res_type = "MOD";
    //                    dbmod.res_lan = "ISO-8859-1";
                        dbmod.res_subtype = "AICC_AU";
                        dbmod.res_src_type = "AICC_FILES";
                        dbmod.res_src_link = file_name;
    //                    dbmod.mod_in_eff_end_datetime = Timestamp.valueOf(dbUtils.MAX_TIMESTAMP);
    //                    dbmod.mod_in_eff_start_datetime = Timestamp.valueOf(dbUtils.MIN_TIMESTAMP);

                        mod_id = insModule(con, dbmod, domain, prof);
                        dbmod.updAicc(con, prof, core_vendor, au_password, mod_import_xml, time_limit_action, web_launch, iniFileCRS.getValue("Course_Creator"), dbmod.res_desc, iniFileCRS.getValue("Version"));

                        htAuModID.put(systemID, new Long(mod_id));
                    }

                }
            } catch (Exception e) {
                CommonLog.error(e.getMessage(),e);
                throw new qdbErrMessage("MOD009");
            } finally {
                if(in!=null) {in.close();}
            }
            Vector vtElement = null;

            int idx = 0;
            // input AICC objectives into DB
            for (int i=0; i<vtCosDescriptor.size(); i++) {
                String objSystemID = null;
                String objDeveloperID = null;
                String objTitle = null;
                String objDesc = null;
                dbObjective dbObj = null;

                vtElement = (Vector)vtCosDescriptor.elementAt(i);
                objSystemID = (String)vtElement.elementAt(0);
                if (objSystemID.toLowerCase().startsWith("j") == true) {
                    objDeveloperID = (String)vtElement.elementAt(1);
                    objTitle = (String)vtElement.elementAt(2);
                    objDesc = (String)vtElement.elementAt(3);

                    dbObj = new dbObjective();
                    dbObj.obj_type = "AICC";
                    dbObj.obj_desc = trimObjDesc(objDesc);
                    // if the supplied parent is not empty, use it as the parent of the current objective
                    if (vtParentObj.size() > 0) {
                        dbObj.obj_obj_id_parent = ((Integer) vtParentObj.get(idx++)).longValue();
                    }
                    dbObj.obj_title = objTitle;
                    dbObj.obj_developer_id = objDeveloperID;
                    dbObj.obj_import_xml = "<objective developer_id=\"";
                    dbObj.obj_import_xml += objDeveloperID;
                    dbObj.obj_import_xml += "\" />";
                    int obj_id = (int)dbObj.insAicc(con, prof);
                    htObjObjID.put(objSystemID, new Integer(obj_id));
                }
            }
            if(vtParentObj.size()==0){
                for(int x = 1; x<vtCosDescriptor.size();x++){
                    vtElement = (Vector)vtCosDescriptor.elementAt(x);
                    vtParentObj.addElement((Integer)htObjObjID.get((String)((String)vtElement.elementAt(0))));
                }
            }
            if (vtObjectiveRelation != null) {
                vtElement = null;
                for (int i=0; i<vtObjectiveRelation.size(); i++) {
                    vtElement = (Vector)vtObjectiveRelation.elementAt(i);
                    String parentSystemID = (String)vtElement.elementAt(0);

                    Vector vtObjID = new Vector();
                    String childSystemID = null;
                    for (int j=1; j<vtElement.size(); j++) {
                        childSystemID = (String)vtElement.elementAt(j);
                        vtObjID.addElement((Integer)htObjObjID.get(childSystemID));
                    }

                    long[] intObjID = new long[vtObjID.size()];
                    for (int k=0; k<vtObjID.size(); k++) {
                        intObjID[k] = ((Integer)vtObjID.elementAt(k)).longValue();
                    }

                    long[] int_parent_mod_id_lst = null;
                    if (parentSystemID.startsWith("A") == true || parentSystemID.startsWith("a") == true) {
                        int_parent_mod_id_lst = new long[] { ((Long)htAuModID.get(parentSystemID)).longValue() };
                    }
                    // collect all AU's mod_id in that AICC Block
                    else if (parentSystemID.startsWith("B") == true || parentSystemID.startsWith("b") == true) {
                        Vector vtBlockElements = (Vector)htBlockElements.get(parentSystemID);
                        int_parent_mod_id_lst = new long[vtBlockElements.size()];
                        String tempSystemID = null;
                        for (int m=0; m<vtBlockElements.size(); m++) {
                            tempSystemID = (String)vtBlockElements.elementAt(m);
                            int_parent_mod_id_lst[m] = ((Long)htAuModID.get(tempSystemID)).longValue();
                        }
                    }
                    dbResourceObjective dbResObj = new dbResourceObjective();
                    dbResObj.insResObj(con, int_parent_mod_id_lst, intObjID);

                }
            }

            // update the cos corresponding fields, or reset the fields that is previously set
            // during importing an AICC course which should be reset when inserting/updating an AICC AU
            updAiccCos(con, prof, null, null, null, -1, false, false, false);

            return mod_id;
/*
        } catch(qdbErrMessage e) {
            // rollback at qdbAction
//            con.rollback();
            throw new qdbErrMessage(e.toString());
        }
*/
    }
    public long insAiccAu(Connection con, dbModule dbmod, String domain, loginProfile prof, String aicc_crs_filename, String aicc_cst_filename, String aicc_des_filename, String aicc_au_filename, String aicc_ort_filename, Vector vtParentObj, boolean upd_src_link, String path)
        throws IOException, SQLException, qdbException, qdbErrMessage, cwSysMessage {
        //try {
            long mod_id = 0;

            String mod_import_xml = null;

            Vector vtCosDescriptor = null;
            Vector vtCosStructure = null;
            Vector vtObjectiveRelation = null;

            Vector vtTemp = null;
			/*
			if( !cwUtils.isUnicodeLittleFile(new File(aicc_crs_filename)) ) {
				throw new qdbErrMessage("GEN008");
			}
			*/
            crsIniFile iniFileCRS = null;
            try {
                iniFileCRS = new crsIniFile(aicc_crs_filename);
            } catch(Exception e) {
                CommonLog.error(e.getMessage(),e);
                throw new qdbErrMessage("MOD009");
            }

            vtCosDescriptor = buildCosDescriptorVector(aicc_des_filename);
            vtCosStructure = getCosStructureVector(aicc_cst_filename);
            if (aicc_ort_filename != null) {
                vtObjectiveRelation = getCosObjectiveVector(aicc_ort_filename);
            }
            Hashtable htBlockElements = buildAiccBlockElements(getMemberRelationLst(aicc_cst_filename));

            Hashtable htAuModID = new Hashtable();
            Hashtable htObjObjID = new Hashtable();

            // insert the moudle
//            dbModule dbmod = null;

            BufferedReader in = null;
            /*
			if( !cwUtils.isUnicodeLittleFile(new File(aicc_au_filename)) ) {
				throw new qdbErrMessage("GEN008");
			}
            */
            try {
            	in = new BufferedReader(new InputStreamReader(cwUtils.openUTF8FileStream(new File(aicc_au_filename)), cwUtils.ENC_UTF));
                String line = null;
                String systemID = null;
                String type = null;
                String command_line = null;
                String file_name = null;
                String max_score = null;
                String mastery_score = null;
                String max_time_allowed = null;
                String time_limit_action = null;
                String system_vendor = null;
                String core_vendor = null;
                String web_launch = null;
                String au_password = null;

                String tempStr = null;

                Hashtable htAuFieldOrder = null;
                Hashtable htAuFieldValue = null;

                StringTokenizer st = null;
                // the first line is used to determine the field order
                for (;;) {
                    line = in.readLine();
                    if (line == null) {
                        break;
                    }
                    else if (line.trim().length() > 0 && line.trim().equalsIgnoreCase("") == false) {
                        htAuFieldOrder = new Hashtable();

                        int index = 1;

                        Vector vtRecord = buildTableRecord(line);
                        String recordElement = null;
                        for (int i=0; i<vtRecord.size(); i++) {
                            recordElement = (String)vtRecord.elementAt(i);
                            htAuFieldOrder.put(recordElement.toLowerCase(), new Integer(index));
                            index++;
                        }

                        break;
                    }
                }

                //actually there should be only one AU record in the .au file for skillsoft course
                for (;;) {
                    String mod_desc = null;
                    line = in.readLine();
                    if (line == null) {
                        break;
                    }
                    else if (line.trim().length() == 0 || line.trim().equalsIgnoreCase("") == true) {
                        continue;
                    }
                    else {
                        htAuFieldValue = new Hashtable();

                        int index = 1;

                        Vector vtRecord = buildTableRecord(line);
                        String recordElement = null;
                        for (int i=0; i<vtRecord.size(); i++) {
                            recordElement = (String)vtRecord.elementAt(i);
                            htAuFieldValue.put(new Integer(index), recordElement);
                            index++;
                        }

                        Integer fieldIndex = null;

                        fieldIndex = (Integer)htAuFieldOrder.get("system_id");
                        systemID = (String)htAuFieldValue.get(fieldIndex);

                        fieldIndex = (Integer)htAuFieldOrder.get("type");
                        type = (String)htAuFieldValue.get(fieldIndex);

                        fieldIndex = (Integer)htAuFieldOrder.get("command_line");
                        command_line = (String)htAuFieldValue.get(fieldIndex);

                        fieldIndex = (Integer)htAuFieldOrder.get("file_name");
                        file_name = (String)htAuFieldValue.get(fieldIndex);
                        if (upd_src_link && file_name != null && file_name.length() > 0) {
                            file_name = transAiccSrcUrl(aicc_au_filename,file_name);
                        }

                        fieldIndex = (Integer)htAuFieldOrder.get("max_score");
                        max_score = (String)htAuFieldValue.get(fieldIndex);

                        fieldIndex = (Integer)htAuFieldOrder.get("mastery_score");
                        mastery_score = (String)htAuFieldValue.get(fieldIndex);

                        fieldIndex = (Integer)htAuFieldOrder.get("max_time_allowed");
                        max_time_allowed = (String)htAuFieldValue.get(fieldIndex);

                        fieldIndex = (Integer)htAuFieldOrder.get("time_limit_action");
                        time_limit_action = (String)htAuFieldValue.get(fieldIndex);

                        fieldIndex = (Integer)htAuFieldOrder.get("system_vendor");
                        system_vendor = (String)htAuFieldValue.get(fieldIndex);

                        fieldIndex = (Integer)htAuFieldOrder.get("core_vendor");
                        core_vendor = (String)htAuFieldValue.get(fieldIndex);

                        fieldIndex = (Integer)htAuFieldOrder.get("web_launch");
                        web_launch = (String)htAuFieldValue.get(fieldIndex);

                        fieldIndex = (Integer)htAuFieldOrder.get("au_password");
                        au_password = (String)htAuFieldValue.get(fieldIndex);

                        vtTemp = null;
                        for (int i=0; i<vtCosDescriptor.size(); i++) {
                            vtTemp = (Vector)vtCosDescriptor.elementAt(i);
                            if (systemID.equalsIgnoreCase((String)vtTemp.elementAt(0)) == true) {
                                break;
                            }
                            else {
                                vtTemp = null;
                            }
                        }

                        mod_import_xml = "<assignable_unit system_id=\"";
                        mod_import_xml += dbUtils.esc4XML(systemID);
                        mod_import_xml += "\" type=\"";
                        mod_import_xml += dbUtils.esc4XML(type);
                        mod_import_xml += "\" command_line=\"";
                        mod_import_xml += dbUtils.esc4XML(command_line);
                        mod_import_xml += "\" file_name=\"";
                        mod_import_xml += dbUtils.esc4XML(file_name);
                        mod_import_xml += "\" max_score=\"";
                        mod_import_xml += dbUtils.esc4XML(max_score);
                        mod_import_xml += "\" mastery_score=\"";
                        mod_import_xml += dbUtils.esc4XML(mastery_score);
                        mod_import_xml += "\" max_time_allowed=\"";
                        mod_import_xml += dbUtils.esc4XML(max_time_allowed);
                        mod_import_xml += "\" time_limit_action=\"";
                        mod_import_xml += dbUtils.esc4XML(time_limit_action);
                        mod_import_xml += "\" system_vendor=\"";
                        mod_import_xml += dbUtils.esc4XML(system_vendor);
                        mod_import_xml += "\" core_vendor=\"";
                        mod_import_xml += dbUtils.esc4XML(core_vendor);
                        mod_import_xml += "\" web_launch=\"";
                        mod_import_xml += web_launch;
                        mod_import_xml += "\" au_password=\"";
                        mod_import_xml += au_password;

                        mod_import_xml += "\" />";

                        if (max_score.length() > 0) {
                            dbmod.mod_max_score = Float.parseFloat(max_score);;
                        }
                        else {
                        }

                        if (mastery_score.length() > 0) {
                            dbmod.mod_pass_score = Float.parseFloat(mastery_score);
                        }
                        else {
                        }

                        if (max_time_allowed.equalsIgnoreCase("") == true || max_time_allowed.length() == 0) {
                        }
                        else {
                            int hr = 0;
                            int min = 0;
                            int sec = 0;
                            String strTime = null;
                            StringTokenizer stTime = new StringTokenizer(max_time_allowed,":");

                            strTime = stTime.nextToken();
                            strTime = strTime.trim();
                            hr = Integer.parseInt(strTime);

                            strTime = stTime.nextToken();
                            strTime = strTime.trim();
                            min = Integer.parseInt(strTime);

                            strTime = stTime.nextToken();
                            strTime = strTime.trim();
                            sec = Integer.parseInt(strTime);

                            dbmod.res_duration = hr*60 + min + sec/60;
                        }

                        dbmod.res_type = "MOD";
    //                    dbmod.res_lan = "ISO-8859-1";
                        dbmod.res_subtype = "AICC_AU";
                        dbmod.res_src_type = "AICC_FILES";
                        dbmod.res_src_link = file_name;
    //                    dbmod.mod_in_eff_end_datetime = Timestamp.valueOf(dbUtils.MAX_TIMESTAMP);
    //                    dbmod.mod_in_eff_start_datetime = Timestamp.valueOf(dbUtils.MIN_TIMESTAMP);

                        mod_id = insModule(con, dbmod, domain, prof);
                        dbmod.updAicc(con, prof, core_vendor, au_password, mod_import_xml, time_limit_action, web_launch, iniFileCRS.getValue("Course_Creator"), dbmod.res_desc, iniFileCRS.getValue("Version"));

                        htAuModID.put(systemID, new Long(mod_id));
                    }

                }
            } catch (Exception e) {
                CommonLog.error(e.getMessage(),e);
                throw new qdbErrMessage("MOD009");
            } finally {
                if(in!=null) {in.close();}
            }
            Vector vtElement = null;

            int idx = 0;
            // input AICC objectives into DB
            for (int i=0; i<vtCosDescriptor.size(); i++) {
                String objSystemID = null;
                String objDeveloperID = null;
                String objTitle = null;
                String objDesc = null;
                dbObjective dbObj = null;

                vtElement = (Vector)vtCosDescriptor.elementAt(i);
                objSystemID = (String)vtElement.elementAt(0);
                if (objSystemID.toLowerCase().startsWith("j") == true) {
                    objDeveloperID = (String)vtElement.elementAt(1);
                    objTitle = (String)vtElement.elementAt(2);
                    objDesc = (String)vtElement.elementAt(3);

                    dbObj = new dbObjective();
                    dbObj.obj_type = "AICC";
                    dbObj.obj_desc = trimObjDesc(objDesc);
                    // if the supplied parent is not empty, use it as the parent of the current objective
                    if (vtParentObj.size() > 0) {
                        dbObj.obj_obj_id_parent = ((Integer) vtParentObj.get(idx++)).longValue();
                    }
                    dbObj.obj_title = objTitle;
                    dbObj.obj_developer_id = objDeveloperID;
                    dbObj.obj_import_xml = "<objective developer_id=\"";
                    dbObj.obj_import_xml += objDeveloperID;
                    dbObj.obj_import_xml += "\" />";
                    int obj_id = (int)dbObj.insAicc(con, prof);
                    htObjObjID.put(objSystemID, new Integer(obj_id));
                }
            }
            if(vtParentObj.size()==0){
                for(int x = 1; x<vtCosDescriptor.size();x++){
                    vtElement = (Vector)vtCosDescriptor.elementAt(x);
                    vtParentObj.addElement((Integer)htObjObjID.get((String)((String)vtElement.elementAt(0))));
                }
            }
            if (vtObjectiveRelation != null) {
                vtElement = null;
                for (int i=0; i<vtObjectiveRelation.size(); i++) {
                    vtElement = (Vector)vtObjectiveRelation.elementAt(i);
                    String parentSystemID = (String)vtElement.elementAt(0);

                    Vector vtObjID = new Vector();
                    String childSystemID = null;
                    for (int j=1; j<vtElement.size(); j++) {
                        childSystemID = (String)vtElement.elementAt(j);
                        vtObjID.addElement((Integer)htObjObjID.get(childSystemID));
                    }

                    long[] intObjID = new long[vtObjID.size()];
                    for (int k=0; k<vtObjID.size(); k++) {
                        intObjID[k] = ((Integer)vtObjID.elementAt(k)).longValue();
                    }

                    long[] int_parent_mod_id_lst = null;
                    if (parentSystemID.startsWith("A") == true || parentSystemID.startsWith("a") == true) {
                        int_parent_mod_id_lst = new long[] { ((Long)htAuModID.get(parentSystemID)).longValue() };
                    }
                    // collect all AU's mod_id in that AICC Block
                    else if (parentSystemID.startsWith("B") == true || parentSystemID.startsWith("b") == true) {
                        Vector vtBlockElements = (Vector)htBlockElements.get(parentSystemID);
                        int_parent_mod_id_lst = new long[vtBlockElements.size()];
                        String tempSystemID = null;
                        for (int m=0; m<vtBlockElements.size(); m++) {
                            tempSystemID = (String)vtBlockElements.elementAt(m);
                            int_parent_mod_id_lst[m] = ((Long)htAuModID.get(tempSystemID)).longValue();
                        }
                    }
                    dbResourceObjective dbResObj = new dbResourceObjective();
                    dbResObj.insResObj(con, int_parent_mod_id_lst, intObjID);

                }
            }

            // update the cos corresponding fields, or reset the fields that is previously set
            // during importing an AICC course which should be reset when inserting/updating an AICC AU
            updAiccCos(con, prof, null, null, null, -1, false, false, false);

            return mod_id;
/*
        } catch(qdbErrMessage e) {
            // rollback at qdbAction
//            con.rollback();
            throw new qdbErrMessage(e.toString());
        }
*/
    }

    public String trimResDesc(String desc) {
        String result = null;
        // 300/2 due to the reason of Unicode
        int sizeLimit = 150;

        if (desc.length() > sizeLimit) {
            result = desc.substring(0, sizeLimit);
        }
        else {
            result = desc;
        }

        return result;
    }

    public String trimObjDesc(String desc) {
        String result = null;
        // 255/2 due to the reason of Unicode
        int sizeLimit = 127;

        if (desc.length() > sizeLimit) {
            result = desc.substring(0, sizeLimit);
        }
        else {
            result = desc;
        }

        return result;
    }

  public void delCourseMod(Connection con, loginProfile prof)
    throws SQLException, qdbException, qdbErrMessage, cwSysMessage {
    // check dependency
    res_id = cos_res_id;
    Vector rcnVec = dbResourceContent.getChildAss(con, cos_res_id);

    for (int i=0;i<rcnVec.size();i++) {
        dbResourceContent dbrcn = (dbResourceContent) rcnVec.elementAt(i);

        long cnt = dbProgress.attemptNum(con, dbrcn.rcn_res_id_content);
        if (cnt > 0)
                //Some of the module had been attempted.
                throw new qdbErrMessage("COS001");

        // delete the module from the course
        dbModule dbmod = new dbModule();
        dbmod.res_id = dbrcn.rcn_res_id_content;
        dbmod.mod_res_id = dbrcn.rcn_res_id_content;
        dbmod.get(con);

        if (dbmod.res_subtype.equals("ASS")) {
            dbAssignment ass = new dbAssignment();
            ass.initialize(dbmod);
            ass.del(con, prof);
        } else if (dbmod.res_subtype.equals("FOR")) {
            dbForum forum = new dbForum(dbmod);
            forum.del(con, prof);
        } else if (dbmod.res_subtype.equals("FAQ")) {
            dbFaq faq = new dbFaq(dbmod);
            faq.del(con, prof);
        } else if (dbEvent.isEventType(dbmod.res_subtype)) {
            dbEvent evt = new dbEvent();
            evt.initialize(dbmod);
            evt.del(con, prof);
        } else {
            dbmod.del(con, prof);
        }

    }


    PreparedStatement stmt = con.prepareStatement(
        " DELETE From Enrolment "
    +   " WHERE enr_res_id = ? " );

    stmt.setLong(1, cos_res_id);
    stmt.executeUpdate();
    stmt.close();

    stmt = con.prepareStatement(
            " DELETE From ResourceContent where rcn_res_id = ? ");

    stmt.setLong(1, cos_res_id);
    stmt.executeUpdate();
    stmt.close();

    stmt = con.prepareStatement(
        " DELETE From ResourceObjective where rob_res_id = ?");

    stmt.setLong(1, cos_res_id);
    stmt.executeUpdate();
    stmt.close();
  }


  public String buildAiccCosStructureXML(Vector vtCosStructure) {
    String result = "";
    String systemID = null;
    Vector vtElement = null;

    if (vtCosStructure.size() > 1) {
        vtElement = (Vector)vtCosStructure.elementAt(0);
        systemID = (String)vtElement.elementAt(0);
    }
    else {
        systemID = (String)vtCosStructure.elementAt(0);
    }

    result += "<element system_id=\"";
    result += systemID;
    result += "\">" + dbUtils.NEWL;
    for (int i=1; i<vtCosStructure.size(); i++) {
        result += buildAiccCosStructureXML((Vector)vtCosStructure.elementAt(i));
    }
    result += "</element>" + dbUtils.NEWL;

    return result;
  }

  public Vector getCosObjectiveVector (String ortFilename) throws IOException, qdbErrMessage {
    String result = null;

    Vector vtObjectiveRelation = new Vector();
    Vector vtElement = null;
    BufferedReader in = null;
    /*
	if( !cwUtils.isUnicodeLittleFile(new File(ortFilename)) ) {
		throw new qdbErrMessage("GEN008");
	}
	*/
	try {
        in = new BufferedReader(new InputStreamReader(cwUtils.openUTF8FileStream(new File(ortFilename)), cwUtils.ENC_UTF));

        String line = null;
        String systemID = null;
        StringTokenizer st = null;

        String tempStr = null;

        // the first line is used to determine the field order
        for (;;) {
            line = in.readLine();
            if (line == null) {
                break;
            }
            else if (line.trim().length() > 0 && line.trim().equalsIgnoreCase("") == false) {
                break;
            }
        }

        for (;;) {
            line = in.readLine();
            if (line == null) {
                break;
            }
            else if (line.trim().length() == 0 || line.trim().equalsIgnoreCase("") == true) {
                continue;
            }
            else {
    /*
                // in case there are empty field with no '"'
                int num_of_quote = 0;
                for (int m=0; m<line.length()-1; m++) {
                    if (line.charAt(m) == '"') {
                        num_of_quote++;
                    }
                    if (line.substring(m,m+2).equalsIgnoreCase(",,") == true && num_of_quote%2 == 0) {
                        line = line.substring(0, m) + ",\"\"," + line.substring(m+2);
                        m = m + 2;
                    }
                }
    */
                vtElement = new Vector();

                Vector vtRecord = buildTableRecord(line);
                String recordElement = null;
                for (int i=0; i<vtRecord.size(); i++) {
                    recordElement = (String)vtRecord.elementAt(i);
                    if (recordElement.length() > 0) {
                        vtElement.addElement(recordElement);
                    }
                }

    /*
                st = new StringTokenizer(line,",");

                while (st.hasMoreTokens()) {
                    systemID = st.nextToken();
                    systemID = systemID.trim();
                    if (systemID.length() == 0) {
                        // can't insert empty fields
                        continue;
                    }
                    else if (systemID.startsWith("\"") == true && systemID.endsWith("\"") == true) {
                        if (systemID.length() > 2) {
                            systemID = systemID.substring(1, systemID.length()-1);
                        }
                        else {
                            // can't insert empty fields
                            continue;
                        }
                    }
                    else if(systemID.length() > 0) {
                        // do nothing
                    }
                    vtElement.addElement(systemID);
                }
    */
                vtObjectiveRelation.addElement(vtElement);
            }

        }
    } catch (Exception e) {
        CommonLog.error(e.getMessage(),e);
        throw new qdbErrMessage("MOD009");
    } finally {
        if(in!=null) {in.close();}
    }
    return vtObjectiveRelation;
  }

  // whether which block contains which AUs
  public Hashtable buildAiccBlockElements(Vector vtMemberRelationLst) {
    Hashtable htBlockElement = new Hashtable();
    String parentSystemID = null;
    String childSystemID = null;
    Vector vtTemp = null;
    for (int i=1; i<vtMemberRelationLst.size(); i++) {
        vtTemp = (Vector)vtMemberRelationLst.elementAt(i);
        parentSystemID = (String)vtTemp.elementAt(0);
        if (parentSystemID.startsWith("B") == true || parentSystemID.startsWith("b") == true) {
            Vector vtElements = new Vector();
            getAllBlockElements(parentSystemID, vtElements, vtMemberRelationLst);
            CommonLog.debug("getAllBlockElements size:" + parentSystemID + ":" + vtElements.size());
            htBlockElement.put(parentSystemID, vtElements);
        }
        else {
            // do nothing
        }
    }
    return htBlockElement;
  }

  public void getAllBlockElements(String parentSystemID, Vector vtElements, Vector vtMemberRelationLst) {
    String childSystemID = null;
    String tempSystemID = null;
    Vector vtTemp = null;
    for (int i=1; i<vtMemberRelationLst.size(); i++) {
        vtTemp = (Vector)vtMemberRelationLst.elementAt(i);
        childSystemID = (String)vtTemp.elementAt(0);
        if (childSystemID.equalsIgnoreCase(parentSystemID) == true) {
            for (int j=1; j<vtTemp.size(); j++) {
                tempSystemID = (String)vtTemp.elementAt(j);
                if (tempSystemID.startsWith("B") == true || tempSystemID.startsWith("b") == true) {
                    getAllBlockElements(tempSystemID, vtElements, vtMemberRelationLst);
                }
                else {
                    vtElements.addElement(tempSystemID);
                }
            }
            break;
        }
        else {
            // do nothing
        }
    }
  }

  public void buildAiccCosStructureVector(Vector vtParent, Vector vtMemberRelationLst) {
    String parentSystemID = null;
    String childSystemID = null;
    boolean match = false;
    Vector vtTemp = null;

    parentSystemID = (String)vtParent.elementAt(0);
    vtTemp = new Vector();
    vtTemp.addElement(parentSystemID);
    vtParent.setElementAt(vtTemp, 0);

    for (int i=1; i<vtParent.size(); i++) {
        parentSystemID = (String)vtParent.elementAt(i);
        match = false;
        for (int j=0; j<vtMemberRelationLst.size(); j++) {
            vtTemp = (Vector)vtMemberRelationLst.elementAt(j);
            vtTemp = (Vector)vtTemp.clone();
            childSystemID = (String)vtTemp.elementAt(0);
            if (parentSystemID.equalsIgnoreCase(childSystemID) == true) {
                vtParent.setElementAt(vtTemp, i);
                buildAiccCosStructureVector(vtTemp, vtMemberRelationLst);
                match = true;
                break;
            }
        }
        if (match == false) {
            vtTemp = new Vector();
            vtTemp.addElement(parentSystemID);
            vtParent.setElementAt(vtTemp, i);
        }
    }
  }

    static public Vector buildAuRecord(String auFilename) throws qdbException, IOException {
        File aicc_au_file = new File(auFilename);
        BufferedReader in = null;
        try {
        	/*
			if( !cwUtils.isUnicodeLittleFile(aicc_au_file) ) {
				throw new qdbErrMessage("GEN008");
			}
			*/
        	in = new BufferedReader(new InputStreamReader(cwUtils.openUTF8FileStream(aicc_au_file), cwUtils.ENC_UTF));
        } catch(Exception e) {
            throw new IOException(e.toString());
        }
        String line = null;
        String systemID = null;
        String type = null;
        String command_line = null;
        String file_name = null;
        String max_score = null;
        String mastery_score = null;
        String max_time_allowed = null;
        String time_limit_action = null;
        String system_vendor = null;
        String core_vendor = null;
        String web_launch = null;
        String au_password = null;

        String tempStr = null;

        Hashtable htAuFieldOrder = null;
        Hashtable htAuFieldValue = null;

        Vector vtAuRecordList = new Vector();

        StringTokenizer st = null;
        // the first line is used to determine the field order
        for (;;) {
            try {
                line = in.readLine();
            } catch(Exception e) {
                throw new IOException(e.toString());
            }
            if (line == null) {
                break;
            }
            else if (line.trim().length() > 0 && line.trim().equalsIgnoreCase("") == false) {
                htAuFieldOrder = new Hashtable();

                int index = 1;

                Vector vtRecord = buildTableRecord(line);
                String recordElement = null;
                for (int i=0; i<vtRecord.size(); i++) {
                    recordElement = (String)vtRecord.elementAt(i);
                    htAuFieldOrder.put(recordElement.toLowerCase(), new Integer(index));
                    index++;
                }

                break;
            }
        }

        // the first element is the column label
        vtAuRecordList.addElement(htAuFieldOrder);

        for (;;) {
            try {
                line = in.readLine();
            } catch(Exception e) {
                throw new IOException(e.toString());
            }
            if (line == null) {
                break;
            }
            else if (line.trim().length() == 0 || line.trim().equalsIgnoreCase("") == true) {
                continue;
            }
            else {
                htAuFieldValue = new Hashtable();

                int index = 1;

                Vector vtRecord = buildTableRecord(line);
                String recordElement = null;
                for (int i=0; i<vtRecord.size(); i++) {
                    recordElement = (String)vtRecord.elementAt(i);
                    htAuFieldValue.put(new Integer(index), recordElement);
                    index++;
                }

                vtAuRecordList.addElement(htAuFieldValue);
            }

        }

        in.close();

        return vtAuRecordList;
    }

  static public Vector buildCosDescriptorVector(String desFilename) throws IOException, qdbErrMessage{
    String result = null;

    Vector vtCosDescriptor = new Vector();
    Vector vtElement = null;
    BufferedReader in = null;
    /*
	if( !cwUtils.isUnicodeLittleFile(new File(desFilename)) ) {
		throw new qdbErrMessage("GEN008");
	}
	*/
    try {
    	in = new BufferedReader(new InputStreamReader(cwUtils.openUTF8FileStream(new File(desFilename)), cwUtils.ENC_UTF));

        String line = null;
        String systemID = null;
        String developerID = null;
        String title = null;
        String description = null;
        String externalID = null;
        StringTokenizer st = null;

        String tempStr = null;

        Hashtable htDesFieldOrder = null;
        Hashtable htDesFieldValue = null;

        // the first line is used to determine the field order
        for (;;) {
            line = in.readLine();
            if (line == null) {
                break;
            }
            else if (line.trim().length() > 0 && line.trim().equalsIgnoreCase("") == false) {
                htDesFieldOrder = new Hashtable();

                int index = 1;

                Vector vtRecord = buildTableRecord(line);
                String recordElement = null;
                for (int i=0; i<vtRecord.size(); i++) {
                    recordElement = (String)vtRecord.elementAt(i);
                    htDesFieldOrder.put(recordElement.toLowerCase(), new Integer(index));
                    index++;
                }

                break;
            }
        }

        for (;;) {
            line = in.readLine();
            if (line == null) {
                break;
            }
            else if (line.trim().length() == 0 || line.trim().equalsIgnoreCase("") == true) {
                continue;
            }
            else {
                // new method
                htDesFieldValue = new Hashtable();

                int index = 1;

                Vector vtRecord = buildTableRecord(line);
                for (int i=0; i<vtRecord.size(); i++) {
                    htDesFieldValue.put(new Integer(index), (String)vtRecord.elementAt(i));
                    index++;
                }

                Integer fieldIndex = null;

                fieldIndex = (Integer)htDesFieldOrder.get("system_id");
                systemID = dbUtils.esc4XML((String)htDesFieldValue.get(fieldIndex));

                fieldIndex = (Integer)htDesFieldOrder.get("developer_id");
                developerID = dbUtils.esc4XML((String)htDesFieldValue.get(fieldIndex));

                fieldIndex = (Integer)htDesFieldOrder.get("title");
                title = (String)htDesFieldValue.get(fieldIndex);

                fieldIndex = (Integer)htDesFieldOrder.get("description");
                description = (String)htDesFieldValue.get(fieldIndex);

                vtElement = new Vector();
                vtElement.addElement(systemID);
                vtElement.addElement(developerID);
                vtElement.addElement(title);
                vtElement.addElement(description);

                vtCosDescriptor.addElement(vtElement);
            }

        }
    } catch (Exception e) {
        CommonLog.error(e.getMessage(),e);
        throw new qdbErrMessage("MOD009");
    } finally {
        if(in!=null) {in.close();}
    }
    return vtCosDescriptor;
  }

  public Vector getMemberRelationLst(String cstFilename) throws IOException, qdbErrMessage {
    Vector vtMemberRelationLst = new Vector();
    Vector vtMemberRelation = null;

    BufferedReader in = null;
    /*
	if( !cwUtils.isUnicodeLittleFile(new File(cstFilename)) ) {
		throw new qdbErrMessage("GEN008");
	}
	*/
	try {
        in = new BufferedReader(new InputStreamReader(cwUtils.openUTF8FileStream(new File(cstFilename)), cwUtils.ENC_UTF));

        String line = null;
        String systemID = null;
        StringTokenizer st = null;
        // the first line is useless
        for (;;) {
            try {
                line = in.readLine();
            } catch(Exception e) {
                throw new IOException(e.toString());
            }
            if (line == null) {
                break;
            }
            else if (line.trim().length() > 0 && line.trim().equalsIgnoreCase("") == false) {
                break;
            }
        }
        for (;;) {
            line = in.readLine();
            if (line == null) {
                break;
            }
            else if (line.trim().length() == 0 || line.trim().equalsIgnoreCase("") == true) {
                continue;
            }
            else {
                vtMemberRelation = new Vector();
                Vector vtRecord = buildTableRecord(line);
                String recordElement = null;
                for (int i=0; i<vtRecord.size(); i++) {
                    recordElement = (String)vtRecord.elementAt(i);
                    if (recordElement.length() > 0) {
                        vtMemberRelation.addElement(recordElement);
                    }
                }
                vtMemberRelationLst.addElement(vtMemberRelation);
            }

        }
    } catch (Exception e) {
        CommonLog.error(e.getMessage(),e);
        throw new qdbErrMessage("MOD009");
    } finally {
        if(in!=null) {in.close();}
    }
    return vtMemberRelationLst;
  }

  public Vector getCosStructureVector (String cstFilename) throws IOException, qdbErrMessage{
    // for the course structure file
    Vector vtMemberRelationLst = getMemberRelationLst(cstFilename);

    Vector vtCosStructure = new Vector();
    Vector vtTemp = null;
    String systemID = null;
    for (int i=0; i<vtMemberRelationLst.size(); i++) {
        vtTemp = (Vector)vtMemberRelationLst.elementAt(i);
        systemID = (String)vtTemp.elementAt(0);
        if (systemID.equalsIgnoreCase("root") == true) {
            break;
        }
    }
    if (vtTemp == null) {
        return null;
    }
    else {
        vtCosStructure = (Vector)vtTemp.clone();
        buildAiccCosStructureVector(vtCosStructure, vtMemberRelationLst);
    }
    return vtCosStructure;
  }

  public String buildRecursiveCosStructXML(Vector vtCosStructure, Vector vtCosDescriptor, Hashtable htAuModID) {
    String result = "";
    String systemID = null;
    Long mod_id = null;
    int itemID = 0;
    Vector vtElement = null;

    if (vtCosStructure.size() > 1) {
        vtElement = (Vector)vtCosStructure.elementAt(0);
        systemID = (String)vtElement.elementAt(0);
    }
    else {
        systemID = (String)vtCosStructure.elementAt(0);
    }

    Vector vtTemp = null;
    for (int i=0; i<vtCosDescriptor.size(); i++) {
        vtTemp = (Vector)vtCosDescriptor.elementAt(i);
        if (systemID.equalsIgnoreCase((String)vtTemp.elementAt(0)) == true) {
            itemID = i+1;
            break;
        }
        else {
            vtTemp = null;
        }
    }

    result += "<item identifier=\"";
    result += "ITEM" + Integer.toString(itemID);
    if (systemID.toLowerCase().startsWith("b") == true) {
        // do nothing for block
    }
    else {
        result += "\" identifierref=\"";
        mod_id = (Long)htAuModID.get(systemID);
        result += mod_id.longValue();
    }
    result += "\" title=\"";
    result += (String)vtTemp.elementAt(2);
    result += "\">" + dbUtils.NEWL;

    if (systemID.toLowerCase().startsWith("b") == true) {
        // do nothing for block
        result += "<itemtype>";
        result += "FDR";
        result += "</itemtype>" + dbUtils.NEWL;
    }
    else {
        result += "<itemtype>";
        result += "MOD";
        result += "</itemtype>" + dbUtils.NEWL;
        result += "<restype>";
        result += "AICC_AU";
        result += "</restype>" + dbUtils.NEWL;
    }

    for (int i=1; i<vtCosStructure.size(); i++) {
        result += buildRecursiveCosStructXML((Vector)vtCosStructure.elementAt(i), vtCosDescriptor, htAuModID);
    }
    result += "</item>" + dbUtils.NEWL;

    return result;
  }

  public static String getCosTitle(Connection con, long res_id) throws SQLException{
    String course_title = null;
    PreparedStatement cos_stmt = con.prepareStatement(GET_COS_TITLE);
    cos_stmt.setLong(1, res_id);
    ResultSet rs = cos_stmt.executeQuery();
    if(rs.next()) {
        course_title = rs.getString("RTITLE");
    }else{
        course_title = "";
    }
    cos_stmt.close();

    return course_title;
  }

    /**
    * Get course title by course item id
    * @param item id
    * @return String of the course title
    */
    public static String getCourseTitle(Connection con, long itm_id)
        throws SQLException, cwException {
            String SQL = " SELECT res_title "
                       + " FROM Resources, Course "
                       + " WHERE res_id = cos_res_id AND cos_itm_id = ? ";
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, itm_id);
            String title = null;
            ResultSet rs = stmt.executeQuery();
            if( rs.next() )
                title = rs.getString("res_title");
            else
            {
                stmt.close();
                throw new cwException("Failed to get the course title, itm id = " + itm_id);
            }
            stmt.close();
            return title;
        }


  public String buildAiccCosStructure(Connection con, Vector vtCosStructure, Vector vtCosDescriptor, Hashtable htAuModID) throws SQLException {
    String result = "";

    String course_title = null;
    PreparedStatement cos_stmt = con.prepareStatement(GET_COS_TITLE);
    cos_stmt.setLong(1, cos_res_id);
    ResultSet cos_rs = cos_stmt.executeQuery();
    if(cos_rs.next()) {
        course_title = cos_rs.getString("RTITLE");
    }

    result += "<tableofcontents identifier=\"";
    result += "TOC1";
    result += "\" title=\"";
    result += dbUtils.esc4XML(course_title);
    result += "\">" + dbUtils.NEWL;

    for (int i=1; i<vtCosStructure.size(); i++) {
        result += buildRecursiveCosStructXML((Vector)vtCosStructure.elementAt(i), vtCosDescriptor, htAuModID);
    }

    result += "</tableofcontents>";
    cos_stmt.close();
    return result;
  }

  public String buildAiccCosImportXML(crsIniFile iniFileCRS, Vector vtCosStructure, Vector vtCosDescriptor, String auFilename, Hashtable htAuModID, Hashtable htObjObjID) {
    String result = "";

    result += "<cos_import_xml>" + dbUtils.NEWL;
    result += "<course_gen_description>" + dbUtils.NEWL;
    result += "<course ";
    result += "Course_Creator=\"";
    result += iniFileCRS.getValue("Course_Creator");
    result += "\" Course_ID=\"";
    result += iniFileCRS.getValue("Course_ID");
    result += "\" Course_System=\"";
    result += iniFileCRS.getValue("Course_System");
    result += "\" Course_Title=\"";
    result += iniFileCRS.getValue("Course_Title");
    result += "\" Level=\"";
    result += iniFileCRS.getValue("Level");
    result += "\" Max_Fields_CST=\"";
    result += iniFileCRS.getValue("Max_Fields_CST");
    result += "\" Max_Fields_ORT=\"";
    result += iniFileCRS.getValue("Max_Fields_ORT");
    result += "\" Total_AUs=\"";
    result += iniFileCRS.getValue("Total_AUs");
    result += "\" Total_Blocks=\"";
    result += iniFileCRS.getValue("Total_Blocks");
    result += "\" Total_Objectives=\"";
    result += iniFileCRS.getValue("Total_Objectives");
    result += "\" Total_Complex_Obj=\"";
    result += iniFileCRS.getValue("Total_Complex_Obj");
    result += "\" Version=\"";
    result += iniFileCRS.getValue("Version");
    result += "\" />" + dbUtils.NEWL;

    result += "<course_behavior ";
    result += "Max_Normal=\"";
    result += iniFileCRS.getValue("Max_Normal");
    result += "\" />" + dbUtils.NEWL;

    result += "<course_description";
    Vector vtCosDes = iniFileCRS.getCosDesc();
    for (int i=0; i<vtCosDes.size(); i++) {
        result += " desc" + Integer.toString(i+1) + "=\"";
        result += (String)vtCosDes.elementAt(i);
        result += "\"";
    }
    result += " />" + dbUtils.NEWL;

    result += "</course_gen_description>" + dbUtils.NEWL;

//    Vector vtCosStructure = getCosStructureVector(cstFilename);
    result += "<course_structure>" + dbUtils.NEWL;
    result += buildAiccCosStructureXML(vtCosStructure) + dbUtils.NEWL;
    result += "</course_structure>" + dbUtils.NEWL;


    // for the descriptor file
//    Vector vtCosDescriptor = buildCosDescriptorVector(desFilename);
    Vector vtElement = null;

    result += "<descriptor>" + dbUtils.NEWL;
    for (int i=0; i<vtCosDescriptor.size(); i++) {
        vtElement = (Vector)vtCosDescriptor.elementAt(i);

        result += "<element";
        result += " system_id=\"";
        result += (String)vtElement.elementAt(0);
        result += "\" developer_id=\"";
        result += (String)vtElement.elementAt(1);
        result += "\" title=\"";
        result += (String)vtElement.elementAt(2);
        result += "\" description=\"";
        result += (String)vtElement.elementAt(3);
        result += "\" external_id=\"";
        String systemID = (String)vtElement.elementAt(0);
        if (systemID.toLowerCase().startsWith("a") == true) {
            if (htAuModID.get(systemID) != null) {
                Long lg_mod_id = (Long)htAuModID.get(systemID);
                result += lg_mod_id.toString();
            }
            else {
                result += "";
            }
        }
        else if (systemID.toLowerCase().startsWith("j") == true) {
            if (htObjObjID.get(systemID) != null) {
                Integer lg_obj_id = (Integer)htObjObjID.get(systemID);
                result += lg_obj_id.toString();
            }
            else {
                result += "";
            }
        }
        else {
            result += "";
        }
        result += "\" />" + dbUtils.NEWL;
    }

    result += "</descriptor>" + dbUtils.NEWL;

    result += "</cos_import_xml>" + dbUtils.NEWL;

    return result;
  }

  static public Vector buildTableRecord(String line) {
    Vector vtRecord = new Vector();
    String tempStr = "";

    char dbQuoteDelimiter = '"';
    char commaDelimiter = ',';
    boolean boolWithinQuote = false;
    boolean boolFieldFinish = false;
    int fieldStartIndex = 0;
    int fieldEndIndex = 0;

    for (int m=0; m<line.length(); m++) {
        if (line.charAt(m) == dbQuoteDelimiter) {
            if (boolWithinQuote == false) {
                boolWithinQuote = true;
                fieldStartIndex = m + 1;
            }
            else if (boolWithinQuote == true) {
                boolWithinQuote = false;
                boolFieldFinish = true;
                fieldEndIndex = m - 1;
            }
        }
        if (line.charAt(m) == commaDelimiter || m == line.length()-1) {
            // find the end of a field
            if (boolWithinQuote == false) {
                if (boolFieldFinish == true) {
                    // do nothing
                }
                else {
                    // special case for last element
                    if (m == line.length()-1 && line.charAt(m) != commaDelimiter){
                        fieldEndIndex = m;
                    }
                    // normal case
                    else {
                        fieldEndIndex = m - 1;
                    }
                }

                if (fieldEndIndex < fieldStartIndex) {
                    tempStr = "";
                }
                else {
                    // special case for last element
                    tempStr = line.substring(fieldStartIndex, fieldEndIndex+1);

                    // comma as delimiter, so escape the white space
                    if (boolFieldFinish == false) {
                        tempStr = tempStr.trim();
                        if (tempStr.length() == 0) {
                            tempStr = "";
                        }
                    }
                    // double quote as delimiter, so preserver the only white space
                    else {
                        // do nothing
                    }
                }

//                vtRecord.addElement(dbUtils.esc4XML(tempStr));
                vtRecord.addElement(tempStr);

                // reset all flag
                fieldStartIndex = m + 1;
                boolFieldFinish = false;
            }
            else if (boolWithinQuote == true) {
                // do nothing
            }
        }
    }

    return vtRecord;
  }

    /*
    Get the list of modules of the given type and belonging to those course
    @param course id list
    @param moduel type list
    */
    private static final String sql_get_module_list =
        " Select cos_res_id AS CID, LCOS.res_title AS CTITLE, mod_res_id AS MID, LMOD.res_title AS MTITLE "
        + " From ResourceContent, Module, Resources LMOD, Resources LCOS, Course "
        + " Where mod_res_id = rcn_res_id_content And mod_res_id = LMOD.res_id "
        + " And cos_res_id = rcn_res_id And cos_res_id = LCOS.res_id ";


    public static String getModuleListAsXML(Connection con, long[] cos_id_lst, String[] mod_type_lst) throws SQLException
    {
        String sql = sql_get_module_list;
        sql += " And rcn_res_id IN " + cwUtils.array2list(cos_id_lst);

        if (mod_type_lst != null && mod_type_lst.length > 0) {
            sql +=  " And mod_type IN " + cwUtils.array2list(mod_type_lst);
        }

        sql += " Order by mod_eff_start_datetime desc ";

        PreparedStatement stmt = con.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        StringBuffer xml = new StringBuffer();
        long mod_id = 0;
        Hashtable modHash = new Hashtable();
        Vector modVec = new Vector();
        while (rs.next()){
            StringBuffer modXML = new StringBuffer();
            mod_id = rs.getLong("MID");
            modXML.append("<module id=\"").append(mod_id)
               .append("\" title=\"").append(cwUtils.esc4XML(rs.getString("MTITLE")))
               .append("\" course_id=\"").append(rs.getLong("CID"))
               .append("\" cos_title=\"").append(cwUtils.esc4XML(rs.getString("CTITLE")))
               .append("\" ");
            modHash.put(new Long(mod_id), modXML.toString());
            modVec.addElement(new Long(mod_id));
        }

        Hashtable modPgrHash = null;
        if (modVec.size() > 0) {
            modPgrHash = dbProgress.totalAttemptNum(con, modVec);
        }

        for (int i=0;i<modVec.size();i++) {
            mod_id = ((Long)modVec.elementAt(i)).longValue();
            xml.append((String) modHash.get(new Long(mod_id)))
               .append("attempt_num=\"");
            Long attempt_num = (Long) modPgrHash.get(new Long(mod_id));
            if (attempt_num != null) {
                xml.append(attempt_num.toString());
            }else {
                xml.append("0");
            }
            xml.append("\"/>").append(cwUtils.NEWL);
        }
        rs.close();
        stmt.close();

        return xml.toString();
    }

    /*
    Check if a course can be quick referenced i.e. lauch the course without enrollment
    */
    public static boolean canQuickReference(Connection con, long cosId)
        throws SQLException {

            boolean canQR = false;
            String SQL = " SELECT itm_can_qr_ind From Course, aeItem "
                    + " WHERE cos_itm_id = itm_id "
                    + " AND cos_res_id = ? ";
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, cosId);
            ResultSet rs = stmt.executeQuery();
            String tkh_type = null;
            if(rs.next()) {
                canQR = rs.getBoolean("itm_can_qr_ind");
            }
            stmt.close();

            return canQR;
        }

    /**
     * get cos_res_id of all child courses of a classroom course that is using common content
     */
    public Vector getChildCosResId(Connection con) throws SQLException {
        Vector vtId = new Vector();
        String SQL =
        "select cos_res_id from aeItemRelation, Course where ire_parent_itm_id in "
        + "(select cos_itm_id from Course, aeItem where "
        + "cos_res_id = ? and cos_itm_id = itm_id and itm_type = ? and itm_content_def = ? and itm_create_run_ind = ? ) "
        + "and ire_child_itm_id = cos_itm_id ";
        int idx = 1;
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(idx++, this.cos_res_id);
        stmt.setString(idx++, "CLASSROOM");
        stmt.setString(idx++, "PARENT");
        stmt.setBoolean(idx++, true);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            vtId.add(new dbCourse(rs.getLong(1)));
        }
        stmt.close();
        return vtId;
    }

    public String  getCosItemType(Connection con, long cos_res_id) throws SQLException {
        String  itm_type = "";
        String SQL = "select itm_type from Course, aeItem where "
                + "cos_res_id = ? and cos_itm_id = itm_id ";
        int idx = 1;
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(idx++, cos_res_id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            itm_type = rs.getString("itm_type");
        }
        stmt.close();
        return itm_type;
    }

    public void updCosStructureFromParent(Connection con, String parent_xml) throws SQLException, qdbException, cwException {
        String SQL = "select mod_res_id, mod_mod_res_id_parent from ResourceContent, Module "
                   + "where rcn_res_id = ? and rcn_res_id_content = mod_res_id";
        int idx = 1;
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(idx++, this.cos_res_id);
        ResultSet rs = stmt.executeQuery();
        Vector vtModId = new Vector();
        Vector vtModIdPar = new Vector();
        while (rs.next()) {
            vtModId.add(new Long(rs.getLong("mod_res_id")));
            vtModIdPar.add(new Long(rs.getLong("mod_mod_res_id_parent")));
        }
        stmt.close();

        this.cos_structure_xml = parent_xml;
        for (int j = 0; j < vtModId.size(); j++) {
            /*if (j == 0) {
                String ModIdPar = " identifierref=\"" + ScormContentParser.SCOROOT + ((Long) vtModIdPar.get(j)).longValue() + "\"";
                String ModId = " identifierref=\"" + ScormContentParser.SCOROOT + ((Long) vtModId.get(j)).longValue() + "\"";
                this.cos_structure_xml = dbUtils.subsitute(this.cos_structure_xml, ModIdPar, ModId);
            }*/
            String ModIdPar = " identifierref=\"" + ((Long) vtModIdPar.get(j)).longValue() + "\"";
            String ModId = " identifierref=\"" + ((Long) vtModId.get(j)).longValue() + "\"";
            this.cos_structure_xml = dbUtils.subsitute(this.cos_structure_xml, ModIdPar, ModId);
        }
        if(this.cos_structure_xml !=null && this.cos_structure_xml.length()>0){
        	this.cos_structure_json=qdbAction.static_env.transformXML(this.cos_structure_xml.replaceAll("&quot;", " "), "cos_structure_json_js.xsl", null);
        }

        this.updCosStructure(con);
    }
    public static long getCosItmTcrId(Connection con, long cos_id) throws SQLException {
    	String sql = "Select itm_tcr_id From aeItem, Course where cos_itm_id = itm_id and cos_res_id = ? ";
    	PreparedStatement stmt = con.prepareStatement(sql);
    	int index = 1;
    	stmt.setLong(index++, cos_id);
    	ResultSet rs = stmt.executeQuery();
    	long itm_tcr_id = 0;
    	if(rs.next()) {
    		itm_tcr_id = rs.getLong("itm_tcr_id");
    	}
    	stmt.close();
    	return itm_tcr_id;
    }

    public static Hashtable getCosStructureXmlMap(Connection con) throws SQLException {
    	Hashtable cosStructureXmlHash = new Hashtable();
    	PreparedStatement stmt = null;
    	ResultSet rs = null;

    	try {
    		stmt = con.prepareStatement(SqlStatements.getCosStructureXml());
    		rs = stmt.executeQuery();
    		while(rs.next()) {
    			cosStructureXmlHash.put(new Long(rs.getLong("cos_res_id")), rs.getString("cos_structure_xml"));
    		}
    	} finally {
    		cwSQL.closePreparedStatement(stmt);
    	}

    	return cosStructureXmlHash;
    }

    public static void updCosStructureJson(Connection con, long resId, String cosStructureJson) throws SQLException {
		String condition = "cos_res_id = " + resId;
        String columnName[]={"cos_structure_json"};
	    String columnValue[]={cosStructureJson};
        cwSQL.updateClobFields(con, "Course", columnName, columnValue, condition);
    }

    public static String transAiccSrcUrl(String au_filename, String org_src_url) {
        if (org_src_url.toLowerCase().startsWith("http://")) {
            return org_src_url;
        }
        org_src_url = org_src_url.replaceAll("\\\\", "/");
        File f = new File(au_filename);
        String au_filepath = f.getParent();
        String tmp_path = au_filepath.substring(au_filepath.lastIndexOf(cwUtils.SLASH)+1,au_filepath.length());
        String new_path = "../content/" + tmp_path;
        if (org_src_url.startsWith("/") || org_src_url.startsWith(".")) {
            new_path += org_src_url.substring(org_src_url.indexOf("/"));
        } else {
            new_path += "/" + org_src_url;
        }
        return new_path;
    }

    public String getVodCosContentasXML(Connection con, loginProfile prof) throws qdbException, SQLException, cwSysMessage {
		StringBuffer xml = new StringBuffer();

		aeItem itm = new aeItem();
		itm.itm_id = cos_itm_id;
		itm.get(con);
		xml.append(dbUtils.xmlHeader);
		xml.append("<course>");
		xml.append(prof.asXML()).append(dbUtils.NEWL);
		xml.append("<mod_vod").append(" res_id=\"").append(res_id).append("\"").append(" itm_id=\"").append(cos_itm_id).append("\"").append(" itm_title=\"").append(
				dbUtils.esc4XML(itm.itm_title)).append("\"").append(" tkh_id=\"").append(tkh_id).append("\"");
		xml.append(">");
		xml.append("</mod_vod>" + dbUtils.NEWL);
		xml.append(itm.itm_xml);
		xml.append(cos_structure_xml).append(dbUtils.NEWL);
		xml.append("</course>" + dbUtils.NEWL);
		return xml.toString();
	}
	 public String getVodCosRptasXML(Connection con,loginProfile prof) throws qdbException, SQLException, cwSysMessage {
		StringBuffer xml = new StringBuffer();
		 String sql = " select res_id,res_title,res_vod_duration,res_type,res_subtype,res_src_link,res_img_link,mov_status,mov_last_acc_datetime from resources ";
		 sql += " inner join resourcecontent on (rcn_res_id_content =res_id )";
		 sql += " left join moduleevaluation on (mov_mod_id = res_id and mov_ent_id = ? and mov_tkh_id = ?)";
		 sql += " where  rcn_res_id = ?  order by res_id desc";

		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setLong(1, prof.usr_ent_id);
		stmt.setLong(2, tkh_id);
		stmt.setLong(3, res_id);
		ResultSet rs = stmt.executeQuery();
		aeItem itm = new aeItem();
		itm.itm_id = cos_itm_id;
		itm.get(con);
		xml.append(dbUtils.xmlHeader);
		xml.append("<course>");
		xml.append(prof.asXML()).append(dbUtils.NEWL);
		xml.append("<mod_vod").append(" res_id=\"").append(res_id).append("\"")
		.append(" itm_id=\"").append(cos_itm_id).append("\"")
		.append(" itm_title=\"").append(dbUtils.esc4XML(itm.itm_title)).append("\"")
		.append(" tkh_id=\"").append(tkh_id).append("\"");
		xml.append(">");
	   	while (rs.next()) {
	   		 xml.append("<mod_vod_cos")
	   	        .append(" res_id=\"").append(rs.getLong("res_id")).append("\"")
	   	        .append(" res_title=\"").append(dbUtils.esc4XML(rs.getString("res_title"))).append("\"")
	   	        .append(" res_vod_duration=\"").append(rs.getLong("res_vod_duration")).append("\"")
	   	        .append(" mov_status=\"").append(dbUtils.esc4XML(rs.getString("mov_status"))).append("\"")
	   	        .append(" res_type=\"").append(dbUtils.esc4XML(rs.getString("res_type"))).append("\"")
	   	        .append(" res_subtype=\"").append(dbUtils.esc4XML(rs.getString("res_subtype"))).append("\"")
	   	        .append(" res_src_link=\"").append(dbUtils.esc4XML(rs.getString("res_src_link"))).append("\"")
	   	        .append(" res_img_link=\"").append(dbUtils.esc4XML(rs.getString("res_img_link"))).append("\"")
	   	        .append(" mov_last_acc_datetime=\"").append(aeUtils.escNull(rs.getTimestamp("mov_last_acc_datetime"))).append("\"")
	   	        .append(">");
	   		 xml.append("</mod_vod_cos>");
	   	}
	   	cwSQL.cleanUp(rs, stmt);
	   	xml.append("</mod_vod>" + dbUtils.NEWL);
	   	xml.append(cos_structure_xml).append(dbUtils.NEWL);
	   	xml.append("</course>" + dbUtils.NEWL);
		return xml.toString();
	}
	 public String getVodCosasXML(Connection con,loginProfile prof,dbCourse dbcos,boolean is_menu) throws qdbException, SQLException {
		StringBuffer xml = new StringBuffer();
		 String sql = " select res_id,res_title,res_vod_duration,res_type,res_subtype,res_src_link,res_img_link,mov_status,mov_last_acc_datetime from resources ";
		 sql += " inner join resourcecontent on (rcn_res_id_content =res_id )";
		 sql += " left join moduleevaluation on (mov_mod_id = res_id and mov_ent_id = ? and mov_tkh_id = ?)";
		 sql += " where  rcn_res_id = ? order by res_id desc";

		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setLong(1, prof.usr_ent_id);
		stmt.setLong(2, tkh_id);
		stmt.setLong(3, res_id);
		ResultSet rs = stmt.executeQuery();
		xml.append(dbUtils.xmlHeader);
		xml.append("<course>");
		xml.append(prof.asXML()).append(dbUtils.NEWL);
		xml.append("<mod_vod").append(" res_id=\"").append(res_id).append("\"")
		.append(" itm_id=\"").append(cos_itm_id).append("\"")
		.append(" tkh_id=\"").append(tkh_id).append("\"");
		if(!is_menu){
			xml.append(" chapter_id=\"").append(dbUtils.esc4XML(dbcos.cos_chapter_id)).append("\"")
			.append(" node_id=\"").append(dbUtils.esc4XML(dbcos.cos_node_id)).append("\"")
			.append(" vod_res_id=\"").append(dbcos.vod_res_id).append("\"");
		}
		xml.append(" is_click_node=\"").append(dbcos.is_click_node).append("\"");
		xml.append(">");
        	while (rs.next()) {
        		 xml.append("<mod_vod_cos")
        	        .append(" res_id=\"").append(rs.getLong("res_id")).append("\"")
        	        .append(" res_title=\"").append(dbUtils.esc4XML(rs.getString("res_title"))).append("\"")
        	        .append(" res_vod_duration=\"").append(rs.getLong("res_vod_duration")).append("\"")
        	        .append(" mov_status=\"").append(dbUtils.esc4XML(rs.getString("mov_status"))).append("\"")
        	        .append(" res_type=\"").append(dbUtils.esc4XML(rs.getString("res_type"))).append("\"")
        	        .append(" res_subtype=\"").append(dbUtils.esc4XML(rs.getString("res_subtype"))).append("\"")
        	        .append(" res_src_link=\"").append(dbUtils.esc4XML(rs.getString("res_src_link"))).append("\"")
        	        .append(" res_img_link=\"").append(dbUtils.esc4XML(rs.getString("res_img_link"))).append("\"")
        	        .append(" mov_last_acc_datetime=\"").append(aeUtils.escNull(rs.getTimestamp("mov_last_acc_datetime"))).append("\"")
        	        .append(">");
        		 xml.append("</mod_vod_cos>");
        	}
        	cwSQL.cleanUp(rs, stmt);
        	xml.append("</mod_vod>" + dbUtils.NEWL);
        	xml.append(cos_structure_xml).append(dbUtils.NEWL);
        	if (is_menu) {
        		xml.append(getvodLearnRecordasXML(con));
			}
        	xml.append("</course>" + dbUtils.NEWL);
		return xml.toString();
	}
	 public String getvodLearnRecordasXML(Connection con) throws qdbException, SQLException {
			StringBuffer xml = new StringBuffer();

			String sql = " select vlr_id,vlr_tkh_id,vlr_chapter_id,vlr_node_id,vlr_node_vod_res_id,vlr_update_timestamp,res_src_link,res_img_link ";
			 sql += "  from vodLearnRecord ";
			 sql += " inner join resources on (res_id = vlr_node_vod_res_id)";
			 sql += " where vlr_tkh_id = ?";
			 sql += " and vlr_update_timestamp = (";
			 sql += " 		select max(vlr.vlr_update_timestamp) from vodLearnRecord vlr where vlr.vlr_tkh_id = ? group by vlr.vlr_tkh_id";
			 sql += " )";

			 PreparedStatement stmt = con.prepareStatement(sql);
				stmt.setLong(1, tkh_id);
				stmt.setLong(2, tkh_id);
				ResultSet rs = stmt.executeQuery();
				if (rs.next()) {
		       		 xml.append("<vod_lrn_record")
		       	        .append(" vlr_id=\"").append(rs.getLong("vlr_id")).append("\"")
		       	        .append(" vlr_tkh_id=\"").append(rs.getLong("vlr_tkh_id")).append("\"")
		       	        .append(" vlr_chapter_id=\"").append(dbUtils.esc4XML(rs.getString("vlr_chapter_id"))).append("\"")
		       	        .append(" vlr_node_id=\"").append(dbUtils.esc4XML(rs.getString("vlr_node_id"))).append("\"")
		       	        .append(" vlr_node_vod_res_id=\"").append(rs.getLong("vlr_node_vod_res_id")).append("\"")
		       	        .append(" vlr_update_timestamp=\"").append(rs.getTimestamp("vlr_update_timestamp")).append("\"")
		       	        .append(" res_src_link=\"").append(dbUtils.esc4XML(rs.getString("res_src_link"))).append("\"")
		       	        .append(" res_img_link=\"").append(dbUtils.esc4XML(rs.getString("res_img_link"))).append("\"")
		       	        .append(">");
		       		 xml.append("</vod_lrn_record>");
		       	}
				cwSQL.cleanUp(rs, stmt);
			return xml.toString();
		}
	 public void addVodLearnRecord(Connection con, loginProfile prof,dbCourse dbo) throws SQLException, qdbException {

		String sel_sql = "select vlr_id from VodLearnRecord where vlr_tkh_id = ? and vlr_node_vod_res_id = ? ";

		String ins_sql = " insert into VodLearnRecord ";
		ins_sql += " (vlr_tkh_id,vlr_chapter_id,vlr_node_id,vlr_node_vod_res_id,vlr_create_usr_id,vlr_create_timestamp,vlr_update_usr_id,vlr_update_timestamp)";
		ins_sql += " values (? ,? ,? ,? ,? ,? ,? ,? )";

		String upd_sql = " update VodLearnRecord set vlr_update_usr_id = ?, vlr_update_timestamp = ? where vlr_id = ?";

		//查询
		long vlr_id = 0;
		PreparedStatement sel_stmt = con.prepareStatement(sel_sql);
		sel_stmt.setLong(1, tkh_id);
		sel_stmt.setLong(2, dbo.vod_res_id);
		ResultSet sel_rs = sel_stmt.executeQuery();
		if (sel_rs.next()) {
			vlr_id = sel_rs.getLong("vlr_id");
		}
		cwSQL.cleanUp(sel_rs, sel_stmt);
		//更新
		if(vlr_id > 0 ){
			PreparedStatement upd_stmt = con.prepareStatement(upd_sql);
			upd_stmt.setString(1, prof.usr_id);
			upd_stmt.setTimestamp(2, dbUtils.getTime(con));
			upd_stmt.setLong(3, vlr_id);
            upd_stmt.executeUpdate();
            upd_stmt.close();
		}else {
			//添加
			PreparedStatement ins_stmt = con.prepareStatement(ins_sql);
			ins_stmt.setLong(1, tkh_id);
			ins_stmt.setString(2, dbo.cos_chapter_id);
			ins_stmt.setString(3, dbo.cos_node_id);
			ins_stmt.setLong(4, dbo.vod_res_id);
			ins_stmt.setString(5, prof.usr_id);
			ins_stmt.setTimestamp(6, dbUtils.getTime(con));
			ins_stmt.setString(7, prof.usr_id);
			ins_stmt.setTimestamp(8, dbUtils.getTime(con));
			ins_stmt.executeUpdate();
		}

	}

}