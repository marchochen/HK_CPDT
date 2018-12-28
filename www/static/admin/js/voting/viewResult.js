$(function() {
	
	if(!$("#pieChart").get(0)){
		return;
	}
	
	var pieChartCanvas = $("#pieChart").get(0).getContext("2d");
	var pieChart = new Chart(pieChartCanvas);
	var pieOptions = {
		segmentShowStroke : true,
		segmentStrokeColor : "#fff",
		segmentStrokeWidth : 1,
		percentageInnerCutout : 50,
		animationSteps : 100,
		animationEasing : "easeOutBounce",
		animateRotate : true,
		animateScale : false,
		responsive : true,
		maintainAspectRatio : false,
		legendTemplate : "<ul class=\"<%=name.toLowerCase()%>-legend\"><% for (var i=0; i<segments.length; i++){%><li><span style=\"background-color:<%=segments[i].fillColor%>\"></span><%if(segments[i].label){%><%=segments[i].label%><%}%></li><%}%></ul>",
		tooltipTemplate : "<%=value %> <%=label%>"
	};

	pieChart.Doughnut(PieData, pieOptions);
});