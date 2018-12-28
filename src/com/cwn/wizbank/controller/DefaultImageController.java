package com.cwn.wizbank.controller;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.util.SocketUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.util.cwUtils;
import com.sun.msv.datatype.xsd.Comparator;

@Controller
@RequestMapping("defaultImage")
public class DefaultImageController {
	
	 
	static private class PhotoFileCompare implements Comparator 
	{

		@Override
		public int compare(Object arg0, Object arg1) {
			PhotoFile p1 = (PhotoFile) arg0;
			
			PhotoFile p2 = (PhotoFile) arg1;
			
			 
			
			if(p1.ext.compareTo(p2.ext)==0)
			{
				return p1.fileName.compareTo(p2.fileName);
			}
			else
			{
				return p1.ext.compareTo(p2.ext);
			}
			
		}
 		
	}
	
	
	static private class PhotoFile implements Comparable<PhotoFile>
	{
		/**
		 * 文件名
		 */
		private String fileName;
		
		/**
		 * 扩展名
		 */
		private String ext;
		
		/**
		 *  父目录名字
		 */
		private String parentName;

		public PhotoFile(String fileName, String ext, String parentName) {
			super();
			this.fileName = fileName;
			this.ext = ext;
			this.parentName = parentName;
		}

		@Override
		public String toString() {
			return "PhotoFile [fileName=" + fileName + ", ext=" + ext + ", parentName=" + parentName + "]";
		}

		@Override
		public int compareTo(PhotoFile other) {
			// TODO Auto-generated method stub
			
			 
		 
			if(this.parentName.compareTo(other.parentName)==0)
			{
				return this.fileName.compareTo(other.fileName);
			}
			else
			{
				return this.parentName.compareTo(other.parentName);
			} 
			
		 
		}
		
		
		
	}
	
	
	/**
	 *  读取文件夹中的所有文件
	 * @param files
	 * @param file
	 * @return
	 */
	private void readFile(List<File> files,File file)
	{
		if(file.isDirectory())
		{
			for(File directory : file.listFiles())
			{
				readFile(files,directory);
			}
		}
		else
		{
			files.add(file);
		}
	}
	
	@RequestMapping("{type}")
	@ResponseBody
	public List<String> getDefaultImages(WizbiniLoader wizbini, @PathVariable(value = "type") String type) {
		//String path = wizbini.getWebDocRoot() + cwUtils.SLASH + type + cwUtils.SLASH + "default";
		String path = wizbini.getWebDocRoot() + cwUtils.SLASH + "imglib" + cwUtils.SLASH + type ;//default统一改为读取imglib文件夹下
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		
		List<String> lists = new ArrayList<String>();
		
		if(type.equals("grade")||type.equals("specialtopic"))
		{
			File[] images = dir.listFiles(new FileFilter() {
				@Override
				public boolean accept(File file) {					
					String filename = file.getName();
					String ext = filename.substring(filename.lastIndexOf(".") + 1, filename.length());

					if (cwUtils.notEmpty(ext) && (ext.equalsIgnoreCase("jpeg") || ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("gif") || ext.equalsIgnoreCase("png"))) {
						return true;
					}
					return false;
				}
			});
			
		 
			for (File file : images) {
				lists.add(file.getName());
			}
		}
		else
		{
			List<File> files = new ArrayList<File>();
			
			List<PhotoFile> photoFiles = new ArrayList<PhotoFile>();
			
			readFile(files,dir);
			
	 
			for(File file : files)
			{
				String filename = file.getName();
				String ext = filename.substring(filename.lastIndexOf(".") + 1, filename.length());

				if (cwUtils.notEmpty(ext) && (ext.equalsIgnoreCase("jpeg") || ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("gif") || ext.equalsIgnoreCase("png"))) {
				 
					PhotoFile p = new PhotoFile(filename.split("."+ext)[0],ext,file.getParentFile().getName()+"/");
					photoFiles.add(p);
					
				}
			}
			
			
			
			/*
			
			File[] images = dir.listFiles(new FileFilter() {
				@Override
				public boolean accept(File file) {
					
					 
					
					String filename = file.getName();
					String ext = filename.substring(filename.lastIndexOf(".") + 1, filename.length());

					if (cwUtils.notEmpty(ext) && (ext.equalsIgnoreCase("jpeg") || ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("gif") || ext.equalsIgnoreCase("png"))) {
						return true;
					}
					return false;
				}
			});
			
			*/
	 
					
		 
			
			//Collections.sort(photoFiles, new PhotoFileCompare());
			
			Collections.sort(photoFiles );
			Collections.reverse(photoFiles );
			
			for(PhotoFile p : photoFiles)
			{
			
				//	System.out.println(p);
				lists.add("/"+p.parentName+p.fileName+"."+p.ext);
			}
			
		}
		
	  return lists;
	}
	
}
