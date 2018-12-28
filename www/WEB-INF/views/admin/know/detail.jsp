<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<%@ include file="../../common/template.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />
<link rel="stylesheet" href="${ctx }/static/js/jquery.uploadify/uploadify.css"/>
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/course.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/sns.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/jquery.sns.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_cm_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/js/cwn_utils.js"></script>
<script type="text/javascript" src="${ctx}/static/js/jquery.uploadify/jquery.uploadify.js"></script>
<script type="text/javascript" src="${ctx}/static/js/jquery.cwn.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery.prompt.js"></script>

<title></title>

</head>
<body>
<input type="hidden" name="belong_module" value="FTN_AMD_SNS_MGT"/>
	 <title:get function="global.FTN_AMD_SNS_MGT"/>
<c:set var="titleValue">
    <c:choose>
    <c:when test="${type eq 'FAQ'}">
<!--       FAQ --><lb:get key="label_cm.label_core_community_management_12"/>
    </c:when>
    <c:otherwise>
<!--             问题详情 --><lb:get key="label_cm.label_core_community_management_20"/>
    </c:otherwise>
    </c:choose>
</c:set>
<ol class="breadcrumb wzb-breadcrumb">
 	<li><a href="javascript:wb_utils_gen_home(true);"><i class="fa wzb-breadcrumb-home fa-home"></i><lb:get key="global.lab_menu_started" /></a></li>
  	<li><a href="javascript:wb_utils_nav_go('${ftn_type }', '${prof.usr_ent_id }', '${label_lan }')">
	  	<c:choose>
		  	<c:when test="${ftn_type == 'FTN_AMD_Q_AND_A_VIEW' }">
				<wb:has-any-permission permission="FTN_AMD_Q_AND_A_VIEW">
					<lb:get key="label_cm.label_core_community_management_211"/><!-- 问答 -->
				</wb:has-any-permission>
		  	</c:when>
		  	<c:otherwise>
				<wb:has-any-permission permission="FTN_AMD_Q_AND_A_MAIN">
					<lb:get key="label_cm.label_core_community_management_1"/><!-- 问答管理 -->
				</wb:has-any-permission>
		  	</c:otherwise>
	  	</c:choose>
  </a></li>
  <li class="active">${titleValue }</li>
</ol> <!-- wzb-breadcrumb End-->
    
    
    <input type="hidden" id="answer_list_size" value="3">

<div class="panel wzb-panel">
<div class="panel-heading">${know_detail.que_title }</div>

<div class="panel-body">
<div class="wzb-wenda clearfix">
<div class="wzb-wenda-tit">
  <p>
    <c:choose>
        <c:when test="${know_detail.que_type == 'UNSOLVED'}">
            <i class="fa f38 fa-question"></i>
        </c:when>
        <c:when test="${know_detail.que_type == 'FAQ'}">
            <p class="wzb-wenda-size">FAQ</p>
        </c:when>
        <c:otherwise>
            <p class="wzb-wenda-size">OK</p>
        </c:otherwise>
    </c:choose>
 </p>
 <p>
 	 <c:choose>
        <c:when test="${know_detail.que_type == 'UNSOLVED'}">
            <lb:get key="label_cm.label_core_community_management_9"/>
        </c:when>
        <c:when test="${know_detail.que_type == 'FAQ'}">
            <lb:get key="label_cm.label_core_community_management_12"/>
        </c:when>
         <c:when test="${know_detail.que_type == 'SOLVED'}">
            <lb:get key="label_cm.label_core_community_management_10"/>
        </c:when>
                <c:when test="${know_detail.que_type == 'POPULAR'}">
            <lb:get key="label_cm.label_core_community_management_11"/>
        </c:when>
    </c:choose>
 		 
 </p>   
     
</div>

<div class="wzb-wenda-tool">
<div class="wzb-wenda-bd">
<c:if test="${isTADM or know_detail.que_create_ent_id == my_usr_id}">
	<c:choose>
	  	<c:when test="${ftn_type == 'FTN_AMD_Q_AND_A_VIEW' }">
			<wb:has-any-permission permission="FTN_AMD_Q_AND_A_VIEW">
				<c:if test="${know_detail.que_type != 'SOLVED'}"><!-- 问答 -->
					<a class="skin-color" href="javascript:;" onclick="delAnswer()"><lb:get key="credits_ZD_CANCEL_QUE"/></a> | <a class="skin-color" href="javascript:;"  onclick="knowSupplement()"><lb:get key="know_requestion_supplement"/></a>
				</c:if>
			   	<!-- 问题补充框 -->
			   	<div class="questipList" id="knowSupplement" style="display: none;">
			   		<div class="questipBg"><i class="fa fa-caret-up"></i></div>
			      	<div class="questipDesc" style="border:1px solid #ddd;">
			      		<c:choose>
			          		<c:when test="${know_detail.que_content != ''}"><c:set var="content" value="${know_detail.que_content}"/></c:when>
			          		<c:otherwise><c:set var="know_supplement_desc"><lb:get key="know_supplement_desc_1"/></c:set></c:otherwise>
			       		</c:choose>
			          
			      		<textarea id="queContent" class="queskang" style="width:440px;height:90px;" placeholder="${know_supplement_desc}">${content}</textarea>
			  			<div class="" id="editer2" style="width:440px;"></div>
			  			
			  			<lb:get key="know_supplement_desc_2"/><input type="button" class="questj" style="float:right;" name="myquesbtn" value='<lb:get key="button_ok"/>' onclick="changeQueContent()"/>    
			    	</div>
				</div> <!-- questipList End -->
			</wb:has-any-permission>
	  	</c:when>
	  	<c:otherwise>
			<wb:has-any-permission permission="FTN_AMD_Q_AND_A_MAIN"><!-- 问答管理 -->
			    <a class="wzb-link04 thickbox" 
			    	onclick="javascript:window.location.href='${ctx}/app/admin/know/add?id='+encrytor.cwnEncrypt('${know_detail.que_id}')+'&type=${know_detail.que_type}'"
			    	href="javascript:void(0);" 
			    	title="">
			    <lb:get key="global.button_update"/> </a> | <a class="wzb-link04" href="javascript:;" onclick="delAnswer();"><lb:get key="global.button_del"/> </a>
			</wb:has-any-permission>
	  	</c:otherwise>
  	</c:choose>
</c:if>
</div>     
</div>

<div class="wzb-wenda-info">
     <h3><a class="color-gray333" href="###" title="">${know_detail.que_title}</a></h3>
         
         <div id="que_supplement" style="width:100%;${know_detail.que_content != ''?'display:inline-block;':''}">
		     <c:if test="${know_detail.que_content != ''}">
	            <p><span class="color-gray999"><lb:get key="label_cm.label_core_community_management_22"/>：</span>${know_detail.que_content}</p>
	         </c:if>
		 </div>
		 <div id="know_content_img">
		 	<c:if test="${know_detail.fileList != null && fn:length(know_detail.fileList) > 0}">
			 	<c:forEach var="val" items="${know_detail.fileList }" varStatus="status">
			 		<c:choose>
				 		<c:when test="${val.mtf_file_type == 'url' }">
				 			<c:set var="url" value="${val.mtf_url}"></c:set>
				 		</c:when>
				 		<c:otherwise>
				 			<c:set var="url" value="${val.mtf_url}/${val.mtf_file_rename }"></c:set>
				 		</c:otherwise>
			 		</c:choose>
				 	<p class="mt10 f14" style="display:inline-block;margin:0 10px 10px 0;"><a href="${url}" target="_blank"><img width="60" src="${url}"/></a></p>
			 	</c:forEach>
		 	</c:if>
		 </div>
		 
      <div class=" color-gray999">
        <c:choose>
            <c:when test="${isTADM}">                                                    <!-- wzb-link04  链接 -->
                <lb:get key="label_cm.label_core_community_management_23"/>：<span class="color-gray333">${know_detail.regUser.usr_display_bil}</span>
            </c:when>
            <c:otherwise>
                <lb:get key="label_cm.label_core_community_management_23"/>：<span class="wzb-link04 color-blue00a" title="">${know_detail.regUser.usr_display_bil}</span>
            </c:otherwise>
        </c:choose>     
        <span class="margin-left15">
            <lb:get key="label_cm.label_core_community_management_19"/>：<span class="color-gray333"><fmt:formatDate value="${know_detail.que_create_timestamp}" pattern="yyyy-MM-dd"/></span>
        </span>
        <span class="margin-left15"><lb:get key="label_cm.label_core_community_management_24"/>：<span class="color-gray333">${know_detail.ask_num}</span></span>
        <c:if test="${ !empty know_detail.que_bounty && know_detail.que_bounty!=0}">                                            
            <span class="margin-left15"><lb:get key="know_bounty"/>：<span class="color-gray333">${know_detail.que_bounty}</span></span>
        </c:if>
       <c:if test="${know_detail.que_type != 'FAQ'}">
        <p>
            <span class="mr20">
<!--                         指定回答 -->
                <lb:get key="label_cm.label_core_community_management_25"/> ：
                <span class="color-gray333"><c:if test="${empty  users}">--</c:if></span>
                <c:forEach items="${users }" var="user">
                    <!-- 去掉链接     链接点击可以去到用户信息界面 -->
                  <!--  <a class="wzb-link04" href="${ctx}/app/personal/${user.usr_ent_id}" title=""> --><span data="${user.usr_ent_id}">${user.usr_display_bil }</span> <!-- </a>   -->                
                </c:forEach>
            </span>
        </p>
        </c:if>
    </div>
</div>
</div> <!-- wzb-wenda End-->

<!-- 最佳答案 -->
<c:if test="${know_detail.que_type == 'SOLVED'}">
	<div class="wzb-wenda-area">
		<div class="wzb-wenda-title"><strong class="wzb-wenda-break wzb-wenda-bg02"><span>
				<lb:get key="know_best_answer"/>
		     </span></strong></div>
		<div id="bestAnswer" class="wzb-wenda-list clearfix">
                           <div class="wzb-user wzb-user68">
                           	<c:choose>
                           		<c:when test="${isTADM}">
		                <span> 
		                    <img class="wzb-pic" src="${ctx}${best_answer.regUser.usr_photo}">
		                </span>
                           		</c:when>
                           		<c:otherwise>
		                    <img class="wzb-pic" src="${ctx}${best_answer.regUser.usr_photo}">
                           			<p style="display: none;" class="companyInfo">
		                    <lb:get key="label_cm.label_core_community_management_29"/>
		                </p>
		                <div style="width: 60px; height: 60px;" class="cornerTL">&nbsp;</div>
		                <div style="width: 60px; height: 60px;" class="cornerTR">&nbsp;</div>
		                <div style="width: 60px; height: 60px;" class="cornerBL">&nbsp;</div>
		                <div style="width: 60px; height: 60px;" class="cornerBR">&nbsp;</div>
                            	</c:otherwise>
                           	</c:choose>
                           </div>
		  	
		  	<div class="wzb-wenda-content">   <!-- wzb-wenda-content   会在右侧出现一条线-->
		          	<c:choose>
		          		<c:when test="${isTADM}">
		          			<span class="mr20 color-gray999"><lb:get key="know_ask_user"/>：</span><span class="">${best_answer.regUser.usr_display_bil}</span>
		          		</c:when>
		          		<c:otherwise>
		          		    <span class="mr20 color-gray999"><lb:get key="know_ask_user"/>：</span><span class="wzb-link04 color-blue00a mr20 skin-color" title="" >${best_answer.regUser.usr_display_bil}</span> 
		          		</c:otherwise>
		          	</c:choose>
		          	<em class="wzb-wenda-time"> 
		          		<span class="mr20 color-gray999"><lb:get key="know_ask_time"/>：</span><fmt:formatDate value="${best_answer.ans_create_timestamp}" pattern="yyyy-MM-dd"/>
		          	</em>
		          	
		          <p class="mt5 f14">${best_answer.ans_content}</p>
		          <c:if test="${best_answer.fileList != null && fn:length(best_answer.fileList) > 0}">
					 	<c:forEach var="val" items="${best_answer.fileList }" varStatus="status">
						 	<c:choose>
						 		<c:when test="${val.mtf_file_type == 'url' }">
						 			<c:set var="best_answer_url" value="${val.mtf_url}"></c:set>
						 		</c:when>
						 		<c:otherwise>
						 			<c:set var="best_answer_url" value="${val.mtf_url}/${val.mtf_file_rename }"></c:set>
						 		</c:otherwise>
					 		</c:choose>
						 	<p class="mt10 f14" style="display:inline-block;margin:0 10px 10px 0;"><a href="${best_answer_url}" target="_blank"><img width="60" src="${best_answer_url}"/></a></p>
					 	</c:forEach>
				 	</c:if>
		          
		          <c:if test="${best_answer.ans_refer_content != null and best_answer.ans_refer_content != ''}">
		          	<p class="mt5 grayC666"><lb:get key="know_reference_material"/>：${best_answer.ans_refer_content}</p>         
		    	  </c:if>
		    </div>
		  <!-- 去掉点赞功能 -->
		  <%-- 
	  		<div class="wzb-wenda-bar" >
	  			<a class="wzb-link05" href="javascript:;" ><i class="fa fa-thumbs-o-up"></i><p><lb:get key="rank_praise"/><span>${best_answer.snsCount.s_cnt_like_count}</span></p></a>
		    </div> 
		     --%>
		</div>
		<div class="wdarea-mess">
		     <span class="grayC333"><lb:get key="know_answer_good_ind"/></span>
		     <span class="mL5 grayC999"><lb:get key="know_currently"/><span class="mL5 mr5 f18 grayC333" id="evaluate_sum">${best_answer.ans_vote_total}</span><lb:get key="know_personal_evaluation"/></span>
		               <!--class = swop_bg 会显示不出背景 --> 
		     <em class="beta_bg" onclick="changeAnsVote('true')"><lb:get key="know_good"/></em><span class="f14" id="vote_for_rate">${good_rate}</span><span class="grayC999" id="vote_for_sum">（${best_answer.ans_vote_for == null? 0 : best_answer.ans_vote_for}）</span>
		     <em class="beta_bg" onclick="changeAnsVote('false')"><lb:get key="know_no_good"/></em><span class="f14" id="vote_down_rate">${not_good_rate}</span><span class="grayC999" id="vote_down_sum">（${best_answer.ans_vote_down == null? 0 : best_answer.ans_vote_down}）</span> 
		</div>
	</div> <!-- wdposi End-->
	<script type="text/javascript">
			var encrytor = wbEncrytor();
   			$(function(){
  					$("#bestAnswer .wzb-wenda-bar:last a").like({
  						count : ${best_answer.snsCount.s_cnt_like_count},
  						flag : ${best_answer.is_user_like},
  						id : ${best_answer.ans_id},
  						module: 'Answer',
  						tkhId : 0,
  					});
   			});
   			
   		</script>
</c:if>


<div class="wzb-wenda-area">
    <c:choose>
        <c:when test="${know_detail.que_type == 'FAQ'}">
              <div class="wzb-wenda-box">
                 <div class="wzb-wenda-list clearfix">           
                       <div class="wzb-user wzb-user68">
                            <span><img alt="" src="${answer.regUser.usr_photo}" class="wzb-pic"></span>
                            <p class="companyInfo" style="display: none;"><lb:get key="label_cm.label_core_community_management_29"/></p>
                            <div class="cornerTL" style="width: 60px; height: 60px;">&nbsp;</div>
                            <div class="cornerTR" style="width: 60px; height: 60px;">&nbsp;</div>
                            <div class="cornerBL" style="width: 60px; height: 60px;">&nbsp;</div>
                            <div class="cornerBR" style="width: 60px; height: 60px;">&nbsp;</div>
                      </div>
                      
                      <div class="wzb-wenda-content">  
                           <p class="color-gray666">
<!--                                                <a class="wzb-link04 pull-right" href="webda_peak.html" title="">设为最佳答案</a>  -->
                           <span class="mr20 color-gray999"><lb:get key="know_ask_user"/>：</span><a class="wzb-link04" href="###" title="">${answer.regUser.usr_display_bil}</a> 
                           <em class="wzb-wenda-time"><span class="mr20 color-gray999"><lb:get key="know_ask_time"/>：</span><fmt:formatDate value="${answer.ans_create_timestamp}" pattern="yyyy-MM-dd"/></em></p>
                           <p class="color-gray666">${answer.ans_content}</p> 
                           <c:if test="${answer.ans_refer_content!= null and answer.ans_refer_content ne '' }">
                           <p class="color-gray666">
<!--                            参考资料 -->
                                <lb:get key="know_reference_material"/> ：
                                ${answer.ans_refer_content}
                           </p>         
                           </c:if>
                      </div>
                      <%-- 
                      <div class="wzb-wenda-bar">
                           <a class="wzb-link05" href="javascript:" ><i class="fa fa-thumbs-o-up"></i><p><lb:get key="rank_praise"/>(<span>${answer.snsCount.s_cnt_like_count}</span>)</p></a>
                           <script type="text/javascript">
                                $(function(){
                                    $(".wzb-wenda-bar .wzb-link05").like({
                                        count : ${answer.snsCount.s_cnt_like_count},
                                        flag : ${answer.is_user_like},
                                        id : '${answer.ans_id}',
                                        module: 'Answer'
                                    });
                                })
                           </script>
                      </div>
                       --%>
                 </div>
          </div>
        </c:when>
        <c:otherwise>
            <div class="wzb-wenda-title"><strong class="wzb-wenda-break wzb-wenda-bg01"><span><lb:get key="label_cm.label_core_community_management_26"/></span></strong> <em id="wbshow" class="fr state skin-color" style="cursor:pointer;" onclick="changeSize()">
            <lb:get key="label_cm.label_core_community_management_27"/><i class="fa mL3 fa-angle-down"></i></em></div>
            <div id="answer_list"></div>
        </c:otherwise>
    </c:choose>
</div> <!-- wzb-wenda-area End-->

<c:if test="${know_detail.que_type == 'UNSOLVED'}">
<div class="wzb-wenda-area" >  <!-- style="display: none;" 隐藏我的回答 -->
     <div class="wzb-wenda-title"><strong class="wzb-wenda-break wzb-wenda-bg02"><span>
<!--      我的回答 --><lb:get key="label_cm.label_core_community_management_14"/>
     </span></strong></div>
         
     <table>
        <tr>
             <td class="wzb-form-label" valign="top">
<!--              发表信息 -->
				 <span class="wzb-form-star">*</span>
                 <lb:get key="know_publish_information"/>：
             </td>
             
             <td class="wzb-form-control">
             
                 <textarea placeholder="<lb:get key='label_core_community_management_127'/>" prompt="<lb:get key='label_core_community_management_127'/> " name="ansContent" class="wzb-textarea-03" style="width:400px;" id="formText"></textarea>
            	<div class="" id="editer" style="width:540px;"></div>
             </td>
        </tr>
        
        <tr>
             <td class="wzb-form-label">
<!--              参考资料 -->
                <lb:get key="know_reference_material"/>：
             </td>
             
             <td class="wzb-form-control">
                 <div class="wzb-selector"><input type="text" value="" name="ansReferContent" class="form-control"></div>
             </td>
        </tr>
        
        <tr>
             <td class="wzb-form-label"></td>
             
             <td class="wzb-form-control">
<!--                  如果您的回答是从其它地方引用，请表明出处。限 255 个字以内。 --><lb:get key="know_reference_material_desc"/>
             </td>
        </tr>
        
        <tr>
        	<td class="wzb-form-label"></td>
        	<td class="wzb-form-control">
        		<span class="wzb-form-star">*</span>
					<!-- 为必填 -->
					<lb:get key="label_rm.label_core_requirements_management_35" />	
        	</td>
        </tr>
        
        <tr>
             <td class="wzb-form-label"></td>
             
             <td class="wzb-form-control">
                 <input type="button" onclick="addKnowAnswer()" class="btn wzb-btn-blue wzb-btn-big margin-right15" value="<lb:get key='global.button_submit'/>" name="frmSubmitBtn">
             </td>
        </tr>
        
    </table>
</div> 
</c:if>
<!--  如果需要隐藏我的回答  将第一行放到C:if前面
<wb:has-any-permission permission="FTN_AMD_Q_AND_A_VIEW">
</wb:has-any-permission> -->
<!-- wzb-wenda-area End-->
 
 <div class="wzb-wenda-area" >  <!--style="display: none;" 隐藏相关问题功能 -->
     <div class="wzb-wenda-title"><strong class="wzb-wenda-break wzb-wenda-bg03"><span>
<!--      相关问题 --><lb:get key="label_cm.label_core_community_management_28"/>
     </span></strong></div>
         
     <ul class="wzb-list-10" id="relatedList">
<!--         相关问题 -->
     </ul>
</div> <!-- wzb-wenda-area End-->   

</div>
</div>
  <!-- </div> wzb-panel End-->
<script type="text/javascript">
	window.onload = function(){
		$("#editerfileRegion").find("i").trigger("click"); //进来或刷新先删除掉以前上传的照片
	}
</script>

<script type="text/javascript">
    var sns = new Sns();
    var encrytor = wbEncrytor();
    $(function(){
        
    	$("textarea[name='ansContent']").val(fetchLabel('label_core_community_management_127'));
    	$("textarea[name='ansContent']").focus(function(){
    		if($("textarea[name='ansContent']").val() == fetchLabel('label_core_community_management_127')){
    			$("textarea[name='ansContent']").val("");
    		}
    	});
    	$("textarea[name='ansContent']").blur(function(){
    		if($("textarea[name='ansContent']").val().trim() == ""){
    			$("textarea[name='ansContent']").val(fetchLabel('label_core_community_management_127'));
    		}
    	});
    	
        if(${know_detail.que_type != 'FAQ'}){
            $.ajax({
                url : '${ctx}/app/admin/know/answer/${know_detail.que_id}'  + '/' + $("#answer_list_size").val(),
                type : 'POST',
                dataType : 'json',
                success : function(data) {
                    getAnswerList(data, '${know_detail.que_type}');
                }
            });
        }
        
        $.ajax({
            url : '${ctx}/app/admin/know/relevantKnow/${know_detail.que_id}/${know_detail.que_type}/${know_detail.knowCatalog.kca_id}',
            type : 'POST',
            dataType : 'json',
            success : function(data) {
            	
            	for(var i = 0;i < data.know_list.results.length;i++){
            		var queId = data.know_list.results[i].que_id;
            		queId = encrytor.cwnEncrypt(queId);
            		data.know_list.results[i].que_id = queId;
            	}
                var html = $("#relatedQuestionTempelate").render(data.know_list.results)
                if(data.know_list.results.length == 0){
                    html = '<li>' + fetchLabel('label_core_community_management_126') + '</li>';
                }
                $("#relatedList").html(html);
            }
        });

    })
    
    //获取回答列表
    function getAnswerList(data, queType){
        if(data.ask_list.totalRecord < 4){
            $("#wbshow").hide();
        } else {
            $("#wbshow").show();
        }
        var html = '';
        $("#answer_list").html('');
        for(var i=0;i<data.ask_list.results.length;i++){
            var ask_detail = data.ask_list.results[i];
            var is_specify = false;
			$(".wzb-wenda-info .mr20 span").each(function(){
				if($(this).attr("data") == ask_detail.ans_create_ent_id){
					is_specify = true;
				}
			});
            var set_del_ind = ask_detail.ans_create_ent_id == ${prof.usr_ent_id} ? true : false;
            $.extend(ask_detail,{set_best_ind: ${prof.usr_ent_id} == ${know_detail.que_create_ent_id} && queType == 'UNSOLVED', isTADM: ${isTADM} ,is_specify:is_specify,set_del_ind: set_del_ind} )
            ask_detail.ans_content=replaceVlaue(ask_detail.ans_content);
            $("#answer_list").append($("#answer-template").render(ask_detail));
            $("#answer_list .wzb-wenda-bar:last a").like({
                count : data.ask_list.results[i].snsCount.s_cnt_like_count,
                flag : data.ask_list.results[i].is_user_like,
                id : data.ask_list.results[i].ans_id,
                module: 'Answer',
                tkhId : 0
            });
          
            //图片显示
			$.templates({
				fileTemplate : '<p class="mt10 f14"><a href="${ctx}{{>url}}" target="_blank">{{>name}}</a></p>',
				imgTemplate : '<p class="mt10 f14" style="display:inline-block;margin:0 10px 10px 0;"><a href="${ctx}{{>url}}" target="_blank"><img width="60" src="${ctx}{{>url}}"/></a></p>'
			})
			var fileList = ask_detail.fileList;
			if(fileList != undefined){
				$.each(fileList,function(i, val){
					var url,name;
					name = val.mtf_file_name;
					url = val.mtf_url;
					if(val.mtf_file_type == 'url'){
						name = val.mtf_url;
					} else {
						url = url + "/" + val.mtf_file_rename;
					}
					var p ={
						name : name,
						url : url		
					}
					var  fileHtml ;
					if(val.mtf_type=='Img'){
						fileHtml = $.render.imgTemplate(p)
					} else {
						fileHtml = $.render.fileTemplate(p);
					}
					$("#answer_list #ans_content_"+ask_detail.ans_id).after(fileHtml);
				});
			}
        }
        if(data.ask_list.results.length == 0){
            html += '<div class="wdarea-list clearfix grayC999" style="padding:5px 5px 5px 10px;"><th>' + fetchLabel('label_core_community_management_125') + '</th></div>';
            $("#answer_list").html(html);
        }
    }
    
    //提交回答
    function addKnowAnswer(){
        if(getChars($("textarea[name='ansContent']").val()) <= 0 || $("textarea[name='ansContent']").val() == fetchLabel('label_core_community_management_127')){
            Dialog.alert(fetchLabel('label_core_community_management_128'));
            return;
        }
        if(getChars($("textarea[name='ansContent']").val()) > 2000){
       	    Dialog.alert(fetchLabel('label_core_community_management_219'));
            return;
        }
        if(getChars($("input[name='ansReferContent']").val()) > 800){
            Dialog.alert(fetchLabel('label_core_community_management_129'));
            return;
        }
        $.ajax({
            url : '${ctx}/app/admin/know/addKnowAnswer/${know_detail.que_id}',
            type : 'post',
            dataType : 'json',
            data : {
                ansContent: $("textarea[name='ansContent']").val(),
                ansReferContent : $("input[name='ansReferContent']").val()
            },
            success : function() {
                $.ajax({
                    url : '${ctx}/app/admin/know/answer/${know_detail.que_id}/' + $("#answer_list_size").val(),
                    type : 'POST',
                    dataType : 'json',
                    success : function(data) {
                        getAnswerList(data, '${know_detail.que_type}');
                        $("textarea[name='ansContent']").val(fetchLabel('label_core_community_management_127'));
                        $("input[name='ansReferContent']").val('');
                        $("#editerfileRegion").empty();
                    }
                });
            }
        });
    }
    
    //取消提问
    function delAnswer(){
        var text = fetchLabel('label_core_community_management_130');
        if(${isTADM}){
            text = fetchLabel('label_core_community_management_131');
        }
        Dialog.confirm({text:text, callback: function (answer) {
                if(answer){
                    $.ajax({
                        url : '${ctx}/app/admin/know/delQuestion/${know_detail.que_id}',
                        type : 'POST',
                        success : function() {
                            window.location.href = '${ctx}/app/admin/know/allKnow?ftn_type=${ftn_type }';
                        }
                    });
                }
            }
        });
    }
    
    //问题补充框显示与隐藏
    function knowSupplement(){
        if($("#knowSupplement:hidden").length > 0){
            $("#knowSupplement").show();
        } else {
            $("#knowSupplement").hide();
        }
    }
    
    //展开收起
    function changeSize(){
        if($("#answer_list_size").val() == 3){
            $("#answer_list_size").val(9999);
            $("#wbshow").html(fetchLabel('label_core_community_management_132') + '<i class="fa mL3 fa-angle-up"></i>');
        } else {
            $("#answer_list_size").val(3);
            $("#wbshow").html(fetchLabel('label_core_community_management_27') + '<i class="fa mL3 fa-angle-down"></i>');
        }
        $.ajax({
            url : '${ctx}/app/admin/know/answer/${know_detail.que_id}'  + '/' + $("#answer_list_size").val(),
            type : 'POST',
            dataType : 'json',
            success : function(data) {
                getAnswerList(data, '${know_detail.que_type}');
            }
        });
    }
    
    //问题补充提交
    function changeQueContent(){
        if(getChars($("#queContent").val()) > 2000){
            Dialog.alert(fetchLabel('label_core_community_management_133'));
            return;
        }
        $.ajax({
            url : '${ctx}/app/admin/know/changeQueContent/${know_detail.que_id}',
            data : {
                queContent : $("#queContent").val()
            },
            type : 'POST',
            success : function() {
            	//获取问答信息，更新图片
				$.ajax({
					url : '${ctx}/app/admin/know/detail/json/${know_detail.que_type}/${know_detail.que_id}',
					type : 'POST',
					dataType : 'json',
					success : function(data) {
						//图片显示
						$.templates({
							fileTemplate : '<p class="mt10 f14"><a href="${ctx}{{>url}}" target="_blank">{{>name}}</a></p>',
							imgTemplate : '<p class="mt10 f14" style="display:inline-block;margin:0 10px 10px 0;"><a href="${ctx}{{>url}}" target="_blank"><img width="60" src="${ctx}{{>url}}"/></a></p>'
						})
						var fileList = data.know_detail.fileList;
						if(fileList != undefined){
							var  fileHtml = "";
							$.each(fileList,function(i, val){
								var url,name;
								name = val.mtf_file_name;
								url = val.mtf_url;
								if(val.mtf_file_type == 'url'){
									name = val.mtf_url;
								} else {
									url = url + "/" + val.mtf_file_rename;
								}
								var p ={
									name : name,
									url : url		
								}
								
								if(val.mtf_type=='Img' ){
									fileHtml += $.render.imgTemplate(p)
								} else {
									fileHtml += $.render.fileTemplate(p);
								}
							});
							$("#know_content_img").html(fileHtml);
						}							
					}
				});
				if($("#queContent") != undefined){
					if($("#queContent").val() != ''){
						var html = '<p class="mt10"><span class="grayC999">' + fetchLabel('label_core_community_management_134') + fetchLabel('label_core_community_management_135') 
                        + '： </span>' + $("#queContent").val() + '</p>';
            			$("#que_supplement").html(html);
						$("#que_supplement").show();
					}else{
						$("#que_supplement").hide();
					}
				}
                $("#knowSupplement").hide();
                $("#editer2fileRegion").empty();
            }
        });
    }
    
    //设为最佳回答
    function setBestAnswer(ansId, ansEntId){
        Dialog.confirm({text:fetchLabel('label_core_community_management_136'), callback: function (answer) {
                if(answer){
                    $.ajax({
                        url : '${ctx}/app/admin/know/setBestAnswer/' + ansId + '/' + ansEntId + '/${know_detail.que_id}',
                        type : 'POST',
                        success : function() {
                            window.location.href = '${ctx}/app/admin/know/detail/SOLVED/${know_detail.que_id}';
                        }
                    });
                }
            }
        });
    }
    
	//删除该条回答
	function delThisAnswer(ansId, ansEntId){
		Dialog.confirm({text:fetchLabel('know_del_answer_confirm'), callback: function (answer) {
				if(answer){
					$.ajax({
						url : '${ctx}/app/admin/know/delThisAnswer/' + ansId + '/' + ansEntId + '/${know_detail.que_id}',
						type : 'POST',
						success : function() {
							window.location.reload();
						}
					});
				}
			}
		});
	}
    
    //回答评价
    function changeAnsVote(goodInd){
        $.ajax({
            url : '${ctx}/app/admin/know/changeAnsVote/${know_detail.que_id}/' + goodInd,
            type : 'POST',
            dataType : 'json',
            success : function(data) {
                if(data.evaluationInd == true){
                    Dialog.alert(fetchLabel('label_core_community_management_137'));
                } else {
                    $("#evaluate_sum").html(data.answerSituation.ans_vote_total);
                    $("#vote_for_rate").html(data.good_rate);
                    $("#vote_for_sum").html('（' + data.answerSituation.ans_vote_for + '）');
                    $("#vote_down_rate").html(data.not_good_rate);
                    $("#vote_down_sum").html('（' + data.answerSituation.ans_vote_down + '）');
                    $("#comment_ind").val("true");
                }
            }
        })
    }
    
    function showCatalog(){
        var iTop = (window.screen.availHeight-30-500)/2;
        var iLeft = (window.screen.availWidth-10-500)/2;   
        str_feature = 'width=' + 450 + ',height=' + 500 + ',scrollbars=' + 'yes' + ',resizable=' + 'yes'+ ',top='+ iTop + ',left='+iLeft;
        prep_win = wbUtilsOpenWin('', 'tree_prep', false, str_feature);
        prep_win.location.href = '${ctx}/app/admin/know/knowCatalogTree?que_id_lst=${know_detail.que_id}';
        prep_win.focus();
    }
    
  /* ==================================================================================== */
  //添加图片选择
  $("#editer").cwnEditer({
         sessionid : '${pageContext.session.id}' ,
         btnTitle : cwn.getLabel("label_core_community_management_113"),
         fileInitUrl : contextPath +  '/app/upload/Know/uncommit',
         fileDelUrl : contextPath +  '/app/upload/del/',
         uploadBtns : [
             {id:'uploadImg', type : 'image', popup : true, name:'<i class="fa fa-file-image-o"></i>&nbsp;' + cwn.getLabel('label_core_community_management_115'), uploadUrl : contextPath + '/app/upload/handle?module=Know&type=Img', onlineUrl : contextPath + '/app/upload/online?module=Know&type=Img'}
         ],
         module : 'Know',
         onlyImg : true
     });
  /* ==================================================================================== */
  /* ==================================================================================== */
  //问答补充添加图片选择
  $("#editer2").cwnEditer({
         sessionid : '${pageContext.session.id}' ,
         btnTitle : cwn.getLabel("label_core_community_management_113"),
         fileInitUrl : contextPath +  '/app/upload/KnowQuestion/uncommit',
         fileDelUrl : contextPath +  '/app/upload/del/',
         uploadBtns : [
             {id:'uploadImg2', type : 'image', popup : true, name:'<i class="fa fa-file-image-o"></i>&nbsp;' + cwn.getLabel('label_core_community_management_115'), uploadUrl : contextPath + '/app/upload/handle?module=KnowQuestion&type=Img', onlineUrl : contextPath + '/app/upload/online?module=KnowQuestion&type=Img'}
         ],
         module : 'KnowQuestion',
         onlyImg : true
     });
  /* ==================================================================================== */
</script>
<script id="answer-template000" type="text/x-jsrender">   <!--讲师端-->
    <div class="wdarea-list clearfix">
        <div class="userpic user82">
            {{if !isTADM}}
                <span class="wdarea-pic"> 
                    <img src="${ctx}{{>regUser.usr_photo}}">
                </span>
            {{else}}
                <span class="wdarea-pic"> 
                    <img src="${ctx}{{>regUser.usr_photo}}">
                </span>
            {{/if}}
            {{if !isTADM}}
                <p style="display: none;" class="companyInfo">
                    <lb:get key="label_cm.label_core_community_management_29"/>
                </p>
                <div style="width: 60px; height: 60px;" class="cornerTL">&nbsp;</div>
                <div style="width: 60px; height: 60px;" class="cornerTR">&nbsp;</div>
                <div style="width: 60px; height: 60px;" class="cornerBL">&nbsp;</div>
                <div style="width: 60px; height: 60px;" class="cornerBR">&nbsp;</div>
            {{/if}}
        </div>
        <div class="wdarea-info" {{if isTADM}}style="border: 0;width: 564px;"{{/if}}>
            <p class="grayC666">
                {{if set_best_ind && !isTADM}}
                    <a class="fr skin-color" href="javascript:;" onclick="setBestAnswer({{>ans_id}}, {{>ans_create_ent_id}})">
                        <lb:get key="label_cm.label_core_community_management_30"/>
                    </a>
                {{/if}}
             
                {{if !isTADM}}
                    <lb:get key="label_cm.label_core_community_management_31"/>：<span title="" class="mr20 skin-color color-blue00a">{{>regUser.usr_display_bil}}</span> 
                {{else}}
                    <lb:get key="label_cm.label_core_community_management_31"/>：<span class="mr20 skin-color">{{>regUser.usr_display_bil}}</span>
                {{/if}}
                <lb:get key="label_cm.label_core_community_management_32"/>：{{>ans_create_timestamp.substring(0,10)}}
            </p>
            <p class="mt5 f14" id="ans_content_{{>ans_id}}">{{:ans_content}}</p>
            {{if ans_refer_content}}
                <p class="mt5 grayC666"><lb:get key="label_cm.label_core_community_management_33"/>:{{>ans_refer_content}}</p>
            {{/if}}
        </div>
        {{if !isTADM}}
            <div class="wdarea-tool">
                <a title="" href="javascript:;" data="false">
                    <i class="fa grayC999  f30 fa-thumbs-o-up skin-color"></i>
                    <p class="grayC666 mt5 f14"><lb:get key="label_cm.label_core_community_management_34"/>(<span>{{>snsCount.s_cnt_like_count}}</span>)</p>
                </a>
            </div>
        {{/if}}
    </div>
</script>

<script id="answer-template" type="text/x-jsrender">       <!--学员端-->
<div class="wzb-wenda-list clearfix">           
    <div class="wzb-user wzb-user68">
            {{if !isTADM}}
                <span> 
                    <img class="wzb-pic" src="${ctx}{{>regUser.usr_photo}}">
                </span>
            {{else}}
                <span> 
                    <img class="wzb-pic" src="${ctx}{{>regUser.usr_photo}}">
                </span>
            {{/if}}
            {{if !isTADM}}
                <p style="display: none;" class="companyInfo">
                    <lb:get key="label_cm.label_core_community_management_29"/>
                </p>
                <div style="width: 60px; height: 60px;" class="cornerTL">&nbsp;</div>
                <div style="width: 60px; height: 60px;" class="cornerTR">&nbsp;</div>
                <div style="width: 60px; height: 60px;" class="cornerBL">&nbsp;</div>
                <div style="width: 60px; height: 60px;" class="cornerBR">&nbsp;</div>
            {{/if}}
    </div>

    <div class="wzb-wenda-content">
            {{if !isTADM}}
                <span class="mr20 color-gray999"><lb:get key="know_ask_user"/>：</span><span class="wzb-link04 color-blue00a mr20 skin-color">{{>regUser.usr_display_bil}}</span> 
            {{else}}
               <span class="mr20 color-gray999"> <lb:get key="know_ask_user"/>：</span><span class="">{{:regUser.usr_display_bil}}</span>
            {{/if}}  
    
			{{if !set_best_ind}}
				<wb:has-any-permission permission="FTN_AMD_Q_AND_A_MAIN"> <!--拥有管理权限，并且不是提问者本人显示这个删除-->
					<div style="float:right;">
							<a class="wzb-link04" href="javascript:;" onclick="delThisAnswer({{>ans_id}}, {{>ans_create_ent_id}})">
								<lb:get key="know_del_answer"/> 
							</a> 
					</div>
				</wb:has-any-permission>
             {{/if}} 

            {{if set_best_ind}}
					<!--判断当前用户是否跟提问用户是同一个，如果是就显示删除按钮-->
					<div style="float:right;">
							<a class="wzb-link04" href="javascript:;" onclick="delThisAnswer({{>ans_id}}, {{>ans_create_ent_id}})">
								<lb:get key="know_del_answer"/>
							</a>
					</div>   

					<!--当该回答人 是跟提问人 一样，就不能设置为最佳答案 -->
                     {{if !set_del_ind}}
                        <div style="float:right;">
                    		 &nbsp;|&nbsp;
               		   </div>

                      <a class="wzb-link04 pull-right" href="javascript:;" onclick="setBestAnswer({{>ans_id}}, {{>ans_create_ent_id}})">
                         <lb:get key="know_set_best_answer"/> 
                      </a>
                    {{/if}}
            {{/if}}     
                
            {{if is_specify }}
                <i class="glyphicon glyphicon-star" style="color: #33CC33;" />
            {{/if}}
            <em class="wzb-wenda-time"><span class="mr20 color-gray999"> <lb:get key="know_ask_time"/>：</span>{{>ans_create_timestamp.substring(0,10)}}</p>
                
            <p class="color-gray666" id="ans_content_{{>ans_id}}">{{:ans_content}}</p> 

            <p class="color-gray666"><lb:get key="know_reference_material"/>：
              {{if ans_refer_content}}
                {{>ans_refer_content}} </p>         
              {{/if}}

              {{if !ans_refer_content}}
                -- </p>         
              {{/if}}
             
    </div>

</div>
</script>

<!--   去掉相关回答 点赞功能   class=wzb-wenda-content  
    <div class="wzb-wenda-bar">
        <a class="wzb-link05" href="javascript:;" ><i class="fa fa-thumbs-o-up"></i><p><lb:get key="rank_praise"/><span>{{>snsCount.s_cnt_like_count}}</span></p></a>
    </div>  -->


<script type="text/x-jsrender" id="relatedQuestionTempelate">
<li onclick="javascript:window.location.href='/app/admin/know/detail/{{:que_type}}/{{:que_id}}'">
    <span class="pull-right margin-right15 color-gray999">{{:que_create_timestamp.substring(0,10) }}</span>
    <a href="/app/admin/know/detail/{{:que_type}}/{{:que_id}}" class="wzb-link03"><i class="fa fa-square"></i>{{:que_title}}</a>
</li>
</script>

<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_cm_${lang}.js"></script>

</body>


</html>