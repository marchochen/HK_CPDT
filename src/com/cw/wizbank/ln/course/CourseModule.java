package com.cw.wizbank.ln.course;

import java.io.IOException;
import java.sql.SQLException;

import com.cw.wizbank.ServletModule;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;

public class CourseModule extends ServletModule {
	public final static String MODULE_NAME = "course";
	CourseParam modParam;

	public CourseModule() {
		super();
		modParam = new CourseParam();
		param = modParam;
	}

	public void process() throws IOException, cwException, SQLException {
		try {
			if (this.prof == null || this.prof.usr_ent_id == 0) {
				throw new cwException(cwUtils.MESSAGE_SESSION_TIMEOUT);
			} else {
				if (modParam.getCmd().equalsIgnoreCase("prep") || modParam.getCmd().equalsIgnoreCase("prep_xml")) {
					resultXml = formatXML(Course.getCourseXml(con), MODULE_NAME);
				} else if (modParam.getCmd().equalsIgnoreCase("exec")) {
					Course.copyCourse(con, wizbini, prof, modParam.getItm_id_lsts(), modParam.getTcr_id_lsts());
					sysMsg = getErrorMsg("LN048", modParam.getUrl_success());
				} else {
					throw new cwException(cwUtils.MESSAGE_NO_RECOGNIZABLE_CMD + modParam.getCmd());
				}
			}
		} catch (cwSysMessage se) {
			try {
				con.rollback();
				msgBox(ServletModule.MSG_STATUS, se, modParam.getUrl_failure(), out);
			} catch (SQLException sqle) {
				out.println("SQL error: " + sqle.getMessage());
			}
		} catch (Exception e) {
			throw new cwException(e.getMessage(), e);
		}
	}
}