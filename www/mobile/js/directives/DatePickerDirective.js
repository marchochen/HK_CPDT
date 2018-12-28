appModule.directive('dateselect', ['SliderEvent', function(SliderEvent){
	var html = '<div class="riqi-sel clearfix">'+
					'<div class="riqi-sel-fen">'+
						'<div id="yearSlider" class="mark-year">'+
							'<div class="mark-year-bg"></div>'+
     						'<ul id="year" class="riqi-sel-list">'+
        						'<li ng-repeat="year in years">{{year}}</li>'+
     						'</ul>'+
						'</div>'+
					'</div>'+
					'<div class="riqi-sel-fen">'+
						'<div id="monthSlider" class="mark-month">'+
							'<div class="mark-month-bg"></div>'+
     						'<ul id="month" class="riqi-sel-list">'+
        						'<li ng-repeat="month in months">{{month}}</li>'+
     						'</ul>'+
						'</div>'+
					'</div>'+
					'<div class="riqi-sel-fen">'+
						'<div id="daySlider" class="mark-day">'+
							'<div class="mark-day-bg"></div>'+
     						'<ul id="day" class="riqi-sel-list">'+
        						'<li ng-repeat="day in days">{{day}}</li>'+
     						'</ul>'+
						'</div>'+
					'</div>'+
				'</div>';
	var showCount = 5;
	var showSize = 32;
	var className  = 'riqi-sel-list';
	var animaClassName = 'f-anim';
	var yearSilderId = 'yearSlider';
	var monthSilderId = 'monthSlider';
	var daySilderId = 'daySlider';
	var yearId = 'year';
	var monthId = 'month';
	var dayId = 'day';
	var util = 'px';
	var defaultSize = '0px';
	var subTagName = 'li';
	return {
		restrict : 'EA',
		scope : {
			dateselect : '='
		},
		template : html,
		replace : true,
		link : function(scope, element, attrs){
			scope.years = insertYear(100, 20);
			scope.months = insertArray(1, 12);
			scope.days = insertArray(1, 31);
			var yearSlider = document.getElementById(yearSilderId);    //year滑动div
			var monthSlider = document.getElementById(monthSilderId);  //month滑动div
			var daySlider = document.getElementById(daySilderId);      //day滑动div
			var yearIndex = 0;
			var monthIndex = 0;
			var dayIndex = 0;
			var sliderYear = new SliderEvent({
				slider : yearSlider,
				moveListener : moveYear,
				endListener : endYear
			});                                                       //year滑动器
			var sliderMonth = new SliderEvent({
				slider : monthSlider,
				moveListener : moveMonth,
				endListener : endMonth
			});                                                       //month滑动器
			var sliderDay = new SliderEvent({
				slider : daySlider,
				moveListener : moveDay,
				endListener : endDay
			});                                                       //day滑动器
			var now = new Date();
			var year = now.getFullYear();
			var month = now.getMonth();
			var day = now.getDate();
			function initSelect(year, month, day){
				for(var i = 0; i < scope.years.length; i++){
					if(scope.years[i] == year){
						yearIndex = -(i-((showCount - 1) / 2));
					}
				}
				monthIndex = -(month-((showCount - 1) / 2));
				dayIndex = -(day-((showCount + 1) / 2));
				document.getElementById(yearId).style.top = yearIndex*showSize + util;
				document.getElementById(monthId).style.top = monthIndex*showSize + util;
				document.getElementById(dayId).style.top = dayIndex*showSize + util;
			}
			function select (){
				if(day > getCountDays(app.parseDate(toDateStr(year, month, 1), 'yyyy-MM-dd'))){
					day = getCountDays(app.parseDate(toDateStr(year, month, 1), 'yyyy-MM-dd'));
				}
				scope.dateselect = toDateStr(year, month, day);
				scope.$apply();
			}
			function moveYear(endPos){
				 yearIndex = move(endPos, yearId, yearIndex);
			}
			function endYear(endPos){
				yearIndex = end(endPos, yearId, yearIndex);
				//这里加上改变时间的代码
				year = scope.years[((showCount - 1) / 2)-yearIndex];
				select();
			}
			function moveMonth(endPos){
				 monthIndex = move(endPos, monthId, monthIndex);
			}
			function endMonth(endPos){
				 monthIndex = end(endPos, monthId, monthIndex);
				//这里加上改变时间的代码
				month = ((showCount - 1) / 2)-monthIndex;
				select();
			}
			function moveDay(endPos){
				dayIndex = move(endPos, dayId, dayIndex);
			}
			function endDay(endPos){
				dayIndex = end(endPos, dayId, dayIndex);
				//这里加上改变时间的代码
				day = ((showCount + 1) / 2) - dayIndex;
				select();
			}
			function move(endPos, elementId, index){
				document.getElementById(elementId).className = className;
				document.getElementById(elementId).style.top = (index*showSize + Number(endPos.y)) + util;
				return index;
			}
			function end(endPos, elementId, index){
				var element = document.getElementById(elementId);
				var elementTop = document.getElementById(elementId).style.top || defaultSize;
				var count = element.getElementsByTagName(subTagName).length;
				var top = elementTop.substring(0, elementTop.indexOf(util));
				var li_index = Math.round(top/showSize);
				if(li_index > (showCount - 1) / 2){
					li_index = (showCount - 1) / 2;
				}
				if(li_index < -(count - ((showCount + 1) / 2))){
					li_index = -(count - ((showCount + 1) / 2));
				}
				index = li_index;
				element.className = className + ' ' + animaClassName;
				document.getElementById(elementId).style.top = index*showSize+util;
				return index;
			}
			scope.$watch('dateselect', function(){
				if(scope.dateselect){
					var date = app.parseDate(scope.dateselect, 'yyyy-MM-dd');
					year = date.getFullYear();
					month = date.getMonth();
					day = date.getDate();
					scope.days = insertArray(1, getCountDays(date));
				}else{
					scope.days = insertArray(1, getCountDays(now));
				}
				initSelect(year, month, day);

			},true);

		}
	};
}]);
function insertArray(start,end){
	var arr=new Array();
	for(var i = start; i <= end; i++){
		if(i < 10){
			arr.push('0'+i);
			continue;
		}
		arr.push(i);
	}
	return arr;
}
function insertYear(before,after){
	var arr=new Array();
	var now = new Date();
	var fullYear = now.getFullYear();
	if(before && after){
		for(var i = 0; i <= before; i++){
			arr.unshift(fullYear-i);
		}
		for(var i = 1; i <= after; i++){
			arr.push(fullYear+i);
		}
	}
	return arr;
}

function getCountDays(date) {
	var curDate =new Date();
	curDate.setFullYear(date.getFullYear());
	curDate.setMonth(date.getMonth() + 1);
	curDate.setDate(0);
	return curDate.getDate();
}

function toDateStr(year, month, day){
	var monthStr = month + 1 > 9 ? month + 1 : '0' + (month + 1);
	var dayStr = day > 9 ? day : '0' + day;
	var yearStr = year;
	return yearStr + '-' + monthStr + '-' + dayStr;
}