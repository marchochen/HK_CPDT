package com.cw.wizbank.enterprise;

//import standard java classes
import java.lang.Long;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Iterator;
import java.util.Vector;
// import jaxb classes
import javax.xml.bind.*;

//import generated IMS classes

import org.imsglobal.enterprise.*;
/*
import org.imsglobal.enterprise.Group;
import org.imsglobal.enterprise.Grouptype;
//import org.imsglobal.enterprise.Groupmembers;
import org.imsglobal.enterprise.Relationship;
import org.imsglobal.enterprise.Typevalue;
import org.imsglobal.enterprise.Description;
import org.imsglobal.enterprise.Sourcedid;
*/
//import cyberwisdom classes
import com.cw.wizbank.util.*;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.db.DbItemType;
import com.cw.wizbank.ae.aeTreeNode;
import com.cw.wizbank.ae.aeCatalog;
import com.cw.wizbank.ae.aeTreeNodeRelation;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbException;
/**
*/
public class IMSTreeNode
{

    private static final String TYPE_TREENODE               =   "cwn catalog";
    private static final String TREENODE_TYPE_VALUE_LEVEL   =   "1";
    private static final String TREENODE_TYPE_VALUE_CONTENT =   "CAT";
    private static final String TREENODE_RELATIONSHIP_LABEL_CONTAIN_COURSE = "Course in Catalog";
    private static final String TREENODE_RELATIONSHIP_LABEL_CONTAIN_PROGRAM = "Program in Catalog";
//    private static final String TREENODE_RELATIONSHIP_LABEL_CONTAIN_PROGRAM = "Program in Catalog";

    private GroupType _group;
    private long wbTreeNodeId;
    private Vector vtItmLst;
    private Timestamp synDate;
    private loginProfile wbProfile;
    private List _Sourcedid;
    private List _Grouptype;
    private List _Relationship;
    private DescriptionType _Description;
    private String[] catalogItemType;
    private String sourceDIDID;
    /**
    Default Constructor.
    */
    
    public IMSTreeNode() throws JAXBException {
        ObjectFactory objFactory = new ObjectFactory();
        // create an empty group
        _group = objFactory.createGroup();
    }
    

    /**
    * Construct a new IMSTreeNode and set required field get from the Group argument.
    * @param group Group object with data
    * @param synDate
    * @param catalogAccess Item type can be attached to the catalog if catalog created by IMSTreeNode
    */
    public IMSTreeNode(GroupType group, Timestamp synDate, String[] catalogItemType){
        this._group = group;
        this._Sourcedid = group.getSourcedid();
        this.sourceDIDID = getSourceDIDID();
        this._Grouptype = group.getGrouptype();
        this._Description = (DescriptionType)group.getDescription();
//        this._Groupmembers = group.getGroupmembers();
        this._Relationship = group.getRelationship();
        this.catalogItemType = catalogItemType;
        this.synDate = synDate;
        return;
    }

    public IMSTreeNode(long wbTreeNodeId, Vector vtItmLst) throws JAXBException{
        ObjectFactory objFactory = new ObjectFactory();
        // create an empty group
        _group = objFactory.createGroup();

        this.wbTreeNodeId = wbTreeNodeId;
        this.vtItmLst = vtItmLst;
        return;
    }

    /**
    * Create tree node to connect item and catalog/category if the tree node existed<br>
    * Update tree node title and description<br>
    * If the parent node of the item not existed,
    * create a catalog as the parent of the item and connected them by tree node<br>
    * Delete the unnecessary tree node
    * @param blackListGroup All sourcedid in the vector will not be updated, deleted or inserted
    * @return boolean, if catalog found or created successfully then return true
    */
    public boolean save(Connection con, loginProfile wbProfile, Vector blackListGroup)
        throws SQLException, cwSysMessage, cwException, qdbException{
            this.wbProfile = wbProfile;
            this.wbTreeNodeId = getWbTreeNodeId(con);
            if( this.wbTreeNodeId == 0 ) {
                this.wbTreeNodeId = insWbCatalog(con);                
            } else {
                if( !updWbTreeNode(con) )
                    this.wbTreeNodeId = 0;
            }

            insWbItemTreeNode(con, blackListGroup);
            cleanUp(con, blackListGroup);
            
            if( this.wbTreeNodeId == 0 )
                return false;
            else
                return true;
        }


    /**
    * Get the tree node id by sourceDID
    */
    private long getWbTreeNodeId(Connection con)
        throws SQLException{
            long treeNodeId = aeTreeNode.getTreeNodeIdByCode(con, sourceDIDID, wbProfile.root_ent_id );
            if( treeNodeId > 0 )
                return treeNodeId;
            return 0;
        }

    /**
    * Return id of the first sourcedid
    */
    public String getSourceDIDID() {
        this.sourceDIDID = ((SourcedidType) this._Sourcedid.iterator().next()).getId();
        return this.sourceDIDID;
    }


    /**
    * Update tree node title and description
    */
    private boolean updWbTreeNode(Connection con)
        throws SQLException, cwSysMessage{
            aeTreeNode aeTnd = new aeTreeNode();
            aeTnd.tnd_id = this.wbTreeNodeId;
            aeTnd.get(con);
            if( _Description != null ) {
                if( _Description.getShort() != null )
                    aeTnd.tnd_title = _Description.getShort();
                if( _Description.getLong() != null )
                    aeTnd.tnd_desc = _Description.getLong();
            }
            aeTnd.tnd_upd_usr_id = this.wbProfile.usr_id;
            aeTnd.tnd_upd_timestamp = cwSQL.getTime(con);
            aeTnd.tnd_syn_timestamp = aeTnd.tnd_upd_timestamp;
            
            aeCatalog aeCat = new aeCatalog();
            aeCat.cat_owner_ent_id = this.wbProfile.root_ent_id;
            aeCat.cat_title = _Description.getShort();
//            if( !aeCat.validCatalogName(con, aeTnd.tnd_cat_id) ){
//                IMSUtils.writeLog(IMSUtils.FAILURE_FILE, this.sourceDIDID + " : CANNOT UPDATE, TITLE DUPLICATED");
//                return false;
//            }

            aeTnd.updTitleDesc(con, true);
            return true;
        }


    /*
    * Loop the list sourcedid get form EntityRelations and insert an item tree ndoe for each sourcedid
    * @param blackListGroup All sourcedid in the vector will not be inserted
    */
    private void insWbItemTreeNode(Connection con, Vector blackListGroup)
        throws SQLException, cwSysMessage, qdbException{
            
            if( _Relationship == null || _Relationship.size() == 0 )
                return;
            
            RelationshipType _relationship;
            SourcedidType _sourcedid;
            String _label;
            String _relation;
            for( Iterator j = _Relationship.iterator(); j.hasNext(); ) {
                _relationship = (RelationshipType)j.next();
                _sourcedid = (SourcedidType)_relationship.getSourcedid();
                _relation = _relationship.getRelation();
                if (_relation != null && (_relation.equalsIgnoreCase(IMSItem.RELATION_TYPE_CHILD_CODE) || _relation.equalsIgnoreCase(IMSItem.RELATION_TYPE_CHILD))){
                    _label = _relationship.getLabel(); 
                    if (_label.equalsIgnoreCase(TREENODE_RELATIONSHIP_LABEL_CONTAIN_COURSE)){
                        if( blackListGroup.indexOf(_sourcedid.getId()) == -1 ){
                            insWbItemTreeNode( con, _sourcedid.getId() );
                        } else {
                            IMSUtils.writeLog(IMSUtils.FAILURE_FILE, _sourcedid.getId() + " , " + this.sourceDIDID + " RELATIONSHIP CANNOT MAKE");
                        }
                    }else{
                        IMSUtils.writeLog(IMSUtils.FAILURE_FILE, _sourcedid.getId() + " , " + this.sourceDIDID + " RELATIONSHIP CANNOT MAKE, RELATIONSHIP: " + _label + " NOT SUPPORTED IN CATALOG");
                    }                
                }else{
                    IMSUtils.writeLog(IMSUtils.FAILURE_FILE, _sourcedid.getId() + " , " + this.sourceDIDID + " RELATIONSHIP CANNOT MAKE, RELATION " + _relation + " NOT SUPPORTED IN CATALOG");
//                    throw new cwException("RELATION: " + _relation + " NOT SUPPORTED IN CATALOG.");
                }
            }
            return;
        }



    /**
    * Insert a item tree node and cascading update item count of the parent<br>
    * If tree node already existed, only update the syn date of the tree node
    */
    private void insWbItemTreeNode(Connection con, String courseId)
        throws SQLException, cwSysMessage, qdbException{
            if( this.wbTreeNodeId == 0 ){
                IMSUtils.writeLog(IMSUtils.FAILURE_FILE, courseId + " : " + this.sourceDIDID + " NOT FOUND IN SYSTEM");
                return;
            }
            
            aeTreeNode aeTnd = new aeTreeNode();
            aeTnd.tnd_parent_tnd_id = this.wbTreeNodeId;

            aeItem itm = new aeItem();
            itm.itm_code = courseId;
            itm.itm_owner_ent_id = wbProfile.root_ent_id;
            aeTnd.tnd_itm_id = itm.getItemId(con);
            
            if( aeTnd.tnd_itm_id != 0 ) {

                aeTnd.tnd_id = aeTnd.getTreeNode(con);
                //if trre node not found, create it
                if( aeTnd.tnd_id == 0 ) {
                    aeTreeNode parentTnd = new aeTreeNode();
                    parentTnd.tnd_id = this.wbTreeNodeId;
                    aeTnd.tnd_cat_id = parentTnd.getCatalogId(con);
                    aeTnd.tnd_create_timestamp = cwSQL.getTime(con);
                    aeTnd.tnd_create_usr_id = wbProfile.usr_id;
                    aeTnd.tnd_upd_timestamp = aeTnd.tnd_create_timestamp;
                    aeTnd.tnd_upd_usr_id = wbProfile.usr_id;
                    aeTnd.tnd_code = courseId;
                    aeTnd.tnd_id = aeTnd.insItmNode(con, wbProfile.root_ent_id);
                }//if tree node exist but tree node code is null
                 //update it same as item code
                else if( aeTnd.tnd_code == null ) {
                    aeTnd.tnd_code = courseId;
                    aeTnd.tnd_upd_usr_id = wbProfile.usr_id;
                    aeTnd.updTndCode(con);
                }
                aeTnd.tnd_syn_timestamp = cwSQL.getTime(con);
                aeTnd.updSynDate(con);
                IMSUtils.writeLog(IMSUtils.SUCCESS_FILE, courseId + " , " + this.sourceDIDID + " RELATIONSHIP MAKE");
                
            } else {
                IMSUtils.writeLog(IMSUtils.FAILURE_FILE, courseId + " NOT FOUND IN SYSTEM");
                return;
            }
            return;
        }


    /**
    * Insert a catalog and catalog tree node and assign accessible item type for the catalog<br>
    * If title of the insert catalog is exist, cataog will not be inserted
    */
    private long insWbCatalog(Connection con)
        throws SQLException, cwSysMessage, qdbException, cwException{

            aeCatalog aeCat = new aeCatalog();
            aeCat.cat_owner_ent_id = wbProfile.root_ent_id;
            long[] ent_id = { wbProfile.root_ent_id };
            //String[] ity_id = { "BOOK", "CLASSROOM", "SELFSTUDY", "SELFSTUDY_B", "VIDEO" };
            aeCat.cat_title = _Description.getShort();
//            if( !aeCat.validCatalogName(con, 0) ){
//                IMSUtils.writeLog(IMSUtils.FAILURE_FILE, this.sourceDIDID + " : CANNOT MAKE, TITLE DUPLICATED");
//                return 0;
//            }
            aeCat.cat_desc = _Description.getLong();
            aeCat.cat_public_ind = false;
            aeCat.cat_status = aeCatalog.CAT_STATUS_ON;            
            aeCat.cat_code = this.sourceDIDID;
            aeCat.ins(con, wbProfile.root_ent_id, wbProfile.usr_id, ent_id, catalogItemType);
            aeTreeNode aeTnd = aeCat.getMyTreeNode(con);
            aeTnd.updSynDate(con);
            return aeTnd.tnd_id;
        }



    /**
    * Delete not-in-syn item tree node
    */
    private void cleanUp(Connection con, Vector blackListGroup)
        throws SQLException, cwSysMessage, qdbException{
            delWbTreeNode(con, blackListGroup);
            return;
        }

    
    /**
    * Delete not-in-syn item tree node
    * @param blackListGroup All sourcedid in the vector will not be clean
    */
    private void delWbTreeNode(Connection con, Vector blackListGroup)
        throws SQLException, cwSysMessage, qdbException {
            
            if( this.wbTreeNodeId == 0 )
                return;
                
            Vector tndVec = aeTreeNode.getNotInSynChild(con, this.wbTreeNodeId, this.synDate);
            Vector itmTndVec = new Vector();
            aeTreeNode _tnd = null;
            aeItem aeItm = new aeItem();
            for(int i=0; i<tndVec.size(); i++) {
                _tnd = (aeTreeNode)tndVec.elementAt(i);
                if( _tnd.tnd_type.equalsIgnoreCase(aeTreeNode.TND_TYPE_ITEM) ){
                    aeItm.itm_id = _tnd.tnd_itm_id;
                    if( blackListGroup.indexOf( aeItm.getItemCode(con) ) == -1 )
                        itmTndVec.addElement(_tnd);
                }
            }
            
            if( !itmTndVec.isEmpty() )
                delWbItemTreeNode(con, itmTndVec);
            return;
        }

    
    /**
    * Delete not-in-syn item tree node 
    */
    private void delWbItemTreeNode(Connection con, Vector tndVec)
        throws SQLException, cwSysMessage, qdbException{
            
            if( tndVec == null || tndVec.size() == 0 )
                return;

            aeTreeNode aeTnd = null;
            aeItem itm = null;
            int removedTreeNodeCount = 0;
            int removedOnStatusTreeNodeCount = 0;
            String status = null;
            for(int i=0; i<tndVec.size(); i++){
                aeTnd = (aeTreeNode)tndVec.elementAt(i);

                itm = new aeItem();
                itm.itm_id = aeTnd.tnd_itm_id;
                status = itm.getItemStatus(con);                
                if(status.equals(aeItem.ITM_STATUS_ON))
                    removedOnStatusTreeNodeCount++;

                aeTnd.tnd_id = aeTnd.tnd_id;
                aeTreeNodeRelation.delTnrByTnd(con,  aeTnd.tnd_id );
                aeTnd.del(con);
                IMSUtils.writeLog(IMSUtils.CLEAN_FILE, itm.getItemCode(con) + " , " + this.sourceDIDID + " RELATIONSHIP CLEANED.");
                removedTreeNodeCount++;
            }
            aeTreeNode parentNode = new aeTreeNode();
            parentNode.tnd_id = this.wbTreeNodeId;
            parentNode.tnd_parent_tnd_id = parentNode.getParentId(con);
            parentNode.cascadeUpdItmCntBy(con, (0-removedTreeNodeCount));
            if( removedOnStatusTreeNodeCount != 0 )
                parentNode.cascadeUpdOnItmCntBy(con, (0-removedOnStatusTreeNodeCount));
            return;
        }


    /**
    * Validate the group is a valid IMSTreeNode
    */
    public static boolean IMSvalidate(GroupType group){
        List groupType = group.getGrouptype();
        for( Iterator i = groupType.iterator(); i.hasNext(); ) {
            GroupType.GrouptypeType _grouptype = (GroupType.GrouptypeType)i.next();
            if( (_grouptype.getScheme()).equalsIgnoreCase(TYPE_TREENODE) ) { 
                List typeValue = _grouptype.getTypevalue();
                for( Iterator j = typeValue.iterator(); j.hasNext(); ){
                    TypevalueType _typevalue = (TypevalueType)j.next();
                    if( (_typevalue.getLevel()).equalsIgnoreCase(TREENODE_TYPE_VALUE_LEVEL)
                    &&  (_typevalue.getValue()).equalsIgnoreCase(TREENODE_TYPE_VALUE_CONTENT) ) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
    * Get all existing item types in the system
    */
    public static String[] getAllWbItemType(Connection con, long site_id)
        throws cwException {
            try{
            DbItemType[] ity = DbItemType.getAllItemTypeInOrg(con, site_id);
            String[] itemType = new String[ity.length];
            for(int i=0; i<itemType.length; i++)
                itemType[i] = ity[i].ity_id;
            
            return itemType;
            }catch(SQLException e){
                throw new cwException(e.getMessage());
            }
        }

    // for export
    
    public GroupType get(Connection con) throws cwSysMessage, SQLException, cwException, JAXBException{
        aeTreeNode node = new aeTreeNode();
        node.tnd_id = this.wbTreeNodeId;
        node.get(con);
        if (node.tnd_code==null){
            throw new cwException("ERROR: TREE NODE CODE EMPTY FOR TND_ID: " + this.wbTreeNodeId);    
        }
        setValue(con, node, vtItmLst);
        return _group;
    }
    
    public void setValue(Connection con, aeTreeNode node, Vector vtItmLst) throws SQLException, JAXBException{
        ObjectFactory objFactory = new ObjectFactory();

        SourcedidType sourcedid = IMSUtils.createSourcedid(node.tnd_code);
        _Sourcedid = _group.getSourcedid();
        _Sourcedid.add(sourcedid);

        _Grouptype = _group.getGrouptype();
        GroupType.GrouptypeType grouptype = objFactory.createGroupTypeGrouptypeType();
        grouptype.setScheme(TYPE_TREENODE);
        List l_Typevalue = grouptype.getTypevalue();
        TypevalueType typeValue = objFactory.createTypevalueType();
        typeValue.setLevel(TREENODE_TYPE_VALUE_LEVEL);
        typeValue.setValue(TREENODE_TYPE_VALUE_CONTENT);
        l_Typevalue.add(typeValue);
        _Grouptype.add(grouptype);
        
        DescriptionType _Description = objFactory.createDescriptionType();
        _Description.setShort(node.tnd_title);
        _Description.setLong(node.tnd_desc);

        _group.setDescription(_Description);
        
        _Relationship = _group.getRelationship();                
        if (vtItmLst!=null && vtItmLst.size() > 0){
            for (int i=0; i<vtItmLst.size(); i++){
                RelationshipType _relationship = objFactory.createRelationshipType();
                SourcedidType itmSourcedid;
                aeItem itm = new aeItem();
                itm.itm_id = ((Long)vtItmLst.elementAt(i)).longValue();
                itm.itm_code = itm.getItemCode(con);
                itmSourcedid = IMSUtils.createSourcedid(itm.itm_code);
                _relationship.setSourcedid(itmSourcedid);
                _relationship.setLabel(TREENODE_RELATIONSHIP_LABEL_CONTAIN_COURSE);
                _relationship.setRelation(IMSItem.RELATION_TYPE_CHILD_CODE);
                
                _Relationship.add(_relationship);
            }
        }
        
    }

//public Vector getUpdatedTreeNode(Connection con, Timestamp startDate, Timestamp endDate, Vector itmLst){




}