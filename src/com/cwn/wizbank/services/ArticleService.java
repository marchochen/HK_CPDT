package com.cwn.wizbank.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.common.SNS;
import com.cwn.wizbank.entity.Article;
import com.cwn.wizbank.entity.ArticleType;
import com.cwn.wizbank.persistence.ArticleMapper;
import com.cwn.wizbank.utils.ImageUtil;
import com.cwn.wizbank.utils.Page;
/**
 * service 实现
 */
@Service
public class ArticleService extends BaseService<Article> {

	@Autowired
	ArticleMapper articleMapper;
	@Autowired
	SnsValuationLogService snsValuationLogService;
	@Autowired
	SnsCountService snsCountService;
	@Autowired
	SnsCommentService snsCommentService;
	/**
	 * 获取所有的文章信息
	 * 
	 * @param prof
	 * @param page
	 * @param wizbini
	 * @param tcr_id
	 */
	public Page<Article> pageArticle(long usr_ent_id, int push_mobile,Page<Article> page, long tcr_id, List<Long> aty_id_list,long new_duration) {
		page.getParams().put("tcr_id", tcr_id);
		page.getParams().put("aty_id_list", aty_id_list);
		page.getParams().put("curTime", getDate());
		page.getParams().put("new_duration", new_duration);
		page.getParams().put("userEntId", usr_ent_id);
		page.getParams().put("art_push_mobile", push_mobile);
		//List<Article> results = 
		articleMapper.selectList(page);
		for(Article article : page.getResults()){
			ImageUtil.combineImagePath(article);
			article.setUserLike(snsValuationLogService.getByUserId(article.getArt_id(), usr_ent_id, SNS.MODULE_ARTICLE, 0));
			article.setSnsCount(snsCountService.getByTargetInfo(article.getArt_id(), SNS.MODULE_ARTICLE));	
			article.setArt_comment_count(snsCommentService.getCommentCount((long)article.getArt_id(), SNS.MODULE_ARTICLE));
			if(page.getPageSize() == 1){
				article.setSns(snsCountService.getUserSnsDetail(article.getArt_id(), usr_ent_id, SNS.MODULE_ARTICLE));
			}
		}
		if(page.getSortname() != null && -1 !=page.getSortname().indexOf("s_cnt_like_count")){
			page.setResults(sortByLikeCount(page.getResults(),page.getSortorder()));
		}
		//page.setResults(results);
		return page;
	}
	/**
	 * 获得文章信息，在打开时增加访问量+1
	 * (此处后期有可能更改)
	 */
	public Article get(long id){
		Article article = articleMapper.get(id);
		if(article != null){
			ImageUtil.combineImagePath(article);		
			article.setArt_access_count(article.getArt_access_count()+1);
			articleMapper.update(article);			
		}
		return article;
	}
	/**
	 * 根据文章分类获取列表
	 * @param tcr_id
	 * @return
	 */
	public List<ArticleType> getarticleType(long tcr_id){
		ArticleType articleType = new ArticleType();
		articleType.setAty_tcr_id(tcr_id);
		return articleMapper.selectArticleType(articleType);
	}
	
	private List<Article> sortByLikeCount(List<Article> data , String sort){
		for(int i=0 ; i<data.size() ; i++){
			Article pre = data.get(i);
			int preCount = 0 ;
			if(null!=pre && null!=pre.getSnsCount() && null!=pre.getSnsCount().getS_cnt_like_count()){
				preCount = pre.getSnsCount().getS_cnt_like_count();
			}
			
			for(int j = i+1 ;j<data.size();j++){
				Article after = data.get(j);
				int afterCount = 0 ;
				if(null!=after && null!=after.getSnsCount() && null!=after.getSnsCount().getS_cnt_like_count()){
					afterCount = after.getSnsCount().getS_cnt_like_count();
				}
				if(sort.equalsIgnoreCase("desc")){
					if(preCount < afterCount ){
						data.set(i, after);
						data.set(j, pre);
					}
				}else{
					if(preCount > afterCount ){
						data.set(i, after);
						data.set(j, pre);
					}
				}
			}
		}
		return data;
	}
	
	
}