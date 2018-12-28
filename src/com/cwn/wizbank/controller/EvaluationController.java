package com.cwn.wizbank.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.entity.Module;
import com.cwn.wizbank.services.ForCallOldAPIService;
import com.cwn.wizbank.services.ModuleService;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.Page;


/**
 * 调查问卷
 * @author leon.li
 *
 */
@Controller
@RequestMapping("evaluation")
public class EvaluationController {
	
	@Autowired
	ModuleService moduleService;
	
	@Autowired
	ForCallOldAPIService forCallOldAPIService;
	
	@RequestMapping("topJson")
	@ResponseBody
	public String evaluation(Model model, WizbiniLoader wizbini, loginProfile prof, Page<Module> page) {
		page.setPageSize(1);
		long tcrId = 0;
		if (wizbini.cfgSysSetupadv.isTcIndependent()) {
			tcrId = prof.my_top_tc_id;
		}
		moduleService.getPublicEvaluation(page, prof.usr_ent_id, tcrId, 0);
		return JsonFormat.format(model, page);
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping("detailJson")
	@ResponseBody
	public String evaDetail(Model model, WizbiniLoader wizbini, loginProfile prof, Page page) throws Exception{
		HashMap resultMap=forCallOldAPIService.getPublicEvalDetail(null, Long.parseLong((String)page.getParams().get("mod_id")));
		model.addAttribute("result", resultMap);
		model.addAttribute("startTime",moduleService.getDatabaseTime().getTime());
		return JsonFormat.format(model, page);
	}
	@SuppressWarnings("rawtypes")
	@RequestMapping("subEvaluation")
	@ResponseBody
	public String subEvaluation(Model model, WizbiniLoader wizbini, loginProfile prof, Page page) throws Exception{
		long startTime = Long.parseLong((String)page.getParams().get("startTime"));
		long useTime = (new Date().getTime() - startTime)/1000/60;
		forCallOldAPIService.submitPublicEval(null, prof, Long.parseLong((String)page.getParams().get("mod_id")), (String)page.getParams().get("que_id_lst"), (String)page.getParams().get("que_anwser_option_lst"), useTime, new Timestamp(startTime));
		return JsonFormat.format(model, page);
	}

}
