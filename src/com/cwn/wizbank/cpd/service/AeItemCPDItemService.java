package com.cwn.wizbank.cpd.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cwn.wizbank.entity.AeItemCPDGourpItem;
import com.cwn.wizbank.entity.AeItemCPDItem;
import com.cwn.wizbank.entity.CpdGroup;
import com.cwn.wizbank.entity.CpdLrnAwardRecord;
import com.cwn.wizbank.entity.ObjectActionLog;
import com.cwn.wizbank.persistence.AeItemCPDItemMapper;
import com.cwn.wizbank.services.BaseService;
import com.cwn.wizbank.systemLog.SystemLogContext;
import com.cwn.wizbank.systemLog.SystemLogTypeEnum;
import com.cwn.wizbank.utils.CommonLog;
import com.cwn.wizbank.utils.DateUtil;

@Service
public class AeItemCPDItemService extends BaseService<AeItemCPDItem>{

	@Autowired
	AeItemCPDItemMapper aeItemCPDItemMapper;
	
	@Autowired
	AeItemCPDGourpItemService aeItemCPDGourpItemService;
	@Autowired
	CpdGroupService cpdGroupService;
	@Autowired
	CpdLrnAwardRecordService cpdLrnAwardRecordService;
	
    public static AeItemCPDItem getByItmIdForOld(Connection con, long itm_id)
		throws SQLException, qdbException ,cwSysMessage {
    	AeItemCPDItem itm = null;
        StringBuffer SQLBuf = new StringBuffer(200);
        SQLBuf.append(" Select * From aeItemCPDItem ");
        SQLBuf.append(" Where aci_itm_id = ? ");
        String SQL = new String(SQLBuf);

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, itm_id);
        ResultSet rs = stmt.executeQuery();
        try{
            if(rs.next()){
            	itm = new AeItemCPDItem();
            	itm.setAci_id(rs.getLong("aci_id"));
            	itm.setAci_itm_id(rs.getLong("aci_itm_id"));
            	itm.setAci_accreditation_code(rs.getString("aci_accreditation_code"));
            	itm.setAci_hours_end_date(rs.getDate("aci_hours_end_date"));
            	itm.setAci_create_usr_ent_id(rs.getLong("aci_create_usr_ent_id"));
            	itm.setAci_create_datetime(rs.getDate("aci_create_datetime"));
            	itm.setAci_update_usr_ent_id(rs.getLong("aci_update_usr_ent_id"));
            	itm.setAci_update_datetime(rs.getDate("aci_update_datetime"));
            }
        }catch(Exception e){
        	CommonLog.error(e.getMessage(),e);
        }finally {
			cwSQL.cleanUp(rs, stmt);
		}

        return itm;
    }

    /**
     * 判断大牌牌照是否关联课程
     * @param ct_id
     * @return
     */
    public boolean getCountItemByCtID(long ct_id){
    	boolean flg =false;
		//获取该大牌下所有生效小牌
    	List<CpdGroup> list = cpdGroupService.getAllOrder(ct_id);
			for(int i =0; i<list.size(); i++){
				if(cpdGroupService.getCountItemByCgID(list.get(i).getCg_id()) == true){
					flg = true;
				}
			}
		return flg;
    }

    /**
     * 课程id查找对应设置的CPT/D时数
     * @param itm_id
     * @return
     * @throws SQLException
     * @throws qdbException
     * @throws cwSysMessage
     */
    public AeItemCPDItem getByItmId(long itm_id)throws SQLException, qdbException ,cwSysMessage {
        	AeItemCPDItem itm = aeItemCPDItemMapper.getByItmId(itm_id);
        	if(itm == null){
        		itm = new AeItemCPDItem(); 
        	}
        	//获取当前生效的所有小牌牌照
    		List<CpdGroup> list = cpdGroupService.getAllOrder(0);
    		AeItemCPDGourpItem[] aelist = new AeItemCPDGourpItem[list.size()];
    		for(int i=0;i<list.size();i++){
    			AeItemCPDGourpItem ae = new AeItemCPDGourpItem();
    			if(null != itm.getAeCPDGourpItemList() && itm.getAeCPDGourpItemList().length > 0){
    				for(int j =0;j<itm.getAeCPDGourpItemList().length; j++){
    					//.longValue() 比较两个Long对象值是否相等
    					if(itm.getAeCPDGourpItemList()[j].getAcgi_cg_id().longValue() == list.get(i).getCg_id().longValue() ){
    						ae.setCpdGroup(list.get(i));
    		    			ae.setAcgi_id(itm.getAeCPDGourpItemList()[j].getAcgi_id());
    		    			ae.setAcgi_cg_id(list.get(i).getCg_id());
    		    			ae.setAcgi_award_core_hours(itm.getAeCPDGourpItemList()[j].getAcgi_award_core_hours());
    		    			ae.setAcgi_award_non_core_hours(itm.getAeCPDGourpItemList()[j].getAcgi_award_non_core_hours());
    		    			aelist[i]=ae;
    		    			break;
    					}
    					if(j == itm.getAeCPDGourpItemList().length-1){
    						ae.setCpdGroup(list.get(i));
    		    			ae.setAcgi_cg_id(list.get(i).getCg_id());
    		    			ae.setAcgi_award_core_hours((float) 0.00);
    		    			ae.setAcgi_award_non_core_hours((float) 0.00);
    		    			aelist[i]=ae;
    					}
    				}
    			}else{
    				ae.setCpdGroup(list.get(i));
	    			ae.setAcgi_cg_id(list.get(i).getCg_id());
	    			ae.setAcgi_award_core_hours((float) 0.00);
	    			ae.setAcgi_award_non_core_hours((float) 0.00);
	    			aelist[i]=ae;
    			}
    		}
    		itm.setAeCPDGourpItemList(aelist);
            return itm;
        }

    /**
     * 新增/修改 课程所设置的CPT/D时数
     * @param aeItemCPDItem
     * @param prof
     */
    public Map<String, Object> saveOrUpdate(AeItemCPDItem aeItemCPDItem ,loginProfile prof){
    	Map<String, Object> map = new HashMap<String, Object>();
    	//查找是否存在父级课程已经设置了cpd时数  并存在code
    	Map<String, Object> child_map = new HashMap<String, Object>();
    	child_map.put("ire_child_itm_id", aeItemCPDItem.getAci_itm_id());
    	List<AeItemCPDItem> list = aeItemCPDItemMapper.getAeItemCPDItem(child_map);
    	if(null != list && list.size() > 0){
    		aeItemCPDItem.setAci_accreditation_code(list.get(0).getAci_accreditation_code());
    	}
    	
        if (aeItemCPDItem.getAci_id() == null || aeItemCPDItem.getAci_id().equals("")) {
        	aeItemCPDItem.setAci_create_datetime(super.getDate());
        	aeItemCPDItem.setAci_create_usr_ent_id(prof.usr_ent_id);
        	aeItemCPDItemMapper.insertReturnID(aeItemCPDItem);
        	if(aeItemCPDItem.getAeCPDGourpItemList() != null && aeItemCPDItem.getAeCPDGourpItemList().length > 0){
        		for(int i = 0; i< aeItemCPDItem.getAeCPDGourpItemList().length ;i++){
            		aeItemCPDItem.getAeCPDGourpItemList()[i].setAcgi_aci_id(aeItemCPDItem.getAci_id());
            		aeItemCPDGourpItemService.saveOrUpdate(aeItemCPDItem.getAeCPDGourpItemList()[i], prof);
            	}
        	}
        	map.put("success", true);
        	
        	//新增课程 如果课程中有班级设置了cpd时数  需要修改班级的“认证编号”
			aeItemCPDItemMapper.updChildCodeByItmId(aeItemCPDItem);
        	
        	//重要功能操作日志  新增课程设置牌照所需时数
    		ObjectActionLog log = new ObjectActionLog();
    		log.setObjectId(aeItemCPDItem.getAci_id());
    		log.setObjectCode(aeItemCPDItem.getItm_code());
    		log.setObjectAction(ObjectActionLog.OBJECT_ACTION_ADD);
    		log.setObjectActionType(ObjectActionLog.OBJECT_ACTION_TYPE_WEB);
    		log.setObjectOptUserId(prof.getUsr_ent_id());
    		log.setObjectActionTime(DateUtil.getCurrentTime());
    		log.setObjectTitle(aeItemCPDItem.getItm_name());
    		log.setObjectType(ObjectActionLog.OBJECT_TYPE_CPD_COURSE_HOURS);
    		log.setObjectOptUserLoginTime(prof.login_date);
    		log.setObjectOptUserLoginIp(prof.ip);
    		SystemLogContext.saveLog(log, SystemLogTypeEnum.OBJECT_ACTION_LOG);
        	
		} else {
			//屏蔽该逻辑验证
			//判断是否能修改(已经报名的学员获得的cpd时数是否大于当前设置的cpd时数)
			/*if(aeItemCPDItem.getAeCPDGourpItemList() != null && aeItemCPDItem.getAeCPDGourpItemList().length > 0){
				for(int i = 0; i< aeItemCPDItem.getAeCPDGourpItemList().length  ;i++){
					 //当前最大核心时数
					 List<CpdLrnAwardRecord> MaxCPDCoreHours = cpdLrnAwardRecordService.getMaxCPDCoreHours(aeItemCPDItem.getAci_itm_id(), aeItemCPDItem.getAeCPDGourpItemList()[i].getAcgi_cg_id());
		        	 //当前最大非核心时数
					 List<CpdLrnAwardRecord> MaxCPDNonCoreHours = cpdLrnAwardRecordService.getMaxCPDNonCoreHours(aeItemCPDItem.getAci_itm_id(), aeItemCPDItem.getAeCPDGourpItemList()[i].getAcgi_cg_id());
		        	 boolean flg = false;
		        	 String name = "";
		        	 if(MaxCPDCoreHours.size() > 0 && null != MaxCPDCoreHours.get(0) && null != MaxCPDCoreHours.get(0).getClar_award_core_hours()){
		    			 if(null != aeItemCPDItem.getAeCPDGourpItemList()[i].getAcgi_award_core_hours()){
		            		 if(aeItemCPDItem.getAeCPDGourpItemList()[i].getAcgi_award_core_hours() < MaxCPDCoreHours.get(0).getClar_award_core_hours()){
		            			flg = true;
		            			name =  MaxCPDCoreHours.get(0).getUsr_display_bil();
		        			 }
		            	 }else if(MaxCPDCoreHours.get(0).getClar_award_core_hours() > 0){
		            		 flg = true;
		            		 name =  MaxCPDCoreHours.get(0).getUsr_display_bil();
		            	 }
		        	 }
		        	 if(MaxCPDNonCoreHours.size() > 0 && null != MaxCPDNonCoreHours.get(0) && null != MaxCPDNonCoreHours.get(0).getClar_award_non_core_hours()){
			        	 if(null != aeItemCPDItem.getAeCPDGourpItemList()[i].getAcgi_award_non_core_hours()){
			        		 if(aeItemCPDItem.getAeCPDGourpItemList()[i].getAcgi_award_non_core_hours() < MaxCPDNonCoreHours.get(0).getClar_award_non_core_hours()){
			        			flg = true;
			        			name =  MaxCPDNonCoreHours.get(0).getUsr_display_bil();
			    			 }
			        	 }else if(MaxCPDNonCoreHours.get(0).getClar_award_non_core_hours() > 0){
			        		 flg = true;
			        		 name =  MaxCPDNonCoreHours.get(0).getUsr_display_bil();
			        	 }
		        	 }	 
		        	 if(flg){ //检测到修改时数冲突（已有学员获得的时数大于当前设置的时数）
		        		 map.put("cg_alias", aeItemCPDItem.getAeCPDGourpItemList()[i].getCpdGroup().getCg_alias());
		       			 map.put("cg_code", aeItemCPDItem.getAeCPDGourpItemList()[i].getCpdGroup().getCg_code());
		       			 map.put("usr_name", name);
		           		 map.put("success", false);
		           		 return map;
		        	 }
				}
			}*/
			
			
			aeItemCPDItem.setAci_update_datetime(super.getDate());
			aeItemCPDItem.setAci_update_usr_ent_id(prof.usr_ent_id);
			super.update(aeItemCPDItem);
			if(aeItemCPDItem.getAeCPDGourpItemList() != null && aeItemCPDItem.getAeCPDGourpItemList().length > 0){
				for(int i = 0; i< aeItemCPDItem.getAeCPDGourpItemList().length  ;i++){
					//修改时  也要重新把课程关联牌照的id赋值给每一个小牌 （在初始设置之后新增的小牌 修改所需时数时会执行新增操作）
					aeItemCPDItem.getAeCPDGourpItemList()[i].setAcgi_aci_id(aeItemCPDItem.getAci_id());
	        		aeItemCPDGourpItemService.saveOrUpdate(aeItemCPDItem.getAeCPDGourpItemList()[i], prof);
	        	}
			}
			
			//修改课程 如果课程中有班级设置了cpd时数  需要修改班级的“认证编号”
			aeItemCPDItemMapper.updChildCodeByItmId(aeItemCPDItem);
			
			map.put("success", true);
			
			//重要功能操作日志  修改课程设置牌照所需时数
    		ObjectActionLog log = new ObjectActionLog();
    		log.setObjectId(aeItemCPDItem.getAci_id());
    		log.setObjectCode(aeItemCPDItem.getItm_code());
    		log.setObjectAction(ObjectActionLog.OBJECT_ACTION_UPD);
    		log.setObjectActionType(ObjectActionLog.OBJECT_ACTION_TYPE_WEB);
    		log.setObjectOptUserId(prof.getUsr_ent_id());
    		log.setObjectActionTime(DateUtil.getCurrentTime());
    		log.setObjectTitle(aeItemCPDItem.getItm_name());
    		log.setObjectType(ObjectActionLog.OBJECT_TYPE_CPD_COURSE_HOURS);
    		log.setObjectOptUserLoginTime(prof.login_date);
    		log.setObjectOptUserLoginIp(prof.ip);
    		SystemLogContext.saveLog(log, SystemLogTypeEnum.OBJECT_ACTION_LOG);
		}
        
        return map;
    }

    /**
     * 获取课程相关的CPD关联信息 以MAP返回
     * @param itm_id
     * @return
     * @throws SQLException
     * @throws qdbException
     * @throws cwSysMessage
     */
	public Map getCPDGourpItemMap(long itm_id ) throws SQLException, qdbException, cwSysMessage{
		AeItemCPDItem aeItemCPDItem = getByItmId(itm_id);
		Map awardCPDGourpItemMap = new HashMap<Long,AeItemCPDGourpItem>();
		if(null!=aeItemCPDItem && null!=aeItemCPDItem.getAeCPDGourpItemList() 
				&& aeItemCPDItem.getAeCPDGourpItemList().length>0){
			for(int i = 0 ; i<aeItemCPDItem.getAeCPDGourpItemList().length ; i++){
				AeItemCPDGourpItem val = aeItemCPDItem.getAeCPDGourpItemList()[i];
					awardCPDGourpItemMap.put(val.getAcgi_cg_id(), val);
			}
		}
		return awardCPDGourpItemMap;
	}


}
