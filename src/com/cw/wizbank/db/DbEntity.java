package com.cw.wizbank.db;

//import java.util.*;
import java.sql.*;
import com.cw.wizbank.util.*;
import com.cw.wizbank.db.sql.*;
import com.cw.wizbank.qdb.dbEntity;


public class DbEntity extends dbEntity{
    // pre define ent_id
    public void updSynDate(Connection con) throws SQLException{
        PreparedStatement stmt = null;
        try{
            ent_syn_date = cwSQL.getTime(con);
            
            stmt = con.prepareStatement(SqlStatements.sql_upd_entity_syn_date);
            
            stmt.setTimestamp(1,ent_syn_date);
            stmt.setLong(2,ent_id);

            if(stmt.executeUpdate() != 1 )
            {
            	stmt.close();
                con.rollback();
                throw new SQLException("Fails to update user syn time.");
            }
        }finally{
            if (stmt!=null)     stmt.close();    
        }
        return;
        
    }
    
    public Timestamp getSynDate(Connection con) throws SQLException{
        PreparedStatement stmt = null;
        try{
            // Fai: for soft-delete
            stmt = con.prepareStatement("SELECT ent_syn_date from Entity WHERE ent_id = ? AND ent_delete_usr_id IS NULL AND ent_delete_timestamp IS NULL ");
            stmt.setLong(1, ent_id);
            ResultSet rs = stmt.executeQuery();
          
            if(rs.next()){
                ent_syn_date =  rs.getTimestamp("ent_syn_date");
            }
        }finally{
            if (stmt!=null)     stmt.close();
        }
        return ent_syn_date;
    }
}