package com.cw.wizbank.content;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;
import com.cw.wizbank.util.cwException;

import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.qdb.dbModule;

public class CosEvaluation extends Survey{
    
	public static String getCosEvalLstAsXML(Connection con, long root_ent_id, String usr_id, boolean isStudyGroupMod,long sgp_id, Vector modTcrIds,int curPage,int pageSize)
        throws cwException, qdbException, SQLException
    {
    	Vector vtMod = dbModule.getPublicModLst(con, root_ent_id, dbModule.MOD_TYPE_SVY, false, isStudyGroupMod,sgp_id, modTcrIds);
        Vector vtModId = modLst2ModId(vtMod);
        if (vtModId.size()==0){
            vtModId.addElement(new Long(0));
        }
        StringBuffer xml = new StringBuffer();
        xml.append("<evaluation_list>")
           .append(dbModule.getModLstAsXML(con, vtMod, null, curPage,pageSize))
           .append("</evaluation_list>");
        
        return xml.toString();        
    }
    
    public static Vector modLst2ModId(Vector vtMod){
        Vector vtModId = new Vector();
        for (int i=0; i<vtMod.size(); i++){
            dbModule dbmod = (dbModule) vtMod.elementAt(i);                        
            vtModId.addElement(new Long(dbmod.mod_res_id));
        }
        return vtModId;
    } 

    
}