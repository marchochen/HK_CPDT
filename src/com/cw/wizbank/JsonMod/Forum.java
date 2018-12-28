package com.cw.wizbank.JsonMod;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.Vector;

import com.cw.wizbank.JsonMod.bean.ForumBean;
import com.cw.wizbank.JsonMod.bean.ForumTopicBean;
import com.cw.wizbank.qdb.dbForum;
import com.cw.wizbank.qdb.dbForumMessage;
import com.cw.wizbank.qdb.dbForumTopic;
import com.cw.wizbank.qdb.dbModule;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;

public class Forum {

	/**
	 * 查询某用户的相关论坛情况
	 * @param con
	 * @param usr_ent_id
	 * @param root_ent_id
	 * @return
	 * @throws SQLException
	 */
	public Vector getPublicForum(Connection con, long usr_ent_id, long root_ent_id, BaseParam param)
		throws SQLException {
		
		ForumModuleParam  forumModuleParam = (ForumModuleParam)param;
		
		Vector resultVec = new Vector();
		
		Hashtable topicNum = dbForum.getNumByForum(con, dbForum.SQL_GET_TOPIC_NUM_BY_FORUM);// 论坛中的主题数
        Hashtable msgNum = dbForum.getNumByForum(con, dbForum.SQL_GET_MSG_NUM_BY_FORUM);	// 论坛文章数
        Hashtable unreadMsgNum = dbForum.getNumByForum(con, dbForum.getUnreadMsgSQL(usr_ent_id));// 未读的论坛文章数
        
        Vector vtMod = dbModule.getPublicModLst(con, root_ent_id, "FOR", true, false, 0, null);
        
        int size = 0;
        if (vtMod != null && vtMod.size() > 0) {
        	
        	size = vtMod.size();
	        for (int count = 0; count < size; count++) {
	        	int end = (forumModuleParam.getStart() + forumModuleParam.getLimit() - 1);
	        	
	        	// 采用假分页技术
	        	if (count >= forumModuleParam.getStart() && count <= end) {
		        	dbModule dbmod = (dbModule) vtMod.elementAt(count);
		        	
		        	ForumBean forumBean = new ForumBean();
		        	forumBean.setFor_res_id(dbmod.mod_res_id);		// 论坛ID
		        	forumBean.setFor_res_title(dbmod.res_title);	// 论坛标题
		        	
		        	Object total = topicNum.get(new Long(dbmod.mod_res_id));
		        	forumBean.setFto_total((total != null) ? ((Long)total).longValue() : 0);	// 该论坛中的主题数
		        	
		        	total = msgNum.get(new Long(dbmod.mod_res_id));
		        	forumBean.setFmg_total((total != null) ? ((Long)total).longValue() : 0);	// 论坛文章数
		        	
		        	total = unreadMsgNum.get(new Long(dbmod.mod_res_id));
		        	forumBean.setFmg_unread_total((total != null) ? ((Long)total).longValue() : 0);	// 未读的论坛文章数
		        	
		        	resultVec.add(forumBean);
	        	}
	        } // end for
	        
	        forumModuleParam.setTotal_rec(size);
	        
        } // end outer if
        
		return resultVec;
	}
	
	/**
	 * 搜索符合条件的论坛主题或发表的文章
	 * @param con 数据库连接器
	 * @param phrase 论坛搜索关键字
	 * @param created_by 发表人
	 * @param search_type_topic 是否按主题搜索(1：是 0：否)
	 * @param search_type_msg 是否按文章内容搜索(1：是 0：否)
	 * @param created_after 搜索起始时间
	 * @param created_before 搜索结束时间
	 * @param sort_order 排序方式(可选值：asc、desc)
	 * @return 符合条件的论坛主题或发表的文章集合
	 * @throws SQLException
	 * @throws qdbException 
	 */
	public Vector searchTopicAndMsg(Connection con, String phrase, String created_by, 
			int search_type_topic, int search_type_msg, Timestamp created_after, Timestamp created_before, String sort_order, BaseParam param)
		throws SQLException, qdbException
	{
		Vector resultVector = new Vector();
		long[] allForums = getIdOfAllForums(con, true);
		if (search_type_topic == 1 && search_type_msg == 0) {
			resultVector = searchTopics(con, allForums, phrase, created_by, created_after, created_before, sort_order, param, true);
		} else if (search_type_msg == 1 && search_type_topic == 0) {
			resultVector = this.searchTopicsByForumMsg(con, allForums, phrase, created_by, created_after, created_before, sort_order, param, true);
		} else {
			resultVector = this.searchForums(con, allForums, phrase, created_by, created_after, created_before, sort_order, param, true);
		}

		return resultVector;
	}
	
	/**
	 * 按主题贴关键字获取符合搜索条件的论坛中的主题贴集
	 * @param con 数据库连接器
	 * @param resIds 主题帖ID集
	 * @param phrase 关键字
	 * @param created_by 创建人
	 * @param created_after 搜索起始时间
	 * @param created_before 搜索结束时间
	 * @param order 排序方式
	 * @return 符合条件的论坛中的主题贴集
	 * @throws SQLException
	 * @throws qdbException
	 */
	public Vector searchTopics(Connection con, long[] resIds, String phrase, String created_by,
			Timestamp created_after, Timestamp created_before, String sort_order, BaseParam param, boolean isPagination) throws SQLException, qdbException {
		
		Vector topicIdVector = new Vector();	// 主题帖ID集
		
		// 根据论坛ID集获取符合搜索条件的主题帖ID集
		if (resIds != null && resIds.length > 0) {
			long[] tempIds = null;
			
			for (int i = 0; i < resIds.length; i++) {
				long res_id = resIds[i];	// 获取某个论坛
				
				// 查询某个论坛ID为res_id中的符合条件的主题帖ID集
				tempIds = dbForumTopic.searchTopics(con, res_id, phrase, 2, created_by, 2, created_after, created_before, sort_order);
				
				if (tempIds != null && tempIds.length > 0) {
					for (int j = 0; j < tempIds.length; j++) {
						topicIdVector.addElement(new Long(tempIds[j]));
					}
				}
			} // end for
		}
		
		// 根据主题贴ID集构建元素为ForumTopicBean的集合，以用于前台JSON输出
		Vector forumTopicVector = this.contentWithoutMsgs(con, topicIdVector, param, isPagination);
		
		return forumTopicVector;
	}
	
	/**
	 * 按文章关键字获取符合搜索条件的论坛中的主题贴集
	 * @param con 数据库连接器
	 * @param resIds 主题帖ID集
	 * @param phrase 关键字
	 * @param created_by 创建人
	 * @param created_after 搜索起始时间
	 * @param created_before 搜索结束时间
	 * @param sort_order 排序方式
	 * @param isPagination 是否分页
	 * @return 符合条件的论坛中的主题贴集
	 * @throws SQLException
	 * @throws qdbException
	 */
	public Vector searchTopicsByForumMsg(Connection con, long[] resIds, String phrase, String created_by,
			Timestamp created_after, Timestamp created_before, String sort_order, BaseParam param, boolean isPagination) throws SQLException, qdbException {
		
		Vector allTopicIdVector = new Vector();	// 多个论坛中的所有主题帖ID集
		
		// 根据论坛ID集获取所有主题帖组成的ID集
		if (resIds != null && resIds.length > 0) {
			long[] tempIds = null;
			
			for (int i = 0; i < resIds.length; i++) {
				long res_id = resIds[i];	// 获取某个论坛ID
				
				// 获取某个论坛ID为res_id的所有主题贴ID
				tempIds = dbForumTopic.getAllTopicIDs(con, res_id, null, sort_order);
				
				// 装入该论坛下的所有主题贴ID
				if (tempIds != null && tempIds.length > 0) {
					for (int j = 0; j < tempIds.length; j++) {
						allTopicIdVector.addElement(new Long(tempIds[j]));
					}
				}
			} // end for
		}
			
		Vector topicIdOfExistMsgsInTopicVec = new Vector();	// 用于存放主题贴ID集，且该集合中的主题贴下存在符合搜索条件的文章
		long[] msgIds = null;	// 文章ID集
		if (allTopicIdVector != null) {
			for (int i = 0; i< allTopicIdVector.size(); i++) {
				long fto_id = ((Long)allTopicIdVector.elementAt(i)).longValue();	// 主题贴ID
				
				// 查询某个主题贴下的符合搜索条件的文章ID集
				msgIds = dbForumMessage.searchMsgs(con, fto_id, phrase, 1, created_by, 1, created_after, created_before, sort_order);

                if (msgIds != null && msgIds.length != 0) {	// 若该主题贴下存在被搜索到的文章ID
                	topicIdOfExistMsgsInTopicVec.addElement((Long)allTopicIdVector.elementAt(i));
                }
            } // end for
		}
		
		// 根据主题贴ID集构建元素为ForumTopicBean的集合，以用于前台JSON输出
		Vector topicOfExistMsgsInTopicVec = this.contentWithoutMsgs(con, topicIdOfExistMsgsInTopicVec, param, isPagination);
		
		return topicOfExistMsgsInTopicVec;
	}
	
	/**
	 * 按主题贴关键字和文章关键字获取符合搜索条件的论坛中的主题贴集
	 * @param con 数据库连接器
	 * @param resIds 主题帖ID集
	 * @param phrase 关键字
	 * @param created_by 创建人
	 * @param created_after 搜索起始时间
	 * @param created_before 搜索结束时间
	 * @param sort_order 排序方式
	 * @param isPagination 是否分页
	 * @return 符合搜索条件的论坛中的主题贴集
	 * @throws SQLException
	 * @throws qdbException
	 */
	public Vector searchForums(Connection con, long[] resIds, String phrase, String created_by,
			Timestamp created_after, Timestamp created_before, String sort_order, BaseParam param, boolean isPagination) throws SQLException, qdbException {
		
		Vector allTopicIdVector = new Vector();	// 主题帖ID集
		
		// 根据论坛ID集获取所有主题帖组成的ID集
		if (resIds != null && resIds.length > 0) {
			long[] tempIds = null;
			
			for (int i = 0; i < resIds.length; i++) {
				long res_id = resIds[i];
				
				// 获取某个论坛ID为res_id的所有主题贴ID
				tempIds = dbForumTopic.getAllTopicIDs(con, res_id, null, sort_order);
				
				// 装入该论坛下的所有主题贴ID
				if (tempIds != null && tempIds.length > 0) {
					for (int j = 0; j < tempIds.length; j++) {
						allTopicIdVector.addElement(new Long(tempIds[j]));
					}
				}
			} // end for
		}
		
		Vector idVector = new Vector();	// 最终的ID集
		
		if (allTopicIdVector.size() > 0) {
				
			// 按主题贴关键字获取符合搜索条件的论坛中的主题贴集
			Vector srhByTopicKeyVec = this.searchTopics(con, resIds, phrase, created_by, created_after, created_before, sort_order, param, false);
			long [] ids_topicKey = null;	// 获取按主题贴关键字搜索到的主题贴ID数组
			Vector idVec_topicKey = new Vector();
			int len_topicKey = (srhByTopicKeyVec != null ? srhByTopicKeyVec.size() : 0);
			if (len_topicKey > 0) {
				ids_topicKey = new long[len_topicKey];
				
				for (int i = 0; i < len_topicKey; i++) {
					ids_topicKey[i] = ((ForumTopicBean)srhByTopicKeyVec.elementAt(i)).getFto_id();
					idVec_topicKey.addElement(new Long(ids_topicKey[i]));
				}
			}
				
			// 按文章关键字获取符合搜索条件的论坛中的主题贴集
			Vector srhByMsgKeyVec = this.searchTopicsByForumMsg(con, resIds, phrase, created_by, created_after, created_before, sort_order, param, false);
			long [] ids_msgKey = null;
			Vector idVec_msgKey = new Vector();
			int len_msgKey = (srhByMsgKeyVec != null ? srhByMsgKeyVec.size() : 0);
			if (len_msgKey > 0) {
				ids_msgKey = new long[len_msgKey];
				
				for (int i = 0; i < len_msgKey; i++) {
					ids_msgKey[i] = ((ForumTopicBean)srhByMsgKeyVec.elementAt(i)).getFto_id();
					idVec_msgKey.addElement(new Long(ids_msgKey[i]));
				}
			}
			
			// 去除搜索到的重复主题ID
			if (allTopicIdVector.size() >= idVec_topicKey.size()) {
				
				for (int i = 0; i < allTopicIdVector.size(); i++) {
					long id = ((Long)allTopicIdVector.elementAt(i)).longValue();
					
					boolean isInclude = false;
					if (idVec_topicKey.contains(new Long(id))) {
						idVector.addElement(new Long(id));
						isInclude = true;
					} else if (isInclude == false && idVec_msgKey.contains(new Long(id))) {
						idVector.addElement(new Long(id));
					}
				} // end for
			} // end if
			
		} // end outer if
		
		return this.contentWithoutMsgs(con, idVector, param, isPagination);
	}
	
	/**
	 * 根据主题贴ID集构建元素为ForumTopicBean的集合，以用于前台JSON输出
	 * @param con 数据库连接器
	 * @param topicIdVector 主题贴ID集(其中的元素为Long类型)
	 * @param isPagination 是否分页
	 * @return 根据主题贴ID集构建元素为ForumTopicBean的集合
	 * @throws SQLException
	 */
	public Vector contentWithoutMsgs(Connection con, Vector topicIdVector, BaseParam param, boolean isPagination) throws SQLException {
		
		ForumModuleParam forumModuleParam = (ForumModuleParam)param;
		
		// 获取每个主题贴下的回贴数，并组织成Map
//		System.out.println(cwUtils.vector2list(topicIdVector));
		Vector forumTopicVector = new Vector();	// 主题贴Bean集
		
		if (topicIdVector != null && topicIdVector.size() > 0) {
			Hashtable msg_count_hash = dbForumMessage.getMsgCount(con, cwUtils.vector2list(topicIdVector));

			int len = topicIdVector.size();
			int end = (forumModuleParam.getStart() + forumModuleParam.getLimit() - 1);// 每页所显示记录的最大序号
			for(int i=0; i < len; i++){
				
				if (isPagination && i >= forumModuleParam.getStart() && i <= end) {	// 分页
		        	Long fto_id = (Long)topicIdVector.elementAt(i);
		        	Long count_l = (Long)msg_count_hash.get(fto_id);
		        	
		            long count = 0;
		            if (count_l != null) {
		                count = count_l.longValue();
		            }
		            
		            // 根据主题贴ID构建与其相关的ForumTopicBean
		            ForumTopicBean topicBean = getMsgByTopicId(con, fto_id.longValue());
		            topicBean.setFto_msg_cnt(count);	// 加入回复数
		            
		            forumTopicVector.addElement(topicBean);
				} else if (!isPagination) {	// 不分页
					Long fto_id = (Long)topicIdVector.elementAt(i);
		        	Long count_l = (Long)msg_count_hash.get(fto_id);
		        	
		            long count = 0;
		            if (count_l != null) {
		                count = count_l.longValue();
		            }
		            
		            // 根据主题贴ID构建与其相关的ForumTopicBean
		            ForumTopicBean topicBean = getMsgByTopicId(con, fto_id.longValue());
		            topicBean.setFto_msg_cnt(count);	// 加入回复数
		            
		            forumTopicVector.addElement(topicBean);
				}
				
			} // end for
			
			forumModuleParam.setTotal_rec(len);
		}
		
		return forumTopicVector;
	}
	
	/**
	 * 根据主题贴获取相关的论坛信息和主题信息的ForumTopicBean
	 * @param con 数据库连接器
	 * @param fto_id 主题贴ID
	 * @return 相关的论坛信息和主题信息的ForumTopicBean
	 * @throws SQLException
	 */
	public ForumTopicBean getMsgByTopicId(Connection con, long fto_id) throws SQLException {
		
		// 根据主题贴获取相关的论坛信息和主题信息SQL
		String sql = "select res.res_id, res.res_title, "	// 论坛ID，论坛名
			+ " foru.fto_id, foru.fto_title, "	// 标题ID，标题名
			+ cwSQL.replaceNull("rUser.usr_nickname", "rUser.usr_display_bil") + " usr_display_bil, foru.fto_create_datetime, "	// 创建人，创建时间
			+ " foru.fto_last_post_datetime " 	// 最后发表时间	
			+ " from ForumTopic foru "	// 论坛主题表
			+ " inner join Resources res on foru.fto_res_id=res.res_id "
			+ " inner join RegUser rUser on foru.fto_usr_id=rUser.usr_id "
			+ " where fto_id=? ";	// 传入：主题ID
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		ForumTopicBean topicBean = null;
        try {
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, fto_id);
			
			rs = pstmt.executeQuery();

			if (rs.next()) {
				topicBean = new ForumTopicBean();
				
				topicBean.setFor_res_id(rs.getLong("res_id"));			// 论坛id
				topicBean.setFor_res_title(rs.getString("res_title"));	// 论坛标题
				topicBean.setFto_id(rs.getLong("fto_id"));				// 主题id
				topicBean.setFto_title(rs.getString("fto_title"));		// 主题名
				topicBean.setFto_create_user(rs.getString("usr_display_bil"));				// 创建人
				topicBean.setFto_create_datetime(rs.getTimestamp("fto_create_datetime"));	// 创建时间
				topicBean.setFto_last_post_datetime(rs.getTimestamp("fto_last_post_datetime"));		// 最后发表时间
				topicBean.setFto_last_post_user(this.getLastPostUser(con, topicBean.getFto_id()));	// 最后发贴用户的昵称
			} // end if
			
		} catch(SQLException se) {
			System.err.println("[Get_Msg_By_TopicId_Exception]: " + se.getMessage());
			CommonLog.error(se.getMessage(),se);
			throw se;
		} finally {
			// 关闭相关的数据库操作
			if (rs != null) {
				rs.close();
				rs = null;
			}
			
			if (pstmt != null) {
				pstmt.close();
				pstmt = null;
			}
		}
		
		return topicBean;
	}
	
	/**
	 * 获取某个主题贴的最后发贴人的昵称
	 * @param con 数据库连接器
	 * @param fto_id 论坛ID
	 * @return 该主题贴下最后发贴人的昵称
	 * @throws SQLException
	 */
	public String getLastPostUser(Connection con, long  fto_id) throws SQLException {
		
		// 查询某一主题贴中最后回复的用户昵称SQL
		String sql = "select " + cwSQL.replaceNull("usr_nickname", "usr_display_bil") + " usr_display_bil "
			+ " from ForumMessage, RegUser "
			+ " where fmg_create_datetime=( "
			+ "			select max(fmg_create_datetime) "
			+ "			from ForumMessage "
			+ "			where fmg_fto_id=? "	// 传入：主题贴ID
			+ " 	  ) "
		    + " 	  and fmg_usr_id=usr_id ";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String name = null;
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, fto_id);
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				name = rs.getString("usr_display_bil");
			} // end if
			
			
		} catch(SQLException se) {
			System.err.println("[Get_Last_Post_User_Exception]: " + se.getMessage());
			CommonLog.error(se.getMessage(),se);
			throw se;
		} finally {
			// 关闭相关的数据库操作
			if (rs != null) {
				rs.close();
				rs = null;
			}
			
			if (pstmt != null) {
				pstmt.close();
				pstmt = null;
			}
		}
		
		return name;
	}
	
	/**
	 * 获取系统现有论坛的所有res_id
	 * @param con 数据库连接器
	 * @return 系统现有论坛的所有res_id数组
	 * @throws SQLException
	 */
	public long[] getIdOfAllForums(Connection con, boolean checkStatus) throws SQLException {
		
		// 查询SQL
		String sql = "select res_id, res_title "
			+ " from Module, Resources "
			+ " where mod_is_public=1 "
			+ "		  and mod_sgp_ind=0 "
			+ "		  and mod_res_id=res_id	"	// 将Module表和Resources表连接
			+ "		  and res_subtype='FOR' ";
		if (checkStatus){
            sql += " and ? between mod_eff_start_datetime AND mod_eff_end_datetime AND res_status = ?";
        }
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		Vector resIdVector = new Vector();
		try {
			Timestamp curTime = cwSQL.getTime(con);
			pstmt = con.prepareStatement(sql);
			if (checkStatus) {
				int index = 1;
				pstmt.setTimestamp(index++, curTime);
				pstmt.setString(index++, "ON");
			}
			rs = pstmt.executeQuery();

			while (rs.next()) {
				resIdVector.addElement(new Long(rs.getLong("res_id")));
			} // end while

		} catch(SQLException se) {
			System.err.println("[Get_All_Forums_Exception]: " + se.getMessage());
			CommonLog.error(se.getMessage(),se);
			throw se;
		} finally {
			// 关闭相关的数据库操作
			if (rs != null) {
				rs.close();
				rs = null;
			}
			
			if (pstmt != null) {
				pstmt.close();
				pstmt = null;
			}
		}
		
		int len = resIdVector.size();
        if (len > 0) {
            long[] resIds = new long[len];

            for (int i=0; i<len; i++) {
                Long element = (Long) resIdVector.elementAt(i);
                resIds[i] = element.longValue();
            }

            return resIds;
        } else {
            return null;
        }
	}
	
	public static String getForumTopicSql(String conditionSql) {
		String sql = "select fto_id, fto_title, fto_res_id, res_title, fto_last_post_datetime from ForumTopic"
				+ " inner join resources on res_id = fto_res_id" 
				+ " inner join module on mod_res_id = res_id" 
				+ " where mod_sgp_ind = ?";
		if (conditionSql != null) {
			sql += "" + conditionSql;
		}
		sql += " order by fto_last_post_datetime desc";
		return sql;
	}
	
	/**
	 * get forum topic that learner can view.
	 * @param con
	 * @return topic list
	 * @throws SQLException
	 */
	public static Vector getForumTopic(Connection con, int start, int pageSize) throws SQLException {
		Vector forumTopicVec = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			String conditionSql = " and mod_is_public = ?";
			stmt = con.prepareStatement(getForumTopicSql(conditionSql));
			int index = 1;
			stmt.setBoolean(index++, false);
			stmt.setBoolean(index++, true);
			rs = stmt.executeQuery();

			int count = 0;
			int end = start + pageSize -1;
			while (rs.next()) {
				if(start <= count && count <= end) {
					if (forumTopicVec == null) {
						forumTopicVec = new Vector();
					}
					ForumTopicBean topicBean = new ForumTopicBean();
					topicBean.setFto_id(rs.getLong("fto_id"));
					topicBean.setFto_title(rs.getString("fto_title"));
					topicBean.setFor_res_id(rs.getLong("fto_res_id"));
					topicBean.setFor_res_title(rs.getString("res_title"));
					topicBean.setFto_last_post_datetime(rs.getTimestamp("fto_last_post_datetime"));
					forumTopicVec.addElement(topicBean);
				}
				count++;
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}

		return forumTopicVec;
	}
	
}
