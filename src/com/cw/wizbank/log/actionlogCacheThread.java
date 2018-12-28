package com.cw.wizbank.log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cw.wizbank.ScheduledTask;
import com.cwn.wizbank.utils.CommonLog;

public class actionlogCacheThread extends ScheduledTask {
    public static List<actionLog> log_cache = new ArrayList<actionLog>();

    private static final Object ACT_LOCK = new Object();

    public static final String LOG_ACT_ADD = "ADD";

    public static final String LOG_ACT_GET = "GET";

    public void init() {
    }

    protected void process() {
        this.setPriority(Thread.MIN_PRIORITY);
        try {
            con = dbSource.openDB(false);
            List<actionLog> lst = actionlogCacheThread.operateCache(actionlogCacheThread.LOG_ACT_GET, null);
            if (lst != null && lst.size() > 0) {
                actionLogBean.ins(con, lst);
            }
            con.commit();
        } catch (Exception e) {
            logger.debug("actionlogCacheThread.process() error", e);
            CommonLog.error(e.getMessage(),e);
            try {
                if (con != null && !con.isClosed()) {
                    con.rollback();
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            if (this.con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    logger.debug("actionlogCacheThread.process() error", e);
                    CommonLog.error(e.getMessage(),e);
                }
            }
        }
    }

    public static List<actionLog> operateCache(String act, actionLog log) {
        List<actionLog> curent_cache = null;
        synchronized (ACT_LOCK) {
            if (act != null && act.equalsIgnoreCase(LOG_ACT_GET)) {
                if (log_cache != null && log_cache.size() > 0) {
                    curent_cache = log_cache;
                    log_cache = new ArrayList<actionLog>();
                }
            } else {
                if (log != null && log.atl_cmd != null && log.atl_cmd.length() > 0) {
                    if (log_cache == null) {
                        log_cache = new ArrayList<actionLog>();
                    }
                    log_cache.add(log);
                }
            }
        }
        return curent_cache;
    }
}
