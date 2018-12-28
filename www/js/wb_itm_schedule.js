//   public functions : use "wbSchedule" prefix 
//   private functions: use "_wbSchedule" prefix

/* constructor */
function wbSchedule()
{
	this.get_calendar=wbScheduleGetSchedule;
	this.export_schedule=wbScheduleExportSchedule;
}
				
				
function wbScheduleGetSchedule(frm,lang)
{	
	txtFldName = wb_msg_calendar_year ;
	if(!gen_validate_positive_integer(frm.year,txtFldName,lang)) return;
	if(frm.year.value.length !=4 || Number(frm.year.value) < 1800){
		alert(wb_msg_usr_enter_valid_year_1 + wb_msg_calendar_year+ wb_msg_usr_enter_valid_year_2)
		frm.year.focus();
		return;
	}
	var frm = document.frmAction
	var month =frm.month.options[frm.month.selectedIndex].value;
	var year = frm.year.value;
	url =wb_utils_invoke_ae_servlet('cmd','get_itm_sch','stylesheet','ae_schedule.xsl','year',year,'month',month);
	window.location.href = url;
}
function wbScheduleExportSchedule(frm,year,month)
{
		var frm = document.frmAction
		url =wb_utils_invoke_ae_servlet('cmd','get_itm_sch','stylesheet','ae_export_schedule.xsl','year',year,'month',month);
		window.location.href = url;
}