(function($){
	var includePath = '';
	
	$.include = function(file, basePath){

        var files = [];
        var tempLang = window.localStorage.getItem("globalLang");
        if(tempLang == 'en-us' || !tempLang) {
        	files.push('js/i18n/en-us.js');
        	files.push('css/other.css');
        } else if(tempLang == 'zh-cn'){
        	files.push('js/i18n/zh-cn.js');
        } else if(tempLang == 'zh-hk') {
        	files.push('js/i18n/zh-hk.js');
        }
        
        //公共文件加载
        files = files.concat([
	        'lib/html5.js',
	     	'lib/angular/angular.min.js',
	    	'lib/angular/angular-translate.min.js',
	    	'lib/bootstrap/ui-bootstrap-tpls.min.js',
	    	'lib/bootstrap/bootstrap.min.css',
	    	'lib/wb_encrypt_util.js',
	    	'js/app.js',
	        'js/directives/directives.js',
	    	'js/services/services.js',
	    	'js/filters/filters.js'
        ])
        
        //controller 中加载的文件
    	var extendfiles = typeof file == "string" ? [file] : file;
    	files = files.concat(extendfiles);
        
        for (var i = 0; i < files.length; i++) {
            var name = files[i].replace(/^\s|\s$/g, "");
            var att = name.split('.');
            var ext = att[att.length - 1].toLowerCase();
            var isCSS = ext == "css";
            var tag = isCSS ? "link" : "script";
            var attr = isCSS ? " type='text/css' rel='stylesheet' " : " language='javascript' type='text/javascript' charset='utf-8' ";
            var link = ( isCSS ? "href" : "src") + "='" + (basePath || includePath) + name + "'";
            if ($(tag + "[" + link + "]").length == 0)
                document.write("<" + tag + attr + link + "></" + tag + ">");
        }
	}
	
})($);
