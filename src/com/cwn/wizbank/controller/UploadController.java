/**
 * 
 */
package com.cwn.wizbank.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.cw.wizbank.Application;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.entity.vo.AttachmentVo;
import com.cwn.wizbank.exception.UploadException;
import com.cwn.wizbank.services.ModuleTempFileService;
import com.cwn.wizbank.utils.FileUtils;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.Params;
import com.cwn.wizbank.utils.RequestStatus;

/**
 * @author leon.li
 *
 */
@Controller
@RequestMapping("upload")
public class UploadController {
	
	@Autowired
	ModuleTempFileService moduleTempFileService;
	
	/**
	 * 先把文件上传上来，然后等提交的时候，更新这些数据的所属id
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	@RequestMapping("{module}/{type}")
	@ResponseBody
	public String upload(loginProfile prof,
			WizbiniLoader wizbini,
			Model model,
			Params params,
			MultipartFile file,
			@RequestParam(value="url", required=false, defaultValue="") String url,
			@PathVariable(value="module") String module,
			@PathVariable(value="type") String type) throws IllegalStateException, IOException{
		
		moduleTempFileService.upload(model, prof.usr_ent_id, wizbini, module, file, url, type);
		
		return JsonFormat.format(params, model);
	}
	
	@RequestMapping("/mobile/{module}/{type}")
	@ResponseBody
	public String upload(loginProfile prof, WizbiniLoader wizbini, Model model, Params params,
			@RequestParam(value="file", required=false )MultipartFile file,			
			@PathVariable(value="module") String module,
			@PathVariable(value="type") String type) throws IllegalStateException, IOException{	
		moduleTempFileService.upload(model, prof.usr_ent_id, wizbini, module, file, "", type);		
		return JsonFormat.format(params, model);
	}	
	
	@RequestMapping("{module}/{type}/online")
	@ResponseBody
	public String upload(loginProfile prof,
			WizbiniLoader wizbini,
			Model model,
			Params params,
			@RequestParam(value="url", required=false, defaultValue="") String url,
			@PathVariable(value="module") String module,
			@PathVariable(value="type") String type) throws IllegalStateException, IOException{
		
		moduleTempFileService.upload(model, prof.usr_ent_id, wizbini, module, null, url, type);
		
		return JsonFormat.format(params, model);
	}
	
	
	@RequestMapping("{module}/noMaster")
	@ResponseBody
	public String getUpload(Model model,
			WizbiniLoader wizbini,
			loginProfile prof,
			Params params,
			@PathVariable(value="module") String module
			){
		String urlPath = wizbini.cfgSysSetupadv.getFileUpload().getEditorDir().getUrl();
		String dirPath = "/" + FileUtils.SNS_TEMP_DIR + "/" + module + "/" + prof.usr_ent_id;

		model.addAttribute("path", urlPath + dirPath);
		model.addAttribute("mtfList", moduleTempFileService.getNotMasterList(prof.usr_ent_id, module));
		return JsonFormat.format(params, model);
	}
	
	@RequestMapping("del/{id}")
	@ResponseBody
	public String del(Model model,
			WizbiniLoader wizbini,
			loginProfile prof,
			Params params,
			@PathVariable(value="id") long id
			){
		moduleTempFileService.delete(id);
		return JsonFormat.format(params, model);
	}
	
	@RequestMapping("delete/{module}")
	@ResponseBody
	public String delList(Model model,
			WizbiniLoader wizbini,
			loginProfile prof,
			Params params,
			@PathVariable(value="module") String module
			){
		moduleTempFileService.deleteList(prof.usr_ent_id, module, 0);
		model.addAttribute(RequestStatus.STATUS, RequestStatus.SUCCESS);
		return JsonFormat.format(params, model);
	}
	
	/**
	 * 先把文件上传上来，然后等提交的时候，更新这些数据的所属id
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	@RequestMapping(value = "handle", method= RequestMethod.POST )
	@ResponseBody
	public String upload(loginProfile prof, WizbiniLoader wizbini, Model model,
			MultipartFile file,
			@RequestParam(value="module", required=true) String module,
			@RequestParam(value="type", required=true) String type) throws IllegalStateException, IOException{
		AttachmentVo atta = moduleTempFileService.upload(prof.usr_ent_id, wizbini, module, file, null, type);
		model.addAttribute("attachment", atta);
		model.addAttribute(RequestStatus.STATUS, RequestStatus.SUCCESS);
		return JsonFormat.format(model);
	}
	
	@RequestMapping("online")
	@ResponseBody
	public String uploadOnline(loginProfile prof,
			WizbiniLoader wizbini,
			Model model,
			@RequestParam(value="url", required=false, defaultValue="") String url,
			@RequestParam(value="module", required=true) String module,
			@RequestParam(value="type", required=true) String type) throws IllegalStateException, IOException{
		AttachmentVo atta = moduleTempFileService.upload(prof.usr_ent_id, wizbini, module, null, url, type);
		model.addAttribute("attachment", atta);
		model.addAttribute(RequestStatus.STATUS, RequestStatus.SUCCESS);
		return JsonFormat.format(model);
	}
	
	@RequestMapping("{module}/uncommit")
	@ResponseBody
	public String getUploadFiles(Model model,
			WizbiniLoader wizbini,
			loginProfile prof,
			Params params,
			@PathVariable(value="module") String module
			){
		model.addAttribute("attachments", moduleTempFileService.getUncommintList(prof.usr_ent_id, module, wizbini));
		return JsonFormat.format(params, model);
	}
	
	@RequestMapping("forbidden")
	public ModelAndView forbidden(){
		ModelAndView mv = new ModelAndView("/error/uploadError");
		mv.addObject("exception", new UploadException("error_upload_forbidden"));
		mv.addObject("forbidden",Application.UPLOAD_FORBIDDEN);
		return mv;
	}
	
	
}
