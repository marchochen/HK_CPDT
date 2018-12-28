package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import com.cwn.wizbank.entity.ItemTargetLrnDetail;
import com.cwn.wizbank.utils.Page;


public interface ItemTargetLrnDetailMapper extends BaseMapper<ItemTargetLrnDetail>{

	public List<ItemTargetLrnDetail> getRecommend(Page<ItemTargetLrnDetail> page);

	public ItemTargetLrnDetail getByUserItemId(Map<String,Object> map);

}