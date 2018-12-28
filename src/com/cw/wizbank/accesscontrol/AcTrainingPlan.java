package com.cw.wizbank.accesscontrol;

import java.sql.Connection;
import java.sql.SQLException;


import com.cwn.wizbank.security.AclFunction;

public class AcTrainingPlan extends AcInstance {

    /**
    function ext id, ��ѵ�ƻ�����
    */
//    public final String FTN_TRA_PLA_MGT = "PLAN_CARRY_OUT";
//    public final String FTN_TRA_CLA_MGT = "TRA_CLA_MGT";
//    public final String FTN_MY_TRA_CLA = "MY_TRA_CLA";
//    public static final String FTN_MY_TRAININGCOS_LIST = "MY_TRAININGCOS_LIST";
	public AcTrainingPlan(Connection con) {
		super(con);
	}

    /**
    check if the user, role pair has the privilege to manage training plan
    */
    public boolean hasMaintainPrivilege(long ent_id, String rol_ext_id) throws SQLException {
        
        return  AccessControlWZB.hasRolePrivilege( rol_ext_id,AclFunction.FTN_AMD_PLAN_MGT) ;
    }
  
   
    
}
