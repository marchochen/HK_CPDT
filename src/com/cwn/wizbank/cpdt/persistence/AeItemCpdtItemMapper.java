package com.cwn.wizbank.cpdt.persistence;

import org.apache.ibatis.annotations.Param;

import com.cwn.wizbank.cpdt.entity.AeItemCpdtItem;
import com.cwn.wizbank.persistence.BaseMapper;

public interface AeItemCpdtItemMapper extends BaseMapper<AeItemCpdtItem> {
	
	AeItemCpdtItem getByItmId(@Param("itmId") long itmId);

}
