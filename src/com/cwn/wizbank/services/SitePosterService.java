package com.cwn.wizbank.services;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.SitePoster;
import com.cwn.wizbank.persistence.SitePosterMapper;
/**
 *  service 实现
 */
@Service
public class SitePosterService extends BaseService<SitePoster> {

	@Autowired
	SitePosterMapper sitePosterMapper;
	
	@Autowired
	EnterpriseInfoPortalService eipService; 
	
	@Autowired
	TcTrainingCenterService tcrService;

	public List<SitePoster> getPoster(long topTcrId, int isMobile) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("rootEntId", topTcrId);
		map.put("isMobile", isMobile);		
		List<SitePoster> list = sitePosterMapper.getPoster(map);
		return list;
	}
	
	
	public List<SitePoster> getLogo(long rootEntId,int isMobile){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("rootEntId", rootEntId);
		map.put("isMobile", isMobile);		
		return sitePosterMapper.getLogo(map);
	}
	
	public SitePoster searchPoster(long ste_id,int isMobile){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("ste_id", ste_id);
		map.put("isMobile", isMobile);		
		return sitePosterMapper.searchSitePoser(map);
	}
	
	/**
	 * 根据请求地址的host获取企业Logo
	 * @param host  
	 * @return
	 */
	public SitePoster getSitePoster(String host){
		Long tcr_id = eipService.getTcrByDomain(host);
		if(tcr_id == null || tcr_id == 0){
			tcr_id = tcrService.getRootTrainingCenter().getTcr_id();
		}
		SitePoster sp = searchPoster(tcr_id,0);
		if(sp == null){
			sp = searchPoster(tcrService.getRootTrainingCenter().getTcr_id(),0);
		}
		return sp;
	}
	
	/**
	 * 根据请求地址的host获取企业Logo
	 * @param host  
	 * @return
	 */
	public SitePoster getSitePoster(String host, int isMobile){
		Long tcr_id = eipService.getTcrByDomain(host);
		if(tcr_id == null || tcr_id == 0){
			tcr_id = tcrService.getRootTrainingCenter().getTcr_id();
		}
		SitePoster sp = searchPoster(tcr_id, isMobile);
		if(sp == null){
			sp = searchPoster(tcrService.getRootTrainingCenter().getTcr_id(), isMobile);
		}
		return sp;
	}
}