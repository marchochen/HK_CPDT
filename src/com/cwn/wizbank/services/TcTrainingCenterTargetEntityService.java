package com.cwn.wizbank.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.TcTrainingCenterTargetEntity;
import com.cwn.wizbank.persistence.TcTrainingCenterTargetEntityMapper;
/**
 *  service 实现
 */
@Service
public class TcTrainingCenterTargetEntityService extends BaseService<TcTrainingCenterTargetEntity> {

	@Autowired
	TcTrainingCenterTargetEntityMapper tcTrainingCenterTargetEntityMapper;

	public void setTcTrainingCenterTargetEntityMapper(TcTrainingCenterTargetEntityMapper tcTrainingCenterTargetEntityMapper){
		this.tcTrainingCenterTargetEntityMapper = tcTrainingCenterTargetEntityMapper;
	}
	
	
	public void create(long tcrId, long usgEntId, String create_usr_id){
		TcTrainingCenterTargetEntity tce = new TcTrainingCenterTargetEntity();
		tce.setTce_tcr_id(tcrId);
		tce.setTce_ent_id(usgEntId);
		tce.setTce_create_timestamp(getDate());
		tce.setTce_create_usr_id(create_usr_id);
		add(tce);
	}
}