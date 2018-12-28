package com.cw.wizbank.instructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Vector;

import com.cw.wizbank.qdb.dbEntity;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwPagination;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;

public class InstructorDao {
	public static Instructor get(Connection con, long ent_id) throws qdbException, SQLException {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		Instructor instr = null;
		try {
			String iti_type_mark = null;

			String sql = " select iti_type_mark from InstructorInf where iti_ent_id = ? ";
			stmt = con.prepareStatement(sql);
			stmt.setLong(1, ent_id);
			rs = stmt.executeQuery();
			if (rs.next()) {
				iti_type_mark = rs.getString("iti_type_mark");
			}
			cwSQL.cleanUp(rs, stmt);

			if (cwUtils.notEmpty(iti_type_mark) && iti_type_mark.equalsIgnoreCase(Instructor.TYPE_MARK_INT)) {
				sql = " select ";
				sql += " iti_ent_id, usr_display_bil as iti_name, usr_gender as iti_gender, usr_bday as iti_bday, usr_tel_1 as iti_mobile, ";
				sql += " usr_email as iti_email, urx_extra_43 as iti_img, iti_introduction, iti_level, iti_cos_type, iti_main_course, ";
				sql += " iti_type, iti_property, iti_highest_educational, iti_graduate_institutions, iti_address, ";
				sql += " iti_work_experience, iti_education_experience, iti_training_experience, ";
				sql += " iti_expertise_areas, iti_good_industry, iti_training_company, iti_training_contacts, iti_training_tel, ";
				sql += " iti_training_email, iti_training_address, iti_status, iti_type_mark, ";
				sql	+= " (select avg(itc_style_score + itc_quality_score + itc_structure_score + itc_interaction_score ) / 4 as score from InstructorComment where itc_iti_ent_id = iti_ent_id ) as iti_score,";
				sql += " iti_create_user_id, iti_create_datetime, iti_update_user_id, iti_update_datetime, iti_recommend,";
				sql += " ent_id, ent_upd_date, ent_type,  ";
				sql += " upt_title as sks_title, usg_display_bil, ugr_display_bil, usr_join_datetime ";
				sql += " from InstructorInf ";
				sql += " inner join Entity on (iti_ent_id = ent_id) ";
				sql += " inner join regUser on (iti_ent_id = usr_ent_id)  ";
				sql += " left join regUserExtension on(urx_usr_ent_id = usr_ent_id)   ";
				sql += " left join EntityRelation ern1 on (ern1.ern_child_ent_id = usr_ent_id and ern1.ern_parent_ind = 1 and ern1.ern_type = 'USR_PARENT_USG') ";
				sql += " left join UserGroup on (ern1.ern_ancestor_ent_id = usg_ent_id) ";
				sql += " left join EntityRelation ern2 on (ern2.ern_child_ent_id = usr_ent_id and ern2.ern_parent_ind = 1 and ern2.ern_type = 'USR_CURRENT_UGR') ";
				sql += " left join UserGrade on (ern2.ern_ancestor_ent_id = ugr_ent_id) ";
				sql += " left join RegUserSkillSet on(uss_ent_id = usr_ent_id)   ";
				sql += " left join cmSkillSet on(uss_ske_id = sks_ske_id and sks_owner_ent_id = 1 and  sks_type = 'SKP' ) ";
				sql += " left join (select *  from UserPositionRelation, UserPosition  where upr_upt_id = upt_id) UserPosition on (upr_usr_ent_id = iti_ent_id) ";
				sql += " where iti_ent_id = ? ";
			} else {
				sql = " select ";
				sql += " iti_ent_id, iti_name, iti_gender, iti_bday, iti_mobile, ";
				sql += " iti_email, iti_img, iti_introduction, iti_level, iti_cos_type, iti_main_course, ";
				sql += " iti_type, iti_property, iti_highest_educational, iti_graduate_institutions, iti_address, ";
				sql += " iti_work_experience, iti_education_experience, iti_training_experience, ";
				sql += " iti_expertise_areas, iti_good_industry, iti_training_company, iti_training_contacts, iti_training_tel, ";
				sql += " iti_training_email, iti_training_address, iti_status, iti_type_mark,";
				sql += " (select avg(itc_style_score + itc_quality_score + itc_structure_score + itc_interaction_score ) / 4 as score from InstructorComment where itc_iti_ent_id = iti_ent_id ) as iti_score,";
				sql += " iti_create_user_id, iti_create_datetime, iti_update_user_id, iti_update_datetime, iti_recommend,";
				sql += " ent_id, ent_upd_date, ent_type, ";
				sql += " '' as sks_title, '' as usg_display_bil, '' as ugr_display_bil, null as usr_join_datetime ";
				sql += " from InstructorInf, Entity ";
				sql += " where iti_ent_id = ent_id and iti_ent_id = ? ";
			}
			stmt = con.prepareStatement(sql);
			stmt.setLong(1, ent_id);
			rs = stmt.executeQuery();
			if (rs.next()) {
				instr = new Instructor();

				instr.ent_id = rs.getLong("ent_id");
				instr.iti_ent_id = rs.getLong("iti_ent_id");
				instr.ent_type = rs.getString("ent_type");
				instr.ent_upd_date = rs.getTimestamp("ent_upd_date");

				instr.iti_name = rs.getString("iti_name");
				instr.iti_gender = rs.getString("iti_gender");
				instr.iti_bday = rs.getTimestamp("iti_bday");
				instr.iti_mobile = rs.getString("iti_mobile");
				instr.iti_email = rs.getString("iti_email");
				instr.iti_img = rs.getString("iti_img");
				instr.iti_introduction = rs.getString("iti_introduction");
				instr.iti_level = rs.getString("iti_level");
				instr.iti_cos_type = rs.getString("iti_cos_type");
				instr.iti_main_course = rs.getString("iti_main_course");
				instr.iti_type = rs.getString("iti_type");
				instr.iti_property = rs.getString("iti_property");
				instr.iti_highest_educational = rs.getString("iti_highest_educational");
				instr.iti_graduate_institutions = rs.getString("iti_graduate_institutions");
				instr.iti_address = rs.getString("iti_address");
				instr.iti_work_experience = rs.getString("iti_work_experience");
				instr.iti_education_experience = rs.getString("iti_education_experience");
				instr.iti_training_experience = rs.getString("iti_training_experience");
				instr.iti_expertise_areas = rs.getString("iti_expertise_areas");
				instr.iti_good_industry = rs.getString("iti_good_industry");
				instr.iti_training_company = rs.getString("iti_training_company");
				instr.iti_training_tel = rs.getString("iti_training_tel");
				instr.iti_training_contacts = rs.getString("iti_training_contacts");
				instr.iti_training_email = rs.getString("iti_training_email");
				instr.iti_training_address = rs.getString("iti_training_address");
				instr.iti_status = rs.getString("iti_status");
				instr.iti_type_mark = rs.getString("iti_type_mark");
				instr.iti_score = rs.getFloat("iti_score");
				instr.iti_create_usr_id = rs.getString("iti_create_user_id");
				instr.iti_create_datetime = rs.getTimestamp("iti_create_datetime");
				instr.iti_upd_usr_id = rs.getString("iti_update_user_id");
				instr.iti_upd_datetime = rs.getTimestamp("iti_update_datetime");

		
				instr.setIti_sks_title(rs.getString("sks_title"));
				instr.setIti_user_group(rs.getString("usg_display_bil"));
				instr.setIti_user_grade(rs.getString("ugr_display_bil"));
				if(rs.getString("usr_join_datetime")!=null){
				  instr.setIti_join_datetime(rs.getTimestamp("usr_join_datetime"));
				}
				instr.setIti_recommend(rs.getInt("iti_recommend"));
			} else {
				throw new qdbException("Failed to get instructor information.");
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return instr;
	}

	public static Vector<InstructorCos> getInstrCos(Connection con, long ent_id) throws qdbException, SQLException {
		ResultSet rs = null;
		PreparedStatement stmt = null;

		Vector<InstructorCos> icsVec = new Vector<InstructorCos>();
		try {
			String sql = " select ics_id, ics_iti_ent_id, ics_title, ics_fee, ics_hours, ics_target, ics_content ";
			sql += " from InstructorCos where ics_iti_ent_id = ? order by ics_id ";

			stmt = con.prepareStatement(sql);
			stmt.setLong(1, ent_id);
			rs = stmt.executeQuery();
			while (rs.next()) {
				InstructorCos ics = new InstructorCos();
				ics.setIcs_id(rs.getLong("ics_id"));
				ics.setIcs_ent_id(rs.getLong("ics_iti_ent_id"));
				ics.setIcs_title(rs.getString("ics_title"));
				ics.setIcs_target(rs.getString("ics_target"));
				ics.setIcs_content(rs.getString("ics_content"));
				ics.setIcs_fee(rs.getFloat("ics_fee"));
				ics.setIcs_hours(rs.getFloat("ics_hours"));

				icsVec.add(ics);
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return icsVec;
	}

	public static long ins(Connection con, Instructor instr, long ref_ent_id) throws qdbException, qdbErrMessage, SQLException {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			// 引用了系统用户，那么用户就是内部讲师
			if (ref_ent_id > 0) {
				instr.iti_ent_id = ref_ent_id;

				instr.iti_type_mark = Instructor.TYPE_MARK_INT;
			} else {
				instr.ent_type = dbEntity.ENT_TYPE_INSTR;
				instr.ent_syn_ind = false;

				instr.ins(con);

				instr.iti_ent_id = instr.ent_id;

				instr.iti_type_mark = Instructor.TYPE_MARK_EXT;
			}

			String sql = "";
			sql += " insert into InstructorInf ( ";
			sql += " iti_ent_id, iti_name, iti_gender, iti_bday, iti_mobile, ";
			sql += " iti_email, iti_img, iti_introduction, iti_level, iti_cos_type, iti_main_course, ";
			sql += " iti_type, iti_property, iti_highest_educational, iti_graduate_institutions, iti_address, ";
			sql += " iti_work_experience, iti_education_experience, iti_training_experience, ";
			sql += " iti_expertise_areas, iti_good_industry, iti_training_company, iti_training_contacts, iti_training_tel, ";
			sql += " iti_training_email, iti_training_address, iti_status, iti_type_mark, ";
			sql += " iti_create_user_id, iti_create_datetime, iti_update_user_id, iti_update_datetime , iti_tcr_id ";
			sql += " ) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ";
			sql += "	?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) ";

			int idx = 1;
			stmt = con.prepareStatement(sql);
			stmt.setLong(idx++, instr.iti_ent_id);
			stmt.setString(idx++, instr.iti_name);
			stmt.setString(idx++, instr.iti_gender);
			stmt.setTimestamp(idx++, instr.iti_bday);
			stmt.setString(idx++, instr.iti_mobile);

			stmt.setString(idx++, instr.iti_email);
			stmt.setString(idx++, instr.iti_img);
			stmt.setString(idx++, instr.iti_introduction);
			stmt.setString(idx++, instr.iti_level);
			stmt.setString(idx++, instr.iti_cos_type);
			stmt.setString(idx++, instr.iti_main_course);

			stmt.setString(idx++, instr.iti_type);
			stmt.setString(idx++, instr.iti_property);
			stmt.setString(idx++, instr.iti_highest_educational);
			stmt.setString(idx++, instr.iti_graduate_institutions);
			stmt.setString(idx++, instr.iti_address);

			stmt.setString(idx++, instr.iti_work_experience);
			stmt.setString(idx++, instr.iti_education_experience);
			stmt.setString(idx++, instr.iti_training_experience);
			stmt.setString(idx++, instr.iti_expertise_areas);
			stmt.setString(idx++, instr.iti_good_industry);
			stmt.setString(idx++, instr.iti_training_company);
			stmt.setString(idx++, instr.iti_training_contacts);

			stmt.setString(idx++, instr.iti_training_tel);
			stmt.setString(idx++, instr.iti_training_email);
			stmt.setString(idx++, instr.iti_training_address);
			stmt.setString(idx++, instr.iti_status);
			stmt.setString(idx++, instr.iti_type_mark);

			stmt.setString(idx++, instr.iti_create_usr_id);
			stmt.setTimestamp(idx++, instr.iti_create_datetime);
			stmt.setString(idx++, instr.iti_upd_usr_id);
			stmt.setTimestamp(idx++, instr.iti_upd_datetime);
			stmt.setLong(idx++, instr.getIti_tcr_id());
			int cnt = stmt.executeUpdate();
			cwSQL.cleanUp(rs, stmt);
			if (cnt != 1) {
				throw new qdbException("Failed to insert instructor.");
			}

			if (true) {
				Timestamp curTime = cwSQL.getTime(con);

				sql = "";
				sql += " insert into acEntityRole (erl_ent_id, erl_rol_id, erl_creation_timestamp, erl_eff_start_datetime, erl_eff_end_datetime) ";
				sql += " select ?, rol_id, ?, ?, ? ";
				sql += " from acRole where rol_ste_uid = 'INSTR' ";

				idx = 1;
				stmt = con.prepareStatement(sql);
				stmt.setLong(idx++, instr.iti_ent_id);
				stmt.setTimestamp(idx++, curTime);
				stmt.setTimestamp(idx++, Timestamp.valueOf(cwUtils.MIN_TIMESTAMP));
				stmt.setTimestamp(idx++, Timestamp.valueOf(cwUtils.MAX_TIMESTAMP));
				stmt.executeUpdate();
				cwSQL.cleanUp(rs, stmt);
			}
			return instr.iti_ent_id;
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
	}

	public static void upd(Connection con, Instructor instr) throws qdbException, qdbErrMessage, SQLException {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			String sql = " ";
			sql += " update InstructorInf set ";
			sql += " iti_name = ?, iti_gender = ?, iti_bday = ?, iti_mobile = ?, iti_email = ?, ";
			if(instr.getIti_img() != null && instr.getIti_img() != ""){
				sql += " iti_img = ?, ";
			}
			sql += " iti_introduction = ?, iti_level = ?, iti_cos_type = ?, iti_main_course = ?, ";
			sql += " iti_type = ?, iti_property = ?, iti_highest_educational = ?, iti_graduate_institutions = ?, iti_address = ?, ";
			sql += " iti_work_experience = ?, iti_education_experience = ?, iti_training_experience = ?, ";
			sql += " iti_expertise_areas = ?, iti_good_industry = ?, iti_training_company = ?, iti_training_contacts = ?, iti_training_tel = ?, ";
			sql += " iti_training_email = ?, iti_training_address = ?, iti_status = ?, iti_type_mark = ?, ";
			sql += " iti_update_user_id = ?, iti_update_datetime = ? ";
			sql += " where iti_ent_id = ? ";

			int idx = 1;
			stmt = con.prepareStatement(sql);
			stmt.setString(idx++, instr.iti_name);
			stmt.setString(idx++, instr.iti_gender);
			stmt.setTimestamp(idx++, instr.iti_bday);
			stmt.setString(idx++, instr.iti_mobile);

			stmt.setString(idx++, instr.iti_email);
			if(instr.getIti_img() != null && instr.getIti_img() != ""){
				stmt.setString(idx++, instr.iti_img);
			}
			stmt.setString(idx++, instr.iti_introduction);
			stmt.setString(idx++, instr.iti_level);
			stmt.setString(idx++, instr.iti_cos_type);
			stmt.setString(idx++, instr.iti_main_course);

			stmt.setString(idx++, instr.iti_type);
			stmt.setString(idx++, instr.iti_property);
			stmt.setString(idx++, instr.iti_highest_educational);
			stmt.setString(idx++, instr.iti_graduate_institutions);
			stmt.setString(idx++, instr.iti_address);

			stmt.setString(idx++, instr.iti_work_experience);
			stmt.setString(idx++, instr.iti_education_experience);
			stmt.setString(idx++, instr.iti_training_experience);

			stmt.setString(idx++, instr.iti_expertise_areas);
			stmt.setString(idx++, instr.iti_good_industry);
			stmt.setString(idx++, instr.iti_training_company);
			stmt.setString(idx++, instr.iti_training_contacts);
			stmt.setString(idx++, instr.iti_training_tel);

			stmt.setString(idx++, instr.iti_training_email);
			stmt.setString(idx++, instr.iti_training_address);
			stmt.setString(idx++, instr.iti_status);
			stmt.setString(idx++, instr.iti_type_mark);
			stmt.setString(idx++, instr.iti_upd_usr_id);
			stmt.setTimestamp(idx++, instr.iti_upd_datetime);

			stmt.setLong(idx++, instr.iti_ent_id);

			int cnt = stmt.executeUpdate();
			if (cnt != 1) {
				throw new qdbException("Failed to insert instructor.");
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
	}

	public static void updateInstructorCourse(Connection con, long iti_ent_id, Vector<InstructorCos> icsVec) throws SQLException {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			String sql = " delete from InstructorCos where ics_iti_ent_id = ? ";

			stmt = con.prepareStatement(sql);
			stmt.setLong(1, iti_ent_id);
			stmt.executeUpdate();
			cwSQL.cleanUp(rs, stmt);

			for (InstructorCos ics : icsVec) {
				sql = " insert into InstructorCos ( ";
				sql += " ics_iti_ent_id, ics_title, ics_fee, ics_hours, ics_target, ics_content ";
				sql += " ) values (?, ?, ?, ?, ?, ?) ";

				int idx = 1;
				stmt = con.prepareStatement(sql);
				stmt.setLong(idx++, iti_ent_id);
				stmt.setString(idx++, ics.getIcs_title());
				stmt.setFloat(idx++, ics.getIcs_fee());
				stmt.setFloat(idx++, ics.getIcs_hours());
				stmt.setString(idx++, ics.getIcs_target());
				stmt.setString(idx++, ics.getIcs_content());
				stmt.executeUpdate();
				cwSQL.cleanUp(rs, stmt);
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
	}

	public static void del(Connection con, long iti_ent_id) throws SQLException, cwException {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			boolean is_external = false;

			String sql = " select iti_type_mark from InstructorInf, Entity where ent_id = iti_ent_id and iti_ent_id = ?  ";

			stmt = con.prepareStatement(sql);
			stmt.setLong(1, iti_ent_id);
			rs = stmt.executeQuery();

			if (rs.next()) {
				if (rs.getString("iti_type_mark").equals(Instructor.TYPE_MARK_EXT)) {
					is_external = true;
				}
			}
			cwSQL.cleanUp(rs, stmt);
			
			sql = " delete from InstructorCos where ics_iti_ent_id = ? ";
			stmt = con.prepareStatement(sql);
			stmt.setLong(1, iti_ent_id);
			stmt.executeUpdate();
			cwSQL.cleanUp(rs, stmt);

			sql = " delete from InstructorComment where itc_iti_ent_id = ? ";
			stmt = con.prepareStatement(sql);
			stmt.setLong(1, iti_ent_id);
			stmt.executeUpdate();
			cwSQL.cleanUp(rs, stmt);

			sql = " delete from InstructorInf where iti_ent_id = ? ";
			stmt = con.prepareStatement(sql);
			stmt.setLong(1, iti_ent_id);
			stmt.executeUpdate();
			cwSQL.cleanUp(rs, stmt);

			if (is_external) {
				// 讲师删除的时候移除用户的讲师角色关联
				sql = " delete from acEntityRole  where erl_ent_id = ? ";
				sql += " and erl_rol_id in ( select rol_id from acRole where rol_ste_uid = 'INSTR') ";
				stmt = con.prepareStatement(sql);
				stmt.setLong(1, iti_ent_id);
				stmt.executeUpdate();
				cwSQL.cleanUp(rs, stmt);
				
				sql = " delete from Entity where ent_id = ? ";
				stmt = con.prepareStatement(sql);
				stmt.setLong(1, iti_ent_id);
				stmt.executeUpdate();
				cwSQL.cleanUp(rs, stmt);
			} else {
				// 内部讲师删除的时候移除用户的讲师角色关联
				sql = " delete from acEntityRole  where erl_ent_id = ? ";
				sql += " and erl_rol_id in ( select rol_id from acRole where rol_ste_uid = 'INSTR') ";

				stmt = con.prepareStatement(sql);
				stmt.setLong(1, iti_ent_id);
				stmt.executeUpdate();
				cwSQL.cleanUp(rs, stmt);
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
	}

	public static String canDelInstructor(Connection con, long iti_ent_id) throws SQLException {
		String title = "";

		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			String sql = " select distinct itm_id, itm_title ";
			sql += " from aeItemAccess, acRole, aeItem where iac_access_id = rol_ext_id ";
			sql += " and itm_id = iac_itm_id and iac_access_type = 'ROLE'  and rol_ste_uid = 'INSTR' and iac_ent_id = ? ";
			sql += " order by itm_title ";

			stmt = con.prepareStatement(sql);
			stmt.setLong(1, iti_ent_id);
			rs = stmt.executeQuery();

			while (rs.next()) {
				title += (cwUtils.notEmpty(title) ? ", " : "") + rs.getString("itm_title");
			}
			return title;
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
	}

	public static void updateInstructorScore(Connection con, long iti_ent_id) throws SQLException {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			float score = 0f;

			String sql = " select ";
			sql += " avg(itc_style_score + itc_quality_score + itc_structure_score + itc_interaction_score ) / 4 as score";
			sql += "      from InstructorComment where itc_iti_ent_id = ? ";

			stmt = con.prepareStatement(sql);
			stmt.setLong(1, iti_ent_id);
			rs = stmt.executeQuery();

			if (rs.next()) {
				score = rs.getFloat("score");
			}
			cwSQL.cleanUp(rs, stmt);

			sql = " update InstructorInf set iti_score = ? where iti_ent_id = ? ";
			stmt = con.prepareStatement(sql);
			stmt.setFloat(1, score);
			stmt.setLong(2, iti_ent_id);
			stmt.executeUpdate();
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
	}
	
	
	public static void delCommentByItmId(Connection con,long itm_id,long[] ins_entIds) throws SQLException {
	    PreparedStatement stmt = null;
	    try {
	        
	        String SQL= "delete from InstructorComment where itc_itm_id =? ";
	        if(ins_entIds != null && ins_entIds.length > 0){
	            SQL +=" and itc_iti_ent_id in "+ cwUtils.array2list(ins_entIds);
	        }
	        
	        stmt = con.prepareStatement(SQL);
	        stmt.setLong(1, itm_id);
	        stmt.executeUpdate();
	    }finally {
	        cwSQL.cleanUp(null, stmt);
	    }
    }
	
	public static Vector getCommentInsIDByItmId(Connection con,long itm_id,long[] exculde_ins_entIds) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Vector re = new Vector();
        try {
            
            String SQL= "select distinct itc_iti_ent_id from InstructorComment where itc_itm_id =? ";
            if(exculde_ins_entIds != null && exculde_ins_entIds.length > 0){
                SQL +=" and itc_iti_ent_id not in "+ cwUtils.array2list(exculde_ins_entIds);
            }
            
            stmt = con.prepareStatement(SQL);
            stmt.setLong(1, itm_id);
            rs = stmt.executeQuery();

            while (rs.next()) {
                re.add(new Long(rs.getLong("itc_iti_ent_id")));
            }
        }finally {
            cwSQL.cleanUp(rs, stmt);
        }
        return re;
    }


	public InstructorComment getInstructorCommentScore(Connection con, long itc_itm_id, long itc_ent_id) throws SQLException {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		InstructorComment itc_ = new InstructorComment();
		try {
			String sql = " ";
			sql += " select itc_id ,itc_itm_id ,itc_ent_id ,itc_iti_ent_id ,itc_style_score ,itc_quality_score ,itc_structure_score ,itc_interaction_score ,itc_score,itc_comment,itc_create_user_id ";
			sql += "  from InstructorComment ";
			sql += " where itc_itm_id = ? and itc_ent_id = ? ";
			sql += " order by itc_update_datetime  desc ";

			int idx = 1;
			stmt = con.prepareStatement(sql);
			stmt.setLong(idx++, itc_itm_id);
			stmt.setLong(idx++, itc_ent_id);

			rs = stmt.executeQuery();
			if (rs.next()) {
				itc_.setItc_id(rs.getLong("itc_id"));
				itc_.setItc_itm_id(rs.getLong("itc_itm_id"));
				itc_.setItc_ent_id(rs.getLong("itc_ent_id"));
				itc_.setItc_iti_ent_id(rs.getLong("itc_iti_ent_id"));
				itc_.setItc_style_score(rs.getFloat("itc_style_score"));
				itc_.setItc_quality_score(rs.getFloat("itc_quality_score"));
				itc_.setItc_structure_score(rs.getFloat("itc_structure_score"));
				itc_.setItc_interaction_score(rs.getFloat("itc_interaction_score"));
				itc_.setItc_score(rs.getFloat("itc_score"));
				itc_.setItc_comment(rs.getString("itc_comment"));
				itc_.setItc_create_usr_id(rs.getString("itc_create_user_id"));
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return itc_;
	}

	public void updateInstructorCommentScore(Connection con, long itc_itm_id, long itc_ent_id, InstructorComment itc) throws qdbException, SQLException {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			String sql = " ";
			sql += " update InstructorComment set ";
			sql += " itc_style_score =? ,itc_quality_score =? ,itc_structure_score =? ,itc_interaction_score = ? , ";
			sql += " itc_score = ? ,itc_comment = ?, itc_update_datetime =? ,itc_update_user_id =? ";
			sql += " where itc_itm_id = ? and itc_ent_id = ? ";

			cwSQL.getTime(con);
			int idx = 1;
			stmt = con.prepareStatement(sql);
			stmt.setFloat(idx++, itc.getItc_style_score());
			stmt.setFloat(idx++, itc.getItc_quality_score());
			stmt.setFloat(idx++, itc.getItc_structure_score());
			stmt.setFloat(idx++, itc.getItc_interaction_score());
			stmt.setFloat(idx++, itc.getItc_score());
			stmt.setString(idx++, itc.getItc_comment());
			stmt.setTimestamp(idx++, cwSQL.getTime(con));
			stmt.setString(idx++, itc.getItc_upd_usr_id());
			stmt.setLong(idx++, itc_itm_id);
			stmt.setLong(idx++, itc_ent_id);
			int cnt = stmt.executeUpdate();
			if (cnt == 0) {
				throw new qdbException("Failed to insert instructor.");
			}

			updateInstructorScore(con, itc.getItc_iti_ent_id());
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
	}

	public void insertInstructorCommentScore(Connection con, long itc_itm_id, long itc_ent_id, InstructorComment itc) throws qdbException, SQLException {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			String sql = " ";
			sql += "insert into InstructorComment (itc_itm_id , itc_ent_id , itc_iti_ent_id ,itc_style_score ,itc_quality_score ,";
			sql += "itc_structure_score , itc_interaction_score , itc_score , itc_comment , itc_create_datetime , itc_create_user_id , itc_update_datetime , itc_update_user_id ) ";
			sql += " values(?,?,?,?,?,?,?,?,?,?,?,?,?) ";

			int idx = 1;
			stmt = con.prepareStatement(sql);
			stmt.setLong(idx++, itc_itm_id);
			stmt.setLong(idx++, itc_ent_id);
			stmt.setLong(idx++, itc.getItc_iti_ent_id());
			stmt.setFloat(idx++, itc.getItc_style_score());
			stmt.setFloat(idx++, itc.getItc_quality_score());
			stmt.setFloat(idx++, itc.getItc_structure_score());
			stmt.setFloat(idx++, itc.getItc_interaction_score());
			stmt.setFloat(idx++, itc.getItc_score());
			stmt.setString(idx++, itc.getItc_comment());
			stmt.setTimestamp(idx++, cwSQL.getTime(con));
			stmt.setString(idx++, itc.getItc_create_usr_id());
			stmt.setTimestamp(idx++, cwSQL.getTime(con));
			stmt.setString(idx++, itc.getItc_upd_usr_id());
			int cnt = stmt.executeUpdate();
			if (cnt != 1) {
				throw new qdbException("Failed to insert instructor.");
			}

			updateInstructorScore(con, itc.getItc_iti_ent_id());
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
	}

	public HashMap<String, Long> getInstructorComment_ent_id(Connection con, long itc_itm_id, long itc_ent_id) throws SQLException {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		HashMap<String, Long> hmp = new HashMap<String, Long>();

		try {
			String sql = " ";
			sql += " select itc_iti_ent_id ";
			sql += "  from InstructorComment ";
			sql += " where itc_itm_id = ? and itc_ent_id = ? ";

			int idx = 1;
			stmt = con.prepareStatement(sql);
			stmt.setLong(idx++, itc_itm_id);
			stmt.setLong(idx++, itc_ent_id);

			rs = stmt.executeQuery();
			while (rs.next()) {
				hmp.put(String.valueOf(rs.getLong("itc_iti_ent_id")), rs.getLong("itc_iti_ent_id"));
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return hmp;
	}

	public Vector<InstructorComment> getInstructorComment(Connection con, long itc_itm_id, long itc_iti_ent_id) throws SQLException {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		Vector<InstructorComment> v = new Vector<InstructorComment>();
		try {
			String sql = " ";
			sql += " select itc_id ,itc_itm_id ,itc_ent_id ,itc_iti_ent_id ,itc_style_score ,itc_quality_score ,itc_structure_score ,itc_interaction_score ,itc_score,itc_comment,itc_update_datetime,usr_display_bil ";
			sql += " from InstructorComment ";
			sql += " left join RegUser on itc_ent_id =usr_ent_id";
			sql += " where itc_itm_id = ? and itc_iti_ent_id = ? ";
			sql += " order by itc_update_datetime  desc ";

			int idx = 1;
			stmt = con.prepareStatement(sql);
			stmt.setLong(idx++, itc_itm_id);
			stmt.setLong(idx++, itc_iti_ent_id);

			rs = stmt.executeQuery();
			while (rs.next()) {
				InstructorComment itc_ = new InstructorComment();
				itc_.setItc_id(rs.getLong("itc_id"));
				itc_.setItc_itm_id(rs.getLong("itc_itm_id"));
				itc_.setItc_ent_id(rs.getLong("itc_ent_id"));
				itc_.setItc_iti_ent_id(rs.getLong("itc_iti_ent_id"));
				itc_.setItc_style_score(rs.getFloat("itc_style_score"));
				itc_.setItc_quality_score(rs.getFloat("itc_quality_score"));
				itc_.setItc_structure_score(rs.getFloat("itc_structure_score"));
				itc_.setItc_interaction_score(rs.getFloat("itc_interaction_score"));
				itc_.setItc_score(rs.getFloat("itc_score"));
				itc_.setItc_comment(rs.getString("itc_comment"));
				itc_.setItc_upd_datetime(rs.getTimestamp("itc_update_datetime"));
				itc_.setUsr_display_bil(rs.getString("usr_display_bil"));
				v.add(itc_);
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return v;
	}

	public static boolean isInstructor(Connection con, long iti_ent_id) throws SQLException {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			String sql = " ";
			sql += " select iti_ent_id from Instructorinf where iti_ent_id = ? ";

			int idx = 1;
			stmt = con.prepareStatement(sql);
			stmt.setLong(idx++, iti_ent_id);

			rs = stmt.executeQuery();
			if (rs.next()) {
				return true;
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return false;
	}

	public Vector<Instructor> search(Connection con, InstructorReqParam param, String iti_type_mark ,long top_tc_id,boolean lnMode) throws SQLException {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		Vector<Instructor> v = new Vector<Instructor>();
		try {
			cwPagination cwPage = param.getCwPage();
			if (cwPage.sortCol == null || cwPage.sortCol.length() == 0) {
				cwPage.sortCol = "iti_update_datetime";
			}
			if (cwPage.sortOrder == null || cwPage.sortOrder.length() == 0) {
				cwPage.sortOrder = "asc";
			}

			StringBuffer sql = new StringBuffer();
			sql.append(" select * from (                                                             ");

			if (InstructorManager.ITI_TYPE_REMARK_EXT.equalsIgnoreCase(iti_type_mark) || cwUtils.isEmpty(iti_type_mark)) {
				sql.append("select iti_ent_id, iti_name, iti_gender, iti_level, NULL as sks_title, iti_main_course, iti_mobile,(select avg(itc_style_score + itc_quality_score + itc_structure_score + itc_interaction_score ) / 4 as score from InstructorComment where itc_iti_ent_id = iti_ent_id ) as iti_score ,iti_training_company,iti_expertise_areas, iti_type_mark, iti_update_datetime ");
				sql.append(" from InstructorInf                                                                                        ");
				sql.append(" inner join entity on(iti_ent_id = ent_id and iti_type_mark = 'EXT')                                                             ");
				sql.append(" where 1=1 ");
				if(lnMode){
					sql.append(" and ( iti_tcr_id in ( select tcn_child_tcr_id from tcRelation where tcn_ancestor = ? ) or iti_tcr_id = ? ) ");
				}
			}
			if (InstructorManager.ITI_TYPE_REMARK_IN.equalsIgnoreCase(iti_type_mark) || cwUtils.isEmpty(iti_type_mark)) {
				if (cwUtils.isEmpty(iti_type_mark)) {
					sql.append(" union ");
				}

				if (cwSQL.DBVENDOR_ORACLE.equalsIgnoreCase(cwSQL.getDbType())) {
					sql.append("select iti_ent_id, cast(usr_display_bil as nvarchar2(255)) as iti_name, cast(usr_gender as nvarchar2(20)) as iti_gender, iti_level, upt_title as sks_title, iti_main_course, cast(usr_tel_1 as nvarchar2(255)) as iti_mobile,(select avg(itc_style_score + itc_quality_score + itc_structure_score + itc_interaction_score ) / 4 as score from InstructorComment where itc_iti_ent_id = iti_ent_id ) as iti_score ,iti_training_company, iti_expertise_areas, iti_type_mark, iti_update_datetime ");
				} else {
					sql.append("select iti_ent_id, usr_display_bil as iti_name, usr_gender as iti_gender, iti_level, upt_title sks_title, iti_main_course, usr_tel_1 as iti_mobile,(select avg(itc_style_score + itc_quality_score + itc_structure_score + itc_interaction_score ) / 4 as score from InstructorComment where itc_iti_ent_id = iti_ent_id ) as iti_score ,iti_training_company, iti_expertise_areas, iti_type_mark, iti_update_datetime ");
				}

				sql.append(" from InstructorInf                                                                                       ");
				sql.append(" inner join reguser on(iti_ent_id = usr_ent_id)                                                             ");
				sql.append(" left join RegUserSkillSet on(uss_ent_id = usr_ent_id)                                                      ");
				sql.append(" left join cmSkillSet on(uss_ske_id = sks_ske_id and sks_owner_ent_id = 1 and  sks_type = 'SKP' )           ");
				sql.append(" left join (select *  from UserPositionRelation, UserPosition  where upr_upt_id = upt_id) UserPosition on (upr_usr_ent_id = iti_ent_id) ");
				sql.append(" where 1=1 ");
				if(lnMode){
					sql.append(" and ( iti_tcr_id in ( select tcn_child_tcr_id from tcRelation where tcn_ancestor = ? )or iti_tcr_id = ? )");
				}
			}
			sql.append("  ) ins ");
			sql.append(" where 1=1   ");
			if (iti_type_mark != null && iti_type_mark.trim().length() > 0) {
				sql.append("  and iti_type_mark = ? ");
			}
			//讲师姓名
			if (param.getIti_name() != null && param.getIti_name().trim().length() > 0) {
			    sql.append("  and lower(iti_name) like ? ");

			}
			if (param.getIti_level() != null && param.getIti_level().trim().length() > 0) {
				sql.append("  and iti_level = ? ");
			}
			//讲师岗位
			if (InstructorManager.ITI_TYPE_REMARK_IN.equalsIgnoreCase(iti_type_mark) && param.getIti_gw_str() != null && param.getIti_gw_str().trim().length() > 0) {
				sql.append("  and lower(sks_title) like ? ");
			}
			//主讲课程
			if (param.getIti_main_course() != null && param.getIti_main_course().trim().length() > 0) {
				sql.append("  and lower(iti_main_course) like ? ");
			}
			if (param.getIti_score_from() > 0) {
				sql.append("  and iti_score >= ? ");
			}
			if (param.getIti_score_to() > 0) {
				sql.append("  and iti_score <= ? ");
			}
			if (param.getIti_expertise_areas() != null && param.getIti_expertise_areas().trim().length() > 0) {
				sql.append("  and lower(iti_expertise_areas) like ? ");
			}

			if (param.getIti_training_company() != null && param.getIti_training_company().trim().length() > 0) {
				sql.append("  and lower(iti_training_company) like ? ");
			}
			if(param.getSearch_text()!=null&&param.getSearch_text().trim().length()>0 && InstructorManager.ITI_TYPE_REMARK_IN.equalsIgnoreCase(iti_type_mark)){
				sql.append("  and (lower(iti_name) like ? ");
				sql.append("  or lower(sks_title) like ? ");
				sql.append("  or lower(iti_main_course) like ?) ");
			}
			if(param.getSearch_text()!=null&&param.getSearch_text().trim().length()>0 && InstructorManager.ITI_TYPE_REMARK_EXT.equalsIgnoreCase(iti_type_mark)){
				sql.append("  and (lower(iti_name) like ? ");
				sql.append("  or lower(iti_expertise_areas) like ? ");
				sql.append("  or lower(iti_expertise_areas) like ? ");
				sql.append("  or lower(iti_main_course) like ?) ");
			}
			if (param.getIls_id() > 0) {
				if ("DEL".equalsIgnoreCase(param.getFor_time_table())) {
					sql.append("  and exists (select ili_usr_ent_id from aeItemLessonInstructor where ili_usr_ent_id = iti_ent_id and ili_ils_id = ?)");
				} else {
					sql.append("  and not exists (select ili_usr_ent_id from aeItemLessonInstructor where ili_usr_ent_id = iti_ent_id and ili_ils_id = ?)");
					sql.append("  and  iti_ent_id in(select iac_ent_id from aeItemLesson,aeItemaccess where iac_itm_id = ils_itm_id and iac_access_id like ? and ils_id = ?)");
				}

			}

			sql.append(" order by " + cwPage.sortCol + " " + cwPage.sortOrder);

			int idx = 1;
			stmt = con.prepareStatement(sql.toString());
			if (InstructorManager.ITI_TYPE_REMARK_EXT.equalsIgnoreCase(iti_type_mark) || cwUtils.isEmpty(iti_type_mark)) {
				if(lnMode){
					stmt.setLong(idx++, top_tc_id);
					stmt.setLong(idx++, top_tc_id);
				}
			}
			if (InstructorManager.ITI_TYPE_REMARK_IN.equalsIgnoreCase(iti_type_mark) || cwUtils.isEmpty(iti_type_mark)) {
				if(lnMode){
					stmt.setLong(idx++, top_tc_id);
					stmt.setLong(idx++, top_tc_id);
				}
			}
			if (iti_type_mark != null && iti_type_mark.trim().length() > 0) {
				stmt.setString(idx++, iti_type_mark);
			}

			if (param.getIti_name() != null && param.getIti_name().trim().length() > 0) {
				stmt.setString(idx++, "%" + param.getIti_name().trim().toLowerCase() + "%");
			}
			if (param.getIti_level() != null && param.getIti_level().trim().length() > 0) {
				stmt.setString(idx++, param.getIti_level().trim());
			}
			if (InstructorManager.ITI_TYPE_REMARK_IN.equalsIgnoreCase(iti_type_mark) && param.getIti_gw_str() != null && param.getIti_gw_str().trim().length() > 0) {
				stmt.setString(idx++, "%" + param.getIti_gw_str().trim().toLowerCase() + "%");
			}
			if (param.getIti_main_course() != null && param.getIti_main_course().trim().length() > 0) {
				stmt.setString(idx++, "%" + param.getIti_main_course().trim().toLowerCase() + "%");
			}
			if (param.getIti_score_from() > 0) {
				stmt.setFloat(idx++, param.getIti_score_from()-(float)0.05);
			}

			if (param.getIti_score_to() > 0) {
				stmt.setFloat(idx++, param.getIti_score_to()+(float)0.04);
			}
			if (param.getIti_expertise_areas() != null && param.getIti_expertise_areas().trim().length() > 0) {
				stmt.setString(idx++, "%" + param.getIti_expertise_areas().trim().toLowerCase() + "%");
			}

			if (param.getIti_training_company() != null && param.getIti_training_company().trim().length() > 0) {
				stmt.setString(idx++, "%" + param.getIti_training_company().trim().toLowerCase() + "%");
			}
			if(param.getSearch_text()!=null&&param.getSearch_text().trim().length()>0 && InstructorManager.ITI_TYPE_REMARK_IN.equalsIgnoreCase(iti_type_mark)){
				stmt.setString(idx++, "%" + param.getSearch_text().trim().toLowerCase() + "%");
				stmt.setString(idx++, "%" + param.getSearch_text().trim().toLowerCase() + "%");
				stmt.setString(idx++, "%" + param.getSearch_text().trim().toLowerCase() + "%");
			}
			if(param.getSearch_text()!=null&&param.getSearch_text().trim().length()>0 && InstructorManager.ITI_TYPE_REMARK_EXT.equalsIgnoreCase(iti_type_mark)){
				stmt.setString(idx++, "%" + param.getSearch_text().trim().toLowerCase() + "%");
				stmt.setString(idx++, "%" + param.getSearch_text().trim().toLowerCase() + "%");
				stmt.setString(idx++, "%" + param.getSearch_text().trim().toLowerCase() + "%");
				stmt.setString(idx++, "%" + param.getSearch_text().trim().toLowerCase() + "%");
			}
			if (param.getIls_id() > 0) {
				stmt.setLong(idx++, param.getIls_id());
				
				if ("DEL".equalsIgnoreCase(param.getFor_time_table())) {
                    
                } else {
                    stmt.setString(idx++, "%INSTR%");
                    stmt.setLong(idx++, param.getIls_id());
                    
                }
			}

			rs = stmt.executeQuery();
			int cnt = 0;

			while (rs.next()) {
				if ((cnt >= (cwPage.curPage - 1) * cwPage.pageSize && cnt < (cwPage.curPage) * cwPage.pageSize)) {
					Instructor inst = new Instructor();
					inst.iti_ent_id = rs.getLong("iti_ent_id");
					inst.iti_name = rs.getString("iti_name");
					inst.iti_gender = rs.getString("iti_gender");
					inst.iti_level = rs.getString("iti_level");
					inst.iti_sks_title = rs.getString("sks_title");
					inst.iti_main_course = rs.getString("iti_main_course");
					inst.iti_mobile = rs.getString("iti_mobile");
					inst.iti_score = rs.getFloat("iti_score");
					inst.iti_training_company = rs.getString("iti_training_company");
					inst.iti_expertise_areas = rs.getString("iti_expertise_areas");
					inst.iti_type_mark = rs.getString("iti_type_mark");

					v.add(inst);
				}
				cnt++;
			}
			cwPage.totalRec = cnt;
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return v;
	}
	
	public String getInstructorName (Connection con,long ent_id){
		String sql = " select ";
		sql += "  iti_name ";
		sql += " from InstructorInf, Entity ";
		sql += " where iti_ent_id = ent_id and iti_ent_id = ? ";
		ResultSet rs = null;
		PreparedStatement stmt = null;
		int idx = 1;
		try {
			stmt = con.prepareStatement(sql);
			stmt.setLong(idx++, ent_id);
			rs = stmt.executeQuery();
			if (rs.next()) {
				return cwUtils.esc4XML(rs.getString("iti_name"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			CommonLog.error(e.getMessage(),e);
		}finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return null;
	}

	public static void updateRecommend(Connection con, long iti_ent_id, long iti_recommend) throws qdbException, SQLException {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			String sql = " ";
			sql += " update InstructorInf set ";
			sql += " iti_recommend =? ";
			sql += " where  iti_ent_id = ? ";
			stmt = con.prepareStatement(sql);
			cwSQL.getTime(con);
			int idx = 1;
			stmt.setLong(idx++, iti_recommend);
			stmt.setLong(idx++, iti_ent_id);
			int cnt = stmt.executeUpdate();
			if (cnt == 0) {
				throw new qdbException("Failed to update instructor.");
			}

		} finally {
			cwSQL.cleanUp(rs, stmt);
		}		
	}
	
	public static void delAeItemTargetRuleByItmId(Connection con,long itm_id) throws SQLException {
	    PreparedStatement stmt = null;
	    try {
	        
	        String SQL= "delete from aeItemTargetRule where itr_itm_id =? ";
	        stmt = con.prepareStatement(SQL);
	        stmt.setLong(1, itm_id);
	        stmt.executeUpdate();
	    }finally {
	        cwSQL.cleanUp(null, stmt);
	    }
    }
	
	public static void delaeItemTargetRuleDetailByItmId(Connection con,long itm_id) throws SQLException {
	    PreparedStatement stmt = null;
	    try {
	        
	        String SQL= "delete from aeItemTargetRuleDetail where ird_itm_id =? ";
	        stmt = con.prepareStatement(SQL);
	        stmt.setLong(1, itm_id);
	        stmt.executeUpdate();
	    }finally {
	        cwSQL.cleanUp(null, stmt);
	    }
    }
	
	public static void delitemTargetLrnDetailByItmId(Connection con,long itm_id) throws SQLException {
	    PreparedStatement stmt = null;
	    try {
	        
	        String SQL= "delete from itemTargetLrnDetail where itd_itm_id =? ";
	        stmt = con.prepareStatement(SQL);
	        stmt.setLong(1, itm_id);
	        stmt.executeUpdate();
	    }finally {
	        cwSQL.cleanUp(null, stmt);
	    }
    }
	
	
}