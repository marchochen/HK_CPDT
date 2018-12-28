
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-admin.css" />
	<script type="text/javascript" src="${ctx}/static/js/layer/layer.js"></script> 
	<script type="text/javascript" src="${ctx}/js/wb_defaultImage.js"></script> 
		<link rel="stylesheet" href="${ctx}/static/css/thickbox.css">
			<script src="${ctx}/static/js/thickbox-compressed.js" type="text/javascript"></script>
			<script type="text/javascript" src="${ctx}/static/js/jsrender.js"></script>
			<script type="text/javascript">
		
			$(function(){
				initDefaultImage('grade', 'grade', false, useDefaultImage);
			})
			var ids="";
			function initcourse(id,index,cnt){
				ids += id + '~'; 
				var num=parseInt(index)+1;
				if(num==cnt){
				ids=ids.substring(0,ids.length-1);
				$('#qid').val(ids);
				}
				
			}
			
			$(window).load(function(){
			   var type="${type}";
			   if(type == 'doadd')	{
				   var img_obj = document.getElementById("extension_43_id");
			       if(typeof(img_obj) == "undefined")
			       {
			    	   initDefaultImage('grade', 'grade', true, useDefaultImage);
			       }
			   }
			})
			
			</script>
</head>
<body>
<input type="hidden" name="belong_module" value="FTN_AMD_STUDY_MAP_MGT"/>

 	 <title:get function="global.FTN_AMD_STUDY_MAP_MGT"/>

	<ol class="breadcrumb wzb-breadcrumb">
		<li><a href="javascript:wb_utils_gen_home(true);">  <!-- 首页 -->
		    <i class="fa wzb-breadcrumb-home fa-home"></i>
			<lb:get key="label_lm.label_core_learning_map_1" /></a>
		</li>
		<li><a href="javascript:javascript:window.history.go(-1);">  <!-- 关键岗位学习地图管理 -->
			<lb:get key="label_lm.label_core_learning_map_104" /></a>
		</li>
		<li class="active">
         <c:choose>
          <c:when test="${type =='update'}"> 
           <lb:get key="label_lm.label_core_learning_map_131" /><!-- 修改-->    
          </c:when>
         <c:otherwise>
           <lb:get key="label_lm.label_core_learning_map_130" /><!-- 添加-->
         </c:otherwise>
        </c:choose>
		</li>
	</ol>
	<!-- wzb-breadcrumb End-->
	<div class="panel wzb-panel">
		<div class="panel-heading">
			<lb:get key="label_lm.label_core_learning_map_24" /><!-- 关键岗位地图管理 -->
		</div>
		<form  method="post" action="${ctx}/app/admin/positionMap/${type}" id="adminForm" enctype="multipart/form-data">
		 <div class="panel-body">
		<c:choose>
			<c:when test="${type =='update'}">
			<input type="hidden" id="upm_id" name="upm_id" value="${position.upm_id }">
			</c:when>
			<c:otherwise>
			</c:otherwise>
		</c:choose>
            <table>
                <tbody>
                    <tr>
                        <td class="wzb-form-label">
                            <span class="wzb-form-star">*</span><lb:get key="label_lm.label_core_learning_map_14" />：
                        </td>
                        <td class="wzb-form-control">
                            <table>
                                <tbody>
                                    <tr>
                                        <td width="20%">
                                            <input class="wzb-inputText" readonly="readonly" id="upt_title" name="upt_title" type="text" value="${position.upt_title }" style="width:300px">
                                            <input type="hidden" id="upt_id" name="upt_id" value="${position.upt_id }">
                                       </td>
                                        <td width="80%" align="left">
                                            <input type="button" name=" " value="<lb:get key='label_lm.label_core_learning_map_54' />"  class="btn wzb-btn-blue margin-bottom1" id="gangwei">
                                      <label class="error" id="error-title" style="display:none"></label>  </td>
                                    </tr>
                                </tbody>
                            </table>
                         </td>
                    </tr>
                    <tr>
                        <td class="wzb-form-label">
                            <span class="wzb-form-star">*</span><lb:get key="label_lm.label_core_learning_map_15" />：
                        </td>
                        <td class="wzb-form-control">
                            <input class="wzb-inputText" readonly="readonly" name="upt_code" value="${position.upt_code}" type="text" style="width:300px">
                        </td>
                    </tr>
                    <tr>
                        <td class="wzb-form-label">
                            <lb:get key="label_lm.label_core_learning_map_16" />：
                        </td>
                        <td class="wzb-form-control">
                            <input class="wzb-inputText" readonly="readonly" name="upc_title" value="${position.upc_title}" type="text" style="width:300px">
                            <input type="hidden" id="upc_id" name="upc_id" value="${position.upc_id }">
                        </td>
                    </tr>
                    <tr>
                        <td class="wzb-form-label">
                            <span class="wzb-form-star">*</span><lb:get key="label_lm.label_core_learning_map_17" />：
                        </td>
                        <td class="wzb-form-control">
                            <input class="wzb-inputText" id="upm_seq_no" name="upm_seq_no" value="${position.upm_seq_no}" type="text" style="width:300px">
                        <label class="error" id="error-seq" style="display:none"></label></td>
                    </tr>
                    <tr>
                        <td class="wzb-form-label" style="padding:0"></td>
                        <td class="wzb-form-control" style="padding:0 0 0 10px;">
                          <lb:get key="label_lm.label_core_learning_map_55" />
                        </td>
                    </tr>
                    <tr>
                        <td class="wzb-form-label" valign="top">
                            <span class="wzb-form-star">*</span>  <lb:get key="label_lm.label_core_learning_map_18" />：
                        </td>
                        <td class="wzb-form-control">
                            <div class="wzb-choose-box" style="margin-top:0;">
                            <input type="hidden" id="qid" name="qid" value="">
                                <c:forEach var="item" varStatus="s" items="${position.items }">
                                  <script> initcourse( '${item.upi_itm_id }','${s.index}','${itemSize }') ; </script>
                               
                              <div id="send_user_${item.upi_itm_id }" class="wzb-choose-info" value=" ${item.title }">
    	<span class="wzb-choose-detail">${item.title }</span><a class="wzb-choose-area" href="javascript:void(0)" onclick="removeDiv(${item.upi_itm_id })">  <i class="fa fa-remove"></i> </a></div>
                                </c:forEach>
                                
                            </div>
                            </td> 
                    </tr>
                    <tr>
                        <td class="wzb-form-label"></td>
                        <td class="wzb-form-control">
                            <a data-toggle="modal" data-target="#chooseUser" href="#" class="wzb-box-4-add btn wzb-btn-blue" id="xuanke">  <lb:get key="label_lm.label_core_learning_map_50" /></a> &nbsp;|&nbsp;<a class="skin-color" href="javascript:void(0);" onclick="clearAll();">  <lb:get key="label_lm.label_core_learning_map_51" /></a>
                       <label class="error" id="error-course" style="display:none"></label>
                        </td>
                    </tr>
                    
                    
                    <tr>
                        <td class="wzb-form-label" valign="top">  <lb:get key="label_lm.label_core_learning_map_19" />：</td>
                        <td class="wzb-form-control">
                            <table border="0" cellspacing="0" cellpadding="3">
                                <tbody>
                                    <tr>
                                        <td width="300px" rowspan="4">
                                            <div>
                                              <c:if test="${type=='doadd' }">
                                            
                                            <img height="100px" width="256px" border="0" id="extension_43_id" name="grade_preview" > <%-- src="${ctx}/grade/grade.jpg" --%>
                                              <input value="${ctx}/grade/grade.jpg" type="hidden" id="curimg">
                                                                                       <input value="${ctx}/grade/grade.jpg" type="hidden" id="imgUrl" name="imgurl">
                                            </c:if>
                                            <c:if test="${type=='update' }">
                                            
                                            <img height="100px" width="256px" border="0" id="extension_43_id" name="grade_preview" src="${ctx}${position.abs_img}">
                                         <input value="${ctx}${position.abs_img}" type="hidden" id="curimg">
                                            
                                         <input value="${ctx}${position.abs_img}" type="hidden" id="imgUrl" name="imgurl">
                                            </c:if>
                                            </div>
                                        </td>
                                        <c:if test="${type=='update' }">
	                                        <td>
	                                            <label>
	                                                <input name="image_radio" onclick="extension_43_change(this);" value="0" checked type="radio" id="remain_image" role="auto"><span class="wbFormRightText">  <lb:get key="label_lm.label_core_learning_map_44" /></span>
	                                                <input value="true" type="hidden" name="remain_photo_ind">
	                                            </label>
	                                        </td>
                                        </c:if>
                                    </tr>
                                    <tr>
                                        <td style="padding: 3px;">
                                            <label>
                                                <input value="1" onclick="useDefaultImage();" <c:if test="${type=='doadd' }"> checked </c:if>  name="image_radio" id="default_image" type="radio"   style=" "> 
                                                <input value="<lb:get key='label_lm.label_core_learning_map_56' />" class="wzb-btn-blue" type="button" name="" onclick="document.getElementById('default_image').checked=true;useDefaultImage();show_default_image() " style="border: 1px solid transparent;padding:3px 8px;" >
                                          		<a style="display: none;" class="thickbox" href="#TB_inline?height=380&amp;width=580&amp;inlineId=myOnPageContent" id="default_btn"></a>
                                            	<input name="default_image" type="hidden" value="${ctx}/grade.jpg">
                                            </label>
											<div id="myOnPageContent" style="display: none;">
												<div class="thickbox-tit">
													<lb:get key="label_km.label_core_knowledge_management_37" />
												</div>
							
												<div class="thickbox-cont thickbox-user clearfix  thickbox-content-2" id="defaultImages"></div>
						                                            
							                      <div class="norm-border thickbox-footer">
							                           <input value="<lb:get key='global.button_ok'/>" onclick="selectImage()" name="pertxt" class="margin-right10    btn wzb-btn-blue wzb-btn-big" type="button">
							                           <input value="<lb:get key='global.button_cancel'/>" name="pertxt" class=" TB_closeWindowButton  btn wzb-btn-blue wzb-btn-big " type="button">
							                      </div>
                                              </div>
                                       	</td>
                                    </tr>
                                    <tr>
                                        <td style="padding: 3px;">
                                          <label for="field99__select2">
                                           <input onclick="extension_43_change(this)" name="image_radio" value="2" id="local_image" type="radio"><span class="wbFormRightText"><lb:get key="label_lm.label_core_learning_map_48" /></span>&nbsp;
                                              <div style="margin:2px 0 0 0; height:0;" class="file">
                                                 <input onchange="$(this).siblings('.file_txt').html(this.value);$(this).siblings('.file_txt').val(this.value);" type="file" name="image" class="file_file" onfocus="" onblur="" onclick="document.getElementById('local_image').checked=true;" title="" value="" id="file_photo_url"><input value="<lb:get key='no_select_file'/>" class="file_txt">
                                                <div class="file_button-blue"><lb:get key="usr_browse" /></div>
                                              </div>
                                              <br>
                                              <span class="text">&nbsp;&nbsp;<lb:get key="label_lm.label_core_learning_map_76" /><br></span>
                                          </label>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </td>
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
    <script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_lm_${lang}.js"></script>
    	<script type="text/javascript" src="${ctx}/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${ctx}/static/admin/js/positionMap/add.js"></script>
    <!-- 内容添加结束 -->
<script type="text/javascript">
   function removeDiv(id){
	   var ids=$('#qid').val();
	   var str="#send_user_";
	   str+=id;
	   
	   var arr=ids.split("~");
	   for (var i = 0; i < arr.length; i++) {
		if(arr[i]==id){
			arr.splice(i,1);
		}
	}
	   var ar=arr.join("~");
	   $("#qid").val(ar);
	   var rs=$('.wzb-choose-box').find(str);
	   $(rs).remove();
   }
</script>


<script type="text/javascript">
    $("#gangwei").click(function(){
        layer.open({
               type: 2,
               area: ['600px', '550px'], //宽高
               title : cwn.getLabel('label_core_learning_map_54'),
               content: [contextPath +'/app/admin/positionMap/catalog','no']
           });
    })
          
          $("#xuanke").click(function(){

        	  layer.open({
	                type: 2,
	                skin:'layui-layer-lan',
	                area: ['900px', '620px'], //宽高
	                title : cwn.getLabel('label_core_learning_map_18'),
	                content:  [contextPath + '/app/admin/tree/getCourseTreesingle','no']
	            });   

          })
          	function clearFileInput(file_obj){
				var file_obj2= file_obj.cloneNode(false);
				file_obj2.onchange= file_obj.onchange; 
				//file_obj2.disabled = true;
				file_obj.parentNode.replaceChild(file_obj2,file_obj);
			}

			function extension_43_change(obj) {
				//$("#default_btn").prev().removeAttr("disabled");
				var img_obj = document.getElementById("extension_43_id");
				var file_obj = document.getElementById("file_photo_url");
				if(obj.id === "remain_image") {
					clearFileInput(file_obj);
					//$("#default_btn").prev().attr("disabled","disabled");
					img_obj.src = $('#curimg').val();
					$("input[name=remain_photo_ind]").val("true");
				} else if(obj.id === "default_image") {
					clearFileInput(file_obj);
					$("input[name=remain_photo_ind]").val("false");
				} else {
					file_obj.disabled = false;
					//$("#default_btn").prev().attr("disabled","disabled");
					$("input[name=remain_photo_ind]").val("false");
					if(document.all.image.files && document.all.image.files[0]){
						//img_obj.src = window.URL.createObjectURL(document.all.image.files[0]);
					}
				}
			}
			
			function previewLocalImage(obj) {
				var img_obj = document.getElementById("extension_43_id");
				img_obj.src = window.URL.createObjectURL(obj.files[0]);
			}
			
          
</script>





</body>
</html> 