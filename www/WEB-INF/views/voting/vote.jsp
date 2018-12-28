<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<%@ include file="../common/template.jsp"%>
<!DOCTYPE html>
<html>
<head>

<title></title>
</head>
<body>
	<div class="xyd-wrapper">
		<div class="xyd-main clearfix">
			<jsp:include page="../personal/personalMenu.jsp"></jsp:include>
			<div class="xyd-article">
				<h1 class="wzb-title-14">${v.vot_title }</h1>

				<p>${v.vot_content }</p>
				<form id="voteForm" action="${ctx}/app/voting/vote?vot_id=${v.vot_id}" method="POST">
				<table class="margin-top15">
					<tbody>
						<tr>
							<td class="wzb-form-label"></td>

							<td class="wzb-form-control">
								<c:set var="aType" value="${v.voteQuestion.vtq_type}" />
								<c:forEach items="${v.voteQuestion.voteOptions}" var="vo" varStatus="status">
									<p>
										
										<label for="fenlei-0${status.index+1 }" class="wzb-form-checkbox"><input
											type="${aType eq 'MC_S'? 'radio':'checkbox'}" value="${vo.vto_id }" id="fenlei-0${status.index+1 }" name="answers">${vo.vto_desc}</label>
									</p>
								</c:forEach>
								
							</td>
						</tr>
					</tbody>
				</table>

				<div class="wzb-bar">
					<input type="submit"
						class="btn wzb-btn-blue wzb-btn-big margin-right15" value="<lb:get key="label_rm.label_core_requirements_management_2"/>"
						name="frmSubmitBtn"><!-- 投票 --> <input type="button"
						onclick="window.location.href='${ctx}/app/voting/viewResult?enc_vot_id=${enc_vot_id }'" class="btn wzb-btn-blue wzb-btn-big"
						value="<lb:get key="label_core_requirements_management_7"/>" name="frmSubmitBtn"><!-- 查看结果 -->
				</div>
				</form>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="${ctx}/js/jquery.validate.js"></script>
	<script type="text/javascript">
		$("#voteForm").validate({
			submitHandler:function(form){
				var unchecked = true;
				$("input[name='answers']").each(function(){
					if($(this).get(0).checked){
						unchecked = false;
					}
				});
				if(unchecked){
		            Dialog.alert(fetchLabel("label_voting_validate_tips"));
					return;
				}
	            form.submit();
	        },
			errorPlacement:function(error,element){
				error.appendTo(element.parent().parent());
			}
		});
	</script>
</body>
</html>
