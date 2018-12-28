package com.cw.wizbank.qdb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cwn.wizbank.entity.SnsComment;

public class dbSns {
	
	private static String QUESTION = "question";
	private static String ANSWER = "answer";
	private static String COURSE = "course";
	private static String GROUP = "group";
	
	/**获取二级动态*/
	private static String selectTwoDoing = " select s_doi_id from sns_doing where s_doi_target_id in ( "
		+ " select s_doi_id from sns_doing where s_doi_target_id = ? and lower(s_doi_module) = ? "
		+ " union all (select s_cmt_id from sns_comment left join sns_doing on s_doi_id = s_cmt_target_id "
		+ " where (s_doi_target_id = ? and lower(s_doi_module) = ?) or (s_cmt_target_id = ? and lower(s_cmt_module) = ?))) ";
	
	/**获取二级评论*/
	private static String selectTwoComment = " select s_cmt_id from sns_comment where s_cmt_target_id in ( "
		+ " select s_cmt_id from sns_comment where s_cmt_target_id = ? and lower(s_cmt_module) = ? "
		+ " union all (select s_doi_id from sns_doing where s_doi_target_id = ? and lower(s_doi_module) = ?)) ";
		
	/**
	 * 删除问题相关赞
	 * @param que_id 问题id
	 */
	public static void deleteKnowLike(Connection con, long que_id) throws SQLException {
        PreparedStatement stmt = null;
        String sql = " delete from sns_valuation where lower(s_vlt_module) = ? and s_vlt_target_id in (select ans_id from ( select ans_id from knowAnswer where ans_que_id = ?) tmp ) ";
        try {
            stmt = con.prepareStatement(sql);
            int index = 1;
            stmt.setString(index++, ANSWER);
            stmt.setLong(index++, que_id);
            stmt.executeUpdate();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
	}
	
	/**
	 * 删除问题相关赞历史
	 * @param que_id 问题id
	 */
	public static void deleteKnowLikeLog(Connection con, long que_id) throws SQLException {
        PreparedStatement stmt = null;
        String sql = " delete from sns_valuation_log where lower(s_vtl_module) = ? and s_vtl_target_id in (select ans_id from knowAnswer where ans_que_id = ?) ";
        try {
            stmt = con.prepareStatement(sql);
            int index = 1;
            stmt.setString(index++, ANSWER);
            stmt.setLong(index++, que_id);
            stmt.executeUpdate();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
	}
	
	/**
	 * 删除问题相关动态
	 * @param que_id 问题id
	 */
	public static void deleteKnowDoing(Connection con, long que_id) throws SQLException {
        PreparedStatement stmt = null;
        String sql = " delete from sns_doing where (lower(s_doi_module) = ? and s_doi_target_id = ?) or (lower(s_doi_module) = ? and s_doi_target_id = ?) or (lower(s_doi_module) = ? and s_doi_target_id in (select ans_id from knowAnswer where ans_que_id = ?) ) ";
        try {
        	 stmt = con.prepareStatement(sql);
             int index = 1;
             stmt.setString(index++, QUESTION);
             stmt.setLong(index++, que_id);
             stmt.setString(index++, ANSWER);
             stmt.setLong(index++, que_id);
             stmt.setString(index++, ANSWER);
             stmt.setLong(index++, que_id);
             stmt.executeUpdate();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
	}
	
	/**
	 * 删除回答的相关动态
	 * @param que_id 问题id
	 */
	public static void deleteKnowAnswerDoing(Connection con, long que_id, long usr_ent_id) throws SQLException {
        PreparedStatement stmt = null;
        String sql = " delete from sns_doing where (lower(s_doi_module) = ? and s_doi_target_id = ? and s_doi_uid = ?) or (lower(s_doi_module) = ? and s_doi_target_id in (select ans_id from knowAnswer where ans_que_id = ? and ans_create_ent_id = ?) ) ";
        try {
            stmt = con.prepareStatement(sql);
            int index = 1;
            stmt.setString(index++, ANSWER);
            stmt.setLong(index++, que_id);
            stmt.setLong(index++, usr_ent_id);
            stmt.setString(index++, ANSWER);
            stmt.setLong(index++, que_id);
            stmt.setLong(index++, usr_ent_id);
            stmt.executeUpdate();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
	}
	
	
	/**
	 * 删除其相关赞统计
	 * @param target_id 目标id
	 */
	public static void deleteLikeCount(Connection con, long target_id, String module) throws SQLException {
		PreparedStatement stmt = null;
        String sql = " delete sns_valuation where s_vlt_id in (select s_vlt_id from sns_valuation "
        		+ " where s_vlt_target_id = ? and lower(s_vlt_module) = ? union all (select s_vlt_id "
        		+ " from sns_valuation where s_vlt_is_comment = 1 and s_vlt_target_id in ( " + selectTwoComment
        		+ "	union all (select s_cmt_id from sns_comment where s_cmt_target_id = ? and lower(s_cmt_module) = ?))) "
        		+ " union all (select s_vlt_id from sns_valuation inner join sns_doing on s_vlt_target_id = s_doi_id "
        		+ " where s_doi_target_id = ? and lower(s_doi_module) = ?)) ";
        try {
            stmt = con.prepareStatement(sql);
            int index = 1;
            stmt.setLong(index++, target_id);
            stmt.setString(index++, module);
            stmt.setLong(index++, target_id);
            stmt.setString(index++, module);
            stmt.setLong(index++, target_id);
            stmt.setString(index++, module);
            stmt.setLong(index++, target_id);
            stmt.setString(index++, module);
            stmt.setLong(index++, target_id);
            stmt.setString(index++, module);
            stmt.executeUpdate();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
	}
	
	/**
	 * 删除其相关赞历史
	 * @param target_id 目标id
	 */
	public static void deleteLikeLog(Connection con, long target_id, String module) throws SQLException {
		PreparedStatement stmt = null;
        String sql = " delete sns_valuation_log where s_vtl_log_id in (select s_vtl_log_id from sns_valuation_log "
        		+ " where s_vtl_target_id = ? and lower(s_vtl_module) = ? union all (select s_vtl_log_id "
        		+ " from sns_valuation_log where s_vtl_is_comment = 1 and s_vtl_target_id in ( " + selectTwoComment
        		+ "	union all (select s_cmt_id from sns_comment where s_cmt_target_id = ? and lower(s_cmt_module) = ?))) "
        		+ " union all (select s_vtl_log_id from sns_valuation_log inner join sns_doing on s_vtl_target_id = s_doi_id "
        		+ " where s_doi_target_id = ? and lower(s_doi_module) = ?)) ";
        try {
            stmt = con.prepareStatement(sql);
            int index = 1;
            stmt.setLong(index++, target_id);
            stmt.setString(index++, module);
            stmt.setLong(index++, target_id);
            stmt.setString(index++, module);
            stmt.setLong(index++, target_id);
            stmt.setString(index++, module);
            stmt.setLong(index++, target_id);
            stmt.setString(index++, module);
            stmt.setLong(index++, target_id);
            stmt.setString(index++, module);
            stmt.executeUpdate();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
	}
	
	/**
	 * 删除其相关评论所产生动态
	 * @param target_id 目标id
	 */
	public static void deleteTwoDoing(Connection con, long target_id, String module) throws SQLException {
		PreparedStatement stmt = null;
        String sql = " delete sns_doing where s_doi_target_id in (" + selectTwoDoing + ") ";
        try {
            stmt = con.prepareStatement(sql);
            int index = 1;
            stmt.setLong(index++, target_id);
            stmt.setString(index++, module);
            stmt.setLong(index++, target_id);
            stmt.setString(index++, module);
            stmt.setLong(index++, target_id);
            stmt.setString(index++, module);
            stmt.executeUpdate();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
	}
	
	/**
	 * 删除其相关二级评论
	 * @param target_id 目标id
	 */
	public static void deleteTwoComment(Connection con, long target_id, String module) throws SQLException {
		PreparedStatement stmt = null;
        String sql = " delete sns_comment where s_cmt_target_id in (" + selectTwoComment + ") ";
        try {
            stmt = con.prepareStatement(sql);
            int index = 1;
            stmt.setLong(index++, target_id);
            stmt.setString(index++, module);
            stmt.setLong(index++, target_id);
            stmt.setString(index++, module);
            stmt.executeUpdate();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
	}
	
	/**
	 * 删除其相关二级评论
	 * @param target_id 目标id
	 */
	public static void deleteCommentById(Connection con, long cmt_id) throws SQLException {
		PreparedStatement stmt = null;
        String sql = " delete sns_comment where s_cmt_id=?";
        try {
            stmt = con.prepareStatement(sql);
            int index = 1;
            stmt.setLong(index++, cmt_id);
            stmt.executeUpdate();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
	}
	
	/**
	 * 删除其相关剩余动态
	 * @param target_id 目标id
	 */
	public static void deleteOtherDoing(Connection con, long target_id, String module) throws SQLException {
		PreparedStatement stmt = null;
        String sql = " delete sns_doing where s_doi_target_id = ? and lower(s_doi_module) = ? ";
        try {
            stmt = con.prepareStatement(sql);
            int index = 1;
            stmt.setLong(index++, target_id);
            stmt.setString(index++, module);
            stmt.executeUpdate();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
	}
	
	/**
	 * 删除其相关剩余评论
	 * @param target_id 目标id
	 */
	public static void deleteOtherComment(Connection con, long target_id, String module) throws SQLException {
		PreparedStatement stmt = null;
        String sql = " delete sns_comment where s_cmt_target_id = ? and lower(s_cmt_module) = ? ";
        try {
            stmt = con.prepareStatement(sql);
            int index = 1;
            stmt.setLong(index++, target_id);
            stmt.setString(index++, module);
            stmt.executeUpdate();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
	}
	
	/**
	 * 删除其相关收藏，分享，赞的统计数
	 * @param target_id 目标id
	 */
	public static void deleteChildSnsCount(Connection con, long target_id, String module) throws SQLException {
		PreparedStatement stmt = null;
        String sql = " delete sns_count where s_cnt_id in (select s_cnt_id from sns_count "
        		+ " where s_cnt_target_id = ? and lower(s_cnt_module) = ? union all (select s_cnt_id "
        		+ " from sns_count where s_cnt_is_comment = 1 and s_cnt_target_id in ( " + selectTwoComment
        		+ "	union all (select s_cmt_id from sns_comment where s_cmt_target_id = ? and lower(s_cmt_module) = ?))) "
        		+ " union all (select s_cnt_id from sns_count inner join sns_doing on s_cnt_target_id = s_doi_id "
        		+ " where s_doi_target_id = ? and lower(s_doi_module) = ?)) ";
        try {
            stmt = con.prepareStatement(sql);
            int index = 1;
            stmt.setLong(index++, target_id);
            stmt.setString(index++, module);
            stmt.setLong(index++, target_id);
            stmt.setString(index++, module);
            stmt.setLong(index++, target_id);
            stmt.setString(index++, module);
            stmt.setLong(index++, target_id);
            stmt.setString(index++, module);
            stmt.setLong(index++, target_id);
            stmt.setString(index++, module);
            stmt.executeUpdate();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
	}
	
	/**
	 * 删除其收藏，分享，赞的统计数
	 * @param target_id 目标id
	 * @param module 类型
	 * @param isComment 是否是评论
	 */
	public static void deleteSnsCount(Connection con, long target_id, String module, boolean isComment) throws SQLException {
		PreparedStatement stmt = null;
        String sql = " delete from sns_count where s_cnt_target_id = ? and lower(s_cnt_module) = ? and s_cnt_is_comment = ? ";
        try {
            stmt = con.prepareStatement(sql);
            int index = 1;
            stmt.setLong(index++, target_id);
            stmt.setString(index++, module.toLowerCase());
            stmt.setBoolean(index++, isComment);
            stmt.executeUpdate();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
	}
	
	public static List<SnsComment> getTopCommentByCourse(Connection con,long itm_id,String module) throws SQLException {
		List<SnsComment> list=new ArrayList<SnsComment>();
		PreparedStatement stmt = null;
		ResultSet rs=null;
        String sql = "select s_cmt_id,s_cmt_module from sns_comment where upper(s_cmt_module)=? and  s_cmt_reply_to_id=0 and s_cmt_target_id=?";
        try {
        	
            stmt = con.prepareStatement(sql);
            stmt.setString(1, module.toUpperCase());
        	stmt.setLong(2, itm_id);
            rs=stmt.executeQuery();
            while(rs.next()){
            	SnsComment comment=new SnsComment();
            	comment.setS_cmt_id(rs.getLong("s_cmt_id"));
            	comment.setS_cmt_module(rs.getString("s_cmt_module"));
            	list.add(comment);
            }
        } finally {
        	  if (stmt != null) {
                  stmt.close();
              }
              if (rs != null) {
                  rs.close();
              }
        }
        return list;
	}
	
	public static List<SnsComment> getSubComment(Connection con,long s_cmt_id) throws SQLException {
		List<SnsComment> list=new ArrayList<SnsComment>();
		PreparedStatement stmt = null;
		ResultSet rs=null;
        String sql = "select s_cmt_id,s_cmt_module from sns_comment where s_cmt_reply_to_id=?";
        try {
        	
            stmt = con.prepareStatement(sql);
        	stmt.setLong(1, s_cmt_id);
            rs=stmt.executeQuery();
            while(rs.next()){
            	SnsComment comment=new SnsComment();
            	comment.setS_cmt_id(rs.getLong("s_cmt_id"));
            	comment.setS_cmt_module(rs.getString("s_cmt_module"));
            	list.add(comment);
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return list;
	}
	
	public static void delValuationByComment(Connection con,long target_id,String module, boolean is_commnet) throws SQLException {
		PreparedStatement stmt = null;
        String sql = " delete from sns_valuation " 
	      +"  where upper(s_vlt_module) = ?"
	       +" and s_vlt_target_id = ?"
	       +" and s_vlt_is_comment = ?";
        try {
        	
            stmt = con.prepareStatement(sql);
        	stmt.setString(1, module.toUpperCase());
        	stmt.setLong(2, target_id);
        	stmt.setBoolean(3, is_commnet);
            stmt.executeUpdate();
            
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
	}
	
	public static void delValuationLogByComment(Connection con,long target_id,String module,boolean is_comment) throws SQLException {
		PreparedStatement stmt = null;
        String sql = "delete from Sns_Valuation_Log where "
        +" s_vtl_target_id = ?"
        +" and upper(s_vtl_module) = ?"
        +" and s_vtl_is_comment = ?";
        try {
        	
            stmt = con.prepareStatement(sql);
        	stmt.setLong(1,target_id);
        	stmt.setString(2,  module.toUpperCase());
        	stmt.setBoolean(3,  is_comment);
            stmt.executeUpdate();
            
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
	}
	public static void deleteDoingByModule(Connection con,long target_id,String module) throws SQLException {
		PreparedStatement stmt = null;
        String sql = "delete from sns_doing "
        		+ "where (s_doi_target_id = ? and upper(s_doi_module) = ?) "
        		+ "or (upper(s_doi_act) = ? and s_doi_act_id = ?)";
        try {
        	
            stmt = con.prepareStatement(sql);
        	stmt.setLong(1,target_id);
        	stmt.setString(2, module.toUpperCase());
        	stmt.setString(3,  module.toUpperCase());
        	stmt.setLong(4,target_id);
            stmt.executeUpdate();
            
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
	}
	

	
	
	
	public static void getAllSubComment(Connection con,long s_cmt_id,List<SnsComment> result) throws SQLException {
		if(result==null){
			result=new ArrayList<SnsComment>();
		}
		List<SnsComment> list_comment=getSubComment(con,s_cmt_id);
		for(SnsComment comment:list_comment){
			result.add(comment);
			getAllSubComment(con,comment.getS_cmt_id(),result);
		}
	}
	
	/**
	 * 删除课程相关赞、动态和评论
	 * @param itm_id 课程id
	 */
	public static void deleteCourseLikeDoingComment(Connection con, long itm_id) throws SQLException {
		List<SnsComment> comment_all=new ArrayList<SnsComment>();
		//获取课程所有一级评论
		List<SnsComment> topComment=getTopCommentByCourse(con,itm_id,COURSE);
		//获取课程一级评论所有的子级评论
		for(SnsComment comment:topComment){
			getAllSubComment(con,comment.getS_cmt_id(),comment_all);
			comment_all.add(comment);
		}
		//循环删除所有评论的赞记录，赞日志记录，动态信息，统计信息
		for(SnsComment comment: comment_all){
			//删除赞记录
			delValuationByComment(con,comment.getS_cmt_id(),comment.getS_cmt_module(),true);
			//删除赞日志记录
			delValuationLogByComment(con,comment.getS_cmt_id(),comment.getS_cmt_module(),true);
			//删除动态信息
			deleteDoingByModule(con,comment.getS_cmt_id(),"COMMENT");
			//删除统计信息
			deleteSnsCount(con,comment.getS_cmt_id(),comment.getS_cmt_module().toLowerCase(),true);
			//删除评论
			deleteCommentById(con,comment.getS_cmt_id());
		}
		
		//删除课程赞记录
		delValuationByComment(con,itm_id,COURSE,false);
		//删除课程赞日志记录
		delValuationLogByComment(con,itm_id,COURSE,false);
		//删除课程统计信息
		deleteSnsCount(con,itm_id,COURSE.toLowerCase(),false);
		//删除课程动态
		deleteDoingByModule(con,itm_id,COURSE);

		//删除课程所有的赞记录，赞日志记录，动态信息，统计信息
//		deleteLikeLog(con, itm_id, COURSE);
//		deleteLikeCount(con, itm_id, COURSE);
//		deleteChildSnsCount(con, itm_id, COURSE);
//		deleteTwoDoing(con, itm_id, COURSE);
//		deleteTwoComment(con, itm_id, COURSE);
//		deleteOtherDoing(con, itm_id, COURSE);
//		deleteOtherComment(con, itm_id, COURSE);
//		deleteSnsCount(con, itm_id, COURSE, false);
	}
	
	/**
	 * 删除群组相关赞、动态和评论
	 * @param itm_id 课程id
	 */
	public static void deleteSnsGroupLikeDoingComment(Connection con, long grp_id) throws SQLException {
		deleteLikeLog(con, grp_id, GROUP);
		deleteLikeCount(con, grp_id, GROUP);
		deleteChildSnsCount(con, grp_id, COURSE);
		deleteTwoDoing(con, grp_id, GROUP);
		deleteTwoComment(con, grp_id, GROUP);
		deleteOtherDoing(con, grp_id, GROUP);
		deleteOtherComment(con, grp_id, GROUP);
		deleteSnsCount(con, grp_id, GROUP, false);
	}
	
}
