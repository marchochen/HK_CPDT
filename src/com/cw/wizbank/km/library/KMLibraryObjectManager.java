package com.cw.wizbank.km.library;

import java.sql.*;
import java.util.*;

import javax.servlet.http.*;

import com.cw.wizbank.db.DbKmLibraryObject;
import com.cw.wizbank.db.DbKmLibraryObjectBorrow;
import com.cw.wizbank.db.DbKmLibraryObjectCopy;
import com.cw.wizbank.db.DbKmBaseObject;
import com.cw.wizbank.db.DbTable;
import com.cw.wizbank.db.DbKmNode;
import com.cw.wizbank.db.DbKmFolder;
import com.cw.wizbank.db.DbKmObject;
import com.cw.wizbank.db.DbKmObjectType;
import com.cw.wizbank.db.DbKmNodeAccess;
import com.cw.wizbank.db.view.ViewKmFolderManager;
import com.cw.wizbank.db.view.ViewKmLibraryObject;
import com.cw.wizbank.db.view.ViewKmObject;
import com.cw.wizbank.qdb.*;
import com.cw.wizbank.util.*;
import com.cw.wizbank.km.KMObjectManager;
import com.cw.wizbank.km.KMModule;
import com.cw.wizbank.search.Search;
import com.cwn.wizbank.utils.CommonLog;


public class KMLibraryObjectManager extends KMObjectManager {
    public static final String SMSG_REC_NOT_FOUND = "GEN005";
    public static final String SMSG_INVALID_CALL_NUM = "KML001";
    public static final String SMSG_INVALID_COPY_NUM = "KML002";
    public static final String SMSG_ITEM_NOT_CHECKOUT = "KML003";
    public static final String SMSG_ITEM_CHECKIN = "KML004";
    public static final String SMSG_ITEM_CHECKOUT = "KML005";
    public static final String SMSG_USER_VIOLATE_POLICY = "KML006";
    public static final String SMSG_NO_COPY_IN_STOCK = "KML007";
    public static final String SMSG_NO_CHECKIN_PERMISSION = "KML008";
    public static final String SMSG_NO_CHECKOUT_PERMISSION = "KML009";
    public static final String SMSG_COPY_EXISTED = "KML010";
    public static final String SMSG_COPY_ON_LOAN = "KML011";
    public static final String SMSG_ITEM_HAS_OUTSTNADING_REQUEST = "KML012";
    public static final String SMSG_ITEM_HAS_OUTSTNADING_COPY = "KML013";    
    public static final String SMSG_CODE_EXISTED = "KML014";
    public static final String SMSG_VIOLATE_OVERDUE_POLICY = "KML015";
    public static final String SMSG_VIOLATE_RENEW_POLICY = "KML016";
    public static final String SMSG_ITEM_IS_RESERVED = "KML017";
    public static final String SMSG_ITEM_RENEW = "KML018";
    public static final String SMSG_SAME_ITEM_IS_BORROWED = "KML019";
    public static final String SMSG_SAME_ITEM_IS_RESERVED = "KML020";
    public static final String SMSG_SAME_ITEM_IS_CHECKOUTED = "KML021";
    public static final String SMSG_BORROW_IS_ALLOWED = "KML022";
    public static final String SMSG_VIOLATE_BORROW_POLICY = "KML023";
    public static final String SMSG_NO_AVAILABLE_COPY = "KML024";
    public static final String SMSG_RESERVE_IS_ALLOWED = "KML025";
    public static final String SMSG_NO_RENEW_PERMISSION = "KML026";
    public static final String SMSG_NO_RESERVE_PERMISSION = "KML027";
    public static final String SMSG_NO_BORROW_PERMISSION = "KML028";
    public static final String SMSG_NO_USER_HISTORY_PERMISSION = "KML029";
    public static final String SMSG_NO_COPY_HISTORY_PERMISSION = "KML030";
    public static final String SMSG_NO_CANCEL_PERMISSION = "KML031";

    private static final String SESS_CHECKOUT_LIST     = "SESS_CHECKOUT_LIST";
    private static final String SESS_CHECKOUT_SORT_BY  = "SESS_CHECKOUT_SORT_BY";
    private static final String SESS_CHECKOUT_ORDER_BY = "SESS_CHECKOUT_ORDER_BY";
    private static final String SESS_CHECKOUT_LIST_TIMESTAMP = "SESS_CHECKOUT_LIST_TIMESTAMP";
    
    private static final String COL_CALL_NUMBER = "call_number";
    private static final String COL_ITEM = "item";
    private static final String COL_COPY_TO_PICK = "copy_to_pick";
    
    public static final String KM_NATURE_LIBRARY = "LIBRARY";

    private static final String SESS_KML_OVERDUE_CWPAGE = "SESS_KML_OVERDUE_CWPAGE";
    private static final String SESS_KML_OVERDUE_VECTOR_DATA = "SESS_KML_OVERDUE_VECTOR_DATA";

    public KMLibraryObjectManager(Connection con, long owner_ent_id) throws SQLException {
        super(con, owner_ent_id);
    }

// checkin
    public String getCheckInListXML(Connection con, String callNum, String copyNum) throws cwException {
        StringBuffer xmlBuffer = new StringBuffer();

        try {
            String obj_title = ViewKmLibraryObject.getObjectTitleByCode(con, callNum);
            xmlBuffer.append("<check_in_node>");
            xmlBuffer.append("<object_title>").append(cwUtils.esc4XML(obj_title)).append("</object_title>").append(cwUtils.NEWL);
            xmlBuffer.append("<call_number>" + cwUtils.esc4XML(callNum) + "</call_number>").append(cwUtils.NEWL);
            xmlBuffer.append("<copy_number>" + cwUtils.esc4XML(copyNum) + "</copy_number>").append(cwUtils.NEWL);
            xmlBuffer.append(ViewKmLibraryObject.getCheckInUserXML(con, callNum, copyNum));
            xmlBuffer.append("</check_in_node>");
        } catch (SQLException e) {
            throw new cwException("SQLException: " + e.getMessage());
        } catch (qdbException e) {
            throw new cwException("qdbException: " + e.getMessage());
        }
        return xmlBuffer.toString();
    }
 
    public void validateCheckInItem(Connection con, String callNum, String copyNum) throws cwSysMessage, SQLException {
        String smsg = "";

        if(!ViewKmLibraryObject.isValidLibraryObject(con, callNum, copyNum)) {  // check for valid pair
            if(!ViewKmLibraryObject.isValidCallNumber(con, callNum)) {          // check for call num
                smsg = SMSG_INVALID_CALL_NUM;
            } else {
                smsg = SMSG_INVALID_COPY_NUM;
            }
        } else if(!ViewKmLibraryObject.isCheckOut(con, callNum, copyNum)){      // check for check-ed out
            smsg = SMSG_ITEM_NOT_CHECKOUT;
        } 
                
        if(smsg.length()>0) {
            throw new cwSysMessage(smsg);
        }
    }

    public String checkin(Connection con, loginProfile prof, qdbEnv env, String userId, String callNum, String copyNum) throws cwException, cwSysMessage {
        try {
            ViewKmLibraryObject viewKmLibObj = new ViewKmLibraryObject();
            viewKmLibObj.getCheckinItem(con, callNum, copyNum);
            Timestamp curTime = cwSQL.getTime(con);
            viewKmLibObj.dbKmLibObjBorrow.lob_update_usr_id = userId;
            viewKmLibObj.dbKmLibObjBorrow.lob_update_timestamp = curTime;
            viewKmLibObj.dbKmLibObjBorrow.updToNotLatest(con);
            
            viewKmLibObj.dbKmLibObjBorrow.lob_status = DbKmLibraryObjectBorrow.STATUS_CHECKIN;
            viewKmLibObj.dbKmLibObjBorrow.lob_latest_ind = DbKmLibraryObjectBorrow.LATEST_IND_FALSE;
            viewKmLibObj.dbKmLibObjBorrow.lob_create_timestamp = curTime;
            viewKmLibObj.dbKmLibObjBorrow.lob_create_usr_id = userId;
            viewKmLibObj.dbKmLibObjBorrow.lob_update_timestamp = curTime;
            viewKmLibObj.dbKmLibObjBorrow.lob_update_usr_id = userId;
            viewKmLibObj.dbKmLibObjBorrow.ins(con);

            viewKmLibObj.dbKmLibObj.lio_num_copy_available++;
            viewKmLibObj.dbKmLibObj.lio_num_copy_in_stock++;
            viewKmLibObj.dbKmLibObj.upd(con);

            StringBuffer xmlBuffer = new StringBuffer();
            
            ArrayList availablecopies = viewKmLibObj.getAvailableCopy(con, viewKmLibObj.dbKmLibObj.lio_bob_nod_id);
            ArrayList requests = viewKmLibObj.getRequestInfo(con, viewKmLibObj.dbKmLibObj.lio_bob_nod_id, env.KM_LIB_BORROW_LIMIT, env.KM_LIB_OVERDUE_LIMIT);

            if(availablecopies.size() > 0 && requests.size() > 0) {
                xmlBuffer.append("<request_node id=\"" + viewKmLibObj.dbKmLibObj.lio_bob_nod_id + "\">");
                String xml = getObjXML(con, viewKmLibObj.dbKmLibObj.lio_bob_nod_id, prof.root_ent_id);  
                xmlBuffer.append(xml);
                xmlBuffer.append("<available_copies>");
                xmlBuffer.append("<number>" + availablecopies.size() + "</number>");
                for(Iterator it=availablecopies.iterator(); it.hasNext(); ) {
                    DbKmLibraryObjectCopy dbKmLibObjCopy = (DbKmLibraryObjectCopy) it.next();
                    xmlBuffer.append("<available_copy id=\"" + dbKmLibObjCopy.loc_id + "\">");
                    xmlBuffer.append(cwUtils.esc4XML(dbKmLibObjCopy.loc_copy));
                    xmlBuffer.append("</available_copy>");
                }
                xmlBuffer.append("</available_copies>");

                xmlBuffer.append("<waiting_request_list>");
                for(Iterator it=requests.iterator(); it.hasNext(); ) {
                    DbKmLibraryObjectBorrow dbKmLibObjBorrow = (DbKmLibraryObjectBorrow) it.next();
                    xmlBuffer.append("<waiting_request>");
                    dbRegUser dbuser = new dbRegUser();
                    dbuser.usr_ent_id = dbKmLibObjBorrow.lob_usr_ent_id;
                    dbuser.get(con);
                    xmlBuffer.append(dbuser.getUserXML(con, null));
                    xmlBuffer.append("<request>" + dbKmLibObjBorrow.lob_status + "</request>");
                    xmlBuffer.append("<request_date>" + dbKmLibObjBorrow.lob_update_timestamp + "</request_date>");
                    xmlBuffer.append("</waiting_request>");
                }
                xmlBuffer.append("</waiting_request_list>");
                xmlBuffer.append("</request_node>");
            }
            
            return xmlBuffer.toString();
        } catch (SQLException e) {
            throw new cwException ("SQLException: " + e.getMessage());
        } catch (qdbException e) {
            throw new cwException (e.getMessage());
        }
    }

// checkout
    public String getCheckOutListXML(Connection con, loginProfile prof, qdbEnv env, HttpSession sess, cwPagination cwPage) throws cwException, cwSysMessage {
        StringBuffer xmlBuffer = new StringBuffer();
        try {
            ViewKmLibraryObject viewKmLibObj = new ViewKmLibraryObject();
            StringBuffer orderSQL = new StringBuffer();
            boolean fromSession = false;
            boolean scopeResult = false;

	        Vector checkoutList = new Vector();
	        Vector checkoutListAll = new Vector();
            Vector checkoutListByPage = new Vector();
            Timestamp sess_timestamp = (Timestamp)sess.getAttribute(SESS_CHECKOUT_LIST_TIMESTAMP);
            String sess_sort_by = (String) sess.getAttribute(SESS_CHECKOUT_SORT_BY);
            String sess_order_by = (String) sess.getAttribute(SESS_CHECKOUT_ORDER_BY);

            if (cwPage.ts == null){
                cwPage.ts = cwSQL.getTime(con);
            }
            if (cwPage.pageSize == 0){
                cwPage.pageSize = cwPagination.defaultPageSize;
            }
            if (cwPage.curPage == 0) {
                cwPage.curPage = 1;
            }

            int start;
            int end;

            if (sess_timestamp != null && sess_timestamp.equals(cwPage.ts)) {
                checkoutListAll = (Vector)sess.getAttribute(SESS_CHECKOUT_LIST);
                if (sess_sort_by!= null && sess_sort_by.equals(cwPage.sortCol)) {
                    if (sess_order_by!= null && sess_order_by.equals(cwPage.sortOrder)) {
                        start = ((cwPage.curPage-1) * cwPage.pageSize) + 1;
                        end = cwPage.curPage * cwPage.pageSize;
                    } else {
                        start = checkoutListAll.size() - cwPage.pageSize;
                        if (start<0) {
                            start = 0;
                        }
                        end = checkoutListAll.size();
                    }
                    for (int i=start ; i<= checkoutListAll.size() && (i <= end);i++) {
                        checkoutListByPage.add(checkoutListAll.elementAt(i-1));
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
            checkoutList = viewKmLibObj.getCheckoutList(con, orderSQL.toString(), checkoutListAll, checkoutListByPage, scopeResult, env.KM_LIB_BORROW_LIMIT, env.KM_LIB_OVERDUE_LIMIT);
            xmlBuffer.append("<check_out_list>");

            if (checkoutList != null) {
                int count;
                if (checkoutListByPage != null && checkoutListByPage.size() != 0) {
                    count = 0;
                } else {
                    count = (cwPage.curPage-1)*cwPage.pageSize;
                }
                
                Vector sortedList = new Vector();
                if(cwPage.sortCol.equalsIgnoreCase(COL_COPY_TO_PICK)) {
                    long target = -1;
                    int targetIndex = -1;
                    ViewKmLibraryObject.itemDetails sortItem1 = null;
                    ViewKmLibraryObject.itemDetails sortItem2 = null;
                    for (int i=count; i<checkoutList.size() && i<count+cwPage.pageSize; i++) {
                        sortItem1 = (ViewKmLibraryObject.itemDetails) checkoutList.elementAt(i);
                        target = sortItem1.copyPick;
                        targetIndex = i;
                        for (int j=i; j<checkoutList.size() && j<count+cwPage.pageSize; j++) {
                            sortItem2 = (ViewKmLibraryObject.itemDetails) checkoutList.elementAt(j);
                            if(cwPage.sortOrder.equalsIgnoreCase("DESC")) {
                                if(sortItem2.copyPick > target) {
                                    target = sortItem2.copyPick;
                                    targetIndex = j;
                                }
                            } else {
                                if(sortItem2.copyPick < target) {
                                    target = sortItem2.copyPick;
                                    targetIndex = j;
                                }
                            }
                        }
                        sortItem1 = (ViewKmLibraryObject.itemDetails) checkoutList.elementAt(i);
                        sortItem2 = (ViewKmLibraryObject.itemDetails) checkoutList.elementAt(targetIndex);
                        checkoutList.setElementAt(sortItem1, targetIndex);
                        checkoutList.setElementAt(sortItem2, i);
                    }
                }
                

                for (int i=count; i<checkoutList.size() && i<count+cwPage.pageSize; i++) {
                    ViewKmLibraryObject.itemDetails itm = (ViewKmLibraryObject.itemDetails) checkoutList.elementAt(i);
                    xmlBuffer.append("<check_out_node id=\"" + itm.bobNodId + "\">");
                    xmlBuffer.append("<title>" + cwUtils.esc4XML(itm.title) + "</title>").append(cwUtils.NEWL);
                    xmlBuffer.append("<type>" + cwUtils.esc4XML(itm.type) + "</type>").append(cwUtils.NEWL);
                    xmlBuffer.append("<call_number>" + cwUtils.esc4XML(itm.callNumber) + "</call_number>").append(cwUtils.NEWL);
                    xmlBuffer.append("<copies_to_pick>" + itm.copyPick + "</copies_to_pick>").append(cwUtils.NEWL);
                    xmlBuffer.append("</check_out_node>");
                }
            }
	        if (!fromSession){
                sess.setAttribute(SESS_CHECKOUT_LIST_TIMESTAMP, cwPage.ts);
                sess.setAttribute(SESS_CHECKOUT_LIST, checkoutListAll);
            }
            if (cwPage.sortCol != null && cwPage.sortOrder != null){
                sess.setAttribute(SESS_CHECKOUT_SORT_BY, cwPage.sortCol);
                sess.setAttribute(SESS_CHECKOUT_ORDER_BY, cwPage.sortOrder);
            }
            if (checkoutListAll != null){
                cwPage.totalRec = checkoutListAll.size();
                if (cwPage.pageSize != 0){
                    cwPage.totalPage = checkoutListAll.size() / cwPage.pageSize;
                    if (checkoutListAll.size() % cwPage.pageSize != 0){
                        cwPage.totalPage++;
                    }
                }
            }
            xmlBuffer.append(cwPage.asXML());
            xmlBuffer.append("</check_out_list>");
        } catch (SQLException e) {
            throw new cwException("SQLException: " + e.getMessage());
        }
        return xmlBuffer.toString();
    }
 
    public String getCheckOutDetailsXML(Connection con, loginProfile prof, qdbEnv env, long callId) throws cwException, cwSysMessage {
        StringBuffer xmlBuffer = new StringBuffer();
        try {
            Vector allCheckoutList = new Vector();
            Vector callIdVec = new Vector();
            Vector checkoutList = new Vector();
            callIdVec.addElement(new Long(callId));

            ViewKmLibraryObject viewKmLibObj = new ViewKmLibraryObject();
            checkoutList = viewKmLibObj.getCheckoutList(con, null, allCheckoutList, callIdVec, true, env.KM_LIB_BORROW_LIMIT, env.KM_LIB_OVERDUE_LIMIT);
            if(checkoutList.size() > 0) {
                ViewKmLibraryObject.itemDetails itm = (ViewKmLibraryObject.itemDetails) checkoutList.elementAt(0);
                xmlBuffer.append("<check_out_node id=\"" + itm.bobNodId + "\">");
                String xml = getObjXML(con, itm.bobNodId, prof.root_ent_id);  
                xmlBuffer.append(xml);
                xmlBuffer.append("<title>" + cwUtils.esc4XML(itm.title) + "</title>").append(cwUtils.NEWL);
                xmlBuffer.append("<call_number>" + cwUtils.esc4XML(itm.callNumber) + "</call_number>").append(cwUtils.NEWL);
                xmlBuffer.append("<copies_to_pick>" + itm.copyPick + "</copies_to_pick>").append(cwUtils.NEWL);

                xmlBuffer.append("<available_copies>");
                ArrayList availablecopies = viewKmLibObj.getAvailableCopy(con, callId);
                xmlBuffer.append("<number>" + availablecopies.size() + "</number>");
                for(Iterator it=availablecopies.iterator(); it.hasNext(); ) {
                    DbKmLibraryObjectCopy dbKmLibObjCopy = (DbKmLibraryObjectCopy) it.next();
                    xmlBuffer.append("<available_copy id=\"" + dbKmLibObjCopy.loc_id + "\">");
                    xmlBuffer.append(cwUtils.esc4XML(dbKmLibObjCopy.loc_copy));
                    xmlBuffer.append("</available_copy>");
                }
                xmlBuffer.append("</available_copies>");

                xmlBuffer.append("<waiting_request_list>");
                ArrayList requests = viewKmLibObj.getRequestInfo(con, callId, env.KM_LIB_BORROW_LIMIT, env.KM_LIB_OVERDUE_LIMIT);
                for(Iterator it=requests.iterator(); it.hasNext(); ) {
                    DbKmLibraryObjectBorrow dbKmLibObjBorrow = (DbKmLibraryObjectBorrow) it.next();
                    xmlBuffer.append("<waiting_request>");
                    dbRegUser dbuser = new dbRegUser();
                    dbuser.usr_ent_id = dbKmLibObjBorrow.lob_usr_ent_id;
                    dbuser.get(con);
                    xmlBuffer.append(dbuser.getUserXML(con, null));
                    xmlBuffer.append("<request>" + dbKmLibObjBorrow.lob_status + "</request>");
                    xmlBuffer.append("<request_date>" + dbKmLibObjBorrow.lob_update_timestamp + "</request_date>");
                    xmlBuffer.append("</waiting_request>");
                }
                xmlBuffer.append("</waiting_request_list>");

                xmlBuffer.append("</check_out_node>");
            }
            
        } catch (SQLException e) {
            throw new cwException("SQLException: " + e.getMessage());
        } catch (qdbException e) {
            throw new cwException(e.getMessage());
        }
        return xmlBuffer.toString();
    }

    public void validateCheckoutUser(Connection con, qdbEnv env, long usrEntId) throws cwException, cwSysMessage, SQLException {
        String smsg = "";

        if(!ViewKmLibraryObject.isValidCheckoutUser(con, usrEntId, env.KM_LIB_BORROW_LIMIT, env.KM_LIB_OVERDUE_LIMIT)) {  
            smsg = SMSG_USER_VIOLATE_POLICY;
        } 
                
        if(smsg.length()>0) {
            throw new cwSysMessage(smsg);
        }
    }

    public long checkout(Connection con, qdbEnv env, String userId, long usrEntId, long callId, long copyId) throws cwException, cwSysMessage {
        String originalStatus = "";
        try {
            Timestamp curTime = cwSQL.getTime(con);
            Timestamp dueDate = new Timestamp(curTime.getTime() + env.KM_LIB_DUE_DAY * 24 * 60 * 60 * 1000);
            dueDate = new Timestamp(dueDate.getYear(), dueDate.getMonth(), dueDate.getDate(), 23,59,59,0);
            ViewKmLibraryObject viewKmLibObj = new ViewKmLibraryObject();
            viewKmLibObj.getCheckoutItem(con, usrEntId, callId);
            
            originalStatus = viewKmLibObj.dbKmLibObjBorrow.lob_status;
            viewKmLibObj.dbKmLibObjBorrow.lob_update_usr_id = userId;
            viewKmLibObj.dbKmLibObjBorrow.lob_update_timestamp = curTime;
            viewKmLibObj.dbKmLibObjBorrow.updToNotLatest(con);
            
            viewKmLibObj.dbKmLibObjBorrow.lob_status = DbKmLibraryObjectBorrow.STATUS_CHECKOUT;
            viewKmLibObj.dbKmLibObjBorrow.lob_loc_id = copyId;
            viewKmLibObj.dbKmLibObjBorrow.lob_due_timestamp = dueDate;
            viewKmLibObj.dbKmLibObjBorrow.lob_create_timestamp = curTime;
            viewKmLibObj.dbKmLibObjBorrow.lob_create_usr_id = userId;
            viewKmLibObj.dbKmLibObjBorrow.lob_update_timestamp = curTime;
            viewKmLibObj.dbKmLibObjBorrow.lob_update_usr_id = userId;
            viewKmLibObj.dbKmLibObjBorrow.lob_renew_no = 0;
            viewKmLibObj.dbKmLibObjBorrow.lob_latest_ind = DbKmLibraryObjectBorrow.LATEST_IND_TRUE;
            viewKmLibObj.dbKmLibObjBorrow.ins(con);

            if(viewKmLibObj.dbKmLibObj.lio_num_copy_in_stock == 0) {
                throw new cwSysMessage (SMSG_NO_COPY_IN_STOCK);
            }

            if(originalStatus.equalsIgnoreCase(DbKmLibraryObjectBorrow.STATUS_RESERVE)) {
                viewKmLibObj.dbKmLibObj.lio_num_copy_available--;
                if(viewKmLibObj.dbKmLibObj.lio_num_copy_available < 0) {
                    viewKmLibObj.dbKmLibObj.lio_num_copy_available = 0;
                }
            }
            viewKmLibObj.dbKmLibObj.lio_num_copy_in_stock--;
            viewKmLibObj.dbKmLibObj.upd(con);
            return viewKmLibObj.dbKmLibObjBorrow.lob_id;
            //return viewKmLibObj.getCheckoutItemXML(con, callId, copyId, env.KM_LIB_BORROW_LIMIT, env.KM_LIB_OVERDUE_LIMIT);
        } catch (SQLException e) {
            throw new cwException ("SQLException: " + e.getMessage());
        }/* catch (qdbException e) {
            throw new cwException (e.getMessage());
        }*/
    }


    public void checkoutNotify(Connection con, loginProfile prof, String mesgSubject, String sender_usr_id, long recEntId, long[] ccEntIds, long[] bccEntIds, long lobId) throws cwException, cwSysMessage, SQLException {
        KMLibraryMessage kmLibMesg = new KMLibraryMessage();
        Timestamp curTime = cwSQL.getTime(con);
                
        Hashtable params = new Hashtable();
        params.put("template_type", "CHECKOUT_NOTIFICATION");
        params.put("template_subtype", "HTML");
        if( mesgSubject != null && mesgSubject.length() > 0 )
            params.put("subject", mesgSubject);
        params.put("lobId", new Long(lobId));
        Vector vec = new Vector();
        vec.addElement(ViewKmLibraryObject.getObjectTitleByLobId(con, lobId));
        params.put("subject_token", vec);
        long[] entIds = new long[1];
        entIds[0] = recEntId;
        kmLibMesg.insNotify(con, prof, sender_usr_id, entIds, ccEntIds, bccEntIds, curTime, params);
    }


    public static String getCheckoutNotifyXML(Connection con, long site_id, String senderUsrId, long usrEntId, long lobId, String mailAccount, long DES_KEY) throws cwException, cwSysMessage, SQLException {
        try { 
            StringBuffer xmlBuffer = new StringBuffer();
            
            ResultSet itemRS = ViewKmLibraryObject.getBorrowItemInfo(con, lobId);
            
            if(itemRS.next()) {
			    KMLibraryMessage kmLibMesg = new KMLibraryMessage();
			    xmlBuffer.append(kmLibMesg.getRecipientXml(con, usrEntId, mailAccount, DES_KEY));
			    xmlBuffer.append(kmLibMesg.getSenderXml(con, senderUsrId, mailAccount));
                xmlBuffer.append("<checkout_node id=\"" + itemRS.getLong("bob_nod_id") + "\">");
                KMLibraryObjectManager kmLibObjMgr = new KMLibraryObjectManager(con, site_id);
                String xml = kmLibObjMgr.getObjXML(con, itemRS.getLong("bob_nod_id"), site_id);  
                xmlBuffer.append(xml);
                xmlBuffer.append("<title>" + cwUtils.esc4XML(itemRS.getString("obj_title")) + "</title>");
                xmlBuffer.append("<call_number>" + cwUtils.esc4XML(itemRS.getString("bob_code")) + "</call_number>");
                dbRegUser regUser = new dbRegUser();
                regUser.usr_ent_id = itemRS.getLong("lob_usr_ent_id");
                regUser.get(con);
                xmlBuffer.append(regUser.getUserShortXML(con, false, true, false, false));
        	    xmlBuffer.append("</checkout_node>");
            } 
            return xmlBuffer.toString();
        } catch (qdbException e) {
            throw new cwException(e.getMessage());
        }
    }


// history 
    public String getUserHistoryXML(Connection con, loginProfile prof, long usrEntId) throws cwException, SQLException, cwSysMessage {
        try { 
            StringBuffer xmlBuffer = new StringBuffer();
            xmlBuffer.append("<history>");
            dbRegUser regUser = new dbRegUser();
            regUser.usr_ent_id = usrEntId;
            regUser.get(con);
            StringBuffer userXML = regUser.getUserShortXML(con, false, true, false, false);
            xmlBuffer.append("<criteria>" + userXML + "</criteria>");
            ResultSet rs = ViewKmLibraryObject.getUserHistory(con, usrEntId);
            xmlBuffer.append(historyAsXML(con, prof, rs, "USER"));
            xmlBuffer.append("</history>");
            return xmlBuffer.toString();
        } catch (qdbException e) {
            throw new cwException(e.getMessage());
        }
    }

    public String getCopyHistoryXML(Connection con, loginProfile prof, long copyId) throws cwException, SQLException, cwSysMessage {
        StringBuffer xmlBuffer = new StringBuffer();
        xmlBuffer.append("<history>");
        String copyXML = ViewKmLibraryObject.getCopyInfo(con, copyId);
        xmlBuffer.append("<criteria>" + copyXML + "</criteria>");
        ResultSet rs = ViewKmLibraryObject.getCopyHistory(con, copyId);
        xmlBuffer.append(historyAsXML(con, prof, rs, "COPY"));
        xmlBuffer.append("</history>");
        return xmlBuffer.toString();
    }

// renew
    public String getRenewXML(Connection con, loginProfile prof, qdbEnv env, long callId, long copyId) throws cwException, cwSysMessage, SQLException {
        String smsg = "";
        try { 
            StringBuffer xmlBuffer = new StringBuffer();
            ResultSet rs = ViewKmLibraryObject.getItemInfo(con, callId, copyId);
            
            Vector overdueUser = ViewKmLibraryObject.getOverdueUser(con, env.KM_LIB_OVERDUE_LIMIT);

            DbKmLibraryObjectBorrow borrow = new DbKmLibraryObjectBorrow();
            borrow.lob_lio_bob_nod_id = callId;
            boolean isReserve = borrow.isItemReserve(con);
            
            if(rs.next()) {
                Timestamp curTime = cwSQL.getTime(con);
                xmlBuffer.append("<renew_node id=\"" + rs.getLong("bob_nod_id") + "\">");
                String xml = getObjXML(con, rs.getLong("bob_nod_id"), prof.root_ent_id);  
                xmlBuffer.append(xml);
                xmlBuffer.append("<cur_time>" + curTime + "</cur_time>");
                xmlBuffer.append("<title>" + cwUtils.esc4XML(rs.getString("obj_title")) + "</title>");
                xmlBuffer.append("<call_number>" + cwUtils.esc4XML(rs.getString("bob_code")) + "</call_number>");
                xmlBuffer.append("<copy id=\"" + rs.getLong("loc_id") + "\">" + cwUtils.esc4XML(rs.getString("loc_copy")) + "</copy>");
                dbRegUser regUser = new dbRegUser();
                regUser.usr_ent_id = rs.getLong("lob_usr_ent_id");
                regUser.get(con);
                xmlBuffer.append(regUser.getUserShortXML(con, false, true, false, false));
                
                if(overdueUser.contains(new Long(rs.getLong("lob_usr_ent_id")))) {
                    throw new cwSysMessage(SMSG_VIOLATE_OVERDUE_POLICY);
                } else if(rs.getTimestamp("lob_due_timestamp").before(curTime)) {
                    throw new cwSysMessage(SMSG_VIOLATE_OVERDUE_POLICY);
                } else if(rs.getLong("lob_renew_no") == env.KM_LIB_RENEW_LIMIT) {
                    xmlBuffer.append("<violate_renew_policy>true</violate_renew_policy>");
                    xmlBuffer.append("<allow_reserve>true</allow_reserve>");
                } else if(isReserve) {
                    xmlBuffer.append("<is_reserved>true</is_reserved>");
                    xmlBuffer.append("<allow_reserve>true</allow_reserve>");
                } else {
                    Timestamp dueDate = new Timestamp(curTime.getTime() + env.KM_LIB_DUE_DAY * 24 * 60 * 60 * 1000);
                    dueDate = new Timestamp(dueDate.getYear(), dueDate.getMonth(), dueDate.getDate(), 23,59,59,0);
                    xmlBuffer.append("<due_timestamp>" + dueDate + "</due_timestamp>");
                    xmlBuffer.append("<renew_no>" + (rs.getLong("lob_renew_no")+1) + "</renew_no>");
                }
        	    xmlBuffer.append("</renew_node>");
            } else {
                throw new cwSysMessage(SMSG_REC_NOT_FOUND, "Item call_id = " + callId + "; copy_id = " + copyId);
            }
            return xmlBuffer.toString();
        } catch (qdbException e) {
            throw new cwException(e.getMessage());
        }
    }

    public void renew(Connection con, qdbEnv env, String userId, long callId, long copyId) throws cwException, cwSysMessage, SQLException {
        String smsg = "";
        StringBuffer xmlBuffer = new StringBuffer();
        ResultSet rs = ViewKmLibraryObject.getItemInfo(con, callId, copyId);
            
        Vector overdueUser = ViewKmLibraryObject.getOverdueUser(con, env.KM_LIB_OVERDUE_LIMIT);

        DbKmLibraryObjectBorrow borrow = new DbKmLibraryObjectBorrow();
        borrow.lob_lio_bob_nod_id = callId;
        boolean isReserve = borrow.isItemReserve(con);
            
        if(rs.next()) {
            Timestamp curTime = cwSQL.getTime(con);
            if(overdueUser.contains(new Long(rs.getLong("lob_usr_ent_id")))) {
                throw new cwSysMessage(SMSG_VIOLATE_OVERDUE_POLICY);
            } else if(rs.getTimestamp("lob_due_timestamp").before(curTime)) {
                throw new cwSysMessage(SMSG_VIOLATE_OVERDUE_POLICY);
            } else if(rs.getLong("lob_renew_no") == env.KM_LIB_RENEW_LIMIT) {
                throw new cwSysMessage(SMSG_VIOLATE_RENEW_POLICY);
            } else if(isReserve) {
                throw new cwSysMessage(SMSG_ITEM_IS_RESERVED);
            } else {
                Timestamp dueDate = new Timestamp(curTime.getTime() + env.KM_LIB_DUE_DAY * 24 * 60 * 60 * 1000);
                dueDate = new Timestamp(dueDate.getYear(), dueDate.getMonth(), dueDate.getDate(), 23,59,59,0);

                DbKmLibraryObjectBorrow renewBorrow = new DbKmLibraryObjectBorrow();
                renewBorrow.lob_lio_bob_nod_id = callId;
                renewBorrow.lob_loc_id = copyId;
                renewBorrow.getByCallCopyId(con);

                renewBorrow.lob_update_usr_id = userId;
                renewBorrow.lob_update_timestamp = curTime;
                renewBorrow.updToNotLatest(con);
                
                renewBorrow.lob_renew_no++;
                renewBorrow.lob_due_timestamp = dueDate;
                renewBorrow.lob_create_usr_id = userId;
                renewBorrow.lob_create_timestamp = curTime;
                renewBorrow.lob_update_usr_id = userId;
                renewBorrow.lob_update_timestamp = curTime;
                renewBorrow.lob_latest_ind = DbKmLibraryObjectBorrow.LATEST_IND_TRUE;
                renewBorrow.ins(con);
            }
        } else {
            throw new cwSysMessage(SMSG_REC_NOT_FOUND, "Item call_id = " + callId + "; copy_id = " + copyId);
        }
        return;
    }

// reserve
    public String getReserveXML(Connection con, loginProfile prof, qdbEnv env, long usrEntId, long callId) throws cwException, cwSysMessage, SQLException {
        try { 
            StringBuffer xmlBuffer = new StringBuffer();
            boolean isOwnCheckout = false;
            boolean isOwnOverdue = false;
            boolean isOwnBorrow = false;
            boolean isOwnReserve = false;
            
            ResultSet itemRS = ViewKmLibraryObject.getItemReserveInfo(con, callId);
            ResultSet borrowRS = ViewKmLibraryObject.getBorrowInfo(con, usrEntId, callId);
            Vector overdueUser = ViewKmLibraryObject.getOverdueUser(con, env.KM_LIB_OVERDUE_LIMIT);
            Vector overBorrowUser = ViewKmLibraryObject.getOverBorrowUser(con, env.KM_LIB_BORROW_LIMIT);
            DbKmLibraryObjectBorrow borrow = new DbKmLibraryObjectBorrow();
            borrow.lob_lio_bob_nod_id = callId;
            boolean isReserve = borrow.isItemReserve(con);
            while(borrowRS.next()) {
                String borrowStatus = borrowRS.getString("lob_status");
                if(borrowStatus.equalsIgnoreCase(DbKmLibraryObjectBorrow.STATUS_CHECKOUT)) {
                    isOwnCheckout = true;
                    Timestamp curTime = cwSQL.getTime(con);
                    if(borrowRS.getTimestamp("lob_due_timestamp").before(curTime)) {
                        isOwnOverdue = true;
                    }
                } else if(borrowStatus.equalsIgnoreCase(DbKmLibraryObjectBorrow.STATUS_BORROW)) {
                    isOwnBorrow = true;
                } else if(borrowStatus.equalsIgnoreCase(DbKmLibraryObjectBorrow.STATUS_RESERVE)) {
                    isOwnReserve = true;
                }
            }
            
            if(itemRS.next()) {
                xmlBuffer.append("<reserve_node id=\"" + itemRS.getLong("bob_nod_id") + "\">");
                String xml = getObjXML(con, itemRS.getLong("bob_nod_id"), prof.root_ent_id);  
                xmlBuffer.append(xml);
                xmlBuffer.append("<title>" + cwUtils.esc4XML(itemRS.getString("obj_title")) + "</title>");
                xmlBuffer.append("<call_number>" + cwUtils.esc4XML(itemRS.getString("bob_code")) + "</call_number>");
                dbRegUser regUser = new dbRegUser();
                regUser.usr_ent_id = usrEntId;
                regUser.get(con);
                xmlBuffer.append(regUser.getUserShortXML(con, false, true, false, false));
                
                if(isOwnBorrow) {
                    throw new cwSysMessage(SMSG_SAME_ITEM_IS_BORROWED);
                } else if(isOwnReserve) {
                    throw new cwSysMessage(SMSG_SAME_ITEM_IS_RESERVED);
                } else if(overdueUser.contains(new Long(usrEntId))) {
                    throw new cwSysMessage(SMSG_VIOLATE_OVERDUE_POLICY);
                } else if (isOwnOverdue) {
                    throw new cwSysMessage(SMSG_VIOLATE_OVERDUE_POLICY);
                } else if((itemRS.getLong("lio_num_copy_available")>0) && !isReserve && !overBorrowUser.contains(new Long(usrEntId)) && !isOwnCheckout) {
                    xmlBuffer.append("<allow_borrow>true</allow_borrow>");
                } else {
            		xmlBuffer.append("<waiting_position>" + (itemRS.getLong("num_reserve")+1) + "</waiting_position>");
                }
        	    xmlBuffer.append("</reserve_node>");
            } else {
                throw new cwSysMessage(SMSG_REC_NOT_FOUND, "Item call_id = " + callId);
            }
            return xmlBuffer.toString();
        } catch (qdbException e) {
            throw new cwException(e.getMessage());
        }
    }

    public long reserve(Connection con, loginProfile prof, qdbEnv env, String userId, long usrEntId, long callId) throws cwException, cwSysMessage, SQLException {
        boolean isOwnCheckout = false;
        boolean isOwnOverdue = false;
        boolean isOwnBorrow = false;
        boolean isOwnReserve = false;
            
        ResultSet itemRS = ViewKmLibraryObject.getItemReserveInfo(con, callId);
        ResultSet borrowRS = ViewKmLibraryObject.getBorrowInfo(con, usrEntId, callId);
        Vector overdueUser = ViewKmLibraryObject.getOverdueUser(con, env.KM_LIB_OVERDUE_LIMIT);
        Vector overBorrowUser = ViewKmLibraryObject.getOverBorrowUser(con, env.KM_LIB_BORROW_LIMIT);
        DbKmLibraryObjectBorrow borrow = new DbKmLibraryObjectBorrow();
        borrow.lob_lio_bob_nod_id = callId;
        boolean isReserve = borrow.isItemReserve(con);
        while(borrowRS.next()) {
            String borrowStatus = borrowRS.getString("lob_status");
            if(borrowStatus.equalsIgnoreCase(DbKmLibraryObjectBorrow.STATUS_CHECKOUT)) {
                isOwnCheckout = true;
                Timestamp curTime = cwSQL.getTime(con);
                if(borrowRS.getTimestamp("lob_due_timestamp").before(curTime)) {
                    isOwnOverdue = true;
                }
            } else if(borrowStatus.equalsIgnoreCase(DbKmLibraryObjectBorrow.STATUS_BORROW)) {
                isOwnBorrow = true;
            } else if(borrowStatus.equalsIgnoreCase(DbKmLibraryObjectBorrow.STATUS_RESERVE)) {
                isOwnReserve = true;
            }
        }
            
        if(itemRS.next()) {
            if(isOwnBorrow) {
                throw new cwSysMessage(SMSG_SAME_ITEM_IS_BORROWED);
            } else if(isOwnReserve) {
                throw new cwSysMessage(SMSG_SAME_ITEM_IS_RESERVED);
            } else if(overdueUser.contains(new Long(usrEntId))) {
                throw new cwSysMessage(SMSG_VIOLATE_OVERDUE_POLICY);
            } else if (isOwnOverdue) {
                throw new cwSysMessage(SMSG_VIOLATE_OVERDUE_POLICY);
            } else if((itemRS.getLong("lio_num_copy_available")>0) && !isReserve && !overBorrowUser.contains(new Long(usrEntId)) && !isOwnCheckout) {
                throw new cwSysMessage(SMSG_BORROW_IS_ALLOWED);
            } else {
                Timestamp curTime = cwSQL.getTime(con);
                DbKmLibraryObjectBorrow reserveBorrow = new DbKmLibraryObjectBorrow();
                reserveBorrow.lob_lio_bob_nod_id = callId;
                reserveBorrow.lob_usr_ent_id = usrEntId;
                reserveBorrow.lob_status = DbKmLibraryObjectBorrow.STATUS_RESERVE;
                reserveBorrow.lob_create_usr_id = userId;
                reserveBorrow.lob_create_timestamp = curTime;
                reserveBorrow.lob_update_usr_id = userId;
                reserveBorrow.lob_update_timestamp = curTime;
                reserveBorrow.lob_latest_ind = DbKmLibraryObjectBorrow.LATEST_IND_TRUE;
                long lobId = reserveBorrow.ins(con);
                return lobId;
            }
        } else {
            throw new cwSysMessage(SMSG_REC_NOT_FOUND, "Item call_id = " + callId);
        }
    }

    public void reserveCancel(Connection con, loginProfile prof, qdbEnv env, String userId, long usrEntId, long callId) throws cwException, cwSysMessage, SQLException {
        String[] status = {DbKmLibraryObjectBorrow.STATUS_RESERVE};
        
        ResultSet reserveRS = DbKmLibraryObjectBorrow.getByUserCallIdStatus(con, usrEntId, callId, status);
            
        if(reserveRS.next()) {
            Timestamp curTime = cwSQL.getTime(con);

            DbKmLibraryObjectBorrow reserve = new DbKmLibraryObjectBorrow();
            reserve.lob_lio_bob_nod_id = reserveRS.getLong("lob_lio_bob_nod_id");
            reserve.lob_id = reserveRS.getLong("lob_id");
            reserve.get(con);

            reserve.lob_update_usr_id = userId;
            reserve.lob_update_timestamp = curTime;
            reserve.updToNotLatest(con);
                
            DbKmLibraryObjectBorrow reserveCancel = new DbKmLibraryObjectBorrow();
            reserveCancel.lob_lio_bob_nod_id = reserve.lob_lio_bob_nod_id;
            reserveCancel.lob_usr_ent_id = reserve.lob_usr_ent_id;
            reserveCancel.lob_create_usr_id = userId;
            reserveCancel.lob_create_timestamp = curTime;
            reserveCancel.lob_update_usr_id = userId;
            reserveCancel.lob_update_timestamp = curTime;
            reserveCancel.lob_status = DbKmLibraryObjectBorrow.STATUS_CANCEL;
            reserveCancel.lob_latest_ind = DbKmLibraryObjectBorrow.LATEST_IND_FALSE;
            reserveCancel.ins(con);
        } else {
            throw new cwSysMessage(SMSG_REC_NOT_FOUND, "Item call_id = " + callId);
        }
    }

    public void reserveNotify(Connection con, loginProfile prof, String mesgSubject, long recEntId, long[] ccEntIds, long[] bccEntIds, long lobId) throws cwException, cwSysMessage, SQLException {
        KMLibraryMessage kmLibMesg = new KMLibraryMessage();
        Timestamp curTime = cwSQL.getTime(con);
                
        Hashtable params = new Hashtable();
        params.put("template_type", "RESERVE_NOTIFICATION");
        params.put("template_subtype", "HTML");
        if( mesgSubject != null && mesgSubject.length() > 0 )
            params.put("subject", mesgSubject);
        Vector vec = new Vector();
        params.put("subject_token", vec);

        params.put("lobId", new Long(lobId));
        long[] entIds = new long[1];
        entIds[0] = recEntId;
        kmLibMesg.insNotify(con, prof, prof.usr_id, entIds, ccEntIds, bccEntIds, curTime, params);
    }

    public static String getReserveNotifyXML(Connection con, long site_id, String senderUsrId, long usrEntId, long lobId, String mailAccount, long DES_KEY) throws cwException, cwSysMessage, SQLException {
        try { 
            StringBuffer xmlBuffer = new StringBuffer();
            
            ResultSet itemRS = ViewKmLibraryObject.getBorrowItemInfo(con, lobId);
            
            if(itemRS.next()) {
			    KMLibraryMessage kmLibMesg = new KMLibraryMessage();
			    xmlBuffer.append(kmLibMesg.getRecipientXml(con, usrEntId, mailAccount, DES_KEY));			
			    xmlBuffer.append(kmLibMesg.getSenderXml(con, senderUsrId, mailAccount));
                xmlBuffer.append("<reserve_node id=\"" + itemRS.getLong("bob_nod_id") + "\">");
                KMLibraryObjectManager kmLibObjMgr = new KMLibraryObjectManager(con, site_id);
                String xml = kmLibObjMgr.getObjXML(con, itemRS.getLong("bob_nod_id"), site_id);
                xmlBuffer.append(xml);
                xmlBuffer.append("<title>" + cwUtils.esc4XML(itemRS.getString("obj_title")) + "</title>");
                xmlBuffer.append("<call_number>" + cwUtils.esc4XML(itemRS.getString("bob_code")) + "</call_number>");
                dbRegUser regUser = new dbRegUser();
                regUser.usr_ent_id = itemRS.getLong("lob_usr_ent_id");
                regUser.get(con);
                xmlBuffer.append(regUser.getUserShortXML(con, false, true, false, false));
        	    xmlBuffer.append("</reserve_node>");
            } 
            return xmlBuffer.toString();
        } catch (qdbException e) {
            throw new cwException(e.getMessage());
        }
    }

// borrow
    public String getBorrowXML(Connection con, loginProfile prof, qdbEnv env, long usrEntId, long callId) throws cwException, cwSysMessage, SQLException {
        try { 
            StringBuffer xmlBuffer = new StringBuffer();
            boolean isOwnCheckout = false;
            boolean isOwnBorrow = false;
            boolean isOwnReserve = false;
            
            ResultSet itemRS = ViewKmLibraryObject.getItemReserveInfo(con, callId);
            ResultSet borrowRS = ViewKmLibraryObject.getBorrowInfo(con, usrEntId, callId);
            Vector overdueUser = ViewKmLibraryObject.getOverdueUser(con, env.KM_LIB_OVERDUE_LIMIT);
            Vector overBorrowUser = ViewKmLibraryObject.getOverBorrowUser(con, env.KM_LIB_BORROW_LIMIT);
            DbKmLibraryObjectBorrow borrow = new DbKmLibraryObjectBorrow();
            borrow.lob_lio_bob_nod_id = callId;
            boolean isReserve = borrow.isItemReserve(con);
            while(borrowRS.next()) {
                String borrowStatus = borrowRS.getString("lob_status");
                if(borrowStatus.equalsIgnoreCase(DbKmLibraryObjectBorrow.STATUS_CHECKOUT)) {
                    isOwnCheckout = true;
                } else if(borrowStatus.equalsIgnoreCase(DbKmLibraryObjectBorrow.STATUS_BORROW)) {
                    isOwnBorrow = true;
                } else if(borrowStatus.equalsIgnoreCase(DbKmLibraryObjectBorrow.STATUS_RESERVE)) {
                    isOwnReserve = true;
                }
            }
            
            if(itemRS.next()) {
                xmlBuffer.append("<borrow_node id=\"" + itemRS.getLong("bob_nod_id") + "\">");
                String xml = getObjXML(con, itemRS.getLong("bob_nod_id"), prof.root_ent_id);  
                xmlBuffer.append(xml);
                xmlBuffer.append("<title>" + cwUtils.esc4XML(itemRS.getString("obj_title")) + "</title>");
                xmlBuffer.append("<call_number>" + cwUtils.esc4XML(itemRS.getString("bob_code")) + "</call_number>");
                dbRegUser regUser = new dbRegUser();
                regUser.usr_ent_id = usrEntId;
                regUser.get(con);
                xmlBuffer.append(regUser.getUserShortXML(con, false, true, false, false));
                
                if(isOwnBorrow) {
                    throw new cwSysMessage(SMSG_SAME_ITEM_IS_BORROWED);
                } else if(isOwnReserve) {
                    throw new cwSysMessage(SMSG_SAME_ITEM_IS_RESERVED);
                } else if(isOwnCheckout) {
                    throw new cwSysMessage(SMSG_SAME_ITEM_IS_CHECKOUTED);
                } else if(overdueUser.contains(new Long(usrEntId))) {
                    throw new cwSysMessage(SMSG_VIOLATE_OVERDUE_POLICY);
                } else if(overBorrowUser.contains(new Long(usrEntId))) {
                    xmlBuffer.append("<violate_borrow_policy>true</violate_borrow_policy>");
                    xmlBuffer.append("<allow_reserve>true</allow_reserve>");
                } else if((itemRS.getLong("lio_num_copy_available")==0)) {
                    xmlBuffer.append("<no_copy_available>true</no_copy_available>");
                    xmlBuffer.append("<allow_reserve>true</allow_reserve>");
                } else if(isReserve) {
                    xmlBuffer.append("<is_reserved>true</is_reserved>");
                    xmlBuffer.append("<allow_reserve>true</allow_reserve>");
                } 
        	    xmlBuffer.append("</borrow_node>");
            } else {
                throw new cwSysMessage(SMSG_REC_NOT_FOUND, "Item call_id = " + callId);
            }
            return xmlBuffer.toString();
        } catch (qdbException e) {
            throw new cwException(e.getMessage());
        }
    }

    public long borrow(Connection con, qdbEnv env, String userId, long usrEntId, long callId) throws cwException, cwSysMessage, SQLException {
        boolean isOwnCheckout = false;
        boolean isOwnBorrow = false;
        boolean isOwnReserve = false;
            
        ResultSet itemRS = ViewKmLibraryObject.getItemReserveInfo(con, callId);
        ResultSet borrowRS = ViewKmLibraryObject.getBorrowInfo(con, usrEntId, callId);
        Vector overdueUser = ViewKmLibraryObject.getOverdueUser(con, env.KM_LIB_OVERDUE_LIMIT);
        Vector overBorrowUser = ViewKmLibraryObject.getOverBorrowUser(con, env.KM_LIB_BORROW_LIMIT);
        DbKmLibraryObjectBorrow borrow = new DbKmLibraryObjectBorrow();
        borrow.lob_lio_bob_nod_id = callId;
        boolean isReserve = borrow.isItemReserve(con);
        while(borrowRS.next()) {
            String borrowStatus = borrowRS.getString("lob_status");
            if(borrowStatus.equalsIgnoreCase(DbKmLibraryObjectBorrow.STATUS_CHECKOUT)) {
                isOwnCheckout = true;
            } else if(borrowStatus.equalsIgnoreCase(DbKmLibraryObjectBorrow.STATUS_BORROW)) {
                isOwnBorrow = true;
            } else if(borrowStatus.equalsIgnoreCase(DbKmLibraryObjectBorrow.STATUS_RESERVE)) {
                isOwnReserve = true;
            }
        }
            
        if(itemRS.next()) {
            if(isOwnBorrow) {
                throw new cwSysMessage(SMSG_SAME_ITEM_IS_BORROWED);
            } else if(isOwnReserve) {
                throw new cwSysMessage(SMSG_SAME_ITEM_IS_RESERVED);
            } else if(isOwnCheckout) {
                throw new cwSysMessage(SMSG_SAME_ITEM_IS_CHECKOUTED);
            } else if(overdueUser.contains(new Long(usrEntId))) {
                throw new cwSysMessage(SMSG_VIOLATE_OVERDUE_POLICY);
            } else if(overBorrowUser.contains(new Long(usrEntId))) {
                throw new cwSysMessage(SMSG_VIOLATE_BORROW_POLICY);
            } else if((itemRS.getLong("lio_num_copy_available")==0)) {
                throw new cwSysMessage(SMSG_NO_AVAILABLE_COPY);
            } else if(isReserve) {
                throw new cwSysMessage(SMSG_RESERVE_IS_ALLOWED);
            } else {
                Timestamp curTime = cwSQL.getTime(con);
                DbKmLibraryObject libObj = new DbKmLibraryObject();
                libObj.lio_bob_nod_id = callId;
                libObj.get(con);
                libObj.lio_num_copy_available--;
                libObj.upd(con);
                
                borrow = new DbKmLibraryObjectBorrow();
                borrow.lob_lio_bob_nod_id = callId;
                borrow.lob_usr_ent_id = usrEntId;
                borrow.lob_status = DbKmLibraryObjectBorrow.STATUS_BORROW;
                borrow.lob_create_usr_id = userId;
                borrow.lob_create_timestamp = curTime;
                borrow.lob_update_usr_id = userId;
                borrow.lob_update_timestamp = curTime;
                borrow.lob_latest_ind = DbKmLibraryObjectBorrow.LATEST_IND_TRUE;
                long lobId = borrow.ins(con);
                
                return lobId;
            }
        } else {
            throw new cwSysMessage(SMSG_REC_NOT_FOUND, "Item call_id = " + callId);
        }
    }

    public void borrowCancel(Connection con, loginProfile prof, qdbEnv env, String userId, long usrEntId, long callId) throws cwException, cwSysMessage, SQLException {
        String[] status = {DbKmLibraryObjectBorrow.STATUS_BORROW};
        
       ResultSet reserveRS = DbKmLibraryObjectBorrow.getByUserCallIdStatus(con, usrEntId, callId, status);
            
        if(reserveRS.next()) {
            Timestamp curTime = cwSQL.getTime(con);

            DbKmLibraryObjectBorrow borrow = new DbKmLibraryObjectBorrow();
            borrow.lob_lio_bob_nod_id = reserveRS.getLong("lob_lio_bob_nod_id");
            borrow.lob_id = reserveRS.getLong("lob_id");
            borrow.get(con);

            borrow.lob_update_usr_id = userId;
            borrow.lob_update_timestamp = curTime;
            borrow.updToNotLatest(con);

            DbKmLibraryObjectBorrow borrowCancel = new DbKmLibraryObjectBorrow();
            borrowCancel.lob_lio_bob_nod_id = borrow.lob_lio_bob_nod_id;
            borrowCancel.lob_usr_ent_id = borrow.lob_usr_ent_id;
            borrowCancel.lob_create_usr_id = userId;
            borrowCancel.lob_create_timestamp = curTime;
            borrowCancel.lob_update_usr_id = userId;
            borrowCancel.lob_update_timestamp = curTime;
            borrowCancel.lob_status = DbKmLibraryObjectBorrow.STATUS_CANCEL;
            borrowCancel.lob_latest_ind = DbKmLibraryObjectBorrow.LATEST_IND_FALSE;
            borrowCancel.ins(con);

            DbKmLibraryObject libObj = new DbKmLibraryObject();
            libObj.lio_bob_nod_id = callId;
            libObj.get(con);
            libObj.lio_num_copy_available++;
            libObj.upd(con);
        } else {
            throw new cwSysMessage(SMSG_REC_NOT_FOUND, "Item call_id = " + callId);
        }
    }

    public void borrowNotify(Connection con, loginProfile prof, String mesgSubject, long recEntId, long[] ccEntIds, long[] bccEntIds, long lobId) throws cwException, cwSysMessage, SQLException {
        KMLibraryMessage kmLibMesg = new KMLibraryMessage();
        Timestamp curTime = cwSQL.getTime(con);
                
        Hashtable params = new Hashtable();
        params.put("template_type", "BORROW_NOTIFICATION");
        params.put("template_subtype", "HTML");
        if( mesgSubject != null && mesgSubject.length() > 0 )
        params.put("subject", mesgSubject);
        params.put("lobId", new Long(lobId));
        Vector vec = new Vector();
        params.put("subject_token", vec);
        long[] entIds = new long[1];
        entIds[0] = recEntId;
        kmLibMesg.insNotify(con, prof, prof.usr_id, entIds, ccEntIds, bccEntIds, curTime, params);
    }

    public static String getBorrowNotifyXML(Connection con, long site_id, String senderUsrId, long usrEntId, long lobId, String mailAccount, long DES_KEY) throws cwException, cwSysMessage, SQLException {
        try { 
            StringBuffer xmlBuffer = new StringBuffer();
            
            ResultSet itemRS = ViewKmLibraryObject.getBorrowItemInfo(con, lobId);
            
            if(itemRS.next()) {
			    KMLibraryMessage kmLibMesg = new KMLibraryMessage();
			    xmlBuffer.append(kmLibMesg.getRecipientXml(con, usrEntId, mailAccount, DES_KEY));			
			    xmlBuffer.append(kmLibMesg.getSenderXml(con, senderUsrId, mailAccount));
                xmlBuffer.append("<borrow_node id=\"" + itemRS.getLong("bob_nod_id") + "\">");
                KMLibraryObjectManager kmLibObjMgr = new KMLibraryObjectManager(con, site_id);
                String xml = kmLibObjMgr.getObjXML(con, itemRS.getLong("bob_nod_id"), site_id);  
                xmlBuffer.append(xml);
                xmlBuffer.append("<title>" + cwUtils.esc4XML(itemRS.getString("obj_title")) + "</title>");
                xmlBuffer.append("<call_number>" + cwUtils.esc4XML(itemRS.getString("bob_code")) + "</call_number>");
                xmlBuffer.append("<copy id=\"" + itemRS.getLong("loc_id") + "\">" + itemRS.getString("loc_copy") + "</copy>");

                dbRegUser regUser = new dbRegUser();
                regUser.usr_ent_id = itemRS.getLong("lob_usr_ent_id");
                regUser.get(con);
                xmlBuffer.append(regUser.getUserShortXML(con, false, true, false, false));
        	    xmlBuffer.append("</borrow_node>");
            } 
            return xmlBuffer.toString();
        } catch (qdbException e) {
            throw new cwException(e.getMessage());
        }
    }

// private method    
    private StringBuffer getOrderSQL(String sort_by, String order_by) {
        String[] ORDER_COL = {COL_CALL_NUMBER, COL_ITEM};
        String[] ORDER_COL_DB = {"call_number", "title"};

        StringBuffer orderSQL = new StringBuffer();
        for(int i=0; i<ORDER_COL.length; i++) {
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

        if (orderSQL.length() == 0){
            orderSQL.append(ORDER_COL_DB[0]);
            orderSQL.append(" DESC ");
        }

        return orderSQL.insert(0, " order by ");
    }

    public String getObjWithCopiesXML(Connection con, long objId, long loc_id, long root_ent_id) throws SQLException, cwSysMessage {
        //get object's basic info latest version
        String version = ViewKmObject.getLatestVersion(con, objId);
        if(version == null) {
            throw new cwSysMessage("GEN005", "Object node id: " + objId + ", version: latest");
        }

        ViewKmObject object = new ViewKmObject();
        object.getBasic(con, objId, version);
        if(object.dbObject == null) {
            throw new cwSysMessage("GEN005", "Object node id: " + objId + ", version: " + version);
        }

        //get ancestor node list
        String ancestorXML = ViewKmFolderManager.getFolderAncestorAsXML(con, object.dbObject.obj_bob_nod_id);
        StringBuffer copyListBuf = new StringBuffer(256);
        Vector v_copy = DbKmLibraryObjectCopy.getCopies(con, objId, loc_id);
        copyListBuf.append("<copy_list>");

        for (int i=0; i<v_copy.size(); i++) {
            DbKmLibraryObjectCopy copy = (DbKmLibraryObjectCopy)v_copy.elementAt(i);
            copyListBuf.append("<copy id=\"").append(copy.loc_id)
                       .append("\" title=\"").append(cwUtils.esc4XML(copy.loc_copy))
                       .append("\" create_usr_id=\"").append(copy.loc_create_usr_id)
                       .append("\" create_timestamp=\"").append(copy.loc_create_timestamp)
                       .append("\" update_usr_id=\"").append(copy.loc_update_usr_id)
                       .append("\" update_timestamp=\"").append(copy.loc_update_timestamp)
                       .append("\" delete_usr_id=\"").append(cwUtils.escNull(copy.loc_delete_usr_id))
                       .append("\" delete_timestamp=\"").append(cwUtils.escNull(copy.loc_delete_timestamp))
                       .append("\">");
            copyListBuf.append("<description>").append(cwUtils.escNull(cwUtils.esc4XML(copy.loc_desc))).append("</description>");
            copyListBuf.append("</copy>");
        }

        copyListBuf.append("</copy_list>");
        
        return getObjectNodeAsXML(object.dbObject, ancestorXML, copyListBuf.toString());
    }

    public String getObjXML(Connection con, long objId, long root_ent_id) throws SQLException, cwSysMessage {
        //get object's basic info latest version
        String version = ViewKmObject.getLatestVersion(con, objId);
        if(version == null) {
            throw new cwSysMessage("GEN005", "Object node id: " + objId + ", version: latest");
        }

        ViewKmObject object = new ViewKmObject();
        object.getBasic(con, objId, version);
        if(object.dbObject == null) {
            throw new cwSysMessage("GEN005", "Object node id: " + objId + ", version: " + version);
        }

        //get ancestor node list
        String ancestorXML = ViewKmFolderManager.getFolderAncestorAsXML(con, object.dbObject.obj_bob_nod_id);

        return getObjectNodeAsXML(object.dbObject, ancestorXML, null);
    }


    public ViewKmObject insObject(Connection con, loginProfile prof,
                                  Vector vNodColName, Vector vNodColType, Vector vNodColValue,
                                  Vector vObjColName, Vector vObjColType, Vector vObjColValue,
                                  Vector vObjClobColName, Vector vObjClobColValue, 
                                  Vector vFileName, String bob_code) 
                                  throws SQLException, cwSysMessage, cwException {
        DbKmBaseObject baseObj = new DbKmBaseObject();
        baseObj.bob_code = bob_code;

        if (baseObj.isCodeExisted(con)) {
            throw new cwSysMessage(SMSG_CODE_EXISTED);
        }

        if(vObjColName.indexOf("obj_publish_ind") < 0) {
            vObjColName.addElement("obj_publish_ind");
            vObjColType.addElement(DbTable.COL_TYPE_BOOLEAN);
            vObjColValue.addElement(new Boolean(true));
        }
        
        if(vObjColName.indexOf("obj_publish_ind") < 0) {
            vObjColName.addElement("obj_publish_ind");
            vObjColType.addElement(DbTable.COL_TYPE_BOOLEAN);
            vObjColValue.addElement(new Boolean(true));
        }
        
        if(vObjColName.indexOf("obj_version") < 0) {
            vObjColName.addElement("obj_version");
            vObjColType.addElement(DbTable.COL_TYPE_STRING);
            vObjColValue.addElement(KMObjectManager.INITIAL_PUBLISH_VERSION);
        }
        
        ViewKmObject object = super.insObject(con, prof,
                                vNodColName, vNodColType, vNodColValue,
                                vObjColName, vObjColType, vObjColValue,
                                vObjClobColName, vObjClobColValue, 
                                vFileName, bob_code); 

        DbKmLibraryObject libObject = new DbKmLibraryObject();
        libObject.lio_bob_nod_id = object.dbObject.obj_bob_nod_id;
        libObject.lio_num_copy = 0;
        libObject.lio_num_copy_available = 0;
        libObject.lio_num_copy_in_stock = 0;
        libObject.ins(con);        

        return object;
    }

    public ViewKmObject editObject(Connection con, qdbEnv env, 
                                      loginProfile prof, String tmpUploadPath,
                                      long objId, Timestamp lastUpdTimestamp,
                                      Vector vObjColName, Vector vObjColType, Vector vObjColValue,
                                      Vector vObjClobColName, Vector vObjClobColValue, 
                                      Vector vFileName, String bob_code) 
                                      throws SQLException, cwSysMessage, cwException {

        DbKmBaseObject baseObj = new DbKmBaseObject();
        baseObj.bob_code = bob_code;
        baseObj.bob_nod_id = objId;

        if (baseObj.isCodeExisted(con)) {
            throw new cwSysMessage(SMSG_CODE_EXISTED);
        }
        
        //get object's basic info latest version
        String lastPubVersion = ViewKmObject.getPublishedVersion(con, objId);
        if(lastPubVersion == null) {
            throw new cwSysMessage("GEN005", "Object node id: " + objId + ", lastPubVersion: latest");
        }
        ViewKmObject object = new ViewKmObject();
        object.getWithAtt(con, objId, lastPubVersion);
        if(object.dbObject == null) {
            throw new cwSysMessage("GEN005", "Object node id: " + objId + ", lastPubVersion: " + lastPubVersion);
        }

        //remove old index
        if(lastPubVersion != null) {
            if(object.vAttachment == null || object.vAttachment.size() == 0) {
                String indexKey = object.dbObject.obj_bob_nod_id + indexKeyToken +
                                object.dbObject.obj_version + indexKeyToken;                    
                removeIndex(env, indexKey);
            } else {
                for(int i=0; i<object.vAttachment.size(); i++) {
                    String indexKey = object.dbObject.obj_bob_nod_id + indexKeyToken +
                                    object.dbObject.obj_version + indexKeyToken + 
                                    ((dbAttachment)object.vAttachment.elementAt(i)).att_filename;
                    removeIndex(env, indexKey);
                }
            }
        }
        
             //get object type
        int index = vObjColName.indexOf("obj_type");
        String objectType = (String)vObjColValue.elementAt(index);
        
        String nature = DbKmObjectType.getNature(con, objectType);    

        //set column values having business logics
        //set database fields
        if(vObjColName.indexOf("obj_bob_nod_id") < 0) {
            vObjColName.addElement("obj_bob_nod_id");
            vObjColType.addElement(DbTable.COL_TYPE_LONG);
            vObjColValue.addElement(new Long(object.dbObject.obj_bob_nod_id));
        }
        if(vObjColName.indexOf("obj_version") < 0) {
            vObjColName.addElement("obj_version");
            vObjColType.addElement(DbTable.COL_TYPE_STRING);
            vObjColValue.addElement(getNextPublishedVersion(object.dbObject.obj_version));
        }
        if(vObjColName.indexOf("obj_publish_ind") < 0) {
            vObjColName.addElement("obj_publish_ind");
            vObjColType.addElement(DbTable.COL_TYPE_BOOLEAN);
            vObjColValue.addElement(new Boolean(true));
        }
        if(vObjColName.indexOf("obj_latest_ind") < 0) {
            vObjColName.addElement("obj_latest_ind");
            vObjColType.addElement(DbTable.COL_TYPE_BOOLEAN);
            vObjColValue.addElement(new Boolean(true));
        }
        if(vObjColName.indexOf("obj_status") < 0) {
            vObjColName.addElement("obj_status");
            vObjColType.addElement(DbTable.COL_TYPE_STRING);
            vObjColValue.addElement(DbKmObject.OBJ_STATUS_CHECKED_IN);
        }
        if(vObjColName.indexOf("obj_update_usr_id") < 0) {
            vObjColName.addElement("obj_update_usr_id");
            vObjColType.addElement(DbTable.COL_TYPE_STRING);
            vObjColValue.addElement(prof.usr_id);
        }

     
        
        //format Vector for aeTemplate
        Vector vTemplate = getObjectTypeTemplate(objectType);
        
        //format Vector for Attachment
        Vector vAttachment = null;
        if(vFileName != null && vFileName.size() > 0) {
            vAttachment = new Vector();
            for(int i=0; i<vFileName.size(); i++) {
                dbAttachment att = new dbAttachment();
                att.att_filename = (String)vFileName.elementAt(i);
                vAttachment.addElement(att);
            }
        } else {
            vAttachment = object.vAttachment;
        }
        Vector vOrgDomainIdList = object.getParentDomainId(con);
                
        //insert new version
        ViewKmObject newVersion = ViewKmObject.synNewVersion(con, lastUpdTimestamp, object,
                                                             vObjColName, vObjColType, vObjColValue,
                                                             vObjClobColName, vObjClobColValue, 
                                                             vTemplate, vAttachment, vOrgDomainIdList); 

        if(newVersion == null) {
            throw new cwSysMessage("GEN006");
        }
        newVersion.dbObject.obj_code = bob_code;        
        //copy files
        if(vFileName != null && vFileName.size() > 0) {
            String saveDirPath = env.INI_OBJ_DIR_UPLOAD + dbUtils.SLASH + 
                                 newVersion.dbObject.obj_bob_nod_id + dbUtils.SLASH + 
                                 newVersion.dbObject.obj_version;
            try {
                dbUtils.moveDir(tmpUploadPath, saveDirPath);
            } catch(qdbException e) {
                throw new cwException(e.getMessage());
            }
        } else {
            String srcDir = env.INI_OBJ_DIR_UPLOAD + dbUtils.SLASH + object.dbObject.obj_bob_nod_id + dbUtils.SLASH + object.dbObject.obj_version;
            String desDir = env.INI_OBJ_DIR_UPLOAD + dbUtils.SLASH + newVersion.dbObject.obj_bob_nod_id + dbUtils.SLASH + newVersion.dbObject.obj_version;
            
            try {
                dbUtils.copyDir(srcDir, desDir);
            } catch(qdbException e) {
                throw new cwException(e.getMessage());
            }
        }

        //add new index
        String[] str_domain_id_list = null;
        if(vOrgDomainIdList != null) {
            str_domain_id_list = new String[vOrgDomainIdList.size()];
            for(int j=0; j<vOrgDomainIdList.size(); j++) {
                str_domain_id_list[j] = ((Long)vOrgDomainIdList.elementAt(j)).toString();
            }
        }
        if(newVersion.vAttachment == null || newVersion.vAttachment.size() == 0) {
            //build index for searchEngine
            String indexKey = newVersion.dbObject.obj_bob_nod_id + indexKeyToken +
                            newVersion.dbObject.obj_version + indexKeyToken;

            bulidIndex(env, indexKey, null, newVersion, str_domain_id_list);
        } else {
            for(int i=0; i<newVersion.vAttachment.size(); i++) {
                //build index for searchEngine
                String indexKey = newVersion.dbObject.obj_bob_nod_id + indexKeyToken +
                                newVersion.dbObject.obj_version + indexKeyToken + 
                                ((dbAttachment)newVersion.vAttachment.elementAt(i)).att_filename;

                bulidIndex(env, indexKey, 
                          ((dbAttachment)newVersion.vAttachment.elementAt(i)).att_filename, 
                          newVersion, str_domain_id_list);
            }
        }

        //update kmBaseObject
        baseObj.upd(con); 
        
        return newVersion;
    }
    
    public void insObjectCopy(Connection con, loginProfile prof, long obj_bob_nod_id, String loc_copy, String loc_desc) throws SQLException, cwSysMessage {        
        DbKmLibraryObjectCopy copy = new DbKmLibraryObjectCopy();
        copy.loc_lio_bob_nod_id = obj_bob_nod_id;
        copy.loc_copy = loc_copy;

        if (copy.isCopyExisted(con)) {
            throw new cwSysMessage(SMSG_COPY_EXISTED);
        }

        copy.loc_desc = loc_desc;
        copy.loc_create_usr_id = prof.usr_id;
        copy.loc_update_usr_id = prof.usr_id;
        copy.ins(con);

        DbKmLibraryObject libObj = new DbKmLibraryObject();
        libObj.lio_bob_nod_id = obj_bob_nod_id;
        libObj.get(con);
        libObj.lio_num_copy_available++;
        libObj.lio_num_copy_in_stock++;
        libObj.lio_num_copy++;
        libObj.upd(con);
    }

    public void updObjectCopy(Connection con, loginProfile prof, long obj_bob_nod_id, long loc_id, String loc_copy, String loc_desc, Timestamp loc_update_timestamp) throws SQLException, cwSysMessage {
        DbKmLibraryObjectCopy copy = new DbKmLibraryObjectCopy();
        copy.loc_lio_bob_nod_id = obj_bob_nod_id;
        copy.loc_id = loc_id;
        copy.get(con);
        
        if (!copy.loc_update_timestamp.equals(loc_update_timestamp)) {
            throw new cwSysMessage("GEN006");
        }

        copy.loc_copy = loc_copy;

        if (copy.isCopyExisted(con)) {
            throw new cwSysMessage(SMSG_COPY_EXISTED);
        }

        copy.loc_desc = loc_desc;
        copy.loc_update_usr_id = prof.usr_id;
        copy.loc_update_timestamp = cwSQL.getTime(con);
        copy.upd(con);
    }

    public void delObjectCopy(Connection con, loginProfile prof, long obj_bob_nod_id, long loc_id, Timestamp loc_update_timestamp) throws SQLException, cwSysMessage {
        DbKmLibraryObjectCopy copy = new DbKmLibraryObjectCopy();
        copy.loc_lio_bob_nod_id = obj_bob_nod_id;
        copy.loc_id = loc_id;
        copy.get(con);
        
        if (!copy.loc_update_timestamp.equals(loc_update_timestamp)) {
            throw new cwSysMessage("GEN006");
        }

        DbKmLibraryObjectBorrow borrow = new DbKmLibraryObjectBorrow();
        borrow.lob_lio_bob_nod_id = obj_bob_nod_id;
        borrow.lob_loc_id = loc_id;
        
        if (borrow.onLoan(con)) {
            throw new cwSysMessage(SMSG_COPY_ON_LOAN);
        }

        if (copy.isLastCopy(con) && borrow.hasOutstandingBorrow(con)) {
            throw new cwSysMessage(SMSG_ITEM_HAS_OUTSTNADING_REQUEST);
        }
        
        copy.loc_delete_usr_id = prof.usr_id;
        copy.loc_delete_timestamp = cwSQL.getTime(con);
        copy.del(con);

        DbKmLibraryObject libObj = new DbKmLibraryObject();
        libObj.lio_bob_nod_id = obj_bob_nod_id;
        libObj.get(con);
        libObj.lio_num_copy_available--;
        libObj.lio_num_copy_in_stock--;
        libObj.lio_num_copy--;
        libObj.upd(con); 
    }
    
    
    /**
    * Get user elibrary object record as xml format
    */
    public String getUserRecAsXML(Connection con, long usr_ent_id, String sort_by, String order_by)
        throws SQLException, cwException{
            
            String[] status = { DbKmLibraryObjectBorrow.STATUS_CHECKOUT,
                                DbKmLibraryObjectBorrow.STATUS_BORROW,
                                DbKmLibraryObjectBorrow.STATUS_RESERVE };
            ResultSet rs = ViewKmLibraryObject.getUserRec(con, usr_ent_id, status, sort_by, order_by);
            StringBuffer userXml = null;
            StringBuffer summaryXml = new StringBuffer();
            StringBuffer checkoutXml = new StringBuffer("<object_checked_out_list>");
            StringBuffer waitingXml = new StringBuffer("<object_waiting_list>");
            int borrowed = 0;
            int reserved = 0;
            int overdue = 0;
            boolean bOverdue = false;
            Timestamp cur_time = cwSQL.getTime(con);
            while(rs.next()){
                bOverdue = false;
                if( userXml == null ){
                    userXml = new StringBuffer();
                    userXml.append("<usr ")
                           .append(" ent_id=\"").append(rs.getLong("usr_ent_id")).append("\" ")
                           .append(" display_bil=\"").append(cwUtils.esc4XML(rs.getString("usr_display_bil"))).append("\" ")
                           .append("/>");
                }
                if( (rs.getString("lob_status")).equalsIgnoreCase(DbKmLibraryObjectBorrow.STATUS_CHECKOUT) ) {
                    borrowed++;                    
                    if( cur_time.after(rs.getTimestamp("lob_due_timestamp")) ){
                        bOverdue = true;
                        overdue++;
                    }
                    checkoutXml.append(constructUserRecObjTagXml(con, rs, bOverdue));
                    
                } else if( (rs.getString("lob_status")).equalsIgnoreCase(DbKmLibraryObjectBorrow.STATUS_BORROW) ) {
                    borrowed++;
                    waitingXml.append(constructUserRecObjTagXml(con, rs, false));
                    
                } else if( (rs.getString("lob_status")).equalsIgnoreCase(DbKmLibraryObjectBorrow.STATUS_RESERVE) ) {
                    reserved++;
                    waitingXml.append(constructUserRecObjTagXml(con, rs, false));
                }

            }
            rs.close();
            
            //No Record in result set
            if( userXml == null ) {
                dbRegUser dbUser = new dbRegUser();
                dbUser.usr_ent_id = usr_ent_id;
                try{
                    dbUser.get(con);
                }catch(qdbException e) {
                    throw new cwException(e.getMessage());
                }
                userXml = new StringBuffer();
                userXml.append("<usr ")
                        .append(" ent_id=\"").append(usr_ent_id).append("\" ")
                        .append(" display_bil=\"").append(cwUtils.esc4XML(dbUser.usr_display_bil)).append("\" ")
                        .append("/>");
            }            
            checkoutXml.append("</object_checked_out_list>");
            waitingXml.append("</object_waiting_list>");

            summaryXml.append("<summary ")
                      .append(" borrowed=\"").append(borrowed).append("\" ")
                      .append(" overdue=\"").append(overdue).append("\" ")
                      .append(" reserved=\"").append(reserved).append("\" ")
                      .append("/>");

            StringBuffer xml = new StringBuffer();
            xml.append("<user_record>")
               .append(userXml)
               .append(summaryXml)
               .append(checkoutXml)
               .append(waitingXml)
               .append("</user_record>");
            return xml.toString();
        }


    /**
    *Generate the object tag in xml used by function getUserRecAsXML
    */
    private String constructUserRecObjTagXml(Connection con, ResultSet rs, boolean bOverdue)
        throws SQLException{
            StringBuffer buf = new StringBuffer();
            buf.append("<object ")
               .append(" lob_id=\"").append(rs.getLong("lob_id")).append("\" ")
               .append(" nod_id=\"").append(rs.getLong("bob_nod_id")).append("\" ")
               .append(" loc_id=\"").append(rs.getLong("loc_id")).append("\" ")
               .append(" type=\"").append(cwUtils.esc4XML(rs.getString("obj_type"))).append("\" ")
               .append(" call_num=\"").append(cwUtils.esc4XML(rs.getString("bob_code"))).append("\" ")
               .append(" copy_num=\"").append(cwUtils.esc4XML(rs.getString("loc_copy"))).append("\" ")
               .append(" title=\"").append(cwUtils.esc4XML(rs.getString("obj_title"))).append("\" ")
               .append(" avail_copy=\"").append(rs.getLong("lio_num_copy_available")).append("\" ")
               .append(" avail_copy_in_stock=\"").append(rs.getLong("lio_num_copy_in_stock")).append("\" ")               
               .append(" status=\"").append(rs.getString("lob_status")).append("\" ")
               .append(" request_date=\"").append(rs.getTimestamp("lob_create_timestamp")).append("\" ");

            if( (rs.getString("lob_status")).equalsIgnoreCase(DbKmLibraryObjectBorrow.STATUS_RESERVE) ||
                (rs.getString("lob_status")).equalsIgnoreCase(DbKmLibraryObjectBorrow.STATUS_BORROW) ){
                buf.append(" wait_pos=\"")
                   .append(getWaitingPos(con, rs.getLong("bob_nod_id"), rs.getLong("usr_ent_id"), rs.getString("lob_status"), rs.getTimestamp("lob_create_timestamp")) )
                   .append("\" ");
            } else if( (rs.getString("lob_status")).equalsIgnoreCase(DbKmLibraryObjectBorrow.STATUS_CHECKOUT) ) {
                buf.append(" due_date=\"").append(rs.getTimestamp("lob_due_timestamp")).append("\" ")
                   .append(" renewal=\"").append(rs.getLong("lob_renew_no")).append("\" ");
                if( bOverdue ) {
                    buf.append(" overdue=\"Y\" ");
                } else {
                    buf.append(" overdue=\"N\" ");
                }
            }
            buf.append("/>");
            return buf.toString();
        }
        
    private int getWaitingPos(Connection con, long nod_id, long usr_ent_id, String status, Timestamp timestamp)
        throws SQLException{
            String[] statusArray = { DbKmLibraryObjectBorrow.STATUS_RESERVE, DbKmLibraryObjectBorrow.STATUS_BORROW };
            ResultSet rs = DbKmLibraryObjectBorrow.getByNodeIdNStatus(con, nod_id, statusArray, "lob_create_timestamp", "ASC");
            int count = 1;
            while(rs.next()){
                if( rs.getLong("lob_usr_ent_id") == usr_ent_id &&
                   (rs.getString("lob_status")).equalsIgnoreCase(status) &&
                   (rs.getTimestamp("lob_create_timestamp")).equals(timestamp) )
                   break;
                else
                    count++;
            }
            rs.close();
            return count;
        }
        
        
        
    /**
    *  Get elibrary item record as xml format
    */
    public String getItemRecAsXML(Connection con, loginProfile prof, qdbEnv env, String call_num, String tvw_id)
        throws SQLException, cwException, cwSysMessage {
            
            DbKmNode dbNode = ViewKmLibraryObject.getNode(con, call_num);
            Vector readableVec = DbKmNodeAccess.getReadableNode(con, prof.usrGroupsList());
            if( readableVec.indexOf(new Long(dbNode.nod_parent_nod_id)) != -1 ) 
                return getItemRecAsXML(con, prof, env, dbNode.nod_id, tvw_id);
            else
                return getItemRecAsXML(con, prof, env, 0, tvw_id);
        }        

    public String getItemRecAsXML(Connection con, loginProfile prof, qdbEnv env, long nod_id, String tvw_id)
        throws SQLException, cwException, cwSysMessage {
            
            StringBuffer xml = new StringBuffer();
            xml.append("<object_inquiry>");
            
            //Construct Object valued template
            KMObjectManager kmObjMgr = new KMObjectManager(con, prof.root_ent_id);
            if( nod_id > 0 ) 
                xml.append(kmObjMgr.getObjectVersionAsXML(con, prof, env, nod_id, null, null, tvw_id, 0));

            Timestamp cur_time = cwSQL.getTime(con);
            ResultSet rs = null;
            String[] status = null;

            //Copy List
            xml.append("<object_list>");
            if( nod_id > 0 ) {
                Vector objVec = DbKmLibraryObjectCopy.getCopies(con, nod_id, 0);
                for(int i=0; i<objVec.size(); i++){
                    DbKmLibraryObjectCopy dbKmLibObj = (DbKmLibraryObjectCopy)objVec.elementAt(i);
                    xml.append("<object ")
                    .append(" loc_id=\"").append(dbKmLibObj.loc_id).append("\" ")
                    .append(" copy_num=\"").append(dbKmLibObj.loc_copy).append("\" ")
                    .append(" />");
                }
            }
            xml.append("</object_list>");


            //Construct onloan list             
            status = new String[1];
            status[0] = DbKmLibraryObjectBorrow.STATUS_CHECKOUT;
            boolean bOverdue = false;
            xml.append("<object_onloan_list>");
            String tagName = "object_onloan";
            if( nod_id > 0 ) {
                rs = ViewKmLibraryObject.getItemRec(con, nod_id, status, "lob_due_timestamp", "ASC");
                while(rs.next()){
                    bOverdue = false;
                    if( cur_time.after(rs.getTimestamp("lob_due_timestamp")) ){
                        bOverdue = true;
                    }                
                    xml.append(constructItemRecObjTagXml(con, rs, env, bOverdue, tagName, nod_id));
                }
                rs.close();
            }

            xml.append("</object_onloan_list>");

            
            
            //Construct avail copy list
            xml.append("<object_avail_list>");
            if( nod_id > 0 ) {
                rs = ViewKmLibraryObject.getAvailItemCopy(con, nod_id, "loc_copy", "ASC");
                while(rs.next()){
                    xml.append("<object_avail ")
                    .append(" loc_id=\"").append(rs.getLong("loc_id")).append("\" ")
                    .append(" copy_num=\"").append(cwUtils.esc4XML(rs.getString("loc_copy"))).append("\" ")
                    .append("/>");
                }
                rs.close();
            }

            xml.append("</object_avail_list>");
            
            //consturct user request list
            status = new String[2];
            status[0] = DbKmLibraryObjectBorrow.STATUS_BORROW;
            status[1] = DbKmLibraryObjectBorrow.STATUS_RESERVE;
            xml.append("<user_request_list>");
            tagName = "user_request";
            if( nod_id > 0 ) {
                rs = ViewKmLibraryObject.getItemRec(con, nod_id, status, "lob_create_timestamp", "ASC");
                while(rs.next()){
                    xml.append(constructItemRecObjTagXml(con, rs, env, false, tagName, nod_id));
                }
                rs.close();
            }
            xml.append("</user_request_list>");
            xml.append("</object_inquiry>");
            return xml.toString();
            
        }
        
        
    /**
    *Generate the object tag in xml used by function getItemRecAsXML
    */
    private String constructItemRecObjTagXml(Connection con, ResultSet rs, qdbEnv env, boolean bOverdue, String tagName, long nod_id)
        throws SQLException{
            StringBuffer buf = new StringBuffer();
            buf.append("<").append(tagName)
               .append(" lob_id=\"").append(rs.getLong("lob_id")).append("\" ")               
               .append(" usr_ent_id=\"").append(rs.getLong("usr_ent_id")).append("\" ")
               .append(" usr_display_bil=\"").append(cwUtils.esc4XML(rs.getString("usr_display_bil"))).append("\" ")
               .append(" request_date=\"").append(rs.getTimestamp("lob_create_timestamp")).append("\" ");
               //.append(" num_reserve=\"").append(DbKmLibraryObjectBorrow.getLibraryBorrowStatusCount(con, nod_id, DbKmLibraryObjectBorrow.STATUS_RESERVE)).append("\" ");               
            if( (rs.getString("lob_status")).equalsIgnoreCase(DbKmLibraryObjectBorrow.STATUS_CHECKOUT) ) {
                buf.append(" due_date=\"").append(rs.getTimestamp("lob_due_timestamp")).append("\" ")
                   .append(" loc_id=\"").append(rs.getLong("loc_id")).append("\" ")
                   .append(" copy_num=\"").append(cwUtils.esc4XML(cwUtils.esc4XML(rs.getString("loc_copy")))).append("\" ")
                   .append(" renewal=\"").append(rs.getLong("lob_renew_no")).append("\" ");
                if( bOverdue )
                    buf.append(" overdue=\"Y\" ");
                else
                    buf.append(" overdue=\"N\" ");
            } else {
                buf.append(" status=\"").append(rs.getString("lob_status")).append("\" ");
            }
    
            if(DbKmLibraryObjectBorrow.isValidToBorrow(con, nod_id, rs.getLong("usr_ent_id"), env.KM_LIB_BORROW_LIMIT, env.KM_LIB_OVERDUE_LIMIT)) {
                buf.append(" violate=\"false\" ");
            } else {
                buf.append(" violate=\"true\" ");
            }
    
            buf.append("/>");
            return buf.toString();
        }
        
    // generate history as XML with RS (provided by getUserHistory/getCopyHistory @ ViewKmLibraryObject)
    private String historyAsXML(Connection con, loginProfile prof, ResultSet rs, String type) throws cwException, SQLException, cwSysMessage {
        try { 
            StringBuffer xmlBuffer = new StringBuffer();
            StringBuffer userXML = new StringBuffer();
            dbRegUser regUser = new dbRegUser();
            while(rs.next()) {
                String status = rs.getString("lob_status");
                xmlBuffer.append("<record id=\"" + rs.getLong("lob_id") + "\" status=\"" + rs.getString("lob_status") + "\">");
                if( (type.equalsIgnoreCase("USER") && userXML.length()==0) ||
                     type.equalsIgnoreCase("COPY") ) {
                    regUser.usr_ent_id = rs.getLong("usr_ent_id");
                    regUser.get(con);
                    userXML = regUser.getUserShortXML(con, false, true, false, false);
                } 
                xmlBuffer.append(userXML);

                xmlBuffer.append("<item id=\"" + rs.getLong("bob_nod_id") + "\">");
                String xml = getObjXML(con, rs.getLong("bob_nod_id"), prof.root_ent_id);  
                xmlBuffer.append(xml);
                xmlBuffer.append("<title>" + cwUtils.esc4XML(rs.getString("obj_title")) + "</title>");
                if(status.equalsIgnoreCase(DbKmLibraryObjectBorrow.STATUS_CHECKIN) ||
                    status.equalsIgnoreCase(DbKmLibraryObjectBorrow.STATUS_CHECKOUT)) {
                    xmlBuffer.append("<call_number>" + cwUtils.esc4XML(rs.getString("bob_code")) + "</call_number>");
                    xmlBuffer.append("<copy id=\"" + rs.getLong("loc_id") + "\">");
                    xmlBuffer.append("<copy_number>" + cwUtils.esc4XML(rs.getString("loc_copy")) + "</copy_number>");
                    xmlBuffer.append("<desc>" + cwUtils.esc4XML(cwSQL.getClobValue(rs, "loc_desc")) + "</desc>");
                    xmlBuffer.append("</copy>");
                    xmlBuffer.append("<renew>" + rs.getLong("lob_renew_no") + "</renew>");
                    xmlBuffer.append("<due_timestamp>" + rs.getTimestamp("lob_due_timestamp") + "</due_timestamp>");
                }
                xmlBuffer.append("<latest_ind>" + rs.getBoolean("lob_latest_ind") + "</latest_ind>");
                xmlBuffer.append("</item>");
                xmlBuffer.append("<process>");
                regUser.usr_ent_id = rs.getLong("process_usr_ent_id");
                regUser.get(con);
                xmlBuffer.append(regUser.getUserShortXML(con, false, false, false, false));
                xmlBuffer.append("<update_timestamp>" + rs.getTimestamp("lob_update_timestamp") + "</update_timestamp>");
                xmlBuffer.append("</process>");
                xmlBuffer.append("</record>");
            }

            return xmlBuffer.toString();
        } catch (qdbException e) {
            throw new cwException(e.getMessage());
        }
    }

    public String getObjectVersionAsXML(Connection con, loginProfile prof, qdbEnv env,
                                        long objId, String version,
                                        String oty_code, String tvw_id,
                                        long parent_nod_id) 
                                        throws SQLException, cwSysMessage, cwException {
        //get object details
        ViewKmObject object = new ViewKmObject();
        object.getAll(con, objId, ViewKmObject.getLatestVersion(con, objId));
        next_version = getNextPublishedVersion(object.dbObject.obj_version);

        return super.getObjectVersionAsXML(con, prof, env, objId, version, oty_code, tvw_id, parent_nod_id);
    }

    public void markDel(Connection con, qdbEnv env, long objId, Timestamp lastUpdTimestamp, String usr_id) 
        throws SQLException, cwSysMessage, cwException {
        Vector v = DbKmLibraryObjectCopy.getCopies(con, objId, 0);
        
        if (v.size() > 0) {
            throw new cwSysMessage(SMSG_ITEM_HAS_OUTSTNADING_COPY);    
        }
        
        DbKmLibraryObjectBorrow borrow = new DbKmLibraryObjectBorrow();
        borrow.lob_lio_bob_nod_id = objId;
        
        if (borrow.hasOutstandingBorrow(con)) {
            throw new cwSysMessage(SMSG_ITEM_HAS_OUTSTNADING_REQUEST);
        }

        super.markDel(con, env, objId, lastUpdTimestamp, usr_id);
    }
    
    public String getLibObjAsXML(Connection con, HttpSession sess, long nod_id, long usr_ent_id, String usrGroupsList, cwPagination cwPage)
        throws SQLException, cwSysMessage, cwException {
        
            boolean useSession = false;
            Vector childVec = null;
            Timestamp sess_time = (Timestamp) sess.getAttribute(KMLibraryModule.SESS_KM_LIB_CHILD_OBJECTS_TS);
            if (cwPage.ts != null && sess_time != null && sess_time.equals(cwPage.ts)) {
                // use session 
                childVec = (Vector) sess.getAttribute(KMLibraryModule.SESS_KM_LIB_CHILD_OBJECTS_VEC);
                if (childVec != null && childVec.size() > 0) {
                    useSession = true;
                }
            }
            
            if (!useSession) {
                ViewKmLibraryObject vKmObj = new ViewKmLibraryObject();
                childVec = vKmObj.getLibChildObjectsNStatus(con, nod_id, usr_ent_id, cwPage.sortCol, cwPage.sortOrder);
                cwPage.ts = cwSQL.getTime(con);
                sess.setAttribute(KMLibraryModule.SESS_KM_LIB_CHILD_OBJECTS_TS, cwPage.ts);
                sess.setAttribute(KMLibraryModule.SESS_KM_LIB_CHILD_OBJECTS_VEC, childVec);
            }
            
            cwPage.totalRec  = childVec.size();
            cwPage.totalPage = (int)Math.ceil((float)cwPage.totalRec/(float)cwPage.pageSize);
            int begin=(cwPage.curPage-1)*cwPage.pageSize;
            int end=begin+cwPage.pageSize;
            if(end>childVec.size()) {
                end = childVec.size();
            }

            StringBuffer xml = new StringBuffer();
            xml.append(cwPage.asXML());
            
            DbKmLibraryObjectBorrow dbKmLibObjBorrow = new DbKmLibraryObjectBorrow();
            xml.append(dbKmLibObjBorrow.getUserSummaryAsXML(con, usr_ent_id));
            DbKmFolder dbKmFolder = new DbKmFolder();
            dbKmFolder.fld_nod_id = nod_id;
            dbKmFolder.get(con);
            xml.append("<object_list>");
            xml.append("<folder id=\"" + dbKmFolder.fld_nod_id + "\">");
            xml.append("<type>" + cwUtils.esc4XML(dbKmFolder.fld_type) + "</type>");
            xml.append("<title>" + cwUtils.esc4XML(dbKmFolder.fld_title) + "</title>");
            xml.append("<desc>" + cwUtils.esc4XML(dbKmFolder.fld_desc) + "</desc>");
            xml.append("</folder>");
            
            for(int i=begin; i<end; i++){
                ViewKmLibraryObject vLibObj = (ViewKmLibraryObject)childVec.elementAt(i);
                xml.append("<object ")
                   .append(" nod_id=\"").append(vLibObj.itm.bobNodId).append("\" ")
//                   .append(" lob_id=\"").append(vLibObj.dbKmLibObjBorrow.lob_id).append("\" ")
                   .append(" type=\"").append(vLibObj.itm.type).append("\" ")
                   .append(" call_num=\"").append(vLibObj.itm.callNumber).append("\" ")
                   .append(" title=\"").append(cwUtils.esc4XML(vLibObj.itm.title)).append("\" ")
                   .append(" avail_copy=\"").append(vLibObj.dbKmLibObj.lio_num_copy_available).append("\" ")
//                   .append(" renewal=\"").append(vLibObj.dbKmLibObjBorrow.lob_renew_no).append("\" ")
//                   .append(" status=\"").append(vLibObj.dbKmLibObjBorrow.lob_status).append("\" ")
                   .append(" num_reserve=\"").append(DbKmLibraryObjectBorrow.getLibraryBorrowStatusCount(con, vLibObj.itm.bobNodId, DbKmLibraryObjectBorrow.STATUS_RESERVE)).append("\" ")
                   .append(" num_copy=\"").append(vLibObj.itm.numCopy).append("\" ")
                   .append(" author=\"").append(cwUtils.esc4XML(vLibObj.itm.author)).append("\" ")
//                   .append(" />");
                   .append(" >");
                xml.append("<borrow_list>");
                for(int j=0;j<vLibObj.itm.lob_status.size();j++) {
                    String lob_status = (String) vLibObj.itm.lob_status.elementAt(j);
                    long lob_id = ((Long) vLibObj.itm.lob_id.elementAt(j)).longValue();
                    long lob_renew_no = ((Long) vLibObj.itm.lob_renew_no.elementAt(j)).longValue();
                    xml.append("<borrow ")
                       .append(" lob_id=\"" + lob_id + "\" ")
                       .append(" renewal=\"" + lob_renew_no + "\" ")
                       .append(" status=\"" + lob_status + "\" ")
                       .append(" />");
                }
                xml.append("</borrow_list>");
                xml.append("</object>");
            }
            xml.append("</object_list>");
            return xml.toString();
        }

    public String getOverdueListXML(Connection con, HttpSession sess, cwPagination cwPage) throws SQLException, cwException {
        Vector v_overdue = null;
        Timestamp cur_time = cwSQL.getTime(con);
    
        if (sess != null) {
            cwPagination sess_page = (cwPagination)sess.getAttribute(SESS_KML_OVERDUE_CWPAGE);

            if (sess_page != null && sess_page.ts.equals(cwPage.ts)) {
                v_overdue = (Vector)sess.getAttribute(SESS_KML_OVERDUE_VECTOR_DATA);
                sess_page.curPage = cwPage.curPage;
                sess_page.pageSize = cwPage.pageSize;
                cwPage = sess_page;
            } else {
                v_overdue = ViewKmLibraryObject.getOverdueInfo(con, owner_ent_id, cur_time, cwPage.sortCol, cwPage.sortOrder);

                if (cwPage.pageSize == 0) {
                    cwPage.pageSize = cwPagination.defaultPageSize;
                }

                cwPage.totalRec = v_overdue.size();
                cwPage.totalPage = (int)Math.ceil( (float)cwPage.totalRec / (float) cwPage.pageSize );
                cwPage.ts = cur_time;

                sess.setAttribute(SESS_KML_OVERDUE_CWPAGE, cwPage);
                sess.setAttribute(SESS_KML_OVERDUE_VECTOR_DATA, v_overdue);
            }
        }

        StringBuffer result = new StringBuffer();
        result.append("<overdue_record_list>");

        if (v_overdue != null) {
            for (int count = cwPage.pageSize*(cwPage.curPage-1); count < cwPage.pageSize*cwPage.curPage && count < v_overdue.size(); count++) {
                DbKmLibraryObjectBorrow borrow = (DbKmLibraryObjectBorrow)v_overdue.elementAt(count);
                dbRegUser usr = new dbRegUser();
                usr.usr_ent_id = borrow.lob_usr_ent_id;

                try {
                    usr.get(con);
                } catch (qdbException e) {
                    throw new cwException(e.getMessage());   
                }
                
                result.append("<overdue_record>");
                result.append(usr.getUserShortXML(con, false, true, false, false));
                result.append("<borrowed_item")
                      .append(" call_number=\"").append(cwUtils.escNull(cwUtils.esc4XML(borrow.object.obj_code)))
                      .append("\" copy_number=\"").append(cwUtils.escNull(cwUtils.esc4XML(borrow.objectCopy.loc_copy)))
                      .append("\" title=\"").append(cwUtils.escNull(cwUtils.esc4XML(borrow.object.obj_title)))
                      .append("\" author=\"").append(cwUtils.escNull(cwUtils.esc4XML(borrow.object.obj_author)))
                      .append("\" type=\"").append(cwUtils.escNull(cwUtils.esc4XML(borrow.object.obj_type)))
                      .append("\" nature=\"").append(cwUtils.escNull(cwUtils.esc4XML(borrow.object.obj_nature)))
                      .append("\" status=\"").append(borrow.lob_status)
                      .append("\" renewed=\"").append(borrow.lob_renew_no)
                      .append("\" due_date=\"").append(borrow.lob_due_timestamp)
                      .append("\"/>");                      
                result.append("</overdue_record>");
            }
        }

        result.append("</overdue_record_list>");
        result.append(cwPage.asXML());

        return result.toString();
    }

       /**
    Publish the object to domain
    @param con Connection to database
    @param env qdbEnv of wizBank
    @param prof loginProfile to wizBank
    @param lastUpdTimestamp last update timestamp for concurrence control
    @param objId object node id 
    @param domain_id_list long array of domain id which the published object will be attached to
    */
    public void addDomain(Connection con, qdbEnv env, loginProfile prof, 
                                       Timestamp lastUpdTimestamp, 
                                       long objId, long[] domain_id_list) 
                                       throws SQLException, cwSysMessage, cwException {
        
        //get object's basic info latest version
//        String version = ViewKmObject.getLatestVersion(con, objId);
        //get published version
        String lastPubVersion = ViewKmObject.getPublishedVersion(con, objId);

        if(lastPubVersion == null) {
            throw new cwSysMessage("GEN005", "Object node id: " + objId + ", version: latest");
        }

        ViewKmObject lastPubObject = new ViewKmObject();
        lastPubObject.getWithTplNAtt(con, objId, lastPubVersion);
        if(lastPubObject.dbObject == null) {
            throw new cwSysMessage("GEN005", "Object node id: " + objId + ", lastPubVersion: " + lastPubVersion);
        }

      /*        //get original published domain id
        Vector vOrgDomainIdList = null;
            vOrgDomainIdList = object.getParentDomainId(con);*/
        
        //Vector for Domain Id
        Vector vDomainIdList = null;
        if(domain_id_list != null) {
            vDomainIdList = new Vector();
            for(int i=0; i<domain_id_list.length; i++) {
                vDomainIdList.addElement(new Long(domain_id_list[i]));
            }
        }
        lastPubObject.addDomain(con, lastUpdTimestamp, 
                vDomainIdList); 

        //remove old index
        if(lastPubVersion != null) {
            if(lastPubObject.vAttachment == null || lastPubObject.vAttachment.size() == 0) {
                String indexKey = lastPubObject.dbObject.obj_bob_nod_id + indexKeyToken +
                                lastPubObject.dbObject.obj_version + indexKeyToken;                    
                removeIndex(env, indexKey);
            } else {
                for(int i=0; i<lastPubObject.vAttachment.size(); i++) {
                    String indexKey = lastPubObject.dbObject.obj_bob_nod_id + indexKeyToken +
                                    lastPubObject.dbObject.obj_version + indexKeyToken + 
                                    ((dbAttachment)lastPubObject.vAttachment.elementAt(i)).att_filename;
                    removeIndex(env, indexKey);
                }
            }
        }

        //add new index
        String[] str_domain_id_list = null;
        if(domain_id_list != null) {
            str_domain_id_list = new String[domain_id_list.length];
            for(int j=0; j<domain_id_list.length; j++) {
                str_domain_id_list[j] = Long.toString(domain_id_list[j]);
            }
        }
        if(lastPubObject.vAttachment == null || lastPubObject.vAttachment.size() == 0) {
            //build index for searchEngine
            String indexKey = lastPubObject.dbObject.obj_bob_nod_id + indexKeyToken +
                            lastPubObject.dbObject.obj_version + indexKeyToken;

            bulidIndex(env, indexKey, null, lastPubObject, str_domain_id_list);
        } else {
            for(int i=0; i<lastPubObject.vAttachment.size(); i++) {
                //build index for searchEngine
                String indexKey = lastPubObject.dbObject.obj_bob_nod_id + indexKeyToken +
                                lastPubObject.dbObject.obj_version + indexKeyToken + 
                                ((dbAttachment)lastPubObject.vAttachment.elementAt(i)).att_filename;

                bulidIndex(env, indexKey, 
                          ((dbAttachment)lastPubObject.vAttachment.elementAt(i)).att_filename, 
                          lastPubObject, str_domain_id_list);
            }
        }    
    }
    
    
    
    
    /**
    Get the xml of the search result
    @param sess HttpSession of the user
    @param usrGroupsList ancestor group list of the user (included his/her own enity id)
    @param obj_title object title
    @param obj_author object author
    @param obj_type object type
    @param node_id_list  domain/work folder id list
    @param key_words    search criteria
    @param flag indicator of search including the deleted object
    @return xml of the search result
    */
    public String getLrnAdvSearchResultAsXML(Connection con, HttpSession sess, long usr_ent_id, String usrGroupsList, String obj_title, String obj_author, String[] obj_type_list, String key_words, String call_num, long[] node_id_list, cwPagination cwPage, String DB_PATH, boolean flag)
        throws SQLException
    {
        boolean useSession = false;
        // All objects that matched the searching criteria
        Vector matchedVec = new Vector();
        Vector readableVec = null;
        Hashtable itemUserStatus = null;
        Hashtable itemStatus = null;
        Vector vObjVec = null;
        ViewKmObject vObj = null;
        Vector objIdVec = null;
        
        Timestamp sess_time = (Timestamp) sess.getAttribute(KMModule.SESS_KM_SEARCH_RESULT_TS);
        if (cwPage.ts != null && sess_time != null && sess_time.equals(cwPage.ts)) {
                // use session 
                matchedVec = (Vector) sess.getAttribute(KMModule.SESS_KM_SEARCH_RESULT_VEC);
                itemUserStatus = (Hashtable) sess.getAttribute(KMLibraryModule.SESS_KM_SEARCH_ITEM_USER_STATUS);
                itemStatus = (Hashtable) sess.getAttribute(KMLibraryModule.SESS_KM_SEARCH_ITEM_STATUS);
                
                if (matchedVec != null && matchedVec.size() > 0) {
                    useSession = true;
                    CommonLog.info("Use session result.");
                }
        }
        
        if (!useSession) {
            // Get the nodes that are required to search and accessible to the user
            // domainSearchVec = XXXnode_id,
            // Get all the readable domains
            readableVec = DbKmNodeAccess.getReadableNode(con, usrGroupsList);
            Vector searchNodeVec = new Vector();
            if (node_id_list != null && node_id_list.length > 0) {

                for(int i=0; i<node_id_list.length; i++){
                    searchNodeVec.addAll(ViewKmFolderManager.getAllChildFoldersID(con, node_id_list[i]));
                    searchNodeVec.addElement(new Long(node_id_list[i]));
                }
                Long nodeID;
                // Remove un-readable nodes
                for (int i=0;i<searchNodeVec.size();i++) {
                    nodeID = (Long) searchNodeVec.elementAt(i);
                    if (!readableVec.contains(nodeID)) {
                        searchNodeVec.removeElementAt(i);
                        i--;
                    }
                }
            }else {
                searchNodeVec = readableVec;
            }
            Search se  = new Search(DB_PATH);
            //matchedVec = se.simple_search(obj_type,  query_string, searchNodeVec, SEARCH_RESULT_LIMIT);
            matchedVec = se.adv_search(obj_title, obj_author, obj_type_list, key_words, call_num, searchNodeVec, SEARCH_RESULT_LIMIT);
            if (matchedVec == null) {
                matchedVec = new Vector();
            }
            CommonLog.info("No of objects found = " + matchedVec.size());
            
            if( !flag ) {
                vObj = null;
                for(int i=matchedVec.size() - 1; i>-1; i--){
                    vObj = (ViewKmObject)matchedVec.elementAt(i);
                    if( vObj.dbObject.obj_delete_timestamp != null ){
                        matchedVec.removeElementAt(i);
                    }
                }
            }
            CommonLog.info("No of objects found = " + matchedVec.size());
            objIdVec = new Vector();
            for (int i=0;i<matchedVec.size();i++) {
                vObj = (ViewKmObject) matchedVec.elementAt(i);
                objIdVec.addElement(new Long(vObj.dbObject.obj_bob_nod_id));
            }
            
            DbKmLibraryObjectBorrow kmLibObjBorrow = new DbKmLibraryObjectBorrow();
            DbKmLibraryObject kmLibObj = new DbKmLibraryObject();
            itemUserStatus = kmLibObjBorrow.getItemUserStatus(con, usr_ent_id, objIdVec);            
            itemStatus = kmLibObj.getItemStatus(con, objIdVec);
        }
       
        
        Hashtable domainsHash = new Hashtable();
        Vector domainsVec = new Vector();
        Hashtable ancestorXMLHash = null;
        Vector publishedVec = null;
        Long objID = null;
        
        // Get the distinct domains of all those objects
        if (matchedVec.size() > 0) {
            // Get the objects' id of all the matched objects
            for (int i=0;i<matchedVec.size();i++) {
                vObj = (ViewKmObject) matchedVec.elementAt(i);
                objIdVec.addElement(new Long(vObj.dbObject.obj_bob_nod_id));
            }
            ViewKmFolderManager.getPublishedDomains(con, objIdVec, domainsHash, domainsVec);
            // Get the readable domains from the list
            if (readableVec == null) {
                readableVec = DbKmNodeAccess.getReadableNode(con, domainsVec, usrGroupsList);
            }
            ancestorXMLHash = ViewKmFolderManager.getFolderAncestorAsXML(con, domainsVec, true);

            // Rebuild not readable domain(s) of the object
            boolean bReadable = false;
            for (int i=0;i<matchedVec.size();i++) {
                vObj = (ViewKmObject) matchedVec.elementAt(i);
                objID = new Long(vObj.dbObject.obj_bob_nod_id);
                publishedVec = (Vector) domainsHash.get(objID);
                if (publishedVec != null) {
                    bReadable = false;
                    for(int j=0;j<publishedVec.size();j++) {
                        if (!readableVec.contains((Long) publishedVec.elementAt(j))) {
                            // Remove the unreadable domains of the object
                            publishedVec.removeElementAt(j);
                            j--;
                        }
                    }
                    domainsHash.put(objID, publishedVec);
                }
            }

        }
        
        
        

        if (!useSession) {
            cwPage.ts = cwSQL.getTime(con);
            sess.setAttribute(KMLibraryModule.SESS_KM_CHILD_OBJECTS_TS, cwPage.ts);
            sess.setAttribute(KMLibraryModule.SESS_KM_CHILD_OBJECTS_VEC, matchedVec);
            sess.setAttribute(KMLibraryModule.SESS_KM_SEARCH_ITEM_USER_STATUS, itemUserStatus);
            sess.setAttribute(KMLibraryModule.SESS_KM_SEARCH_ITEM_STATUS, itemStatus);
        }
        
        cwPage.totalRec  = matchedVec.size();
        cwPage.totalPage = (int)Math.ceil((float)cwPage.totalRec/(float)cwPage.pageSize);
        int begin=(cwPage.curPage-1)*cwPage.pageSize;
        int end=begin+cwPage.pageSize;
        if(end>matchedVec.size()) {
            end = matchedVec.size();
        }

        StringBuffer xml = new StringBuffer();
        xml.append(cwPage.asXML());
            
        DbKmLibraryObjectBorrow dbKmLibObjBorrow = new DbKmLibraryObjectBorrow();
        xml.append(dbKmLibObjBorrow.getUserSummaryAsXML(con, usr_ent_id));
       
        
        xml.append("<object_list>");
        Vector ancestorXMLVec = null;
        DbKmLibraryObject dbKmLibObj = null;
        for (int i=begin;i<end;i++) {
            vObj = (ViewKmObject) matchedVec.elementAt(i);
            dbKmLibObj = (DbKmLibraryObject)itemStatus.get(new Long(vObj.dbObject.obj_bob_nod_id));
            dbKmLibObjBorrow = (DbKmLibraryObjectBorrow)itemUserStatus.get(new Long(vObj.dbObject.obj_bob_nod_id));
            xml.append("<object ")
               .append(" nod_id=\"").append(vObj.dbObject.obj_bob_nod_id).append("\" ")               
               .append(" type=\"").append(cwUtils.esc4XML(vObj.dbObject.obj_type)).append("\" ")               
               .append("  author=\"").append(cwUtils.esc4XML(vObj.dbObject.obj_author)).append("\"  ")

               .append(" call_num=\"").append(cwUtils.esc4XML(vObj.dbObject.obj_code)).append("\" ")
               .append(" title=\"").append(cwUtils.esc4XML(vObj.dbObject.obj_title)).append("\" ")
               .append(" num_reserve=\"").append(DbKmLibraryObjectBorrow.getLibraryBorrowStatusCount(con, vObj.dbObject.obj_bob_nod_id, DbKmLibraryObjectBorrow.STATUS_RESERVE)).append("\" ");
            if( dbKmLibObjBorrow != null ) {               
               xml.append(" lob_id=\"").append(dbKmLibObjBorrow.lob_id).append("\" ")
               .append(" renewal=\"").append(dbKmLibObjBorrow.lob_renew_no).append("\" ")
               .append(" status=\"").append(dbKmLibObjBorrow.lob_status).append("\" ");
            }
            if( dbKmLibObj != null ) {
               xml.append(" num_copy=\"").append(dbKmLibObj.lio_num_copy).append("\" ")
               .append(" avail_copy=\"").append(dbKmLibObj.lio_num_copy_available).append("\" ");
            }
            xml.append(" />");

        }
        xml.append("</object_list>");
        return xml.toString();

    }
    
    
}