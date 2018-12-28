/**
 * 
 */
package com.cwn.wizbank.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.vo.SearchResultVo;
import com.cwn.wizbank.persistence.SearchMapper;
import com.cwn.wizbank.utils.ImageUtil;
import com.cwn.wizbank.utils.Page;

/**
 * @author leon.li
 *
 */
@Service
public class SearchService {
	
	@Autowired
	SearchMapper searchMapper;
	
	
	public List<SearchResultVo> search(Page<SearchResultVo> page, String type, String searchValue, long userEntId, long tcrId,boolean showMobileOnly){
		page.getParams().put("type", type);
		page.getParams().put("searchValue", searchValue);
		page.getParams().put("userEntId", userEntId);
		page.getParams().put("tcrId", tcrId);		
		page.getParams().put("showMobileOnly", showMobileOnly);		
		List<SearchResultVo> list = searchMapper.search(page);
		if(list != null && list.size() > 0) {
			for(SearchResultVo vo : list) {
				if(vo.getIntroduction() != null){
					vo.setIntroduction(vo.getIntroduction().replaceAll("\"\"", "\""));
				}
				ImageUtil.doImagePath(vo);
			}
		}
		
		return list;
	}
	
	public Map<String,Integer> searchResultMsg(String searchValue, long userEntId, long tcrId){
		   Map<String,Integer> reMap=new HashMap<String,Integer>();
		   String [] types=new String[]{"All","Course"/*,"Exam"*/,"Open","Message","Article"/*,"Group"*/,/*"Answer",*//*"Contacts"*//*"Knowledge"*/};
		   for(int i=0;i<types.length;i++){
			    Page<SearchResultVo> page = new Page<SearchResultVo>();
			    page.getParams().put("type", types[i]);
				page.getParams().put("searchValue", searchValue);
				page.getParams().put("userEntId", userEntId);
				page.getParams().put("tcrId", tcrId);
				searchMapper.search(page);
				reMap.put(types[i], page.getTotalRecord());
		   }
		   return reMap;
	}
	
	/**
	 * 按类型分组
	 * @param searchValue
	 * @param userEntId
	 * @param tcrId
	 * @return
	 */
	public Map<String, Page<?>> searchGroupResultMsg(String searchValue, long userEntId, long tcrId, long isMobile,boolean showMobileOnly){
		   Map<String, Page<?>> reMap = new HashMap<String, Page<?>>();
		   String [] types = new String[]{"All","Course"/*,"Exam"*/,"Open","Message","Article",/*"Group","Answer",/*"Contacts","Knowledge"*/};
		   for(int i=0; i<types.length; i++){
			    Page<SearchResultVo> page = new Page<SearchResultVo>();
			    page.setPageSize(1);
			    page.getParams().put("type", types[i]);
				page.getParams().put("searchValue", searchValue);
				page.getParams().put("userEntId", userEntId);
				page.getParams().put("tcrId", tcrId);
				page.getParams().put("isMobile", isMobile);
				page.getParams().put("showMobileOnly", showMobileOnly);
				searchMapper.search(page);
				for(SearchResultVo obj : page.getResults()){
					ImageUtil.doImagePath(obj);
				}
				reMap.put(types[i], page);
		   }
		   return reMap;
	}
}
