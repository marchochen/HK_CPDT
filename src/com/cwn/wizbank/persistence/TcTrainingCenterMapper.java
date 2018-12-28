package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.cw.wizbank.tree.TreeNode;
import com.cwn.wizbank.entity.TcTrainingCenter;

public interface TcTrainingCenterMapper extends BaseMapper<TcTrainingCenter> {

	/**
	 * 获取用户的培训中心
	 * @param usrEntId	用户ent id
	 * @return
	 */
	public List<TcTrainingCenter> getUserTrainingCenter(long usrEntId);

	List<TcTrainingCenter> selectTcrIdList(TcTrainingCenter tcTrainingCenter);

	public TcTrainingCenter getRootTrainingCenter();

	List<TcTrainingCenter> getMyTrainingCenter(Map<String, Object> map);

	// 获取培训中心树
	public List<TreeNode> getTrainingCenterTree(@Param("tcr_id") long tcr_id, @Param("ent_id") long ent_id);

	public List<TreeNode> getTrainingCenterTreeByOfficer(@Param("ent_id") long ent_id,@Param("current_role") String current_role);

	public List<TreeNode> getSubTrainingCenterTree(@Param("tcr_id") long tcr_id);
	
	
	public List<Long> getTrainingCenterIdByOfficer(Map<String, Object> map);

	public long getTrainingCenterGroupCount(long tcrId);
	
	public List<TcTrainingCenter> getTopTwoTrainingCenterBytcrId(long tcrId);

}