package com.cwn.wizbank.persistence;

import com.cwn.wizbank.entity.AeAppnTargetEntity;


public interface AeAppnTargetEntityMapper extends BaseMapper<AeAppnTargetEntity>{

	void delByUsrEntId(long usr_ent_id);


}