package com.cw.wizbank.qdb;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.cw.wizbank.ScheduledTask;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.message.MessageScheduler;
import com.cw.wizbank.report.ExportController;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cwn.wizbank.utils.CommonLog;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class LoadTestScheduler extends ScheduledTask implements Job{

    public LoadTestScheduler(){
        logger = Logger.getLogger(MessageScheduler.class);
    }

    public void init() {
        if (this.param != null) {
            for (int i = 0; i < this.param.size(); i++) {
                
            }   
        }
    }

    protected void process() {
        //this.setPriority(Thread.MIN_PRIORITY);
        try {
            wizbini = WizbiniLoader.getInstance();
            dbSource = new cwSQL();
            dbSource.setParam(wizbini);
            con = dbSource.openDB(false);
            loadTest();
        } catch (Exception e) {
            logger.debug("LoadTestScheduler.process() error", e);
            CommonLog.error(e.getMessage(),e);
        } finally {
            if (this.con != null) {
                try {
                    con.commit();
                    con.close();
                } catch (SQLException e) {
                    logger.debug("LoadTestScheduler.process() error", e);
                    CommonLog.error(e.getMessage(),e);
                }
            }
        }
    }
    

    public void loadTest() throws SQLException, qdbException, IOException, cwSysMessage, cwException {
        List list = getTstId(con);
        Iterator itr = list.iterator();
        while(itr.hasNext()) {
            dbModule mod = new dbModule();
            long res_id = ((Long)itr.next()).longValue();
            Long res_id_key = new Long(res_id);
            try{
                mod.mod_res_id = res_id;
                mod.get(con);
                if (!qdbAction.tests_memory.containsKey(res_id_key)) {
                    qdbAction.tests_memory.put(res_id_key, new TestMemory());
                }
                TestMemory testMemory = (TestMemory)qdbAction.tests_memory.get(res_id_key);
                if (testMemory.hs_tests_score.size() == 0) {
                    ExportController controller = new ExportController();
                    testMemory.beginSetTest(mod, "s1u3", wizbini.getFileUploadResDirAbs(), con,controller);
                }
            }catch (Exception e){
                logger.debug("LoadTest mod_id = "+ res_id +" error : ", e);
            }
            
        }
        return;
    }
    
    public List getTstId(Connection con) throws SQLException {
        List list =  new ArrayList();
        String SQL = "select res_id "                                            
                   + " from resources, aeItem, course, resourcecontent "
                   + " where itm_id = cos_itm_id and cos_res_id = rcn_res_id and rcn_res_id_content =res_id  "                          
                   + " and (? between itm_eff_start_datetime and itm_eff_end_datetime or itm_eff_end_datetime is null)  "             
                   + " and res_status =? and itm_status = ? and res_type = ? "
                   + " and (res_subtype =? or res_subtype = ?) "
                   + " and itm_id in ( "
                   + " select app_itm_id from aeApplication,courseevaluation  "
                   + "  where app_tkh_id = cov_tkh_id and cov_status = 'I' "
                   + " ) "  ;
        int index = 1;
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setTimestamp(index++, cwSQL.getTime(con));
        stmt.setString(index++, dbResource.RES_STATUS_ON);
        stmt.setString(index++, aeItem.ITM_STATUS_ON);
        stmt.setString(index++, dbResource.RES_TYPE_MOD);
        stmt.setString(index++, dbModule.MOD_TYPE_TST);
        stmt.setString(index++, dbModule.MOD_TYPE_DXT);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
        	list.add(new Long(rs.getLong("res_id")));
        }
        rs.close();
        stmt.close();
        con.commit();
        return list;

    }
    

    
    public static void reLoadTest(Connection con, WizbiniLoader wizbini,long re_res_id) throws SQLException, qdbException, IOException, cwSysMessage, cwException {
        List list = getTstChildId(con,  re_res_id);
        Iterator itr = list.iterator();
        while(itr.hasNext()) {
            dbModule mod = new dbModule();
            long res_id = ((Long)itr.next()).longValue();
            Long res_id_key = new Long(res_id);
            if(qdbAction.tests_memory != null){
                qdbAction.tests_memory.remove(res_id_key);
            }
            try{
                mod.mod_res_id = res_id;
                mod.get(con);
                if (!qdbAction.tests_memory.containsKey(res_id_key)) {
                    qdbAction.tests_memory.put(res_id_key, new TestMemory());
                }
                TestMemory testMemory = (TestMemory)qdbAction.tests_memory.get(res_id_key);
                if (testMemory.hs_tests_score.size() == 0) {
                    ExportController controller = new ExportController();
                    testMemory.beginSetTest(mod, "s1u3", wizbini.getFileUploadResDirAbs(), con,controller);
                }
            }catch (Exception e){
                throw new cwSysMessage(("LoadTest mod_id = "+ res_id +" error : "+ e.getMessage()));
            }
            
        }
        return;
    }
    
    private static List getTstChildId(Connection con, long res_id) throws SQLException {
        List list =  new ArrayList();
        String SQL = "select res_id "                                            
                   + " from resources, aeItem, course, resourcecontent, module "
                   + " where itm_id = cos_itm_id and cos_res_id = rcn_res_id and rcn_res_id_content =res_id  and mod_res_id = res_id"                          
           
                   + " and res_status =? and itm_status = ? and res_type = ? "
                   + " and (res_subtype =? or res_subtype = ?) "
                   + " and itm_apply_ind = 1 "
                   + " and (mod_res_id = ? or mod_mod_res_id_parent = ?) "
                 ;
        int index = 1;
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setString(index++, dbResource.RES_STATUS_ON);
        stmt.setString(index++, aeItem.ITM_STATUS_ON);
        stmt.setString(index++, dbResource.RES_TYPE_MOD);
        stmt.setString(index++, dbModule.MOD_TYPE_TST);
        stmt.setString(index++, dbModule.MOD_TYPE_DXT);
        stmt.setLong(index++, res_id);
        stmt.setLong(index++, res_id);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            list.add(new Long(rs.getLong("res_id")));
        }
        rs.close();
        stmt.close();
        con.commit();
        return list;

    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        init();
        process();
    }
}
