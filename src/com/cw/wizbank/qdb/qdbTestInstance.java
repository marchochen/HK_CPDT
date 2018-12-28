package com.cw.wizbank.qdb;

import java.sql.*;
import java.util.*;
import java.io.*;

import javax.servlet.http.HttpSession;

import com.cw.wizbank.util.*;
import com.cw.wizbank.db.DbAssessment;
import com.cw.wizbank.db.DbAssessmentEvaluation;
import com.cw.wizbank.db.DbTrackingHistory;
import com.cw.wizbank.report.ExportController;
import com.cw.wizbank.report.LearnerRptExporter;
import com.cwn.wizbank.utils.CommonLog;


public class qdbTestInstance extends Object
{
    public dbModule dbmod;
    public String    usr_id;
    public Timestamp start_time;
    public long      used_time; // in minutes
    public String    pgr_status;
    public float     total_score;
    public float     max_score; // NEW
    public Vector qdbQues; // vector of qdbQueInstance
    public dbProgress pgr ;
    //  for exam item
	public boolean is_score_mark_as_zero;
	public boolean isTerminateExam;
	public String terminate_exam_msg;
	public String error;
	
	private final static String ATTACHMENT_PREFIX = "Attachment ";  
    
    private boolean isEssayMarked = true;
    
    public qdbTestInstance() 
    {
        dbmod = new dbModule();
        qdbQues = new Vector();
    }
    
    private class InnerTestInstance extends qdbTestInstance{
    	int mov_total_attempt;
    	String mov_status;
    	int mov_not_mark_ind;
    }
    
    
    public long markAndSave(Connection con, loginProfile prof, long tkh_id)
        throws qdbException, qdbErrMessage, cwSysMessage, cwException, SQLException,CloneNotSupportedException
    {
        dbModule mod_ = new dbModule() ; 
        mod_.mod_res_id = dbmod.mod_res_id; 
        mod_.get(con); 

        //get the user's entity id
        long usr_ent_id = (prof.usr_id.equals(usr_id)) ? prof.usr_ent_id 
                                                       : dbRegUser.getEntId(con, usr_id);
        
        if (tkh_id == DbTrackingHistory.TKH_ID_UNDEFINED){
        	CommonLog.debug("!!!! get tracking id in qdnTestInstance.markAndSave ");
            tkh_id = DbTrackingHistory.getAppTrackingIDByMod(con, dbmod.mod_res_id, usr_ent_id);
        }
        
        if (mod_.mod_type.equals(dbModule.MOD_TYPE_SVY)){
            //Check if the svy is answered by other
            //if true, remove the progress and progress attempt record
            if( usr_id != null && !prof.usr_id.equalsIgnoreCase(usr_id) ){
            	CommonLog.info("Answered by other ..............");
                dbProgressAttempt dbAtm = new dbProgressAttempt();
                dbAtm.atm_pgr_usr_id = usr_id;
                dbAtm.atm_pgr_res_id = dbmod.mod_res_id;
                dbAtm.atm_tkh_id = tkh_id;
                dbAtm.del(con);

                dbProgress dbPgr = new dbProgress();
                dbPgr.pgr_usr_id = usr_id;
                dbPgr.pgr_res_id = dbmod.mod_res_id;
                dbPgr.pgr_tkh_id = tkh_id;
                dbPgr.del(con);
                
            }
        }
        
        if (mod_.mod_max_attempt > 0 &&
            dbProgress.totalAttemptNum(con, dbmod.mod_res_id) >= mod_.mod_max_attempt)
            throw new qdbErrMessage("PGR004");
        
        if (mod_.mod_max_usr_attempt > 0) { 
            long temp_attempt_num = dbModuleEvaluation.getTotalAttempt(con, dbmod.mod_res_id, usr_ent_id, tkh_id);
            if((temp_attempt_num > mod_.mod_max_usr_attempt) || (temp_attempt_num == mod_.mod_max_usr_attempt && mod_.mod_type.equals(dbModule.MOD_TYPE_SVY))) {
                throw new qdbErrMessage("PGR005");
            } 
        }

        if (mod_.mod_sub_after_passed_ind == 0) {
            dbModuleEvaluation mov = new dbModuleEvaluation();
            mov.mov_ent_id = usr_ent_id;
            mov.mov_tkh_id = tkh_id;
            mov.mov_mod_id = mod_.mod_res_id;
            if(mov.get(con) && mov.mov_status.equals(dbModuleEvaluation.STATUS_PASSED)) {
                throw new qdbErrMessage("PGR008");
            }
        }
        
        boolean isAsm = false;
        if (mod_.mod_type.equals(dbModule.MOD_TYPE_ASM)){
            isAsm = true;    
        }else{
            isAsm = false;    
        }

//System.out.println("is asm type:" + isAsm);
        get(con, isAsm, null);

//        if (!mod_.mod_type.equals(dbModule.MOD_TYPE_SVY)){
            if (isAsm){
                DbAssessment asm = new DbAssessment();
                asm.asm_res_id = dbmod.mod_res_id;
                asm.get(con);
//                System.out.println("mark( con," +asm.asm_neg_score_ind+ " , " + asm.asm_diff_multiplier+")");
                mark(con, asm.asm_neg_score_ind, asm.asm_diff_multiplier, null);
            }else{  
//                System.out.println("mark(con)");                
                mark(con);
            }
//        }
        dbProgress dbp = new dbProgress();
        if(dbProgress.usrAttemptNum(con,dbmod.mod_res_id,prof.usr_id)>0) {
            long lastAtmNbr = dbProgress.getLastAttemptNbr(con, dbmod.mod_res_id, tkh_id);
            long numEssay = dbp.numEssay(con, dbmod.mod_res_id, prof.usr_id, lastAtmNbr);
            boolean isEssayMarked = dbp.isEssayMarked(con, dbmod.mod_res_id, prof.usr_id, lastAtmNbr,tkh_id);
            long numEssayNotMarked = dbp.numEssayNotMarked(con, dbmod.mod_res_id, tkh_id, lastAtmNbr);
                            
            if(numEssay>0) {
                if(numEssayNotMarked > 0) {
                    throw new qdbErrMessage(dbProgress.ESSAY_GRADING_MSG);
                }
            }
        }
        return save(con, prof, isAsm, tkh_id);
    }
    
    private void get(Connection con, boolean isAsm, Hashtable Ques_memory)
    throws qdbException, cwSysMessage,CloneNotSupportedException,SQLException
{
    validate();
    qdbQueInstance q = null;
    HashMap res_time_map = new HashMap(); 
    if(Ques_memory != null && Ques_memory.size()> 0){
        StringBuffer ids = new StringBuffer();
        for(int i=0;i<qdbQues.size();i++){
            q = (qdbQueInstance) qdbQues.elementAt(i); 
            ids.append(q.dbque.que_res_id).append(",");
        }
        ids.append("0");
        String sql = "select res_id, res_upd_date from resources  where res_id in (" + ids + ")";
        PreparedStatement stmt = con.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        while(rs.next()) {
            res_time_map.put(new Long(rs.getLong("res_id")), rs.getTimestamp("res_upd_date"));
        }
        stmt.close();
    }
    
    if(qdbQues.size()>0)
      for(int i=0;i<qdbQues.size();i++)
      {
        q = (qdbQueInstance) qdbQues.elementAt(i);  
        if(Ques_memory != null && Ques_memory.get(new Long(q.dbque.que_res_id)) != null){
            dbQuestion que_temp = (dbQuestion)Ques_memory.get(new Long(q.dbque.que_res_id));
            if(res_time_map.get(new Long(q.dbque.que_res_id)) != null && ((Timestamp)res_time_map.get(new Long(q.dbque.que_res_id))).compareTo(que_temp.res_upd_date)!= 0){
                q.get(con, dbmod.mod_res_id, isAsm); 
            }else{
                q.dbque = (dbQuestion)que_temp.clone();
                q.score_multiplier = 1;
            }
        }else{
            q.get(con, dbmod.mod_res_id, isAsm); 
            if(Ques_memory != null ){
                Ques_memory.put(new Long(q.dbque.que_res_id), q.dbque); 
            }
            
        }
            
      }
}
    
    private void mark(Connection con)
        throws qdbException, cwSysMessage
    {
        mark(con, false, 1, null);
    }
    
    // isAsm , negscore is specially for quiz room module 
    private void mark(Connection con, boolean negScore, int diffMultiplier, ExportController controller)
        throws qdbException, cwSysMessage
    {
        validate();
        qdbQueInstance q = null;
        total_score = 0;
        max_score = 0; // NEW
        
        // check the score indicator of the module 
        dbModule db_mod = new dbModule(); 
        db_mod.mod_res_id = dbmod.mod_res_id ; 
        db_mod.get(con); 
        
        if(qdbQues.size()>0)
          for(int i=0;i<qdbQues.size();i++)
          {
            q = (qdbQueInstance) qdbQues.elementAt(i);  
            // db_mod.mod_score_ind is a retired param
            if(!q.dbque.que_type.equalsIgnoreCase(dbQuestion.QUE_TYPE_ESSAY)&&
               !q.dbque.que_type.equalsIgnoreCase(dbQuestion.QUE_TYPE_ESSAY_2)) {
                q.mark(negScore, diffMultiplier);   
                total_score += q.que_score;
                max_score += q.que_max_score; // NEW
            } else {
                isEssayMarked = false;
                max_score += q.dbque.que_score;
            }
            if (controller != null) {
            	controller.next();
            }
                
          }
    }
    
    // save a marked test instance
    private long save(Connection con, loginProfile prof, boolean isAsm, long tkh_id)
        throws qdbException, cwSysMessage, qdbErrMessage
    {
       validate();
       try {
         dbProgress pgr = new dbProgress();
         dbProgressAttempt atm = null;
         qdbQueInstance q = null;
         int correct_cnt = 0;
         int incorrect_cnt = 0;
         int giveup_cnt = 0;
         dbModule dbMod = new dbModule();
         dbModuleEvaluation dbmov = new dbModuleEvaluation();
          
         pgr.pgr_usr_id = usr_id;
         pgr.pgr_res_id = dbmod.mod_res_id;
         pgr.pgr_tkh_id = tkh_id;
         
         pgr.pgr_score  = total_score;
         pgr.pgr_max_score = max_score; // NEW
         pgr.pgr_start_datetime = start_time;
		 dbMod.mod_res_id = dbmod.mod_res_id;
		 dbMod.get(con);
         if(!isEssayMarked) {
            pgr.pgr_status = dbProgress.PGR_STATUS_NOT_GRADED;
            pgr.pgr_score = 0;
         } else {
             pgr.pgr_status = pgr_status;
			 if((pgr.pgr_score/dbMod.mod_max_score)*100 >= dbMod.mod_pass_score) {
	 			pgr.pgr_completion_status = dbProgress.COMPLETION_STATUS_PASSED;
	 		 } else {
				pgr.pgr_completion_status = dbProgress.COMPLETION_STATUS_FAILED;
			 }
         }
         pgr.ins(con, used_time);
         // do the interactions
         
         if(qdbQues.size()>0)
          for(int i=0;i<qdbQues.size();i++)
          {
            q = (qdbQueInstance) qdbQues.elementAt(i);  
            
            for(int j=0;j<q.atms.size();j++)
            {
                atm = (dbProgressAttempt) q.atms.elementAt(j);
                // check atm_response_bil, if null, skip insert
                //  this is to skip unanswered questions
                //if(atm.atm_response_bil==null)
                //    continue;
                atm.atm_pgr_usr_id = usr_id;
                atm.atm_pgr_res_id = dbmod.mod_res_id;
                atm.atm_tkh_id = tkh_id;
                atm.atm_pgr_attempt_nbr = pgr.pgr_attempt_nbr;
                if(q.dbque.que_type.equalsIgnoreCase(dbQuestion.QUE_TYPE_ESSAY)||
                   q.dbque.que_type.equalsIgnoreCase(dbQuestion.QUE_TYPE_ESSAY_2)) {
                    atm.atm_score = -1;
                    //assume essat tyep question have only one interaction,
                    //so interaction max score = question score.
                    atm.atm_max_score = q.dbque.que_score;
                }
                
                // For dynamic, save the order of question in an attempt
                atm.atm_order = i+1;
                // insert
                atm.ins(con);
                
                if (atm.atm_response_bil != null){
                    if (atm.atm_correct_ind){
                        correct_cnt ++;    
                    }else{
                        incorrect_cnt ++;    
                    }    
                }else{
                    giveup_cnt++;    
                }
                    
            }
          }
          
          if (isAsm){
              DbAssessmentEvaluation asv = new DbAssessmentEvaluation();
              asv.save(con, dbRegUser.getEntId(con, usr_id), dbmod.mod_res_id, correct_cnt, incorrect_cnt, giveup_cnt); 
          }
          
          //added by kim 
          dbmov.mov_cos_id = dbModule.getCosId(con, dbmod.mod_res_id);
          dbmov.mov_ent_id = dbRegUser.getEntId(con, usr_id);
          dbmov.mov_mod_id = dbmod.mod_res_id;
          dbmov.mov_tkh_id = tkh_id;
          if (isAsm){
                dbmov.score_delta = total_score;
          }else{
                dbmov.mov_score = "" + total_score;
          }

          dbmov.mod_time = (cwSQL.getTime(con).getTime() - start_time.getTime())/1000;
          dbmov.mov_create_usr_id = prof.usr_id;
          dbmov.mov_update_usr_id = prof.usr_id;

          // set the status by getting the passing score and compare  
          if (dbMod.mod_type.equals(dbModule.MOD_TYPE_SVY) || dbMod.mod_type.equals(dbModule.MOD_TYPE_EVN)){

            dbmov.mov_status = dbAiccPath.STATUS_COMPLETE;
          }else{            
            if (((float)total_score/(float)max_score)* 100 >= dbMod.mod_pass_score){
                dbmov.mov_status = dbAiccPath.STATUS_PASSED;
            }else{
                dbmov.mov_status = dbAiccPath.STATUS_FAILED;
            }
          }
          if(!isEssayMarked) {
            dbmov.mov_status = dbAiccPath.STATUS_INCOMPLETE;
            dbmov.mov_score = "" + 0;
          } 
          if (!dbMod.mod_type.equals(dbModule.MOD_TYPE_SVY)) {
        	  dbmov.attempt_counted = true;
          }
          dbmov.save(con, prof);
          
          
         // if everything's fine
         con.commit();
		 return pgr.pgr_attempt_nbr;
       } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
       }
    }
    
    private void validate()
        throws qdbException
    {
        if(usr_id==null || usr_id.length()==0)
            throw new qdbException("Invalid user.");
        if(dbmod.mod_res_id==0)
            throw new qdbException("Invalid test id.");
       /* if(qdbQues.size()==0)
            throw new qdbException("Receives no attempt.");*/
    }   
	/**
	 * Move the user answered file(s) to pre-defined directory.
	 * Pre-defined directory: {DOC Root} \ resource \ { MODULE ID } 
	 * \ { USER ENTTIY ID } \ { QUESTION ID } \ { ATTEMPT NO. } 
	 *  
	 */
	public void procUploadedFiles(Connection con, qdbEnv static_env, 
									String tmpUploadDir, Hashtable h_file_filename, long que_id,
									loginProfile prof, long mod_res_id, long attempt_nbr)
		throws IOException {
			
			File savePath = new File(static_env.INI_DIR_UPLOAD, Long.toString(mod_res_id));
			if( !savePath.exists() ) {
				savePath.mkdir();				
			}
			savePath = new File(savePath, Long.toString(prof.usr_ent_id));
			if( !savePath.exists() ) {
				savePath.mkdir();
			}
			
			Enumeration enumeration = h_file_filename.keys();
			while(enumeration.hasMoreElements()){
				String file = (String)enumeration.nextElement();
				String filename = (String) h_file_filename.get(file);
				File fileSavePath = new File(savePath, Long.toString(que_id));
				if( !fileSavePath.exists() ) {
					fileSavePath.mkdir();
				}
				fileSavePath = new File(fileSavePath, Long.toString(attempt_nbr));
				if( !fileSavePath.exists() ) {
					fileSavePath.mkdir();
				}
				String newFilename = constructNewFilename(file, filename);
				dbUtils.moveFile( tmpUploadDir + dbUtils.SLASH + filename,
								fileSavePath.getAbsolutePath() + dbUtils.SLASH + newFilename );

			}
			return;
		}
	
	private String constructNewFilename(String file, String filename){
		int index = Integer.parseInt( file.substring(file.lastIndexOf("_") + 1 ));
		String fileType = filename.substring( filename.lastIndexOf(".") + 1 );
		return ATTACHMENT_PREFIX + index + "." + fileType;
		
	}
    

//  randy add 
     private void get_test(Connection con, boolean isAsm, ExportController controller)
        throws qdbException,SQLException
        {
            validate();
           // qdbQueInstance q = null;
            if(qdbQues.size()>0)
                /*
              for(int i=0;i<qdbQues.size();i++)
              {
                q = (qdbQueInstance) qdbQues.elementAt(i);  
                q.get_test(con, dbmod.mod_res_id, isAsm);     
              }
              */
                qdbQueInstance.get_test( con, qdbQues, controller);
        }
    
     private void mark_test(Connection con, boolean negScore, int diffMultiplier, dbModule db_mod, 
             Hashtable hs_int_score, ExportController controller)
     throws qdbException, cwSysMessage
     {
         validate();
         qdbQueInstance q = null;
         total_score = 0;
         max_score = 0; // NEW

         // check the score indicator of the module 

         
         if(qdbQues.size()>0)
           for(int i=0;i<qdbQues.size();i++)
           {
             q = (qdbQueInstance) qdbQues.elementAt(i);  
             // db_mod.mod_score_ind is a retired param
             if(!q.dbque.que_type.equalsIgnoreCase(dbQuestion.QUE_TYPE_ESSAY)&&
                !q.dbque.que_type.equalsIgnoreCase(dbQuestion.QUE_TYPE_ESSAY_2)) {
                 q.mark(negScore, diffMultiplier, hs_int_score);   
                 total_score += q.que_score;
                 max_score += q.que_max_score; // NEW
             } else {
                 isEssayMarked = false;
                 max_score += q.dbque.que_score;
             }
             if (controller != null) {
                     controller.next();
             }    
  

          }
           
     }
    //    save a marked test instance
        private dbProgress save_test(Connection con, loginProfile prof, boolean isAsm, long tkh_id, long ent_id, dbModule dbMod, ExportController controller, boolean attempt_counted)
            throws qdbException, cwSysMessage
        {
            
            validate();
            try {
              dbProgress pgr = new dbProgress();
              dbProgressAttempt atm = null;
              qdbQueInstance q = null;
              boolean isNotMark = false;
              int correct_cnt = 0;
              int incorrect_cnt = 0;
              int giveup_cnt = 0;
              int not_score_cnt = 0;
              //dbModule dbMod = new dbModule();
              dbModuleEvaluation dbmov = new dbModuleEvaluation();
               
              pgr.pgr_usr_id = usr_id;
              pgr.pgr_res_id = dbmod.mod_res_id;
              pgr.pgr_tkh_id = tkh_id;
              
              dbModule mod = new dbModule();
              mod.mod_res_id = dbmod.mod_res_id;
              mod.getScoreByModResId( con);
              max_score = mod.mod_max_score;
              
              pgr.pgr_score  = total_score;
              pgr.pgr_max_score = max_score; // NEW
              pgr.pgr_start_datetime = start_time;
              if(!isEssayMarked) {
                 pgr.pgr_status = dbProgress.PGR_STATUS_NOT_GRADED;
                 pgr.pgr_score = 0;
              } else {
              pgr.pgr_status = pgr_status;
              if((pgr.pgr_score/dbMod.mod_max_score)*100 >= dbMod.mod_pass_score) {
                 pgr.pgr_completion_status = dbProgress.COMPLETION_STATUS_PASSED;
              } else {
                 pgr.pgr_completion_status = dbProgress.COMPLETION_STATUS_FAILED;
              }
          }

              pgr.ins(con, used_time);
              // do the interactions
              Timestamp curTime = dbUtils.getTime(con);
              if (qdbQues.size() > 0){
                  PreparedStatement stmt = dbProgressAttempt.getInsStmt(con);
                  
                  boolean istrue = false;
                 for (int i = 0; i < qdbQues.size() && !controller.isCancelled(); i++) {
                     q = (qdbQueInstance) qdbQues.elementAt(i);
                     
                     istrue = false;
                     for (int j = 0; j < q.atms.size(); j++) {
                         atm = (dbProgressAttempt) q.atms.elementAt(j);
                         // check atm_response_bil, if null, skip insert
                         // this is to skip unanswered questions
                         // if(atm.atm_response_bil==null)
                         // continue;
                         atm.atm_pgr_usr_id = usr_id;
                         atm.atm_pgr_res_id = dbmod.mod_res_id;
                         atm.atm_tkh_id = tkh_id;
                         atm.atm_pgr_attempt_nbr = pgr.pgr_attempt_nbr;
                         if (q.dbque.que_type.equalsIgnoreCase(dbQuestion.QUE_TYPE_ESSAY) || q.dbque.que_type
                                         .equalsIgnoreCase(dbQuestion.QUE_TYPE_ESSAY_2)) {
                             atm.atm_score = -1;
                             // assume essat tyep question have only one
                             // interaction,
                             // so interaction max score = question score.
                             atm.atm_max_score = q.dbque.que_score;
                             // 如果有问答题的话需要管理员来评分
                             isNotMark = true;
                         }
                         // For dynamic, save the order of question in an attempt
                         atm.atm_order = i + 1;
                         // insert
                         //atm.ins(con);
                         atm.insBatch(con,stmt, curTime);

                         if (atm.atm_response_bil != null && !(q.dbque.que_type.equalsIgnoreCase(dbQuestion.QUE_TYPE_ESSAY) || q.dbque.que_type
                                 .equalsIgnoreCase(dbQuestion.QUE_TYPE_ESSAY_2))) {
                             if (atm.atm_correct_ind) {
                            	 istrue = true;
                             } 
                         }

                     }
                     if (controller != null) {
                         controller.next();
                     }
                     if (q.dbque.que_type.equalsIgnoreCase(dbQuestion.QUE_TYPE_ESSAY) || q.dbque.que_type
                             .equalsIgnoreCase(dbQuestion.QUE_TYPE_ESSAY_2)) {
                    	 not_score_cnt ++; //尚未评分
                     }else if(istrue){ //同一道题目只要有得分就统计到答对题目里面
                    	 correct_cnt++; //答对题目
                     }else{
                    	 incorrect_cnt++; //答错题目
                     }
                 }
                 stmt.executeBatch();
                 stmt.close();
              }
               
              if (isAsm){
                  DbAssessmentEvaluation asv = new DbAssessmentEvaluation();
                  asv.save(con, dbRegUser.getEntId(con, usr_id), dbmod.mod_res_id, correct_cnt, incorrect_cnt, giveup_cnt);
              }
               
               //added by kim 
               dbmov.mov_cos_id = dbModule.getCosId(con, dbmod.mod_res_id);
               dbmov.mov_ent_id = ent_id;// dbRegUser.getEntId(con, usr_id);
               dbmov.mov_mod_id = dbmod.mod_res_id;
               dbmov.mov_tkh_id = tkh_id;
              if (isAsm){
                    dbmov.score_delta = total_score;
              }else{
               dbmov.mov_score = "" + total_score;
              }

              dbmov.mod_time = (cwSQL.getTime(con).getTime() - start_time.getTime())/1000;
              if(dbmov.mod_time < 1){
            	  dbmov.mod_time = 0;
              }
               dbmov.mov_create_usr_id = prof.usr_id;
               dbmov.mov_update_usr_id = prof.usr_id;
     
              // set the status by getting the passing score and compare
             // dbMod.mod_res_id = dbmod.mod_res_id;
             // dbMod.get(con);
              if (dbMod.mod_type.equals(dbModule.MOD_TYPE_SVY)){
                dbmov.mov_status = dbAiccPath.STATUS_COMPLETE;
              }else{
                 if (((float)total_score/(float)max_score)* 100 >= dbMod.mod_pass_score){
                     dbmov.mov_status = dbAiccPath.STATUS_PASSED;
                 }else{
                     dbmov.mov_status = dbAiccPath.STATUS_FAILED;
            }
                 }
           if(!isEssayMarked) {
             dbmov.mov_status = dbAiccPath.STATUS_INCOMPLETE;
             dbmov.mov_score = "" + 0;
           } 
           dbmov.attempt_counted = attempt_counted;
          
          if (dbMod.mod_type.equals(dbModule.MOD_TYPE_TST) || dbMod.mod_type.equals(dbModule.MOD_TYPE_DXT)) {
        	  //如果是测验模块的话就要判断里面是否有问答题
        	  dbmov.save(con, prof, isNotMark);
          } else {
        	  dbmov.save(con, prof);
          }



          //con.rollback();
         // if everything's fine
          con.commit();
          
          pgr.pgr_not_score_cnt = not_score_cnt;
          pgr.pgr_correct_cnt = correct_cnt;
          pgr.pgr_incorrect_cnt = incorrect_cnt + giveup_cnt;
         return pgr;
        } catch(SQLException e) {
            CommonLog.error(e.getMessage(),e);
             throw new qdbException("SQL Error: " + e.getMessage());
        } catch (qdbErrMessage e) {
        	CommonLog.error(e.getMessage(),e);
            throw new qdbException("qdb Error: " + e.getMessage());
		}
     }
    
    private static Map<String, Boolean> operateAppId = new HashMap<String, Boolean>();
    public long markAndSave_test(Connection con, loginProfile prof, long tkh_id, HttpSession sess, ExportController controller)
    throws qdbException, qdbErrMessage, cwSysMessage, cwException, SQLException, CloneNotSupportedException
   {
		while(true){
			//System.out.println("wait");
			synchronized (operateAppId) {
				if(operateAppId.get(String.valueOf(tkh_id)) != null){
					try {
						operateAppId.wait();
						//System.out.println("wait");
					} catch (InterruptedException e) {}
				}else{
					operateAppId.put(String.valueOf(tkh_id), Boolean.TRUE);
					//System.out.println("get lock");
					break;
				}
			}
		}
        validate();
    	long usr_ent_id = (prof.usr_id.equals(usr_id)) ? prof.usr_ent_id
                : dbRegUser.getEntId(con, usr_id);
        InnerTestInstance iti= getCheckTest(con, usr_ent_id, tkh_id);

    	if (tkh_id == DbTrackingHistory.TKH_ID_UNDEFINED){
    		CommonLog.debug("!!!! get tracking id in qdnTestInstance.markAndSave ");
            tkh_id = DbTrackingHistory.getAppTrackingIDByMod(con, dbmod.mod_res_id, usr_ent_id);
        }
        
        if (iti.dbmod.mod_type.equals(dbModule.MOD_TYPE_SVY)) {
            //Check if the svy is answered by other
            //if true, remove the progress and progress attempt record
            if( usr_id != null && !prof.usr_id.equalsIgnoreCase(usr_id) ) {
            	CommonLog.debug("Answered by other ..............");
                dbProgressAttempt dbAtm = new dbProgressAttempt();
                dbAtm.atm_pgr_usr_id = usr_id;
                dbAtm.atm_pgr_res_id = dbmod.mod_res_id;
                dbAtm.atm_tkh_id = tkh_id;
                dbAtm.del(con);
   
                dbProgress dbPgr = new dbProgress();
                dbPgr.pgr_usr_id = usr_id;
                dbPgr.pgr_res_id = dbmod.mod_res_id;
                dbPgr.pgr_tkh_id = tkh_id;
                dbPgr.del(con);              
            }
        }
        
        if (iti.dbmod.mod_max_attempt > 0 && 
                dbProgress.totalAttemptNum(con, dbmod.mod_res_id) >= iti.dbmod.mod_max_attempt) {
            throw new qdbErrMessage("PGR004");
        }
        //iti.mov_total_attempt 获取到的值为该用户已提交的总次数  判断当前提交的是否达到最大次数 故需要 +1
        if (iti.dbmod.mod_max_usr_attempt > 0 && iti.mov_total_attempt + 1 > iti.dbmod.mod_max_usr_attempt) { 
            throw new qdbErrMessage("PGR005");
        }
        
        if (iti.dbmod.mod_sub_after_passed_ind == 0 && iti.mov_status == "P") {
            throw new qdbErrMessage("PGR008");
        }
        
        if (iti.mov_not_mark_ind == 1) {
            throw new qdbErrMessage("PGR011");
        }
        
        boolean isAsm = false;
        if (iti.dbmod.mod_type.equals(dbModule.MOD_TYPE_ASM)) {
            isAsm = true;
        } else {
            isAsm = false;
        }

        get(con, isAsm, qdbAction.Ques_memory);
        
        if (qdbQues.size()>0) {
            controller.setTotalRow(qdbQues.size() * 3 + 1);
        }
        
        Vector vec_test_score = (Vector)sess.getAttribute("test_data");
        sess.removeAttribute("test_data");
        if (vec_test_score == null) {
            throw new qdbErrMessage("PGR012");
        }
        Hashtable HS_Ques = (Hashtable)vec_test_score.get(0);
        Hashtable hs_int_score = (Hashtable)vec_test_score.get(1);
        qdbQueInstance.get_test(qdbQues, HS_Ques);  
   
        if (isAsm) {
            DbAssessment asm = new DbAssessment();
            asm.asm_res_id = dbmod.mod_res_id;
            asm.get(con);

            mark(con, asm.asm_neg_score_ind, asm.asm_diff_multiplier, controller);
        } else {
        mark_test(con, false, 1, iti.dbmod, hs_int_score, controller);
        }

        dbProgress dbp = new dbProgress();

        if (dbp.numEssayNotMarked_test(con, dbmod.mod_res_id, tkh_id) > 0) {
            throw new qdbErrMessage(dbProgress.ESSAY_GRADING_MSG);
        }

         dbProgress pgr = save_test(con, prof, isAsm, tkh_id, prof.usr_ent_id, iti.dbmod, controller, true);
        // in order to make sure the progress reach 100% only when this API ends
        controller.next();
        synchronized (operateAppId) {
        	operateAppId.remove(String.valueOf(tkh_id));
        	operateAppId.notifyAll();
		}
        return pgr.pgr_attempt_nbr;
    }
    
    private InnerTestInstance getCheckTest(Connection con, long usr_ent_id, long tkh_id) throws SQLException {
    	InnerTestInstance iti = new InnerTestInstance();
    	String sql = "SELECT mod_type, mod_max_score, mod_pass_score, mod_instruct, mod_max_attempt, " +
    			"mod_max_usr_attempt, mod_score_ind, mod_score_reset, mod_logic, mod_eff_start_datetime, " +
    			"mod_eff_end_datetime, mod_usr_id_instructor, mod_has_rate_q, mod_is_public, mod_mod_id_root, " +
    			"mod_show_answer_ind, mod_sub_after_passed_ind, mod_mod_res_id_parent, mod_auto_save_ind, " +
    			"mov_total_attempt, mov_status, mov_not_mark_ind " +
    			"FROM Module left join ModuleEvaluation  on (mov_mod_id = mod_res_id AND mov_ent_id = ? " +
    			"AND mov_tkh_id = ?) WHERE mod_res_id = ?";
    	PreparedStatement stmt = con.prepareStatement(sql);
    	int index = 1;
    	stmt.setLong(index++, usr_ent_id);
    	stmt.setLong(index++, tkh_id);
    	stmt.setLong(index++, dbmod.mod_res_id);
    	ResultSet rs = stmt.executeQuery();
    	if (rs.next()) {
    		iti.dbmod.mod_type    = rs.getString("mod_type");
    		iti.dbmod.mod_max_score = rs.getFloat("mod_max_score");
    		iti.dbmod.mod_pass_score = rs.getFloat("mod_pass_score");
    		iti.dbmod.mod_instruct = rs.getString("mod_instruct");
    		iti.dbmod.mod_max_attempt = rs.getLong("mod_max_attempt");
    		iti.dbmod.mod_max_usr_attempt = rs.getLong("mod_max_usr_attempt");
    		iti.dbmod.mod_score_ind = rs.getBoolean("mod_score_ind");
    		iti.dbmod.mod_score_reset  = rs.getLong("mod_score_reset");
    		iti.dbmod.mod_logic = rs.getString("mod_logic");
    		iti.dbmod.mod_eff_start_datetime = rs.getTimestamp("mod_eff_start_datetime");
    		iti.dbmod.mod_eff_end_datetime = rs.getTimestamp("mod_eff_end_datetime");
    		iti.dbmod.mod_usr_id_instructor = rs.getString("mod_usr_id_instructor");
    		iti.dbmod.mod_has_rate_q = rs.getBoolean("mod_has_rate_q");
    		iti.dbmod.mod_is_public = rs.getBoolean("mod_is_public");
    		iti.dbmod.mod_show_answer_ind = rs.getInt("mod_show_answer_ind");
    		iti.dbmod.mod_sub_after_passed_ind = rs.getInt("mod_sub_after_passed_ind");
    		iti.dbmod.mod_mod_id_root = rs.getInt("mod_mod_id_root");
    		iti.dbmod.mod_mod_res_id_parent = rs.getLong("mod_mod_res_id_parent");
    		iti.dbmod.mod_show_save_and_suspend_ind = rs.getInt("mod_auto_save_ind");
    		iti.mov_not_mark_ind = rs.getInt("mov_not_mark_ind");
    		iti.mov_status = rs.getString("mov_status");
    		iti.mov_total_attempt = rs.getInt("mov_total_attempt");
    	}
    	//获取当前用户尝试次数 
        String submit_num_sql = "SELECT res.res_id,(case when att.submit_num is null then atm.submit_num else att.submit_num end) as submit_num"+
					  " from Resources res"+
					  " left join (select pat_prg_res_id, count(pat_att_id) as submit_num from ProgressAttachment"+
					  "           where pat_tkh_id = ?  "+
					  "           group by pat_prg_res_id ) att  on att.pat_prg_res_id = res.res_id"+
					  " left join( select atm_pgr_res_id, max(atm_pgr_attempt_nbr) as submit_num  from ProgressAttempt "+
					  "      where atm_tkh_id = ? "+
					  "      group by atm_pgr_res_id ) atm  on atm.atm_pgr_res_id = res.res_id"+
				      " where res.res_id = ? ";
        PreparedStatement submit_num_stmt = con.prepareStatement(submit_num_sql);
    	int submit_num_index = 1;
    	submit_num_stmt.setLong(submit_num_index++, tkh_id);
    	submit_num_stmt.setLong(submit_num_index++, tkh_id);
    	submit_num_stmt.setLong(submit_num_index++, dbmod.mod_res_id);
    	ResultSet submit_num_rs = submit_num_stmt.executeQuery();
    	if (submit_num_rs.next()) {
    		iti.mov_total_attempt = submit_num_rs.getInt("submit_num");
    	}
    	
    	cwSQL.cleanUp(rs, stmt);
    	cwSQL.cleanUp(submit_num_rs, submit_num_stmt);
    	
    	return iti;
    }
    
    
    
    public static qdbTestInstance markAndSaveMobile(Connection con, loginProfile prof, long tkh_id, long mod_res_id, long[] que_id_lst,long[] que_anwser_option_id_lst,String[] resp_lst, Timestamp start_time)
    throws qdbException, qdbErrMessage, cwSysMessage, cwException, SQLException, CloneNotSupportedException
   {
      //delete the information of auto-save
        dbProgressAttemptSave dbpas = new dbProgressAttemptSave();
        dbpas.pasTkhId = tkh_id;
        dbpas.pasResId = mod_res_id;
        dbpas.del(con);
        dbProgressAttemptSaveAnswer dbpsa = new dbProgressAttemptSaveAnswer();
        dbpsa.psaTkhId = tkh_id;
        dbpsa.psaPgrResId = mod_res_id;
        dbpsa.delAll(con);
        dbResourceContent.DelForDxt(con, mod_res_id, tkh_id);
        
        qdbTestInstance tst = new qdbTestInstance();
        tst.start_time = start_time;
        tst.usr_id = prof.usr_id;
        tst.pgr_status ="OK";
        tst.dbmod.mod_res_id = mod_res_id;
        tst.dbmod.res_id = mod_res_id;
        
        InnerTestInstance iti= tst.getCheckTest(con, prof.usr_ent_id, tkh_id);
        
        tst.dbmod = iti.dbmod;
        tst.dbmod.mod_res_id = mod_res_id;
        tst.dbmod.res_id = mod_res_id;
        
        if (iti.dbmod.mod_max_usr_attempt > 0 && iti.mov_total_attempt > iti.dbmod.mod_max_usr_attempt) { 
        	tst.error = "PGR005";
        	return tst;
        }
        
        if (iti.dbmod.mod_sub_after_passed_ind == 0 && iti.mov_status == "P") {
            tst.error = "PGR008";
        	return tst;
        }
        
        dbProgress dbp = new dbProgress();
        if (dbp.numEssayNotMarked_test(con, mod_res_id, tkh_id) > 0) {
        	tst.error = "PGR011";
        	return tst;
        }
        
        if(que_id_lst == null && que_id_lst.length < 1){
        	tst.error = "PGR111"; //没有题目
        	return tst;
        }
        dbInteraction intr = null;
        for (int i = 0; i < que_id_lst.length; i++) {
            qdbQueInstance q = new qdbQueInstance();
            q.dbque.que_res_id = que_id_lst[i];
            q.dbque.res_id = q.dbque.que_res_id;
            tst.qdbQues.add(q);
            
            long interaction_length = 1;
            if(que_anwser_option_id_lst.length > i){
            	interaction_length = que_anwser_option_id_lst[i];
            }
            for (int m = 0; m < interaction_length; m++) {
	            intr = new dbInteraction();
	            intr.int_res_id = q.dbque.que_res_id;
	            intr.int_order = m + 1;
	            q.dbque.ints.addElement(intr);
	            dbProgressAttempt atm = new dbProgressAttempt();
	            atm.atm_int_res_id = q.dbque.que_res_id;
	            atm.atm_int_order = intr.int_order;
	            if (resp_lst.length > i) {
	                String resp = resp_lst[i];
	                if (resp != null && resp.trim().length() > 0) {
	                    atm.atm_response_bil = resp.trim();
	                    atm.atm_response_bil = atm.atm_response_bil.trim();
	
	                    // Matching type of response
	                    atm.atm_responses = dbUtils.split(resp.trim(), "~");
	
	                    if (atm.atm_responses != null) {
	                        for (int k = 0; k < atm.atm_responses.length; k++) {
	                            if (atm.atm_responses[k] != null && atm.atm_responses[k].trim().length() > 0) {
	                                atm.atm_responses[k] = atm.atm_responses[k].trim();
	                            }
	                        }
	                    }  
	                    
	                    // response of MC in other option
	                    if (atm.atm_responses != null && atm.atm_responses.length > m) {
	                    	atm.atm_response_bil_ext = atm.atm_responses[m];
	                    	atm.atm_response_bil = atm.atm_responses[m];
	    				}
	                }
	            }
	            q.atms.addElement(atm);
            }

        }

        tst.get(con, false, qdbAction.Ques_memory);
        
        //配对题提交的答案需要特殊处理
        tst.handleMT();
        
        tst.mark_test(con, false, 1, iti.dbmod, null, null);
       
        ExportController controller = new ExportController();
        tst.pgr = tst.save_test(con, prof, false, tkh_id, prof.usr_ent_id, iti.dbmod, controller, true);
       


        return tst;
    }
    /**
     * 如果是配对题，移动端的需要做特许处理
     */
	private void handleMT() {
		if(qdbQues.size()>0)
            for(int i=0;i<qdbQues.size();i++){
            	qdbQueInstance q = (qdbQueInstance) qdbQues.elementAt(i);  
            	if(q.dbque.que_type.equalsIgnoreCase(dbQuestion.QUE_TYPE_MATCHING)) {
				  if (q.atms != null) {
					 for (int k = 0; k < q.atms.size(); k++) {
						 dbProgressAttempt atm = (dbProgressAttempt) q.atms.get(k);
						 if (atm.atm_responses.length > k && atm.atm_responses[k] != null && !atm.atm_responses[k].equals("") && atm.atm_responses[k].trim().length() > 0) {
							 atm.atm_response_bil_ext = atm.atm_responses[k].trim();
							 atm.atm_responses = new String[]{atm.atm_responses[k]};
							 atm.atm_response_bil = atm.atm_response_bil_ext.trim() + "~";
						 }
					 }
				 }
			  }
           }
	}

	public dbModule getDbmod() {
		return dbmod;
	}

	public void setDbmod(dbModule dbmod) {
		this.dbmod = dbmod;
	}

	public String getUsr_id() {
		return usr_id;
	}

	public void setUsr_id(String usr_id) {
		this.usr_id = usr_id;
	}

	public Timestamp getStart_time() {
		return start_time;
	}

	public void setStart_time(Timestamp start_time) {
		this.start_time = start_time;
	}

	public long getUsed_time() {
		return used_time;
	}

	public void setUsed_time(long used_time) {
		this.used_time = used_time;
	}

	public String getPgr_status() {
		return pgr_status;
	}

	public void setPgr_status(String pgr_status) {
		this.pgr_status = pgr_status;
	}

	public float getTotal_score() {
		return total_score;
	}

	public void setTotal_score(float total_score) {
		this.total_score = total_score;
	}

	public float getMax_score() {
		return max_score;
	}

	public void setMax_score(float max_score) {
		this.max_score = max_score;
	}

	public Vector getQdbQues() {
		return qdbQues;
	}

	public void setQdbQues(Vector qdbQues) {
		this.qdbQues = qdbQues;
	}

	public dbProgress getPgr() {
		return pgr;
	}

	public void setPgr(dbProgress pgr) {
		this.pgr = pgr;
	}

	public boolean isIs_score_mark_as_zero() {
		return is_score_mark_as_zero;
	}

	public void setIs_score_mark_as_zero(boolean is_score_mark_as_zero) {
		this.is_score_mark_as_zero = is_score_mark_as_zero;
	}

	public boolean isTerminateExam() {
		return isTerminateExam;
	}

	public void setTerminateExam(boolean isTerminateExam) {
		this.isTerminateExam = isTerminateExam;
	}

	public String getTerminate_exam_msg() {
		return terminate_exam_msg;
	}

	public void setTerminate_exam_msg(String terminate_exam_msg) {
		this.terminate_exam_msg = terminate_exam_msg;
	}

	public boolean isEssayMarked() {
		return isEssayMarked;
	}

	public void setEssayMarked(boolean isEssayMarked) {
		this.isEssayMarked = isEssayMarked;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}