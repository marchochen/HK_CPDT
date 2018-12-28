<script type="text/javascript" src="${ctx}/js/kindeditor/kindeditor.js"></script>
<script type="text/javascript" src="${ctx}/js/kindeditor/lang/en.js"></script>
<link type="text/css" rel="stylesheet" href="${ctx}/js/kindeditor/themes/default/default.css" />
<script type="text/javascript">
	var kindeditorOptions = {
		uploadJson : '${ctx}/servlet/Dispatcher',
		urlType : 'domain',
		langType : '${label_lan}',
		fieldName : 'imgFile',
		items : [ 'source','undo', 'redo', '|', 'cut', 'copy', 'paste', '|',
				'formatblock', 'fontname', 'fontsize', '|', 'forecolor',
				'hilitecolor', 'bold', 'italic', 'underline', 'strikethrough',
				'lineheight', 'removeformat', '|', '/', 'justifyleft',
				'justifycenter', 'justifyright', 'justifyfull',
				'insertorderedlist', 'insertunorderedlist', 'image',
				'insertfile', 'table', 'hr', 'emoticons', 'link', 'unlink',
				'|', 'fullscreen', 'preview', 'selectall', '|', '|', 'about' ]
	};

	var minKindeditorOptions = $.extend({}, kindeditorOptions, {
		width : 490,
		minWidth : 460,
		afterBlur: function(){this.sync();}
	});

	var kindeditorMCOptions = $.extend({}, kindeditorOptions, {
		items : [ 'undo', 'redo', '|', 'cut', 'copy', 'paste', '|',
				'formatblock', 'fontname', 'fontsize', '|', 'forecolor',
				'hilitecolor', 'bold', 'italic', 'underline', 'strikethrough',
				'lineheight', 'removeformat', '|', 'image', ]
	});

	var kindeditorFBOptions = $.extend({}, kindeditorOptions, {
		minWidth : 550,
		items : [ 'undo', 'redo', '|', 'cut', 'copy', 'paste', '|',
				'formatblock', 'fontname', 'fontsize', '|', 'forecolor',
				'hilitecolor', 'bold', 'italic', 'underline', 'strikethrough',
				'lineheight', 'removeformat', '|', 'fb', ]
	});
</script>