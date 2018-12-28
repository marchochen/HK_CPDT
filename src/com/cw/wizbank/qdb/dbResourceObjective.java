package com.cw.wizbank.qdb;

import java.sql.*;
import java.util.*;

import com.cw.wizbank.util.cwUtils;

public class dbResourceObjective
{
    public long         rob_res_id;
    public long         rob_obj_id;

    public dbResourceObjective() {;}

    // check if this object exists in database
    public boolean doesExist(Connection con)
        throws SQLException
    {
        PreparedStatement stmt = null;
        boolean result = false;
        try {
            stmt = con.prepareStatement(
            " SELECT rob_obj_id "
            +   " from ResourceObjective "
            +   " where rob_res_id = ? "
            +   " and rob_obj_id = ? ");

            stmt.setLong(1, rob_res_id);
            stmt.setLong(2, rob_obj_id);
            ResultSet rs = stmt.executeQuery();
            result = rs.next();
        } finally {
            if(stmt!=null) {stmt.close();}
        }
        return result;
    }

    // get the list of objective id given the resource id
    public static Vector getObjId(Connection con, long resId)
        throws qdbException
    {
        try {
            PreparedStatement stmt = con.prepareStatement(
            " SELECT rob_obj_id "
            +   " from ResourceObjective "
            +   " where rob_res_id =? " );

            // set the values for prepared statements
            stmt.setLong(1, resId);

            Vector result = new Vector();
            dbResourceObjective resObj = null;
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
            {
                resObj = new dbResourceObjective();
                resObj.rob_obj_id = rs.getLong("rob_obj_id");
                resObj.rob_res_id = resId;
                result.addElement(resObj);
            }
            stmt.close();

            return result;

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }

    }

    public static long getObjIdByResId(Connection con, long resId) throws SQLException {
    	String sql = "SELECT rob_obj_id from ResourceObjective where rob_res_id =? " ;
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setLong(1, resId);
        ResultSet rs = stmt.executeQuery();
        long obj_id = 0;
        if (rs.next()) {
            obj_id = rs.getLong("rob_obj_id");
        }
        stmt.close();
        return obj_id;
    }
    
    public static HashMap getObj(Connection con,long resId) throws qdbException{
    	HashMap objs = new HashMap();
    	Vector objId = new Vector();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from objective,ResourceObjective where rob_res_id =? and obj_id = rob_obj_id ");
		PreparedStatement pst = null;
		ResultSet rs = null;
		try{
			pst = con.prepareStatement(sql.toString());
			pst.setLong(1,resId);
			rs = pst.executeQuery();
			while(rs.next()){
				long obj_id = rs.getLong("obj_id");
				Long id = new Long(obj_id);
				String obj_status = rs.getString("obj_status");
				objId.add(id);
				objs.put(id,obj_status);
			}
			objs.put("keyVec",objId);
		}catch (Exception e) {
		throw new qdbException(e.getMessage());
	} finally {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException sqle) {
				;
			}
		}
		if (pst != null) {
			try {
				pst.close();
			} catch (SQLException sqle) {
				;
			}
		}
	}
    	return objs;
    }

    public static Vector getAsmObjId(Connection con, long resId) throws qdbException {
        try {
            PreparedStatement stmt =
                con.prepareStatement(
                    " SELECT qcs_obj_id " + " from QueContainerSpec " + " where qcs_res_id = ? ");

            // set the values for prepared statements
            stmt.setLong(1, resId);

            Vector result = new Vector();
            dbResourceObjective resObj = null;
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                resObj = new dbResourceObjective();
                resObj.rob_obj_id = rs.getLong("qcs_obj_id");
                resObj.rob_res_id = resId;
                result.addElement(resObj);
            }
            stmt.close();
            return result;
        } catch (SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }

    }

    // get the child count of an objective (include all hidden resources)
    public static Hashtable getAllResourceCount(Connection con, Vector idVec, String lanCode, String[] types, String[] subtypes, String privilege, String owner)
        throws qdbException
    {
        try {
            Hashtable objHash = new Hashtable();
            if (idVec == null || idVec.size()==0) {
                return objHash;
            }

            StringBuffer sqlBuf = new StringBuffer();
            sqlBuf.append(" SELECT count(rob_res_id) OBJCNT ,  rob_obj_id OBJID ")
                  .append(" FROM ResourceObjective ")
                  .append(" WHERE rob_obj_id IN ").append(cwUtils.vector2list(idVec));

            sqlBuf.append(" GROUP BY rob_obj_id ORDER BY rob_obj_id ");

            PreparedStatement stmt = con.prepareStatement(sqlBuf.toString());
            Vector resIdVec = new Vector();
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
            {
                Long OBJID_ = new Long(rs.getLong("OBJID"));
                Long OBJCNT_ = new Long(rs.getLong("OBJCNT"));
                objHash.put(OBJID_, OBJCNT_);
            }
            stmt.close();

            return objHash;

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    // get the child count of an objective
    // Should be placed in ResourceObjective
    public static Hashtable getResourceCount(Connection con, Vector idVec, String lanCode, String[] types, String privilege, String owner)
        throws qdbException
    {
        return getResourceCount(con, idVec, lanCode, types, null, privilege, owner, null);
    }
    
    // get the child count of an objective
    // Should be placed in ResourceObjective
    public static Hashtable getResourceCount(Connection con, Vector idVec, String lanCode, String[] types, String[] subtypes, String privilege, String owner, String status)
        throws qdbException
    {
        try {
            Hashtable objHash = new Hashtable();
            if (idVec == null || idVec.size()==0) {
                return objHash;
            }

            StringBuffer sqlBuf = new StringBuffer();
            sqlBuf.append(" SELECT count(rob_res_id) OBJCNT ,  rob_obj_id OBJID ")
                  .append(" FROM ResourceObjective ")
                  .append(" WHERE rob_obj_id IN ").append(cwUtils.vector2list(idVec))
                  .append(" AND rob_res_id IN ")
                  .append(" (SELECT res_id from Resources WHERE res_res_id_root is null ")
                  // The resources that the user can acccess
                  .append("     AND res_privilege = ? ");

            if (privilege != null && privilege.equalsIgnoreCase(dbResource.RES_PRIV_AUTHOR)) {
                sqlBuf.append("     AND res_usr_id_owner = '" + owner + "' ");
            }

            if (lanCode != null){
            	sqlBuf.append("           AND res_lan  =  ? ");
            }

            if (status != null) {
                sqlBuf.append("           AND res_status = ? ");
            }
            
            if (types == null || types.length == 0 || types[0]==null || types[0].length() == 0) {
                types = new String[3];
                types[0] = dbResource.RES_TYPE_QUE;
                types[1] = dbResource.RES_TYPE_GEN;
                types[2] = dbResource.RES_TYPE_AICC;
            }

            for (int i=0; i<types.length; i++) {
                if (types[i] != null && types[i].length() > 0) {
                    if (i==0){
                    	 sqlBuf.append("    AND ( ");
                    }else{
                    	 sqlBuf.append("    OR ");
                    }
                    
                    //sqlBuf.append("  res_type = '").append(types[i]).append("' ");
                    sqlBuf.append("  res_type = ? ");

                    if (i== types.length -1){
                    	sqlBuf.append(" ) ");
                    }
                }
            }
            if (subtypes != null) {
                for (int i = 0; i < subtypes.length; i++) {
                    if (subtypes[i] != null && subtypes[i].length() > 0) {
                        if (i == 0){
                        	sqlBuf.append("    AND ( ");
                        } else{
                        	sqlBuf.append("    OR ");
                        }

                        sqlBuf.append("  res_subtype = '").append(subtypes[i]).append("' ");

                        if (i == subtypes.length - 1){
                        	sqlBuf.append(" ) ");
                        }
                    }
                }
            }


            sqlBuf.append(" ) GROUP BY rob_obj_id ORDER BY rob_obj_id ");

            PreparedStatement stmt = con.prepareStatement(sqlBuf.toString());

            // set the values for prepared statements
            int index = 1;
            stmt.setString(index++, privilege);
            if (lanCode != null){
            	stmt.setString(index++, lanCode);
            }

            if (status != null) {
            	stmt.setString(index++, status);
            }
            for (int i=0; i<types.length; i++) {
            	if (types[i] != null && types[i].length() > 0) {
            		stmt.setString(index++, types[i]);
            	}
            }
            Vector resIdVec = new Vector();
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
            {
                Long OBJID_ = new Long(rs.getLong("OBJID"));
                Long OBJCNT_ = new Long(rs.getLong("OBJCNT"));
                objHash.put(OBJID_, OBJCNT_);
            }
            stmt.close();

            return objHash;

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }

    }


    // get the list of resource id given a objective id
    public static Vector getResId(Connection con, long objId)
        throws qdbException
    {
        try {
            PreparedStatement stmt = con.prepareStatement(
            " SELECT rob_res_id "
            +   " from ResourceObjective "
            +   " where rob_obj_id =? " );

            // set the values for prepared statements
            stmt.setLong(1, objId);

            Vector result = new Vector();
            dbResourceObjective resObj = null;
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
            {
                resObj = new dbResourceObjective();
                resObj.rob_obj_id = objId;
                resObj.rob_res_id = rs.getLong("rob_res_id");
                result.addElement(resObj);
            }
            stmt.close();

            return result;

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }

    }

    // get the child count of an objective
/*
    public static long getResourceCount(Connection con, long objId, String lanCode, String[] types, loginProfile prof)
        throws qdbException
    {
        try {
            String SQL =
            " SELECT rob_res_id "
            +   " from ResourceObjective "
            +   " where rob_obj_id = ?  and "
            +   "  rob_res_id in "
            +   "    (SELECT res_id from Resources WHERE res_res_id_root is null ";

            if (lanCode != null)
                SQL +=   "           AND res_lan  = '" + lanCode + "' ";

            if (types == null || types.length == 0 || types[0]==null || types[0].length() == 0) {
                types = new String[3];
                types[0] = dbResource.RES_TYPE_QUE;
                types[1] = dbResource.RES_TYPE_GEN;
                types[2] = dbResource.RES_TYPE_AICC;
            }

            if (types !=null && types.length > 0 && types[0]!=null && types[0].length() > 0) {
                for (int i=0; i<types.length; i++) {
                    if (types[i] != null && types[i].length() > 0) {
                        if (i==0)
                            SQL += "    AND ( " ;
                        else
                            SQL += "    OR " ;

                        SQL += "  res_type = '" + types[i] + "' " ;

                        if (i== types.length -1)
                            SQL += " ) ";
                    }
                }
            }


            SQL += " ) ";

            PreparedStatement stmt = con.prepareStatement(SQL);

            // set the values for prepared statements
            stmt.setLong(1, objId);

            Vector resIdVec = new Vector();
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
            {
                resIdVec.addElement(new Long(rs.getLong("rob_res_id")));
            }

            if (resIdVec.size() ==0)
                return 0;

            Vector rpmVec = dbResourcePermission.getResPermission(con, resIdVec, prof);

            long qCount = 0;
            for(int i=0;i<rpmVec.size();i++) {
                dbResourcePermission dbrpm = new dbResourcePermission();
                dbrpm = (dbResourcePermission) rpmVec.elementAt(i);
                if (dbrpm.rpm_read  || dbrpm.rpm_write || dbrpm.rpm_execute)
                    qCount += 1;
            }

            return qCount;

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }

    }
*/

    // get the list of objective id given the resource id
    public static Vector getResObj(Connection con, String[] que_id_lst)
        throws qdbException
    {
        try {
            String id_lst = dbUtils.array2list(que_id_lst);

            PreparedStatement stmt = con.prepareStatement(
            " SELECT rob_res_id, rob_obj_id "
            +   " from ResourceObjective "
            +   " where rob_res_id IN "
            +   id_lst );


            Vector result = new Vector();
            Vector idVec = new Vector();
            Vector objVec = new Vector();

            ResultSet rs = stmt.executeQuery();
            while(rs.next())
            {
                idVec.addElement(new Long(rs.getLong("rob_res_id")));
                objVec.addElement(new Long(rs.getLong("rob_obj_id")));
            }
            stmt.close();
            if (idVec.size() != objVec.size())
                throw new qdbException("Failed to get resource objective.");

            result.addElement(idVec);
            result.addElement(objVec);
            return result;

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }

    }

    // by cliff, 2001/6/13
    // remove all objective relation with the resource
    public void removeResObj(Connection con, long[] lstResID) throws qdbException {
        PreparedStatement stmt = null;
        for (int i=0; i<lstResID.length; i++) {
            try {
                stmt = con.prepareStatement(
                " DELETE from ResourceObjective "
                +   " where rob_res_id = ? " );

                // set the values for prepared statements
                stmt.setLong(1, lstResID[i]);
                int stmtResult=stmt.executeUpdate();
                stmt.close();
                /**
                 * no need to check
                 * emily, 2002-10-11
                 */
//                if ( stmtResult!=1)
//                {
//                    // rollback at qdbAction
//                    //con.rollback();
//                    throw new qdbException("Failed to remove resource-objective.");
//                }
            } catch(SQLException e) {
                throw new qdbException("SQL Error: " + e.getMessage());
            }
        }
    }

    // by cliff, 2001/4/18
    public void insResObj(Connection con, long[] lstResID, long[] lstObjID) throws qdbException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        for (int i=0; i<lstResID.length; i++) {
            for (int j=0; j<lstObjID.length; j++) {
                try  {
                    stmt = con.prepareStatement(
                    " SELECT rob_res_id "
                    +   " from ResourceObjective "
                    +   " where rob_res_id = ? and rob_obj_id = ? " );

                    // set the values for prepared statements
                    stmt.setLong(1, lstResID[i]);
                    stmt.setLong(2, lstObjID[j]);

                    rs = stmt.executeQuery();
                    if(rs.next()) {
                        stmt.close();
                        continue;
                    }
                    else {
                        stmt.close();
                        stmt = con.prepareStatement(
                                " INSERT INTO ResourceObjective "
                                + " (rob_res_id, "
                                + " rob_obj_id ) "
                                + " VALUES (?,?) ");

                        stmt.setLong(1, lstResID[i]);
                        stmt.setLong(2, lstObjID[j]);
                        int stmtResult=stmt.executeUpdate();
                        stmt.close();
                        if ( stmtResult!=1)
                        {
                            // rollback at qdbAction
                            //con.rollback();
                            throw new qdbException("Failed to insert resource-objective.");
                        }

                    }
                } catch(SQLException e) {
                    throw new qdbException("SQL Error: " + e.getMessage());
                }
            }
        }
    }






    /**
    * Get the cloned questions resource id and it's objectives id
    * @param objective id in vector
    * @param question type in array
    * @return hashtable res_id as key, obj_id as value
    */
    public static Hashtable getObjQueTable(Connection con, Vector objIdVec, String[] queType, int difficulty, String privilege)
        throws SQLException {

            Hashtable objQueTable = new Hashtable();
            if( objIdVec == null || objIdVec.isEmpty() )
                return objQueTable;
            dbResourceObjective[] dbResObjs = getObjQue(con, objIdVec, queType, difficulty, privilege);
            for (int i=0; i<dbResObjs.length; i++){
                objQueTable.put( new Long(dbResObjs[i].rob_res_id), new Long(dbResObjs[i].rob_obj_id) );
            }
            return objQueTable;
        }
    public static dbResourceObjective[] getObjQue(Connection con, Vector objIdVec, String[] queType, int difficulty, String privilege)
        throws SQLException {
            Vector vTmp = new Vector();                
/*
            String SQL = " SELECT RES2.res_id res_id, rob_obj_id "
                       + " FROM ResourceObjective, Resources RES1, Resources RES2"
                       + " WHERE rob_obj_id IN " + cwUtils.vector2list(objIdVec)
                       + " AND rob_res_id = RES1.res_id ";
                       if( difficulty > 0 )
                            SQL += " AND RES1.res_difficulty = ? ";
                       if( privilege != null && privilege.length() > 0 )
                            SQL += " AND RES1.res_privilege = ? ";

                       if( queType != null && queType.length > 0 ) {
                            SQL += " AND RES1.res_subtype IN ( ? ";
                            for(int i=1; i<queType.length; i++)
                                SQL += " ,? ";
                            SQL += " ) ";
                       }
                   SQL+= " AND RES1.res_id = RES2.res_res_id_root "
                       + " AND RES1.res_upd_date = RES2.res_upd_date "
                       + " AND RES1.res_res_id_root is NULL ";
*/

            String SQL = " SELECT rob_res_id , rob_obj_id "
                       + " FROM ResourceObjective, Resources "
                       + " WHERE rob_obj_id IN " + cwUtils.vector2list(objIdVec)
                       + " AND res_type = ? "
                       + " AND rob_res_id = res_id AND res_res_id_root is null ";
                       if( difficulty > 0 )
                            SQL += " AND res_difficulty = ? ";
                       if( privilege != null && privilege.length() > 0 )
                            SQL += " AND res_privilege = ? " ;
                       if( queType != null && queType.length > 0 ) {
                            SQL += " AND res_subtype IN ( ? ";
                            for(int i=1; i<queType.length; i++)
                                SQL += " ,? ";
                            SQL += " ) ";
                       }

            PreparedStatement stmt = con.prepareStatement(SQL);
            int index = 1;
            stmt.setString(index++, "QUE");
            if( difficulty > 0 )
                stmt.setLong(index++, difficulty);
            if( privilege != null && privilege.length() > 0 )
                stmt.setString(index++, privilege);
            if( queType != null && queType.length > 0 )
                for(int i=0; i<queType.length; i++) {
                    stmt.setString( (i+index), queType[i] );
                }
                
            dbResourceObjective dbResObj = null;
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                dbResObj = new dbResourceObjective();
                dbResObj.rob_res_id = rs.getLong("rob_res_id");
                dbResObj.rob_obj_id = rs.getLong("rob_obj_id");
                vTmp.addElement(dbResObj);
            }
            stmt.close();
            
            dbResourceObjective result[] = new dbResourceObjective[vTmp.size()];
            result = (dbResourceObjective[])vTmp.toArray(result);
            return result;
        }



}