package com.cw.wizbank.JsonMod.know;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import com.cw.wizbank.JsonMod.Course.Course;
import com.cw.wizbank.JsonMod.Course.CourseModuleParam;
import com.cw.wizbank.JsonMod.credit.Credit;
import com.cw.wizbank.JsonMod.know.bean.CatalogNavBean;
import com.cw.wizbank.JsonMod.know.bean.KnowAnswerBean;
import com.cw.wizbank.JsonMod.know.bean.KnowHelpBean;
import com.cw.wizbank.JsonMod.know.bean.KnowQuestionBean;
import com.cw.wizbank.JsonMod.know.bean.MyKnowQueBean;
import com.cw.wizbank.JsonMod.know.bean.QueSearchCriteriaBean;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.config.organization.usermanagement.impl.UserManagementImpl;
import com.cw.wizbank.config.system.scheduledtask.ParamType;
import com.cw.wizbank.config.system.scheduledtask.TaskType;
import com.cw.wizbank.config.system.scheduledtask.impl.ScheduledTaskImpl;
import com.cw.wizbank.credit.dao.CreditsTypeDAO;
import com.cw.wizbank.credit.view.ViewCreditsDAO;
import com.cw.wizbank.db.DbTrainingCenter;
import com.cw.wizbank.db.view.ViewTrainingCenter;
import com.cw.wizbank.know.dao.KnowAnswerDAO;
import com.cw.wizbank.know.dao.KnowCatalogDAO;
import com.cw.wizbank.know.dao.KnowCatalogRelationDAO;
import com.cw.wizbank.know.dao.KnowQuestionDAO;
import com.cw.wizbank.know.dao.KnowVoteDetailDAO;
import com.cw.wizbank.know.db.KnowAnswerDB;
import com.cw.wizbank.know.db.KnowCatalogDB;
import com.cw.wizbank.know.db.KnowCatalogRelationDB;
import com.cw.wizbank.know.db.KnowQuestionDB;
import com.cw.wizbank.know.db.KnowVoteDetailDB;
import com.cw.wizbank.know.view.ViewKnowAnsUserDAO;
import com.cw.wizbank.know.view.ViewKnowCatalogRelationDAO;
import com.cw.wizbank.know.view.ViewKnowQueAnsDAO;
import com.cw.wizbank.know.view.ViewQueCatalogAnsDAO;
import com.cw.wizbank.know.view.ViewUserCreditsUserDAO;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbSns;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.WordsFilter;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwPagination;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;

/**
 * @author DeanChen
 * 
 */
public class Know {

    private KnowModuleParam knowModParam = null;

    private loginProfile prof = null;

    private WizbiniLoader wizbIni = null;

    public static final String TAB_QUE_UNANS_LST = "que_unans_lst";

    public static final String TAB_QUE_ANSED_LST = "que_ansed_lst";

    public static final String TAB_QUE_POPULAR_LST = "que_popular_lst";
    
    public static final String TAB_QUE_FAQ_LST = "que_faq_lst";

    public static final String QUE_SEARCH_CRITERIA = "que_search_criteria";

    public static final String TAB_MY_QUE = "my_que";

    public static final String TAB_MY_ANS = "my_ans";

    public static final String ACTIVE_TAB = "active_tab";

    // for search parameter list
    private static final String KEY_SRH_KEYWORD = "srh_key";

    private static final String KEY_SRH_KEY_TYPE = "srh_key_type";

    private static final String KEY_SRH_CATALOG_ID = "srh_catalog_id";

    private static final String KEY_SRH_QUE_TYPE = "srh_que_type";

    private static final String KEY_SRH_QUE_START_PERIOD = "srh_que_start_period";
    
    private static final String KEY_SIMPLE_SRH_IND = "srh_simple_srh_ind";

    // search, key type const
    public static final String SRH_KEY_TYPE_TITLE = "TITLE";

    public static final String SRH_KEY_TYPE_FULLTEXT = "FULLTEXT";

    // search, question start period const
    public static final String[] que_start_period_arr = { "IMMEDIATE", "LAST_1_WEEK", "LAST_2_WEEK", "LAST_1_MONTH", "LAST_2_MONTH" };

    public static final int[] que_start_period_day_arr = { -1, 7, 14, 30, 60 };
    
    //tabname of json
    public static final String JSON_KNOW_CATALOG_DETAIL = "know_catalog_detail";
    public static final String JSON_KNOW_CATALOG = "know_catalog";
    
    // relation course count
    private static final int RELATE_COURSE_NUM = 5;
    private static final int KNOW_CATALOG_DETAIL_COLUMN_NUM = 3;
    
    public final static String SIMPLE_SRH_KEY = "SIMPLE_SRH_KEY";

    public Know() {
        ;
    }

    /**
     * constructor
     * 
     * @param knowModParam
     * @param prof
     */
    public Know(KnowModuleParam knowModParam, loginProfile prof) {
        this.knowModParam = knowModParam;
        this.prof = prof;
    }

    /**
     * constructor
     * 
     * @param knowModParam
     * @param prof
     * @param wizbIni
     */
    public Know(KnowModuleParam knowModParam, loginProfile prof,
            WizbiniLoader wizbIni) {
        this.knowModParam = knowModParam;
        this.prof = prof;
        this.wizbIni = wizbIni;
    }

    /**
     * to insert one question of kown.
     * 
     * @param con
     * @throws SQLException
     * @throws cwException
     * @throws cwSysMessage
     * @throws qdbException
     */
    public void insKownQuestion(Connection con) throws SQLException,
            cwException, cwSysMessage {

        KnowQuestionDB knowQueDb = genKnowQueDBByParam(con, KnowQuestionDAO.QUE_TYPE_UNSOLVED);
        KnowQuestionDAO knowQueDao = new KnowQuestionDAO();
        knowQueDao.ins(con, knowQueDb);
        int queId = knowQueDb.getQue_id();
        ViewKnowCatalogRelationDAO.updCatQueCount(con, knowQueDb
                .getQue_kca_id(), 1);

        // add records to knowCatalogRelation
        insQueRelation(con, knowQueDb.getQue_kca_id(), queId, prof.usr_id);

        // deduction credits of current user
        Credit credit = new Credit();
        credit.updUserCredits(con, Credit.ZD_NEW_QUE, queId,
                (int) prof.usr_ent_id, prof.usr_id, 0, 0, 0,0);
    }

    private void insQueRelation(Connection con, int catId, int queId,
            String userId) throws SQLException {
        KnowCatalogRelationDAO relationDao = new KnowCatalogRelationDAO();
        Vector relationVec = relationDao.getRelationByCatId(con, catId, queId,
                userId);
        for (Iterator iter = relationVec.iterator(); iter.hasNext();) {
            KnowCatalogRelationDB relationDb = (KnowCatalogRelationDB) iter
                    .next();
            relationDao.ins(con, relationDb);
        }
    }

    /**
     * to get question by id.
     * 
     * @param con
     * @param resultMap
     * @throws SQLException
     */
    public void getKownQuestion(Connection con, HashMap resultMap)
            throws SQLException {
        int queId = knowModParam.getQue_id();
        KnowQuestionDB knowQueDb = new KnowQuestionDB();
        knowQueDb.setQue_id(queId);
        KnowQuestionDAO knowQueDao = new KnowQuestionDAO();
        knowQueDao.getQueByQueId(con, knowQueDb);
        // get parent catalog id
        int parentCatId = KnowCatalogRelationDAO.getDirectParentCat(con,
                knowQueDb.getQue_kca_id());
        knowQueDb.setQue_parent_kca_id(parentCatId);

        resultMap.put("que_detail", knowQueDb);
    }

    public void updKownQuestion(Connection con) throws SQLException {
        KnowQuestionDB queDb = genKnowQueDBByParam(con, KnowQuestionDAO.QUE_TYPE_UNSOLVED);
        KnowQuestionDAO knowQueDao = new KnowQuestionDAO();
        knowQueDao.upd(con, queDb);
    }

    /**
     * to generate KnowQuestaionDB by parameters object that gets from request.
     * 
     * @param con
     * @param queType
     * @return KnowQuestaionDB
     * @throws SQLException
     */
    private KnowQuestionDB genKnowQueDBByParam(Connection con, String queType)
            throws SQLException {
        Timestamp curTime = cwSQL.getTime(con);

        KnowQuestionDB knowQueDB = new KnowQuestionDB();
        knowQueDB.setQue_id(knowModParam.getQue_id());
        knowQueDB.setQue_kca_id(knowModParam.getQue_kca_id());
        knowQueDB.setQue_title(knowModParam.getQue_title());
        knowQueDB.setQue_content(knowModParam.getQue_content());
        knowQueDB.setQue_status(KnowQuestionDAO.QUE_STATUS_OK);
        knowQueDB.setQue_type(queType);
        knowQueDB.setQue_popular_ind(false);
        knowQueDB.setQue_reward_credits(knowModParam.getQue_reward_credits());
        knowQueDB.setQue_create_ent_id((int) prof.usr_ent_id);
        knowQueDB.setQue_create_timestamp(curTime);
        knowQueDB.setQue_update_ent_id(knowQueDB.getQue_create_ent_id());
        knowQueDB.setQue_update_timestamp(curTime);

        return knowQueDB;
    }

    /**
     * to insert one answer of specified question.
     * 
     * @param con
     * @param resultMap
     * @throws SQLException
     * @throws cwException
     * @throws cwSysMessage
     */
    public void insKnowAnswer(Connection con) throws SQLException, cwException,
            cwSysMessage {
        // validate whether it exists the filter words in the question content
        // and question title.
        boolean isContainFilterWord = WordsFilter.isContainFilterWords(knowModParam.getAns_content(), wizbIni.filterWordsVec);
        if (isContainFilterWord) {
            throw new cwSysMessage(KnowModule.SILLY_INVESTMENT);
        }

        // insert answer
        KnowAnswerDB knowAnsDb = genKnowAnsDBByParam(con);
        KnowAnswerDAO knowAnsDao = new KnowAnswerDAO();
        knowAnsDao.ins(con, knowAnsDb);
        
        // add credit of current user
        Credit credit = new Credit();
        credit.updUserCredits(con, Credit.ZD_COMMIT_ANS, knowAnsDb.getAns_que_id(), (int) prof.usr_ent_id, prof.usr_id, 0, 0, 0,0);
    }

    /**
     * set the answer is optimal.
     * 
     * @param con
     * @throws SQLException
     * @throws qdbException
     * @throws cwException
     * @throws cwSysMessage
     */
    public void updAnswerRight(Connection con) throws SQLException,
            cwException, cwSysMessage {
        int ansId = knowModParam.getAns_id();
        // set in knowmoduleAccess
        int queId = knowModParam.getAns_que_id();
        int creatorEntId = knowModParam.getAns_create_ent_id();

        // validate whether the question is solved
        KnowQuestionDB queDb = new KnowQuestionDB();
        queDb.setQue_id(queId);
        KnowQuestionDAO queDao = new KnowQuestionDAO();
        queDao.getQueByQueId(con, queDb);

        // update answer to database
        KnowQuestionDAO.updQueType(con, queId, KnowQuestionDAO.QUE_TYPE_SOLVED);
        KnowAnswerDAO.updAnsRight(con, ansId);

        // add credit of current user
        Credit credit = new Credit();
        credit.updUserCredits(con, Credit.ZD_CHOOSE_ANS, queId,
                (int) prof.usr_ent_id, prof.usr_id, 0, 0, 0,0);

        // add credit of user whose answer current question
        String ansCretorUserId = dbRegUser.getUserId(con, creatorEntId);
        credit.updUserCredits(con, Credit.ZD_RIGHT_ANS, queId, creatorEntId,
                ansCretorUserId, 0, 0, 0,0);

    }

    /**
     * to update answer for vote.
     * 
     * @param con
     * @throws SQLException
     */
    public void updAnswerVote(Connection con) throws SQLException {
        int ansId = knowModParam.getAns_id();

        KnowAnswerDB ansDb = new KnowAnswerDB();
        ansDb.setAns_id(ansId);
        boolean isVoteFor = knowModParam.isVote_for_ind();

        // get database object
        KnowAnswerDAO knowAnsDao = new KnowAnswerDAO();
        knowAnsDao.get(con, ansDb);
        // update datebase object
        updAnswerDbForVote(ansDb, isVoteFor);

        // update datebase
        knowAnsDao.updVote(con, ansDb);

        // insert vote log
        KnowVoteDetailDB voteDetailDb = new KnowVoteDetailDB();
        voteDetailDb.setKvd_que_id(ansDb.getAns_que_id());
        voteDetailDb.setKvd_ans_id(ansId);
        voteDetailDb.setKvd_ent_id((int) prof.usr_ent_id);
        voteDetailDb.setKvd_create_usr_id(prof.usr_id);
        Timestamp curTime = cwSQL.getTime(con);
        voteDetailDb.setKvd_create_timestamp(curTime);
        KnowVoteDetailDAO voteDetailsDao = new KnowVoteDetailDAO();
        voteDetailsDao.ins(con, voteDetailDb);
    }

    private void updAnswerDbForVote(KnowAnswerDB ansDb, boolean isVoteFor) {
        int voteTotal = 0;
        int voteFor = ansDb.getAns_vote_for();
        int voteDown = ansDb.getAns_vote_down();
        int tempVoteTotal = ansDb.getAns_temp_vote_total();
        int tempVoteFor = ansDb.getAns_temp_vote_for();
        int tempVoteForDownDiff = 0;

        // set for vote
        if (isVoteFor) {
            voteFor += 1;
            tempVoteFor += 1;
        } else {
            voteDown += 1;
        }
        voteTotal = voteFor + voteDown;
        
        // set for temp vote
        tempVoteTotal += 1 ;
        tempVoteForDownDiff = 2 * tempVoteFor - tempVoteTotal;

        // set db
        ansDb.setAns_vote_total(voteTotal);
        ansDb.setAns_vote_for(voteFor);
        ansDb.setAns_vote_down(voteDown);
        ansDb.setAns_temp_vote_total(tempVoteTotal);
        ansDb.setAns_temp_vote_for(tempVoteFor);
        ansDb.setAns_temp_vote_for_down_diff(tempVoteForDownDiff);
    }

    /**
     * to generate KnowAnswerDB by parameters object that gets from request.
     * 
     * @param con
     * @return
     * @throws SQLException
     */
    private KnowAnswerDB genKnowAnsDBByParam(Connection con)
            throws SQLException {
        Timestamp curTime = cwSQL.getTime(con);

        KnowAnswerDB knowAnsDB = new KnowAnswerDB();
        knowAnsDB.setAns_que_id(knowModParam.getAns_que_id());
        knowAnsDB.setAns_content(knowModParam.getAns_content());
        knowAnsDB.setAns_content_search(knowModParam.getAns_content_search());
        knowAnsDB.setAns_refer_content(knowModParam.getAns_refer_content());
        knowAnsDB.setAns_right_ind(false);
        knowAnsDB.setAns_status(KnowAnswerDAO.ANS_STATUS_OK);
        knowAnsDB.setAns_create_ent_id((int) prof.usr_ent_id);
        knowAnsDB.setAns_create_timestamp(curTime);
        knowAnsDB.setAns_update_ent_id((int) prof.usr_ent_id);
        knowAnsDB.setAns_update_timestamp(curTime);

        return knowAnsDB;
    }

    public void cancelQuestion(Connection con) throws SQLException,
            cwException, cwSysMessage {
        int queId = knowModParam.getQue_id();

        KnowQuestionDB knowQueDb = new KnowQuestionDB();
        knowQueDb.setQue_id(queId);
        KnowQuestionDAO queDao = new KnowQuestionDAO();
        queDao.getQueByQueId(con, knowQueDb);

        // delete answer of question
        KnowAnswerDAO.delAnsByQueId(con, queId);
        
        KnowCatalogRelationDAO.delRelationOfQue(con, queId);
        
        // delete question
        KnowQuestionDAO.delQueByQueId(con, queId);

        // update the question count of catalog
        ViewKnowCatalogRelationDAO.updCatQueCount(con, knowQueDb
                .getQue_kca_id(), -1);

        // add credits of current user
        Credit credit = new Credit();
        credit.updUserCredits(con, Credit.ZD_CANCEL_QUE, queId,
                (int) prof.usr_ent_id, prof.usr_id, 0, 0, 0,0);

    }

    public void getMyRecord(Connection con, HashMap resultMap)
            throws SQLException, cwException {
        boolean isRecordHome = knowModParam.isHome_ind();
        if (isRecordHome) {
            // get statistics
            getKnowStatisticsByUser(con, resultMap);
            // get queVec
            getMyKnowQue(con, resultMap);
            // set the tab selected
            resultMap.put(ACTIVE_TAB, knowModParam.getActivetab());
        } else {
            // get queVec
            getMyKnowQue(con, resultMap);
        }
    }

    /**
     * to get my question of know.
     * 
     * @param con
     * @param resultMap
     * @throws SQLException
     * @throws cwException
     */
    public void getMyKnowQue(Connection con, HashMap resultMap)
            throws SQLException, cwException {
        boolean isGetByQueCreator = isGetByQueCreator();

        // get queVec
        ViewKnowQueAnsDAO viewKnowQueAnsDao = new ViewKnowQueAnsDAO(
                knowModParam.getStart(), knowModParam.getLimit());
        Vector myQueVec = new Vector();
        viewKnowQueAnsDao.sort_col = knowModParam.getSort();
        viewKnowQueAnsDao.sort_order = knowModParam.getDir();

        int queTotal = viewKnowQueAnsDao.getQueLstByCreator(con, prof.usr_ent_id, myQueVec, isGetByQueCreator);

        // generate myQue
        MyKnowQueBean myQueBean = new MyKnowQueBean();
        myQueBean.setIs_my_que(isGetByQueCreator);
        myQueBean.setTotal(queTotal);
        myQueBean.setSort(viewKnowQueAnsDao.sort_col);
        myQueBean.setDir(viewKnowQueAnsDao.sort_order);
        myQueBean.setQue_lst(myQueVec);

        // generate result json
        if (isGetByQueCreator) {
            knowModParam.setActivetab(TAB_MY_QUE);
            resultMap.put(TAB_MY_QUE, myQueBean);
        } else {
            knowModParam.setActivetab(TAB_MY_ANS);
            resultMap.put(TAB_MY_ANS, myQueBean);
        }
    }

    private boolean isGetByQueCreator() {
        boolean isGetByQueCreator = false;
        if (TAB_MY_ANS.equalsIgnoreCase(knowModParam.getActivetab())) {
            isGetByQueCreator = false;
        } else {
            isGetByQueCreator = true;
        }

        return isGetByQueCreator;
    }

    /**
     * to get my statistics information.
     * 
     * @param con
     * @param resultMap
     * @throws SQLException
     */
    public void getKnowStatisticsByUser(Connection con, HashMap resultMap)
            throws SQLException {
        // to get the count of my questions
        int myQueCount = KnowQuestionDAO.getQueCountByCreator(con,
                prof.usr_ent_id);
        // to get the count of my answers
        int myAnsCount = ViewKnowQueAnsDAO.getQueCountByReponser(con,(int) prof.usr_ent_id);
        // to get my total credits
//        int myTotalCredits = ViewCreditsDAO.getCreditsByEntId(con, prof.usr_ent_id);

        // to generate Statistics map
        HashMap StatisticsMap = new HashMap();
        StatisticsMap.put("usr_ent_id", new Long(prof.usr_ent_id));
        StatisticsMap.put("usr_display_bil", prof.usr_display_bil);
        StatisticsMap.put("que_count", new Integer(myQueCount));
        StatisticsMap.put("ans_count", new Integer(myAnsCount));
//        StatisticsMap.put("credits_total", new Integer(myTotalCredits));

        // set json
        resultMap.put("personal_know_info", StatisticsMap);
    }

    /**
     * to get the structure of know's catalog before to add a question.
     * 
     * @param con
     * @return
     * @throws SQLException
     */
	public void getKnowCatalogStructure(Connection con, HashMap resultMap) throws SQLException {
		HashMap catalogStuctureMap = ViewKnowCatalogRelationDAO.getKnowCatalogStucture(con, false, true, prof.root_ent_id);
		Vector catalogStrucVec = convertCatalogMapToVec(catalogStuctureMap);
		resultMap.put("catalog_structure", catalogStrucVec);
	}
    
    /**
     * to get the detail of catalog structure by fixed column.
     * @param con
     * @param resultMap
     * @throws SQLException
     */
    public void getKnowCatalogStructDetail(Connection con, HashMap resultMap) throws SQLException {
    	HashMap catalogStuctureMap = ViewKnowCatalogRelationDAO.getKnowCatalogStucture(con, false, true, prof.root_ent_id);
    	Vector catalogStructVec = convertCatalogMapToVec(catalogStuctureMap);
    	//show catalog detail by column
    	Vector topCatalogStructVec = splitCatalogVec(catalogStructVec, KNOW_CATALOG_DETAIL_COLUMN_NUM);
    	HashMap topKnowCatalogMap = new HashMap();
    	topKnowCatalogMap.put(JSON_KNOW_CATALOG, topCatalogStructVec);
    	resultMap.put(JSON_KNOW_CATALOG_DETAIL, topKnowCatalogMap);
    }
    
    public static Vector splitCatalogVec(Vector catalogStructVec, int splitNum) {
    	Vector topCatalogVec = null;
    	
    	if(catalogStructVec != null && catalogStructVec.size() > 0) {
    		topCatalogVec = new Vector();
    		int hasGetCatalogNum = 0;
    		int catalogStructSize = catalogStructVec.size();
    		for(int index = 0; hasGetCatalogNum < catalogStructSize && index < splitNum; index++) {
    			Vector topCatalogColumnVec = new Vector();
    			
    			int currentCatCount = catalogStructSize / splitNum;
    			if(index < catalogStructSize % splitNum) {
    				currentCatCount++;
    			}
    			// get catalog on column index
    			for(int columnCatIndex = hasGetCatalogNum; columnCatIndex < hasGetCatalogNum + currentCatCount; columnCatIndex++) {
    				topCatalogColumnVec.addElement(catalogStructVec.elementAt(columnCatIndex));
    			}
    			topCatalogVec.addElement(topCatalogColumnVec);
    			hasGetCatalogNum += currentCatCount;
    		}
    	}
    	
    	return topCatalogVec;
    }
    
    /**
     * to get the top catalog of question.
     * @param con
     * @param resultMap
     * @throws SQLException
     */
    public void getKnowTopCatalog(Connection con, HashMap resultMap) throws SQLException {
        Vector topCatVec = KnowCatalogDAO.getTopCatalogList(con, prof.root_ent_id);
        HashMap catalogStuctureMap = new HashMap();
        catalogStuctureMap.put("catalog_structure", topCatVec);
        resultMap.put("top_catalog_lst", catalogStuctureMap);
    }

    /**
     * to get the information of know's homepage.
     * 
     * @param con
     * @param resultMap
     * @param itmDir upload path of item
     * @throws SQLException
     * @throws cwException
     */
    public void getKnowHomePage(Connection con, WizbiniLoader wizbini, String inLang, HashMap resultMap, String itmDir)
            throws SQLException, cwException {
        // know statistics
        HashMap queDiffStatusMap = KnowQuestionDAO.getDiffStatusQueCnt(con, prof.root_ent_id);
        HashMap queMap = new HashMap();
        queMap.put("que_answered", queDiffStatusMap
                .get(KnowQuestionDAO.QUE_TYPE_SOLVED)
                + "");
        queMap.put("que_unanswered", queDiffStatusMap
                .get(KnowQuestionDAO.QUE_TYPE_UNSOLVED)
                + "");
        resultMap.put("que_ans_count", queMap);

        // question catalog
        HashMap catalogStuctureMap = ViewKnowCatalogRelationDAO
                .getKnowCatalogStucture(con, false, true, prof.root_ent_id);
        Vector catalogStrucVec = convertCatalogMapToVec(catalogStuctureMap);

        catalogStuctureMap = new HashMap();
        catalogStuctureMap.put("catalog_structure", catalogStrucVec);
        resultMap.put("know_catalog", catalogStuctureMap);
        
        // top catalog list
        catalogStuctureMap = new HashMap();
        catalogStuctureMap.put("catalog_structure", catalogStrucVec);
        resultMap.put("top_catalog_lst", catalogStuctureMap);

        // credits charts
//        Vector creditsChartsVec = ViewUserCreditsUserDAO
//                .getKnowCreditsCharts(con, prof.root_ent_id);
//        HashMap creditsLstMap = new HashMap();
//        creditsLstMap.put("credits_lst", creditsChartsVec);
//        resultMap.put("user_credits", creditsLstMap);

        // get default question list
        knowModParam.setHome_ind(false);
        getQueList(con, wizbini, inLang, resultMap, itmDir);
    }

    private HashMap getQueMap(String queType, Vector queVec, int queTotal,
            String sort, String dir) {
        boolean que_popular_ind = false;
        if (KnowQuestionDAO.QUE_TYPE_POPULAR.equals(queType) || TAB_QUE_POPULAR_LST.equals(queType)) {
            que_popular_ind = true;
        } else {
            que_popular_ind = false;
        }

        HashMap queMap = new HashMap(); 
        queMap.put("que_type", queType);
        queMap.put("que_popular_ind", new Boolean(que_popular_ind));
        queMap.put("total", new Integer(queTotal));
        queMap.put("sort_col", sort);
        queMap.put("sort_order", dir);
        queMap.put("que_lst", queVec);

        return queMap;
    }

    public void getUnsolvedQue(Connection con, HashMap resultMap)
            throws SQLException, cwException {
        ViewQueCatalogAnsDAO viewQueCatAnsDao = new ViewQueCatalogAnsDAO(knowModParam);

        Vector unsolvedQueVec = new Vector();
        int queTotal = viewQueCatAnsDao.getQue(con, KnowQuestionDAO.QUE_TYPE_UNSOLVED, unsolvedQueVec,
                knowModParam.getKca_id(), prof.root_ent_id);
        HashMap unsolvedQueMap = getQueMap(KnowQuestionDAO.QUE_TYPE_UNSOLVED,
                unsolvedQueVec, queTotal, viewQueCatAnsDao.sort,
                viewQueCatAnsDao.dir);

        resultMap.put("que_unans_lst", unsolvedQueMap);
    }

    public void getSolvedQue(Connection con, HashMap resultMap)
            throws SQLException, cwException {
        ViewQueCatalogAnsDAO viewQueCatAnsDao = new ViewQueCatalogAnsDAO(knowModParam);

        Vector solvedQueVec = new Vector();
        int queTotal = viewQueCatAnsDao.getQue(con, KnowQuestionDAO.QUE_TYPE_SOLVED, solvedQueVec,knowModParam.getKca_id(), prof.root_ent_id);

        HashMap solvedQueMap = getQueMap(KnowQuestionDAO.QUE_TYPE_SOLVED,solvedQueVec, queTotal, viewQueCatAnsDao.sort, viewQueCatAnsDao.dir);
        resultMap.put("que_ansed_lst", solvedQueMap);
    }
    
    public void getFaq(Connection con, HashMap resultMap) throws SQLException, cwException {
		ViewQueCatalogAnsDAO viewQueCatAnsDao = new ViewQueCatalogAnsDAO(knowModParam);

		Vector solvedQueVec = new Vector();
		int queTotal = viewQueCatAnsDao.getQue(con, KnowQuestionDAO.QUE_TYPE_FAQ, solvedQueVec, knowModParam.getKca_id(), prof.root_ent_id);

		HashMap solvedQueMap = getQueMap(KnowQuestionDAO.QUE_TYPE_FAQ, solvedQueVec, queTotal, viewQueCatAnsDao.sort, viewQueCatAnsDao.dir);
		resultMap.put("que_faq_lst", solvedQueMap);
	}

    public void getPopulardQue(Connection con, HashMap resultMap) throws SQLException, cwException {
        ViewQueCatalogAnsDAO viewQueCatAnsDao = new ViewQueCatalogAnsDAO(knowModParam);

        Vector popularQueVec = new Vector();
        int queTotal = viewQueCatAnsDao.getPopularQue(con, popularQueVec, knowModParam.getKca_id(), prof.root_ent_id);

        HashMap popularQueMap = getQueMap(KnowQuestionDAO.QUE_TYPE_POPULAR, popularQueVec, queTotal, viewQueCatAnsDao.sort, viewQueCatAnsDao.dir);
        resultMap.put("que_popular_lst", popularQueMap);
    }

    public void setSessOfSearchQue(HttpSession sess,
            QueSearchCriteriaBean searchBean) {
        if (sess != null) {

        }
    }

    /**
     * to search que by cirteria
     * 
     * @param con
     * @param resultMap
     * @throws SQLException
     * @throws cwException
     */
    public void searchQue(Connection con, QueSearchCriteriaBean criteriaBean,
            HashMap resultMap) throws SQLException, cwException {
        // validate whether active tab is in the question type list of search
        // and set that tab type of question
    	String[] srhQueType = null;
        if (criteriaBean != null) {
            srhQueType = criteriaBean.getSrh_que_type();
        }
        boolean isVaildTab = isVaildTab(knowModParam.getActivetab(), srhQueType); 
        String tabQueType = getTabQueType(srhQueType);

        Vector queVec = new Vector();
        int queTotal = 0;
        
        if (isVaildTab) {
            // set default active tab for search
            ViewQueCatalogAnsDAO viewQueCatAnsDao = new ViewQueCatalogAnsDAO(knowModParam);
            
            // set the condition of search
            viewQueCatAnsDao.srhCriteriaBean = criteriaBean;
            viewQueCatAnsDao.srhCriteriaBean.setSrh_tab_que_type(tabQueType);
            // execute query
            queTotal = viewQueCatAnsDao.searchQue(con, queVec, prof.root_ent_id);

            // set sort and dir when the sort and dir that are get from request
            // are empty.
            knowModParam.setSort(viewQueCatAnsDao.sort);
            knowModParam.setDir(viewQueCatAnsDao.dir);
        }

        // set search criteria
        resultMap.put("srh_criteria", criteriaBean);

        // set the map that is used to get json
        HashMap queMap = getQueMap(tabQueType, queVec, queTotal, knowModParam
                .getSort(), knowModParam.getDir());

        // set question list
        resultMap.put(tabQueType, queMap);

        // set the tab type that has been selected.
        resultMap.put("active_tab", tabQueType);
        
        //set catalog structure
        if(criteriaBean.isSrh_simple_srh_ind()) {
            getKnowTopCatalog(con, resultMap);
        }
    }
    
    public String searchQueAsXML(Connection con, QueSearchCriteriaBean criteriaBean, cwPagination page) throws SQLException, cwException {
    	int queTotal = 0;
    	Vector queVec = new Vector();
        ViewQueCatalogAnsDAO viewQueCatAnsDao = new ViewQueCatalogAnsDAO(knowModParam);
        viewQueCatAnsDao.srhCriteriaBean = criteriaBean;
		queTotal = viewQueCatAnsDao.searchQue(con, queVec, prof.root_ent_id, this.knowModParam.getCwPage());
		
        page.totalRec = queTotal;
		page.totalPage = page.totalRec / page.pageSize;
		if (page.totalRec % page.pageSize != 0) {
			page.totalPage++;
		}
		
		StringBuffer xml = new StringBuffer();
		xml.append("<que_lst>");
		for (int i = (page.curPage - 1) * page.pageSize; i < queVec.size() && i < (page.curPage * page.pageSize); i++) {
			KnowQuestionBean que = (KnowQuestionBean) queVec.elementAt(i);
			StringBuffer queStr = new StringBuffer();
			queStr.append("<que id=\"").append(que.getQue_id()).append("\"");		
			queStr.append(" title=\"").append(cwUtils.esc4XML(que.getQue_title())).append("\"");
			queStr.append(" create_usr_ent_id=\"").append(que.getQue_create_ent_id()).append("\"");
			queStr.append(" create_usr_name=\"").append(cwUtils.esc4XML(dbRegUser.getUsrNickname(con, que.getQue_create_ent_id()))).append("\"");
			queStr.append(" create_timestamp=\"").append(que.getQue_create_timestamp()).append("\"");
			queStr.append(" update_timestamp=\"").append(que.getQue_update_timestamp()).append("\"");
			queStr.append(" kcr_create_timestamp=\"").append(KnowCatalogRelationDAO.getQueKcrCreateTimestamp(con, que.getQue_id())).append("\"");
			queStr.append(" que_type=\"").append(que.getQue_type()).append("\">");
			int parent_kca_id = KnowCatalogRelationDAO.getDirectParentCat(con, que.getKca_id());
			if (parent_kca_id > 0) {
				KnowCatalogDB catDb = new KnowCatalogDB();
				KnowCatalogDAO catalogDAO = new KnowCatalogDAO();
				catDb.setKca_id(parent_kca_id);
				catalogDAO.get(con, catDb);
				queStr.append("<kca id=\"").append(catDb.getKca_id()).append("\" title=\"").append(cwUtils.esc4XML(catDb.getKca_title())).append("\"/>");
			}
			queStr.append("<kca id=\"").append(que.getKca_id()).append("\" title=\"").append(cwUtils.esc4XML(que.getKca_title())).append("\"/>");
			queStr.append("</que>");
			xml.append(queStr);
		}
		xml.append("</que_lst>");
		xml.append(page.asXML());
		return xml.toString();
    }
    
    /**
     * to filter invalid tab type of question
     * 
     * @param tabQueType
     * @return
     */
    private String getTabQueType(String[] srhQueType) {
        String tabQueType = knowModParam.getActivetab();

        // check whether the tab type is invalid in search criterion.
        boolean isExistUnansType = false;
        boolean isExistAnsedType = false;
        boolean isExistPopularType = false;
        boolean isExistFaqType = false;
        if(srhQueType != null && srhQueType.length > 0) {
            for(int i = 0; i < srhQueType.length; i++) {
                String queType = srhQueType[i];
                if (TAB_QUE_UNANS_LST.equalsIgnoreCase(queType)) {
                    isExistUnansType = true;
                } else if (TAB_QUE_ANSED_LST.equalsIgnoreCase(queType)) {
                    isExistAnsedType = true;
                } else if (TAB_QUE_POPULAR_LST.equalsIgnoreCase(queType)) {
                    isExistPopularType = true;
                } else if (TAB_QUE_FAQ_LST.equalsIgnoreCase(queType)) {
                	isExistFaqType = true;
                }
            }
        } 
        
        // set default tab
        if (tabQueType == null && srhQueType != null && srhQueType.length > 0) {
            //the sort of question type, unanswer > answered > popular > faq
            if(isExistUnansType) {
                tabQueType = TAB_QUE_UNANS_LST;
            } else if(!isExistUnansType && isExistAnsedType) {
                tabQueType = TAB_QUE_ANSED_LST;
            } else if(!isExistUnansType && !isExistAnsedType && isExistPopularType) {
                tabQueType = TAB_QUE_POPULAR_LST;
            } else if(!isExistUnansType && !isExistAnsedType && !isExistPopularType && isExistFaqType) {
                tabQueType = TAB_QUE_FAQ_LST;
            }
        }
        
        if (TAB_QUE_UNANS_LST.equalsIgnoreCase(tabQueType)) {
            tabQueType = TAB_QUE_UNANS_LST;
        } else if (TAB_QUE_ANSED_LST.equalsIgnoreCase(tabQueType)) {
            tabQueType = TAB_QUE_ANSED_LST;
        } else if (TAB_QUE_POPULAR_LST.equalsIgnoreCase(tabQueType)) {
            tabQueType = TAB_QUE_POPULAR_LST;
        } else if (TAB_QUE_FAQ_LST.equalsIgnoreCase(tabQueType)) {
            tabQueType = TAB_QUE_FAQ_LST;
        } else {
            tabQueType = TAB_QUE_UNANS_LST;
        }

        return tabQueType;
    }
    
    public static boolean isVaildTab(String tabQueType, String[] srhQueTypeList) {
    	boolean isVaildTab = false;
    	
    	if(tabQueType == null) {
        	isVaildTab = true;
        }
    	if(srhQueTypeList == null) {
    		isVaildTab = true;
    	}
    	if(tabQueType != null && srhQueTypeList != null) {
    		for(int queTypeIndex = 0; queTypeIndex < srhQueTypeList.length; queTypeIndex++) {
    			if(tabQueType.equalsIgnoreCase(srhQueTypeList[queTypeIndex])){
    				isVaildTab = true;
    				break;
    			}
    		}
    	}
    	
    	return isVaildTab;
    }

    public QueSearchCriteriaBean getQueSrhBeanByParam(HttpSession sess)
            throws cwException {
        if (sess == null) {
            throw new cwException("session is null");
        }
        String searchId = knowModParam.getSearch_id();
        if (searchId == null || "".equals(searchId)) {
            throw new cwException("search_id is null");
        }
        HashMap searchMap = (HashMap) sess.getAttribute(searchId);
        String searchKey = null;
        String srhKeyType = null;
        String srhCatalogId = null;
        String srhQueStartPeriod = null;
        String[] srhQueType = null;
        boolean srhSimpleSrhInd = false;
        
        String [] searchValArr = null;
        searchValArr = (String[]) searchMap.get(KEY_SRH_KEYWORD);
        if(searchValArr != null && searchValArr.length > 0) {
            searchKey = cwUtils.unicodeFrom(searchValArr[0], knowModParam.getClientEnc(), knowModParam.getEncoding());
        }
        searchValArr = (String[]) searchMap.get(KEY_SRH_KEY_TYPE);
        if(searchValArr != null && searchValArr.length > 0) {
            srhKeyType = searchValArr[0];
        }
        
        srhQueType = (String[]) searchMap.get(KEY_SRH_QUE_TYPE);
        
        searchValArr = (String[]) searchMap.get(KEY_SRH_CATALOG_ID);
        if(searchValArr != null && searchValArr.length > 0) {
            srhCatalogId = searchValArr[0];
        }
        searchValArr = (String[]) searchMap.get(KEY_SRH_QUE_START_PERIOD);
        if(searchValArr != null && searchValArr.length > 0) {
            srhQueStartPeriod = searchValArr[0];
        }
        searchValArr = (String[]) searchMap.get(KEY_SIMPLE_SRH_IND);
        if(searchValArr != null && searchValArr.length > 0) {
            srhSimpleSrhInd = Boolean.valueOf(searchValArr[0]).booleanValue();
        }

        QueSearchCriteriaBean criterialBean = new QueSearchCriteriaBean();
        if (searchKey != null) {
            criterialBean.setSrh_key(searchKey);
        }
        if (srhKeyType != null) {
            criterialBean.setSrh_key_type(srhKeyType);
        }
        if (srhQueType != null && srhQueType.length > 0) {
            criterialBean.setSrh_que_type(srhQueType);
        }
        if (srhCatalogId != null) {
            long[] catIdArr = dbUtils.string2LongArray(srhCatalogId, "~");
            criterialBean.setSrh_catalog_id(catIdArr);
        }
        if (srhQueStartPeriod != null) {
            criterialBean.setSrh_que_start_period(srhQueStartPeriod);
        }
        
        criterialBean.setSrh_simple_srh_ind(srhSimpleSrhInd);

        return criterialBean;
    }
    
    /**
     * to get content and its answers of question.
     * @param con
     * @param resultMap
     * @param itmDir upload path of item
     * @throws SQLException
     * @throws cwException 
     */
    public void getQueDetail(Connection con, WizbiniLoader wizbini, String inLang, HashMap resultMap, String itmDir)
            throws SQLException, cwException {
    	String uploadUsrDirAbs = wizbIni.cfgSysSetupadv.getFileUpload().getUsrDir().getName();
    	//default user photo
    	UserManagementImpl userManagement = (UserManagementImpl) wizbIni.cfgOrgUserManagement.get(prof.root_id);
    	String defaultUserPhotoPath = userManagement.getUserProfile().getProfileAttributes().getExtension43().getValue();

    	// to get the detail of question
        int queId = knowModParam.getQue_id();
        KnowQuestionBean queBean = ViewKnowCatalogRelationDAO.getQueDetailByQueId(con,
                queId, uploadUsrDirAbs, defaultUserPhotoPath);
        String queType = queBean.getQue_type();
        resultMap.put("que_detail", queBean);

        // catalog navigation
        knowModParam.setKca_id(queBean.getKca_id());
        CatalogNavBean catNavBean = getCatalogNav(con);
        resultMap.put("nav_link", catNavBean);

        ViewKnowAnsUserDAO viewAnsUserDao = new ViewKnowAnsUserDAO(knowModParam
                .getStart(), knowModParam.getLimit());
        Vector ansVec = null;
        if (KnowQuestionDAO.QUE_TYPE_SOLVED.equals(queType) || KnowQuestionDAO.QUE_TYPE_FAQ.equals(queType)) {
            // to get the detail of right answer
            ansVec = new Vector();
            viewAnsUserDao.getAnsListByQueId(con, ansVec, queId, true, uploadUsrDirAbs, defaultUserPhotoPath);
            KnowAnswerBean rigthAnsBean = null;
            if (ansVec != null && ansVec.size() >= 1) {
                rigthAnsBean = (KnowAnswerBean) ansVec.elementAt(0);
            }

            // set the rate of vote
            setRightAnsVoteRate(rigthAnsBean);
            resultMap.put("que_right_ans", rigthAnsBean);

            if(KnowQuestionDAO.QUE_TYPE_SOLVED.equals(queType)) {
	            // whether the vote is overdue.
	            boolean isOverdue = KnowQuestionDAO.isOverdueVote(con, queId,
	                    wizbIni.zdVoteDuration);
	            // validate that current user has vote.
	            boolean hasVote = KnowVoteDetailDAO.isExistVote(con, queId,
	                    rigthAnsBean.getAns_id(), (int) prof.usr_ent_id);
	            HashMap queVoteMap = new HashMap();
	            queVoteMap.put("allow_vote", new Boolean(!isOverdue));
	            queVoteMap.put("has_vote", new Boolean(hasVote));
	            resultMap.put("que_vote", queVoteMap);
            }
        }

        // to get the detail of other answers
        ansVec = new Vector();
        int otherAnsTotal = viewAnsUserDao.getAnsListByQueId(con, ansVec,
                queId, false, uploadUsrDirAbs, defaultUserPhotoPath);
        HashMap otherAnsMap = new HashMap();
        otherAnsMap.put("ans_count", new Integer(otherAnsTotal));
        otherAnsMap.put("ans_lst", ansVec);
        resultMap.put("que_other_ans", otherAnsMap);

        // to get relation questions
        Vector relationQueVec = KnowQuestionDAO.getRelationQue(con, queId);
        resultMap.put("que_relation_lst", relationQueVec);

        // to get relation courses
        Vector catTitleVec = ViewKnowCatalogRelationDAO.getCatalogTitleByQueId(con, queId, false);
        Vector relateCosVec = getRelateCosByCatTitle(con, wizbini, inLang, catTitleVec, itmDir);
        resultMap.put("itm_lst", relateCosVec);
    }
    
    private Vector getRelateCosByCatTitle(Connection con, WizbiniLoader wizbini, String inLang, Vector catTitleVec, String itmDir) throws SQLException, cwException {
        Vector resultVec = null;
        if(catTitleVec != null) {
            String[] srhKeyArr = cwUtils.vec2strArray(catTitleVec);
            
            HashMap conditionMap = new HashMap();
            conditionMap.put("srh_key", srhKeyArr);
            conditionMap.put("srh_key_type", "FULLTEXT");
            Vector tcrIdVec = ViewTrainingCenter.getTcIdsByUser(con, prof.usr_ent_id, wizbini.cfgSysSetupadv.isTcIndependent());
			conditionMap.put("tcr_id_lst", tcrIdVec);
            conditionMap.put("sort", "citm.itm_publish_timestamp");
            conditionMap.put("dir", "desc");
            
            CourseModuleParam param = new CourseModuleParam(); 
            param.setStart(0);
            param.setLimit(RELATE_COURSE_NUM);
            Course cos = new Course();
            resultVec = cos.searchCourses(con, wizbini, prof.usr_ent_id, prof.root_ent_id, inLang, conditionMap, param, itmDir);
        }
        return resultVec;
    }

    private void setRightAnsVoteRate(KnowAnswerBean rigthAnsBean) {
    	if (rigthAnsBean != null) {
            float totalVoteCount = rigthAnsBean.getAns_vote_total();
            float voteForCount = rigthAnsBean.getAns_vote_for();
            int voteDownCount = rigthAnsBean.getAns_vote_down();
            
            int voteForRate = 0;
            int voteDownRate = 0;
            if (totalVoteCount > 0 && voteForCount > 0) {
                voteForRate = Math.round((voteForCount/totalVoteCount) * 100);
            }
            if (voteDownCount > 0) {
                voteDownRate = 100 - voteForRate;
            }

            rigthAnsBean.setAns_vote_for_rate(voteForRate);
            rigthAnsBean.setAns_vote_down_rate(voteDownRate);
        }
    }

    public void getQueList(Connection con, WizbiniLoader wizbini, String inLang, HashMap resultMap, String itmDir)
            throws SQLException, cwException {
        String tabQueType = knowModParam.getActivetab();
        if (TAB_QUE_ANSED_LST.equalsIgnoreCase(tabQueType)) {
            tabQueType = TAB_QUE_ANSED_LST;
            getSolvedQue(con, resultMap);
        } else if (TAB_QUE_POPULAR_LST.equalsIgnoreCase(tabQueType)) {
            tabQueType = TAB_QUE_POPULAR_LST;
            getPopulardQue(con, resultMap);
        } else if (TAB_QUE_FAQ_LST.equalsIgnoreCase(tabQueType)) {
            tabQueType = TAB_QUE_FAQ_LST;
            getFaq(con, resultMap);
        } else {
            tabQueType = TAB_QUE_UNANS_LST;
            getUnsolvedQue(con, resultMap);
        }

        // set tab selected
        resultMap.put(ACTIVE_TAB, tabQueType);

        if (knowModParam.isHome_ind()) {
            // top catalog structure
            getKnowTopCatalog(con, resultMap);
            // get sub catalog structure by catalog id
            getSubCatalogQue(con, resultMap);
            
            int catId = knowModParam.getKca_id();
            // set relation course for json
            if(catId != 0){
                Vector catTitleVec = ViewKnowCatalogRelationDAO.getCatalogTitleByCatId(con, catId, false);
                Vector relateCosVec = getRelateCosByCatTitle(con, wizbini, inLang, catTitleVec, itmDir);
                resultMap.put("itm_lst", relateCosVec);
            }
        }
    }

    public void getSubCatalogQue(Connection con, HashMap resultMap)
            throws SQLException, cwException {
        // catalog nav
        CatalogNavBean catNavBean = getCatalogNav(con);
        resultMap.put("nav_link", catNavBean);

        int catId = knowModParam.getKca_id();

        // question catalog
        Vector catalogStrucVec = ViewKnowCatalogRelationDAO.getSubCatStuctByCatId(con, catId);
        // get parent catalog id
        int parentCatId = KnowCatalogRelationDAO.getDirectParentCat(con, catId);

        HashMap catalogStuctureMap = new HashMap();
        catalogStuctureMap.put("parent_id", new Integer(parentCatId));
        catalogStuctureMap.put("catalog_structure", catalogStrucVec);
        resultMap.put("know_catalog", catalogStuctureMap);

        // unsolved question list
        ViewQueCatalogAnsDAO viewQueCatAnsDao = new ViewQueCatalogAnsDAO(knowModParam);
        Vector unsolvedQueVec = new Vector();
        int queTotal = viewQueCatAnsDao.getQue(con, KnowQuestionDAO.QUE_TYPE_UNSOLVED, unsolvedQueVec,
                knowModParam.getKca_id(), prof.root_ent_id);
        HashMap unsolvedQueMap = getQueMap(KnowQuestionDAO.QUE_TYPE_UNSOLVED,
                unsolvedQueVec, queTotal, viewQueCatAnsDao.sort,
                viewQueCatAnsDao.dir);

        resultMap.put("que_unans_lst", unsolvedQueMap);

    }

    private CatalogNavBean getCatalogNav(Connection con) throws SQLException {
        Vector parentNavVec = ViewKnowCatalogRelationDAO.getParentCatalogList(con, knowModParam.getKca_id());
        KnowCatalogDB knowCatDb = new KnowCatalogDB();
        knowCatDb.setKca_id(knowModParam.getKca_id());
        KnowCatalogDAO knowCatDao = new KnowCatalogDAO();
        knowCatDao.get(con, knowCatDb);

        CatalogNavBean catNavBean = new CatalogNavBean();
        catNavBean.setId(knowCatDb.getKca_id());
        catNavBean.setTitle(knowCatDb.getKca_title());
        catNavBean.setParent_nav(parentNavVec);

        return catNavBean;
    }

    public void getKnowHelp(Connection con, HashMap resultMap)
            throws SQLException {
        KnowHelpBean helpBean = new KnowHelpBean();
        // zd_vote_duration
        helpBean.setZd_vote_duration(wizbIni.zdVoteDuration);

        // set available_question_days,popular_question_count,vote_for_rate
        setPopularHelpInfo(helpBean);

        // set creditsType list
        Vector creditsTypeVec = CreditsTypeDAO.getAll(con);
        helpBean.setCredits_type_lst(creditsTypeVec);

        resultMap.put("help", helpBean);
    }

    private void setPopularHelpInfo(KnowHelpBean helpBean) {
        ScheduledTaskImpl scheduleTask = (ScheduledTaskImpl) wizbIni.cfgSysScheduledTask;

        if (scheduleTask != null) {
            int availableQuestionDays = 0;
            int popularQuestionCount = 0;
            int voteForRate = 0;

            List list = scheduleTask.getTask();
            List popularParamList = null;
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    TaskType taskType = (TaskType) list.get(i);
                    if (taskType != null
                            && taskType.getClassName().equals(
                                    MarkPopularQueScheduler.class.getName())) {
                        popularParamList = taskType.getParam();
                        break;
                    }
                }
            }
            if (popularParamList != null) {
                for (int i = 0; i < popularParamList.size(); i++) {
                    ParamType paramType = (ParamType) popularParamList.get(i);
                    String val = null;
                    if (paramType.getName().equals("available_question_days")) {
                        val = paramType.getValue();
                        availableQuestionDays = Integer.parseInt(val);
                    } else if (paramType.getName().equals(
                            "popular_question_count")) {
                        val = paramType.getValue();
                        popularQuestionCount = Integer.parseInt(val);
                    } else if (paramType.getName().equals("vote_for_rate")) {
                        val = paramType.getValue();
                        voteForRate = Integer.parseInt(val);
                    }
                }
            }

            // set bean of know help
            helpBean.setAvailable_question_days(availableQuestionDays);
            helpBean.setPopular_question_count(popularQuestionCount);
            helpBean.setVote_for_rate(voteForRate);
        }
    }

	public String getKnowCatalogList(Connection con) throws SQLException {
		int kca_id = this.knowModParam.getKca_id();
		cwPagination page = this.knowModParam.getCwPage();
		String result = KnowCatalogRelationDAO.getCatalogChildAsXML(con, kca_id, page, prof, wizbIni.cfgSysSetupadv.isTcIndependent());
		return result;
	}
	
	public void delKownQuestion(Connection con) throws SQLException {
		String que_id_lst = this.knowModParam.getQue_id_lst();
		String[] que_ids = null; 
		if (que_id_lst!= null) {
			que_ids = cwUtils.splitToString(que_id_lst, "~");
		}
		if (que_id_lst != null && que_ids.length > 0) {
			for (int i = 0; i < que_ids.length; i++) {
				long que_id = new Long(que_ids[i]).longValue();
				delKownQuestion(con, que_id);
			}
		}
	}
	
	public static void delKownQuestion(Connection con, long que_id) throws SQLException{
		//删除问题相关赞
		dbSns.deleteKnowLike(con, que_id);
		dbSns.deleteKnowLikeLog(con, que_id);
		//删除问题相关动态
		dbSns.deleteKnowDoing(con, que_id);
		// delete vote details of question
		KnowVoteDetailDAO.deleteByQueId(con, que_id);
		// delete answer of question
        KnowAnswerDAO.delAnsByQueId(con, que_id);
        // delete question
        KnowQuestionDAO queDao = new KnowQuestionDAO();
		queDao.del(con, que_id);
		// delete the relation of question between question and catalog
		KnowCatalogRelationDAO.delRelationOfQue(con, que_id);
	}

	public String getCatalogInfoAsXML(Connection con) throws SQLException {
		int kca_id = this.knowModParam.getKca_id();
		int parent_kca_id = this.knowModParam.getParent_kca_id();
		
		StringBuffer xml = new StringBuffer();
		if (kca_id > 0) {
			xml.append("<parent_kca id=\"").append(KnowCatalogRelationDAO.getDirectParentCat(con, kca_id)).append("\"/>");
			KnowCatalogDB knowCatDb = new KnowCatalogDB();
			knowCatDb.setKca_id(kca_id);
			KnowCatalogDAO catDao = new KnowCatalogDAO();
			catDao.get(con, knowCatDb);
			xml.append("<kca id=\"").append(knowCatDb.getKca_id()).append("\"");
			xml.append(" public_ind=\"").append(knowCatDb.isKca_public_ind() ? 1 : 0).append("\"");
			xml.append(" title=\"").append(cwUtils.esc4XML(knowCatDb.getKca_title())).append("\"");
			xml.append(" update_timestamp=\"").append(knowCatDb.getKca_update_timestamp()).append("\"/>");
		} else {
			xml.append("<parent_kca id=\"").append(parent_kca_id).append("\"/>");
		}
		return xml.toString();
	}

	public void insKownCatalog(Connection con) throws SQLException {	
		KnowCatalogDB knowCatalogDB = getKnowCatalogDBForIns(con);
		KnowCatalogDAO catDao = new KnowCatalogDAO();
		int kca_Id = catDao.ins(con, knowCatalogDB);
		int parent_id = knowModParam.getParent_kca_id();
		if (parent_id > 0) {
			insCatRelation(con, parent_id, kca_Id, prof.usr_id);	
		}
	}
	
	private void insCatRelation(Connection con, int parent_id, int kca_Id,
            String userId) throws SQLException {
        KnowCatalogRelationDAO relationDao = new KnowCatalogRelationDAO();
        Vector relationVec = relationDao.getRelationCatByCatId(con, parent_id, kca_Id,
                userId);
        for (Iterator iter = relationVec.iterator(); iter.hasNext();) {
            KnowCatalogRelationDB relationDb = (KnowCatalogRelationDB) iter
                    .next();
            relationDao.ins(con, relationDb);
        }
    }
	
	private KnowCatalogDB getKnowCatalogDBForIns(Connection con) throws SQLException {
		KnowCatalogDB knowCatalogDB = new KnowCatalogDB();
		knowModParam.getPage();
		knowCatalogDB.setKca_title(knowModParam.getKca_title());
		knowCatalogDB.setKca_public_ind(knowModParam.getKca_public_ind());
		knowCatalogDB.setKca_parent_kca_id(knowModParam.getParent_kca_id());
		if (knowCatalogDB.getKca_parent_kca_id() <= 0) {
			knowCatalogDB.setKca_type(KnowCatalogDAO.KCA_TYPE_CATALOG);
		} else {
			knowCatalogDB.setKca_type(KnowCatalogDAO.KCA_TYPE_NORMAL);
		}
		knowCatalogDB.setKca_tcr_id((int) prof.my_top_tc_id);
		knowCatalogDB.setKca_create_usr_id(prof.usr_id);
		knowCatalogDB.setKca_update_usr_id(prof.usr_id);
		return knowCatalogDB;
	}
	
	public void updKownCatalog(Connection con) throws SQLException {	
		KnowCatalogDAO catDao = new KnowCatalogDAO();
		KnowCatalogDB knowCatalogDB = getKnowCatalogDBForUpd(con);
		catDao.upd(con, knowCatalogDB);
	}
	
	private KnowCatalogDB getKnowCatalogDBForUpd(Connection con) throws SQLException {
		KnowCatalogDB knowCatalogDB = new KnowCatalogDB();
		knowCatalogDB.setKca_id(knowModParam.getKca_id());
		knowCatalogDB.setKca_title(knowModParam.getKca_title());
		knowCatalogDB.setKca_public_ind(knowModParam.getKca_public_ind());
		knowCatalogDB.setKca_update_usr_id(prof.usr_id);
		return knowCatalogDB;
	}

	public void delKownCatalog(Connection con) throws SQLException {
		int kca_id = this.knowModParam.getKca_id();
		KnowCatalogDAO.del(con, kca_id);
	}
	
	public String getSimpleSrhKey(HttpSession sess) throws cwException {
		if (sess == null) {
			throw new cwException("session is null");
		}
		String srhKey = this.knowModParam.getSrh_key();
		if (srhKey != null && srhKey != null && !"".equals(srhKey)) {
			sess.setAttribute(SIMPLE_SRH_KEY, srhKey);
		}
		return (String) sess.getAttribute(SIMPLE_SRH_KEY);
	}

	public String getSearchResultAsXML(Connection con, String srh_key) throws SQLException, cwException {
		cwPagination cwPage = this.knowModParam.getCwPage();
		QueSearchCriteriaBean criterialBean = new QueSearchCriteriaBean();
		if (srh_key != null) {
			criterialBean.setSrh_key(srh_key);
		}
		criterialBean.setSrh_simple_srh_ind(true);
		String xml = searchQueAsXML(con, criterialBean, cwPage);
		return xml.toString();
	}

	public void changeKca(Connection con) throws SQLException {
		int kca_id = this.knowModParam.getKca_id();
		String que_id_lst = this.knowModParam.getQue_id_lst();
		String[] que_ids = null;
		if (que_id_lst != null) {
			que_ids = cwUtils.splitToString(que_id_lst, "~");
		}
		if (que_id_lst != null && que_ids.length > 0) {
			KnowQuestionDAO queDao = new KnowQuestionDAO();
			for (int i = 0; i < que_ids.length; i++) {
				int que_id = new Integer(que_ids[i]).intValue();
				queDao.change(con, que_id, kca_id, prof.usr_ent_id);
				insQueRelation(con, kca_id, que_id, prof.usr_id);
			}
		}
	}

	/**
	 * 初始化整颗树
	 */
	public Vector getKnowCatalogTree(Connection con) throws SQLException {
		Vector vec = KnowCatalogDAO.getCatalogTreeVec(con, prof.root_ent_id);
		return vec;
	}

	/*
	 * convert the catalog map that contains catalog structure and the order of catalog ids to vector
	 */
	private static Vector convertCatalogMapToVec(HashMap resultMap) {
		HashMap catalogMap = (HashMap) resultMap.get(ViewKnowCatalogRelationDAO.MAP_KEY_KNOW_CATALOG);
		Vector catalogIdsVec =  (Vector) resultMap.get(ViewKnowCatalogRelationDAO.MAP_KEY_KNOW_CATALOG_IDS_ORDER);

		Vector catalogVec = new Vector();
		if(catalogIdsVec != null) {
			for(Iterator catIdsIter= catalogIdsVec.iterator(); catIdsIter.hasNext();) {
				Long catalogIdObj = (Long) catIdsIter.next();
				catalogVec.addElement(catalogMap.get(catalogIdObj));
			}
		}
		
		return catalogVec;
	}
	
	public String getInsFaqPrepXml(Connection con) throws SQLException {
		StringBuffer resultXml = new StringBuffer();
		
        //current catalog
        KnowCatalogDB knowCatDb = new KnowCatalogDB();
        knowCatDb.setKca_id(knowModParam.getKca_id());
        KnowCatalogDAO knowCatDao = new KnowCatalogDAO();
        knowCatDao.get(con, knowCatDb);
        
        resultXml.append("<cur_catalog id=\"").append(knowModParam.getKca_id()).append("\">")
        	.append(cwUtils.esc4XML(knowCatDb.getKca_title())).append("</cur_catalog>");
        
        //parent catalog list
        resultXml.append("<parent_catlog_lst>");
        Vector parentNavVec = ViewKnowCatalogRelationDAO.getParentCatalogList(con, knowModParam.getKca_id());
        for(int catIndex = 0; catIndex < parentNavVec.size(); catIndex++) {
        	CatalogNavBean catNavBean = (CatalogNavBean) parentNavVec.elementAt(catIndex);
        	resultXml.append("<catalog ").append("order=\"").append(catNavBean.getOrder()).append("\">");
        	resultXml.append(cwUtils.esc4XML(catNavBean.getTitle()));
        	resultXml.append("</catalog>");
        }
        resultXml.append("</parent_catlog_lst>");
        
		return resultXml.toString();
	}
	
	public void insFaq(Connection con) throws SQLException {
		//insert question
		KnowQuestionDB knowQueDb = genKnowQueDBByParam(con, KnowQuestionDAO.QUE_TYPE_FAQ);
        KnowQuestionDAO knowQueDao = new KnowQuestionDAO();
        knowQueDb.setQue_answered_timestamp(knowModParam.getCur_time());
        knowQueDao.ins(con, knowQueDb);
        int queId = knowQueDb.getQue_id();
        ViewKnowCatalogRelationDAO.updCatQueCount(con, knowQueDb.getQue_kca_id(), 1);

        // add records to knowCatalogRelation
        insQueRelation(con, knowQueDb.getQue_kca_id(), queId, prof.usr_id);
        
        // insert answer
        KnowAnswerDB knowAnsDb = genKnowAnsDBByParam(con);
        knowAnsDb.setAns_create_timestamp(knowModParam.getCur_time());
        knowAnsDb.setAns_update_timestamp(knowModParam.getCur_time());
        knowAnsDb.setAns_que_id(queId);
        knowAnsDb.setAns_right_ind(true);
        KnowAnswerDAO knowAnsDao = new KnowAnswerDAO();
        knowAnsDao.ins(con, knowAnsDb);
	}
	
	public String getFaq(Connection con) throws SQLException {
		StringBuffer resultXml = new StringBuffer();
		
		int queId = knowModParam.getQue_id();
        KnowQuestionDB knowQueDb = new KnowQuestionDB();
        knowQueDb.setQue_id(queId);
        KnowQuestionDAO knowQueDao = new KnowQuestionDAO();
        knowQueDao.getQueByQueId(con, knowQueDb);
        
        //general question xml
        resultXml.append("<que id=\"").append(queId).append("\">");
        resultXml.append("<title>").append(cwUtils.esc4XML(knowQueDb.getQue_title())).append("</title>");
        resultXml.append("<content>").append(cwUtils.esc4XML(knowQueDb.getQue_content())).append("</content>");
        //general answer xml
        String ansContent = KnowAnswerDAO.getAnsContentByQueId(con, queId);
        resultXml.append("<ans_content>").append(cwUtils.esc4XML(ansContent)).append("</ans_content>");
        resultXml.append("</que>");
        
        resultXml.append(getLastUpdateDate(knowQueDb.getQue_update_timestamp()));
        
        //general the full path of catalog
        knowModParam.setKca_id(knowQueDb.getQue_kca_id());
        resultXml.append(getInsFaqPrepXml(con));
        
		return resultXml.toString();
	}

	private String getLastUpdateDate(Timestamp lastUpdateTimestamp) {
		return "<update_timestamp>" + lastUpdateTimestamp + "</update_timestamp>";
	}
	
	public void updFaq(Connection con) throws SQLException {
		KnowQuestionDB queDb = genKnowQueDBByParam(con, KnowQuestionDAO.QUE_TYPE_FAQ);
        KnowQuestionDAO knowQueDao = new KnowQuestionDAO();
        knowQueDao.upd(con, queDb);
        
        String columnName[]={"ans_content"};
        String columnValue[]={knowModParam.getAns_content()};
        String condition = "ans_que_id= " + knowModParam.getQue_id();
        cwSQL.updateClobFields(con, "knowAnswer",columnName,columnValue, condition);
	}
	
}