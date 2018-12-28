package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import com.cwn.wizbank.entity.UserPosition;
import com.cwn.wizbank.utils.Page;

public interface UserPositionMapper  extends BaseMapper<UserPosition>{
	
	
	public Long add(UserPosition userPosition);
	public List<UserPosition> getPositionList(Page<UserPosition> page);
	public List<UserPosition> getPositionMapPage(Page<UserPosition> page);
	public UserPosition getByMapId(long upm_id);
	public int batchdelete(Map<String,Object> map);
	public int getCountById(Map<String,Object> map);
	public List<UserPosition> getList(Map<String,Object> map);
	public boolean isExistFormProp(Map<String,Object> map);

}
