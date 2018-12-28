package com.cwn.wizbank.persistence;

import com.cwn.wizbank.entity.AeItemExtension;


public interface AeItemExtensionMapper extends BaseMapper<AeItemExtension>{

	void updateAccessCount(long itm_id);


}