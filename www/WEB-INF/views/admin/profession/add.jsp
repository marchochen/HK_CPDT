<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<%@ include file="../../common/meta.tree.jsp"%>
<%@ include file="../../common/meta.kindeditor.jsp"%>
<%@ include file="../../common/meta.datepicker.jsp"%>
</head>
<body>
<input type="hidden" name="belong_module" value="FTN_AMD_STUDY_MAP_MGT"/>
<input type="hidden" id="sid"/>
<input type="hidden" id="time"  value="${fn:length(profession.gradeList)}"/>

 	 <title:get function="global.FTN_AMD_STUDY_MAP_MGT"/>
	<ol class="breadcrumb wzb-breadcrumb">
		<li><a href="javascript:wb_utils_gen_home(true);"><i
				class="fa wzb-breadcrumb-home fa-home"></i> 
				<lb:get key="label_lm.label_core_learning_map_1" /></a></li>
		<li><a href="${ctx}/app/admin/profession"><!-- 职级发展序列 -->
		<lb:get key="label_lm.label_core_learning_map_103" /></a></li>
		<li class="active">
		
				<c:choose>
				<c:when test="${type =='update'}">
					<lb:get key="label_lm.label_core_learning_map_8"/> <!-- 修改-->
				</c:when>
				<c:otherwise>
					<lb:get key="label_lm.label_core_learning_map_9"/><!-- 创建 -->
				</c:otherwise>
			</c:choose>
		
		
		</li>
	</ol>
	<!-- wzb-breadcrumb End-->
	<div class="panel wzb-panel">
		<c:choose>
				<c:when test="${type =='update'}">
		<form id="adminForm" method="post" action="${ctx}/app/admin/profession/update">
		<input type="hidden" name="pfs_id" value="${profession.pfs_id }">
		</c:when>
				<c:otherwise>
				
		<form id="adminForm" method="post" action="${ctx}/app/admin/profession/doadd">
					</c:otherwise>
			</c:choose>
		<div class="panel-heading">
		
		
			<c:choose>
				<c:when test="${type =='update'}">
					<lb:get key="label_lm.label_core_learning_map_8"/> <!-- 修改-->
				</c:when>
				<c:otherwise>
					<lb:get key="label_lm.label_core_learning_map_9"/><!-- 创建-->
				</c:otherwise>
			</c:choose>
				
			 </div>
	 

		<div class="panel-body">
			<div class="">
				<table>
					<tbody>
						<tr>
							<td  class="wzb-form-label"><span class="wzb-form-star">*</span><!-- 标题 -->
							<lb:get key="label_lm.label_core_learning_map_5"/>：</td>

							<td class="wzb-form-control" >
								<div class="wzb-selector" style="display:inline-block;">
									<input type="text" class="form-control" id="pfs_title" onblur="this.value=this.value.trim();"   name="pfs_title" value="${profession.pfs_title }"/> 
									<input type="hidden" class="form-control" id="old_title"   value="${profession.pfs_title }"/>
								</div>    <label for="pfs_title" class="error" id="error_title" style="display:none"></label>
							 </td>
						</tr>

                    <c:choose>
				<c:when test="${type =='update'}">
				<c:forEach items="${profession.gradeList }" var="grade" varStatus="s">
				            <c:choose>
				<c:when test="${s.index ==0 }">
				<tr  id="node${s.index+1 }">
				       </c:when>
				       <c:otherwise>
				       <tr class="wzb-form-jiedian"  id="node${s.index+1 }">
				       </c:otherwise>
				       </c:choose>  
                        <td colspan="2">
                            <table>
                                <tbody>
                                	<c:if test="${s.index !=0 }">  
                                	<tr>
                                        <td style="padding-top:28px;"></td>
                                        <td style="padding-top:28px;"><i class="fa fa-times-circle wzb-form-close" onclick="deleteHTMLNode('node${s.index+1 }')"></i></td>
                                    </tr>
                                    </c:if>	   
										<tr id="tr_content">
							<td class="wzb-form-label"><span class="wzb-form-star">*</span><!-- 职级 -->
							<lb:get key="label_lm.label_core_learning_map_4"/>：</td>
							<div class="wzb-selector" id="n${s.index+1 }">
							<div><input type="hidden" id="g${s.index+1 }" name="gid" value="${grade.ugr_ent_id }"></div>
							<td class="wzb-form-control">		
									<input type="text" class="form-control"  placeholder="<lb:get key='label_core_learning_map_101' />"  readonly="readonly" id="name${s.index+1 }" name="name" value="${grade.ugr_display_bil }" style="width:300px;height: 30px;"><input type="button" name="" value="<lb:get key='button_select' />" class="zhiji btn wzb-btn-blue" style="<c:choose> <c:when test="${lang eq 'en-us'}">margin-bottom:4px;</c:when><c:otherwise>margin-top:0px;</c:otherwise></c:choose>height:30px;" class="zhiji" id="zhiji${s.index+1 }">
								
							<label  class="error" for="gid"  id="error_g${s.index+1 }" style="display:none"></label>
							</td></div>
						</tr>
                        <tr>
                        <td class="wzb-form-label" valign="top">
                            <span class="wzb-form-star">*</span>
                            <lb:get key="label_lm.label_core_learning_map_6"/>：
                        </td>
                        <td class="wzb-form-control">
                            <div class="wzb-choose-box" id="s${s.index+1 }" style="margin-top:0;">
                            <c:forEach items="${grade.items }" var="item" varStatus="t">
                            <div id="send_user_${item.psi_itm_id }" class="wzb-choose-info" value=" ${item.title }">
    	<span class="wzb-choose-detail">${item.title }</span><a class="wzb-choose-area" href="javascript:void(0)" onclick="removeDiv(${item.psi_itm_id },${s.index+1 });">  <i class="fa fa-remove"></i> </a></div>
                            </c:forEach>
                        <input type="hidden" id="q${s.index+1 }" name="qid" value="${grade.itemValue }">
                            </div>
                           <label  class="error" for="qid" id="error_q${s.index+1 }" style="display:none"></label>
                        </td>
                    </tr>
                     <tr>
                        <td></td>
                        <td class="wzb-form-control">
                            <a data-toggle="modal" data-target="#chooseUser" href="javascript:void(0);" class="xuanke wzb-box-4-add btn wzb-btn-blue" onclick="xuanze(${s.index+1 })" id="xuanke"><lb:get key='button_select' /></a> &nbsp;|&nbsp;<a class="skin-color" href="javascript:void(0);" onclick="clearAll(${s.index+1 })"><lb:get key="label_lm.label_core_learning_map_51" /></a>
                        </td>
                    </tr>
                      </tbody>
                            </table>
                        </td>
                    </tr>
                    </c:forEach>
				</c:when>
				<c:otherwise>
					   <tr id="tr_content">
							<td class="wzb-form-label"><span class="wzb-form-star">*</span><!-- 职级 -->
							<lb:get key="label_lm.label_core_learning_map_4"/>：</td>
							<div class="wzb-selector" id="n1">
							<div></div>
							<td class="wzb-form-control">		
									<input type="text" class="form-control"  placeholder="<lb:get key='label_core_learning_map_101' />"  readonly="readonly" id="name1" name="name" value="" style="width:300px;height: 30px;"><input type="button" name="" value="<lb:get key='button_select' />" class="zhiji btn wzb-btn-blue" style="<c:choose> <c:when test="${lang eq 'en-us'}">margin-bottom:4px;</c:when><c:otherwise>margin-bottom:2px;</c:otherwise></c:choose>height:30px;margin-top:0;" class="zhiji" id="zhiji1">
								<label class="error"  for="gid" id="error_g1" style="display:none"></label>
								 </td>
								 </div>  
						</tr>
                        <tr>
                        <td class="wzb-form-label" valign="top">
                            <span class="wzb-form-star">*</span>
                            <lb:get key="label_lm.label_core_learning_map_6"/>：
                        </td>
                        <td class="wzb-form-control">
                            <div class="wzb-choose-box" id="s1" style="margin-top:0;">
                            <input type="hidden" id="q1" name="qid" value="">
                            </div>
                        
                        <label  class="error" for="qid" id="error_q1" style="display:none"></label></td>
                    </tr>
                     <tr>
                        <td></td>
                        <td class="wzb-form-control">
                            <a data-toggle="modal" data-target="#chooseUser" href="javascript:void(0);" class="xuanke wzb-box-4-add btn wzb-btn-blue" onclick="xuanze(1)" id="xuanke"><lb:get key='button_select' /></a> &nbsp;|&nbsp;<a class="skin-color" href="javascript:void(0);" onclick="clearAll(1)"><lb:get key="label_lm.label_core_learning_map_51" /></a>
                        </td>
                    </tr>
				</c:otherwise>
			</c:choose>
				
                    <tr id="nodelist">
                        <td></td>
                        <td class="wzb-form-control">
                            <input type="button" class="btn wzb-btn-yellow" value="<lb:get key="label_lm.label_core_learning_map_117"/>" onclick="addHTMLNode()">   (<lb:get key="label_lm.label_core_learning_map_74"/>)
                        </td>
                    </tr>
					</tbody>
				</table>
			</div>
			<table>
				<tbody>
					<tr>
						<td class="wzb-form-label"></td>

						<td class="wzb-form-control"><span class="wzb-form-star">*</span>
							<!-- 为必填 --><lb:get key="label_rm.label_core_requirements_management_35" /></td>
					</tr>
				</tbody>
			</table>

			<div class="wzb-bar">
				<input id="createBtn" type="submit" name="frmSubmitBtn" value="<lb:get key='global.button_ok'/>"
					class="btn wzb-btn-blue wzb-btn-big margin-right15"
					onclick="javascript:void(0);"><!-- 确定 --> <input type="button"
					name="frmSubmitBtn" value="<lb:get key='global.button_cancel'/>" class="btn wzb-btn-blue wzb-btn-big"
					onclick="javascript:window.history.go(-1)"><!-- 取消 -->
			</div>

		</div>
	
		</form>		
	</div>
	<!-- wzb-panel End-->
	<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_lm_${lang}.js"></script>
    <script type="text/javascript" src="${ctx}/js/jquery.selector.js"></script>
	<script type="text/javascript" src="${ctx}/static/js/layer/layer.js"></script>
	<script type="text/javascript" src="${ctx}/js/wb_item.js"></script>
	<script type="text/javascript" src="${ctx}/js/wb_application.js"></script>
	<script type="text/javascript" src="${ctx}/js/jquery.validate.js"></script>
	<script type="text/javascript" src="${ctx}/static/admin/js/profession/add.js"></script>
		<script type="text/javascript">
		$(document).delegate('.zhiji','click', function () {
			var zid=this.id.substring(5,this.id.length);
			   $('#sid').val(zid);
			   layer.open({
	                type: 2,
	                skin:'layui-layer-lan',
	                area: ['400px', '500px'], //宽高
	                title : cwn.getLabel('label_core_learning_map_4'),
	                content:  [contextPath + '/app/admin/tree/getGradeTree','no']
	                            

	            });   
		 }); 
		   function xuanze(id){
			   $('#sid').val(id);
			   layer.open({
	                type: 2,
	                skin:'layui-layer-lan',
	                area: ['900px', '620px'], //宽高
	                title : cwn.getLabel('label_core_learning_map_6'),
	                content:  [contextPath + '/app/admin/tree/getCourseTreemultiple','no']
	                            

	            });   
		   }
		

        


</script>
<script type="text/javascript">
   function removeDiv(id,sid){
	   var si="#s"+sid;
	   var str="#send_user_";
	   str+=id;
	   var qs="#q"+sid;
	   var ids=$(qs).val();
	   var arr=ids.split("~");
	   
	   for (var i = 0; i < arr.length; i++) {
		if(arr[i]==id){
			arr.splice(i,1);
		}
	}
	   var ar=arr.join("~");
	   $(qs).val(ar);
	   var rs=$(si).find(str);
	   $(rs).remove();
	   
   }
   //增加节点
   function addHTMLNode(){
	   //点击事件都加上id条件
	  var cnt=$("input[name='name']").length;
			  
		   if($("#time").val()==0){
			   $("#time").val(1);
		   }
		var index=Number($("#time").val())+1;
		
	  if(cnt<10){
		  var dataSrouce = {
				  id: index
		  };  
		$("#time").val(index);
	   var html=$('#node-template').render(dataSrouce);
	      $("#nodelist").before(html);}else{
	    	  Dialog.alert(cwn.getLabel('label_core_learning_map_74'));
	      }
   }
	   //删除节点
   function deleteHTMLNode(id){
		   var el=$("#"+id);
		   $(el).remove();     
   }
</script>

<script id="node-template" type="text/x-jsrender">
   <tr  class="wzb-form-jiedian"  id="node{{>id}}">
                        <td colspan="2">
                            <table>
                                <tbody>
	              <tr>
                                        <td style="padding-top:28px;"></td>
                                        <td style="padding-top:28px;"><i class="fa fa-times-circle wzb-form-close" onclick="deleteHTMLNode('node{{>id}}')"></i></td>
                                    </tr>
						<tr id="tr_content">
							<td class="wzb-form-label"><span class="wzb-form-star">*</span>
							<lb:get key="label_lm.label_core_learning_map_4"/>：</td>
<div class="wzb-selector" id="n{{>id}}">
  <div></div>
							<td class="wzb-form-control">		
							
									<input type="text" class="form-control"  placeholder="<lb:get key='label_core_learning_map_101' />"  readonly="readonly" id="name{{>id}}" name="name" value="" style="width:300px;"><input type="button" name="" value="<lb:get key='button_select' />" class="zhiji btn wzb-btn-blue" style="margin-bottom:2px;margin-top:0;" class="zhiji" id="zhiji{{>id}}">
                             <label for="gid" class="error" id="error_g{{>id}}" style="display:none"></label></td>
						</div></tr>
                        <tr>
                        <td class="wzb-form-label" valign="top">
                            <span class="wzb-form-star">*</span>
                           <lb:get key="label_lm.label_core_learning_map_6" />：
                        </td>
                        <td class="wzb-form-control">
                            <div class="wzb-choose-box" id="s{{>id}}" style="margin-top:0;">
                            </div>
                        <label for="qid"  class="error" id="error_q{{>id}}" style="display:none"></label></td>
                    </tr>
                     <tr>
                        <td></td>
                        <td class="wzb-form-control">
                            <a data-toggle="modal" data-target="#chooseUser" href="#" class="xuanke wzb-box-4-add btn wzb-btn-blue" onclick="xuanze({{>id}})" id="xuanke"><lb:get key='button_select' /></a> &nbsp;|&nbsp;<a class="skin-color" href="javascript:void(0);" onclick="clearAll({{>id}})"><lb:get key="label_lm.label_core_learning_map_51" /></a>
                        </td>
                    </tr>
                    </tbody>
                            </table>
                        </td>
                    </tr>
</script>



	<script id="option-template" type="text/x-jsrender">
		<p>
			<lb:get key="label_rm.label_core_requirements_management_40"/> ：<input type="text" class="form-control" style="width: 280px;" name="options"><input type="button" class="btn wzb-btn-blue" onclick="delOption(this);" value="<lb:get key='global.button_del'/>" style="margin-bottom:2px;">
		</p>
	</script>

</body>
</html>