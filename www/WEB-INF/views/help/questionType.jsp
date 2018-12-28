<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>

<script type="text/javascript">
	
	function questionTypeSave() {
		if($('#questionTypeForm').valid()){
			var url = '${ctx}/app/help/questionType/'+questionTypeAct;
			if(questionTypeAct=='update'){
				url = url+'/'+$(".kuang #hqt_id").val();
			}
			$.ajax({  
		        type:'post',      
			    url: url,  
			    data:{
			    	hqt_id : $(".kuang #hqt_id").val(),
			    	hqt_type_name : $('.kuang #hqt_type_name').val() ,
			    	hqt_top_index : $('.kuang #hqt_top_index').val() 
			    },  
			    cache:false,  
			    dataType:'json',  
			    success:function(data){  
			    	closeQuestionTypeForm();
			    	 window.location = "${ctx}/app/help";
			       }  
			    });  
		}
		
	}
	
	function questionTypeDel(id){
	   if(confirm("确认要删除？")){ 
		  $.ajax({  
		        type:'delete',      
			    url: '${ctx}/app/help/questionType/'+$(".kuang #hqt_id").val(),
			    cache:false,  
			    dataType:'json',  
			    success:function(data){  
			    	if(data.success==true){
				    	closeQuestionTypeForm();
				    	 window.location = "${ctx}/app/help";
			    	}
			    }  
			});  
	   }
	}
	
</script>

<div class="kuang wzb-qtip">
    <div class="qtip-titlebar">
        <div class="qtip-title" aria-atomic="true" id="question_type_form_title">修改分类</div>
    </div>
    <form id="questionTypeForm">
    <div class="kuang-nei">
    	<input type="hidden" id="hqt_id">
        <table class="wzb-table-three ">
            <tbody>
                <tr>
                    <td class="wzb-table-title" style="padding-bottom: 40px;"><lb:get key='label_cm.label_core_community_management_206'/>：</td>
                    <td class="wzb-table-content">
                        <div class="wzb-selector"><input type="text" name="hqt_type_name" id="hqt_type_name" class="form-control required">
                        	
                      	</div>
                      	<label class="error" id="error_hqt_type_name" style="" for='hqt_type_name'></label>
                    </td>
                </tr>

                <tr>
                    <td class="wzb-table-title" style="padding-bottom: 40px;"><lb:get key='label_cm.label_core_community_management_197'/>：</td>

                    <td class="wzb-table-content ">
                        <div class="wzb-selector zhiding">
                            <input type="text" value="" name="hqt_top_index" id="hqt_top_index" class="form-control ">
                            <span>(<lb:get key='label_cm.label_core_community_management_200'/>)</span>
                        </div>
                         <label class="error" id="error_hqt_top_index" style="" for='hqt_top_index'></label>
                    </td>
                </tr>
                
               <%--  <tr>
                	 <td class="wzb-table-title"><lb:get key="label_cm.label_core_community_management_199" />：</td>
                	 <td  class="wzb-table-content ">
                	  	<div class="wzb-selector">
             	 			<select class="wzb-select" name="hqt_language"
								style="margin-right: 80px;" id="hqt_language">
								<option value="zh-cn" selected="selected">
									<lb:get key="label_cm.label_core_community_management_207" />
								</option>
								<option value="en-us" >
									<lb:get key="label_cm.label_core_community_management_196" />
								</option>
								<option value="zh-hk">
									<lb:get key="label_cm.label_core_community_management_195" />
								</option>
							</select> 
						</div>
                	 </td>
                </tr> --%>
                
                <%-- <tr>
                	 <td class="wzb-table-title"><lb:get key='label_cm.label_core_community_management_198'/>：</td>
                	 <td  class="wzb-table-content ">
                	  	<div class="wzb-selector">
							<select class="wzb-select" name="hqt_is_publish"
								style="margin-right: 80px;" id="hqt_is_publish">
								<option value="1" selected="selected">
									<lb:get key="status_yes" />
								</option>
								<option value="0">
									<lb:get key="status_no" />
								</option>
							</select> 
						</div>
                	 </td>
                </tr> --%>
            </tbody>
        </table>
        <div class="wzb-bar">
            <a  class="btn wzb-btn-blue margin-right10 wzb-btn-big" onclick="questionTypeSave()"><lb:get key='global.button_ok'/></a>
            <a  class="btn wzb-btn-blue margin-right10 wzb-btn-big" onclick="closeQuestionTypeForm()"><lb:get key='global.button_cancel'/></a>
            <a  class="btn wzb-btn-blue margin-right10 wzb-btn-big" style="display: none;" onclick="questionTypeDel()" id='del_question_type_btn' ><lb:get key='global.button_del'/></a>
        </div>
    </div>
    </form>
</div>
