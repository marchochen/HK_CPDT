<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:import href="../utils/wb_gen_form_button.xsl"/>
<xsl:variable name="img_path"><xsl:value-of select="$wb_img_path"/></xsl:variable>

	<!-- ====================================================================== -->
	<xsl:template name="tree_js" match="tree_list_js">
		<xsl:param name="lab_now_loading"/>
		<xsl:param name="lab_catalog"/>
		<xsl:param name="lab_item"/>
		<xsl:param name="lab_competence"/>
		<xsl:param name="lab_user_group"/>
		<xsl:param name="lab_user_group_and_user"/>
		<xsl:param name="lab_appr_root"/>
		<xsl:param name="lab_grade"/>
		<xsl:param name="lab_grade_and_user"/>
		<xsl:param name="lab_class1"/>
		<xsl:param name="lab_class1_and_user"/>
		<xsl:param name="lab_industry"/>
		<xsl:param name="lab_industry_and_user"/>
		<xsl:param name="lab_knowledge_object"/>
		<xsl:param name="lab_root_catalog"/>
		<xsl:param name="lab_root_item"/>
		<xsl:param name="lab_root_competence"/>
		<xsl:param name="lab_root_user_group"/>
		<xsl:param name="lab_root_user_group_and_user"/>
		<xsl:param name="lab_root_grade"/>
		<xsl:param name="lab_root_grade_and_user"/>
		<xsl:param name="lab_root_class1"/>
		<xsl:param name="lab_root_class1_and_user"/>
		<xsl:param name="lab_root_industry"/>
		<xsl:param name="lab_root_industry_and_user"/>
		<xsl:param name="lab_root_knowledge_object"/>
		<xsl:param name="lab_root_objective"/>
		<xsl:param name="lab_root_lib_catalogue"/>
		<xsl:param name="lab_root_domain"/>
		<xsl:param name="lab_root_folders"/>
		<xsl:param name="lab_root_training_center"/>
		<xsl:param name="lab_root_skill"/>
		<xsl:param name="lab_organizational"/>
		<xsl:param name="lab_global"/>
		<xsl:param name="lab_close"/>
		<xsl:param name="lab_move_objective"/>
		<xsl:param name="lab_all_skillset"/>
		<xsl:param name="show_button">true</xsl:param>
		<!--<head>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>-->
			<!-- <xsl:call-template name="new_css"/> -->
			<!-- <script language="JavaScript" type="text/javascript" src="{$wb_js_path}jquery.js"/> -->
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_goldenman.js"/>			
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}xtree.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}xmlextras.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}xloadtree.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wizbTree.js"/>
			<link type="text/css" rel="stylesheet" href="{$img_path}tree/css/xtree.css"/>
			<link type="text/css" rel="stylesheet" href="{$img_path}tree/css/xtree2.links.css"/>			
			<script language="JavaScript" type="text/javascript"><![CDATA[
			
		goldenman = new wbGoldenMan;
		
		tree_type              = getUrl('tree_type')?getUrlParam('tree_type'):''                       ;
		pick_method            = getUrl('pick_method')?getUrlParam('pick_method'):''                   ;
		js                     = getUrl('js')?getUrlParam('js'):''                                     ;
		item_type_list         = getUrl('item_type_lst')?getUrlParam('item_type_lst'):''             ;
		pick_leave             = getUrl('pick_leave')?getUrlParam('pick_leave'):''                     ;
		approved_list          = getUrl('approved_list')?getUrlParam('approved_list'):''               ;
		flag                   = getUrl('flag')?getUrlParam('flag'):''                                 ;
		close_option           = getUrl('close_option')?getUrlParam('close_option'):''                 ;
		pick_root              = getUrl('pick_root')?getUrlParam('pick_root'):''                       ;
		override_appr_usg      = getUrl('override_appr_usg')?getUrlParam('override_appr_usg'):''       ;
		tree_subtype           = getUrl('tree_subtype')?getUrlParam('tree_subtype'):''                 ;
		get_supervise_group    = getUrl('get_supervise_group')?getUrlParam('get_supervise_group'):''   ;
		complusory_tree        = getUrl('complusory_tree')?getUrlParam('complusory_tree'):''           ;
		get_direct_supervise   = getUrl('get_direct_supervise')?getUrlParam('get_direct_supervise'):'' ;
		ftn_ext_id             = getUrl('ftn_ext_id')?getUrlParam('ftn_ext_id'):''                     ;
		search_rol_ext_id      = getUrl('search_rol_ext_id')?getUrlParam('search_rol_ext_id'):''                     ;
		parent_tcr_id          = getUrl('parent_tcr_id')?getUrlParam('parent_tcr_id'):''               ;
		filter_user_group      = getUrl('filter_user_group')?getUrlParam('filter_user_group'):''       ;
		show_bil_nickname      = getUrl('show_bil_nickname')?getUrlParam('show_bil_nickname'):''       ;
		sgp_id				   = getUrl('sgp_id')?getUrlParam('sgp_id'):''  
		itm_id				   = getUrl('itm_id')?getUrlParam('itm_id'):''
		tc_id				   = getUrl('tc_id')?getUrlParam('tc_id'):''
		groupType			   = getUrl('groupType')?getUrlParam('groupType'):''
		
		/*
		alert("tree_type           " + tree_type           );
		alert("pick_method         " + pick_method         );
		alert("js                  " + js                  );
		alert("item_type_list      " + item_type_list      );
		alert("pick_leave          " + pick_leave          );
		alert("approved_list       " + approved_list       );
		alert("flag                " + flag                );
		alert("close_option        " + close_option        );
		alert("pick_root           " + pick_root           );
		alert("override_appr_usg   " + override_appr_usg   );
		alert("tree_subtype        " + tree_subtype        );
		alert("get_supervise_group " + get_supervise_group );
		alert("complusory_tree     " + complusory_tree     );
		alert("get_direct_supervise" + get_direct_supervise);
		alert("ftn_ext_id          " + ftn_ext_id          );
		alert("sgp_id			   " +sgp_id			   );		
		*/
				
		//var tree_attrib = ["obj_tree","name","id","title","type","src","checkbox", "radio", "rootbox", "nodebox"];
		
		var jstree = Array();
		
		$(function(){
			page_onload();
		})
		]]>
		<xsl:apply-templates select="//tree_list_js/tree" mode="loadxmltree">
					<xsl:with-param name="tree_type" select="@type"/>
					<xsl:with-param name="lab_now_loading" select="$lab_now_loading"/>
					<xsl:with-param name="lab_catalog" select="$lab_catalog"/>
					<xsl:with-param name="lab_item" select="$lab_item"/>
					<xsl:with-param name="lab_competence" select="$lab_competence"/>
					<xsl:with-param name="lab_user_group" select="$lab_user_group"/>
					<xsl:with-param name="lab_user_group_and_user" select="$lab_user_group_and_user"/>
					<xsl:with-param name="lab_appr_root" select="$lab_appr_root"/>
					<xsl:with-param name="lab_grade" select="$lab_grade"/>
					<xsl:with-param name="lab_grade_and_user" select="$lab_grade_and_user"/>
					<xsl:with-param name="lab_class1" select="$lab_class1"/>
					<xsl:with-param name="lab_class1_and_user" select="$lab_class1_and_user"/>
					<xsl:with-param name="lab_industry" select="$lab_industry"/>
					<xsl:with-param name="lab_industry_and_user" select="$lab_industry_and_user"/>
					<xsl:with-param name="lab_knowledge_object" select="$lab_knowledge_object"/>
					<xsl:with-param name="lab_root_catalog" select="$lab_root_catalog"/>
					<xsl:with-param name="lab_root_item" select="$lab_root_item"/>
					<xsl:with-param name="lab_root_competence" select="$lab_root_competence"/>
					<xsl:with-param name="lab_root_user_group" select="$lab_root_user_group"/>
					<xsl:with-param name="lab_root_user_group_and_user" select="$lab_root_user_group_and_user"/>
					<xsl:with-param name="lab_root_grade" select="$lab_root_grade"/>
					<xsl:with-param name="lab_root_grade_and_user" select="$lab_root_grade_and_user"/>
					<xsl:with-param name="lab_root_class1" select="$lab_root_class1"/>
					<xsl:with-param name="lab_root_class1_and_user" select="$lab_root_class1_and_user"/>
					<xsl:with-param name="lab_root_industry" select="$lab_root_industry"/>
					<xsl:with-param name="lab_root_industry_and_user" select="$lab_root_industry_and_user"/>
					<xsl:with-param name="lab_root_knowledge_object" select="$lab_root_knowledge_object"/>
					<xsl:with-param name="lab_root_objective" select="$lab_root_objective"/>
					<xsl:with-param name="lab_root_lib_catalogue" select="$lab_root_lib_catalogue"/>
					<xsl:with-param name="lab_root_domain" select="$lab_root_domain"/>
					<xsl:with-param name="lab_root_folders" select="$lab_root_folders"/>
					<xsl:with-param name="lab_root_training_center" select="$lab_root_training_center"/>
					<xsl:with-param name="lab_root_skill" select="$lab_root_skill"/>
					<xsl:with-param name="lab_organizational" select="$lab_organizational"/>
					<xsl:with-param name="lab_global" select="$lab_global"/>
					<xsl:with-param name="lab_close" select="$lab_close"/>
					<xsl:with-param name="lab_move_objective" select="$lab_move_objective"/>
					<xsl:with-param name="lab_all_skillset" select="$lab_all_skillset"/>
				</xsl:apply-templates><![CDATA[
		
		webFXTreeConfig.setSkinImage("]]><xsl:value-of select="$img_path"/><![CDATA[tree/images/default");
		
		if(jstree.length>0){
			for(i=0;i<jstree.length;i++){
					jstree[i]["obj_tree"] = new wizbTree(jstree[i]["title"],jstree[i]["src"]);
					jstree[i]["obj_tree"].setSrcId(jstree[i]["srcid"]);
					jstree[i]["obj_tree"].setEnableCheckBox(jstree[i]["checkbox"]);
					jstree[i]["obj_tree"].setEnableRadio(jstree[i]["radio"]);
					jstree[i]["obj_tree"].setEnableRootBox(jstree[i]["rootbox"]);
					jstree[i]["obj_tree"].setEnableNodeBox(jstree[i]["nodebox"]);
					jstree[i]["obj_tree"].setExpanded(true);
					if("]]><xsl:value-of select="//tree_list_js/tree/@onaction"/><![CDATA[" != '') {
						jstree[i]["obj_tree"].setAction("javascript:]]><xsl:value-of select="//tree_list_js/tree/@onaction"/><![CDATA[");
					}
					
			}
		}
		
		function page_onload(width){
			var div_width = document.body.clientWidth/jstree.length;
			if (width) {
				div_width = width;
			}
			for(i=0;i<jstree.length;i++){
					var obj_div = document.getElementById(jstree[i]["name"]+(i+1));
					obj_div.style.cssText = obj_div.style.cssText + "width:" + (div_width-2) + "px !important";
					obj_div.innerHTML=jstree[i]["obj_tree"].toHtml();
			}
			
			confirmAlterTc();
		}
		
		function returnSelectValues(){
		    var str = "";
		    var param = new Array();
			for(j=0;j<jstree.length;j++){
				str = jstree[j]["obj_tree"].getAllSelected();
				if(str=="" || str.length ==0){
					return false;
				}
				//alert(str);
				var opt = String(str).split(":_:_:");
				str="";
				for(m=0;m<opt.length;m++){
					//alert(opt[m]);
					str += "~%~"+String(opt[m]).replace(",","~%~") + "~%~TREE" ;
				}
				param[j] = jstree[j]["type"] +  str;
			}
			
			var str_fun = "pick_ok('" + js + "'" ;
			for(n=0;n<param.length;n++){
				str_fun += ",'" + escape(param[n]) + "'" ;
			}
			str_fun = str_fun + ")";
			//alert(str_fun);
			eval(str_fun);
			
				
		}
		
		function getNodeSrc(tree_type,node_id,node_type){
			var srcurl = "";
			for(i=0;i<jstree.length;i++){
				//alert(jstree[i]["type"] + " " + tree_type.toLowerCase());
				if(jstree[i]["type"].toLowerCase()==tree_type.toLowerCase()){
					srcurl = setUrlParam("node_id",node_id,jstree[i]["src"]);
					if(node_type && node_type != null && node_type != ''){
						srcurl = setUrlParam("node_type",node_type,srcurl);
					}
					if(jstree[i]["type"]=="user_group_and_user" && (get_supervise_group=="1" || get_direct_supervise=="1" ) ){
					//alert(get_supervise_group+"\n"+get_direct_supervise);
						srcurl = setUrlParam("get_supervise_group",0,srcurl);
						srcurl = setUrlParam("get_direct_supervise",0,srcurl);
					}
				}
			}
			//alert(tree_type + "\n url:" + srcurl);
			return srcurl;
		}
		
		function confirmAlterTc(){
			var confirmFunction = getUrlParam('confirm_function');
			var confirm_msg = getUrlParam('confirm_msg');
			
			if(confirmFunction != null && confirmFunction != '' && eval('window.opener.' + confirmFunction) 
				&& confirm_msg != null && confirm_msg != '' && eval('window.opener.' + confirm_msg)) {
				if(confirm(eval('window.opener.' + confirm_msg))) {
					eval('window.opener.' + confirmFunction + '()');
				} else {
					window.close();
				}
			}
			
		}
		
		]]></script>
		<!--</head>-->
		<div leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" >
			<table cellpadding="0" cellspacing="0" border="0">
				<tr>
					<xsl:apply-templates select="//tree_list_js/tree" mode="div">
					
					</xsl:apply-templates>
				</tr>
			</table>
			<xsl:if test="$show_button='true'">
				<table style="margin-top: 20px;">
					<tr>
						<td align="center" height="19" valign="middle">
							<xsl:call-template name="wb_gen_form_button">
								<xsl:with-param name="wb_gen_btn_name" select="$lab_gen_ok"/>
								<xsl:with-param name="wb_gen_btn_href">Javascript:returnSelectValues();</xsl:with-param>
							</xsl:call-template>					
							<xsl:call-template name="wb_gen_form_button">
								<xsl:with-param name="wb_gen_btn_name" select="$lab_close"/>
								<xsl:with-param name="wb_gen_btn_href">Javascript:pick_cancel();</xsl:with-param>
							</xsl:call-template>
						</td>
					</tr>
				</table>
			</xsl:if>
		</div>
	</xsl:template>
	<!-- ====================================================================== -->
	<xsl:template match="tree" mode="loadxmltree">
		<xsl:param name="lab_now_loading"/>
		<xsl:param name="lab_catalog"/>
		<xsl:param name="lab_item"/>
		<xsl:param name="lab_competence"/>
		<xsl:param name="lab_user_group"/>
		<xsl:param name="lab_user_group_and_user"/>
		<xsl:param name="lab_appr_root"/>
		<xsl:param name="lab_grade"/>
		<xsl:param name="lab_grade_and_user"/>
		<xsl:param name="lab_class1"/>
		<xsl:param name="lab_class1_and_user"/>
		<xsl:param name="lab_industry"/>
		<xsl:param name="lab_industry_and_user"/>
		<xsl:param name="lab_knowledge_object"/>
		<xsl:param name="lab_root_catalog"/>
		<xsl:param name="lab_root_item"/>
		<xsl:param name="lab_root_competence"/>
		<xsl:param name="lab_root_user_group"/>
		<xsl:param name="lab_root_user_group_and_user"/>
		<xsl:param name="lab_root_grade"/>
		<xsl:param name="lab_root_grade_and_user"/>
		<xsl:param name="lab_root_class1"/>
		<xsl:param name="lab_root_class1_and_user"/>
		<xsl:param name="lab_root_industry"/>
		<xsl:param name="lab_root_industry_and_user"/>
		<xsl:param name="lab_root_knowledge_object"/>
		<xsl:param name="lab_root_objective"/>
		<xsl:param name="lab_root_lib_catalogue"/>
		<xsl:param name="lab_root_domain"/>
		<xsl:param name="lab_root_folders"/>
		<xsl:param name="lab_root_training_center"/>
		<xsl:param name="lab_root_skill"/>
		<xsl:param name="lab_organizational"/>
		<xsl:param name="lab_global"/>
		<xsl:param name="lab_close"/>
		<xsl:param name="lab_move_objective"/>
		<xsl:param name="lab_all_skillset"/>
		<![CDATA[
		tree_title = "]]><xsl:call-template name="tree_title">
			<xsl:with-param name="tree_type" select="@type"/>
			<xsl:with-param name="tree_title" select="@title"/>
			<xsl:with-param name="lab_now_loading" select="$lab_now_loading"/>
			<xsl:with-param name="lab_catalog" select="$lab_catalog"/>
			<xsl:with-param name="lab_item" select="$lab_item"/>
			<xsl:with-param name="lab_competence" select="$lab_competence"/>
			<xsl:with-param name="lab_user_group" select="$lab_user_group"/>
			<xsl:with-param name="lab_user_group_and_user" select="$lab_user_group_and_user"/>
			<xsl:with-param name="lab_appr_root" select="$lab_appr_root"/>
			<xsl:with-param name="lab_grade" select="$lab_grade"/>
			<xsl:with-param name="lab_grade_and_user" select="$lab_grade_and_user"/>
			<xsl:with-param name="lab_class1" select="$lab_class1"/>
			<xsl:with-param name="lab_class1_and_user" select="$lab_class1_and_user"/>
			<xsl:with-param name="lab_industry" select="$lab_industry"/>
			<xsl:with-param name="lab_industry_and_user" select="$lab_industry_and_user"/>
			<xsl:with-param name="lab_knowledge_object" select="$lab_knowledge_object"/>
			<xsl:with-param name="lab_root_catalog" select="$lab_root_catalog"/>
			<xsl:with-param name="lab_root_item" select="$lab_root_item"/>
			<xsl:with-param name="lab_root_competence" select="$lab_root_competence"/>
			<xsl:with-param name="lab_root_user_group" select="$lab_root_user_group"/>
			<xsl:with-param name="lab_root_user_group_and_user" select="$lab_root_user_group_and_user"/>
			<xsl:with-param name="lab_root_grade" select="$lab_root_grade"/>
			<xsl:with-param name="lab_root_grade_and_user" select="$lab_root_grade_and_user"/>
			<xsl:with-param name="lab_root_class1" select="$lab_root_class1"/>
			<xsl:with-param name="lab_root_class1_and_user" select="$lab_root_class1_and_user"/>
			<xsl:with-param name="lab_root_industry" select="$lab_root_industry"/>
			<xsl:with-param name="lab_root_industry_and_user" select="$lab_root_industry_and_user"/>
			<xsl:with-param name="lab_root_knowledge_object" select="$lab_root_knowledge_object"/>
			<xsl:with-param name="lab_root_objective" select="$lab_root_objective"/>
			<xsl:with-param name="lab_root_lib_catalogue" select="$lab_root_lib_catalogue"/>
			<xsl:with-param name="lab_root_domain" select="$lab_root_domain"/>
			<xsl:with-param name="lab_root_folders" select="$lab_root_folders"/>
			<xsl:with-param name="lab_root_training_center" select="$lab_root_training_center"/>
			<xsl:with-param name="lab_root_skill" select="$lab_root_skill"/>
			<xsl:with-param name="lab_organizational" select="$lab_organizational"/>
			<xsl:with-param name="lab_global" select="$lab_global"/>
			<xsl:with-param name="lab_close" select="$lab_close"/>
			<xsl:with-param name="lab_move_objective" select="$lab_move_objective"/>
			<xsl:with-param name="lab_all_skillset" select="$lab_all_skillset"/>
		</xsl:call-template><![CDATA[";
		
		if(]]><xsl:value-of select="@global_cat"/><![CDATA[){
			tree_title = "]]><xsl:value-of select="$lab_global"/><xsl:value-of select="$lab_catalog"/><![CDATA[";
		}		
		
		index = ]]><xsl:value-of select="position()-1"/><![CDATA[;
		jstree[index] = {};
		jstree[index]["name"] = "tree_]]><xsl:value-of select="@type"/><![CDATA[";
		jstree[index]["srcid"] = "]]><xsl:value-of select="@srcid"/><![CDATA[";
		jstree[index]["title"] = tree_title;
		tree_mode = ]]><xsl:value-of select="@pick_method"/><![CDATA[;
		tree_root = (]]><xsl:value-of select="@pick_root"/><![CDATA[==1)?true:false;
		jstree[index]["type"] = "]]><xsl:value-of select="@type"/><![CDATA[";
		
		
		if(jstree[index]["type"]=="user_group_and_user" && (get_supervise_group=="1" || get_direct_supervise=="1" ) ){
			jstree[index]["title"] = "]]><xsl:value-of select="$lab_appr_root"/><![CDATA[";
		}
		
		
		switch(jstree[index]["type"]){		
			case "move_objective":
				jstree[index]["src"] = wb_utils_invoke_servlet('cmd', 'gen_tree' , 'tree_type', 'move_objective', 'stylesheet', 'gen_tree.xsl', 'pick_leave', '0', 'pick_root','1', 'pick_method', '2' ,'node_id', jstree[index]["srcid"] ,'flag', 'true' ,'tree_subtype' ,'' ,'js' ,'obj.paste','close_option','1' ,'override_appr_usg','0' ,'get_supervise_group','0', 'complusory_tree' ,'1' ,'get_direct_supervise','0');
				break;
			default:
				var tmpUrl = goldenman.opentree(']]><xsl:value-of select="@type"/><![CDATA[',pick_method,js,item_type_list,pick_leave,approved_list,flag,close_option,pick_root,override_appr_usg, tree_subtype, get_supervise_group,complusory_tree,get_direct_supervise,ftn_ext_id,true, parent_tcr_id, filter_user_group, '', '', show_bil_nickname,sgp_id, itm_id,search_rol_ext_id,tc_id,groupType);
				jstree[index]["src"] = setUrlParam("global_cat","]]><xsl:value-of select="@global_cat"/><![CDATA[",tmpUrl);
				//alert(jstree[index]["src"]);
				break;
		}
		
		switch(tree_mode){
			case 1:
				jstree[index]["checkbox"] = true;
				jstree[index]["radio"] = false;
				jstree[index]["rootbox"] = tree_root;
				jstree[index]["nodebox"] = true;				
				break;
			case 2:
				jstree[index]["checkbox"] = false;
				jstree[index]["radio"] = false;
				jstree[index]["rootbox"] = tree_root;
				jstree[index]["nodebox"] = true;
				break;
			case 3:
			case 4:
				jstree[index]["checkbox"] = true;
				jstree[index]["radio"] = false;
				jstree[index]["rootbox"] = tree_root;
				jstree[index]["nodebox"] = false;			
				break;
			case 5:
			case 6:
				jstree[index]["checkbox"] = false;
				jstree[index]["radio"] = true;
				jstree[index]["rootbox"] = tree_root;
				jstree[index]["nodebox"] = false;				
				break;
		};
		//jstree[index]["checkbox"] = ]]><xsl:value-of select="@checkbox"/><![CDATA[;
		//jstree[index]["radio"] = ]]><xsl:value-of select="@radio"/><![CDATA[;
		//jstree[index]["rootbox"] = ]]><xsl:value-of select="@rootbox"/><![CDATA[;
		//jstree[index]["nodebox"] = ]]><xsl:value-of select="@nodebox"/><![CDATA[;]]>
	</xsl:template>
	<!-- ====================================================================== -->
	<xsl:template match="tree" mode="div">
		<td valign="top" class="wzb-listbox2">
			<div id="tree_{@type}{position()}" class="Bg" style="height:360px;border :0px solid Silver;; overflow:auto;padding-left:10px"/>
		</td>
	</xsl:template>
	<!-- ====================================================================== -->
	<xsl:template name="tree_title">
		<xsl:param name="tree_type"/>
		<xsl:param name="tree_title"/>
		<xsl:param name="lab_now_loading"/>
		<xsl:param name="lab_catalog"/>
		<xsl:param name="lab_item"/>
		<xsl:param name="lab_competence"/>
		<xsl:param name="lab_user_group"/>
		<xsl:param name="lab_user_group_and_user"/>
		<xsl:param name="lab_appr_root"/>
		<xsl:param name="lab_grade"/>
		<xsl:param name="lab_grade_and_user"/>
		<xsl:param name="lab_class1"/>
		<xsl:param name="lab_class1_and_user"/>
		<xsl:param name="lab_industry"/>
		<xsl:param name="lab_industry_and_user"/>
		<xsl:param name="lab_knowledge_object"/>
		<xsl:param name="lab_root_catalog"/>
		<xsl:param name="lab_root_item"/>
		<xsl:param name="lab_root_competence"/>
		<xsl:param name="lab_root_user_group"/>
		<xsl:param name="lab_root_user_group_and_user"/>
		<xsl:param name="lab_root_grade"/>
		<xsl:param name="lab_root_grade_and_user"/>
		<xsl:param name="lab_root_class1"/>
		<xsl:param name="lab_root_class1_and_user"/>
		<xsl:param name="lab_root_industry"/>
		<xsl:param name="lab_root_industry_and_user"/>
		<xsl:param name="lab_root_knowledge_object"/>
		<xsl:param name="lab_root_objective"/>
		<xsl:param name="lab_root_lib_catalogue"/>
		<xsl:param name="lab_root_domain"/>
		<xsl:param name="lab_root_folders"/>
		<xsl:param name="lab_root_training_center"/>
		<xsl:param name="lab_root_skill"/>
		<xsl:param name="lab_organizational"/>
		<xsl:param name="lab_global"/>
		<xsl:param name="lab_close"/>
		<xsl:param name="lab_move_objective"/>
		<xsl:param name="lab_all_skillset"/>
		<xsl:choose>
			<xsl:when test="$tree_title !=''"><xsl:value-of select="$tree_title"/></xsl:when>
			<xsl:otherwise>
				<xsl:choose>	
					<xsl:when test="$tree_type='user_group' or $tree_type='user_group_and_user'"><xsl:value-of select="$lab_root_user_group"/></xsl:when>
					<xsl:when test="$tree_type='grade' or $tree_type='grade_and_user'"><xsl:value-of select="$lab_root_grade"/></xsl:when>
					<xsl:when test="$tree_type='industry' or $tree_type='industry_and_user'"><xsl:value-of select="$lab_root_industry"/></xsl:when>
					<xsl:when test="$tree_type='competence'"><xsl:value-of select="$lab_root_competence"/></xsl:when>
					<xsl:when test="$tree_type='user_group_and_user'"><xsl:value-of select="$lab_root_user_group_and_user"/></xsl:when>
					<xsl:when test="$tree_type='catalog' or $tree_type='tc_catalog' or $tree_type='TC_CATALOG'"><xsl:value-of select="$lab_root_catalog"/></xsl:when>			
					<xsl:when test="$tree_type='knowledge_object'"><xsl:value-of select="$lab_root_knowledge_object"/></xsl:when>
					<xsl:when test="$tree_type='item' or $tree_type='tc_catalog_item' or $tree_type='tc_catalog_item_and_run' or $tree_type='tc_catalog_item_integrated' or $tree_type='TC_CATALOG_ITEM_SELF'"><xsl:value-of select="$lab_root_item"/></xsl:when>
					<xsl:when test="$tree_type='item_from_catalog'"><xsl:value-of select="$lab_root_domain"/></xsl:when>
					<xsl:when test="$tree_type='km_domain_and_object' or $tree_type = 'km_work_folder_and_object'"><xsl:value-of select="$lab_root_domain"/></xsl:when>
					<xsl:when test="$tree_type='SYLLABUS_AND_OBJECT'or $tree_type='syllabus_and_object'or $tree_type = 'move_objective' or $tree_type='TC_SYLLABUS_AND_OBJECT'"><xsl:value-of select="$lab_root_objective"/></xsl:when>
					<xsl:when test="$tree_type='training_center' or $tree_type='nav_training_center' or $tree_type='nav_training_center_for_ta' or $tree_type='tc_catalog_item_run' or $tree_type='TC_CATALOG_ITEM_AND_RUN'"><xsl:value-of select="$lab_root_training_center"/></xsl:when>
					<xsl:when test="$tree_type='COMPETENCE_PROFILE_AND_SKILL'"><xsl:value-of select="$lab_root_skill"/></xsl:when>
					<xsl:when test="$tree_type='COMPETENCE_PROFILE'"><xsl:value-of select="$lab_all_skillset"/></xsl:when>
					<xsl:when test="$tree_type='my_staff'"><xsl:value-of select="$lab_appr_root"/></xsl:when>
					<xsl:when test="$tree_type='TC_CATALOG_ITEM_BATCHUPDATE'"><xsl:value-of select="$lab_root_training_center"/></xsl:when>
					<xsl:otherwise><xsl:value-of select="$tree_title"/></xsl:otherwise>
				</xsl:choose>
			</xsl:otherwise>	
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>	