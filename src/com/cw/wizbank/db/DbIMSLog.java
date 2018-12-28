package com.cw.wizbank.db;

import java.sql.*;

import org.springframework.format.annotation.DateTimeFormat;

import com.cw.wizbank.util.cwSQL;


public class DbIMSLog{
    
    public long ilg_id;
    public String ilg_create_usr_id;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:ss")
    public Timestamp ilg_create_timestamp;
    public String ilg_type;
    public String ilg_process;
    public String ilg_filename;
    public String ilg_desc;
    public String ilg_method;
    public boolean ilg_dup_data_update_ind;
    public String ilg_target_id;
    public Long ilg_tcr_id;
    public String success_txt;
    public String failure_txt;
    
    public long getIlg_id() {
		return ilg_id;
	}

	public void setIlg_id(long ilg_id) {
		this.ilg_id = ilg_id;
	}

	public String getIlg_create_usr_id() {
		return ilg_create_usr_id;
	}

	public void setIlg_create_usr_id(String ilg_create_usr_id) {
		this.ilg_create_usr_id = ilg_create_usr_id;
	}

	public Timestamp getIlg_create_timestamp() {
		return ilg_create_timestamp;
	}

	public void setIlg_create_timestamp(Timestamp ilg_create_timestamp) {
		this.ilg_create_timestamp = ilg_create_timestamp;
	}

	public String getIlg_type() {
		return ilg_type;
	}

	public void setIlg_type(String ilg_type) {
		this.ilg_type = ilg_type;
	}

	public String getIlg_process() {
		return ilg_process;
	}

	public void setIlg_process(String ilg_process) {
		this.ilg_process = ilg_process;
	}

	public String getIlg_filename() {
		return ilg_filename;
	}

	public void setIlg_filename(String ilg_filename) {
		this.ilg_filename = ilg_filename;
	}

	public String getIlg_desc() {
		return ilg_desc;
	}

	public void setIlg_desc(String ilg_desc) {
		this.ilg_desc = ilg_desc;
	}

	public String getIlg_method() {
		return ilg_method;
	}

	public void setIlg_method(String ilg_method) {
		this.ilg_method = ilg_method;
	}

	public boolean isIlg_dup_data_update_ind() {
		return ilg_dup_data_update_ind;
	}

	public void setIlg_dup_data_update_ind(boolean ilg_dup_data_update_ind) {
		this.ilg_dup_data_update_ind = ilg_dup_data_update_ind;
	}

	public String getIlg_target_id() {
		return ilg_target_id;
	}

	public void setIlg_target_id(String ilg_target_id) {
		this.ilg_target_id = ilg_target_id;
	}

	public Long getIlg_tcr_id() {
		return ilg_tcr_id;
	}

	public void setIlg_tcr_id(Long ilg_tcr_id) {
		this.ilg_tcr_id = ilg_tcr_id;
	}
	
	public String getSuccess_txt() {
        return success_txt;
    }

    public void setSuccess_txt(String success_txt) {
        this.success_txt = success_txt;
    }

    public String getFailure_txt() {
        return failure_txt;
    }

    public void setFailure_txt(String failure_txt) {
        this.failure_txt = failure_txt;
    }

    /**
    * Insert record to database and return the record id
    */
    public static  long ins(Connection con, DbIMSLog dbIlg)
        throws SQLException {
            
            //Insert record
            
            String SQL = " INSERT INTO IMSLog ( "
                       + " ilg_create_usr_id, ilg_create_timestamp, "
                       + " ilg_type, ilg_process, "
                       + " ilg_filename, ilg_desc, "
                       + " ilg_method, ilg_target_id,ilg_tcr_id) "
                       + " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?,?) ";
            
            PreparedStatement stmt = con.prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS);
            int index = 1;
            stmt.setString(index++, dbIlg.ilg_create_usr_id);
            stmt.setTimestamp(index++, dbIlg.ilg_create_timestamp);
            stmt.setString(index++, dbIlg.ilg_type);
            stmt.setString(index++, dbIlg.ilg_process);
            stmt.setString(index++, dbIlg.ilg_filename);
            stmt.setString(index++, dbIlg.ilg_desc);
            stmt.setString(index++, dbIlg.ilg_method);
            stmt.setString(index++, dbIlg.ilg_target_id);
            stmt.setLong(index++, dbIlg.ilg_tcr_id);
            stmt.executeUpdate();
            
            //Get record id
            dbIlg.ilg_id = cwSQL.getAutoId(con, stmt, "IMSLog", "ilg_id");
            stmt.close();
            
            return dbIlg.ilg_id;
        }

}