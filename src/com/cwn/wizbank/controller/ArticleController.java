package com.cwn.wizbank.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.common.SNS;
import com.cwn.wizbank.entity.Article;
import com.cwn.wizbank.entity.ArticleType;
import com.cwn.wizbank.exception.AuthorityException;
import com.cwn.wizbank.exception.DataNotFoundException;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.security.annotation.HasPermission;
import com.cwn.wizbank.security.service.AclService;
import com.cwn.wizbank.services.AcRoleFunctionService;
import com.cwn.wizbank.services.ArticleService;
import com.cwn.wizbank.services.SnsCollectService;
import com.cwn.wizbank.services.SnsCommentService;
import com.cwn.wizbank.services.SnsCountService;
import com.cwn.wizbank.utils.EncryptUtil;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.LabelContent;
import com.cwn.wizbank.utils.Page;
import com.cwn.wizbank.utils.Params;

/**
 * 文章
 * @author leon.li
 * 2014-8-5 下午2:54:23
 */
@Controller
@RequestMapping("article")
public class ArticleController {
	
	@Autowired
	ArticleService articleService;
	
	@Autowired
	SnsCollectService snsCollectService;
	
	@Autowired
	AcRoleFunctionService acRoleFunctionService;
	
	@Autowired
	SnsCountService snsCountService;
	
	@Autowired
	SnsCommentService snsCommentService;
	
	@Autowired
	AclService aclService;
	
	@RequestMapping("")
	@HasPermission(AclFunction.FTN_LRN_ART_MGT)
	public ModelAndView announce(ModelAndView mav, @RequestParam(value = "id", required = false, defaultValue = "0") String id,
			loginProfile prof) throws DataNotFoundException{
		
		if(!"0".equals(id)){
			long real_id = EncryptUtil.cwnDecrypt(id);
			if (real_id > 0) {
				if (articleService.get(real_id) == null) {
					throw new DataNotFoundException(LabelContent.get(prof.cur_lan, "error_data_not_found"));
				}
				mav.addObject("id", real_id);
			}
		}
		mav = new ModelAndView("article/article_list");
		return mav;
	}
	
	@RequestMapping("mgt_comments")
	@HasPermission(value = {AclFunction.FTN_LRN_ART_MGT, AclFunction.FTN_AMD_ARTICLE_MAIN})
	public ModelAndView announceManage(ModelAndView mav, @RequestParam(value = "id", required = false, defaultValue = "0") long id,
			loginProfile prof) throws DataNotFoundException, AuthorityException{
		boolean isManage = false;
		isManage = aclService.hasAnyPermission(prof.current_role, AclFunction.FTN_AMD_ARTICLE_MAIN);
		if(isManage){
			mav = new ModelAndView("article/mgt_art_comment");
			if (id > 0) {
				if (articleService.get(id) == null) {
					throw new DataNotFoundException(LabelContent.get(prof.cur_lan, "error_data_not_found"));
				}
				mav.addObject("id", id);
			}
		}
		else{
			throw new AuthorityException(LabelContent.get(prof.cur_lan, "error_no_authority"));
		}
		
		return mav;
	}
	
	@RequestMapping("/detail/{encId}")
	@HasPermission(value = {AclFunction.FTN_LRN_ART_MGT, AclFunction.FTN_AMD_ARTICLE_MAIN})
	public ModelAndView announceDetail(ModelAndView mav,@PathVariable(value="encId")String encId){
		long art_id = EncryptUtil.cwnDecrypt(encId);
		mav = new ModelAndView("article/article");
		mav.addObject("atr_id", art_id);
		return mav;
	}
	
	@RequestMapping("pageJson/{aty_id}")
	@ResponseBody
	@HasPermission(AclFunction.FTN_LRN_ART_MGT)
	public String list(Model model, Page<Article> page, WizbiniLoader wizbini, @PathVariable(value="aty_id") long aty_id,
			@RequestParam(value = "tcr_id", defaultValue="0") long tcr_id,
			@RequestParam(value = "push_mobile", defaultValue="0") int push_mobile,loginProfile prof) {
		if(wizbini.cfgSysSetupadv.isTcIndependent()){
			tcr_id = prof.my_top_tc_id;	//不把prof传到service层是为了解耦合
		}
		List<Long> aty_id_list = new ArrayList<Long>();
		if(aty_id > 0){
			aty_id_list.add(aty_id);
		} else {
			List<ArticleType> list = articleService.getarticleType(prof.my_top_tc_id);
			aty_id_list.add(0l);
			for(int i=0;i<list.size();i++){
				aty_id_list.add(list.get(i).getAty_id());
			}
		}
		articleService.pageArticle(prof.usr_ent_id, push_mobile, page, tcr_id, aty_id_list, wizbini.cfgSysSetupadv.getNewestDuration());
		return JsonFormat.format(model, page);
	}
	
	
	@RequestMapping("detailJson/{encId}")
	@ResponseBody
	@HasPermission(value = {AclFunction.FTN_LRN_ART_MGT, AclFunction.FTN_AMD_ARTICLE_MAIN})
	public String detail(
			loginProfile prof,
			@PathVariable(value="encId") String encId,
			Params param,
			Model model) {
		
		long id = EncryptUtil.cwnDecrypt(encId);
		Article article =articleService.get(id);
		article.setArt_comment_count(snsCommentService.getCommentCount(id, SNS.MODULE_ARTICLE));
		model.addAttribute(article);
		model.addAttribute("snsCount", snsCountService.getByTargetInfo(id, SNS.MODULE_ARTICLE));
		model.addAttribute("sns", snsCountService.getUserSnsDetail(id, prof.usr_ent_id, SNS.MODULE_ARTICLE));
		return JsonFormat.format(param, model);
	}
	
	@RequestMapping("articleType")
	@ResponseBody
	@HasPermission(AclFunction.FTN_LRN_ART_MGT)
	public String articleType(Model model, loginProfile prof, Params param) {
		model.addAttribute("article_type_list", articleService.getarticleType(prof.my_top_tc_id));
		return JsonFormat.format(param, model);
	}
	
	@RequestMapping("collectArticle/{s_clt_module}/{s_clt_target_id}")
	@ResponseBody
	@HasPermission(AclFunction.FTN_LRN_ART_MGT)
	public void collectArticle(Model model, loginProfile prof, @PathVariable(value="s_clt_target_id") long s_clt_target_id, 
			@PathVariable(value="s_clt_module") String s_clt_module) throws Exception {
		snsCollectService.add(model,s_clt_target_id, prof.usr_ent_id, s_clt_module);
	}
}

