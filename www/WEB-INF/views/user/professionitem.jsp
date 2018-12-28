<%@ page isELIgnored="false" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<script type="text/javascript"
	src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript">
	var colModel = [
			{
				name : '',
				display : getLabel('79'),
				width : 670,
				align : 'left',
				format : function(data) {
					itm_icon = data.itm_icon;
					if (itm_icon == '' || itm_icon == undefined) {
						itm_icon = '/static/images/course1.png';
					} else {
						itm_icon = '/item/' + data.itm_id + '/' + itm_icon;
					}
					p = {
						itm_icon : itm_icon,
						itm_title : data.itm_title,
						itm_desc : data.itm_desc,
						title_link : '${ctx}/app/course/detail/' + data.itm_id
					};
					return $('#titleTemplate').render(p);
				}
			},
			{
				name : '',
				display : getLabel('PFS01'),
				width : 210,
				align : 'left',
				format : function(data) {
					var type_str = '';
					if (data.itr_group_ind == 0 && data.itr_grade_ind == 0
							&& data.itr_position_ind == 0) {
						type_str = '<div class="margin-top30"><lb:get key="recommend_grade"/></div>';
					} else if (data.itr_group_ind == 1) {
						type_str = '<div class="margin-top30"><lb:get key="recommend_group"/></div>';
					} else if (data.itr_grade_ind == 1) {
						type_str = '<div class="margin-top30"><lb:get key="recommend_grade"/></div>';
					} else if (data.itr_position_ind == 1) {
						type_str = '<div class="margin-top30"><lb:get key="recommend_position"/></div>';
					}
					return type_str;
				}
			}, {
				name : '',
				display : getLabel('997'),
				width : 170,
				align : 'left',
				format : function(data) {
					if (data.att_ats_id == 0 || data.att_ats_id == undefined) {
						return '<div class="margin-top30"><lb:get key="status_notapp"/></div>';
					} else if (data.att_ats_id == 1) {
						return '<div class="margin-top30"><lb:get key="status_completed"/></div>';
					} else if (data.att_ats_id == 2) {
						return '<div class="margin-top30"><lb:get key="status_inprogress"/></div>';
					}
					return '';
				}
			}, {
				name : '',
				display : getLabel('1004'),
				width : 83,
				format : function(data) {
					var handler = '${ctx}/app/course/detail/' + data.itm_id;
					att_ats_id = data.att_ats_id;
					app_tkh_id = data.app_tkh_id;
					cos_res_id = data.cos_res_id;
					if (att_ats_id == 0 || att_ats_id == undefined) {
						name = '<lb:get key="btn_enrollment"/>';
					} else if (att_ats_id == 1) {
						name = '<lb:get key="module_review"/>';
					} else if (att_ats_id == 2) {
						name = '<lb:get key="menu_study"/>';
					}
					p = {
						name : name,
						handler : handler
					};
					return $('#btnTemplate').render(p);
				}
			} ];

	$(document).ready(function() {
						var pfs_ugr_id = $("#pfs_ugr_id")[0].value;
						var pfs_id = $("#pfs_id")[0].value;
						$("#dp").table(
								{
									url : '${ctx}/app/profession/professionitem/json?pfs_ugr_id='
													+ pfs_ugr_id
													+ '&pfs_id='
													+ pfs_id,
									colModel : colModel
								}
						);
					});
</script>


<!-- templates -->
<script id="titleTemplate" type="text/x-jquery-tmpl">
			  <img class="fwpic" src="${ctx}{{>itm_icon}}">
<div class="xyd-path-first clearfix">
      <a class="fwim pull-left" href="{{>title_link}}">
           <div class="main_img">
                <img class="fwpic" src="{{>itm_icon}}">
                <div class="show">
                      <span class="imgArea">
                            <em><lb:get key='index_click_to_study'/></em>
                       </span>
                </div>
           </div>
      </a>

	 <div style="font-size:16px; line-height:24px; margin-left:20px">
		<a class="xyd-path-link" href="{{>title_link}}">{{>itm_title}}</a>
	 </div>
</div>
</script>

<script id="btnTemplate" type="text/x-jquery-tmpl">
<a class="btn wzb-btn-yellow wzb-btn-big margin-top30" href="{{>handler}}">{{>name}}</a>
</script>

<script id="numberTemplate" type="text/x-jquery-tmpl">
<div style="text-align:center;">
{{>app_count}}
</div>
</script>

<script id="timeTemplate" type="text/x-jquery-tmpl">
{{>itm_publish_timestamp}}
</script>
<!-- templates -->
</head>
<body>


	<div class="xyd-wrapper">
		<div class="xyd-main clearfix">
			<div class="wzb-model-13">
				<div class="wzb-title-10">
					<a class="wzb-link01" href="javascript: go('${ctx}/app/profession');" title=""><lb:get key="menu_profession"/></a>
					&gt; <span>${cur_pfs_title }</span>
				</div>

				<div>
					<input type="hidden" id="pfs_ugr_id" value="${cur_pfs_ugr_id }" />
					<input type="hidden" id="pfs_id" value="${cur_pfs_id }" />
				</div>
				
				<div role="tabpanel" class="wzb-tab-5 margin-top15">
					<ul class="nav nav-tabs" role="tablist">
						<c:forEach items="${ugr_id_lst }" var="ugr_id" varStatus="status">
							<li role="presentation" class="${cur_pfs_ugr_id eq ugr_id ? 'active' : ''}"
								onclick="javascript: go('${ctx}/app/profession/professionitem?pfs_ugr_id=${ugr_id }&pfs_id=${cur_pfs_id }');">
								<a href="#deputy" aria-controls="deputy" role="tab" data-toggle="tab">${cur_usr_ugr_id == ugr_id ? '★' : '' }${ugr_title_lst[status.index] }</a>
							</li>
						</c:forEach>
					</ul>

					<div class="tab-content">
						<div role="tabpanel" class="tab-pane active wzb-profess" id="review">
							 <div id="dp"></div>
							 <!-- wzb-page end -->
						</div>
					</div>
				</div>
			</div>

		</div>
	</div>
</body>
</html>