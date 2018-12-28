package com.cwn.wizbank.cpdt.persistence;

import java.util.Map;

import com.cwn.wizbank.cpdt.entity.AeItemCpdtGourpItem;
import com.cwn.wizbank.cpdt.vo.AeItemCpdtGourpItemVo;
import com.cwn.wizbank.persistence.BaseMapper;

public interface AeItemCpdtGourpItemMapper extends BaseMapper<AeItemCpdtGourpItem> {
	
	AeItemCpdtGourpItemVo getAeItemCpdtGourpItemVo(Map<String,Object> params);

}
