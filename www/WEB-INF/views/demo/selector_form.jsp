<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="container" style="width: 720px; margin: 0 auto;">
	<form class="form-inline text-right" role="form">
		<div class="form-group">
			<div class="input-group">
				<input type="text" class="form-control" id="tag-input">
				<div class="input-group-btn">
					<button onclick="searchTag()" type="button" class="btn btn-default">
						<i class="glyphicon glyphicon-search"></i>
					</button>
				</div>
			</div>
		</div>
	</form>
	<hr />
	<div id="tag-data"></div>
</div>
<script type="text/javascript">
	function searchTag() {
		var q = $('#tag-input').val();
		loadTagData(q);
	}

	function getTagData(q) {
		var tagData = [];
		if ($('#tag-data').find('input:checked').size() > 0) {
			$('#tag-data').find('input:checked').each(function() {
				tagData.push({
					id : $(this).attr('value'),
					title : $(this).attr('title')
				})
			});
		}
		return tagData;
	}

	function loadTagData(q) {
		var params = [];
		if (q != undefined) {
			params = [ {
				name : 'q',
				value : q
			} ];
		}

		$.ajax({
			dataType : 'json',
			url : '${ctx}/app/tree/tagListJson',
			data : params
		}).done(function(data) {
			if (data && data.records) {

				$('#tag-data').empty();

				$(data.records).each(function() {
					var htm = '<div class="checkbox"><label><input title="' + this.title + '" value="' + this.id + '" type="checkbox">' + this.title + '</label></div>';
					$('#tag-data').append($(htm));
				});
			}
		});
	}
</script>