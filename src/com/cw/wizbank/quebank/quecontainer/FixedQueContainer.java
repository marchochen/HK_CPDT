package com.cw.wizbank.quebank.quecontainer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

import com.cw.wizbank.qdb.dbModule;
import com.cw.wizbank.qdb.dbModuleEvaluation;
import com.cw.wizbank.qdb.dbResource;
import com.cw.wizbank.qdb.dbQuestion;
import com.cw.wizbank.qdb.dbResourceContent;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.dbResourceObjective;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.db.view.ViewQueContainer;
import com.cw.wizbank.db.DbQueContainerSpec;

public class FixedQueContainer extends dbResource {
    public String qct_select_logic;
    public int qct_allow_shuffle_ind = 0;

    public FixedQueContainer() {}

    public void get(Connection con) throws qdbException, cwSysMessage {
        ViewQueContainer myViewQueContainer = new ViewQueContainer();
        myViewQueContainer.res_id = res_id;
        myViewQueContainer.get(con);
        qct_select_logic = myViewQueContainer.qct_select_logic;
        qct_allow_shuffle_ind = myViewQueContainer.qct_allow_shuffle_ind;

        super.get(con);
    }

    public void ins(Connection con, long obj_id, loginProfile prof) throws qdbException, qdbErrMessage {
        String[] robs = new String[1];
        robs[0] = Long.toString(obj_id);
        super.ins_res(con, robs, prof);

        ViewQueContainer myViewQueContainer = new ViewQueContainer();
        myViewQueContainer.res_id = res_id;
        myViewQueContainer.qct_select_logic = qct_select_logic;
        myViewQueContainer.qct_allow_shuffle_ind = qct_allow_shuffle_ind;
        myViewQueContainer.ins(con);
    }

    public void upd(Connection con, loginProfile prof) throws qdbException, qdbErrMessage, cwSysMessage {
        super.upd_res(con, prof, true);

        ViewQueContainer myViewQueContainer = new ViewQueContainer();
        myViewQueContainer.res_id = res_id;
        myViewQueContainer.qct_select_logic = qct_select_logic;
        myViewQueContainer.qct_allow_shuffle_ind = qct_allow_shuffle_ind;
        myViewQueContainer.upd(con);
    }

    public void del_container(Connection con, loginProfile prof) throws qdbException, qdbErrMessage, cwSysMessage, SQLException {
        // delete the child questions and the related records
        Vector vtResourceContent = dbResourceContent.getChildAss(con, res_id);

        if (vtResourceContent.size() > 0) {
            String[] id_lst = new String[vtResourceContent.size()];
            for (int i = 0; i < vtResourceContent.size(); i++) {
                dbResourceContent myDbResourceContent = (dbResourceContent)vtResourceContent.elementAt(i);
                id_lst[i] = Long.toString(myDbResourceContent.rcn_res_id_content);
            }

            dbResourceContent myDbResourceContent = new dbResourceContent();
            myDbResourceContent.rcn_res_id = res_id;
            myDbResourceContent.delRes(con, id_lst);

            for (int i = 0; i < id_lst.length; i++) {
                dbQuestion dbque = new dbQuestion();
                dbque.que_res_id = Long.parseLong(id_lst[i]);
                dbque.res_id = Long.parseLong(id_lst[i]);
                dbque.get(con);

                if (dbque.res_subtype.equalsIgnoreCase(RES_SUBTYPE_FSC)) {
                    FixedScenarioQue myFixedScenarioQue = new FixedScenarioQue();
                    myFixedScenarioQue.res_id = Long.parseLong(id_lst[i]);
                    myFixedScenarioQue.del(con, prof);
                } else if (dbque.res_subtype.equalsIgnoreCase(RES_SUBTYPE_DSC)) {
                    DynamicScenarioQue myDynamicScenarioQue = new DynamicScenarioQue();
                    myDynamicScenarioQue.res_id = Long.parseLong(id_lst[i]);
                    myDynamicScenarioQue.del(con, prof);
                } else {
                    dbque.del(con);
                }
            }
        }

        // delete the container itself
        ViewQueContainer myViewQueContainer = new ViewQueContainer();
        myViewQueContainer.res_id = res_id;
        myViewQueContainer.del(con);

        DbQueContainerSpec myDbQueContainerSpec = new DbQueContainerSpec();
        myDbQueContainerSpec.qcs_res_id = res_id;
        myDbQueContainerSpec.del_frm_res_id(con);
    }

    public void del(Connection con, loginProfile prof) throws qdbException, qdbErrMessage, cwSysMessage, SQLException {
        del_container(con, prof);
        super.del_res(con, prof);
    }

    /*
    	public void addQuestion(Connection con, String[] que_id_lst, String uploadDir , loginProfile prof) 
    		throws qdbException, qdbErrMessage, cwSysMessage
    	{
    		insQ(con, que_id_lst, uploadDir, prof);
    	}
    	*/

    public void removeQuestion(Connection con, String[] que_id_lst, loginProfile prof) throws qdbException, qdbErrMessage, cwSysMessage, SQLException {
        delQ(con, que_id_lst, prof);
    }

    public String asXML(Connection con, loginProfile prof, boolean checkStatusRes) throws cwException, qdbException, SQLException {
        ViewQueContainer myViewQueContainer = new ViewQueContainer();
        myViewQueContainer.res_id = res_id;
        myViewQueContainer.get(con);

        StringBuffer result = new StringBuffer(2048);
        result.append(dbUtils.xmlHeader).append(dbUtils.NEWL);
        result.append("<resource_content>").append(dbUtils.NEWL);
        result.append(getQueContainerListNoHeaderAsXML(con, prof, checkStatusRes, "rcn_order")).append(dbUtils.NEWL);
        result.append("<que_container_extra_info ");
        result.append("res_id=\"");
        result.append(Long.toString(myViewQueContainer.res_id));
        result.append("\" allow_shuffle_ind=\"");
        result.append(Integer.toString(myViewQueContainer.qct_allow_shuffle_ind));
        result.append("\" selection_logic=\"");
        result.append(myViewQueContainer.qct_select_logic);
        result.append("\">");
        result.append("</que_container_extra_info>");
        result.append("</resource_content>").append(dbUtils.NEWL);
        return result.toString();
    }

    public void addQuestion(Connection con, String[] que_id_lst, String uploadDir, loginProfile prof, boolean boolWithChildObj, Vector vtClassModId) throws qdbException, qdbErrMessage, cwSysMessage {
        res_upd_user = prof.usr_id;

        int i;
        dbResourceContent resObj = new dbResourceContent();
        Vector failList = new Vector();
        long newId = 0;

        Vector idobjVec = dbResourceObjective.getResObj(con, que_id_lst);
        Vector<Long> idVec = (Vector)idobjVec.elementAt(0);
        Vector objVec = (Vector)idobjVec.elementAt(1);

        //for(i =0;i< idVec.size();i++) {
        for (i = 0; i < idVec.size(); i++) {
            resObj.rcn_res_id = res_id;
            //long tmpId = ((Long) idVec.elementAt(i)).longValue();
            long tmpId = idVec.get(i);
            long objId = ((Long)objVec.elementAt(i)).longValue();
            //  duplicate the question
            newId = dbQuestion.insTestQ(con, res_id, tmpId, objId, res_upd_user, uploadDir, prof, boolWithChildObj);
            if (newId <= 0) {
                failList.addElement(new Long(tmpId));
            } else {
                resObj.rcn_res_id_content = newId;
                resObj.rcn_obj_id_content = objId;
                resObj.ins(con);

                dbUtils.copyMediaFrom(uploadDir, tmpId, newId); 
                //copy the new module-question relation to every class
                for (int j = 0; vtClassModId != null && j < vtClassModId.size(); j++) {
                    resObj.rcn_rcn_res_id_parent = res_id;
                    resObj.rcn_rcn_sub_nbr_parent = resObj.rcn_sub_nbr;
                    resObj.rcn_res_id = ((Long) vtClassModId.get(j)).longValue();
                    resObj.rcn_res_id_content = newId;
                    resObj.rcn_obj_id_content = objId;
                    resObj.ins(con);
                }
            }
        }
        // Calculate the max score after adding the question to the test
        ViewQueContainer myViewQueContainer = new ViewQueContainer();
        myViewQueContainer.res_id = res_id;
        myViewQueContainer.updScore(con, res_subtype);
        super.updateTimeStamp(con);

        if (failList.size() > 0) {
            String mesg = new String();
            for (i = 0; i < failList.size(); i++) {
                if (i != 0)
                    mesg += ", ";

                mesg += ((Long)failList.elementAt(i)).longValue();
            }

            throw new qdbErrMessage("ASM012", mesg);
        }
    }

    private void delQ(Connection con, String[] que_id_lst, loginProfile prof) throws qdbException, qdbErrMessage, cwSysMessage, SQLException {
        res_upd_user = prof.usr_id;

        // check timeStamp 
        // super.checkTimeStamp(con);

        int i;
        Long idObj = null;

        for (i = 0; i < que_id_lst.length; i++) {

            dbResourceContent resObj = new dbResourceContent();

            resObj.rcn_res_id = res_id;
            long tmpId = Long.parseLong(que_id_lst[i]);
            resObj.rcn_res_id_content = tmpId;

            resObj.del(con);

            dbQuestion dbque = new dbQuestion();
            dbque.que_res_id = resObj.rcn_res_id_content;
            dbque.res_id = resObj.rcn_res_id_content;
            dbque.get(con);

            if (dbque.res_subtype.equalsIgnoreCase(RES_SUBTYPE_FSC)) {
                FixedScenarioQue myFixedScenarioQue = new FixedScenarioQue();
                myFixedScenarioQue.res_id = resObj.rcn_res_id_content;
                myFixedScenarioQue.del(con, prof);
            } else if (dbque.res_subtype.equalsIgnoreCase(RES_SUBTYPE_DSC)) {
                DynamicScenarioQue myDynamicScenarioQue = new DynamicScenarioQue();
                myDynamicScenarioQue.res_id = resObj.rcn_res_id_content;
                myDynamicScenarioQue.del(con, prof);
            } else {
                dbque.res_upd_user = prof.usr_id;
                dbque.del(con);
            }

        }

        /*
        // calculate the max score of the test after deleting the question(s).
        ViewQueContainer myViewQueContainer = new ViewQueContainer();
        myViewQueContainer.res_id = res_id;
        myViewQueContainer.updScore(con, res_subtype);         
        super.updateTimeStamp(con);
        */
    }

    /**
    Update the shuffle ind of this Fixed Que Container.
    Pre-defined class variable: res_id
    							res_upd_date
    							qct_allow_shuffle_ind
    */
    public void updateShuffleInd(Connection con, loginProfile prof) throws qdbErrMessage, qdbException, SQLException {
        this.res_upd_user = prof.usr_id;
        // check timeStamp 
        checkTimeStamp(con);

        ViewQueContainer queContainer = new ViewQueContainer();
        queContainer.qct_allow_shuffle_ind = this.qct_allow_shuffle_ind;
        queContainer.res_id = this.res_id;
        queContainer.updateShuffleInd(con);

        updateTimeStamp(con);
        return;
    }

    /**
    Reorder Question in a QueContainer
    Pre-defined variables: res_id
    					   res_upd_date
    */
    public void reorderQ(Connection con, String[] que_id_lst, String[] que_order_lst, loginProfile prof) throws qdbException, qdbErrMessage {
        this.res_upd_user = prof.usr_id;
        super.checkTimeStamp(con);

        for (int i = 0; i < que_id_lst.length; i++) {
            dbResourceContent resObj = new dbResourceContent();
            resObj.rcn_res_id = res_id;
            long tmpId = Long.parseLong(que_id_lst[i]);
            resObj.rcn_res_id_content = tmpId;
            long tmpOrder = Long.parseLong(que_order_lst[i]);
            resObj.rcn_order = tmpOrder;
            resObj.updOrder(con);
        }
        super.updateTimeStamp(con);
        return;
    }

    public boolean canReorder(Connection con) throws qdbException, cwSysMessage, SQLException, qdbErrMessage {
        boolean canReorder = true;
        dbResource dbRes = new dbResource();
        dbRes.res_id = this.res_id;
        dbRes.get(con);
        dbModule dbMod = new dbModule();
        if (dbRes.res_type.equals(dbResource.RES_TYPE_QUE)) {
            dbMod.mod_res_id = dbRes.res_mod_res_id_test;
        } else if (dbRes.res_type.equals(dbResource.RES_TYPE_MOD)) {
            dbMod.mod_res_id = this.res_id;
        }
        if(dbModuleEvaluation.getModuleAttemptCount(con,dbMod.mod_res_id) > 0
            || dbMod.getClassModuleAttemptCount(con, dbMod.mod_res_id) > 0 ) {
            canReorder = false;
        }
        return canReorder;
    }

}