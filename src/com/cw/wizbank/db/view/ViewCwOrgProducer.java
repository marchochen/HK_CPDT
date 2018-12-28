/**
 * <p>Title:ViewCwOrgProducer</p>
 * <p>Description:core version 4.0.22b</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Cyberwisdom.net</p>
 * @version 4.0.22b
 */
package com.cw.wizbank.db.view;

import java.sql.*;
import java.util.*;

import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.qdb.*;
import com.cw.wizbank.ae.*;
import com.cw.wizbank.accesscontrol.*;
import com.cw.wizbank.db.sql.SqlStatements;
import com.cwn.wizbank.utils.CommonLog;

public class ViewCwOrgProducer{
	private Hashtable htNewRoleID;
	private Hashtable htNewRoleExtID;
   	Timestamp curTime  = null;   	    
                                                    
	public ViewCwOrgProducer(Connection con, Hashtable htNewRoleID, Hashtable htNewRoleExtID) {
		this.htNewRoleID = htNewRoleID;
		this.htNewRoleExtID = htNewRoleExtID;
   	    try {
            curTime = dbUtils.getTime(con);
        } catch (qdbException e) {
        }
   	}
   	public long get_src_ent_id(Connection con, String src_ste_id) throws SQLException, cwException {
   	    long ste_ent_id = 0;
   	    
   	    // get the src entity id
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_site_ent_id); 
                
        stmt.setString(1, src_ste_id);
        ResultSet rs = stmt.executeQuery();
            
        if(rs.next()) {
            ste_ent_id = rs.getLong("ste_ent_id");
        }
            
        stmt.close();
        
        return ste_ent_id;
   	}
   	
	public long copyRootNode(Connection con, long ste_ent_id, String target_ste_id, String target_ste_name)
		throws SQLException, cwException {
   	    long new_ste_ent_id = 0;
   	    
   	    PreparedStatement stmt = null;
   	    ResultSet rs = null;
   	    
        // clone the entity record
        stmt = con.prepareStatement(SqlStatements.sql_clone_entity);
                
        stmt.setLong(1, ste_ent_id);
                
		if (stmt.executeUpdate() != 1) {
            con.rollback();
            throw new cwException("Failed to clone the root node entity.");
        }
            
        stmt.close();

        // get the newly created entity id
        stmt = con.prepareStatement(SqlStatements.sql_get_latest_ent_id); 
                
        rs = stmt.executeQuery();
            
        if(rs.next()) {
            new_ste_ent_id = rs.getLong(1);
        }
            
        stmt.close();
        
        // clone the acSite record
		String sql_clone_acsite =
			"Insert into acSite (ste_ent_id, ste_name, ste_join_datetime, ste_status, ste_domain, ste_login_url, " +
								"ste_ird_id, ste_trusted, ste_max_users, ste_eff_start_date, ste_eff_end_date, ste_max_login_trial, " +
								"ste_cov_syn_datetime, ste_default_sys_ent_id, ste_targeted_entity_lst, " +
								"ste_rsv_min_gap, ste_rsv_min_len, ste_rsv_link, " +
								"ste_lan_xml, ste_guest_ent_id, ste_ctl_4, " +
								"ste_target_by_peer_ind, ste_ldap_host, ste_ldap_dn, " +
								"ste_appr_staff_role, ste_usr_pwd_valid_period, ste_qr_mod_types, ste_id) "
				+ "Select "
				+ new_ste_ent_id
				+ ", '"
				+ target_ste_name
				+ "', ste_join_datetime, "
                                + "ste_status, ste_domain, ste_login_url, "
                                + "ste_ird_id, ste_trusted, ste_max_users, "
                                + "ste_eff_start_date, ste_eff_end_date, ste_max_login_trial, "
                                + "ste_cov_syn_datetime, ste_default_sys_ent_id, ste_targeted_entity_lst, "
                                + "ste_rsv_min_gap, ste_rsv_min_len, ste_rsv_link, "
                                + "ste_lan_xml, ste_guest_ent_id, ste_ctl_4, "
                                + "ste_target_by_peer_ind, ste_ldap_host, ste_ldap_dn, "
                                + "ste_appr_staff_role, ste_usr_pwd_valid_period, ste_qr_mod_types, "
				+ "'"
				+ target_ste_id
				+ "' "
                                + "From acSite "
                                + "Where ste_ent_id = ?";
                                
        stmt = con.prepareStatement(sql_clone_acsite);
                
        stmt.setLong(1, ste_ent_id);
                
		if (stmt.executeUpdate() != 1) {
            con.rollback();
            throw new cwException("Failed to clone acSite.");
        }
            
        stmt.close();
                                
        // clone the UserGroup record
		String sql_clone_usergroup =
			"Insert into UserGroup (usg_ent_id, usg_code, usg_name, usg_display_bil, usg_level, usg_usr_id_admin, " +
				"usg_role, usg_ent_id_root, usg_subg_code, usg_comment_1, usg_comment_2, usg_comment_3, " +
				"usg_comment_4, usg_status, usg_usr_cnt, usg_budget, usg_desc)"
				+ "Select "
				+ new_ste_ent_id
				+ ", usg_code, usg_name, "
                                + "usg_display_bil, usg_level, usg_usr_id_admin, "
                                + "usg_role, usg_ent_id_root, usg_subg_code, "
                                + "usg_comment_1, usg_comment_2, usg_comment_3, "
                                + "usg_comment_4, usg_status, usg_usr_cnt, "
                                + "usg_budget, usg_desc "
                                + "From UserGroup "
                                + "Where usg_ent_id = ?";
                                
        stmt = con.prepareStatement(sql_clone_usergroup);
                
        stmt.setLong(1, ste_ent_id);
                
		if (stmt.executeUpdate() != 1) {
            con.rollback();
            throw new cwException("Failed to clone root user group.");
        }
            
        stmt.close();
        
        // clone the syllabus
		String sql_clone_syllabus =
			"Insert into Syllabus (syl_desc, syl_locale, syl_privilege, syl_ent_id_root)"
                                + "Select syl_desc, syl_locale, "
				+ "syl_privilege, "
				+ new_ste_ent_id
				+ " "
                                + "From Syllabus "
                                + "Where syl_ent_id_root = ?";
                                
        stmt = con.prepareStatement(sql_clone_syllabus);
                
        stmt.setLong(1, ste_ent_id);
                
		if (stmt.executeUpdate() != 1) {
            con.rollback();
            throw new cwException("Failed to clone root syllabus.");
        }
            
        stmt.close();

        // get the src syllabus id
        stmt = con.prepareStatement(SqlStatements.sql_get_syl_id); 
        stmt.setLong(1, ste_ent_id);
                
        rs = stmt.executeQuery();
         
        long src_syl_id = 0;
        if(rs.next()) {
            src_syl_id = rs.getLong("syl_id");
        }
            
        stmt.close();

        // get the new syllabus id
        stmt = con.prepareStatement(SqlStatements.sql_get_syl_id); 
        stmt.setLong(1, new_ste_ent_id);
                
        rs = stmt.executeQuery();
         
        long new_syl_id = 0;
        if(rs.next()) {
            new_syl_id = rs.getLong("syl_id");
        }
            
        stmt.close();
        
        // clone the syllabus objective
        /*
        //Objective recycle bin is no longer exist
        long max_obj_id = dbObjective.getMaxId(con) + 1;
		String sql_clone_syllabus_obj =
			"Insert into Objective "
				+ "Select "
				+ max_obj_id
				+ ", "
				+ new_syl_id
				+ ", obj_type, "
                                + "obj_desc, obj_obj_id_parent, obj_title, "
                                + "obj_developer_id, obj_import_xml, obj_ancester "
                                + "From Objective "
                                + "WHERE obj_syl_id = ? " 
                                + "and obj_desc = ? " 
                                + "and obj_type = ? ";
                                
        stmt = con.prepareStatement(sql_clone_syllabus_obj);
                
        stmt.setLong(1, src_syl_id);
        stmt.setString(2,dbObjective.LOST_FOUND);
        stmt.setString(3,dbObjective.OBJ_TYPE_SYS);
                
		if (stmt.executeUpdate() != 1) {
            con.rollback();
            throw new cwException("Failed to clone syllabus objective.");
        }
            
        stmt.close();
        */
        return new_ste_ent_id;
   	}
   	
	public long copyRecycleBin(Connection con, long src_ste_ent_id, long target_ste_ent_id)
		throws SQLException, cwException {
   	    PreparedStatement stmt = null;
   	    ResultSet rs = null;
   	    
   	    long src_recycle_bin_ent_id = 0;
   	    long target_recycle_bin_ent_id = 0;
   	    
   	    try {
            src_recycle_bin_ent_id = dbUserGroup.getLostFoundID(con, src_ste_ent_id) ;
        } catch (qdbException e) {
            throw new cwException(e.toString());
        }
   	    
        // clone the entity record
        stmt = con.prepareStatement(SqlStatements.sql_clone_entity);
                
        stmt.setLong(1, src_recycle_bin_ent_id);
                
		if (stmt.executeUpdate() != 1) {
            con.rollback();
            throw new cwException("Failed to clone the recycle bin entity.");
        }
            
        stmt.close();

        // get the newly created entity id
        stmt = con.prepareStatement(SqlStatements.sql_get_latest_ent_id); 
                
        rs = stmt.executeQuery();
            
        if(rs.next()) {
            target_recycle_bin_ent_id = rs.getLong(1);
        }
            
        stmt.close();

        // clone the UserGroup record
		String sql_clone_usergroup =
			"Insert into UserGroup "
				+ "Select "
				+ target_recycle_bin_ent_id
				+ ", usg_code, usg_name, "
                                + "usg_display_bil, usg_level, usg_usr_id_admin, "
				+ "usg_role, "
				+ target_ste_ent_id
				+ ", usg_subg_code, "
                                + "usg_comment_1, usg_comment_2, usg_comment_3, "
                                + "usg_comment_4, usg_status, usg_usr_cnt, "
                                + "usg_budget, usg_desc "
                                + "From UserGroup "
                                + "Where usg_ent_id = ?";
                                
        stmt = con.prepareStatement(sql_clone_usergroup);
                
        stmt.setLong(1, src_recycle_bin_ent_id);
                
		if (stmt.executeUpdate() != 1) {
            con.rollback();
            throw new cwException("Failed to clone root user group.");
        }
            
        stmt.close();
   	    
   	    return target_recycle_bin_ent_id;
   	}

	public void copyHomepageFtn(Connection con, long src_ste_ent_id, long target_ste_ent_id, String usr_id)
		throws SQLException, cwException {
   	    PreparedStatement stmt = null;
   	    ResultSet rs = null;
   	    
        stmt = con.prepareStatement(SqlStatements.sql_get_acrole); 
                
        stmt.setLong(1, src_ste_ent_id);
        rs = stmt.executeQuery();
            
        while(rs.next()) {
            String rol_ext_id = rs.getString("rol_ext_id");
            long rol_id = rs.getLong("rol_id");
            
            String new_rol_ext_id = replaceStr(rol_ext_id, "_" + src_ste_ent_id, "_" + target_ste_ent_id);

            // get the homepage function list for the user role
            PreparedStatement stmt3 = con.prepareStatement(SqlStatements.sql_get_ac_hom_ftn_ext_id);                     
            stmt3.setString(1, rol_ext_id);
            ResultSet rs2 = stmt3.executeQuery();
            Vector vt_hp_ftn_ext_id = new Vector();
            while(rs2.next()) {
                vt_hp_ftn_ext_id.addElement(rs2.getString(1));
            }            
            stmt3.close();
            
            String[] hp_ftn_ext_id_lst = new String[vt_hp_ftn_ext_id.size()];
            for (int i=0; i<vt_hp_ftn_ext_id.size(); i++) {
                hp_ftn_ext_id_lst[i] = (String)vt_hp_ftn_ext_id.elementAt(i);
            }
            
            // copy the homepage function
//            AcHomePage myAcHomePage = new AcHomePage(con);
//            myAcHomePage.save_rol_ftn(new_rol_ext_id, hp_ftn_ext_id_lst, usr_id);
            
        }
        stmt.close();
   	}
   	
	public void copyUserRole(Connection con, long src_ste_ent_id, long target_ste_ent_id)
		throws SQLException, cwException {
   	    PreparedStatement stmt = null;
   	    PreparedStatement stmt2 = null;
   	    ResultSet rs = null;
   	    ResultSet rs2 = null;
		long new_ent_id = 0;
        stmt = con.prepareStatement(SqlStatements.sql_get_acrole); 
                
        stmt.setLong(1, src_ste_ent_id);
        rs = stmt.executeQuery();

        while(rs.next()) {
            long rol_id = rs.getLong("rol_id");
            
            String operator = cwSQL.getConcatOperator();
            // clone the UserGroup record
			String sql_clone_user_role = "Insert into acRole (rol_ext_id, rol_seq_id, rol_ste_ent_id, rol_url_home, rol_creation_timestamp, " +
									"rol_xml, rol_ste_default_ind, rol_report_ind, " +
									"rol_skin_root, rol_status, rol_ste_uid, " +
									"rol_target_ent_type, rol_auth_level, rol_tc_ind) " 
									+ "Select "
                                    // add some dummy prefix for rol_ext_id
                                    + "'x'" + operator + "rol_ext_id, "
                                    + "rol_seq_id, "
		+ target_ste_ent_id
		+ ", rol_url_home, ?, "
                                    + "rol_xml, rol_ste_default_ind, rol_report_ind, "
                                    + "rol_skin_root, rol_status, rol_ste_uid, "
                                    + "rol_target_ent_type, rol_auth_level, rol_tc_ind "
                                    + "From acRole "
                                    + "Where rol_ste_ent_id = ? and rol_id = ?";
                                    
            stmt2 = con.prepareStatement(sql_clone_user_role);
            stmt2.setTimestamp(1, curTime);
            stmt2.setLong(2, src_ste_ent_id);
            stmt2.setLong(3, rol_id);
                    
			if (stmt2.executeUpdate() < 1) {
                con.rollback();
                throw new cwException("Failed to clone user role.");
            }
                
            stmt2.close();      
            
            // get the latest role id
            long new_rol_id = 0;
            stmt2 = con.prepareStatement(SqlStatements.sql_get_latest_role_id); 
                    
            rs2 = stmt2.executeQuery();
                
            if(rs2.next()) {
                new_rol_id = rs2.getLong(1);
				this.htNewRoleID.put(Long.toString(rol_id), new Long(new_rol_id));
            }
                
            stmt2.close();
            
        }  
        stmt.close();
   	    
        // modify the user role record
        stmt = con.prepareStatement(SqlStatements.sql_get_acrole); 
                
        stmt.setLong(1, target_ste_ent_id);
        rs = stmt.executeQuery();
            
        while(rs.next()) {
            String rol_ext_id = rs.getString("rol_ext_id");
            rol_ext_id = rol_ext_id.substring(1);
            String rol_xml = cwSQL.getClobValue(rs, "rol_xml");
            long rol_id = rs.getLong("rol_id");
            
            AcRoleFunction myAcRoleFunction = new AcRoleFunction(con);
            /*
            Hashtable htRoleFunction = myAcRoleFunction.getRoleFunction(rol_ext_id);
            Vector vt_rol_ftn_ext_id = (Vector)htRoleFunction.get(AcRoleFunction.FTN_EXT_IDS);
            */
            
            // get the role function list for the user role
            stmt2 = con.prepareStatement(SqlStatements.sql_get_role_ftn_ext_id); 
            stmt2.setString(1, rol_ext_id);                    
            rs2 = stmt2.executeQuery();            
            Vector vt_rol_ftn_ext_id = new Vector();
            while (rs2.next()) {
                vt_rol_ftn_ext_id.addElement(rs2.getString("ftn_ext_id"));
            }
            stmt2.close();

            String[] rol_ftn_ext_id_lst = new String[vt_rol_ftn_ext_id.size()];
            for (int i=0; i<vt_rol_ftn_ext_id.size(); i++) {
                rol_ftn_ext_id_lst[i] = (String)vt_rol_ftn_ext_id.elementAt(i);
            }
            
            // start modifying the rol_ext_id
            int index1 = 0;
            index1 = rol_ext_id.indexOf('_');
            String new_rol_ext_id = replaceStr(rol_ext_id, "_" + src_ste_ent_id, "_" + target_ste_ent_id);
            String new_rol_xml = replaceStr(rol_xml, "_" + src_ste_ent_id, "_" + target_ste_ent_id);
            
            stmt2 = con.prepareStatement(SqlStatements.sql_upd_acrole);
                    
            stmt2.setString(1, new_rol_ext_id);
            stmt2.setString(2, new_rol_xml);
            stmt2.setLong(3, rol_id);
                    
			if (stmt2.executeUpdate() != 1) {
                con.rollback();
                throw new cwException("Failed to clone user role.");
            }
            
            stmt2.close();
            
            // copy the role function record
            myAcRoleFunction.saveRoleFunction(new_rol_ext_id, rol_ftn_ext_id_lst);            
        }
            
		stmt = con.prepareStatement(SqlStatements.sql_get_latest_ent_id);
		rs = stmt.executeQuery();
		if (rs.next()) {
			new_ent_id = rs.getLong(1);
        stmt.close();
        
		}
   	}
   	
   	// return the entity id of "Admin"
	public String copySysUser(
		Connection con,
		long src_ste_ent_id,
		long target_ste_ent_id,
		Vector vtSrcEntityID,
		Vector vtNewEntityID)
		throws SQLException, cwException {
   	    PreparedStatement stmt = null;
   	    PreparedStatement stmt2 = null;
   	    PreparedStatement stmt3 = null;
   	    ResultSet rs = null;
   	    ResultSet rs2 = null;
   	    
   	    long guest_ent_id = 0;
   	    String new_admin_usr_id = "";
   	    
   	    // get the guest acount entity id
        stmt = con.prepareStatement(SqlStatements.sql_get_site_guest_ent_id); 
                
        stmt.setLong(1, src_ste_ent_id);
        rs = stmt.executeQuery();
            
        if (rs.next()) {
            guest_ent_id = rs.getLong(1);
        }        
        stmt.close();

   	    // get the guest acount entity id
        stmt = con.prepareStatement(SqlStatements.sql_get_sys_user); 
                
        stmt.setLong(1, src_ste_ent_id);
        stmt.setString(2, "SYS");
        rs = stmt.executeQuery();
            
        while (rs.next()) {
            String usr_ste_usr_id = rs.getString("usr_ste_usr_id");
            long ent_id = rs.getLong("usr_ent_id");
            long new_ent_id = 0;
            
            // clone the entity record
            stmt2 = con.prepareStatement(SqlStatements.sql_clone_entity);
            stmt2.setLong(1, ent_id);
			if (stmt2.executeUpdate() != 1) {
                con.rollback();
                throw new cwException("Failed to clone the sys user entity.");
            }
                
            stmt2.close();
            
            // get the newly created entity id
            stmt2 = con.prepareStatement(SqlStatements.sql_get_latest_ent_id); 
                    
            rs2 = stmt2.executeQuery();
                
            if(rs2.next()) {
                new_ent_id = rs2.getLong(1);
            }
                
            stmt2.close();
            
            // clone the reguser record
		 String sql_clone_reg_user =
				"Insert into RegUser (usr_id, usr_ent_id, usr_pwd, usr_email, usr_email_2, usr_full_name_bil, " +
					"usr_initial_name_bil, usr_last_name_bil, usr_first_name_bil, " +
					"usr_display_bil, usr_gender, usr_bday, usr_hkid, usr_other_id_no, usr_other_id_type, usr_tel_1, " +
					"usr_tel_2, usr_fax_1, usr_country_bil, usr_postal_code_bil, usr_state_bil, usr_address_bil, " +
					"usr_class, usr_class_number, usr_signup_date, usr_last_login_role, usr_last_login_date, usr_status, " +
					"usr_upd_date, usr_ste_ent_id, usr_ste_usr_id, usr_extra_1, usr_cost_center, usr_login_trial, " +
					"usr_login_status, usr_remark_xml, usr_extra_2, usr_extra_3, usr_extra_4, usr_extra_5, " +
					"usr_extra_6, usr_extra_7, usr_extra_8, usr_extra_9, usr_extra_10, usr_approve_usr_id, " +
					"usr_approve_timestamp, usr_approve_reason, usr_pwd_need_change_ind, " +
					"usr_pwd_upd_timestamp, usr_syn_rol_ind, usr_not_syn_gpm_type, " +
					"usr_join_datetime, usr_job_title, usr_app_approval_usg_ent_id, usr_source, usr_nickname ) "
                                    + "Select "
					+ "'s"
					+ target_ste_ent_id
					+ "u"
					+ new_ent_id
					+ "',"
					+ new_ent_id
					+ ", usr_pwd, "
                                    + "usr_email, usr_email_2, usr_full_name_bil, "
                                    + "usr_initial_name_bil, usr_last_name_bil, usr_first_name_bil, "
                                    + "usr_display_bil, usr_gender, usr_bday, usr_hkid, "
                                    + "usr_other_id_no, usr_other_id_type, usr_tel_1, "
                                    + "usr_tel_2, usr_fax_1, usr_country_bil, "
                                    + "usr_postal_code_bil, usr_state_bil, usr_address_bil, "
                                    + "usr_class, usr_class_number, usr_signup_date, "
                                    + "'', usr_last_login_date, usr_status, "
                                    + "usr_upd_date, "
					+ target_ste_ent_id
					+ ", "
                                    + "usr_ste_usr_id, "
                                    + "usr_extra_1, usr_cost_center, usr_login_trial, "
                                    + "usr_login_status, usr_remark_xml, usr_extra_2, "
                                    + "usr_extra_3, usr_extra_4, usr_extra_5, "
                                    + "usr_extra_6, usr_extra_7, usr_extra_8, "
                                    + "usr_extra_9, usr_extra_10, usr_approve_usr_id, "
                                    + "usr_approve_timestamp, usr_approve_reason, "
                                    // usr_pwd_need_change_ind
                                    + "1, "
                                    + "usr_pwd_upd_timestamp, usr_syn_rol_ind, usr_not_syn_gpm_type, "
                                    + "usr_join_datetime, usr_job_title, NULL, usr_source, usr_nickname "
                                    + "From RegUser "
                                    + "Where usr_ent_id = ?";
                                    
            stmt2 = con.prepareStatement(sql_clone_reg_user);
                    
            stmt2.setLong(1, ent_id);
                    
			if (stmt2.executeUpdate() < 1) {
                con.rollback();
                throw new cwException("Failed to clone reguser.");
            }
            stmt2.close();
			final String sql_clone_reg_user_ext =
				"Insert into "
					+ "RegUserExtension(urx_usr_ent_id,urx_extra_datetime_11,urx_extra_datetime_12,urx_extra_datetime_13,urx_extra_datetime_14,urx_extra_datetime_15,"
    					+ "urx_extra_datetime_16,urx_extra_datetime_17,urx_extra_datetime_18,urx_extra_datetime_19,urx_extra_datetime_20,urx_extra_singleoption_21,urx_extra_singleoption_22,"
    					+ "urx_extra_singleoption_23,urx_extra_singleoption_24,urx_extra_singleoption_25,urx_extra_singleoption_26,urx_extra_singleoption_27,urx_extra_singleoption_28,"
    					+ "urx_extra_singleoption_29,urx_extra_singleoption_30,urx_extra_multipleoption_31,urx_extra_multipleoption_32,urx_extra_multipleoption_33,urx_extra_multipleoption_34,"
    					+ "urx_extra_multipleoption_35,urx_extra_multipleoption_36,urx_extra_multipleoption_37,urx_extra_multipleoption_38,"
    					+ "urx_extra_multipleoption_39,urx_extra_multipleoption_40,"
    					+ "urx_extra_41,urx_extra_42,urx_extra_43,urx_extra_44,urx_extra_45) "
                    +"Select "
        			    + new_ent_id +","                        + "urx_extra_datetime_11,urx_extra_datetime_12,urx_extra_datetime_13,urx_extra_datetime_14,urx_extra_datetime_15,"
        				+ "urx_extra_datetime_16,urx_extra_datetime_17,urx_extra_datetime_18,urx_extra_datetime_19,urx_extra_datetime_20,urx_extra_singleoption_21,urx_extra_singleoption_22,"
        				+ "urx_extra_singleoption_23,urx_extra_singleoption_24,urx_extra_singleoption_25,urx_extra_singleoption_26,urx_extra_singleoption_27,urx_extra_singleoption_28,"
        				+ "urx_extra_singleoption_29,urx_extra_singleoption_30,urx_extra_multipleoption_31,urx_extra_multipleoption_32,urx_extra_multipleoption_33,urx_extra_multipleoption_34,"
        				+ "urx_extra_multipleoption_35,urx_extra_multipleoption_36,urx_extra_multipleoption_37,urx_extra_multipleoption_38,"
        				+ "urx_extra_multipleoption_39,urx_extra_multipleoption_40, "
    					+ "urx_extra_41,urx_extra_42,urx_extra_43,urx_extra_44,urx_extra_45 "
                        + "From RegUserExtension "
                        + "Where urx_usr_ent_id = ?";               
            
            stmt2 = con.prepareStatement(sql_clone_reg_user_ext);
            stmt2.setLong(1,ent_id);
            
            if(stmt2.executeUpdate() < 1){
                con.rollback();
                throw new cwException("Failed to cloe RegUserExtension for RegUser.");                
            }
            
            stmt2.close();
            if (usr_ste_usr_id.equalsIgnoreCase("admin")) {
                stmt2 = con.prepareStatement(SqlStatements.sql_get_usr_id);                        
                stmt2.setLong(1, new_ent_id);
                rs2 = stmt2.executeQuery();
                    
                if(rs2.next()) {
                    new_admin_usr_id = rs2.getString(1);
                }
                    
                stmt2.close();                
            }

            // clone the EntityRelation
			final String sql_clone_groupmember =
				"Insert into EntityRelation (ern_ancestor_ent_id, ern_child_ent_id, ern_type, ern_order, ern_parent_ind, "
					+ "ern_syn_timestamp, ern_remain_on_syn, ern_create_usr_id, ern_create_timestamp) "
	                    + " Select "
						+ target_ste_ent_id
						+ ", "
						+ new_ent_id
						+ ", ern_type, ern_order, ern_parent_ind, ern_syn_timestamp, ern_remain_on_syn, '"
						+ new_admin_usr_id
						+ "', ? "
	                    + "From EntityRelation "
						+ "Where ern_child_ent_id = ? and ern_type = ?";
			
            stmt2 = con.prepareStatement(sql_clone_groupmember);
            stmt2.setTimestamp(1, curTime);
            stmt2.setLong(2, ent_id);
            stmt2.setString(3, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
                    
			if (stmt2.executeUpdate() < 1) {
                con.rollback();
                throw new cwException("Failed to clone EntityRelation.");
            }
			stmt2.close();
			//<Christ>
			//get new root UserGrade ent_id
			long src_ent_id_group = 0;
			long new_ent_id_group = 0;
			stmt2 = con.prepareStatement(SqlStatements.sql_get_grade_group_member_frm_member_id);
			stmt2.setLong(1, ent_id);
			stmt2.setString(2, dbEntityRelation.ERN_TYPE_USR_CURRENT_UGR);
			rs2 = stmt2.executeQuery();
			while (rs2.next()) {
				src_ent_id_group = rs2.getLong("ern_ancestor_ent_id");
				for (int i = 0; i < vtNewEntityID.size(); i++) {                
					if (src_ent_id_group == ((Long) vtSrcEntityID.elementAt(i)).longValue()) {
						new_ent_id_group = ((Long) vtNewEntityID.elementAt(i)).longValue();
					}
				}
				String sql_clone_groupmember3 =
					"Insert into EntityRelation (ern_ancestor_ent_id, ern_child_ent_id, ern_type, ern_order, ern_parent_ind, "
					+ "ern_syn_timestamp, ern_remain_on_syn, ern_create_usr_id, ern_create_timestamp) "
	                    + " Select "
						+ new_ent_id_group
						+ ", "
						+ new_ent_id
						+ ", ern_type, ern_order, ern_parent_ind, ern_syn_timestamp, ern_remain_on_syn, '"
						+ new_admin_usr_id
						+ "', ? "
	                    + "From EntityRelation "
						+ "Where ern_child_ent_id = ? and ern_ancestor_ent_id = ? and ern_type = ?";
				stmt3 = con.prepareStatement(sql_clone_groupmember3);
	            stmt3.setTimestamp(1, curTime);
	            stmt3.setLong(2, ent_id);
	            stmt3.setLong(3, src_ent_id_group);
	            stmt3.setString(4, dbEntityRelation.ERN_TYPE_USR_CURRENT_UGR);
				stmt3.executeUpdate();
				stmt3.close();
			}
            stmt2.close();
			//</Christ> end 
            
            // get the role_id
            stmt2 = con.prepareStatement(SqlStatements.sql_get_entity_role_id); 
            stmt2.setLong(1, ent_id);
            rs2 = stmt2.executeQuery();
            long src_rol_id = 0;
            long new_rol_id = 0;
            while (rs2.next()) {
                src_rol_id = rs2.getLong(1);
				if (this.htNewRoleID.get(Long.toString(src_rol_id)) != null) {
					new_rol_id = ((Long) this.htNewRoleID.get(Long.toString(src_rol_id))).longValue();
				} else {
                    continue;
                }
                // clone the acEntityRole
				String sql_clone_entity_role =
					"Insert into acEntityRole (erl_ent_id, erl_rol_id, erl_creation_timestamp, erl_syn_timestamp, erl_eff_start_datetime, erl_eff_end_datetime) "
                                        + "Select "
						+ new_ent_id
						+ ", "
						+ new_rol_id
						+ ", ? , "
                                        + "erl_syn_timestamp, erl_eff_start_datetime, erl_eff_end_datetime "
                                        + "From acEntityRole "
                                        + "Where erl_ent_id = ? and erl_rol_id = ?";
                                        
                stmt3 = con.prepareStatement(sql_clone_entity_role);
	            stmt3.setTimestamp(1, curTime);
                stmt3.setLong(2, ent_id);
                stmt3.setLong(3, src_rol_id);

				if (stmt3.executeUpdate() < 1) {
                    con.rollback();
                    throw new cwException("Failed to clone acEntityRole.");
                }
                    
                stmt3.close();
                
            }
            
            stmt2.close();
            
            // update the guest account info in acSite
            if (usr_ste_usr_id.equalsIgnoreCase("guest")) {
                stmt2 = con.prepareStatement(SqlStatements.sql_upd_acsite_guest_ent_id);
                        
                stmt2.setLong(1, new_ent_id);
                stmt2.setLong(2, target_ste_ent_id);
                        
				if (stmt2.executeUpdate() != 1) {
                    con.rollback();
                    throw new cwException("Failed to clone the sys user entity.");
                }
                    
                stmt2.close();                
            }
			if (usr_ste_usr_id.equalsIgnoreCase("sysadmin")) {
				stmt3 = con.prepareStatement(SqlStatements.sql_upd_acsite_ste_default_sys_ent_id);
				stmt3.setLong(1, new_ent_id);
				stmt3.setLong(2, target_ste_ent_id);

				if (stmt3.executeUpdate() != 1) {
					con.rollback();
					throw new cwException("Failed to clone the sys user entity.");
				}

				stmt3.close();
			}

		}
		if (stmt != null) {
        stmt.close();
		}
		if (stmt3 != null) {
			stmt3.close();
		}
		if (stmt2 != null) {
			stmt2.close();
		}
        return new_admin_usr_id;
   	}
   	
	public void copyUserGrade(
		Connection con,
		long src_ste_ent_id,
		long target_ste_ent_id,
		Vector vtSrcEntityID,
		Vector vtNewEntityID)
		throws SQLException, cwException {
   	    PreparedStatement stmt = null;
   	    PreparedStatement stmt2 = null;
   	    ResultSet rs = null;
   	    ResultSet rs2 = null;
   	    long new_ent_id = 0;
   	    long ent_id = 0;
   	    
   	    // get the user grade entity id
        stmt = con.prepareStatement(SqlStatements.sql_get_user_grade); 
        int index = 1;
        stmt.setLong(index++, src_ste_ent_id);
        stmt.setString(index++, "ROOT");
        stmt.setBoolean(index++, true);
        rs = stmt.executeQuery();
            
        while (rs.next()) {
            ent_id = rs.getLong("ugr_ent_id");
            vtSrcEntityID.addElement(new Long(ent_id));
            // clone the entity record
            stmt2 = con.prepareStatement(SqlStatements.sql_clone_entity);
            stmt2.setLong(1, ent_id);
                    
			if (stmt2.executeUpdate() != 1) {
                con.rollback();
                throw new cwException("Failed to clone the user grade entity.");
            }
            stmt2.close();
            // get the newly created entity id
            stmt2 = con.prepareStatement(SqlStatements.sql_get_latest_ent_id); 
            rs2 = stmt2.executeQuery();
            if(rs2.next()) {
                new_ent_id = rs2.getLong(1);
                vtNewEntityID.addElement(new Long(new_ent_id));                
            }
                
            stmt2.close();            
            
            // clone the user grade record
			String sql_clone_user_grade =
				"Insert into UserGrade (ugr_ent_id, ugr_display_bil, ugr_ent_id_root, ugr_type, ugr_seq_no, ugr_default_ind) "
                                    + "Select "
                                    + new_ent_id 
                                    + ", ugr_display_bil, "
                                    + target_ste_ent_id
                                    + ", ugr_type, ugr_seq_no, ugr_default_ind "
                                    + "From UserGrade "
                                    + "Where ugr_ent_id = ?";
                                    
            stmt2 = con.prepareStatement(sql_clone_user_grade);
                    
            stmt2.setLong(1, ent_id);
                    
			if (stmt2.executeUpdate() < 1) {
                con.rollback();
                throw new cwException("Failed to clone User Grade.");
            }
                
            stmt2.close();
                        
        }        

        stmt.close();

   	    // start copying the group member record
   	    // get the user grade entity id
        stmt = con.prepareStatement(SqlStatements.sql_get_user_grade); 
        
        int index1 = 1;
        stmt.setLong(index1++, src_ste_ent_id);
        stmt.setString(index1++, "ROOT");
        stmt.setBoolean(index1++, true);
        rs = stmt.executeQuery();
            
        while (rs.next()) {
            ent_id = rs.getLong("ugr_ent_id");
            
            long src_ent_id_group = 0;
            long new_ent_id_group = 0;
            long src_ent_id_member = 0;
            long new_ent_id_member = 0;
            
            stmt2 = con.prepareStatement(SqlStatements.sql_get_group_member_frm_member_id); 
            stmt2.setLong(1, ent_id);                    
            rs2 = stmt2.executeQuery();                
            if(rs2.next()) {
                src_ent_id_group = rs2.getLong("ern_ancestor_ent_id");
                src_ent_id_member = rs2.getLong("ern_child_ent_id");
			} else {
                stmt2.close();
                continue;
            }
            stmt2.close();
            
            for (int i=0; i<vtNewEntityID.size(); i++) {
                
                if (src_ent_id_group == ((Long)vtSrcEntityID.elementAt(i)).longValue()) {
                    new_ent_id_group = ((Long)vtNewEntityID.elementAt(i)).longValue();
                }
                if (src_ent_id_member == ((Long)vtSrcEntityID.elementAt(i)).longValue()) {
                    new_ent_id_member = ((Long)vtNewEntityID.elementAt(i)).longValue();
                }
            }
            CommonLog.debug("grade ern_child_ent_id: " + new_ent_id_group);
            CommonLog.debug("grade ern_ancestor_ent_id: " + new_ent_id_member);
            // clone the EntityRelation
			String sql_clone_groupmember =
				"Insert into EntityRelation (ern_ancestor_ent_id, ern_child_ent_id, ern_type, ern_order, ern_parent_ind, "
				+ "ern_syn_timestamp, ern_remain_on_syn, ern_create_usr_id, ern_create_timestamp) "
                    + " Select "
					+ new_ent_id_group
					+ ", "
					+ new_ent_id_member
					+ ", ern_type, ern_order, ern_parent_ind, ern_syn_timestamp, ern_remain_on_syn, ern_create_usr_id, ? "
                    + "From EntityRelation "
					+ "Where ern_child_ent_id = ?";
                                    
            stmt2 = con.prepareStatement(sql_clone_groupmember);
                    
            stmt2.setTimestamp(1, curTime);
            stmt2.setLong(2, ent_id);
            CommonLog.debug("grade sql_clone_groupmember: " + sql_clone_groupmember);
			if (stmt2.executeUpdate() < 1) {
                con.rollback();
                throw new cwException("Failed to clone EntityRelation.");
            }
                
            stmt2.close();
            
        }
        
        stmt.close();
   	    
   	}

	public void copyLearningSolnType(
		Connection con,
		long src_ste_ent_id,
		long target_ste_ent_id,
		String new_admin_usr_id)
		throws SQLException, cwException {
   	    PreparedStatement stmt = null;
   	    PreparedStatement stmt2 = null;
   	    ResultSet rs = null;
   	    ResultSet rs2 = null;
   	    
   	    // get the user role
        stmt = con.prepareStatement(SqlStatements.sql_get_acrole);                 
        stmt.setLong(1, src_ste_ent_id);
        rs = stmt.executeQuery();
        while(rs.next()) {
            String rol_ext_id = rs.getString("rol_ext_id");
            String new_rol_ext_id = replaceStr(rol_ext_id, "_" + src_ste_ent_id, "_" + target_ste_ent_id);
			this.htNewRoleExtID.put(rol_ext_id, new_rol_ext_id);
        }
        stmt.close();
   	    
        // clone aeItemType
		String sql_clone_lrn_soln_type =
			"Insert into aeItemType (ity_owner_ent_id, ity_id, ity_run_ind, ity_seq_id, ity_title_xml, ity_icon_url, ity_create_timestamp, " +
			"ity_create_usr_id, ity_init_life_status, ity_create_run_ind, ity_apply_ind, ity_qdb_ind, ity_auto_enrol_qdb_ind, " +
			"ity_cascade_inherit_col, ity_certificate_ind, ity_session_ind, ity_create_session_ind, ity_has_attendance_ind, ity_ji_ind, " +
			"ity_completion_criteria_ind, ity_can_cancel_ind, ity_target_method, ity_reminder_criteria_ind, ity_tkh_method, " +
			"ity_exam_ind, ity_blend_ind, ity_ref_ind) "
                                + "Select "
                                + target_ste_ent_id
                                + ", ity_id, ity_run_ind, ity_seq_id, "
                                + "ity_title_xml, ity_icon_url, ity_create_timestamp, "
				+ "'"
				+ new_admin_usr_id
				+ "', "
                                + "ity_init_life_status, ity_create_run_ind, "
                                + "ity_apply_ind, ity_qdb_ind, ity_auto_enrol_qdb_ind, "
                                + "ity_cascade_inherit_col, ity_certificate_ind, ity_session_ind, "
                                + "ity_create_session_ind, ity_has_attendance_ind, ity_ji_ind, "
                                + "ity_completion_criteria_ind, ity_can_cancel_ind, ity_target_method, "
                                + "ity_reminder_criteria_ind, ity_tkh_method, "
                                + "ity_exam_ind, ity_blend_ind, ity_ref_ind "
                                + "From aeItemType "
                                + "Where ity_owner_ent_id = ?";
                                        
        stmt = con.prepareStatement(sql_clone_lrn_soln_type);
                        
        stmt.setLong(1, src_ste_ent_id);

		if (stmt.executeUpdate() < 1) {
            con.rollback();
            throw new cwException("Failed to clone Learning Soln Type.");
        }
                    
        stmt.close();
        
   	    // get the user grade entity id
        stmt = con.prepareStatement(SqlStatements.sql_get_aetemplate); 
        stmt.setLong(1, src_ste_ent_id);
        rs = stmt.executeQuery();
        
        long src_tpl_id = 0;
        long new_tpl_id = 0;
        String tpl_xml = "";
        while (rs.next()) {
            src_tpl_id = rs.getLong("tpl_id");
			tpl_xml = cwSQL.getClobValue(rs, "tpl_xml");
            
            // clone aeTemplate

			String sql_clone_aetemplate =
				"Insert into aeTemplate (tpl_ttp_id, tpl_title, tpl_xml, tpl_owner_ent_id, tpl_create_timestamp, " +
				"tpl_create_usr_id, tpl_upd_timestamp, tpl_upd_usr_id, tpl_approval_ind) "
                                    + "Select "
                                    + "tpl_ttp_id, tpl_title, "
                                    + "tpl_xml, "
					+ target_ste_ent_id
					+ ", "
					+ "?, "
					+ "'"
					+ new_admin_usr_id
					+ "', ?, "
					+ "'"
					+ new_admin_usr_id
					+ "', "
                                    + "tpl_approval_ind "
                                    + "From aeTemplate "
                                    + "Where tpl_id = ?";
                                            
            stmt2 = con.prepareStatement(sql_clone_aetemplate, PreparedStatement.RETURN_GENERATED_KEYS);
                            
            stmt2.setTimestamp(1, curTime);
            stmt2.setTimestamp(2, curTime);
            stmt2.setLong(3, src_tpl_id);
            
			if (stmt2.executeUpdate() < 1) {
                con.rollback();
                throw new cwException("Failed to clone aeTemplate.");
            }
			// get the newly created template id
            new_tpl_id = cwSQL.getAutoId(con, stmt2, "aeTemplate", "tpl_id");            
            stmt2.close();
            /*
            String condition = " tpl_id = " + new_tpl_id;
            String tableName = "aeTemplate";
            String[] colName = {"tpl_xml"};
            String[] colValue = {tpl_xml};
            cwSQL.updateClobFields(con, tableName, colName, colValue, condition);
            */

            // clone aeTemplateView
			String sql_clone_aetemplate_view =
				"Insert into aeTemplateView (tvw_tpl_id, tvw_id, tvw_xml, tvw_cat_ind, tvw_target_ind, tvw_cm_ind, " +
				"tvw_mote_ind, tvw_itm_acc_ind, tvw_res_ind, tvw_rsv_ind, tvw_create_timestamp, tvw_create_usr_id, " +
				"tvw_wrk_tpl_ind, tvw_cost_center_ind, tvw_ctb_ind, " +
				"tvw_filesize_ind, tvw_km_domain_ind, tvw_km_published_version_ind, tvw_tcr_ind) "
                                    + "Select "
                                    + new_tpl_id
                                    + ", tvw_id, tvw_xml, "
                                    + "tvw_cat_ind, tvw_target_ind, tvw_cm_ind, "
                                    + "tvw_mote_ind, tvw_itm_acc_ind, tvw_res_ind, "
                                    + "tvw_rsv_ind, "
					+ "?, "
					+ "'"
					+ new_admin_usr_id
					+ "', "
                                    + "tvw_wrk_tpl_ind, tvw_cost_center_ind, tvw_ctb_ind, "
                                    + "tvw_filesize_ind, tvw_km_domain_ind, tvw_km_published_version_ind, tvw_tcr_ind "
                                    + "From aeTemplateView "
                                    + "Where tvw_tpl_id = ?";
                                            
            stmt2 = con.prepareStatement(sql_clone_aetemplate_view);
                            
            stmt2.setTimestamp(1, curTime);
            stmt2.setLong(2, src_tpl_id);
            
            stmt2.executeUpdate();
            stmt2.close();
            
            String tvw_xml = "";
            String tvw_id = "";
            
            // clone aeItemTypeTemplate
			String sql_clone_aetemplate_type_template =
				"Insert into aeItemTypeTemplate (itt_ity_owner_ent_id, itt_ity_id, itt_ttp_id, itt_seq_id, " +
				"itt_tpl_id, itt_run_tpl_ind, itt_create_timestamp, itt_create_usr_id, itt_session_tpl_ind) "
                                    + "Select "
                                    + target_ste_ent_id
                                    + ", itt_ity_id, itt_ttp_id, "
                                    + "itt_seq_id, "
                                    + new_tpl_id//itt_tpl_id
                                    + ", itt_run_tpl_ind, "
					+ "?, "
					+ "'"
					+ new_admin_usr_id
					+ "', "
                                    + "itt_session_tpl_ind "
                                    + "From aeItemTypeTemplate "
                                    + "Where itt_tpl_id = ?";
                                            
            stmt2 = con.prepareStatement(sql_clone_aetemplate_type_template);
                            
            stmt2.setTimestamp(1, curTime);
            stmt2.setLong(2, src_tpl_id);
                            
            stmt2.executeUpdate();
            /*
            if( stmt2.executeUpdate() < 1)
            {
                con.rollback();
                throw new cwException("Failed to clone aeItemTypeTemplate.");
            }
            */
                        
            stmt2.close();
            
        }
        stmt.close();
   	}

	public void copyManagementReport(
		Connection con,
		long src_ste_ent_id,
		long target_ste_ent_id,
		String new_admin_usr_id)
		throws SQLException, cwException {
   	    PreparedStatement stmt = null;
   	    PreparedStatement stmt2 = null;
   	    PreparedStatement stmt3 = null;
   	    ResultSet rs = null;
   	    ResultSet rs2 = null;
   	    ResultSet rs3 = null;
   	    
   	    Hashtable htNewRteID = new Hashtable();
   	    
   	    // get the guest acount entity id
        stmt = con.prepareStatement(SqlStatements.sql_get_report_template);                 
        stmt.setLong(1, src_ste_ent_id);
        rs = stmt.executeQuery();            
        long rte_id = 0;
        String rol_ext_id = "";
        while (rs.next()) {
            rte_id = rs.getLong("rte_id");

            // clone ReportTemplate
			String sql_clone_report_template =
				"Insert into ReportTemplate (rte_title_xml, rte_type, rte_get_xsl, rte_exe_xsl, rte_dl_xsl, " +
				"rte_meta_data_url, rte_seq_no, rte_owner_ent_id, rte_create_usr_id, rte_create_timestamp, " +
				"rte_upd_usr_id, rte_upd_timestamp) "
                                            + "Select "
                                            + "rte_title_xml, rte_type, "
                                            + "rte_get_xsl, rte_exe_xsl, rte_dl_xsl, "
                                            + "rte_meta_data_url, rte_seq_no, "
					+ target_ste_ent_id
					+ ", "
					+ "'"
					+ new_admin_usr_id
					+ "', "
					+ "?, "
					+ "'"
					+ new_admin_usr_id
					+ "', "
                                            + "rte_upd_timestamp "
                                            + "From ReportTemplate "
                                            + "Where rte_id = ?";
                                            
            stmt2 = con.prepareStatement(sql_clone_report_template, PreparedStatement.RETURN_GENERATED_KEYS);
                            
            stmt2.setTimestamp(1, curTime);
            stmt2.setLong(2, rte_id);
                            
			if (stmt2.executeUpdate() < 1) {
                con.rollback();
                throw new cwException("Failed to clone ReportTemplate.");
            }
			// get the newly created template id
			long new_rte_id = cwSQL.getAutoId(con, stmt2, "ReportTemplate", "rte_id");
            stmt2.close();
            htNewRteID.put(Long.toString(rte_id), new Long(new_rte_id));
            
            stmt2 = con.prepareStatement(SqlStatements.sql_get_ac_report_template); 
            stmt2.setLong(1, rte_id);                    
            rs2 = stmt2.executeQuery();                
            while(rs2.next()) {
                rol_ext_id = rs2.getString("ac_rte_rol_ext_id");
                String new_rol_ext_id = replaceStr(rol_ext_id, "_" + src_ste_ent_id, "_" + target_ste_ent_id);
                
                // clone acReportTemplate
				String sql_clone_ac_report_template =
					"Insert into acReportTemplate (ac_rte_id, ac_rte_ent_id, ac_rte_rol_ext_id, ac_rte_ftn_ext_id, ac_rte_owner_ind, " +
					"ac_rte_create_usr_id, ac_rte_create_timestamp) "
                                                + "Select "
						+ new_rte_id
						+ ", "
                                                + "ac_rte_ent_id, "
						+ "'"
						+ new_rol_ext_id
						+ "', "
                                                + "ac_rte_ftn_ext_id, "
                                                + "ac_rte_owner_ind, "
						+ "'"
						+ new_admin_usr_id
						+ "', "
						+ "? "
                                                + "From acReportTemplate "
                                                + "Where ac_rte_id = ? and ac_rte_rol_ext_id = ?";
                                                
                stmt3 = con.prepareStatement(sql_clone_ac_report_template);
                                
                stmt3.setTimestamp(1, curTime);
                stmt3.setLong(2, rte_id);
                stmt3.setString(3, rol_ext_id);
                                
				if (stmt3.executeUpdate() < 1) {
                    con.rollback();
                    throw new cwException("Failed to clone AcReportTemplate.");
                }
                            
                stmt3.close();                
                
            }                
            stmt2.close();            
                      
        }
        stmt.close();
        
        // clone ObjectView
		String sql_clone_object_view =
			"Insert into ObjectView (ojv_owner_ent_id, ojv_type, ojv_subtype, ojv_option_xml, ojv_create_usr_id, " +
			"ojv_create_timestamp, ojv_update_usr_id, ojv_update_timestamp) "
                                        + "Select "
				+ target_ste_ent_id
				+ ", "
                                        + "ojv_type, ojv_subtype, "
                                        + "ojv_option_xml, "
				+ "'"
				+ new_admin_usr_id
				+ "', "
				+ "?, "
				+ "'"
				+ new_admin_usr_id
				+ "', "
				+ "? "
                                        + "From ObjectView "
                                        + "Where ojv_owner_ent_id = ?";
                                                
        stmt = con.prepareStatement(sql_clone_object_view);
                                
        stmt.setTimestamp(1, curTime);
        stmt.setTimestamp(2, curTime);
        stmt.setLong(3, src_ste_ent_id);
        stmt.executeUpdate();
                                
        stmt.close();

        String condition = "";
        boolean flag = false;
        for (Enumeration e = htNewRteID.keys() ; e.hasMoreElements() ;) {
            flag = true;
            String src_rte_id = (String)e.nextElement();
            condition += src_rte_id + ",";
        }
        if (flag) {
            condition = "(" + condition.substring(0, condition.length()-1);
            condition += ")";            
		} else {
            condition = "";
        }

        // get the newly created template id
        stmt = con.prepareStatement(SqlStatements.sql_get_report_spec + condition);
                    
        rs = stmt.executeQuery();
                
        while(rs.next()) {
            long rsp_id = rs.getLong("rsp_id");
            long rsp_rte_id = rs.getLong("rsp_rte_id");
            long new_rsp_rte_id = ((Long)htNewRteID.get(Long.toString(rsp_rte_id))).longValue();

            // clone ReportSpec
			String sql_clone_report_spec =
				"Insert into ReportSpec (rsp_rte_id, rsp_ent_id, rsp_title, rsp_xml, rsp_create_usr_id, rsp_create_timestamp, " +
				"rsp_upd_usr_id, rsp_upd_timestamp) "
                                            + "Select "
					+ new_rsp_rte_id
					+ ", "
                                            + "rsp_ent_id, "
                                            + "rsp_title, rsp_xml, "
					+ "'"
					+ new_admin_usr_id
					+ "', "
					+ "?, "
					+ "'"
					+ new_admin_usr_id
					+ "', "
					+ "? "
                                            + "From ReportSpec "
                                            + "Where rsp_id = ?";
                                                    
            stmt2 = con.prepareStatement(sql_clone_report_spec);
                                    
            stmt2.setTimestamp(1, curTime);
            stmt2.setTimestamp(2, curTime);
            stmt2.setLong(3, rsp_id);
            stmt2.executeUpdate();
                                    
            stmt2.close();
            
        }
                
        stmt.close();        
	}

	class fmType {
		long ftp_id;
		long parent_ftp_id;
	}
	
	/**
	 * copy related other related tables
	 * e-mail:qjqyx@avl.com.cn
	 * @author:Christ Qiu
	 * @param:sourceDir: source organization directory
	 * @param:targetDir: directory for new organization
	 * @param:htNewRoleExtID:rol_ext_id of new organization
	 */
	//<Christ>
	public void copyOtherRelatedTab(Connection con, long src_ste_ent_id, long target_ste_ent_id)
		throws SQLException, cwException {
		//copy aeAttendanceStatus
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		PreparedStatement stmt3 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;
		stmt2 =
			con.prepareStatement(
				"Insert into aeAttendanceStatus (ats_title_xml, ats_type, ats_attend_ind,ats_default_ind,"
					+ "ats_ent_id_root ,ats_cov_status)"
					+ " select ats_title_xml, ats_type, ats_attend_ind, ats_default_ind ,"
					+ target_ste_ent_id
					+ ", ats_cov_status from aeAttendanceStatus"
					+ " where ats_ent_id_root=? order by ats_id ");
		stmt2.setLong(1, src_ste_ent_id);
		stmt2.executeUpdate();
		stmt2.close();
		CommonLog.debug("Finish copying aeAttendanceStatus.");

		//copy TimeSlot
		stmt2 =
			con.prepareStatement(
				"Insert into fmTimeSlot (tsl_id, tsl_owner_ent_id, tsl_start_time, tsl_end_time) "
					+ "select  tsl_id , "
					+ target_ste_ent_id
					+ ", tsl_start_time, tsl_end_time "
					+ "from fmTimeSlot where tsl_owner_ent_id = ?");
		stmt2.setLong(1, src_ste_ent_id);
		stmt2.executeUpdate();
		stmt2.close();
		CommonLog.debug("Finish copying fmTimeSlot.");

		//copy fmFacilityType

		Vector vt_SrcFtpParentID = new Vector();
		Vector vt_NewFtpParentID = new Vector();

		long old_ftp_id = 0;
		long Curftp_id = 0;
		stmt3 =
			con.prepareStatement(
				"select ftp_id , ftp_parent_ftp_id from fmFacilityType "
					+ "where ftp_owner_ent_id = ? order by ftp_parent_ftp_id ,ftp_id");
		stmt3.setLong(1, src_ste_ent_id);
		rs3 = stmt3.executeQuery();
		
		Vector fmt_list = getFmFacilityTypeList(con, src_ste_ent_id);
		
		//produce new ftp_id
		Curftp_id = getMax_FtpId(con) + 1;
		long NewFtpParentID = 0;
		
		Iterator iter = fmt_list.iterator();
		while (iter.hasNext()) {
			fmType ft = (fmType)iter.next();
			old_ftp_id = ft.ftp_id;
			long SrcFtpParentID = ft.parent_ftp_id;
			if (SrcFtpParentID != 0) {
				boolean founded = false;
				for (int i = 0; i < vt_SrcFtpParentID.size(); i++) {
					if (SrcFtpParentID == ((Long) vt_SrcFtpParentID.get(i)).longValue()) {
						NewFtpParentID = ((Long) vt_NewFtpParentID.get(i)).longValue();
						founded = true;
					}
				}
				if (!founded) {
					throw new cwException("SrcFtpParentID cannot be found:" + SrcFtpParentID);
				}
				stmt2 =
					con.prepareStatement(
						"Insert into fmFacilityType (ftp_id, ftp_title_xml, ftp_main_indc, ftp_class_name ,"
							+ " ftp_xsl_prefix ,ftp_parent_ftp_id, ftp_owner_ent_id )"
							+ "select "
							+ Curftp_id
							+ ",ftp_title_xml, ftp_main_indc, ftp_class_name , ftp_xsl_prefix ,"
							+ NewFtpParentID
							+ ", "
							+ target_ste_ent_id
							+ " from fmFacilityType where ftp_id=?");
			} else {
				vt_SrcFtpParentID.addElement(new Long(old_ftp_id));
				vt_NewFtpParentID.addElement(new Long(Curftp_id));
				stmt2 =
					con.prepareStatement(
						"Insert into fmFacilityType (ftp_id, ftp_title_xml, ftp_main_indc, ftp_class_name ,"
							+ " ftp_xsl_prefix ,ftp_parent_ftp_id, ftp_owner_ent_id )"
							+ "select "
							+ Curftp_id
							+ ",ftp_title_xml, ftp_main_indc, ftp_class_name ,"
							+ " ftp_xsl_prefix ,ftp_parent_ftp_id,"
							+ target_ste_ent_id
							+ " from fmFacilityType where ftp_id=?");
			}
			//			stmt2.setLong(1, src_ste_ent_id);
			stmt2.setLong(1, old_ftp_id);
			if (stmt2.executeUpdate() != 1) {
				con.rollback();
				throw new cwException("Failed to clone fmFacilityType.");

			}

			stmt2.close();
			Curftp_id = Curftp_id + 1;
			//	loop ++;
		}
		stmt3.close();
		CommonLog.debug("Finish copying fmFacilityType.");
	}
	//</Christ>

	private Vector getFmFacilityTypeList(Connection con, long src_ste_ent_id) throws SQLException {
		String get_parent = "select ftp_id , ftp_parent_ftp_id from fmFacilityType "
			+ "where ftp_owner_ent_id = ? and ftp_parent_ftp_id is null";
		
		String get_child = "select ftp_id , ftp_parent_ftp_id from fmFacilityType "
			+ "where ftp_owner_ent_id = ? and ftp_parent_ftp_id is not null order by ftp_parent_ftp_id ,ftp_id";
		
		Vector fmTypeLst = new Vector();
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(get_parent);
			stmt.setLong(1, src_ste_ent_id);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				fmType ft = new fmType();
				ft.ftp_id = rs.getLong("ftp_id");
				ft.parent_ftp_id = rs.getLong("ftp_parent_ftp_id");
				fmTypeLst.addElement(ft);
			}
			//get child ftp
			stmt = con.prepareStatement(get_child);
			stmt.setLong(1, src_ste_ent_id);
			rs = stmt.executeQuery();
			while (rs.next()) {
				fmType ft = new fmType();
				ft.ftp_id = rs.getLong("ftp_id");
				ft.parent_ftp_id = rs.getLong("ftp_parent_ftp_id");
				fmTypeLst.addElement(ft);
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return fmTypeLst;
	}
	
	//<Christ> 
	public static long getMax_tlsId(Connection con) throws SQLException {
		PreparedStatement stmt = con.prepareStatement(" SELECT MAX(tsl_id) FROM fmTimeSlot ");

		long max_tls_id = 0;
		ResultSet rs = stmt.executeQuery();

		if (rs.next()) {
			max_tls_id = rs.getLong(1);
		}
		stmt.close();
		return max_tls_id;
	}

	public static long getMax_FtpId(Connection con) throws SQLException {
		PreparedStatement stmt = con.prepareStatement(" SELECT MAX(ftp_id) FROM fmFacilityType ");

		long max_ftp_id = 0;
		ResultSet rs = stmt.executeQuery();

		if (rs.next()) {
			max_ftp_id = rs.getLong(1);
		}
		stmt.close();
		return max_ftp_id;
	}
	//</Christ>	 

	public static String replaceStr(String inStr, String src_pattern, String target_pattern) {
        String result = "";
   	    while (inStr.indexOf(src_pattern) != -1) {
   	        int index = inStr.indexOf(src_pattern);
   	        result += inStr.substring(0, index) + target_pattern;
   	        inStr = inStr.substring(index + src_pattern.length());
   	    }
   	    
   	    return result+inStr;
   	}
   	
}