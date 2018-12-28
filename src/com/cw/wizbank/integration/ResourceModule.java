package com.cw.wizbank.integration;
import java.io.*;
import java.sql.*;

import com.cw.wizbank.ServletModule;
import com.cw.wizbank.util.*;

/**
 * Servlet module to perform resource integration.
 */
public class ResourceModule extends ServletModule {

	public final static String RES_TYPE_WCT = "WCT";
	public final static String RES_TYPE_MC = "MC";
	public final static String RES_TYPE_FB = "FB";
	public final static String RES_TYPE_TF = "TF";

	public void process() throws SQLException, IOException, cwException {

		ResourceReqParam urlp = null;
		urlp =
			new ResourceReqParam(
				con,
				wizbini,
				request,
				clientEnc,
				static_env.ENCODING);
		if (bMultipart) {
			urlp.setMultiPart(multi);
		}
		urlp.common();
		PrintWriter out = response.getWriter();

		if (urlp.cmd.equalsIgnoreCase("ins_res")) {
			try {
				if (urlp.type.equalsIgnoreCase(WCTParser.TYPE)) {
					urlp.WCTParam();
				} else if (urlp.type.equalsIgnoreCase(MCParser.TYPE)) {
					urlp.MCParam();
				} else if (urlp.type.equalsIgnoreCase(TFParser.TYPE)) {
					urlp.TFParam();
				} else if (urlp.type.equalsIgnoreCase(FBParser.TYPE)) {
					urlp.FBParam();
				} else {
					throw new cwException(
						ResourceUtils.getFailureCode(
							ResourceUtils.CODE_RES_TYPE,
							ResourceUtils.ERROR_NOT_EXIST));
				}
			} catch (cwException e) {
				out.print(e.getMessage());
				con.rollback();
				return;
			}

			//Checks request from trusted site
			String host = request.getRemoteHost();
			String referer = request.getHeader("REFERER");
			if (!ResourceUtils
				.isTrustDomain(con, host, referer, urlp.resParser.site_id)) {
				out.print(
					ResourceUtils.getFailureCode(
						ResourceUtils.CODE_SITE_ID,
						ResourceUtils.ERROR_NOT_EXIST));
				con.rollback();
				return;
			}

			try {
				urlp.resParser.parse(
					con,
					ResourceUtils.getProf(
						con,
						urlp.resParser.site_id,
						wizbini));
			} catch (cwException e) {
				out.print(e.getMessage());
				con.rollback();
				return;
			}
			out.print(ResourceUtils.getSuccessCode());
			con.commit();
			return;
		} else {
			out.println(
				ResourceUtils.getFailureCode(
					ResourceUtils.CODE_CMD_VALUE,
					ResourceUtils.ERROR_NOT_EXIST));
			con.commit();
			return;
		}

	}

}
