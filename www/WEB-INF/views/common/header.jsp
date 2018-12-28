<%@page import="com.cw.wizbank.qdb.loginProfile"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="taglibs.jsp"%>
<script type="text/javascript" src="${ctx}/static/js/basic.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery.prompt.js"></script>
<style type="text/css">
    #gongao #scroll_begin, #gongao #scroll_end{display:inline}
    #scroll_begin,#scroll_end{width: 300px;}
</style>

<c:if test="${prof!= null && !prof.isLrnRole}">
	  <script type="text/javascript">
	  //加上这个控制，是为了预防学员换到学员角色后，通过浏览器的后退回到管理员页面。
	   if (window.parent == null) {
			window.location.href =  '${ctx}/' + '${prof.role_url_home}';
		} else {
			 //如果是弹出或内嵌窗口，则不需要做这个的操作
			//window.parent.location.href =  '${ctx}/' + '${prof.role_url_home}';
		}
	  </script>
</c:if>
<script type="text/javascript">
	$(function() {
		$('#role-select-list li').each(function(){
			$(this).click(function(){
				wb_utils_lrn_change_role_lrn($(this).attr('data-value'));
			});
		});
		$('#lang-select-list li').each(function(){
			$(this).click(function(){
				changeLang($(this).attr('data-value'));
			});
		});
		if(${prof != null and prof.common_role_id != 'TADM'}){
			$.ajax({
				url : '${ctx}/app/message/myWebMessageTotal',
				type : 'POST',
				dataType : 'json',
				success : function(data) {
					 $("#myWebMessageTotal").show();
					if(data.total>0){
						if(data.total>99){
							data.total = "99+";
						}
					   $("#myWebMessageTotal").html(data.total);
					}else{
					   $("#myWebMessageTotal").hide();
					}
				}
			});
		}
		$("#globalSearchText").prompt("<lb:get key='global.lab_search_type_nav' />");
	});

	function doLogout() {
		Dialog.confirm({text:getLabel('625'), callback: function (answer) {
				if(answer){
					var url = wb_utils_invoke_servlet('cmd', 'logout');
					self.location.href = url;
				}
			}
		});
	}

	function ScrollImgLeft(){
        var speed=50;
        var scroll_begin = document.getElementById("scroll_begin");
        var scroll_end = document.getElementById("scroll_end");
        var scroll_div = document.getElementById("scroll_div");

       if(scroll_begin.offsetWidth<=420){
           clearInterval(MyMar)
       }
        if(scroll_begin.offsetWidth>=420){
            scroll_end.innerHTML=scroll_begin.innerHTML;
        }
        function Marquee(){
            if(scroll_end.offsetWidth-scroll_div.scrollLeft<=0)
                scroll_div.scrollLeft-=scroll_begin.offsetWidth;
            else
                scroll_div.scrollLeft++;
                  scroll_end.offsetWidth-scroll_div.scrollLeft;
            //console.log(scroll_end.offsetWidth-scroll_div.scrollLeft)

        }
        var MyMar=setInterval(Marquee,speed);
        scroll_div.onmouseover=function() {clearInterval(MyMar);};
        scroll_div.onmouseout=function() {MyMar=setInterval(Marquee,speed);}
    }
	
	function goPersonal(){
			var url = '${ctx}/app/personal/${prof.usr_ent_id}';
			self.location.href = url;
	}
</script>
<!-- header开始 -->
<div class="xyd-header" id="header">
	<div class="xyd-header-box">
		<div class="xyd-logo">
			<a href="${ctx}/app/home" title="" target=""> <c:if
					test="${!empty logo}">
					<c:if test="${lang == 'zh-cn'}">
						<img src="${ctx}/poster/cw/${logo.sp_logo_file_cn}" alt="wizbank" />
					</c:if>
					<c:if test="${lang == 'zh-hk'}">
						<img src="${ctx}/poster/cw/${logo.sp_logo_file_hk}" alt="wizbank" />
					</c:if>
					<c:if test="${lang == 'en-us'}">
						<img src="${ctx}/poster/cw/${logo.sp_logo_file_us}" alt="wizbank" />
					</c:if>
				</c:if> <c:if test="${empty logo}">
					<img src="${ctx}/poster/cw/wizbang.png" alt="wizbank" />
				</c:if>
			</a>
		</div>
<c:if test="${ prof!= null}">
		<div class="xyd-tool">
			<div class="xyd-welcome">
			      <div id="gongao">
					    <div style="width:420px;height:30px;margin:0 auto;white-space: nowrap;overflow:hidden;" id="scroll_div" class="scroll_div">
					        <div id="scroll_begin">
								       ${welcomeText}<%-- <c:if
										test="${empty welcomeText}">
										<lb:get key="global.lab_global_wel" />
									</c:if> --%>
					        </div>
					        <div id="scroll_end"></div>
					    </div>
					    <script type="text/javascript">ScrollImgLeft();</script>
					</div>
			</div>

 			<div class="xyd-email">
				<a href="${ctx }/app/message/webMessage">
					<i class="fa xyd-email-icon fa-envelope"></i>
					<span id="myWebMessageTotal" style="display: none;"></span>
				</a>
			</div>

		   <c:if test="${showTaskOnPage}"><!-- header 是否显示我的任务图标 -->

		   <c:set var="taskCount" value="${announceCount +  messageCount + approvalCount + evaluationCount + votingCount}"/><!-- 任务总数 -->

			<div class="xyd-people xyd-flag">
				<div class="xyd-menu" >
					<ul>
						<li href="javascript:void(0)" class="">
                             <a class="thickbox" id="tob" data-toggle="modal" data-target="#taskModal">
                             	<i class="fa xyd-flag-icon fa-flag-o"></i>
                             	<c:if test="${taskCount > 0 }">
                             		<span class="xyd-flag-number" style="background:#522ccf;">${taskCount > 99 ? '99+' : taskCount}</span>
                             	</c:if>
							 </a>
						</li>
					</ul>
				</div>

      			<div class="modal fade" tabindex="-1"  role="dialog" aria-labelledby="" aria-hidden="true" id="taskModal">
                    <div class="modal-dialog" style="width:400px;height:560px;margin: -230px 0 0 -200px;">
                        <div class="modal-content">
                            <div class="modal-body" style="min-height:500px;padding:0;">
                            	<div class="bg-position01">
									<img src="/static/images/renwu_bg02.png" alt="">
								 </div>
								<!-- <div class="bg-position01 bg-position02">
									<img src="/static/images/renwu_bg03.png" alt="">
								 </div> -->
								<!-- <div class="houzi-position">
									<img src="/static/images/houzi.png" alt="">
								 </div> -->
                            	<div id="TB_title" class="position03">
                                	<div id="TB_ajaxWindowTitle"></div>
                                	<div id="TB_closeAjaxWindow">
                                		<a aria-label="Close" data-dismiss="modal" href="javascript:void(0)" class="TB_closeWindowButton"></a>
                                	</div>
                                </div>

                                <div id="wzb-pop" >
                                        <div class="xyd-pop-top">
                                             <img src="${ctx }/static/images/xyd-renwu.png" alt=""/>
                                             <p class="xyd-pop-title"><lb:get key="global.lab_index_new_task"/></p>
                                        </div>

                                        <div class="xyd-pop-content">

                                        <!-- 学员没有下属，不要在任务里显示【报名审批】 -->
                                        <c:if test="${prof.hasStaff == 'true'}">
                                        	<div class="xyd-pop-info clearfix">
												<a class="wzb-link02" href="${ctx }/app/subordinate/subordinateApproval" title="">
													<em class="xyd-pop-num">${approvalCount == 0 ? '' : (approvalCount > 99 ? '99+' : approvalCount) }</em>
													<lb:get key="global.lab_ftn_APPR_APP_LIST" />
												</a>
											</div>
                                        </c:if>

										<div class="xyd-pop-info clearfix">
											<a class="wzb-link02" href="${ctx }/app/personal/personalEvaluation/${prof.usr_ent_id}" title="">
												<em class="xyd-pop-num">${evaluationCount == 0 ? '' : (evaluationCount > 99 ? '99+' : evaluationCount) }</em>
												<lb:get key="global.lab_index_uncommit_evaluation" />
											</a>
										</div>
										<div class="xyd-pop-info clearfix">
												<a class="wzb-link02" href="${ctx }/app/voting" title="">
													<em class="xyd-pop-num">${votingCount == 0 ? '' : (votingCount > 99 ? '99+' : votingCount) }</em>
													<lb:get key="global.lab_index_use_voting" /></a>
												</a>
										</div>
										<div class="xyd-pop-info clearfix">
											<a class="wzb-link02" href="${ctx }/app/announce" title="">
												<em class="xyd-pop-num">${announceCount == 0 ? '' : (announceCount > 99 ? '99+' : announceCount) }</em>
												<lb:get key="global.lab_index_new_announce" /></a>
											</a>
										</div>
										<div class="xyd-pop-info clearfix">
											<a class="wzb-link02" href="${ctx }/app/message/webMessage" title="">
												<em class="xyd-pop-num">${messageCount == 0 ? '' : (messageCount > 99 ? '99+' : messageCount) }</em>
												<lb:get key="global.lab_index_new_message" /></a>
											</a>
										</div>
									</div>
									<div class="wzb-bar">
										<a data-dismiss="modal" class="btn wzb-btn-orange wzb-btn-big" href="javascript:;"><lb:get key="global.lab_index_know_it"/></a>
									</div>
								</div>
                            </div>
                        </div>
                    </div>
                </div>
			</div>
			</c:if>

			<!-- 右上角用户头像start -->

			<div class="xyd-people">
               <div class="xyd-menu" style="margin:-4px 0 0 0;">
                    <ul>
                         <li href="javascript:void(0)" class="">
                            <img src="${prof.usr_photo}" alt='${prof.usr_photo}' class="xyd-user-icon">
                             <ul class='nav-top'>
                                  <div class="xyd-tip-box-2"></div>
                                  <li style="height:auto;background:rgba(255,255,255,0.9);" >
                                      <div class="xyd-user" style="padding: 15px 15px 2px 15px;">
                                          <div class="xyd-user-box clearfix" style="background:none;">
                                              <div class="wzb-user wzb-user82">

                                                  <a style="margin-left: -15px;" href="javascript:goPersonal();"><img  src="${prof.usr_photo}" alt='${prof.usr_photo}' class="wzb-pic"></a>

                                              </div>

                                              <div class="xyd-user-content">
                                               <%--    <p onclick="goPersonal();" class="wzb-link04" title="" style="line-height:20px;">${prof.usr_display_bil}</p> --%>
                                                  <p onclick=""  title="" style="font-size:14px; color:#00aeef; text-decoration:none;line-height:20px;">${prof.usr_display_bil}</p>
                                                  <p style="line-height:20px;">${prof.usg_display_bil}</p>
                                              </div>
                                          </div>
                                          <!-- usermess End-->

                                          <p><span class="color-gray666"><!-- 上次登录 --><lb:get key="index_last_login_date"/>：</span><fmt:formatDate value="${prof.usr_last_login_date}" pattern="yyyy-MM-dd HH:mm"/></p>
                                      </div>
                                  </li>
                                  <li><a class="wDzye" href="${ctx }/app/personal/0"> <!-- 我的主页 --> <lb:get key="global.lab_menu_home" /></a></li>

                                  <c:if test="${prof.hasStaff == 'true'}">
									<li><a href="${ctx}/app/subordinate/subordinateList" class="wDxshu"><!-- 我的下属 --> <lb:get key="global.lab_menu_subordinate" /></a></li>
								  </c:if>

                                  <c:set var="roles_length">
									<%
										long roles_length = 0;
											if (request.getSession().getAttribute("auth_login_profile") != null) {
												roles_length = ((loginProfile) request.getSession()
														.getAttribute("auth_login_profile")).roles.length;
											}
									%>
									<%=roles_length%>
								</c:set>
								<c:if test="${roles_length > 1}">

									<li>
									  <a href="javascript:void(0)"class="jSe"><!-- 角色 --> <lb:get key="global.lab_menu_role" /></a>
                                      <ul style="display: none;" id="role-select-list">
                                          <c:forEach items="${prof.roleList}" var="role">
											<c:set var="lab">
												<c:if test="${fn:indexOf(role['id'], '_') >= 0}">
													<lb:get
														key="global.lab_rol_${fn:substringBefore(role['id'], '_')}" />
												</c:if>
												<c:if test="${fn:indexOf(role['id'], '_') < 0}">
												${role['title']}
											</c:if>
											</c:set>
											<li data-value="${role['id']}" id="${role['id']}"><a href="javascript:;">${lab}</a></li>
										 </c:forEach>
                                      </ul>
                                    </li>

								</c:if>

                                  <li>
                                  	<a href="javascript:;" class="yYan"> <!-- 语言 --> <lb:get key="global.lab_menu_language" /></a>
									<ul id="lang-select-list" style="display: none;">
										<li data-value="en-us"><a href="javascript:;"><lb:get key="global.lab_language_en_us" /></a></li>
										<li data-value="zh-cn"><a href="javascript:;"><lb:get key="global.lab_language_zh_cn" /></a></li>
										<li data-value="zh-hk"><a href="javascript:;"><lb:get key="global.lab_language_zh_hk" /></a></li>
									</ul>
								  </li>
                                  <li>
                                  	<a class="tChu" href="javascript: doLogout();"> <!-- 退出 --><lb:get key="global.lab_menu_exit" /></a>
								  </li>

                              </ul>
                         </li>
                    </ul>
               </div>
           </div>
		   <!-- 右上角用户头像end -->

		</div>
	   </c:if>
	</div>
</div>
<!-- header End-->
<c:if test="${ prof!= null}">
<!-- menu start -->
<div class="xyd-fixbox" id="menu">
	<div class="xyd-fixcut xyd-nav skin-bg">
		<div class="xyd-sub">
			<div class="xyd-menu">
				<ul>
					<li class="cur"><a href="${ctx }/app/home"> <!-- 	首页 --> <lb:get
								key="global.lab_menu_started" />
					</a></li>
					<li><a href="javascript:;"> <!-- 最新消息 --> <lb:get
								key="global.lab_menu_information" />
					</a>
						<ul>
							<li><a href="${ctx }/app/announce"> <!-- 公告 --> <lb:get
										key="global.lab_menu_announce" />
							</a></li>
							<li><a href="${ctx }/app/article"> <!-- 资讯 --> <lb:get
										key="global.lab_menu_article" />
							</a></li>
						</ul></li>
					<li><a href="javascript:;"> <!-- 学习 --> <lb:get
								key="global.lab_menu_study" />
					</a>
						<ul>
							<li ><a href="${ctx }/app/course/courseCatalog"> <!-- 课程目录 -->
									<lb:get key="label_core_training_management_297" /></a></li>
							<%-- <li><a href="${ctx }/app/course/recommend"> <!-- 推荐课程 -->
									<lb:get key="global.lab_menu_recommend" /></a></li> --%>
							<li><a href="${ctx }/app/course/signup"> <!--	已报名课程 -->
									<lb:get key="global.lab_menu_singup_course" /></a></li>
							<li><a href="${ctx }/app/course/open"> <!-- 公开课 --> <lb:get
										key="global.lab_menu_open_course" /></a></li>
							
							<c:if test="${specialTopicCount > 0 }">
                            <li><a href="${ctx }/app/learningmap/specialTopic">
                          			<lb:get  key="global.lab_specialtopic" />
                                 </a>
                            </li>
                             </c:if>
                            
                           <%-- <li><a href="${ctx }/app/course/schedule"> <!-- 我的日程表 --> <lb:get
										key="global.lab_menu_study_map" />
							</a></li>--%>

						</ul></li>
						<li><a href="${ctx }/app/course/schedule"> <!-- 我的日程表 --> <lb:get
										key="global.lab_menu_study_map" />
							</a></li>
					<%-- <li><a href="javascript:;"> <!-- 考试 --> <lb:get
								key="global.lab_menu_test" />
					</a>
						<ul>
							<li ><a href="${ctx }/app/exam/courseCatalog"> <!-- 考试目录 -->
									<lb:get key="global.lab_menu_test_catalog" /></a></li>
							
							<li><a href="${ctx }/app/exam/signup"> <!-- 已报名考试 --> <lb:get
										key="global.lab_exam_signup" /></a></li>
						</ul></li> --%>
				<%-- 	<li><a href="javascript:;"> <!-- 社区  --> <lb:get
								key="global.lab_menu_community" />
					</a>
						<ul>
							<c:if test="${sns_enabled == true}">
								<li><a href="${ctx }/app/group/groupList/0"> <!-- 群组 -->
										<lb:get key="global.lab_menu_group" />
								</a></li>
							</c:if>

							<li><a href="${ctx }/app/know/allKnow"> <!-- 在线问答 --> <lb:get
										key="global.lab_menu_answer" />
							</a></li>
							<li><a href="${ctx }/app/kb/center/index"> <!-- 知识中心 -->
									<lb:get key="global.lab_kb_center" />
							</a></li>
							
							<li><a href="${ctx }/app/rank/courseRank/T"> <!--课程排行榜 -->
									<lb:get key="global.lab_menu_course_top" />
							</a></li>
							<li><a href="${ctx }/app/rank/creditRank"> <!--积分排行榜 -->
									<lb:get key="global.lab_menu_credit_rank" />
							</a></li>
							<li><a href="${ctx }/app/rank/learningRank"> <!--学习排行榜 -->
									<lb:get key="global.lab_menu_learning_rank" />
							</a></li>
							
							<li><a href="${ctx }/app/instr"> <!--讲师风采 --> <lb:get
										key="global.lab_instr_main" />
							</a></li>
							
						</ul></li> --%>
<%-- 
					   <c:if test="${learningMapCount > 0 }">
				       		<li><a href="javascript:;"> <!-- 学习地图 -->
				       			<lb:get key="global.FTN_AMD_STUDY_MAP_MGT" />
                            </a>
                            <ul>
                                 <c:if test="${professionCount > 0 }">
                            <li><a href="${ctx }/app/learningmap/professionMap">
                                      <lb:get  key="global.lab_profession_map" />
                                 </a>
                            </li>
                            </c:if>  
                            <c:if test="${positionMapCount > 0 }">
                             <li><a href="${ctx }/app/learningmap/index">
                          			<lb:get  key="global.lab_postion_map" />
                                  </a>
                             </li>
                            </c:if>
						
                         </ul>
                         </li>
                       </c:if>
                        --%>	
                       
                   <%--  <c:if test="${liveCount > 0 }">
                      	<li>
                    		<a href="${ctx }/app/live"> 
                    			<lb:get key="label_live" /> <!-- 直播 --> 
                    		</a>
                   		</li>
					</c:if>
					--%>	
				</ul>
				<!-- nav End-->
			</div>

			<script type="text/javascript">
				$(function() {
					nav();
					checkMenu(); //选中
				})
			</script>

			<div class="xyd-search">

				<form action="${ctx}/app/search/toPage" method="post"
					name="searchForm" id="searchForm">
					<input type="text" class="form-control xyd-search-text"
						name="searchText" value="" id="globalSearchText" /> <input
						type="button" class="xyd-search-button" name="mybtn" value=""
						id="globalSearchBtn" />
				</form>
				<script type="text/javascript">
					$(function() {
						$("#globalSearchBtn").click(function() {
							if($("#globalSearchText").val() == fetchLabel('lab_search_type_nav')){
								$("#globalSearchText").val('');
							}
							$("#searchForm").submit();
						})
					})
					
				</script>

			</div>
		</div>
	</div>
</div>
</c:if>
<!-- menu end -->
