package com.cwn.wizbank.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.entity.LiveItem;
import com.cwn.wizbank.exception.MessageException;
import com.cwn.wizbank.security.annotation.Anonymous;
import com.cwn.wizbank.services.LiveItemService;
import com.cwn.wizbank.services.LiveRecordsService;
import com.cwn.wizbank.utils.EncryptUtil;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.Page;

/**
 * 直播
 * @author bill
 *
 */
@Controller
@RequestMapping("live")
public class LiveController {
	
	@Autowired
	LiveItemService lvItemService;
	
	@Autowired
	LiveRecordsService liveRecordsService;
	
	@RequestMapping("")
	public ModelAndView index(ModelAndView mav) {
		mav = new ModelAndView("live/list");
		
		return mav;
	}
	
	@RequestMapping(value = "view/{lv_enc_id}")
	public ModelAndView getLiveView(ModelAndView mav,@PathVariable(value = "lv_enc_id") String lv_enc_id,loginProfile prof,WizbiniLoader wizbini) throws MessageException{
		mav = new ModelAndView("live/view");
		
		long lvId = EncryptUtil.cwnDecrypt(lv_enc_id);
		
		LiveItem lvItem = lvItemService.getLiveItemById(lvId,prof);
		
		mav.addObject("lv_excess", lvItemService.hasLiveExcess(lvItem, prof, wizbini));
		
		mav.addObject("lv", lvItem);
		
		return mav;
	}
	
	/**
	 * 获取直播json列表
	 * @param model
	 * @param page
	 * @param searchContent
	 * @param status
	 * @param req_source
	 * @return
	 * @throws MessageException 
	 */
	@RequestMapping(value = "liveList")
	@ResponseBody
	public String getLvItemList(Model model,Page<LiveItem> page,WizbiniLoader wizbini,loginProfile prof,
			@RequestParam(value = "searchContent", required = false, defaultValue = "") String searchContent,
			@RequestParam(value = "status", required = false, defaultValue = "") String status,
			@RequestParam(value = "isMobile", required = false, defaultValue = "false") boolean isMobile) throws MessageException{
		
		lvItemService.getLvItemList(page,wizbini,prof,searchContent,status,isMobile,true);
		
		return JsonFormat.format(model, page);
	}
	
	/**
	 * 获取直播详情
	 * @param model
	 * @param page
	 * @param lv_enc_id
	 * @param prof
	 * @return
	 * @throws MessageException 
	 */
	@RequestMapping(value = "detail/{lv_enc_id}")
	@ResponseBody
	public String toPage(Model model, Page<LiveItem> page,@PathVariable(value = "lv_enc_id") String lv_enc_id,WizbiniLoader wizbini,loginProfile prof) throws MessageException{
		
		long lvId = EncryptUtil.cwnDecrypt(lv_enc_id);
		
		LiveItem lvItem =  lvItemService.getLiveItemById(lvId, prof);
		
		model.addAttribute("live", lvItem);
		
		model.addAttribute("lv_excess", lvItemService.hasLiveExcess(lvItem, prof, wizbini));
		
		return JsonFormat.format(model, page);
	}
	
	/**
	 * 获取直播观众详情（本地数据库）
	 * @param model
	 * @param page
	 * @param lv_enc_id
	 * @param prof
	 * @return
	 */
	@RequestMapping(value = "liveRecordsList/{lv_enc_id}")
	@ResponseBody
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getLiveRecordsList(Model model, Page page,@PathVariable(value = "lv_enc_id") String lv_enc_id){
		
		long lvId = EncryptUtil.cwnDecrypt(lv_enc_id);
		
		liveRecordsService.getLiveOnlineUser(page, lvId);
					
		return JsonFormat.format(model, page);
	}
	
	/**
	 * 修改观看状态
	 * @param model
	 * @param page
	 * @param lvId
	 * @param prof
	 * @param status
	 * @return
	 */
	@RequestMapping(value = "updateRecordsStatus/{lv_enc_id}")
	@ResponseBody
	@SuppressWarnings("rawtypes")
	public String updateRecordsStatus(Model model, Page page,
			@PathVariable(value = "lv_enc_id") String lv_enc_id,loginProfile prof,
			@RequestParam(value="status", required = false,defaultValue="0") int status){
		
		long lvId = EncryptUtil.cwnDecrypt(lv_enc_id);
		
		//加入观看记录 状态1为正在观看 
		liveRecordsService.saveOrUpdate(lvId, prof.usr_ent_id, status);
		
		return JsonFormat.format(model, page);
	}
	
	/**
	 * 获取我的直播列表
	 * @param model
	 * @param page
	 * @param prof
	 * @return
	 * @throws MessageException 
	 */
	@RequestMapping(value = "getMyLiveList")
	@ResponseBody
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getMyLiveList(Model model, Page page,loginProfile prof,WizbiniLoader wizbini) throws MessageException{
		
		lvItemService.getMyLiveList(page, prof, wizbini);
		
		return JsonFormat.format(model, page);
	}
	
	/**
	 * 密码验证
	 * @param model
	 * @param page
	 * @param lv_enc_id
	 * @param password
	 * @return
	 */
	@RequestMapping(value="checkPwd")
	@ResponseBody
	public String checkPwd(Model model, Page<LiveItem> page,
			@RequestParam(value="lv_enc_id", required = false,defaultValue="0") String lv_enc_id,
			@RequestParam(value = "password", required = false, defaultValue="") String password){
		
		long lv_id = EncryptUtil.cwnDecrypt(lv_enc_id);
		
		model.addAttribute("status", lvItemService.checkLivePwd( lv_id, password.trim()));
		
		return JsonFormat.format(model, page);
	}
	
	/**
	 * 查询直播个数
	 * @param model
	 * @return
	 */
	@RequestMapping(value="getLiveCount")
	@ResponseBody
	public String checkPwd(Model model, Page<LiveItem> page, WizbiniLoader wizbini,loginProfile prof){
		
		int livecount = lvItemService.getCount(wizbini, prof);
		
		model.addAttribute("lv_count", livecount);
		
		return JsonFormat.format(model, page);
	}
	
	/**
	 * 展示互动回调接口（需要在展示互动管理后台设置接口URL,需要外网）
	 * ClassNo：代表直播 ID 或实时课堂 ID，与 conf ID 或 SDK ID 含义相同
		Operator：触发动作的用户，但目前当 Action=102、103、104、106 时，Operation=0
		Action ：触发的动作类型
				101—用户进入
						(注：两种情况下产生。直播中客户端进入，不论直播是否开启；直播开启后，用户从 web端成功加入到直播中，但直播未开启时加入不会产生状态码)
				102—会议创建
						（注：第一个客户端成功进入就会触发，不区分角色）
				103—直播开始
						（注：组织者点击开始直播按钮触发，或者会议设置为自动开启直播，组织者进入就会触发）
				104—暂停直播（上课）
						（注：组织者/老师点击暂停直播触发）
				105—停止直播（上课）
						（注：两种情况下产生。组织者/教师关闭直播；当组织者/教师关闭客户端时选择暂时离开后，直播/教室里没有任何的客户端超过 5 分钟，就会触发）
				106—录制件产生
						（注：组织者/教师点击停止录制按钮，或者在录制过程中直播关闭，就会触发）
				107—退出直播教室
						（注：用户关闭客户端或播放器，触发）
				110—用户异常离开
						（注：用户由于网络中断、程序死、死机或浏览器播放器异常关闭等，触发）
		Affected：动作所影响的用户,一般和 Operator 相同,目前不起作用
		totalusernum:会议总人数
	 */
	@RequestMapping(value="receiveLiveInfo",method=RequestMethod.GET)
	@ResponseBody
	@Anonymous
	@SuppressWarnings("rawtypes")
	public String receiveInfo(Model model, Page page, @RequestParam(value = "ClassNo", required = false, defaultValue="") String classNo,
			@RequestParam(value = "Operator", required = false, defaultValue="") String operator,
			@RequestParam(value = "Action", required = false, defaultValue="") String action,
			@RequestParam(value = "Affected", required = false, defaultValue="") String affected,
			@RequestParam(value = "totalusernum", required = false, defaultValue="") String totalusernum){
		
		lvItemService.updateLiveByClassNo(classNo, action, totalusernum);
		
		return JsonFormat.format(model, page);
	}
	
}
