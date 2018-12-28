package com.cw.wizbank.scorm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Iterator;
import java.util.zip.ZipException;

import org.apache.tools.zip.ZipFile;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.cw.wizbank.util.UploadListener;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;

public class ZipManager{
	public final static String MANIFEST_FILE_NAME = "imsmanifest.xml";
	/**
	 * 字符编码转换,解决中文乱码问题
	 * @param str
	 * @return
	 */
	public static String make8859toGB(String str)
	{
		try {
			String str8859 = new String(str.getBytes("8859_1"), "GB2312");
			return str8859;
		} catch (UnsupportedEncodingException ioe) {
			return str;
		}
	}
	
	/**
	 * 解压zip文件到目标文件夹
	 * @param sourceZip  压缩文件全路径,包括文件名
	 * @param outFileName  解压目标路径
	 * @return String 清单文件路径
	 * @throws ZipException,IOException,Exception 
	 */
	public static String releaseZipToFile(String sourceZip, String outFileName, UploadListener.FileUploadStats fileUploadStats)
 throws Exception {
        File infile = new File(sourceZip);
        String manifest_file_path = null;
        try {
            // 检查是否是ZIP文件
            ZipFile zip = new ZipFile(infile, "UTF-8");
            // zip.close();

            // 统计出zip中的文件总数
            int zipFileCount = 0;
            // ZipInputStream zipFile = new ZipInputStream(new FileInputStream(infile));
            // while ((zipFile.getNextEntry()) != null){
            // zipFileCount++;
            // }
            // zipFile.close();
            java.util.Enumeration e = zip.getEntries();
            org.apache.tools.zip.ZipEntry zipEntry = null;
            // while (e.hasMoreElements()) {
            // zipFileCount++;
            // }

            int i = infile.getAbsolutePath().lastIndexOf('.');
            File extractDir = new File(outFileName);
            if (!extractDir.exists()) {
                if (!extractDir.mkdirs())
                    throw new IOException("Failed to create extract dir: " + extractDir);
            }
            int index = 0;
            byte[] c = new byte[1024];
            int slen;

            while (e.hasMoreElements()) {
                zipFileCount++;
                zipEntry = (org.apache.tools.zip.ZipEntry) e.nextElement();
                if (zipEntry != null) {
                    i = zipEntry.getName().replace('/', File.separatorChar).lastIndexOf(File.separatorChar);
                    if (i != -1) {
                        File dirs = new File(outFileName + File.separator + zipEntry.getName().replace('/', File.separatorChar).substring(0, i));
                        dirs.mkdirs();
                        dirs = null;
                    }
                    if (zipEntry.isDirectory()) {
                        File dirs = new File(outFileName + cwUtils.SLASH + zipEntry.getName().replace('/', File.separatorChar));
                        dirs.mkdir();
                        dirs = null;
                    } else {
                        String filename = zipEntry.getName().substring(zipEntry.getName().lastIndexOf('/') + 1);
                        if (filename.equalsIgnoreCase(MANIFEST_FILE_NAME) && zipEntry.getName().indexOf("/") == -1) {
/*                        	System.out.println("----manifest_file:" + zipEntry.getName());
                            String[] file_array = zipEntry.getName().split("/");
                            if (!(file_array.length > 2 && file_array[file_array.length - 2].equals("lib")))*/
                            manifest_file_path = zipEntry.getName();
                        }
                        InputStream in = zip.getInputStream(zipEntry);
                        FileOutputStream out = new FileOutputStream(outFileName + File.separator + zipEntry.getName().replace('/', File.separatorChar));
                        while ((slen = in.read(c, 0, c.length)) != -1)
                            out.write(c, 0, slen);
                        out.close();
                        in.close();
                    }
                }

                // 建立与目标文件的输入连接
                // ZipInputStream in = new ZipInputStream(new FileInputStream(infile));
                // ZipEntry file = in.getNextEntry();
                // int i = infile.getAbsolutePath().lastIndexOf('.');
                // File extractDir = new File(outFileName);
                // boolean bOk = extractDir.mkdirs();
                // if (!bOk)
                // throw new IOException ("Failed to create extract dir: " + extractDir);
                // byte[] c = new byte[1024];
                // int slen;
                // int index = 0;
                // while (file != null) {
                // i = make8859toGB(file.getName()).replace('/', '\\')
                // .lastIndexOf('\\');
                // if (i != -1) {
                // File dirs = new File(outFileName
                // + File.separator
                // + make8859toGB(file.getName()).replace('/', '\\')
                // .substring(0, i));
                // dirs.mkdirs();
                // dirs = null;
                // }
                // if (file.isDirectory()) {
                // File dirs = new File(make8859toGB(file.getName()).replace(
                // '/', '\\'));
                // dirs.mkdir();
                // dirs = null;
                // }
                // else {
                // String filename = file.getName().substring(
                // file.getName().lastIndexOf('/') + 1);
                // if(filename.equalsIgnoreCase(MANIFEST_FILE_NAME)){
                // manifest_file_path = file.getName();
                // }
                // FileOutputStream out = new FileOutputStream(outFileName
                // + File.separator
                // + make8859toGB(file.getName()).replace('/', '\\'));
                // while ((slen = in.read(c, 0, c.length)) != -1)
                // out.write(c, 0, slen);
                // out.close();
                // }
                fileUploadStats.setExtractedFile(++index);
                // file = in.getNextEntry();
            }
            // in.close();
            fileUploadStats.setFilesInZip(zipFileCount);
            zip.close();
            CommonLog.debug("Unzip file \"" + sourceZip + "\" to \"" + outFileName + "\"");
            return manifest_file_path;

            // } catch (ZipException zipe) {
            // throw new ZipException ("The file format must be zip: " + sourceZip.substring(
            // sourceZip.lastIndexOf(cwUtils.SLASH) + 1));
        } catch (IOException ioe) {
            // throw new IOException ("Failed to extract zip file:" + sourceZip.substring(
            // sourceZip.lastIndexOf(cwUtils.SLASH) + 1));
            CommonLog.error(ioe.getMessage(),ioe);
            return "aa";
        } catch (Exception i) {
            i.printStackTrace();
            throw new Exception("Other error in extract zip file:" + sourceZip.substring(sourceZip.lastIndexOf(cwUtils.SLASH) + 1));
        }
    }
	
	/**
	 * 删除zip文件
	 * @param sourceZip 压缩文件全路径,包括文件名
	 * @return String
	 * @throws IOException 
	 */
	public static void delZipFile(String sourceZip) throws IOException {
		File infile = new File(sourceZip);
		if(!infile.delete()){
			throw new IOException ("Failed to delete temporary zip file:" + sourceZip.substring(
					sourceZip.lastIndexOf(cwUtils.SLASH) + 1));
		}
	}
	
	/**
	 * 修改清单文件的路径
	 * @param manifestFilePath
	 * @param dataPath
	 * @throws Exception
	 */
	public static void modiListFilePath(String extractPath, String manifestFilePath, String dataPath, String url) throws Exception{
		if(manifestFilePath==null){
			throw new Exception ("No Manifest File!");
		}
		String temp_path = "";
		String new_path = "";
		int path_length = manifestFilePath.split("/").length;
		if(path_length>1){
			temp_path = manifestFilePath.substring(0, manifestFilePath.lastIndexOf('/'));
			for(int i=1;i<path_length;i++){
				new_path += "../";
			}
		}
		new_path += dataPath + "/";
		
		if(!temp_path.equals("")){
            new_path += temp_path + "/";
        }
		
		if(url != null){
            if(url.endsWith("/")){
                new_path = url + new_path ;
            }else{
                new_path = url + "/" + new_path ;
            }
        }
		
		
		
		Document doc = null;
		SAXReader reader = new SAXReader();
		try {
			File xmlfile = new File(extractPath + cwUtils.SLASH + manifestFilePath);
			//FileInputStream in = new FileInputStream(xmlfile);
			FileInputStream in = cwUtils.openUTF8FileStream(xmlfile);
			InputStreamReader strInStream;
			String code = getXmlFileEncodeType(xmlfile);
			if(code==null || code.equals("")){
				strInStream = new InputStreamReader(in,cwUtils.ENC_UTF);
			}else{
				strInStream = new InputStreamReader(in, code);
			}
			doc = reader.read(strInStream);
			xmlfile = null;
		} catch (DocumentException e) {
		    CommonLog.error(e.getMessage(),e);
			throw new Exception ("Can't open manifest file!");
		}
		Element root = doc.getRootElement();
		Element item = null;
		for (Iterator it = root.elementIterator("resources"); it.hasNext();) {
			item = (Element) it.next();
			for (Iterator it1 = item.elementIterator("resource"); it1.hasNext();) {
				Element item1 = (Element) it1.next();
				String href1 = item1.attributeValue("href");
				//add xml:base support
				String xml_base = item1.attributeValue("base");
				if(href1!=null){
					if (xml_base != null && xml_base.length() > 0) {
						href1 = xml_base + "/" + href1;
					}
					href1 = new_path + href1.replaceAll("\\\\", "/");
					item1.attribute("href").setText(href1);
				}
				for (Iterator it2 = item1.elementIterator("file"); it2.hasNext();) {
					Element item2 = (Element) it2.next();
					String href2 = item2.attributeValue("href");
					if(href2!=null){
						if (xml_base != null && xml_base.length() > 0) {
							href2 = xml_base + "/" + href2;
						}
						href2 = new_path + href2.replaceAll("\\\\", "/");
						item2.attribute("href").setText(href2);
					}
				}
			}
		}

		try {
			Writer wr = new OutputStreamWriter(new FileOutputStream(extractPath + cwUtils.SLASH + manifestFilePath),"UTF-8");
			doc.write(wr);   
		    wr.close();
		} catch (IOException e) {
		    CommonLog.error(e.getMessage(),e);
			throw new Exception ("Modify manifest file error!");
		}
	}
	
	private static String getXmlFileEncodeType(File file) throws IOException{
	    String code = "";
	    try{
    		BufferedReader reader = new BufferedReader(new InputStreamReader(cwUtils.openUTF8FileStream(file)));
    		String line = reader.readLine(); 
    		reader.close();
    		if(line!=null){ 
    			if(line.indexOf("encoding")==-1){
    				code = "";
    			}else if(line.indexOf("GB2312")!=-1 || line.indexOf("gb2312")!=-1){
    				code = "GB2312";
    			}else if(line.indexOf("UTF-8")!=-1 || line.indexOf("utf-8")!=-1){
    				code = "UTF-8";
    			}else{
    				code = line.substring(line.indexOf("encoding")+10, line.lastIndexOf("\""));
    			}
    			
    		}
	    }catch (Exception e){
	        try{
    	        BufferedReader reader = new BufferedReader(new FileReader(file));
    	        String line = reader.readLine(); 
    	        reader.close();
    	        if(line!=null){ 
    	            if(line.indexOf("encoding")==-1){
    	                code = "";
    	            }else if(line.indexOf("GB2312")!=-1 || line.indexOf("gb2312")!=-1){
    	                code = "GB2312";
    	            }else if(line.indexOf("UTF-8")!=-1 || line.indexOf("utf-8")!=-1){
    	                code = "UTF-8";
    	            }else{
    	                code = line.substring(line.indexOf("encoding")+10, line.lastIndexOf("\""));
    	            }
    	        } 
	        }catch (Exception ee){
	            code = "UTF-8";
	        }
		}
		return code;
	}
}
