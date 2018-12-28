package com.cw.wizbank.enterprise;

//import standard java classes
import java.lang.Long;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Iterator;
import java.io.IOException;


// import jaxb classes
import javax.xml.bind.*;

//import generated IMS classes
import org.imsglobal.enterprise.*;

/*
import org.imsglobal.enterprise.Grouptype;
import org.imsglobal.enterprise.Typevalue;
import org.imsglobal.enterprise.Group;
import org.imsglobal.enterprise.Description;
import org.imsglobal.enterprise.Sourcedid;
import org.imsglobal.enterprise.Timeframe;
import org.imsglobal.enterprise.Begin;
import org.imsglobal.enterprise.End;
import org.imsglobal.enterprise.Enrollcontrol;
import org.imsglobal.enterprise.Extension;
import org.imsglobal.enterprise.CwnExtension;
import org.imsglobal.enterprise.CwnCreditList;
import org.imsglobal.enterprise.CwnCredit;
import org.imsglobal.enterprise.CwnExt;
import org.imsglobal.enterprise.CwnItemTitle;
import org.imsglobal.enterprise.CwnTitle;
import org.imsglobal.enterprise.Relationship;
*/
//import cyberwisdom classes
import com.cw.wizbank.util.*;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeItemRelation;
import com.cw.wizbank.qdb.dbCourse;
import com.cw.wizbank.qdb.dbResource;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.db.DbTable;
import com.cw.wizbank.ae.db.DbItemType;
import com.cw.wizbank.ae.db.DbFigureType;// import com.cw.wizbank.ae.db.DbItemCredit;
import com.cw.wizbank.ae.db.DbFigure; //import com.cw.wizbank.ae.db.DbItemCreditValue;
import com.cw.wizbank.ae.db.view.ViewItemTemplate;
import com.cwn.wizbank.utils.CommonLog;

/**
IMSItem represents a special kind of Group and this
kind of Group is represented as aeItem in wizBank.
*/
public class IMSItem
{
    public static final String GROUP_TYPE_SCHEME_ITEM = "cwn item";
    public static final String GROUP_TYPE_LEVEL_ITEM = "2";
    public static final String GROUP_TYPE_CONTENT_ITEM = "COS";

    public static final String GROUP_TYPE_LEVEL_ITEMTYPE = "3";

    public static final String GROUP_TYPE_LEVEL_SESSION = "4";
    public static final String GROUP_TYPE_CONTENT_SESSION = "SESSION";

    // session under a run
    public static final String GROUP_TYPE_LEVEL_RUN_SESSION = "5";
    public static final String GROUP_TYPE_CONTENT_RUN_SESSION = "SESSION";

    public static final String GROUP_TYPE_LEVEL_RUN = "4";
    public static final String GROUP_TYPE_CONTENT_RUN = "RUN";
    
    public static final String RELATION_TYPE_PARENT_CODE = "1";
    public static final String RELATION_TYPE_PARENT = "Parent";

    public static final String RELATION_TYPE_CHILD_CODE = "2";
    public static final String RELATION_TYPE_CHILD = "Child";
    
    public static final String RELATION_TYPE_KNOWN_AS_CODE = "3";
    public static final String RELATION_TYPE_KNOWN_AS = "KnownAs";

    public static final String RELATIONSHIP_LABEL_COURSESESSION = "Course Session";
    public static final String RELATIONSHIP_LABEL_RUNSESSION = "Run Session";
    public static final String RELATIONSHIP_LABEL_COURSERUN = "Course Training";

    // itemtype as key, int[] as value 
    protected static Hashtable htExtension = null;
    protected long wbItemId;
    protected long pWbItmId;
    protected String wbItemType;
    protected boolean isSession;
    protected boolean isRun;
    protected loginProfile wbProfile;
    protected String sourceDIDID;
//    public List _Sourcedid;
//    public List _Grouptype;
//    public List _Relationship;
    
    public static Hashtable htCreditId = new Hashtable();

    protected GroupType _group;

    
    public static void setExportExtension(String extension_map) throws cwException{
        htExtension = new Hashtable();
        String[] itemtype_map = cwUtils.splitToString(extension_map, ";");
        String itemtype = null;
        String s_extensions;
        String[] extensions = null;
        int index; 
        for (int i=0; i<itemtype_map.length; i++){
            if (itemtype_map[i].length()>0){
                index = itemtype_map[i].indexOf(":");
                if (index>0 && index+1<itemtype_map[i].length()){
                    itemtype = itemtype_map[i].substring(0, index); 
                    s_extensions = itemtype_map[i].substring(index+1, itemtype_map[i].length());
                    extensions = cwUtils.splitToString(s_extensions, ",");
                    htExtension.put(itemtype, extensions);            
                }else{
                    throw new cwException("invalid EXPORT_ITEM_EXTENSION:" + extension_map);    
                }
            }
        }
        
    }
    
    public IMSItem() throws JAXBException{
        org.imsglobal.enterprise.ObjectFactory objFactory = new org.imsglobal.enterprise.ObjectFactory();
        // create an empty group
        _group = objFactory.createGroup();
    }
    /**
    Construct a new IMSItem that contains data currently contained in the Group argument.
    @param group a Group object with data
    */
    public IMSItem(GroupType group){
        this._group = group;
//        setRecstatus(group.getRecstatus());
//        setComments(group.getComments());
//        setDescription(group.getDescription());
//        setOrg(group.getOrg());
//        setTimeframe(group.getTimeframe());
//        setEnrollcontrol(group.getEnrollcontrol());
//        setEmail(group.getEmail());
//        setUrl(group.getUrl());
//        setDatasource(group.getDatasource());
//        setExtension(group.getExtension());
//        this._Sourcedid = group.getSourcedid();
//        this._Grouptype = group.getGrouptype();
//        this._Relationship = group.getRelationship();
//        getSourceDIDID();
        return;
    }

    /**
    Get List of Sourcedid
    */
/*    public List getSourcedid() {
        if(this._Sourcedid == null) {
            return _group.getSourcedid();
        } else {
            return this._Sourcedid;
        }
    }
    */
    
    /**
    Get List of Grouptype
    */
    /*
    public List getGrouptype() {
        if(this._Grouptype == null) {
            return _group.getGrouptype();
        } else {
            return this._Grouptype;
        }
    }
    */
    /**
    Get List of Relationship
    */
    /*
    public List getRelationship() {
        if(this._Relationship == null) {
            return _group.getRelationship();
        } else {
            return this._Relationship;
        }
    }
    */
    
    /**
    Return values of enterprise.group.sourcedid.id of this IMSItem (Group).
    @return sourcedid.id of this IMSItem
    */
    
    public String getSourceDIDID() {
        this.sourceDIDID = ((SourcedidType) _group.getSourcedid().iterator().next()).getId();
        return this.sourceDIDID;
    }
    
    
    /**
    Check if the input Group is an IMSItem 
    @return true input Group is an IMSItem
    */
    public static boolean IMSvalidate(GroupType group) {
        boolean result = true;
        List l_grouptype = group.getGrouptype();
        if(l_grouptype == null || l_grouptype.size() == 0) {
            //no <grouptype> found, assume its correct
            result = true;
        } else {
            //assume typevalue and scheme are incorrect,
            //if one correct typevalue, scheme is found,
            //true result = true and return
            result = false;
            for (Iterator i = l_grouptype.iterator(); i.hasNext(); ){
                GroupType.GrouptypeType grouptype = (GroupType.GrouptypeType)i.next();
                String scheme = grouptype.getScheme();
                List l_typevalue = grouptype.getTypevalue();
                for (Iterator j = l_typevalue.iterator(); j.hasNext(); ){
                    TypevalueType typevalue = (TypevalueType)j.next();
                    String typevalueLevel = typevalue.getLevel();
                    String typevalueContent = typevalue.getValue();
                    try {
                        if(typevalueLevel.equals(GROUP_TYPE_LEVEL_ITEM) 
                        && typevalueContent.equals(GROUP_TYPE_CONTENT_ITEM)
                        && scheme.equals(GROUP_TYPE_SCHEME_ITEM)) {
                            result = true;
                            break;
                        }
                    } catch(NullPointerException e) {
                        continue;
                        //throw new cwException("IMSItem.IMSvalidate: The following cannot be NULL, enterprise.group.grouptype.typevalue.level, enterprise.group.grouptype.typevalue.content, enterprise.group.grouptype.scheme");
                    }
                }
                if(result == true) break;
            }
        }
        return result;
    }

    // init itemtype, isSession
    private void parseGroupType(){
        List l_grouptype = _group.getGrouptype();
        if(l_grouptype != null && l_grouptype.size() > 0) {
            for (Iterator i = l_grouptype.iterator(); i.hasNext(); ){
                GroupType.GrouptypeType grouptype = (GroupType.GrouptypeType)i.next();
                String scheme = grouptype.getScheme();
                if (scheme!=null && scheme.equals(GROUP_TYPE_SCHEME_ITEM)){
                    List l_typevalue = grouptype.getTypevalue();
                    for (Iterator j = l_typevalue.iterator(); j.hasNext(); ){
                        TypevalueType typevalue = (TypevalueType)j.next();
                        String typevalueLevel = typevalue.getLevel();
                        String typevalueContent = typevalue.getValue();
                        try {
                            if(typevalueLevel.equals(GROUP_TYPE_LEVEL_ITEMTYPE)){
                                wbItemType = typevalueContent;
                            }
                            if((typevalueLevel.equals(GROUP_TYPE_LEVEL_SESSION) && typevalueContent.equalsIgnoreCase(GROUP_TYPE_CONTENT_SESSION))
                                || (typevalueLevel.equals(GROUP_TYPE_LEVEL_RUN_SESSION) && typevalueContent.equalsIgnoreCase(GROUP_TYPE_CONTENT_RUN_SESSION)))
                            {
                                isSession = true;
                            }
                            if(typevalueLevel.equals(GROUP_TYPE_LEVEL_RUN) 
                                && typevalueContent.equalsIgnoreCase(GROUP_TYPE_CONTENT_RUN)){
                                isRun = true;
                            }

                        } catch(NullPointerException e) {
                            continue;
                        }
                    }
                    break;
                }
            }            
        }
    }
    public void setWbItemId(long wbItemId) throws SQLException {
        this.wbItemId = wbItemId;
        return;
    }

    /**
    Get wizBank's aeItem.itm_id represented by this IMSItem from database
    @param con Connection to database
    @param wbSiteId wizBank organization's site id
    @return wizBank's aeItem.itm_id represented by this IMSItem, or 0 if no matching aeItem is found
    */
    public long getWbItemId(Connection con, long wbSiteId) throws SQLException {
        this.wbItemId = 0;
        aeItem itm = new aeItem();
        itm.itm_code = this.sourceDIDID;
        itm.itm_owner_ent_id = wbSiteId;
        this.wbItemId = itm.getItemId(con);
        return this.wbItemId;
    }

    /**
    Get wizBank's aeItem.itm_id represented by this IMSItem without consulted by database
    for performance concern.
    Thus make sure you have consulted database once before calling this method.
    @return wizBank's aeItem.itm_id represented by this IMSItem
    */
    public long getWbItemId() {
        return this.wbItemId;
    }

    /**
    Save this IMSItem to wizBank's aeItem.
    Firstly, check if the wizBank organization specified by wbSiteId argument has an aeItem
    represented by this IMSItem. 
    If yes, update that aeItem, 
    else create an new aeItem in wizBank to represent this IMSItem.
    Lastly, get the updated/inserted aeItem's itm_id and thus can retrieve it by getItmId()
    @param con Connection to database
    @param wbItemType wizBank's Item type of this IMSItem
    @param wbProfile wizBank's loginProfile, who are going to perform this save action
    @return Updated/Inserted aeItem's itm_id
    */
    public long save(Connection con, String defaultItemType, loginProfile wbProfile) 
        throws IOException, cwException, SQLException, cwSysMessage, qdbException {
        try{
            parseGroupType();            
            this.wbProfile = wbProfile;
            if (this.wbItemType == null){
                this.wbItemType = defaultItemType;
            }
            
            if (wbItemType == null){
                throw new cwException("ITEM_TYPE IS NOT DEFINED IN XML OR INI FILE.");                    
            }else if (!aeItem.isItemTypeExist(con, wbItemType)){
                throw new cwException("INVALID ITEM_TYPE:" + wbItemType);
            }
            //get this.sourceDIDID to make sure it has been initialized
            getSourceDIDID();
            //check if aeItem exists
            //if exists, update aeItem
            //else insert aeItem
            if(getWbItemId(con, this.wbProfile.root_ent_id) > 0) {
                updItem(con);
            } else {
                insItem(con);
            }
        }catch (cwSysMessage syse){
            CommonLog.error(syse.getMessage(),syse);
            throw new cwException(syse.getSystemMessage(wbProfile.label_lan));    
        }
        
        
        //get aeItem.itm_id

        return this.wbItemId;
    }        

    /**
    Insert this IMSItem to wizBank's aeItem
    @param con Connection to database
    */
    private void insItem(Connection con) 
        throws SQLException, IOException, cwException, cwSysMessage, qdbException {        
        //prepare Vectors and item XML
        //Vectors for insert aeItem
        Vector vColName = new Vector();
        Vector vColType = new Vector();
        Vector vColValue = new Vector();
        Vector vClobColName = new Vector();
        Vector vClobColValue = new Vector();
        //construct aeItem for insert
        aeItem itm = constructAeItem(con, this.wbProfile.root_ent_id, vColName, vColType, vColValue, vClobColName, vClobColValue);
        //Get Item/Appn/Workflow Templates
        Vector v_Template = getItemTypeTemplate(con);
        String[] tpl_type_list = (String[]) v_Template.elementAt(0);
        long[] tpl_id_list = (long[]) v_Template.elementAt(1);

        //Get qdb_ind of this item type
        DbItemType dbIty = new DbItemType();
        dbIty.ity_owner_ent_id = this.wbProfile.root_ent_id;
        dbIty.ity_id = this.wbItemType;
        dbIty.ity_run_ind = itm.itm_run_ind;
        dbIty.ity_session_ind = itm.itm_session_ind;
        dbIty.getItemType(con);
        
        if(dbIty.ity_qdb_ind) {
            dbCourse cos = new dbCourse();
            cos.res_lan = this.wbProfile.label_lan;
            cos.res_title = itm.itm_title;
            cos.res_usr_id_owner = this.wbProfile.usr_id;
            cos.res_upd_user = this.wbProfile.usr_id;
//            cos.res_title_xml = itm.itm_title_xml;
            itm.insWZBCourse(con, this.wbProfile, cos, 0,
                    tpl_type_list, tpl_id_list,
                    vColName, vColType, vColValue,
                    vClobColName, vClobColValue);
        } else {
            itm.ins(con, this.wbProfile.root_ent_id, this.wbProfile.usr_id, 0,
                    tpl_type_list, tpl_id_list, this.wbProfile,
                    vColName, vColType, vColValue,
                    vClobColName, vClobColValue);
        }
        return;
    }

    /**
    Update this IMSItem to wizBank's aeItem
    @param con Connection to database
    */
    private void updItem(Connection con) 
        throws IOException, cwException, SQLException, cwSysMessage, qdbException {
        //Vectors for update aeItem
        Vector vColName = new Vector();
        Vector vColType = new Vector();
        Vector vColValue = new Vector();
        Vector vClobColName = new Vector();
        Vector vClobColValue = new Vector();
        //construct aeItem for update
        aeItem itm = constructAeItem(con, this.wbProfile.root_ent_id, vColName, vColType, vColValue, vClobColName, vClobColValue);

        //Update aeItem based on its qdb_ind
        if(itm.getQdbInd(con)) {
            dbCourse cos = new dbCourse();
            cos.cos_res_id = cos.getCosResId(con, itm.itm_id);
            cos.res_id = cos.cos_res_id;
            cos.res_type = dbResource.RES_TYPE_COS;
            cos.get(con);
            //only res_title, res_title_xml will be updated in this application
            cos.res_title = itm.itm_title;
//            cos.res_title_xml = itm.itm_title_xml;
            
            itm.updWZBCourseWithoutTimestampChecking(con, 
                            this.wbProfile, 
                            cos,
                            vColName, vColType, vColValue,
                            vClobColName, vClobColValue);
        } else {
            itm.updWithoutTimestampChecking(con, 
                            this.wbProfile.root_ent_id, 
                            this.wbProfile.usr_id, 
                            vColName, vColType, vColValue,
                            vClobColName, vClobColValue);
        }
        return;
    }
    
    /**
    Construct an aeItem object of the IMSItem for insert/update.
    @param con Connection to database
    @param vColName Empty Vector, stores aeItem column name to be updated when return
    @param vColType Empty Vector, stores aeItem column type to be updated when return
    @param vColValue Empty Vector, stores aeItem column value to be updated when return
    @param vClobColName Empty Vector, stores aeItem Clob column name to be updated when return
    @param vClobColType Empty Vector, stores aeItem Clob column value to be updated when return
    @return Object of aeItem with param to be updated
    */
    protected aeItem constructAeItem(Connection con, long own_ent_id,
                              Vector vColName, Vector vColType, Vector vColValue,
                              Vector vClobColName, Vector vClobColValue) 
        throws SQLException, IOException, cwException, cwSysMessage {
        String sTemp;  // a temp String
        Hashtable data = new Hashtable(); // store the column name, value pair
        //construct aeItem and populates its values
        aeItem itm = new aeItem();
        itm.itm_id = this.wbItemId;
        itm.itm_owner_ent_id = own_ent_id;
        //set itm_code
        itm.itm_code = this.sourceDIDID;
//        if (isSession)  itm.itm_session_ind = true;
        data.put("itm_code", itm.itm_code);
        vColName.addElement("itm_code");
        vColType.addElement(DbTable.COL_TYPE_STRING);
        vColValue.addElement(itm.itm_code);
        //set itm_title
        sTemp = _group.getDescription().getShort();
        if(sTemp != null) {
            if(sTemp.length() > 0) {
                itm.itm_title = sTemp;
                data.put("itm_title", sTemp);
                vColName.addElement("itm_title");
                vColType.addElement(DbTable.COL_TYPE_STRING);
                vColValue.addElement(sTemp);
            } else {
                throw new cwException("Item title cannot be empty.");
            }
        }
        //set itm_eff_start/end_datetime
        if(_group.getTimeframe() != null) {
            if(_group.getTimeframe().getBegin() != null) {
                //set itm_eff_start_datetime
                sTemp = _group.getTimeframe().getBegin().getValue();
                if (sTemp != null){
                    if(sTemp.length() == 0) {
                        itm.itm_eff_start_datetime = null;
                    }else {
                        itm.itm_eff_start_datetime = Timestamp.valueOf(IMSUtils.convertTimestampFromISO8601(sTemp));
                    }
                    data.put("itm_eff_start_datetime", itm.itm_eff_start_datetime.toString());
                    vColName.addElement("itm_eff_start_datetime");
                    vColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
                    vColValue.addElement(itm.itm_eff_start_datetime);
                }
            }
            if(_group.getTimeframe().getEnd() != null) {
                //set itm_eff_end_datetime
                sTemp = _group.getTimeframe().getEnd().getValue();
                if (sTemp != null){
                    if (sTemp.length() == 0){
                        itm.itm_eff_end_datetime = null;
                    }else{
                        itm.itm_eff_end_datetime = Timestamp.valueOf(IMSUtils.convertTimestampFromISO8601(sTemp));
                    }    
                    data.put("itm_eff_end_datetime", itm.itm_eff_end_datetime.toString());
                    vColName.addElement("itm_eff_end_datetime");
                    vColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
                    vColValue.addElement(itm.itm_eff_end_datetime);
                }
            }
        }
        
        if (itm.itm_eff_start_datetime != null && itm.itm_eff_end_datetime !=null){
            if (itm.itm_eff_start_datetime.after(itm.itm_eff_end_datetime)){
                throw new cwException("Item Start Date must be before or equals to End Date.");
            }                            
        }
        //set itm_apply_ind
        if (_group.getEnrollcontrol()!=null ){
            sTemp = _group.getEnrollcontrol().getEnrollaccept();
            if(sTemp != null) {
                if(sTemp.equals("1")) {
                    itm.itm_apply_ind = true;
                    data.put("itm_apply_ind", "true");
                } else {
                    itm.itm_apply_ind = false;
                    data.put("itm_apply_ind", "false");
                }
                vColName.addElement("itm_apply_ind");
                vColType.addElement(DbTable.COL_TYPE_BOOLEAN);
                vColValue.addElement(new Boolean(itm.itm_apply_ind));
            }
        }
        if (isSession || isRun){
            itm.itm_session_ind = isSession;
            itm.itm_run_ind = isRun;
            List _Relationship = _group.getRelationship();
            if (_Relationship==null || _Relationship.size() !=1){
                throw new cwException("SESSION/CLASS SHOULD CONTAINS ONE AND ONLY ONE RELATIONSHIP, ERROR IN ITEM: " + this.sourceDIDID);    
            }
            RelationshipType pRelationship = (RelationshipType)_Relationship.iterator().next();
            // no label checking
            String _relation = pRelationship.getRelation();
            if (_relation != null && (_relation.equalsIgnoreCase(IMSItem.RELATION_TYPE_PARENT_CODE) || _relation.equalsIgnoreCase(IMSItem.RELATION_TYPE_PARENT))){
                SourcedidType pSourceDIDID = (SourcedidType)pRelationship.getSourcedid();
                aeItem pitm = new aeItem();
                pitm.itm_code = pSourceDIDID.getId();
                pitm.itm_owner_ent_id = own_ent_id;
                itm.parent_itm_id = pitm.getItemId(con);
                this.pWbItmId = itm.parent_itm_id;  
            }else{
                throw new cwException("RELATION: " + _relation + " IS NOT SUPPORTED IN SESSION, ERROR IN SESSION: " + this.sourceDIDID);
            }
        }

        // set extension field 
        String cwn_status = null;
        if (_group.getExtension() !=null){
            org.imsglobal.enterprise.ExtensionType extension = (org.imsglobal.enterprise.ExtensionType)_group.getExtension();    
            if (extension!=null){
                org.imsglobal.enterprise.cwn.ExtensionType cwnExtension = (org.imsglobal.enterprise.cwn.ExtensionType)extension.getExtension();
                if (cwnExtension!=null){
/*                    CwnItemTitle _cwnItemTitle = cwnExtension.getCwnItemTitle();
                    if (_cwnItemTitle!=null){
                        itm.itm_title_xml = IMSUtils.genTitleXML(_cwnItemTitle);
                        vClobColName.addElement("itm_title_xml");
                        vClobColValue.addElement(itm.itm_title_xml);
                    }
                    CwnItemUrl _CwnItemUrl = cwnExtension.getCwnItemUrl();
                    if (_CwnItemUrl!=null){
                        itm.itm_ext_ref_url = IMSUtils.genUrlXML(_CwnItemUrl);
                        vClobColName.addElement("itm_ext_ref_url");
                        vClobColValue.addElement(itm.itm_ext_ref_url);
                    }
                    */
                    cwn_status = cwnExtension.getStatus();
                    org.imsglobal.enterprise.cwn.ExtensionType.CreditListType cwnCreditList = cwnExtension.getCreditList();
                    if (cwnCreditList!=null){
                        List l_credit = cwnCreditList.getCredit();    
                        if (l_credit!=null){
                            Vector vtCredit = new Vector();
                            for (Iterator i = l_credit.iterator(); i.hasNext(); ) {
                                long ict_id; 
                                org.imsglobal.enterprise.cwn.ExtensionType.CreditListType.CreditType cwnCredit = (org.imsglobal.enterprise.cwn.ExtensionType.CreditListType.CreditType)i.next();
                                String key = cwnCredit.getType();
                                if (cwnCredit.getSubtype()!=null){
                                    key += cwnCredit.getSubtype();
                                }
                                if (!htCreditId.containsKey(key)){
                                    //ict_id = DbItemCredit.getIdByType(con, own_ent_id, cwnCredit.getType(), cwnCredit.getSubtype()); 
                                    ict_id = DbFigureType.getIdByType(con, own_ent_id, cwnCredit.getType(), cwnCredit.getSubtype()); 
                                    htCreditId.put(key, new Long(ict_id));
                                }else{
                                    ict_id = ((Long)htCreditId.get(key)).longValue();
                                }
                                //DbItemCreditValue dbicv = new DbItemCreditValue();
                                DbFigure dbFig = new DbFigure();
                                dbFig.fig_fgt_id = ict_id;//dbicv.icv_ict_id = ict_id;
                                float credit_value;
                                try{
                                    credit_value = Float.parseFloat(cwnCredit.getValue()); 
                                }catch(NumberFormatException e){
                                    throw new cwException("INVALID CREDIT VALUE IN ITEM" + this.sourceDIDID);
                                }
                                dbFig.fig_value = credit_value;//dbicv.icv_value = credit_value; 
                                vtCredit.addElement(dbFig);//vtCredit.addElement(dbicv);
                            }
                            itm.itm_credit_values = new DbFigure[vtCredit.size()];// new DbItemCreditValue[vtCredit.size()];
                            for (int i=0; i<vtCredit.size(); i++){
                                itm.itm_credit_values[i] = (DbFigure)vtCredit.elementAt(i);//(DbItemCreditValue)vtCredit.elementAt(i);
                            }
                        }
                    }
                    List extList = cwnExtension.getExt();
                    if (extList!=null){
                        for (Iterator i = extList.iterator(); i.hasNext(); ) {
                            org.imsglobal.enterprise.cwn.ExtensionType.ExtType cwnExt = (org.imsglobal.enterprise.cwn.ExtensionType.ExtType)i.next();
                            String extType = cwnExt.getExttype();
                            if (extType != null && extType.length() != 0 ){
                                data.put("field" + extType, cwnExt.getValue());
                            }else{
                                throw new cwException("NO EXTTYPE ATTRIBUTE IN cwn:ext.");    
                            }
                        }
                    }
                }
            }
        }        
        //set item type
        // check itemtype not changed
        if (itm.itm_id!=0){
            String old_itm_type = itm.getItemType(con);
            if (!old_itm_type.equals(this.wbItemType)){
                throw new cwException("ITEMTYPE SHOULD NOT CHANGE TO UPDATE ITEM.");                
            }
        }
        itm.itm_type = this.wbItemType;
        data.put("itm_type", itm.itm_type);
        vColName.addElement("itm_type");
        vColType.addElement(DbTable.COL_TYPE_STRING);
        vColValue.addElement(itm.itm_type);
        //set item status
        if (cwn_status!=null && cwn_status.length() > 0){
            itm.itm_status = cwn_status;
        }else{
            itm.itm_status = aeItem.ITM_STATUS_ON;
        }
        data.put("itm_status", itm.itm_status);
        vColName.addElement("itm_status");
        vColType.addElement(DbTable.COL_TYPE_STRING);
        vColValue.addElement(itm.itm_status);
        //set itm_syn_timestamp
        vColName.addElement("itm_syn_timestamp");
        vColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
        vColValue.addElement(cwSQL.getTime(con));
        //Generate itm_xml
        itm.generateItemXML(con, data);
        vClobColName.addElement("itm_xml");
        vClobColValue.addElement(itm.itm_xml);
        return itm;
    }
/*    
    private String genItmTitleXML(CwnItemTitle _cwnItemTitle){
        StringBuffer title_xml = new StringBuffer();
        title_xml.append("<title>");
        List _CwnTitle = _cwnItemTitle.getCwnTitle();
        CwnTitle cwnTitle; 
        String lang;
        String title;
        for (Iterator i = _CwnTitle.iterator(); i.hasNext(); ){
            cwnTitle = (CwnTitle)i.next();
            lang = cwnTitle.getLang();
            title_xml.append("<" + lang + ">").append(cwnTitle.getValue()).append("</" + lang + ">");
        }
        title_xml.append("</title>");
        return title_xml.toString();
    }*/
    
    /**
    Get Item/Appn/Workflow Templates of this wbItemType based on wbProfile.root_ent_id
    @param con Connection to database
    @return Vector of array. 1st element is String[] of Template Types, 
            while the 2nd element is long[] of Template Ids.
    */
    protected Vector getItemTypeTemplate(Connection con) throws SQLException, cwException {
        ViewItemTemplate viItmTpl = new ViewItemTemplate();
        viItmTpl.ownerEntId = this.wbProfile.root_ent_id;
        viItmTpl.itemType = this.wbItemType;
        viItmTpl.runInd = isRun;
        viItmTpl.sessionInd = isSession;
        String[] templateTypes = viItmTpl.getItemTypeTemplates(con);
        //find out the template id
        long[] tpl_ids;
        // get own template for non-run item (for session, no need to add parent template)  
        if(!isRun) {
            tpl_ids = getTplIds(con, this.wbItemType, 
                        templateTypes, isSession, this.wbProfile.root_ent_id);
        }
        else {
            tpl_ids = getRunTplIds(con, this.wbItemType,
                                    templateTypes, this.wbProfile.root_ent_id, this.pWbItmId);
        }        
        
        Vector v_array = new Vector();
        v_array.addElement(templateTypes);
        v_array.addElement(tpl_ids);
        return v_array;
    }
    
    private static long[] getTplIds(Connection con, String itemType, 
                        String[] templateTypes, boolean session_ind, long ownerEntId)
                        throws SQLException , cwException{

        long[] tpl_ids = new long[templateTypes.length];
        //for each template type, check if the tpl_id is in session.
        //if not found, get the 1st tpl and put it into session
        for(int i=0; i<templateTypes.length; i++) {
            tpl_ids[i] = getTplId(con, itemType,
                            templateTypes[i], false, session_ind, 
                            ownerEntId);

            if(tpl_ids[i] == 0) {
                throw new cwException("Cannot found template for " + templateTypes[i]);
            }
        }
        return tpl_ids;
    }
    
    private static long getTplId(Connection con, String itemType,
                        String templateType, boolean runInd, boolean sessionInd, long ownerEntId)
                        throws SQLException {
        ViewItemTemplate viItmTpl = new ViewItemTemplate();
        viItmTpl.ownerEntId = ownerEntId;
        viItmTpl.itemType = itemType;
        viItmTpl.templateType = templateType;
        viItmTpl.runInd = runInd;
        viItmTpl.sessionInd = sessionInd;
        return viItmTpl.getFirstTplId(con);
      }

    public static long[] getRunTplIds(Connection con, String itemType,
                        String[] templateTypes, long ownerEntId, long pItemId)
                        throws SQLException, cwException {

        long[] tpl_ids = new long[templateTypes.length];
        //get the template from session or database
        //if not found, get from it's parent
        for(int i=0; i<templateTypes.length; i++) {
            tpl_ids[i] = getTplId(con, itemType,
                            templateTypes[i], true, false, 
                            ownerEntId);
            if(tpl_ids[i] == 0 ) {
                aeItem pItm = new aeItem();
                pItm.itm_id = pItemId;
                tpl_ids[i] = pItm.getTemplateId(con, templateTypes[i]);
            }
            if(tpl_ids[i] == 0) {
                throw new cwException("Cannot found template for " + templateTypes[i]);
            }
        }
        return tpl_ids;
    }

    /*
    for export
    predefine itm_id
    */
    public GroupType get(Connection con) throws SQLException, cwSysMessage, cwException, JAXBException {
        aeItem aeItm = new aeItem();
        aeItm.itm_id = this.wbItemId; 
        aeItm.getItem(con);
        aeItem pItm = null;
        if (aeItm.itm_session_ind || aeItm.itm_run_ind ){
            aeItemRelation aeIre = new aeItemRelation();
            aeIre.ire_child_itm_id = aeItm.itm_id;
            aeItm.parent_itm_id = aeIre.getParentItemId(con);
            pItm = new aeItem();
            pItm.itm_id = aeItm.parent_itm_id;
            pItm.getItemCode(con);
            pItm.getRunInd(con);
        }                
        setValue(aeItm, pItm);
        return _group;
    }

    public void setValue(aeItem itm, aeItem pItm) throws cwException, JAXBException{
        org.imsglobal.enterprise.ObjectFactory objFactory = new org.imsglobal.enterprise.ObjectFactory();
        org.imsglobal.enterprise.cwn.ObjectFactory cwnobjFactory = new org.imsglobal.enterprise.cwn.ObjectFactory();
        
        SourcedidType sourcedid = IMSUtils.createSourcedid(itm.itm_code);
        List _Sourcedid = _group.getSourcedid();
        _Sourcedid.add(sourcedid);

        List _Grouptype = _group.getGrouptype();
        GroupType.GrouptypeType grouptype = objFactory.createGroupTypeGrouptypeType();
        grouptype.setScheme(GROUP_TYPE_SCHEME_ITEM);
        List l_Typevalue = grouptype.getTypevalue();
        TypevalueType itmTypeValue = objFactory.createTypevalueType();
        itmTypeValue.setLevel(GROUP_TYPE_LEVEL_ITEM);
        itmTypeValue.setValue(GROUP_TYPE_CONTENT_ITEM);
        TypevalueType itmTypeTypeValue = objFactory.createTypevalueType();
        itmTypeTypeValue.setLevel(GROUP_TYPE_LEVEL_ITEMTYPE);
        itmTypeTypeValue.setValue(itm.itm_type);
        l_Typevalue.add(itmTypeValue);
        l_Typevalue.add(itmTypeTypeValue);
        if (itm.itm_session_ind){
            TypevalueType sessionTypeValue = objFactory.createTypevalueType();
            sessionTypeValue.setLevel(GROUP_TYPE_LEVEL_SESSION);
            sessionTypeValue.setValue(GROUP_TYPE_CONTENT_SESSION);
            l_Typevalue.add(sessionTypeValue);
        }else if (itm.itm_run_ind){
            TypevalueType runTypeValue = objFactory.createTypevalueType();
            runTypeValue.setLevel(GROUP_TYPE_LEVEL_RUN);
            runTypeValue.setValue(GROUP_TYPE_CONTENT_RUN);
            l_Typevalue.add(runTypeValue);
        }
        _Grouptype.add(grouptype);
        
        DescriptionType _Description = objFactory.createDescriptionType();
        _Description.setShort(itm.itm_title);
        _group.setDescription(_Description);
        if (itm.itm_eff_start_datetime !=null || itm.itm_eff_end_datetime !=null){
            TimeframeType _Timeframe = objFactory.createTimeframeType();
            if (itm.itm_eff_start_datetime !=null){
                BeginType _Begin = objFactory.createBeginType();
                _Begin.setValue(IMSUtils.convertTimestampToISO8601(itm.itm_eff_start_datetime));
                _Timeframe.setBegin(_Begin);
            }
            if (itm.itm_eff_end_datetime !=null){
                EndType _End = objFactory.createEndType();
                _End.setValue(IMSUtils.convertTimestampToISO8601(itm.itm_eff_end_datetime));
                _Timeframe.setEnd(_End);
            }
            _group.setTimeframe(_Timeframe);
        }

        EnrollcontrolType enrollcontrol = objFactory.createEnrollcontrolType();
        String enrollaccept; 
        if (itm.itm_apply_ind){
            enrollaccept = "1";
        }else{
            enrollaccept = "0";
        }
        enrollcontrol.setEnrollaccept(enrollaccept);
        _group.setEnrollcontrol(enrollcontrol);
        
        if (itm.itm_session_ind || itm.itm_run_ind ){
            if (pItm.itm_code==null){
                throw new cwException("ERROR IN EXPORT SESSION: " + itm.itm_code + " , EMPTY PARENT CODE");    
            }
            SourcedidType pSourcedid = IMSUtils.createSourcedid(pItm.itm_code);
            RelationshipType relationship = objFactory.createRelationshipType();
            relationship.setRelation(RELATION_TYPE_PARENT_CODE); 
            relationship.setSourcedid(pSourcedid); 
            if (itm.itm_session_ind){
                if (pItm.itm_run_ind)
                    relationship.setLabel(RELATIONSHIP_LABEL_RUNSESSION); 
                else
                    relationship.setLabel(RELATIONSHIP_LABEL_COURSESESSION); 
            }else if (itm.itm_run_ind){
                relationship.setLabel(RELATIONSHIP_LABEL_COURSERUN); 
            }
            List _Relationship = _group.getRelationship();
            _Relationship.add(relationship);
        }
        org.imsglobal.enterprise.ExtensionType extension = objFactory.createExtensionType();
        org.imsglobal.enterprise.cwn.ExtensionType cwnExtension = cwnobjFactory.createExtensionType();
        cwnExtension.setStatus(itm.itm_status);
        
        if (htExtension!=null && !htExtension.isEmpty()){
            String content;
            String[] extension_fields = (String[])htExtension.get(itm.itm_type);
            if (extension_fields!=null && extension_fields.length>0 && itm.itm_xml !=null){
                List l_cwnExt = cwnExtension.getExt();
                Hashtable htXMLContent = itm.getXMLContent();
                for (int i=0; i<extension_fields.length; i++){
                    org.imsglobal.enterprise.cwn.ExtensionType.ExtType cwnext = cwnobjFactory.createExtensionTypeExtType();
                    content = (String)htXMLContent.get("field" + extension_fields[i]);
                    if (content==null)
                        content = "";
                    cwnext.setValue(cwUtils.esc4XML(content)); 
                    cwnext.setExttype(extension_fields[i]); 
                    l_cwnExt.add(cwnext);                    
                }
            }
        }
        extension.setExtension(cwnExtension); 
        _group.setExtension(extension);

    }

}
