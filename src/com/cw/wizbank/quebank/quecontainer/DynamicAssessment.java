package com.cw.wizbank.quebank.quecontainer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

import com.cw.wizbank.qdb.dbModule;
import com.cw.wizbank.qdb.dbObjective;
import com.cw.wizbank.qdb.dbResource;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.qdb.QStat;

import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;

import com.cw.wizbank.db.DbQueContainerSpec;
import com.cw.wizbank.db.view.ViewResources;

public class DynamicAssessment extends DynamicQueContainer {

    public final static String ASS_ADAPTIVE_LOGIC = "ADT";
    public final static String ASS_RANDOM_LOGIC = "RND";

    public DynamicAssessment() {}

    /**
     * Check whether the dynamic assessment has enough question or not.
     * @param con
     * @return boolean indicating whether the dynamic assessment has enough question or not.
     * @throws SQLException
     * @throws cwException Thrown if get objective id list error.
     */
    public boolean hasEnoughQue(Connection con) throws SQLException, cwException, qdbException {

        try {
            getChildQueId(con);
        }
        catch (cwSysMessage e) {
            return false;
        }
        return true;
    }

    /**
     * Get child question id meet all pre-defined requirement.
     * @param con
     * @return Vector of question id in Long.
     * @throws SQLException
     * @throws cwSysMessage Thrown if not enough question.
     * @throws cwException Thrown if get objective id list error.
     */
    public Vector getChildQueId(Connection con) throws SQLException, cwSysMessage, cwException {

        DbQueContainerSpec myDbQueContainerSpec = new DbQueContainerSpec();
        Vector vtDbQueContainerSpec = DbQueContainerSpec.getQueContainerSpecs(con, this.res_id);
        return getChildQueId(con, vtDbQueContainerSpec, null, 0);

    }

    /**
     * Get child question id meet the specified requirement.
     * @param con
     * @param queSpec Vector of DbQueContainerSpec.
     * @return Vector of question id in Long.
     * @throws SQLException
     * @throws cwSysMessage Thrown if not enough question.
     * @throws cwException Thrown if get objective id list error.
     */
    public Vector getChildQueId(Connection con, Vector queSpec) throws SQLException, cwSysMessage, cwException {
        return getChildQueId(con, queSpec, null, 0);

    }

    /**
     * Get child question id meet all pre-defined requirement when assessment launching by module.
     * @param con
     * @param mod_id Module launch the assessment.
     * @param usr_id User get the assessment.
     * @return Vector of question id in Long.
     * @throws SQLException
     * @throws cwSysMessage Thrown if not enough question.
     * @throws cwException Thrown if get objective id list error.
     */
    public Vector getChildQueIdFromModule(Connection con, long mod_id, String usr_id) throws SQLException, cwSysMessage, cwException {

        DbQueContainerSpec myDbQueContainerSpec = new DbQueContainerSpec();
        Vector vtDbQueContainerSpec = DbQueContainerSpec.getQueContainerSpecs(con, this.res_id);
        return getChildQueId(con, vtDbQueContainerSpec, usr_id, mod_id);

    }

    /**
     * Get child question id meet the specified requirement when assessment launching by module. 
     * @param con
     * @param queSpec Vector of DbQueContainerSpec.
     * @param mod_id Module launch the assessment.
     * @param usr_id User get the assessment.
     * @return Vector of question id in Long.
     * @throws SQLException
     * @throws cwSysMessage Thrown if not enough question.
     * @throws cwException Thrown if get objective id list error.
     */
    public Vector getChildQueIdFromModule(Connection con, Vector queSpec, long mod_id, String usr_id) throws SQLException, cwSysMessage, cwException {

        return getChildQueId(con, queSpec, usr_id, mod_id);

    }

    /**
     * Get child question id meet the specified requirement.
     * @param con
     * @param queSpec Vector of DbQueContainerSpec.
     * @param usr_id User id of the user who get the assessement.
     * @param mod_id Module id of the module which the assessment belong to. 
     * @return Vector of question id in Long.
     * @throws SQLException
     * @throws cwSysMessage Thrown if not enough question.
     * @throws cwException Thrown if get objective id/assessment owner usr id error.
     */
    private Vector getChildQueId(Connection con, Vector queSpec, String usr_id, long mod_id) throws SQLException, cwSysMessage, cwException {

        Vector v_que_id = new Vector();
        ViewResources viewRes = new ViewResources();
        String objLst = null;
        String ass_owner_usr_id = null;
        for (int i = 0; i < queSpec.size(); i++) {
            DbQueContainerSpec spec = (DbQueContainerSpec)queSpec.elementAt(i);

            try {
                objLst = dbObjective.getSelfAndChildsObjIdLst(con, spec.qcs_obj_id);
            } catch (qdbException e) {
                throw new cwException(e.getMessage());
            }
            if (spec.qcs_privilege != null && spec.qcs_privilege.equalsIgnoreCase(dbResource.RES_PRIV_AUTHOR)) {
                try {
                    ass_owner_usr_id = dbResource.getResUsrIdOwner(con, this.res_id);
                } catch (qdbException e) {
                    throw new cwException("Failed to get assessment owner usr_id.");
                }
            }

            //if launch by module
            if (usr_id != null && usr_id.length() > 0 && mod_id > 0) {
                if (this.qct_select_logic.equalsIgnoreCase(ASS_ADAPTIVE_LOGIC)) {
                    applyAdaptiveLogic(con, mod_id, usr_id, spec);
                }
            }

            Vector tmp_v_que_id = viewRes.getDynamicAssChildQueId(con, ass_owner_usr_id, objLst, spec.qcs_res_id, spec.qcs_type, spec.qcs_score, spec.qcs_difficulty, spec.qcs_privilege, spec.qcs_duration);

            //if not enough question after apply logic, use original spec. to get question again.
            if (usr_id != null && usr_id.length() > 0 && mod_id > 0 && spec.qcs_qcount > tmp_v_que_id.size()) {

                spec = (DbQueContainerSpec)queSpec.elementAt(i);
                tmp_v_que_id = viewRes.getDynamicAssChildQueId(con, ass_owner_usr_id, objLst, spec.qcs_res_id, spec.qcs_type, spec.qcs_score, spec.qcs_difficulty, spec.qcs_privilege, spec.qcs_duration);
            }
            
            if (spec.qcs_qcount > tmp_v_que_id.size()) {
                throw new cwSysMessage("MSP001");
            }

            try {
                v_que_id.addAll(cwUtils.randomDrawFromVec(tmp_v_que_id, spec.qcs_qcount));

            } catch (cwException e) {
                throw new cwSysMessage("MSP001");
            }
        }

        return v_que_id;

    }

    /**
     * Depend on user performance in the specified module and objective 
     * to determine the question difficuly in the DbQueContainerSpec. 
     * @param con
     * @param mod_id Module id of the module which the assessment belong to.
     * @param usr_id User id of the user who get the assessement.  
     * @param spec DbQueContainerSpec to search question.
     * @throws SQLException
     * @throws cwException Thrown if error in getting precentage by objective.
     */
    private void applyAdaptiveLogic(Connection con, long mod_id, String usr_id, DbQueContainerSpec spec) throws SQLException, cwException {

        int pass_percentage = (int)dbModule.getPassingScore(con, mod_id);

        int weight = 0;

        int percentage = 0;
        try {
            percentage = QStat.percentageByObj(con, mod_id, usr_id, spec.qcs_obj_id);
        } catch (qdbException e) {
            throw new cwException(e.getMessage());
        }

        // depend on the percentage, set the weight
        if (percentage < 0) {
            weight = 0;
        } else if (percentage < (pass_percentage * 0.5)) {
            weight = -2;
        } else if (percentage < (pass_percentage * 0.8)) {
            weight = -1;
        } else if (percentage > (pass_percentage + (100 - pass_percentage) * 0.8)) {
            weight = 3;
        } else if (percentage > (pass_percentage + (100 - pass_percentage) * 0.6)) {
            weight = 2;
        } else if (percentage > (pass_percentage + (100 - pass_percentage) * 0.3)) {
            weight = 1;
        } else {
            weight = 0;
        }

        if (weight != 0) {
            spec.qcs_difficulty += weight;
            if (spec.qcs_difficulty < 1) {
                spec.qcs_difficulty = 1;
            } else if (spec.qcs_difficulty > 3) {
                spec.qcs_difficulty = 3;
            }
        }
        return;
    }

}