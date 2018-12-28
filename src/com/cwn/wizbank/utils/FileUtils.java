package com.cwn.wizbank.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * 文件操作类
 * @author leon.li
 * 2014-8-7 下午6:04:20
 */
public class FileUtils  extends org.apache.commons.io.FileUtils {

	public final static String SNS_TEMP_DIR = "SNSTEMPDIR";
	
	/**
	 *  复制文件
	 * @param targetFile
	 * @param file
	 */
	public static void fileChannelCopy(File targetFile, File file) {
		FileInputStream fi = null;
		FileOutputStream fo = null;
		FileChannel in = null;
		FileChannel out = null;
		try {
			fi = new FileInputStream(targetFile);
			fo = new FileOutputStream(file);
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
}
