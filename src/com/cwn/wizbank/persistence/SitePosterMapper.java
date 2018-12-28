package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import com.cwn.wizbank.entity.SitePoster;


public interface SitePosterMapper extends BaseMapper<SitePoster>{

	List<SitePoster> getPoster(Map<String,Object> map);
	List<SitePoster> getLogo(Map<String,Object> map);
	SitePoster searchSitePoser(Map<String,Object> map);
}