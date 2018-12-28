/**
 * 
 */
package com.cwn.wizbank.controller;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cwn.wizbank.entity.SnsDoing;
import com.cwn.wizbank.exception.BeanAssignException;
import com.cwn.wizbank.security.annotation.Anonymous;
import com.cwn.wizbank.services.SnsDoingService;
import com.cwn.wizbank.utils.BeanUtils;
import com.cwn.wizbank.utils.Page;
import com.cwn.wizbank.utils.RequestStatus;

/**
 * @author leon.li
 * 2014-7-30 上午10:59:28
 */
@Controller
@RequestMapping("demo")
@Anonymous
public class DemoController {

	@Autowired
	SnsDoingService snsDoingService;

	@Anonymous
	@RequestMapping("list")
	public String page(Model model, Page<SnsDoing> page) throws SQLException {
		model.addAttribute("page", snsDoingService.page(page));
		return "demo/list";
	}

	@RequestMapping(value = "add", method = RequestMethod.GET)
	public ModelAndView add(@RequestParam(value = "id", required = false, defaultValue = "0") int id) {
		SnsDoing snsDoing = null;
		String operation = RequestStatus.ADD;
		if (id > 0) {
			operation = RequestStatus.UPDATE;
			snsDoing = snsDoingService.get(id);
		} else {
			snsDoing = new SnsDoing();
		}
		ModelAndView mav = new ModelAndView("demo/edit", "snsDoing", snsDoing);
		mav.addObject(RequestStatus.STATUS, operation);
		return mav;
	}

	@RequestMapping(value = "add", method = RequestMethod.POST)
	public String add(@ModelAttribute("snsDoing") SnsDoing snsDoing, RedirectAttributes attributes) throws BeanAssignException, IllegalAccessException, InvocationTargetException {
		//验证
		if (StringUtils.isEmpty(snsDoing.getS_doi_title())) {
			attributes.addFlashAttribute(RequestStatus.FLASH_MSG, "标题不能为空");
			attributes.addFlashAttribute(RequestStatus.TO_FOCUS, "$(name=[s_doi_title])");
			return "redirect:add";
		}
		//前端传过来的id如果存在，则是修改操作，否则是新增，所有前端数据已经绑定到了 snsDoing 中
		if (snsDoing.getS_doi_id() != null && snsDoing.getS_doi_id() > 0) {
			SnsDoing dsnsDoing = null;
			//获取当前存在的对象
			dsnsDoing = snsDoingService.get(snsDoing.getS_doi_id());
			if (dsnsDoing != null) {
				//更新操作
				BeanUtils.copyProperties(snsDoing, dsnsDoing);
				snsDoingService.update(dsnsDoing);
			} else {
				//对象不存在
				attributes.addFlashAttribute(RequestStatus.FLASH_MSG, "对象目标不存在");
				return "demo/edit";
			}
		} else {
			//新增
			snsDoingService.add(snsDoing);
		}
		return "redirect:list";
	}

	@RequestMapping(value = "delete/{id}")
	public String remove(@PathVariable int id) {
		snsDoingService.delete(id);
		return "redirect:/app/demo/list";
	}

	// 选择框测试
	@RequestMapping("test/selector")
	public String test() {
		return "demo/selector";
	}

	@RequestMapping(value = "test/selector/form", method = RequestMethod.GET)
	public String form() {
		return "demo/selector_form";
	}

	@RequestMapping(value = "test/selector/post", method = RequestMethod.POST)
	public String post() {
		return "demo/selector";
	}

	// 文件上传框
	@RequestMapping(value = "test/upload", method = RequestMethod.GET)
	public String upload() {
		return "demo/upload";
	}

	@RequestMapping(value = "test/upload", method = RequestMethod.POST)
	public String upload(Model model) {
		return "demo/upload";
	}
}
