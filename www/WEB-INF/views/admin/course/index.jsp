<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/js/tree/css/ztree.css" />
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-admin.css" />
<script type="text/javascript" src="${ctx}/js/wb_attendance.js"></script>
<script type="text/javascript" src="${ctx}/js/wb_course.js"></script>
<script type="text/javascript" src="${ctx}/js/wb_item.js"></script>
<script type="text/javascript" src="${ctx}/js/wb_application.js"></script>
<script>
 attn = new wbAttendance; 
 course_lst = new wbCourse;
 itm_lst = new wbItem;
 app = new wbApplication;
 lang = '${lang}';
</script>

</head>

<body>
    <c:if test="${type eq 'course'}">
        <input type="hidden" name="belong_module" value="FTN_AMD_TRAINING_MGT"/>
    </c:if>
    
    <c:if test="${type eq 'open'}">
        <input type="hidden" name="belong_module" value="FTN_AMD_TRAINING_MGT"/>
    </c:if>
    
    <c:if test="${type eq 'exam'}">
        <input type="hidden" name="belong_module" value="FTN_AMD_EXAM_MGT"/>
    </c:if>
    
    <c:set var="HAS_FTN_AMD_ITM_COS_MAIN_VIEW" value="false"></c:set>
    <c:set var="HAS_FTN_AMD_ITM_COS_MAIN_PERFORMANCE" value="false"></c:set>
    <c:set var="HAS_FTN_AMD_ITM_COS_MAIN_APPLICATION" value="false"></c:set>
    
    <c:set var="HAS_FTN_AMD_EXAM_MAIN_VIEW" value="false"></c:set>
    <c:set var="HAS_FTN_AMD_EXAM_MAIN_PERFORMANCE" value="false"></c:set>
    <c:set var="HAS_FTN_AMD_EXAM_MAIN_APPLICATION" value="false"></c:set>
    
    <c:set var="HAS_FTN_AMD_OPEN_COS_MAIN" value="false"></c:set>
    
    <c:choose>
	    <c:when test="${type eq 'course' }">
	    	<wb:has-any-permission permission="FTN_AMD_ITM_COS_MAIN_VIEW">
	    		<c:set var="HAS_FTN_AMD_ITM_COS_MAIN_VIEW" value="true"></c:set>
	    	</wb:has-any-permission>
	    	
	    	<wb:has-any-permission permission="FTN_AMD_ITM_COS_MAIN_PERFORMANCE">
	    		<c:set var="HAS_FTN_AMD_ITM_COS_MAIN_PERFORMANCE" value="true"></c:set>
	    	</wb:has-any-permission>
	    	
	    	<wb:has-any-permission permission="FTN_AMD_ITM_COS_MAIN_APPLICATION">
	    		<c:set var="HAS_FTN_AMD_ITM_COS_MAIN_APPLICATION" value="true"></c:set>
	    	</wb:has-any-permission>
	    </c:when>
		<c:when test="${type eq 'open' }">
			<wb:has-any-permission permission="FTN_AMD_OPEN_COS_MAIN">
	    		<c:set var="HAS_FTN_AMD_OPEN_COS_MAIN" value="true"></c:set>
	    	</wb:has-any-permission>
		</c:when>
		<c:otherwise>
			<wb:has-any-permission permission="FTN_AMD_EXAM_MAIN_VIEW">
	    		<c:set var="HAS_FTN_AMD_EXAM_MAIN_VIEW" value="true"></c:set>
	    	</wb:has-any-permission>
	    	
	    	<wb:has-any-permission permission="FTN_AMD_EXAM_MAIN_PERFORMANCE">
	    		<c:set var="HAS_FTN_AMD_EXAM_MAIN_PERFORMANCE" value="true"></c:set>
	    	</wb:has-any-permission>
	    	
	    	<wb:has-any-permission permission="FTN_AMD_EXAM_MAIN_APPLICATION">
	    		<c:set var="HAS_FTN_AMD_EXAM_MAIN_APPLICATION" value="true"></c:set>
	    	</wb:has-any-permission>
		</c:otherwise>
	</c:choose>
	
	<c:set var="showItmQuickEntrance" value="false"></c:set>
	
	<c:choose>
	    <c:when test="${type eq 'course' }">
	    	<c:if test="${HAS_FTN_AMD_ITM_COS_MAIN_VIEW or HAS_FTN_AMD_ITM_COS_MAIN_PERFORMANCE or HAS_FTN_AMD_ITM_COS_MAIN_APPLICATION}">
	    		<c:set var="showItmQuickEntrance" value="true"></c:set>
	    	</c:if>
	    </c:when>
		<c:when test="${type eq 'open' }">
			<c:if test="${HAS_FTN_AMD_OPEN_COS_MAIN }">
	    		<c:set var="showItmQuickEntrance" value="true"></c:set>
	    	</c:if>
		</c:when>
		<c:otherwise>
			<c:if test="${HAS_FTN_AMD_EXAM_MAIN_VIEW or HAS_FTN_AMD_EXAM_MAIN_PERFORMANCE or HAS_FTN_AMD_EXAM_MAIN_APPLICATION}">
	    		<c:set var="showItmQuickEntrance" value="true"></c:set>
	    	</c:if>
		</c:otherwise>
	</c:choose>
   
   <c:choose>
   		<c:when test="${type eq 'exam'}">
   			<title:get function="global.FTN_AMD_EXAM_MGT"/>
   		</c:when>
   		<c:otherwise>
   			<title:get function="global.FTN_AMD_TRAINING_MGT"/>
   		</c:otherwise>
   </c:choose>	

    <ol class="breadcrumb wzb-breadcrumb">
        <li><a href="javascript:wb_utils_gen_home(true);"><i class="fa wzb-breadcrumb-home fa-home"></i><lb:get key="label_tm.label_core_training_management_1"/></a></li>
        <%-- <c:if test="${not (type eq 'exam') }">
        	<li><a href="javascript:void(0)"><lb:get key="global.FTN_AMD_TRAINING_MGT"/></a></li>
        </c:if> --%>
        <li class="active">
	       	<c:choose>
	        	<c:when test="${type eq 'course' }">
	        		<lb:get key="label_tm.label_core_training_management_2"/> 
	        	</c:when>
	        	<c:when test="${type eq 'open' }">
	        		<lb:get key="global.FTN_AMD_OPEN_COS_MAIN"/> 
	        	</c:when>
	        	<c:otherwise>
	        		<lb:get key="label_tm.label_core_training_management_3"/>
	        	</c:otherwise>
	        </c:choose>
        
        
         </li>
    </ol>
    <!-- wzb-breadcrumb End-->

    <div class="panel wzb-panel">
        <div class="panel-heading">
        	   <c:choose>
		        	<c:when test="${type eq 'course' }">
		        		<lb:get key="label_tm.label_core_training_management_2"/> 
		        	</c:when>
	        	
		        	<c:otherwise>
		        		<lb:get key="label_tm.label_core_training_management_3"/>
		        	</c:otherwise>
	        	</c:choose>
        
        	<%--
        		<lb:get key="item_${type }_main"/>
        	 --%>
        </div>

        <div class="panel-body">
            <div class="wzb-area clearfix">
                <div class="wzb-area-left">
                    
                    <wzb:tcr-admin id="tcr" event="function(e, treeId, treeNode){treeClick(e, treeId, treeNode)}"></wzb:tcr-admin>
                	<input type="hidden" id="sel_tcr_id" value="">
                	
                	   <dl class="wzb-list-4" data-group='itmParams' data-table='dt' data-column="itm_status">
                            <dt><!-- 状态 --><lb:get key="label_tm.label_core_training_management_20"/>：</dt>
                            <c:choose>
						        	<c:when test="${type eq 'course' }">
						        		<dd><a href="javascript:;" data-value="" ><!-- 所有课程 --><lb:get key="label_tm.label_core_training_management_350"/></a></dd>
			                            <dd><a href="javascript:;" data-value="ON"><!-- 所有已发布的课程 --><lb:get key="label_tm.label_core_training_management_351"/></a></dd>
			                            <c:if test="${type ne 'open'}">
			                            	<dd><a href="javascript:;" data-value="ON_MOBILE"><!-- 同时发布到移动端的课程 --><lb:get key="label_tm.label_core_training_management_352"/></a></dd>
			                            </c:if>
			                            <dd><a href="javascript:;" data-value="OFF"><!-- 未发布的课程 --><lb:get key="label_tm.label_core_training_management_353"/></a></dd> 
						        	</c:when>
						        	<c:when test="${type eq 'open' }">
						        		<dd><a href="javascript:;" data-value="" ><!-- 所有公开课 --><lb:get key="label_tm.label_core_training_management_467"/></a></dd>
			                            <dd><a href="javascript:;" data-value="ON"><!-- 所有已发布的公开课 --><lb:get key="label_tm.label_core_training_management_468"/></a></dd>
			                            <dd><a href="javascript:;" data-value="OFF"><!-- 未发布的公开课 --><lb:get key="label_tm.label_core_training_management_469"/></a></dd> 
						        	</c:when>
						        	<c:when test="${type eq 'exam' }">
						        		<dd><a href="javascript:;" data-value="" ><!-- 所有考试 --><lb:get key="label_tm.label_core_training_management_366"/></a></dd>
			                            <dd><a href="javascript:;" data-value="ON"><!-- 所有已发布的考试 --><lb:get key="label_tm.label_core_training_management_367"/></a></dd>
			                            <c:if test="${type ne 'open'}">
			                            	<dd><a href="javascript:;" data-value="ON_MOBILE"><!-- 同时发布到移动端的考试 --><lb:get key="label_tm.label_core_training_management_368"/></a></dd>
			                            </c:if>
			                            <dd><a href="javascript:;" data-value="OFF"><!-- 未发布的考试 --><lb:get key="label_tm.label_core_training_management_369"/></a></dd> 
						        	</c:when>
	        				</c:choose>
                        </dl>
                        
                        <c:if test="${type ne 'open' }"><!-- 不是公开课显示类别 -->
                        <dl class="wzb-list-4" data-group='itmParams' data-table='dt' data-column="cos_type" data-value="${cos_type }">
                            <dt><!--课程类型--><lb:get key="label_tm.label_core_training_management_9"/>：</dt>
                            <dd><a href="javascript:;" data-value="" ><!--全部--><lb:get key="label_tm.label_core_training_management_10"/></a></dd>
                            <dd><a href="javascript:;" data-value="selfstudy" ><!--网上课程, 网上考试-->
                            
                            	
                                 <c:choose>
						        	<c:when test="${type eq 'course' }">
						        		<lb:get key="label_tm.label_core_training_management_11"/> 
						        	</c:when>
			        	
						        	<c:otherwise>
						        		<lb:get key="label_tm.label_core_training_management_12"/>
						        	</c:otherwise>
	        					</c:choose>
                            
                            <%
                            	/*
                            	     <lb:get key="item_${type }_online"/>
                            	*/
                            %>
                        </a></dd>
                            <dd><a href="javascript:;" data-value="classroom" ><!--面授课程， 离线考试-->
                            
                            
                            	<c:choose>
						        	<c:when test="${type eq 'course' }">
						        		<lb:get key="label_tm.label_core_training_management_13"/> 
						        	</c:when>
			        	
						        	<c:otherwise>
						        		<lb:get key="label_tm.label_core_training_management_36"/>
						        	</c:otherwise>
	        					</c:choose>
	        					
	        					<%--
	        					
	        					 <lb:get key="item_${type }_offline"/>
	        					 --%>
                            
                            </a>
                            
                            
                            </dd>
                           <%--  <c:if test="${type ne 'exam' }"><!-- 类型不是考试显示项目式课程 -->
                            <dd><a href="javascript:;" data-value="integrated" ><!--项目式课程--><lb:get key="label_tm.label_core_training_management_14"/> </a></dd>
                            </c:if> --%>
                        </dl>
                        
                       
                        </c:if>
                     
                        
                        <dl class="wzb-list-5">
	                        <dt><!--目录分类--><lb:get key="label_tm.label_core_training_management_7"/>：</dt>
	                        <dd>
	                            <wzb:catalog-admin showRoot="true" type="${type }" event="function(e, treeId, treeNode){treeCatalogClick(e, treeId, treeNode)}"></wzb:catalog-admin>
	                        </dd>
	                    </dl>
                </div>

                <div class="wzb-area-content">
                    <form class="form-search">
                        <input type="text" class="form-control" name="searchText" placeholder="<lb:get key='label_tm.label_core_training_management_32'/>">
                        <input style='display:none' />
                        <input type="button" class="form-submit" value=""  id="searchBtn">
                        <%-- <button type="button" class="btn wzb-btn-yellow"><!-- 导入课程 -->
                             	<c:choose>
						        	<c:when test="${type eq 'course' }">
						        		<lb:get key="label_tm.label_core_training_management_23"/> 
						        	</c:when>
			        				<c:when test="${type eq 'open' }">
			        					<lb:get key="label_tm.label_core_training_management_44"/>
			        				</c:when>
						        	<c:otherwise>
						        		<lb:get key="label_tm.label_core_training_management_42"/>
						        	</c:otherwise>
	        					</c:choose>                       
                        	
                        	
                        	
                        	   <lb:get key="item_${type}_import"/>
                        	
                        
                        </button> --%>
                        
                        <c:choose>
                            <c:when test="${cos_type eq 'integrated' }">
                            	<c:if test="${HAS_FTN_AMD_ITM_COS_MAIN_VIEW }">
	                               <span style="position:relative">
	                                <button type="button"   class="btn wzb-btn-yellow" onclick="javascript:COSTypeChoose();" ><!-- 新增课程 -->
										<lb:get key="label_tm.label_core_training_management_33"/>
	                               </button>
	                               
	                               <button type="button"   class="btn wzb-btn-yellow" onclick="javascript:itm_lst.upd_item_prep_batch(false);" ><!-- 批量修改课程 -->
										<lb:get key="label_tm.label_core_training_management_275"/>
	                               </button>
	                                <div id="cos_type_choose" class="wzb-exam-choose" style="display:none;"  >
	                                	<ul style="list-style:none;">
											<li><a href="javascript:itm_lst.select_add_item_type_prep('COS','SELFSTUDY');"><lb:get key="label_tm.label_core_training_management_11"/></a></li>
											<li><a href="javascript:itm_lst.select_add_item_type_prep('COS','CLASSROOM');"><lb:get key="label_tm.label_core_training_management_13"/></a></li>
											<li><a href="javascript:itm_lst.select_add_item_type_prep('INTEGRATED','INTEGRATED');"><lb:get key="label_tm.label_core_training_management_14"/></a></li>
									    </ul>
	                                </div>
	                               </span>
	                            </c:if>
                            </c:when>
                            <c:when test="${type eq 'exam' }">
                            	<c:if test="${HAS_FTN_AMD_EXAM_MAIN_VIEW }">
	                            	<span style="position:relative">
		                                <button type="button" class="btn wzb-btn-yellow" onclick="javascript:EXAMTypeChoose();"><!-- 新增课程 -->
		                                
		                                <lb:get key="label_tm.label_core_training_management_24"/>
		                                <%--
		                                 <lb:get key="item_${type}_add"/>
		                                 --%>
		                                </button>
		                                 <div id="exam_type_choose"  class="wzb-exam-choose"  style="display:none;" >
		                                	<ul style="list-style:none;" >
												<li><a href="javascript:itm_lst.select_add_item_type_prep('EXAM','SELFSTUDY', true);"> 
												<lb:get key="label_tm.label_core_training_management_12"/></a></li>
												<li><a href="javascript:itm_lst.select_add_item_type_prep('EXAM','CLASSROOM', true);">
												<lb:get key="label_tm.label_core_training_management_36"/></a></li>
										    </ul>
		                                </div>	
	                                </span>
	                            </c:if>
                            </c:when>
                            <c:when test="${type eq 'open' }">
                            	<c:if test="${HAS_FTN_AMD_OPEN_COS_MAIN}">
	                            	<button type="button" class="btn wzb-btn-yellow" onclick="javascript:itm_lst.select_add_item_type_prep('REF','AUDIOVIDEO', true);"><!-- 新增课程 -->
	                                	<lb:get key="label_tm.label_core_training_management_25"/>
	                                </button>
                            	</c:if>
                            </c:when>
                            
                            <c:otherwise>
                             <c:if test="${HAS_FTN_AMD_ITM_COS_MAIN_VIEW }">
	                             <span style="position:relative">
	                                <button type="button"   class="btn wzb-btn-yellow" onclick="javascript:COSTypeChoose();" ><!-- 新增课程 -->
	                                
									 <lb:get key="label_tm.label_core_training_management_33"/>
	                               </button>
	                               <button type="button"   class="btn wzb-btn-yellow" onclick="javascript:itm_lst.upd_item_prep_batch(false);" ><!-- 批量修改课程 -->
										<lb:get key="label_tm.label_core_training_management_275"/>
	                               </button>
	                                <div id="cos_type_choose" class="wzb-course-choose" style="display:none;" >
	                                	<ul style="list-style:none;" >
											<li><a href="javascript:itm_lst.select_add_item_type_prep('COS','SELFSTUDY', true);">
											 <lb:get key="label_tm.label_core_training_management_11"/></a></li>
											<li><a href="javascript:itm_lst.select_add_item_type_prep('COS','CLASSROOM', true);"> <lb:get key="label_tm.label_core_training_management_13"/></a></li>
											<%-- <li><a href="javascript:itm_lst.select_add_item_type_prep('INTEGRATED','INTEGRATED');"> <lb:get key="label_tm.label_core_training_management_14"/></a></li> --%>
									    </ul>
	                                </div>
	                               </span>
                             </c:if>
								
                            </c:otherwise>
                        </c:choose>
                    
                    </form>

                    <div role="tabpanel" class="wzb-tab-2" >
                        <ul class="nav nav-tabs" role="tablist">
                            <span>
					<!--课程列表 -->

									<c:choose>
						        	<c:when test="${type eq 'course' }">
						        		<lb:get key="course_list"/> 
						        	</c:when>
			        				<c:when test = "${type eq 'open' }">
			        					<lb:get key="label_tm.label_core_training_management_45"/> 
			        				</c:when>
						        	<c:otherwise>
						        		<lb:get key="label_tm.label_core_training_management_43"/>
						        	</c:otherwise>
	        					</c:choose>
							<%--
							
											<lb:get key="item_${type }_list"/>
							 --%>
			
                            </span>
                            
                            <em data-column="itm_upd_timestamp"><!-- 最后修改日期 --><lb:get key="label_tm.label_core_training_management_339"/><i class="fa fa-caret-down skin-color"></i></em>
                            <em data-column="itm_publish_timestamp"><!-- 发布日期 --><lb:get key="label_tm.label_core_training_management_27"/></em>
                            <em data-column="itm_title"><!-- 标题 --><lb:get key="label_tm.label_core_training_management_28"/> </em>
                            <li role="presentation" class="active"><a href="#home" aria-controls="home" role="tab" data-toggle="tab"><!--图文版  --><lb:get key="global.global_style_image"/>  <i class="datatable-pager-switch datatable-pager-switch-grid" style="display: inline-block;"></i>  </a></li>
                            <li role="presentation"><a href="#profile" aria-controls="profile" role="tab" data-toggle="tab"><!-- 列表版 --><lb:get key="global.global_style_list"/> <i class="datatable-pager-switch datatable-pager-switch-list"  style="display: inline-block;"></i>    </a></li>
                        </ul>
                        
                        <span id="itemList">
                        
                        </span>
                    </div>
                    <!-- wzb-tab-2 end -->

                </div>

            </div>


        </div>
    </div>
    <!-- wzb-panel End-->
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_tm_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/js/tree/js/jquery.ztree.core.js"></script>
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript" src="${ctx}/js/wb_item.js"></script>
<script type="text/javascript" src="${ctx}/js/wb_application.js"></script>
<script type="text/javascript">var type = '${type}'; var cos_type = '${cos_type}'</script>
<script type="text/javascript" src="${ctx}/static/admin/js/course.js"></script>
<script id="title-template" type="text/x-jsrender">
{{>text}}
</script>
<script id="courseGridTemplate" type="text/x-jsrender">
 <dl class="wzb-list-6">
      <dd>
			<a title="" href="javascript:itm_lst.get_item_detail({{:itm_id}})">
            	<div class="main_img">
                  	<img class="fwpic" src="{{:itm_icon}}">
                  	<div class="show">
                  	</div>
            	</div>
			</a>
      </dd>
      <dt>
           <p><a class="wzb-link01" href="javascript:itm_lst.get_item_detail({{:itm_id}})"><img src="${ctx}/static/images/pc-icon-p.png" width="26px" height="22px"/>{{if itm_mobile_ind == 'yes' && itm_ref_ind != 1}}<img src="${ctx}/static/images/phone-icon-g.png" width="21px" height="22px"/>{{/if}}{{:itm_title}}</a></p>
           <div class="offheight clearfix" style="overflow: visible">
				<div class="offwidth" style="width:45%">
					<span class="color-gray999">
                        <c:choose>
						     <c:when test="${type eq 'course' }">
						        <lb:get key="label_tm.label_core_training_management_34"/>：
						     </c:when>
                             <c:when test = "${type eq 'open' }">
			        			<lb:get key="label_tm.label_core_training_management_34"/>：
			        		 </c:when>
						     <c:otherwise>
						        <lb:get key="label_tm.label_core_training_management_370"/>：
						     </c:otherwise>
	        			</c:choose>
					</span>{{:itm_type_str}}
				</div> 
				<div class="offwidth" style="width:45%">
					<span class="color-gray999">
					{{if itm_exam_ind==1}}
					<lb:get key="label_tm.label_core_training_management_466"/>：
					{{else}}
					<lb:get key="label_tm.label_core_training_management_35"/>：
					{{/if}}
						
					</span>
					{{:itm_code}}
				</div>
				{{include tmpl="#itmQuickEntranceTemplate"/}}

				<div class="offheight clearfix" style="float:left;width:100%">
					<div class="offwidth">
						<span class="color-gray999">
							<lb:get key="label_tm.label_core_training_management_27"/>：
						</span>
						{{:itm_publish_timestamp}}
					</div>
				</div>
			
			</div>
      </dt>
 </dl>
</script>
<script id="courseBtnTemplate" type="text/x-jsrender">
<span style="white-spacing:nowrap;">
<input type="button" name="frmSubmitBtn" value="<lb:get key='label_tm.label_core_training_management_29'/>" class="btn wzb-btn-blue" onclick="javascript:course_lst.edit_cos({{:cos_res_id}},'{{:itm_type}}','false','','false')"> 
{{if itm_type != 'CLASSROOM'}}
	<input type="button" name="frmSubmitBtn" value="<lb:get key='label_tm.label_core_training_management_30'/>" class="btn wzb-btn-blue" onclick="javascript:attn.get_grad_record({{:itm_id}});"> 
{{/if}}
{{if itm_type != 'AUDIOVIDEO'}}
<input type="button" name="frmSubmitBtn" value="<lb:get key='label_tm.label_core_training_management_234'/>" class="btn wzb-btn-blue" onclick="app.get_application_list('',{{:itm_id}},'','','','');">
{{/if}}
</span>
</script>

<!-- 每个课程功能快速操作入口，具体显示逻辑请详见需求文档 -->
<script id="itmQuickEntranceTemplate" type="text/x-jsrender">
	<span class="quick-container" style="position:relative;<c:if test="${!showItmQuickEntrance }">display:none;</c:if>">
		<i class="quick-icon"></i>
		<div class="wzb-exam-choose wzb-quick" style="display:none;">
			<ul style="list-style:none;">
				<c:choose>
			    	<c:when test="${type eq 'course' or type eq 'exam'}">

						<c:choose>
			    			<c:when test="${type eq 'course'}">
								<c:if test="${HAS_FTN_AMD_ITM_COS_MAIN_VIEW }">
									<li onclick="javascript:itm_lst.get_item_publish({{>itm_id}})"><a href="javascript:itm_lst.get_item_publish({{>itm_id}})">{{if itm_publish_timestamp && itm_publish_timestamp!='--'}}<lb:get key="label_core_training_management_256"/> {{else}}<lb:get key="label_core_training_management_255"/> {{/if}}</a></li>
								</c:if>
								{{if !isOffline}}
									<c:if test="${HAS_FTN_AMD_ITM_COS_MAIN_APPLICATION }">
										<li onclick="javascript:app.get_application_list('',{{>itm_id}},'','','','')"><a href="javascript:app.get_application_list('',{{>itm_id}},'','','','')"><lb:get key="label_core_training_management_234"/></a></li>
									</c:if>
									<c:if test="${HAS_FTN_AMD_ITM_COS_MAIN_PERFORMANCE }">
										<li onclick="javascript:attn.get_grad_record({{>itm_id}})"><a href="javascript:attn.get_grad_record({{>itm_id}})"><lb:get key="label_core_training_management_242"/></a></li>
									</c:if>
								{{/if}}
							</c:when>
			    			<c:when test="${type eq 'exam'}">
								<c:if test="${HAS_FTN_AMD_EXAM_MAIN_VIEW }">
									<li onclick="javascript:itm_lst.get_item_publish({{>itm_id}})"><a href="javascript:itm_lst.get_item_publish({{>itm_id}})">{{if itm_publish_timestamp && itm_publish_timestamp!='--'}}<lb:get key="label_core_training_management_256"/> {{else}}<lb:get key="label_core_training_management_255"/> {{/if}}</a></li>
								</c:if>
								{{if !isOffline}}
									<c:if test="${HAS_FTN_AMD_EXAM_MAIN_APPLICATION }">
										<li onclick="javascript:app.get_application_list('',{{>itm_id}},'','','','')"><a href="javascript:app.get_application_list('',{{>itm_id}},'','','','')"><lb:get key="label_core_training_management_234"/></a></li>
									</c:if>
									<c:if test="${HAS_FTN_AMD_EXAM_MAIN_PERFORMANCE }">
										<li onclick="javascript:attn.get_grad_record({{>itm_id}})"><a href="javascript:attn.get_grad_record({{>itm_id}})"><lb:get key="label_core_training_management_242"/></a></li>
									</c:if>
								{{/if}}
							</c:when>
			    		</c:choose>			
					</c:when>
			    	<c:when test="${type eq 'open'}">
						<c:if test="${HAS_FTN_AMD_OPEN_COS_MAIN }">
							<li onclick="javascript:itm_lst.get_item_publish({{>itm_id}})"><a href="javascript:itm_lst.get_item_publish({{>itm_id}})">{{if itm_publish_timestamp && itm_publish_timestamp!='--'}}<lb:get key="label_core_training_management_256"/> {{else}}<lb:get key="label_core_training_management_255"/> {{/if}}</a></li>
						</c:if>
					</c:when>
			    </c:choose>
		    </ul>
	    </div>
	</span>
</script>
</body>
</html>