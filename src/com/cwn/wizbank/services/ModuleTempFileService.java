package com.cwn.wizbank.services;


import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.cw.wizbank.config.WizbiniLoader;
import com.cwn.wizbank.entity.ModuleTempFile;
import com.cwn.wizbank.entity.vo.AttachmentVo;
import com.cwn.wizbank.persistence.ModuleTempFileMapper;
import com.cwn.wizbank.utils.ContextPath;
import com.cwn.wizbank.utils.FileUtils;
import com.cwn.wizbank.utils.RequestStatus;
/**
 *  service 实现
 */
@Service
public class ModuleTempFileService extends BaseService<ModuleTempFile> {

	@Autowired
	ModuleTempFileMapper moduleTempFileMapper;

	
	/**
	 * 上传文件
	 * @param usrEntId
	 * @param wizbini
	 * @param module
	 * @param file
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public void upload(Model model, long usrEntId, WizbiniLoader wizbini, String module, MultipartFile file, String url, String type) throws IllegalStateException, IOException {
		ModuleTempFile mtf = null;
		
		if(file != null){
			mtf = new ModuleTempFile();
			mtf.setMtf_create_time(getDate());
			mtf.setMtf_file_name(file.getOriginalFilename());
			mtf.setMtf_file_size(file.getSize());
			mtf.setMtf_module(module);
			mtf.setMtf_target_id(0l);
			mtf.setMtf_type(type);
			mtf.setMtf_usr_id(usrEntId);
			//mtf.setTmf_file_type(file.getContentType());
			
			String filename = file.getOriginalFilename();
			String ftype = filename.substring(filename.lastIndexOf(".") + 1,
					filename.length());
			
			mtf.setMtf_file_type(ftype);
			//重命名
			mtf.setMtf_file_rename((new Date().getTime()) + "." + ftype);
			
			String basePath = wizbini.getFileEditorDirAbs();
			String dirPath = File.separator + FileUtils.SNS_TEMP_DIR + File.separator + module + File.separator + usrEntId;
			File dir = new File(basePath + dirPath);
			if(!dir.exists()){
				dir.mkdirs();
			}
			File renamefile = new File(basePath + dirPath, mtf.getMtf_file_rename());
			if(!renamefile.exists()){
				renamefile.createNewFile();
			}
			file.transferTo(renamefile);
			
			String urlPath = wizbini.cfgSysSetupadv.getFileUpload()
					.getEditorDir().getUrl();
			
			model.addAttribute("path");
			model.addAttribute("fullPath", urlPath + dirPath + '/' + mtf.getMtf_file_rename());
			
		} else {
			mtf = new ModuleTempFile();
			mtf.setMtf_create_time(getDate());
			mtf.setMtf_module(module);
			//mtf.setMtf_file_name(url);
			mtf.setMtf_type(type);
			mtf.setMtf_target_id(0l);
			mtf.setMtf_usr_id(usrEntId);
			mtf.setMtf_file_type("url");
			mtf.setMtf_url(url);
		}
		
		if(mtf != null){
			this.add(mtf);
			model.addAttribute("tmf", mtf);
		}
		model.addAttribute(RequestStatus.STATUS, RequestStatus.SUCCESS);
	}
	
	public List<ModuleTempFile> getNotMasterList(long userEntId, String module) {
		return getList(userEntId, module, 0);
	}
	
	public List<AttachmentVo> getUncommintList(long userEntId, String module, WizbiniLoader wizbini) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userEntId", userEntId);
		map.put("module", module);
		map.put("targetId", 0);
		List<AttachmentVo> list =  moduleTempFileMapper.getAttachmentList(map);
		if(list != null && !list.isEmpty()) {
			String urlPath = wizbini.cfgSysSetupadv.getFileUpload().getEditorDir().getUrl();
			String dirPath = File.separator + FileUtils.SNS_TEMP_DIR + File.separator + module + File.separator + userEntId;
			for(AttachmentVo att : list) {
				if("url".equals(att.getFile_type())){
					att.setUrl(att.getUrl());
					att.setFile_name(att.getUrl());
				} else {
					att.setUrl(ContextPath.getContextPath() + "/" + urlPath + dirPath + '/' + att.getFile_rename());
				}
			}
		}
		
		return list;
	}	

	public List<ModuleTempFile> getList(long userEntId, String module, long targetId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userEntId", userEntId);
		map.put("module", module);
		map.put("targetId", targetId);
		return moduleTempFileMapper.getList(map);
	}
	
	public void deleteList(long userEntId, String module, long targetId){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userEntId", userEntId);
		map.put("module", module);
		map.put("targetId", targetId);
		moduleTempFileMapper.deleteList(map);
		
	}

	public void setMaster(long targetId, long uId, String moduleDoing) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userEntId", uId);
		map.put("module", moduleDoing);
		map.put("targetId", targetId);
		moduleTempFileMapper.updateList(map);
		
	}

	public ModuleTempFileMapper getModuleTempFileMapper() {
		return moduleTempFileMapper;
	}

	public void setModuleTempFileMapper(ModuleTempFileMapper moduleTempFileMapper) {
		this.moduleTempFileMapper = moduleTempFileMapper;
	}
	
	
	/**
	 * 上传文件
	 * @param usrEntId
	 * @param wizbini
	 * @param module
	 * @param file
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public AttachmentVo upload(long userEntId, WizbiniLoader wizbini, String module, MultipartFile file, String url, String type) throws IllegalStateException, IOException {
		ModuleTempFile mtf = null;
		AttachmentVo atch = new AttachmentVo();

		if(file != null){
			mtf = new ModuleTempFile();
			mtf.setMtf_create_time(getDate());
			mtf.setMtf_file_name(file.getOriginalFilename());
			mtf.setMtf_file_size(file.getSize());
			mtf.setMtf_module(module);
			mtf.setMtf_target_id(0l);
			mtf.setMtf_type(type);
			mtf.setMtf_usr_id(userEntId);
			//mtf.setTmf_file_type(file.getContentType());
			
			String filename = file.getOriginalFilename();
			String ftype = filename.substring(filename.lastIndexOf(".") + 1,
					filename.length());
			
			mtf.setMtf_file_type(ftype);
			//重命名
			mtf.setMtf_file_rename((new Date().getTime()) + "." + ftype);
			
			String basePath = wizbini.getFileEditorDirAbs();
			String dirPath = File.separator + FileUtils.SNS_TEMP_DIR + File.separator + module + File.separator + userEntId;
			File dir = new File(basePath + dirPath);
			if(!dir.exists()){
				dir.mkdirs();
			}
			File renamefile = new File(basePath + dirPath, mtf.getMtf_file_rename());
			if(!renamefile.exists()){
				renamefile.createNewFile();
			}
			file.transferTo(renamefile);
			
			String urlPath = wizbini.cfgSysSetupadv.getFileUpload().getEditorDir().getUrl();
			
			atch.setUrl(ContextPath.getContextPath() + "/" + urlPath + dirPath + '/' + mtf.getMtf_file_rename());
			atch.setFile_name(mtf.getMtf_file_name());
		} else {
			mtf = new ModuleTempFile();
			mtf.setMtf_create_time(getDate());
			mtf.setMtf_module(module);
			mtf.setMtf_type(type);
			mtf.setMtf_target_id(0l);
			mtf.setMtf_usr_id(userEntId);
			mtf.setMtf_file_type("url");
			mtf.setMtf_url(url);
			
			atch.setUrl(url);
			atch.setFile_name(url);
		}
		
		if(mtf != null){
			this.add(mtf);
		}
		atch.setType(mtf.getMtf_type());
		atch.setUsr_id(mtf.getMtf_usr_id());
		atch.setFile_size(mtf.getMtf_file_size());
		atch.setId(mtf.getMtf_id());
		return atch;
	}
	
	
}