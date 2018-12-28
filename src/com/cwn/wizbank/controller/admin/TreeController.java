package com.cwn.wizbank.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.entity.vo.AeTreeNodeVo;
import com.cwn.wizbank.entity.vo.TreeNodeVo;
import com.cwn.wizbank.services.AeTreeNodeService;
import com.cwn.wizbank.services.UserTreeNodeService;
import com.cwn.wizbank.utils.Params;

/**
 * tree页面跳转 Controller
 * 树
 */
@Controller("adminTreeController")
@RequestMapping("admin/tree")
public class TreeController {
	@Autowired
	AeTreeNodeService aeTreeNodeService;
	
	@Autowired
	UserTreeNodeService userTreeNodeService;
	
	//供单个课程调用
	@RequestMapping(method = RequestMethod.GET, value = "getCourseTreesingle")
	public String getCourseTree(ModelMap map) {
		return "admin/tree/coursetree";
	}

	@RequestMapping(method = RequestMethod.GET, value = "getCourseTreemultiple")
	public String getCourseTrees(ModelMap map) {
		return "admin/tree/coursetrees";
	}
	@RequestMapping(method = RequestMethod.GET, value = "getGradeTree")
	public String getRankTree(ModelMap map) {
		//获取必修课所有列表
		return "admin/tree/gradetree";
	}
	@RequestMapping("gradeTreeJson")
	@ResponseBody
	public List<TreeNodeVo> gradeTreeJson(@RequestParam(value = "treeId", required = false, defaultValue = "0") long treeId,Model model,WizbiniLoader wizbini,loginProfile prof) {
		return  aeTreeNodeService.getGradeTree(treeId,wizbini.cfgSysSetupadv.isTcIndependent(),prof.my_top_tc_id);
	}
	
	/**
	 * 分层获取用户组树
	 * @param treeId
	 * @param map
	 * @param prof
	 * @param wizbini
	 * @return
	 */
	@RequestMapping("getUserGroupLevelTree")
	@ResponseBody
	public List<TreeNodeVo> getUserGroupLevelTree(@RequestParam(value = "treeId", required = false, defaultValue = "-1") long treeId,
			@RequestParam(value = "showSubordinate", required = false, defaultValue = "false") boolean showSubordinateInd,
			ModelMap map,loginProfile prof,WizbiniLoader wizbini) {
		return userTreeNodeService.getGroupTreeLevelJson(treeId,prof.getUsr_ent_id(), wizbini.cfgSysSetupadv.isTcIndependent(),
				prof.my_top_tc_id, prof.current_role, AccessControlWZB.isRoleTcInd(prof.current_role),prof.cur_lan , showSubordinateInd);
	}
	
	@RequestMapping("getCourseTreeJson")
	@ResponseBody
	public List<AeTreeNodeVo> adminCourseTreeJson(
			loginProfile prof,
			WizbiniLoader wizbini,
			@RequestParam(value = "cosType", required = false, defaultValue = "course_and_exam") String cosType,
			@RequestParam(value = "firstLevel", required = false, defaultValue = "false") Boolean firstLevel,
			Params param,
			Model model) {
		return  aeTreeNodeService.getAdminTraingCenterCourse(prof.my_top_tc_id, prof.usr_ent_id, cosType, prof.current_role,firstLevel,prof.cur_lan);
	}
	
	
}
