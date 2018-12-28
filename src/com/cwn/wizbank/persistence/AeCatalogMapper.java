package com.cwn.wizbank.persistence;

import java.util.List;

import com.cwn.wizbank.entity.AeCatalog;


public interface AeCatalogMapper extends BaseMapper<AeCatalog>{

	List<AeCatalog> getItemCatalogList(Long itemId);

}