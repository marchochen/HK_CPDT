package com.cw.wizbank.qdb;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Random;
import java.util.Vector;

import com.cw.wizbank.report.ExportController;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;

public class TestMemory {
	public Hashtable hs_tests_score = new Hashtable();
	public Timestamp updateTimeStamp;
	public boolean generated = false;
	public static final String MOD_TYPE_DXT = "DXT";
	public static final String MOD_TYPE_TST = "TST";
    public static final String MOD_TYPE_STX = "STX";
	
    public synchronized void beginSetTest(dbModule dbmod, String usrId, String uploadDir, Connection con, ExportController controller) 
    	throws SQLException, cwSysMessage, qdbException, qdbErrMessage, cwException 
    {
    	//得到同步锁之后再判断一次，预防等待同步锁时别人已经生成测验了
    	if (this.hs_tests_score.size() == 0) {
			setTest(dbmod, usrId, uploadDir, con, controller);
		}
    }
    
    public synchronized void reSetTest(dbModule dbmod, Timestamp updateTimeStamp, String usrId, String uploadDir, Connection con, ExportController controller) 
    	throws SQLException, cwSysMessage, qdbException, qdbErrMessage, cwException 
    {
    	//得到同步锁之后再判断一次，预防等待同步锁时别人已经生成测验了
    	if (!updateTimeStamp.equals(this.updateTimeStamp)) {
			setTest(dbmod, usrId, uploadDir, con, controller);
		}
   }
    
    public void setTest(dbModule dbmod, String usrId, String uploadDir, Connection con, ExportController controller) throws SQLException, cwSysMessage, qdbException, qdbErrMessage, cwException {
        this.updateTimeStamp = null;
        Vector vec_test_score = null;
    	Vector idsVec = null;
    	int index = 1;
    	if (dbmod.mod_type.equalsIgnoreCase(MOD_TYPE_TST)) {	
    		vec_test_score = dbmod.getQue_test(con, null, null);
			this.hs_tests_score.put(new Integer(index), vec_test_score);
			controller.currentRow = 5;
		}else if (dbmod.mod_type.equalsIgnoreCase(MOD_TYPE_DXT)){
			Hashtable dynId = dbModuleSpec.genDynQue_test(con, dbmod, usrId, uploadDir, controller);
			Enumeration ids = dynId.keys();		
			Random random = new Random();
			//根据生成的id生成对应的dbq对象
			while (ids.hasMoreElements()) {
				idsVec = (Vector)dynId.get(ids.nextElement());
				vec_test_score = dbmod.getQue_test(con, idsVec, random);
				this.hs_tests_score.put(new Integer(index), vec_test_score);
				index++;
				if(index%3 ==1 && controller.currentRow < 50){
				    controller.currentRow++ ;
				}
			}	
		}
    	//模块更新时间
    	dbResource dbr = new dbResource();
		dbr.res_id = dbmod.mod_res_id;
		this.updateTimeStamp = dbr.getUpdateTimeStamp(con);
		this.generated = true;
    }
}
