package com.cw.wizbank.accesscontrol;

import java.sql.Connection;
import java.sql.SQLException;

import com.cwn.wizbank.security.service.AclService;
import com.cwn.wizbank.web.WzbApplicationContext;

public class AcEvaluation extends AcInstance {

	/**
	 * function, maintain evaluation
	 */
	public static final String FTN_EVN_MAIN = "EVN_MAIN";
	/**
	 * function, view evaluation list
	 */
	public static final String FTN_EVN_LIST = "EVN_LIST";

	public AcEvaluation(Connection con) {
		super(con);
	}
	
	/**
	 * check whether user has privilege to view evaluation list.
	 * @param ent_id
	 * @param rol_ext_id
	 * @return
	 * @throws SQLException
	 */

	
	

}
