package com.cw.wizbank.codetable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import com.cw.wizbank.qdb.dbMessage;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;

public class CodeTable {
    /**
     * CLOB column
     * Table:       CodeTable
     * Column:      ctb_xml
     * Nullable:    YES
     */
    //search page size
    public static final int CTB_SEARCH_PAGE_SIZE = 10;

    //get all pages when lookup
    public static final int CTB_ALL_PAGES = -100;

    //search key
    public static final String CTB_TYPE = "CTB_TYPE";
    public static final String CTB_ID_LST = "CTB_ID_LST";
    public static final String CTB_S_TIMESTAMP = "CTB_S_TIMESTAMP";

    //System Message
    public static final String CTB_RECORD_EXIST = "UGR001";
	public static final String CTB_TYPE_NOT_EXIST = "CTB001";
	public static final String CTB_TYPE_RECORD_EXIST = "CTB002";
	public static final String 	INVALID_OPERATION = "AEQM05";
	public static final String INVALID_TIMESTAMP_MSG = "MSG001";
	public static final String RECORD_NOT_EXIST = "GEN005";

    //DataBase fields
    public String ctb_type;
    public String ctb_id;
    public String ctb_title;
    public String ctb_xml;
    public Timestamp ctb_create_timestamp;
    public String ctb_create_usr_id;
    public Timestamp ctb_upd_timestamp;
    public String ctb_upd_usr_id;

    //SQL Statements
    static final String DEL_CODE_TABLE_SQL = " Delete From CodeTable "
                                          + " Where ctb_type = ? "
                                          + " And ctb_id = ? ";

    static final String GET_CODE_TABLE_SQL = " Select ctb_title, ctb_xml "
                                           + "       ,ctb_create_timestamp, ctb_create_usr_id "
                                           + "       ,ctb_upd_timestamp, ctb_upd_usr_id "
                                           + " From CodeTable "
                                           + " Where ctb_type = ? "
                                           + " And ctb_id = ? ";
    private static final String sql_get_code_type =
        "SELECT ctp_id, ctp_title, ctp_sys_ind FROM CodeType ";
	private static final String sql_get_code_data_list =
			"SELECT ctb_title, ctb_id FROM codetable WHERE ctb_type = ? ";
	private static final String sql_get_usr_name =
				"SELECT usr_display_bil FROM reguser WHERE usr_id = ? ";
	private static final String sql_get_code_data_detail =
			"select ctb_id,ctb_title,ctb_type,ctb_create_timestamp,create_user=c.usr_display_bil,"
		  + " ctb_upd_timestamp,update_user=u.usr_display_bil from codetable,reguser c,reguser u"
		  +	"  where ctb_id = ? and ctb_type = ? "
		  + "  and c.usr_id = ctb_create_usr_id  "
		  + "  and u.usr_id = ctb_upd_usr_id  " ;
    private static final String sql_get_code_type_specific =
        "SELECT ctp_title, ctp_sys_ind FROM CodeType WHERE ctp_id = ? ";

    private static final String sql_ins_code_type =
        "INSERT INTO CodeType (ctp_id, ctp_sys_ind, ctp_title, ctp_create_timestamp, ctp_create_usr_id, ctp_update_timestamp, ctp_update_usr_id) values (?, ?, ?, ?, ?, ?, ?)";
    private static final String sql_upd_code_type =
        "UPDATE CodeType SET ctp_sys_ind = ? , ctp_title = ? , ctp_update_timestamp = ? , ctp_update_usr_id = ? WHERE ctp_id = ? ";

    private static final String CTB_TYPE_SUPPLIER = "SUPPLIER";

    public static String lookUp(Connection con, HttpSession sess, String ctb_type, String ctb_id
                               ,String ctb_title, String orderBy, String sortOrder, boolean exact
                               ,int page, Timestamp in_timestamp, boolean showTimestamp)
        throws SQLException ,cwSysMessage {
          String xml = "";
          if(ctb_type == null && ctb_id == null && ctb_title == null && in_timestamp == null)
            return xml;

          if(page == 0) //default page is page 1
            page = 1;
          if(ctb_type == null)
            ctb_type = "";
          if(ctb_id == null)
            ctb_id = "";
          if(ctb_title == null)
            ctb_title = "";
          if(orderBy == null || orderBy.length() == 0)
            orderBy = "ctb_id";
          if(sortOrder == null || sortOrder.length() == 0)
            sortOrder = "asc";

          String ctb_id_lst;
          boolean fromSession;
          Timestamp sess_timestamp = null;

          if(sess == null ) {
            fromSession = false;
          }
          else {
            sess_timestamp = (Timestamp) sess.getAttribute(CTB_S_TIMESTAMP);
            if (sess_timestamp != null && sess_timestamp.equals(in_timestamp)) {
                ctb_id_lst = (String)sess.getAttribute(CTB_ID_LST);
                ctb_type = (String)sess.getAttribute(CTB_TYPE);
                fromSession = true;
            } else {
                fromSession = false;
            }
          }

          CodeTable ctb;
          StringBuffer resultBuf = new StringBuffer(2500);
          StringBuffer SQLBuf = new StringBuffer(300);
          String SQL;
          PreparedStatement stmt;
          int count=0;
          ResultSet rs;

          if(fromSession) {
            ctb_id_lst = (String) sess.getAttribute(CTB_ID_LST);
            ctb_type = (String) sess.getAttribute(CTB_TYPE);
            SQLBuf.append(" Select ctb_type, ctb_id " );
            SQLBuf.append(" From CodeTable ");
            SQLBuf.append(" Where ctb_type = ? ");
            SQLBuf.append(" And ctb_id in ").append(ctb_id_lst);
            SQLBuf.append(" Order by ");
            SQLBuf.append(orderBy);
            SQLBuf.append(" " + sortOrder);
            SQL = new String(SQLBuf);
            stmt = con.prepareStatement(SQL);
            stmt.setString(1, ctb_type);
            rs = stmt.executeQuery();
          }
          else {
            //-----------------------//
            SQLBuf.append(" Select ctb_type, ctb_id ");
            SQLBuf.append(" From CodeTable ");
            SQLBuf.append(" Where ");
            SQLBuf.append(" 1=1 ");
            if(ctb_type.length() != 0)
                SQLBuf.append(" And ctb_type = ? ");
            if(ctb_id.length() != 0)
                SQLBuf.append(" And lower(ctb_id) like ? ");
            if(ctb_title.length() != 0)
                SQLBuf.append(" And lower(ctb_title) like ? ");
            SQLBuf.append(" Order by ");
            SQLBuf.append(orderBy);
            SQLBuf.append(" " + sortOrder);

            SQL = new String(SQLBuf);
            stmt = con.prepareStatement(SQL);
            int index = 1;
            if(ctb_type.length() != 0)
                stmt.setString(index++, ctb_type);
            if(exact) {
                if(ctb_id.length() != 0)
                    stmt.setString(index++, ctb_id);
                if(ctb_title.length() != 0)
                    stmt.setString(index++, ctb_title);
            }
            else {
                if(ctb_id.length() != 0)
                    stmt.setString(index++, ctb_id.toLowerCase() + "%");
                if(ctb_title.length() != 0)
                    stmt.setString(index++, ctb_title.toLowerCase() + "%");
            }
            rs = stmt.executeQuery();
            sess_timestamp = cwSQL.getTime(con);
          }

          StringBuffer ctb_id_lstBuf = new StringBuffer(100);
          ctb_id_lstBuf.append("(''");
          while(rs.next()) {
              ctb = new CodeTable();
              ctb.ctb_type = rs.getString("ctb_type");
              ctb.ctb_id = rs.getString("ctb_id");
              ctb_id_lstBuf.append(",'").append(ctb.ctb_id).append("'");
              if (page == CTB_ALL_PAGES || (count >= CTB_SEARCH_PAGE_SIZE*(page-1) && count < CTB_SEARCH_PAGE_SIZE*page)) {
                  ctb.get(con);
                  resultBuf.append(ctb.asXML(con,showTimestamp)).append(cwUtils.NEWL);
              }
              count++;
          }
          ctb_id_lstBuf.append(")");
          stmt.close();
          if(sess != null) {
            sess.setAttribute(CTB_TYPE, ctb_type);
            sess.setAttribute(CTB_ID_LST, ctb_id_lstBuf.toString());
            sess.setAttribute(CTB_S_TIMESTAMP, sess_timestamp);
          }

          // get the details of the input code type, all blank if code type not exist
          String typeTitle;
          String typeSys;
          int col = 0;
          stmt = con.prepareStatement(sql_get_code_type_specific);
          col = 1;
          stmt.setString(col++, ctb_type);
          rs = stmt.executeQuery();
          if (rs.next()) {
            col = 1;
            typeTitle = rs.getString(col++);
            if (rs.getBoolean(col++))
                typeSys = "yes";
            else
                typeSys = "no";
          } else {
            typeTitle = "";
            typeSys   = "";
          }
          stmt.close();

          StringBuffer xmlBuf = new StringBuffer(2500);
          xmlBuf.append("<codes orderby=\"").append(orderBy).append("\"");
          xmlBuf.append(" sortorder=\"").append(sortOrder).append("\"");
          xmlBuf.append(" page_size=\"").append(CTB_SEARCH_PAGE_SIZE).append("\"");
          if(page == CTB_ALL_PAGES)
              xmlBuf.append(" cur_page=\"").append("\"");
          else
              xmlBuf.append(" cur_page=\"").append(page).append("\"");
          xmlBuf.append(" total=\"").append(count).append("\"");
          xmlBuf.append(" type_id=\"").append(dbUtils.esc4XML(ctb_type)).append("\"")
                .append(" type_title=\"").append(dbUtils.esc4XML(typeTitle)).append("\"")
                .append(" type_system=\"").append(typeSys).append("\"");
          xmlBuf.append(" search_timestamp=\"").append(cwUtils.escNull(sess_timestamp)).append("\">");
          xmlBuf.append(resultBuf.toString());
          xmlBuf.append("</codes>").append(cwUtils.NEWL);
          xml = xmlBuf.toString();
          return xml;
    }


    //format the object into XML
    public String asXML(Connection con,  boolean  showTimestamp)
    throws SQLException  ,cwSysMessage{
        StringBuffer xmlBuf = new StringBuffer(2500);
        xmlBuf.append("<code type=\"").append(dbUtils.esc4XML(ctb_type)).append("\"");
        xmlBuf.append(" id=\"").append(dbUtils.esc4XML(ctb_id)).append("\"");;
        xmlBuf.append(" title=\"").append(dbUtils.esc4XML(cwUtils.escNull(ctb_title))).append("\">");
        xmlBuf.append(cwUtils.NEWL);

        if(showTimestamp)  {
            xmlBuf.append("<creator usr_id=\"").append(ctb_create_usr_id).append("\"");
            xmlBuf.append(" timestamp=\"").append(ctb_create_timestamp).append("\">");
            xmlBuf.append(dbUtils.esc4XML(dbRegUser.getUserName(con,ctb_create_usr_id)));
            xmlBuf.append("</creator>");
            xmlBuf.append(dbUtils.NEWL);

        xmlBuf.append("<last_updated usr_id=\"").append(ctb_upd_usr_id).append("\"");
        xmlBuf.append(" timestamp=\"").append(ctb_upd_timestamp).append("\">");
        xmlBuf.append(dbUtils.esc4XML(dbRegUser.getUserName(con,ctb_upd_usr_id)));
        xmlBuf.append("</last_updated>");
        xmlBuf.append(dbUtils.NEWL);
        }
        xmlBuf.append("</code>").append(dbUtils.NEWL);

        return xmlBuf.toString();
    }

    //retrieve data into object from DB
    public void get(Connection con) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(GET_CODE_TABLE_SQL);
        stmt.setString(1, ctb_type);
        stmt.setString(2, ctb_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            ctb_title = rs.getString("ctb_title");
            ctb_xml = cwSQL.getClobValue(rs, "ctb_xml");
            ctb_create_timestamp = rs.getTimestamp("ctb_create_timestamp");
            ctb_create_usr_id = rs.getString("ctb_create_usr_id");
            ctb_upd_timestamp = rs.getTimestamp("ctb_upd_timestamp");
            ctb_upd_usr_id = rs.getString("ctb_upd_usr_id");
        }
        else
            throw new SQLException("Cannot find Code Table, ctb_type = " + ctb_type + " ctb_id = " + ctb_id);

        stmt.close();
    }

    //delete a record from CodeTable
    //check the last update timestamp
    public void del(Connection con, Timestamp upd_timestamp) throws SQLException, cwSysMessage {
        //check update timestamp
        if(!isLastUpd(con, upd_timestamp))
            throw new cwSysMessage(dbMessage.INVALID_TIMESTAMP_MSG);

        //del(con);
    }

    //delete a record from CodeTable
    public void del(Connection con, String _ctb_type,String _ctb_id) throws SQLException, cwSysMessage{
        PreparedStatement stmt = con.prepareStatement(DEL_CODE_TABLE_SQL);
        stmt.setString(1, _ctb_type);
        stmt.setString(2, _ctb_id);
        int row = stmt.executeUpdate();
        stmt.close();
        con.commit();
        if(row == 0)
            throw new cwSysMessage(RECORD_NOT_EXIST);
        if(row > 1)
            throw new SQLException("More than 1 Code Table have ctb_type = " + _ctb_type + " ctb_id = " + _ctb_id);
    }

    //update a record in CodeTable
    //check last update timestamp
    public void upd(Connection con, String usr_id,String _ctb_id,String _ctb_id_bef,String _ctb_type,String _ctb_type_bef,String _ctb_title,boolean isTypeM,boolean isIdM,Timestamp upd_timestamp)
      throws SQLException, cwSysMessage, cwException {
        //check ctb_type
		ctb_id = _ctb_id;
		ctb_type = _ctb_type;
		Timestamp curTime;
		try {curTime = dbUtils.getTime(con);
		}catch(qdbException e) {throw new SQLException(e.getMessage());
		} 
		ctb_upd_timestamp = curTime;
		ctb_upd_usr_id = usr_id;   
		if(!isLastUpd(con,_ctb_id_bef,_ctb_type_bef,upd_timestamp))
			throw new cwSysMessage(INVALID_TIMESTAMP_MSG); 
		if (isTypeM){
			if(!isTypeExist(con,_ctb_type)){
				throw new cwSysMessage(CTB_TYPE_NOT_EXIST);
				//throw new SQLException("Cannot find Code Type. ctb_type = " + _ctb_type);
			} else {
				if(isRecordExist(con,_ctb_type,_ctb_id)){
					throw new cwSysMessage(CTB_TYPE_RECORD_EXIST);
					//throw new SQLException("The Code : ctb_type = " + _ctb_type+" ctb_id = "+_ctb_id+" alread exist !!");	
				} else {
					upd(con,_ctb_type,_ctb_type_bef,_ctb_id,_ctb_id_bef,_ctb_title);
					return;
				}
			}				
		}//check ctb_id 
		else {
			if (isIdM){
				if(isRecordExist(con,_ctb_type,_ctb_id)){
					throw new cwSysMessage(CTB_RECORD_EXIST);
					//throw new SQLException("The Code : ctb_type = " + _ctb_type+" ctb_id = "+_ctb_id+" alread exist !!");
				} else {
					upd(con,_ctb_type,_ctb_type_bef,_ctb_id,_ctb_id_bef,_ctb_title);
					return;
				}
			}
		} 				
        //check update timestamp
        //if(!isLastUpd(con, upd_timestamp))
        //    throw new cwSysMessage(dbMessage.INVALID_TIMESTAMP_MSG);    
        upd(con,_ctb_type,_ctb_type_bef,_ctb_id,_ctb_id_bef,_ctb_title);
    }

    //update a record in CodeTable
    //ctb_id and ctb_type are keys that cannot be updated
    public void upd(Connection con,String _ctb_type,String _ctb_type_bef,String _ctb_id,String _ctb_id_bef,String _ctb_title) throws SQLException {
        StringBuffer SQLBuf = new StringBuffer(300);

        SQLBuf.append(" Update CodeTable Set ");
		SQLBuf.append(" ctb_id = ? ");
		SQLBuf.append(" ,ctb_type = ? ");
		SQLBuf.append(" ,ctb_title = ? ");
        SQLBuf.append(" ,ctb_upd_timestamp = ? ");
        SQLBuf.append(" ,ctb_upd_usr_id = ? ");
        SQLBuf.append(" Where ctb_type = ? ");
        SQLBuf.append(" And ctb_id = ? ");

        String SQL = new String(SQLBuf);
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setString(1, _ctb_id);
		stmt.setString(2, _ctb_type);
		stmt.setString(3, _ctb_title);
        stmt.setTimestamp(4, ctb_upd_timestamp);
        stmt.setString(5, ctb_upd_usr_id);
        stmt.setString(6, _ctb_type_bef);
        stmt.setString(7, _ctb_id_bef);

        stmt.executeUpdate();
        stmt.close();
        con.commit();
    }

    //insert a record into CodeTable
    //get DB time if create time is not input
    public void ins(Connection con, String usr_id,String ctp_id,String _ctb_id,String _ctb_title) throws SQLException, cwSysMessage, cwException {

        //generate a ctb_id for Supplier
        /*if(ctb_type != null && ctb_type.equals(CTB_TYPE_SUPPLIER)) {
            ctb_id = CodeTable.genCtbIdForSupplier(con);
        }*/

        //check if (ctb_type, ctb_id) already exists
        if(isRecordExist(con,ctp_id,_ctb_id))
            throw new cwSysMessage(CTB_RECORD_EXIST);

        try {
            if(ctb_create_timestamp == null)
                ctb_create_timestamp = dbUtils.getTime(con);
        }
        catch(qdbException e) {
            throw new SQLException(e.getMessage());
        }
        if(ctb_upd_timestamp == null)
            ctb_upd_timestamp = ctb_create_timestamp;
        ctb_create_usr_id = usr_id;
        ctb_upd_usr_id = usr_id;
        ins(con,ctp_id,_ctb_id,_ctb_title);
    }

    //generate an unique ctb_id for Supplier by adding the max(ctb_id) by 1
    private static String genCtbIdForSupplier(Connection con) throws SQLException, cwException {
            long maxCtbId;
            String sCtbId;
            final String SQL = " Select max(ctb_id) From CodeTable "
                                + " Where ctb_type = ? ";
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setString(1, CTB_TYPE_SUPPLIER);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                try {
                    maxCtbId = Long.parseLong(rs.getString(1));
                }
                catch(NumberFormatException e) {
                    maxCtbId = 0;
                }
            }
            else {
                maxCtbId = 0;
            }
            stmt.close();
            maxCtbId++;
            sCtbId = new Long(maxCtbId).toString();
            for(int i=sCtbId.length();i<10;i++) {
                sCtbId = "0" + sCtbId;
            }
            return sCtbId;
    }

    public void ins(Connection con,String ctp_id,String _ctb_id,String _ctb_title) throws SQLException {
        StringBuffer SQLBuf = new StringBuffer(300);
        SQLBuf.append(" Insert into CodeTable ");
        SQLBuf.append(" (ctb_type, ctb_id, ctb_title ");
        SQLBuf.append(" ,ctb_create_timestamp, ctb_create_usr_id ");
        SQLBuf.append(" ,ctb_upd_timestamp, ctb_upd_usr_id ) ");
        // << BEGIN for oracle migration!
        //SQLBuf.append(" ,ctb_xml ) ");
        SQLBuf.append(" Values ");
        SQLBuf.append(" (?,?,?,?,?,?,?) ");
        //SQLBuf.append(" (?,?,?,?,?,?,?,");
        //SQLBuf.append(cwSQL.getClobNull(con));
        //SQLBuf.append(")");
        // >> END
        //String SQL = new String(SQLBuf);
        PreparedStatement stmt =con.prepareStatement(SQLBuf.toString());
        stmt.setString(1, ctp_id);
        stmt.setString(2, _ctb_id);
        stmt.setString(3, _ctb_title);
        stmt.setTimestamp(4, ctb_create_timestamp);
        stmt.setString(5, ctb_create_usr_id);
        stmt.setTimestamp(6, ctb_upd_timestamp);
        stmt.setString(7, ctb_upd_usr_id);
        try {
			int row = stmt.executeUpdate();
        	stmt.close();
        	con.commit();
        }			
		catch(Exception e) {
			CommonLog.error(e.getMessage(),e);
		}
        // << BEGIN for oracle migration!
        // Update ctb_xml
        // construct the condition
        String condition = "ctb_id = '" + _ctb_id + "' AND ctb_type = '" + ctp_id + "'";
        // construct the column & value
        String[] columnName = new String[1];
        String[] columnValue = new String[1];
        columnName[0] = "ctb_xml";
        columnValue[0] = ctb_xml;
        cwSQL.updateClobFields(con, "CodeTable", columnName, columnValue, condition);
        // >> END
    }

    public boolean isRecordExist(Connection con,String ctb_type,String ctb_id) throws SQLException {
        boolean result;
        long count=0;
        final String SQL = " Select count(*) from CodeTable "
                         + " Where ctb_type = ? "
                         + " And ctb_id = ? ";

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setString(1, ctb_type);
        stmt.setString(2, ctb_id);
        ResultSet rs = stmt.executeQuery();

        if(rs.next())
            count = rs.getLong(1);

        stmt.close();
        if(count > 0)
            result = true;
        else
            result = false;

        return result;
    }
    
    public boolean isTypeExist(Connection con, String type) throws SQLException {
        boolean result;
        long count=0;
        final String SQL = " Select count(*) from CodeType "
                         + " Where ctp_id = ? ";

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setString(1, type);
        ResultSet rs = stmt.executeQuery();

        if(rs.next())
            count = rs.getLong(1);

        stmt.close();
        if(count > 0)
            result = true;
        else
            result = false;

        return result;
    }
    
    public boolean isLastUpd(Connection con, Timestamp upd_timestamp) throws SQLException,cwSysMessage {
        boolean result;
        final String SQL = " Select ctb_upd_timestamp From CodeTable "
                         + " Where ctb_type = ? "
                         + " And ctb_id = ? ";

         PreparedStatement stmt = con.prepareStatement(SQL);
         stmt.setString(1, ctb_type);
         stmt.setString(2, ctb_id);
         ResultSet rs = stmt.executeQuery();
         if(rs.next())
            ctb_upd_timestamp = rs.getTimestamp("ctb_upd_timestamp");
         else
            throw new cwSysMessage(CTB_TYPE_RECORD_EXIST);
         if(upd_timestamp == null || ctb_upd_timestamp == null)
            result = false;
         else {
            //upd_timestamp.setNanos(tnd_upd_timestamp.getNanos());
            if(upd_timestamp.equals(ctb_upd_timestamp))
                result = true;
            else
                result = false;
         }
         stmt.close();
         return result;
    }
//    Vincent
	public boolean isLastUpd(Connection con, String ctb_id_bef,String ctb_type_bef,Timestamp upd_timestamp) throws SQLException,cwSysMessage {
			boolean result;
			final String SQL = " Select ctb_upd_timestamp From CodeTable "
							 + " Where ctb_type = ? "
							 + " And ctb_id = ? ";

			 Timestamp ctb_upd_timestamp_bef;
			 PreparedStatement stmt = con.prepareStatement(SQL);
			 stmt.setString(1, ctb_type_bef);
			 stmt.setString(2, ctb_id_bef);
			 ResultSet rs = stmt.executeQuery();
			 if(rs.next())
				 ctb_upd_timestamp_bef = rs.getTimestamp("ctb_upd_timestamp");
			 else
				throw new cwSysMessage(RECORD_NOT_EXIST);
			 if(upd_timestamp == null || ctb_upd_timestamp == null)
				result = false;
			 else {
				//upd_timestamp.setNanos(tnd_upd_timestamp.getNanos());
				if(upd_timestamp.equals(ctb_upd_timestamp_bef))
					result = true;
				else
					result = false;
			 }
			 stmt.close();
			 return result;
		}
//	Vincent
    /**
     * get all code types available in the system
     * 2001.07.27 wai
     */
    public static StringBuffer getTypeListAsXML(Connection con) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int col = 0;
        StringBuffer result = new StringBuffer(500);
        result.append("<code_data_type_list>");

        // get all code types
        stmt = con.prepareStatement(sql_get_code_type);
        rs = stmt.executeQuery();
        String typeID;
        String typeTitle;
        String typeSys;
        // for each type, get the details
        while (rs.next()) {
            col = 1;
            typeID      = rs.getString(col++);
            typeTitle   = rs.getString(col++);
            typeSys     = rs.getString(col++);
            /*if (rs.getBoolean(col++))
                typeSys = "yes";
            else
                typeSys = "no";*/
            if (typeSys.equalsIgnoreCase("0"))
            result.append("<code_data_type").append(" id=\"").append(dbUtils.esc4XML(typeID)).append("\"")
                  .append(" ind=\"").append(dbUtils.esc4XML(typeSys)).append("\"").append(" >").append("<title>")
                  .append(dbUtils.esc4XML(typeTitle)).append("</title>").append("</code_data_type>");
        }
        stmt.close();

        result.append("</code_data_type_list>");
        return result;
    }
    //Vincent'code
	public static StringBuffer getCodeDataListAsXML(Connection con,String code_data_type) throws SQLException {
			PreparedStatement stmt = null;
			ResultSet rs = null;
			int col = 0;
			StringBuffer result = new StringBuffer(500);
			result.append("<code_data_list type=\"").append(code_data_type).append(" \">");

			// get all code types
			stmt = con.prepareStatement(sql_get_code_data_list);
			col = 1;
			stmt.setString(col++,code_data_type);
			rs = stmt.executeQuery();
			String codeDataID;
			String codeDataTitle;
			while (rs.next()) {
				col = 1;
				codeDataTitle      = rs.getString(col++);
				codeDataID   = rs.getString(col++);
				result.append("<code_data").append(" id=\"").append(dbUtils.esc4XML(codeDataID)).append("\"")
					  .append(" >").append("<title>")
					  .append(dbUtils.esc4XML(codeDataTitle)).append("</title>").append("</code_data>");
			}
			stmt.close();
			result.append("</code_data_list>");
			return result;
	}
	public static StringBuffer getCodeDataDetailAsXML(Connection con,String code_data_id,String code_data_type) throws SQLException,cwSysMessage {
			PreparedStatement stmt = null;
			ResultSet rs = null;
			int col = 0;
			StringBuffer result = new StringBuffer(500);
			result.append("<code_data_detail>");

			// get all code types
			stmt = con.prepareStatement(sql_get_code_data_detail);
			col = 1;
			stmt.setString(col++,code_data_id);
			stmt.setString(col++,code_data_type);
			rs = stmt.executeQuery();
			String codeDataID;
			String codeDataTitle;
		    String codeDataType;
		    String codeDataCreatTimestamp;
		    String codeDataCreatUsr;
		    String codeDataUpdateUsr;
		    String codeDataUpdateTimestamp;
		    if (rs.next()){
				codeDataTitle      = rs.getString("ctb_title");
				codeDataID   = rs.getString("ctb_id");
				codeDataType = rs.getString("ctb_type");
				codeDataCreatTimestamp = rs.getString("ctb_create_timestamp");
				codeDataCreatUsr = rs.getString("create_user");
				codeDataUpdateUsr =rs.getString("update_user");
				codeDataUpdateTimestamp = rs.getString("ctb_upd_timestamp");   
				result.append("<id>").append(dbUtils.esc4XML(codeDataID)).append("</id>").append("<title>")
					  .append(dbUtils.esc4XML(codeDataTitle)).append("</title>").append("<type>")
					  .append(dbUtils.esc4XML(codeDataType)).append("</type>")
					  .append("<create_time>").append(dbUtils.esc4XML(codeDataCreatTimestamp)).append("</create_time>")
					  .append("<create_user>").append(dbUtils.esc4XML(codeDataCreatUsr)).append("</create_user>")
					  .append("<update_time>").append(dbUtils.esc4XML(codeDataUpdateTimestamp)).append("</update_time>")
				      .append("<update_user>").append(dbUtils.esc4XML(codeDataUpdateUsr)).append("</update_user>");
		    } else {
		    	throw new cwSysMessage(INVALID_OPERATION);
		    }
			stmt.close();
			result.append("</code_data_detail>");
			return result;
	}
	public static StringBuffer insCodeDataAsXML(Connection con,String ctb_type) throws SQLException {
			StringBuffer result = new StringBuffer(500);
			result.append("<code_data_type>");
			result.append("<ctb_type>").append(ctb_type).append("</ctb_type>");
			result.append("</code_data_type>");
			return result;
	}	
    //Vincent'code
    /**
    Get XML of CodeTable data for Item's item template
    @param con Connection to databse
    @param ctb_types String array of ctb_type wanted
    @return XML for transformation of valued template
    */
    public static String getCtb4ItemTemplate(Connection con, String[] ctb_types)
        throws SQLException {
        
        StringBuffer xmlBuf = new StringBuffer(1024);
        StringBuffer tempBuf = new StringBuffer(1024);
        PreparedStatement stmt = null;
        String prevCtbType = null;
        String thisCtbType = null;
        try {
            stmt = getAllCtbSQL(con, ctb_types);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                thisCtbType = rs.getString("ctb_type");
                if(prevCtbType == null) {
                    tempBuf.append("<").append(cwUtils.esc4XML(thisCtbType)).append(">");
                    tempBuf.append("<title>");
                } else if(!prevCtbType.equals(thisCtbType)) {
                    tempBuf.append("</title>");
                    tempBuf.append("</").append(cwUtils.esc4XML(prevCtbType)).append(">");
                    xmlBuf.append(tempBuf.toString());
                    tempBuf = new StringBuffer(1024);
                    tempBuf.append("<").append(cwUtils.esc4XML(thisCtbType)).append(">");
                    tempBuf.append("<title>");
                }
                tempBuf.append("<ctb id=\"")
                       .append(cwUtils.esc4XML(rs.getString("ctb_id")))
                       .append("\">")
                       .append("<desc name=\"")
                       .append(cwUtils.esc4XML(rs.getString("ctb_title")))
                       .append("\"/>")
                       .append("</ctb>");
                prevCtbType = thisCtbType;
            }
            if(prevCtbType != null) {
                tempBuf.append("</title>");
                tempBuf.append("</").append(cwUtils.esc4XML(prevCtbType)).append(">");
                xmlBuf.append(tempBuf.toString());
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        return xmlBuf.toString();
    }

    //get all the records from code table of the input types
    public static String getAll(Connection con, String[] ctb_types) 
        throws SQLException, cwSysMessage {
        
        StringBuffer xmlBuf = new StringBuffer(1024);
        PreparedStatement stmt = null;
        try {
            stmt = getAllCtbSQL(con, ctb_types);
            xmlBuf.append("<codes>");
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                CodeTable ctb = new CodeTable();
                ctb.ctb_type = rs.getString("ctb_type");
                ctb.ctb_id = rs.getString("ctb_id");
                ctb.get(con);
                xmlBuf.append(ctb.asXML(con,false)).append(cwUtils.NEWL);
            }
            xmlBuf.append("</codes>");
        } finally {
            if(stmt!=null) stmt.close();
        }
        return xmlBuf.toString();
    }
    
    private static PreparedStatement getAllCtbSQL(Connection con, String[] ctb_types) 
        throws SQLException {
        PreparedStatement stmt = null;
        StringBuffer SQLBuf = new StringBuffer(300);
        SQLBuf.append(" Select ctb_type, ctb_id, ctb_title, ctb_xml, ")
              .append(" ctb_create_usr_id, ctb_create_timestamp, ")
              .append(" ctb_upd_usr_id, ctb_upd_timestamp ")
              .append(" From CodeTable ")
              .append(" Where ")
              .append(" 1=0 ");
        for(int i=0; i<ctb_types.length; i++) {
            SQLBuf.append(" Or ctb_type = ? ");
        }
        SQLBuf.append(" Order by ctb_type, ctb_id, ctb_title");
        stmt = con.prepareStatement(SQLBuf.toString());
        for(int i=0; i<ctb_types.length; i++) {
            stmt.setString(i+1, ctb_types[i]);
        }
        return stmt;
    }
    
      public void saveCodeType(Connection con, String type, boolean sys_ind, String title, String upd_usr_id) throws SQLException{
        if (isTypeExist(con, type)){
            updCodeType(con, type, sys_ind, title, upd_usr_id);
        }else{
            insCodeType(con, type, sys_ind, title, upd_usr_id);
        }
    }
    public void insCodeType(Connection con, String type, boolean sys_ind, String title, String upd_usr_id) throws SQLException{
        Timestamp curTime = cwSQL.getTime(con);
        PreparedStatement stmt =con.prepareStatement(sql_ins_code_type);
        stmt.setString(1, type);
        stmt.setBoolean(2, sys_ind);
        stmt.setString(3, title);
        stmt.setTimestamp(4, curTime);
        stmt.setString(5, upd_usr_id);
        stmt.setTimestamp(6, curTime);
        stmt.setString(7, upd_usr_id);
        stmt.executeUpdate();
        stmt.close();
   }

    public void updCodeType(Connection con, String type, boolean sys_ind, String title, String upd_usr_id) throws SQLException{
        Timestamp curTime = cwSQL.getTime(con);
        PreparedStatement stmt =con.prepareStatement(sql_upd_code_type);
        stmt.setBoolean(1, sys_ind);
        stmt.setString(2, title);
        stmt.setTimestamp(3, curTime);
        stmt.setString(4, upd_usr_id);
        stmt.setString(5, type);

        stmt.executeUpdate();
        stmt.close();
   }
   
   public void delNotActiveCode(Connection con, String type, Vector vtActiveId) throws SQLException{
        StringBuffer sqlBuf = new StringBuffer();
        sqlBuf.append(" Delete From CodeTable Where ctb_type = ? ");

        if (vtActiveId!=null){
            sqlBuf.append("And ctb_id NOT IN (");
            for(int i=0;i<vtActiveId.size();i++){
                if (i > 0) 
                sqlBuf.append(",");
                sqlBuf.append("'" + (String) vtActiveId.elementAt(i) + "'");
            }
            sqlBuf.append(")");
        }
        PreparedStatement stmt = con.prepareStatement(sqlBuf.toString());
        stmt.setString(1, type);
        int row = stmt.executeUpdate();
        stmt.close();
    }
}