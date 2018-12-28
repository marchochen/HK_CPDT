package com.cw.wizbank.ae;

import java.sql.*;
import java.util.*;
import com.cw.wizbank.qdb.*;
import com.cw.wizbank.util.*;
import com.cw.wizbank.ae.db.*;
import com.cw.wizbank.ae.db.view.ViewFigure;


public class aeItemFigure {
    
    public final static String IAD001 = "IAD001";
    
    
    /**
    * Insert ItemAccreditation and ItemCredit Record into database    
    */
    public void ins(Connection con, String usr_id, long itm_id, DbFigure dbFig)
        throws SQLException, cwException, cwSysMessage {

            if( isFigureTypeExisted(con, itm_id, dbFig.fig_fgt_id) )
                throw new cwSysMessage(IAD001);       
            Timestamp curTime = cwSQL.getTime(con);
            dbFig.fig_update_usr_id = usr_id;
            dbFig.fig_update_timestamp = curTime;
            dbFig.fig_create_usr_id = usr_id;
            dbFig.fig_create_timestamp = curTime;
            dbFig.ins(con);
            
            DbItemFigure dbIfg = new DbItemFigure();
            dbIfg.ifg_itm_id = itm_id;
            dbIfg.ifg_fig_id = dbFig.fig_id;
            dbIfg.ins(con);
            
            return;
        
        }
    
    
    /**
    * Update ItemCreditValue of the item
    */
    public void upd(Connection con, loginProfile prof, DbFigure dbFig)
        throws SQLException, cwException {
            
            Timestamp curTime = cwSQL.getTime(con);
            dbFig.fig_update_usr_id = prof.usr_id;
            dbFig.fig_update_timestamp = curTime;
            dbFig.upd(con);
            
            return;
            
        }
    
    
    


    public void delAll(Connection con, long itm_id)
        throws SQLException, cwException {
            long[] fig_id_lst = getFigureValueId(con, itm_id, 0);
            Vector vec = new Vector();
            if( fig_id_lst != null && fig_id_lst.length > 0 ) {
                for(int i=0; i<fig_id_lst.length; i++)
                    vec.addElement(new Long(fig_id_lst[i]));
                del(con, itm_id, vec);
                //DbItemCreditValue.del(con, vec);
            }
            return;
        }

    /**
    * Delete ItemCreditValue of the item
    */
    public void del(Connection con, long itm_id, Vector v_fig_id)
        throws SQLException, cwException{
            
            if( v_fig_id != null && v_fig_id.size() > 0 ) {
                DbItemFigure dbIfg = new DbItemFigure();
                dbIfg.ifg_itm_id = itm_id;
                dbIfg.del(con, v_fig_id);
                DbFigure.del(con, v_fig_id);
            }
            return;
        }
    
    
    /**
    * Get all run items belong to the item, 
    * check the run effective datetime to determine the credit value of the run
    */
    public void insChildFigure(Connection con, loginProfile prof, long itm_id, DbFigure dbFig)
        throws SQLException, cwException, cwSysMessage {

            DbFigure _dbFig = new DbFigure();
            _dbFig.fig_fgt_id = dbFig.fig_fgt_id;
            _dbFig.fig_value = dbFig.fig_value;
                       
            // Get all runs belong to the item
            aeItem run;
            aeItem itm = new aeItem();
            itm.itm_id = itm_id;
            Vector vec;
            try{
                vec = itm.getRunItemAsVector(con, false, false, null);
            }catch(qdbException e) {
                throw new cwException("Failed to get run items : " + e.getMessage());
            }catch(cwSysMessage e) {
                throw new cwException("Failed to get run items : " + e.getMessage());
            }
            Vector v_app_id = null;
            aeUserFigure aeUfg = new aeUserFigure();
            for(int i=0; i<vec.size(); i++){
                run = (aeItem)vec.elementAt(i);
                ins(con, prof.usr_id, run.itm_id, _dbFig);
                v_app_id = aeApplication.getLatestItmAppnLstWAtt(con, run.itm_id);
                aeUfg.insUserFigure(con, run.itm_id, v_app_id, prof.usr_id, _dbFig.fig_fgt_id);
            }
            return;
        }


    
    /**
    * Update all run item belong to the item
    * check the effective datetime of the run to override the credit value of the run
    */
    public void updChildFigure(Connection con, loginProfile prof, long itm_id, DbFigure dbFig)
        throws SQLException, cwException, cwSysMessage {

            DbFigure _dbFig = new DbFigure();
            // Get the origianl value of item credit
            _dbFig.fig_id = dbFig.fig_id;
            _dbFig.get(con);

            long org_fgt_id = _dbFig.fig_fgt_id;
            _dbFig = new DbFigure();
            _dbFig.fig_fgt_id = dbFig.fig_fgt_id;
            _dbFig.fig_value = dbFig.fig_value;


            // Get all runs belong to the item
            aeItem run;
            long[] id_lst;
            aeItem itm = new aeItem();
            itm.itm_id = itm_id;
            Vector vec;
            
            try{
                vec = itm.getRunItemAsVector(con, false, false, null);
            }catch(qdbException e) {
                throw new cwException("Failed to get item : " + e.getMessage());
            }catch(cwSysMessage e) {
                throw new cwException("Failed to get item : " + e.getMessage());
            }

            
            //check the item credit effective datetime 
            //if start/end datetime within run period
            //give it a warning
            boolean flag = true;
            Vector icvVec = new Vector();
            DbItemFigure dbIfg = new DbItemFigure();

            for(int i=0; i<vec.size(); i++) {
                run = (aeItem)vec.elementAt(i);
                //Assume run will not contain more than one credit value of each cedit type
                id_lst = getFigureValueId(con, run.itm_id, org_fgt_id);
                if( id_lst != null && id_lst.length > 0 ) {
                    _dbFig.fig_id = id_lst[0];
                    upd(con, prof, _dbFig);
                } else 
                    ins(con, prof.usr_id, run.itm_id, _dbFig);
            }

            return;
        }
    
    
    
    /**
    * Check parent CPT insert a valid CPT for itself
    */
    public void insParentFigure(Connection con, String usr_id, long parent_id, aeItem itm)
        throws SQLException, cwException, cwSysMessage {
            
            Vector itmVec = new Vector();
            itmVec.addElement(new Long(parent_id));
            ViewFigure.ViewFigureDetail[] figureDtls = ViewFigure.getFigureDetail(con, itmVec, null, null, null, null);
            DbFigure dbFig = new DbFigure();
            for (int i=0; i<figureDtls.length; i++){
                dbFig.fig_fgt_id = figureDtls[i].fgt_id;
                dbFig.fig_value = figureDtls[i].fig_value;
                ins(con, usr_id, itm.itm_id, dbFig);
            }
            return;
            
        }
    
    
    
    
    /**
    * Delete all runs item credit which withthin the period of the deleted item credit value
    */
    public void delChildFigure(Connection con, long itm_id, Vector v_fig_id)
        throws SQLException, cwException {
            //Get all runs belong to the item
            aeItem itm = new aeItem();
            itm.itm_id = itm_id;
            Vector vec;            
            try{
                vec = itm.getRunItemAsVector(con, false, false, null);
            }catch(qdbException e) {
                throw new cwException("Failed to get item : " + e.getMessage());
            }catch(cwSysMessage e) {
                throw new cwException("Failed to get item : " + e.getMessage());
            }
            DbFigure dbFig = new DbFigure();
            DbItemFigure dbIfg = new DbItemFigure();
            aeUserFigure aeUfg = new aeUserFigure();
            DbUserFigure dbUfg = new DbUserFigure();
            Vector v_del_fig_id = new Vector();
            Vector v_ufg_fig_id = null;
            long fgt_id;
            aeItem run = null;
            long[] id_lst = null;
            for(int i=0; i<v_fig_id.size(); i++) {
                dbFig.fig_id = ((Long)v_fig_id.elementAt(i)).longValue();
                dbFig.get(con);
                for(int j=0; j<vec.size(); j++) {
                    run = (aeItem)vec.elementAt(j);
                    id_lst = getFigureValueId(con, run.itm_id, dbFig.fig_fgt_id);                    
                    if( id_lst != null && id_lst.length > 0 ) {
                        dbIfg.ifg_itm_id = run.itm_id;
                        dbIfg.ifg_fig_id = id_lst[0];
                        dbIfg.del(con);
                        //Add Item Accreditation Value Id to vector for batch delete
                        v_del_fig_id.addElement(new Long(dbIfg.ifg_fig_id));

                        //Remove User Accreditation
                        //Each User not more than one credit type value in each item                        
                        v_ufg_fig_id = ViewFigure.getUserFigureIdByItemId(con, run.itm_id, dbFig.fig_fgt_id);
                        dbUfg.del(con, v_ufg_fig_id);
                        v_del_fig_id.addAll(v_ufg_fig_id);
                    }
                }
            }
            
            if( v_del_fig_id.size() > 0 ) {
                DbFigure.del(con, v_del_fig_id);
            }
            
            return;
        }
    
    
    public boolean isFigureTypeExisted(Connection con, long itm_id, long fgt_id)
        throws SQLException, cwException {
            long[] id_lst = getFigureValueId(con, itm_id, fgt_id);
            if( id_lst == null || id_lst.length == 0 )
                return false;
            else
                return true;
        }
    
    
    
    /**
    *Get all the credit value id belong to the specified item
    *@return long[] contain the credit value id
    */
    public long[] getFigureValueId(Connection con, long itm_id, long fgt_id)
        throws SQLException, cwException {
            Vector v_itm_id = new Vector();
            v_itm_id.addElement(new Long(itm_id));
            return getFigureValueId(con, v_itm_id, fgt_id);
        }
    public long[] getFigureValueId(Connection con, Vector v_itm_id, long fgt_id)
        throws SQLException, cwException {
            
            DbFigure[] figures = ViewFigure.getItemFigure(con, v_itm_id, fgt_id);
            Vector vec = new Vector();
            for (int i=0; i<figures.length; i++){                
                vec.addElement(new Long(figures[i].fig_id));
            }
            
            long[] id_lst = null;
            if( vec.size() > 0 ) {
                id_lst = new long[vec.size()];
                for(int i=0; i<id_lst.length; i++)
                    id_lst[i] = ((Long)vec.elementAt(i)).longValue();
            }
            
            return id_lst;
            
        }
    
    
    
    /**
    * Get the item crediations as XML
    */
    public String getDetailAsXML(Connection con, long itm_id, String[] fgt_type, String[] fgt_subtype, String sortCol, String sortOrder)
        throws SQLException {
            
            Vector itmIdVec = new Vector();
            itmIdVec.addElement(new Long(itm_id));
            StringBuffer xml = new StringBuffer();                        
            
            xml.append("<figure_value_list>").append(cwUtils.NEWL);
            
            ViewFigure.ViewFigureDetail[] figureDtls = ViewFigure.getFigureDetail(con, itmIdVec, fgt_type, fgt_subtype, sortCol, sortOrder);
            for (int i=0; i<figureDtls.length; i++){
                
                xml.append("<figure_value ")
                   .append(" fig_id=\"").append(figureDtls[i].fig_id).append("\" ")
                   .append(" fig_value=\"").append(figureDtls[i].fig_value).append("\" ")
                   .append(" fgt_type=\"").append(figureDtls[i].fgt_type).append("\" ")
                   .append(" fgt_subtype=\"").append(cwUtils.escNull(figureDtls[i].fgt_subtype)).append("\" ")
                   .append(" >").append(cwUtils.NEWL)
                   .append(figureDtls[i].fgt_xml)
                   .append("</figure_value>").append(cwUtils.NEWL);
                
            }
            xml.append("</figure_value_list>").append(cwUtils.NEWL);
            return xml.toString();
        }
    

    /**
    * Get a item accreditation 
    */ 
    public String asXML(Connection con, long fig_id)
        throws SQLException, cwException {
            
            StringBuffer xml = new StringBuffer();
            ViewFigure.ViewFigureDetail[] figureDtls = ViewFigure.getFigureDetail(con, fig_id);
            if( figureDtls.length > 0 ) {
                
                xml.append("<figure_value ")
                   .append(" fig_id=\"").append(figureDtls[0].fig_id).append("\" ")
                   .append(" fig_value=\"").append(figureDtls[0].fig_value).append("\" ")
                   .append(" fgt_type=\"").append(figureDtls[0].fgt_type).append("\" ")
                   .append(" fgt_subtype=\"").append(cwUtils.escNull(figureDtls[0].fgt_subtype)).append("\" ")
                   .append(" >").append(cwUtils.NEWL)
                   .append( figureDtls[0].fgt_xml )
                   .append("</figure_value>");
            } else 
                throw new cwException("Failed to get item credit value, icv_id = " + fig_id);
            return xml.toString();
            
        }
    
    
    
    public static String getItemFigureXML(Connection con, long itm_id, long root_ent_id)
        throws SQLException {
            
            StringBuffer xml = new StringBuffer();
            Hashtable creditValueHash = getItemFigureValue(con, itm_id);
            DbFigureType[] dbFigureTypes = DbItemFigure.getAll(con, root_ent_id);
            for (int i=0; i<dbFigureTypes.length; i++){
                xml.append("<figure_type id=\"").append(dbFigureTypes[i].fgt_id).append("\">").append(cwUtils.NEWL);
                if( creditValueHash.containsKey(new Long(dbFigureTypes[i].fgt_id)) )
                    xml.append("<num value=\"").append(creditValueHash.get(new Long(dbFigureTypes[i].fgt_id))).append("\"/>").append(cwUtils.NEWL);
                xml.append(dbFigureTypes[i].fgt_xml)
                   .append("</figure_type>").append(cwUtils.NEWL);
            }
            return xml.toString();
        }

    public static String getItemRunFigureXML(Connection con, long itm_id)
        throws SQLException {
            
            StringBuffer xml = new StringBuffer();
            Hashtable creditValueHash = getItemFigureValue(con, itm_id);
            DbFigureType[] figureTypes = ViewFigure.getItemFigureDetail(con, itm_id, 0);
            for (int i=0; i<figureTypes.length; i++){
                xml.append("<figure_type id=\"").append(figureTypes[i].fgt_id).append("\">").append(cwUtils.NEWL);
                if( creditValueHash.containsKey(new Long(figureTypes[i].fgt_id)) )
                    xml.append("<num value=\"").append(creditValueHash.get(new Long(figureTypes[i].fgt_id))).append("\"/>").append(cwUtils.NEWL);
                xml.append(figureTypes[i].fgt_xml)
                   .append("</figure_type>").append(cwUtils.NEWL);
            }
            return xml.toString();
        }
    
    
    
    public static String getItemFigureXML(Connection con, long itm_id)
        throws SQLException {
            
            StringBuffer xml = new StringBuffer();
            xml.append("<figure_list>").append(cwUtils.NEWL);
            //ResultSet rs = DbFigureType.getAll(con, owner_ent_id);
            DbFigureType[] figureTypes = ViewFigure.getItemFigureDetail(con, itm_id, 0);
            for (int i=0; i<figureTypes.length; i++){
                xml.append("<figure ")
                   .append(" id=\"").append(figureTypes[i].fgt_id).append("\" ")
                   .append(" type=\"").append(figureTypes[i].fgt_type).append("\" ")
                   .append(" subtype=\"").append(cwUtils.escNull(figureTypes[i].fgt_subtype)).append("\" ")
                   .append(">").append(cwUtils.NEWL)
                   .append(figureTypes[i].fgt_xml)
                   .append("</figure>").append(cwUtils.NEWL);
            }
            xml.append("</figure_list>").append(cwUtils.NEWL);
            return xml.toString();
        }
    
    
    
    
    

    public static Hashtable getItemFigureValue(Connection con, long itm_id)
        throws SQLException{
            
            DbFigure[] figures = ViewFigure.getItemFigure(con, itm_id, 0);
            Hashtable creditTable = new Hashtable();
            for (int i=0; i<figures.length; i++)
                creditTable.put(new Long(figures[i].fig_fgt_id), new Float(figures[i].fig_value));
            return creditTable;
            
        }
    
    
    
    
    /**
    * Copy the item accreditation from parent to itself
    */
    public void insParentFigure(Connection con, loginProfile prof, long parent_itm_id, long self_itm_id)
        throws SQLException, cwException, cwSysMessage {
            DbFigure[] figures = ViewFigure.getAllItemFigure(con, parent_itm_id);
            for (int i=0; i<figures.length; i++){
                ins(con, prof.usr_id, self_itm_id, figures[i]);
            }
            return;
        }
        
        
    public String getAllFigure(Connection con, long root_ent_id, long itm_id) throws SQLException{
        Hashtable h_fig_val = getItemFigureValue(con, itm_id);
        DbFigureType[] figureTypes = DbFigureType.getAll(con, root_ent_id);
        Hashtable h_xml = new Hashtable();
        Vector v_fgt_type = new Vector();
        StringBuffer xml;
        for (int i=0; i<figureTypes.length; i++){
            
            if(v_fgt_type.indexOf(figureTypes[i].fgt_type) == -1)
                v_fgt_type.addElement(figureTypes[i].fgt_type);
            
            if( h_xml.containsKey(figureTypes[i].fgt_type) ) {
                xml = new StringBuffer();
                xml.append(h_xml.get(figureTypes[i].fgt_type));
            } else {
                xml = new StringBuffer();
            }
            xml.append("<figure ");
            if( h_fig_val.containsKey(new Long(figureTypes[i].fgt_id)) ){
                xml.append(" ifg_value=\"").append(h_fig_val.get(new Long(figureTypes[i].fgt_id))).append("\" ");
            }
            xml.append(" id=\"").append(figureTypes[i].fgt_id).append("\" ")
               .append(" type=\"").append(figureTypes[i].fgt_type).append("\" ")
               .append(" subtype=\"").append(cwUtils.escNull(figureTypes[i].fgt_subtype)).append("\" ")
               .append(">").append(cwUtils.NEWL)
               .append(figureTypes[i].fgt_xml)
               .append("</figure>").append(cwUtils.NEWL);
            h_xml.put(figureTypes[i].fgt_type, xml.toString());
        }
        xml = new StringBuffer();
        for(int i=0; i<v_fgt_type.size(); i++){
            xml.append("<figure_list type=\"").append(v_fgt_type.elementAt(i)).append("\">")
               .append(h_xml.get((String)v_fgt_type.elementAt(i)))
               .append("</figure_list>");
        }
        return xml.toString();
    }
    
    public void updItemRunFigure(Connection con, loginProfile prof, long itm_id, Vector v_fgt_id, Vector v_fig_value)
        throws SQLException, cwException, cwSysMessage{
            
            DbFigure dbFig = new DbFigure();
            for(int i=0; i<v_fgt_id.size(); i++){
                dbFig.fig_id = getFigureId(con, itm_id, ((Long)v_fgt_id.elementAt(i)).longValue());
                dbFig.fig_fgt_id = ((Long)v_fgt_id.elementAt(i)).longValue();
                dbFig.fig_value = ((Float)v_fig_value.elementAt(i)).floatValue();
                upd(con, prof, dbFig);
            }
            return;
        }


    
    
    public void updItemFigure(Connection con, loginProfile prof, long itm_id, Vector v_fgt_id, Vector v_fig_value)
        throws SQLException, cwException, cwSysMessage {
            boolean isEmptyLst = false;
            if( v_fgt_id == null || v_fgt_id.isEmpty() ){
                v_fgt_id = new Vector();
                v_fgt_id.addElement(new Long(0));
                isEmptyLst = true;
            }
            aeItem itm = new aeItem();
            itm.itm_id = itm_id;
            Vector v_itm_id = new Vector();
            Vector v_itm;
            try{
                v_itm = itm.getRunItemAsVector(con, false, false, null);
                for(int i=0; i<v_itm.size(); i++)
                    v_itm_id.addElement(new Long(((aeItem)v_itm.elementAt(i)).itm_id));
            }catch(qdbException e) {
                throw new cwException("Failed to get run items : " + e.getMessage());
            }catch(cwSysMessage e) {
                throw new cwException("Failed to get run items : " + e.getMessage());
            }
            v_itm_id.addElement(new Long(itm_id));
            delNotInList(con, v_itm_id, v_fgt_id);
            aeUserFigure aeUfg = new aeUserFigure();
            aeUfg.delNotInList(con, v_itm_id, v_fgt_id);
            
            if( isEmptyLst ) 
                return;

            Vector v_app_id;
            DbFigure dbFig = new DbFigure();
            for(int i=0; i<v_fgt_id.size(); i++){
                dbFig.fig_id = getFigureId(con, itm_id, ((Long)v_fgt_id.elementAt(i)).longValue());
                dbFig.fig_fgt_id = ((Long)v_fgt_id.elementAt(i)).longValue();
                dbFig.fig_value = ((Float)v_fig_value.elementAt(i)).floatValue();
                if( dbFig.fig_id == 0 ) {
                    ins(con, prof.usr_id, itm_id, dbFig);
                    for(int j=0; j<v_itm_id.size()-1; j++) {//becasue last id is course item id
                        ins(con, prof.usr_id, ((Long)v_itm_id.elementAt(j)).longValue(), dbFig);
                        v_app_id = aeApplication.getLatestItmAppnLstWAtt(con, ((Long)v_itm_id.elementAt(j)).longValue());
                        aeUfg.insUserFigure(con, ((Long)v_itm_id.elementAt(j)).longValue(), v_app_id, prof.usr_id, dbFig.fig_fgt_id);
                    }
                }else{
                    upd(con, prof, dbFig);
                }
            }
            return;
        }


    public long getFigureId(Connection con, long itm_id, long fgt_id)
        throws SQLException {
            
            DbFigure[] figures = ViewFigure.getItemFigure(con, itm_id, fgt_id);
            long fig_id = 0;
            if(figures.length>0)
                fig_id = figures[0].fig_id;
            return fig_id;
        }
    
    
    public void delNotInList(Connection con, Vector v_itm_id, Vector v_fgt_id)
        throws SQLException, cwException {
            if( v_itm_id == null || v_itm_id.isEmpty() || v_fgt_id == null || v_fgt_id.isEmpty() )
                return;
            Vector v_fig_id = ViewFigure.getNotInListItemFigId(con, v_itm_id, v_fgt_id);
            DbItemFigure.del(con, v_fig_id);
            DbFigure.del(con, v_fig_id);
            return;
        }
    
}

