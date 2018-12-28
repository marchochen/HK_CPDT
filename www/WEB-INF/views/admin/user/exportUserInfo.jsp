<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="../../admin/common/groupTree.jsp"%>
<%@ include file="../../admin/common/userTree.jsp"%>
<link rel="stylesheet" href="${ctx }/static/js/jquery.uploadify/uploadify.css"/>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-admin.css" />
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_um_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/js/wb_usergroup.js"></script>
<script type="text/javascript" src="${ctx}/static/admin/js/user/exportUserInfo.js"></script>
<script type="text/javascript">
function downExcel(){
	layer.load();
	var uIdArray = new Array;
	var gIdArray = new Array;
	var uid = document.getElementById("uid").options;   
	var gid = document.getElementById("gid").options;  
	for(var i=0;i<uid.length;i++){
		uIdArray[i] = uid[i].value;
	}  
	for(var i=0;i<gid.length;i++){
		gIdArray[i] = gid[i].value;
	} 
   	$.ajax({
           url : "${ctx}/app/admin/user/expor",
           type : 'POST',
		   data: {
			   "usr_id_lst" : uIdArray,
			   "grp_id_lst" : gIdArray,
		   },
           dataType : 'json',
           traditional : true,
           success : function(data) {
        	layer.closeAll('loading');
           	window.location.href = data.fileUri;
           }
      });
   }
</script>
</head>
<body>
       <div class="wzb-banner wzb-banner-bg01" style="opacity: 1; background:rgb(51, 102, 102);"><i class="fa wzb-banner-icon fa-child"></i><lb:get key="label_um.label_core_user_management_21"/></div>
       
        <ol class="breadcrumb wzb-breadcrumb">
            <li><a href="javascript:wb_utils_gen_home(true);"><i class="fa wzb-breadcrumb-home fa-home"></i><lb:get key="label_um.label_core_user_management_22"/></a></li>
            <li><a href="javascript:wb_utils_nav_go('FTN_AMD_USR_INFO','${prof.usr_ent_id }', '${label_lan }');"><lb:get key="label_um.label_core_user_management_23"/></a></li>
            <li class="active"><lb:get key="label_um.label_core_user_management_49"/></li>
        </ol> 
        
       <form  method="post"  id="exporForm" enctype="multipart/form-data">
        <div class="panel wzb-panel">
            <div class="panel-heading"><lb:get key="label_um.label_core_user_management_23"/></div>
            <div class="panel-body">
                <div class="wzb-ui-desc-text">   
                    <lb:get key="label_um.label_core_user_management_24"/></br>
                    <lb:get key="label_um.label_core_user_management_54"/></br>
                  <span style="margin-left: 42px;"><lb:get key="label_um.label_core_user_management_57"/></span>
                </div>
                <table>
                    <tbody>
                        <tr>
                            <td class="wzb-form-label" valign="top">
                                 <lb:get key="label_um.label_core_user_management_15"/>：
                            </td>
                            <td class="wzb-form-control">
                                <select id="gid" name="gid" class="wzb-choose-box" style="margin-top:0;" multiple="">
                                </select>
                                <!-- <div class="wzb-choose-box" style="margin-top:0;">
                                    <input type="hidden" id="uid" name="uid" value="">
                                </div> -->
                            </td>
                        </tr>
                        <tr>
                            <td class="wzb-form-label"></td>
                            <td class="wzb-form-control">
                                <a href="javascript:showUserGroup()" class="wzb-box-4-add btn wzb-btn-blue margin-right4" id="xuanzheyonghu"><lb:get key="label_um.label_core_user_management_17"/></a><a href="javascript:removeSelectedOptions('gid');" class="wzb-box-4-add btn wzb-btn-blue"><lb:get key="label_um.label_core_user_management_18"/></a>
                            </td>
                        </tr>
                        <tr>
                            <td class="wzb-form-label" valign="top">
                                <lb:get key="label_um.label_core_user_management_16"/>：
                            </td>
                            <td class="wzb-form-control">
                                <!-- <div class="wzb-choose-box" style="margin-top:0;">
                                    <input type="hidden" id="qid" name="qid" value="">
                                </div> -->
                                <select id="uid" name="uid" class="wzb-choose-box" style="margin-top:0;" multiple="">
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td class="wzb-form-label"></td>
                            <td class="wzb-form-control">
                                <a href="javascript:showUser()" class="wzb-box-4-add btn wzb-btn-blue margin-right4" id="xuanzheyonghu"><lb:get key="label_um.label_core_user_management_17"/></a><a href="javascript:removeSelectedOptions('uid');" class="wzb-box-4-add btn wzb-btn-blue"><lb:get key="label_um.label_core_user_management_18"/></a>
                            </td>
                        </tr>
                    </tbody>
                </table>
                <div class="wzb-bar">
                    <button type="button" class="btn wzb-btn-blue wzb-btn-big margin-right10" onclick="downExcel();">
                        <lb:get key="label_um.label_core_user_management_19" />
                    </button>
                    <button type="button" class="btn wzb-btn-blue wzb-btn-big" onclick="javascript:wb_utils_nav_go('FTN_AMD_USR_INFO','${prof.usr_ent_id }', '${label_lan }');">
                        <lb:get key="global.button_cancel" />
                    </button>
                </div>
            </div>
        </div>  <!-- wzb-panel End-->
        <!-- 正文内容 end -->
       </form>
      <!-- wzb-main End-->   
 <!-- wzb-wrapper End-->


</body>
</html>