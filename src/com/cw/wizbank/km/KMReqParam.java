package com.cw.wizbank.km;

import java.util.*;
import java.sql.Timestamp;

import javax.servlet.ServletRequest;

import com.cw.wizbank.*;
import com.cw.wizbank.db.*;
import com.cw.wizbank.util.*;
import com.cwn.wizbank.utils.CommonLog;

public class KMReqParam extends ReqParam {
    
    public static final String DELIMITER                    =   "~"; 
    
    public DbKmFolder folder = null;
    public DbKmNode node = null;
    public DbKmNodeSubscription sub = null;
    public DbKmNodeAssignment dbNodeAss = null;
    
    public long nod_id;
    public long[] nod_id_lst;
    
    long[] reader_ent_lst = null;
    long[] author_ent_lst = null;
    long[] owner_ent_lst = null;
    long[] assign_ent_id = null;
    
    //DENNIS: for objects BEGIN
    public long obj_bob_nod_id;
    public String obj_version;
    public Timestamp obj_update_timestamp;
    public String obj_type;
    public String tvw_id;
    public long parent_nod_id;
    public Vector vNodColName;
    public Vector vNodColType;
    public Vector vNodColValue;
    public Vector vObjColName;
    public Vector vObjColType;
    public Vector vObjColValue;
    public Vector vObjClobColName;
    public Vector vObjClobColValue;
    public Vector vFileName;
    public long[] domain_id_list;
    public boolean keepCheckOut;
    public String bob_code;
    //DENNIS: for objects END
    
    // for notification
    public long action_id;
    public Timestamp email_send_timestamp;
    public String sender_usr_id;
    
    // for search
    public String words;
    public String obj_title;
    public String obj_author;
    public String[] obj_type_list;
    public boolean show_deleted;
    public String call_num;
    
    public String usr_id;
    public long usr_ent_id;

    // for Folder and Object
    public String nature;
    
    public KMReqParam(ServletRequest inReq, String clientEnc_, String encoding_)
        throws cwException {
            this.req = inReq;
            this.clientEnc = clientEnc_;
            this.encoding = encoding_;
            
            //Print submited param
            Enumeration enumeration = req.getParameterNames();
            while( enumeration.hasMoreElements() ) {
                String name = (String) enumeration.nextElement();
                String[] values = req.getParameterValues(name);
                //if( values != null )
                //    for(int i=0; i<values.length; i++)
                //        System.out.println(name + " (" + i + "):" + values[i]);
            }
    }
    
    public void getFolder() throws cwException {
        
        getFolderBasic();
        
        folder.fld_title = unicode(getStringParameter("title"));
        folder.fld_desc = unicode(getStringParameter("desc"));
        folder.fld_nature = unicode(getStringParameter("nature"));
        folder.nod_order = getLongParameter("order");
        folder.nod_parent_nod_id = getLongParameter("parent_nod_id");
        folder.nod_acl_inherit_ind = getBooleanParameter("inherit_ind");
        folder.nod_display_option_ind = getIntParameter("display_option_ind");
        
        String prmNm = "reader_ent_lst";
        String var = req.getParameter(prmNm);
        CommonLog.info("var: " + var);
        reader_ent_lst = getLongArrayParameter("reader_ent_lst", DELIMITER);
        author_ent_lst = getLongArrayParameter("author_ent_lst", DELIMITER);
        owner_ent_lst = getLongArrayParameter("owner_ent_lst", DELIMITER);
        
    }

    public void getFolderBasic() throws cwException {
        folder = new DbKmFolder();
        
        folder.fld_nod_id = getLongParameter("nod_id");
        folder.fld_type = getStringParameter("type");
        folder.fld_nature = unicode(getStringParameter("nature"));        
        folder.fld_update_timestamp = getTimestampParameter("update_timestamp");
        
    }

    public void prepInsObject() throws cwException {
        this.obj_type = getStringParameter("obj_type");
        this.nature = getStringParameter("nature");
        this.tvw_id = getStringParameter("tvw_id");
        this.parent_nod_id = getLongParameter("parent_nod_id");
    }

    public void getSubscription() throws cwException {
        sub = new DbKmNodeSubscription();
        
        sub.nsb_nod_id = getLongParameter("nod_id");
        sub.nsb_type = getStringParameter("type");
        sub.nsb_email_send_type = getStringParameter("email_send_type");
        
    }
    
    public void getNodeList() throws cwException {
        nod_id_lst = getLongArrayParameter("nod_id_lst", DELIMITER);
        
    }
    
    public void getNotify() throws cwException {
        sub = new DbKmNodeSubscription();
        
        sub.nsb_usr_ent_id = getLongParameter("usr_ent_id");
        sub.nsb_email_send_type = getStringParameter("email_send_type");
        email_send_timestamp = getTimestampParameter("email_send_timestamp");
        action_id = getLongParameter("action_id");
        sender_usr_id = getStringParameter("sender_usr_id");
        
    }

    public void insObject() throws cwException {
        
        this.vNodColName = new Vector();
        this.vNodColType = new Vector();
        this.vNodColValue = new Vector();
        this.vObjColName = new Vector();
        this.vObjColType = new Vector();
        this.vObjColValue = new Vector();
        this.vObjClobColName = new Vector();
        this.vObjClobColValue = new Vector();
        this.vFileName = new Vector();

        String var;
        String prmNm;

        //get uploaded file names
        Enumeration files = multi.getFileNames();
        while(files.hasMoreElements()) {
            String s = (String)files.nextElement();
            String t = multi.getFilesystemName(s);
            if(t!=null && !t.equalsIgnoreCase("null")) {
                vFileName.addElement(t);
            }
            //vFileName.addElement(multi.getFilesystemName((String)files.nextElement()));
        }

        prmNm = "nod_type";
        var = (bMultipart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            this.vNodColName.addElement("nod_type");
            this.vNodColType.addElement(DbTable.COL_TYPE_STRING);
            this.vNodColValue.addElement(var);
        }

        prmNm = "nod_order";
        var = (bMultipart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            this.vNodColName.addElement("nod_order");
            this.vNodColType.addElement(DbTable.COL_TYPE_LONG);
            this.vNodColValue.addElement(new Long(var));
        }

        prmNm = "nod_parent_nod_id";
        var = (bMultipart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            this.vNodColName.addElement("nod_parent_nod_id");
            this.vNodColType.addElement(DbTable.COL_TYPE_LONG);
            this.vNodColValue.addElement(new Long(var));
        }
        
        prmNm = "nod_display_option_ind";
        var = (bMultipart) ? multi.getParameter(prmNm): req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            this.vNodColName.addElement("nod_display_option_ind");
            this.vNodColType.addElement(DbTable.COL_TYPE_LONG);
            this.vNodColValue.addElement(new Long(var));
        }
        
        prmNm = "obj_version";
        var = (bMultipart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            this.vObjColName.addElement("obj_version");
            this.vObjColType.addElement(DbTable.COL_TYPE_STRING);
            this.vObjColValue.addElement(var);
        }

        prmNm = "obj_publish_ind";
        var = (bMultipart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            this.vObjColName.addElement("obj_publish_ind");
            this.vObjColType.addElement(DbTable.COL_TYPE_BOOLEAN);
            this.vObjColValue.addElement(new Boolean(var));
        }

        prmNm = "obj_latest_ind";
        var = (bMultipart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            this.vObjColName.addElement("obj_latest_ind");
            this.vObjColType.addElement(DbTable.COL_TYPE_BOOLEAN);
            this.vObjColValue.addElement(new Boolean(var));
        }

        prmNm = "obj_type";
        var = (bMultipart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            this.vObjColName.addElement("obj_type");
            this.vObjColType.addElement(DbTable.COL_TYPE_STRING);
            this.vObjColValue.addElement(var);
        }

        prmNm = "obj_title";
        var = (bMultipart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            this.vObjColName.addElement("obj_title");
            this.vObjColType.addElement(DbTable.COL_TYPE_STRING);
            this.vObjColValue.addElement(var);
        }

        prmNm = "obj_desc";
        var = (bMultipart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            this.vObjColName.addElement("obj_desc");
            this.vObjColType.addElement(DbTable.COL_TYPE_STRING);
            this.vObjColValue.addElement(var);
        }

        prmNm = "obj_status";
        var = (bMultipart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            this.vObjColName.addElement("obj_status");
            this.vObjColType.addElement(DbTable.COL_TYPE_STRING);
            this.vObjColValue.addElement(var);
        }

        prmNm = "obj_keywords";
        var = (bMultipart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            this.vObjColName.addElement("obj_keywords");
            this.vObjColType.addElement(DbTable.COL_TYPE_STRING);
            this.vObjColValue.addElement(var);
        }

        prmNm = "obj_comment";
        var = (bMultipart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            this.vObjColName.addElement("obj_comment");
            this.vObjColType.addElement(DbTable.COL_TYPE_STRING);
            this.vObjColValue.addElement(var);
        }

        prmNm = "obj_author";
        var = (bMultipart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            this.vObjColName.addElement("obj_author");
            this.vObjColType.addElement(DbTable.COL_TYPE_STRING);
            this.vObjColValue.addElement(var);
        }

        prmNm = "obj_xml";
        var = (bMultipart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            this.vObjClobColName.addElement("obj_xml");
            this.vObjClobColValue.addElement(var);
        }
        
        prmNm = "obj_code";
        bob_code = (bMultipart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);

        return;
    }

    public void checkInObject() throws cwException {
        
        this.vObjColName = new Vector();
        this.vObjColType = new Vector();
        this.vObjColValue = new Vector();
        this.vObjClobColName = new Vector();
        this.vObjClobColValue = new Vector();
        this.vFileName = new Vector();

        String var;
        String prmNm;

        //get uploaded file names
        Enumeration files = multi.getFileNames();
        while(files.hasMoreElements()) {
            String s = (String)files.nextElement();
            String t = multi.getFilesystemName(s);
            if(t!=null && !t.equalsIgnoreCase("null")) {
                vFileName.addElement(t);
            }
            //vFileName.addElement(multi.getFilesystemName((String)files.nextElement()));
        }

        prmNm = "obj_version";
        var = (bMultipart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            this.vObjColName.addElement("obj_version");
            this.vObjColType.addElement(DbTable.COL_TYPE_STRING);
            this.vObjColValue.addElement(var);
        }

        prmNm = "obj_publish_ind";
        var = (bMultipart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            this.vObjColName.addElement("obj_publish_ind");
            this.vObjColType.addElement(DbTable.COL_TYPE_BOOLEAN);
            this.vObjColValue.addElement(new Boolean(var));
        }

        prmNm = "obj_latest_ind";
        var = (bMultipart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            this.vObjColName.addElement("obj_latest_ind");
            this.vObjColType.addElement(DbTable.COL_TYPE_BOOLEAN);
            this.vObjColValue.addElement(new Boolean(var));
        }

        prmNm = "obj_type";
        var = (bMultipart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            this.vObjColName.addElement("obj_type");
            this.vObjColType.addElement(DbTable.COL_TYPE_STRING);
            this.vObjColValue.addElement(var);
        }

        prmNm = "obj_title";
        var = (bMultipart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            this.vObjColName.addElement("obj_title");
            this.vObjColType.addElement(DbTable.COL_TYPE_STRING);
            this.vObjColValue.addElement(var);
        }

        prmNm = "obj_desc";
        var = (bMultipart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            this.vObjColName.addElement("obj_desc");
            this.vObjColType.addElement(DbTable.COL_TYPE_STRING);
            this.vObjColValue.addElement(var);
        }

        prmNm = "obj_status";
        var = (bMultipart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            this.vObjColName.addElement("obj_status");
            this.vObjColType.addElement(DbTable.COL_TYPE_STRING);
            this.vObjColValue.addElement(var);
        }

        prmNm = "obj_keywords";
        var = (bMultipart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            this.vObjColName.addElement("obj_keywords");
            this.vObjColType.addElement(DbTable.COL_TYPE_STRING);
            this.vObjColValue.addElement(var);
        }

        prmNm = "obj_comment";
        var = (bMultipart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            this.vObjColName.addElement("obj_comment");
            this.vObjColType.addElement(DbTable.COL_TYPE_STRING);
            this.vObjColValue.addElement(var);
        }

        prmNm = "obj_author";
        var = (bMultipart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            this.vObjColName.addElement("obj_author");
            this.vObjColType.addElement(DbTable.COL_TYPE_STRING);
            this.vObjColValue.addElement(var);
        }

        prmNm = "obj_xml";
        var = (bMultipart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            this.vObjClobColName.addElement("obj_xml");
            this.vObjClobColValue.addElement(var);
        }
        
        this.obj_bob_nod_id = getLongParameter("obj_nod_id");
        this.obj_update_timestamp = getTimestampParameter("obj_update_timestamp");

        this.keepCheckOut = getBooleanParameter("keep_checked_out");

        prmNm = "obj_code";
        bob_code = (bMultipart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        
        return;
    }

    public void getObject() throws cwException {
        
        this.obj_bob_nod_id = getLongParameter("obj_nod_id");
        this.obj_version = getStringParameter("obj_version");
        this.obj_type = getStringParameter("obj_type");
        this.tvw_id = getStringParameter("tvw_id");
        this.parent_nod_id = getLongParameter("parent_nod_id");
        
        return;
    }
    
    public void checkOutObject() throws cwException {
        
        this.obj_bob_nod_id = getLongParameter("obj_nod_id");
        this.obj_update_timestamp = getTimestampParameter("obj_update_timestamp");
   
        return;
    }

    public void prepPublish() throws cwException {
        
        this.obj_bob_nod_id = getLongParameter("obj_nod_id");
   
        return;
    }

    public void getObjectAtt() throws cwException {
        
        this.obj_bob_nod_id = getLongParameter("obj_nod_id");
        this.obj_version = getStringParameter("obj_version");
        this.parent_nod_id = getLongParameter("parent_nod_id");
        
        return;
    }

    public void publishObject() throws cwException {
        
        this.obj_update_timestamp = getTimestampParameter("obj_update_timestamp"); 
        this.obj_bob_nod_id = getLongParameter("obj_nod_id");
        this.domain_id_list = getLongArrayParameter("domain_id_list", DELIMITER);
    }

    public void getObjectHist() throws cwException {
        
        this.obj_bob_nod_id = getLongParameter("obj_nod_id");
    }

    public void delObject() throws cwException {
        
        this.obj_bob_nod_id = getLongParameter("obj_nod_id");
        this.obj_update_timestamp = getTimestampParameter("obj_update_timestamp");
    }
    
    public void simpleSearch() throws cwException {
        
        this.nod_id = getLongParameter("nod_id");
        this.obj_type = getStringParameter("obj_type");
        this.words = unicode(getStringParameter("words"));
    }
    
    public void addToMyWorkplace() throws cwException {
        
        this.dbNodeAss = new DbKmNodeAssignment();
        this.dbNodeAss.nam_nod_id = getLongParameter("nod_id");
        this.parent_nod_id = getLongParameter("parent_nod_id");
        return;
    }
    
    public void removeFromMyWorkplace() throws cwException {
        
        this.dbNodeAss = new DbKmNodeAssignment();
        this.dbNodeAss.nam_nod_id = getLongParameter("nod_id");
        return;
        
    }
    
    public void assignWorkplace() throws cwException {
        
        this.dbNodeAss = new DbKmNodeAssignment();
        this.dbNodeAss.nam_nod_id = getLongParameter("nod_id");
        this.parent_nod_id = getLongParameter("parent_nod_id");
        this.assign_ent_id = getLongArrayParameter("assign_ent_lst", DELIMITER);
        return;
        
    }
    
    
    public void getFolderAssignedWorkplace() throws cwException {
        
        this.nod_id = getLongParameter("nod_id");
        return;
        
    }
    
    public void getObjAssignedWorkplace()  throws cwException {
        
        this.nod_id = getLongParameter("nod_id");
        this.parent_nod_id = getLongParameter("parent_nod_id");
        return;
        
    }
    
    public void getUser() throws cwException{
        
        this.usr_ent_id = getLongParameter("usr_ent_id");
        this.usr_id = getStringParameter("usr_id");
        return;
    }

    public void advSearch() throws cwException{
        this.obj_title = getStringParameter("obj_title");
        this.obj_author = getStringParameter("obj_author");
        this.words = unicode( getStringParameter("key_words") );
        this.nod_id_lst = getLongArrayParameter("nod_id_lst", DELIMITER);
        this.obj_type_list = cwUtils.splitToString( getStringParameter("obj_type_lst"), DELIMITER );
        this.show_deleted = getBooleanParameter("show_deleted");
        this.call_num = getStringParameter("call_num");
        return;
    }
}
