 <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
 <script type="text/javascript" src="${ctx}/js/jquery.js"></script>
 <script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<link rel="stylesheet" href="${ctx}/js/tree/css/ztree.css" />
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-admin.css" />
<link rel="stylesheet" href="${ctx}/static/css/learner.css"/>
<link rel="stylesheet" href="${ctx}/static/css/thickbox.css"/>
<link rel="stylesheet" href="${ctx}/static/theme/blue/css/style.css"/>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-default.css" />
<script type="text/javascript" src="${ctx}/static/js/bootstrap/js/bootstrap.min.js" ></script>
<link rel="stylesheet" href="/static/js/bootstrap/css/bootstrap.css"/>

<link rel="stylesheet" href="${ctx}/static/js/bootstrap/css/less/mixins/clearfix.less"/>

<link rel="stylesheet" href="${ctx}/static/admin/css/admin.css" />
<link rel="stylesheet" href="${ctx}/static/css/base.css" />
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/global_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_tm_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/js/tree/js/jquery.ztree.core.js"></script>
<script type="text/javascript" src="${ctx}/js/wb_item.js"></script>
<script type="text/javascript" src="${ctx}/js/wb_utils.js"></script>
<script type="text/javascript" src="${ctx}/js/wb_application.js"></script>
<script type="text/javascript" src="${ctx}/static/js/cwn_utils.js"></script>
<script type="text/javascript" src="${ctx}/static/js/jsrender.js"></script>
<script type="text/javascript" src="${ctx}/static/js/layer/layer.js"></script>
  <script type="text/javascript">
  var dt, itmParams;
  var arr=[];
  $(function(){
	  dt = $("#itemList").table({
	        	url : '${ctx}/app/user/getInstructors',
	        	params : itmParams,
	    		gridTemplate : function(data){
	    			p = {
	    				image : data.usr_photo,
	    				usr_display_bil : data.usr_display_bil,
	    				ugr_display_bil : data.ugr_display_bil,
	    				href : '/app/personal/' + data.usr_ent_id,
	    				usr_ent_id : data.usr_ent_id,
	    				add : true,
	    			}
	    			return $('#expertTemplate').render(p);
	    		},
	    		view : 'grid',
	    		rowSize : 4,
	    		rp : 12,
	    		showpager : 5,
	    		usepager : true,
	    		hideHeader : true,
	    		trLine : false, 
	            onSuccess : function(data){
	                //给添加按钮事件
	            	//默认选中的给予标记选中
	             	 parent.$("#experts .wzb-choose-info").each(function(){
	             		var usr_ent_id = $(this).attr("value");
	             		var usr_display_bil =  $(this).find(".wzb-choose-detail").text();
	             		p = {
	             			usr_ent_id : usr_ent_id,
	             			usr_display_bil : usr_display_bil
	             		}
	             		$("#user_" + usr_ent_id).html($("#cancel-user-button").render(p));
	                });                
	            }
	      });
		
  
	  document.onkeydown = function(e){ 
		    var ev = document.all ? window.event : e;
		    if(ev.keyCode==13) {

		    	  var p = {
		                  searchText : $("input[name='searchContent']").val()
		              }
		          itmParams = $.extend(itmParams, p);
		          $(dt).reloadTable({
		              params : itmParams
		          });
		     }
		}
  $("#searchBtn").click(function(){
      var p = {
    		  searchContent : $("input[name='searchContent']").val()
          }
      itmParams = $.extend(itmParams, p);
      $(dt).reloadTable({
          params : itmParams
      });
  })
  });
  
  function addSendUser(usr_ent_id,usr_display_bil){
		var p = {
				usr_ent_id : usr_ent_id,
				usr_display_bil : usr_display_bil
		}
		$("#user_" + usr_ent_id).html($("#cancel-user-button").render(p));
		
		parent.$("#experts").append($("#detele-user-button").render(p));
	}
	//取消已选中的专家
	function cancelUser(usr_ent_id, usr_display_bil){
		var p = {
			usr_ent_id : usr_ent_id,
			usr_display_bil : usr_display_bil
		}
		$("#user_" + usr_ent_id).html($("#add-user-button").render(p));
		
		parent.$("#send_user_" + usr_ent_id).remove();
	}
	
   
</script>

</head>
<body>
<div class="panel wzb-panel">
<div class="panel-body">
                                    <div class="modal-dialog">
                                    <div class="modal-content">
      <div class="modal-body">
                                    <div class="wzb-model-3">
          <form class="form-search form-tool" onsubmit="return false;">
                        <input type="text" class="form-control"  name="searchContent" placeholder="<lb:get key="label_lm.label_core_learning_map_45" />">
                        <input type="button" class="form-submit"   value=""  id="searchBtn">
                    </form>
                       
<div class="form-tool wzb-title-3">
                                          <lb:get key="label_lm.label_core_learning_map_38" />

                      
                                        <div class="wzb-percent clearfix datatable" style="width: 100%;">
                                                <div class="datatable-body">
 <span id="itemList">
 </span>
           <div class="modal-footer closeer">
                                </div>      
                                </div>
                                </div>
                                <div style="float:right;">
                                    <button type="button" class="btn wzb-btn-blue" onclick="closeLayer();" data-dismiss="modal">  <lb:get key="label_lm.label_core_learning_map_46" /></button></div>
                                </div></div></div>
                                </div>
 </div>
</div>
</div>
<script type="text/x-jsrender" id="expertTemplate">

<div class="wzb-display-01 wzb-percent-4"> 
   <dl class="wzb-list-7 clearfix">
       <dd>
            <div class="wzb-user wzb-user68">
                <a {{if isNormal == true}}href="{{>href}}"{{/if}}> <img class="wzb-pic" src="{{>image}}"></a>
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
            <a class="wzb-link04" href="#" title="">{{>usr_display_bil}}</a>
            <p>{{>ugr_display_bil}}</p>
       </dt>
  </dl>
	<span id="user_{{>usr_ent_id}}">
				{{if add == true}}
					<a name="addUser" class="btn wzb-btn-blue" href="javascript:;" onclick="addSendUser({{>usr_ent_id}},'{{>usr_display_bil}}')">
						<i class="fa fa-plus"></i>
						<lb:get key="global.button_add"/>
					</a>
				{{else}}
					<a name="addUser" class="btn wzb-btn-blue" href="javascript:;" onclick="cancelUser({{>usr_ent_id}},'{{>usr_display_bil}}')">
						<i class="fa fa-plus"></i>
						<lb:get key="global.button_add"/>
					</a>
				{{/if}}
		</span>
</div>
</script>

<script type="text/javascript">
function getCheckedIds() {
	var isChecked = $('#cms_all').is(":checked");
	if (!isChecked) {
		$('input[name="cms_checkbox"]').each(function() {
			$(this).prop("checked", false);
		});
	} else {
		$('input[name="cms_checkbox"]').each(function() {
			$(this).prop("checked", true);
		});
	}
}
  function closeLayer(){
	var index = parent.layer.getFrameIndex(window.name); 
	  parent.layer.close(index);
  }
function addBxk() {
var text="";
var ids="";
    $('input[name="cms_checkbox"]:checked').each(function() {
    	ids += $(this).val() + '~'; 
    	
    	text+='<div id="send_user_'+$(this).val()+'" class="wzb-choose-info" value="'+$(this).val()+'">'+
    	'<span class="wzb-choose-detail">'+$(this).attr("item")+'</span>' +'<a class="wzb-choose-area" href="javascript:void(0)" onclick="removeDiv('+$(this).val()+')">  <i class="fa fa-remove"></i> </a></div>';
    });
    if (ids == '') {
        layer.alert(fetchLabel('please_select'));
    } else {
    	ids=ids.substring(0,ids.length-1);
    	text+='<input type="hidden" id="qid" name="qid" value="'+ids+'">';
    	 parent.$('.wzb-choose-box').html(text);
    	 closeLayer();
    }
}
</script>
<script id="add-user-button" type="text/x-jsrender">
	<a name="addUser" class="btn wzb-btn-blue" href="javascript:;" onclick="addSendUser({{>usr_ent_id}}, '{{>usr_display_bil}}')">
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