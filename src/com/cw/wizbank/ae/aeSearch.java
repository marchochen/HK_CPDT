package com.cw.wizbank.ae;

import java.sql.*;
import com.cw.wizbank.qdb.*;
import com.cw.wizbank.util.*;
import javax.servlet.http.*;

public class aeSearch {

    public static final String ITEM_STATUS_OUTSTANDING = "OUTSTANDING";
    public static final String ITEM_STATUS_CANCEL = "CANCELLED";
    public static final String ITEM_STATUS_CLOSED = "CLOSED";
    public static final String SEARCH_TYPE_EXACT = "EXACT";
    public static final String SEARCH_TYPE_PARTIAL = "PARTIAL";
    public static final String INVOICE = "INVOICE";
    public static final String APPLYEASY = "APPLYEASY";

    //Order by
    public static final String USR_ID = "usr_id";
    public static final String LAST_NAME = "last_name";
    public static final String FIRST_NAME = "first_name";
    public static final String DISPLAY_NAME = "display_name";
    public static final String NUM_OF_ITEM = "num_of_item";
    public static final String TOTAL_OS = "total_os";

    public static final String GET_OPEN_ACC
    = " SELECT distinct COUNT(oim_id) AS TOTALOI, SUM(oim_os_amt) AS SUMAMT, usr_ent_id AS ENTID, "
    + " usr_id AS USRID, usr_ste_usr_id AS USRSID, usr_email AS USREMAIL, usr_last_name_bil AS USRLASTNAME, "
    + " usr_first_name_bil AS USRFIRSTNAME, usr_display_bil AS DISPNAME "
    + " FROM aeOpenItem, RegUser, aeAccount "
    + " WHERE oim_type = ? AND acn_ent_id " 
    + " IN ( SELECT distinct ern_child_ent_id "                   // sub_sql to find all the user
    + "      FROM EntityRelation, UserGroup "                         // under this organization
    + "      WHERE ( usg_ent_id = ? OR usg_ent_id_root = ? )"      // if user belong 2 groups , result will find out a duplicate item
    + "              AND ern_ancestor_ent_id = usg_ent_id "           // filter out the duplicate ent_id 
    + "              AND ern_parent_ind = ? "
    + "    )"
    + " AND acn_ent_id = usr_ent_id AND acn_id = oim_acn_id ";
    
    
    public static final String SQL_GROUP_BY
    = " GROUP BY acn_id, usr_ent_id, usr_id, usr_ste_usr_id, usr_email, usr_last_name_bil, "
    + " usr_first_name_bil, usr_display_bil ";

    public String usr_id;
    public String usr_id_type;
    public String usr_last_name;
    public String usr_last_name_type;
    public String usr_first_name;
    public String usr_first_name_type;
    public String usr_email;
    public String usr_email_type;
    public long oi_id;
    public Timestamp oi_due_from;
    public Timestamp oi_due_to;
    public String oi_desc;
    public String oi_desc_type;
    public String oi_status[];
    public String acn_type;
    
    public int page_num;
    public int item_per_page;    
    public String order_by;
    public String sort_by;
    boolean first_time;
    
    public int total_pages;
    public String[] result_id_lst;
    
    public boolean ascending;
    
    aeSearch() {;}
    
    public boolean getSearchResultAsXML(Connection con,HttpSession sess, StringBuffer result, loginProfile prof)
        throws SQLException, qdbException {
            StringBuffer SQL = new StringBuffer();            
            float total_os = 0;
            float total_org = 0;
            int total_users = 0;
            long ENTID=0;
            boolean flag=false; // assume no item find
            PreparedStatement stmt = null;
            ResultSet rs = null;

            result.append("<result>").append(dbUtils.NEWL);
            
            //if( (usr_id != null && usr_id.length() > 0) || (usr_last_name != null && usr_last_name.length() > 0)
            //   || (usr_first_name != null && usr_first_name.length() > 0) ) {
        
                SQL.append(GET_OPEN_ACC);
                //search in first time, put all the usr_ent_id in the session                
                if(first_time) {
                    if(usr_id != null && usr_id.length() > 0 )
                        if(usr_id_type != null && usr_id_type.equalsIgnoreCase(SEARCH_TYPE_PARTIAL))
                            SQL.append(" AND usr_ste_usr_id like ? ");
                        else
                            SQL.append(" AND usr_ste_usr_id = ? " );
                    
                    if(usr_last_name != null && usr_last_name.length() > 0 ) {
                        if(usr_last_name_type != null && usr_last_name_type.equalsIgnoreCase(SEARCH_TYPE_PARTIAL))
                            SQL.append(" AND lower(usr_last_name_bil) like ? ");
                        else
                            SQL.append(" AND lower(usr_last_name_bil) = ? ");
                    }        
                    
                    if(usr_first_name != null && usr_first_name.length() > 0 ) {
                        if(usr_first_name_type != null && usr_first_name_type.equalsIgnoreCase(SEARCH_TYPE_PARTIAL))
                            SQL.append(" AND lower(usr_first_name_bil) like ? ");
                        else
                            SQL.append(" AND lower(usr_first_name_bil) = ? ");
                    }
                    
                    if(usr_email != null && usr_email.length() > 0 ) {
                        if(usr_email_type != null && usr_email_type.equalsIgnoreCase(SEARCH_TYPE_PARTIAL))
                            SQL.append(" AND lower(usr_email) like ? ");
                        else
                            SQL.append(" AND lower(usr_email) = ? ");
                    }
                    
                    if(oi_id > 0)
                        SQL.append(" AND oim_id = ? ");
                    
                    if(oi_due_from != null)
                        SQL.append(" AND oim_due_datetime >= ? ");
                    
                    if(oi_due_to != null)
                        SQL.append(" AND oim_due_datetime <= ? ");
                        
                    if(oi_desc != null && oi_desc.length() > 0 ) {
                        if(oi_desc_type != null && oi_desc_type.equalsIgnoreCase(SEARCH_TYPE_PARTIAL))
                            SQL.append(" AND lower(oim_desc) like ? ");
                        else
                            SQL.append(" AND lower(oim_desc) = ? ");
                    }
                    
                    
                    if(oi_status != null) {
                        SQL.append(" AND ( oim_status = ? ");
                        for(int i=1;i < oi_status.length; i++)
                            SQL.append(" OR oim_status = ? ");
                        SQL.append(" ) " );    
                    }
                    
                    SQL.append(" AND acn_type =  ? ");
                    
                }
                
                else {
                    total_users = ((Integer)sess.getAttribute("TOTAL_USERS")).intValue();
                    total_pages = ((Integer)sess.getAttribute("TOTAL_PAGE")).intValue(); 
                    acn_type = (String)sess.getAttribute("ACN_TYPE");
                    oi_status = (String[])sess.getAttribute("OIM_STATUS");
                    
                    if(oi_status != null) {
                        SQL.append(" AND ( oim_status = ? ");
                        for(int i=1;i < oi_status.length; i++)
                            SQL.append(" OR oim_status = ? ");
                        SQL.append(" ) " );    
                    }
                    
                    SQL.append(" AND acn_type = ? ");
                    result_id_lst = dbUtils.split((String)sess.getAttribute("SEARCH_RESULT_ID"), ",");
                    SQL.append(" AND usr_ent_id IN (");
                    
                    int start_index = (page_num-1) * item_per_page;
                    int end_index = start_index + item_per_page < result_id_lst.length ? start_index + item_per_page : result_id_lst.length;

                    if( order_by != null && order_by.length() > 0 ) {
                        start_index = 0;
                        end_index = result_id_lst.length;
                    }
                    for(int i=start_index; i<end_index; i++) {
                        SQL.append(result_id_lst[i]).append(",");
                    }
                    SQL.append("0)");
                }
                
                SQL.append(SQL_GROUP_BY);

                               
                if(order_by != null && order_by.length() > 0 ) {
                    SQL.append(" order by ").append(order_by);
                }
                else
                    SQL.append(" order by ").append((String)sess.getAttribute("ORDER_BY"));
                
                if(sort_by != null && sort_by.length() > 0 )
                    SQL.append(" ").append(sort_by);
                else
                    SQL.append(" ").append((String)sess.getAttribute("SORT_BY"));
                


                //System.out.println("SQL : " + SQL);
               
                try{
                    stmt = con.prepareStatement(SQL.toString());
                    int index = 1;
                    stmt.setString(index++, INVOICE);
                    stmt.setLong(index++, prof.root_ent_id);                    
                    stmt.setLong(index++, prof.root_ent_id);
                    stmt.setBoolean(index++, true);
                    //System.out.println(INVOICE);
                    //System.out.println(prof.root_ent_id);
                    //System.out.println(prof.root_ent_id);
                    //index++;
                    
                    if(usr_id != null) {
                        if(usr_id_type != null && usr_id_type.equalsIgnoreCase(SEARCH_TYPE_PARTIAL))                            
                            stmt.setString(index++, "%" + usr_id + "%");
                        else
                            stmt.setString(index++, usr_id);
                        //System.out.println(usr_id);
                    }
                    if(usr_last_name != null) {
                        if(usr_last_name_type != null && usr_last_name_type.equalsIgnoreCase(SEARCH_TYPE_PARTIAL))
                            stmt.setString(index++, "%" + usr_last_name.toLowerCase() + "%");
                        else
                            stmt.setString(index++, usr_last_name.toLowerCase());
                        //System.out.println(usr_last_name);    
                    }        
                    
                    if(usr_first_name != null) {
                        if(usr_first_name_type != null && usr_first_name_type.equalsIgnoreCase(SEARCH_TYPE_PARTIAL))
                            stmt.setString(index++, "%" + usr_first_name.toLowerCase() + "%");
                        else
                            stmt.setString(index++, usr_first_name.toLowerCase());
                        //System.out.println(usr_first_name);    
                    }
                    
                    if(usr_email != null) {
                        if(usr_email_type != null && usr_email_type.equalsIgnoreCase(SEARCH_TYPE_PARTIAL))
                            stmt.setString(index++, "%" + usr_email.toLowerCase() + "%");
                        else
                            stmt.setString(index++, usr_email.toLowerCase());
                        //System.out.println(usr_email);    
                    }
                    
                    if(oi_id > 0) {
                        stmt.setLong(index++,oi_id);
                        //System.out.println(oi_id);
                    }
                    
                    if(oi_due_from != null) {
                        stmt.setTimestamp(index++, oi_due_from);
                        //System.out.println(oi_due_from);
                    }
                    
                    if(oi_due_to != null) {
                        stmt.setTimestamp(index++,oi_due_to);
                        //System.out.println(oi_due_to);
                    }
                    if(oi_desc != null) {
                        if(oi_desc_type != null && oi_desc_type.equalsIgnoreCase(SEARCH_TYPE_PARTIAL))
                            stmt.setString(index++, "%" + oi_desc.toLowerCase() + "%");
                        else
                            stmt.setString(index++,oi_desc.toLowerCase());
                        //System.out.println(oi_desc);    
                    }

                    
                    if(oi_status != null) {
                        for(int i=0;i<oi_status.length; i++) {
                            stmt.setString(index++,oi_status[i]);
                            //System.out.println(oi_status[i]);
                        }
                    }
                    /*
                    if(first_time) {
                        stmt.setString(index++,INVOICE);
                        stmt.setString(index,acn_type);
                    }
                    else {
                        stmt.setString(index,acn_type);                        
                    }
                    */
                    stmt.setString(index,acn_type);
                    //System.out.println(acn_type);
                    rs = stmt.executeQuery();
                    
                    String USRID="";
                    String USRSID="";
                    //String USRIDGROUP="";
                    String USRLASTNAME;
                    String USRFIRSTNAME;
                    String USREMAIL;                    
                    //long ITEMID;                
                    Timestamp ITEMDUE;
                    String ITEMDESC;
                    String ITEMSTATUS;            
                    String DISPNAME;
                    float ITEMORGAMT;
                    float ITEMOSAMT;
                    String ITEMCCY;
                    float SUMAMT = 0;
                    int TOTALOI = 0;                    
                    int count = 0;
                    String result_id = "" ;
                    while(rs.next()) {
                        flag = true;    // item find                           

                        ENTID = rs.getLong("ENTID");
                        result_id += Long.toString(ENTID) + ",";
                        
                        // search in first time or reorder
                        if(first_time || (order_by!=null && order_by.length()>0)) {
                            total_users++;
                            if(count >= item_per_page)
                                continue;
                            count++;
                        }
                        
                            
                            USRID = rs.getString("USRID");
                            USRSID = rs.getString("USRSID");
                            USRLASTNAME = rs.getString("USRLASTNAME");
                            USRFIRSTNAME = rs.getString("USRFIRSTNAME");
                            USREMAIL = rs.getString("USREMAIL");
                            DISPNAME = rs.getString("DISPNAME");
                            TOTALOI = rs.getInt("TOTALOI");
                            SUMAMT = rs.getFloat("SUMAMT");
                            
                            result.append("<item usr_id = \"").append(dbUtils.esc4XML(USRSID)).append("\" ");
                            result.append("ent_id =\"").append(ENTID).append("\" ");
                            result.append("display_name=\"").append(dbUtils.esc4XML(DISPNAME)).append("\" ");
                            result.append("last_name = \"").append(dbUtils.esc4XML(USRLASTNAME)).append("\" ");
                            result.append("first_name = \"").append(dbUtils.esc4XML(USRFIRSTNAME)).append("\" ");
                            result.append("total_item =\"").append(TOTALOI).append("\" ");
                            result.append("total_os =\"").append(aeUtils.twoDecPt(aeUtils.roundingFloat(SUMAMT,2))).append("\" ");
                            result.append("email =\"").append(cwUtils.esc4XML(USREMAIL)).append("\"/>").append(dbUtils.NEWL);                        
                    }
//                    System.out.println("result list : " + result_id);
                    stmt.close();

                    if( first_time ) {
                        sess.setAttribute("TOTAL_USERS",new Integer(total_users));
                        total_pages = ceilInt((float)total_users/item_per_page);
                        sess.setAttribute("TOTAL_PAGE",new Integer(total_pages));
                        sess.setAttribute("ACN_TYPE",acn_type);
                        sess.setAttribute("OIM_STATUS", oi_status);
                    }
                    
                    if(order_by != null && order_by.length() > 0 ) {                                                
                        sess.setAttribute("SEARCH_RESULT_ID",result_id);
                        sess.setAttribute("ORDER_BY",order_by);                        
                    }
                    
                    if(sort_by != null && sort_by.length() > 0 ) {
                        sess.setAttribute("SORT_BY",sort_by);
                    }
                    
                    
                    }catch(SQLException e) {
                            throw new qdbException("SQL Error: " + e.getMessage());           
                    }
                
                result.append("<total_users num_of_users=\"").append((Integer)sess.getAttribute("TOTAL_USERS")).append("\" ");
                result.append("total_page=\"").append((Integer)sess.getAttribute("TOTAL_PAGE")).append("\" ");
                if(page_num == 0)
                    page_num = 1;
                result.append("page_num=\"").append(page_num).append("\" ");
                result.append("sort_by=\"").append(sort_by).append("\" ");                
                result.append("order_by=\"").append(order_by).append("\" ");
                    
                result.append("item_per_page=\"").append(item_per_page).append("\" />").append(dbUtils.NEWL);
                result.append("</result>");                
                //System.out.println("Result : " + result.toString());
                
                
                if(oi_id > 0 && flag) {
                    result.setLength(0);
                    result.append(aeOpenItem.getOSOpenItem(con,sess,ENTID,acn_type));
                }
                
                return flag;
                
            }
            
            
    public static int ceilInt(float num) {        
        double double_num = (new Float(num)).doubleValue();
        return (new Double(Math.ceil(double_num))).intValue();            
    }
            
}

    
