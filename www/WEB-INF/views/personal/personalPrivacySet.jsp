<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<%@ include file="../common/template.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/course.js"></script>
<title></title>
<script type="text/javascript">
	$(function(){
		$("select[name='s_set_my_learning_record']").children().eq(${snsSetting.s_set_my_learning_record}).attr("selected",true);
		$("select[name='s_set_my_credit']").children().eq(${snsSetting.s_set_my_credit}).attr("selected",true);
		$("select[name='s_set_my_files']").children().eq(${snsSetting.s_set_my_files}).attr("selected",true);
		/* $("select[name='s_set_doing']").children().eq(${snsSetting.s_set_doing}).attr("selected",true); */
		$("select[name='s_set_my_collection']").children().eq(${snsSetting.s_set_my_collection}).attr("selected",true);
		$("select[name='s_set_group']").children().eq(${snsSetting.s_set_group}).attr("selected",true);
		$("select[name='s_set_like']").children().eq(${snsSetting.s_set_like}).attr("selected",true);
		$("select[name='s_set_my_follow']").children().eq(${snsSetting.s_set_my_follow}).attr("selected",true);
		$("select[name='s_set_my_fans']").children().eq(${snsSetting.s_set_my_fans}).attr("selected",true);
		$("select[name='s_set_my_learning_situation']").children().eq(${snsSetting.s_set_my_learning_situation}).attr("selected",true);
		if(${sns_enabled} == false){
			$("select").find("option:eq(1)").remove();
		}
		
		if("${result_msg}" == "update_ok"){
			Dialog.alert(fetchLabel("update_ok"));
		}
	})
</script>
</head>
<body>
<div class="xyd-wrapper">
	<div id="main" class="xyd-main clearfix">
		<jsp:include page="personalMenu.jsp"></jsp:include>
		
		<div class="xyd-article">
            <div class="wzb-title-2"><i class="fa font-size16 margin-right10 fa-cog"></i><lb:get key="usr_privacy_set"/></div>
            
            <form action="${ctx}/app/personal/personalPrivacySet/update/changePrivacySet" method="post">
			<dl class="xyd-privacy clearfix">
				<dd>
					 <div class="xyd-privacy-title"><lb:get key="usr_set_who_browse"/></div>
					 <lb:get key="usr_set_tip" />
				</dd>
				
				<dt>
					 <table class="xyd-privacy-table">
					 	<tr><!-- 档案 -->
							  <th><lb:get key="personal_my_files"/> ：</th>
							  
							  <td>
								  <select class="form-control" style="width:auto;padding: 0 0 0 8px;" name="s_set_my_files">
										  <option value="0"><lb:get key="usr_set_all_see"/></option>
										  <option value="1"><lb:get key="usr_set_my_attention_see"/></option>
										  <option value="2"><lb:get key="usr_set_all_not_see"/></option>
								  </select>
							  </td>
						 </tr>
						 
						 <tr><!-- 积分 -->
							  <th><lb:get key="personal_my_credit"/> ：</th>
							  
							  <td>
								  <select class="form-control" style="width:auto;padding: 0 0 0 8px;" name="s_set_my_credit">
										 <option value="0"><lb:get key="usr_set_all_see"/></option>
										 <option value="1"><lb:get key="usr_set_my_attention_see"/></option>
										 <option value="2"><lb:get key="usr_set_all_not_see"/></option>
								  </select>
							  </td>
						 </tr>
						 
						 <%-- <c:if test="${sns_enabled == true}">
						 <tr>
							  <th><lb:get key="personal_my_dynamic"/> :</th>
							  
							  <td>
								  <select class="form-control" style="width:auto;" name="s_set_doing">
										  <option value="0"><lb:get key="usr_set_all_see"/></option>
										  <option value="1"><lb:get key="usr_set_my_attention_see"/></option>
										  <option value="2"><lb:get key="usr_set_all_not_see"/></option>
								  </select>
							  </td>
						 </tr>
						 </c:if> --%>
						 
						 <tr><!-- 收藏 -->
							  <th><lb:get key="personal_my_collection"/> ：</th>
							  
							  <td>
								  <select class="form-control" style="width:auto;padding: 0 0 0 8px;" name="s_set_my_collection">
										  <option value="0"><lb:get key="usr_set_all_see"/></option>
										  <option value="1"><lb:get key="usr_set_my_attention_see"/></option>
										  <option value="2"><lb:get key="usr_set_all_not_see"/></option>
								  </select>
							  </td>
						 </tr>
						 
						 <c:if test="${sns_enabled == true}">
						 <tr><!-- 群组 -->
							  <th><lb:get key="personal_my_group"/> ：</th>
							  
							  <td>
								  <select class="form-control" style="width:auto;padding: 0 0 0 8px;" name="s_set_group">
										  <option value="0"><lb:get key="usr_set_all_see"/></option>
										  <option value="1"><lb:get key="usr_set_my_attention_see"/></option>
										  <option value="2"><lb:get key="usr_set_all_not_see"/></option>
								  </select>
							  </td>
						 </tr>
						 
						 <tr><!-- 被赞 -->
							  <th><lb:get key="personal_ta_praise"/> ：</th>
							  
							  <td>
								  <select class="form-control" style="width:auto;padding: 0 0 0 8px;" name="s_set_like">
										  <option value="0"><lb:get key="usr_set_all_see"/></option>
										  <option value="1"><lb:get key="usr_set_my_attention_see"/></option>
										  <option value="2"><lb:get key="usr_set_all_not_see"/></option>
								  </select>
							  </td>
						 </tr>
						 
						 <tr><!-- 关注 -->
							  <th><lb:get key="personal_my_attention"/> ：</th>
							  
							  <td>
								  <select class="form-control" style="width:auto;padding: 0 0 0 8px;" name="s_set_my_follow">
										  <option value="0"><lb:get key="usr_set_all_see"/></option>
										  <option value="1"><lb:get key="usr_set_my_attention_see"/></option>
										  <option value="2"><lb:get key="usr_set_all_not_see"/></option>
								  </select>
							  </td>
						 </tr>
						 
						 <tr><!-- 粉丝 -->
							  <th><lb:get key="personal_my_fans"/> ：</th>
							  
							  <td>
								  <select class="form-control" style="width:auto;padding: 0 0 0 8px;" name="s_set_my_fans">
										  <option value="0"><lb:get key="usr_set_all_see"/></option>
										  <option value="1"><lb:get key="usr_set_my_attention_see"/></option>
										  <option value="2"><lb:get key="usr_set_all_not_see"/></option>
								  </select>
							  </td>
						 </tr>
						 </c:if>
						 
						 <tr><!-- 学习记录 -->
							  <th><lb:get key="personal_my_learning_record"/> ：</th>
							  
							  <td>
								  <select class="form-control" style="width:auto;padding: 0 0 0 8px;" name="s_set_my_learning_record">
										  <option value="0"><lb:get key="usr_set_all_see"/></option>
										  <option value="1"><lb:get key="usr_set_my_attention_see"/></option>
										  <option value="2"><lb:get key="usr_set_all_not_see"/></option>
								  </select>
							  </td>
						 </tr>
						 
						 <tr> <!-- 学习概况 -->
							  <th><lb:get key="personal_my_learning_situation"/> ：</th>
							  
							  <td>
								  <select class="form-control" style="width:auto;padding: 0 0 0 8px;" name="s_set_my_learning_situation">
										  <option value="0"><lb:get key="usr_set_all_see"/></option>
										  <option value="1"><lb:get key="usr_set_my_attention_see"/></option>
										  <option value="2"><lb:get key="usr_set_all_not_see"/></option>
								  </select>
							  </td>
						 </tr>
						 
						 <tr>
							  <th></th>
							  
							  <td>
								  <input type="submit" class="btn wzb-btn-orange margin-top10 wzb-btn-big" name="pertxt" value='<lb:get key="button_ok"/>'/>
							  </td>
						 </tr>
					 </table>    
				</dt>
			</dl> <!-- xyd-privacy End-->
			</form>
		</div> <!-- xyd-article End-->
	</div>
</div> <!-- xyd-wrapper End-->
</body>
</html>