package com.cwn.wizbank.idv;


import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cw.wizbank.Application;
import com.cw.wizbank.config.WizbiniLoader;
import com.cwn.wizbank.security.annotation.Anonymous;
import com.cwn.wizbank.utils.IdocViewUtils;


@RequestMapping("idv")
@Controller
public class IdocViewController {
	
	/**
	 * 在线预览
	 * @param wizbini
	 * @param request
	 * @param filePath  原文件路径
	 * @return 打开预览文件地址
	 */
	@RequestMapping(value="preview",method = RequestMethod.GET)
	public String preview(WizbiniLoader wizbini, HttpServletRequest request,@RequestParam (value="filePath") String filePath ){
		
		String path = IdocViewUtils.getViewPathForWeb(Application.I_DOC_VIEW_PREVIEW_HOST+filePath);
		return "redirect:"+path;
	}
	
	/**
	 * 在线预览
	 * @param wizbini
	 * @param request
	 * @param filePath  原文件路径
	 * @return 打开预览文件地址
	 */
	@RequestMapping(value="previewUrl",method = RequestMethod.GET)
	@Anonymous
	@ResponseBody
	public String previewUrl(WizbiniLoader wizbini, HttpServletRequest request,@RequestParam (value="filePath") String filePath ){
		
		String path = IdocViewUtils.getViewPath(Application.I_DOC_VIEW_PREVIEW_HOST+filePath);
		return path;
	}

}
