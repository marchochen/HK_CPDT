package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import com.cwn.wizbank.entity.AeTreeNode;
import com.cwn.wizbank.entity.vo.AeTreeNodeVo;
import com.cwn.wizbank.entity.vo.TreeNodeVo;


public interface AeTreeNodeMapper extends BaseMapper<AeTreeNode>{

	public List<AeTreeNodeVo> getTraingCenterCatalog(Map<String,Object> map);
	
	/**
	 * 获取子集列表
	 * 如果map.list is null or size < 1 就是拿顶级目录
	 * @param map
	 * @return
	 */
	public List<AeTreeNode> getSubCatalog(Map<String,Object> map);
	
	/**
	 * 获取课程的目录
	 * @param itmId
	 * @return
	 */
	public List<AeTreeNode> getItemCatalog(Long itmId);
	
	
	
	public List<AeTreeNodeVo> getAdminTraingCenterCatalog(Map<String,Object> map);
	
	public List<TreeNodeVo> getGradeTree(Map<String,Object> map);
	public int getGradeTreeHasChild(Map<String,Object> map);
	public List<AeTreeNodeVo> getAdminTraingCenterCourse(Map<String,Object> map);
}