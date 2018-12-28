package com.cwn.wizbank.persistence;

import java.util.List;

import com.cwn.wizbank.entity.SnsGroup;
import com.cwn.wizbank.utils.Page;


public interface SnsGroupMapper extends BaseMapper<SnsGroup>{

	List<SnsGroup> selectSnsGroupList(Page<SnsGroup> page);
	
	List<SnsGroup> selectAllSnsGroupList(Page<SnsGroup> page);
	
	SnsGroup selectSnsGroupDetail(SnsGroup snsGroup);

	List<SnsGroup> getByName(Page<SnsGroup> page);

	int checkGroupName(SnsGroup snsgroup);


}