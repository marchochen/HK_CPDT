package com.cwn.wizbank.persistence;

import com.cwn.wizbank.entity.ResourcePermission;


public interface ResourcePermissionMapper extends BaseMapper<ResourcePermission>{

	void delAllByEntId(long usgCode);


}