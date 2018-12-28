package com.cwn.wizbank.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.entity.ProfessionItem;
import com.cwn.wizbank.persistence.ProfessionItemMapper;
import com.cwn.wizbank.utils.Page;
/**
 * service 实现
 */
@Service
public class ProfessionItemService extends BaseService<ProfessionItem> {

	@Autowired
	ProfessionItemMapper professionItemMapper;
	public void add(ProfessionItem professionItem){
		professionItemMapper.add(professionItem);
	}
	public void deleteByPfsId(long id){
		professionItemMapper.deleteByPfsId(id);
	}
	public ProfessionItem getPsiByPfsid(long id){
		return professionItemMapper.getPsiByPfsid(id);
	}
	 public Page<ProfessionItem> get_psi_list(loginProfile prof, Model model, long pfs_ugr_id, long pfs_id, Page<ProfessionItem> page) {
			ProfessionItem psi = professionItemMapper.getPsiByPfsid(pfs_id);
			if(psi == null || psi.getPsi_itm() == null) {
				return null;
			}
			String[] tmp_itm_id_lst = psi.getPsi_itm().split("\\|");
			String[] tmp_ugr_id_lst = psi.getPsi_ugr_id().split("\\|");
			String itm_id_lst_str = "";
			for(int i = 0;i<tmp_ugr_id_lst.length;i++) {
				long tmp_ugr_id = Long.parseLong(tmp_ugr_id_lst[i]);
				if(tmp_ugr_id == pfs_ugr_id) {
					itm_id_lst_str = tmp_itm_id_lst[i];
					break;
				}
			}
			String[] itm_id_lst = itm_id_lst_str.split("~");
			List<Long> list_itm_ids = new ArrayList<Long>();
			for(int i = 0;i<itm_id_lst.length;i++) {
				list_itm_ids.add(Long.parseLong(itm_id_lst[i].trim()+""));
			}
			
			page.getParams().put("usr_ent_id", prof.usr_ent_id);
			page.getParams().put("itm_id_lst", list_itm_ids);
			page.setResults(professionItemMapper.pageItem(page));
			return page;
		}
	 
}