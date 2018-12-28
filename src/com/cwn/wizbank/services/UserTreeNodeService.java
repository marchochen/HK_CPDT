package com.cwn.wizbank.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cw.wizbank.util.LangLabel;
import com.cwn.wizbank.entity.AeTreeNode;
import com.cwn.wizbank.entity.UserGroup;
import com.cwn.wizbank.entity.vo.TreeNodeVo;
import com.cwn.wizbank.persistence.UserGroupMapper;
import com.cwn.wizbank.persistence.UserTreeNodeMapper;
/**
 *  service 实现
 */
@Service
public class UserTreeNodeService extends BaseService<AeTreeNode> {

	@Autowired
	UserTreeNodeMapper userTreeNodeMapper;
	@Autowired
	UserGroupMapper userGroupMapper;
	
	/**
	 * 获取用户组(分层获取)
	 * @param tcrId
	 * @return
	 */
	
	public List<TreeNodeVo> getGroupTreeLevelJson(long groupId , long userId, 
			boolean isTcIndependent,long topTcrId ,String userRole , boolean isRoleTcid ,String langure,
			boolean showSubordinateInd){
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("user_ent_id", userId);
		map.put("showSubordinateInd", showSubordinateInd);
		if(isRoleTcid){
			map.put("isRoleTcInd", true);
			map.put("user_role", userRole);
		}
		map.put("isTcIndependent", isTcIndependent);
		List<TreeNodeVo> groupNodeVos = new ArrayList<TreeNodeVo>();
		if(-1L == groupId){ //获取最顶层用户组
			map.put("top_tcr_id", topTcrId);
			map.put("ern_type", "USG_PARENT_USG");
			List<TreeNodeVo> nodeVos =userTreeNodeMapper.getTopLevelGroupTree(map);//搜索出第一层用户组
			TreeNodeVo fakeRoot = new TreeNodeVo(); //作一个假的 所有用户组 为最高选项
			fakeRoot.setId(0L);
			fakeRoot.setIsParent(true);
			fakeRoot.setName( LangLabel.getValue(langure, "668"));
			fakeRoot.setOpen(true);
			fakeRoot.setpId(0L);
			fakeRoot.setNocheck(true);
			groupNodeVos.add(fakeRoot);
			Long rootId  = null;
			if(null!=nodeVos){
				for(TreeNodeVo node : nodeVos){
					if(TreeNodeVo.ROOT_ROLE.equalsIgnoreCase(node.getRole())){ //判断第一层用户组中是否有 所有用户组 一项
						rootId = node.getId();
					}
					node.setpId(0L); //把第一层用户组的父节点都搞到假的 所有用户组下
					node.setNocheck(false);//设置可以多选状态 (checkbox显示)
				}
			}
			if(null!=rootId){//如果第一层用户组中确实是有  "所有用户组" 的一项 那么查出他所能管理的下一级用户组
				if(isTcIndependent){
		            map.put("top_tcr_id", topTcrId);
		        }else{
		        	map.remove("top_tcr_id");
		        }
				map.put("usg_id", rootId);
				List<TreeNodeVo> nextLevelGroups =userTreeNodeMapper.getNextLevelGroupTree(map);
				if(null!=nextLevelGroups){
					for(TreeNodeVo node : nextLevelGroups){
						node.setpId(0L);
						node.setNocheck(false);
					}
				}
				groupNodeVos.addAll(nextLevelGroups);
			}else{ //如果第一层用户组中没有  "所有用户组" 的一项 直接加上第一层用户组
				groupNodeVos.addAll(nodeVos);
			}

		}else{
			if(isTcIndependent){
	            map.put("top_tcr_id", topTcrId);
	        }
			map.put("usg_id", groupId);
			groupNodeVos =userTreeNodeMapper.getNextLevelGroupTree(map);
		}
		for (TreeNodeVo treeNodeVo : groupNodeVos) {
			map.put("usg_id", treeNodeVo.getId());
			Integer n = userGroupMapper.groupHasChild(map);
			if(null!= n && n>0){
				treeNodeVo.setIsParent(true);
			}
		}
		//如果是要显示下属用户组 那么做多一层筛选
		/*
		if(showSubordinateInd){
			List<TreeNodeVo> subordinateTreeNode = new ArrayList<TreeNodeVo>();
			List<UserGroup> subordinateList = userGroupMapper.getSubordinateGroup(userId);
			for(TreeNodeVo group : groupNodeVos){
				for(UserGroup sg : subordinateList){
					if(sg.getUsg_ent_id() == group.getId()){
						subordinateTreeNode.add(group);
					}
				}
			}
			groupNodeVos = subordinateTreeNode;
		}
		*/
		return groupNodeVos;
	}
	
}