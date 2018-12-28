<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<%@ include file="../common/template.jsp"%>
<!DOCTYPE html>
<html>
<head>
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/course.js"></script>
<link rel="stylesheet" href="${ctx}/static/admin/css/font-awesome/css/font-awesome.min.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css"/>
<title></title>
</head>
<body>
	<script type="text/javascript">
	wbEncrytor = new wbEncrytor;
	
	$(function() {
		
		$.ajax({  
	        type:'post',      
	        url : '${ctx}/app/learningmap/positionMapInfoJson',  
	        cache:false,
	        dataType:'json',  
	        success:function(data){
	        	data.enc_upt_id = wbEncrytor.cwnEncrypt(data.upt_id);
	        	$("#main").html($("#topTemplate").render(data));
	        }  
	   });  
		
		
		$.ajax({  
	        type:'post',      
	        url : '${ctx}/app/learningmap/positionMapListJson',  
	        cache:false,
	        dataType:'json',  
	         success:function(data){
	        	 for(var i=0; i< data.length; i++)
	        	 {
	        		 (data[i])['upt_id'] = wbEncrytor.cwnEncrypt((data[i])['upt_id']);
	        	 }
	        	 
	        $("#positionContent").html($("#centerTemplate").render(data));
	         }  
	   });  
		
		$.ajax({  
	        type:'post',      
	        url : '${ctx}/app/learningmap/positionCatalogListJson',  
	        cache:false,
	        dataType:'json',  
	         success:function(data){
	        	 for(var i=0; i<data.length; i++){
	        		var items =(data[i])["items"];
	        		for(var j=0; j<items.length; j++){
	        			(((data[i])["items"])[j])["upt_id"] = wbEncrytor.cwnEncrypt((((data[i])["items"])[j])["upt_id"]);
	        		}
	        	 }
	        $(".wzb-model-4").html($("#downTemplate").render(data));
	         }  
	   });  
		
	})

	</script>
	<div class="xyd-wrapper">
		<div id="main" class="xyd-main-3 clearfix">
		
    </div>

</div> <!-- xyd-wrap End-->

<div class="xyd-wrapper">
<div class="xyd-main clearfix">
    <div class="wzb-list-25">
        <ul id="positionContent">
          
        </ul>
    </div>
    <div class="wzb-model-4" style="margin:0;">
    </div>
</div>
</div> <!-- xyd-wrap End-->
<!-- template start -->
<script id="topTemplate" type="text/x-jsrender">
			 <div class="bg-4">
      <div class="bg-1">
          <div class="number-1" style="float:left;">
              <span class="s-border-1"></span>
              <span class="s-border-2"></span>
              <span class="s-border-3"></span>
              <span class="s-border-4"></span>
              <i class="number-1-1"> {{>count }}  <i class="number-1-2">个</i></i>
          </div>

          <div class="wenzi-1" style="float:left;width:180px;">
            <i class="font-size34"><lb:get key="label_lm.label_core_learning_map_59" /></i>
          </div>

          <div class="bg-2" style="position:relative;">
                 {{if flag }}
                 <div class="bg-2-1" style="cursor:default;" onclick="window.location.href='${ctx}/app/learningmap/detail?id={{:enc_upt_id }}'">
                   <span class="wenzi-7"><lb:get key="label_lm.label_core_learning_map_60" />:</span>
                  <div class="wenzi-2">{{>title }}</div>
                  {{else}}
              <div class="bg-2-1">
                    <div class="wenzi-8"><lb:get key="label_lm.label_core_learning_map_61" /></div>
                  {{/if}}
              </div>
          </div>
      </div>
</script>
<!-- template end -->

<!-- template start -->
<script id="centerTemplate" type="text/x-jsrender">
		 {{if #index==0}}
           <li class="bgcolor-00afef main_img_2" onclick="window.location.href='${ctx}/app/learningmap/detail?id={{:upt_id }}'">
                <img src="${ctx}{{:abs_img}}" alt="">
                <i>
                    <a href="#"> {{:upt_title }}</a>
                </i>
            </li>
        {{/if}}
  {{if #index==1}}
         <li class="bgcolor-a760c3 main_img_2" onclick="window.location.href='${ctx}/app/learningmap/detail?id={{:upt_id }}'">
                <img src="${ctx}{{:abs_img}}" alt="">
                <i>
                    <a href="#"> {{:upt_title }}</a>
                </i>
            </li>
        {{/if}}
  {{if #index==2}}
           <li class="bgcolor-407ce1 main_img_2" style="margin-right:0;" onclick="window.location.href='${ctx}/app/learningmap/detail?id={{:upt_id }}'">
                <img src="${ctx}{{:abs_img}}" alt="">
                <i>
                   <a href="#"> {{:upt_title }}</a>
                </i>
            </li>
        
        {{/if}}
  {{if #index==3 }} 
           <li class="bgcolor-00afef main_img_2" onclick="window.location.href='${ctx}/app/learningmap/detail?id={{:upt_id }}'">
                <img src="${ctx}{{:abs_img}}" alt="">
                <i>
                   <a href="#"> {{:upt_title }}</a>
                </i>
            </li>
        {{/if}}
  {{if #index==4 }}
         <li class="bgcolor-a760c3 main_img_2" onclick="window.location.href='${ctx}/app/learningmap/detail?id={{:upt_id }}'">
                <img src="${ctx}{{:abs_img}}" alt="">
                <i>
                   <a href="#"> {{:upt_title }}</a>
                </i>
            </li>
        {{/if}}
  {{if #index==5 }}
           <li class="bgcolor-407ce1 main_img_2" style="margin-right:0;" onclick="window.location.href='${ctx}/app/learningmap/detail?id={{:upt_id }}'">
                <img src="${ctx}{{:abs_img}}" alt="">
                <i>
                   <a href="#"> {{:upt_title }}</a>
                </i>
            </li>
        {{/if}}
</script>
<!-- template end -->

<!-- template start -->
<script id="downTemplate" type="text/x-jsrender">

 {{if num >#index+1 }} 
   <dl class="wzb-list-9-1 wzb-border-bottom clearfix">
            <dt style="background:#f1f1f1;color:#666;">{{>upc_title }}</dt>
            <dd>
          {{for items}} 
            <a onclick="window.location.href='${ctx}/app/learningmap/detail?id={{:upt_id }}'" href="javascript:void(0);">{{:upt_title }}</a>
             {{/for}}
            </dd>
     </dl>
{{/if}}
  {{if num == #index+1 }} 
 <dl class="wzb-list-9-1 clearfix">
            <dt style="background:#f1f1f1;color:#666;">{{>upc_title }}</dt>
            <dd>
          {{for items}} 
            <a onclick="window.location.href='${ctx}/app/learningmap/detail?id={{:upt_id }}'" href="javascript:void(0);">{{:upt_title }}</a>
             {{/for}}
            </dd>
     </dl>

{{/if}}
</script>
</body>
</html>