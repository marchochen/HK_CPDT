package com.cw.wizbank.supervise;

import java.sql.*;
import java.util.*;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.db.view.ViewSuperviseTargetEntity;

/**
Supervisor is a class representing a supervisor in wizBank
*/
public class Supervisor extends dbRegUser {
    
    public static final String SEARCH_MY_STAFF = "my_staff";
    public static final String SEARCH_MY_DIRECT_STAFF = "my_direct_staff";
    
    /**
    Vector of Long objects contains this supervisor's staff ent_id
    */
    private Vector vStaff;

    /**
    Vector of Long objects contains this supervisor's direct staff ent_id
    */
    private Vector vDirectStaff;

    /**
    Vector of Long objects contains this supervisor's group staff ent_id
    */
    private Vector vGroupStaff;
    
    /**
    Create a Supervisor
    @param con Connection to database
    @param usr_ent_id user entity id of the supervisor
    */
    public Supervisor(Connection con, long usr_ent_id) throws cwException {
        super();
        
        this.usr_ent_id = usr_ent_id;
        this.ent_id = usr_ent_id;
        try {
            get(con);
        } catch (qdbException qdbe) {
            throw new cwException(qdbe.getMessage());
        }
    }
    
    /**
    Test if this supervisor has any staff
    @param con Connection to database
    @return true if and only if the supervisor has at least one staff; false otherwise
    */
    public boolean hasStaff(Connection con) throws SQLException {
        ViewSuperviseTargetEntity view = new ViewSuperviseTargetEntity();
        return view.hasStaff(con, this.usr_ent_id);
    }

    /**
    Get the number of staff of this supervisor
    @param con Connection
    @return the number of staff 
    */
    public long getStaffCount(Connection con) throws SQLException {
        ViewSuperviseTargetEntity view = new ViewSuperviseTargetEntity();
        return view.getStaffCount(con, this.usr_ent_id);
    }

    /**
    Get the number of direct reports staff of this supervisor
    @param con Connection to database
    @return the number of direct report staff
    */
    public long getDirectStaffCount(Connection con) throws SQLException {
        ViewSuperviseTargetEntity view = new ViewSuperviseTargetEntity();
        return view.getDirectStaffCount(con, this.usr_ent_id);
    }

    /**
    Test if the specified user is one of the supervisor's staff
    @param con Connection to database
    @param staff_usr_ent_id user entity id of the testing staff
    @return true if and only if the specified user is one of the supervisor's staff; false otherwise
    */
    public boolean isMyStaff(Connection con, long staff_usr_ent_id) throws SQLException {
        ViewSuperviseTargetEntity view = new ViewSuperviseTargetEntity();
        return view.isMyStaff(con, this.usr_ent_id, staff_usr_ent_id);
    }

    /**
    Get a XML showing this supervisor's all staff count and direct staff count
    @param con Connection to database
    @retunr XML showing this supervisor's all staff count and direct staff count
    */
    public String getStaffCountAsXML(Connection con) throws SQLException {
        StringBuffer xmlBuf = new StringBuffer(256);
        long staffCount = getStaffCount(con);
        long directStaffCount = getDirectStaffCount(con);
        xmlBuf.append("<supervisor ent_id=\"").append(this.usr_ent_id).append("\">")
              .append("<staff_count type=\"").append("all").append("\">").append(staffCount).append("</staff_count>")
              .append("<staff_count type=\"").append("direct").append("\">").append(directStaffCount).append("</staff_count>")
              .append("</supervisor>");
        return xmlBuf.toString();
    }

    /**
    Get the entity ids of the staff of this supervisor from database
    @param con Connection to database
    @return Vector of Long objects containing the staffs' entity ids
    */
    public Vector getStaffEntIdVector(Connection con) throws SQLException {
        ViewSuperviseTargetEntity view = new ViewSuperviseTargetEntity();
        this.vStaff = view.getStaffEntIdVector(con, this.usr_ent_id);
        return this.vStaff;
    }

    /**
    Get the entity ids of the staff of this supervisor.
    The Vector returned is the result when getStaffEntIdVector(Connection con) is last called.
    @return Vector of Long objects containing the staffs' entity ids
    */
    public Vector getStaffEntIdVector() {
        return this.vStaff;
    }

    /**
    Get the entity ids of the direct staff of this supervisor from database
    @param con Connection to database
    @return Vector of Long objects containing the direct staffs' entity ids
    */
    public Vector getDirectStaffEntIdVector(Connection con) throws SQLException {
        ViewSuperviseTargetEntity view = new ViewSuperviseTargetEntity();
        this.vDirectStaff = view.getDirectStaffEntIdVector(con, this.usr_ent_id);
        return this.vDirectStaff;
    }

    /**
    Get the entity ids of the staff of this supervisor.
    The Vector returned is the result when getDirectStaffEntIdVector(Connection con) is last called.
    @return Vector of Long objects containing the direct staffs' entity ids
    */
    public Vector getDirectStaffEntIdVector() {
        return this.vDirectStaff;
    }
    
    /**
    Get the entity ids of the staff of this supervisor.
    The Vector returned is the result when getGroupStaffEntIdVector(Connection con) is last called.
    @return Vector of Long objects containing the group staffs' entity ids
    */
    public Vector getGroupStaffEntIdVector() {
        return this.vGroupStaff;
    }
    
    /**
    Get supervise groups of this supervisor
    @param con Connection to database
    @return Vector of dbUserGroup objects representing the supervisor's supervise groups
    */
    public Vector getSuperviseGroup(Connection con, String cur_lan) throws SQLException {
        ViewSuperviseTargetEntity view = new ViewSuperviseTargetEntity();
        return view.getSuperviseGroup(con, this.usr_ent_id, cur_lan);
    }
    
    // return vector of dbRegUser ojbect, contains usr_ent_id, usr_display_bil
    public Vector getDirectStaff(Connection con) throws SQLException{
        ViewSuperviseTargetEntity view = new ViewSuperviseTargetEntity();
        return view.getDirectStaffInfoVector(con, this.usr_ent_id);
    }

}
