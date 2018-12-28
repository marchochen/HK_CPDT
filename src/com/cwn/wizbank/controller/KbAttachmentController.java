package com.cwn.wizbank.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.services.KbAttachmentService;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.Params;
import com.cwn.wizbank.utils.RequestStatus;

@Controller
@RequestMapping("kb/attachment")
public class KbAttachmentController {
	@Autowired
	KbAttachmentService kbAttachmentService;

	@RequestMapping("upload")
	@ResponseBody
	public Map<String, Object> upload(loginProfile prof, WizbiniLoader wizbini, HttpServletRequest request, Model model, Params params, MultipartFile file) throws IllegalStateException, IOException {
		return kbAttachmentService.upload(model, wizbini, prof, file, request);
	}
	
	@RequestMapping("uploadMobile")
	@ResponseBody
	public String uploadMobile(loginProfile prof, WizbiniLoader wizbini, HttpServletRequest request, Model model, Params params, MultipartFile file) throws IllegalStateException, IOException {
		model.addAttribute("result", kbAttachmentService.upload(model, wizbini, prof, file, request));
		return JsonFormat.format(params, model);
	}
	@RequestMapping("delete/{id}")
	@ResponseBody
	public String delete(Model model, loginProfile prof, WizbiniLoader wizbini, Params params,
			@PathVariable(value = "id") long id){
		kbAttachmentService.deleteAttachmentWithFile(wizbini, id);
		model.addAttribute(RequestStatus.STATUS, RequestStatus.SUCCESS);
		return JsonFormat.format(params, model);
	}
}
