package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import com.cwn.wizbank.entity.vo.TreeNodeVo;

public interface UserTreeNodeMapper {
	
	public List<TreeNodeVo> getTopLevelGroupTree(Map map);
	public List<TreeNodeVo> getNextLevelGroupTree(Map map);
	public List<TreeNodeVo> getGroupTreeByUserId(Map map);
	
}
