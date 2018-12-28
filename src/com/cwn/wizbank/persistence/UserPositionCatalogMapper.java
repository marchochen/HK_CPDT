package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import com.cwn.wizbank.entity.UserPositionCatalog;
import com.cwn.wizbank.utils.Page;

public interface UserPositionCatalogMapper  extends BaseMapper<UserPositionCatalog>{
	
	
	public Long add(UserPositionCatalog userPositionCatalog);
	public List<UserPositionCatalog> getPageList(Page<UserPositionCatalog> page);
	public int batchdelete(Map<String,Object> map);
	public void update(UserPositionCatalog userPositionCatalog);
	public List<UserPositionCatalog> getListForPositionMap(Map<String,Object> map);
	public boolean isExistTitle(Map<String,Object> map);
}
