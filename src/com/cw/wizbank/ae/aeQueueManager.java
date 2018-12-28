package com.cw.wizbank.ae;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import com.cw.wizbank.accesscontrol.AcWorkFlow;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.accesscontrol.acSite;
import com.cw.wizbank.ae.db.DbAppnApprovalList;
import com.cw.wizbank.ae.db.DbItemTargetRuleDetail;
import com.cw.wizbank.ae.db.DbItemType;
import com.cw.wizbank.ae.db.DbTemplateView;
import com.cw.wizbank.ae.db.sql.OuterJoinSqlStatements;
import com.cw.wizbank.ae.db.sql.SqlStatements;
import com.cw.wizbank.ae.db.view.ViewAppnApprovalList;
import com.cw.wizbank.ae.db.view.ViewItemTargetGroup;
import com.cw.wizbank.cf.CFCertificate;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.config.organization.exportcols.ColType;
import com.cw.wizbank.config.organization.exportcols.DbCol;
import com.cw.wizbank.config.organization.exportcols.ExportCols;
import com.cw.wizbank.config.organization.personalization.Personalization;
import com.cw.wizbank.db.DbTable;
import com.cw.wizbank.db.view.ViewEntityToTree;
import com.cw.wizbank.db.view.ViewRoleTargetGroup;
import com.cw.wizbank.db.view.ViewSuperviseTargetEntity;
import com.cw.wizbank.newmessage.MessageService;
import com.cw.wizbank.newmessage.entity.MessageTemplate;
import com.cw.wizbank.qdb.dbCourse;
import com.cw.wizbank.qdb.dbCourseEvaluation;
import com.cw.wizbank.qdb.dbEntity;
import com.cw.wizbank.qdb.dbEntityRelation;
import com.cw.wizbank.qdb.dbMessage;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbUserGroup;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbAction;
import com.cw.wizbank.qdb.qdbEnv;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.upload.UploadEnrollment;
import com.cw.wizbank.util.EntityFullPath;
import com.cw.wizbank.util.LangLabel;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwPagination;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.cpd.service.CpdLrnAwardRecordService;
import com.cwn.wizbank.cpd.service.CpdUtilService;
import com.cwn.wizbank.entity.AeItem;
import com.cwn.wizbank.persistence.AeItemMapper;
import com.cwn.wizbank.persistence.SuperviseTargetEntityMapper;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.utils.CommonLog;
import com.cwn.wizbank.web.WzbApplicationContext;
// >>
public class aeQueueManager {

    //Session key for application queue search
    public static final String QM_APPN_ID_LST = "qm_appn_is_lst";
    public static final String QM_APPN_SESS_TIMESTAMP = "qm_appn_sess_timestamp";
    public static final String QM_APPN_PARAM = "qm_appn_param";
    public static final String QM_H_PARENT_RUN = "qm_h_parent_run";
    public static final String QM_V_FILTERED_ITM_ID = "qm_v_filtered_itm_id";

    // Hashtable keys for application queue search
    public static final String QM_APPN_CREATE_DATE_BEGIN = "qm_appn_create_date_begin";
    public static final String QM_APPN_CREATE_DATE_END = "qm_appn_create_date_end";
    public static final String QM_APPN_UPD_DATE_BEGIN = "qm_appn_upd_date_begin";
    public static final String QM_APPN_UPD_DATE_END = "qm_appn_upd_date_end";
    public static final String QM_USR_DISPLAY_BIL = "qm_usr_display_bil";
    //public static final String QM_CTB_ID = "qm_ctb_id";
    //public static final String QM_CTB_TITLE = "qm_ctb_title";
    public static final String QM_TND_TITLE = "qm_tnd_title";
    public static final String QM_ITM_TITLE = "qm_itm_title";
    public static final String QM_SLOT_START_DATE = "qm_slot_start_date";
    public static final String QM_SLOT_END_DATE = "qm_slot_end_date";
    public static final String QM_APPN_EXT3 = "qm_appn_ext3";
    public static final String QM_APPN_EXT1 = "qm_appn_ext1";
    //public static final String QM_APPN_STATUS = "qm_appn_status";
    public static final String QM_QUEUE_TYPE = "qm_queue_type";
    //public static final String QM_ORDER_BY = "qm_order_by";
    public static final String QM_SORT_ORDER = "qm_sort_order";
    public static final String QM_APPN_PARAM_TIMESTAMP = "qm_appn_param_timestamp";

    // Session Keys
    public static final String APPN_HISTORY_ID      = "appn_history_id";
    public static final String APPN_HISTORY_ACTION  = "appn_history_action";
    public static final String APPN_HISTORY_COMMENT = "appn_history_comment";
    public static final String APPN_UPD_TIMESTAMP   = "appn_upd_timestamp";

    public static final String APP_XML      = "app_xml";
    public static final String USR_APPROVER_LST  = "usr_ent_n_rol_id_lst";

    public static final String QM_INVALID_ACTION = "AEQM05";
    public static final String QM_NO_APPN_SELECTED = "AEQM06";
    public static final String QM_APPN_NOT_READY = "AEQM07";
    public static final String QM_APPN_SUCCESS = "AEQM08";

    public static final String QM_ID       = "qm_id";
    public static final String QM_CMD      = "qm_command";
    public static final String QM_TYPE     = "qm_type";
    public static final String QM_STAT     = "qm_stat";
    public static final String QM_CONTENT  = "qm_content";
    public static final String QM_SORT_BY  = "qm_sort_by";
    public static final String QM_ORDER_BY = "qm_order_by";
    public static final String QM_HISTORY  = "qm_history";

    public static final String QM_APPN_ITM_ID = "qm_appn_itm_id";
    public static final String QM_APPN_ENT_ID = "qm_appn_ent_id";
    public static final String QM_APPN_XML    = "qm_appn_xml";

    public static final int    QUEUE_SIZE  = 10;
    public static final int    APPN_SIZE   = 10;
    public static final int    ITEM_SIZE   = 10;

    public static final String OPENITEM_INVOICE = "INVOICE";
    public static final String APPNOPENITEM_APPLYEASY = "APPLYEASY";

    private static final String ENROLLMENT = "ENROLLMENT";
    private static final String WITHDRAWAL = "WITHDRAWAL";

    private static final String ENROLLED = "Enrolled";
    private static final String APPROVED = "Approved but Requested Withdrawal";
    private static final String CONFIRMED = "Confirmed but Requested Withdrawal";

	//auto_enroll_ind
	public boolean auto_enroll_ind = true;
    //send_mail_ind
    public boolean send_mail_ind=true;
    //search enroll has filter
    public boolean has_filter = false;

    public static final String EXPORT_ENROLLMENT = "ENROLLMENT";
    public static final String EXPORT_ITEM_SEARCH = "ITEM_SEARCH";
    public static final String EXPORT_HASH = "EXPORT_HASH";
    public static final String SELECT_COLS = "SELECT_COLS";
    public static final String EXTRA_COLS = "EXTRA_COLS";

    public static final String EXPORT_COLS_ROLE = "role";
    public static final String EXPORT_COLS_GROUP = "group";
    public static final String EXPORT_COLS_GRADE = "grade";
    public static final String EXPORT_COLS_DS = "direct_supervisors";
    public class aeAppRec {
    	long app_id;
    	String app_usr_display_bil;
    	Timestamp app_create_timestamp;
    }

    //change status(queue) of multiple applications
    public void updApplicationStatus(Connection con, long[] app_id_lst, String[] comment_lst
                                   ,long process_id, long status_id, long action_id
                                   ,String fr_status, String to_status, String verb
                                   ,Timestamp[] upd_timestamp_lst, String usr_id)
      throws SQLException, cwException, IOException, cwSysMessage {

        try {
            boolean hasComment = false;
            if(app_id_lst == null || upd_timestamp_lst == null)
                return;
            if(app_id_lst.length != upd_timestamp_lst.length)
                throw new cwException("app_id_lst.length != upd_timestamp_lst.length");
            if(comment_lst != null)
                hasComment = true;
            if(hasComment && comment_lst.length != app_id_lst.length)
                throw new cwException("app_id_lst.length != comment_lst.length");

            aeApplication app;
            aeAppnActnHistory actn;
            aeAppnCommHistory comm;
            aeItemRelation ire;
            long temp_itm_id;
            aeWorkFlow wkf = new aeWorkFlow(dbUtils.xmlHeader);
            StringBuffer actionBuf = new StringBuffer(300);
            actionBuf.append("<current process_id=\"").append(process_id).append("\" status_id=\"").append(status_id);
            actionBuf.append("\" action_id=\"").append(action_id).append("\"/>");
            String actionXML = new String(actionBuf);  //an xml used by aeWorkFlow
            String workFlowXML;  //workflow xml used by aeWorkFlow
            Timestamp curTime = cwSQL.getTime(con);

            for(int i=0;i<app_id_lst.length;i++) {
                //update aeApplication
                app = new aeApplication();
                app.app_id = app_id_lst[i];
                app.get(con);
                workFlowXML = app.getRawTemplate(con, aeTemplate.WORKFLOW);

                if(workFlowXML == null || workFlowXML.length() == 0)
                    throw new cwException("Cannot find workflow template for application , app_id = " + app.app_id);
                if(!wkf.checkAction(actionXML,app.app_process_xml,workFlowXML))
                    throw new cwSysMessage(QM_INVALID_ACTION);
                app.app_process_xml = wkf.checkStatus(actionXML, app.app_process_xml,workFlowXML);
                app.app_status = wkf.returnQueue(app.app_process_xml,workFlowXML);
                if (app.checkUpdTimestamp(upd_timestamp_lst[i])) {
                    app.app_upd_usr_id = usr_id;
                    app.app_upd_timestamp = curTime;
                    app.upd(con);
                }
                else
                    throw new cwSysMessage(dbMessage.INVALID_TIMESTAMP_MSG);

                //insert into aeAppnActnHistory
                actn = new aeAppnActnHistory();
                actn.aah_app_id = app.app_id;
                actn.aah_process_id = process_id;
                actn.aah_action_id = action_id;
                actn.aah_fr = fr_status;
                actn.aah_to = to_status;
                actn.aah_verb = verb;
                actn.aah_create_usr_id = usr_id;
                actn.aah_create_timestamp = curTime;
                actn.aah_upd_usr_id = usr_id;
                actn.aah_upd_timestamp = curTime;
                actn.aah_id = actn.ins(con);

                //ins into aeAppnCommHistory
                if(hasComment && comment_lst[i] != null && comment_lst[i].trim().length() > 0) {
                    comm = new aeAppnCommHistory();
                    comm.ach_app_id = app.app_id;
                    comm.ach_aah_id = actn.aah_id;
                    comm.ach_content = comment_lst[i];
                    comm.ach_create_timestamp = curTime;
                    comm.ach_create_usr_id = usr_id;
                    comm.ach_upd_timestamp = curTime;
                    comm.ach_upd_usr_id = usr_id;
                    comm.ins(con);
                }
            }
        }
        catch(qdbException e) {
            throw new cwException(e.getMessage());
        }
    }

    //generate an application list, e.g Pending List, etc.
    // type is the queue type that want to be returned
    public String applicationList(Connection con, long owner_ent_id, long usr_ent_id
                                 ,boolean showAll, Hashtable param, HttpSession sess)
      throws SQLException {
        StringBuffer xmlBuf = new StringBuffer(2500);

        //check if the result should be read from session
        boolean frSess;
        Timestamp sessTime, paramTime;
        if(sess != null) {
            sessTime = (Timestamp) sess.getAttribute(QM_APPN_SESS_TIMESTAMP);
            paramTime = (Timestamp) param.get(QM_APPN_PARAM_TIMESTAMP);
            if(sessTime != null && paramTime != null && sessTime.equals(paramTime))
                frSess = true;
            else
                frSess = false;
        }
        else
            frSess = false;

        long[] app_id_lst;
        String app_id_sql_lst="";
        String orderBy, sortOrder;
        if(frSess) {
            app_id_lst = (long[]) sess.getAttribute(QM_APPN_ID_LST);
            orderBy = (String) param.get(QM_ORDER_BY);
            sortOrder = (String) param.get(QM_SORT_ORDER);
            param = (Hashtable) sess.getAttribute(QM_APPN_PARAM);
            //put back the order by and sort order
            param.put(QM_ORDER_BY, orderBy);
            param.put(QM_SORT_ORDER, sortOrder);
            //prepare the sql lst
            app_id_sql_lst = aeUtils.prepareSQLList(app_id_lst);
            sessTime = (Timestamp) sess.getAttribute(QM_APPN_SESS_TIMESTAMP);
        }
        else
            sessTime = cwSQL.getTime(con);

        Timestamp app_create_date_begin = (Timestamp) param.get(QM_APPN_CREATE_DATE_BEGIN);
        Timestamp app_create_date_end = (Timestamp) param.get(QM_APPN_CREATE_DATE_END);
        Timestamp app_upd_date_begin = (Timestamp) param.get(QM_APPN_UPD_DATE_BEGIN);
        Timestamp app_upd_date_end = (Timestamp) param.get(QM_APPN_UPD_DATE_END);
        String usr_display_bil = (String) param.get(QM_USR_DISPLAY_BIL);
//        String ctb_id = (String) param.get(QM_CTB_ID);
//        String ctb_title = (String)param.get(QM_CTB_TITLE);
        String tnd_title = (String) param.get(QM_TND_TITLE);
        String itm_title = (String) param.get(QM_ITM_TITLE);
        Timestamp slot_start_date = (Timestamp) param.get(QM_SLOT_START_DATE);
        Timestamp slot_end_date = (Timestamp) param.get(QM_SLOT_END_DATE);
        String app_ext1 = (String) param.get(QM_APPN_EXT1);
        String app_ext3 = (String) param.get(QM_APPN_EXT3);
        //String status = (String) param.get(QM_APPN_STATUS);
        String queue_type = (String) param.get(QM_QUEUE_TYPE);
        orderBy = (String) param.get(QM_ORDER_BY);
        sortOrder = (String) param.get(QM_SORT_ORDER);

        //get applications in the queue
        StringBuffer SQLBuf = new StringBuffer (300);
        SQLBuf.append(" select app_id, app_status, app_create_timestamp, app_upd_timestamp, app_ext1, app_ext2, app_ext3 ");
        SQLBuf.append(" ,c.itm_id child_itm_id, c.itm_type child_itm_type, c.itm_eff_start_datetime slot_start_datetime ");
        SQLBuf.append(" ,p.itm_id parent_itm_id, p.itm_type parent_itm_type, p.itm_title parent_itm_title, p.itm_ext1 parent_itm_ext1, p.itm_capacity parent_itm_capacity ");
        SQLBuf.append(" ,usr_display_bil ");
        SQLBuf.append(" ,tnd_id ,tnd_title ");
        SQLBuf.append(" From aeApplication, aeItem c, aeItemRelation, aeItem p, RegUser, aeTreeNode ");
        SQLBuf.append(" where ");
        SQLBuf.append(" app_itm_id = c.itm_id ");
        SQLBuf.append(" and c.itm_id = ire_child_itm_id ");
        SQLBuf.append(" and ire_parent_itm_id = p.itm_id ");
        SQLBuf.append(" and usr_ent_id = app_ent_id ");
        SQLBuf.append(" and tnd_id = app_ext2 ");
        if(frSess)
            SQLBuf.append(" and app_id in ").append(app_id_sql_lst);
        else {
            SQLBuf.append(" and c.itm_owner_ent_id = ? ");

            if(usr_display_bil != null && usr_display_bil.length() != 0)
                SQLBuf.append(" and lower(usr_display_bil) like ? ");
            if(tnd_title != null && tnd_title.length() != 0)
                SQLBuf.append(" and lower(tnd_title) like ? ");
            if(itm_title != null && itm_title.length() != 0)
                SQLBuf.append(" and lower(p.itm_title) like ? ");
            if(slot_start_date != null && !slot_start_date.equals(aeUtils.EMPTY_DATE))
                SQLBuf.append(" and c.itm_eff_start_datetime >= ? ");
            if(slot_end_date != null && !slot_end_date.equals(aeUtils.EMPTY_DATE))
                SQLBuf.append(" and c.itm_eff_start_datetime <= ? ");
            if(app_ext1 != null && app_ext1.length() != 0)
                SQLBuf.append(" and lower(app_ext1) like ? ");
            if(app_ext3 != null && app_ext3.length() != 0)
                SQLBuf.append(" and lower(app_ext3) like ? ");
            if(queue_type != null && queue_type.length() != 0)
                SQLBuf.append(" and app_status = ? ");
            if(!showAll)
                SQLBuf.append(" and app_ent_id = ? ");
            if(app_create_date_begin != null && !app_create_date_begin.equals(aeUtils.EMPTY_DATE))
                SQLBuf.append(" and app_create_timestamp >= ? ");
            if(app_create_date_end != null && !app_create_date_end.equals(aeUtils.EMPTY_DATE))
                SQLBuf.append(" and app_create_timestamp <= ? ");
            if(app_upd_date_begin != null && !app_upd_date_begin.equals(aeUtils.EMPTY_DATE))
                SQLBuf.append(" and app_upd_timestamp >= ? ");
            if(app_upd_date_end != null && !app_upd_date_end.equals(aeUtils.EMPTY_DATE))
                SQLBuf.append(" and app_upd_timestamp <= ? ");
        }

        if(orderBy == null || orderBy.length() == 0) {
            orderBy = " app_upd_timestamp ";
            param.put(QM_ORDER_BY, orderBy);
        }
        if(sortOrder == null || sortOrder.length() == 0) {
            sortOrder = " desc ";
            param.put(QM_SORT_ORDER, sortOrder);
        }
        SQLBuf.append(" order by ").append(orderBy).append(" ").append(sortOrder);

        String SQL = new String(SQLBuf);
//        System.out.println(SQL);
        PreparedStatement stmt = con.prepareStatement(SQL);
        int index = 1;
        if(!frSess) {
            stmt.setLong(index++, owner_ent_id);
            if(usr_display_bil != null && usr_display_bil.length() != 0)
                stmt.setString(index++, usr_display_bil.toLowerCase() + "%");
            if(tnd_title != null && tnd_title.length() != 0)
                stmt.setString(index++, tnd_title.toLowerCase() + "%");
            if(itm_title != null && itm_title.length() != 0)
                stmt.setString(index++, itm_title.toLowerCase() + "%");
            if(slot_start_date != null && !slot_start_date.equals(aeUtils.EMPTY_DATE))
                stmt.setTimestamp(index++, slot_start_date);
            if(slot_end_date != null && !slot_end_date.equals(aeUtils.EMPTY_DATE))
                stmt.setTimestamp(index++, slot_end_date);
            if(app_ext1 != null && app_ext1.length() != 0)
                stmt.setString(index++, app_ext1.toLowerCase() + "%");
            if(app_ext3 != null && app_ext3.length() != 0)
                stmt.setString(index++, app_ext3.toLowerCase() + "%");
            if(queue_type != null && queue_type.length() != 0)
                stmt.setString(index++, queue_type);
            if(!showAll)
                stmt.setLong(index++, usr_ent_id);
            if(app_create_date_begin != null && !app_create_date_begin.equals(aeUtils.EMPTY_DATE))
                stmt.setTimestamp(index++, app_create_date_begin);
            if(app_create_date_end != null && !app_create_date_end.equals(aeUtils.EMPTY_DATE))
                stmt.setTimestamp(index++, app_create_date_end);
            if(app_upd_date_begin != null && !app_upd_date_begin.equals(aeUtils.EMPTY_DATE))
                stmt.setTimestamp(index++, app_upd_date_begin);
            if(app_upd_date_end != null && !app_upd_date_end.equals(aeUtils.EMPTY_DATE))
                stmt.setTimestamp(index++, app_upd_date_end);
        }

        ResultSet rs = stmt.executeQuery();
        //foramt input param as xml
        xmlBuf.append(inputParamAsXML(param)).append(dbUtils.NEWL);

        xmlBuf.append("<applications queue=\"").append(queue_type).append("\"");
        xmlBuf.append(" timestamp=\"").append(sessTime).append("\">").append(dbUtils.NEWL);
        Vector v_app_id = new Vector();
        while(rs.next()) {
            xmlBuf.append("<application id=\"").append(rs.getString("app_id")).append("\"");
            xmlBuf.append(" status=\"").append(rs.getString("app_status")).append("\"");
            xmlBuf.append(" ext1=\"").append(dbUtils.esc4XML(aeUtils.escNull(rs.getString("app_ext1")))).append("\"");
            xmlBuf.append(" ext3=\"").append(dbUtils.esc4XML(aeUtils.escNull(rs.getString("app_ext3")))).append("\"");
            xmlBuf.append(" update_timestamp=\"").append(aeUtils.escNull(rs.getTimestamp("app_upd_timestamp"))).append("\"");
            xmlBuf.append(" create_timestamp=\"").append(aeUtils.escNull(rs.getTimestamp("app_create_timestamp"))).append("\">").append(dbUtils.NEWL);
            xmlBuf.append("<applicant display_name=\"").append(dbUtils.esc4XML(aeUtils.escNull(rs.getString("usr_display_bil")))).append("\"/>").append(dbUtils.NEWL);
            xmlBuf.append("<item id=\"").append(rs.getLong("child_itm_id")).append("\"");
            xmlBuf.append(" type=\"").append(aeUtils.escNull(rs.getString("child_itm_type"))).append("\"");
            xmlBuf.append(" slot_start_datetime=\"").append(aeUtils.escNull(rs.getTimestamp("slot_start_datetime"))).append("\"/>").append(dbUtils.NEWL);
            xmlBuf.append("<parent_item id=\"").append(rs.getLong("parent_itm_id")).append("\"");
            xmlBuf.append(" type=\"").append(aeUtils.escNull(rs.getString("parent_itm_type"))).append("\"");
            xmlBuf.append(" title=\"").append(dbUtils.esc4XML(aeUtils.escNull(rs.getString("parent_itm_title")))).append("\"");
            xmlBuf.append(" ext1=\"").append(dbUtils.esc4XML(aeUtils.escNull(rs.getString("parent_itm_ext1")))).append("\"");
            xmlBuf.append(" capacity=\"").append(rs.getLong("parent_itm_capacity")).append("\"/>");
            xmlBuf.append("<parent_node id=\"").append(aeUtils.escZero(rs.getLong("tnd_id"))).append("\"");
            xmlBuf.append(" title=\"").append(dbUtils.esc4XML(aeUtils.escNull(rs.getString("tnd_title")))).append("\"/>").append(dbUtils.NEWL);
            //xmlBuf.append("<code type=\"").append(dbUtils.esc4XML(aeTreeNode.escNull(rs.getString("ctb_type")))).append("\"");
            //xmlBuf.append(" id=\"").append(dbUtils.esc4XML(aeTreeNode.escNull(rs.getString("ctb_id")))).append("\"");
            //xmlBuf.append(" title=\"").append(dbUtils.esc4XML(aeTreeNode.escNull(rs.getString("ctb_title")))).append("\"/>");
            xmlBuf.append("</application>").append(dbUtils.NEWL);
            v_app_id.addElement(new Long(rs.getLong("app_id")));
        }
        xmlBuf.append("</applications>");
        stmt.close();
        if(!frSess) {
            app_id_lst = aeUtils.vec2longArray(v_app_id);
            sess.setAttribute(QM_APPN_ID_LST,app_id_lst);
            sess.setAttribute(QM_APPN_SESS_TIMESTAMP,sessTime);
            sess.setAttribute(QM_APPN_PARAM, param);
        }
        return xmlBuf.toString();
    }

    //format the input param for application search
    public static String inputParamAsXML(Hashtable param) {
        Timestamp app_create_date_begin = (Timestamp) param.get(QM_APPN_CREATE_DATE_BEGIN);
        Timestamp app_create_date_end = (Timestamp) param.get(QM_APPN_CREATE_DATE_END);
        Timestamp app_upd_date_begin = (Timestamp) param.get(QM_APPN_UPD_DATE_BEGIN);
        Timestamp app_upd_date_end = (Timestamp) param.get(QM_APPN_UPD_DATE_END);
        String usr_display_bil = (String) param.get(QM_USR_DISPLAY_BIL);
        String tnd_title = (String) param.get(QM_TND_TITLE);
        String itm_title = (String) param.get(QM_ITM_TITLE);
        Timestamp slot_start_date = (Timestamp) param.get(QM_SLOT_START_DATE);
        Timestamp slot_end_date = (Timestamp) param.get(QM_SLOT_END_DATE);
        String itm_ext1 = (String) param.get(QM_APPN_EXT3);
        String queue_type = (String) param.get(QM_QUEUE_TYPE);
        String orderBy = ((String) param.get(QM_ORDER_BY)).trim();
        String sortOrder = ((String) param.get(QM_SORT_ORDER)).trim();
        String app_ext1 = (String) param.get(QM_APPN_EXT1);
        String app_ext3 = (String) param.get(QM_APPN_EXT3);

        StringBuffer xmlBuf = new StringBuffer(1000);
        xmlBuf.append("<input app_create_date_begin=\"").append(aeUtils.escEmptyDate(app_create_date_begin)).append("\"");
        xmlBuf.append(" app_create_date_end=\"").append(aeUtils.escEmptyDate(app_create_date_end)).append("\"");
        xmlBuf.append(" app_upd_date_begin=\"").append(aeUtils.escEmptyDate(app_upd_date_begin)).append("\"");
        xmlBuf.append(" app_upd_date_end=\"").append(aeUtils.escEmptyDate(app_upd_date_end)).append("\"");
        xmlBuf.append(" usr_display_bil=\"").append(dbUtils.esc4XML(aeUtils.escNull(usr_display_bil))).append("\"");
        xmlBuf.append(" app_ext1=\"").append(dbUtils.esc4XML(aeUtils.escNull(app_ext1))).append("\"");
        xmlBuf.append(" app_ext3=\"").append(dbUtils.esc4XML(aeUtils.escNull(app_ext3))).append("\"");
        xmlBuf.append(" tnd_title=\"").append(dbUtils.esc4XML(aeUtils.escNull(tnd_title))).append("\"");
        xmlBuf.append(" itm_title=\"").append(dbUtils.esc4XML(aeUtils.escNull(itm_title))).append("\"");
        xmlBuf.append(" start_date=\"").append(aeUtils.escNull(aeUtils.escEmptyDate(slot_start_date))).append("\"");
        xmlBuf.append(" end_date=\"").append(aeUtils.escNull(aeUtils.escEmptyDate(slot_end_date))).append("\"");
        xmlBuf.append(" ext1=\"").append(aeUtils.escNull(itm_ext1)).append("\"");
        xmlBuf.append(" status=\"").append(aeUtils.escNull(queue_type)).append("\"");
        xmlBuf.append(" orderby=\"").append(aeUtils.escNull(orderBy)).append("\"");
        xmlBuf.append(" sortorder=\"").append(aeUtils.escNull(sortOrder)).append("\"/>").append(dbUtils.NEWL);
        String xml = new String(xmlBuf);
        return xml;
    }

    public String pendingApplications(Connection con, HttpSession sess, long root_ent_id, int page, String sort_by, String order_by, boolean showHistory)
        throws SQLException, qdbException
    {
        StringBuffer result = new StringBuffer();
        aeApplication app;
        long[] queue;
        Long sess_itm_id;
        String sess_cmd;
        String sess_sort_by;
        String sess_order_by;
        String sess_history;
        String cmd = "pendingApplication";

        if (sort_by == null || sort_by.length() == 0) {
            sort_by = aeApplication.ORDER_APP[0];
        }

        if (order_by == null || order_by.length() == 0) {
            order_by = "ASC";
        }

        sess_cmd = (String)sess.getAttribute(QM_CMD);
        sess_itm_id = (Long)sess.getAttribute(QM_ID);
        sess_sort_by = (String)sess.getAttribute(QM_SORT_BY);
        sess_order_by = (String)sess.getAttribute(QM_ORDER_BY);
        sess_history = (String)sess.getAttribute(QM_HISTORY);

        if (sess_cmd != null && sess_cmd.equals(cmd) &&
            sess_itm_id.longValue() == 0 &&
            sess_sort_by != null && sess_sort_by.equals(sort_by) &&
            sess_order_by != null && sess_order_by.equals(order_by) &&
            sess_history != null && sess_history.equals((new Boolean(showHistory)).toString()) &&
            page != 0) {
            queue = (long[])sess.getAttribute(QM_CONTENT);
//System.out.println("Debugging: From Session");
        } else {
            if (page == 0) {
                page = 1;
            }

            queue = aeApplication.getQueue(con, root_ent_id, aeApplication.PENDING, sort_by, order_by, showHistory);

            sess.setAttribute(QM_ID, new Long(0));
            sess.setAttribute(QM_CMD, cmd);
            sess.setAttribute(QM_SORT_BY, sort_by);
            sess.setAttribute(QM_ORDER_BY, order_by);
            sess.setAttribute(QM_HISTORY, new Boolean(showHistory).toString());
            sess.removeAttribute(QM_STAT);
            sess.removeAttribute(QM_TYPE);

            if (queue != null) {
                sess.setAttribute(QM_CONTENT, queue);
            } else {
                sess.removeAttribute(QM_CONTENT);
            }
//System.out.println("Debugging: From Database");
        }

        result.append("<pending_applications total=\"");

        if (queue == null) {
            result.append("0");
        } else {
            result.append(queue.length);
        }

        result.append("\" page_size=\"");
        result.append(APPN_SIZE);
        result.append("\" cur_page=\"");
        result.append(page);
        result.append("\" sort_by=\"");
        result.append(sort_by);
        result.append("\" order_by=\"");
        result.append(order_by);
        result.append("\">");

        if (queue != null) {
            int count = (page-1)*APPN_SIZE;

            for (int i=count; i<queue.length && i<count+APPN_SIZE; i++) {
                app = new aeApplication();
                app.app_id = queue[i];
                app.getWithItem(con);
                result.append(app.contentAsXML(con, null, true, false, false));
            }
        }

        result.append("</pending_applications>");
        result.append(dbUtils.NEWL);

        return result.toString();
    }

    public String pendingItems(Connection con, HttpSession sess, long root_ent_id, int page, String sort_by, String order_by, boolean showHistory)
        throws SQLException, qdbException, cwSysMessage
    {
        StringBuffer result = new StringBuffer();
        aeItem item;
        long[] queue;
        Long sess_itm_id;
        String sess_cmd;
        String sess_sort_by;
        String sess_order_by;
        String sess_history;
        String cmd = "pendingItems";
        Timestamp cur_time = cwSQL.getTime(con);

        if (sort_by == null || sort_by.length() == 0) {
            sort_by = aeItem.ORDER[0];
        }

        if (order_by == null || order_by.length() == 0) {
            order_by = "ASC";
        }

        sess_cmd = (String)sess.getAttribute(QM_CMD);
        sess_itm_id = (Long)sess.getAttribute(QM_ID);
        sess_sort_by = (String)sess.getAttribute(QM_SORT_BY);
        sess_order_by = (String)sess.getAttribute(QM_ORDER_BY);
        sess_history = (String)sess.getAttribute(QM_HISTORY);

        if (sess_cmd != null && sess_cmd.equals(cmd) &&
            sess_itm_id.longValue() == 0 &&
            sess_sort_by != null && sess_sort_by.equals(sort_by) &&
            sess_order_by != null && sess_order_by.equals(order_by) &&
            sess_history != null && sess_history.equals((new Boolean(showHistory)).toString()) &&
            page != 0) {
            queue = (long[])sess.getAttribute(QM_CONTENT);
//System.out.println("Debugging: From Session");
        } else {
            if (page == 0) {
                page = 1;
            }

            queue = aeItem.getQueue(con, root_ent_id, sort_by, order_by, showHistory);

            sess.setAttribute(QM_ID, new Long(0));
            sess.setAttribute(QM_CMD, cmd);
            sess.setAttribute(QM_SORT_BY, sort_by);
            sess.setAttribute(QM_ORDER_BY, order_by);
            sess.setAttribute(QM_HISTORY, new Boolean(showHistory).toString());
            sess.removeAttribute(QM_STAT);
            sess.removeAttribute(QM_TYPE);

            if (queue != null) {
                sess.setAttribute(QM_CONTENT, queue);
            } else {
                sess.removeAttribute(QM_CONTENT);
            }
//System.out.println("Debugging: From Database");
        }

        result.append("<pending_items total=\"");

        if (queue == null) {
            result.append("0");
        } else {
            result.append(queue.length);
        }

        result.append("\" page_size=\"");
        result.append(ITEM_SIZE);
        result.append("\" cur_page=\"");
        result.append(page);
        result.append("\" sort_by=\"");
        result.append(sort_by);
        result.append("\" order_by=\"");
        result.append(order_by);
        result.append("\">");

        if (queue != null) {
            int count = (page-1)*APPN_SIZE;

            for (int i=count; i<queue.length && i<count+APPN_SIZE; i++) {
                item = new aeItem();
                item.itm_id = queue[i];
                item.getItem(con);
                result.append(item.contentAsXML(con, cur_time));
            }
        }

        result.append("</pending_items>");
        result.append(dbUtils.NEWL);

        return result.toString();
    }


    public String processQueue(Connection con, HttpSession sess, long root_ent_id, long itm_id, String queue_type, int page, String sort_by, String order_by)
        throws SQLException, qdbException ,cwSysMessage, cwException
    {
        StringBuffer result = new StringBuffer();
        aeApplication app;
        aeItem item = new aeItem();
        long[] queue;
        int size;
        Long sess_itm_id;
        String sess_cmd;
        String sess_queue_type;
        String sess_order_by;
        String sess_sort_by;
        String stat_xml;
        String cmd = "processingQueue";

        if (sort_by == null || sort_by.length() == 0) {
            sort_by = aeApplication.ORDER_APP_BY_ITEM[0];
        }

        if (order_by == null || order_by.length() == 0) {
            order_by = "ASC";
        }

        if (queue_type == null || queue_type.length() == 0) {
            queue_type = "all";
        }

        sess_cmd = (String)sess.getAttribute(QM_CMD);
        sess_itm_id = (Long)sess.getAttribute(QM_ID);
        sess_queue_type = (String)sess.getAttribute(QM_TYPE);
        sess_sort_by = (String)sess.getAttribute(QM_SORT_BY);
        sess_order_by = (String)sess.getAttribute(QM_ORDER_BY);

        if (sess_cmd != null && sess_cmd.equals(cmd) &&
            sess_itm_id.longValue() == itm_id &&
            sess_queue_type != null && sess_queue_type.equals(queue_type) &&
            sess_sort_by != null && sort_by.equals(sort_by) &&
            sess_order_by != null && order_by.equals(order_by) &&
            page != 0) {
            queue = (long[])sess.getAttribute(QM_CONTENT);
            stat_xml = (String)sess.getAttribute(QM_STAT);
//System.out.println("Debugging: From Session");

        } else {
            if (page == 0) {
                page = 1;
            }

            queue = aeApplication.getQueueFromItem(con, root_ent_id, itm_id, queue_type, sort_by, order_by);
            stat_xml = aeApplication.queueStatAsXML(con, itm_id, queue_type);

            sess.setAttribute(QM_ID, new Long(itm_id));
            sess.setAttribute(QM_CMD, cmd);
            sess.setAttribute(QM_TYPE, queue_type);
            sess.setAttribute(QM_STAT, stat_xml);
            sess.setAttribute(QM_SORT_BY, sort_by);
            sess.setAttribute(QM_ORDER_BY, order_by);

            if (queue != null) {
                sess.setAttribute(QM_CONTENT, queue);
            } else {
                sess.removeAttribute(QM_CONTENT);
            }
//System.out.println("Debugging: From Database");
        }

        item.itm_id = itm_id;
        item.getItem(con);


        if (page == -1) {
            size = queue.length;
            page = 1;
        } else {
            size = QUEUE_SIZE;
        }

        result.append(item.contentAsXML(con, null));
        result.append(stat_xml);
        result.append("<application_list total=\"");

        if (queue == null) {
            result.append("0");
        } else {
            result.append(queue.length);
        }

        result.append("\" page_size=\"");
        result.append(size);
        result.append("\" cur_page=\"");
        result.append(page);
        result.append("\" sort_by=\"");
        result.append(sort_by);
        result.append("\" order_by=\"");
        result.append(order_by);
        result.append("\">");
        result.append(dbUtils.NEWL);
        Vector entIdVec = new Vector();
        if (queue != null) {
            int count;
            if (page == -1) {
                count = 0;
            } else {
                count = (page-1)*QUEUE_SIZE;
            }

            int index=0;
            for (int i=count; i<queue.length && i<count+size; i++) {
                app = new aeApplication();
                app.app_id = queue[i];
                app.get(con);
                entIdVec.addElement(new Long(app.app_ent_id));
                result.append(app.contentAsXML(con, null, false, true, false));
            }

        }
        result.append("</application_list>");



        result.append(dbUtils.NEWL);

        return result.toString();
    }

    public String exportQueue(Connection con, HttpSession sess, long root_ent_id, long itm_id, String queue_type, String sort_by)
        throws SQLException, qdbException ,cwSysMessage
    {
        StringBuffer result = new StringBuffer();

        if (sort_by == null || sort_by.length() == 0) {
            sort_by = aeApplication.ORDER_APP_BY_ITEM[0];
        }

        if (queue_type == null || queue_type.length() == 0) {
            queue_type = "all";
        }

        result.append(aeApplication.printQueueFromItem(con, root_ent_id, itm_id, queue_type, sort_by));

        return result.toString();
    }

    public String processApplication(Connection con, HttpSession sess, long app_id, String itm_tvw_id, String app_tvw_id, qdbEnv inEnv, long owner_ent_id)
        throws SQLException, cwException, cwSysMessage
    {
        return processApplication(con, sess, app_id, itm_tvw_id, app_tvw_id, inEnv, owner_ent_id, null);
    }

    public String processApplication(Connection con, HttpSession sess, long app_id, String itm_tvw_id, String app_tvw_id, qdbEnv inEnv, long owner_ent_id, loginProfile prof)
        throws SQLException, cwException, cwSysMessage
    {
        try {
        StringBuffer result = new StringBuffer();
        aeApplication app = new aeApplication();
        aeItem item = new aeItem();
        //aeTemplate tpl = new aeTemplate();
        long tpl_id;
        Long sess_app_id;
        String sess_cmd;
        String stat_xml;
        String cmd = "processingApplicant";
        Timestamp sess_upd_timestamp;

        app.app_id = app_id;
        app.getWithItem(con);
        item.itm_id = app.app_itm_id;
        //tpl_id = item.getTemplateId(con, aeTemplate.WORKFLOW);
        //tpl.tpl_id = tpl_id;
        //tpl.get(con);

        sess_cmd = (String)sess.getAttribute(QM_CMD);
        sess_app_id = (Long)sess.getAttribute(QM_ID);
        sess_upd_timestamp = (Timestamp)sess.getAttribute(APPN_UPD_TIMESTAMP);

        if (sess_cmd != null && sess_cmd.equals(cmd) &&
            sess_app_id.longValue() == app_id) {
            stat_xml = (String)sess.getAttribute(QM_STAT);
//System.out.println("Debugging: From Session");
        } else {
            stat_xml = aeApplication.queueStatAsXML(con, app.app_itm_id, app.app_status);

            sess.setAttribute(QM_ID, new Long(app_id));
            sess.setAttribute(QM_CMD, cmd);
            sess.removeAttribute(QM_TYPE);
            sess.setAttribute(QM_STAT, stat_xml);
            sess.removeAttribute(QM_CONTENT);
            sess.removeAttribute(QM_SORT_BY);
            sess.removeAttribute(QM_ORDER_BY);
//System.out.println("Debugging: From Database");
        }

        result.append(app.itemAsXML(con));
        result.append("<update_timestamp>").append(sess_upd_timestamp).append("</update_timestamp>").append(dbUtils.NEWL);

        //get application form
        aeTemplate appnFormTpl = new aeTemplate();
        appnFormTpl.tpl_id = item.getTemplateId(con, aeTemplate.APPNFORM);
        appnFormTpl.get(con);
        DbTemplateView dbTplVi = new DbTemplateView();
        int cnt = 0;
        do {
            if(cnt > 1) {
                break;
            }
            if(cnt > 0) {
                app_tvw_id = "DETAIL_VIEW";
            }
            dbTplVi.tvw_tpl_id = appnFormTpl.tpl_id;
            dbTplVi.tvw_id = app_tvw_id;
            dbTplVi.get(con);
            cnt ++;
        } while(dbTplVi.tvw_xml == null);
        StringBuffer detailXML = new StringBuffer(1024);
        detailXML.append("<applyeasy>")
                    .append(dbTplVi.tvw_xml)
                    .append(appnFormTpl.tpl_xml)
                    .append(app.app_xml)
                    .append("</applyeasy>");
        //System.out.println(detailXML);
        result.append(aeUtils.transformXML(detailXML.toString(), inEnv.INI_XSL_VALTPL, inEnv, null)).append(dbUtils.NEWL);
        //Check if app_ent_id is a targeted learner of app_itm_id
        aeItem itm = new aeItem();
        itm.itm_id = app.app_itm_id;
        itm.getItem(con);
        String[] tree_sub_type_lst = ViewEntityToTree.getTargetEntity(con, owner_ent_id);

        if (tree_sub_type_lst != null && tree_sub_type_lst.length > 0) {
            //Vector v_target_lrn = itm.getTargetLrn(con);
            result.append("<targeted_lrn_ind>")
                //.append(v_target_lrn.contains(new Long(app.app_ent_id)))
                .append(aeItem.isTargetedLearner(con, app.app_ent_id, itm.itm_id, true))
                .append("</targeted_lrn_ind>")
                .append(dbUtils.NEWL);
        }

        //check if the login user is the application's current approver
        if(prof !=null ) {
            result.append("<current_approver_ind>")
                .append(DbAppnApprovalList.isCurrentApprover(con, app_id, prof.usr_ent_id))
                .append("</current_approver_ind>");
        }

        //if template view id is given, get the item's item template
        if(itm_tvw_id != null && itm_tvw_id.length() > 0) {
            result.append("<item_template>");
            result.append(app.getItemApplyView(con, itm_tvw_id, inEnv));
            result.append("</item_template>");
        }
        result.append(stat_xml);
        result.append(app.workFlowTpl);
        result.append(app.contentAsXML(con, sess, false, true, true, true));

        if (! app.app_status.equals(aeApplication.WITHDRAWN)) {
            result.append("<queue_action name=\"Withdrawn\"/>");
        }

        return result.toString();
        }
        catch(qdbException e) {
            throw new cwException(e.getMessage());
        }
    }


/*    public void insAppnComment(Connection con, HttpSession sess, String usr_id, long app_id, long aah_id, String content)
        throws qdbException, SQLException
    {
        aeAppnCommHistory comm = new aeAppnCommHistory();
        Long appn_id;
        Vector appn_history;
        Timestamp cur_timestamp;
        Timestamp appn_upd_timestamp;
        aeApplication app = new aeApplication();

        app.app_id = app_id;
        app.get(con);

        cur_timestamp = dbUtils.getTime(con);
        comm.ach_app_id = app_id;
        comm.ach_aah_id = aah_id;
        comm.ach_content = content;
        comm.ach_create_usr_id = usr_id;
        comm.ach_create_timestamp = cur_timestamp;
        comm.ach_upd_usr_id = usr_id;
        comm.ach_upd_timestamp = cur_timestamp;

        appn_id = (Long)sess.getAttribute(APPN_HISTORY_ID);
        appn_history = (Vector)sess.getAttribute(APPN_HISTORY_COMMENT);
        appn_upd_timestamp = (Timestamp)sess.getAttribute(APPN_UPD_TIMESTAMP);

        if (appn_history == null) {
            appn_history = new Vector();
        }

        if (appn_id != null && appn_id.longValue() == comm.ach_app_id && appn_upd_timestamp != null) {
            appn_history.addElement(comm);
        } else {
            appn_history.addElement(comm);
            sess.setAttribute(APPN_UPD_TIMESTAMP, app.app_upd_timestamp);
            sess.setAttribute(APPN_HISTORY_ID, new Long(comm.ach_app_id));
            sess.removeAttribute(APPN_HISTORY_ACTION);
        }

        sess.setAttribute(APPN_HISTORY_COMMENT, appn_history);
    }*/

    public void insAppnComment(Connection con, String usr_id, long app_id, long aah_id, String content)
        throws qdbException, SQLException
    {
        aeAppnCommHistory comm = new aeAppnCommHistory();
        Timestamp cur_timestamp = dbUtils.getTime(con);

        comm.ach_app_id = app_id;
        comm.ach_aah_id = aah_id;
        comm.ach_content = content;
        comm.ach_create_usr_id = usr_id;
        comm.ach_create_timestamp = cur_timestamp;
        comm.ach_upd_usr_id = usr_id;
        comm.ach_upd_timestamp = cur_timestamp;
        comm.ins(con);
    }

    public void insAppnAction(Connection con, HttpSession sess, String usr_id, long app_id, long process_id,
                              String fr, String to, String verb, long action_id, long status_id,
                              long usr_ent_id, String rol_ext_id)
        throws SQLException, IOException, cwSysMessage, qdbException, cwException
    {
//        System.out.println("app_id = " + app_id);
//        System.out.println("process_id = " + process_id);
//        System.out.println("fr = " + fr);
//        System.out.println("to = " + to);
//        System.out.println("verb = " + verb);
//        System.out.println("action_id = " + action_id);
//        System.out.println("status_id = " + status_id);
//        System.out.println("rol_ext_id = " + rol_ext_id);


        aeApplication app = new aeApplication();
        aeAppnActnHistory actn = new aeAppnActnHistory();
        aeTemplate tpl = new aeTemplate();
        aeItem item = new aeItem();
        long tpl_id;
        Long appn_id;
        Vector appn_history;
        Timestamp cur_timestamp;
        Timestamp appn_upd_timestamp;
        aeWorkFlow workFlow = new aeWorkFlow(dbUtils.xmlHeader);
        String curStatus;
        StringBuffer action = new StringBuffer();

        app.app_id = app_id;
        app.getWithItem(con);
        item.itm_id = app.app_itm_id;
        tpl_id = item.getTemplateId(con, aeTemplate.WORKFLOW);
        tpl.tpl_id = tpl_id;
        tpl.get(con);

        action.append("<current process_id=\"").append(process_id).append("\" status_id=\"").append(status_id);
        action.append("\" action_id=\"").append(action_id).append("\"/>");

        cur_timestamp = dbUtils.getTime(con);
        actn.aah_app_id = app_id;
        actn.aah_process_id = process_id;
        actn.aah_fr = fr;
        actn.aah_to = to;
        actn.status_id = status_id;
        actn.aah_verb = verb;
        actn.aah_action_id = action_id;
        actn.aah_create_usr_id = usr_id;
        actn.aah_create_timestamp = cur_timestamp;
        actn.aah_upd_usr_id = usr_id;
        actn.aah_upd_timestamp = cur_timestamp;

        appn_id = (Long)sess.getAttribute(APPN_HISTORY_ID);
        appn_history = (Vector)sess.getAttribute(APPN_HISTORY_ACTION);
        appn_upd_timestamp = (Timestamp)sess.getAttribute(APPN_UPD_TIMESTAMP);

        if (appn_id != null && appn_id.longValue() == actn.aah_app_id &&
            appn_history != null && appn_history.size() != 0 && appn_upd_timestamp != null) {
            aeAppnActnHistory tempAction = (aeAppnActnHistory)appn_history.elementAt(appn_history.size()-1);
            curStatus = tempAction.status;
        } else {
            curStatus = app.app_process_xml;
            appn_history = new Vector();
        }
        AcWorkFlow acWorkFlow = new AcWorkFlow(con, tpl.tpl_xml);


        if (workFlow.checkAction(action.toString(), curStatus, tpl.tpl_xml) == true &&
            acWorkFlow.checkPrivilege(usr_ent_id, rol_ext_id, process_id, status_id, action_id, app, false)) {
            actn.status = workFlow.checkStatus(action.toString(), curStatus, tpl.tpl_xml);
        } else {
            //throw new qdbException("This action is invalid.");
            throw new cwSysMessage(QM_INVALID_ACTION);
        }


        if (appn_id != null && appn_id.longValue() == actn.aah_app_id && appn_upd_timestamp != null) {
            appn_history.addElement(actn);
        } else {
            appn_history.addElement(actn);
            sess.setAttribute(APPN_UPD_TIMESTAMP, app.app_upd_timestamp);
            sess.setAttribute(APPN_HISTORY_ID, new Long(actn.aah_app_id));
            sess.removeAttribute(APPN_HISTORY_COMMENT);
        }

        sess.setAttribute(APPN_HISTORY_ACTION, appn_history);
    }

    public String insMultiAppnAction(Connection con, HttpSession sess, String usr_id, long[] app_id_lst, long process_id,
                                     String fr, String to, String verb, long action_id, long status_id, loginProfile prof)
        throws SQLException, IOException, cwSysMessage, qdbException, cwException
    {
        if(app_id_lst == null || app_id_lst.length == 0) {
            throw new cwSysMessage(QM_NO_APPN_SELECTED);
        }

        Timestamp cur_time = cwSQL.getTime(con);

        //format XML
        StringBuffer xmlBuf = new StringBuffer(1024);
        xmlBuf.append("<process id=\"").append(process_id).append("\"")
              .append(" status_id=\"").append(status_id).append("\"")
              .append(" action_id=\"").append(action_id).append("\"")
              .append(" fr=\"").append(fr).append("\"")
              .append(" to=\"").append(to).append("\"")
              .append(" verb=\"").append(verb).append("\"/>");
        xmlBuf.append("<application_list>");
        aeApplication app = new aeApplication();
        aeItem itm = new aeItem();
        dbRegUser usr = new dbRegUser();
        aeItemRelation ire = new aeItemRelation();
        for(int i=0;i<app_id_lst.length; i++) {
            insAppnAction(con, sess, usr_id, app_id_lst[0], process_id, fr, to, verb, action_id, status_id, prof.usr_ent_id, prof.common_role_id);

            app.app_id = app_id_lst[i];

            app.getWithItem(con);

            itm.itm_id = app.app_itm_id;
            itm.getItem(con);

            usr.usr_ent_id = app.app_ent_id;

            usr.get(con);

            xmlBuf.append("<application id=\"").append(app.app_id).append("\"")
                  .append(" timestamp=\"").append(app.app_upd_timestamp).append("\">");
            xmlBuf.append(itm.contentAsXML(con, cur_time));
            xmlBuf.append(usr.getUserXML(con, prof));
            xmlBuf.append("</application>");
        }
        xmlBuf.append("</application_list>");
        return xmlBuf.toString();
    }

    public void makeMultiRemoveActn(Connection con, loginProfile prof,
                                    long[] app_id_lst, Timestamp time_lst[])
        throws SQLException, IOException, cwSysMessage, qdbException, cwException {

        if(app_id_lst == null || app_id_lst.length == 0) {
            throw new cwSysMessage(QM_NO_APPN_SELECTED);
        }
        Timestamp cur_time = cwSQL.getTime(con);
        
        String fr = "";
        for(int i=0; i<app_id_lst.length; i++) {
            aeApplication app = new aeApplication();
            app.app_id = app_id_lst[i];
            Vector vRemoveAction = app.getCurrentRemoveAction(con);
            String processId    = (String) vRemoveAction.elementAt(0);
            String statusId     = (String) vRemoveAction.elementAt(1);
            String actionId     = (String) vRemoveAction.elementAt(2);
            String fromStatus   = (String) vRemoveAction.elementAt(3);
            String toStatus     = (String) vRemoveAction.elementAt(4);
            String verb         = (String) vRemoveAction.elementAt(5);
            
            fr += fromStatus + ",";
            doAppnActn(con, app_id_lst[i], Long.parseLong(processId),
                       fromStatus, toStatus, verb, Long.parseLong(actionId),
                       Long.parseLong(statusId), prof, null,
                       time_lst[i], cur_time, false);
        }
        String[] arr_fr = fr.split(",");
        int count = 0;
        for(int i=0; i<arr_fr.length; i++) {
        	if("Enrolled".equals(arr_fr[i]) || "Pending Approval".equals(arr_fr[i]))
        		count++;
        }
        for(int i=0; i<count; i++) {
        	doAppnAutoIntoQueue(con, prof, prof.cur_itm_id);
        }
        return;
    }
    
    /**
     * Waiting list自動按先進先出原則處理
     * @param con
     * @param prof
     * @param fr	课程审批状态
     */
    public void doAppnAutoIntoQueue(Connection con, loginProfile prof, long itm_id) throws SQLException, qdbException, IOException, cwSysMessage, cwException {
    	aeItem item = new aeItem();
    	aeQueueManager qm = new aeQueueManager();
    	aeApplication app = null;
    	item.itm_id = itm_id;
    	item.getItem(con);
    	int enrol_cnt = 0;
    	if(item.itm_app_approval_type != null) {
    		enrol_cnt = aeApplication.countItemAppnAndPend(con, item.itm_id);
    	} else {
    		enrol_cnt = aeApplication.countItemAppn(con, item.itm_id, true);
    	}
    	if(item.itm_capacity > 0 && enrol_cnt < item.itm_capacity) {
    		app = aeApplication.getFirstWaitingLearner(con, item.itm_id);
    		if(app != null) {
    			aeApplication.deleteWaitingLearner(con, app.app_id);
    			prof.isCancelEnrol = true;
    			if(item.itm_app_approval_type != null) {
    				qm.auto_enroll_ind = false;
    				qm.insApplication(con, null, app.app_ent_id,
    						item.itm_id, prof, 0, 0, 0, 0, null, null, false, 0, null);
    			} else {
    				qm.auto_enroll_ind = true;
    				item.itm_not_allow_waitlist_ind = true;
    				qm.insAppNoWorkflow(con, null, app.app_ent_id, item.itm_id, null,null, prof, item);
    			}
    			aeApplication.updateCreateUsrId(con, app.app_ent_id, item.itm_id);
    			prof.isCancelEnrol = false;
    		}
    	}
    }

    public void makeMultiActn(Connection con, loginProfile prof,
                              long[] app_id_lst, String content,
                              Timestamp time_lst[], long process_id,
                              String fr, String to, String verb, long action_id,
                              long status_id)
        throws SQLException, IOException, cwSysMessage, qdbException, cwException {
        makeMultiActn(con, prof, app_id_lst, content, time_lst, process_id, fr, to, verb, action_id, status_id, null);
    }

    public void makeMultiActn(Connection con, loginProfile prof,
                              long[] app_id_lst, String content,
                              Timestamp time_lst[], long process_id,
                              String fr, String to, String verb, long action_id,
                              long status_id, String[] app_priority_lst)
        throws SQLException, IOException, cwSysMessage, qdbException, cwException {

        if(app_id_lst == null || app_id_lst.length == 0) {
            throw new cwSysMessage(QM_NO_APPN_SELECTED);
        }
        Timestamp cur_time = cwSQL.getTime(con);
        /*
        if(content_lst == null) {
            content_lst = new String[app_id_lst.length];
        }
        */

        for(int i=0; i<app_id_lst.length; i++) {
            String app_priority = null;

            if (app_priority_lst != null) {
                app_priority = app_priority_lst[i];
//                System.out.println("app_priority_lst[i] " + app_priority_lst[i]);
            }

            doAppnActn(con, app_id_lst[i], process_id,
                       fr, to, verb, action_id,
                       status_id, prof, content,
                       time_lst[i], cur_time, false, app_priority);
        }
        
        doAppnAutoIntoQueue(con, prof, prof.cur_itm_id);
        return;
    }

    public void insMultiHistory2DB(Connection con, HttpSession sess, loginProfile prof, long[] app_id_lst, String content_lst[], Timestamp time_lst[])
        throws SQLException, IOException, cwSysMessage, qdbException, cwException {

        if(app_id_lst == null || app_id_lst.length == 0) {
            throw new cwSysMessage(QM_NO_APPN_SELECTED);
        }
        Vector appn_action = (Vector)sess.getAttribute(APPN_HISTORY_ACTION);
        for(int i=0; i<app_id_lst.length; i++) {
            sess.setAttribute(APPN_HISTORY_ID, new Long(app_id_lst[i]));
            sess.setAttribute(APPN_HISTORY_ACTION, appn_action);
            insHistory2DB(con, sess, prof, app_id_lst[i], content_lst[i], time_lst[i], false);
        }
        return;
    }

    public void insHistory2DB(Connection con, HttpSession sess, loginProfile prof, long app_id, String content, Timestamp time, boolean triggerAction)
        throws SQLException, IOException, cwSysMessage, qdbException, cwException
    {
        try {
            Long appn_id;
            long tpl_id;
            String oldQueue = null;
            String newQueue = null;
            String lastest_status = null;
            aeApplication app = new aeApplication();
            aeWorkFlow workFlow = new aeWorkFlow(dbUtils.xmlHeader);
            aeAppnActnHistory actn;
            aeAppnCommHistory comm;
            aeTemplate tpl = new aeTemplate();
            aeItem item = new aeItem();
            Vector appn_action;
            Vector appn_comment;
            dbCourse cos = new dbCourse();

            appn_id = (Long)sess.getAttribute(APPN_HISTORY_ID);
            appn_action = (Vector)sess.getAttribute(APPN_HISTORY_ACTION);
            app.app_id = app_id;
            app.getWithItem(con);
            if (appn_id.longValue() == app_id) {
                saveAppnActn(con, app,
                            content, false,
                            appn_action,
                            time,
                            prof);
            }
        }
        finally {
            sess.removeAttribute(QM_ID);
            sess.removeAttribute(QM_CMD);
            sess.removeAttribute(QM_TYPE);
            sess.removeAttribute(QM_STAT);
            sess.removeAttribute(QM_CONTENT);
            sess.removeAttribute(QM_SORT_BY);
            sess.removeAttribute(QM_ORDER_BY);
            sess.removeAttribute(QM_HISTORY);
            sess.removeAttribute(APPN_HISTORY_ID);
            sess.removeAttribute(APPN_HISTORY_ACTION);
            sess.removeAttribute(APPN_HISTORY_COMMENT);
            sess.removeAttribute(APPN_UPD_TIMESTAMP);
        }
    }

    /**
    Perform an Application WorkFlow Action<BR>
    @param app_id application id
    @param process_id process id of the action belongs to
    @param status_id status id of the action belongs to
    @param action_id action id of the action wants to perform
    @param fr process status from
    @param to process status to
    @param verb verb of the action
    @param content comment on the action
    @param prof loginProfile of who make this action
    @param time last update time of the application
    @param curTime current time
    @param triggerAction indicate if the action is from event trigger
    */
    public void doAppnActn(Connection con, long app_id, long process_id,
                              String fr, String to, String verb, long action_id,
                              long status_id, loginProfile prof, String content,
                              Timestamp time, Timestamp curTime, boolean triggerAction)
        throws SQLException, IOException, cwSysMessage, qdbException, cwException
    {
        doAppnActn(con, app_id, process_id, fr, to, verb, action_id, status_id, prof, content,
                   time, curTime, triggerAction, prof);
        return;

    }

    public void doAppnActn(Connection con, long app_id, long process_id,
                              String fr, String to, String verb, long action_id,
                              long status_id, loginProfile prof, String content,
                              Timestamp time, Timestamp curTime, boolean triggerAction, String app_priority)
        throws SQLException, IOException, cwSysMessage, qdbException, cwException
    {
        doAppnActn(con, app_id, process_id, fr, to, verb, action_id, status_id, prof, content,
                   time, curTime, triggerAction, prof, app_priority);
        return;

    }

    public void doAppnActn(Connection con, long app_id, long process_id,
                              String fr, String to, String verb, long action_id,
                              long status_id, loginProfile prof, String content,
                              Timestamp time, Timestamp curTime, boolean triggerAction,
                              loginProfile loginUser)
        throws SQLException, IOException, cwSysMessage, qdbException, cwException
    {
        doAppnActn(con, app_id, process_id, fr, to, verb, action_id, status_id, prof, content, time, curTime, triggerAction, loginUser, null);
    }

    public void doAppnActn(Connection con, long app_id, long process_id,
                              String fr, String to, String verb, long action_id,
                              long status_id, loginProfile prof, String content,
                              Timestamp time, Timestamp curTime, boolean triggerAction,
                              loginProfile loginUser, String app_priority)
        throws SQLException, IOException, cwSysMessage, qdbException, cwException
    {
        aeApplication app = new aeApplication();
        aeAppnActnHistory actn = new aeAppnActnHistory();
        Vector appn_history = new Vector();
        aeWorkFlow workFlow = new aeWorkFlow(dbUtils.xmlHeader);
        String curStatus;
        StringBuffer action = new StringBuffer();
        //get application details
        app.app_id = app_id;
        app.app_priority = app_priority;

        app.getWithItem(con);
        String covStatus = dbCourseEvaluation.getCovStatus(con, app.app_tkh_id);
        if(curTime == null) curTime = cwSQL.getTime(con);

        //format the action in XML
        action.append("<current process_id=\"").append(process_id).append("\" status_id=\"").append(status_id);
        action.append("\" action_id=\"").append(action_id).append("\"/>");
        //format the ActionHistory Object
        actn.aah_app_id = app_id;
        actn.aah_process_id = process_id;
        actn.aah_fr = fr;
        actn.aah_to = to;
        actn.status_id = status_id;
        actn.aah_verb = verb;
        actn.aah_action_id = action_id;
        actn.aah_create_usr_id = prof.usr_id;
        actn.aah_create_timestamp = curTime;
        actn.aah_upd_usr_id = prof.usr_id;
        actn.aah_upd_timestamp = curTime;
        curStatus = app.app_process_xml;
        //get action access control and get the new app_process_xml

        if( !triggerAction ) {
            AcWorkFlow acWorkFlow = new AcWorkFlow(con, app.workFlowTpl);
            if (workFlow.checkAction(action.toString(), curStatus, app.workFlowTpl) == true) {
                actn.status = workFlow.checkStatus(action.toString(), curStatus, app.workFlowTpl);
            } else {
                throw new cwSysMessage(QM_INVALID_ACTION);
            }
        } else {
            actn.status = workFlow.checkStatus(action.toString(), curStatus, app.workFlowTpl);
        }
        appn_history.addElement(actn);
        prof.cur_itm_id = app.app_itm_id;
        //save the action into database
        saveAppnActn(con, app, content, triggerAction, appn_history, time, prof, loginUser);
        //如果开启了CPD模式
        if(AccessControlWZB.hasCPDFunction()){
	        //如果是删除操作 有时完成状态的申请记录 要删除对应的CPD时数
	        if(null!=covStatus && "_Exit".equalsIgnoreCase(to)){
	        	//删除学员获得的CPD
	        	CpdLrnAwardRecordService.delForOld(con, app.app_itm_id, app.app_ent_id, app.app_id);
	        	//重新计算其他已完成的学习记录看看能否获得CPD时数
	        	CpdUtilService cpdUtilService = new CpdUtilService();
	        	cpdUtilService.calOtherAwardForOld(app.app_ent_id, app.app_itm_id, prof.usr_ent_id, con);
	        }
        }
    }

    /**
    Save an Application WorkFlow Action in database and trigger event<BR>
    */
    public void saveAppnActn(Connection con, aeApplication app,
                                  String content, boolean triggerAction,
                                  Vector appn_action,
                                  Timestamp time,
                                  loginProfile prof)
        throws SQLException, IOException, cwSysMessage, qdbException, cwException
    {
        saveAppnActn(con, app, content, triggerAction, appn_action, time, prof, prof);
        return;
    }

    private void saveAppnActn(Connection con, aeApplication app,
                                  String content, boolean triggerAction,
                                  Vector appn_action,
                                  Timestamp time,
                                  loginProfile prof,
                                  loginProfile loginUser)
        throws SQLException, IOException, cwSysMessage, qdbException, cwException
    {
        String oldQueue = null;
        String newQueue = null;
        String lastest_status = null;
        aeWorkFlow workFlow = new aeWorkFlow(dbUtils.xmlHeader);
        aeAppnActnHistory actn;
        aeAppnCommHistory comm;
        //for each action history, insert into aeAppnActnHistory
        if (appn_action != null && appn_action.size() != 0) {
            for (int i=0; i<appn_action.size(); i++) {

                actn = (aeAppnActnHistory)appn_action.elementAt(i);
                if (app.app_id == actn.aah_app_id) {
                    //access control on each action

                    if(!triggerAction) {
                        AcWorkFlow acWorkFlow = new AcWorkFlow(con, app.workFlowTpl);
                        if(AccessControlWZB.isRoleTcInd(prof.current_role)){
	                        if (!acWorkFlow.checkPrivilege(prof.usr_ent_id, prof.common_role_id,
	                                                       actn.aah_process_id, actn.status_id,
	                                                       actn.aah_action_id, app, triggerAction)) {
	                            throw new cwSysMessage(QM_INVALID_ACTION);
	                        }
                        }
                    }

                    actn.aah_id = actn.ins(con);
                    lastest_status = actn.aah_to;
                }

            }
            oldQueue = app.app_status;
            //insert the comment for action history
            //cannot support multiple action
            actn = (aeAppnActnHistory)appn_action.elementAt(appn_action.size()-1);
            if (content != null && content.length() != 0) {

                comm = new aeAppnCommHistory();
                comm.ach_app_id = app.app_id;
                comm.ach_aah_id = actn.aah_id;
                comm.ach_content = content;
                comm.ach_create_timestamp = actn.aah_create_timestamp;
                comm.ach_create_usr_id = actn.aah_create_usr_id;
                comm.ach_upd_timestamp = actn.aah_upd_timestamp;
                comm.ach_upd_usr_id = actn.aah_upd_usr_id;

                comm.ins(con);

            }
            if (actn.aah_process_id == 0) {
                //??
                app.app_status = actn.aah_to;
            } else {
                app.app_process_xml = actn.status;

                newQueue = workFlow.checkQueue(actn.status, app.workFlowTpl);

                if (newQueue != null && ! newQueue.equals("")) {
                    app.app_status = newQueue;
                }
            }
            if (triggerAction || time == null || app.checkUpdTimestamp(time)) {
                app.app_upd_usr_id = prof.usr_id;
                app.app_upd_timestamp = time;
                app.app_process_status = lastest_status;
                if(triggerAction) {
                    app.updNoTime(con);
                }
                else {
                    app.upd(con);
                }
                if(app.app_status.equalsIgnoreCase("Admitted") && !lastest_status.equalsIgnoreCase("_Exit")){
	                aeItem item = new aeItem();
	    			item.itm_id = app.app_itm_id;
	    			item.getItem(con);
	    			//如果是项目式培训会自动报名其以下课程
//	                if(item.itm_integrated_ind){
//	    			    Vector vec = IntegratedLrn.getCourse( con,  item.itm_id) ;
//	    			    if(vec != null && vec.size() > 0){
//	    			        for(int i=0; i<vec.size(); i++) {
//	    			            IntegratedLrn itgLrn = (IntegratedLrn)vec.elementAt(i);
//	    			            try{
//	    			                if(itgLrn != null && itgLrn.itm_apply_ind)
//	    			                    app.insApp( con,  prof,  app.app_ent_id, itgLrn.itm_id,  true, null);
//	    			            }catch(Exception e){
//	    			                e.printStackTrace();
//	    			            }
//	    			        }
//	    			    }
//	    			}
                }
            } else {
                throw new cwSysMessage("AEQM02");
            }
        }
        //event trigger
        for (int i=0; i<appn_action.size(); i++) {


            actn = (aeAppnActnHistory)appn_action.elementAt(i);
            if (app.app_id == actn.aah_app_id) {

                aeWorkFlowEvent wfEvent = new aeWorkFlowEvent(con, app.workFlowTpl, app.workFlowTplId);
                //transfer the send mail indicator
                wfEvent.send_mail_ind = send_mail_ind;
                //wfEvent.auto_enroll_ind = auto_enroll_ind;
                wfEvent.eventTrigger(con, app, actn.aah_id, prof, loginUser,
                                     actn.aah_process_id,
                                     actn.aah_fr, actn.aah_to,
                                     actn.aah_action_id,
                                     content,
                                     triggerAction);
            }


        }
    }

    /**
    Initial application's app_process_xml, app_status and app_process_status
    */
    private aeApplication initAppnProcess(Connection con, long itm_id)
        throws SQLException, qdbException, IOException, cwSysMessage, cwException {
            return initAppnProcess(con, itm_id, 0, 0, null);
        }


    private aeApplication initAppnProcess(Connection con, long itm_id, long status_id, long action_id, String to)
        throws SQLException, qdbException, IOException, cwSysMessage, cwException  {
        aeItem item = new aeItem();
        aeTemplate tpl = new aeTemplate();
        aeWorkFlow workFlow = new aeWorkFlow(dbUtils.xmlHeader);
        aeApplication app = new aeApplication();
        String initStatus;

        item.itm_id = itm_id;
        item.getItem(con);
        tpl.tpl_id = item.getTemplateId(con, aeTemplate.WORKFLOW);
        tpl.get(con);

        if (to != null && (action_id == 0 || status_id == 0)){
            // hardcode mapping of the enrollment status in xml to the corresponding action id in workflow xml
            // such that it is language independent (just a temporary hardcode)
            // 2004-11-11 kawai
            status_id = 1;
            if (to.equals("Pending Approval")) {
                action_id = 1;
            } else if (to.equals("Waitlisted")) {
                action_id = 6;
            } else if (to.equals("Enrolled")) {
                action_id = 7;
            } else if (to.equals("Not Approved")) {
                action_id = 2;
            } else if (to.equals("Cancelled")) {
                action_id = 3;
            } else {
                throw new cwException("Unknown Enrollment Status:" + to);
            }
        }
//        app.app_process_xml = workFlow.initStatus(tpl.tpl_xml);
        app.app_process_xml = workFlow.initStatus(tpl.tpl_xml, status_id, action_id);

        app.app_status = workFlow.checkQueue(app.app_process_xml, tpl.tpl_xml);
        //System.out.println("status: " + app.app_status);

        app.app_process_status = workFlow.initProcessStatus(app.app_process_xml);
        //System.out.println("process_status: " + app.app_process_status);
        app.workFlowTpl = tpl.tpl_xml;
        app.workFlowTplId = tpl.tpl_id;
        return app;
    }

    private aeApplication checkItemCapacityStatus(Connection con, long itm_id, long ent_id)
        throws SQLException, cwSysMessage, qdbException, IOException, cwException
    {
        if (aeApplication.hasApplied(con, itm_id, ent_id)) {
            throw new cwSysMessage("AEQM01");
        }

        aeApplication app = new aeApplication();
        aeWorkFlow workFlow = new aeWorkFlow(dbUtils.xmlHeader);
        aeItem item = new aeItem();
        aeTemplate tpl = new aeTemplate();
        String initStatus;
        String nextStatus;
        StringBuffer action = new StringBuffer();
        String queueType;
        long tpl_id;
        int count;
        Vector args = new Vector();

        item.itm_id = itm_id;
        item.getItem(con);

        tpl_id = item.getTemplateId(con, aeTemplate.WORKFLOW);
        tpl.tpl_id = tpl_id;
        tpl.get(con);

        initStatus = workFlow.initStatus(tpl.tpl_xml);

        if (item.itm_capacity > 0) {
            count = aeApplication.countQueue(con, itm_id, aeApplication.ADMITTED);

            if (item.itm_capacity > count) {
                args = workFlow.getAction("<applyeasy>" + tpl.tpl_xml + "</applyeasy>", "capacity", true);
//                action = "<current process_id=\"1\" status_id=\"1\" action_id=\"1\"/>";
            } else {
//                action = "<current process_id=\"1\" status_id=\"1\" action_id=\"2\"/>";
                args = workFlow.getAction("<applyeasy>" + tpl.tpl_xml + "</applyeasy>", "capacity", false);
            }
        } else {
//            action = "<current process_id=\"1\" status_id=\"1\" action_id=\"1\"/>";
            args = workFlow.getAction("<applyeasy>" + tpl.tpl_xml + "</applyeasy>", "capacity", true);
        }

        if (args == null || args.size() != 6) {
            throw new cwException("workFlow error");
        }

        action.append("<current process_id=\"").append(args.elementAt(0)).append("\" status_id=\"").append(args.elementAt(1)).append("\" action_id=\"").append(args.elementAt(2)).append("\"/>");

        if (workFlow.checkAction(action.toString(), initStatus, tpl.tpl_xml) == true) {
            app.app_process_xml = nextStatus = workFlow.checkStatus(action.toString(), initStatus, tpl.tpl_xml);
        } else {
            //throw new qdbException("This action is invalid.");
            throw new cwSysMessage(QM_INVALID_ACTION);
        }

        app.app_status = workFlow.checkQueue(nextStatus, tpl.tpl_xml);
        app.actn_process_id = Long.parseLong((String)args.elementAt(0));
        app.actn_action_id = Long.parseLong((String)args.elementAt(2));
        app.actn_fr = (String)args.elementAt(3);
        app.actn_to = (String)args.elementAt(4);
        app.actn_verb = (String)args.elementAt(5);

        return app;
    }

    public String getApplicationForm(Connection con, HttpSession sess, long itm_id,
                                     long ent_id, loginProfile prof, qdbEnv env,
                                     long tnd_id, String itm_tvw_id, String app_tvw_id)
        throws qdbException, IOException, SQLException, cwSysMessage, cwException
    {
        //aeApplication app = checkItemCapacityStatus(con, itm_id, ent_id);
        if (aeApplication.hasApplied(con, itm_id, ent_id)) {
            throw new cwSysMessage("AEQM01");
        }
        aeItem item = new aeItem();
        aeTemplate tpl = new aeTemplate();
        aeTreeNode treeNode = new aeTreeNode();
        dbRegUser user = new dbRegUser();
        long tpl_id;
        StringBuffer result = new StringBuffer();
        StringBuffer detailXML = new StringBuffer();
        Long sess_itm_id;
        Long sess_ent_id;
        String sess_app_xml;
        Timestamp cur_time = dbUtils.getTime(con);

        item.itm_id = itm_id;
        item.getItem(con);

        if(!item.itm_apply_ind) {
            throw new cwSysMessage(QM_APPN_NOT_READY);
        }

        if (item.itm_appn_start_datetime != null
            && cur_time.before(item.itm_appn_start_datetime)) {
            throw new cwSysMessage("AEQM03");
        }

        if (item.itm_appn_end_datetime != null
            && cur_time.after(item.itm_appn_end_datetime)) {
            throw new cwSysMessage("AEQM04");
        }
        int enrol_cnt = aeApplication.countItemAppn(con, item.itm_id, false);
        if(item.itm_not_allow_waitlist_ind && (item.itm_capacity <= enrol_cnt)) {
        	throw new cwSysMessage("AEQM11");
        }

        //user.usr_id = prof.usr_id;
        //user.get(con);
        user.usr_ent_id = ent_id;
        //user.usr_id = user.getUserId(con);
        user.get(con);

        tpl_id = item.getTemplateId(con, aeTemplate.APPNFORM);
        tpl.tpl_id = tpl_id;
        tpl.get(con);
        DbTemplateView dbTplVi = new DbTemplateView();
        int cnt = 0;
        do {
            if(cnt > 1) {
                break;
            }
            if(cnt > 0) {
                app_tvw_id = "DETAIL_VIEW";
            }
            dbTplVi.tvw_tpl_id = tpl.tpl_id;
            dbTplVi.tvw_id = app_tvw_id;
            dbTplVi.get(con);
            cnt ++;
        } while(dbTplVi.tvw_xml == null);

        treeNode.tnd_id = tnd_id;
        result.append(treeNode.getNavigatorAsXML(con)).append(dbUtils.NEWL);
        result.append(item.contentAsXML(con, cur_time));
        result.append(user.getUserXML(con, prof));
        //item capacity and number of admitted application
        long count = aeApplication.countQueue(con, itm_id, aeApplication.ADMITTED);
        result.append("<item_capacity full=\"").append(item.itm_capacity).append("\"");
        result.append(" admitted=\"").append(count).append("\"/>");

        sess_itm_id = (Long)sess.getAttribute(QM_APPN_ITM_ID);
        sess_ent_id = (Long)sess.getAttribute(QM_APPN_ENT_ID);
        sess_app_xml = (String)sess.getAttribute(QM_APPN_XML);

        if (sess_itm_id != null && itm_id == sess_itm_id.longValue() &&
            sess_ent_id != null && ent_id == sess_ent_id.longValue() && sess_app_xml != null) {
            detailXML.append("<applyeasy>")
                     .append(dbTplVi.tvw_xml)
                     .append(tpl.tpl_xml)
                     .append(sess_app_xml)
                     .append("</applyeasy>");
            result.append(aeUtils.transformXML(detailXML.toString(), env.INI_XSL_VALTPL, env, null)).append(dbUtils.NEWL);
        } else {
            detailXML.append("<applyeasy>")
                     .append(dbTplVi.tvw_xml)
                     .append(tpl.tpl_xml)
                     .append("</applyeasy>");
            result.append(aeUtils.transformXML(detailXML.toString(), env.INI_XSL_VALTPL, env, null)).append(dbUtils.NEWL);
            //result.append("<queue result=\"").append(app.app_status).append("\"/>").append(dbUtils.NEWL);
        }
        //if template view id is given, get the item's item template
        if(itm_tvw_id != null && itm_tvw_id.length() > 0) {
            aeApplication app = new aeApplication();
            app.app_itm_id = itm_id;
            result.append("<item_template>");
            result.append(app.getItemApplyView(con, itm_tvw_id, env));
            result.append("</item_template>");
        }
        return result.toString();
    }

    public String confirmApplication(Connection con, HttpSession sess, long itm_id, long ent_id, String app_xml, loginProfile prof, qdbEnv env, long tnd_id, String itm_tvw_id, String app_tvw_id)
        throws qdbException, IOException, SQLException, cwSysMessage, cwException
    {
        //aeApplication app = checkItemCapacityStatus(con, itm_id, ent_id);
        if (aeApplication.hasApplied(con, itm_id, ent_id)) {
            throw new cwSysMessage("AEQM01");
        }
        aeItem item = new aeItem();
        aeTemplate tpl = new aeTemplate();
        aeTreeNode treeNode = new aeTreeNode();
        dbRegUser user = new dbRegUser();
        long tpl_id;
        StringBuffer result = new StringBuffer();
        StringBuffer detailXML = new StringBuffer();
        Long sess_itm_id;
        Long sess_ent_id;

        item.itm_id = itm_id;
        item.getItem(con);

        //user.usr_id = prof.usr_id;
        //user.get(con);
        user.usr_ent_id = ent_id;
        //user.usr_id = user.getUserId(con);
        user.get(con);

        tpl_id = item.getTemplateId(con, aeTemplate.APPNFORM);
        tpl.tpl_id = tpl_id;
        tpl.get(con);
        DbTemplateView dbTplVi = new DbTemplateView();
        int cnt = 0;
        do {
            if(cnt > 1) {
                break;
            }
            if(cnt > 0) {
                app_tvw_id = "DETAIL_VIEW";
            }
            dbTplVi.tvw_tpl_id = tpl.tpl_id;
            dbTplVi.tvw_id = app_tvw_id;
            dbTplVi.get(con);
            cnt ++;
        } while(dbTplVi.tvw_xml == null);

        treeNode.tnd_id = tnd_id;
        result.append(treeNode.getNavigatorAsXML(con)).append(dbUtils.NEWL);
        result.append(item.contentAsXML(con, null));
        result.append(user.getUserXML(con, prof));

        //detailXML.append("<applyeasy>").append(tpl.tpl_xml).append(app_xml).append("</applyeasy>");
        detailXML.append("<applyeasy>")
                 .append(dbTplVi.tvw_xml)
                 .append(tpl.tpl_xml)
                 .append(app_xml)
                 .append("</applyeasy>");
        result.append(aeUtils.transformXML(detailXML.toString(), env.INI_XSL_VALTPL, env, null)).append(dbUtils.NEWL);

        sess.setAttribute(QM_APPN_ITM_ID, new Long(itm_id));
        sess.setAttribute(QM_APPN_ENT_ID, new Long(ent_id));
        sess.setAttribute(QM_APPN_XML, app_xml);
        //if template view id is given, get the item's item template
        if(itm_tvw_id != null && itm_tvw_id.length() > 0) {
            aeApplication app = new aeApplication();
            app.app_itm_id = itm_id;
            result.append("<item_template>");
            result.append(app.getItemApplyView(con, itm_tvw_id, env));
            result.append("</item_template>");
        }
        return result.toString();
    }

    /**
    Insert into aeApplication<BR>
    @param app_xml filled application form
    @param ent_id application entity id
    @param itm_id item id of item wants to apply
    @param prof loginProfile of who created this application
    @param aer_app_id application id that will used to populate aeAppnEnrolRelation
    @return instance of aeApplication created
    */
    public aeApplication insApplication(Connection con, String app_xml, long ent_id,
                                        long itm_id, loginProfile prof, long aer_app_id)
        throws SQLException, qdbException, IOException, cwSysMessage, cwException {
            return insApplication(con, app_xml, ent_id, itm_id, prof, aer_app_id, 0, 0, 0, null, null, true, 0, null);
        }

    public aeApplication insApplication(Connection con, String app_xml, long ent_id,
                                    long itm_id, loginProfile prof, long aer_app_id, long process_id, long status_id, long action_id, String to, String verb, boolean checkDate, long tkh_id, String from)
        throws SQLException, qdbException, IOException, cwSysMessage, cwException {

        return insApplication(con, app_xml, ent_id, itm_id, prof, aer_app_id, process_id, status_id, action_id, to, verb, checkDate, tkh_id, null, from);
    }

    public aeApplication insApplication(Connection con, String app_xml, long ent_id,
                                        long itm_id, loginProfile prof, long aer_app_id, long process_id, long status_id, long action_id, String to, String verb, boolean checkDate, long tkh_id, String comment, String from)
        throws SQLException, qdbException, IOException, cwSysMessage, cwException {

        long app_id = aeApplication.getAppId(con, itm_id, ent_id, true);
        
        if(tkh_id == 0 && app_id > 0){

        	tkh_id = aeApplication.getTkhId(con, app_id);
        }

        aeApplication app = new aeApplication();
        if (app_id != 0 && (from == null || !from.equals(UploadEnrollment.FROM_UPLOAD))) {
            //if an admitted application exists, insert inactive aeAppnEnrolRelation records
            //aeApplication appn = new aeApplication();
            app.app_id = app_id;

            app.getWithItem(con);

            if(app.app_status != null && app.app_status.equalsIgnoreCase(aeApplication.ADMITTED)) {
                enrolCos(con, app.app_id, itm_id, ent_id, prof, tkh_id);
            }
        }
        else {
            aeItem item = new aeItem();
            Timestamp cur_time = dbUtils.getTime(con);
            item.itm_id = itm_id;
            item.getItem(con);
            if (checkDate){
                if (item.itm_appn_start_datetime != null
                    && cur_time.before(item.itm_appn_start_datetime)) {
                    throw new cwSysMessage("AEQM03");
                }
                if (item.itm_appn_end_datetime != null
                    && cur_time.after(item.itm_appn_end_datetime)) {
                    throw new cwSysMessage("AEQM04");
                }
            }
            //init the application to get app_process_xml, app_status and app_process_status
            app = initAppnProcess(con, itm_id, status_id, action_id, to);

            app.app_itm_id = itm_id;
            app.app_ent_id = ent_id;
            app.app_xml = app_xml;
            app.app_create_usr_id = prof.usr_id;
            app.app_upd_usr_id = prof.usr_id;
            app.app_tkh_id = tkh_id;
            insAppn2DB(con, app, cur_time, prof, aer_app_id, process_id, action_id, to, verb, comment,from);

        }
        return app;
    }

    /**
    Insert into aeApplication, aeAppnActnHistory and do event trigger
    */
    private void insAppn2DB(Connection con, aeApplication app,
                            Timestamp cur_time, loginProfile prof,
                            long aer_app_id)
        throws SQLException, IOException, qdbException, cwException, cwSysMessage {
            insAppn2DB(con, app, cur_time, prof, aer_app_id, 0, 0, null, null, null, null);
    }

    private void insAppn2DB(Connection con, aeApplication app,
                            Timestamp cur_time, loginProfile prof,
                            long aer_app_id, long process_id,
                            long action_id, String to, String verb,
                            String comment,
                            String from)
        throws SQLException, IOException, qdbException, cwException, cwSysMessage {
        aeAppnActnHistory actn = new aeAppnActnHistory();

        aeWorkFlow workFlow = new aeWorkFlow(dbUtils.xmlHeader);

        if(cur_time == null) {
            cur_time = cwSQL.getTime(con);
        }
        
        //报名推荐类型
        if (prof.current_role.indexOf(AccessControlWZB.ROL_STE_UID_TADM) == 0){
            app.app_nominate_type = aeApplication.NOMINATE_TYPE_TADM;
        } else {
            if(prof.isLrnRole && prof.usr_ent_id != app.app_ent_id){
                app.app_nominate_type = aeApplication.NOMINATE_TYPE_SUP;
            }
        }
        //insert into aeApplication
        app.ins(con);

        //insert an application history
        actn.aah_app_id = app.app_id;
        actn.aah_process_id = process_id;
        actn.aah_fr = null;
        if (to != null) {
            actn.aah_to = to;
        }
        else {
            actn.aah_to = "Applied";
        }
        //DENNIS: 2002-08-01, change the verb to capital letters
        /*actn.aah_verb = "applied";*/
        if (verb != null) {
            actn.aah_verb = verb;
        }
        else {
            actn.aah_verb = "Applied";
        }
        actn.aah_action_id = action_id;
        actn.aah_create_usr_id = app.app_create_usr_id;
        actn.aah_create_timestamp = cur_time;
        actn.aah_upd_usr_id = app.app_create_usr_id;
        actn.aah_upd_timestamp = cur_time;
        actn.aah_actn_type = from;
        actn.aah_id = actn.ins(con);

        //insert application comment history
        if(comment != null && comment.length() > 0) {
            aeAppnCommHistory comm = new aeAppnCommHistory();
            comm.ach_app_id = app.app_id;
            comm.ach_aah_id = actn.aah_id;
            comm.ach_content = comment;
            comm.ach_create_timestamp = cur_time;
            comm.ach_create_usr_id = app.app_create_usr_id;
            comm.ach_upd_timestamp = cur_time;
            comm.ach_upd_usr_id = app.app_create_usr_id;
            comm.ins(con);
        }

        //Perform event trigger
        Hashtable process_status = workFlow.getCurProcessStatus(app.app_process_xml);

        Enumeration key = process_status.keys();
        aeWorkFlowEvent wfEvent = new aeWorkFlowEvent(con, app.workFlowTpl, app.workFlowTplId);
		wfEvent.auto_enroll_ind = auto_enroll_ind;
        wfEvent.send_mail_ind = send_mail_ind;
        while(key.hasMoreElements()) {
            String event_process_id = (String) key.nextElement();
            String event_status_name = (String) process_status.get(event_process_id);
            wfEvent.eventTrigger(con, app, actn.aah_id, prof, prof,
                                 Long.parseLong(event_process_id),
                                 "", event_status_name, 0, null, false);

        }
        return;
    }


    public void insApplication(Connection con, HttpSession sess, long ent_id, long itm_id, String create_usr_id, long tnd_id, loginProfile prof)
        throws SQLException, qdbException, IOException, cwSysMessage, cwException
    {
        //aeApplication app = checkItemCapacityStatus(con, itm_id, ent_id);
        if (aeApplication.hasApplied(con, itm_id, ent_id)) {
            throw new cwSysMessage("AEQM01");
        }
        aeItem item = new aeItem();
        aeTemplate tpl = new aeTemplate();
        aeAppnActnHistory actn = new aeAppnActnHistory();
        aeTreeNode treeNode = new aeTreeNode();
        aeWorkFlow workFlow = new aeWorkFlow(dbUtils.xmlHeader);
        long id;
        long tpl_id;
        long app_id;
        //StringBuffer result = new StringBuffer();
        Timestamp cur_time = dbUtils.getTime(con);
        Long sess_itm_id;
        Long sess_ent_id;
        String sess_app_xml;
        int openItemId = 0;
        dbCourse cos = new dbCourse();
        item.itm_id = itm_id;
        item.getItem(con);
        /*
        if(!item.itm_apply_ind) {
            throw new cwSysMessage(QM_APPN_NOT_READY);
        }
        */
        if (item.itm_appn_start_datetime != null
            && cur_time.before(item.itm_appn_start_datetime)) {
            throw new cwSysMessage("AEQM03");
        }
        if (item.itm_appn_end_datetime != null
            && cur_time.after(item.itm_appn_end_datetime)) {
            throw new cwSysMessage("AEQM04");
        }
        tpl_id = item.getTemplateId(con, aeTemplate.APPNFORM);
        tpl.tpl_id = tpl_id;
        tpl.get(con);
        sess_itm_id = (Long)sess.getAttribute(QM_APPN_ITM_ID);
        sess_ent_id = (Long)sess.getAttribute(QM_APPN_ENT_ID);
        sess_app_xml = (String)sess.getAttribute(QM_APPN_XML);
        aeApplication app = initAppnProcess(con, itm_id);
        if (sess_itm_id != null && itm_id == sess_itm_id.longValue() &&
            sess_ent_id != null && ent_id == sess_ent_id.longValue() && sess_app_xml != null) {
            app.app_ent_id = ent_id;
            app.app_itm_id = itm_id;
            //app.app_process_status = app.actn_to;
            app.app_xml = sess_app_xml;
            app.app_create_usr_id = create_usr_id;
            app.app_upd_usr_id = create_usr_id;
            insAppn2DB(con, app, cur_time, prof, app.app_id);
            // remark for skippung payment event - dennis
//            if (item.itm_fee > 0) {
//                openItemId = aeAdapter.insOpenItem(con, app.app_ent_id, app.app_id, app.app_create_usr_id, OPENITEM_INVOICE, item.itm_fee, item.itm_fee_ccy, app.app_create_timestamp, item.itm_code, item.itm_title, APPNOPENITEM_APPLYEASY, new Long(app.app_id).toString(), APPNOPENITEM_APPLYEASY);
//            }
        } else {
            throw new cwException("com.cw.wizbank.ae.aeQueueManager.insApplication: Session is invalid");
        }
        /*
        if (tnd_id != 0) {
            treeNode.tnd_id = tnd_id;
            result.append(treeNode.getNavigatorAsXML(con)).append(dbUtils.NEWL);
        }
        result.append(item.contentAsXML(con));
        result.append("<process_status=\"").append(app.app_process_status).append("\"/>").append(dbUtils.NEWL);
        result.append("<queue result=\"").append(app.app_status).append("\"/>").append(dbUtils.NEWL);
        if (item.itm_fee > 0)
            result.append("<openitem id=\"").append(openItemId).append("\"/>");
        */
        sess.removeAttribute(QM_APPN_ITM_ID);
        sess.removeAttribute(QM_APPN_ENT_ID);
        sess.removeAttribute(QM_APPN_XML);
        return;
        //return result.toString();
    }

    /*
    public String insApplication(Connection con, HttpSession sess, long ent_id, long itm_id, String create_usr_id, long tnd_id, loginProfile prof)
        throws SQLException, qdbException, IOException, cwSysMessage, cwException
    {
        //aeApplication app = checkItemCapacityStatus(con, itm_id, ent_id);
        if (aeApplication.hasApplied(con, itm_id, ent_id)) {
            throw new cwSysMessage("AEQM01");
        }
        aeItem item = new aeItem();
        aeTemplate tpl = new aeTemplate();
        aeAppnActnHistory actn = new aeAppnActnHistory();
        aeTreeNode treeNode = new aeTreeNode();
        aeWorkFlow workFlow = new aeWorkFlow(dbUtils.xmlHeader);
        long id;
        long tpl_id;
        long app_id;
        StringBuffer result = new StringBuffer();
        Timestamp cur_time = dbUtils.getTime(con);
        Long sess_itm_id;
        Long sess_ent_id;
        String sess_app_xml;
        int openItemId = 0;
        dbCourse cos = new dbCourse();

        item.itm_id = itm_id;
        item.getItem(con);

        if(!item.itm_apply_ind) {
            throw new cwSysMessage("GEN005", "NOT ready to apply");
        }

        if (item.itm_appn_start_datetime != null
            && cur_time.before(item.itm_appn_start_datetime)) {
            throw new cwSysMessage("AEQM03");
        }

        if (item.itm_appn_end_datetime != null
            && cur_time.after(item.itm_appn_end_datetime)) {
            throw new cwSysMessage("AEQM04");
        }

        tpl_id = item.getTemplateId(con, aeTemplate.APPNFORM);
        tpl.tpl_id = tpl_id;
        tpl.get(con);

        sess_itm_id = (Long)sess.getAttribute(QM_APPN_ITM_ID);
        sess_ent_id = (Long)sess.getAttribute(QM_APPN_ENT_ID);
        sess_app_xml = (String)sess.getAttribute(QM_APPN_XML);
        aeApplication app = initAppnProcess(con, itm_id);
        if (sess_itm_id != null && itm_id == sess_itm_id.longValue() &&
            sess_ent_id != null && ent_id == sess_ent_id.longValue() && sess_app_xml != null) {
            app.app_ent_id = ent_id;
            app.app_itm_id = itm_id;
            //app.app_process_status = app.actn_to;
            app.app_xml = sess_app_xml;
            app.app_create_usr_id = create_usr_id;
            app.app_upd_usr_id = create_usr_id;

            app.ins(con);
            app_id  = app.app_id;

            //insert an application history
            actn.aah_app_id = app_id;
            actn.aah_process_id = 0;
            actn.aah_fr = null;
            actn.aah_to = "Applied";
            actn.aah_verb = "applied";
            actn.aah_action_id = 0;
            actn.aah_create_usr_id = create_usr_id;
            actn.aah_create_timestamp = cur_time;
            actn.aah_upd_usr_id = create_usr_id;
            actn.aah_upd_timestamp = cur_time;
            actn.ins(con);

            Hashtable process_status = workFlow.getCurProcessStatus(app.app_process_xml);
            Enumeration key = process_status.keys();
            aeWorkFlowEvent wfEvent = new aeWorkFlowEvent(app.workFlowTpl);
            while(key.hasMoreElements()) {
                String process_id = (String) key.nextElement();
                String status_name = (String) process_status.get(process_id);
                wfEvent.eventTrigger(con, sess, app, prof,
                                     Long.parseLong(process_id),
                                     "", status_name);
            }

            if (item.itm_fee > 0) {
                openItemId = aeAdapter.insOpenItem(con, ent_id, app_id, create_usr_id, OPENITEM_INVOICE, item.itm_fee, item.itm_fee_ccy, app.app_create_timestamp, item.itm_code, item.itm_title, APPNOPENITEM_APPLYEASY, new Long(app_id).toString(), APPNOPENITEM_APPLYEASY);
            }

            try {
                if (item.itm_qdb_ind && app.app_status.equals(aeApplication.ADMITTED)) {
                //if (item.itm_type.equals(aeItem.ITM_TYPE_WIZBCOURSE) && app.app_status.equals(aeApplication.ADMITTED)) {
                    cos.cos_res_id = getResId(con, app.app_itm_id, item.itm_run_ind);
                    cos.enroll(con, app.app_ent_id, null);
                }
            } catch (qdbErrMessage e) {
                throw new cwException("com.cw.wizbank.ae.aeQueueManager.insApplication: Error, " + app.app_ent_id + " can't enrol to the course.");
            }

        } else {
            throw new cwException("com.cw.wizbank.ae.aeQueueManager.insApplication: Session is invalid");
        }
        if (tnd_id != 0) {
            treeNode.tnd_id = tnd_id;
            result.append(treeNode.getNavigatorAsXML(con)).append(dbUtils.NEWL);
        }

        result.append(item.contentAsXML(con));
        result.append("<process_status=\"").append(app.app_process_status).append("\"/>").append(dbUtils.NEWL);
        result.append("<queue result=\"").append(app.app_status).append("\"/>").append(dbUtils.NEWL);
        if (item.itm_fee > 0)
            result.append("<openitem id=\"").append(openItemId).append("\"/>");

        sess.removeAttribute(QM_APPN_ITM_ID);
        sess.removeAttribute(QM_APPN_ENT_ID);
        sess.removeAttribute(QM_APPN_XML);
        return result.toString();
    }
    */

    public void cancelAppn(HttpSession sess)
    {
        sess.removeAttribute(QM_APPN_ITM_ID);
        sess.removeAttribute(QM_APPN_ENT_ID);
        sess.removeAttribute(QM_APPN_XML);
    }

    public String viewApplication(Connection con, long app_id, qdbEnv env, String itm_tvw_id, String app_tvw_id)
        throws SQLException, qdbException, cwException, cwSysMessage
    {
        aeApplication app = new aeApplication();
        aeItem item = new aeItem();
        aeTemplate tpl = new aeTemplate();
        long tpl_id;
        StringBuffer result = new StringBuffer();
        StringBuffer detailXML = new StringBuffer();

        app.app_id = app_id;
        app.getWithItem(con);
        item.itm_id = app.app_itm_id;

        tpl_id = item.getTemplateId(con, aeTemplate.APPNFORM);
        tpl.tpl_id = tpl_id;
        tpl.get(con);
        DbTemplateView dbTplVi = new DbTemplateView();
        int cnt = 0;
        do {
            if(cnt > 1) {
                break;
            }
            if(cnt > 0) {
                app_tvw_id = "DETAIL_VIEW";
            }
            dbTplVi.tvw_tpl_id = tpl.tpl_id;
            dbTplVi.tvw_id = app_tvw_id;
            dbTplVi.get(con);
            cnt ++;
        } while(dbTplVi.tvw_xml == null);

//        result.append(app.itemAsXML(con));
        result.append(app.contentAsXML(con, null, true, false, false));
        detailXML.append("<applyeasy>")
                 .append(dbTplVi.tvw_xml)
                 .append(tpl.tpl_xml)
                 .append(app.app_xml)
                 .append("</applyeasy>");
        result.append(aeUtils.transformXML(detailXML.toString(), env.INI_XSL_VALTPL, env, null)).append(dbUtils.NEWL);
        //if template view id is given, get the item's item template
        if(itm_tvw_id != null && itm_tvw_id.length() > 0) {
            result.append("<item_template>");
            result.append(app.getItemApplyView(con, itm_tvw_id, env));
            result.append("</item_template>");
        }
        return result.toString();
    }

    public void cancelHistorySession(HttpSession sess)
    {
        sess.removeAttribute(APPN_HISTORY_ID);
        sess.removeAttribute(APPN_HISTORY_ACTION);
        sess.removeAttribute(APPN_HISTORY_COMMENT);

        sess.removeAttribute(QM_ID);
        sess.removeAttribute(QM_CMD);
        sess.removeAttribute(QM_TYPE);
        sess.removeAttribute(QM_STAT);
        sess.removeAttribute(QM_CONTENT);
        sess.removeAttribute(QM_SORT_BY);
        sess.removeAttribute(QM_ORDER_BY);
        sess.removeAttribute(QM_HISTORY);
    }

    public void queueAction(Connection con, HttpSession sess, String usr_id, long app_id, String to_queue)
        throws SQLException, cwException, qdbException
    {
        Timestamp cur_timestamp = dbUtils.getTime(con);
        aeAppnActnHistory actn = new aeAppnActnHistory();
        aeApplication app = new aeApplication();
        Vector appn_history = new Vector();

        if (! aeApplication.isValidQueue(to_queue)) {
            throw new cwException("com.cw.wizbank.ae.aeQueueManager.queueAction: Error: invalid queue");
        }

        app.app_id = app_id;
        app.get(con);

        actn.aah_app_id = app_id;
        actn.aah_fr = app.app_status;
        actn.aah_to = to_queue;
        actn.aah_process_id = 0;
        actn.aah_create_usr_id = usr_id;
        actn.aah_create_timestamp = cur_timestamp;
        actn.aah_upd_usr_id = usr_id;
        actn.aah_upd_timestamp = cur_timestamp;
        actn.status = null;
        appn_history.addElement(actn);

        sess.setAttribute(APPN_HISTORY_ID, new Long(actn.aah_app_id));
        sess.setAttribute(APPN_HISTORY_ACTION, appn_history);
        sess.setAttribute(APPN_UPD_TIMESTAMP, app.app_upd_timestamp);
    }

    public String getEnrolledItems(Connection con, long ent_id)
        throws SQLException, qdbException, cwException
    {
        StringBuffer result = new StringBuffer();
        StringBuffer SQL = new StringBuffer();
        Hashtable items = new Hashtable();
        Vector programs = new Vector();
        Vector pgm_itm_lst = new Vector();
        Vector cos_vec = new Vector();
        Vector item_vec = new Vector();
        aeItem item;
        PreparedStatement stmt;
        ResultSet rs;
        Long pgm;
        Long temp;
        long id;

		SQL.append(OuterJoinSqlStatements.aeQueueManagerGetEnrolledItems());
//		  SQL.append("SELECT itm_id, itm_code, itm_type, itm_title, itm_eff_start_datetime, itm_eff_end_datetime, app_status, cos_res_id FROM aeApplication, aeItem, Course WHERE app_ent_id = ? AND itm_id = app_itm_id AND itm_status = ? AND cos_itm_id ");
//		  SQL.append(cwSQL.get_right_join(con)).append(" app_itm_id ORDER BY itm_id");
//        System.out.println(SQL.toString());
        stmt = con.prepareStatement(SQL.toString());
        stmt.setLong(1, ent_id);
        stmt.setString(2, aeItem.ITM_STATUS_ON);
        rs = stmt.executeQuery();

        while (rs.next()) {
            item = new aeItem();
            item.itm_id = rs.getLong("itm_id");
            item.itm_code = rs.getString("itm_code");
            item.itm_type = rs.getString("itm_type");
            item.itm_title = rs.getString("itm_title");
            item.itm_eff_start_datetime = rs.getTimestamp("itm_eff_start_datetime");
            item.itm_eff_end_datetime = rs.getTimestamp("itm_eff_end_datetime");
            item.app_status = rs.getString("app_status");
            item.cos_res_id = rs.getLong("cos_res_id");

            if (item.cos_res_id != 0) {
                cos_vec.addElement(new Long(item.cos_res_id));
            }

            items.put(new Long(item.itm_id), item);
            item_vec.add(new Long(item.itm_id));
        }

        result.append("<cur_time>").append(dbUtils.getTime(con)).append("</cur_time>").append(dbUtils.NEWL);
        result.append("<program_list>").append(dbUtils.NEWL);

        for (int i=0; i<programs.size(); i++) {
            item = (aeItem)programs.elementAt(i);
            result.append("<program id=\"").append(item.itm_id).append("\" code=\"").append(dbUtils.esc4XML(item.itm_code));
            result.append("\" title=\"").append(dbUtils.esc4XML(item.itm_title)).append("\" status=\"").append(item.app_status);
            result.append("\" type=\"").append(item.itm_type).append("\" course_id=\"").append(item.cos_res_id).append("\" eff_start_datetime=\"").append(item.itm_eff_start_datetime).append("\" eff_end_datetime=\"").append(item.itm_eff_end_datetime).append("\">").append(dbUtils.NEWL);

            stmt = con.prepareStatement("SELECT pdt_itm_id, pdt_core_ind FROM aeProgramDetails WHERE pdt_pgm_itm_id = ? ORDER BY pdt_itm_id");
            stmt.setLong(1, item.itm_id);
            rs = stmt.executeQuery();

            while (rs.next()) {
                id = rs.getLong("pdt_itm_id");

                if (items.containsKey(new Long(id))) {
                    pgm_itm_lst.addElement(new Long(id));

                    item = (aeItem)items.get(new Long(id));
                    result.append("<item id=\"").append(item.itm_id).append("\" code=\"").append(dbUtils.esc4XML(item.itm_code));
                    result.append("\" title=\"").append(dbUtils.esc4XML(item.itm_title)).append("\" status=\"").append(item.app_status);
                    result.append("\" type=\"").append(item.itm_type).append("\" course_id=\"").append(item.cos_res_id);
                    result.append("\" core_ind=\"").append(rs.getInt("pdt_core_ind")).append("\" eff_start_datetime=\"").append(item.itm_eff_start_datetime).append("\" eff_end_datetime=\"").append(item.itm_eff_end_datetime).append("\"/>").append(dbUtils.NEWL);
                }
            }

            result.append("</program>").append(dbUtils.NEWL);
        }

        result.append("</program_list>").append(dbUtils.NEWL);
        result.append("<item_list>").append(dbUtils.NEWL);

        for (int i=0; i<item_vec.size(); i++) {
            temp = (Long)item_vec.elementAt(i);

            if (! pgm_itm_lst.contains(temp)) {
                item = (aeItem)items.get(temp);
                result.append("<item id=\"").append(item.itm_id).append("\" code=\"").append(dbUtils.esc4XML(item.itm_code));
                result.append("\" title=\"").append(dbUtils.esc4XML(item.itm_title)).append("\" status=\"").append(item.app_status);
                result.append("\" type=\"").append(item.itm_type).append("\" course_id=\"").append(item.cos_res_id).append("\" eff_start_datetime=\"").append(item.itm_eff_start_datetime).append("\" eff_end_datetime=\"").append(item.itm_eff_end_datetime).append("\"/>").append(dbUtils.NEWL);
            }
        }

        result.append("</item_list>").append(dbUtils.NEWL);
        result.append(dbCourseEvaluation.getProgressStatus(con, ent_id, cos_vec));

        stmt.close();
        return result.toString();
    }

    public String getEnrolledItemsFromPgm(Connection con, long ent_id, long itm_id, qdbEnv env)
        throws SQLException, qdbException, cwException
    {
        StringBuffer result = new StringBuffer();
        StringBuffer SQL = new StringBuffer();
        StringBuffer itm_lst = new StringBuffer();
        StringBuffer detailXML = new StringBuffer();
        Hashtable items = new Hashtable();
        Vector pgm_itm_lst = new Vector();
        Vector cos_vec = new Vector();
        aeItem pgm_item = null;
        aeItem item;
        aeTemplate tpl;
        PreparedStatement stmt;
        ResultSet rs;
        long id;
        long tpl_id;

		SQL.append(OuterJoinSqlStatements.aeQueueManagerGetEnrolledItemsFromPgm());
//		SQL.append("SELECT itm_id, itm_xml, itm_type, itm_eff_start_datetime, itm_eff_end_datetime, app_status, cos_res_id FROM aeApplication, aeItem, Course WHERE app_ent_id = ? AND itm_id = app_itm_id AND itm_status = ? AND cos_itm_id ");
//		SQL.append(cwSQL.get_right_join(con)).append(" app_itm_id ORDER BY itm_id");
//        System.out.println(SQL.toString());
        stmt = con.prepareStatement(SQL.toString());
        stmt.setLong(1, ent_id);
        stmt.setString(2, aeItem.ITM_STATUS_ON);
        rs = stmt.executeQuery();

        while (rs.next()) {
            item = new aeItem();
            item.itm_id = rs.getLong("itm_id");
            //item.itm_xml = rs.getString("itm_xml");
            item.itm_xml = cwSQL.getClobValue(rs, "itm_xml");

            item.itm_type = rs.getString("itm_type");
            item.itm_eff_start_datetime = rs.getTimestamp("itm_eff_start_datetime");
            item.itm_eff_end_datetime = rs.getTimestamp("itm_eff_end_datetime");
            item.app_status = rs.getString("app_status");
            item.cos_res_id = rs.getLong("cos_res_id");

            items.put(new Long(item.itm_id), item);
        }

        if (pgm_item == null) {
            throw new cwException("com.cw.wizbank.ae.aeQueueManager.getEnrolledItemsFromPgm: item id " + itm_id + " is not a program or " +
                                  "you have't enrolled to this program");
        }

        tpl_id = pgm_item.getTemplateId(con, aeTemplate.ITEM);
        tpl = new aeTemplate();
        tpl.tpl_id = tpl_id;
        tpl.get(con);

        result.append("<cur_time>").append(dbUtils.getTime(con)).append("</cur_time>").append(dbUtils.NEWL);
        detailXML.append("<applyeasy>").append(tpl.tpl_xml).append(pgm_item.itm_xml).append("</applyeasy>");
        result.append("<program id=\"").append(pgm_item.itm_id).append("\" status=\"").append(pgm_item.app_status).append("\" course_id=\"").append(pgm_item.cos_res_id).append("\" eff_start_datetime=\"").append(pgm_item.itm_eff_start_datetime).append("\" eff_end_datetime=\"").append(pgm_item.itm_eff_end_datetime).append("\">").append(dbUtils.NEWL);
        result.append(aeUtils.transformXML(detailXML.toString(), env.INI_XSL_VALTPL, env, null)).append(dbUtils.NEWL);

        stmt = con.prepareStatement("SELECT pdt_itm_id, pdt_core_ind FROM aeProgramDetails WHERE pdt_pgm_itm_id = ? ORDER BY pdt_itm_id");
        stmt.setLong(1, pgm_item.itm_id);
        rs = stmt.executeQuery();

        while (rs.next()) {
            id = rs.getLong("pdt_itm_id");

            if (items.containsKey(new Long(id))) {
                pgm_itm_lst.addElement(new Long(id));
                item = (aeItem)items.get(new Long(id));

                tpl = new aeTemplate();
                tpl_id = item.getTemplateId(con, aeTemplate.ITEM);
                tpl.tpl_id = tpl_id;
                tpl.get(con);

                if (item.cos_res_id != 0) {
                    cos_vec.addElement(new Long(item.cos_res_id));
                }

                detailXML = new StringBuffer();
                detailXML.append("<applyeasy>").append(tpl.tpl_xml).append(item.itm_xml).append("</applyeasy>");

                result.append("<item id=\"").append(item.itm_id).append("\" status=\"").append(item.app_status).append("\" course_id=\"").append(item.cos_res_id);
                result.append("\" core_ind=\"").append(rs.getInt("pdt_core_ind")).append("\" eff_start_datetime=\"").append(item.itm_eff_start_datetime).append("\" eff_end_datetime=\"").append(item.itm_eff_end_datetime).append("\">").append(dbUtils.NEWL);
                result.append(aeUtils.transformXML(detailXML.toString(), env.INI_XSL_VALTPL, env, null)).append(dbUtils.NEWL);
                result.append("</item>").append(dbUtils.NEWL);
            }
        }

        result.append("</program>").append(dbUtils.NEWL);
        result.append(dbCourseEvaluation.getProgressStatus(con, ent_id, cos_vec));

        stmt.close();
        return result.toString();
    }

    private static long getResId(Connection con, long itm_id, boolean itm_run_ind)
        throws SQLException
    {
        aeItem itm = new aeItem();
        itm.itm_id = itm_id;
        itm.itm_run_ind = itm_run_ind;
        return itm.getResId(con);
    }

    protected static long getResId(Connection con, long itm_id)
        throws SQLException
    {
        aeItem itm = new aeItem();
        itm.itm_id = itm_id;
        itm.getRunInd(con);
        return itm.getClassResId(con);
    }

    public static void updAppn(Connection con, long app_id, String upd_usr_id)
        throws cwException, qdbException, IOException, SQLException, cwSysMessage
    {
        aeApplication app = new aeApplication();
        aeAppnActnHistory actn = new aeAppnActnHistory();
        aeItem item = new aeItem();
        aeTemplate tpl = new aeTemplate();
        aeWorkFlow workFlow = new aeWorkFlow(dbUtils.xmlHeader);
        dbCourse cos = new dbCourse();
        String oldQueue;
        String newQueue;
        String newStatus;
        StringBuffer action = new StringBuffer();
        Vector args = new Vector();
        Timestamp cur_timestamp;
        Timestamp upd_timestamp;

        app.app_id = app_id;
        app.getWithItem(con);
        item.itm_id = app.app_itm_id;
        upd_timestamp = app.app_upd_timestamp;

        tpl.tpl_id = item.getTemplateId(con, aeTemplate.WORKFLOW);
        tpl.get(con);

        args = workFlow.getAction("<applyeasy>" + tpl.tpl_xml + "</applyeasy>", "payment", true);

        if (args == null || args.size() != 6) {
            throw new cwException("workFlow error");
        }

        action.append("<current process_id=\"").append(args.elementAt(0)).append("\" status_id=\"").append(args.elementAt(1)).append("\" action_id=\"").append(args.elementAt(2)).append("\"/>");

        cur_timestamp = dbUtils.getTime(con);
        actn.aah_app_id = app_id;
        actn.aah_process_id = Long.parseLong((String)args.elementAt(0));
        actn.aah_fr = (String)args.elementAt(3);
        actn.aah_to = (String)args.elementAt(4);
        actn.aah_verb = (String)args.elementAt(5);
        actn.aah_action_id = Long.parseLong((String)args.elementAt(2));
        actn.aah_create_usr_id = upd_usr_id;
        actn.aah_create_timestamp = cur_timestamp;
        actn.aah_upd_usr_id = upd_usr_id;
        actn.aah_upd_timestamp = cur_timestamp;

        if (workFlow.checkAction(action.toString(), app.app_process_xml, tpl.tpl_xml) == true) {
            newStatus = workFlow.checkStatus(action.toString(), app.app_process_xml, tpl.tpl_xml);
        } else {
            //throw new cwException("com.cw.wizbank.ae.aeQueueManager.updAppn: This action is invalid.");
            throw new cwSysMessage(QM_INVALID_ACTION);
        }

        oldQueue = app.app_status;
        newQueue = workFlow.checkQueue(newStatus, tpl.tpl_xml);

        // get the lastest upd_timestamp from aeApplication
        app = new aeApplication();
        app.app_id = app_id;
        app.getWithItem(con);
        app.app_process_xml = newStatus;

        if (newQueue != null && ! newQueue.equals("")) {
            app.app_status = newQueue;
        }

        if (app.checkUpdTimestamp(upd_timestamp)) {
            app.app_upd_usr_id = upd_usr_id;
            app.app_upd_timestamp = cur_timestamp;
            app.upd(con);
            actn.ins(con);
        } else {
            throw new cwSysMessage("AEQM02");
        }
    }


    public String getNotifyStatus(Connection con, HttpServletRequest request, long root_ent_id, long itm_id, cwPagination cwPage )
        throws SQLException, cwException {

            StringBuffer xml = new StringBuffer();
            HttpSession sess = request.getSession(true);
            Hashtable data = null;
            boolean useSess = false;
            Timestamp sess_pagetime = null;
            String sess_order = null;
            String sess_sort = null;
            if (sess !=null) {
                data = (Hashtable) sess.getAttribute("JI_STATUS_LIST");
                if ( data !=null ) {
                    sess_pagetime = (Timestamp) data.get("HASH_TIMESTAMP");
                    sess_order = (String) data.get("HASH_ORDERBY");
                    sess_sort = (String) data.get("HASH_SORTBY");
                    if ( sess_pagetime.equals(cwPage.ts) )
                        useSess = true;
                } else {
                    data = new Hashtable();
                }
            }

            int start = (cwPage.curPage-1) * cwPage.pageSize + 1;
            int end  = cwPage.curPage * cwPage.pageSize;

            Vector idVecFromSess = new Vector();
            Vector idVecFromDb = new Vector();
            String sql = null;
            if( useSess ) {
                idVecFromSess = (Vector)data.get("HASH_ENT_ID_VEC");
                //change order by or sort by will goto page 1 of the serach result
                if( ( sess_order != null && !sess_order.equalsIgnoreCase(cwPage.sortCol) ) ||
                    ( sess_sort != null && !sess_sort.equalsIgnoreCase(cwPage.sortOrder) )   ) {
                        start = 1;
                        end = idVecFromSess.size();
                        cwPage.curPage = 1;
                    }
                long[] id = new long[end - start + 1];
                for(int i=start; i<=idVecFromSess.size() && i<=end; i++)
                    id[i-start] = ((Long)idVecFromSess.elementAt(i-1)).longValue();
                //rs = getNotifyStatusResultSet(con, itm_id, cwPage.sortCol, cwPage.sortOrder, id, "Admitted");
                sql = getNotifyStatusSql(cwPage.sortCol, cwPage.sortOrder, id);
            } else {
                //rs = getNotifyStatusResultSet(con, itm_id, cwPage.sortCol, cwPage.sortOrder, null, "Admitted");
                sql = getNotifyStatusSql(cwPage.sortCol, cwPage.sortOrder, null);
            }
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setLong(1, itm_id);
            stmt.setBoolean(2, true);
            stmt.setString(3, aeApplication.ADMITTED);
            ResultSet rs = stmt.executeQuery();

            StringBuffer xmlBody = new StringBuffer();
            Hashtable userGroupTable = new Hashtable();
            Vector groupTitle = new Vector();
            Long usr_ent_id;
            Vector entIdVec = new Vector();
            while( rs.next() ) {
                usr_ent_id = new Long(rs.getLong("usr_ent_id"));
                entIdVec.addElement(usr_ent_id);
            }
            rs.beforeFirst();
            userGroupTable = dbUserGroup.getUserGroupTitle(con, entIdVec);

            String requiredEntId = new String();
            xmlBody.append("<usr_list>").append(cwUtils.NEWL);
            while( rs.next() ) {
                usr_ent_id = new Long(rs.getLong("usr_ent_id"));
                if( idVecFromDb.size() < cwPage.pageSize  ) {
                    requiredEntId += usr_ent_id + "~";
                    xmlBody.append("<usr usr_id=\"").append(rs.getString("usr_id")).append("\" ")
                           .append(" usr_ent_id=\"").append(rs.getLong("usr_ent_id")).append("\" ")
                           .append(" usr_display_bil=\"").append(cwUtils.esc4XML(rs.getString("usr_display_bil"))).append("\" ")
                           .append(" usr_grade=\"").append(cwUtils.esc4XML(rs.getString("ugr_display_bil"))).append("\" ")
                           .append(" usr_tel_1=\"").append(cwUtils.esc4XML(rs.getString("usr_tel_1"))).append("\" ")
                           .append(" app_notify_status=\"").append(rs.getLong("app_notify_status")).append("\" ")
                           .append(" app_notify_senddate=\"").append(rs.getTimestamp("app_notify_datetime")).append("\">")
                           .append(cwUtils.NEWL);
                    groupTitle = (Vector)userGroupTable.get(usr_ent_id);
                    xmlBody.append("<user_group>").append(cwUtils.NEWL);
                    for(int i=0; i<groupTitle.size(); i++)
                        xmlBody.append("<group title=\"").append(cwUtils.esc4XML((String)groupTitle.elementAt(i))).append("\"/>").append(cwUtils.NEWL);
                    xmlBody.append("</user_group>").append(cwUtils.NEWL);
                    xmlBody.append("</usr>").append(cwUtils.NEWL);
                    idVecFromDb.addElement(usr_ent_id);
                } else if( idVecFromDb.indexOf(usr_ent_id) == -1 )
                    idVecFromDb.addElement(usr_ent_id);
            }
            xmlBody.append("</usr_list>").append(cwUtils.NEWL);
            if( requiredEntId.trim().length() > 0 ) {
                /* Get data from message module through http connection */
                String url_redirect = "../servlet/Dispatcher?module=message.MessageModule"
                                    + "&cmd=get_itm_msg_status_xml"
                                    + "&itm_id=" + itm_id
                                    + "&ent_ids=" + requiredEntId;
                String returnXML;
                try{
                    url_redirect = cwUtils.getRealPath(request, url_redirect);
                    returnXML = aeUtils.urlRedirect( url_redirect, request );
                }catch(Exception e) {
                    throw new cwException("Failed to get item message status, item id = " + itm_id + " : " + e);
                }
                xmlBody.append(returnXML);
            }


            cwPage.totalRec = 0;
            if( useSess ) {
                cwPage.totalRec = idVecFromSess.size();
                if( ( sess_order != null && !sess_order.equalsIgnoreCase(cwPage.sortCol) ) ||
                    ( sess_sort != null && !sess_sort.equalsIgnoreCase(cwPage.sortOrder) ))
                        data.put("HASH_ENT_ID_VEC", idVecFromDb);
            } else {
                cwPage.totalRec = idVecFromDb.size();
                cwPage.ts = cwSQL.getTime(con);
                data.put("HASH_TIMESTAMP", cwPage.ts);
                data.put("HASH_ENT_ID_VEC", idVecFromDb);
            }
            cwPage.totalPage = (int)Math.ceil( (float)cwPage.totalRec / (float) cwPage.pageSize );
            data.put("HASH_ORDERBY", cwPage.sortCol);
            data.put("HASH_SORTBY", cwPage.sortOrder);
            sess.setAttribute("JI_STATUS_LIST", data);
/*
            xml.append("<notification_status total=\"").append(count).append("\" ")
               .append(" cur_page=\"").append(cwPage.curPage).append("\" ")
               .append(" pagesize=\"").append(cwPage.pageSize).append("\" ")
               .append(" timestamp=\"").append(cwPage.ts).append("\" ")
               .append(" order_by=\"").append(cwPage.sortCol).append("\" ")
               .append(" sort_by=\"").append(cwPage.sortOrder).append("\" ")
               .append(" >").append(cwUtils.NEWL)
               .append(xmlBody)
               .append("</notification_status>").append(cwUtils.NEWL);
*/
            xml.append("<notification_status itm_id=\"").append(itm_id).append("\">").append(cwUtils.NEWL)
               .append(cwPage.asXML())
               .append(xmlBody)
               //.append("</list>").append(cwUtils.NEWL)
               .append("</notification_status>").append(cwUtils.NEWL);

            stmt.close();
            return xml.toString();

        }


    public String getNotifyStatusSql(String order_by, String sort_by, long[] entIds)
        throws SQLException {

        StringBuffer idList = new StringBuffer().append("(0");
        if( entIds != null ) {
            for(int i=0; i<entIds.length; i++)
                idList.append(",").append(entIds[i]);
            idList.append(")");
        }

        String SQL = " SELECT usr_ent_id, usr_id, usr_display_bil, app_notify_status, "
                   + " app_notify_datetime, usr_tel_1, ugr_display_bil "
                   + " FROM aeApplication , regUser , usergrade , EntityRelation "
                   + " WHERE app_ent_id = usr_ent_id "
                   + " AND ugr_ent_id = ern_ancestor_ent_id "
                   + " AND usr_ent_id = ern_child_ent_id "
                   + " AND app_itm_id = ? "
                   + " AND ern_parent_ind = ? ";
                   if( entIds != null )
                        SQL += " AND usr_ent_id IN " + idList;
                   SQL += " AND app_status = ? ";
               SQL += " ORDER BY " + order_by
                   +  "  " + sort_by;

        return SQL;
    }

    /**
    Get applications base on usr_ent_id_root and app_process_status
    @ent_id_lst a SQL list of usr_ent_id that wants to find out their application (e.g. users of an approver)
    @return Vector of aeApplication
    */
    public Vector getAppnByProcessStatus(Connection con, long owner_ent_id,
                                         String process_status,
                                         long itm_id,
                                         String app_id_lst,
                                         String ent_id_lst,
                                         String orderBy,
                                         String sortOrder)
                                         throws SQLException, IOException, cwException {
        Vector v_app = new Vector();
        Timestamp max_timestamp = Timestamp.valueOf(dbUtils.MAX_TIMESTAMP);
        String sql_null_string = cwSQL.get_null_sql(cwSQL.COL_TYPE_STRING);
        if(orderBy == null || orderBy.length() == 0) {
            orderBy = "r_itm_appn_end_datetime";
        }
        if(sortOrder == null || sortOrder.length() == 0) {
            sortOrder = "ASC";
        }
        StringBuffer SQLBuf = new StringBuffer(1024);
        process_status = getProcessStatus(con, process_status, itm_id, owner_ent_id);
        SQLBuf = new StringBuffer(SqlStatements.getApplicationList(con, app_id_lst, process_status, itm_id, ent_id_lst));
        // for sort
        if(orderBy.equalsIgnoreCase("gpm_full_path")) {
            SQLBuf.append(" Order By ste_name ").append(sortOrder);
        } else {
            SQLBuf.append(" Order By ").append(orderBy).append(" ").append(sortOrder);
        }
        if(!orderBy.equalsIgnoreCase("r_itm_appn_end_datetime")) {
            SQLBuf.append(", r_itm_appn_end_datetime asc ");
        }
        if(!orderBy.equalsIgnoreCase("r_itm_title")) {
            SQLBuf.append(", r_itm_title asc ");
        }
        if(!orderBy.equalsIgnoreCase("p_itm_title")) {
            SQLBuf.append(", p_itm_title asc ");
        }

        PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
        int index = 1;
        stmt.setTimestamp(index++, max_timestamp);
        stmt.setTimestamp(index++, max_timestamp);
        stmt.setTimestamp(index++, max_timestamp);
        stmt.setTimestamp(index++, max_timestamp);
        stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
        stmt.setBoolean(index++, true);
        stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
        stmt.setBoolean(index++, true);
        stmt.setBoolean(index++, true);
        if(app_id_lst == null) {
            stmt.setLong(index++, owner_ent_id);
            if(process_status != null && process_status.length() > 0) {
                stmt.setString(index++, process_status);
            }
            if(itm_id != 0) {
                stmt.setLong(index++, itm_id);
            }
        }
        stmt.setTimestamp(index++, max_timestamp);
        stmt.setTimestamp(index++, max_timestamp);
        stmt.setTimestamp(index++, max_timestamp);
        stmt.setTimestamp(index++, max_timestamp);
        stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
        stmt.setBoolean(index++, true);
        stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
        stmt.setBoolean(index++, true);
        stmt.setBoolean(index++, false);
        if(app_id_lst == null) {
            stmt.setLong(index++, owner_ent_id);
            if(process_status != null && process_status.length() > 0) {
                stmt.setString(index++, process_status);
            }
            if(itm_id != 0) {
                stmt.setLong(index++, itm_id);
            }
        }
        ResultSet rs = stmt.executeQuery();
        EntityFullPath entityfullpath = EntityFullPath.getInstance(con);
        while(rs.next()) {
        	long usg_id = rs.getLong("ern_usg_id");
        	if(usg_id == 0){
        		usg_id = rs.getLong("erh_usg_id");
        	}
            aeApplication app = new aeApplication();
            app.app_id = rs.getLong("app_id");
            app.app_ent_id = rs.getLong("app_ent_id");
            app.app_itm_id = rs.getLong("app_itm_id");
            app.app_status = rs.getString("app_status");
            app.app_process_status = rs.getString("app_process_status");
            app.app_create_timestamp = rs.getTimestamp("app_create_timestamp");
            app.app_create_usr_id = rs.getString("app_create_usr_id");
            app.app_upd_timestamp = rs.getTimestamp("app_upd_timestamp");
            app.app_upd_usr_id = rs.getString("app_upd_usr_id");
            app.app_ext1 = rs.getString("app_ext1");
            app.app_ext2 = rs.getLong("app_ext2");
            app.app_ext3 = rs.getString("app_ext3");
            app.app_notify_status = rs.getInt("app_notify_status");
            app.app_notify_datetime = rs.getTimestamp("app_notify_datetime");
            app.app_priority = rs.getString("app_priority");
            app.gpm_full_path = entityfullpath.getFullPath(con, usg_id);
            app.ste_name = rs.getString("ste_name");
            v_app.addElement(app);
        }
        stmt.close();
        return v_app;
    }

    public Hashtable getAppnByProcessStatus(Connection con, long owner_ent_id, String process_status, long itm_id, String app_id_lst, String ent_id_lst,
                                            String orderBy, String sortOrder, WizbiniLoader wizbini, HttpSession sess, long page_size, long page_num, String root_id)
        throws SQLException, IOException, cwException {
        Vector v_app = new Vector();
        Hashtable app_hash = new Hashtable();
        Hashtable hash = getSelectColsApp(con, wizbini, root_id, EXPORT_ENROLLMENT);
        String select_cols = (String)hash.get(SELECT_COLS);
        Hashtable export_hash = (Hashtable)hash.get(EXPORT_HASH);
        Vector extra_cols = (Vector) hash.get(EXTRA_COLS);
        has_filter = false;
        //for get enroll data
        PreparedStatement stmt = getEnrollList(con, owner_ent_id, process_status, itm_id, app_id_lst, ent_id_lst, orderBy, sortOrder, wizbini, sess, false, select_cols, extra_cols);
        ResultSet rs = stmt.executeQuery();
        
        rs.last();
        long total_num = rs.getRow();
        
        //page control
        if (page_size == 0) {
            page_size = 10;
        }
        if (page_num == 0) {
            page_num = 1;
        }
        long i = 0;
        if (page_num > 1 && page_size * (page_num - 1) >= total_num) {
            page_num = total_num / page_size;

            if (total_num % page_size > 0) {
                page_num++;
            }
        }
        rs.beforeFirst();
        
        EntityFullPath entityfullpath = EntityFullPath.getInstance(con);
        Vector usr_ent_vec = new Vector();
        
        while (rs.next()) {
            i++;
            long cur_page_end_num = page_num * page_size;
            long cur_page_start_num = (page_num - 1) * page_size;

            if ((i <=cur_page_end_num && i >= cur_page_start_num + 1) || ent_id_lst != null) {
                aeApplication app = new aeApplication();
                app.export_cols_hash = new Hashtable();
                app.app_id = rs.getLong("app_id");
                app.app_itm_id = itm_id;
                app.app_ent_id = rs.getLong("usr_ent_id");
//                app.app_process_status = rs.getString("app_process_status");
                app.app_upd_timestamp = rs.getTimestamp("app_upd_timestamp");
                app.app_priority = rs.getString("app_priority");
                app.ste_name = rs.getString("ste_name");
//                app.aal_approval_role = rs.getString("aal_approval_role");
                app.usr_ent_id = rs.getLong("usr_ent_id");
                app.usr_ste_usr_id = rs.getString("login_id");
                app.usr_display_bil = rs.getString("usr_name");
//                app.usr_tel_1 = rs.getString("usr_tel_1");
                Enumeration export_keys = export_hash.keys();
                while(export_keys.hasMoreElements()) {
                	String key = (String)export_keys.nextElement();
                	Vector value =(Vector) export_hash.get(key);
                	StringBuffer col_value = new StringBuffer();
                	if( value != null && value.size() > 0 ) {
                		for(int j=0; j<value.size(); j++) {
                			if(j !=0 ) {
                				col_value.append(", ");
                			}
                			String[] column =(String[]) value.get(j);
                			col_value.append(getColValue(rs, column));
                		}
                	}
                	app.export_cols_hash.put(key, col_value.toString());
                }
                if(extra_cols != null && extra_cols.contains(EXPORT_COLS_ROLE)) {
                	Vector roles_vec = dbRegUser.getGrantedRoles(con, app.app_ent_id);
                	app.export_cols_hash.put(EXPORT_COLS_ROLE, (String)roles_vec.elementAt(1));
                }
                if(extra_cols != null && extra_cols.contains(EXPORT_COLS_DS)) {
                	app.export_cols_hash.put(EXPORT_COLS_DS, ViewSuperviseTargetEntity.getDirectSupervisorAsXML(con, app.usr_ent_id).toString());
                }
                v_app.addElement(app);
                if (!usr_ent_vec.contains(new Long(app.app_ent_id))) {
                    usr_ent_vec.addElement(new Long(app.app_ent_id));
                }
            }
        }
        rs.close();
        stmt.close();
        if (usr_ent_vec.size() != 0) {
            // Use temp table instead of using IN (1,2,3,.....,XXXX)
            String colName = "tmp_usr_ent_id";
            String tableName = cwSQL.createSimpleTemptable(con, colName, cwSQL.COL_TYPE_LONG, 0);
            cwSQL.insertSimpleTempTable(con, tableName, usr_ent_vec, cwSQL.COL_TYPE_LONG);
            Hashtable usr_group_hash = dbEntityRelation.getGroupEntIdRelation(con, tableName, colName);
            Hashtable usr_grade_hash = dbEntityRelation.getGradeRelation(con, tableName, colName);
            cwSQL.dropTempTable(con, tableName);
            EntityFullPath full_path = EntityFullPath.getInstance(con);
            for(int j = 0; j < v_app.size(); j++) {
            	aeApplication app = (aeApplication)v_app.get(j);
            	long usg_id = -1;
            	if(usr_group_hash.get(app.app_ent_id) != null) {
            	    usg_id = (Long)usr_group_hash.get(app.app_ent_id);
            	}
            	app.usr_group_name = entityfullpath.getFullPath(con, usg_id);
            	if(app.usr_group_name == null){
                    app.usr_group_name = "";
                }
            	app.export_cols_hash.put(EXPORT_COLS_GROUP, app.usr_group_name);
            	app.usr_grade_name = (String)usr_grade_hash.get(app.app_ent_id);
            	
            	if(app.usr_grade_name == null){
            	    app.usr_grade_name = "";
            	}
            	app.export_cols_hash.put(EXPORT_COLS_GRADE, app.usr_grade_name);
            }
        }
        long[] page_var = { total_num, page_size, page_num };
        app_hash.put("v_app", v_app);
        app_hash.put("page_var", page_var);
        app_hash.put("has_filter", new Boolean(has_filter));
        return app_hash;
    }
    public static String getColValue(ResultSet rs, String[] column) throws SQLException {
    	StringBuffer col_value = new StringBuffer();
		if (column[1].equalsIgnoreCase(cwSQL.COL_TYPE_TIMESTAMP)) {
			col_value.append(rs.getTimestamp(column[0]));
		}else if (column[1].equalsIgnoreCase(cwSQL.COL_TYPE_LONG)) {
			col_value.append(rs.getLong(column[0]));
		}else if (column[1].equalsIgnoreCase(cwSQL.COL_TYPE_INTEGER)) {
			col_value.append(rs.getInt(column[0]));
		}else if (column[1].equalsIgnoreCase(cwSQL.COL_TYPE_FLOAT)) {
			col_value.append(rs.getFloat(column[0]));
		}else if (column[1].equalsIgnoreCase(cwSQL.COL_TYPE_BOOLEAN)) {
			col_value.append(rs.getBoolean(column[0]));
		}else if (column[1].equalsIgnoreCase(cwSQL.COL_TYPE_STRING)) {
			col_value.append(rs.getString(column[0]));
		}
		return col_value.toString();
    }
    /**
     * @return
     */
    private PreparedStatement getEnrollList(Connection con, long owner_ent_id, String process_status, long itm_id, String app_id_lst, String ent_id_lst,
    String orderBy, String sortOrder, WizbiniLoader wizbini, HttpSession sess, boolean isCount, String select_cols, Vector extra_cols) throws SQLException, IOException, cwException {
        dbUserGroup dbusg = new dbUserGroup();
        Hashtable filter_hash = new Hashtable();
        StringBuffer SQLBuf = new StringBuffer(1024);
        StringBuffer SQLCond = null;
        String cond_sql = "";
        String filter_type = (String) sess.getAttribute("filter_type");
        if (filter_type != null) {
            if (!filter_type.equalsIgnoreCase("advanced_filter") && (String) sess.getAttribute("filter_value") != null) {
                SQLCond = new StringBuffer(1024);
                SQLCond.append(" AND (lower(usr_ste_usr_id) LIKE ? OR lower(usr_display_bil) LIKE ?)");
            } else if (filter_type.equalsIgnoreCase("advanced_filter")) {
                filter_hash = (Hashtable) sess.getAttribute("appn_filter");

                dbusg = (dbUserGroup) filter_hash.get("dbusg");

                if ((Timestamp) filter_hash.get("appn_upd_fr") != null) {
                    cond_sql += " AND app_upd_timestamp >= ?";
                }
                if ((Timestamp) filter_hash.get("appn_upd_to") != null) {
                    cond_sql += " AND app_upd_timestamp <= ?";
                }

                if (dbusg.s_usr_id != null) {
                    cond_sql += " AND lower(usr_ste_usr_id) LIKE ? ";
                }

                if (dbusg.s_usr_job_title != null) {
                    cond_sql += " AND lower(usr_job_title) LIKE ? ";
                }

                if (dbusg.s_usr_email != null) {
                    cond_sql += " AND lower(usr_email) LIKE ? ";
                }

                if (dbusg.s_usr_email_2 != null) {
                    cond_sql += " AND lower(usr_email_2) LIKE ? ";
                }

                if (dbusg.s_usr_initial_name_bil != null) {
                    cond_sql += " AND lower(usr_initial_name_bil) LIKE ? ";
                }

                if (dbusg.s_usr_last_name_bil != null) {
                    cond_sql += " AND lower(usr_last_name_bil) LIKE ? ";
                }

                if (dbusg.s_usr_first_name_bil != null) {
                    cond_sql += " AND lower(usr_first_name_bil) LIKE ? ";
                }

                if (dbusg.s_usr_display_bil != null) {
                    cond_sql += " AND lower(usr_display_bil) LIKE ? ";
                }

                if (dbusg.s_usr_id_display_bil != null) {
                    cond_sql += " AND (lower(usr_display_bil) LIKE ? OR lower(usr_ste_usr_id) LIKE ?) ";
                }

                if (dbusg.s_usr_gender != null) {
                    cond_sql += " AND usr_gender = ? ";
                }

                if (dbusg.s_usr_bday_fr != null) {
                    cond_sql += " AND usr_bday >= ? ";
                }

                if (dbusg.s_usr_bday_to != null) {
                    cond_sql += " AND usr_bday <= ? ";
                }

                if (dbusg.s_usr_jday_fr != null) {
                    cond_sql += " AND usr_join_datetime >= ? ";
                }

                if (dbusg.s_usr_jday_to != null) {
                    cond_sql += " AND usr_join_datetime <= ? ";
                }

                if (dbusg.s_usr_hkid != null) {
                    cond_sql += " AND lower(usr_hkid) LIKE ? ";
                }

                if (dbusg.s_usr_tel != null) {
                    cond_sql += " AND (lower(usr_tel_1) LIKE ? OR lower(usr_tel_2) LIKE ?) ";
                }

                if (dbusg.s_usr_fax != null) {
                    cond_sql += " AND lower(usr_fax_1) LIKE ? ";
                }

                if (dbusg.s_usr_address_bil != null) {
                    cond_sql += " AND lower(usr_address_bil) LIKE ? ";
                }

                if (dbusg.s_usr_postal_code_bil != null) {
                    cond_sql += " AND lower(usr_postal_code_bil) LIKE ? ";
                }

                if (dbusg.usg_ent_id != 0) {
                    cond_sql += " AND usr_ent_id IN (SELECT ern_child_ent_id FROM EntityRelation where ern_type = ? AND ern_ancestor_ent_id = " + dbusg.usg_ent_id + ") ";
                }
                if (dbusg.s_idc_int != 0) {
                    cond_sql += " AND usr_ent_id IN (SELECT ern_child_ent_id FROM EntityRelation where ern_type = ? AND ern_ancestor_ent_id = " + dbusg.s_idc_int + ") ";
                }
                if (dbusg.s_idc_fcs != 0) {
                    cond_sql += " AND usr_ent_id IN (SELECT ern_child_ent_id FROM EntityRelation where ern_type = ? AND ern_ancestor_ent_id = " + dbusg.s_idc_fcs + ") ";
                }
                if (dbusg.s_grade != 0) {
                    cond_sql += " AND usr_ent_id IN (SELECT ern_child_ent_id FROM EntityRelation where ern_type = ? AND ern_ancestor_ent_id = " + dbusg.s_grade + ") ";
                }

                //search all extension field
                if (dbusg.s_ext_col_names != null && dbusg.s_ext_col_names.size() > 0) {
                    for (int i = 0; i < dbusg.s_ext_col_names.size(); i++) {
                        String att_name = (String) dbusg.s_ext_col_names.get(i);
                        int startPos = att_name.indexOf("extension_");
                        if (startPos != -1) {
                            int extPosition = Integer.valueOf(att_name.substring(startPos + 10, att_name.indexOf("_", startPos + 10))).intValue();
                            if (dbusg.s_ext_col_types.get(i).equals(DbTable.COL_TYPE_STRING)) {
                                String sql = null;
                                if (extPosition > 0 && extPosition <= 10 && ((String) dbusg.s_ext_col_values.get(i)).length() > 0) {
                                    sql = " AND usr_extra_" + extPosition + " LIKE ? ";
                                } else if (extPosition > 20 && extPosition <= 30 && ((String) dbusg.s_ext_col_values.get(i)).length() > 0) {
                                    sql = " AND urx_extra_singleoption_" + extPosition + " = ? ";
                                } else if (extPosition > 30 && extPosition <= 40 && ((String[]) dbusg.s_ext_col_values.get(i)).length > 0) {
                                    StringBuffer temp = new StringBuffer();
                                    String[] value = (String[]) dbusg.s_ext_col_values.get(i);
                                    if (value != null && value.length > 0) {
                                        for (int j = 0; j < value.length; j++) {
                                            if (j == 0) {
                                                temp.append(" AND urx_extra_multipleoption_").append(extPosition).append(" LIKE '% ").append(value[j]).append(" %'");
                                            } else {
                                                temp.append(" OR urx_extra_multipleoption_").append(extPosition).append(" LIKE '% ").append(value[j]).append(" %'");
                                            }
                                        }
                                    }
                                    sql = temp.toString();
                                }
                                if (sql != null) {
                                    cond_sql += sql;
                                }
                            }
                            if (dbusg.s_ext_col_types.get(i).equals(DbTable.COL_TYPE_TIMESTAMP)) {
                                String sql = null;
                                if (dbusg.s_ext_col_values.get(i) != "") {
                                    if (att_name.endsWith("_fr")) {
                                        sql = " AND urx_extra_datetime_" + extPosition + " >= ? ";
                                    } else {
                                        sql = " AND urx_extra_datetime_" + extPosition + " <= ? ";
                                    }
                                }
                                if (sql != null) {
                                    cond_sql += sql;
                                }
                            }
                        }
                    }
                }
                if(cond_sql != null && cond_sql.length()>0){
                    SQLCond = new StringBuffer(cond_sql);
                }
            }
        }
        process_status = getProcessStatus(con, process_status, itm_id, owner_ent_id);
        SQLBuf = new StringBuffer(SqlStatements.getApplicationListForEnroll(con, app_id_lst, process_status, itm_id, ent_id_lst, isCount, select_cols));
        if (SQLCond != null) {
            SQLBuf.append(SQLCond);
        }
        if (!isCount) {
        	SQLBuf.append(" Order By ").append(orderBy).append(" ").append(sortOrder);
        }

        PreparedStatement stmt = con.prepareStatement(SQLBuf.toString(), ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        int index = 1;
        stmt.setLong(index++, itm_id);
        stmt.setLong(index++, owner_ent_id);
        stmt.setString(index++, aeApplication.PENDING.toUpperCase());
        if (app_id_lst == null) {
            if (process_status != null && process_status.length() > 0) {
            	if(process_status.equals("已报名") || process_status.equals("Enrolled") || process_status.equals("已報名")) {
            		//do nothing
            	} else {
            		stmt.setString(index++, process_status);
            	}
            }
        }
        if (filter_type != null) {
            if (!filter_type.equalsIgnoreCase("advanced_filter") && (String) sess.getAttribute("filter_value") != null) {
                String filter_value = (String) sess.getAttribute("filter_value");
                stmt.setString(index++, '%' + filter_value.toLowerCase() + '%');
                stmt.setString(index++, '%' + filter_value.toLowerCase() + '%');
            } else if (filter_type.equalsIgnoreCase("advanced_filter")) {

                if ((Timestamp) filter_hash.get("appn_upd_fr") != null) {
                    stmt.setTimestamp(index++, (Timestamp) filter_hash.get("appn_upd_fr"));
                }
                if ((Timestamp) filter_hash.get("appn_upd_to") != null) {
                    stmt.setTimestamp(index++, (Timestamp) filter_hash.get("appn_upd_to"));
                }

                if (dbusg.s_usr_id != null) {
                    stmt.setString(index++, '%' + dbusg.s_usr_id.toLowerCase() + '%');
                }

                if (dbusg.s_usr_job_title != null) {
                    stmt.setString(index++, '%' + dbusg.s_usr_job_title.toLowerCase() + '%');
                }

                if (dbusg.s_usr_email != null) {
                    stmt.setString(index++, '%' + dbusg.s_usr_email.toLowerCase() + '%');
                }

                if (dbusg.s_usr_email_2 != null) {
                    stmt.setString(index++, '%' + dbusg.s_usr_email_2.toLowerCase() + '%');
                }

                if (dbusg.s_usr_initial_name_bil != null) {
                    stmt.setString(index++, '%' + dbusg.s_usr_initial_name_bil.toLowerCase() + '%');
                }

                if (dbusg.s_usr_last_name_bil != null) {
                    stmt.setString(index++, '%' + dbusg.s_usr_last_name_bil.toLowerCase() + '%');
                }

                if (dbusg.s_usr_first_name_bil != null) {
                    stmt.setString(index++, '%' + dbusg.s_usr_first_name_bil.toLowerCase() + '%');
                }

                if (dbusg.s_usr_display_bil != null) {
                    stmt.setString(index++, '%' + dbusg.s_usr_display_bil.toLowerCase() + '%');
                }

                if (dbusg.s_usr_id_display_bil != null) {
                    stmt.setString(index++, '%' + dbusg.s_usr_id_display_bil.toLowerCase() + '%');
                    stmt.setString(index++, '%' + dbusg.s_usr_id_display_bil.toLowerCase() + '%');
                }

                if (dbusg.s_usr_gender != null) {
                    stmt.setString(index++, dbusg.s_usr_gender);

                }

                if (dbusg.s_usr_bday_fr != null) {
                    stmt.setTimestamp(index++, dbusg.s_usr_bday_fr);
                }

                if (dbusg.s_usr_bday_to != null) {
                    stmt.setTimestamp(index++, dbusg.s_usr_bday_to);
                }

                if (dbusg.s_usr_jday_fr != null) {
                    stmt.setTimestamp(index++, dbusg.s_usr_jday_fr);
                }

                if (dbusg.s_usr_jday_to != null) {
                    stmt.setTimestamp(index++, dbusg.s_usr_jday_to);
                }
                if (dbusg.s_usr_hkid != null) {
                    stmt.setString(index++, '%' + dbusg.s_usr_hkid.toLowerCase() + '%');
                }

                if (dbusg.s_usr_tel != null) {
                    stmt.setString(index++, '%' + dbusg.s_usr_tel.toLowerCase() + '%');
                    stmt.setString(index++, '%' + dbusg.s_usr_tel.toLowerCase() + '%');
                }

                if (dbusg.s_usr_fax != null) {
                    stmt.setString(index++, '%' + dbusg.s_usr_fax.toLowerCase() + '%');
                }

                if (dbusg.s_usr_address_bil != null) {
                    stmt.setString(index++, '%' + dbusg.s_usr_address_bil.toLowerCase() + '%');
                }

                if (dbusg.s_usr_postal_code_bil != null) {
                    stmt.setString(index++, '%' + dbusg.s_usr_postal_code_bil.toLowerCase() + '%');
                }

                if (dbusg.usg_ent_id != 0) {
                    stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
                }
                if (dbusg.s_idc_int != 0) {
                	stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_INTEREST_IDC);
                }
                if (dbusg.s_idc_fcs != 0) {
                	stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_FOCUS_IDC);
                }
                if (dbusg.s_grade != 0) {
                	stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_CURRENT_UGR);
                }

                if (dbusg.s_ext_col_names != null && dbusg.s_ext_col_names.size() > 0) {
                    for (int i = 0; i < dbusg.s_ext_col_names.size(); i++) {
                        String att_name = (String) dbusg.s_ext_col_names.get(i);
                        int startPos = att_name.indexOf("extension_");
                        if (startPos != -1) {
                            int extPosition = Integer.valueOf(att_name.substring(startPos + 10, att_name.indexOf("_", startPos + 10))).intValue();
                            if (dbusg.s_ext_col_types.get(i).equals(DbTable.COL_TYPE_STRING)) {
                                String sql = null;
                                if (extPosition > 0 && extPosition <= 10 && ((String) dbusg.s_ext_col_values.get(i)).length() > 0) {
                                    stmt.setString(index++, '%' + ((String) dbusg.s_ext_col_values.get(i)) + '%');
                                } else if (extPosition > 20 && extPosition <= 30 && ((String) dbusg.s_ext_col_values.get(i)).length() > 0) {
                                    stmt.setString(index++, ((String) dbusg.s_ext_col_values.get(i)));
                                }
                            }
                            if (dbusg.s_ext_col_types.get(i).equals(DbTable.COL_TYPE_TIMESTAMP)) {
                                String sql = null;
                                if (dbusg.s_ext_col_values.get(i) != "") {
                                    if (att_name.endsWith("_from")) {
                                        stmt.setTimestamp(index++, (Timestamp) dbusg.s_ext_col_values.get(i));
                                    } else {
                                        stmt.setTimestamp(index++, (Timestamp) dbusg.s_ext_col_values.get(i));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (SQLCond != null || (process_status != null && process_status.length() > 0)) {
            has_filter = true;
        }
        return stmt;
    }
    public Hashtable getSelectColsApp(Connection con, WizbiniLoader wizbini, String root_id, String export_type) throws SQLException {
        StringBuffer select_cols = new StringBuffer();
        select_cols.append("");
        ExportCols export_cols = (ExportCols)wizbini.cfgOrgExportCols.get(root_id);
        List export_lst =null;
        if(export_type ==null || export_type.equalsIgnoreCase(EXPORT_ENROLLMENT)) {
        	export_lst = export_cols.getEnrollmentList().getColumns().getCol();
        } else if (export_type.equalsIgnoreCase(EXPORT_ITEM_SEARCH)) {
        	export_lst = export_cols.getItmSearchList().getColumns().getCol();
        }
        Hashtable export_hash = new Hashtable();
		Vector ext_cols = new Vector();
        if(export_lst != null) {
        	Iterator it = export_lst.iterator();
        	while(it.hasNext()) {
        		ColType col = (ColType)it.next();
        		String id = col.getId();
//        		System.out.println("id = " + id);
        		String extra =col.getExtra();
//        		System.out.println("extra = " + extra);
//        		String label = col.getLabel();
//        		System.out.println("label = " + label);
        		boolean active =col.isActive();
//        		System.out.println("active = " + active);
        		List db_col_lst = col.getDbCol();
        		Vector db_col_vec = new Vector();
        		String[] column = new String[2];
        		if (db_col_lst != null && active && (extra !=null && extra.equals("no"))) {
        			Iterator it_value = db_col_lst.iterator();
        			while (it_value.hasNext()) {
        				DbCol db_col = (DbCol)it_value.next();
        				String name = db_col.getName();
        				String type = db_col.getType();
//        				System.out.println("db_col = " + db_col);
        				select_cols.append(", " + name);
        				column[0] = name;
        				column[1] = type;
        				db_col_vec.add(column);
        			}
        		} else if(extra !=null && extra.equals("yes")) {
        			ext_cols.add(id);
        		}
        		export_hash.put(id, db_col_vec);
        	}
        }
        Hashtable hash = new Hashtable();
        hash.put(EXPORT_HASH, export_hash);
        hash.put(SELECT_COLS, select_cols.toString());
        hash.put(EXTRA_COLS, ext_cols);
        return hash;
    }
   
    public Vector getEnrollVector(Connection con, long owner_ent_id, long itm_id) throws SQLException {
    	 
    	Vector v_app = new Vector();
        
         StringBuffer SQLBuf = new StringBuffer(1024);
         SQLBuf.append("  select distinct app_id, app_status, app_priority,  usr_ent_id, usr_ste_usr_id login_id, ste_name, usr_display_bil usr_name , usr_ste_usr_id, usr_display_bil, usr_tel_1, app_process_status, aal_approval_role, app_upd_timestamp  ");
         SQLBuf.append(" from aeApplication  inner join aeItem  on (app_itm_id = itm_id and itm_id = ? and itm_owner_ent_id = ?) ");
         SQLBuf.append(" inner join RegUser on (app_ent_id = usr_ent_id)  ");
         SQLBuf.append(" inner join Entity on (ent_id = usr_ent_id)  ");
         SQLBuf.append(" inner join acSite on (usr_ste_ent_id = ste_ent_id)  ");
         SQLBuf.append(" inner join RegUserExtension on (urx_usr_ent_id = usr_ent_id)  ");
         SQLBuf.append(" left join (  	select distinct aal_app_id, aal_approval_role from aeAppnApprovalList where aal_status = ?  ) t  ");
         SQLBuf.append(" on (t.aal_app_id = app_id)  Order By app_process_status asc  ");
         
         PreparedStatement stmt = con.prepareStatement(SQLBuf.toString(), ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
         int index = 1;
         stmt.setLong(index++, itm_id);
         stmt.setLong(index++, owner_ent_id);
         stmt.setString(index++, aeApplication.PENDING.toUpperCase());
         
         ResultSet rs = stmt.executeQuery();
         while (rs.next()) {
                 aeApplication app = new aeApplication();
                 app.export_cols_hash = new Hashtable();
                 app.app_id = rs.getLong("app_id");
                 app.app_itm_id = itm_id;
                 app.app_ent_id = rs.getLong("usr_ent_id");
//                 app.app_process_status = rs.getString("app_process_status");
                 app.app_upd_timestamp = rs.getTimestamp("app_upd_timestamp");
                 app.app_priority = rs.getString("app_priority");
                 app.ste_name = rs.getString("ste_name");
//                 app.aal_approval_role = rs.getString("aal_approval_role");
                 app.usr_ent_id = rs.getLong("usr_ent_id");
                 app.usr_ste_usr_id = rs.getString("login_id");
                 app.usr_display_bil = rs.getString("usr_name");
//                 app.usr_tel_1 = rs.getString("usr_tel_1");
                 v_app.addElement(app);
         }
         rs.close();
         stmt.close();
         
         return v_app;
    }
    
    /**
    Get xml of applications based on owner_ent_id, app_process_id and itm_id
    */
    public String getAppnByProcessStatusAsXML(Connection con, long owner_ent_id,
                                              String process_status, long itm_id,
                                              long page_size,
                                              long page_num, String ent_id_lst,
                                              String orderBy, String sortOrder,
                                              boolean download, WizbiniLoader wizbini, HttpSession sess, String root_id)
                                              throws SQLException, cwException, cwSysMessage, IOException {
        return getAppnByProcessStatusAsXML(con, owner_ent_id, process_status, itm_id,
                        page_size, page_num, null, ent_id_lst, orderBy, sortOrder, download, null, wizbini, sess, root_id);

    }

    public String getAppnByProcessStatusAsXML(Connection con, long owner_ent_id,
                                              String process_status, long itm_id,
                                              long page_size,
                                              long page_num, String app_id_lst, String ent_id_lst,
                                              String orderBy, String sortOrder,
                                              boolean download, Vector matchedAppVec,
                                              WizbiniLoader wizbini, HttpSession sess, String root_id)
                                              throws SQLException, cwException, cwSysMessage, IOException {

        StringBuffer xmlBuf = new StringBuffer(1024);
        StringBuffer appXML = new StringBuffer(1024);
        Timestamp cur_time = cwSQL.getTime(con);
        String filter_type = (String)sess.getAttribute("filter_type");

        if(filter_type != null && (filter_type.equalsIgnoreCase("advanced_filter")||(String)sess.getAttribute("filter_value")!= null ||(String)sess.getAttribute("appn_process_status")!= null)){
            if(filter_type.equalsIgnoreCase("advanced_filter")){
                Hashtable filter_hash = (Hashtable)sess.getAttribute("appn_filter");
                process_status = (String)filter_hash.get("appn_process_status");
            }else if((String)sess.getAttribute("appn_process_status")!= null) {
                process_status = (String)sess.getAttribute("appn_process_status");
            }
        }

        if(filter_type != null && (String)sess.getAttribute("filter_value")!= null){
            appXML.append("<filter_value>").append(cwUtils.esc4XML((String)sess.getAttribute("filter_value"))).append("</filter_value>");
        }

        if(orderBy == null || orderBy.length() == 0) {
            orderBy = "usr_ste_usr_id";
        }
        if(sortOrder == null || sortOrder.length() == 0) {
            sortOrder = "ASC";
        }
        //get application list as xml
        aeItem itm = new aeItem();
        String itm_content = null;
        //Vector v_target_lrn = null;
        process_status = getProcessStatus(con, process_status, itm_id, owner_ent_id);
        //Vector v_app = getAppnByProcessStatus(con, owner_ent_id, process_status, itm_id, app_id_lst, ent_id_lst, orderBy, sortOrder);

        Hashtable hash_app = getAppnByProcessStatus(con, owner_ent_id, process_status, itm_id, app_id_lst, ent_id_lst, orderBy, sortOrder, wizbini, sess, page_size, page_num, root_id);
        boolean has_filter =  ((Boolean)hash_app.get("has_filter")).booleanValue();
        appXML.append("<has_filter>").append(has_filter).append("</has_filter>");

        Vector v_app = (Vector)hash_app.get("v_app");
        //Filter the application
        if (matchedAppVec != null && v_app != null) {
            Vector v_app_filtered = new Vector();
            for (int i=0;i<v_app.size();i++) {
                aeApplication app = (aeApplication) v_app.elementAt(i);
                if (matchedAppVec.contains(new Long(app.app_id))) {
                    v_app_filtered.addElement(app);
                }
            }
            v_app = v_app_filtered;
        }
//        System.out.println("before ViewEntityToTree.getTargetEntity");
        String[] tree_sub_type_lst = ViewEntityToTree.getTargetEntity(con, owner_ent_id);
//        System.out.println("after ViewEntityToTree.getTargetEntity");

        if(itm_id != 0) {
            try {
                itm.itm_id = itm_id;
                itm.getItem(con);
                itm_content = itm.contentAsXML(con, null);

                appXML.append("<item id=\"").append(itm.itm_id).append("\"")
                      .append(" title=\"").append(cwUtils.esc4XML(itm.itm_title)).append("\"")
                      .append(" run_ind=\"").append(itm.itm_run_ind).append("\"")
                      .append(" integrated_ind=\"").append(itm.itm_integrated_ind).append("\"")
                      .append(" life_status=\"").append(cwUtils.esc4XML(itm.itm_life_status)).append("\"")
                      .append(" appn_eff_start_datetime=\"").append(itm.itm_appn_start_datetime).append("\"")
                      .append(" appn_eff_end_datetime=\"").append(itm.itm_appn_end_datetime).append("\"")
                      .append(" cur_time=\"").append(cur_time).append("\"")
                      .append(" app_approval_type=\"").append(cwUtils.escNull(itm.itm_app_approval_type)).append("\"")
                      .append(">");
                if(itm.itm_run_ind) {
                    aeItemRelation ire = new aeItemRelation();
                    ire.ire_child_itm_id = itm.itm_id;
                    aeItem ireParentItm = ire.getParentInfo(con);
                    if (ireParentItm != null) {
                        itm.parent_itm_id = ireParentItm.itm_id;

                        appXML.append("<parent id=\"").append(itm.parent_itm_id).append("\"")
                              .append(" title=\"").append(dbUtils.esc4XML(aeUtils.escNull(ireParentItm.itm_title))).append("\"/>");
                    }
                }
                appXML.append("</item>");
                appXML.append(itm.getTargetLrnAsXML(con));
            }
            catch(qdbException e) {
                //do nothing
                ;
            }
        }
        long check_itm_id = itm_id;
        if(itm.itm_run_ind) {
             check_itm_id = itm.parent_itm_id;
        }
        appXML.append(getExportColsXML(wizbini, root_id, EXPORT_ENROLLMENT));
        appXML.append("<application_list status=\"").append(aeUtils.escNull(process_status)).append("\"");

        appXML.append(">");
        aeApplication app;
        dbRegUser usr = new dbRegUser();
        try {
            Hashtable app_hash = null;
            if(itm_id != 0) {
                Vector ent_vec = new Vector();
                for (int i=0; i<v_app.size(); i++) {
                    app = (aeApplication) v_app.elementAt(i);
                    ent_vec.addElement(new Long(app.app_ent_id));
                }
                aeItem itm_temp = new aeItem();
                itm_temp.itm_id = itm_id;

                if (itm.itm_run_ind){
                    if (itm.parent_itm_id == 0){
                        aeItemRelation ire = new aeItemRelation();
                        ire.ire_child_itm_id = itm_id;
                        itm.parent_itm_id = ire.getParentItemId(con);
                    }
                }else{
                    itm.parent_itm_id = itm_id;
                }
                app_hash = itm_temp.getAttendedUser(con, ent_vec, itm.parent_itm_id);
            }

            boolean isOrgHasNoShow = aeAttendanceStatus.isValidStatus(con, aeAttendanceStatus.STATUS_TYPE_NOSHOW, owner_ent_id);
            Hashtable h_itm = null;
            Hashtable h_itm_content = null;
            if(itm_id == 0) {
                h_itm = new Hashtable();
                h_itm_content = new Hashtable();
            }
            for(int i=0; i<v_app.size(); i++) {
                    app = (aeApplication) v_app.elementAt(i);
                    usr.usr_ent_id = app.app_ent_id;
                    usr.get(con);
                    appXML.append("<application id=\"").append(app.app_id).append("\"");
//                    appXML.append(" process_status=\"").append(app.app_process_status).append("\"");
                    appXML.append(" priority=\"").append(aeUtils.escNull(app.app_priority)).append("\"");
                    appXML.append(" timestamp=\"").append(app.app_upd_timestamp).append("\"");
                    appXML.append(">");
                    appXML.append("<app_export_col>");
                    if (app.export_cols_hash != null && app.export_cols_hash.size() > 0) {
                    	Enumeration export_keys = app.export_cols_hash.keys();
                    	while(export_keys.hasMoreElements()) {
                    		String key = (String)export_keys.nextElement();
                    		String value = (String)app.export_cols_hash.get(key);
                    		appXML.append("<").append(cwUtils.esc4XML(cwUtils.escNull(key))).append(">");
                    		if(key.equalsIgnoreCase(EXPORT_COLS_DS) || key.equalsIgnoreCase(EXPORT_COLS_ROLE)) {
                    			appXML.append(value);
                    		} else {
                    			appXML.append(cwUtils.esc4XML(cwUtils.escNull(value)));
                    		}
                    		appXML.append("</").append(cwUtils.esc4XML(cwUtils.escNull(key))).append(">");
                    	}
                    }
                    appXML.append("</app_export_col>");
                    appXML.append("<user id=\"").append(app.usr_ste_usr_id).append("\"")
                    	  .append(" ent_id=\"").append(app.usr_ent_id).append("\"")
	                      .append(" name=\"").append(cwUtils.esc4XML(app.usr_display_bil)).append("\"")
	                      .append(">");
                    appXML.append("</user>");

                    // have noshow status
                    if (isOrgHasNoShow){
                        appXML.append("<no_show_count>").append(aeAttendance.getNoShowCount(con, app.app_ent_id)).append("</no_show_count>");
                    }
                    if(itm_id != 0) {
                        //Not approver
                        appXML.append(app.appnLatestHistoryAsXML(con));
                        appXML.append("<attended_before>");
                        Vector vec_ent_app = (Vector)app_hash.get(new Long(app.app_ent_id));
                        if (vec_ent_app == null || (vec_ent_app.size() == 1 && vec_ent_app.contains(new Long(app.app_id)))) {
                            appXML.append("NO");
                        } else {
                        	long vec_app_id = Long.parseLong(vec_ent_app.get(0)+"");//根据序列，跳过首次报名（第一次报名的永远不出现重读标志）
                        	if(app.app_id != vec_app_id){
		                        	appXML.append("YES");
                        	}
                        }
                        appXML.append("</attended_before>");
                    }
                    if(itm_id == 0) {
                        //approver
                        //get aeItem Object, targeted learner and item content from Hashtables if they have been got before
                        Long key = new Long(app.app_itm_id);
                        itm = (aeItem) h_itm.get(key);
                        itm_content = (String) h_itm_content.get(key);
                        if(itm == null) {
                            // if objects not found in Hashtable, get them from DB and put back to Hashtables
                            itm = new aeItem();
                            itm.itm_id = app.app_itm_id;
                            itm.getItem(con);
                            itm_content = itm.contentAsXML(con, cur_time);
                            h_itm.put(key, itm);
                            h_itm_content.put(key, itm_content);
                        }
                    }
                    if (tree_sub_type_lst != null && tree_sub_type_lst.length > 0) {
                        String itmApplyMethod = aeItem.getItemApplyMethod(con,app.app_itm_id);
                        if(itmApplyMethod.indexOf(DbItemTargetRuleDetail.ITE_ELECTIVE) != -1)
                            appXML.append("<targeted_lrn_ind>").append(aeItem.isTargetedLearner(con, app.app_ent_id, app.app_itm_id, false)).append("</targeted_lrn_ind>");
                        if(itmApplyMethod.indexOf(DbItemTargetRuleDetail.ITE_COMPULSORY) != -1)
                            appXML.append("<comp_targeted_lrn_ind>").append(aeItem.isTargetedLearner(con, app.app_ent_id, app.app_itm_id,  false)).append("</comp_targeted_lrn_ind>");
                    }
                    appXML.append(itm_content);
                    if(!itm.itm_integrated_ind) {
                    	appXML.append(aeItem.getBeIntegratedLrnXml(con, itm.itm_id, app.usr_ent_id));
                    }
                    appXML.append("</application>");
            }
        }
        catch(qdbException e) {
            throw new cwException(e.getMessage());
        }
        appXML.append("</application_list>");
        if(!download) {
            //format XML
            long[] page_var = (long[])hash_app.get("page_var");
            xmlBuf.append("<pagination total_rec=\"").append(page_var[0]).append("\"");
            xmlBuf.append(" page_size=\"").append(page_var[1]).append("\"");
            xmlBuf.append(" cur_page=\"").append(page_var[2]).append("\"");
            xmlBuf.append(" sort_col=\"").append(orderBy).append("\"");
            xmlBuf.append(" sort_order=\"").append(sortOrder).append("\"/>");
            //get workflow template
            aeTemplate tpl = new aeTemplate();
            if(itm_id != 0) {
                tpl.tpl_xml = itm.getRawTemplate(con, aeTemplate.WORKFLOW);
            } else if (v_app.size() > 0) {
                aeApplication result_app = (aeApplication) v_app.elementAt(0);
                aeItem result_itm = new aeItem();
                result_itm.itm_id = result_app.app_itm_id;
                tpl.tpl_xml = itm.getRawTemplate(con, aeTemplate.WORKFLOW);
            }
            else {
                tpl.tpl_xml = aeTemplate.getFirstRawTpl(con, aeTemplate.WORKFLOW, owner_ent_id, "asc");
            }
            xmlBuf.append(tpl.tpl_xml);
        }
        xmlBuf.append(aeItem.getAllItemTypeTitleInOrg(con, owner_ent_id));
        xmlBuf.append(appXML.toString());
        return xmlBuf.toString();
    }
    public static String getExportColsXML(WizbiniLoader wizbini, String root_id, String export_type) throws cwException{
        StringWriter writer = new StringWriter();
        if(export_type == null || export_type.equalsIgnoreCase(EXPORT_ENROLLMENT)) {
        	wizbini.marshal(((ExportCols)wizbini.cfgOrgExportCols.get(root_id)).getEnrollmentList(), writer);
        } else if (export_type.equalsIgnoreCase(EXPORT_ITEM_SEARCH)) {
        	wizbini.marshal(((ExportCols)wizbini.cfgOrgExportCols.get(root_id)).getItmSearchList(), writer);
        }
        String export_cols = writer.toString();
        export_cols = export_cols.substring(export_cols.indexOf("<", 2));
        return export_cols;
    }
    /**
    Get application list as XML base on the app_p
    rocess_status and itm_id, if it is not zero
    */
    public String getAppnListAsXML(Connection con,
                                   String process_status, long itm_id,
                                   long page_size, long page_num,
                                   String orderBy, String sortOrder,
                                   boolean download, long[] app_id, long[] app_ent_id,
                                   boolean show_approval_ent_only,
                                   WizbiniLoader wizbini, HttpSession sess, loginProfile prof)
                                   throws SQLException, cwSysMessage, cwException, IOException {
        StringBuffer xmlBuf = new StringBuffer(1024);
		String[] s = null;
        Vector appVec = null;
        if (show_approval_ent_only) {
            appVec = getApproverInChargeAppId(con, itm_id, prof.usr_ent_id, prof.current_role);
            if (appVec.size() == 0) {
                appVec.addElement(new Long(0));
            }
        }

        String app_ent_id_lst = null;
        if(app_ent_id != null && app_ent_id.length > 0) {
            app_ent_id_lst = cwUtils.array2list(app_ent_id);
        }
        String app_id_lst = null;
        if(app_id != null && app_id.length > 0) {
            app_id_lst = cwUtils.array2list(app_id);
        }

        if(!download) {
            //get the application count on each application process status
            StringBuffer SQLBuf = new StringBuffer(512);
            SQLBuf.append(" select app_process_status, count(*) as cnt ")
                .append(" from aeApplication, aeItem ")
                .append(" where itm_id = app_itm_id ")
                .append(" and itm_owner_ent_id = ? ");
            if(itm_id != 0) {
                SQLBuf.append(" and app_itm_id = ? ");
            }
            if (show_approval_ent_only) {
                //SQLBuf.append(" and app_id IN " + cwUtils.vector2list(appVec));
				s = cwSQL.getSQLClause(con,"tmp_app_id",cwSQL.COL_TYPE_LONG,appVec,0);
				SQLBuf.append(" and app_id IN " + s[0]);
            }

            SQLBuf.append(" group by app_process_status ");
//            System.out.println(SQLBuf.toString());

            PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
            int index = 1;
            stmt.setLong(index++, prof.root_ent_id);
            if(itm_id != 0) {
                stmt.setLong(index++, itm_id);
            }
            ResultSet rs = stmt.executeQuery();
            long total_cnt = 0;
            long cnt;
            xmlBuf.append("<process_status_cnt_list>");
            while(rs.next()) {
                cnt = rs.getLong("cnt");
                total_cnt += cnt;
                xmlBuf.append("<process_status_cnt name=\"").append(rs.getString("app_process_status")).append("\"");
                xmlBuf.append(" cnt=\"").append(cnt).append("\"/>");
            }
            stmt.close();
            xmlBuf.append("<total_process_status_cnt ");
            xmlBuf.append(" cnt=\"").append(total_cnt).append("\"/>");
            xmlBuf.append("</process_status_cnt_list>");
        }
        xmlBuf.append(getAppnByProcessStatusAsXML(con, prof.root_ent_id,
                                                    process_status, itm_id,
                                                    page_size,
                                                    page_num, app_id_lst, app_ent_id_lst,
                                                    orderBy, sortOrder, download, appVec, wizbini, sess, prof.root_id));
		if(s != null && s[1] != null){
		    cwSQL.dropTempTable(con,s[1]);
		}
        return xmlBuf.toString();
    }

    /**
    Get the application list under approver "prof"
    and app_process_status should equal to input process_status
    */
    public String getApprAppnListAsXML(Connection con, long owner_ent_id,
                                       String process_status, long page_size,
                                       long page_num,
                                       loginProfile prof,
                                       String orderBy, String sortOrder,
                                       boolean download, WizbiniLoader wizbini)
                                       throws SQLException, cwException, cwSysMessage, IOException {
        //get approver's servant
        Vector v_usr_ent_id =
            ViewRoleTargetGroup.getTargetGroupsLrn(con, prof.usr_ent_id, prof.current_role, false);
        /*
        for(int i=0; i<v_usr_ent_id.size(); i++) {
            System.out.println("targeted lrn for approver = " + (Long)v_usr_ent_id.elementAt(i));
        }
        */

        //getAppnByProcessStatusAsXML
        if(v_usr_ent_id.size() == 0) {
            v_usr_ent_id.addElement(new Long(0));
        }
        String usr_ent_id_lst = cwUtils.vector2list(v_usr_ent_id);

        return getAppnByProcessStatusAsXML(con, owner_ent_id,
                                           process_status, 0, page_size,
                                           page_num, usr_ent_id_lst,
                                           orderBy, sortOrder, download, wizbini, null, prof.root_id);
    }

    public void delAppn(Connection con, long[] app_id_lst, Timestamp[] app_upd_timestamp_lst,long owner_ent_id)
        throws SQLException, cwException, cwSysMessage {
            aeApplication app = new aeApplication();
            for(int i=0; i<app_id_lst.length; i++) {
                //<<add for bjmu
                CFCertificate cf = new CFCertificate(con);
                cf.delCertificationItemAppID((int)app_id_lst[i],(int)owner_ent_id);
                //>>add for bjmu
                app.app_id = app_id_lst[i];
                app.del(con, app_upd_timestamp_lst[i]);
            }
        return;
    }

    public void enrolCos(Connection con, long app_id, long itm_id, long ent_id, loginProfile prof)
        throws SQLException, cwException, cwSysMessage {
        enrolCos(con, app_id, itm_id, ent_id, prof, 0);
    }

    /**
    Enrol into a course<Br>
    Insert into ResourcePermission and Enrollment<BR>
    Active aeAppnEnrolRelation by (ent_id, res_id)<BR>
    */
    public void enrolCos(Connection con, long app_id, long itm_id, long ent_id, loginProfile prof, long tkh_id)
        throws SQLException, cwException, cwSysMessage {
        dbCourse cos = new dbCourse();

        cos.cos_res_id = getResId(con, itm_id);
        cos.cos_itm_id = itm_id;

        if(cos.cos_res_id != 0) {
            //update enrolment

            try {
                cos.enroll(con, ent_id, prof);
            }
            catch(qdbException e) {
                throw new cwException(e.getMessage());
            }




            //save attendance
            //System.out.println("Trigger init_attendance");
            int ats_id = aeAttendanceStatus.getIdByType(con, prof.root_ent_id, "PROGRESS");

            aeAttendance.saveStatus(con, app_id, itm_id , cos.cos_res_id, ent_id,
                                    false, ats_id, prof.usr_id, tkh_id);

            aeItem itm = new aeItem();
            itm.itm_id = itm_id;
            itm.getItem(con);
            if (itm.itm_create_session_ind){
                // check if the session has attendance
                DbItemType dbIty = new DbItemType();
                dbIty.ity_id = itm.itm_type;
                dbIty.ity_owner_ent_id = itm.itm_owner_ent_id;
                dbIty.ity_run_ind = false;
                dbIty.ity_session_ind = true;
                Hashtable h_ity_inds = dbIty.getInd(con);
                if (((Boolean)h_ity_inds.get("ity_has_attendance_ind")).booleanValue()){
                    Vector v_app_id = new Vector();
                    v_app_id.addElement(new Long(app_id));
                    aeSession.insSessionAttendance(con, itm_id, null, v_app_id, ats_id, prof.usr_id, prof.root_ent_id);
                }
            }
            //System.out.println("attendance saved");
        }

        return;
    }

    /**
    Unenrol a course<BR>
    Delete aeAppnEnrolRelation based on (aer_app_id, ent_id, res_id)<BR>
    If count(*) of aeAppnEnrolRelation base on (ent_id, res_id) is zero, unenroll from RecoursePermission and Enrolment as well
    */
    public void unenrolCos(Connection con, long app_id, long itm_id, long ent_id, loginProfile prof,
                           long aer_app_id)
                           throws SQLException, cwException, cwSysMessage {
        dbCourse cos = new dbCourse();
        cos.cos_res_id = getResId(con, itm_id);
        if(cos.cos_res_id != 0) {
            //update attendance
            //System.out.println("Trigger make_attendance_incomplete");
            aeAttendance.delByAppn(con, app_id);
            /*
            aeAttendance att = new aeAttendance();
            att.att_app_id = app_id;
            String cur_ats_type = att.getAttStatus(con);
            if(cur_ats_type == null
                || !cur_ats_type.equalsIgnoreCase(aeAttendanceStatus.STATUS_TYPE_ATTEND)) {
                int ats_id = aeAttendanceStatus.getIdByType(con, prof.root_ent_id, "INCOMPLETE");
                aeAttendance.saveStatus(con, app_id, cos.cos_res_id, ent_id,
                                        false, ats_id, prof.usr_id);
                //System.out.println("attendance updated to incomplete");
            }
            */
            cos.unenroll(con, ent_id, prof.root_ent_id);
        }
        return;
    }

    private static final String sql_upd_child_app_syn_date =
        " Update aeApplication set app_syn_date = ? " +
        " Where app_id in " +
        " (select * from (select p.app_id from aeProgramDetails, aeApplication p" +
        " where p.app_ent_id = ? " +
        " And pdt_itm_id = p.app_itm_id " +
        " And pdt_pgm_itm_id = ?) aeApp )";
    private static final String sql_upd_app_syn_date =
        " Update aeApplication set app_syn_date = ? " +
        " Where app_ent_id = ? " +
        " And app_itm_id = ? ";
    /**
    Update SynDate of application (ent_id, itm_id)
    And update the SynDate on applications of the child courses
    */
    public void updAppnSynDate(Connection con, long ent_id, long itm_id, Timestamp syn_date)
        throws SQLException {

        //update appn syn date of (ent_id, itm_id)
//                System.out.println(sql_upd_app_syn_date);

        PreparedStatement stmt = con.prepareStatement(sql_upd_app_syn_date);
        stmt.setTimestamp(1, syn_date);
        stmt.setLong(2, ent_id);
        stmt.setLong(3, itm_id);
        stmt.executeUpdate();
        stmt.close();

        //lookup aeProgramDetails to update syn_date of application on courses if input item is a program
//        System.out.println(sql_upd_child_app_syn_date);
        String get_app_id_list_sql ="select p.app_id as app_id from aeProgramDetails, aeApplication p" +
			        " where p.app_ent_id = ? " +
			        " And pdt_itm_id = p.app_itm_id " +
			        " And pdt_pgm_itm_id = ?";
        stmt = con.prepareStatement(get_app_id_list_sql);
        stmt.setLong(1, ent_id);
        stmt.setLong(2, itm_id);
        ResultSet rs = stmt.executeQuery();
        String appIds = "";
        while(rs.next()) {
        	appIds=appIds+rs.getString("app_id")+",";
        }
        stmt.close();
        
       if(appIds.length() > 0){
    	   String sql_upd_child_app_syn_date_by_id ="Update aeApplication set app_syn_date = ? Where app_id in ( ? )";
    	   stmt = con.prepareStatement(sql_upd_child_app_syn_date_by_id);
           stmt.setTimestamp(1, syn_date);
           stmt.setString(2, appIds.substring(0, appIds.length()-1));
           stmt.executeUpdate();
           stmt.close();
       } 
        
        return;
    }

    private static final String sql_get_non_syn_appn =
        " Select app_id From aeApplication, aeItem " +
        " Where (app_syn_date < ? Or app_syn_date is null) " +
        " and app_itm_id = itm_id " +
        " and itm_type = ? " +
        " And Exists (Select * from aeItem Where itm_id = app_itm_id And itm_owner_ent_id = ?) ";
    /**
    Get the not-in-syn applpications' app_id
    @param con Connection to database
    @param synDate start syn_date
    @param siteId organization site id
    @return Vector of app_id
    */
    public Vector getNotInSynAppId(Connection con, Timestamp synDate, long siteId, String itemType, Boolean isRun) throws SQLException {
        PreparedStatement stmt = null;
        Vector v = new Vector();
        try {
            String sql = sql_get_non_syn_appn;
            if (isRun!=null)    sql += " and itm_run_ind = ? ";

//            System.out.println(sql);
            stmt = con.prepareStatement(sql);
            stmt.setTimestamp(1, synDate);
            stmt.setString(2, itemType);
            stmt.setLong(3, siteId);
            if (isRun!=null)    stmt.setBoolean(4, isRun.booleanValue());

            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                v.addElement(new Long(rs.getLong("app_id")));
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        return v;
    }

    public void WithdrawNonSynAppn(Connection con, Timestamp syn_date, loginProfile prof)
        throws SQLException, cwException, IOException, cwSysMessage {
        //System.out.println("qm.WithdrawNonSynAppn");
        aeApplication app = new aeApplication();
        aeWorkFlow workFlow = new aeWorkFlow(dbUtils.xmlHeader);
//        System.out.println(sql_get_non_syn_appn);
        PreparedStatement stmt = con.prepareStatement(sql_get_non_syn_appn);
        stmt.setTimestamp(1, syn_date);
        stmt.setLong(2, prof.root_ent_id);
        ResultSet rs = stmt.executeQuery();
        try {
            while(rs.next()) {
                app.app_id = rs.getLong("app_id");
                //System.out.println("app_id = " + app.app_id);
                app.getWithItem(con);
                //System.out.println("getwithItem OK");
                Vector args =
                    workFlow.getAction("<applyeasy>" + app.workFlowTpl + "</applyeasy>", "batch", true);
                //System.out.println("after get withdraw action");
                if(args != null && args.size() == 6) {
                    long process_id = Long.parseLong((String)args.elementAt(0));
                    long status_id = Long.parseLong((String)args.elementAt(1));
                    long action_id = Long.parseLong((String)args.elementAt(2));
                    String fr = (String)args.elementAt(3);
                    String to = (String)args.elementAt(4);
                    String verb = (String)args.elementAt(5);
                    try {
                        doAppnActn(con, app.app_id, process_id,
                                fr, to, verb, action_id,
                                status_id, prof, "NotSyn",
                                null, syn_date, true);
                    }
                    catch(cwSysMessage e) {
                        ; //do nothing and continue with "action is invalid"
                    }
                }
            }
            stmt.close();
        }
        catch(qdbException e) {
            throw new cwException(e.getMessage());
        }
        return;
    }

    /**
    Do and save enrolment assignment
    */
    public static  void doEnrolAssignment(Connection con, long[] itm_id_lst, long[] ent_id_lst,
                                  long[] app_id_lst, Timestamp[] app_upd_timestamp_lst,
                                  loginProfile prof)
                                  throws SQLException, cwException, IOException, cwSysMessage {

        if(itm_id_lst != null && app_id_lst != null
           && app_upd_timestamp_lst != null && ent_id_lst != null) {
            aeApplication app = new aeApplication();
            for(int i=0; i<itm_id_lst.length; i++) {
                if(itm_id_lst[i] == 0 && app_id_lst[i] == 0) {
                    //if the entity does not assigned to an item
                    //and the user orginally does not have any application
                    //do nothing
                }
                else if(itm_id_lst[i] == 0 && app_id_lst[i] != 0) {
                    //if the entity does not assigned to an item
                    //and the user orginally has an application
                    //delete this application
                    app.app_id = app_id_lst[i];
                    app.del(con, app_upd_timestamp_lst[i]);
                }
                else if(itm_id_lst[i] != 0 && app_id_lst[i] == 0) {
                    //if the entity is assigned to an item
                    //and the user orginally does not have any application
                    //insert application
                    try {
                        aeQueueManager qm = new aeQueueManager();
                        qm.insApplication(con, null, ent_id_lst[i],
                                    itm_id_lst[i], prof, 0);
                        //System.out.println("ins ent_id = " + ent_id_lst[i]);
                    }
                    catch(qdbException e) {
                        throw new cwException(e.getMessage());
                    }
                }
                else if(itm_id_lst[i] != 0 && app_id_lst[i] != 0) {
                    //if the entity is assigned to an item
                    //and the user orginally has an application
                    app.app_id = app_id_lst[i];
                    try {
                        app.get(con);
                        if(app.app_itm_id != itm_id_lst[i]) {
                            //if the entity has changed to another run, delete the appn
                            //and ins a new applicaion
                            app.del(con, app_upd_timestamp_lst[i]);
                            aeQueueManager qm = new aeQueueManager();
                            qm.insApplication(con, null, ent_id_lst[i],
                                        itm_id_lst[i], prof, 0);
                        }
                    }
                    catch(qdbException e) {
                        throw new cwException(e.getMessage());
                    }
                    //app.updAppnItemId(con, itm_id_lst[i], app_upd_timestamp_lst[i]);
                }
            }
        }
        return;
    }

    /**
    Insert multiple application at a time
    */
    public void insMultiAppn(Connection con, long itm_id, long[] ent_id_lst, loginProfile prof)
        throws SQLException, IOException, cwException, cwSysMessage {
            insMultiAppn(con, itm_id, ent_id_lst, prof, 0, 0, 0, null, null);
        }

    public void insMultiAppn(Connection con, long itm_id, long[] ent_id_lst, loginProfile prof, long process_id, long status_id, long action_id, String to, String verb)
        throws SQLException, IOException, cwException, cwSysMessage {


        if(ent_id_lst != null && itm_id != 0) {
            Vector v_member_ent_id;
            aeItem item = new aeItem();
            item.itm_id = itm_id;
            item.getItem(con);
            dbEntity ent = new dbEntity();
            try {
                for(int i=0; i<ent_id_lst.length; i++) {

                    ent.ent_id = ent_id_lst[i];
                    ent.get(con);


                    if(ent.ent_type.equals(dbEntity.ENT_TYPE_USER_GROUP)) {
                    	dbEntityRelation dbEr = new dbEntityRelation();
                    	dbEr.ern_type = dbEntityRelation.ERN_TYPE_USR_PARENT_USG;
                    	dbEr.ern_ancestor_ent_id = ent_id_lst[i];
                        v_member_ent_id = dbEr.getChildUser(con);

                    }
                    else if(ent.ent_type.equals(dbEntity.ENT_TYPE_USER)) {
                        v_member_ent_id = new Vector();
                        v_member_ent_id.addElement(new Long(ent_id_lst[i]));
                    }
                    else {
                        v_member_ent_id = new Vector();
                    }
                    for(int j=0; j<v_member_ent_id.size(); j++) {
                        //insert application for each member if has a learner role
                        long member_ent_id = ((Long)v_member_ent_id.elementAt(j)).longValue();

                        boolean has_lrn_role = AccessControlWZB.hasRole( con, member_ent_id, AccessControlWZB.ROL_EXT_ID_NLRN);

                        if(has_lrn_role) {
                        	 if(item.itm_app_approval_type == null || item.itm_app_approval_type.length() == 0 || auto_enroll_ind) {
          						insAppNoWorkflow(con, null, member_ent_id, itm_id, null,null, prof, item);
          						//如果是项目式培训会自动报名其以下课程
//          						if(item.itm_integrated_ind){
//          						    Vector vec = IntegratedLrn.getCourse( con,  itm_id) ;
//          						    if(vec != null && vec.size() > 0){
//          						        for(int k=0; k<vec.size(); k++) {
//          						            IntegratedLrn itgLrn = (IntegratedLrn)vec.elementAt(k);
//          						            try{
//          						                if(itgLrn != null && itgLrn.itm_apply_ind){
//          						                	aeApplication app = new aeApplication();
//          						                	app.insApp( con,  prof,  member_ent_id, itgLrn.itm_id,  true, null);
//          						                }
//          						            }catch(Exception e){
//          						                e.printStackTrace();
//          						            }
//          						        }
//          						    }
//          						}
          					} else{
          					  insApplication(con, null, member_ent_id,
                                      itm_id, prof, 0, process_id, status_id, action_id, to, verb, false, 0, null);
          					}

                        }
                    }
                }
            }
            catch(qdbException e) {
                throw new cwException(e.getMessage());
            }
        }
        return;
    }

    public void insMultiAppnBySuperviser(Connection con, long itm_id, long[] ent_id_lst, loginProfile prof)
            throws SQLException, IOException, cwException, cwSysMessage {
        if(ent_id_lst != null && itm_id != 0) {
            Vector v_member_ent_id;
            aeItem item = new aeItem();
            item.itm_id = itm_id;
            item.getItem(con);
            dbEntity ent = new dbEntity();
            try {
                for(int i=0; i<ent_id_lst.length; i++) {
                    ent.ent_id = ent_id_lst[i];
                    ent.get(con);
                    if(ent.ent_type.equals(dbEntity.ENT_TYPE_USER_GROUP)) {
                        dbEntityRelation dbEr = new dbEntityRelation();
                        dbEr.ern_type = dbEntityRelation.ERN_TYPE_USR_PARENT_USG;
                        dbEr.ern_ancestor_ent_id = ent_id_lst[i];
                        v_member_ent_id = dbEr.getChildUser(con);

                    }
                    else if(ent.ent_type.equals(dbEntity.ENT_TYPE_USER)) {
                        v_member_ent_id = new Vector();
                        v_member_ent_id.addElement(new Long(ent_id_lst[i]));
                    }
                    else {
                        v_member_ent_id = new Vector();
                    }
                    for(int j=0; j<v_member_ent_id.size(); j++) {
                        //insert application for each member if has a learner role
                        long member_ent_id = ((Long)v_member_ent_id.elementAt(j)).longValue();

                        boolean has_lrn_role = AccessControlWZB.hasRole( con, member_ent_id, AccessControlWZB.ROL_EXT_ID_NLRN);

                        if(has_lrn_role) {

                            SuperviseTargetEntityMapper superviseTargetEntityMapper = (SuperviseTargetEntityMapper) WzbApplicationContext.getBean
                                    ("superviseTargetEntityMapper");

                            boolean loginUserHasAdminRole = false;
                            try {
                                loginUserHasAdminRole = AccessControlWZB.hasRole(con, prof.usr_ent_id,
                                        AccessControlWZB.ROL_STE_UID_TADM);
                            } catch(Exception e) {

                            }
                            if(auto_enroll_ind &&
                                    (( item.itm_app_approval_type == null || item.itm_app_approval_type.length() == 0)
                                    ||  (loginUserHasAdminRole
                                            && (item.itm_app_approval_type != null &&
                                            item.itm_app_approval_type.indexOf(AccessControlWZB.ROL_STE_UID_TADM)>-1))  //isTAdminCanDO
                                    ||  (superviseTargetEntityMapper.isUserSuperviser(member_ent_id, prof.usr_ent_id)
                                            && (item.itm_app_approval_type != null
                                            && item.itm_app_approval_type.indexOf("DIRECT_SUPERVISE")>-1
                                            && item.itm_app_approval_type.indexOf("DIRECT_SUPERVISE_SUPERVISE") < 0))//isSuperviserCanDO
                                    ||  (superviseTargetEntityMapper.isUserGroupSuperviser(member_ent_id, prof.usr_ent_id)
                                            && (item.itm_app_approval_type != null
                                            && item.itm_app_approval_type.indexOf("DIRECT_SUPERVISE_SUPERVISE")>-1))//isgGroupSuperviserCanDO
                                    ) ) {
                                insAppNoWorkflow(con, null, member_ent_id, itm_id, null,null, prof, item);
                            } else{
                                insApplication(con, null, member_ent_id, itm_id, prof, 0, 0, 0, 0, null, null,
                                        true, 0,
                                        null, null);
                                /*insApplication(con, null, member_ent_id,
                                        itm_id, prof, 0, 0, 0, 0, null, null, false, 0, null);*/
                            }

                        }
                    }
                }
            }
            catch(qdbException e) {
                throw new cwException(e.getMessage());
            }
        }
        return;
    }

    private static final String sql_get_ent_id_from_aer =
        " Select aer_ent_id, app_id from aeAppnEnrolRelation, aeApplication " +
        " Where aer_app_id = app_id " +
        " And app_itm_id = ? " +
        " And aer_res_id = ? " ;

    public void delOnePgmCos(Connection con, long pgm_itm_id, long itm_id, long cos_res_id, loginProfile prof)
        throws SQLException, cwException {

        dbCourse cos = new dbCourse();
        cos.cos_res_id = (cos_res_id == 0) ? cos.getCosResId(con, itm_id)
                                           : cos_res_id;
        //get application from aeAppnEnrolRelation
//        System.out.println(sql_get_ent_id_from_aer);

        PreparedStatement stmt = con.prepareStatement(sql_get_ent_id_from_aer);
        Vector v_ent_id = new Vector();
        Vector v_pgm_app_id = new Vector();
        stmt.setLong(1, pgm_itm_id);
        stmt.setLong(2, cos.cos_res_id);
        ResultSet rs = stmt.executeQuery();
        while(rs.next()) {
            v_ent_id.addElement(new Long(rs.getLong("aer_ent_id")));
            v_pgm_app_id.addElement(new Long(rs.getLong("app_id")));
        }
        stmt.close();

        //for applicant, check if they still have record in aeAppnEnrolRelation
        //if no record found, withdraw application and
        aeApplication app = new aeApplication();
        for(int i=0; i<v_ent_id.size(); i++) {
            long ent_id = ((Long)v_ent_id.elementAt(i)).longValue();
            //set it to Withdrawn
            app.app_id = aeApplication.getAppId(con,itm_id,ent_id, true);
            if(app.app_id != 0) {
                app.app_status = aeApplication.WITHDRAWN;
                app.updAppnStatus(con);
                aeAttendance.delByAppn(con, app.app_id);
                //update attendance
                /*
                aeAttendance att = new aeAttendance();
                att.att_app_id = app.app_id;
                String cur_ats_type = att.getAttStatus(con);
                if(cur_ats_type == null
                    || !cur_ats_type.equalsIgnoreCase(aeAttendanceStatus.STATUS_TYPE_ATTEND)) {
                    int ats_id = aeAttendanceStatus.getIdByType(con, prof.root_ent_id, "INCOMPLETE");
                    aeAttendance.saveStatus(con, app.app_id, cos.cos_res_id, ent_id,
                                            false, ats_id, prof.usr_id);
                }
                */
                cos.unenroll(con, ent_id, prof.root_ent_id);
            }
        }
        return;
    }

    private static final String sql_get_appn_lst =
        " Select app_id, app_ent_id From aeApplication Where app_itm_id = ? " +
        " AND app_status <> ? AND app_status <> ? " +
        " AND (Exists (select att_app_id from aeAttendance, aeAttendanceStatus where att_app_id = app_id AND att_itm_id = app_itm_id AND att_ats_id = ats_id and ats_type = ?) " +
        " OR Not Exists (select att_app_id from aeAttendance where att_app_id = app_id AND att_itm_id = app_itm_id )) ";
    public void applyOnePgmCos(Connection con, long pgm_itm_id, long cos_itm_id, loginProfile prof)
        throws SQLException, cwException, IOException, cwSysMessage {
//System.out.println(sql_get_appn_lst);
        PreparedStatement stmt = con.prepareStatement(sql_get_appn_lst);
        stmt.setLong(1, pgm_itm_id);
        stmt.setString(2, aeApplication.REJECTED);
        stmt.setString(3, aeApplication.WITHDRAWN);
        stmt.setString(4, aeAttendanceStatus.STATUS_TYPE_PROGRESS);
        ResultSet rs = stmt.executeQuery();
        try {
            while(rs.next()) {
                long aer_app_id = rs.getLong("app_id");
                long app_ent_id = rs.getLong("app_ent_id");
                insApplication(con, null, app_ent_id,
                            cos_itm_id, prof, aer_app_id);
            }
        }
        catch(qdbException e) {
            throw new cwException (e.getMessage());
        }
        finally {
            stmt.close();
        }
        return;
    }

    private static final String sql_get_appn_history =
        " select app_id, app_status, app_process_status " +
        " from aeApplication " +
        " where app_ent_id = ? " +
        " and app_itm_id = ? " +
        " order by app_create_timestamp desc ";
    /**
    Get XML of application history of a user on an item
    @param usr_ent_id applicant's entity id
    */
    public String getAppnHistoryAsXML(Connection con, long usr_ent_id, long itm_id,
                                      int page, int page_size)
                                      throws SQLException, cwSysMessage, cwException {

        StringBuffer xmlBuf = new StringBuffer(1024);
        xmlBuf.append("<enrolment_history>");
        //format user xml
        xmlBuf.append("<user ent_id=\"").append(usr_ent_id).append("\"/>");
        //format item xml
        aeItem itm = new aeItem();
        itm.itm_id = itm_id;
        itm.getItem(con);
        xmlBuf.append("<item id=\"").append(itm.itm_id).append("\"");
        xmlBuf.append(" title=\"").append(aeUtils.escNull(cwUtils.esc4XML(itm.itm_title))).append("\"");
        xmlBuf.append(" run_ind=\"").append(itm.itm_run_ind).append("\">");
        if(itm.itm_run_ind) {
            aeItemRelation ire = new aeItemRelation();
            ire.ire_child_itm_id = itm.itm_id;
            aeItem ireParentItm = ire.getParentInfo(con);
            if (ireParentItm != null) {
                xmlBuf.append("<parent id=\"").append(ireParentItm.itm_id).append("\"");
                xmlBuf.append(" title=\"").append(cwUtils.esc4XML(aeUtils.escNull(ireParentItm.itm_title))).append("\"/>");
            }
        }
        xmlBuf.append("</item>");
        //application xml
        StringBuffer appXML = new StringBuffer(1024);
//        System.out.println(sql_get_appn_history);
        PreparedStatement stmt = con.prepareStatement(sql_get_appn_history);
        stmt.setLong(1, usr_ent_id);
        stmt.setLong(2, itm_id);
        ResultSet rs = stmt.executeQuery();
        int cnt = 0;
        if(page == 0) page = 1;
        if(page_size == 0) page_size = 10;
        int start = (page-1) * page_size;
        int end  = page * page_size - 1;
        try {
            while(rs.next()) {
                if(cnt >= start && cnt <= end) {
                    long app_id = rs.getLong("app_id");
                    appXML.append("<application id=\"").append(app_id).append("\"");
                    appXML.append(" status=\"").append(rs.getString("app_status")).append("\"");
                    appXML.append(" process_status=\"").append(rs.getString("app_process_status")).append("\">");
                    aeApplication app = new aeApplication();
                    app.app_id = app_id;
                    appXML.append(app.appnLatestHistoryAsXML(con));
                    appXML.append("</application>");
                }
                cnt++;
            }
            stmt.close();
        }
        catch(qdbException e) {
            throw new cwException(e.getMessage());
        }
        //pagination tag
        cwPagination cwPage = new cwPagination();
        cwPage.totalRec = cnt;
        cwPage.curPage = page;
        cwPage.pageSize = page_size;
        cwPage.totalPage = (int)Math.ceil((float)cwPage.totalRec / cwPage.pageSize);
        cwPage.sortCol = "";
        cwPage.sortOrder = "";
        cwPage.ts = null;
        xmlBuf.append(cwPage.asXML());
        xmlBuf.append(appXML.toString());
        xmlBuf.append("</enrolment_history>");
        return xmlBuf.toString();
    }

    private String getProcessStatus(Connection con, String process_status, long itm_id, long owner_ent_id)
        throws SQLException, IOException, cwException {

        if(process_status != null) {
            try {
                //test if process_status is a number
                Long.parseLong(process_status);
                aeTemplate tpl = new aeTemplate();
                if(itm_id != 0) {
                    //get item workflow
                    aeItem item = new aeItem();
                    item.itm_id = itm_id;
                    tpl.tpl_xml = item.getRawTemplate(con, aeTemplate.WORKFLOW);
                    /*
                    tpl.tpl_id = item.getTemplateId(con, aeTemplate.WORKFLOW);
                    tpl.get(con);
                    */
                } else {
                    //get the 1st workflow template in the organization no matter what item type
                    tpl.tpl_xml = aeTemplate.getFirstRawTpl(con, aeTemplate.WORKFLOW, owner_ent_id, "asc");
                    /*
                    tpl.tpl_id = aeTemplate.getFirstTplId(con, aeTemplate.WORKFLOW, owner_ent_id, "asc");
                    tpl.getRawTemplate(con);
                    */
                }
                String inRule = "<applyeasy>" + tpl.tpl_xml + "</applyeasy>";
                aeWorkFlow wkf = new aeWorkFlow(dbUtils.xmlHeader);
                process_status = wkf.getProcessStatus(inRule, null, process_status);
            }
            catch(NumberFormatException e) {
                //Do nothing if process_status is not a number
                ;
            }
        }
        return process_status;
    }

    public aeApplication.ViewAppnUser[] getEnrollmentUser(Connection con, long itm_id, String process_status, long root_ent_id)
        throws SQLException, IOException, cwException {

            String status = getProcessStatus(con, process_status, itm_id, root_ent_id);
            aeApplication aeApp = new aeApplication();
            aeApplication.ViewAppnUser[] appnUser = aeApp.getEnrolledUser(con, itm_id, status, "usr_display_bil", "ASC");
            return appnUser;
    }

    public String getApproverAppnListAsXML(Connection con, long owner_ent_id,
                                           long user_ent_id, String rol_ext_id,
                                           String process_status, int tpl_id,
                                           Hashtable appStatusList,
                                           long page_size, long page_num,
                                           String orderBy, String sortOrder,
                                           boolean download)
            throws SQLException, cwException, cwSysMessage, IOException {

        return (getApproverAppnListAsXML(con, owner_ent_id,
                                      user_ent_id, rol_ext_id,
                                           process_status, tpl_id,
                                           appStatusList,
                                           page_size, page_num,
                                           orderBy,  sortOrder,
                                           download, 0));


     }


    /**
     * added for the application list of approver
     * Emily, 20020830
     *
     * @param con
     * @param owner_ent_id
     * @param process_status
     * @param page_size
     * @param page_num
     * @param orderBy
     * @param sortOrder
     * @param download
     * @return
     * @throws SQLException
     * @throws cwException
     * @throws cwSysMessage
     * @throws IOException
     */
    public String getApproverAppnListAsXML(Connection con, long owner_ent_id,
                                           long user_ent_id, String rol_ext_id,
                                           String process_status, int tpl_id,
                                           Hashtable appStatusList,
                                           long page_size, long page_num,
                                           String orderBy, String sortOrder,
                                           boolean download, long itm_id)
            throws SQLException, cwException, cwSysMessage, IOException {
        return (getApproverAppnListAsXML(con, owner_ent_id,
                                      user_ent_id, rol_ext_id,
                                           process_status, tpl_id,
                                           appStatusList,
                                           page_size, page_num,
                                           orderBy,  sortOrder,
                                           download, itm_id, null));
     }

    public String getApproverAppnListAsXML(Connection con, long owner_ent_id,
                                           long user_ent_id, String rol_ext_id,
                                           String process_status, int tpl_id,
                                           Hashtable appStatusList,
                                           long page_size, long page_num,
                                           String orderBy, String sortOrder,
                                           boolean download, long itm_id, String show_status)
            throws SQLException, cwException, cwSysMessage, IOException {
        StringBuffer result = new StringBuffer();

        if(tpl_id == 0 && itm_id !=0){
            tpl_id = aeTemplate.getTplIdByItmId(con, "WORKFLOW", itm_id);
        }
        if(itm_id != 0){
            // item information
            aeItem itm = new aeItem();
            itm.itm_id = itm_id;
            itm.getItem(con);

            result.append("<item id=\"").append(itm.itm_id).append("\"")
                    .append(" title=\"").append(cwUtils.esc4XML(itm.itm_title)).append("\"")
                    .append(" run_ind=\"").append(itm.itm_run_ind).append("\"")
                    .append(" life_status=\"").append(cwUtils.esc4XML(itm.itm_life_status)).append("\"")
                    .append(" appn_eff_start_datetime=\"").append(itm.itm_appn_start_datetime).append("\"")
                    .append(" appn_eff_end_datetime=\"").append(itm.itm_appn_end_datetime).append("\"")
                    .append(">");

            if(itm.itm_run_ind) {
                aeItemRelation ire = new aeItemRelation();
                ire.ire_child_itm_id = itm.itm_id;
                aeItem ireParentItm = ire.getParentInfo(con);
                if (ireParentItm != null) {
                    itm.parent_itm_id = ireParentItm.itm_id;

                    result.append("<parent id=\"").append(itm.parent_itm_id).append("\"")
                            .append(" title=\"").append(dbUtils.esc4XML(aeUtils.escNull(ireParentItm.itm_title))).append("\"/>");
                }
            }

            result.append("</item>");



        }
        /**
         * get the application count on each application process status of current approver
         */
        aeWorkFlowCache workFlow = new aeWorkFlowCache(con);
        Vector statusList = workFlow.getStatusList(appStatusList, rol_ext_id, tpl_id);
        Vector removeStatusVec = new Vector();
        if(show_status!=null) {
            if(show_status.equalsIgnoreCase(ENROLLMENT)) {
                for(int m=0;m<statusList.size();m++) {
                    String statusName = (String) statusList.elementAt(m);
                    if(!statusName.equalsIgnoreCase("ENROLLED")) {
                        removeStatusVec.addElement(new Integer(m));
                    }
                }
            } else if(show_status.equalsIgnoreCase(WITHDRAWAL)) {
                for(int m=0;m<statusList.size();m++) {
                    String statusName = (String) statusList.elementAt(m);
                    if(statusName.equalsIgnoreCase("ENROLLED")) {
                        removeStatusVec.addElement(new Integer(m));
                    }
                }
            }

            for(int m=removeStatusVec.size()-1;m>=0;m--) {
                Integer indexObj = (Integer) removeStatusVec.elementAt(m);
                statusList.removeElementAt(indexObj.intValue());
            }
        }
        /**
         * parse the status id to name
         */
        //System.out.println("process_status " + process_status);

        process_status = workFlow.getStatusById(appStatusList, rol_ext_id, tpl_id, process_status);
        //System.out.println("process_status " + process_status);

        // for process_status_cnt_list
//        if (!download) {
//            StringBuffer sqlStr = new StringBuffer(512);
//            sqlStr.append("SELECT app_process_status, count(*) AS cnt ")
//                  .append(" FROM aeApplication, aeItem, aeItemTemplate ")
//                  .append(" WHERE itm_owner_ent_id = ? ")
//                  .append(" AND itp_tpl_id = ? ")
//                  .append(" AND itm_id = itp_itm_id ")
//                  .append(" AND itm_id = app_itm_id ")
//                  .append(" AND app_process_status IN (''");
//            for (int i = 0; i < statusList.size(); i++) {
//                sqlStr.append(", ?");
//            }
//            sqlStr.append(") GROUP BY app_process_status ");
//System.out.println(sqlStr.toString());
//            PreparedStatement stmt = con.prepareStatement(sqlStr.toString());
//
//            int index = 1;
//            stmt.setLong(index++, owner_ent_id);
//            stmt.setInt(index++, tpl_id);
//            for (int i = 0; i < statusList.size(); i++) {
//                stmt.setString(index++, (String)statusList.elementAt(i));
//            }
//
//            ResultSet rs = stmt.executeQuery();
//            long total_cnt = 0;
//            long cnt;
//            result.append("<process_status_cnt_list>");
//            while(rs.next()) {
//                cnt = rs.getLong("cnt");
//                total_cnt += cnt;
//                result.append("<process_status_cnt name=\"").append(rs.getString("app_process_status")).append("\"");
//                result.append(" cnt=\"").append(cnt).append("\"/>");
//                result.append("<process_status name=\"").append((String)statusList.elementAt(i)).append("\"/>");
//            }
//            result.append("<total_process_status_cnt ");
//            result.append(" cnt=\"").append(total_cnt).append("\"/>");
//            result.append("</process_status_cnt_list>");
//        }
//        result.append("</process_status_cnt_list>");

        if (!download) {
            result.append("<process_status_list>");
            for (int i = 0; i < statusList.size(); i++) {
                result.append("<process_status name=\"").append((String)statusList.elementAt(i)).append("\"/>");
            }
            result.append("</process_status_list>");
        }

        String[] statusListArr = new String[0];
        statusListArr = (String[])statusList.toArray(statusListArr);
        Vector v_sql_param = new Vector();
        String targetGroupLrnSql = ViewRoleTargetGroup.getTargetGroupsLrnSQL(con, user_ent_id, rol_ext_id, false, v_sql_param);
        targetGroupLrnSql = "(" + targetGroupLrnSql + ")";
        // get count for each status
        String cntSql = SqlStatements.getApproverApplicationCnt(statusListArr, itm_id, targetGroupLrnSql);
//                System.out.println(cntSql);

        PreparedStatement stmt = con.prepareStatement(cntSql);
    //System.out.println(cntSql);
        int i = 1;
        stmt.setInt(i++, tpl_id);
        for (int idx = 0; idx < statusListArr.length; idx++) {
            stmt.setString(i++, statusListArr[idx]);
//            System.out.println("statusListArr[idx] " + statusListArr[idx]);
        }

        stmt.setString(i++, rol_ext_id);
        stmt.setLong(i++, user_ent_id);
        for (int k=0; k<v_sql_param.size(); k++){
            if (((String)v_sql_param.elementAt(k)).equals(ViewItemTargetGroup.SQL_PARAM_CUR_TIME)){
            	stmt.setBoolean(i++, true);
            }
        }

        ResultSet rs = stmt.executeQuery();

        long total_cnt = 0;
        long cnt;
        result.append("<process_status_cnt_list>");
        while(rs.next()) {
            cnt = rs.getLong("cnt");
            total_cnt += cnt;
            result.append("<process_status_cnt name=\"").append(rs.getString("app_process_status")).append("\"");
            result.append(" cnt=\"").append(cnt).append("\"/>");
            //result.append("<process_status name=\"").append((String)statusList.elementAt(i)).append("\"/>");
        }
        result.append("<total_process_status_cnt ");
        result.append(" cnt=\"").append(total_cnt).append("\"/>");
        result.append("</process_status_cnt_list>");
        rs.close();
        stmt.close();



        // construct sql
        String sqlStr = SqlStatements.getApproverApplication(statusListArr, process_status, itm_id);
        if (targetGroupLrnSql.length() == 0) {
            // not approver
            targetGroupLrnSql = "0";
        }
        sqlStr += targetGroupLrnSql ;

//System.out.println(sqlStr);

        stmt = con.prepareStatement(sqlStr);

        i = 1;
        /*
        System.out.println("tpl_id " + tpl_id);
          System.out.println("user_ent_id " + user_ent_id);
          System.out.println("rol_ext_id " + rol_ext_id);*/
        stmt.setInt(i++, tpl_id);
        if (process_status != null && process_status.length() > 0) {
            stmt.setString(i++, process_status);
        } else {
            for (int idx = 0; idx < statusListArr.length; idx++) {
                stmt.setString(i++, statusListArr[idx]);
//                System.out.println("statusListArr[idx] " + statusListArr[idx]);
            }
        }
        stmt.setString(i++, rol_ext_id);
        stmt.setLong(i++, user_ent_id);
        for (int k=0; k<v_sql_param.size(); k++){
            if (((String)v_sql_param.elementAt(k)).equals(ViewItemTargetGroup.SQL_PARAM_CUR_TIME)){
            	stmt.setBoolean(i++, true);
            }
        }


        rs = stmt.executeQuery();

        Vector v_usr_ent_id = new Vector();
        Vector v_app_id = new Vector();

        while (rs.next()) {
//            System.out.println("app_id " + rs.getLong("app_id"));
            v_usr_ent_id.addElement(new Long(rs.getLong("app_ent_id")));
            v_app_id.addElement(new Long(rs.getLong("app_id")));
        }
        /*
          //Tim todo: add filtering in here
        Hashtable hs = getApplicationIdByRolExtId(con, rol_ext_id);
        System.out.println("user_ent_id " + user_ent_id);
        while (rs.next()) {
            //System.out.println("app_id " + rs.getLong("app_id"));
            if(hs.containsKey(new Long(rs.getLong("app_id")))){
                 System.out.println("in HS :app_id " + rs.getLong("app_id"));
                if(((Long)hs.get(new Long(rs.getLong("app_id")))).equals(new Long(user_ent_id))){
                    v_usr_ent_id.addElement(new Long(rs.getLong("app_ent_id")));
                    v_app_id.addElement(new Long(rs.getLong("app_id")));
                }
            }
            else{
                v_usr_ent_id.addElement(new Long(rs.getLong("app_ent_id")));
                v_app_id.addElement(new Long(rs.getLong("app_id")));
            }
        }
        */
        if (v_usr_ent_id.size() == 0) {
            v_usr_ent_id.addElement(new Long(0));
        }
        if (v_app_id.size() == 0) {
            v_app_id.addElement(new Long(0));
        }
        String usr_ent_id_lst = cwUtils.vector2list(v_usr_ent_id);
        String app_id_lst = cwUtils.vector2list(v_app_id);

        // getApplicationByIdAsXML
        if(process_status != null && process_status.length() > 0 && process_status.startsWith("(") && process_status.endsWith(")")) {
            String[] procStatus = cwUtils.splitToString(process_status.substring(1, process_status.length()-1), ",");
            String temp = "";
            for(int m=0;m<procStatus.length; m++) {
                if(temp!="")
                    temp +=",";
                temp += procStatus[m].substring(1, procStatus[m].length()-1);
            }
            process_status = temp;
        }
        result.append(this.getApplicationByIdAsXML(con, app_id_lst, owner_ent_id, tpl_id,
                                                   process_status, page_size, page_num,
                                                   orderBy, sortOrder, download, itm_id));

        stmt.close();

        return result.toString();
    }

    public String getApproverNomAppnListAsXML(Connection con, long owner_ent_id,
                                           long user_ent_id, String rol_ext_id,
                                           String[] process_status_lst,
                                           int page_size, int page_num,
                                           String orderBy, String sortOrder,
                                           long itm_id)
            throws SQLException, cwException, cwSysMessage, IOException, qdbException {
        return getApproverNomAppnListAsXML(con, owner_ent_id, user_ent_id, rol_ext_id,
                                            process_status_lst, page_size, page_num,
                                            orderBy, sortOrder, itm_id, null);
    }

    public String getApproverNomAppnListAsXML(Connection con, long owner_ent_id,
                                           long user_ent_id, String rol_ext_id,
                                           String[] process_status_lst,
                                           int page_size, int page_num,
                                           String orderBy, String sortOrder,
                                           long itm_id, String show_status)
            throws SQLException, cwException, cwSysMessage, IOException, qdbException {
        StringBuffer result = new StringBuffer();
        cwPagination page = new cwPagination();
        page.sortCol = cwPagination.esc4SortSql(orderBy);
        page.sortOrder = cwPagination.esc4SortSql(sortOrder);
        
        
        page.pageSize = page_size;

        if (page_num == 0) {
            page.curPage = 1;
        } else {
            page.curPage = page_num;
        }

        // item information
        aeItem itm = new aeItem();
        itm.itm_id = itm_id;
        itm.getItem(con);

        result.append("<item id=\"").append(itm.itm_id).append("\"")
                .append(" title=\"").append(cwUtils.esc4XML(itm.itm_title)).append("\"")
                .append(" run_ind=\"").append(itm.itm_run_ind).append("\"")
                .append(" life_status=\"").append(cwUtils.esc4XML(itm.itm_life_status)).append("\"")
                .append(" appn_eff_start_datetime=\"").append(itm.itm_appn_start_datetime).append("\"")
                .append(" appn_eff_end_datetime=\"").append(itm.itm_appn_end_datetime).append("\"")
                .append(">");

        if(itm.itm_run_ind) {
            aeItemRelation ire = new aeItemRelation();
            ire.ire_child_itm_id = itm.itm_id;
            aeItem ireParentItm = ire.getParentInfo(con);
            if (ireParentItm != null) {
                itm.parent_itm_id = ireParentItm.itm_id;

                result.append("<parent id=\"").append(itm.parent_itm_id).append("\"")
                      .append(" title=\"").append(dbUtils.esc4XML(aeUtils.escNull(ireParentItm.itm_title))).append("\"/>");
            }
        }

        result.append("</item>");


        // construct sql
        Vector v_sql_param = new Vector();
        String targetGroupLrnSql = ViewRoleTargetGroup.getTargetGroupsLrnSQL(con, user_ent_id, rol_ext_id, false, v_sql_param);
        if (targetGroupLrnSql.length() == 0) {
            // not approver
            targetGroupLrnSql = "0";
        }
        targetGroupLrnSql = "(" + targetGroupLrnSql + ")";
        String processStatusLstSql = null;

        if (process_status_lst != null && process_status_lst.length > 0) {
            if(show_status!=null) {
                Vector revProcStatusLst = new Vector();
                if(show_status.equalsIgnoreCase(ENROLLMENT)) {
                    for(int m=0;m<process_status_lst.length;m++) {
                        if(process_status_lst[m].equalsIgnoreCase("ENROLLED")) {
                            revProcStatusLst.addElement(process_status_lst[m]);
                        }
                    }
                } else if(show_status.equalsIgnoreCase(WITHDRAWAL)) {
                    for(int m=0;m<process_status_lst.length;m++) {
                        if(!process_status_lst[m].equalsIgnoreCase("ENROLLED")) {
                            revProcStatusLst.addElement(process_status_lst[m]);
                        }
                    }
                }
                String[] aArray = new String[revProcStatusLst.size()];
                for(int m=0;m<revProcStatusLst.size();m++) {
                    aArray[m] = (String) revProcStatusLst.elementAt(m);
                }
                process_status_lst=aArray;
            }
        }

        if (process_status_lst != null && process_status_lst.length > 0) {
            StringBuffer processStatusLstBuf = new StringBuffer();

            processStatusLstBuf.append("(''");

            for (int i=0; i<process_status_lst.length; i++) {
                processStatusLstBuf.append(", '").append(process_status_lst[i]).append("'");
            }

            processStatusLstBuf.append(")");
            processStatusLstSql = processStatusLstBuf.toString();
        }

        String sqlStr = SqlStatements.getApproverNomApplication(processStatusLstSql, itm_id, targetGroupLrnSql, orderBy, sortOrder);
//        System.out.println(sqlStr);

        PreparedStatement stmt = con.prepareStatement(sqlStr);
        int index = 1;
        for (int k=0; k<v_sql_param.size(); k++){
            if (((String)v_sql_param.elementAt(k)).equals(ViewItemTargetGroup.SQL_PARAM_CUR_TIME)){
            	stmt.setBoolean(index++, true);
            }
        }
        stmt.setLong(index++, user_ent_id);
        stmt.setString(index++, rol_ext_id);

        ResultSet rs = stmt.executeQuery();
        result.append("<status_list>");

        for (int i=0; i<process_status_lst.length; i++) {
            result.append("<status>").append(process_status_lst[i]).append("</status>");
        }

        result.append("</status_list>");
        result.append("<application_list>");
        page.totalRec = 0;

        while (rs.next()) {
            if (page.totalRec>=(page.curPage-1)*page.pageSize && page.totalRec<page.curPage*page.pageSize) {
                dbRegUser usr = new dbRegUser();
                usr.usr_ent_id = rs.getLong("app_ent_id");
                usr.get(con);
                result.append("<application id=\"").append(rs.getLong("app_id")).append("\"");
                result.append(" process_status=\"").append(dbUtils.esc4XML(aeUtils.escNull(rs.getString("app_process_status")))).append("\"");
    //            appXML.append(" priority=\"").append(aeUtils.escNull(app.app_priority)).append("\"");
                result.append(" timestamp=\"").append(rs.getTimestamp("app_upd_timestamp")).append("\">");
                result.append(usr.getUserShortXML(con, false, true));
                result.append("<approver id=\"").append(rs.getLong("approver_ent_id")).append("\"");
                result.append(" display_bil=\"").append(dbUtils.esc4XML(aeUtils.escNull(rs.getString("approver_display_bil")))).append("\"/>");
                result.append("</application>");
            }

            page.totalRec++;
        }

        page.totalPage = (int)Math.ceil((float)page.totalRec/page.pageSize);

        result.append(page.asXML());
        result.append("</application_list>");
        stmt.close();

        return result.toString();
    }

    private String getApplicationByIdAsXML(Connection con, String appIdList,
                                           long owner_ent_id, int tpl_id, String processStatus,
                                           long pageSize, long pageNum,
                                           String orderBy, String sortOrder, boolean download, long itm_id)
            throws SQLException, cwException, cwSysMessage, IOException {

        StringBuffer xmlBuf = new StringBuffer(1024);
        StringBuffer appXML = new StringBuffer(1024);
        Timestamp cur_time = cwSQL.getTime(con);
        if(orderBy == null || orderBy.length() == 0) {
            orderBy = " usr_display_bil ";
        }
        if(sortOrder == null || sortOrder.length() == 0) {
            sortOrder = " ASC ";
        }
        //get application list as xml
        aeItem itm = new aeItem();
        String itm_content = null;
        Vector v_target_lrn = null;
        Vector v_comp_target_lrn = null;
        Vector v_app = this.getApplicationById(con, appIdList, pageSize, pageNum, orderBy, sortOrder);
        String[] tree_sub_type_lst = ViewEntityToTree.getTargetEntity(con, owner_ent_id);

        appXML.append("<application_list status=\"").append(aeUtils.escNull(processStatus)).append("\"");
        appXML.append(" tpl_id=\"").append(tpl_id);
        appXML.append("\" itm_id=\"").append(itm_id).append("\">");
        if (pageSize == 0) pageSize = 10;
        if (pageNum ==0) pageNum = 1;
        aeApplication app;
        dbRegUser usr = new dbRegUser();
//        Timestamp cur_time = cwSQL.getTime(con);
        try {
            Hashtable app_hash = null;

            if (pageSize*(pageNum-1) >= v_app.size()) {
                pageNum = v_app.size()/pageSize;

                if (v_app.size()%pageSize > 0) {
                    pageNum++;
                }
            }

            long start = pageSize * (pageNum-1);
            boolean isOrgHasNoShow = aeAttendanceStatus.isValidStatus(con, aeAttendanceStatus.STATUS_TYPE_NOSHOW, owner_ent_id);
            Hashtable h_itm = null;
            Hashtable h_v_target_lrn = null;
            Hashtable h_v_comp_target_lrn = null;
            Hashtable h_itm_content = null;
            // get item information for approver
            h_itm = new Hashtable();
            h_v_target_lrn = new Hashtable();
            h_v_comp_target_lrn = new Hashtable();
            h_itm_content = new Hashtable();

            for(int i=0; i<v_app.size(); i++) {
                if (download || (i >= start && i < start+pageSize)) {
                    app = (aeApplication) v_app.elementAt(i);
                    usr.usr_ent_id = app.app_ent_id;
                    usr.get(con);

        aeItem aItem = new aeItem();
        aItem.itm_id = app.app_itm_id;
        aItem.getItem(con);
        long check_itm_id = aItem.itm_id;
        if(aItem.itm_run_ind) {
            check_itm_id = aItem.parent_itm_id;
        }
        boolean target_lrn = aeItem.isTargetedLearner(con, usr.usr_ent_id, itm_id, false);
        boolean comp_target_lrn = false;//aeItem.isTargetedLearner(con, itm_id, usr.usr_ent_id, DbItemTargetEntity.ITE_COMPULSORY);
        aeItemRequirement itmr = new aeItemRequirement();
        boolean item_exemption = itmr.checkItemExemption(con, usr.usr_ent_id, check_itm_id);
        boolean user_exemption = itmr.checkUserExemption(con, usr.usr_ent_id, check_itm_id);
        boolean prerequisite = itmr.checkPrerequisite(con, usr.usr_ent_id, check_itm_id, aeItemRequirement.REQ_SUBTYPE_ENROLLMENT);

                    appXML.append("<application id=\"").append(app.app_id).append("\"");
                    appXML.append(" targeted_lrn_ind=\"").append(target_lrn).append("\"");
                    appXML.append(" comp_targeted_lrn_ind=\"").append(comp_target_lrn).append("\"");
                    appXML.append(" item_exemption_ind=\"").append(item_exemption).append("\"");
                    appXML.append(" user_exemption_ind=\"").append(user_exemption).append("\"");
                    appXML.append(" prerequisite_ind=\"").append(prerequisite).append("\"");

                    appXML.append(" process_status=\"").append(app.app_process_status).append("\"");
                    if(app.app_process_status.equalsIgnoreCase(ENROLLED)) {
                        appXML.append(" process_status_id=\"1\"");
                    } else if(app.app_process_status.equalsIgnoreCase(APPROVED)) {
                        appXML.append(" process_status_id=\"11\"");
                    } else if(app.app_process_status.equalsIgnoreCase(CONFIRMED)) {
                        appXML.append(" process_status_id=\"12\"");
                    }
                    appXML.append(" priority=\"").append(aeUtils.escNull(app.app_priority)).append("\"");
                    appXML.append(" timestamp=\"").append(app.app_upd_timestamp).append("\">");
                    appXML.append(usr.getUserShortXML(con, false, true));
                    // have noshow status
                    /*if (aeAttendanceStatus.isValidStatus(con, aeAttendanceStatus.STATUS_TYPE_NOSHOW, owner_ent_id)){*/
                    if (isOrgHasNoShow){
                        appXML.append("<no_show_count>").append(aeAttendance.getNoShowCount(con, app.app_ent_id)).append("</no_show_count>");
                    }
//                    if(itm_id != 0) {
//                        //Not approver
//                        appXML.append(app.appnLatestHistoryAsXML(con));
//                        appXML.append("<attended_before>");
//                        Vector vec_ent_app = (Vector)app_hash.get(new Long(app.app_ent_id));
//                        if (vec_ent_app == null || (vec_ent_app.size() == 1 && vec_ent_app.contains(new Long(app.app_id)))) {
//                            appXML.append("NO");
//                        } else {
//                            appXML.append("YES");
//                        }
//                        appXML.append("</attended_before>");
//                    }
                    //approver
                    //get aeItem Object, targeted learner and item content from Hashtables if they have been got before
                    Long key = new Long(app.app_itm_id);
                    itm = (aeItem)h_itm.get(key);
                    v_target_lrn = (Vector)h_v_target_lrn.get(key);
                    v_comp_target_lrn = (Vector)h_v_comp_target_lrn.get(key);
                    itm_content = (String)h_itm_content.get(key);
                    if(itm == null) {
                        // if objects not found in Hashtable, get them from DB and put back to Hashtables
                        itm = new aeItem();
                        v_target_lrn = new Vector();
                        v_comp_target_lrn = new Vector();
                        itm.itm_id = app.app_itm_id;
                        itm.getItem(con);
                        if (tree_sub_type_lst != null && tree_sub_type_lst.length > 0) {
                            v_target_lrn = itm.getTargetLrn(con, DbItemTargetRuleDetail.ITE_ELECTIVE);
                            v_comp_target_lrn = itm.getTargetLrn(con, DbItemTargetRuleDetail.ITE_COMPULSORY);
                        }
                        itm_content = itm.contentAsXML(con, cur_time);
                        h_itm.put(key, itm);
                        h_v_target_lrn.put(key, v_target_lrn);
                        h_v_comp_target_lrn.put(key, v_comp_target_lrn);
                        h_itm_content.put(key, itm_content);
                    }
                    if (tree_sub_type_lst != null && tree_sub_type_lst.length > 0) {
                        /*
                        System.out.println("getting the target Info itm_id:" + app.app_itm_id);
                        for(int k =0; k < v_target_lrn.size(); k++){
                            System.out.println("user ent id:" + (Long) v_target_lrn.elementAt(k));
                        }*/
                        appXML.append("<targeted_lrn_ind>").append(v_target_lrn.contains(new Long(app.app_ent_id))).append("</targeted_lrn_ind>");
                        appXML.append("<comp_targeted_lrn_ind>").append(v_comp_target_lrn.contains(new Long(app.app_ent_id))).append("</comp_targeted_lrn_ind>");
                    }
                    appXML.append(itm_content);
                    appXML.append("</application>");
                }
            }
        } catch(qdbException e) {
            throw new cwException(e.getMessage());
        }
        appXML.append("</application_list>");
        if(!download) {
            //format XML
            xmlBuf.append("<pagination total_rec=\"").append(v_app.size()).append("\"");
            xmlBuf.append(" page_size=\"").append(pageSize).append("\"");
            xmlBuf.append(" cur_page=\"").append(pageNum).append("\"");
            xmlBuf.append(" sort_col=\"").append(orderBy).append("\"");
            xmlBuf.append(" sort_order=\"").append(sortOrder).append("\"/>");
            //get workflow template
            aeTemplate tpl = new aeTemplate();
            if(itm_id != 0) {
                aeItem tempitm = new aeItem();
                tempitm.itm_id = itm_id;
                tempitm.get(con);
                tpl.tpl_xml = tempitm.getRawTemplate(con, aeTemplate.WORKFLOW);
            }
            else if(tpl_id > 0) {
                tpl.tpl_id = tpl_id;
                tpl.getRawTemplate(con);
            } else {
                tpl.tpl_xml = aeTemplate.getFirstRawTpl(con, aeTemplate.WORKFLOW, owner_ent_id, "ASC");
                /*
                tpl.tpl_id = aeTemplate.getFirstTplId(con, aeTemplate.WORKFLOW, owner_ent_id, "desc");
                tpl.get(con);
                */
            }
            xmlBuf.append(tpl.tpl_xml);
        }
        xmlBuf.append(aeItem.getAllItemTypeTitleInOrg(con, owner_ent_id));
        xmlBuf.append(appXML.toString());
        return xmlBuf.toString();
    }

    private Vector getApplicationById(Connection con, String appIdList,
                                      long page_size, long page_num,
                                      String orderBy, String sortOrder)
            throws SQLException, cwException, cwSysMessage, IOException {
        Timestamp max_timestamp = Timestamp.valueOf(dbUtils.MAX_TIMESTAMP);
        String sql_null_string = cwSQL.get_null_sql(cwSQL.COL_TYPE_STRING);
        if (orderBy == null || orderBy.length() == 0) {
            orderBy = " r_itm_appn_end_datetime";
        }
        if (sortOrder == null || sortOrder.length() == 0) {
            sortOrder = " ASC ";
        }

        StringBuffer sqlStr = new StringBuffer(SqlStatements.getApplicationList(con, appIdList, null, 0, null));
        sqlStr.append(" ORDER BY ").append(orderBy).append(" ").append(sortOrder);
        if (!orderBy.equalsIgnoreCase("r_itm_appn_end_datetime")) {
            sqlStr.append(", r_itm_appn_end_datetime asc ");
        }
        if (!orderBy.equalsIgnoreCase("r_itm_title")) {
            sqlStr.append(", r_itm_title ASC ");
        }
        if (!orderBy.equalsIgnoreCase("p_itm_title")) {
            sqlStr.append(", p_itm_title ASC ");
        }

        PreparedStatement stmt = con.prepareStatement(sqlStr.toString());

        int index = 1;
        stmt.setTimestamp(index++, max_timestamp);
        stmt.setTimestamp(index++, max_timestamp);
        stmt.setTimestamp(index++, max_timestamp);
        stmt.setTimestamp(index++, max_timestamp);
        stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
        stmt.setBoolean(index++, true);
        stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
        stmt.setBoolean(index++, true);
        stmt.setBoolean(index++, true);
        stmt.setTimestamp(index++, max_timestamp);
        stmt.setTimestamp(index++, max_timestamp);
        stmt.setTimestamp(index++, max_timestamp);
        stmt.setTimestamp(index++, max_timestamp);
        stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
        stmt.setBoolean(index++, true);
        stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
        stmt.setBoolean(index++, true);
        stmt.setBoolean(index++, false);
        ResultSet rs = stmt.executeQuery();

        Vector result = new Vector();
        while(rs.next()) {
            aeApplication app = new aeApplication();
            app.app_id = rs.getLong("app_id");
            app.app_ent_id = rs.getLong("app_ent_id");
            app.app_itm_id = rs.getLong("app_itm_id");
            app.app_status = rs.getString("app_status");
            app.app_process_status = rs.getString("app_process_status");
            app.app_create_timestamp = rs.getTimestamp("app_create_timestamp");
            app.app_create_usr_id = rs.getString("app_create_usr_id");
            app.app_upd_timestamp = rs.getTimestamp("app_upd_timestamp");
            app.app_upd_usr_id = rs.getString("app_upd_usr_id");
            app.app_ext1 = rs.getString("app_ext1");
            app.app_ext2 = rs.getLong("app_ext2");
            app.app_ext3 = rs.getString("app_ext3");
            app.app_notify_status = rs.getInt("app_notify_status");
            app.app_notify_datetime = rs.getTimestamp("app_notify_datetime");
            app.app_priority = rs.getString("app_priority");
            result.addElement(app);
        }
        stmt.close();

        return result;
    }

   public Hashtable getApplicationIdByRolExtId(Connection con, String rol_ext_id)
        throws SQLException, cwException
    {
        StringBuffer SQL = new StringBuffer();
        Hashtable result = new Hashtable();


        SQL.append("SELECT ate_app_id,ate_usr_ent_id from aeAppnTargetEntity WHERE ate_rol_ext_id = ? ");

        PreparedStatement stmt = con.prepareStatement(SQL.toString());
        stmt.setString(1, rol_ext_id);
        ResultSet rs = stmt.executeQuery();


        while (rs.next()) {
            result.put(new Long(rs.getLong("ate_app_id")), new Long(rs.getLong("ate_usr_ent_id")));
            //addElement(new Long(rs.getLong("ate_app_id")));
        }

        stmt.close();
        return result;
    }

  public String getApproverAppnCourseListAsXML(Connection con, long owner_ent_id,
                                           long user_ent_id, String rol_ext_id,
                                           String process_status, int tpl_id,
                                           Hashtable appStatusList,
                                           cwPagination cwPage,HttpSession sess,
                                           boolean download)
            throws SQLException, cwException, cwSysMessage, IOException {
        return getApproverAppnCourseListAsXML(con, owner_ent_id, user_ent_id, rol_ext_id,
                                    process_status, tpl_id, appStatusList, cwPage, sess,
                                    download, null);
    }

  public String getApproverAppnCourseListAsXML(Connection con, long owner_ent_id,
                                           long user_ent_id, String rol_ext_id,
                                           String process_status, int tpl_id,
                                           Hashtable appStatusList,
                                           cwPagination cwPage,HttpSession sess,
                                           boolean download, String show_status)
            throws SQLException, cwException, cwSysMessage, IOException {
        StringBuffer result = new StringBuffer();

        Vector tplVec = aeTemplate.getTemplateByTtpTtle(con, "WORKFLOW");
        Vector v_itm_id = new Vector();
        Hashtable h_tpl_id = new Hashtable();
        for(int j=0; j < tplVec.size()  ; j = j + 2){
            //System.out.println("tpl_id " + tplVec.elementAt(j));
            //System.out.println("ttp_id " + tplVec.elementAt(j+1));

            /**
            * get the application count on each application process status of current approver
            */
            aeWorkFlowCache workFlow = new aeWorkFlowCache(con);
            Vector statusList = workFlow.getStatusList(appStatusList, rol_ext_id, ((Long) tplVec.elementAt(j)).intValue());
            Vector removeStatusVec = new Vector();
            if(show_status!=null) {
                if(show_status.equalsIgnoreCase(ENROLLMENT)) {
                    for(int m=0;m<statusList.size();m++) {
                        String statusName = (String) statusList.elementAt(m);
                        if(!statusName.equalsIgnoreCase("ENROLLED")) {
                            removeStatusVec.addElement(new Integer(m));
                        }
                    }
                } else if(show_status.equalsIgnoreCase(WITHDRAWAL)) {
                    for(int m=0;m<statusList.size();m++) {
                        String statusName = (String) statusList.elementAt(m);
                        if(statusName.equalsIgnoreCase("ENROLLED")) {
                            removeStatusVec.addElement(new Integer(m));
                        }
                    }
                }

                for(int m=removeStatusVec.size()-1;m>=0;m--) {
                    Integer indexObj = (Integer) removeStatusVec.elementAt(m);
                    statusList.removeElementAt(indexObj.intValue());
                }
            }
            /**
            * parse the status id to name
            */
            process_status = workFlow.getStatusById(appStatusList, rol_ext_id, tpl_id, process_status);


            //get approver's servant
            String[] statusListArr = new String[0];
            statusListArr = (String[])statusList.toArray(statusListArr);
            // construct sql
            Vector v_sql_param = new Vector();
            String targetGroupLrnSql = ViewRoleTargetGroup.getTargetGroupsLrnSQL(con, user_ent_id, rol_ext_id, false, v_sql_param);
            //String sqlStr = SqlStatements.getApproverApplication(statusListArr, process_status, itm_id);

            if (targetGroupLrnSql.length() == 0) {
                // not approver
                targetGroupLrnSql = "0";
            }
            //sqlStr += "(" + targetGroupLrnSql + ")";
            String sqlStr = SqlStatements.getApproverApplicationCourse(statusListArr, "(" + targetGroupLrnSql + ")");
//  System.out.println(sqlStr);

            PreparedStatement stmt = con.prepareStatement(sqlStr);
            int i = 1;
            stmt.setLong(i++, user_ent_id);
            stmt.setString(i++, rol_ext_id);

            stmt.setLong(i++, ((Long) tplVec.elementAt(j)).longValue());
            stmt.setLong(i++, ((Long) tplVec.elementAt(j+1)).longValue());
            for (int idx = 0; idx < statusListArr.length; idx++) {
                stmt.setString(i++, statusListArr[idx]);
            }
            for (int k=0; k<v_sql_param.size(); k++){
                if (((String)v_sql_param.elementAt(k)).equals(ViewItemTargetGroup.SQL_PARAM_CUR_TIME)){
                    stmt.setBoolean(i++, true);
                }
            }

            stmt.setString(i++, rol_ext_id);
            stmt.setLong(i++, ((Long) tplVec.elementAt(j)).longValue());
            stmt.setLong(i++, ((Long) tplVec.elementAt(j+1)).longValue());
            for (int idx = 0; idx < statusListArr.length; idx++) {
                stmt.setString(i++, statusListArr[idx]);
            }
            for (int k=0; k<v_sql_param.size(); k++){
                if (((String)v_sql_param.elementAt(k)).equals(ViewItemTargetGroup.SQL_PARAM_CUR_TIME)){
                	stmt.setBoolean(i++, true);
                }
            }
            ResultSet rs = stmt.executeQuery();
//            System.out.println("user_ent_id " + user_ent_id);
            while (rs.next()) {
                //System.out.println("itm_id " + rs.getLong("itm_id"));
                //System.out.println("itm_title " + rs.getString("itm_title"));
                v_itm_id.addElement(new Long(rs.getLong("itm_id")));
                h_tpl_id.put(new Long(rs.getLong("itm_id")), (Long) tplVec.elementAt(j));

            }

            rs.close();
            stmt.close();
        }
        if (v_itm_id.size() == 0) {
            v_itm_id.addElement(new Long(0));
        }
        String itm_id_lst = cwUtils.vector2list(v_itm_id);

        result.append(aeItem.getApproverApplicaitonItmLst(con, cwPage ,itm_id_lst, sess, h_tpl_id));


        return result.toString();
    }

    private static final String sql_get_app_id_by_target =
        "SELECT app_id From aeApplication "
        + " WHERE app_itm_id = ?  "
        + "AND app_id NOT IN (select ate_app_id from aeAppnTargetEntity where ate_rol_ext_id = ? and ate_usr_ent_id != ?) "
        + "AND app_ent_id IN  ";

    public static Vector getApproverInChargeAppId(Connection con, long itm_id, long usr_ent_id, String rol_ext_id)  throws SQLException
    {
        Vector appVec = new Vector();
        Vector v_sql_param = new Vector();
        String targetGroupLrnSql = ViewRoleTargetGroup.getTargetGroupsLrnSQL(con, usr_ent_id, rol_ext_id, false, v_sql_param);
        if (targetGroupLrnSql.length() == 0) {
            targetGroupLrnSql = "0";
        }
        String sql = sql_get_app_id_by_target + "(" + targetGroupLrnSql + ")";
        PreparedStatement stmt = con.prepareStatement(sql);
        int index = 1;
        stmt.setLong(index++, itm_id);
        stmt.setString(index++, rol_ext_id);
        stmt.setLong(index++, usr_ent_id);
        for (int k=0; k<v_sql_param.size(); k++){
            if (((String)v_sql_param.elementAt(k)).equals(ViewItemTargetGroup.SQL_PARAM_CUR_TIME)){
            	stmt.setBoolean(index++, true);
            }
        }
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            appVec.addElement(new Long(rs.getLong("app_id")));
        }
        stmt.close();
        return appVec;
    }

    public String getMultiApplicationForm(Connection con, HttpSession sess, long itm_id,
                                     long[] ent_id_lst, Hashtable entIdRole, String app_xml)
        throws qdbException, IOException, SQLException, cwSysMessage, cwException
    {

        // put app_xml and approver_lst to session
         //System.out.println("app_xml: " + app_xml);
         //            System.out.println("entIdRole: " + entIdRole);


        if(app_xml !=null)
            sess.setAttribute(aeQueueManager.APP_XML, app_xml);
        if(entIdRole !=null)
            sess.setAttribute(aeQueueManager.USR_APPROVER_LST, entIdRole);

        StringBuffer result = new StringBuffer();

        // item information
        aeItem itm = new aeItem();
        itm.itm_id = itm_id;
        itm.getItem(con);

        result.append("<item id=\"").append(itm.itm_id).append("\"")
                .append(" title=\"").append(cwUtils.esc4XML(itm.itm_title)).append("\"")
                .append(" run_ind=\"").append(itm.itm_run_ind).append("\"")
                .append(" life_status=\"").append(cwUtils.esc4XML(itm.itm_life_status)).append("\"")
                .append(" appn_eff_start_datetime=\"").append(itm.itm_appn_start_datetime).append("\"")
                .append(" appn_eff_end_datetime=\"").append(itm.itm_appn_end_datetime).append("\"")
                .append(">");

        if(itm.itm_run_ind) {
            aeItemRelation ire = new aeItemRelation();
            ire.ire_child_itm_id = itm.itm_id;
            aeItem ireParentItm = ire.getParentInfo(con);
            if (ireParentItm != null) {
                itm.parent_itm_id = ireParentItm.itm_id;

                result.append("<parent id=\"").append(itm.parent_itm_id).append("\"")
                      .append(" title=\"").append(dbUtils.esc4XML(aeUtils.escNull(ireParentItm.itm_title))).append("\"/>");
            }
        }

        result.append("</item>");

        result.append("<application_info itm_id=\"").append(itm_id).append("\"");
       // result.append(" app_xml=\"").append(dbUtils.esc4XML(app_xml)).append("\"");
        //result.append(" usr_ent_n_rol_id_lst=\"").append(dbUtils.esc4XML(usr_ent_n_rol_id_lst)).append("\"");
        result.append("/>");
        StringBuffer qualifyStr = new StringBuffer();
        StringBuffer notQualifyStr = new StringBuffer();
        long check_itm_id = itm_id;
        if(itm.itm_run_ind) {
             check_itm_id = itm.parent_itm_id;
        }
        for(int i=0; i < ent_id_lst.length; i++){
            boolean target_lrn = aeItem.isTargetedLearner(con, ent_id_lst[i], itm_id,  false);
            boolean comp_target_lrn = false;//aeItem.isTargetedLearner(con, itm_id, ent_id_lst[i], DbItemTargetEntity.ITE_COMPULSORY);
            aeItemRequirement itmr = new aeItemRequirement();
            boolean item_exemption = itmr.checkItemExemption(con, ent_id_lst[i], check_itm_id);
            boolean user_exemption = itmr.checkUserExemption(con, ent_id_lst[i], check_itm_id);
            boolean prerequisite = itmr.checkPrerequisite(con, ent_id_lst[i], check_itm_id, aeItemRequirement.REQ_SUBTYPE_ENROLLMENT);
            dbRegUser usr = new dbRegUser();
            usr.usr_ent_id = ent_id_lst[i];
            usr.get(con);

//            if((target_lrn || comp_target_lrn) && prerequisite && !item_exemption && !user_exemption){ // need to enhance for prequistes and Not taken before
            if((comp_target_lrn && prerequisite && !item_exemption && !user_exemption) ||
               (target_lrn && prerequisite && !item_exemption && user_exemption) ||
               (target_lrn && prerequisite && !item_exemption && !user_exemption)) {
                 qualifyStr.append("<application targeted_lrn_ind=\"").append(target_lrn).append("\"");
                 qualifyStr.append(" comp_targeted_lrn_ind=\"").append(comp_target_lrn).append("\"");
                 qualifyStr.append(" item_exemption_ind=\"").append(item_exemption).append("\"");
                 qualifyStr.append(" user_exemption_ind=\"").append(user_exemption).append("\"");
                 qualifyStr.append(" prerequisite_ind=\"").append(prerequisite).append("\"");
                 qualifyStr.append(">");
                 qualifyStr.append(usr.getUserShortXML(con, false, true));
                 qualifyStr.append("</application>");
            }else{
                 notQualifyStr.append("<application targeted_lrn_ind=\"").append(target_lrn).append("\"");
                 notQualifyStr.append(" comp_targeted_lrn_ind=\"").append(comp_target_lrn).append("\"");
                 notQualifyStr.append(" item_exemption_ind=\"").append(item_exemption).append("\"");
                 notQualifyStr.append(" user_exemption_ind=\"").append(user_exemption).append("\"");
                 notQualifyStr.append(" prerequisite_ind=\"").append(prerequisite).append("\"");
                 notQualifyStr.append(">");
                 notQualifyStr.append(usr.getUserShortXML(con, false, true));
                 notQualifyStr.append("</application>");
            }

        }
        result.append("<application_list type=\"").append("qualify").append("\">");
        result.append(qualifyStr);
        result.append("</application_list>");
        result.append("<application_list type=\"").append("not_qualify").append("\">");
        result.append(notQualifyStr);
        result.append("</application_list>");



        return result.toString();
    }

    public static String[] getAllProcessStatus(Connection con, long itm_id) throws SQLException, cwException, IOException, qdbException{
        aeItem item = new aeItem();
        aeTemplate tpl = new aeTemplate();
        aeWorkFlow workFlow = new aeWorkFlow(dbUtils.xmlHeader);

        item.itm_id = itm_id;
        tpl.tpl_id = item.getTemplateId(con, aeTemplate.WORKFLOW);
        tpl.get(con);

        return workFlow.getAllStatus(tpl.tpl_xml);
    }
    // get process status from workflow with sys status
    public static String[] getProcessStatus(Connection con, long itm_id, String app_status) throws SQLException, cwException, IOException, qdbException{
        aeItem item = new aeItem();
        aeTemplate tpl = new aeTemplate();
        aeWorkFlow workFlow = new aeWorkFlow(dbUtils.xmlHeader);

        item.itm_id = itm_id;
        tpl.tpl_id = item.getTemplateId(con, aeTemplate.WORKFLOW);
        tpl.get(con);

        return workFlow.getProcessStatusBySysStatus(tpl.tpl_xml, app_status);
    }

    public static void updUpdateTimestamp(Connection con, long app_id, String update_usr_id, Timestamp upd_timestamp) throws SQLException{
        String sql = "UPDATE aeAppnActnHistory SET aah_upd_timestamp = ? , aah_upd_usr_id = ? WHERE aah_app_id = ? ";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setTimestamp(1, upd_timestamp);
        stmt.setString(2, update_usr_id);
        stmt.setLong(3, app_id);
        stmt.executeUpdate();
        stmt.close();
    }

    /**
    End the application's approval process
    @param con Connection to database
    @param app_id application id
    @param aah_id application action id
    @param action action taken
    @parma prof loginProfile of the action taker
    */
    public void endApproval(Connection con, long app_id, long aah_id, String action, loginProfile prof)
        throws SQLException, cwException {

        Timestamp curTime = cwSQL.getTime(con);
        //get the action taker's approval role
        DbAppnApprovalList thisApproval = new DbAppnApprovalList();
        thisApproval.aal_app_id = app_id;
        thisApproval.aal_usr_ent_id = prof.usr_ent_id;
        String approval_role = thisApproval.getCurrentApprovalRole(con);
        if(approval_role == null) {
            AccessControlWZB acl = new AccessControlWZB();
            Hashtable hRole = acl.getAllRoleUid(con, prof.root_ent_id, "rol_ext_id");
            approval_role = (String)hRole.get(prof.current_role);
        }
        //end the approval process
        DbAppnApprovalList.makeAction(con, app_id, aah_id, prof.usr_ent_id
                                        ,approval_role
                                        ,action
                                        ,curTime);
    }

    /**
    Route the application to the next step approvers.
    @param con Connection to database
    @param app_id id of the application
    @param aah_id id of the application action
    @param app_itm_id id of the item
    @param app_ent_id id of the applicant
    @param prof loginProfile of the action taker
    @return aeWorkFlowEvent.FUNC_RETURN_OK if the application is routed successfully
            aeWorkFlowEvent.FUNC_RETURN_NO_SUPERVISOR if the application has no next available supervisor and the application is routed to Training Adimistrators
            aeWorkFlowEvent.FUNC_RETURN_END_OF_APPROVAL if the application finishs its approval process
    */
    public String toNextApprovers(Connection con, long app_id, long aah_id, long app_itm_id, long app_ent_id, loginProfile prof) throws SQLException, cwSysMessage, qdbException, cwException {

        String result = null;

        //check if the Item needs approval
        aeItem itm = new aeItem();
        itm.itm_id = app_itm_id;
        itm.get(con);
        if(itm.itm_app_approval_type == null) {
            //the item does not require approval
            result = aeWorkFlowEvent.FUNC_RETURN_END_OF_APPROVAL;
        } else {
        
            //get current step's approver type
            String approverType = DbAppnApprovalList.getCurrentStepApproverType(con, app_id);

            Timestamp curTime = cwSQL.getTime(con);
            CommonLog.debug("approverType = " + approverType);
            CommonLog.debug("itm_app_approval_type = " + itm.itm_app_approval_type);
            int routeDS;
            int routeGS;
            int routeTADM;
            if(approverType == null) {
                // 是否是上司提名，上司提名的报名记录，要跳过上司审批
                boolean isNorminate = false;
                if (prof != null && prof.isLrnRole && prof.usr_ent_id != app_ent_id) {
                    isNorminate = true;
                }
                if(prof.isCancelEnrol)
                	isNorminate = false;
                //this is the 1st step supervisor
                if(itm.itm_app_approval_type.equals(aeItem.APP_APPROVAL_TYPE_DS)
                    || itm.itm_app_approval_type.equals(aeItem.APP_APPROVAL_TYPE_DS_GS)
                    || itm.itm_app_approval_type.equals(aeItem.APP_APPROVAL_TYPE_DS_TADM)
                    || itm.itm_app_approval_type.equals(aeItem.APP_APPROVAL_TYPE_DS_GS_TADM)) {
                    //the 1st step supervisor is Direct Supervisor
                	CommonLog.debug("Try DS");
                    routeDS = isNorminate ? APPROVER_SKIPPED : route2DS(con, prof.root_ent_id, app_id, app_ent_id, curTime);
                    if (routeDS == APPROVER_FOUND){
                        // routed to DS successuly
                        result = aeWorkFlowEvent.FUNC_RETURN_OK;
                    }else {
                        // routeDS == APPROVER_SKIPPED || routeDS == NO_APPROVER
                        if (itm.itm_app_approval_type.equals(aeItem.APP_APPROVAL_TYPE_DS)){
                            if (routeDS == APPROVER_SKIPPED){
                                // only need DS and DS skipped, end
                                result = aeWorkFlowEvent.FUNC_RETURN_END_OF_APPROVAL;
                            }else{
                               // no DS, route to TADM instead
                                routeTADM = route2TADM(con, prof.root_ent_id, app_id, app_ent_id, curTime, app_itm_id);
                                if (routeTADM == APPROVER_FOUND){
                                    // route to TADM successfully
                                    result = aeWorkFlowEvent.FUNC_RETURN_NO_SUPERVISOR;
                                }else{
                                    // TADM skipped, end
                                    result = aeWorkFlowEvent.FUNC_RETURN_END_OF_APPROVAL;
                                }
                            }
                        }else if (itm.itm_app_approval_type.equals(aeItem.APP_APPROVAL_TYPE_DS_GS)
                                || itm.itm_app_approval_type.equals(aeItem.APP_APPROVAL_TYPE_DS_GS_TADM)){
                            // DS skipped or no DS, route to next approver (GS)
                            SuperviseTargetEntityMapper superviseTargetEntityMapper = (SuperviseTargetEntityMapper) WzbApplicationContext.getBean
                                    ("superviseTargetEntityMapper");
                            boolean isGroupSuperviser = superviseTargetEntityMapper.isUserGroupSuperviser(app_ent_id, prof
                                    .usr_ent_id);
                            if(!isGroupSuperviser) {
                                isNorminate = false;
                            }
                            routeGS = isNorminate ? APPROVER_SKIPPED : route2GS(con, prof.root_ent_id, app_id, app_ent_id, curTime, 0);
                            if (routeGS == APPROVER_FOUND){
                                // routed to GS successfully
                                result = aeWorkFlowEvent.FUNC_RETURN_OK;
                            }else {
                                // (routeGS == APPROVER_SKIPPED || routeGS == NO_APPROVER )
                                if (itm.itm_app_approval_type.equals(aeItem.APP_APPROVAL_TYPE_DS_GS)){
                                    if (routeDS == APPROVER_SKIPPED || routeGS == APPROVER_SKIPPED ){
                                        // EITHER ONE SKIPPED , end
                                        result = aeWorkFlowEvent.FUNC_RETURN_END_OF_APPROVAL;
                                    }else{
                                        // no DS & no GS, route to TADM instead
                                        routeTADM = route2TADM(con, prof.root_ent_id, app_id, app_ent_id, curTime, app_itm_id);
                                        if (routeTADM == APPROVER_FOUND){
                                            // route to TADM successfully
                                            result = aeWorkFlowEvent.FUNC_RETURN_NO_SUPERVISOR;
                                        }else{
                                            // TADM skipped, end
                                            result = aeWorkFlowEvent.FUNC_RETURN_END_OF_APPROVAL;
                                        }
                                    }
                                }else{
                                    // DS & GS skipped / no found, route to next approver (TADM)
                                    routeTADM = route2TADM(con, prof.root_ent_id, app_id, app_ent_id, curTime, app_itm_id);
                                    if (routeTADM == APPROVER_FOUND){
                                        //route to TADM successfully
                                        result = aeWorkFlowEvent.FUNC_RETURN_OK;
                                    }else{
                                        //TADM skipped, end
                                        result = aeWorkFlowEvent.FUNC_RETURN_END_OF_APPROVAL;
                                    }
                                }
                            }
                        }else{
                            // APP_APPROVAL_TYPE_DS_TADM
                            routeTADM = route2TADM(con, prof.root_ent_id, app_id, app_ent_id, curTime, app_itm_id);
                            if (routeTADM == APPROVER_FOUND){
                                result = aeWorkFlowEvent.FUNC_RETURN_OK;
                            }else{
                                result = aeWorkFlowEvent.FUNC_RETURN_END_OF_APPROVAL;
                            }
                        }
                    }
                } else if(itm.itm_app_approval_type.equals(aeItem.APP_APPROVAL_TYPE_TADM)) {
                    //the 1st approver is TADM
                    routeTADM = route2TADM(con, prof.root_ent_id, app_id, app_ent_id, curTime, app_itm_id);
                    if (routeTADM == APPROVER_FOUND){
                        result = aeWorkFlowEvent.FUNC_RETURN_OK;
                    }else{
                        result = aeWorkFlowEvent.FUNC_RETURN_END_OF_APPROVAL;
                    }
                }
            } else if(approverType.equals(DbAppnApprovalList.ROLE_DIRECT_SUPERVISE)) {
                //current approver is DS

                //get action taker's approval role type
                //and make the pending approval records to history records
                DbAppnApprovalList thisApproval = new DbAppnApprovalList();
                thisApproval.aal_app_id = app_id;
                thisApproval.aal_usr_ent_id = prof.usr_ent_id;
                String approval_role = (thisApproval.getCurrentApprovalRole(con) == null) ? DbAppnApprovalList.ROLE_TADM : thisApproval.aal_approval_role;
                DbAppnApprovalList.makeAction(con, app_id, aah_id, prof.usr_ent_id
                                                ,approval_role
                                                ,DbAppnApprovalList.ACTION_APPROVED
                                                ,curTime);

                if(itm.itm_app_approval_type.equals(aeItem.APP_APPROVAL_TYPE_DS)) {
                    //only DS approval is needed.
                    //so this is the end of approval process
                    result = aeWorkFlowEvent.FUNC_RETURN_END_OF_APPROVAL;
                } else if(itm.itm_app_approval_type.equals(aeItem.APP_APPROVAL_TYPE_DS_GS)
                            || itm.itm_app_approval_type.equals(aeItem.APP_APPROVAL_TYPE_DS_GS_TADM)) {
                    //the next approver is the group approver
                    routeGS =  route2GS(con, prof.root_ent_id, app_id, app_ent_id, curTime, 0);
                    if (routeGS == APPROVER_FOUND){
                        // routed to GS successfully
                        result = aeWorkFlowEvent.FUNC_RETURN_OK;
                    }else {
                        // (routeGS == APPROVER_SKIPPED || routeGS == NO_APPROVER )
                        if (itm.itm_app_approval_type.equals(aeItem.APP_APPROVAL_TYPE_DS_GS)){
                            result = aeWorkFlowEvent.FUNC_RETURN_END_OF_APPROVAL;
                        }else{
                            // APP_APPROVAL_TYPE_DS_GS_TADM
                            // GS skipped, route to next approver (TADM)
                            routeTADM = route2TADM(con, prof.root_ent_id, app_id, app_ent_id, curTime, app_itm_id);
                            if (routeTADM == APPROVER_FOUND){
                                //route to TADM successfully
                                result = aeWorkFlowEvent.FUNC_RETURN_OK;
                            }else{
                                result = aeWorkFlowEvent.FUNC_RETURN_END_OF_APPROVAL;
                            }
                        }
                    }
                } else if(itm.itm_app_approval_type.equals(aeItem.APP_APPROVAL_TYPE_DS_TADM)) {
                    //the next approver is TADM
                    routeTADM = route2TADM(con, prof.root_ent_id, app_id, app_ent_id, curTime, app_itm_id);
                    if (routeTADM == APPROVER_FOUND){
                        //route to TADM successfully
                        result = aeWorkFlowEvent.FUNC_RETURN_OK;
                    }else{
                        result = aeWorkFlowEvent.FUNC_RETURN_END_OF_APPROVAL;
                    }
                }

            } else if(approverType.equals(DbAppnApprovalList.ROLE_SUPERVISE)) {
                //current approver is GS

                //get the current group supervisor's approval group entity id
                long currentSupervisedGroup = DbAppnApprovalList.getCurrentSupervisedGroupEntId(con, app_id);

                //get action taker's approval role type
                //and make the pending approval records to history records
                DbAppnApprovalList thisApproval = new DbAppnApprovalList();
                thisApproval.aal_app_id = app_id;
                thisApproval.aal_usr_ent_id = prof.usr_ent_id;
                String approval_role = (thisApproval.getCurrentApprovalRole(con) == null) ? DbAppnApprovalList.ROLE_TADM : thisApproval.aal_approval_role;
                DbAppnApprovalList.makeAction(con, app_id, aah_id, prof.usr_ent_id
                                                ,approval_role
                                                ,DbAppnApprovalList.ACTION_APPROVED
                                                ,curTime);

                //route to the next group approver
                routeGS = route2GS(con, prof.root_ent_id, app_id, app_ent_id, curTime, currentSupervisedGroup);
                if (routeGS == APPROVER_FOUND){
                    // routed to GS successfully
                    result = aeWorkFlowEvent.FUNC_RETURN_OK;
                }else {
                    // (routeGS == APPROVER_SKIPPED || routeGS == NO_APPROVER )
                    if (itm.itm_app_approval_type.equals(aeItem.APP_APPROVAL_TYPE_DS_GS)){
                        result = aeWorkFlowEvent.FUNC_RETURN_END_OF_APPROVAL;
                    }else{
                        // APP_APPROVAL_TYPE_DS_GS_TADM
                        // GS skipped, route to next approver (TADM)
                        routeTADM = route2TADM(con, prof.root_ent_id, app_id, app_ent_id, curTime, app_itm_id);
                        if (routeTADM == APPROVER_FOUND){
                            //route to TADM successfully
                            result = aeWorkFlowEvent.FUNC_RETURN_OK;
                        }else{
                            result = aeWorkFlowEvent.FUNC_RETURN_END_OF_APPROVAL;
                        }
                    }
                }
            } else if(approverType.equals(DbAppnApprovalList.ROLE_TADM)) {
                //current approver is TADM, so this is the end of the approval process
                //update the pending approval list to History
                DbAppnApprovalList.makeAction(con, app_id, aah_id, prof.usr_ent_id
                                                ,DbAppnApprovalList.ROLE_TADM
                                                ,DbAppnApprovalList.ACTION_APPROVED
                                                ,curTime);
                result = aeWorkFlowEvent.FUNC_RETURN_END_OF_APPROVAL;
            }
        }

        return result;
    }

    public StringBuffer getMyAppnApprovalLstAsXML(Connection con, loginProfile prof, String aal_status, boolean bSlimOut, cwPagination page) throws SQLException{
        StringBuffer xml = new StringBuffer();
        if (page.curPage == 0)
            page.curPage = 1;

        if (page.pageSize == 0)
            page.pageSize = cwPagination.defaultPageSize;

        if (page.sortCol == null || page.sortCol.length() == 0) {
            if (aal_status.equalsIgnoreCase(DbAppnApprovalList.STATUS_HISTORY))
                page.sortCol = "aal_action_timestamp";
            else
                page.sortCol = "aal_create_timestamp";
        }

        if (page.sortOrder == null || page.sortOrder.length() == 0) {
            if (aal_status.equalsIgnoreCase(DbAppnApprovalList.STATUS_HISTORY))
                page.sortOrder = "desc";
            else
                page.sortOrder = "asc";
        }
        ViewAppnApprovalList viewAppnAppr = new ViewAppnApprovalList();
        Vector vtApprRec = null;
        if (aal_status.equalsIgnoreCase(DbAppnApprovalList.STATUS_HISTORY)){
            vtApprRec = viewAppnAppr.getApprovalHistoryList(con, prof.usr_ent_id, page.sortCol, page.sortOrder, prof.current_role);
        }else{
            vtApprRec = viewAppnAppr.getPendingApprovalList(con, prof.usr_ent_id, page.sortCol, page.sortOrder, prof.current_role);
        }
        page.ts = cwSQL.getTime(con);
        page.totalRec = vtApprRec.size();
        page.totalPage = page.totalRec/page.pageSize;

        if (page.totalRec%page.pageSize !=0) {
            page.totalPage++;
        }
        xml.append("<approval_list type=\"").append(aal_status.toUpperCase()).append("\">").append(cwUtils.NEWL);

        int start = page.pageSize * (page.curPage-1);
        int end = start+page.pageSize - 1;
        if (end >= page.totalRec){
            end = page.totalRec - 1;
        }
        for(int i=start; i <= end; i++) {
            ViewAppnApprovalList data = (ViewAppnApprovalList) vtApprRec.elementAt(i);
            xml.append("<approval_record>").append(cwUtils.NEWL);
            xml.append("<application id=\"").append(data.app_id).append("\">").append(cwUtils.NEWL)
                .append("<pending_status>").append(data.app_process_status).append("</pending_status>").append(cwUtils.NEWL)
                .append("<update_timestamp>").append(data.app_upd_timestamp).append("</update_timestamp>").append(cwUtils.NEWL)
                .append("</application>").append(cwUtils.NEWL);
            if (!bSlimOut){
                xml.append("<approver ent_id=\"").append(data.aal_usr_ent_id).append("\">").append(cwUtils.NEWL)
                    .append("<display_bil>").append(cwUtils.esc4XML(prof.usr_display_bil)).append("</display_bil>").append(cwUtils.NEWL)
                    .append("<approval_role>").append(data.aal_approval_role).append("</approval_role>").append(cwUtils.NEWL)
                    .append("</approver>").append(cwUtils.NEWL);
            }
            xml.append("<applicant ent_id=\"").append(data.app_ent_id).append("\">").append(cwUtils.NEWL)
                .append("<display_bil>").append(cwUtils.esc4XML(data.appt_display_bil)).append("</display_bil>").append(cwUtils.NEWL)
                .append("<group_full_path>").append(cwUtils.esc4XML(dbEntityRelation.getFullPath(con, data.app_ent_id))).append("</group_full_path>").append(cwUtils.NEWL)
                .append("</applicant>").append(cwUtils.NEWL);
            if (!bSlimOut){
                xml.append("<item id=\"").append(data.itm_id).append("\">").append(cwUtils.NEWL)
                    .append("<title>").append(cwUtils.esc4XML(data.itm_title)).append("</title>").append(cwUtils.NEWL);
                if(data.run_itm_id > 0) {
                    xml.append("<item id=\"").append(data.run_itm_id).append("\">").append(cwUtils.NEWL)
                        .append("<title>").append(cwUtils.esc4XML(data.run_itm_title)).append("</title>").append(cwUtils.NEWL)
                        .append("</item>");
                }
                xml.append("</item>").append(cwUtils.NEWL);
            }
            xml.append("<create_timestamp>").append(data.aal_create_timestamp).append("</create_timestamp>").append(cwUtils.NEWL);
            if (data.aal_action_taken!=null && !bSlimOut){
                xml.append("<action>").append(data.aal_action_taken).append("</action>");
                xml.append("<action_taker ent_id=\"").append(data.actr_ent_id).append("\">").append(cwUtils.NEWL)
                    .append("<display_bil>").append(cwUtils.esc4XML(data.actr_display_bil)).append("</display_bil>").append(cwUtils.NEWL)
                    .append("<approval_role>").append(data.actr_role).append("</approval_role>").append(cwUtils.NEWL)
                    .append("</action_taker>").append(cwUtils.NEWL);
                xml.append("<action_timestamp>").append(data.aal_action_timestamp).append("</action_timestamp>");
            }
            xml.append("</approval_record>").append(cwUtils.NEWL);
        }

        xml.append("</approval_list>");

        xml.append(page.asXML());
        if (!bSlimOut && aal_status.equalsIgnoreCase(DbAppnApprovalList.STATUS_PENDING)){
            xml.append(aeTemplate.getFirstRawTpl(con, aeTemplate.WORKFLOW, prof.root_ent_id, "ASC"));
        }
        return xml;

    }
    /*
    public StringBuffer approvalRecAsXML(Connection con, ViewAppnApprovalList data, String status){
        StringBuffer xml = new StringBuffer();

        return xml;

    }
    */
    /**
    Get the applicant's next group supervisors
    @param con Connection to database
    @param app_ent_id applicant's user entity id
    @param current_usg_ent_id the application's current approval group, 0 if the application does not have an approval group yet
    @param Vector contains 2 elements: 1) Long of the entity id of the next supervised group; 2) Vector of Long of the entity id of the next group supervisors
    @return 1 - normal
    */
    private static final int APPROVER_SKIPPED = 2;
    private static final int APPROVER_FOUND = 1;
    private static final int NO_APPROVER = 0;

    private int route2GS(Connection con, long root_ent_id, long app_id, long app_ent_id, Timestamp curTime, long current_usg_ent_id)
                                          throws SQLException, qdbException {
//        Vector vResult = null;
        Vector vSupervisor = null;
        Long groupEntId = null;

        //get application's terminating group for approval
        dbRegUser usr = new dbRegUser();
        usr.usr_ent_id = app_ent_id;
        usr.get(con);
        boolean gsSkippped = false;

        //if the user's terminating group for approval <> current usg_ent_id
        //get the next group approval
        if(usr.usr_app_approval_usg_ent_id > 0 && usr.usr_app_approval_usg_ent_id != current_usg_ent_id) {
            //get the user's ancester list
        	dbEntityRelation dbEr = new dbEntityRelation();
        	dbEr.ern_child_ent_id = app_ent_id;
        	dbEr.ern_type = dbEntityRelation.ERN_TYPE_USR_PARENT_USG;
        	Vector ancestorLst = dbEr.getAncestorList2Vc(con, false);
            String ancestorLstSql = cwUtils.vector2list(ancestorLst);;

            //get the user's group supervisors
            ViewSuperviseTargetEntity sup = new ViewSuperviseTargetEntity();
            Hashtable h = sup.getGroupSupervisors(con, ancestorLstSql);

            //get the next group approvers' supervised group entity id
            int begin = 0;
            if(current_usg_ent_id == 0) {
                begin = ancestorLst.size() - 1;
            } else {
                for(int i=0; i<ancestorLst.size(); i++) {
                    if(((Long)ancestorLst.elementAt(i)).longValue() == current_usg_ent_id) {
                        begin = i - 1 ;
                        break;
                    }
                }
            }

            //get the next group supervisors and their supervised group
            for(int i=begin; i>=0 ; i--) {
                Vector v = (Vector)h.get((Long)ancestorLst.elementAt(i));
                if(v != null && v.size() > 0) {
                    boolean thisGsSkippped = false;
                    for (int j=0; j<v.size(); j++){
                        long approver_ent_id = ((Long) v.elementAt(j)).longValue();
                        if (approver_ent_id == app_ent_id || DbAppnApprovalList.isPreviousApprover(con, app_id, approver_ent_id)){
                        	CommonLog.debug("Skipped GS == APPLICANT");
                         gsSkippped = true;
                         thisGsSkippped = true;
                        }
                        continue;
                    }
                    if (!thisGsSkippped){
                        vSupervisor = v;
                        groupEntId = (Long)ancestorLst.elementAt(i);
                        break;
                    }
                }
                if (((Long)ancestorLst.elementAt(i)).longValue() == usr.usr_app_approval_usg_ent_id){
                    break;
                }
            }

        }
        if (groupEntId != null && vSupervisor != null){
            for(int i=0; i<vSupervisor.size(); i++) {
                DbAppnApprovalList approval = new DbAppnApprovalList();
                approval.aal_usr_ent_id = ((Long)vSupervisor.elementAt(i)).longValue();
                approval.aal_app_id = app_id;
                approval.aal_app_ent_id = app_ent_id;
                approval.aal_approval_role = DbAppnApprovalList.ROLE_SUPERVISE;
                approval.aal_approval_usg_ent_id = groupEntId.longValue();
                approval.aal_status = DbAppnApprovalList.STATUS_PENDING;
                approval.aal_create_timestamp = curTime;
                approval.ins(con);
            }
            return APPROVER_FOUND;
        }else if (gsSkippped){
            return APPROVER_SKIPPED;
        }else{
            return NO_APPROVER;
        }
    }


    /**
    Get the Training Administrator(s) of a Learning Solution
    @param con Connection to database
    @param app_itm_id item id of the Learning Solution
    @param app_ent_id site entity id of the site which the Learning Solution belongs to
    @return Vector of Long of the entity id of the Training Administrator
    */
    private int route2TADM(Connection con, long root_ent_id, long app_id, long app_ent_id, Timestamp curTime, long app_itm_id)
        throws SQLException, cwException {

        //get TADM role
        AccessControlWZB acl = new AccessControlWZB();
        Hashtable hRole = acl.getAllRoleUid(con, root_ent_id, "rol_ste_uid");
        String tadm = (String)hRole.get(DbAppnApprovalList.ROLE_TADM);

        Vector vTADM = aeItemAccess.getAccessEntVec(con, tadm, app_itm_id);
        for (int i=0; i<vTADM.size(); i++ ){
            long tadm_ent_id = ((Long)vTADM.elementAt(i)).longValue();
            if (tadm_ent_id == app_ent_id || DbAppnApprovalList.isPreviousApprover(con, app_id, tadm_ent_id)){
                vTADM.clear();
                return APPROVER_SKIPPED;
            }
        }
        if (vTADM.size() == 0){
            return NO_APPROVER;
        }else{
            for(int i=0; i<vTADM.size(); i++) {
                DbAppnApprovalList approval = new DbAppnApprovalList();
                approval.aal_usr_ent_id = ((Long)vTADM.elementAt(i)).longValue();
                approval.aal_app_id = app_id;
                approval.aal_app_ent_id = app_ent_id;
                approval.aal_approval_role = DbAppnApprovalList.ROLE_TADM;
                approval.aal_status = DbAppnApprovalList.STATUS_PENDING;
                approval.aal_create_timestamp = curTime;
                approval.ins(con);
            }
            return APPROVER_FOUND;
        }
    }

	public String checkAppnConflictAsXML(Connection con, long itm_id, long usr_ent_id, boolean multi_conf) throws cwException, SQLException, qdbException, cwSysMessage {
		List confArray = checkAppnConflict(con, itm_id, usr_ent_id, multi_conf, null);
		if (confArray != null && confArray.size() > 0) {
			StringBuffer result = new StringBuffer();
			Iterator iter = confArray.iterator();
			result.append("<application_conflict itm_id=\"").append(itm_id).append("\"");
			result.append(" usr_ent_id=\"").append(usr_ent_id).append("\"");
			result.append(" usr_display_bil=\"").append(cwUtils.esc4XML(dbRegUser.getDisplayBil(con, usr_ent_id))).append("\"");
			result.append(" usr_ste_usr_id=\"").append(dbRegUser.getSteUsrId(con, usr_ent_id)).append("\">");
			while (iter.hasNext()) {
				String conflict = (String)iter.next();
				result.append("<conflict id=\"").append(conflict.substring(0, conflict.indexOf("~"))).append("\">").append(cwUtils.NEWL);
				result.append(conflict.substring(conflict.indexOf("~")+1));
				result.append(cwUtils.NEWL);
				result.append("</conflict>").append(cwUtils.NEWL);
			}
			result.append("</application_conflict>");
			return result.toString();
		} else {
			return null;
		}
	}

	//checkAppnConflict
	public ArrayList checkAppnConflict(Connection con, long itm_id, long usr_ent_id, boolean multi, ArrayList conf_msg_ls)
		throws cwException, SQLException, qdbException, cwSysMessage {

		ArrayList confArray = new ArrayList();
		//check if the learner a repeated learner for not allow repeat course
		List retake_itm_lst = hasRetakeConflict(con, itm_id, usr_ent_id);
		if(retake_itm_lst.size() != 0) {
			StringBuffer xml = new StringBuffer(200);
			xml.append("CONF_1~");
			if(conf_msg_ls!=null)conf_msg_ls.add("CONF_1");
			aeItem retake_itm = new aeItem();
			for(int i=0;i<retake_itm_lst.size();i++){
				long retake_itm_id = ((Long)retake_itm_lst.get(i)).longValue();
				retake_itm.itm_id = retake_itm_id;
				xml.append("<retake_item itm_id=\"").append(retake_itm.itm_id).append("\" ")
					.append("itm_code=\"").append(cwUtils.esc4XML(retake_itm.getItemCode(con))).append("\"/>");
			}
			confArray.add(xml.toString());
			if (!multi) return confArray;
		}

		//check if the learner passes prerequisite criteria
		aeItemRequirement itm_req = new aeItemRequirement();
		Hashtable requirement = itm_req.checkPrerequisiteAndGetConditionRuleList(con, usr_ent_id, itm_id, aeItemRequirement.REQ_SUBTYPE_ENROLLMENT, 1);
		if(requirement.containsKey("false")) {
			StringBuffer result = new StringBuffer(200);
			StringBuffer xml = new StringBuffer(200);
			List require_list = new ArrayList();
			result.append("CONF_3~");
			if(conf_msg_ls!=null)conf_msg_ls.add("CONF_3");
			require_list = (List) requirement.get("false");
			for(int i=0;i<require_list.size();i++){
				xml.append("<require_item>").append(cwUtils.NEWL);
				try {
					xml.append(itm_req.conditionRuleXML(con,(String) require_list.get(i)));
				}catch (qdbException e) {
					throw new cwSysMessage(QM_INVALID_ACTION);
				}
				xml.append("</require_item>").append(cwUtils.NEWL);
			}
			result.append(xml.toString());
			confArray.add(result.toString());
			if (!multi) return confArray;
		}
		//check if the learner is included in targeted enrollments
		aeItem itm = new aeItem();
		itm.itm_id = itm_id;
		//课程发布为 所有学员时  目标学员之外的学员也可以报名
		itm.getItem(con);
		boolean itm_status_all = false;
		if(null != itm.itm_access_type && !itm.itm_access_type.equals("")){
			itm_status_all = itm.itm_access_type.equalsIgnoreCase(aeItem.ITM_STATUS_ALL);
		}
		
		if(itm.getRunInd(con) && !aeItem.isTargetedLearner(con, usr_ent_id, itm_id, false) && !itm_status_all) {
			StringBuffer xml = new StringBuffer(200);
			aeItemRelation itr = new aeItemRelation();
			itr.ire_child_itm_id = itm_id;
			xml.append("CONF_2~");
			if(conf_msg_ls!=null)conf_msg_ls.add("CONF_2");
			xml.append("<conflict_item itm_id=\"").append((itr.getParentInfo(con)).itm_id).append("\" ")
				.append("display_bil=\"").append((itr.getParentInfo(con)).itm_title).append("\" ")
				.append("itm_code=\"").append((itr.getParentInfo(con)).itm_code).append("\" ")
				.append("run_code=\"").append(itm.getItemCode(con)).append("\"/>");
			confArray.add(xml.toString());
			if (!multi) return confArray;
		}
		//check if the learner has enrolled other classes conflict with this one
		List conf_itm_lst = hasTimetableConflict(con, itm_id, usr_ent_id);
		if(conf_itm_lst.size() != 0) {
			StringBuffer xml = new StringBuffer(200);
			xml.append("CONF_5~");
			if(conf_msg_ls!=null)conf_msg_ls.add("CONF_5");
			aeItem chd_itm = new aeItem();
			aeItem prt_itm = new aeItem();
			aeItemRelation itm_ire = new aeItemRelation();
			for(int i=0;i<conf_itm_lst.size();i++){
				long conf_itm_id = ((Long)conf_itm_lst.get(i)).longValue();
				chd_itm.itm_id = conf_itm_id;
				itm_ire.ire_child_itm_id = conf_itm_id;
				prt_itm = itm_ire.getParentInfo(con);
				xml.append("<conflict_item itm_id=\"").append(prt_itm.itm_id).append("\" ")
					.append("display_bil=\"").append(prt_itm.itm_title).append("\" ")
					.append("itm_code=\"").append(prt_itm.itm_code).append("\" ")
					.append("run_code=\"").append(chd_itm.getItemCode(con)).append("\"/>");
			}
			confArray.add(xml.toString());
			if (!multi) return confArray;
		}

		return confArray;
	}
	private static final String SQL_HAS_SAMECOURSE_CONFLICT = "select app_itm_id " +
				" from aeApplication, aeAttendance, aeAttendanceStatus" +
				" where app_ent_id=?" +
				" and app_itm_id in (select ire_child_itm_id from aeItemRelation where ire_parent_itm_id in (select ire_parent_itm_id from aeItemRelation where ire_child_itm_id=?))" +
				" and att_app_id = app_id and att_ats_id=ats_id and ats_cov_status='I'";

	public List checkAppnSameCourse(Connection con, long itm_id, long usr_ent_id) throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List same_itm_lst = new ArrayList();
		try {
			stmt = con.prepareStatement(SQL_HAS_SAMECOURSE_CONFLICT);
			stmt.setLong(1, usr_ent_id);
			stmt.setLong(2, itm_id);
			rs = stmt.executeQuery();
			if (rs.next()) {
				same_itm_lst.add(new Long(rs.getLong(1)));
			}
		} finally {
			if (rs != null) rs.close();
			if (stmt != null) stmt.close();
		}
		return same_itm_lst;
	}

	private String getTimetableConflictSQL(List ttb_itm_ids) {
	    StringBuffer sqlbuf = new StringBuffer();
	    sqlbuf.append(" select distinct c_itm_id from (");
	    if (ttb_itm_ids != null && ttb_itm_ids.size() > 0) {
	        sqlbuf.append("select o.ils_itm_id as c_itm_id"
				+ " from aeItemLesson o, aeItemLesson n "
				+ " where n.ils_itm_id = ?"
				+ " and o.ils_itm_id in (");
		    for (int i = 0; i < ttb_itm_ids.size(); i++) {
		        sqlbuf.append(ttb_itm_ids.get(i) + ((i != (ttb_itm_ids.size()-1)) ? "," : ""));
		    }
		    sqlbuf.append(")");
			sqlbuf.append(" and ( "
				+ " (o.ils_start_time between n.ils_start_time and n.ils_end_time) "
				+ " or "
				+ " (o.ils_end_time between n.ils_start_time and n.ils_end_time) "
				+ " or "
				+ " (o.ils_start_time > n.ils_start_time and o.ils_end_time < n.ils_end_time) "
				+ " or "
				+ " (o.ils_start_time < n.ils_start_time and o.ils_end_time > n.ils_end_time)"
				+ " )");
	    }
	    sqlbuf.append(") confictItem ");
    return sqlbuf.toString();
	}

	public List hasTimetableConflict(Connection con, long itm_id, long usr_ent_id) throws SQLException {
		aeItemLesson ils = new aeItemLesson();
		ils.ils_itm_id = itm_id;
		List ttb_itm_ids = new ArrayList();
		ils.getActiveAppnItmid(con, usr_ent_id, itm_id, ttb_itm_ids);
		List conf_itm_id_lst = new ArrayList();
		boolean hasTimetable = ils.isDateSetted(con);
		if (hasTimetable){
			if (ttb_itm_ids != null && ttb_itm_ids.size() > 0) {
				PreparedStatement stmt = null;
				ResultSet rs = null;
				stmt = con.prepareStatement(getTimetableConflictSQL(ttb_itm_ids));
				int index = 1;
				if (ttb_itm_ids != null && ttb_itm_ids.size() > 0) {
					stmt.setLong(index++, itm_id);
				}
				rs = stmt.executeQuery();
				if(rs.next()) {
					conf_itm_id_lst.add(new Long(rs.getLong(1)));
				}
				if (rs != null) rs.close();
				if (stmt != null) stmt.close();

			}
		}
		return conf_itm_id_lst;
	}

    private static final String SQL_HAS_RETAKE_CONFLICT
        = " select app_itm_id "
        + " from aeApplication, aeAttendance, aeAttendanceStatus "
        + " where app_ent_id = ? "
        + " and app_itm_id = ? "
        + " and app_id = att_app_id "
        + " and att_ats_id = ats_id "
        + " and ats_type = ? ";

    private static final String SQL_HAS_RETAKE_CONFLICT_RUN
        = " select app_itm_id "
        + " from aeApplication, aeAttendance, aeAttendanceStatus, aeItemRelation "
        + " where app_ent_id = ? "
        + " and app_itm_id = ire_child_itm_id "
        + " and ire_parent_itm_id = ? "
        + " and app_id = att_app_id "
        + " and att_ats_id = ats_id "
        + " and ats_type = ? ";

	public List hasRetakeConflict(Connection con, long itm_id, long usr_ent_id)
		throws SQLException, cwException {
			List itm_id_lst = new ArrayList();
            PreparedStatement stmt = null;
            ResultSet rs =null;
			boolean retakable = aeItem.isItemRetakable(con, itm_id);
			if(retakable) {
				return itm_id_lst;
			} else {
                aeItemRelation ire = new aeItemRelation();
                ire.ire_child_itm_id = itm_id;
                long parent_itm_id = ire.getParentItemId(con);
                String SQL = null;
                if(parent_itm_id > 0 ) {
                    SQL = SQL_HAS_RETAKE_CONFLICT_RUN;
                } else {
                    SQL = SQL_HAS_RETAKE_CONFLICT;
                }

                stmt = con.prepareStatement(SQL);
                try {
                    stmt.setLong(1, usr_ent_id);
                    if(parent_itm_id > 0) {
                        stmt.setLong(2, parent_itm_id);
                    } else {
                        stmt.setLong(2, itm_id);
                    }
                    stmt.setString(3, aeAttendanceStatus.STATUS_TYPE_ATTEND);
                    rs = stmt.executeQuery();
                    if(rs.next()) {
						itm_id_lst.add(new Long(rs.getLong(1)));
                    }
                    return itm_id_lst;
                } finally {
                    if(stmt!=null) stmt.close();
                    if(rs!=null) rs.close();
                }
			}
		}

    public void makeItemCancelled(Connection con, loginProfile prof, long itm_id)
        throws SQLException, IOException, cwSysMessage, qdbException, cwException {

        //get the item's workflow template id
        aeItem item = new aeItem();
        item.itm_id = itm_id;
        long tpl_id = item.getTemplateId(con, aeTemplate.WORKFLOW);

        //trun attendance status to 'W'
        makeItemCancelledAttActn(con, prof, itm_id);

        //turn cov to 'W'
        makeItemCancelledCovActn(con, prof, itm_id);

        //cancel all the pending/admitted/waiting applications
        makeItemCancelledAppnActn(con, prof, itm_id, tpl_id, aeApplication.PENDING);
        makeItemCancelledAppnActn(con, prof, itm_id, tpl_id, aeApplication.ADMITTED);
        makeItemCancelledAppnActn(con, prof, itm_id, tpl_id, aeApplication.WAITING);
    }

    private void makeItemCancelledAppnActn(Connection con, loginProfile prof, long itm_id, long tpl_id, String app_status)
        throws SQLException, IOException, cwSysMessage, qdbException, cwException {

        //get applications
        long[] list = aeApplication.getQueueFromItem(con, itm_id, app_status);
        if(list != null && list.length > 0) {
            //get the first app_process_xml
            aeApplication app = new aeApplication();
            app.app_id = list[0];
            app.get(con);

            //get the details of the application's current process
            aeWorkFlow wkf = new aeWorkFlow(dbUtils.xmlHeader);
            Vector vProcess = wkf.parseProcessXML(app.app_process_xml);
            String currentProcessId    = (String) vProcess.elementAt(0);
            String currentProcessName  = (String) vProcess.elementAt(1);
            String currentStatusId     = (String) vProcess.elementAt(2);
            String currentStatusName   = (String) vProcess.elementAt(3);

            //get the item cancelled action
            Vector vAction = wkf.getItemCancelledAction(tpl_id, currentProcessId, currentStatusId);
            String processId    = (String) vAction.elementAt(0);
            String statusId     = (String) vAction.elementAt(1);
            String actionId     = (String) vAction.elementAt(2);
            String fromStatus   = (String) vAction.elementAt(3);
            String toStatus     = (String) vAction.elementAt(4);
            String verb         = (String) vAction.elementAt(5);

            //do item cancelled action for each application
            for(int i=0; i<list.length; i++) {
                doAppnActn(con, list[i], Long.parseLong(processId),
                                       fromStatus, toStatus, verb, Long.parseLong(actionId),
                                       Long.parseLong(statusId), prof, null,
                                       null, null, false);
            }
        }
        return;
    }

    private static final String SQL_MAKE_ATT_CANCELLED
        = " update aeAttendance set att_ats_id = ?, att_timestamp = ?, att_update_usr_id = ?, att_update_timestamp = ? "
        + " where att_app_id in (select app_id from aeApplication where app_itm_id = ? and app_status in (?,?,?)) ";

    private void makeItemCancelledAttActn(Connection con, loginProfile prof, long itm_id) throws SQLException {
        PreparedStatement stmt = null;
        //get current time
        Timestamp curTime = cwSQL.getTime(con);

        //get 'W' ats_id
        int ats_id = aeAttendanceStatus.getIdByCovStatus(con, prof.root_ent_id, "W");
        try {
            stmt = con.prepareStatement(SQL_MAKE_ATT_CANCELLED);
            stmt.setInt(1, ats_id);
            stmt.setTimestamp(2, curTime);
            stmt.setString(3, prof.usr_id);
            stmt.setTimestamp(4, curTime);
            stmt.setLong(5, itm_id);
            stmt.setString(6, aeApplication.PENDING);
            stmt.setString(7, aeApplication.ADMITTED);
            stmt.setString(8, aeApplication.WAITING);
            stmt.executeUpdate();
        } finally {
            if(stmt!=null) stmt.close();
        }
    }

    private static final String SQL_MAKE_COV_CANCELLED
        = " update CourseEvaluation set cov_status = ?, cov_final_ind = ?, cov_update_timestamp = ? "
        + " where cov_tkh_id in (select app_tkh_id from aeApplication where app_itm_id = ? and app_status in (?,?,?)) ";

    private void makeItemCancelledCovActn(Connection con, loginProfile prof, long itm_id) throws SQLException {
        PreparedStatement stmt = null;
        //get current time
        Timestamp curTime = cwSQL.getTime(con);

        try {
            stmt = con.prepareStatement(SQL_MAKE_COV_CANCELLED);
            stmt.setString(1, "W");
            stmt.setBoolean(2, true);
            stmt.setTimestamp(3, curTime);
            stmt.setLong(4, itm_id);
            stmt.setString(5, aeApplication.PENDING);
            stmt.setString(6, aeApplication.ADMITTED);
            stmt.setString(7, aeApplication.WAITING);
            stmt.executeUpdate();
        } finally {
            if(stmt!=null) stmt.close();
        }
    }


    public void setAppnFilterSession(HttpSession sess, String filter_value, String filter_type, String app_process_status, Timestamp appn_upd_fr, Timestamp appn_upd_to, dbUserGroup dbusg) {
        if (filter_type != null) {
            if (filter_type.equalsIgnoreCase("simple_filter")) {
                if (filter_value == null || filter_value.length() == 0) {
                    sess.removeAttribute("filter_value");
                } else {
                    sess.setAttribute("filter_value", filter_value);
                    sess.removeAttribute("appn_filter");
                }
                sess.setAttribute("filter_type", filter_type);
            } else if (filter_type.equalsIgnoreCase("status_filter")) {
                if (app_process_status == null) {
                    sess.removeAttribute("appn_process_status");
                } else {
                    sess.setAttribute("appn_process_status", app_process_status);
                    sess.removeAttribute("appn_filter");
                }
                sess.setAttribute("filter_type", filter_type);
            } else if (filter_type.equalsIgnoreCase("advanced_filter")) {
                Hashtable filter_hash = new Hashtable();
                if (app_process_status != null) {
                    filter_hash.put("appn_process_status", app_process_status);
                }
                if (appn_upd_fr != null) {
                    filter_hash.put("appn_upd_fr", appn_upd_fr);
                }
                if (appn_upd_to != null) {
                    filter_hash.put("appn_upd_to", appn_upd_to);
                }
                filter_hash.put("dbusg", dbusg);

                sess.setAttribute("appn_filter", filter_hash);
                sess.removeAttribute("appn_process_status");
                sess.removeAttribute("filter_value");
                sess.setAttribute("filter_type", filter_type);
            } else if (filter_type.equalsIgnoreCase("clear_filter")) {
                sess.removeAttribute("filter_value");
                sess.removeAttribute("appn_process_status");
                sess.removeAttribute("appn_filter");
                sess.setAttribute("filter_type", filter_type);
            }
        } else {
            sess.removeAttribute("filter_value");
            sess.removeAttribute("appn_process_status");
            sess.removeAttribute("appn_filter");
            sess.removeAttribute("filter_type");
        }
    }

    private int route2DS(Connection con, long root_ent_id, long app_id, long app_ent_id, Timestamp curTime) throws cwException, SQLException{
        ViewSuperviseTargetEntity.ViewDirectSupervisor[] ds
        = ViewSuperviseTargetEntity.getDirectSupervisor(con, app_ent_id);

        if (ds.length == 0){
            return NO_APPROVER;
        }
        for(int i=0; i<ds.length; i++) {
            // skipDS if DS = applicant
            if (ds[i].usr_ent_id == app_ent_id){
            	CommonLog.debug("DS = applicant");
                return APPROVER_SKIPPED;
            }
        }
        for(int i=0; i<ds.length; i++) {
            DbAppnApprovalList approval = new DbAppnApprovalList();
            approval.aal_usr_ent_id = ds[i].usr_ent_id;
            approval.aal_app_id = app_id;
            approval.aal_app_ent_id = app_ent_id;
            approval.aal_approval_role = DbAppnApprovalList.ROLE_DIRECT_SUPERVISE;
            approval.aal_status = DbAppnApprovalList.STATUS_PENDING;
            approval.aal_create_timestamp = curTime;
            approval.ins(con);
        }
        return APPROVER_FOUND;
    }

    public Vector getMyAppnApprovalLst4HomePage(Connection con, long usr_ent_id) throws SQLException {
    	Vector result = new Vector();
    	String sql = "SELECT aal_app_id, aal_create_timestamp, usr_display_bil from aeAppnApprovalList, RegUser where aal_usr_ent_id = ? and aal_status = ? and aal_app_ent_id = usr_ent_id order by aal_create_timestamp asc";
    	PreparedStatement stmt = con.prepareStatement(sql);
    	int index = 1;
    	stmt.setLong(index++, usr_ent_id);
    	stmt.setString(index++, DbAppnApprovalList.STATUS_PENDING);
    	ResultSet rs = stmt.executeQuery();
    	while (rs.next()) {
    		aeAppRec appRec = new aeAppRec();
    		appRec.app_id = rs.getLong("aal_app_id");
    		appRec.app_usr_display_bil = rs.getString("usr_display_bil");
    		appRec.app_create_timestamp = rs.getTimestamp("aal_create_timestamp");
    		result.add(appRec);
    	}
    	stmt.close();
    	return result;
    }

    public StringBuffer getMyAppnApprovalLst4HomePageAsXML(Vector appRec) throws SQLException{
        StringBuffer xml = new StringBuffer(512);
        long totalRec = appRec.size();
        int app_size = 3;
        if(app_size > appRec.size()) {
        	app_size = appRec.size();
        }
        xml.append("<approval_list type=\"").append(DbAppnApprovalList.STATUS_PENDING).append("\">");
        for(int i=0; i < app_size; i++) {
        	aeAppRec data = (aeAppRec) appRec.elementAt(i);
            xml.append("<approval_record>");
            xml.append("<application id=\"").append(data.app_id).append("\"/>");
            xml.append("<applicant>")
                .append("<display_bil>").append(cwUtils.esc4XML(data.app_usr_display_bil)).append("</display_bil>")
                .append("</applicant>");
            xml.append("<create_timestamp>").append(data.app_create_timestamp).append("</create_timestamp>");
            xml.append("</approval_record>");
        }
        xml.append("</approval_list>");
        xml.append("<pagination total_rec=\"").append(totalRec).append("\"/>");
        return xml;
    }

    /**
     * convert the workflow xml to json with these cases:
     * 1. every node will be treated as an object
     * 	  ex. <process></process> to
     * 		"process":{
     * 		}
     * 2. attributes in xml will be treated as field
     *    ex. <process id="1" name="Application" type="queue"></process> to
     *    	"process":{
     *    		"id" : "1",
     *    		"name" : "Application",
     *    		"type" : "queue"
     *    	}
     * 3. the different sub nodes will be treated as sub obj
     *    ex. <process>
     *    			<status></status>
     *    			<action></action>
     *    	  </process> to
     *    	"process":{
     *    		"status":{
     *    		},
     *    		"action":{
     *    		}
     *    	}
     *  4. sub nodes with the same name will be treated as an array
     *    ex. <process id="1" name="Application" type="queue">
     *    		<status id="0" name="_Exit"/>
     *    		<status id="1" name="等待审批"/>
     *    	  </process> to
     *    	"process":{
     *    		"id" : "1",
     *    		"name" : "Application",
     *    		"type" : "queue",
     *    		"status":[{
     *    			"id" : "0",
     *    			"name" : "_Exit"
     *    		},{
     *    			"id" : "1",
     *    			"name" : "等待审批"
     *    		}]
     *    	}
     * @param con
     * @param owner_ent_id
     * @param resultJson
     * @throws SQLException
     * @throws cwException
     */
    public static void convertWorkflowToJson(Connection con, long owner_ent_id, HashMap resultJson) throws SQLException, cwException {
    	String convertedJson = "";
        StringBuffer tpl_xml = new StringBuffer(1024);
        tpl_xml.append("<xml>")
        	.append(aeTemplate.getFirstRawTpl(con, aeTemplate.WORKFLOW, owner_ent_id, ""))
        	.append("</xml>");
        convertedJson = qdbAction.static_env.transformXML(tpl_xml.toString(), "workflow_to_json.xsl",null);
        convertedJson = cwUtils.decodeUnicode(convertedJson.toString());
        JSONObject strucObj = JSONObject.fromObject(convertedJson);
        resultJson.put(aeTemplate.WORKFLOW.toLowerCase(), strucObj);
    }
    public void enrolCosNoflow(Connection con, long app_id, long itm_id, long ent_id, loginProfile prof, long tkh_id) throws cwException, SQLException, cwSysMessage{

        dbCourse cos = new dbCourse();

        cos.cos_res_id = getResId(con, itm_id);
        cos.cos_itm_id = itm_id;

        if(cos.cos_res_id != 0) {
            //update enrolment
            try {
                cos.enroll(con, ent_id, prof);
            }
            catch(qdbException e) {
                throw new cwException(e.getMessage());
            }
            int ats_id =2;
            aeAttendance.saveStatus_no_flow(con, app_id, itm_id , cos.cos_res_id, ent_id,  false, ats_id, prof.usr_id, tkh_id);
        }

        return;
    }
    /**
     * 无需审批的报名，跳过报名工作流
     * @param con
     * @param app_xml
     * @param ent_id
     * @param itm_id
     * @param prof
     * @param item
     * @return
     * @throws SQLException
     * @throws qdbException
     * @throws cwSysMessage
     * @throws cwException
     * @throws IOException
     */
    public aeApplication insAppNoWorkflow(Connection con, String app_xml, long ent_id, long itm_id, String comment, String from, loginProfile prof, aeItem item) throws SQLException, qdbException, cwSysMessage, cwException, IOException {
        long app_id = aeApplication.getAppId(con, itm_id, ent_id, true);
        aeApplication app = new aeApplication();
        
        String work_flow_lan = ((Personalization)qdbAction.wizbini.cfgOrgPersonalization.get(prof.root_id)).getSkinList().getDefaultLang();
        if (app_id != 0) {
            //if an admitted application exists, insert inactive aeAppnEnrolRelation records
            app.app_id = app_id;
            app.getWithItem(con);
            if(this.isAdmittedApp(app)) {
                enrolCos(con, app.app_id, itm_id, ent_id, prof, 0);
            }
        } else {
            aeTreeNode treeNode = new aeTreeNode();
            StringBuffer result = new StringBuffer();
            String[] process_status_lst = aeReqParam.split(LangLabel.getValue(work_flow_lan, "process_status"), "~");
            Timestamp cur_time = dbUtils.getTime(con);
            //check application date
            if(AccessControlWZB.isLrnRole(prof.current_role)){
                checkAppDate(item, cur_time);
            }
            boolean can_app = false;
            app.itm_capacity = item.itm_capacity;
            app.itm_id = item.itm_id;
            //check item not allow waitlisted
            boolean hasTa = AccessControlWZB.hasRolePrivilege(prof.current_role, AclFunction.FTN_AMD_ITM_COS_MAIN_APPLICATION) || AccessControlWZB.hasRolePrivilege(prof.current_role, AclFunction.FTN_AMD_EXAM_MAIN_APPLICATION);
            if( hasTa || item.itm_not_allow_waitlist_ind) { //if not allow waitlist, TA will not check item capacity
                can_app = true;
            } else if(!app.isItemCapacityExceed(con, process_status_lst, false)){
                can_app = true;
            }
            String app_process_xml = null;
            String app_status = null;
            String app_process_status =null;
            String aah_fr_0 = null;
            String aah_to_0 = null;
            String aah_verb_0 = null;
            String aah_fr_1= null;
            String aah_to_1 = null;
            String aah_verb_1 = null;
            long[] cc_sup_ent_ids = null;
            long aah_action_id_1 = 0;
            if(prof.cur_lan == null) {
                prof.cur_lan ="zh-cn";
            }
           //init the application to get app_process_xml, app_status , app_process_status, aah_fr, aah_to, aah_verb and aah_action_id
            if (can_app) {
                  app_process_xml = "<application_process><process id=\"1\" name=\"Application\"><status id=\"3\" name=\"" + LangLabel.getValue(work_flow_lan, "work_flow_app_status_enrol") +"\"/></process></application_process>";
                  app_status ="Admitted";

                 app_process_status = LangLabel.getValue(work_flow_lan, "work_flow_app_status_enrol");
                 aah_fr_1 = LangLabel.getValue(work_flow_lan, "work_flow_app_status_pending");
                 aah_to_1 =  LangLabel.getValue(work_flow_lan, "work_flow_app_status_enrol");
                 aah_verb_1 = LangLabel.getValue(work_flow_lan, "work_flow_app_status_con");
                 

                  aah_action_id_1 = 7;
            } else {
                 app_process_xml = "<application_process><process id=\"1\" name=\"Application\"><status id=\"2\" name=\"" + LangLabel.getValue(work_flow_lan, "work_flow_app_status_wait") +"\"/></process></application_process>";
                 app_status ="Waiting";

                 app_process_status = LangLabel.getValue(work_flow_lan, "work_flow_app_status_wait");
                 aah_fr_1 = LangLabel.getValue(work_flow_lan, "work_flow_app_status_pending");
                 aah_to_1 =  LangLabel.getValue(work_flow_lan, "work_flow_app_status_wait");
                 aah_verb_1 = LangLabel.getValue(work_flow_lan, "work_flow_app_status_check");
                 
                 

                 aah_action_id_1 = 6;
            }
            //报名操作历史纪录,报名的方式：学员申请报名，培训管理员添加报名，培训管理员导入报名
            aah_fr_0 =null;
            if(hasTa){

                aah_to_0 = LangLabel.getValue(work_flow_lan, "work_flow_app_status_add_enroll");
                aah_verb_0 = LangLabel.getValue(work_flow_lan, "work_flow_app_status_add_enroll");

            } else {

                aah_to_0 = LangLabel.getValue(work_flow_lan, "work_flow_app_status_pending");
                aah_verb_0 = LangLabel.getValue(work_flow_lan, "work_flow_app_status_pending");

            }

            app.app_process_xml =app_process_xml;
            app.app_status = app_status;
            app.app_process_status = app_process_status;
            app.app_itm_id = itm_id;
            app.app_ent_id = ent_id;
            app.app_xml = app_xml;
            app.app_create_usr_id = prof.usr_id;
            app.app_upd_usr_id = prof.usr_id;
            app.app_tkh_id = 0;
            
          //报名推荐类型
            if (prof.current_role.indexOf(AccessControlWZB.ROL_STE_UID_TADM) == 0){
                app.app_nominate_type = aeApplication.NOMINATE_TYPE_TADM;
            } else {
                if(prof.isLrnRole && prof.usr_ent_id != app.app_ent_id){
                    app.app_nominate_type = aeApplication.NOMINATE_TYPE_SUP;
                }
                
            }
            
            // insert into aeApplication
            app.ins(con);
            // insert an application history
            aeAppnActnHistory actn = new aeAppnActnHistory();
            actn.aah_app_id = app.app_id;
            actn.aah_process_id = 0;
            actn.aah_fr = aah_fr_0;
            actn.aah_to = aah_to_0;
            actn.aah_verb = aah_verb_0;
            actn.aah_action_id = 0;
            actn.aah_create_usr_id = app.app_create_usr_id;
            actn.aah_create_timestamp = cur_time;
            actn.aah_upd_usr_id = app.app_create_usr_id;
            actn.aah_upd_timestamp = cur_time;
            actn.aah_actn_type = from;
            actn.aah_id = actn.ins(con);
            //insert application comment history
            if(comment != null && comment.length() > 0) {
                aeAppnCommHistory comm = new aeAppnCommHistory();
                comm.ach_app_id = app.app_id;
                comm.ach_aah_id = actn.aah_id;
                comm.ach_content = comment;
                comm.ach_create_timestamp = cur_time;
                comm.ach_create_usr_id = app.app_create_usr_id;
                comm.ach_upd_timestamp = cur_time;
                comm.ach_upd_usr_id = app.app_create_usr_id;
                comm.ins(con);
            }
            // insert an application history
            actn.aah_app_id = app.app_id;
            actn.aah_process_id = 0;
            actn.aah_fr = aah_fr_1;
            actn.aah_to = aah_to_1;
            actn.aah_verb = aah_verb_1;
            actn.aah_action_id = aah_action_id_1;
            actn.aah_create_usr_id ="s1u4";
            actn.aah_create_timestamp = cur_time;
            actn.aah_upd_usr_id = "s1u4";
            actn.aah_upd_timestamp = cur_time;
            actn.aah_actn_type = null;
            actn.aah_id = actn.ins(con);
            // if enrolled, insert to enroll records
//            prof.my_top_tc_id = ViewTrainingCenter.getTopTc(con, prof.usr_ent_id, false);
            if (can_app) { 
				enrolCosNoflow(con, app.app_id, itm_id, ent_id, prof, 0);
				if (aeItem.getRunInd(con, itm_id)){
	                aeItemMessage.insNotifyForJI(con, prof, app.app_ent_id, app.app_itm_id, app.app_id);
				}
	            app.app_tkh_id = aeApplication.getTkhId(con, app.app_id);
            }
            // 获取课程信息
            AeItemMapper aeItemMapper = (AeItemMapper) WzbApplicationContext.getBean("aeItemMapper");
            AeItem aeItem = aeItemMapper.get(itm_id);
            // send email
            item.getSendMailInd(con);
            Hashtable param = setEmailParam(can_app);
            if (item.itm_send_enroll_email_ind > 0 && send_mail_ind
            		&& MessageTemplate.isActive(con, aeItem.getItm_tcr_id(), (String)param.get("template_type"))) {
            	
            	if(item.itm_send_enroll_email_ind == 2) {
            		// 抄送至直属上司
            		List<Long> supervisors = this.getMySupervise(con, app.app_ent_id);
            		if(supervisors != null && supervisors.size() > 0) {
            			cc_sup_ent_ids = new long[supervisors.size()];
            			for(int i=0; i<supervisors.size(); i++) {
            				cc_sup_ent_ids[i] = supervisors.get(i);
            			}
            		}
            	}
            	sendEnrollEmail(con, param, prof, app.app_ent_id, item.itm_id, app.app_id, cc_sup_ent_ids);
            }
        }
        return app;
    }
    
    public List<Long> getMySupervise(Connection con, long app_ent_id) throws SQLException {
    	List<Long> supervisors = new ArrayList<Long>();
    	PreparedStatement stmt = null;
    	ResultSet rs = null;
    	String sql = "select usr_ent_id from superviseTargetEntity"
    			+ " left join regUser on usr_ent_id = spt_source_usr_ent_id"
    			+ " where spt_target_ent_id = ?";
    	stmt = con.prepareStatement(sql);
    	stmt.setLong(1, app_ent_id);
    	rs = stmt.executeQuery();
    	while(rs.next()) {
    		supervisors.add(rs.getLong(1));
    	}
    	stmt.close();
		return supervisors;
	}

	/**
     * 是否已经可以报名
     * @param item
     * @param cur_time
     * @throws cwSysMessage
     */
	private void checkAppDate(aeItem item, Timestamp cur_time) throws cwSysMessage {
		if (item.itm_appn_start_datetime != null
		    && cur_time.before(item.itm_appn_start_datetime)) {
		    throw new cwSysMessage("AEQM03");
		}
		if (item.itm_appn_end_datetime != null
		    && cur_time.after(item.itm_appn_end_datetime)) {
		    throw new cwSysMessage("AEQM04");
		}
	}
	/**
	 * 是否已经报名
	 * @param app
	 * @return
	 */
	private boolean isAdmittedApp(aeApplication app) {
		return app.app_status != null && app.app_status.equalsIgnoreCase(aeApplication.ADMITTED);
	}
	/**
	 * 设置邮件属性
	 * @param can_enroll
	 * @return
	 */
	private  Hashtable setEmailParam(boolean can_enroll){
		 Hashtable param = new Hashtable();
		 if(can_enroll){
			 param.put("template_type","ENROLLMENT_CONFIRMED");
			 param.put("template_subtype","HTML");
			 param.put("from","sysadmin");
			 param.put("to","applicant");
			 param.put("cc","participated_approvers");
			 param.put("reply","item_access");
			 param.put("reply_role","TADM");
			 param.put("subject","Course Enrollment Confirmed");
		 } else {
			 param.put("template_type","ENROLLMENT_WAITLISTED");
			 param.put("template_subtype","HTML");
			 param.put("from","sysadmin");
			 param.put("to","applicant");
			 param.put("cc","item_access");
			 param.put("cc_role", "TADM");
			 param.put("reply","item_access");
			 param.put("reply_role","TADM");
			 param.put("subject","Course Enrollment: Waiting List");
		 }
		 return param;
	}
	/**
	 * send email
	 * @param con
	 * @param param
	 * @param prof
	 * @param app_ent_id
	 * @param itm_id
	 * @param app_id
	 * @throws SQLException
	 * @throws cwException
	 * @throws cwSysMessage
	 * @throws IOException
	 * @throws qdbException 
	 */
	private void sendEnrollEmail(Connection con, Hashtable param, loginProfile prof, long app_ent_id, long itm_id, long app_id, long[] cc_sup_ent_ids) throws SQLException, cwException, cwSysMessage, IOException, qdbException{

		CommonLog.info("SEND_NOTIFY ...aeQueueManager Line6623.........."+param.get("template_type")+"...........");
		String DELIMITER = ";";
		long[] ent_id = null;
		long[] cc_ent_id = null;
		long[] bcc_ent_id = null;
		long[] reply_ent_id = null;
		String sender_usr_id = null;
		String from = (String)param.get("from");
		String to = (String)param.get("to");
		String appr_role = (String)param.get("appr_role");
		String cc = (String)param.get("cc");
		String cc_role = (String)param.get("cc_role");
		StringTokenizer stk_cc_role = (cc_role == null) ? null
				: new StringTokenizer(cc_role, DELIMITER);
		String cc_list = (String) param.get("cc_list");
		String cc_role_list = (String) param.get("cc_role_list");

		String to_role = (String)param.get("to_role");
		StringTokenizer stk_to_role = (to_role == null) ? null
				: new StringTokenizer(to_role, DELIMITER);
		String from_role = (String)param.get("from_role");
		String from_ent_id = (String)param.get("from_ent_id");

		dbRegUser sender = null;
		if( from != null ) {
			sender = new dbRegUser();
			try {
				if( from.equalsIgnoreCase("applicant") ) {
					sender.usr_ent_id = app_ent_id;
					sender.get(con);
					sender_usr_id = sender.usr_id;
				}
				else if( from.equalsIgnoreCase("sysadmin") ) {
					acSite site = new acSite();
					aeItem item = new aeItem();
					site.ste_ent_id = prof.root_ent_id;
					sender.usr_ent_id = site.getSiteSysEntId(con);
					item.itm_id = itm_id;
					item.itm_notify_email = item.getNotifyEmail(con);
					sender.get(con);
					if(!"".equals(item.itm_notify_email)) {
						sender.usr_display_bil = "Course support";
						sender.getUsrID(con);
					}
					
					sender_usr_id = sender.usr_id;
				}
				else if( from.equalsIgnoreCase("item_access") ) {
					Vector v_iac = aeItemAccess.getItemAccessByItem(con, itm_id);
					if(v_iac == null || v_iac.size() == 0) {
						//if no item access record found
						//check it if it is a run and get the parent's item access
						v_iac = aeItemAccess.getParentItemAccessByItem(con, itm_id);
					}
					Vector v_ent_id = new Vector();
					AccessControlWZB acl = new AccessControlWZB();
					Hashtable h_role_map = acl.getAllRoleUid(con, prof.root_ent_id, "rol_ste_uid");
					String from_role_2 = (String) h_role_map.get(from_role);
					if(from_role_2 == null) {
						from_role_2 = "";
					}
					for(int i=0; i < v_iac.size(); i++) {
						aeItemAccess iac = (aeItemAccess) v_iac.elementAt(i);
						if((iac.iac_access_type).equals(aeItemAccess.ACCESS_TYPE_ROLE)
								&& (from_role.equalsIgnoreCase(iac.iac_access_id)
										|| from_role_2.equalsIgnoreCase(iac.iac_access_id))) {
							v_ent_id.addElement(new Long(iac.iac_ent_id));
						}
					}
					sender.usr_ent_id = ((Long)v_ent_id.elementAt(0)).longValue();
					sender.get(con);
					sender_usr_id = sender.usr_id;
				}
				else if( from.equalsIgnoreCase("ent_id") ) {
					sender.usr_ent_id = (new Long(from_ent_id)).longValue();
					sender.get(con);
					sender_usr_id = sender.usr_id;
				}
			} catch (qdbException e) {
				throw new cwException(e.getMessage());
			}
		}
		aeWorkFlowEvent wf =  new aeWorkFlowEvent(con, null, 15);
		aeApplication app = new aeApplication();
		app.app_itm_id = itm_id;
		app.app_id = app_id;
		app.app_ent_id = app_ent_id;
		wf.setApp(app);
		wf.prof = prof;
		if(sender.usr_status != null
		/*&& !sender.usr_status.equalsIgnoreCase(dbRegUser.USR_STATUS_SYS)*/) {
			//if sender is not SYS user, try to send the mail
			if( to != null ) {
				ent_id = wf.get_to_ent_ids(con, to, stk_to_role, appr_role);
			}
			//Either use cc or cc_list
			if( cc != null ) {
				Vector v = wf.get_cc_entVec(con, cc, stk_cc_role);
				cc_ent_id = aeUtils.vec2longArray(v);
			}else if (cc_list != null && cc_list.length() > 0) {
				String[] cc_array = cwUtils.splitToString(cc_list, aeWorkFlowEvent.DEFAULT_DELIMITER);
				String[] cc_role_array = cwUtils.splitToString(cc_role_list, aeWorkFlowEvent.DEFAULT_DELIMITER);
				if (cc_array.length != cc_role_array.length) {
					CommonLog.info("cc_list doesn't match with cc_role_list.");
				}
				Vector v = new Vector();
				for (int i=0;i<cc_array.length;i++) {
					StringTokenizer stk_cc_role_tmp = (cc_role_array[i] == null) ? null
							: new StringTokenizer(cc_role_array[i], DELIMITER);
					Vector v_tmp = wf.get_cc_entVec(con, cc_array[i], stk_cc_role_tmp);
					for (int j=0;j<v_tmp.size();j++) {
						Long id_tmp = (Long) v_tmp.elementAt(j);
						if (!v.contains(id_tmp)) {
							v.addElement(id_tmp);
						}
					}

				}
				cc_ent_id = aeUtils.vec2longArray(v);
			}

			Timestamp sendTime = cwSQL.getTime(con);
			aeXMessage aeXmsg = new aeXMessage();
			param.put("itm_id", new Long(itm_id));
			param.put("app_id", new Long(app_id));
			param.put("action_taker_ent_id", new Long(prof.usr_ent_id));

			if(ent_id != null && ent_id.length > 0) {
				//aeXmsg.insNotify(con, prof, sender_usr_id, ent_id, cc_ent_id, bcc_ent_id, sendTime, param, reply_ent_id);

                //插入邮件及邮件内容
                MessageService msgService = new MessageService();
                String mtp_type = (String)param.get("template_type");
                
        		MessageTemplate mtp = new MessageTemplate();
        		mtp.setMtp_tcr_id(prof.my_top_tc_id);
        		mtp.setMtp_type(mtp_type);
        		mtp.getByTcr(con);
        		
                String[] contents = msgService.getMsgContent(con, mtp, app.app_ent_id, prof.usr_ent_id, app.app_itm_id, app.app_id,ent_id);
                if(cc_sup_ent_ids != null && cc_sup_ent_ids.length > 0) {
                	msgService.insMessage(con, mtp, sender_usr_id, ent_id, cc_sup_ent_ids, sendTime, contents,app.app_itm_id);
                } else {
                	msgService.insMessage(con, mtp, sender_usr_id, ent_id, cc_ent_id, sendTime, contents,app.app_itm_id);
                }
			}else {
				CommonLog.info("No recipients.");
			}
		}

	}
}