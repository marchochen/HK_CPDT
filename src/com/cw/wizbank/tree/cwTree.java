package com.cw.wizbank.tree;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.cw.wizbank.JsonMod.commonBean.JsonTreeBean;
import com.cw.wizbank.JsonMod.supervise.Supervise;
import com.cw.wizbank.accesscontrol.AcTrainingCenter;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.ae.aeCatalog;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeTreeNode;
import com.cw.wizbank.ae.db.view.ViewCatalogToTree;
import com.cw.wizbank.db.DbCmSkillBase;
import com.cw.wizbank.db.DbKmFolder;
import com.cw.wizbank.db.DbKmNodeAccess;
import com.cw.wizbank.db.DbKmObject;
import com.cw.wizbank.db.DbTrainingCenter;
import com.cw.wizbank.db.view.ViewCmToTree;
import com.cw.wizbank.db.view.ViewEntityToTree;
import com.cw.wizbank.db.view.ViewKmFolderManager;
import com.cw.wizbank.db.view.ViewKnowledgeObjectToTree;
import com.cw.wizbank.db.view.ViewRoleTargetGroup;
import com.cw.wizbank.db.view.ViewTrainingCenter;
import com.cw.wizbank.qdb.dbEntity;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbResource;
import com.cw.wizbank.qdb.dbResourceObjective;
import com.cw.wizbank.qdb.dbUserGroup;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbAction;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.supervise.Supervisor;
import com.cw.wizbank.tree.cwTree.nodeInfo;
import com.cw.wizbank.util.LangLabel;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.utils.CommonLog;

import net.sf.json.JSONArray;

public class cwTree {
	public static boolean enableAppletTree = false;
	public boolean isJsonTree =false;
	public boolean zTree =false;
	
	public static final String NODE_TYPE_SKP = "SKP";
	public static final String NODE_TYPE_CSP = "CSP";
	// public static final String NODE_TYPE_USR_SKILLS="USS"; 

	public static final String ALL = "ALL";
	public static final String UNSPECIFIED = "UNSPECIFIED";

    public String tree_type;
    public String tree_subtype;
    public String tree_title;
    public long tree_id;
    public long node_id;
    public String virtual_catalog;
    public int pick_method;
    public int auto_pick;
    public int pick_leave;
    public int pick_root;
    public int complusory_tree;
    public String node_type;
    public String js;
    public String[] catalogItemTypes;
    public boolean flag;
    public boolean get_supervise_group;
    public boolean get_direct_supervise;
    public boolean override_appr_usg;
    public boolean hasGlobalCat;
    public String ftn_ext_id;
    public String rol_ext_id;
    public long parent_tcr_id;
    public boolean tcEnableInd;
    public boolean isLrnShowShareTraining = true;
    public boolean filter_user_group;
    public boolean isCreateTree = false;
    public boolean hasGlobalCat_curTree;
    public boolean show_bil_nickname;

    public long sgp_id;
    public long itm_id;

    public boolean appendUsrSkills = false;

    public class nodeInfo {
        public String title;
        public long id;
        public String type;
        public String is_folder;
        public String has_child;
        public String pickable;
        public long count;
        public String node_type;
        public String fullPath;
        public String virtual_catalog;
        public nodeInfo(String title, long id, String type, String is_folder, String has_child) {
            this.title = title;
            this.id = id;
            this.type = type;
            this.is_folder = is_folder;
            this.has_child = has_child;
            this.pickable = "YES";
            this.count = -1;
        }

        public nodeInfo(String title, long id, String type, String is_folder, String has_child, String pickable, String node_type) {
            this.title = title;
            this.id = id;
            this.type = type;
            this.is_folder = is_folder;
            this.has_child = has_child;
            this.pickable = pickable;
            this.count = -1;
            this.node_type = node_type;
        }

        public nodeInfo(String title, long id, String type, String is_folder, String has_child, long count) {
            this.title = title;
            this.id = id;
            this.type = type;
            this.is_folder = is_folder;
            this.has_child = has_child;
            this.pickable = "YES";
            this.count = count;
        }

        public nodeInfo(String title, long id, String type, String is_folder, String has_child, long count, String node_type) {
            this.title = title;
            this.id = id;
            this.type = type;
            this.is_folder = is_folder;
            this.has_child = has_child;
            this.pickable = "YES";
            this.count = count;
            this.node_type = node_type;
        }

        public nodeInfo(String title, long id, String type, String is_folder, String has_child, String pickable) {
            this.title = title;
            this.id = id;
            this.type = type;
            this.is_folder = is_folder;
            this.has_child = has_child;
            this.pickable = pickable;
            this.count = -1;
        }

        public nodeInfo(String title, long id, String type, String is_folder, String has_child, String fullPath, boolean is_targeted_lrn) {
            this.title = title;
            this.id = id;
            this.type = type;
            this.is_folder = is_folder;
            this.has_child = has_child;
            this.pickable = "YES";
            this.count = -1;
            this.fullPath = fullPath;
        }
        public nodeInfo(String title, String virtual_catalog, String type, String is_folder, String has_child, String pickable) {
            this.title = title;
            this.virtual_catalog = virtual_catalog;
            this.type = type;
            this.is_folder = is_folder;
            this.has_child = has_child;
            this.pickable = "YES";
            this.count = -1;
            this.pickable = pickable;
        }

    }

    private static final String TREE_ROOT = "ROOT_0_0_0";
    private static final String APPR_ROOT = "APPR_0_0_0";

    public static final String CATALOG = "CATALOG";
    public static final String ITEM = "ITEM";
    public static final String NORMAL = "NORMAL";
	public static final String ITEM_FROM_CATALOG = "ITEM_FROM_CATALOG"; // return item_id from catalog instead of tnd_id
    public static final String COMPETENCE_PROFILE = "COMPETENCE_PROFILE";
    public static final String COMPETENCE = "COMPETENCE";
    public static final String TARGETED_LEARNER = "TARGETED_LEARNER";
    public static final String EXEMPT_LEARNER = "EXEMPT_LEARNER";
    public static final String USER_GROUP = "USER_GROUP";
    public static final String MY_STAFF = "MY_STAFF";
    public static final String USER_GROUP_AND_USER = "USER_GROUP_AND_USER";
    public static final String GRADE = "GRADE";
    public static final String GRADE_AND_USER = "GRADE_AND_USER";
    public static final String INDUSTRY = "INDUSTRY";
    public static final String INDUSTRY_AND_USER = "INDUSTRY_AND_USER";
    public static final String KNOWLEDGE_OBJECT = "KNOWLEDGE_OBJECT";
    public static final String SYLLABUS_AND_OBJECT = "SYLLABUS_AND_OBJECT";
	public static final String MOVE_OBJECTIVE = "MOVE_OBJECTIVE";   //Only for move button in the learning resource management
	public static final String KM_DOMAIN = "KM_DOMAIN";
	public static final String KM_DOMAIN_AND_OBJECT = "KM_DOMAIN_AND_OBJECT";
	public static final String KM_DOMAIN_ONLY_EDIT = "KM_DOMAIN_ONLY_EDIT";
	public static final String KM_WORK_FOLDER = "KM_WORK_FOLDER";
	public static final String KM_WORK_FOLDER_AND_OBJECT = "KM_WORK_FOLDER_AND_OBJECT";
	public static final String KM_WORK_FOLDER_ONLY_EDIT = "KM_WORK_FOLDER_ONLY_EDIT";
	public static final String KM_CATALOGS = "KM_CATALOGS";

    public static final String USER_CLASSIFICATION = "USER_CLASSIFICATION";

    public static final String TRAINING_CENTER = "TRAINING_CENTER";
    public static final String TRAINING_CENTER_NOACL = "TRAINING_CENTER_NOACL";
    public static final String NAV_TRAINING_CENTER = "NAV_TRAINING_CENTER";
    public static final String TC_CATALOG_ITEM = "TC_CATALOG_ITEM";
    public static final String TC_CATALOG_ITEM_BATCHUPDATE="TC_CATALOG_ITEM_BATCHUPDATE";
    public static final String TC_CATALOG_ITEM_SELF = "TC_CATALOG_ITEM_SELF";
    public static final String TC_CATALOG_ITEM_RUN = "TC_CATALOG_ITEM_RUN";
    public static final String TC_CATALOG_ITEM_AND_RUN = "TC_CATALOG_ITEM_AND_RUN";
    public static final String TC_CATALOG_ITEM_INTEGRATED = "TC_CATALOG_ITEM_INTEGRATED";
    public static final String TC_CATALOG = "TC_CATALOG";
    public static final String TC_SYLLABUS_AND_OBJECT = "TC_SYLLABUS_AND_OBJECT";
    public static final String NAV_TRAINING_CENTER_FOR_TA = "NAV_TRAINING_CENTER_FOR_TA";
    public static final String COMPETENCE_PROFILE_AND_SKILL ="COMPETENCE_PROFILE_AND_SKILL";


    public static final String NODE_TYPE_TC = "TC";
    public static final String NODE_TYPE_CATALOG = "CATALOG";
    public static final String NODE_TYPE_OBJECTIVE = "OBJECTIVE";

    public static final int PICK_ANY_MULT = 1;
    public static final int PICK_ANY_SINGLE = 2;
    public static final int PICK_LEAVE_FOLDER_MULT = 3;
    public static final int PICK_LEAVE_NOT_FOLDER_MULT = 4;

    public static final String LAB_DIRECT_STAFF = "458";
    public static final String LAB_DEPARTMENT = "262";

    public cwTree() {
        tree_type = null;
        tree_subtype = null;
        tree_title = "";
        tree_id = 0;
        node_id = 0;
        pick_method = PICK_ANY_MULT; // default method
        auto_pick = 0;
        pick_leave = 0;
        pick_root = 1;
        complusory_tree = 1;
        virtual_catalog ="";
        sgp_id=0;
    }

    public String treeXML(Connection con, loginProfile prof ,boolean isTcIndependent) throws qdbException, SQLException, cwSysMessage, cwException {
        String tree_xml = null;
        if (tree_type.equalsIgnoreCase(CATALOG) || tree_type.equalsIgnoreCase(ITEM)) {
            tree_xml = catalog2Tree(con, prof);
        } else if (tree_type.equalsIgnoreCase(ITEM_FROM_CATALOG)) {
            tree_xml = catalog2Tree(con, prof, true);
        } else if (tree_type.equalsIgnoreCase(COMPETENCE_PROFILE)) {
            tree_xml = competenceProfile2Tree(con, prof);
        } else if (tree_type.equalsIgnoreCase(COMPETENCE)) {
            tree_xml = competence2Tree(con, prof);
        } else if (tree_type.equalsIgnoreCase(TARGETED_LEARNER)) {
            tree_xml = targetedLearner2Tree(con, prof);
        } else if (tree_type.equalsIgnoreCase(EXEMPT_LEARNER)) {
            tree_xml = exemptLearner2Tree(con, prof);
        } else if (tree_type.equalsIgnoreCase(USER_GROUP) || tree_type.equalsIgnoreCase(USER_GROUP_AND_USER)) {
        	// LN模式下
        	if (isTcIndependent) {
        		//查看是当前培训中心为几级
				String sql = "select COUNT(*) from tcRelation where tcn_child_tcr_id = " + prof.tcId;
				int count = getTcChenk(con, sql);
				Boolean type = false;
				if(null != prof.group_cmd && "upd_tc_prep".equals(prof.group_cmd) && count < 2){
					type = true;//如果是一级或者是二级还有是修改的就设置为true，LN模式逻辑
				}
				//一级和二级培训中心的LN逻辑入口
				if(prof.tcId.equals("1") || type){
					tree_xml = userGroup2Tree(con, prof, "simple");
				}else{
					tree_xml = userGroup2Tree(con, prof);
				}
            }else {
                tree_xml = userGroup2Tree(con, prof);
            }

        } else if (tree_type.equalsIgnoreCase(MY_STAFF)) {
            tree_xml = myStaff2Tree(con, prof);
        } else if (tree_type.equalsIgnoreCase(GRADE) || tree_type.equalsIgnoreCase(GRADE_AND_USER)) {
            tree_xml = grade2Tree(con, prof);
        } else if (tree_type.equalsIgnoreCase(INDUSTRY) || tree_type.equalsIgnoreCase(INDUSTRY_AND_USER)) {
            tree_xml = industry2Tree(con, prof);
        } else if (tree_type.equalsIgnoreCase(KNOWLEDGE_OBJECT)) {
            tree_xml = knowledgeObject2Tree(con, prof, tree_type,isTcIndependent);
        } else if (tree_type.equalsIgnoreCase(SYLLABUS_AND_OBJECT) || tree_type.equalsIgnoreCase(MOVE_OBJECTIVE)) {
            tree_xml = knowledgeObject2Tree(con, prof, tree_type,isTcIndependent);
        } else if (tree_type.equalsIgnoreCase(KM_DOMAIN) || tree_type.equalsIgnoreCase(KM_DOMAIN_ONLY_EDIT)) {
            tree_xml = kmNode2Tree(con, prof, tree_type);
        } else if (tree_type.equalsIgnoreCase(KM_WORK_FOLDER) || tree_type.equalsIgnoreCase(KM_WORK_FOLDER_ONLY_EDIT)) {
            tree_xml = kmNode2Tree(con, prof, tree_type);
        } else if (tree_type.equalsIgnoreCase(KM_DOMAIN_AND_OBJECT) || tree_type.equalsIgnoreCase(KM_WORK_FOLDER_AND_OBJECT)) {
            tree_xml = kmObject2Tree(con, prof, tree_type);
        } else if (tree_type.equalsIgnoreCase(USER_CLASSIFICATION)){
            tree_xml = classification2Tree(con, prof, tree_type, tree_subtype);
        } else if (tree_type.equalsIgnoreCase(TRAINING_CENTER) || tree_type.equalsIgnoreCase(NAV_TRAINING_CENTER) || tree_type.equalsIgnoreCase(TRAINING_CENTER_NOACL)){
            tree_xml = trainingCenter2Tree(con, prof, tree_type);
        } else if (tree_type.equalsIgnoreCase(TC_CATALOG_ITEM) || tree_type.equalsIgnoreCase(TC_CATALOG) || tree_type.equalsIgnoreCase(TC_CATALOG_ITEM_RUN)
        	||tree_type.equalsIgnoreCase(TC_CATALOG_ITEM_BATCHUPDATE) || tree_type.equalsIgnoreCase(TC_CATALOG_ITEM_SELF) || tree_type.equalsIgnoreCase(TC_CATALOG_ITEM_AND_RUN) || tree_type.equalsIgnoreCase(TC_CATALOG_ITEM_INTEGRATED)){
            tree_xml = tcItemCatalog2Tree(con, prof, tree_type,sgp_id);
        } else if (tree_type.equalsIgnoreCase(TC_SYLLABUS_AND_OBJECT)){
            tree_xml = tcSyllabusObj2Tree(con, prof, tree_type);
        } else if (tree_type.equalsIgnoreCase(NAV_TRAINING_CENTER_FOR_TA)) {
        	tree_xml = trainingCenter4Ta2Tree(con, prof, tree_type);
        } else if(tree_type.equalsIgnoreCase(COMPETENCE_PROFILE_AND_SKILL)){
        	tree_xml = skillAndSkp2Tree(con, prof, tree_type);
        }
     //   System.out.println("xml--"+tree_xml);
        return tree_xml;
    }

    public String create_treeXML(Connection con, loginProfile prof) throws SQLException, cwSysMessage, cwException {
    	String[] tree_sub_type_lst;
        if(tree_type.equalsIgnoreCase(TARGETED_LEARNER)){
        	tree_sub_type_lst = ViewEntityToTree.getTargetEntity(con, prof.root_ent_id);
        }else {
        	tree_sub_type_lst = new String[]{tree_type};
        }

    	//build catalog tree xml for Multi-organization
    	if(tree_type.equalsIgnoreCase(CATALOG) && hasGlobalCat){
    		tree_sub_type_lst = new String[]{tree_type,tree_type};
    	}

        StringBuffer tree_xml = new StringBuffer();
        tree_xml.append("<tree_list_js>");
        for(int i=0;i<tree_sub_type_lst.length;i++){
        	if(tree_sub_type_lst[i].equalsIgnoreCase(USER_GROUP) ||
        			tree_sub_type_lst[i].equalsIgnoreCase(GRADE) ||
        			tree_sub_type_lst[i].equalsIgnoreCase(INDUSTRY)
        			){
        		tree_id =  ViewEntityToTree.getRootId(con, tree_sub_type_lst[i], prof.root_ent_id);
        	}

        	if(tree_sub_type_lst[i].equalsIgnoreCase(MOVE_OBJECTIVE)){
        		getTreeRootInfo(con);
        	}
        	nodeInfo node = new nodeInfo(TREE_ROOT, tree_id, TREE_ROOT, "YES", "YES");

        	tree_xml.append("<tree")
        			.append(" srcid=\"").append(node.id).append("\"")
        			.append(" title=\"").append(dbUtils.esc4XML(tree_title)).append("\"")
        			.append(" type=\"").append(tree_sub_type_lst[i]).append("\"")
//        			.append(" checkbox=\"").append(true).append("\"")
//        			.append(" radio=\"").append(true).append("\"")
//        			.append(" rootbox=\"").append(true).append("\"")
//        			.append(" nodebox=\"").append(true).append("\"")
	            .append(" subtype=\"").append(cwUtils.escNull(tree_subtype)).append("\"")
	            .append(" pick_method=\"").append(pick_method).append("\"")
	            .append(" auto_pick=\"").append(auto_pick).append("\"")
	            .append(" pick_leave=\"").append(pick_leave).append("\"")
	            .append(" pick_root=\"").append(pick_root).append("\"")
	            .append(" complusory_tree=\"").append(complusory_tree).append("\"")
//	            .append("\" cat_public_ind=\"").append(catPublicInd ? "1" : "0")
	            .append(" ftn_ext_id=\"").append(cwUtils.escNull(ftn_ext_id)).append("\"")
	            .append(" rol_ext_id=\"").append(cwUtils.escNull(rol_ext_id)).append("\"")
            	.append(" pickable=\"YES\"");
        	if(tree_type.equalsIgnoreCase(CATALOG) && hasGlobalCat && i!=0){
        		tree_xml.append(" global_cat=\"true\"");
        	}else{
        		tree_xml.append(" global_cat=\"false\"");
        	}
	       if(tree_type.equalsIgnoreCase(NAV_TRAINING_CENTER) || tree_type.equalsIgnoreCase(NAV_TRAINING_CENTER_FOR_TA)) {
	    	   tree_xml.append(" onaction=\"show_content('0')\"");
		   }
        	tree_xml.append("/>");
        }
        tree_xml.append("</tree_list_js>");


//        System.out.println(tree_xml);
        return tree_xml.toString();
    }

    private void getTreeRootInfo(Connection con) throws SQLException{
    	if(node_id!=0){
			ViewKnowledgeObjectToTree view = new ViewKnowledgeObjectToTree();
			ViewKnowledgeObjectToTree.objectInfo objInfo = view.getObjectInfo(con, node_id);
			tree_title = objInfo.title;
			tree_id = objInfo.id;
    	}
    }


    private String catalog2Tree(Connection con, loginProfile prof) throws qdbException, SQLException, cwSysMessage {
		return catalog2Tree(con, prof, false);
	}

	private String catalog2Tree(Connection con, loginProfile prof, boolean getItemId) throws qdbException, SQLException, cwSysMessage {
        StringBuffer xmlBuf = new StringBuffer(1024);
        boolean checkStatus = false;
        
   
        if(! AccessControlWZB.hasRolePrivilege( prof.current_role,AclFunction.FTN_AMD_CAT_MAIN) ){
            checkStatus = true;
        }

        if(enableAppletTree){
        	xmlBuf.append(catalog2Tree(con, prof, getItemId, checkStatus, false));

	        if(node_id == 0) {
	            //check if need to get global catalogs
	            //DENNIS
	            if(this.hasGlobalCat) {
	                xmlBuf.append(catalog2Tree(con, prof, getItemId, checkStatus, true));
	            }
	        }
        }else{// Use javascript-tree

        	if(node_id==0 && hasGlobalCat_curTree){
        		xmlBuf.append(catalog2Tree(con, prof, getItemId, checkStatus, true));
        	}else{
        		xmlBuf.append(catalog2Tree(con, prof, getItemId, checkStatus, false));
        	}
        }

        String xml = format2TreeXML(xmlBuf.toString());

        if (node_id == 0) {
            return formatXML(xml, 1, js);
        } else {
            return xml;
        }
    }


	private String catalog2Tree(Connection con, loginProfile prof, boolean getItemId, boolean checkStatus, boolean catPublicInd) throws qdbException, SQLException, cwSysMessage {
        Vector nodes = new Vector();
        Vector tempNodes = new Vector();

        nodeInfo node_info = null;
        String is_folder = null;
        String has_child = null;
        long[] tnd_lst = null;
        long[] usr_ent_ids = usrGroups(prof.usr_ent_id, prof.usrGroups);
        Vector tree_node_lst;
        Hashtable notChild;
        String result;

        if (node_id == 0) {
            tree_node_lst = new Vector();

            Vector cat_lst =  aeCatalog.orgCatalogListAsVector(con, usr_ent_ids, checkStatus, catalogItemTypes, this.parent_tcr_id, this.tcEnableInd);

            for (int i=0; i<cat_lst.size(); i++) {
                tree_node_lst.addElement((aeTreeNode)((aeCatalog)cat_lst.elementAt(i)).cat_treenode);
            }

            node_info = new nodeInfo(TREE_ROOT, 0, TREE_ROOT, "YES", "YES");
            nodes.addElement(node_info);
        } else {
            if (tree_type.equalsIgnoreCase(CATALOG)) {
                tree_node_lst = aeTreeNode.getChildAndItemNodeVector(con, checkStatus, node_id, true);
            } else {
                tree_node_lst = aeTreeNode.getChildAndItemNodeVector(con, checkStatus, node_id, false);
            }

            node_info = new nodeInfo("IGNORE", node_id, "IGNORE", "YES", "YES");
            nodes.addElement(node_info);
        }

        tnd_lst = new long[tree_node_lst.size()];

        for (int i=0; i<tree_node_lst.size(); i++) {
            aeTreeNode node = (aeTreeNode)tree_node_lst.elementAt(i);
            tnd_lst[i] = node.tnd_id;

            node_info = new nodeInfo(node.tnd_title, node.tnd_id, node.tnd_type, "YES", "YES");
            nodes.addElement(node_info);
        }

        if (tree_type.equalsIgnoreCase(CATALOG)) {
            notChild = ViewCatalogToTree.treeNodeNoChildAndTypeInfo(con, tnd_lst, checkStatus, true);
        } else {
            notChild = ViewCatalogToTree.treeNodeNoChildAndTypeInfo(con, tnd_lst, checkStatus, false);
        }

        Enumeration enumeration = notChild.keys();

        while (enumeration.hasMoreElements()) {
            String node_id = (String)enumeration.nextElement();

            for (int i=0; i<nodes.size(); i++) {
                node_info = (nodeInfo)nodes.elementAt(i);

                if (node_id.equals((new Long(node_info.id)).toString())) {
                    String node_type = (String)notChild.get(node_id);
                    node_info.has_child = "NO";

                    if (node_type.equals("ITEM")) {
                        node_info.is_folder = "NO";
                    }
                }
            }
        }

        for (int i=0; i<nodes.size(); i++) {
            node_info = (nodeInfo)nodes.elementAt(i);
            if(node_info.is_folder.equalsIgnoreCase("NO") && node_info.has_child.equalsIgnoreCase("NO")) {
                aeTreeNode treeNode = new aeTreeNode();
                treeNode.tnd_id = node_info.id;
                node_info.id = treeNode.getItemId(con);
            }
            if(tree_type.equalsIgnoreCase(ITEM) && node_info.is_folder.equalsIgnoreCase("YES")) {
                node_info.pickable = "NO";
            }
        }

        if (tree_type.equalsIgnoreCase(CATALOG)){
            result = formatVector2XML(nodes, CATALOG, CATALOG, pick_method, auto_pick, pick_leave, pick_root, complusory_tree, catPublicInd, tree_subtype, ftn_ext_id);
        } else if(tree_type.equalsIgnoreCase(ITEM)) {
            result = formatVector2XML(nodes, ITEM, ITEM, pick_method, auto_pick, pick_leave, pick_root, complusory_tree, catPublicInd, tree_subtype, ftn_ext_id);
        } else {
            result = formatVector2XML(nodes, ITEM, ITEM, pick_method, auto_pick, pick_leave, pick_root, complusory_tree, catPublicInd, tree_subtype, ftn_ext_id);
        }

        return result;
    }

    private String competence2Tree(Connection con, loginProfile prof) throws SQLException {
        String result;
        Vector nodes = new Vector();
        Vector lst;
        Vector has_child_lst;
        nodeInfo node_info = null;
        ViewCmToTree cmTree = new ViewCmToTree();
        ViewCmToTree.skillInfo node;
        Hashtable h_child_count = null;

        if (node_id == 0) {
            lst = cmTree.SkillRootListAsVector(con, prof.root_ent_id);
            has_child_lst = cmTree.SkillFolderHasChild(con, lst);
            node_info = new nodeInfo(TREE_ROOT, 0, TREE_ROOT, "YES", "YES");
            nodes.addElement(node_info);
            h_child_count = cmTree.SkillFolderChildCount(con, lst);
            for (int i=0; i<lst.size(); i++) {
                node = (ViewCmToTree.skillInfo)lst.elementAt(i);
				if( !h_child_count.containsKey(new Long(node.id)) ) {
					continue;
				}
                if(node.type.equals(DbCmSkillBase.COMPETENCY_GROUP)) {
                    node_info = new nodeInfo(node.title, node.id, node.type, "YES", has_child_lst.contains(node) ? "YES" : "NO");
                } else {
                    node_info = new nodeInfo(node.title, node.id, node.type, "NO", "NO");
                }

                nodes.addElement(node_info);
            }
        } else {
            lst = cmTree.skillListAsVector(con, node_id);
            has_child_lst = cmTree.SkillFolderHasChild(con, lst);
            node_info = new nodeInfo("IGNORE", node_id, "IGNORE", "YES", "YES");
            nodes.addElement(node_info);

            for (int i=0; i<lst.size(); i++) {
                node = (ViewCmToTree.skillInfo)lst.elementAt(i);

                if (node.type.equals(DbCmSkillBase.COMPETENCY_GROUP)) {
                    if (has_child_lst.contains(node)) {
                        node_info = new nodeInfo(node.title, node.id, node.type, "YES", "YES");
                    } else {
                        node_info = new nodeInfo(node.title, node.id, node.type, "YES", "NO");
                    }
                } else {
                    node_info = new nodeInfo(node.title, node.id, node.type, "NO", "NO");
                }

                nodes.addElement(node_info);
            }
        }

        result = formatVector2XML(nodes, COMPETENCE, COMPETENCE, pick_method, auto_pick, pick_leave, pick_root, complusory_tree, false, tree_subtype, ftn_ext_id);
        result = format2TreeXML(result);
//System.out.println(result);
        if (node_id == 0) {
            return formatXML(result, 1, js);
        } else {
            return result;
        }
    }

    private String competenceProfile2Tree(Connection con, loginProfile prof) throws SQLException {
        String result;
        Vector nodes = new Vector();
        nodeInfo node_info = null;
        ViewCmToTree cmTree = new ViewCmToTree();
		ViewCmToTree.skillInfo node;

		if (node_id == 0) {
            node_info = new nodeInfo(TREE_ROOT, 0, TREE_ROOT, "YES", "YES");
            nodes.addElement(node_info);

            Vector lst = cmTree.skpListAsVector(con, prof, qdbAction.wizbini.cfgSysSetupadv.isTcIndependent());
            for (int i=0; i<lst.size(); i++) {
                node = (ViewCmToTree.skillInfo)lst.elementAt(i);
                node_info = new nodeInfo(node.title, node.id, node.type, "NO", "NO");
                nodes.addElement(node_info);
            }
        }

		result = formatVector2XML(nodes, COMPETENCE_PROFILE, COMPETENCE_PROFILE, pick_method, auto_pick, pick_leave, pick_root, complusory_tree, false, tree_subtype, ftn_ext_id);
        result = format2TreeXML(result);

        if (node_id == 0) {
            return formatXML(result, 1, js);
        } else {
            return result;
        }
    }

    private String targetedLearner2Tree(Connection con, loginProfile prof) throws SQLException, cwException, qdbException {
        String result;
        StringBuffer temp = new StringBuffer();
        int count = 0;
        String[] tree_sub_type_lst = ViewEntityToTree.getTargetEntity(con, prof.root_ent_id);

        auto_pick = 1; //override

        if (tree_sub_type_lst != null && tree_sub_type_lst.length > 0) {
            for (int i=0; i<tree_sub_type_lst.length; i++) {
                if (tree_sub_type_lst[i].equalsIgnoreCase(USER_GROUP)) {
                    temp.append(entity2Tree(con, prof, USER_GROUP, USER_GROUP, true));
                    count++;
                } else if (tree_sub_type_lst[i].equalsIgnoreCase(GRADE)) {
                    temp.append(entity2Tree(con, prof, GRADE, GRADE, true));
                    count++;
                } else if (tree_sub_type_lst[i].equalsIgnoreCase(INDUSTRY)) {
                    temp.append(entity2Tree(con, prof, INDUSTRY, INDUSTRY, true));
                    count++;
                }
            }
        } /*else {
            temp.append(entity2Tree(con, prof, USER_GROUP, USER_GROUP, true));
            temp.append(entity2Tree(con, prof, GRADE, GRADE, true));
            temp.append(entity2Tree(con, prof, INDUSTRY, INDUSTRY, true));
            count = 3;
        }*/

        result = format2TreeXML(temp.toString());

        if (node_id == 0) {
            return formatXML(result, count, js);
        } else {
            return result;
        }
    }

    private String exemptLearner2Tree(Connection con, loginProfile prof) throws SQLException, cwException, qdbException {
        String result;
        StringBuffer temp = new StringBuffer();
        int count = 0;
        Vector vtTreeSubtype = ViewEntityToTree.getExemptEntity(con, prof.root_ent_id);
//        Vector vtTreeSubtype = new Vector();
//        vtTreeSubtype.addElement("CLASS1");
//        vtTreeSubtype.addElement(USER_GROUP);

//        auto_pick = 1; //override

        for (int i=0; i<vtTreeSubtype.size(); i++) {
            String subtype = (String)vtTreeSubtype.elementAt(i);
                if (subtype.equalsIgnoreCase(USER_GROUP)) {
                    temp.append(entity2Tree(con, prof, USER_GROUP, USER_GROUP, true));
                    count++;
                } else if (subtype.equalsIgnoreCase(GRADE)) {
                    temp.append(entity2Tree(con, prof, GRADE, GRADE, true));
                    count++;
                } else if (subtype.equalsIgnoreCase(INDUSTRY)) {
                    temp.append(entity2Tree(con, prof, INDUSTRY, INDUSTRY, true));
                    count++;
                } else{
                    temp.append(entity2Tree(con, prof, subtype, USER_CLASSIFICATION, true));
                    count++;
                }
        }
        /* else {
            temp.append(entity2Tree(con, prof, USER_GROUP, USER_GROUP, true));
            temp.append(entity2Tree(con, prof, GRADE, GRADE, true));
            temp.append(entity2Tree(con, prof, INDUSTRY, INDUSTRY, true));
            count = 3;
        }*/

        result = format2TreeXML(temp.toString());

        if (node_id == 0) {
            return formatXML(result, count, js);
        } else {
            return result;
        }
    }

    /**
    Generate XML for the approver who wants to view his/her approval groups
    @param con Connection to database
    @param prof current loginProfile
    @param tree_type either USER_GROUP or USER_GROUP_AND_USER
    @param notIncludeChild indicate to include users in the tree or not
    @return XML of tree with a dummy root node
    */
    private String apprGroup2Tree(Connection con, loginProfile prof,
                                  String tree_type, boolean notIncludeChild)
                                  throws SQLException, cwException {

        Vector nodes = new Vector();
        nodeInfo node_info = null;
        ViewEntityToTree.entityInfo ent_info;
        Vector notChild;
        long[] ent_id_lst;

        //get prof's approval groups
        ViewRoleTargetGroup[] apprGroups =
            ViewRoleTargetGroup.getTargetGroups(con, prof.usr_ent_id, prof.current_role, true, true);

        //add an dummy root
        node_info = new nodeInfo(APPR_ROOT, 0, APPR_ROOT, "YES", "YES");
        nodes.addElement(node_info);

        //append the prof's approval and supervised groups together into the tree
        ent_id_lst = new long[apprGroups.length ];
        for (int i=0; i<apprGroups.length; i++) {
            ent_id_lst[i] = apprGroups[i].targetEntIds[0];
            node_info = new nodeInfo(apprGroups[i].targetEntNames[0],
                                     apprGroups[i].targetEntIds[0],
                                     apprGroups[i].targetEntTypes[0],

                                     "YES", "YES");
            nodes.addElement(node_info);
        }

        //get the list of approval does not has child
        notChild = ViewEntityToTree.entityNoChildAndTypeInfo(con, ent_id_lst, USER_GROUP, notIncludeChild, ftn_ext_id);

        //update the nodes vector with the information that some node do not have child
        for (int m=0;m<notChild.size(); m++) {
            String node_id = (String)notChild.elementAt(m);

            for (int i=0; i<nodes.size(); i++) {
                node_info = (nodeInfo)nodes.elementAt(i);

                if (node_id.equals((new Long(node_info.id)).toString())) {
                    node_info.has_child = "NO";

                    if (node_info.type.equals("USR")) {
                        node_info.is_folder = "NO";
                    }
                }
            }
        }
        //vednor XML and return
        return formatVector2XML(nodes, tree_type, tree_type, pick_method, auto_pick, pick_leave, pick_root, complusory_tree, false, tree_subtype, ftn_ext_id);
    }

    /**
    Generate XML for the approver who wants to view his/her supervise groups
    @param con Connection to database
    @param prof current loginProfile
    @param tree_type either USER_GROUP or USER_GROUP_AND_USER
    @param notIncludeChild indicate to include users in the tree or not
    @return XML of tree with a dummy root node
    */
    public String  supGroup2Tree(Connection con, loginProfile prof,
                                  boolean get_supervise_group , boolean get_direct_supervise, String tree_type, boolean notIncludeChild)
                                  throws SQLException, cwException {

        Vector nodes = new Vector();
        Vector jsonNodes =new Vector();
        nodeInfo node_info = null;
        ViewEntityToTree.entityInfo ent_info;
        Vector notChild;

        Vector vEntId = new Vector();

        //add an dummy root
        node_info = new nodeInfo(APPR_ROOT, 0, APPR_ROOT, "YES", "YES");
        nodes.addElement(node_info);

        //get prof's supervise groups
        if (get_supervise_group){
            Supervisor sup = new Supervisor(con, prof.usr_ent_id);
            Vector vSupGroups = sup.getSuperviseGroup(con, prof.cur_lan);

            //append the prof's supervised groups together into the tree
            for (int i=0; i<vSupGroups.size(); i++) {
                dbUserGroup usg = (dbUserGroup)vSupGroups.elementAt(i);
                vEntId.addElement(new Long(usg.usg_ent_id));
                node_info = new nodeInfo(usg.usg_display_bil,
                                     usg.usg_ent_id,
                                     usg.ent_type,
                                     "YES", "YES");
                nodes.addElement(node_info);
            }
        }
        if (get_direct_supervise){
        	CommonLog.debug("get_direct_supervise");
            Supervisor sup = new Supervisor(con, prof.usr_ent_id);
            Vector v_usr = sup.getDirectStaff(con);
            CommonLog.debug(String.valueOf(v_usr.size()));
            //append the prof's direct supervise ppl together into the tree
            for (int i=0; i<v_usr.size(); i++) {
                dbRegUser usr = (dbRegUser)v_usr.elementAt(i);
                vEntId.addElement(new Long(usr.usr_ent_id));
                node_info = new nodeInfo(usr.usr_display_bil,
                                     usr.usr_ent_id,
                                     dbEntity.ENT_TYPE_USER,
                                     "NO", "NO");
                nodes.addElement(node_info);
            }
        }

        long[] ent_id_lst = cwUtils.vec2longArray(vEntId);
        //get the list of approval does not has child
        notChild = ViewEntityToTree.entityNoChildAndTypeInfo(con, ent_id_lst, USER_GROUP, notIncludeChild, ftn_ext_id);

        //update the nodes vector with the information that some node do not have child
        for (int m=0;m<notChild.size(); m++) {
            String node_id = (String)notChild.elementAt(m);

            for (int i=0; i<nodes.size(); i++) {
                node_info = (nodeInfo)nodes.elementAt(i);

                if (node_id.equals((new Long(node_info.id)).toString())) {
                    node_info.has_child = "NO";

                    if (node_info.type.equals("USR")) {
                        node_info.is_folder = "NO";
                    }
                }
            }
        }
        	//vednor XML and return
        	return formatVector2XML(nodes, tree_type, tree_type, pick_method, auto_pick, pick_leave, pick_root, complusory_tree, false, tree_subtype, ftn_ext_id);
    }

    public String userGroup2Tree(Connection con, loginProfile prof) throws SQLException, cwException, qdbException {
        String result;
        if(get_supervise_group || get_direct_supervise) {
            //Supervisor views his/her approval groups
            if (tree_type.equalsIgnoreCase(USER_GROUP)){
                result = supGroup2Tree(con, prof, get_supervise_group , get_direct_supervise, tree_type, true);
            }else{
                result = supGroup2Tree(con, prof, get_supervise_group , get_direct_supervise, tree_type, false);
            }
        }else if(!override_appr_usg && node_id == 0 && dbUtils.isUserApprRole(con, prof.usr_ent_id, prof.current_role)) {
            //Approver role wants to view the user group's root
            result = apprGroup2Tree(con, prof, tree_type, tree_type.equalsIgnoreCase(USER_GROUP));
        }
        else if (tree_type.equalsIgnoreCase(USER_GROUP)) {
            result = entity2Tree(con, prof, USER_GROUP, tree_type, true);
        } else {
            result = entity2Tree(con, prof, USER_GROUP, tree_type, false);
        }
        if(isJsonTree){
        	return result;
        }else{
        	result = format2TreeXML(result);


        	if (node_id == 0 || flag) {
        		return formatXML(result, 1, js);
        	} else {
        		return result;
        	}
        }
    }

    public String userGroup2Tree(Connection con, loginProfile prof,String  simple) throws SQLException, cwException, qdbException {
        String result;
        if(get_supervise_group || get_direct_supervise) {
            //Supervisor views his/her approval groups
            if (tree_type.equalsIgnoreCase(USER_GROUP)){
                result = supGroup2Tree(con, prof, get_supervise_group , get_direct_supervise, tree_type, true);
            }else{
                result = supGroup2Tree(con, prof, get_supervise_group , get_direct_supervise, tree_type, false);
            }
        }else if(!override_appr_usg && node_id == 0 && dbUtils.isUserApprRole(con, prof.usr_ent_id, prof.current_role)) {
            //Approver role wants to view the user group's root
            result = apprGroup2Tree(con, prof, tree_type, tree_type.equalsIgnoreCase(USER_GROUP));
        }
        else if (tree_type.equalsIgnoreCase(USER_GROUP)) {
            result = entity2Tree(con, prof, USER_GROUP, tree_type, true,simple);
        } else {
            result = entity2Tree(con, prof, USER_GROUP, tree_type, false);
        }
        if(isJsonTree){
            return result;
        }else{
            result = format2TreeXML(result);


            if (node_id == 0 || flag) {
                return formatXML(result, 1, js);
            } else {
                return result;
            }
        }
    }
    public String myStaff2Tree(Connection con, loginProfile prof) throws SQLException, cwException, qdbException {
    	String result;
    	result = entity2Tree(con, prof, MY_STAFF, tree_type, true);
        if(isJsonTree){
        	return result;
        }else{
        	result = format2TreeXML(result);


        	if (node_id == 0 || flag) {
        		return formatXML(result, 1, js);
        	} else {
        		return result;
        	}
        }
    }

    private String grade2Tree(Connection con, loginProfile prof) throws SQLException, qdbException {
        String result;

        if (tree_type.equalsIgnoreCase(GRADE)) {
            result = entity2Tree(con, prof, GRADE, tree_type, true);
        } else {
            result = entity2Tree(con, prof, GRADE, tree_type, false);
        }

        result = format2TreeXML(result);

        if (node_id == 0) {
            return formatXML(result, 1, js);
        } else {
            return result;
        }
    }

    private String industry2Tree(Connection con, loginProfile prof) throws SQLException, qdbException {
        String result;

        if (tree_type.equalsIgnoreCase(INDUSTRY)) {
            result = entity2Tree(con, prof, INDUSTRY, tree_type, true);
        } else {
            result = entity2Tree(con, prof, INDUSTRY, tree_type, false);
        }

        result = format2TreeXML(result);

        if (node_id == 0) {
            return formatXML(result, 1, js);
        } else {
            return result;
        }
    }

    private String classification2Tree(Connection con, loginProfile prof, String tree_type, String tree_subtype) throws SQLException, qdbException{
        String result;

        if (tree_type.equalsIgnoreCase(USER_CLASSIFICATION)) {
            result = entity2Tree(con, prof, tree_subtype, tree_type, true);
        } else {
            result = entity2Tree(con, prof, tree_subtype, tree_type, false);
        }

        result = format2TreeXML(result);

        if (node_id == 0) {
            return formatXML(result, 1, js);
        } else {
            return result;
        }

    }

    private String entity2Tree(Connection con, loginProfile prof, String group_type, String type, boolean notIncludeChild) throws SQLException, qdbException {
        Vector ent_lst;
        Vector nodes = new Vector();
        Vector jsonNodes = new Vector();
        nodeInfo node_info = null;
        ViewEntityToTree entity2Tree = new ViewEntityToTree();
        ViewEntityToTree.entityInfo ent_info;
        Vector notChild;
        long[] ent_id_lst;
        long id;

        if (node_id == 0) {
        	if (group_type.equals(MY_STAFF)) {
        		id = Supervise.STAFF_TYPE_ALL_STAFF;
        		node_info = new nodeInfo(TREE_ROOT, id, TREE_ROOT, "YES", "YES");
                nodes.addElement(node_info);
                node_info = new nodeInfo(LangLabel.getValue(prof.cur_lan, LAB_DIRECT_STAFF), Supervise.STAFF_TYPE_DIRECT_STAFF, "USG", "YES", "NO", null, false);
                nodes.addElement(node_info);
                node_info = new nodeInfo(LangLabel.getValue(prof.cur_lan, LAB_DEPARTMENT), Supervise.STAFF_TYPE_GROUP_STAFF, "USG", "YES", "YES", null, false);
                if (!entity2Tree.hasDirSuperviseGroup(con, prof.usr_ent_id)) {
                	node_info.has_child = "NO";
                }
                nodes.addElement(node_info);
        	} else {
        		id = ViewEntityToTree.getRootId(con, group_type, prof.root_ent_id);
        	}
            node_info = new nodeInfo(TREE_ROOT, id, TREE_ROOT, "YES", "YES");
            nodes.addElement(node_info);
        } else {
            id = node_id;
            if( flag )
                node_info = new nodeInfo(TREE_ROOT, id, TREE_ROOT, "YES", "YES");
            else
                node_info = new nodeInfo("IGNORE", node_id, "IGNORE", "YES", "YES");
            nodes.addElement(node_info);
        }

        if (id > 0) {
	        //get super tcr_id
			long sup_tcr_id = DbTrainingCenter.getSuperTcId(con, prof.root_ent_id);
	        if(parent_tcr_id != 0 && parent_tcr_id != sup_tcr_id && node_id == 0) {
	        	ent_lst = entity2Tree.getTcUsgAsVector(con, parent_tcr_id);
	        } else {
	        	ent_lst = entity2Tree.entityListAsVector(con, prof, id, group_type, notIncludeChild, this.ftn_ext_id, prof.usr_ent_id, prof.current_role, prof.root_ent_id, tcEnableInd, filter_user_group, show_bil_nickname, this.rol_ext_id);
	        }
        } else if (id == Supervise.STAFF_TYPE_GROUP_STAFF) {
        	//闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔告灮閹风兘鏁撻弬銈嗗
        	ent_lst = entity2Tree.getDirSuperviseGroup(con, prof.usr_ent_id, prof.cur_lan);
        } else {
        	ent_lst = entity2Tree.entityListAsVector(con, prof, id, group_type, notIncludeChild, this.ftn_ext_id, prof.usr_ent_id, prof.current_role, prof.root_ent_id, tcEnableInd, filter_user_group, show_bil_nickname, this.rol_ext_id);
        }

        ent_id_lst = new long[ent_lst.size()];

        for (int i=0; i<ent_lst.size(); i++) {
            ent_info = (ViewEntityToTree.entityInfo)ent_lst.elementAt(i);
            ent_id_lst[i] = ent_info.ent_id;

            node_info = new nodeInfo(ent_info.title, ent_info.ent_id, ent_info.type, "YES", "YES", ent_info.fullPath, false);
            nodes.addElement(node_info);
        }
        notChild = ViewEntityToTree.entityNoChildAndTypeInfo(con, ent_id_lst, group_type, notIncludeChild, ftn_ext_id);

        for (int m=0;m<notChild.size(); m++) {
            String node_id = (String)notChild.elementAt(m);

            for (int i=0; i<nodes.size(); i++) {
                node_info = (nodeInfo)nodes.elementAt(i);

                if (node_id.equals((new Long(node_info.id)).toString())) {
                    node_info.has_child = "NO";

                    if(node_info.type.equals("UGR") && !cwTree.enableAppletTree){
                    	node_info.is_folder = "NO";
                    }
                    if (node_info.type.equals("USR")) {
                        node_info.is_folder = "NO";
                    }
                }
            }
        }
        if(isJsonTree){
        	 for (int i=0; i<nodes.size(); i++) {
        		 node_info = (nodeInfo)nodes.elementAt(i);
        		 if(!(node_info.type.equals(TREE_ROOT) || node_info.type.equals(APPR_ROOT) || "IGNORE".equalsIgnoreCase(node_info.type))){
	        		 JsonTreeBean jNode =new JsonTreeBean();
	                 nodeInfo2JsonNode(node_info, jNode);
	                 jsonNodes.add(jNode);
        		 }
        	 }
        	JSONArray jObj =JSONArray.fromObject(jsonNodes);
        	return jObj.toString();
        }else{
        	return formatVector2XML(nodes, type, type, pick_method, auto_pick, pick_leave, pick_root, complusory_tree, false, tree_subtype, ftn_ext_id);
        }
    }
    private String entity2Tree(Connection con, loginProfile prof, String group_type, String type, boolean notIncludeChild,String simple) throws SQLException, qdbException {
        Vector ent_lst;
        Vector nodes = new Vector();
        Vector jsonNodes = new Vector();
        nodeInfo node_info = null;
        ViewEntityToTree entity2Tree = new ViewEntityToTree();
        ViewEntityToTree.entityInfo ent_info;
        Vector notChild;
        long[] ent_id_lst;
        long id;

        if (node_id == 0) {
            if (group_type.equals(MY_STAFF)) {
                id = Supervise.STAFF_TYPE_ALL_STAFF;
                node_info = new nodeInfo(TREE_ROOT, id, TREE_ROOT, "YES", "YES");
                nodes.addElement(node_info);
                //閻╂挳鏁撻弬銈嗗闁跨喐鏋婚幏鐑芥晸閺傘倖瀚�
                node_info = new nodeInfo(LangLabel.getValue(prof.cur_lan, LAB_DIRECT_STAFF), Supervise.STAFF_TYPE_DIRECT_STAFF, "USG", "YES", "NO", null, false);
                nodes.addElement(node_info);
                //闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔告灮閹风兘鏁撻弬銈嗗
                node_info = new nodeInfo(LangLabel.getValue(prof.cur_lan, LAB_DEPARTMENT), Supervise.STAFF_TYPE_GROUP_STAFF, "USG", "YES", "YES", null, false);
                if (!entity2Tree.hasDirSuperviseGroup(con, prof.usr_ent_id)) {
                    node_info.has_child = "NO";
                }
                nodes.addElement(node_info);
            } else {
                id = ViewEntityToTree.getRootId(con, group_type, prof.root_ent_id);
            }
            node_info = new nodeInfo(TREE_ROOT, id, TREE_ROOT, "YES", "NO");
            nodes.addElement(node_info);
        } else {
            id = node_id;
            if( flag )
                node_info = new nodeInfo(TREE_ROOT, id, TREE_ROOT, "YES", "YES");
            else
                node_info = new nodeInfo("IGNORE", node_id, "IGNORE", "YES", "YES");
            nodes.addElement(node_info);
        }

        if (id > 0) {
            //get super tcr_id
            long sup_tcr_id = DbTrainingCenter.getSuperTcId(con, prof.root_ent_id);
            if(parent_tcr_id != 0 && parent_tcr_id != sup_tcr_id && node_id == 0) {
                ent_lst = entity2Tree.getTcUsgAsVector(con, parent_tcr_id);
            } else {
                ent_lst = entity2Tree.entityListAsVector(con, prof, id, group_type, notIncludeChild, this.ftn_ext_id, prof.usr_ent_id, prof.current_role, prof.root_ent_id, tcEnableInd, filter_user_group, show_bil_nickname, this.rol_ext_id);
            }
        } else if (id == Supervise.STAFF_TYPE_GROUP_STAFF) {
            //闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔告灮閹风兘鏁撻弬銈嗗
            ent_lst = entity2Tree.getDirSuperviseGroup(con, prof.usr_ent_id, prof.cur_lan);
        } else {
            ent_lst = entity2Tree.entityListAsVector(con, prof, id, group_type, notIncludeChild, this.ftn_ext_id, prof.usr_ent_id, prof.current_role, prof.root_ent_id, tcEnableInd, filter_user_group, show_bil_nickname, this.rol_ext_id);
        }
        String sql ="select DISTINCT tce_ent_id from  tcTrainingCenterTargetEntity";
        List list = getTce_ent_id(con,sql,"tce_ent_id");
        ViewEntityToTree.entityInfo  info ;
        if (list.size()>0){
            for (int i =0 ;i<list.size(); i++){
                for (int j=0;j<ent_lst.size();j++){
                   info = (ViewEntityToTree.entityInfo) ent_lst.get(j);
                   if (Long.valueOf(list.get(i).toString())==(info.ent_id)){
                        ent_lst.remove(j);
                   }
                }
            }
        }
        ent_id_lst = new long[ent_lst.size()];

        for (int i=0; i<ent_lst.size(); i++) {
            ent_info = (ViewEntityToTree.entityInfo)ent_lst.elementAt(i);
            ent_id_lst[i] = ent_info.ent_id;

            node_info = new nodeInfo(ent_info.title, ent_info.ent_id, ent_info.type, "YSE", "NO", ent_info.fullPath, false);
            nodes.addElement(node_info);
        }
        notChild = ViewEntityToTree.entityNoChildAndTypeInfo(con, ent_id_lst, group_type, notIncludeChild, ftn_ext_id);

        for (int m=0;m<notChild.size(); m++) {
            String node_id = (String)notChild.elementAt(m);

            for (int i=0; i<nodes.size(); i++) {
                node_info = (nodeInfo)nodes.elementAt(i);

                if (node_id.equals((new Long(node_info.id)).toString())) {
                    node_info.has_child = "NO";

                    if(node_info.type.equals("UGR") && !cwTree.enableAppletTree){
                        node_info.is_folder = "NO";
                    }
                    if (node_info.type.equals("USR")) {
                        node_info.is_folder = "NO";
                    }
                }
            }
        }
        if(isJsonTree){
            for (int i=0; i<nodes.size(); i++) {
                node_info = (nodeInfo)nodes.elementAt(i);
                if(!(node_info.type.equals(TREE_ROOT) || node_info.type.equals(APPR_ROOT) || "IGNORE".equalsIgnoreCase(node_info.type))){
                    JsonTreeBean jNode =new JsonTreeBean();
                    nodeInfo2JsonNode(node_info, jNode);
                    jsonNodes.add(jNode);
                }
            }
            JSONArray jObj =JSONArray.fromObject(jsonNodes);
            return jObj.toString();
        }else{
            return formatVector2XML(nodes, type, type, pick_method, auto_pick, pick_leave, pick_root, complusory_tree, false, tree_subtype, ftn_ext_id);
        }
    }

    //闁跨喐鏋婚幏鐤嚄闁跨喐鏋婚幏鐑芥晸缁愭牗鎷濋幏鐑芥晸閺傘倖瀚规担鍧楁晸閻偆娅㈤幏鐑芥晸閺傘倖瀚瑰▽锟犳晸閺傘倖瀚归柨鐔鸿崱|闁跨喐鏋婚幏鐤唲闁跨喐鏋婚幏鐑芥晸閺傘倖瀚�
    public static List getTce_ent_id(Connection con,String sql,String cloum){
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List list = new ArrayList();
        try {
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()){
                list.add(rs.getInt(cloum));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            cwSQL.cleanUp(rs, stmt);
        }
        return  list;
    }
    
	
	//判断LN模式下培训中心为几级
	public static int getTcChenk(Connection con, String sql) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int count = 0;
		try {
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return count;
	}
	
    
    private String knowledgeObject2Tree(Connection con, loginProfile prof, String treeType,boolean isTcIndependent) throws SQLException, qdbException {
        boolean bShowRes = true;
        if (treeType.equalsIgnoreCase(SYLLABUS_AND_OBJECT)
        	|| treeType.equalsIgnoreCase(MOVE_OBJECTIVE)) {
            bShowRes = false;
        }

        String result;
        Vector nodes = new Vector();
        Vector lst;
        nodeInfo node_info = null;
        ViewKnowledgeObjectToTree ko = new ViewKnowledgeObjectToTree();
        ViewKnowledgeObjectToTree.objectInfo node;

        if (node_id == 0) {
            long sylId = ko.getSyllabusId(con, prof.root_ent_id);
            lst = ko.syllabusRootListAsVector(con, sylId, prof.usr_ent_id, prof.current_role, prof.my_top_tc_id, tcEnableInd,isTcIndependent);

            node_info = new nodeInfo(TREE_ROOT, 0, TREE_ROOT, "YES", "YES");
            nodes.addElement(node_info);

            Vector child_lst = ko.objectiveHasChild(con, lst, prof.usrGroupsList(), bShowRes);

            Hashtable hChildResCount = null;
            if(treeType.equalsIgnoreCase(SYLLABUS_AND_OBJECT)) {
                //if the type is SYLLABUS_AND_OBJECT,
                //get the objectives' child resource count
                Vector vObjId = new Vector();
                hChildResCount = new Hashtable();
                String[] res_types = cwUtils.splitToString(tree_subtype, "~");
                for(int i=0; i<lst.size(); i++) {
                    vObjId.addElement(new Long(((ViewKnowledgeObjectToTree.objectInfo)lst.elementAt(i)).id));
                }
                if(vObjId.size() > 0) {
                    Hashtable hCWChildResCount = dbResourceObjective.getResourceCount(con, vObjId, null, res_types, null, dbResource.RES_PRIV_CW, prof.usr_id, dbResource.RES_STATUS_ON);
                    Hashtable hAuthorChildResCount = dbResourceObjective.getResourceCount(con, vObjId, null, res_types, null, dbResource.RES_PRIV_AUTHOR, prof.usr_id, dbResource.RES_STATUS_ON);
                    for(int i=0; i<vObjId.size(); i++) {
                        Long objId = (Long) vObjId.elementAt(i);
                        Long cwChildResCount = (Long) hCWChildResCount.get(objId);
                        Long authorChildResCount = (Long) hAuthorChildResCount.get(objId);
                        Long childResCount = new Long(((cwChildResCount==null)?0:cwChildResCount.longValue()) + ((authorChildResCount==null)?0:authorChildResCount.longValue()));
                        hChildResCount.put(objId, childResCount);
                    }
                }
            }

            for (int i=0; i<lst.size(); i++) {
                node = (ViewKnowledgeObjectToTree.objectInfo)lst.elementAt(i);

                if(treeType.equalsIgnoreCase(SYLLABUS_AND_OBJECT)) {
                    //if the type is SYLLABUS_AND_OBJECT,
                    //append the child objective count to the object titles
                    Long tempNodeId = new Long(node.id);
                    Long tempChildResCount = (Long)hChildResCount.get(tempNodeId);
                    if(tempChildResCount == null) {
                        tempChildResCount = new Long(0);
                    }
                    if (!child_lst.contains(node)) {
                        node_info = new nodeInfo(node.title, node.id, node.type, "YES", "NO", tempChildResCount.longValue());
                    } else {
                        node_info = new nodeInfo(node.title, node.id, node.type, "YES", "YES", tempChildResCount.longValue());
                    }
                } else {
                    if (!child_lst.contains(node)) {
                        node_info = new nodeInfo(node.title, node.id, node.type, "YES", "NO");
                    } else {
                        node_info = new nodeInfo(node.title, node.id, node.type, "YES", "YES");
                    }
                }
                nodes.addElement(node_info);
            }
            //此处注释掉，【所有资源文件夹】无论是否LN模式，都不加载"共享目錄"
            /*if (!isTcIndependent) {
				if (treeType.equalsIgnoreCase(SYLLABUS_AND_OBJECT)) {
					lst = ko.objectiveSharedListAsVector(con, prof.root_ent_id);
					nodeInfo shared_node = null;
					if (lst.size() > 0) {
						shared_node = new nodeInfo(LangLabel.getValue(prof.cur_lan, "lab_ftn_GLB_CAT_MAIN"), -1, "SYLLABUS_AND_OBJECT", "YES", "YES");
					} else {
						shared_node = new nodeInfo(LangLabel.getValue(prof.cur_lan, "lab_ftn_GLB_CAT_MAIN"), -1, "SYLLABUS_AND_OBJECT", "YES", "NO");
					}
					// nodeInfo shared_node = new nodeInfo(LangLabel.getValue(prof.cur_lan, "lab_ftn_GLB_CAT_MAIN"), -1, "SYLLABUS_AND_OBJECT", "YES", "YES");
					nodes.addElement(shared_node);
				}
			}*/
        } else { // node_type.equals "SYB"
        	if(cwTree.enableAppletTree){
				ViewKnowledgeObjectToTree view = new ViewKnowledgeObjectToTree();
				ViewKnowledgeObjectToTree.objectInfo objInfo = view.getObjectInfo(con, node_id);
	            node_info = new nodeInfo(objInfo.title, node_id, objInfo.type, "YES", "YES");
	            nodes.addElement(node_info);
        	}
        	Vector child_lst = null;
        	if (node_id == -1) {
        	    lst = ko.objectiveSharedListAsVector(con, prof.root_ent_id);
        	    child_lst = new Vector();

        	} else {
        	    lst = ko.objectiveListAsVector(con, node_id);
        	    child_lst = ko.objectiveHasChild(con, lst, prof.usrGroupsList(), bShowRes);
        	}


            Hashtable hChildResCount = null;
            if(treeType.equalsIgnoreCase(SYLLABUS_AND_OBJECT)) {
                //if the type is SYLLABUS_AND_OBJECT,
                //get the objectives' child resource count
                Vector vObjId = new Vector();
                hChildResCount = new Hashtable();
                String[] res_types = cwUtils.splitToString(tree_subtype, "~");
                for(int i=0; i<lst.size(); i++) {
                    vObjId.addElement(new Long(((ViewKnowledgeObjectToTree.objectInfo)lst.elementAt(i)).id));
                }
                if(vObjId.size() > 0) {
                    Hashtable hCWChildResCount = dbResourceObjective.getResourceCount(con, vObjId, null, res_types, null, dbResource.RES_PRIV_CW, prof.usr_id, dbResource.RES_STATUS_ON);
                    Hashtable hAuthorChildResCount = dbResourceObjective.getResourceCount(con, vObjId, null, res_types, null, dbResource.RES_PRIV_AUTHOR, prof.usr_id, dbResource.RES_STATUS_ON);
                    for(int i=0; i<vObjId.size(); i++) {
                        Long objId = (Long) vObjId.elementAt(i);
                        Long cwChildResCount = (Long) hCWChildResCount.get(objId);
                        Long authorChildResCount = (Long) hAuthorChildResCount.get(objId);
                        Long childResCount = new Long(((cwChildResCount==null)?0:cwChildResCount.longValue()) + ((authorChildResCount==null)?0:authorChildResCount.longValue()));
                        hChildResCount.put(objId, childResCount);
                    }
                }
            }

            for (int i=0; i<lst.size(); i++) {
                node = (ViewKnowledgeObjectToTree.objectInfo)lst.elementAt(i);

                if(treeType.equalsIgnoreCase(SYLLABUS_AND_OBJECT)) {
                    //if the type is SYLLABUS_AND_OBJECT,
                    //append the child objective count to the object titles
                    Long tempNodeId = new Long(node.id);
                    Long tempChildResCount = (Long)hChildResCount.get(tempNodeId);
                    if(tempChildResCount == null) {
                        tempChildResCount = new Long(0);
                    }
                    if (!child_lst.contains(node)) {
                        node_info = new nodeInfo(node.title, node.id, node.type, "YES", "NO", tempChildResCount.longValue());
                    } else {
                        node_info = new nodeInfo(node.title, node.id, node.type, "YES", "YES", tempChildResCount.longValue());
                    }
                } else {
                    if (!child_lst.contains(node)) {
                        node_info = new nodeInfo(node.title, node.id, node.type, "YES", "NO");
                    } else {
                        node_info = new nodeInfo(node.title, node.id, node.type, "YES", "YES");
                    }
                }

                nodes.addElement(node_info);
            }

            if (bShowRes) {
                lst = ko.resourceListAsVector(con, node_id, prof.usrGroupsList());

                for (int i=0; i<lst.size(); i++) {
                    node = (ViewKnowledgeObjectToTree.objectInfo)lst.elementAt(i);
                    node_info = new nodeInfo(node.title, node.id, node.type, "NO", "NO");
                    nodes.addElement(node_info);
                }
            }
        }

        result = formatVector2XML(nodes, treeType, treeType, pick_method, auto_pick, pick_leave, pick_root, complusory_tree, false, tree_subtype, ftn_ext_id);
        result = format2TreeXML(result);

        if (node_id == 0 ||flag) {
            return formatXML(result, 1, js);
        } else {
            return result;
        }
    }

    /**
    Dedicate for km objects publish
    1. Show only readable domains
    2. Check-box will be disabled if the user is the owner of the domains
    */
    private String kmNode2Tree(Connection con, loginProfile prof, String treeType) throws SQLException {

        String result;
        Vector nodes = new Vector();
        Vector parentVec = null;
        nodeInfo node_info = null;

        String folderType = null;
        if (treeType.equalsIgnoreCase(KM_DOMAIN) || treeType.equalsIgnoreCase(KM_DOMAIN_ONLY_EDIT)) {
            folderType = DbKmFolder.FOLDER_TYPE_DOMAIN;
        }else {
            folderType = DbKmFolder.FOLDER_TYPE_WORK;
        }

        if (node_id == 0) {
            parentVec = ViewKmFolderManager.getRootFolders(con, folderType, prof.root_ent_id);
            node_info = new nodeInfo(TREE_ROOT, 0, TREE_ROOT, "YES", "YES");
            nodes.addElement(node_info);
        }else {
            parentVec = ViewKmFolderManager.getNonLinkChildFolders(con, node_id);
            node_info = new nodeInfo("IGNORE", node_id, "IGNORE", "YES", "YES");
            nodes.addElement(node_info);
        }

        Vector parentIDVec = new Vector();
        DbKmFolder fld = null;
        for (int i=0;i<parentVec.size();i++) {
            fld = (DbKmFolder) parentVec.elementAt(i);
            parentIDVec.addElement(new Long(fld.fld_nod_id));
        }
        Hashtable childHash = null;
        if( parentIDVec != null && !parentIDVec.isEmpty() )
            childHash = ViewKmFolderManager.getNonLinkChildFoldersHash(con, parentIDVec);
        else
            childHash = new Hashtable();

        Vector allVec = new Vector();
        Vector childVec = null;
        for (int i=0;i<parentVec.size();i++) {
            fld = (DbKmFolder) parentVec.elementAt(i);
            allVec.addElement(new Long(fld.fld_nod_id));

            childVec = (Vector) childHash.get(new Long(fld.fld_nod_id));
            if (childVec != null) {
                for (int j=0;j<childVec.size();j++) {
                    fld = (DbKmFolder) childVec.elementAt(j);
                    allVec.addElement(new Long(fld.fld_nod_id));
                }
            }
        }

        Vector readableVec = new Vector();
        Vector editableVec = new Vector();
        if (allVec.size() > 0) {
//            readableVec = DbKmNodeAccess.getReadableNode(con, allVec, prof.usrGroupsList());
//            editableVec = DbKmNodeAccess.getEditableNode(con, allVec, prof.usrGroupsList());
            readableVec = allVec;
            editableVec = allVec;
        }

        for (int i=0;i<parentVec.size();i++) {
            fld = (DbKmFolder) parentVec.elementAt(i);
            if (readableVec.contains(new Long(fld.fld_nod_id))) {
                String pickable = "NO";

                if (editableVec.contains(new Long(fld.fld_nod_id))) {
                    pickable = "YES";
                }

                if (pickable.equals("YES") || treeType.equalsIgnoreCase(KM_DOMAIN) || treeType.equalsIgnoreCase(KM_WORK_FOLDER)) {
                    String hasChild = "NO";

                    childVec = (Vector) childHash.get(new Long(fld.fld_nod_id));
                    if (childVec != null) {
                        for (int j=0;j<childVec.size();j++) {
                            DbKmFolder fldc = (DbKmFolder) childVec.elementAt(j);
                            if (readableVec.contains(new Long(fldc.fld_nod_id))) {
                                hasChild = "YES";
                                break;
                            }
                        }
                    }
                    node_info = new nodeInfo(fld.fld_title, fld.fld_nod_id, fld.fld_type, "YES", hasChild, pickable);
                    nodes.addElement(node_info);
                }
            }
        }

        result = formatVector2XML(nodes, treeType, treeType, pick_method, auto_pick, pick_leave, pick_root, complusory_tree, false, tree_subtype, ftn_ext_id);
        result = format2TreeXML(result);

        if (node_id == 0) {
            return formatXML(result, 1, js);
        } else {
            return result;
        }
    }




    /**
    * Generate eLibrary domain/folder and object as a treexml format
    */
    private String kmObject2Tree(Connection con, loginProfile prof, String treeType) throws SQLException {

        String result;
        Vector nodes = new Vector();
        Vector parentVec = null;
        nodeInfo node_info = null;

        String folderType = null;
        if (treeType.equalsIgnoreCase(KM_DOMAIN_AND_OBJECT)) {
            folderType = DbKmFolder.FOLDER_TYPE_DOMAIN;
        }else {
            folderType = DbKmFolder.FOLDER_TYPE_WORK;
        }


        if (node_id == 0) {
            parentVec = ViewKmFolderManager.getRootFolders(con, folderType, prof.root_ent_id);
            node_info = new nodeInfo(TREE_ROOT, 0, TREE_ROOT, "YES", "YES");
            nodes.addElement(node_info);
        }else {
            parentVec = ViewKmFolderManager.getNonLinkChildFolders(con, node_id);
            node_info = new nodeInfo("IGNORE", node_id, "IGNORE", "YES", "YES");
            nodes.addElement(node_info);
        }



        Vector parentIDVec = new Vector();
        //prevent empty list passed into ViewKmFolderManager.getNonLinkChildFoldersHash
        parentIDVec.addElement(new Long(0));
        DbKmFolder fld = null;
        DbKmObject obj = null;
        for (int i=0;i<parentVec.size();i++) {
            fld = (DbKmFolder) parentVec.elementAt(i);
            parentIDVec.addElement(new Long(fld.fld_nod_id));
        }

        //parentIDVec store all folders id in the vector
        //find all subfolders in the parentIDVec
        Hashtable childHash = null;
        if( parentIDVec != null && !parentIDVec.isEmpty() )
            childHash = ViewKmFolderManager.getNonLinkChildFoldersHash(con, parentIDVec);
        else
            childHash = new Hashtable();
        Vector allVec = new Vector();
        Vector childVec = null;
        for (int i=0;i<parentVec.size();i++) {
            fld = (DbKmFolder) parentVec.elementAt(i);
            allVec.addElement(new Long(fld.fld_nod_id));

            childVec = (Vector) childHash.get(new Long(fld.fld_nod_id));
            if (childVec != null) {
                for (int j=0;j<childVec.size();j++) {
                    fld = (DbKmFolder) childVec.elementAt(j);
                    allVec.addElement(new Long(fld.fld_nod_id));
                }
            }
        }

        Vector readableVec = new Vector();
        if (allVec.size() > 0) {
            readableVec = DbKmNodeAccess.getReadableNode(con, allVec, prof.usrGroupsList());
        }


        for (int i=0;i<parentVec.size();i++) {
            fld = (DbKmFolder) parentVec.elementAt(i);
            if (readableVec.contains(new Long(fld.fld_nod_id))) {
                String hasChild = "NO";
                if( fld.fld_obj_cnt > 0 )
                    hasChild = "YES";
                else {
                    childVec = (Vector) childHash.get(new Long(fld.fld_nod_id));
                    if (childVec != null) {
                        for (int j=0;j<childVec.size();j++) {
                            DbKmFolder fldc = (DbKmFolder) childVec.elementAt(j);
                            if (readableVec.contains(new Long(fldc.fld_nod_id))) {
                                hasChild = "YES";
                                break;
                            }
                        }
                    }
                }
                node_info = new nodeInfo(fld.fld_title, fld.target_nod_id, fld.fld_type, "YES", hasChild, "NO");
                nodes.addElement(node_info);
            }
        }

        //Get current level object
        Vector childObjVec = ViewKmFolderManager.getChildObjects(con, node_id, "DELETED");
        for(int i=0; i<childObjVec.size(); i++){
            obj = (DbKmObject)childObjVec.elementAt(i);
            node_info = new nodeInfo(obj.obj_title, obj.target_nod_id, obj.nod_type, "NO", "NO", "YES");
            nodes.addElement(node_info);
        }


        result = formatVector2XML(nodes, treeType, treeType, pick_method, auto_pick, pick_leave, pick_root, complusory_tree, false, tree_subtype, ftn_ext_id);
        result = format2TreeXML(result);
        if (node_id == 0) {
            return formatXML(result, 1, js);
        } else {
            return result;
        }
    }

	private String kmAllCatalogs2Tree(Connection con, loginProfile prof, String treeType) throws SQLException {
		String result;
		Vector nodes = new Vector();
		Vector parentVec = null;
		nodeInfo node_info = null;
		String folderType = null;
		if (treeType.equalsIgnoreCase(KM_CATALOGS)) {
			folderType = DbKmFolder.FOLDER_TYPE_DOMAIN;
		} else {
			folderType = DbKmFolder.FOLDER_TYPE_WORK;
		}

		if (node_id == 0) {
			parentVec = ViewKmFolderManager.getRootFolders(con, folderType, prof.root_ent_id);
			node_info = new nodeInfo(KM_CATALOGS, 0, KM_CATALOGS, "YES", "YES");
			nodes.addElement(node_info);
		} else {
			parentVec = ViewKmFolderManager.getNonLinkChildFolders(con, node_id);
			node_info = new nodeInfo("IGNORE", node_id, "IGNORE", "YES", "YES");
			nodes.addElement(node_info);
		}

		Vector parentIDVec = new Vector();
		parentIDVec.addElement(new Long(0));
		DbKmFolder fld = null;
		DbKmObject obj = null;
		for (int i = 0; i < parentVec.size(); i++) {
			fld = (DbKmFolder) parentVec.elementAt(i);
			parentIDVec.addElement(new Long(fld.fld_nod_id));
		}

		// parentIDVec store all folders id in the vector
		// find all subfolders in the parentIDVec
		Hashtable childHash = null;
		if (parentIDVec != null && !parentIDVec.isEmpty())
			childHash = ViewKmFolderManager.getNonLinkChildFoldersHash(con, parentIDVec);
		else
			childHash = new Hashtable();
		Vector allVec = new Vector();
		Vector childVec = null;
		for (int i = 0; i < parentVec.size(); i++) {
			fld = (DbKmFolder) parentVec.elementAt(i);
			allVec.addElement(new Long(fld.fld_nod_id));

			childVec = (Vector) childHash.get(new Long(fld.fld_nod_id));
			if (childVec != null) {
				for (int j = 0; j < childVec.size(); j++) {
					fld = (DbKmFolder) childVec.elementAt(j);
					allVec.addElement(new Long(fld.fld_nod_id));
				}
			}
		}

		Vector readableVec = new Vector();
		if (allVec.size() > 0) {
			readableVec = DbKmNodeAccess.getReadableNode(con, allVec, prof.usrGroupsList());
		}

		for (int i = 0; i < parentVec.size(); i++) {
			fld = (DbKmFolder) parentVec.elementAt(i);
			if (readableVec.contains(new Long(fld.fld_nod_id))) {
				String hasChild = "NO";
				if (fld.fld_obj_cnt > 0)
					hasChild = "YES";
				else {
					childVec = (Vector) childHash.get(new Long(fld.fld_nod_id));
					if (childVec != null) {
						for (int j = 0; j < childVec.size(); j++) {
							DbKmFolder fldc = (DbKmFolder) childVec.elementAt(j);
							if (readableVec.contains(new Long(fldc.fld_nod_id))) {
								hasChild = "YES";
								break;
							}
						}
					}
				}
				node_info = new nodeInfo(fld.fld_title, fld.target_nod_id, fld.fld_type, "YES", hasChild, "YES");
				nodes.addElement(node_info);
			}
		}
		result = formatVector2XML(nodes, treeType, treeType, pick_method, auto_pick, pick_leave, pick_root, complusory_tree, false, tree_subtype);
		result = format2TreeXML(result);
		if (node_id == 0) {
			return formatXML(result, 1, js);
		} else {
			return result;
		}
	}

	public static String formatVector2XML(Vector nodes, String tree_title, String tree_type, int pick_method, int auto_pick, int pick_leave, int pick_root, int complusory_tree,
			boolean catPublicInd, String tree_subtype) {
		StringBuffer result = new StringBuffer();
		nodeInfo node_info = (nodeInfo) nodes.elementAt(0);
		result.append("<tree>");
		result.append(cwTree.node2XML(node_info, true));

		for (int i = 1; i < nodes.size(); i++) {
			node_info = (nodeInfo) nodes.elementAt(i);
			result.append(cwTree.node2XML(node_info, false));
		}

		result.append("</tree>").append(cwUtils.NEWL).append("</tree>").append(cwUtils.NEWL);
		return result.toString();
	}

	public static String node2XML(nodeInfo info, boolean isParent) {
		StringBuffer result = new StringBuffer();
        result.append("<tree identifier=\"").append(info.id).append("\" title=\"").append(cwUtils.esc4XML(info.title))
        .append("\" type=\"").append(info.type)
        .append("\" is_folder=\"").append(info.is_folder)
        .append("\" has_child=\"").append(info.has_child)
        .append("\" pickable=\"").append(info.pickable);

		if (info.count >= 0) {
			result.append("\" count=\"").append(info.count);
		}

		if (isParent) {
			result.append("\">");
		} else {
			result.append("\"/>");
		}

		result.append(cwUtils.NEWL);

		return result.toString();
	}

    public static String formatXML (String tree_xml, int tree_count, String js_name) {
        StringBuffer result = new StringBuffer();

        result.append("<tree_info>").append(cwUtils.NEWL);
        result.append("<js>").append(js_name).append("</js>").append(cwUtils.NEWL);
        result.append("<count>").append(tree_count).append("</count>").append(cwUtils.NEWL);
        result.append("<xml>").append(cwUtils.esc4XML(tree_xml.toString())).append("</xml>").append(cwUtils.NEWL);
        result.append("</tree_info>").append(cwUtils.NEWL);
        if(cwTree.enableAppletTree){
        	return result.toString();
        }else{
        	return tree_xml;
        }
    }

    public static String formatVector2XML(Vector nodes, String tree_title, String tree_type, int pick_method, int auto_pick, int pick_leave, int pick_root, int complusory_tree, boolean catPublicInd, String tree_subtype, String ftn_ext_id) {
        if (nodes == null || nodes.size() == 0) {
            return "<null/>";
        }
        StringBuffer result = new StringBuffer();
        nodeInfo node_info = (nodeInfo)nodes.elementAt(0);
        if(cwTree.enableAppletTree){
	        result.append("<tree title=\"").append(tree_type)
	            .append("\" type=\"").append(tree_type)
	            .append("\" subtype=\"").append(cwUtils.escNull(tree_subtype))
	            .append("\" pick_method=\"").append(pick_method)
	            .append("\" auto_pick=\"").append(auto_pick)
	            .append("\" pick_leave=\"").append(pick_leave)
	            .append("\" pick_root=\"").append(pick_root)
	            .append("\" complusory_tree=\"").append(complusory_tree)
	            .append("\" cat_public_ind=\"").append(catPublicInd ? "1" : "0")
	            .append("\" ftn_ext_id=\"").append(cwUtils.escNull(ftn_ext_id))
	            .append("\">").append(cwUtils.NEWL);
        }else{
        	result.append("<tree>").append(cwUtils.NEWL);
        }

        result.append(cwTree.node2XML(node_info, true,tree_type));

        for (int i=1; i<nodes.size(); i++) {
            node_info = (nodeInfo)nodes.elementAt(i);
            result.append(cwTree.node2XML(node_info, false,tree_type));
        }
        if(cwTree.enableAppletTree){
            result.append("</node>").append(cwUtils.NEWL).append("</tree>").append(cwUtils.NEWL);
        }else{
        	result.append("</tree>").append(cwUtils.NEWL);
        }
        return result.toString();
    }

    public static String format2TreeXML(String tree_xml) {
        StringBuffer treeXML = new StringBuffer();
        if(cwTree.enableAppletTree){
        	treeXML.append(cwUtils.xmlHeader).append(cwUtils.NEWL).append("<tree_list>").append(cwUtils.NEWL).append(tree_xml).append("</tree_list>").append(cwUtils.NEWL);
        	return treeXML.toString();
        }else{
        	return tree_xml;
        }
    }

    public static String node2XML(nodeInfo info, boolean isParent,String tree_type) {
        StringBuffer result = new StringBuffer();
//        result.append("<node identifier=\"").append(info.id).append("\" title=\"").append(cwUtils.esc4XML(info.title)).append("\" type=\"").append(info.type).append("\" is_folder=\"").append(info.is_folder).append("\" has_child=\"").append(info.has_child);
        if(cwTree.enableAppletTree){
	        result.append("<node identifier=\"").append(info.id).append("\" title=\"").append(cwUtils.esc4XML(info.title))
	              .append("\" type=\"").append(info.type)
	              .append("\" fullPath=\"").append(cwUtils.esc4XML(cwUtils.escNull(info.fullPath)))
	              .append("\" is_folder=\"").append(info.is_folder)
	              .append("\" has_child=\"").append(info.has_child)
	              .append("\" pickable=\"").append(info.pickable);
			if(info.count >= 0) {
		          result.append("\" count=\"").append(info.count);
		      }

		      if (isParent) {
		          result.append("\">");
		      } else {
		          result.append("\"/>");
		      }
        }else{
        	String node_title = info.title;
        	if(!(info.type.equals(TREE_ROOT) || info.type.equals(APPR_ROOT) || info.type.equals("IGNORE"))){
        		if(info.count >= 0) {
  		          node_title += "("+info.count+")";
  		      }

        		result.append("<tree");
		        String tempScrid=new String();
		        if(info.id ==0 &&info.virtual_catalog !=null){
		        	tempScrid=info.virtual_catalog;
		        }else{
		        	tempScrid=String.valueOf(info.id);
		        }

		        result.append(" srcid=\"").append(tempScrid).append("\"")
		        		.append(" text=\"").append(cwUtils.esc4XML(node_title)).append("\" ")
		        	    .append(" fullPath=\"").append(cwUtils.esc4XML(cwUtils.escNull(info.fullPath))).append("\" ")
		        		.append(" is_folder=\"").append(info.is_folder).append("\" ")
		        		.append(" pickable=\"").append(info.pickable).append("\" ");
		        if(info.has_child.equalsIgnoreCase("YES")){
		        	result.append(" src=\"getNodeSrc('"+tree_type+"','"+tempScrid+"','"+info.node_type+"')\""+" onsetsrc=\"getNodeSrc('"+tree_type+"','"+tempScrid+"','"+info.node_type+"')\"");
		        }
		        if(tree_type.equalsIgnoreCase(NAV_TRAINING_CENTER) || tree_type.equalsIgnoreCase(NAV_TRAINING_CENTER_FOR_TA)) {
		        	result.append(" onaction=\"show_content('").append(tempScrid).append("')\"");
		        }
		        if(info.node_type != null) {
		        	result.append(" node_type=\"").append(info.node_type).append("\"");
		        }
		        result.append("/>");
        	}
        }
        result.append(cwUtils.NEWL);

        return result.toString();
    }

    public static long[] usrGroups(long usr_ent_id, Vector v) {
        long[] list = new long[v.size() + 1];
        list[0] = usr_ent_id;

        for(int i=0;i<v.size();i++) {
            list[i+1] = ((Long) v.elementAt(i)).longValue();
        }

        return list;
    }

    public String treeXML_example(int size) {
        String tree_xml;

        if (size == 4) {
            tree_xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<tree_list>\n<tree title=\"Tree 1\" type=\"1\" pick_method=\"1\">\n<node identifier=\"1\" title=\"Tree 1\" type=\"1\" is_folder=\"YES\" has_child=\"YES\">\n<node identifier=\"11\" title=\"Node 1\" type=\"1\" is_folder=\"YES\" has_child=\"YES\"/>\n<node identifier=\"12\" title=\"Node 2\" type=\"1\" is_folder=\"YES\" has_child=\"YES\"/>\n<node identifier=\"13\" title=\"Node 3\" type=\"1\" is_folder=\"YES\" has_child=\"YES\"/>\n</node>\n</tree>\n<tree title=\"Tree 2\" type=\"1\" pick_method=\"2\">\n<node identifier=\"2\" title=\"Tree 2\" type=\"1\" is_folder=\"YES\" has_child=\"YES\">\n<node identifier=\"21\" title=\"Node 1\" type=\"1\" is_folder=\"YES\" has_child=\"YES\"/>\n<node identifier=\"22\" title=\"Node 2\" type=\"1\" is_folder=\"YES\" has_child=\"YES\"/>\n<node identifier=\"23\" title=\"Node 3\" type=\"1\" is_folder=\"YES\" has_child=\"YES\"/>\n</node>\n</tree>\n<tree title=\"Tree 3\" type=\"1\" pick_method=\"3\">\n<node identifier=\"3\" title=\"Tree 3\" type=\"1\" is_folder=\"YES\" has_child=\"YES\">\n<node identifier=\"31\" title=\"Node 1\" type=\"1\" is_folder=\"YES\" has_child=\"YES\"/>\n<node identifier=\"32\" title=\"Node 2\" type=\"1\" is_folder=\"YES\" has_child=\"YES\"/>\n<node identifier=\"33\" title=\"Node 3\" type=\"1\" is_folder=\"YES\" has_child=\"NO\"/>\n</node>\n</tree>\n<tree title=\"Tree 4\" type=\"1\" pick_method=\"4\">\n<node identifier=\"1\" title=\"Tree 4\" type=\"1\" is_folder=\"YES\" has_child=\"YES\">\n<node identifier=\"11\" title=\"Node 1\" type=\"1\" is_folder=\"YES\" has_child=\"YES\"/>\n<node identifier=\"12\" title=\"Node 2\" type=\"1\" is_folder=\"YES\" has_child=\"YES\"/>\n<node identifier=\"13\" title=\"Node 3\" type=\"1\" is_folder=\"NO\" has_child=\"NO\"/>\n</node>\n</tree>\n</tree_list>";
            size = 4;
        } else if (size == 2) {
            tree_xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<tree_list>\n<tree title=\"Tree 1\" type=\"1\" pick_method=\"1\">\n<node identifier=\"1\" title=\"Tree 1\" type=\"1\" is_folder=\"YES\"  has_child=\"YES\">\n<node identifier=\"11\" title=\"Node 1\" type=\"1\" is_folder=\"YES\" has_child=\"YES\"/>\n<node identifier=\"12\" title=\"Node 2\" type=\"1\" is_folder=\"YES\"  has_child=\"YES\"/>\n<node identifier=\"13\" title=\"Node 3\" type=\"1\" is_folder=\"YES\" has_child=\"YES\"/>\n</node>\n</tree>\n<tree title=\"Tree 2\" type=\"1\" pick_method=\"2\">\n<node identifier=\"2\" title=\"Tree 2\" type=\"1\" is_folder=\"YES\" has_child=\"YES\">\n<node identifier=\"21\" title=\"Node 1\" type=\"1\" is_folder=\"YES\" has_child=\"YES\"/>\n<node identifier=\"22\" title=\"Node 2\" type=\"1\" is_folder=\"YES\" has_child=\"YES\"/>\n<node identifier=\"23\" title=\"Node 3\" type=\"1\" is_folder=\"YES\" has_child=\"YES\"/>\n</node>\n</tree>\n</tree_list>";
            size = 2;
        } else {
            tree_xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<tree_list>\n<tree title=\"Tree 1\" type=\"1\" pick_method=\"1\">\n<node identifier=\"1\" title=\"Tree 1\" type=\"1\" is_folder=\"YES\" has_child=\"YES\">\n<node identifier=\"11\" title=\"Node 1\" type=\"1\" is_folder=\"YES\" has_child=\"YES\"/>\n<node identifier=\"12\" title=\"Node 2\" type=\"1\" is_folder=\"YES\" has_child=\"YES\"/>\n<node identifier=\"13\" title=\"Node 3\" type=\"1\" is_folder=\"YES\" has_child=\"YES\"/>\n</node>\n</tree>\n</tree_list>";
            size = 1;
        }

/*        if(cwUtils.perl.match("#\n#i", tree_xml)) {
            tree_xml = cwUtils.perl.substitute("s#\n#:_:_:#ig", tree_xml);
        }*/

        tree_xml = cwUtils.esc4XML(tree_xml);

        StringBuffer result = new StringBuffer();
        result.append("<tree_info>").append(cwUtils.NEWL);
        result.append("<count>").append(size).append("</count>").append(cwUtils.NEWL);
        result.append("<xml>").append(tree_xml).append("</xml>").append(cwUtils.NEWL);
        result.append("</tree_info>").append(cwUtils.NEWL);

        return result.toString();
    }

    private String trainingCenter2Tree(Connection con, loginProfile prof, String tree_type) throws SQLException, qdbException{
        String result = "";
        result = tc2Tree(con, prof, tree_type);
        result = format2TreeXML(result);
        if (node_id == 0 || flag) {
            return formatXML(result, 1, js);
        } else {
            return result;
        }
    }

    private String tc2Tree(Connection con, loginProfile prof, String type) throws SQLException {
    	nodeInfo node_info = null;
    	Vector nodes = new Vector();
    	ViewEntityToTree entity2Tree = new ViewEntityToTree();
    	ViewEntityToTree.entityInfo ent_info;
    	Vector tc_lst = new Vector();

    	if(node_id == 0) {

    		boolean isLrn = AccessControlWZB.isLrnRole(prof.current_role);
    		boolean isInstr = AccessControlWZB.isIstRole(prof.current_role);
    		//boolean hasPrivilege = hasPrivilege = AccessControlWZB.hasRolePrivilege(prof.current_role,AclFunction.FTN_AMD_TC_MAIN);;
    		
    		if( ViewTrainingCenter.isSuperTA(con, prof.root_ent_id, prof.usr_ent_id, prof.current_role)||
       		    (!AccessControlWZB.isRoleTcInd(prof.current_role))
               	|| isLrn ) {
    			tc_lst = entity2Tree.getSuperTcInfo(con, prof, isLrn, isInstr, false, false);
    		} else if(type.equalsIgnoreCase(TRAINING_CENTER_NOACL)) {
   				tc_lst = entity2Tree.getTopTcInfo(con, prof, false, false,0, type.equalsIgnoreCase(TRAINING_CENTER_NOACL));
    		} else {
    		    tc_lst = entity2Tree.getMyTcInfo(con, prof, false, false,0);
    		}
    		if(tc_lst != null && tc_lst.size() > 0) {
    			node_info = new nodeInfo(TREE_ROOT, 0, TREE_ROOT, "YES", "YES");
    		} else {
    			node_info = new nodeInfo(TREE_ROOT, 0, TREE_ROOT, "YES", "NO");
    		}
    		nodes.addElement(node_info);
            for (int i=0; i<tc_lst.size(); i++) {
                ent_info = (ViewEntityToTree.entityInfo)tc_lst.elementAt(i);
                node_info = new nodeInfo(ent_info.title, ent_info.ent_id, ent_info.type, "YES", ent_info.has_child);
                nodes.addElement(node_info);
            }
    	} else {
            node_info = new nodeInfo("IGNORE", node_id, "IGNORE", "YES", "NO");
            nodes.addElement(node_info);
    		tc_lst = entity2Tree.getSubTcInfo(con, node_id, prof, false);
    	
    		// 如果是LN模式
    		if(qdbAction.wizbini.cfgSysSetupadv.isTcIndependent()) {
    			String sql ="select DISTINCT eip_tcr_id from  EnterpriseInfoPortal";
        		List list = getTce_ent_id(con,sql,"eip_tcr_id");
        		
                ViewEntityToTree.entityInfo  info ;
                if (list.size()>0){
                    for (int i =0 ;i<list.size(); i++){
                        for (int j=0;j<tc_lst.size();j++){
                            info = (ViewEntityToTree.entityInfo)tc_lst.get(j);
                            if (Long.valueOf(list.get(i).toString())==(info.ent_id)){
                                tc_lst.remove(j);
                            }
                        }
                    }
                }
                
    		}
    		
            for (int i=0; i<tc_lst.size(); i++) {
                ent_info = (ViewEntityToTree.entityInfo)tc_lst.elementAt(i);
                node_info = new nodeInfo(ent_info.title, ent_info.ent_id, ent_info.type, "YES", ent_info.has_child);
                nodes.addElement(node_info);
            }
    	}



        return formatVector2XML(nodes, type, type, pick_method, auto_pick, pick_leave, pick_root, complusory_tree, false, tree_subtype, ftn_ext_id);
    }

    public String genNavTrainingCenterTree(Connection con, loginProfile prof, boolean isTcForTaInAnno) throws SQLException, cwSysMessage, cwException {
    	if(isTcForTaInAnno) {
    		this.tree_type = NAV_TRAINING_CENTER_FOR_TA.toLowerCase();
    	} else {
    		this.tree_type = NAV_TRAINING_CENTER.toLowerCase();
    	}
    	this.pick_root = 1;
    	this.pick_method = PICK_ANY_SINGLE;
    	return create_treeXML(con, prof);
    }

    private String tcItemCatalog2Tree(Connection con, loginProfile prof, String tree_type, long sgp_id) throws SQLException {
        String result = "";

        result = tcItemCatalog2TreeXML(con, prof, tree_type, sgp_id);

        if(isJsonTree){
            return result;
        } else {
            result = format2TreeXML(result);
            if (node_id == 0 || flag) {
                return formatXML(result, 1, js);
            } else {
                return result;
            }
        }
    }

    public String tcItemCatalog2TreeXML(Connection con, loginProfile prof, String type, long sgp_id) throws SQLException {
    	nodeInfo node_info = null;
    	Vector nodes = new Vector();
    	Vector jsonNodes = new Vector();
    	ViewEntityToTree entity2Tree = new ViewEntityToTree();
    	ViewEntityToTree.entityInfo ent_info;
    	Vector tree_node_lst = new Vector();
    	if(node_id == 0) {
    		/*
    		if(itm_id > 0) {
    			tree_node_lst = entity2Tree.getTopTcByItem(con, prof,type);
    		} else {
    		*/
//    			AcTrainingCenter actc = new AcTrainingCenter(con);
//    			AccessControlWZB acl = new AccessControlWZB();
    			if(AccessControlWZB.isLrnRole(prof.current_role)) {
    				tree_node_lst = entity2Tree.getSuperTcInfo(con, prof, true, false, true, false);
    			}else {
    				tree_node_lst = entity2Tree.getTopTcInfo(con, prof, true, false,sgp_id, false);
    			}
    		/*}*/
    		if(tree_node_lst != null && tree_node_lst.size() > 0) {
    			node_info = new nodeInfo(TREE_ROOT, 0, TREE_ROOT, "YES", "YES");
    		} else {
    			node_info = new nodeInfo(TREE_ROOT, 0, TREE_ROOT, "YES", "NO");
    		}
    		nodes.addElement(node_info);
    	} else if(node_type != null && node_type.equalsIgnoreCase(NODE_TYPE_TC)){
            node_info = new nodeInfo("IGNORE", node_id, "IGNORE", "YES", "YES");
            nodes.addElement(node_info);
            if(tree_type.equalsIgnoreCase(TC_CATALOG_ITEM)||tree_type.equalsIgnoreCase(TC_CATALOG_ITEM_BATCHUPDATE)|| tree_type.equalsIgnoreCase(TC_CATALOG_ITEM_SELF) || tree_type.equalsIgnoreCase(TC_CATALOG_ITEM_INTEGRATED)) {
            	tree_node_lst = entity2Tree.getSubTcAndCatalogInfo(con, node_id, prof, false, true, itm_id);
            } else if(tree_type.equalsIgnoreCase(TC_CATALOG_ITEM_RUN) || tree_type.equalsIgnoreCase(TC_CATALOG_ITEM_AND_RUN)) {
            	tree_node_lst = entity2Tree.getSubTcAndCatalogInfo(con, node_id, prof, false, true, 0);
            } else if(tree_type.equalsIgnoreCase(TC_CATALOG)) {
            	tree_node_lst = entity2Tree.getSubTcAndCatalogInfo(con, node_id, prof, false, false, 0);
            }
    	} else if(node_type == null || node_type.equalsIgnoreCase(NODE_TYPE_CATALOG)) {//node_type == null: the node is under a catalog but its not a catalog
    		node_info = new nodeInfo("IGNORE", node_id, "IGNORE", "YES", "YES");
    		nodes.addElement(node_info);
    		if(tree_type.equalsIgnoreCase(TC_CATALOG_ITEM)) {
    			boolean isIntgItem = aeItem.isIntegratedItem(con, itm_id);
    			tree_node_lst = entity2Tree.getChildAndItemNode(con, node_id, prof, true, isIntgItem, false);
    		} else if (tree_type.equalsIgnoreCase(TC_CATALOG_ITEM_SELF)) {
				tree_node_lst = entity2Tree.getChildAndItemNode(con, node_id, prof, true, false, false, false, new String[]{aeItem.ITM_TYPE_SELFSTUDY, aeItem.ITM_TYPE_AUDIOVIDEO});
			} else if(tree_type.equalsIgnoreCase(TC_CATALOG_ITEM_RUN)) {
    			tree_node_lst = entity2Tree.getChildAndItemNode(con, node_id, prof, true, true, false, false, false);
    		} else if(tree_type.equalsIgnoreCase(TC_CATALOG_ITEM_AND_RUN)) {
    			tree_node_lst = entity2Tree.getChildAndItemNode(con, node_id, prof, true, true, true, false, false);
    		} else if(tree_type.equalsIgnoreCase(TC_CATALOG)) {
    			tree_node_lst = entity2Tree.getChildAndItemNode(con, node_id, prof, false, false, false);
    		} else if(tree_type.equalsIgnoreCase(TC_CATALOG_ITEM_INTEGRATED)){
    			boolean isIntgItem = aeItem.isIntegratedItem(con, itm_id);
    			tree_node_lst = entity2Tree.getChildAndItemNode(con, node_id, prof, true, isIntgItem, true);
    		}else if(tree_type.equalsIgnoreCase(TC_CATALOG_ITEM_BATCHUPDATE)){
    			boolean isIntgItem = aeItem.isIntegratedItem(con, itm_id);
    			tree_node_lst = entity2Tree.getChildAndItemNode(con, node_id, prof, true, isIntgItem, false, false, new String[]{aeItem.ITM_TYPE_SELFSTUDY});
    		}
    	}

    	for (int i=0; i<tree_node_lst.size(); i++) {
    		ent_info = (ViewEntityToTree.entityInfo)tree_node_lst.elementAt(i);
    		node_info = new nodeInfo(ent_info.title, ent_info.ent_id, ent_info.type, "YES", ent_info.has_child, "YES", ent_info.node_type);
    		if(ent_info.type.equalsIgnoreCase(ITEM)) {
    			node_info.is_folder = "NO";
    		}
    		if(ent_info.node_type != null && ent_info.node_type.equalsIgnoreCase(NODE_TYPE_TC)) {
    			node_info.pickable = "NO";
    		}
    		nodes.addElement(node_info);
    	}

    	if (zTree) { // for new ztree
			for (int i = 0; i < nodes.size(); i++) {
				node_info = (nodeInfo) nodes.elementAt(i);
				if (!(node_info.type.equals(TREE_ROOT) || node_info.type.equals(APPR_ROOT) || "IGNORE".equalsIgnoreCase(node_info.type))) {
					TreeNode treeNode = TreeUtils.nodeInfoToTreeNode(node_info);
					jsonNodes.add(treeNode);
				}
			}
			JSONArray jObj = JSONArray.fromObject(jsonNodes);
			return jObj.toString();
    	} else if (isJsonTree) {
			for (int i = 0; i < nodes.size(); i++) {
				node_info = (nodeInfo) nodes.elementAt(i);
				if (!(node_info.type.equals(TREE_ROOT) || node_info.type.equals(APPR_ROOT) || "IGNORE".equalsIgnoreCase(node_info.type))) {
					JsonTreeBean jNode = new JsonTreeBean();
					nodeInfo2JsonNode(node_info, jNode);
					jsonNodes.add(jNode);
				}
			}
			JSONArray jObj = JSONArray.fromObject(jsonNodes);
			return jObj.toString();
		} else {
			return formatVector2XML(nodes, type, type, pick_method, auto_pick, pick_leave, pick_root, complusory_tree, false, tree_subtype, ftn_ext_id);
		}
    }

    private String tcSyllabusObj2Tree(Connection con, loginProfile prof, String tree_type) throws SQLException, qdbException {
        String result = "";

        result = tcSyllabusObj2TreeXML(con, prof, tree_type);

        result = format2TreeXML(result);
        if (node_id == 0 || flag) {
            return formatXML(result, 1, js);
        } else {
            return result;
        }

    }

    private String tcSyllabusObj2TreeXML(Connection con, loginProfile prof, String type) throws SQLException, qdbException {
    	nodeInfo node_info = null;
    	Vector nodes = new Vector();
    	ViewEntityToTree entity2Tree = new ViewEntityToTree();
    	ViewEntityToTree.entityInfo ent_info;
    	Vector tree_node_lst = new Vector();
    	Hashtable hChildResCount = null;
    	ViewKnowledgeObjectToTree ko = new ViewKnowledgeObjectToTree();
    	long sylId = ko.getSyllabusId(con, prof.root_ent_id);
    	if(node_id == 0) {
   			tree_node_lst = entity2Tree.getTopTcInfo(con, prof, false, true,0, false);
    		if(tree_node_lst != null && tree_node_lst.size() > 0) {
    			node_info = new nodeInfo(TREE_ROOT, 0, TREE_ROOT, "YES", "YES");
    		} else {
    			node_info = new nodeInfo(TREE_ROOT, 0, TREE_ROOT, "YES", "NO");
    		}
    		nodes.addElement(node_info);
    	} else if(node_type != null && node_type.equalsIgnoreCase(NODE_TYPE_TC)){
    		node_info = new nodeInfo("IGNORE", node_id, "IGNORE", "YES", "YES");
    		nodes.addElement(node_info);
    		tree_node_lst = entity2Tree.getSubTcAndObjectiveInfo(con, sylId, node_id, prof);

            if(type.equalsIgnoreCase(TC_SYLLABUS_AND_OBJECT)) {
                //if the type is SYLLABUS_AND_OBJECT,
                //get the objectives' child resource count
                Vector vObjId = new Vector();
                hChildResCount = new Hashtable();
                String[] res_types = cwUtils.splitToString(tree_subtype, "~");
                for(int i=0; i<tree_node_lst.size(); i++) {
                	ViewEntityToTree.entityInfo entity = (ViewEntityToTree.entityInfo)tree_node_lst.elementAt(i);
                	if(entity.node_type != null && entity.node_type.equalsIgnoreCase(NODE_TYPE_OBJECTIVE)) {
                		vObjId.addElement(new Long(entity.ent_id));
                	}
                }
                if(vObjId.size() > 0) {
                    Hashtable hCWChildResCount = dbResourceObjective.getResourceCount(con, vObjId, null, res_types, null, dbResource.RES_PRIV_CW, prof.usr_id, dbResource.RES_STATUS_ON);
                    Hashtable hAuthorChildResCount = dbResourceObjective.getResourceCount(con, vObjId, null, res_types, null, dbResource.RES_PRIV_AUTHOR, prof.usr_id, dbResource.RES_STATUS_ON);
                    for(int i=0; i<vObjId.size(); i++) {
                        Long objId = (Long) vObjId.elementAt(i);
                        Long cwChildResCount = (Long) hCWChildResCount.get(objId);
                        Long authorChildResCount = (Long) hAuthorChildResCount.get(objId);
                        Long childResCount = new Long(((cwChildResCount==null)?0:cwChildResCount.longValue()) + ((authorChildResCount==null)?0:authorChildResCount.longValue()));
                        hChildResCount.put(objId, childResCount);
                    }
                }
            }
    	} else if(node_type.equalsIgnoreCase(NODE_TYPE_OBJECTIVE)) {
    		node_info = new nodeInfo("IGNORE", node_id, "IGNORE", "YES", "YES");
    		nodes.addElement(node_info);
    		tree_node_lst = entity2Tree.getChildObjectiveNode(con, sylId, node_id, prof.root_ent_id);
            if(type.equalsIgnoreCase(TC_SYLLABUS_AND_OBJECT)) {
                //if the type is SYLLABUS_AND_OBJECT,
                //get the objectives' child resource count
                Vector vObjId = new Vector();
                hChildResCount = new Hashtable();
                String[] res_types = cwUtils.splitToString(tree_subtype, "~");
                for(int i=0; i<tree_node_lst.size(); i++) {
                	ViewEntityToTree.entityInfo entity = (ViewEntityToTree.entityInfo)tree_node_lst.elementAt(i);
                	if(entity.node_type != null && entity.node_type.equalsIgnoreCase(NODE_TYPE_OBJECTIVE)) {
                		vObjId.addElement(new Long(entity.ent_id));
                	}
                }
                if(vObjId.size() > 0) {
                    Hashtable hCWChildResCount = dbResourceObjective.getResourceCount(con, vObjId, null, res_types, null, dbResource.RES_PRIV_CW, prof.usr_id, dbResource.RES_STATUS_ON);
                    Hashtable hAuthorChildResCount = dbResourceObjective.getResourceCount(con, vObjId, null, res_types, null, dbResource.RES_PRIV_AUTHOR, prof.usr_id, dbResource.RES_STATUS_ON);
                    for(int i=0; i<vObjId.size(); i++) {
                        Long objId = (Long) vObjId.elementAt(i);
                        Long cwChildResCount = (Long) hCWChildResCount.get(objId);
                        Long authorChildResCount = (Long) hAuthorChildResCount.get(objId);
                        Long childResCount = new Long(((cwChildResCount==null)?0:cwChildResCount.longValue()) + ((authorChildResCount==null)?0:authorChildResCount.longValue()));
                        hChildResCount.put(objId, childResCount);
                    }
                }
            }
    	}
        for (int i=0; i<tree_node_lst.size(); i++) {
        	ent_info = (ViewEntityToTree.entityInfo)tree_node_lst.elementAt(i);
        	if(type.equalsIgnoreCase(TC_SYLLABUS_AND_OBJECT)) {
        		if(!ent_info.node_type.equalsIgnoreCase(NODE_TYPE_OBJECTIVE)) {
            		node_info = new nodeInfo(ent_info.title, ent_info.ent_id, ent_info.type, "YES", ent_info.has_child, "YES", ent_info.node_type);
        		} else {
        			//if the type is SYLLABUS_AND_OBJECT,
        			//append the child objective count to the object titles
        			Long tempNodeId = new Long(ent_info.ent_id);
        			Long tempChildResCount = (Long)hChildResCount.get(tempNodeId);
        			if(tempChildResCount == null) {
        				tempChildResCount = new Long(0);
        			}
        			node_info = new nodeInfo(ent_info.title, ent_info.ent_id, ent_info.type, "YES", ent_info.has_child, tempChildResCount.longValue(), ent_info.node_type);
        		}
        		if(ent_info.node_type != null && ent_info.node_type.equalsIgnoreCase(NODE_TYPE_TC)) {
        			node_info.pickable = "NO";
        		}
            }
            nodes.addElement(node_info);
        }
        return formatVector2XML(nodes, type, type, pick_method, auto_pick, pick_leave, pick_root, complusory_tree, false, tree_subtype, ftn_ext_id);
    }

    private String trainingCenter4Ta2Tree(Connection con, loginProfile prof, String tree_type) throws SQLException, qdbException{
        String result = "";
        result = tc4Ta2Tree(con, prof, tree_type);
        result = format2TreeXML(result);
        if (node_id == 0 || flag) {
            return formatXML(result, 1, js);
        } else {
            return result;
        }
    }

    private String tc4Ta2Tree(Connection con, loginProfile prof, String type) throws SQLException {
    	nodeInfo node_info = null;
    	Vector nodes = new Vector();
    	ViewEntityToTree entity2Tree = new ViewEntityToTree();
    	ViewEntityToTree.entityInfo ent_info;
    	Vector tc_lst = new Vector();

    	if(node_id == 0) {
    		AcTrainingCenter actc = new AcTrainingCenter(con);
    		AccessControlWZB acl = new AccessControlWZB();
    		boolean isLrn = AccessControlWZB.isLrnRole(prof.current_role);
    		boolean isInstr = AccessControlWZB.isIstRole(prof.current_role);
    		if(AccessControlWZB.hasRolePrivilege(prof.current_role, AclFunction.FTN_AMD_TC_MAIN)) {
    			tc_lst = entity2Tree.getSuperTcInfo(con, prof, isLrn, isInstr, false, true);
    		}
    		if(tc_lst != null && tc_lst.size() > 0) {
    			node_info = new nodeInfo(TREE_ROOT, 0, TREE_ROOT, "YES", "YES");
    		} else {
    			node_info = new nodeInfo(TREE_ROOT, 0, TREE_ROOT, "YES", "NO");
    		}
    		nodes.addElement(node_info);
    	} else {
            node_info = new nodeInfo("IGNORE", node_id, "IGNORE", "YES", "YES");
            nodes.addElement(node_info);
    		tc_lst = entity2Tree.getSubTcInfo(con, node_id, prof, true);
    	}

        for (int i=0; i<tc_lst.size(); i++) {
            ent_info = (ViewEntityToTree.entityInfo)tc_lst.elementAt(i);
            node_info = new nodeInfo(ent_info.title, ent_info.ent_id, ent_info.type, "YES", ent_info.has_child);
            nodes.addElement(node_info);
        }

        return formatVector2XML(nodes, type, type, pick_method, auto_pick, pick_leave, pick_root, complusory_tree, false, tree_subtype, ftn_ext_id);
    }

    private String skillAndSkp2Tree(Connection con, loginProfile prof, String tree_type) throws SQLException, qdbException{
    	  String result = "";
          result = skillSkp2Tree(con, prof, tree_type);
          result = format2TreeXML(result);

          if (node_id == 0 || flag) {
              return formatXML(result, 1, js);
          } else {
              return result;
          }
    }

   private String skillSkp2Tree(Connection con, loginProfile prof, String type)
			throws SQLException {
		nodeInfo node_info = null;
		Vector nodes = new Vector();
		Vector jsonNodes = new Vector();
		ViewEntityToTree entity2Tree = new ViewEntityToTree();
		ViewEntityToTree.entityInfo ent_info;
		Vector setVc = new Vector();

		if (node_id == 0 && (virtual_catalog == null || virtual_catalog.length() == 0)) {
			node_info = new nodeInfo(TREE_ROOT, 0, TREE_ROOT, "YES", "YES", "NO");
			nodes.addElement(node_info);
			String lab_set = "";
			String lab_group = "";
			lab_set = LangLabel.getValue(prof.cur_lan, "lab_skillSet");
			lab_group = LangLabel.getValue(prof.cur_lan, "lab_skillGroup");
			setVc = entity2Tree.getAllSkillSet(con);
			String hasSkpChild="YES";
			if(setVc ==null || setVc.isEmpty()){
				hasSkpChild="NO";
			}
			node_info = new nodeInfo(lab_set, NODE_TYPE_SKP, type, "YES",hasSkpChild, "NO");
			nodes.addElement(node_info);
			setVc = entity2Tree.getAllSkillGroup(con);
			String hasCspChild="YES";
			if(setVc ==null || setVc.isEmpty()){
				hasCspChild="NO";
			}
			node_info = new nodeInfo(lab_group, NODE_TYPE_CSP, type, "YES", hasCspChild, "NO");
			nodes.addElement(node_info);
			
//			if (appendUsrSkills == true) {
//				
//				node_info = new nodeInfo(lab_usr_all_skills, NODE_TYPE_USR_SKILLS, type, "YES", "YES", "NO");
//				nodes.addElement(node_info);
//			}
		} else if (node_id == 0 && virtual_catalog != null && virtual_catalog.length() > 0) {
			if (NODE_TYPE_SKP.equalsIgnoreCase(virtual_catalog)) { 
				for (int i = 0; i < setVc.size(); i++) {
					ent_info = (ViewEntityToTree.entityInfo) setVc.elementAt(i);
					node_info = new nodeInfo(ent_info.title, ent_info.ent_id, ent_info.type, "YES", ent_info.has_child);
					nodes.addElement(node_info);
				}
			} else if (NODE_TYPE_CSP.equalsIgnoreCase(virtual_catalog)) {
				for (int i = 0; i < setVc.size(); i++) {
					ent_info = (ViewEntityToTree.entityInfo) setVc.elementAt(i);
					node_info = new nodeInfo(ent_info.title, ent_info.ent_id, ent_info.type, "NO", "NO");
					nodes.addElement(node_info);
				}
//			} else if (NODE_TYPE_USR_SKILLS.equalsIgnoreCase(virtual_catalog)){	
//				// lab_usr_all_skills
//				// NODE_TYPE_USR_SKILLS
			}
		} else {
			setVc = entity2Tree.getSkillBySet(con, node_id);
			for (int i = 0; i < setVc.size(); i++) {
				ent_info = (ViewEntityToTree.entityInfo) setVc.elementAt(i);
				node_info = new nodeInfo(ent_info.title, ent_info.ent_id, ent_info.type, "NO", "NO");
				nodes.addElement(node_info);
			}
		}

		if (isJsonTree) {
			for (int i = 0; i < nodes.size(); i++) {
				node_info = (nodeInfo) nodes.elementAt(i);
				if (!(node_info.type.equals(TREE_ROOT) || node_info.type.equals(APPR_ROOT) || "IGNORE".equalsIgnoreCase(node_info.type))) {
					JsonTreeBean jNode = new JsonTreeBean();
					nodeInfo2JsonNode(node_info, jNode);
					jsonNodes.add(jNode);
				}
			}
			JSONArray jObj = JSONArray.fromObject(jsonNodes);
			return jObj.toString();
		} else {
			return formatVector2XML(nodes, type, type, pick_method, auto_pick, pick_leave, pick_root, complusory_tree, false, tree_subtype, ftn_ext_id);
		}

	}

    private void nodeInfo2JsonNode(nodeInfo node, JsonTreeBean jNode){
    	jNode.setId(node.id);
    	jNode.setText(node.title);
    	if (cwTree.NORMAL.equalsIgnoreCase(node.type)) {
    		jNode.setType(cwTree.CATALOG);
    	}
    	
//    	if (node_type == null && cwTree.COMPETENCE_PROFILE_AND_SKILL.equalsIgnoreCase(tree_type)) {	// 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔告灮閹风兘鏁撻弬銈嗗闁跨喐鏋婚幏鐑芥晸娓氥儲鏋婚幏鐑芥晸閿燂拷//    		jNode.setType(node.virtual_catalog);
//    	} else {
//    		jNode.setType(node.node_type);
//    	}
    }

}