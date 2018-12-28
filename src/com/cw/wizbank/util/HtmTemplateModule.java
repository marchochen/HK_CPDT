package com.cw.wizbank.util;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;

import com.cw.wizbank.ServletModule;
import com.cw.wizbank.JsonMod.BaseParam;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.dbUtils;
import com.cwn.wizbank.utils.CommonLog;

import freemarker.core.Environment;
import freemarker.template.*;

public class HtmTemplateModule extends ServletModule implements Filter {
	private static final String HtmTemplateFolder = cwUtils.SLASH + "src" + cwUtils.SLASH + "htm_template";
	private static String AppnRoot = "";
    public Writer sw;
	
	public HtmTemplateModule() {
		super();
		param = new BaseParam();
	}
	
	public static void setAppnRoot(String appRoot) {
		AppnRoot = appRoot;
	}
	
	public static String getAppnRoot() {
	    if (AppnRoot.equals("") && wizbini != null) {
	        setAppnRoot(wizbini.getAppnRoot());
	    }
		return AppnRoot;
	}

	public void process() {
		try {
			if (param.getCmd().equalsIgnoreCase("process_all_htm_template")) {
				new HtmTemplate2Htm().processAllTemplate();
				out.println("all htm template have processed!");
			}
		} catch (Exception e) {
		    CommonLog.error(e.getMessage(),e);
			out.println(e.getMessage());
		}
	}

	public void destroy() {
		;
	}

	public void init(FilterConfig fConfig) throws ServletException {
		CommonLog.info("HtmTemplateModule int Start..");
		try {
			setAppnRoot(WizbiniLoader.getInstance(fConfig).getAppnRoot());
		} catch (cwException e) {
			CommonLog.error(e.getMessage(),e);
		};
		CommonLog.info("HtmTemplateModule int End..");
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		String requestURI;
		String[] tokens;
		requestURI = ((HttpServletRequest) request).getRequestURI();
		tokens = dbUtils.split(requestURI, "/");

		try {
			if (tokens.length > 1 && tokens[tokens.length - 1].endsWith(".htm")) {
				new HtmTemplate2Htm().processTemplate(tokens[tokens.length - 1]);
			}
		} catch (Exception e) {
			CommonLog.error(e.getMessage(),e);
		} finally {
			chain.doFilter(request, response);
		}
	}

	/**
	 * 转换Htm模板成Htm
	 */
	public class HtmTemplate2Htm {
		private final String HtmFolder = cwUtils.SLASH + "www" + cwUtils.SLASH + "htm";
		
		public String getHtmFolder() {
			return HtmFolder;
		}

		/**
		 * 转换选定的Htm模板
		 */
		public void processTemplate(String templateName) throws Exception {
			processTemplate2Htm(templateName);
		}

		/**
		 * 转换所有的Htm模板
		 */
		public void processAllTemplate() throws Exception {
			processTemplate2Htm(null);
		}

		private void processTemplate2Htm(String templateName) throws Exception {
			File templateFolder = new File(HtmTemplateModule.getAppnRoot() + HtmTemplateFolder);
			if (templateFolder.exists()) {
				Configuration cfg = getDefConfiguration(templateFolder);
				if (templateName != null && templateName.length() > 0) {
					File templateFile = new File(HtmTemplateModule.getAppnRoot() + HtmTemplateFolder, templateName);
					if (templateFile.exists()) {
						processT2H(cfg, templateName);
					} else {
						CommonLog.info("!!-----------------------------------!!file " + templateName + " does not Exist!");
					}
				} else {
					File[] templateLst = templateFolder.listFiles();
					for (int i = 0; i < templateLst.length; i++) {
						if (templateLst[i].isFile()) {
							processT2H(cfg, templateLst[i].getName());
							// cfg.clearTemplateCache();
						}
					}
				}
			} else {
				CommonLog.error("folder " + templateFolder + " does not Exist!");
				throw new Exception("folder " + templateFolder + " does not Exist!");
			}

		}

		/**
		 * 转换
		 */
		private void processT2H(Configuration cfg, String templateName) throws Exception {
			Template temp = cfg.getTemplate(templateName);
			temp.setEncoding("UTF-8");
			Map root = new HashMap();
			HtmTemplateModule freemarker = new HtmTemplateModule();
			root.put("freemarker", freemarker);
			File templateFile = new File(HtmTemplateModule.getAppnRoot() + HtmFolder, templateName);
			FileOutputStream out = new FileOutputStream(templateFile);
			
//			out.write(cwUtils.BOM_UTF8);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			OutputStreamWriter bytew = new OutputStreamWriter(bos);
			
			Environment env = temp.createProcessingEnvironment(root, bytew);
			env.process();
			env.include("share" + cwUtils.SLASH + "main.htm", null, true);
			bytew.flush();
			bytew.close();
			byte[] tmp = bos.toByteArray();
			int start = 0;
			for (int i = 0; i < tmp.length; i++) {
			    if (tmp[i] == 60) {
			        start = i;
			        break;
			    }
			}
			out.write(cwUtils.BOM_UTF8);
			Writer wr = new OutputStreamWriter(out, cwUtils.ENC_UTF);
			wr.write(new String(tmp, start, tmp.length - start));
			wr.close();
		}
	}
	
	/**
	 * 整理Htm代码，去掉换行前后的空格，单引号前加'\',替换变量方式
	 */
	public static String genJsFunc(String name, String content) {
		return name + ":function(a){return '" + content.trim().replaceAll("([\\n\\r]+[\\s]+)|([\\s]+[\\n\\r])", "").replaceAll("([^\\\\'])'", "$1\\\\'").replaceAll("\\{\\$(\\w*)\\}", "'+a[\'$1\']+'") + "'}";
	}
	
	/**
	 * 在xsl里调用此方法通过模板生成对应的Js对象字符串
	 */
	public static String genJsFuncToXsl(String templateName) throws Exception {
		StringBuffer result = new StringBuffer(512);
		String nameSpace = "htmspace";
		StringWriter writer = new StringWriter();
		File templateFolder = new File(HtmTemplateModule.getAppnRoot() + HtmTemplateFolder);
		if (templateFolder.exists()) {
			Configuration cfg = getDefConfiguration(templateFolder);
			if (templateName != null && templateName.length() > 0) {
				File templateFile = new File(HtmTemplateModule.getAppnRoot() + HtmTemplateFolder, templateName);
				if (templateFile.exists()) {
					Template temp = cfg.getTemplate(templateName);
					Map root = new HashMap();
					Environment env = temp.createProcessingEnvironment(root, writer);
					env.process();
					Environment.Namespace ns = env.getCurrentNamespace();
					ns = (Environment.Namespace)ns.get(nameSpace);
					TemplateModelIterator tempIter = ns.keys().iterator();
					String key = null;
					while (tempIter.hasNext()) {
						if (result.length() > 0) {
							result.append(",");
						}
						key = tempIter.next().toString();
						result.append(genJsFunc(key, ns.get(key).toString()));
					}
				}
			}
		} else {
			throw new Exception("folder " + templateFolder + " does not Exist!");
		}
		return result.toString();
	}
	
	/**
	 * 获取默认的 Configuration 对象
	 */
	public static Configuration getDefConfiguration(File templateFolder) throws Exception {
		Configuration cfg = null;
		cfg = new Configuration();
		cfg.setDirectoryForTemplateLoading(templateFolder);
		cfg.setObjectWrapper(new DefaultObjectWrapper());
		cfg.setDefaultEncoding(cwUtils.ENC_UTF);
		cfg.addAutoImport("htmspace", "share" + cwUtils.SLASH + "init.htm");
		cfg.addAutoImport("htmspace2", "share" + cwUtils.SLASH + "init2.htm");
		return cfg;
	}

	public static String getHtmTemplateFolder() {
		return HtmTemplateFolder;
	}
}
