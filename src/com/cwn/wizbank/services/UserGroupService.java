package com.cwn.wizbank.services;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.handler.UserRoleAuthorizationInterceptor;

import com.cwn.wizbank.entity.Entity;
import com.cwn.wizbank.entity.EntityRelation;
import com.cwn.wizbank.entity.UserGroup;
import com.cwn.wizbank.entity.vo.UserGroupVo;
import com.cwn.wizbank.exception.DataNotFoundException;
import com.cwn.wizbank.exception.MessageException;
import com.cwn.wizbank.persistence.AeCatalogAccessMapper;
import com.cwn.wizbank.persistence.EntityMapper;
import com.cwn.wizbank.persistence.EntityRelationMapper;
import com.cwn.wizbank.persistence.ResourcePermissionMapper;
import com.cwn.wizbank.persistence.UserGroupMapper;
import com.cwn.wizbank.utils.Params;

@Service
public class UserGroupService extends BaseService<UserGroup> {

	@Autowired
	UserGroupMapper userGroupMapper;

	@Autowired
	EntityMapper entityMapper;

	@Autowired
	EntityRelationMapper entityRelationMapper;

	@Autowired
	EntityRelationService entityRelationService;

	@Autowired
	AeCatalogAccessMapper aeCatalogAccessMapper;

	@Autowired
	ResourcePermissionMapper resourcePermissionMapper;

	@Autowired
	UsrRoleTargetEntityService usrRoleTargetEntityService;

	/**
	 * 获取用户组
	 * 
	 * @param params
	 * @return
	 */
	public List<UserGroupVo> getList(Params params) {
		List<UserGroupVo> list = userGroupMapper.selectList(params);

		return list;
	}

	public List<UserGroup> getListByTcrId(long tcrId) {
		return userGroupMapper.selectListByTcrId(tcrId);
	}

	public Long create(String usgName, long parentId, String create_usr_id) {
		Date date = getDate();
		Entity usg_entity = new Entity();
		usg_entity.setEnt_type("USG");
		usg_entity.setEnt_upd_date(date);
		usg_entity.setEnt_syn_ind(0L);
		entityMapper.insert(usg_entity);

		// 表中用户组编号直接修改为ent_id
		usg_entity.setEnt_ste_uid(usg_entity.getEnt_id() + "");
		entityMapper.update(usg_entity);

		long usg_id = usg_entity.getEnt_id();
		UserGroup usg = new UserGroup();
		usg.setUsg_ent_id(usg_id);
		usg.setUsg_display_bil(usgName);
		usg.setUsg_ent_id_root(1L);
		userGroupMapper.insert(usg);

		parentId = parentId > 0 ? parentId : 1;
		long order = 1;
		EntityRelation usgErt = new EntityRelation();
		usgErt.setErn_child_ent_id(parentId);
		usgErt.setErn_type("USG_PARENT_USG");

		List<EntityRelation> enr_lst = entityRelationMapper.getByChild(usgErt);
		if (enr_lst != null && enr_lst.size() > 0) {
			for (EntityRelation enr : enr_lst) {
				order++;
				enr.setErn_child_ent_id(usg_id);
				enr.setErn_remain_on_syn(0l);
				enr.setErn_create_timestamp(date);
				enr.setErn_create_usr_id(create_usr_id);
				enr.setErn_parent_ind(0l);
				entityRelationMapper.insert(enr);
			}
		}

		usgErt.setErn_child_ent_id(usg_id);
		usgErt.setErn_ancestor_ent_id(parentId);
		usgErt.setErn_parent_ind(1l);
		usgErt.setErn_order(order);
		usgErt.setErn_remain_on_syn(0l);
		usgErt.setErn_create_timestamp(date);
		usgErt.setErn_create_usr_id(create_usr_id);
		entityRelationMapper.insert(usgErt);
		return usg_id;
	}

	public UserGroup getByUsgCode(String usg_code) {
		return userGroupMapper.getByUsgCode(usg_code);
	}

	public void deleteUsg(String entUsgCode, String usrId, boolean checkChild)
			throws DataNotFoundException, MessageException {
		UserGroup usg = this.getByUsgCode(entUsgCode + "");
		
		if (usg != null) {
			long usgCode = usg.getUsg_ent_id();
			if (checkChild) {
				if (this.userGroupMapper.hasChild(usgCode)) {
					throw new MessageException("用户组存在子用户组，不能删除");
				}
			} else {
				this.entityRelationService.delAsAncestor(usrId, usgCode);
			}

			// del all catalog access record
			aeCatalogAccessMapper.delEnt(usgCode);

			// delete permission assigned to the user group
			resourcePermissionMapper.delAllByEntId(usgCode);

			// del records in EntityRelation that the group is as member
			// delEntityRelationAsChild(ent_delete_usr_id);
			entityRelationService.delAsChild(usrId, "USG_PARENT_USG", usgCode);

			// del record in user role target group, this group as target group
			/*
			 * DbRoleTargetEntity dbrte = new DbRoleTargetEntity();
			 * dbrte.rte_ent_id = usgCode; dbrte.del(con, false);
			 */
			usrRoleTargetEntityService.delete(usgCode);

			// del Entity

			// this.entityRelationMapper.delete(usg.getUsg_ent_id());
			this.delete(usgCode);
		} else {
			throw new DataNotFoundException();
		}
	}
	
	/**
	 * 获取培训中心下面的用户组数目
	 * @param tcrId
	 * @return
	 */
	public long getUserGroupCountInTCent(long tcrId){
		return this.userGroupMapper.getUserGroupCountInTCent(tcrId);
	}
	
	/**
	 * 获取用户能管理的用户组
	 * @param userId
	 * @param isTcIndependent
	 * @param topTcrId
	 * @param userRole
	 * @param isRoleTcid
	 * @return
	 */
	public List<UserGroup> getGroupByUserId(long userId, 
			boolean isTcIndependent,long topTcrId ,String userRole , boolean isRoleTcid){
		Map<String,Object> map=new HashMap<String, Object>();
		if(isRoleTcid){
			map.put("isRoleTcInd", true);
			map.put("user_ent_id", userId);
			map.put("user_role", userRole);
		}
		if(isTcIndependent){
            map.put("top_tcr_id", topTcrId);
        }
		return userGroupMapper.getGroupByUserId(map);
	}
	
	/**
	 * 获取用户能管理的用户组Id
	 * @param userId
	 * @param isTcIndependent
	 * @param topTcrId
	 * @param userRole
	 * @param isRoleTcid
	 * @return
	 */
	public String getGroupIdByUserId(long userId, 
			boolean isTcIndependent,long topTcrId ,String userRole , boolean isRoleTcid){
		
		List<UserGroup> groupList = getGroupByUserId( userId, isTcIndependent, topTcrId , userRole ,  isRoleTcid);
		if(null==groupList){
			return null;
		}else{
			StringBuffer sb = new StringBuffer();
			for(int i = 0; i < groupList.size();i++){
				sb.append(groupList.get(i).getUsg_ent_id().toString());
				if(i!=groupList.size()-1){
					sb.append(",");
				}
			}
			return sb.toString();
		}
		
	}
	
	/**
	 * 获取用户组层级结构如：父用户组 > 子用户组  > 后代用户组....（包括child_ent_id对应的群组名称）
	 * 注意：用户组不包括最根的用户组【All User Groups】
	 * @param child_ent_id
	 * @return
	 */
	public String getGroupLevelString(long child_ent_id){
		String result = "";
		List<String> groupArr = userGroupMapper.selectGroupLevelList(child_ent_id);
		
		if(groupArr == null || groupArr.size() == 0){
			return result;
		}
		
		String split = " > ";
		
		for(String group : groupArr){
			result += group + split;
		}
		
		return result.substring(0, result.length()-split.length());
		
	}
}
