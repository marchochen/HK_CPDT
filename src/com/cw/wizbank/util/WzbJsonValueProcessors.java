/**
 * Created on 2008-11-24
 */
package com.cw.wizbank.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

/**
 * @author terry
 *
 */
public class WzbJsonValueProcessors {

	public WzbJsonValueProcessors() {

	}

	public static class DefaultEsc4HtmlStringProcessor implements JsonValueProcessor {

		/*
		 * @see net.sf.json.processors.JsonValueProcessor#processArrayValue(java.lang.Object, net.sf.json.JsonConfig)
		 */
		public Object processArrayValue(Object value, JsonConfig arg1) {
			return process(value);
		}

		/*
		 * @see net.sf.json.processors.JsonValueProcessor#processObjectValue(java.lang.String, java.lang.Object, net.sf.json.JsonConfig)
		 */
		public Object processObjectValue(String key, Object value, JsonConfig arg2) {
			return process(value);
		}

		private Object process(Object value) {
			return cwUtils.esc4Json(value.toString());
		}
	}

	public static class NotEsc4HtmlStringProcessor implements JsonValueProcessor {

		public Object processArrayValue(Object value, JsonConfig jsonConfig) {
			return value;
		}

		public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {
			return value;
		}
	}

	/*
	 * output timestamp using Timestamp.toString().
	 */
	public static class DateJsonValueProcessor implements JsonValueProcessor {
		public Object processArrayValue(Object value, JsonConfig jsonConfig) {
			return process(value);
		}

		public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {
			return process(value);
		}

		private Object process(Object value) {
			if (value instanceof Timestamp)
				return cwUtils.format((Timestamp) value);
			else if (value instanceof Date) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				return sdf.format(value);
			} else if (value == null)
				return "";
			else
				return value.toString();
		}
	}

	public static class FloatJsonValueProcessor implements JsonValueProcessor {
		public Object processArrayValue(Object value, JsonConfig jsonConfig) {
			return process(value);
		}

		public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {
			return process(value);
		}

		private Object process(Object value) {
			if (value instanceof Float) {
				return value.toString();
			}
			return null;
		}
	}
}
