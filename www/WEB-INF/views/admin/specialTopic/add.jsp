<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
	<head>
		<%@ include file="../../common/meta.kindeditor.jsp"%>
		<link rel="stylesheet" href="${ctx}/js/tree/css/ztree.css" />
		<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />
		<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-admin.css" />
		<link rel="stylesheet" type="text/css" href="${ctx}/js/kindeditor/themes/default/default.css"/>
		<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-admin.css" />
		<link rel="stylesheet" href="${ctx}/static/css/thickbox.css">
		<script type="text/javascript" src="${ctx}/static/js/layer/layer.js"></script> 
		<script type="text/javascript" src="${ctx}/js/wb_defaultImage.js"></script> 
		<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
		<script src="${ctx}/static/js/thickbox-compressed.js" type="text/javascript"></script>
		<script type="text/javascript" src="${ctx}/static/js/jsrender.js"></script>
		
		<script type="text/javascript">
			$(function(){
				initDefaultImage('specialtopic', 'topic', false, function(){
					if('update' !='${type}'){
						//使用默认图片
						useDefaultImage();
					}
				});
				if('update'=='${type}'){
					$('#q1').val('${itemStr}');
					$('#extert_ids').val('${extertStr}');
				}
			});
		</script>
		
		<script type="text/javascript">
			$(function() {
				//专题讲师
				$("#addInstructorModal").on('show.bs.modal', function (e) {
					addUser();
				});
			});

			function addUser(searchContent){
				$(".modal-dialog #wzb-pop-1-main").html('');
		        $(".modal-dialog").find("#wzb-pop-1-main").empty().table({
		        	url : contextPath + '/app/instr/getInstructors',
		        	params : {
						searchContent : searchContent
					},
		    		gridTemplate : function(data){
		 						if(data.iti_level== 'J'){
		 							data.iti_level = fetchLabel("label_core_basic_data_management_5");
		 						}else if(data.iti_level == 'M'){
		 							data.iti_level= fetchLabel("label_core_basic_data_management_6");
		 						}else if(data.iti_level == 'S'){
		 							data.iti_level= fetchLabel("label_core_basic_data_management_7");
		 						}else if(data.iti_level == 'D'){
		 							data.iti_level = fetchLabel("label_core_basic_data_management_8");
		 						}
		    			p = {
		    				image : contextPath + data.iti_img,
		    				usr_display_bil  : data.iti_name,
		    				upt_title  : data.upt_title,
		    				href : contextPath + '/app/personal/' + data.iti_ent_id,
		    				usr_ent_id : data.iti_ent_id,
		    				iti_type_mark : data.iti_type_mark,
		    				add : true,
		    				iti_level : data.iti_level,
		    				iti_score : data.iti_score,
		    				isNormal : true
		    			}
		    			return $('#instructorTemplate').render(p);
		    		},
		    		view : 'grid',
		    		rowSize : 4,
		    		rp : 12,
		    		showpager : 5,
		    		usepager : true,
		    		hideHeader : true,
		    		trLine : false, 
		            onSuccess : function(data){
		            	
		            	var userPlusNum = $(".wzb-user-plus").length;
		            	var chooseUserNum = $("experts .wzb-choose-info").length;
		            	if(userPlusNum == 0 && chooseUserNum == 0){
		            		$("#addAll").hide();
		            	}else{
		            		$("#addAll").show();
		            	}
		            	
		                //给添加按钮事件
		            	//默认选中的给予标记选中
		             	 $("#experts .wzb-choose-info").each(function(){
		             		var usr_ent_id = $(this).attr("value");
		             		var usr_display_bil =  $(this).find(".wzb-choose-detail").text();
		             		p = {
		             			usr_ent_id : usr_ent_id,
		             			usr_display_bil : usr_display_bil,
		             		}
		             		$("#user_" + usr_ent_id).html($("#cancel-user-button").render(p));
		                });                
		            }
	    		});
			}
	
			//添加选中的讲师
			function addSendUser(usr_ent_id,usr_display_bil){
				var p = {
						usr_ent_id : usr_ent_id,
						usr_display_bil : usr_display_bil
				}
				$("#user_" + usr_ent_id).html($("#cancel-user-button").render(p));
				$("#experts").html($("#experts").html() + $("#detele-user-button").render(p));
			}
			
			//取消已选中的讲师
			function cancelUser(usr_ent_id, usr_display_bil){
				var p = {
					usr_ent_id : usr_ent_id,
					usr_display_bil : usr_display_bil
				}
				$("#user_" + usr_ent_id).html($("#add-user-button").render(p));
				$("#send_user_" + usr_ent_id).remove();
			}
	
			//选择所有用户
			function addAll(){
				$("a[name='addUser']").click();
			}
			
			//搜索用户
			function searchUserList(){
				searchContent = $("input[name='search_add_member']").val();
				if(fetchLabel('attention_find_desc') == searchContent){
					searchContent =  "";
				}
				addUser(searchContent);
			}
	
			$(window).load(function(){
			    var type="${type}";
			    if(type == 'doadd') {
			       var img_obj = document.getElementById("extension_43_id");
			       if(typeof(img_obj) == "undefined"){
			    	   initDefaultImage('specialtopic', 'topic', true);
			       }
			       useDefaultImage();
			    }
			});
		</script>
		
	</head>
	<body>
		<input type="hidden" name="belong_module" value="FTN_AMD_TRAINING_MGT"/>
 	 	<title:get function="global.FTN_AMD_TRAINING_MGT"/>
		<!-- 导航栏 -->
		<ol class="breadcrumb wzb-breadcrumb">
			<li>
				<a href="javascript:wb_utils_gen_home(true);">
					<i class="fa wzb-breadcrumb-home fa-home"></i>
					<lb:get key="label_lm.label_core_learning_map_1" />
				</a>
			</li>
			<li>
				<a href="${ctx }/app/admin/specialTopic">
					<lb:get key="label_lm.label_core_learning_map_31" /><!-- 专题培训管理-->
				</a>
			</li>
			<li class="active">
				<c:choose>
					<c:when test="${type =='update'}">
						<lb:get key="label_lm.label_core_learning_map_80" />
					</c:when>
					<c:otherwise>
						<lb:get key="label_lm.label_core_learning_map_30" />
					</c:otherwise>
				</c:choose>
			</li>
		</ol>
		
		<!-- wzb-breadcrumb End-->
		<div class="panel wzb-panel">
			<div class="panel-heading">
				<lb:get key="label_lm.label_core_learning_map_31" /><!-- 专题培训管理 -->
		</div>
			<form  method="post" action="${ctx}/app/admin/specialTopic/${type}" id="adminForm" enctype="multipart/form-data" onkeydown="if(event.keyCode==13)return false;" onSubmit="return checkSubmit();">
				<c:choose>
					<c:when test="${type =='update'}">
						<input type="hidden" id="ust_id" name="ust_id" value="${specialTopic.ust_id }">
					</c:when>
					<c:otherwise></c:otherwise>
				</c:choose>
				
				<div class="panel-body">
		        	<div class="wzb-item-main">
		        	
			            <ul role="tablist" class="nav nav-tabs page-tabs">
			                <li class="active" role="presentation">
			                    <a href="#basic" data-toggle="tab" role="tab" aria-controls="basic"><lb:get key="label_lm.label_core_learning_map_42" /></a>
			                </li>
			                <li role="presentation" class="">
			                    <a href="#senior" data-toggle="tab" role="tab" aria-controls="senior"><lb:get key="label_lm.label_core_learning_map_43" /></a>
			                </li>
			            </ul>
			            
		            	<input type="hidden" id="sid" value="1">
		            	
			            <!--tab-content start-->
			            <div class="tab-content">
		                	<div id="basic" class="tab-pane active" role="tabpanel">
			                    <table>
			                        <tbody>
			                            <tr>
			                                <td valign="top" class="wzb-form-label"><span class="wzb-form-star">*</span><lb:get key="label_lm.label_core_learning_map_22" />：</td>
			                                <td class="wzb-form-control">
			                                    <input class="wzb-inputText" id="ust_title" name="ust_title" style="width:400px" type="text" onblur="this.value=this.value.trim();" value="${specialTopic.ust_title }">
			                                    <input type="hidden" class="form-control" id="old_title"   value="${specialTopic.ust_title }"/>
			                              		<label for="ust_title" class="error" id="error-title" style="display:none"></label> 
			                              	</td>  
			                            </tr>
		                              	<tr>
					                        <td valign="top" class="wzb-form-label"><span class="wzb-form-star">*</span><lb:get key="label_lm.label_core_learning_map_19" />：</td>
					                        <td class="wzb-form-control">
					                            <table border="0" cellspacing="0" cellpadding="3">
					                                <tbody>
					                                
					                                    <tr>
					                                        <td width="280px" rowspan="4">
					                                            <div>
					                                            	<c:if test="${type=='doadd' }">
					                                            		<img height="100px" width="251px" border="0" id="extension_43_id" name="topic_preview" src="">  <%-- ${ctx}/specialtopic/topic.jpg --%>
					                                              		<input value="${ctx}/grade/grade.jpg" type="hidden" id="curimg">
					                                               		<input value="${ctx}/specialtopic/topic.jpg" type="hidden" id="imgUrl" name="imgurl">
					                                            	</c:if>
					                                            	<c:if test="${type=='update' }">
					                                            		<img height="100px" width="251px" border="0" id="extension_43_id" name="topic_preview" src="${ctx}${specialTopic.abs_img }">
					                                         			<input value="${ctx}${specialTopic.abs_img }" type="hidden" id="curimg">
					                                         			<input value="${ctx}${specialTopic.abs_img }" type="hidden" id="imgUrl" name="imgurl">
					                                            	</c:if>
					                                            </div>
					                                        </td>
					                                        
					                                        <c:if test="${type=='update' }">  <!-- 新增时  不显示 “保存当前头像” -->
						                                        <td>
						                                            <label>
						                                                <input name="image_radio"   onclick="extension_43_change(this);" value="0" checked type="radio" id="remain_image" role="auto">
						                                                <span class="wbFormRightText"><lb:get key="label_lm.label_core_learning_map_44" /></span>
						                                          		<input value="true" type="hidden" name="remain_photo_ind">
						                                            </label>
						                                        </td>
					                                        </c:if>
					                                    </tr>
					                                    
		                                    			<tr>
					                                        <td style="padding: 3px;">
					                                            <label>
					                                                <input value="1" onclick="useDefaultImage();" <c:if test="${type=='doadd' }">checked</c:if>  name="image_radio" id="default_image" type="radio" role="default">
					                                                <input value="<lb:get key='lab_kb_default_images'/>" style="border: 1px solid transparent;padding:3px 8px;" class="wzb-btn-blue" type="button" name="" onclick="useDefaultImage();document.getElementById('default_image').checked=true;show_default_image()">
					                                          		<a style="display: none;" class="thickbox" href="#TB_inline?height=380&amp;width=580&amp;inlineId=myOnPageContent" id="default_btn"></a>
					                                            	<input name="default_image" type="hidden" value="${ctx}/specialtopic/topic.jpg">
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
						                                        		<input onchange="$(this).siblings('.file_txt').html(this.value);$(this).siblings('.file_txt').val(this.value);" type="file" name="image" class="file_file" onfocus="" onblur="" onclick="document.getElementById('local_image').checked=true;" title="" value="" id="file_photo_url">
						                                             	<input value='<lb:get key="no_select_file"/>' class="file_txt">
						                                                <div class="file_button-blue"><lb:get key="usr_browse" /></div>
					                                                </div>
																	<br>
						                                             <span class="text">&nbsp;&nbsp;<lb:get key="label_lm.label_core_learning_map_49" /><br></span>
					                                          	</label>
					                                        </td>
					                                    </tr>
					                                    
					                                </tbody>
					                            </table>
					                        </td>
					                    </tr>
					                    
		                            	<tr>
		                                	<td valign="top" class="wzb-form-label"><span class="wzb-form-star">*</span><lb:get key="label_lm.label_core_learning_map_57" />：</td>
		                                	<td class="wzb-form-control">
			                                    <textarea class="wzb-inputTextArea" id="ust_summary"  name="ust_summary"       rows="4" style="width:400px">${specialTopic.ust_summary }</textarea>
			                             		<label for="ust_summary" class="error" id="error-summary" style="display:none"></label> 
		                             		</td>                               
		                           		</tr>
		                           		
		                            	<tr>
			                                <td class="wzb-form-label" valign="top">
			                                    <span class="wzb-form-star">*</span><lb:get key="label_lm.label_core_learning_map_47" />：
			                                </td>
			                                <td class="wzb-form-control">
		                                    	<div class="wzb-choose-box" id="s1" style="margin-top:0;">
		                                        	<input type="hidden" id="q1" name="qid" value="">
			                                    	<c:if test="${type=='update' }">
					                                	<c:forEach var="item" varStatus="s" items="${specialTopic.items }">
							                              	<div id="send_user_${item.usi_itm_id }" class="wzb-choose-info" value=" ${item.usi_itm_id }">
								    							<span class="wzb-choose-detail">${item.title }</span>
								    							<a class="wzb-choose-area" href="javascript:void(0)" onclick="removeDiv(${item.usi_itm_id });">  <i class="fa fa-remove"></i> </a>
							    							</div>
							                           </c:forEach>
			                         				</c:if>
			                                    </div>
			                                </td>
		                            	</tr>
			                            <tr>
			                                <td class="wzb-form-label"></td>
			                                <td class="wzb-form-control">
			                                    <a data-toggle="modal" data-target="#chooseUser" href="#" class="wzb-box-4-add btn wzb-btn-blue" id="xuanke"><lb:get key="label_lm.label_core_learning_map_50" /></a> &nbsp;|&nbsp;<a class="skin-color" onclick="clearAll();" href="javascript:void(0);"><lb:get key="label_lm.label_core_learning_map_51" /></a>
			                                	<label class="error" id="error-course" style="display:none"></label> 
			                                </td>
			                            </tr>
			                        </tbody>
			                    </table>
			                </div>
		                
			                <div id="senior" class="tab-pane" role="tabpanel">
			                    <table>
			                        <tbody>
			                        
			                            <tr>
			                                <td valign="top" class="wzb-form-label"><span class="wzb-form-star">*</span><lb:get key="label_lm.label_core_learning_map_29" />：</td>
			                                <td class="wzb-form-control">
			                                    <span class="wbFormRightText">
			                                        <c:choose>
														<c:when test="${type =='update'}">
															<c:if test="${specialTopic.ust_showindex ==0 }">
						                                        <label for="ust_showindex"><input  name="ust_showindex" type="radio" value="1"><lb:get key="label_lm.label_core_learning_map_39" /></label>
						                                        <label for="ust_showindex"><input checked="" name="ust_showindex" type="radio" value="0"><lb:get key="label_lm.label_core_learning_map_40" /></label>
					                                   		</c:if>
						                                    <c:if test="${specialTopic.ust_showindex ==1 }">
						                                        <label for="ust_showindex"><input checked="" name="ust_showindex" type="radio" value="1"><lb:get key="label_lm.label_core_learning_map_39" /></label>
						                                        <label for="ust_showindex"><input  name="ust_showindex" type="radio" value="0"><lb:get key="label_lm.label_core_learning_map_40" /></label>
						                                   	</c:if>
														</c:when>
														<c:otherwise>
						  									<label for="ust_showindex"><input  name="ust_showindex" type="radio" value="1"><lb:get key="label_lm.label_core_learning_map_39" /></label>
			                                        		<label for="ust_showindex"><input checked="" name="ust_showindex" type="radio" value="0"><lb:get key="label_lm.label_core_learning_map_40" /></label>
														</c:otherwise>
													</c:choose>
			                                    </span>
			                                </td>
			                            </tr>
			                            
			                            <tr>
			                                <td valign="top" class="wzb-form-label"><span class="wzb-form-star">*</span><lb:get key="label_lm.label_core_learning_map_52" />：</td>
			                                <td class="wzb-form-control">
			                                    <span class="wbFormRightText">
			                                    	<c:choose>
														<c:when test="${type =='update'}">
						 									<c:if test="${specialTopic.ust_status ==0 }">
					                                        	<label for="ust_status"><input  name="ust_status" type="radio" value="1"><lb:get key="label_lm.label_core_learning_map_39" /></label>
						                                        <label for="ust_status"><input checked="" name="ust_status" type="radio" value="0"><lb:get key="label_lm.label_core_learning_map_40" /></label>
						                                   	</c:if>
						                                    <c:if test="${specialTopic.ust_status ==1 }">
						                                       	<label for="ust_status"><input checked="" name="ust_status" type="radio" value="1"><lb:get key="label_lm.label_core_learning_map_39" /></label>
						                                        <label for="ust_status"><input  name="ust_status" type="radio" value="0"><lb:get key="label_lm.label_core_learning_map_40" /></label>
						                                   </c:if>
														</c:when>
														<c:otherwise>
														   <label for="ust_status"><input checked="" name="ust_status" type="radio" value="1"><lb:get key="label_lm.label_core_learning_map_39" /></label>
					                                        <label for="ust_status"><input  name="ust_status" type="radio" value="0"><lb:get key="label_lm.label_core_learning_map_40" /></label>
														</c:otherwise>
													</c:choose>
			                                    </span>
			                                </td>
			                            </tr>
			                            
		                               	<tr id="tr_content">
		                                	<td valign="top" class="wzb-form-label"><lb:get key="label_lm.label_core_learning_map_41" />：</td>
											<td class="wzb-form-control">
												<textarea id="ust_content"	style="width: 300px;" class="form-control" name="ust_content">${specialTopic.ust_content }</textarea>
											 	<script type="text/javascript">
													var editor;
													KindEditor.ready(function(K) {
														var temp = {
															allowFileManager : true,
															afterBlur : function(){
											                	//编辑器失去焦点时直接同步，可以取到值
											                    this.sync();
											                }
														}
														
														editor = K.create('#ust_content',$.extend(temp,minKindeditorOptions)
																);
													});
												</script>
												<label class="error" id="error-content" style="display:none"><!-- 内容不能为空 -->
												<lb:get key="label_rm.label_core_requirements_management_19"/></label>
											</td>
										</tr>
			                            <%-- <tr>
			                                <td class="wzb-form-label" valign="top">
			                                    <lb:get key="label_lm.label_core_learning_map_68" />：
			                                </td>
											<td class="wzb-form-control">
												<div class="wzb-choose-box" id="experts" style="width: 400px;">
						                        	<c:if test="${type=='update' }">
			                                			<c:forEach var="expert" varStatus="s" items="${specialTopic.experts }">
										            		<div id="send_user_${expert.use_ent_id }" class="wzb-choose-info" value="${expert.use_ent_id }">
																<span class="wzb-choose-detail"> ${expert.title }</span>
															    <a class="wzb-choose-area" href="javascript:;" onclick="cancelUser(${expert.use_ent_id },'${expert.title }')">
																	<i class="fa fa-remove"></i>
																</a>
													     	</div>    
												     	</c:forEach>                    
					      							</c:if>
											  	</div>
												<input type="hidden" id="expert_ids" name="expert_ids" >
			
												<div class="modal fade" id="addInstructorModal" tabindex="-1" role="dialog" aria-labelledby="addInstructorModalLabel" aria-hidden="true">
													<div class="modal-dialog">
														<div class="modal-content cont">
								                    		<div class="modal-header" style="background:#00aeef;color:#fff;border:none;">
								                          		<button type="button" class="close" style="color:#fff;opacity:1;" data-dismiss="modal" aria-hidden="true" >&times;</button>
								                          		<h4 class="modal-title" id="myModalLabel"> <lb:get key="label_lm.label_core_learning_map_68" /></h4>
								                        	</div>
  															
															<div class="modal-body" style="height:552px;overflow-y: auto;">
			                                                    <div class="form-search form-tool">  
			                                                     	<input type="text" class="form-control"  name="search_add_member" placeholder="<lb:get key="label_lm.label_core_learning_map_45" />">              
			                                 						<input type="button" class="form-submit" value="" onclick="searchUserList()" />
			                                                    </div>
				                                                <div class="form-tool wzb-title-3" style="font-weight: normal;">                 
		                                                    		<lb:get key="subordinate_filter_result"/>
			                                                      	<div class="form-tool-right">
				                                                    	<input id="addAll" type="button" class="btn wzb-btn-yellow" value='<lb:get key="button_add_all"/>' onclick="addAll()"/>
				                                                    </div>
				                                               	</div>
																<div class="wzb-percent clearfix " id="wzb-pop-1-main"></div>
				                                            </div>
				                                            
				                                            <div class="modal-footer">
																<button type="button" class="btn wzb-btn-blue wzb-btn-big" data-dismiss="modal"><lb:get key="button_close"/></button>
															</div>
								                      	</div>
													</div>
												</div>
											</td>
			                            </tr>
			                            <tr>
			                                <td class="wzb-form-label"></td>
			                                <td class="wzb-form-control">
			                                    <a data-toggle="modal" data-target="#addInstructorModal" href="#" class="wzb-box-4-add btn wzb-btn-blue"><lb:get key="label_lm.label_core_learning_map_50" /></a> &nbsp;|&nbsp;<a class="skin-color" onclick="clearExpertAll();" href="javascript:void(0);"><lb:get key="label_lm.label_core_learning_map_51" /></a>
			                                </td>
			                            </tr> --%>
			                        </tbody>
			                    </table>
			                </div>
		            	</div><!--tab-content end-->
		
			           	<table>
			            	<tbody>
				                <tr>
				                    <td class="wzb-form-label"></td><td class="wzb-ui-module-text"><span class="wzb-form-star">*</span><lb:get key="label_lm.label_core_learning_map_53" /></td>
				                </tr>
				                <tr>
				                    <td height="10" align="right" width="20%"></td><td height="10" width="80%"></td>
				                </tr>
			               	</tbody>
			           	</table>
		
			          	<div class="wzb-bar">
							<input id="createBtn" type="submit" name="frmSubmitBtn" value="<lb:get key='global.button_ok'/>" class="btn wzb-btn-blue wzb-btn-big margin-right15" onclick="javascript:void(0);"><!-- 确定 --> 
							<input type="button" name="frmSubmitBtn" value="<lb:get key='global.button_cancel'/>" class="btn wzb-btn-blue wzb-btn-big" onclick="javascript:window.history.go(-1)"><!-- 取消 -->
						</div>
						
		        	</div>
		    	</div><!-- 内容添加结束 -->
        	</form>
        	
    		<script type="text/javascript">
	    		$("#xuanke").click(function(){
					layer.open({
			            type: 2,
			            skin:'layui-layer-lan',
			            area: ['900px', '550px'], //宽高
			            title : cwn.getLabel('label_core_learning_map_47'),
			            content:  [contextPath + '/app/admin/tree/getCourseTreemultiple','no']
					});   
	    		})
			</script>
			<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_lm_${lang}.js"></script>
			<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_bdm_${lang}.js"></script>
			<script type="text/javascript" src="${ctx}/js/jquery.validate.js"></script>
			<script type="text/javascript" src="${ctx}/static/admin/js/specialTopic/add.js"></script>
			<script type="text/javascript">
				function removeDiv(id){
				   var ids=$('#q1').val();
				   var str="#send_user_";
				   str+=id;
				   var arr=ids.split("~");
				   for (var i = 0; i < arr.length; i++) {
						if(arr[i]==id){
							arr.splice(i,1);
						}
					}
				   var ar=arr.join("~");
				   $("#q1").val(ar);
				   var rs=$('#s1').find(str);
				   $(rs).remove();
				}
				
				function clearFileInput(file_obj){
					var file_obj2= file_obj.cloneNode(false);
					file_obj2.onchange= file_obj.onchange; 
					//file_obj2.disabled = true;
					file_obj.parentNode.replaceChild(file_obj2,file_obj);
				}
			
				function extension_43_change(obj) {
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
							img_obj.src = window.URL.createObjectURL(document.all.image.files[0]);
						}
					}
				}
						
				function previewLocalImage(obj) {
					var name=document.getElementById("file_photo_url").value;
					var type=name.substr(name.lastIndexOf(".")+1).toLowerCase();
					//alert(name.substr(name.lastIndexOf(".")+1).toLowerCase());
				    if(type!= "bmp"&&type!= "jpg"&&type!= "gif"){
				        Dialog.alert(fetchLabel('label_core_learning_map_122'));
				        $(".file_txt").val("");
			       	}else{
						var img_obj = document.getElementById("extension_43_id");
						img_obj.src = window.URL.createObjectURL(obj.files[0]);
			       	}
				}
			</script>
			
		</div>

		<script id="instructorTemplate" type="text/x-jsrender">
			<div class="wzb-display-01 wzb-percent-4"> 
			   <dl class="wzb-list-7 clearfix">
			       	<dd>
			            <div class="wzb-user wzb-user68">
			                <img class="wzb-pic" src="{{>image}}">
			                {{if isNormal == true}} 
			                    <p class="companyInfo" style="display: none;"><lb:get key="know_ta"/></p>
			                    <div class="cornerTL" style="width: 60px; height: 60px;">&nbsp;</div>
			                    <div class="cornerTR" style="width: 60px; height: 60px;">&nbsp;</div>
			                    <div class="cornerBL" style="width: 60px; height: 60px;">&nbsp;</div>
			                    <div class="cornerBR" style="width: 60px; height: 60px;">&nbsp;</div>
			                {{/if}}
			            </div>
			       	</dd>
			      
			       	<dt>
			           	{{>usr_display_bil}}
						<p>{{>upt_title}}</p>
						<p>{{>iti_level}}</p>
			       	</dt>
			  	</dl>
				<span id="user_{{>usr_ent_id}}" class="wzb-user-plus">
					{{if add == true}}
						<a name="addUser" class="wzb-link04" href="javascript:;" onclick="addSendUser({{>usr_ent_id}},'{{>usr_display_bil}}')">
							<i class="fa fa-plus"></i>
							<lb:get key="global.button_add"/>
						</a>
					{{else}}
						<a name="addUser" class="wzb-link04" href="javascript:;" onclick="cancelUser({{>usr_ent_id}},'{{>usr_display_bil}}')">
							<i class="fa fa-plus"></i>
							<lb:get key="global.button_add"/>
						</a>
					{{/if}}
				</span>
			</div>
		</script>
	
		<script id="add-user-button" type="text/x-jsrender">
			<a name="addUser" class="wzb-link04" href="javascript:;" onclick="addSendUser({{>usr_ent_id}}, '{{>usr_display_bil}}')">
				<i class="fa fa-plus"></i>
				<lb:get key="global.button_add"/>
			</a>
		</script>

		<script id="cancel-user-button" type="text/x-jsrender">
			<span class="skin-color">
				<i class="fa f14 fbold mr5 fa-check"></i>
				<lb:get key="label_im.label_core_information_management_36"/>
			</span> | 
			<a name="cancelUser" class="grayC666" href="javascript:;" onclick="cancelUser({{>usr_ent_id}},'{{>usr_display_bil}}')"><lb:get key="global.button_cancel"/></a>
		</script>
	
		<script id="detele-user-button" type="text/x-jsrender">
			<div id="send_user_{{>usr_ent_id}}" class="wzb-choose-info" value="{{>usr_ent_id}}">
				<span class="wzb-choose-detail">{{>usr_display_bil}}</span>
				<a class="wzb-choose-area" href="javascript:;" onclick="cancelUser({{>usr_ent_id}},'{{>usr_display_bil}}')">
					<i class="fa fa-remove"></i>
				</a>
			</div>
		</script>
	
	</body>
</html> 