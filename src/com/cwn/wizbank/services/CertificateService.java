package com.cwn.wizbank.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.Certificate;
import com.cwn.wizbank.persistence.CertificateMapper;
import com.cwn.wizbank.utils.Page;
/**
 * service 实现
 */
@Service
public class CertificateService extends BaseService<Certificate> {

	@Autowired
	CertificateMapper certificateMapper;


	public void setCertificateMapper(CertificateMapper certificateMapper) {
		this.certificateMapper = certificateMapper;
	}
	
	/**
	 * 获取证书列表
	 * @param usr_ent_id 用户id
	 * 
	 * @return
	 */
	public Page<Certificate> getCertificateList(Page<Certificate> page, long usr_ent_id) {
		page.getParams().put("usr_ent_id", usr_ent_id);
		certificateMapper.selectCertificateList(page);
		return page;
	}
	
}