package com.cw.wizbank.profession;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.SQLException;

import javax.servlet.http.HttpSession;

import com.cw.wizbank.ServletModule;
import com.cw.wizbank.qdb.UserGrade;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cwn.wizbank.utils.CommonLog;

public class ProfessionModule extends ServletModule {
	private static String END_XML = "_xml";
	public static long PSILENGTH = 10;	//预留职业发展学习必修数量
	
	public void process() throws SQLException, cwException, IOException {
		if (prof == null) {
			response.sendRedirect(static_env.URL_SESSION_TIME_OUT);
		}
		PrintWriter out = response.getWriter();
		ProfessionReqParam urlp = new ProfessionReqParam(request, clientEnc, static_env.ENCODING);
		HttpSession sess = request.getSession(true);
		String profile = dbRegUser.getUserAttributeInfoXML(wizbini, prof.root_id);
		if (bMultipart) {
			urlp.setMultiPart(multi);
		}
		urlp.common();
		try {
			if (urlp.cmd == null) {
				throw new cwException("Invalid Command");
			} else if (urlp.cmd.equalsIgnoreCase("GET_PFS_TREE")
					|| urlp.cmd.equalsIgnoreCase("GET_PFS_TREE_XML")) {
				String result = formatXML("", profile, "profession");
				outPutView(out, urlp, result);
			} else if (urlp.cmd.equalsIgnoreCase("PFS_TOP_NAV")
					|| urlp.cmd.equalsIgnoreCase("PFS_TOP_NAV_XML")) {

				String result = formatXML("", profile, "profession");

				outPutView(out, urlp, result);
			} else if (urlp.cmd.equalsIgnoreCase("PFS_NAV")
					|| urlp.cmd.equalsIgnoreCase("PFS_NAV_XML")) {
				String xml = Profession.getAllAsXml(con);
				String result = formatXML(xml, profile, "profession");

				outPutView(out, urlp, result);
			} else if (urlp.cmd.equalsIgnoreCase("PFS_INFO")
					|| urlp.cmd.equalsIgnoreCase("PFS_INFO_XML")) {
				String result = formatXML("", profile, "profession");

				outPutView(out, urlp, result);
			} else if (urlp.cmd.equalsIgnoreCase("GET_PFS")
					|| urlp.cmd.equalsIgnoreCase("GET_PFS_XML")) {
//				StringBuffer xml = urlp.pfs.asXML(con);
				String result = formatXML("", profile, "profession");

				outPutView(out, urlp, result);
			} else if (urlp.cmd.equalsIgnoreCase("PFS_INS_UPD_PREP")
					|| urlp.cmd.equalsIgnoreCase("PFS_INS_UPD_PREP_XML")) {
				urlp.getPfsParam();
				StringBuffer xml = new StringBuffer();
				if(urlp.pfs.pfs_id > 0) {
					xml.append("<pfs_id>").append(urlp.pfs.pfs_id).append("</pfs_id>");
					Profession pfs = Profession.get(con, urlp.pfs.pfs_id);
					xml.append(pfs.asXml());
					xml.append(UserGrade.getGradesAsXmlForProfession(con));
					xml.append("<psi_list>");
					ProfessionItem psi = ProfessionItem.getByPfsid(con, urlp.pfs.pfs_id);
					String[] ugr_id_lst = new String[]{};
					String[] itm_id_lst = new String[]{};
					if(psi != null) {
						if(psi.psi_ugr_id != null) {
							ugr_id_lst = psi.psi_ugr_id.split("\\|");
						}
						if(psi.psi_itm != null) {
							itm_id_lst = psi.psi_itm.split("\\|");
						}
					}
					for(int i=0;i<PSILENGTH;i++) {
						if(ugr_id_lst.length > i || itm_id_lst.length > i) {
							xml.append("<psi>");
							xml.append("<index>").append(i+1).append("</index>");
							xml.append("<psi_ugr_id>").append(ugr_id_lst[i]).append("</psi_ugr_id>");
							xml.append(ProfessionItem.getItmLstAsXml(con, itm_id_lst[i].split("~")));
							xml.append("</psi>");
						} else {
							xml.append("<psi/>");
						}
					}
					xml.append("</psi_list>");
					
				}
				String result = formatXML(xml.toString(), profile, "profession");

				outPutView(out, urlp, result);
			} else if (urlp.cmd.equalsIgnoreCase("PFS_INS_UPD_EXEC")) {
				urlp.getPfsParam();
				try {
					if(urlp.pfs.pfs_id > 0) {
						urlp.pfs.pfs_update_usr_id = prof.usr_ent_id;
						urlp.pfs.pfs_update_time = cwSQL.getTime(con);
						urlp.pfs.upd(con);
						ProfessionItem.delByPfsid(con, urlp.pfs.pfs_id);
						ProfessionItem psi = new ProfessionItem();
						psi.psi_pfs_id = urlp.pfs.pfs_id;
						psi.psi_ugr_id = urlp.psi_ugr_id_lst;
						psi.psi_itm = urlp.psi_itm_id_lst;
						if(!"".equals(psi.psi_ugr_id) && !"".equals(psi.psi_itm)) {
							psi.ins(con);
						}
					} else {
						urlp.pfs.pfs_create_time = cwSQL.getTime(con);
						urlp.pfs.pfs_create_usr_id = prof.usr_ent_id;
						urlp.pfs.ins(con);
					}
					con.commit();
					cwSysMessage e = new cwSysMessage("ENT003");
					msgBox(MSG_STATUS, e, urlp.url_success, out);
				} catch (Exception e) {
					con.rollback();
//					msgBox(MSG_ERROR, con, e, urlp, out);
					CommonLog.error(e.getMessage(),e);
					return;
				}
			} else if (urlp.cmd.equalsIgnoreCase("PFS_DEL")
					|| urlp.cmd.equalsIgnoreCase("PFS_DEL_XML")) {
				urlp.getPfsParam();
				ProfessionItem.delByPfsid(con, urlp.pfs.pfs_id);
				urlp.pfs.del(con);
				
				con.commit();
				cwSysMessage e = new cwSysMessage("ENT001");
				msgBox(MSG_STATUS, e, urlp.url_success, out);
			}
		} catch (Exception saxe) {
			con.rollback();
			throw new IOException(saxe.getMessage());
		}
	}

	private void outPutView(Writer out, ProfessionReqParam urlp, String xml) throws cwException {
		if (urlp.cmd.endsWith(END_XML)) {
			static_env.outputXML((PrintWriter) out, xml);
		} else {
			generalAsHtml(xml, (PrintWriter) out, urlp.stylesheet);
		}
	}
}