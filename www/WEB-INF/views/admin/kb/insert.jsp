<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="../../common/meta.kindeditor.jsp"%>
<%@ include file="../../common/meta.tree.jsp"%>
<script type="text/javascript" src="${ctx}/static/js/jquery.uploadify/jquery.uploadify.js"></script>
<link rel="stylesheet" type="text/css" href="${ctx }/static/js/jquery.uploadify/uploadify.css" />
<script type="text/javascript" src="${ctx}/js/jquery.uploader.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery.selector.js"></script>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_km_${lang}.js"></script>


<style type="text/css">
.video-error { margin-top: 60px;}
#menuTabs a { outline: none;}
table a { color: #000；}
.clickCatalog { background-color: gray;}
</style>
</head>
<body>
<input type="hidden" name="belong_module" value="FTN_AMD_KNOWLEDGE_MGT"/>


	 <title:get function="global.FTN_AMD_KNOWLEDGE_MGT"/>
<ol class="breadcrumb wzb-breadcrumb">
  <li><a href="javascript:wb_utils_gen_home(true);"><i class="fa wzb-breadcrumb-home fa-home"></i><lb:get key="label_km.label_core_knowledge_management_1"/></a></li>
<!--   <li><a href="#">知识管理</a></li> -->
  <li><a href="${ctx}/app/admin/kb/storege"><lb:get key="label_km.label_core_knowledge_management_2"/></a></li>
  <li class="active">

  		<c:if test="${type=='add'}">
  			<c:choose>
  				<c:when test="${kbItem.kbi_type=='ARTICLE'}">
  					<lb:get key="label_km.label_core_knowledge_management_26" />
  				</c:when>
  				<c:when test="${kbItem.kbi_type=='DOCUMENT'}">
  					<lb:get key="label_km.label_core_knowledge_management_27" />
  				</c:when>
  				<c:when test="${kbItem.kbi_type=='VEDIO'}">
  					<lb:get key="label_km.label_core_knowledge_management_28" />
  				</c:when>
  				<c:when test="${kbItem.kbi_type=='IMAGE'}">
  					<lb:get key="label_km.label_core_knowledge_management_29"/>
  				</c:when>
  			</c:choose>	
  		</c:if>
  		
  		<c:if test="${type=='update'}">
  			<c:choose>
  				<c:when test="${kbItem.kbi_type=='ARTICLE'}">
  					<lb:get key="label_km.label_core_knowledge_management_30" />
  				</c:when>
  				<c:when test="${kbItem.kbi_type=='DOCUMENT'}">
  					<lb:get key="label_km.label_core_knowledge_management_31" />
  				</c:when>
  				<c:when test="${kbItem.kbi_type=='VEDIO'}">
  					<lb:get key="label_km.label_core_knowledge_management_32" />
  				</c:when>
  				<c:when test="${kbItem.kbi_type=='IMAGE'}">
  					<lb:get key="label_km.label_core_knowledge_management_33"/>
  				</c:when>
  			</c:choose>
  		</c:if>
  	
  </li>
</ol> <!-- wzb-breadcrumb End-->

<div class="panel wzb-panel">
<div class="panel-heading">				
   	<c:if test="${type=='add'}">
  			<c:choose>
  				<c:when test="${kbItem.kbi_type=='ARTICLE'}">
  					<lb:get key="label_km.label_core_knowledge_management_26" />
  				</c:when>
  				<c:when test="${kbItem.kbi_type=='DOCUMENT'}">
  					<lb:get key="label_km.label_core_knowledge_management_27" />
  				</c:when>
  				<c:when test="${kbItem.kbi_type=='VEDIO'}">
  					<lb:get key="label_km.label_core_knowledge_management_28" />
  				</c:when>
  				<c:when test="${kbItem.kbi_type=='IMAGE'}">
  					<lb:get key="label_km.label_core_knowledge_management_29"/>
  				</c:when>
  			</c:choose>	
  		</c:if>
  		
  		<c:if test="${type=='update'}">

  			<c:choose>
  				<c:when test="${kbItem.kbi_type=='ARTICLE'}">
  					<lb:get key="label_km.label_core_knowledge_management_30" />
  				</c:when>
  				<c:when test="${kbItem.kbi_type=='DOCUMENT'}">
  					<lb:get key="label_km.label_core_knowledge_management_31" />
  				</c:when>
  				<c:when test="${kbItem.kbi_type=='VEDIO'}">
  					<lb:get key="label_km.label_core_knowledge_management_32" />
  				</c:when>
  				<c:when test="${kbItem.kbi_type=='IMAGE'}">
  					<lb:get key="label_km.label_core_knowledge_management_33"/>
  				</c:when>
  			</c:choose>
  		</c:if>
</div>
	<div class="panel-body">
		<form:form modelAttribute="kbItem" action="${ctx}/app/admin/kb/insert?source=${source}" onsubmit="return check()" cssClass="form-horizontal" enctype="multipart/form-data">
			<form:input type="hidden" cssStyle="width: 300px;" cssClass="form-control" path="kbi_id" />
			<form:input type="hidden" cssStyle="width: 300px;" cssClass="form-control" path="kbi_online" />
			<form:input type="hidden" cssStyle="width: 300px;" cssClass="form-control" path="kbi_type" />
			<input type="hidden" name="source" value="${source}" />
			<form:input type="hidden" id="kbi_default_image" cssStyle="width: 300px;" cssClass="form-control" path="kbi_default_image" />
			<div class="form-group">
				<form:label cssClass="col-sm-2 control-label" path="kbi_title">
					<span class="wzb-form-star">*</span>
					<lb:get key="label_km.label_core_knowledge_management_17" />：
				</form:label>
				<div class="col-sm-10">
					<form:input cssStyle="width: 300px;" cssClass="form-control" path="kbi_title" />
					<form:errors path="kbi_title" cssClass="error" />
				</div>
			</div>
			<div class="form-group">
				<form:label cssClass="col-sm-2 control-label" path="kbi_desc">
					<span class="wzb-form-star">*</span>
					<lb:get key="label_km.label_core_knowledge_management_34" />：
				</form:label>
				<div class="col-sm-10">
					<form:textarea cssStyle="width: 300px;" cssClass="form-control" path="kbi_desc"></form:textarea>
					<form:errors path="kbi_desc" cssClass="error" />
				</div>
			</div>

			<div class="form-group">
				<link rel="stylesheet" href="${ctx}/static/css/thickbox.css" />
				<script src="${ctx}/static/js/thickbox-compressed.js" type="text/javascript"></script>
				<label class="col-sm-2 control-label" name="kbi_imgeFile"><span class="wzb-form-star">*</span> <lb:get key="label_km.label_core_knowledge_management_35" />：</label>
				<div class="col-sm-10" style="padding: 0 10px;">
					<div class="wzb-uploader" id="image-input">
						<c:if test="${kbItem.imageAttachment != null && kbItem.imageAttachment.kba_id !=null}">
							<ul>
								<li id="${kbItem.imageAttachment.kba_id}" url="${ctx}${kbItem.imageAttachment.kba_url}" filename="${kbItem.imageAttachment.kba_filename}">${kbItem.imageAttachment.kba_filename}</li>
							</ul>
						</c:if>
					</div>
					<div>
						<span><a href="#TB_inline?height=480&width=600&inlineId=myOnPageContent" class="thickbox"><lb:get key="label_km.label_core_knowledge_management_36"/></a></span> <span><form:errors path="kbi_image" cssClass="error thickbox" style="margin-left:30px;" /></span>
					</div>
				</div>
				<div id="myOnPageContent" style="display: none;">
					<div class="thickbox-tit">
						<lb:get key="label_km.label_core_knowledge_management_37" />
					</div>

					<div class="thickbox-cont thickbox-user clearfix thickbox-content-2" id="defaultImages" ></div>

					<div class="norm-border  thickbox-footer" style="padding-bottom:10px;">
						<input id="define" type="button" class="btn wzb-btn-blue wzb-btn-bigger margin-right15  wzb-btn-big" name="pertxt" value="<lb:get key='global.button_submit'/>" />
						<input type="button" class="btn wzb-btn-blue wzb-btn-bigger TB_closeWindowButton  wzb-btn-big" name="pertxt" value="<lb:get key='global.button_cancel'/>" />
					</div>
				</div>
			</div>
			<div class="form-group">
				<form:label cssClass="col-sm-2 control-label" path="kbi_content">
					<span class="wzb-form-star">*</span>
					<lb:get key="label_km.label_core_knowledge_management_38" />：
				</form:label>

				<div class="col-sm-10" style="${kbItem.kbi_type=='DOCUMENT'?'padding: 0 10px;':''}">
					<c:choose>
						<c:when test="${kbItem.kbi_type=='ARTICLE'}">
							<form:textarea id="editor" cssStyle="width: 300px;" cssClass="form-control" path="kbi_content"></form:textarea>
							<script type="text/javascript">
								var editor;
								KindEditor.ready(function(K) {
									editor = K.create('#editor',
											minKindeditorOptions);
								});
							</script>
						</c:when>
						<c:when test="${kbItem.kbi_type=='DOCUMENT'}">
							<div class="wzb-uploader" id="doc-input">
								<c:if test="${fn:length(kbItem.attachments) > 0}">
									<ul>
										<c:forEach items="${kbItem.attachments}" var="attachment" varStatus="vs">
											<li id="${attachment.kba_id}" url="${ctx}${attachment.kba_url}" filename="${attachment.kba_filename}">${attachment.kba_filename}</li>
										</c:forEach>
									</ul>
								</c:if>
							</div>
							<div  style="width:50%;">
								<span class="wzb-ui-module-text"><lb:get key="label.lab_kb_Notice"/></span>
							</div>
						</c:when>
						<c:when test="${kbItem.kbi_type=='VEDIO'}">
							<div class="wzb-uploader" id="vedio-input">
								<c:if test="${fn:length(kbItem.attachments) > 0}">
									<ul>
										<c:forEach items="${kbItem.attachments}" var="attachment" varStatus="vs">
											<li id="${attachment.kba_id}" url="${ctx}${attachment.kba_url}" filename="${attachment.kba_filename}">${attachment.kba_filename}</li>
										</c:forEach>
									</ul>
								</c:if>
							</div>
						</c:when>
						<c:when test="${kbItem.kbi_type=='IMAGE'}">
							<div class="wzb-uploader" id="image-content-input">
								<c:if test="${fn:length(kbItem.attachments) > 0}">
									<ul>
										<c:forEach items="${kbItem.attachments}" var="attachment" varStatus="vs">
											<li id="${attachment.kba_id}" url="${ctx}${attachment.kba_url}" filename="${attachment.kba_filename}">${attachment.kba_filename}</li>
										</c:forEach>
									</ul>
								</c:if>
							</div>
						</c:when>
					</c:choose>
					<div id="video-error-id">
						<form:errors path="kbi_content" cssClass="error" />
					</div>
				</div>
			</div>
			<c:if test="${kbItem.kbi_type =='DOCUMENT' || kbItem.kbi_type =='VEDIO'}">
				<div class="form-group">
					<form:label cssClass="col-sm-2 control-label" path="kbi_download">
						<span class="wzb-form-star">*</span>
						<lb:get key="label_km.label_core_knowledge_management_39" />：
					</form:label>
					<div class="col-sm-10">
						<form:select cssStyle="width: 120px;" cssClass="form-control" path="kbi_download">
							<form:option value="ALLOW">
								<lb:get key="label_km.label_core_knowledge_management_40" />
							</form:option>
							<form:option value="INTERDICT">
								<lb:get key="label_km.label_core_knowledge_management_41" />
							</form:option>
						</form:select>
						<form:errors path="kbi_download" cssClass="error" />
					</div>
				</div>
			</c:if>
			<div class="form-group">
				<form:label cssClass="col-sm-2 control-label" path="kbi_catalog_ids">
					<span class="wzb-form-star">*</span>
					<lb:get key="label_km.label_core_knowledge_management_42" />：
				</form:label>
				<div class="col-sm-10">
					<form:select id="catalog-selector" cssStyle="width: 120px;" cssClass="form-control" path="kbi_catalog_ids">
						<c:if test="${fn:length(kbItem.catalogues) > 0}">
							<c:forEach items="${kbItem.catalogues}" var="catalogue" varStatus="vs">
								<form:option value="${catalogue.kbc_id}" label="${catalogue.kbc_title}" />
							</c:forEach>
						</c:if>
					</form:select>
					<form:errors path="kbi_catalog_ids" cssClass="error" />
				</div>
			</div>
			<div class="form-group">
				<form:label cssClass="col-sm-2 control-label" path="kbi_tag_ids">
					<lb:get key="label_km.label_core_knowledge_management_43" />：
				</form:label>
				<div class="col-sm-10">
					<form:select id="tag-selector" cssStyle="width: 120px;" cssClass="form-control" path="kbi_tag_ids">
						<c:if test="${fn:length(kbItem.tags) > 0}">
							<c:forEach items="${kbItem.tags}" var="tag" varStatus="vs">
								<form:option value="${tag.tag_id}" label="${tag.tag_title}" />
							</c:forEach>
						</c:if>
					</form:select>
					<form:errors path="kbi_tag_ids" cssClass="error" />
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-2"></div>
				<div class="col-sm-10"><span class="wzb-form-star">*</span>
							<!-- 为必填 --><lb:get key="label_rm.label_core_requirements_management_35" /></div>
			</div>
			<div class="wzb-bar">
				<button type="submit" class="btn wzb-btn-blue wzb-btn-blue wzb-btn-big margin-right10">
					<lb:get key="global.button_ok" />
				</button>
				<c:choose>
					<c:when test="${source=='approval'}">
						<button type="button" class="btn wzb-btn-blue navbar-btn wzb-btn-big margin-right10" onclick="javascript:go('${ctx}/app/admin/kb/approval');">
							<lb:get key="global.button_cancel" />
						</button>
					</c:when>
					<c:otherwise>
						<button type="button" class="btn wzb-btn-blue  navbar-btn wzb-btn-big" onclick="javascript:go('${ctx}/app/admin/kb/storege');">
							<lb:get key="global.button_cancel" />
						</button>
					</c:otherwise>
				</c:choose>
			</div>
		</form:form>
		<div class="clear"></div>
	</div>
	<div class="clear"></div>
</div>  <!-- wzb-panel End-->

<script type="text/javascript">
    sessionid = '${pageContext.session.id}';
    $(function() {
        // 最简单的加载数据模式
        $('#catalog-selector').wzbSelector({
            simple : {
                enable : true,
                init : true,
                filter : true,
                sessionid : sessionid,
                url : '${ctx}/app/tree/catalogListJson'
            },
            message : {
                title : fetchLabel('label_core_knowledge_management_42')
            },
            bodyStyle : "max-height:452px;overflow-y: auto;"
        });

        $.getJSON('${ctx}/app/kb/center/getDefaultImages', function(rData) {
            var str = '';
            $.each(rData, function(index, obj) {
                var p = {
                    file : obj.kba_file,
                    filename : obj.kba_filename
                }
                str += $('#image-template').render(p);
            })
            $('#defaultImages').html(str);
        })

        // 最简单的加载数据模式
        $('#tag-selector').wzbSelector({
            simple : {
                enable : true,
                init : true,
                filter : true,
                sessionid : sessionid,
                url : '${ctx}/app/tree/tagListJson'
            },
            message : {
                title : fetchLabel('label_core_knowledge_management_86')
            },
            bodyStyle : "max-height:452px;overflow-y: auto;"
        });

        $('#image-input').wzbUploader({
            type : 'image',
            sessionid : sessionid,
            fieldname : 'kbi_image',
            multiple : false
        });

        $('#doc-input').wzbUploader({
            type : 'document',
            sessionid : sessionid,
            fieldname : 'kbi_content',
            multiple : false
        });

        $('#image-content-input').wzbUploader({
            type : 'image',
            sessionid : sessionid,
            fieldname : 'kbi_content'
        });

        $('#vedio-input').wzbUploader({
            type : 'vedio',
            sessionid : sessionid,
            fieldname : 'kbi_content',
            multiple : false
        });
        
        $('#wzb-uploader-li-2').click(function(){
            $('input[name=kbi_online]').val('ONLINE');
            $('#video-error-id').addClass('video-error');
        });
        
        $('#wzb-uploader-li-1').click(function(){
            $('input[name=kbi_online]').val('OFFLINE');
            $('#video-error-id').removeClass('video-error');
        });
        <c:if test="${!empty kbItem.kbi_online}">
            if('${kbItem.kbi_online}' == 'ONLINE'){
                $('#wzb-uploader-li-1').removeClass('active');
                $('#kbi_content_tab_div').removeClass('active').removeClass('in');
                $('#wzb-uploader-li-2').addClass('active');
                $('#kbi_content_url_tab_div').addClass('active').addClass('in');
                $('#video-error-id').addClass('video-error');
                <c:if test="${!empty kbItem.kbi_content}">
                    $('input[name=kbi_content]').val('${kbItem.kbi_content}');
                </c:if>
            }
        </c:if>

        $('#define')
                .click(
                        function() {
                            var image = $('#defaultImages .cur img')
                                    .attr('src');
                            var tpl = '';
                            if (image) {
                                tpl += '<div class="wzb-uploader-prewview wzb-uploader-prewview-image">';
                                tpl += '<img src="' + image + '"/>';
                                tpl += '<a class="wzb-uploader-prewview-remove"><i class="glyphicon glyphicon-remove"></i></a>';
                                tpl += '</div>';
                                var e = $(tpl);
                                e.find('a.wzb-uploader-prewview-remove').hide();
                                e.hover(function() {
                                    e.find('a.wzb-uploader-prewview-remove')
                                            .show();
                                }, function() {
                                    e.find('a.wzb-uploader-prewview-remove')
                                            .hide();
                                });
                                $('i.glyphicon-remove', e).click(function() {
                                    $(this).parent().parent().remove();
                                    $('#kbi_default_image').val('');
                                });
                                $('#image-input .wzb-uploader-file-list').html(
                                        e);
                                $('#kbi_default_image').val(image);
                                tb_remove()
                            }
                        })
    });
    function changeImage(thisObj) {
        $("#" + $(thisObj).parent().attr("id") + " .cur").removeClass("cur");
        $(thisObj).addClass("cur");
    }
    
    function check(){
    	if(getChars($('#kbi_title').val()) > 80){
    		Dialog.alert(eval('wb_msg_usr_title_too_long'));
    		return false;
    	}
    	if(getChars($('#kbi_desc').val()) > 400){
    		Dialog.alert(eval('wb_msg_usr_desc_too_long'));
    		return false;
    	}
    	if(getChars($('#editor').val()) > 20000){
    		Dialog.alert(fetchLabel('label_core_knowledge_management_94'));
    		return false;
    	}
    	return true;
    }
</script>
<script id="image-template" type="text/x-jsrender">
        <a href="###" title="" onclick="changeImage(this)">
            <img width="116" height="70" src="${ctx}/imglib/knowledge/{{>file}}" alt="{{>file}}"  />
            <div class="thickbox-bg"></div>
        </a>
</script>
</body>
</html>