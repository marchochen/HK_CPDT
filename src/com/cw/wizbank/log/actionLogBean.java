package com.cw.wizbank.log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbAction;

public class actionLogBean {
    public static final String LOG_OBJ = "LOG_OBJ";

    public static final String ATL_STATUS_S = "Success";

    public static final String ATL_STATUS_F = "Fail";
    
    public static final String ATL_IDS_TYPE_USR = "usr";
    public static final String ATL_IDS_TYPE_USG = "usg";
    public static final String ATL_IDS_TYPE_GRADE = "grade";
    public static final String ATL_IDS_TYPE_ITM = "itm";
    public static final String ATL_IDS_TYPE_RESOURCE = "resource";
    public static final String ATL_IDS_TYPE_MOD = "mod";
    public static final String ATL_IDS_TYPE_CCR = "ccr";
    public static final String ATL_IDS_TYPE_APP = "app";
    public static final String ATL_IDS_TYPE_CPD = "cpd";
    public static final String ATL_IDS_TYPE_TKH = "tkh";
    public static final String ATL_IDS_TYPE_TCR = "tcr";
    public static final String ATL_IDS_TYPE_CAT = "cat";
    public static final String ATL_IDS_TYPE_MSG = "msg";
    public static final String ATL_IDS_TYPE_ILS = "ils";
    public static final String ATL_IDS_TYPE_FAC = "fac";  
    public static final String ATL_IDS_TYPE_RSV = "rsv";  
    public static final String ATL_IDS_TYPE_ITR = "itr";
    public static final String ATL_IDS_TYPE_CERT = "cert";
    
    public void ins(Connection con, actionLog log) {

    }

    public static void ins(Connection con, List<actionLog> log_list) throws SQLException {
        String sql = "insert into actionLog(atl_usr_ent_id,atl_session_id,atl_usr_ste_usr_id,atl_cmd,atl_start_date,atl_duration,"
                + " atl_remote,atl_server,atl_status,atl_error_msg,atl_ids,atl_ids_type,atl_login_code,atl_description)" 
                + " values(?, ?,?, ?,?, ?,?, ?,?, ?,?, ?,?, ?)";
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(sql);
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            int line = 0;
            for (int i = 0; i < log_list.size(); i++) {
                line++;
                actionLog log = log_list.get(i);
                String dat_str = format.format(log.atl_start_date);
                Timestamp ts = Timestamp.valueOf(dat_str);
                int index = 1;
                stmt.setLong(index++, log.atl_usr_ent_id);
                stmt.setString(index++, log.atl_session_id);
                stmt.setString(index++, log.atl_usr_ste_usr_id);
                stmt.setString(index++, log.atl_cmd);
                stmt.setTimestamp(index++, ts);
                stmt.setLong(index++, log.atl_duration);
                stmt.setString(index++, log.atl_remote);
                stmt.setString(index++, log.atl_server);
                stmt.setString(index++, log.atl_status);
                stmt.setString(index++, log.atl_error_msg);
                stmt.setString(index++, log.atl_ids);
                stmt.setString(index++, log.atl_ids_type);
                stmt.setString(index++, log.atl_login_code);
                stmt.setString(index++, log.atl_description);
                stmt.addBatch();
                if (line > 1000) {
                    line = 0;
                    stmt.executeBatch();
                }

            }
            if (line > 0) {
                stmt.executeBatch();
            }

        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    public static void setLog(actionLog log, HttpServletRequest request, String cmd, String atl_status, String atl_error_msg) {
        if (log == null) {
            log = new actionLog();
        }
        if (cmd != null && cmd.length() > 0 && (log.atl_cmd == null || log.atl_cmd.length() == 0)) {
            log.atl_cmd = cmd;
        }
        if (atl_status != null && atl_status.length() > 0 && log.atl_status == null) {
            log.atl_status = atl_status;
        }
        if (atl_error_msg != null && atl_error_msg.length() > 0) {
            log.atl_error_msg = atl_error_msg;
        }
        HttpSession sess = request.getSession(false);
        if (sess != null) {
            log.atl_session_id = sess.getId();
            loginProfile prof = (loginProfile) sess.getAttribute(qdbAction.AUTH_LOGIN_PROFILE);
            if (prof != null) {
                if (log.atl_usr_ent_id == 0) {
                    log.atl_usr_ent_id = prof.usr_ent_id;
                }
                if (log.atl_usr_ste_usr_id == null || log.atl_usr_ste_usr_id.length() < 1) {
                    log.atl_usr_ste_usr_id = prof.usr_ste_usr_id;
                }
            }
        }
        if (log.atl_start_date != null) {
            long my_time = System.currentTimeMillis();
            log.atl_duration = my_time - log.atl_start_date.getTime();
        }
        log.atl_remote = request.getRemoteAddr();
        log.atl_server = request.getServerName();
        actionlogCacheThread.operateCache(actionlogCacheThread.LOG_ACT_ADD, log);
    }

}
