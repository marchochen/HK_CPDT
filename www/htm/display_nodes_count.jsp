<%@page contentType="text/html; charset=GB2312" %>
<%@ page import="com.cw.wizbank.ae.aeWorkFlowCache" %>
<%@ page import="com.cw.wizbank.ae.aeWorkFlow" %>
<%@ page import="com.cw.wizbank.ae.Template" %>
<%@ page import="com.cw.wizbank.batch.batchUtil.BatchUtils" %>
<%@ page import="com.cw.wizbank.util.cwSQL" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.util.Hashtable" %>
<%@ page import="org.xml.sax.InputSource" %>
<%@ page import="org.xml.sax.SAXException" %>
<%@ page import="org.apache.xerces.parsers.DOMParser" %>
<%@ page import="org.w3c.dom.Node" %>
<%@ page import="org.w3c.dom.NodeList" %>
<%@ page import="org.w3c.dom.Document" %>
<%@ page import="org.w3c.dom.Element" %>
<%@ page import="com.cw.wizbank.util.cwException" %>
<%@ page import="com.cw.wizbank.ae.aeWorkFlowEvent" %>
<%@ page import="com.cw.wizbank.qdb.dbUtils" %>

<html>
  <head>
   <title>reload_workflowxml</title>
  </head>
    <body>
    <form name="workflow" method="get">
    <p>reload workflow xml</p>
    <%
	Connection con = BatchUtils.openDB("D:\\ada\\wwwdev\\sac\\trunk");
    out.println("Start reload:" + "<br>");

    out.println("In aeWorkFlowCache"+"<br>");
    out.println(((Node)(aeWorkFlowCache.cachedWorkFlowXML.get(aeWorkFlowCache.WORKFLOW_NODE + "15"))).getChildNodes().getLength()+"<br>");
    out.println(((Node)(aeWorkFlowCache.cachedWorkFlowXML.get(aeWorkFlowCache.EVENT_TRIGGER_NODE + "15"))).getChildNodes().getLength()+"<br>");
    out.println(((Node)(aeWorkFlowCache.cachedWorkFlowXML.get(aeWorkFlowCache.QUEUE_RULE_NODE + "15"))).getChildNodes().getLength()+"<br>");

    out.println("In aeWorkFlowEvent"+"<br>");
    aeWorkFlowEvent wfe = new aeWorkFlowEvent(con, "", 15);
    out.println(wfe.workFlowNode.getChildNodes().getLength()+"<br>");
    out.println(wfe.eventTriggerNode.getChildNodes().getLength()+"<br>");
    out.println(wfe.queueRuleNode.getChildNodes().getLength()+"<br>");
    out.println(aeWorkFlowCache.cachedWorkFlowXML);
    out.println(aeWorkFlowCache.cached_process_xml);
	out.println("Finished!");
    %>
</body>
</html>
