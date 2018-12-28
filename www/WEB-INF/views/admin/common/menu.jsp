<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<link href="${ctx}/static/admin/css/scrollbar/jquery.mCustomScrollbar.css" rel="stylesheet" />
<script src="${ctx}/static/admin/js/scrollbar/jquery.mCustomScrollbar.concat.min.js"></script>
 <style>

                    .mCSB_container .cur{position: relative}
                    .mCSB_container .cur ul{
                        position: absolute;
                        left: 220px;
                        top: 0px;
                        z-index: 99999999999999999999;
                    }
                    #mCSB_1{
                        overflow: visible !important;
                    }
                    .mCSB_container{
                        overflow: visible !important;
                    }
                </style>  
                
	<c:if test="${prof!= null && prof.isLrnRole}">
		  <script type="text/javascript">
		  //加上这个控制，是为了预防学员换到管理员角色后，通过浏览器的后退回到学员页面。
		   if (window.parent == null) {
				window.location.href =  '${ctx}/' + '${prof.role_url_home}';
			} else {
			     //如果是弹出或内嵌窗口，则不需要做这个的操作
				//window.parent.location.href =  '${ctx}/' + '${prof.role_url_home}';
			}
		  </script>
	</c:if>
<script>
$(function(){
    $('#role-select-list dd').each(function(){
        $(this).click(function(){
            wb_utils_lrn_change_role_lrn($(this).attr('data-value'));
        });
    });

    if(${prof != null}){
		$.ajax({
			url : '${ctx}/app/message/myWebMessageTotal',
			type : 'POST',
			dataType : 'json',
			success : function(data) {
				 $("#envelope_num").show();
				if(data.total>0){
					if(data.total>99){
						data.total = "99+";
					}
				   $("#envelope_num").html(data.total);
				}else{
				   $("#envelope_num").hide();
				}
			}
		});			
	}
    $("#iknow").click(function(){
        $(".xtxx_warning").animate({"marginTop":'-50px'},2000);
    })
});

//滚动条
$(window).resize(function(){
	$(".wzb-sidebar .wzb-menu").height($(window).height()-80);
});  
$(function(){
	$('.mCSB_container').height("0px");
	$(".wzb-sidebar .wzb-menu").height($(window).height()-80);
	$(".content").mCustomScrollbar({
        scrollInertia:150,
        advanced:{
            updateOnBrowserResize:true,
            updateOnContentResize:true,
        },
    });
    function onTotalScrollBackCallback(){
        $(".callback_demo .callback_demo_output").html("<em>Scrolled to top. Content top position: "+mcs.top+"</em>").children("em").delay(1000).fadeOut("slow");
    }
}) ;
</script>
    <div class="wzb-top">
        <span class="wzb-logo" ><img src="${ctx }/static/images/wzb-logo.png" alt="wizbank" /></span>
        <div class="wzb-top-toggle">
            <i class="fa fa-bars"></i>
        </div>
        <c:if test="${sys_warning == true}">
			<div class="xtxx_warning"><lb:get key="sys_warning" />：&nbsp;&nbsp;<lb:get key="sys_warning_user_msg"/>
			        <span class="btn wzb-btn-yellow-border" id="iknow" style="margin:0 0 0 20px;"><lb:get key="sys_warning_know"/></span>
			</div>
		</c:if>
        <div class="wzb-tool">
            <div class="wzb-menu">
                <ul class="clearfix">
                
                    <li>
                    	<a class="wzb-menu-sup" href="javascript:wb_utils_nav_go('SYS_MESSAGE',${prof.usr_ent_id},'${label_lan}');" title=""><i
                            class="fa wzb-envelope-icon fa-envelope-o"></i> <span id="envelope_num" style="display:none" class="wzb-badge wzb-badge-pink">0</span></a>
                    </li>
                    
                    <li>
                    	<a class="wzb-menu-sub" href="javascript:;" title=""><img class="wzb-user" src="${prof.usr_photo}" alt="wizbank" />
                             <c:if test="${fn:length(prof.roles) > 0}">
                                 <c:forEach items="${prof.roleList}" var="role">
                                 		<c:if test="${prof.current_role == role['id'] }">
	                                         <c:if test="${fn:indexOf(role['id'], '_') >= 0}">
	                                             <lb:get key="global.lab_rol_${fn:substringBefore(role['id'], '_')}" />
	                                         </c:if>
	                                         <c:if test="${fn:indexOf(role['id'], '_') < 0}">
	                                             ${role['title']}
	                                         </c:if>
                                         </c:if>
                                 </c:forEach>
                             </c:if>
	                    	<%-- <lb:get key="global.lab_rol_${fn:substringBefore(prof.current_role, '_')}"/> --%>
	                    	<i class="fa wzb-caret-icon fa-caret-down"></i>
                    	</a>
                        <ul>
                            <li id="role-select-list"><a class="wzb-menu-son" href="javascript:;" title="" style="cursor:default;"><i class="fa fa-user"></i><lb:get key="global.lab_menu_role"/></a>
                                <dl>
                                    <c:set var="roles_length">
                                        ${fn:length(prof.roles) }
                                    </c:set>
                                    <c:if test="${roles_length > 0}">
                                        <c:forEach items="${prof.roleList}" var="role">
                                            <c:set var="lab">
                                                <c:if test="${fn:indexOf(role['id'], '_') >= 0}">
                                                    <lb:get key="global.lab_rol_${fn:substringBefore(role['id'], '_')}" />
                                                </c:if>
                                                <c:if test="${fn:indexOf(role['id'], '_') < 0}">
                                                    ${role['title']}
                                                </c:if>
                                            </c:set>
                                            <dd data-value="${role['id']}" id="${role['id']}">
                                                <a href="javascript:;">${lab}</a>
                                            </dd>
                                        </c:forEach>
                                    </c:if>
                                 </dl>
                            </li>
                            <li><a class="wzb-menu-son" href="javascript:;" title="" style="cursor:default;"><i class="fa fa-stack-exchange"></i><lb:get key="global.lab_menu_language"/></a>
                                <dl>
                                    <dd><a href="javascript:changeLang('zh-cn')" title=""><lb:get key="global.lab_language_zh_cn"/></a></dd>
                                    <dd><a href="javascript:changeLang('zh-hk')" title=""><lb:get key="global.lab_language_zh_hk"/></a></dd>
                                    <dd><a href="javascript:changeLang('en-us')" title=""><lb:get key="global.lab_language_en_us"/></a></dd>
                                </dl>
                            </li>
                            <li><a class="wzb-menu-son" href="javascript:wb_utils_nav_go('USR_OWN_MAIN',${prof.usr_ent_id})" title=""><i class="fa fa-instagram"></i><lb:get key="global.lab_ftn_USR_OWN_MAIN"/></a></li>
                            <li><a class="wzb-menu-son" href="javascript:wb_utils_nav_go('USR_PWD_UPD',${prof.usr_ent_id})" title=""><i class="fa fa-key"></i><lb:get key="global.lab_usr_change_psd"/></a></li>
                            <%--屏蔽帮助中心 --%>
                           <%--  <li><a class="wzb-menu-son" href="javascript:wb_utils_nav_go('HELP_CENTER')" title=""><i class="wzb-Bzhu"></i><lb:get key="global.lab_menu_help"/></a></li>  --%>
                            <%-- <li><a class="wzb-menu-son" href="javascript:wb_utils_nav_go('USR_OWN_PREFER',${prof.usr_ent_id},'${label_lan}')" title=""><i class="fa fa-smile-o"></i><lb:get key="global.lab_ftn_USR_OWN_PREFER"/></a></li> --%>
                            <li><a class="wzb-menu-son" href="javascript:wb_utils_logout('${label_lan}')" title=""><i class="fa fa-sign-out"></i><lb:get key="global.lab_menu_exit"/></a></li>
                        </ul></li>
                </ul>
            </div>
        </div>
    </div>
    <!-- wzb-top End-->
    	<!-- 左侧菜单栏 -->
        <div class="wzb-sidebar">
            <div class="wzb-menu content" style="padding: 0">
                <ul style="height:700px;">
                	<!-- 首页 -->
                    <li><a class="wzb-menu-son" href="javascript:wb_utils_gen_home(true);" title=""><i class="fa fa-home" style="margin-right:9px;"></i><lb:get key="global.lab_menu_started"/></a></li>
                    <c:forEach items="${prof.roleFunctions }" var="fun">
                    <li>
	                    <c:choose>
	                        <c:when test="${fn:length(fun.subFunctions) < 1 }">
	                            <a class="wzb-menu-son" href="javascript:wb_utils_nav_go('${fun.ftn_ext_id }', '${prof.usr_ent_id }', '${label_lan }');" title="" data="${fun.ftn_ext_id}">
	                        </c:when>
	                        <c:otherwise>
								<a class="wzb-menu-son" href="javascript:;" title="" data="${fun.ftn_ext_id}">
	                        </c:otherwise>
	                    </c:choose>
                    	<!-- 图标 -->
	                    <c:choose>
	                        <c:when test="${fun.ftn_ext_id eq 'FTN_AMD_ARTICLE_MGT'}">
	                        <i class="fa fa-globe"></i>
	                        <i class="fa fa-angle-right" aria-hidden="true"></i>
	                        </c:when>
	                        <c:when test="${fun.ftn_ext_id eq 'FTN_AMD_DEMAND_MGT'}">
	                        <i class="fa fa-cubes"></i>
	                        <i class="fa fa-angle-right" aria-hidden="true"></i>
	                        </c:when>
	                        <c:when test="${fun.ftn_ext_id eq 'FTN_AMD_PLAN_MGT'}">
	                        <i class="fa fa-list-alt"></i>
	                        <i class="fa fa-angle-right" aria-hidden="true"></i>
	                        </c:when>
	                        <c:when test="${fun.ftn_ext_id eq 'FTN_AMD_STUDY_MAP_MGT'}">
	                        <i class="fa fa-maxcdn"></i>
	                        <i class="fa fa-angle-right" aria-hidden="true"></i>
	                        </c:when>
	                        <c:when test="${fun.ftn_ext_id eq 'FTN_AMD_TRAINING_MGT'}">
	                        <i class="fa fa-calendar"></i>
	                        <i class="fa fa-angle-right" aria-hidden="true"></i>
	                        </c:when> 
	                        <c:when test="${fun.ftn_ext_id eq 'FTN_AMD_EXAM_MGT'}">
	                        <i class="fa fa-columns"></i>
	                        </c:when> 
	                        <c:when test="${fun.ftn_ext_id eq 'FTN_AMD_SNS_MGT'}">
	                       <!--  <i class="fa fa-comments"></i>
	                        <i class="fa fa-angle-right" aria-hidden="true"></i> -->
	                         <i class="fa fa-comments"></i>
	                        </c:when>
	                        <c:when test="${fun.ftn_ext_id eq 'FTN_AMD_CREDIT_SETTING_MAIN'}">
	                       	 <i class="fa fa-globe"></i>
	                        </c:when>
	                        <c:when test="${fun.ftn_ext_id eq 'FTN_AMD_KNOWLEDGE_MGT'}">
	                        <i class="fa fa-life-ring"></i>
	                        <i class="fa fa-angle-right" aria-hidden="true"></i>
	                        </c:when>
	                        <c:when test="${fun.ftn_ext_id eq 'FTN_AMD_TRAINING_REPORT_MGT'}">
	                        <i class="fa fa-table"></i>
	                        </c:when>
	                        <c:when test="${fun.ftn_ext_id eq 'FTN_AMD_USR_INFO_MGT'}">
	                        <i class="fa fa-child"></i>
	                        <i class="fa fa-angle-right" aria-hidden="true"></i>
	                        </c:when> 
	                        <c:when test="${fun.ftn_ext_id eq 'FTN_AMD_BASE_DATA_MGT'}">
	                        <i class="fa fa-suitcase"></i>
	                        <i class="fa fa-angle-right" aria-hidden="true"></i>
	                        </c:when>  
	                        <c:when test="${fun.ftn_ext_id eq 'FTN_AMD_SYSTEM_SETTING_MGT'}">
	                        <i class="fa fa-wrench"></i>
	                        <i class="fa fa-angle-right" aria-hidden="true"></i>
	                        </c:when> 
	                        <c:when test="${fun.ftn_ext_id eq 'FTN_AMD_FACILITY_MGT'}">
	                        <i class="fa fa-road"></i>
	                        <i class="fa fa-angle-right" aria-hidden="true"></i>
	                        </c:when>
	                        <c:when test="${fun.ftn_ext_id eq 'FTN_AMD_LIVE_MAIN'}">
		                        <i class="fa fa-video-camera"></i>
	                        </c:when>
	                        <c:when test="${fun.ftn_ext_id eq 'FTN_AMD_CPT_D_MGT'}">
		                        <i class="fa fa-compass"></i>
		                        <i class="fa fa-angle-right" aria-hidden="true"></i>
	                        </c:when>
	                        <c:otherwise>                    
	                        <i class="fa fa-globe"></i>
	                        <i class="fa fa-angle-right" aria-hidden="true"></i>
	                        </c:otherwise>
	                    </c:choose>
	                    <!-- 功能名称 -->
								<lb:get key="global.${fun.ftn_ext_id }"/></a>

							<!-- 二级子功能 -->
                        <c:if test="${fn:length(fun.subFunctions) > 0 }">
		                        <ul>
		                            <c:forEach items="${fun.subFunctions}" var="test">
		                            	<li><a href="javascript:wb_utils_nav_go('${test.ftn_ext_id }', '${prof.usr_ent_id }', '${label_lan }')" title=""><lb:get key="global.${test.ftn_ext_id }"/></a></li>
		                            </c:forEach>
		                        </ul>
                        </c:if>
                    </li>
                    </c:forEach>
                </ul>
            </div>
        </div>