package com.cw.wizbank.ae.db.view;

import java.sql.*;
import java.util.*;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.ae.db.sql.OuterJoinSqlStatements;

public class ViewGlobalEnrollmentReport {
    
    private long ste_ent_id = 0;
    private String ste_name = null;
    private long enrollmentCount = 0;
    private long itm_id = 0;
    private String itm_title = null;
    private String itm_code = null;
    public long getSiteEntId() {
        return this.ste_ent_id;
    }
    
    public String getSiteName() {
        return this.ste_name;
    }
    
    public long getEnrollmentCount() {
        return this.enrollmentCount;
    }
    
    public long getItemId() {
    	return this.itm_id;
    }
    
    public String getItemTitle() {
    	return this.itm_title;
    }
    
    public String getItemCode() {
    	return this.itm_code;
    }
    
    public static Vector generateReport(Connection con, int all_org_ind, 
                                        long[] ent_id_list, Timestamp start_date, 
                                        Timestamp end_date) 
                                        throws SQLException {
        
        Vector v = new Vector();
        PreparedStatement stmt = null;
        String siteList = null;
        if(all_org_ind == 0) {
            siteList = cwUtils.array2list(ent_id_list);
        }
        try {
            stmt = con.prepareStatement(OuterJoinSqlStatements.getGlobalEnrollmentReport(siteList, start_date, end_date));
            int index = 1;
            if(start_date != null) {
                stmt.setTimestamp(index++, start_date);
            }
            if(end_date != null) {
                stmt.setTimestamp(index++, end_date);
            }
            stmt.setString(index++, "OK");
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                ViewGlobalEnrollmentReport glb = new ViewGlobalEnrollmentReport();
                glb.ste_ent_id = rs.getLong("ste_ent_id");
                glb.ste_name = rs.getString("ste_name");
                glb.enrollmentCount = rs.getLong("enrollmentCount");
                v.addElement(glb);
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        return v;
    }

	
	public static Hashtable getItemEnrollmentCount(Connection con, Vector v_ste_id, Timestamp start_date, Timestamp end_date, String course_code) throws SQLException {
		Hashtable h_ste_itm = new Hashtable();
		if( v_ste_id == null || v_ste_id.isEmpty() ) {
			return h_ste_itm;
		}
		String ste_id_lst = cwUtils.vector2list(v_ste_id);
		String[] course_code_lst = null;
		if( course_code != null && course_code.length() > 0 ) {
			StringTokenizer tokens = new StringTokenizer(course_code, " ");
			course_code_lst = new String[tokens.countTokens()];
			int i = 0;
			while(tokens.hasMoreTokens()){
				course_code_lst[i++] = tokens.nextToken();				
			}
		}
		PreparedStatement stmt = con.prepareStatement(OuterJoinSqlStatements.getGlobalEnrollmentReportItemEnrollmentCount(ste_id_lst, start_date, end_date, course_code_lst));
		int index = 1;
		if(start_date != null) {
			stmt.setTimestamp(index++, start_date);
		}
		if(end_date != null) {
			stmt.setTimestamp(index++, end_date);
		}
		if( course_code_lst != null ) {
			for(int i=0; i<course_code_lst.length; i++) {
				stmt.setString(index++, "%" + course_code_lst[i].toLowerCase() + "%");
			}
		}

		ResultSet rs = stmt.executeQuery();
		while(rs.next()){
			ViewGlobalEnrollmentReport glb = new ViewGlobalEnrollmentReport();
			glb.ste_ent_id = rs.getLong("itm_owner_ent_id");
			glb.itm_id = rs.getLong("itm_id");
			glb.itm_title = rs.getString("itm_title");
			glb.enrollmentCount = rs.getLong("enrollmentCount");
			glb.itm_code = rs.getString("itm_code");
			Vector v_itm = null;
			Long steId = new Long(glb.ste_ent_id);
			if(h_ste_itm.containsKey(steId)) {
				v_itm = (Vector) h_ste_itm.get(steId);
			} else {
				v_itm = new Vector();
			}
			v_itm.add(glb);
			h_ste_itm.put(steId, v_itm);
		}
		stmt.close();
		return h_ste_itm;
	}
		
	
}