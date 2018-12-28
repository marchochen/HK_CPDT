package com.cwn.wizbank.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.UserPositionLrnMap;
import com.cwn.wizbank.persistence.UserPositionLrnMapMapper;
import com.cwn.wizbank.utils.Page;

/**
 * 岗位学习地图相关service 实现
 * 
 */
@Service
public class UserPositionLrnMapService extends BaseService<UserPositionLrnMap> {

	@Autowired
	UserPositionLrnMapMapper userPositionLrnMapMapper;
    @Autowired
    AcRoleService acRoleService;
	public void add(UserPositionLrnMap userPositionLrnMap) {
		userPositionLrnMap.setUpm_create_time(getDate());
		userPositionLrnMap.setUpm_update_time(getDate());
		userPositionLrnMapMapper.add(userPositionLrnMap);
	}

	/**
	 *  前端查询关键岗位
	 * @param page
	 * @return
	 */
	public Page<UserPositionLrnMap> getPositionMapFrontList(Page<UserPositionLrnMap> page ) {
		List<UserPositionLrnMap> positionLrnMaps = userPositionLrnMapMapper.getPositionMapFrontList(page);
		page.setResults(positionLrnMaps);
		return page;
	}
	public Page<UserPositionLrnMap> getPositionMapList(Page<UserPositionLrnMap> page,long upm_id, long upt_id
			, long upc_id ) {
		page.getParams().put("upm_id", upm_id);
		page.getParams().put("upt_id", upt_id);
		page.getParams().put("upc_id", upc_id);
		List<UserPositionLrnMap> positionLrnMaps = userPositionLrnMapMapper.getPositionMapList(page);
		page.setResults(positionLrnMaps);
		return page;
	}
	public int getCountById(String id,String upm_status,boolean isTcIndependent,long top_tcr_id){
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("upm_upt_id", id);
		map.put("upm_status", upm_status);
		if(isTcIndependent){
            map.put("top_tcr_id", top_tcr_id);
        }  
		return userPositionLrnMapMapper.getCountById(map);
	}
	public int getCountByFrontId(String id,String upm_status,boolean isTcIndependent,long top_tcr_id){
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("upm_upt_id", id);
		map.put("upm_status", upm_status);
		if(isTcIndependent){
            map.put("top_tcr_id", top_tcr_id);
        }  
		return userPositionLrnMapMapper.getCountById(map);
	}
	public List<UserPositionLrnMap> getPositionFrontMap(String status,Long upc_id,boolean isTcIndependent,long top_tcr_id){
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("upm_status", status);
		map.put("upc_id", upc_id);
		if(isTcIndependent){
			map.put("top_tcr_id ", top_tcr_id);
		}
		return userPositionLrnMapMapper.getPostFrontMapList(map);
	}
	/**
	 * 根据字段名查询
	 * 字段名
	 * @param fieldName 
	 * 
	 * 参数
	 * @param param
	 * @return
	 */
	public UserPositionLrnMap getByFieldName(String fieldName,long id){
		Map<String,Object> map=new HashMap<String, Object>();
		map.put(fieldName, id);
		return userPositionLrnMapMapper.getByFieldName(map);
	}
	public List<UserPositionLrnMap> getPositionMapNotOneList(long upm_id,boolean isTcIndependent,long top_tcr_id) {
		Map<String,Object> map=new HashMap<String, Object>();
		if(isTcIndependent){
			map.put("top_tcr_id ", top_tcr_id);
		}
		map.put("upm_id", upm_id);
		return userPositionLrnMapMapper.getPositionMapNotOneList(map);
	}

	public void publishAndCancel(long upm_id, long ump_status) {
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("upm_status", ump_status);
		map.put("upm_id", upm_id);
		userPositionLrnMapMapper.updateStatus(map);
	}
	public void batPublishAndCancel(String ids,int ump_status){
		String [] upmIds= ids.split(",");
		for (String upmId : upmIds) {
			publishAndCancel(Long.parseLong(upmId),ump_status);
		}
	}
	public int getPositionMapNotOneListSize(long upm_id,boolean isTcIndependent,long top_tcr_id) {
		Map<String,Object> map=new HashMap<String, Object>();
		if(isTcIndependent){
			map.put("top_tcr_id ", top_tcr_id);
		}
		map.put("upm_id", upm_id);
		return userPositionLrnMapMapper.getPositionMapNotOneListSize(map);
	}
}