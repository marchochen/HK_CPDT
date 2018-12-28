package com.cw.wizbank.JsonMod.know;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.know.dao.KnowAnswerDAO;
import com.cw.wizbank.know.dao.KnowCatalogDAO;
import com.cw.wizbank.know.dao.KnowCatalogRelationDAO;
import com.cw.wizbank.know.dao.KnowQuestionDAO;
import com.cw.wizbank.know.dao.KnowVoteDetailDAO;
import com.cw.wizbank.know.db.KnowAnswerDB;
import com.cw.wizbank.know.db.KnowQuestionDB;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.util.WordsFilter;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;

/**
 * @author DeanChen
 * 
 */
public class KnowModuleAccess {

    private Connection con = null;

    private KnowModuleParam knowModParam = null;

    private loginProfile prof = null;

    private WizbiniLoader wizbIni = null;

    private String cmd = null;

    /**
     * constructor
     * 
     * @param knowModParam
     * @param prof
     * @param wizbIni
     */
    public KnowModuleAccess(Connection con, KnowModuleParam knowModParam,
            loginProfile prof, WizbiniLoader wizbIni) {
        this.con = con;
        this.knowModParam = knowModParam;
        this.prof = prof;
        this.wizbIni = wizbIni;
        if (knowModParam != null) {
            cmd = knowModParam.getCmd();
        }
    }

    public void process() throws cwSysMessage, SQLException, cwException {
        if (cmd == null || "".equals(cmd)) {
            return;
        }

        if (cmd.equalsIgnoreCase(KnowModule.INS_QUE)) {
            insQue();
        } else if (cmd.equalsIgnoreCase(KnowModule.INS_ANS)) {
            insAns();
        } else if (cmd.equalsIgnoreCase(KnowModule.GET_QUE_DETAIL)) {
            getQueDetail();
        } else if (cmd.equalsIgnoreCase(KnowModule.UPD_ANS_RIGHT)) {
            updAnsRight();
        } else if (cmd.equalsIgnoreCase(KnowModule.CANCEL_QUE)) {
            cancelQue();
        } else if (cmd.equalsIgnoreCase(KnowModule.VOTE_ANS)) {
            voteAns();
        } else if (cmd.equalsIgnoreCase(KnowModule.UPD_QUE)) {
            updQue();
        } else if (cmd.equalsIgnoreCase(KnowModule.DEL_QUE)) {
        	delQue();
        } else if (cmd.equalsIgnoreCase(KnowModule.GET_KCA_LST)) {
        	getKcaLst();
        } else if (cmd.equalsIgnoreCase(KnowModule.INS_KCA_PREP)) {
        	insKcaPrep();
        } else if (cmd.equalsIgnoreCase(KnowModule.UPD_KCA_PREP)) {
        	updKcaPrep();
        } else if (cmd.equalsIgnoreCase(KnowModule.INS_KCA_EXEC)) {
        	insKca();
        } else if (cmd.equalsIgnoreCase(KnowModule.UPD_KCA_EXEC)) {
        	updKca();
        } else if (cmd.equalsIgnoreCase(KnowModule.DEL_KCA)) {
        	delKca();
        } else if (cmd.equalsIgnoreCase(KnowModule.CHANGE_KCA_EXEC)) { // 改变目录
        	changeKca();
        } else if (cmd.equalsIgnoreCase(KnowModule.INS_FAQ_PREP)) {
        	insFaqPrep();
		} else if (cmd.equalsIgnoreCase(KnowModule.INS_FAQ)) {
			insFaq();
		} else if (cmd.equalsIgnoreCase(KnowModule.GET_FAQ)) {
			getFaq();
		} else if (cmd.equalsIgnoreCase(KnowModule.UPD_FAQ)) {
			updFaq();
		}
    }

    private void insQue() throws cwSysMessage {
        // validate whether it exists the filter words in the question content
        // and question title.
        String titleAndContent = knowModParam.getQue_title() + " "
                + knowModParam.getQue_content();
        checkFilterWord(titleAndContent);
    }

    private void insAns() throws cwSysMessage, cwException, SQLException {
        // whether the question is exists.
        int queId = knowModParam.getAns_que_id();
        checkVaildQueId(queId);

        // validate whether it exists the filter words in the question content
        // and question title.
        String ansContent = knowModParam.getAns_content() + " " + knowModParam.getAns_refer_content();
        checkFilterWord(ansContent);

        KnowQuestionDB queDb = new KnowQuestionDB();
        queDb.setQue_id(queId);
        KnowQuestionDAO queDao = new KnowQuestionDAO();

        // check whether question is exists.
        boolean isExists = queDao.getQueByQueId(con, queDb);
        if (!isExists) {
            throw new cwSysMessage(KnowModule.QUE_NOT_EXIST);
        }

        // check whether the question is answered.
        String queType = queDb.getQue_type();
        if (KnowQuestionDAO.QUE_TYPE_SOLVED.equals(queType)) {
            throw new cwSysMessage(KnowModule.SOLVED_QUE_CANNOT_ANSED);
        }
    }

    private void getQueDetail() throws cwException, SQLException, cwSysMessage {
        // whether the question is exists.
        int queId = knowModParam.getQue_id();
        checkVaildQueId(queId);

        // check whether question is deleted.
        KnowQuestionDB knowQueDb = new KnowQuestionDB();
        knowQueDb.setQue_id(queId);
        KnowQuestionDAO queDao = new KnowQuestionDAO();
        boolean isExists = queDao.getQueByQueId(con, knowQueDb);
        if (!isExists) {
            throw new cwSysMessage(KnowModule.QUE_NOT_EXIST);
        }
    }

	private void checkVaildQueId(int queId) throws cwException {
		if (queId <= 0) {
            throw new cwException("The question id is wrong. id : " + knowModParam.getQue_id());
        }
	}

    private void updAnsRight() throws SQLException, cwSysMessage, cwException {
        int ansId = knowModParam.getAns_id();
        if (ansId <= 0) {
            throw new cwException("The answer id is wrong. id : " + ansId);
        }

        KnowAnswerDAO ansDao = new KnowAnswerDAO();
        KnowAnswerDB ansDb = new KnowAnswerDB();
        ansDb.setAns_id(ansId);
        boolean isExist = ansDao.get(con, ansDb);
        if (!isExist) {
            throw new cwSysMessage(KnowModule.ANS_NOT_EXIST);
        }

        // set ans_que_id and creator for logic operation
        int queId = ansDb.getAns_que_id();
        knowModParam.setAns_que_id(queId);
        knowModParam.setAns_create_ent_id(ansDb.getAns_create_ent_id());

        // check whether question is deleted.
        KnowQuestionDB knowQueDb = new KnowQuestionDB();
        knowQueDb.setQue_id(queId);
        KnowQuestionDAO queDao = new KnowQuestionDAO();
        boolean isExists = queDao.getQueByQueId(con, knowQueDb);
        if (!isExists) {
            throw new cwSysMessage(KnowModule.QUE_NOT_EXIST);
        }

        // check whether current user has modify Privilege
        // or current user is creator of question.
        boolean isQueCreator = queDao.isQueCreator(con, knowQueDb);
        if (!isQueCreator) {
            throw new cwSysMessage("ACL002");
        }

        // check whether question has been set right answer.
        if (knowQueDb.getQue_type().equals(KnowQuestionDAO.QUE_TYPE_SOLVED)) {
            throw new cwSysMessage(KnowModule.REPEAT_SET_RIGHT_ANS);
        }
    }

    private void cancelQue() throws SQLException, cwSysMessage, cwException {
        // whether the question is exists.
        int queId = knowModParam.getQue_id();
        checkVaildQueId(queId);

        KnowQuestionDB queDb = new KnowQuestionDB();
        queDb.setQue_id(queId);
        queDb.setQue_create_ent_id((int) prof.usr_ent_id);
        KnowQuestionDAO queDao = new KnowQuestionDAO();

        // check whether question is exists.
        boolean isExists = queDao.getQueByQueId(con, queDb);
        if (!isExists) {
            throw new cwSysMessage(KnowModule.QUE_NOT_EXIST);
        }

        String queType = queDb.getQue_type();
        if (KnowQuestionDAO.QUE_TYPE_SOLVED.equals(queType)) {
        	throw new cwSysMessage(KnowModule.SOLVED_QUE_CANNOT_CANCELED);
        }

        // check whether current user is creator of question.
        boolean isQueCreator = queDao.isQueCreator(con, queDb);
        if (!isQueCreator) {
            throw new cwSysMessage("ACL002");
        }
    }

    private void voteAns() throws cwSysMessage, SQLException, cwException {
        int ansId = knowModParam.getAns_id();
        if (ansId <= 0) {
            throw new cwException("The answer id is wrong. id : " + ansId);
        }

        // one user can only vote one time on one right.
        KnowAnswerDB ansDb = new KnowAnswerDB();
        ansDb.setAns_id(knowModParam.getAns_id());

        KnowAnswerDAO knowAnsDao = new KnowAnswerDAO();
        knowAnsDao.get(con, ansDb);

        // check whether the question is exists.
        int queId = ansDb.getAns_que_id();
        KnowQuestionDB queDb = new KnowQuestionDB();
        queDb.setQue_id(queId);
        KnowQuestionDAO queDao = new KnowQuestionDAO();
        boolean isExists = queDao.getQueByQueId(con, queDb);
        if (!isExists) {
            throw new cwSysMessage(KnowModule.QUE_NOT_EXIST);
        }

        // whether the vote is overdue.
        boolean isOverdue = KnowQuestionDAO.isOverdueVote(con, queId, wizbIni.zdVoteDuration);
        if (isOverdue) {
            throw new cwSysMessage(KnowModule.VOTE_IS_CLOSED);
        }

        // check whether current user repeats vote.
        boolean hasVote = KnowVoteDetailDAO.isExistVote(con, ansDb.getAns_que_id(), knowModParam.getAns_id(), (int) prof.usr_ent_id);
        if (hasVote) {
            throw new cwSysMessage(KnowModule.REPEAT_VOTE);
        }
    }

    private void updQue() throws SQLException, cwSysMessage, cwException {
        // validate whether it exists the filter words in the question content
        // and question title.
        String titleAndContent = knowModParam.getQue_content();
        checkFilterWord(titleAndContent);

        int queId = knowModParam.getQue_id();
        checkVaildQueId(queId);
        checkLastUpdateDate();
        
        KnowQuestionDB queDb = new KnowQuestionDB();
        queDb.setQue_id(queId);
        queDb.setQue_create_ent_id((int) prof.usr_ent_id);
        KnowQuestionDAO queDao = new KnowQuestionDAO();
        
        // check whether the question is exists.
        boolean isExists = queDao.getQueByQueId(con, queDb);
        if (!isExists) {
            throw new cwSysMessage(KnowModule.QUE_NOT_EXIST);
        }

        // check whether current user is creator of question.
        boolean isQueCreator = queDao.isQueCreator(con, queDb);
        if (!isQueCreator) {
            throw new cwSysMessage("ACL002");
        }

        // check whether the question is unanswered.
        if (queDb.getQue_type().equals(KnowQuestionDAO.QUE_TYPE_SOLVED)) {
            throw new cwSysMessage(KnowModule.SOLVED_QUE_CANNOT_UPDATED);
        }
    }

	private void checkLastUpdateDate() throws cwException, SQLException, cwSysMessage {
		String updTimeStr = knowModParam.getQue_update_timestamp();
        Timestamp queUpdateDate = null;
        if(updTimeStr != null && !"".equals(updTimeStr)) {
            queUpdateDate= Timestamp.valueOf(updTimeStr);
        }
        if (queUpdateDate == null) {
            throw new cwException("The parameter 'que_update_timestamp' is null.");
        }
        // check whether the question has been updated.
        boolean hasUpdated = KnowQuestionDAO.hasUpdated(con, knowModParam.getQue_id(), queUpdateDate);
        if(hasUpdated) {
            throw new cwSysMessage("GEN006");
        }
	}

	private void checkFilterWord(String titleAndContent) throws cwSysMessage {
		boolean isContainFilterWord = WordsFilter.isContainFilterWords(titleAndContent, wizbIni.filterWordsVec);
        if (isContainFilterWord) {
            throw new cwSysMessage(KnowModule.SILLY_INVESTMENT);
        }
	}
    
    private void securityCheck() throws SQLException, cwSysMessage {
		
	}

	private void getKcaLst() throws SQLException, cwSysMessage {
		securityCheck();
	}
	
	private void insKcaPrep() throws SQLException, cwSysMessage {
		securityCheck();
	}
	
	private void updKcaPrep() throws SQLException, cwSysMessage {
		securityCheck();
		
		int kca_id = this.knowModParam.getKca_id(); 
		isExistCatalog(kca_id);
	}
	
	private void insKca() throws SQLException, cwSysMessage {
		securityCheck();

		String kca_title = this.knowModParam.getKca_title(); // 目录名检查
        if (KnowCatalogDAO.isCatalogTitleExists(con, kca_title.trim(), 0, prof.root_ent_id)) {
        	throw new cwSysMessage(KnowModule.CAT_TITLE_EXISTS);
        }
	}
	
	private void updKca() throws SQLException, cwSysMessage {
		securityCheck();
		
		int kca_id = this.knowModParam.getKca_id();
		isExistCatalog(kca_id);

        String updTimeStr = knowModParam.getKca_upd_timestamp(); // 时间同步检查
		Timestamp kcaUpdateDate = null;
		if (updTimeStr != null && !"".equals(updTimeStr)) {
			kcaUpdateDate = Timestamp.valueOf(updTimeStr);
		}
		if (kcaUpdateDate == null) {
			throw new cwSysMessage("GEN000");
		}
        boolean hasUpdated = KnowCatalogDAO.hasUpdated(con, kca_id, kcaUpdateDate);
        if(hasUpdated) {
            throw new cwSysMessage(KnowModule.CANNOT_UPD_CAT);
        }
        
        String kca_title = this.knowModParam.getKca_title(); // 目录名检查
        if (KnowCatalogDAO.isCatalogTitleExists(con, kca_title.trim(), kca_id, prof.root_ent_id)) {
        	throw new cwSysMessage(KnowModule.CAT_TITLE_EXISTS);
        }
        
	}

	private void isExistCatalog(int kcaId) throws SQLException, cwSysMessage {
		if (kcaId == 0 || !KnowCatalogDAO.isCatalogExists(con, kcaId)) { // 检查目录是否存在
			throw new cwSysMessage(KnowModule.CAT_NOT_EXIST);
		}
	}
	
	private void delKca() throws SQLException, cwSysMessage {
		securityCheck();
		
		int kca_id = this.knowModParam.getKca_id(); 
		isExistCatalog(kca_id);
		
		String updTimeStr = knowModParam.getKca_upd_timestamp(); // 时间同步检查
		Timestamp kcaUpdateDate = null;
		if (updTimeStr != null && !"".equals(updTimeStr)) {
			kcaUpdateDate = Timestamp.valueOf(updTimeStr);
		}
		if (kcaUpdateDate == null) {
			throw new cwSysMessage("GEN000");
		}
        boolean hasUpdated = KnowCatalogDAO.hasUpdated(con, kca_id, kcaUpdateDate);
        if(hasUpdated) {
            throw new cwSysMessage(KnowModule.CANNOT_UPD_CAT);
        }
        
        if (KnowCatalogRelationDAO.isCatalogHasQue(con, kca_id)) {
        	throw new cwSysMessage(KnowModule.HAS_SUB_CAT_OR_QUE);
        }  
        
        if (KnowCatalogRelationDAO.isCatalogHasChildCatalog(con, kca_id)) {
        	throw new cwSysMessage(KnowModule.HAS_SUB_CAT_OR_QUE);
        }
         
	}
	
	private void delQue() throws SQLException, cwSysMessage {
		securityCheck();
		
		String que_id_lst = this.knowModParam.getQue_id_lst();
		String kcr_create_timestamp_lst = this.knowModParam.getKcr_create_timestamp_lst();
		if (que_id_lst == null || que_id_lst.length() <= 0) {
			throw new cwSysMessage("GEN000");
		}

		if (kcr_create_timestamp_lst == null || kcr_create_timestamp_lst.length() <= 0) {
			throw new cwSysMessage("GEN000");
		}
		
		String[] que_ids = null; 
		String[] que_cts = null; 
		if (que_id_lst!= null) {
			que_ids = cwUtils.splitToString(que_id_lst, "~");
		}
		
		if (kcr_create_timestamp_lst != null) {
			que_cts = cwUtils.splitToString(kcr_create_timestamp_lst, "~");
		}
		
		// 同步检查
		boolean pass = true;
		if (que_ids != null) {
			for (int i = 0; i < que_ids.length; i++) {
				String que_id = que_ids[i];
				String kcr_create_timestamp = que_cts[i];
				Timestamp kcrUpdateDate = null;
				if (kcr_create_timestamp != null && !"".equals(kcr_create_timestamp)) {
					kcrUpdateDate = Timestamp.valueOf(kcr_create_timestamp);
				}
				if (KnowQuestionDAO.hasKcrUpdated(con, new Integer(que_id).intValue(), kcrUpdateDate)) {
					pass = false;
					break;
				}
			}
		}
		if (!pass) {
			throw new cwSysMessage(KnowModule.CANNOT_DEL_QUE);
		}
	}
	
	// 改变目录的检查采用与删除一样的操作
	public void changeKca() throws SQLException, cwSysMessage {
		delQue();
		
		int kca_id = this.knowModParam.getKca_id();
		if (kca_id <= 0) {
			throw new cwSysMessage("The parameter 'kca_id' is null.");
		}
	}
	
	private void insFaqPrep() throws SQLException, cwSysMessage {
		//whether the catalog is exist
		isExistCatalog(knowModParam.getKca_id());
	}
	
	private void insFaq() throws cwSysMessage, SQLException {
		isExistCatalog(knowModParam.getQue_kca_id());
		// validate whether it exists the filter words in the question content and question title.
		String titleAndContent = knowModParam.getQue_title() + " "+ knowModParam.getQue_content() + " " + knowModParam.getAns_content();
        checkFilterWord(titleAndContent);
	}
	
	private void getFaq() throws cwException {
		checkVaildQueId(knowModParam.getQue_id());
	}
	
	private void updFaq() throws cwException, cwSysMessage, SQLException {
		checkVaildQueId(knowModParam.getQue_id());
		String filteredContent = knowModParam.getQue_content() + " " + knowModParam.getAns_content(); 
		checkFilterWord(filteredContent);
		checkLastUpdateDate();
	}
}
