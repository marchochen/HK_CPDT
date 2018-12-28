package com.cw.wizbank.competency;

import java.sql.*;
import java.util.*;
import java.io.IOException;

import javax.servlet.http.HttpSession;

import com.cw.wizbank.qdb.dbResource;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbProgress;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.util.*;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.db.*;
import com.cw.wizbank.db.view.*;
import com.cwn.wizbank.utils.CommonLog;

public class CmAssessmentManager{
    
    private static final String COMMA               =   ",";
    private static final String SKILL_SET           =   "SKILL_SET";
    private static final String SKILL               =   "SKILL";
    private static final String HASH_TIMESTAMP      =   "page_timestamp";
    private static final String HASH_ASM_ID_VEC     =   "asm_id_vec";
    private static final String HASH_ORDERBY        =   "order_by";
    private static final String HASH_SORTBY         =   "sort_by";
    private static final String HASH_NUM_OF_USER    =   "num_of_user";
    private static final String HASH_TOTAL_WEIGHT   =   "total_weight";
    
    private static final String HASH_PREPARED       =   "prepared";
    private static final String HASH_COLLECTED      =   "collected";
    private static final String HASH_NOTIFIED       =   "notified";
    private static final String HASH_RESOLVED       =   "resolved";
    
    private static final String ASSESSMENT_RESULT   =   "ASR";
    private static final String SKILL_SECTION       =   "SCT";
    
    private static final String MANAGEMENT          =   "MGMT";
    private static final String PEERS               =   "PEERS";
    private static final String REPORTS             =   "REPORTS";
    private static final String CLIENTS             =   "CLIENTS";
    private static final String SELF                =   "SELF";
    private static final String RESOLVED            =   "RESOLVED";
    private static final String DELETED             =   "DELETED";
    
    public long asm_id = 0;
    
    /*
    public CmAssessmentManager(long asm_id) {
        this.asm_id = asm_id;
        return;
    }
    */
    /**
    Pcik a Rator to Assess the Assessment 
    @param skb_id the array of the skill id
    @param flag true : insert a user in list and delete user not in list, false : insert user in list
    @return row count of INSERT
    */
    public int pickRator(Connection con, loginProfile prof
                        ,DbCmAssessmentUnit[] DbAu, boolean flag
                        ,String sender_usr_id, String notify_msg_subject
                        ,String collect_msg_subject)
        throws SQLException, cwException, cwSysMessage {

            if( DbAu == null || DbAu.length == 0 )
                return 0;
            for(int i=0; i<DbAu.length; i++)
                DbAu[i].asu_asm_id = asm_id;
                
            if(flag) {
                long[] ids = new long[DbAu.length];
                for(int i=0; i<DbAu.length; i++)
                    ids[i] = DbAu[i].asu_ent_id;
                DbAu[0].delNotInList(con, ids);
            }
            
            Hashtable hMsgXtpTable = new Hashtable();
            CmAssessmentNotify assNot = new CmAssessmentNotify();            
            int count = 0;
            for(int i=0; i<DbAu.length; i++) {
                if(!DbAu[i].exist(con))
                    if(DbAu[i].ins(con) == 1){
                        //insert messaging records
                        Hashtable msgXtpTable = (Hashtable)hMsgXtpTable.get(DbAu[i].asu_type);
                        if(msgXtpTable == null) {
                            msgXtpTable = ViewCmAssessment.getMsgTemplateId(con, asm_id, DbAu[i].asu_type);
                            if(msgXtpTable == null || msgXtpTable.size() == 0) {
                                assNot.insNotification(con, asm_id, DbAu[i].asu_type, sender_usr_id, Timestamp.valueOf(cwUtils.MAX_TIMESTAMP), notify_msg_subject, false, prof);
                                assNot.insCollectionNotification(con, asm_id, DbAu[i].asu_type, sender_usr_id, Timestamp.valueOf(cwUtils.MAX_TIMESTAMP), collect_msg_subject, false, prof);
                                msgXtpTable = ViewCmAssessment.getMsgTemplateId(con, asm_id, DbAu[i].asu_type);
                            }
                            hMsgXtpTable.put(DbAu[i].asu_type, msgXtpTable);
                        }
                        assNot.insNotificationRecipient(con, msgXtpTable, DbAu[i].asu_ent_id);
                        count++;
                    }
            }
            return count;
        }

    
    
    
    
    
    
    
    
    /*
    Get the rator result
    @param asu_ent_id array of the rator entity id
    @return vector of class array which contains the latest answer of the rator
    vector size is equal to the size of the asu_ent_id array
    
    public Vector getRatorsResult(Connection con, long[] asu_ent_id)
        throws SQLException, cwSysMessage {            
            
            Vector vec = new Vector();
            DbCmSkillSetCoverage[] DbSsc = null;
            DbCmAssessmentUnit DbAssU = new DbCmAssessmentUnit();
            DbAssU.asu_asm_id = asm_id;
            long asu_sks_id;
            for(int i=0; i<asu_ent_id.length; i++) {
                DbAssU.asu_ent_id = asu_ent_id[i];
                asu_sks_id = DbAssU.getLstestSkillSetId(con);
                if(asu_sks_id != 0) {
                    ViewCmSkillCoverage Vcsc = new ViewCmSkillCoverage();
                    DbSsc = Vcsc.getAllSkillsSetCoverage(con, asu_sks_id);
                } else {
                    DbSsc = new DbCmSkillSetCoverage[0];
                }
                vec.addElement(DbSsc);
            }            
            return vec;
        }
    */








    /*
    Calculate the average of the specified rators
    @param asu_ent_id array of the rator entity id
    @return DbCmSkillSetCoverage array
    
    public DbCmSkillSetCoverage[] getRatorsAveResult(Connection con, long[] asu_ent_id)
        throws SQLException, cwSysMessage {
            int numSsc = 0;
            DbCmAssessment DbAss = new DbCmAssessment();
            DbAss.asm_id = asm_id;
            DbAss.get(con);
            ViewCmSkillCoverage Vcsc = new ViewCmSkillCoverage();
            DbCmSkillSetCoverage[] DbSsc = Vcsc.getAllSkillsSetCoverage(con, DbAss.asm_sks_skb_id);
                        
            Vector vec = getRatorsResult(con, asu_ent_id);
            for(int i=0; i<vec.size(); i++) {
                DbCmSkillSetCoverage[] _DbSsc = (DbCmSkillSetCoverage[])vec.elementAt(i);
                if(_DbSsc.length == 0)
                    vec.remove(i--);                
            }
            
            int size = vec.size();
            DbCmSkillSetCoverage[][] DbSscResult = new DbCmSkillSetCoverage[size+1][];
            for(int i=0; i<size; i++)
                DbSscResult[i] = (DbCmSkillSetCoverage[])vec.elementAt(i);                        
            
            for(int i=0; i<DbSsc.length; i++) {
                if(DbSsc[i].skb_type.equalsIgnoreCase(SKILL_SET)) {
                    DbSscResult[size+1][i].skb_type = SKILL_SET;
                    continue;
                }
                float ave = 0;
                StringBuffer response = new StringBuffer().append("<response>").append(cwUtils.NEWL);
                for(int j=0; j<size; j++) {
                    ave += DbSscResult[j][i].ssc_level;
                    response.append(DbSscResult[j][i].ssc_xml).append(cwUtils.NEWL);
                }
                response.append(cwUtils.NEWL);
                DbSscResult[size+1][i].skb_type = SKILL;
                DbSscResult[size+1][i].ssc_level = ave / size;
                DbSscResult[size+1][i].ssc_xml = response.toString();                
            }
            
            return DbSscResult[size+1];
        }
    */




    /**
    Construct a XML of the assessment by getting data from database
    @return String of XML
    */
    public String getAssessmentXML(Connection con)
        throws SQLException, cwSysMessage, cwException {

        return getAssessmentXML(con, (loginProfile)null);
    }
    


    /**
    Construct a XML of the assessment by getting data from database
    if prof is not null, will vendor a <reference> xml segment that shows the 
    reference data for the user described in the prof object
    @param prof wizBank login profile
    @return String of XML
    */
    public String getAssessmentXML(Connection con, loginProfile prof)
        throws SQLException, cwSysMessage, cwException {
            DbCmAssessment DbAss = new DbCmAssessment();
            DbAss.asm_id = asm_id;
            DbAss.get(con);

            DbCmAssessmentUnit DbAssU = new DbCmAssessmentUnit();
            DbAssU.asu_asm_id = asm_id;
            
            DbCmSkillSet skillSet = new DbCmSkillSet();
            skillSet.sks_skb_id = DbAss.asm_sks_skb_id;
            skillSet.get(con);
            
            dbRegUser dbUsr = new dbRegUser();
            dbUsr.usr_ent_id = DbAss.asm_ent_id;
            try{
                dbUsr.get(con);
            }catch(qdbException e) {
                throw new cwException(e.getMessage());
            }
            
            Timestamp curTime = cwSQL.getTime(con);
            
            StringBuffer xml = new StringBuffer();
            xml.append("<assessment id=\"").append(asm_id).append("\" ")
               .append(" ent_id=\"").append(DbAss.asm_ent_id).append("\" ")
               .append(" usr_id=\"").append(dbUsr.usr_id).append("\" ")
               .append(" display_bil=\"").append(cwUtils.esc4XML(dbUsr.usr_display_bil)).append("\" ")
               .append(" title=\"").append(cwUtils.esc4XML(DbAss.asm_title)).append("\" ")
               .append(" sks_id=\"").append(DbAss.asm_sks_skb_id).append("\" ")
               .append(" sks_title=\"").append(cwUtils.esc4XML(skillSet.sks_title)).append("\" ")
               .append(" review_start=\"").append(DbAss.asm_review_start_datetime).append("\" ")
               .append(" review_end=\"").append(DbAss.asm_review_end_datetime).append("\" ")
               .append(" due_datetime=\"").append(DbAss.asm_eff_end_datetime).append("\" ")
               .append(" notification_datetime=\"").append(DbAss.asm_eff_start_datetime).append("\" ")
               .append(" num_assign=\"").append((DbAssU.getAuEntityId(con)).size() - 1).append("\" ")
               .append(" num_collected=\"").append(DbAssU.numberOfSubmit(con, true)).append("\" ")
               .append(" last_upd_timestamp=\"").append(DbAss.asm_update_timestamp).append("\" ")
               .append(" auto_resolved_ind=\"").append(DbAss.asm_auto_resolved_ind).append("\" ");
            
            if( DbAss.asm_status != null && DbAss.asm_status.equalsIgnoreCase("Resolved") ) {
                xml.append(" status=\"Resolved\" ");
                //Get Resolved user display bil and resolved time
                Vector v = ViewCmAssessment.getResolvedUserNTime(con, asm_id);
                xml.append(" last_resolve_user=\"").append((String)v.elementAt(0)).append("\" ")
                   .append(" last_resolve_timestamp=\"").append((Timestamp)v.elementAt(1)).append("\" ");

            }
            else if( curTime.before(DbAss.asm_eff_start_datetime) )
                xml.append(" status=\"Prepared\" ");
            else if( curTime.after(DbAss.asm_eff_start_datetime) && curTime.before(DbAss.asm_eff_end_datetime) )
                xml.append(" status=\"Notified\" ");
            else if( curTime.after(DbAss.asm_eff_end_datetime) )
                xml.append(" status=\"Collected\" ");

            xml.append(" />").append(cwUtils.NEWL);            
            
            return xml.toString();
            
        }

    /**
    Construct a XML of the Survey by getting data from session
    @return String of XML
    @deprecated This method is no longer updated since the introduction of applet tree to insert/update of Assessment
    */
    public String getAssessmentXML(Connection con, HttpSession sess)
        throws SQLException, cwSysMessage, cwException {
            
            StringBuffer xml = new StringBuffer();
            Hashtable data = null;
            long ent_id = 0;
            long sks_id = 0;
            String asm_title = null;
            Timestamp review_start_datetime = null;
            Timestamp review_end_datetime = null;
            Timestamp notification_datetime = null;
            Timestamp due_datetime = null;
            if( sess != null )
                data = (Hashtable)sess.getAttribute("Competency_ASSESSMENT");
            if( data != null ){

                ent_id = ((Long)data.get(CompetencyModule.HASH_ENTITY_ID)).longValue();
                sks_id = ((Long)data.get(CompetencyModule.HASH_SURVEY_ID)).longValue();
                asm_title = (String)data.get(CompetencyModule.HASH_ASSESSMENT_TITLE);
                
                
                if( data.get(CompetencyModule.HASH_REVIEW_START_DATETIME) != null)
                    review_start_datetime = (Timestamp)data.get(CompetencyModule.HASH_REVIEW_START_DATETIME);
                    
                
                if( data.get(CompetencyModule.HASH_REVIEW_END_DATETIME) != null )
                    review_end_datetime = (Timestamp)data.get(CompetencyModule.HASH_REVIEW_END_DATETIME);
                    
                
                if( data.get(CompetencyModule.HASH_NOTIFICATION_DATETIME) != null )
                    notification_datetime = (Timestamp)data.get(CompetencyModule.HASH_NOTIFICATION_DATETIME);
                
                
                if( (Timestamp)data.get(CompetencyModule.HASH_DUE_DATETIME) != null )
                    due_datetime = (Timestamp)data.get(CompetencyModule.HASH_DUE_DATETIME);
            }

            dbRegUser dbUsr = null;
            if( ent_id  > 0 ) {
                dbUsr = new dbRegUser();
                dbUsr.usr_ent_id = ent_id;
                try{
                    dbUsr.get(con);
                }catch(qdbException e) {
                    throw new cwException(e.getMessage());
                }
            }

            DbCmSkillSet skillSet = null;
            if( sks_id > 0 ) {
                skillSet = new DbCmSkillSet();
                skillSet.sks_skb_id = sks_id;
                skillSet.get(con);
            }


            xml.append("<assessment id=\"").append(asm_id).append("\" ");
            if( ent_id  > 0 ) {
                xml.append(" ent_id=\"").append(ent_id).append("\" ")
                   .append(" usr_id=\"").append(dbUsr.usr_id).append("\" ")
                   .append(" display_bil=\"").append(cwUtils.esc4XML(dbUsr.usr_display_bil)).append("\" "); 
            }
            else {
                xml.append(" ent_id=\"\" ")
                   .append(" usr_id=\"\" ")
                   .append(" display_bil=\"\" ");
            } 
            if( asm_title != null && asm_title.length() > 0 )
                xml.append(" title=\"").append(cwUtils.esc4XML(asm_title)).append("\" ");
            else
                xml.append(" title=\"\" ");
            
            if( sks_id > 0 ) {
                xml.append(" sks_id=\"").append(sks_id).append("\" ")
                   .append(" sks_title=\"").append(cwUtils.esc4XML(skillSet.sks_title)).append("\" ");
            }
            else
                xml.append(" sks_id=\"\" sks_title=\"\" ");
                
            if( review_start_datetime != null )
                xml.append(" review_start=\"").append(review_start_datetime).append("\" ");
            else
                xml.append(" review_start=\"\" ");
            
            if( review_end_datetime != null )
                xml.append(" review_end=\"").append(review_end_datetime).append("\" ");
            else
                xml.append(" review_end=\"\" ");
                
            if( due_datetime != null )   
                xml.append(" due_datetime=\"").append(due_datetime).append("\" ");
            else
                xml.append(" due_datetime=\"\" ");
            
            if( notification_datetime != null )
                xml.append(" notification_datetime=\"").append(notification_datetime).append("\" ");
            else
                xml.append(" notification_datetime=\"\" ");
               
            xml.append(" />").append(cwUtils.NEWL);            
            
            
            return xml.toString();
        }

    private static int GET_REFRESH_ASS_LIST_STATUS_NO_SESSION = 5;
    private static int GET_REFRESH_ASS_LIST_STATUS_CHANGE_PAGE = 1;
    private static int GET_REFRESH_ASS_LIST_STATUS_CHANGE_SORT_ORDER = 2;
    private static int GET_REFRESH_ASS_LIST_STATUS_CHANGE_ORDERING_COLUMN = 3;
    private static int GET_REFRESH_ASS_LIST_STATUS_CHANGE_FILTER = 4;
    /*  change page :
            from session
            same filtering
            same ordering column
            same sort order
            might changed cur page            
            -- use same set of idVec as sessIdVec
            -- only need to select this page info (ids = this page info)
        
        change sort order:
            from session
            same filtering
            same ordering column
            reverse sort order
            -- use reverse sessIdVec to idVec
            -- only need to select this page info (ids = this page info)
            -- cur_page = 1

        change ordering column:
            from session
            same filtering
            different ordering column
            -- use sessIdVec to limit the result set
            -- reselect all records and put as new sessIdVec
            -- cur_page = 1
            -- sort = 'desc'
        change filter :
            from session
            different filtering
            -- same as no session
            
        no session : 
            -- reselect all records
            -- cur_page = 1
        
    */
    
    /**
    Get assessment list
    @param pagesize : number of assessment show in a page
    @param sort by
    @param order by
    @return string of xml
    */     
    public String getRefreshedAssListAsXML(Connection con, HttpSession sess, boolean prepared, boolean collected, boolean notified, boolean resolved, int cur_page, String order_by, String sort_by, int pagesize, Timestamp pagetime, long root_ent_id, boolean hasTc,String tcr_id_lst)
        throws SQLException, cwException {
            
        Timestamp curTime = cwSQL.getTime(con);
        int list_status = 0;
        String status;
        Vector idVec = new Vector();
        Vector sessIdvec = null;
        StringBuffer xmlBody = new StringBuffer();
        
        ViewCmAssessment Vass = new ViewCmAssessment();
        ResultSet rs=null;

        Hashtable sessData = (Hashtable)sess.getAttribute("Competency_ASS_LIST");
        if (sessData==null){
            list_status = GET_REFRESH_ASS_LIST_STATUS_NO_SESSION;
        }else{
            Timestamp sessPagetime = (Timestamp)sessData.get(HASH_TIMESTAMP);
            if (!sessPagetime.equals(pagetime)){
//                System.out.println("time not match.");
                list_status = GET_REFRESH_ASS_LIST_STATUS_NO_SESSION;
            }else{
                String sessOrderby = (String)sessData.get(HASH_ORDERBY);
                String sessSortby = (String)sessData.get(HASH_SORTBY);
                Boolean sessPrepared = (Boolean)sessData.get(HASH_PREPARED);
                Boolean sessCollected = (Boolean)sessData.get(HASH_COLLECTED);
                Boolean sessNotified = (Boolean)sessData.get(HASH_NOTIFIED);
                Boolean sessResolved = (Boolean)sessData.get(HASH_RESOLVED);
                    
                if (sessPrepared.booleanValue()== prepared 
                    && sessCollected.booleanValue()== collected 
                    && sessNotified.booleanValue()== notified 
                    && sessResolved.booleanValue()== resolved ){
                    
                    if (sessOrderby.equalsIgnoreCase(order_by)){
                        if (sessSortby.equalsIgnoreCase(sort_by)){
                                list_status = GET_REFRESH_ASS_LIST_STATUS_CHANGE_PAGE;
                        }else{
                                list_status = GET_REFRESH_ASS_LIST_STATUS_CHANGE_SORT_ORDER;
                        }
                    }else{
                        list_status = GET_REFRESH_ASS_LIST_STATUS_CHANGE_ORDERING_COLUMN;
                    }
                    sessIdvec = (Vector)sessData.get(HASH_ASM_ID_VEC);
                }else{
                    list_status = GET_REFRESH_ASS_LIST_STATUS_CHANGE_FILTER;
                }
            }  
        }
        // finished init list_status
        int start;
        int end;
        long[] ids;

        if (list_status == GET_REFRESH_ASS_LIST_STATUS_CHANGE_PAGE 
            || list_status == GET_REFRESH_ASS_LIST_STATUS_CHANGE_SORT_ORDER ){

            if (list_status == GET_REFRESH_ASS_LIST_STATUS_CHANGE_SORT_ORDER){
                Vector reverseIdvec = new Vector();
                for(int i=sessIdvec.size()-1; i>=0; i--)
                    reverseIdvec.addElement((Long)sessIdvec.elementAt(i));
                cur_page = 1;
                sessIdvec = reverseIdvec;
            }
            
            start = (cur_page-1) * pagesize + 1;
            end  = cur_page * pagesize;
            ids = new long[end-start+1];
            for(int i=start; i<=end && i<=sessIdvec.size(); i++)
                ids[i-start] = ((Long)sessIdvec.elementAt(i-1)).longValue();
            rs = Vass.assList(con, ids, order_by, sort_by, root_ent_id, hasTc, tcr_id_lst);            
            idVec = sessIdvec;                                        

        }else if (list_status == GET_REFRESH_ASS_LIST_STATUS_CHANGE_ORDERING_COLUMN){
            ids = new long[sessIdvec.size()];
            for(int i=0; i<sessIdvec.size(); i++)
                ids[i] = ((Long)sessIdvec.elementAt(i)).longValue();
            rs = Vass.assList(con, ids, order_by, sort_by, root_ent_id, hasTc, tcr_id_lst);            
            cur_page = 1;
//            sort_by = " DESC ";
        }else if (list_status == GET_REFRESH_ASS_LIST_STATUS_CHANGE_FILTER 
                || list_status == GET_REFRESH_ASS_LIST_STATUS_NO_SESSION ){
            rs = Vass.assList(con, prepared, collected, notified, resolved, order_by, sort_by, curTime, root_ent_id, hasTc, tcr_id_lst);
            cur_page = 1;
        }else{
            throw new cwException("no know list status in getRefreshedAssListAsXML: " + list_status);
        }

//        System.out.println("list_status: " + list_status);
//        System.out.println("idVec: " + idVec.size());
        
        DbCmAssessmentUnit dbAssU = new DbCmAssessmentUnit();  
        int cur_count = 0;
        while( rs.next() ) {                
            cur_count++;
                if( cur_count <= pagesize ) {
                    dbAssU.asu_asm_id = rs.getLong("asm_id");
                    xmlBody.append("<assessment id=\"").append(rs.getLong("asm_id")).append("\" ")
                       .append(" assessee=\"").append(cwUtils.esc4XML(rs.getString("usr_display_bil"))).append("\" ")
                       .append(" title=\"").append(cwUtils.esc4XML(rs.getString("asm_title"))).append("\" ");
                    status = rs.getString("asm_status");
                    if( status != null && status.equalsIgnoreCase("RESOLVED") )
                        xmlBody.append(" status=\"RESOLVED\" ");
                    else {
                        if( curTime.before(rs.getTimestamp("asm_eff_start_datetime")) )
                            xmlBody.append(" status=\"PREPARED\" ");
                        else if( curTime.after(rs.getTimestamp("asm_eff_start_datetime")) &&
                                 curTime.before(rs.getTimestamp("asm_eff_end_datetime")) )
                             xmlBody.append(" status=\"NOTIFIED\" ");
                        else if( curTime.after(rs.getTimestamp("asm_eff_end_datetime")) )
                             xmlBody.append(" status=\"COLLECTED\" ");
                    }
                    xmlBody.append(" assigned=\"").append(rs.getLong("total")).append("\" ")
                       //.append(" notified=\"").append(ViewCmAssessment.numberOfNotify(con, dbAssU.asu_asm_id, "ASSESSMENT_NOTIFICATION", "Y")).append("\" ")
					   .append(" notified=\"").append(numberOfNotify(con, dbAssU.asu_asm_id)).append("\" ")
                       .append(" collected=\"").append(dbAssU.numberOfSubmit(con, true)).append("\" ")
                       .append(" notify_date=\"").append(rs.getTimestamp("asm_eff_start_datetime")).append("\" ")
                       .append(" due_date=\"").append(rs.getTimestamp("asm_eff_end_datetime")).append("\" ")
                       .append(" />").append(cwUtils.NEWL);
                }
                if (list_status == GET_REFRESH_ASS_LIST_STATUS_CHANGE_ORDERING_COLUMN
                    || list_status == GET_REFRESH_ASS_LIST_STATUS_CHANGE_FILTER 
                    || list_status == GET_REFRESH_ASS_LIST_STATUS_NO_SESSION ){
                    idVec.addElement(new Long(rs.getLong("asm_id")));
                }
            }
            rs.close();
            
            StringBuffer xml = new StringBuffer();
            xml.append("<assessments total=\"").append(idVec.size()).append("\" ")
               .append(" cur_page=\"").append(cur_page).append("\" ")
               .append(" page_size=\"").append(pagesize).append("\" ")
               .append(" timestamp=\"").append(curTime).append("\" ")
               .append(" sort_by=\"").append(sort_by).append("\" ")
               .append(" order_by=\"").append(order_by).append("\" ")
               .append(" >").append(cwUtils.NEWL)
               .append(xmlBody.toString())
               .append("</assessments>").append(cwUtils.NEWL);
            
            Hashtable data = new Hashtable();
            data.put(HASH_TIMESTAMP, curTime);
            data.put(HASH_ASM_ID_VEC, idVec);
            data.put(HASH_ORDERBY, order_by);
            data.put(HASH_SORTBY, sort_by);
            
            data.put(HASH_PREPARED, new Boolean(prepared));
            data.put(HASH_COLLECTED, new Boolean(collected));
            data.put(HASH_NOTIFIED, new Boolean(notified));
            data.put(HASH_RESOLVED, new Boolean(resolved));
            
            sess.setAttribute("Competency_ASS_LIST", data);
            
            return xml.toString();
        }



    /**
    Get Assessment list as xml 
    @return string of xml
    */
    /*
    public String getAssessmentListAsXML(Connection con, HttpSession sess, int cur_page, int pagesize, Timestamp pagetime, String order_by, String sort_by, long root_ent_id)
        throws SQLException, cwException {
            Timestamp curTime = cwSQL.getTime(con);
            Hashtable data = (Hashtable)sess.getAttribute("Competency_ASS_LIST");
            Timestamp sessPagetime = (Timestamp)data.get(HASH_TIMESTAMP);
            Vector sessIdvec = (Vector)data.get(HASH_ASM_ID_VEC);
            String sessOrderby = (String)data.get(HASH_ORDERBY);
            String sessSortby = (String)data.get(HASH_SORTBY);
            ViewCmAssessment Vass = new ViewCmAssessment();
            ResultSet rs;
            boolean reorder = false;
            int start;
            int end;
            long[] ids;

            if( sessOrderby.equalsIgnoreCase(order_by) && sessSortby.equalsIgnoreCase(sort_by) ) {                
                start = (cur_page-1) * pagesize + 1;
                end  = cur_page * pagesize;
                ids = new long[end-start+1];

                for(int i=start; i<=end && i<=sessIdvec.size(); i++)
                    ids[i-start] = ((Long)sessIdvec.elementAt(i-1)).longValue();
                rs = Vass.assList(con, ids, order_by, sort_by, root_ent_id);                
            } else {
                
                if( !sessSortby.equalsIgnoreCase(sort_by) )
                    order_by = " DESC ";

                start = 1;
                ids = new long[sessIdvec.size()];
                for(int i=0; i<ids.length; i++)
                    ids[i] = ((Long)sessIdvec.elementAt(i)).longValue();
                rs = Vass.assList(con, ids, order_by, sort_by, root_ent_id);
                reorder = true;
                cur_page = 1;
            }
            
            Vector reorderedId = new Vector();
            StringBuffer xmlBody = new StringBuffer();
            DbCmAssessmentUnit dbAssU = new DbCmAssessmentUnit();
            String status;
            while( rs.next() ) {

                if( reorderedId.size() < pagesize ) {
                    dbAssU.asu_asm_id = rs.getLong("asm_id");
                    xmlBody.append("<assessment id=\"").append(rs.getLong("asm_id")).append("\" ")
                       .append(" assessee=\"").append(cwUtils.esc4XML(rs.getString("usr_display_bil"))).append("\" ")
                       .append(" title=\"").append(cwUtils.esc4XML(rs.getString("asm_title"))).append("\" ");
                    status = rs.getString("asm_status");
                    if( status != null && status.equalsIgnoreCase("RESOLVED") )
                        xmlBody.append(" status=\"RESOLVED\" ");
                    else {
                        if( curTime.before(rs.getTimestamp("asm_eff_start_datetime")) )
                            xmlBody.append(" status=\"PREPARED\" ");
                        else if( curTime.after(rs.getTimestamp("asm_eff_start_datetime")) &&
                                 curTime.before(rs.getTimestamp("asm_eff_end_datetime")) )
                             xmlBody.append(" status=\"NOTIFIED\" ");
                        else if( curTime.after(rs.getTimestamp("asm_eff_end_datetime")) )
                             xmlBody.append(" status=\"COLLECTED\" ");
                    }
                    xmlBody.append(" assigned=\"").append(rs.getLong("total")).append("\" ")
                       .append(" notified=\"").append(ViewCmAssessment.numberOfNotify(con, dbAssU.asu_asm_id, "ASSESSMENT_NOTIFICATION", "Y")).append("\" ")
                       .append(" collected=\"").append(dbAssU.numberOfSubmit(con, true)).append("\" ")
                       .append(" notify_date=\"").append(rs.getTimestamp("asm_eff_start_datetime")).append("\" ")
                       .append(" due_date=\"").append(rs.getTimestamp("asm_eff_end_datetime")).append("\" ")
                       .append(" />").append(cwUtils.NEWL);
                }
                reorderedId.addElement(new Long(rs.getLong("asm_id")));
            }


            StringBuffer xml = new StringBuffer();
            xml.append("<assessments total=\"").append(sessIdvec.size()).append("\" ")
               .append(" cur_page=\"").append(cur_page).append("\" ")
               .append(" page_size=\"").append(pagesize).append("\" ")
               .append(" timestamp=\"").append(pagetime).append("\" ")
               .append(" sort_by=\"").append(sort_by).append("\" ")
               .append(" order_by=\"").append(order_by).append("\" ")
               .append(" >").append(cwUtils.NEWL)
               .append(xmlBody.toString())
               .append("</assessments>").append(cwUtils.NEWL);
               
            if( reorder ) {                
                Hashtable curData = new Hashtable();
                //curData.put(HASH_TIMESTAMP, pagetime);
                curData.put(HASH_ASM_ID_VEC, reorderedId);
                curData.put(HASH_ORDERBY, order_by);
                curData.put(HASH_SORTBY, sort_by);
                sess.setAttribute("Competency_ASS_LIST", curData);
            }
            
            rs.close();
            return xml.toString();
            
        }

*/




    /**
    Get assessment units of specified assessment id in XML
    @param long value of assessment id
    @return string of XML
    */
    public String getAssessmentUnitsAsXML(Connection con)
        throws SQLException {
        return getAssessmentUnitsAsXML(con, false);
    }
    
    public String getAssessmentUnitsAsXML(Connection con, boolean getResolved)
        throws SQLException {
            
            ViewCmAssessment Vass = new ViewCmAssessment();
            ResultSet rs = Vass.getAssessmentUnits(con, asm_id, getResolved);
            StringBuffer xml = new StringBuffer();
            xml.append("<assessors>").append(cwUtils.NEWL);
            Timestamp cur_timestamp = cwSQL.getTime(con);
            Timestamp eff_start_timestamp = null;
            String mgh_status = null;
            while( rs.next() ) {
                xml.append("<assessor ent_id=\"").append(rs.getLong("asu_ent_id")).append("\" ")
                   .append(" weight=\"").append(rs.getLong("asu_weight")).append("\" ")
                   .append(" display_bil=\"").append(cwUtils.esc4XML(rs.getString("usr_display_bil"))).append("\" ")
                   .append(" type=\"").append(rs.getString("asu_type")).append("\" ")
                   .append(" status=\"");
                   
                if( rs.getBoolean("asu_submit_ind") ) {
                    xml.append("COLLECTED\" ")
                       .append("collection_timestamp=\"").append(rs.getTimestamp("sks_update_timestamp")).append("\"/>");
                }
                else {
                    eff_start_timestamp = rs.getTimestamp("aua_eff_start_timestamp");
                    if (eff_start_timestamp ==null || cur_timestamp.before(eff_start_timestamp)) {
                        xml.append("UNNOTIFIED");
                    } else {
                        xml.append("NOTIFIED");
                    }
                    xml.append("\"/>");
                }
            }
            xml.append("</assessors>").append(cwUtils.NEWL);
            
            rs.close();
            return xml.toString();
        }

    /**
    Submit Assessment
    */    
    public void submitTestAssessment(Connection con, long assessee_ent_id, long mod_res_id)
        throws SQLException, cwException, cwSysMessage, IOException {

        //Check permission : user have submit right ... not implement
            
        dbRegUser assessee=new dbRegUser();
        dbProgress pgr=new dbProgress();
        try {
            //Get assessor info
            assessee = new dbRegUser();
            assessee.usr_ent_id = assessee_ent_id;
            assessee.get(con);
                
            //Get Test Score
            pgr = new dbProgress();
            pgr.pgr_usr_id = assessee.usr_id;
            pgr.pgr_res_id = mod_res_id;
            pgr.get(con);
        } catch(qdbException e) {
            throw new cwException(e.getMessage());
        } catch (qdbErrMessage ee) {
            throw new cwSysMessage(ee.getId());
        }
            
        //Get Assessment details
        DbCmAssessment asm = new DbCmAssessment();
        asm.getByMod(con, mod_res_id);

        //Calculate user skill level
        CmSkillLevelCalculator calc = new CmSkillLevelCalculator();
        float skill_level = calc.getLevelFromMarkingScheme((pgr.pgr_score/pgr.pgr_max_score)*100, asm.asm_marking_scheme_xml);            
        
        //Get Assessment's SkillSetCoverage
        ViewCmSkillCoverage vsc = new ViewCmSkillCoverage();
        DbCmSkillSetCoverage[] ssc = vsc.getAllSkillsSetCoverage(con, asm.asm_sks_skb_id);

        //Get Resolved SkillSet Id
        DbCmAssessmentUnit asu = new DbCmAssessmentUnit();
        asu.asu_asm_id = asm.asm_id;
        asu.asu_sks_skb_id = asu.getResolvedId(con);
            
        if(asu.asu_sks_skb_id == 0) {
            //Create an assessment result set
            DbCmSkillSet skillSet = new DbCmSkillSet();
            skillSet.sks_type = ASSESSMENT_RESULT;                
            skillSet.sks_xml = "<comment></comment>";
            skillSet.sks_owner_ent_id = assessee.usr_ste_ent_id;
            skillSet.sks_create_usr_id = assessee.usr_id;                
            skillSet.ins(con);
                    
            //Update AssessmentUnit's submit_ind and assessment result foreign key
            DbCmAssessmentUnit.submitTestAssessment(con, asm.asm_id, 1, skillSet.sks_skb_id);

            //Create SkillSetCoverage for the assessment result
            for(int i=0; i<ssc.length; i++) {
                ssc[i].ssc_sks_skb_id = skillSet.sks_skb_id;
                ssc[i].ssc_level = skill_level;
                ssc[i].ssc_xml = "<comment></comment>";
                ssc[i].ins(con);
            }
        } else {
            //Update SkillSetCoverage for the assessment result
            for(int i=0; i<ssc.length; i++) {
                ssc[i].ssc_sks_skb_id = asu.asu_sks_skb_id;
                ssc[i].ssc_level = skill_level;
                ssc[i].ssc_xml = "<comment></comment>";
                ssc[i].upd(con);
            }
        }
        return;
    }


    /**
    Submit Assessment
    */    
    public void submitAssessment(Connection con, loginProfile prof, long[] skillIdList, float[] answerIdList, String comment, boolean submit, Timestamp last_upd_timestamp)
        throws SQLException, cwSysMessage, cwException {

            //Check permission : user have submit right ... not implement
            DbCmAssessmentUnit dbAssU = new DbCmAssessmentUnit();
            dbAssU.asu_asm_id = asm_id;
            dbAssU.asu_ent_id = prof.usr_ent_id;
            dbAssU.asu_attempt_nbr = 1;
            dbAssU.get(con);
            int curSection = 0;
            if( dbAssU.asu_sks_skb_id > 0 ) {
                // update a assessment result set
                if( dbAssU.asu_submit_ind ) {
                    throw new cwSysMessage(CompetencyModule.SMSG_CMP_ASS_SUBMITED);
                }
                
                //get the original answer's skillset coverage
                ViewCmSkillCoverage Vsc = new ViewCmSkillCoverage();
                DbCmSkillSetCoverage[] DbSsc = Vsc.getAllSkillsSetCoverage(con, dbAssU.asu_sks_skb_id);
                
                //get the original answer's skillset
                DbCmSkillSet skillSet = new DbCmSkillSet();
                skillSet.sks_skb_id = dbAssU.asu_sks_skb_id;
                
                //check answer's last update timestamp
                if( !skillSet.validUpdTimestamp(con, last_upd_timestamp) )
                    throw new cwSysMessage(CompetencyModule.SMSG_CMP_INVALID_TIMESTAMP);
                
                //update answer's skillset with comment
                skillSet.sks_update_usr_id = prof.usr_id;
                if( comment != null && comment.length() > 0 )
                    skillSet.sks_xml = "<comment provide=\"Y\">" + comment + "</comment>";
                else
                    skillSet.sks_xml = null;
                skillSet.updComment(con);

                //update answer's skillset coverage
                for(int i=0; i<DbSsc.length; i++) {
                	if(skillIdList !=null){
                		for(int j=0; j<skillIdList.length; j++) {
                			if(skillIdList[j] == DbSsc[i].ssc_skb_id) {
                				DbSsc[i].ssc_level = answerIdList[j];
                				DbSsc[i].bAnswered = true;
                				break;
                			}
                			DbSsc[i].bAnswered = false;
                		}                		
                	}else{
                		DbSsc[i].bAnswered = false;
                	}
                    DbSsc[i].upd(con);
                }
                if( submit ) {
                    dbAssU.asu_submit_ind = submit;
                    dbAssU.updSubmitStatus(con);
                }
            } else {

                // create a assessment result set
                DbCmSkillSet skillSet = new DbCmSkillSet();
                skillSet.sks_type = ASSESSMENT_RESULT;                
                skillSet.sks_owner_ent_id = prof.root_ent_id;
                skillSet.sks_create_usr_id = prof.usr_id;
                skillSet.sks_update_usr_id = prof.usr_id;
                if(comment != null && comment.length() > 0) {
                    skillSet.sks_xml = "<comment provide=\"Y\">" + comment + "</comment>";
                } else {
                    skillSet.sks_xml = null;
                }
                skillSet.ins(con);
                
                //update the skill set id for the assessment unit
                dbAssU.asu_sks_skb_id = skillSet.sks_skb_id;
                dbAssU.updSkillSetId(con);
                
                //get survey's skillset coverage
                DbCmAssessment dbAss = new DbCmAssessment();
                dbAss.asm_id = asm_id;
                dbAss.get(con);
                long sks_id = dbAss.asm_sks_skb_id;
                ViewCmSkillCoverage Vsc = new ViewCmSkillCoverage();
                long[] skillIdArray = Vsc.getRatingSkill(con, sks_id);
                
                Vector sksIdVec = new Vector();
                sksIdVec.addElement(new Long(skillSet.sks_skb_id));
                
                //insert the answers into skillset coverage
                for(int i=0; i<skillIdArray.length; i++) {
                    DbCmSkillSetCoverage DbSsc = new DbCmSkillSetCoverage();
                    DbSsc.ssc_skb_id = skillIdArray[i];
                    DbSsc.ssc_sks_skb_id = skillSet.sks_skb_id;
                    DbSsc.ssc_xml = null;
                    if(skillIdList !=null){
                    	for(int j=0; j<skillIdList.length; j++) {
                    		if(skillIdList[j] == skillIdArray[i]) {
                    			DbSsc.ssc_level = answerIdList[j];
                    			DbSsc.bAnswered = true;
                    			break;
                    		}
                    		DbSsc.bAnswered = false;
                    	}
                    }else{
                    	DbSsc.bAnswered = false;
                    }
                    DbSsc.ins(con);
                }

                //update assessment unit status
                dbAssU.asu_submit_ind = submit;
                dbAssU.updSubmitStatus(con);
                
                //resolve this Assessment if it needs to be resolved now.
                if(!dbAssU.asu_type.equals(SELF) && isNeedAutoResolve(con, dbAss)) {
                    //loginProfile sysProf = dbRegUser.getSiteDefaultSysProfile(con, prof);
                    resolveAssessment(con, dbAss, dbRegUser.getSiteDefaultSysProfile(con,prof));
                }
            }
            return;
        }

    /**
    Check if the input Assessment need to be auto resolve at this moment
    */
    private boolean isNeedAutoResolve(Connection con, DbCmAssessment dbAss) throws SQLException {
        boolean result;
        
        CommonLog.debug("dbAss is null = " + (dbAss == null));
        CommonLog.debug("dbAss.asm_status = " + dbAss.asm_status);
        
        if(dbAss.asm_auto_resolved_ind && !dbAss.asm_status.equals(RESOLVED) && !dbAss.asm_status.equals(DELETED) && DbCmAssessmentUnit.hasOtherAsu(con,dbAss.asm_id)) {
            result = DbCmAssessmentUnit.isAllOtherAsuSubmitted(con, dbAss.asm_id);
        } else {
            result = false;
        }
        return result;
    }

    private void resolveAssessment(Connection con, DbCmAssessment dbAss, loginProfile prof) 
        throws SQLException, cwSysMessage, cwException {
        //Get Resolved AssessmentUnit Id
        DbCmAssessmentUnit asu = new DbCmAssessmentUnit();
        asu.asu_asm_id = dbAss.asm_id;
        asu.asu_sks_skb_id = asu.getResolvedId(con);

        //Get Assessment's rating skill and format the skillset coverage
        ViewCmSkillCoverage vsc = new ViewCmSkillCoverage();
        long[] skillIdArray = vsc.getRatingSkill(con, dbAss.asm_sks_skb_id);
        DbCmSkillSetCoverage[] ssc = new DbCmSkillSetCoverage[skillIdArray.length];
        for(int i=0; i<skillIdArray.length; i++) {
            ssc[i] = new DbCmSkillSetCoverage();
            ssc[i].ssc_skb_id = skillIdArray[i];
            ssc[i].bAnswered = true;
        }
        
        //Get skill scores by type
        this.asm_id = dbAss.asm_id;
        float mgmt, peers, clients, reports, all, total;
        float mgmtWeight, peersWeight, reportsWeight, clientsWeight;
        int numMgmt, numPeers, numReports, numClients;
        //Get weighted average by types
        Hashtable mgmtResult = overallResultByType(con, MANAGEMENT);
        Hashtable peersResult = overallResultByType(con, PEERS);
        Hashtable reportsResult = overallResultByType(con, REPORTS);
        Hashtable clientsResult = overallResultByType(con, CLIENTS);
        //Get number of assessors of each type
        numMgmt = ((Integer)mgmtResult.get(HASH_NUM_OF_USER)).intValue();
        numPeers = ((Integer)peersResult.get(HASH_NUM_OF_USER)).intValue();
        numReports = ((Integer)reportsResult.get(HASH_NUM_OF_USER)).intValue();
        numClients = ((Integer)clientsResult.get(HASH_NUM_OF_USER)).intValue();
        //For each Skill, calculate weighted average of all assessors
        for(int i=0; i<ssc.length; i++) {
            total = 0;
            all = 0;
            if( numMgmt > 0 ) {
                mgmt = cwUtils.roundingFloat(((Float)mgmtResult.get(new Long(ssc[i].ssc_skb_id))).floatValue(),2);
                mgmtWeight = ((Float)mgmtResult.get(HASH_TOTAL_WEIGHT)).floatValue();
                total += mgmtWeight;
                all += mgmt*mgmtWeight;
            }
            if( numPeers > 0 ) {
                peers = cwUtils.roundingFloat(((Float)peersResult.get(new Long(ssc[i].ssc_skb_id))).floatValue(),2);
                peersWeight = ((Float)peersResult.get(HASH_TOTAL_WEIGHT)).floatValue();
                total += peersWeight;
                all += peers*peersWeight;
            }
            if( numReports > 0 ) {
                reports = cwUtils.roundingFloat(((Float)reportsResult.get(new Long(ssc[i].ssc_skb_id))).floatValue(),2);
                reportsWeight = ((Float)reportsResult.get(HASH_TOTAL_WEIGHT)).floatValue();
                total += reportsWeight;
                all += reports*reportsWeight;
            }
            if( numClients > 0 ) {
                clients = cwUtils.roundingFloat(((Float)clientsResult.get(new Long(ssc[i].ssc_skb_id))).floatValue(),2);
                clientsWeight = ((Float)clientsResult.get(HASH_TOTAL_WEIGHT)).floatValue();
                total += clientsWeight;
                all += clients*clientsWeight;
            }
            if( total == 0 ) {
                all = 0;
            } else {
                all = cwUtils.roundingFloat((all/total), 2);
            }
            //Store the weighted average into the SkillSet
            ssc[i].ssc_level = all;
        }
                    
        //create a new skillSet if necessary
        if(asu.asu_sks_skb_id == 0) {
            DbCmSkillSet skillSet = new DbCmSkillSet();
            skillSet.sks_type = ASSESSMENT_RESULT;
            skillSet.sks_owner_ent_id = prof.root_ent_id;
            skillSet.sks_create_usr_id = prof.usr_id;
            skillSet.ins(con);
            asu.asu_sks_skb_id = skillSet.sks_skb_id;
        }
                
        //Create SkillSetCoverage for the assessment result
        for(int i=0; i<ssc.length; i++) {
            ssc[i].ssc_sks_skb_id = asu.asu_sks_skb_id;
            ssc[i].ins(con);
        }
        
        //Update the Resolved AssessmentUnit
        asu.asu_ent_id = prof.usr_ent_id;
        asu.updResolvedId(con);
        
        //update Assessment to RESOLVED
        dbAss.asm_status = RESOLVED;
        dbAss.updStatus(con);
        return;
    }

    
    /**
    Get the survey result of the assessor
    @param long assessment id
    @param long user entity id
    @return String of xml
    */
    public String getAssessorResult(Connection con, long asm_id, long usr_ent_id)
        throws SQLException, cwSysMessage, cwException {
            
            // get the skill set(ASR) id of the assessor
            DbCmAssessmentUnit dbAssU = new DbCmAssessmentUnit();
            dbAssU.asu_asm_id = asm_id;
            dbAssU.asu_ent_id = usr_ent_id;
            dbAssU.asu_attempt_nbr = 1;            
            long sks_id = dbAssU.getASRSkillSetId(con);

            // Get the skill set(SKT) id assigned to the assessment
            DbCmAssessment dbAss = new DbCmAssessment();
            dbAss.asm_id = asm_id;
            long skt_id = dbAss.getSurveyId(con);

            //variables for XML
            DbCmSkillSet DbSs = new DbCmSkillSet(); //for assessment survey 
            DbCmSkillSet DbSsr = null; //for assessment answer
            StringBuffer xmlBuf = new StringBuffer(1024);

            //get Survey details
            DbSs.sks_skb_id = skt_id;
            DbSs.get(con);
            
            //get Answer details
            if( sks_id > 0 ) {
                DbSsr = new DbCmSkillSet();
                DbSsr.sks_skb_id = sks_id;
                DbSsr.get(con);
            }
            
            //make survey header
            xmlBuf.append("<survey id=\"").append(DbSs.sks_skb_id).append("\"")
                  .append(" type=\"").append(DbSs.sks_type).append("\">")
                  .append("<title>").append(cwUtils.esc4XML(DbSs.sks_title)).append("</title>");
            if(sks_id > 0) {
                xmlBuf.append("<last_upd_timestamp>").append(DbSsr.sks_update_timestamp).append("</last_upd_timestamp>");
                if(DbSsr.sks_xml != null && DbSsr.sks_xml.length() > 0) {
                    xmlBuf.append(DbSsr.sks_xml);
                } else {
                    xmlBuf.append(DbSs.sks_xml);
                }
            } else {
                xmlBuf.append(DbSs.sks_xml);
            }

            //get Survey SkillSetCoverage details
            CmSkillSetManager skillSetManager = new CmSkillSetManager();
            skillSetManager.sks_skb_id = skt_id;
            xmlBuf.append(skillSetManager.getSurveyContentAsXML(con));
            
            //get Answer details
            if( sks_id > 0 ) {
                xmlBuf.append(getAnswerAsXML(con,sks_id));
            }
            
            xmlBuf.append("</survey>");
            return xmlBuf.toString();                        
        }


    /**
    Get XML of assessment answer (SkillSetCoverage) 
    @param con Connection to database
    @param sks_id SkillSet id of the answer
    */
    private String getAnswerAsXML(Connection con, long sks_id) throws SQLException {
        StringBuffer xmlBuf = new StringBuffer(1024);
        ViewCmSkillCoverage VSsc = new ViewCmSkillCoverage();
        DbCmSkillSetCoverage[] resultCoverage = VSsc.getAllSkillsSetCoverage(con, sks_id);
        xmlBuf.append("<answer><skill_list>");
        for(int i=0; i<resultCoverage.length; i++) {
            xmlBuf.append("<skill id=\"").append(resultCoverage[i].ssc_skb_id).append("\">");
            if( resultCoverage[i].bAnswered ) {
				xmlBuf.append(resultCoverage[i].ssc_level);
            } 
            xmlBuf.append("</skill>");
        }
        xmlBuf.append("</skill_list></answer>");
        return xmlBuf.toString();
    }


    /**
    Get overall result of the assessment
    @return String of XML
    */
    public String overallResult(Connection con)
        throws SQLException, cwException, cwSysMessage {
            
            StringBuffer xml = new StringBuffer();
            DbCmAssessment dbAss = new DbCmAssessment();
            dbAss.asm_id = asm_id;
            dbAss.get(con);
            Timestamp resolved_upd_timestamp = ViewCmAssessment.getResolvedSkillSetTimestamp(con, asm_id);
            ViewCmSkillCoverage Vsc = new ViewCmSkillCoverage();
            DbCmSkillSetCoverage DbSsc[] = Vsc.getAllSkillsSetCoverage(con, dbAss.asm_sks_skb_id);
            Hashtable mgmtResult = overallResultByType(con, MANAGEMENT);
            int numMgmt = ((Integer)mgmtResult.get(HASH_NUM_OF_USER)).intValue();
            Hashtable peersResult = overallResultByType(con, PEERS);
            int numPeers = ((Integer)peersResult.get(HASH_NUM_OF_USER)).intValue();
            Hashtable reportsResult = overallResultByType(con, REPORTS);
            int numReports = ((Integer)reportsResult.get(HASH_NUM_OF_USER)).intValue();
            Hashtable clientsResult = overallResultByType(con, CLIENTS);
            int numClients = ((Integer)clientsResult.get(HASH_NUM_OF_USER)).intValue();
            Hashtable selfResult = overallResultByType(con, SELF);
            int numSelf = ((Integer)selfResult.get(HASH_NUM_OF_USER)).intValue();
            Hashtable resolvedResult = overallResultByType(con, RESOLVED);
            int numResolved = ((Integer)resolvedResult.get(HASH_NUM_OF_USER)).intValue();
            
            DbCmSkill DbSkill = new DbCmSkill();
            DbCmSkillLevel skillLevel = new DbCmSkillLevel();
            int count = 1;
            xml.append("<overall_result ")
               .append(" mgmt=\"").append(numMgmt).append("\" ")
               .append(" peers=\"").append(numPeers).append("\" ")
               .append(" reports=\"").append(numReports).append("\" ")
               .append(" clients=\"").append(numClients).append("\" ")
               .append(" self=\"").append(numSelf).append("\" ");
            if(resolved_upd_timestamp != null) {
                xml.append("last_upd_timestamp=\"").append(resolved_upd_timestamp).append("\" ");
            }
            xml.append(" >")
               .append(cwUtils.NEWL);
            

            for(int i=0; i<DbSsc.length; i++) {

                if( DbSsc[i].skb_type.equalsIgnoreCase(SKILL_SET))
                    continue;
                xml.append("<question num=\"").append(count++).append("\" >").append(cwUtils.NEWL);
                //Skill detial
                DbSkill.skl_skb_id = DbSsc[i].ssc_skb_id;
                DbSkill.skb_id = DbSkill.skl_skb_id;
                DbSkill.get(con);
                xml.append("<skill id=\"").append(DbSkill.skl_skb_id).append("\">");
                xml.append("<title>").append(cwUtils.esc4XML(DbSkill.skb_title)).append("</title>").append(cwUtils.NEWL)
                   .append("<desc>").append(cwUtils.esc4XML(DbSkill.skb_description)).append("</desc>").append(cwUtils.NEWL);

                if( DbSsc[i].ssc_xml != null && DbSsc[i].ssc_xml.length() > 0 )
                    xml.append(DbSsc[i].ssc_xml );
                xml.append("</skill>").append(cwUtils.NEWL);

                //Scale detial
                skillLevel.sle_ssl_id = DbSkill.skb_ssl_id;
                Vector vec = skillLevel.getById(con);
                StringBuffer temp = new StringBuffer(512);
                long max_level = 0;
                for(int j=0; j<vec.size(); j++) {
                    DbCmSkillLevel level = (DbCmSkillLevel)vec.elementAt(j);
                    temp.append(level.asXML());
                    if(max_level < level.sle_level) {
                        max_level = level.sle_level;
                    }
                }

                xml.append("<scale max_level=\"").append(max_level).append("\" >")
                .append(cwUtils.NEWL)
                .append(temp.toString());

                xml.append("</scale>").append(cwUtils.NEWL);
                
                
                if( DbSkill.skl_rating_ind ) {
                	xml.append(skillResultXml(DbSsc[i].ssc_skb_id, mgmtResult, 
                								peersResult, reportsResult, clientsResult, 
                								selfResult, resolvedResult));
                } else {
                	xml.append("<skill_list>");
                	Vector v_child_skb = DbSkill.getChildDesc(con);
                	for(int j=0; j<v_child_skb.size(); j++){
                		DbCmSkillBase skillBase = (DbCmSkillBase)v_child_skb.elementAt(j);
                		xml.append("<skill id=\"").append(skillBase.skb_id).append("\">")
                			.append("<desc>").append(cwUtils.esc4XML(skillBase.skb_description)).append("</desc>")
                			.append(skillResultXml(skillBase.skb_id, mgmtResult, 
									peersResult, reportsResult, clientsResult, 
									selfResult, resolvedResult))
							.append("</skill>");
                	}
                	xml.append("</skill_list>");
                }
                   
                xml.append("</question>").append(cwUtils.NEWL);
            }
            
            xml.append("</overall_result>").append(cwUtils.NEWL);
            return xml.toString();
        }



	private String skillResultXml(long skb_id, Hashtable mgmtResult, Hashtable peersResult, 
									Hashtable reportsResult, Hashtable clientsResult, 
									Hashtable selfResult, Hashtable resolvedResult){
		
		int numMgmt = ((Integer)mgmtResult.get(HASH_NUM_OF_USER)).intValue();
		int numPeers = ((Integer)peersResult.get(HASH_NUM_OF_USER)).intValue();
		int numReports = ((Integer)reportsResult.get(HASH_NUM_OF_USER)).intValue();
		int numClients = ((Integer)clientsResult.get(HASH_NUM_OF_USER)).intValue();
		int numSelf = ((Integer)selfResult.get(HASH_NUM_OF_USER)).intValue();
		int numResolved = ((Integer)resolvedResult.get(HASH_NUM_OF_USER)).intValue();

		float mgmt, peers, clients, reports, all, diff, self, resolved, total;
		float mgmtWeight, peersWeight, reportsWeight, clientsWeight, selfWeight;

		all = 0;
		mgmt = 0;
		peers = 0;
		reports = 0;
		clients = 0;
		self = 0;
		total = 0;
		resolved = 0;
		mgmtWeight = 0;
		peersWeight = 0;
		clientsWeight = 0;
		reportsWeight = 0;
		selfWeight = 0;
		if( numMgmt > 0 ) {                    
			mgmt = cwUtils.roundingFloat(((Float)mgmtResult.get(new Long(skb_id))).floatValue(),2);
			mgmtWeight = ((Float)mgmtResult.get(HASH_TOTAL_WEIGHT)).floatValue();
			total += mgmtWeight;
			all += mgmt*mgmtWeight;
		}
		if( numPeers > 0 ) {
			peers = cwUtils.roundingFloat(((Float)peersResult.get(new Long(skb_id))).floatValue(),2);
			peersWeight = ((Float)peersResult.get(HASH_TOTAL_WEIGHT)).floatValue();
			total += peersWeight;
			all += peers*peersWeight;
		}
		if( numReports > 0 ) {
			reports = cwUtils.roundingFloat(((Float)reportsResult.get(new Long(skb_id))).floatValue(),2);
			reportsWeight = ((Float)reportsResult.get(HASH_TOTAL_WEIGHT)).floatValue();
			total += reportsWeight;
			all += reports*reportsWeight;
		}
		if( numClients > 0 ) {
			clients = cwUtils.roundingFloat(((Float)clientsResult.get(new Long(skb_id))).floatValue(),2);
			clientsWeight = ((Float)clientsResult.get(HASH_TOTAL_WEIGHT)).floatValue();
			total += clientsWeight;
			all += clients*clientsWeight;
		}
		if( numSelf > 0 ) {
			self = cwUtils.roundingFloat(((Float)selfResult.get(new Long(skb_id))).floatValue(),2);
			selfWeight = ((Float)selfResult.get(HASH_TOTAL_WEIGHT)).floatValue();
			//SELF need not to be counted into all, 
			//Actually ALL is renamed to Others
			/*
			all += self;
			total++;
			*/
		}
		if( numResolved > 0 )
			if(resolvedResult.get(new Long(skb_id)) != null)
				resolved = cwUtils.roundingFloat(((Float)resolvedResult.get(new Long(skb_id))).floatValue(),2);

		if( total == 0 )
			all = 0;
		else    
			all = cwUtils.roundingFloat((all/total), 2);
		diff = self - all;
        
        StringBuffer xml = new StringBuffer();
                
		xml.append("<result ");
		if( numMgmt > 0 )
			xml.append(" mgmt=\"").append(mgmt).append("\" ");
		else
			xml.append(" mgmt=\"NA\" ");
                
		if( numPeers > 0 )    
			xml.append(" peers=\"").append(peers).append("\" ");
		else
			xml.append(" peers=\"NA\" ");

		if( numReports > 0 )
			xml.append(" reports=\"").append(reports).append("\" ");
		else
			xml.append(" reports=\"NA\" ");
                    
		if( numClients > 0 )    
			xml.append(" clients=\"").append(clients).append("\" ");
		else
			xml.append(" clients=\"NA\" ");
                
		if( numSelf > 0 )
			xml.append(" self=\"").append(self).append("\" ");
		else
			xml.append(" self=\"NA\" ");
                   
		if( numResolved > 0 )
			xml.append(" resolved=\"").append(resolved).append("\" ");
		else
			xml.append(" resolved=\"NA\" ");
                   
		xml.append(" all=\"").append(all).append("\" ")
		   .append(" diff=\"").append(diff).append("\" ")
		   .append("/>").append(cwUtils.NEWL);
		
		return xml.toString();		
	}




    /**
    Get overall result of a specified type of assessor
    @return Hashtable with skill id as the key and average score of the skill as the element
    */
    public Hashtable overallResultByType(Connection con, String type)
        throws SQLException {
            
            DbCmAssessmentUnit dbAssU = new DbCmAssessmentUnit();
            dbAssU.asu_asm_id = asm_id;
            dbAssU.asu_type = type;
            DbCmAssessmentUnit[] dbAssUArray = dbAssU.getByType(con);
            
            Hashtable result = new Hashtable();
            result.put(HASH_NUM_OF_USER, new Integer(dbAssUArray.length));
            if( dbAssUArray.length == 0 ) 
                return result;            
            
            DbCmSkillSetCoverage skillSetCoverage[][] = new DbCmSkillSetCoverage[dbAssUArray.length][];
            ViewCmSkillCoverage Vsc = new ViewCmSkillCoverage();
            for(int i=0; i<dbAssUArray.length; i++)
                skillSetCoverage[i] = Vsc.getAllSkillsSetCoverage(con, dbAssUArray[i].asu_sks_skb_id);                        
            
            long skill_id;
            float ave_score;
            float weight=0;
outer:      for(int i=0; i<skillSetCoverage[0].length; i++) {
                ave_score = 0;
                weight = 0;
                for(int j=0; j<dbAssUArray.length; j++) {
                    if(skillSetCoverage[j][i].skb_type.equalsIgnoreCase(SKILL_SET))
                        continue outer;
                    ave_score += skillSetCoverage[j][i].ssc_level * dbAssUArray[j].asu_weight;
                    weight += dbAssUArray[j].asu_weight;                    
                }
                skill_id = skillSetCoverage[0][i].ssc_skb_id;                
                ave_score /= weight;
                result.put(new Long(skill_id), new Float(ave_score));
            }
            //store the total weight of this type's assessors
            if(weight == 0) {
                for(int j=0; j<dbAssUArray.length; j++) {
                    weight += dbAssUArray[j].asu_weight;
                }
            }
            CommonLog.debug("total weight of type " + type + " = " + weight);
            result.put(HASH_TOTAL_WEIGHT, new Float(weight));
            return result;
            
        }
    
    
    
    
    /**
    Save the resolved mark of the assessment
    @param float array : the score of the skill
    */
    public void saveResolvedScore(Connection con, Hashtable h_id_value, loginProfile prof, Timestamp last_upd_timestamp)
        throws SQLException, cwException, cwSysMessage {
            
            DbCmAssessmentUnit dbAssU = new DbCmAssessmentUnit();
            dbAssU.asu_asm_id = asm_id;
            long sks_id = dbAssU.getResolvedId(con);
            DbCmSkillSet skillSet = new DbCmSkillSet();
            if( sks_id == 0 ) {
                //create skill set to store resolved score
                skillSet.sks_type = ASSESSMENT_RESULT;
                skillSet.sks_owner_ent_id = prof.root_ent_id;
                skillSet.sks_create_usr_id = prof.usr_id;

                skillSet.ins(con);

                dbAssU.asu_sks_skb_id = skillSet.sks_skb_id;
                dbAssU.asu_ent_id = prof.usr_ent_id;
                dbAssU.updResolvedId(con);

                DbCmAssessment dbAss = new DbCmAssessment();
                dbAss.asm_id = asm_id;
                dbAss.asm_status = RESOLVED;
                dbAss.updStatus(con);
                long svy_id = dbAss.getSurveyId(con);
                ViewCmSkillCoverage Vsc = new ViewCmSkillCoverage();
                //DbCmSkillSetCoverage[] DbSsc = Vsc.getAllSkillsSetCoverage(con, svy_id);
				
				DbCmSkillSetCoverage DbSsc = new DbCmSkillSetCoverage();
                int count = 0;
                Enumeration enumeration = h_id_value.keys();
                while(enumeration.hasMoreElements()){
                	DbSsc.ssc_skb_id = ((Long)enumeration.nextElement()).longValue();
                    DbSsc.ssc_sks_skb_id = skillSet.sks_skb_id;
                    DbSsc.ssc_xml = "";
                    DbSsc.ssc_level = ( (Float)h_id_value.get(new Long(DbSsc.ssc_skb_id)) ).floatValue();
                    DbSsc.bAnswered = true;
                    DbSsc.ins(con);
                }
                
            } else {
                //update skill set 
                
                skillSet.sks_skb_id = sks_id;
                if( !skillSet.validUpdTimestamp(con, last_upd_timestamp) )
                    throw new cwSysMessage(CompetencyModule.SMSG_CMP_INVALID_TIMESTAMP);
                ViewCmSkillCoverage Vsc = new ViewCmSkillCoverage();
                DbCmSkillSetCoverage[] DbSsc = Vsc.getAllSkillsSetCoverage(con, sks_id);
                for(int i=0; i<DbSsc.length; i++) {
                    DbSsc[i].ssc_level = ( (Float)h_id_value.get(new Long(DbSsc[i].ssc_skb_id)) ).floatValue();
                    DbSsc[i].ssc_xml = "";
                    DbSsc[i].bAnswered = true;
                    DbSsc[i].upd(con);
                }
                skillSet.get(con);
                skillSet.sks_update_usr_id = prof.usr_id;
                skillSet.upd(con);
            }
            
            return;    
        }
 
    
    
    /**
    Get detial of the specified user
    @param long array of user entity id
    @return String of xml
    */
    public String generateSelectedNameList(Connection con, long[] entIds)
        throws SQLException, cwSysMessage {            
            
            DbCmAssessment dbAss = new DbCmAssessment();
            dbAss.asm_id = asm_id;
            dbAss.get(con);
            
            StringBuffer xml = new StringBuffer();
            xml.append("<assessment assessee_id=\"").append(dbAss.asm_ent_id).append("\">").append(cwUtils.NEWL);
            dbRegUser dbUser = new dbRegUser();
            xml.append(dbUser.getSelectedUserAsXML(con, entIds));
                        
            xml.append("</assessment>")
               .append(cwUtils.NEWL);
            
            return xml.toString();
        }
 

    /**
    Get the assessment info which assigned to the specified user
    @param long value of user entity id
    @return String of XML
    */
    public String getAssignedAss(Connection con, long usr_ent_id)
        throws SQLException {

        return getAssignedAss(con, usr_ent_id, false);
    }
    
    /**
    Get the assessment info which assigned to the specified user
    @param long value of user entity id
    @param withResolved the result set will also contain assessments assigned as resolver if withResolved = true
           else will only contain assessments assigned as assessors
    @return String of XML
    */
    public String getAssignedAss(Connection con, long usr_ent_id, boolean withResolved)
        throws SQLException {
            
            ViewCmAssessment ViewAss = new ViewCmAssessment();
            ResultSet rs = ViewAss.getAssignedAss(con, usr_ent_id, withResolved);
            StringBuffer xml = new StringBuffer();
            xml.append("<assessments>").append(cwUtils.NEWL);            
            while( rs.next() ) {
                xml.append("<assessment id=\"").append(rs.getLong("asm_id")).append("\" ")
                   .append(" type=\"").append(rs.getString("asm_type")).append("\" ")
                   .append(" asu_type=\"").append(rs.getString("asu_type")).append("\" ");
                long mod_res_id = rs.getLong("asm_mod_res_id");
                if(mod_res_id > 0) {
                    xml.append(" mod_res_id=\"").append(mod_res_id).append("\" ");
                }
                if(rs.getLong("asu_sks_skb_id") > 0) {
                    xml.append(" status=\"").append("INPROGRESS").append("\" ");
                } else {
                    xml.append(" status=\"").append("NOTSTARTED").append("\" ");
                }
                xml.append(" eff_end_detatime=\"").append(rs.getTimestamp("asm_eff_end_datetime")).append("\" ")
                    .append("title=\"").append(cwUtils.esc4XML(rs.getString("asm_title"))).append("\" ")
                    .append("assessee=\"").append(cwUtils.esc4XML(rs.getString("usr_display_bil"))).append("\">")
                   .append(cwUtils.NEWL);
                if(mod_res_id > 0) {
                    dbResource res = new dbResource();
                    res.res_id = mod_res_id;
                    xml.append(res.getTplXslAsXML(con));
                }
                xml.append("</assessment>");
            }
            xml.append("</assessments>");
            
            rs.close();
            return xml.toString();
        }
    
    
    /**
    Update assessment units info
    */
    public void updAssessmentUnits(Connection con, long[] entIdArray, String[] typeArray, long[] weightArray)
        throws SQLException, cwException {
            
            DbCmAssessmentUnit dbAssU = new DbCmAssessmentUnit();
            for(int i=0; i<entIdArray.length; i++) {                
                dbAssU.asu_asm_id = asm_id;
                dbAssU.asu_ent_id = entIdArray[i];
                dbAssU.asu_weight = weightArray[i];
                dbAssU.asu_type = typeArray[i];
                dbAssU.updAssessUnitInfo(con);
            }
            return;   
        }

/* 
    public void insAssessment(Connection con, DbCmAssessment asm, loginProfile prof, 
                              String sender_usr_id, String notify_msg_subject, 
                              String collect_msg_subject, boolean self_ass,
                              long[] mgmt_assessor_ent_id_list) 
        throws SQLException, cwSysMessage, cwException {
        //Get DB Time
        Timestamp curTime = cwSQL.getTime(con);
        
        //Set asm_eff_start_datetime to today if not given
        if(asm.asm_eff_start_datetime == null) {
            asm.asm_eff_start_datetime = curTime;
        }
        //Set asm_eff_start_datetime to tomorrow if not given
        if(asm.asm_eff_end_datetime == null) {
            asm.asm_eff_end_datetime = new Timestamp(curTime.getTime() + 24*60*60*1000);
        }            
        //Ask CmSkillSetManager to clone the picked skillset
        CmSkillSetManager skillSetMgr = new CmSkillSetManager();
        Vector v = skillSetMgr.clone2ASY(con, asm.asm_sks_skb_id, prof.usr_id);
        DbCmSkillSet skillSet = (DbCmSkillSet)v.elementAt(0);
        DbCmSkillSetCoverage[] DbSsc = (DbCmSkillSetCoverage[])v.elementAt(1);
                
        //Insert Assessment and AssessmentUnit with ASY's sks_skb_id
        asm.asm_sks_skb_id = skillSet.sks_skb_id;
        asm.asm_create_usr_id = prof.usr_id;
        asm.asm_create_timestamp = curTime;
        asm.asm_update_usr_id = prof.usr_id;
        asm.asm_update_timestamp = curTime;
        asm.ins(con);
        DbCmAssessmentUnit asu = new DbCmAssessmentUnit();
        asu.asu_asm_id = asm.asm_id;
        asu.asu_ent_id = prof.usr_ent_id;
        asu.asu_attempt_nbr = 1;
        asu.asu_weight = 1;
        asu.asu_type = "RESOLVED";
        asu.asu_submit_ind = false;
        asu.ins(con);
 
        //Create records for sending message
        CmAssessmentNotify assNot = new CmAssessmentNotify();
        assNot.insNotification(con, asm.asm_id, sender_usr_id, asm.asm_eff_start_datetime, notify_msg_subject, false, prof);
        assNot.insCollectionNotification(con, asm.asm_id, sender_usr_id, asm.asm_eff_end_datetime, collect_msg_subject, false, prof);
        
        //Create SELF Assessment Unit
        if( self_ass ) {
            asu.asu_asm_id = asm.asm_id;
            asu.asu_ent_id = asm.asm_ent_id;
            asu.asu_attempt_nbr = 1;
            asu.asu_weight = 1;
            asu.asu_type = SELF;
            asu.asu_submit_ind = false;
            asu.ins(con);
                    
            Hashtable msgXtpTable = ViewCmAssessment.getMsgTemplateId(con, asu.asu_asm_id);
            assNot.insNotificationRecipient(con, msgXtpTable, asu.asu_ent_id);
        }

        //Create MGMT Assessment Unit(s)
        if( mgmt_assessor_ent_id_list != null && mgmt_assessor_ent_id_list.length > 0) {
            for(int i=0; i<mgmt_assessor_ent_id_list.length; i++) {
                asu.asu_asm_id = asm.asm_id;
                asu.asu_ent_id = mgmt_assessor_ent_id_list[i];
                asu.asu_attempt_nbr = 1;
                asu.asu_weight = 1;
                asu.asu_type = MANAGEMENT;
                asu.asu_submit_ind = false;
                asu.ins(con);
                        
                Hashtable msgXtpTable = ViewCmAssessment.getMsgTemplateId(con, asu.asu_asm_id);
                assNot.insNotificationRecipient(con, msgXtpTable, asu.asu_ent_id);
            }
        }
        
        return;
    }
*/
/*
    public void updAssessment(Connection con, DbCmAssessment asm, loginProfile prof) 
        throws SQLException, cwSysMessage, cwException  {

        boolean clone = false; //indicate if need to clone a new Assessment Survey
        
        //Get orginal Assessment from DB
        DbCmAssessment old_asm = new DbCmAssessment();
        old_asm.asm_id = asm.asm_id;
        old_asm.get(con);

        //Set asm_eff_start/end_datetime to old one if not given
        if(asm.asm_eff_start_datetime == null) {
            asm.asm_eff_start_datetime = old_asm.asm_eff_start_datetime;
        }
        if(asm.asm_eff_end_datetime == null) {
            asm.asm_eff_end_datetime = old_asm.asm_eff_end_datetime;
        }

        //Cannot change SKT if Assessment started 
        if( old_asm.asm_sks_skb_id != asm.asm_sks_skb_id ) {
            if( ViewCmAssessment.ratorStarted(con, asm.asm_id) ) {
                throw new cwSysMessage(CompetencyModule.SMSG_CMP_ASS_STARTED);
            } else {
                clone = true;
            }
        } 

        //Cannot change notification date and collection date if they are passed
        Timestamp curTime = cwSQL.getTime(con);
        if( old_asm.asm_eff_start_datetime.before(curTime) && !asm.asm_eff_start_datetime.equals(old_asm.asm_eff_start_datetime)){
            throw new cwSysMessage(CompetencyModule.SMSG_CMP_INVALID_NOTIFICATION_DATE);
        }
        if( old_asm.asm_eff_end_datetime.before(curTime) && !asm.asm_eff_end_datetime.equals(old_asm.asm_eff_end_datetime)){
            throw new cwSysMessage(CompetencyModule.SMSG_CMP_INVALID_COLLECTION_DATE);
        }

        //Clone the picked skill set to ASY
        DbCmSkillSet skillSet = null;
        DbCmSkillSetCoverage[] DbSsc = null;
        if(clone) {
            CmSkillSetManager skillSetMgr = new CmSkillSetManager();
            Vector v = skillSetMgr.clone2ASY(con, asm.asm_sks_skb_id, prof.usr_id);
            skillSet = (DbCmSkillSet)v.elementAt(0);
            DbSsc = (DbCmSkillSetCoverage[])v.elementAt(1);
        }
        
        //Update assessment 
        if(clone) {
            asm.asm_sks_skb_id = skillSet.sks_skb_id;
        }
        asm.asm_update_usr_id = prof.usr_id;
        asm.upd(con);

        //Delete the old ASY
        if(clone) {
            DbCmSkillSetCoverage sscSweeper = new DbCmSkillSetCoverage();
            sscSweeper.delAllSkills(con, new long[] {old_asm.asm_sks_skb_id});
            DbCmSkillSet sksSweeper = new DbCmSkillSet();
            sksSweeper.del(con, new long[] {old_asm.asm_sks_skb_id});
        }

        //Update messaging records
        CmAssessmentNotify assNot = new CmAssessmentNotify();
        assNot.updNotification(con, asm.asm_id, asm.asm_eff_start_datetime, asm.asm_eff_end_datetime, prof);

        //Resolve this Assessment if it needs to be resolved now.
        asm.asm_status = old_asm.asm_status;
        if(isNeedAutoResolve(con, asm)) {
            resolveAssessment(con, asm, prof.usr_id, prof.root_ent_id);
        }
        return;
    }
*/

    /**
    Get an XML of <assessment_reference_data>
    @param con Connection to database
    @param root_ent_id organization site id
    */
    public String getAssessmentReferenceDataAsXML(Connection con, long root_ent_id) throws SQLException {
        return getAssessmentReferenceDataAsXML(con, root_ent_id, false);
    }
    
    public String getAssessmentReferenceDataAsXML(Connection con, long root_ent_id, boolean getApproverRole) throws SQLException {
        ViewCmSkillCoverage Vsc = new ViewCmSkillCoverage();
        StringBuffer xmlBuf = new StringBuffer(512);
        CmSkillSetManager ssmgr = new CmSkillSetManager();
        xmlBuf.append("<assessment_reference_data>");
        xmlBuf.append(ssmgr.getSkillSetListAsXML(con, "SKT", root_ent_id));
        if(getApproverRole) {
            String[] roleArray = dbRegUser.getApproverRolExtId(con);
            for(int i=0; i<roleArray.length; i++) {
                xmlBuf.append(AccessControlWZB.getRolXml(roleArray[i]));
            }
        }
        xmlBuf.append("</assessment_reference_data>");
        return xmlBuf.toString();
    }

    /** ins assessment for batch type, but not ins in batch, it is inserted one by one
    */

    public void insAssessment(Connection con, DbCmAssessment asm, loginProfile prof, 
                    String sender_usr_id, String notify_msg_subject, String collect_msg_subject, 
                    boolean self_ass, Timestamp selfNotifyDate, Timestamp selfDueDate,
                    long[] assessorEntIdList, String[] assessorTypeList,
                    String[] auaTypeList, Timestamp[] auaNotifyDateList, Timestamp[] auaDueDateList) 
        throws SQLException, cwSysMessage, cwException {
        //Get DB Time
        Timestamp curTime = cwSQL.getTime(con);
        
        //Set asm_eff_start_datetime to today if not given
        if(asm.asm_eff_start_datetime == null) {
            asm.asm_eff_start_datetime = curTime;
        }
        //Set asm_eff_start_datetime to tomorrow if not given
        if(asm.asm_eff_end_datetime == null) {
            asm.asm_eff_end_datetime = new Timestamp(curTime.getTime() + 24*60*60*1000);
        }            
        //Ask CmSkillSetManager to clone the picked skillset
        CmSkillSetManager skillSetMgr = new CmSkillSetManager();
        Vector v = skillSetMgr.clone2ASY(con, asm.asm_sks_skb_id, prof.usr_id);
        DbCmSkillSet skillSet = (DbCmSkillSet)v.elementAt(0);
        DbCmSkillSetCoverage[] DbSsc = (DbCmSkillSetCoverage[])v.elementAt(1);
                
        //Insert Assessment and AssessmentUnit with ASY's sks_skb_id
        asm.asm_sks_skb_id = skillSet.sks_skb_id;
        asm.asm_create_usr_id = prof.usr_id;
        asm.asm_create_timestamp = curTime;
        asm.asm_update_usr_id = prof.usr_id;
        asm.asm_update_timestamp = curTime;
        asm.ins(con);

        DbCmAssessmentUnit asu = new DbCmAssessmentUnit();
        DbCmAsmUnitTypeAttr aua = null;
        CmAssessmentNotify assNot = null;

        for(int i=0;i<auaTypeList.length;i++) {
            aua = new DbCmAsmUnitTypeAttr();
            aua.aua_asm_id = asm.asm_id;
            aua.aua_asu_type = auaTypeList[i];
            aua.aua_eff_start_timestamp = auaNotifyDateList[i];
            aua.aua_eff_end_timestamp = auaDueDateList[i];
            aua.ins(con);
            //Create records for sending message
            assNot = new CmAssessmentNotify();
            assNot.insNotification(con, asm.asm_id, aua.aua_asu_type, sender_usr_id, aua.aua_eff_start_timestamp, notify_msg_subject, false, prof);
            assNot.insCollectionNotification(con, asm.asm_id, aua.aua_asu_type, sender_usr_id, aua.aua_eff_end_timestamp, collect_msg_subject, false, prof);
        }

        //Get created Messaging Records to create AssessmentUnit Records
        Hashtable hMsgXtpTable = new Hashtable();
        for(int i=0; i<auaTypeList.length; i++) {
            Hashtable msgXtpTable = ViewCmAssessment.getMsgTemplateId(con, asm.asm_id, auaTypeList[i]);
            hMsgXtpTable.put(auaTypeList[i], msgXtpTable);
        }

        //Create Assessment Unit(s)
        if( assessorEntIdList != null && assessorEntIdList.length > 0) {
            for(int i=0; i<assessorEntIdList.length; i++) {
                asu.asu_asm_id = asm.asm_id;
                asu.asu_ent_id = assessorEntIdList[i];
                asu.asu_attempt_nbr = 1;
                asu.asu_weight = 1;
                asu.asu_type = assessorTypeList[i];
                asu.asu_submit_ind = false;
                asu.ins(con);
                Hashtable msgXtpTable = (Hashtable) hMsgXtpTable.get(assessorTypeList[i]);
                assNot.insNotificationRecipient(con, msgXtpTable, asu.asu_ent_id);
            }
        }
        
        return;
    }

    /**
    Construct a XML of the batch prep assessment
    @return String of XML
    */
    public String getPrepAssessmentXML(Connection con, DbCmAssessment[] batchAsm, 
                String usrType, boolean selfAsm, Timestamp selfNotifyDate, Timestamp selfDueDate,
                String[] assessorRoleList, Timestamp assessorNotifyDate, Timestamp assessorDueDate,
                String[] resolverRoleList, Timestamp resolverNotifyDate, Timestamp resolverDueDate)
        throws SQLException, cwSysMessage, cwException {
            
            StringBuffer xml = new StringBuffer();
            int weight = 1;
            for(int count=1, i=0;i<batchAsm.length;i++) {
                DbCmSkillSet skillSet = new DbCmSkillSet();
                skillSet.sks_skb_id = batchAsm[i].asm_sks_skb_id;
                skillSet.get(con);
                
                dbRegUser dbUser = new dbRegUser();
                dbUser.usr_ent_id = batchAsm[i].asm_ent_id;
                try{
                    dbUser.get(con);
                }catch(qdbException e) {
                    throw new cwException(e.getMessage());
                }

                Timestamp curTime = cwSQL.getTime(con);
                xml.append("<assessment number=\"").append(count++).append("\" ")
                    .append(" ent_id=\"").append(batchAsm[i].asm_ent_id).append("\" ")
                    .append(" usr_id=\"").append(dbUser.usr_id).append("\" ")
                    .append(" display_bil=\"").append(cwUtils.esc4XML(dbUser.usr_display_bil)).append("\" ")
                    .append(" title=\"").append(cwUtils.esc4XML(batchAsm[i].asm_title)).append("\" ")
                    .append(" sks_id=\"").append(batchAsm[i].asm_sks_skb_id).append("\" ")
                    .append(" sks_title=\"").append(cwUtils.esc4XML(skillSet.sks_title)).append("\" ")
                    .append(" review_start=\"").append(batchAsm[i].asm_review_start_datetime).append("\" ")
                    .append(" review_end=\"").append(batchAsm[i].asm_review_end_datetime).append("\" ")
                    .append(" due_datetime=\"").append(batchAsm[i].asm_eff_end_datetime).append("\" ")
                    .append(" notification_datetime=\"").append(batchAsm[i].asm_eff_start_datetime).append("\" ")
                    .append(" last_upd_timestamp=\"").append(batchAsm[i].asm_update_timestamp).append("\" ")
                    .append(" auto_resolved_ind=\"").append(batchAsm[i].asm_auto_resolved_ind).append("\" ");
                xml.append(" >").append(cwUtils.NEWL);
                xml.append("<assessor_type_attr_list>");
                
                DbCmAsmUnitTypeAttr aua = null;

                aua = new DbCmAsmUnitTypeAttr();
                aua.aua_asm_id = asm_id;
                aua.aua_asu_type = SELF;
                aua.aua_eff_start_timestamp = selfNotifyDate;
                aua.aua_eff_end_timestamp = selfDueDate;
                xml.append(assessorTypeAttrXML(con, aua));

                aua = new DbCmAsmUnitTypeAttr();
                aua.aua_asm_id = asm_id;
                aua.aua_asu_type = MANAGEMENT;
                aua.aua_eff_start_timestamp = assessorNotifyDate;
                aua.aua_eff_end_timestamp = assessorDueDate;
                xml.append(assessorTypeAttrXML(con, aua));

                aua = new DbCmAsmUnitTypeAttr();
                aua.aua_asm_id = asm_id;
                aua.aua_asu_type = RESOLVED;
                aua.aua_eff_start_timestamp = resolverNotifyDate;
                aua.aua_eff_end_timestamp = resolverDueDate;
                xml.append(assessorTypeAttrXML(con, aua));

                aua = new DbCmAsmUnitTypeAttr();
                aua.aua_asm_id = asm_id;
                aua.aua_asu_type = PEERS;
                aua.aua_eff_start_timestamp = null;
                aua.aua_eff_end_timestamp = null;
                xml.append(assessorTypeAttrXML(con, aua));

                aua = new DbCmAsmUnitTypeAttr();
                aua.aua_asm_id = asm_id;
                aua.aua_asu_type = REPORTS;
                aua.aua_eff_start_timestamp = null;
                aua.aua_eff_end_timestamp = null;
                xml.append(assessorTypeAttrXML(con, aua));

                aua = new DbCmAsmUnitTypeAttr();
                aua.aua_asm_id = asm_id;
                aua.aua_asu_type = CLIENTS;
                aua.aua_eff_start_timestamp = null;
                aua.aua_eff_end_timestamp = null;
                xml.append(assessorTypeAttrXML(con, aua));

                xml.append("</assessor_type_attr_list>");
                
                xml.append("<assessor_list>");
                if(selfAsm) {
                    xml.append(assessorXML(con, batchAsm[i].asm_ent_id, weight, SELF));
                }
                xml.append("</assessor_list>");
                xml.append("</assessment>").append(cwUtils.NEWL);
            }
            
            return xml.toString();
        }

    public String getAssessmentUnitTypeAttrAsXML(Connection con) throws SQLException {
        StringBuffer xmlBuffer = new StringBuffer();
        DbCmAsmUnitTypeAttr aua = new DbCmAsmUnitTypeAttr();

        xmlBuffer.append("<assessor_type_attr_list>");

        // for each type 
        aua = new DbCmAsmUnitTypeAttr();
        aua.aua_asm_id = asm_id;
        aua.aua_asu_type = MANAGEMENT;
        aua.get(con);
        xmlBuffer.append(assessorTypeAttrXML(con, aua));
        
        aua = new DbCmAsmUnitTypeAttr();
        aua.aua_asm_id = asm_id;
        aua.aua_asu_type = PEERS;
        aua.get(con);
        xmlBuffer.append(assessorTypeAttrXML(con, aua));

        aua = new DbCmAsmUnitTypeAttr();
        aua.aua_asm_id = asm_id;
        aua.aua_asu_type = REPORTS;
        aua.get(con);
        xmlBuffer.append(assessorTypeAttrXML(con, aua));

        aua = new DbCmAsmUnitTypeAttr();
        aua.aua_asm_id = asm_id;
        aua.aua_asu_type = CLIENTS;
        aua.get(con);
        xmlBuffer.append(assessorTypeAttrXML(con, aua));

        aua = new DbCmAsmUnitTypeAttr();
        aua.aua_asm_id = asm_id;
        aua.aua_asu_type = SELF;
        aua.get(con);
        xmlBuffer.append(assessorTypeAttrXML(con, aua));

        aua = new DbCmAsmUnitTypeAttr();
        aua.aua_asm_id = asm_id;
        aua.aua_asu_type = RESOLVED;
        aua.get(con);
        xmlBuffer.append(assessorTypeAttrXML(con, aua));

        xmlBuffer.append("</assessor_type_attr_list>");
        
        return xmlBuffer.toString();
    }
    
    public boolean isExitAssessmentUnit(Connection con,long asm_id, String type) throws SQLException{
    	String sql = "select count(*) num from cmAssessmentUnit where asu_asm_id=? and asu_type=?";
    	PreparedStatement stmt = con.prepareStatement(sql);
    	stmt.setLong(1, asm_id);
    	stmt.setString(2, type);
    	ResultSet rs = stmt.executeQuery();
    	boolean exit = false;
    	if(rs.next()){
    		if(rs.getInt("num") >0){
    			exit = true;
    		}
    	}
    	if(stmt !=null){
    		stmt.close();
    	}
		return exit;	
    }

    public void updAssessment(Connection con, DbCmAssessment asm, loginProfile prof
                             ,String sender_usr_id, String[] auaTypeList
                             ,Timestamp[] auaNotifyDateList, Timestamp[] auaDueDateList
                             ,String notify_msg_subject, String collect_msg_subject)
        throws SQLException, cwSysMessage, cwException  {

        boolean clone = false; //indicate if need to clone a new Assessment Survey
        
        //Get orginal Assessment from DB
        DbCmAssessment old_asm = new DbCmAssessment();
        old_asm.asm_id = asm.asm_id;
        old_asm.get(con);

        //Cannot change SKT if Assessment started 
        if( old_asm.asm_sks_skb_id != asm.asm_sks_skb_id ) {
            if( ViewCmAssessment.ratorStarted(con, asm.asm_id) ) {
                throw new cwSysMessage(CompetencyModule.SMSG_CMP_ASS_STARTED);
            } else {
                clone = true;
            }
        } 

        //Cannot change notification date and collection date if they are passed
        Timestamp curTime = cwSQL.getTime(con);
/*
        if( old_asm.asm_eff_start_datetime.before(curTime) && !asm.asm_eff_start_datetime.equals(old_asm.asm_eff_start_datetime)){
            throw new cwSysMessage(CompetencyModule.SMSG_CMP_INVALID_NOTIFICATION_DATE);
        }
        if( old_asm.asm_eff_end_datetime.before(curTime) && !asm.asm_eff_end_datetime.equals(old_asm.asm_eff_end_datetime)){
            throw new cwSysMessage(CompetencyModule.SMSG_CMP_INVALID_COLLECTION_DATE);
        }
*/
        // no need to check if effective start datetime is earlier than current time
        // (2003-07-16 kawai)
        //if( asm.asm_eff_start_datetime.before(curTime)){
        //    throw new cwSysMessage(CompetencyModule.SMSG_CMP_INVALID_NOTIFICATION_DATE);
        //}
        if( asm.asm_eff_end_datetime.before(curTime)){
            throw new cwSysMessage(CompetencyModule.SMSG_CMP_INVALID_COLLECTION_DATE);
        }

        //Clone the picked skill set to ASY
        DbCmSkillSet skillSet = null;
        DbCmSkillSetCoverage[] DbSsc = null;
        if(clone) {
            CmSkillSetManager skillSetMgr = new CmSkillSetManager();
            Vector v = skillSetMgr.clone2ASY(con, asm.asm_sks_skb_id, prof.usr_id);
            skillSet = (DbCmSkillSet)v.elementAt(0);
            DbSsc = (DbCmSkillSetCoverage[])v.elementAt(1);
        }
        
        //Update assessment 
        if(clone) {
            asm.asm_sks_skb_id = skillSet.sks_skb_id;
        }
        asm.asm_update_usr_id = prof.usr_id;
        asm.upd(con);

        //Delete the old ASY
        if(clone) {
            DbCmSkillSetCoverage sscSweeper = new DbCmSkillSetCoverage();
            sscSweeper.delAllSkills(con, new long[] {old_asm.asm_sks_skb_id});
            DbCmSkillSet sksSweeper = new DbCmSkillSet();
            sksSweeper.del(con, new long[] {old_asm.asm_sks_skb_id});
        }

        //Resolve this Assessment if it needs to be resolved now.
        asm.asm_status = old_asm.asm_status;
        if(isNeedAutoResolve(con, asm)) {
            resolveAssessment(con, asm, dbRegUser.getSiteDefaultSysProfile(con,prof));
        }

        // insert cmAsmUnitTypeAttr 
        for(int i=0; i<auaTypeList.length; i++) {
            DbCmAsmUnitTypeAttr aua = new DbCmAsmUnitTypeAttr();
            aua.aua_asm_id = asm.asm_id;
            aua.aua_asu_type = auaTypeList[i];
            if(auaNotifyDateList[i] == null || auaDueDateList[i] == null) {
                aua.del(con);
            } else {
                aua.aua_eff_start_timestamp = auaNotifyDateList[i];
                aua.aua_eff_end_timestamp = auaDueDateList[i];              	
            	if(aua.isExist(con)) {
            		aua.upd(con);
            	} else {
            		aua.ins(con);
            	}
                
            }
        }

        //insert or update Messaging records
        CmAssessmentNotify assNot = new CmAssessmentNotify();
        for(int i=0; i<auaTypeList.length; i++) {
            Hashtable msgXtpTable = ViewCmAssessment.getMsgTemplateId(con, asm.asm_id, auaTypeList[i]);
            if(msgXtpTable==null || msgXtpTable.size() == 0) {
                assNot.insNotification(con, asm.asm_id, auaTypeList[i], sender_usr_id, auaNotifyDateList[i], notify_msg_subject, false, prof);
                assNot.insCollectionNotification(con, asm.asm_id, auaTypeList[i], sender_usr_id, auaDueDateList[i], collect_msg_subject, false, prof);
            } else {
                assNot.updNotification(con, asm.asm_id, auaTypeList[i], auaNotifyDateList[i], auaDueDateList[i], prof);
            }
        }
        
        return;
    }

    /**
    Update assessment units info
    */
    public void updAssessmentUnits(Connection con, long asm_id, long[] entIdArray, String asu_type, long weight)
        throws SQLException, cwException {
            
            boolean[] isAsuExist = new boolean[entIdArray.length];
            
            //Remove assessment units not in the input list
            DbCmAssessmentUnit rmAsu = new DbCmAssessmentUnit();
            rmAsu.asu_asm_id = asm_id;
            rmAsu.asu_type = asu_type;
            rmAsu.delNotInListByAsuType(con, entIdArray);

            //insert assessment unit
            for(int i=0; i<entIdArray.length; i++) {                
                DbCmAssessmentUnit dbAssU = new DbCmAssessmentUnit();
                dbAssU.asu_asm_id = asm_id;
                dbAssU.asu_ent_id = entIdArray[i];
                dbAssU.asu_weight = weight;
                dbAssU.asu_type = asu_type;
                dbAssU.asu_attempt_nbr = 1;
                if(!dbAssU.existWithType(con)) {
                    isAsuExist[i] = false;
                    dbAssU.ins(con);
                } else {
                    isAsuExist[i] = true;
                }
            }
            
            //get Messaging records
            Hashtable msgXtpTable = ViewCmAssessment.getMsgTemplateId(con, asm_id, asu_type);
            CmAssessmentNotify assNot = new CmAssessmentNotify();

            //Remove message recipients not in the input list
            assNot.delNotificationRecipientNotInListByAsuType(con, asm_id, asu_type, entIdArray);
            
            //insert messaging records
            for(int i=0; i<entIdArray.length; i++) {                
                if(!isAsuExist[i]) {
                    assNot.insNotificationRecipient(con, msgXtpTable, entIdArray[i]);
                }
            }
            return;
        }

    private String assessorTypeAttrXML(Connection con, DbCmAsmUnitTypeAttr aua) throws SQLException {
        StringBuffer xmlBuffer = new StringBuffer();
        if(isExitAssessmentUnit(con, aua.aua_asm_id, aua.aua_asu_type)){
	        xmlBuffer.append("<assessor_type type=\"").append(aua.aua_asu_type).append("\" ");
	        if(aua.aua_eff_start_timestamp != null) {
	            xmlBuffer.append(" notify_datetime=\"").append(aua.aua_eff_start_timestamp).append("\" ");
	        } else {
	            xmlBuffer.append(" notify_datetime=\"\" ");
	        }
	
	        if(aua.aua_eff_end_timestamp != null) {
	            xmlBuffer.append(" due_datetime=\"").append(aua.aua_eff_end_timestamp).append("\" ");
	        } else {
	            xmlBuffer.append(" due_datetime=\"\" ");
	        }
	        xmlBuffer.append("/>");
        }
        
        return xmlBuffer.toString();
    }

    private String assessorXML(Connection con, long entId, int weight, String type) throws cwException {
        StringBuffer xmlBuffer = new StringBuffer();
        
        dbRegUser dbUser = new dbRegUser();
        dbUser.usr_ent_id = entId;
        try{
            dbUser.get(con);
        }catch(qdbException e) {
            throw new cwException(e.getMessage());
        }
        xmlBuffer.append("<assessor ent_id=\"").append(entId).append("\" ");
        xmlBuffer.append(" weight=\"").append(weight).append("\" ");
        xmlBuffer.append(" type=\"").append(type).append("\" ");
        xmlBuffer.append(" display_bil=\"").append(dbUser.usr_display_bil).append("\" ");
        
        xmlBuffer.append("/>");
        
        return xmlBuffer.toString();
    }
    
    /**
     * Get total number of assessee notified in the specified assessment.
     * @param con
     * @param asm_id Assessment id
     * @return number of assessee notified
     * @throws SQLException
     */
	public int numberOfNotify(Connection con, long asm_id)
		throws SQLException{
			Hashtable h_type_ts = DbCmAsmUnitTypeAttr.getAllTypeEffStartTimestamp(con, asm_id);
			Hashtable h_type_num = DbCmAssessmentUnit.getAssesseeNumByType(con, asm_id);
			int total = 0;
			Timestamp cur_time = cwSQL.getTime(con);
			Enumeration enumeration = h_type_ts.keys();
			while(enumeration.hasMoreElements()){
				String type = (String) enumeration.nextElement();
				if( !type.equalsIgnoreCase(CmAssessmentManager.RESOLVED) ){
					if( h_type_num.containsKey(type)){
						if( ((Timestamp) h_type_ts.get(type)).before(cur_time) ) {
							total += ((Integer) h_type_num.get(type)).intValue();
						}
					}
				}
			}
			return total;
		}
    
    
}

