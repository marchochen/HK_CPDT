package com.cw.wizbank.qdb;

import java.sql.*;
import java.util.Vector;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.io.*;


public class dbAiccPath
{
    public static String STATUS_COMPLETE = "C";
    public static String STATUS_INCOMPLETE = "I";
    public static String STATUS_PASSED = "P";
    public static String STATUS_FAILED = "F";
    public static String STATUS_BROWSED = "B";
    public static String STATUS_NOT_ATTEMPTED = "N";

    public static String REPORT_TYPE_USAGE = "U";
    public static String REPORT_TYPE_LEARNER = "L";
    
    public long acp_cos_id;
    public long acp_ent_id;
    public long acp_mod_id;
    public Timestamp acp_acc_datetime;
    public String acp_ele_loc;
    public String acp_status;
    public String acp_why_left;
    public float acp_ele_time;
    
    public Hashtable paths ;
    public String mod_status;
    public boolean mod_unload;
    
    public dbAiccPath() 
    {
        paths = new Hashtable();
    }

    private class ElementReport{
        String acp_ele_loc = "";
        long no_of_attempt = 0;
        float total_ele_time = 0;
        Timestamp last_access = null;
        long no_of_attendant = 0;
    }
    // called by qdbAction
    public String getElementRptAsXML(Connection con, loginProfile prof, String res_path) throws qdbException{
        try{
            // set the start_resource_tag, end_resource_tag and acp_cos_id
            getResourceInfo(con, res_path);
            Vector memberlist = dbUserGroup.getMemberListFromCos(con, acp_cos_id);
            String xml = new String(dbUtils.xmlHeader);
            xml += "<report>" + dbUtils.NEWL;
            xml += prof.asXML() + dbUtils.NEWL;
            xml += start_resource_tag;
            Vector vtElementReport = getElement(con);
            vtElementReport = parseVtElement(vtElementReport);
            xml += getElementXML(vtElementReport, memberlist.size());
            xml += getManifest(res_path);
            xml += end_resource_tag;
            xml += "</report>";
            return xml;
        }catch(qdbErrMessage e){
            throw new qdbException(e.getMessage());    
        }
    }
    
    // called by qdbAction
    public String getLearnerElementRptAsXML(Connection con, loginProfile prof, String res_path) throws qdbException{
        try{        
            // set the start_resource_tag, end_resource_tag and acp_cos_id
            // for learner, no need to parse the vector 
            getResourceInfo(con, res_path);
            String xml = new String(dbUtils.xmlHeader);
            xml += "<report>" + dbUtils.NEWL;
            xml += prof.asXML() + dbUtils.NEWL;
            dbRegUser dbRU = new dbRegUser();
            dbRU.usr_ent_id = acp_ent_id;
            dbRU.getByEntId(con);
            xml += "<learner id=\"" + acp_ent_id + "\" usr_id=\"" + dbUtils.esc4XML(dbRegUser.usrId2SteUsrId(con,dbRU.usr_id)) + "\" name=\"" + dbUtils.esc4XML(dbRU.usr_display_bil) + "\" />" + dbUtils.NEWL;
            xml += start_resource_tag;
            Vector vtElementReport = getLearnerElement(con);
            xml += getLearnerElementXML(vtElementReport);
            xml += getManifest(res_path);
            xml += end_resource_tag;
            xml += "</report>";
            return xml;
        }catch(qdbErrMessage e){
            throw new qdbException(e.getMessage());    
        }
        catch(SQLException sqle) {
            throw new qdbException(sqle.getMessage());
        }
    }

    private String start_resource_tag = "";
    private String end_resource_tag = "";
    
    public void getResourceInfo(Connection con, String res_path) throws qdbErrMessage, qdbException{
        // get the course id and cos title with mod_id
        // get the relation from ResourceContent
        try{
            String cos_title = null;
            String module_title = null;
            Vector vtElementReport = null;
            String SQL = " SELECT res_id, res_title FROM resources, resourcecontent " 
                        + " WHERE (res_id = rcn_res_id or res_id = rcn_res_id_content) AND "
                        + " rcn_res_id_content = ? ";

            PreparedStatement stmt = con.prepareStatement(SQL);                      
            stmt.setLong(1, acp_mod_id);
                
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next())
            {
                if (rs.getLong("res_id") == acp_mod_id){
                    module_title = rs.getString("res_title");
                }    
                else{     
                    cos_title = rs.getString("res_title");
                    acp_cos_id = rs.getLong("res_id");
                }    
            }
            
            start_resource_tag = "<course id=\"" + acp_cos_id + "\" title=\"" + dbUtils.esc4XML(cos_title) + "\" >" + dbUtils.NEWL; 
            start_resource_tag += "<module id=\"" + acp_mod_id + "\" title=\"" + dbUtils.esc4XML(module_title) + "\" >" + dbUtils.NEWL;
            end_resource_tag = "</module>" + dbUtils.NEWL;
            end_resource_tag += "</course>" + dbUtils.NEWL;      
            stmt.close();
        } catch(SQLException e) {
                throw new qdbException("SQL Error: " + e.getMessage()); 
        }  
    }
    
    // assume acp_mod_id parsed
    // group by ent_id !!!
    // need to be parse before print as xml
    private Vector getElement(Connection con) throws qdbException{
        try{
            Vector vtElementReport = new Vector(); 
            String SQL = " SELECT "
                    + " acp_ele_loc "
                    + " , COUNT(*) AS no_of_attempt "
                    + " , SUM(acp_ele_time) AS total_ele_time  "
                    + " , MAX(acp_acc_datetime) AS last_access "
                    + " FROM AiccPath WHERE " 
                    + " acp_mod_id = ? "
                    + " GROUP BY acp_mod_id, acp_ele_loc, acp_ent_id  "
                    + " ORDER BY acp_ele_loc ";

            PreparedStatement stmt = con.prepareStatement(SQL);                      
            stmt.setLong(1, acp_mod_id);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next())
            {
                ElementReport er = new ElementReport();
                er.acp_ele_loc = rs.getString("acp_ele_loc");
                er.no_of_attempt = rs.getLong("no_of_attempt");
                er.total_ele_time = rs.getFloat("total_ele_time");
                er.last_access = rs.getTimestamp("last_access");
                vtElementReport.addElement(er);
            }
            stmt.close();
            return vtElementReport;
            
        } catch(SQLException e) {
                throw new qdbException("SQL Error: " + e.getMessage()); 
        }  
    }
    // scroll to one learner only
    private Vector getLearnerElement(Connection con) throws qdbException{
        try{
            Vector vtElementReport = new Vector(); 
            String SQL = " SELECT "
                    + " acp_ele_loc "
                    + " , COUNT(*) AS no_of_attempt "
                    + " , SUM(acp_ele_time) AS total_ele_time  "
                    + " , MAX(acp_acc_datetime) AS last_access "
                    + " FROM AiccPath WHERE " 
                    + " acp_mod_id = ? AND "
                    + " acp_ent_id = ? "
                    + " GROUP BY acp_ele_loc "
                    + " ORDER BY acp_ele_loc ";

            PreparedStatement stmt = con.prepareStatement(SQL);                      
            stmt.setLong(1, acp_mod_id);
            stmt.setLong(2, acp_ent_id);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next())
            {
                ElementReport er = new ElementReport();
                er.acp_ele_loc = rs.getString("acp_ele_loc");
                er.no_of_attempt = rs.getLong("no_of_attempt");
                er.total_ele_time = rs.getFloat("total_ele_time");
                er.last_access = rs.getTimestamp("last_access");
                vtElementReport.addElement(er);
            }
            stmt.close();
            return vtElementReport;
            
        } catch(SQLException e) {
                throw new qdbException("SQL Error: " + e.getMessage()); 
        }  
    }

    private ElementReport accumElement(ElementReport element, ElementReport sum){
        sum.acp_ele_loc = element.acp_ele_loc;
        sum.no_of_attendant++; 
        sum.no_of_attempt += element.no_of_attempt;
        sum.total_ele_time += element.total_ele_time;
        if (element.last_access != null){
            if (sum.last_access != null){
                if (sum.last_access.before(element.last_access))
                    sum.last_access = element.last_access;
            }else{
                sum.last_access = element.last_access;
            }    
        }        
        return sum;
    } 
    
    private Vector parseVtElement(Vector vtElementReport) throws qdbErrMessage, qdbException{
        String xml = "";
        Vector vtFinalElement = new Vector();
        for (int i=0;i<vtElementReport.size();i++){
            // for new loc
            ElementReport sum = new ElementReport();
            sum = accumElement((ElementReport) vtElementReport.elementAt(i), sum);
            
            while (i<vtElementReport.size()-1){
                // if same loc, accumulate to sum and i++
                // else, break the while loop
                if (((ElementReport)vtElementReport.elementAt(i+1)).acp_ele_loc.equals(sum.acp_ele_loc)){
                    sum = accumElement((ElementReport) vtElementReport.elementAt(i+1), sum);    
                    i++;
                }
                else 
                    break;
            }   
            vtFinalElement.addElement(sum);
        }            
        return vtFinalElement;    
    }
    
    public String getManifest(String res_path) throws qdbException{
        String filename = res_path + dbUtils.SLASH + acp_mod_id + dbUtils.SLASH + "imsmanifest.xml";
        String outSource = "";
        StringBuffer outBuf = new StringBuffer();
        
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(filename), dbUtils.ENC_UTF));
            String inline;
            
            while ((inline = in.readLine()) != null) {
                outBuf.append(inline).append(dbUtils.NEWL);
            }
            in.close();
        } catch (FileNotFoundException e) {
            throw new qdbException("file not found:" +  filename);
        } catch (IOException e) {
            throw new qdbException("read xml file error:" + e.getMessage());
        }
        
        outSource = outBuf.toString();
        
        String start = "<tableofcontents"; 
        String end = "tableofcontents>";
        
        int indexS = outSource.indexOf(start);
        int indexE = outSource.indexOf(end) + 16; // plus no of char in "tableofcontents>"
        
        if (indexS >=0 && indexE >=0) 
            outSource= outSource.substring(indexS, indexE);
        else{
            outSource= outSource.substring(outSource.indexOf("<manifest"));
        }
        return outSource;
    }
    /*
    public static String displayFloat(float odds) {
        odds = (int)(odds * 100);
        String output = Float.toString((float)odds/(float)100);
        if ((odds % 10) == 0) output += "0";
        return output;
    }
    */
    public String getElementXML(Vector vtElementReport, long class_size){
        StringBuffer xmlBuf = new StringBuffer();
        xmlBuf.append("<element_list>");
        for (int i=0;i<vtElementReport.size();i++) {
            ElementReport er = (ElementReport) vtElementReport.elementAt(i); 
            xmlBuf.append("<element_loc id=\"" + er.acp_ele_loc + 
                "\" no_of_attendant=\"" + er.no_of_attendant + 
                "\" percent=\"" + (int)(((float)er.no_of_attendant / (float)class_size ) * 100) + 
                "\" avg_per_learner=\"" + dbAiccPath.getTime((float)er.total_ele_time / (float)er.no_of_attendant) + 
                "\" hit=\"" + er.no_of_attempt + 
                "\" avg_per_hit=\"" + dbAiccPath.getTime((float)er.total_ele_time / (float)er.no_of_attempt) + 
                "\" last_access=\""  +  er.last_access + 
                "\"/>" + dbUtils.NEWL);
        }
        xmlBuf.append("</element_list>");
        return xmlBuf.toString();
    }
    
    public String getLearnerElementXML(Vector vtElementReport){
        StringBuffer xmlBuf = new StringBuffer();
        xmlBuf.append("<element_list>");
        for (int i=0;i<vtElementReport.size();i++) {
            ElementReport er = (ElementReport) vtElementReport.elementAt(i); 
            xmlBuf.append("<element_loc id=\"" + er.acp_ele_loc + 
                "\" hit=\"" + er.no_of_attempt + 
                "\" total_time=\"" + dbAiccPath.getTime(er.total_ele_time) +  
                "\" last_access=\""  +  er.last_access + 
                "\"/>" + dbUtils.NEWL);
        }
        xmlBuf.append("</element_list>");
        return xmlBuf.toString();
    }
    

    public void save(Connection con, loginProfile prof)
        throws qdbException
    {
        String[] date_ = (String[]) paths.get("date");
        String[] time_ = (String[]) paths.get("time");
        String[] element_location_ = (String[]) paths.get("element_location");
        String[] status_ = (String[]) paths.get("status");
        String[] why_left_ = (String[]) paths.get("why_left");
        String[] time_in_element_ = (String[]) paths.get("time_in_element");
        
        // insert record in AiccPath
        for (int i=0;i<date_.length;i++) {
            dbAiccPath dbacp = new dbAiccPath();
            dbacp.acp_cos_id = acp_cos_id;
            dbacp.acp_ent_id = acp_ent_id;
            dbacp.acp_mod_id = acp_mod_id;
            
            // Validate input
            if (date_[i] == null || time_[i] == null || element_location_[i] == null 
                || status_[i] == null || time_in_element_ == null)
                throw new qdbException("Input parameter invalid.");
            
            String datetime = date_[i] + " " + time_[i];
            datetime = dbUtils.subsitute(datetime,"/", "-");
            dbacp.acp_acc_datetime = Timestamp.valueOf(datetime);
            dbacp.acp_ele_loc = element_location_[i];
            dbacp.acp_status = status_[i];
            dbacp.acp_why_left = why_left_[i];
            dbacp.acp_ele_time = convert2Second(time_in_element_[i]);
            
            dbacp.ins(con);
            
        }
        /*
        // update module status
        dbModuleEvaluation dbmov = new dbModuleEvaluation();
        dbmov.mov_cos_id = acp_cos_id;
        dbmov.mov_ent_id = acp_ent_id;
        dbmov.mov_mod_id = acp_mod_id;
        dbmov.mov_status = mod_status;
        dbmov.unload = mod_unload;
        dbmov.save(con);
        
        // update course status
        dbCourseEvaluation dbcov = new dbCourseEvaluation();
        dbcov.cov_cos_id = acp_cos_id;
        dbcov.cov_ent_id = acp_ent_id;
        dbcov.save(con);
        */
    }
    
    public void ins(Connection con)
        throws qdbException
    {
        try {
            PreparedStatement stmt = con.prepareStatement(                      
                    " INSERT INTO AiccPath "
                    + " (acp_cos_id "
                    + " ,acp_ent_id "
                    + " ,acp_mod_id "
                    + " ,acp_acc_datetime "
                    + " ,acp_ele_loc "
                    + " ,acp_status "
                    + " ,acp_why_left "
                    + " ,acp_ele_time ) "
                    + " VALUES (?,?,?,?,?,?,?,?) "); 

            stmt.setLong(1, acp_cos_id);
            stmt.setLong(2, acp_ent_id);
            stmt.setLong(3, acp_mod_id);
            stmt.setTimestamp(4, acp_acc_datetime);
            stmt.setString(5, acp_ele_loc);
            stmt.setString(6, acp_status);
            stmt.setString(7, acp_why_left);
            stmt.setFloat(8, acp_ele_time);
//add stmt.close()
//modified by Lear
            int stmtResult=stmt.executeUpdate();
            stmt.close();
            if ( stmtResult!=1)
            {
                con.rollback(); 
                throw new qdbException("Failed to insert evaluation path."); 
            }
            
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }        
    }
    
    public Vector getAll(Connection con)
        throws qdbException
    {
        try {
            Vector pathVec = new Vector();
            
            PreparedStatement stmt = con.prepareStatement(                      
                    " SELECT "
                    + "  acp_acc_datetime "
                    + " ,acp_ele_loc "
                    + " ,acp_status "
                    + " ,acp_why_left "
                    + " ,acp_ele_time  "
                    + " FROM AiccPath WHERE " 
                    + " acp_cos_id = ? " 
                    + " AND acp_ent_id = ? "
                    + " AND acp_mod_id = ? "
                    + " ORDER BY acp_acc_datetime ASC ");

            stmt.setLong(1, acp_cos_id);
            stmt.setLong(2, acp_ent_id);
            stmt.setLong(3, acp_mod_id);

            ResultSet rs = stmt.executeQuery();
            
            while (rs.next())
            {
                dbAiccPath acp = new dbAiccPath();
                acp.acp_cos_id = acp_cos_id; 
                acp.acp_ent_id = acp_ent_id;
                acp.acp_mod_id = acp_mod_id;
                acp.acp_acc_datetime = rs.getTimestamp("acp_acc_datetime");
                acp.acp_ele_loc = rs.getString("acp_ele_loc");
                acp.acp_status  = rs.getString("acp_status");
                acp.acp_why_left = rs.getString("acp_why_left");
                acp.acp_ele_time = rs.getFloat("acp_ele_time");
                
                pathVec.addElement(acp);
                
            }
            
            stmt.close();
            return pathVec; 
            
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }        
            
    }
    
    public String getPathAsXML(Connection con, loginProfile prof)
        throws qdbException
    {
        String xml = new String(dbUtils.xmlHeader);
        xml += "<student_path>" + dbUtils.NEWL;
        xml += prof.asXML() + dbUtils.NEWL;
        
        xml += getAiccPathAsXML(con);

        xml += "</student_path>" + dbUtils.NEWL;
        return xml;
    }
    
    public String getAiccPathAsXML(Connection con)
        throws qdbException
    {
        String xml = new String("");
        String pathBody = new String("");

        Vector pathVec = getAll(con);
        
        // Calculate the number of distinct visited lesson location
        
        Vector distinctPath = new Vector();
        Timestamp last_acc_datetime = new Timestamp(0);
        
        for (int i=0;i<pathVec.size();i++) {
            dbAiccPath acp = (dbAiccPath) pathVec.elementAt(i); 
            
            if (!distinctPath.contains(new String(acp.acp_ele_loc)))
                distinctPath.addElement(new String(acp.acp_ele_loc));
            
            if (acp.acp_acc_datetime.after(last_acc_datetime))
                last_acc_datetime = acp.acp_acc_datetime;
                
            //    Timestamp yyyy-mm-dd hh:mm:ss.fffffffff"
               String acc_datetime = acp.acp_acc_datetime.toString();
               
                    
               pathBody += "<path course_id=\"" + acp.acp_cos_id + "\" student_id=\"" 
                        +   acp.acp_ent_id + "\" lesson_id=\"" + acp.acp_mod_id 
                        + "\" date=\""  +  dbAiccPath.getDate(acp.acp_acc_datetime) 
                        + "\" time=\""  +  dbAiccPath.getTime(acp.acp_acc_datetime) 
                        + "\" element_in_location=\"" + acp.acp_ele_loc 
                        + "\" status=\"" + acp.acp_status
                        + "\" why_left=\"" + acp.acp_why_left
                        + "\" time_in_element=\"" + dbAiccPath.getTime(acp.acp_ele_time) 
                        + "\"/>" + dbUtils.NEWL;
        }
        
        xml += "<visited_path completed_path=\"" + distinctPath.size() 
            +  "\" last_acc_datetime=\"" + last_acc_datetime + "\">" + dbUtils.NEWL;
        xml += pathBody;
        xml += "</visited_path>" + dbUtils.NEWL;
        
        return xml;
    }
    
    public static String getDate(Timestamp ts) 
    {
        if (ts == null){
            return null;
        }else{
            // Format the current time.
            SimpleDateFormat formatter = new SimpleDateFormat ("yyyy/MM/dd HH:mm");
            String dateString = formatter.format(ts);
        
            return dateString;
        }
    }
    
    public static String getTime(Timestamp ts) 
    {
        // Format the current time.
        SimpleDateFormat formatter = new SimpleDateFormat ("HH:mm:ss.SS");
        String timeString = formatter.format(ts);
        
        return timeString;

    }   
    
    public static String getTime(float seconds) 
    {
        Float Seconds = new Float(seconds);
        if (Seconds.isNaN()){
            return "0:00:00";    
        }
        
        // Conver 23.32 seconds to "00:00:23.32"
        int hr =0;
        int mm =0;
        
        String mmStr = "";
        String ssStr = "";
        Double tmp = null;
        
        tmp = new Double(Math.floor(seconds/(60*60)));
        hr = tmp.intValue();
        
        seconds = seconds - hr*60*60;
        tmp = new Double(Math.floor(seconds/60));
        mm = tmp.intValue();
        
        if (mm < 10) 
            mmStr = "0" + mm;
        else 
            mmStr = "" + mm;
        
        seconds = seconds - mm*60;
        if (seconds < 10)
            ssStr = "0" + seconds;
        else
            ssStr = "" + seconds;
            
        if (ssStr.length() > 2)
            ssStr = ssStr.substring(0,2);
        
        String timeString = hr + ":" + mmStr + ":" + ssStr;
        return timeString;
        
        
    }   
    
    public static float convert2Second(String time_)
    {
        float seconds = 0;
        
        int hr = 0;
        int mm = 0;
        float ss = 0;
        int indexH = time_.indexOf(":");
        hr = Integer.parseInt(time_.substring(0,indexH));
        int indexM = time_.indexOf(":", indexH + 1);
        mm = Integer.parseInt(time_.substring(indexH + 1 ,indexM));
        ss = Float.valueOf(time_.substring(indexM + 1,time_.length())).floatValue();
        
        seconds = hr *60 * 60 + mm * 60 + ss;
        return seconds;
    }
    
    public static long attemptNum(Connection con, long modId)
        throws qdbException
    {
        try {
            PreparedStatement stmt = con.prepareStatement(                      
                    " SELECT  count(acp_mod_id) AS CNT "
                    + "  FROM AiccPath "
                    + " WHERE " 
                    + "  acp_mod_id = ? ");

            stmt.setLong(1, modId);
            ResultSet rs = stmt.executeQuery();
            
            long cnt = 0;
            if (rs.next()) {
                cnt = rs.getLong("CNT");
            }
            stmt.close();
            
            return cnt;
            
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }
    }
}