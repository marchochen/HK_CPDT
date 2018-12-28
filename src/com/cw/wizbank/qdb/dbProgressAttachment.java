package com.cw.wizbank.qdb;

import java.sql.*;
import java.util.*;

public class dbProgressAttachment 
{
    
    String pat_prg_usr_id;
    long pat_prg_res_id;
    long pat_prg_attempt_nbr;
    long pat_att_id;
    long pat_tkh_id;
    
    long attNum;  //store the attachment number of a Progress


    //Constructor, need to specify the keys in Progress
    public dbProgressAttachment(String usr_id, long res_id, long attempt_nbr, long tkh_id) {
        pat_prg_usr_id = usr_id;    
        pat_prg_res_id = res_id;
        pat_prg_attempt_nbr = attempt_nbr;
        pat_tkh_id = tkh_id;
    }
    
    //Constructor, temporary
    public dbProgressAttachment(String usr_id, long res_id, long attempt_nbr) {
        pat_prg_usr_id = usr_id;    
        pat_prg_res_id = res_id;
        pat_prg_attempt_nbr = attempt_nbr;
    }
    
    public dbProgressAttachment() {
        super();
    }
    
    
    public String asXML(Connection con, loginProfile prof) throws qdbException {

        Vector attVector;
        dbAttachment att;
        int i;
        String result = "<?xml version=\"1.0\" encoding=\"" + dbUtils.ENC_UTF + "\" standalone=\"no\" ?>" + dbUtils.NEWL;
        try {        
            result += "<progress usr_id=\""+ dbUtils.esc4XML(dbRegUser.usrId2SteUsrId(con,pat_prg_usr_id)) + "\" res_id=\"" + pat_prg_res_id + "\" attempt_nbr=\"" + pat_prg_attempt_nbr +"\">" + dbUtils.NEWL;    
            // author's information
            result += prof.asXML() + dbUtils.NEWL;
                
            result += dbResourcePermission.aclAsXML(con,pat_prg_res_id,prof);
            // Module Header 
            //result += getModHeader(con);
                
            result += "<body>" + dbUtils.NEWL;

            attVector = get(con);  //get the attachment of a Progress as a Vector

            for(i=0;i<attVector.size();i++) {
                att = (dbAttachment) attVector.elementAt(i);
                result += "<attachment id=\"" + att.att_id ; 
                result += "\" type=\"" + att.att_type ; 
                result += "\" filename=\"" + att.att_filename;
                result += "\" desc=\"" + dbUtils.esc4XML(att.att_desc);
                result += "\">" + "</attachment>" + dbUtils.NEWL;
            }
            
            result += "</body>" + dbUtils.NEWL;
            result += "</progress>"; 
                
            return result;
        }
        catch(SQLException e) {
            throw new qdbException(e.getMessage());
        }
    }
    
        
    //return tht attachment of a Progress as a Vector
    public Vector get(Connection con) throws qdbException {
        dbAttachment att;
        String att_idLst = getAtt_idLst(con,false);  //get the att_id of all the attachments
        att_idLst = att_idLst.substring(1,att_idLst.length()-1);
        StringTokenizer att_idLstTok = new StringTokenizer(att_idLst, ",");
        Vector v = new Vector();
        int i;
        
        //for each att_id in the array
        //new a dbAttachment and put it into a Vector
        while(att_idLstTok.hasMoreTokens()) {
            att = new dbAttachment(con, Long.parseLong(att_idLstTok.nextToken()));
            v.addElement(att);
        }
        return v;
    }

    
    //input a vector of attachments and their parentFilenames
    //and insert them into tables
    public void ins(Connection con, loginProfile prof, Vector attLst, Vector parentFileLst) 
      throws qdbException, qdbErrMessage {
        int i;
        long parentAtt_id;
        dbAttachment att;
        String parentFilename;
/*
        String privilege=null;

        if(prof.sysRoles.contains(dbUserGroup.USG_ROLE_TEACHER)) 
            privilege = dbResourcePermission.RIGHT_WRITE;
        else if(prof.sysRoles.contains(dbUserGroup.USG_ROLE_STUDENT)) 
            privilege = dbResourcePermission.RIGHT_EXECUTE;
*/        
        // check User Right
//        if (!dbResourcePermission.hasPermission(con, pat_prg_res_id, prof, privilege)) {
//            throw new qdbErrMessage("You don't have permission to " + privilege + " the record.");
//        }

        //check if the attachment vector size == parent file name vector size    
        if(attLst.size() != parentFileLst.size()) 
            throw new qdbException ("Number of attachments not equals to number of parents.");
          
          
        for(i=0;i<attLst.size();i++) {
            att = (dbAttachment) attLst.elementAt(i);
            parentFilename = (String) parentFileLst.elementAt(i);
            parentAtt_id = getAtt_id(con, parentFilename);  //get parent att_id
            att.att_att_id_parent = parentAtt_id;  //set the parent att_id in att
            pat_att_id = att.ins(con);  //insert into Attachment
            ins(con);  //insert into ProgressAttachment
        }
    }
    
    
    public void ins(Connection con, loginProfile prof, Vector attLst) 
      throws qdbException, qdbErrMessage {
        int i;
        dbAttachment att;
/*        
        String privilege=null;
        
        if(prof.sysRoles.contains(dbUserGroup.USG_ROLE_TEACHER)) 
            privilege = dbResourcePermission.RIGHT_WRITE;
        else if(prof.sysRoles.contains(dbUserGroup.USG_ROLE_STUDENT)) 
            privilege = dbResourcePermission.RIGHT_EXECUTE;
*/        
        // check User Right
//        if (!dbResourcePermission.hasPermission(con, pat_prg_res_id, prof, privilege)) {
//            throw new qdbErrMessage("You don't have permission to " + privilege + " the record.");
//        }
        
        //loop through the input Vector and insert the attachments into tables
        for(i=0;i<attLst.size();i++){
            att = (dbAttachment) attLst.elementAt(i);
            pat_att_id = att.ins(con);  //insert into Attachment
            ins(con);   //insert into ProgressAttachment
        }
    }  
    
    
    //insert a record into ProgressAttachment
    public void ins(Connection con) throws qdbException {
        try {
            String SQL = "Insert into ProgressAttachment ";
            SQL += "(pat_prg_usr_id, pat_prg_res_id, pat_prg_attempt_nbr, pat_att_id, pat_tkh_id) ";
            SQL += "values (?, ?, ?, ?, ?)";
        
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setString(1, pat_prg_usr_id);
            stmt.setLong(2, pat_prg_res_id);
            stmt.setLong(3, pat_prg_attempt_nbr);
            stmt.setLong(4, pat_att_id);
            stmt.setLong(5, pat_tkh_id);
            int stmtResult=stmt.executeUpdate();
            stmt.close();
            if ( stmtResult!=1)                            
            {
                con.rollback();
                throw new qdbException ("Insert into ProgressAttachment failed.");  
            }
            
        }
        catch(SQLException e) {
            throw new qdbException (e.getMessage());
        }
    }
    
    
    public void delAll(Connection con, loginProfile prof, long res_id, long attempt_nbr)
      throws qdbException, qdbErrMessage {
        dbAttachment att;
        int i;
/*
        String privilege=null;

        if(prof.sysRoles.contains(dbUserGroup.USG_ROLE_TEACHER)) 
            privilege = dbResourcePermission.RIGHT_WRITE;
        else if(prof.sysRoles.contains(dbUserGroup.USG_ROLE_STUDENT)) 
            privilege = dbResourcePermission.RIGHT_EXECUTE;
*/        
        // check User Right
//        if (!dbResourcePermission.hasPermission(con, res_id, prof, privilege)) {
//            long cosId  = dbModule.getCosId(con,res_id); 
//            if (!dbResourcePermission.hasPermission(con, cosId, prof, privilege)) {
//                throw new qdbErrMessage("You don't have permission to " + privilege + " the record.");
//            }
//        }
        
        pat_prg_res_id = res_id;
        pat_prg_attempt_nbr = attempt_nbr;
        
        String att_idLst = getAtt_idLst(con,true);  //the the att_id of all the users' attachments
        
        del(con,true);  //del all users' attachments
        if(!isEmptyLst(att_idLst)) {
            att = new dbAttachment();
            att.delInLst(con, att_idLst);  //del from Attachment
        }
    }
    
    //delete all the attachments of a Progress
    public void delAll(Connection con, loginProfile prof) 
      throws qdbException, qdbErrMessage {
        dbAttachment att;
        int i;
/*
        String privilege=null;
        
        if(prof.sysRoles.contains(dbUserGroup.USG_ROLE_TEACHER)) 
            privilege = dbResourcePermission.RIGHT_WRITE;
        else if(prof.sysRoles.contains(dbUserGroup.USG_ROLE_STUDENT)) 
            privilege = dbResourcePermission.RIGHT_EXECUTE;
*/        
        // check User Right
//        if (!dbResourcePermission.hasPermission(con, pat_prg_res_id, prof, privilege)) {
//            long cosId  = dbModule.getCosId(con,pat_prg_res_id); 
//            if (!dbResourcePermission.hasPermission(con, cosId, prof, privilege)) {
//                throw new qdbErrMessage("You don't have permission to " + privilege + " the record.");
//            }
//        }
        
        String att_idLst = getAtt_idLst(con,false);  //get the att_id of all the attachments 
        
        if(!isEmptyLst(att_idLst)) {
            att = new dbAttachment();
            delInLst(con,att_idLst);  //del from ProgressAttachment
            att.delInLst(con,att_idLst);  //del from Attachment
        }
    }
    
    public void delAllTechAtt(Connection con, loginProfile prof) 
      throws qdbException, qdbErrMessage {
        dbAttachment att;
        int i;
/*
        String privilege=null;
        
        if(prof.sysRoles.contains(dbUserGroup.USG_ROLE_TEACHER)) 
            privilege = dbResourcePermission.RIGHT_WRITE;
        else if(prof.sysRoles.contains(dbUserGroup.USG_ROLE_STUDENT)) 
            privilege = dbResourcePermission.RIGHT_EXECUTE;
*/        
        String att_idTechLst = getAtt_idTechLst(con,false);
        //System.out.println("IdTechLst = " + att_idTechLst);
        
        if(!isEmptyLst(att_idTechLst)) {
            att = new dbAttachment();
            delInLst(con,att_idTechLst);  //del from ProgressAttachment
            att.delInLst(con, att_idTechLst);  //del from Attachment
        }
    }
    
    public boolean isEmptyLst(String Lst) {
        boolean result = true;
        if(Lst != null && !Lst.equalsIgnoreCase("()"))
            result = false;
        return result;
    }
    
    public void delInLst(Connection con, String idLst) throws qdbException {
        try {
            String SQL = "Delete from ProgressAttachment "
                       + " Where pat_att_id in " + idLst;
                       
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.executeUpdate();
            stmt.close();
        }
        catch (SQLException e) {
            throw new qdbException(e.getMessage());
        }        
    }
    
    //delete attachments of a Progress according to a filename list
    public void delAttLst(Connection con, loginProfile prof, String [] files) 
      throws qdbException, qdbErrMessage {
        int i;    
        dbAttachment att;
/*
        String privilege=null;

        if(prof.sysRoles.contains(dbUserGroup.USG_ROLE_TEACHER)) 
            privilege = dbResourcePermission.RIGHT_WRITE;
        else if(prof.sysRoles.contains(dbUserGroup.USG_ROLE_STUDENT)) 
            privilege = dbResourcePermission.RIGHT_EXECUTE;
*/            
        // check User Right
//        if (!dbResourcePermission.hasPermission(con, pat_prg_res_id, prof, privilege)) {
//            long cosId  = dbModule.getCosId(con,pat_prg_res_id); 
//            if (!dbResourcePermission.hasPermission(con, cosId, prof, privilege)) {
//                throw new qdbErrMessage("You don't have permission to " + privilege + " the record.");
//            }
//        }
        
        //loop through the file list to del records in tables
        for(i=0;i<files.length;i++){
            att = new dbAttachment(con, getAtt_id(con, files[i]));
            pat_att_id = getAtt_id(con, files[i]);
            del(con, false);  //del from ProgressAttachment
            att.del(con);  //del from Attachment
        }
    }


    //delete a record from ProgressAttachment
    public void del(Connection con, boolean allUsers) throws qdbException {
        try {
            int rowsAffected=0;
            String SQL = "Delete from ProgressAttachment ";
            SQL += "Where pat_prg_res_id = ? ";
            SQL += "And pat_prg_attempt_nbr = ? ";
            
            if(allUsers == false) {  
                SQL += "And pat_att_id = ? ";
                SQL += "And pat_prg_usr_id = ? ";
                SQL += "And pat_tkh_id = ? ";
            }
            
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1,pat_prg_res_id);
            stmt.setLong(2,pat_prg_attempt_nbr);
            
            if(allUsers == false) {
                stmt.setLong(3,pat_att_id);
                stmt.setString(4, pat_prg_usr_id);
                stmt.setLong(5, pat_tkh_id);
            }
            
            rowsAffected = stmt.executeUpdate();
            stmt.close();
            if(allUsers == false && rowsAffected != 1) {
                con.rollback();
                throw new qdbException("Not only one record deleted from ProgressAttachment for pat_att_id = " + pat_att_id);
            }
        }
        catch(SQLException e) {
            throw new qdbException (e.getMessage());
        }
    }


    //return a String that stores the att_id of all the attachemnt of a Progress
    //e.g. (1,12,125)
    public String getAtt_idLst(Connection con, boolean allUsers) throws qdbException {
        try {
            int i=0;
            String att_idLst="(";
            long temp_id;
            
            String SQL = "Select pat_att_id ";
            SQL += "From ProgressAttachment ";
            SQL += "Where pat_prg_res_id = ? ";
            SQL += "And pat_prg_attempt_nbr = ? ";
            if(allUsers == false) {
                SQL += "And pat_prg_usr_id = ? ";
                SQL += "And pat_tkh_id = ? ";
            }
            SQL += "Order by pat_att_id ";
                
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, pat_prg_res_id);
            stmt.setLong(2, pat_prg_attempt_nbr);
            if(allUsers == false) {
                stmt.setString(3, pat_prg_usr_id);
                stmt.setLong(4, pat_tkh_id);
            }
            
            ResultSet rs = stmt.executeQuery();
            i = 0;
            while(rs.next() == true) {
                att_idLst += rs.getLong(1) +",";
                i++;
            }

            if(i>0) 
                att_idLst = att_idLst.substring(0,att_idLst.length()-1) + ")";
            else
                att_idLst = "()";
            
            stmt.close();
            return att_idLst;
        }
        catch (SQLException e) {
            throw new qdbException (e.getMessage());
        }
    }

    //return a String that stores the att_id of all the techer attachemnt of a Progress
    //e.g (1,12,125)
    public String getAtt_idTechLst(Connection con, boolean allUsers) throws qdbException {
        try {
            long temp_id;
            String att_idLst="(";
            int i = 0;
            
            
            String SQL = "Select pat_att_id ";
            SQL += "From ProgressAttachment, Attachment ";
            SQL += "Where pat_prg_res_id = ? ";
            SQL += "And pat_prg_attempt_nbr = ? ";
            SQL += "And att_type = ? ";
            SQL += "And att_id = pat_att_id ";
            if(allUsers == false) {
                SQL += "And pat_prg_usr_id = ? ";
                SQL += "And pat_tkh_id = ? ";
            }
            SQL += "Order by pat_att_id ";
                
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, pat_prg_res_id);
            stmt.setLong(2, pat_prg_attempt_nbr);
            stmt.setString(3, dbAttachment.ATT_TYPE_TEACHER);
            if(allUsers == false) {
                stmt.setString(4, pat_prg_usr_id);
                stmt.setLong(5, pat_tkh_id);
            }
            ResultSet rs = stmt.executeQuery();
            while(rs.next() == true) {
                att_idLst += rs.getLong(1) + ",";
                i++;
            }
            if(i>0)     
                att_idLst = att_idLst.substring(0, att_idLst.length() - 1) + ")";
            else
                att_idLst = "()";
            
            stmt.close();
            return att_idLst;
        }
        catch (SQLException e) {
            throw new qdbException (e.getMessage());
        }
    }

    //input a filename and return the corresponding att_id of a progress
    public long getAtt_id(Connection con, String filename) throws qdbException {
        try {
            String SQL = "Select pat_att_id ";
            SQL += "From Attachment, ProgessAttachment ";
            SQL += "Where pat_prg_usr_id = ? ";
            SQL += "And pat_prg_res_id = ? ";
            SQL += "And pat_prg_attempt_nbr = ? ";
            SQL += "And pat_att_id = att_id ";
            SQL += "And att_filename = ? ";
            SQL += "And pat_tkh_id = ? ";
            
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setString(1, pat_prg_usr_id);
            stmt.setLong(2, pat_prg_res_id);
            stmt.setLong(3,pat_prg_attempt_nbr);
            stmt.setString(4, filename);
            stmt.setLong(5, pat_tkh_id);
            
            ResultSet rs = stmt.executeQuery();
            if(rs.next() == true) { 
                pat_att_id = rs.getLong(1);
            }
            else {
            	stmt.close();
                con.rollback();
                throw new qdbException("Record for file " + filename + " not found.");
            }
            
            stmt.close();
            return pat_att_id;
        }
        catch(SQLException e) {
            throw new qdbException(e.getMessage());
        }
    }

    
    
    //return the number of attachments of a Progress
    public long attNum(Connection con) throws qdbException {
        try {
            String SQL = "Select count(*) from ProgressAttachment ";
            SQL += " Where pat_prg_usr_id = ? ";
            SQL += " And pat_prg_res_id = ? ";
            SQL += " And pat_prg_attempt_nbr = ? ";
            SQL += " And pat_tkh_id = ? ";
            
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setString(1, pat_prg_usr_id);
            stmt.setLong(2, pat_prg_res_id);
            stmt.setLong(3, pat_prg_attempt_nbr);
            stmt.setLong(4, pat_tkh_id);
            
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next() == true) { 
                attNum = rs.getLong(1);
            }
            else {
            	stmt.close();
                con.rollback();
                throw new qdbException("Cannot get the count from ProgressAttachment");
            }
            
            stmt.close();
            return attNum;

        
        }
        catch(SQLException e) {
            throw new qdbException(e.getMessage());
        }
    }
}