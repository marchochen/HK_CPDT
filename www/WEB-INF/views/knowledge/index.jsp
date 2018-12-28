<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery.prompt.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/sns.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/jquery.sns.js"></script>
<title></title>
<script type="text/javascript">
	var sns = new Sns();
	var knowledge = '';
	var kbc_id = '';
	var tag_id = '';
	var kbi_type = '';
	$(function() {
		getListKnowledge('rankingKnowledge', 'latest');//最新知识排行
		getListKnowledge('my_share', 'my_share'); //我的分享
		getListKnowledge('recentView', 'recentView');//最近浏览

		$.getJSON('${ctx}/app/kb/catalog/admin/getCatalogJson',
				function(rData) {
					if(rData.length > 0){
						var str = '<a class="cur" title="'+fetchLabel('lab_STATUS_ALL')+'" href="#" onclick="changeCatalog(this,\'\',\'CATALOG\')">'+fetchLabel('lab_STATUS_ALL')+'</a>';
						$.each(rData, function(index, obj) {
							var p = {
								id : obj.kbc_id,
								title : obj.kbc_title,
								type : 'CATALOG'
							}
							str += $('#a-template').render(p);
						})
						$('#norm-list-1-info').prepend(str);
						$("#norm-list-1-info a:gt(5)").hide();
						$("#norm-list-1-info #norm-more-catalog").hide();
						$("#norm-list-1-info #norm-less-catalog").hide();
						if($("#norm-list-1-info").children("a").length>6){
							$("#norm-list-1-info #norm-more-catalog").show();
						}
						$("#norm-list-1-info #norm-more-catalog").click(function() {
							$("#norm-list-1-info a:gt(5)").show();
							$("#norm-list-1-info #norm-more-catalog").hide();
							$("#norm-list-1-info #norm-less-catalog").show();
						});
						$("#norm-list-1-info #norm-less-catalog").click(function() {
							$("#norm-list-1-info a:gt(5)").hide();
							$("#norm-list-1-info #norm-more-catalog").show();
							$("#norm-list-1-info #norm-less-catalog").hide();
						})
						$("#norm-more-type-dl").addClass("wzb-border-bottom");
					}else{
						$("#norm-more-catalog-dl").hide();
					}
				});

		$.getJSON('${ctx}/app/kb/tag/admin/getTagJson', function(rData) {
			if(rData.length > 0){
				var str = '<a class="cur" title="'+fetchLabel('lab_STATUS_ALL')+'" href="#" onclick="changeCatalog(this,\'\',\'TAG\')">'+fetchLabel('lab_STATUS_ALL')+'</a>';
				$.each(rData, function(index, obj) {
					var p = {
						id : obj.tag_id,
						title : obj.tag_title,
						type : 'TAG'
					}
					str += $('#a-template').render(p);
				})
				$('#norm-list-2-info').prepend(str);
				$("#norm-list-2-info a:gt(5)").hide();
				$("#norm-list-2-info #norm-more-tag").hide();
				$("#norm-list-2-info #norm-less-tag").hide();
				if($("#norm-list-2-info").children("a").length>6){
					$("#norm-list-2-info #norm-more-tag").show();
				}
				$("#norm-list-2-info #norm-more-tag").click(function() {
					$("#norm-list-2-info a:gt(5)").show();
					$("#norm-list-2-info #norm-more-tag").hide();
					$("#norm-list-2-info #norm-less-tag").show();
				})
				$("#norm-list-2-info #norm-less-tag").click(function() {
					$("#norm-list-2-info a:gt(5)").hide();
					$("#norm-list-2-info #norm-more-tag").show();
					$("#norm-list-2-info #norm-less-tag").hide();
				})
				$("#norm-more-type-dl").addClass("wzb-border-bottom");
				$("#norm-more-catalog-dl").addClass("wzb-border-bottom");
			}else{
				$("#norm-more-tag-dl").hide();
			}
		});
		var knowledgeModel = [ {
			format : function(data) {
				var p = {
					enc_kbi_id : wbEncrytor().cwnEncrypt(data.kbi_id),
				};
				return $('#knowledge-template').render(p);
			}
		} ];

		knowledge = $("#knowledgeList").table({
			url : '${ctx}/app/kb/center/index/indexJson',
			gridTemplate : function(data) {
				if (data.kbi_id == undefined) {
					return "<li><img src='${ctx}/static/theme/blue/images/nocourse.jpg'/></li>";
				} else {
					$.extend(
						data,
						{
							kbi_type : fetchLabel('lab_TYPE_' + data.kbi_type),
							enc_kbi_id : wbEncrytor().cwnEncrypt(data.kbi_id),
							kbi_desc : data.kbi_desc ? (data.kbi_desc.length > 23 ? data.kbi_desc.substring(0, 23) + '......' : data.kbi_desc) : '',
							kbi_desc_all : data.kbi_desc ? data.kbi_desc : '',
							kbi_title : data.kbi_title.length > 23 ? data.kbi_title.substring(0, 23) + '......' : data.kbi_title,
							kbi_title_all : data.kbi_title,
							kba_url : data.imageAttachment.kba_url
						})
					return $('#knowledge-template').render(data);
				}
			},
			rowCallback : function(data){
				$("#kbi_like_count_" + data.kbi_id).like({
					count : data.snsCount ? data.snsCount.s_cnt_like_count:0,
					flag : data.sns ? data.sns.like : false,
					id : data.kbi_id,
					module: 'Knowledge',
					tkhId : 0
				})
				$("#kbi_collect_count_" + data.kbi_id).collect({
					count : data.snsCount?data.snsCount.s_cnt_collect_count:0,
					flag : data.sns?data.sns.collect : false,
					id : data.kbi_id,
					module: 'Knowledge',
					tkhId : 0
				})
				if(${sns_enabled} == true){
					$("#kbi_share_count_" + data.kbi_id).share({
						count : data.snsCount?data.snsCount.s_cnt_share_count:0,
						flag : data.sns?data.sns.share : false,
						id : data.kbi_id,
						module: 'Knowledge',
						tkhId : 0,
						width : $("#kbi_share_count_" + data.kbi_id).width()/2
					})
				} else {
					$("#kbi_share_count_" + data.kbi_id).remove();
				}
			},
			view : 'grid',
			rowSize : '3',
			rp : 9,
			params : {
				sortname : 'kbi_access_count',
				sortorder : 'desc'
			},
			hideHeader : true,
			usepager : true
		});

		$("#sortul li").click(
				function() {
					//alert($(this).index());
					var sortname = $(this).attr("id");
					var sortorder = $(this).attr("data");
					if (sortorder == 'asc') {
						$(this).attr("data", "desc");
					} else if (sortorder == 'desc') {
						$(this).attr("data", "asc");
					}
					//css 切换
					$(this).addClass("active").siblings().removeClass("active");
					if ($(this).children("a").children("i").hasClass("fa-caret-up")) {
						$(this).children("a").children("i").addClass("fa-caret-down")
								.removeClass("fa-caret-up");
					} else {
						$(this).children("a").children("i").addClass("fa-caret-up")
								.removeClass("fa-caret-down");
					}

					$(knowledge).reloadTable({
						url : '${ctx}/app/kb/center/index/indexJson',
						params : {
							kbc_id : kbc_id,
							tag_id : tag_id,
							kbi_type : kbi_type,
							sortname : sortname,
							sortorder : sortorder
						}
					});
				});

		$(".norm-share").click(function() {
			$(this).find(".norm-share-info").toggle();
		});

	})

	function getListKnowledge(id, type) {
		var p = '?page=1&rp=11&t' + new Date().getTime();
		var dataUrl = '${ctx}/app/kb/center/index/' + type;
		$.getJSON(dataUrl + p, function(rData) {
			var str = '';
			$.each(rData.rows, function(index, obj) {
				var cssClass = 'gray';
				if (index < 3) {
					cssClass = 'orange';
				}
				var p = {
					index : index + 1,
					cssClass : cssClass,
					kbi_id : obj.kbi_id,
					enc_kbi_id : wbEncrytor().cwnEncrypt(obj.kbi_id),
					kbi_title : obj.kbi_title,
					kbi_title_all : obj.kbi_title
				}
				if (type == 'recentView') {
					if (index < 5) {
						str += $('#li-template').render(p);
					}
				} else if(type == 'my_share'){
					if (index < 5) {
						str += $('#li-template').render(p);
					}
				} else {
					str += $('#ranking-template').render(p);
				}
			});
			if (str == '') {
				$('#' + id).html($('#blank-template').render());
			} else {
				$('#' + id).html(str);
			}
		})
	};

	function onClickRankingChang(thisObj, id, type) {
		$(thisObj).parent().children(".active").removeClass('active');
		$(thisObj).addClass('active');
		getListKnowledge(id, type);
	}

	//切换分类
	function changeCatalog(thisObj, value, type) {
		if (type == 'CATALOG') {
			kbc_id = value;
		} else if (type == 'TAG') {
			tag_id = value;
		} else if (type == 'ITEM') {
			kbi_type = value;
		}
		$("#" + $(thisObj).parent().attr("id") + " .cur")
				.removeClass("cur");
		$(thisObj).addClass("cur");
		var sortname = $('#sortul .active').attr("id");
		var sortorder = $('#sortul .active').attr("data");
		if (sortorder == 'asc') {
			sortorder = 'desc'
		} else {
			sortorder = 'asc'
		}
		$(knowledge).reloadTable({
			url : '${ctx}/app/kb/center/index/indexJson',
			params : {
				kbc_id : kbc_id,
				tag_id : tag_id,
				kbi_type : kbi_type,
				sortname : sortname,
				sortorder : sortorder

			}
		});
	}
</script>
<script id="ranking-template" type="text/x-jsrender">
<li>
	<a href="${ctx}/app/kb/center/view?source=index&enc_kbi_id={{>enc_kbi_id}}" title="{{>kbi_title_all}}">
		<em class="wzb-list-{{>cssClass}}">{{>index}}</em>
		<span class="color-gray333">{{>kbi_title}}</span>
	</a>
</li>
</script>
<script id="li-template" type="text/x-jsrender">
<li>
	 <a href="${ctx}/app/kb/center/view?source=index&enc_kbi_id={{>enc_kbi_id}}" title="{{>kbi_title_all}}">{{>kbi_title}}</a>
</li>
</script>
<script id="a-template" type="text/x-jsrender">
	<a title="{{>title}}" href="#" onclick="changeCatalog(this,{{>id}},'{{>type}}')">{{>title}}</a>
</script>
<script id="knowledge-template" type="text/x-jsrender">
<li>
	 <div class="norm-fwim">
		  <a href="${ctx}/app/kb/center/view?source=index&enc_kbi_id={{>enc_kbi_id}}"><img class="norm-fwim-pic" src="${ctx}{{>kba_url}}"/></a>
		  <a href="${ctx}/app/kb/center/view?source=index&enc_kbi_id={{>enc_kbi_id}}" class="norm-fwim-white" title="{{>kbi_title}}">{{>kbi_title}}</a>

		  <div class="norm-fwim-desc">
			   <a class="norm-fwim-gray" href="${ctx}/app/kb/center/view?source=index&enc_kbi_id={{>enc_kbi_id}}" title="{{>kbi_desc}}">{{>kbi_desc}}</a>
			   <div class="norm-fwim-area">
					<!--<a id="kbi_like_count_{{>kbi_id}}" class="margin-right15 wzb-link05" href="javascript:;" title='<lb:get key="sns_like"/>'><i class="fa skin-color fa-thumbs-o-up"></i> <span>0</span></a>-->
					<a id="kbi_collect_count_{{>kbi_id}}" class="margin-right15 wzb-link05" href="javascript:;" title='<lb:get key="sns_collect"/>'><i class="fa skin-color fa-star"></i> <span>0</span></a>
			   </div>

			   <div class="norm-fwim-info">
					{{>kbi_type}}
					<div class="pull-right" style="line-height:35px;"><lb:get key="lab_kb_from"/>：{{>usr_display_bil}}</div>
			   </div>
		  </div>
	 </div>
</li>
</script>
<script id="blank-template" type="text/x-jsrender">
<div class="losedata"><i class="fa fa-folder-open-o"></i><p><lb:get key="lab_table_empty"/></p></div>
</script>
</head>
<body>
	<div class="xyd-wrapper">
		<div id="main" class="xyd-main clearfix">
			<div class="xyd-sidebar">
                <dl class="xyd-share">
                    <dt><lb:get key="lab_kb_want_share" /></dt>
                    <dd class="xyd-share-info">
                         <a href="${ctx}/app/kb/center/insert?source=index&kbi_type=ARTICLE" title="">
                            <i class="fa fa-file-text-o"></i>
                            <lb:get key="lab_TYPE_ARTICLE" />
                        </a>
                        <a href="${ctx}/app/kb/center/insert?source=index&kbi_type=DOCUMENT" title="">
                            <i class="fa fa-file-word-o"></i>
                            <lb:get key="lab_TYPE_DOCUMENT" />
                        </a>
                        <a href="${ctx}/app/kb/center/insert?source=index&kbi_type=VEDIO" title="">
                            <i class="fa fa-file-video-o"></i>
                            <lb:get key="lab_TYPE_VEDIO" />
                        </a>
                        <a href="${ctx}/app/kb/center/insert?source=index&kbi_type=IMAGE" title="">
                            <i class="fa fa-file-image-o"></i>
                            <lb:get key="lab_TYPE_IMAGE" />
                        </a>
                    </dd>
                </dl>

                <div role="tabpanel" class="wzb-tab-3">
					<!-- Nav tabs -->
					<ul class="nav nav-tabs" role="tablist">
						<li role="presentation" class="active" onclick="onClickRankingChang(this,'rankingKnowledge','latest')"><a href="#waiting" aria-controls="waiting" role="tab" data-toggle="tab"><lb:get key="article_the_new" /></a></li>
						<li role="presentation" onclick="onClickRankingChang(this,'rankingKnowledge','hottest')"><a href="#passing" aria-controls="passing" role="tab" data-toggle="tab"><lb:get key="article_the_hottest" /></a></li>
						<%-- <li role="presentation" onclick="onClickRankingChang(this,'rankingKnowledge','popularity')"><a href="#refusal" aria-controls="refusal" role="tab" data-toggle="tab"><lb:get key="article_popularity" /></a></li> --%>
					</ul>

                    <div class="tab-content">
                         <div class="wzb-model-6">
                              <ul class="wzb-list-13 wzb-list-13-mc" id="rankingKnowledge">
                              </ul>
                         </div>
                    </div>
                </div>

                <h3 class="wzb-title-4 skin-bg"><a href="${ctx}/app/kb/center/list?tab=APPROVED" title='<lb:get key="global_more"/>' class="wzb-title-more"><lb:get key="global_more"/></a><lb:get key="lab_kb_my_share" /><i class="fa fa-sanzuo fa-caret-up"></i></h3>
                <ul class="wzb-list-22" id="my_share">
                </ul>

                <h3 class="wzb-title-4 skin-bg"><lb:get key="lab_kb_view_lattest" /> <i class="fa fa-sanzuo fa-caret-up"></i></h3>
                <ul class="wzb-list-22" id='recentView'>
                </ul>
			</div> <!-- xyd-sidebar End-->

			<div class="xyd-content">
				<div class="main">
                    <div class="wzb-model-4">
                         <dl class="wzb-list-9 clearfix" id="norm-more-type-dl">
                             <dt><lb:get key="global_kinds" />：</dt>
                             <dd id="kbi_type">
                                <a class="cur" title='<lb:get key="lab_STATUS_ALL"/>' href="###" onclick="changeCatalog(this,'','ITEM')">
									<lb:get key="lab_STATUS_ALL" />
								</a>
								<a title='<lb:get key="lab_TYPE_ARTICLE"/>' href="###" onclick="changeCatalog(this,'ARTICLE','ITEM')">
									<lb:get key="lab_TYPE_ARTICLE" />
								</a>
								<a href="###" title='<lb:get key="lab_TYPE_DOCUMENT"/>' onclick="changeCatalog(this,'DOCUMENT','ITEM')">
									<lb:get key="lab_TYPE_DOCUMENT" />
								</a>
								<a href="###" title='<lb:get key="lab_TYPE_VEDIO"/>' onclick="changeCatalog(this,'VEDIO','ITEM')">
									<lb:get key="lab_TYPE_VEDIO" />
								</a>
								<a href="###" title='<lb:get key="lab_TYPE_IMAGE"/>' onclick="changeCatalog(this,'IMAGE','ITEM')">
									<lb:get key="lab_TYPE_IMAGE" />
								</a>
                            </dd>
                        </dl>

                         <dl class="wzb-list-9 clearfix" id="norm-more-catalog-dl">
                             <dt><lb:get key="article_catalog" />：</dt>
                             <dd id="norm-list-1-info"><span id="norm-more-catalog" class="wzb-list-more"><lb:get key="global_more" />&gt;&gt;</span> <span id="norm-less-catalog" class="wzb-list-more"><lb:get key="click_up" />&gt;&gt;</span></dd>
                        </dl>

                        <dl class="wzb-list-9 clearfix" id="norm-more-tag-dl">
                             <dt><lb:get key="lab_kb_tag" />：</dt>
                             <dd id="norm-list-2-info"><span id="norm-more-tag" class="wzb-list-more"><lb:get key="global_more" />&gt;&gt;</span> <span id="norm-less-tag" class="wzb-list-more"><lb:get key="click_up" />&gt;&gt;</span></dd>
                        </dl>
                    </div> <!-- wzb-model-4 End-->

					<div class="wzb-model-12">
                        <div role="tabpanel" class="wzb-tab-2">
							<!-- Nav tabs -->
							<ul class="nav nav-tabs" role="tablist" id="sortul">
								<span><lb:get key="lab_kb_know_list" /></span>
								<li role="presentation" id="kbi_title" data="asc"><a href="#title" aria-controls="title" role="tab" data-toggle="tab"><lb:get key="global_title" /> <i class="fa fa-caret-down"></i></a></li>
								<li role="presentation" class="active" id="kbi_access_count" data="asc"><a href="#home" aria-controls="home" role="tab" data-toggle="tab"><lb:get key="lab_kb_count_view" /> <i class="fa fa-caret-down"></i></a></li>
								<%-- <li role="presentation" id="s_cnt_like_count" data="asc"><a href="#profile" aria-controls="profile" role="tab" data-toggle="tab"><lb:get key="lab_kb_praise" /> <i class="fa fa-caret-down"></i></a></li> --%>
								<li role="presentation" id="kbi_publish_datetime" data="asc"><a href="#time" aria-controls="time" role="tab" data-toggle="tab"><lb:get key="lab_kb_create_time" /> <i class="fa fa-caret-down"></i></a></li>
							</ul>

							<div class="tab-content">
								<ul class="wzb-list-23 clearfix" id="knowledgeList">

								</ul>
							</div>
						</div>
						<!-- wbtab end -->
					</div>
					<!-- norm-box End-->
				</div>
				<!-- main End-->
			</div>
		</div>
	</div>
	<!-- xyd-wrapper End-->
</body>
</html>