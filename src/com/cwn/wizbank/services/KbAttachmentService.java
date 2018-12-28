package com.cwn.wizbank.services;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.entity.KbAttachment;
import com.cwn.wizbank.persistence.KbAttachmentMapper;
import com.cwn.wizbank.utils.CommonLog;
import com.cwn.wizbank.utils.RequestStatus;

@Service
public class KbAttachmentService extends BaseService<KbAttachment> {
	@Autowired
	KbAttachmentMapper kbAttachmentMapper;

	public Map<String, Object> upload(Model model, WizbiniLoader wizbini, loginProfile prof, MultipartFile file, HttpServletRequest request) throws IllegalStateException, IOException {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		if (file != null) {
			String filename = file.getOriginalFilename();
			String filetype = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
			String newFilename = getFileNewName(filetype,file.getInputStream());
			
			KbAttachment attachment = new KbAttachment();
			attachment.setKba_filename(filename);
			attachment.setKba_file(newFilename);
			attachment.setKba_create_user_id(prof.usr_id);
			attachment.setKba_create_datetime(kbAttachmentMapper.getDate());
			try {
				kbAttachmentMapper.insert(attachment);
			} catch (Exception e) {
				CommonLog.error(e.getMessage(),e);
			}

			long kba_id = attachment.getKba_id();

			attachment.setKba_url("/attachment/" + kba_id + "/" + attachment.getKba_file());
			kbAttachmentMapper.updateUrl(attachment);

			String basePath = wizbini.getFileUploadAttachmentDirAbs() + File.separator + kba_id + File.separator;
			File distPath = new File(basePath);
			if (!distPath.exists()) {
				distPath.mkdirs();
			}

			File distFile = new File(distPath, newFilename);
			if (distFile.exists()) {
				distFile.delete();
			}
			file.transferTo(distFile);

			// 取回文件路径，用于页面预览
			String url = "/" + wizbini.cfgSysSetupadv.getFileUpload().getAttachmentDir().getUrl() + "/"+ kba_id + "/" + newFilename;
			
			attachment.setKba_url(url);

			// 转pdf
			/* 弃用open office
			try {
				if (filetype.equals("doc") || filetype.equals("docx") || filetype.equals("xls") || filetype.equals("xlsx") || filetype.equals("ppt") || filetype.equals("pptx")) {
					//如果是用open office预览 就转换成pdf
						PdfConverter pdfConverter = new PdfConverter(basePath + newFilename);
						pdfConverter.pdf2jsonPath = request.getSession().getServletContext().getRealPath("/") + "pdf2json"+cwUtils.SLASH+"pdf2json.exe";
						pdfConverter.file2pdf(wizbini.openOfficeConnection, Application.OPENOFFICE_HOST, Application.OPENOFFICE_PORT, false);
				} else if (filetype.equals("pdf")) {
					PdfConverter pdfConverter = new PdfConverter(basePath + newFilename);
					pdfConverter.pdf2jsonPath = request.getSession().getServletContext().getRealPath("/") + "pdf2json"+cwUtils.SLASH+"pdf2json.exe";
					pdfConverter.pdf2json();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			*/
			resultMap.put("attachment", attachment);
		}
		resultMap.put(RequestStatus.STATUS, RequestStatus.SUCCESS);
		return resultMap;
	}

	private String getFileNewName(String filetype,InputStream is) {
		
		long curDate = new Date().getTime();
		
		String fileName = getComplexFileName(15);
		
//		if(!filetype.equalsIgnoreCase("mp4")){
//		}
		fileName += "." + filetype; 
		
		try {
			
			if(filetype.equals("jpg")||filetype.equals("png")||filetype.equals("gif")||filetype.equals("bmp")||filetype.equals("jpeg")){
				
				BufferedImage sourceImg = ImageIO.read(is);
				
				fileName = curDate + "_" +  sourceImg.getWidth() + "_" + sourceImg.getHeight()+"."+filetype;
			}
			
		} catch (IOException e) {
			CommonLog.error(e.getMessage(),e);
		}finally{
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {
					is = null;
					CommonLog.error(e.getMessage(),e);
				}
			}
		}
		
		return fileName;
	}

	public KbAttachment upload(Model model, WizbiniLoader wizbini, loginProfile prof, File file, HttpServletRequest request) {
		KbAttachment attachment = new KbAttachment();
		if (file != null) {
			String filename = file.getName();
			String filetype = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
			String newFilename = new Date().getTime() + "." + filetype;

			attachment.setKba_filename(filename);
			attachment.setKba_file(newFilename);
			attachment.setKba_create_user_id(prof.usr_id);
			attachment.setKba_create_datetime(kbAttachmentMapper.getDate());
			try {
				kbAttachmentMapper.insert(attachment);
			} catch (Exception e) {
				CommonLog.error(e.getMessage(),e);
			}

			long kba_id = attachment.getKba_id();

			attachment.setKba_url("/attachment/" + kba_id + "/" + attachment.getKba_file());
			kbAttachmentMapper.updateUrl(attachment);

			String basePath = wizbini.getFileUploadAttachmentDirAbs() + File.separator + kba_id + File.separator;
			File distPath = new File(basePath);
			if (!distPath.exists()) {
				distPath.mkdirs();
			}

			File distFile = new File(distPath, newFilename);
			if (distFile.exists()) {
				distFile.delete();
			}
			fileChannelCopy(file, distFile);
		}
		return attachment;
	}

	public List<KbAttachment> getKbAttachmentsByKbItemId(Long id) {
		return kbAttachmentMapper.getKbAttachmentsByKbItemId(id);
	}

	/**
	 * 使用文件通道的方式复制文件
	 * 
	 * @param s
	 *            源文件
	 * @param t
	 *            复制到的新文件
	 */

	public void fileChannelCopy(File s, File t) {
		FileInputStream fi = null;
		FileOutputStream fo = null;
		FileChannel in = null;
		FileChannel out = null;
		try {
			fi = new FileInputStream(s);
			fo = new FileOutputStream(t);
			in = fi.getChannel();// 得到对应的文件通道
			out = fo.getChannel();// 得到对应的文件通道
			in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道
		} catch (IOException e) {
			CommonLog.error(e.getMessage(),e);
		} finally {
			try {
				fi.close();
				in.close();
				fo.close();
				out.close();
			} catch (IOException e) {
				CommonLog.error(e.getMessage(),e);
			}
		}
	}

	public List<KbAttachment> getAttachmentByContent(String kbi_content) {
		List<KbAttachment> list = new ArrayList<KbAttachment>();
		String[] fileIds = kbi_content.split(",");
		for (int i = 0; i < fileIds.length; i++) {
			list.add(kbAttachmentMapper.get(Long.valueOf(fileIds[i])));
		}
		return list;
	}

	public void delInvalidAttachment(WizbiniLoader wizbini) {
		List<KbAttachment> list = kbAttachmentMapper.getInvalidAttachment();
		for (KbAttachment kbAttachment : list) {
			deleteAttachmentWithFile(wizbini, kbAttachment);
		}
	}

	public void deleteAttachmentWithFile(WizbiniLoader wizbini, KbAttachment kbAttachment) {
		kbAttachmentMapper.delete(kbAttachment.getKba_id());
		String basePath = wizbini.getFileUploadAttachmentDirAbs() + File.separator + kbAttachment.getKba_id() + File.separator;
		deleteDir(new File(basePath));
	}
	public void deleteAttachmentWithFile(WizbiniLoader wizbini, long kbaId){
		kbAttachmentMapper.delete(kbaId);
		String basePath = wizbini.getFileUploadAttachmentDirAbs() + File.separator + kbaId + File.separator;
		deleteDir(new File(basePath));
	}
	private void deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				deleteDir(new File(dir, children[i]));
			}
		}
		dir.delete();
	}
	
	/**
	 * 生成随机字母和数字组合
	 * @param length 长度
	 * @return
	 */
	public String getComplexFileName(int length){
		String val = "";  
        Random random = new Random();  
        //参数length，表示生成几位随机数  
        for(int i = 0; i < length; i++) {  
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";  
            //输出字母还是数字  
            if( "char".equalsIgnoreCase(charOrNum) ) {  
                //输出是大写字母还是小写字母  
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char)(random.nextInt(26) + temp);
            } else if( "num".equalsIgnoreCase(charOrNum) ) {  
                val += String.valueOf(random.nextInt(10));  
            }  
        }
		return val;
	}
}
