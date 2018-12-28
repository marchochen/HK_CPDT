// ------------------ wizBank Navigation Center object ------------------- 
// Convention:
//   public functions : use "wbNavigate" prefix 
//   private functions: use "_wbNavigate" prefix
// Dependency:
//   gen_utils.js
//   wb_utils.js
// ------------------------------------------------------------ 

/* constructor */
function wbNavigate() {	
	this.get_nav_lst = wbNavigateGetNavLstURL
}

function wbNavigateGetNavLstURL(num_of_mod,id){
		url = wb_utils_invoke_servlet('cmd','get_last_visit_mod','stylesheet','lrn_nav_center.xsl')		
		if (num_of_mod != null)
		url+= '&num_of_mod=' + num_of_mod;
		if(id != null)
		url+= '&course_id=' + id;
		return url
}