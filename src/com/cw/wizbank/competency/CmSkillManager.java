package com.cw.wizbank.competency;

import java.sql.*;
import java.util.*;

import javax.servlet.http.HttpSession;

import com.cw.wizbank.util.*;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.db.*;
import com.cw.wizbank.db.view.ViewCmSkill;
import com.cw.wizbank.db.view.ViewCmAssessment;
import com.cw.wizbank.db.view.ViewCmSkillCoverage;
import com.cw.wizbank.ae.db.DbItemCompetency;
import com.cwn.wizbank.utils.CommonLog;

public class CmSkillManager {

    //public DbCmSkillTreeNode    node = null;
    public DbCmSkill            skill = null;
    public DbCmSkillSet         skillSet = null;
    public DbCmSkillScale       skillScale = null;
    public Vector               skillLevelVec = null;
    
    private static final String SKILL_PROFILE       =   "SKP";
    private static final String SKILL_TEMPLATE      =   "SKT";
    private static final String ASSESSMENT_RESULT   =   "ASR";

    private static final String HASH_SKL_ID_VEC     =   "skl_id_vector";
    private static final String HASH_SSL_ID_VEC     =   "scale_id_vector";
    private static final String HASH_TIMESTAMP      =   "page_timestamp";
    private static final String HASH_SORTBY         =   "sort_by";
    private static final String HASH_ORDERBY        =   "order_by";
    private static final String SESS_SKILL_SEARCH   =   "Competency_SKILL_SEARCH";
    public CmSkillManager() {;}


	private static final String SESS_CM_PAGINATION		=	"SESS_CM_PAGINATION";
	private static final String SESS_CM_SKB_ID_VECTOR	=	"SESS_CM_SKB_ID_VECTOR";

    /**
    Get the content of a composite skill 
    @skl_skb_id if it is less than or equal to zero, means get the top level of the tree
    */
    public String getCompSkillContentAsXML(Connection con, long skl_skb_id, 
                                           int curPage, int pageSize, 
                                           HttpSession sess, Timestamp in_timestamp) 
        throws SQLException, cwSysMessage, cwException {
        
        this.skill = new DbCmSkill();
        this.skill.skl_skb_id = skl_skb_id;
        this.skill.skb_id = skl_skb_id;
        StringBuffer xmlBuf = new StringBuffer(1024);
        //this.skill.skl_skb_id <= 0 means want to view the top level of the tree
        if(this.skill.skl_skb_id > 0) {
            this.skill.get(con);
            if(this.skill.skl_type == null 
                || !this.skill.skl_type.equals(DbCmSkillBase.COMPETENCY_COMPOSITE_SKILL)) {
                throw new SQLException("Composite skill not found: skl_skb_id = " + this.skill.skl_skb_id);
            }
        }
        xmlBuf.append(getSkillAsXML(false, this.skill.getAncestorTitle(con)));
        if(this.skill.skb_ssl_id > 0) {
            xmlBuf.append(getSkillScaleAsXML(con))
                  .append(cwUtils.NEWL);
        }
        xmlBuf.append(getSkillChildAsXML(con, curPage, pageSize,sess, in_timestamp));
        xmlBuf.append("</skill>");
        return xmlBuf.toString();
    }

    /**
    Get XML containing <skill_reference_data> and <skill> (skill details)
    @param scale_id_list scales inside this list will be shown into details with their levels
    @return return XML is not well-formed
    */
    public String getSkill4UpdateAsXML(Connection con, long skl_skb_id, long[] scale_id_list) 
        throws SQLException, cwSysMessage, cwException {
        this.skill = new DbCmSkill();
        this.skill.skl_skb_id = skl_skb_id;
        this.skill.skb_id = skl_skb_id;
        StringBuffer xmlBuf = new StringBuffer(1024);
        this.skill.get(con);
        xmlBuf.append(getSkillReferenceDataAsXML(con, this.skill.skb_owner_ent_id, scale_id_list));
        xmlBuf.append(getSkillAsXML(false, null));
        if(this.skill.skb_ssl_id > 0) {
            xmlBuf.append(getSkillScaleAsXML(con))
                  .append(cwUtils.NEWL);
        }
        xmlBuf.append("</skill>");
        return xmlBuf.toString();
    }

    /**
    Vendor this.skill into XML
    Need to be initialize contents of this.skill before calling this method
    @closeInd Indicate if need to append a close tag </skill> to the return XML
    */
    private String getSkillAsXML(boolean closeInd, Hashtable htAncestor) {
        StringBuffer xmlBuf = new StringBuffer(1024);
        xmlBuf.append("<skill id=\"").append(cwUtils.escZero(this.skill.skl_skb_id)).append("\"")
              .append(" type=\"").append(cwUtils.escNull(this.skill.skl_type)).append("\"")
              .append(" parent_id=\"").append(cwUtils.escZero(this.skill.skb_parent_skb_id)).append("\"")
              .append(" derive_rule=\"").append(cwUtils.escNull(this.skill.skl_derive_rule)).append("\"")
              .append(">").append(cwUtils.NEWL);
        xmlBuf.append("<title>").append(cwUtils.esc4XML(cwUtils.escNull(this.skill.skb_title))).append("</title>")
              .append(cwUtils.NEWL);
        xmlBuf.append("<desc>").append(cwUtils.esc4XML(cwUtils.escNull(this.skill.skb_description))).append("</desc>")
              .append(cwUtils.NEWL);
        xmlBuf.append("<create usr_id=\"").append(cwUtils.escNull(this.skill.skb_create_usr_id)).append("\"")
              .append(" timestamp=\"").append(cwUtils.escNull(this.skill.skb_create_timestamp)).append("\"/>")
              .append(cwUtils.NEWL);
        xmlBuf.append("<update usr_id=\"").append(cwUtils.escNull(this.skill.skb_update_usr_id)).append("\"")
              .append(" timestamp=\"").append(cwUtils.escNull(this.skill.skb_update_timestamp)).append("\"/>")
              .append(cwUtils.NEWL);
              
        if (htAncestor!=null){
            Long l_ancestor;
            String ancestor_title;
            xmlBuf.append("<ancestor_list>").append(cwUtils.NEWL);
            if (this.skill.skb_ancestor!=null && this.skill.skb_ancestor.length()>0){
                long[] ancestors = cwUtils.splitToLong(this.skill.skb_ancestor.trim(), " , ");
                for (int i=0; i<ancestors.length; i++){
                    l_ancestor = new Long(ancestors[i]);
                    ancestor_title = (String)htAncestor.get(l_ancestor);
                    xmlBuf.append("<skill id=\"").append(l_ancestor).append("\">").append(cwUtils.NEWL);
                    xmlBuf.append("<title>").append(cwUtils.esc4XML(cwUtils.escNull(ancestor_title))).append("</title>").append(cwUtils.NEWL);
                    xmlBuf.append("</skill>").append(cwUtils.NEWL);
                }
            }
            xmlBuf.append("</ancestor_list>").append(cwUtils.NEWL);
        }
        if(closeInd) {
            xmlBuf.append("</skill>");
        }
        return xmlBuf.toString();        
    }

    /**
    Get a scale content from database and 
    vendor it as XML from this.skill.skl_ssl_id
    */
    private String getSkillScaleAsXML(Connection con) 
        throws SQLException, cwSysMessage, cwException {
        ViewCmSkill viewSkill = new ViewCmSkill();
        return viewSkill.getScaleAsXML(con, this.skill.skb_ssl_id);
    }

    private String getSkillScaleAsXML(ViewCmSkill scale) {
        boolean selected_ind = (scale.skillLevelVec != null);
        StringBuffer xmlBuf = new StringBuffer(256);
        xmlBuf.append("<scale id=\"").append(scale.skillScale.ssl_id).append("\"")
              .append(" selected=\"").append(selected_ind).append("\">")
              .append(cwUtils.NEWL);
        xmlBuf.append("<title>").append(cwUtils.esc4XML(cwUtils.escNull(scale.skillScale.ssl_title))).append("</title>")
              .append(cwUtils.NEWL);
        xmlBuf.append("<create usr_id=\"").append(cwUtils.escNull(scale.skillScale.ssl_create_usr_id)).append("\"")
              .append(" timestamp=\"").append(cwUtils.escNull(scale.skillScale.ssl_create_timestamp)).append("\"/>");
        xmlBuf.append("<update usr_id=\"").append(cwUtils.escNull(scale.skillScale.ssl_update_usr_id)).append("\"")
              .append(" timestamp=\"").append(cwUtils.escNull(scale.skillScale.ssl_update_timestamp)).append("\"/>");
        if(selected_ind) {
            xmlBuf.append("<levels>");
            for(int i=0; i<scale.skillLevelVec.size(); i++) {
                DbCmSkillLevel level = (DbCmSkillLevel) scale.skillLevelVec.elementAt(i);
                xmlBuf.append("<level scale_id=\"").append(level.sle_ssl_id).append("\"")
                      .append(" value=\"").append(level.sle_level).append("\"")
                      .append(" label=\"").append(level.sle_label).append("\">")
                      .append(cwUtils.esc4XML(cwUtils.escNull(level.sle_description)))
                      .append("</level>");
            }
            xmlBuf.append("</levels>");
        }
        xmlBuf.append("</scale>");
        return xmlBuf.toString();
    }

    /**
    Get XML of child skills of this.skill
    */
    private String getSkillChildAsXML(Connection con, int curPage, int pageSize, 
                                      HttpSession sess, Timestamp in_timestamp) 
                                      throws SQLException {
        StringBuffer xmlBuf = new StringBuffer();
        cwPagination cwPage = new cwPagination();
        boolean fr_sess = false;
        long[] skl_skb_id_list=null;
        Timestamp sess_timestamp = (Timestamp) sess.getAttribute(CompetencyModule.SESS_COMPETENCY_SKILL_CHILD_TS);
        if(sess_timestamp != null && in_timestamp != null && sess_timestamp.equals(in_timestamp)) {
            skl_skb_id_list = (long[]) sess.getAttribute(CompetencyModule.SESS_COMPETENCY_SKILL_CHILD_LIST);
            if(skl_skb_id_list != null && skl_skb_id_list.length > 0) {
                fr_sess = true;
            }
        }

        Vector v_child = null;
        if(fr_sess) {
            v_child = DbCmSkill.getSkillFromList(con, skl_skb_id_list);
            cwPage.ts = sess_timestamp;
        } else {
            v_child = this.skill.getSkillChild(con);
            cwPage.ts = cwSQL.getTime(con);
            sess.setAttribute(CompetencyModule.SESS_COMPETENCY_SKILL_CHILD_TS, cwPage.ts);
            skl_skb_id_list = new long[v_child.size()];
            for(int i=0; i<v_child.size(); i++) {
                DbCmSkill tempSkill = (DbCmSkill) v_child.elementAt(i);
                skl_skb_id_list[i] = tempSkill.skl_skb_id;
            }
            sess.setAttribute(CompetencyModule.SESS_COMPETENCY_SKILL_CHILD_LIST, skl_skb_id_list);
        }
        cwPage.totalRec  = v_child.size();
        cwPage.pageSize  = pageSize;
        cwPage.curPage   = curPage;
        cwPage.sortCol   = "skl_type";
        cwPage.sortOrder = "asc";
        cwPage.totalPage = (int)Math.ceil((float)cwPage.totalRec/(float)cwPage.pageSize);
        
        int begin=(curPage-1)*pageSize;
        int end=begin+pageSize;
        if(end>v_child.size()) {
            end = v_child.size();
        }
        xmlBuf.append("<skill_list>")
            .append(cwUtils.NEWL);
        xmlBuf.append(cwPage.asXML())
            .append(cwUtils.NEWL);
        CmSkillManager childMgr = new CmSkillManager();
        for(int i=begin; i<end; i++) {
            childMgr.skill = (DbCmSkill)v_child.elementAt(i);
            xmlBuf.append(childMgr.getSkillAsXML(true, null));
        }
        xmlBuf.append("</skill_list>")
            .append(cwUtils.NEWL);
        return xmlBuf.toString();    
    }


    /**
    Get all the shared scale name 
    @param owner_ent_id id of the organization
    @scale_id id of the scale that showing the details
    */
    public String getAllScaleAsXML(Connection con, long owner_ent_id, long scale_id)
        throws SQLException, cwSysMessage , cwException {
        
        ViewCmSkill vSkill = new ViewCmSkill();
        
        ResultSet rs = vSkill.getSharedScale(con, owner_ent_id, " ssl_title ", " ASC ");
        
        StringBuffer xmlBody = new StringBuffer();
        
        int count = 0;
        while (rs.next()) {
            count ++;
            xmlBody.append("<scale id=\"").append(rs.getLong("ssl_id")).append("\" level_count=\"")
                    .append(rs.getInt("level_count")).append("\">").append(cwUtils.NEWL)
                    .append("<title>").append(cwUtils.esc4XML(rs.getString("ssl_title")))
                    .append("</title>").append(cwUtils.NEWL)
                    .append("</scale>").append(cwUtils.NEWL);
        }
        rs.close();
        
        StringBuffer xmlBuf = new StringBuffer();
        xmlBuf.append("<scalelist>").append(cwUtils.NEWL);
        xmlBuf.append("<scales total=\"").append(count).append("\">").append(cwUtils.NEWL);
        xmlBuf.append(xmlBody.toString());              
        xmlBuf.append("</scales>").append(cwUtils.NEWL);
        
        // get the specified scale details 
        if (scale_id > 0) {
            xmlBuf.append(vSkill.getScaleAsXML(con, scale_id));
        }
        xmlBuf.append("</scalelist>").append(cwUtils.NEWL);


        return xmlBuf.toString();
    }

    
    /**
    Get all the sclae that within the current page and pagese
    */
    public String getScaleListAsXML(Connection con, HttpSession sess, long owner_ent_id, int cur_page, int pagesize, Timestamp pagetime, String order_by, String sort_by)
        throws SQLException, cwSysMessage , cwException {
        
        Hashtable data = null;
        boolean useSess = false;
        Timestamp sess_pagetime = null;
        if (sess !=null) {
            data = (Hashtable) sess.getAttribute(CompetencyModule.SESS_COMPETENCY_SCALE_LIST);
            if (data !=null) {
                sess_pagetime = (Timestamp) data.get(HASH_TIMESTAMP);
                if (sess_pagetime.equals(pagetime)) {
                    useSess = true;
                }
            }
        }
        
        int start = (cur_page-1) * pagesize + 1;
        int end  = cur_page * pagesize;
        
        ViewCmSkill vSkill = new ViewCmSkill();
        
        ResultSet rs = null;
        Vector sessIdVec = new Vector();
        if (useSess) {
            sessIdVec = (Vector)data.get(HASH_SSL_ID_VEC);
            Vector matchVec = new Vector();            
            String sessSortby = (String)data.get(HASH_SORTBY);
            String sessOrderby = (String)data.get(HASH_ORDERBY);
            if( !sessSortby.equalsIgnoreCase(sort_by) || !sessOrderby.equalsIgnoreCase(order_by) ) {
                rs = vSkill.getSharedScale(con, owner_ent_id, order_by, sort_by);
                start = 1;
                end = pagesize;
                cur_page = 1;
            } else {
                for (int i=start ; i<= sessIdVec.size() && i <= end;i++) {
                    matchVec.addElement(sessIdVec.elementAt(i-1));
                }
            
                rs = vSkill.getSharedScale(con, owner_ent_id, order_by, sort_by, matchVec);
            }
        }else {
            rs = vSkill.getSharedScale(con, owner_ent_id, order_by, sort_by);
        }
        
        StringBuffer xmlBody = new StringBuffer();
        Vector sslIdVec = new Vector();
        DbCmSkillScale scale = null;
        int i=start;
        long scale_id = 0;
        while (rs.next()) {
            //i++;
            scale_id = rs.getLong("ssl_id");
            sslIdVec.addElement(new Long(scale_id));
            if (/*useSess ||*/ (i >= start  && i<=end)) {
                xmlBody.append("<scale id=\"").append(scale_id).append("\" level_count=\"")
                        .append(rs.getInt("level_count")).append("\">").append(cwUtils.NEWL)
                        .append("<title>").append(cwUtils.esc4XML(rs.getString("ssl_title")))
                        .append("</title>").append(cwUtils.NEWL)
                        .append("</scale>").append(cwUtils.NEWL);
            }
            i++;
        }
        rs.close();
        
        int count = 0;
        Timestamp pageTime = null;
        Hashtable curData = new Hashtable();
        if (useSess) {
            pageTime = sess_pagetime;
            count = sessIdVec.size();
            curData.put(HASH_SSL_ID_VEC, sessIdVec);
        }else {
            count = sslIdVec.size();
            pageTime = cwSQL.getTime(con);
            curData.put(HASH_SSL_ID_VEC, sslIdVec);
            /*
            Hashtable curData = new Hashtable();
            curData.put(HASH_TIMESTAMP, pageTime);
            curData.put(HASH_SSL_ID_VEC, sslIdVec);
            sess.setAttribute(CompetencyModule.SESS_COMPETENCY_SCALE_LIST, curData);
            */
        }        
        curData.put(HASH_SORTBY, sort_by);
        curData.put(HASH_ORDERBY, order_by);
        curData.put(HASH_TIMESTAMP, pageTime);
        
        sess.setAttribute(CompetencyModule.SESS_COMPETENCY_SCALE_LIST, curData);

        
        StringBuffer xmlBuf = new StringBuffer();
        xmlBuf.append("<scalelist>").append(cwUtils.NEWL);
        xmlBuf.append("<scales total=\"").append(count).append("\" cur_page=\"")
              .append(cur_page).append("\" pagesize=\"").append(pagesize)
              .append("\" timestamp=\"").append(pageTime)
              .append("\" order_by=\"").append(order_by)
              .append("\" sort_by=\"").append(sort_by)
              .append("\">").append(cwUtils.NEWL);
        xmlBuf.append(xmlBody.toString());              
        xmlBuf.append("</scales>").append(cwUtils.NEWL);
        xmlBuf.append("</scalelist>").append(cwUtils.NEWL);

        return xmlBuf.toString();
    }


    /**
    Update skill
    */
    public void updSkill(Connection con, String usr_id, long owner_ent_id) 
        throws SQLException, cwSysMessage, cwException {

        // Check timestamp
        if (!skill.equalsTimestamp(con)) {
            throw new cwSysMessage(CompetencyModule.SMSG_CMP_INVALID_TIMESTAMP);
        }
        //get the image of skill before update from database
        DbCmSkill curSkill_ = new DbCmSkill();
        curSkill_.skl_skb_id = this.skill.skl_skb_id;
        curSkill_.skb_id = this.skill.skl_skb_id;
        curSkill_.get(con);
        //if this.skill's scale is going to be change 
        //check if this.skill is at top level
        //only top level skill can be changed
        if(curSkill_.skb_ssl_id != this.skill.skb_ssl_id ){
            if (curSkill_.skb_parent_skb_id > 0) {
                throw new cwSysMessage(CompetencyModule.SMSG_CMP_SCALE_NOT_CHANGEABLE);
            }else{
                // is top level
                // to change the skill, should not have a successor that is active and consists in active assessmentResult
                if (ViewCmAssessment.checkSkillSuccessorInASY(con, this.skill.skl_skb_id)){
                    throw new cwSysMessage(CompetencyModule.SMSG_CMP_SKILL_OR_SUCCESSOR_IN_USE);
                }
            }
        }
        //update skill
        ViewCmSkill vSkill = new ViewCmSkill();
        vSkill.skill = this.skill;


        vSkill.updSkill(con, usr_id, owner_ent_id);
        //if this.skill's scale was changed, cascade changes its children's scales
        if(curSkill_.skb_ssl_id != this.skill.skb_ssl_id) {
            //TODO: Cascase update
            this.skill.updSuccessorScale(con);
        }
        return;
    }

    

    /**
    Insert a new skill
    @param owner_ent_id entity id of the organization
    */
    public void insSkill(Connection con, String usr_id, long owner_ent_id) 
        throws SQLException, cwSysMessage, cwException {
        
        //if this.skill has parent, 
        //check if this.skill.skl_ssl_id is same with its parent
        if(this.skill.skb_parent_skb_id >=0 && !checkScaleInheritence(con)) {
            throw new cwSysMessage(CompetencyModule.SMSG_CMP_SCALE_NOT_INHERIT);
        }
        
        ViewCmSkill vSkill = new ViewCmSkill();
        vSkill.skill = skill;
        //vSkill.skillScale = skillScale;
        //vSkill.skillLevelVec = skillLevelVec;
        vSkill.insSkill(con, usr_id, owner_ent_id);
        
    }

    
    /**
    Delete a new skill and move it to recycle bin
    @param owner_ent_id entity id of the organization
    */
    public void delSkill(Connection con, String usr_id) 
        throws SQLException, cwSysMessage, cwException {
        
        // delete the skill from survey. profile (keep the assessment result as Histroy)
        ViewCmSkillCoverage.removeSkills(con, skill.skl_skb_id, SKILL_PROFILE);
        ViewCmSkillCoverage.removeSkills(con, skill.skl_skb_id, SKILL_TEMPLATE);

        // delete the item skill relation
        DbItemCompetency itemCm = new DbItemCompetency();
        itemCm.itc_skl_skb_id = skill.skl_skb_id;
        itemCm.delBySkillId(con);
		skill.delChild(con, usr_id);
        skill.del(con, usr_id);
        
    }

    /**
    Physically remove a new skill from recycle bin
    */
    /*
    public void trashSkill(Connection con)
        throws SQLException, cwSysMessage, cwException {
//System.out.println("A");
        // Check timestamp
        if (!skill.equalsTimestamp(con)) {
            throw new cwSysMessage(CompetencyModule.SMSG_CMP_INVALID_TIMESTAMP);
        }
//System.out.println("B");
        // Check if the skill is used
        if (DbCmSkillSetCoverage.checkSkillExist(con, skill.skl_skb_id)) {
            throw new cwSysMessage(CompetencyModule.SMSG_CMP_SKILL_USED);
        }
//System.out.println("C");
        ViewCmSkill vSkill = new ViewCmSkill();
        vSkill.skill = skill;
        vSkill.trashSkill(con);

    }
*/
    /**
    Insert a shared scale
    */
    public void insScale(Connection con, String usr_id, long owner_ent_id)
        throws SQLException, cwSysMessage {

        ViewCmSkill vSkill = new ViewCmSkill();
        skillScale.ssl_share_ind = true;
        vSkill.skillScale = skillScale;
        vSkill.skillLevelVec = skillLevelVec;
        
        vSkill.insScale(con, usr_id, owner_ent_id);
    }

    
    /**
    Delete a shared scale
    */
    public void delScale(Connection con, String usr_id)
        throws SQLException, cwSysMessage, cwException {
        // Can't delete the scale if it is used in any skill
        if (DbCmSkill.isUsedScale(con, skillScale.ssl_id)) {
            throw new cwSysMessage(CompetencyModule.SMSG_CMP_SCALE_USED);
        }
        // Check timestamp
        if (!skillScale.equalsTimestamp(con)) {
            throw new cwSysMessage(CompetencyModule.SMSG_CMP_INVALID_TIMESTAMP);
        }
        
        ViewCmSkill vSkill = new ViewCmSkill();
        vSkill.skillScale = skillScale;
        if(DbCmSkill.isScaleUsedByDeletedSkill(con, skillScale.ssl_id)) {
            skillScale.softDelete(con, usr_id);
        } else {
            vSkill.delScale(con);
        }
    }

    /**
    Update a shared scale
    */
    public void updScale(Connection con, String usr_id)
        throws SQLException, cwSysMessage, cwException {
    
        // Check timestamp
        if (!skillScale.equalsTimestamp(con)) {
            throw new cwSysMessage(CompetencyModule.SMSG_CMP_INVALID_TIMESTAMP);
        }

        //boolean isUsedScale = DbCmSkill.isUsedScale(con, skillScale.ssl_id);
        boolean isUsedScale = ViewCmAssessment.isScaleInASR(con, skillScale.ssl_id);
        
        ViewCmSkill vSkill = new ViewCmSkill();
        vSkill.skillScale = skillScale;
        vSkill.skillScale.ssl_share_ind = true;
        vSkill.skillLevelVec = skillLevelVec;
        if (isUsedScale) {
            //scale level cannot be updated if it is used in assessment
            DbCmSkillLevel oldSkillLevel = new DbCmSkillLevel();
            oldSkillLevel.sle_ssl_id = skillScale.ssl_id;
            Vector oldSkillLevelVec = oldSkillLevel.getById(con);
            if(oldSkillLevelVec.size() != skillLevelVec.size()) {
                throw new cwSysMessage(CompetencyModule.SMSG_CMP_SCALE_IN_USED);
            } else {
                vSkill.updScaleByLevel(con, usr_id);
            }
        }else {
            vSkill.updScale(con, usr_id);
        }
    }




    /**
    Generate the skill list with target level in XML format from the data in session
    */
    public String genSkillListFromSess(Connection con, HttpSession sess)
        throws SQLException, cwException {
            
            StringBuffer xml = new StringBuffer();
            Hashtable hashList = (Hashtable)sess.getAttribute("Competency_SKILL_SET");
            if( hashList != null ) {
                
                Vector skillIdVec = (Vector)hashList.get(CompetencyModule.HASH_SKILL_ID_LIST);
                Vector levelVec = (Vector)hashList.get(CompetencyModule.HASH_SKILL_LEVEL_LIST);
                Hashtable skillLevelHash = new Hashtable();
                for(int i=0; i<skillIdVec.size() && i<levelVec.size(); i++)
                    skillLevelHash.put((Long)skillIdVec.elementAt(i), (Long)levelVec.elementAt(i));
                
                xml.append("<search>").append(cwUtils.NEWL);
                if( skillIdVec!= null && !skillIdVec.isEmpty() ) {
                    ResultSet rs = ViewCmSkill.getSkillsLevel(con, skillIdVec);                
                    Long skillId;
                    while( rs.next() ) {
                        skillId = new Long(rs.getLong("skb_id"));                    
                        xml.append("<skill id=\"").append(skillId).append("\" ")
                           .append(" max_level=\"").append(rs.getLong("total")).append("\" ")
                           .append(" target_level=\"").append((Long)skillLevelHash.get(skillId)).append("\" ")
                           .append(" type=\"").append(rs.getString("skb_type"))
                           .append("\">").append(cwUtils.NEWL)
                           .append("<title>").append(cwUtils.esc4XML(rs.getString("skb_title"))).append("</title>").append(cwUtils.NEWL)
                           .append("<desc>").append(cwUtils.esc4XML(rs.getString("skb_description"))).append("</desc>").append(cwUtils.NEWL)
                           .append("</skill>").append(cwUtils.NEWL);
                    }
                }
                xml.append("</search>").append(cwUtils.NEWL);
            }
            return xml.toString();
        }
        
    /**
    Search skill 
    @return String of XML
    */
    public String skillSearchAsXML(Connection con, HttpSession sess, long root_id, int cur_page, 
                                   int pagesize, Timestamp pagetime, String sort_by, String skb_title,
                                   String skb_description, String sle_label, String sle_description,
                                   int sle_level, boolean advSearch)
        throws SQLException, cwException {
            
            Hashtable data = null;
            boolean useSess = false;
            Timestamp sess_pagetime = null;
            String sess_sort = null;
            if (sess != null) {
                data = (Hashtable) sess.getAttribute(SESS_SKILL_SEARCH);
                if (data !=null) {
                    sess_pagetime = (Timestamp) data.get(HASH_TIMESTAMP);                    
                    sess_sort = (String) data.get(HASH_SORTBY);
                    if ( sess_pagetime.equals(pagetime) )
                        useSess = true;
                } else
                    data = new Hashtable();
            }
            
            int start = (cur_page-1) * pagesize + 1;
            int end  = cur_page * pagesize;

            Vector idVecFromSess = new Vector();
            Vector idVecFromDb = new Vector();
            Vector selectedIdVec = new Vector();
            ResultSet rs = null;
            if( useSess ) {
                idVecFromSess = (Vector)data.get(HASH_SKL_ID_VEC);
                //change sort by will goto page 1 of the serach result
                if( sess_sort != null && !sess_sort.equalsIgnoreCase(sort_by) ) {
                        start = 1;
                        end = idVecFromSess.size();
                        cur_page = 1;
                        selectedIdVec = idVecFromSess;
                } else {
                    for(int i=start-1; i<idVecFromSess.size() && i<end; i++)
                        selectedIdVec.addElement(idVecFromSess.elementAt(i));
                }
                if( !selectedIdVec.isEmpty() )
                    rs = DbCmSkill.getByIds(con, sort_by, selectedIdVec);
            } else {
                if( advSearch )
                    rs = ViewCmSkill.advSkillSearch(con, root_id, sort_by, skb_title, skb_description,
                                                    sle_label, sle_description, sle_level);
                else
                    rs = ViewCmSkill.simpleSkillSearch(con, root_id, sort_by, skb_title);
            }
            
            StringBuffer xmlBody = new StringBuffer();
            while( rs.next() ) {
                if( idVecFromDb.size() < pagesize ) {
                    xmlBody.append("<skill id=\"").append(rs.getLong("skb_id")).append("\" ")
                           .append(" type=\"").append(rs.getString("skb_type")).append("\" ")
                           .append(" title=\"").append(cwUtils.esc4XML(rs.getString("skb_title"))).append("\" ")
                           .append(" desc=\"").append(cwUtils.esc4XML(rs.getString("skb_description"))).append("\" ")
                           .append(">").append(cwUtils.NEWL);
                    xmlBody.append("<create usr_id=\"").append(rs.getString("skb_create_usr_id")).append("\"")
                           .append(" timestamp=\"").append(rs.getTimestamp("skb_create_timestamp")).append("\"/>")
                           .append(cwUtils.NEWL);
                    xmlBody.append("<update usr_id=\"").append(rs.getString("skb_update_usr_id")).append("\"")
                           .append(" timestamp=\"").append(rs.getTimestamp("skb_update_timestamp")).append("\"/>")
                           .append(cwUtils.NEWL);
                    xmlBody.append("</skill>").append(cwUtils.NEWL);
                }
                idVecFromDb.addElement(new Long(rs.getLong("skb_id")));
            }
            rs.close();
            
            int count = 0;
            if( useSess ) {
                count = idVecFromSess.size();
                if( sess_sort != null && !sess_sort.equalsIgnoreCase(sort_by) )
                        data.put(HASH_SKL_ID_VEC, idVecFromDb);
            } else {
                cur_page = 1;
                count = idVecFromDb.size();
                pagetime = cwSQL.getTime(con);                
                data.put(HASH_TIMESTAMP, pagetime);
                data.put(HASH_SKL_ID_VEC, idVecFromDb);
            }
            data.put(HASH_SORTBY, sort_by);
            sess.setAttribute(SESS_SKILL_SEARCH, data);

            StringBuffer xml = new StringBuffer();
            xml.append("<skills total=\"").append(count).append("\" ")
               .append(" cur_page=\"").append(cur_page).append("\" ")
               .append(" pagesize=\"").append(pagesize).append("\" ")
               .append(" timestamp=\"").append(pagetime).append("\" ")            
               .append(" sort_by=\"").append(sort_by).append("\" ")
               .append(" >").append(cwUtils.NEWL)
               .append(xmlBody)
               .append("</skills>").append(cwUtils.NEWL);


            return xml.toString();
        }


    /**
    Scale Search
    @return String of XML
    */
    public String scaleSearchAsXML(Connection con, HttpSession sess, long root_id, int cur_page,
                                                           int pagesize, Timestamp pagetime, String sort_by,
                                                           String order_by, String ssl_title, String ssl_share,
                                                           String sle_label, String sle_description,
                                                           int sle_level, boolean advSearch)
        throws SQLException {

            Hashtable data = null;
            boolean useSess = false;
            Timestamp sess_pagetime = null;
            String sess_sort = null;
            String sess_order = null;
            if (sess != null) {
                data = (Hashtable) sess.getAttribute(SESS_SKILL_SEARCH);
                if (data !=null) {
                    sess_pagetime = (Timestamp) data.get(HASH_TIMESTAMP);                    
                    sess_sort = (String) data.get(HASH_SORTBY);
                    sess_order = (String) data.get(HASH_ORDERBY);
                    if ( sess_pagetime.equals(pagetime) )
                        useSess = true;
                } else
                    data = new Hashtable();
            }
            
            int start = (cur_page-1) * pagesize + 1;
            int end  = cur_page * pagesize;

            Vector idVecFromSess = new Vector();
            Vector idVecFromDb = new Vector();
            Vector selectedIdVec = new Vector();
            ResultSet rs = null;
            if( useSess ) {
                idVecFromSess = (Vector)data.get(HASH_SKL_ID_VEC);
                //change sort by will goto page 1 of the serach result
                if( (sess_sort != null && !sess_sort.equalsIgnoreCase(sort_by)) ||
                    (sess_order != null && !sess_order.equalsIgnoreCase(order_by)) ) {
                        start = 1;
                        end = idVecFromSess.size();
                        cur_page = 1;
                        selectedIdVec = idVecFromSess;
                } else {
                    for(int i=start-1; i<idVecFromSess.size() && i<end; i++)
                        selectedIdVec.addElement(idVecFromSess.elementAt(i));
                }
              
                if( !selectedIdVec.isEmpty() )
                    rs = ViewCmSkill.getScaleByIds(con, sort_by, order_by, selectedIdVec);
              
            } else {
                if( advSearch )
                    rs = ViewCmSkill.advScaleSearch(con, sort_by, order_by, root_id, ssl_title, 
                                                    ssl_share, sle_level, sle_label, sle_description );
                else
                    rs = ViewCmSkill.simpleScaleSearch(con, sort_by, order_by, root_id, ssl_title);
            }
            
            StringBuffer xmlBody = new StringBuffer();
            while( rs.next() ) {
                if( idVecFromDb.size() < pagesize ) {
                    xmlBody.append("<scale id=\"").append(rs.getLong("ssl_id")).append("\" ")
                           .append(" title=\"").append(cwUtils.esc4XML(rs.getString("ssl_title"))).append("\" ")
                           .append(" count=\"").append(rs.getLong("total")).append("\" ")
                           .append(" share=\"").append(rs.getBoolean("ssl_share_ind")).append("\" ")
                           .append("/>").append(cwUtils.NEWL);
                }
                idVecFromDb.addElement(new Long(rs.getLong("ssl_id")));
            }
            rs.close();
            
            int count = 0;
            if( useSess ) {
                count = idVecFromSess.size();
                if( (sess_sort != null && !sess_sort.equalsIgnoreCase(sort_by))
                 || (sess_order != null && !sess_order.equalsIgnoreCase(order_by)) )
                        data.put(HASH_SKL_ID_VEC, idVecFromDb);
            } else {
                cur_page = 1;
                count = idVecFromDb.size();
                pagetime = cwSQL.getTime(con);                
                data.put(HASH_TIMESTAMP, pagetime);
                data.put(HASH_SKL_ID_VEC, idVecFromDb);
            }
            data.put(HASH_SORTBY, sort_by);
            data.put(HASH_ORDERBY, order_by);
            sess.setAttribute(SESS_SKILL_SEARCH, data);

            StringBuffer xml = new StringBuffer();
            xml.append("<scales total=\"").append(count).append("\" ")
               .append(" cur_page=\"").append(cur_page).append("\" ")
               .append(" pagesize=\"").append(pagesize).append("\" ")
               .append(" timestamp=\"").append(pagetime).append("\" ")            
               .append(" sort_by=\"").append(sort_by).append("\" ")
               .append(" order_by=\"").append(order_by).append("\" ")
               .append(" >").append(cwUtils.NEWL)
               .append(xmlBody)
               .append("</scales>").append(cwUtils.NEWL);


            return xml.toString();

        }
        
    /**
    Get XML of <skill_reference_data>
    */
    public String getSkillReferenceDataAsXML(Connection con, long owner_ent_id, long[] scale_id_list) 
        throws SQLException {
            
        StringBuffer xmlBuf = new StringBuffer(512);
        xmlBuf.append("<skill_reference_data>");
        //get <derive_rule_list>
        CmSkillLevelCalculator calc = new CmSkillLevelCalculator();
        xmlBuf.append(calc.getDeriveRuleListAsXML());
        //get <scale_list>
        xmlBuf.append(getSharedScaleAsXML(con, owner_ent_id, scale_id_list));
        xmlBuf.append("</skill_reference_data>");
        return xmlBuf.toString();
    }
    
    /**
    Get XML of organization's shared scales
    And those inside scale_id_list will be shown into details with its levels
    */
    private String getSharedScaleAsXML(Connection con, long owner_ent_id, long[] scale_id_list) 
        throws SQLException {
        StringBuffer xmlBuf = new StringBuffer(512);
        xmlBuf.append("<scale_list>");
        Vector v_scale = ViewCmSkill.getSharedScale(con, owner_ent_id, scale_id_list);
        CommonLog.debug("v_scale.size() = " + v_scale.size());
        for(int i=0; i<v_scale.size(); i++) {
            ViewCmSkill scale = (ViewCmSkill) v_scale.elementAt(i);
            xmlBuf.append(getSkillScaleAsXML(scale));
        }
        xmlBuf.append("</scale_list>");
        return xmlBuf.toString();
    }

    /**
    Check if this.skill's scale id preserve with its parent's scale id
    Pre-define variables: skill.skb_parent_skb_id
                          skill.skb_ssl_id
    */
    private boolean checkScaleInheritence(Connection con) throws SQLException, cwSysMessage {
        DbCmSkill parentSkill = new DbCmSkill();
        parentSkill.skl_skb_id = this.skill.skb_parent_skb_id;
        parentSkill.skb_id = this.skill.skb_parent_skb_id;
        return (this.skill.skb_ssl_id == parentSkill.getScaleId(con));
    }

    /**
    Get input skill's ancestor as XML
    */
    public String getSkillAncestorAsXML(Connection con, long skillId) 
        throws SQLException, cwSysMessage {
        DbCmSkillBase skillBase = new DbCmSkillBase();
        skillBase.skb_id = skillId;
        skillBase.get(con,false);
        return getSkillAncestorAsXML(con, skillBase);
    }


    /**
    Get input skill's ancestor as XML
    */
    public String getSkillAncestorAsXML(Connection con, DbCmSkillBase skillBase) throws SQLException {
        Hashtable htAncestor = skillBase.getAncestorTitle(con);
        StringBuffer xmlBuf = new StringBuffer(256);
        if (htAncestor!=null){
            Long l_ancestor;
            String ancestor_title;
            xmlBuf.append("<ancestor_list>").append(cwUtils.NEWL);
            if (skillBase.skb_ancestor!=null && skillBase.skb_ancestor.length()>0){
                long[] ancestors = cwUtils.splitToLong(skillBase.skb_ancestor.trim(), " , ");
                for (int i=0; i<ancestors.length; i++){
                    l_ancestor = new Long(ancestors[i]);
                    ancestor_title = (String)htAncestor.get(l_ancestor);
                    xmlBuf.append("<skill id=\"").append(l_ancestor).append("\">").append(cwUtils.NEWL);
                    xmlBuf.append("<title>").append(cwUtils.esc4XML(cwUtils.escNull(ancestor_title))).append("</title>").append(cwUtils.NEWL);
                    xmlBuf.append("</skill>").append(cwUtils.NEWL);
                }
            }
            xmlBuf.append("</ancestor_list>").append(cwUtils.NEWL);
        }
        return xmlBuf.toString();
    }
    
    
    public void delCompGroup(Connection con, loginProfile prof, DbCmSkillTreeNode skillTreeNode)
    	throws SQLException, cwSysMessage, cwException {
    		
    		Hashtable h_child_status = DbCmSkillBase.getChildNStatusHash(con, skillTreeNode.skb_id);

    		//No child, physically del
    		if( h_child_status.isEmpty() ){
    			skillTreeNode.del(con);
    		} else {
    			//check child is soft-deleted
    			Enumeration enumeration = h_child_status.keys();
    			while(enumeration.hasMoreElements()){
    				if( ((Boolean)h_child_status.get((Long)enumeration.nextElement())).booleanValue() ){
    					throw new cwSysMessage(CompetencyModule.SMSG_CMP_NODE_NOT_EMPTY);
    				}
    			}
    			skillTreeNode.softDel(con);
    		}
    		return;
    	}
    
    public void updCompGroup(Connection con, loginProfile prof, DbCmSkillTreeNode skillTreeNode)
    	throws SQLException, cwSysMessage, cwException {
    		
    		boolean upd_scale = false;
    		DbCmSkillTreeNode updTreeNode = new DbCmSkillTreeNode();
    		updTreeNode.stn_skb_id = skillTreeNode.stn_skb_id;
    		updTreeNode.skb_id = skillTreeNode.stn_skb_id;
    		updTreeNode.get(con);
    		
    		//if change competency group scale and it's child is in used throw exception
    		if( updTreeNode.skb_ssl_id != skillTreeNode.skb_ssl_id ) {
				upd_scale = true;
				if ( ViewCmAssessment.checkSkillSuccessorInASY(con,skillTreeNode.skb_id) ){
					throw new cwSysMessage(CompetencyModule.SMSG_CMP_SKILL_OR_SUCCESSOR_IN_USE);					
				}
				
    		}

    		updTreeNode.skb_update_usr_id = prof.usr_id;
    		updTreeNode.skb_title = skillTreeNode.skb_title;
    		updTreeNode.skb_description = skillTreeNode.skb_description;
    		updTreeNode.skb_ssl_id = skillTreeNode.skb_ssl_id;
    		updTreeNode.upd(con);

    		if( upd_scale ){
    			updTreeNode.updSuccessorScale(con);
    		}
    		return;
    	}
    
    
	
	public String getCompGroupAndChildXML(Connection con, HttpSession sess, long stn_id, cwPagination cwPage )
		throws SQLException, cwException, cwSysMessage {

			StringBuffer xml = new StringBuffer(1024);
			Vector v_skb = null;

			if( cwPage.pageSize <= 0 ){
				cwPage.pageSize = 10;
			}

			if( cwPage.curPage <= 0 ) {
				cwPage.curPage = 1;
			}

			if( cwPage.sortCol == null ) {
				cwPage.sortCol = "skb_title";
			}
				
			if( cwPage.sortOrder == null ) {
				cwPage.sortOrder = "ASC";
			}

			int start = (cwPage.curPage - 1) * cwPage.pageSize;
			int end = start + cwPage.pageSize;
			
			cwPagination sessPage = (cwPagination)sess.getAttribute(SESS_CM_PAGINATION);
			
			if( sessPage != null && sessPage.ts != null && cwPage.ts != null && (cwPage.ts).equals(sessPage.ts)) {

				Vector v_skb_id = (Vector)sess.getAttribute(SESS_CM_SKB_ID_VECTOR);
				Vector v_sub_skb_id = new Vector();
				
				for(int i=start; i<end && i<v_skb_id.size(); i++){
					v_sub_skb_id.addElement(v_skb_id.elementAt(i));
				}
				v_skb = DbCmSkillTreeNode.getChild(con, stn_id, DbCmSkillBase.COMPETENCY_COMPOSITE_SKILL, cwPage, v_sub_skb_id);
				cwPage.totalRec = sessPage.totalRec;
				cwPage.totalPage = sessPage.totalPage;
			} else {
				
				cwPage.ts = cwSQL.getTime(con);
				v_skb = new Vector();
				Vector v_skb_id = new Vector();
				Vector v_full_skb = DbCmSkillTreeNode.getChild(con, stn_id, DbCmSkillBase.COMPETENCY_COMPOSITE_SKILL, cwPage, null);
				for(int i=0; i<v_full_skb.size(); i++){
					if(i>= start && i<end) {
						v_skb.addElement(v_full_skb.elementAt(i));						
					}
					v_skb_id.addElement( new Long(((DbCmSkill)v_full_skb.elementAt(i)).skb_id) );
				}
				
				cwPage.totalRec = v_skb_id.size();
				cwPage.totalPage = (int)Math.ceil((float)cwPage.totalRec/(float)cwPage.pageSize);
				
				sess.setAttribute(SESS_CM_SKB_ID_VECTOR, v_skb_id);
				sess.setAttribute(SESS_CM_PAGINATION, cwPage);

			}
			
		
			DbCmSkillTreeNode skillTreeNode = new DbCmSkillTreeNode();
			skillTreeNode.skb_id = stn_id;
			skillTreeNode.stn_skb_id = stn_id;
			skillTreeNode.get(con, true);
			xml.append(skillTreeNode.asXML());
			
			xml.append("<competency_list>");
			if( v_skb!= null ) {
			
				for(int i=0; i<v_skb.size(); i++){
					DbCmSkill skill = (DbCmSkill)v_skb.elementAt(i);
					xml.append("<competency ")
						.append(" id=\"").append(skill.skb_id).append("\" ")
						.append(" type=\"").append(skill.skb_type).append("\" ")
						.append(" ssl_id=\"").append(skill.skb_ssl_id).append("\"")
						.append(" rating_ind=\"").append(skill.skl_rating_ind).append("\"")
						.append(" derive_rule=\"").append(skill.skl_derive_rule).append("\"")
						.append(">")
						.append("<title>").append(cwUtils.esc4XML(skill.skb_title)).append("</title>")
						.append("<description>").append(cwUtils.esc4XML(skill.skb_description)).append("</description>")
						.append("</competency>");
				}
			}
			xml.append(cwPage.asXML());
			xml.append("</competency_list>");

			return xml.toString();
		}
	
	    
	public void insCompetency(Connection con, loginProfile prof, DbCmSkill skill, String[] behav_desc)
		throws SQLException, cwException, cwSysMessage {
			
			skill.skb_ssl_id = DbCmSkillBase.getScaleId(con, skill.skb_parent_skb_id);
			skill.skb_owner_ent_id = prof.root_ent_id;
			skill.skb_type = DbCmSkillBase.COMPETENCY_COMPOSITE_SKILL;
			skill.skl_type = skill.skb_type;
			skill.skb_create_usr_id = prof.usr_id;
			skill.ins(con);
			
			if( behav_desc != null && behav_desc.length > 0 ) {
				int count = 1;
				DbCmSkill behavSkill = new DbCmSkill();
				behavSkill.skl_rating_ind = !skill.skl_rating_ind;
				behavSkill.skb_parent_skb_id = skill.skb_id;
				behavSkill.skb_type = DbCmSkillBase.COMPETENCY_SKILL;
				behavSkill.skl_type = behavSkill.skb_type;
				behavSkill.skb_ssl_id = skill.skb_ssl_id;
				behavSkill.skb_create_usr_id = prof.usr_id;
				behavSkill.skb_owner_ent_id = prof.root_ent_id;
				for(int i=0; i<behav_desc.length; i++){
					if( behav_desc[i] != null && behav_desc[i].length() > 0 ) {
						behavSkill.skb_order = count++;
						behavSkill.skb_description = behav_desc[i];
						behavSkill.ins(con);
					}
				}
			}
			return;
		}
    
    public void updCompetency(Connection con, loginProfile prof, DbCmSkill skill, String[] behav_desc, long[] behav_order, long[] behav_skb_id)
    	throws SQLException, cwException, cwSysMessage {
    		
    		skill.skb_update_usr_id = prof.usr_id;
    		skill.skb_update_timestamp = cwSQL.getTime(con);
    		skill.updCompetency(con);
    		
    		long skb_parent_skb_id = skill.skb_id;
    		    		
    		//toggle the rating ind used by behavior
    		skill.skl_rating_ind = !skill.skl_rating_ind;
    		skill.skb_title = null;
    		if( behav_skb_id != null && behav_skb_id.length > 0 ) {
	    		for(int i=0; i<behav_skb_id.length; i++){
	    			skill.skb_description = behav_desc[i];
	    			skill.skb_order = behav_order[i];
	    			skill.skb_id = behav_skb_id[i];
	    			skill.updBehavior(con);
	    		}
    		}
			
			skill.skb_type = DbCmSkillBase.COMPETENCY_SKILL;
			skill.skl_type = DbCmSkillBase.COMPETENCY_SKILL;
			skill.skb_create_usr_id = prof.usr_id;
			skill.skb_title = null;
			long ssl_id = DbCmSkillBase.getScaleId(con, skb_parent_skb_id);
    		for(int i=behav_skb_id.length; i<behav_desc.length; i++) {
    			skill.skb_parent_skb_id = skb_parent_skb_id;
    			skill.skb_owner_ent_id = prof.root_ent_id;
    			skill.skb_ssl_id = ssl_id;
				skill.skb_description = behav_desc[i];
				skill.skb_order = behav_order[i];
 				skill.ins(con);
    		}

    		return;
    	}
    
    
    public String getCompetencyAsXml(Connection con, long skb_id)
    	throws SQLException, cwException, cwSysMessage{
    		
    		StringBuffer xml = new StringBuffer();
    		DbCmSkill skill = new DbCmSkill();
    		skill.skb_id = skb_id;
    		skill.skl_skb_id = skb_id;
    		skill.get(con, true);
    		
    		xml.append(skill.XML());
    		xml.append("<behavioral_indicator_list>");
			Vector child = skill.getSkillChild(con);
			for(int i=0; i<child.size(); i++){
				DbCmSkill childSkill = (DbCmSkill)child.elementAt(i);
				xml.append("<behavioral_indicator ")
					.append(" id=\"").append(childSkill.skb_id).append("\">")
					.append("<description>").append(cwUtils.esc4XML(childSkill.skb_description)).append("</description>")
					.append("</behavioral_indicator>");
			}
    		xml.append("</behavioral_indicator_list>");
    		return xml.toString();
    		
    	}
    
    
}
