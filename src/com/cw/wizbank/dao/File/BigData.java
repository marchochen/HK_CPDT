package com.cw.wizbank.dao.File;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List; //import javax.servlet.http.HttpServletRequest;
//import org.apache.commons.fileupload.FileItem;
//import org.apache.commons.fileupload.FileUploadException;
//import org.apache.commons.fileupload.disk.DiskFileItemFactory;
//import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import com.cw.wizbank.dao.Log4jFactory;

public class BigData {
	private static final Logger logger = Log4jFactory
			.createLogger(com.cw.wizbank.dao.File.BigData.class);
	// private static final ServletFileUpload fileUpload = getFileUpload();

	private List data = new ArrayList();

	private String fileName;
	private long fileSize;

	public BigData(List data) {
		this.data = data;
	}
	
	public BigData(byte[] data){
		try {
			BufferedInputStream input = new BufferedInputStream(new ByteArrayInputStream(data));
			this.data = getData(input);
		} catch (RuntimeException e) {
			logger.error("get content from clob exception", e);
			throw new BigDataException("get content from clob exception");
		}
	}

	public BigData(File file) {
		try {
			BufferedInputStream input = new BufferedInputStream(
					new FileInputStream(file));
			this.data = getData(input);
			this.fileName = file.getName();
			this.fileSize = file.length();
		} catch (FileNotFoundException e) {
			logger.error(file.getAbsolutePath() + " not found", e);
			throw new BigDataException(file.getAbsolutePath() + " not found");
		}
	}

	public BigData(Clob clob) {
		try {
			BufferedInputStream input = new BufferedInputStream(clob
					.getAsciiStream());
			this.data = getData(input);
		} catch (SQLException e) {
			logger.error("get content from clob exception", e);
			throw new BigDataException("get content from clob exception");
		}
	}

	public BigData(Blob blob) {
		try {
			BufferedInputStream input = new BufferedInputStream(blob
					.getBinaryStream());
			this.data = getData(input);
		} catch (SQLException e) {
			logger.error("get content from blob exception", e);
			throw new BigDataException("get content from blob exception");
		}
	}

	// public BigData(HttpServletRequest request, String paramName) {
	// try {
	// List<FileItem> fileListItems = fileUpload.parseRequest(request);
	// FileItem targetFileItem = null;
	// for (Iterator<FileItem> ites = fileListItems.iterator(); ites
	// .hasNext();) {
	// FileItem fileItem = ites.next();
	// if (!fileItem.isFormField()
	// && fileItem.getFieldName().equals(paramName)) {
	// targetFileItem = fileItem;
	// break;
	// }
	// }
	// if (targetFileItem == null) {
	// throw new BigDataException(
	// "not found file input from formfield " + paramName);
	// }
	// this.fileName = targetFileItem.getName();
	// this.fileSize = targetFileItem.getSize();
	// this.data = getData(new BufferedInputStream(targetFileItem
	// .getInputStream()));
	// } catch (FileUploadException e) {
	// logger.error("Get fileContent from HttpServletRequest failure", e);
	// throw new BigDataException(
	// "Get fileContent from HttpServletRequest failure");
	// } catch (IOException e) {
	// logger.error("Get fileContent from inputStream failure", e);
	// throw new BigDataException(
	// "Get fileContent from inputStream failure");
	// }
	//
	// }

	public final void writeBigData(File file) {
		try {
			BufferedOutputStream output = new BufferedOutputStream(
					new FileOutputStream(file));
			this.writeBigData(output);
		} catch (FileNotFoundException e) {
			logger.error(file.getAbsolutePath() + " not found.", e);
			throw new BigDataException(file.getAbsolutePath() + " not found.");
		}
	}

	public final InputStream getInputStrean() {
		int length = 0;
		for (Iterator iterator = this.data.iterator(); iterator.hasNext();) {
			SaveBufferData saveBufferData = (SaveBufferData) iterator.next();
			length += saveBufferData.getLength();
		}
		byte[] saveData = new byte[length];
		int index = 0;
		for (Iterator iterator = this.data.iterator(); iterator.hasNext();) {
			SaveBufferData saveBufferData = (SaveBufferData) iterator.next();
			for (int n = 0; n < saveBufferData.getLength(); n++) {
				saveData[index + n] = saveBufferData.getSavaData()[n];
			}
			index += saveBufferData.getLength();
		}
		return new BufferedInputStream(new ByteArrayInputStream(saveData, 0,
				length));
	}

	public final String getContent() {
		StringBuffer str = new StringBuffer();
		for (Iterator iterator = this.data.iterator(); iterator.hasNext();) {
			SaveBufferData saveBufferData = (SaveBufferData) iterator.next();
			str.append(new String(saveBufferData.getSavaData(), 0,
					saveBufferData.getLength()));
		}
		return str.toString();
	}

	public static final void writeStream(BufferedInputStream input,
			BufferedOutputStream output) {
		try {
			int length = 0;
			byte[] savaData = new byte[SaveBufferData.bufferSize];
			while ((length = input.read(savaData, 0, SaveBufferData.bufferSize)) != -1) {
				output.write(savaData, 0, length);
			}
			output.flush();
		} catch (IOException e) {
			logger.error("write stream failure.", e);
			throw new BigDataException("write stream failure.");
		} finally {
			closeStream(input, output);
		}

	}

	public String getFileName() {
		return fileName;
	}

	public long getFileSize() {
		return fileSize;
	}

	public final void writeBigData(BufferedOutputStream output) {
		try {
			for (Iterator dataitem = this.data.iterator(); dataitem
					.hasNext();) {
				SaveBufferData saveBufferData = (SaveBufferData) dataitem.next();
				output.write(saveBufferData.getSavaData(), 0, saveBufferData
						.getLength());
			}
			output.flush();
		} catch (IOException e) {
			logger.error("write file fialure.", e);
			throw new BigDataException("write file not fialure.");
		} finally {
			closeStream(null, output);
		}
	}

	private static List getData(BufferedInputStream input) {
		List allData = new ArrayList();
		try {
			int length = 0;
			byte[] saveData = new byte[SaveBufferData.bufferSize];
			while ((length = input.read(saveData, 0, SaveBufferData.bufferSize)) != -1) {
				SaveBufferData saveBufferData = new SaveBufferData(saveData,
						length);
				saveData = new byte[SaveBufferData.bufferSize];
				allData.add(saveBufferData);
			}
			return allData;
		} catch (IOException e) {
			logger.error("file write exception", e);
			throw new BigDataException("file write exception");
		} finally {
			closeStream(input, null);
		}

	}

	// private static ServletFileUpload getFileUpload() {
	// // Create a factory for disk-based file items
	// DiskFileItemFactory factory = new DiskFileItemFactory();
	// // Set factory constraints
	// factory.setSizeThreshold(SaveBufferData.bufferSize); // 设置缓冲区大小，这里是5MB
	// // factory.setRepository(tempPathFile);// 设置缓冲区目录
	// // Create a new file upload handler
	// ServletFileUpload upload = new ServletFileUpload(factory);
	//
	// // Set overall request size constraint
	// upload.setSizeMax(10 * 1024 * 1024); // 设置最大文件尺寸，这里是10MB
	//
	// return upload;
	// }

	private static void closeStream(InputStream input, OutputStream output) {
		try {
			if (input != null) {
				input.close();
			}
			if (output != null) {
				output.close();
			}
		} catch (IOException e) {
			logger.error("close stream failure.", e);
		}
	}

	private static final class BigDataException extends RuntimeException {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public BigDataException() {

		}

		public BigDataException(String message) {
			super(message);
		}
	}

}
