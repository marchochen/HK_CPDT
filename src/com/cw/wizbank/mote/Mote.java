package com.cw.wizbank.mote;

//import java.io.*;
import java.util.*;
import java.sql.*;

import javax.servlet.http.*;

//import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.dbModule;
import com.cw.wizbank.util.*;
import com.cw.wizbank.ae.db.*;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeItemAccess;
import com.cw.wizbank.ae.aeAttendance;
import com.cw.wizbank.course.CourseCriteria;
import com.cw.wizbank.db.DbCourseCriteria;
import com.cwn.wizbank.utils.CommonLog;


public class Mote extends MoteDefault{
    public long imt_id;
    public String title;
//    public Timestamp due_date;
//  public String plan_xml;
    public Timestamp eff_start_date;
    public Timestamp eff_end_date;
    public String status;
    /*
    public int level1_ind;
    public int level2_ind;
    public int level3_ind;
    public int level4_ind;
    */
    public String attend_comment_xml;
//    public float rating_target;
    public String rating_actual_xml;
    public String rating_comment_xml;
//    public float cost_target;
    public String cost_actual;
    public String cost_comment_xml;
//    public float time_target;
    public String time_actual;
    public String time_comment_xml;
    public String comment_xml;
    public String attch1_xml;
    public String attch2_xml;
    public String attch3_xml;
    public String attch4_xml;
    /*
    public Timestamp create_timestamp;
    public String create_usr_id;
    public Timestamp update_timestamp;
    public String update_usr_id;
    */
    aeItem item = null;
    public Mote(){
        item = new aeItem();
    }

    private static final String[] ORDER_COL = {"duedate", "itemtitle", "itemtype", "motetitle"};
    private static final String[] ORDER_COL_DB = {"imt_due_date", "itm_title", "itm_type", "imt_title"};
    private static final String[] LEVEL_COL_DB = {"imt_level1_ind desc", "imt_level2_ind desc" , "imt_level3_ind desc" , "imt_level4_ind desc"};

    private static final String MOTE_LST     = "mote_lst";
    private static final String MOTE_SORT_BY  = "mote_sort_by";
    private static final String MOTE_ORDER_BY = "mote_order_by";
    private static final String MOTE_TIMESTAMP = "mote_timestamp";


    private static final int DEF_PAGESIZE = 10;
    private static final String SQL_UPD_MOTE_STATUS = " UPDATE aeItemMote set imt_status = ? , imt_upd_usr_id = ? , imt_upd_timestamp = ? WHERE IMT_ID = ? ";
    public static final String DEL_BY_IMD_SQL = " Delete FROM aeItemMote WHERE imt_imd_id = ? ";
    public static final String DEL_BY_IMT_SQL = " Delete FROM aeItemMote WHERE imt_id = ? ";

    public static final String STATUS_PROGRESS = "PROGRESS";
    public static final String STATUS_COMPLETE = "COMPLETE";

    public static StringBuffer getInsSQL(long imd_id){
        StringBuffer SQL = new StringBuffer(400);

        SQL.append("INSERT INTO aeItemMote (");
        SQL.append(" imt_imd_id , ");
        SQL.append(" imt_title , ");
        SQL.append(" imt_due_date , ");
     // insert imt_plan_xml(null) for oracle
     // SQL.append(" imt_plan_xml , ");
        SQL.append(" imt_status , ");
        SQL.append(" imt_level1_ind , ");
        SQL.append(" imt_level2_ind , ");
        SQL.append(" imt_level3_ind , ");
        SQL.append(" imt_level4_ind , ");
        SQL.append(" imt_participant_target , ");
        SQL.append(" imt_rating_target , ");
        SQL.append(" imt_cost_target , ");
        SQL.append(" imt_time_target , ");
        SQL.append(" imt_create_timestamp , ");
        SQL.append(" imt_create_usr_id , ");
        SQL.append(" imt_upd_timestamp , ");
        SQL.append(" imt_upd_usr_id ");
        SQL.append(" ) VALUES ");
        SQL.append(" (");
        SQL.append(imd_id).append(", 'New MOTE Report',  ?, 'PROGRESS', ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

        return SQL;
    }

    public static String getUpdDefaultValueSQL(){
        StringBuffer SQL = new StringBuffer();
        SQL.append("UPDATE aeItemMote SET ");
     // insert imt_plan_xml(null) for oracle
     // SQL.append(" imt_plan_xml = ? , ");
        SQL.append(" imt_due_date = ? , ");
        SQL.append(" imt_level1_ind = ?, ");
        SQL.append(" imt_level2_ind = ?, ");
        SQL.append(" imt_level3_ind = ? , ");
        SQL.append(" imt_level4_ind = ? , ");
        SQL.append(" imt_participant_target = ?, ");
        SQL.append(" imt_rating_target = ?, ");
        SQL.append(" imt_cost_target = ? , ");
        SQL.append(" imt_time_target = ? , ");
        SQL.append(" imt_upd_timestamp = ? , ");
        SQL.append(" imt_upd_usr_id = ? ");
        SQL.append(" WHERE imt_imd_id = ? ");
        CommonLog.debug("mote L479:" + SQL.length());

        return SQL.toString();
    }

    public static String getUpdSQL(){
        StringBuffer SQL = new StringBuffer(1024);
        SQL.append("UPDATE aeItemMote SET ");
        SQL.append(" imt_title = ? , ");
     // insert imt_plan_xml(null) for oracle
     //    SQL.append(" imt_plan_xml = ? , ");
        SQL.append(" imt_due_date = ? , ");
        SQL.append(" imt_eff_start_date = ? , ");
        SQL.append(" imt_eff_end_date = ? , ");
        SQL.append(" imt_status = ? , ");
        SQL.append(" imt_level1_ind = ?, ");
        SQL.append(" imt_level2_ind = ?, ");
        SQL.append(" imt_level3_ind = ? , ");
        SQL.append(" imt_level4_ind = ? , ");
    //    SQL.append(" imt_attend_comment_xml = ? , ");
        SQL.append(" imt_participant_target = ?, ");
        SQL.append(" imt_rating_target = ?, ");
    //    SQL.append(" imt_rating_actual_xml = ?, ");
    //    SQL.append(" imt_rating_comment_xml = ?, ");
        SQL.append(" imt_cost_target = ? , ");
        SQL.append(" imt_cost_actual = ?, ");
    //    SQL.append(" imt_cost_comment_xml = ?, ");
        SQL.append(" imt_time_target = ? , ");
        SQL.append(" imt_time_actual = ?, ");
    //    SQL.append(" imt_time_comment_xml = ?, ");
    //    SQL.append(" imt_comment_xml = ?, ");
    //    SQL.append(" imt_attch1_xml = ?, ");
    //    SQL.append(" imt_attch2_xml = ?, ");
    //    SQL.append(" imt_attch3_xml = ?, ");
    //    SQL.append(" imt_attch4_xml = ?, ");
        SQL.append(" imt_upd_timestamp = ? , ");
        SQL.append(" imt_upd_usr_id = ? ");
        SQL.append(" WHERE imt_id = ? ");
        return SQL.toString();
    }

    public static String getMoteLstSQL(){
        StringBuffer SQL = new StringBuffer();
        SQL.append("SELECT itm_id, itm_title, itm_code, itm_type, itm_status, imd_id, ");
        SQL.append(" imt_id, imt_due_date, imt_eff_start_date, imt_eff_end_date, imt_title, imt_status, imt_level1_ind, imt_level2_ind, imt_level3_ind, imt_level4_ind , imt_upd_timestamp ");
        SQL.append(" FROM aeItem, aeItemMoteDefault, aeItemMote ");
        SQL.append(" WHERE itm_life_status is null AND imt_imd_id = imd_id AND itm_imd_id = imd_id ");
        return SQL.toString();
    }

    public String getGetSQL(){
        StringBuffer SQL = new StringBuffer();
        SQL.append("SELECT imt_title, imt_plan_xml, imt_due_date, imt_eff_start_date, imt_eff_end_date, imt_status, ");
        SQL.append(" imt_level1_ind, imt_level2_ind, imt_level3_ind, imt_level4_ind, ");
        SQL.append(" imt_attend_comment_xml, imt_participant_target, imt_rating_target, imt_rating_actual_xml, imt_rating_comment_xml, imt_cost_target, imt_cost_actual, imt_cost_comment_xml, imt_time_target, imt_time_actual, imt_time_comment_xml, imt_comment_xml, ");
        SQL.append(" imt_attch1_xml, imt_attch2_xml, imt_attch3_xml, imt_attch4_xml, ");
        SQL.append(" imt_create_timestamp, imt_create_usr_id, imt_upd_timestamp, imt_upd_usr_id");
        SQL.append(" FROM aeItemMote ");
        SQL.append(" WHERE imt_id = ? ");
        return SQL.toString();
    }

    public String getMoteLstAsXML(Connection con, HttpSession sess, loginProfile prof, long root_ent_id, String moteStatus, long itm_id, cwPagination cwPage, boolean show_respon)
         throws qdbException, SQLException, cwSysMessage
    {
        Vector itm_id_lst = new Vector();
        if (show_respon){
            itm_id_lst = aeItemAccess.getResponItemAsVector(con, prof.usr_ent_id, prof.current_role , aeItemAccess.ACCESS_TYPE_ROLE);
        }
        if (itm_id != 0){
            itm_id_lst.addElement(new Long(itm_id));
        }

        StringBuffer result = new StringBuffer(10000);
        StringBuffer orderSQL = new StringBuffer(100);

        boolean fromSession = false;
        boolean scopeResult = false;

        Vector vtMote = new Vector();
        Vector moteAll = new Vector();
        Vector moteByPage = new Vector();

        Timestamp sess_timestamp = (Timestamp)sess.getAttribute(MOTE_TIMESTAMP);
        String sess_sort_by = (String) sess.getAttribute(MOTE_SORT_BY);
        String sess_order_by = (String) sess.getAttribute(MOTE_ORDER_BY);

        if (cwPage.ts == null)
            cwPage.ts = cwSQL.getTime(con);

        if (cwPage.pageSize == 0){
            cwPage.pageSize = DEF_PAGESIZE;
        }
        if (cwPage.curPage == 0) {
            cwPage.curPage = 1;
        }

        int default_ind = 0;

        int start;
        int end;

        if (sess_timestamp != null && sess_timestamp.equals(cwPage.ts))
        {
            moteAll = (Vector)sess.getAttribute(MOTE_LST);
            if (sess_sort_by!= null && sess_sort_by.equals(cwPage.sortCol)){
                if (sess_order_by!= null && sess_order_by.equals(cwPage.sortOrder)){
                    start = ((cwPage.curPage-1) * cwPage.pageSize) + 1;
                    end = cwPage.curPage * cwPage.pageSize;
                }else{
                    // same sort col, reverse order
                    start = moteAll.size() - cwPage.pageSize;
                    if (start<0){
                        start = 0;
                    }
                    end = moteAll.size();
                }
                for (int i=start ; i<= moteAll.size() && (i <= end);i++) {
                    moteByPage.addElement(moteAll.elementAt(i-1));
                }
                scopeResult = true;
            }else{
                scopeResult = false;
            }
            fromSession = true;

        } else {
            fromSession = false;
        }
        orderSQL.append(getOrderSQL(cwPage.sortCol, cwPage.sortOrder));
        vtMote = getMoteLst(con, root_ent_id, moteStatus, itm_id_lst, orderSQL.toString(), moteAll, moteByPage, scopeResult, fromSession);
        if (!fromSession){
            sess.setAttribute(MOTE_TIMESTAMP, cwPage.ts);
            sess.setAttribute(MOTE_LST, moteAll);
        }
        if (cwPage.sortCol != null && cwPage.sortOrder != null){
            sess.setAttribute(MOTE_SORT_BY, cwPage.sortCol);
            sess.setAttribute(MOTE_ORDER_BY, cwPage.sortOrder);
        }
        result.append("<mote_maintance>");
        if (moteAll != null){
            cwPage.totalRec = moteAll.size();
            if (cwPage.pageSize != 0){
                cwPage.totalPage = moteAll.size() / cwPage.pageSize;
                if (moteAll.size() % cwPage.pageSize != 0){
                    cwPage.totalPage++;
                }
            }
        }
        result.append(cwPage.asXML());

        if (itm_id != 0){
            aeItem itm = new aeItem();
            itm.itm_id = itm_id;
            imd_id = itm.getMoteId(con);
            result.append("<mote_list imd_id=\"");
            result.append(imd_id);
            result.append("\" >");
        }else{
            result.append("<mote_list>");
        }
        if (vtMote != null) {
            int count;
            if (moteByPage != null && moteByPage.size() != 0) {
                count = 0;
            } else {
                count = (cwPage.curPage-1)*cwPage.pageSize;
            }

            for (int i=count; i<vtMote.size() && i<count+cwPage.pageSize; i++) {

                Mote mote = (Mote) vtMote.elementAt(i);
                result.append("<mote>").append(mote.infoAsXML(con, false, null, root_ent_id)).append("</mote>").append(cwUtils.NEWL);
            }
        }
        result.append("</mote_list>");
        result.append(cwUtils.NEWL);
        result.append("</mote_maintance>");
        result.append(cwUtils.NEWL);
//System.out.println("mote L178:" + result.length());
//System.out.println("mote L179:" + orderSQL.length());

        return result.toString();
    }

    public StringBuffer infoAsXML(Connection con, boolean displaySiteMoteAttr, StringBuffer siteMoteAttr, long root_ent_id) throws qdbException, SQLException, cwSysMessage{
        item.getItem(con);
        StringBuffer result = new StringBuffer(800);
        result.append("<mote_info ");
        result.append(" imt_id=\"").append(imt_id);
        result.append("\" imd_id=\"").append(imd_id);
        result.append("\" title=\"").append(cwUtils.esc4XML(cwUtils.escNull(title)));
        result.append("\" eff_start_date=\"").append(cwUtils.escNull(eff_start_date));
        result.append("\" eff_end_date=\"").append(cwUtils.escNull(eff_end_date));
        result.append("\" due_date=\"").append(due_date);
        result.append("\" status=\"").append(status);
        result.append("\" upd_date=\"").append(upd_timestamp);
        result.append("\" >").append(cwUtils.NEWL);

        result.append(item.shortInfoAsXML(con));
        result.append(item.getRunItemAsXML(con, false, false, null));        

        if (displaySiteMoteAttr){
            if (siteMoteAttr == null){
                MoteSite moteSite= new MoteSite();
                moteSite.ims_ent_id_root = root_ent_id;
                moteSite.getByRoot(con);
                siteMoteAttr = moteSite.siteAttrAsXML();
            }
            result.append(siteMoteAttr);
        }
        result.append(cwUtils.escNull(plan_xml));
        result.append("<level_list>").append(cwUtils.NEWL);
        if (level1_ind == 1){
            result.append("<level>1</level>").append(cwUtils.NEWL);
        }
        if (level2_ind == 1){
            result.append("<level>2</level>").append(cwUtils.NEWL);
        }
        if (level3_ind == 1){
            result.append("<level>3</level>").append(cwUtils.NEWL);
        }
        if (level4_ind == 1){
            result.append("<level>4</level>").append(cwUtils.NEWL);
        }
        result.append("</level_list>").append(cwUtils.NEWL);
        result.append("</mote_info>").append(cwUtils.NEWL);
//        System.out.println("mote L222:" + result.length());

        return result;

    }

    public static StringBuffer getScopeSQL(Vector moteByPage){
        StringBuffer scopeSQL = new StringBuffer(60);
        if (moteByPage != null && moteByPage.size() > 0){
            scopeSQL.append(" AND imt_id IN  (");

            for (int i=0 ; i< moteByPage.size() ;i++) {
                if (i!=0) {
                    scopeSQL.append(",");
                }
                scopeSQL.append(((Long) moteByPage.elementAt(i)).longValue());
            }
            scopeSQL.append(" ) ");
        }

        return scopeSQL;
    }

    private Vector getMoteLst(Connection con, long root_ent_id, String moteStatus, Vector itm_id_lst, String orderSQL, Vector moteAll, Vector moteByPage, boolean scopeResult, boolean fromSession) throws SQLException{
        StringBuffer sql = new StringBuffer(getMoteLstSQL());
        Vector vtMote = new Vector();
        if (!scopeResult){
            moteAll.clear();
        }
        if (moteStatus != null){
            sql.append(" AND imt_status = '" + moteStatus + "' ");
        }
        if (itm_id_lst == null || itm_id_lst.size() == 0){
            itm_id_lst = new Vector();
            itm_id_lst.addElement(new Long(0));
        }
        sql.append(" AND itm_id in " + cwUtils.vector2list(itm_id_lst));

        if (root_ent_id != 0){
            sql.append(" AND itm_owner_ent_id = " + root_ent_id);
        }

        StringBuffer scopeSQL = new StringBuffer();
        if(scopeResult){
            scopeSQL = getScopeSQL(moteByPage);
        }

        sql.append(scopeSQL);
        if(orderSQL != null) {
            sql.append(" ").append(orderSQL);
        }

        PreparedStatement stmt = con.prepareStatement(sql.toString());

        ResultSet rs = stmt.executeQuery();

        while (rs.next()){
            Mote mote = new Mote();
            mote.item.itm_id = rs.getLong("itm_id");
            mote.item.itm_title = rs.getString("itm_title");
            mote.item.itm_code = rs.getString("itm_code");
            mote.item.itm_type = rs.getString("itm_type");
            mote.item.itm_status = rs.getString("itm_status");

            mote.imd_id = rs.getLong("imd_id");
//            if (itm_id != 0){
//                imd_id = mote.imd_id;
//            }

            mote.imt_id = rs.getLong("imt_id");
            mote.status = rs.getString("imt_status");
            mote.due_date = rs.getTimestamp("imt_due_date");
            mote.eff_start_date = rs.getTimestamp("imt_eff_start_date");
            mote.eff_end_date = rs.getTimestamp("imt_eff_end_date");
            mote.title = rs.getString("imt_title");
            mote.level1_ind = rs.getInt("imt_level1_ind");
            mote.level2_ind = rs.getInt("imt_level2_ind");
            mote.level3_ind = rs.getInt("imt_level3_ind");
            mote.level4_ind = rs.getInt("imt_level4_ind");
            mote.upd_timestamp = rs.getTimestamp("imt_upd_timestamp");
            if (!scopeResult){
                moteAll.addElement(new Long(mote.imt_id));
            }
            vtMote.addElement(mote);
        }
        cwSQL.cleanUp(rs, stmt);
        return vtMote;
    }


    public static StringBuffer getOrderSQL(String sort_by, String order_by){
        StringBuffer orderSQL = new StringBuffer(80);
        if ("level".equalsIgnoreCase(sort_by)){
                for (int i=0; i<LEVEL_COL_DB.length; i++){
                    if (order_by != null && order_by.equalsIgnoreCase("DESC"))
                        orderSQL.append(LEVEL_COL_DB[LEVEL_COL_DB.length - i - 1]);
                    else
                        orderSQL.append(LEVEL_COL_DB[i]);
                    if (i+1<LEVEL_COL_DB.length){
                        orderSQL.append(", ");
                    }
                }
        }else{

            for (int i=0; i<ORDER_COL.length; i++) {
                    if (ORDER_COL[i].equalsIgnoreCase(sort_by)) {
                        orderSQL.append(ORDER_COL_DB[i]);
                        if (order_by != null && order_by.equalsIgnoreCase("DESC")) {
                            orderSQL.append(" DESC ");
                        } else {
                            orderSQL.append(" ASC ");
                        }
                        break;
                    }
            }
        }
        if (orderSQL.length() == 0){
                orderSQL.append(ORDER_COL_DB[0]);
                orderSQL.append(" DESC ");
        }

        return orderSQL.insert(0, " order by ");
    }

    /*
    *   MoteAsXML()
    *   get the xml for a course with its itm_id
    */
    /*
    public static String MoteAsXML(Connection con, long itm_id) throws cwException, cwSysMessage, qdbException, SQLException {
            StringBuffer xmlBuf = new StringBuffer();
            aeItem cos = new aeItem();
            DbItemMote itemMote = new DbItemMote();
            DbItemBudget itemBudget = new DbItemBudget();

            cos.itm_id = itm_id;
            cos.getItem(con);
            String cosXML = cos.contentAsXML(con);

            itemMote.imt_itm_id = (int)itm_id;
            itemMote.get(con);

            itemBudget.ibd_itm_id = (int)itm_id;
            itemBudget.get(con);
            int pctTarget = ViewMoteRpt.getCosCapacity(con, itm_id);
            int pctActual = dbUserGroup.getMemberListFromCos(con, itm_id).size();
            String ratTarget = getAvgCosRating(con, itm_id, ViewMoteRpt.TARGET_OVERALL_RATING);
            String ratActual = getAvgCosRating(con, itm_id, ViewMoteRpt.ACTUAL_OVERALL_RATING);

            xmlBuf.append(cosXML);
            xmlBuf.append("<mote itm_id=\"");
            xmlBuf.append(itm_id);
            xmlBuf.append("\" status=\"");
            xmlBuf.append(itemMote.imt_status);
            xmlBuf.append("\" >");
            xmlBuf.append("<participant target=\"");
            xmlBuf.append(pctTarget);
            xmlBuf.append("\" actual=\"");
            xmlBuf.append(pctActual);
            xmlBuf.append("\" comment=\"");
            xmlBuf.append(cwUtils.esc4XML(itemMote.imt_participant_cmt));
            xmlBuf.append("\" />");
            xmlBuf.append(cwUtils.NEWL);

            xmlBuf.append("<rating target=\"");
            xmlBuf.append(ratTarget);
            xmlBuf.append("\" actual=\"");
            xmlBuf.append(ratActual);
            xmlBuf.append("\" comment=\"");
            xmlBuf.append(cwUtils.esc4XML(itemMote.imt_rating_cmt));
            xmlBuf.append("\" />");
            xmlBuf.append(cwUtils.NEWL);

            xmlBuf.append("<budget target=\"");
            xmlBuf.append(itemBudget.ibd_target);
            xmlBuf.append("\" actual=\"");
            xmlBuf.append(itemBudget.ibd_actual);
            xmlBuf.append("\" comment=\"");
            xmlBuf.append(cwUtils.esc4XML(itemMote.imt_budget_cmt));
            xmlBuf.append("\" />");
            xmlBuf.append(cwUtils.NEWL);

            xmlBuf.append("<comment positive=\"");
            xmlBuf.append(cwUtils.esc4XML(itemMote.imt_pos_cmt));
            xmlBuf.append("\" negative=\"");
            xmlBuf.append(cwUtils.esc4XML(itemMote.imt_neg_cmt));
            xmlBuf.append("\" instructor=\"");
            xmlBuf.append(cwUtils.esc4XML(itemMote.imt_ist_cmt));
            xmlBuf.append("\" suggestion=\"");
            xmlBuf.append(cwUtils.esc4XML(itemMote.imt_suggestion));
            xmlBuf.append("\" />");
            xmlBuf.append(cwUtils.NEWL);
            xmlBuf.append("</mote>");
            xmlBuf.append(cwUtils.NEWL);

            return cwUtils.escNull(xmlBuf.toString());
    }
 */
    public void updMoteStatus(Connection con, String usr_id) throws SQLException{
        upd_timestamp = cwSQL.getTime(con);
        upd_usr_id = usr_id;

        PreparedStatement stmt = con.prepareStatement(SQL_UPD_MOTE_STATUS);
        stmt.setString(1, status);
        stmt.setString(2, upd_usr_id);
        stmt.setTimestamp(3, upd_timestamp);
        stmt.setLong(4, imt_id);

        stmt.executeUpdate();
        cwSQL.cleanUp(null, stmt);
    }

    public void ins(Connection con) throws SQLException{
        // use the ins method in moteDefault with mote table ins statement
        ins(con, getInsSQL(imd_id).toString());
    }

    public void ins(Connection con, String SQL) throws SQLException {
		// give the function to sub-class, for insert the data to Oracle
		// super.ins(con, SQL);

		PreparedStatement stmt = con.prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS);

		int col = 1;
		stmt.setTimestamp(col++, due_date);
		// stmt.setString(col++, plan_xml);
		stmt.setInt(col++, level1_ind);
		stmt.setInt(col++, level2_ind);
		stmt.setInt(col++, level3_ind);
		stmt.setInt(col++, level4_ind);
		stmt.setString(col++, participant_target);
		stmt.setString(col++, rating_target);
		stmt.setString(col++, cost_target);
		stmt.setString(col++, time_target);
		stmt.setTimestamp(col++, create_timestamp);
		stmt.setString(col++, create_usr_id);
		stmt.setTimestamp(col++, upd_timestamp);
		stmt.setString(col++, upd_usr_id);
		stmt.executeUpdate();

		// start update
		// get the update condition for imt_id (oracle)
		imt_id = cwSQL.getAutoId(con, stmt, "aeItemMote", "imt_id");
		if (imt_id > 0) {
			// update the reference for Oracle
			String columnName[] = { "imt_plan_xml" };
			String columnValue[] = { plan_xml };
			String condition = "imt_id = " + imt_id;
			cwSQL.updateClobFields(con, "aeItemMote", columnName, columnValue, condition);
		}
		// end
		stmt.close();
	}

    public void insMote(Connection con, long imd_id, String usr_id) throws SQLException{
        this.imd_id = imd_id;

        Mote dbMote = new Mote();
        dbMote.imd_id = imd_id;
        dbMote.getDefaultValue(con);
        dbMote.upd_usr_id = usr_id;
        dbMote.create_usr_id = usr_id;
        dbMote.upd_timestamp = cwSQL.getTime(con);
        dbMote.create_timestamp = dbMote.upd_timestamp;
        dbMote.status = "PROGRESS";

        dbMote.title = title;
//        if (due_date != null){
            dbMote.due_date = due_date;
//        }

        dbMote.eff_start_date = eff_start_date;
        dbMote.eff_end_date = eff_end_date;
        dbMote.level1_ind = level1_ind;
        dbMote.level2_ind = level2_ind;
        dbMote.level3_ind = level3_ind;
        dbMote.level4_ind = level4_ind;

        dbMote.ins(con);
        dbMote.upd(con);
    }
    // by frontend, with imt_id, imd_id
    public void updMote(Connection con, String usr_id, String itemType) throws SQLException{
        if (upd_timestamp == null){
            upd_timestamp = cwSQL.getTime(con);
        }
        upd_usr_id = usr_id;
        if (itemType != null && itemType.equalsIgnoreCase("CLASSROOM")){
            super.upd(con, MoteDefault.getUpdSQL());
        }
        this.upd(con);
    }

    public StringBuffer getAttendXML(Connection con, boolean isClassroom) throws qdbException, SQLException, cwSysMessage{
        StringBuffer result = new StringBuffer(200);
        result.append("<attendance>").append(cwUtils.NEWL);

        if (isClassroom){
//            int target_attend = item.getTargetLrn(con).size();
//            int target_attend = participant_target;

            result.append("<target>").append(cwUtils.escNull(participant_target)).append("</target>").append(cwUtils.NEWL);

//            result.append("<target>").append(target_attend).append("</target>").append(cwUtils.NEWL);

            int total_attend = 0;
            StringBuffer attendAct = new StringBuffer();
//            Vector runLst = aeItemRelation.getChildItemId(con, item.itm_id);

            Vector runLst = item.getRunItemAsVector(con, false, false, null);

            for (int i =0; i<runLst.size(); i++){
                aeItem run = (aeItem) runLst.elementAt(i);
                long run_id = run.itm_id;
                if (run.itm_life_status== null || !run.itm_life_status.equals(aeItem.ITM_LIFE_STATUS_CANCELLED)){
                    int this_attend = aeAttendance.getAttendCountByItem(con, run_id, null, null); 
                    attendAct.append("<actual type=\"RUN\" title=\"").append(cwUtils.esc4XML(run.itm_title)).append("\" ");
                    attendAct.append("itm_id=\"").append(run.itm_id).append("\">");
                    attendAct.append(this_attend).append("</actual>").append(cwUtils.NEWL);
                    total_attend += this_attend;
                }
            }
            result.append("<actual type=\"OVERALL\">").append(total_attend).append("</actual>").append(cwUtils.NEWL);
            result.append(attendAct);
            String ratio;
            if (participant_target == null){
                ratio = null;    
            }else{
                int target_attend = Integer.parseInt(participant_target);
            if (target_attend == 0){
                    ratio = null;    
            }else{
                    ratio = ((int)(total_attend * 10000 / target_attend)) / (float)100 +"" ;    
                }
            }
            result.append("<ratio>").append(cwUtils.escNull(ratio)).append("</ratio>").append(cwUtils.NEWL);            
        }else{
        	CommonLog.info("eff_start_date: " + eff_start_date + ", " +  " eff_end_date: " + eff_end_date);
            result.append("<actual type=\"OVERALL\">").append(aeAttendance.getAttendCountByItem(con, item.itm_id, eff_start_date, eff_end_date)).append("</actual>").append(cwUtils.NEWL);
        }
        result.append(attend_comment_xml);
        result.append("</attendance>").append(cwUtils.NEWL);
//        System.out.println("mote L517:" + result.length());

        return result;
    }

    public StringBuffer getRatingXML(Connection con, long itm_id, boolean isClassroom) throws SQLException{
        StringBuffer result = new StringBuffer(400);
        result.append("<rating>").append(cwUtils.NEWL);
        result.append("<target>").append(rating_target).append("</target>").append(cwUtils.NEWL);
        if (isClassroom){
            result.append(rating_actual_xml);
        }else{
            result.append("<actual type=\"OVERALL\">").append(DbItemRating.getAvgRating(con, itm_id, "USR", eff_start_date, eff_end_date)).append("</actual>").append(cwUtils.NEWL);
        }
        result.append(rating_comment_xml);
        result.append("</rating>").append(cwUtils.NEWL);
//        System.out.println("mote L533:" + result.length());

        return result;
    }

    public StringBuffer getCostXML(){
        StringBuffer result = new StringBuffer(300);
        result.append("<cost_budget>").append(cwUtils.NEWL);
        result.append("<target>").append(cost_target).append("</target>").append(cwUtils.NEWL);
        result.append("<actual type=\"OVERALL\">").append(cost_actual).append("</actual>").append(cwUtils.NEWL);
        result.append(cost_comment_xml);
        result.append("</cost_budget>").append(cwUtils.NEWL);
//        System.out.println("mote L545:" + result.length());

        return result;
    }

    public StringBuffer getTimeXML(){
        StringBuffer result = new StringBuffer(300);
        result.append("<time_budget>").append(cwUtils.NEWL);
        result.append("<target>").append(time_target).append("</target>").append(cwUtils.NEWL);
        result.append("<actual type=\"OVERALL\">").append(time_actual).append("</actual>").append(cwUtils.NEWL);
        result.append(time_comment_xml);
        result.append("</time_budget>").append(cwUtils.NEWL);
//        System.out.println("mote L557:" + result.length());

        return result;
    }

    public StringBuffer getLevelLstXML(Connection con, long itm_id) throws cwException, SQLException{
        StringBuffer result = new StringBuffer(1200);
        long cos_id = 0;
        long ccr_id = 0;
        Vector vtMod = null;

        if (level1_ind == 1 || level2_ind == 1){
            DbCourseCriteria dbCourseCriteria = new DbCourseCriteria();
            dbCourseCriteria.ccr_itm_id = itm_id;
            dbCourseCriteria.ccr_type = DbCourseCriteria.TYPE_COMPLETION;
            dbCourseCriteria.getCcrIdByItmNType(con);
            ccr_id = dbCourseCriteria.ccr_id;
            vtMod = CourseCriteria.getCriteriaLst(con, ccr_id, eff_start_date, eff_end_date);
        }
        result.append("<level_list>").append(cwUtils.NEWL);
        if (level1_ind == 1){
            result.append("<level type=\"1\">").append(cwUtils.NEWL);
            if (ccr_id != 0){
                    result.append("<content>").append(cwUtils.NEWL);
                    result.append("<module_list>").append(cwUtils.NEWL);
                    for (int i=0; i<vtMod.size(); i++){
                        dbModule dbMod = (dbModule) vtMod.elementAt(i);
                        if (dbMod.mod_type.equals(dbModule.MOD_TYPE_SVY)){
                            result.append("<module id=\"").append(dbMod.mod_res_id).append("\" type=\"").append(dbMod.mod_type).append("\" >").append(cwUtils.esc4XML(dbMod.res_title)).append("</module>").append(cwUtils.NEWL);
                        }
                    }
                    result.append("</module_list>").append(cwUtils.NEWL);
                    result.append("</content>").append(cwUtils.NEWL);
            }
            result.append(attch1_xml).append(cwUtils.NEWL);
            result.append("</level>").append(cwUtils.NEWL);
        }
        if (level2_ind == 1){
            result.append("<level type=\"2\">").append(cwUtils.NEWL);
            if (ccr_id != 0){
                    result.append("<content>").append(cwUtils.NEWL);
                    result.append("<module_list>").append(cwUtils.NEWL);
                    for (int i=0; i<vtMod.size(); i++){
                        dbModule dbMod = (dbModule) vtMod.elementAt(i);
                        if (dbMod.mod_type.equals(dbModule.MOD_TYPE_TST) || dbMod.mod_type.equals(dbModule.MOD_TYPE_DXT) || dbMod.mod_type.equals(dbModule.MOD_TYPE_STX) || dbMod.mod_type.equals(dbModule.MOD_TYPE_ASS)){
                            result.append("<module id=\"").append(dbMod.mod_res_id).append("\" type=\"").append(dbMod.mod_type).append("\" >").append(cwUtils.esc4XML(dbMod.res_title)).append("</module>").append(cwUtils.NEWL);
                        }
                    }
                    result.append("</module_list>").append(cwUtils.NEWL);
                    result.append("</content>").append(cwUtils.NEWL);
            }
            result.append(attch2_xml).append(cwUtils.NEWL);
            result.append("</level>").append(cwUtils.NEWL);
        }
        if (level3_ind == 1){
            result.append("<level type=\"3\">").append(cwUtils.NEWL);
            result.append(attch3_xml).append(cwUtils.NEWL);
            result.append("</level>").append(cwUtils.NEWL);
        }
        if (level4_ind == 1){
            result.append("<level type=\"4\">").append(cwUtils.NEWL);
            result.append(attch4_xml).append(cwUtils.NEWL);
            result.append("</level>").append(cwUtils.NEWL);
        }

        result.append("</level_list>").append(cwUtils.NEWL);
//        System.out.println("mote L578:" + result.length());

        return result;
    }
/*
    public StringBuffer getLevelXML(Connection con, int level) throws cwException{
        StringBuffer result = new StringBuffer(400);
        result.append("<level type=\"").append(level).append("\">").append(cwUtils.NEWL);
        if ((level == 1 || level == 2)){
            long cos_id = dbCourse.getCosResId(con, itm_id);
            if (cos_id != 0){
                long ccr_id = dbCourse.getCosCriteriaId(con, cos_id);
                if (ccr_id != 0){
                    result.append("<content>").append(cwUtils.NEWL);

                    Vector vtMod = null;
                    if (level == 1){
                        vtMod = getCriteriaLst(con, ccr_id, "SVY", eff_start_date, eff_end_date);
                    }else if (level == 2){
                        vtMod = getCriteriaLst(con, ccr_id, null, eff_start_date, eff_end_date);
                    }
                    result.append("<resource_list>").append(cwUtils.NEWL);
                    result.append("<resource id=\"").append(content_id).append("\" >").append(dbResource.getResTitle(con, 5113)).append("</resource>").append(cwUtils.NEWL);
                    result.append("</resource_list>").append(cwUtils.NEWL);
                    result.append("</content>").append(cwUtils.NEWL);
                }
            }
        }
        result.append("<attachment>").append(cwUtils.NEWL);
        if (level == 1){
            result.append(attch1_xml).append(cwUtils.NEWL);
        }else if (level == 2){
            result.append(attch2_xml).append(cwUtils.NEWL);
        }else if (level == 3){
            result.append(attch3_xml).append(cwUtils.NEWL);
        }else if (level == 4){
            result.append(attch4_xml).append(cwUtils.NEWL);
        }
        result.append("</attachment>").append(cwUtils.NEWL);
        result.append("</level>").append(cwUtils.NEWL);
//        System.out.println("mote L611:" + result.length());

        return result;
    }
  */
    public StringBuffer getMoteAsXML(Connection con, long imt_id, long itm_id, long root_ent_id) throws cwException, qdbException, SQLException, cwSysMessage{
        this.item.itm_id = itm_id;
        this.imt_id = imt_id;
//        super.item.itm_id = itm_id;
        MoteSite moteSite= new MoteSite();
        moteSite.ims_ent_id_root = root_ent_id;
        moteSite.getByRoot(con);
        StringBuffer siteMoteAttr = moteSite.siteAttrAsXML();

        get(con);
        StringBuffer result = new StringBuffer(1024);
        // item initialize in infoAsXML
        result.append("<mote_report>");
        result.append(infoAsXML(con, true, siteMoteAttr, root_ent_id));
        result.append("<mote_content>");
        boolean isClassroom;
        if (item.itm_type.equalsIgnoreCase("CLASSROOM")){
            isClassroom = true;
        }else{
            isClassroom = false;
        }

        if (moteSite.ims_attend_ind == 1){
             result.append(getAttendXML(con, isClassroom));
        }
        if (moteSite.ims_rating_ind == 1){
             result.append(getRatingXML(con, itm_id, isClassroom));
        }
        if (moteSite.ims_cost_budget_ind == 1){
             result.append(getCostXML());
        }
        if (moteSite.ims_time_budget_ind == 1){
             result.append(getTimeXML());
        }
        if (moteSite.ims_comment_xml != null){
             result.append(comment_xml);
        }
        result.append(getLevelLstXML(con, itm_id));
        result.append("</mote_content>");
        result.append("</mote_report>");
        return result;

    }

    public void getDefaultValue(Connection con) throws SQLException{
        super.get(con);
    }

    /*
    *   get by imt_id
    */
    public void get(Connection con) throws SQLException{

        PreparedStatement stmt = con.prepareStatement(getGetSQL());
        stmt.setLong(1, imt_id);

        ResultSet rs = stmt.executeQuery();

        if (rs.next()){
            int col = 1;
            title = rs.getString(col++);
         //get the reference from oracle
            plan_xml = cwSQL.getClobValue(rs,"imt_plan_xml");
            col++;
            due_date = rs.getTimestamp(col++);
            eff_start_date = rs.getTimestamp(col++);
            eff_end_date = rs.getTimestamp(col++);
            status = rs.getString(col++);

            level1_ind = rs.getInt(col++);
            level2_ind = rs.getInt(col++);
            level3_ind = rs.getInt(col++);
            level4_ind = rs.getInt(col++);

    //get the reference from oracle
            attend_comment_xml = cwSQL.getClobValue(rs,"imt_attend_comment_xml");
            col++;

            participant_target = rs.getString(col++);
            rating_target = rs.getString(col++);

        //  rating_actual_xml = rs.getString(col++);
            rating_actual_xml = cwSQL.getClobValue(rs,"imt_rating_actual_xml");
            col++;

        //  rating_comment_xml = rs.getString(col++);
            rating_comment_xml = cwSQL.getClobValue(rs,"imt_rating_comment_xml");
            col++;

            cost_target = rs.getString(col++);
            cost_actual = rs.getString(col++);

       //   cost_comment_xml = rs.getString(col++);
            cost_comment_xml = cwSQL.getClobValue(rs,"imt_cost_comment_xml");
            col++;

            time_target = rs.getString(col++);
            time_actual = rs.getString(col++);

       //   time_comment_xml = rs.getString(col++);
            time_comment_xml = cwSQL.getClobValue(rs,"imt_time_comment_xml");
            col++;

       //   comment_xml = rs.getString(col++);
            comment_xml = cwSQL.getClobValue(rs,"imt_comment_xml");
            col++;

       //     attch1_xml = rs.getString(col++);
       //     attch2_xml = rs.getString(col++);
       //     attch3_xml = rs.getString(col++);
       //     attch4_xml = rs.getString(col++);
            attch1_xml = cwSQL.getClobValue(rs,"imt_attch1_xml");
            attch2_xml = cwSQL.getClobValue(rs,"imt_attch2_xml");
            attch3_xml = cwSQL.getClobValue(rs,"imt_attch3_xml");
            attch4_xml = cwSQL.getClobValue(rs,"imt_attch4_xml");
            col+=4;

            create_timestamp = rs.getTimestamp(col++);
            create_usr_id = rs.getString(col++);
            upd_timestamp = rs.getTimestamp(col++);
            upd_usr_id = rs.getString(col++);
        }
        stmt.close();
    }

    public void upd(Connection con) throws SQLException{

        PreparedStatement stmt = con.prepareStatement(this.getUpdSQL());
            int col=1;
            stmt.setString(col++, title);
            //       stmt.setString(col++, plan_xml);
            stmt.setTimestamp(col++, due_date);
            stmt.setTimestamp(col++, eff_start_date);
            stmt.setTimestamp(col++, eff_end_date);
            stmt.setString(col++, status);
            stmt.setInt(col++, level1_ind);
            stmt.setInt(col++, level2_ind);
            stmt.setInt(col++, level3_ind);
            stmt.setInt(col++, level4_ind);
        //        stmt.setString(col++, attend_comment_xml);
            stmt.setString(col++, participant_target);
            stmt.setString(col++, rating_target);
            //       stmt.setString(col++, rating_actual_xml);
            //       stmt.setString(col++, rating_comment_xml);
            stmt.setString(col++, cost_target);
            stmt.setString(col++, cost_actual);
            //       stmt.setString(col++, cost_comment_xml);
            stmt.setString(col++, time_target);
            stmt.setString(col++, time_actual);
            //       stmt.setString(col++, time_comment_xml);
            //       stmt.setString(col++, comment_xml);
            //       stmt.setString(col++, attch1_xml);
            //       stmt.setString(col++, attch2_xml);
            //       stmt.setString(col++, attch3_xml);
            //       stmt.setString(col++, attch4_xml);
            stmt.setTimestamp(col++, upd_timestamp);
            stmt.setString(col++, upd_usr_id);

            stmt.setLong(col++, imt_id);
            stmt.executeUpdate();

      //update the reference for Oracle
       /* Hashtable fields = new Hashtable();
        fields.put("imt_plan_xml", plan_xml);
        fields.put("imt_attend_comment_xml", attend_comment_xml);
        fields.put("imt_rating_actual_xml", rating_actual_xml);
        fields.put("imt_rating_comment_xml", rating_comment_xml);
        fields.put("imt_cost_comment_xml", cost_comment_xml);
        fields.put("imt_time_comment_xml", time_comment_xml);
        fields.put("imt_comment_xml", comment_xml);
        fields.put("imt_attch1_xml", attch1_xml);
        fields.put("imt_attch2_xml", attch2_xml);
        fields.put("imt_attch3_xml", attch3_xml);
        fields.put("imt_attch4_xml", attch4_xml);
      */
        String columnName[]={"imt_plan_xml",
                             "imt_attend_comment_xml",
                             "imt_rating_actual_xml",
                             "imt_rating_comment_xml",
                             "imt_cost_comment_xml",
                             "imt_time_comment_xml",
                             "imt_comment_xml",
                             "imt_attch1_xml",
                             "imt_attch2_xml",
                             "imt_attch3_xml",
                             "imt_attch4_xml"
                            };
        String columnValue[]={plan_xml,
                             attend_comment_xml,
                             rating_actual_xml,
                             rating_comment_xml,
                             cost_comment_xml,
                             time_comment_xml,
                             comment_xml,
                             attch1_xml,
                             attch2_xml,
                             attch3_xml,
                             attch4_xml
                            };

        String condition = "imt_id = " + imt_id;
        cwSQL.updateClobFields(con, "aeItemMote", columnName,columnValue, condition);

            stmt.close();
    }

    public void updDefaultValue(Connection con) throws SQLException{
        super.upd(con, this.getUpdDefaultValueSQL());
    }

    // with imd_id
    public void updMoteDefaultNMote(Connection con, long itm_id, String itemType, String usr_id) throws SQLException{

        if (upd_timestamp == null){
            upd_timestamp = cwSQL.getTime(con);
        }
        upd_usr_id = usr_id;

        //MoteDefault moteDef = new MoteDefault();
        //moteDef = (MoteDefault)this.clone();
        //moteDef.upd(con);
        super.upd(con, super.getUpdSQL());

        if (itemType != null && itemType.equalsIgnoreCase("CLASSROOM")){
            Vector itm_id_lst = new Vector();
            itm_id_lst.addElement(new Long(itm_id));
            Vector vtImt = getMoteLstByItm(con, itm_id);
            for (int i=0; vtImt != null && i<vtImt.size() ; i++){
                Mote oldMote = (Mote) vtImt.elementAt(i);
                imt_id = oldMote.imt_id;
                updDefaultValue(con);
            }
        }
    }

    public Vector getMoteLstByItm(Connection con, long itm_id) throws SQLException{
            Vector itm_id_lst = new Vector();
            itm_id_lst.addElement(new Long(itm_id));
            Vector vtImt = getMoteLst(con, 0, null, itm_id_lst, null, new Vector(), null, false, false);
            return vtImt;
    }
    // return imd_id
    public long insMoteDefaultNMote(Connection con, String usr_id) throws SQLException{
        if (upd_timestamp == null || create_timestamp == null){
            upd_timestamp = cwSQL.getTime(con);
            create_timestamp = upd_timestamp;
        }
        upd_usr_id = usr_id;
        create_usr_id = usr_id;

        // ins the moteDefault
        super.ins(con, super.getInsSQL().toString());

        // in the mote
        this.ins(con);
        this.get(con);
        if (title == null){
            title = "New MOTE Report";
            upd(con);
        }
        return this.imd_id;
    }

    // return imd_id
    public long cloneMoteDefaultNMote(Connection con, long original_imd_id, String itemType, String usr_id) throws SQLException{
        this.imd_id = original_imd_id;
        // get the default value of the imd_id
        super.get(con);

        upd_timestamp = cwSQL.getTime(con);
        create_timestamp = upd_timestamp;
        upd_usr_id = usr_id;
        create_usr_id = usr_id;

        // ins the moteDefault
        super.ins(con, super.getInsSQL().toString());

        // ins mote woth new moteDefault id
        super.ins(con, this.getInsSQL(imd_id).toString());
        return imd_id;
    }

    public void delMoteDefaultNMote(Connection con) throws SQLException{
        this.del(con, DEL_BY_IMD_SQL, imd_id);
        MoteDefault moteDef = new MoteDefault();
        moteDef.imd_id = this.imd_id;
        moteDef.del(con);
    }

    public void del(Connection con)throws SQLException{
        del(con, DEL_BY_IMT_SQL, imt_id);
    }

    public void del(Connection con, String SQL, long id) throws SQLException{
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, id);
        stmt.executeUpdate();
        stmt.close();
    }


}