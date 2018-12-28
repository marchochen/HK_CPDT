package com.cwn.wizbank.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.cw.wizbank.qdb.dbEntityRelation;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.entity.Profession;
import com.cwn.wizbank.entity.ProfessionLrnItem;
import com.cwn.wizbank.persistence.AeItemMapper;
import com.cwn.wizbank.persistence.ProfessionMapper;
import com.cwn.wizbank.utils.EncryptUtil;
import com.cwn.wizbank.utils.Page;
/**
 * service 实现
 */
@Service
public class ProfessionService extends BaseService<Profession> {

	@Autowired
	ProfessionMapper professionMapper;
	
	@Autowired
	AeItemMapper aeItemMapper;

	@Autowired
	ProfessionLrnItemService professionLrnItemService;
	
	public List<Profession> get_pfs_list(loginProfile prof){
    	List<Profession> pfs_list = professionMapper.getAllPfsList();
    	List<Profession> tmp_pfs_list = new ArrayList<Profession>();
    	if(pfs_list != null) {
	    	for(int i = 0;i<pfs_list.size();i++) {
	    		Profession pfs = pfs_list.get(i);
	    		if(pfs.getPsi() != null){
		    		String psi_ugr_id = pfs.getPsi().getPsi_ugr_id();
					String[] tmp_ugr_id_lst = psi_ugr_id.split("\\|");
					String[] tmp_ugr_title_lst = new String[tmp_ugr_id_lst.length];
					for(int j = 0;j<tmp_ugr_id_lst.length;j++) {
						tmp_ugr_title_lst[j] = professionMapper.getUgrTitleByUgrId(Long.parseLong(tmp_ugr_id_lst[j]));
					}
					pfs.setUgr_id_lst(tmp_ugr_id_lst);
					pfs.setUgr_title_lst(tmp_ugr_title_lst);
					tmp_pfs_list.add(pfs);
	    		}
	    	}
    	}
		return tmp_pfs_list;
    }
	
	public long getCurUserUgrId(long usr_ent_id) {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ern_child_ent_id", usr_ent_id);
		map.put("ern_type", dbEntityRelation.ERN_TYPE_USR_CURRENT_UGR);
		map.put("ern_parent_ind", true);
    	long cur_usr_ugr_id = professionMapper.getCurUserUgrId(map);
    	return cur_usr_ugr_id;
	}
	
	public String getPfsTitleById(long pfs_id) {

    	Profession pfs = professionMapper.get(pfs_id);
    	if(pfs == null) {
    		return null;
    	}
    	return pfs.getPfs_title();
	}
    
    public boolean get_ugr_list(ModelAndView mav, long pfs_id, loginProfile prof) throws Exception {
    	List<Profession> pfs_list = get_pfs_list(prof);
    	for(int i = 0;i<pfs_list.size();i++) {
    		Profession pfs = (Profession)pfs_list.get(i);
    		if(pfs.getPfs_id() == pfs_id) {
    			mav.addObject("ugr_id_lst", pfs.getUgr_id_lst());
    			mav.addObject("ugr_title_lst", pfs.getUgr_title_lst());
    			break;
//    			String title = LangLabel.getValue(prof.cur_lan, "PFS00") + " - " + pfs.getPfs_title();
//    			mav.addObject("pfs_title", title);
    		}
    	}
    	long cur_usr_ugr_id = getCurUserUgrId(prof.usr_ent_id);
    	mav.addObject("cur_usr_ugr_id", cur_usr_ugr_id);
    	String cur_pfs_title = getPfsTitleById(pfs_id);
    	mav.addObject("cur_pfs_title", cur_pfs_title);
    	return true;
    }
    
//    public boolean get_ugr_list(Model model) throws Exception {
//        Connection con = null;
//        try {
//        	con = getConnection();
//        	Vector pfs_list = ProfessionBean.getPfsList(con);
//        	for(int i = 0;i<pfs_list.size();i++) {
//        		ProfessionBean pfs = (ProfessionBean)pfs_list.get(i);
//        		
//        	}
//        	model.addAttribute("pfs_list", pfs_list);
//        	return true;
//        } finally {
//            releaseConnection(con);
//        }
//    }
  
    public Page<Profession> get_psi_page(loginProfile prof,Page<Profession> page) {
    	List<Profession> pfs_list=professionMapper.getAllPfsPage(page);
    	List<Profession> tmp_pfs_list = new ArrayList<Profession>();
    	if(pfs_list != null) {
	    	for(int i = 0;i<pfs_list.size();i++) {
	    		Profession pfs = pfs_list.get(i);
	    		if(pfs.getPsi() != null){
	    			String psi_itm=pfs.getPsi().getPsi_itm();
	    			String[] tmp_psi_itm=psi_itm.split("\\|");
	    			List<String> itmlist=new ArrayList<String>();
	    			for (int k = 0; k < tmp_psi_itm.length; k++) {
	    				String [] imt=tmp_psi_itm[k].split("\\~");
	    				for (int j = 0; j < imt.length; j++) {
	    				itmlist.add(aeItemMapper.get(Long.parseLong(imt[j].trim())).getItm_title());
	    					
	    				}
					}
	    			
		    		String psi_ugr_id = pfs.getPsi().getPsi_ugr_id();
					String[] tmp_ugr_id_lst = psi_ugr_id.split("\\|");
					String[] tmp_ugr_title_lst = new String[tmp_ugr_id_lst.length];
					for(int j = 0;j<tmp_ugr_id_lst.length;j++) {
						tmp_ugr_title_lst[j] = professionMapper.getUgrTitleByUgrId(Long.parseLong(tmp_ugr_id_lst[j]));
					}
					String[] tmp_psi_itm_lst = (String[]) itmlist.toArray(new String[itmlist.size()]);
					pfs.setUgr_id_lst(tmp_ugr_id_lst);
					pfs.setUgr_title_lst(tmp_ugr_title_lst);
					pfs.setItm_title_lst(tmp_psi_itm_lst);
					tmp_pfs_list.add(pfs);
	    		}
	    	}
    	}
    	page.setResults(tmp_pfs_list);
    	return page;
    	}
    public void add(Profession profession){
    	profession.setPfs_create_time(getDate());
    	profession.setPfs_update_time(getDate());
    	 professionMapper.add(profession);
 }
    public void update(Profession profession){
    	profession.setPfs_update_time(getDate());
    	professionMapper.update(profession);
    }
   public  Page<Profession> get_pfs_page(Page<Profession> page){
	   professionMapper.getAllProfessionPage(page);
	   return page;
   }
   public void deleteProfession(long pfs_id){
	   professionMapper.delete(pfs_id);
	   professionLrnItemService.deleteByPfsId(pfs_id);
   }
   public void publishAndCancel(long pfs_id,int status){
	   Map<String, Object> map = new HashMap<String, Object>();
		map.put("pfs_id", pfs_id);
		map.put("pfs_status", status);
	   professionMapper.updateStatus(map);
   }
   public List<Profession> getAll(boolean isTcIndependent,long top_tcr_id){
	   Map<String, Object> map = new HashMap<String, Object>();
	   if(isTcIndependent){
		   map.put("top_tcr_id", top_tcr_id);
	   }
	   return professionMapper.getAll(map);
   }
   public List<Profession> getAllFront(boolean isTcIndependent,long top_tcr_id){
	   List<Profession> list=getAll(isTcIndependent,top_tcr_id);
	   String url ="nodelist?id=";
	   for (Profession profession : list) {
		   profession.setUrl(url+EncryptUtil.cwnEncrypt(profession.getPfs_id()));
	}
	   return list;
   }
   public boolean isExistTitle(String pfs_title,String old_value,boolean isTcIndependent,long top_tcr_id){
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("pfs_title", pfs_title);
		map.put("old_value", old_value);
		if(isTcIndependent){
            map.put("top_tcr_id", top_tcr_id);
        }  
		return professionMapper.isExistFormProp(map);
	}
   public void batDeleteProfession(String ids) {
		String [] pfsIds=ids.split(",");
		for (String pfsId : pfsIds) {
			deleteProfession(Long.parseLong(pfsId));
		}
	}
   public void batPublishAndCancel(String ids,int pfs_status){
		String [] pfsIds= ids.split(",");
		for (String pfsId : pfsIds) {
			publishAndCancel(Long.parseLong(pfsId),pfs_status);
		}
	}
   public boolean getAffectedPfs(long id){
		return professionMapper.getAffectedPfs(id)>0 ? true:false;
	}
   
   public int getAllFrontCount(boolean isTcIndependent,long top_tcr_id){
	   List<Profession> list=getAll(isTcIndependent,top_tcr_id);
	   return list == null ? 0 : list.size();
   }
   
}