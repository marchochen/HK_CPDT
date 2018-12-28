package com.cw.wizbank.course;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.qdb.dbModule;

public class ModulePrerequisiteManagement {
	
	
	public static final String SING_VIEW = "I";

    public static final String SING_COMPLETE = "C";

    public static final String SING_PASS = "P";

    public static final String SING_FAIL = "F";
	
	 public String getItemModLstAsXML(Connection con, aeItem itm)
            throws SQLException, IOException, cwException {
        StringBuffer xml = new StringBuffer();
        xml.append("<prerequisite_module>");
        xml.append("<item id=\"" + itm.itm_id + "\"/>");
        if ((itm.itm_create_run_ind && itm.itm_content_def != null && itm.itm_content_def.equalsIgnoreCase(aeItem.PARENT))|| // if class room course and parent module
                (!itm.itm_create_run_ind && !itm.itm_run_ind && itm.itm_apply_ind)|| // if online course
                (!itm.itm_create_run_ind && itm.itm_run_ind && itm.itm_apply_ind && itm.itm_content_def != null && itm.itm_content_def.equalsIgnoreCase(aeItem.CHILD))// if class and child
                                                        // module
        ) {
            xml.append("<editable>true</editable>");
        } else {
            xml.append("<editable>false</editable>");
        }
        xml.append("<mod_list>");
        // add module
        List mod_lst = getModList(con, itm.itm_id);
        Iterator it = mod_lst.iterator();
        while (it.hasNext()) {
            List mod_content = (List) it.next();
            long mod_id = ((Long) mod_content.get(0)).longValue();
            String mod_type = (String) mod_content.get(1);
            String mod_title = (String) mod_content.get(2);
            String mod_required_time = (String) mod_content.get(3);
            xml.append("<module id=\"" + mod_id + "\">");
            xml.append("<title>").append(dbUtils.esc4XML(mod_title)).append("</title>");
            xml.append("<type>").append(dbUtils.esc4XML(mod_type.toLowerCase())).append("</type>");
            xml.append("<duration>").append(dbUtils.esc4XML(mod_required_time)).append("</duration>");
            // xml.append("<type2low>").append(dbUtils.esc4XML(mod_type.toLowerCase())).append("</type2low>");
            xml.append(getPreStatus(mod_type));
            xml.append(getPreModXML(con, mod_id));
            xml.append("</module>");

        }
        xml.append("</mod_list>");
        xml.append("<mod_structure>");
        xml.append(getModStructure(con, itm.itm_id));
        xml.append("</mod_structure>");
        xml.append("</prerequisite_module>");
        return xml.toString();

    }
     
	 public void upd(Connection con, List mod_id_list, List pre_module_id_list, List pre_module_status_list, long itm_id, String create_usr_id)throws SQLException, cwException, cwSysMessage {
        updateRecord(con, mod_id_list, pre_module_id_list, pre_module_status_list, create_usr_id);
        aeItem itm = new aeItem();
        itm.itm_id = itm_id;
        itm.get(con);
        if (itm.itm_create_run_ind && itm.itm_content_def != null && itm.itm_content_def.equalsIgnoreCase(aeItem.PARENT)) {
            List child_course_lst = getChildCourse(con, itm_id);
            for (int i = 0; i < child_course_lst.size(); i++) {
                long couse_id = ((Long) child_course_lst.get(i)).longValue();
                List child_mod_id_list = map2Child(con, mod_id_list, couse_id);
                List child_pre_module_id_list = map2Child(con, pre_module_id_list, couse_id);
                updateRecord(con, child_mod_id_list, child_pre_module_id_list, pre_module_status_list, create_usr_id);
            }
        }
    }

	 public boolean delModPrerequisite(Connection con, long mod_id)
            throws SQLException {
        boolean can_del = false;

        String sql_check = "select * from ResourceRequirement where rrq_req_res_id=? or rrq_res_id=?";
        String sql_del = "delete from ResourceRequirement where rrq_res_id=? or "
                + "rrq_res_id in(select mod_res_id from Module where mod_mod_res_id_parent=?)";
        PreparedStatement stmt = con.prepareStatement(sql_check);
        ResultSet rs = null;
        stmt.setLong(1, mod_id);
        stmt.setLong(2, mod_id);
        rs = stmt.executeQuery();
        if (!rs.next()) {
            can_del = true;
        }
        if (can_del) {
            stmt = con.prepareStatement(sql_del);
            stmt.setLong(1, mod_id);
            stmt.setLong(2, mod_id);
            stmt.executeUpdate();
        }
        stmt.close();
        return can_del;
    }
     
     public void copyCosModPrerequisite(Connection con, List child_mod_id_lst, String create_usr_id, Timestamp create_Time) throws SQLException {

        String sql_get_map = "select mod_res_id, mod_mod_res_id_parent from Module where mod_res_id in ";
        StringBuffer temp = new StringBuffer();
        Map parent2child = new HashMap();
        temp.append("(");
        for (int i = 0; i < child_mod_id_lst.size(); i++) {
            temp.append(child_mod_id_lst.get(i) + ",");
        }
        temp.append("0)");
        sql_get_map = sql_get_map + temp;
        PreparedStatement stmt = con.prepareStatement(sql_get_map);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {

            parent2child.put(new Long(rs.getLong("mod_mod_res_id_parent")), new Long(rs.getLong("mod_res_id")));
        }
        String sql_get_record = "select * from ResourceRequirement where rrq_res_id in";
        Iterator it = parent2child.keySet().iterator();
        temp = new StringBuffer();
        temp.append("(");
        while (it.hasNext()) {
            temp.append(it.next() + ",");
        }
        temp.append("0)");
        sql_get_record = sql_get_record + temp;
        stmt = con.prepareStatement(sql_get_record);
        rs = stmt.executeQuery();
        while (rs.next()) {
            long mod_id = ((Long) parent2child.get(new Long(rs
                    .getLong("rrq_res_id")))).longValue();
            long pre_mod_id = ((Long) parent2child.get(new Long(rs.getLong("rrq_req_res_id")))).longValue();
            ini(con, mod_id, pre_mod_id, rs.getString("rrq_status"), create_usr_id, create_Time);
        }
        stmt.close();
    }
     public void delCosModPrerequisite(Connection con, long itm_id)
            throws SQLException {

        StringBuffer temp = new StringBuffer();
        temp.append("(select rcn_res_id_content from Resourcecontent,Course where cos_res_id = rcn_res_id and cos_itm_id = ? )");
        String sql_del = "delete from ResourceRequirement where "
                + "rrq_res_id in(select mod_res_id from Module where mod_mod_res_id_parent in"
                + temp
                + " ) ";
        String sql_del2 = "delete from ResourceRequirement where rrq_res_id in"
                        + temp
                       ;
        PreparedStatement stmt = con.prepareStatement(sql_del);
        stmt.setLong(1, itm_id);
        stmt.executeUpdate();
        stmt = con.prepareStatement(sql_del2);
        stmt.setLong(1, itm_id);
        stmt.executeUpdate();
        stmt.close();
    }

     public boolean hasCompletePreMod(Connection con, long ent_id, long tkh_id,
            long mod_id) throws SQLException, cwException {
        boolean result = true;
        String sql_has_pre = "select * from ResourceRequirement where rrq_res_id=? ";
        PreparedStatement stmt = con.prepareStatement(sql_has_pre);
        stmt.setLong(1, mod_id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            String sql_get_inf = "select rrq_status,mov_status,mod_type from Module,ResourceRequirement,Moduleevaluation where rrq_res_id=? "
                    + " and rrq_req_res_id=mod_res_id and mov_mod_id=rrq_req_res_id and mov_tkh_id=? and mov_ent_id=?";
            stmt = con.prepareStatement(sql_get_inf);
            stmt.setLong(1, mod_id);
            stmt.setLong(2, tkh_id);
            stmt.setLong(3, ent_id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                String pre_status = rs.getString("rrq_status");
                String mov_status = rs.getString("mov_status");
                if (pre_status != null) {
                    if (pre_status.equalsIgnoreCase(SING_VIEW)) {
                        if (mov_status != null && (mov_status.equalsIgnoreCase(SING_VIEW) || mov_status.equalsIgnoreCase(SING_FAIL) || mov_status.equalsIgnoreCase(SING_COMPLETE) || mov_status.equalsIgnoreCase(SING_PASS))) {

                        } else {
                            result = false;
                        }
                    }
                    if (pre_status.equalsIgnoreCase(SING_FAIL)) {
                        if (mov_status != null
                                && (mov_status.equalsIgnoreCase(SING_FAIL) || mov_status.equalsIgnoreCase(SING_COMPLETE) || mov_status.equalsIgnoreCase(SING_PASS))) {

                        } else {
                            result = false;
                        }
                    }
                    if (pre_status.equalsIgnoreCase(SING_COMPLETE)) {
                        if (mov_status != null && (mov_status.equalsIgnoreCase(SING_COMPLETE) || mov_status.equalsIgnoreCase(SING_PASS))) {

                        } else {
                            result = false;
                        }
                    }
                    if (pre_status.equalsIgnoreCase(SING_PASS)) {
                        if (mov_status != null && (mov_status.equalsIgnoreCase(SING_PASS))) {

                        } else {
                            result = false;
                        }
                    }
                }

            } else {
                result = false;
            }
        }
        stmt.close();
        return result;

    }
     
     public boolean hasCompletePreMod(long rrq_req_res_id, String pre_status, String mov_status) throws SQLException, cwException {
         boolean result = true;
         if(rrq_req_res_id > 0){
             if (mov_status != null ) {
                 if (pre_status != null) {
                     if (pre_status.equalsIgnoreCase(SING_VIEW)) {
                         if (mov_status.equalsIgnoreCase(SING_VIEW) || mov_status.equalsIgnoreCase(SING_FAIL) || mov_status.equalsIgnoreCase(SING_COMPLETE) || mov_status.equalsIgnoreCase(SING_PASS)) {

                         } else {
                        	 result = false;
                         }
                     }
                     if (pre_status.equalsIgnoreCase(SING_FAIL)) {
                         if (mov_status.equalsIgnoreCase(SING_FAIL) || mov_status.equalsIgnoreCase(SING_COMPLETE) || mov_status.equalsIgnoreCase(SING_PASS)) {

                         } else {
                        	 result = false;
                         }
                     }
                     if (pre_status.equalsIgnoreCase(SING_COMPLETE)) {
                         if (mov_status.equalsIgnoreCase(SING_COMPLETE) || mov_status.equalsIgnoreCase(SING_PASS)) {

                         } else {
                        	 result = false;
                         }
                     }
                     if (pre_status.equalsIgnoreCase(SING_PASS)) {
                         if ((mov_status.equalsIgnoreCase(SING_PASS))) {

                         } else {
                        	 result = false;
                         }
                     }
                 }
             } else {
                 result = false;
             }
    	 }
         return result;

     }
     public String getPreModXML(Connection con, long mod_id)
            throws SQLException, cwException {
        StringBuffer xml = new StringBuffer();
        String sql = "select * from ResourceRequirement where rrq_res_id=? order by rrq_create_timestamp desc";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setLong(1, mod_id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            xml.append("<pre_module id=\"" + rs.getLong("rrq_req_res_id") + "\">");
            xml.append("<checked_status>").append(rs.getString("rrq_status")).append("</checked_status>");
            xml.append("</pre_module>");
        } else {
            xml.append("<pre_module/>");
        }
        stmt.close();
        return xml.toString();
    }
    public String getPreModXML(long rrq_req_res_id, String rrq_status){
    	StringBuffer xml = new StringBuffer();
    	if(rrq_req_res_id > 0) {
            xml.append("<pre_module id=\"" + rrq_req_res_id + "\">");
            xml.append("<checked_status>").append(rrq_status).append("</checked_status>");
            xml.append("</pre_module>");
    	} else {
    		xml.append("<pre_module/>");
    	}
    	return xml.toString();
    }
    private List getModList(Connection con, long itm_id) throws SQLException, IOException, cwException {
        List mod_lst = new ArrayList();
        String sql = "select mod_required_time, mod_res_id,mod_type,res_title from ResourCecontent, Module,Resources where rcn_res_id_content= mod_res_id and res_id=mod_res_id"
                + " and rcn_res_id in (select distinct cos_res_id from Course where cos_itm_id=?)";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setLong(1, itm_id);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            List mod_content = new ArrayList();
            mod_content.add(new Long(rs.getLong("mod_res_id")));
            mod_content.add(rs.getString("mod_type"));
            mod_content.add(rs.getString("res_title"));
            mod_content.add(rs.getString("mod_required_time"));
            mod_lst.add(mod_content);
        }
        stmt.close();
        return mod_lst;
    }
    private String getPreStatus(String type)throws IOException{
    	StringBuffer xml=new StringBuffer();
    	xml.append("<pre_status>");
    	if(type!=null){
    		if(type.trim().equalsIgnoreCase(dbModule.MOD_TYPE_ASS) || type.trim().equalsIgnoreCase(dbModule.MOD_TYPE_TST)||type.trim().equalsIgnoreCase(dbModule.MOD_TYPE_DXT)){
    			xml.append("<status>").append(SING_VIEW).append("</status >");
    			xml.append("<status is_defaul=\"1\">").append(SING_PASS).append("</status >");
    		}
    		if(type.trim().equalsIgnoreCase(dbModule.MOD_TYPE_SVY)){
    			xml.append("<status is_defaul=\"1\">").append(SING_COMPLETE).append("</status >");
    		}
    		if(type.trim().equalsIgnoreCase(dbModule.MOD_TYPE_RDG)||
    		   type.trim().equalsIgnoreCase(dbModule.MOD_TYPE_REF)||
    		   type.trim().equalsIgnoreCase(dbModule.MOD_TYPE_GLO )||
    		   type.trim().equalsIgnoreCase(dbModule.MOD_TYPE_VOD)||
    		   type.trim().equalsIgnoreCase(dbModule.MOD_TYPE_FOR)||
    		   type.trim().equalsIgnoreCase(dbModule.MOD_TYPE_FAQ)||
    		   type.trim().equalsIgnoreCase(dbModule.MOD_TYPE_CHAT)
    		   ){
    			xml.append("<status is_defaul=\"1\">").append(SING_VIEW).append("</status >");
    		}
    		if(type.trim().equalsIgnoreCase(dbModule.MOD_TYPE_AICC_U)){
    			xml.append("<status>").append(SING_VIEW).append("</status >");
    			xml.append("<status is_defaul=\"1\">").append(SING_PASS).append("</status >");
    			xml.append("<status>").append(SING_COMPLETE).append("</status >");
    		}
    		if(type.trim().equalsIgnoreCase(dbModule.MOD_TYPE_NETG_COK)){
    			xml.append("<status>").append(SING_VIEW).append("</status >");
    			xml.append("<status is_defaul=\"1\">").append(SING_PASS).append("</status >");
    			xml.append("<status>").append(SING_COMPLETE).append("</status >");
    		}
    		if(type.trim().equalsIgnoreCase(dbModule.MOD_TYPE_SCO)){
    			xml.append("<status>").append(SING_VIEW).append("</status >");
    			xml.append("<status is_defaul=\"1\">").append(SING_PASS).append("</status >");
    			xml.append("<status>").append(SING_COMPLETE).append("</status >");
    		}
    	}
    	xml.append("</pre_status>");
    	return xml.toString();
    	
    }
    
    
    
    private void updateRecord(Connection con, List mod_id_list, List pre_module_id_list, List pre_module_status_list, String create_usr_id) throws SQLException, cwException {
        Timestamp curTime = cwSQL.getTime(con);

        String sql_del = "delete from ResourceRequirement where rrq_res_id in ";
        StringBuffer temp = new StringBuffer(sql_del);
        temp.append("(");
        for (int i = 0; i < mod_id_list.size(); i++) {
            temp.append(mod_id_list.get(i)).append(",");
        }
        temp.append("0)");
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(temp.toString());
            stmt.executeUpdate();
        }
        finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        for (int i = 0; i < mod_id_list.size(); i++) {
            long mod_id = Long.parseLong(mod_id_list.get(i).toString());
            if (pre_module_id_list.get(i) != null) {
                long pre_mod_id = Long.parseLong(pre_module_id_list.get(i).toString());
                String pre_status = pre_module_status_list.get(i).toString();
                if (mod_id != pre_mod_id && checkStatus(con, pre_mod_id, pre_status)) {
                    ini(con, mod_id, pre_mod_id, pre_status, create_usr_id, curTime);
                } else {
                    throw new cwException("The module can not be itself prerequisite module！");
                }
            }
        }
    }
    
    private boolean checkStatus(Connection con, long pre_mod_id, String status)
            throws SQLException, cwException {
        boolean result = false;
        String type = null;
        String sql = "select mod_type from Module where mod_res_id = ?";
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(sql);
            stmt.setLong(1, pre_mod_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                type = rs.getString("mod_type");
            } else {
                throw new cwException("The module does not exist:" + pre_mod_id);
            }
            if (type != null && type.length() > 0 && status != null) {
                type = type.toUpperCase();
                status = status.trim();
                if (type.equals(dbModule.MOD_TYPE_TST) 
                        || type.equals(dbModule.MOD_TYPE_DXT) ||type.equals(dbModule.MOD_TYPE_ASS)) {
                    if (status.equals(SING_PASS)
                            || status.equals(SING_VIEW)) {
                        result = true;
                    }
                }
                if (type.equals(dbModule.MOD_TYPE_SVY)) {
                    if (status.equals(SING_COMPLETE)) {
                        result = true;
                    }
                }
                if (type.equals(dbModule.MOD_TYPE_GLO )
                        || type.equals(dbModule.MOD_TYPE_FOR)
                        || type.equals(dbModule.MOD_TYPE_FAQ)
                        || type.equals(dbModule.MOD_TYPE_CHAT)) {
                    if (status.equals(SING_VIEW)) {
                        result = true;
                    }
                }
                if (type.equals(dbModule.MOD_TYPE_AICC_U) || type.equals(dbModule.MOD_TYPE_VOD) || type.equals(dbModule.MOD_TYPE_RDG)
                        || type.equals(dbModule.MOD_TYPE_REF)) {
                    if (status.equals(SING_VIEW)
                            || status.equals(SING_PASS)
                            || status.equals(SING_COMPLETE)) {
                        result = true;
                    }
                }
                if (type.equals(dbModule.MOD_TYPE_NETG_COK)) {
                    if (status.equals(SING_VIEW)
                            || status.equals(SING_PASS)
                            || status.equals(SING_COMPLETE)) {
                        result = true;
                    }
                }
                if (type.equals(dbModule.MOD_TYPE_SCO)) {
                    if (status.equals(SING_VIEW)
                            || status.equals(SING_PASS)
                            || status.equals(SING_COMPLETE)) {
                        result = true;
                    }
                }
            }
            if (!result) {
                throw new cwException("The module " + type + " can not have the status:" + status);
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return result;
    }

    private List getChildCourse(Connection con, long itm_id) throws SQLException {
        String sql = "select cos_res_id from aeItemRelation, Course where ire_parent_itm_id = ? and cos_itm_id = ire_child_itm_id";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setLong(1, itm_id);
        ResultSet rs = stmt.executeQuery();
        List child_lst = new ArrayList();
        while (rs.next()) {
            child_lst.add(new Long(rs.getLong("cos_res_id")));
        }
        stmt.close();
        return child_lst;

    }
    
    private List map2Child(Connection con, List mod_id_list, long couse_id) throws SQLException, cwException {
        String sql = "select mod_res_id from ResourceContent, Module where rcn_res_id = ? and rcn_res_id_content = mod_res_id and mod_mod_res_id_parent = ?";
        PreparedStatement stmt = null;
        List child_mod_id_lst = null;
        try {
            stmt = con.prepareStatement(sql);
            ResultSet rs = null;
            child_mod_id_lst = new ArrayList();
            for (int i = 0; i < mod_id_list.size(); i++) {
                if (mod_id_list.get(i) != null) {
                    long root_mod_id = ((Long) mod_id_list.get(i)).longValue();
                    stmt.setLong(1, couse_id);
                    stmt.setLong(2, root_mod_id);
                    rs = stmt.executeQuery();
                    if (rs.next()) {
                        child_mod_id_lst.add(new Long(rs.getLong("mod_res_id")));
                    } else {
                        throw new cwException("cannot find the child of module:" + root_mod_id);
                    }
                } else {
                    child_mod_id_lst.add(null);
                }
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return child_mod_id_lst;
    }
    
    private String getModStructure(Connection con, long itm_id) throws SQLException, IOException, cwException {
        String struc_xml = "";
        String sql = "select cos_structure_xml from Course where cos_itm_id=?";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setLong(1, itm_id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            struc_xml = cwSQL.getClobValue(rs, "cos_structure_xml");
        }
        stmt.close();
        if (struc_xml == null)
            struc_xml = "";
        return struc_xml;

    }
   
    private void ini(Connection con, long mod_id, long pre_mod_id, String pre_status, String create_usr_id, Timestamp create_Time)  throws SQLException {
        String sql_ind = "insert into ResourceRequirement (rrq_res_id,rrq_req_res_id,rrq_status,rrq_create_usr_id,rrq_create_timestamp) "
                + " values(?,?,?,?,?)";
        PreparedStatement stmt = con.prepareStatement(sql_ind);
        stmt.setLong(1, mod_id);
        stmt.setLong(2, pre_mod_id);
        stmt.setString(3, pre_status);
        stmt.setString(4, create_usr_id);
        stmt.setTimestamp(5, create_Time);
        stmt.executeUpdate();
        stmt.close();
    }

}
