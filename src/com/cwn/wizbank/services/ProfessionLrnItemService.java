package com.cwn.wizbank.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.ProfessionLrnItem;
import com.cwn.wizbank.entity.UserGrade;
import com.cwn.wizbank.entity.vo.GradeNodesVo;
import com.cwn.wizbank.persistence.ProfessionLrnItemMapper;
import com.cwn.wizbank.utils.EncryptUtil;
import com.cwn.wizbank.utils.StringUtils;
/**
 * service 实现
 */
@Service
public class ProfessionLrnItemService extends BaseService<ProfessionLrnItem> {

	@Autowired
	ProfessionLrnItemMapper professionLrnItemMapper;
	@Autowired
	UserGradeService gradeService;
	public void add(ProfessionLrnItem professionLrnItem){
		professionLrnItemMapper.add(professionLrnItem);
	}
	public void deleteByPfsId(long id){
		professionLrnItemMapper.deleteByPfsId(id);
	}
	public List<ProfessionLrnItem> getPsiByPfsid(long id){
		return professionLrnItemMapper.getPsiByPfsid(id);
	}
	public List<UserGrade> getItemByGradeId(long pfs_id){
		List<Long> ugr_ids=getGradeIds(pfs_id);
		List<UserGrade> grades=new ArrayList<UserGrade>();
		for (Long ugr_id : ugr_ids) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("psi_pfs_id", pfs_id);
			params.put("psi_ugr_id", ugr_id);
			UserGrade grade=gradeService.get(ugr_id);
			if(grade!=null){
			List<ProfessionLrnItem> items=professionLrnItemMapper.getItemByGradeId(params);
			grade.setItems(items);
		    List<String> itemValues=new ArrayList<String>();
		    		
			for (ProfessionLrnItem professionLrnItem : items) {
				itemValues.add(professionLrnItem.getPsi_itm_id());
			}
			grade.setItemValue(StringUtils.listToString(itemValues, "~"));
			grades.add(grade);
		}
			}
		
		return grades;
	}
	public List<Long> getGradeIds(long pfs_id){
		return professionLrnItemMapper.getGradeIdsByPfsId(pfs_id);
	}
	public List<GradeNodesVo> getItemByfFrontPfsId(long id){
		List<GradeNodesVo> nodes=professionLrnItemMapper.getItemFrontByPfsId(id);
		removeDuplicate(nodes);
		String url ="/app/learningmap/grade/courselist?pfs_id="+EncryptUtil.cwnEncrypt(id)+"&id=";
		for (GradeNodesVo nodesVo : nodes) {
			nodesVo.setUrl(url+EncryptUtil.cwnEncrypt(nodesVo.getUgr_ent_id()));
		}
		return nodes;
	}
	public  void removeDuplicate(List<GradeNodesVo> list) { 
		for ( int i = 0 ; i < list.size() - 1 ; i ++ ) { 
		for ( int j = list.size() - 1 ; j > i; j -- ) { 
		if (list.get(j).getUgr_ent_id()==list.get(i).getUgr_ent_id()) { 
		list.remove(j); 
		} 
		} 
		} 
	} 
}