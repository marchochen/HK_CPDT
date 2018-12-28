package com.cw.wizbank.supplier.utils;

import com.cw.wizbank.util.cwSQL;

public class SQLHelper {

	public static String replaceSpecialChar(String str){
		//System.out.println(str);
		if (str != null && !"".equals(str)) {
			str = str.replaceAll("\\\\", "\\\\\\\\")
					.replaceAll("%", "\\\\%")
					.replaceAll("_", "\\\\_")
					.replaceAll("\\[", "\\\\[")
					.replaceAll("\\^", "\\\\^");
			if (cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType())) {
				str = str.replaceAll("\\&", "\\\\&");
			}

		}
		//System.out.println(str);
		return str;
	}
	
	public static void main(String[] args) {
		String value = "[";
		//System.out.println(replaceSpecialChar(value));
	}
}
