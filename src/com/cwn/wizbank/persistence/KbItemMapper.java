package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import com.cwn.wizbank.entity.KbItem;
import com.cwn.wizbank.utils.Page;

public interface KbItemMapper extends BaseMapper<KbItem> {

	public List<KbItem> selectPage(Page<KbItem> page);

	public List<KbItem> getKbItemFromCatalog(Long kbc_id);

	public List<KbItem> selectKbItemWithView(Page<KbItem> page);

	public void publish(KbItem kbItem);

	public void approval(KbItem kbItem);

	public List<KbItem> selectKbItemRecentView(Page<KbItem> page);

	public List<KbItem> listStudyPage(Page<KbItem> page);

	public void updateAccessCount(KbItem kbitem);

	public void updateDownloadCount(KbItem kbItem);

	public void updateByShare(KbItem kbItem);

	public boolean hashAuthority(Map<String,Object> map);
	
	public long selectWaitAppCount(Map<String, Object> map);

	public List<KbItem> getKbItemListForUpdateOldData();

	public void updateKbi_filetype(Map<String, Object> params);
	
	/**
	 * 查询 培训中心【params.tcrId】（包括子培训中心）下管理员或者学员（根据【params.type】标示是管理员还是学员）kbItem的数量
	 * @param params
	 * @return
	 */
	public long getkbItemCountByRootTcrId(Map<String, Object> params);
	
	public String getTitle (Long kbi_id);

}