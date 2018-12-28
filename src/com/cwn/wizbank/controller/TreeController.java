package com.cwn.wizbank.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.tree.TreeNode;
import com.cwn.wizbank.services.KbCatalogService;
import com.cwn.wizbank.services.TagService;
import com.cwn.wizbank.services.TcTrainingCenterService;
import com.cwn.wizbank.utils.JsonFormat;

/**
 * Tree Controller
 * 
 * 树
 */
@Controller
@RequestMapping("tree")
public class TreeController {
	@Autowired
	TagService tagService;

	@Autowired
	TcTrainingCenterService tcTrainingCenterService;

	@Autowired
	KbCatalogService kbCatalogService;

	@RequestMapping("tagListJson")
	@ResponseBody
	public String tagListJson(Model model, loginProfile prof, @RequestParam(value = "q", required = false, defaultValue = "") String filter) throws Exception {
		model.addAttribute("records", tagService.treeJsonList(filter, prof));
		return JsonFormat.format(model);
	}

	@RequestMapping("catalogListJson")
	@ResponseBody
	public String catalogListJson(Model model, loginProfile prof, @RequestParam(value = "q", required = false, defaultValue = "") String filter) throws Exception {
		model.addAttribute("records", kbCatalogService.treeJsonList(filter, prof));
		return JsonFormat.format(model);
	}

	@RequestMapping("tcListJson/{head}")
	@ResponseBody
	public List<TreeNode> tcListJson(Model model, loginProfile prof, @RequestParam(value = "id", required = false) Long node_id, @PathVariable(value = "head") String head) throws Exception {
		return tcTrainingCenterService.getTrainingCenterTree(node_id, prof.usr_ent_id, head, prof);
	}
}
