/**
 * 
 */
package com.cwn.wizbank.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.entity.AcFunction;
import com.cwn.wizbank.entity.AeApplication;
import com.cwn.wizbank.entity.AeItem;
import com.cwn.wizbank.entity.ItemTargetLrnDetail;
import com.cwn.wizbank.utils.Page;

/**
 * @author leon.li
 * 2014-8-8 下午3:58:41
 */
@Service
public class HomeService {
	
	@Autowired
	RegUserService regUserService;
	
	@Autowired
	AeItemService aeItemService;
	
	@Autowired
	AeApplicationService aeApplicationService;
	
	@Autowired
	AcRoleFunctionService acRoleFunctionService;
	
	
	@Autowired
	ItemTargetLrnDetailService itemTargetLrnDetailService;
	private final int pSize=3;

	public void getOpenList(Page<AeItem> page, long tcrId, long userEntId) {
		aeItemService.getOpens(page, tcrId, userEntId);
	}

	public void getSignupList(Page<AeApplication> page, long tcrId, long usr_ent_id, String lanLabel) {
		page.setPageSize(6);
		aeApplicationService.signup(usr_ent_id, lanLabel, page);
		
		if(page.getResults().size() < page.getPageSize()){			
		   if(removeObj(page)){		
			   int len =pSize-page.getResults().size();
			   for(int i = 0; i < len; i++) {
				  AeApplication obj = new AeApplication();
				  page.getResults().add(obj);
			  }
			}
		}
		page.setTotalRecord(page.getResults().size());
	}

	public void getRecommendList(Page<ItemTargetLrnDetail> page, long tcrId, long usr_ent_id,
			String cur_lan) {
		page.setPageSize(6);
		//page.getParams().put("tcrId", tcrId);
		itemTargetLrnDetailService.recommend(usr_ent_id, cur_lan, page);
		if(page.getResults().size() < page.getPageSize() && page.getResults().size() != 0){
			if(removeObj(page)){	
			       int len = pSize - page.getResults().size();
			       for(int i = 0; i < len; i++) {
				      ItemTargetLrnDetail obj = new ItemTargetLrnDetail();
				      page.getResults().add(obj);
			       }
			 }
		}
		page.setTotalRecord(page.getResults().size());
	}

	public void getHotList(Page<AeItem> page, long tcrId, long userEntId,
			String cur_lan) {
		page.setPageSize(6);
		//page.getParams().put("tcrId", tcrId);
		aeItemService.getHotCourse(page, tcrId, userEntId);
		if(page.getResults().size() < page.getPageSize() && page.getResults().size() != 0){	
			if(removeObj(page)){	
			      int len = page.getPageSize() - page.getResults().size();
			      for(int i = 0; i < len; i++) {
				      AeItem obj = new AeItem();
				      page.getResults().add(obj);
			       }
			 }      
		}
		page.setTotalRecord(page.getResults().size());
	}
	
	public void getNewList(Page<AeItem> page, long tcrId, long userEntId,
			String cur_lan) {
		//page.setPageSize(6);
		page.getParams().put("tcrId", tcrId);
		aeItemService.getCatalogCourse(userEntId, cur_lan, page);
		
		page.setTotalRecord(page.getResults().size());
	}
	
	//删除page中多余的对象
	private  boolean  removeObj(Page<?> page){
		boolean flag = true;
		if(page.getResults().size() == pSize){
			flag = false;
		} else if(page.getResults().size() > pSize){
			flag = false;
		    for(int i = pSize; i < page.getResults().size();){
			   page.getResults().remove(i);
		    }
		}		
		return flag;
	}
	
	
	/**
	 * 获取管理员端的菜单
	 * @param usr_ent_id
	 * @param current_role
	 */
	public List<AcFunction> getHomeMenus(String current_role) {
		return acRoleFunctionService.getFunctions(current_role);
	}
	
	/**
	 * 获取管理员端的菜单
	 * @param usr_ent_id
	 * @param current_role
	 */
	public List<AcFunction> getMenusMarkFavorite(loginProfile prof) {
		return acRoleFunctionService.getMenusMarkFavorite(prof);
	}

	public List<AcFunction> roleFavoriteFunctionJson(String current_role,long role_id) {
		return acRoleFunctionService.roleFavoriteFunctionJson(current_role,role_id);
	}

	public List<AcFunction> roleHasFavoriteFunction(long role_id) {
		// TODO Auto-generated method stub
		return  acRoleFunctionService.roleHasFavoriteFunction(role_id);
	}
}
