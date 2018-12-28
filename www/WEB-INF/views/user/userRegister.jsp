<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../common/meta.datepicker.jsp"></jsp:include>
<title></title>
<style type="text/css">
span.error {
	color: red;
	float: none;
	margin-left: 5px;
}
</style>
<script type="text/javascript">
	$(function(){
		$("input[name='birthday']").datepicker();
		$("input[name='join_datetime']").datepicker();
	})
	
	function submitRegister(){
		if($("input[name='usr_ste_usr_id']").val() == ''){
			Dialog.alert(fetchLabel('error_please_input') + fetchLabel('usr_name'));
			return;
		}
		if($("input[name='usr_ste_usr_id']").val().search(/[^A-Za-z0-9_-]/) != -1 || $("input[name='usr_ste_usr_id']").val().length < ${usrIdMinLength} || $("input[name='usr_ste_usr_id']").val().length > ${usrIdMaxLength}){
			Dialog.alert(fetchLabel('usr_name') + fetchLabel('usr_id_error') + '${usrIdMinLength}-${usrIdMaxLength}');
			return;
		}
		
		
		if($("input[name='usr_ste_usr_id']").children()!= undefined && $("input[name='usr_ste_usr_id']").children().length >0 && $($("input[name='usr_ste_usr_id']").children().get(0)).val() != "")
		{
			Dialog.alert(fetchLabel("error_user_name_exist"));
			return;
		}
		/* if($("input[name='usr_ste_usr_id").val() !='' && !/^(?=.*[a-zA-Z]+)(?=.*[0-9]+)[A-Za-z0-9\-\_]*$/.test($("input[name='usr_ste_usr_id']").val())) {
			Dialog.alert(fetchLabel('usr_name') + fetchLabel('usr_id_error') + '${usrIdMinLength}-${usrIdMaxLength}');
			return;
		} */
		
		if($("input[name='usr_pwd']").val() == ''){
			Dialog.alert(fetchLabel('error_please_input') + fetchLabel('usr_password'));
			return;
		}
		if($("input[name='usr_pwd']").val().length < ${pwdMinLength} || $("input[name='usr_pwd']").val().length > ${pwdMaxLength}){
			Dialog.alert(fetchLabel('usr_password_erre') + '${pwdMinLength}-${pwdMaxLength}');
			return;
		}
		
		if($("input[name='confirm_pwd']").val() == ''){
			Dialog.alert(fetchLabel('error_please_input') + fetchLabel('usr_confirm_password'));
			return;
		}
		if($("input[name='confirm_pwd']").next().html() != ''){
			Dialog.alert(fetchLabel('error_pwd_inconsistent'));
			return;
		}
		
		if($("input[name='usr_pwd']").val() !='' && !/^(?=.*[a-zA-Z]+)(?=.*[0-9]+)[A-Za-z0-9\-\_]*$/.test($("input[name='usr_pwd']").val())) {
			Dialog.alert(fetchLabel('usr_password_erre') + '${pwdMinLength}-${pwdMaxLength}');
			return;
		}
		if($("input[name='usr_display_bil']").val() == ''){
			Dialog.alert(fetchLabel('error_please_input') + fetchLabel('usr_full_name'));
			return;
		}
		if(getChars($("input[name='usr_display_bil']").val()) > 80){
			Dialog.alert(fetchLabel('usr_full_name') + fetchLabel('label_title_length_warn_80'));
			return;
		} 
		
		
		if($("input[name='usr_email']").val() !='' && !/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test($("input[name='usr_email']").val())){
			Dialog.alert(fetchLabel('usr_mail_error'));
			return;
		}
		if($("input[name='usr_tel_1']").val() !='' && !/^((\d{11})|((\d{7,8})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1})|(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1})))$/.test($("input[name='usr_tel_1']").val())){
			Dialog.alert(fetchLabel('usr_tel_error'));
			return;
		}
		var _cur_date = new Date();
		var demo = $("#demo").val();
		var demo1 = $("#demo1").val();
		if(demo != ''){
			var _d_date = new Date(demo.substring(0, 4), Number(demo.substring(5, 7)) - 1, Number(demo.substring(8, 10)))
			if(_d_date >= _cur_date){
				Dialog.alert(wb_msg_usr_enter_vaild + fetchLabel('usr_birthday'));	
	          return;
			}
		}
		if(demo1 != ''){
			var _d_date1 = new Date(demo1.substring(0, 4), Number(demo1.substring(5, 7)) - 1, Number(demo1.substring(8, 10)))
			if(_d_date1 >= _cur_date){
				Dialog.alert(wb_msg_usr_enter_vaild + fetchLabel('usr_join_datetime'));	
	          return;
			}
		}
		
		$("#registerForm").submit();
	}
	
	function checkUserName(){
		if($("input[name='usr_ste_usr_id']").val() != ''){
			$.ajax({
				url : '${ctx}/app/user/checkUserName',
				data : {
					usr_ste_usr_id : $("input[name='usr_ste_usr_id']").val()
				},
				type : 'POST',
				dataType : 'json',
				success : function(data){
					if(data.message){
						
						var input = "<input type='hidden' value='" + fetchLabel("error_user_name_exist") +"'>'</input>";
						
						$("input[name='usr_ste_usr_id']").empty();
						
						
						
						$("input[name='usr_ste_usr_id']").append(input);
					} else {
						var input = "<input type='hidden' />" ;
						$("input[name='usr_ste_usr_id']").empty();
						$("input[name='usr_ste_usr_id']").append(input)
					}
				}
			})
		}
	}
	
	function checkPassword(){
		if($("input[name='usr_pwd']").val() != $("input[name='confirm_pwd']").val()){
			$("input[name='confirm_pwd']").next().html(fetchLabel("error_pwd_inconsistent"));
		} else {
			$("input[name='confirm_pwd']").next().html('');
		}
	}
</script>
</head>
<body>
	<div class="xyd-wrapper">
		<div class="xyd-whole">
			<div class="xyd-whole-title skin-bg">
				<lb:get key="login_new_user_register" />
				<i class="fa fa-sanzuo fa-caret-up"></i>
			</div>

			<div class="xyd-whole-main">
				<div class="xyd-whole-top">
					<span class="pull-left"><lb:get key="login_register_attention_matters" /></span> 
					<span class="pull-right"><lb:get key="login_haved_account" />
						<em class="fbold f16">
						<a class="wzb-link04" href="${ctx}/app/user/userLogin/$?lang=${lang}"  target="_blank">
							<i class="fa f18 mr5 fa-user"></i> 
							<lb:get key="login" />
						</a>
						</em>
					</span>
				</div>
				<!-- xyd-whole-top End-->

				<form name="wyd-form" id="registerForm" action = "${ctx}/app/user/userRegister/register" method = "post">
					<div class="clearfix">
						<div class="xyd-whole-left">
							<div class="wzb-title-2">
								<lb:get key="usr_detail" />
							</div>
							<table class="margin-top15">
								<tr>
									<td class="wzb-form-label"><span class="wzb-form-star">*</span><lb:get key="usr_name" />：
									</td>

									<td class="wzb-form-control"><input type="text"
										class="form-control wzb-text-05" name="usr_ste_usr_id"
										onblur="checkUserName()" maxlength="${usrIdMaxLength}"
										value=""></td>
								</tr>

								<tr>
									<td class="wzb-form-label"><span class="wzb-form-star">*</span><lb:get key="usr_password" />：
									</td>

									<td class="wzb-form-control"><input type="password"
										class="form-control wzb-text-05" name="usr_pwd"
										maxlength="${pwdMaxLength}" value=""></td>
								</tr>

								<tr>
									<td class="wzb-form-label"><span class="wzb-form-star">*</span><lb:get
											key="usr_confirm_password" />：
									</td>

									<td class="wzb-form-control"><input type="password"
										class="form-control wzb-text-05" name="confirm_pwd"
										onblur="checkPassword()" maxlength="${pwdMaxLength}"><span
										class="error"></span></td>
								</tr>

								<tr>
									<td class="wzb-form-label"><span class="wzb-form-star">*</span><lb:get key="usr_full_name" />：
									</td>

									<td class="wzb-form-control"><input type="text"
										class="form-control wzb-text-05" name="usr_display_bil"
										value=""></td>
								</tr>

								<tr>
									<td class="wzb-form-label"><lb:get key="usr_gender" />：</td>
									<td class="wzb-form-control"><select
										style="width:245px;height:25px;color:#999;border:1px solid #dfdfdf;" name="usr_gender"><option
												value=""><lb:get key="usr_gender_null" /></option>
											<option value="M"><lb:get key="usr_gender_M" /></option>
											<option value="F"><lb:get key="usr_gender_F" /></option></select></td>
								</tr>

								<tr>
									<td class="wzb-form-label"><span class="wzb-form-star"></span><lb:get key="usr_birthday" />：
									</td>

									<td class="wzb-form-control"><input type="text" id="demo"
										class="laydate-icon pertxt Wdate wzb-text-05" name="birthday"
										readonly="readonly" /></td>
								</tr>

								<tr>
									<td class="wzb-form-label"><lb:get key="usr_join_datetime" />：</td>

									<td class="wzb-form-control"><input type="text" id="demo1"
										class="laydate-icon pertxt Wdate wzb-text-05" name="join_datetime"
										readonly="readonly" value=""></td>
								</tr>
							</table>
						</div>


						<div class="xyd-whole-right">
							<div class="wzb-title-2">
								<lb:get key="usr_contact_information" />
							</div>
							<table class="margin-top15">
								<tr>
									<td class="wzb-form-label"><lb:get key="usr_mail" />：</td>

									<td class="wzb-form-control"><input type="text"
										class="form-control wzb-text-05" name="usr_email" value="">
									</td>
								</tr>

								<tr>
									<td class="wzb-form-label"><lb:get key="usr_tel" />：</td>

									<td class="wzb-form-control"><input type="text"
										class="form-control wzb-text-05" name="usr_tel_1" value="">
									</td>
								</tr>

								<tr>
									<td class="wzb-form-label"><lb:get key="usr_qq" />：</td>

									<td class="wzb-form-control"><input type="text"
										class="form-control wzb-text-05" name="userExt.urx_extra_41"
										value=""></td>
								</tr>
							</table>
						</div>
					</div>

					<div class="wzb-bar">
						<input type="button"
							class="btn wzb-btn-yellow wzb-btn-big margin-right15"
							value='<lb:get key="button_ok"/>' onclick="submitRegister()">
						<input type="button" class="btn wzb-btn-yellow wzb-btn-big"
							value='<lb:get key="button_cancel"/>'
							onclick="javascript:window.location.href='${ctx}/app/user/userLogin/$?lang=${lang}'">
					</div>
				</form>

			</div>
			<!-- xyd-whole-main End-->

		</div>
	</div>
	<!-- xyd-wrap End-->
	
<!-- 日期控件 start-->
<script>
!function(){
laydate.skin('molv');//切换皮肤，请查看skins下面皮肤库
laydate({elem: '#demo'});//绑定元素
}();
//日期范围限制
var start = {
    elem: '#start',
    format: 'YYYY-MM-DD',
    min: laydate.now(), //设定最小日期为当前日期
    max: '2099-06-16', //最大日期
    istime: true,
    istoday: false,
    choose: function(datas){
         end.min = datas; //开始日选好后，重置结束日的最小日期
         end.start = datas //将结束日的初始值设定为开始日
    }
};
var end = {
    elem: '#end',
    format: 'YYYY-MM-DD',
    min: laydate.now(),
    max: '2099-06-16',
    istime: true,
    istoday: false,
    choose: function(datas){
        start.max = datas; //结束日选好后，充值开始日的最大日期
    }
};
laydate(start);
laydate(end);
//自定义日期格式
laydate({
    elem: '#test1',
    format: 'YYYY年MM月DD日',
    festival: true, //显示节日
    choose: function(datas){ //选择日期完毕的回调
        alert('得到：'+datas);
    }
});
//日期范围限定在昨天到明天
laydate({
    elem: '#hello3',
    min: laydate.now(-1), //-1代表昨天，-2代表前天，以此类推
    max: laydate.now(+1) //+1代表明天，+2代表后天，以此类推
});

window.onload=function(){
    var aqing=document.getElementById("qing")
    //console.log(aqing)
    var body2 = document.getElementById("by");
    

}
</script>
<!-- 日期控件 end-->
</body>
</html>