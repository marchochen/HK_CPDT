<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:choose>
    <c:when test="${sub.ftn_ext_id eq 'FTN_AMD_SNS_GROUP_MAIN'}"><!-- 群组管理 -->
       qzgli
    </c:when>
    <c:when test="${sub.ftn_ext_id eq 'FTN_AMD_RES_MAIN'}"><!-- 资源管理 -->
       zxgli
    </c:when>
    <c:when test="${sub.ftn_ext_id eq 'FTN_AMD_TEACHING_COURSE_LIST'}"><!-- 我的教学课程 -->
       wdjxkcheng
    </c:when>
    <c:when test="${sub.ftn_ext_id eq 'FTN_AMD_Q_AND_A_MAIN'}"><!-- 问答管理 -->
       wdgli
    </c:when>
    <c:when test="${sub.ftn_ext_id eq 'FTN_AMD_SYS_MSG_LIST'}"><!-- 公告 -->
       ggao
    </c:when>
    <c:when test="${sub.ftn_ext_id eq 'FTN_AMD_EXAM_LIST'}"><!-- 考试列表 -->
       kslbiao
    </c:when>
    <c:when test="${sub.ftn_ext_id eq 'FTN_AMD_MSG_MAIN'}"><!-- 公告管理 -->
		gggli
	</c:when>
	<c:when test="${sub.ftn_ext_id eq 'FTN_AMD_TRAINING_REPORT_MGT'}"><!-- 培训报表 -->
		pxbbiao
	</c:when>
	<c:when test="${sub.ftn_ext_id eq 'FTN_AMD_PROFESSION_MAIN'}"><!-- 职级学习地图管理 -->
		zjxxdtgli
	</c:when>
	<c:when test="${sub.ftn_ext_id eq 'FTN_AMD_CREDIT_SETTING_MAIN'}"><!-- 积分管理 -->
		jfgli
	</c:when>
	<c:when test="${sub.ftn_ext_id eq 'FTN_AMD_Q_AND_A_MAIN'}"><!-- 问答管理 -->
		wdgli
	</c:when>
	<c:when test="${sub.ftn_ext_id eq 'FTN_AMD_POSTER_MAIN'}"><!-- PC样式管理 -->
		pcysgli
	</c:when>
	<c:when test="${sub.ftn_ext_id eq 'FTN_AMD_TC_MAIN'}"><!-- 培训中心管理 -->
		pxzxgli
	</c:when>
	<c:when test="${sub.ftn_ext_id eq 'FTN_AMD_RES_MAIN'}"><!-- 资源管理 -->
		zygli
	</c:when>
	<c:when test="${sub.ftn_ext_id eq 'FTN_AMD_VOTING_MAIN'}"><!-- 投票 -->
		tpiao
	</c:when>
	<c:when test="${sub.ftn_ext_id eq 'FTN_AMD_OPEN_COS_MAIN'}"><!-- 公开课管理 -->
		gkkgli
	</c:when>
	<c:when test="${sub.ftn_ext_id eq 'FTN_AMD_EVN_MAIN'}"><!-- 公共调查问卷 -->
		ggtcwjuan
	</c:when>
	<c:when test="${sub.ftn_ext_id eq 'FTN_AMD_MOBILE_POSTER_MAIN'}"><!-- 移动管理 -->
		ydgli
	</c:when>
	<c:when test="${sub.ftn_ext_id eq 'FTN_AMD_PLAN_CARRY_OUT'}"><!-- 计划实施 -->
		jhsshi
	</c:when>
	<c:when test="${sub.ftn_ext_id eq 'FTN_AMD_ARTICLE_MAIN'}"><!-- 资讯管理 -->
		zxgli
	</c:when>
	<c:when test="${sub.ftn_ext_id eq 'FTN_AMD_YEAR_PALN'}"><!-- 年度计划 -->
		ndjhua
	</c:when>
	<c:when test="${sub.ftn_ext_id eq 'FTN_AMD_MAKEUP_PLAN'}"><!-- 编外计划 -->
		bwjhua
	</c:when>
	<c:when test="${sub.ftn_ext_id eq 'FTN_AMD_YEAR_PLAN_APPR'}"><!-- 年度计划审批 -->
		ndjhspi
	</c:when>
	<c:when test="${sub.ftn_ext_id eq 'FTN_AMD_MAKEUP_PLAN_APPR'}"><!-- 编外计划审批 -->
		bwjhspi
	</c:when>
	<c:when test="${sub.ftn_ext_id eq 'FTN_AMD_YEAR_SETTING'}"><!-- 年度设置 -->
		ndszhi
	</c:when>
	<c:when test="${sub.ftn_ext_id eq 'FTN_AMD_POSITION_MAP_MAIN'}"><!-- 关键岗位学习地图管理 -->
		gjgwxxdtgli
	</c:when>
	<c:when test="${sub.ftn_ext_id eq 'FTN_AMD_SPECIALTOPIC_MAIN'}"><!-- 专题培训 -->
		ztpxun
	</c:when>
	<c:when test="${sub.ftn_ext_id eq 'FTN_AMD_ITM_COS_MAIN'}"><!-- 课程管理 -->
		kcgli
	</c:when>
	<c:when test="${sub.ftn_ext_id eq 'FTN_AMD_CERTIFICATE_MAIN'}"><!-- 证书管理 -->
		zsgli
	</c:when>
	<c:when test="${sub.ftn_ext_id eq 'FTN_AMD_CAT_MAIN'}"><!-- 课程目录管理 -->
		kcmlgli
	</c:when>
	<c:when test="${sub.ftn_ext_id eq 'FTN_AMD_OPEN_COS_MAIN'}"><!-- 公开课管理 -->
		gkkgli
	</c:when>
	<c:when test="${sub.ftn_ext_id eq 'FTN_AMD_COS_EVN_MAIN'}"><!-- 课程评估问卷管理 -->
		kcpgwjgli
	</c:when>
	<c:when test="${sub.ftn_ext_id eq 'FTN_AMD_Q_AND_A_VIEW'}"><!-- 问答 -->
		wda
	</c:when>
	<c:when test="${sub.ftn_ext_id eq 'FTN_AMD_Q_AND_A_MAIN'}"><!-- 问答管理 -->
		wdgli
	</c:when>
	<c:when test="${sub.ftn_ext_id eq 'FTN_AMD_SNS_GROUP_VIEW'}"><!-- 群组 -->
		qzu
	</c:when>
	<c:when test="${sub.ftn_ext_id eq 'FTN_AMD_KNOWLEDGE_STOREGE'}"><!-- 知识库 -->
		zsku
	</c:when>
	<c:when test="${sub.ftn_ext_id eq 'FTN_AMD_KNOWLEDEG_CATALOG'}"><!-- 知识目录 -->
		zsmlu
	</c:when>
	<c:when test="${sub.ftn_ext_id eq 'FTN_AMD_KNOWLEDEG_TAG'}"><!-- 知识标签 -->
		zsbqian
	</c:when>
	<c:when test="${sub.ftn_ext_id eq 'FTN_AMD_KNOWLEDEG_APP'}"><!-- 知识审批 -->
		zsspi
	</c:when>
	<c:when test="${sub.ftn_ext_id eq 'FTN_AMD_POSITION_MAIN'}"><!-- 岗位管理 -->
		gwgli
	</c:when>
	<c:when test="${sub.ftn_ext_id eq 'FTN_AMD_USR_INFO'}"><!-- 用户信息 -->
		yhxxi
	</c:when>
	<c:when test="${sub.ftn_ext_id eq 'FTN_AMD_USR_REGIETER_APP'}"><!-- 注册审批 -->
		zcspi
	</c:when>
	<c:when test="${sub.ftn_ext_id eq 'FTN_AMD_USR_ACTIVATE'}"><!-- 用户激活 -->
		yhjhuo
	</c:when>
	<c:when test="${sub.ftn_ext_id eq 'FTN_AMD_GRADE_MAIN'}"><!-- 职级管理 -->
		zjgli
	</c:when>
	<c:when test="${sub.ftn_ext_id eq 'FTN_AMD_INT_INSTRUCTOR_MAIN'}"><!-- 内部讲师管理 -->
		nbjsgli
	</c:when>
	<c:when test="${sub.ftn_ext_id eq 'FTN_AMD_EXT_INSTRUCTOR_MAIN'}"><!-- 外部讲师管理 -->
		wbjsgli
	</c:when>
	<c:when test="${sub.ftn_ext_id eq 'FTN_AMD_SUPPLIER_MAIN'}"><!-- 供应商管理 -->
		gysgli
	</c:when>
	<c:when test="${sub.ftn_ext_id eq 'FTN_AMD_MESSAGE_TEMPLATE_MAIN'}"><!-- 邮件模板管理 -->
		yjmbgli
	</c:when>
	<c:when test="${sub.ftn_ext_id eq 'FTN_AMD_CREDIT_SETTING_MAIN'}"><!-- 积分管理 -->
		yhxxi
	</c:when>
	<c:when test="${sub.ftn_ext_id eq 'FTN_AMD_FACILITY_BOOK_CREATE'}"><!-- 新增预定 -->
		xzyding
	</c:when>
	<c:when test="${sub.ftn_ext_id eq 'FTN_AMD_FACILITY_BOOK_CALENDAR'}"><!-- 预定日历 -->
		ydrli
	</c:when>
	<c:when test="${sub.ftn_ext_id eq 'FTN_AMD_FACILITY_BOOK_HISTORY'}"><!-- 预订记录 -->
		ydjlu
	</c:when>
	<c:when test="${sub.ftn_ext_id eq 'FTN_AMD_FACILITY_INFO'}"><!-- 设施信息 -->
		ssxxi
	</c:when>
	<c:when test="${sub.ftn_ext_id eq 'FTN_AMD_SYS_ROLE_MAIN'}"><!-- 角色管理 -->
		jsgli
	</c:when>
	<c:when test="${sub.ftn_ext_id eq 'FTN_AMD_SYS_SETTING_MAIN'}"><!-- 系统设置 -->
		xtszhi
	</c:when>
	<c:when test="${sub.ftn_ext_id eq 'FTN_AMD_SYS_SETTING_LOG'}"><!-- 系统运作日志 -->
		xtyzrzhi
	</c:when>
	<c:when test="${sub.ftn_ext_id eq 'FTN_AMD_EIP_MAIN'}"><!-- 企业管理 -->
		wzb-color-2-color13 fa fa-shield
	</c:when>
    <c:otherwise>
    	fa wzb-color-2-color13 fa-newspaper-o
    </c:otherwise>
</c:choose>