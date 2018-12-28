package com.cwn.wizbank.persistence;

import com.cwn.wizbank.entity.AeAppnApprovalList;


public interface AeAppnApprovalListMapper extends BaseMapper<AeAppnApprovalList>{

	int hasPendingApprovalAppn(long usr_ent_id);


}