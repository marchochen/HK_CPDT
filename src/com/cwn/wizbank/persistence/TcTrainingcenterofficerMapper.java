package com.cwn.wizbank.persistence;

import java.util.Map;

import com.cwn.wizbank.entity.TcTrainingCenter;
import com.cwn.wizbank.entity.TcTrainingcenterofficer;

public interface TcTrainingcenterofficerMapper extends BaseMapper<TcTrainingcenterofficer> {

	void delOfficerRoleFromTc(Map<String, Object> map);

}
