<%@page import="java.sql.*"%>
<%!
	public static void close(Connection con) {
		try {
			con.close();
		} catch (SQLException sqle) {
			System.out.println("Database colse error");
		}
	}
%>