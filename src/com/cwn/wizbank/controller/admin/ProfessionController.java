package com.cwn.wizbank.controller.admin;

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

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.entity.Profession;
import com.cwn.wizbank.entity.ProfessionLrnItem;
import com.cwn.wizbank.entity.UserGrade;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.security.annotation.HasPermission;
import com.cwn.wizbank.services.ProfessionLrnItemService;
import com.cwn.wizbank.services.ProfessionService;
import com.cwn.wizbank.utils.EncryptUtil;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.Page;

/**
 * 职级学习地图的controller
 * 
 * @author demo.zhao
 *
 */
@HasPermission(AclFunction.FTN_AMD_PROFESSION_MAIN)
@Controller("adminProfessionController")
@RequestMapping("admin/profession")
public class ProfessionController {

	@Autowired
	private ProfessionService professionService;
	@Autowired
	private ProfessionLrnItemService professionLrnItemService;
	/**
	 * 转发到列表首页
	 * 
	 * @return
	 */
	@HasPermission(AclFunction.FTN_AMD_PROFESSION_MAIN)
	@RequestMapping("")
	public String page() {
		return "admin/profession/index";
	}

	/**
	 * 获取json列表
	 * 
	 * @param page
	 * @param prof
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@HasPermission(AclFunction.FTN_AMD_PROFESSION_MAIN)
	@RequestMapping("pageJson")
	@ResponseBody
	public String page(Page<Profession> page,WizbiniLoader wizbini, loginProfile prof, Model model)
			throws ParseException {
		 if(wizbini.cfgSysSetupadv.isTcIndependent()){
	            page.getParams().put("top_tcr_id", prof.my_top_tc_id);
	            }
		professionService.get_pfs_page(page);
		return JsonFormat.format(model, page);
	}

	/**
	 * 转发到创建的页面
	 * 
	 * @return
	 */
	@HasPermission(AclFunction.FTN_AMD_PROFESSION_MAIN)
	@RequestMapping(method = RequestMethod.GET, value = "add")
	public String createProfessionPage(ModelMap map) {
		map.addAttribute("type", "add");
		return "admin/profession/add";
	}
	@HasPermission(AclFunction.FTN_AMD_PROFESSION_MAIN)
	@RequestMapping(method = RequestMethod.POST, value = "doadd")
	public String addprofession(Profession profession,String gid,String qid,loginProfile profile) {
			profession.setPfs_create_usr_id(profile.usr_ent_id);
			profession.setPfs_tcr_id(profile.my_top_tc_id);
			profession.setPfs_update_usr_id(profile.usr_ent_id);
			professionService.add(profession);
		long psi_pfs_id=profession.getPfs_id();
			
		String [] gids=gid.split(",");
		String [] qids=qid.split(",");
			for (int i = 0; i < qids.length; i++) {
				
				ProfessionLrnItem item=new ProfessionLrnItem();
			
			item.setPsi_ugr_id(gids[i]);
			item.setPsi_pfs_id(psi_pfs_id);
				String [] qidas=qids[i].split("~");
				for (String id : qidas) {
					item.setPsi_itm_id(id);
					professionLrnItemService.add(item);
				}
		}
		return "redirect:/app/admin/profession";
	}
	@HasPermission(AclFunction.FTN_AMD_PROFESSION_MAIN)
	@RequestMapping(method=RequestMethod.GET,value="updatePage")
	public String updatePage(Profession profession,Model model){
		if(!StringUtils.isEmpty(profession.getEncrypt_pfs_id())){
			Long pfs_id = EncryptUtil.cwnDecrypt(profession.getEncrypt_pfs_id());
			profession.setPfs_id(pfs_id);
		}
		
		model.addAttribute("type", "update");
		
		 profession = professionService.get(profession.getPfs_id());
		  
		 List<UserGrade> gradeList= professionLrnItemService.getItemByGradeId(profession.getPfs_id());
		 profession.setGradeList(gradeList);
		 model.addAttribute("profession", profession);
		return "admin/profession/add";
	}
	@HasPermission(AclFunction.FTN_AMD_PROFESSION_MAIN)
	@RequestMapping(method = RequestMethod.POST, value = "update")
	public String update(Profession profession,String gid,String qid,loginProfile profile) {
		Profession profess=professionService.get(profession.getPfs_id());
		profess.setPfs_update_usr_id(profile.usr_ent_id);
		profess.setPfs_title(profession.getPfs_title());
			professionService.update(profess);
			professionLrnItemService.deleteByPfsId(profession.getPfs_id());
			String [] gids=gid.split(",");
			String [] qids=qid.split(",");
				for (int i = 0; i < gids.length; i++) {
					
					ProfessionLrnItem item=new ProfessionLrnItem();
				
				item.setPsi_ugr_id(gids[i]);
				item.setPsi_pfs_id(profession.getPfs_id());
					String [] qidas=qids[i].split("~");
					for (String id : qidas) {
						item.setPsi_itm_id(id);
						professionLrnItemService.add(item);
					}
			}
		return "redirect:/app/admin/profession";
	}
	@HasPermission(AclFunction.FTN_AMD_PROFESSION_MAIN)
	@RequestMapping(method = RequestMethod.GET, value = "delete")
	public String delete(Profession profession) {
		if(!StringUtils.isEmpty(profession.getEncrypt_pfs_id())){
			Long pfs_id = EncryptUtil.cwnDecrypt(profession.getEncrypt_pfs_id());
			profession.setPfs_id(pfs_id);
		}
		professionService.deleteProfession(profession.getPfs_id());
		return "redirect:/app/admin/profession";
	}
	@HasPermission(AclFunction.FTN_AMD_PROFESSION_MAIN)
	@RequestMapping(method = RequestMethod.GET, value = "cancelPublished")
	public String cancelPublished(Profession profession) {
		if(!StringUtils.isEmpty(profession.getEncrypt_pfs_id())){
			Long pfs_id = EncryptUtil.cwnDecrypt(profession.getEncrypt_pfs_id());
			profession.setPfs_id(pfs_id);
		}
		professionService.publishAndCancel(profession.getPfs_id(),0);
		return "redirect:/app/admin/profession";
	}
	@HasPermission(AclFunction.FTN_AMD_PROFESSION_MAIN)
	@RequestMapping(method = RequestMethod.GET, value = "published")
	public String published(Profession profession) {
		if(!StringUtils.isEmpty(profession.getEncrypt_pfs_id())){
			Long pfs_id = EncryptUtil.cwnDecrypt(profession.getEncrypt_pfs_id());
			profession.setPfs_id(pfs_id);
		}
		professionService.publishAndCancel(profession.getPfs_id(),1);
		return "redirect:/app/admin/profession";
	}
	@HasPermission(AclFunction.FTN_AMD_PROFESSION_MAIN)
	@RequestMapping("checkExistTitle")
	@ResponseBody
	public String checkExistTitle(Model model,WizbiniLoader wizbini, loginProfile prof,
			@RequestParam(value = "pfs_title", required = false, defaultValue = "") String pfs_title,@RequestParam(value = "old_value", required = false, defaultValue = "") String old_value) {
		if(professionService.isExistTitle(pfs_title, old_value,wizbini.cfgSysSetupadv.isTcIndependent(),prof.my_top_tc_id  )){
			model.addAttribute("stauts", 1);
		}else{
			model.addAttribute("stauts", 0);
		}
		return JsonFormat.format(model);
	}
	@HasPermission(AclFunction.FTN_AMD_PROFESSION_MAIN)
	@RequestMapping(method = RequestMethod.POST, value = "batDelete")
	public String batDelete(@RequestParam(value = "ids", required = false, defaultValue = "") String ids) {
		professionService.batDeleteProfession(ids);
		return "redirect:/app/admin/profession";
	}
	@HasPermission(AclFunction.FTN_AMD_PROFESSION_MAIN)
	@RequestMapping(method = RequestMethod.POST, value = "batPublish")
	public String batPublished(@RequestParam(value = "ids", required = false, defaultValue = "") String ids, loginProfile profile) {
		professionService.batPublishAndCancel(ids,1);
		return "redirect:/app/admin/profession";
	}
	@HasPermission(AclFunction.FTN_AMD_PROFESSION_MAIN)
	@RequestMapping(method = RequestMethod.POST, value = "batCancelPublish")
	public String batCancelPublish(@RequestParam(value = "ids", required = false, defaultValue = "") String ids,loginProfile profile) {
		professionService.batPublishAndCancel(ids, 0);
		return "redirect:/app/admin/profession";
	}
	/**
	 *  查询受影响职级发展(职级删除)
	 */
	@HasPermission(AclFunction.FTN_AMD_GRADE_MAIN)
	@RequestMapping(method = RequestMethod.POST, value = "getAffectedPfs")
	@ResponseBody
	public String getAffectedPfs(Model model, loginProfile prof,@RequestParam(value = "id", required = false, defaultValue = "") long id) {
		model.addAttribute("flag", professionService.getAffectedPfs(id));
	return JsonFormat.format(model);
	}
}

