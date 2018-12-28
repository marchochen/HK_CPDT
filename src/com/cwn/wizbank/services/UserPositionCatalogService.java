package com.cwn.wizbank.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.cwn.wizbank.entity.UserPositionCatalog;
import com.cwn.wizbank.persistence.UserPositionCatalogMapper;
import com.cwn.wizbank.utils.Page;

/**
 * 岗位目录service 实现
 */
@Service
public class UserPositionCatalogService extends BaseService<UserPositionCatalog> {

	@Autowired
	UserPositionCatalogMapper userPositionCatalogMapper;
	@Autowired
	AcRoleService acRoleService;
	public void add(UserPositionCatalog userPositionCatalog){
		userPositionCatalog.setUpc_create_datetime(getDate());
		userPositionCatalog.setUpc_update_datetime(getDate());
		userPositionCatalogMapper.add(userPositionCatalog);
	}
	public List<UserPositionCatalog> getPageList(Page<UserPositionCatalog> page,long upc_id){
		    page.getParams().put("upc_id", upc_id);
		return userPositionCatalogMapper.getPageList(page);
	}
	public List<UserPositionCatalog> getList(boolean isTcIndependent,long top_tcr_id){
		Map<String,Object> map=new HashMap<String, Object>();
		if(isTcIndependent){
            map.put("top_tcr_id", top_tcr_id);
        }  
		return userPositionCatalogMapper.list(map);
	}
	public int batchdelete(String ids){
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("ids", ids);
		return userPositionCatalogMapper.batchdelete(map);
	}
	public void updateCatalog(UserPositionCatalog userPositionCatalog,long usr_ent_id) {
		userPositionCatalog.setUpc_update_datetime(getDate());
		userPositionCatalog.setUpc_update_user_id(usr_ent_id);
		 userPositionCatalogMapper.update(userPositionCatalog);
	}
	public List<UserPositionCatalog> getPositionMapCatalogList(boolean isTcIndependent,long top_tcr_id){
		Map<String,Object> map=new HashMap<String, Object>();
		if(isTcIndependent){
		map.put("top_tcr_id", top_tcr_id);
		}
		return userPositionCatalogMapper.getListForPositionMap(map);
    }
	public boolean isExistTitle(String upc_title,long old_id,boolean isTcIndependent,long top_tcr_id){
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("upc_title", upc_title);
		map.put("old_id", old_id);
		if(isTcIndependent){
            map.put("top_tcr_id", top_tcr_id);
        }  
		return userPositionCatalogMapper.isExistTitle(map);
	}
}