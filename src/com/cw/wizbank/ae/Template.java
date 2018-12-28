package com.cw.wizbank.ae;

/**
 * <p>Title: </p>
 * <p>Description: based version: core version 3.5.52</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Cyberwisdom.net</p>
 * @author Emily
 * @version 1.0
 */
import java.util.Vector;

public class Template {
    private String title = null;
    private String type = null;
    private Vector statusList = null;
    private String xmlStr = null;

    public Template() {
        this.statusList = new Vector();
    }

    /**
     * getter
     */
    public String getTitle() {
        return this.title;
    }

    public String getType() {
        return this.type;
    }

    public ProcessStatus[] getStatusList() {
        ProcessStatus[] tmpList = new ProcessStatus[0];
        return (ProcessStatus[])this.statusList.toArray(tmpList);
    }

    public String getXmlStr() {
        return this.xmlStr;
    }

    /**
     * setter
     */
    public void setTitle(String value) {
        this.title = value;
    }

    public void setType(String value) {
        this.type = value;
    }

    public void setStatusList(Vector value) {
        this.statusList = (Vector)value.clone();
    }

    public void setXmlStr(String value) {
        this.xmlStr = value;
    }

    /**
     * add a status to the list
     * @param status: new status operate by approver
     */
    public void addStatus(ProcessStatus status) {
        this.statusList.addElement(status);
    }
}