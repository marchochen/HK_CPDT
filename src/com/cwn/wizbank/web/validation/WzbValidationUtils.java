package com.cwn.wizbank.web.validation;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CharUtils;

public class WzbValidationUtils extends ValidationUtils {
	public static void validateEmpty(Errors errors, String field, String message, String... args) {
		rejectIfEmptyOrWhitespace(errors, field, null, coverArgsToArray(args), message);
	}

	// 验证输入是否是正整数
	public static void validateIsPositiveInteger(Errors errors, String field, String message, String... args) {
		String patten = "^\\+?[1-9][0-9]*$";
		validatePatten(errors, field, patten, message, args);
	}

	// 验证输入是否是指定值
	public static void validateIsValue(Errors errors, String field, String target, String message, String... args) {
		Object value = errors.getFieldValue(field);
		if (value == null || !value.toString().trim().equals(target)) {
			errors.rejectValue(field, null, coverArgsToArray(args), message);
		}
	}

	// 验证两次输入是否一致
	public static void validateEqualTo(Errors errors, String field, String targetField, String message, String... args) {
		if (!errors.hasFieldErrors(field)) {
			Object value = errors.getFieldValue(field);
			Object target = errors.getFieldValue(targetField);

			if (value != null && target != null) {
				String val1 = value.toString().trim();
				String val2 = target.toString().trim();
				if (!val1.equals(val2)) {
					errors.rejectValue(field, null, coverArgsToArray(args), message);
				}
			}
		}
	}

	// 验证电子邮箱
	public static void validateEmail(Errors errors, String field, String message, String... args) {
		String patten = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
		validatePatten(errors, field, patten, message, args);
	}

	// 身份证号码
	public static void validateUserID(Errors errors, String field, String message, String... args) {
		String patten = "^(\\d{15}$)|(\\d{17})([0-9]|X)$";
		validatePatten(errors, field, patten, message, args);
	}

	// 手机号码
	public static void validatePhone(Errors errors, String field, String message, String... args) {
		String patten = "^(((17[0-9])|(13[0-9])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8})$";
		validatePatten(errors, field, patten, message, args);
	}

	// 电话号码
	public static void validateTel(Errors errors, String field, String message, String... args) {
		String patten = "^((\\d{3,4}-)|\\d{3.4}-)?\\d{7,8}$";
		validatePatten(errors, field, patten, message, args);
	}

	// 文本不包含特殊字符且长度限制
	public static void validateTextAndLength(Errors errors, String field, long length, String message, String... args) {
		String patten = "^[A-Za-z0-9\u4e00-\u9fa5]{1," + length + "}$";
		validatePatten(errors, field, patten, message, args);
	}

	// 文本不包含特殊字符且长度限制
	public static void validateLength(Errors errors, String field, long length, String message, String... args) {
		String patten = "^(.){1," + length + "}$";
		validatePatten(errors, field, patten, message, args);
	}
	
	//文本长度限制（一个中文字算两个字符）
	public static void validateInputLength(Errors errors, String field, long length, String message, String... args) {
		if (!errors.hasFieldErrors(field)) {
			Object value = errors.getFieldValue(field);
			if (value != null && cwUtils.notEmpty(value.toString())) {
				String val = value.toString().trim();
				
				if(CharUtils.getStringLength(val) > length){
					errors.rejectValue(field, null, coverArgsToArray(args), message);
				}
			}
		}
	}

	public static void validateMin(Errors errors, String field, String message, String... args) {
		Object value = errors.getFieldValue(field);
		if (value == null) {
			errors.rejectValue(field, null, coverArgsToArray(args), message);
		}
	}

	public static void validateMax(Errors errors, String field, String message, String... args) {
		Object value = errors.getFieldValue(field);
		if (value == null) {
			errors.rejectValue(field, null, coverArgsToArray(args), message);
		}
	}

	public static void validatePatten(Errors errors, String field, String patten, String message, String... args) {
		if (!errors.hasFieldErrors(field)) {
			Object value = errors.getFieldValue(field);
			if (value != null && cwUtils.notEmpty(value.toString())) {
				String val = value.toString().trim();
				Pattern p = Pattern.compile(patten);
				Matcher m = p.matcher(val);
				if (!m.find()) {
					errors.rejectValue(field, null, coverArgsToArray(args), message);
				}
			}
		}
	}

	public static Object[] coverArgsToArray(String... args) {
		if (args != null) {
			Vector<String> vector = new Vector<String>();
			for (String arg : args) {
				vector.add(arg);
			}
			return vector.toArray();
		} else {
			return null;
		}
	}
}