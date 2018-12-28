package com.cwn.wizbank.entity;

import java.sql.Timestamp;
import java.util.Date;

public class ImsLog implements java.io.Serializable{

	private static final long serialVersionUID = 481508853213188842L;

    public Long ilg_id;
    public String ilg_create_usr_id;
    public Timestamp ilg_create_timestamp;
    public String ilg_type;
    public String ilg_process;
    public String ilg_filename;
    public String ilg_desc;
    public String ilg_method;
    public boolean ilg_dup_data_update_ind;
    public String ilg_target_id;
    public Long ilg_tcr_id;
    
    /*
     * 用户名
     */
    public String usr_display_bil;
    

    /*
     * 文件的路径
     */
    public String file_uri;
    /*
     * success文件的路径
     */
    public String success_file_uri;
    /*
     * unsuccess文件的路径
     */
    public String unsuccess_file_uri;
    /*
     * error文件的路径
     */
    public String error_file_uri;
    
    
    
    
    public Long getIlg_id() {
        return ilg_id;
    }
    public void setIlg_id(Long ilg_id) {
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
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    public String getUsr_display_bil() {
        return usr_display_bil;
    }
    public void setUsr_display_bil(String usr_display_bil) {
        this.usr_display_bil = usr_display_bil;
    }
    public String getFile_uri() {
        return file_uri;
    }
    public void setFile_uri(String file_uri) {
        this.file_uri = file_uri;
    }
    public String getSuccess_file_uri() {
        return success_file_uri;
    }
    public void setSuccess_file_uri(String success_file_uri) {
        this.success_file_uri = success_file_uri;
    }
    public String getUnsuccess_file_uri() {
        return unsuccess_file_uri;
    }
    public void setUnsuccess_file_uri(String unsuccess_file_uri) {
        this.unsuccess_file_uri = unsuccess_file_uri;
    }
    public String getError_file_uri() {
        return error_file_uri;
    }
    public void setError_file_uri(String error_file_uri) {
        this.error_file_uri = error_file_uri;
    }
	
    
    
	
}
