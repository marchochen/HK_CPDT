<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/sns.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/jquery.sns.js"></script>
<script type="text/javascript" src="${ctx}/js/wb_evaluation.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang }/label_bdm_${lang }.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/course.js"></script>
<title></title>

</head>
<body>
	

<div class="xyd-wrapper">
<div class="xyd-main clearfix">
<div class="xyd-sidebar">
<h3 class="wzb-title-4 skin-bg">
<!-- 讲师风采 --><lb:get key="label_bdm.label_core_basic_data_management_1"/>
<i class="fa fa-sanzuo fa-caret-up"></i></h3>

<div class="wzb-model-7">
	<h3 class="wzb-title-2 skin-color">
	<!-- 筛选条件 --> <lb:get key="label_bdm.label_core_basic_data_management_39"/>
	</h3>
    <dl class="wzb-list-4" id="instrLevel">
        <dt>
<!--         讲师级别 -->
       		<lb:get key="label_bdm.label_core_basic_data_management_3"/>：
        </dt>
        <dd><a href="javascript:;" data=""><i class="fa margin-right10 fa-circle skin-color"></i>
<!--         全部 --><lb:get key="label_bdm.label_core_basic_data_management_4"/>
        </a></dd>
        <dd><a href="javascript:;" data="J"><i class="fa margin-right10 fa-circle-thin"></i>
<!--         初级 --><lb:get key="label_bdm.label_core_basic_data_management_5"/>
        </a></dd>
        <dd><a href="javascript:;" data="M"><i class="fa margin-right10 fa-circle-thin"></i>
<!--         中级 --><lb:get key="label_bdm.label_core_basic_data_management_6"/>
        </a></dd>
        <dd><a href="javascript:;" data="S"><i class="fa margin-right10 fa-circle-thin"></i>
<!--         高级 --><lb:get key="label_bdm.label_core_basic_data_management_7"/>
        </a></dd>
        <dd><a href="javascript:;" data="D"><i class="fa margin-right10 fa-circle-thin"></i>
<!--         特聘 --><lb:get key="label_bdm.label_core_basic_data_management_8"/>
        </a></dd>
    </dl> 
       
    <dl class="wzb-list-4" id="instrType">
        <dt>
<!--         讲师类型： -->
       		<lb:get key="label_bdm.label_core_basic_data_management_9"/>：
        </dt>
        <dd><a href="javascript:;" data=""><i class="fa margin-right10 fa-circle skin-color"></i>
<!--         全部 --><lb:get key="label_bdm.label_core_basic_data_management_4"/>
        </a></dd>
        <dd><a href="javascript:;"  data="P"><i class="fa margin-right10 fa-circle-thin"></i>
<!--         兼职 --><lb:get key="label_bdm.label_core_basic_data_management_11"/>
        </a></dd>
        <dd><a href="javascript:;"  data="F"><i class="fa margin-right10 fa-circle-thin"></i>
<!--         专职 --><lb:get key="label_bdm.label_core_basic_data_management_10"/>
        </a></dd>
    </dl>
    
    <dl class="wzb-list-4" id="instrFrom">
        <dt>
<!--         讲师来源： -->
       		<lb:get key="label_bdm.label_core_basic_data_management_12"/>：
        </dt>
        <dd><a href="javascript:;" data=""><i class="fa margin-right10 fa-circle skin-color"></i>
<!--         全部--><lb:get key="label_bdm.label_core_basic_data_management_4"/>
        </a></dd>
        <dd><a href="javascript:;"  data="IN"><i class="fa margin-right10 fa-circle-thin"></i>
<!--         内部讲师 --><lb:get key="label_bdm.label_core_basic_data_management_13"/>
        </a></dd>
        <dd><a href="javascript:;"  data="EXT"><i class="fa margin-right10 fa-circle-thin"></i>
<!--         外部讲师 --><lb:get key="label_bdm.label_core_basic_data_management_14"/>
        </a></dd>
    </dl>
</div>
</div> <!-- xyd-sidebar End-->

<div class="xyd-article">
<div class="wzb-title-2">
<!-- 讲师列表 --><lb:get key="label_bdm.label_core_basic_data_management_2"/>
</div>

<span id="instrList"></span>
             
</div> <!-- xyd-article End-->
</div>
</div> <!-- xyd-wrap End-->
<script type="text/javascript">
var len = 130;
var dt;
$(function(){
	dt = $("#instrList").table({
		url : '${ctx}/app/instr/page',
		colModel : colModel,
		rp : globalPageSize,
		showpager : 10,
		hideHeader : true,
		usepager : true,
		params : {
		},
		onSuccess : function(data){

		}
	});

	var iti_level = "", iti_type = "", iti_type_mark = "";
	$("#instrLevel dd a").click(function() {
		var _this = this;
		var data = iti_level = $(_this).attr("data");
		var eleI = $(_this).find("i");
		if($(eleI).hasClass("fa-circle")) {
			$(eleI).toggleClass("fa-circle-thin");
			$(eleI).removeClass("fa-circle");
		} else {
			$(eleI).addClass("fa-circle").removeClass("fa-circle-thin").parents("dd").siblings().find("i").removeClass("fa-circle").addClass("fa-circle-thin");
		}

		$(dt).reloadTable({
			params : {
				iti_level : data,
				iti_type : iti_type,
				iti_type_mark : iti_type_mark
			}
		})
	})

	$("#instrType dd a").click(function() {
		var _this = this;
		var data = iti_type =  $(_this).attr("data");
		var eleI = $(_this).find("i");
		if($(eleI).hasClass("fa-circle")) {
			$(eleI).toggleClass("fa-circle-thin");
			$(eleI).removeClass("fa-circle");
		} else {
			$(eleI).addClass("fa-circle").removeClass("fa-circle-thin").parents("dd").siblings().find("i").removeClass("fa-circle").addClass("fa-circle-thin");
		}

		$(dt).reloadTable({
			params : {
				iti_type : data,
				iti_level : iti_level,
				iti_type_mark : iti_type_mark
			}
		})
	})

	$("#instrFrom dd a").click(function() {
		var _this = this;
		var data = iti_type_mark =  $(_this).attr("data");
		var eleI = $(_this).find("i");
		if($(eleI).hasClass("fa-circle")) {
			$(eleI).toggleClass("fa-circle-thin");
			$(eleI).removeClass("fa-circle");
		} else {
			$(eleI).addClass("fa-circle").removeClass("fa-circle-thin").parents("dd").siblings().find("i").removeClass("fa-circle").addClass("fa-circle-thin");
		}

		$(dt).reloadTable({
			params : {
				iti_type_mark : data,
				iti_level : iti_level,
				iti_type : iti_type
			}
		})
	})
})
var colModel = [ {
		format : function(data) {
			data.iti_star = cwn.getStar(data.iti_score);
			data.iti_type_str = data.iti_type == "F" ? cwn.getLabel('label_core_basic_data_management_10') : cwn.getLabel('label_core_basic_data_management_11');
			data.iti_level_str = cwn.getInstructorLevel(data.iti_level);
			var is_desc_sub;
			var iti_desc_sub = data.iti_expertise_areas ? substr(data.iti_expertise_areas, 0, len) : "";
			if(data.iti_expertise_areas == undefined || getChars(data.iti_expertise_areas) < len) {
				 is_desc_sub = 'display:none';
			}
			data.iti_desc_sub = iti_desc_sub;
			data.is_desc_sub = is_desc_sub;
			data.enc_iti_ent_id = wbEncrytor().cwnEncrypt(data.iti_ent_id);
			return $('#instrTemplate').render(data);
		}
	}];
</script>
<script id="instrTemplate" type="text/x-jsrender">
	<dl class="wzb-list-18">
	    <dd><a title="" href="${ctx}/app/instr/detail/{{:enc_iti_ent_id}}"><img src="${ctx}{{:iti_img}}"></a>
		</dd>
	    <dt>
	        <p><a class="wzb-link04" href="${ctx}/app/instr/detail/{{:enc_iti_ent_id}}" title="">{{:iti_name}}</a></p>
	         {{if iti_recommend}}
			 <!-- <span style="background:#ff8c00;"><lb:get key="label_bdm.label_core_basic_data_management_28"/></span> -->
              <div class="tuijian-icon" style="float:right;color:#29E236;"><lb:get key="label_bdm.label_core_basic_data_management_28"/></div>
			{{/if}}

            <div class="offheight clearfix">
              <div class="offwidth">
                <span class="color-gray999"><lb:get key="label_bdm.label_core_basic_data_management_45"/>：</span>
				{{:iti_star}}
             </div>
             <div class="offwidth"><span class="color-gray999"><lb:get key="label_bdm.label_core_basic_data_management_12"/>：</span>
                {{if iti_type_mark == 'IN'}}
                      <lb:get key="label_bdm.label_core_basic_data_management_13"/>
                {{else}}
                      <lb:get key="label_bdm.label_core_basic_data_management_14"/>
                {{/if}}
             </div>
            </div>	

	        <div class="offheight clearfix"><div class="offwidth"><span class="color-gray999"><lb:get key="label_bdm.label_core_basic_data_management_3"/>：</span>{{:iti_level_str}}</div> <div class="offwidth"><span class="color-gray999"><lb:get key="label_bdm.label_core_basic_data_management_9"/>：</span>{{:iti_type_str}}</div></div>
	        <p><span class="color-gray999"><lb:get key="label_bdm.label_core_basic_data_management_26"/>：</span>
				<span data="{{>iti_expertise_areas}}">{{>iti_desc_sub}}</span>
				<a class="wzb-show skin-color open_desc" style="{{>is_desc_sub}}" >
					<span><lb:get key="click_down"/></span>
					<i class="fa fa-angle-down"></i>
				</a>
			</p> 
	    </dt>
	</dl>
</script>
</body>
</html>