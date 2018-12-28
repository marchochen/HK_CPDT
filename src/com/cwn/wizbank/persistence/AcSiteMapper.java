package com.cwn.wizbank.persistence;

import java.util.List;

import com.cwn.wizbank.entity.AcSite;


public interface AcSiteMapper extends BaseMapper<AcSite>{

	public List<AcSite> selectAll();
}