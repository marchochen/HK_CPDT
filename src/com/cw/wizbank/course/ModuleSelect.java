package com.cw.wizbank.course;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.db.view.ViewTrainingCenter;
import com.cw.wizbank.qdb.dbCourse;
import com.cw.wizbank.qdb.dbModule;
import com.cw.wizbank.qdb.dbResource;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwUtils;
//import com.ibm.db2.jcc.b.tc;
import com.cwn.wizbank.entity.Resources;

public class ModuleSelect {
    
    /*
     *  String ismultiple;
String discos;
String dismod;
String fieldname;
String wintitle;*/
    
    public String getpageXML(Connection con, String is_multiple, String dis_cos_type, String dis_mod_type, String field_name, String sel_type, String title, String width, long cos_id) throws SQLException{
        StringBuffer xml = new StringBuffer();
        xml.append("<sel_mod_option>");
        xml.append("<sel_type>").append(sel_type).append("</sel_type>");
        xml.append("<is_multiple>").append(is_multiple).append("</is_multiple>");
        xml.append("<dis_cos_type>").append(dis_cos_type).append("</dis_cos_type>");
        xml.append("<dis_mod_type>").append(dis_mod_type).append("</dis_mod_type>");
        xml.append("<re_field_name>").append(field_name).append("</re_field_name>");
        xml.append("<width>").append(width).append("</width>");
        if(title != null && title.length() > 0){
            xml.append("<title>").append(title).append("</title>");
        }
        long tcr_id = dbCourse.getCosItmTcrId(con, cos_id);
        xml.append("<training_center id=\"").append(tcr_id).append("\"/>");    
        xml.append("</sel_mod_option>");
        
        return xml.toString();
    }
    
    public String getCosListXML(Connection con, String search_type, String title_code, String dis_cos_type, loginProfile prof, long itm_tcr_id, boolean tc_enabled) throws SQLException {
        StringBuffer xml = new StringBuffer();
        List itm_data_list = getItmDataList( con, search_type,  title_code,  dis_cos_type, prof.root_ent_id, prof.usr_ent_id, itm_tcr_id, tc_enabled) ;
        xml.append("<data>");
        for (int i = 0; i<itm_data_list.size(); i++) {
            Map itm_data = (Map)itm_data_list.get(i);
            if (itm_data != null){
                xml.append("<item>");
                xml.append("<id>").append(itm_data.get("ID")).append("</id>");
                xml.append("<title>").append(dbUtils.esc4XML(itm_data.get("TITLE").toString())).append("</title>");
                xml.append("</item>");
            }
        }
        
        xml.append("</data>");
        return xml.toString();
    }
    
    public String getItmMods(Connection con, String is_multiple, String dis_mod_type, String sel_type,String sel_mod_status, long itm_id, String width) throws SQLException,qdbException {
        StringBuffer xml = new StringBuffer();
        xml.append("<sel_mod_option>");
        xml.append("<sel_type>").append(sel_type).append("</sel_type>");
        xml.append("<is_multiple>").append(is_multiple).append("</is_multiple>");
        xml.append("<dis_mod_type>").append(dis_mod_type).append("</dis_mod_type>");
        xml.append("<width>").append(width).append("</width>");
        xml.append("</sel_mod_option>");
        long cos_id = dbCourse.getCosResId(con, itm_id) ;
        if(cos_id > 0){
            String stu_xml = dbCourse.getCosStructureAsXML(con, cos_id);
            if(stu_xml != null){
                 xml.append("<modules>");
                 xml.append(stu_xml);
                 xml.append("<mod_list>");
                 List mod_list = getItmMods( con,  dis_mod_type,  sel_mod_status,  cos_id) ;
                 for (int i = 0; i<mod_list.size(); i++) {
                     Map mod_data = (Map)mod_list.get(i);
                     if (mod_data != null){
                         xml.append("<module id = '").append(mod_data.get("ID")).append("'>");
                         xml.append("<status>").append(mod_data.get("STATUS")).append("</status>");
                         xml.append("</module>");
                     }
                 }
                 
                 xml.append("</mod_list>");
                 xml.append("</modules>");
            }
        }
        return xml.toString();
    }
   
    private List getItmDataList(Connection con, String search_type, String title_code, String dis_cos_type, long root_ent_id, long usr_ent_id, long itm_tcr_id, boolean tc_enabled)  throws SQLException {
		List itm_list = new ArrayList();
		String sql = null;
		PreparedStatement stmt = null;
		//if itm_tcr_id is 0, will get the user all tc.
		String tcr_id_lst = "";
		if(tc_enabled) {
			if(itm_tcr_id == 0) {
				Vector vec = ViewTrainingCenter.getAllTcByOfficer(con, usr_ent_id);
				tcr_id_lst =cwUtils.vector2list(vec);
			} else {
				tcr_id_lst = "( " + itm_tcr_id + ")";
			}			
		}
		try {
			if (title_code != null) {
				title_code = cwUtils.esc4SQL(title_code);
			}
			String escape = " escape '\\' ";
			if(cwSQL.DBVENDOR_MYSQL.equalsIgnoreCase(cwSQL.getDbType())){
				 escape = " escape '\\\\' ";
			}
			String ConcatOperator = cwSQL.getConcatOperator();
			if(cwSQL.DBVENDOR_MYSQL.equalsIgnoreCase(cwSQL.getDbType())){
				sql = "select  childItem.itm_id as id, concat( parentItem.itm_title , "
						+ "'(' ," 
						+ " parentItem.itm_code , "
						+ " ') > ' , "
						+ " childItem.itm_title ,"
						+ " '(' ,"
						+ " childItem.itm_code , "
						+ " ')' ) as title from aeItem childItem, aeItemRelation, aeItem parentItem"
						+ " where "
						+ " childItem.itm_id = ire_child_itm_id and ire_parent_itm_id = parentItem.itm_id and childItem.itm_type in ('CLASSROOM','SELFSTUDY','VIDEO') and childItem.itm_owner_ent_id = ? ";
			}else{
				sql = "select  childItem.itm_id as id,parentItem.itm_title "
						+ ConcatOperator
						+ "'('" 
						+ ConcatOperator
						+ " parentItem.itm_code "
						+ ConcatOperator
						+ " ') > ' "
						+ ConcatOperator
						+ " childItem.itm_title"
						+ ConcatOperator
						+ "'('"
						+ ConcatOperator
						+ "childItem.itm_code "
						+ ConcatOperator
						+ " ')' as title from aeItem childItem, aeItemRelation, aeItem parentItem"
						+ " where "
						+ " childItem.itm_id = ire_child_itm_id and ire_parent_itm_id = parentItem.itm_id and childItem.itm_type in ('CLASSROOM','SELFSTUDY','VIDEO') and childItem.itm_owner_ent_id = ? ";
			}
			if(tc_enabled) {
		    	sql +=" and childItem.itm_tcr_id in " + tcr_id_lst;
		    }
			if (title_code != null && title_code.trim().length() > 0
					&& search_type != null) {
				if (search_type.equals("ct_cc")) {
					sql = sql
							+ " and (lower(childItem.itm_title) like ? "
							+ escape
							+ " or lower(childItem.itm_code) like ? "
							+ escape + ")";
				}
				if (search_type.equals("ct")) {
					sql = sql
							+ " and lower(childItem.itm_title) like ? "
							+ escape;
				}
				if (search_type.equals("cc")) {
					sql = sql
							+ " and lower(childItem.itm_code) like ? "
							+ escape;
				}
				if (search_type.equals("mt")) {
					sql = sql
							+ " and childItem.itm_id in(select cos_itm_id from Course,ResourceContent,Resources where cos_res_id = rcn_res_id and rcn_res_id_content = res_id and res_type = '"
							+ dbResource.RES_TYPE_MOD
							+ "' and lower(res_title) like ? escape '/') ";

				}
			}
			if (dis_cos_type != null && dis_cos_type.equals("2")) {
				sql = sql + " and childItem.itm_create_run_ind != 1";
			}
			if (dis_cos_type != null && dis_cos_type.equals("3")) {
				sql = sql
						+ " and childItem.itm_id not in (select itm_id from aeItem where itm_create_run_ind = 1 and itm_content_def = '"
						+ aeItem.CHILD + "')";
			}
			sql = sql + " union";
			if(cwSQL.DBVENDOR_MYSQL.equalsIgnoreCase(cwSQL.getDbType())){
				sql = sql + " select itm_id as id, concat( itm_title , '(', itm_code,  ')' ) as title from aeItem " 						
						+ " where";
			}else{
				sql = sql + " select itm_id as id,itm_title " + ConcatOperator
						+ "'('" + ConcatOperator + " itm_code "
						+ ConcatOperator + " ')' as title from aeItem"
						+ " where";
			}
			sql = sql
					+ " itm_id not in (select ire_child_itm_id from aeItemRelation)  and itm_type in ('CLASSROOM','SELFSTUDY','VIDEO','MOBILE')  and itm_owner_ent_id = ?";
			if(tc_enabled) {
				sql +=" and itm_tcr_id in " + tcr_id_lst;
			}
			if (title_code != null && title_code.trim().length() > 0
					&& search_type != null) {
				if (search_type.equals("ct_cc")) {
					sql = sql + " and (lower(itm_title) like ? "
							+ escape + " or lower(itm_code) like ? "
							+ escape + ")";
				}
				if (search_type.equals("ct")) {
					sql = sql + " and lower(itm_title) like ? "
							+ escape + " ";
				}
				if (search_type.equals("cc")) {
					sql = sql + " and lower(itm_code) like ? "
							+ escape + " ";
				}
				if (search_type.equals("mt")) {
					sql = sql
							+ " and itm_id in(select cos_itm_id from Course,ResourceContent,Resources where cos_res_id = rcn_res_id and rcn_res_id_content = res_id and res_type = '"
							+ dbResource.RES_TYPE_MOD
							+ "' and lower(res_title) like ? "
							+ escape + ") ";
				}
			}
			if (dis_cos_type != null && dis_cos_type.equals("2")) {
				sql = sql + " and itm_create_run_ind != 1";
			}
			if (dis_cos_type != null && dis_cos_type.equals("3")) {
                     sql = sql + " and itm_id not in (select itm_id from aeItem where itm_create_run_ind = 1 and itm_content_def = '" + aeItem.CHILD + "')";
			}
			sql = sql + "  order by title";
			stmt = con.prepareStatement(sql);
			int index = 1;
			//if (dbproduct.indexOf("db2") >= 0) {
				//stmt.setLong(index++, root_ent_id);
			////stmt.setLong(index++, root_ent_id);
			//} else {
			
				stmt.setLong(index++, root_ent_id);
				if (title_code != null && title_code.trim().length() > 0
						&& search_type != null) {
					stmt.setString(index++, "%" + title_code.trim().toLowerCase() + "%");
					if (search_type.equals("ct_cc")) {
						stmt.setString(index++, "%" + title_code.trim().toLowerCase() + "%");
					}
				}

				stmt.setLong(index++, root_ent_id);
                if(title_code != null && title_code.trim().length()>0 && search_type != null) {
					stmt.setString(index++, "%" + title_code.trim().toLowerCase() + "%");
					if (search_type.equals("ct_cc")) {
						stmt.setString(index++, "%" + title_code.trim().toLowerCase() + "%");
					}
				}
			//}
                
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				HashMap itm = new HashMap();
				itm.put("ID", rs.getString("id"));
				itm.put("TITLE", rs.getString("title"));
				System.out.println("id:"+rs.getString("id"));
				System.out.println("title:"+rs.getString("title"));
				itm_list.add(itm);
			}
			return itm_list;
        }  
        finally{
			if (stmt != null) {
				stmt.close();
			}
		}

	}
    
    private List getItmMods(Connection con, String dis_mod_type, String sel_mod_status, long cos_id) throws SQLException {
        List itm_list = new ArrayList();
        PreparedStatement stmt = null;
        try{
            boolean is_cos_level_content_cos = isCosLevContentCos (con, cos_id);
            String sql = "select res_id, res_status from resources ,resourcecontent" +
                          " where res_id = rcn_res_id_content and rcn_res_id = ? and res_type = '" + dbResource.RES_TYPE_MOD + "'";
            if(dis_mod_type != null && dis_mod_type.equals("3")){
                sql = sql + " and res_subtype in ('" + dbModule.MOD_TYPE_TST + "','" + dbModule.MOD_TYPE_DXT + "')";
            }
            if(sel_mod_status != null && sel_mod_status.length() > 0){
                sql = sql + "and res_status = '" + sel_mod_status + "'";
            }
            
            stmt = con.prepareStatement(sql);
            stmt.setLong(1,cos_id);
            ResultSet rs = stmt.executeQuery();
            while( rs.next() ){
                HashMap mod = new HashMap();
                mod.put("ID",rs.getString("res_id"));
                if(is_cos_level_content_cos){
                    mod.put("STATUS","");
                }else{
                    mod.put("STATUS",rs.getString("res_status"));
                }
                
                itm_list.add(mod);
            }
            if(sel_mod_status != null && sel_mod_status.length() > 0 && is_cos_level_content_cos){
                itm_list.removeAll(itm_list);
            }
            return itm_list;
        }  
        finally{
            if(stmt != null){
                stmt.close();
            }
        } 
        
    }
    
    private  boolean isCosLevContentCos (Connection con,long cos_id) throws SQLException{
        boolean result = false;
        boolean create_run = false;
        boolean run = false;
        boolean apply = false;
        String content_def="";
        String SQL = "SELECT itm_create_run_ind, itm_run_ind,itm_apply_ind, itm_content_def FROM aeItem, course WHERE itm_id = cos_itm_id and cos_res_id = ? ";
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, cos_id);

        ResultSet rs = stmt.executeQuery();
        if (rs.next()){
            create_run = rs.getBoolean("itm_create_run_ind");
            run = rs.getBoolean("itm_run_ind");
            apply = rs.getBoolean("itm_apply_ind");
            content_def=rs.getString("itm_content_def");
        }
        if(create_run && !run && !apply && content_def!=null && content_def.equalsIgnoreCase("PARENT")){
            result=true;
        }
        stmt.close();
        return result;
    }
    public String strReplace(String soursestr, String oldstr, String newstr, int start, int end){
        int index = soursestr.indexOf(oldstr, start);
        int start_cut = 0;
        StringBuffer temp = new StringBuffer();       
        while(index >= 0 && index < end) {
            temp.append(soursestr.substring(start_cut,index)).append(newstr);
            start_cut = index + oldstr.length();
            start = start_cut;
            index = soursestr.indexOf(oldstr, start);
        }
        temp.append(soursestr.substring(start_cut,soursestr.length()));
        return temp.toString();
    }

	public static List<Resources> getResourcesByModId(Connection con, long mod_id) throws SQLException {
		List<Resources> params = new ArrayList<Resources>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String sql = "select res_id, res_title from Resources"
					+ " where res_mod_res_id_test = ? and res_subtype = ?"
					+ " order by res_id";
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setLong(index++, mod_id);
			stmt.setString(index++, "MC");
			rs = stmt.executeQuery();
			while(rs.next()) {
				Resources resources = new Resources();
				resources.setRes_id(rs.getInt("res_id"));
				resources.setRes_title(rs.getString("res_title"));
				params.add(resources);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
            if(stmt != null){
                stmt.close();
            }
        }
		return params;
	}

}
