package com.cw.wizbank.enterprise;

//import standard java classes
import java.lang.Long;
import java.sql.*;
import java.util.List;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Enumeration;
import javax.xml.bind.*;
//import generated IMS classes
import org.imsglobal.enterprise.*;

//import cyberwisdom classes
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeItemAccess;
import com.cw.wizbank.accesscontrol.AccessControlWZB;

/**
IMSItemAccess represents a special kind of Membership and this
kind of Membership is represented as aeItemAccess in wizBank.
<BR>
IMSItemAccess has 2 constructors.
The default constructor IMSItemAccess() is preserved for the usage of importing XML
data into IMSItemAccess directly. And thus you can use getMember() to retrieve child node info.
The 2nd constructor, IMSItemAccess(Membership, Member, Role) does not import XML itself.
it takes in Membership, Member and Role objects as it's properties. Thus you cannot use
getMember() to retrieve child node info. However you can use getIacMember() and getIacRole()
to retrieve data passed into constructor.
*/
public class IMSItemAccess
{
//    public static final String ROLE_TYPE_INSTRUCTOR_CODE = "02";
//    public static final String ROLE_TYPE_INSTRUCTOR = "Instructor";

//    public static final String ROLE_TYPE_ADMINISTRATOR_CODE = "07";
//    public static final String ROLE_TYPE_ADMINISTRATOR = "Administrator";
    
//    private static final String IAC_ROLE_MAP_KEY_DELIMITER = ":";
//    private static final String IAC_ROLE_MAP_PAIR_DELIMITER = ",";
    
    private MembershipType _membership;
    private static Hashtable h_roleMap;
    private Hashtable htRoleUid = null;
    private loginProfile wbProfile;
    private long wbEntityId;
    private long wbItemId;
    private String wbRoleId;
    private MemberType iacMember;
    private RoleType iacRole;
    
    private String sourceDIDID;
    private String memberSourceDIDID;
    
    /**
    Default Constructor.
    */
    public IMSItemAccess() throws JAXBException{
        ObjectFactory objFactory = new ObjectFactory();
        // create an empty group
        _membership = objFactory.createMembership();
    }
    
    /**
    Construct a new IMSItemAccess that contains Comments and Sourcedid currently contained in the membership argument.
    And stores iacMemeber and iacRole as its properties.
    @param membership a Membership object with data
    @param iacMember a Member object with data
    @param iacRole a Role object with data
    */
    public IMSItemAccess(MembershipType membership, MemberType iacMember, RoleType iacRole){
        this._membership = membership;
//        setComments(membership.getComments());
//        setSourcedid(membership.getSourcedid());
        this.iacMember = iacMember;
        this.iacRole = iacRole;
    }

    /**
    Get iacMember object 
    @return iacMember
    */
    public MemberType getIacMember() {
        return this.iacMember;
    }
    
    /**
    Get iacRole object
    @return iacRole
    */
    public RoleType getIacRole() {
        return this.iacRole;
    }

    /** 
    get memberSourceDIDID
    set memberSourceDIDID if memberSourceDIDID not yet set
    @return memberSourceDIDID
    */    
    public String getMemberSourceDIDID(){
        if (this.memberSourceDIDID==null){
            this.memberSourceDIDID = iacMember.getSourcedid().getId();
        }
        return this.memberSourceDIDID; 
    }
    /** 
    get sourceDIDID
    set sourceDIDID if sourceDIDID not yet set
    @return sourceDIDID
    */    
    public String getSourceDIDID() {
        if (this.sourceDIDID==null){
            this.sourceDIDID = _membership.getSourcedid().getId();
        }
        return this.sourceDIDID;
    }
    
    /**
    Get wizBank's aeItem.itm_id represented by this IMSItemAccess from database
    @param con Connection to database
    @param wbSiteId wizBank organization's site id
    @return wizBank's aeItem.itm_id represented by this IMSItemAccess, or 0 if no matching aeItem is found
    */
    public long getWbItemId(Connection con, long wbSiteId) throws SQLException {
        this.wbItemId = 0;
        aeItem itm = new aeItem();
        itm.itm_code = _membership.getSourcedid().getId();
        itm.itm_owner_ent_id = wbSiteId;
        this.wbItemId = itm.getItemId(con);
        return this.wbItemId;
    } 
    
    public void setWbItemId(long wbItemId){
        this.wbItemId = wbItemId;
    }
    /**
    Get wizBank's RegUser.usr_ent_id represented by this IMSItemAccess.iacMember from database
    @param con Connection to database
    @param wbSiteId wizBank organization's site id
    @return wizBank's RegUser.usr_ent_id represented by this IMSItemAccess.iacMember, or 0 if no matching RegUser is found
    */
    public long getWbUserEntId(Connection con, long wbSiteId) throws SQLException, qdbException {
        this.wbEntityId = 0;
        dbRegUser dbusr = new dbRegUser();
        this.wbEntityId = dbusr.getEntId(con, this.iacMember.getSourcedid().getId(), wbSiteId);        
        return this.wbEntityId ;
    } 
    
    /**
    Reset roleMap and get wizBank's role id from the input roleMap argument.
    @param roleType roletype from XML
    @param roleMap a String maps roletype to aeItem.iac_access_id e.g. "02:ADM_1,07:IST_1"
    @return mapping role id
    */
    /*
    public String getWbRoleId(String roleType, String roleMap) throws cwException {
        //reset roleMap
        this.h_roleMap = null;
        setupRoleMap(roleMap);
        return getWbRoleId(roleType);
    }
    */
    
    /**
    Get wizBank's role id from h_roleMap
    @return mapping role id
    */
    private String getWbRoleId(String roleType) throws cwException{
        /*
        if(this.h_roleMap == null) {
            return null;
        } else {
            return (String)this.h_roleMap.get(roleType);
        }
        */
        if(this.h_roleMap == null) {
            this.wbRoleId = null;                        
//            throw new cwException("IMSItemAccess.getWbRoleId: invalid roleMap");
        } else {
            this.wbRoleId = (String)this.h_roleMap.get(roleType);
        }
        return this.wbRoleId;
    }
    
    public static Hashtable initRoleType(Connection con, long siteId) throws SQLException, cwException{
        AccessControlWZB acl = new AccessControlWZB();
        Hashtable htRole = acl.getAllRoleUid(con, siteId, "rol_ste_uid");
        Hashtable htRoleType = new Hashtable();

        Enumeration enumeration = htRole.keys();
        while(enumeration.hasMoreElements()) {
            String cur_role = (String)enumeration.nextElement();
            if (cur_role.equals("LRNR")){
                htRoleType.put(cur_role, "01");
            }else if (cur_role.equals("TADM")){
                htRoleType.put(cur_role, "07");
            }else if (cur_role.equals("SADM")){
                htRoleType.put(cur_role, "07");
            }else if (cur_role.equals("GADM")){
                htRoleType.put(cur_role, "07");
            }else{
                htRoleType.put(cur_role, "07");
            }
        }
        return htRoleType;
    }
    
    /**
    Save this ItemAccess Membership to wizBank's aeItemAccess.
    This method will use 2 algorithms to perform the save action.
    If this membership comes from directly import from XML, it will save all underlying membership to aeItemAccess.
    Else if this membership comes from iacMember and iacRole, save that relation to aeItemAccess.
    @param con Connection to database.
    @param wbProfile wizBank's loginProfile who perform this save action.
    @param roleMap a String maps roletype to aeItem.iac_access_id e.g. "02:ADM_1,07:IST_1"
    */
    public void save(Connection con, loginProfile wbProfile)
        throws cwException, qdbException, SQLException {
        //setup class property
        this.wbProfile = wbProfile;
        setupRoleMap(con, wbProfile.root_ent_id, "rol_ste_uid");
//        getRoleType(); 
        
        //check if this Membership if from XML or iacMember, iacRole
        if(this.iacMember !=null && this.iacRole != null) {
            save(con);
        } else if(_membership.getMember() != null) {
            List l_member = _membership.getMember();
            for (Iterator j = l_member.iterator(); j.hasNext(); ){
                MemberType member = (MemberType)j.next();
                List l_role = member.getRole();
                for (Iterator k = l_role.iterator(); k.hasNext();){
                    RoleType role = (RoleType)k.next();
                    IMSItemAccess imsItmAccess = new IMSItemAccess(_membership, member, role);
                    imsItmAccess.save(con, this.wbProfile);
                }
            }
        }
        return;
    }

    /**
    Save this ItemAccess Membership to wizBank's aeItemAccess
    @param con Connection to database
    */
    private void save(Connection con) throws cwException, SQLException, qdbException {
        //check if the this membership is active
        if(this.iacRole.getStatus() != null && this.iacRole.getStatus().equals("1")) {
            //get item id
            if(getWbItemId(con, this.wbProfile.root_ent_id) == 0) {
                throw new cwException ("IMSItemAccess.update: item not found");
            }
            //get user entity id
            if(getWbUserEntId(con, this.wbProfile.root_ent_id) == 0) {
                throw new cwException("IMSItemAccess.update: user not found");
            }
            //get role id
            String imsRole = iacRole.getSubrole();
            if (imsRole==null)  
                imsRole = iacRole.getRoletype();
            if(getWbRoleId(imsRole) == null) {
                throw new cwException("IMSItemAccess.update: role id not found");
            }
            Timestamp startDate = null;
            Timestamp endDate = null;
            String sTemp;
            if (iacRole.getTimeframe()!=null){
                if(iacRole.getTimeframe().getBegin() != null) {
                    //set startDate
                    sTemp = iacRole.getTimeframe().getBegin().getValue();
                    if (sTemp != null && sTemp.length() > 0){
                        startDate = Timestamp.valueOf(IMSUtils.convertTimestampFromISO8601(sTemp));
                    }
                }
                if (iacRole.getTimeframe().getEnd() != null){
                    sTemp = iacRole.getTimeframe().getEnd().getValue();
                    if (sTemp != null && sTemp.length() > 0){
                        endDate = Timestamp.valueOf(IMSUtils.convertTimestampFromISO8601(sTemp));
                    }
                }
            }
            // init start end date
            if (startDate==null){
                startDate = Timestamp.valueOf(cwUtils.MIN_TIMESTAMP);
            }
            if (endDate==null){
                endDate = Timestamp.valueOf(cwUtils.MAX_TIMESTAMP);
            }
            
            AccessControlWZB acl = new AccessControlWZB();
            if(!acl.hasUserRole(con, this.wbEntityId, this.wbRoleId, startDate, endDate)) {
                acl.assignUser2Role(con, this.wbEntityId, this.wbRoleId, startDate, endDate);
            }
            acl.updEntityRoleSynTimestamp(con, this.wbEntityId, this.wbRoleId, startDate, endDate);


            //insert into aeItemAccess if it does not exist
            aeItemAccess iac = new aeItemAccess();
            iac.iac_ent_id = this.wbEntityId;
            iac.iac_itm_id = this.wbItemId;
            iac.iac_access_id = this.wbRoleId;
            iac.iac_access_type = aeItemAccess.ACCESS_TYPE_ROLE;
            if(!iac.isExist(con)) {
                long wbCosId = 0;
                try {
                    wbCosId = aeItemAccess.getCosIdByItemId(con, this.wbItemId);
                    iac.ins(con, wbCosId);
                } catch(cwException e) {
                    //continue to insert aeItemAccess if Item does not has Course attachs to it
                    iac.ins(con);
                }
            }
            //update aeItemAccess.iac_syn_timestamp
            iac.updSynTimestamp(con);
        }
        return;
    }

    /**
    Parse the input roleMap
    @param roleMap a String maps roletype to aeItem.iac_access_id e.g. "02:ADM_1,07:IST_1"
    @return Hashtable contains (roletype, wizbank rol_id)
    */
    /*
    public static Hashtable parseRoleMap(String roleMap) throws cwException {
        Hashtable h = new Hashtable();
        Vector row = cwUtils.splitToVecString(roleMap, IAC_ROLE_MAP_PAIR_DELIMITER);
        for(int i=0; i<row.size();i++){
            String[] tmpArr = cwUtils.splitToString((String)row.elementAt(i), IAC_ROLE_MAP_KEY_DELIMITER);
            if (tmpArr.length==2){
                h.put(tmpArr[0], tmpArr[1]);
            }else{
                throw new cwException("IMSItemAccess.parseRoleMap: invalid roleMap");
            }
        }
        return h;
    }*/

    /**
    Setup the static class property h_roleMap
    @param roleMap a String maps roletype to aeItem.iac_access_id e.g. "02:ADM_1,07:IST_1"
    */
    private void setupRoleMap(Connection con, long siteId, String key) throws cwException, SQLException {
        if(this.h_roleMap == null) {
            AccessControlWZB acl = new AccessControlWZB();
            this.h_roleMap = acl.getAllRoleUid(con, siteId, key);
            if(this.h_roleMap.size() == 0) {
                this.h_roleMap = null;
            }
            /*
            if(roleMap == null || roleMap.length() == 0) {
                throw new cwException("IMSItemAccess.setupRoleMap: invalid roleMap");
            } 
            this.h_roleMap = parseRoleMap(roleMap);
            if(this.h_roleMap.size() == 0) {
                this.h_roleMap = null;
            }
            */
        }
        if (this.htRoleUid == null){
            htRoleUid = initRoleType(con, siteId);
        }
        return;
    }

    /**
    Get the wizBank's role id of the membership referencing to its h_roleMap
    */
    /*
    private String getRoleType() throws cwException {
        if(this.h_roleMap == null) {
            throw new cwException("IMSItemAccess.getRoleType: invalid roleMap");
        } else {
            this.wbRoleId = (String)this.h_roleMap.get(this.iacRole.getRoletype());
        }
        return this.wbRoleId;
    }
    */
    
    // for export
    public MembershipType get(Connection con, long siteId) throws SQLException, cwException, JAXBException{
        ObjectFactory objFactory = new ObjectFactory();
        
        setupRoleMap(con, siteId, "rol_ext_id");
        aeItem itm = new aeItem();
        itm.itm_id = wbItemId;        
        sourceDIDID = itm.getItemCode(con);
        SourcedidType _Sourcedid = IMSUtils.createSourcedid(sourceDIDID);
        _membership.setSourcedid(_Sourcedid);
        
        aeItemAccess.ViewItemAccess[] itmAcc = aeItemAccess.getAssignedRoleList(con, this.wbItemId);      
        Hashtable htMember = new Hashtable();
        for (int i = 0; i < itmAcc.length; i++) {
            Long entId = new Long(itmAcc[i].iac_ent_id);
            MemberType member = (MemberType) htMember.get(entId);
            if (member==null){
                member = objFactory.createMember();
                SourcedidType sourcedid = IMSUtils.createSourcedid(itmAcc[i].usr_ste_usr_id);
                member.setSourcedid(sourcedid);
                //1 indicate the member is a person
                member.setIdtype("1");
            }
            RoleType role = objFactory.createRoleType();
            //01 indicate learner or student
            String subrole = (String)h_roleMap.get(itmAcc[i].iac_access_id);
            role.setRoletype((String)htRoleUid.get(subrole)); 
            role.setSubrole(subrole);
            //1 indicate active user
            role.setStatus("1");
        
            List role_list = member.getRole();
            role_list.add(role);
            htMember.put(entId, member);
        } 
        List _member = _membership.getMember();         
        Enumeration e_member = htMember.elements();
        while(e_member.hasMoreElements()) {
            MemberType member = (MemberType) e_member.nextElement();                        
            _member.add(member);
        }

        if (_member.size()<=0){
            return null;    
        }else{
            return _membership;
        }
    }
    
}
