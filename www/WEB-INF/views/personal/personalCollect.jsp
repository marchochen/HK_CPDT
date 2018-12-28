\<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<%@ include file="../common/template.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/course.js"></script>
<title></title>
<script type="text/javascript">
	$(function() {
		$(".wbtabcont").table({
			url : '${ctx}/app/personal/getCollectList/Course/${usrEntId}?isPC=true',
			colModel : colModel,
			rp : 10,
			showpager : 5,
			hideHeader : true,
			usepager : true
		})
	})
	
	colModel = [{
		format : function(data) {
			if($(".active").attr("name") == 'Course'){
				var tnd_title = fetchLabel('collect_catalog_null');
				if(data.aeTreeNode != undefined){
					tnd_title = data.aeTreeNode.tnd_title
				}
				var collect_course = fetchLabel('collect_course') + fetchLabel('selfstudy');//默认网上课程
				if(data.itm_type == "SELFSTUDY"){//网上课程
					//collect_course = fetchLabel('collect_course') + fetchLabel('selfstudy');
					if(data.itm_exam_ind == 1){ //网上考试
						collect_course = fetchLabel('collect_course') + fetchLabel('exam_selfstudy');
					}
				}else if(data.itm_type == "CLASSROOM"){//离线课程
					collect_course = fetchLabel('collect_course') + fetchLabel('classroom');
					if(data.itm_exam_ind == 1){ //离线考试
						collect_course = fetchLabel('collect_course') + fetchLabel('exam_classroom');
					}
				}else if(data.itm_type == "INTEGRATED"){//项目式培训
					collect_course = fetchLabel('collect_course') + fetchLabel('integrated');
				}else if(data.itm_type == "AUDIOVIDEO"){//公开课
					collect_course = fetchLabel('collect_course') + fetchLabel('open_course');
				}
				p = {
					image : data.itm_icon,
					imageInd : true,
					id : data.itm_id,
					href : '${ctx}/app/course/detail/' + wbEncrytor().cwnEncrypt(data.itm_id),
					title_desc : collect_course,
					title : data.itm_title,
					title_2_desc : fetchLabel('collect_catalog'),
					title_2 : tnd_title,
					time : data.s_clt_create_datetime.substring(0,16),
					isMeInd : ${isMeInd}
				}
			} else if($(".active").attr("name") == 'Knowledge'){
				p = {
					image : data.imageAttachment.kba_url,
					imageInd : true,
					id : data.kbi_id,
					href : '${ctx}/app/kb/center/view?source=index&enc_kbi_id=' + wbEncrytor().cwnEncrypt(data.kbi_id)+"&kbi_type="+data.kbi_type,
					title_desc : fetchLabel('collect_knowledge'),
					title : data.kbi_title,
					title_2_desc : fetchLabel('lab_kb_type'),
					title_2 : fetchLabel('lab_TYPE_'+data.kbi_type),
					time : data.collect.s_clt_create_datetime.substring(0,16),
					isMeInd : ${isMeInd}
				}
			} else {
				p = {
					image : data.art_icon_file,
					imageInd : true,
					id : data.art_id,
					href : '${ctx}/app/article/detail/'+ wbEncrytor().cwnEncrypt(data.art_id),
					title_desc : fetchLabel('collect_article'),
					title : data.art_title,
					title_2_desc : fetchLabel('collect_article_type'),
					title_2 : data.articleType.aty_title,
					time : data.snsCollect.s_clt_create_datetime.substring(0,16),
					isMeInd : ${isMeInd}
				}
			}
			
			return $('#collect-template').render(p);
		}
	}]
	
	function cancelCollect(s_clt_target_id){
		Dialog.confirm({text:fetchLabel('collect_confirm_delete'), callback: function (answer) {
				if(answer){
					$.ajax({
						url : '${ctx}/app/personal/cancelCollect/' + s_clt_target_id + '/' + $(".active").attr("name"),
						type : 'POST',
						success : function(){
							$(".wbtabcont").html('');
							$(".wbtabcont").table({
								url : '${ctx}/app/personal/getCollectList/' + $(".active").attr("name") + '/${usrEntId}?isPC=true',
								colModel : colModel,
								rp : 10,
								showpager : 5,
								hideHeader : true,
								usepager : true
							})
						}
					});
				}
			}
		});
	}
	
	function changeTab(thisObj,s_clt_module){
		$(".active").removeClass("active");
		$(thisObj).addClass("active");
		$(".wbtabcont").html('');
		$(".wbtabcont").table({
			url : '${ctx}/app/personal/getCollectList/' + $(".active").attr("name") + '/${usrEntId}?isPC=true',
			colModel : colModel,
			rp : 10,
			showpager : 5,
			hideHeader : true,
			usepager : true
		})
	}
</script>
</head>
<body>
	<div class="xyd-wrapper">
		<div id="main" class="xyd-main clearfix">
			<jsp:include page="personalMenu.jsp"></jsp:include>

			<div class="xyd-article">
                <div role="tabpanel" class="wzb-tab-3">
					<!-- Nav tabs -->
					<ul class="nav nav-tabs" role="tablist">
						<li role="presentation" class="active" onclick="changeTab(this)" name="Course"><a href="#home" style="padding-left:0;" aria-controls="home" role="tab" data-toggle="tab"><lb:get key="collect_courses" /></a></li>
						<li role="presentation" onclick="changeTab(this)" name="Article"><a href="#profile" aria-controls="profile" role="tab" data-toggle="tab"><lb:get key="collect_articles" /></a></li>
						<li role="presentation" onclick="changeTab(this)" name="Knowledge"><a href="#setting" aria-controls="setting" role="tab" data-toggle="tab"><lb:get key="collect_knowledge" /></a></li>
					</ul>

                    <div class="tab-content">
                         <div class="wbtabcont"></div>
                    </div>
                </div> <!-- wzb-tab-3 end -->
			</div> <!-- xyd-article End-->

		</div>
	</div>
	<!-- xyd-wrapper End-->
</body>
</html>