<%@page import="java.sql.*"%>
<%@page import="java.io.*"%>
<%@page import="com.cw.wizbank.ae.aeTreeNode"%>
<%@page import="com.cw.wizbank.qdb.loginProfile"%>
<%@page import="com.cw.wizbank.qdb.qdbAction"%>
<%@page import="com.cw.wizbank.qdb.qdbEnv"%>
<%@page import="com.cw.wizbank.config.WizbiniLoader"%>
<%@page import="com.cw.wizbank.util.cwSQL"%>
<%@page import="com.cw.wizbank.util.cwPagination"%>

<%response.setHeader("Pragma","no-cache");%> 
<%response.setHeader("Cache-Control","no-store");%> 
<%response.setDateHeader("Expires",-1);%> 
<%
Connection con = null;
WizbiniLoader wizbini = qdbAction.getWizbini();
qdbEnv env = (qdbEnv) config.getServletContext().getAttribute(WizbiniLoader.SCXT_STATIC_ENV);
//ClassCastException will be thrown if a new class loader is used
//e.g. when a JSP is first loaded, a new class loader will be used
//so use a wrapper class to read the loginProfile and put it back to session
loginProfile prof = null;
try {
    prof = (loginProfile)session.getAttribute(qdbAction.AUTH_LOGIN_PROFILE);
}
catch(ClassCastException e) {
    prof = new loginProfile();
    prof.readSession(session);
}
try {
    cwSQL sqlCon = new cwSQL();
    sqlCon.setParam(wizbini);
    con = sqlCon.openDB(false);
} catch (Exception e) {
}

%> 
