package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import com.cwn.wizbank.entity.UserPositionLrnItem;

public interface UserPositionLrnItemMapper  extends BaseMapper<UserPositionLrnItem>{
	
	
	public Long add(UserPositionLrnItem userPositionLrnItem);
	public List<UserPositionLrnItem> getItemByMapList(Map<String,Object> map);
	public void deleteByUpmId(long upm_id);
}
