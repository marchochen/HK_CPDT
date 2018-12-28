(function(window){
	
	function TimeShow(options,time,callback){
		this.timeInter = null;
		this.Time = options || {};
		this.elem = options.element || {};
		this.mode = options.mode;
		this.time = time;
		this.callback = callback;
		this.showDong()
	};

	TimeShow.prototype.showDong = function(){
		var showthis = this
		clearInterval(this.timeInter)
		this.timeInter = setInterval(function(){
			var NowTime = new Date();
			var EndTime = new Date(showthis.time);
			showthis.algorithm(NowTime,EndTime)
		},0)
	};

	TimeShow.prototype.algorithm = function(NowTime,EndTime){
		if(this.mode == 1){
    		var t = NowTime.getTime() - EndTime.getTime();
	   	}else{
	    	var t = EndTime.getTime() - NowTime.getTime();
	    }
	    if(t < 0){
	    	clearInterval(this.timeInter);
	    }
	    var d=0;
	    var h=0;
	    var m=0;
	    var s=0;
	    if(t>=0){
	        d=Math.floor(t/1000/60/60/24);//天
	        h=Math.floor(t/1000/60/60%24);//时
	        m=Math.floor(t/1000/60%60);//分
	        s=Math.floor(t/1000%60)
	    }
	    $('.miao').html(s)
	    $(this.elem.tian).html(d)
	    var m1 = m.toString().split("");
	    var h1 = h.toString().split("");
	    var s1 = s.toString().split("");
	    if(h1.length==1){
	        $(this.elem.shi1).html(0);
	        $(this.elem.shi2).html(h1[0]);
	    }else if(h1.length==2){+
	        $(this.elem.shi1).html(h1[0]);
	        $(this.elem.shi2).html(h1[1]);
	    }
	    if(m1.length==2){
	        $(this.elem.fen1).html(m1[0]);
	        $(this.elem.fen2).html(m1[1]);
	    }else if(m1.length==1){
	        $(this.elem.fen1).html(0);
	        $(this.elem.fen2).html(m1[0]);
	        
	    }
	};

window.TimeShow = TimeShow;

})(window)