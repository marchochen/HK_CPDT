<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<title></title>
</head>
<body>
	<!-- content部分 -->
	<div id="content">
		<div class="inner center">
			<!-- 内容左边框部分开始 -->
			<!-- 公告列表 -->
			<div class="left fl announce_all">
				<div class="blue_bg line_h40">
					<h3 class="font_size18 pad_16 family_font fl">消息</h3>
					<div class="sanjiao"></div>
				</div>
				<div class="b_box pad_16 padright_16">
					<ul class="announce_list font_size14">
						<li>
							<b class="gonggao"></b>
							<a href="#" class="gray333_font">新员工入职培训：导师招募正在进行</a>
							<p class="gray999_font line_h18">2014-06-24</p>
						</li>
						<li  class="announce_cur">
							<b class="gonggao"></b>
							<a href="#" class="gray333_font">平台推广活动：课程试用开通</a>
							<p class="gray999_font line_h18">2014-06-24</p>
						</li>
						<li>
							<b class="gonggao"></b>
							<a href="#" class="gray333_font">行动绩效支持：解决方案带着走</a>
							<p class="gray999_font line_h18">2014-06-24</p>
						</li>
						<li>
							<b class="gonggao"></b>
							<a href="#" class="gray333_font">平台推广活动：课程试用开通</a>
							<p class="gray999_font line_h18">2014-06-24</p>
						</li>
						<li>
							<b class="gonggao"></b>
							<a href="#" class="gray333_font">携手汇思，实现企业学习移动性</a>
							<p class="gray999_font line_h18">2014-06-24</p>
						</li>
						<li>
							<b class="gonggao"></b>
							<a href="#" class="gray333_font">绩效支持：不是学什么，而是做什么</a>
							<p class="gray999_font line_h18">2014-06-24</p>
						</li>
					</ul>
					<script type="text/javascript">
						$(function(){
							$("ul.announce_list>li").click(
								function(){
									$(this).addClass('announce_cur').siblings('').removeClass('announce_cur');
								}
							);
						});
					</script>
					<!-- 课程分页 -->
					<div class="course_page">
						<!-- <a class="disable"><</a> -->
						<span class="page_now">1</span>
						<a href="#">2</a>
						<a href="#">3</a>
						<a href="#">4</a>
						<a href="#">&gt;</a>
						<span class="refresh"></span>
					</div>
					<div class="cl"></div>
				</div>
			</div>
			<!-- 公告详情 -->
			<div class="right fl announce_detail">
				<div class="padd_40">
					<h3 class="class_title course_t">平台推广活动：课程试用开通</h3>
					<!-- 发布人、时间 -->
					<div class="announce_release">
						<div class="fl">
							<a href="#"><img src="${ctx}/images/touxiang1.png"/></a>
						</div>
						<div class="fl box_an">
							<a href="#" class="blue_font">Pure</a>
							<span class="gray333_font font_size14">2014-06-20 13:20:00</span>
						</div>
					</div>
					<div class="cl h18"></div>
					<!-- 发布的内容 -->
					<div class="announce_content gray333_font font_size14">
						<h4>Dear Anna</h4>
						<p class="no_p">
							Thank you for your recent enrollment of the course《新员工培训》(001).We have reviewed and approved your enrollment request.You can start your course by logging into the system using the link below:http://www.cyberwisdom.net
						</p>
						<h4>Regards</h4>
						<p class="no_p">From:training & development</p>
						<p class="no_p">Date:2014-7-28 00:00:00</p>
					</div>
				</div>
			</div>
		</div>
		<div class="cl"></div>
	</div>
</body>
</html>