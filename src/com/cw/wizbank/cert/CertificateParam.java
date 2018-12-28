package com.cw.wizbank.cert;

import com.cw.wizbank.JsonMod.BaseParam;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwUtils;

public class CertificateParam extends BaseParam {
	private long cert_id;
	private String cert_title;
	private long cert_tc_id = Long.MIN_VALUE;
	private String cert_img;
	private String mod_status_ind;
	private String cert_core;
	private String cert_end;
	private String cert_status_sear;


	private long itm_id;
	private long tkh_id;

	public long getItm_id() {
		return itm_id;
	}

	public void setItm_id(long itmId) {
		itm_id = itmId;
	}

	public long getTkh_id() {
		return tkh_id;
	}

	public void setTkh_id(long tkhId) {
		tkh_id = tkhId;
	}

	public String getMod_status_ind() {
		return mod_status_ind;
	}

	public void setMod_status_ind(String modStatusInd) {
		mod_status_ind = modStatusInd;
	}

	public long getCert_tc_id() {
		return cert_tc_id;
	}

	public void setCert_tc_id(long certTcId) {
		cert_tc_id = certTcId;
	}

	public String getCert_img() {
		return cert_img;
	}

	public void setCert_img(String certImg) {
		cert_img = certImg;
	}

	public String getCert_title() {
		return cert_title;
	}

	public void setCert_title(String certTitle) throws cwException {
		if("search_certificate_list".equals(super.getCmd())){
			certTitle=cwUtils.unicodeFrom(certTitle, clientEnc, encoding);
		}
		this.cert_title = certTitle;
	}

	public long getCert_id() {
		return cert_id;
	}

	public void setCert_id(long certId) {
		cert_id = certId;
	}
	public String getCert_core() {
		return cert_core;
	}

	public void setCert_core(String cert_core) throws cwException {
		if("search_certificate_list".equals(super.getCmd())){
			cert_core=cwUtils.unicodeFrom(cert_core, clientEnc, encoding);
		}
		this.cert_core = cert_core;
	}

	public String getCert_end() {
		return cert_end;
	}

	public void setCert_end(String cert_end) {
		this.cert_end = cert_end;
	}
	public String getCert_status_sear() {
		return cert_status_sear;
	}

	public void setCert_status_sear(String cert_status_sear) {
		this.cert_status_sear = cert_status_sear;
	}

}
