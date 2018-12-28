package com.cwn.wizbank.entity;

import java.sql.Timestamp;
import java.util.List;

/**
 * <p>Title:Profession</p>
 * <p>Description:职级 </p>
 * @author halo.pan
 *
 * @date 2016年4月5日 下午3:34:46
 *
 */
public class Profession implements java.io.Serializable {
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

		Long pfs_id;
		
		String pfs_title;
		
		Timestamp pfs_create_time;
		
		Long pfs_create_usr_id;
		
		Timestamp pfs_update_time;
		
		int pfs_template;
		
		Long pfs_tcr_id;
		
		Long pfs_update_usr_id;
		
		String[] ugr_title_lst;

		String[] ugr_id_lst;
		
		String[] itm_title_lst;
		
		int pfs_status;
		private RegUser user;
		
		private ProfessionItem psi;
		
		List<UserGrade> gradeList;
		
		String url;
		
		private String encrypt_pfs_id;
		
		public String[] getUgr_title_lst() {
			return ugr_title_lst;
		}

		public void setUgr_title_lst(String[] ugr_title_lst) {
			this.ugr_title_lst = ugr_title_lst;
		}

		public String[] getUgr_id_lst() {
			return ugr_id_lst;
		}

		public void setUgr_id_lst(String[] ugr_id_lst) {
			this.ugr_id_lst = ugr_id_lst;
		}

		public Long getPfs_id() {
			return pfs_id;
		}

		public void setPfs_id(Long pfs_id) {
			this.pfs_id = pfs_id;
		}

		public String getPfs_title() {
			return pfs_title;
		}

		public void setPfs_title(String pfs_title) {
			this.pfs_title = pfs_title;
		}

		public Timestamp getPfs_create_time() {
			return pfs_create_time;
		}

		public void setPfs_create_time(Timestamp pfs_create_time) {
			this.pfs_create_time = pfs_create_time;
		}

		public Long getPfs_create_usr_id() {
			return pfs_create_usr_id;
		}

		public void setPfs_create_usr_id(Long pfs_create_usr_id) {
			this.pfs_create_usr_id = pfs_create_usr_id;
		}

		public Timestamp getPfs_update_time() {
			return pfs_update_time;
		}

		public void setPfs_update_time(Timestamp pfs_update_time) {
			this.pfs_update_time = pfs_update_time;
		}

		public Long getPfs_tcr_id() {
			return pfs_tcr_id;
		}

		public void setPfs_tcr_id(Long pfs_tcr_id) {
			this.pfs_tcr_id = pfs_tcr_id;
		}

		public Long getPfs_update_usr_id() {
			return pfs_update_usr_id;
		}

		public void setPfs_update_usr_id(Long pfs_update_usr_id) {
			this.pfs_update_usr_id = pfs_update_usr_id;
		}

		public RegUser getUser() {
			return user;
		}

		public void setUser(RegUser user) {
			this.user = user;
		}

		public ProfessionItem getPsi() {
			return psi;
		}

		public void setPsi(ProfessionItem psi) {
			this.psi = psi;
		}

		public String[] getItm_title_lst() {
			return itm_title_lst;
		}

		public void setItm_title_lst(String[] itm_title_lst) {
			this.itm_title_lst = itm_title_lst;
		}

		public int getPfs_status() {
			return pfs_status;
		}

		public void setPfs_status(int pfs_status) {
			this.pfs_status = pfs_status;
		}


		public int getPfs_template() {
			return pfs_template;
		}

		public void setPfs_template(int pfs_template) {
			this.pfs_template = pfs_template;
		}

		public List<UserGrade> getGradeList() {
			return gradeList;
		}

		public void setGradeList(List<UserGrade> gradeList) {
			this.gradeList = gradeList;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getEncrypt_pfs_id() {
			return encrypt_pfs_id;
		}

		public void setEncrypt_pfs_id(String encrypt_pfs_id) {
			this.encrypt_pfs_id = encrypt_pfs_id;
		}

		
}