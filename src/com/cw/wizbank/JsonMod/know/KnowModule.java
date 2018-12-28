package com.cw.wizbank.JsonMod.know;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Vector;

import net.sf.json.JSONArray;

import com.cw.wizbank.ServletModule;
import com.cw.wizbank.JsonMod.ErrorMsg;
import com.cw.wizbank.JsonMod.know.bean.QueSearchCriteriaBean;
import com.cw.wizbank.util.JsonHelper;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;

/**
 * @author DeanChen at 2008-07-31
 */
public class KnowModule extends ServletModule {
	
	public static final String MOD_NAME = "know";

    public static final String INS_QUE = "INS_QUE";

    public static final String GET_QUE = "GET_QUE";

    public static final String UPD_QUE = "UPD_QUE";

    public static final String INS_ANS = "INS_ANS";

    public static final String UPD_ANS_RIGHT = "UPD_ANS_RIGHT";

    public static final String VOTE_ANS = "VOTE_ANS";

    public static final String CANCEL_QUE = "CANCEL_QUE";

    public static final String GET_MY_RECORD = "GET_MY_RECORD";

    public static final String GET_MY_STATIS = "GET_MY_STATIS";

    public static final String GET_KNOW_HOME = "GET_KNOW_HOME";

    public static final String GET_QUE_LST = "GET_QUE_LST";

    public static final String GET_CATALOG_DETAIL = "GET_CATALOG_DETAIL";
    
    public static final String GET_CATALOG_STRUCT = "GET_CATALOG_STRUCT";

    public static final String SEARCH_QUE = "SEARCH_QUE";

    public static final String GET_QUE_DETAIL = "GET_QUE_DETAIL";

    public static final String GET_KNOW_HELP = "GET_KNOW_HELP";
    
    public static final String GET_KCA_LST = "GET_KCA_LST";
    public static final String GET_KCA_LST_XML = "GET_KCA_LST_XML";
    
    public static final String INS_KCA_PREP = "INS_KCA_PREP";
    public static final String INS_KCA_PREP_XML = "INS_KCA_PREP_XML";
    
    public static final String INS_KCA_EXEC = "INS_KCA_EXEC";
	
	public static final String UPD_KCA_PREP = "UPD_KCA_PREP";
	public static final String UPD_KCA_PREP_XML = "UPD_KCA_PREP_XML";
	
	public static final String UPD_KCA_EXEC = "UPD_KCA_EXEC";
	
	public static final String DEL_KCA = "DEL_KCA";
	
	public static final String GET_KCA_TREE = "GET_KCA_TREE";
	
	public static final String CHANGE_KCA_EXEC = "CHANGE_KCA_EXEC";
	
	public static final String DEL_QUE = "DEL_QUE";
	
	public static final String SEARCH_QUE_FOR_MGT = "SEARCH_QUE_FOR_MGT";
	public static final String SEARCH_QUE_FOR_MGT_XML = "SEARCH_QUE_FOR_MGT_XML";
	
	public static final String INS_FAQ_PREP = "INS_FAQ_PREP";
	
	public static final String INS_FAQ_PREP_XML = "INS_FAQ_PREP_XML";
	
	public static final String INS_FAQ = "INS_FAQ";
	
	public static final String GET_FAQ = "GET_FAQ";
	
	public static final String GET_FAQ_XML = "GET_FAQ_XML";
	
	public static final String UPD_FAQ = "UPD_FAQ";
	
    // for prompt information

    public static final String INS_QUE_SUCCESS = "KNOW01";

    public static final String UPD_QUE_SUCCESS = "KNOW02";

    public static final String DEL_QUE_SUCCESS = "KNOW03";

    public static final String SOLVED_QUE_CANNOT_ANSED = "KNOW04";
    
    public static final String SOLVED_QUE_CANNOT_CANCELED = "KNOW05";
    
    public static final String SOLVED_QUE_CANNOT_UPDATED = "KNOW06";

    public static final String QUE_NOT_EXIST = "KNOW07";
    
    public static final String ANS_NOT_EXIST = "KNOW08";

    public static final String REPEAT_VOTE = "KNOW09";

    public static final String VOTE_IS_CLOSED = "KNOW10";

    public static final String REPEAT_SET_RIGHT_ANS = "KNOW11";

    public static final String SILLY_INVESTMENT = "KNOW12";

    public static final String NOT_ENOUGH_CREDITS = "KNOW13";
    
    public static final String NO_MGT_CAT = "KNOW14"; // 没有可以管理的知道目录

    public static final String CAT_NOT_EXIST = "GEN000"; // 目录不存在 
    
    public static final String CANNOT_UPD_CAT = "GEN006";
    
    public static final String CANNOT_DEL_QUE = "GEN006";
    
    public static final String HAS_SUB_CAT_OR_QUE = "KNOW20"; // 存在子目录或者问题
    
    public static final String CAT_TITLE_EXISTS = "KNOW15"; // 目录名已经被使用
    
    public static final String INS_KCA_SUCCESS = "KNOW16";

    public static final String UPD_KCA_SUCCESS = "KNOW17";

    public static final String DEL_KCA_SUCCESS = "KNOW18";
    
    public static final String CHA_QUE_SUCCESS = "KNOW19";
    
    KnowModuleParam modParam;
    
    public KnowModule() {
        super();
        modParam = new KnowModuleParam();
        param = modParam;
    }

    public void process() throws SQLException, IOException, cwException {
        
        try {
                if (prof == null || prof.usr_ent_id == 0) {// 若未登录
                	throw new cwException(cwUtils.MESSAGE_SESSION_TIMEOUT);
                } else {
                    // for access control
                    KnowModuleAccess knowModAc = new KnowModuleAccess(con,modParam, prof, wizbini);
                    knowModAc.process();
                    Know know = null;
                    if (modParam.getCmd().equalsIgnoreCase(INS_QUE)) {
                        know = new Know(modParam, prof, wizbini);
                        know.insKownQuestion(con);
                        sysMsg = getErrorMsg(INS_QUE_SUCCESS,param.getUrl_success());
                    } else if (modParam.getCmd().equalsIgnoreCase(GET_QUE)) {
                        know = new Know(modParam, prof);
                        know.getKownQuestion(con, resultJson);
                        know.getKnowCatalogStructure(con, resultJson);
                        JsonHelper.disableEsc4JsonAll(defJsonConfig);
                    } else if (modParam.getCmd().equalsIgnoreCase(UPD_QUE)) {
                        know = new Know(modParam, prof, wizbini);
                        know.updKownQuestion(con);
                        sysMsg = getErrorMsg(UPD_QUE_SUCCESS, modParam.getUrl_success());
                    } else if (modParam.getCmd().equalsIgnoreCase(INS_ANS)) {
                        know = new Know(modParam, prof, wizbini);
                        know.insKnowAnswer(con);
                        redirectUrl = modParam.getUrl_success();
                        
                    } else if (modParam.getCmd().equalsIgnoreCase(UPD_ANS_RIGHT)) {
                        know = new Know(modParam, prof);
                        know.updAnswerRight(con);
                        redirectUrl = modParam.getUrl_success();
                        
                    } else if (modParam.getCmd().equalsIgnoreCase(VOTE_ANS)) {
                        know = new Know(modParam, prof);
                        know.updAnswerVote(con);
                        redirectUrl = modParam.getUrl_success();
                        
                    } else if (modParam.getCmd().equalsIgnoreCase(CANCEL_QUE)) {
                        know = new Know(modParam, prof);
                        know.cancelQuestion(con);
                        sysMsg = getErrorMsg(DEL_QUE_SUCCESS, modParam.getUrl_success());
                    } else if (modParam.getCmd().equalsIgnoreCase(GET_MY_RECORD)) {
                        know = new Know(modParam, prof);
                        know.getMyRecord(con, resultJson);
                    } else if (modParam.getCmd().equalsIgnoreCase(GET_MY_STATIS)) {
                        know = new Know(modParam, prof);
                        know.getKnowStatisticsByUser(con, resultJson);
                    } else if (modParam.getCmd().equalsIgnoreCase(GET_KNOW_HOME)) {
                        know = new Know(modParam, prof);
                        String itmDir = wizbini.cfgSysSetupadv.getFileUpload().getItmDir().getName();
                        know.getKnowHomePage(con, wizbini, prof.cur_lan, resultJson, itmDir);
                    } else if (modParam.getCmd().equalsIgnoreCase(GET_QUE_LST)) {
                        know = new Know(modParam, prof);
                        String itmDir = wizbini.cfgSysSetupadv.getFileUpload().getItmDir().getName();
                        know.getQueList(con, wizbini, prof.cur_lan, resultJson, itmDir);
                    } else if (modParam.getCmd().equalsIgnoreCase(GET_CATALOG_STRUCT)) {
                        know = new Know(modParam, prof);
                        know.getKnowCatalogStructure(con, resultJson);
                    } else if (modParam.getCmd().equalsIgnoreCase(GET_CATALOG_DETAIL)) {
                        know = new Know(modParam, prof);
                        know.getKnowCatalogStructDetail(con, resultJson);
                    } else if (modParam.getCmd().equalsIgnoreCase(SEARCH_QUE)) {
                        know = new Know(modParam, prof);
                        QueSearchCriteriaBean criteriaBean = know.getQueSrhBeanByParam(sess);
                        know.searchQue(con, criteriaBean, resultJson);
                    } else if (modParam.getCmd().equalsIgnoreCase(GET_QUE_DETAIL)) {
                        know = new Know(modParam, prof, wizbini);
                        String itmDir = wizbini.cfgSysSetupadv.getFileUpload().getItmDir().getName();
                        know.getQueDetail(con, wizbini, prof.cur_lan, resultJson, itmDir);
                    } else if (modParam.getCmd().equalsIgnoreCase(GET_KNOW_HELP)) {
                        know = new Know(modParam, prof, wizbini);
                        know.getKnowHelp(con, resultJson);
                    } 
					else if (modParam.getCmd().equalsIgnoreCase(GET_KCA_LST)
							|| modParam.getCmd().equalsIgnoreCase(GET_KCA_LST_XML)) { // 管理后台
						know = new Know(modParam, prof, wizbini);
						resultXml = formatXML(know.getKnowCatalogList(con), MOD_NAME);
					} else if (modParam.getCmd().equalsIgnoreCase(INS_KCA_PREP)
							|| modParam.getCmd().equalsIgnoreCase(INS_KCA_PREP_XML)) { // 添加目录页面
						know = new Know(modParam, prof);
						resultXml = formatXML(know.getCatalogInfoAsXML(con), MOD_NAME);
					} else if (modParam.getCmd().equalsIgnoreCase(INS_KCA_EXEC)) { // 添加目录
						know = new Know(modParam, prof, wizbini);
						know.insKownCatalog(con);
						sysMsg = getErrorMsg(INS_KCA_SUCCESS, modParam.getUrl_success());
					} else if (modParam.getCmd().equalsIgnoreCase(UPD_KCA_PREP)
							|| modParam.getCmd().equalsIgnoreCase(UPD_KCA_PREP_XML)) { // 修改目录页面
						know = new Know(modParam, prof);
						resultXml = formatXML(know.getCatalogInfoAsXML(con), MOD_NAME);
					} else if (modParam.getCmd().equalsIgnoreCase(UPD_KCA_EXEC)) { // 修改目录
						know = new Know(modParam, prof, wizbini);
						know.updKownCatalog(con);
						sysMsg = getErrorMsg(UPD_KCA_SUCCESS, modParam.getUrl_success());
					} else if (modParam.getCmd().equalsIgnoreCase(DEL_KCA)) { // 删除目录
						know = new Know(modParam, prof, wizbini);
						know.delKownCatalog(con);
						sysMsg = getErrorMsg(DEL_KCA_SUCCESS, modParam.getUrl_success());
					} else if (modParam.getCmd().equalsIgnoreCase(GET_KCA_TREE)) { // 目录列表
						know = new Know(modParam, prof);
						Vector vec = know.getKnowCatalogTree(con);
						JSONArray ja = JSONArray.fromObject(vec, defJsonConfig);
						out.print(ja);
						return;
					} else if (modParam.getCmd().equalsIgnoreCase(CHANGE_KCA_EXEC)) { // 改变目录
						know = new Know(modParam, prof, wizbini);
						know.changeKca(con);
						sysMsg = getErrorMsg(CHA_QUE_SUCCESS, modParam.getUrl_success());
					} else if (modParam.getCmd().equalsIgnoreCase(DEL_QUE)) { // 删除问题
						know = new Know(modParam, prof, wizbini);
						know.delKownQuestion(con);
						sysMsg = getErrorMsg(DEL_QUE_SUCCESS, modParam.getUrl_success());
					} else if (modParam.getCmd().equalsIgnoreCase(SEARCH_QUE_FOR_MGT)
							|| modParam.getCmd().equalsIgnoreCase(SEARCH_QUE_FOR_MGT_XML)) { // 搜索
						know = new Know(modParam, prof);
						resultXml = formatXML(know.getSearchResultAsXML(con, know.getSimpleSrhKey(sess)), MOD_NAME);
					} else if (modParam.getCmd().equalsIgnoreCase(INS_FAQ_PREP) || modParam.getCmd().equalsIgnoreCase(INS_FAQ_PREP_XML)) {
						know = new Know(modParam, prof);
						resultXml = formatXML(know.getInsFaqPrepXml(con), MOD_NAME);
					} else if (modParam.getCmd().equalsIgnoreCase(INS_FAQ)) {
						know = new Know(modParam, prof);
						know.insFaq(con);
						sysMsg = getErrorMsg(INS_QUE_SUCCESS,param.getUrl_success());
					} else if (modParam.getCmd().equalsIgnoreCase(GET_FAQ) || modParam.getCmd().equalsIgnoreCase(GET_FAQ_XML)) {
						know = new Know(modParam, prof);
						resultXml = formatXML(know.getFaq(con), MOD_NAME);
					} else if (modParam.getCmd().equalsIgnoreCase(UPD_FAQ)) {
						know = new Know(modParam, prof);
						know.updFaq(con);
						sysMsg = getErrorMsg(UPD_QUE_SUCCESS, modParam.getUrl_success());
                    } else {
                    	throw new cwException(cwUtils.MESSAGE_NO_RECOGNIZABLE_CMD + modParam.getCmd());
                    }
                }
        } catch (cwSysMessage se) {
            try {
                con.rollback();
                msgBox(ServletModule.MSG_STATUS, se, modParam.getUrl_failure(),
                        out);
            } catch (SQLException sqle) {
                out.println("SQL error: " + sqle.getMessage());
            }
        } 
    }
}
