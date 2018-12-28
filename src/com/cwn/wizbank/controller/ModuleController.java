package com.cwn.wizbank.controller;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.entity.Module;
import com.cwn.wizbank.entity.ModuleEvaluation;
import com.cwn.wizbank.entity.Progress;
import com.cwn.wizbank.services.ModuleEvaluationService;
import com.cwn.wizbank.services.ModuleManagementService;
import com.cwn.wizbank.services.ModuleService;
import com.cwn.wizbank.services.ProgressService;
import com.cwn.wizbank.services.RegUserService;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.Page;

/**
 * 
 * @author lance 课程模块
 */
@Controller
@RequestMapping("module")
public class ModuleController {

	@Autowired
	ModuleManagementService moduleManagementService;
	
	@Autowired
	ModuleService moduleService;
	
	@Autowired
	RegUserService regUserService;
	
	@Autowired
	ProgressService progressService;
	
	@Autowired
	ModuleEvaluationService moduleEvaluationService;
	
	/**
	 * 获取考试基本信息
	 * @param page
	 * @param tkh_id
	 * @param mod_id
	 * @return
	 */
	@RequestMapping("getTstStart")
	@ResponseBody
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getTstStart(Model model, Page page, loginProfile prof, @RequestParam(value="tkh_id", defaultValue="false") long tkh_id, 
		@RequestParam(value="mod_id", defaultValue="false") long mod_id) throws Exception {
		moduleService.getModuleDetail(page, mod_id);
		model.addAttribute("user", regUserService.getUserDetail(prof.usr_ent_id, prof.usr_ent_id));
		return JsonFormat.format(model, page);
	}
	
	/**
	 * 获取考试试题
	 * @param page
	 * @param tkh_id
	 * @param mod_id
	 * @param window_name
	 * @return
	 */
	@RequestMapping("getTstDetail")
	@ResponseBody
	@SuppressWarnings("rawtypes")
	public String getTstDetail(Model model, Page page, loginProfile prof, WizbiniLoader wizbini, HttpSession sess,
		@RequestParam(value="tkh_id", defaultValue="false") long tkh_id, 
		@RequestParam(value="mod_id", defaultValue="false") long mod_id, 
		@RequestParam(value="window_name", defaultValue="false") String window_name) throws Exception {
		model.addAttribute(moduleManagementService.getTstMap(null, prof, wizbini, sess, tkh_id, mod_id, window_name));
		return JsonFormat.format(model, page);
	}
	
	/**
	 * 提交答案
	 * @param tkh_id
	 * @param mod_id
	 * @param que_id_str 题目id
	 * @param que_anwser_option_str 回答答案
	 * @param start_time 开始考试时间
	 * @return
	 */
	@RequestMapping("submitTst")
	@ResponseBody
	@SuppressWarnings("rawtypes")
	public String submitTst(Model model, Page page, loginProfile prof, @RequestParam(value="tkh_id", defaultValue="false") long tkh_id, 
			@RequestParam(value="mod_id", defaultValue="false") long mod_id, 
			@RequestParam(value="que_id_str", defaultValue="false") String que_id_str,
			@RequestParam(value="que_anwser_option_str", defaultValue="false") String que_anwser_option_str,
			@RequestParam(value="que_anwser_option_id_str", defaultValue="false") String que_anwser_option_id_str,
			@RequestParam(value="start_time", defaultValue="false") Timestamp start_time) throws Exception {
		model.addAttribute("score", moduleManagementService.sendTstResult(null, prof, tkh_id, mod_id, que_id_str,que_anwser_option_id_str, que_anwser_option_str, start_time));
		return JsonFormat.format(model);
	}
	
	/**
	 * 获取答题情况
	 * @param tkh_id
	 * @param mod_id
	 * @return
	 */
	@RequestMapping("getTstScore/{attempt}")
	@ResponseBody
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getTstScore(Model model, Page page, loginProfile prof, @PathVariable(value = "attempt") long attempt, 
			@RequestParam(value="tkh_id", defaultValue="false") long tkh_id, 
			@RequestParam(value="mod_id", defaultValue="false") long mod_id){
		progressService.getAnswerDetail(page, mod_id, tkh_id, attempt);
		model.addAttribute("user", regUserService.getUserDetail(prof.usr_ent_id, prof.usr_ent_id));
		return JsonFormat.format(model, page);
		
	}
	
	/**
	 * 获取作业成绩
	 * @param tkh_id
	 * @param mod_id
	 * @return
	 */
	@RequestMapping("getAssScore")
	@ResponseBody
	@SuppressWarnings({ "rawtypes"})
	public String getAssScore(Model model, Page page, loginProfile prof, @RequestParam(value="tkh_id", defaultValue="false") long tkh_id, 
			@RequestParam(value="mod_id", defaultValue="false") long mod_id) throws Exception {
		model.addAttribute(moduleManagementService.getAssScore(null, prof, tkh_id, mod_id));
		return JsonFormat.format(model, page);
		
	}
	
	/**
	 * 提交作业
	 * @param tkh_id
	 * @param mod_id
	 * @param que_id_str 题目id
	 * @param que_anwser_option_str 回答答案
	 * @param start_time 开始考试时间
	 * @return
	 */
	@RequestMapping("submitAss")
	@ResponseBody
	public void submitAss(loginProfile prof, WizbiniLoader wizbini, @RequestParam(value="tkh_id", defaultValue="false") long tkh_id, 
			@RequestParam(value="mod_id", defaultValue="") long mod_id, 
			@RequestParam(value="step", defaultValue="") long step,
			@RequestParam(value="comment", defaultValue="false") List<String> comment_list,
			@RequestParam(value="ass_file", required = false) List<MultipartFile> file_list,
			@RequestParam(value="file_detail_str", defaultValue="") String file_detail_str) throws Exception {
		moduleManagementService.submitAssignment(null, prof, wizbini, tkh_id, mod_id, step, comment_list, file_list, file_detail_str);
	}
	
	/**
	 * 获取作业内容
	 * @param tkh_id
	 * @param mod_id
	 * @return
	 */
	@RequestMapping("getAssContent")
	@ResponseBody
	@SuppressWarnings("rawtypes")
	public String getAssContent(Model model, Page page, loginProfile prof, WizbiniLoader wizbini,
			@RequestParam(value="tkh_id", defaultValue="false") long tkh_id, 
			@RequestParam(value="mod_id", defaultValue="false") long mod_id) throws Exception {
		model.addAttribute(moduleManagementService.getAssContent(null, prof, wizbini, tkh_id, mod_id));
		return JsonFormat.format(model, page);
	}
	
	/**
	 * 获取教材或视频点播内容
	 * @param tkh_id
	 * @param mod_id
	 * @return
	 */
	@RequestMapping("getRdgContent")
	@ResponseBody
	@SuppressWarnings("rawtypes")
	public String getRdgContent(Model model, Page page, loginProfile prof, WizbiniLoader wizbini,
			@RequestParam(value="tkh_id", defaultValue="false") long tkh_id, 
			@RequestParam(value="mod_id", defaultValue="false") long mod_id) throws Exception {
		model.addAttribute(moduleManagementService.getRdgContent(null, prof, wizbini, tkh_id, mod_id));
		return JsonFormat.format(model, page);
	}
	
	/**
	 * 获取参考内容
	 * @param tkh_id
	 * @param mod_id
	 * @return
	 */
	@RequestMapping("getRefContent")
	@ResponseBody
	@SuppressWarnings("rawtypes")
	public String getRefContent(Model model, Page page, loginProfile prof, WizbiniLoader wizbini,
			@RequestParam(value="tkh_id", defaultValue="false") long tkh_id, 
			@RequestParam(value="mod_id", defaultValue="false") long mod_id) throws Exception {
		model.addAttribute(moduleManagementService.getRefContent(null, prof, wizbini, tkh_id, mod_id));
		return JsonFormat.format(model, page);
	}
	
	/**
	 * 获取AICC课件学习报告
	 * @param tkh_id
	 * @param res_id
	 * @param mod_id
	 * @return
	 */
	@RequestMapping("getAiccReport")
	@ResponseBody
	@SuppressWarnings("rawtypes")
	public String getAiccReport(Model model, Page page, loginProfile prof, 
			@RequestParam(value="tkh_id", defaultValue="false") long tkh_id, 
			@RequestParam(value="res_id", defaultValue="false") long res_id, 
			@RequestParam(value="mod_id", defaultValue="false") long mod_id) throws Exception {
		model.addAttribute(moduleManagementService.getAiccReport(null, prof, tkh_id, res_id, mod_id));
		return JsonFormat.format(model, page);
	}
	
	/**
	 * 上传离线学习的学习记录
	 * @param tkh_id
	 * @param res_id
	 * @param mod_id
	 * @param duration 学习时长
	 * @param last_time 最后学习时间
	 * @return
	 */
	@RequestMapping("sendModuleTrack")
	@ResponseBody
	public void sendModuleTrack(loginProfile prof, @RequestParam(value="tkh_id", defaultValue="false") long tkh_id, 
			@RequestParam(value="res_id", defaultValue="false") long res_id, 
			@RequestParam(value="mod_id", defaultValue="false") long mod_id,
			@RequestParam(value="duration", defaultValue="false") long duration,
			@RequestParam(value="last_time", defaultValue="false") Timestamp last_time) throws Exception {
		moduleManagementService.sendModuleTrack(null, prof, tkh_id, res_id, mod_id, duration, last_time);
	}
	
	
	
	/**
	 * 获取考试报告详情
	 * @param page
	 * @param tkh_id
	 * @param mod_id
	 * @param window_name
	 * @return
	 */
	@RequestMapping("getTstReportDetail")
	@ResponseBody
	@SuppressWarnings("rawtypes")
	public String getTstReportDetail(Model model, Page page, loginProfile prof, WizbiniLoader wizbini, HttpSession sess,
		@RequestParam(value = "attempt", defaultValue="false") int attempt, 	
		@RequestParam(value="tkh_id", defaultValue="false") int tkh_id, 
		@RequestParam(value="mod_id", defaultValue="false") int mod_id, 
		@RequestParam(value="que_id_lst", defaultValue="0") String[] que_id_lst) throws Exception {
		model.addAttribute(moduleManagementService.getTstReportDetailMap(null, prof, tkh_id, mod_id, attempt,que_id_lst));
		return JsonFormat.format(model, page);
	}
	
	/**
	 * 获取每个题目的答对情况
	 * @param model
	 * @param page
	 * @param prof
	 * @param wizbini
	 * @param sess
	 * @param attempt
	 * @param tkh_id
	 * @param mod_id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getAllAnswerDetail")
	@ResponseBody
	@SuppressWarnings("rawtypes")
	public String getAllAnswerDetail(Model model, Page page, loginProfile prof, WizbiniLoader wizbini, HttpSession sess,
			@RequestParam(value = "attempt", defaultValue="false") int attempt, 	
			@RequestParam(value="tkh_id", defaultValue="false") long tkh_id, 
			@RequestParam(value="mod_id", defaultValue="false") int mod_id) throws Exception {
			progressService.selectAllAnswerDetail(page, mod_id, tkh_id, attempt, prof.usr_id);
			return JsonFormat.format(model, page);
		}
	
	
	/**
	 * 未评分
	 * @param model
	 * @param page
	 * @param prof
	 * @param attempt
	 * @param tkh_id
	 * @param mod_id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("selectNotScore")
	@ResponseBody
	@SuppressWarnings("rawtypes")
	public String selectNotScore(Model model, Page page, loginProfile prof, WizbiniLoader wizbini, HttpSession sess,
		@RequestParam(value = "attempt", defaultValue="false") int attempt, 	
		@RequestParam(value="tkh_id", defaultValue="false") int tkh_id, 
		@RequestParam(value="mod_id", defaultValue="false") int mod_id) throws Exception {
		model.addAttribute(progressService.selectNotScore(mod_id, tkh_id, attempt,prof));
		return JsonFormat.format(model);
	}
	
	
	/**
	 * 获取考试最高分数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getMaxProgressAttempt")
	@ResponseBody
	public String selectNotScore(Model model, Page<Progress> page, loginProfile prof,
		@RequestParam(value="tkh_id", defaultValue="0") int tkh_id, 
		@RequestParam(value="mod_id", defaultValue="0") int mod_id) throws Exception {
		model.addAttribute(progressService.getMaxProgressAttempt(page, mod_id, tkh_id));
		return JsonFormat.format(model);
	}
	
	/**
	 * 学员端考试报告report
	 * @param mav
	 * @param prof
	 * @return
	 */
	@RequestMapping(value = "report")
	public ModelAndView tstScore(ModelAndView mav, loginProfile prof,
			@RequestParam(value="tkh_id", defaultValue="false") long tkh_id, 
			@RequestParam(value="mod_id", defaultValue="false") long mod_id){
		mav = new ModelAndView("course/report");
		mav.addObject("mod_id", mod_id);
		mav.addObject("tkh_id", tkh_id);
		return mav;
	}
	
	
	@RequestMapping(value = "getAttempt")
	@ResponseBody
	public String getAttempt(Model model,  loginProfile prof,  @RequestParam(value="tkh_id", defaultValue="0") long tkh_id,
		@RequestParam(value="mod_id", defaultValue="0") long mod_id) throws Exception {
		Module mod = moduleService.get(mod_id);
		ModuleEvaluation mv = moduleEvaluationService.getByIds(mod_id, tkh_id, prof.getUsr_ent_id());
		Map map = new HashMap<String,Object>();
		map.put("mod_max_usr_attempt", mod.getMod_max_usr_attempt());
		map.put("mov_total_attempt", mv.getMov_total_attempt());
		return JsonFormat.format(map);
	}

	
	
}
