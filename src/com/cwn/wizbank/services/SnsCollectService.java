package com.cwn.wizbank.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.cwn.wizbank.common.SNS;
import com.cwn.wizbank.entity.AeItem;
import com.cwn.wizbank.entity.AeTreeNode;
import com.cwn.wizbank.entity.Article;
import com.cwn.wizbank.entity.KbItem;
import com.cwn.wizbank.entity.SnsCollect;
import com.cwn.wizbank.persistence.AeTreeNodeMapper;
import com.cwn.wizbank.persistence.SnsCollectMapper;
import com.cwn.wizbank.utils.ImageUtil;
import com.cwn.wizbank.utils.Page;
import com.cwn.wizbank.utils.RequestStatus;
/**
 * service 实现
 */
@Service
public class SnsCollectService extends BaseService<SnsCollect> {

	@Autowired
	SnsCollectMapper snsCollectMapper;

	@Autowired
	SnsCountService snsCountService;
	
	@Autowired
	SnsCommentService snsCommentService;
	
	@Autowired
	AeTreeNodeMapper aeTreeNodeMapper;
	
	@Autowired
	KbAttachmentService kbAttachmentService;
	
	public void setSnsCollectMapper(SnsCollectMapper snsCollectMapper) {
		this.snsCollectMapper = snsCollectMapper;
	}
	
	/**
	 * 获取个人课程收藏列表
	 * @param usr_ent_id 用户id 
	 * @param s_clt_module Course ： 课程
	 * @return
	 */
	public Page<AeItem> getCollectItemList(Page<AeItem> page, long usr_ent_id, String s_clt_module,String open_symbol){
		page.getParams().put("usr_ent_id", usr_ent_id);
		page.getParams().put("s_clt_module", s_clt_module);
		page.getParams().put("open_symbol", open_symbol);
		snsCollectMapper.selectCollectItemList(page);
		for(AeItem aeitem : page.getResults()){
			//由于oracle 11G 以下版不支持within group 语法，所以当使用oracle数据库中，SQL中的Tnd_Title会返回常量值Get_Tnd_Title，在程序中再每个课程取一次目录数据
			if(aeitem.getAeTreeNode() != null && aeitem.getAeTreeNode().getTnd_title() != null && "Get_Tnd_Title".equals(aeitem.getAeTreeNode().getTnd_title())){
				String title = "";
				List<AeTreeNode> list = aeTreeNodeMapper.getItemCatalog(aeitem.getItm_id());
				for(AeTreeNode node : list){
					if(node != null){
						if(!"".equals(title)){
							title += ", ";
						}
						title += node.getTnd_title();
					}
				}
				aeitem.getAeTreeNode().setTnd_title(title);
			}
			ImageUtil.combineImagePath(aeitem);
			aeitem.setSnsCount(snsCountService.getByTargetInfo(aeitem.getItm_id(), SNS.MODULE_COURSE));
			aeitem.setCnt_comment_count(snsCommentService.getCommentCount(aeitem.getItm_id(), SNS.MODULE_COURSE));
		}
		return page;
	}
	
	/**
	 * 获取个人文章收藏列表
	 * @param usr_ent_id 用户id 
	 * @param s_clt_module Article ： 文章
	 * @return
	 */
	public Page<Article> getCollectArticleList(Page<Article> page, long usr_ent_id, String s_clt_module){
		page.getParams().put("usr_ent_id", usr_ent_id);
		page.getParams().put("s_clt_module", s_clt_module);
		if(page.getParams().containsKey("isMobile")&&page.getParams().get("isMobile").equals("true")){
			page.getParams().put("art_push_mobile", 1);
		}
		snsCollectMapper.selectCollectArticleList(page);
		for(Article article : page.getResults()){
			ImageUtil.combineImagePath(article);	
			article.setSnsCount(snsCountService.getByTargetInfo(article.getArt_id(), SNS.MODULE_ARTICLE));
			article.setArt_comment_count(snsCommentService.getCommentCount((long) article.getArt_id(), SNS.MODULE_ARTICLE));
		}
		return page;
	}
	
	/**
	 * 获取个人知识收藏列表
	 * @param usr_ent_id 用户id 
	 * @param s_clt_module Knowledge ： 知识
	 * @return
	 */
	public Page<KbItem> getCollectKnowledgeList(Page<KbItem> page, long usr_ent_id, String s_clt_module){
		page.getParams().put("usr_ent_id", usr_ent_id);
		page.getParams().put("s_clt_module", s_clt_module);
		snsCollectMapper.selectCollectKnowledgeList(page);
		for(KbItem kbItem : page.getResults()){
			ImageUtil.combineImagePath(kbItem.getImageAttachment());
			kbItem.setSnsCount(snsCountService.getByTargetInfo(kbItem.getKbi_id(), SNS.MODULE_KNOWLEDGE));
			kbItem.setKbi_comment_count(snsCommentService.getCommentCount((long) kbItem.getKbi_id(), SNS.MODULE_KNOWLEDGE));
			if(kbItem.getKbi_type().equalsIgnoreCase("DOCUMENT") && kbItem.getKbi_content()!=null && !kbItem.getKbi_content().equals("")){
				String fileName = kbAttachmentService.get(Long.parseLong(kbItem.getKbi_content())).getKba_filename();
				kbItem.setDocType(KbItemService.getDocType(fileName));
			}
		}
		return page;
	}
	
	/**
	 * 获取用户收藏
	 * @param targetId
	 * @param userEntId
	 * @param module
	 * @return
	 */
	public SnsCollect getByUserId(long targetId, long userEntId, String module) {
		Map<String,Object> map = new HashMap<String,Object>();
		SnsCollect collect =  null;
		map.put("targetId", targetId);
		map.put("userEntId", userEntId);
		map.put("module", module);
		
		
		try{
			collect = snsCollectMapper.getByUserId(map);
		}catch(Exception e){
			// 由于页面上快速点击多次，可能导致同一个用户在一个对像下会有多条记录，在这种情况下会出错，所以先删除出错的数据，再重新取数据
			snsCollectMapper.delErrorData(map);
			collect = snsCollectMapper.getByUserId(map);
		}
		return collect;
	}

	/**
	 * 取消收藏
	 * @param model
	 * @param targetId
	 * @param module
	 * @param userEntId
	 * @throws Exception 
	 */
	public void cancel(Model model, long targetId, String module, long userEntId) throws Exception {
		SnsCollect snsCollect = getByUserId(targetId, userEntId, module);

		snsCollectMapper.delete(snsCollect);
		
		
		// 更新统计信息
		long count = snsCountService.updateCollectCount(
				targetId, module, userEntId, false);
		model.addAttribute("count", count);
		model.addAttribute(RequestStatus.STATUS, RequestStatus.SUCCESS);

	}

	/**
	 * 收藏
	 * @param model
	 * @param targetId
	 * @param usr_ent_id
	 * @param module
	 * @throws Exception 
	 */
	public void add(Model model, long targetId, long userEntId, String module) throws Exception {
		
		SnsCollect snsCollect = getByUserId(targetId, userEntId, module);
		if(snsCollect == null){
			snsCollect = new SnsCollect();
			snsCollect.setS_clt_create_datetime(getDate());
			snsCollect.setS_clt_uid(userEntId);
			snsCollect.setS_clt_module(module);
			snsCollect.setS_clt_target_id(targetId);
			snsCollectMapper.insert(snsCollect);
			
			// 更新统计信息
			long count = snsCountService.updateCollectCount(
					targetId, module, userEntId, true);
			model.addAttribute("count", count);
			
			model.addAttribute(RequestStatus.STATUS, RequestStatus.SUCCESS);
		} else {
			model.addAttribute(RequestStatus.STATUS, RequestStatus.EXISTS);
		}
	}
	
}