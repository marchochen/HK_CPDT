/**
 * 
 */
package com.cwn.wizbank.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.entity.Profession;
import com.cwn.wizbank.entity.ProfessionItem;
import com.cwn.wizbank.exception.DataNotFoundException;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.security.annotation.HasPermission;
import com.cwn.wizbank.services.ProfessionItemService;
import com.cwn.wizbank.services.ProfessionService;
import com.cwn.wizbank.services.TcTrainingCenterService;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.Page;

@Controller
@RequestMapping("profession")
@HasPermission(AclFunction.FTN_LRN_LEARNING_PROFESSION)
public class ProfessionController {

	@Autowired
	ProfessionService professionService;
	@Autowired
	ProfessionItemService professionItemService;
	@Autowired
	TcTrainingCenterService tcTrainingCenterService;
	
	@RequestMapping("")
	public ModelAndView profession(ModelAndView mav, loginProfile prof) throws DataNotFoundException {
		List<Profession> pfs_list = professionService.get_pfs_list(prof);
		long cur_usr_ugr_id = professionService.getCurUserUgrId(prof.usr_ent_id);
		mav = new ModelAndView("user/profession");
		mav.addObject("pfs_list", pfs_list);
		mav.addObject("cur_usr_ugr_id", cur_usr_ugr_id);
		return mav;
	}

	// 职业发展学习任务职级对应课程
	@RequestMapping("/professionitem")
	public ModelAndView profession_item(loginProfile prof, ModelAndView mav, @RequestParam(value = "pfs_ugr_id", required = false) long pfs_ugr_id
			, @RequestParam(value = "pfs_id", required = false) long pfs_id) throws Exception {
		mav = new ModelAndView("user/professionitem");
		professionService.get_ugr_list(mav, pfs_id, prof);
		mav.addObject("cur_pfs_ugr_id", pfs_ugr_id);
		mav.addObject("cur_pfs_id", pfs_id);
		return mav;
	}
		
	@RequestMapping("professionitem/json")
	@ResponseBody
	public String list(WizbiniLoader wizbini, loginProfile prof, Page<ProfessionItem> page, Model model, @RequestParam(value = "pfs_ugr_id", required = false) long pfs_ugr_id
			, @RequestParam(value = "pfs_id", required = false) long pfs_id) throws Exception {
		professionItemService.get_psi_list(prof, model, pfs_ugr_id, pfs_id, page);
		return JsonFormat.format(model, page);
	}
}
