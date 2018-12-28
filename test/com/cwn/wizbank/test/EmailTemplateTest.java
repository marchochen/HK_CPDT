package com.cwn.wizbank.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.cwn.wizbank.base.BaseTest;
import com.cwn.wizbank.entity.MessageTemplate;
import com.cwn.wizbank.services.MessageTemplateService;

public class EmailTemplateTest extends BaseTest {
	
	@Autowired
	MessageTemplateService messageTemplateService;
	
	@Test
	@Rollback(false)
	public void test(){
		List<String> fileNames = new ArrayList<String>();
		File dir = new File(getDirPath());
		if(dir.isDirectory()) {
			File[] listFiles = dir.listFiles();
			if(listFiles != null && listFiles.length > 0) {
				for(File f : listFiles) {
					String name = f.getName();
					String content = file2String(f);
					name = name.substring(2).replace(".html", "");
					fileNames.add(name);
					//
					Map<String,Object> param = new HashMap<String,Object>();
					param.put("tpl_type", name);
					param.put("tcr_id", 1);
					MessageTemplate temp = messageTemplateService.getByType(param);
					temp.setMtp_content(content);
					messageTemplateService.update(temp);
					System.out.println(name + "    " + content);
				}
			}
		}
		
	}
	
	
	public String getDirPath(){
		String path = this.getClass().getResource("").getPath();
		path = path.substring(0, path.lastIndexOf("/"));
		path = path.substring(0, path.lastIndexOf("/") + 1);
		path = path + "resources/email/";
		return path;
	}
	
	
	public static String file2String(File file) {
		String result = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));// 构造一个BufferedReader类来读取文件
			String s = null;
			while ((s = br.readLine()) != null) {// 使用readLine方法，一次读一行
				result = result + "\n" + s;
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
