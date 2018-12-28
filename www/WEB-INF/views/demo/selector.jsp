<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="../common/meta.tree.jsp"%>
<script type="text/javascript" src="${ctx}/js/jquery.selector.js"></script>
<script type="text/javascript">
	function getSrhTagData() {
		return getTagData();
	}

	$(function() {
		// 最简单的加载数据模式
		$('#catalog-selector').wzbSelector({
			simple : {
				enable : true,
				init : true,
				filter : true,
				url : '${ctx}/app/tree/catalogListJson'
			},
			message : {
				title : "知识目录"
			}
		});
		
		// 最简单的加载数据模式
		$('#tag-selector').wzbSelector({
			simple : {
				enable : true,
				init : true,
				filter : true,
				url : '${ctx}/app/tree/tagListJson'
			},
			message : {
				title : "标签选择"
			}
		});

		$('#tag-remote-selector').wzbSelector({
			remote : {
				enable : true,
				url : '${ctx}/app/demo/test/selector/form',
				callback : getSrhTagData
			},
			message : {
				title : "标签选择"
			}
		});

		// 多选的树模式
		$('#tc-selector-multiple').wzbSelector({
			auto_close : false,
			tree : {
				enable : true,
				type : 'multiple',
				setting : {
					async : {
						enable : true,
						autoParam : [ "id" ],
						url : '${ctx}/app/tree/tcListJson'
					}
				}
			},
			message : {
				title : "培训中心选择"
			}
		});

		// 单选的树模式
		$('#tc-selector-single').wzbSelector({
			type : 'single',
			tree : {
				enable : true,
				type : 'single',
				setting : {
					async : {
						enable : true,
						autoParam : [ "id" ],
						url : '${ctx}/app/tree/tcListJson'
					}
				}
			},
			message : {
				title : "培训中心选择"
			}
		});
	});
</script>
</head>
<body>
	<div class="container">
		<div class="panel panel-default">
			<div class="panel-heading">
				<h3 class="panel-title">选择框测试</h3>
			</div>
			<div class="panel-body">
				<form class="form-horizontal" role="form">
					<div class="form-group">
						<label for="tc" class="col-lg-3 control-label">名称</label>
						<div class="col-lg-9">
							<input class="form-control" />
						</div>
					</div>
					
					<div class="form-group">
						<label for="tc" class="col-lg-3 control-label">知识目录（简单模式）</label>
						<div class="col-lg-9">
							<select id="catalog-selector" multiple="multiple">
							</select>
						</div>
					</div>

					<div class="form-group">
						<label for="tc" class="col-lg-3 control-label">标签（简单模式）</label>
						<div class="col-lg-9">
							<select id="tag-selector" multiple="multiple">
							</select>
						</div>
					</div>

					<div class="form-group">
						<label for="tc" class="col-lg-3 control-label">标签（远程模式）</label>
						<div class="col-lg-9">
							<select id="tag-remote-selector" multiple="multiple">
							</select>
						</div>
					</div>

					<div class="form-group">
						<label for="tc" class="col-lg-3 control-label">培训中心（多选树）</label>
						<div class="col-lg-9">
							<select id="tc-selector-multiple" multiple="multiple">
							</select>
						</div>
					</div>

					<div class="form-group">
						<label for="tc" class="col-lg-3 control-label">培训中心（单选树）</label>
						<div class="col-lg-9">
							<select id="tc-selector-single">
							<option value="1">AAAAAAAAAAAAAAA</option>
							</select>
						</div>
					</div>


				</form>
			</div>
		</div>
	</div>
</body>
</html>