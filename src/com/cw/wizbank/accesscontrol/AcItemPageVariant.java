package com.cw.wizbank.accesscontrol;


import java.sql.*;
import java.util.Vector;
import com.cw.wizbank.ae.aeItem;
/*
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeTreeNode;
import com.cw.wizbank.ae.aeItemRelation;
import com.cw.wizbank.ae.aeItemAccess;
*/
public class AcItemPageVariant extends AcInstance {
    public AcItemPageVariant(Connection con) {
        super(con);
    }

    public Vector getItemPageVariant(String itm_approval_status, boolean run_ind, boolean isAssistant) throws SQLException{
        Vector vtAuth = new Vector(); 
        String sql = "SELECT ipv_name FROM acItemPageVariant WHERE ipv_itm_approval_status = ? AND ipv_run_ind = ? AND ipv_mgt_assistant_ind = ? AND ipv_auth_ind = ? ";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, itm_approval_status);
        stmt.setBoolean(2, run_ind);
        stmt.setBoolean(3, isAssistant);
        stmt.setBoolean(4, true);
        
        String ipv_name;
        ResultSet rs = stmt.executeQuery();
        while (rs.next()){
            ipv_name = rs.getString("ipv_name");
            vtAuth.addElement(ipv_name);
        }
        stmt.close();
        return vtAuth;
    }
    
//    public boolean hasMakeApprovalActnPrivilege(aeItem itm, String action, long ent_id, String rol_ext_id) throws SQLException{
//        boolean result = false;
//        if(itm.itm_approval_status != null && !itm.itm_session_ind && !itm.itm_run_ind){
//            boolean isAssistant = hasFunctionPrivilege(ent_id, rol_ext_id, AcItem.FTN_ITM_MGT_ASSISTANT);
//            if (action.equalsIgnoreCase(aeItem.ITM_APPROVAL_ACTION_REQ_APPR)){
//                if (isAssistant){
//                    if (itm.itm_approval_status.equalsIgnoreCase(aeItem.ITM_APPROVAL_STATUS_PREAPPROVE) 
//                        || itm.itm_approval_status.equalsIgnoreCase(aeItem.ITM_APPROVAL_STATUS_APPROVED_OFF)){
//                        result = true;    
//                    }
//                }
//            }else if (action.equalsIgnoreCase(aeItem.ITM_APPROVAL_ACTION_CANCEL_REQ_APPR)){
//                if (isAssistant){
//                    if (itm.itm_approval_status.equalsIgnoreCase(aeItem.ITM_APPROVAL_STATUS_PENDING_APPROVAL) 
//                    || itm.itm_approval_status.equalsIgnoreCase(aeItem.ITM_APPROVAL_STATUS_PENDING_REAPPROVAL) ){
//                        result = true;    
//                    }
//                }
//            }else if (action.equalsIgnoreCase(aeItem.ITM_APPROVAL_ACTION_APPR_PUB)){
//                if (!isAssistant){
//                    if (itm.itm_approval_status.equalsIgnoreCase(aeItem.ITM_APPROVAL_STATUS_PENDING_APPROVAL) 
//                    || itm.itm_approval_status.equalsIgnoreCase(aeItem.ITM_APPROVAL_STATUS_PENDING_REAPPROVAL) ){
//                        result = true;    
//                    }
//                }
//            }else if (action.equalsIgnoreCase(aeItem.ITM_APPROVAL_ACTION_DECLINE_APPR_PUB)){
//                if (!isAssistant){
//                    if (itm.itm_approval_status.equalsIgnoreCase(aeItem.ITM_APPROVAL_STATUS_PENDING_APPROVAL) 
//                    || itm.itm_approval_status.equalsIgnoreCase(aeItem.ITM_APPROVAL_STATUS_PENDING_REAPPROVAL) ){
//                        result = true;    
//                    }
//                }
//            }else if (action.equalsIgnoreCase(aeItem.ITM_APPROVAL_ACTION_PUB)){
//                if (!isAssistant){
//                    if (itm.itm_approval_status.equalsIgnoreCase(aeItem.ITM_APPROVAL_STATUS_APPROVED_OFF) ){
//                        result = true;    
//                    }
//                }
//            }else if (action.equalsIgnoreCase(aeItem.ITM_APPROVAL_ACTION_UNPUB)){
//                if (!isAssistant){
//                    if (itm.itm_approval_status.equalsIgnoreCase(aeItem.ITM_APPROVAL_STATUS_APPROVED) ){
//                        result = true;    
//                    }
//                }
//            }
//        }
//        return result;
//    }
}