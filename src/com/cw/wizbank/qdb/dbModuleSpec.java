package com.cw.wizbank.qdb;

import java.sql.*;
import java.util.*;

import com.cw.wizbank.db.DbQueContainerSpec;
import com.cw.wizbank.db.view.ViewQueContainer;
import com.cw.wizbank.quebank.quecontainer.DynamicScenarioQue;
import com.cw.wizbank.quebank.quecontainer.FixedScenarioQue;
import com.cw.wizbank.report.ExportController;
import com.cw.wizbank.util.*;

public class dbModuleSpec
{
	public long msp_id;
    public long msp_res_id;
    public long msp_obj_id;
    public String msp_type;
    public long msp_score;
    public int msp_difficulty;
    public String msp_privilege;
    public float msp_duration;
    public long msp_qcount;
    public long msp_algorithm;
    
    public String obj_desc;
    
    //全部难度
    public static final String ALL_DIFF_SPEC = "ALL_DIFF_SPEC";
    //指定某个难度
    public static final String SPE_DIFF_SPEC = "SPE_DIFF_SPEC";
    
    public dbModuleSpec() {;}
    
    
    public void save(Connection con)
        throws qdbException, qdbErrMessage, cwSysMessage {
            save(con, true);
            return;
        }
    public void save(Connection con, boolean flag)
        throws qdbException, qdbErrMessage, cwSysMessage
    {
        
        try {  
            dbModule dbmod = new dbModule(); 
            dbmod.res_id = msp_res_id; 
            dbmod.mod_res_id = msp_res_id;
             
            
/*            
            PreparedStatement stmt = con.prepareStatement(
                  " SELECT msp_res_id FROM ModuleSpec WHERE " 
                + "   msp_res_id = ? AND msp_obj_id = ? ");
            
            stmt.setLong(1, msp_res_id);
            stmt.setLong(2, msp_obj_id);
            ResultSet rs = stmt.executeQuery();
*/            
            String SQL = new String();
//            if (rs.next()) {
			if( msp_id > 0 ) {
                SQL = " UPDATE ModuleSpec SET " 
                    + "         msp_type = ? "
                    + "         , msp_score = ? "
                    + "         , msp_difficulty = ? "
                    + "         , msp_privilege = ? "
                    + "         , msp_duration = ? " 
                    + "         , msp_qcount = ? "
                    + "         , msp_algorithm = ? "
                    + "			Where msp_id = ? ";	
                    //+ "     WHERE msp_res_id = ? AND msp_obj_id = ? " ; 
            }else {
                SQL = " INSERT INTO ModuleSpec "
                    + "         ( msp_type "
                    + "         , msp_score "
                    + "         , msp_difficulty "
                    + "         , msp_privilege "
                    + "         , msp_duration " 
                    + "         , msp_qcount "
                    + "         , msp_algorithm "
                    + "         , msp_res_id "
                    + "         , msp_obj_id ) "
                    + "      VALUES (?,?,?,?,?,?,?,?,?) " ;
            }
            //stmt.close();

            PreparedStatement stmt = con.prepareStatement(SQL);
            
            stmt.setString(1, msp_type);
            stmt.setLong(2, msp_score);
            stmt.setInt(3, msp_difficulty);
            stmt.setString(4, msp_privilege);
            stmt.setFloat(5, msp_duration);
            stmt.setLong(6, msp_qcount);
            stmt.setLong(7, msp_algorithm);
            if( msp_id > 0 ) {
				stmt.setLong(8, msp_id);            	
            } else {
	            stmt.setLong(8, msp_res_id);
    	        stmt.setLong(9, msp_obj_id);
            }

            stmt.executeUpdate(); 
            stmt.close();
            
            if(flag)
                dbmod.updMaxScore(con); 
            
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }
        
    }
 
    public String asXML(loginProfile  prof)
    {
        StringBuffer xmlBuf = new StringBuffer();
        String[] qType = null;
        if(msp_type != null) {
            qType = cwUtils.splitToString(msp_type, "~");
        }   
        xmlBuf.append(dbUtils.xmlHeader)
              .append("<modulespec res_id=\"").append(msp_res_id).append("\">").append(dbUtils.NEWL)
              .append(prof.asXML())
              .append("<objective id=\"").append(msp_obj_id)
              /*.append("\" type=\"").append(msp_type)*/
              .append("\" msp_id=\"").append(msp_id)
              .append("\" score=\"").append(msp_score)
              .append("\" difficulty=\"").append(msp_difficulty)
              .append("\" privilege=\"").append(msp_privilege)
              .append("\" duration=\"").append(msp_duration)
              .append("\" q_count=\"").append(msp_qcount)
              .append("\" algorithm=\"").append(msp_algorithm)
              .append("\">")
              .append("<desc>").append(dbUtils.esc4XML(obj_desc)).append("</desc>");
        
        xmlBuf.append("<type_list>");
        if(qType != null && qType.length > 0) {
            for(int i=0; i<qType.length; i++) {
                xmlBuf.append("<type>").append(qType[i]).append("</type>");
            }
        }
        xmlBuf.append("</type_list>");
        xmlBuf.append("</objective>").append(dbUtils.NEWL)
              .append("</modulespec>").append(dbUtils.NEWL);
        
        return xmlBuf.toString();
    }
 
    public void get(Connection con)
        throws qdbException
    {
        try {  
            PreparedStatement stmt = con.prepareStatement(
                "SELECT  "
                + " msp_type "
                + " , msp_score "
                + " , msp_difficulty "
                + " , msp_privilege "
                + " , msp_duration " 
                + " , msp_qcount "
                + " , msp_algorithm "
                + " , obj_desc FROM ModuleSpec, Objective "
                //+ " WHERE msp_res_id = ? and msp_obj_id = ? "
                + " Where msp_id = ?" 
                + "   AND msp_obj_id = obj_id ");
            
            //stmt.setLong(1, msp_res_id);
            //stmt.setLong(2, msp_obj_id);
            stmt.setLong(1, msp_id);

            ResultSet rs = stmt.executeQuery(); 
            if( rs.next())
            {
                msp_type  = rs.getString("msp_type");
                msp_score = rs.getLong("msp_score");
                msp_difficulty = rs.getInt("msp_difficulty"); 
                msp_privilege  = rs.getString("msp_privilege");
                msp_duration  = rs.getFloat("msp_duration");
                msp_qcount = rs.getLong("msp_qcount");
                msp_algorithm = rs.getLong("msp_algorithm");
                obj_desc = rs.getString("obj_desc");
            }else {
            	stmt.close();
                throw new qdbException("Failed to get Module Spec."); 
            }
            stmt.close();
            
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }
    }
    
    /*
    public static  Vector getObjId(Connection con, long modId) 
        throws qdbException, SQLException
    {
        Vector result = new Vector();
        Vector objVec = dbModuleSpec.getModuleSpecs(con,modId) ; 
        Vector objIdVec = new Vector(); 
        dbResourceObjective resObj = new dbResourceObjective(); 

        long objID = 0;
        int i,j, k;
        boolean bExists = false; 
        dbModuleSpec dbmsp = new dbModuleSpec(); 
                
        for(i=0;i<objVec.size();i++)
        {
            dbmsp = (dbModuleSpec) objVec.elementAt(i); 
            objIdVec = dbObjective.getSelfAndChildsObjId(con, dbmsp.msp_obj_id);
            
            for(j=0;j<objIdVec.size();j++)
            {
                objID = ((Long) objIdVec.elementAt(j)).longValue(); 
                bExists = false;
                for(k=0;k<result.size();k++) {
                    if (objID == ((dbResourceObjective)result.elementAt(k)).rob_obj_id)
                        bExists = true;
                }
                if (!bExists) {
                    resObj = new dbResourceObjective(); 
                    resObj.rob_obj_id = objID; 
                    resObj.rob_res_id = modId; 
                    result.addElement(resObj); 
                }
            }
        }

        return result; 
    }
    */
    
    /*
        @return vector of long object
    */
    private Vector matchQue(Connection con, String usrId, String objLst)
        throws qdbException
    {
        try {
            Vector vResId = new Vector();                
            String SQL = "";
                        
            SQL =  " SELECT que_res_id, que_type FROM Question, Resources, ResourceObjective "; 
            SQL +=  "    WHERE que_res_id = res_id ";
            SQL +=  "      and rob_res_id = res_id ";
            SQL +=  "      and res_res_id_root is null " ;
            SQL +=  "      and res_mod_res_id_test is null " ;
            SQL +=  "      and res_status = 'ON' " ;
            
            if ( msp_duration > 0)
                SQL +=  "      and res_duration = ? "; 

            if ( msp_score > 0) 
                SQL +=  "      and que_score = ? " ; 


            if (msp_privilege == null || msp_privilege.length() == 0 ) {
                SQL +=  "      and ( res_privilege ='" + dbResource.RES_PRIV_CW  + "'"  ;
                SQL +=  "      or  res_usr_id_owner ='" + usrId + "' )"  ;
            }else if (msp_privilege.equalsIgnoreCase(dbResource.RES_PRIV_AUTHOR)) {
                SQL +=  "      and  res_privilege ='" + dbResource.RES_PRIV_AUTHOR + "'"  ;
                SQL +=  "      and  res_usr_id_owner ='" + usrId + "'"  ;
            }else {
                SQL +=  "      and  res_privilege ='" + dbResource.RES_PRIV_CW + "'"  ;
            }
            
            String[] qType = null;
            if (msp_type != null && msp_type.length() != 0 ) {
                qType = cwUtils.splitToString(msp_type, "~");
                //SQL +=  "      and que_type ='"  + msp_type + "'";
                SQL += " and que_type IN ( ? ";
                for(int i=1; i<qType.length; i++)
                    SQL += " ,? ";
                SQL += " ) ";
            }
            
            if (msp_difficulty >= 1 && msp_difficulty <= 3)
                SQL +=  "      and res_difficulty ="  + msp_difficulty ;
                            
            SQL +=  "      and rob_obj_id IN " + objLst  ;

            PreparedStatement stmt = con.prepareStatement(SQL); 
            int index = 1;
            if ( msp_duration > 0){
            	stmt.setFloat(index++, msp_duration);
            }
            if ( msp_score > 0) {
            	stmt.setLong(index++, msp_score);
            }
            if( qType != null ) {
                for(int i=0; i<qType.length; i++)
                    stmt.setString(index++, qType[i]);
            }
            ResultSet rs = stmt.executeQuery(); 
            
            Vector vTempQueId = new Vector();
            while (rs.next()){
                //check num of sub-que if question is a dsc question
                String que_type = rs.getString("que_type");
                long que_id = rs.getLong("que_res_id");
                if (que_type.equals(dbResource.RES_SUBTYPE_DSC)) {
                    DynamicScenarioQue dsq = new DynamicScenarioQue();
                    dsq.res_id = que_id;
                    try {
                        vTempQueId = dsq.getChildQueId(con);
                    }
                    catch (cwSysMessage e) {
                        //do nothing,just ignore this id
                    }
                    if (vTempQueId.size() > 0) {
                        vResId.addElement(new Long(que_id));
                    }
                }
                else {
                    vResId.addElement(new Long(que_id));
                }
            }
            stmt.close();
            return vResId; 
            
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }
    }
    
    public long matchQueNum(Connection con, String usrId, Vector qArray, String objLst)
    throws qdbException, qdbErrMessage
    {
            Vector vResId = matchQue(con,usrId, objLst); 
            int i = 0; 
            long queId = 0; 
            for (int k=0; k<vResId.size(); k++){
                if (!qArray.contains(vResId.elementAt(k)))
                    i++;
            }
                
            return i; 
    }
    
    public long matchQueNum(Connection con, String usrId, String objLst)
    	throws qdbException, qdbErrMessage, cwSysMessage, cwException
    {
            Vector vResId = matchQue(con, usrId, objLst);             
            return vResId.size(); 
    }
    
    // Append more question to the current question list
    /*
    public Vector genQue(Connection con, String usrId, Vector curQArray)
        throws qdbException, qdbErrMessage
    {
        // CL (17 Dec, 2001)
        String objLst = dbObjective.getSelfAndChildsObjIdLst(con,msp_obj_id); 
        
        Vector qArray = genQue(con, usrId, curQArray, objLst);
        return qArray;
    }
    */
    
    // Append more question to the current question list
    public Vector genQue(Connection con, String usrId, Vector curQArray, String objLst)
        throws qdbException, qdbErrMessage
    {
        int org_size = curQArray.size(); 

        Vector qArray = curQArray;
            Vector tmpArray = matchQue(con,usrId, objLst); 

            if (tmpArray.size() >= msp_qcount) {
                
                int i, number;
                Long qid; 
                for(i=0;i<msp_qcount;i++) {
                	
                	if (tmpArray.size() == 0) {
        				//没有足够的题目
        				throw new qdbErrMessage("MSP001");
        			}
                    number = (int)(Math.random()*tmpArray.size()) ; 
                    qid = (Long) tmpArray.elementAt(number); 
                    
                    if (qArray.contains(qid)) {
                            i--; 
                    }else {
                        qArray.addElement(qid); 
                    }
                    tmpArray.remove(qid);
                }
            }

            if ((qArray.size()- org_size)!= Long.valueOf(msp_qcount).intValue()) {
                //Not enough questions fulfill the requirements.
                throw new qdbErrMessage("MSP001"); 
            }
            
            return qArray; 
    }
    
    
    public static Vector genSTXDynQue(Connection con, dbModule dbmod, dbModuleSpec STXmsp, String[] robs, String ownerId, String usrId)
        throws qdbException, qdbErrMessage
    {
        Vector qArray  = new Vector();
        
        Vector objVec = new Vector();

        for (int i=0;i<robs.length;i++) {
            // if robs[i] is syllabus objective
            Vector assVec = dbObjective.getSelfAndChildsObjId(con, Long.parseLong(robs[i]));
            for(int j=0;j<assVec.size();j++) {
                 Long objId = (Long) assVec.elementAt(j);
                 objVec.addElement(objId);
            }
        }
        
        //String objLst = dbUtils.array2list(robs);
        String objLst = dbUtils.vec2list(objVec);
        qArray = STXmsp.genQue(con, ownerId, qArray, objLst);
        if (qArray == null) {
            throw new qdbErrMessage("MSP001");
        }
        
        qArray = randomQue(qArray);
        
        return qArray;

        
    }
    
    public static Vector genDynQue(Connection con, dbModule dbmod, String ownerId, String usrId)
        throws qdbException, qdbErrMessage, SQLException
    {

        dbModuleSpec  dbmsp = new dbModuleSpec();
        dbModuleSpec  dbmsp_new = new dbModuleSpec();
        Vector conVec_tmp = null;
        if (dbmod.mod_mod_res_id_parent > 0) {
            conVec_tmp = dbModuleSpec.getModuleSpecs(con, dbmod.mod_mod_res_id_parent);
        } else {
            conVec_tmp = dbModuleSpec.getModuleSpecs(con, dbmod.mod_res_id);
        }
        Vector conVec = new Vector();
        Vector objVec = new Vector();
        Vector tmpVec = new Vector();
        int i,j,k;
        String objLst = "";
        String SQL = "";
        boolean bExits = false;
        
        // Order the criteria by the level of each objective DESC
        Vector levelVec_tmp = new Vector();
        int max_level = 0;
        for(i=0; i< conVec_tmp.size(); i++) {
            dbmsp = (dbModuleSpec) conVec_tmp.elementAt(i);
            int level = dbObjective.getObjLevel(con, dbmsp.msp_obj_id); 
            levelVec_tmp.addElement(new Integer(level));
            if (level > max_level)
                max_level = level;
        }
        
        for(i=max_level;i>=0;i--) {
            for(j=0;j<levelVec_tmp.size();j++) {
                int level = ((Integer) (levelVec_tmp.elementAt(j))).intValue();
                if (level == i) {
                    conVec.addElement(conVec_tmp.elementAt(j));
                }
            }
        }
        
        Vector qArray  = null;

        if (dbmod.mod_logic != null && 
                dbmod.mod_logic.equalsIgnoreCase(dbModule.MOD_LOGIC_ADT)) {
                
                qArray = dbModuleSpec.drawQue(con, conVec, dbmod, ownerId, usrId,  true);
        }
        
        // For dynamic test which is not adaptive OR
        // Adaptive dynamic test which is failed to get enough question using the algorithm
        if (qArray == null)
            qArray = dbModuleSpec.drawQue(con, conVec, dbmod, ownerId, usrId,  false);
        
        if (qArray == null) {
            throw new qdbErrMessage("MSP001");
        }
        
//        qArray = randomQue(qArray);
        
        return qArray;
    }

    private static Vector randomQue(Vector qArray)
    {
        if (qArray == null || qArray.size() == 0) 
            return qArray;
        
        Vector QueArray = new Vector();
        int number = 0;
        
        for (int i=0; i< 1000;i++) {
            number = (int)(Math.random()*qArray.size());
            if (number == qArray.size()) 
                number = qArray.size() -1 ;
                
            Long qId = (Long) qArray.elementAt(number); 
            if ( !QueArray.contains(qId))
                QueArray.addElement(qId); 
            
            // Finished
            if (QueArray.size() == qArray.size())
                break;
        }
        
        // Failed to randomize after 1000 trial
        if (QueArray.size() < qArray.size()) {
            for (int i=0;i<qArray.size();i++) {
                Long qId = (Long) qArray.elementAt(i); 
                if (!QueArray.contains(qId))
                QueArray.addElement(qId); 
            }
        }
        
        // Still failed to randomize the array , return original
        if (QueArray.size() != qArray.size()) 
            QueArray = qArray; 
        
        return QueArray; 
    }
    
    private static Vector drawQue(Connection con, Vector conVec , dbModule dbmod, String ownerId, String usrId, boolean adaptive)
        throws qdbException 
    {
        try {
            Vector qArray = new Vector();
            dbModuleSpec dbmsp = null;
            String objLst = new String();
            
            // For each objective , generate the specified number of question
            for(int i=0; i< conVec.size(); i++)
            {
                dbmsp = (dbModuleSpec) conVec.elementAt(i);
                    
                // Adaptive
                if (adaptive) {
                    Vector mspVec = dbModuleSpec.applyLogic(con,dbmod,ownerId, usrId, dbmsp, qArray);
                    dbmsp = (dbModuleSpec) mspVec.elementAt(0);
                }
                    
                objLst = dbObjective.getSelfAndChildsObjIdLst(con,dbmsp.msp_obj_id);
                qArray = dbmsp.genQue(con, ownerId, qArray, objLst);
            }
                
            return qArray;
        }catch (qdbErrMessage e) {
            return null;
        }
    }
    
    public static Hashtable drawQue(Connection con, Vector conVec , dbModule dbmod, String usrId, boolean adaptive, String uploadDir)
    	throws qdbException, cwSysMessage, cwException ,qdbErrMessage
	{
	    //try {
	    	//保存每个生成题目的类型
	    	Hashtable queType = new Hashtable();
	    	//最终返回的map
	    	Hashtable dynQue = new Hashtable();
	    	dynQue.put(ALL_DIFF_SPEC, new ArrayList());
	    	dynQue.put(SPE_DIFF_SPEC, new ArrayList());
	    	
	    	//保存所生成题目的id
	    	Vector qArray = null;
	        Map specHas = null;
	        dbModuleSpec dbmsp = null;
	        
	        // For each objective , generate the specified number of question
	        for(int i = 0; i < conVec.size(); i++)
	        {
	        	specHas = new HashMap();
	            dbmsp = (dbModuleSpec) conVec.elementAt(i);
	                
	            // Adaptive
	            if (adaptive) {
	                Vector mspVec = dbModuleSpec.applyLogic(con, dbmod, dbmod.res_usr_id_owner, usrId, dbmsp);
	                dbmsp = (dbModuleSpec) mspVec.elementAt(0);
	            }                
	            qArray = dbmsp.getQue(con, dbmod.res_id, uploadDir, usrId);
	            specHas.put("ids", qArray);
	            specHas.put("qcount", new Long(dbmsp.msp_qcount));
	            if (dbmsp.msp_difficulty > 0) {
	            	//指定难度的条件
	            	((List)dynQue.get(SPE_DIFF_SPEC)).add(specHas);
	            } else {
	            	//全部难度的条件
	            	((List)dynQue.get(ALL_DIFF_SPEC)).add(specHas);
	            }
	        }
	        return dynQue;
//	    }catch (qdbErrMessage e) {
//	        return null;
//	    }
	}

    private static Vector applyLogic(Connection con, dbModule dbmod, String ownerId, String usrId, dbModuleSpec dbmsp, Vector qArray)
        throws qdbException, qdbErrMessage
    {
        Vector mspVec = new Vector();

        int i, percentage, pass_percentage; 
        
        pass_percentage = new Float(dbmod.mod_pass_score).intValue();
        
        int weight = 0;

        percentage = QStat.percentageByObj(con, dbmod.mod_res_id, usrId, dbmsp.msp_obj_id); 

        // depend on the percentage, set the weight

        if (percentage < 0) {
            weight = 0;
        }else if (percentage < (pass_percentage * 0.5))  {
            weight = -2;
        }else if (percentage < (pass_percentage * 0.8))  {
            weight = -1;
        }else if (percentage > (pass_percentage + (100-pass_percentage)*0.8)) {
            weight = 3;
        }else if (percentage > (pass_percentage + (100-pass_percentage)*0.6)) {
            weight = 2;
        }else if (percentage > (pass_percentage + (100-pass_percentage)*0.3)) {
            weight = 1;
        }else {
            weight = 0;
        }

        dbModuleSpec dbmsp_new = new dbModuleSpec(); 
        dbmsp_new.msp_res_id = dbmsp.msp_res_id;
        dbmsp_new.msp_obj_id = dbmsp.msp_obj_id;
        dbmsp_new.msp_id = dbmsp.msp_id;
        dbmsp_new.get(con);

        
        if (weight != 0)        // Search Condition unchange
        {
            dbmsp_new.msp_difficulty += weight;
            if (dbmsp_new.msp_difficulty <1)
                dbmsp_new.msp_difficulty = 1; 
            else if (dbmsp_new.msp_difficulty >3)
                dbmsp_new.msp_difficulty = 3;
        }

        String objLst = dbObjective.getSelfAndChildsObjIdLst(con,dbmsp.msp_obj_id);
        if (dbmsp_new.matchQueNum(con, ownerId, qArray, objLst) >= dbmsp_new.msp_qcount) {
            mspVec.addElement(dbmsp_new);
        }else {
            mspVec.addElement(dbmsp);
        }

        return mspVec;
    }
    
    private static Vector applyLogic(Connection con, dbModule dbmod, String ownerId, String usrId, dbModuleSpec dbmsp)
    	throws qdbException, qdbErrMessage, cwSysMessage, cwException
	{
	    Vector mspVec = new Vector();
	
	    int percentage, pass_percentage; 
	    
	    pass_percentage = new Float(dbmod.mod_pass_score).intValue();
	    
	    int weight = 0;
	
	    percentage = QStat.percentageByObj(con, dbmod.mod_res_id, usrId, dbmsp.msp_obj_id); 
	
	    // depend on the percentage, set the weight
	
	    if (percentage < 0) {
	        weight = 0;
	    }else if (percentage < (pass_percentage * 0.5))  {
	        weight = -2;
	    }else if (percentage < (pass_percentage * 0.8))  {
	        weight = -1;
	    }else if (percentage > (pass_percentage + (100-pass_percentage)*0.8)) {
	        weight = 3;
	    }else if (percentage > (pass_percentage + (100-pass_percentage)*0.6)) {
	        weight = 2;
	    }else if (percentage > (pass_percentage + (100-pass_percentage)*0.3)) {
	        weight = 1;
	    }else {
	        weight = 0;
	    }
	
	    dbModuleSpec dbmsp_new = new dbModuleSpec(); 
	    dbmsp_new.msp_res_id = dbmsp.msp_res_id;
	    dbmsp_new.msp_obj_id = dbmsp.msp_obj_id;
	    dbmsp_new.msp_id = dbmsp.msp_id;
	    dbmsp_new.get(con);
	
	    
	    if (weight != 0)        // Search Condition unchange
	    {
	        dbmsp_new.msp_difficulty += weight;
	        if (dbmsp_new.msp_difficulty <1)
	            dbmsp_new.msp_difficulty = 1; 
	        else if (dbmsp_new.msp_difficulty >3)
	            dbmsp_new.msp_difficulty = 3;
	    }
	
	    String objLst = dbObjective.getSelfAndChildsObjIdLst(con,dbmsp.msp_obj_id);
	    if (dbmsp_new.matchQueNum(con, ownerId, objLst) >= dbmsp_new.msp_qcount) {
	        mspVec.addElement(dbmsp_new);
	    }else {
	        mspVec.addElement(dbmsp);
	    }
	
	    return mspVec;
	}
    
    /**
     * Get module spec records of the module
     * @param module id
     * @return Vector of dbModuleSpce class
     */
    public static Vector getModuleSpecs(Connection con, long modId)
        throws SQLException
    {
        Vector result = new Vector();
        
        PreparedStatement stmt = con.prepareStatement(
            " SELECT msp_id, msp_obj_id "
                + " , msp_type "
                + " , msp_score "
                + " , msp_difficulty "
                + " , msp_privilege "
                + " , msp_duration " 
                + " , msp_qcount "
                + " , msp_algorithm  FROM ModuleSpec "
                + " WHERE msp_res_id = ? "); 
            
        stmt.setLong(1,modId);

        ResultSet rs = stmt.executeQuery(); 
        while (rs.next()) {
            dbModuleSpec dbmsp = new dbModuleSpec();
            dbmsp.msp_id = rs.getLong("msp_id"); 
            dbmsp.msp_res_id = modId; 
            dbmsp.msp_obj_id = rs.getLong("msp_obj_id"); 
            dbmsp.msp_type = rs.getString("msp_type"); 
            dbmsp.msp_score = rs.getLong("msp_score"); 
            dbmsp.msp_difficulty = rs.getInt("msp_difficulty"); 
            dbmsp.msp_privilege = rs.getString("msp_privilege"); 
            dbmsp.msp_duration = rs.getFloat("msp_duration"); 
            dbmsp.msp_qcount = rs.getLong("msp_qcount"); 
            dbmsp.msp_algorithm = rs.getLong("msp_algorithm"); 
                
            result.addElement(dbmsp); 
        }
        stmt.close();
        return result; 

    }
    
    /**
    * Delete ModuleSpecs from the module    
    */
    public void delMsp(Connection con, Vector objIdVec)
        throws SQLException, cwException {
            
            String SQL = " DELETE FROM ModuleSpec "
                       + " WHERE msp_res_id = ? AND msp_obj_id IN " +cwUtils.vector2list(objIdVec);
            
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, msp_res_id);
            stmt.executeUpdate();
            stmt.close();
            return;
        }

    /**
    Get the number of defined specification of the input resource
    @param con Connection to database
    @msp_res_id resource id of the input resource
    @return number of defomed specification of the input resource
    */    
    static int getModuleSpecCount(Connection con, long msp_res_id) throws SQLException {
        PreparedStatement stmt = null;
        int count = 0;
        try {
        	String sql = " Select count(*) CNT From ModuleSpec, Module Where " +
        			" msp_res_id = " + cwSQL.replaceNull("mod_mod_res_id_parent", "mod_res_id") +
        			" And mod_res_id = ? ";
            stmt = con.prepareStatement(sql);
            stmt.setLong(1, msp_res_id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                count = rs.getInt("CNT");
            }
        } finally {
            if(stmt!=null) {stmt.close();}
        }
        return count;
    }

    public void get_from_id(Connection con) throws qdbException {
        try {
            PreparedStatement stmt =
                con.prepareStatement(
                    "SELECT  "
                        + " msp_res_id "
                        + " , msp_obj_id "
                        + " , msp_type "
                        + " , msp_score "
                        + " , msp_difficulty "
                        + " , msp_privilege "
                        + " , msp_duration "
                        + " , msp_qcount "
                        + " , msp_algorithm FROM ModuleSpec "
                        + " WHERE msp_id = ? ");

            stmt.setLong(1, msp_id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                msp_res_id = rs.getLong("msp_res_id");
                msp_obj_id = rs.getLong("msp_obj_id");
                msp_type = rs.getString("msp_type");
                msp_score = rs.getLong("msp_score");
                msp_difficulty = rs.getInt("msp_difficulty");
                msp_privilege = rs.getString("msp_privilege");
                msp_duration = rs.getFloat("msp_duration");
                msp_qcount = rs.getLong("msp_qcount");
                msp_algorithm = rs.getLong("msp_algorithm");
            } else {
                throw new qdbException("Failed to get Module Spec.");
            }
            stmt.close();

        } catch (SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }
    
    public static Vector getByResId(Connection con, long id) throws SQLException {
        Vector vtModSpec = new Vector();
        PreparedStatement stmt = con.prepareStatement("SELECT  " 
                                + " msp_obj_id " 
                                + " , msp_type " 
                                + " , msp_score " 
                                + " , msp_difficulty " 
                                + " , msp_privilege " 
                                + " , msp_duration " 
                                + " , msp_qcount " 
                                + " , msp_algorithm FROM ModuleSpec " 
                                + " WHERE msp_res_id = ? ");
        stmt.setLong(1, id);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            dbModuleSpec dbModSpec = new dbModuleSpec();
            dbModSpec.msp_obj_id = rs.getLong("msp_obj_id");
            dbModSpec.msp_type = rs.getString("msp_type");
            dbModSpec.msp_score = rs.getLong("msp_score");
            dbModSpec.msp_difficulty = rs.getInt("msp_difficulty");
            dbModSpec.msp_privilege = rs.getString("msp_privilege");
            dbModSpec.msp_duration = rs.getFloat("msp_duration");
            dbModSpec.msp_qcount = rs.getLong("msp_qcount");
            dbModSpec.msp_algorithm = rs.getLong("msp_algorithm");
            vtModSpec.addElement(dbModSpec);
        }
        cwSQL.cleanUp(rs, stmt);
        return vtModSpec;
    }
    
    public static Hashtable genDynQue_test(Connection con, dbModule dbmod, String usrId, String uploadDir, ExportController controller) 
    	throws SQLException, qdbException, cwSysMessage, qdbErrMessage, cwException 	
    {
    	Hashtable dynId = new Hashtable();
    	dbModuleSpec  dbmsp = new dbModuleSpec();
    	Vector conVec_tmp = null;
    	if (dbmod.mod_mod_res_id_parent > 0) {
            conVec_tmp = dbModuleSpec.getModuleSpecs(con, dbmod.mod_mod_res_id_parent);
        } else {
            conVec_tmp = dbModuleSpec.getModuleSpecs(con, dbmod.mod_res_id);
        }
    	controller.currentRow = 3;
    	
    	Vector conVec = new Vector();
        int i,j;   
        //把抽题条件按照资源文件夹的层数由深到浅重新排序
        Vector levelVec_tmp = new Vector();
        int max_level = 0;
        for(i = 0; i < conVec_tmp.size(); i++) {
            dbmsp = (dbModuleSpec)conVec_tmp.get(i);
            int level = dbObjective.getObjLevel(con, dbmsp.msp_obj_id); 
            levelVec_tmp.add(new Integer(level));
            if (level > max_level)
                max_level = level;
        }
        controller.currentRow =5;
        for(i = max_level; i >= 0; i--) {
            for(j = 0; j < levelVec_tmp.size(); j++) {
                int level = ((Integer)(levelVec_tmp.get(j))).intValue();
                if (level == i) {
                    conVec.add(conVec_tmp.get(j));
                }
            }
        }
        controller.currentRow = 7;
        Hashtable dynQue  = null;
        Random random = new Random();
        if (dbmod.mod_logic != null && dbmod.mod_logic.equalsIgnoreCase(dbModule.MOD_LOGIC_ADT)) {    
        	dynQue = dbModuleSpec.drawQue(con, conVec, dbmod, usrId, true, uploadDir);
        }  
        controller.currentRow = 10;
        //随机性抽题
        if (dynQue == null) {
        	/*
    	 		dynQue包含所有符合条件的题目id
    	 		key: ALL_DIFF_SPEC  value: 为全部难度条件的题目  
    	 		key: SPE_DIFF_SPEC  value: 为指定难度条件的题目
    	    */
        	dynQue = dbModuleSpec.drawQue(con, conVec, dbmod, usrId, false, uploadDir);
        	for (i = 1; i <= 100; i++) {
        		//生成一份题目的id
        		dynId.put(new Integer(i), getDynId(dynQue, random));
        	}
        } else {
        	//适应性测验只生成一份题
        	dynId.put(new Integer(1), getDynId(dynQue, random));
        }    
        controller.currentRow = 15;
    	return dynId;
    }
    
    public Vector getQue(Connection con, long modId, String uploadDir, String usrId) 
    	throws qdbErrMessage, qdbException, cwSysMessage, cwException 
    {      
        //返回所有符合该条件的题目id
        Vector tmpArray = matchQue(con, modId, uploadDir, usrId); 
        if (tmpArray.size() < msp_qcount) {
        	throw new qdbErrMessage("MSP001"); 
        }      
        return tmpArray; 
    }
    
    public Vector matchQue(Connection con, long modId, String uploadDir, String usrId)
    	throws qdbException, cwSysMessage, cwException
	{
	    try {
	        Vector vResId = new Vector();    
	        Vector robVec = null;
	        Vector vTempQueId = new Vector();
	        Vector tmp = null;
	        Hashtable dynQue = new Hashtable();
	        StringBuffer sql = new StringBuffer();
	        sql.append("SELECT p_res.res_id as p_res_id , p_res.res_lan as p_res_lan, p_res.res_title as p_res_title, ")
	           .append("p_res.res_desc as p_res_desc, p_res.res_type as p_res_type, p_res.res_subtype as p_res_subtype, ")
	           .append("p_res.res_annotation as p_res_annotation, p_res.res_format as p_res_format, ")
	           .append("p_res.res_difficulty as p_res_difficulty, p_res.res_duration as p_res_duration, ")
	           .append("p_res.res_privilege as p_res_privilege, p_res.res_status as p_res_status, ")
	           .append("p_res.res_usr_id_owner as p_res_usr_id_owner, p_res.res_create_date as p_res_create_date, ")
	           .append("p_res.res_tpl_name as p_res_tpl_name, p_res.res_res_id_root as p_res_res_id_root, ")
	           .append("p_res.res_mod_res_id_test as p_res_mod_res_id_test, p_res.res_upd_user as p_res_upd_user, ")
	           .append("p_res.res_upd_date as p_res_upd_date, p_res.res_src_type as p_res_src_type, ")
	           .append("p_res.res_src_link as p_res_src_link, p_res.res_instructor_name as p_res_instructor_name, ")
	           .append("p_res.res_instructor_organization as p_res_instructor_organization, que_xml, que_score, ")
	           .append("que_type, que_int_count, que_prog_lang, que_media_ind, que_submit_file_ind, int_res_id, ")
	           .append("int_label, int_order, int_xml_outcome, int_xml_explain, int_res_id_explain, int_res_id_refer, ")
	           .append("c_res.res_id as child_res_id, qcs_id ");
	        sql.append("FROM Objective ")
	           .append("inner join ResourceObjective on ((obj_ancester like ? or obj_id = ?) and obj_id = rob_obj_id) ")
	           .append("inner join Resources p_res on (rob_res_id = p_res.res_id) ")
	           .append("inner join Question on (que_res_id = p_res.res_id and que_score = ? and que_type = ?");
	        if (this.msp_difficulty >= 1 && this.msp_difficulty <= 3) {
	        	sql.append("  and res_difficulty = ?)");
	        }else {
	        	sql.append("  and res_difficulty in (1,2,3))");
	        }
	        sql.append(" left join Interaction on (p_res.res_id = int_res_id)")
	           .append(" left join QueContainerSpec on (qcs_res_id = p_res.res_id)")
	           .append(" left join Resources c_res on")
	           .append(" (p_res.res_id = c_res.res_res_id_root and c_res.res_mod_res_id_test is null and c_res.res_upd_date = p_res.res_upd_date)");
	        sql.append(" WHERE p_res.res_res_id_root is null")
	           .append(" and p_res.res_mod_res_id_test is null")
	           .append(" and p_res.res_status = 'ON'")
	           .append(" and  p_res.res_privilege ='CW'");
	
	        PreparedStatement stmt = con.prepareStatement(sql.toString());
	        int index = 1;
	        stmt.setString(index++, "% " + msp_obj_id + " %");
	        stmt.setLong(index++, msp_obj_id);
	        stmt.setLong(index++, msp_score);
	        stmt.setString(index++, msp_type);
	        if (this.msp_difficulty >= 1 && this.msp_difficulty<= 3) {
	        	stmt.setInt(index++, msp_difficulty);
	        }
	        ResultSet rs = stmt.executeQuery();   
	        dbQuestion dbq = null;
	        long QueId;
	        Long que_id_obj, qcsId;
	        while (rs.next()){
	            tmp = new Vector();	
	            QueId = rs.getLong("p_res_id");
	            que_id_obj = new Long(QueId);
	            if(!dynQue.containsKey(que_id_obj)){	            	
		            dbq = new dbQuestion();
		            tmp.add(dbq);
		            tmp.add(new Long(rs.getLong("child_res_id")));	            
		            vTempQueId.addElement(que_id_obj);
		            dynQue.put(que_id_obj, tmp);
			        dbq.res_id = QueId;
			        dbq.que_res_id = QueId;
			        dbq.res_lan = rs.getString("p_res_lan");
			        dbq.res_title = rs.getString("p_res_title");
			        dbq.res_desc = rs.getString("p_res_desc");
			        dbq.res_type = rs.getString("p_res_type");
			        dbq.res_subtype = rs.getString("p_res_subtype");
			        dbq.res_annotation = rs.getString("p_res_annotation");
			        dbq.res_format = rs.getString("p_res_format");
			        dbq.res_difficulty = rs.getInt("p_res_difficulty");
			        dbq.res_duration = rs.getFloat("p_res_duration");
			        dbq.res_privilege = rs.getString("p_res_privilege");
			        dbq.res_status = rs.getString("p_res_status");
			        dbq.res_usr_id_owner = rs.getString("p_res_usr_id_owner");
			        dbq.res_create_date = rs.getTimestamp("p_res_create_date");
			        dbq.res_tpl_name = rs.getString("p_res_tpl_name");
			        dbq.res_res_id_root = rs.getLong("p_res_res_id_root");
			        dbq.res_mod_res_id_test = 0;
			        dbq.res_upd_user = rs.getString("p_res_upd_user");
			        dbq.res_upd_date = rs.getTimestamp("p_res_upd_date");
			        dbq.res_src_type = rs.getString("p_res_src_type");
			        dbq.res_src_link = rs.getString("p_res_src_link");
			        dbq.res_instructor_name = rs.getString("p_res_instructor_name");
			        dbq.res_instructor_organization = rs.getString("p_res_instructor_organization");
			        dbq.que_xml = cwSQL.getClobValue(rs, "que_xml");
			        dbq.que_score = rs.getInt("que_score");
			        dbq.que_type = rs.getString("que_type");
			        dbq.que_int_count = rs.getInt("que_int_count");
			        dbq.que_prog_lang = rs.getString("que_prog_lang");
			        dbq.que_media_ind = rs.getBoolean("que_media_ind");
			        dbq.que_submit_file_ind = rs.getBoolean("que_submit_file_ind");
			        dbq.qcs_id.add(new Long(rs.getLong("qcs_id")));
	            } else {
	            	dbq = (dbQuestion)((Vector)dynQue.get(que_id_obj)).get(0);
	            	qcsId = new Long(rs.getLong("qcs_id"));
	            	if (!dbq.qcs_id.contains(qcsId)) {
	            		dbq.qcs_id.add(qcsId);
	            	}
	            }
		           
		        if( !dbq.que_type.equalsIgnoreCase(dbResource.RES_SUBTYPE_FSC) && !dbq.que_type.equalsIgnoreCase(dbResource.RES_SUBTYPE_DSC) ) {
		            dbInteraction intObj = new dbInteraction();
		            intObj.int_res_id = rs.getLong("int_res_id");
		            intObj.int_label = rs.getString("int_label");
		            intObj.int_order = rs.getInt("int_order");
		            intObj.int_xml_outcome = cwSQL.getClobValue(rs, "int_xml_outcome");
		            intObj.int_xml_explain = cwSQL.getClobValue(rs, "int_xml_explain");
		            intObj.int_res_id_explain = rs.getLong("int_res_id_explain");
		            intObj.int_res_id_refer = rs.getLong("int_res_id_refer");
		            dbq.ints.add(intObj);
		        }          
	        }

	        for (int i = 0; i < vTempQueId.size(); i++) {
	        	tmp = (Vector)dynQue.get(vTempQueId.get(i));
	        	dbq = (dbQuestion)tmp.get(0);
	        	long tmpId = dbq.res_id;
	        	long child_res_id = ((Long)tmp.get(1)).longValue();
	        	long qcs_id;
	        	if (child_res_id == 0) {
	        	    robVec = new Vector();
                    //Do not update ResourceContent And add ResourceObjective same as parent.
                    robVec = dbResourceObjective.getObjId(con, tmpId); 
                    String[] robs = new String[1];
                    for(int j = 0; j < robVec.size(); j++) {
                        robs[j] = Long.toString(((dbResourceObjective) robVec.get(j)).rob_obj_id);
                    }
                    //复制主题目
                    dbq.ins(con, robs, dbResource.RES_TYPE_QUE);                 
                    
                    PreparedStatement stmt1 = con.prepareStatement(
                        " UPDATE Resources "
                    +  "   SET res_res_id_root = ? , "
                    +  "       res_upd_date = ? "
                    +  "  WHERE res_id  = ? " );
                    index = 1;
                    stmt1.setLong(index++, tmpId);
                    stmt1.setTimestamp(index++, dbq.res_upd_date);
                    stmt1.setLong(index++, dbq.que_res_id);
        
                    if (stmt1.executeUpdate() != 1) {
                        stmt1.close();
                        con.rollback();
                        throw new qdbException("Failed to update the resouce.");
                    }
                    stmt1.close();
                    if(uploadDir != null) {
                    	//复制图片
                        dbUtils.copyMediaFrom(uploadDir, tmpId, dbq.res_id);  
                    }
                    
                    vResId.add(new Long(dbq.res_id));
                    
                    if (dbq.que_type.equalsIgnoreCase(dbResource.RES_SUBTYPE_FSC) || 
                    	dbq.que_type.equalsIgnoreCase(dbResource.RES_SUBTYPE_DSC)) {
                    	Vector v_que_id = null; 
                    	long _que_res_id;
                    	FixedScenarioQue srcFsq = new FixedScenarioQue();
                    	//tmpId为还没复制前的res_id
                        srcFsq.res_id = tmpId;
                        v_que_id = srcFsq.getChildQueId(con);
                        dbResourceContent _rcn = null;
                        for(int j = 0; j < v_que_id.size(); j++){
                        	//复制所有的子题目
                        	_que_res_id = dbQuestion.duplicateDynQ(con, ((Long)v_que_id.get(j)).longValue(), uploadDir, null, dbq.res_id, false);
                           //关联复制后的主题目与子题目的关系
                           _rcn = new dbResourceContent();
                           _rcn.rcn_res_id = dbq.res_id;
                           _rcn.rcn_res_id_content = _que_res_id;
                           _rcn.rcn_order = (j+1);
                           _rcn.rcn_sub_nbr = 0;//rcn.rcn_order;
                           _rcn.rcn_score_multiplier = 1;
                           _rcn.ins(con);
                       }
                       if (dbq.que_type.equalsIgnoreCase(dbResource.RES_SUBTYPE_DSC)) {
                    	   //复制动态情景题的抽题条件
                           DbQueContainerSpec qcs = new DbQueContainerSpec();
                           for (int j = 0; j < dbq.qcs_id.size(); j++) {
	                           qcs.qcs_id = ((Long)dbq.qcs_id.get(j)).longValue();
	                           qcs.get(con);
	                           qcs.qcs_res_id = dbq.res_id;
	                           qcs.ins(con, dbq.que_type, usrId);
                           }
                       } else {
                    	   //复制静态情景题是否打乱子题目的记录
                           ViewQueContainer vqc = new ViewQueContainer();
                           vqc.res_id = tmpId;
                           vqc.get(con);
                           vqc.res_id = dbq.res_id;
                           vqc.ins(con);
                       }                           
                    }    
	        	} else {
	        		vResId.add(new Long(child_res_id));
	        	}
	        }
	        stmt.close();
	        return vResId; 
	        
	    } catch(SQLException e) {
	        throw new qdbException("SQL Error: " + e.getMessage()); 
	    }
	}
    
    /**
     * 生成一份题目的id
     * @throws qdbErrMessage 
     */
    private static Vector getDynId(Hashtable dynQue, Random random) throws cwException, qdbErrMessage {
    	Vector ids = new Vector();
    	List tempLst = null;
    	Map specMap = null;
    	int i;
    	
    	//先抽指定难度条件的题目，再抽全部难度条件的题目
    	tempLst = (List)dynQue.get(SPE_DIFF_SPEC);
    	for (i = 0; i < tempLst.size(); i++) {
    		specMap = (Map)tempLst.get(i);
    		setId(specMap, ids, random);
    	}
    	
    	tempLst = (List)dynQue.get(ALL_DIFF_SPEC);
    	for (i = 0; i < tempLst.size(); i++) {
    		specMap = (Map)tempLst.get(i);
    		setId(specMap, ids, random);
    	}
    	return ids;
    }
    
    private static void setId(Map specMap, Vector ids, Random random) throws cwException, qdbErrMessage {
    	List tmp = new ArrayList();
    	tmp.addAll((List)specMap.get("ids"));
		long count = ((Long)specMap.get("qcount")).longValue();
		randomGetQueId(tmp, count, random, ids);
    }
    
    /**
     * 随机抽取题目id
     * @param vec 该条件所符合的所有题目id
     * @param count 该条件的抽题数
     * @param ids 抽取出来的题目id
     */
    private static void randomGetQueId(List vec, long count, Random random, List ids) throws cwException, qdbErrMessage {
		if( count > vec.size() ){
			throw new cwException("Required object more than vector size.");
		}
		int index;
		Long tempId;
		for(int i=0; i<count; i++){
			if (vec.size() == 0) {
				//没有足够的题目
				throw new qdbErrMessage("MSP001");
			}
			index = random.nextInt(vec.size());
			tempId = (Long)vec.get(index);
			if (ids.contains(tempId)) {
				i--;
			} else {
				ids.add(vec.get(index));
			}
			vec.remove(index);
		}
    }
}    