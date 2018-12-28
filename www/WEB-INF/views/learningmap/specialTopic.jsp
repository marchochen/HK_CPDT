<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<%@ include file="../common/template.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-admin.css" />
<script type="text/javascript" src="${ctx}/static/js/front/course.js"></script>
<link rel="stylesheet" href="${ctx}/static/admin/css/font-awesome/css/font-awesome.min.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css"/>
<script type="text/javascript" src="${ctx}/static/js/base.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery.corner.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery.roundabout.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery.roundabout-shapes.js"></script>
<script type="text/javascript" src="${ctx}/js/index.js"></script>
<title></title>
</head>
<body>

	<div class="xyd-wrapper">
<div class="xyd-main-3 clearfix">
    <div class="bg-10" >
<!--轮播-->
        <div id="gla">
            <div id="gla_box">
                <ul>
                <c:forEach var="specialTopic" items="${specialTopics }" >
               <li>

                        <a href="${ctx}/app/learningmap/specialDetail?id=${specialTopic.encrypt_ust_id}">
                            <div class="gla_inbox">
                                <img src="${ctx }${specialTopic.abs_img}" />
                                <i class="bg-tuijian"><lb:get key="label_lm.label_core_learning_map_65" /></i>
                            </div>
                        </a>
                    </li>
                
                </c:forEach>
                </ul>
            </div>
       </div>
       
       
<!--轮播-->

    </div>



</div>
</div> <!-- xyd-wrap End-->

<div class="xyd-wrapper">
<div class="xyd-main clearfix">
    <div>
        <ul class="wzb-list-27 wzb-list-28 clearfix" id="main">
  
        </ul>
    </div>
    
</div>
</div> <!-- xyd-wrap End-->
<script type="text/javascript" src="${ctx}/static/js/front/specialTopic/index.js"></script>
<!-- template start -->
<script id="centerTemplate" type="text/x-jsrender">
     <li onclick="window.location.href='${ctx}/app/learningmap/specialDetail?id={{>ust_id}}'">
                <div>
                    <img src="${ctx}{{>abs_img}}" alt="">
                    <span class="wzb-list-27-title">{{>ust_title}}</span>
                    <span class="wzb-list-27-tit">{{>ust_summary}}</span>
                </div>
                <p>
                    <i class="list-tool-dian color-gray999">{{>ust_hits}}</i>
                    <i class="list-tool-you"></i>
                </p>
            </li> 
</script>
<!-- template end -->
</body>
</html>