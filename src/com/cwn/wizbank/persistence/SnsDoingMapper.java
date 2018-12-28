package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import com.cwn.wizbank.entity.SnsDoing;
import com.cwn.wizbank.utils.Page;

public interface SnsDoingMapper extends BaseMapper<SnsDoing>{
	
	/**
	 * 	查询所有动态
	 * @param page
	 * @return
	 */
	public List<SnsDoing> selectList(Page<SnsDoing> page);
	
	public List<SnsDoing> selectPersonalDoingList(Page<SnsDoing> page);

	public List<SnsDoing> listUserAll(Page<SnsDoing> page);
	
	public List<SnsDoing> selectIndexList(Page<SnsDoing> page);

	public List<SnsDoing> listGroupDoing(Page<SnsDoing> page);
	
	public void deleteDoingByModule(Map<String, Object> map);
	
	public List<SnsDoing> getAllGroupDoingList(Map<String, Object> map);
	
}
