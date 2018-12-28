package com.cw.wizbank.ae;

import java.io.IOException;
import java.io.StringReader;
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
import java.util.Vector;

import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.AttributeList;
import org.xml.sax.HandlerBase;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.cw.wizbank.accesscontrol.AcItem;
import com.cw.wizbank.accesscontrol.acSite;
import com.cw.wizbank.ae.db.DbItemTargetRuleDetail;
import com.cw.wizbank.ae.db.DbItemType;
import com.cw.wizbank.ae.db.DbLearningSoln;
import com.cw.wizbank.ae.db.sql.OuterJoinSqlStatements;
import com.cw.wizbank.ae.db.view.ViewItemTargetGroup;
import com.cw.wizbank.ae.db.view.ViewLearningSoln;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.config.organization.learningplan.LearningPlan;
import com.cw.wizbank.db.DbCmSkill;
import com.cw.wizbank.db.DbIndustryCode;
import com.cw.wizbank.db.DbUserGrade;
import com.cw.wizbank.qdb.dbEntity;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbUserGroup;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;

public class aeLearningSoln {
    public class XMLparser extends HandlerBase {
        Long period_id;
        Long ent_id;
        Vector grade_lst;
        Vector period_lst;
        
        XMLparser(Vector lst) {
            grade_lst = lst;
            period_lst = new Vector();
        }
        
        public void startElement(String name, AttributeList inAttribList) throws SAXException {
            if (name.equals("period")) {
                period_id = new Long(inAttribList.getValue("id")); 
            } else if (name.equals("ent")) {
                ent_id = new Long(inAttribList.getValue("id"));

                for (int i=0; i<grade_lst.size(); i++) {
                    Long grade_ent_id = (Long)grade_lst.elementAt(i);

                    if (grade_ent_id.equals(ent_id) && !period_lst.contains(period_id)) {
                        period_lst.addElement(period_id);
                    }
                }
            }
        }
    }

    public static final boolean debug = false;
    public static final int threshold = 1;
    
    public static final String COMPULSORY = "COMPULSORY";
    public static final String ELECTIVE = "ELECTIVE";
    public static final String OTHER = "OTHER";
    public static final String TRAINING_NEEDS = "TRAINING_NEEDS";
    private static final String DESC = "DESC";
    private static final String ASC = "ASC";
    public long usr_id = 0;;
    
    public String myLearningSoln(Connection con, HttpSession sess, loginProfile prof, long usr_ent_id, Vector soln_type, Vector item_type, String[] targeted_item_apply_method_lst, boolean bShowAllTypes, String[] order_bys, String[] sort_bys) throws SQLException, qdbException, cwException, cwSysMessage {
//long my_time = System.currentTimeMillis();
//Date my_date = new Date(my_time);
//System.out.println(">>>> TIME (1) = " + my_date.toString());
//System.out.println("My LearningSoln");
//System.out.println("usr_id: " + prof.usr_ent_id);

        this.usr_id = prof.usr_ent_id;
        Timestamp curTime = cwSQL.getTime(con);
        long grade_ent_id = 0;

        Vector user_group_ent_ids = new Vector();
        Vector user_industry_ent_ids = new Vector();
        Vector user_grade_peer_ent_ids = new Vector();
        Hashtable peers = new Hashtable();
//        Vector order_grade_lst = new Vector();
        long[] target_group = new long[3];
        Hashtable target_soln = null;
        Vector itm_lst = null;
        String period_xml = null;
        boolean targetByPeer = acSite.isTargetByPeer(con, prof.root_ent_id);
        if (soln_type == null || soln_type.size() == 0) {
            soln_type = new Vector();
            soln_type.addElement(COMPULSORY);
            soln_type.addElement(ELECTIVE);
            soln_type.addElement(OTHER);
            soln_type.addElement(TRAINING_NEEDS);
        }
//        System.out.println("soln_type" + soln_type.size());
        
        if (item_type == null || item_type.size() == 0) {
            if (bShowAllTypes) {
                String[] all_item_types = DbItemType.getAllItemTypeIdInOrg(con, prof.root_ent_id);
                if (item_type == null) {
                    item_type = new Vector();
                }
                for (int i=0;i<all_item_types.length;i++) {
                    item_type.addElement(all_item_types[i]);
                }
            }else {
                item_type = DbItemType.getApplicableItemType(con, prof.root_ent_id);
            }
        }
        
//System.out.println(">>>> TIME (2) = " + (System.currentTimeMillis() - my_time));          

        grade_ent_id = DbUserGrade.getGradeEntId(con, usr_ent_id);
//System.out.println(">>>> TIME (3) = " + (System.currentTimeMillis() - my_time));          
        user_group_ent_ids = dbUserGroup.getUserParentEntIds(con, usr_ent_id);
//System.out.println(">>>> TIME (4) = " + (System.currentTimeMillis() - my_time));          
        user_industry_ent_ids = DbIndustryCode.getIndCodeEntIds(con, usr_ent_id);
//System.out.println(">>>> TIME (5) = " + (System.currentTimeMillis() - my_time));          
        //DbUserGrade.getPeers(con, grade_ent_id, user_grade_peer_ent_ids, peers);
        if (grade_ent_id > 0){
            if( targetByPeer ) {
                DbUserGrade.getPeers(con, grade_ent_id, user_grade_peer_ent_ids, peers);
            }else {            
                Long gradeEntId = new Long(grade_ent_id);
                user_grade_peer_ent_ids.addElement(gradeEntId);
                Hashtable ugrTable = DbUserGrade.getDisplayName(con, "(" + grade_ent_id + ")");
                peers.put(gradeEntId, (String)ugrTable.get(gradeEntId.toString()));
            }
        }
        period_xml = ViewLearningSoln.getPeriodXML(con, prof.root_ent_id);
        target_soln = (Hashtable)sess.getAttribute(aeLearningPlan.SESS_LEARNING_PLAN_HASH);

        if (target_soln == null) {
            Vector combination = new Vector();
            target_soln = new Hashtable();
            
            Hashtable h_v_entity = new Hashtable();

            if (user_group_ent_ids != null && user_group_ent_ids.size() > 0) {
                h_v_entity.put(dbEntity.ENT_TYPE_USER_GROUP, user_group_ent_ids);
            }
            
            if (user_industry_ent_ids != null && user_industry_ent_ids.size() > 0) {
                h_v_entity.put(dbEntity.ENT_TYPE_INDUSTRY_CODE, user_industry_ent_ids);
            }
            
            if (user_grade_peer_ent_ids.size() != 0) {
                Vector user_grade_vec = null;
                
                for (int i=0; i<user_grade_peer_ent_ids.size(); i++) {
                    user_grade_vec = new Vector();
                    Long grade_id = (Long)user_grade_peer_ent_ids.elementAt(i);

                    user_grade_vec.addElement(grade_id);
                    h_v_entity.put(dbEntity.ENT_TYPE_USER_GRADE, user_grade_vec);
                    //searchItem(con, h_v_entity, target_soln, grade_id, targeted_item_apply_method_lst);
                    searchItem(con, h_v_entity, target_soln, grade_id, null);
                }
            } else {
                //searchItem(con, h_v_entity, target_soln, new Long(0), targeted_item_apply_method_lst);
                searchItem(con, h_v_entity, target_soln, new Long(0), null);
            }
            
/*            for (int i=0; i<user_group_ent_ids.size(); i++) {
                combination.addElement(user_group_ent_ids.elementAt(i));

                for (int j=0; j<user_grade_peer_ent_ids.size(); j++) {
                    Long grade_id = (Long)user_grade_peer_ent_ids.elementAt(j);
                    combination.addElement(user_grade_peer_ent_ids.elementAt(j));

                    if (user_industry_ent_ids.size() != 0) {
                        for (int k=0; k<user_industry_ent_ids.size(); k++) {
                            combination.addElement(user_industry_ent_ids.elementAt(k));                            
    //                        aeLearningSoln.searchItem(con, combination, target_soln, grade_id, targeted_item_apply_method_lst);
                            aeLearningSoln.searchItem(con, combination, target_soln, grade_id);
                            combination.removeElement(combination.lastElement());
                        }
                    } else {
    //                    aeLearningSoln.searchItem(con, combination, target_soln, grade_id, targeted_item_apply_method_lst);
                        aeLearningSoln.searchItem(con, combination, target_soln, grade_id);
                    }

                    combination.removeElement(combination.lastElement());            
                }

                combination.removeElement(combination.lastElement());                        
            }*/

//            sess.setAttribute(aeLearningPlan.SESS_LEARNING_PLAN_HASH, target_soln);
        }
//System.out.println(">>>> TIME (6) = " + (System.currentTimeMillis() - my_time));          
/*        if (target_soln == null) {
//System.out.println("NOT FROM SESSION!!!!!!!!!!!!!!!!!!!!!");
            Vector combination = new Vector();
            target_soln = new Hashtable();

            for (int i=0; i<user_group_ent_ids.size(); i++) {
                combination.addElement(user_group_ent_ids.elementAt(i));

                if (user_grade_peer_ent_ids.size() != 0) {
                    for (int j=0; j<user_grade_peer_ent_ids.size(); j++) {
                        Long grade_id = (Long)user_grade_peer_ent_ids.elementAt(j);
                        combination.addElement(user_grade_peer_ent_ids.elementAt(j));

                        if (user_industry_ent_ids.size() != 0) {
                            for (int k=0; k<user_industry_ent_ids.size(); k++) {
                                combination.addElement(user_industry_ent_ids.elementAt(k));
    //                            aeLearningSoln.searchItem(con, combination, target_soln, grade_id, targeted_item_apply_method_lst);
                                aeLearningSoln.searchItem(con, combination, target_soln, grade_id);
                                combination.removeElement(combination.lastElement());
                            }
                        } else {
    //                        aeLearningSoln.searchItem(con, combination, target_soln, grade_id, targeted_item_apply_method_lst);
                            aeLearningSoln.searchItem(con, combination, target_soln, grade_id);
                        }

                        combination.removeElement(combination.lastElement());            
                    }
                } else {
    //                aeLearningSoln.searchItem(con, combination, target_soln, new Long(0), targeted_item_apply_method_lst);
                    aeLearningSoln.searchItem(con, combination, target_soln, new Long(0));
                }

                combination.removeElement(combination.lastElement());                        
            }

            sess.setAttribute(aeLearningPlan.SESS_LEARNING_PLAN_HASH, target_soln);
        } else {
//System.out.println("FROM SESSION!!!!!!!!!!!!!!!!!!!!!");
        }            */

Enumeration enumeration = target_soln.keys();
while(enumeration.hasMoreElements()) {
    Long id = (Long)enumeration.nextElement();
    Vector lst = (Vector)target_soln.get(id);
    
    if (lst != null) {
        for (int i=0; i<lst.size(); i++) {
//            System.out.println((Long)lst.elementAt(i) + "; ");   
        }
    }
    
}

        // find aeItemPlan (with attendance)
        //Hashtable other_soln = ViewLearningSoln.getMyLeanringSoln(con, usr_ent_id, grade_ent_id);
        Hashtable other_soln;
        if( targetByPeer )
            other_soln = ViewLearningSoln.getMyLeanringSoln(con, usr_ent_id, grade_ent_id);
        else
            other_soln = ViewLearningSoln.getMyLeanringSoln(con, usr_ent_id, 0);
        
//System.out.println(">>>> TIME (7) = " + (System.currentTimeMillis() - my_time));          
Enumeration a = other_soln.keys();
while (a.hasMoreElements()) {
    Long key = (Long)a.nextElement();
}
        //Hashtable other_soln_created_by = ViewLearningSoln.checkSelfInitiated(con, usr_ent_id, grade_ent_id);
        Vector other_soln_created_by;
        if( targetByPeer )
            other_soln_created_by = ViewLearningSoln.checkSelfInitiated(con, usr_ent_id, grade_ent_id);
        else
            other_soln_created_by = ViewLearningSoln.checkSelfInitiated(con, usr_ent_id, 0);

        Vector other_soln_not_created_by;
        if( targetByPeer )
            other_soln_not_created_by = ViewLearningSoln.checkSelfInitiated(con, usr_ent_id, grade_ent_id, false);
        else
            other_soln_not_created_by = ViewLearningSoln.checkSelfInitiated(con, usr_ent_id, 0, false);

        // find trainingNeedsItem
        Hashtable training_needs_soln = ViewLearningSoln.getMyTrainingNeeds(con, usr_ent_id, threshold);

        // get itm_status
        Hashtable temp_info = new Hashtable();
        Vector temp_vector = new Vector();
        Vector temp_lst = new Vector();

        Enumeration enum_temp_soln = target_soln.keys();
            
        while (enum_temp_soln.hasMoreElements()) {
            temp_lst.addElement((Long)enum_temp_soln.nextElement());
        }
            
        ViewLearningSoln.getItemInfo(con, temp_lst, temp_info, temp_vector);
//System.out.println(">>>> TIME (8) = " + (System.currentTimeMillis() - my_time));          
        Vector v_targeted_item_apply_method_lst = null;
        
        if (targeted_item_apply_method_lst != null && targeted_item_apply_method_lst.length > 0) {
            v_targeted_item_apply_method_lst = new Vector();
            
            for (int i=0; i<targeted_item_apply_method_lst.length; i++) {
                v_targeted_item_apply_method_lst.addElement(targeted_item_apply_method_lst[i].toLowerCase());
            }
        }        
//System.out.println(">>>> TIME (9) = " + (System.currentTimeMillis() - my_time));          
        
        itm_lst = new Vector();
        Enumeration enum_target_soln = target_soln.keys();
        Enumeration enum_other_soln = other_soln.keys();
        Enumeration enum_training_needs_soln = training_needs_soln.keys();
        
        while (enum_target_soln.hasMoreElements()) {
            Long itm_id = (Long)enum_target_soln.nextElement();
            aeItem item = (aeItem)temp_info.get(itm_id);        
            
            if ((v_targeted_item_apply_method_lst == null) || (item.itm_apply_method != null && v_targeted_item_apply_method_lst.contains(item.itm_apply_method.toLowerCase()))) {
                itm_lst.addElement(itm_id);   
            }
        }
//System.out.println(">>>> TIME (10) = " + (System.currentTimeMillis() - my_time));          
                
        while (enum_other_soln.hasMoreElements()) {
            Long itm_id = (Long)enum_other_soln.nextElement();

            itm_lst.addElement(itm_id);   
        }

        while (enum_training_needs_soln.hasMoreElements()) {
            Long itm_id = (Long)enum_training_needs_soln.nextElement();

            itm_lst.addElement(itm_id);   
        }

        cwUtils.removeDuplicate(itm_lst);
        
        Hashtable soln_info = ViewLearningSoln.getApplicationAndAttendance(con, usr_ent_id, itm_lst);
//System.out.println(">>>> TIME (11) = " + (System.currentTimeMillis() - my_time));          
//a = soln_info.keys();
//while (a.hasMoreElements()) {
//    System.out.println("> " + ((Long)a.nextElement()).longValue());   
//}
//        Enumeration enum_soln_info = soln_info.keys();
//        Hashtable version_hash = ViewLearningSoln.getItemVersionInfo(con, enum_soln_info);
//        handleOldVersion(soln_info, version_hash);

        // handle item type (filter book, video, ...)
        Hashtable item_info = new Hashtable();
        Vector item_vector = new Vector();
        ViewLearningSoln.getItemInfo(con, itm_lst, item_info, item_vector);

        // added for provide sorting in java code 
        // if sorting param is provided, else no sorting is made
        if(order_bys!=null && sort_bys!=null) {
//            System.out.println("==> in sort");
            boolean validFlag = true;
            Vector sortColVec = new Vector();
            sortColVec.add("TYPE");
            sortColVec.add("TITLE");
            sortColVec.add("NATURE");
            for(int k=0; k<order_bys.length; k++) {
                if(!sortColVec.contains(order_bys[k].toUpperCase())) {
                    validFlag = false;
                }
            }
            
            if(validFlag) {
                Vector sortedItemVec = new Vector();
                HashMap itemMap = new HashMap();

                // collect info from item_vector for perform sorting
                for (int i=0; i<item_vector.size(); i++) {
                    Long itm_id = (Long)item_vector.elementAt(i);
                    aeItem item = (aeItem)item_info.get(itm_id);
                    
                    itemMap = new HashMap();
                    itemMap.put("ID", itm_id);
                    itemMap.put("TYPE", item.itm_type);
                    itemMap.put("TITLE", item.itm_title);
                    String nature = "";
//                    if(aeItem.isTargetedLearner(con, item.itm_id, usr_ent_id, DbItemTargetEntity.ITE_COMPULSORY)) {
//                        nature = COMPULSORY;
//                    } else {
                        nature = ELECTIVE;
//                    }
                    itemMap.put("NATURE", nature);
                    sortedItemVec.addElement(itemMap);
                }

                // perform sorting
                sortedItemVec = sorting(sortedItemVec, order_bys, sort_bys);
                
                // assign sorted result back to the item vector
                for(int i=0; i<sortedItemVec.size(); i++) {
                    itemMap = (HashMap) sortedItemVec.elementAt(i);
                    Long itm_id = (Long) itemMap.get("ID");
                    item_vector.setElementAt(itm_id, i);
                }
            }
        }

//System.out.println(">>>> TIME (12) = " + (System.currentTimeMillis() - my_time));          
//a = item_info.keys();
//while (a.hasMoreElements()) {
//    System.out.println(">> " + ((Long)a.nextElement()).longValue());   
//}
//System.out.println("14");
        
        // output xml (get period xml; output compulsory, then elective and other items)
        StringBuffer xml_compulsory = new StringBuffer();
        StringBuffer xml_elective = new StringBuffer();
        StringBuffer xml_other = new StringBuffer();
        StringBuffer xml_training_needs = new StringBuffer();
        Enumeration enum_item_info = item_info.keys();

        Vector grade_vec = new Vector();
        grade_vec.addElement(new Long(grade_ent_id));
        Vector my_period_lst = getPeriodList(grade_vec, period_xml);            

        AcItem acitm = new AcItem(con);
        boolean hasPreApprReadPrivilege = true;
        boolean hasOffReadPrivilege = acitm.hasOffReadPrivilege(usr_ent_id, prof.current_role);
//System.out.println(">>>> TIME (13) = " + (System.currentTimeMillis() - my_time));          

        Hashtable soln_enrollment_info = ViewLearningSoln.getEnrollmentInfo(con, itm_lst, hasOffReadPrivilege, curTime);

        for (int i=0; i<item_vector.size(); i++) {
            Long itm_id = (Long)item_vector.elementAt(i);
            aeItem item = (aeItem)item_info.get(itm_id);
//System.out.println("!@!@ " + item.itm_id);            
            if (((item_type == null) || 
                (item_type.contains(item.itm_type) || item_type.contains(item.itm_type.toLowerCase()))) &&
                (hasOffReadPrivilege || item.itm_status.equals(aeItem.ITM_STATUS_ON)) &&
                (hasPreApprReadPrivilege || item.itm_life_status == null) &&
                (v_targeted_item_apply_method_lst == null || v_targeted_item_apply_method_lst.contains(item.itm_apply_method.toLowerCase())) &&
                !item.itm_run_ind) {
                if (target_soln.containsKey(itm_id)) {
                    aeItemRequirement itmr = new aeItemRequirement();
                    boolean item_exemption = itmr.checkItemExemption(con, usr_ent_id, itm_id.longValue());
                    boolean user_exemption = itmr.checkUserExemption(con, usr_ent_id, itm_id.longValue());
                    
//                    if(!item_exemption && !user_exemption) {
//                        System.out.println("not exemption");

                        if (item.itm_apply_method != null && item.itm_apply_method.equals(aeItem.ITM_APPLY_METHOD_COMPULSORY)) {
                            Vector period_lst = getPeriodList((Vector)target_soln.get(itm_id), period_xml);
                            xml_compulsory.append(toXML(con, itm_id, item, soln_info, 0, true, period_lst, (Vector)target_soln.get(itm_id), peers, my_period_lst, soln_enrollment_info, null, null, new Boolean(item_exemption), new Boolean(user_exemption), prof.usr_ent_id));
                        } else {
                            Long l_period_id = (Long)other_soln.get(itm_id);
                            long period_id = 0;

                            if (l_period_id != null) {
                                period_id = l_period_id.longValue();
                            }
                            xml_elective.append(toXML(con, itm_id, item, soln_info, period_id, false, null, (Vector)target_soln.get(itm_id), peers, null, soln_enrollment_info, null, null, new Boolean(item_exemption), new Boolean(user_exemption), prof.usr_ent_id));
                        }
//                    }else{
//                        System.out.println("is exempted");
//                    }
                } else if(training_needs_soln.containsKey(itm_id)){
                    try {
                        ArrayList arrayList = (ArrayList) training_needs_soln.get(itm_id);
                        StringBuffer skillListBuf = new StringBuffer();
                        for(Iterator it=arrayList.iterator();it.hasNext();) {
                            Long sklSkbIdObj = (Long) it.next();
                            DbCmSkill dbCmSkill = new DbCmSkill();
                            dbCmSkill.skl_skb_id = sklSkbIdObj.longValue();
                            dbCmSkill.get(con);
                            skillListBuf.append(dbCmSkill.asXML());
                        }
                        xml_training_needs.append(toXML(con, itm_id, item, soln_info, 0, false, null, null, null, null, soln_enrollment_info, null, skillListBuf.toString(), null, null, prof.usr_ent_id));
                    } catch (cwSysMessage e) {
                        throw new cwException("cwSysMessage :" + e.getMessage());
                    }
                } else {
                    Long l_period_id = (Long)other_soln.get(itm_id);
                    long period_id = 0;

                    if (l_period_id != null) {
                        period_id = l_period_id.longValue();
                    }
                    xml_other.append(toXML(con, itm_id, item, soln_info, period_id, false, null, null, null, null, soln_enrollment_info, other_soln_created_by, other_soln_not_created_by, null, null, null, prof.usr_ent_id));
                }
            }
//System.out.println(">>>> TIME (13.1) = " + (System.currentTimeMillis() - my_time));          
        }
                
        // make xml
        StringBuffer result = new StringBuffer();
        
        result.append("<learning_soln>").append(cwUtils.NEWL);
        result.append("<cur_time>").append(curTime).append("</cur_time>").append(cwUtils.NEWL);
        result.append("<grade_ent_id_list>").append(cwUtils.NEWL);

        for (int i=0; i<user_grade_peer_ent_ids.size(); i++) {
            Long g_id = (Long)user_grade_peer_ent_ids.elementAt(i);
            
            result.append("<ent id=\"").append(g_id.longValue()).append("\" display_bil=\"").append(peers.get(g_id));
            
            if (g_id.longValue() == grade_ent_id) {
                result.append("\" current=\"true");
            }
            
            result.append("\"/>").append(cwUtils.NEWL);
        }
        
        result.append("</grade_ent_id_list>").append(cwUtils.NEWL);
        result.append(period_xml);
        
//        if (prof.usr_ent_id != usr_ent_id) {
            dbRegUser user = new dbRegUser();
            user.usr_ent_id = usr_ent_id;
            user.get(con);

            result.append("<student id=\"").append(user.usr_ste_usr_id).append("\" last_name=\"").append(cwUtils.esc4XML(aeUtils.escNull(user.usr_last_name_bil))).append("\" first_name=\"").append(cwUtils.esc4XML(aeUtils.escNull(user.usr_first_name_bil))).append("\" display_bil=\"").append(cwUtils.esc4XML(aeUtils.escNull(user.usr_display_bil))).append("\" ent_id=\"").append(usr_ent_id).append("\"/>").append(cwUtils.NEWL);
//        }
        result.append(aeItem.getAllItemTypeTitleInOrg(con, prof.root_ent_id));
//System.out.println(">>>> TIME (14) = " + (System.currentTimeMillis() - my_time));          

        if (soln_type.contains(COMPULSORY) || soln_type.contains(COMPULSORY.toLowerCase())) {
            result.append("<item_list type=\"").append(COMPULSORY).append("\">").append(cwUtils.NEWL);
            result.append(xml_compulsory.toString());
            result.append("</item_list>");
        }
        
        if (soln_type.contains(ELECTIVE) || soln_type.contains(ELECTIVE.toLowerCase())) {
            result.append("<item_list type=\"").append(ELECTIVE).append("\">").append(cwUtils.NEWL);
            result.append(xml_elective.toString());
            result.append("</item_list>");
        }

        if (soln_type.contains(TRAINING_NEEDS) || soln_type.contains(TRAINING_NEEDS.toLowerCase())) {
            result.append("<item_list type=\"").append(TRAINING_NEEDS).append("\">").append(cwUtils.NEWL);
            result.append(xml_training_needs.toString());
            result.append("</item_list>");
        }

        if (soln_type.contains(OTHER) || soln_type.contains(OTHER.toLowerCase())) {
            result.append("<item_list type=\"").append(OTHER).append("\">").append(cwUtils.NEWL);
            result.append(xml_other.toString());
            result.append("</item_list>");
        }
        
        if(order_bys!=null && sort_bys!=null) {
            result.append("<sorting order_by=\"");
            for(int i=0; i<order_bys.length; i++) {
                if(i!=0) {
                    result.append(",");
                }
                result.append(order_bys[i]);
            }
            result.append("\" sort_by=\"");
            for(int i=0; i<sort_bys.length; i++) {
                if(i!=0) {
                    result.append(",");
                }
                result.append(sort_bys[i]);
            }
            result.append("\"/>");
        }
            
        result.append("</learning_soln>").append(cwUtils.NEWL);
//System.out.println(">>>> TIME (15) = " + (System.currentTimeMillis() - my_time));          
        return result.toString();
    }

    private String toXML(Connection con, Long itm_id, aeItem item, Hashtable soln_info, long period_id, boolean is_compulsory, Vector compulsory_period_lst, Vector target_to, Hashtable peers, Vector my_period_lst, Hashtable enrol_info, Vector planned_by, Vector not_planned_by, String trainingNeedsXML, Boolean item_exemption, Boolean user_exemption, long usr_ent_id) throws SQLException {
        boolean boolBoth = false;
        for (int i=0; i<planned_by.size(); i++) {
            // self-planed
            if (planned_by.contains(itm_id)) {
                for (int j=0; j<not_planned_by.size(); j++) {
                    // not self-planed also.  That means the course is both self-planed and recommended by management
                    if (not_planned_by.contains(itm_id)) {
                        boolBoth = true;
                    }
                }
            }
        }
        if (boolBoth == true) {
            Vector vtDummy = new Vector();
            Vector vtFullItem = new Vector();
            for (int i=0; i<planned_by.size(); i++) {
                vtFullItem.addElement(planned_by.elementAt(i));
            }
            for (int i=0; i<not_planned_by.size(); i++) {
                vtFullItem.addElement(not_planned_by.elementAt(i));
            }
            
            return toXML(con, itm_id, item, soln_info, period_id, is_compulsory, compulsory_period_lst, target_to, peers, my_period_lst, enrol_info, vtFullItem, trainingNeedsXML, item_exemption, user_exemption, usr_ent_id)
                 + toXML(con, itm_id, item, soln_info, period_id, is_compulsory, compulsory_period_lst, target_to, peers, my_period_lst, enrol_info, vtDummy, trainingNeedsXML, item_exemption, user_exemption, usr_ent_id);
        }
        else {
            return toXML(con, itm_id, item, soln_info, period_id, is_compulsory, compulsory_period_lst, target_to, peers, my_period_lst, enrol_info, planned_by, trainingNeedsXML, item_exemption, user_exemption, usr_ent_id);
        }
    }

    private String toXML(Connection con, Long itm_id, aeItem item, Hashtable soln_info, long period_id, boolean is_compulsory, Vector compulsory_period_lst, Vector target_to, Hashtable peers, Vector my_period_lst, Hashtable enrol_info, Vector planned_by, String trainingNeedsXML, Boolean item_exemption, Boolean user_exemption, long usr_ent_id) throws SQLException {
        StringBuffer temp = new StringBuffer();
        Vector v_my_soln_info = (Vector)soln_info.get(itm_id);

        temp.append("<item id=\"").append(item.itm_id).append("\" code=\"").append(cwUtils.esc4XML(aeUtils.escNull(item.itm_code))).append("\" title=\"").append(cwUtils.esc4XML(aeUtils.escNull(item.itm_title))).append("\" type=\"").append(aeUtils.escNull(item.itm_type)).append("\" course_id=\"").append(item.cos_res_id).append("\" eff_start_datetime=\"").append(aeUtils.escNull(item.itm_eff_start_datetime)).append("\" item.itm_eff_end_datetime=\"").append(aeUtils.escNull(item.itm_eff_end_datetime)).append("\" nature=\"").append(aeUtils.escNull(item.itm_apply_method))
            .append("\" unit=\"").append(item.itm_unit);
        if (planned_by != null) {
            if (planned_by.contains(itm_id)) {
                temp.append("\" self_plan=\"").append("true");
            }
            else {
                temp.append("\" self_plan=\"").append("false");
            }
        }        
        temp.append("\">").append(cwUtils.NEWL);
        
        temp.append("<target_to>").append(cwUtils.NEWL);

        boolean conntainAllPeer = true;

        if (target_to != null && peers != null) {
            for (int i=0; i<target_to.size(); i++) {
                Long ent_id = (Long)target_to.elementAt(i);
println("~~ " + ent_id.longValue());
println("~~~ " + peers.get(ent_id));
                temp.append("<title>").append(peers.get(ent_id)).append("</title>").append(cwUtils.NEWL);
            }
        }

        temp.append("</target_to>").append(cwUtils.NEWL);
        
        //System.out.println("usr_id: " + usr_id);
        if(this.usr_id != 0){
            temp.append("<targeted_lrn_ind>").append(aeItem.isTargetedLearner(con, usr_id, item.itm_id, false)).append("</targeted_lrn_ind>");
            temp.append("<comp_targeted_lrn_ind>").append(false).append("</comp_targeted_lrn_ind>");
            temp.append("<item_exemption_ind>").append(cwUtils.escNull(item_exemption)).append("</item_exemption_ind>");
            temp.append("<user_exemption_ind>").append(cwUtils.escNull(user_exemption)).append("</user_exemption_ind>");
        }
        temp.append("<plan>").append(cwUtils.NEWL);

        if (is_compulsory) {
            if (target_to.size() == peers.size()) {
                for (int i=0; i<my_period_lst.size(); i++) {
                    temp.append("<period id=\"").append(((Long)my_period_lst.elementAt(i)).longValue()).append("\"/>").append(cwUtils.NEWL);   
                }
            } else {
                for (int i=0; i<compulsory_period_lst.size(); i++) {
                    temp.append("<period id=\"").append(((Long)compulsory_period_lst.elementAt(i)).longValue()).append("\"/>").append(cwUtils.NEWL);
                }
            }
        } else {
            if (period_id != 0) {
                temp.append("<period id=\"").append(period_id).append("\"/>").append(cwUtils.NEWL);
            }
        }

        temp.append("</plan>").append(cwUtils.NEWL);
        try{
	        temp.append("<applicable>").append(aeItem.isItemEnrollable(con, itm_id.longValue(), usr_ent_id)).append("</applicable>");
        }catch(cwException e){
        	throw new SQLException(e.getMessage());
        }
        temp.append("<history>").append(cwUtils.NEWL);

        if (v_my_soln_info != null) {
            for (int i=0; i<v_my_soln_info.size(); i++) {           
                Hashtable info = (Hashtable)v_my_soln_info.elementAt(i);
                        
                if (info != null) {
                    temp.append("<record itm_id=\"").append(aeUtils.escNull(info.get(ViewLearningSoln.ITEM_ID))).append("\">").append(cwUtils.NEWL);
                    temp.append("<application id=\"").append(aeUtils.escNull(info.get(ViewLearningSoln.APPN_ID))).append("\" datetime=\"").append(aeUtils.escNull(info.get(ViewLearningSoln.APPN_TIMESTAMP))).append("\" process_status=\"").append(cwUtils.esc4XML(aeUtils.escNull(info.get(ViewLearningSoln.APPN_PROCESS_STATUS)))).append("\" queue_status=\"").append(aeUtils.escNull(info.get(ViewLearningSoln.APPN_STATUS))).append("\"/>").append(cwUtils.NEWL);
                    temp.append("<attendance datetime=\"").append(aeUtils.escNull(info.get(ViewLearningSoln.ATTN_TIMESTAMP))).append("\" attended=\"").append(aeUtils.escNull(info.get(ViewLearningSoln.ATTN_ATTEND_IND))).append("\"/>").append(cwUtils.NEWL);
                    temp.append("</record>").append(cwUtils.NEWL);                    
                }
            }
        }
                
        temp.append("</history>").append(cwUtils.NEWL); 
        temp.append("<training_center id=\"").append(item.itm_tcr_id).append("\">")
        	.append(cwUtils.esc4XML(cwUtils.escNull(item.itm_tcr_title))).append("</training_center>");
        if (enrol_info == null || enrol_info.get(itm_id) == null) {
            temp.append("<enrolment_info/>").append(cwUtils.NEWL);
        }else {
            aeItem nextEnrol = (aeItem) enrol_info.get(itm_id); 
            temp.append("<enrolment_info itm_id=\"").append(nextEnrol.itm_id)
                .append("\" start_datetime=\"").append(nextEnrol.itm_appn_start_datetime)
                .append("\" end_datetime=\"").append(nextEnrol.itm_appn_end_datetime)
                .append("\"/>").append(cwUtils.NEWL);
        }
        if (trainingNeedsXML == null) {
            temp.append("<skill_list/>").append(cwUtils.NEWL);
        }else {
            temp.append("<skill_list>");
            temp.append(trainingNeedsXML);
            temp.append("</skill_list>");
        }

        temp.append("</item>").append(cwUtils.NEWL);
    
        return temp.toString();
    }
    
    public Vector getPeriodList(Vector grade_lst, String period_xml) {
        XMLparser myXMLparser = null;
        Vector period_lst = new Vector();
        
        if (period_xml != null) {
            try {
                StringReader in = new StringReader(period_xml);
                SAXParserFactory saxFactory = SAXParserFactory.newInstance();
                SAXParser saxParser  = saxFactory.newSAXParser();
                myXMLparser = new XMLparser(grade_lst);
                saxParser.parse(new InputSource(in), myXMLparser);
                in.close();
                    
                period_lst = myXMLparser.period_lst;
            } catch (ParserConfigurationException e) {
                e.getMessage();
            } catch (SAXException e) {
                e.getMessage();
            } catch (IOException e) {
                e.getMessage();
            }
        }
        
        return period_lst;
    }
    
    public static int insSoln(Connection con, loginProfile prof, long usr_ent_id, long itm_id, long period_id) throws SQLException, qdbException, cwSysMessage, cwException {
        long grade_ent_id = DbUserGrade.getGradeEntId(con, usr_ent_id);
        dbRegUser user = new dbRegUser();
        user.usr_ent_id = usr_ent_id;
        String usr_id = user.getUserId(con);
        long parent_itm_id = ViewLearningSoln.getParentItemId(con, itm_id);
        boolean targetByPeer = acSite.isTargetByPeer(con, prof.root_ent_id);
        if (parent_itm_id != 0) {
            itm_id = parent_itm_id;
        }       
       
        long base_itm_id = aeItem.getBaseItemId(con, itm_id);        
        long soln_itm_id;
        if( targetByPeer )
            soln_itm_id = ViewLearningSoln.getLearningSoln(con, usr_ent_id, usr_id, grade_ent_id, base_itm_id, prof.usr_ent_id);
        else
            soln_itm_id = ViewLearningSoln.getLearningSoln(con, usr_ent_id, usr_id, 0, base_itm_id, prof.usr_ent_id);
        if (soln_itm_id == 0) {
            Hashtable peers = new Hashtable();
            Vector order_grade_lst = new Vector();
            //DbUserGrade.getPeers(con, grade_ent_id, order_grade_lst, peers);
            if( targetByPeer )
                DbUserGrade.getPeers(con, grade_ent_id, order_grade_lst, peers);
            else {
                Long gradeEntId = new Long(grade_ent_id);
                order_grade_lst.addElement(gradeEntId);
                Hashtable ugrTable = DbUserGrade.getDisplayName(con, "(" + grade_ent_id + ")");            
                peers.put(gradeEntId, (String)ugrTable.get(gradeEntId.toString()));
            }
            StringBuffer ent_lst = new StringBuffer();
            if (base_itm_id == 0) {
                throw new cwException("com.cw.wizbank.ae.aeLearningSoln.insSoln: base_itm_id can't be 0");
            }
          
            if (order_grade_lst != null && order_grade_lst.size() != 0) {
                for (int i=0; i<order_grade_lst.size(); i++) {
                    if (i != 0) {
                        ent_lst.append(",");
                    }
                    ent_lst.append(" ").append(order_grade_lst.elementAt(i)).append(" ");
                }
            } else {
                ent_lst.append(" " + grade_ent_id + " ");
            }
            DbLearningSoln soln = new DbLearningSoln();
            soln.lsn_ent_id = usr_ent_id;
            soln.lsn_itm_id = base_itm_id;
            soln.lsn_period_id = period_id;
            soln.lsn_ent_id_lst = ent_lst.toString();
            soln.lsn_create_usr_id = prof.usr_id;
            soln.lsn_upd_usr_id= prof.usr_id;
            try
            {
            	soln.ins(con);
            }catch(SQLException ex)
            {
            	throw ex;
            }
            //return 
            } else {
            Vector itm_lst = new Vector();
            Vector period_lst = new Vector();            
            itm_lst.addElement(new Long(itm_id));
            period_lst.addElement(new Long(period_id));
            updSoln(con, prof, usr_ent_id, itm_lst, period_lst);
        }

        return 1;
    }

    public static boolean existSoln(Connection con, long usr_ent_id, long itm_id, long root_ent_id) throws SQLException {
        long grade_ent_id = DbUserGrade.getGradeEntId(con, usr_ent_id);
        dbRegUser user = new dbRegUser();
        user.usr_ent_id = usr_ent_id;
        long parent_itm_id = ViewLearningSoln.getParentItemId(con, itm_id);
        if (parent_itm_id != 0) {
            itm_id = parent_itm_id;
        }
        long base_itm_id = aeItem.getBaseItemId(con, itm_id);
        boolean targetByPeer = acSite.isTargetByPeer(con, root_ent_id);
        long soln_itm_id;
        if( targetByPeer )
            soln_itm_id = ViewLearningSoln.getLearningSoln(con, usr_ent_id, grade_ent_id, base_itm_id);
        else
            soln_itm_id = ViewLearningSoln.getLearningSoln(con, usr_ent_id, 0, base_itm_id);

//        long soln_itm_id = ViewLearningSoln.getLearningSoln(con, usr_ent_id, grade_ent_id, base_itm_id);

        if (soln_itm_id == 0) {
            return false;
        } else {
            return true;
        }
    }    
    public static void updSingleSolnDate(Connection con, long usr_ent_id, long itm_id, String lsn_start_datetime, String lsn_end_datetime) throws SQLException {
		Timestamp start_datetime = null;
		Timestamp end_datetime = null;
		if (lsn_start_datetime != null) {
			start_datetime = Timestamp.valueOf(lsn_start_datetime);
		}
		if (lsn_end_datetime != null) {
			end_datetime = Timestamp.valueOf(lsn_end_datetime);
		}
		updSingleSolnDate(con, usr_ent_id, itm_id, start_datetime, end_datetime);
	}
    public static void updSingleSolnDate(Connection con,long usr_ent_id,long itm_id,Timestamp lsn_start_datetime,Timestamp lsn_end_datetime) throws SQLException
    {
    	String  strSQL="update aeLearningSoln set lsn_start_datetime=? , lsn_end_datetime=? " +
    			"where lsn_ent_id=? and lsn_itm_id= ? ";
    	
    	// String strSQL="DELETE From aeLearningSoln WHERE lsn_ent_id = ? AND lsn_itm_id = ?" ;
    	 PreparedStatement stmt=con.prepareStatement(strSQL);
    	 stmt.setTimestamp(1, lsn_start_datetime);
         stmt.setTimestamp(2, lsn_end_datetime);
    	 stmt.setLong(3, usr_ent_id);
         stmt.setLong(4, itm_id);
         
         stmt.executeUpdate();
         stmt.close();
         
    }
    
    public static void updSoln(Connection con, loginProfile prof, long usr_ent_id, Vector itm_lst, Vector period_lst) throws SQLException, cwException, qdbException, cwSysMessage {
println(1);
        if (itm_lst != null && period_lst != null) {
println(2);
            if (itm_lst.size() != period_lst.size()) {
                throw new cwException("com.cw.wizbank.ae.aeLearningSoln.updSoln: # of items and # of periods are not match");    
            }

            long grade_ent_id = DbUserGrade.getGradeEntId(con, usr_ent_id);
            dbRegUser user = new dbRegUser();
            user.usr_ent_id = usr_ent_id;
            String usr_id = user.getUserId(con);
            boolean targetByPeer = acSite.isTargetByPeer(con, prof.root_ent_id);
       
println(3);
            for (int i=0; i<itm_lst.size(); i++) {
                long itm_id = ((Long)itm_lst.elementAt(i)).longValue();
                //long soln_itm_id = ViewLearningSoln.getLearningSoln(con, usr_ent_id, grade_ent_id, itm_id);
                long soln_itm_id;
                if( targetByPeer )
                    soln_itm_id = ViewLearningSoln.getLearningSoln(con, usr_ent_id, grade_ent_id, itm_id);
                else
                    soln_itm_id = ViewLearningSoln.getLearningSoln(con, usr_ent_id, 0, itm_id);

println(4);                
                if (soln_itm_id == 0) {
                    aeLearningSoln.insSoln(con, prof, usr_ent_id, itm_id, ((Long)period_lst.elementAt(i)).longValue());
//                    throw new cwException("com.cw.wizbank.ae.aeLearningSoln.updSoln: soln_itm_id can't be 0 for itm_id " + itm_id);
                } else {
                    DbLearningSoln soln = new DbLearningSoln();
                    soln.lsn_ent_id = usr_ent_id;
                    soln.lsn_itm_id = soln_itm_id;
                    soln.lsn_period_id = ((Long)period_lst.elementAt(i)).longValue();
                    soln.lsn_upd_usr_id= prof.usr_id;
println(5);
                    if (soln.lsn_period_id == -1) {
                        soln.del(con, usr_id, grade_ent_id, prof.root_ent_id, prof.usr_ent_id);
println("deleted!!");
                    } else {
                        soln.upd(con, grade_ent_id, prof.root_ent_id);
println("updated!!");
                    }
                }
            }
        }
    }
    public static void searchItem(Connection con, Hashtable h_v_entity, Hashtable target_soln, Long grade_id, String[] array_itm_apply_method) throws SQLException {
        Vector itm_lst = new Vector();
        
//        for (int i=0; i<combination.size(); i++) {
//            target_group[i] = ((Long)combination.elementAt(i)).longValue();   
//        }
        
//        ViewItemTargetGroup target = new ViewItemTargetGroup();
//        target.targetEntIds = target_group;
//        target.targetType = ViewItemTargetGroup.TARGETED_LEARNER;
//        target.searchItem(con, itm_lst);
//System.out.println("View Item Target Group =============");
        ViewItemTargetGroup.searchItem(con, h_v_entity, array_itm_apply_method, DbItemTargetRuleDetail.IRD_TYPE_TARGET_LEARNER, itm_lst);

//println("<><> combination=" + combination);
//System.out.println("<><> itm_lst=" + itm_lst);
        if (itm_lst != null) {
            for (int i=0; i<itm_lst.size(); i++) {
                Long item_id = (Long)itm_lst.elementAt(i);
                Vector grade_lst = null;
                        
                if (target_soln.containsKey(item_id)) {
                    grade_lst = (Vector)target_soln.get(item_id);
                            
                    if (! grade_lst.contains(grade_id)) {
//System.out.println("<><><> 1 grade_id=" + grade_id);
                        grade_lst.addElement(grade_id);
                    }
                } else {
//System.out.println("<><><> 2 grade_id=" + grade_id);
                    grade_lst = new Vector();
                    grade_lst.addElement(grade_id);
                }

                target_soln.put(item_id, grade_lst);            
            }
        }        
    }    
    
    public static void delByItem(Connection con, long itm_id) throws SQLException {
        DbLearningSoln.delByItem(con, itm_id);
    }
    public static void delSoln(Connection con,long itm_id,long usr_id) throws SQLException
    {
    	 String strSQL="DELETE From aeLearningSoln WHERE lsn_ent_id = ? AND lsn_itm_id = ?" ;
    	 PreparedStatement stmt=con.prepareStatement(strSQL);
    	 stmt.setLong(1, usr_id);
         stmt.setLong(2, itm_id);
         stmt.executeUpdate();
         stmt.close();
    	 
    }

    private static void println(String str) {
        if (debug) {
        	CommonLog.debug(str);
           // System.out.println(str);
        }
    }

    private static void println(long l) {
        if (debug) {
        	CommonLog.debug(String.valueOf(l));
           // System.out.println(l);
        }
    }

    private static void print(String str) {
        if (debug) {
        	CommonLog.debug(str);
            //System.out.print(str);
        }
    }

    private static void print(long l) {
        if (debug) {
        	CommonLog.debug(String.valueOf(l));
            //System.out.print(l);
        }
    }



    
   /**
    Get the user-item status(app_status, app_process_status, attendance, learning plan) 
    of input user and items 
    */
    public Hashtable getUserItemStatus(Connection con, long usr_ent_id, long owner_ent_id, Vector v_itm_id) 
        throws SQLException, cwException {
        
        Hashtable h_itemStatus = new Hashtable();
        if(v_itm_id == null) {
            v_itm_id = new Vector();
        }
        String sql_itm_id = aeUtils.prepareSQLList(v_itm_id);
        StringBuffer SQLBuf = new StringBuffer(1024);
        String null_sql_string = cwSQL.get_null_sql(cwSQL.COL_TYPE_STRING);
        String null_sql_long = cwSQL.get_null_sql(cwSQL.COL_TYPE_LONG);
        SQLBuf.append(" Select itm_id, itm_title, a.app_status, a.app_process_status ")
              .append(" ,att_ats_id, ats_type ")
              .append( OuterJoinSqlStatements.getAppnLeftJoinAtt() )
              .append(" itm_id = a.app_itm_id ")
              .append(" and a.app_ent_id = ? ")
              .append(" and a.app_itm_id in ").append(sql_itm_id)
              .append(" and a.app_create_timestamp = ")
              .append(" (Select max(b.app_create_timestamp) ")
              .append(" from aeApplication b ")
              .append(" where b.app_itm_id = a.app_itm_id and b.app_ent_id = a.app_ent_id) ")
              .append(" union ")
              .append(" Select itm_id, itm_title ")
              .append(" ,").append(null_sql_string).append(" as app_status")
              .append(" ,").append(null_sql_string).append(" as app_process_status")
              .append(" ,").append(null_sql_long).append(" as att_ats_id ")
              .append(" ,").append(null_sql_string).append(" as ats_status ")
              .append(" from aeItem where itm_id in ").append(sql_itm_id)
              .append(" and not exists (Select app_id From aeApplication where app_itm_id = itm_id and app_ent_id = ? ) ")
              .append(" order by itm_title ");
        PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
        stmt.setLong(1, usr_ent_id);
        stmt.setLong(2, usr_ent_id);
        ResultSet rs = stmt.executeQuery();
        Vector lrn_soln_itm_id = null;
        while(rs.next()) {
            Long itm_id = new Long(rs.getLong("itm_id"));
            String itm_title = aeUtils.escNull(rs.getString("itm_title"));
            String app_status = aeUtils.escNull(rs.getString("app_status"));
            String app_process_status = aeUtils.escNull(rs.getString("app_process_status"));
            String att_ats_id = aeUtils.escNull(rs.getString("att_ats_id"));
            String ats_type = aeUtils.escNull(rs.getString("ats_type"));
            String overallStatus = null;
            if(ats_type.length() != 0) {
                overallStatus = ats_type;
            } else if(app_process_status.length() != 0) {
                overallStatus = app_process_status;
            } else {
                if(lrn_soln_itm_id == null) {
                    lrn_soln_itm_id = getLearningSoln(con, usr_ent_id, owner_ent_id);
                }
                if(lrn_soln_itm_id.contains(itm_id)) {
                    overallStatus = "Planned";
                } else {
                    overallStatus = "Not Planned";
                }
            }
            String[] itemStatus = new String[6];
            itemStatus[0] = overallStatus;
            itemStatus[1] = itm_title;
            itemStatus[2] = app_status;
            itemStatus[3] = app_process_status;
            itemStatus[4] = att_ats_id;
            itemStatus[5] = ats_type;
            h_itemStatus.put(itm_id, itemStatus);
        }
        stmt.close();
        return h_itemStatus;
    }
    
    private Vector getLearningSoln(Connection con, long usr_ent_id, long owner_ent_id) 
        throws SQLException, cwException {
            
        Vector v_itm_id = new Vector();
        long grade_ent_id = 0;
        Vector user_group_ent_ids = new Vector();
        Vector user_industry_ent_ids = new Vector();
        Vector user_grade_peer_ent_ids = new Vector();
        Hashtable peers = new Hashtable();
        Hashtable target_soln = null;
        Vector item_type = null;
        
        //Get Applicable Item Types
        if (item_type == null || item_type.size() == 0) {
            item_type = DbItemType.getApplicableItemType(con, owner_ent_id);
        }

        //Get Parent User Grade (Peer)
        grade_ent_id = DbUserGrade.getGradeEntId(con, usr_ent_id);
        DbUserGrade.getPeers(con, grade_ent_id, user_grade_peer_ent_ids, peers);
        //Get User Parent Group
        user_group_ent_ids = dbUserGroup.getUserParentEntIds(con, usr_ent_id);
        //Get User Parent Industry
        user_industry_ent_ids = DbIndustryCode.getIndCodeEntIds(con, usr_ent_id);

        target_soln = new Hashtable();
        Hashtable h_v_entity = new Hashtable();
        //Prepare h_v_entity for ViewItemTargetEntity to search targeted items
        if (user_group_ent_ids != null && user_group_ent_ids.size() > 0) {
            h_v_entity.put(dbEntity.ENT_TYPE_USER_GROUP, user_group_ent_ids);
        }
        if (user_industry_ent_ids != null && user_industry_ent_ids.size() > 0) {
            h_v_entity.put(dbEntity.ENT_TYPE_INDUSTRY_CODE, user_industry_ent_ids);
        }
        if (user_grade_peer_ent_ids != null && user_grade_peer_ent_ids.size() > 0) {
            h_v_entity.put(dbEntity.ENT_TYPE_USER_GRADE, user_grade_peer_ent_ids);
        }
        ViewItemTargetGroup.searchItem(con, h_v_entity, null, DbItemTargetRuleDetail.IRD_TYPE_TARGET_LEARNER, v_itm_id);

        //get items not in targeted items, but planned by user
        Hashtable other_soln = ViewLearningSoln.getMyLeanringSoln(con, usr_ent_id, grade_ent_id);
        Enumeration a = other_soln.keys();
        while (a.hasMoreElements()) {
            Long key = (Long)a.nextElement();
            v_itm_id.addElement(key);
        }
        //Remove duplicate item id in v_itm_id
        cwUtils.removeDuplicate(v_itm_id);
        return v_itm_id;
    }

/*
    private Vector getLearningSoln(Connection con, long usr_ent_id, long owner_ent_id) 
        throws SQLException, cwException {
        
        Vector v_itm_id = new Vector();
        long grade_ent_id = DbUserGrade.getGradeEntId(con, usr_ent_id);
        Vector user_group_ent_ids = dbUserGroup.getUserParentEntIds(con, usr_ent_id);
        Vector user_industry_ent_ids = DbIndustryCode.getIndCodeEntIds(con, usr_ent_id);
        Hashtable target_soln = new Hashtable();
        String period_xml = ViewLearningSoln.getPeriodXML(con, owner_ent_id);
        Vector combination = new Vector();
        Hashtable peers = new Hashtable();
        Vector user_grade_peer_ent_ids = new Vector();
        DbUserGrade.getPeers(con, grade_ent_id, user_grade_peer_ent_ids, peers);

        for (int i=0; i<user_group_ent_ids.size(); i++) {
            combination.addElement(user_group_ent_ids.elementAt(i));

            for (int j=0; j<user_grade_peer_ent_ids.size(); j++) {
                Long grade_id = (Long)user_grade_peer_ent_ids.elementAt(j);
                combination.addElement(user_grade_peer_ent_ids.elementAt(j));

                if (user_industry_ent_ids.size() != 0) {
                    for (int k=0; k<user_industry_ent_ids.size(); k++) {
                        combination.addElement(user_industry_ent_ids.elementAt(k));                            
                        aeLearningSoln.searchItem(con, combination, target_soln, grade_id);
                        combination.removeElement(combination.lastElement());
                    }
                } else {
                    aeLearningSoln.searchItem(con, combination, target_soln, grade_id);
                }

                combination.removeElement(combination.lastElement());            
            }

            combination.removeElement(combination.lastElement());                        
        }

        Enumeration enumeration = target_soln.keys();
        while(enumeration.hasMoreElements()) {
            Long id = (Long)enumeration.nextElement();
            v_itm_id.addElement(id);
        }
        Hashtable other_soln = ViewLearningSoln.getMyLeanringSoln(con, usr_ent_id, grade_ent_id);
        Enumeration a = other_soln.keys();
        while (a.hasMoreElements()) {
            Long key = (Long)a.nextElement();
            v_itm_id.addElement(key);
        }
        return v_itm_id;
    }
*/ 

    private Vector sorting(Vector itemVector, String[] order_by, String[] sort_by) {
        for(int i=0; i<itemVector.size(); i++) {
            for(int j=i; j<itemVector.size(); j++) {
                HashMap itemMap1 = (HashMap) itemVector.elementAt(i);
                HashMap itemMap2 = (HashMap) itemVector.elementAt(j);
                String[] compareItem1 = new String[order_by.length];
                String[] compareItem2 = new String[order_by.length];

                for(int k=0; k<order_by.length; k++) {
                    compareItem1[k] = (String) itemMap1.get(order_by[k].toUpperCase());
                    compareItem2[k] = (String) itemMap2.get(order_by[k].toUpperCase());
                }
                
                if(sort_by[0].equalsIgnoreCase(DESC)) {
                    if(compareItem1[0].compareTo(compareItem2[0])<0) {
                        itemVector.setElementAt(itemMap1, j);
                        itemVector.setElementAt(itemMap2, i);
                    } else if(compareItem1[0].compareTo(compareItem2[0]) == 0) {
                        // second level sorting
                        if(order_by.length>1 && sort_by.length>1) {
                            if(sort_by[1].equalsIgnoreCase(DESC)) {
                                if(compareItem1[1].compareTo(compareItem2[1])<0) {
                                    itemVector.setElementAt(itemMap1, j);
                                    itemVector.setElementAt(itemMap2, i);
                                }
                             } else if(sort_by[1].equalsIgnoreCase(ASC)) {
                                if(compareItem1[1].compareTo(compareItem2[1])>0) {
                                    itemVector.setElementAt(itemMap1, j);
                                    itemVector.setElementAt(itemMap2, i);
                                }
                             }
                        }
                    }
                } else if (sort_by[0].equalsIgnoreCase(ASC)) {
                    if(compareItem1[0].compareTo(compareItem2[0])>0) {
                        itemVector.setElementAt(itemMap1, j);
                        itemVector.setElementAt(itemMap2, i);
                    } else if(compareItem1[0].compareTo(compareItem2[0]) == 0) {
                        // second level sorting
                        if(order_by.length>1 && sort_by.length>1) {
                            if(sort_by[1].equalsIgnoreCase(DESC)) {
                                if(compareItem1[1].compareTo(compareItem2[1])<0) {
                                    itemVector.setElementAt(itemMap1, j);
                                    itemVector.setElementAt(itemMap2, i);
                                }
                            } else if(sort_by[1].equalsIgnoreCase(ASC)) {
                                if(compareItem1[1].compareTo(compareItem2[1])>0) {
                                    itemVector.setElementAt(itemMap1, j);
                                    itemVector.setElementAt(itemMap2, i);
                                }
                            }
                        }
                    }
                } // end if-else

            } // end for-j
        } // end for-i
        
        return itemVector;
    }
    
    public static String getLearningPlanConfigXML(WizbiniLoader wizbini, String root_id) throws cwException{
        StringWriter writer = new StringWriter(); 
        wizbini.marshal((LearningPlan)wizbini.cfgOrgLearningPlan.get(root_id), writer);
        String config_xml = writer.toString();
        config_xml = config_xml.substring(config_xml.indexOf("<", 2));
        return config_xml; 
    }
}