package com.cwn.wizbank.wechat.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;

import com.cwn.wizbank.utils.CommonLog;

/**
 * 文件与字符转换类
 *
 */
public class FileStringHelper {
	private static final int DEFAULT_BUFFER_SIZE = 1024;

	/**
	 * 将字符串写入指定文件(当指定的父路径中文件夹不存在时，会最大限度去创建，以保证保存成功
	 * 
	 * @param res
	 *            原字符串
	 * @param filePath
	 *            文件路径
	 * @return 成功标记
	 */
	public static boolean string2File(String res, String filePath) {
		boolean flag = true;
		BufferedReader bufferedReader = null;
		BufferedWriter bufferedWriter = null;
		try {
			File distFile = new File(filePath);
			if (!distFile.getParentFile().exists())
				distFile.getParentFile().mkdirs();
			bufferedReader = new BufferedReader(new StringReader(res));
			bufferedWriter = new BufferedWriter(new FileWriter(distFile));
			char buf[] = new char[1024]; // 字符缓冲
			int len;
			while ((len = bufferedReader.read(buf)) != -1) {
				bufferedWriter.write(buf, 0, len);
			}
			bufferedWriter.flush();
			bufferedReader.close();
			bufferedWriter.close();
		} catch (IOException e) {
			CommonLog.error(e.getMessage(),e);
			flag = false;
			return flag;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					CommonLog.error(e.getMessage(),e);
				}
			}
		}
		return flag;
	}

	/**
	 * 文本文件转换为指定编码的字符
	 * 
	 * @param file
	 *            文本文件
	 * @param encoding
	 *            编码类型
	 * @return 转换后的字符
	 * @throws IOException
	 */
	public static String file2String(File file, String encoding) {
		InputStreamReader reader = null;
		StringWriter writer = new StringWriter();
		try {
			if (encoding == null || "".equals(encoding.trim())) {
				reader = new InputStreamReader(new FileInputStream(file),
						encoding);
			} else {
				reader = new InputStreamReader(new FileInputStream(file));
			}
			// 将输入流写入输出
			char[] buffer = new char[DEFAULT_BUFFER_SIZE];
			int n = 0;
			while (-1 != (n = reader.read(buffer))) {
				writer.write(buffer, 0, n);
			}
		} catch (Exception e) {
			CommonLog.error(e.getMessage(),e);
			return null;
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
					CommonLog.error(e.getMessage(),e);
				}
		}
		return writer.toString();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}
