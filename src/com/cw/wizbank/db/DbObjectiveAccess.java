/*
 * Created on 2004-6-7
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.cw.wizbank.db;

import java.sql.*;

import com.cw.wizbank.db.sql.*;


/**
 * @author donaldl
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class DbObjectiveAccess {
	
	public long oac_obj_id;
	public long oac_ent_id;
	public String oac_access_type;
	
	public void ins(Connection con) throws SQLException {
			PreparedStatement stmt = null;
			try {
				stmt = con.prepareStatement(SqlStatements.SQL_INS_OAC);
				int index=1;
				stmt.setLong(index++,oac_obj_id);
				stmt.setString(index++,oac_access_type);
				if(this.oac_ent_id == 0) {
					stmt.setNull(index++, java.sql.Types.INTEGER);
				} else {
					stmt.setLong(index++,oac_ent_id);
				}
	
				stmt.executeUpdate();
			} finally {
				if(stmt!=null) stmt.close();
			}
		}
}
