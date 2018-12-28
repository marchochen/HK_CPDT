package com.cwn.wizbank.services;


import java.sql.Timestamp;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cw.wizbank.config.organization.usermanagement.UserManagement;
import com.cw.wizbank.util.cwEncode;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.entity.AcSite;
import com.cwn.wizbank.persistence.AcSiteMapper;
/**
 *  service 实现
 */
@Service
public class AcSiteService extends BaseService<AcSite> {

	@Autowired
	AcSiteMapper AcSiteMapper;

	public void setAcSiteMapper(AcSiteMapper AcSiteMapper){
		this.AcSiteMapper = AcSiteMapper;
	}
	
	
	public AcSite getSite(){
		 AcSite acSite = AcSiteMapper.selectAll().get(0);
		 Timestamp start_date = null;
	     Timestamp end_date = null;
		 
		 try {
             if (cwEncode.checkValidKey(acSite.getSte_eff_start_date()) && 
                 cwEncode.checkValidKey(acSite.getSte_eff_end_date())) {

                 start_date = Timestamp.valueOf(cwEncode.decodeKey(acSite.getSte_eff_start_date()));
                 end_date = Timestamp.valueOf(cwEncode.decodeKey(acSite.getSte_eff_end_date()));
             }                    
         }catch (Exception e) {
             
             // do nothing
         }
         
      // Expiry date decoded successfully
         if (start_date !=null && end_date != null) {
        	 
        	 Date curDate = getDate();
             // Not expired yet
             if (curDate.after(start_date) && curDate.before(end_date)) {
            	 
             }else{
            	 acSite = null;
             }
         }
         
		 return  acSite;
	}
}