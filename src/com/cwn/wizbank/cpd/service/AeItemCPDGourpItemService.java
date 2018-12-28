package com.cwn.wizbank.cpd.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.entity.AeItemCPDGourpItem;
import com.cwn.wizbank.persistence.AeItemCPDGourpItemMapper;
import com.cwn.wizbank.services.BaseService;

@Service
public class AeItemCPDGourpItemService  extends BaseService<AeItemCPDGourpItem> {
	
	@Autowired
	AeItemCPDGourpItemMapper  aeItemCPDGourpItemMapper;

	public AeItemCPDGourpItem getAeItemCPDGourpItemBy(Long id){
		Map map = new HashMap<String,Object>();
		map.put("id", id);
		List<AeItemCPDGourpItem> list = aeItemCPDGourpItemMapper.getAeItemCPDGourpItem(map);
		if(null!=list && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	 /**
     * 判断小牌牌照是否关联课程
     * @param cg_id
     * @return
     */
    public boolean getCountItemByCgID(long cg_id){
    	boolean flg =false;
		int count = aeItemCPDGourpItemMapper.getCountItemByCgID(cg_id);
		if(count > 0){
			flg = true;
		}
		return flg;
    }
	
    /**
     * 新增/修改 小牌与课程管理 设置所需时数
     * @param aeItemCPDGourpItem
     */
    public void saveOrUpdate(AeItemCPDGourpItem aeItemCPDGourpItem ,loginProfile prof){
    	if(null == aeItemCPDGourpItem.getAcgi_award_core_hours() || aeItemCPDGourpItem.getAcgi_award_core_hours().equals("")){
    		aeItemCPDGourpItem.setAcgi_award_core_hours((float) 0.0);
    	}
    	if(null == aeItemCPDGourpItem.getAcgi_award_non_core_hours() || aeItemCPDGourpItem.getAcgi_award_non_core_hours().equals("")){
    		aeItemCPDGourpItem.setAcgi_award_non_core_hours((float) 0.0);
    	}
    	if(null == aeItemCPDGourpItem.getAcgi_id() || aeItemCPDGourpItem.getAcgi_id().equals("")){
    		aeItemCPDGourpItem.setAcgi_create_datetime(super.getDate());
    		aeItemCPDGourpItem.setAcgi_create_usr_ent_id(prof.usr_ent_id);
    		if(aeItemCPDGourpItem.getAcgi_award_non_core_hours() != 0.0 || aeItemCPDGourpItem.getAcgi_award_core_hours() != 0.0){
    			super.add(aeItemCPDGourpItem);
    		}
    	}else{
    		aeItemCPDGourpItem.setAcgi_update_datetime(super.getDate());
    		aeItemCPDGourpItem.setAcgi_update_usr_ent_id(prof.usr_ent_id);
    		super.update(aeItemCPDGourpItem);
    	}
    	
    };
    
}
