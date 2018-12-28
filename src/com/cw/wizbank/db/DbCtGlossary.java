package com.cw.wizbank.db;

import java.sql.*;
import java.util.*;

import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.util.*;
import com.cwn.wizbank.utils.CommonLog;



public class DbCtGlossary {

    public long glo_res_id;
    public long glo_id;
    public String glo_keyword;
    public String glo_definition;
    public Timestamp glo_create_timestamp;
    public String glo_create_usr_id;
    public Timestamp glo_update_timestamp;
    public String glo_update_usr_id;


    /**
     * CLOB column
     * Table:       ctGlossary
     * Column:      glo_definition
     * Nullable:    NO
     */
    public final static String[] LETTER = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
                                            "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "OTHER" };

    public DbCtGlossary() { ; }


    // insert a keyword and definition
    public void ins(Connection con)
        throws SQLException, cwException {

        // << BEGIN for oracle migration!
        String DbCtGlossary_INS   =    " INSERT INTO ctGlossary "
                                +    " ( glo_res_id, "
                                +    "   glo_keyword, "
                                +    "   glo_definition, "// NOT NULLABLE!
                                +    "   glo_create_usr_id, "
                                +    "   glo_create_timestamp, "
                                +    "   glo_update_usr_id, "
                                +    "   glo_update_timestamp )"
                                +    " VALUES ( ?, ?, " + cwSQL.getClobNull() + ", ?, ?, ?, ? ) ";


        int count = 1;
        PreparedStatement stmt = con.prepareStatement(DbCtGlossary_INS, PreparedStatement.RETURN_GENERATED_KEYS);
        stmt.setLong(count++, glo_res_id);
        stmt.setString(count++, glo_keyword);
        //stmt.setString(count++, glo_definition);
        stmt.setString(count++, glo_create_usr_id);
        stmt.setTimestamp(count++,glo_create_timestamp);
        stmt.setString(count++,glo_update_usr_id);
        stmt.setTimestamp(count++,glo_update_timestamp);

        if( stmt.executeUpdate() != 1 )
            throw new cwException("Failed to insert Key in Glossary.");
        glo_id = cwSQL.getAutoId(con, stmt, "ctGlossary", "glo_id");
        stmt.close();

        // Update glo_definition
        // construct the condition
        String condition = "glo_id = " + glo_id;
        // construct the column & value
        String[] columnName = new String[1];
        String[] columnValue = new String[1];
        columnName[0] = "glo_definition";
        columnValue[0] = glo_definition;
        cwSQL.updateClobFields(con, "ctGlossary", columnName, columnValue, condition);
        // >> END

        return;
    }



    // update the keyword and definition
    public void upd(Connection con)
        throws SQLException, cwException {

        // << BEGIN for oracle migration!
        String DbCtGlossary_UPD = " UPDATE ctGlossary SET "
                              + " glo_keyword = ?, "
                              //+ " glo_definition = ?, "
                              + " glo_update_timestamp = ?, "
                              + " glo_update_usr_id = ? "
                              + " WHERE glo_id = ? ";

        PreparedStatement stmt = con.prepareStatement(DbCtGlossary_UPD);
        int count = 1;
        stmt.setString(count++, glo_keyword);
        //stmt.setString(count++, glo_definition);
        stmt.setTimestamp(count++, glo_update_timestamp);
        stmt.setString(count++,glo_update_usr_id);
        stmt.setLong(count++,glo_id);

        if( stmt.executeUpdate() != 1 )
            throw new cwException("Failed to update Key in Glossary.");

        stmt.close();

        // Update glo_definition
        // construct the condition
        String condition = "glo_id = " + glo_id;
        // construct the column & value
        String[] columnName = new String[1];
        String[] columnValue = new String[1];
        columnName[0] = "glo_definition";
        columnValue[0] = glo_definition;
        cwSQL.updateClobFields(con, "ctGlossary", columnName, columnValue, condition);
        // >> END

        return;
    }


	// del the keyword and definition
	public void del(Connection con)
		throws SQLException, cwException {

		String DbCtGlossary_DEL = " DELETE FROM ctGlossary "
							  + " WHERE glo_id = ? ";

		PreparedStatement stmt = con.prepareStatement(DbCtGlossary_DEL);
		stmt.setLong(1, glo_id);

		if( stmt.executeUpdate() != 1 )
			throw new cwException("Failed to delete Key in Glossary.");

		stmt.close();
		return;

	}

	// del the keyword and definition
	public void delByResId(Connection con)
		throws SQLException, cwException {

		String DbCtGlossary_DEL = " DELETE FROM ctGlossary "
							  + " WHERE glo_res_id = ? ";

		PreparedStatement stmt = con.prepareStatement(DbCtGlossary_DEL);
		stmt.setLong(1, glo_res_id);

		stmt.executeUpdate();
		stmt.close();
		return;

	}


    // Construct a Hashtable store the glossary id of each letter
    public Hashtable getIndex(Connection con)
        throws SQLException {

        String keyword;
        char firstChar;
        long _glo_id;
        Vector[] letter = new Vector[27];
        for(int i=0; i<27; i++)
            letter[i] = new Vector();

        Hashtable letterIdTable = new Hashtable();

        String DbCtGlossary_GET_INDEX =  " SELECT glo_id, glo_keyword FROM ctGlossary, Module WHERE glo_id > 0  AND  glo_res_id = " + cwSQL.replaceNull("mod_mod_res_id_parent", "mod_res_id") + " and mod_res_id = ? ";

        PreparedStatement stmt = con.prepareStatement(DbCtGlossary_GET_INDEX);
        stmt.setLong(1,glo_res_id);

        ResultSet rs = stmt.executeQuery();
        while( rs.next() ) {
            _glo_id = rs.getLong("glo_id");
            keyword = rs.getString("glo_keyword");
            firstChar = keyword.toUpperCase().charAt(0);

            switch(firstChar) {
                case 'A':
                    letter[0].addElement(new Long(_glo_id));
                    break;
                case 'B':
                    letter[1].addElement(new Long(_glo_id));
                    break;
                case 'C':
                    letter[2].addElement(new Long(_glo_id));
                    break;
                case 'D':
                    letter[3].addElement(new Long(_glo_id));
                    break;
                case 'E':
                    letter[4].addElement(new Long(_glo_id));
                    break;
                case 'F':
                    letter[5].addElement(new Long(_glo_id));
                    break;
                case 'G':
                    letter[6].addElement(new Long(_glo_id));
                    break;
                case 'H':
                    letter[7].addElement(new Long(_glo_id));
                    break;
                case 'I':
                    letter[8].addElement(new Long(_glo_id));
                    break;
                case 'J':
                    letter[9].addElement(new Long(_glo_id));
                    break;
                case 'K':
                    letter[10].addElement(new Long(_glo_id));
                    break;
                case 'L':
                    letter[11].addElement(new Long(_glo_id));
                    break;
                case 'M':
                    letter[12].addElement(new Long(_glo_id));
                    break;
                case 'N':
                    letter[13].addElement(new Long(_glo_id));
                    break;
                case 'O':
                    letter[14].addElement(new Long(_glo_id));
                    break;
                case 'P':
                    letter[15].addElement(new Long(_glo_id));
                    break;
                case 'Q':
                    letter[16].addElement(new Long(_glo_id));
                    break;
                case 'R':
                    letter[17].addElement(new Long(_glo_id));
                    break;
                case 'S':
                    letter[18].addElement(new Long(_glo_id));
                    break;
                case 'T':
                    letter[19].addElement(new Long(_glo_id));
                    break;
                case 'U':
                    letter[20].addElement(new Long(_glo_id));
                    break;
                case 'V':
                    letter[21].addElement(new Long(_glo_id));
                    break;
                case 'W':
                    letter[22].addElement(new Long(_glo_id));
                    break;
                case 'X':
                    letter[23].addElement(new Long(_glo_id));
                    break;
                case 'Y':
                    letter[24].addElement(new Long(_glo_id));
                    break;
                case 'Z':
                    letter[25].addElement(new Long(_glo_id));
                    break;
                default :
                    letter[26].addElement(new Long(_glo_id));
                    break;
            }
        }
        stmt.close();
        for(int i=0; i<27; i++) {
            letterIdTable.put(LETTER[i], letter[i]);
        }

        return letterIdTable;
    }


    // Construct a vector with elements keyword, definition and glossary id
    public Vector getContent(long[] glo_ids, Connection con)
        throws SQLException{

        Vector keyDefList = new Vector();
        Vector keyVec = new Vector();
        Vector defVec = new Vector();
        Vector idVec = new Vector();

        String DbCtGlossary_GET_CONTENT = " SELECT glo_keyword, glo_definition, glo_id FROM ctGlossary "
                                      + " WHERE glo_id IN " + dbUtils.array2list(glo_ids)
                                      + " ORDER BY glo_keyword asc ";


        PreparedStatement stmt = con.prepareStatement(DbCtGlossary_GET_CONTENT);
        ResultSet rs = stmt.executeQuery();
        while ( rs.next() ) {
            keyVec.addElement((String)rs.getString("glo_keyword"));
            // << BEGIN for oracle migration!
            defVec.addElement((String)cwSQL.getClobValue(rs, "glo_definition"));
            // >> END
            idVec.addElement( new Long(rs.getLong("glo_id")));
        }
        stmt.close();
        keyDefList.addElement(keyVec);
        keyDefList.addElement(defVec);
        keyDefList.addElement(idVec);
        return keyDefList;
    }



    // Get resource id of the glossary
    public long getResId(Connection con)
        throws SQLException, cwException {

            String DbCtGlossary_GET_RES_ID = " SELECT glo_res_id FROM ctGlossary WHERE glo_id = ? ";
            PreparedStatement stmt = con.prepareStatement(DbCtGlossary_GET_RES_ID);
            stmt.setLong(1, glo_id);
            ResultSet rs = stmt.executeQuery();
            long res_id = 0;
            if( rs.next() ) {
            	res_id = rs.getLong("glo_res_id");
            } else {
                stmt.close();
                throw new cwException("Error : cannot find res_id !");
            }
            stmt.close();
            return res_id;
        }
        
    public static Vector getGloList(Connection con, int id) throws SQLException{
       Vector vtGlo = new Vector();
       String sql = "select glo_keyword, glo_definition FROM ctGlossary, Module "
                  + "where glo_res_id = " + cwSQL.replaceNull("mod_mod_res_id_parent", "mod_res_id") + " and mod_res_id = ? ";
       PreparedStatement stmt = con.prepareStatement(sql);
       stmt.setLong(1,id);
       ResultSet rs = stmt.executeQuery();
       while (rs.next()) {
            DbCtGlossary dbglo = new DbCtGlossary();
            dbglo.glo_keyword = rs.getString("glo_keyword");
            dbglo.glo_definition = cwSQL.getClobValue(rs, "glo_definition");
            CommonLog.debug(dbglo.glo_keyword);
            CommonLog.debug(dbglo.glo_definition);
            vtGlo.addElement(dbglo);
        }
       stmt.close();
           return vtGlo;
       }
}