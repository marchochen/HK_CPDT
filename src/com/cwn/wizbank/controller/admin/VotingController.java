package com.cwn.wizbank.controller.admin;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.db.view.ViewTrainingCenter;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.util.cwSysMessage;
import com.cwn.wizbank.entity.ObjectActionLog;
import com.cwn.wizbank.entity.TcTrainingCenter;
import com.cwn.wizbank.entity.Voting;
import com.cwn.wizbank.entity.VotingResponseResult;
import com.cwn.wizbank.exception.MessageException;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.security.annotation.HasPermission;
import com.cwn.wizbank.services.ForCallOldAPIService;
import com.cwn.wizbank.services.TcTrainingCenterService;
import com.cwn.wizbank.services.VotingService;
import com.cwn.wizbank.systemLog.SystemLogContext;
import com.cwn.wizbank.systemLog.SystemLogTypeEnum;
import com.cwn.wizbank.utils.EncryptUtil;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.Page;

/**
 * 投票的controller
 *
 * @author andrew.xiao
 *
 */
@Controller("adminVotingController")
@RequestMapping("admin/voting")
public class VotingController {

	@Autowired
	private VotingService votingService;

	@Autowired
	private TcTrainingCenterService tcTrainingCenterService;
	
	@Autowired
	ForCallOldAPIService forCallOldAPIService;

	/**
	 * 转发到投票活动列表首页
	 *
	 * @return
	 * @throws MessageException 
	 */
	@RequestMapping("")
	@HasPermission(value = {AclFunction.FTN_AMD_VOTING_MAIN})
	public String page(loginProfile prof) throws Exception {
        //throw new MessageException("label_core_requirements_management_3" );
		if (AccessControlWZB.hasRolePrivilege(prof.current_role, AclFunction.FTN_AMD_VOTING_MAIN) 
				&& AccessControlWZB.isRoleTcInd(prof.current_role)) {
			if (AccessControlWZB.isRoleTcInd(prof.current_role) && !forCallOldAPIService.hasEffTc(null, prof.usr_ent_id)) {
				throw new MessageException("label_core_requirements_management_57");
			}
		}
		return "admin/voting/index";
	}

	/**
	 * 获取投票活动json列表
	 *
	 * @param page
	 * @param prof
	 * @param model
	 * @return
	 * @throws ParseException
	 * @throws SQLException 
	 */
	@RequestMapping("pageJson")
	@ResponseBody
	@HasPermission(value = {AclFunction.FTN_AMD_VOTING_MAIN})
	public String page(Page<Voting> page, loginProfile prof, Model model)
			throws ParseException, SQLException {

		votingService.pageAdmin(page, prof.usr_ent_id,prof.current_role);

		return JsonFormat.format(model, page);
	}

	/**
	 * 转发到创建投票活动的页面
	 *
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "createVotingPage")
	public String createVotingPage(ModelMap map, 
			@RequestParam(value = "tcrId", required = false, defaultValue = "0")Long tcrId) {
		map.addAttribute("type", "create");
		if(tcrId > 0) {
			TcTrainingCenter tcTrainingCenter = tcTrainingCenterService.get(tcrId);
			map.addAttribute("tcTrainingCenter", tcTrainingCenter);
		}
		return "admin/voting/createPage";
	}

	/**
	 * 创建投票活动
	 * @param voting 投票基本信息
	 * @param loginProfile 登录人的信息
	 * @param options 问题的选项
	 * @param optionType 问题的类型：单选或多选
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "createVoting")
	@HasPermission(value = {AclFunction.FTN_AMD_VOTING_MAIN})
	public String createVoting(Voting voting,loginProfile profile,String[] options,String optionType) {
		votingService.createVoting(voting,profile.getUsr_ent_id(),options,optionType);
		ObjectActionLog log = new ObjectActionLog(voting.getVot_id(), 
				null,
				voting.getVot_title(),
				ObjectActionLog.OBJECT_TYPE_VT,
				ObjectActionLog.OBJECT_ACTION_ADD,
				ObjectActionLog.OBJECT_ACTION_TYPE_WEB,
				profile.getUsr_ent_id(),
				profile.getUsr_last_login_date(),
				profile.getIp()
		);
		SystemLogContext.saveLog(log, SystemLogTypeEnum.OBJECT_ACTION_LOG);
		return "redirect:/app/admin/voting";
	}

	/**
	 * 转发到更新页面，和创建页面用的是同一个页面
	 * @param voting
	 * @param map
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET,value="updatePage")
	@HasPermission(value = {AclFunction.FTN_AMD_VOTING_MAIN})
	public String updatePage(Voting voting,ModelMap map){
		map.addAttribute("type", "update");
		if(!StringUtils.isEmpty(voting.getEncrypt_vot_id())){
			Long votId = EncryptUtil.cwnDecrypt(voting.getEncrypt_vot_id());
			voting.setVot_id(votId);
		}
		voting = votingService.get(voting.getVot_id());
		TcTrainingCenter tcTrainingCenter = tcTrainingCenterService.get(voting.getVot_tcr_id());
		map.addAttribute("v", voting);
		map.addAttribute("tcTrainingCenter", tcTrainingCenter);
		return "admin/voting/createPage";
	}

	/**
	 * 更新投票活动
	 * @param voting
	 * @param profile
	 * @param options
	 * @param optionType
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "updateVoting")
	@HasPermission(value = {AclFunction.FTN_AMD_VOTING_MAIN})
	public String updateVoting(Voting voting,loginProfile profile,String[] options,String optionType){
		votingService.updateVoting(voting,profile.getUsr_ent_id(),options,optionType);
		ObjectActionLog log = new ObjectActionLog(voting.getVot_id(), 
				null,
				voting.getVot_title(),
				ObjectActionLog.OBJECT_TYPE_VT,
				ObjectActionLog.OBJECT_ACTION_UPD,
				ObjectActionLog.OBJECT_ACTION_TYPE_WEB,
				profile.getUsr_ent_id(),
				profile.getUsr_last_login_date(),
				profile.getIp()
		);
		SystemLogContext.saveLog(log, SystemLogTypeEnum.OBJECT_ACTION_LOG);
		return "redirect:/app/admin/voting";
	}

	/**
	 * 删除投票活动
	 * @param voting
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET,value="delete")
	@HasPermission(value = {AclFunction.FTN_AMD_VOTING_MAIN})
	public String delete(Voting voting,loginProfile profile){
		voting = votingService.get(voting.getVot_id());
		ObjectActionLog log = new ObjectActionLog(voting.getVot_id(), 
				null,
				voting.getVot_title(),
				ObjectActionLog.OBJECT_TYPE_VT,
				ObjectActionLog.OBJECT_ACTION_DEL,
				ObjectActionLog.OBJECT_ACTION_TYPE_WEB,
				profile.getUsr_ent_id(),
				profile.getUsr_last_login_date(),
				profile.getIp()
		);
		votingService.delete(voting);
		SystemLogContext.saveLog(log, SystemLogTypeEnum.OBJECT_ACTION_LOG);
		return "redirect:/app/admin/voting";
	}

	/**
	 * 取消发布投票活动
	 * @param voting
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET,value="cancelPublished")
	@HasPermission(value = {AclFunction.FTN_AMD_VOTING_MAIN})
	public String cancelPublished(Voting voting,loginProfile profile){
		if(!StringUtils.isEmpty(voting.getEncrypt_vot_id())){
			Long votId = EncryptUtil.cwnDecrypt(voting.getEncrypt_vot_id());
			voting.setVot_id(votId);
		}
		voting = votingService.cancelPublished(voting,profile.getUsr_ent_id());
		ObjectActionLog log = new ObjectActionLog(voting.getVot_id(), 
				null,
				voting.getVot_title(),
				ObjectActionLog.OBJECT_TYPE_VT,
				ObjectActionLog.OBJECT_ACTION_PUB,
				ObjectActionLog.OBJECT_ACTION_TYPE_WEB,
				profile.getUsr_ent_id(),
				profile.getUsr_last_login_date(),
				profile.getIp()
		);
		if(voting.getVot_status().equals("OFF")){
			log.setObjectAction(ObjectActionLog.OBJECT_ACTION_CANCLE_PUB);
		}

		SystemLogContext.saveLog(log, SystemLogTypeEnum.OBJECT_ACTION_LOG);
		return "redirect:/app/admin/voting";
	}

	/**
	 * 查看投票结果
	 * @param voting
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET,value="viewResult")
	public String viewResult(Voting voting,ModelMap map){
		if(!StringUtils.isEmpty(voting.getEncrypt_vot_id())){
			Long votId = EncryptUtil.cwnDecrypt(voting.getEncrypt_vot_id());
			voting.setVot_id(votId);
		}
		List<VotingResponseResult> result = votingService.getVotingResponseResult(voting.getVot_id());
		voting = votingService.get(voting.getVot_id());
		map.addAttribute("result", result);
		map.addAttribute("voting", voting);
		return "admin/voting/viewResult";
	}
}

