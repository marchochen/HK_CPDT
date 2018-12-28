package com.cwn.wizbank.controller.admin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.entity.AcFunction;
import com.cwn.wizbank.entity.AcRole;
import com.cwn.wizbank.entity.SystemStatistics;
import com.cwn.wizbank.entity.UserFavoriteFunction;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.security.service.AclService;
import com.cwn.wizbank.services.AeApplicationService;
import com.cwn.wizbank.services.ForCallOldAPIService;
import com.cwn.wizbank.services.HomeService;
import com.cwn.wizbank.services.KbItemService;
import com.cwn.wizbank.services.RegUserService;
import com.cwn.wizbank.services.ResourcesService;
import com.cwn.wizbank.services.SystemStatisticsService;
import com.cwn.wizbank.services.UserFavoriteFunctionService;
import com.cwn.wizbank.utils.RequestStatus;

@Controller("adminHomeController")
@RequestMapping("admin/home")
public class HomeController {
	
	@Autowired
	HomeService homeService;
	
	@Autowired
	UserFavoriteFunctionService userFavoriteFunctionService;
	
	@Autowired
	RegUserService regUserService;
	
	@Autowired
	ResourcesService resourcesService;
	
	@Autowired
	AeApplicationService aeApplicationService;
	
	@Autowired
	AclService aclService;
	
	@Autowired
	SystemStatisticsService systemStatisticsService;
	
	@Autowired
	KbItemService kbItemService;
	
	@Autowired
	ForCallOldAPIService forCallOldAPIService;
	
	/**
	 * 
	 * @return
	 * @throws SQLException 
	 */
	@RequestMapping("")
	public String adminHome(loginProfile prof, Model model, HttpSession session,HttpServletRequest req,HttpServletResponse response) throws SQLException,IOException{
		
		if(prof!= null && prof.isLrnRole){
			String home_url = prof.goHome(req);
			response.sendRedirect(home_url);
			return null;
		}
		
		//报名审批
		long itemWaitAppCount = 0;
		itemWaitAppCount = aeApplicationService.selectPedingAppCount(prof.usr_ent_id, prof.current_role);
		model.addAttribute("itemWaitAppCount", itemWaitAppCount);
		
		//知识审批，需要判断权限
		long kbWaitAppCount = 0;
		if(aclService.hasAnyPermission(prof.current_role, new String[]{AclFunction.FTN_AMD_KNOWLEDEG_APP})){
			kbWaitAppCount = kbItemService.selectWaitAppCount(prof.usr_ent_id,prof.current_role);
		}
		model.addAttribute("kbWaitAppCount", kbWaitAppCount);
		
		//作业批改
		long assAppCount = 0;
		//试卷批改
		long testAppCount = 0;
		
		//培训管理员和讲师才有作业审批和试卷审批
		if(prof.current_role.equals(AcRole.ROLE_TADM_1) || prof.current_role.equals(AcRole.ROLE_INSTR_1)){
			//作业批改
			assAppCount = resourcesService.getModuleAppCount(prof.usr_ent_id, "ASS", prof.current_role);
			//试卷批改
			testAppCount = resourcesService.getModuleAppCount(prof.usr_ent_id, "DXT,TST", prof.current_role);
		}
		
		model.addAttribute("assAppCount", assAppCount);
		model.addAttribute("testAppCount", testAppCount);
		
		//数据统计
		SystemStatistics statistics = this.systemStatisticsService.getInstanceStatisticForTA(prof.my_top_tc_id, prof.usr_ent_id);
		model.addAttribute("statistics", statistics);
		
		//是否达到用户警告值
		model.addAttribute("sys_warning", forCallOldAPIService.getWarn(null));
		
		String view = "admin/home/index";
		
		return view;
	}
	
	@RequestMapping("markFavoriteFunctionJson")
	@ResponseBody
	public List<AcFunction> favoriteFunctionJson(loginProfile prof, Model model){
		return homeService.getMenusMarkFavorite(prof);
	}
	
	
	@RequestMapping("favoriteFunJson")
	@ResponseBody
	public List<UserFavoriteFunction> userFavorite(loginProfile prof){
		return userFavoriteFunctionService.getList(prof.usr_ent_id, prof.current_role);		
	}
	
	@RequestMapping("addFavorite/{id}")
	@ResponseBody
	public Model addFavorite(loginProfile prof, @PathVariable("id") int funId, Model model){
		userFavoriteFunctionService.addFavorite(prof.usr_ent_id, prof.current_role, funId);
		model.addAttribute(RequestStatus.STATUS, RequestStatus.SUCCESS);
		return model;
	}
	/**
	 * addMultipleFavorite
	 * @param prof
	 * @param funIds
	 * @param model
	 * @return
	 */
	@RequestMapping("addMtpFavorite")
	@ResponseBody
	public Model addFavorite(loginProfile prof, @RequestParam("ids") int[] funIds, Model model){
		userFavoriteFunctionService.addMtpFavorite(prof.usr_ent_id, prof.current_role, funIds);
		model.addAttribute(RequestStatus.STATUS, RequestStatus.SUCCESS);
		return model;
	}
	
	@RequestMapping("delFavorite/{id}")
	@ResponseBody
	public Model delFavorite(loginProfile prof, @PathVariable("id") int funId, Model model){
		userFavoriteFunctionService.deleteFavorite(prof.usr_ent_id, funId);
		model.addAttribute(RequestStatus.STATUS, RequestStatus.SUCCESS);
		return model;
	}
	
	@RequestMapping("roleFavoriteFunctionJson")
	@ResponseBody
	public Model roleFavoriteFunctionJson(loginProfile prof, Model model,@RequestParam(value="role_id")long role_id){
		model.addAttribute("acFunctionList", homeService.roleFavoriteFunctionJson(prof.current_role,role_id));
		if(role_id > 0){
			model.addAttribute("functions", homeService.roleHasFavoriteFunction(role_id));
		}
		return model;
	}
}
