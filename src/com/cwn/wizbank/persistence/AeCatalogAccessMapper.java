package com.cwn.wizbank.persistence;

import com.cwn.wizbank.entity.AeCatalogAccess;


public interface AeCatalogAccessMapper extends BaseMapper<AeCatalogAccess>{

	public void delEnt(long entId);

}