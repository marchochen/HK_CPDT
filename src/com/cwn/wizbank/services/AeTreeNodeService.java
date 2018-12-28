package com.cwn.wizbank.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.util.LangLabel;
import com.cwn.wizbank.entity.AeCatalog;
import com.cwn.wizbank.entity.AeTreeNode;
import com.cwn.wizbank.entity.vo.AeTreeNodeVo;
import com.cwn.wizbank.entity.vo.TreeNodeVo;
import com.cwn.wizbank.persistence.AeTreeNodeMapper;
/**
 *  service 实现
 */
@Service
public class AeTreeNodeService extends BaseService<AeTreeNode> {

	@Autowired
	AeTreeNodeMapper aeTreeNodeMapper;
	@Autowired
	AcRoleService acRoleService;

	/**
	 * 获取用户中心的目录
	 * @param userTopTcrId
	 * @param selectedTcrId
	 * @param userEntId
	 * @param cos_type
	 * @param wizbini
	 * @return
	 */
	public List<AeTreeNodeVo> getTraingCenterCatalog(long userTopTcrId, long selectedTcrId, long userEntId, 
			String cos_type, WizbiniLoader wizbini) {
		Map<String,Object> map = new HashMap<String,Object>();
		
		Map<String,Object> params = new HashMap<String,Object>();
		
		map.put("tcr_id", userTopTcrId);
		map.put("selectedTcrId", selectedTcrId);
		map.put("cat_status", AeCatalog.CAT_STATUS_ON);
		map.put("tnd_type", AeTreeNode.TND_TYPE_ITEM);
		map.put("cos_type",cos_type);
		map.put("userEntId", userEntId);
		params.put("userEntId", userEntId);
		map.put("params", params);

		map.put("tc_independent", wizbini.cfgSysSetupadv.isTcIndependent());
		return aeTreeNodeMapper.getTraingCenterCatalog(map);
	}

	/**
	 * 获取子集目录
	 * @param tcrId
	 * @param tndId
	 * @param userEntId
	 * @param cos_type
	 * @return
	 */
	public List<AeTreeNode> getSubCatalog(long tcrId, long tndId, long userEntId, String cos_type){
		return this.getSubCatalog(tcrId, tndId, userEntId, cos_type, false);
	}

	/**
	 * 获取子集目录
	 * @param tcrId
	 * @param tndId
	 * @param userEntId
	 * @param cos_type
	 * @param onlySecond
	 * @return
	 */
	public List<AeTreeNode> getSubCatalog(long tcrId, long tndId, long userEntId, String cos_type, boolean onlySecond){
		Map<String,Object> map = new HashMap<String,Object>();
		Map<String,Object> params = new HashMap<String,Object>();
		map.put("tcrId", tcrId);
		map.put("catStatus", AeCatalog.CAT_STATUS_ON);
		map.put("tndType", AeTreeNode.TND_TYPE_ITEM);
		map.put("cos_type",cos_type);
		map.put("tndId", tndId);
		map.put("userEntId", userEntId);
		//只查询第二级目录
		map.put("onlySecond", onlySecond);
		params.put("userEntId", userEntId);
		map.put("params", params);
		return aeTreeNodeMapper.getSubCatalog(map);
	}
	
	/**
	 * 获取公开课的两级目录
	 * @param tcrId
	 * @return
	 */
	public Map<String,Object> getOpenCatalog(long tcrId, long tndId, long userEntId, String cos_type){
		Map<String,Object> map = new HashMap<String,Object>();
		List<AeTreeNode> first = getSubCatalog(tcrId, 0, userEntId, cos_type);
		if(tndId < 1 && first.size()> 0 && first.get(0)!= null && first.get(0).getTnd_id() > 0 ){
			//tndId = first.get(0).getTnd_id();
		}
		if(tndId > 0 ){
			List<AeTreeNode> second = getSubCatalog(tcrId, tndId, userEntId, cos_type);
			map.put("second", second);
		}else if(tndId == 0){
			//获取全部第一级别的目录时，获取全部的二级目录
			List<AeTreeNode> second = getSubCatalog(tcrId, 0, userEntId, cos_type, true);
			map.put("second", second);
		}
		map.put("first", first);
		return map;
	}
	
	/**
	 * 获取公开课的两级目录
	 * @param tcrId
	 * @return
	 */
	public Map<String,Object> getMobileCatalog(long tcrId, long tndId, long userEntId, String cos_type){
		Map<String,Object> map = new HashMap<String,Object>();
		List<AeTreeNode> first = getSubCatalog(tcrId, tndId, userEntId, cos_type);
		if(first != null){
			for(AeTreeNode atn : first){
				atn.setTnd_parent_tnd_id(tndId);
			}
		}
		if(tndId > 0 ){
			List<AeTreeNode> second = getSubCatalog(tcrId, tndId, userEntId, cos_type);
			if(second != null){
				for(AeTreeNode atn : second){
					atn.setTnd_parent_tnd_id(tndId);
				}
			}
			map.put("second", second);
		}
		map.put("first", first);
		return map;
	}
	
	
	
	/**
	 * 获取用户中心的目录
	 * @param tcrId
	 * @return
	 */
	public List<AeTreeNodeVo> getAdminTraingCenterCatalog(long tcrId, long userEntId, 
			String cosType, String rolExtId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("tcrId", tcrId);
		map.put("cat_status", AeCatalog.CAT_STATUS_ON);
		map.put("tnd_type", AeTreeNode.TND_TYPE_ITEM);
		map.put("userEntId", userEntId);
		map.put("rolExtId", rolExtId);
		map.put("cosType", cosType);
		boolean isRoleTcInd=AccessControlWZB.isRoleTcInd(rolExtId);
		map.put("isRoleTcInd", isRoleTcInd);
		
		return aeTreeNodeMapper.getAdminTraingCenterCatalog(map);
	}
	/**
	 *  职级树
	 * @param id
	 * @return
	 * 
	 */
	public List<TreeNodeVo> getGradeTree(long id,boolean isTcIndependent,long top_tcr_id){
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("ugr_ent_id", id);
		if(isTcIndependent){
            map.put("top_tcr_id", top_tcr_id);
        }  
		List<TreeNodeVo> treeNodeVos=aeTreeNodeMapper.getGradeTree(map);
		for (TreeNodeVo treeNodeVo : treeNodeVos) {
		int n=getGradeTreeHasChild(treeNodeVo.getId(),isTcIndependent,top_tcr_id);
		if(n>0){
			treeNodeVo.setIsParent(true);
		}
		}
		return treeNodeVos;
	}
	/**
	 * @param id
	 * @return 是否有子节点
	 */
	public int getGradeTreeHasChild(long id,boolean isTcIndependent,long top_tcr_id){
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("ugr_ent_id", id);
		if(isTcIndependent){
            map.put("top_tcr_id", top_tcr_id);
        }  
		return aeTreeNodeMapper.getGradeTreeHasChild(map);
	}	
	/**
	 * 获取课程的目录
	 * @param tcrId
	 * @return
	 */
	public List<AeTreeNodeVo> getAdminTraingCenterCourse(long tcrId, long userEntId, 
			String cosType, String rolExtId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("tcrId", tcrId);
		map.put("cat_status", AeCatalog.CAT_STATUS_ON);
		map.put("tnd_type", AeTreeNode.TND_TYPE_ITEM);
		map.put("userEntId", userEntId);
		map.put("rolExtId", rolExtId);
		map.put("cosType", cosType);
		boolean isRoleTcInd=AccessControlWZB.isRoleTcInd(rolExtId);
		map.put("isRoleTcInd", isRoleTcInd);
		return aeTreeNodeMapper.getAdminTraingCenterCourse(map);
	}
	
	public List<AeTreeNodeVo> getAdminTraingCenterCourse(long tcrId, long userEntId, 
			String cosType, String rolExtId , boolean isTopLevel,String cur_lan) {
		List<AeTreeNodeVo> voList = getAdminTraingCenterCourse( tcrId,  userEntId, cosType,  rolExtId);
		if(isTopLevel){
			List<AeTreeNodeVo> firstNodes = new ArrayList<AeTreeNodeVo>();
			AeTreeNodeVo vo = new AeTreeNodeVo();
			vo.setId(0L);
			vo.setpId(0L);
			vo.setHasChild(true);
			vo.setOpen(1);
			vo.setNocheck(true);
			vo.setName(LangLabel.getValue(cur_lan, "ALL"));
			firstNodes.add(vo);
			if(null!=voList){
				for(AeTreeNodeVo node : voList){
					node.setNocheck(false);//设置可以多选状态 (checkbox显示)
				}
			}
			firstNodes.addAll(voList);
			return firstNodes;
		}else{
			return voList;
		}
	}
	
}