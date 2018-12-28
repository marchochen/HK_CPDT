<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
 <script type="text/javascript" src="${ctx}/js/jquery.js"></script>
 <script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
  <link rel="stylesheet" href="${ctx}/static/js/bootstrap/css/bootstrap.css">
<link rel="stylesheet" href="${ctx}/js/tree/css/ztree.css" />
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-admin.css" />
<link rel="stylesheet" href="${ctx}/static/css/base.css" />
<link rel="stylesheet" href="${ctx}/static/admin/css/admin.css" />
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-default.css" />
<link rel="stylesheet" href="${ctx}/static/admin/css/font-awesome/css/font-awesome.min.css" />
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/global_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_lm_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/js/tree/js/jquery.ztree.core.js"></script>
<script type="text/javascript" src="${ctx}/js/wb_item.js"></script>
<script type="text/javascript" src="${ctx}/js/wb_utils.js"></script>
<script type="text/javascript" src="${ctx}/js/wb_application.js"></script>
	<script type="text/javascript" src="${ctx}/static/js/layer/layer.js"></script> 
<script type="text/javascript" src="${ctx}/static/js/cwn_utils.js"></script>
<script type="text/javascript" src="${ctx}/static/js/jsrender.js"></script>
  <script type="text/javascript">
  var dt, itmParams;
  $(function(){
	  dt = $("#itemList").table({
			 url : '${ctx}/app/admin/position/mapPageJson',
				colModel : dtModel,
		        rp : 5,
		        hideHeader : false,
				sortname : 'upc_update_datetime',
				sortorder : 'desc',
		        usepager : true,
		        hight:600,
		        params:itmParams
		    });
  
	  document.onkeydown = function(e){ 
		    var ev = document.all ? window.event : e;
		    if(ev.keyCode==13) {

		    	  var p = {
		        		  upc_id:$("#grade-select").val(),
		                  searchText : $("input[name='searchText']").val()
		              }
		          itmParams = $.extend(itmParams, p);
		          $(dt).reloadTable({
		              params : itmParams
		          });
		     }
		}
		 $("#grade-select").change(function(){
	            var p = {
		    			  upc_id:$(this).val(),
		                  searchText : $("input[name='searchText']").val()
		              }
		          itmParams = $.extend(itmParams, p);
		          $(dt).reloadTable({
		              params : itmParams
		          });

	    });
  $("#searchBtn").click(function(){
      var p = {
    		  upc_id:$("#grade-select").val(),
              searchText : $("input[name='searchText']").val()
          }
      itmParams = $.extend(itmParams, p);
      $(dt).reloadTable({
          params : itmParams
      });
  })
  });
    
    var dtModel = [
                   {
                  		display : " ",
                  		align : 'left',
                  		width : '1%',
                  		sortable : false,
                  		format : function(data) {
                  			p = {
                  				text : data.upt_id,
                  			}
                  			return $('#input-template').render(p);
                  		}
                  	},  
           {
           		display : cwn.getLabel('global_title'),//"标题",
           		align : 'left',
           		width : '50%',
           		sortable : true,
           		name : 'upt_title',
           		format : function(data) {
           			p = {
           				text : data.upt_title,
           			}
           			return $('#text-template').render(p);
           		}
           	},  
{
    display : cwn.getLabel('global_code'),//"编号",
    align : 'left',
    width : '24%',
    sortable : true,
    name : 'upt_code',
		format : function(data) {
			p = {
				text : data.upt_code
			}
			return p.text;
		}
	},{
		display : cwn.getLabel('global_kind'),//"类型",
		align : 'left',
		width : '25%',
		sortable : true,
		name : 'upc_title',
		format : function(data) {
			if (typeof(data.upc_title) == "undefined") { 
				data.upc_title=cwn.getLabel('label_core_learning_map_84');
				}  
			p = {
				text : data.upc_title
			}
			return p.text;
		}
	} 

];
</script>

</head>
<body>
<div class="panel wzb-panel" style="height: 550px;">
	<div class="panel-body" style="min-height:500px;height:500px;overflow-y:auto;padding:0 15px;">
          <form class="form-search" onsubmit="return false;">
	         		<lb:get key='label_lm.label_core_learning_map_20'/>&nbsp;&nbsp;&nbsp;	<select class="wzb-form-select" name="grade-select" style="margin-right:80px;" id="grade-select">
	                <option value="0" selected=""><lb:get key="label_lm.label_core_learning_map_13"/></option>
		             <c:forEach items="${catalogs }" var="catalog">
		                 <option value="${catalog.upc_id }">${catalog.upc_title }</option>
		             </c:forEach>
                </select>
                <input type="text" class="form-control" name="searchText" placeholder="<lb:get key='label_tm.label_core_training_management_32'/>">
                <input type="button" class="form-submit" value=""  id="searchBtn">
           </form>
                       
			<div role="tabpanel" class="wzb-tab-2" >
               <ul class="nav nav-tabs" role="tablist"></ul>
               <form><span id="itemList"></span></form>             
			</div>
		  	<div class="wzb-bar">
		     	<input style="border:0;" value="<lb:get key='global.button_ok'/>" name="" class="btn wzb-btn-blue wzb-btn-big  margin-right10" onclick="doSub()" type="button">
		      	<input style="border:0;" value="<lb:get key='global.button_cancel'/>" name="" class="TB_closeWindowButton wzb-btn-big  btn wzb-btn-blue " onclick="closeLayer()" type="button">
		  	</div>
	</div>
</div>
<script id="text-template" type="text/x-jsrender">
		{{>text}}
</script>

<script id="courseGridTemplate" type="text/x-jsrender">

 <dl class="wzb-list-6">
      <dd>
           <div class="main_img">
                  <img class="fwpic" src="{{:itm_icon}}">
                  <div class="show">
                       <span class="imgArea">
                             <a title="" href="javascript:itm_lst.get_item_detail({{:itm_id}})">
							 <lb:get key="label_tm.label_core_training_management_80"/></a>
                       </span>
                  </div>
            </div>
      </dd>
      <dt>
           <p><a class="wzb-link01" href="javascript:itm_lst.get_item_detail({{:itm_id}})" title="">{{:itm_title}}</a></p>
           <div class="offheight clearfix">
				<div class="offwidth">
					<span class="color-gray999">
						<lb:get key="label_tm.label_core_training_management_34"/> 
					</span>{{:itm_type_str}}
				</div> 
				<div class="offwidth">
					<span class="color-gray999">
						<lb:get key="label_tm.label_core_training_management_35"/>
					</span>
					{{:itm_code}}
				</div>
			</div>
           <div class="offheight clearfix"><div class="offwidth"><span class="color-gray999">
				<lb:get key="label_tm.label_core_training_management_27"/></span>{{:itm_publish_timestamp}}</div></div>
		   <div class="margin-top15">
				{{include tmpl="#courseBtnTemplate" /}}
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
<input type="button" name="frmSubmitBtn" value="<lb:get key='label_tm.label_core_training_management_31'/>" class="btn wzb-btn-blue" onclick="app.get_application_list('',{{:itm_id}},'','','','');">
{{/if}}
</span>
</script>
<script id="input-template" type="text/x-jsrender">
<input type=radio value="{{>text}}" name="title">
</script>
<script type="text/javascript">
	  function closeLayer(){
			var index = parent.layer.getFrameIndex(window.name); 
			  parent.layer.close(index);
		  }
function doSub() {
	var val=$('input:radio[name="title"]:checked').val();
	if (typeof(val) == "undefined") { 
		layer.alert(fetchLabel('label_core_learning_map_26'));
		} else{
			$.ajax({  
			type:'post',      
		    url:'${ctx}/app/admin/positionMap/checkExistPosition',  
		    data:{upt_id:val},  
		    cache:false,  
		    dataType:'json',  
		    success:function(data){  
		    	if(data.stauts==1){
		    		layer.alert(fetchLabel('label_core_learning_map_78'));
		    	}
		    		else{
		    			$.ajax({  
		    		        type:'post',      
		    			    url:'${ctx}/app/admin/position/mapPageJson',  
		    			    data:{upt_id:val},  
		    			    cache:false,  
		    			    dataType:'json',  
		    			    success:function(data){  
		    			    	parent.$('input[name="upt_title"]').val(data.rows[0].upt_title); 
		    			    	parent.$('input[name="upt_id"]').val(data.rows[0].upt_id); 
		    			    	parent.$('input[name="upt_code"]').val(data.rows[0].upt_code); 
		    			    	parent.$('input[name="upc_id"]').val(data.rows[0].upc_id);
		    			    	parent.$('input[name="upc_title"]').val(data.rows[0].upc_title); 
		    			    	closeLayer();
		    			       }  
		    			    });  
		    		
		    	}
		    }
			 });  

		} 
    
}

</script>

</body>
</html>