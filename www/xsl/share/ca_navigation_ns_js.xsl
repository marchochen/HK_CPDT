<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:import href="../utils/wb_gen_img_button.xsl"/>
<xsl:import href="../utils/wb_gen_form_button.xsl"/>
<xsl:import href="../utils/escape_js.xsl"/>
<xsl:import href="../utils/wb_css.xsl"/>
<xsl:variable name="img_path"><xsl:value-of select="$wb_img_path"/></xsl:variable>
<xsl:variable name="itm_type"><xsl:value-of select="//course/@type"/></xsl:variable>

	<xsl:template name="tree_js">
		<xsl:param name="srcid"/>
		<xsl:param name="title"/>
		<xsl:param name="identifier"/>

		<xsl:param name="lab_add_folder_tooltip"/>
		<xsl:param name="lab_add_module_tooltip"/>
		<xsl:param name="lab_add_test_tooltip"/>
		<xsl:param name="lab_add_vido_tooltip"/>
		<xsl:param name="lab_edit_node_tooltip"/>
		<xsl:param name="lab_remove_node_tooltip"/>
		<xsl:param name="lab_reorder_node_tooltip"/>
		<xsl:param name="lab_move_up_node_tooltip"/>
		<xsl:param name="lab_move_down_node_tooltip"/>
		<xsl:param name="lab_save_reorder_tooltip"/>
		<xsl:param name="lab_new_node_default_label"/>
		<xsl:param name="lab_loading_applet"/>
		<xsl:param name="for_scorm">0</xsl:param>
		<xsl:variable name="isEnrollment_related">
			<xsl:choose>
				<xsl:when test="not (/course/header/enrollment_related)">false</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="/course/header/enrollment_related"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>				
		<head>
		<script>
					function sel(){//滚动条兼容写法
						var div = document.getElementById("tree_div")
						div.style.height=$(window).height()-40+"px"; 	
					}
				window.onload=function(){
					sel()			
				window.onresize=function(){
					sel()
					}
				}
			</script>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<xsl:if test="$for_scorm = 0">
				<xsl:call-template name="wb_css">
					<xsl:with-param name="view">wb_ui</xsl:with-param>
				</xsl:call-template>
			</xsl:if>
			<script language="JavaScript" src="{$wb_js_path}wb_module.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_course.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}gen_utils.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_utils.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}urlparam.js" type="text/javascript"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_module.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}xtree.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}xmlextras.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}xloadtree.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wizbTree.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}jquery.js"/>
			<script type="text/javascript" src="../../static/js/i18n/{$wb_cur_lang}/label_tm_{$wb_cur_lang}.js" language="JavaScript"/>
			<link rel="stylesheet" href="../static/css/base.css"/>
			<link type="text/css" rel="stylesheet" href="{$img_path}tree/css/xtree.css"/>
			<link type="text/css" rel="stylesheet" href="{$img_path}tree/css/xtree2.links.css"/>
			
			<!--alert样式  -->
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}jquery.js"/>
			
			<link rel="stylesheet" href="../static/js/jquery.qtip/jquery.qtip.css" />
			<script type="text/javascript" src="../static/js/jquery.dialogue.js"></script>
			<script type="text/javascript" src="../static/js/jquery.qtip/jquery.qtip.js"></script>
			
			<script language="Javascript" type="text/javascript" src="../../static/js/cwn_utils.js"/>
			<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/global_{$wb_cur_lang}.js"></script>
			
			<script language="JavaScript" type="text/javascript"><![CDATA[
		parent.course_timestamp = ']]><xsl:value-of select="/course/@timestamp"/><![CDATA[';
		var module_lst = new wbModule;
		var new_node_id = "";
		var last_modify_node;
		var temp_title = "$mod_title_";
		var temp_id = "$mod_id_";
		var temp_type = "$mod_type_";
		
		webFXTreeConfig.setSkinImage("]]><xsl:value-of select="$img_path"/><![CDATA[tree/images/default");
		
		var src_url = self.location.href + "&create_tree=y";
		var jstree = new wizbTree("",src_url);
			jstree.setText("]]><xsl:call-template name="escape_js"><xsl:with-param name="input_str" select="/course/header/title/text()"/></xsl:call-template><![CDATA[");
			jstree.setSrcId(]]><xsl:value-of select="$srcid"/><![CDATA[);
			jstree.setIcon("]]><xsl:value-of select="$img_path"/><![CDATA[root.gif");
			jstree.setOpenIcon("]]><xsl:value-of select="$img_path"/><![CDATA[root.gif");
			]]><xsl:if test="$for_scorm = 0"><![CDATA[
			jstree.setAction("javascript:parent.NavPage.root_action()");
			]]></xsl:if><![CDATA[
			jstree.setTarget("ContentPage");
			]]><xsl:if test="$for_scorm = 1"><![CDATA[
			jstree.setExpanded(true);
			]]></xsl:if><![CDATA[
			//jstree.expandAll();
			]]><xsl:if test="$for_scorm = 0"><![CDATA[
			jstree.dynamicExpandAll("jstree");
		   ]]></xsl:if><![CDATA[
				
		function root_action(){
			btnControl();
			if(!jstree_exist_new_node()){
				parent.show_frame_content(parent.course_info_url);
				//parent.NavPage.show_course();
			}
		}
			
		function page_onload(){
			//var div_width = document.body.clientWidth;
			var div_height = document.body.clientHeight;
			var obj_div = document.getElementById("tree_div");
			//obj_div.style.width = div_width+'px';
			obj_div.style.height = div_height - 28 +'px';
			obj_div.innerHTML=jstree.toHtml();
			btnControl();
		}				
			
			
		function jstree_exist_new_node(){
			if(new_node_id == ''){
				return false;
			}else{
				var node = webFXTreeHandler.getNodeById(new_node_id);
				jstree.setSelected(node);
				//parent.show_frame_content(node.getAction());
				return true;
			}			
		}
		
		function jstree_addNode(type,url){
			//alert("old:"+wizbTree.getMaxItemIndex());
			//alert(wizbTree.getMaxItemIndex()+1);
			wizbTree._maxitemindex = (wizbTree.getMaxItemIndex()+1);
			//alert("new:"+wizbTree.getMaxItemIndex());
			var node = jstree.getSelected();
			if(node==null){
				node = jstree.getTree();
			}
			if(node.attributes && (node.attributes["is_folder"] != "YES")){
				node = node.getParent();
			}
			var new_node = new wizbTreeItem("<font color='red'>]]><xsl:value-of select="$lab_new_node_default_label"/><![CDATA[</font>");
			new_node.attributes = {};
			if(type=='folder'){
				new_node.attributes["is_folder"] = "YES";
				new_node.setIcon(webFXTreeConfig.folderIcon);
				new_node.setOpenIcon(webFXTreeConfig.folderIcon);
				new_node.attributes["itemtype"] = "FDR";
				new_node.attributes["identifier"] = "ITEM"+(wizbTree.getMaxItemIndex());
			}else {
				new_node.attributes["is_folder"] = "NO";
				new_node.setSrcId(temp_id);
				new_node.attributes["title"] = temp_title;
				new_node.attributes["identifier"] = "ITEM"+(wizbTree.getMaxItemIndex());
				new_node.attributes["itemtype"] = "MOD";
				new_node.attributes["restype"] = temp_type;
			}
			new_node.setAction(url);
			new_node.setTarget("ContentPage");
			
			node.add(new_node);
			node.setExpanded(true);
			jstree.setSelected(new_node);
			new_node_id = new_node.getId();
		}	
			
			
		function jstree_cancelAdd(){
			var node = webFXTreeHandler.getNodeById(new_node_id);
			if(node != null){
				node.getParent().remove(node);
			}
			jstree.setSelected(jstree);
			wizbTree._maxitemindex = wizbTree.getMaxItemIndex()-1;
			new_node_id = "";
			btnControl();
		}
		
		function jstree_editNode(){
			var node = jstree.getSelected();
			if(node==null || node==jstree.getTree()){
				return false;
			}
			if(node.attributes){
				if( node.attributes["is_folder"]=="YES"){
					folder_edit_mode(node.getText());
				}else{
					mod_edit_mode(node.getSrcId(),parent.cos_id, parent.course_timestamp, 1, jstree.toXML());
				}
			}else{
				
			}
		}
		

		function jstree_addNode_commit(node_title, mod_type, mod_id, course_timestamp){
			var node = webFXTreeHandler.getNodeById(new_node_id);
			last_modify_node = node;
			node.setSrcId(mod_id);
			node.setText(node_title);
			node.attributes["title"] = node_title;
			node.attributes["restype"] = mod_type;
			node.setAction("javascript:parent.NavPage.mod_read_mode('"+mod_id+"');");
			var mod_img = getModuleImage("]]><xsl:value-of select="$img_path"/><![CDATA[",mod_type);
			node.setIcon(mod_img);
			node.setOpenIcon(mod_img);
			set_cos_timestamp(course_timestamp);
			new_node_id = "";
			btnControl();
		}
		
		function jstree_editNode_commit(node_title, course_timestamp){	
			//alert("jstree_editNode_commit");
			var node = jstree.getSelected();
			last_modify_node = jstree.getSelected();
			node.setText(node_title);
			node.attributes["title"]=node_title;
			node.setTarget("ContentPage");
			var str_action = "";
			if( node.attributes["is_folder"]=="YES"){
				str_action = "javascript:parent.NavPage.folder_read_mode('" + node_title + "');";
				node.setAction(str_action);
				saveCourseStructXML(parent.cos_id, parent.course_timestamp, 1, jstree.toXML());
			}else {
				str_action = "javascript:parent.NavPage.mod_read_mode('" + node.getSrcId() + "');";
				node.setAction(str_action);
			}
			////alert("course_timestamp"+course_timestamp+"\nparent.course_timestamp:"+parent.course_timestamp);
			new_node_id = "";
			if(course_timestamp != null) {
				set_cos_timestamp(course_timestamp);
			}
			btnControl();
			//get_cos_timestamp();
		}
		
		function jstree_node_commit(){
			//parent.course_timestamp = course_timestamp;
			
			////alert("course_timestamp"+course_timestamp+"\nparent.course_timestamp:"+parent.course_timestamp);
		}
		
		function jstree_node_rollback(){
			//alert("jstree_node_rollback");
			jstree.reload();
			/*
			var node = jstree.getSelected();
			if(node.getId() == last_modify_node.getId()){
				node.setText(last_modify_node.getText());
				node.setSrcId(last_modify_node.getSrcId());
				node.setAction(last_modify_node.getAction());
				node.update();
			}else{
				node.getParent().remove();
			}
			*/
		}				
			
			
		function move_node_up(){
			var node = jstree.getSelected();
			if(node==null){
				node = jstree.getTree();
			}
			node.moveNodeUp();
		}
		
		function move_node_down(){
			var node = jstree.getSelected();
			if(node==null){
				node = jstree.getTree();
			}
			node.moveNodeDown();			
		}
		
		function reorder_module(){
			if(jstree_exist_new_node()){
				return false;
			}		
			document.getElementById("operation_mod").style.display  = "none";
			document.getElementById("reorder_mod").style.display  = "";
			
		}
		
		function reorder_confirm(){
			saveCourseStructXML(parent.cos_id, parent.course_timestamp, 1, jstree.toXML());
			reorder_reset(false);
		}	
		
		function reorder_reset(isReload){
			if(isReload){
				jstree.reload();
			}
			document.getElementById("operation_mod").style.display  = "";
			document.getElementById("reorder_mod").style.display  = "none";		
		}	
			
	
			// used for delete module
			mod_id = 0;
			mod_status = '';
			mod_timestamp = '';
			mod_attempted = '';
			mod_lang = '';
			//
			
			course_lst = new wbCourse
			mod = new wbModule
			cos_id = getUrlParam('course_id');
		
			// from Applet to JS
			function show_course() {
				//alert("show course content:" + cos_id);	
				url = course_lst.view_info_url(cos_id);
				parent.show_frame_content(url);
			//	document.CourseAuthoring.cancelNavLock();
			}
	
			function add_folder() {
				//alert("add folder");
				url = wb_utils_invoke_servlet('cmd','get_prof','stylesheet','ca_ins_folder.xsl');
				if(jstree_exist_new_node()){
					return false;
				}
				jstree_addNode("folder",url);
				parent.show_frame_content(url);
				
			}
			
			//for VIDEO course
			function getlevel(node){
				var level = 0;
			    lay = getFolderlevel(node, 0);
			    for(i = lay; i> 0; i-- ){
			   		level ++;
			    }
			   return level;
			}
			function getFolderlevel(node, lay){
				if(node==null || node == undefined){
					return lay;
				}
				var parent_node = node.getParent() ;
				if(parent_node != null){
					lay = lay+1;
					return getFolderlevel(parent_node, lay)
				}
				return lay;
			}
			
			function btnControl(){
				var itm_type = ']]><xsl:value-of select="$itm_type"/><![CDATA[';
				if(itm_type == 'AUDIOVIDEO'){
					document.getElementById("add_module").style.display = "none";
					var node = jstree.getSelected();
					if(node==null){
						node = jstree.getTree();
					}
					level = getlevel(node);
				
						document.getElementById("add_folder").style.display = "none";
					//	document.getElementById("add_test").style.display = "";
						document.getElementById("add_video").style.display = "";
						
						
					
				}else{
					//if(document.getElementById("add_test")!=undefined)
					//document.getElementById("add_test").style.display = "none";
					if(document.getElementById("add_video")!=undefined)
					document.getElementById("add_video").style.display = "none";
				}	
			}
	
			function folder_read_mode(name) {			
				//alert("Read folder with name:" + name);
				btnControl();
				if(!jstree_exist_new_node()){				
					wb_utils_set_cookie('folder_title',name);
					url = wb_utils_invoke_servlet('cmd','get_prof','stylesheet','ca_view_folder.xsl')
					parent.show_frame_content(url);
				//	document.CourseAuthoring.cancelNavLock();
				}
			}
	
			function folder_edit_mode(name) {
				//alert("Edit folder with name:" + name);
				btnControl();
				if(!jstree_exist_new_node()){			
					wb_utils_set_cookie('folder_title',name);
					url = wb_utils_invoke_servlet('cmd','get_prof','stylesheet','ca_edit_folder.xsl')
					parent.show_frame_content(url);
				}
			}
	
			//function delete_module() {
			//	//alert("delete module");
			//	window.parent.deleteModule();
			//}
			
			function add_module(in_course_id, in_course_timestamp, in_course_struct_xml_cnt, in_course_struct_xml_1) {
				if(jstree_exist_new_node()){
					return false;
				}
				url = course_lst.ins_mod_select_url(cos_id);
				jstree_addNode("mode",url);
								
				in_course_id = parent.cos_id;
				in_course_timestamp = parent.course_timestamp;
				in_course_struct_xml_cnt = 1;
				in_course_struct_xml_1 = jstree.toXML();
				//alert("add module"+"\nin_course_id:"+in_course_id + "\n"+"in_course_timestamp:"+in_course_timestamp+ "\n"+"in_course_struct_xml_cnt:"+in_course_struct_xml_cnt+"\n"+"in_course_struct_xml_1:"+in_course_struct_xml_1);
				
				parent.add_module(url, in_course_id, in_course_timestamp, in_course_struct_xml_cnt, in_course_struct_xml_1);
			//	parent.show_frame_content(url);
			}
			
			function add_test(in_course_id, in_course_timestamp, in_course_struct_xml_cnt, in_course_struct_xml_1) {
				if(jstree_exist_new_node()){
					return false;
				}
				url_ = course_lst.ins_mod_select_url(cos_id);
				jstree_addNode("mode",url_);
				
				url = wb_utils_invoke_servlet('cmd', 'get_cos', 'course_id', cos_id, 'stylesheet', 'ins_mod_test_prep.xsl');
								
				in_course_id = parent.cos_id;
				in_course_timestamp = parent.course_timestamp;
				in_course_struct_xml_cnt = 1;
				in_course_struct_xml_1 = jstree.toXML();
				//alert("add module"+"\nin_course_id:"+in_course_id + "\n"+"in_course_timestamp:"+in_course_timestamp+ "\n"+"in_course_struct_xml_cnt:"+in_course_struct_xml_cnt+"\n"+"in_course_struct_xml_1:"+in_course_struct_xml_1);
				
				parent.add_module(url, in_course_id, in_course_timestamp, in_course_struct_xml_cnt, in_course_struct_xml_1);
			//	parent.show_frame_content(url);
			}
			function add_vido(in_course_id, in_course_timestamp, in_course_struct_xml_cnt, in_course_struct_xml_1) {
				if(jstree_exist_new_node()){
					return false;
				}
				//url_ = course_lst.ins_mod_select_url(cos_id);
				
				
				url = wb_utils_invoke_servlet('cmd', 'get_tpl', 'tpl_type', 'VOD', 'tpl_subtype', 'VOD', 'course_id', cos_id, 'dpo_view', 'IST_EDIT', 'stylesheet', 'tst_ins.xsl');;				
				jstree_addNode("mode",url);
				in_course_id = parent.cos_id;
				in_course_timestamp = parent.course_timestamp;
				in_course_struct_xml_cnt = 1;
				in_course_struct_xml_1 = jstree.toXML();
				//alert("add module"+"\nin_course_id:"+in_course_id + "\n"+"in_course_timestamp:"+in_course_timestamp+ "\n"+"in_course_struct_xml_cnt:"+in_course_struct_xml_cnt+"\n"+"in_course_struct_xml_1:"+in_course_struct_xml_1);
				
				parent.add_module(url, in_course_id, in_course_timestamp, in_course_struct_xml_cnt, in_course_struct_xml_1);
			//	parent.show_frame_content(url);
			}
	
			function mod_read_mode(mod_id) {
				btnControl();
				//alert("Read Mode of Mod ID:" + mod_id);
				if(!jstree_exist_new_node()){
					url = mod.view_info_url(mod_id)
					parent.show_frame_content(url);
				//	document.CourseAuthoring.cancelNavLock();
				}
			}
	
			function mod_edit_mode(mod_id, in_course_id, in_course_timestamp, in_course_struct_xml_cnt, in_course_struct_xml_1) {
				//alert("Edit Mode of Mod ID:" + mod_id +"\n"+"in_course_id:"+in_course_id + "\n"+"in_course_timestamp:"+in_course_timestamp+ "\n"+"in_course_struct_xml_cnt:"+in_course_struct_xml_cnt+"\n"+"in_course_struct_xml_1:"+in_course_struct_xml_1);
				btnControl();
				if(!jstree_exist_new_node()){
					url = mod.upd_prep_url(mod_id,cos_id);
					parent.mod_edit_mode(url, in_course_id, in_course_timestamp, in_course_struct_xml_cnt, in_course_struct_xml_1);
				//	parent.show_frame_content(url);
				}
			}
			
			function get_cos_timestamp() {
				//alert("get_cos_timestamp");
				parent.get_cos_timestamp();
			}
			
			
			
	
			// from JS to Applet
			
			function addNode(node_title, mod_type, mod_id, course_timestamp) {
				//alert("Applet addNode\n"+"node_title:"+node_title+"\nmod_type:"+mod_type+"\nmod_id:"+mod_id+"\ncourse_timestamp:"+course_timestamp);
				//document.CourseAuthoring.addNodeValues(node_title, mod_type, mod_id, course_timestamp);
				jstree_addNode_commit(node_title, mod_type, mod_id, course_timestamp);
			}
	
			function editNode(node_title, course_timestamp) {
				//alert("Applet editNode\nnode_title:"+node_title);
				jstree_editNode_commit(node_title, course_timestamp);
				//document.CourseAuthoring.setNodeValues(node_title);
			}
			
			function cancelAdd() {
				//alert("Applet cancelAdd");
				jstree_cancelAdd();
				//document.CourseAuthoring.cancelAdd();
			}
			
			function cancelEdit() {
				//alert("Applet cancelEdit");
				//document.CourseAuthoring.cancelEdit();
			}
			
			function cancelDelete() {
				//alert("Applet cancelDelete");	
				//document.CourseAuthoring.cancelDelete();
			}
			
			function canceNav() {
				//alert("Applet cancelNav");
				//document.CourseAuthoring.cancelNavLock();
			}
			
			function confirmDelete(course_timestamp) {
				//alert("Applet confirmDelete");
				var node = jstree.getSelected();
				node.getParent().remove(node);
				set_cos_timestamp(course_timestamp);
				//document.CourseAuthoring.confirmDelete(course_timestamp);
				btnControl();
			}
	
			function saveCourseStructSuccess() {
				//alert("Applet saveCourseStructSuccess");
				get_cos_timestamp();
				jstree_node_commit();
				//document.CourseAuthoring.saveCourseStructSuccess();
			}
			
			function saveCourseStructFailure() {
				//alert("Applet saveCourseStructFailure");
				jstree_node_rollback();
				//document.CourseAuthoring.saveCourseStructFailure();
			}
	
			function set_cos_timestamp(course_timestamp) {
				//alert("Applet set_cos_timestamp:"+course_timestamp);
				parent.course_timestamp = course_timestamp;
				//document.CourseAuthoring.set_cos_timestamp(course_timestamp);
			}
			function getCosStructureXML(){
				//alert("Applet getCosStructureXML");
				var node = jstree.getSelected();
				node.attributes["title"] = temp_title;
			    mod_edit_mode(node.getSrcId(),parent.cos_id, parent.course_timestamp, 1, jstree.toXML())
				//document.CourseAuthoring.getCosStructureXML();	
			}
			
			// from JS to JS
			function set_del_module_var(id, status, timestamp, attempted, lang) {
				//alert("frames['NavPage' ].set_del_module_var:\nid:"+id+"\nstatus:"+status+"\ntimestamp:"+timestamp+"\nattempted:"+attempted+"\nlang:"+lang);
				mod_id = id;
				mod_status = status;
				mod_timestamp = timestamp;
				mod_attempted = attempted;
				mod_lang = lang;
			}
			
			function finish_loading() {
				parent.finish_loading();
			}
	
			// normal method
			function delete_module(in_course_id, in_course_timestamp, in_course_struct_xml_cnt, in_course_struct_xml_1) {
			    //window.top.Dialog.alert(2);
				if(!jstree_exist_new_node()){
					var node = jstree.getSelected();
					if(node == null || node == '' || node == undefined ){
						return;
					}
					if(node != null && node!=jstree.getTree() && !node.hasChildren()){
						if(node.attributes["itemtype"]=="FDR"){
							var isDelete = confirm(eval('label_tm.label_core_training_management_473'));
							if(!isDelete){
								return;
							}
							node.getParent().remove(node);
							root_action();
							saveCourseStructXML(parent.cos_id, parent.course_timestamp, 1, jstree.toXML());
						}else{
						    //course_lst.del_mod_url(mod_id,cos_id,mod_status,mod_timestamp,mod_attempted,mod_lang,parent.course_timestamp,jstree.toXML(node.getId()));
							url = course_lst.del_mod_url(mod_id,cos_id,mod_status,mod_timestamp,mod_attempted,mod_lang,parent.course_timestamp,jstree.toXML(node.getId()));
							if (url) {
							//  parent.show_frame_content(url);
								parent.delete_module(mod_id, mod_timestamp, parent.cos_id, parent.course_timestamp, 1, jstree.toXML(node.getId()));
								get_cos_timestamp();
							}
						}
					}else{
					   if(node!=jstree.getTree()){
					      window.top.Dialog.alert(eval('label_tm.label_core_training_management_365'));
					   } 
					}
				}
			}
			
			function saveCourseStructXML(in_course_id, in_course_timestamp, in_course_struct_xml_cnt, in_course_struct_xml_1) {			
				//alert("saveCourseStructXML " + "\nin_course_id:"+in_course_id + "\n"+"in_course_timestamp:"+in_course_timestamp+ "\n"+"in_course_struct_xml_cnt:"+in_course_struct_xml_cnt+"\n"+"in_course_struct_xml_1:"+in_course_struct_xml_1);
				parent.saveCourseStructXML(in_course_id, in_course_timestamp, in_course_struct_xml_cnt, in_course_struct_xml_1);
			}		
		 function show_menu(){
				window.parent.menu_content.cols="225,*";	
				var obj_tree = document.getElementById("tree_div");
				var obj_show_menu = document.getElementById("show_menu_div");
				obj_show_menu.style.display="none";
				obj_tree.style.display="";
			}
			function hid_menu(){
				window.parent.menu_content.cols="30,*";	
				var obj_tree = document.getElementById("tree_div");
				var obj_show_menu = document.getElementById("show_menu_div");
				obj_tree.style.display="none";
				obj_show_menu.style.display="";
			}
			]]></script>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="page_onload()">
			<table cellpadding="0" cellspacing="0" border="0" class="Bg">
				<xsl:choose>
				<xsl:when test="$for_scorm = 0">
				<tr>
					<td>
						<xsl:if test="$isEnrollment_related = 'false'">
						<table cellpadding="0" cellspacing="0" border="0" style="width:232px;">
							<tr id="operation_mod">
								<td height="28px" width="40px" align="center" id ="add_folder" style="margin-right:4px;">
									<xsl:call-template name="img_button">
										<xsl:with-param name="wb_gen_btn_name" select="$lab_add_folder_tooltip"/>
										<xsl:with-param name="wb_gen_btn_img"><xsl:value-of select="$img_path"/>add_folder.gif</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_href">Javascript:add_folder();</xsl:with-param>
									</xsl:call-template>
								</td>
								<td height="28px" width="40px" align="center" id ="add_module" style="margin-right:4px;">
									<xsl:call-template name="img_button">
										<xsl:with-param name="wb_gen_btn_name" select="$lab_add_module_tooltip"/>
										<xsl:with-param name="wb_gen_btn_img"><xsl:value-of select="$img_path"/>add_document.gif</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_href">Javascript:add_module();</xsl:with-param>
									</xsl:call-template>
								</td>
								<td height="28px" width="40px" align="center" id ="add_video" style="margin-right:4px;">
									<xsl:call-template name="img_button">
										<xsl:with-param name="wb_gen_btn_name" select="$lab_add_vido_tooltip"/>
										<xsl:with-param name="wb_gen_btn_img"><xsl:value-of select="$img_path"/>add_vod.gif</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_href">Javascript:add_vido();</xsl:with-param>
									</xsl:call-template>
								</td>
								<!-- 
								<td height="28px" width="40px" align="center" id ="add_test" >
									<xsl:call-template name="img_button">
										<xsl:with-param name="wb_gen_btn_name" select="$lab_add_test_tooltip"/>
										<xsl:with-param name="wb_gen_btn_img"><xsl:value-of select="$img_path"/>add_test.gif</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_href">Javascript:add_test();</xsl:with-param>
									</xsl:call-template>
								</td>
								 -->
								<td height="28px" width="40px" align="center" style="margin-right:4px;">
									<xsl:call-template name="img_button">
										<xsl:with-param name="wb_gen_btn_name" select="$lab_edit_node_tooltip"/>
										<xsl:with-param name="wb_gen_btn_img"><xsl:value-of select="$img_path"/>edit.gif</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_href">Javascript:jstree_editNode();</xsl:with-param>
									</xsl:call-template>
								</td>
								<td height="28px" width="40px" align="center" style="margin-right:4px;">
									<xsl:call-template name="img_button">
										<xsl:with-param name="wb_gen_btn_name" select="$lab_remove_node_tooltip"/>
										<xsl:with-param name="wb_gen_btn_img"><xsl:value-of select="$img_path"/>delete.gif</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_href">Javascript:delete_module();</xsl:with-param>
									</xsl:call-template>
								</td>
								<td height="28px" width="40px" align="center" style="margin-right:4px;">
									<xsl:call-template name="img_button">
										<xsl:with-param name="wb_gen_btn_name" select="$lab_reorder_node_tooltip"/>
										<xsl:with-param name="wb_gen_btn_img"><xsl:value-of select="$img_path"/>reorder.gif</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_href">Javascript:reorder_module();</xsl:with-param>
									</xsl:call-template>
								</td>								
								<!--<td><input type="button" value="toxml" onclick="jstree.reload();"/></td>
								<td><input type="button" value="toHTML" onclick="alert(jstree.toHtml());"/></td>-->
							</tr>
							<tr id="reorder_mod" style="display:none">
								<td height="28px" width="50px" align="center">
									<xsl:call-template name="img_button">
										<xsl:with-param name="wb_gen_btn_name" select="$lab_move_up_node_tooltip"/>
										<xsl:with-param name="wb_gen_btn_img"><xsl:value-of select="$img_path"/>up.gif</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_href">Javascript:move_node_up();</xsl:with-param>
									</xsl:call-template>
								</td>
								<td height="28px" width="50px" align="left">
									<xsl:call-template name="img_button">
										<xsl:with-param name="wb_gen_btn_name" select="$lab_move_down_node_tooltip"/>
										<xsl:with-param name="wb_gen_btn_img"><xsl:value-of select="$img_path"/>down.gif</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_href">Javascript:move_node_down();</xsl:with-param>
									</xsl:call-template>
								</td>
								<td height="28px" width="50px" align="left">
									<xsl:call-template name="img_button">
										<xsl:with-param name="wb_gen_btn_name" select="$lab_save_reorder_tooltip"/>
										<xsl:with-param name="wb_gen_btn_img"><xsl:value-of select="$img_path"/>reorder_confirm.gif</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_href">Javascript:reorder_confirm();</xsl:with-param>
									</xsl:call-template>
								</td>
								<td height="28px" width="50px" align="left">
									<xsl:call-template name="img_button">
										<xsl:with-param name="wb_gen_btn_name" select="$lab_gen_cancel"/>
										<xsl:with-param name="wb_gen_btn_img"><xsl:value-of select="$img_path"/>fm_clash.gif</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_href">Javascript:reorder_reset(true);</xsl:with-param>
									</xsl:call-template>
								</td>															
							</tr>
						</table>
						</xsl:if>
					</td>
				</tr>
				<tr>
					<td valign="top">
						<div id="tree_div" class="Bg" style="width:225px;border:1px solid Silver;overflow:auto;padding-left:5px"/>
					</td>
				</tr>
				</xsl:when>
				<xsl:otherwise>
					<tr>
						<td valign="top" onmouseover="show_menu();" onmouseout="hid_menu();">
						<div id="tree_div" style="width:225px;border:1px solid Silver;overflow:auto;display:none;"/>
						<div id="show_menu_div" style="width:20px;border:0px solid Silver;overflow:auto;">
							<div style="height:200px;"/>
							<div style="border:1px solid Silver;background:#c6cfe0">导航菜单</div>
							</div>
						</td>
					</tr>
				</xsl:otherwise>
				</xsl:choose>
			</table>
			
		</body>
	</xsl:template>
<!-- =================================================================== -->
<xsl:template name="img_button">
	<xsl:param name="frm">document.frmXml</xsl:param>
	<xsl:param name="field_name">frmSubmitBtn</xsl:param>
	<xsl:param name="wb_gen_btn_name"/>
	<xsl:param name="wb_gen_btn_href"/>
	<xsl:param name="wb_gen_btn_img"/>
	<xsl:param name="id"/>
	<!--
	<input onClick="{$wb_gen_btn_href}" class="Btn" value="" name="{$field_name}" type="button" style="background-image:url({$wb_gen_btn_img}); background-position:center; width:22px;height: 22px" title="{$wb_gen_btn_name}">
		<xsl:if test="$id != ''">
			<xsl:attribute name="id"><xsl:value-of select="$id"/></xsl:attribute>
		</xsl:if>
	</input>
	-->
	
	<a href="#" target="_self" onclick="{$wb_gen_btn_href}" title="{$wb_gen_btn_name}"><img src="{$wb_gen_btn_img}" alt="{$wb_gen_btn_name}" border="0"/></a>
	
</xsl:template>
	
</xsl:stylesheet>
