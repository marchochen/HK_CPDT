package com.cwn.wizbank.entity;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

public class CpdRegistration implements java.io.Serializable{

	private static final long serialVersionUID = 8744790597805894561L;
	
	private Long cr_id;
	private Long cr_usr_ent_id;
	private Long cr_ct_id;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	public Date cr_reg_datetime;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")   
	private Date cr_de_reg_datetime;
	
	private Long cr_create_usr_ent_id;
	private Date cr_create_datetime;
	private Long cr_update_usr_ent_id;
	private Date cr_update_datetime;
	private String cr_status;
	private String cr_reg_number;
	public String upload_desc;
	public String srcFile;
	public String CPD_group_code;
	public String license_type;
	private Date cgr_initial_datetime;
	private Date cgr_expiry_datetime;
	private Long cr_cg_id;
	RegUser user;
	CpdType cpdType;
	
	//其他所需属性
    private List exportUserIds; //选择的用户ID
	private List exportGroupIds; //选择的用户组ID
	private List exportCpdTypeIds; //选择的大牌组ID
	private List<CpdRegistration> cpdRegistrationList; 
	private List<CpdGroupRegistration> cpdGroupRegistrationList;  //该大牌关联的已注册的小牌
	
	
	
	public String getLicense_type() {
		return license_type;
	}
	public void setLicense_type(String license_type) {
		this.license_type = license_type;
	}
	public Long getCr_cg_id() {
		return cr_cg_id;
	}
	public void setCr_cg_id(Long cr_cg_id) {
		this.cr_cg_id = cr_cg_id;
	}
	public String getCPD_group_code() {
		return CPD_group_code;
	}
	public void setCPD_group_code(String cPD_group_code) {
		CPD_group_code = cPD_group_code;
	}
	public Date getCgr_initial_datetime() {
		return cgr_initial_datetime;
	}
	public void setCgr_initial_datetime(Date cgr_initial_datetime) {
		this.cgr_initial_datetime = cgr_initial_datetime;
	}
	public Date getCgr_expiry_datetime() {
		return cgr_expiry_datetime;
	}
	public void setCgr_expiry_datetime(Date cgr_expiry_datetime) {
		this.cgr_expiry_datetime = cgr_expiry_datetime;
	}
	public Long getCr_id() {
		return cr_id;
	}
	public void setCr_id(Long cr_id) {
		this.cr_id = cr_id;
	}
	public Long getCr_usr_ent_id() {
		return cr_usr_ent_id;
	}
	public void setCr_usr_ent_id(Long cr_usr_ent_id) {
		this.cr_usr_ent_id = cr_usr_ent_id;
	}
	
	public String getSrcFile() {
		return srcFile;
	}
	public void setSrcFile(String srcFile) {
		this.srcFile = srcFile;
	}
	public String getUpload_desc() {
		return upload_desc;
	}
	public void setUpload_desc(String upload_desc) {
		this.upload_desc = upload_desc;
	}
	public Long getCr_ct_id() {
		return cr_ct_id;
	}
	public void setCr_ct_id(Long cr_ct_id) {
		this.cr_ct_id = cr_ct_id;
	}
	public Date getCr_reg_datetime() {
		return cr_reg_datetime;
	}
	public void setCr_reg_datetime(Date cr_reg_datetime) {
		this.cr_reg_datetime = cr_reg_datetime;
	}
	public Date getCr_de_reg_datetime() {
		return cr_de_reg_datetime;
	}
	public void setCr_de_reg_datetime(Date cr_de_reg_datetime) {
		this.cr_de_reg_datetime = cr_de_reg_datetime;
	}
	public Long getCr_create_usr_ent_id() {
		return cr_create_usr_ent_id;
	}
	public void setCr_create_usr_ent_id(Long cr_create_usr_ent_id) {
		this.cr_create_usr_ent_id = cr_create_usr_ent_id;
	}
	public Date getCr_create_datetime() {
		return cr_create_datetime;
	}
	public void setCr_create_datetime(Date cr_create_datetime) {
		this.cr_create_datetime = cr_create_datetime;
	}
	public Long getCr_update_usr_ent_id() {
		return cr_update_usr_ent_id;
	}
	public void setCr_update_usr_ent_id(Long cr_update_usr_ent_id) {
		this.cr_update_usr_ent_id = cr_update_usr_ent_id;
	}
	public Date getCr_update_datetime() {
		return cr_update_datetime;
	}
	public void setCr_update_datetime(Date cr_update_datetime) {
		this.cr_update_datetime = cr_update_datetime;
	}
	public String getCr_status() {
		return cr_status;
	}
	public void setCr_status(String cr_status) {
		this.cr_status = cr_status;
	}
	public String getCr_reg_number() {
		return cr_reg_number;
	}
	public void setCr_reg_number(String cr_reg_number) {
		this.cr_reg_number = cr_reg_number;
	}
    public RegUser getUser() {
        return user;
    }
    public void setUser(RegUser user) {
        this.user = user;
    }
    public CpdType getCpdType() {
        return cpdType;
    }
    public void setCpdType(CpdType cpdType) {
        this.cpdType = cpdType;
    }
	public List getExportUserIds() {
		return exportUserIds;
	}
	public void setExportUserIds(List exportUserIds) {
		this.exportUserIds = exportUserIds;
	}
	public List getExportGroupIds() {
		return exportGroupIds;
	}
	public void setExportGroupIds(List exportGroupIds) {
		this.exportGroupIds = exportGroupIds;
	}
	public List getExportCpdTypeIds() {
		return exportCpdTypeIds;
	}
	public void setExportCpdTypeIds(List exportCpdTypeIds) {
		this.exportCpdTypeIds = exportCpdTypeIds;
	}
	public List<CpdRegistration> getCpdRegistrationList() {
		return cpdRegistrationList;
	}
	public void setCpdRegistrationList(List<CpdRegistration> cpdRegistrationList) {
		this.cpdRegistrationList = cpdRegistrationList;
	}
	public List<CpdGroupRegistration> getCpdGroupRegistrationList() {
		return cpdGroupRegistrationList;
	}
	public void setCpdGroupRegistrationList(
			List<CpdGroupRegistration> cpdGroupRegistrationList) {
		this.cpdGroupRegistrationList = cpdGroupRegistrationList;
	}
	
}
