package com.cw.wizbank.profession;

import javax.servlet.ServletRequest;

import com.cw.wizbank.ReqParam;
import com.cw.wizbank.util.cwException;

public class ProfessionReqParam extends ReqParam {

	Profession pfs;
	ProfessionItem psi;
	String psi_itm_id_lst;
	String psi_ugr_id_lst;
	
	public ProfessionReqParam() {
	}
	
	public ProfessionReqParam(ServletRequest inReq, String clientEnc_, String encoding_) throws cwException {
		this.req = inReq;
		this.clientEnc = clientEnc_;
		this.encoding = encoding_;
		common();
		return;
	}
	
	public void getPfsParam() throws cwException{
		pfs = new Profession();
		pfs.pfs_id = getLongParameter("pfs_id");
		pfs.pfs_title = getStringParameter("pfs_title");
		psi_itm_id_lst = getStringParameter("psi_itm_id_lst");
		psi_ugr_id_lst = getStringParameter("psi_ugr_id_lst");
	}
	
	public void getPsiParam() throws cwException {
		psi = new ProfessionItem();
		psi.psi_id = getLongParameter("psi_id");
		psi.psi_itm = getStringParameter("psi_itm");
		psi.psi_pfs_id = getLongParameter("psi_pfs_id");
		psi.psi_ugr_id = getStringParameter("psi_ugr_id");
	}


}