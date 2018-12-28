package com.cwn.wizbank.persistence;

import java.util.List;

import com.cwn.wizbank.entity.Certificate;
import com.cwn.wizbank.utils.Page;


public interface CertificateMapper extends BaseMapper<Certificate>{

	List<Certificate> selectCertificateList(Page<Certificate> page);
	
}