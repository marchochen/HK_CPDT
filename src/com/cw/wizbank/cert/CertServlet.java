package com.cw.wizbank.cert;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.util.LangLabel;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;

public class CertServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static WizbiniLoader wizbini = null;
	private Connection con = null;

	public CertServlet() {
	}

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		try {
			wizbini = WizbiniLoader.getInstance(config);
		} catch (Exception e) {
			CommonLog.error(e.getMessage(),e);
			throw new ServletException(e.getMessage());
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			cwSQL sqlCon = new cwSQL();
			sqlCon.setParam(wizbini);
			con = sqlCon.openDB(false);

			long cert_id = getLongParameter(request, "cert_id");
			long itm_id = getLongParameter(request, "itm_id");
			long tkh_id = getLongParameter(request, "tkh_id");
			String lan = request.getParameter("lan");
			if (lan == null && lan.length() <= 0) {
				lan = wizbini.cfgSysSkinList.getDefaultLang();
			}

			if (cert_id > 0) {
				CertificateBean certbean = CertificateDao.getCertByID(con, cert_id);
				String savePath = wizbini.getFileCertImgDirAbs() + dbUtils.SLASH + cert_id  + cwUtils.SLASH +  certbean.getCfc_img();
				String imgpath = cwUtils.replaceSlashToHttp(savePath);

				HashMap<String, String> outpdf = new HashMap<String, String>();
				outpdf.put("imgpath", imgpath);
				
/*				 cert_id = 16;
				 itm_id = 153;
				 tkh_id = 367;*/
				// preview
				if (tkh_id <= 0) {
					outpdf = getMapPDF(lan, cert_id, itm_id, tkh_id);
					outPDF(response, imgpath, outpdf, false);
				}
				// download
				if (cert_id > 0 && itm_id > 0 && tkh_id > 0) {
					outpdf = getMapPDF(lan, cert_id, itm_id, tkh_id);
					outPDF(response, imgpath, outpdf, true);
				}
			}
			con.commit();
		} catch (Exception e1) {
			e1.printStackTrace();
			try {
				if (con != null && !con.isClosed()) {
					con.rollback();
				}
			} catch (SQLException e) {
				CommonLog.error(e.getMessage(),e);
			}
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					CommonLog.error(e.getMessage(),e);
				}
			}
		}
	}

	public HashMap<String,String> getMapPDF(String lan, long cert_id, long itm_id, long tkh_id) throws SQLException {

		String trainTitle = LangLabel.getValue(lan, "cert_aeitem_train_name"); // 培训中心名称
		String courseTitle = LangLabel.getValue(lan, "cert_aeitem_name"); // 证书名称
		String userName = LangLabel.getValue(lan, "cert_learner_name");
		String text = LangLabel.getValue(lan, "cert_content"); // This is to certify that 
		String text_2 = LangLabel.getValue(lan, "cert_content_2"); //  has Successfully completed 
		String text_3 = LangLabel.getValue(lan, "cert_content_3"); // on
		String certCode = LangLabel.getValue(lan, "cert_code");
		String certTitle = LangLabel.getValue(lan, "cert_name");
		String itmCode = "";
		DateFormat fmt = new SimpleDateFormat("dd MMM yyyy",Locale.ENGLISH);
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat format2 = new SimpleDateFormat("yyyyMMddHHmmss");
		Timestamp t = cwSQL.getTime(con);
		String dateText = "";
		HashMap<String, String> outpdf = new HashMap<String, String>();
		CertificateDao certDAO = new CertificateDao();
		//证书名称和编号
		if (cert_id > 0) {
			CertificateBean cfb = certDAO.getCertByID(con, cert_id);
			certCode = cfb.getCfc_code();
			certTitle = cfb.getCfc_title();
		}
		// 课程名以及培训中心名称
		if(itm_id >0 ) {
			String[] title = certDAO.getItemTitle(con, itm_id);
			courseTitle = title[0];
			trainTitle = title[1];
			itmCode = title[2];
		}
		// 预览
		if (tkh_id <= 0) {
			dateText = fmt.format(t);
			if(itmCode != ""){
				courseTitle = courseTitle+"("+itmCode+")";//课程名称和课程编号
			}
		}
		// download
		if (cert_id > 0 && itm_id > 0 && tkh_id > 0) {
			courseTitle = courseTitle+"("+itmCode+")";//课程名称和课程编号
			String[] entity = new String[6];
			entity = certDAO.getCertBytkhID(con, tkh_id);
			String endDate = entity[4];
			userName = entity[3];
//			outpdf.put("certcoredate", format2.format(t));//下载证书需要带时间日期的可以用这段代码
			Date dt = null;
			try {
				dt = format1.parse(endDate);
				dateText = fmt.format(dt);
			} catch (ParseException e) {
				CommonLog.error(e.getMessage(),e);
			}
		}
		outpdf.put("certtitle", certTitle);
		outpdf.put("certcore", certCode);
		outpdf.put("note", text);
		outpdf.put("participantName", userName);
		outpdf.put("note2", text_2);
		outpdf.put("courseTitle", courseTitle);
		outpdf.put("note3", text_3);
		outpdf.put("dateText", dateText);
		return outpdf;
	}
	

	public void outPDF(HttpServletResponse response, String imgpath, HashMap<String, String> outpdf, boolean isDownload) {
		response.setContentType("application/pdf");
		if (isDownload == true) {
			response.setHeader("Content-disposition", "attachment; filename=" + "certificate.pdf");
		}
		try {
			CertPDF pdf = new CertPDF(wizbini);
			OutputStream os = response.getOutputStream();
			pdf.CertTemplate(os, response, imgpath, outpdf, isDownload);
			os.flush();
			os.close();
		} catch (Exception e) {
			CommonLog.error(e.getMessage(),e);
		}
	}

	protected long getLongParameter(HttpServletRequest request, String paraname) throws cwException {
		long value;
		String var = null;
		try {
			var = request.getParameter(paraname);
			if (var != null && var.length() > 0)
				value = Long.valueOf(var).longValue();
			else
				value = 0;

			return value;
		} catch (Exception e) {
			throw new cwException("Parameter format exception : name=" + paraname + ",value=" + var);
		}
	}
}
