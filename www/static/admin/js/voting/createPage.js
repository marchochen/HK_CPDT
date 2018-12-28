$(function(){
	optionLength = $("input[name='options']").length;
	if(optionLength < 3){
		for(var i= $("input[name='options']").length;i<3;i++){
			$("#optionContainer").append($("#option-template").render());
			optionLength++;
		}
	}

	$("#creatOptionBtn").unbind("click");
	$("#creatOptionBtn").bind("click",function(){
		if(optionLength == 10){
			Dialog.alert(cwn.getLabel('label_core_requirements_management_25'));
			return;
		}
		$("#optionContainer").append($("#option-template").render());
		optionLength++;
	});
	
    $('#tc-selector-single').wzbSelector({
        type : 'single',
        ignoreRootNode : true,
        tree : {
            enable : true,
            type : 'single',
            setting : {
                async : {
                    enable : true,
                    autoParam : [ "id" ],
                    url : contextPath+'/app/tree/tcListJson/withHead'
                },data : {
                    simpleData : {
                        enable : true
                    }
                }
            }
        },
        message : {
            title : fetchLabel('label_core_requirements_management_43')
        }
    });
    
    
    /*
     * 设置开始和结束时间的值
     */
    var setTime = function(source,field){
    	if(source){
        	var year = source.getFullYear();
        	var month = source.getMonth() + 1;
        	var date = source.getDate();
        	eval("document.votingForm."+field+"_yy.value = year");
        	eval("document.votingForm."+field+"_mm.value = month");
        	eval("document.votingForm."+field+"_dd.value = date");
        }
    };
    
    setTime(vot_eff_date_from,"start");
    setTime(vot_eff_date_to,"end");
    
    /**
     * 获取日期控制的日期
     */
    var getTime = function(field){
    	var year, month, date;
    	eval("year = document.votingForm."+field+"_yy.value");
    	eval("month = document.votingForm."+field+"_mm.value");
    	eval("date = document.votingForm."+field+"_dd.value");
    	if(year && month && date){
    		return year+"-"+month+"-"+date;
    	}
    	return "";
    };
    
    $("#votingForm").validate({
    	submitHandler: function(form){
     		
    		if(!$.trim($("#vot_title").val())){
    			Dialog.alert(cwn.getLabel('label_core_requirements_management_44'));
    			return;
    		}
    		if(getChars($("#vot_title").val())>80)
    		{
    			Dialog.alert(cwn.getLabel('label_core_requirements_management_55'));
    			return;
    		}
    		if(!$.trim($("#vot_content").val())){
    			Dialog.alert(cwn.getLabel('label_core_requirements_management_19'));
    			return;
    		}else{
    			var tit=$.trim($("#vot_content").val().replace(/&nbsp;/ig,''));
    			var tet = KindEditor.create("textarea[name='vot_content']");
    			var t_text=tet.text();
    			if(tit==''||t_text=='')
    			{
    				Dialog.alert(cwn.getLabel('label_core_requirements_management_19'));
        			return;
    			}else{
    				$("#error-content").hide();
    			}
    			if(getChars(t_text)>2000)
        		{
    				Dialog.alert(cwn.getLabel('label_core_requirements_management_65'));
        			return;
        		}
    		}
    		var optionCount = 0;
    		var input_option = $("input[name='options']");
    		for(var i=0;i<input_option.length;i++){
    			if($.trim($(input_option[i]).val()) != ""){
    				optionCount += 1;
    				if(getChars($(input_option[i]).val())>200){
    					Dialog.alert(cwn.getLabel('label_core_requirements_management_56'));
    					return;
    					//$(input_option[i]).parent().children().last().show();
    				}else{
    					$(input_option[i]).parent().children().last().hide();
    				}
    			}
    		}
    		if(optionCount<2){
    			Dialog.alert(cwn.getLabel('label_core_requirements_management_23'));
    			return;
    		}else{
    			$("#error-option").hide();
    		}
    		
    		if(!$.trim($("#tc-selector-single").val())){
    			Dialog.alert(cwn.getLabel('label_core_requirements_management_28'));
    			return;
    		}else {
    			$("#error-tcr").hide();
    		}
    		
    		var start = getTime("start");
    		$("#vot_eff_date_from").val(start);
    		if(!$.trim(start)){
    			Dialog.alert(cwn.getLabel('label_core_requirements_management_36'));
    			return;
    		}
    		if(!wbUtilsValidateDate("document.votingForm" + ".start",fetchLabel("label_core_requirements_management_32"))){
    			return false;
    		}
    		
    		var end = getTime("end");
    		$("#vot_eff_date_to").val(end);
    		if(!$.trim(end)){
    			Dialog.alert(cwn.getLabel('label_core_requirements_management_37'));
    			return;
    		}
    		if(!wbUtilsValidateDate("document.votingForm" + ".end",fetchLabel("label_core_requirements_management_32"))){
    			return false;
    		}
    		
    		if(new Date(start).getTime() > new Date(end).getTime()){
    			Dialog.alert(getLabel("674"));
    			return;
    		}
    		
    		form.submit();   //提交表单   
        }
    });
});

function delOption(obj){
	$(obj).parent('p').remove();
	optionLength--;
}