<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/sns.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/jquery.sns.js"></script>
<script type="text/javascript" src="${ctx}/js/wb_evaluation.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang }/label_bdm_${lang }.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/course.js"></script>

<title></title>

</head>
<body>

<div class="xyd-wrapper">
<div class="xyd-main clearfix">
<div class="wzb-model-11">
<dl class="wzb-list-18">
    <dd><a title="" href="javascript:;"><img src="${ctx }${instr.iti_img}"></a>
  <%--   <c:if test="${instr.iti_recommend > 0 }">
	    <span style="background:#ff8c00;">
	<!--     推荐 --><lb:get key="label_bdm.label_core_basic_data_management_28"/>
	    </span>
    </c:if> --%>
    </dd>
    <dt>
        <p>${instr.iti_name }</p>
        <div class="offheight clearfix">
            <div class="offwidth">
                <div class="color-gray999">
                     <!--讲师评分-->
                   <span class="color-gray999"><lb:get key="label_bdm.label_core_basic_data_management_45"/>：</span>
                    <p style="display:inline-block;">
                        <c:forEach begin="1" end="${instr.iti_score + 1}" varStatus="status">
							<c:if test="${instr.iti_score + 1 > status.index + 0.5}">
								<img src="${ctx }/static/images/wzb-star-yes.png" alt=""/>
							</c:if>
						</c:forEach>
						<c:forEach begin="1" end="${5 - instr.iti_score + 0.5}">
			    			<img src="${ctx }/static/images/wzb-star-no.png" alt=""/>
						</c:forEach>
                    </p>
                </div>
            </div>
            <div class="offwidth">
               <!--推荐-->
               <c:choose>
                  <c:when test="${instr.iti_recommend > 0 }">
                       <div class="tuijian-icon" style="color:#29E236;"><lb:get key="label_bdm.label_core_basic_data_management_28"/></div>
                  </c:when>
                  <c:otherwise>
                        <div class="" style="color:#29E236;">&nbsp;</div>
                  </c:otherwise>
               </c:choose>
               
            </div>
            <div class="offwidth">
                  <!--讲师级别 -->
                  <span class="color-gray999"><lb:get key="label_bdm.label_core_basic_data_management_3"/>：</span>
	               <c:choose>
	        		<c:when test="${instr.iti_level == 'J'}">
	        		    <lb:get key="label_bdm.label_core_basic_data_management_5"/> 
	        		</c:when>
	        		<c:when test="${instr.iti_level == 'M'}">
	        		    <lb:get key="label_bdm.label_core_basic_data_management_6"/> 
	        		</c:when>
	        		<c:when test="${instr.iti_level == 'S'}">
	        		    <lb:get key="label_bdm.label_core_basic_data_management_7"/> 
	        		</c:when>
	        		<c:when test="${instr.iti_level == 'D'}">
	        		  <lb:get key="label_bdm.label_core_basic_data_management_8"/> 
	        		</c:when>
	        	 </c:choose>
            </div> 
            <div class="offwidth">
                <!--讲师类型 -->
                <span class="color-gray999"><lb:get key="label_bdm.label_core_basic_data_management_9"/>  ：</span>
                <c:choose>
	        		<c:when test="${instr.iti_type == 'F'}">
	        		    <lb:get key="label_bdm.label_core_basic_data_management_10"/> 
	        		</c:when>
	        		<c:when test="${instr.iti_type == 'P'}">
	        		    <lb:get key="label_bdm.label_core_basic_data_management_11"/> 
	        		</c:when>
	        		<c:otherwise>
	        			<lb:get key="label_bdm.label_core_basic_data_management_11"/>
	        		</c:otherwise>
        	    </c:choose>
            </div>
        </div>
        
        
        <%-- <p title="${instr.iti_score}">
			<c:forEach begin="1" end="${instr.iti_score + 1}" varStatus="status">
				<c:if test="${instr.iti_score + 1 > status.index + 0.5}">
					<img src="${ctx }/static/images/wzb-star-yes.png" alt=""/>
				</c:if>
			</c:forEach>
			<c:forEach begin="1" end="${5 - instr.iti_score + 0.5}">
    			<img src="${ctx }/static/images/wzb-star-no.png" alt=""/>
			</c:forEach>
        </p>
        <div class="offheight clearfix"><div class="offwidth"><span class="color-gray999">
<!--         讲师级别 -->
        	<lb:get key="label_bdm.label_core_basic_data_management_3"/>：
        </span>
        
        
        	
        	<c:choose>
        		<c:when test="${instr.iti_level == 'J'}">
        		    <lb:get key="label_bdm.label_core_basic_data_management_5"/> 
        		</c:when>
        		<c:when test="${instr.iti_level == 'M'}">
        		    <lb:get key="label_bdm.label_core_basic_data_management_6"/> 
        		</c:when>
        		<c:when test="${instr.iti_level == 'S'}">
        		    <lb:get key="label_bdm.label_core_basic_data_management_7"/> 
        		</c:when>
        		<c:when test="${instr.iti_level == 'D'}">
        		  <lb:get key="label_bdm.label_core_basic_data_management_8"/> 
        		</c:when>
        	</c:choose>
          
</div> 

	<div class="offwidth"><span class="color-gray999">
<!--         讲师类型 -->
      		<lb:get key="label_bdm.label_core_basic_data_management_9"/>  ：
        </span>
       
        
        <c:choose>
        		<c:when test="${instr.iti_type == 'F'}">
        		    <lb:get key="label_bdm.label_core_basic_data_management_10"/> 
        		</c:when>
        		<c:when test="${instr.iti_type == 'P'}">
        		    <lb:get key="label_bdm.label_core_basic_data_management_11"/> 
        		</c:when>
        		<c:otherwise>
        			<lb:get key="label_bdm.label_core_basic_data_management_11"/>
        		</c:otherwise>
        	</c:choose>
        
        </div></div> --%>
        <p id="iti_expertise" data="${isT == false ? iti_expertise_areas : instr.iti_expertise_areas}"><!-- 擅长领域 -->
			<%-- <span class="color-gray999">
	        	<lb:get key="label_bdm.label_core_basic_data_management_26"/>：
	        </span>
          	<c:if test="${isT == false}">
               ${iti_expertise_areas}
          	</c:if>
          	<c:if test="${isT == true}">
          		${instr.iti_expertise_areas}
			</c:if> --%>
        </p>
    </dt>
</dl>
</div>

<div class="wzb-model-10">
<div role="tabpanel" class="wzb-tab-1">
<ul class="nav nav-tabs" role="tablist">
    <li role="presentation" class="active"><a href="#home" aria-controls="home" role="tab" data-toggle="tab">个人详情</a></li>
    <li role="presentation"><a href="#profile" aria-controls="profile" role="tab" data-toggle="tab">开课列表</a></li>
</ul>

<div class="tab-content">
     <div role="tabpanel" class="tab-pane active" id="home">
          <table class="wzb-table-three margin-bottom25">
                <tr>
                     <td class="wzb-table-title">
<!--                      姓名 -->
                     	<lb:get key="label_bdm.label_core_basic_data_management_18"/>：
                     </td>
                     <td class="wzb-table-content">${instr.iti_name }</td>
                </tr>
                <c:if test="${fn:trim(instr.iti_gender) ne '' and instr.iti_gender ne null }">
                <tr>
                     <td class="wzb-table-title">
<!--                      性别 -->
                     	<lb:get key="label_bdm.label_core_basic_data_management_19"/>：
                     </td>
                     <td class="wzb-table-content"> <lb:get key="usr_gender_${instr.iti_gender}"/> </td>
                </tr>
                </c:if>
                <c:if test="${instr.iti_bday ne '' and instr.iti_bday ne null }">
                <tr>
                     <td class="wzb-table-title">
<!--                      出生日期 -->
                     	<lb:get key="label_bdm.label_core_basic_data_management_20"/>：
                     </td>
                     <td class="wzb-table-content">
                     	<fmt:formatDate value="${instr .iti_bday }" pattern="yyyy-MM-dd"/> 
                     </td>
                </tr>
                </c:if>
                <c:if test="${instr.iti_mobile ne '' and instr.iti_mobile ne null }">
                <tr>
                     <td class="wzb-table-title">
<!--                      手机号码 -->
                     	<lb:get key="label_bdm.label_core_basic_data_management_21"/>：
                     </td>
                     <td class="wzb-table-content">${instr.iti_mobile }</td>
                </tr>
                </c:if>
                <c:if test="${instr.iti_email ne '' and instr.iti_email ne null }">
                <tr>
                     <td class="wzb-table-title">
<!--                      电子邮件 -->
                    	 <lb:get key="label_bdm.label_core_basic_data_management_22"/>：
                     </td>
                     <td class="wzb-table-content">${instr.iti_email }</td>
                </tr>
                </c:if>
                <c:if test="${instr.iti_address ne '' and instr.iti_address ne null }">
                <tr>
                     <td class="wzb-table-title">
<!--                      通讯地址 -->
                    	<lb:get key="label_bdm.label_core_basic_data_management_42"/>：
                     </td>
                     <td class="wzb-table-content">${instr.iti_address }</td>
                </tr>
                </c:if>
                <c:if test="${instr.iti_highest_educational ne '' and instr.iti_highest_educational ne null }">
                <tr>
                     <td class="wzb-table-title">
<!--                      最高学历 -->
                    	 <lb:get key="label_bdm.label_core_basic_data_management_43"/>：
                     </td>
                     <td class="wzb-table-content">${instr.iti_highest_educational }</td>
                </tr>
                </c:if>
                <c:if test="${instr.iti_graduate_institutions ne '' and instr.iti_graduate_institutions ne null }">
                <tr>
                     <td class="wzb-table-title">
<!--                      毕业院校 -->
                    	 <lb:get key="label_bdm.label_core_basic_data_management_44"/>：
                     </td>
                     <td class="wzb-table-content">${instr.iti_graduate_institutions }</td>
                </tr>
                </c:if>
                <c:if test="${instr.iti_level ne '' and instr.iti_level ne null }">
                <tr>
                     <td class="wzb-table-title">
<!--                      讲师级别 -->
                   		<lb:get key="label_bdm.label_core_basic_data_management_3"/> ：
                     </td>
                     <td class="wzb-table-content">
                     
                     
                     
                       	<c:choose>
			        		<c:when test="${instr.iti_level == 'J'}">
			        		    <lb:get key="label_bdm.label_core_basic_data_management_5"/> 
			        		</c:when>
			        		<c:when test="${instr.iti_level == 'M'}">
			        		    <lb:get key="label_bdm.label_core_basic_data_management_6"/> 
			        		</c:when>
			        		<c:when test="${instr.iti_level == 'S'}">
			        		    <lb:get key="label_bdm.label_core_basic_data_management_7"/> 
			        		</c:when>
			        		<c:when test="${instr.iti_level == 'D'}">
			        		  <lb:get key="label_bdm.label_core_basic_data_management_8"/> 
			        		</c:when>
			        	</c:choose>
                     
                     
                     
                     </td>
                </tr>
                </c:if>
                
                <c:if test="${instr.iti_type_mark ne '' and instr.iti_type_mark ne null }">
                <tr>
                     <td class="wzb-table-title">
<!--                      讲师来源 -->
                   		<lb:get key="label_bdm.label_core_basic_data_management_12"/> ：
                     </td>
                     <td class="wzb-table-content">
                       	<c:choose>
			        		<c:when test="${instr.iti_type_mark == 'IN'}">
			        		    <lb:get key="label_bdm.label_core_basic_data_management_13"/> 
			        		</c:when>
			        		<c:when test="${instr.iti_type_mark == 'EXT'}">
			        		  <lb:get key="label_bdm.label_core_basic_data_management_14"/> 
			        		</c:when>
			        	</c:choose>
                     
                     </td>
                </tr>
                </c:if>
                
                
                <c:if test="${instr.iti_cos_type ne '' and instr.iti_cos_type ne null }">
                <tr>
                     <td class="wzb-table-title">
<!--                      授课类别 -->
                    	<lb:get key="label_bdm.label_core_basic_data_management_23"/>：
                     </td>
                     <td class="wzb-table-content">${instr.iti_cos_type }</td>
                </tr>
                </c:if>
                <c:if test="${instr.iti_main_course ne '' and instr.iti_main_course ne null }">
                <tr>
                     <td class="wzb-table-title" valign="top">
<!--                      主讲课程 -->
                    	<lb:get key="label_bdm.label_core_basic_data_management_24"/>：
                     </td>
                     <td class="wzb-table-content">${instr.iti_main_course }</td>
                </tr>
                </c:if>
                <c:if test="${instr.iti_introduction ne '' and instr.iti_introduction ne null }">
                <tr>
                     <td class="wzb-table-title" valign="top">
<!--                      讲师简介 -->
                    	<lb:get key="label_bdm.label_core_basic_data_management_25"/>：
                     </td>
                     <td class="wzb-table-content">${instr.iti_introduction }</td>
                </tr>
                </c:if>
                <c:if test="${instr.iti_expertise_areas ne '' and instr.iti_expertise_areas ne null }">
                <tr>
                     <td class="wzb-table-title" valign="top">
<!--                      擅长领域： -->
                    	<lb:get key="label_bdm.label_core_basic_data_management_26"/>：
                     </td>
                     <td class="wzb-table-content">${instr.iti_expertise_areas }</td>
                </tr>
                </c:if>
                <c:if test="${instr.iti_good_industry ne '' and instr.iti_good_industry ne null }">
                <tr>
                     <td class="wzb-table-title" valign="top">
<!--                      擅长行业 -->
                    	<lb:get key="label_bdm.label_core_basic_data_management_27"/>：
                     </td>
                     <td class="wzb-table-content">${instr.iti_good_industry }</td>
                </tr>   
                </c:if>             
          </table>
          
          <%-- <div class="wzb-model-5">
                <span class="btn wzb-btn-gray">
<!--                 可提供课程如下 -->
					<lb:get key="label_bdm.label_core_basic_data_management_29"/>
                </span>
                                                                                                  
                <div class="wzb-model-1">
                     <span id="cosList">     
                          
                     </span>
                </div>
          </div> --%>
     </div>
     <div role="tabpanel" class="tab-pane" id="profile">
     	  <span id="itemList">
     	  </span>
     </div>
</div>
</div>

</div>


</div>
</div> <!-- xyd-wrap End-->

<script type="text/javascript">
var len = 180;

var dtItem;
$(function(){
    
	var desc = $("#iti_expertise").attr("data");
	var is_desc_sub;
	var iti_desc_sub = desc ? substr(desc, 0, len) : "";
	if(desc == undefined || getChars(desc) < len) {
		 is_desc_sub = 'display:none';
	}
	if(desc != undefined)
	{
		desc=desc.replace(/\<br\/>/g,"");
	}
	if(iti_desc_sub != undefined)
	{
		iti_desc_sub=iti_desc_sub.replace(/\<br\/>/g,"");
	}
	var p = {
			iti_desc_sub : iti_desc_sub,
			is_desc_sub : is_desc_sub,
			iti_desc : desc
		};
	$("#iti_expertise").html($('#expertise-template').render(p));
	
	dtItem = $("#itemList").table({
		url : '${ctx}/app/instr/pageItemCos/${id}',
		colModel : colModel,
		rp : globalPageSize,
		showpager : 10,
		hideHeader : true,
		usepager : true,
		params : {
		},
		onSuccess : function(data){

		}
	})
})
var colModel = [ {
		format : function(data) {
			var is_desc_sub;
			var itm_desc_sub = data.itm_desc ? substr(data.itm_desc, 0, len) : "";
			if(data.itm_desc == undefined || getChars(data.itm_desc) < len) {
				 is_desc_sub = 'display:none';
			}
			var p = {
					title : data.itm_title,
					itm_desc_sub : itm_desc_sub,
					is_desc_sub : is_desc_sub,
					
					itm_ref_ind : data.itm_ref_ind,
					itm_title  :  data.itm_title,
					itm_mobile_ind  :  data.itm_mobile_ind,
					cnt_app_count : data.cnt_app_count, 
					itm_integrated_ind	: data.itm_integrated_ind,
					itm_eff_start_datetime :data.itm_eff_start_datetime,
					itm_icon : data.itm_icon,
					itm_type : fetchLabel(data.itm_type.toLowerCase()),
					itm_desc : data.itm_desc,
					itm_id : data.itm_id,
					encItmId : wbEncrytor().cwnEncrypt(data.itm_id),
					itm_publish_timestamp : data.itm_publish_timestamp,
					parent_title : data.parent? data.parent.itm_title + "  >  " : "",
					parent_id : data.parent? data.parent.itm_id : "",
					app_tkh_id : data.app ? data.app.app_tkh_id :"",
					ics_hours :  Wzb.displayTime(data.itm_publish_timestamp, Wzb.time_format_ymd),
					is_compulsory : data.itd? data.itd.itd_compulsory_ind == '1' ? "<div class=wzb-bixiu><lb:get key='required'/></div>" : "" :"" 
				};
			return $('#itemTemplate').render(p);
		}
	}];


var dtCos;
/* $(function(){
	dtCos = $("#cosList").table({
		url : '${ctx}/app/instr/pageCos/${id}',
		colModel : cosColModel,
		rp : globalPageSize,
		showpager : 10,
		hideHeader : false,
		usepager : true,
		params : {
		},
		onSuccess : function(data){

		}
	})
}) */
var cosColModel = [{
        display : cwn.getLabel('label_core_basic_data_management_30'),
        tdWidth : '15%',
        format : function(data) {
            p = {
                text : data.ics_title,
            }
            return $('#text-center-template').render(p);
        }
    },       {
        display : cwn.getLabel('label_core_basic_data_management_31'),
        tdWidth : '15%',
        format : function(data) {
            p = {
                text : data.ics_fee
            }
            return $('#text-center-template').render(p);
        }
    },       {
        display : cwn.getLabel('label_core_basic_data_management_32'),
        tdWidth : '15%',
        format : function(data) {
            p = {
                text : data.ics_target
            }
            return $('#text-center-template').render(p);
        }
    },       {
        display : cwn.getLabel('label_core_basic_data_management_34'),
        tdWidth : '15%',
        format : function(data) {
            p = {
                text : data.ics_hours
            }
            return $('#text-center-template').render(p);
        }
    },       {
        display : cwn.getLabel('label_core_basic_data_management_33'),
        tdWidth : '15%',
        format : function(data) {
            p = {
                text : data.ics_content
            }
            return $('#text-center-template').render(p);
        }
    },];
</script>

<!-- {{>iti_desc_sub}} -->
<script id="expertise-template" type="text/x-jsrender">
	<span class="color-gray999">
    <lb:get key="label_bdm.label_core_basic_data_management_26"/>：</span>
	<span data="{{>iti_desc}}">{{>iti_desc}}</span>

			
</script>

<script id="itemTemplate" type="text/x-jsrender">
 <dl class="wzb-list-6">
	  <dd>
		   <a class="fwim" href="${ctx}/app/course/detail/{{:encItmId}}">
				<div class="main_img">
					  <img class="fwpic" src="${ctx}{{:itm_icon}}">
					  <div class="show">
						   <span class="imgArea">
								 <em><lb:get key="label_bdm.label_core_basic_data_management_35"/></em>
						   </span>
					  </div>
				 </div>
			</a>
	  </dd>
	  <dt>
			<p>
    			<a class="wzb-link01" href="${ctx}/app/course/detail/{{>parent_id}}" >{{>parent_title}}</a>
				<a class="wzb-link01" href="${ctx}/app/course/detail/{{>itm_id}}{{if app_tkh_id}}?tkhId={{>app_tkh_id}}{{/if}}" ><img src="${ctx}/static/images/pc-icon-p.png" width="26px" height="22px"/>{{if itm_mobile_ind == 'yes'}}<img src="${ctx}/static/images/phone-icon-g.png" width="21px" height="22px"/>{{/if}}{{>title}}</a>
			</p>		
		<div class="offheight clearfix">
			<div class="offwidth">
				<span class="color-gray999"><lb:get key="label_bdm.label_core_basic_data_management_36"/>：</span>{{if cnt_app_count}}{{:cnt_app_count}}{{else}}0{{/if}}
			</div> 
			<div class="offwidth">
				<span class="color-gray999"><lb:get key="label_bdm.label_core_basic_data_management_37"/>：</span>{{:itm_publish_timestamp}}
			</div>
		</div>
		   <p><span class="color-gray999">
            <span class="color-gray999"><lb:get key="course_introduction"/>：</span>
			<span data="{{>itm_desc}}">{{>itm_desc_sub}}</span>
            <a class="wzb-show skin-color open_desc" style="{{>is_desc_sub}}" >
				<span><lb:get key="click_down"/></span>
				<i class="fa fa-angle-down"></i>
			 </a>
          </p>

	  </dt>
 </dl>
</script>

<script id="text-center-template" type="text/x-jsrender">
        <div class="text-left">{{>text}}</div>
</script>

</body>
</html>