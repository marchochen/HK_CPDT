<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.cwn.wizbank.entity.*,java.util.*,com.cw.wizbank.util.*, com.cw.wizbank.qdb.*"%>
<%@ include file="../../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="../../common/meta.tree.jsp"%>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/global_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_ss_${lang}.js"></script>

<script type="text/javascript" src="${ctx}/js/jquery.validate.js"></script>
<style type="text/css">
.related{
    color: #00AEEF;
}
</style>
<script type="text/javascript">
	$(function(){
		var role_id = 0;
		if("${acRole.rol_id}" != ""){
			role_id = "${acRole.rol_id}"; 
		}
		
		$("#roleForm").validate({
			rules:{
				rol_title:{
	                required:true
	            }
	        }
	        ,
	        messages:{
	        	rol_title:{
	                required:fetchLabel("label_core_system_setting_21")
	            }
	        }
		});
		
		$.ajax({
			url : '${ctx}/app/admin/home/roleFavoriteFunctionJson?role_id=' + role_id,
			dataType : 'JSON',
			type : 'post',
			success : function(result){
				var data = result.acFunctionList;
				var arr = [];
				var obj = {};
				for(var i = 0; i < data.length; i++){//筛选删除"常用功能"
					if(data[i].ftn_ext_id != 'FTN_AMD_COMMON_USERD_MGT'){
						obj = data[i];
						arr.push(obj);
					}
				}
				$.each(arr,function(i,val){
					var label = cwn.getLabel(val.ftn_ext_id);
					val.ftn_name = label;
					$.each(val.subFunctions,function(j,sval){
						var lab = cwn.getLabel(sval.ftn_ext_id);
						sval.ftn_name = lab;
						 $.each(sval.subFunctions,function(k,value){
							var fun_lab = cwn.getLabel(value.ftn_ext_id);
							value.ftn_name = fun_lab;
						})  
					})
				})
				$("#MenusMarkFavorite").html($("#favorite_parent").render(arr));
				
				//一级功能选中
				$(".wzb-form-checkbox dt input[type='checkbox']").click(function(){
					var dt = $(this).parents('dt');
					if($(this).prop("checked")){
						$(dt).siblings().find("input[type='checkbox']").prop("checked",true);
					}else{
						$(dt).siblings().find("input[type='checkbox']").prop("checked",false);
					}
				})
				
				//二级功能选中处理
				$(".wzb-form-checkbox dd input[type='checkbox']").click(function(){
					var dt = $(this).parents().siblings("dt");
					var isChecked = $(this).prop("checked");
					if(isChecked){
						 $(dt).find("input[type='checkbox']").prop("checked",true);
						 var dd = $(this).parents().siblings("dd3");
						 $(dd).find("input[type='checkbox']").prop("checked",true);
						 
					}else{
						$(this).parents().siblings("dd3").find("input[type='checkbox']").prop("checked",false);
					}
				})
				
				//三级功能选中处理
				$(".wzb-form-checkbox dd3 input[type='checkbox']").click(function(){
					var dt = $(this).parents().siblings("dt");
					var dd = $(this).parents("dd3").siblings("label");
					var isChecked = $(this).prop("checked");
					if(isChecked){
						 $(dt).find("input[type='checkbox']").prop("checked",true);
						 $(dd).find("input[type='checkbox']").prop("checked",true);
					}else{
						var siblings = $(this).parents(".checkbox").siblings();
						var length = $(siblings).length;
						var checked = false;
						if(length > 0){
							for(var i = 0;i<length;i++){
								if($(siblings[i]).find("input[type='checkbox']").prop("checked")){
									checked = true;
								}
							}
						}
						if(!checked){
							$(dt).find("input[type='checkbox']").prop("checked",false);
						}
						
						var siblings_top = $(this).parents.parents(".checkbox").siblings();
						var length_top = $(siblings_top).length;
						var checked_top = false;
						alert
						
						if(length_top > 0){
							for(var i = 0;i<length_top;i++){
								if($(siblings_top[i]).find("input[type='checkbox']").prop("checked")){
									checked_top = true;
								}
							}
						}
						if(!checked_top){
							$(dt).find("input[type='checkbox']").prop("checked",false);
						}
					}
				})
				
				
				//角色修改
				if(result.functions){
					var acFunction = result.functions;
					$.each(acFunction,function(i,val){
						var ftn_id = val.ftn_id;
						var ftn_id_check;
						if(ftn_id > 0){
							ftn_id_check = $("input[name='acFunction.ftn_id'][value='"+ftn_id+"']");
							$(ftn_id_check).prop("checked","checked");
							if($(ftn_id_check).prop("checked",true)){
								$(ftn_id_check).parents().siblings("dt").find("input[name='acFunction.ftn_id']").prop("checked",true);
							}
						}
					});
				}
			}
		});
	})
	
	
	function save(){
		var rol_title =$('#rol_title').val();
		if(getChars(rol_title) > 20 && $('#is_sys').val() !='SYSTEM')
		{
           Dialog.alert(label_ss.label_core_system_setting_144);	
           $('#rol_title').focus();
           return;
		}
		
		var str = '';
		var checkbox_lst = $("#MenusMarkFavorite dl input[type='checkbox']");
		for(var i = 0;i<checkbox_lst.length;i++){
			if($(checkbox_lst[i]).prop("checked")){
				str = str + $(checkbox_lst[i]).val() + '~';
			}
		}
		
		var flag = $("#roleForm").valid();
		
		if (str != "") {
			str = str.substring(0, str.length-1);
		}else{
			flag = false;
			$("#error_fun").text(fetchLabel("label_core_system_setting_22"));
			$("#error_fun").show();
		}
		
		if(!flag){
			window.location.href = "#role_top";
			if(!$("#rol_title").val()){
				$("#rol_title").focus();
			}
			return;
		}
		
		$("#ftn_id_lst").val(str);
		$("form").submit();
	}
	
	

	
	
</script>
</head>
<body>
  	
  	
  	<c:if test="${exist=='true' }">
  		<script type="text/javascript">
  		//alert("名称不能重复");
  		/* function error_title(){
  			 $("#error_title").text(fetchLabel("label_core_system_setting_128"));
  			 $("#error_title").show();
  		} */
  		</script>
  	</c:if>
  
    <title:get function="global.FTN_AMD_SYSTEM_SETTING_MGT"/>

    <ol class="breadcrumb wzb-breadcrumb">
		  <li><a href="javascript:wb_utils_gen_home(true);"><i class="fa wzb-breadcrumb-home fa-home"></i><lb:get key="global.lab_menu_started" />
		</a></li>
		  <li><a href="${ctx}/app/admin/role/list"><lb:get key="label_ss.label_core_system_setting_1"/></a></li>
		  
		    		<c:choose>
		  			<c:when test="${type=='add'}">
		  				  <li class="active"><lb:get key =  "label_ss.label_core_system_setting_11"/>   </li>
		  			</c:when>
		  			<c:when test="${type=='update'}">
		  				  <li class="active">${acRole.rol_title}   </li>
		  			</c:when>
		  		</c:choose>
		
		
		  	<%--
		  		<lb:get key="role_${type}_manage"/>
		  	 --%>
		  
		
	</ol> <!-- wzb-breadcrumb End-->
		
	<div class="panel wzb-panel">
		<div class="panel-heading">
			<c:choose>
		  			<c:when test="${type=='add'}">
		  				<lb:get key =  "label_ss.label_core_system_setting_11"/>
		  			</c:when>
		  			<c:when test="${type=='update'}">
		  				${acRole.rol_title}
		  			</c:when>
		  		</c:choose>
		</div>
		
		<div id="role_top"></div>
		
		<div class="panel-body">
			<form:form  modelAttribute="acRole" action="${ctx}/app/admin/role/save?type=${type}" method="post" cssClass="form-horizontal" id="roleForm">
				<form:input type="hidden" cssStyle="width: 300px;" cssClass="form-control" path="rol_id" />
				<input type="hidden" name="ftn_id_lst" id="ftn_id_lst" value=""/>
				<table>
				    <tr>
				         <td class="wzb-form-label"><span class="wzb-form-star">*</span><lb:get key="label_ss.label_core_system_setting_20"/>：</td>
				         
				         <td class="wzb-form-control">
				              <div class="wzb-selector">
				                   <input type="text" class="form-control" ${acRole.rol_type eq 'SYSTEM'?'readonly=\'readonly\'':''} name="rol_title" id="rol_title" value="${acRole.rol_title}"> 
				                   <!-- <label id="error_title" class="error" style="display:none"> -->
				                   <form:errors path="rol_title" cssClass="control-label has-error error" />
				              </div>
				              <input type="hidden" name="is_sys" id = "is_sys" value="${acRole.rol_type}"/>
				         </td>
				    </tr>
				    
				   <!-- 释放when判断，释放系统管理员显示是否与培训中心关联的单选项 (sys角色只显示，不修改)-->
				    <!--	 test="${not (type eq 'update' and acRole.rol_ext_id eq 'ADM_1')}" --><!-- 管理员不可选择培训中心相关 -->
					    	<tr>
						         <td class="wzb-form-label" valign="top"><span class="wzb-form-star">*</span><lb:get key="label_ss.label_core_system_setting_12"/>：</td>
						         
						         <td class="wzb-form-control">
						             <p><label for="psde" class="wzb-input-label"><input type="radio" name="rol_tc_ind" value="1" checked="checked" id="psde"><lb:get key="label_ss.label_core_system_setting_13"/></label></p>
						             <p><label for="psdf" class="wzb-input-label"><input type="radio" name="rol_tc_ind" value="0" id="psdf"><lb:get key="label_ss.label_core_system_setting_14"/></label></p>
						         	 <form:errors path="rol_tc_ind" cssClass="control-label has-error error" />
						         	<script>
						         		var rol_tc_ind = "${acRole.rol_tc_ind}";
						         		if(rol_tc_ind && rol_tc_ind!=null){
						         			$("input[name='rol_tc_ind'][value='"+rol_tc_ind+"']").attr("checked","checked");
						         			if(${acRole.rol_type eq 'SYSTEM'}){
						         				$("input[name='rol_tc_ind'][value !='"+rol_tc_ind+"']").attr("disabled","disabled");
						         			}
						         		}
						         	</script>
						         </td>
						    </tr>
				    
				    	
				    		<!-- <input type="hidden" name="rol_tc_ind" value="0"/> --><!-- 管理员不可选择培训中心相关，默认为【无关联】 -->
				    	
				   
				    
				    <tr>
				         <td class="wzb-form-label" valign="top"><span class="wzb-form-star">*</span><lb:get key="label_ss.label_core_system_setting_15"/>：<!-- 未选择权限功能所提示的错误信息 --><label id="error_fun" class="error" style="display:none"></label><form:errors path="acFunction.ftn_id" cssClass="control-label has-error error" /></td>
				         
				         <td class="wzb-form-control" id="MenusMarkFavorite">
				             
				         </td>
				    </tr>
				    
				    <tr>
				    	<td class="wzb-form-label"></td>
				    	<td class="wzb-form-control">
				    		 <p class="color-gray666"><span class="wzb-form-star">*</span><lb:get key="label_ss.label_core_system_setting_16"/><br/>
				                	 <!--
				                	 <lb:get key="label_ss.label_core_system_setting_17"/></p>	
				                	 -->
				    	</td>
				    </tr>
				</table>
				
				<div class="wzb-bar">
				     <input type="button" onclick="javascrip:save();" class="btn wzb-btn-blue wzb-btn-big margin-right15" value="<lb:get key='label_ss.label_core_system_setting_18'/>" name="frmSubmitBtn">
				     <input type="button" onclick="javascript:go('${ctx}/app/admin/role/list');" class="btn wzb-btn-blue wzb-btn-big" value="<lb:get key='label_ss.label_core_system_setting_19'/>" name="frmSubmitBtn">
				</div>
			</form:form>
		</div>
    </div>
    <!-- wzb-panel End-->
    <script type="text/x-jquery-tmpl" id="favorite_parent">
		<dl class="wzb-form-checkbox">
			<dt><div class="checkbox"><label>
				<input type="checkbox" name="acFunction.ftn_id" value="{{:ftn_id }}">

				{{:ftn_name}} 
			</label></div></dt>
			<dd>
				{{for subFunctions}}
					{{include tmpl="#favorite_child" /}}
				{{/for}}
			</dd>
		</dl>
	</script>
	<script type="text/x-jquery-tmpl" id="favorite_child">
  		<div class="checkbox">
		
		<label  {{if ftn_tc_related && ftn_tc_related == 'Y'}}class="related"{{/if}}>
			<input type="checkbox" name="acFunction.ftn_id"  value="{{>ftn_id }}" >{{>ftn_name}}
		</label>
           <dd3 class="123456">
				{{for subFunctions}}
					{{include tmpl="#favorite_child_l3" /}}
				{{/for}}
			</dd3>
        </div>
	</script>
	
	<script type="text/x-jquery-tmpl" id="favorite_child_l3">
  		<div class="checkbox">
		<label style="margin-left: 20px"  {{if ftn_tc_related && ftn_tc_related == 'Y'}}class="related"{{/if}} >
			<input type="checkbox" name="acFunction.ftn_id"  value="{{>ftn_id }}">{{>ftn_name}}
		</label>
        </div>
	</script>
	<!--  
	<script type="text/x-jquery-tmpl" id="favorite_fun_child">
  		<div class="checkbox"><label>
			<input type="checkbox" name="fun" {{if uff_fun_id> 0 }}checked=checked{{/if}} value="{{>ftn_id }}">{{>ftn_name}}
		</label></div>
	</script>-->
</body>

</html>