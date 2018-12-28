package com.cwn.wizbank.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.tree.TreeNode;
import com.cwn.wizbank.entity.AcRole;
import com.cwn.wizbank.entity.EntityRelation;
import com.cwn.wizbank.entity.TcRelation;
import com.cwn.wizbank.entity.TcTrainingCenter;
import com.cwn.wizbank.entity.TcTrainingcenterofficer;
import com.cwn.wizbank.persistence.TcRelationMapper;
import com.cwn.wizbank.persistence.TcTrainingCenterMapper;
import com.cwn.wizbank.persistence.TcTrainingcenterofficerMapper;
import com.cwn.wizbank.utils.LabelContent;

/**
 * service 实现
 */
@Service
public class TcTrainingCenterService extends BaseService<TcTrainingCenter> {

	@Autowired
	TcTrainingCenterMapper tcTrainingCenterMapper;
	
	@Autowired
	TcTrainingcenterofficerMapper tcTrainingcenterofficerMapper;
	
	@Autowired
	TcRelationMapper tcRelationMapper;

	public void setTcTrainingCenterMapper(TcTrainingCenterMapper tcTrainingCenterMapper) {
		this.tcTrainingCenterMapper = tcTrainingCenterMapper;
	}

	public List<TcTrainingCenter> getTcrIdList(long usr_ent_id, long tcr_id) {
		TcTrainingCenter tcTrainingCenter = new TcTrainingCenter();
		tcTrainingCenter.setTcr_id(tcr_id);
		EntityRelation entityRelation = new EntityRelation();
		entityRelation.setErn_child_ent_id(usr_ent_id);
		tcTrainingCenter.setEntityRelation(entityRelation);
		return tcTrainingCenterMapper.selectTcrIdList(tcTrainingCenter);
	}

	public TcTrainingCenter getRootTrainingCenter() {
		return tcTrainingCenterMapper.getRootTrainingCenter();
	}

	public TcTrainingCenter getTopTrainingCenter(WizbiniLoader wizbini, Long userEntId) {
		TcTrainingCenter topTc = null;
		if (wizbini.cfgSysSetupadv.isTcIndependent()) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userEntId", userEntId);
			map.put("isTcIndependent", wizbini.cfgSysSetupadv.isTcIndependent());

			List<TcTrainingCenter> tcList = tcTrainingCenterMapper.getMyTrainingCenter(map);
			if (tcList != null && tcList.size() > 0) {
				topTc = tcList.get(0);
			} else {
				topTc = this.getRootTrainingCenter();
			}
		} else {
			topTc = this.getRootTrainingCenter();
		}
		return topTc;
	}

	public long getTopTcrId(WizbiniLoader wizbini, Long userEntId) {
		TcTrainingCenter topTc = getTopTrainingCenter(wizbini, userEntId);
		if (topTc != null) {
			return topTc.getTcr_id();
		}
		return 0;
	}

	/*
	 * 获取培训中心树
	 */
	public List<TreeNode> getTrainingCenterTree(Long tcr_id, long ent_id, String head,loginProfile prof) {
		List<TreeNode> list = new ArrayList<TreeNode>();
		if (tcr_id == null || tcr_id == 0) {
			
			String current_role = prof.current_role;
			//不跟培训中心关联的 ，获取全部
			if(!AccessControlWZB.isRoleTcInd(current_role) || current_role.equals(AcRole.ROLE_ADM_1)){
				current_role = "AMD_1";
				ent_id = prof.my_top_tc_id;
			}
			
			List<TreeNode> nodeList = tcTrainingCenterMapper.getTrainingCenterTreeByOfficer(ent_id,current_role);
			
			if (head.equals("withHead") && tcr_id == null) {
				
				TreeNode treeNode = new TreeNode();
				treeNode.setName(LabelContent.get(prof.cur_lan, "label_tm.label_core_training_management_301"));
				
				long id = getPidFromChildList(nodeList);
				
				treeNode.setId(id);
				treeNode.setOpen(true);
				treeNode.setParent(true);
				treeNode.setpId(-1);
				list.add(treeNode);
			}
			
			list.addAll(nodeList);

		} else {
			list = tcTrainingCenterMapper.getSubTrainingCenterTree(tcr_id);
		}
		return list;
	}
	

	private long getPidFromChildList(List<TreeNode> nodeList) {
		if(nodeList==null || nodeList.size() == 0){
			return 0;
		}
		TreeNode node = nodeList.get(0);
		return node.getpId();
	}

	public TcTrainingCenterMapper getTcTrainingCenterMapper() {
		return tcTrainingCenterMapper;
	}

	public List<Long> getTrainingCenterIdByOfficer(long userEntId,
			String rolExtId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userEntId", userEntId);
		map.put("rolExtId", rolExtId);
		return tcTrainingCenterMapper.getTrainingCenterIdByOfficer(map);
	}
	
	
	public boolean isTcOfficer(long tcr_id, long userEntId,String rolExtId) {
		boolean result = false;
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userEntId", userEntId);
		map.put("rolExtId", rolExtId);
		List<Long> tclist = tcTrainingCenterMapper.getTrainingCenterIdByOfficer(map);
		if(tclist != null && tclist.size() > 0){
			for(long temp_tcr_id : tclist){
				if(tcr_id == temp_tcr_id){
					result = true;
					break;
				}
			}
		}
		return result;
		
	}

	/**
	 * 创建培训中心
	 * @param tcrCode
	 * @param tcrName
	 * @param parentTcrId
	 * @param userEntId
	 * @return
	 */
	public long create(String tcrCode, String tcrName, long parentTcrId, long userEntId, String create_usr_id) {
		TcTrainingCenter tc = new TcTrainingCenter();
		Date date = getDate();
		tc.setTcr_code(tcrCode);
		tc.setTcr_title(tcrName);
		tc.setTcr_ste_ent_id(1l);
		tc.setTcr_status("OK");
		tc.setTcr_create_timestamp(date);
		tc.setTcr_update_timestamp(date);
		tc.setTcr_parent_tcr_id(parentTcrId);
		tc.setTcr_create_usr_id(create_usr_id);
		tc.setTcr_user_mgt_ind(1l);
		tc.setTcr_update_usr_id(create_usr_id);
		this.add(tc);
		
		
		TcTrainingcenterofficer tco = new TcTrainingcenterofficer();
		tco.setTco_create_timestamp(date);
		tco.setTco_create_usr_id(create_usr_id);
		tco.setTco_major_ind(1);
		tco.setTco_rol_ext_id("TADM_1");
		tco.setTco_tcr_id(tc.getTcr_id());
		tco.setTco_update_timestamp(date);
		tco.setTco_update_usr_id(create_usr_id);
		tco.setTco_usr_ent_id(userEntId);
		tcTrainingcenterofficerMapper.insert(tco);
		
		
		long order = 1;
		TcRelation tcr = new TcRelation();
		
		tcr.setTcn_ancestor(parentTcrId);
		tcr.setTcn_child_tcr_id(tc.getTcr_id());
		tcr.setTcn_create_timestamp(date);
		tcr.setTcn_create_usr_id(create_usr_id);
		
		List<TcRelation> tcn_lst = tcRelationMapper.getListByChildId(parentTcrId);
		if(tcn_lst != null && tcn_lst.size() > 0){
			for(TcRelation tcn :tcn_lst ){
				order ++;
				tcn.setTcn_child_tcr_id(tc.getTcr_id());
				tcn.setTcn_create_timestamp(date);
				tcn.setTcn_create_usr_id(create_usr_id);
				tcRelationMapper.insert(tcn);
			}
		}
		tcr.setTcn_order(order);
		tcRelationMapper.insert(tcr);
		
		return tc.getTcr_id();
	}
	
	/**
	 * 学员获取我的培训中心
	 * @param wizbini
	 * @param userEntId
	 * @return
	 */
	public List<TcTrainingCenter> getMyTrainingCenter(WizbiniLoader wizbini, Long userEntId){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userEntId", userEntId);
		map.put("isTcIndependent", wizbini.cfgSysSetupadv.isTcIndependent());
		List<TcTrainingCenter> list = this.tcTrainingCenterMapper.getMyTrainingCenter(map);
		return list;
	};
	
	public List<TcTrainingCenter> getMyTrainingCenter(Long userEntId ,boolean isTcIndependent,
			long topTcrId ,String userRole , boolean isRoleTcid){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userEntId", userEntId);
		map.put("isTcIndependent", isTcIndependent);
		List<TcTrainingCenter> list = this.tcTrainingCenterMapper.getMyTrainingCenter(map);
		return list;
	};
	
	/**
	 * ln模式获取培训中心的最高培训中心id
	 * @return
	 */
	public long getTopTwoTrainingCenterBytcrId(long tcrId){
		List<TcTrainingCenter> list = this.tcTrainingCenterMapper.getTopTwoTrainingCenterBytcrId(tcrId);
		if(list != null && list.size() > 0){
			TcTrainingCenter ttc = list.get(0);
			if(ttc != null && ttc.getTcr_id() != null && ttc.getTcr_id() > 0){
				return ttc.getTcr_id();
			}
		}
		return tcrId;
	};
	
}