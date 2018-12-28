package com.cwn.wizbank.services;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.EntityRelation;
import com.cwn.wizbank.entity.EntityRelationHistory;
import com.cwn.wizbank.persistence.EntityRelationMapper;

/**
 * service 实现
 */
@Service
public class EntityRelationService extends BaseService<EntityRelation> {

	@Autowired
	EntityRelationMapper entityRelationMapper;
	@Autowired
	EntityRelationHistoryService entityRelationHistoryService;

	public void setEntityRelationMapper(
			EntityRelationMapper entityRelationMapper) {
		this.entityRelationMapper = entityRelationMapper;
	}

	public void delAsAncestor(String usrId, long ern_ancestor_ent_id) {
		String userRelation = "USR_PARENT_USG";
		String childRelation = "USG_PARENT_USG";
		/*
		 * Vector ancestors = new Vector();
		 * ancestors.addAll(getChilds(userRelation, ern_ancestor_ent_id));
		 * ancestors.addAll(getChilds(childRelation, ern_ancestor_ent_id));
		 * moveToHistory(con, usr_id, ancestors, null);
		 */
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user_relation", userRelation);
		map.put("child_relation", childRelation);
		map.put("ern_ancestor_ent_id", ern_ancestor_ent_id);
		this.entityRelationMapper.delAncestor(map);

	}

	/**
	 * get all Childs
	 * 
	 * @param con
	 * @param ern_type
	 * @param ancestor_ent_id
	 * @return
	 * @throws SQLException
	 */
	public List<EntityRelation> getChilds(String ern_type, long ancestor_ent_id)
			throws SQLException {
		// this.ern_child_ent_id = 0;
		List<EntityRelation> vc = getEntityRelations(ern_type, ancestor_ent_id,
				0);
		return vc;
	}

	public void delAsChild(String usr_id, String ern_type, long ern_child_ent_id) {
		List<EntityRelation> ancestors = getAncestors(ern_type,
				ern_child_ent_id);
		// 删除已有的历史记录
		// if(ancestors != null && ancestors.size()>0){
		// dbEntityRelationHistory.delAll(con, ern_child_ent_id, ern_type);
		// }
		// moveToHistory(con, usr_id, ancestors, endTime);
		this.delAsChild(ern_type, ern_child_ent_id);

	}

	public void delAsChild(String ern_type, Long ent_id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ern_type", ern_type);
		map.put("ent_id", ent_id);
		this.entityRelationMapper.delAsChild(map);
	}

	/**
	 * get all Ancestors
	 * 
	 * @param con
	 * @param ern_type
	 * @param child_ent_id
	 * @return
	 * @throws SQLException
	 */
	public List<EntityRelation> getAncestors(String ern_type, long child_ent_id) {
		List<EntityRelation> vc = getEntityRelations(ern_type, 0, child_ent_id);
		return vc;
	}

	/**
	 * 查询EntityRelation记录
	 * 
	 * @param con
	 * @param ern_type
	 * @param ancestor_ent_id
	 *            为0则查询父记录
	 * @param child_ent_id
	 *            为0则查询子记录
	 * @return
	 * @throws SQLException
	 */
	private List<EntityRelation> getEntityRelations(String ern_type,
			long ancestor_ent_id, long child_ent_id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ern_type", ern_type);
		map.put("ancestor_ent_id", ancestor_ent_id);
		map.put("child_ent_id", child_ent_id);
		return this.entityRelationMapper.getEntityRelations(map);
	}

	public void delAllEntityRelation(String usr_id, long usr_ent_id) {
		Vector<EntityRelation> allAncestors = new Vector<EntityRelation>();
		Vector<String> ancetor_type = new Vector<String>();

		List<EntityRelation> list = this.entityRelationMapper
				.getList(usr_ent_id);
		if (list != null && !list.isEmpty()) {
			for (EntityRelation er : list) {
				allAncestors.addElement(er);
				if (!ancetor_type.contains(er.getErn_type())) {
					ancetor_type.add(er.getErn_type());
				}
			}
		}
		// 删除已有的历史记录
		if (ancetor_type != null && ancetor_type.size() > 0) {
			for (int i = 0; i < ancetor_type.size(); i++) {
				entityRelationHistoryService.deleteAll(usr_ent_id,
						(String) ancetor_type.get(i));
			}
		}
		moveToHistory(usr_id, allAncestors);

		this.entityRelationMapper.delByChildEntId(usr_ent_id);
	}

	/**
	 * 把EntityRelation中的记录备份到EntityRelationHistory中
	 * 
	 * @param con
	 * @param usr_Id
	 * @param EntityRelationVc
	 *            从EntityRelation中删除的记录
	 * @param updTime
	 * @throws SQLException
	 */
	private void moveToHistory(String usr_Id,
			Vector<EntityRelation> EntityRelationVc) {
		Date endTime = getDate();
		if (EntityRelationVc != null && EntityRelationVc.size() > 0) {
			for (EntityRelation ern : EntityRelationVc) {
				ern.setErn_create_timestamp(endTime);
				ern.setErn_create_usr_id(usr_Id);
				ern.setErn_end_timestamp(endTime);
				ern.setErn_start_timestamp(ern.getErn_create_timestamp());
				entityRelationHistoryService.getEntityRelationHistoryMapper()
						.insertHistory(ern);
			}
		}
	}

	public int getCount(long usr_ent_id, String ern_type, int parent_ind) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("usr_ent_id", usr_ent_id);
		map.put("ern_type", ern_type);
		map.put("parent_ind", parent_ind);
		return this.entityRelationMapper.getCount(map);
	}
}