package com.cw.wizbank.JsonMod;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Vector;
import org.apache.commons.beanutils.BeanUtils;
import com.cw.wizbank.ServletModule;
import com.cw.wizbank.JsonMod.study.Study;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwUtils;

public class ForumModule extends ServletModule {
	ForumModuleParam modParam;
	
	public ForumModule() {
		super();
		modParam = new ForumModuleParam();
		param = modParam;
	}
	
	public void process() throws SQLException, IOException, cwException {
		
		try {
				if(this.prof == null || this.prof.usr_ent_id == 0) {	// 若还是未登录
					throw new cwException(cwUtils.MESSAGE_SESSION_TIMEOUT);
				} else {// 若已经登陆
					
					if ("srh_forum_topic".equalsIgnoreCase(modParam.getCmd())) {	// 从高级搜索中搜索符合条件的论坛主题
						Forum forum = new Forum();
						
//						AcModule acmod = new AcModule(con);
//						if(!acmod.checkReadPermission(prof, courseModuleParam.getRes_id())) {
//							cwException ee = new cwException("Do not read the forum!");
//							
//							errMsg = new ErrorMsg(null, "NO_RIGHT_READ_MSG", "", "", ee.getMessage());
//							throw ee;
//						}
						
						// 从Session中获取搜索条件Map
						HashMap conditionMapFromSess = (HashMap)sess.getAttribute(modParam.getSearch_id());
						BeanUtils.populate(modParam, conditionMapFromSess);	// 将搜索条件Map派发至courseModuleParam
						
						String phrase = modParam.getPhrase();						// 论坛搜索关键字
						String created_by = modParam.getCreated_by();				// 发表人
						int search_type_topic = modParam.getSearch_type_topic();	// 是否按主题搜索(1：是 0：否)
						int search_type_msg = modParam.getSearch_type_msg();		// 是否按文章内容搜索(1：是 0：否)
						int created_after_days = modParam.getCreated_after_days();	// 搜索的天数
						
						Timestamp created_after = null;	// 搜索结束时间
						Timestamp created_before = null;// 搜索起始时间
						
						// select datediff(Day, '2008-9-10', dateadd(Day, 天数, getdate()));
						if (created_after_days > 0) {
//							created_after = cwSQL.getBeforeOrAfterDate(con, created_after_days, true);
							Timestamp cur_time = cwSQL.getTime(con);
							Calendar cal_today = Calendar.getInstance();
							cal_today.setTime(cur_time);
							cal_today.add(Calendar.DATE, -1 * created_after_days);
							created_after = new Timestamp(cal_today.getTimeInMillis());
							
							created_before = cur_time;
						}
						
//						String val = null;
//						val = courseModuleParam.getCreated_after();
//						if ( val!= null && val.trim().length()!= 0 ) {
//							created_after = Timestamp.valueOf(val);
//						}
//						
//						val = courseModuleParam.getCreated_before();
//						if ( val!= null && val.trim().length()!= 0 ) {
//							created_before = Timestamp.valueOf(val);
//						}
						
						String sort_order = modParam.getSort_order();				// 排序方式(可选值：asc、desc)
						
						HashMap forumMap = new HashMap();
						
						// 根据条件搜索
						Vector topicVec = forum.searchTopicAndMsg(con, phrase, created_by, search_type_topic, search_type_msg, created_after, created_before, sort_order, modParam);
						forumMap.put("total", new Long(modParam.getTotal_rec()));
						forumMap.put("for_topic_lst", topicVec);
						
						resultJson.put("forum", forumMap);
					} else if ("get_public_forum".equalsIgnoreCase(modParam.getCmd())) {	// 用户进入论坛
						Forum forum = new Forum();
						
						Vector forumVec = forum.getPublicForum(con, prof.usr_ent_id, prof.root_ent_id, modParam);
						
						HashMap forumMap = new HashMap();
						forumMap.put("total", new Long(modParam.getTotal_rec()));
						forumMap.put("for_topic_lst", forumVec);
						
						resultJson.put("forum", forumMap);
					} else {
						throw new cwException(cwUtils.MESSAGE_NO_RECOGNIZABLE_CMD + modParam.getCmd());
					}
				}	
		 } catch (qdbException e) {
			 throw new cwException(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			throw new cwException(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			throw new cwException(e.getMessage(), e);
		}
	}
	
}
