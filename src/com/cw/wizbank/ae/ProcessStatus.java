package com.cw.wizbank.ae;

/**
 * <p>Title: </p>
 * <p>Description: based version: core version 3.5.52</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Cyberwisdom.net</p>
 * @author Emily
 * @version 1.0
 */
import java.util.Hashtable;

public class ProcessStatus {
    // constant
    public final static String APP = "app";
    public final static String SYS = "sys";
    public final static String SELF = "self";

    private int processId = 0;
    private int statusId = 0;
    private String statusName = null;
    private Hashtable roleList = null;    // key: rol_ext_id; value: app/sys/self
    private String actionXML = null;

    /**
     * constructor
     */
    public ProcessStatus() {
        this.roleList = new Hashtable();
    }

    /**
     * getter
     */
    public int getProcessId() {
        return this.processId;
    }

    public int getStatusId() {
        return this.statusId;
    }

    public String getStatusName() {
        return this.statusName;
    }

    public Hashtable getRoleList() {
        return this.roleList;
    }
	
	public String getActionXML(){
		return this.actionXML;
	}

    /**
     * setter
     */
    public void setProcessid(int pId) {
        this.processId = pId;
    }

    public void setStatusId(int sId) {
        this.statusId = sId;
    }

    public void setStatusName(String sName) {
        this.statusName = sName;
    }

    public void setRoleList(Hashtable list) {
//        this.roleList = (Hashtable)list.clone();
        this.roleList = (Hashtable)list;
    }
    
    public void setActionXML(String xml){
    	this.actionXML = xml;
    	return;
    }

    /**
     * put a role in the list
     * @param roleId: role_ext_id
     * @param roleSrc: app/sys/self
     */
    public void putRole(String roleId, String roleSrc) {
        this.roleList.put(roleId, roleSrc);
    }
}