package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import com.cwn.wizbank.entity.ObjectActionLog;

public interface ObjectActionLogMapper extends BaseMapper<ObjectActionLog>{

	public void saveLog(ObjectActionLog log);
	
	public List<Map> getObjectActionLog(Map map);

}
