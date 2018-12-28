package com.cw.wizbank.qdb;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.xerces.parsers.DOMParser;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.cw.wizbank.importcos.ImportCos;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;

/**
 * validation for dbcourse
 * 
 * @author elveahuang
 */
public class CourseValidator {

	public static boolean validFileExist(File file) {
		if (file == null || !file.exists() || file.length() <= 0) {
			return false;
		}
		return true;
	}

	// for aicc
	// valid files
	public static String validAiccFile(File aiccCrsFile, File aiccAuFile, File aiccDesFile, File aiccCstFile, File aiccOrtFile) {
		String msg = "";

		if (!validFileExist(aiccCrsFile)) { // valid aiccCrsFile
			msg += genErrorXML(aiccCrsFile, "F001", null, null);
		}

		if (!validFileExist(aiccAuFile)) { // valid aiccCrsFile
			msg += genErrorXML(aiccAuFile, "F001", null, null);
		}

		if (!validFileExist(aiccDesFile)) {// valid aiccDesFile
			msg += genErrorXML(aiccDesFile, "F001", null, null);
		}

		if (!validFileExist(aiccCstFile)) {// valid aiccCstFile
			msg += genErrorXML(aiccCstFile, "F001", null, null);
		}

		if (aiccOrtFile != null && aiccOrtFile.exists()) { // maybe null
			if (!validFileExist(aiccOrtFile)) {// valid aiccOrtFile
				msg += genErrorXML(aiccOrtFile, "F001", null, null);
			}
		}
		return msg;
	}

	/**
	 * aiccCrsFile:1;aiccAuFile:2;aiccDesFile:3;aiccCstFile:4;aiccOrtFile:5;
	 */
	public static String validAiccCourse(File aiccCrsFile, File aiccAuFile, File aiccDesFile, File aiccCstFile, File aiccOrtFile) throws Exception {
		String msg = validAiccFile(aiccCrsFile, aiccAuFile, aiccDesFile, aiccCstFile, aiccOrtFile);
		if (msg.equals("")) {
			msg += validAuFile(aiccAuFile); // valid au file
		}
		return msg;
	}

	public static String validAiccCourse(String aiccCrsFile, String aiccAuFile, String aiccDesFile, String aiccCstFile, String aiccOrtFile)
			throws Exception {
		File ortFile = null;
		if (aiccOrtFile != null && !aiccOrtFile.equals("")) {
			ortFile = new File(aiccOrtFile);
		}
		String msg = validAiccFile(new File(aiccCrsFile), new File(aiccAuFile), new File(aiccDesFile), new File(aiccCstFile), ortFile);
		if (msg.equals("")) {
			msg += validAuFile(new File(aiccAuFile)); // valid au file
		}
		return msg;
	}

	private static String validAuFile(File auFile) throws Exception {
		String msg = "";
		int lineNumber = 0;

		String key = null;
		String value = null;
		String line = null;

		Hashtable fields = new Hashtable();
		BufferedReader in = new BufferedReader(new InputStreamReader(cwUtils.openUTF8FileStream(auFile), cwUtils.ENC_UTF));
		while (true) {
			line = in.readLine();
			lineNumber++;

			if (line == null)
				break;

			if (line.trim().length() > 0 && !line.trim().equalsIgnoreCase("")) {
				Vector vs = dbCourse.buildTableRecord(line);
				String recordElement = null;
				int index = 0;
				for (int i = 0; i < vs.size(); i++) {
					recordElement = (String) vs.elementAt(i);
					fields.put(recordElement.toLowerCase(), new Integer(index));
					index++;
				}
				break;
			}
		}

		while (true) {
			line = in.readLine();
			lineNumber++;
			if (line == null)
				break;

			if (line.trim().length() > 0 && line.trim().equalsIgnoreCase("") == false) {
				Vector v = dbCourse.buildTableRecord(line);

				// valid for max_score - integer number.
				key = "max_score";
				value = (String) v.get(Integer.parseInt(fields.get(key).toString()));
				msg += validAiccField(auFile, lineNumber, key, value, "int");

				// valid for max_score - integer number.
				key = "mastery_score";
				value = (String) v.get(Integer.parseInt(fields.get(key).toString()));
				msg += validAiccField(auFile, lineNumber, key, value, "int");

				// valid for max_time_allowed - integer number.
				key = "max_time_allowed";
				value = (String) v.get(Integer.parseInt(fields.get(key).toString()));
				msg += validAiccField(auFile, lineNumber, key, value, "time");
			}
		}
		return msg;
	}

	public static String validAiccField(File file, int lineNumber, String key, String value, String validType) {
		return validAiccField(file, lineNumber, key, value, true, validType);
	}

	public static String validAiccField(File file, int lineNumber, String key, String value, boolean nullable, String validType) {
		String msg = "";
		if (validType.equals("int")) {
			if (!validIntValue(value, true)) {
				msg += genErrorXML(file, "AICC001", lineNumber, key);
			}
		} else if (validType.equals("time")) {
			if (!validDate(value, true)) {
				msg += genErrorXML(file, "AICC002", lineNumber, key);
			}
		}
		return msg;
	}

	// for scorm
	public static String validScormFile(String file) throws IOException {
		return validScormFile(new File(file));
	}

	public static String validScormFile(File file) throws IOException {
		String msg = "";

		if (!validFileExist(file)) {
			msg = genErrorXML(file, "F001", null, null);
			return msg;
		}

		InputSource inputSource = new InputSource(new InputStreamReader(cwUtils.openUTF8FileStream(file), cwUtils.ENC_UTF));
		DOMParser parser = new DOMParser();
		try {
			parser.parse(inputSource);
		} catch (SAXException e) {
			CommonLog.error(e.getMessage(),e);
			msg += genErrorXML(file, "F002", null, null);
			return msg;
		}

		BufferedReader in = new BufferedReader(new InputStreamReader(cwUtils.openUTF8FileStream(file), cwUtils.ENC_UTF));

		String line = "";
		int lineNumber = 0;

		String[] keyBeginArray = new String[] { "<adlcp:masteryscore>", "<adlcp:maxTimeAllowed>", "<masteryScore>", "<maxTimeAllowed>" };
		String[] keyEndArray = new String[] { "</adlcp:masteryscore>", "</adlcp:maxTimeAllowed>", "</masteryScore>", "</maxTimeAllowed>" };
		String keyBegin = "";
		String keyEnd = "";
		String result = "";
		int start = 0;
		int end = 0;
		String validType = "";

		while (true) {
			line = in.readLine();
			if (line == null) {
				break;
			}
			lineNumber++;
			line = line.trim().toLowerCase();

			for (int i = 0; i < keyBeginArray.length; i++) {
				if (line.length() > 0 && line.indexOf(keyBeginArray[i].toLowerCase()) >= 0) {
					keyBegin = keyBeginArray[i].toLowerCase();
					keyEnd = keyEndArray[i].toLowerCase();

					if (keyBegin.equals(keyBeginArray[0].toLowerCase()) || keyBegin.equals(keyBeginArray[2].toLowerCase())) {
						validType = "int";
					} else if (keyBegin.equals(keyBeginArray[1].toLowerCase()) || keyBegin.equals(keyBeginArray[3].toLowerCase())) {
						validType = "time";
					}
				} else {
					continue;
				}

				if (line.length() > 0 && line.indexOf(keyBegin) >= 0) {
					if (line.indexOf(keyEnd) >= 0) {
						start = line.indexOf(keyBegin) + keyBegin.length();
						end = line.indexOf(keyEnd);
						result = line.substring(start, end).trim();
						msg += validScormField(file, lineNumber, keyBegin, result, validType);
					} else {
						start = line.indexOf(keyBegin) + keyBegin.length();
						result = line.substring(start).trim();
						while (true) {
							String nextLine = in.readLine();
							if (nextLine == null) {
								break;
							}
							lineNumber++;
							nextLine = nextLine.trim();
							if (nextLine.indexOf(keyEnd) >= 0) {
								result += nextLine.substring(0, nextLine.indexOf(keyEnd));
								msg += validScormField(file, lineNumber, keyBegin, result, validType);
								break;
							} else {
								result += nextLine.substring(0).trim();
								continue;
							}
						}
					}
				}
			}
		}
		return msg.trim();
	}

	public static String validScormField(File file, int lineNumber, String key, String value, String validType) {
		String msg = "";
		if (validType.equals("int")) {
			if (!validIntValue(value, true)) {
				msg = genErrorXML(file, "SCORM001", lineNumber, key);
			}
		} else if (validType.equals("time")) {
			if (!validDate(value, true)) {
				msg = genErrorXML(file, "SCORM002", lineNumber, key);
			}
		}
		return msg;
	}

	public static String validNetgFile(File file) throws IOException {
		String msg = "";

		if (!validFileExist(file)) {
			msg = genErrorXML(file, "F001", null, null);
			return msg;
		}

		String line;
		int lineNumber = 0;

		BufferedReader in = new BufferedReader(new InputStreamReader(cwUtils.openUTF8FileStream(file), cwUtils.ENC_UTF));
		while (true) {
			line = in.readLine();
			if (line == null) {
				break;
			}
			lineNumber++;
			String key = ImportCos.NETG_CDF_PARAM_DURA;
			String value = "";
			// dura
			if (line.trim().length() > 0 && line.trim().toUpperCase().startsWith(key)) {
				value = line.substring(key.length()).trim();
				String[] str = new String[] { ImportCos.NETG_CDF_PARAM_HOUR, ImportCos.NETG_CDF_PARAM_SEC, ImportCos.NETG_CDF_PARAM_MIN };
				for (int i = 0; i < str.length; i++) {
					if (value.toUpperCase().endsWith(str[i])) {
						value = value.substring(0, value.length() - str[i].length()).trim();
						if (!validFloatValue(value, true)) {
							msg += genErrorXML(file, "NETG001", lineNumber, key.substring(0, key.indexOf("=")));
						}
						break;
					}
				}
			}
		}
		return msg;
	}

	public static boolean validDate(String value, boolean nullable) {
		if (nullable) {
			if (value == null || value.trim().equals("")) {
				return true;
			}
		}
		if (value != null && !value.trim().equals("")) {
			try {
				SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
				format.parse(value);
			} catch (ParseException e) {
				return false;
			}
		}
		return true;
	}

	public static boolean validIntValue(String value, boolean nullable) {
		if (nullable) {
			if (value == null || value.trim().equals("")) {
				return true;
			}
		}
		if (value != null && !value.trim().equals("")) {
			try {
				Integer.parseInt(value);
			} catch (NumberFormatException e) {
				return false;
			}
		}
		return true;
	}

	public static boolean validFloatValue(String value, boolean nullable) {
		if (nullable) {
			if (value == null || value.trim().equals("")) {
				return true;
			}
		}
		if (value != null && !value.trim().equals("")) {
			try {
				Float.parseFloat(value);
			} catch (NumberFormatException e) {
				return false;
			}
		}
		return true;
	}

	public static String genErrorXML(String file, String code, String line, String field) {
		file = (file == null) ? "" : file;
		code = (code == null) ? "" : code;
		line = (line == null) ? "" : line;
		field = (field == null) ? "" : field;
		return "<error file=\"" + dbUtils.esc4XML(file) + "\" line=\"" + line + "\" code=\"" + code + "\" field=\"" + dbUtils.esc4XML(field) + "\"/>";
	}

	public static String genErrorXML(File file, String code, int line, String field) {
		String s = (new Integer(line)).toString();
		return genErrorXML(file.getName(), code, s, field);
	}

	public static String genErrorXML(File file, String code, String line, String field) {
		return genErrorXML(file.getName(), code, line, field);
	}

	// only for test
	public static void main(String[] args) throws Exception {
		// aicc
		/*
		File f1 = new File("D:\\samplecourse.crs");
		File f2 = new File("D:\\samplecourse.au");
		File f3 = new File("D:\\samplecourse.des");
		File f4 = new File("D:\\samplecourse.ort");
		File f5 = new File("D:\\samplecourse.cst");
		System.out.println(CourseValidator.validAiccCourse(f1, f2, f3, f4, f5));
		System.out.println("............................");
		// scorm
		File f6 = new File("D:\\imsmanifest.xml");
		System.out.println(CourseValidator.validScormFile(f6));
		System.out.println("............................");
		// netg
		File f7 = new File("D:\\zh_CN_45233j.cdf");
		System.out.println(CourseValidator.validNetgFile(f7));
		System.out.println("............................");
		*/
	}
}
