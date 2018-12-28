<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:import href="../utils/wb_gen_form_button.xsl"/>
<xsl:variable name="img_path"><xsl:value-of select="$wb_img_path"/></xsl:variable>
	<!-- ====================================================================== -->
	<xsl:template name="tree_js" match="tree_list_js">
		<xsl:param name="lab_tree_root"/>
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
		<xsl:param name="lab_root_lib_catalogue"/>
		<xsl:param name="lab_root_domain"/>
		<xsl:param name="lab_root_folders"/>
		<xsl:param name="lab_organizational"/>
		<xsl:param name="lab_global"/>
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_step_1"/>
		<xsl:param name="lab_next"/>
		<xsl:param name="lab_cancel"/>
		<xsl:param name="lab_count_suffix"/>
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<xsl:call-template name="new_css"/>
			<script language="JavaScript" src="{$wb_js_path}urlparam.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_objective.js"/>
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
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="pick_js_function"/>
			<script language="JavaScript" type="text/javascript"><![CDATA[
			
		goldenman = new wbGoldenMan;
		
		tree_type              = getUrl('tree_type')?getUrlParam('tree_type'):''                       ;
		pick_method            = getUrl('pick_method')?getUrlParam('pick_method'):''                   ;
		js                     = getUrl('js')?getUrlParam('js'):''                                     ;
		item_type_list         = getUrl('item_type_list')?getUrlParam('item_type_list'):''             ;
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
		parent_tcr_id          = getUrl('parent_tcr_id')?getUrlParam('parent_tcr_id'):''               ;
		ftn_ext_id             = getUrl('ftn_ext_id')?getUrlParam('ftn_ext_id'):''                     ;
		
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
		alert("parent_tcr_id       " + parent_tcr_id       );		
		alert("ftn_ext_id          " + ftn_ext_id          );		
		*/
				
		//var tree_attrib = ["obj_tree","name","id","title","type","src","checkbox", "radio", "rootbox", "nodebox"];
		
		var jstree = Array();
		]]><xsl:apply-templates select="/tree/tree_list_js/tree" mode="loadxmltree">
					<xsl:with-param name="tree_type" select="@type"/>
					<xsl:with-param name="lab_tree_root" select="$lab_tree_root"/>
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
					<xsl:with-param name="lab_root_lib_catalogue" select="$lab_root_lib_catalogue"/>
					<xsl:with-param name="lab_root_domain" select="$lab_root_domain"/>
					<xsl:with-param name="lab_root_folders" select="$lab_root_folders"/>
					<xsl:with-param name="lab_organizational" select="$lab_organizational"/>
					<xsl:with-param name="lab_global" select="$lab_global"/>
					<xsl:with-param name="lab_desc" select="$lab_desc"/>
					<xsl:with-param name="lab_step_1" select="$lab_step_1"/>
					<xsl:with-param name="lab_next" select="$lab_next"/>
					<xsl:with-param name="lab_cancel" select="$lab_cancel"/>
					<xsl:with-param name="lab_count_suffix" select="$lab_count_suffix"/>						
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

			}
		}
		
		function page_onload(){
			var div_width = document.body.clientWidth/jstree.length;
			for(i=0;i<jstree.length;i++){
					var obj_div = document.getElementById(jstree[i]["name"]);
					obj_div.style.width = div_width+'px';
					obj_div.innerHTML=jstree[i]["obj_tree"].toHtml();
			}			
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
					if(opt[m].split(",")[0]<=0){ //The selected node is root
						return false;
					}
					str += "~%~"+String(opt[m]).replace(",","~%~") + "~%~TREE" ;
				}
				param[j] = jstree[j]["type"] +  str;
			}
			
			var str_fun = "pick_ok('" + js + "'" ;
			for(n=0;n<param.length;n++){
				str_fun += ",'" + param[n] + "'" ;
			}
			str_fun = str_fun + ")";
			//alert(str_fun);
			eval(str_fun);
			
				
		}
		
		function getNodeSrc(tree_type,node_id){
			var srcurl = "";
			for(i=0;i<jstree.length;i++){
				//alert(jstree[i]["type"] + " " + tree_type.toLowerCase());
				if(jstree[i]["type"]==tree_type.toLowerCase()){
					srcurl = jstree[i]["src"] + "&node_id=" + node_id;
				}
			}
//			alert(tree_type + "  url:" + srcurl);
			return srcurl;
		}		
		
		
		]]></script>
		</head>	
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="page_onload()">
			<table cellpadding="0" cellspacing="0" border="0" bgcolor="white" width="{$wb_gen_table_width}">
				<tr>
					<td>
						<xsl:call-template name="wb_ui_title">
							<xsl:with-param name="width">100%</xsl:with-param>
							<xsl:with-param name="text" select="$lab_step_1"/>
						</xsl:call-template>
						<xsl:call-template name="wb_ui_desc">
							<xsl:with-param name="text" select="$lab_desc"/>
						</xsl:call-template>
					</td>
				</tr>
			</table>
			
			<table cellpadding="0" cellspacing="0" border="0" style="padding-top:2px;">
				<tr>
					<xsl:apply-templates select="/tree/tree_list_js/tree" mode="div">
					
					</xsl:apply-templates>
				</tr>
			</table>
			<table cellpadding="3" cellspacing="0" border="0" align="center">
				<tr>
						<td height="10">
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</td>
					</tr>
				<tr>
					<td align="center" height="19" valign="middle">
						<xsl:call-template name="wb_gen_form_button">
							<xsl:with-param name="wb_gen_btn_name" select="$lab_next"/>
							<xsl:with-param name="wb_gen_btn_href">Javascript:returnSelectValues();</xsl:with-param>
						</xsl:call-template>
						<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
						<xsl:call-template name="wb_gen_form_button">
							<xsl:with-param name="wb_gen_btn_name" select="$lab_cancel"/>
							<xsl:with-param name="wb_gen_btn_href">Javascript:pick_cancel();</xsl:with-param>
						</xsl:call-template>
					</td>
				</tr>
			</table>
		</body>
	</xsl:template>
	<!-- ====================================================================== -->
	<xsl:template match="tree" mode="loadxmltree">
		<xsl:param name="tree_type"/>
		<xsl:param name="lab_tree_root"/>
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
		<xsl:param name="lab_root_lib_catalogue"/>
		<xsl:param name="lab_root_domain"/>
		<xsl:param name="lab_root_folders"/>
		<xsl:param name="lab_organizational"/>
		<xsl:param name="lab_global"/>
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_step_1"/>
		<xsl:param name="lab_next"/>
		<xsl:param name="lab_cancel"/>
		<xsl:param name="lab_count_suffix"/>
		tree_title = "<xsl:call-template name="tree_title">
			<xsl:with-param name="tree_type" select="@type"/>
			<xsl:with-param name="tree_title" select="@title"/>
			<xsl:with-param name="lab_tree_root" select="$lab_tree_root"/>
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
			<xsl:with-param name="lab_root_lib_catalogue" select="$lab_root_lib_catalogue"/>
			<xsl:with-param name="lab_root_domain" select="$lab_root_domain"/>
			<xsl:with-param name="lab_root_folders" select="$lab_root_folders"/>
			<xsl:with-param name="lab_organizational" select="$lab_organizational"/>
			<xsl:with-param name="lab_global" select="$lab_global"/>
			<xsl:with-param name="lab_desc" select="$lab_desc"/>
			<xsl:with-param name="lab_step_1" select="$lab_step_1"/>
			<xsl:with-param name="lab_next" select="$lab_next"/>
			<xsl:with-param name="lab_cancel" select="$lab_cancel"/>
			<xsl:with-param name="lab_count_suffix" select="$lab_count_suffix"/>		
		</xsl:call-template>";
		
		index = <xsl:value-of select="position()-1"/>;
		jstree[index] = {};
		jstree[index]["name"] = "tree_<xsl:value-of select="@type"/>";
		jstree[index]["srcid"] = "<xsl:value-of select="@srcid"/>";
		jstree[index]["title"] = tree_title; 
		tree_mode = <xsl:value-of select="@pick_method"/>;
		tree_root = (<xsl:value-of select="@pick_root"/>==1)?true:false;
		jstree[index]["type"] = "<xsl:value-of select="@type"/>";
		jstree[index]["src"] = goldenman.opentree('<xsl:value-of select="@type"/>',pick_method,js,item_type_list,pick_leave,approved_list,flag,close_option,pick_root,override_appr_usg, tree_subtype, get_supervise_group,complusory_tree,get_direct_supervise,ftn_ext_id,true,parent_tcr_id);
		
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
		//jstree[index]["checkbox"] = <xsl:value-of select="@checkbox"/>;
		//jstree[index]["radio"] = <xsl:value-of select="@radio"/>;
		//jstree[index]["rootbox"] = <xsl:value-of select="@rootbox"/>;
		//jstree[index]["nodebox"] = <xsl:value-of select="@nodebox"/>;
	</xsl:template>
	<!-- ====================================================================== -->
	<xsl:template match="tree" mode="div">
		<td valign="top">
			<div id="tree_{@type}" class="Bg" style="height:320px;border :1px solid Silver;; overflow:auto;padding-left: 2px;"/>
		</td>
	</xsl:template>
	<!-- ====================================================================== -->
	<xsl:template name="tree_title">
		<xsl:param name="tree_type"/>
		<xsl:param name="tree_title"/>
		<xsl:param name="lab_tree_root"/>
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
		<xsl:param name="lab_root_lib_catalogue"/>
		<xsl:param name="lab_root_domain"/>
		<xsl:param name="lab_root_folders"/>
		<xsl:param name="lab_organizational"/>
		<xsl:param name="lab_count_suffix"/>
		<xsl:choose>	
			<xsl:when test="$tree_type='user_group'"><xsl:value-of select="$lab_root_user_group"/></xsl:when>
			<xsl:when test="$tree_type='grade'"><xsl:value-of select="$lab_root_grade"/></xsl:when>
			<xsl:when test="$tree_type='industry'"><xsl:value-of select="$lab_root_industry"/></xsl:when>
			<xsl:when test="$tree_type='competence'"><xsl:value-of select="$lab_root_competence"/></xsl:when>
			<xsl:when test="$tree_type='user_group_and_user'"><xsl:value-of select="$lab_root_user_group_and_user"/></xsl:when>
			<xsl:when test="$tree_type='catalog'"><xsl:value-of select="$lab_root_catalog"/></xsl:when>			
			<xsl:when test="$tree_type='knowledge_object'"><xsl:value-of select="$lab_root_knowledge_object"/></xsl:when>
			<xsl:when test="$tree_type='syllabus_and_object'"><xsl:value-of select="$lab_tree_root"/></xsl:when>
			<xsl:when test="$tree_type='item'"><xsl:value-of select="$lab_root_item"/></xsl:when>
			<xsl:when test="$tree_type='item_from_catalog'"><xsl:value-of select="$lab_root_domain"/></xsl:when>
			<xsl:when test="$tree_type='km_domain_and_object'"><xsl:value-of select="$lab_root_domain"/></xsl:when>
			<xsl:otherwise><xsl:value-of select="$tree_title"/></xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>	