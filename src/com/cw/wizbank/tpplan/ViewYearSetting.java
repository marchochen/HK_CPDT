package com.cw.wizbank.tpplan;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.db.DbTrainingCenter;
import com.cw.wizbank.db.view.ViewTrainingCenter;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.tpplan.db.dbTpYearSetting;
import com.cw.wizbank.tree.cwTree;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;

public class ViewYearSetting {

	private static String tc_splite = ",";
	public static String getYearSettingXML (Connection con, loginProfile prof, WizbiniLoader wizbini, long year, long tcr_id) throws SQLException, cwSysMessage, cwException {
		StringBuffer xml = new StringBuffer(1024);

		ViewTrainingCenter viewTc = new ViewTrainingCenter();
		List tcr_lst = new ArrayList<DbTrainingCenter>();
		if(AccessControlWZB.isRoleTcInd(prof.current_role)){
			 tcr_lst = viewTc.getTrainingCenterByOfficer(con, prof.usr_ent_id, prof.current_role, false);
		}else{
			 tcr_lst.add(viewTc.getTrainingCenterById(con, prof.my_top_tc_id));
		}
		
		xml.append("<my_charge_tc_lst>");
		if (tcr_lst != null) {
			Iterator tcr_itr = tcr_lst.iterator();
			while (tcr_itr.hasNext()) {
				DbTrainingCenter tc = (DbTrainingCenter) tcr_itr.next();
				if (tc != null) {
					xml.append("<tc tcr_id=\"").append(tc.tcr_id).append("\">")
							.append(cwUtils.esc4XML(tc.tcr_title)).append(
									"</tc>");
				}
			}
		}
		xml.append("</my_charge_tc_lst>");
		//set default training center
		if(tcr_id <= 0) {
			tcr_id = ViewTrainingCenter.getDefaultTc(con, prof);
			/*
			if(tcr_id == 0) {
    			throw new cwSysMessage("TC002");
    		}
    		*/
		}
		xml.append("<cur_tc_id>").append(tcr_id).append("</cur_tc_id>");
		DbTrainingCenter dbtc = new DbTrainingCenter();
		dbtc.tcr_id = tcr_id;
		dbtc.setTcr_ste_ent_id(prof.root_ent_id);
		Vector child_tc = dbtc.getChildTcList(con);
		boolean hasChildTc = false;
		if(child_tc != null && child_tc.size() > 0) {
			hasChildTc = true;
		}
		xml.append("<child_tc_lst>");
		if(hasChildTc) {
			for(int i=0; i<child_tc.size(); i++) {
				DbTrainingCenter tc = (DbTrainingCenter)child_tc.elementAt(i);
				xml.append("<tc tcr_id=\"").append(tc.tcr_id)
					.append("\">").append(cwUtils.esc4XML(tc.tcr_title))
					.append("</tc>");
			}
		}
		xml.append("</child_tc_lst>");

		if(hasChildTc) {
			long display_year;
			long minYear = tpPlanManagement.getMinTraPlanYear(con, wizbini);
			long maxYear = minYear + 5;
			if(year > 0) {
				display_year = year;
			} else {
				display_year = minYear + 1;
			}
			;
			xml.append("<year_option>");
			boolean valide_year = false;
			for (int i = (int)maxYear; i >= minYear; i--) {
				if(year == 0 || year == i) {
					valide_year = true;
				}
				xml.append("<option>").append(i).append("</option>");
			}
			if(!valide_year) {
				throw new cwException("wrong year!");
			}
			xml.append("</year_option>");
			xml.append("<setting>");
			xml.append("<ysg_tcr_id>").append(tcr_id).append("</ysg_tcr_id>");
			xml.append("<ysg_year>").append(display_year).append("</ysg_year>");
			dbTpYearSetting dbtys = dbTpYearSetting.get(con, tcr_id, display_year); 
			if(dbtys != null) {
				xml.append("<ysg_submit_start_datetime>").append(dbtys.ysg_submit_start_datetime).append("</ysg_submit_start_datetime>");
				xml.append("<ysg_submit_end_datetime>").append(dbtys.ysg_submit_end_datetime).append("</ysg_submit_end_datetime>");
				xml.append("<ysg_update_timestamp>").append(dbtys.ysg_update_timestamp).append("</ysg_update_timestamp>");
				String child_tcr_id_lst = dbtys.ysg_child_tcr_id_lst;
				Vector sel_child_tcr_id = cwUtils.splitToVec(child_tcr_id_lst.substring(0, child_tcr_id_lst.lastIndexOf(tc_splite)), tc_splite);
				xml.append("<sel_child_tc_lst>");
				for (int i=0; i<sel_child_tcr_id.size(); i++) {
					xml.append("<tc tcr_id=\"").append(((Long)sel_child_tcr_id.elementAt(i)).longValue()).append("\"/>");
				}
				xml.append("</sel_child_tc_lst>");
			}
			xml.append("</setting>");
		}
		return xml.toString();
	}
	
	public static String getTcNavTreeXML(Connection con, long tcr_id, loginProfile prof) throws SQLException, cwSysMessage, cwException {
		StringBuffer xml = new StringBuffer(1024);
        cwTree tree = new cwTree();
        xml.append(tree.genNavTrainingCenterTree(con, prof, true));
        DbTrainingCenter dbtc = DbTrainingCenter.getInstance(con, tcr_id);
        if(dbtc != null) {
        	xml.append("<cur_tc id=\"").append(tcr_id).append("\">").append(cwUtils.esc4XML(dbtc.tcr_title)).append("</cur_tc>");
        }
        return xml.toString();
	}
}
