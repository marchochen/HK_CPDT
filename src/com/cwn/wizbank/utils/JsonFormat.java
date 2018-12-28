package com.cwn.wizbank.utils;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.DefaultValueProcessorMatcher;

import org.springframework.ui.Model;

import com.cw.wizbank.util.JsonHelper;
import com.cw.wizbank.util.JsonPropertyFilter;
import com.cw.wizbank.util.WzbJsonValueProcessors;

public class JsonFormat {

	public static String format(Model model, Page<?> page) {
		model.addAttribute("rows", page.getResults());
		model.addAttribute("total", page.getTotalRecord());
		model.addAttribute("page", page.getPageNo());
		model.addAttribute("pageSize", page.getPageSize());
		return format(page.getParams(), model);
	}

	public static String format(Map<String,Object> param, Model model) {
		JSONObject jsonObject = JSONObject.fromObject(model, JsonFormat.getDefaultJsonconfig());
/*		String callback = "";
		if(param != null){
			callback = (String)param.get("callback");
		}
		if(StringUtils.isNotEmpty(callback)){
			return callback + "("+ jsonObject.toString() +")";
		} else {
			return jsonObject.toString();
		}*/
		return jsonObject.toString();

	}
	
	public static String format(Params param, Model model) {
		return format(param.getParams(), model);
	}

	public static String format(Model model) {
		return	format(new HashMap<String,Object>(), model);
	}

	public static JsonConfig getDefaultJsonconfig() {
		JsonConfig jsonConfig = new JsonConfig();
		return getDefaultJsonconfig(jsonConfig);
	}

	public static JsonConfig getDefaultJsonconfig(JsonConfig jsonConfig) {
		jsonConfig.registerJsonValueProcessor(Timestamp.class, new WzbJsonValueProcessors.DateJsonValueProcessor());
		jsonConfig.registerJsonValueProcessor(Date.class, new WzbJsonValueProcessors.DateJsonValueProcessor());
		jsonConfig.registerJsonValueProcessor(float.class, new WzbJsonValueProcessors.FloatJsonValueProcessor());
		jsonConfig.registerJsonValueProcessor(Float.class, new WzbJsonValueProcessors.FloatJsonValueProcessor());
		jsonConfig.registerJsonValueProcessor(String.class, new WzbJsonValueProcessors.DefaultEsc4HtmlStringProcessor());
		JsonHelper.disableEsc4Json(com.cw.wizbank.JsonMod.commonBean.GoldenManBean.class, "value", jsonConfig);
		JsonHelper.disableEsc4Json(com.cw.wizbank.JsonMod.commonBean.OptionBean.class, "text", jsonConfig);
		
		JsonHelper.disableEsc4Json(com.cwn.wizbank.entity.Module.class, "mod_web_launch", jsonConfig);
		
		jsonConfig.setDefaultValueProcessorMatcher(DefaultValueProcessorMatcher.DEFAULT);
		jsonConfig.setJsonPropertyFilter(new JsonPropertyFilter());
		return jsonConfig;
	}
	
	public static String toJsonDateFormat(Timestamp time) {
		if (time == null)
			return "";
		return time.toString();
		/*SimpleDateFormat format = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
		return format.format(time);*/
	}
	
	public static String format(Object obj) {
		JSONObject jsonObject = JSONObject.fromObject(obj, JsonFormat.getDefaultJsonconfig());
		return jsonObject.toString();

	}
	
}