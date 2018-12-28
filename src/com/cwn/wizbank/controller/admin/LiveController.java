package com.cwn.wizbank.controller.admin;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.entity.LiveItem;
import com.cwn.wizbank.exception.EncryptException;
import com.cwn.wizbank.exception.ErrorException;
import com.cwn.wizbank.exception.MessageException;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.security.annotation.HasPermission;
import com.cwn.wizbank.services.EnterpriseInfoPortalService;
import com.cwn.wizbank.services.LiveQcloudService;
import com.cwn.wizbank.services.LiveItemService;
import com.cwn.wizbank.services.LiveRecordsService;
import com.cwn.wizbank.services.TcTrainingCenterService;
import com.cwn.wizbank.utils.EncryptUtil;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.Page;

/**
 * 直播管理
 * @author bill
 */
@Controller("adminLiveController")
@RequestMapping("admin/live")
@HasPermission(value = {AclFunction.FTN_AMD_LIVE_MAIN})
public class LiveController {
	
	@Autowired
	LiveItemService liveItemService;
	
	@Autowired
	LiveRecordsService liveRecordsService;
	
	@Autowired
	private TcTrainingCenterService tcTrainingCenterService;
	
	@Autowired
	EnterpriseInfoPortalService enterpriseInfoPortalService;
	
	@Autowired
	LiveQcloudService liveItemQcloudService;
	
	/**
	 *  跳转到直播管理列表
	 * @param mav
	 * @param prof
	 * @param wizbini
	 * @return
	 */
	@RequestMapping(value = "list")
	public ModelAndView index(ModelAndView mav,loginProfile prof,WizbiniLoader wizbini){
		mav = new ModelAndView("admin/live/list");
		mav.addObject("liveAuth", liveItemService.getLiveModeAuth(prof,wizbini));
		return mav;
	}
	
	/**
	 * 跳转到添加直播
	 * @param mav
	 * @param mode_type
	 * @return
	 * @throws MessageException
	 */
	@RequestMapping(value = "insert")
	public ModelAndView insert(ModelAndView mav,loginProfile prof,WizbiniLoader wizbini,
			@RequestParam(value = "mode_type",defaultValue="") String mode_type) throws MessageException {
		if(!LiveItem.LIVE_MODE_TYPE_VHALL.equalsIgnoreCase(mode_type) && !LiveItem.LIVE_MODE_TYPE_QCLOUD.equalsIgnoreCase(mode_type) && !LiveItem.LIVE_MODE_TYPE_GENSEE.equalsIgnoreCase(mode_type)){
			throw new MessageException("label_core_live_management_54");
		}
		Map<String, Object> map = liveItemService.getLiveModeAuth(prof, wizbini);
		Object obj = map.get(mode_type.toLowerCase()+"LiveAuth");
		if(obj != null && !Boolean.parseBoolean(obj.toString())){
			throw new MessageException("label_core_live_management_54");
		}
		
		mav = new ModelAndView("admin/live/insert");
		mav.addObject("live_mode_type", mode_type);
		mav.addObject("type","add");
		return mav;
	}
	
	/**
	 * 跳转到修改直播，与创建直播是相同页面
	 * @param mav
	 * @param lv_enc_id
	 * @return
	 */
	@RequestMapping(value = "update/{lv_enc_id}")
	public ModelAndView update(ModelAndView mav,@PathVariable(value = "lv_enc_id") String lv_enc_id){
		mav = new ModelAndView("admin/live/insert");
		
		long lvId = EncryptUtil.cwnDecrypt(lv_enc_id);
		
		LiveItem lvItem = liveItemService.getLiveItemById(lvId);
		mav.addObject("tcTrainingCenter", tcTrainingCenterService.get(lvItem.getLv_tcr_id()));		
		mav.addObject("lv", lvItem);
		mav.addObject("type","update");
		return mav;
	}
	
	/**
	 * 获取直播列表json
	 * @param model
	 * @param prof
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
			@RequestParam(value = "isMobile", required = false, defaultValue = "false") boolean isMobile,
			@RequestParam(value = "live_mode_type", required = false, defaultValue = "") String live_mode_type) throws MessageException{
		
		liveItemService.getLvItemList(page, wizbini, prof, searchContent, status, isMobile, false,live_mode_type);
		
		return JsonFormat.format(model, page);
	}
	
	/**
	 * 添加直播
	 * @param mav
	 * @param liveItem
	 * @param wizbini
	 * @param prof
	 * @param request
	 * @param image
	 * @return
	 * @throws ErrorException 
	 * @throws IOException 
	 * @throws IllegalStateException 
	 * @throws MessageException 
	 * @throws Exception
	 */
	@RequestMapping(value="addlive",method = RequestMethod.POST)
	public ModelAndView addlive(ModelAndView mav, LiveItem liveItem,WizbiniLoader wizbini, loginProfile prof,
			@RequestParam(value="image", required = false) MultipartFile image, @RequestParam(value = "imgurl", required = false) String imgurl,
			@RequestParam(value = "image_radio", required = false) int image_radio) throws IllegalStateException, IOException, ErrorException, MessageException{
		mav = new ModelAndView("redirect:/app/admin/live/list?mode_type="+liveItem.getLv_mode_type());
		
		liveItemService.createLive(liveItem,wizbini,prof,image,imgurl,image_radio);

		return mav;
	}
	
	/**
	 * 修改直播
	 * @param mav
	 * @param liveItem
	 * @param wizbini
	 * @param prof
	 * @param lv_enc_id
	 * @param image
	 * @param imgurl
	 * @param image_radio
	 * @return
	 * @throws ErrorException
	 * @throws IllegalStateException
	 * @throws IOException
	 * @throws MessageException
	 */
	@RequestMapping(value="updatelive/{lv_enc_id}",method = RequestMethod.POST)
	public ModelAndView updatelive(ModelAndView mav, LiveItem liveItem,WizbiniLoader wizbini, loginProfile prof,
			@PathVariable(value="lv_enc_id") String lv_enc_id,@RequestParam(value="image", required = false) MultipartFile image, 
			@RequestParam(value = "imgurl", required = false) String imgurl,
			@RequestParam(value = "image_radio", required = false) int image_radio) throws ErrorException, IllegalStateException, IOException, MessageException {
		mav = new ModelAndView("redirect:/app/admin/live/list?mode_type="+liveItem.getLv_mode_type());
		
		liveItemService.updateLive(liveItem, EncryptUtil.cwnDecrypt(lv_enc_id), wizbini, prof, image, imgurl, image_radio);
		
		return mav;
	}
	
	/**
	 * 删除直播
	 * @param mav
	 * @param lv_enc_id
	 * @return
	 * @throws ErrorException
	 * @throws MessageException 
	 * @throws EncryptException 
	 */
	@RequestMapping(value = "delete/{lv_enc_id}",method=RequestMethod.POST)
	@ResponseBody
	public String delete(Model model,Page<LiveItem> page, @PathVariable(value="lv_enc_id") String lv_enc_id,loginProfile prof) throws ErrorException, EncryptException, MessageException{
				
		liveItemService.deleteLive(EncryptUtil.cwnDecrypt(lv_enc_id),prof);
		
		model.addAttribute("status", "success");
		
		return JsonFormat.format(model, page);
	}
	
	/**
	 * 跳转到发起直播页面
	 * @param mav
	 * @param request
	 * @param lv_enc_id
	 * @param prof
	 * @return
	 * @throws ErrorException
	 * @throws MessageException 
	 * @throws  
	 */
	@RequestMapping(value = "detail/host/{lv_enc_id}")
	public ModelAndView getStartLive(ModelAndView mav,loginProfile prof,@PathVariable(value = "lv_enc_id") String lv_enc_id) throws ErrorException, MessageException{
		mav = new ModelAndView("admin/live/detail");
		
		mav.addObject("lv", liveItemService.startLive(EncryptUtil.cwnDecrypt(lv_enc_id), prof));
		mav.addObject("prof", prof);
		
		return mav;
	}
	
	/**
	 * 开始直播
	 * @param mav
	 * @param request
	 * @param lv_enc_id
	 * @param prof
	 * @return
	 * @throws ErrorException
	 * @throws MessageException 
	 * @throws  
	 */
	@RequestMapping(value = "startLive/{lv_enc_id}")
	@ResponseBody
	public String startLive(Model model,loginProfile prof,Page<LiveItem> page,@PathVariable(value = "lv_enc_id") String lv_enc_id) throws ErrorException, MessageException{
		
		liveItemService.startLive(EncryptUtil.cwnDecrypt(lv_enc_id), prof);
		
		model.addAttribute("status", "success");
		
		return JsonFormat.format(model, page);
	}
	
	/**
	 * 结束直播
	 * @param mav
	 * @param lv_enc_id
	 * @return
	 * @throws ErrorException
	 * @throws MessageException 
	 * @throws EncryptException 
	 */
	@RequestMapping(value = "endlive/{lv_enc_id}")
	public ModelAndView endLive(ModelAndView mav, @PathVariable(value="lv_enc_id") String lv_enc_id, loginProfile prof) throws ErrorException, EncryptException, MessageException{
		LiveItem liveItem = liveItemService.stopLive(EncryptUtil.cwnDecrypt(lv_enc_id),prof);
		
		return new ModelAndView("redirect:/app/admin/live/list?mode_type="+liveItem.getLv_mode_type());
	}
	
	/**
	 * 查看回放
	 * @param mav
	 * @param lv_enc_id
	 * @param prof
	 * @return
	 */
	@RequestMapping(value = "view/{lv_enc_id}")
	public ModelAndView getLiveView(ModelAndView mav,@PathVariable(value = "lv_enc_id") String lv_enc_id, loginProfile prof){
		mav = new ModelAndView("admin/live/view");
		
		long lvId = EncryptUtil.cwnDecrypt(lv_enc_id);
		
		mav.addObject("lv", liveItemService.getLiveItemById(lvId,prof));
		mav.addObject("prof", prof);
		
		return mav;
	}	
	
	/**
	 * 密码验证
	 * @param model
	 * @param page
	 * @param lv_enc_id
	 * @param password
	 * @return
	 */
	@RequestMapping(value="checkPwd",method=RequestMethod.POST)
	@ResponseBody
	public String checkPwd(Model model, Page<LiveItem> page,
			@RequestParam(value="lv_enc_id", required = false,defaultValue="0") String lv_enc_id,
			@RequestParam(value = "password", required = false, defaultValue="") String password){
		
		long lv_id = EncryptUtil.cwnDecrypt(lv_enc_id);
		
		model.addAttribute("status", liveItemService.checkLivePwd( lv_id, password.trim()));
		
		return JsonFormat.format(model, page);
	}
	
	/**
	 * 发布与取消发布
	 * @param mav
	 * @param lv_enc_id
	 * @param type
	 * @return
	 */
	@RequestMapping(value="{type}/{lv_enc_id}")
	public ModelAndView publish(ModelAndView mav,loginProfile prof,
			@PathVariable(value="lv_enc_id") String lv_enc_id, 
			@PathVariable(value="type") String type){
		
		long lvId = EncryptUtil.cwnDecrypt(lv_enc_id);
		
		LiveItem liveItem = liveItemService.updateStatus(lvId, type, prof);
		
		return new ModelAndView("redirect:/app/admin/live/list?mode_type="+liveItem.getLv_mode_type());
	}
	
	/**
	 * 过滤时间
	 * @param binder
	 */
	@InitBinder   
    public void initBinder(WebDataBinder binder) {   
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   
        dateFormat.setLenient(true);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));   
    }
	
}
