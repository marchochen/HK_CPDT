<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:import href="../utils/wb_gen_form_button.xsl"/>
<xsl:import href="../utils/wb_css.xsl"/>
<xsl:import href="../share/usr_detail_label_share.xsl"/>
<xsl:variable name="img_path"><xsl:value-of select="$wb_img_path"/></xsl:variable>
	<xsl:template name="tree_js">
		<xsl:param name="srcid"/>
		<xsl:param name="title"/>
		<xsl:param name="identifier"/>
		
		<xsl:param name="lab_add_folder_tooltip"/>
		<xsl:param name="lab_add_module_tooltip"/>
		<xsl:param name="lab_edit_node_tooltip"/>
		<xsl:param name="lab_remove_node_tooltip"/>
		<xsl:param name="lab_reorder_node_tooltip"/>
		<xsl:param name="lab_move_up_node_tooltip"/>
		<xsl:param name="lab_move_down_node_tooltip"/>
		<xsl:param name="lab_save_reorder_tooltip"/>
		<xsl:param name="lab_new_node_default_label"/>
		<xsl:param name="lab_loading_applet"/>		
			<head>
			<title><xsl:value-of select="$wb_wizbank" /></title>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<script type="text/javascript" src="../static/js/jquery.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_ugr.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}gen_utils.js"  type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_utils.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}xtree.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}xmlextras.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}xloadtree.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wizbTree.js"/>
			<link type="text/css" rel="stylesheet" href="{$img_path}tree/css/xtree.css"/>
			<link type="text/css" rel="stylesheet" href="{$img_path}tree/css/xtree2.links.css"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
		    <script language="Javascript" type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/global_{$wb_cur_lang}.js"/>
		    <script language="Javascript" type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/label_lm_{$wb_cur_lang}.js"/>
			
			<link rel="stylesheet" href="../static/css/base.css"/>
			<!--alert样式  -->
			<link rel="stylesheet" href="../static/js/jquery.qtip/jquery.qtip.css" />
			<script type="text/javascript" src="../static/js/jquery.dialogue.js"></script>
			<script type="text/javascript" src="../static/js/jquery.qtip/jquery.qtip.js"></script>
			
			<script language="Javascript" type="text/javascript" src="../../static/js/cwn_utils.js"/>
						
			<xsl:call-template name="new_css"/>
			
			<script language="JavaScript" type="text/javascript"><![CDATA[
			ugr = new wbUgr();			
			
		var new_node_id = "";	
		var temp_title = "$mod_title_";
		var temp_id = "$mod_id_";
		var temp_type = "$mod_type_";				
			
		webFXTreeConfig.setSkinImage("]]><xsl:value-of select="$img_path"/><![CDATA[tree/images/default");
		
		
		var src_url = self.location.href + "&create_tree=y";
		var jstree = new wizbTree("",src_url);
			jstree.setText("]]><xsl:value-of select="$lab_grade"/><![CDATA[");
			jstree.setSrcId(]]><xsl:value-of select="$srcid"/><![CDATA[);
			jstree.setIcon("]]><xsl:value-of select="$img_path"/><![CDATA[root.gif");
			jstree.setOpenIcon("]]><xsl:value-of select="$img_path"/><![CDATA[root.gif");
			jstree.setAction("javascript:root_action()");
			jstree.setTarget("_self");
			//jstree.setExpanded(true);
			jstree.dynamicExpandAll("jstree");			
			
			
		function changeContent(url) {
			parent.document.getElementById("ContentPage").src = url;
		}
		function root_action(){
			if(!jstree_exist_new_node()){
				changeContent(ugr.ugr_home_url());
			}
		}
					
		function page_onload(){
			var div_width = document.body.clientWidth-10;
			var div_height = document.body.clientHeight -80;   
					var obj_div = document.getElementById("tree_div");
					obj_div.style.width = div_width+'px';
					obj_div.style.height = div_height+'px';   
					obj_div.innerHTML=jstree.toHtml();		
		}
		
		function jstree_exist_new_node(){
			if(new_node_id == ''){
				return false;
			}else{
				var node = webFXTreeHandler.getNodeById(new_node_id);
				jstree.setSelected(node);
				return true;
			}			
		}		
		
		function jstree_add_ugr(){
			if(jstree_exist_new_node()){
				return false;
			}		
			////alert("old:"+wizbTree.getMaxItemIndex());
			////alert(wizbTree.getMaxItemIndex()+1);
			wizbTree._maxitemindex = (wizbTree.getMaxItemIndex()+1);
			////alert("new:"+wizbTree.getMaxItemIndex());
			var node = jstree.getSelected();
			if(node==null){
				node = jstree.getTree();
			}
			var new_node = new wizbTreeItem("<font color='red'>]]><xsl:value-of select="$lab_new_node_default_label"/><![CDATA[</font>");
			new_node.attributes = {};
			new_node.setSrcId(temp_id);
			new_node.attributes["title"] = temp_title;
			new_node.attributes["identifier"] = "ITEM"+(wizbTree.getMaxItemIndex());
			new_node.attributes["itemtype"] = "UGR";
			new_node.attributes["restype"] = temp_type;
			
			var ugr_id = 0;
			if(node.getSrcId()){
				ugr_id = node.getSrcId();
			}
			var url = ugr.ugr_ins_prep_url(ugr_id);
			new_node.setAction(url);
			new_node.setTarget("ContentPage");
			
			node.add(new_node);
			node.setExpanded(true);
			jstree.setSelected(new_node);
			new_node_id = new_node.getId();			
			add_ugr(node.getSrcId()?node.getSrcId():0,"",1,jstree.toXML());
			//parent.add_ugr(url);
		}
		
		function jstree_ugr_edit_mode(){
			if(jstree_exist_new_node()){
				return false;
			}		
			var node = jstree.getSelected();
			if(node==null || node==jstree.getTree()){
				return false;
			}
			ugr_edit_mode(node.getSrcId(),parent.cos_id, parent.course_timestamp, 1, jstree.toXML());
		}
		
		function jstree_delete_ugr(){	
			var node = jstree.getSelected();
			if(node==null || node==jstree.getTree()){
				return false;
			}
			////alert("delete module");	
			if(!jstree_exist_new_node()){
				var node = jstree.getSelected();
				if(node != null ){
				  var url =wb_utils_controller_base+"admin/profession/getAffectedPfs";
			      $.ajax({
					url : url,
				    data:{id:node.getSrcId()},  
					dataType : 'json',
					cache:false,  
					type : "post",
					success : function(data) {
	                  if(data.flag){
						 //Dialog.alert('1');
						 Dialog.alert(fetchLabel('label_core_learning_map_126'));
						}else{
							delete_ugr(node.getSrcId(), '', 1, jstree.toXML(node.getId()));
						}
								}
							})
				    
				} 
			}
		}			
		
		function jstree_add_commit(node_title, mod_type, mod_id, course_timestamp){
			var node = webFXTreeHandler.getNodeById(new_node_id);
			last_modify_node = node;
			node.setSrcId(mod_id);
			node.setText(node_title);
			node.attributes["title"] = node_title;
			node.attributes["restype"] = mod_type;
			node.setAction("javascript:ugr_read_mode('"+mod_id+"');");
			node.setTarget("_self");
			new_node_id = "";			
		}
		
		function jstree_editNode_commit(node_title){	
			////alert("jstree_editNode_commit");
			var node = jstree.getSelected();
			last_modify_node = jstree.getSelected();
			node.setText(node_title);
			node.attributes["title"]=node_title;
			node.setTarget("ContentPage");
			var str_action = "javascript:ugr_read_mode('" + node.getSrcId() + "');";
			node.setAction(str_action);
			node.setTarget("_self");
			//////alert("course_timestamp"+course_timestamp+"\nparent.course_timestamp:"+parent.course_timestamp);
			new_node_id = "";
		}				
		
		function jstree_cancelAdd(){
			var node = webFXTreeHandler.getNodeById(new_node_id);
			if(node != null){
				node.getParent().remove(node);
			}
			jstree.setSelected(jstree);
			wizbTree._maxitemindex = wizbTree.getMaxItemIndex()-1;
			new_node_id = "";
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
			var childs = jstree.getTree().childNodes;
			var id_order = "";
			for(i=0;i<childs.length;i++){
				if(i==0){
					id_order += childs[i].getSrcId();
				}else{
					id_order += ","+childs[i].getSrcId();
				}
				
			}
			//alert(id_order);
			save_ugr_order(id_order);
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
			
							
				
				function wb_utils_finish_loading_applet(){
					// I.E.
					if(document.all){
						self.DIV2.style.left = -1500;
						self.DIV1.style.left = 0;
					}
					// NS6
						else if(document.getElementById){
						document.getElementById("DIV2").style.left = -500;
					}
					// NS other than 6
						else{
						document.layers["DIV2"].left = -500;
					}
				}
				
				/////////////////////////////////////////////////////////////////////////////
				// from Applet to JS
				
				function finish_loading() {
				}
				
				function home() {
					parent.home();
				}
				
				function refresh() {
					window.location.reload();
				}
				
				function ugr_read_mode(ugr_ent_id) {
					// Store the usergrade currently selected.
					if(!jstree_exist_new_node()){				
						wb_utils_set_cookie('ugr_ent_id',ugr_ent_id);
						url = ugr.ugr_url(ugr_ent_id);
						changeContent(url);
					}
				}
		
				function add_ugr(ugr_ent_id, in_course_timestamp, in_course_struct_xml_cnt, in_course_struct_xml_1) {
					changeContent(ugr.ugr_ins_prep_url(ugr_ent_id));
				}
		
				function ugr_edit_mode(ugr_ent_id, in_course_id, in_course_timestamp, in_course_struct_xml_cnt, in_course_struct_xml_1) {
					changeContent(ugr.ugr_upd_prep_url(ugr_ent_id));
				}
				
				function delete_ugr(ugr_ent_id, in_course_timestamp, in_course_struct_xml_cnt, in_course_struct_xml_1) {
					url = ugr.ugr_del_url(']]><xsl:value-of select="$wb_lang"/><![CDATA[', ugr_ent_id);
					changeContent(url);
				}
				
				function save_ugr_order(order) {
					//alert("save_ugr_order\norder:"+order);
					parent.save_ugr_order(order)
				}
				
				/////////////////////////////////////////////////////////////////////////////
				// from JS to Applet
				
				function cancel_add() {
					jstree_cancelAdd();
				}
				
				function add_node(node_title, mod_type, mod_id, course_timestamp) {
					//alert("Applet add_node\nnode_title:"+node_title+"\nmod_type:"+mod_type+"\nmod_id:"+mod_id+"\ncourse_timestamp:"+course_timestamp);
					jstree_add_commit(node_title,mod_type,mod_id,course_timestamp);
					//document.UserGrade.addNodeValues(node_title, mod_type, mod_id, course_timestamp);
				}
				
				function edit_node(node_title) {
					//alert("Applet edit_node\nnode_title:"+node_title);
					jstree_editNode_commit(node_title);
					//document.UserGrade.setNodeValues(node_title);
				}
		
				function confirm_delete() {
					//alert("Applet confirm_delete");
					var node = jstree.getSelected();
					node.getParent().remove(node);
					//document.UserGrade.confirmDelete(0);
				}
		
				function save_ugr_success() {
					//alert("Applet save_ugr_success");
					//document.UserGrade.saveUgrSuccess();
				}
				]]></script>			
				
				</head>
		<body style="" leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="page_onload()">
			<table cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td>
						<table cellpadding="0" cellspacing="0" border="0">
							<tr id="operation_mod">
								<!--<td height="28px" width="50px" align="center">
									<xsl:call-template name="img_button">
										<xsl:with-param name="wb_gen_btn_name" select="$lab_add_folder_tooltip"/>
										<xsl:with-param name="wb_gen_btn_img"><xsl:value-of select="$img_path"/>add_folder.gif</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_href">Javascript:add_folder();</xsl:with-param>
									</xsl:call-template>
								</td>
								-->
								<td height="28px" width="50px" align="center">
									<xsl:call-template name="img_button">
										<xsl:with-param name="wb_gen_btn_name" select="$lab_add_module_tooltip"/>
										<xsl:with-param name="wb_gen_btn_img"><xsl:value-of select="$img_path"/>add_document.gif</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_href">Javascript:jstree_add_ugr();</xsl:with-param>
									</xsl:call-template>
								</td>
								<td height="28px" width="50px" align="center">
									<xsl:call-template name="img_button">
										<xsl:with-param name="wb_gen_btn_name" select="$lab_edit_node_tooltip"/>
										<xsl:with-param name="wb_gen_btn_img"><xsl:value-of select="$img_path"/>edit.gif</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_href">Javascript:jstree_ugr_edit_mode();</xsl:with-param>
									</xsl:call-template>
								</td>
								<td height="28px" width="50px" align="center">
									<xsl:call-template name="img_button">
										<xsl:with-param name="wb_gen_btn_name" select="$lab_remove_node_tooltip"/>
										<xsl:with-param name="wb_gen_btn_img"><xsl:value-of select="$img_path"/>delete.gif</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_href">Javascript:jstree_delete_ugr();</xsl:with-param>
									</xsl:call-template>
								</td>
								<td height="28px" width="50px" align="center">
									<xsl:call-template name="img_button">
										<xsl:with-param name="wb_gen_btn_name" select="$lab_reorder_node_tooltip"/>
										<xsl:with-param name="wb_gen_btn_img"><xsl:value-of select="$img_path"/>reorder.gif</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_href">Javascript:reorder_module();</xsl:with-param>
									</xsl:call-template>
								</td>								
								<!--<td><input type="button" value="toxml" onclick="//alert(jstree.toXML());"/></td>
								<td><input type="button" value="toHTML" onclick="//alert(jstree.toHtml());"/></td>-->
							</tr>
							<tr id="reorder_mod" style="display:none">
								<td height="28px" width="50px" align="center">
									<xsl:call-template name="img_button">
										<xsl:with-param name="wb_gen_btn_name" select="$lab_move_up_node_tooltip"/>
										<xsl:with-param name="wb_gen_btn_img"><xsl:value-of select="$img_path"/>up.gif</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_href">Javascript:move_node_up();</xsl:with-param>
									</xsl:call-template>
								</td>
								<td height="28px" width="50px" align="center">
									<xsl:call-template name="img_button">
										<xsl:with-param name="wb_gen_btn_name" select="$lab_move_down_node_tooltip"/>
										<xsl:with-param name="wb_gen_btn_img"><xsl:value-of select="$img_path"/>down.gif</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_href">Javascript:move_node_down();</xsl:with-param>
									</xsl:call-template>
								</td>
								<td height="28px" width="50px" align="center">
									<xsl:call-template name="img_button">
										<xsl:with-param name="wb_gen_btn_name" select="$lab_save_reorder_tooltip"/>
										<xsl:with-param name="wb_gen_btn_img"><xsl:value-of select="$img_path"/>reorder_confirm.gif</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_href">Javascript:reorder_confirm();</xsl:with-param>
									</xsl:call-template>
								</td>
								<td height="28px" width="50px" align="center">
									<xsl:call-template name="img_button">
										<xsl:with-param name="wb_gen_btn_name" select="$lab_gen_cancel"/>
										<xsl:with-param name="wb_gen_btn_img"><xsl:value-of select="$img_path"/>fm_clash.gif</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_href">Javascript:reorder_reset(true);</xsl:with-param>
									</xsl:call-template>
								</td>															
							</tr>							
						</table>
					</td>
				</tr>
				<tr>
					<td valign="top">
						<div id="tree_div" class="Bg" style="border:1px solid Silver;overflow:auto;"/>
					</td>
				</tr>
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
	
	<a href="javascript:void(0)" target="_self" onclick="{$wb_gen_btn_href}" title="{$wb_gen_btn_name}"><img src="{$wb_gen_btn_img}" alt="{$wb_gen_btn_name}" border="0"/></a>
	
</xsl:template>	
</xsl:stylesheet>
