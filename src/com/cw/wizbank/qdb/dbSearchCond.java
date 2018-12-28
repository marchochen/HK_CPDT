package com.cw.wizbank.qdb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import com.cw.wizbank.accesscontrol.AcTrainingCenter;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.db.DbTrainingCenter;
import com.cw.wizbank.db.view.ViewTrainingCenter;
import com.cw.wizbank.quebank.quecontainer.DynamicScenarioQue;
import com.cw.wizbank.quebank.quecontainer.FixedScenarioQue;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwPagination;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;


public class dbSearchCond
{
    public static final String RES_TYPE_QUE     = "QUE";
    public static final String RES_TYPE_MOD     = "MOD";
    public static final String RES_TYPE_COS     = "COS";
    public static final String SEARCH_RESULT_ID = "RESULT_ID";
    public static final String SEARCH_KEY       = "SEARCH_KEY";
    public static final String SEARCH_ORDER     = "SEARCH_ORDER";
//    public static final String SEARCH_FOR_TITLE = "RESULT_FOR";

    public Timestamp search_create_time_after;
    public Timestamp search_create_time_before;
    public Timestamp search_update_time_after;
    public Timestamp search_update_time_before;
    public long search_id;
    public long search_id_before;
    public long search_id_after;
    public float search_dur_before;
    public float search_dur_after;
    public int search_diff_before;
    public int search_diff_after;
    public String search_privilege;
    public String search_status;
    public String[] search_type;
    public String[] search_sub_type;
    public String search_owner;
    public String search_key;
    public String search_order;
    public String search_lang;
    public String search_for;

    public String[] search_title;
    public String[] search_desc;
    public String search_que_type;
    public String search_mod_type;
    public int search_start_index;
    public int search_items_per_page;
    public int[] search_diff_list;

	public long[] search_obj_id;
	public long search_owner_ent_id;

        dbSearchCond()
        {
            search_title = null;
            search_title = new String[10];

            search_desc = null;
            search_desc = new String[10];
        }


    public static String searchAsXML(HttpSession sess, Connection con, dbSearchCond dbsearch, int page, int page_size, loginProfile prof, boolean tc_enabled)
        throws qdbException, qdbErrMessage
    {
       try {
            String tempStat;
            String DELIMITER = " ";
            String SQL = "";
			StringBuffer SQLTotal = new StringBuffer();

            String result = "<?xml version=\"1.0\" encoding=\"" + dbUtils.ENC_UTF + "\" standalone=\"no\" ?>" + dbUtils.NEWL;
            result += "<search>" + dbUtils.NEWL;
            result += "<meta>" + dbUtils.NEWL; 
            result += prof.asXML() + dbUtils.NEWL;
            result += "<tc_enabled>" + tc_enabled + "</tc_enabled>" + dbUtils.NEWL;
            result += "</meta>" + dbUtils.NEWL;
            String dbproduct = cwSQL.getDbProductName();

//			boolean isAdmin = AccessControlWZB.isSysAdminRole(prof.current_role);
			Vector storeObjId = null;
			Vector sylId = dbSyllabus.getSybVec(con,prof.current_role,prof.root_ent_id);
			StringBuffer sylIds = new StringBuffer();
			sylIds.append("(0 ");
			for(int loop = 0; loop < sylId.size(); loop++){
				dbSyllabus dbs = (dbSyllabus)sylId.elementAt(loop);
				sylIds.append(",").append(dbs.syl_id);
			}
			sylIds.append(")");
			

//			if(!isAdmin) {
				if(tc_enabled) {
					if(AccessControlWZB.isRoleTcInd(prof.current_role)) {
						SQLTotal.append(" select distinct(oac_obj_id) from objectiveAccess ")
						   .append(" INNER JOIN objective on ( obj_id = oac_obj_id ) ")
						   .append(" inner join (select distinct(child.tcr_id) as tcr_id, child.tcr_title as tcr_title from tcTrainingCenter ancestor ")
						   .append(" inner join tcTrainingCenterOfficer on (tco_tcr_id = ancestor.tcr_id) ")
						   .append(" Left join tcRelation on (tcn_ancestor = ancestor.tcr_id) ")
						   .append(" inner join tcTrainingCenter child on (child.tcr_id = tcn_child_tcr_id or child.tcr_id = ancestor.tcr_id) ")
						   .append(" where tco_usr_ent_id =? ")
						   .append(" and child.tcr_status = ? ")
						   .append(" and ancestor.tcr_status = ?) A on (obj_tcr_id = tcr_id) ")
						   .append(" WHERE  1=1  ")
						   .append(" and obj_syl_id in ").append(sylIds) ;
					} else  {
						SQLTotal.append(" select distinct(oac_obj_id) from objectiveAccess ")
						   .append(" INNER JOIN objective on ( obj_id = oac_obj_id ) ")
						   .append(" WHERE  1=1  ")
						   .append(" and ( obj_tcr_id in ( select tcn_child_tcr_id from tcRelation where tcn_ancestor = ? )or obj_tcr_id = ? )")
						   .append(" and obj_syl_id in ").append(sylIds) ; 
					}
				} else {
					SQLTotal.append(" select distinct(oac_obj_id) ")
			        	.append(" from ObjectiveAccess, Objective ")
			        	.append(" where obj_id = oac_obj_id ")
			        	.append(" and obj_syl_id in ").append(sylIds) ;
				}
				PreparedStatement mystmt = con.prepareStatement(SQLTotal.toString());
				int index = 1;
				if(tc_enabled) {
					if(AccessControlWZB.isRoleTcInd(prof.current_role)) {
						mystmt.setLong(index++,prof.usr_ent_id);
						mystmt.setString(index++, DbTrainingCenter.STATUS_OK);
						mystmt.setString(index++, DbTrainingCenter.STATUS_OK);
					} else  {
						mystmt.setLong(index++, prof.my_top_tc_id);
						mystmt.setLong(index++, prof.my_top_tc_id);
					}
				}
				
				ResultSet rsview = mystmt.executeQuery();
				storeObjId = new Vector();
				while(rsview.next()){
					Long id = new Long(rsview.getLong("oac_obj_id"));
					storeObjId.addElement(id);
				}
				if(mystmt!=null) {
					mystmt.close();
				}
//			}

				
			SQL = " SELECT DISTINCT res_id RID, res_title RTITLE , res_type RTYPE, "
			    + " res_usr_id_owner ROWNER , res_status RSTATUS, "
	            + " res_subtype STYPE, res_difficulty , res_duration , res_privilege, "
	            + " res_create_date , res_upd_date from Resources,ResourceObjective,Objective, RegUser "
	            + " where  res_id = rob_res_id " 
                + " and  res_usr_id_owner = usr_id " 
                + " and (res_type = 'QUE' or res_type = 'GEN' or res_type = 'AICC' or res_type = 'ASM' or res_type = 'SCORM' or res_type = 'NETGCOK') "
                + " and rob_obj_id = obj_id " 
                + " and res_res_id_root is null and res_mod_res_id_test is null ";
             if(storeObjId != null && storeObjId.size()!=0){
				SQL += " and (";   
				for(int k = 0;k<storeObjId.size();k++){
					if(k==0){
						SQL += " (obj_ancester like '% " + storeObjId.elementAt(k) + " %'"  + " or obj_id = "+ storeObjId.elementAt(k)+")" ;
					}else{
						SQL += " OR (obj_ancester like '% " + storeObjId.elementAt(k)+" %'" + " or obj_id = "+storeObjId.elementAt(k)+")";
					}
				}
				SQL += ")";
             } else if(AccessControlWZB.isRoleTcInd(prof.current_role) || !tc_enabled){
				SQL += " and obj_syl_id in " + sylIds;
             } else {
            	SQL += " and obj_id = 0 ";
             }
               tempStat = "";
                Vector v_tmp_param = new Vector();
                Vector v_sql_param_value = new Vector();
                Vector v_sql_param_type = new Vector();
                for( int i=0; i< dbsearch.search_title.length; i++ ){
                    if ( dbsearch.search_title[i] != null && dbsearch.search_title[i].length() > 0 )
                    {
                        if ( ((dbsearch.search_title[i]).substring(0,1)).equalsIgnoreCase("+")){
                            SQL += " AND lower(res_title) like ? ";
                            v_sql_param_value.addElement("%" + dbsearch.search_title[i].substring(1,dbsearch.search_title[i].length()).toLowerCase() + "%");
                            v_sql_param_type.addElement(cwSQL.COL_TYPE_STRING);
                        }
                        else if ( ((dbsearch.search_title[i]).substring(0,1)).equalsIgnoreCase("-")){
                            SQL += " AND lower(res_title) not like ? ";
                            v_sql_param_value.addElement("%" + dbsearch.search_title[i].substring(1,dbsearch.search_title[i].length()).toLowerCase() + "%");
                            v_sql_param_type.addElement(cwSQL.COL_TYPE_STRING);
                        }else{
                            if ( tempStat == "" ){
                                tempStat += " lower(res_title) like ? ";
                            }else{
                                tempStat += " OR lower(res_title) like ?  ";
                            }
                            v_tmp_param.addElement("%" + dbsearch.search_title[i].toLowerCase() + "%");
                        }
                    }
                }
                if( tempStat != "" ){
                    SQL += "AND ( " + tempStat + " ) ";
                    for (int i=0; i<v_tmp_param.size();i++){
                        v_sql_param_value.addElement((String)v_tmp_param.elementAt(i));
                        v_sql_param_type.addElement(cwSQL.COL_TYPE_STRING);
                    }
                }

                tempStat = "";
                v_tmp_param = new Vector();
                String phrase;
                for( int i=0; i< dbsearch.search_desc.length; i++ ){
                    if ( dbsearch.search_desc[i] != null && dbsearch.search_desc[i].length() > 0 ){
                        if ( ((dbsearch.search_desc[i]).substring(0,1)).equalsIgnoreCase("+")){
                            phrase = "%" + dbsearch.search_desc[i].substring(1,dbsearch.search_desc[i].length()) + "%";
                            SQL += " AND lower(res_desc) LIKE ? ";
                            v_sql_param_value.addElement(phrase.toLowerCase());
                            v_sql_param_type.addElement(cwSQL.COL_TYPE_STRING);
                        }else if ( ((dbsearch.search_desc[i]).substring(0,1)).equalsIgnoreCase("-")){
                            phrase = "%" + dbsearch.search_desc[i].substring(1,dbsearch.search_desc[i].length()) + "%";
                            SQL += " AND lower(res_desc) NOT LIKE ? ";
                            v_sql_param_value.addElement(phrase.toLowerCase());
                            v_sql_param_type.addElement(cwSQL.COL_TYPE_STRING);
                        }else{
                            if ( tempStat != "" )
                                tempStat += " OR ";
                            phrase = "%" + dbsearch.search_desc[i] + "%";
                            tempStat += " lower(res_desc) LIKE ? ";
                            v_tmp_param.addElement(phrase.toLowerCase());
                        }
                    }
                }
                if( tempStat != "" ){
                    SQL += "AND ( " + tempStat + " ) ";
                    for (int i=0; i<v_tmp_param.size();i++){
                        v_sql_param_value.addElement((String)v_tmp_param.elementAt(i));
                        v_sql_param_type.addElement(cwSQL.COL_TYPE_STRING);
                    }
                }

            if(dbsearch.search_diff_list != null && dbsearch.search_diff_list.length>0) {
                SQL += " AND res_difficulty IN (";
                for(int i=0; i<dbsearch.search_diff_list.length; i++) {
                    if(i==0) {
                        SQL += "?";
                    } else {
                        SQL += ",?";
                    }
                    v_sql_param_value.addElement(new Integer(dbsearch.search_diff_list[i]));
                    v_sql_param_type.addElement(cwSQL.COL_TYPE_INTEGER);
                }
                SQL += ")";
            }

            if (dbsearch.search_diff_before != 0){
                SQL += " AND res_difficulty <= ? ";
                v_sql_param_value.addElement(new Integer(dbsearch.search_diff_before));
                v_sql_param_type.addElement(cwSQL.COL_TYPE_INTEGER);
            }

            if (dbsearch.search_diff_after != 0){
                SQL += " AND res_difficulty >= ? ";
                v_sql_param_value.addElement(new Integer(dbsearch.search_diff_after));
                v_sql_param_type.addElement(cwSQL.COL_TYPE_INTEGER);
            }
            if (dbsearch.search_id != 0 ){
                SQL += " AND res_id = ? ";
                v_sql_param_value.addElement(new Long(dbsearch.search_id));
                v_sql_param_type.addElement(cwSQL.COL_TYPE_LONG);
            }
            if (dbsearch.search_id_after != 0 ){
                SQL += " AND res_id >= ? ";
                v_sql_param_value.addElement(new Long(dbsearch.search_id_after));
                v_sql_param_type.addElement(cwSQL.COL_TYPE_LONG);
            }
            if (dbsearch.search_id_before != 0 ){
                SQL += " AND res_id <= ? ";
                v_sql_param_value.addElement(new Long(dbsearch.search_id_before));
                v_sql_param_type.addElement(cwSQL.COL_TYPE_LONG);
            }
            if (dbsearch.search_dur_after != 0){
                SQL += " AND res_duration >= ? ";
                v_sql_param_value.addElement(new Float(dbsearch.search_dur_after));
                v_sql_param_type.addElement(cwSQL.COL_TYPE_FLOAT);
            }
            if (dbsearch.search_dur_before != 0){
                SQL += " AND res_duration <= ? ";
                v_sql_param_value.addElement(new Float(dbsearch.search_dur_before));
                v_sql_param_type.addElement(cwSQL.COL_TYPE_FLOAT);
            }
            if (dbsearch.search_owner != null && dbsearch.search_owner.length() > 0){
                SQL += " AND lower(usr_display_bil) like ? ";
                v_sql_param_value.addElement("%" + dbsearch.search_owner.toLowerCase() + "%");
                v_sql_param_type.addElement(cwSQL.COL_TYPE_STRING);
            }
            if (dbsearch.search_privilege != null && dbsearch.search_privilege.length() > 0){
                SQL += " AND res_privilege = ? ";
                v_sql_param_value.addElement(dbsearch.search_privilege);
                v_sql_param_type.addElement(cwSQL.COL_TYPE_STRING);
            }
            if (dbsearch.search_status != null && dbsearch.search_status.length() > 0){
                SQL += " AND res_status = ? ";
                v_sql_param_value.addElement(dbsearch.search_status);
                v_sql_param_type.addElement(cwSQL.COL_TYPE_STRING);
            }
            
			if(dbsearch.search_type!=null && dbsearch.search_type.length > 0 && dbsearch.search_type[0] !=null && dbsearch.search_type[0].length() > 0) {
				SQL += " AND ( " ;
				for (int i=0;i<dbsearch.search_type.length;i++) {
					if (i != 0)
						SQL += " OR " ;

						SQL += " res_type = ? ";
						v_sql_param_value.addElement(dbsearch.search_type[i]);
						v_sql_param_type.addElement(cwSQL.COL_TYPE_STRING);
				}
				SQL += " ) ";
			}
            else{
                SQL += " AND  ( res_type = ? or res_type = ? or res_type = ? or res_type = ? or res_type = ? or res_type = ?)";
                v_sql_param_value.addElement("QUE");
                v_sql_param_type.addElement(cwSQL.COL_TYPE_STRING);
                v_sql_param_value.addElement("GEN");
                v_sql_param_type.addElement(cwSQL.COL_TYPE_STRING);
                v_sql_param_value.addElement("AICC");
				v_sql_param_type.addElement(cwSQL.COL_TYPE_STRING);
				v_sql_param_value.addElement("ASM");
				v_sql_param_type.addElement(cwSQL.COL_TYPE_STRING);
				v_sql_param_value.addElement("SCORM");
				v_sql_param_type.addElement(cwSQL.COL_TYPE_STRING);
				v_sql_param_value.addElement("NETGCOK");
				v_sql_param_type.addElement(cwSQL.COL_TYPE_STRING);
            }
            
			if(dbsearch.search_sub_type!=null && dbsearch.search_sub_type.length > 0 && dbsearch.search_sub_type[0] !=null && dbsearch.search_sub_type[0].length() > 0) {
				SQL += " AND ( " ;
				for (int i=0;i<dbsearch.search_sub_type.length;i++) {
					if (i != 0)
						SQL += " OR " ;
	
						SQL += " res_subtype = ? ";
						v_sql_param_value.addElement(dbsearch.search_sub_type[i]);
						v_sql_param_type.addElement(cwSQL.COL_TYPE_STRING);
				}
				SQL += " ) ";
			}
            
            
            if (dbsearch.search_create_time_after != null ){
                SQL += " AND res_create_date >= ? ";
                v_sql_param_value.addElement(dbsearch.search_create_time_after);
                v_sql_param_type.addElement(cwSQL.COL_TYPE_TIMESTAMP);
            }
            if (dbsearch.search_create_time_before != null ){
                SQL += " AND res_create_date <= ? ";
                v_sql_param_value.addElement(dbsearch.search_create_time_before);
                v_sql_param_type.addElement(cwSQL.COL_TYPE_TIMESTAMP);
            }
            if (dbsearch.search_update_time_after != null ){
                SQL += " AND res_upd_date >= ? ";
                v_sql_param_value.addElement(dbsearch.search_update_time_after);
                v_sql_param_type.addElement(cwSQL.COL_TYPE_TIMESTAMP);
            }
            if (dbsearch.search_update_time_before != null ){
                SQL += " AND res_upd_date <= ? ";
                v_sql_param_value.addElement(dbsearch.search_update_time_before);
                v_sql_param_type.addElement(cwSQL.COL_TYPE_TIMESTAMP);
            }
            if (dbsearch.search_lang != null && dbsearch.search_lang.length() > 0){
                SQL += " AND res_lan = ? ";
                v_sql_param_value.addElement(dbsearch.search_lang);
                v_sql_param_type.addElement(cwSQL.COL_TYPE_STRING);
            }
            SQL += " AND obj_tcr_id in (" + ViewTrainingCenter.ta_fliter + ")";
            SQL += " ORDER BY " + dbsearch.search_key + " " + dbsearch.search_order;
            PreparedStatement stmt = con.prepareStatement(SQL);

             index = 1;
            for (int i=0; i<v_sql_param_value.size(); i++){
                String colType = (String)v_sql_param_type.elementAt(i);
                //System.out.println(colType + " "+ v_sql_param_value.elementAt(i));
                if(colType.equals(cwSQL.COL_TYPE_LONG)) {
                    //long lvalue = Long.parseLong((String)this.vColValue.elementAt(i));
                    long lvalue = ((Long)v_sql_param_value.elementAt(i)).longValue();
                    stmt.setLong(index++, lvalue);
                }
                else if(colType.equals(cwSQL.COL_TYPE_STRING)) {
                    String svalue;
                    if (v_sql_param_value.elementAt(i)==null)
                        svalue = null;
                    else
                        svalue = (String)v_sql_param_value.elementAt(i);

                    stmt.setString(index++, svalue);
                }
                else if(colType.equals(cwSQL.COL_TYPE_TIMESTAMP)) {
                    Timestamp tsvalue;
                    if (v_sql_param_value.elementAt(i)==null){
                        tsvalue = null;
                    }else{
                        tsvalue = (Timestamp)v_sql_param_value.elementAt(i);
                    }
                    stmt.setTimestamp(index++, tsvalue);
                }
                else if(colType.equals(cwSQL.COL_TYPE_INTEGER)) {
                    int ivalue = ((Integer)v_sql_param_value.elementAt(i)).intValue();
                    stmt.setInt(index++, ivalue);
                } else if(colType.equals(cwSQL.COL_TYPE_FLOAT)) {
                      float fvalue = ((Float)v_sql_param_value.elementAt(i)).floatValue();
                      stmt.setFloat(index++, fvalue);
                }else{
                	CommonLog.info("Warning, unknown datatype: " + colType);
                }
            }
            stmt.setLong(index++, prof.usr_ent_id);
            ResultSet rs = stmt.executeQuery();

            String result_id="";
            
            /**
             * Pagination parameter init
             */
            if (page == 0) {
                page = 1;
            }
            if (page_size == 0) {
                page_size = 10;
            }
            int counter = 0;
            int start = page_size * (page-1);
            
            result += "<item_list>" + dbUtils.NEWL;
            
            while (rs.next())
            {
            	Long RID = new Long(rs.getLong("RID"));
                result_id += RID.toString() + ",";
            	
            	if (counter >= start && counter < start+page_size) {
            		result += "<item id=\"" + RID
                            + "\" index=\"" + (counter+1)
                            + "\" type=\"" + dbUtils.esc4XML(rs.getString("RTYPE"))
                            + "\" status=\"" + dbUtils.esc4XML(rs.getString("RSTATUS"))
                            + "\" owner=\"" + dbUtils.esc4XML(dbRegUser.usrId2SteUsrId(con,dbUtils.esc4XML(rs.getString("ROWNER"))))
                            + "\" sub_type=\"" + dbUtils.esc4XML(rs.getString("STYPE"))
                            + "\"><title>" + dbUtils.esc4XML(rs.getString("RTITLE"))+"</title>";

            		String content = dbUtils.esc4XML(dbResource.getResDesc(con,RID));
                    if ( content != null )
                        result +="<content>" + content + "</content>";

                    result += "</item>" + dbUtils.NEWL;
            	}
            	counter++;
            }
            
            //Build pagination result
	    	cwPagination pagn = new cwPagination();
	        pagn.totalRec = counter;
	        pagn.totalPage = (int)Math.ceil((float)counter/page_size);
	        pagn.pageSize = page_size;
	        pagn.curPage = page;
	        pagn.ts = null;
	        result +=pagn.asXML();
	        
            result += "</item_list>" + dbUtils.NEWL;
            result += "</search>" + dbUtils.NEWL;
            
            stmt.close();
            sess.setAttribute(SEARCH_RESULT_ID,result_id);
            sess.setAttribute(SEARCH_KEY,dbsearch.search_key);
            sess.setAttribute(SEARCH_ORDER,dbsearch.search_order);

            return result;
        } catch(SQLException e) {
        	CommonLog.error(e.getMessage(),e);
            throw new qdbException("SQL Error: " + e.getMessage());
        }

    }


	public static String simple_searchAsXML(HttpSession sess, Connection con, dbSearchCond dbsearch, int page, int page_size, long start, long end, loginProfile prof, boolean tc_enabled)
			throws qdbException, qdbErrMessage
		{
		   try {
				String res_title_lan, res_desc_lan, titleOR, descOR, titleAND, descAND;
				Vector RIDVEC = new Vector();
				String DELIMITER = " ";
				//String gpIds = prof.usrGroupsList();
				String SQL = "";
				StringBuffer SQLTotal =new StringBuffer();

				String result = "<?xml version=\"1.0\" encoding=\"" + dbUtils.ENC_UTF + "\" standalone=\"no\" ?>" + dbUtils.NEWL;
				result += "<search>" + dbUtils.NEWL;
				result +="<meta>" + dbUtils.NEWL;
				result += prof.asXML() + dbUtils.NEWL;
				result +="<tc_enabled>" + tc_enabled +"</tc_enabled>"+dbUtils.NEWL;
				result +="</meta>";
				
		
//				boolean isAdmin = AccessControlWZB.isSysAdminRole(prof.current_role);
//				//如果不是与培训中心相关但是也有资源管理权限的角色可以看成是管理员角色
//				if(!isAdmin && !AccessControlWZB.isRoleTcInd(prof.current_role) 
//						&& AccessControlWZB.hasRolePrivilege( prof.current_role,  AclFunction.FTN_AMD_RES_MAIN)){
//					isAdmin = true;
//				}
				Vector storeObjId = null;
				Vector sylId = dbSyllabus.getSybVec(con,prof.current_role,prof.root_ent_id);
				StringBuffer sylIds = new StringBuffer();
				sylIds.append("(0 ");
				for(int loop = 0; loop < sylId.size(); loop++){
					dbSyllabus dbs = (dbSyllabus)sylId.elementAt(loop);
					sylIds.append(",").append(dbs.syl_id);
				}
				sylIds.append(")"); 
				AcTrainingCenter actc = new AcTrainingCenter(con);
//				if(!isAdmin) {
					if(tc_enabled) {
						if(AccessControlWZB.isRoleTcInd(prof.current_role)) {
							SQLTotal.append(" select distinct(oac_obj_id) from objectiveAccess ")
							   .append(" INNER JOIN objective on ( obj_id = oac_obj_id ) ")
							   .append(" inner join (select distinct(child.tcr_id) as tcr_id, child.tcr_title as tcr_title from tcTrainingCenter ancestor ")
							   .append(" inner join tcTrainingCenterOfficer on (tco_tcr_id = ancestor.tcr_id) ")
							   .append(" Left join tcRelation on (tcn_ancestor = ancestor.tcr_id) ")
							   .append(" inner join tcTrainingCenter child on (child.tcr_id = tcn_child_tcr_id or child.tcr_id = ancestor.tcr_id) ")
							   .append(" where tco_usr_ent_id =? ")
							   .append(" and child.tcr_status = ? ")
							   .append(" and ancestor.tcr_status = ?) A on (obj_tcr_id = tcr_id) ")
							   .append(" WHERE  1=1  ")
							   .append(" and obj_syl_id in ").append(sylIds) ;
						} else {
							SQLTotal.append(" select distinct(oac_obj_id) from objectiveAccess ")
							   .append(" INNER JOIN objective on ( obj_id = oac_obj_id ) ")
							   .append(" WHERE  1=1 ")
							   .append(" and ( obj_tcr_id in ( select tcn_child_tcr_id from tcRelation where tcn_ancestor = ? )or obj_tcr_id = ? )")
							   .append(" and obj_syl_id in ").append(sylIds) ; 
						}
					} else {
						SQLTotal.append(" select distinct(oac_obj_id) ")
				        	.append(" from ObjectiveAccess, Objective ")
				        	.append(" where  obj_id = oac_obj_id ")
				        	.append(" and obj_syl_id in ").append(sylIds) ;
					}
					PreparedStatement mystmt = con.prepareStatement(SQLTotal.toString());
					int index = 1;
					
					if(tc_enabled) {
						if(AccessControlWZB.isRoleTcInd(prof.current_role)) {
							mystmt.setLong(index++,prof.usr_ent_id);
							mystmt.setString(index++, DbTrainingCenter.STATUS_OK);
							mystmt.setString(index++, DbTrainingCenter.STATUS_OK);
						} else  {
							mystmt.setLong(index++, prof.my_top_tc_id);
							mystmt.setLong(index++, prof.my_top_tc_id);
						}
						
					}
					
					ResultSet rsview = mystmt.executeQuery();
					storeObjId = new Vector();
					while(rsview.next()){
						Long id = new Long(rsview.getLong("oac_obj_id"));
						storeObjId.addElement(id);
					}
					if(mystmt!=null) {
						mystmt.close();
					}
//				}
					SQL = " SELECT DISTINCT res_id RID, res_title RTITLE , res_type RTYPE, "
						+ " res_desc RDESC , res_usr_id_owner ROWNER , res_status RSTATUS, " 
						+ " res_usr_id_owner ROWNER , res_status RSTATUS, " 
						+ " res_subtype STYPE  from Resources,ResourceObjective,Objective "
						+ " where  res_id = rob_res_id " 
						+ " and (res_type = 'QUE' or res_type = 'GEN' or res_type = 'AICC' or res_type = 'ASM' or res_type = 'SCORM' or res_type = 'NETGCOK') "
						+ " and rob_obj_id = obj_id " 
						+ " AND res_res_id_root is null AND res_mod_res_id_test is null ";

                    if(storeObjId != null && storeObjId.size()!=0){
						SQL += " and( ";
						for(int k = 0;k<storeObjId.size();k++){
							if(k==0){
								SQL += " (obj_ancester like '% " + storeObjId.elementAt(k) + " %'"  + " or obj_id = "+ storeObjId.elementAt(k)+")" ;
							}else{
								SQL += " OR (obj_ancester like '% " + storeObjId.elementAt(k)+" %'" + " or obj_id = "+storeObjId.elementAt(k)+")";
							}
					     }
					     SQL+= ")";
                    } else {
                    	SQL += " and obj_id = 0 ";
                    }
						
					titleOR = "";

					Vector v_sql_param_value = new Vector();
					String phrase;
					
					for( int i=0; i< dbsearch.search_title.length; i++ )
						if ( dbsearch.search_title[i] != null && dbsearch.search_title[i].length() > 0 )
						{
							String cond = dbsearch.search_title[i].substring(0,1);
							if ( cond.equalsIgnoreCase("+") || cond.equalsIgnoreCase("-")){
								phrase = dbsearch.search_title[i].substring(1,dbsearch.search_title[i].length());
								if (cond.equalsIgnoreCase("+")){
									SQL += " AND ( lower(res_title) like ? OR lower(res_desc) like ?  OR lower(res_id) LIKE ? ) ";
									v_sql_param_value.addElement("%" + phrase.toLowerCase() + "%");
									v_sql_param_value.addElement("%" + phrase.toLowerCase() + "%");
									v_sql_param_value.addElement("%" + phrase.toLowerCase() + "%");
								}else{
									SQL += " AND ( lower(res_title) NOT LIKE ? AND lower(res_desc) NOT LIKE ?  AND lower(res_id) NOT LIKE ? ) ";
									v_sql_param_value.addElement("%" + phrase.toLowerCase() + "%");
									v_sql_param_value.addElement("%" + phrase.toLowerCase() + "%");
									v_sql_param_value.addElement("%" + phrase.toLowerCase() + "%");
								}
							}else{
								phrase = dbsearch.search_title[i];

								if ( titleOR == "" )
									titleOR += " AND ( ";
								else
									titleOR += " OR ";

								titleOR += " lower(res_title) LIKE ? OR lower(res_desc) LIKE ?  OR lower(res_id) LIKE ? ";
								v_sql_param_value.addElement("%" + phrase.toLowerCase() + "%");
								v_sql_param_value.addElement("%" + phrase.toLowerCase() + "%");
								v_sql_param_value.addElement("%" + phrase.toLowerCase() + "%");
							}
						}
					if( titleOR != "" )
						titleOR += " ) ";

					SQL += titleOR;
					SQL += " ORDER BY res_title ";
					CommonLog.debug(SQL);
					PreparedStatement stmt = con.prepareStatement(SQL);
					 index = 1;
					String svalue;
					for (int i=0; i<v_sql_param_value.size(); i++){
						if (v_sql_param_value.elementAt(i)==null)
							svalue = null;
						else
							svalue = (String)v_sql_param_value.elementAt(i);

						stmt.setString(index++, svalue);
					}
			 
					CommonLog.debug("before exe");
					ResultSet rs = stmt.executeQuery();
					CommonLog.debug("after exe");
			 
					String RTITLE, RDESC, RTYPE, ROWNER, RSTATUS, MTYPE, STYPE, result_id="";
					Vector RTITLEVEC = new Vector();
					Vector RDESCVEC = new Vector();
					Vector RTYPEVEC = new Vector();
					Vector ROWNERVEC = new Vector();
					Vector RSTATUSVEC = new Vector();
					Vector STYPEVEC = new Vector();
					int counter = 0;
					
					 if (page == 0) {
			                page = 1;
			            }
		            if (page_size == 0) {
		                page_size = 10;
		            }
		            
		            start = page_size * (page-1);
					while (rs.next())
					{
						Long RID = new Long(rs.getLong("RID"));
						RIDVEC.addElement(RID);
						result_id += RID.toString() + ",";
						if(counter >= start && counter < start+page_size){
							//if(counter < dbsearch.search_items_per_page) {
								RTITLE = dbUtils.esc4XML(rs.getString("RTITLE"));
								RTITLEVEC.addElement(RTITLE);
	
								//RDESC = dbUtils.esc4XML(rs.getString("RDESC"));
								RDESC = dbUtils.esc4XML(dbResource.getResDesc(con,RID.longValue()));
								RDESCVEC.addElement(RDESC);
	
								RTYPE = dbUtils.esc4XML(rs.getString("RTYPE"));
								RTYPEVEC.addElement(RTYPE);
	
								STYPE = dbUtils.esc4XML(rs.getString("STYPE"));
								STYPEVEC.addElement(STYPE);
	
								ROWNER = dbUtils.esc4XML(rs.getString("ROWNER"));
								ROWNERVEC.addElement(ROWNER);
	
								RSTATUS = dbUtils.esc4XML(rs.getString("RSTATUS"));
								RSTATUSVEC.addElement(RSTATUS);
							//}
						}
						counter++;
				}
				stmt.close();
					sess.setAttribute(SEARCH_RESULT_ID,result_id);
					sess.setAttribute(SEARCH_KEY,"res_title");
					sess.setAttribute(SEARCH_ORDER,"asc");
					String search_title = "";
					if(dbsearch.search_title.length>0){
						search_title=dbsearch.search_title[0];
					}
					result += "<item_list total=\"" + RIDVEC.size() + "\" search_title=\""+cwUtils.esc4XML(search_title)+ "\">" + dbUtils.NEWL;

					for(int i=0; i<RIDVEC.size() && i < RTYPEVEC.size() ; i++)
					{    result += "<item id=\"" + ((Long)RIDVEC.elementAt(i)).toString()
								+ "\" index=\"" + (i+1)
								+ "\" type=\"" + RTYPEVEC.elementAt(i)
								+ "\" status=\"" + RSTATUSVEC.elementAt(i)
								+ "\" owner=\"" + dbUtils.esc4XML(dbRegUser.usrId2SteUsrId(con,(String) ROWNERVEC.elementAt(i)))
								+ "\" sub_type=\"" + STYPEVEC.elementAt(i)
								+ "\"><title>" + RTITLEVEC.elementAt(i) + "</title>";

						if ( RDESCVEC.elementAt(i) != null )
								result +="<content>" + RDESCVEC.elementAt(i) + "</content>";

						result += "</item>" + dbUtils.NEWL;
					}
					
					 //Build pagination result
			    	cwPagination pagn = new cwPagination();
			        pagn.totalRec = counter;
			        pagn.totalPage = (int)Math.ceil((float)counter/page_size);
			        pagn.pageSize = page_size;
			        pagn.curPage = page;
			        pagn.ts = null;
			        result +=pagn.asXML();
					
					
					result += "</item_list>" + dbUtils.NEWL;
				result += "</search>" + dbUtils.NEWL;
				return result;
			} catch(SQLException e) {
				throw new qdbException("SQL Error: " + e.getMessage());
			}
		 }
    
    public static String search_result(HttpSession sess, Connection con, dbSearchCond dbsearch, loginProfile prof)
        throws qdbException, qdbErrMessage
    {
       try {
        String[] result_id;
        String resId = "(0";
        result_id = dbUtils.split((String)sess.getAttribute(SEARCH_RESULT_ID), ",");
//        dbsearch.search_for = (String)sess.getAttribute(SEARCH_FOR_TITLE);
        for(int i=dbsearch.search_start_index;i<dbsearch.search_start_index+dbsearch.search_items_per_page && i<result_id.length;i++)
            resId += "," + result_id[i];
        resId +=")";

        String SQL = " SELECT DISTINCT res_id RID, res_title RTITLE , res_type RTYPE, "
                   //+ " res_desc RDESC , res_usr_id_owner ROWNER , res_status RSTATUS, "
                   + " res_usr_id_owner ROWNER , res_status RSTATUS, "
                   + " res_subtype STYPE"
                   + " FROM Resources "
                   + " WHERE res_id IN " + resId
                   + " ORDER BY " + (String)sess.getAttribute(SEARCH_KEY) + " " + (String)sess.getAttribute(SEARCH_ORDER);
        PreparedStatement stmt = con.prepareStatement(SQL);
        ResultSet rs = stmt.executeQuery();

        String result = "<?xml version=\"1.0\" encoding=\"" + dbUtils.ENC_UTF + "\" standalone=\"no\" ?>" + dbUtils.NEWL;
        result += "<search>" + dbUtils.NEWL;
        result += prof.asXML() + dbUtils.NEWL;

        String RTITLE, RDESC, RTYPE, ROWNER, RSTATUS, MTYPE, STYPE;
        Long RID;
        Vector RIDVEC = new Vector();
        Vector RTITLEVEC = new Vector();
        Vector RDESCVEC = new Vector();
        Vector RTYPEVEC = new Vector();
        Vector ROWNERVEC = new Vector();
        Vector RSTATUSVEC = new Vector();
        Vector STYPEVEC = new Vector();

        while (rs.next())
        {
            RID = new Long(rs.getLong("RID"));
            RIDVEC.addElement(RID);

            RTITLE = dbUtils.esc4XML(rs.getString("RTITLE"));
            RTITLEVEC.addElement(RTITLE);

            //RDESC = dbUtils.esc4XML(rs.getString("RDESC"));
            RDESC = dbUtils.esc4XML(dbResource.getResDesc(con,RID.longValue()));
            RDESCVEC.addElement(RDESC);

            RTYPE = dbUtils.esc4XML(rs.getString("RTYPE"));
            RTYPEVEC.addElement(RTYPE);

            STYPE = dbUtils.esc4XML(rs.getString("STYPE"));
            STYPEVEC.addElement(STYPE);

            ROWNER = dbUtils.esc4XML(rs.getString("ROWNER"));
            ROWNERVEC.addElement(ROWNER);

            RSTATUS = dbUtils.esc4XML(rs.getString("RSTATUS"));
            RSTATUSVEC.addElement(RSTATUS);
        }
        stmt.close();
        //result += "<item_list total=\"" + result_id.length + "\">" + dbUtils.NEWL;

        result += "<item_list total=\"" + result_id.length + "\">" + dbUtils.NEWL;

        //int LstIndex;
        for(int i=0; i < result_id.length - dbsearch.search_start_index && i < dbsearch.search_items_per_page && i < RIDVEC.size(); i++)
        {    result += "<item id=\"" + RIDVEC.elementAt(i)//result_id[ i + dbsearch.search_start_index ]
                    + "\" index=\"" + (dbsearch.search_start_index + i + 1)
                    + "\" type=\"" + RTYPEVEC.elementAt(i)
                    + "\" status=\"" + RSTATUSVEC.elementAt(i)
                    + "\" owner=\"" + dbUtils.esc4XML(dbRegUser.usrId2SteUsrId(con,(String) ROWNERVEC.elementAt(i)))
                    + "\" sub_type=\"" + STYPEVEC.elementAt(i)
                    + "\"><title>" + RTITLEVEC.elementAt(i) + "</title>";

            if ( RDESCVEC.elementAt(i) != null )
                    result +="<content>" + RDESCVEC.elementAt(i) + "</content>";

            result += "</item>" + dbUtils.NEWL;
        }
        result += "</item_list>" + dbUtils.NEWL;
        result += "</search>" + dbUtils.NEWL;

        return result;
    }catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

	/* no pagination */
    public static StringBuffer searchQueWDetails(Connection con, dbSearchCond dbsearch, loginProfile prof, String cur_stylesheet)
        throws qdbException, cwSysMessage, SQLException, cwException {
        StringBuffer SQL = new StringBuffer();
        StringBuffer xml = new StringBuffer();
        String gpIds = prof.usrGroupsList();
        Vector v_sql_param_value = new Vector();
        Vector v_sql_param_type = new Vector();
        Vector v_obj_id = new Vector();
        for (int i = 0; i < dbsearch.search_obj_id.length; i++) {
            v_obj_id.addElement(new Long(dbsearch.search_obj_id[i]));
            dbObjective dbobj = new dbObjective();
            dbobj.obj_id = dbsearch.search_obj_id[i];
            Vector vtChildObj = dbobj.getChildsObjId(con);
            for (int j = 0; j < vtChildObj.size(); j++) {
                v_obj_id.addElement((Long)vtChildObj.elementAt(j));
            }
        }
        SQL
            .append(" SELECT que_res_id, que_xml, que_score, que_type, que_int_count, que_prog_lang, que_media_ind, que_submit_file_ind ,  ")
            .append(" res_lan , res_title , res_desc , res_type , res_subtype , res_annotation , res_format , res_difficulty , res_duration , res_privilege , res_status , res_usr_id_owner , res_tpl_name , res_res_id_root , res_mod_res_id_test , res_upd_user , res_upd_date , res_src_type , res_src_link , res_instructor_name , res_instructor_organization ")
            .append(" FROM Resources , Question, RegUser, ResourceObjective ")
            .append(" WHERE que_res_id = res_id and usr_id = res_usr_id_owner ")
            .append(" AND res_res_id_root is null AND res_mod_res_id_test is null ")
            .append(" AND res_type = ? ")
            .append(" AND rob_res_id = res_id AND rob_obj_id in ")
            .append(cwUtils.vector2list(v_obj_id))
            .append(" AND res_id in (select rpm_res_id from ResourcePermission where rpm_ent_id IN ")
            .append(gpIds)
            .append(" AND (rpm_read = ? or rpm_write = ? or rpm_execute = ? ) )");

        v_sql_param_value.addElement("QUE");
        v_sql_param_type.addElement(cwSQL.COL_TYPE_STRING);
        v_sql_param_value.addElement(new Boolean(true));
        v_sql_param_type.addElement(cwSQL.COL_TYPE_BOOLEAN);
        v_sql_param_value.addElement(new Boolean(true));
        v_sql_param_type.addElement(cwSQL.COL_TYPE_BOOLEAN);
        v_sql_param_value.addElement(new Boolean(true));
        v_sql_param_type.addElement(cwSQL.COL_TYPE_BOOLEAN);

        if (dbsearch.search_owner_ent_id != 0) {
            SQL.append(" AND usr_ent_id = ? ");
            v_sql_param_value.addElement(new Long(dbsearch.search_owner_ent_id));
            v_sql_param_type.addElement(cwSQL.COL_TYPE_LONG);
        }
        if (dbsearch.search_privilege != null && dbsearch.search_privilege.length() > 0) {
            SQL.append(" AND res_privilege = ? ");
            v_sql_param_value.addElement(dbsearch.search_privilege);
            v_sql_param_type.addElement(cwSQL.COL_TYPE_STRING);
        }
        if (dbsearch.search_status != null && dbsearch.search_status.length() > 0) {
            SQL.append(" AND res_status = ? ");
            v_sql_param_value.addElement(dbsearch.search_status);
            v_sql_param_type.addElement(cwSQL.COL_TYPE_STRING);
        }

        if (dbsearch.search_sub_type != null
            && dbsearch.search_sub_type.length > 0
            && dbsearch.search_sub_type[0] != null
            && dbsearch.search_sub_type[0].length() > 0) {
            SQL.append(" AND ( ");
            for (int i = 0; i < dbsearch.search_sub_type.length; i++) {
                if (i != 0)
                    SQL.append(" OR ");

                SQL.append(" res_subtype = ? ");
                v_sql_param_value.addElement(dbsearch.search_sub_type[i]);
                v_sql_param_type.addElement(cwSQL.COL_TYPE_STRING);
            }
            SQL.append(" ) ");
        }

        if (dbsearch.search_create_time_after != null) {
            SQL.append(" AND res_create_date >= ? ");
            v_sql_param_value.addElement(dbsearch.search_create_time_after);
            v_sql_param_type.addElement(cwSQL.COL_TYPE_TIMESTAMP);
        }
        if (dbsearch.search_create_time_before != null) {
            SQL.append(" AND res_create_date <= ? ");
            v_sql_param_value.addElement(dbsearch.search_create_time_before);
            v_sql_param_type.addElement(cwSQL.COL_TYPE_TIMESTAMP);
        }
        if (dbsearch.search_update_time_after != null) {
            SQL.append(" AND res_upd_date >= ? ");
            v_sql_param_value.addElement(dbsearch.search_update_time_after);
            v_sql_param_type.addElement(cwSQL.COL_TYPE_TIMESTAMP);
        }
        if (dbsearch.search_update_time_before != null) {
            SQL.append(" AND res_upd_date <= ? ");
            v_sql_param_value.addElement(dbsearch.search_update_time_before);
            v_sql_param_type.addElement(cwSQL.COL_TYPE_TIMESTAMP);
        }
        PreparedStatement stmt = con.prepareStatement(SQL.toString());

        int index = 1;
        for (int i = 0; i < v_sql_param_value.size(); i++) {
            String colType = (String)v_sql_param_type.elementAt(i);
            if (colType.equals(cwSQL.COL_TYPE_LONG)) {
                long lvalue = ((Long)v_sql_param_value.elementAt(i)).longValue();
                stmt.setLong(index++, lvalue);
            } else if (colType.equals(cwSQL.COL_TYPE_STRING)) {
                String svalue;
                if (v_sql_param_value.elementAt(i) == null)
                    svalue = null;
                else
                    svalue = (String)v_sql_param_value.elementAt(i);

                stmt.setString(index++, svalue);
            } else if (colType.equals(cwSQL.COL_TYPE_TIMESTAMP)) {
                Timestamp tsvalue;
                if (v_sql_param_value.elementAt(i) == null) {
                    tsvalue = null;
                } else {
                    tsvalue = (Timestamp)v_sql_param_value.elementAt(i);
                }
                stmt.setTimestamp(index++, tsvalue);
            } else if (colType.equals(cwSQL.COL_TYPE_BOOLEAN)) {
                boolean bvalue = ((Boolean)v_sql_param_value.elementAt(i)).booleanValue();
                stmt.setBoolean(index++, bvalue);
            } else {
            	CommonLog.info("Warning, unknown datatype: " + colType);
            }
        }
        ResultSet rs = stmt.executeQuery();
        Vector vtQueId = new Vector();
        Vector vtQuestion = new Vector();
        while (rs.next()) {
            dbQuestion que = new dbQuestion();
            long que_res_id = rs.getLong("que_res_id");
            que.que_res_id = que_res_id;
            que.que_xml = cwSQL.getClobValue(rs, "que_xml");
            que.que_score = rs.getInt("que_score");
            que.que_type = rs.getString("que_type");
            que.que_int_count = rs.getInt("que_int_count");
            que.que_prog_lang = rs.getString("que_prog_lang");
            que.que_media_ind = rs.getBoolean("que_media_ind");
            que.que_submit_file_ind = rs.getBoolean("que_submit_file_ind");

            que.res_id = que_res_id;
            que.res_lan = rs.getString("res_lan");
            que.res_title = rs.getString("res_title");
            que.res_desc = cwSQL.getClobValue(rs, "res_desc");
            que.res_type = rs.getString("res_type");
            que.res_subtype = rs.getString("res_subtype");
            que.res_annotation = rs.getString("res_annotation");
            que.res_format = rs.getString("res_format");
            que.res_difficulty = rs.getInt("res_difficulty");
            que.res_duration = rs.getFloat("res_duration");
            que.res_privilege = rs.getString("res_privilege");
            que.res_status = rs.getString("res_status");
            que.res_usr_id_owner = rs.getString("res_usr_id_owner");
            que.res_tpl_name = rs.getString("res_tpl_name");
            que.res_res_id_root = rs.getLong("res_res_id_root");
            que.res_mod_res_id_test = rs.getLong("res_mod_res_id_test");
            que.res_upd_user = rs.getString("res_upd_user");
            que.res_upd_date = rs.getTimestamp("res_upd_date");
            que.res_src_type = rs.getString("res_src_type");
            que.res_src_link = rs.getString("res_src_link");
            que.res_instructor_name = rs.getString("res_instructor_name");
            que.res_instructor_organization = rs.getString("res_instructor_organization");

            vtQueId.addElement(new Long(que_res_id));
            vtQuestion.addElement(que);

        }
        stmt.close();
        xml.append(dbsearch.condAsXML());
        xml.append("<question_list>").append(cwUtils.NEWL);
        Hashtable htInteraction = dbInteraction.getInteractions(con, vtQueId);
        for (int i = 0; i < vtQuestion.size(); i++) {
            dbQuestion que = (dbQuestion)vtQuestion.elementAt(i);
            que.ints = (Vector)htInteraction.get(new Long(que.que_res_id));
            if (que.ints == null) {
                que.ints = new Vector();
            }

            if (que.que_type.equals(dbResource.RES_SUBTYPE_FSC)) {
                FixedScenarioQue fsc = new FixedScenarioQue();
                fsc.res_id = que.que_res_id;
                fsc.get(con, que);

                Vector v_que_id = fsc.getChildQueId(con);
                if (fsc.qct_allow_shuffle_ind == 1) {
                    v_que_id = cwUtils.randomVec(v_que_id);
                }
                //					for (int k=0; k<v_que_id.size(); k++){
                //						System.out.println(v_que_id.elementAt(k));
                //					}
                xml.append(fsc.detailsAsXML(con, prof, cur_stylesheet));
            } else if (que.que_type.equals(dbResource.RES_SUBTYPE_DSC)) {
                DynamicScenarioQue dsc = new DynamicScenarioQue();
                dsc.res_id = que.que_res_id;
                dsc.get(con, que);
                xml.append(dsc.detailsAsXML(con, prof, cur_stylesheet));
            } else {
                xml.append(que.queAsXML(con, null, null, false, null));
            }
        }

        xml.append("</question_list>").append(cwUtils.NEWL);
        return xml;
    }
    
    public StringBuffer condAsXML() {
        StringBuffer xml = new StringBuffer();
        xml.append("<condition>").append(cwUtils.NEWL);
        if (search_create_time_after != null || search_create_time_before != null) {
            xml.append("<create_time>").append(cwUtils.NEWL);
            if (search_create_time_after != null) {
                xml.append("<after>").append(search_create_time_after).append("</after>").append(
                    cwUtils.NEWL);
            }
            if (search_create_time_before != null) {
                xml.append("<before>").append(search_create_time_before).append(
                    "</before>").append(
                    cwUtils.NEWL);
            }
            xml.append("</create_time>").append(cwUtils.NEWL);
        }
        if (search_update_time_after != null || search_update_time_before != null) {
            xml.append("<update_time>").append(cwUtils.NEWL);
            if (search_update_time_after != null) {
                xml.append("<after>").append(search_update_time_after).append("</after>").append(
                    cwUtils.NEWL);
            }
            if (search_update_time_before != null) {
                xml.append("<before>").append(search_update_time_before).append(
                    "</before>").append(
                    cwUtils.NEWL);
            }
            xml.append("</update_time>").append(cwUtils.NEWL);
        }
        if (search_id > 0 || search_id_before > 0 || search_id_after > 0) {
            xml.append("<id>");
            if (search_id > 0) {
                xml.append("<exact_id>").append(search_id).append("</exact_id>").append(
                    cwUtils.NEWL);
            }
            if (search_id_before > 0) {
                xml.append("<before>").append(search_id_before).append("</before>").append(
                    cwUtils.NEWL);
            }
            if (search_id_after > 0) {
                xml.append("<after>").append(search_id_after).append("</after>").append(
                    cwUtils.NEWL);
            }
            xml.append("</id>").append(cwUtils.NEWL);
        }
        if (search_diff_before != 0 || search_diff_after != 0) {
            xml.append("<diff>").append(cwUtils.NEWL);
            if (search_diff_after != 0)
                xml.append("<after>").append(search_diff_after).append("</after>").append(
                    cwUtils.NEWL);
            if (search_diff_before != 0)
                xml.append("<before>").append(search_diff_before).append("</before>").append(
                    cwUtils.NEWL);
            xml.append("</diff>").append(cwUtils.NEWL);
        }
        if (search_privilege != null && search_privilege.length() > 0) {
            xml.append("<privilege>").append(search_privilege).append("</privilege>").append(
                cwUtils.NEWL);
        }
        if (search_status != null && search_status.length() > 0) {
            xml.append("<status>").append(search_status).append("</status>").append(cwUtils.NEWL);
        }
        if (search_type != null
            && search_type.length > 0
            && search_type[0] != null
            && search_type[0].length() > 0) {
            xml.append("<type>");
            for (int i = 0; i < search_type.length; i++) {
                if (i > 0) {
                    xml.append("~");
                }
                xml.append(search_type);
            }
            xml.append("</type>").append(cwUtils.NEWL);
        }
        if (search_sub_type != null
            && search_sub_type.length > 0
            && search_sub_type[0] != null
            && search_sub_type[0].length() > 0) {
            xml.append("<subtype>");
            for (int i = 0; i < search_sub_type.length; i++) {
                if (i > 0) {
                    xml.append("~");
                }
                xml.append(search_sub_type);
            }
            xml.append("</subtype>").append(cwUtils.NEWL);
        }
        if ((search_owner != null && search_owner.length() > 0) || search_owner_ent_id > 0) {
            xml.append("<owner>");
            if (search_owner != null && search_owner.length() > 0) {
                xml.append("<usr_id>").append(search_owner).append("</usr_id>").append(
                    cwUtils.NEWL);
            }
            if (search_owner_ent_id > 0) {
                xml.append("<usr_ent_id>").append(search_owner_ent_id).append(
                    "</usr_ent_id>").append(
                    cwUtils.NEWL);
            }
            xml.append("</owner>").append(cwUtils.NEWL);
        }
        if (search_key != null && search_key.length() > 0) {
            xml.append("<key>").append(search_key).append("</key>").append(cwUtils.NEWL);
        }
        if (search_order != null && search_order.length() > 0) {
            xml.append("<order>").append(search_order).append("</order>").append(cwUtils.NEWL);
        }
        if (search_lang != null && search_lang.length() > 0) {
            xml.append("<lang>").append(search_lang).append("</lang>").append(cwUtils.NEWL);
        }
        if (search_title != null && search_title.length > 0) {
            xml.append("<title_list>");
            for (int i = 0; i < search_title.length; i++) {
                if (search_title[i] != null && search_title[i].length() > 0)
                    xml.append("<title>").append(search_title[i]).append("</title>");
            }
            xml.append("</title_list>").append(cwUtils.NEWL);
        }
        if (search_desc != null && search_desc.length > 0) {
            xml.append("<desc_list>");
            for (int i = 0; i < search_desc.length; i++) {
                if (search_desc[i] != null && search_desc[i].length() > 0)
                    xml.append("<desc>").append(search_desc[i]).append("</desc>");
            }
            xml.append("</desc_list>").append(cwUtils.NEWL);
        }
        if (search_obj_id != null && search_obj_id.length > 0) {
            xml.append("<obj_list>");
            for (int i = 0; i < search_obj_id.length; i++) {
                xml.append("<obj_id>").append(search_obj_id[i]).append("</obj_id>");
            }
            xml.append("</obj_list>").append(cwUtils.NEWL);
        }
        xml.append("</condition>").append(cwUtils.NEWL);

        return xml;
    }
    
}

    