		var dt,  itmParams;
		var encrytor = new wbEncrytor();
		$(function(){
			  dt = $("#main").table({
			        url : contextPath + '/app/learningmap/specialListJson',
			        gridTemplate : function(data){
			          data.ust_id = encrytor.cwnEncrypt(data.ust_id);
					  return $('#centerTemplate').render(data);
					},
			        userView : true,
			        view : 'grid',
			        rp : 6,
					rowSize : 3,
			        hideHeader : false,
					sortname : 'ust_update_time',
					sortorder : 'desc',
			        usepager : true,
			        params : itmParams,
			        onSuccess : function(data){
			        }

			    }); 

	   });  
