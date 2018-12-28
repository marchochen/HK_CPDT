var Mouse = {
	x: 0,
	y: 0,
	refresh: function(e) {
		if (e && !this.down && !jQuery(e.target).hasClass("flexpaper_zoomSlider")) {
			return;
		}
		var posx = 0,
			posy = 0;
		if (!e) {
			e = window.event;
		}
		if (e.pageX || e.pageY) {
			posx = e.pageX;
			posy = e.pageY;
		} else {
			if (e.clientX || e.clientY) {
				posx = e.clientX + document.body.scrollLeft + document.documentElement.scrollLeft;
				posy = e.clientY + document.body.scrollTop + document.documentElement.scrollTop;
			}
		}
		this.x = posx;
		this.y = posy;
	}
};
var mouseMoveHandler = document.onmousemove || function() {};
document.onmousemove = function(e) {
	Mouse.refresh(e);
};
var MPosition = {
	get: function(obj) {
		var curleft = curtop = 0;
		if (obj.offsetParent) {
			do {
				curleft += obj.offsetLeft;
				curtop += obj.offsetTop;
			} while (obj = obj.offsetParent);
		}
		return [curleft, curtop];
	}
};
var Slider = function(wrapper, options) {
	if (typeof wrapper == "string") {
		wrapper = document.getElementById(wrapper);
	}
	if (!wrapper) {
		return;
	}
	var handle = wrapper.getElementsByTagName("div")[0];
	if (!handle || handle.className.search(/(^|\s)flexpaper_handle(\s|$)/) == -1) {
		return;
	}
	this.init(wrapper, handle, options || {});
	this.setup();
};
Slider.prototype = {
	init: function(wrapper, handle, options) {
		this.wrapper = wrapper;
		this.handle = handle;
		this.options = options;
		this.value = {
			current: options.value || 0,
			target: options.value || 0,
			prev: -1
		};
		this.disabled = options.disabled || false;
		this.steps = options.steps || 0;
		this.snapping = options.snapping || false;
		this.speed = options.speed || 5;
		this.callback = options.callback || null;
		this.animation_callback = options.animation_callback || null;
		this.bounds = {
			pleft: options.pleft || 0,
			left: 0,
			pright: -(options.pright || 0),
			right: 0,
			width: 0,
			diff: 0
		};
		this.offset = {
			wrapper: 0,
			mouse: 0,
			target: 0,
			current: 0,
			prev: -9999
		};
		this.dragging = false;
		this.tapping = false;
	},
	setup: function() {
		var self = this;
		this.wrapper.onselectstart = function() {
			return false;
		};
		this.handle.onmousedown = function(e) {
			self.preventDefaults(e, true);
			this.focus();
			self.handleMouseDownHandler(e);
		};
		this.wrapper.onmousedown = function(e) {
			self.preventDefaults(e);
			self.wrapperMouseDownHandler(e);
		};
		var mouseUpHandler = document.onmouseup || function() {};
		document.onmouseup = function(e) {
			if (self.dragging) {
				mouseUpHandler(e);
				self.preventDefaults(e);
				self.documentMouseUpHandler(e);
			}
		};
		var resizeHandler = document.onresize || function() {};
		window.onresize = function(e) {
			resizeHandler(e);
			self.setWrapperOffset();
			self.setBounds();
		};
		this.setWrapperOffset();
		if (!this.bounds.pleft && !this.bounds.pright) {
			this.bounds.pleft = MPosition.get(this.handle)[0] - this.offset.wrapper;
			this.bounds.pright = -this.bounds.pleft;
		}
		this.setBounds();
		this.setSteps();
		this.interval = setInterval(function() {
			self.animate();
		}, 100);
		self.animate(false, true);
	},
	setWrapperOffset: function() {
		this.offset.wrapper = MPosition.get(this.wrapper)[0];
	},
	setBounds: function() {
		this.bounds.left = this.bounds.pleft;
		this.bounds.right = this.bounds.pright + this.wrapper.offsetWidth;
		this.bounds.width = this.bounds.right - this.bounds.left;
		this.bounds.diff = this.bounds.width - this.handle.offsetWidth;
	},
	setSteps: function() {
		if (this.steps > 1) {
			this.stepsRatio = [];
			for (var i = 0; i <= this.steps - 1; i++) {
				this.stepsRatio[i] = i / (this.steps - 1);
			}
		}
	},
	disable: function() {
		this.disabled = true;
		this.handle.className += " disabled";
	},
	enable: function() {
		this.disabled = false;
		this.handle.className = this.handle.className.replace(/\s?disabled/g, "");
	},
	handleMouseDownHandler: function(e) {
		if (Mouse) {
			Mouse.down = true;
			Mouse.refresh(e);
		}
		var self = this;
		this.startDrag(e);
		this.cancelEvent(e);
	},
	wrapperMouseDownHandler: function(e) {
		this.startTap();
	},
	documentMouseUpHandler: function(e) {
		this.stopDrag();
		this.stopTap();
		if (Mouse) {
			Mouse.down = false;
		}
	},
	startTap: function(target) {
		if (this.disabled) {
			return;
		}
		if (target === undefined) {
			target = Mouse.x - this.offset.wrapper - this.handle.offsetWidth / 2;
		}
		this.setOffsetTarget(target);
		this.tapping = true;
	},
	stopTap: function() {
		if (this.disabled || !this.tapping) {
			return;
		}
		this.setOffsetTarget(this.offset.current);
		this.tapping = false;
		this.result();
	},
	startDrag: function(e) {
		if (!e) {
			e = window.event;
		}
		if (this.disabled) {
			return;
		}
		this.offset.mouse = Mouse.x - MPosition.get(this.handle)[0];
		this.dragging = true;
		if (e.preventDefault) {
			e.preventDefault();
		}
	},
	stopDrag: function() {
		if (this.disabled || !this.dragging) {
			return;
		}
		this.dragging = false;
		this.result();
	},
	feedback: function() {
		var value = this.value.current;
		if (this.steps > 1 && this.snapping) {
			value = this.getClosestStep(value);
		}
		if (value != this.value.prev) {
			if (typeof this.animation_callback == "function") {
				this.animation_callback(value);
			}
			this.value.prev = value;
		}
	},
	result: function() {
		var value = this.value.target;
		if (this.steps > 1) {
			value = this.getClosestStep(value);
		}
		if (typeof this.callback == "function") {
			this.callback(value);
		}
	},
	animate: function(onMove, first) {
		if (onMove && !this.dragging) {
			return;
		}
		if (this.dragging) {
			this.setOffsetTarget(Mouse.x - this.offset.mouse - this.offset.wrapper);
		}
		this.value.target = Math.max(this.value.target, 0);
		this.value.target = Math.min(this.value.target, 1);
		this.offset.target = this.getOffsetByRatio(this.value.target);
		if (!this.dragging && !this.tapping || this.snapping) {
			if (this.steps > 1) {
				this.setValueTarget(this.getClosestStep(this.value.target));
			}
		}
		if (this.dragging || first) {
			this.value.current = this.value.target;
		}
		this.slide();
		this.show();
		this.feedback();
	},
	slide: function() {
		if (this.value.target > this.value.current) {
			this.value.current += Math.min(this.value.target - this.value.current, this.speed / 100);
		} else {
			if (this.value.target < this.value.current) {
				this.value.current -= Math.min(this.value.current - this.value.target, this.speed / 100);
			}
		} if (!this.snapping) {
			this.offset.current = this.getOffsetByRatio(this.value.current);
		} else {
			this.offset.current = this.getOffsetByRatio(this.getClosestStep(this.value.current));
		}
	},
	show: function() {
		if (this.offset.current != this.offset.prev) {
			this.handle.style.left = String(this.offset.current) + "px";
			this.offset.prev = this.offset.current;
		}
	},
	setValue: function(value, snap) {
		this.setValueTarget(value);
		if (snap) {
			this.value.current = this.value.target;
		}
	},
	setValueTarget: function(value) {
		this.value.target = value;
		this.offset.target = this.getOffsetByRatio(value);
	},
	setOffsetTarget: function(value) {
		this.offset.target = value;
		this.value.target = this.getRatioByOffset(value);
	},
	getRatioByOffset: function(offset) {
		return (offset - this.bounds.left) / this.bounds.diff;
	},
	getOffsetByRatio: function(ratio) {
		return Math.round(ratio * this.bounds.diff) + this.bounds.left;
	},
	getClosestStep: function(value) {
		var k = 0;
		var min = 1;
		for (var i = 0; i <= this.steps - 1; i++) {
			if (Math.abs(this.stepsRatio[i] - value) < min) {
				min = Math.abs(this.stepsRatio[i] - value);
				k = i;
			}
		}
		return this.stepsRatio[k];
	},
	preventDefaults: function(e, selection) {
		if (!e) {
			e = window.event;
		}
		if (e.preventDefault) {
			e.preventDefault();
		}
		if (selection && document.selection) {
			document.selection.empty();
		}
	},
	cancelEvent: function(e) {
		if (!e) {
			e = window.event;
		}
		if (e.stopPropagation) {
			e.stopPropagation();
		} else {
			e.cancelBubble = true;
		}
	}
};
var h = void 0,
	n = !0,
	p = null,
	v = !1;

function x() {
	return function() {};
}

function aa(g) {
	return function() {
		return this[g];
	};
}

function ca(g) {
	return function() {
		return g;
	};
}
var FLEXPAPER = window.FLEXPAPER ? window.FLEXPAPER : window.FLEXPAPER = {},
	da = 1,
	ea = da;
FLEXPAPER.Zg = function() {
	var g = [];
	return {
		jm: function(c) {
			g.push(c);
		},
		notify: function(c, d) {
			for (var e = 0, f = g.length; e < f; e++) {
				var k = g[e];
				if (k[c]) {
					k[c](d);
				}
			}
		}
	};
}();

function fa(g) {
	ea >= da && FLEXPAPER.Zg.notify("warn", g);
}

function A(g, c, d, e) {
	try {
		throw Error();
	} catch (f) {
		f.stack && f.stack.split("\n").slice(2);
	}
	FLEXPAPER.Zg.notify("error", g);
	d && c && (e ? jQuery("#" + d).trigger(c, e) : jQuery("#" + d).trigger(c));
	throw Error(g);
}
FLEXPAPER.Ih = {
	init: function() {
		if ("undefined" == typeof eb || !eb) {
			eb = {};
		}
		var g = navigator.userAgent.toLowerCase(),
			c = location.hash.substr(1),
			d = v,
			e = "";
		0 <= c.indexOf("mobilepreview=") && (d = n, e = c.substr(c.indexOf("mobilepreview=")).split("&")[0].split("=")[1]);
		var f;
		try {
			f = "ontouchstart" in document.documentElement;
		} catch (k) {
			f = v;
		}
		if (!f && (g.match(/iphone/i) || g.match(/ipod/i) || g.match(/ipad/i))) {
			d = n;
		}
		c = eb;
		f = /win/.test(g);
		var l = /mac/.test(g),
			u;
		if (!(u = d)) {
			try {
				u = "ontouchstart" in document.documentElement;
			} catch (m) {
				u = v;
			}
		}
		c.platform = {
			win: f,
			mac: l,
			touchdevice: u,
			ios: d && ("ipad" == e || "iphone" == e) || g.match(/iphone/i) || g.match(/ipod/i) || g.match(/ipad/i),
			android: d && "android" == e || -1 < g.indexOf("android"),
			Jc: d && ("ipad" == e || "iphone" == e) || navigator.userAgent.match(/(iPad|iPhone);.*CPU.*OS 6_\d/i),
			Vd: d && "iphone" == e || g.match(/iphone/i) || g.match(/ipod/i),
			De: d && "ipad" == e || g.match(/ipad/i),
			winphone: g.match(/Windows Phone/i) || g.match(/iemobile/i) || g.match(/WPDesktop/i),
			blackberry: g.match(/BlackBerry/i),
			webos: g.match(/webOS/i),
			hh: -1 < g.indexOf("android") && !(jQuery(window).height() < jQuery(window).width()),
			bd: d,
			oc: window.devicePixelRatio ? window.devicePixelRatio : 1
		};
		d = eb;
		e = document.createElement("div");
		e.innerHTML = "000102030405060708090a0b0c0d0e0f";
		d.Sc = e;
		eb.platform.touchonlydevice = eb.platform.touchdevice && (eb.platform.android || eb.platform.ios || eb.platform.blackberry || eb.platform.webos) || eb.platform.winphone;
		eb.platform.Nb = eb.platform.touchonlydevice && (eb.platform.Vd || eb.platform.hh);
		eb.platform.ios && (d = navigator.appVersion.match(/OS (\d+)_(\d+)_?(\d+)?/), d != p && 1 < d.length ? (eb.platform.Ph = parseInt(d[1], 10), eb.platform.Jc = 6 <= eb.platform.Ph) : eb.platform.Jc = n);
		eb.browser = {
			version: (g.match(/.+?(?:rv|it|ra|ie)[\/: ]([\d.]+)(?!.+opera)/) || [])[1],
			vb: (g.match(/.+?(?:version|chrome|firefox|opera|msie|OPR)[\/: ]([\d.]+)(?!.+opera)/) || [])[1],
			safari: (/webkit/.test(g) || /applewebkit/.test(g)) && !/chrome/.test(g),
			opera: /opera/.test(g),
			msie: /msie/.test(g) && !/opera/.test(g) && !/applewebkit/.test(g),
			zk: "Netscape" == navigator.appName && /Trident\/.*rv:([0-9]{1,}[.0-9]{0,})/.exec(navigator.userAgent) != p && !/opera/.test(g),
			mozilla: /mozilla/.test(g) && !/(compatible|webkit)/.test(g),
			chrome: /chrome/.test(g)
		};
		eb.browser.version && 1 < eb.browser.version.match(/\./g).length && (eb.browser.version = eb.browser.version.substr(0, eb.browser.version.indexOf(".", eb.browser.version.indexOf("."))));
		eb.browser.vb && 1 < eb.browser.vb.match(/\./g).length && (eb.browser.vb = eb.browser.vb.substr(0, eb.browser.vb.indexOf(".", eb.browser.vb.indexOf("."))));
		eb.browser.jb = {
			rb: !eb.platform.touchonlydevice || eb.platform.android && !window.annotations || eb.platform.Jc && !window.annotations || eb.platform.ios && 6.99 <= eb.platform.Ph && !window.annotations,
			Fl: eb.browser.mozilla && 4 <= eb.browser.version.split(".")[0] || eb.browser.chrome && 535 <= eb.browser.version.split(".")[0] || eb.browser.msie && 10 <= eb.browser.version.split(".")[0] || eb.browser.safari && 534 <= eb.browser.version.split(".")[0],
			Kl: document.documentElement.requestFullScreen || document.documentElement.mozRequestFullScreen || document.documentElement.webkitRequestFullScreen
		};
		if (eb.browser.msie) {
			var g = eb.browser,
				q;
			try {
				/MSIE ([0-9]{1,}[.0-9]{0,})/.exec(navigator.userAgent) != p && (rv = parseFloat(RegExp.$1)), q = rv;
			} catch (y) {
				q = -1;
			}
			g.version = q;
		}
	}
};
var C = "Portrait",
	D = "BookView",
	H = "TwoPage",
	L = "FlipView",
	M = "ThumbView",
	U = "SinglePage";

function W() {
	for (var g = eb.Sg.innerHTML, c = [], d = 0;
		"\n" != g.charAt(d) && d < g.length;) {
		for (var e = 0, f = 6; 0 <= f; f--) {
			" " == g.charAt(d) && (e |= Math.pow(2, f)), d++;
		}
		c.push(String.fromCharCode(e));
	}
	return c.join("");
}

function ga(g, c, d) {
	this.aa = g;
	this.Ec = c;
	this.containerId = d;
	this.scroll = function() {
		var c = this;
		jQuery(this.Ec).bind("mousedown", function(d) {
			if (c.aa.Yb || (g.Rj && g.Rj() || jQuery("*:focus").hasClass("flexpaper_textarea_contenteditable") || jQuery("*:focus").hasClass("flexpaper_note_textarea")) || c.aa.vc) {
				return n;
			}
			c.ml(c.Ec);
			c.Jg = d.pageY;
			c.Ig = d.pageX;
			return v;
		});
		jQuery(this.Ec).bind("mousemove", function(d) {
			return c.Mj(d);
		});
		this.aa.Vi || (jQuery(this.containerId).bind("mouseout", function(d) {
			c.kk(d);
		}), jQuery(this.containerId).bind("mouseup", function() {
			c.ui();
		}), this.aa.Vi = n);
	};
	this.Mj = function(c) {
		if (!this.aa.pg) {
			return n;
		}
		this.aa.wh != this.Ec && (this.Jg = c.pageY, this.Ig = c.pageX, this.aa.wh = this.Ec);
		this.scrollTo(this.Ig - c.pageX, this.Jg - c.pageY);
		this.Jg = c.pageY;
		this.Ig = c.pageX;
		return v;
	};
	this.ml = function(c) {
		this.aa.pg = n;
		this.aa.wh = c;
		jQuery(this.Ec).removeClass("flexpaper_grab");
		jQuery(this.Ec).addClass("flexpaper_grabbing");
	};
	this.kk = function(c) {
		0 == jQuery(this.aa.ga).has(c.target).length && this.ui();
	};
	this.ui = function() {
		this.aa.pg = v;
		jQuery(this.Ec).removeClass("flexpaper_grabbing");
		jQuery(this.Ec).addClass("flexpaper_grab");
	};
	this.scrollTo = function(c, d) {
		var k = jQuery(this.containerId).scrollLeft() + c,
			l = jQuery(this.containerId).scrollTop() + d;
		jQuery(this.containerId).scrollLeft(k);
		jQuery(this.containerId).scrollTop(l);
	};
}

function ha(g) {
	function c(c, d) {
		var e, f, k, l, m;
		k = c & 2147483648;
		l = d & 2147483648;
		e = c & 1073741824;
		f = d & 1073741824;
		m = (c & 1073741823) + (d & 1073741823);
		return e & f ? m ^ 2147483648 ^ k ^ l : e | f ? m & 1073741824 ? m ^ 3221225472 ^ k ^ l : m ^ 1073741824 ^ k ^ l : m ^ k ^ l;
	}

	function d(d, e, f, k, l, m, g) {
		d = c(d, c(c(e & f | ~e & k, l), g));
		return c(d << m | d >>> 32 - m, e);
	}

	function e(d, e, f, k, l, m, g) {
		d = c(d, c(c(e & k | f & ~k, l), g));
		return c(d << m | d >>> 32 - m, e);
	}

	function f(d, e, f, k, l, m, g) {
		d = c(d, c(c(e ^ f ^ k, l), g));
		return c(d << m | d >>> 32 - m, e);
	}

	function k(d, e, f, k, l, m, g) {
		d = c(d, c(c(f ^ (e | ~k), l), g));
		return c(d << m | d >>> 32 - m, e);
	}

	function l(c) {
		var d = "",
			e = "",
			f;
		for (f = 0; 3 >= f; f++) {
			e = c >>> 8 * f & 255, e = "0" + e.toString(16), d += e.substr(e.length - 2, 2);
		}
		return d;
	}
	var u = [],
		m, q, y, z, t, s, r, w;
	g = function(c) {
		c = c.replace(/\r\n/g, "\n");
		for (var d = "", e = 0; e < c.length; e++) {
			var f = c.charCodeAt(e);
			128 > f ? d += String.fromCharCode(f) : (127 < f && 2048 > f ? d += String.fromCharCode(f >> 6 | 192) : (d += String.fromCharCode(f >> 12 | 224), d += String.fromCharCode(f >> 6 & 63 | 128)), d += String.fromCharCode(f & 63 | 128));
		}
		return d;
	}(g);
	u = function(c) {
		var d, e = c.length;
		d = e + 8;
		for (var f = 16 * ((d - d % 64) / 64 + 1), k = Array(f - 1), l = 0, m = 0; m < e;) {
			d = (m - m % 4) / 4, l = 8 * (m % 4), k[d] |= c.charCodeAt(m) << l, m++;
		}
		d = (m - m % 4) / 4;
		k[d] |= 128 << 8 * (m % 4);
		k[f - 2] = e << 3;
		k[f - 1] = e >>> 29;
		return k;
	}(g);
	t = 1732584193;
	s = 4023233417;
	r = 2562383102;
	w = 271733878;
	for (g = 0; g < u.length; g += 16) {
		m = t, q = s, y = r, z = w, t = d(t, s, r, w, u[g + 0], 7, 3614090360), w = d(w, t, s, r, u[g + 1], 12, 3905402710), r = d(r, w, t, s, u[g + 2], 17, 606105819), s = d(s, r, w, t, u[g + 3], 22, 3250441966), t = d(t, s, r, w, u[g + 4], 7, 4118548399), w = d(w, t, s, r, u[g + 5], 12, 1200080426), r = d(r, w, t, s, u[g + 6], 17, 2821735955), s = d(s, r, w, t, u[g + 7], 22, 4249261313), t = d(t, s, r, w, u[g + 8], 7, 1770035416), w = d(w, t, s, r, u[g + 9], 12, 2336552879), r = d(r, w, t, s, u[g + 10], 17, 4294925233), s = d(s, r, w, t, u[g + 11], 22, 2304563134), t = d(t, s, r, w, u[g + 12], 7, 1804603682), w = d(w, t, s, r, u[g + 13], 12, 4254626195), r = d(r, w, t, s, u[g + 14], 17, 2792965006), s = d(s, r, w, t, u[g + 15], 22, 1236535329), t = e(t, s, r, w, u[g + 1], 5, 4129170786), w = e(w, t, s, r, u[g + 6], 9, 3225465664), r = e(r, w, t, s, u[g + 11], 14, 643717713), s = e(s, r, w, t, u[g + 0], 20, 3921069994), t = e(t, s, r, w, u[g + 5], 5, 3593408605), w = e(w, t, s, r, u[g + 10], 9, 38016083), r = e(r, w, t, s, u[g + 15], 14, 3634488961), s = e(s, r, w, t, u[g + 4], 20, 3889429448), t = e(t, s, r, w, u[g + 9], 5, 568446438), w = e(w, t, s, r, u[g + 14], 9, 3275163606), r = e(r, w, t, s, u[g + 3], 14, 4107603335), s = e(s, r, w, t, u[g + 8], 20, 1163531501), t = e(t, s, r, w, u[g + 13], 5, 2850285829), w = e(w, t, s, r, u[g + 2], 9, 4243563512), r = e(r, w, t, s, u[g + 7], 14, 1735328473), s = e(s, r, w, t, u[g + 12], 20, 2368359562), t = f(t, s, r, w, u[g + 5], 4, 4294588738), w = f(w, t, s, r, u[g + 8], 11, 2272392833), r = f(r, w, t, s, u[g + 11], 16, 1839030562), s = f(s, r, w, t, u[g + 14], 23, 4259657740), t = f(t, s, r, w, u[g + 1], 4, 2763975236), w = f(w, t, s, r, u[g + 4], 11, 1272893353), r = f(r, w, t, s, u[g + 7], 16, 4139469664), s = f(s, r, w, t, u[g + 10], 23, 3200236656), t = f(t, s, r, w, u[g + 13], 4, 681279174), w = f(w, t, s, r, u[g + 0], 11, 3936430074), r = f(r, w, t, s, u[g + 3], 16, 3572445317), s = f(s, r, w, t, u[g + 6], 23, 76029189), t = f(t, s, r, w, u[g + 9], 4, 3654602809), w = f(w, t, s, r, u[g + 12], 11, 3873151461), r = f(r, w, t, s, u[g + 15], 16, 530742520), s = f(s, r, w, t, u[g + 2], 23, 3299628645), t = k(t, s, r, w, u[g + 0], 6, 4096336452), w = k(w, t, s, r, u[g + 7], 10, 1126891415), r = k(r, w, t, s, u[g + 14], 15, 2878612391), s = k(s, r, w, t, u[g + 5], 21, 4237533241), t = k(t, s, r, w, u[g + 12], 6, 1700485571), w = k(w, t, s, r, u[g + 3], 10, 2399980690), r = k(r, w, t, s, u[g + 10], 15, 4293915773), s = k(s, r, w, t, u[g + 1], 21, 2240044497), t = k(t, s, r, w, u[g + 8], 6, 1873313359), w = k(w, t, s, r, u[g + 15], 10, 4264355552), r = k(r, w, t, s, u[g + 6], 15, 2734768916), s = k(s, r, w, t, u[g + 13], 21, 1309151649), t = k(t, s, r, w, u[g + 4], 6, 4149444226), w = k(w, t, s, r, u[g + 11], 10, 3174756917), r = k(r, w, t, s, u[g + 2], 15, 718787259), s = k(s, r, w, t, u[g + 9], 21, 3951481745), t = c(t, m), s = c(s, q), r = c(r, y), w = c(w, z);
	}
	return (l(t) + l(s) + l(r) + l(w)).toLowerCase();
}
String.format = function() {
	for (var g = arguments[0], c = 0; c < arguments.length - 1; c++) {
		g = g.replace(RegExp("\\{" + c + "\\}", "gm"), arguments[c + 1]);
	}
	return g;
};
jQuery.fn.wm = function(g, c) {
	return this.each(function() {
		jQuery(this).fadeIn(g, function() {
			eb.browser.msie ? $(this).get(0).style.removeAttribute("filter") : "";
			"function" == typeof eval(c) ? eval(c)() : "";
		});
	});
};
jQuery.fn.Sj = function(g) {
	this.each(function() {
		eb.browser.msie ? eval(g)() : jQuery(this).fadeOut(400, function() {
			eb.browser.msie ? $(this).get(0).style.removeAttribute("filter") : "";
			"function" == typeof eval(g) ? eval(g)() : "";
		});
	});
};
jQuery.fn.Gm = function(g, c) {
	if (0 <= jQuery.fn.jquery.indexOf("1.8")) {
		try {
			if (jQuery._data(this[0], "events") === h) {
				return v;
			}
		} catch (d) {
			return v;
		}
		var e = jQuery._data(this[0], "events")[g];
		if (e === h || 0 === e.length) {
			return v;
		}
		var f = 0;
	} else {
		if (this.data("events") === h) {
			return v;
		}
		e = this.data("events")[g];
		if (e === h || 0 === e.length) {
			return v;
		}
		f = 0;
	}
	for (; f < e.length; f++) {
		if (e[f].handler == c) {
			return n;
		}
	}
	return v;
};
jQuery.fn.ln = function(g) {
	if (this.data("events") === h) {
		return v;
	}
	var c = this.data("events")[g];
	if (c === h || 0 === c.length) {
		return v;
	}
	for (var d = 0; d < c.length; d++) {
		jQuery(this).unbind(g, c[d].handler);
	}
	return v;
};
jQuery.fn.Nm = function() {
	eb.browser.jb.rb ? this.scrollTo(ce, 0, {
		axis: "xy",
		offset: -30
	}) : this.data("jsp").scrollToElement(ce, v);
};
jQuery.fn.Dg = function(g) {
	this.css({
		width: 0,
		height: 0,
		"border-bottom": String.format("{0}px solid transparent", g),
		"border-top": String.format("{0}px solid transparent", g),
		"border-right": String.format("{0}px solid {1}", g, "#AAAAAA"),
		"font-size": "0px",
		"line-height": "0px",
		cursor: "pointer"
	});
	this.on("mouseover", function(c) {
		jQuery(c.target).css({
			"border-right": String.format("{0}px solid {1}", g, "#DEDEDE")
		});
	});
	this.on("mouseout", function(c) {
		jQuery(c.target).css({
			"border-right": String.format("{0}px solid {1}", g, "#AAAAAA")
		});
	});
};
jQuery.fn.Pk = function(g, c) {
	this.css({
		width: 0,
		height: 0,
		"border-bottom": String.format("{0}px solid {1}", g, "#AAAAAA"),
		"border-top": String.format("{0}px solid {1}", g, "#AAAAAA"),
		"border-left": String.format("1px solid {1}", g, "#AAAAAA"),
		"font-size": "0px",
		"line-height": "0px",
		cursor: "pointer"
	});
	this.on("mouseover", function(d) {
		jQuery(c).trigger("mouseover");
		jQuery(d.target).css({
			"border-left": String.format("1px solid {1}", g, "#DEDEDE"),
			"border-bottom": String.format("{0}px solid {1}", g, "#DEDEDE"),
			"border-top": String.format("{0}px solid {1}", g, "#DEDEDE")
		});
	});
	this.on("mouseout", function(d) {
		jQuery(c).trigger("mouseout");
		jQuery(d.target).css({
			"border-left": String.format("1px solid {1}", g, "#AAAAAA"),
			"border-bottom": String.format("{0}px solid {1}", g, "#AAAAAA"),
			"border-top": String.format("{0}px solid {1}", g, "#AAAAAA")
		});
	});
};
jQuery.fn.vf = function(g, c) {
	this.css({
		width: 0,
		height: 0,
		"border-bottom": String.format("{0}px solid transparent", g),
		"border-top": String.format("{0}px solid transparent", g),
		"border-left": String.format("{0}px solid {1}", g, "#AAAAAA"),
		"font-size": "0px",
		"line-height": "0px",
		cursor: "pointer"
	});
	c && this.css({
		opacity: 0.3
	});
	this.on("mouseover", function(d) {
		c ? jQuery(d.target).css({
			"border-left": String.format("{0}px solid {1}", g, "#FFFFFF"),
			opacity: 0.85
		}) : jQuery(d.target).css({
			"border-left": String.format("{0}px solid {1}", g, "#DEDEDE")
		});
	});
	this.on("mouseout", function(d) {
		jQuery(d.target).css({
			"border-left": String.format("{0}px solid {1}", g, "#AAAAAA")
		});
		c && jQuery(d.target).css({
			opacity: 0.3
		});
	});
};
jQuery.fn.Qk = function(g, c) {
	this.css({
		width: 0,
		height: 0,
		"border-bottom": String.format("{0}px solid {1}", g, "#AAAAAA"),
		"border-top": String.format("{0}px solid {1}", g, "#AAAAAA"),
		"border-right": String.format("1px solid {1}", g, "#AAAAAA"),
		"font-size": "0px",
		"line-height": "0px",
		cursor: "pointer"
	});
	this.on("mouseover", function(d) {
		jQuery(c).trigger("mouseover");
		jQuery(d.target).css({
			"border-right": String.format("1px solid {1}", g, "#DEDEDE"),
			"border-top": String.format("{0}px solid {1}", g, "#DEDEDE"),
			"border-bottom": String.format("{0}px solid {1}", g, "#DEDEDE")
		});
	});
	this.on("mouseout", function(d) {
		jQuery(c).trigger("mouseout");
		jQuery(d.target).css({
			"border-right": String.format("1px solid {1}", g, "#AAAAAA"),
			"border-top": String.format("{0}px solid {1}", g, "#AAAAAA"),
			"border-bottom": String.format("{0}px solid {1}", g, "#AAAAAA")
		});
	});
};
jQuery.fn.addClass5 = function(g) {
	return this[0].classList ? (this[0].classList.add(g), this) : this.addClass(g);
};
jQuery.fn.removeClass5 = function(g) {
	return this[0].classList ? (this[0].classList.remove(g), this) : this.addClass(g);
};
jQuery.fn.hf = function() {
	this.css({
		display: "none"
	});
};
jQuery.fn.ce = function() {
	this.css({
		display: "block"
	});
};
window.requestAnim = window.requestAnimationFrame || window.webkitRequestAnimationFrame || window.mozRequestAnimationFrame || window.oRequestAnimationFrame || window.msRequestAnimationFrame || function(g) {
	window.setTimeout(g, 1000 / 60);
};
jQuery.fn.Pd = function() {
	var g = this.css("transform");
	return g && "none" != g && ("0px,0px" != g.translate || 1 != parseFloat(g.scale)) ? n : v;
};

function ia(g, c) {
	var d = "0",
		e = g += "";
	if (d == p || 1 > d.length) {
		d = " ";
	}
	if (g.length < c) {
		for (var e = "", f = 0; f < c - g.length; f++) {
			e += d;
		}
		e += g;
	}
	return e;
}
jQuery.fn.spin = function(g) {
	this.each(function() {
		var c = jQuery(this),
			d = c.data();
		d.Og && (d.Og.stop(), delete d.Og);
		g !== v && (d.Og = (new Spinner(jQuery.extend({
			color: c.css("color")
		}, g))).spin(this));
	});
	return this;
};

function ja(g) {
	return (g = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(g)) ? {
		r: parseInt(g[1], 16),
		g: parseInt(g[2], 16),
		b: parseInt(g[3], 16)
	} : p;
}
jQuery.xe = function(g, c, d) {
	g = g.offset();
	return {
		x: Math.floor(c - g.left),
		y: Math.floor(d - g.top)
	};
};
jQuery.fn.xe = function(g, c) {
	return jQuery.xe(this.first(), g, c);
};
(function(g) {
	g.fn.moveTo = function(c) {
		return this.each(function() {
			var d = g(this).clone();
			g(d).appendTo(c);
			g(this).remove();
		});
	};
})(jQuery);

function ka(g) {
	return g.replace(/(?:(?:^|\n)\s+|\s+(?:$|\n))/g, "").replace(/\s+/g, " ");
}

function X(g) {
	window.Rg || (window.Rg = 1);
	if (!window.zh) {
		var c = window,
			d = document.createElement("div");
		document.body.appendChild(d);
		d.style.position = "absolute";
		d.style.width = "1in";
		var e = d.offsetWidth;
		d.style.display = "none";
		c.zh = e;
	}
	return g / (72 / window.zh) * window.Rg;
}

function Y(g) {
	g = g.replace(/-/g, "-\x00").split(/(?=-| )|\0/);
	for (var c = [], d = 0; d < g.length; d++) {
		"-" == g[d] && d + 1 <= g.length ? (c[c.length] = -1 * parseFloat(ka(g[d + 1].toString())), d++) : c[c.length] = parseFloat(ka(g[d].toString()));
	}
	return c;
}
FLEXPAPER.Tg = function(g, c) {
	if (0 < g.indexOf("[*,2]")) {
		var d = g.substr(g.indexOf("[*,"), g.indexOf("]") - g.indexOf("[*,") + 1);
		return g.replace(d, ia(c, parseInt(d.substr(d.indexOf(",") + 1, d.indexOf("]") - 2))));
	}
	return 0 < g.indexOf("[*,2,true]") ? g.replace("_[*,2,true]", "") : g;
};
FLEXPAPER.Hm = function(g) {
	return "#" != g.charAt(0) && "/" != g.charAt(0) && (-1 == g.indexOf("//") || g.indexOf("//") > g.indexOf("#") || g.indexOf("//") > g.indexOf("?"));
};
FLEXPAPER.rm = function(g, c, d, e, f, k, l) {
	if (e < c) {
		var u = c;
		c = e;
		e = u;
		u = d;
		d = f;
		f = u;
	}
	u = document.createElement("div");
	u.id = g + "_line";
	u.className = "flexpaper_cssline flexpaper_annotation_" + l + " flexpaper_interactiveobject_" + l;
	g = Math.sqrt((c - e) * (c - e) + (d - f) * (d - f));
	u.style.width = g + "px";
	u.style.marginLeft = k;
	e = Math.atan((f - d) / (e - c));
	u.style.top = d + 0.5 * g * Math.sin(e) + "px";
	u.style.left = c - 0.5 * g * (1 - Math.cos(e)) + "px";
	u.style.MozTransform = u.style.WebkitTransform = u.style.msTransform = u.style.am = "rotate(" + e + "rad)";
	return u;
};
FLEXPAPER.Wm = function(g, c, d, e, f, k) {
	if (e < c) {
		var l = c;
		c = e;
		e = l;
		l = d;
		d = f;
		f = l;
	}
	g = jQuery("#" + g + "_line");
	l = Math.sqrt((c - e) * (c - e) + (d - f) * (d - f));
	g.css("width", l + "px");
	e = Math.atan((f - d) / (e - c));
	g.css("top", d + 0.5 * l * Math.sin(e) + "px");
	g.css("left", c - 0.5 * l * (1 - Math.cos(e)) + "px");
	g.css("margin-left", k);
	g.css("-moz-transform", "rotate(" + e + "rad)");
	g.css("-webkit-transform", "rotate(" + e + "rad)");
	g.css("-o-transform", "rotate(" + e + "rad)");
	g.css("-ms-transform", "rotate(" + e + "rad)");
};
var ImagePageRenderer = window.ImagePageRenderer = function() {
		function g(c, d, e) {
			this.ia = c;
			this.config = d;
			this.Kc = d.jsonfile;
			this.jsDirectory = e;
			this.pageImagePattern = d.pageImagePattern;
			this.pageThumbImagePattern = d.pageThumbImagePattern;
			this.pageSVGImagePattern = d.pageSVGImagePattern;
			this.zg = d.pageHighResImagePattern;
			this.JSONPageDataFormat = this.gb = this.dimensions = p;
			this.Ma = d.compressedJSONFormat != p ? d.compressedJSONFormat : n;
			this.pa = p;
			this.Mb = "pageLoader_[pageNumber]";
			this.Mc = "data:image/gif;base64,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%3D%3D";
			this.qa = "data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///yH5BAEAAAAALAAAAAABAAEAAAIBRAA7";
			this.Kd = -1;
			this.Ra = p;
			this.Rd = v;
			this.Xc = this.pb = n;
			this.sb = d.SVGMode;
		}
		g.prototype = {
			sd: ca("ImagePageRenderer"),
			Na: function(c) {
				return c.aa.ha ? c.aa.ha.ra : "";
			},
			kb: function(c) {
				return c.aa.ha.mk;
			},
			Bb: function() {
				jQuery(this.Ra).unbind();
				this.Ra.Bb();
				delete this.Hc;
				this.Hc = p;
				delete this.dimensions;
				this.dimensions = p;
				delete this.Ra;
				this.Ra = p;
				delete this.pa;
				this.pa = p;
			},
			initialize: function(c) {
				var d = this;
				d.Hc = c;
				d.JSONPageDataFormat = d.Ma ? {
					wd: "width",
					vd: "height",
					jd: "text",
					fc: "d",
					Ge: "f",
					uc: "l",
					wc: "t",
					xc: "w",
					tc: "h"
				} : {
					wd: d.config.JSONPageDataFormat.pageWidth,
					vd: d.config.JSONPageDataFormat.pageHeight,
					jd: d.config.JSONPageDataFormat.textCollection,
					fc: d.config.JSONPageDataFormat.textFragment,
					Ge: d.config.JSONPageDataFormat.textFont,
					uc: d.config.JSONPageDataFormat.textLeft,
					wc: d.config.JSONPageDataFormat.textTop,
					xc: d.config.JSONPageDataFormat.textWidth,
					tc: d.config.JSONPageDataFormat.textHeight
				};
				jQuery.ajaxSetup({
					cache: v
				});
				d.Ra = new la(d.ia, d.Ma, d.JSONPageDataFormat, n);
				jQuery.ajaxPrefilter(function(c, d, e) {
					if (c.onreadystatechange) {
						var g = c.xhr;
						c.xhr = function() {
							function d() {
								c.onreadystatechange(k, e);
							}
							var k = g.apply(this, arguments);
							k.addEventListener ? k.addEventListener("readystatechange", d, v) : setTimeout(function() {
								var c = k.onreadystatechange;
								c && (k.onreadystatechange = function() {
									d();
									c.apply(this, arguments);
								});
							}, 0);
							return k;
						};
					}
				});
				if (!eb.browser.msie && !eb.browser.safari && 6 > eb.browser.vb) {
					var e = jQuery.ajaxSettings.xhr;
					jQuery.ajaxSettings.xhr = function() {
						return e();
					};
				}
				jQuery("#" + d.ia).trigger("onDocumentLoading");
				c = document.createElement("a");
				c.href = d.Kc;
				c.search += 0 < c.search.length ? "&" : "?";
				c.search += "callback=?";
				d.pm = v;
				0 < d.Kc.indexOf("{page}") ? (d.Fa = n, jQuery.ajax({
					url: d.Id(10),
					dataType: d.config.JSONDataType,
					success: function(c) {
						c.e && (c = CryptoJS.md.decrypt(c.e, CryptoJS.Ib.ld.parse(eb.Sg ? W() : eb.Sc.innerHTML)), c = jQuery.parseJSON(c.toString(CryptoJS.Ib.ie)), d.Cd = n);
						if (0 < c.length) {
							d.pa = Array(c[0].pages);
							for (var e = 0; e < c.length; e++) {
								d.pa[e] = c[e], d.pa[e].loaded = n;
							}
							for (e = 0; e < d.pa.length; e++) {
								d.pa[e] == p && (d.pa[e] = [], d.pa[e].loaded = v);
							}
							d.Hc();
							d.Ra.be(c);
						}
					},
					error: function(c, e, l) {
						A("Error loading JSON file (" + c.statusText + "," + l + "). Please check your configuration.", "onDocumentLoadedError", d.ia, c.responseText != p && 0 == c.responseText.indexOf("Error:") ? c.responseText.substr(6) : "");
					}
				})) : jQuery.ajax({
					url: d.Kc,
					dataType: d.config.JSONDataType,
					success: function(c) {
						c.e && (c = CryptoJS.md.decrypt(c.e, CryptoJS.Ib.ld.parse(eb.Sg ? W() : eb.Sc.innerHTML)), c = jQuery.parseJSON(c.toString(CryptoJS.Ib.ie)), d.Cd = n);
						for (var e = 0; e < c.length; e++) {
							c[e].loaded = n;
						}
						d.pa = c;
						d.Hc();
						d.Ra.be(c);
					},
					onreadystatechange: x(),
					error: function(c, e, l) {
						A("Error loading JSON file (" + c.statusText + "," + l + "). Please check your configuration.", "onDocumentLoadedError", d.ia, c.responseText != p && 0 == c.responseText.indexOf("Error:") ? c.responseText.substr(6) : "");
					}
				});
			},
			getDimensions: function(c, d) {
				var e = this.pa.length;
				c == p && (c = 0);
				d == p && (d = e);
				if (this.dimensions == p || d && c) {
					this.dimensions == p && (this.dimensions = [], this.gb = []);
					for (e = c; e < d; e++) {
						this.pa[e].loaded ? (this.dimensions[e] = [], this.ii(e), this.Hb == p && (this.Hb = this.dimensions[e])) : this.Hb != p && (this.dimensions[e] = [], this.dimensions[e].page = e, this.dimensions[e].loaded = v, this.dimensions[e].width = this.Hb.width, this.dimensions[e].height = this.Hb.height, this.dimensions[e].Aa = this.Hb.Aa, this.dimensions[e].Oa = this.Hb.Oa);
					}
				}
				return this.dimensions;
			},
			ii: function(c) {
				if (this.dimensions[c]) {
					this.dimensions[c].page = c;
					this.dimensions[c].loaded = n;
					this.dimensions[c].width = this.pa[c][this.JSONPageDataFormat.wd];
					this.dimensions[c].height = this.pa[c][this.JSONPageDataFormat.vd];
					this.dimensions[c].Aa = this.dimensions[c].width;
					this.dimensions[c].Oa = this.dimensions[c].height;
					this.gb[c] = [];
					this.gb[c] = "";
					900 < this.dimensions[c].width && (this.dimensions[c].width = 918, this.dimensions[c].height = 1188);
					for (var d = 0, e; e = this.pa[c][this.JSONPageDataFormat.jd][d++];) {
						this.Ma ? !isNaN(e[0].toString()) && 0 <= Number(e[0].toString()) && (!isNaN(e[1].toString()) && 0 <= Number(e[1].toString()) && !isNaN(e[2].toString()) && 0 < Number(e[2].toString()) && !isNaN(e[3].toString()) && 0 < Number(e[3].toString())) && (this.gb[c] += e[5]) : !isNaN(e[this.JSONPageDataFormat.uc].toString()) && 0 <= Number(e[this.JSONPageDataFormat.uc].toString()) && (!isNaN(e[this.JSONPageDataFormat.wc].toString()) && 0 <= Number(e[this.JSONPageDataFormat.wc].toString()) && !isNaN(e[this.JSONPageDataFormat.xc].toString()) && 0 < Number(e[this.JSONPageDataFormat.xc].toString()) && !isNaN(e[this.JSONPageDataFormat.tc].toString()) && 0 < Number(e[this.JSONPageDataFormat.tc].toString())) && (this.gb[c] += e[this.JSONPageDataFormat.fc]);
					}
					this.gb[c] = this.gb[c].toLowerCase();
				}
			},
			Ic: function(c) {
				this.ib = v;
				if (c.ba == C || c.ba == U) {
					c.ba == C && c.ea(c.la).addClass("flexpaper_hidden"), this.sb ? c.ea(c.va).append("<object data='" + this.qa + "' type='image/svg+xml' id='" + c.page + "' class='flexpaper_interactivearea flexpaper_border flexpaper_grab flexpaper_hidden flexpaper_rescale' style='" + c.getDimensions() + "' /></div>") : c.ea(c.va).append("<img alt='' src='" + this.qa + "' id='" + c.page + "' class='flexpaper_interactivearea flexpaper_border flexpaper_grab flexpaper_hidden flexpaper_rescale' style='" + c.getDimensions() + ";background-size:cover;' />"), c.ba == U && 0 == c.pageNumber && this.mf(c, c.la);
				}
				c.ba == M && jQuery(c.la).append("<img src='" + this.qa + "' alt='" + this.xa(c.pageNumber + 1) + "'  id='" + c.page + "' class='flexpaper_hidden' style='" + c.getDimensions() + "'/>");
				c.ba == this.Na(c) && this.kb(c).Ic(this, c);
				if (c.ba == H || c.ba == D) {
					0 == c.pageNumber && (jQuery(c.la + "_1").append("<img id='" + c.Mb + "_1' class='flexpaper_pageLoader' src='" + this.Mc + "' style='position:absolute;left:50%;top:" + c.Xa() / 4 + "px;margin-left:-32px;' />"), jQuery(c.la + "_1").append("<img src='" + this.qa + "' alt='" + this.xa(c.pageNumber + 1) + "'  id='" + c.page + "' class='flexpaper_interactivearea flexpaper_grab flexpaper_hidden flexpaper_load_on_demand' style='" + c.getDimensions() + ";position:absolute;background-size:cover;'/>"), jQuery(c.la + "_1").append("<div id='" + c.na + "_1_textoverlay' style='position:relative;left:0px;top:0px;width:100%;height:100%;'></div>")), 1 == c.pageNumber && (jQuery(c.la + "_2").append("<img id='" + c.Mb + "_2' class='flexpaper_pageLoader' src='" + this.Mc + "' style='position:absolute;left:50%;top:" + c.Xa() / 4 + "px;margin-left:-32px;' />"), jQuery(c.la + "_2").append("<img src='" + this.qa + "' alt='" + this.xa(c.pageNumber + 1) + "'  id='" + c.page + "' class='flexpaper_interactivearea flexpaper_grab flexpaper_hidden flexpaper_load_on_demand' style='" + c.getDimensions() + ";position:absolute;left:0px;top:0px;background-size:cover;'/>"), jQuery(c.la + "_2").append("<div id='" + c.na + "_2_textoverlay' style='position:absolute;left:0px;top:0px;width:100%;height:100%;'></div>"));
				}
			},
			Id: function(c) {
				return this.Kc.replace("{page}", c);
			},
			xa: function(c, d, e) {
				this.config.PageIndexAdjustment && (c += this.config.PageIndexAdjustment);
				this.Cd && (c = CryptoJS.md.encrypt(c.toString(), CryptoJS.Ib.ld.parse(eb.Sg ? W() : eb.Sc.innerHTML)).toString());
				return !e || e && !this.pageSVGImagePattern ? d ? this.pageThumbImagePattern != p && 0 < this.pageThumbImagePattern.length ? 0 < this.pageThumbImagePattern.indexOf("?") ? this.pageThumbImagePattern.replace("{page}", c) + "&resolution=" + d : this.pageThumbImagePattern.replace("{page}", c) + "?resolution=" + d : 0 < this.pageImagePattern.indexOf("?") ? this.pageImagePattern.replace("{page}", c) + "&resolution=" + d : this.pageImagePattern.replace("{page}", c) + "?resolution=" + d : this.pageImagePattern.replace("{page}", c) : d ? this.pageThumbImagePattern != p && 0 < this.pageThumbImagePattern.length ? this.pageThumbImagePattern.replace("{page}", c) : 0 < this.pageSVGImagePattern.indexOf("?") ? this.pageSVGImagePattern.replace("{page}", c) + "&resolution=" + d : this.pageSVGImagePattern.replace("{page}", c) + "?resolution=" + d : this.pageSVGImagePattern.replace("{page}", c);
			},
			tb: function(c, d) {
				return this.zg.replace("{page}", c).replace("{sector}", d);
			},
			gf: function(c) {
				return c + (10 - c % 10);
			},
			Lc: function(c, d, e) {
				var f = this;
				f.Nc != f.gf(c) && (f.Nc = f.gf(c), jQuery.ajax({
					url: f.Id(f.Nc),
					dataType: f.config.JSONDataType,
					async: d,
					success: function(c) {
						c.e && (c = CryptoJS.md.decrypt(c.e, CryptoJS.Ib.ld.parse(eb.Sg ? W() : eb.Sc.innerHTML)), c = jQuery.parseJSON(c.toString(CryptoJS.Ib.ie)), f.Cd = n);
						if (0 < c.length) {
							for (var d = 0; d < c.length; d++) {
								var g = parseInt(c[d].number) - 1;
								f.pa[g] = c[d];
								f.pa[g].loaded = n;
								f.ii(g);
							}
							f.Ra.be(f.pa);
							jQuery(f).trigger("onTextDataUpdated");
							e != p && e();
						}
						f.Nc = p;
					},
					error: function(c) {
						A("Error loading JSON file (" + c.statusText + "). Please check your configuration.", "onDocumentLoadedError", f.ia);
						f.Nc = p;
					}
				}));
			},
			Ia: function(c) {
				return c.Kd;
			},
			Ea: function(c, d) {
				c.Kd = d;
			},
			Eb: function(c, d, e) {
				var f = this;
				if (!(c.za && c.ba != f.Na(c))) {
					if (c.ba != f.Na(c) && -1 < f.Ia(c)) {
						window.clearTimeout(c.Pb), c.Pb = setTimeout(function() {
							f.Eb(c, d, e);
						}, 250);
					} else {
						if (c.ba == C || c.ba == M) {
							if (!c.za && (jQuery(c.Da).attr("src") == f.qa || f.sb) && !c.Qd) {
								f.Ea(c, c.pageNumber), c.dimensions.loaded || f.Lc(c.pageNumber + 1, n, function() {
									f.Rb(c);
								}), c.rc(), f.ua = new Image, jQuery(f.ua).bind("load", function() {
									c.Qd = n;
									c.Ae = this.height;
									c.Be = this.width;
									f.Wb(c);
									c.dimensions.Aa > c.dimensions.width && (c.dimensions.width = c.dimensions.Aa, c.dimensions.height = c.dimensions.Oa, (c.ba == C || c.ba == U) && c.Qa());
								}).bind("error", function() {
									A("Error loading image (" + this.src + ")", "onErrorLoadingPage", f.ia, c.pageNumber);
								}), jQuery(f.ua).bind("error", function() {
									f.Ea(c, -1);
								}), jQuery(f.ua).attr("src", f.xa(c.pageNumber + 1, c.ba == M ? 200 : p));
							}!c.za && (jQuery(c.Da).attr("src") == f.qa && c.Qd) && f.Wb(c);
							e != p && e();
						}
						c.ba == f.Na(c) && (c.dimensions.loaded || f.Lc(c.pageNumber + 1, n, function() {
							f.Rb(c);
						}), f.kb(c).Eb(f, c, d, e));
						c.ba == U && (c.Lb || (c.rc(), c.Lb = n), 0 == c.pageNumber && (f.Ea(c, c.pages.ja), f.getDimensions()[f.Ia(c)].loaded || f.Lc(f.Ia(c) + 1, n, function() {
							f.Rb(c);
						}), f.ua = new Image, jQuery(f.ua).bind("load", function() {
							c.Qd = n;
							c.Ae = this.height;
							c.Be = this.width;
							c.Jb();
							f.Wb(c);
							c.dimensions.Aa > c.dimensions.width && (c.dimensions.width = c.dimensions.Aa, c.dimensions.height = c.dimensions.Oa, c.Qa());
							c.za || jQuery("#" + f.ia).trigger("onPageLoaded", c.pageNumber + 1);
							c.za = n;
							f.Ea(c, -1);
						}), jQuery(f.ua).bind("error", function() {
							c.Jb();
							f.Ea(c, -1);
						}), jQuery(f.ua).attr("src", f.xa(c.pages.ja + 1)), jQuery(c.la + "_1").removeClass("flexpaper_load_on_demand"), e != p && e()));
						if (c.ba == H || c.ba == D) {
							if (c.Lb || (c.rc(), c.Lb = n), 0 == c.pageNumber) {
								jQuery(c.Da), c.ba == D ? f.Ea(c, 0 != c.pages.ja ? c.pages.ja : c.pages.ja + 1) : c.ba == H && f.Ea(c, c.pages.ja), f.getDimensions()[f.Ia(c) - 1] && !f.getDimensions()[f.Ia(c) - 1].loaded && f.Lc(f.Ia(c) + 1, n, function() {
									f.Rb(c);
								}), f.ua = new Image, jQuery(f.ua).bind("load", function() {
									c.Qd = n;
									c.Ae = this.height;
									c.Be = this.width;
									c.Jb();
									f.Wb(c);
									c.dimensions.Aa > c.dimensions.width && (c.dimensions.width = c.dimensions.Aa, c.dimensions.height = c.dimensions.Oa, c.Qa());
									c.za || jQuery("#" + f.ia).trigger("onPageLoaded", c.pageNumber + 1);
									c.za = n;
									f.Ea(c, -1);
								}), jQuery(f.ua).bind("error", function() {
									c.Jb();
									f.Ea(c, -1);
								}), c.ba == D && jQuery(f.ua).attr("src", f.xa(0 != c.pages.ja ? c.pages.ja : c.pages.ja + 1)), c.ba == H && jQuery(f.ua).attr("src", f.xa(c.pages.ja + 1)), jQuery(c.la + "_1").removeClass("flexpaper_load_on_demand"), e != p && e();
							} else {
								if (1 == c.pageNumber) {
									var k = jQuery(c.Da);
									c.pages.ja + 1 > c.pages.getTotalPages() ? k.attr("src", "") : (0 != c.pages.ja || c.ba == H ? (f.Ea(c, c.pages.ja + 1), f.ua = new Image, jQuery(f.ua).bind("load", function() {
										c.Jb();
										f.Wb(c);
										c.dimensions.Aa > c.dimensions.width && (c.dimensions.width = c.dimensions.Aa, c.dimensions.height = c.dimensions.Oa);
										c.za || jQuery("#" + f.ia).trigger("onPageLoaded", c.pageNumber + 1);
										c.za = n;
										f.Ea(c, -1);
									}), jQuery(f.ua).bind("error", function() {
										f.Ea(c, -1);
										c.Jb();
									})) : c.Jb(), c.ba == D && jQuery(f.ua).attr("src", f.xa(c.pages.ja + 1)), c.ba == H && jQuery(f.ua).attr("src", f.xa(c.pages.ja + 2)), 1 < c.pages.ja && jQuery(c.la + "_2").removeClass("flexpaper_hidden"), jQuery(c.la + "_2").removeClass("flexpaper_load_on_demand"));
									e != p && e();
								}
							}
						}
					}
				}
			},
			Wb: function(c) {
				if (c.ba == C && (Math.round(100 * (c.Be / c.Ae)) != Math.round(100 * (c.dimensions.width / c.dimensions.height)) || this.sb) && !(eb.browser.msie && 9 > eb.browser.version)) {
					this.sb ? (jQuery(c.Da).attr("data", this.xa(c.pageNumber + 1, p, n)), jQuery(c.la).removeClass("flexpaper_load_on_demand"), jQuery(c.Da).css("width", jQuery(c.Da).css("width"))) : (jQuery(c.Da).css("background-image", "url('" + this.xa(c.pageNumber + 1) + "')"), jQuery(c.Da).attr("src", this.qa)), jQuery("#" + c.Mb).hide(), c.za || jQuery("#" + this.ia).trigger("onPageLoaded", c.pageNumber + 1), c.za = n;
				} else {
					if (c.ba == this.Na(c)) {
						this.kb(c).Wb(this, c);
					} else {
						if (c.ba == H || c.ba == D) {
							if (0 == c.pageNumber) {
								var d = c.ba == D ? 0 != c.pages.ja ? c.pages.ja : c.pages.ja + 1 : c.pages.ja + 1;
								c.wf != d && (eb.browser.msie || eb.browser.safari && 5 > eb.browser.vb ? jQuery(c.Da).attr("src", this.xa(d)) : jQuery(c.Da).css("background-image", "url('" + this.xa(d) + "')"), jQuery(c.la + "_1").removeClass("flexpaper_hidden"), c.wf = d);
								jQuery(c.Da).removeClass("flexpaper_hidden");
							}
							1 == c.pageNumber && (d = c.ba == D ? c.pages.ja + 1 : c.pages.ja + 2, c.wf != d && (eb.browser.msie || eb.browser.safari && 5 > eb.browser.vb ? jQuery(c.Da).attr("src", this.xa(d)) : jQuery(c.Da).css("background-image", "url('" + this.xa(d) + "')"), c.wf = d, c.ba == H && jQuery(c.la + "_2").removeClass("flexpaper_hidden")), jQuery(c.Da).removeClass("flexpaper_hidden"));
						} else {
							c.ba == U ? jQuery(c.Da).attr("src", this.xa(this.Ia(c) + 1)) : this.sb ? (jQuery(c.Da).attr("data", this.xa(c.pageNumber + 1, p, n)), jQuery(c.la).removeClass("flexpaper_load_on_demand")) : jQuery(c.Da).attr("src", this.xa(c.pageNumber + 1), c.ba == M ? 200 : p), jQuery("#" + c.Mb).hide();
						}
						c.za || jQuery("#" + this.ia).trigger("onPageLoaded", c.pageNumber + 1);
						c.za = n;
					}
				}
				this.Ea(c, -1);
				this.Rd || (this.Rd = n, window.$FlexPaper(this.ia).tf());
			},
			ci: function(c) {
				c.ba == H || c.ba == D ? (0 == c.pageNumber && jQuery(c.wa).css("background-image", "url(" + this.qa + ")"), 1 == c.pageNumber && jQuery(c.wa).css("background-image", "url(" + this.qa + ")")) : jQuery(c.wa).css("background-image", "url(" + this.qa + ")");
			},
			unload: function(c) {
				jQuery(c.la).addClass("flexpaper_load_on_demand");
				var d = p;
				if (c.ba == C || c.ba == M || c.ba == U) {
					d = jQuery(c.Da);
				}
				if (c.ba == H || c.ba == D) {
					d = jQuery(c.Da), jQuery(c.Da).addClass("flexpaper_hidden");
				}
				c.ba == this.Na(c) && this.kb(c).unload(this, c);
				d != p && 0 < d.length && (d.attr("alt", d.attr("src")), d.attr("src", "data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///yH5BAEAAAAALAAAAAABAAEAAAIBRAA7"));
				c.Lb = v;
				c.wf = -1;
				jQuery(".flexpaper_pageword_" + this.ia + "_page_" + c.pageNumber + ":not(.flexpaper_selected_searchmatch, .flexpaper_annotation_" + this.ia + ")").remove();
				c.Cg && c.Cg();
				jQuery(".flexpaper_annotation_" + this.ia + "_page_" + c.pageNumber).remove();
			},
			getNumPages: function() {
				return this.pa.length;
			},
			Rb: function(c, d, e, f) {
				this.Ra.Rb(c, d, e, f);
			},
			Qb: function(c, d, e) {
				this.Ra.Qb(c, d, e);
			},
			Ka: function(c, d, e) {
				this.Ra.Ka(c, e);
			},
			mf: function(c, d) {
				if (this.ib) {
					if (c.scale < c.we()) {
						c.qi = d, c.si = v;
					} else {
						!d && c.qi && (d = c.qi);
						var e = 0.25 * Math.round(c.kg()),
							f = 0.25 * Math.round(c.jg());
						jQuery(".flexpaper_flipview_canvas_highres_" + c.pageNumber).remove();
						d == p && (d = c.la);
						var k = eb.platform.Jc || eb.platform.android ? "flexpaper_flipview_canvas_highres" : c.na + "_canvas_highres";
						jQuery(d).append(String.format("<div id='" + c.na + "_canvas_highres_l1t1' class='{4}' style='z-index:11;position:relative;float:left;background-repeat:no-repeat;background-size:100% 100%;width:{2}px;height:{3}px;clear:both;'></div>", 0, 0, e, f, k) + String.format("<div id='" + c.na + "_canvas_highres_l2t1' class='{4}' style='z-index:11;position:relative;float:left;background-repeat-no-repeat;background-size:100% 100%;width:{2}px;height:{3}px;'></div>", e + 0 + 0, 0, e, f, k) + String.format("<div id='" + c.na + "_canvas_highres_r1t1' class='{4}' style='z-index:11;position:relative;float:left;background-repeat-no-repeat;background-size:100% 100%;width:{2}px;height:{3}px;'></div>", 2 * e + 0, 0, e, f, k) + String.format("<div id='" + c.na + "_canvas_highres_r2t1' class='{4}' style='z-index:11;position:relative;float:left;background-repeat-no-repeat;background-size:100% 100%;width:{2}px;height:{3}px;'></div>", 3 * e + 0, 0, e, f, k) + String.format("<div id='" + c.na + "_canvas_highres_l1t2' class='{4}' style='z-index:11;position:relative;float:left;background-repeat-no-repeat;background-size:100% 100%;width:{2}px;height:{3}px;clear:both;'></div>", 0, f + 0 + 0, e, f, k) + String.format("<div id='" + c.na + "_canvas_highres_l2t2' class='{4}' style='z-index:11;position:relative;float:left;background-repeat-no-repeat;background-size:100% 100%;width:{2}px;height:{3}px;'></div>", e + 0 + 0, f + 0 + 0, e, f, k) + String.format("<div id='" + c.na + "_canvas_highres_r1t2' class='{4}' style='z-index:11;position:relative;float:left;background-repeat-no-repeat;background-size:100% 100%;width:{2}px;height:{3}px;'></div>", 2 * e + 0, f + 0 + 0, e, f, k) + String.format("<div id='" + c.na + "_canvas_highres_r2t2' class='{4}' style='z-index:11;position:relative;float:left;background-repeat-no-repeat;background-size:100% 100%;width:{2}px;height:{3}px;'></div>", 3 * e + 0, f + 0 + 0, e, f, k) + String.format("<div id='" + c.na + "_canvas_highres_l1b1' class='{4}' style='z-index:11;position:relative;float:left;background-repeat-no-repeat;background-size:100% 100%;width:{2}px;height:{3}px;clear:both;'></div>", 0, 2 * f + 0, e, f, k) + String.format("<div id='" + c.na + "_canvas_highres_l2b1' class='{4}' style='z-index:11;position:relative;float:left;background-repeat-no-repeat;background-size:100% 100%;width:{2}px;height:{3}px;'></div>", e + 0 + 0, 2 * f + 0, e, f, k) + String.format("<div id='" + c.na + "_canvas_highres_r1b1' class='{4}' style='z-index:11;position:relative;float:left;background-repeat-no-repeat;background-size:100% 100%;width:{2}px;height:{3}px;'></div>", 2 * e + 0, 2 * f + 0, e, f, k) + String.format("<div id='" + c.na + "_canvas_highres_r2b1' class='{4}' style='z-index:11;position:relative;float:left;background-repeat-no-repeat;background-size:100% 100%;width:{2}px;height:{3}px;'></div>", 3 * e + 0, 2 * f + 0, e, f, k) + String.format("<div id='" + c.na + "_canvas_highres_l1b2' class='{4}' style='z-index:11;position:relative;float:left;background-repeat-no-repeat;background-size:100% 100%;width:{2}px;height:{3}px;clear:both;'></div>", 0, 3 * f + 0, e, f, k) + String.format("<div id='" + c.na + "_canvas_highres_l2b2' class='{4}' style='z-index:11;position:relative;float:left;background-repeat-no-repeat;background-size:100% 100%;width:{2}px;height:{3}px;'></div>", e + 0 + 0, 3 * f + 0, e, f, k) + String.format("<div id='" + c.na + "_canvas_highres_r1b2' class='{4}' style='z-index:11;position:relative;float:left;background-repeat-no-repeat;background-size:100% 100%;width:{2}px;height:{3}px;'></div>", 2 * e + 0, 3 * f + 0, e, f, k) + String.format("<div id='" + c.na + "_canvas_highres_r2b2' class='{4}' style='z-index:11;position:relative;float:left;background-repeat-no-repeat;background-size:100% 100%;width:{2}px;height:{3}px;'></div>", 3 * e + 0, 3 * f + 0, e, f, k) + "");
						c.si = n;
					}
				}
			},
			dc: function(c) {
				if (!(c.scale < c.we())) {
					!c.si && this.ib && this.mf(c);
					if (this.ib) {
						var d = document.getElementById(c.na + "_canvas_highres_l1t1"),
							e = document.getElementById(c.na + "_canvas_highres_l2t1"),
							f = document.getElementById(c.na + "_canvas_highres_l1t2"),
							k = document.getElementById(c.na + "_canvas_highres_l2t2"),
							l = document.getElementById(c.na + "_canvas_highres_r1t1"),
							g = document.getElementById(c.na + "_canvas_highres_r2t1"),
							m = document.getElementById(c.na + "_canvas_highres_r1t2"),
							q = document.getElementById(c.na + "_canvas_highres_r2t2"),
							y = document.getElementById(c.na + "_canvas_highres_l1b1"),
							z = document.getElementById(c.na + "_canvas_highres_l2b1"),
							t = document.getElementById(c.na + "_canvas_highres_l1b2"),
							s = document.getElementById(c.na + "_canvas_highres_l2b2"),
							r = document.getElementById(c.na + "_canvas_highres_r1b1"),
							w = document.getElementById(c.na + "_canvas_highres_r2b1"),
							G = document.getElementById(c.na + "_canvas_highres_r1b2"),
							I = document.getElementById(c.na + "_canvas_highres_r2b2");
						if (1 == c.pageNumber && 1 == c.pages.ja || c.pageNumber == c.pages.ja - 1 || c.pageNumber == c.pages.ja - 2) {
							var B = c.ba == this.Na(c) ? c.pages.da : p,
								J = c.ba == this.Na(c) ? c.pageNumber + 1 : c.pages.ja + 1;
							jQuery(d).visible(n, B) && "none" === jQuery(d).css("background-image") && jQuery(d).css("background-image", "url('" + this.tb(J, "l1t1") + "')");
							jQuery(e).visible(n, B) && "none" === jQuery(e).css("background-image") && jQuery(e).css("background-image", "url('" + this.tb(J, "l2t1") + "')");
							jQuery(f).visible(n, B) && "none" === jQuery(f).css("background-image") && jQuery(f).css("background-image", "url('" + this.tb(J, "l1t2") + "')");
							jQuery(k).visible(n, B) && "none" === jQuery(k).css("background-image") && jQuery(k).css("background-image", "url('" + this.tb(J, "l2t2") + "')");
							jQuery(l).visible(n, B) && "none" === jQuery(l).css("background-image") && jQuery(l).css("background-image", "url('" + this.tb(J, "r1t1") + "')");
							jQuery(g).visible(n, B) && "none" === jQuery(g).css("background-image") && jQuery(g).css("background-image", "url('" + this.tb(J, "r2t1") + "')");
							jQuery(m).visible(n, B) && "none" === jQuery(m).css("background-image") && jQuery(m).css("background-image", "url('" + this.tb(J, "r1t2") + "')");
							jQuery(q).visible(n, B) && "none" === jQuery(q).css("background-image") && jQuery(q).css("background-image", "url('" + this.tb(J, "r2t2") + "')");
							jQuery(y).visible(n, B) && "none" === jQuery(y).css("background-image") && jQuery(y).css("background-image", "url('" + this.tb(J, "l1b1") + "')");
							jQuery(z).visible(n, B) && "none" === jQuery(z).css("background-image") && jQuery(z).css("background-image", "url('" + this.tb(J, "l2b1") + "')");
							jQuery(t).visible(n, B) && "none" === jQuery(t).css("background-image") && jQuery(t).css("background-image", "url('" + this.tb(J, "l1b2") + "')");
							jQuery(s).visible(n, B) && "none" === jQuery(s).css("background-image") && jQuery(s).css("background-image", "url('" + this.tb(J, "l2b2") + "')");
							jQuery(r).visible(n, B) && "none" === jQuery(r).css("background-image") && jQuery(r).css("background-image", "url('" + this.tb(J, "r1b1") + "')");
							jQuery(w).visible(n, B) && "none" === jQuery(w).css("background-image") && jQuery(w).css("background-image", "url('" + this.tb(J, "r2b1") + "')");
							jQuery(G).visible(n, B) && "none" === jQuery(G).css("background-image") && jQuery(G).css("background-image", "url('" + this.tb(J, "r1b2") + "')");
							jQuery(I).visible(n, B) && "none" === jQuery(I).css("background-image") && jQuery(I).css("background-image", "url('" + this.tb(J, "r2b2") + "')");
						}
					}
					c.ai = n;
				}
			},
			Sb: function(c) {
				if (this.ib) {
					var d = eb.platform.Jc || eb.platform.android ? "flexpaper_flipview_canvas_highres" : c.na + "_canvas_highres";
					c.ai && 0 < jQuery("." + d).length && (jQuery("." + d).css("background-image", ""), c.ai = v);
				}
			}
		};
		return g;
	}(),
	CanvasPageRenderer = window.CanvasPageRenderer = function() {
		function g(c, d, e, f) {
			this.ia = c;
			this.file = d;
			this.jsDirectory = e;
			this.initialized = v;
			this.JSONPageDataFormat = this.Pa = this.dimensions = p;
			this.pageThumbImagePattern = f.pageThumbImagePattern;
			this.pageImagePattern = f.pageImagePattern;
			this.config = f;
			this.Ze = this.ia + "_dummyPageCanvas_[pageNumber]";
			this.Xf = "#" + this.Ze;
			this.$e = this.ia + "dummyPageCanvas2_[pageNumber]";
			this.Yf = "#" + this.$e;
			this.ob = [];
			this.context = this.wa = p;
			this.La = [];
			this.spacedTextList = [];
			this.Mc = "data:image/gif;base64,R0lGODlhIAAgAPMAAP///wAAAMbGxoSEhLa2tpqamjY2NlZWVtjY2OTk5Ly8vB4eHgQEBAAAAAAAAAAAACH/C05FVFNDQVBFMi4wAwEAAAAh/hpDcmVhdGVkIHdpdGggYWpheGxvYWQuaW5mbwAh+QQJCgAAACwAAAAAIAAgAAAE5xDISWlhperN52JLhSSdRgwVo1ICQZRUsiwHpTJT4iowNS8vyW2icCF6k8HMMBkCEDskxTBDAZwuAkkqIfxIQyhBQBFvAQSDITM5VDW6XNE4KagNh6Bgwe60smQUB3d4Rz1ZBApnFASDd0hihh12BkE9kjAJVlycXIg7CQIFA6SlnJ87paqbSKiKoqusnbMdmDC2tXQlkUhziYtyWTxIfy6BE8WJt5YJvpJivxNaGmLHT0VnOgSYf0dZXS7APdpB309RnHOG5gDqXGLDaC457D1zZ/V/nmOM82XiHRLYKhKP1oZmADdEAAAh+QQJCgAAACwAAAAAIAAgAAAE6hDISWlZpOrNp1lGNRSdRpDUolIGw5RUYhhHukqFu8DsrEyqnWThGvAmhVlteBvojpTDDBUEIFwMFBRAmBkSgOrBFZogCASwBDEY/CZSg7GSE0gSCjQBMVG023xWBhklAnoEdhQEfyNqMIcKjhRsjEdnezB+A4k8gTwJhFuiW4dokXiloUepBAp5qaKpp6+Ho7aWW54wl7obvEe0kRuoplCGepwSx2jJvqHEmGt6whJpGpfJCHmOoNHKaHx61WiSR92E4lbFoq+B6QDtuetcaBPnW6+O7wDHpIiK9SaVK5GgV543tzjgGcghAgAh+QQJCgAAACwAAAAAIAAgAAAE7hDISSkxpOrN5zFHNWRdhSiVoVLHspRUMoyUakyEe8PTPCATW9A14E0UvuAKMNAZKYUZCiBMuBakSQKG8G2FzUWox2AUtAQFcBKlVQoLgQReZhQlCIJesQXI5B0CBnUMOxMCenoCfTCEWBsJColTMANldx15BGs8B5wlCZ9Po6OJkwmRpnqkqnuSrayqfKmqpLajoiW5HJq7FL1Gr2mMMcKUMIiJgIemy7xZtJsTmsM4xHiKv5KMCXqfyUCJEonXPN2rAOIAmsfB3uPoAK++G+w48edZPK+M6hLJpQg484enXIdQFSS1u6UhksENEQAAIfkECQoAAAAsAAAAACAAIAAABOcQyEmpGKLqzWcZRVUQnZYg1aBSh2GUVEIQ2aQOE+G+cD4ntpWkZQj1JIiZIogDFFyHI0UxQwFugMSOFIPJftfVAEoZLBbcLEFhlQiqGp1Vd140AUklUN3eCA51C1EWMzMCezCBBmkxVIVHBWd3HHl9JQOIJSdSnJ0TDKChCwUJjoWMPaGqDKannasMo6WnM562R5YluZRwur0wpgqZE7NKUm+FNRPIhjBJxKZteWuIBMN4zRMIVIhffcgojwCF117i4nlLnY5ztRLsnOk+aV+oJY7V7m76PdkS4trKcdg0Zc0tTcKkRAAAIfkECQoAAAAsAAAAACAAIAAABO4QyEkpKqjqzScpRaVkXZWQEximw1BSCUEIlDohrft6cpKCk5xid5MNJTaAIkekKGQkWyKHkvhKsR7ARmitkAYDYRIbUQRQjWBwJRzChi9CRlBcY1UN4g0/VNB0AlcvcAYHRyZPdEQFYV8ccwR5HWxEJ02YmRMLnJ1xCYp0Y5idpQuhopmmC2KgojKasUQDk5BNAwwMOh2RtRq5uQuPZKGIJQIGwAwGf6I0JXMpC8C7kXWDBINFMxS4DKMAWVWAGYsAdNqW5uaRxkSKJOZKaU3tPOBZ4DuK2LATgJhkPJMgTwKCdFjyPHEnKxFCDhEAACH5BAkKAAAALAAAAAAgACAAAATzEMhJaVKp6s2nIkolIJ2WkBShpkVRWqqQrhLSEu9MZJKK9y1ZrqYK9WiClmvoUaF8gIQSNeF1Er4MNFn4SRSDARWroAIETg1iVwuHjYB1kYc1mwruwXKC9gmsJXliGxc+XiUCby9ydh1sOSdMkpMTBpaXBzsfhoc5l58Gm5yToAaZhaOUqjkDgCWNHAULCwOLaTmzswadEqggQwgHuQsHIoZCHQMMQgQGubVEcxOPFAcMDAYUA85eWARmfSRQCdcMe0zeP1AAygwLlJtPNAAL19DARdPzBOWSm1brJBi45soRAWQAAkrQIykShQ9wVhHCwCQCACH5BAkKAAAALAAAAAAgACAAAATrEMhJaVKp6s2nIkqFZF2VIBWhUsJaTokqUCoBq+E71SRQeyqUToLA7VxF0JDyIQh/MVVPMt1ECZlfcjZJ9mIKoaTl1MRIl5o4CUKXOwmyrCInCKqcWtvadL2SYhyASyNDJ0uIiRMDjI0Fd30/iI2UA5GSS5UDj2l6NoqgOgN4gksEBgYFf0FDqKgHnyZ9OX8HrgYHdHpcHQULXAS2qKpENRg7eAMLC7kTBaixUYFkKAzWAAnLC7FLVxLWDBLKCwaKTULgEwbLA4hJtOkSBNqITT3xEgfLpBtzE/jiuL04RGEBgwWhShRgQExHBAAh+QQJCgAAACwAAAAAIAAgAAAE7xDISWlSqerNpyJKhWRdlSAVoVLCWk6JKlAqAavhO9UkUHsqlE6CwO1cRdCQ8iEIfzFVTzLdRAmZX3I2SfZiCqGk5dTESJeaOAlClzsJsqwiJwiqnFrb2nS9kmIcgEsjQydLiIlHehhpejaIjzh9eomSjZR+ipslWIRLAgMDOR2DOqKogTB9pCUJBagDBXR6XB0EBkIIsaRsGGMMAxoDBgYHTKJiUYEGDAzHC9EACcUGkIgFzgwZ0QsSBcXHiQvOwgDdEwfFs0sDzt4S6BK4xYjkDOzn0unFeBzOBijIm1Dgmg5YFQwsCMjp1oJ8LyIAACH5BAkKAAAALAAAAAAgACAAAATwEMhJaVKp6s2nIkqFZF2VIBWhUsJaTokqUCoBq+E71SRQeyqUToLA7VxF0JDyIQh/MVVPMt1ECZlfcjZJ9mIKoaTl1MRIl5o4CUKXOwmyrCInCKqcWtvadL2SYhyASyNDJ0uIiUd6GGl6NoiPOH16iZKNlH6KmyWFOggHhEEvAwwMA0N9GBsEC6amhnVcEwavDAazGwIDaH1ipaYLBUTCGgQDA8NdHz0FpqgTBwsLqAbWAAnIA4FWKdMLGdYGEgraigbT0OITBcg5QwPT4xLrROZL6AuQAPUS7bxLpoWidY0JtxLHKhwwMJBTHgPKdEQAACH5BAkKAAAALAAAAAAgACAAAATrEMhJaVKp6s2nIkqFZF2VIBWhUsJaTokqUCoBq+E71SRQeyqUToLA7VxF0JDyIQh/MVVPMt1ECZlfcjZJ9mIKoaTl1MRIl5o4CUKXOwmyrCInCKqcWtvadL2SYhyASyNDJ0uIiUd6GAULDJCRiXo1CpGXDJOUjY+Yip9DhToJA4RBLwMLCwVDfRgbBAaqqoZ1XBMHswsHtxtFaH1iqaoGNgAIxRpbFAgfPQSqpbgGBqUD1wBXeCYp1AYZ19JJOYgH1KwA4UBvQwXUBxPqVD9L3sbp2BNk2xvvFPJd+MFCN6HAAIKgNggY0KtEBAAh+QQJCgAAACwAAAAAIAAgAAAE6BDISWlSqerNpyJKhWRdlSAVoVLCWk6JKlAqAavhO9UkUHsqlE6CwO1cRdCQ8iEIfzFVTzLdRAmZX3I2SfYIDMaAFdTESJeaEDAIMxYFqrOUaNW4E4ObYcCXaiBVEgULe0NJaxxtYksjh2NLkZISgDgJhHthkpU4mW6blRiYmZOlh4JWkDqILwUGBnE6TYEbCgevr0N1gH4At7gHiRpFaLNrrq8HNgAJA70AWxQIH1+vsYMDAzZQPC9VCNkDWUhGkuE5PxJNwiUK4UfLzOlD4WvzAHaoG9nxPi5d+jYUqfAhhykOFwJWiAAAIfkECQoAAAAsAAAAACAAIAAABPAQyElpUqnqzaciSoVkXVUMFaFSwlpOCcMYlErAavhOMnNLNo8KsZsMZItJEIDIFSkLGQoQTNhIsFehRww2CQLKF0tYGKYSg+ygsZIuNqJksKgbfgIGepNo2cIUB3V1B3IvNiBYNQaDSTtfhhx0CwVPI0UJe0+bm4g5VgcGoqOcnjmjqDSdnhgEoamcsZuXO1aWQy8KAwOAuTYYGwi7w5h+Kr0SJ8MFihpNbx+4Erq7BYBuzsdiH1jCAzoSfl0rVirNbRXlBBlLX+BP0XJLAPGzTkAuAOqb0WT5AH7OcdCm5B8TgRwSRKIHQtaLCwg1RAAAOwAAAAAAAAAAAA%3D%3D";
			this.pb = this.Rd = v;
			this.qa = "data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///yH5BAEAAAAALAAAAAABAAEAAAIBRAA7";
			this.qf = 1;
			this.gb = [];
			this.sf = {};
			this.JSONPageDataFormat = p;
			this.Xc = n;
			this.Ma = f.compressedJSONFormat != p ? f.compressedJSONFormat : n;
		}
		g.prototype = {
			sd: ca("CanvasPageRenderer"),
			Na: function(c) {
				return c.aa.ha ? c.aa.ha.ra : "";
			},
			kb: function(c) {
				return c.aa.ha.Dj;
			},
			Bb: function() {
				jQuery(this.Ra).unbind();
				this.Ra.Bb();
				delete this.Hc;
				this.Hc = p;
				delete this.dimensions;
				this.dimensions = p;
				delete this.Ra;
				this.Ra = p;
				delete this.La;
				this.La = p;
				delete this.spacedTextList;
				this.spacedTextList = p;
			},
			initialize: function(c, d) {
				var e = this;
				e.Hc = c;
				e.oc = eb.platform.oc;
				e.Fk = ("undefined" != e.jsDirectory && e.jsDirectory != p ? e.jsDirectory : "js/") + "pdf.min.js";
				e.JSONPageDataFormat = e.Ma ? {
					wd: "width",
					vd: "height",
					jd: "text",
					fc: "d",
					Ge: "f",
					uc: "l",
					wc: "t",
					xc: "w",
					tc: "h"
				} : {
					wd: e.config.JSONPageDataFormat.pageWidth,
					vd: e.config.JSONPageDataFormat.pageHeight,
					jd: e.config.JSONPageDataFormat.textCollection,
					fc: e.config.JSONPageDataFormat.textFragment,
					Ge: e.config.JSONPageDataFormat.textFont,
					uc: e.config.JSONPageDataFormat.textLeft,
					wc: e.config.JSONPageDataFormat.textTop,
					xc: e.config.JSONPageDataFormat.textWidth,
					tc: e.config.JSONPageDataFormat.textHeight
				};
				e.Fa = e.file.indexOf && 0 <= e.file.indexOf("[*,") && e.config && e.config.jsonfile != p;
				e.Fa && (e.jl = e.file.substr(e.file.indexOf("[*,"), e.file.indexOf("]") - e.file.indexOf("[*,")), e.Ah = e.Ah = v);
				jQuery.getScript(e.Fk, function() {
					if (e.Ah) {
						var c = new XMLHttpRequest;
						c.open("HEAD", e.Vf(1), v);
						c.overrideMimeType("application/pdf");
						c.onreadystatechange = function() {
							if (200 == c.status) {
								var d = c.getAllResponseHeaders(),
									k = {};
								if (d) {
									for (var d = d.split("\r\n"), l = 0; l < d.length; l++) {
										var g = d[l],
											u = g.indexOf(": ");
										0 < u && (k[g.substring(0, u)] = g.substring(u + 2));
									}
								}
								e.dh = "bytes" === k["Accept-Ranges"];
								e.Fj = "identity" === k["Content-Encoding"] || k["Content-Encoding"] === p || !k["Content-Encoding"];
								e.dh && (e.Fj && !eb.platform.ios && !eb.browser.safari) && (e.file = e.file.substr(0, e.file.indexOf(e.jl) - 1) + ".pdf", e.Fa = v);
							}
							c.abort();
						};
						try {
							c.send(p);
						} catch (k) {}
					}
					e.Ra = new la(e.ia, e.Fa, e.JSONPageDataFormat, n);
					window["wordPageList_" + e.ia] = e.Ra.La;
					jQuery("#" + e.ia).trigger("onDocumentLoading");
					PDFJS.workerSrc = ("undefined" != e.jsDirectory && e.jsDirectory != p ? e.jsDirectory : "js/") + "pdf.worker.min.js";
					PDFJS.disableWorker = e.Fa || eb.browser.zk || eb.browser.msie;
					PDFJS.disableRange = eb.platform.ios || e.Fa || eb.browser.safari;
					PDFJS.disableAutoFetch = eb.platform.ios || e.Fa;
					PDFJS.enableStats = v;
					PDFJS.um = n;
					PDFJS.vm = n;
					if (e.Fa) {
						e.Fa && (e.config && e.config.jsonfile != p) && (e.Fa = n, e.Kc = e.config.jsonfile, e.Pm = new Promise(x()), jQuery.ajax({
							url: e.Id(10),
							dataType: e.config.JSONDataType,
							success: function(c) {
								c.e && (c = CryptoJS.md.decrypt(c.e, CryptoJS.Ib.ld.parse(eb.Sg ? W() : eb.Sc.innerHTML)), c = jQuery.parseJSON(c.toString(CryptoJS.Ib.ie)), e.Cd = n);
								if (0 < c.length) {
									e.pa = Array(c[0].pages);
									for (var d = 0; d < c.length; d++) {
										e.pa[d] = c[d], e.pa[d].loaded = n, e.Lg(d);
									}
									0 < e.pa.length && (e.xb = e.pa[0].twofold, e.xb && (e.oc = 1));
									for (d = 0; d < e.pa.length; d++) {
										e.pa[d] == p && (e.pa[d] = [], e.pa[d].loaded = v);
									}
									e.Ra.be(e.pa);
								}
								e.od = 1;
								e.Pa = Array(c[0].pages);
								e.ob = Array(c[0].pages);
								e.sg(e.od, function() {
									e.Hc();
								});
							},
							error: function(c, d, f) {
								A("Error loading JSON file (" + c.statusText + "," + f + "). Please check your configuration.", "onDocumentLoadedError", e.ia, c.responseText != p && 0 == c.responseText.indexOf("Error:") ? c.responseText.substr(6) : "");
							}
						}));
					} else {
						e.Kc = e.config.jsonfile;
						var l = new jQuery.Deferred;
						if (e.Kc && 0 < e.Kc.length) {
							var g = jQuery.ajax({
								url: e.Id(10),
								dataType: e.config.JSONDataType,
								success: function(c) {
									c.e && (c = CryptoJS.md.decrypt(c.e, CryptoJS.Ib.ld.parse(eb.Sg ? W() : eb.Sc.innerHTML)), c = jQuery.parseJSON(c.toString(CryptoJS.Ib.ie)), e.Cd = n);
									if (0 < c.length) {
										e.pa = Array(c[0].pages);
										for (var d = 0; d < c.length; d++) {
											e.pa[d] = c[d], e.pa[d].loaded = n, e.Lg(d);
										}
										for (d = 0; d < e.pa.length; d++) {
											e.pa[d] == p && (e.pa[d] = [], e.pa[d].loaded = v);
										}
										e.Ra.be(e.pa);
										0 < e.pa.length && (e.xb = e.pa[0].twofold, e.xb && (e.oc = 1));
									}
								}
							});
							g.fail(function() {
								l.resolve();
							});
							g.then(function() {
								l.resolve();
							});
						} else {
							l.resolve();
						}
						l.then(function() {
							var c = {};
							e.file.indexOf ? c.url = e.file : c = e.file;
							e.bi() && (c.password = e.config.signature + "e0737b87e9be157a2f73ae6ba1352a65");
							PDFJS.getDocument(c).then(function(c) {
								e.pdf = e.Pa = c;
								e.initialized = n;
								e.dimensions = p;
								e.ob = Array(!e.xb ? e.Pa.numPages : e.pa.length);
								e.dimensions = [];
								e.Pa.getDestinations().then(function(c) {
									e.destinations = c;
								});
								e.Pa.getPage(d && d.StartAtPage ? d.StartAtPage : 1).then(function(c) {
									c = c.getViewport(1);
									var d = e.Pa.numPages;
									!e.Fa && e.xb && (d = e.pa.length);
									for (i = 1; i <= d; i++) {
										e.dimensions[i - 1] = [], e.dimensions[i - 1].page = i - 1, e.dimensions[i - 1].width = c.width, e.dimensions[i - 1].height = c.height, e.dimensions[i - 1].Aa = c.width, e.dimensions[i - 1].Oa = c.height;
									}
									e.Wf = n;
									e.Hc();
								});
								e.ti(e.Pa);
							}, function(c) {
								A("Cannot load PDF file (" + c + ")", "onDocumentLoadedError", e.ia, "Cannot load PDF file (" + c + ")");
								jQuery(e).trigger("loadingProgress", {
									ia: e.ia,
									progress: "Error"
								});
							}, x(), function(c) {
								jQuery(e).trigger("loadingProgress", {
									ia: e.ia,
									progress: Math.round(100 * (c.loaded / c.total)) + "%"
								});
							});
						});
					}
				});
				e.JSONPageDataFormat = {
					wd: "width",
					vd: "height",
					jd: "text",
					fc: "d",
					Ge: "f",
					uc: "l",
					wc: "t",
					xc: "w",
					tc: "h"
				};
			},
			sg: function(c, d, e) {
				var f = this,
					k = {};
				k.url = f.Vf(c);
				f.bi() && (k.password = f.config.signature + "e0737b87e9be157a2f73ae6ba1352a65");
				f.xn = PDFJS.getDocument(k).then(function(k) {
					f.Pa[c - 1] = k;
					f.initialized = n;
					f.dimensions || (f.dimensions = []);
					f.Pa[c - 1].getDestinations().then(function(c) {
						f.destinations = c;
					});
					f.Pa[c - 1].getPage(1).then(function(k) {
						f.ob[c - 1] = k;
						var l = k.getViewport(1);
						for (i = 1; i <= f.Pa[c - 1].numPages; i++) {
							var g = f.dimensions && f.dimensions[i - 1] ? f.dimensions[i - 1] : [];
							f.dimensions[i - 1] = [];
							f.dimensions[i - 1].loaded = n;
							f.dimensions[i - 1].page = i - 1;
							f.dimensions[i - 1].width = l.width;
							g.width && (f.dimensions[i - 1].width != g.width && e) && (e.dimensions.Aa = l.width, e.dimensions.Oa = l.height, e.Qa());
							if (1 < c && f.xb && (c < f.Pa[c - 1].numPages || 0 != f.Pa[c - 1].numPages % 2)) {
								f.dimensions[i - 1].width /= 2;
							}
							f.dimensions[i - 1].height = l.height;
							f.dimensions[i - 1].Aa = l.width;
							f.dimensions[i - 1].Oa = l.height;
							if (1 < c && f.xb && (c < f.Pa[c - 1].numPages || 0 != f.Pa[c - 1].numPages % 2)) {
								f.dimensions[i - 1].Aa /= 2;
							}
							f.Ga[i - 1] != p && f.Ga.length > i && (f.dimensions[i - 1].hc = f.Ga[i].hc, f.dimensions[i - 1].gc = f.Ga[i].gc, f.dimensions[i - 1].mb = f.Ga[i].mb, f.dimensions[i - 1].yc = f.Ga[i].yc);
							f.sf[c - 1 + " " + k.ref.gen + " R"] = c - 1;
						}
						f.Wf = n;
						f.od = -1;
						d && d();
					});
					f.od = -1;
				}, function(c) {
					A("Cannot load PDF file (" + c + ")", "onDocumentLoadedError", f.ia);
					jQuery(f).trigger("loadingProgress", {
						ia: f.ia,
						progress: "Error"
					});
					f.od = -1;
				}, x(), function(c) {
					jQuery(f).trigger("loadingProgress", {
						ia: f.ia,
						progress: Math.round(100 * (c.loaded / c.total)) + "%"
					});
				});
			},
			Id: function(c) {
				return this.Kc.replace("{page}", c);
			},
			dg: function(c) {
				var d = 1;
				if (1 < c) {
					for (var e = 0; e < c; e++) {
						(0 != e % 2 || 0 == e % 2 && 0 == c % 2 && e == c - 1) && d++;
					}
					return d;
				}
				return 1;
			},
			bi: function() {
				return this.config.signature != p && 0 < this.config.signature.length;
			},
			Vf: function(c) {
				this.xb && 1 < c && (c = this.dg(c));
				if (0 <= this.file.indexOf("{page}")) {
					return this.file.replace("{page}", c);
				}
				if (0 <= this.file.indexOf("[*,")) {
					var d = this.file.substr(this.file.indexOf("[*,"), this.file.indexOf("]") - this.file.indexOf("[*,") + 1);
					return this.file.replace(d, ia(c, parseInt(d.substr(d.indexOf(",") + 1, d.indexOf("]") - 2))));
				}
			},
			gf: function(c) {
				return c + (10 - c % 10);
			},
			Lc: function(c, d, e, f, k) {
				var l = this;
				l.Nc = l.gf(c);
				jQuery.ajax({
					url: l.Id(l.Nc),
					dataType: l.config.JSONDataType,
					async: d,
					success: function(c) {
						c.e && (c = CryptoJS.md.decrypt(c.e, CryptoJS.Ib.ld.parse(eb.Sg ? W() : eb.Sc.innerHTML)), c = jQuery.parseJSON(c.toString(CryptoJS.Ib.ie)), l.Cd = n);
						if (0 < c.length) {
							for (var d = 0; d < c.length; d++) {
								var f = parseInt(c[d].number) - 1;
								l.pa[f] = c[d];
								l.pa[f].loaded = n;
								l.yk(f);
								l.Lg(f, k);
							}
							l.Ra.be(l.pa);
							jQuery(l).trigger("onTextDataUpdated");
							e != p && e();
						}
						l.Nc = p;
					},
					error: function(c) {
						A("Error loading JSON file (" + c.statusText + "). Please check your configuration.", "onDocumentLoadedError", l.ia);
						l.Nc = p;
					}
				});
			},
			Lg: function(c) {
				this.Ga || (this.Ga = []);
				this.Ga[c] || (this.Ga[c] = []);
				this.Ga[c].hc = this.pa[c][this.JSONPageDataFormat.wd];
				this.Ga[c].gc = this.pa[c][this.JSONPageDataFormat.vd];
				this.Ga[c].mb = this.Ga[c].hc;
				this.Ga[c].yc = this.Ga[c].gc;
				c = this.Ga[c];
				for (var d = 0; d < this.getNumPages(); d++) {
					this.Ga[d] == p && (this.Ga[d] = [], this.Ga[d].hc = c.hc, this.Ga[d].gc = c.gc, this.Ga[d].mb = c.mb, this.Ga[d].yc = c.yc);
				}
			},
			getDimensions: function() {
				var c = this;
				if (c.dimensions == p || c.Wf || c.dimensions != p && 0 == c.dimensions.length) {
					c.dimensions == p && (c.dimensions = []);
					var d = c.Pa.numPages;
					!c.Fa && c.xb && (d = c.pa.length);
					if (c.Fa) {
						for (var e = 0; e < c.getNumPages(); e++) {
							c.dimensions[e] != p || c.dimensions[e] != p && !c.dimensions[e].loaded ? (c.Hb == p && (c.Hb = c.dimensions[e]), !c.dimensions[e].mb && c.Ga[e] != p && (c.dimensions[e].mb = c.Ga[e].mb, c.dimensions[e].yc = c.Ga[e].yc)) : c.Hb != p && (c.dimensions[e] = [], c.dimensions[e].page = e, c.dimensions[e].loaded = v, c.dimensions[e].width = c.Hb.width, c.dimensions[e].height = c.Hb.height, c.dimensions[e].Aa = c.Hb.Aa, c.dimensions[e].Oa = c.Hb.Oa, c.Ga[e - 1] != p && (c.dimensions[e - 1].hc = c.Ga[e].hc, c.dimensions[e - 1].gc = c.Ga[e].gc, c.dimensions[e - 1].mb = c.Ga[e].mb, c.dimensions[e - 1].yc = c.Ga[e].yc), e == c.getNumPages() - 1 && (c.dimensions[e].hc = c.Ga[e].hc, c.dimensions[e].gc = c.Ga[e].gc, c.dimensions[e].mb = c.Ga[e].mb, c.dimensions[e].yc = c.Ga[e].yc), c.sf[e + " 0 R"] = e);
						}
					} else {
						for (e = 1; e <= d; e++) {
							var f = e;
							c.xb && (f = c.dg(e));
							c.Pa.getPage(f).then(function(d) {
								var e = d.getViewport(1);
								c.dimensions[d.pageIndex] = [];
								c.dimensions[d.pageIndex].page = d.pageIndex;
								c.dimensions[d.pageIndex].width = e.width;
								c.dimensions[d.pageIndex].height = e.height;
								c.dimensions[d.pageIndex].Aa = e.width;
								c.dimensions[d.pageIndex].Oa = e.height;
								e = d.ref;
								c.sf[e.num + " " + e.gen + " R"] = d.pageIndex;
							});
						}
					}
					c.Wf = v;
				}
				return c.dimensions;
			},
			yk: function(c) {
				if (this.dimensions[c]) {
					this.dimensions[c].page = c;
					this.dimensions[c].loaded = n;
					this.gb[c] = [];
					this.gb[c] = "";
					for (var d = 0, e; e = this.pa[c][this.JSONPageDataFormat.jd][d++];) {
						this.Ma ? !isNaN(e[0].toString()) && 0 <= Number(e[0].toString()) && (!isNaN(e[1].toString()) && 0 <= Number(e[1].toString()) && !isNaN(e[2].toString()) && 0 <= Number(e[2].toString()) && !isNaN(e[3].toString()) && 0 <= Number(e[3].toString())) && (this.gb[c] += e[5]) : !isNaN(e[this.JSONPageDataFormat.uc].toString()) && 0 <= Number(e[this.JSONPageDataFormat.uc].toString()) && (!isNaN(e[this.JSONPageDataFormat.wc].toString()) && 0 <= Number(e[this.JSONPageDataFormat.wc].toString()) && !isNaN(e[this.JSONPageDataFormat.xc].toString()) && 0 < Number(e[this.JSONPageDataFormat.xc].toString()) && !isNaN(e[this.JSONPageDataFormat.tc].toString()) && 0 < Number(e[this.JSONPageDataFormat.tc].toString())) && (this.gb[c] += e[this.JSONPageDataFormat.fc]);
					}
					this.gb[c] = this.gb[c].toLowerCase();
				}
			},
			getNumPages: function() {
				return this.Fa ? this.pa.length : this.xb ? this.pa.length : this.Pa ? this.Pa.numPages : this.pa.length;
			},
			getPage: function(c) {
				this.Pa.getPage(c).then(function(c) {
					return c;
				});
				return p;
			},
			Wb: function(c) {
				var d = this;
				c.ba == H || c.ba == D ? (0 == c.pageNumber && jQuery(c.wa).css("background-image", "url('" + d.xa(c.pages.ja + 1) + "')"), 1 == c.pageNumber && jQuery(c.wa).css("background-image", "url('" + d.xa(c.pages.ja + 2) + "')")) : c.ba == M ? jQuery(c.wa).css("background-image", "url('" + d.xa(c.pageNumber + 1, 200) + "')") : c.ba == U ? jQuery(c.wa).css("background-image", "url('" + d.xa(d.Ia(c) + 1) + "')") : jQuery(c.wa).css("background-image", "url('" + d.xa(c.pageNumber + 1) + "')");
				c.ua = new Image;
				jQuery(c.ua).bind("load", function() {
					var e = Math.round(100 * (c.ua.width / c.ua.height)),
						f = Math.round(100 * (c.dimensions.width / c.dimensions.height));
					if (c.ba == U) {
						var e = d.Ga[c.pages.ja],
							k = Math.round(100 * (e.hc / e.gc)),
							f = Math.round(100 * (c.dimensions.Aa / c.dimensions.Oa));
						k != f && (c.dimensions.Aa = e.hc, c.dimensions.Oa = e.gc, c.Qa());
					} else {
						e != f && (c.dimensions.Aa = c.ua.width, c.dimensions.Oa = c.ua.height, c.Qa());
					}
				});
				jQuery(c.ua).attr("src", d.xa(c.pageNumber + 1));
			},
			ci: function(c) {
				c.ba == H || c.ba == D ? (0 == c.pageNumber && jQuery(c.wa).css("background-image", "url(" + this.qa + ")"), 1 == c.pageNumber && jQuery(c.wa).css("background-image", "url(" + this.qa + ")")) : jQuery(c.wa).css("background-image", "url(" + this.qa + ")");
			},
			Ic: function(c) {
				this.yb = c.yb = this.Fa && this.config.MixedMode;
				if (c.ba == C || c.ba == U) {
					jQuery(c.la).append("<canvas id='" + this.Ba(1, c) + "' style='position:relative;left:0px;top:0px;width:100%;height:100%;display:none;background-repeat:no-repeat;background-size:" + (!eb.browser.mozilla && !eb.browser.safari || !eb.platform.mac ? "cover" : "100% 100%") + ";background-color:#ffffff;' class='" + (!this.config.DisableShadows ? "flexpaper_border" : "") + " flexpaper_interactivearea flexpaper_grab flexpaper_hidden flexpaper_rescale'></canvas><canvas id='" + this.Ba(2, c) + "' style='position:relative;left:0px;top:0px;width:100%;height:100%;display:block;background-repeat:no-repeat;background-size:" + (!eb.browser.mozilla && !eb.browser.safari || !eb.platform.mac ? "cover" : "100% 100%") + ";background-color:#ffffff;' class='" + (!this.config.DisableShadows ? "flexpaper_border" : "") + " flexpaper_interactivearea flexpaper_grab flexpaper_hidden flexpaper_rescale'></canvas>");
				}
				c.ba == this.Na(c) && this.kb(c).Ic(this, c);
				c.ba == M && jQuery(c.la).append("<canvas id='" + this.Ba(1, c) + "' style='" + c.getDimensions() + ";background-repeat:no-repeat;background-size:" + (!eb.browser.mozilla && !eb.browser.safari || !eb.platform.mac ? "cover" : "100% 100%") + ";background-color:#ffffff;' class='flexpaper_interactivearea flexpaper_grab flexpaper_hidden' ></canvas>");
				if (c.ba == H || c.ba == D) {
					0 == c.pageNumber && (jQuery(c.la + "_1").append("<img id='" + c.Mb + "_1' src='" + this.Mc + "' style='position:absolute;left:" + (c.Sa() - 30) + "px;top:" + c.Xa() / 2 + "px;' />"), jQuery(c.la + "_1").append("<canvas id='" + this.Ba(1, c) + "' style='position:absolute;width:100%;height:100%;background-repeat:no-repeat;background-size:" + (!eb.browser.mozilla && !eb.browser.safari || !eb.platform.mac ? "cover" : "100% 100%") + ";background-color:#ffffff;' class='flexpaper_interactivearea flexpaper_grab flexpaper_hidden'/></canvas>"), jQuery(c.la + "_1").append("<div id='" + c.na + "_1_textoverlay' style='position:relative;left:0px;top:0px;width:100%;height:100%;z-index:10'></div>")), 1 == c.pageNumber && (jQuery(c.la + "_2").append("<img id='" + c.Mb + "_2' src='" + this.Mc + "' style='position:absolute;left:" + (c.Sa() / 2 - 10) + "px;top:" + c.Xa() / 2 + "px;' />"), jQuery(c.la + "_2").append("<canvas id='" + this.Ba(2, c) + "' style='position:absolute;width:100%;height:100%;background-repeat:no-repeat;background-size:" + (!eb.browser.mozilla && !eb.browser.safari || !eb.platform.mac ? "cover" : "100% 100%") + ";background-color:#ffffff;' class='flexpaper_interactivearea flexpaper_grab flexpaper_hidden'/></canvas>"), jQuery(c.la + "_2").append("<div id='" + c.na + "_2_textoverlay' style='position:absolute;left:0px;top:0px;width:100%;height:100%;z-index:10'></div>"));
				}
			},
			Ba: function(c, d) {
				var e = d.pageNumber;
				if ((d.ba == H || d.ba == D) && 0 == d.pageNumber % 2) {
					return this.ia + "_dummyCanvas1";
				}
				if ((d.ba == H || d.ba == D) && 0 != d.pageNumber % 2) {
					return this.ia + "_dummyCanvas2";
				}
				if (1 == c) {
					return this.Ze.replace("[pageNumber]", e);
				}
				if (2 == c) {
					return this.$e.replace("[pageNumber]", e);
				}
			},
			gk: function(c, d) {
				if ((d.ba == H || d.ba == D) && 0 == d.pageNumber % 2) {
					return "#" + this.ia + "_dummyCanvas1";
				}
				if ((d.ba == H || d.ba == D) && 0 != d.pageNumber % 2) {
					return "#" + this.ia + "_dummyCanvas2";
				}
				if (1 == c) {
					return this.Xf.replace("[pageNumber]", d.pageNumber);
				}
				if (2 == c) {
					return this.Yf.replace("[pageNumber]", d.pageNumber);
				}
			},
			Eb: function(c, d, e) {
				var f = this;
				f.cg = n;
				if (c.ba != f.Na(c) || f.kb(c).hl(f, c, d, e)) {
					if ((c.ba == C || c.ba == H || c.ba == D) && c.context == p && !c.Lb) {
						c.rc(), c.Lb = n;
					}
					1 == f.Rk && (1 < c.scale && c.yb) && f.Ea(c, -1);
					if (-1 < f.Ia(c)) {
						window.clearTimeout(c.Pb), c.Pb = setTimeout(function() {
							f.Eb(c, d, e);
						}, 50);
					} else {
						f.Rk = c.scale;
						if (c.ba == H || c.ba == D) {
							if (0 == c.pageNumber) {
								c.ba == D ? f.Ea(c, 0 == c.pages.ja ? c.pages.ja : c.pages.ja - 1) : c.ba == H && f.Ea(c, c.pages.ja), f.xh = c, c.Jb();
							} else {
								if (1 == c.pageNumber) {
									c.ba == D ? f.Ea(c, c.pages.ja) : c.ba == H && f.Ea(c, c.pages.ja + 1), f.xh = c, jQuery(c.la + "_2").removeClass("flexpaper_hidden"), jQuery(c.la + "_2").removeClass("flexpaper_load_on_demand"), c.Jb();
								} else {
									return;
								}
							}
						} else {
							c.ba == U ? f.Ea(c, c.pages.ja) : (f.Ea(c, c.pageNumber), f.xh = c);
						}
						f.Kg(c);
						if ((c.yb || f.Fa) && !c.dimensions.loaded) {
							var k = c.pageNumber + 1;
							c.ba == U && (k = f.Ia(c) + 1);
							f.Lc(k, n, function() {
								c.dimensions.loaded = v;
								f.Rb(c);
							}, n, c);
						}
						if (c.yb && c.scale <= f.pf(c)) {
							-1 < f.Ia(c) && window.clearTimeout(c.Pb), jQuery(c.la).removeClass("flexpaper_load_on_demand"), f.Fa ? (k = new XMLHttpRequest, k.open("GET", f.Vf(c.pageNumber + 1), n), k.overrideMimeType("text/plain; charset=x-user-defined"), k.send(p)) : f.dh && f.ob[f.Ia(c)] == p && (k = f.Ia(c) + 1, f.Pa.getPage(k).then(function(d) {
								f.ob[f.Ia(c)] = d;
							})), c.ba == f.Na(c) ? f.kb(c).Eb(f, c, d, e) : (f.Wb(c), f.qd(c, e));
						} else {
							if (c.yb && c.scale > f.pf(c) && c.ba != f.Na(c) && f.Wb(c), f.ob[f.Ia(c)] == p && !f.Fa && (k = f.Ia(c) + 1, f.xb && (k = f.dg(k)), f.Pa.getPage(k).then(function(k) {
								f.ob[f.Ia(c)] = k;
								window.clearTimeout(c.Pb);
								f.Ea(c, -1);
								f.Eb(c, d, e);
							})), 100 != c.wa.width && 1 == c.scale && c.ba == f.Na(c) && !c.ei) {
								jQuery("#" + f.Ba(1, c)).ce(), jQuery("#" + f.Ba(2, c)).hf(), 1 == c.scale && eb.browser.safari ? (jQuery("#" + f.Ba(1, c)).css("-webkit-backface-visibility", "hidden"), jQuery("#" + f.Ba(2, c)).css("-webkit-backface-visibility", "hidden"), jQuery("#" + c.na + "_textoverlay").css("-webkit-backface-visibility", "hidden")) : eb.browser.safari && (jQuery("#" + f.Ba(1, c)).css("-webkit-backface-visibility", "visible"), jQuery("#" + f.Ba(2, c)).css("-webkit-backface-visibility", "visible"), jQuery("#" + c.na + "_textoverlay").css("-webkit-backface-visibility", "visible")), f.Ea(c, -1), c.za || jQuery("#" + f.ia).trigger("onPageLoaded", c.pageNumber + 1), c.za = n, f.Ka(c, n, e);
							} else {
								if (k = n, f.ob[f.Ia(c)] == p && f.Fa && (c.ba == f.Na(c) && (k = f.kb(c).dl(f, c)), f.Pa[f.Ia(c)] == p && (-1 == f.od && k) && (f.od = f.Ia(c) + 1, f.sg(f.od, function() {
									window.clearTimeout(c.Pb);
									f.Ea(c, -1);
									f.Eb(c, d, e);
								}, c))), !(f.ob[f.Ia(c)] == p && k) && (c.ba == f.Na(c) ? f.kb(c).Eb(f, c, d, e) : (c.wa.width = c.Sa(), c.wa.height = c.Xa()), f.xb && 0 < c.qb.indexOf("cropCanvas") && (c.wa.width *= 2), !(f.ob[f.Ia(c)] == p && k))) {
									var k = v,
										l = c.nc + "_textLayer";
									jQuery("#" + l).remove();
									if (0 == jQuery("#" + l).length && (c.ba == C || c.ba == U || c.ba == H || c.ba == D || c.ba == f.Na(c) && f.kb(c).Pl(f, c))) {
										var k = n,
											g = c.Gc(),
											g = "<div id='" + l + "' class='textLayer flexpaper_pageword_" + f.ia + "' style='margin-left:" + g + "px;'></div>";
										c.ba == C || f.Na(c) ? jQuery(c.va).append(g) : (c.ba == H || c.ba == D) && jQuery(c.va + "_" + (c.pageNumber % 2 + 1)).append(g);
									}
									if (f.cg) {
										var g = c.wa.height / f.getDimensions()[c.pageNumber].height,
											g = g * f.oc,
											m = f.ob[f.Ia(c)].getViewport(g);
										f.xb || (c.wa.width = m.width, c.wa.height = m.height);
										var q = c.Ok = {
											canvasContext: c.context,
											viewport: m,
											pageNumber: c.pageNumber,
											zf: k && !f.Fa ? new ma(document.getElementById(l)) : p,
											continueCallback: function(c) {
												c();
											}
										};
										q.canvasContext.spacedTextList = [];
										window.requestAnim(function() {
											f.ob[f.Ia(c)].render(q).promise.then(function() {
												if (f.ob[f.Ia(c)] != p) {
													if (!f.Fa && !(c.yb && c.scale <= f.pf(c))) {
														for (var d = c.wa.height / f.getDimensions()[c.pageNumber].height, k = q.canvasContext.spacedTextList, l = 0; l < k.length; l++) {
															k[l].Xk != d && (k[l].h /= d, k[l].l /= d, k[l].t /= d, k[l].w /= d, k[l].Xk = d);
														}
														c.ba == U || c.ba == H || c.ba == D ? f.Ra.ji(k, f.Ia(c), f.getNumPages()) : f.Ra.ji(k, c.pageNumber, f.getNumPages());
														f.ki(f.ob[f.Ia(c)], c, m, f.Fa);
														f.qd(c, e);
														f.Ka(c, n, e);
													} else {
														f.Fa || f.ki(f.ob[f.Ia(c)], c, m, f.Fa), (f.Fa || !f.Fa && !f.xb) && f.ob[f.Ia(c)].destroy(), f.qd(c, e);
													}
												} else {
													f.qd(c, e), fa(c.pageNumber + "  is missing its pdf page (" + f.Ia(c) + ")");
												}
											}, function(c) {
												A(c.toString(), "onDocumentLoadedError", f.ia);
											});
										}, 50);
									} else {
										f.Ea(c, -1);
									}
									jQuery(c.la).removeClass("flexpaper_load_on_demand");
								}
							}
						}
					}
				}
			},
			pf: ca(1.1),
			Ia: aa("Kd"),
			Ea: function(c, d) {
				if ((!this.Fa || c.yb && 1 == c.scale) && c) {
					c.Kd = d;
				}
				this.Kd = d;
			},
			Kg: function(c) {
				c.ba == C || c.ba == U ? jQuery(this.gk(1, c)).is(":visible") ? (c.qb = this.Ba(2, c), c.Ud = this.Ba(1, c)) : (c.qb = this.Ba(1, c), c.Ud = this.Ba(2, c)) : c.ba == this.Na(c) ? this.kb(c).Kg(this, c) : (c.qb = this.Ba(1, c), c.Ud = p);
				this.xb && 0 < c.pageNumber && 0 == c.pageNumber % 2 ? (c.wa = document.createElement("canvas"), c.wa.width = c.wa.height = 100, c.wa.id = c.qb + "_cropCanvas", c.qb += "_cropCanvas") : c.wa = document.getElementById(c.qb);
				c.qk != p && (c.qk = document.getElementById(c.Ud));
				c.wa.getContext && (c.context = c.wa.getContext("2d"), c.context.Le = c.context.mozImageSmoothingEnabled = c.context.imageSmoothingEnabled = v);
			},
			Gj: function(c, d, e, f) {
				c = f.convertToViewportRectangle(d.rect);
				c = PDFJS.Util.normalizeRect(c);
				d = e.Gc();
				f = document.createElement("a");
				f.style.position = "absolute";
				f.style.left = Math.floor(c[0]) / this.oc + d + "px";
				f.style.top = Math.floor(c[1]) / this.oc + "px";
				f.style.width = Math.ceil(c[2] - c[0]) / this.oc + "px";
				f.style.height = Math.ceil(c[3] - c[1]) / this.oc + "px";
				f.style["z-index"] = 20;
				f.className = "pdfPageLink_" + e.pageNumber + " flexpaper_interactiveobject_" + this.ia;
				return f;
			},
			ki: function(c, d, e, f) {
				var k = this;
				1 != d.scale && d.ba == k.Na(d) || (jQuery(".pdfPageLink_" + d.pageNumber).remove(), c.getAnnotations().then(function(l) {
					for (var g = 0; g < l.length; g++) {
						var m = l[g];
						switch (m.subtype) {
							case "Link":
								var q = k.Gj("a", m, d, e, c.view);
								q.style.position = "absolute";
								q.href = m.url || "";
								!m.url && !f ? (m = "string" === typeof m.dest ? k.destinations[m.dest][0] : m != p && m.dest != p ? m.dest[0] : p, m = m instanceof Object ? k.sf[m.num + " " + m.gen + " R"] : m + 1, jQuery(q).data("gotoPage", m + 1), jQuery(q).on("click", function() {
									d.aa.gotoPage(parseInt(jQuery(this).data("gotoPage")));
									return v;
								}), jQuery(d.va).append(q)) : q.href != p && ("" != q.href && m.url) && (jQuery(q).on("click", function() {
									jQuery(d.ga).trigger("onExternalLinkClicked", this.href);
								}), jQuery(d.va).append(q));
						}
					}
				}));
			},
			qd: function(c, d) {
				this.Ka(c, n, d);
				jQuery("#" + c.qb).ce();
				this.Kh(c);
				(c.ba == C || c.ba == U) && jQuery(c.Db).remove();
				c.ba == this.Na(c) && this.kb(c).qd(this, c, d);
				if (c.qb && 0 < c.qb.indexOf("cropCanvas")) {
					var e = c.wa;
					c.qb = c.qb.substr(0, c.qb.length - 11);
					c.wa = jQuery("#" + c.qb).get(0);
					c.wa.width = e.width / 2;
					c.wa.height = e.height;
					c.wa.getContext("2d").drawImage(e, e.width / 2, 0, c.wa.width, c.wa.height, 0, 0, e.width / 2, e.height);
					jQuery(c.wa).ce();
				}
				if (c.ba == H || c.ba == D) {
					0 == c.pageNumber && (jQuery(c.Da).removeClass("flexpaper_hidden"), jQuery(c.la + "_1").removeClass("flexpaper_hidden")), 1 == c.pageNumber && jQuery(c.Da).removeClass("flexpaper_hidden");
				}
				c.za || jQuery("#" + this.ia).trigger("onPageLoaded", c.pageNumber + 1);
				c.za = n;
				c.ei = v;
				c.Im = v;
				this.Rd || (this.Rd = n, window.$FlexPaper(this.ia).tf());
				d != p && d();
				this.Fa && (this.Pa[c.pageNumber] = p, this.ob[c.pageNumber] = p);
			},
			Kh: function(c) {
				c.ba != H && (c.ba != D && (c.ba != this.Na(c) || eb.browser.safari)) && jQuery("#" + c.Ud).hf();
				this.Ea(c, -1);
			},
			xa: function(c, d) {
				this.Cd && (c = CryptoJS.md.encrypt(c.toString(), CryptoJS.Ib.ld.parse(eb.Sg ? W() : eb.Sc.innerHTML)).toString());
				if (d) {
					if (this.pageThumbImagePattern != p && 0 < this.pageThumbImagePattern.length) {
						return this.pageThumbImagePattern.replace("{page}", c) + (0 < this.pageThumbImagePattern.indexOf("?") ? "&" : "?") + "resolution=" + d;
					}
				} else {
					return this.pageSVGImagePattern ? this.pageSVGImagePattern.replace("{page}", c) : this.pageImagePattern.replace("{page}", c);
				}
			},
			unload: function(c) {
				jQuery(".flexpaper_pageword_" + this.ia + "_page_" + c.pageNumber + ":not(.flexpaper_selected_searchmatch, .flexpaper_annotation_" + this.ia + ")").remove();
				c.ba != this.Na(c) && this.ci(c);
				c.yb && (jQuery(c.wa).css("background-image", "url(" + this.qa + ")"), c.ua = p);
				c.context != p && (c.wa != p && 100 != c.wa.width) && (this.Fa && (this.Pa[c.pageNumber] = p, this.ob[c.pageNumber] = p), this.context = this.wa = c.Ok = p, c.Cg && c.Cg(), jQuery(".flexpaper_annotation_" + this.ia + "_page_" + c.pageNumber).remove());
			},
			ti: function(c) {
				var d = this;
				d.Pa.getPage(d.qf).then(function(e) {
					e.getTextContent().then(function(e) {
						var k = "";
						if (e) {
							for (var l = 0; l < e.items.length; l++) {
								k += e.items[l].str;
							}
						}
						d.gb[d.qf - 1] = k.toLowerCase();
						d.qf + 1 < d.getNumPages() + 1 && (d.qf++, d.ti(c));
					});
				});
			},
			Rb: function(c, d, e, f) {
				this.Ra.Rb(c, d, e, f);
			},
			Qb: function(c, d, e) {
				this.Ra.Qb(c, d, e);
			},
			Ka: function(c, d, e) {
				var f = this.pa != p && this.pa[c.pageNumber] && this.pa[c.pageNumber].text && 0 < this.pa[c.pageNumber].text.length && this.Fa;
				if (c.za || d || f) {
					c.Il != c.scale && (jQuery(".flexpaper_pageword_" + this.ia + "_page_" + c.pageNumber).remove(), c.Il = c.scale), d = this.ee != p ? this.ee : e, this.ee = p, this.Ra.Ka(c, d);
				} else {
					if (e != p) {
						if (this.ee != p) {
							var k = this.ee;
							this.ee = function() {
								k();
								e();
							};
						} else {
							this.ee = e;
						}
					}
				}
			}
		};
		return g;
	}();

function ma(g) {
	this.an = g;
	this.beginLayout = function() {
		this.$m = [];
		this.bn = [];
		this.spacedTextList = [];
	};
	this.endLayout = x();
	this.km = x();
}
var la = window.TextOverlay = function() {
	function g(c, d, e, f) {
		this.ia = c;
		this.JSONPageDataFormat = e;
		this.pa = [];
		this.ab = p;
		this.La = [];
		this.Ma = this.Ol = d;
		this.pb = f;
		this.state = {};
		this.qa = "data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///yH5BAEAAAAALAAAAAABAAEAAAIBRAA7";
	}
	g.prototype = {
		Bb: function() {
			delete this.ia;
			this.ia = p;
			delete this.pa;
			this.pa = p;
			delete this.JSONPageDataFormat;
			this.JSONPageDataFormat = p;
			delete this.ab;
			this.ab = p;
			delete this.La;
			this.La = p;
			delete this.state;
			this.state = p;
			delete this.qa;
			this.qa = p;
			delete this.pb;
			this.pb = p;
		},
		Wk: function() {
			this.state[this.Ma] || (this.state[this.Ma] = [], this.state[this.Ma].pa = this.pa, this.state[this.Ma].ab = this.ab, this.state[this.Ma].La = this.La, window["wordPageList_" + this.ia] = p);
			this.pa = [];
			this.ab = p;
			this.La = [];
			this.Ma = this.Ol;
		},
		Na: function(c) {
			return c.aa.ha ? c.aa.ha.ra : "";
		},
		kb: function(c) {
			return c.aa.ha.Hl;
		},
		be: function(c) {
			this.pa = c;
			this.ab == p && (this.ab = Array(c.length));
			window["wordPageList_" + this.ia] = this.La;
		},
		ji: function(c, d, e) {
			this.ab == p && (this.ab = Array(e));
			this.pa[d] = [];
			this.pa[d].text = c;
			window["wordPageList_" + this.ia] = this.La;
		},
		Rb: function(c, d, e, f) {
			var k = c.pageNumber,
				l = v,
				g = v;
			if (!this.ab) {
				if (c.yb && (this.Ma = n), this.state[this.Ma]) {
					if (this.pa = this.state[this.Ma].pa, this.ab = this.state[this.Ma].ab, this.La = this.state[this.Ma].La, window["wordPageList_" + this.ia] = this.La, !this.ab) {
						return;
					}
				} else {
					return;
				}
			}
			if (!window.annotations && eb.touchdevice && !f) {
				e && e();
			} else {
				if (!window.annotations && (!c.aa.vc && !f) && (l = n), g = this.rf != p && this.rf[c.pageNumber] != p, c.ba != M) {
					if (c.ba == D && (0 == c.pageNumber && (k = 0 != c.pages.ja ? c.pages.ja - 1 : c.pages.ja), 1 == c.pageNumber && (k = c.pages.ja), 0 == c.pages.getTotalPages() % 2 && k == c.pages.getTotalPages() && (k -= 1), 0 == c.pages.ja % 2 && c.pages.ja > c.pages.getTotalPages())) {
						return;
					}
					c.ba == U && (k = c.pages.ja);
					if (c.ba == H && (0 == c.pageNumber && (k = c.pages.ja), 1 == c.pageNumber && (k = c.pages.ja + 1), 1 == c.pageNumber && k >= c.pages.getTotalPages() && 0 != c.pages.getTotalPages() % 2)) {
						return;
					}
					d = c.Va || !d;
					c.ba == this.Na(c) && (isvisble = this.kb(c).Zb(this, c));
					var m = jQuery(".flexpaper_pageword_" + this.ia + "_page_" + k + ":not(.flexpaper_annotation_" + this.ia + ")").length;
					f = c.dimensions.mb != p ? c.dimensions.mb : c.dimensions.Aa;
					f = this.pb ? c.Sa() / f : 1;
					if (d && 0 == m) {
						var q = m = "",
							y = 0;
						if (this.ab[k] == p || !this.pb) {
							if (this.pa[k] == p) {
								return;
							}
							this.ab[k] = this.pa[k][this.JSONPageDataFormat.jd];
						}
						if (this.ab[k] != p) {
							c.yb && (this.Ma = n);
							var z = new WordPage(this.ia, k),
								t = c.Gc(),
								s = [],
								r = c.$c(),
								w = c.Nd(),
								G = v,
								I = -1,
								B = -1,
								J = 0,
								F = -1,
								S = -1,
								V = v;
							this.La[k] = z;
							c.ba == this.Na(c) && (f = this.kb(c).ck(this, c, f));
							c.cn = f;
							for (var ba = 0; G = this.ab[k][ba++];) {
								var E = !this.Ma ? G[this.JSONPageDataFormat.fc] : G[5],
									T = p,
									N = p;
								if (E == p) {
									fa("word not found in node");
									e && e();
									return;
								}
								0 == E.length && (E = " ");
								if (0 <= E.indexOf("actionGoToR")) {
									T = E.substring(E.indexOf("actionGoToR") + 12, E.indexOf(",", E.indexOf("actionGoToR") + 13)), E = E.substring(E.indexOf(",") + 1);
								} else {
									if (0 <= E.indexOf("actionGoTo")) {
										T = E.substring(E.indexOf("actionGoTo") + 11, E.indexOf(",", E.indexOf("actionGoTo") + 12)), E = E.substring(E.indexOf(",") + 1);
									} else {
										if (0 <= E.indexOf("actionURI")) {
											if (0 <= E.indexOf("actionURI(") && 0 < E.indexOf("):") ? (N = E.substring(E.indexOf("actionURI(") + 10, E.lastIndexOf("):")), E = E.substring(E.indexOf("):") + 2)) : (N = E.substring(E.indexOf("actionURI") + 10), E = E.substring(E.indexOf("actionURI") + 10)), -1 == N.indexOf("http") && -1 == N.indexOf("mailto") && 0 != N.indexOf("/")) {
												N = "http://" + N;
											} else {
												for (var K = ba, O = !this.Ma ? G[this.JSONPageDataFormat.fc] : G[5], P = 1; 2 >= P; P++) {
													for (K = ba; K < this.ab[k].length && 0 <= this.ab[k][K].toString().indexOf("actionURI") && -1 == this.ab[k][K].toString().indexOf("actionURI(");) {
														var V = this.ab[k][K],
															Q = !this.Ma ? V[this.JSONPageDataFormat.fc] : V[5];
														1 == P ? 0 <= Q.indexOf("actionURI") && (11 < Q.length && -1 == Q.indexOf("http://") && -1 == Q.indexOf("https://") && -1 == Q.indexOf("mailto")) && (O += Q.substring(Q.indexOf("actionURI") + 10)) : !this.Ma ? V[this.JSONPageDataFormat.fc] : V[5] = O;
														K++;
													}
													2 == P && -1 == O.indexOf("actionURI(") && (E = O, N = E.substring(E.indexOf("actionURI") + 10), E = E.substring(E.indexOf("actionURI") + 10));
												}
											}
										}
									}
								} if (T || N || !l || g) {
									K = (!this.Ma ? G[this.JSONPageDataFormat.wc] : G[0]) * f + 0;
									O = (!this.Ma ? G[this.JSONPageDataFormat.uc] : G[1]) * f + 0;
									P = (!this.Ma ? G[this.JSONPageDataFormat.xc] : G[2]) * f;
									Q = (!this.Ma ? G[this.JSONPageDataFormat.tc] : G[3]) * f;
									z.cl(y, E);
									G = -1 != I && I != K;
									V = ba == this.ab[k].length;
									0 > O && (O = 0);
									0 > K && (K = 0);
									O + P > r && (P = r - O);
									K + Q > w && (Q = w - K);
									s[y] = {};
									s[y].left = O;
									s[y].right = O + P;
									s[y].top = K;
									s[y].bottom = K + Q;
									s[y].el = "#" + this.ia + "page_" + k + "_word_" + y;
									s[y].i = y;
									s[y].Yh = T;
									s[y].Ei = N;
									m += "<span id='" + this.ia + "page_" + k + "_word_" + y + "' class='flexpaper_pageword flexpaper_pageword_" + this.ia + "_page_" + k + " flexpaper_pageword_" + this.ia + "' style='left:" + O + "px;top:" + K + "px;width:" + P + "px;height:" + Q + "px;margin-left:" + t + "px;" + (s[y].Yh || s[y].Ei ? "cursor:hand;" : "") + ";" + (eb.browser.msie ? "background-image:url(" + this.qa + ");color:transparent;" : "") + "'></span>";
									if (T != p || N != p) {
										var R = document.createElement("a");
										R.style.position = "absolute";
										R.style.left = Math.floor(O) + t + "px";
										R.style.top = Math.floor(K) + "px";
										R.style.width = Math.ceil(P) + "px";
										R.style.height = Math.ceil(Q) + "px";
										R.style["margin-left"] = t;
										R.href = N != p ? N : "";
										jQuery(R).css("z-index", "99");
										R.className = "pdfPageLink_" + c.pageNumber + " flexpaper_interactiveobject_" + this.ia + " flexpaper_pageword_" + this.ia + "_page_" + k + " gotoPage_" + T + " flexpaper_pageword_" + this.ia;
										T != p && (jQuery(R).data("gotoPage", T), jQuery(R).on("click", function() {
											c.aa.gotoPage(parseInt(jQuery(this).data("gotoPage")));
											return v;
										}));
										if (N != p) {
											jQuery(R).on("click touchstart", function(d) {
												jQuery(c.ga).trigger("onExternalLinkClicked", this.href);
												d.stopImmediatePropagation();
												d.preventDefault();
												return v;
											});
										}
										c.ba == H || c.ba == D ? (0 == c.pageNumber && jQuery(c.la + "_1_textoverlay").append(R), 1 == c.pageNumber && jQuery(c.la + "_2_textoverlay").append(R)) : jQuery(c.va).append(R);
									}
									eb.platform.touchdevice && c.ba == C && (G || V ? (V && (J += P, q = q + "<div style='float:left;width:" + P + "px'>" + (" " == E ? "&nbsp;" : E) + "</div>"), q = "<div id='" + this.ia + "page_" + k + "_word_" + y + "_wordspan' class='flexpaper_pageword flexpaper_pageword_" + this.ia + "_page_" + k + " flexpaper_pageword_" + this.ia + "' style='color:transparent;left:" + F + "px;top:" + I + "px;width:" + J + "px;height:" + B + "px;margin-left:" + S + "px;font-size:" + B + "px" + (s[y].Yh || s[y].Ei ? "cursor:hand;" : "") + "'>" + q + "</div>", jQuery(c.yg).append(q), I = K, B = Q, J = P, F = O, S = t, q = "<div style='background-colorfloat:left;width:" + P + "px'>" + (" " == E ? "&nbsp;" : E) + "</div>") : (-1 == F && (F = O), -1 == S && (S = t), -1 == I && (I = K), -1 == B && (B = Q), q = q + "<div style='float:left;width:" + P + "px'>" + (" " == E ? "&nbsp;" : E) + "</div>", J += P, B = Q));
								}
								y++;
							}
							z.Zk(s);
							c.ba == C && jQuery(c.va).append(m);
							c.ba == U && jQuery(c.va).append(m);
							c.ba == this.Na(c) && this.kb(c).wj(this, c, m);
							if (c.ba == H || c.ba == D) {
								0 == c.pageNumber && jQuery(c.la + "_1_textoverlay").append(m), 1 == c.pageNumber && jQuery(c.la + "_2_textoverlay").append(m);
							}
							d && jQuery(c).trigger("onAddedTextOverlay", c.pageNumber);
							if (g) {
								for (k = 0; k < this.rf[c.pageNumber].length; k++) {
									this.rj(c, this.rf[c.pageNumber][k].Zm, this.rf[c.pageNumber][k].dn);
								}
							}
						}
					}
					e != p && e();
				}
			}
		},
		Qb: function(c, d, e) {
			var f = this;
			window.annotations || jQuery(c).unbind("onAddedTextOverlay");
			var k = c.ba == H || c.ba == D ? c.pages.ja + c.pageNumber : c.pageNumber;
			c.ba == D && (0 < c.pages.ja && 1 == c.pageNumber) && (k -= 2);
			c.ba == U && (k = c.pages.ja);
			if ((c.Va || !e) && window.Ua - 1 == k) {
				if (jQuery(".flexpaper_selected").removeClass("flexpaper_selected"), jQuery(".flexpaper_selected_searchmatch").removeClass("flexpaper_selected_searchmatch"), jQuery(".flexpaper_selected_default").removeClass("flexpaper_selected_default"), jQuery(".flexpaper_tmpselection").remove(), !f.La[k] || f.La[k] != p && 0 == f.La[k].fe.length) {
					jQuery(c).bind("onAddedTextOverlay", function() {
						f.Qb(c, d, e);
					}), f.Rb(c, e, p, n);
				} else {
					for (var l = f.La[k].fe, g = "", m = 0, q = 0, y = -1, z = -1, t = d.split(" "), s = 0; s < l.length; s++) {
						var r = (l[s] + "").toLowerCase();
						if (jQuery.trim(r) == d || jQuery.trim(g + r) == d) {
							r = jQuery.trim(r);
						}
						if (0 == d.indexOf(g + r) && (g + r).length <= d.length && " " != g + r) {
							if (g += r, -1 == y && (y = m, z = m + 1), d.length == r.length && (y = m), g.length == d.length) {
								if (q++, window.ed == q) {
									if (c.ba == C || c.ba == U) {
										eb.browser.jb.rb ? jQuery("#pagesContainer_" + f.ia).scrollTo(jQuery(f.La[k].Za[y].el), 0, {
											axis: "xy",
											offset: -30
										}) : jQuery("#pagesContainer_" + f.ia).data("jsp").scrollToElement(jQuery(f.La[k].Za[y].el), v);
									}
									for (var w = y; w < m + 1; w++) {
										c.ba == f.Na(c) ? (r = jQuery(f.La[k].Za[w].el).clone(), f.kb(c).jh(f, c, r, d)) : (jQuery(f.La[k].Za[w].el).addClass("flexpaper_selected"), jQuery(f.La[k].Za[w].el).addClass("flexpaper_selected_default"), jQuery(f.La[k].Za[w].el).addClass("flexpaper_selected_searchmatch"));
									}
								} else {
									g = "", y = -1;
								}
							}
						} else {
							if (0 <= (g + r).indexOf(t[0])) {
								-1 == y && (y = m, z = m + 1);
								g += r;
								if (1 < t.length) {
									for (r = 0; r < t.length - 1; r++) {
										0 < t[r].length && l.length > m + 1 + r && 0 <= (g + l[m + 1 + r]).toLowerCase().indexOf(t[r]) ? (g += l[m + 1 + r].toLowerCase(), z = m + 1 + r + 1) : (g = "", z = y = -1);
									}
								} - 1 == g.indexOf(d) ? (g = "", z = y = -1) : q++;
								if (window.ed == q && 0 < g.length) {
									for (var w = jQuery(f.La[k].Za[y].el), G = parseFloat(w.css("left").substring(0, w.css("left").length - 2)) - (c.ba == f.Na(c) ? c.Gc() : 0), r = w.clone(), I = 0, B = 0, J = 0; y < z; y++) {
										I += parseFloat(jQuery(f.La[k].Za[y].el).css("width").substring(0, w.css("width").length - 2));
									}
									B = 1 - (g.length - d.length) / g.length;
									J = g.indexOf(d) / g.length;
									r.addClass("flexpaper_tmpselection");
									r.attr("id", r.attr("id") + "tmp");
									r.addClass("flexpaper_selected");
									r.addClass("flexpaper_selected_searchmatch");
									r.addClass("flexpaper_selected_default");
									r.css("width", I * B + "px");
									r.css("left", G + I * J + "px");
									if (c.ba == C || c.ba == U) {
										jQuery(c.va).append(r), eb.browser.jb.rb ? jQuery("#pagesContainer_" + f.ia).scrollTo(r, 0, {
											axis: "xy",
											offset: -30
										}) : jQuery("#pagesContainer_" + f.ia).data("jsp").scrollToElement(r, v);
									}
									c.ba == f.Na(c) && f.kb(c).jh(f, c, r, d);
									c.ba == U && jQuery("#dummyPage_0_" + f.ia + "_textoverlay").append(r);
									c.ba == D && (0 == k ? jQuery("#dummyPage_0_" + f.ia + "_1_textoverlay").append(r) : jQuery("#dummyPage_" + (k - 1) % 2 + "_" + f.ia + "_" + ((k - 1) % 2 + 1) + "_textoverlay").append(r));
									c.ba == H && jQuery("#dummyPage_" + k % 2 + "_" + f.ia + "_" + (k % 2 + 1) + "_textoverlay").append(r);
								} else {
									g = "";
								}
								z = y = -1;
							} else {
								0 < g.length && (g = "", y = -1);
							}
						}
						m++;
					}
				}
			}
		},
		rj: function(c, d, e) {
			jQuery(c).unbind("onAddedTextOverlay");
			var f = c.ba == H || c.ba == D ? c.pages.ja + c.pageNumber : c.pageNumber;
			c.ba == D && (0 < c.pages.ja && 1 == c.pageNumber) && (f -= 2);
			c.ba == U && (f = c.pages.ja);
			for (var k = this.La[f].fe, l = -1, g = -1, m = 0, q = 0; q < k.length; q++) {
				var y = k[q] + "";
				m >= d && -1 == l && (l = q);
				if (m + y.length >= d + e && -1 == g && (g = q, -1 != l)) {
					break;
				}
				m += y.length;
			}
			for (d = l; d < g + 1; d++) {
				c.ba == this.Na(c) ? jQuery(this.La[f].Za[d].el).clone() : (jQuery(this.La[f].Za[d].el).addClass("flexpaper_selected"), jQuery(this.La[f].Za[d].el).addClass("flexpaper_selected_yellow"), jQuery(this.La[f].Za[d].el).addClass("flexpaper_selected_searchmatch"));
			}
		},
		Ka: function(c, d) {
			this.Rb(c, d == p, d);
		}
	};
	return g;
}();
window.WordPage = function(g, c) {
	this.ia = g;
	this.pageNumber = c;
	this.fe = [];
	this.Za = p;
	this.Vl = "";
	this.Cm = aa("fe");
	this.Am = aa("pageNumber");
	this.cl = function(c, e) {
		this.fe[c] = e;
	};
	this.Xm = function(c) {
		this.Vl = c;
	};
	this.Zk = function(c) {
		this.Za = c;
	};
	this.Bm = aa("Za");
	this.match = function(c, e) {
		var f, k = p;
		f = "#page_" + this.pageNumber + "_" + this.ia;
		0 == jQuery(f).length && (f = "#dummyPage_" + this.pageNumber + "_" + this.ia);
		f = jQuery(f).offset();
		window.$FlexPaper(this.ia).ba == U && (f = "#dummyPage_0_" + this.ia, f = jQuery(f).offset());
		if (window.$FlexPaper(this.ia).ba == H || window.$FlexPaper(this.ia).ba == D) {
			f = 0 == this.pageNumber || window.$FlexPaper(this.ia).ba == H ? jQuery("#dummyPage_" + this.pageNumber % 2 + "_" + this.ia + "_" + (this.pageNumber % 2 + 1) + "_textoverlay").offset() : jQuery("#dummyPage_" + (this.pageNumber - 1) % 2 + "_" + this.ia + "_" + ((this.pageNumber - 1) % 2 + 1) + "_textoverlay").offset();
		}
		c.top -= f.top;
		c.left -= f.left;
		for (f = 0; f < this.Za.length; f++) {
			if (this.pk(c, this.Za[f], e) && (k == p || k != p && k.top < this.Za[f].top || k != p && k.top <= this.Za[f].top && k != p && k.left < this.Za[f].left)) {
				k = this.Za[f], k.pageNumber = this.pageNumber;
			}
		}
		return k;
	};
	this.pk = function(c, e, f) {
		return !e ? v : f ? c.left + 3 >= e.left && c.left - 3 <= e.right && c.top + 3 >= e.top && c.top - 3 <= e.bottom : c.left + 3 >= e.left && c.top + 3 >= e.top;
	};
	this.Jd = function(c, e) {
		var f = window.a,
			k = window.b,
			l = new na,
			g, m, q = 0,
			y = -1;
		if (p == f) {
			return l;
		}
		if (f && k) {
			var z = [],
				t;
			f.top > k.top ? (g = k, m = f) : (g = f, m = k);
			for (g = g.i; g <= m.i; g++) {
				if (this.Za[g]) {
					var s = jQuery(this.Za[g].el);
					0 != s.length && (t = parseInt(s.attr("id").substring(s.attr("id").indexOf("word_") + 5)), y = parseInt(s.attr("id").substring(s.attr("id").indexOf("page_") + 5, s.attr("id").indexOf("word_") - 1)) + 1, 0 <= t && z.push(this.fe[t]), q++, c && (s.addClass("flexpaper_selected"), s.addClass(e), "flexpaper_selected_strikeout" == e && !s.data("adjusted") && (t = s.height(), s.css("margin-top", t / 2 - t / 3 / 1.5), s.height(t / 2.3), s.data("adjusted", n))));
				}
			}
			eb.platform.touchdevice || jQuery(".flexpaper_selector").val(z.join("")).select();
		} else {
			eb.platform.touchdevice || jQuery("#selector").val("");
		}
		l.Fm = q;
		l.un = f.left;
		l.vn = f.right;
		l.wn = f.top;
		l.tn = f.bottom;
		l.qn = f.left;
		l.rn = f.right;
		l.sn = f.top;
		l.pn = f.bottom;
		l.Uj = z != p && 0 < z.length ? z[0] : p;
		l.Km = z != p && 0 < z.length ? z[z.length - 1] : l.Uj;
		l.Vj = f != p ? f.i : -1;
		l.Lm = k != p ? k.i : l.Vj;
		l.text = z != p ? z.join("") : "";
		l.page = y;
		l.nn = this;
		return l;
	};
};

function na() {}

function oa(g) {
	var c = hoverPage;
	if (g = window["wordPageList_" + g]) {
		return g.length >= c ? g[c] : p;
	}
}
var Z = function() {
		function g(c, d, e, f) {
			this.aa = d;
			this.ga = c;
			this.pages = {};
			this.selectors = {};
			this.container = "pagesContainer_" + e;
			this.da = "#" + this.container;
			this.ja = f == p ? 0 : f - 1;
			this.hd = f;
			this.Oc = this.$d = p;
			this.ac = this.$b = -1;
			this.cd = this.mc = 0;
			this.initialized = v;
			this.ia = this.aa.ia;
			this.document = this.aa.document;
		}
		g.prototype = {
			ea: function(c) {
				if (0 < c.indexOf("undefined")) {
					return jQuery(p);
				}
				this.selectors || (this.selectors = {});
				this.selectors[c] || (this.selectors[c] = jQuery(c));
				return this.selectors[c];
			},
			xg: function() {
				this.bg != p && (window.clearTimeout(this.bg), this.bg = p);
				this.aa.ha && this.aa.ba == this.aa.ha.ra && this.aa.ha.cb.xg(this);
			},
			Ab: function() {
				return this.aa.ha && this.aa.ba == this.aa.ha.ra && this.aa.ha.cb.Ab(this) || this.aa.ba == U;
			},
			Sk: function() {
				return !(this.aa.ha && this.aa.ha.cb.Ab(this));
			},
			Qa: function(c, d, e) {
				var f = this.aa.scale;
				this.aa.scale = c;
				if (this.aa.ba == H || this.aa.ba == D) {
					var k = 100 * c + "%";
					eb.platform.touchdevice || this.ea(this.da).css({
						width: k,
						"margin-left": this.Od()
					});
				}
				this.pages[0] && (this.pages[0].scale = c);
				for (k = 0; k < this.document.numPages; k++) {
					this.Wa(k) && (this.pages[k].scale = c, this.pages[k].Qa());
				}
				this.aa.ha && this.aa.ba == this.aa.ha.ra && this.aa.ha.cb.Qa(this, f, c, d, e);
			},
			Bb: function() {
				for (var c = 0; c < this.document.numPages; c++) {
					this.pages[c].Bb(), delete this.pages[c];
				}
				this.selectors = this.pages = this.ga = this.aa = p;
			},
			resize: function(c, d, e) {
				if (this.aa.ba == C || this.aa.ba == U) {
					c -= eb.browser.msie ? 0 : 2;
				}
				this.aa.ba == M && (d -= 10);
				this.ea(this.da).css({
					width: c,
					height: d
				});
				this.aa.ba == H && (this.aa.Vg = this.ga.height() - (!eb.platform.touchdevice ? 27 : 0), this.aa.Ie = c / 2 - 2, this.ea(this.da).height(this.aa.Vg), this.ea("#" + this.container + "_2").css("left", this.ea("#" + this.container).width() / 2), eb.platform.touchdevice || (this.ea(this.da + "_1").width(this.aa.Ie), this.ea(this.da + "_2").width(this.aa.Ie)));
				if (this.aa.ha && this.aa.ba == this.aa.ha.ra) {
					this.aa.ha.cb.resize(this, c, d, e);
				} else {
					this.pc();
					for (c = 0; c < this.document.numPages; c++) {
						this.Wa(c) && this.pages[c].Qa();
					}
				}
				this.Wg = p;
				this.jScrollPane != p && (this.jScrollPane.data("jsp").reinitialise(this.Tb), this.jScrollPane.data("jsp").scrollTo(this.$b, this.ac, v));
			},
			ad: function(c) {
				var d = this;
				if (!d.ma) {
					var e = v;
					"function" === typeof d.mg && d.Dm();
					jQuery(".flexpaper_pageword").each(function() {
						jQuery(this).hasClass("flexpaper_selected_default") && (e = n);
					});
					d.touchwipe != p && (d.touchwipe.config.preventDefaultEvents = v);
					d.Ab() || (jQuery(".flexpaper_pageword_" + d.ia).remove(), setTimeout(function() {
						(d.aa.ba == H || d.aa.ba == D) && d.cc();
						d.Ka();
						e && d.getPage(window.Ua - 1).Qb(window.Pc, v);
					}, 500));
					d.aa.ha && d.aa.ba == d.aa.ha.ra ? d.aa.ha.cb.ad(d, c) : d.Qa(1);
					d.jScrollPane != p ? (d.jScrollPane.data("jsp").reinitialise(d.Tb), d.jScrollPane.data("jsp").scrollTo(d.$b, d.ac, v)) : (d.aa.ba == H || d.aa.ba == D) && d.ea(d.da).parent().scrollTo({
						left: d.$b + "px",
						top: d.ac + "px"
					}, 0, {
						axis: "xy"
					});
				}
			},
			kc: function(c) {
				var d = this;
				if (!d.ma) {
					var e = v;
					d.touchwipe != p && (d.touchwipe.config.preventDefaultEvents = n);
					"function" === typeof d.mg && d.Em();
					jQuery(".flexpaper_pageword").each(function() {
						jQuery(this).hasClass("flexpaper_selected_default") && (e = n);
					});
					d.Ab() || jQuery(".flexpaper_pageword_" + d.ia).remove();
					d.aa.ha && d.aa.ba == d.aa.ha.ra ? d.aa.ha.cb.kc(d, c) : d.Qa(window.FitHeightScale);
					setTimeout(function() {
						d.Ka();
						e && d.getPage(window.Ua - 1).Qb(window.Pc, v);
					}, 500);
					d.Ka();
					d.jScrollPane != p ? (d.jScrollPane.data("jsp").scrollTo(0, 0, v), d.jScrollPane.data("jsp").reinitialise(d.Tb)) : d.ea(d.da).parent().scrollTo({
						left: 0,
						top: 0
					}, 0, {
						axis: "xy"
					});
				}
			},
			wg: function() {
				var c = this;
				c.Gd();
				if (c.aa.ha && c.aa.ba == c.aa.ha.ra) {
					c.aa.ha.cb.wg(c);
				} else {
					if (c.aa.ba == U || c.aa.ba == H || c.aa.ba == D) {
						c.touchwipe = c.ea(c.da).touchwipe({
							wipeLeft: function() {
								!c.aa.Yb && !window.ec && c.ma == p && ((c.aa.ba == H || c.aa.ba == D) && 1 != c.aa.scale && c.next(), c.aa.ba == U && c.next());
							},
							wipeRight: function() {
								!c.aa.Yb && !window.ec && c.ma == p && ((c.aa.ba == H || c.aa.ba == D) && 1 != c.aa.scale && c.previous(), c.aa.ba == U && c.previous());
							},
							preventDefaultEvents: c.aa.ba == H || c.aa.ba == D || c.aa.ba == U,
							min_move_x: 300,
							min_move_y: 500
						});
					}
				} if (eb.platform.bd) {
					c.ea(c.da).on("mousedown", function(d) {
						c.$b = d.pageX;
						c.ac = d.pageY;
					});
				}
				c.ea(c.da).on("touchstart", function(d) {
					c.$b = d.originalEvent.touches[0].pageX;
					c.ac = d.originalEvent.touches[0].pageY;
				});
				c.ea(c.da).on(!eb.platform.bd ? "touchend" : "mouseup", function() {
					if (!eb.platform.ios && c.lb != p) {
						for (var d = 0; d < c.document.numPages; d++) {
							c.Wa(d) && c.ea(c.pages[d].Da).transition({
								y: 0,
								scale: 1
							}, 0, "ease", function() {
								c.ma > c.aa.scale && c.ma - c.aa.scale < c.aa.document.ZoomInterval && (c.ma += c.aa.document.ZoomInterval);
								0 < c.Ke - c.Dc && c.ma < c.aa.scale && (c.ma = c.aa.scale + c.aa.document.ZoomInterval);
								c.ma < c.aa.scale && c.jScrollPane != p && (c.jScrollPane.data("jsp").scrollTo(0, 0, v), c.jScrollPane.data("jsp").reinitialise(c.Tb));
								c.aa.fb(c.ma, {
									yh: n
								});
								c.ma = p;
							});
						}
						c.ea(c.da).addClass("flexpaper_pages_border");
						c.Ag = c.lb < c.ma;
						c.lb = p;
						c.Ee = p;
						c.ma = p;
						c.nb = p;
						c.Kb = p;
					}
					c.aa.pages.jScrollPane != p && c.aa.pages.jScrollPane.data("jsp").enable();
				});
				c.aa.ha && c.aa.ba == c.aa.ha.ra ? c.aa.ha.cb.mh(c) : eb.platform.touchdevice && c.ea(c.da).doubletap(function(d) {
					if (c.aa.ba == H || c.aa.ba == D) {
						(c.aa.ba == H || c.aa.ba == D) && 1 != c.aa.scale ? c.ad() : (c.aa.ba == H || c.aa.ba == D) && 1 == c.aa.scale && c.kc(), d.preventDefault();
					}
				}, p, 300);
				c.ea(c.da).on("scroll gesturechange", function() {
					c.aa.ba == U ? c.aa.renderer.ib && !c.ma && c.aa.renderer.dc(c.pages[0]) : c.aa.ha && c.aa.ba == c.aa.ha.ra || (eb.platform.ios && c.Eg(-1 * c.ea(c.da).scrollTop()), eb.platform.ios ? (setTimeout(function() {
						c.Je();
						c.dd();
					}, 1000), setTimeout(function() {
						c.Je();
						c.dd();
					}, 2000), setTimeout(function() {
						c.Je();
						c.dd();
					}, 3000)) : c.Je(), c.dd(), c.Ka(), c.$d != p && (window.clearTimeout(c.$d), c.$d = p), c.$d = setTimeout(function() {
						c.Jh();
						window.clearTimeout(c.$d);
						c.$d = p;
					}, 100), c.Qm = n);
				});
				this.Jh();
			},
			mh: x(),
			Eg: function(c) {
				for (var d = 0; d < this.document.numPages; d++) {
					this.Wa(d) && this.pages[d].Eg(c);
				}
			},
			Ci: function() {
				var c = this.ea(this.da).css("transform") + "";
				c != p && (c = c.replace("translate", ""), c = c.replace("(", ""), c = c.replace(")", ""), c = c.replace("px", ""), c = c.split(","), this.mc = parseFloat(c[0]), this.cd = parseFloat(c[1]), isNaN(this.mc) && (this.cd = this.mc = 0));
			},
			uh: function(c, d) {
				this.ea(this.da).transition({
					x: this.mc + (c - this.nb) / this.aa.scale,
					y: this.cd + (d - this.Kb) / this.aa.scale
				}, 0);
			},
			Xe: function(c, d) {
				this.aa.ha && this.aa.ha.cb.Xe(this, c, d);
			},
			ek: function(c, d) {
				var e = this.ga.width();
				return c / d - e * (this.zc / e / d);
			},
			fk: function(c) {
				var d = this.ga.height();
				return c / this.aa.scale - d * (this.Ac / d / this.aa.scale);
			},
			Gd: function() {
				this.aa.ha && this.aa.ha.cb.Gd(this);
			},
			lg: function() {
				if (this.aa.ha) {
					return this.aa.ha.cb.lg(this);
				}
			},
			getTotalPages: function() {
				return this.document.numPages;
			},
			Uf: function(c) {
				var d = this;
				c.empty();
				jQuery(d.aa.renderer).on("onTextDataUpdated", function() {
					d.Ka(d);
				});
				d.aa.Oc == p && !d.aa.document.DisableOverflow && (d.aa.Oc = d.ga.height(), eb.platform.touchonlydevice ? d.aa.Fd || d.ga.height(d.aa.Oc - 10) : d.ga.height(d.aa.Oc - 27));
				var e = d.aa.ha && d.aa.ha.backgroundColor ? "background-color:" + d.aa.ha.backgroundColor + ";" : "";
				d.aa.ha && d.aa.ha.backgroundImage && (e = "background-color:transparent;");
				if (d.aa.ba == C || d.aa.ba == U) {
					eb.platform.touchonlydevice && d.aa.ba == U && (eb.browser.jb.rb = v);
					var f = jQuery(d.aa.ca).height(),
						k = !eb.platform.touchonlydevice ? 26 : 31,
						f = d.ga.height() + (eb.browser.jb.rb ? window.annotations ? 0 : k - f : 10),
						k = d.ga.width() - 2,
						l = 1 < d.hd ? "visibility:hidden;" : "",
						g = eb.browser.msie && 9 > eb.browser.version ? "position:relative;" : "";
					d.aa.document.DisableOverflow ? c.append("<div id='" + d.container + "' class='flexpaper_pages' style='overflow:hidden;'></div>") : c.append("<div id='" + d.container + "' class='flexpaper_pages " + (!window.annotations ? "flexpaper_pages_border" : "") + "' style='-moz-user-select:none;-webkit-user-select:none;" + g + ";" + l + "height:" + f + "px;width:" + k + "px;overflow-y: auto;overflow-x: auto;;-webkit-overflow-scrolling: touch;-webkit-backface-visibility: hidden;-webkit-perspective: 1000;" + e + ";'></div>");
					eb.browser.jb.rb ? eb.platform.touchonlydevice ? (jQuery(c).css("overflow-y", "auto"), jQuery(c).css("overflow-x", "auto"), jQuery(c).css("-webkit-overflow-scrolling", "touch")) : (jQuery(c).css("overflow-y", "visible"), jQuery(c).css("overflow-x", "visible"), jQuery(c).css("-webkit-overflow-scrolling", "visible")) : (jQuery(c).css("overflow-y", "hidden"), jQuery(c).css("overflow-x", "hidden"), jQuery(c).css("-webkit-overflow-scrolling", "hidden"));
					if (eb.platform.touchdevice && (eb.platform.De || eb.platform.Vd || eb.platform.android)) {
						jQuery(d.da).on("touchmove", function(c) {
							if (!eb.platform.ios && 2 == c.originalEvent.touches.length) {
								c.preventDefault && c.preventDefault();
								c.returnValue = v;
								c = Math.sqrt((c.originalEvent.touches[0].pageX - c.originalEvent.touches[1].pageX) * (c.originalEvent.touches[0].pageX - c.originalEvent.touches[1].pageX) + (c.originalEvent.touches[0].pageY - c.originalEvent.touches[1].pageY) * (c.originalEvent.touches[0].pageY - c.originalEvent.touches[1].pageY));
								c *= 2;
								d.ma == p && (d.ea(d.da).removeClass("flexpaper_pages_border"), d.lb = 1, d.Ee = c);
								d.ma == p && (d.lb = 1, d.Dc = 1 + (jQuery(d.pages[0].Da).width() - d.ga.width()) / d.ga.width());
								var e = c = (d.lb + (c - d.Ee) / jQuery(d.da).width() - d.lb) / d.lb;
								d.Ab() || (1 < e && (e = 1), -0.3 > e && (e = -0.3), 0 < c && (c *= 0.7));
								d.Ke = d.Dc + d.Dc * c;
								d.He = 1 + (d.Ke - d.Dc);
								d.ma = d.pages[0].Hh(jQuery(d.pages[0].Da).width() * d.He);
								d.ma < d.aa.document.MinZoomSize && (d.ma = d.aa.document.MinZoomSize);
								d.ma > d.aa.document.MaxZoomSize && (d.ma = d.aa.document.MaxZoomSize);
								if (d.Ab()) {
									jQuery(".flexpaper_annotation_" + d.ia).hide();
									for (c = 0; c < d.document.numPages; c++) {
										d.Wa(c) && jQuery(d.pages[c].Da).transition({
											transformOrigin: "50% 0px",
											scale: d.He
										}, 0, "ease", x());
									}
								}
							}
						}), jQuery(d.da).on("touchstart", x()), jQuery(d.da).on("gesturechange", function(c) {
							if (d.vi != n) {
								d.aa.renderer.ib && jQuery(".flexpaper_flipview_canvas_highres").hide();
								d.ma == p && (d.lb = 1, d.Dc = 1 + (jQuery(d.pages[0].Da).width() - d.ga.width()) / d.ga.width());
								var e, f = e = (c.originalEvent.scale - d.lb) / d.lb;
								d.Ab() || (1 < f && (f = 1), -0.3 > f && (f = -0.3), 0 < e && (e *= 0.7));
								d.Ke = d.Dc + d.Dc * e;
								d.He = 1 + (d.Ke - d.Dc);
								d.ma = d.pages[0].Hh(jQuery(d.pages[0].Da).width() * d.He);
								d.ma < d.aa.document.MinZoomSize && (d.ma = d.aa.document.MinZoomSize);
								d.ma > d.aa.document.MaxZoomSize && (d.ma = d.aa.document.MaxZoomSize);
								c.preventDefault && c.preventDefault();
								if (d.Ab()) {
									jQuery(".flexpaper_annotation_" + d.ia).hide();
									for (c = 0; c < d.document.numPages; c++) {
										d.Wa(c) && jQuery(d.pages[c].Da).transition({
											transformOrigin: "50% 0px",
											scale: d.He
										}, 0, "ease", x());
									}
								}
								if (!d.Ab() && (0.7 <= f || -0.3 >= f)) {
									d.vi = n, d.ma > d.aa.scale && d.ma - d.aa.scale < d.aa.document.ZoomInterval && (d.ma += d.aa.document.ZoomInterval), d.aa.fb(d.ma), d.ma = p;
								}
							}
						}), jQuery(d.da).on("gestureend", function() {
							d.vi = v;
							if (d.ma != p) {
								if (d.Ab()) {
									for (var c = 0; c < d.document.numPages; c++) {
										d.Wa(c) && d.ea(d.pages[c].Da).transition({
											y: 0,
											scale: 1
										}, 0, "ease", function() {
											d.ma > d.aa.scale && d.ma - d.aa.scale < d.aa.document.ZoomInterval && (d.ma += d.aa.document.ZoomInterval);
											0 < d.Ke - d.Dc && d.ma < d.aa.scale && (d.ma = d.aa.scale + d.aa.document.ZoomInterval);
											d.aa.fb(d.ma, {
												yh: n
											});
											d.ma = p;
										});
									}
								} else {
									d.ma > d.aa.scale && d.ma - d.aa.scale < d.aa.document.ZoomInterval && (d.ma += d.aa.document.ZoomInterval), d.aa.fb(d.ma), d.ma = p;
								}
							}
						});
					}
				}
				if (d.aa.ba == H || d.aa.ba == D) {
					f = d.ga.height() - (eb.browser.msie ? 37 : 0), k = d.ga.width() - (eb.browser.msie ? 0 : 20), e = 0, 1 == d.aa.sa && d.aa.ba == D && (e = k / 3, k -= e), eb.platform.touchdevice ? eb.browser.jb.rb ? (c.append("<div id='" + d.container + "' style='-moz-user-select:none;-webkit-user-select:none;margin-left:" + e + "px;position:relative;width:100%;' class='flexpaper_twopage_container'><div id='" + d.container + "_1' class='flexpaper_pages' style='position:absolute;top:0px;height:99%;margin-top:20px;'></div><div id='" + d.container + "_2' class='flexpaper_pages' style='position:absolute;top:0px;height:99%;margin-top:20px;'></div></div>"), jQuery(c).css("overflow-y", "scroll"), jQuery(c).css("overflow-x", "scroll"), jQuery(c).css("-webkit-overflow-scrolling", "touch")) : (c.append("<div id='" + d.container + "_jpane' style='-moz-user-select:none;-webkit-user-select:none;height:" + f + "px;width:100%;" + (window.eb.browser.msie || eb.platform.android ? "overflow-y: scroll;overflow-x: scroll;" : "overflow-y: auto;overflow-x: auto;") + ";-webkit-overflow-scrolling: touch;'><div id='" + d.container + "' style='margin-left:" + e + "px;position:relative;height:100%;width:100%' class='flexpaper_twopage_container'><div id='" + d.container + "_1' class='flexpaper_pages' style='position:absolute;top:0px;height:99%;margin-top:20px;'></div><div id='" + d.container + "_2' class='flexpaper_pages' style='position:absolute;top:0px;height:99%;margin-top:20px;'></div></div></div>"), jQuery(c).css("overflow-y", "visible"), jQuery(c).css("overflow-x", "visible"), jQuery(c).css("-webkit-overflow-scrolling", "visible")) : (c.append("<div id='" + d.container + "' style='-moz-user-select:none;-webkit-user-select:none;margin-left:" + e + "px;position:relative;' class='flexpaper_twopage_container'><div id='" + d.container + "_1' class='flexpaper_pages' style='position:absolute;top:0px;height:99%;margin-top:" + (!eb.browser.msie ? 20 : 10) + "px;'></div><div id='" + d.container + "_2' class='flexpaper_pages " + (d.aa.ba == D && 2 > d.hd ? "flexpaper_hidden" : "") + "' style='position:absolute;top:0px;height:99%;margin-top:" + (!eb.browser.msie ? 20 : 10) + "px;'></div></div>"), jQuery(c).css("overflow-y", "auto"), jQuery(c).css("overflow-x", "auto"), jQuery(c).css("-webkit-overflow-scrolling", "touch")), d.aa.Vg == p && (d.aa.Vg = d.ga.height() - (!eb.platform.touchdevice ? 27 : 0), d.aa.Ie = d.ea(d.da).width() / 2 - 2), d.ea(d.da).css({
						height: "90%"
					}), d.ea("#" + this.container + "_2").css("left", d.ea("#" + d.container).width() / 2), eb.platform.touchdevice || (d.ea(d.da + "_1").width(d.aa.Ie), d.ea(d.da + "_2").width(d.aa.Ie));
				}
				d.aa.ba == M && (jQuery(c).css("overflow-y", "visible"), jQuery(c).css("overflow-x", "visible"), jQuery(c).css("-webkit-overflow-scrolling", "visible"), g = eb.browser.msie && 9 > eb.browser.version ? "position:relative;" : "", c.append("<div id='" + this.container + "' class='flexpaper_pages' style='" + g + ";" + (eb.platform.touchdevice ? "padding-left:10px;" : "") + (eb.browser.msie ? "overflow-y: scroll;overflow-x: hidden;" : "overflow-y: auto;overflow-x: hidden;-webkit-overflow-scrolling: touch;") + "'></div>"), jQuery(".flexpaper_pages").height(d.ga.height() - 0));
				d.aa.ha && d.aa.ha.cb.Uf(d, c);
				d.ga.trigger("onPagesContainerCreated");
				jQuery(d).bind("onScaleChanged", d.xg);
			},
			create: function(c) {
				var d = this;
				d.Uf(c);
				if (!eb.browser.jb.rb && d.aa.ba != M && (d.Tb = {}, d.aa.ba == H || d.aa.ba == D)) {
					d.jScrollPane = d.ea(d.da + "_jpane").jScrollPane(d.Tb);
				}
				for (c = 0; c < this.document.numPages; c++) {
					d.Wa(c) && this.addPage(c);
				}
				d.wg();
				if (!eb.browser.jb.rb) {
					if (d.aa.ba == C || d.aa.ba == U) {
						d.jScrollPane = d.ea(this.da).jScrollPane(d.Tb);
					}
					window.zine && !(d.aa.ha && d.aa.ha.ra == d.aa.ba) && jQuery(d.ea(this.da)).bind("jsp-initialised", function() {
						jQuery(this).find(".jspHorizontalBar, .jspVerticalBar").hide();
					}).jScrollPane().hover(function() {
						jQuery(this).find(".jspHorizontalBar, .jspVerticalBar").stop().fadeTo("fast", 0.9);
					}, function() {
						jQuery(this).find(".jspHorizontalBar, .jspVerticalBar").stop().fadeTo("fast", 0);
					});
				}!eb.browser.jb.rb && d.aa.ba == M && (d.jScrollPane = d.ea(d.da).jScrollPane(d.Tb));
				1 < d.hd && d.aa.ba == C && setTimeout(function() {
					d.scrollTo(d.hd, n);
					d.hd = -1;
					jQuery(d.da).css("visibility", "visible");
				}, 500);
				d.hd && d.aa.ba == U && jQuery(d.da).css("visibility", "visible");
			},
			getPage: function(c) {
				if (this.aa.ba == H || this.aa.ba == D) {
					if (0 != c % 2) {
						return this.pages[1];
					}
					if (0 == c % 2) {
						return this.pages[0];
					}
				} else {
					return this.aa.ba == U ? this.pages[0] : this.pages[c];
				}
			},
			Wa: function(c) {
				if (this.document.DisplayRange) {
					var d = this.document.DisplayRange.split("-");
					if (c + 1 >= parseInt(d[0]) && c <= parseInt(d[1]) - 1) {
						return n;
					}
				} else {
					return (this.aa.ba == H || this.aa.ba == D) && (0 == c || 1 == c) || this.aa.ba != H && this.aa.ba != D;
				}
			},
			addPage: function(c) {
				this.pages[c] = new pa(this.ia, c, this, this.ga, this.aa, this.df(c));
				this.pages[c].create(this.ea(this.da));
				jQuery(this.aa.ga).trigger("onPageCreated", c);
			},
			df: function(c) {
				for (var d = 0; d < this.document.dimensions.length; d++) {
					if (this.document.dimensions[d].page == c) {
						return this.document.dimensions[d];
					}
				}
				return {
					width: -1,
					height: -1
				};
			},
			scrollTo: function(c, d) {
				if (this.ja + 1 != c || d) {
					!eb.browser.jb.rb && this.jScrollPane ? this.jScrollPane.data("jsp").scrollToElement(this.pages[c - 1].ea(this.pages[c - 1].va), n, v) : jQuery(this.da).scrollTo && jQuery(this.da).scrollTo(this.pages[c - 1].ea(this.pages[c - 1].va), 0);
				}
				this.Ka();
			},
			Tk: function() {
				for (var c = 0; c < this.getTotalPages(); c++) {
					this.Wa(c) && this.pages[c] && this.pages[c].Pb && window.clearTimeout(this.pages[c].Pb);
				}
			},
			Jh: function() {
				this.pc();
			},
			pc: function() {
				var c = this;
				c.Qc != p && (window.clearTimeout(c.Qc), c.Qc = p);
				c.Qc = setTimeout(function() {
					c.cc();
				}, 200);
			},
			Pg: function() {
				if (this.jScrollPane != p) {
					try {
						this.jScrollPane.data("jsp").reinitialise(this.Tb);
					} catch (c) {}
				}
			},
			cc: function(c) {
				var d = this;
				if (d.aa.ha && d.aa.ba == d.aa.ha.ra) {
					d.aa.ha.cb.cc(d, c);
				} else {
					d.Qc != p && (window.clearTimeout(d.Qc), d.Qc = p);
					c = d.ea(this.da).scrollTop();
					for (var e = 0; e < this.document.numPages; e++) {
						if (d.Wa(e)) {
							var f = !d.pages[e].Va;
							this.pages[e].Zb(c, d.ea(this.da).height(), n) ? (f && d.ga.trigger("onVisibilityChanged", e + 1), this.pages[e].Va = n, this.pages[e].load(function() {
								if (d.aa.ba == H || d.aa.ba == D) {
									!d.ea(d.da).is(":animated") && 1 != d.aa.scale && (d.ea(d.da).css("margin-left", d.Od()), d.ea("#" + this.container + "_2").css("left", d.ea("#" + d.container).width() / 2)), !d.initialized && d.jScrollPane != p && (d.jScrollPane.data("jsp").reinitialise(d.Tb), d.initialized = n);
								}
							}), this.pages[e].vk(), this.pages[e].Ka()) : d.aa.ba != H && d.aa.ba != D && this.pages[e].unload();
						}
					}
				}
			},
			dd: function() {
				this.aa.ba != this.aa.ra() ? this.aa.Vc(this.ja + 1) : this.aa.Vc(this.ja);
			},
			Ka: function(c) {
				c = c ? c : this;
				for (var d = 0; d < c.document.numPages; d++) {
					c.Wa(d) && c.pages[d].Va && c.pages[d].Ka();
				}
			},
			Je: function() {
				for (var c = this.ja, d = this.ea(this.da).scrollTop(), e = 0; e < this.document.numPages; e++) {
					if (this.Wa(e) && this.aa.ba != U) {
						var f = !this.pages[e].Va;
						if (this.pages[e].Zb(d, this.ea(this.da).height(), v)) {
							c = e;
							f && this.ga.trigger("onVisibilityChanged", e + 1);
							break;
						}
					}
				}
				this.ja != c && this.ga.trigger("onCurrentPageChanged", c + 1);
				this.ja = c;
			},
			setCurrentCursor: function(c) {
				for (var d = 0; d < this.document.numPages; d++) {
					this.Wa(d) && ("TextSelectorCursor" == c ? jQuery(this.pages[d].la).addClass("flexpaper_nograb") : jQuery(this.pages[d].la).removeClass("flexpaper_nograb"));
				}
			},
			gotoPage: function(c) {
				this.aa.gotoPage(c);
			},
			ye: function(c, d) {
				c = parseInt(c);
				var e = this;
				e.aa.renderer.Sb && e.aa.renderer.Sb(e.pages[0]);
				jQuery(".flexpaper_pageword").remove();
				jQuery(".flexpaper_interactiveobject_" + e.ia).remove();
				e.pages[0].unload();
				e.pages[0].visible = n;
				var f = e.ea(e.da).scrollTop();
				e.aa.Vc(c);
				e.ga.trigger("onCurrentPageChanged", c);
				e.pages[0].Zb(f, e.ea(this.da).height(), n) && (e.ga.trigger("onVisibilityChanged", c + 1), e.pages[0].load(function() {
					d != p && d();
					e.pc();
					e.jScrollPane != p && e.jScrollPane.data("jsp").reinitialise(e.Tb);
				}));
			},
			ze: function(c, d) {
				c = parseInt(c);
				var e = this;
				0 == c % 2 && 0 < c && e.aa.ba == D && c != e.getTotalPages() && (c += 1);
				c == e.getTotalPages() && (e.aa.ba == H && 0 == e.getTotalPages() % 2) && (c = e.getTotalPages() - 1);
				0 == c % 2 && e.aa.ba == H && (c -= 1);
				c > e.getTotalPages() && (c = e.getTotalPages());
				jQuery(".flexpaper_pageword").remove();
				jQuery(".flexpaper_interactiveobject_" + e.ia).remove();
				if (c <= e.getTotalPages() && 0 < c) {
					e.aa.Vc(c);
					e.ja != c && e.ga.trigger("onCurrentPageChanged", c);
					e.pages[0].unload();
					e.pages[0].load(function() {
						if (e.aa.ba == H || e.aa.ba == D) {
							e.ea(e.da).animate({
								"margin-left": e.Od()
							}, {
								duration: 250
							}), e.ea("#" + this.container + "_2").css("left", e.ea("#" + e.container).width() / 2), e.Qa(e.aa.scale);
						}
					});
					1 < e.aa.sa ? (e.ea(e.pages[1].la + "_2").removeClass("flexpaper_hidden"), e.ea(e.da + "_2").removeClass("flexpaper_hidden")) : e.aa.ba == D && 1 == e.aa.sa && (e.ea(e.pages[1].la + "_2").addClass("flexpaper_hidden"), e.ea(e.da + "_2").addClass("flexpaper_hidden"));
					0 != e.getTotalPages() % 2 && (e.aa.ba == H && c >= e.getTotalPages()) && e.ea(e.pages[1].la + "_2").addClass("flexpaper_hidden");
					0 == e.getTotalPages() % 2 && (e.aa.ba == D && c >= e.getTotalPages()) && e.ea(e.pages[1].la + "_2").addClass("flexpaper_hidden");
					var f = e.ea(this.da).scrollTop();
					e.pages[1].unload();
					e.pages[1].visible = n;
					!e.ea(e.pages[1].la + "_2").hasClass("flexpaper_hidden") && e.pages[1].Zb(f, e.ea(this.da).height(), n) && (e.ga.trigger("onVisibilityChanged", c + 1), e.pages[1].load(function() {
						d != p && d();
						e.ea(e.da).animate({
							"margin-left": e.Od()
						}, {
							duration: 250
						});
						e.ea("#" + this.container + "_2").css("left", e.ea("#" + e.container).width() / 2);
						e.pc();
						e.jScrollPane != p && e.jScrollPane.data("jsp").reinitialise(e.Tb);
					}));
				}
			},
			rotate: function(c) {
				this.pages[c].rotate();
			},
			Od: function(c) {
				this.ga.width();
				var d = 0;
				1 == this.aa.sa && !c && this.aa.ba == D ? d = this.jn = (this.ga.width() / 2 - this.ea(this.da + "_1").width() / 2) * (this.aa.scale + 0.7) : (c = jQuery(this.da + "_2").width(), 0 == c && (c = this.ea(this.da + "_1").width()), d = this.hn = (this.ga.width() - (this.ea(this.da + "_1").width() + c)) / 2);
				10 > d && (d = 0);
				return d;
			},
			previous: function() {
				var c = this;
				if (c.aa.ba == C) {
					var d = c.ea(c.da).scrollTop() - c.pages[0].height - 14;
					0 > d && (d = 1);
					eb.browser.jb.rb ? c.ea(c.da).scrollTo(d, {
						axis: "y",
						duration: 500
					}) : c.jScrollPane.data("jsp").scrollToElement(this.pages[c.aa.sa - 2].ea(this.pages[c.aa.sa - 2].va), n, n);
				}
				c.aa.ba == U && 0 < c.aa.sa - 1 && (!eb.platform.touchdevice || 1 == this.aa.scale ? c.ye(c.aa.sa - 1) : (c.aa.Yb = n, c.ea(c.da).removeClass("flexpaper_pages_border"), c.ea(c.da).transition({
					x: 1000
				}, 350, function() {
					c.pages[0].unload();
					c.ea(c.da).transition({
						x: -800
					}, 0);
					c.ye(c.aa.sa - 1, x());
					c.ea(c.da).transition({
						x: 0
					}, 350, function() {
						c.aa.Yb = v;
						window.annotations || c.ea(c.da).addClass("flexpaper_pages_border");
					});
				})));
				c.aa.ha && c.aa.ba == c.aa.ha.ra && c.aa.ha.cb.previous(c);
				if ((c.aa.ba == H || c.aa.ba == D) && !(1 > c.aa.sa - 2)) {
					!eb.platform.touchdevice || 1 == this.aa.scale ? c.ze(c.aa.sa - 2) : (c.ja = c.aa.sa - 2, c.aa.Yb = n, c.ea(c.da).animate({
						"margin-left": 1000
					}, {
						duration: 350,
						complete: function() {
							jQuery(".flexpaper_interactiveobject_" + c.ia).remove();
							1 == c.aa.sa - 2 && c.aa.ba == D && c.pages[1].ea(c.pages[1].la + "_2").addClass("flexpaper_hidden");
							setTimeout(function() {
								c.ea(c.da).css("margin-left", -800);
								c.pages[0].unload();
								c.pages[1].unload();
								c.ea(c.da).animate({
									"margin-left": c.Od()
								}, {
									duration: 350,
									complete: function() {
										setTimeout(function() {
											c.aa.Yb = v;
											c.ze(c.aa.sa - 2);
										}, 500);
									}
								});
							}, 500);
						}
					}));
				}
			},
			next: function() {
				var c = this;
				if (c.aa.ba == C) {
					var d = c.aa.sa - 1,
						d = 100 < this.pages[c.aa.sa - 1].ea(this.pages[c.aa.sa - 1].va).offset().top - c.ga.offset().top ? c.aa.sa - 1 : c.aa.sa;
					eb.browser.jb.rb ? this.pages[d] && c.ea(c.da).scrollTo(this.pages[d].ea(this.pages[d].va), {
						axis: "y",
						duration: 500
					}) : c.jScrollPane.data("jsp").scrollToElement(this.pages[c.aa.sa].ea(this.pages[c.aa.sa].va), n, n);
				}
				c.aa.ba == U && c.aa.sa < c.getTotalPages() && (!eb.platform.touchdevice || 1 == c.aa.scale ? c.ye(c.aa.sa + 1) : (c.aa.Yb = n, c.ea(c.da).removeClass("flexpaper_pages_border"), c.ea(c.da).transition({
					x: -1000
				}, 350, "ease", function() {
					c.pages[0].unload();
					c.ea(c.da).transition({
						x: 1200
					}, 0);
					c.ye(c.aa.sa + 1, x());
					c.ea(c.da).transition({
						x: 0
					}, 350, "ease", function() {
						window.annotations || c.ea(c.da).addClass("flexpaper_pages_border");
						c.aa.Yb = v;
					});
				})));
				c.aa.ha && c.aa.ba == c.aa.ha.ra && c.aa.ha.cb.next(c);
				if (c.aa.ba == H || c.aa.ba == D) {
					if (c.aa.ba == H && c.aa.sa + 2 > c.getTotalPages()) {
						return v;
					}!eb.platform.touchdevice || 1 == this.aa.scale ? c.ze(c.aa.sa + 2) : (c.ja = c.aa.sa + 2, c.aa.Yb = n, c.ea(c.da).animate({
						"margin-left": -1000
					}, {
						duration: 350,
						complete: function() {
							jQuery(".flexpaper_interactiveobject_" + c.ia).remove();
							c.aa.sa + 2 <= c.getTotalPages() && 0 < c.aa.sa + 2 && c.pages[1].ea(c.pages[1].la + "_2").removeClass("flexpaper_hidden");
							setTimeout(function() {
								c.ea(c.da).css("margin-left", 800);
								c.pages[0].unload();
								c.pages[1].unload();
								c.pages[0].Va = n;
								c.pages[1].Va = n;
								c.ga.trigger("onVisibilityChanged", c.ja);
								c.ea(c.da).animate({
									"margin-left": c.Od(n)
								}, {
									duration: 350,
									complete: function() {
										setTimeout(function() {
											c.aa.Yb = v;
											c.ze(c.aa.sa + 2);
										}, 500);
									}
								});
							}, 500);
						}
					}));
				}
			},
			ud: function(c) {
				this.aa.ha && this.aa.ba == this.aa.ha.ra && this.aa.ha.cb.ud(this, c);
			}
		};
		return g;
	}(),
	pa = function() {
		function g(c, d, e, f, k, l) {
			this.ga = f;
			this.aa = k;
			this.pages = e;
			this.$a = 1000;
			this.za = this.Va = v;
			this.ia = c;
			this.pageNumber = d;
			this.dimensions = l;
			this.selectors = {};
			this.Mc = "data:image/gif;base64,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%3D";
			this.na = "dummyPage_" + this.pageNumber + "_" + this.ia;
			this.page = "page_" + this.pageNumber + "_" + this.ia;
			this.nc = "pageContainer_" + this.pageNumber + "_" + this.ia;
			this.Dk = this.nc + "_textlayer";
			this.Ze = "dummyPageCanvas_" + this.pageNumber + "_" + this.ia;
			this.$e = "dummyPageCanvas2_" + this.pageNumber + "_" + this.ia;
			this.We = this.page + "_canvasOverlay";
			this.Mb = "pageLoader_" + this.pageNumber + "_" + this.ia;
			this.Xh = this.nc + "_textoverlay";
			this.ba = this.aa.ba;
			this.ra = this.aa.ha ? this.aa.ha.ra : "";
			this.renderer = this.aa.renderer;
			c = this.aa.scale;
			this.scale = c;
			this.la = "#" + this.na;
			this.Da = "#" + this.page;
			this.va = "#" + this.nc;
			this.Om = "#" + this.Dk;
			this.Xf = "#" + this.Ze;
			this.Yf = "#" + this.$e;
			this.qm = "#" + this.We;
			this.Db = "#" + this.Mb;
			this.yg = "#" + this.Xh;
		}
		g.prototype = {
			ea: function(c) {
				if (0 < c.indexOf("undefined")) {
					return jQuery(p);
				}
				this.selectors || (this.selectors = {});
				this.selectors[c] || (this.selectors[c] = jQuery(c));
				return this.selectors[c];
			},
			show: function() {
				this.aa.ba != H && this.aa.ba != D && this.ea(this.Da).removeClass("flexpaper_hidden");
			},
			Gd: function() {
				if (!eb.platform.touchdevice || window.annotations && this.pages.jScrollPane) {
					!eb.browser.jb.rb && this.pages.jScrollPane ? this.aa.ba == U ? this.pages.jScrollPane.data("jsp").scrollToX(0, v) : this.pages.jScrollPane.data("jsp").scrollToX(this.ea(this.va).width() / 2, v) : this.ea(this.va).parent().scrollTo({
						left: "50%"
					}, 0, {
						axis: "x"
					});
				}
			},
			create: function(c) {
				var d = this;
				if (d.aa.ba == C && (c.append("<div class='flexpaper_page " + (d.aa.document.DisableOverflow ? "flexpaper_ppage" : "") + " " + (d.aa.document.DisableOverflow && d.pageNumber < d.aa.renderer.getNumPages() - 1 ? "ppage_break" : "ppage_none") + "' id='" + d.nc + "' style='position:relative;" + (d.aa.document.DisableOverflow ? "max-height:90%;" : "") + "'><div id='" + d.na + "' class='' style='z-index:11;" + d.getDimensions() + ";'></div></div>"), 0 < jQuery(d.aa.Xg).length)) {
					var e = this.$a * this.scale;
					jQuery(d.aa.Xg).append("<div id='" + d.Xh + "' class='flexpaper_page' style='position:relative;height:" + e + "px;width:100%;overflow:hidden;'></div>");
				}
				d.aa.ba == U && 0 == d.pageNumber && c.append("<div class='flexpaper_page' id='" + d.nc + "' class='flexpaper_rescale' style='position:relative;'><div id='" + d.na + "' class='' style='position:absolute;z-index:11;" + d.getDimensions() + "'></div></div>");
				if (d.aa.ba == H || d.aa.ba == D) {
					0 == d.pageNumber && jQuery(c.children().get(0)).append("<div class='flexpaper_page' id='" + d.nc + "_1' style='z-index:2;float:right;position:relative;'><div id='" + d.na + "_1' class='flexpaper_hidden flexpaper_border' style='" + d.getDimensions() + ";float:right;'></div></div>"), 1 == d.pageNumber && jQuery(c.children().get(1)).append("<div class='flexpaper_page' id='" + d.nc + "_2' style='position:relative;z-index:1;float:left;'><div id='" + d.na + "_2' class='flexpaper_hidden flexpaper_border' style='" + d.getDimensions() + ";float:left'></div></div>");
				}
				d.aa.ba == M && (c.append("<div class='flexpaper_page' id='" + d.nc + "' style='position:relative;" + (eb.browser.msie ? "clear:none;float:left;" : "display:inline-block;") + "'><div id=\"" + d.na + '" class="flexpaper_page flexpaper_thumb flexpaper_border flexpaper_load_on_demand" style="margin-left:10px;' + d.getDimensions() + '"></div></div>'), jQuery(d.va).on("mousedown", function() {
					d.aa.gotoPage(d.pageNumber + 1);
				}));
				d.aa.ba == d.ra ? d.aa.ha.ic.create(d, c) : (d.aa.renderer.Ic(d), d.show(), d.height = d.ea(d.va).height(), d.li());
				jQuery(d.va).on("mouseover", function() {
					jQuery(".flexpaper_mark_link, .pdfPageLink_" + d.pageNumber).css("background", d.aa.linkColor);
					jQuery(".flexpaper_mark_link, .pdfPageLink_" + d.pageNumber).css({
						opacity: d.aa.of
					});
				});
				jQuery(d.va).on("mouseout", function(c) {
					0 == jQuery(c.toElement).parents(".flexpaper_page").find("#" + d.na + "_textoverlay").length && (jQuery(".pdfPageLink_" + d.pageNumber).css("background", ""), jQuery(".flexpaper_mark_link, .pdfPageLink_" + d.pageNumber).css({
						opacity: 0
					}));
				});
			},
			dk: function() {
				if (this.aa.ba == C || this.aa.ba == U) {
					return this.We;
				}
				if (this.aa.ba == H || this.aa.ba == D) {
					if (0 == this.pageNumber) {
						return this.We + "_1";
					}
					if (1 == this.pageNumber) {
						return this.We + "_2";
					}
				}
			},
			Eg: function(c) {
				this.ea(this.yg).css({
					top: c
				});
			},
			Jb: function() {
				(this.aa.ba == C || this.aa.ba == U || this.aa.ba == this.ra) && this.ea("#" + this.Mb).hide();
				if (this.aa.ba == H || this.aa.ba == D) {
					0 == this.pageNumber && this.ea(this.Db + "_1").hide(), 1 == this.pageNumber && this.ea(this.Db + "_2").hide();
				}
			},
			rc: function() {
				var c = this;
				if (c.aa.ba == C || c.aa.ba == U || c.aa.ba == c.ra) {
					c.$a = 1000;
					if (0 < c.ea(c.Db).length) {
						return;
					}
					if (c.kd === p && c.aa.ba == c.ra) {
						c.kd = jQuery("<div class='flexpaper_pageLoader' style='position:absolute;left:50%;top:50%;'></div>"), c.ea(c.va).append(c.kd), c.kd.spin({
							color: "#777"
						}), c.kf = setTimeout(function() {
							c.kd.remove();
						}, 1000);
					} else {
						var d = 0 < jQuery(c.va).length ? jQuery(c.va) : c.bc;
						0 != d.length ? 0 == d.find("#" + c.Mb).length && d.append("<img id='" + c.Mb + "' src='" + c.Mc + "' class='flexpaper_pageLoader'  style='position:absolute;left:50%;top:50%;height:11px;margin-left:" + (c.Gc() - 10) + "px;' />") : fa("can't show loader, missing container for page " + c.pageNumber);
					}
				}
				if (c.aa.ba == H || c.aa.ba == D) {
					if (0 == c.pageNumber) {
						if (0 < c.ea(c.Db + "_1").length) {
							c.ea(c.Db + "_1").show();
							return;
						}
						c.ea(c.la + "_1").append("<img id='" + c.Mb + "_1' src='" + c.Mc + "' style='position:absolute;left:" + (c.Sa() - 30) + "px;top:" + c.Xa() / 2 + "px;' />");
						c.ea(c.Db + "_1").show();
					}
					1 == c.pageNumber && (0 < c.ea(c.Db + "_2").length || c.ea(c.la + "_2").append("<img id='" + c.Mb + "_2' src='" + c.Mc + "' style='position:absolute;left:" + (c.Sa() / 2 - 10) + "px;top:" + c.Xa() / 2 + "px;' />"), c.ea(c.Db + "_2").show());
				}
			},
			Qa: function() {
				var c = this.Sa(),
					d = this.Xa(),
					e = this.Gc();
				if (this.aa.ba == C || this.aa.ba == U) {
					90 == this.rotation ? (this.ea(this.va).css({
						"margin-left": (this.ea(this.va).height() - this.ea(this.va).width()) / 2 + 10
					}), this.ea(this.va).css({
						"margin-top": (this.ea(this.va).width() - this.ea(this.va).height()) / 2 + 10
					})) : 270 == this.rotation ? (this.ea(this.va).css({
						"margin-left": (this.ea(this.va).height() - this.ea(this.va).width()) / 2 + 10
					}), this.ea(this.va).css({
						"margin-top": (this.ea(this.va).width() - this.ea(this.va).height()) / 2 + 100
					})) : 180 == this.rotation ? this.ea(this.va).css({
						height: d,
						width: c,
						"margin-left": 3 * e,
						"margin-top": 0
					}) : this.ea(this.va).css({
						height: d,
						width: c,
						"margin-left": e,
						"margin-top": 0
					}), this.ea(this.yg).css({
						height: d,
						width: "99%",
						"margin-left": e
					}), this.ea(this.la).css({
						height: d,
						width: c,
						"margin-left": e
					}), this.ea(this.Da).css({
						height: d,
						width: c,
						"margin-left": e
					}), this.ea(this.Xf).css({
						height: d,
						width: c
					}), this.ea(this.Yf).css({
						height: d,
						width: c
					}), this.ea(this.Db).css({
						"margin-left": e
					}), this.aa.renderer.ib && (jQuery(".flexpaper_flipview_canvas_highres").css({
						width: 0.25 * c,
						height: 0.25 * d
					}).show(), this.scale < this.we() ? this.aa.renderer.Sb(this) : this.aa.renderer.dc(this)), this.Ce(this.scale, e);
				}
				if (this.aa.ba == H || this.aa.ba == D) {
					this.ea(this.la + "_1").css({
						height: d,
						width: c
					}), this.ea(this.la + "_2").css({
						height: d,
						width: c
					}), this.ea(this.la + "_1_textoverlay").css({
						height: d,
						width: c
					}), this.ea(this.la + "_2_textoverlay").css({
						height: d,
						width: c
					}), this.ea(this.Da).css({
						height: d,
						width: c
					}), eb.browser.jb.rb || (0 == this.pages.ja ? this.pages.ea(this.pages.da).css({
						height: d,
						width: c
					}) : this.pages.ea(this.pages.da).css({
						height: d,
						width: 2 * c
					}), this.aa.ba == H && this.pages.ea(this.pages.da).css({
						width: "100%"
					})), eb.platform.touchdevice && 1 <= this.scale && this.pages.ea(this.pages.da).css({
						width: 2 * c
					}), eb.platform.touchdevice && (this.aa.ba == H && this.pages.ea(this.pages.da + "_2").css("left", this.pages.ea(this.pages.da + "_1").width() + e + 2), this.aa.ba == D && this.pages.ea(this.pages.da + "_2").css("left", this.pages.ea(this.pages.da + "_1").width() + e + 2));
				}
				if (this.aa.ba == this.ra) {
					var f = this.Bh() * this.$a,
						k = this.Sa() / f;
					this.dimensions.mb != p && (this.pb && this.aa.renderer.Fa) && (k = this.pages.Uc / 2 / f);
					this.aa.ba == this.ra ? 1 == this.scale && this.Ce(k, e) : this.Ce(k, e);
				}
				this.height = d;
				this.width = c;
			},
			we: ca(1),
			Ab: function() {
				return this.aa.ba == U;
			},
			resize: x(),
			Bh: function() {
				return this.dimensions.Aa / this.dimensions.Oa;
			},
			$c: function() {
				return this.aa.ba == this.ra ? this.aa.ha.ic.$c(this) : this.$a * this.scale * (this.dimensions.Aa / this.dimensions.Oa);
			},
			Nd: function() {
				return this.aa.ba == this.ra ? this.aa.ha.ic.Nd(this) : this.$a * this.scale;
			},
			getDimensions: function() {
				var c = this.ve(),
					d = this.aa.$c();
				if (this.aa.ba == C || this.aa.ba == U) {
					var e = this.$a * this.scale;
					return "height:" + e + "px;width:" + e * c + "px;margin-left:" + (d - e * c) / 2 + "px;";
				}
				if (this.aa.ba == this.ra) {
					return this.aa.ha.ic.getDimensions(this, c);
				}
				if (this.aa.ba == H || this.aa.ba == D) {
					return e = this.ga.width() / 2 * this.scale, (0 == this.pageNumber ? "margin-left:0px;" : "") + "height:" + e + "px;width:" + e * c + "px";
				}
				if (this.aa.ba == M) {
					return e = this.$a * ((this.ga.height() - 100) / this.$a) / 2.7, "height:" + e + "px;width:" + e * c + "px";
				}
			},
			ve: function() {
				return this.dimensions.Aa / this.dimensions.Oa;
			},
			Sa: function() {
				return this.aa.ba == this.ra ? this.aa.ha.ic.Sa(this) : this.$a * this.ve() * this.scale;
			},
			kg: function() {
				return this.aa.ba == this.ra ? this.aa.ha.ic.kg(this) : this.$a * this.ve() * this.scale;
			},
			Hh: function(c) {
				return c / (this.$a * this.ve());
			},
			Xa: function() {
				return this.aa.ba == this.ra ? this.aa.ha.ic.Xa(this) : this.$a * this.scale;
			},
			jg: function() {
				return this.aa.ba == this.ra ? this.aa.ha.ic.jg(this) : this.$a * this.scale;
			},
			Gc: function() {
				var c = this.aa.$c(),
					d = 0;
				if (this.aa.ba == C || this.aa.ba == U) {
					return d = (c - this.Sa()) / 2 / 2 - 4, 0 < d ? d : 0;
				}
				if (this.aa.ba == H || this.aa.ba == D) {
					return 0;
				}
				if (this.aa.ba == this.ra) {
					return this.aa.ha.ic.Gc(this);
				}
			},
			Zb: function(c, d, e) {
				var f = v;
				if (this.aa.ba == C || this.aa.ba == M) {
					if (this.offset = this.ea(this.va).offset()) {
						this.pages.Wg || (this.pages.Wg = this.aa.ka.offset().top);
						var f = this.offset.top - this.pages.Wg + c,
							k = this.offset.top + this.height;
						d = c + d;
						f = e || eb.platform.touchdevice && !eb.browser.jb.rb ? this.Va = c - this.height <= f && d >= f || f - this.height <= c && k >= d : c <= f && d >= f || f <= c && k >= d;
					} else {
						f = v;
					}
				}
				this.aa.ba == U && (f = this.Va = 0 == this.pageNumber);
				this.aa.ba == this.ra && (f = this.Va = this.aa.ha.ic.Zb(this));
				if (this.aa.ba == D) {
					if (0 == this.pages.getTotalPages() % 2 && this.pages.ja >= this.pages.getTotalPages() && 1 == this.pageNumber) {
						return v;
					}
					f = this.Va = 0 == this.pageNumber || 0 != this.pages.ja && 1 == this.pageNumber;
				}
				if (this.aa.ba == H) {
					if (0 != this.pages.getTotalPages() % 2 && this.pages.ja >= this.pages.getTotalPages() && 1 == this.pageNumber) {
						return v;
					}
					f = this.Va = 0 == this.pageNumber || 1 == this.pageNumber;
				}
				return f;
			},
			vk: function() {
				this.za || this.load();
			},
			load: function(c) {
				this.Ka(c);
				if (!this.za) {
					if (this.aa.ba == H && (c = this.aa.renderer.getDimensions(this.pageNumber - 1, this.pageNumber - 1)[this.pages.ja + this.pageNumber], c.width != this.dimensions.width || c.height != this.dimensions.height)) {
						this.dimensions = c, this.Qa();
					}
					if (this.aa.ba == D && (c = this.aa.renderer.getDimensions(this.pageNumber - 1, this.pageNumber - 1)[this.pages.ja - (0 < this.pages.ja ? 1 : 0) + this.pageNumber], c.width != this.dimensions.width || c.height != this.dimensions.height)) {
						this.dimensions = c, this.Qa();
					}
					if (this.aa.ba == U) {
						c = this.aa.renderer.getDimensions(this.pageNumber - 1, this.pageNumber - 1)[this.pages.ja];
						if (c.width != this.dimensions.width || c.height != this.dimensions.height) {
							this.dimensions = c, this.Qa(), jQuery(".flexpaper_pageword_" + this.ia).remove(), this.Ka();
						}
						this.dimensions.loaded = v;
					}
					this.aa.renderer.Eb(this, v);
					"function" === typeof this.mg && this.loadOverlay();
				}
			},
			unload: function() {
				if (this.za || !(this.aa.ba != H && this.aa.ba != D && this.aa.ba != this.ra)) {
					delete this.selectors, this.selectors = {}, jQuery(this.ua).unbind(), delete this.ua, this.ua = p, this.za = v, this.aa.renderer.unload(this), jQuery(this.Db).remove(), this.kd && (delete this.kd, this.kd = p), this.aa.ba == this.ra && this.aa.ha.ic.unload(this), this.aa.ba != H && this.aa.ba != D && this.ea("#" + this.dk()).remove(), "function" === typeof this.mg && this.mn();
				}
			},
			Ka: function(c) {
				this.aa.ba != M && ((this.Va || c != p) && !this.pages.animating) && this.aa.renderer.Ka(this, v, c);
			},
			Qb: function(c, d) {
				this.aa.renderer.Qb(this, c, d);
			},
			li: function() {
				if (this.aa.ba == C || this.aa.ba == U) {
					!(eb.browser.msie && 9 > eb.browser.version) && !eb.platform.ios && (new ga(this.aa, "CanvasPageRenderer" == this.renderer.sd() ? this.la : this.Da, this.ea(this.va).parent())).scroll();
				}
			},
			Ce: function(c, d) {
				var e = this;
				if (e.aa.Ca[e.pageNumber]) {
					for (var f = 0; f < e.aa.Ca[e.pageNumber].length; f++) {
						if ("link" == e.aa.Ca[e.pageNumber][f].type) {
							var k = e.aa.Ca[e.pageNumber][f].sk * c,
								l = e.aa.Ca[e.pageNumber][f].tk * c,
								g = e.aa.Ca[e.pageNumber][f].width * c,
								m = e.aa.Ca[e.pageNumber][f].height * c;
							if (0 == jQuery("#flexpaper_mark_link_" + e.pageNumber + "_" + f).length) {
								var q = jQuery(String.format("<div id='flexpaper_mark_link_{4}_{5}' class='flexpaper_mark_link flexpaper_mark' style='left:{0}px;top:{1}px;width:{2}px;height:{3}px;box-shadow: 0px 0px 0px 0px;'></div>", k, l, g, m, e.pageNumber, f)),
									m = e.va;
								0 == jQuery(m).length && (m = e.bc);
								q = jQuery(m).append(q).find("#flexpaper_mark_link_" + e.pageNumber + "_" + f);
								q.data("link", e.aa.Ca[e.pageNumber][f].href);
								q.bind("mousedown touchstart", function(c) {
									0 == jQuery(this).data("link").indexOf("actionGoTo:") ? e.aa.gotoPage(jQuery(this).data("link").substr(11)) : jQuery(e.ga).trigger("onExternalLinkClicked", jQuery(this).data("link"));
									c.preventDefault();
									c.stopImmediatePropagation();
									return v;
								});
								q.bind("mouseover", function() {
									jQuery(this).css({
										opacity: 0.4
									});
								});
								q.bind("mouseout", function() {
									jQuery(this).css({
										opacity: 0.2
									});
								});
							} else {
								q = jQuery("#flexpaper_mark_link_" + e.pageNumber + "_" + f), q.css({
									left: k + "px",
									top: l + "px",
									width: g + "px",
									height: m + "px",
									"margin-left": d + "px"
								});
							}
						}
						if ("video" == e.aa.Ca[e.pageNumber][f].type) {
							if (q = e.aa.Ca[e.pageNumber][f].Rl * c, k = e.aa.Ca[e.pageNumber][f].Sl * c, l = e.aa.Ca[e.pageNumber][f].width * c, g = e.aa.Ca[e.pageNumber][f].height * c, m = e.aa.Ca[e.pageNumber][f].src, 0 == jQuery("#flexpaper_mark_video_" + e.pageNumber + "_" + f).length) {
								var y = jQuery(String.format("<div id='flexpaper_mark_video_{4}_{5}' class='flexpaper_mark_video flexpaper_mark' style='left:{0}px;top:{1}px;width:{2}px;height:{3}px;margin-left:{7}px'><img src='{6}' style='width:{2}px;height:{3}px;' class='flexpaper_mark'/></div>", q, k, l, g, e.pageNumber, f, m, d)),
									m = e.va;
								0 == jQuery(m).length && (m = e.bc);
								q = jQuery(m).append(y).find("#flexpaper_mark_video_" + e.pageNumber + "_" + f);
								q.data("video", e.aa.Ca[e.pageNumber][f].url);
								q.bind("mousedown touchstart", function(c) {
									var d = jQuery(this).data("video");
									if (d && 0 <= d.toLowerCase().indexOf("youtube")) {
										for (var e = d.substr(d.indexOf("?") + 1).split("&"), f = "", k = 0; k < e.length; k++) {
											0 == e[k].indexOf("v=") && (f = e[k].substr(2));
										}
										jQuery(this).html(String.format("<iframe width='{0}' height='{1}' src='http://www.youtube.com/embed/{2}?rel=0&autoplay=1&enablejsapi=1' frameborder='0' allowfullscreen ></iframe>", jQuery(this).width(), jQuery(this).height(), f));
									}
									d && 0 <= d.toLowerCase().indexOf("vimeo") && (f = d.substr(d.lastIndexOf("/") + 1), jQuery(this).html(String.format("<iframe src='//player.vimeo.com/video/{2}?autoplay=1' width='{0}' height='{1}' frameborder='0' webkitallowfullscreen mozallowfullscreen allowfullscreen></iframe>", jQuery(this).width(), jQuery(this).height(), f)));
									c.preventDefault();
									c.stopImmediatePropagation();
									return v;
								});
							} else {
								y = jQuery("#flexpaper_mark_video_" + e.pageNumber + "_" + f), y.css({
									left: q + "px",
									top: k + "px",
									width: l + "px",
									height: g + "px",
									"margin-left": d + "px"
								}).find(".flexpaper_mark").css({
									width: l + "px",
									height: g + "px"
								});
							}
						}
						if ("image" == e.aa.Ca[e.pageNumber][f].type) {
							var m = e.aa.Ca[e.pageNumber][f].nk * c,
								y = e.aa.Ca[e.pageNumber][f].ok * c,
								z = e.aa.Ca[e.pageNumber][f].width * c,
								t = e.aa.Ca[e.pageNumber][f].height * c,
								q = e.aa.Ca[e.pageNumber][f].src,
								k = e.aa.Ca[e.pageNumber][f].href,
								l = e.aa.Ca[e.pageNumber][f].lk;
							0 == jQuery("#flexpaper_mark_image_" + e.pageNumber + "_" + f).length ? (g = jQuery(String.format("<div id='flexpaper_mark_image_{4}_{5}' class='flexpaper_mark_image flexpaper_mark' style='left:{0}px;top:{1}px;width:{2}px;height:{3}px;'><img src='{6}' style='width:{2}px;height:{3}px;' class='flexpaper_mark'/></div>", m, y, z, t, e.pageNumber, f, q)), m = e.va, 0 == jQuery(m).length && (m = e.bc), g = jQuery(m).append(g).find("#flexpaper_mark_image_" + e.pageNumber + "_" + f), g.data("image", e.aa.Ca[e.pageNumber][f].url), k != p && 0 < k.length && (g.data("link", k), g.bind("mousedown touchstart", function(c) {
								0 == jQuery(this).data("link").indexOf("actionGoTo:") ? e.aa.gotoPage(jQuery(this).data("link").substr(11)) : jQuery(e.ga).trigger("onExternalLinkClicked", jQuery(this).data("link"));
								c.preventDefault();
								c.stopImmediatePropagation();
								return v;
							})), l != p && 0 < l.length && (g.data("hoversrc", l), g.data("imagesrc", q), g.bind("mouseover", function() {
								jQuery(this).find(".flexpaper_mark").attr("src", jQuery(this).data("hoversrc"));
							}), g.bind("mouseout", function() {
								jQuery(this).find(".flexpaper_mark").attr("src", jQuery(this).data("imagesrc"));
							}))) : (g = jQuery("#flexpaper_mark_image_" + e.pageNumber + "_" + f), g.css({
								left: m + "px",
								top: y + "px",
								width: z + "px",
								height: t + "px",
								"margin-left": d + "px"
							}).find(".flexpaper_mark").css({
								width: z + "px",
								height: t + "px"
							}));
						}
					}
				}
			},
			Bb: function() {
				jQuery(this.va).find("*").unbind();
				jQuery(this).unbind();
				jQuery(this.ua).unbind();
				delete this.ua;
				this.ua = p;
				jQuery(this.va).find("*").remove();
				this.selectors = this.pages = this.aa = this.ga = p;
			},
			rotate: function() {
				if (!this.rotation || 360 == this.rotation) {
					this.rotation = 0;
				}
				this.rotation += 90;
				if (this.aa.ba == C || this.aa.ba == U) {
					this.Qa(), this.ea(this.va).transition({
						rotate: this.rotation
					});
				}
			}
		};
		return g;
	}();

function qa(g, c) {
	this.aa = this.ta = g;
	this.ga = this.aa.ga;
	this.resources = this.aa.resources;
	this.ia = this.aa.ia;
	this.document = c;
	this.Ed = p;
	this.Ja = "toolbar_" + this.aa.ia;
	this.ca = "#" + this.Ja;
	this.rh = this.Ja + "_bttnPrintdialogPrint";
	this.qh = this.Ja + "_bttnPrintdialogCancel";
	this.nh = this.Ja + "_bttnPrintDialog_RangeAll";
	this.oh = this.Ja + "_bttnPrintDialog_RangeCurrent";
	this.ph = this.Ja + "_bttnPrintDialog_RangeSpecific";
	this.Mf = this.Ja + "_bttnPrintDialogRangeText";
	this.Rh = this.Ja + "_labelPrintProgress";
	this.ag = p;
	this.create = function() {
		var c = this;
		if (!eb.platform.touchonlydevice && !c.ag) {
			var e = c.resources.oa.Ki,
				f = String.format("<div class='flexpaper_floatright flexpaper_bttnPercent' sbttnPrintIdtyle='text-align:center;padding-top:5px;background-repeat:no-repeat;width:20px;height:20px;font-size:9px;font-family:Arial;background-image:url({0})'><div id='lblPercent'></div></div>", c.resources.oa.fj);
			eb.browser.msie && addCSSRule(".flexpaper_tbtextinput", "height", "18px");
			jQuery(c.ca).html(String.format("<img src='{0}' class='flexpaper_tbbutton print flexpaper_bttnPrint'/>", c.resources.oa.aj) + String.format("<img src='{0}' class='flexpaper_tbseparator' />", e) + (c.aa.config.document.ViewModeToolsVisible ? String.format("<img src='{1}' class='flexpaper_bttnSinglePage flexpaper_tbbutton flexpaper_viewmode flexpaper_singlepage {0}' />", c.aa.ub == C ? "flexpaper_tbbutton_pressed" : "", c.resources.oa.ej) + String.format("<img src='{1}' class='flexpaper_bttnTwoPage flexpaper_tbbutton flexpaper_viewmode flexpaper_twopage {0}' />", c.aa.ub == H ? "flexpaper_tbbutton_pressed" : "", c.resources.oa.kj) + String.format("<img src='{0}' class='flexpaper_tbbutton flexpaper_thumbview flexpaper_viewmode flexpaper_bttnThumbView' />", c.resources.oa.hj) + String.format("<img src='{0}' class='flexpaper_tbbutton flexpaper_fitmode flexpaper_fitwidth flexpaper_bttnFitWidth' />", c.resources.oa.Zi) + String.format("<img src='{0}' class='flexpaper_tbbutton flexpaper_fitmode flexpaper_fitheight flexpaper_bttnFitHeight'/>", c.resources.oa.Yi) + String.format("<img src='{0}' class='flexpaper_tbbutton flexpaper_bttnRotate'/>", c.resources.oa.cj) + String.format("<img src='{0}' class='flexpaper_tbseparator' />", e) : "") + (c.aa.config.document.ZoomToolsVisible ? String.format("<div class='flexpaper_slider flexpaper_zoomSlider'><div class='flexpaper_handle'></div></div>") + String.format("<input type='text' class='flexpaper_tbtextinput flexpaper_txtZoomFactor' style='width:40px;' />") + String.format("<img class='flexpaper_tbbutton flexpaper_bttnFullScreen' src='{0}' />", c.resources.oa.Si) + String.format("<img src='{0}' class='flexpaper_tbseparator' style='margin-left:5px' />", e) : "") + (c.aa.config.document.NavToolsVisible ? String.format("<img src='{0}' class='flexpaper_tbbutton flexpaper_previous flexpaper_bttnPrevPage'/>", c.resources.oa.Gi) + String.format("<input type='text' class='flexpaper_tbtextinput flexpaper_currPageNum flexpaper_txtPageNumber' value='1' style='width:50px;text-align:right;' />") + String.format("<div class='flexpaper_tblabel flexpaper_numberOfPages flexpaper_lblTotalPages'> / </div>") + String.format("<img src='{0}' class='flexpaper_tbbutton flexpaper_next flexpaper_bttnPrevNext'/>", c.resources.oa.Ii) + String.format("<img src='{0}' class='flexpaper_tbseparator' />", e) : "") + (c.aa.config.document.CursorToolsVisible ? String.format("<img src='{0}' class='flexpaper_tbbutton flexpaper_bttnTextSelect'/>", c.resources.oa.gj) + String.format("<img src='{0}' class='flexpaper_tbbutton flexpaper_tbbutton_pressed flexpaper_bttnHand'/>", c.resources.oa.Wi) + String.format("<img src='{0}' class='flexpaper_tbseparator' />", e) : "") + (c.aa.config.document.SearchToolsVisible ? String.format("<input type='text' class='flexpaper_tbtextinput flexpaper_txtSearch' style='width:70px;margin-left:4px' />") + String.format("<img src='{0}' class='flexpaper_find flexpaper_tbbutton flexpaper_bttnFind' />", c.resources.oa.Ni) + String.format("<img src='{0}' class='flexpaper_tbseparator' />", e) : "") + f);
			jQuery(c.ca).addClass("flexpaper_toolbarstd");
		} else {
			c.ag || (e = c.resources.oa.Li, jQuery(c.ca).html((!eb.platform.touchonlydevice ? String.format("<img src='{0}' class='flexpaper_tbbutton_large flexpaper_print flexpaper_bttnPrint' style='margin-left:5px;'/>", c.resources.oa.bj) : "") + (c.aa.config.document.ViewModeToolsVisible ? (!eb.platform.Nb ? String.format("<img src='{0}' class='flexpaper_tbbutton_large flexpaper_viewmode flexpaper_singlepage {1} flexpaper_bttnSinglePage' style='margin-left:15px;'>", c.resources.oa.dj, c.aa.ub == C ? "flexpaper_tbbutton_pressed" : "") : "") + (!eb.platform.Nb ? String.format("<img src='{0}' style='margin-left:-1px;' class='flexpaper_tbbutton_large flexpaper_viewmode  flexpaper_twopage {1} flexpaper_bttnTwoPage'>", c.resources.oa.mj, c.aa.ub == H ? "flexpaper_tbbutton_pressed" : "") : "") + (!eb.platform.Nb ? String.format("<img src='{0}' style='margin-left:-1px;' class='flexpaper_tbbutton_large flexpaper_viewmode flexpaper_thumbview flexpaper_bttnThumbView'>", c.resources.oa.jj) : "") + (!eb.platform.Nb ? String.format("<img src='{0}' style='margin-left:-1px;' class='flexpaper_tbbutton_large flexpaper_fitmode flexpaper_fitwidth flexpaper_bttnFitWidth'>", c.resources.oa.Ri) : "") + (!eb.platform.Nb ? String.format("<img src='{0}' style='margin-left:-1px;' class='flexpaper_tbbutton_large flexpaper_fitmode fitheight flexpaper_bttnFitHeight'>", c.resources.oa.$i) : "") + "" : "") + (c.aa.config.document.ZoomToolsVisible ? String.format("<img class='flexpaper_tbbutton_large flexpaper_bttnZoomIn' src='{0}' style='margin-left:5px;' />", c.resources.oa.oj) + String.format("<img class='flexpaper_tbbutton_large flexpaper_bttnZoomOut' src='{0}' style='margin-left:-1px;' />", c.resources.oa.pj) + (!eb.platform.Nb ? String.format("<img class='flexpaper_tbbutton_large flexpaper_bttnFullScreen' src='{0}' style='margin-left:-1px;' />", c.resources.oa.Ti) : "") + "" : "") + (c.aa.config.document.NavToolsVisible ? String.format("<img src='{0}' class='flexpaper_tbbutton_large flexpaper_previous flexpaper_bttnPrevPage' style='margin-left:15px;'/>", c.resources.oa.Hi) + String.format("<input type='text' class='flexpaper_tbtextinput_large flexpaper_currPageNum flexpaper_txtPageNumber' value='1' style='width:70px;text-align:right;' />") + String.format("<div class='flexpaper_tblabel_large flexpaper_numberOfPages flexpaper_lblTotalPages'> / </div>") + String.format("<img src='{0}'  class='flexpaper_tbbutton_large flexpaper_next flexpaper_bttnPrevNext'/>", c.resources.oa.Ji) + "" : "") + (c.aa.config.document.SearchToolsVisible ? String.format("<input type='text' class='flexpaper_tbtextinput_large flexpaper_txtSearch' style='margin-left:15px;width:130px;' />") + String.format("<img src='{0}' class='flexpaper_find flexpaper_tbbutton_large flexpaper_bttnFind' style=''/>", c.resources.oa.Qi) + "" : "")), jQuery(c.ca).addClass("flexpaper_toolbarios"));
		}
		jQuery(c.ga).bind("onDocumentLoaded", function() {
			jQuery(c.ca).find(".flexpaper_bttnPercent").hide();
		});
	};
	this.Sh = function(c) {
		c = this.Ya = c.split("\n");
		jQuery(this.ca).find(".flexpaper_bttnPrint").attr("title", this.ya(c, "Print"));
		jQuery(this.ca).find(".flexpaper_bttnSinglePage").attr("title", this.ya(c, "SinglePage"));
		jQuery(this.ca).find(".flexpaper_bttnTwoPage, .flexpaper_bttnBookView").attr("title", this.ya(c, H));
		jQuery(this.ca).find(".flexpaper_bttnThumbView").attr("title", this.ya(c, M));
		jQuery(this.ca).find(".flexpaper_bttnFitWidth").attr("title", this.ya(c, "FitWidth"));
		jQuery(this.ca).find(".flexpaper_bttnFitHeight").attr("title", this.ya(c, "FitHeight"));
		jQuery(this.ca).find(".flexpaper_bttnFitHeight").attr("title", this.ya(c, "FitPage"));
		jQuery(this.ca).find(".flexpaper_zoomSlider").attr("title", this.ya(c, "Scale"));
		jQuery(this.ca).find(".flexpaper_txtZoomFactor").attr("title", this.ya(c, "Scale"));
		jQuery(this.ca).find(".flexpaper_bttnFullScreen, .flexpaper_bttnFullscreen").attr("title", this.ya(c, "Fullscreen"));
		jQuery(this.ca).find(".flexpaper_bttnPrevPage").attr("title", this.ya(c, "PreviousPage"));
		jQuery(this.ca).find(".flexpaper_txtPageNumber").attr("title", this.ya(c, "CurrentPage"));
		jQuery(this.ca).find(".flexpaper_bttnPrevNext").attr("title", this.ya(c, "NextPage"));
		jQuery(this.ca).find(".flexpaper_txtSearch, .flexpaper_bttnTextSearch").attr("title", this.ya(c, "Search"));
		jQuery(this.ca).find(".flexpaper_bttnFind").attr("title", this.ya(c, "Search"));
		this.aa.ka.find(".flexpaper_bttnHighlight").find(".flexpaper_tbtextbutton").html(this.ya(c, "Highlight", "Highlight"));
		this.aa.ka.find(".flexpaper_bttnComment").find(".flexpaper_tbtextbutton").html(this.ya(c, "Comment", "Comment"));
		this.aa.ka.find(".flexpaper_bttnStrikeout").find(".flexpaper_tbtextbutton").html(this.ya(c, "Strikeout", "Strikeout"));
		this.aa.ka.find(".flexpaper_bttnDraw").find(".flexpaper_tbtextbutton").html(this.ya(c, "Draw", "Draw"));
		this.aa.ka.find(".flexpaper_bttnDelete").find(".flexpaper_tbtextbutton").html(this.ya(c, "Delete", "Delete"));
		this.aa.ka.find(".flexpaper_bttnShowHide").find(".flexpaper_tbtextbutton").html(this.ya(c, "ShowAnnotations", "Show Annotations"));
	};
	this.ya = function(c, e, f) {
		for (var k = 0; k < c.length; k++) {
			var g = c[k].split("=");
			if (g[0] == e) {
				return g[1];
			}
		}
		return f ? f : p;
	};
	this.bindEvents = function() {
		var c = this;
		jQuery(c.ca).find(".flexpaper_tbbutton_large, .flexpaper_tbbutton").each(function() {
			jQuery(this).data("minscreenwidth") && parseInt(jQuery(this).data("minscreenwidth")) > window.innerWidth && jQuery(this).hide();
		});
		0 == c.aa.ka.find(".flexpaper_printdialog").length && c.aa.ka.prepend("<div id='modal-print' class='modal-content flexpaper_printdialog' style='overflow:hidden;'><font style='color:#000000;font-size:11px'><b>" + c.ya(c.Ya, "Selectprintrange", "Select print range") + "</b></font><div style='width:98%;padding-top:5px;padding-left:5px;background-color:#ffffff;'><table border='0' style='margin-bottom:10px;'><tr><td><input type='radio' name='PrintRange' checked='checked' id='" + c.nh + "'/></td><td>" + c.ya(c.Ya, "All", "All") + "</td></tr><tr><td><input type='radio' name='PrintRange' id='" + c.oh + "'/></td><td>" + c.ya(c.Ya, "CurrentPage", "Current Page") + "</td></tr><tr><td><input type='radio' name='PrintRange' id='" + c.ph + "'/></td><td>" + c.ya(c.Ya, "Pages", "Pages") + "</td><td><input type='text' style='width:120px' id='" + c.Mf + "' /><td></tr><tr><td colspan='3'>" + c.ya(c.Ya, "Enterpagenumbers", "Enter page numbers and/or page ranges separated by commas. For example 1,3,5-12") + "</td></tr></table><a id='" + c.rh + "' class='flexpaper_printdialog_button'>" + c.ya(c.Ya, "Print", "Print") + "</a>&nbsp;&nbsp;<a class='flexpaper_printdialog_button' id='" + c.qh + "'>" + c.ya(c.Ya, "Cancel", "Cancel") + "</a><span id='" + c.Rh + "' style='padding-left:5px;'></span><div style='height:5px;display:block;margin-top:5px;'>&nbsp;</div></div></div>");
		jQuery("input:radio[name=PrintRange]:nth(0)").attr("checked", n);
		c.aa.config.Toolbar ? (jQuery(c.ca).find(".flexpaper_txtZoomFactor").bind("click", function() {
			if (!jQuery(this).hasClass("flexpaper_tbbutton_disabled")) {
				return v;
			}
		}), jQuery(c.ca).find(".flexpaper_currPageNum").bind("click", function() {
			jQuery(c.ca).find(".flexpaper_currPageNum").focus();
		}), jQuery(c.ca).find(".flexpaper_txtSearch").bind("click", function() {
			jQuery(c.ca).find(".flexpaper_txtSearch").focus();
			return v;
		}), jQuery(c.ca).find(".flexpaper_bttnFind").bind("click", function() {
			c.searchText(jQuery(c.ca).find(".flexpaper_txtSearch").val());
			jQuery(c.ca).find(".flexpaper_bttnFind").focus();
			return v;
		})) : (jQuery(c.ca).find(".flexpaper_bttnFitWidth").bind("click", function() {
			jQuery(this).hasClass("flexpaper_tbbutton_disabled") || (c.aa.fitwidth(), jQuery("#toolbar").trigger("onFitModeChanged", "Fit Width"));
		}), jQuery(c.ca).find(".flexpaper_bttnFitHeight").bind("click", function() {
			jQuery(this).hasClass("flexpaper_tbbutton_disabled") || (c.aa.fitheight(), jQuery("#toolbar").trigger("onFitModeChanged", "Fit Height"));
		}), jQuery(c.ca).find(".flexpaper_bttnTwoPage").bind("click", function() {
			jQuery(this).hasClass("flexpaper_tbbutton_disabled") || (c.aa.ub == D ? c.aa.switchMode(D) : c.aa.switchMode(H));
		}), jQuery(c.ca).find(".flexpaper_bttnSinglePage").bind("click", function() {
			(!c.aa.config.document.TouchInitViewMode || !c.aa.config.document.TouchInitViewMode == U) && eb.platform.touchonlydevice ? c.aa.switchMode(U, c.aa.getCurrPage()) : c.aa.switchMode(C, c.aa.getCurrPage() - 1);
		}), jQuery(c.ca).find(".flexpaper_bttnThumbView").bind("click", function() {
			c.aa.switchMode("Tile");
		}), jQuery(c.ca).find(".flexpaper_bttnPrint").bind("click", function() {
			eb.platform.touchonlydevice ? c.aa.printPaper("current") : (jQuery("#modal-print").css("background-color", "#dedede"), jQuery("#modal-print").smodal({
				minHeight: 255,
				appendTo: c.aa.ka
			}), jQuery("#modal-print").parent().css("background-color", "#dedede"));
		}), jQuery(c.ca).find(".flexpaper_bttnDownload").bind("click", function() {
			window.zine ? (window.open(FLEXPAPER.Tg(c.document.PDFFile, c.aa.getCurrPage()), "windowname3", p), 0 < c.document.PDFFile.indexOf("[*,") && -1 == c.document.PDFFile.indexOf("[*,2,true]") && window.open(FLEXPAPER.Tg(c.document.PDFFile, c.aa.getCurrPage() - 1), "windowname4", p)) : window.open(FLEXPAPER.Tg(c.document.PDFFile, c.aa.getCurrPage()), "windowname4", p);
			return v;
		}), jQuery(c.ca).find(".flexpaper_bttnPrevPage").bind("click", function() {
			c.aa.previous();
			return v;
		}), jQuery(c.ca).find(".flexpaper_bttnPrevNext").bind("click", function() {
			c.aa.next();
			return v;
		}), jQuery(c.ca).find(".flexpaper_bttnZoomIn").bind("click", function() {
			c.aa.ba == H || c.aa.ba == D ? c.aa.pages.ad() : (c.aa.ba == C || c.aa.ba == U) && c.aa.ZoomIn();
		}), jQuery(c.ca).find(".flexpaper_bttnZoomOut").bind("click", function() {
			c.aa.ba == H || c.aa.ba == D ? c.aa.pages.kc() : (c.aa.ba == C || c.aa.ba == U) && c.aa.ZoomOut();
		}), jQuery(c.ca).find(".flexpaper_txtZoomFactor").bind("click", function() {
			if (!jQuery(this).hasClass("flexpaper_tbbutton_disabled")) {
				return jQuery(c.ca).find(".flexpaper_txtZoomFactor").focus(), v;
			}
		}), jQuery(c.ca).find(".flexpaper_currPageNum").bind("click", function() {
			jQuery(c.ca).find(".flexpaper_currPageNum").focus();
		}), jQuery(c.ca).find(".flexpaper_txtSearch").bind("click", function() {
			jQuery(c.ca).find(".flexpaper_txtSearch").focus();
			return v;
		}), jQuery(c.ca).find(".flexpaper_bttnFullScreen, .flexpaper_bttnFullscreen").bind("click", function() {
			c.aa.openFullScreen();
		}), jQuery(c.ca).find(".flexpaper_bttnFind").bind("click", function() {
			c.searchText(jQuery(c.ca).find(".flexpaper_txtSearch").val());
			jQuery(c.ca).find(".flexpaper_bttnFind").focus();
			return v;
		}), jQuery(c.ca).find(".flexpaper_bttnTextSelect").bind("click", function() {
			c.aa.gd = "flexpaper_selected_default";
			jQuery(c.ca).find(".flexpaper_bttnTextSelect").addClass("flexpaper_tbbutton_pressed");
			jQuery(c.ca).find(".flexpaper_bttnHand").removeClass("flexpaper_tbbutton_pressed");
			c.aa.setCurrentCursor("TextSelectorCursor");
		}), jQuery(c.ca).find(".flexpaper_bttnHand").bind("click", function() {
			jQuery(c.ca).find(".flexpaper_bttnHand").addClass("flexpaper_tbbutton_pressed");
			jQuery(c.ca).find(".flexpaper_bttnTextSelect").removeClass("flexpaper_tbbutton_pressed");
			c.aa.setCurrentCursor("ArrowCursor");
		}), jQuery(c.ca).find(".flexpaper_bttnRotate").bind("click", function() {
			c.aa.rotate();
		}));
		jQuery("#" + c.Mf).bind("keydown", function() {
			jQuery(this).focus();
		});
		jQuery(c.ca).find(".flexpaper_currPageNum, .flexpaper_txtPageNumber").bind("keydown", function(e) {
			if (!jQuery(this).hasClass("flexpaper_tbbutton_disabled")) {
				if ("13" != e.keyCode) {
					return;
				}
				c.gotoPage(this);
			}
			return v;
		});
		jQuery(c.ca).find(".flexpaper_txtSearch").bind("keydown", function(e) {
			if ("13" == e.keyCode) {
				return c.searchText(jQuery(c.ca).find(".flexpaper_txtSearch").val()), v;
			}
		});
		jQuery(c.ca).bind("onZoomFactorChanged", function(e, f) {
			var k = Math.round(100 * (f.Ld / c.aa.document.MaxZoomSize) * c.aa.document.MaxZoomSize) + "%";
			jQuery(c.ca).find(".flexpaper_txtZoomFactor").val(k);
			c.Ld != f.Ld && (c.Ld = f.Ld, jQuery(c.aa).trigger("onScaleChanged", f.Ld));
		});
		jQuery(c.ga).bind("onDocumentLoaded", function(e, f) {
			2 > f ? jQuery(c.ca).find(".flexpaper_bttnTwoPage").addClass("flexpaper_tbbutton_disabled") : jQuery(c.ca).find(".flexpaper_bttnTwoPage").removeClass("flexpaper_tbbutton_disabled");
		});
		jQuery(c.ca).bind("onCursorChanged", function(e, f) {
			"TextSelectorCursor" == f && (jQuery(c.ca).find(".flexpaper_bttnTextSelect").addClass("flexpaper_tbbutton_pressed"), jQuery(c.ca).find(".flexpaper_bttnHand").removeClass("flexpaper_tbbutton_pressed"));
			"ArrowCursor" == f && (jQuery(c.ca).find(".flexpaper_bttnHand").addClass("flexpaper_tbbutton_pressed"), jQuery(c.ca).find(".flexpaper_bttnTextSelect").removeClass("flexpaper_tbbutton_pressed"));
		});
		jQuery(c.ca).bind("onFitModeChanged", function(e, f) {
			jQuery(".flexpaper_fitmode").each(function() {
				jQuery(this).removeClass("flexpaper_tbbutton_pressed");
			});
			"FitHeight" == f && jQuery(c.ca).find(".flexpaper_bttnFitHeight").addClass("flexpaper_tbbutton_pressed");
			"FitWidth" == f && jQuery(c.ca).find(".flexpaper_bttnFitWidth").addClass("flexpaper_tbbutton_pressed");
		});
		jQuery(c.ca).bind("onProgressChanged", function(e, f) {
			jQuery("#lblPercent").html(100 * f);
			1 == f && jQuery(c.ca).find(".flexpaper_bttnPercent").hide();
		});
		jQuery(c.ca).bind("onViewModeChanged", function() {
			jQuery(".flexpaper_viewmode").each(function() {
				jQuery(this).removeClass("flexpaper_tbbutton_pressed");
			});
			if ("Portrait" == c.aa.ba || "SinglePage" == c.aa.ba) {
				jQuery(c.ca).find(".flexpaper_bttnSinglePage").addClass("flexpaper_tbbutton_pressed"), jQuery(c.ca).find(".flexpaper_bttnFitWidth").removeClass("flexpaper_tbbutton_disabled"), jQuery(c.ca).find(".flexpaper_bttnFitHeight").removeClass("flexpaper_tbbutton_disabled"), jQuery(c.ca).find(".flexpaper_bttnPrevPage").removeClass("flexpaper_tbbutton_disabled"), jQuery(c.ca).find(".flexpaper_bttnPrevNext").removeClass("flexpaper_tbbutton_disabled"), jQuery(c.ca).find(".flexpaper_bttnTextSelect").removeClass("flexpaper_tbbutton_disabled"), eb.platform.touchdevice || (jQuery(c.ca).find(".flexpaper_zoomSlider").removeClass("flexpaper_tbbutton_disabled"), jQuery(c.ca).find(".flexpaper_txtZoomFactor").removeClass("flexpaper_tbbutton_disabled"), c.aa.toolbar.wb && c.aa.toolbar.wb.enable());
			}
			if ("TwoPage" == c.aa.ba || "BookView" == c.aa.ba) {
				jQuery(c.ca).find(".flexpaper_bttnTwoPage").addClass("flexpaper_tbbutton_pressed");
				jQuery(c.ca).find(".flexpaper_bttnFitWidth").addClass("flexpaper_tbbutton_disabled");
				jQuery(c.ca).find(".flexpaper_bttnFitHeight").addClass("flexpaper_tbbutton_disabled");
				jQuery(c.ca).find(".flexpaper_bttnPrevPage").removeClass("flexpaper_tbbutton_disabled");
				jQuery(c.ca).find(".flexpaper_bttnPrevNext").removeClass("flexpaper_tbbutton_disabled");
				jQuery(c.ca).find(".flexpaper_bttnTextSelect").removeClass("flexpaper_tbbutton_disabled");
				if (eb.platform.touchdevice || eb.browser.msie) {
					jQuery(c.ca).find(".flexpaper_zoomSlider").addClass("flexpaper_tbbutton_disabled"), jQuery(c.ca).find(".flexpaper_txtZoomFactor").addClass("flexpaper_tbbutton_disabled"), c.aa.toolbar.wb && c.aa.toolbar.wb.disable();
				}!eb.platform.touchdevice && !eb.browser.msie && (jQuery(c.ca).find(".flexpaper_zoomSlider").removeClass("flexpaper_tbbutton_disabled"), jQuery(c.ca).find(".flexpaper_txtZoomFactor").removeClass("flexpaper_tbbutton_disabled"), c.aa.toolbar.wb && c.aa.toolbar.wb.enable());
			}
			"ThumbView" == c.aa.ba && (jQuery(c.ca).find(".flexpaper_bttnThumbView").addClass("flexpaper_tbbutton_pressed"), jQuery(c.ca).find(".flexpaper_bttnFitWidth").addClass("flexpaper_tbbutton_disabled"), jQuery(c.ca).find(".flexpaper_bttnFitHeight").addClass("flexpaper_tbbutton_disabled"), jQuery(c.ca).find(".flexpaper_bttnPrevPage").addClass("flexpaper_tbbutton_disabled"), jQuery(c.ca).find(".flexpaper_bttnPrevNext").addClass("flexpaper_tbbutton_disabled"), jQuery(c.ca).find(".flexpaper_bttnTextSelect").addClass("flexpaper_tbbutton_disabled"), eb.platform.touchdevice || (jQuery(c.ca).find(".flexpaper_zoomSlider").addClass("flexpaper_tbbutton_disabled"), jQuery(c.ca).find(".flexpaper_txtZoomFactor").addClass("flexpaper_tbbutton_disabled"), c.aa.toolbar.wb && c.aa.toolbar.wb.disable()));
		});
		jQuery(c.ca).bind("onFullscreenChanged", function(e, f) {
			f ? jQuery(c.ca).find(".flexpaper_bttnFullscreen").addClass("flexpaper_tbbutton_disabled") : jQuery(c.ca).find(".flexpaper_bttnFullscreen").removeClass("flexpaper_tbbutton_disabled");
		});
		jQuery(c.ca).bind("onScaleChanged", function(e, f) {
			c.wb && c.wb.setValue(f, n);
		});
		jQuery("#" + c.qh).bind("click", function(c) {
			jQuery.smodal.close();
			c.stopImmediatePropagation();
			return v;
		});
		jQuery("#" + c.rh).bind("click", function() {
			var e = "";
			jQuery("#" + c.nh).is(":checked") && (c.aa.printPaper("all"), e = "1-" + c.aa.renderer.getNumPages());
			jQuery("#" + c.oh).is(":checked") && (c.aa.printPaper("current"), e = jQuery(c.ca).find(".flexpaper_txtPageNumber").val());
			jQuery("#" + c.ph).is(":checked") && (e = jQuery("#" + c.Mf).val(), c.aa.printPaper(e));
			jQuery(this).html("Please wait");
			window.onPrintRenderingProgress = function(e) {
				jQuery("#" + c.Rh).html("Processing page:" + e);
			};
			window.onPrintRenderingCompleted = function() {
				jQuery.smodal.close();
				c.ga.trigger("onDocumentPrinted", e);
			};
			return v;
		});
		c.Ml();
	};
	this.tj = function(c, e) {
		var f = this;
		if (0 != jQuery(f.ca).find(".flexpaper_zoomSlider").length && f.wb == p) {
			f = this;
			this.Yd = c;
			this.Xd = e;
			if (window.zine) {
				var k = {
					ge: 0,
					Fb: f.aa.ga.width() / 2,
					Xb: f.aa.ga.height() / 2
				};
				f.wb = new Slider(jQuery(f.ca).find(".flexpaper_zoomSlider").get(0), {
					callback: function(c) {
						c * f.aa.document.MaxZoomSize >= f.aa.document.MinZoomSize && c <= f.aa.document.MaxZoomSize ? f.aa.fb(f.aa.document.MaxZoomSize * c, k) : c * f.aa.document.MaxZoomSize < f.aa.document.MinZoomSize ? f.aa.fb(f.aa.document.MinZoomSize, k) : c > f.aa.document.MaxZoomSize && f.aa.fb(f.aa.document.MaxZoomSize, k);
					},
					animation_callback: function(c) {
						c * f.aa.document.MaxZoomSize >= f.aa.document.MinZoomSize && c <= f.aa.document.MaxZoomSize ? f.aa.fb(f.aa.document.MaxZoomSize * c, k) : c * f.aa.document.MaxZoomSize < f.aa.document.MinZoomSize ? f.aa.fb(f.aa.document.MinZoomSize, k) : c > f.aa.document.MaxZoomSize && f.aa.fb(f.aa.document.MaxZoomSize, k);
					},
					snapping: v
				});
			} else {
				f.wb = new Slider(jQuery(f.ca).find(".flexpaper_zoomSlider").get(0), {
					callback: function(c) {
						c * f.aa.document.MaxZoomSize >= f.Yd && c <= f.Xd ? f.aa.fb(f.aa.document.MaxZoomSize * c) : c * f.aa.document.MaxZoomSize < f.Yd ? f.aa.fb(f.Yd) : c > f.Xd && f.aa.fb(f.Xd);
					},
					animation_callback: function(c) {
						c * f.aa.document.MaxZoomSize >= f.Yd && c <= f.Xd ? f.aa.fb(f.aa.document.MaxZoomSize * c) : c * f.aa.document.MaxZoomSize < f.Yd ? f.aa.fb(f.Yd) : c > f.Xd && f.aa.fb(f.Xd);
					},
					snapping: v
				});
			}
			jQuery(f.ca).find(".flexpaper_txtZoomFactor").bind("keypress", function(c) {
				if (!jQuery(this).hasClass("flexpaper_tbbutton_disabled") && 13 == c.keyCode) {
					try {
						var d = {
								ge: 0,
								Fb: f.aa.ga.width() / 2,
								Xb: f.aa.ga.height() / 2
							},
							e = jQuery(f.ca).find(".flexpaper_txtZoomFactor").val().replace("%", "") / 100;
						f.aa.Zoom(e, d);
					} catch (k) {}
					return v;
				}
			});
		}
	};
	this.Nl = function(c) {
		jQuery(c).val() > this.document.numPages && jQuery(c).val(this.document.numPages);
		(1 > jQuery(c).val() || isNaN(jQuery(c).val())) && jQuery(c).val(1);
	};
	this.Ll = function(c) {
		this.aa.ba == H ? "1" == c ? jQuery(this.ca).find(".flexpaper_txtPageNumber").val("1-2") : parseInt(c) <= this.document.numPages && 0 == this.document.numPages % 2 || parseInt(c) < this.document.numPages && 0 != this.document.numPages % 2 ? jQuery(this.ca).find(".flexpaper_txtPageNumber").val(c + "-" + (c + 1)) : jQuery(this.ca).find(".flexpaper_txtPageNumber").val(this.document.numPages) : this.aa.ba == D || this.aa.ba == L ? "1" == c && !eb.platform.Vd ? jQuery(this.ca).find(".flexpaper_txtPageNumber").val("1") : parseInt(c) + 1 <= this.document.numPages && (!this.aa.ha || !this.aa.ha.hb) ? (0 != parseInt(c) % 2 && (c -= 1), jQuery(this.ca).find(".flexpaper_txtPageNumber").val(c + "-" + (c + 1))) : jQuery(this.ca).find(".flexpaper_txtPageNumber").val(c) : "0" != c && jQuery(this.ca).find(".flexpaper_txtPageNumber").val(c);
	};
	this.Ml = function() {
		jQuery(this.ca).find(".flexpaper_lblTotalPages").html(" / " + this.document.numPages);
	};
	this.gotoPage = function(c) {
		0 <= jQuery(c).val().indexOf("-") && this.aa.ba == H ? (c = jQuery(c).val().split("-"), !isNaN(c[0]) && !isNaN(c[1]) && (0 == parseInt(c[0]) % 2 ? this.aa.gotoPage(parseInt(c[0]) - 1) : this.aa.gotoPage(parseInt(c[0])))) : isNaN(jQuery(c).val()) || (this.Nl(c), this.aa.gotoPage(jQuery(c).val()));
	};
	this.searchText = function(c) {
		this.aa.searchText(c);
	};
}
window.addCSSRule = function(g, c, d) {
	for (var e = p, f = 0; f < document.styleSheets.length; f++) {
		var k = document.styleSheets[f],
			l = k.cssRules || k.rules,
			u = g.toLowerCase();
		if (l != p) {
			e == p && (e = document.styleSheets[f]);
			for (var m = 0, q = l.length; m < q; m++) {
				if (l[m].selectorText && l[m].selectorText.toLowerCase() == u) {
					if (d != p) {
						l[m].style[c] = d;
						return;
					}
					k.deleteRule ? k.deleteRule(m) : k.removeRule ? k.removeRule(m) : l[m].style.cssText = "";
				}
			}
		}
	}
	k = e || {};
	k.insertRule ? (l = k.cssRules || k.rules, k.insertRule(g + "{ " + c + ":" + d + "; }", l.length)) : k.addRule && k.addRule(g, c + ":" + d + ";", 0);
};
window.FlexPaperViewer_Zine = function(g, c, d) {
	this.aa = c;
	this.ga = d;
	this.toolbar = g;
	this.ra = "FlipView";
	this.mm = this.toolbar.Ja + "_bttnZoomIn";
	this.nm = this.toolbar.Ja + "_bttnZoomOut";
	this.lm = this.toolbar.Ja + "_bttnBookView";
	this.kn = this.toolbar.Ja + "_txtPageNumber";
	this.zj = this.toolbar.Ja + "_barPrint";
	this.Bj = this.toolbar.Ja + "_barViewMode";
	this.yj = this.toolbar.Ja + "_barNavTools";
	this.xj = this.toolbar.Ja + "_barCursorTools";
	this.Aj = this.toolbar.Ja + "_barSearchTools";
	this.qa = "data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///yH5BAEAAAAALAAAAAABAAEAAAIBRAA7";
	this.Nf = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACcAAAAnCAAAAACpyA7pAAAAAXNSR0IArs4c6QAAAAlwSFlzAAALEwAACxMBAJqcGAAAAAd0SU1FB9wGGgchEmOlRoEAAAFUSURBVDjLrZS9SgNREIW/m531JyGbQFAQJE+w4EuYWvQd7C0sAjYpfQcfwsJSXyJgbZFKEhTUuIZkd8Yimx/Dboyytzz345yZuZdxF2x0SpthiBbsZ3/gXnofuYBXbjZSrtevHeRycfQ0bIIo76+HlZ08zDSoPgcBYgz2Ai/t+mYZOQfAbXnJoIoYVFzmcGaiq0SGKL6XPcO56vmKGNgvnGFTztZzTDlNsltdyGqIEec88UKODdEfATm5irBJLoihClTaIaerfrc8Xn/O60OBdgjKyapn2L6a95soEJJdZ6hAYkjMyE+1u6wqv4BRXPB/to25onP/43e8evmw5Jd+vm6Oz1Q3ExAHdDpHOO6XkRbQ7ThAQIxdczC8zDBrpallw53h9731PST7E0pmWsetoRx1NRNjUi6/jfL3i1+zCASI/MZ2LqeTaDKb33hc2J4sep9+A+KGjvNJJ1I+AAAAAElFTkSuQmCC";
	this.Of = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACcAAAAnCAAAAACpyA7pAAAAAXNSR0IArs4c6QAAAAlwSFlzAAALEwAACxMBAJqcGAAAAAd0SU1FB9wGGggFBG3FVUsAAAFTSURBVDjLrZQxSwNBEIXfbuaSUxKUNDYKRmJhZXMIgv/FIm0K/0kau/wdqxQeaGEQksJW0CJ4SC6ZZ5G9eIZbc8pdOfftm/d2ljE3KPXZchhEK9bjH7jX+8TfsH7addzLRA683HI+ZhcQxdukUQ+8nIbhdL8NIR6D0DqXd3niCgBgxOr4EkKwYQrDZEXTmBGiqBVjaw6mpqu8xXet+SPC3EGPnuO4lSMhhHpG/F1WQrRMX4UA3KpHwJJKks1hHG8YJeN42CRJJbO8gwggzjc1o0HvZ94IxT4jurwLpDVXeyhymQJIFxW/Z5bmqu77H72zzZ9POT03rJFHZ+RGKG4l9G8v8gKZ/KjvloYQO0sAs+sCscxISAhw8my8DlddO4Alw441vyQ1ONwlhUjbremHf7/I0V4CCIAkOG6teyxSAlYCAAgMkHyaJLu/Od6r2pNV79MvlFCWQTKpHw8AAAAASUVORK5CYII%3D";
	this.Hf = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACcAAAAnCAAAAACpyA7pAAAAAXNSR0IArs4c6QAAAAlwSFlzAAALEwAACxMBAJqcGAAAAAd0SU1FB9wGGgcmKZ3vOWIAAAG3SURBVDjLrZS9bhNBFIW/uzOLwVacSIYCCUVCyivwAkgGJ31cpMwT8A6UlKnSpKTgARBPkCK8QZCIlAqRBPGXxbF37qFYO8aWNk6QVyvNnNlP52juzlx7xa2e7HYY0ZfspztwF6e/aoHQXO+MudOvq49rubL4/HsdovPz25PW/TpM3l750m4Txdmjdqjftd0L6WyFKGjZjcWxViGikwcHE/0eMmHsHiBMxod3mCDkTiYhdyXf7h0PDYDK3YbHvW1PchfSmEve3zzfvwQz8Gq43D/f7Hu65jyllHa2OLpqgASpGhpXR2ztpJSSS1GUDrvPP318nyJYlWtAvHj7/Vk3HEApMnfcvXuydxg3AkjIhQRhIx7unXTdHfcInoCnb/IMZIAlA1B4jY8iCRyicAeFMC3YtJpZAzm4iKrWZTI0w8mQqfpKFGn+b/i8SiKWDPI57s+8GpRLPs+acPbPO9XYWOuuuZN000SZZnKv/QyrMmxm9p/7WMxBNHg5cyFezCiIEMUD2QK3psjg4aJW5B3IJF/jJkNjrTr3o2bzx6C+v+SrKiACRd5p1IeOitGkfsPh0vrksvvpX4Z15Dxt627DAAAAAElFTkSuQmCC";
	this.Ne = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACcAAAAnCAAAAACpyA7pAAAAAXNSR0IArs4c6QAAAAlwSFlzAAALEwAACxMBAJqcGAAAAAd0SU1FB9wGGggfBarvnwYAAAG+SURBVDjLrZQ/axRRFMV/9+2bnUkgKGlsFIxEECWfwMaPIJhqk0+QbUxlkyqdrWBrp/gZ0sTGVgJptkkKmyASLRaHdWf2Hou3f9yVzSaylwf33XmHe+47vDn2kmtFuB6M6Evupxvgvn8p5xM2H24OcV/P4p25uEG/o02Izo+zvJnNxXlRnN9eJ0inWRE1NywWqx0pCuV25WUs74roNEwQnHYLD8J4+hlhHvjwluBgDSdI4E7te62TXlIzSR96J609r3EHKUhIGqi9c3HYBTNQSt3Di522BpISTpK0v8txvwAJlFLRP2Z3f3gehTu8en766f2gCZZ4DWh+e3P57EXjNbgI7kja7hwc5VsR0hhIELfyo4POtiTcI8iBRx/zADLA3ADUeIf/znAQROECxTgRbKJmWEECFzHNjUw2AoySIVM6JaZZpkKzlUSsqRozuGq2quolv2eNcPbXmtTYsNZNeUfs6SVqvBvzjvsZljhsavef91iMS5bwZOrz439NI0grC9sVUoAHi6i1AUEqNoJd9Vtyd1WKolpfO/8131/ivVslRKDM7q+NOepKEGIGkBmUPStH+vX5uSyfXLaf/gE6n/uTJg/UHAAAAABJRU5ErkJggg%3D%3D";
	this.Se = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACcAAAAnCAAAAACpyA7pAAAAAXNSR0IArs4c6QAAAAlwSFlzAAALEwAACxMBAJqcGAAAAAd0SU1FB9wGGgcnEaz2sL0AAAGlSURBVDjLrZRNjtNAEIW/ssvDkGgyIwUWSGhWHIEj8Cf2bDgFV+AMLGHBCgmJA3ABdpyBkWaFmAHxGyLHrsfC7dgmsQhSvLG763O/qtddbU/Y6cl2w/DY83r6D+7z+Y9RIJ+czhN3/un4xihXLT78PAUPvn+5OT0cwxSzo4+zGS4urs/y8artIK8vjnDB1BrsBZaqMr190w2mC+FB0a5mIgXLswf2eg3mRZBJKJpHhgkz49fzy/uPom7nkfockkASB+V7e/g4epyLqLukaaSKy1dfb9+xl2k6RCZV7X+gBrP8lr97dna3DVSSB3SmmExgkT+1KIsuEDh93eQtQHbYBQJcRPQI9d4WXX6uTnftX+OPOl3hou7nN/hqA7XwimWxsfkYgH6n8bIanGe1NZhpDW87z4YhawgbCgw4WapUqZCOG/aREia03pzUbxoKN3qG0ZeWtval7diXsg2jtnK2aaiD21++oJRnG3BwcbWVuTfWmxORwbV/XUUxh0yKk20F9pI9CcnFajL5thy/X4pjLcCBRTG/Mi66Wqxa/8pyb/fkvu/TP0a/9eMEsgteAAAAAElFTkSuQmCC";
	this.Pf = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACcAAAAnCAAAAACpyA7pAAAAAXNSR0IArs4c6QAAAAlwSFlzAAALEwAACxMBAJqcGAAAAAd0SU1FB9wGGggfGb7uw0kAAAGtSURBVDjLrZS/bhNBEIe/Wc/5DksRKA0NSASFBvEQvAM0IJ4gFfRUtJSJ0tHyEFQU1DQ0bpKCBgkEFBEny2fvj+LW98f2gSN5pdPt7nya2flpZuwlO62wG4bHPfvTNbgfn8vhgOMHx4n7euG3B7nlfKpj8Mivi3ycDXKxKC5vHRKkL1nhGlzmxWQquVBudTKfSBsFvT8nJMksvxIeGSUrpvrDZtPndrZswFEkSBDrJcOEmXH15tuzk7hI9yAFidVTkASSyOcf7cUrdQwu1Ept1Pv8++nPx0/C23QtEaQYO/5r3B+NP7yePm0skkfo+JMJLI7eWZyNW0PEQeslI4AwIcb2wkVUh1Dnv9KLKFxt3FY/TJjauGItX/V2avP1BdWIjQcagKp0rha9em5cmKmBt9WzYchqwvoBepwsZaqUSMv1+0gJE6KbH3W9dALX8QyjG1ra2pe2Y1/KNoTaytmmoN4dCUkXtKZLABc3lun4cKg3CxHg/v9Gh44gSMVRsH9Qxp2J5KI6PLj8Mzxf/O7NEhwos3sHTYxFJQieAWQG5czKlX5zfu9rTu57nv4FFIsPySkiwzoAAAAASUVORK5CYII%3D";
	this.Te = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACcAAAAnCAAAAACpyA7pAAAAAXNSR0IArs4c6QAAAAlwSFlzAAALEwAACxMBAJqcGAAAAAd0SU1FB9wGGgcoGry8dfoAAAFASURBVDjLrZRNSgNBEEZfJzUqCZkERhdCCB7GC+jCrXgDb+QRvIBnEQkuxKAQI2NIeqpcTCI9Mp3JQHrzaPj6q5/uLnfPXquznwzRA/tZC93HdBEVdHuTbKObvg/PozqfP39PQJSvz3H/JCYzTQdvaYoYs7O0G6/aHXWL2QAx6LudzXH93BAlKd0eALiroiwlUcTAAjutgWGlbtNDj6D/sVGKoUWQTFEHNcTw21NSRqoCwBuif7tofqC4W16HTZc7HyOGlqceAbiqIsxvj7iGGMV2F+1LYYhnmQR+P3VYeiR8i3Vo9Z4Nd8PLoEm2uAjcnwC4rKJ13PBfel+Dln6hLt4XQ0Bc+BnqIOCumeMaorqUDpw2jSLNoGOmo52GjpGaibHu9ebL+HxJhpaXVeVJdhwPus7X2/6tVgebk4eep79dEZnAuEZ32QAAAABJRU5ErkJggg%3D%3D";
	this.Qf = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACcAAAAnCAAAAACpyA7pAAAAAXNSR0IArs4c6QAAAAlwSFlzAAALEwAACxMBAJqcGAAAAAd0SU1FB9wGGgggAxtSEA8AAAE0SURBVDjLrZQxT8MwEIW/uJc2VKpAXVhAoqgMbLDyq/iVjAiJgS7twIoEAyJCTerHYNokyGlTVC+fJT/fuzvZl9zTabluMswfOJ720L095u2G/avpr+51bqetutVypimY530+6KetOp9li5MxTnpOM1PrSiwbziQTGiRbi0kGn8I8vSB7AOCuiSDs+VBvrdc+BoQJ1q4lhv6i0qmenaIQJvw6ugWnJgC8MF/5tsbDY6Bw65YINnITPtx6AuCmicpXXXyb9bb2RcJKil4tXhFFidXfYgx7vWfVdNcxVLrN/iWcN7G3b/1flmUE/65jW1+E6zISHJg4Wu3qSyYcXO5KURNwUjZxybZvydlQMlGMR4uv9tzs/DgPVeXpxWjjURYCZylAmkD+neTr/i35ONScPPQ8/QFgdrQzzjNS3QAAAABJRU5ErkJggg%3D%3D";
	this.Ue = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACcAAAAnCAQAAAAmqpm+AAAAAmJLR0QA/4ePzL8AAAAJcEhZcwAACxMAAAsTAQCanBgAAAAHdElNRQfdBRUTLyj0mgAyAAAC8ElEQVRIx83Wz2ucZRAH8M+72c2mu91NmibFSEgaGy1SrRdFFFIJ9uDBk6KCN6EHD0qLB8GDFwXrQUEK7UnQP0DwUD23RVS8WG2EKrShDSpNYhLaNJtNNvs+HnbzY7fvLmmo2BneyzPzft+Z9zvPzEQngnsoKfdU0rH7Obrw38DNmbK4A4AOOUP2NsJNmdFtYAdwa0om3Ta0ScUt8wbldd01WBArKrihqLge3ax+RR12wnKkU4eqWYXNZPMiOy+ZSF5JWE82kxhZSqfH7Ddg0YwJ01bbEJIRb0YX7oDLOuo5nZg34HFHXXHeby3/Ye3ZgAtNX3vTiAVfm1SWlnPEU4ad800bWupwsWqT6W0j/vC52KCqorIv/eC4cVdbRBgLSAXBmrhBn/GwaaeMeNaoT72oYtjvPpPxsnSTd03XBEEqFtNgyHgSpzyCX2TRbcpVscvO2ufRRLgaRko92U1NO+hn01ZVZC3h9obtopKxBu91jTcvWdzAa0HkV3s8pMuKI9jtBbuUfWvOPw4lVmi8ldmtDg/gusixDcZGjYKzyspN3gnMVhscFgT9/vajPUqWjPlOTt6CuN4gk+CqNbg1lGW2GK6JjDrvKxNirxtTdFwa9Or1p+UEuLK15G5cNul5ObFRrCCug3FYr3PtmnvzfWDZBWlvmbRbpIeN5ljwGr5veSuC6NXANYUGQ94HBl1wpuG0x0f6RGa9o3wH2KL9rUbPktNWjHvfkF2ysorGndGPoM/Hulu1qlcC15uigwe94QmRvyzggC6RgEgQuewTt5qiG24HR9ZBTzskI+WGn8x5F0GEYMKHCXBtBuOKSy41nLznpKjefw8nlnECs63lipOW6y+uJDKbgrRom3rRaRWR4IsmS60yo5cCN6knsR0pKCqbb8gqiGqDEfrM6Ng23GLCthDbp7L+72I9dxVf81ikRywINWYrcnJuJtT6dnaUjG5BqdY+a4clGXtldwAXqyipNG9Qq22G8v+2Lt7f2+e/O1kvzGyGcjEAAAAASUVORK5CYII%3D";
	this.Rf = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACcAAAAnCAQAAAAmqpm+AAAAAmJLR0QA/4ePzL8AAAAJcEhZcwAACxMAAAsTAQCanBgAAAAHdElNRQfdBRUTOjTXLrppAAAC50lEQVRIx83WT2hcVRTH8c+bvMnMxMSkKU2Fqv1DhQ7aItJWRZEgiAUrKqJuXAZRN2ahRRfd+A+6EtFFF4roTrC4K0pBFDQRIsVakSht1FoUUtoG2oyTTPKui3kmmcmbIQ4Vcu/unvu+75z7O/fcE40G13DkXNMRJ9azd+H/wV1wUqWj8LrdYmcj7pyzYps7wC2aNymkwDjBJWcVdMt3gEsUFU0ZMIhcEJyWVxQLHcxIrKjHpCDUgw0KIp2LEim4IvwbbFcmLKfoLmXbzPjDuHPm2gC7JCuVbU7nkic9poBpW93tKT/41LdtfAzLuGbfYm8om/axH1Xk9XnE/XY55sO2uFz2Ab+p7HvP+UKvoiGJIw7p9rh9bYXJBUHSNA/Y47zD9jhg2CeeUXOb0w7p9qz8qv31GQS5RELDHwqG8bJbLRpTQL8zTqk56SNb7M30i0RSLwGN/hXc7mt/mjOvxyyuLtm+cdXBFr4tXbKkQYoBkTGb3Ktozn3o9bySqndN+8vezAxNWim7FWd0GVlSbGd6I9/xt2pGHjQlSmjYcFGwxe/GbVBx0QNOGHSdy4KcXAtcnREvoKZrhWFKZLfPHfWdxEsY8rQF0G/Ir2oZuJqF7Gpc9bOH9UqUMYckhbHfJsfbVb+wyvVZx+UdNul6kQFsTC39RnCi5a0IWTg+M+UeLxgXvKrsQbDRB3pxVKk1LstwxeuqHvK2HXqUlAw46JgbEGz2vg2tKssTgQnFVYabjbpT5DeXsEspLWKRIHLKK2aaTnxfOxxFuw27Q7ec87407QiCCMGE0Qxcm4exasJEw8qI90RpudzfukCtdfzkRZX0w2prKdbeCox5zbxI8FZmOxEHlCyuGfiVRw2ouLDqpANi2OGX9EzWMmaaNK0Hun35VhRtl/sPwOZXjBv1LL+zNYP6TJntqEeJ3aQ/7W/i+mJF3jZ9GUEsqKXa58Qr2o58Gk1FVbTULC3l3Twur7d2cX13n/8ANgFb4QoS+/QAAAAASUVORK5CYII%3D";
	this.Ve = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACcAAAAnCAQAAAAmqpm+AAAAAmJLR0QA/4ePzL8AAAAJcEhZcwAACxMAAAsTAQCanBgAAAAHdElNRQfdBRUTMCbeeCOrAAAC4ElEQVRIx83Wz2tcVRQH8M+bzGTixJmkaSNGSmpqtUi1LlREQSXYrRtFXIrgogtFV4ILV11UwYUU6krQP0BwUV23Xai4abQRqlCDDVqa1CS00cmbzMy7LuZHZqZvxnao2Ht4MLxz5vu+937PPedE7wS3cWXc1pVN3Mnswn8Dt2bZ5hAAIwpm7e6GW7ZqwswQcDVlS/4yuyPFdev2Gjd2y2BBoqToipJSi91V00pGDKNyZNSIuquKO5sdFxk+ZSLjykJrs7lUZhmjHnG/GZtWLVqxPUCQnGSHXbgBLu+I541i3YxHHXHRGT/1PcPG04YLPV87as6GLy2JZRU850n7nPbVAFmacIl6j+stc37xqcRedSWxz33rbfN+7cMwEZAJgpqky572oBUnzHlG1oQpVfv97GM5L8v2RDesJgitEpB0ndoTOOEh/KCo4rJ1cMEpL3rYQh9+zRKQqHdY1kHnrNhWlbeprNr2LSh7tiu6ZcnOJUu62BVFfrTLfmMqHZxjX1vzp0OpGZp0KtsZcC8uibzRVixq/jolFvdEpyhb7wrYEEy77Du7mrlOomijfTppcPUGXA2xXIfjN5EDzvjCokRO1ai4WWenTPndVgpcrJZejWNLXlCQONBkst0OO2zK6UHFvfc+sOWsrDctuVskkmmfXdGr+KbvrQhpcJy17HGvOddM8UbEpA8VcKxPXQxCeuv520kV89436y55eSXzPjGNYI8PTPQrVa8ELine4LjP6x4T+cMGHjAmEhAJIhd85HpX/KZ9g+DIO+gph+RkXPG9Ne+2szBYdCwFbkBjrDjvfNeb9xxvyhI5nJrGqVL0Wxcdt9X8Y6W/FFnRTdqCk6oiwWc9nmyD9UuBa7Rz699XUUlsvWtXQdRojLDHqpGbhttMmRYS96i2zi4xeUv8etsik5JGNQ6oKii4Jh5qRsmZEJQb5bPxsixnt/wQcImqsmrvBLU9oCn/b+PinT19/gPF4yPjYMxK2QAAAABJRU5ErkJggg%3D%3D";
	this.Sf = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACcAAAAnCAQAAAAmqpm+AAAAAmJLR0QA/4ePzL8AAAAJcEhZcwAACxMAAAsTAQCanBgAAAAHdElNRQfdBRUUAAI4cucMAAAC30lEQVRIx83WT2hcVRTH8c97eTN5E5M2TWkq+Kd/UGjQFpG2KroorgpWVETducpCV2YhRRdd+Qe6EtFFF4rozkVxVxRBFGoiRIpakfinUWtRSGkbrB2nM5l3XeR1kkzejHGokHM3j3fe+3LOPb977okmgutosetqSWY9Rxf+H9x5p1R7Sq/sdretxJ11RmJrD7imuhkhByYZLjqjX1mpB1wmlZo1bARxEJxWkkqEHlYkkRowIwiLyQb9Ir0XJdLvsnAt2b5CWCx1rzHbzfvNlLOudgH2yZZXtl3OFU96TD/mbHOfp3zjA190iTEs4dpjS7xizJz3fauqZMgjHrTLce92xcXFG/yqMV951icGpUZljjqs7HH7uhYmDoKsbR20xzlH7HEQIwY03Om0w8qeUVr1/eIKwrUWsDzZ1AG84A5NkzJ/qmmCU97ztL1OdlBg3gJWxtfvLif97qq6AU1NCy3f5/5yqENsrUOWrYhuWGTSFg9IW9L40Qaj3jTnD3sLFZp1quw2/KTPeKtiUf70hr/VCnTQJpSw4oMLgpv8asomVRdsRnCDS4JY3AG3yEgW0NC3zDErsttHjvlSJlUXW8h9G436WaMA17BQ3I1rvvewQZkx1GQtGPttcaJb9wurQr/ihJIjZmwQicXKrdjG8XHHUxGKcHxo1v2eM5VLqA42e8cgjql0xhU5LntZzUNet9OAiophhxx3I4Kt3rapU2d5IjAtXeW41YR7RH5xEbtU8iYWCSJfe9F8247v64YjtdsBdyuLnfOpOUdbKgymTRTgulyMNdOmV7wZ91Yu6cj+zg1qrfad51XzH2udS7H2UWDSS+oiwWuF40QSUMkb0FrsM48aVnV+1U4HJLDTD61j/u8231bTxUR3LJ2K1A7xfwC232LcbGDpnm0YMWTWlZ5mlMQtNubzTbL4sqpku6GCJBY08trHkmVjRynPpqomag1LLd3VcWm9jYvre/r8BzXJTgadvkYEAAAAAElFTkSuQmCC";
	this.Pe = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACcAAAAnCAAAAACpyA7pAAAAAXNSR0IArs4c6QAAAAlwSFlzAAALEwAACxMBAJqcGAAAAAd0SU1FB9wGGgcqC+Q6N4oAAAF8SURBVDjLrZRBThRREIa/mq5WmJGBZJgFRjFx4cJ4AY/hhgvIAUyUW8DGtV6AAxjvoSsXJqILI2qiSDOZ6a7fxYOB0N1Om8zbvaov/19VqZQ9o9PrdcPwWLKe/oP7cXTSCmT97dE5d/RtfauVK4uPf7bBg98/7wxW2jDFcO3rcIiL4/Ewa+/abmTV8RouGFjAg6ebdej76w9gg0J4kGcB7K6807Uhhd3ffQFkeeACBTB6v1/X23sUgFDi0gwba0xB4SKqFKqauAoghIsyWKBXCo+5dgOn81zgdPEFF7FQL9XXwVe4qBb2UQkvmeQpctZEnQFMyiXvs65w04b89JKbx8YPM7+2ytW47nu487JB8LCm9+rL3VJQygBkDuaf39b04k3HPswg/Pm9U4DBp4OyN9/M5Ot28cHs8a30uW0mIKUcXKzKLlt80uTaFz3YXHSKYgQ9KTawf1DGRkguZv3+r0n7fcnXVYADRT662W46K2YX85tOl3Ynl31P/wJHQa4shXXBLAAAAABJRU5ErkJggg%3D%3D";
	this.Jf = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACcAAAAnCAAAAACpyA7pAAAAAXNSR0IArs4c6QAAAAlwSFlzAAALEwAACxMBAJqcGAAAAAd0SU1FB9wGGgghCwySqXwAAAGGSURBVDjLrZS/ThRRFMZ/d+YMO26yamgWEzdxFRsbqaTlGXgEnoAHwG20puABaC1MfA5jYWJsaBaNjQUJFIQJ2TtzP4sZdgh7B5dkb3XvmV++b86fHLfPUidZDsPCivX0AO7se9FtuPZ6s+H+TG3YyVWzE22CBc6nvbWskwt5fvp0nUT6meWmzuMs759IJtRzgrfvny2K/f3wA1zvUlggdQIm/a+6U6Tg3kx2AZeGOt8AbHyLdPDoXd0GYYKmhNFKquVU312EczUnYSI02iGmFgCCsLCMb8BaoejkhAY2EZp/VUxNN74PzvceTsJKfFpHfIzyAL5c8TzrFjeLfJ+13Dw23ErvTKuvhou+x3ufIoLHC3qHv8deUAYHoMTAZb++LOhVn5fMI3FQZR9fXQIMpgc+bVsvbL4S6o7vPK5fI1HdXhomHrUByu2YbS4SePm/UmsMiZSPE3cP5Xjel0z49cHpVfd+sdGTAgwosheDuUfpBYllAJmD4toVN/WbcbGqPbnqffoPyHTE/GI3wZEAAAAASUVORK5CYII%3D";
	this.Re = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACcAAAAnCAAAAACpyA7pAAAAAXNSR0IArs4c6QAAAAlwSFlzAAALEwAACxMBAJqcGAAAAAd0SU1FB9wGGgcqK99UF0IAAAGmSURBVDjLrZTNahRREIW/un3bnx4zCYwuBAk+hb2ZbV7BB3AhZlAQRN9EEBGXQQJBfArXvoCLWYnBgEbGIdNdx0WmTd/uGY0wtbunT9epOrfq2lMuFeFyNKJvOJ/+g/dterqWkBW7oyVv+nX79lpeNfv8cxei8+PkzuBa8s0uipEPt74Mh0RxfGuYdbu+O20Qu5LVx1sEiYF5J/b2WwcbIEUn72Ur759U7VZyJwrkaW3lI07bkNA5r+WhOeUEQiohovA6yTae4KGNgYsoquTf8QQFSLBKRE+x8jFClvJwIolu+QxhoFQXovA/lureCzz0853X12BZPX5OnS2vq99vvcSC3wCTNVIXUYtYMc8b3aPqSXAD8F9t3rzqzPOHl4Rlwr/Ms+B92LcVEy5C+9Iwjt5g9DJKqa6Md28x/+ceyXTAg7BCt4sYB687tqzcS5kOeVjQ97mnweFoL+1aRIjd9kyvPsX24EeI4nrXWZk+JudCBLjpfeksGZcRBMl3+sa2V4Edl6JYFMX3+fr3Jd/WDCIwy0dX1/J8MVs0/p2dbeyd3PR7+hsfn9edOMpPUgAAAABJRU5ErkJggg%3D%3D";
	this.Lf = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACcAAAAnCAAAAACpyA7pAAAAAXNSR0IArs4c6QAAAAlwSFlzAAALEwAACxMBAJqcGAAAAAd0SU1FB9wGGgghLkeWfTsAAAGuSURBVDjLrZQ/axRRFMV/983b7BiMSgptFIxE8GOILkgaaz+Eha2FRfwL8Q9pg2ih6Mewt7FJkyYp7EQwpHCQnZl7LOIu897Okgj7qnl3zpxzz713rj3gVCecDkb0BfPpP3A/v1XzBZeur//Dfd+Pl+bi2vGe1iE6v/aHS4PknXWS8bI8uLBKkHYHZVRyXDfC5NliubwnBUlDU3buPetcbDiWolNY7nl0/0fTTaPwY7+e5jZ6zFFafhEFXbrgjJ5C0CxOnZi1bGziQQlOIgpPNDY28YCSmIvoqe7tJ7jJSHWdSPLtrS3cLLOGIArX1MPN13gQOZ8nfov2zhZNnGQ+36/OQZBNpFK/DXVxfKvtkx6FtgBQ3cXVTTbPn59TuJ00z4KP9jD0AEVaeePDm2mKSYKproy324S2Z/yzTgZ2tilO4gMP7LzM2tHDB268f8XZnG/2/xW8u3g3ZA2OPSvB9OJr4enSiOJMbk+mL0mgFAGu9UgnjrUGQSrXwkxh227tLy9LUdSrKwe/5++XeOV8BRGoBldXphpNLQhxADAwqP5YNZmDMYeL2pOL3qd/AZpy8NOvjvTnAAAAAElFTkSuQmCC";
	this.Qe = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACcAAAAnCAAAAACpyA7pAAAAAXNSR0IArs4c6QAAAAlwSFlzAAALEwAACxMBAJqcGAAAAAd0SU1FB9wGGgcrBRqZK8wAAAE8SURBVDjLrZTNSsRAEIRrfuJPwmYXogdB9mmy7+P7iIgIIoKIL+RhT+Ki4A8hbJIuD+qaSWbWCKnTUPmopjM9rU4wSHoYBisj5/Ef3PPyPQiYeJ59c8un6VGQq4uHjzlgBW8vx8leCKOkk8c0hSVWh6kJd612TLOaQJNIlPzqVLpSCUgtEpm2i7MeaCIRTYIOh/MuR5AeDhd+Tpq2AOCycSWkJmvp5AFXbmBNahH0OVy7nogG+nUB3Dh1AU2KJw+4dTqhJuHlcNfySE02fg73G68hbY0y8t9svjmV9ZZ5zofNs4MxyLlpDNXNh72jLhbIy4e9yz7m7cOTRljAKsdbqH5RwBL7bH9ZeNJiQgMHf60iyb7maga1hVKYCWmJKo5fy/B+iaYsAAugiLLdcNGqqH7+33o92p4ce59+Av+enpsD10kAAAAAAElFTkSuQmCC";
	this.Kf = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACcAAAAnCAAAAACpyA7pAAAAAXNSR0IArs4c6QAAAAlwSFlzAAALEwAACxMBAJqcGAAAAAd0SU1FB9wGGggiBLcA5y4AAAE5SURBVDjLrZS7TsNAEEWPN+vERIpAaWhAIigU7vkwPhIQRDxFkyYpaJGgwkJxmEsRiPzaxEi5lTU6vuNZz97oglZy7TC87dhP/+DeHrJww+7Z+Jd7nfnDIPe9mGoM3nif9bpxkLMkmR8McdJLnHgFFfmkP5WcpF5UqF/Wyd5CcmadIiau6mDHzElgBcG1VQSSkyi9DNxUDVecqhy39XG8sPovnpyXz0Y4s1pf4K5cM3OgykcDcF+sCZxkDX7wWKhZ87wrPW2fd6Xn0rxL8k7zBqTrp3y5YZ/TdvtcwhTkym4K9U3b3aMqFvBL293LOtY4R4ObcLVISBtDw0l72zASycHptujQCJyUjFy0gYo46kte5MPB/DOcL/54PwMPZPHJYN1jmQucjwHiCLKvKPs7vwUfu8rJXefpD93iniqiS4VUAAAAAElFTkSuQmCC";
	this.Oe = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACcAAAAnCAAAAACpyA7pAAAAAXNSR0IArs4c6QAAAAlwSFlzAAALEwAACxMBAJqcGAAAAAd0SU1FB9wGGgcrJ8/5aigAAAJ5SURBVDjLrZTfSxRRFMe/d+aOq2v7I9fV/JEEYWVaGZrgQ2DYQ0HQW73ZSw+BPgRBUA9FPvUiRC+Bf0AQRBGR1ENEoWgERmhgij/a1owkdXSb3WZnzulhZnZHc8vA83bnfO73fO+dc4+4jC2FsjUMkrZZj/+D+5FYKwiowbqYyyW+R6oKcpYxk6oDJGF1qba0uBDGFA59C4chGYvxsFr41KJItRdDkAyUCgcTjHjTgZpUYvzTLz9ZajAkQcupBc6eBi9V13d+fjjuP4pGkAwwOWqip0l/MqWrFR3tV+6/8HkEQz2KVDE70dM8evvr3ob65YHJ9iOJefYCmR2QDLKdbZ1tk30nLmhiNpr60He1a0LPCRJDMizHXuA47rZdxNSDjwBGn5459CZ/hwyFCERERPH64XQXZm6NkWCiYdFOuQCRhFe3TLyL76Q7GcAGkEg02/m6gGSQU7cCC5oYTLopw2Da4A/OhxVEl3nMS6pSIf/NKMy2Y2Kem5LC8ixV1c7m/dnM0kJGAwDMfTnV/2hX2lVoKX6ezsllLF8/rw2o3ffeB5xF9XkeXd+GjVhxc3Otx4qeOYeM91aKfa+zwoXMqI8T2bGO1sbln4pWefJ6FYvylsFMnhPnMBfyxHd3t4iFJWW/wmABTF1zf93aHqgHoQc8bvXltFldFpp+/KpNQlC8wW0aMwK5vsuHhkoETAt6r2JJPux7v7zhYaYNwwJGbtiqLfL7+Q/OjZGbpsL9eU4CUmwGvr1Uo0+4GQlIRglvCiaTObUgQwHK/zWKKAYozBSF+AslECVmycgGg3qm8HzRImwAEoChxQKFi2aNrDevTHPb5uR2z9PfLQs68f4FXIYAAAAASUVORK5CYII%3D";
	this.If = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACcAAAAnCAQAAAAmqpm+AAAAAXNSR0IArs4c6QAAAAlwSFlzAAALEwAACxMBAJqcGAAAAAd0SU1FB9wGGggiGzoI6tsAAALsSURBVEjHzdZNaBxlGAfw3+xOspvFYK2WSlvTpih40IJgP9RDIQcFEQLB6kFykUIvUj9zCR4KiiCItCB6UqEXDyqePAgpRhCtaSmYYonFJsGepHXrRzfE7mZeDztJZzezoV0r5HlPM++8//n/n6/3iV4KbqEV3FKLE+uZXfh/4C45Y6Ereb3uc28r3K8uiG3uAm7JNTNCChgnqLqgpFdPF3CJsrJZG2xEIQjO6lEWC12sSKysYkYQmmKDkkg2KM2nLfZ5yE5/Oe8HZyx2YBgp+VtYFltsAyPo87znEPzmTrs87bz3TXUELEqykU1amBW8Za/ffWRaVWyrYU846phPOnoxLPtOizcSwZv2+MazTtjhKSNKjjhsyYv2d/ChrO9apY4YMm3MsNf0iszY5KqTXnXUmB9Vc7mFZbGJbOX2eRLjhozjrA+cSne+ddyovb7MTZiAQhMqS3uLB01Y8ArOOWRKUEi/mRB5vIPYRCJuEs2y2ywyaZtNlryeJsfy/qxfPCLpEIgVsVnfbcVFJZGvzLUdqqmtHM0TmyZK9oMruMtlwelVB4tiBWHtRGld84Ld5kUaq/YGDPp5jZKLG6grZv4yb9YB7zpuQL2NwX4VX6x6C3WN/G78p88UjXvbSeWWnQEHBd+v1f1Cjic+dc6wl33XUvR3O+Y2kTf0I8pN5By4gpoxVQd96FEbVFRsN+pz9wvY5WN35JAIguiZwFSbKBg07jGRiy4reiCNZ0hZ/eRQW6kt2oPoQOBUDhwFQ4bsU8KcE/5wRK8g0hA7bbQNbjfNqujUtidMqIhEFjVwxXuKGmKJh288FFlbUHNVA0x6QUNRI/d67hCKtWzSYf8oCt7JhYtvdhT42ojtqqZzx4k4oM/STQDOrWoMUG7WLOz0X0eLYPB6KMoGFXLy/MYswjaV63dF3Ub9ZtW6mlFi97g9nW/i5XTosUN/joiGeuqKgjgzdvSkahYsilaGpZV79lraONfVuLi+p89/AdAUWQEn4HTQAAAAAElFTkSuQmCC";
	this.le = "data:image/gif;base64,R0lGODlhAwAVAIABAJmZmf///yH5BAEKAAEALAAAAAADABUAAAINRBynaaje0pORrWnhKQA7";
	this.ol = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAsAAAANCAYAAAB/9ZQ7AAAABmJLR0QA/wD/AP+gvaeTAAAACXBIWXMAAA3XAAAN1wFCKJt4AAAAB3RJTUUH3QkaAjcFsynwDgAAAMxJREFUKM+9kLEuRQEQRGeuV5FIJApBQZ5EReFP/IBCBIVvpFT4BR9AR+29cxTukyvRaEyzmd3Jzu4kI4Ad9d4JANVLdS1JhvwB/yBuu0jiL5pl22WSzNRBPVE3225MVW2TZA84bfsWYFDvgNX30zQY6wtwmCRRo96qy9V8Et2zevDjMKDqFfA+2fykzr9F6o16vnIALtRX4AE4GvtbwHVGq8epi3qm7k74HFjMRrINnLdd/6KS5FgdkpBkv206DkzykaSTbWkbdUyxs094zOEo59nhUAAAAABJRU5ErkJggg%3D%3D";
	this.sl = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAANCAYAAACZ3F9/AAAAAXNSR0IArs4c6QAAAAZiS0dEAFEAUQBRjSJ44QAAAAlwSFlzAAALEwAACxMBAJqcGAAAAAd0SU1FB9wCCgAwB/13ZqAAAADXSURBVCjPhZIxTkMxEETf2F9I0EaCI9DRUZEL0XINbpMzQJ2eG1DQpvszNDbyN4kylde7O+PxLgxIckgS2+mw3ePDWFumxrPnc/GmURKXMOfKXDAzX8LcWEfmTtLu6li42O4SD8ARuAHW6RVV0tH2PfANsAyMT8A7cJo9JSHJHfAsiSSoKa6S6jWfjWxNUrtiAbKtUQaSLh+gSEppSf3/3I1qBmIl0ejxC3BnHz02X2lTeASgr5ft3bXZ2d71NVyA1yS3pZSfJB/AS5I/xWGWn5L2tt+A0y9ldpXCCID4IwAAAABJRU5ErkJggg%3D%3D";
	this.Al = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAABmJLR0QA/wD/AP+gvaeTAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAB3RJTUUH3gIDABU3A51oagAAAIpJREFUOMulk9ENgCAMRKkTOAqjMIKj6CSOghs4gm7gCM+fGgmCsXJJP0i4cj16zhkBjNwYreSeDJ1rhLVByM6TRf6gqgf3w7g6GTi0fGJUTHxaX19W8oVNK8f6RaYHZiqo8aTQqHhZROTrNy4VhcGybamJMRltBvpfGwcENXxryYJvzcLemp1HnE/SdAV9Q8z4YgAAAABJRU5ErkJggg%3D%3D";
	this.nl = "data:image/png;base64,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%3D%3D";
	this.Cl = "data:image/png;base64,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%3D%3D";
	this.ql = "data:image/gif;base64,R0lGODlhEAAPAKECAGZmZv///1FRUVFRUSH5BAEKAAIALAAAAAAQAA8AAAIrlI+pB7DYQAjtSTplTbdjB2Wixk3myDTnCnqr2b4vKFxyBtnsouP8/AgaCgA7";
	this.rl = "data:image/gif;base64,R0lGODlhDQANAIABAP///1FRUSH5BAEHAAEALAAAAAANAA0AAAIXjG+Am8oH4mvyxWtvZdrl/U2QJ5Li+RQAOw%3D%3D";
	this.ul = "data:image/gif;base64,R0lGODlhDQANAIABAP///1FRUSH5BAEHAAEALAAAAAANAA0AAAIYjAOnC7ncnmpRIuoerpBabF2ZxH3hiSoFADs%3D";
	this.Dl = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA0AAAANCAYAAABy6+R8AAAAAXNSR0IArs4c6QAAAAZiS0dEAFEAUQBRjSJ44QAAAAlwSFlzAAALEwAACxMBAJqcGAAAAAd0SU1FB9wCCgEMO6ApCe8AAAFISURBVCjPfZJBi49hFMV/521MUYxEsSGWDDWkFKbkA/gAajaytPIFLKx8BVkodjP5AINGU0xZKAslC3Ys2NjP+VnM++rfPzmb23065z6de27aDsMwVD0C3AfOAYeB38BP9fEwDO/aMgwDAAFQDwKbwC9gZxScUM8Al5M8SPJ0Eu5JYV0FeAZcBFaAxSSPkjwHnrQ9Pf1E22XVsX5s+1m9o54cB9J2q+361KM+VN+ot9uqrjIH9VJbpz7qOvAeuAIcSnJzThA1SXaTBGAAvgCrwEvg0yxRXUhikrOjZ1RQz7uHFfUu/4C60fb16G9hetxq+1a9Pkdears2Dt1Rj87mdAx4BfwAttWvSQ4AV9W1aYlJtoFbmQJTjwP3gAvAIlDgG7CsXvu7uWQzs+cxmj0F7Fd3k3wfuRvqDWAfM+HxP6hL6oe2tn3xB7408HFbpc41AAAAAElFTkSuQmCC";
	this.pl = "data:image/png;base64,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%3D%3D";
	this.Bl = "data:image/gif;base64,R0lGODlhEAAPAIABAP///1FRUSH5BAEKAAEALAAAAAAQAA8AAAIkjI+pi+DhgJGMnrfsxEnDqHgRN3WjJp5Wel6mVzbsR8HMjScFADs%3D";
	this.zl = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABoAAAAaCAYAAACpSkzOAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyJpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMC1jMDYwIDYxLjEzNDc3NywgMjAxMC8wMi8xMi0xNzozMjowMCAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENTNSBNYWNpbnRvc2giIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6NDJBOEJGMUEyN0IyMTFFMTlFOTNFMjNDNDUxOUFGMTciIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6NDJBOEJGMUIyN0IyMTFFMTlFOTNFMjNDNDUxOUFGMTciPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDo0MkE4QkYxODI3QjIxMUUxOUU5M0UyM0M0NTE5QUYxNyIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDo0MkE4QkYxOTI3QjIxMUUxOUU5M0UyM0M0NTE5QUYxNyIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PrESQzQAAAF3SURBVHjaYvz//z8DPQATA53A8LOIkRLNNpaWAkCqHogVgBjEbjxy/PgBbGpZKLRkPxAbQIUuAPEHXOqZsRhwX05WVhCIHzx68gSnRqB8O5AKQBKSAGIPoPhFoL4HBIMOaNF5JFcuAOKF6MEBVOMA9Q0ukAjUs4BQYkhECoIEkIFAg/dDDYeBfAIh2w9Ur0BMqkMPMgeohfOhBgQQsAiWSPAGHcig+3gMeQBNZYTAA2jogCy1Z8SRokAung9VRCkAWRiIK+guQBVQCj5AzalnITKOyAWg1HoQlHoZCWRIUBD2kxmEG4BJPJBgWQdUBPM2ufG0EaVkALkcmJN/YFMJyuHAnM4IzcAcpAQZ0KGF6PkoAGhZAzSosAUfP4m+AoVEINYiCGQRNLeDIu8iVE6fiIyJzRJHoG8u4CzrgJYlUBDxsBQWCI1b/PURtFSoh5ZxxIIL0HpoA8kVH1J55g9NCAJowXMBmj82YAsmrBaNtoIGvUUAAQYApBd2hzrzVVQAAAAASUVORK5CYII%3D";
	this.wl = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABoAAAAaCAYAAACpSkzOAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyJpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMC1jMDYwIDYxLjEzNDc3NywgMjAxMC8wMi8xMi0xNzozMjowMCAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENTNSBNYWNpbnRvc2giIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6NDJBOEJGMUUyN0IyMTFFMTlFOTNFMjNDNDUxOUFGMTciIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6NDJBOEJGMUYyN0IyMTFFMTlFOTNFMjNDNDUxOUFGMTciPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDo0MkE4QkYxQzI3QjIxMUUxOUU5M0UyM0M0NTE5QUYxNyIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDo0MkE4QkYxRDI3QjIxMUUxOUU5M0UyM0M0NTE5QUYxNyIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/Pj8crNUAAAFxSURBVHjavFbNbYMwGDU0A7BA2oxAj5EqlU7QZgKSY4+ZgDJBmgmAY09JN8ihUo7NBqVVBmCD9H3qc4UsnCBi8qQnGwN+fL/GU8TdePyCIQZHyg1KsPjYbmVf5VEkwzBV/SCH2MyjJYnqF6lPd/WN2HcYk2O4hMYfJEaHSwj5l7JocOTeBgzAd84j8J6jM6E5U16EQq69go8uXZeDO4po6DpLXQoVYNWwHlrWOwuFaBk79qomMRseyNbpLQK34BOYca1i3BaGS/+Bj9N989A2GaSKv8AlNw8Ys1WvBStfimfEZZ82K2yo732yYPHwlDGbnZMMTRbJZmvOA+06iM1tlnWJUcXMyYwMi7BBxHt5l0PSdF1qdAMztSUTv120oNJSP6rmyvhU4NtYlNB9TYHfsKmOulpU1l7WwZYamtQ69Q3nXU/KcsDelhgFu3B8HBU6JVcMdB9YI/UnVzL72e/frodDj9YEDn8glxB5lotfAQYAtCJqk8z+2M8AAAAASUVORK5CYII%3D";
	this.xl = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABoAAAAaCAYAAACpSkzOAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyJpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMC1jMDYwIDYxLjEzNDc3NywgMjAxMC8wMi8xMi0xNzozMjowMCAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENTNSBNYWNpbnRvc2giIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6Q0FBOEM3Q0EyOTQ4MTFFMUFDMjBDMDlDMDQxRTYzMzkiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6Q0FBOEM3Q0IyOTQ4MTFFMUFDMjBDMDlDMDQxRTYzMzkiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDpBMENEMDM3NTI5NDgxMUUxQUMyMEMwOUMwNDFFNjMzOSIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDpBMENEMDM3NjI5NDgxMUUxQUMyMEMwOUMwNDFFNjMzOSIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/Ptz3FgYAAAErSURBVHjaYmQAAhtLSwEgVQ/ECUAMYlMDfADiBUDceOT48Q+MUEv2A7EBA23ABSB2ZJaTlW0HMgIYaAckgJiDCRpctAYJLFjiBBS2E4GYn4pxJsCCRdAQGHkPoIlkIzT+KAZM6L6BWQICQPYBaoUdukUCQF/A4wzILqCWRaDk/R9HkmSgZpJnwiFuQKIlFwgpwEgMwHhhRObDfIxDvBAoPgFJDBTs/dhSKhMFoZGIbAnUMaAixxGaRahjEchQoA8MgNgBTfwCtIyjjkVAC0BBdB6Uz4Bs9Ly2kZpBh5z0HQglDiZaFGygaoEuFpGSj0YtGoEWgUrv91Rs+eBsETFhKy5oABaALGokppinsLnVyPzoyZMfwCbXSlCTCIg1oDS1GpAzoKX8B4AAAwAuBFgKFwVWUgAAAABJRU5ErkJggg%3D%3D";
	this.yl = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABoAAAAaCAYAAACpSkzOAAAABmJLR0QA/wD/AP+gvaeTAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAB3RJTUUH3gEfAAUcuIwRjAAAAVpJREFUSMftlrtKA0EUhr9ZFkEMCCpYCb6AIGJzdF7AUhRsREF9AQmCl1IhgpjGwkohb+Ab2Ew4ldZik8pOVOy8kNhMYAhBd5PZVB5Y2BnO8O3M/5+zYwCsyA6wD0wALeKEAZ6BY6daM1ZkA6hRbGwmQJniYy8FRnMsePVHOwSUcqwbSfJo4lTHnOo4sJx3S0mOXA3eh4sEHVmRnkVKM+adONXbDutGBT0CW0613mX+FGgGc4f9gK6AehdTPAAH7bEVMX+BkgxOy+LGVr9Ht2ZFZoDrUCMrMusLvRlLozn/OCA0wxSwXpS9+4p/UDu+iwJ12vetKFAp7HNOVYE7P/wC7oFqjF634FSrQR3hVOfDBCuyHWNHK1ZkMYCEgEy6GSvSAKYzAs+BS+AJ+PD/pUlgCbj45cMbac6WX+71jpEALwMoo/cEqAwAVDFe0FXgzN9uYsYnsOtUb34AitxcDYrQdlwAAAAASUVORK5CYII%3D";
	this.vl = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABoAAAAaCAYAAACpSkzOAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyJpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMC1jMDYwIDYxLjEzNDc3NywgMjAxMC8wMi8xMi0xNzozMjowMCAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENTNSBNYWNpbnRvc2giIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6NDJBOEJGMTYyN0IyMTFFMTlFOTNFMjNDNDUxOUFGMTciIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6NDJBOEJGMTcyN0IyMTFFMTlFOTNFMjNDNDUxOUFGMTciPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDpDNTQyQTc3NTI3QjExMUUxOUU5M0UyM0M0NTE5QUYxNyIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDpDNTQyQTc3NjI3QjExMUUxOUU5M0UyM0M0NTE5QUYxNyIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PkQAqvIAAADoSURBVHjaYmEAAhtLSwEgVQ/ECUAMYlMDfADiBUDceOT48Q+MUEv2A7EBA23ABSB2ZJaTlW0HMgIYaAckgJiDCRpctAYJTFSME3xAgIlCAw4AcSAoDoBYEBjpjCCMTSELJZYADXUkVjElPppIimIWCpMtHACzyXt88U22j4DB9gA9wmkVdCQBcixqxJaykFJcIb18JEAvi+SxCYIK1f9kJgZGtFT3f8gmhlGLRi2i3KIPdLDnAwu0SVRAqk4SM/oCkI8a0esWGjS3GpkfPXnyA9jkWglqEgGxBpSmVgNyBhAnghqQAAEGADc+O4K5UN0FAAAAAElFTkSuQmCC";
	this.ta = g.ta;
	this.aa.zb = -1;
	this.cb = new ra;
	this.ic = new sa;
	this.mk = new ta;
	this.Dj = new ua;
	this.Hl = new va;
	this.Hj = function(c) {
		var d = this;
		d.Ja = c;
		d.Rc = d.aa.Fd ? jQuery("#" + d.Ja).wrap("<div id='" + d.Ja + "_wrap' style='text-align:center;width:100%;position:absolute;z-index:100;top:-70px'></div>").parent() : jQuery("#" + d.Ja).wrap("<div id='" + d.Ja + "_wrap' style='text-align:center;width:100%;'></div>").parent();
		d.aa.ug = n;
		jQuery("#" + d.Ja).css("visibility", "hidden");
		d.ta.PreviewMode = d.ta.config.document.PreviewMode;
		d.ta.config.document.UIConfig != p ? jQuery.ajax({
			type: "GET",
			url: d.ta.config.document.UIConfig != p ? d.ta.config.document.UIConfig : "UI_Zine.xml",
			dataType: "xml",
			error: function() {
				d.eh();
			},
			success: function(c) {
				d.je = c;
				c = !eb.platform.touchonlydevice ? "desktop" : "mobile";
				!eb.platform.Nb && (eb.platform.touchonlydevice && 0 < jQuery(d.je).find("tablet").length) && (c = "tablet");
				toolbar_el = jQuery(d.je).find(c).find("toolbar");
				var e = jQuery(d.je).find(c).find("general");
				d.backgroundColor = jQuery(e).attr("backgroundColor");
				d.linkColor = jQuery(e).attr("linkColor") != p ? jQuery(e).attr("linkColor") : "#72e6ff";
				d.ta.linkColor = d.linkColor;
				d.of = jQuery(e).attr("linkAlpha") != p ? jQuery(e).attr("linkAlpha") : 0.4;
				d.ta.of = d.of;
				d.backgroundImage = jQuery(e).attr("backgroundImage");
				d.El = jQuery(e).attr("stretchBackgroundImage") == p || jQuery(e).attr("stretchBackgroundImage") != p && "true" == jQuery(e).attr("stretchBackgroundImage");
				d.aa.Zf = jQuery(e).attr("enablePageShadows") == p || jQuery(e).attr("enablePageShadows") != p && "true" == jQuery(e).attr("enablePageShadows");
				d.hb = ("true" == jQuery(e).attr("forceSinglePage") || eb.platform.Nb) && !d.ta.PreviewMode;
				d.fd = jQuery(e).attr("panelColor");
				d.kh = jQuery(e).attr("backgroundAlpha");
				d.tg = jQuery(e).attr("navPanelBackgroundAlpha");
				d.Mh = jQuery(e).attr("imageAssets");
				d.te = !eb.platform.touchonlydevice && (jQuery(e).attr("enableFisheyeThumbnails") == p || jQuery(e).attr("enableFisheyeThumbnails") && "false" != jQuery(e).attr("enableFisheyeThumbnails")) && !d.hb;
				d.aa.ug = "false" != jQuery(e).attr("navPanelsVisible");
				d.Dd = jQuery(e).attr("zoomDragMode") != p && "false" != jQuery(e).attr("zoomDragMode");
				d.se = jQuery(e).attr("disableZoom") != p && "false" != jQuery(e).attr("disableZoom");
				d.Bi = jQuery(e).attr("flipSpeed") != p ? jQuery(e).attr("flipSpeed") : "medium";
				jQuery(d.toolbar.ca).css("visibility", "hidden");
				if (d.backgroundImage) {
					d.El ? (jQuery(d.ta.ga).css("background-color", ""), jQuery(d.ta.ga).css("background", ""), jQuery(d.ta.ka).css({
						background: "url('" + d.backgroundImage + "')",
						"background-size": "cover"
					}), jQuery(d.ta.ga).css("background-size", "cover")) : (jQuery(d.ta.ga).css("background", ""), jQuery(d.ta.ka).css({
						background: "url('" + d.backgroundImage + "')",
						"background-color": d.backgroundColor
					}), jQuery(d.ta.ga).css("background-size", ""), jQuery(d.ta.ga).css("background-position", "center"), jQuery(d.ta.ka).css("background-position", "center"), jQuery(d.ta.ga).css("background-repeat", "no-repeat"), jQuery(d.ta.ka).css("background-repeat", "no-repeat"));
				} else {
					if (d.backgroundColor && -1 == d.backgroundColor.indexOf("[")) {
						var g = ja(d.backgroundColor),
							g = "rgb(" + g.r + "," + g.g + "," + g.b + ")";
						jQuery(d.ta.ga).css("background", g);
						jQuery(d.ta.ka).css("background", g);
						jQuery(d.Rc).css("background", g);
					} else {
						if (d.backgroundColor && 0 <= d.backgroundColor.indexOf("[")) {
							var m = d.backgroundColor.split(",");
							m[0] = m[0].toString().replace("[", "");
							m[0] = m[0].toString().replace("]", "");
							m[0] = m[0].toString().replace(" ", "");
							m[1] = m[1].toString().replace("[", "");
							m[1] = m[1].toString().replace("]", "");
							m[1] = m[1].toString().replace(" ", "");
							g = m[0].toString().substring(0, m[0].toString().length);
							m = m[1].toString().substring(0, m[1].toString().length);
							jQuery(d.ta.ga).css("background", "");
							jQuery(d.ta.ka).css({
								background: "linear-gradient(" + g + ", " + m + ")"
							});
							jQuery(d.ta.ka).css({
								background: "-webkit-linear-gradient(" + g + ", " + m + ")"
							});
							eb.browser.msie && 10 > eb.browser.version && (jQuery(d.ta.ga).css("filter", "progid:DXImageTransform.Microsoft.gradient(GradientType=0,startColorStr='" + g + "', endColorStr='" + m + "');"), jQuery(d.ta.ka).css("filter", "progid:DXImageTransform.Microsoft.gradient(GradientType=0,startColorStr='" + g + "', endColorStr='" + m + "');"));
						} else {
							jQuery(d.ta.ka).css("background-color", "#222222");
						}
					}
				}
				d.fh();
				jQuery(d.toolbar.ca).children().css("display", "none");
				d.Nf = d.qa;
				d.Of = d.qa;
				d.Hf = d.qa;
				d.Ne = d.qa;
				d.Se = d.qa;
				d.Pf = d.qa;
				d.Te = d.qa;
				d.Qf = d.qa;
				d.Ue = d.qa;
				d.Rf = d.qa;
				d.Ve = d.qa;
				d.Sf = d.qa;
				d.Pe = d.qa;
				d.Jf = d.qa;
				d.Re = d.qa;
				d.Lf = d.qa;
				d.Qe = d.qa;
				d.Kf = d.qa;
				d.Oe = d.qa;
				d.If = d.qa;
				var q = "",
					y = p,
					g = 0;
				jQuery(toolbar_el).attr("visible") && "false" == jQuery(toolbar_el).attr("visible") ? d.zi = v : d.zi = n;
				!jQuery(toolbar_el).attr("width") || jQuery(toolbar_el).attr("width") != p && 0 <= jQuery(toolbar_el).attr("width").indexOf("%") ? jQuery(d.toolbar.ca).css("width", p) : jQuery(toolbar_el).attr("width") && jQuery(d.toolbar.ca).css("width", parseInt(jQuery(toolbar_el).attr("width")) + 60 + "px");
				jQuery(toolbar_el).attr("backgroundColor") && jQuery(d.toolbar.ca).css("background-color", jQuery(toolbar_el).attr("backgroundColor"));
				jQuery(toolbar_el).attr("borderColor") && jQuery(d.toolbar.ca).css("border-color", jQuery(toolbar_el).attr("borderColor"));
				jQuery(toolbar_el).attr("borderStyle") && jQuery(d.toolbar.ca).css("border-style", jQuery(toolbar_el).attr("borderStyle"));
				jQuery(toolbar_el).attr("borderThickness") && jQuery(d.toolbar.ca).css("border-width", jQuery(toolbar_el).attr("borderThickness"));
				jQuery(toolbar_el).attr("paddingTop") && (jQuery(d.toolbar.ca).css("padding-top", jQuery(toolbar_el).attr("paddingTop") + "px"), g += parseFloat(jQuery(toolbar_el).attr("paddingTop")));
				jQuery(toolbar_el).attr("paddingLeft") && jQuery(d.toolbar.ca).css("padding-left", jQuery(toolbar_el).attr("paddingLeft") + "px");
				jQuery(toolbar_el).attr("paddingRight") && jQuery(d.toolbar.ca).css("padding-right", jQuery(toolbar_el).attr("paddingRight") + "px");
				jQuery(toolbar_el).attr("paddingBottom") && (jQuery(d.toolbar.ca).css("padding-bottom", jQuery(toolbar_el).attr("paddingBottom") + "px"), g += parseFloat(jQuery(toolbar_el).attr("paddingTop")));
				jQuery(toolbar_el).attr("cornerRadius") && jQuery(d.toolbar.ca).css({
					"border-radius": jQuery(toolbar_el).attr("cornerRadius") + "px",
					"-moz-border-radius": jQuery(toolbar_el).attr("cornerRadius") + "px"
				});
				jQuery(toolbar_el).attr("height") && jQuery(d.toolbar.ca).css("height", parseFloat(jQuery(toolbar_el).attr("height")) - g + "px");
				jQuery(toolbar_el).attr("location") && "float" == jQuery(toolbar_el).attr("location") && (d.Af = n);
				jQuery(toolbar_el).attr("location") && "bottom" == jQuery(toolbar_el).attr("location") && (d.gn = n, jQuery(d.toolbar.ca).parent().detach().insertAfter(d.ga), jQuery(d.toolbar.ca).css("margin-top", "15px"), jQuery(jQuery(d.aa.ga).css("height", jQuery(d.aa.ga).height() - 40 + "px")));
				var z = 2 == eb.platform.oc && !eb.platform.touchonlydevice ? "@2x" : "";
				jQuery(jQuery(d.je).find(c)).find("toolbar").find("element").each(function() {
					"bttnPrint" == jQuery(this).attr("id") || jQuery(this).attr("id");
					"bttnDownload" == jQuery(this).attr("id") && !d.aa.document.PDFFile && jQuery(this).attr("visible", v);
					"bttnDownload" == jQuery(this).attr("id") && (d.ta.renderer.config.signature && 0 < d.ta.renderer.config.signature.length) && jQuery(this).attr("visible", v);
					if (!jQuery(this).attr("visible") || "true" == jQuery(this).attr("visible")) {
						switch (jQuery(this).attr("type")) {
							case "button":
								q = ".flexpaper_" + jQuery(this).attr("id");
								jQuery(this).attr("paddingLeft") && jQuery(q).css("padding-left", jQuery(this).attr("paddingLeft") - 6 + "px");
								if (0 == jQuery(q).length && (jQuery(d.toolbar.ca).append(String.format("<img id='{0}' class='{1} flexpaper_tbbutton'/>", jQuery(this).attr("id"), "flexpaper_" + jQuery(this).attr("id"))), jQuery(this).attr("onclick"))) {
									var c = jQuery(this).attr("onclick");
									jQuery(q).bind("mousedown", function() {
										eval(c);
									});
								}
								var e = jQuery(this).attr("id");
								jQuery(this).attr("src") && (e = jQuery(this).attr("src"));
								jQuery(q).load(function() {
									jQuery(this).css("display", "block");
								});
								jQuery(q).attr("src", d.Mh + e + z + ".png");
								jQuery(this).attr("icon_width") && jQuery(q).css("width", jQuery(this).attr("icon_width") + "px");
								jQuery(this).attr("icon_height") && jQuery(q).css("height", jQuery(this).attr("icon_height") + "px");
								jQuery(this).attr("paddingRight") && jQuery(q).css("padding-right", jQuery(this).attr("paddingRight") - 6 + "px");
								jQuery(this).attr("paddingTop") && jQuery(q).css("padding-top", jQuery(this).attr("paddingTop") + "px");
								d.Af ? jQuery(q).css("margin-top", "0px") : jQuery(q).css("margin-top", "2px");
								y != p && jQuery(q).insertAfter(y);
								y = jQuery(q);
								break;
							case "separator":
								q = "#" + d.toolbar.Ja + "_" + jQuery(this).attr("id");
								jQuery(q).css("display", "block");
								jQuery(q).attr("src", d.Mh + "/bar" + z + ".png");
								jQuery(this).attr("width") && jQuery(q).css("width", jQuery(this).attr("width") + "px");
								jQuery(this).attr("height") && jQuery(q).css("height", jQuery(this).attr("height") + "px");
								jQuery(this).attr("paddingLeft") && jQuery(q).css("padding-left", +jQuery(this).attr("paddingLeft") + "px");
								jQuery(this).attr("paddingRight") && jQuery(q).css("padding-right", +jQuery(this).attr("paddingRight") + "px");
								jQuery(this).attr("paddingTop") && jQuery(q).css("padding-top", +jQuery(this).attr("paddingTop") + "px");
								jQuery(q).css("margin-top", "0px");
								y != p && jQuery(q).insertAfter(y);
								y = jQuery(q);
								break;
							case "slider":
								q = ".flexpaper_" + jQuery(this).attr("id");
								jQuery(q).css("display", "block");
								jQuery(this).attr("width") && jQuery(q).css("width", jQuery(this).attr("width") + "px");
								jQuery(this).attr("height") && jQuery(q).css("height", jQuery(this).attr("height") + "px");
								jQuery(this).attr("paddingLeft") && jQuery(q).css("padding-left", jQuery(this).attr("paddingLeft") + "px");
								jQuery(this).attr("paddingRight") && jQuery(q).css("padding-right", jQuery(this).attr("paddingRight") + "px");
								jQuery(this).attr("paddingTop") && jQuery(q).css("padding-top", jQuery(this).attr("paddingTop") + "px");
								d.Af ? jQuery(q).css("margin-top", "-5px") : jQuery(q).css("margin-top", "-3px");
								y != p && jQuery(q).insertAfter(y);
								y = jQuery(q);
								break;
							case "textinput":
								q = ".flexpaper_" + jQuery(this).attr("id");
								jQuery(q).css("display", "block");
								jQuery(this).attr("width") && jQuery(q).css("width", jQuery(this).attr("width") + "px");
								jQuery(this).attr("height") && jQuery(q).css("height", jQuery(this).attr("height") + "px");
								jQuery(this).attr("paddingLeft") && jQuery(q).css("padding-left", jQuery(this).attr("paddingLeft") + "px");
								jQuery(this).attr("paddingRight") && jQuery(q).css("padding-right", jQuery(this).attr("paddingRight") + "px");
								jQuery(this).attr("paddingTop") && jQuery(q).css("padding-top", jQuery(this).attr("paddingTop") + "px");
								jQuery(this).attr("readonly") && "true" == jQuery(this).attr("readonly") && jQuery(q).attr("disabled", "disabled");
								y != p && jQuery(q).insertAfter(y);
								eb.platform.touchonlydevice ? jQuery(q).css("margin-top", jQuery(this).attr("marginTop") ? jQuery(this).attr("marginTop") + "px" : "7px") : d.Af ? jQuery(q).css("margin-top", "-2px") : jQuery(q).css("margin-top", "0px");
								y = jQuery(q);
								break;
							case "label":
								q = ".flexpaper_" + jQuery(this).attr("id");
								jQuery(q).css("display", "block");
								jQuery(this).attr("width") && jQuery(q).css("width", jQuery(this).attr("width") + "px");
								jQuery(this).attr("height") && jQuery(q).css("height", jQuery(this).attr("height") + "px");
								jQuery(this).attr("paddingLeft") && jQuery(q).css("padding-left", jQuery(this).attr("paddingLeft") + "px");
								jQuery(this).attr("paddingRight") && jQuery(q).css("padding-right", jQuery(this).attr("paddingRight") + "px");
								jQuery(this).attr("paddingTop") && jQuery(q).css("padding-top", jQuery(this).attr("paddingTop") + "px");
								y != p && jQuery(q).insertAfter(y);
								eb.platform.touchonlydevice ? jQuery(q).css("margin-top", jQuery(this).attr("marginTop") ? jQuery(this).attr("marginTop") + "px" : "9px") : d.Af ? jQuery(q).css("margin-top", "1px") : jQuery(q).css("margin-top", "3px");
								y = jQuery(q);
								break;
						}
					}
				});
				jQuery(d.toolbar.ca).css({
					"margin-left": "auto",
					"margin-right": "auto"
				});
				jQuery(toolbar_el).attr("location") && jQuery(toolbar_el).attr("location");
				jQuery(e).attr("glow") && "true" == jQuery(e).attr("glow") && (d.Zl = n, jQuery(d.toolbar.ca).css({
					"box-shadow": "0 0 35px rgba(22, 22, 22, 1)",
					"-webkit-box-shadow": "0 0 35px rgba(22, 22, 22, 1)",
					"-moz-box-shadow": "0 0 35px rgba(22, 22, 22, 1)"
				}));
				d.fd ? jQuery(d.toolbar.ca).css("background-color", d.fd) : eb.platform.touchonlydevice ? !jQuery(toolbar_el).attr("gradients") || jQuery(toolbar_el).attr("gradients") && "true" == jQuery(toolbar_el).attr("gradients") ? jQuery(d.toolbar.ca).addClass("flexpaper_toolbarios_gradients") : jQuery(d.toolbar.ca).css("background-color", "#555555") : jQuery(d.toolbar.ca).css("background-color", "#555555");
				d.zi ? jQuery(d.toolbar.ca).css("visibility", "visible") : jQuery(d.toolbar.ca).hide();
				jQuery(jQuery(d.je).find("content")).find("page").each(function() {
					var c = jQuery(this);
					jQuery(this).find("link").each(function() {
						d.aa.addLink(jQuery(c).attr("number"), jQuery(this).attr("href"), jQuery(this).attr("x"), jQuery(this).attr("y"), jQuery(this).attr("width"), jQuery(this).attr("height"));
					});
					jQuery(this).find("video").each(function() {
						d.aa.addVideo(jQuery(c).attr("number"), jQuery(this).attr("src"), jQuery(this).attr("url"), jQuery(this).attr("x"), jQuery(this).attr("y"), jQuery(this).attr("width"), jQuery(this).attr("height"));
					});
					jQuery(this).find("image").each(function() {
						d.aa.addImage(jQuery(c).attr("number"), jQuery(this).attr("src"), jQuery(this).attr("x"), jQuery(this).attr("y"), jQuery(this).attr("width"), jQuery(this).attr("height"), jQuery(this).attr("href"), jQuery(this).attr("hoversrc"));
					});
				});
			}
		}) : d.eh();
		d.ta.PreviewMode && (d.Lh(), d.jf());
	};
	this.jf = function() {
		this.aa.ka.find(".flexpaper_fisheye").hide();
	};
	this.Mg = function() {
		this.vh();
	};
	this.Lh = function() {
		jQuery(this.ta.ga).css("padding-top", "20px");
		jQuery("#" + this.Ja).hide();
	};
	this.il = function() {
		jQuery(this.ta.ga).css("padding-top", "0px");
		jQuery("#" + this.Ja).show();
	};
	this.eh = function() {
		this.hb = eb.platform.Nb && !this.ta.PreviewMode;
		this.Dd = n;
		this.te = !eb.platform.touchonlydevice;
		this.tg = 1;
		this.aa.Zf = n;
		jQuery(this.toolbar.ca).css({
			"border-radius": "3px",
			"-moz-border-radius": "3px"
		});
		jQuery(this.toolbar.ca).css({
			"margin-left": "auto",
			"margin-right": "auto"
		});
		this.ta.config.document.PanelColor && (this.fd = this.ta.config.document.PanelColor);
		this.backgroundColor = this.ta.config.document.BackgroundColor ? this.ta.config.document.BackgroundColor : "#222222";
		this.backgroundImage || jQuery(this.ta.ka).css("background-color", this.backgroundColor);
		this.fd ? jQuery(this.toolbar.ca).css("background-color", this.fd) : eb.platform.touchonlydevice ? jQuery(this.toolbar.ca).addClass("flexpaper_toolbarios_gradients") : jQuery(this.toolbar.ca).css("background-color", "#555555");
		this.fh();
	};
	this.fh = function() {
		if (eb.platform.touchonlydevice) {
			var c = eb.platform.Nb ? -5 : -1,
				d = eb.platform.Nb ? 7 : 15,
				k = eb.platform.Nb ? 40 : 60;
			jQuery(this.toolbar.ca).html((this.toolbar.ta.config.document.ViewModeToolsVisible ? String.format("<img src='{0}' style='margin-left:{1}px' class='flexpaper_tbbutton_large flexpaper_twopage flexpaper_tbbutton_pressed flexpaper_bttnBookView flexpaper_viewmode'>", this.Hf, d) + String.format("<img src='{0}' class='flexpaper_bttnSinglePage flexpaper_tbbutton_large flexpaper_singlepage flexpaper_viewmode' style='margin-left:{1}px;'>", this.Se, c) + String.format("<img src='{0}' style='margin-left:{1}px;' class='flexpaper_tbbutton_large flexpaper_thumbview flexpaper_bttnThumbView flexpaper_viewmode' >", this.Te, c) + "" : "") + (this.toolbar.ta.config.document.ZoomToolsVisible ? String.format("<img class='flexpaper_tbbutton_large flexpaper_bttnZoomIn' src='{0}' style='margin-left:{1}px;' />", this.Ue, d) + String.format("<img class='flexpaper_tbbutton_large flexpaper_bttnZoomOut' src='{0}' style='margin-left:{1}px;' />", this.Ve, c) + String.format("<img class='flexpaper_tbbutton_large flexpaper_bttnFullscreen' src='{0}' style='margin-left:{1}px;' />", this.Pe, c) + "" : "") + (this.toolbar.ta.config.document.NavToolsVisible ? String.format("<img src='{0}' class='flexpaper_tbbutton_large flexpaper_previous flexpaper_bttnPrevPage' style='margin-left:{0}px;'/>", this.Re, d) + String.format("<input type='text' class='flexpaper_tbtextinput_large flexpaper_currPageNum flexpaper_txtPageNumber' value='1' style='width:{0}px;' />", k) + String.format("<div class='flexpaper_lblTotalPages flexpaper_tblabel_large flexpaper_numberOfPages'> / </div>") + String.format("<img src='{0}' class='flexpaper_bttnPrevNext flexpaper_tbbutton_large flexpaper_next'/>", this.Qe) + "" : "") + (this.toolbar.ta.config.document.SearchToolsVisible ? String.format("<input type='text' class='flexpaper_txtSearch flexpaper_tbtextinput_large' style='margin-left:{1}px;width:130px;' />", d) + String.format("<img src='{0}' class='flexpaper_bttnFind flexpaper_find flexpaper_tbbutton_large' style=''/>", this.Oe) + "" : ""));
			jQuery(this.toolbar.ca).removeClass("flexpaper_toolbarstd");
			jQuery(this.toolbar.ca).addClass("flexpaper_toolbarios");
			jQuery(this.toolbar.ca).parent().parent().css({
				"background-color": this.backgroundColor
			});
		} else {
			jQuery(this.toolbar.ca).css("margin-top", "15px"), c = this.ta.renderer.config.signature && 0 < this.ta.renderer.config.signature.length, jQuery(this.toolbar.ca).html(String.format("<img style='margin-left:10px;' src='{0}' class='flexpaper_bttnPrint flexpaper_tbbutton print'/>", this.sl) + (this.aa.document.PDFFile && 0 < this.aa.document.PDFFile.length && !c ? String.format("<img src='{0}' class='flexpaper_bttnDownload flexpaper_tbbutton download'/>", this.ol) : "") + String.format("<img src='{0}' id='{1}' class='flexpaper_tbseparator' />", this.le, this.zj) + (this.ta.config.document.ViewModeToolsVisible ? String.format("<img style='margin-left:10px;' src='{1}' class='flexpaper_tbbutton {0} flexpaper_bttnBookView flexpaper_twopage flexpaper_tbbuttonviewmode flexpaper_viewmode' />", "FlipView" == this.ta.ub ? "flexpaper_tbbutton_pressed" : "", this.Cl) + String.format("<img src='{1}' class='flexpaper_tbbutton {0} flexpaper_bttnSinglePage flexpaper_singlepage flexpaper_tbbuttonviewmode flexpaper_viewmode' />", "Portrait" == this.ta.ub ? "flexpaper_tbbutton_pressed" : "", this.nl) + String.format("<img src='{0}' id='{1}' class='flexpaper_tbseparator' />", this.le, this.Bj) : "") + (this.ta.config.document.ZoomToolsVisible ? String.format("<div class='flexpaper_zoomSlider flexpaper_slider'><div class='flexpaper_handle'></div></div>") + String.format("<input type='text' class='flexpaper_tbtextinput flexpaper_txtZoomFactor' style='width:40px;' />") + String.format("<img style='margin-left:10px;' class='flexpaper_tbbutton flexpaper_bttnFullscreen' src='{0}' />", this.pl) : "") + (this.ta.config.document.NavToolsVisible ? String.format("<img src='{0}' class='flexpaper_tbbutton flexpaper_previous flexpaper_bttnPrevPage'/>", this.rl) + String.format("<input type='text' class='flexpaper_txtPageNumber flexpaper_tbtextinput flexpaper_currPageNum' value='1' style='width:50px;text-align:right;' />") + String.format("<div class='flexpaper_lblTotalPages flexpaper_tblabel flexpaper_numberOfPages'> / </div>") + String.format("<img src='{0}' class='flexpaper_bttnPrevNext flexpaper_tbbutton flexpaper_next'/>", this.ul) + String.format("<img src='{0}' id='{1}' class='flexpaper_tbseparator' />", this.le, this.yj) : "") + (this.ta.config.document.CursorToolsVisible ? String.format("<img style='margin-top:5px;margin-left:6px;' src='{0}' class='flexpaper_tbbutton flexpaper_bttnTextSelect'/>", this.Bl) + String.format("<img style='margin-top:4px;' src='{0}' class='flexpaper_tbbutton flexpaper_tbbutton_pressed flexpaper_bttnHand'/>", this.ql) + String.format("<img src='{0}' id='{1}' class='flexpaper_tbseparator' />", this.le, this.xj) : "") + (this.ta.config.document.SearchToolsVisible ? String.format("<input id='{0}' type='text' class='flexpaper_tbtextinput flexpaper_txtSearch' style='width:40px;margin-left:4px' />") + String.format("<img src='{0}' class='flexpaper_find flexpaper_tbbutton flexpaper_bttnFind' />", this.Dl) : "") + String.format("<img src='{0}' id='{1}' class='flexpaper_tbseparator' />", this.le, this.Aj));
		}
	};
	this.bindEvents = function() {
		var c = this;
		eb.platform.touchonlydevice ? (jQuery(c.toolbar.ca).find(".flexpaper_bttnPrint").on("mousedown touchstart", function() {
			c.Of != c.qa && jQuery(this).attr("src", c.Of);
		}), jQuery(c.toolbar.ca).find(".flexpaper_bttnPrint").on("mouseup touchend", function() {
			c.Nf != c.qa && jQuery(this).attr("src", c.Nf);
		}), jQuery(c.toolbar.ca).find(".flexpaper_bttnBookView").on("mousedown touchstart", function() {
			c.Ne != c.qa && jQuery(this).attr("src", c.Ne);
		}), jQuery(c.toolbar.ca).find(".flexpaper_bttnBookView").on("mouseup touchend", function() {
			c.Ne != c.qa && jQuery(this).attr("src", c.Hf);
		}), jQuery(c.toolbar.ca).find(".flexpaper_bttnSinglePage").on("mousedown touchstart", function() {
			c.Pf != c.qa && jQuery(this).attr("src", c.Pf);
		}), jQuery(c.toolbar.ca).find(".flexpaper_bttnSinglePage").on("mouseup touchend", function() {
			c.Se != c.qa && jQuery(this).attr("src", c.Se);
		}), jQuery(c.toolbar.ca).find(".flexpaper_bttnThumbView").on("mousedown touchstart", function() {
			c.Qf != c.qa && jQuery(this).attr("src", c.Qf);
		}), jQuery(c.toolbar.ca).find(".flexpaper_bttnThumbView").on("mouseup touchend", function() {
			c.Te != c.qa && jQuery(this).attr("src", c.Te);
		}), jQuery(c.toolbar.ca).find(".flexpaper_bttnZoomIn").on("mousedown touchstart", function() {
			c.Rf != c.qa && jQuery(this).attr("src", c.Rf);
		}), jQuery(c.toolbar.ca).find(".flexpaper_bttnZoomIn").on("mouseup touchend", function() {
			c.Ue != c.qa && jQuery(this).attr("src", c.Ue);
		}), jQuery(c.toolbar.ca).find(".flexpaper_bttnZoomOut").on("mousedown touchstart", function() {
			c.Sf != c.qa && jQuery(this).attr("src", c.Sf);
		}), jQuery(c.toolbar.ca).find(".flexpaper_bttnZoomOut").on("mouseup touchend", function() {
			c.Ve != c.qa && jQuery(this).attr("src", c.Ve);
		}), jQuery(c.toolbar.ca).find(".flexpaper_bttnFullscreen").on("mousedown touchstart", function() {
			c.Jf != c.qa && jQuery(this).attr("src", c.Jf);
		}), jQuery(c.toolbar.ca).find(".flexpaper_bttnFullscreen").on("mouseup touchend", function() {
			c.Pe != c.qa && jQuery(this).attr("src", c.Pe);
		}), jQuery(c.toolbar.ca).find(".flexpaper_bttnPrevPage").on("mousedown touchstart", function() {
			c.Lf != c.qa && jQuery(this).attr("src", c.Lf);
		}), jQuery(c.toolbar.ca).find(".flexpaper_bttnPrevPage").on("mouseup touchend", function() {
			c.Re != c.qa && jQuery(this).attr("src", c.Re);
		}), jQuery(c.toolbar.ca).find(".flexpaper_bttnNextPage").on("mousedown touchstart", function() {
			c.Kf != c.qa && jQuery(this).attr("src", c.Kf);
		}), jQuery(c.toolbar.ca).find(".flexpaper_bttnNextPage").on("mouseup touchend", function() {
			c.Qe != c.qa && jQuery(this).attr("src", c.Qe);
		}), jQuery(c.toolbar.ca).find(".flexpaper_bttnFind").on("mousedown touchstart", function() {
			c.If != c.qa && jQuery(this).attr("src", c.If);
		}), jQuery(c.toolbar.ca).find(".flexpaper_bttnFind").on("mouseup touchend", function() {
			c.Oe != c.qa && jQuery(this).attr("src", c.Oe);
		})) : (jQuery(c.toolbar.ca).find(".flexpaper_txtSearch").on("focus", function() {
			40 >= jQuery(this).width() && (jQuery(c.toolbar.ca).animate({
				width: jQuery(c.toolbar.ca).width() + 60
			}, 100), jQuery(this).animate({
				width: jQuery(this).width() + 60
			}, 100));
		}), jQuery(c.toolbar.ca).find(".flexpaper_txtSearch").on("blur", function() {
			40 < jQuery(this).width() && (jQuery(c.toolbar.ca).animate({
				width: jQuery(c.toolbar.ca).width() - 60
			}, 100), jQuery(this).animate({
				width: 40
			}, 100));
		}));
		jQuery(c.toolbar.ca).find(".flexpaper_bttnZoomIn").bind("click", function() {
			c.ta.pages.ad(n);
		});
		jQuery(c.toolbar.ca).find(".flexpaper_bttnZoomOut").bind("click", function() {
			c.ta.pages.kc();
		});
		0 == c.aa.ka.find(".flexpaper_socialsharedialog").length && c.aa.ka.prepend(String.format("<div id='modal-socialshare' class='modal-content flexpaper_socialsharedialog' style='overflow:hidden;'><font style='color:#000000;font-size:11px'><img src='{0}' align='absmiddle' />&nbsp;<b>{15}</b></font><div style='width:530px;height:307px;margin-top:5px;padding-top:5px;padding-left:5px;background-color:#ffffff;box-shadow: 0px 2px 10px #aaa'><div style='position:absolute;left:20px;top:42px;color:#000000;font-weight:bold;'>{6}</div><div style='position:absolute;left:177px;top:42px;color:#000000;font-weight:bold;'><hr size='1' style='width:350px'/></div><div style='position:absolute;left:20px;top:62px;color:#000000;font-weight:bold;'><select class='flexpaper_ddlSharingOptions'><option>{7}</option><option>{16}</option></select></div><div style='position:absolute;left:175px;top:62px;color:#000000;font-weight:bold;'><input type='text' readonly style='width:355px;' class='flexpaper_socialsharing_txtUrl' /></div><div style='position:absolute;left:20px;top:102px;color:#000000;font-weight:bold;'>{8}</div><div style='position:absolute;left:177px;top:107px;color:#000000;font-weight:bold;'><hr size='1' style='width:350px'/></div><div style='position:absolute;left:20px;top:118px;color:#000000;font-size:10px;'>{9}</div><div style='position:absolute;left:20px;top:148px;color:#000000;font-weight:bold;'><input type='text' style='width:139px;' value='&lt;{10}&gt;' class='flexpaper_txtPublicationTitle' /></div><div style='position:absolute;left:165px;top:146px;color:#000000;'><img src='{1}' class='flexpaper_socialshare_twitter' style='cursor:pointer;' /></div><div style='position:absolute;left:200px;top:146px;color:#000000;'><img src='{2}' class='flexpaper_socialshare_facebook' style='cursor:pointer;' /></div><div style='position:absolute;left:235px;top:146px;color:#000000;'><img src='{3}' class='flexpaper_socialshare_googleplus' style='cursor:pointer;' /></div><div style='position:absolute;left:270px;top:146px;color:#000000;'><img src='{4}' class='flexpaper_socialshare_tumblr' style='cursor:pointer;' /></div><div style='position:absolute;left:305px;top:146px;color:#000000;'><img src='{5}' class='flexpaper_socialshare_linkedin' style='cursor:pointer;' /></div><div style='position:absolute;left:20px;top:192px;color:#000000;font-weight:bold;'>{11}</div><div style='position:absolute;left:20px;top:208px;color:#000000;font-size:10px;'>{12}</div><div style='position:absolute;left:20px;top:228px;color:#000000;font-size:10px;'><input type='radio' name='InsertCode' class='flexpaper_radio_miniature' checked />&nbsp;{13}&nbsp;&nbsp;&nbsp;&nbsp;<input type='radio' name='InsertCode' class='flexpaper_radio_fullembed' />&nbsp;{14}</div><div style='position:absolute;left:20px;top:251px;color:#000000;font-size:10px;'><textarea class='flexpaper_txtEmbedCode' readonly style='width:507px;height:52px'></textarea></div></div></div>", c.Al, c.zl, c.vl, c.wl, c.yl, c.xl, c.aa.toolbar.ya(c.aa.toolbar.Ya, "CopyUrlToPublication", "Copy URL to publication"), c.aa.toolbar.ya(c.aa.toolbar.Ya, "DefaultStartPage", "Default start page"), c.aa.toolbar.ya(c.aa.toolbar.Ya, "ShareOnSocialNetwork", "Share on Social Network"), c.aa.toolbar.ya(c.aa.toolbar.Ya, "ShareOnSocialNetworkDesc", "You can easily share this publication to social networks. Just click on the appropriate button below."), c.aa.toolbar.ya(c.aa.toolbar.Ya, "SharingTitle", "Sharing Title"), c.aa.toolbar.ya(c.aa.toolbar.Ya, "EmbedOnSite", "Embed on Site"), c.aa.toolbar.ya(c.aa.toolbar.Ya, "EmbedOnSiteDesc", "Use the code below to embed this publication to your website."), c.aa.toolbar.ya(c.aa.toolbar.Ya, "EmbedOnSiteMiniature", "Linkable Miniature"), c.aa.toolbar.ya(c.aa.toolbar.Ya, "EmbedOnSiteFull", "Full Publication"), c.aa.toolbar.ya(c.aa.toolbar.Ya, "Share", "Share"), c.aa.toolbar.ya(c.aa.toolbar.Ya, "StartOnCurrentPage", "Start on current page")));
		c.aa.ka.find(".flexpaper_radio_miniature, .flexpaper_radio_fullembed, .flexpaper_ddlSharingOptions").on("change", function() {
			c.Df();
		});
		c.aa.ka.find(".flexpaper_txtPublicationTitle").on("focus", function(c) {
			-1 != jQuery(c.target).val().indexOf("Sharing Title") && jQuery(c.target).val("");
		});
		c.aa.ka.find(".flexpaper_txtPublicationTitle").on("blur", function(c) {
			0 == jQuery(c.target).val().length && jQuery(c.target).val("<Sharing Title>");
		});
		c.aa.ka.find(".flexpaper_txtPublicationTitle").on("keydown", function() {
			c.Df();
		});
		c.Df();
		jQuery(c.toolbar.ca).find(".flexpaper_bttnSocialShare").bind("click", function() {
			c.Df();
			jQuery("#modal-socialshare").css("background-color", "#dedede");
			jQuery("#modal-socialshare").smodal({
				minHeight: 350,
				minWidth: 550,
				appendTo: c.aa.ka
			});
			jQuery("#modal-socialshare").parent().css("background-color", "#dedede");
		});
		jQuery(c.toolbar.ca).find(".flexpaper_bttnBookView").bind("click", function() {
			eb.browser.msie && 8 >= eb.browser.version ? c.ta.switchMode("BookView", c.ta.getCurrPage()) : c.ta.switchMode("FlipView", c.ta.getCurrPage() + 1);
			jQuery(this).addClass("flexpaper_tbbutton_pressed");
		});
		c.aa.ka.find(".flexpaper_socialsharing_txtUrl, .flexpaper_txtEmbedCode").bind("focus", function() {
			jQuery(this).select();
		});
		c.aa.ka.find(".flexpaper_socialsharing_txtUrl, .flexpaper_txtEmbedCode").bind("mouseup", ca(v));
		c.aa.ka.find(".flexpaper_socialshare_twitter").bind("mousedown", function() {
			window.open("https://twitter.com/intent/tweet?url=" + escape(c.rd(v)) + "&text=" + escape(c.ef()), "_flexpaper_exturl");
			c.aa.ga.trigger("onSocialMediaShareClicked", "Twitter");
		});
		c.aa.ka.find(".flexpaper_socialshare_facebook").bind("mousedown", function() {
			window.open("http://www.facebook.com/sharer.php?u=" + escape(c.rd(v), "_flexpaper_exturl"));
			c.aa.ga.trigger("onSocialMediaShareClicked", "Facebook");
		});
		c.aa.ka.find(".flexpaper_socialshare_googleplus").bind("mousedown", function() {
			window.open("https://plus.google.com/share?url=" + escape(c.rd(v)), "_flexpaper_exturl");
			c.aa.ga.trigger("onSocialMediaShareClicked", "GooglePlus");
		});
		c.aa.ka.find(".flexpaper_socialshare_tumblr").bind("mousedown", function() {
			window.open("http://www.tumblr.com/share/link?name=" + escape(c.ef()) + "&url=" + escape(c.rd(v)), "_flexpaper_exturl");
			c.aa.ga.trigger("onSocialMediaShareClicked", "Tumblr");
		});
		c.aa.ka.find(".flexpaper_socialshare_linkedin").bind("mousedown", function() {
			window.open("http://www.linkedin.com/shareArticle?mini=true&url=" + escape(c.rd(v)) + "&title=" + escape(c.ef()), "_flexpaper_exturl");
			c.aa.ga.trigger("onSocialMediaShareClicked", "LinkedIn");
		});
	};
	this.Df = function() {
		this.aa.ka.find(".flexpaper_txtEmbedCode").val('<iframe frameborder="0"  width="400" height="300"  title="' + this.ef() + '" src="' + this.rd() + '" type="text/html" scrolling="no" marginwidth="0" marginheight="0"></iframe>');
		this.aa.ka.find(".flexpaper_socialsharing_txtUrl").val(this.rd(v));
	};
	this.ef = function() {
		return -1 == this.aa.ka.find(".flexpaper_txtPublicationTitle").val().indexOf("Sharing Title") ? this.aa.ka.find(".flexpaper_txtPublicationTitle").val() : "";
	};
	this.rd = function(c) {
		0 == arguments.length && (c = n);
		var d = this.aa.ka.find(".flexpaper_ddlSharingOptions").prop("selectedIndex"),
			k = this.aa.ka.find(".flexpaper_radio_miniature").is(":checked");
		return window.location.href.toString().substring(0) + (0 < d ? "#page=" + this.aa.getCurrPage() : "") + (0 < d && k && c ? "&" : k && c ? "#" : "") + (k && c ? "PreviewMode=Miniature" : "");
	};
	this.initialize = function() {
		var c = this.aa;
		if (!c.config.document.InitViewMode || c.config.document.InitViewMode && "Zine" == c.config.document.InitViewMode || "TwoPage" == c.config.document.InitViewMode) {
			c.ub = L, c.config.document.MinZoomSize = 1, c.ba = c.ub, "TwoPage" == c.ba && (c.ba = L), c.scale = 1;
		}
		c.config.document.Wh = c.config.document.MinZoomSize;
		c.ka === p && (c.ka = jQuery("<div style='" + c.ga.attr("style") + ";margin-bottom;20px;overflow-x: hidden;overflow-y: hidden;' class='flexpaper_viewer_container'/>"), c.ka = c.ga.wrap(c.ka).parent(), c.ga.css({
			left: "0px",
			top: "0px",
			position: "relative",
			width: "100%",
			height: "100%"
		}).addClass("flexpaper_viewer"), eb.browser.safari && c.ga.css("-webkit-transform", "translateZ(0)"));
		jQuery(c.ga).bind("onCurrentPageChanged", function() {
			c.fisheye && c.Ej();
		});
	};
	this.Gl = function(d) {
		eb.platform.touchonlydevice ? c.switchMode(U, d) : c.switchMode(C, d);
	};
	FlexPaperViewer_HTML.prototype.$h = function(c) {
		var d = this;
		if (d.zb != c) {
			var k = (c - 20 + 1) / 2,
				g = k + 9 + 1,
				u = 1,
				m = d.config.document.PanelColor != p ? d.config.document.PanelColor : "#555555";
			d.fisheye.find(".flexpaper_fisheye_item").parent().parent().remove();
			0 > d.getTotalPages() - c && (g = g + (d.getTotalPages() - c) / 2 + (c - d.getTotalPages()) % 2);
			19 < c ? d.fisheye.find(".flexpaper_fisheye_panelLeft").animate({
				opacity: 1
			}, 150) : d.fisheye.find(".flexpaper_fisheye_panelLeft").animate({
				opacity: 0
			}, 150);
			c < d.getTotalPages() ? d.fisheye.find(".flexpaper_fisheye_panelRight").animate({
				opacity: 1
			}, 150) : d.fisheye.find(".flexpaper_fisheye_panelRight").animate({
				opacity: 0
			}, 150);
			for (i = k; i < g; i++) {
				d.uj(u), u++;
			}
			d.fisheye.find(".flexpaper_fisheye_item, .flexpaper_fisheye_panelLeft, .flexpaper_fisheye_panelRight").bind("mouseover", function() {
				if (!(d.pages.animating || 0 == d.fisheye.css("opacity"))) {
					var c = (1 - Math.min(1, Math.max(0, 1 / d.Fh))) * d.Eh + d.Cb;
					d.fisheye.css({
						"z-index": 12,
						"pointer-events": "auto"
					});
					jQuery(this).parent().parent().parent().find("span").css({
						display: "none"
					});
					jQuery(this).parent().find("span").css({
						display: "inline-block"
					});
					jQuery(this).parent().parent().parent().find("p").remove();
					var e = jQuery(this).context.dataset && 1 == jQuery(this).context.dataset.pageindex ? d.af / 3 : 0;
					jQuery(this).parent().find("span").after(String.format("<p style='width: 0;height: 0;border-left: 7px solid transparent;border-right: 7px solid transparent;border-top: 7px solid {0};margin-top:-35px;margin-left:{1}px;'></p>", m, c / 2 - 14 + e));
				}
			});
			d.fisheye.find(".flexpaper_fisheye_item").bind("mouseout", function(c) {
				d.pages.animating || 0 == d.fisheye.css("opacity") || (d.fg = c.pageX, d.gg = c.pageY, d.Zc = c.target, jQuery(d.Zc).get(0), d.ni(), d.fisheye.css({
					"z-index": 9,
					"pointer-events": "none"
				}), jQuery(this).parent().find("span").css({
					display: "none"
				}), jQuery(this).parent().find("p").remove());
			});
			d.fisheye.find("li").each(function() {
				jQuery(this).bind("mousemove", function(c) {
					if (!(d.pages.animating || 0 < c.om) && d.fisheye.is(":visible")) {
						d.Zc = c.target, d.fg = c.pageX, d.gg = c.pageY, jQuery(d.Zc).get(0), d.hg = n, d.Zh();
					}
				});
			});
			jQuery(d.pages.da + ", " + d.pages.da + "_parent, #" + d.ia).bind("mouseover", function() {
				if (d.fisheye && (d.fisheye.css({
					"z-index": 9,
					"pointer-events": "none"
				}), (eb.browser.msie || eb.browser.safari && 5 > eb.browser.vb) && d.Zc)) {
					d.Zc = p;
					var c = d.fisheye.find("a").find("canvas").data("origwidth"),
						e = d.fisheye.find("a").find("canvas").data("origheight");
					d.fisheye.find("li").each(function() {
						jQuery(this).find("a").css({
							height: e,
							width: c,
							top: d.Cb / 3
						});
						jQuery(this).find("a").find("canvas").css({
							height: e,
							width: c,
							top: d.Cb / 3
						});
					});
				}
			});
		}
		d.zb = c;
	};
	FlexPaperViewer_HTML.prototype.Ej = function() {
		if ((this.sa > this.zb || this.sa <= this.zb - 20) && -1 != this.zb) {
			this.nf(this.sa > this.zb ? 20 : -20);
		}
	};
	FlexPaperViewer_HTML.prototype.nf = function(c) {
		var d = this;
		0 != c && d.$h(d.zb + c);
		window.setTimeout(function() {
			d.zd = (d.zb - 20 + 1) / 2 + 1;
			d.Qg = d.zd + 9;
			0 > d.getTotalPages() - d.zb && (d.Qg = d.Qg + (d.getTotalPages() - d.zb) / 2 + (d.zb - d.getTotalPages()) % 2);
			d.renderer.pd(d, d.zd, 2 * d.Yc);
		}, 300);
	};
	FlexPaperViewer_HTML.prototype.uj = function(c) {
		var d = 0 == i ? 1 : 2 * i + 1,
			k = this;
		if (k.fisheye) {
			var g = k.config.document.PanelColor != p ? k.config.document.PanelColor : "#555555",
				u = "",
				u = 1 == d ? "&nbsp;&nbsp;" + c + "&nbsp;&nbsp;" : d == k.getTotalPages() && 0 == k.getTotalPages() % 2 ? (d - 1).toString() : d - 1 + "-" + d;
			c = jQuery(String.format("<li><a style='height:{2}px;width:{7}px;top:{9}px;' class='flexpaper_thumbitem'><span style='margin-left:{8}px;background-color:{0}'>{4}</span><canvas data-pageIndex='{5}' data-ThumbIndex='{6}' class='flexpaper_fisheye_item' style='pointer-events: auto;' /></a></li>", g, k.Md, 0.8 * k.Yc, k.af, u, d, c, k.Cb, 1 == d ? k.af : 0, k.Cb / 3));
			c.insertBefore(k.fisheye.find(".flexpaper_fisheye_panelRight").parent());
			c.find(".flexpaper_fisheye_item").css({
				opacity: 0
			});
			jQuery(c).bind("mousedown", function() {
				1 != !k.scale && (k.fisheye && k.fisheye.css({
					"z-index": 9,
					"pointer-events": "none"
				}), d > k.getTotalPages() && (d = k.getTotalPages()), k.gotoPage(d));
			});
		}
	};
	this.vh = function() {
		var c = this.aa;
		0 < c.ka.find(".flexpaper_fisheye").length && c.ka.find(".flexpaper_fisheye").remove();
		c.zb = -1;
		var d = 0;
		0 < c.getDimensions(0).length && (d = c.getDimensions(0)[0].Aa / c.getDimensions(0)[0].Oa - 0.3);
		c.zm = 25;
		c.Yc = 0.25 * c.ga.height();
		c.af = 0.41 * c.Yc;
		c.Md = jQuery(c.ga).offset().top + jQuery(c.pages.da).height() - c.ka.offset().top;
		c.Fh = 1.25 * c.Yc;
		c.Cb = c.Yc / (3.5 - d);
		c.Xj = 2.5 * c.Cb;
		c.Yj = -(c.Cb / 3);
		c.ka.append(jQuery(String.format("<div class='flexpaper_fisheye' style='position:absolute;pointer-events: none;top:{1}px;z-index:12;left:{4}px;'><ul><li><div class='flexpaper_fisheye_panelLeft' style='pointer-events: auto;position:relative;-moz-border-radius-topleft: 10px;border-top-left-radius: 10px;-moz-border-radius-bottomleft: 10px;border-bottom-left-radius: 10px;background-color:{0};left:0px;width:22px;'><div style='position:absolute;height:100px;width:100px;left:0px;top:-40px;'></div><div class='flexpaper_fisheye_leftArrow' style='position:absolute;top:20%;left:3px'></div></div></li><li><div class='flexpaper_fisheye_panelRight' style='pointer-events: auto;position:relative;-moz-border-radius-topright: 10px;border-top-right-radius: 10px;-moz-border-radius-bottomright: 10px;border-bottom-right-radius: 10px;background-color:{0};left:0px;width:22px;'><div style='position:absolute;height:100px;width:100px;left:0px;top:-40px;'></div><div class='flexpaper_fisheye_rightArrow' style='position:absolute;top:20%;left:3px;'></div></div></li></ul></div>", c.config.document.PanelColor != p ? c.config.document.PanelColor : "#555555", c.Md, 0.8 * c.Yc, c.af, c.Yj)));
		c.fisheye = c.ka.find(".flexpaper_fisheye");
		c.fisheye.css({
			top: c.Md - (c.fisheye.find(".flexpaper_fisheye_panelLeft").offset().top - jQuery(c.fisheye).offset().top) + c.fisheye.find(".flexpaper_fisheye_panelLeft").height() / 2
		});
		c.Eh = c.Xj - c.Cb;
		c.fg = -1;
		c.gg = -1;
		c.eg = v;
		c.hg = v;
		c.ue = c.Cb - 0.4 * c.Cb;
		c.ym = c.ue / c.Cb;
		c.fisheye.find(".flexpaper_fisheye_panelLeft").bind("mousedown", function() {
			c.nf(-20);
		});
		c.fisheye.find(".flexpaper_fisheye_panelRight").bind("mousedown", function() {
			c.nf(20);
		});
		36 < c.ue && (c.ue = 36);
		c.fisheye.find(".flexpaper_fisheye_panelLeft").css({
			opacity: 0,
			height: c.ue + "px",
			top: "-10px"
		});
		c.fisheye.find(".flexpaper_fisheye_panelRight").css({
			height: c.ue + "px",
			top: "-10px"
		});
		if ($("#knowledgeCont").attr("id")) {
			c.Md = c.Md + 230;
		}
		c.fisheye.css({
			top: c.Md - (c.fisheye.find(".flexpaper_fisheye_panelLeft").offset().top - jQuery(c.fisheye).offset().top) + c.fisheye.find(".flexpaper_fisheye_panelLeft").height() / 3
		});
		c.Dh = 30 < c.fisheye.find(".flexpaper_fisheye_panelLeft").height() ? 11 : 0.35 * c.fisheye.find(".flexpaper_fisheye_panelLeft").height();
		c.fisheye.find(".flexpaper_fisheye_leftArrow").Dg(c.Dh);
		c.fisheye.find(".flexpaper_fisheye_rightArrow").vf(c.Dh);
		jQuery(c).unbind("onThumbPanelThumbAdded");
		jQuery(c).bind("onThumbPanelThumbAdded", function(d, f) {
			var g = c.fisheye.find(String.format('*[data-thumbIndex="{0}"]', f.Ad));
			g.data("pageIndex");
			var m = (f.Ad - 1) % 10;
			g && g.animate({
				opacity: 1
			}, 300);
			c.zd < c.Qg && (c.zb - 20 + 1) / 2 + m + 2 > c.zd && (c.Jl ? (c.zd++, c.Jl = v) : c.zd = (c.zb - 20 + 1) / 2 + m + 2, c.renderer.pd(c, c.zd, 2 * c.Yc));
			0 == m && g.height() - 10 < c.fisheye.find(".flexpaper_fisheye_panelRight").height() && (c.fisheye.find(".flexpaper_fisheye_panelLeft").css("top", c.fisheye.find(".flexpaper_fisheye_panelLeft").height() - g.height() + 5 + "px"), c.fisheye.find(".flexpaper_fisheye_panelLeft").height(c.fisheye.find(".flexpaper_fisheye_panelLeft").height() - 3), c.fisheye.find(".flexpaper_fisheye_panelRight").css("top", c.fisheye.find(".flexpaper_fisheye_panelRight").height() - g.height() + 5 + "px"), c.fisheye.find(".flexpaper_fisheye_panelRight").height(c.fisheye.find(".flexpaper_fisheye_panelRight").height() - 3));
		});
		c.$h(19);
		c.PreviewMode || c.nf(0);
		1 != c.scale && c.fisheye.animate({
			opacity: 0
		}, 0);
	};
	this.tf = function() {
		if (c.ba == L && window.zine) {
			c.document.StartAtPage && !c.Me && (c.Me = 0 != c.document.StartAtPage % 2 ? c.document.StartAtPage - 1 : c.document.StartAtPage);
			c.Cf = v;
			var d = 900;
			"fast" == c.ha.Bi && (d = 500);
			"slow" == c.ha.Bi && (d = 2000);
			c.Ha = jQuery(c.pages.da).turn({
				gradients: !eb.platform.android,
				acceleration: n,
				elevation: 50,
				duration: d,
				page: c.Me ? c.Me : 1,
				display: !c.ha.hb ? "double" : "single",
				pages: c.getTotalPages(),
				cornerDragging: c.document.EnableCornerDragging,
				when: {
					turning: function(d, e) {
						c.pages.animating = n;
						c.pages.Fe = p;
						c.pages.ja = 0 == e % 2 ? e + 1 : e;
						if (1 == e && !c.ha.hb) {
							var f = c.Cf ? 600 : 0;
							jQuery(c.pages.da + "_parent").transition({
								x: -(c.pages.Fc() / 4)
							}, f, "ease", x());
						} else {
							c.ha.hb || jQuery(c.pages.da + "_parent").transition({
								x: 0
							}, 600, "ease", x());
						}
						c.sa = 1 < e ? c.pages.ja : e;
						c.renderer.Xc && c.Cf && c.pages.ud(e - 1);
						c.renderer.Xc && c.Cf && c.pages.ud(e);
						c.ba == L && (c.pages.pages[e - 1] && (!c.pages.pages[e - 1].Lb && !c.pages.pages[e - 1].za) && (c.pages.pages[e - 1].Lb = n, c.pages.pages[e - 1].rc()), e < c.getTotalPages() && (c.pages.pages[e] && !c.pages.pages[e].Lb && !c.pages.pages[e].za) && (c.pages.pages[e].Lb = n, c.pages.pages[e].rc()));
					},
					turned: function(d, e) {
						c.Cf = n;
						c.pages.animating = v;
						c.Vc(e);
						c.pages.cc();
						c.ga.trigger("onCurrentPageChanged", e);
						c.Ug != p && (c.Ug(), c.Ug = p);
					},
					destroyed: function() {
						c.Jj && c.ga.parent().remove();
					}
				}
			});
			jQuery(c.Ha).bind("cornerActivated", function() {
				c.fisheye && c.fisheye.css({
					"z-index": 9,
					"pointer-events": "none"
				});
			});
			jQuery(c.ca).trigger("onScaleChanged", 0.2);
		}
		if (c.backgroundColor && -1 == c.backgroundColor.indexOf("[") && !this.backgroundImage) {
			d = ja(this.backgroundColor), d = "rgba(" + d.r + "," + d.g + "," + d.b + "," + (this.kh != p ? parseFloat(this.kh) : 1) + ")", jQuery(this.ta.ga).css("background", d), jQuery(this.Rc).css("background", d);
		} else {
			if (c.backgroundColor && 0 <= c.backgroundColor.indexOf("[") && !this.backgroundImage) {
				var f = c.backgroundColor.split(",");
				f[0] = f[0].toString().replace("[", "");
				f[0] = f[0].toString().replace("]", "");
				f[0] = f[0].toString().replace(" ", "");
				f[1] = f[1].toString().replace("[", "");
				f[1] = f[1].toString().replace("]", "");
				f[1] = f[1].toString().replace(" ", "");
				d = f[0].toString().substring(0, f[0].toString().length);
				f = f[1].toString().substring(0, f[1].toString().length);
				jQuery(c.ta.ga).css("backgroundImage", "linear-gradient(top, " + d + ", " + f + ")");
			}
		}
		c.ba == L && !eb.platform.touchonlydevice && c.ha.Mg && c.ha.te ? (c.ha.vh(), c.PreviewMode && c.ha.jf()) : (c.fisheye && (c.fisheye.remove(), c.fisheye = p), c.zb = -1);
		FlexPaperViewer_HTML.prototype.Kj = function(c, d, e, f) {
			c = e - c;
			d = f - d;
			return Math.sqrt(c * c + d * d);
		};
		FlexPaperViewer_HTML.prototype.turn = function(c) {
			this.Ha && (1 == arguments.length && this.Ha.turn(arguments[0]), 2 == arguments.length && this.Ha.turn(arguments[0], arguments[1]));
		};
		FlexPaperViewer_HTML.prototype.Zh = function() {
			this.eg || (this.eg = n, this.Ch && window.clearTimeout(this.Ch), this.Ch = window.setTimeout(this.Wj(this), 40));
		};
		FlexPaperViewer_HTML.prototype.Wj = function(c) {
			c.ni();
			c.eg = v;
			c.hg && (c.hg = v, c.Zh());
		};
		FlexPaperViewer_HTML.prototype.ni = function() {
			var c = this;
			c.fisheye.find("li").each(function() {
				var d = c.Zc;
				if (!(eb.browser.msie || eb.browser.safari && 5 > eb.browser.vb) || c.Zc) {
					if ("IMG" != jQuery(d).get(0).tagName && "DIV" != jQuery(d).get(0).tagName && "CANVAS" != jQuery(d).get(0).tagName) {
						c.fisheye.find("li").each(function() {
							var d = this;
							requestAnim(function() {
								jQuery(d).find("a").css({
									width: c.Cb,
									top: c.Cb / 3
								});
							}, 10);
						});
					} else {
						var d = jQuery(this).offset().left + jQuery(this).outerWidth() / 2,
							e = jQuery(this).offset().top + jQuery(this).outerHeight() / 2,
							f = (1 - Math.min(1, Math.max(0, c.Kj(d, e, c.fg, c.gg) / c.Fh))) * c.Eh + c.Cb,
							d = jQuery(this).find("a").find("canvas").data("origwidth"),
							e = jQuery(this).find("a").find("canvas").data("origheight"),
							g = f / d;
						if (d && e) {
							var y = this;
							eb.browser.msie || eb.browser.safari && 5 > eb.browser.vb ? (jQuery(this).find("a").animate({
								height: e * g,
								width: f,
								top: f / 3
							}, 0), jQuery(this).find("a").find("canvas").css({
								height: e * g,
								width: f,
								top: f / 3
							}), c.Jm = c.Zc) : requestAnim(function() {
								jQuery(y).find("a").css({
									width: f,
									top: f / 3
								});
							}, 10);
						}
					}
				}
			});
		};
		jQuery(c.toolbar.ca).css("visibility", "visible");
		c.fisheye && c.fisheye.css({
			"z-index": 9,
			"pointer-events": "none"
		});
	};
	this.Bb = function() {
		c.Ha.turn("destroy");
		delete c.Ha;
	};
	this.xf = function() {
		c.Ha = p;
	};
	this.switchMode = function(d, f) {
		c.Ha && c.Ha.turn("destroy");
		c.Ha = p;
		d == C || d == U ? (c.Oc = c.ga.height(), c.Oc = c.Oc - jQuery(c.ca).outerHeight() + 20, c.ga.height(c.Oc)) : (c.Me = 0 != f % 2 ? f - 1 : f, c.Oc = p, c.ga.css({
			left: "0px",
			top: "0px",
			position: "relative",
			width: "100%",
			height: "100%"
		}));
		c.ba == L && d != L && (c.config.document.MinZoomSize = 1, jQuery(c.pages.da).turn("destroy"), c.fisheye && c.fisheye.remove());
		d != L && c.config.document.Wh && (c.config.document.MinZoomSize = c.config.document.Wh);
		d == L && (c.scale = 1, c.ba = L);
	};
	this.gotoPage = function(d, f) {
		c.ba == L && c.pages.ik(d, f);
	};
	this.Vc = function() {
		if (c.ba == L) {
			if (1 < c.pages.ja && 1 == c.scale ? jQuery(c.pages.da + "_panelLeft").animate({
				opacity: 1
			}, 100) : 1 == c.pages.ja && jQuery(c.pages.da + "_panelLeft").animate({
				opacity: 0
			}, 100), c.pages.ja <= c.getTotalPages() && 1.1 >= c.scale) {
				1 < c.getTotalPages() && jQuery(c.pages.da + "_panelRight").animate({
					opacity: 1
				}, 100), c.fisheye && "1" != c.fisheye.css("opacity") && window.setTimeout(function() {
					1.1 >= c.scale && (c.fisheye.show(), c.fisheye.animate({
						opacity: 1
					}, 100));
				}, 700);
			} else {
				if (1.1 < c.scale || c.pages.ja + 2 >= c.getTotalPages()) {
					jQuery(c.pages.da + "_panelRight").animate({
						opacity: 0
					}, 100), 1 == c.scale && 0 == c.getTotalPages() % 2 && c.pages.ja - 1 <= c.getTotalPages() ? c.fisheye && (c.fisheye.show(), c.fisheye.animate({
						opacity: 1
					}, 100)) : c.fisheye && c.fisheye.animate({
						opacity: 0
					}, 0, function() {
						c.fisheye.hide();
					});
				}
			}
		}
	};
	this.Ng = function() {
		this.aa.fisheye && (this.Zj = this.aa.fisheye.css("margin-left"), this.aa.fisheye.animate({
			"margin-left": parseFloat(this.aa.fisheye.css("margin-left")) + 0.5 * this.aa.Ta.width() + "px"
		}, 200));
	};
	this.lf = function() {
		this.aa.fisheye && this.aa.fisheye.animate({
			"margin-left": parseFloat(this.Zj) + "px"
		}, 200);
	};
	this.resize = function(d, f, g, l) {
		if (c.ba == L) {
			c.ga.css({
				width: d,
				height: f - 35
			});
			d = c.ga.width();
			f = c.ga.height();
			d - 5 < jQuery(document.body).width() && d + 5 > jQuery(document.body).width() && f + 37 - 5 < jQuery(document.body).height() && f + 37 + 5 > jQuery(document.body).height() ? c.ka.css({
				width: "100%",
				height: "100%"
			}) : (g == p || g == n) && c.ka.css({
				width: d,
				height: f + 37
			});
			c.pages.resize(d, f, l);
			c.fisheye && c.ga && (c.Md = jQuery(c.ga).offset().top + jQuery(c.pages.da).height() - jQuery(c.ka).offset().top, c.fisheye.css({
				top: c.Md - (c.fisheye.find(".flexpaper_fisheye_panelLeft").offset().top - jQuery(c.fisheye).offset().top) + c.fisheye.find(".flexpaper_fisheye_panelLeft").height() / 2
			}), c.Yc = 0.25 * c.ga.height());
			for (d = 0; d < c.document.numPages; d++) {
				c.pages.Wa(d) && (c.pages.pages[d].ei = n, c.pages.pages[d].za = v);
			}
			c.cc();
			c.pages.Ka();
		}
	};
	this.setCurrentCursor = x();
};
window.FlexPaper_Resources = function(g) {
	this.aa = g;
	this.ga = this.aa.ga;
	this.oa = {};
	this.oa.Yg = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABYAAAAWCAQAAABuvaSwAAAAAXNSR0IArs4c6QAAAAJiS0dEAP+Hj8y/AAAACXBIWXMAAAsTAAALEwEAmpwYAAAAB3RJTUUH2gEEFCsCTiK85wAAABl0RVh0Q29tbWVudABDcmVhdGVkIHdpdGggR0lNUFeBDhcAAADUSURBVCjPY2AYBViBPkEVjMgctbkfvzIw/mbd2ZCs8CzqG6Mzk0LfpHuM/7HqFP3Ptldzhd7WFhH5JMavLB3y/xn+e0XisEbsP0cOhKWQxPyKgYGBgfEJWwNCnglZMRMDV5TsHN9lDAxMDEzszC7mi/9L+39AyLOgeuAXq6oFJxsDAyPDf+5/M27+dOldPQGHM6T+8+YwMDAwHGFQSWJ/hSmP5gwWRgYGBgYbBmashqGINihY7Nj7kIGBgaGO3ebrnr14gnwpAwMDA8M8OH/+aDrFCwBelDJZ0XbhRgAAAABJRU5ErkJggg%3D%3D";
	this.oa.fj = "data:image/gif;base64,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%3D%3D";
	this.oa.aj = "data:image/gif;base64,R0lGODlhDwAPAOMGAPHx8f39/f7+/ujo6N3d3fr5+f///52dnf///////////////////////////////yH5BAEKAAgALAAAAAAPAA8AAARCEMlJ6zw4Y3uE+Z93VMdgnuZIHUTrtiqizbMcGp4HGuNR/MDgrwegaYqyVA5UGvRguB2L0MvodJrVzhCIrYyWsCUCADs%3D";
	this.oa.Ki = "data:image/gif;base64,R0lGODlhAwAVAIABAJmZmf///yH5BAEKAAEALAAAAAADABUAAAINRBynaaje0pORrWnhKQA7";
	this.oa.ej = "data:image/gif;base64,R0lGODlhDwAPAOf/AAAAAAEBAQICAgMDAwQEBAUFBQYGBgcHBwgICAkJCQoKCgsLCwwMDA0NDQ4ODg8PDxAQEBERERISEhMTExQUFBUVFRYWFhcXFxgYGBkZGRoaGhsbGxwcHB0dHR4eHh8fHyAgICEhISIiIiMjIyQkJCUlJSYmJicnJygoKCkpKSoqKisrKywsLC0tLS4uLi8vLzAwMDExMTIyMjMzMzQ0NDU1NTY2Njc3Nzg4ODk5OTo6Ojs7Ozw8PD09PT4+Pj8/P0BAQEFBQUJCQkNDQ0REREVFRUZGRkdHR0hISElJSUpKSktLS0xMTE1NTU5OTk9PT1BQUFFRUVJSUlNTU1RUVFVVVVZWVldXV1hYWFlZWVpaWltbW1xcXF1dXV5eXl9fX2BgYGFhYWJiYmNjY2RkZGVlZWZmZmdnZ2hoaGlpaWpqamtra2xsbG1tbW5ubm9vb3BwcHFxcXJycnNzc3R0dHV1dXZ2dnd3d3h4eHl5eXp6ent7e3x8fH19fX5+fn9/f4CAgIGBgYKCgoODg4SEhIWFhYaGhoeHh4iIiImJiYqKiouLi4yMjI2NjY6Ojo+Pj5CQkJGRkZKSkpOTk5SUlJWVlZaWlpeXl5iYmJmZmZqampubm5ycnJ2dnZ6enp+fn6CgoKGhoaKioqOjo6SkpKWlpaampqenp6ioqKmpqaqqqqurq6ysrK2tra6urq+vr7CwsLGxsbKysrOzs7S0tLW1tba2tre3t7i4uLm5ubq6uru7u7y8vL29vb6+vr+/v8DAwMHBwcLCwsPDw8TExMXFxcbGxsfHx8jIyMnJycrKysvLy8zMzM3Nzc7Ozs/Pz9DQ0NHR0dLS0tPT09TU1NXV1dbW1tfX19jY2NnZ2dra2tvb29zc3N3d3d7e3t/f3+Dg4OHh4eLi4uPj4+Tk5OXl5ebm5ufn5+jo6Onp6erq6uvr6+zs7O3t7e7u7u/v7/Dw8PHx8fLy8vPz8/T09PX19fb29vf39/j4+Pn5+fr6+vv7+/z8/P39/f7+/v///yH5BAEKAPgALAAAAAAPAA8AAAitAHEJFJgLmMFgwoQNI6aQ3b+HECM+FKbMocR//vrpy3cv1zGHqUKKFHmvXi1iFjH248dvnz579OTFEgZy5MiYroCBg5iRpUuY8lL9AnfP5khz5UzpIgpzXjx48N61Q4d0VC2il7Jq1Yr0Uyx295o+jToV6SZY7OptXTsu3KVW7OTJg+eO3bp16tCJC/eNkiN2ec2RGwcO3Ddv2bBZ+1Oo0SNGiiJLRmTJkaJDAQEAOw%3D%3D";
	this.oa.kj = "data:image/gif;base64,R0lGODlhDwAPAOf/AAAAAAEBAQICAgMDAwQEBAUFBQYGBgcHBwgICAkJCQoKCgsLCwwMDA0NDQ4ODg8PDxAQEBERERISEhMTExQUFBUVFRYWFhcXFxgYGBkZGRoaGhsbGxwcHB0dHR4eHh8fHyAgICEhISIiIiMjIyQkJCUlJSYmJicnJygoKCkpKSoqKisrKywsLC0tLS4uLi8vLzAwMDExMTIyMjMzMzQ0NDU1NTY2Njc3Nzg4ODk5OTo6Ojs7Ozw8PD09PT4+Pj8/P0BAQEFBQUJCQkNDQ0REREVFRUZGRkdHR0hISElJSUpKSktLS0xMTE1NTU5OTk9PT1BQUFFRUVJSUlNTU1RUVFVVVVZWVldXV1hYWFlZWVpaWltbW1xcXF1dXV5eXl9fX2BgYGFhYWJiYmNjY2RkZGVlZWZmZmdnZ2hoaGlpaWpqamtra2xsbG1tbW5ubm9vb3BwcHFxcXJycnNzc3R0dHV1dXZ2dnd3d3h4eHl5eXp6ent7e3x8fH19fX5+fn9/f4CAgIGBgYKCgoODg4SEhIWFhYaGhoeHh4iIiImJiYqKiouLi4yMjI2NjY6Ojo+Pj5CQkJGRkZKSkpOTk5SUlJWVlZaWlpeXl5iYmJmZmZqampubm5ycnJ2dnZ6enp+fn6CgoKGhoaKioqOjo6SkpKWlpaampqenp6ioqKmpqaqqqqurq6ysrK2tra6urq+vr7CwsLGxsbKysrOzs7S0tLW1tba2tre3t7i4uLm5ubq6uru7u7y8vL29vb6+vr+/v8DAwMHBwcLCwsPDw8TExMXFxcbGxsfHx8jIyMnJycrKysvLy8zMzM3Nzc7Ozs/Pz9DQ0NHR0dLS0tPT09TU1NXV1dbW1tfX19jY2NnZ2dra2tvb29zc3N3d3d7e3t/f3+Dg4OHh4eLi4uPj4+Tk5OXl5ebm5ufn5+jo6Onp6erq6uvr6+zs7O3t7e7u7u/v7/Dw8PHx8fLy8vPz8/T09PX19fb29vf39/j4+Pn5+fr6+vv7+/z8/P39/f7+/v///yH5BAEKAP4ALAAAAAAPAA8AAAjCAHEJFAgMGK5gwhIOIyZsGLt/ECEOixhRmLKHFJX968dPX757uY49TEXy37F/JFPdq1eLGMaN/4j982iPnrxYwkaWFIaSpE1XwMBF7PcP2Mx8NeWl+gXuXsp7v5ySNFfOlK6mNePd03Wv3Tp0VEfVanqp7L1a98peovopFrt7We/F6vqV6iZY7OqprQdLb9lx4S61YidPHjzCreSBFRfuGyVH7NSBHQfO0Tdv3LJhs/anUKNHjBSJLqQIESNLjhQdCggAOw%3D%3D";
	this.oa.hj = "data:image/gif;base64,R0lGODlhDwAPAMZLAGZmZmxsbHl5eX5+foSEhIWFhYaGhoiIiImJiYqKiouLi4yMjI2NjY6OjpCQkJKSkpOTk5SUlJaWlqKioqenp6qqqqurq62trbCwsLGxsbW1tbi4uLm5ubq6ur29vcDAwMHBwcLCwsPDw8TExMfHx8rKyszMzNDQ0NbW1tnZ2dvb29zc3N7e3t/f3+Dg4OHh4ePj4+Tk5OXl5ebm5ujo6Onp6erq6uvr6+zs7O3t7e7u7u/v7/Dw8PHx8fLy8vPz8/T09PX19fb29vf39/j4+Pn5+fr6+vv7+/z8/P39/f7+/v///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////yH+EUNyZWF0ZWQgd2l0aCBHSU1QACH5BAEKAH8ALAAAAAAPAA8AAAepgBuCghwfHyAhiSEjISI4S5CRkpAhJzgCAAJLmAJJnEMflgAmAEujAEinQR4lOAEAAUqvAUazPhwkj5FHRURDQb1BGiOXmUqcQJw+GR04p0OnPac1ExiusD+zObMyEhc4Q0JAPz08Ozk3NjQyERXFAj+cOZwyDxbOpDynN6cvDhTXAuiYRWNWiwUNcKibEQOGixYsVqhIgWIAAQUMEhzYuNEAAggRDhQIBAA7";
	this.oa.Zi = "data:image/gif;base64,R0lGODlhDwAPAOf/AAAAAAEBAQICAgMDAwQEBAUFBQYGBgcHBwgICAkJCQoKCgsLCwwMDA0NDQ4ODg8PDxAQEBERERISEhMTExQUFBUVFRYWFhcXFxgYGBkZGRoaGhsbGxwcHB0dHR4eHh8fHyAgICEhISIiIiMjIyQkJCUlJSYmJicnJygoKCkpKSoqKisrKywsLC0tLS4uLi8vLzAwMDExMTIyMjMzMzQ0NDU1NTY2Njc3Nzg4ODk5OTo6Ojs7Ozw8PD09PT4+Pj8/P0BAQEFBQUJCQkNDQ0REREVFRUZGRkdHR0hISElJSUpKSktLS0xMTE1NTU5OTk9PT1BQUFFRUVJSUlNTU1RUVFVVVVZWVldXV1hYWFlZWVpaWltbW1xcXF1dXV5eXl9fX2BgYGFhYWJiYmNjY2RkZGVlZWZmZmdnZ2hoaGlpaWpqamtra2xsbG1tbW5ubm9vb3BwcHFxcXJycnNzc3R0dHV1dXZ2dnd3d3h4eHl5eXp6ent7e3x8fH19fX5+fn9/f4CAgIGBgYKCgoODg4SEhIWFhYaGhoeHh4iIiImJiYqKiouLi4yMjI2NjY6Ojo+Pj5CQkJGRkZKSkpOTk5SUlJWVlZaWlpeXl5iYmJmZmZqampubm5ycnJ2dnZ6enp+fn6CgoKGhoaKioqOjo6SkpKWlpaampqenp6ioqKmpqaqqqqurq6ysrK2tra6urq+vr7CwsLGxsbKysrOzs7S0tLW1tba2tre3t7i4uLm5ubq6uru7u7y8vL29vb6+vr+/v8DAwMHBwcLCwsPDw8TExMXFxcbGxsfHx8jIyMnJycrKysvLy8zMzM3Nzc7Ozs/Pz9DQ0NHR0dLS0tPT09TU1NXV1dbW1tfX19jY2NnZ2dra2tvb29zc3N3d3d7e3t/f3+Dg4OHh4eLi4uPj4+Tk5OXl5ebm5ufn5+jo6Onp6erq6uvr6+zs7O3t7e7u7u/v7/Dw8PHx8fLy8vPz8/T09PX19fb29vf39/j4+Pn5+fr6+vv7+/z8/P39/f7+/v///yH5BAEKAOAALAAAAAAPAA8AAAjRAHEJFJgLWDBhCIUNI6bwn8OHEB8KUxbxn79+/Pbpy3cv17GI/vhlzIfvXr1axCBe1MjRHj15sYT9gyemmEiS9+Z9cfbOFTB4Zr4Uc0cP3zx05IIKS/ULzJcvYMCssmct6lMwpnTRm7Pznr158eC1MyOm2aha8vK1GVaPXth26cBQO/cpFjyv9MC+a7dOHTpz5TbBgtdWHrx37NalO1duXLhLreDJM+yOnV9z5MSF+0bJkeW/5MZ988ZtWzZs1v4UavSIkaLXihAxqmTJkaJDAQEAOw%3D%3D";
	this.oa.Yi = "data:image/gif;base64,R0lGODlhDwAPAOf/AAAAAAEBAQICAgMDAwQEBAUFBQYGBgcHBwgICAkJCQoKCgsLCwwMDA0NDQ4ODg8PDxAQEBERERISEhMTExQUFBUVFRYWFhcXFxgYGBkZGRoaGhsbGxwcHB0dHR4eHh8fHyAgICEhISIiIiMjIyQkJCUlJSYmJicnJygoKCkpKSoqKisrKywsLC0tLS4uLi8vLzAwMDExMTIyMjMzMzQ0NDU1NTY2Njc3Nzg4ODk5OTo6Ojs7Ozw8PD09PT4+Pj8/P0BAQEFBQUJCQkNDQ0REREVFRUZGRkdHR0hISElJSUpKSktLS0xMTE1NTU5OTk9PT1BQUFFRUVJSUlNTU1RUVFVVVVZWVldXV1hYWFlZWVpaWltbW1xcXF1dXV5eXl9fX2BgYGFhYWJiYmNjY2RkZGVlZWZmZmdnZ2hoaGlpaWpqamtra2xsbG1tbW5ubm9vb3BwcHFxcXJycnNzc3R0dHV1dXZ2dnd3d3h4eHl5eXp6ent7e3x8fH19fX5+fn9/f4CAgIGBgYKCgoODg4SEhIWFhYaGhoeHh4iIiImJiYqKiouLi4yMjI2NjY6Ojo+Pj5CQkJGRkZKSkpOTk5SUlJWVlZaWlpeXl5iYmJmZmZqampubm5ycnJ2dnZ6enp+fn6CgoKGhoaKioqOjo6SkpKWlpaampqenp6ioqKmpqaqqqqurq6ysrK2tra6urq+vr7CwsLGxsbKysrOzs7S0tLW1tba2tre3t7i4uLm5ubq6uru7u7y8vL29vb6+vr+/v8DAwMHBwcLCwsPDw8TExMXFxcbGxsfHx8jIyMnJycrKysvLy8zMzM3Nzc7Ozs/Pz9DQ0NHR0dLS0tPT09TU1NXV1dbW1tfX19jY2NnZ2dra2tvb29zc3N3d3d7e3t/f3+Dg4OHh4eLi4uPj4+Tk5OXl5ebm5ufn5+jo6Onp6erq6uvr6+zs7O3t7e7u7u/v7/Dw8PHx8fLy8vPz8/T09PX19fb29vf39/j4+Pn5+fr6+vv7+/z8/P39/f7+/v///yH5BAEKANUALAAAAAAPAA8AAAjXAHEJxJULGLBgwhIKG0Zs4b+HEMHIg/hQGDSK/+h8MQPPXz9++4BdhOjmi0kx/EDm66WMIjJpYIwR26cv371cxyj68wfm3b58+O7Vq0UMokd+YObZtEdPXixd+37es1dvXjx47tqxSycKVr168uC9cwcmXbpz5caFs9RKXlh37NaB4WaOnLhw3yipihcP7jl0YK7dBeetm6RV7tytUwfGzJcyX75546YN0il2W89FC2PyF7dt2rAxcqROXd1xxsDc0rYtGzZrfggtaoSodm1DiSZRYoSoUEAAOw%3D%3D";
	this.oa.Si = "data:image/gif;base64,R0lGODlhEQAOAMIGAGZmZm9vb4eHh4yMjLKyssrKyv///////yH5BAEKAAcALAAAAAARAA4AAAMweLrcDDDK+MK4GMM33Npf5x2gAoheSaKOeo4kwMHuZMvfPXlE7xOwgmBIFBRgSEUCADs%3D";
	this.oa.Gi = "data:image/gif;base64,R0lGODlhDwAPAMZsAAAAAAEBAQICAgMDAwQEBAUFBQYGBgcHBwgICAkJCQoKCgsLCwwMDA0NDQ4ODg8PDxAQEBERERISEhMTExQUFBUVFRYWFhcXFxgYGBkZGRoaGhsbGxwcHB0dHR4eHh8fHyAgICEhISIiIiMjIyQkJCUlJSYmJicnJygoKCkpKSoqKisrKywsLC0tLS4uLi8vLzAwMDExMTIyMjMzMzQ0NDU1NTY2Njc3Nzg4ODk5OTo6Ojs7Ozw8PD09PT4+Pj8/P0BAQEFBQUJCQkNDQ0REREVFRUZGRkdHR0hISElJSUpKSktLS0xMTE1NTU5OTk9PT1BQUFFRUVJSUlNTU1RUVFVVVVZWVldXV1hYWFlZWVpaWltbW1xcXF1dXV5eXl9fX2BgYGFhYWJiYmNjY2RkZGVlZWZmZmdnZ2hoaGlpaWpqamtra////////////////////////////////////////////////////////////////////////////////yH5BAEHAH8ALAAAAAAPAA8AAAcrgH+Cg4SFhoNrh4ZriYqIjI6CjJCOk5SHlpeYlpF/nJ2TnZKNoqSip6iKgQA7";
	this.oa.Ii = "data:image/gif;base64,R0lGODlhDwAPAMZsAAAAAAEBAQICAgMDAwQEBAUFBQYGBgcHBwgICAkJCQoKCgsLCwwMDA0NDQ4ODg8PDxAQEBERERISEhMTExQUFBUVFRYWFhcXFxgYGBkZGRoaGhsbGxwcHB0dHR4eHh8fHyAgICEhISIiIiMjIyQkJCUlJSYmJicnJygoKCkpKSoqKisrKywsLC0tLS4uLi8vLzAwMDExMTIyMjMzMzQ0NDU1NTY2Njc3Nzg4ODk5OTo6Ojs7Ozw8PD09PT4+Pj8/P0BAQEFBQUJCQkNDQ0REREVFRUZGRkdHR0hISElJSUpKSktLS0xMTE1NTU5OTk9PT1BQUFFRUVJSUlNTU1RUVFVVVVZWVldXV1hYWFlZWVpaWltbW1xcXF1dXV5eXl9fX2BgYGFhYWJiYmNjY2RkZGVlZWZmZmdnZ2hoaGlpaWpqamtra////////////////////////////////////////////////////////////////////////////////yH5BAEHAH8ALAAAAAAPAA8AAAcqgH+Cg4SFhWuGiX9riIqEjI2Oi5CSk5SOkJeJmZGbmoqfoJ2YlaWmp4SBADs%3D";
	this.oa.gj = "data:image/gif;base64,R0lGODlhEAAPAIABAGtrawAAACH5BAEKAAEALAAAAAAQAA8AAAIkjI+pi+DhgJGMnrfsxEnDqHgRN3WjJp5Wel6mVzbsR8HMjScFADs%3D";
	this.oa.Wi = "data:image/gif;base64,R0lGODlhEAAPAIQYADk5OUJCQk1NTVVVVV9fX2ZmZuHh4eLi4uPj4+Tk5OXl5ebm5ufn5+jo6Onp6erq6uvr6+zs7O7u7u/v7/Dw8PLy8vT09PX19QAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAEKAB8ALAAAAAAQAA8AAAVa4CeOZGmORXGSaTFJ6VpUVBFBxRKXc33nhYOKZam4YI6CYoiiSW4PnRCVKlJeEQdjKSpcak7oIjH9zC5WLGSLYM7CkEeDbGCaC5RnfF7mXfUMdix6cgmCKyYhADs%3D";
	this.oa.Ni = "data:image/gif;base64,R0lGODlhDwAPAMZBAOzs7GBgYEpKSj8/Qz09QUhISEtLSxkZGff39y8vLxYVG2dnaKKiooiIiG1tbczMzJCPk05OTt3c4dzc3B0dHY+PjzQ1NU1MT5mZmTY1Oy0tLfj4+NjX1u/v7z09PdLS0uvq6pmZltXV1k1MTiEhIe7u8OPj5O3t7c3Nzfb29ezr7pWVmdfX1/v7++nn6dXV1YyMjcbGxklJSdLR09LR0uPj44+PklNTVPLy8vn499TU1pKSkn5+fujo6CIhJtrZ2wAAAP///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////yH5BAEKAH8ALAAAAAAPAA8AAAdngH+Cg4SFhC40LIaFPxYJBxWLgjoEMEEoMjuLPRk2QUEAMR4MhjMKKkEtOA88EYYiPhKfNR0BDosrAyZBCBgUL5IQGg0BJEACJ5IhNwsTBccbkoMpF0AGINKCJSNAHNmCAB853+TSgQA7";
	this.oa.cj = "data:image/gif;base64,R0lGODlhEQAQAOcAAAAAAAEBAQICAgMDAwQEBAUFBQYGBgcHBwgICAkJCQoKCgsLCwwMDA0NDQ4ODg8PDxAQEBERERISEhMTExQUFBUVFRYWFhcXFxgYGBkZGRoaGhsbGxwcHB0dHR4eHh8fHyAgICEhISIiIiMjIyQkJCUlJSYmJicnJygoKCkpKSoqKisrKywsLC0tLS4uLi8vLzAwMDExMTIyMjMzMzQ0NDU1NTY2Njc3Nzg4ODk5OTo6Ojs7Ozw8PD09PT4+Pj8/P0BAQEFBQUJCQkNDQ0REREVFRUZGRkdHR0hISElJSUpKSktLS0xMTE1NTU5OTk9PT1BQUFFRUVJSUlNTU1RUVFVVVVZWVldXV1hYWFlZWVpaWltbW1xcXF1dXV5eXl9fX2BgYGFhYWJiYmNjY2RkZGVlZWZmZmdnZ2hoaGlpaWpqamtra2xsbG1tbW5ubm9vb3BwcHFxcXJycnNzc3R0dHV1dXZ2dnd3d3h4eHl5eXp6ent7e3x8fH19fX5+fn9/f4CAgIGBgYKCgoODg4SEhIWFhYaGhoeHh4iIiImJiYqKiouLi4yMjI2NjY6Ojo+Pj5CQkJGRkZKSkpOTk5SUlJWVlZaWlpeXl5iYmJmZmZqampubm5ycnJ2dnZ6enp+fn6CgoKGhoaKioqOjo6SkpKWlpaampqenp6ioqKmpqaqqqqurq6ysrK2tra6urq+vr7CwsLGxsbKysrOzs7S0tLW1tba2tre3t7i4uLm5ubq6uru7u7y8vL29vb6+vr+/v8DAwMHBwcLCwsPDw8TExMXFxcbGxsfHx8jIyMnJycrKysvLy8zMzM3Nzc7Ozs/Pz9DQ0NHR0dLS0tPT09TU1NXV1dbW1tfX19jY2NnZ2dra2tvb29zc3N3d3d7e3t/f3+Dg4OHh4eLi4uPj4+Tk5OXl5ebm5ufn5+jo6Onp6erq6uvr6+zs7O3t7e7u7u/v7/Dw8PHx8fLy8vPz8/T09PX19fb29vf39/j4+Pn5+fr6+vv7+/z8/P39/f7+/v///yH5BAEHAP8ALAAAAAARABAAAAjcAP8JHDhQmcGDBgkC+6FH4LJs4Lp1A5dt2UBgSqIMRPZtHTp0674hG6hH48Bi2s6NG3dOWzGBQ5gMGfJEoLGUK1saG8jFSZCBwbCRCxeOHLZgBNsQ/GWNHDhw5Kz9IkjVV9Nv36L2cuRoESJEgv786dMHT5063cJJy9Uo2rZp06A9e2bQ2S+0atmCk1euHDly4rhxG9esTrhz1Ww14rbuqTdv3KxZ65asjjRqyGgtbgzuceTJyezQGq15b9+/gQc7s0OwkTRucOXSVWaX9UCuir4G8uOHDx+zdQgGBAA7";
	this.lh = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAL4AAABTCAYAAAAhrr3BAAAACXBIWXMAAAsTAAALEwEAmpwYAAAKT2lDQ1BQaG90b3Nob3AgSUNDIHByb2ZpbGUAAHjanVNnVFPpFj333vRCS4iAlEtvUhUIIFJCi4AUkSYqIQkQSoghodkVUcERRUUEG8igiAOOjoCMFVEsDIoK2AfkIaKOg6OIisr74Xuja9a89+bN/rXXPues852zzwfACAyWSDNRNYAMqUIeEeCDx8TG4eQuQIEKJHAAEAizZCFz/SMBAPh+PDwrIsAHvgABeNMLCADATZvAMByH/w/qQplcAYCEAcB0kThLCIAUAEB6jkKmAEBGAYCdmCZTAKAEAGDLY2LjAFAtAGAnf+bTAICd+Jl7AQBblCEVAaCRACATZYhEAGg7AKzPVopFAFgwABRmS8Q5ANgtADBJV2ZIALC3AMDOEAuyAAgMADBRiIUpAAR7AGDIIyN4AISZABRG8lc88SuuEOcqAAB4mbI8uSQ5RYFbCC1xB1dXLh4ozkkXKxQ2YQJhmkAuwnmZGTKBNA/g88wAAKCRFRHgg/P9eM4Ors7ONo62Dl8t6r8G/yJiYuP+5c+rcEAAAOF0ftH+LC+zGoA7BoBt/qIl7gRoXgugdfeLZrIPQLUAoOnaV/Nw+H48PEWhkLnZ2eXk5NhKxEJbYcpXff5nwl/AV/1s+X48/Pf14L7iJIEyXYFHBPjgwsz0TKUcz5IJhGLc5o9H/LcL//wd0yLESWK5WCoU41EScY5EmozzMqUiiUKSKcUl0v9k4t8s+wM+3zUAsGo+AXuRLahdYwP2SycQWHTA4vcAAPK7b8HUKAgDgGiD4c93/+8//UegJQCAZkmScQAAXkQkLlTKsz/HCAAARKCBKrBBG/TBGCzABhzBBdzBC/xgNoRCJMTCQhBCCmSAHHJgKayCQiiGzbAdKmAv1EAdNMBRaIaTcA4uwlW4Dj1wD/phCJ7BKLyBCQRByAgTYSHaiAFiilgjjggXmYX4IcFIBBKLJCDJiBRRIkuRNUgxUopUIFVIHfI9cgI5h1xGupE7yAAygvyGvEcxlIGyUT3UDLVDuag3GoRGogvQZHQxmo8WoJvQcrQaPYw2oefQq2gP2o8+Q8cwwOgYBzPEbDAuxsNCsTgsCZNjy7EirAyrxhqwVqwDu4n1Y8+xdwQSgUXACTYEd0IgYR5BSFhMWE7YSKggHCQ0EdoJNwkDhFHCJyKTqEu0JroR+cQYYjIxh1hILCPWEo8TLxB7iEPENyQSiUMyJ7mQAkmxpFTSEtJG0m5SI+ksqZs0SBojk8naZGuyBzmULCAryIXkneTD5DPkG+Qh8lsKnWJAcaT4U+IoUspqShnlEOU05QZlmDJBVaOaUt2ooVQRNY9aQq2htlKvUYeoEzR1mjnNgxZJS6WtopXTGmgXaPdpr+h0uhHdlR5Ol9BX0svpR+iX6AP0dwwNhhWDx4hnKBmbGAcYZxl3GK+YTKYZ04sZx1QwNzHrmOeZD5lvVVgqtip8FZHKCpVKlSaVGyovVKmqpqreqgtV81XLVI+pXlN9rkZVM1PjqQnUlqtVqp1Q61MbU2epO6iHqmeob1Q/pH5Z/YkGWcNMw09DpFGgsV/jvMYgC2MZs3gsIWsNq4Z1gTXEJrHN2Xx2KruY/R27iz2qqaE5QzNKM1ezUvOUZj8H45hx+Jx0TgnnKKeX836K3hTvKeIpG6Y0TLkxZVxrqpaXllirSKtRq0frvTau7aedpr1Fu1n7gQ5Bx0onXCdHZ4/OBZ3nU9lT3acKpxZNPTr1ri6qa6UbobtEd79up+6Ynr5egJ5Mb6feeb3n+hx9L/1U/W36p/VHDFgGswwkBtsMzhg8xTVxbzwdL8fb8VFDXcNAQ6VhlWGX4YSRudE8o9VGjUYPjGnGXOMk423GbcajJgYmISZLTepN7ppSTbmmKaY7TDtMx83MzaLN1pk1mz0x1zLnm+eb15vft2BaeFostqi2uGVJsuRaplnutrxuhVo5WaVYVVpds0atna0l1rutu6cRp7lOk06rntZnw7Dxtsm2qbcZsOXYBtuutm22fWFnYhdnt8Wuw+6TvZN9un2N/T0HDYfZDqsdWh1+c7RyFDpWOt6azpzuP33F9JbpL2dYzxDP2DPjthPLKcRpnVOb00dnF2e5c4PziIuJS4LLLpc+Lpsbxt3IveRKdPVxXeF60vWdm7Obwu2o26/uNu5p7ofcn8w0nymeWTNz0MPIQ+BR5dE/C5+VMGvfrH5PQ0+BZ7XnIy9jL5FXrdewt6V3qvdh7xc+9j5yn+M+4zw33jLeWV/MN8C3yLfLT8Nvnl+F30N/I/9k/3r/0QCngCUBZwOJgUGBWwL7+Hp8Ib+OPzrbZfay2e1BjKC5QRVBj4KtguXBrSFoyOyQrSH355jOkc5pDoVQfujW0Adh5mGLw34MJ4WHhVeGP45wiFga0TGXNXfR3ENz30T6RJZE3ptnMU85ry1KNSo+qi5qPNo3ujS6P8YuZlnM1VidWElsSxw5LiquNm5svt/87fOH4p3iC+N7F5gvyF1weaHOwvSFpxapLhIsOpZATIhOOJTwQRAqqBaMJfITdyWOCnnCHcJnIi/RNtGI2ENcKh5O8kgqTXqS7JG8NXkkxTOlLOW5hCepkLxMDUzdmzqeFpp2IG0yPTq9MYOSkZBxQqohTZO2Z+pn5mZ2y6xlhbL+xW6Lty8elQfJa7OQrAVZLQq2QqboVFoo1yoHsmdlV2a/zYnKOZarnivN7cyzytuQN5zvn//tEsIS4ZK2pYZLVy0dWOa9rGo5sjxxedsK4xUFK4ZWBqw8uIq2Km3VT6vtV5eufr0mek1rgV7ByoLBtQFr6wtVCuWFfevc1+1dT1gvWd+1YfqGnRs+FYmKrhTbF5cVf9go3HjlG4dvyr+Z3JS0qavEuWTPZtJm6ebeLZ5bDpaql+aXDm4N2dq0Dd9WtO319kXbL5fNKNu7g7ZDuaO/PLi8ZafJzs07P1SkVPRU+lQ27tLdtWHX+G7R7ht7vPY07NXbW7z3/T7JvttVAVVN1WbVZftJ+7P3P66Jqun4lvttXa1ObXHtxwPSA/0HIw6217nU1R3SPVRSj9Yr60cOxx++/p3vdy0NNg1VjZzG4iNwRHnk6fcJ3/ceDTradox7rOEH0x92HWcdL2pCmvKaRptTmvtbYlu6T8w+0dbq3nr8R9sfD5w0PFl5SvNUyWna6YLTk2fyz4ydlZ19fi753GDborZ752PO32oPb++6EHTh0kX/i+c7vDvOXPK4dPKy2+UTV7hXmq86X23qdOo8/pPTT8e7nLuarrlca7nuer21e2b36RueN87d9L158Rb/1tWeOT3dvfN6b/fF9/XfFt1+cif9zsu72Xcn7q28T7xf9EDtQdlD3YfVP1v+3Njv3H9qwHeg89HcR/cGhYPP/pH1jw9DBY+Zj8uGDYbrnjg+OTniP3L96fynQ89kzyaeF/6i/suuFxYvfvjV69fO0ZjRoZfyl5O/bXyl/erA6xmv28bCxh6+yXgzMV70VvvtwXfcdx3vo98PT+R8IH8o/2j5sfVT0Kf7kxmTk/8EA5jz/GMzLdsAAAAgY0hSTQAAeiUAAICDAAD5/wAAgOkAAHUwAADqYAAAOpgAABdvkl/FRgAAK9xJREFUeNrsnXe8bFV597/P2ntPP+X2wuWCqNRLFbAjGgiIRgmKAhFsGE00akSjmBgNEd4YiSXmjRV8gxJU0KhRVBJUFOmgoiBFernl3NPPtF3W8/6x1pyZU++53IrM8/nsz5kzs2fPLr/1rN9Tl6gqXenKU01CAPuvx0JoIRyGfAGedhiEIWQW/c0maKRompHd9yiay5DQgFXMfv0EL9gbcj1gBZT2FkXIjbfB7++HKFr4GRn/N23C4afAmkPd69lEtnQwAclgxe0QNkFlK25L4A+hQGMdaB24b+tvcQZqIb9/+5goIEjf2RCt6aJwDvnNz+/k8x/6Gs9pDHDwxgH6P7mE5n4N/um9wjPzIWe87Tiu+NS3+NrLz8ZsfJAj+u7m6L2GWdZb4YjRHHdfcBcrTnoJuefvT2Hjw3DYzVTCHOVqxQG/K/PKUcDpYD+4gJHWIQq2AaaI9L8Fysd17+TupvG7MqesA7kcotPBNBb2FQFNgRRKL0R6ToHoad072QX+k0bWAN8H/RUE1y/4VmkKUkH6zoDysd272AX+LhRh4SzF2foV4ApgLfDqzg+2+GURZPHboHBEF11d4O9CUYGmQLpA9EcYxF4M5tkgv0DNzQv+LTuB9L26C/ou8LdRTLB9jpMA2QL3jTgP7KkQA8HlC+f1DcjtAz0v66LqSSBmtz67ic1zaekIeNWCz190oVznNGL9WywATSS7CsnY4kbsPKeLzgIpdFHVBf628HIDG++BLAaZAdqVwIkodkrsYPpmA4gGQGrOlz7npgDrgM+SAnWgye9JuIfEzxjzbXEDyn8Cuf26iOpSnW2UIILR9bDxbli9bnoQazXwwEI0vfIQNEdoB49m3bGHXP8lItLfQY9uJ9UtEyRNIVyO9J7SRVNX47dAoZBmW/ON/WYg9P4bIYk9j57U5r3A8BZGDpjNIJud5rfMsSkgnxLh8Gm0/Z62O2iuDSBDlr8eTJfidIEPkGXovs+AvfeEOG7RiS3JiaBvQdTx8iCEscdhbD2YKZNT0RHr+S9Ng0f8SDHzbPJmTPimWW7FY1v24sRQPAh6ntNFUhf4LVBY6O1Bn3s0HHSAU5CagdH5ts8Bz8awP4EnYiaBwXsd559q3AbzXpZMgBnyu86ptQ8D+YQbVDOMhFHmNyAAiyx+RRdFXeDP1Poo6KEHo886EAohpHY+PDVRPoORvyAQIRAIAxh5CGza6ZnRLV6W2eSyDMQ443jmVga+DKYXmdXUGZt/YDehdCBUDuuiqAv8OXh+nMCaJejRe0Nv3tGYUCCYZQvlNpBBkBOdjzCE+jDUR6Zr/flQiQYDoMF8/PxTiByGCcCYmWNJtH+Scs2xyeJXsrt7hLuyq706SQqlHHrYnsid90NSn2/vLxJkH8Ho1aAx2Qg01kNpGWQKMA5amZvmjICMgYbM4b9/PWLPdikGrcGhk1YtakCz3jknFhtDaR2UD+8iqAv8hVAfC+Uc2ldERh6Bos6FrfXAPQgvBn5EBtR+C/0HgTYBUiQnUwFLW5ubAZ97P7MOQIPqoaLRv06OBxOCDVEN/EAJ3W9ovHbu62ggK4/fihmoK09t4LeoT2bQAUFWKeS8nThTvgW8FfgRATB2K+hQyzuUI4j6Z2hzVQgjdI+9QXIzjyj0YXOXaBr0mjAEG6Ba9h6jlq1cBZogsm7280+hsBrpOXg++vhm4GXAZuBDwF8DlwK/7kLuqQr8lmK2oBtAVgH5WcH/AC7DZn/gLrIqxBta1GWcVGfmx2sG0Urnt886Dug9mpLYC+16OWRM8kSVkEKPISwFIIKq9aCPW9TnCKDs35xi1EplHYR9c13dO4FPAl8FNgA9wPuAX3aB/1QHfgv8Kej9ICuZXfMrV1Pg5QTchbXOsxPkQTVDZkk7EwOjfchg1efQdEgQvFqsnq0EZBUhHVeaNSW/CPLljChXA0lQFZ/bwxrQw4FrpxvOrtBkTjkD+E/gTP//MnelJF24dYHvwejJwCO4mK2d4Vz5OREnA/1kjLBHAv0hqLWYKOcM02mDaaAGySNtFqQKJlxMedGFREVsPkJNgFHABDQmUpoTE+QKGfmKISq68aOKoPwxotfO+I3yvvNdVQW4f6d6zv7wJWLr6j63aG3u+lydABj14N+L6enDCSm/BI4j4woaCrkcpNkoQVicgilRSAPnPbJMTWzL0n9gfGAvwhy20udiA0EBjCIyDpoS14W4bgnzUKgYohKYiBNRPtwejhaiMlI5aLYreSmwL7AYeD7wV8CdwG1zXHkP8OfACX6w3Ad8EfhZx5053VOt/5rl+2uBVwJX+u++FrgVeBD4C38+/bgI9H/OcYwC8EZ/DiuACeBG4N88TWvJ8c7w4We4wpwzcImCQ8D3gYv959Plmf5cjvT38CZ/7Ic69nm6f/I/9vP+K4C9gX8Hal45/wTYYzui7qvbBnwBjHQ+yF7PiwN/0uP+5swvBhjwj2H5NMoj/AD4G+CbJAWl/wSnxseus2RjTOmGUM1BJi4K3J46jkLkrRiL2iZZc9zbGE0oWiiUISwixhnDaQOqDSWIIN/DoUFO91W4e9JYsBOE1ccIcqunX8WLgBOBPuBAYJE30G9ipu9qLfADD7ZL/dU/zz/89wP/4lXASuDjHmzf7Ph+Hvi2v99f8+99Avi8H3Rr/PEHgWfhqskuAt7WcXd7/KA5EPgGcIO/+6/zwD4GeNTv+x4/KF4JnAZ8z1/XPsCFwJ/5zwY7zvGVfsDd68/V+Pfe7MHdmklfA7zBD75vAgcDdwAXmcDUPMr2AVZtR+Cv2BbgryWxz6GePpcePYJA1qAswtKLxaBUgRHgca/1fgr6E5BNcw6ix/yY75+i+TcAIyhHMF6/VcO9Id9jpXanJdnU9t6oQrPYDjBNkn4uRCRywTDBhjnE1qH6MASLwDYQCSEsQFhCoiJiQrIUaoPkwlzhBWLM3S3oqg3I3f9Tikc8a7o78wN+u9Nrwff595dPm6YD4Ct++j7E35+WvBX4HHALcI0H1ZHAJf4etjJS/4//7mF+0OAVzDl+sL1imgY+yZ/TncCn/HvnAIcDB03TwOcDv/Ngf0/HsV/ujfMjvbu5Jf8CXO819Gv9e3t60H/dz2oto+g8//4V3mkx0uFNuMQf/zWeKurvrr+7NXHH25lnpFvDP0OvPc5FuIbI3GEfrX49veHxd9tfP3KMvXvDPrp+ZJGO1AOaiaBZBbFrMHo0kr2NoPI1TOE3qL0Y5DmzAl/8JY96/dDOovwBAScRV2HzXWCCMaJlCUEPBD0Q9jhF2wi98amt7SyQY9qzU4Algdpj7SS4ljcorkJtACbWQ3UTxOPkSyXKfcteUK4sptzjtkrfKnKNR9Cx++ebB+fjoy/w2vSsaaDHa+yr/ADqHAwbPYhaIP5r4Gzgt9NoSwP4y1lox5XAZ4C/A0odM9SXp4Eer7Wv8c+60y/WC7xjGujBzYZ/4QHb4oDv8BTtbR2gx79+kz+H0/17TU+J7vKf3Qfoxoc2cetVvyaXz+1Q47bkb5p1mtG4z0TW+qnnWAnMi8lF6ySzATlPcXIBmAw7NI6YGrrJuOZRUQ4pREghhGIO8gESBhAtW07j8TdSG3sDgf0OhvMx3DKF8iT+cR7pJ3Sn+W8h5AwCVsjQ7zbq3s9X7TlyMaUDHJc3IQw+hqQ3dJQr6jKUCxwM3TVZTbD1QeeVifLuUn1zJwdVQbIUTWMKUZ4CKdSH1vnwbnsOSmvQGIS+Z8wH/rnkZX76v2GOz7/ktX7F04tRD6qbPJd+KfD//Dadr/8YV0Yzm/yntzsOAm4GTvHHni6r/TN/tOO9nJ9tbp/j2D/wv3uspykv8xRuNk094anSScBnO4z/j3fudM03rqNRbRD0lnco8PcBeSMi+2CTmGatl75FT9P1o6uJbQ/GOGzkI3QicVFRESgJSOpwYdSBThVShWqG1gXG6hAaNAqQQh4aEXLHsMgYJ1PgZdT4N4R/pJVfb/zE+jsP/giwKML1WE5ibPOXZf0tSr6Ske/1+fQRDD4A2Wb/jBSkcj4mv2rSDtEUbWxECzlHbWaLuqobBMXe5eRL/ahNkLSxhwdhGyRp1VVuPTFZ4/n/fzMzw9R6LrvYu0En/Pu3+PjAZ4Df+FlgNrl/nt+9zwPxGR74w97G+BPgOf53l3vuv4apXeNC5i/8GfOfP90P+h5vEM92jRmuSddIB/Ub6Zx5fnfDPdzyw1+SL+dJd7A787egFyJ6Dpqcw8DdUNvLce6W8SoC+RxUa44h5QXJZx54gUsmE+uVYyuRK/SxIHU5NrGgG0axcZNgGCQmIuKvMZzkOfF/T+qX9aC3gew/qWuvAj7C4w+H2d0Xx6rhsIoDuQSGcC8gsGCroPwxJj0bs8L7Ji3UN6CaoEHZB7SC9iSOp0YSUOxdTq7Yg2rmr4OKn3s69Pk2edZyfhDdM8fnvwEuZ2ahzVL/tw9YMgvlUOYvqU/8Pq1nfpqfWUY8tfmppxt3eO27ZNoMtqWKoph2jkjkadxc19hprxhPgSY10YYHN9Kox5R7izvDj6/rEX0vobmEiYkL7FVXv0xWPgP22stVUYlCIeczGX3o1TTBel7dAr1YMK1aVlz+S6tnpU3RjY9BIcXuDcHjfoIM2A/hOwifRPh7oEoOsntA6mCeBigjxDxiH+O58TDXq6SOsmQQLIdocYhagaJZTDH4LFITpO6cTPWNkDawuWJb0xvj+Yi4c5WAUv9KcoUKajPvKFJmjS5smwz66MU5W/BbdwLtFcCHvXJ4uzeOj2dmotLe8xxzDz+A7/ezyX94T897vQdu+swj07j5XlsYzHt6eqSeNv8Y+KctfGdWahiEAcYIO1JmzveB3K7DycsZqr1V7/3NZr3/Xuf3NgGSjyAwDvym4SqrbAwagybtzfq/NPx7mbuX4+MwvNFdZQjZGtCeSWgJdd7DGFejHOadMCQ3QPO70PweNH/I9+KbOU4fwPIAZR50eiMMgLEU2ZzAQPI5hpN9GEuhOgj1AcgaYEJsEKJiHPhNK28iQ4yhvGhVB+i1PQuoHUDtxIwC9Sc+FlqG44o5Pj/ba95Kh+vzUs/pL/Suvz/yhup0jXsscxfoHO8BeZvn8DnvwanNsu/B0yLNTR+jeOYcxz7CzxCtGMTNuC4Yc8nlfuCxnZXKNgC/Nf4C8wWE53PfnVfpXb9x51fIo2EAQYqYCWjErh7Wxs4vbuPJTW3i3tO6D/ErOrARbdZQ6ZgMVrgsYDZ4x1ydZ2P4CcJbCNxjdMmSoCl3ExFg2A9hzAgmCWHTo7DhRqgN8Xei9lSGEhiIYf0I1MbQIHKuTBO4S/bFKaqKBCHlxWuI8hWfr9MCvX9t8vcTlOoEJdpbmRlR44XL97zG/+Qsn/UBHwV+7nmz8UbpRu8paQ2cj3jX4Es6vlv3WvdDc2j7j3ivUZ12kc2hs+z7Lh9Hr0+jURb451kwE3oX6Y1+A/hXb6G9YZbjHzMtLiG7Avjz+/FF7iEMT+KR+86zzdoH5YDDkEIR4mHHpbMI6plzC7Y4vvHpvWpdIYiqu7QEdGC9o8fW6Sdpugld+9xtlhGvr0L6tckX7CgvQHgPxgVGPGO6GjgRYVAVxkOobwIsb2kO8o9LLZTXgKpPea4lUCyCMQ74xkzO5kEQUVq0ljDMOU6vbcCLqgN4kL9mBqOw85pc5Wk2gfj7HHYYgq/3fvU+b7AOeo36Ic+53+e/d6EPSB3F1GS5f/QA+o4H7/3eq3Ox96js72nMiP/8w34WaQ2K2/19vMxTnV97+nOm9/B9zLtMzwf+3l/PNf6crvPn9ZA3gj/gadBLOoJjvwAu8O7Sg31swfprOc/77K/ooDwzait0ZpR7e0pxIQGsjDD8WzY+cqfG9S+weK8SZtirYevKCxsZlDOXFZkGjhKqcf9rANJERzejgyNIq6tZi0Uar9XXukYFuhnSDZAOAIazCDnK8+EfiIIafgqcKJBUDbYuGGP4gEScn8UweAuko9B7kCCRQVMLzRTKBWwQAgbVjDBXprxkL4IgN8np2zWQBsISmCgG/fYMp0SugpRXzzeN/3KaJr50mlfkKu9JOdeH8NXz6Ks82Aa8lt7TpxTcMgsHP9NHa4/xwM8Dv/eR3/O91g09TbnIg7neQYte4wfEuW1VxFXA3/qnUvLeJe2IxJ/q9++MB/zMv//wtHP8W+BXfqZ6pT/OqP/+Zzr2u90PhEmbxihE1i3ZoGCNymeUbLGiKojzUGvLFa2ouMRCEUUUFHWORxQRi4q2kw9FBJVrFx65DcNLGds8wIMjl+oKu5RQIYjb4ZqKuFth8GWEOglqIkU3KWIiiJuTgBdvJ4s/xtgSqN8OfRtBc5NB2QN8AOZbwBcFbla4Q+GEceFugfchHG/UPWURGL8HknFYdIQQ9ARoLUFLRawJUSxRoUJlyV6YIEJt2u4AoZmjMGHFn7he5aOdUxV4lqBpfa45+pxZXH2vm2W/W/2Un8N1jZjuU3/cA2ouedx7ZjqVZMnTqLd60JeYu3Z4yNOa93iNOz4tWeSd0yhx3vP+8/zW592t2Ra4/OV+FpQO92ynfN9v7TvsgR9YsJCJykdac4DMGiGc+SR0ci+dxYyQrczONOYqraavYoQryVPGZEzy8NgxHfKTdMVjByQCqgHSl4eCQYcbiGorZkSsUG1CvQbDTQhzUDYzTvcU4BSEDX7a30cMZ+DHWNhxVySCxkZl87UZfUcYCqsga2ZkPZAr9FFeuhYTBZ7T+9GiFoIcRD0YEdTaDJ3NKyGgMbrxBqR/3+0x7cZzBHqeiNGn0zwxYwua0WcPZG3pfEa34ryq23IhO5/jzxKIF+FnKrwey9cxHR4EC1r1IG+lGwQgPaDjoE03RVGKkNAgww2yLKPmmZI6PGETGIgcYQ28ETxlQAsrJ0/eU3IjYE3bvS64YG5aU4ZviOk5APLrUnKLVlBeujeoRUmcpreZpzohqnlIm9gkwRTyF5lC7hezP4EAsVV2MylPcxFuTyn4WekPRsIFAV5BY+vmoB4QwzdRLiDr8CCIY5NaBentSIQIgU0g3pMiFjQXEi8JqA1mk50BRdzKOWJdys2gwAqZe+SLdRPKfNlGLcfL+G9j4nqDKBugVh7BpjGEsev9k2VorgyFfsRHqIG7gkrp/aZYmL0Rlk2Q4V9S2KeK5Mu7y7O8ep6A0bbKLU8t4IugcYKqRRPbHvuONn0UywkIR0/G9QI/qeVxGZaRM6d0FEzZIASkxDRsnTTIYAnIkOuwjYHMm15iYNQ4s6GsHSRSOoLCOjWAKtNmpk7bQfKGbChPdvUdsHcJqURQSJEwQHqWIoUSZC54KMaMYcxZ2UR1JB2fmP2+WIuMNCgkddh9gP+aHXjsD/EHJmYuwANommCTFE2yqf4EB/wY5X1kZJNZlH437QCfDju3pYZCQ2pUZYKUbNKMMotASj7eXusArIHNxqXXS6vbn/qKQq+EjXR4hiY7ArbfawWMTWURUiwjFF1ANxcgUYj0r0B6FjlKZgyYIMXImaA3u1w9mWPz+8sOd0H34vJfnj6by68r2wv4Ig5NzQTbbGIbyfw9L5WfoVxF5kEv3tb3ml/UeagTA7Vcg6Y0nKb2g6d1aOlzE2lWbwPX+IVMBgOv3TP/vU6NzlQTv/X/ZGM2C6ZQIihUvBESwIiFBGTxKigtdj5UqYDpqSE9rwP57hbvWiu6u+Pbi5zu/e934bIZu7K9qY6EBoyiDw+T3fYYBHahCu3zCC8lAG21YKpBNg52yBmsWd67L3XaOOpoqyNFT6NMm7IYYEKhYqGkM2vRZR6PliqYXB7Tsxo1edSm7rdiSxAuhdwyn1sUgJH1iDkL9H+3DHp3nNJL3ocU+3eGYgrnnZ278sSBL6GQbKgR3/AAuqnWpg0LEcv/Nod4RHvYU/FxrQRMCvVFLjDV1wvGGOc+VHXg73CvqrjsBoou4cyOuc9Cj/ShAHLec6otGtMyiDsozpTWmIEQ9O6Jhr1gxGVmSgOzquQ6NdQSpDcE4acivAX0962TEiOonWWmUxeZLp/wYaIDXrozno/uRA/fUw/4Iz+6l+qtG9Bm9kT0SlWE71DnHdKhqVXBFqBZc8HdRcsFEwrqn592aGaM8+hoDKYE0hQkdmn4GGiIA/9SO8292er2J20PTisoFlZWIrmK42AmhDRDlhWRZWW0kSHVuEE5/zHywfnOtylIEKC2SDywmSyZOeNpfYLSMa/fWaCHqQlnXY2/vYFfvekxZ7AVnrBe+hbwDunU4gaynG9+EMPIWEalR4ha6Tst6tLqX+AT0RgEUYMsDdFaAonFCIwb5x0tK9iOEoHp2l6BsNBPUFruo8MBqCC9RVgROK5v9cca23MltjehGeQjpJBDohy2CslIHclFM+iTTVLXnWHnyXdxaQiCC/93ZbtSnRYAsyd8nOu8D3lfcIZoVnGpOqSOv2eZMjqiVMpCvtBBd/wJZEOg611BFxWBIEQqQJy6mcjCYAj5rB3YMt4Ilg6D14R5wp7V7Q8RKIbImjKa1W7F2vMIzHe1nmE31QgWlV3Lkkn17qjOrJWzrQjvzpNH/NaVHQH87fAsm8CXxVX/uwSLvMd1pyfGwvi4kmUuYbJNliB9GL/Cpkd0YCAzUAhdzk4tI8lgKIRl2mHPTnFhClHvHkgY+RYjAoFkZs2iX1PgMq02fooEfaBvI01XYS0Ij+ESyH6Jy3Ppyq4W3UnAl+3zQxehvBtYgYW0wGSXhMnUdf871QnH+4slB3o74Xz4rXb2EgaIMah6WhsGSA8EjYzxBIqZy1OddGf63aLKCiRfcRdkjBXYJCt7fy09hRFt1k9F5MMgFdRAYNBmgqYZJskA2YRwJcJncYXdc/hZgicLfMTPwHsymRjOelwm57a0M1yC63PTcmnVcU2sHt2eJ5/G6Y4HPttn9h5Q+HuxfF6Nd2G2AlvBTMZQn3Cpx+WiGxBZ3OEWD3zqZtBy33heVALJMobqUIg7aA4QFHoJSktaoAfIZGlPXpb1/hEqoU66e3wkzBrILDrecNNPnC0nF70hHR58nQofFxetzKafuNZG5rr+XuCDzNabfOHyAC5NuSUvwaUl49+/cdr+bwJe7FXKubQ7E52NKwI52DuJW1LzIL0Clxq8NTPcsbh2IS+Cdr6Ul2FcWsPnmdr46gnJIccdzB3X3Mn9t92/g7ssbD/a+gXJODLL8xYN2hWHatrYRTylGfOViwkUBJenP8nTA2eUWvXRV/9lARMoaWQZrsLihtf6YS6NelZbTJATz+ulpxjJykWLZiwB1AoriyuhtNUGJkkxzRzx8MOkI8OhBMG5IqzEmDdNUfb5IvG912GbVczMVIVeXC78tshvpwF/He2U5h/NAvxjOz4/zwPy67gc/ZaM+hvYj/MPHIgrLjkd1zHtli2cUx5HYd85TYWNe8XQj+sacbzfLsfVBQ880ZvQs6SH1/3TGXzl/Zdyz68f3oHA37787C+zHLEV3j5ZcBJIuxKrBjrhHoUJXXr++JirYDReWefICMgcVhGX4NbCvyiBKNUepRBGhKlgelc3TJQfwshaEYF8hFmzFInCDnoi7uAWMBaxBg0MJBnZSJVkcJB4eABZNUmi3ojVa21qL24bvgHphoeI77uFwoEvmn7ldVzdQGHBd8pRjhfiMivBNVXplM505dnoSatVeurBdwnwXFxRyBdxhSWb/G8tw0V//xJX7/tMr/mP9vvMBfqvASd32HIXea3+oAf+Mlz7v7fhKrJOxRW9n8hC2kfOIaXeEmd+7M+46P2XEV+74UkAfPcQ3oFyIxkfRVlLIE57Vz08xHH8uA7NpkuSTHIucCUBRFrDpDXE19sYI65kFn5loISwn4AMFFPKZl/CICzGjbGHMMFaE0UEa/vRwCJpjAThZIRYCXwqQ4jNMjRVbArZY+MkwxswWQxphvbm/Cwo5yDy1U4AatJk4LLzWfOho5HclITFQVzZ39bIGZ6q4Ln3OdvgXPgHD/rv4dr2TW8/8pDX7t/w+7Satb4LVy01m5zXAfp7PO26bo7jftkPtpNwpZKfYxsT50q9JU4592R+fsotZJsa2xWkOzIo8hUsR2L4AMJvddzVnScxVMdhYgQadQd6BEwf7Yotv3SVGn3cGvujRLN3xWQvbpjs8hpZfx2VqkLSt5xmb46RiYlgZHT48eH1940O6+MMVe9j8PHb2fz47Qw8+is2Pnwr4+PDJJRpZnniLCJOA5LUkGVCZgUbVlBCdLCBNjPUWjTT/dXaA9RaWhtBRO2emxi//r+29f4823PivDPxOX0bXJdlXIe163FVXevn2fcuD/YWwf1TZs/jP6pjIG7ENZ66bp7jPo7rrHCN//9UXL/NbZI91i7hpIvPIf/sddhmzfc72j01/hSDF5GPSaqf1ISjGrF5XqzBszTK9pHI9gVKObMoSi0IGddxNtk6d2O4C8OvSLmdjLpkHE3ERxGOb5GEQm8vlb4VoIKtBmRDQVWWL72TPRc9N0uajt5Iq+hcScUQig9o2QxpJeSp8TTMoPkK0hyHkQSWFYDYIPW1IFNWMpFCzPAPP07lOSdj8qUn9Dxxhd6tjMu3zetJWpgknoc3F7DvD3Elleu8h2YNM7uwva+D07+PheX6N3BljzfjHG9/gysr3Ca/Ye8hz+DAyy+gOnAlGzZ/F5s1txm6O6s/fgz8QpFfmGKIqqLYEkrROFzWSalJ3hu5jUmP0BEIH0R4FbGbEdRCvlKid9FySBpu2aslARpnFQ2i66Sy8rm2Ougqq4wzZlVA8yXSKCKQ0A0KE7rlAdKELIixkSvyFSNItQE9CZRi0GDGPTJRieZDv2Xixm/Se8yZW3svCriGUE/z/1+AWzZoW+XqBRiqLclwvTvX+Rln7TTg7+FnEHDF4JdtxXnc7enUm3Gtz9fhusNtGzXJ5+hZczJaOZAodwFxo7FNjUl2fv6HThLuGsogMIhSEwvBCgj7QSyHknEZyo0or6KjFjwq5OhdsdxV2BsLOYW8EK1eulRKhZ+BwZQWTy5r0grrigTYVlKbGNQY1ATYICILIjRXxEYFstIislIfqqHHaNG64qOpm0T9DP/g82i61R2sP93B6/+L7VfkceVW7v9gx+vpC3q9sGM2ugy2uoXl5ZPeAHes7Sa9/fvyd5/8KAc9/xDievNJBPzO8Io3y6Tm5gQp0ofwz1iuQznNkW43UWoGYRTSt2oZJhegBYGCgdCnZ0bh6mD1sl+B3ShBiCktcl4c9Q2gvWcoC+jIcfAD0U5bKK5UQvJ5n0Fni539ylubRDmaD9/O8A/+79Zc9bu84QmuR+XZ29GZfMdW7l+b5mGazu9bju7vP4FzuYl2ofsR2xs6ez59DX/6wfdQ7F1Cc2LiCfGocJcAXh3gbd0zU9ep4ShRvkTAIRr7onX/SBQIQqF37VKCviLaip5mrT6YApn2mnJpPaXyT7KJ6mkS5jHFRdjaSDulU33yXCgEqaBi3LjSdsaoCJhSAFHJhZNF51ze0OSKDP33J+l5zquIlq3d0pWfQLsV9hCuNcjQdryzG56g6plNWu0jRjxXP4CFEwv1U+Wwj208Y4fAyIQsP/yF7PfSB0jjGKO6+wFfWt4aFFvLsI2sXcLolO9JZHwVyyLJu4L1Vo6PZmCKht79VhEt7nGNYVuGvRGXqmkEVPskjqNw+YorNE1Ps/UGEuQxxX60MT6ZryzqtL4Y8b1trQe8mx0k8oNOIgjzkNUWzVmR4xPhFkB39vU+9shf9ZuYuqjD7iaL/d9+XPPXJyK5jmNNXV9gO0nU18+rzn0nozffy4XfvA+7FeDfccCfpChKc9R1HWzWvUuwQ/NLkWeL4WtYelTbrkxSd7ukD3r3X02ur891RRPTnjVa7QBVQFXIshI2uyqq9GxM0mxFlqZImEcKZqrCEkOWCzFp6oCv6rw8gBRNZ34z0HzWpM91+iWmTQp7H0pu1bxKrdfz5OX+/w/iWv/tzmI6/ua38Vg5dnB/zDcd9UyaI0Pcdvt9C6Y94aSNv70kA1FxZX/5AmmiZMMuKlsqzrgDxTitfykZj3us9V6b1uxQBhZBz16ryPf3oakP69pWpUtHLrIVCBBNklDidDP5/FeCnt732pERrM0wuRImX0GTxiSvt2GADUNfaeW8PxIZJCc+yCygIRLkjiNgDcij0xW/zWLKh/zRlu7IFzt47leYv3X27iItu2M9LiViW5oI1XeEtp8ubzjmWdx377fIrF048NNDV24/WqOg+ZBcJQ8iRN4PMgdbeE3ajNcl1Rr10SqNWkzSzFxvnn7o3Wclpf5l2NS2DdJW33QV1yZOJwfCOFZjbcZIlP8XE0RnhZWe5fG44/gmV0QlwMbVSWM3K+axI23OZUoytXAdgxjpF+HTjWrz1EY9s2LaOiWrTtAzv0L8MO3o5U24taKeDDLUoa1vxuXl7Pay14rVxI2HyLICW8occfn4S7dfBpx2eko6islnn4L0VMGSK0VE+R4q9QmaQ3VqKGEiFBKw6ksAJxtt+v78rV7j4oNQljHFNkgSQDag9oMmX/xSmKZonLqvForOyxlXsc2YrF7HJhaRPDYCidTNAM7iRbMQbIBgT2k07BeSRvyWTivKprD5+u+x4pXvmnVQe+CDi2qevo2ac2dKaxmgJd6wvWl3P+FiPuTtf/p27nnwcDZu/Cl2bPNCqI7dVee7BryB2ZxAkhrFIhR6YfgxZeznGzCrNyOrfV97de4fwXVHUEJEAkQiTJhbH0T5GiMDBOUyEuUu0ix7fpDPv9ESoqmSNWuktTrJ6BDp+AhkGRIIYa5MUDRYsahV1IJV32FXQ+Jmg1qdNxeMGQjEnjtZOCaugewscoSnOL6/HGcy//pUu5vc2vH6eU8A+CtxCz8UcN2Ud0pDKmNC9t/nGPbb+yjWD/07Nr1rS8btLirgV4ZQC80qkjSYpNsBJFWwNShoCo0U9vAt91Pn6Zl0v1uf/mz4rQlzbk2u2mNQzKGZfTvYJRpXXhHEy4lyJQLjevlLmEe17vg+dcJcqR1b61gN0Vqh2ayjKLGVD+QDrsQt3ECWxvQdftz0q1rmjdlWI8X3bINnZFfJzz03L3q366e3EiQnA3/sX1+5s09eTJH+I88gqlyPzdJ5rPfM7qItvpzmuNearkOZTvrI3SrjzVFgEPQRn1KfY9LlaFrrUUSAcK1NY2yakI2NkDTHSeOJetqsntYcbnyzOrqB8ZFHaNSHUc0Iir2YqOhQXkix2mw7HzoecaNuyTJHtyxQz4JX1LOAWmqIowr9R//JFA+bN2BbfvB/wy18/GSTBzsG67NxWaQLlQLtFuM1XPrCTpfSilXs9863kjWa83D85uiuusEXo7wWw4tErFsxqNWRzThAJzWHzbzn1LKXh1cypZFZtaWFMSCpdTTFCJpRF8LXSmA+irXvb1SHJG6Oky/2ksuVMZFFCg2yrElgAkQCVJy2TxopccP32Wm1RVEdRyBtTLBo3fMp7X1A5/V8DBeoAvgf4N08eeUTtNOsP4GrFbhtAd+7wNsFMHNBjJ0qy155LGOjj2LjZE5/7a6SJqKvliC5QlFFvZcywLb67rfA3xhzvgVtVYxGHe5Pwy8k5JGW9hcskmbTnKyci8gJIuZmtZZGdYRqfSNxNIGl7ryi2vQ+fbDWUq/HvnyMVqfazWC/gk0JwpC9XnMOJpzM6P1z3PI5eJCctTPceDtQfoxLmwYXg7iS+dsYlryrtnUPNuBqBHaZSDGi/4XPQtNsLo6/S2Uz6KkoR6lyCJaVavgQhnwrKVZw4Mf45Zt/D7K3Z6DJpH98imtJkgSNZhSG/w/wExE5jWLyVxokRzfrkKQj5PI5oqifIAiAPI16ilpFTIhqAJLdC5yNMQ9ktQmWPe9klh492VhqKa0OE+3p/hK2PvhjcOtL7S4BrnNw1VQn4Kq2vg98G5dcd4+/+8s8HXo1LhOzRXHOxNUA71IJK+X5jNtdL2q5WVNuxvIfCPnJohSd7BxCWnWjIALsfQ78UuFRzfjudOAbm2IncyKmSAp8FaOXIbxACF6mNndMszG2T5I0luVzfWRpL9XxFM2yJiK/iwrBN4AvAIOoIlGOVce/bjqvXdzx/9Nopx1vrXy743XnwMnNoWVbsrXtH/LTbJPZpIorVLkAV7KY84bryfMc9w7gr4Cf7B64snMDP5urD/xOOTO/gJeLI71I4SzpbPs97WyTCWd7RmWwD4DswcVSYmy6z0HVIoUMNXM2Pchw1ULXgEHErBJ0da02uGR442CQJaXEZumjUb5wb18hyto3MiW/eDWLj5gSsR3FLae5PRZP+EXH6xu93QBuZcLp8i3abT02buXvXNtx7N/Ns1/d05fLPH17MbAKl8psvHYfxuUefQtXozvGbi7OuM12AyrqArPvajlVVNsav/V5K/qbevCHZTbrg3xhVkslA56eweoFd/tYD7IeFTdoRCa3WcfrVDfZ+A7iszf4bS75lt+eqMvy51ux/01+Czzwl3jgT+CK1Ud5Ekk4CbpdL2tQjkP98rgdjaJmtvJz4JeIz4RlHpuzE1w9212u7Q9JMj/LPPpkvohwNzqXIzTzGZqtxce9xteps4J7GXJfWufTrWDWDLHAgMWs1Xa2ZVe6shtq/P529GoWjj/zHN9Pxmham+P8FbAWk2lX63dlduDb5m7Qel2527ljXE988VHZlsYXS7vDsuFS4JsEW8C0BTuSOZMz6z7srkwDfnNwt1jJ8RYJ9JqgR14UhAFJPE6WaXvlFAMUQHLcyyjvJl3ATJWCZNplOV2ZHfiyezQAzlB5t0rx2pG7gvLm34/RDMDk3fpsJg8o4z1FzgjH2axmAcAPutS+K7s/x8cE8qvqUP1NQ2P1rxaWmigJLNan3luoScoZY0PcIoFPVdjiASE3ltBT7g6ArsyAxu4lSWq/Qb852ZTl94WctOpO7hbDSSJ8T/0SQipb3qzMv1ppV57qGn83EnGF5FcqPByFcmYzVYtyCfNHF7vSla3DmXZVYle6VKcrXXlqyP8fADF70AYRjJ39AAAAAElFTkSuQmCC";
	this.Cj = "data:image/png;base64,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";
	this.Hk = function() {
		var c = this.aa;
		this.jk = n;
		jQuery(".flexpaper_tbloader").hide();
		jQuery(".flexpaper_floatright").show();
		var d = eb.platform.touchdevice || 2 == jQuery(".flexpaper_floatright").length && jQuery(".flexpaper_floatright").is(":visible") && jQuery(".flexpaper_bttnI").children(0).attr("src") == this.oa.Yg || v,
			e = Math.pow(9, 3),
			f = Math.pow(6, 2),
			d = c.config.key != p && 0 < c.config.key.length && 0 <= c.config.key.indexOf("@"),
			g = "",
			g = "",
			g = ["Z1n3d0ma1n"],
			g = g[0],
			f = parseInt(f) + wa(n) + "AdaptiveUId0ma1n",
			e = ha(parseInt(e) + (d ? c.config.key.split("$")[0] : wa(n)) + g),
			f = ha(f),
			e = "$" + e.substring(11, 30).toLowerCase(),
			f = "$" + f.substring(11, 30).toLowerCase();
		(d = xa() || c.config.key == e || c.config.key == f || d && e == "$" + c.config.key.split("$")[1]) || alert("License key not accepted. Please check your configuration settings.");
		this.jk = v;
		jQuery(".flexpaper_tbloader").hide();
		(d || xa()) && jQuery(this).trigger("onPostinitialized");
	};
	this.initialize = function() {
		var c = this.aa,
			d = this;
		c.ka.prepend(String.format("<div id='modal-I' class='modal-content'><p><img src='{0}' style='display:block;padding-left:70px;padding-bottom:10px;' /></p>FlexPaper 2.3.1. Developed by Devaldi Ltd.<br/>For more information, see the <a href='http://flexpaper.devaldi.com/?ref=FlexPaper'>FlexPaper Project</a> home page</div>", "data:image/png;base64,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"));
		var e = String.format("<div class='flexpaper_floatright flexpaper_bttnI' style='display:none'><img src='{0}' /></div>", c.resources.oa.Yg);
		eb.platform.touchdevice || jQuery(c.toolbar.ca).append(e);
		jQuery(c.ga).bind("onPagesContainerCreated", function() {
			c.ga.append("<div style='-moz-user-select:none;-webkit-user-select:none;float:right;position:relative;margin-top:0px;margin-right:10px;z-index:11'><a href='#' onClick='dlInfoBox()'><img id='fpabt' src='" + d.lh + "' border='0' width='95'></a></div>");
			jQuery("#fpabt").bind("mouseover", function() {
				jQuery("#fpabt").attr("src", c.resources.Cj);
			});
			jQuery("#fpabt").bind("mouseout", function() {
				jQuery("#fpabt").attr("src", d.lh);
			});
		});
		jQuery(c.toolbar.ca).find(".flexpaper_bttnI").bind("click", function() {
			jQuery("#modal-I").smodal();
		});
		jQuery(c.toolbar.ca).bind("onProgressChanged", function(d, e) {
			1 == e && jQuery(c.ca).find(".flexpaper_bttnI").show();
		});
	};
	this.oa.bj = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACMAAAAdCAYAAAAgqdWEAAAAAXNSR0IArs4c6QAAAAlwSFlzAAALEwAACxMBAJqcGAAAAAd0SU1FB9wDCAAZMw/s7MUAAAPsSURBVEjHvVffTyJXFP4Ogq4D4ccMlAimqxiXl5I0G6P/wJZmq131tfvcTbr/QbP/zbbvW7fafehu9RXTNiHUiE9gJQQVVkdWhDhzTx/44cAMA+xqTzK5cOfm3O9+5zvn3CEYLJvNcrVaRa1WgxACd2UulwterxeKokCSJAQCAQIAai9IpVI8Pj4OWZYxNTWFu7ZcLodisYhoNIpgMAhZlskJANvb2+zz+RCbm+MWQDYC7WPcGsnmP/WMnfezsRhkWUY2mwVz8xVls1lWVRXxeBxutwc9QPqBGwbsMMZnZ+/p4OAAsdkYnKqqIhqNYlKSWLCw3WxzcxO/vHqFYYGsra9jZWXFkpm2+fx+DoVCVHlfgaPRaCAYDjMTYdDzUUcfwu/M3ByrqgqnruuA6HtS6hp5VCQw+iYb/9A0DU4AoLaCzPoYRdDWp2EeBLfj19mkUvRmCFtkycjUMAQbfPdjpUNEE8wNOCsWOuLj0XkhHkxoNzOGXSyY+cQsZthmk4kZiKGAQ7vWRsKhXWsDfHdbSzNsCtPFxQXevPmN/slksLi4RN8+eYKJexMjgZm4NwEmpl9fv8buboq+SCTw+PE38Hq9/cPEbIb/808vkU6nAQBHhSMwCySTSSwsLGBsbGxg7HRdhyzLYBY4Khzh5OQEf7x7h0q5jB+eP+/PDJhMmkmn051JoQt8qF7C6XTC7wu0wdv2JiIH168apGkaC110fKXTaeN+VtnEdtmEvb09vHjxo7WQ+s91it11j9a4Oy+7w0Q22UREEEJHo6F/UkckopvuzDbZxMRWFbc5wYzbMKMfi4QxCrhvO7gTMwBji6JHNEAPt4yGLPdyDirzxljflmbYrujZdG0wM2ZmZvHVo0cAEcYcDojmelsGHUTQhQCY8fvbt8jnc1ad3Kprm7NJURSqVCoAgIAcwJcPH370HfjPv/+iNhhFUXrKjLnOmE753dOn2NjYwL+Hh02KwdhNpbC1tQW3JBHMNz/DkRiXtRotLy9jcWmJqLX28/v3sbq62lvXOp8vTpfLhdPjYwRDn3V5fvAgjmffP8OHy0tIkgQWjFKphEq5jMqQOimVSmDBWFtbRzL5NTxuN3x+P1h0q+Ywn4PX6wVlMhmu1+sIhUII+BXTFcxUeCw+E/rFys5He81p+RiFQgHz8/NwJBIJqtfrOD8/x9l5BUzMzaLEYGI2ju2nvYbBzXJKDG49aM1x72h4h9bvk9MSisUiIpEIotHoTeB3dnbY4/FgcnIS09PTDAaBDIcyXtmo6zpGXXPG/20fN2OHxPxhHqqqIhwOIx6Pk6no7O/vc7VaxdXV1f/yrR0MBhGJRDoY/gMkxwIzT9ZCwQAAAABJRU5ErkJggg%3D%3D";
	this.oa.Li = "data:image/gif;base64,R0lGODlhBgAqAKEBAJmZmf///////////yH+GkNyZWF0ZWQgd2l0aCBHSU1QIG9uIGEgTWFjACH5BAEKAAIALAAAAAAGACoAAAIkBBJmuOjPTlIR2hlbu05TzHWSMkohSFYn+m2h561svMKlbEcFADs%3D";
	this.oa.dj = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACMAAAAdCAYAAAAgqdWEAAAAAXNSR0IArs4c6QAAAAlwSFlzAAALEwAACxMBAJqcGAAAAAd0SU1FB9wDCAAYCdD7BDYAAAMVSURBVEjHvVfdSuNQEP6mJEJjaG2aUtsorC3SKxHfwYfwB8HFfYPFB/ERll3cl7AoiBfe2gXjjbogZV1XbShoLWlmL5rEY9qkUVMHhsn5yZw53/yccwgCmabJ7XYbDw8PcBwH4yJZlpHJZJDP56EoCnK5HAEAeROOj495YmICmqahVCph3HRxcYFmswnDMKDrOjRNIwkA9vf3OZvNolKtsmsgi4aGELuSItoUkP74XKUCTdNgmiaY+0NkmiZbloVarYbJSRUBQ8KMi2NsHOL7+zs6OztDZa4CybIsGIaBtKKww87Qxb5sbQUXHjCEiPwdLi8vY2V1NRIZj7JTU1woFOj27happ6cn6MUiMxHCONYWmSHLMr5ub2Nvbw8/d3cpSqfIn6pVtiwLUq/XA5xQyF/lCkVRoCoKNE1DvV4HAKysrlOELr/Ptm2kAICY2WUI0v+OS91uF5IkYXFxEQBQr9fx4/s3CPqC7K8FABIAMDnBDBkWxCPJtm1Ml6axtr7GqqrSyckJDg8PYds2Nj9vcggqfiyl+i3ymAISDIrtql6vh1+NX2AQLS0tYWNjA7Ozszg6OvJ0iiyuST4yYEQgE99NjuNgZ2fHzy5ZlmHbtqeGQ2LH7+8bE1n531ZOmBndblewdPQ/bsywuHJSBe2lccQjM7ZvDDtjP4virOHGDEVlU2LQxIqZ59gao5sQ000UmU3JEMXNJiG4Ig+1ZLw0gAoHApg5xE2JIcQ81E084KbA0UzvLjLDoRlZxKTX1dj3BHDMokcf4KaQ03/QTQKCY8smJsSuM4lcrt5QZ0h8vqRkWcbN9TXY4VBOxJgI/Zfn58hkMqBGo8GdTgeFQgG5qfyAM4OFB0OeCWHvlSgd3pybf9e4urrC/Pw8UgsLC9TpdNBqtXDfuoV7/3SBZRalx94cBvfLNzHYZbh9HJTCGNzvvzd/0Gw2US6XYRjGc305ODhgVVWRTqcxMzPDYBBI2BQLgNCL6xi96BPbno5n6YN4+fsSlmWhWCyiVqvRQICenp5yu93G4+Pjh7y1dV1HuVz2bfgP5BfYXgA24coAAAAASUVORK5CYII%3D";
	this.oa.mj = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACMAAAAdCAYAAAAgqdWEAAAAAXNSR0IArs4c6QAAAAlwSFlzAAALEwAACxMBAJqcGAAAAAd0SU1FB9wDCAAUJ6CYRvUAAAPBSURBVEjHxZfNThtXFMd/Z/wh4VAbPKYIG1mNkeUVUkpC+gY067as0k0CPEfUXdRnaHbNl8kD2Nl4JFasmqgLjJIIwsJqQsGMLAEGz5wu7DFjYzupXZorXZ25H+fec8/5/8+9I/hKuVzWWq3G8fExrutyVSUUChGNRjFNk0gkwuTkpACIN2Fzc1PD4TDxeJyZmRmuuuzs7FCpVEilUiQSCeLxuAQBSqWSxmIxMnNz2jJQ/Yb2KdqSMqAtXbI9fj2TIR6PUy6XUW0OSblcVtu2yeVyXLs2Tpch/Yz7HGM/p2i1eijb29tkrmcI2rZNKpViLBJRV5s4cV1XDcMQgPX1dYqFQjOmIt4pOgz57dEjVldWLu3km8/3d+6wvLysgLiui2EYAMQmJnRqakoODg8w6vU6ielpVRFUBEcVCQRQEfL5vBQLBbLZLOFwuL3wpeNJbyepKuFwmGw2S7FQIJ/Pi4oggQCOKt6e38zNqW3bGI7jgIt41cAQXOTF8zwvi0VM0+Te/VUikUh/Zw8gXiQS4d79VUzT5GWxyIvneXDBwOjYt9FoYACIqrYqoqrPnj6hWCwAsLBwk1AgoPV6ve+G0sdjAPV6nVAgwMLCTQCKxQLPnj7B26slAQg23dw+mj7+/bFsbGyQyWS48e0NXVpaIhAI0Gg0+qNQ3L6AbjQaTMRj+uNPP/BVdJxXf7wSyyrhOA3u/ny3g4VGsyVeFcuySKfTrKytsbh4m/rZubx5+w7HcQZwXPoyy3Ec3rx9J/Wzc1m8/Z2srK2RTqexLKu9p6cf7MgQLcru7e3xy4MHnJ+fSy/29M04veDkuvz68GGbXaFQ6OJg2pmfgr0A6DjOQE/8GwB3s+vs7KyvXgsz7aMNlch8+sPoSacxOtqlOKx+t14LMzJamtchb4amXidmlBHDxJBhokeYREe7AIeEjKfX5RlR+TRJrwTA/qeFB2DVkcKkQxrTVJRuAI/2NhkewHI5z4z6QvqP9ILerT1KmGTIMEmvMPm8PBSbRkszF2ySUqmkC7du8aXLn69fEwyFQux/+EBi6usvZsj73R2i0ShGLBajWq1yVD1EVNqVHhJfW7r6e7UHreH1/b3/kWq1immaGPPz83J6esrR0RHVowNa789mshZVv/SqN0fRZhoVRVuVVp92S98Yre+P+39RqVRIJpOkUqkLnluWpePj44yNjTE7O6sogvj+m9QHbOl4jklHn7/trXEh24zdfb+LbdtMT0+Ty+XkEpW3tra0VqtxcnLyv/xrJxIJkslk24Z/AJjcAKCwx1DkAAAAAElFTkSuQmCC";
	this.oa.jj = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACMAAAAdCAYAAAAgqdWEAAAAAXNSR0IArs4c6QAAAAlwSFlzAAALEwAACxMBAJqcGAAAAAd0SU1FB9wDCAAWLXJ7zWkAAAL7SURBVEjHzVdRTxpBEP7G3JGABOTuCJHzoUIMT77VtOkP8keYCpEafoR/5nxqJPbNB88XtYkhtRblQgKY6E0fuDsW2Fs51KaTbJa9b3Z3dma+2YUgiOu63O/3MRgM4Ps+3kt0XUcul4NpmshkMigUCgQAFCq0221OpVIwDAPr6+t4b7m6ukKn04Ft27AsC4ZhkAYAjuNwPp9HpVrlwEAWDY0RDnpSjGmmj/DNSgWGYcB1XTCPIXJdlz3PQ61Ww+pqFjOGxBm3iLGLCD883NPFxQUqmxVonufBtm2kMxn22U+y2as8E0p+bY2LxSJ177vQHh8fYZVKzJLdDup1AoBut0sAYJomAKDRbIoYAowEDAAowCiYR41mU7YNPlSrfHpyQtrz8zPgJ3S5St9PNC8aPz09QQMAYhZdGIUoPPXu7i4A4OjoCKF+6C0JpponC3VkkAYATP5sHihzRtBPirHEK5EjxsZM9p3yTCqVmjpZOGbQspjsgNOeASORZ8AqjrGKf7J1I8+Q4zj8+dMXjrFYVWfwBtSO1jlpfw/CRCwN07fmgZTa+/WGiE1RO8Ck1N6vN/BimJiTXYoq/WUxIWdImjNxFAUTlsSUOaNN8mo+TOo8XAp7mU0Uw6Y4ihLH0/4FbAHPEMsYoCherCiIjATY1F7kOA5/3NnhmDC9+xMiXOfH6WmUwCSLYat1KKX23t5XarUOMUPtEIMKi8uXCbWXOM5bY0IC/1e3drK7iUmV3EpsoTojXSL+9mVSYMp5cTmj6zpWdF3H3e0t2Oe5FnvCV2Cydn15iVwuBzo7O+PRaIRisYjCmjkXTFnhoQVf5ao1Qp27P7e4ubnB1tYWVra3t2k0GqHX6+Gh1wUT87g4MZiYxT5soQ6DxyWXGBw0BN94thcwBL9/3/1Cp9NBuVyGbduT+nJ8fMzZbBbpdBobGxsMBoGEQwnPweA7ID4Tw2/iOFxj0kdOvP55Dc/zUCqVUKvVaK7onJ+fc7/fx3A4/Cf/tS3LQrlcjmz4C6V4EUpXdwN6AAAAAElFTkSuQmCC";
	this.oa.Ri = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACMAAAAdCAYAAAAgqdWEAAAAAXNSR0IArs4c6QAAAAlwSFlzAAALEwAACxMBAJqcGAAAAAd0SU1FB9wDCAAWE7Ma0MIAAAM+SURBVEjHxVdNT9tAEH0TESQCSsBxGhFzKOGQE5eKHxbshL8Q2/yocOWIesCcgEtUSsGxIvEhgaeHeDfrzTpJW1FGenrrnTe7Y3u86yUoFkURTyYTPD09IU1TfJSVy2VUq1XU63VUKhXs7OwQAJAQnJ+f8/r6OizLwu7uLj7arq+vMRqN4DgObNuGZVm0BgDD4ZBrtRraBwecJchqogXGGdOCa9JY+vfbbViWhSiKwDx1URRFnCQJOp0ONje3oCVSlNwqya5iHMePdHV1hfZ+G2tJksBxHGxUKpxy+ieT/dOTEVbb3uZGo0EPjw8ovb6+wm42mYnwWfh6cMBJkqD0/v4OpCADYGAB8rpd0vop60dBLJnGEXh7e0MJAIiZM0BhFPTBc7vI4uYAAJ7bNfoMYDWuBABMqQBrrLfhesezopn6chA+1zs2+hWo4/MsGZAAaZxre66nVTCRDtXvuR6ZNIbxSSYDlmCNAWaAgZ7rmr4nFv4shnVJz3Xzfs7Nl4ubJpMuAqHnuebPPPPn9Abree6SOaDWDAuQxuj13eKFRtFl7cK1qdd3SdPPxU2T4dSIfs9buPCZYhZZv+cVzqXUDAmw4JN+f4XFXOpnj3eJnfT76ny5uBJytcUkOAhPl+cy04v20v0qCE/V+XJx2aInwSqHwenCO1X0or1QHwanqn4urqQU4lzxMjEFYbjkLeVR/ERCLiheGZcVsDRSWPTDDwI2F7DUibbxNflBgNmOI/VQ5lIXPbmFQmG1Tb4fmh7NPPRE/NCs0+eaFfBqGGgJmTSqDfxw5bGVAl591/YHgVLA5l0bAPxB8Le7tgRrbMTAn9aQpmOm6U0O/MA0FheMK+NoOBzyt6MjfLZ9v7jAWrlcxv3dHezGl09L5PbmGtVqFaVarYY4jjGOH0FMEjAwlGvS+k3Xi8YQfb/ufyKOY9TrdZQODw/p5eUF4/EY8fgBWSWLxZ5Vlj8gmYahLaNZHBSfZMWHrP3z/gdGoxFarRYcx5n9mZ2dnfHW1hY2Njawt7fHYBBIOWawcsQg5SsW/aJPvRZjzFgeVW5ub5AkCZrNJjqdDs2dYS4vL3kymeD5+fm/nLVt20ar1ZI5/AZC6tu9dBzKcQAAAABJRU5ErkJggg%3D%3D";
	this.oa.$i = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACMAAAAdCAYAAAAgqdWEAAAAAXNSR0IArs4c6QAAAAlwSFlzAAALEwAACxMBAJqcGAAAAAd0SU1FB9wDCAAVHAiInpAAAAMSSURBVEjHzVfbTuJQFF2bUBOJcimMoPgwI4n8XmmJ8wXQwlz8JE184gvsEzjJjEaYDENINBrtnof2HA71lJtx4iGLda6rq7uHfShBKb7v83Q6xd3dHYIgwFsVwzCQzWZRLBaRyWRQKBQIAEhM6PV6vLW1BdM0sb+/j7cug8EA19fXqFarKJVKME2T0gBwdnbGuVwOR7UaRwZZNZpQOGJa0KYYy/FPR0cwTRO+74M5HEr5vs+ZTAaVSgUEIgJxxIjqKkNpk5jXsCyo7ehDGlbHkc8XuF6vYzgc4tfPX0y9Xo+r1So+7O1x7M5Jc3fxqMBuNGT/99NTXjUyqs6g36fHx0ekHh4eUCqXmYmwLlQjwtgmOh9rNZ5MJkg9Pz8DAUgDaFiAHMvS7inHspCwlnQ6Ak9PT0gBADFzBCiMhD44trVwZzu2BXX+AkhdAKEZpkCAYxyvw3YaK/10bach1iZB1eeZGZAAxXiu7tjOWrnEsR2K1uug6pM0A5bgGAPMAANN294ouTVtW9VWdDHfL80Ei0BoOja9Jts2HXvJNcJ56XDPsC63yPLl6zddnlnLIIOT5sv+0AwHeA8lHdqmeGZ987NJp5OOQrjwMS0L75I2JYzrH9Nsy7yHyBDrBOTCzycny6KwNDKdbjcpKjIUYdKbFVJY9MPrdPg1G9PrdDA7cRgKxLXUpCePUCis1snzupsZ8bpQ9OKYXWt2HKwGd01DrtddWVuaWefU9tzOahFxO5ue2hIcYy1cb/Eecr2OTosTdJkJ6qnNpAE0LD7U9jytkbbnIWEt6XQEDMNAyjAMjG5vwQGvjVbbnTPSarsb6Vz1+8hms0jlcjmMx2P8Hf8J//NHgIahtAVarTBC7ZY3N2+Zhuj7PRpiPB6jWCyGCeni4oJ3d3eRz+eRL5hh4hPfDJI8S1XqnJf5NOm9IlYfjW5xc3ODSqWC4+NjklLn5+e8s7OD7e1tHB4ezkwIyZdmhCjN9alt9UZIHoEEAFc/rjCZTFAul1Gv1+lFCr+8vOTpdIr7+/v/8q5dKpVwcHAgPfwDmoUpP5Jd2SsAAAAASUVORK5CYII%3D";
	this.oa.oj = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACMAAAAdCAYAAAAgqdWEAAAAAXNSR0IArs4c6QAAAAZiS0dEAP8A/wD/oL2nkwAAAAlwSFlzAAALEwAACxMBAJqcGAAAAAd0SU1FB9wEAwEtNTlGiyEAAAOcSURBVEjHxZfNbttGEMd/Q0ty5MhULFF1LPkQy0B1MuDXiN+hybXIpS1a9yGaB2lO6SPYQHPxJTmazsXOoURiVaYIAZZUWJweRFKstKTktmkXWCx3ZjmcnfnPB4XUcF1XB4MBt7e3hGHI5xrFYhHbtqnX62xsbLC1tSUAEh84OzvTUqlErVZjZ2eHzz0uLy/xPI9Wq4XjONRqNSkAnJycaLVapb2/r5GCmlY0Y2i0Ss5e5taEv9duU6vVcF0X1SlLXNfVIAjodDo8fFhhTpEs5ZJ9GIaoKqqKiGBZFiKyqoHU92/k4uKC9l6bQhAEtFotyhsbGmq48LGllhFERBBEp0SV6Ka5lolH9dEjbTQa0rvpYY3HY5ztbVUR/q/5ZH9fgyCgMJlMIMy0gphuk2E1WfJu7jt3d3cUAGK7GvCxFDNv3vzK619eA3B4eMizr54nhyaTCWtra8tcncgtAKiE8xGihigxYsb76EkqT8WyFCBkgpUN5oUonCozU85kBclQSABsu5oQHMeJZQnAWqGYeROTqwpzdzdYRrNSiQKUSqVEWKm0Dqpp/tJoWrAM4UqKGzE4CAbJ82g0glD+dlaOMKN5bsrV0n3vJgTP+21e1rJoWnSTqtk06Wj48fh4WWgDcPzD90b6Ty9frmYZVIyY0RCw5N+pjDOT5WNGyXCTzHhP9vawbZv3FxfU63VK6+uMRyN832c4HCaSm80m6w8e8Md4TK/X48tOh7hMrOQmyYimNbES3ouvX5huw6tXP8vbd28BaDQafPvNdxgTpq4YTSpqyrggSFQ8Za4SJ2ce7zyGd1NipVJJA3jRU1Flz8phEYCzy4Gq5pYDUsJFhJmoxWhK8bLLASpZyUQkzqhq9nP3upsKbQ8TNMIwnPY5UxBmRmUhr/isMsrlcvK8ublprhuWtdI3/nHVXviw3utqpqqdV5vyO72nR0fy9Ogo2euKPXBenrl3cxXjwOT/yNBxBK7UXBWLRQrFYpHup084jS/uhRVB0DDfJbqiyz5cXWLbNla1WsX3ffr+DaKSTAwrqb3M0U37PBkx7ffuNb7vU6/XsQ4ODmQ0GtHv9/H7vWlvL5r0+ek1nvEZRafpWxSNJhFN59cUj+j5uvsRz/NoNpu0Wq1Zfjk9PdVKpUK5XGZ3d1fRqScS8KXawb9kHU3qmC7sYxmzNcHJ1YcrgiBge3ubTqcjC6A6Pz/XwWDAcDj8T/61Hceh2WwmOvwJjz3t/SbNLJ0AAAAASUVORK5CYII%3D";
	this.oa.pj = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACMAAAAdCAYAAAAgqdWEAAAAAXNSR0IArs4c6QAAAAZiS0dEAP8A/wD/oL2nkwAAAAlwSFlzAAALEwAACxMBAJqcGAAAAAd0SU1FB9wEAwEuF8cLmQYAAALYSURBVEjHzVfNbtpAEP7G2CQQZCe2KYqdQwCpPuW9qtzTh2geCq55gpheSC6oSUqMhRSgCjs9+AcHvMbkp+lIq/Xs2rOz334zsyZkxPd9nk6neHp6ghACHyWapkHXdViWhXq9jqOjIwIASl64urriarUK0zRxfHyMj5bhcIjRaATXdWHbNkzTJBUAer0eG4aBTrfLsYOcdVQiHPdUoNNan863Ox2Ypgnf98EcTZHv+xyGITzPw8FBA2uOyJxLdSEEmBnMDCKCoiggorIAcRA80mAwQKfdgRqGIVzXRa1eZ8FiY7GtyBCIiEAgjgaZ4p0WIpOIcXjIzWaTxo9jKIvFAnarxUyEz2qn3S6HYQh1uVwCQooC5e1Gghpt+bbwm+fnZ6gAkOCaw4+tnHmjvLCjAgCTWI8QzokSWTTl7ZZLOLIRhZEzK+fyUJAZpxILYQenImQyS+Ugw7JUEitMBfrWaNpABgIlN7kLb3eXmDNcdExlj+I10bR5TMz50CyXS1QqFQDA94uLN23/x+VlOWTAlMsZFgCU94jgF/AXc4YhOSZazZ2229B1HT8HA1iWhereHhbzOYIgwGw2Sy07joO9/X38WSwwHo/x1fOQlIlSx0SSaKqQks6dfzuX5ZhyVZtLRhMT5xlAVP1YRk7e+aTiyi6zExP4XcrB1mhaLVNQDsBEWxLLm5KJECK650QklNpR8Rq8dxRSlFJr/I9Vu6g2fcwduCjPfPrlStM0qJqm4eHuDnbzCz5Lbm+G0HUdimEYCIIAk+ARxJQ25PTI6LQ2nqcX2UjGfj/cIwgCWJYF5ezsjObzOSaTCYLJOLrbE6f3/GyftOQdBkfpmxgcN8RjvN5n5hA/3z/8wmg0guM4cF13lV/6/T43Gg3UajWcnJwwGATKkC9zHYzHE/rRi7GsnthY9SlPbm5vEIYhWq0WPM+jDVJdX1/zdDrFbDb7J//atm3DcZzUh7/MEqJMe2pkgAAAAABJRU5ErkJggg%3D%3D";
	this.oa.Ti = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACMAAAAdCAYAAAAgqdWEAAAAAXNSR0IArs4c6QAAAAlwSFlzAAALEwAACxMBAJqcGAAAAAd0SU1FB9wDCAAUOVqXe5YAAAPASURBVEjHvVfLbttGFD1HpIxIMWRboqxY9EYKBMELbws4yKZ110m2ydIfEHQXoB9QIIsa7Q80u/Qn2trwKkaXTiE5BSoHMAQ/ZD0gwNRC4u2CIsXHkJILpwMIw+Ednrlz77kPEb7RbDZlOBzi9vYWtm3jS410Oo1cLodCoYBsNou1tTUCAN0Nx8fHsrS0hHw+j42NDXzp0Wq10G63YZomDMNAPp+nDgAHBweysrKC6uPHMlVQ/IrGDJnOTFgzNHvySrWKfD6PZrMJEUfEZrMpg8EA9XodDx8uI6RInHKLKLvIkF6vy9PTU1QrVeiDwQCmaSKTzYotHk/k70+f+Mu7d7zpdECSIgLSO//OiogICoaBvb091Go17/3K6qoUi0XedG/Ao6Mj+WpnR8Iff//mDa3RCJVKxQPzKRM4BIBSFv6u1Woh8+ABfnj7Nuxq/PnhA/XJZALY0Zt2u11sbW3h9evv7o20P/+0j0ajAcwC1Tt3PB5DBwC611Pwg47oXjlD77ggjg4AQjtsNgFA27YhtGVONEXOipPZtk0RcTEjUegoA0JpGRICxoEnWUYtI8EZZmSvHrpjwDIQACJxqWS6ECasg3lGpowQdb5ylEnK/DbnXJh3MJiLqX495Ywo3eT4VxZ1RdyaM946YR7CDBFY7DgXx8r+UxRxesEYTN1zs4ozQdl91CaSDGOGoynBTbhfNykwg25iTDQRRDxl7m4ZgiQjmCHLUFQAEHrk5qKJLbFYMhIUAZwpgdXlQNc0txAuUg7muknXNCeaksoBRJ0R+/0+Wv+0MJnYnEwmd0wms6FpGlKpFHq9vqOIqFsRPa7IfP3NLv74/Tfs7/84t09xbxvXRvhbjd3db2P9G1u1Xzx7jlKxKFdXV9R0PXANzTU3puUirIQIXEuKr0Uora9jZ+fJvKodjSZqKT55+jSOI6Jw2cI9sACJeYZxJd9/QCqVwng8xq/v3/Py4gKZTMZPRpKEZVkoPXqEl69eUdd1JYaKe+l0Gno6ncb15SWM4roiqwV1FNupuH99/AjLsmL50el0AHH2KzFC4/NZC7lcDjw5OZHRaIRisYi11UIkdkWRVLhg5kvCcPdcdy5xfn6OWq2G1Pb2NkejEfr9Pnr9GwhFnKQkEDrNhzu7P3ePQBwG0Elk7nfwybzZJ8P0+er6Au12G+VyGaZpzsLg8PBQlpeXkclksLm5KXAsHGiMfFfyFxAG3vnXLsZs9ox49vkMg8EApVIJ9XqdkaTTaDRkOBzCsqz/5b+2YRgol8ueDv8CmAsGVvaUUeAAAAAASUVORK5CYII%3D";
	this.oa.Hi = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACMAAAAdCAYAAAAgqdWEAAAAAXNSR0IArs4c6QAAAAlwSFlzAAALEwAACxMBAJqcGAAAAAd0SU1FB9wDCAARF/s2ghwAAAMOSURBVEjHvVfRTuJAFD2XUBOQgJQSIvVhhRie/LbVAv7BSmE/Sl999sH6om5iyLouSkOimGjvPrSdDmVaCsadyc3pTKenZ+7cuZ0SpOI4Ds9mM7y8vMDzPHxV0TQN5XIZtVoNxWIR1WqVAIDCARcXF7y1tQVd17G7u4uvLre3txiPxzBNE4ZhQNd1ygPA2dkZVyoVtNptDgSyLDShcICU0qYYivv7rRZ0XYfjOGD2b5HjOOy6LjqdDra3S4gJSRKXRWyWws/PT3R9fY3Wfgs513VhmiYKxSJ7foWEacaxceyxx0dH3xHj4ITx8NhDZWeH6/U6Jk8T5N7e3mA0GsxE+KxZx8cEYO3nvrXb7Lou8h8fH4CX6HKKIRLa6FpHUZ+nfJZS+PH+/o48AFAYQcvxkSlmrO7x4hsE3eqYkXnyvlu9+A5hxS5R7qau1V2aMZOXRc3SLvTFROJUXqAEQdSzrASVtM5OW/SM9CqFZ1iZSnpda1UGSs0zqnyVlwJulXBx3etb6TPf8EsSxAynLdNC6fe6q6PS58uym5aXiTnbVE76/WxbhL3NPQOmtN3kCznprZHkaaOYyfktUSmGog5HP7NrUXORijfsB4Ccn6SEcQwXbDTMJiiBixN4fYWhGCZRISFUfcPRKFMAZzTBG4mJCkkY9ssIZoY9HHJ6ADMkDhlJ5pHeFS2T9AmFhEjoA5jItkfpAZzNIl7hmQ1tkCBoEy4pgIUPISES+hba9mCoCGDOaoJHCmBhHMM043DcwB6qVokVyEk80jIpYz0t78TH4dS2N8kzgkfTNOQ0TcPjwwPY40/bj9OBL2bN5+5ublAul0GXl5c8n89Rr9dR3aktHcFYcaChjP8raRzhmMe/D7i/v8fBwQFyh4eHNJ/PMZ1O8TydIIjkcPFYxtDCMYxYGg2eg3RPoHQPwfWfx98Yj8doNpswTTM6kZ2fn3OpVEKhUMDe3h6DQSBpUtJxMOgXx7qFPrkdckQonHj36w6u66LRaKDT6dDSF/Tq6opnsxleX1//y7+2YRhoNptCwz/m1JcKyxmy4QAAAABJRU5ErkJggg%3D%3D";
	this.oa.Ji = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACMAAAAdCAYAAAAgqdWEAAAAAXNSR0IArs4c6QAAAAlwSFlzAAALEwAACxMBAJqcGAAAAAd0SU1FB9wDCAATL+ECWAAAAAMHSURBVEjHvVdRT+JAEP6GFBOQgJRyROrDCTE8+dvEAvcPTgr+KH312QfrE5oYcp6n0pAoJtq5B7btsm2BHnpDvsxud/djdmc6syVI4jgOT6dTvLy8wPM8fJVks1kUi0VUKhXk83mUy2UCAPInXFxc8NbWFnRdx+7uLr5aRqMRxuMxTNOEYRjQdZ00ADg7O+NSqYRGs8nCQJYNTRAWmpb0SdHB+H6jAV3X4TgOmOdD5DgOu66LVquF7e0CFEOSjFvH2HWEn5+f6Pr6Go39BjKu68I0TeTyefbmP0g6Ee32kTqPY/qqjvCWdna4Wq3i8ekRmbe3Nxi1GjMR0gAArONjSrsuDt+bTXZdF5mPjw/AA8UAMRpBX0in3YY0hxbmRHWUR+D9/R0ZACBmFoCkkfAM5Eecb5DVhjK+LgJeAHNjmDwfrGi1HUAVq3Osrl8FmZ9DY0A+SNFqO0CcdKyOvH4VZH4KjAEHYEUDzFhsc5gxYqRrWUlcvMi78J/hyShBpYCUtugvkW7HWsEZgzBm2AcpOhGrpNuzKIYzjpeYOHQTs5ca60iv20nFJ2KGfLCik7Gm/Oj1ZO443uCoNYgYkorYZ9UdAMBgeOrzJ3EGz7V50otU4k8xaDg49XdKMVU9Uv01EcBxVVpeSEm7ST6RIfNqf8r/JdwUpnf6jCuEPRiAmWnFhiI8mghgSth16pOx7eHSpLiMR5Odtqn07eFGXJpftTd1k90fzEtFyptexE1SmP3T29S3B+CUd+Dktyk56lfGzIltJ+WRdQKY5M8XLZvN4uH+Hkb1W2of/zzpg73NI+72ZoRisQi6vLzk2WyGarWK8k4l4kz1jBETPEnfK8s4/DkPf+5xd3eHg4MDZA4PD2k2m2EymeB58ghx/xTOm19efB1cQMQcBs/T90I1F31VS2MQ7d8PvzAej1Gv12GaZphfzs/PuVAoIJfLYW9vj8EgkLQp+XpHkAsILTyT+z5HqINDvLm9geu6qNVqaLVaFAmqq6srnk6neH19/S/f2oZhoF6vBzb8BbU1MGBi7Vj6AAAAAElFTkSuQmCC";
	this.oa.Qi = "data:image/png;base64,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";
	this.oa.Wl = "";
	this.oa.Xl = "";
	this.oa.Yl = "";
	this.oa.bm = "";
	this.oa.$l = "";
	this.oa.em = "";
	this.oa.fm = "";
	this.oa.gm = "";
	this.oa.dm = "";
	this.oa.hm = "";
};
window.dlInfoBox = function() {
	jQuery("#modal-I").smodal();
};

function wa(g) {
	var c = window.location.href.toString();
	0 == c.length && (c = document.URL.toString());
	if (g) {
		var d;
		d = c.indexOf("///");
		0 <= d ? d += 3 : (d = c.indexOf("//"), d = 0 <= d ? d + 2 : 0);
		c = c.substr(d);
		d = c.indexOf(":");
		var e = c.indexOf("/");
		0 < d && 0 < e && d < e || (0 < e ? d = e : (e = c.indexOf("?"), d = 0 < e ? e : c.length));
		c = c.substr(0, d);
	}
	if (g && (g = c.split("."))) {
		if (d = g.length, !(2 >= d)) {
			if (!(e = -1 != "co,com,net,org,web,gov,edu,".indexOf(g[d - 2] + ","))) {
				b: {
					for (var e = ".ac.uk .ab.ca .bc.ca .mb.ca .nb.ca .nf.ca .nl.ca .ns.ca .nt.ca .nu.ca .on.ca .pe.ca .qc.ca .sk.ca .yk.ca".split(" "), f = 0; f < e.length;) {
						if (-1 !== c.indexOf(e[f], c.length - e[f].length)) {
							e = n;
							break b;
						}
						f++;
					}
					e = v;
				}
			}
			c = e ? g[d - 3] + "." + g[d - 2] + "." + g[d - 1] : g[d - 2] + "." + g[d - 1];
		}
	}
	return c;
}

function xa() {
	var g = wa(v);
	return true;
}
var ua = function() {
		function g() {}
		g.prototype = {
			Ic: function(c, d) {
				if (d.Va && (d.og || d.create(d.pages.da), !d.initialized)) {
					c.yb = d.yb = c.config.MixedMode;
					var e = d.la;
					0 == jQuery(e).length && (e = jQuery(d.bc).find(d.la));
					if (d.ba == L) {
						var f = 0 != d.pageNumber % 2 ? "flexpaper_zine_page_left" : "flexpaper_zine_page_right";
						d.aa.Zf || (f = 0 != d.pageNumber % 2 ? "flexpaper_zine_page_left_noshadow" : "flexpaper_zine_page_right_noshadow");
						jQuery(e).append("<div id='" + d.na + "_canvascontainer' style='height:100%;width:100%;position:relative;'><canvas id='" + c.Ba(1, d) + "' style='background-repeat:no-repeat;background-size:" + (!eb.browser.safari && !eb.browser.mozilla || !eb.platform.mac ? "cover" : "100% 100%") + ";position:relative;left:0px;top:0px;height:100%;width:100%;background-color:#ffffff;display:none;' class='flexpaper_interactivearea flexpaper_grab flexpaper_hidden flexpaper_flipview_canvas flexpaper_flipview_page' width='100%' height='100%' ></canvas><canvas id='" + c.Ba(2, d) + "' style='position:relative;left:0px;top:0px;width:100%;height:100%;display:block;background-color:#ffffff;display:none;' class='flexpaper_border flexpaper_interactivearea flexpaper_grab flexpaper_rescale flexpaper_flipview_canvas_highres flexpaper_flipview_page' width='100%' height='100%'></canvas><div id='" + d.na + "_textoverlay' style='position:absolute;left:0px;top:0px;width:100%;height:100%;' class='" + f + "'></div></div>");
						if (eb.browser.chrome || eb.browser.safari) {
							eb.browser.safari && (jQuery("#" + c.Ba(1, d)).css("-webkit-backface-visibility", "hidden"), jQuery("#" + c.Ba(2, d)).css("-webkit-backface-visibility", "hidden")), jQuery("#" + d.na + "_textoverlay").css("-webkit-backface-visibility", "hidden");
						}
						eb.browser.mozilla && (jQuery("#" + c.Ba(1, d)).css("backface-visibility", "hidden"), jQuery("#" + c.Ba(2, d)).css("backface-visibility", "hidden"), jQuery("#" + d.na + "_textoverlay").css("backface-visibility", "hidden"));
					}
					d.initialized = n;
				}
			},
			hl: function(c, d) {
				if (d.ba == L && 0 == jQuery("#" + c.Ba(1, d)).length || d.ba == L && d.za) {
					return v;
				}
				d.ba == L && (d.context == p && !d.Lb && !d.za) && (d.rc(), d.Lb = n);
				return n;
			},
			dl: function(c, d) {
				return 1 == d.scale || 1 < d.scale && d.pageNumber == d.pages.ja - 1 || d.pageNumber == d.pages.ja - 2;
			},
			Eb: function(c, d, e, f) {
				1 == d.scale && eb.browser.safari ? (jQuery("#" + c.Ba(1, d)).css("-webkit-backface-visibility", "hidden"), jQuery("#" + c.Ba(2, d)).css("-webkit-backface-visibility", "hidden"), jQuery("#" + d.na + "_textoverlay").css("-webkit-backface-visibility", "hidden")) : eb.browser.safari && (jQuery("#" + c.Ba(1, d)).css("-webkit-backface-visibility", "visible"), jQuery("#" + c.Ba(2, d)).css("-webkit-backface-visibility", "visible"), jQuery("#" + d.na + "_textoverlay").css("-webkit-backface-visibility", "visible"));
				!(d.ba == L && 0 == jQuery("#" + c.Ba(1, d)).length) && !(d.ba == L && d.za) && (d.ba == L && 1 < d.scale ? d.pageNumber == d.pages.ja - 1 || d.pageNumber == d.pages.ja - 2 ? (jQuery(c).trigger("UIBlockingRenderingOperation", {
					ia: c.ia,
					Gk: n
				}), magnifier = 2236 * Math.sqrt(1 / (d.Sa() * d.Xa())), d.wa.width = d.Sa() * magnifier, d.wa.height = d.Xa() * magnifier) : (c.cg = v, d.wa.width = 2 * d.Sa(), d.wa.height = 2 * d.Xa(), d.za = n, jQuery("#" + d.qb).ce(), c.Kh(d), eb.platform.touchdevice && (c.ng != p && window.clearTimeout(c.ng), c.ng = setTimeout(x(), 1500)), f != p && f()) : d.yb && c.pageImagePattern ? (d.dimensions.loaded || c.Lc(d.pageNumber + 1, n, function() {
					c.Rb(d);
				}, n), d.im = n, d.ua == p ? (d.lc = n, d.ua = new Image, jQuery(d.ua).bind("load", function() {
					jQuery(d.Db).remove();
					jQuery(d.wa).css("background-image", "url('" + c.xa(d.pageNumber + 1) + "')");
					d.lc = v;
				}), jQuery(d.ua).bind("error", function() {
					jQuery(d.Db).remove();
					jQuery(d.wa).css("background-image", "url('" + c.xa(d.pageNumber + 1) + "')");
					d.lc = v;
				}), jQuery(d.ua).attr("src", c.xa(d.pageNumber + 1)), c.ng = setTimeout(function() {
					d.lc && ("none" == jQuery(d.wa).css("background-image") && jQuery(d.wa).css("background-image", "url('" + c.xa(d.pageNumber + 1) + "')"), d.lc = v);
				}, 6000)) : d.lc || "none" == jQuery(d.wa).css("background-image") && jQuery(d.wa).css("background-image", "url('" + c.xa(d.pageNumber + 1) + "')"), c.qd(d, f)) : (d.wa.width = 1 * d.Sa(), d.wa.height = 1 * d.Xa()));
			},
			Pl: function(c, d) {
				return d.ba == L;
			},
			Kg: function(c, d) {
				d.ba == L && (1 < d.scale ? (d.qb = c.Ba(2, d), d.Ud = c.Ba(1, d)) : (d.qb = c.Ba(1, d), d.Ud = c.Ba(2, d)));
			},
			qd: function(c, d) {
				d.ba == L && (1 < d.scale ? (jQuery("#" + c.Ba(1, d)).hf(), jQuery("#" + c.Ba(2, d)).ce()) : (jQuery("#" + c.Ba(1, d)).ce(), jQuery("#" + c.Ba(2, d)).hf()), (!d.yb || !(c.pageImagePattern && 1 == d.scale)) && jQuery(d.Db).remove());
				jQuery(c).trigger("UIBlockingRenderingOperationCompleted", {
					ia: c.ia
				});
			}
		};
		CanvasPageRenderer.prototype.pf = function(c) {
			return c.ba == L ? 1 : 1.4;
		};
		CanvasPageRenderer.prototype.pd = function(c, d, e) {
			var f = this;
			if (f.pageThumbImagePattern != p && 0 < f.pageThumbImagePattern.length) {
				for (var g = 0, l = p, u = c.getDimensions(d)[d - 1].width / c.getDimensions(d)[d - 1].height, m = 1; m < d; m++) {
					g += 2;
				}
				var q = 1 == d ? g + 1 : g,
					y = new Image;
				jQuery(y).bind("load", function() {
					var l = d % 10;
					0 == l && (l = 10);
					var m = c.ka.find(".flexpaper_fisheye").find(String.format('*[data-thumbIndex="{0}"]', l)).get(0);
					m.width = e * u - 2;
					m.height = e / u / 2 - 2;
					var t = jQuery(m).parent().width() / m.width;
					m.getContext("2d").fillStyle = "#999999";
					var z = (m.height - m.height * u) / 2,
						I = m.height * u;
					0 > z && (m.height += m.width - I, z += (m.width - I) / 2);
					eb.browser.msie && jQuery(m).css({
						width: m.width * t + "px",
						height: m.height * t + "px"
					});
					jQuery(m).data("origwidth", m.width * t);
					jQuery(m).data("origheight", m.height * t);
					m.getContext("2d").fillRect(1 == d ? m.width / 2 : 0, z, q == c.getTotalPages() ? m.width / 2 + 2 : m.width + 2, I + 2);
					m.getContext("2d").drawImage(y, 1 == d ? m.width / 2 + 1 : 1, z + 1, m.width / 2, I);
					if (1 < d && g + 1 <= c.getTotalPages() && q + 1 <= c.getTotalPages()) {
						var B = new Image;
						jQuery(B).bind("load", function() {
							m.getContext("2d").drawImage(B, m.width / 2 + 1, z + 1, m.width / 2, I);
							m.getContext("2d").strokeStyle = "#999999";
							m.getContext("2d").moveTo(m.width - 1, z);
							m.getContext("2d").lineTo(m.width - 1, I + 1);
							m.getContext("2d").stroke();
							jQuery(c).trigger("onThumbPanelThumbAdded", {
								Ad: l,
								thumbData: m
							});
						});
						jQuery(B).attr("src", f.xa(q + 1, 200));
					} else {
						jQuery(c).trigger("onThumbPanelThumbAdded", {
							Ad: l,
							thumbData: m
						});
					}
				});
				q <= c.getTotalPages() && jQuery(y).attr("src", f.xa(q, 200));
			} else {
				if (-1 < f.Ia(p) || 1 != c.scale) {
					window.clearTimeout(f.Bd), f.Bd = setTimeout(function() {
						f.pd(c, d, e);
					}, 50);
				} else {
					g = 0;
					l = p;
					u = c.getDimensions(d)[d - 1].width / c.getDimensions(d)[d - 1].height;
					for (m = 1; m < d; m++) {
						g += 2;
					}
					var q = 1 == d ? g + 1 : g,
						y = new Image,
						z = d % 10;
					0 == z && (z = 10);
					l = c.ka.find(".flexpaper_fisheye").find(String.format('*[data-thumbIndex="{0}"]', z)).get(0);
					l.width = e * u;
					l.height = e / u / 2;
					m = jQuery(l).parent().width() / l.width;
					eb.browser.msie && jQuery(l).css({
						width: l.width * m + "px",
						height: l.height * m + "px"
					});
					jQuery(l).data("origwidth", l.width * m);
					jQuery(l).data("origheight", l.height * m);
					var t = l.height / f.getDimensions()[q - 1].height;
					f.Ea(p, "thumb_" + q);
					f.Pa.getPage(q).then(function(m) {
						var r = m.getViewport(t),
							y = l.getContext("2d"),
							G = document.createElement("canvas");
						G.height = l.height;
						G.width = G.height * u;
						var I = {
							canvasContext: G.getContext("2d"),
							viewport: r,
							zf: p,
							pageNumber: q,
							continueCallback: function(g) {
								1 != c.scale || q != this.pageNumber ? (window.clearTimeout(f.Bd), f.Bd = setTimeout(function() {
									f.pd(c, d, e);
								}, 50)) : g();
							}
						};
						m.render(I).promise.then(function() {
							var m = (l.height - l.height * u) / 2,
								s = l.height * u;
							0 > m && (l.height += l.width - s, m += (l.width - s) / 2);
							f.Ea(p, -1, "thumb_" + q);
							1 < d && g + 1 <= c.getTotalPages() && q + 1 <= c.getTotalPages() ? -1 < f.Ia(p) || 1 != c.scale ? (window.clearTimeout(f.Bd), f.Bd = setTimeout(function() {
								f.pd(c, d, e);
							}, 50)) : (f.Ea(p, "thumb_" + (q + 1)), f.Pa.getPage(q + 1).then(function(g) {
								r = g.getViewport(t);
								var k = document.createElement("canvas");
								k.width = G.width;
								k.height = G.height;
								I = {
									canvasContext: k.getContext("2d"),
									viewport: r,
									zf: p,
									pageNumber: q + 1,
									continueCallback: function(g) {
										1 != c.scale || q + 1 != this.pageNumber ? (window.clearTimeout(f.Bd), f.Bd = setTimeout(function() {
											f.pd(c, d, e);
										}, 50)) : g();
									}
								};
								g.render(I).promise.then(function() {
									f.Ea(p, -1);
									y.fillStyle = "#ffffff";
									y.fillRect(1 == d ? l.width / 2 : 0, m, l.width / 2, s);
									1 != d && y.fillRect(l.width / 2, m, l.width / 2, s);
									y.drawImage(G, 1 == d ? l.width / 2 : 0, m, l.width / 2, s);
									1 != d && y.drawImage(k, l.width / 2, m, l.width / 2, s);
									jQuery(c).trigger("onThumbPanelThumbAdded", {
										Ad: z,
										thumbData: l
									});
								}, function() {
									f.Ea(p, -1, "thumb_" + (q + 1));
								});
							})) : (y.fillStyle = "#ffffff", y.fillRect(1 == d ? l.width / 2 : 0, m, l.width / 2, s), 1 != d && y.fillRect(l.width / 2, m, l.width / 2, s), y.drawImage(G, 1 == d ? l.width / 2 : 0, m, l.width / 2, s), jQuery(c).trigger("onThumbPanelThumbAdded", {
								Ad: z,
								thumbData: l
							}));
						}, function() {
							f.Ea(p, -1);
						});
					});
				}
			}
		};
		return g;
	}(),
	ta = function() {
		function g() {}
		g.prototype = {
			Ic: function(c, d) {
				if (d.Va && (d.og || d.create(d.pages.da), !d.initialized)) {
					c.ib = c.zg != p && 0 < c.zg.length && eb.platform.touchonlydevice && !eb.platform.bd;
					if (d.ba == L) {
						var e = 0 != d.pageNumber % 2 ? "flexpaper_zine_page_left" : "flexpaper_zine_page_right";
						d.aa.Zf || (e = 0 != d.pageNumber % 2 ? "flexpaper_zine_page_left_noshadow" : "flexpaper_zine_page_right_noshadow");
						var f = d.la;
						0 == jQuery(f).length && (f = jQuery(d.bc).find(d.la));
						c.mf(d, f);
						c.sb ? jQuery(f).append("<canvas id='" + d.na + "_canvas' class='flexpaper_flipview_page' height='100%' width='100%' style='z-index:10;position:absolute;left:0px;top:0px;width:100%;height:100%;'></canvas><canvas id='" + d.na + "_canvas_highres' class='flexpaper_flipview_page' height='100%' width='100%' style='display:none;z-index:10;position:absolute;left:0px;top:0px;width:100%;height:100%;background-color:#ffffff;'></canvas><div id='" + d.na + "_textoverlay' style='z-index:11;position:absolute;left:0px;top:0px;width:100%;height:100%;' class='" + e + "'></div>") : jQuery(f).append("<canvas id='" + d.na + "_canvas' class='flexpaper_flipview_page' height='100%' width='100%' style='z-index:10;position:absolute;left:0px;top:0px;width:100%;height:100%;'></canvas><canvas id='" + d.na + "_canvas_highres' class='flexpaper_flipview_page' height='100%' width='100%' style='image-rendering:-webkit-optimize-contrast;display:none;z-index:10;position:absolute;left:0px;top:0px;width:100%;height:100%;'></canvas><div id='" + d.na + "_textoverlay' style='z-index:11;position:absolute;left:0px;top:0px;width:100%;height:100%;' class='" + e + "'></div>");
						if (eb.browser.chrome || eb.browser.safari) {
							eb.browser.safari && (jQuery("#" + d.na + "_canvas").css("-webkit-backface-visibility", "hidden"), jQuery("#" + d.na + "_canvas_highres").css("-webkit-backface-visibility", "hidden")), jQuery("#" + d.na + "_textoverlay").css("-webkit-backface-visibility", "hidden");
						}
					}
					d.initialized = n;
				}
			},
			Eb: function(c, d, e, f) {
				d.initialized || c.Ic(d);
				if (!d.za && d.ba == L) {
					if (-1 < c.Ia(d) && !(d.pageNumber == d.pages.ja || d.pageNumber == d.pages.ja - 2 || d.pageNumber == d.pages.ja - 1)) {
						if (window.clearTimeout(d.Pb), d.pageNumber == d.pages.ja || d.pageNumber == d.pages.ja - 2 || d.pageNumber == d.pages.ja - 1) {
							d.Pb = setTimeout(function() {
								c.Eb(d, e, f);
							}, 250);
						}
					} else {
						if (!d.za) {
							c.sm = d.scale;
							c.Ea(d, d.pageNumber);
							1 == d.scale && d.rc();
							d.lc = n;
							if (!d.ua || d.rk != d.scale || c.Di(d)) {
								d.rk = d.scale, d.ua = new Image, jQuery(d.ua).bind("load", function() {
									d.lc = v;
									d.Qd = n;
									d.Ae = this.height;
									d.Be = this.width;
									d.Jb();
									c.Wb(d);
									d.dimensions.Aa > d.dimensions.width && (d.dimensions.width = d.dimensions.Aa, d.dimensions.height = d.dimensions.Oa);
								}), jQuery(d.ua).bind("abort", function() {
									d.lc = v;
									c.Ea(d, -1);
								}), jQuery(d.ua).bind("error", function() {
									d.lc = v;
									c.Ea(d, -1);
								});
							}
							1 >= d.scale ? jQuery(d.ua).attr("src", c.xa(d.pageNumber + 1, p, c.sb)) : c.ib && 1 < d.scale ? d.pageNumber == d.pages.ja - 1 || d.pageNumber == d.pages.ja - 2 ? jQuery(d.ua).attr("src", c.xa(d.pageNumber + 1, p, c.sb)) : jQuery(d.ua).attr("src", c.qa) : d.pageNumber == d.pages.ja - 1 || d.pageNumber == d.pages.ja - 2 ? (c.sb && (-1 == jQuery(d.ua).attr("src").indexOf(".svg") || d.wk != d.scale) && c.Ia(d) == d.pageNumber && (d.pageNumber == d.pages.ja - 1 || d.pageNumber == d.pages.ja - 2) ? (jQuery(c).trigger("UIBlockingRenderingOperation", c.ia), d.wk = d.scale, jQuery(d.ua).attr("src", c.xa(d.pageNumber + 1, p, c.sb))) : d.yi == d.scale && (jQuery(d.la + "_canvas_highres").show(), jQuery(d.la + "_canvas").hide()), c.sb || jQuery(d.ua).attr("src", c.xa(d.pageNumber + 1, p, c.sb))) : jQuery(d.ua).attr("src", c.qa);
						}
						jQuery(d.la).removeClass("flexpaper_load_on_demand");
						!d.za && (jQuery(d.Da).attr("src") == c.qa && d.Qd) && c.Wb(d);
						f != p && f();
					}
				}
			},
			Wb: function(c, d) {
				if (d.ba == L) {
					jQuery(d.la).removeClass("flexpaper_hidden");
					jQuery(".flexpaper_pageLoader").hide();
					1 == d.scale && eb.browser.safari ? (jQuery("#" + d.na + "_canvas").css("-webkit-backface-visibility", "hidden"), jQuery("#" + d.na + "_canvas_highres").css("-webkit-backface-visibility", "hidden"), jQuery("#" + d.na + "_textoverlay").css("-webkit-backface-visibility", "hidden")) : eb.browser.safari && (jQuery("#" + d.na + "_canvas").css("-webkit-backface-visibility", "visible"), jQuery("#" + d.na + "_canvas_highres").css("-webkit-backface-visibility", "visible"), jQuery("#" + d.na + "_textoverlay").css("-webkit-backface-visibility", "visible"));
					if (c.Di(d)) {
						1 == d.scale ? (jQuery(d.va).css("background-image", "url('" + c.xa(d.pageNumber + 1, p, c.sb) + "')"), c.Sb(d)) : (d.pageNumber == d.pages.ja - 1 || d.pageNumber == d.pages.ja - 2 ? jQuery(d.va).css("background-image", "url('" + c.xa(d.pageNumber + 1) + "')") : jQuery(d.va).css("background-image", "url(" + c.qa + ")"), jQuery(d.la + "_canvas").hide(), c.ib && d.scale > d.we() && (d.Pb = setTimeout(function() {
							c.dc(d);
							jQuery(".flexpaper_flipview_canvas_highres").show();
							jQuery(".flexpaper_flipview_canvas").hide();
						}, 500)));
					} else {
						var e = document.getElementById(d.na + "_canvas");
						jQuery(d.va).css("background-image", "url(" + c.qa + ")");
						if (1 == d.scale && e && (100 == e.width || jQuery(e).hasClass("flexpaper_redraw"))) {
							var f = e;
							if (f) {
								f.width = d.Sa();
								f.height = d.Xa();
								var g = f.getContext("2d");
								g.Le = g.mozImageSmoothingEnabled = g.imageSmoothingEnabled = n;
								g.drawImage(d.ua, 0, 0, d.Sa(), d.Xa());
								jQuery(e).removeClass("flexpaper_redraw");
								1 == d.scale && (jQuery(d.la + "_canvas").show(), jQuery(d.la + "_canvas_highres").hide());
								1 < d.pageNumber && jQuery(d.la + "_pixel").css({
									width: 2 * d.Sa(),
									height: 2 * d.Xa()
								});
								c.Sb(d);
							}
						} else {
							1 == d.scale && (e && 100 != e.width) && (jQuery(d.la + "_canvas").show(), jQuery(d.la + "_canvas_highres").hide(), c.Sb(d));
						} if (1 < d.scale) {
							if (f = document.getElementById(d.na + "_canvas_highres")) {
								if ((c.sb && d.yi != d.scale || 100 == f.width || jQuery(f).hasClass("flexpaper_redraw")) && (d.pageNumber == d.pages.ja - 1 || d.pageNumber == d.pages.ja - 2)) {
									d.yi = d.scale;
									jQuery(c).trigger("UIBlockingRenderingOperation", c.ia);
									e = 1000 < d.ga.width() || 1000 < d.ga.height() ? 1 : 2;
									g = (d.ga.width() - 30) * d.scale;
									if (eb.platform.ios && (1500 < g * d.ve() || 535 < d.Nd())) {
										e = 2236 * Math.sqrt(1 / (d.Sa() * d.Xa()));
									}
									eb.browser.safari && (!eb.platform.touchdevice && 3 > e) && (e = 3);
									g = f.getContext("2d");
									g.Le || g.mozImageSmoothingEnabled || g.imageSmoothingEnabled ? (g.Le = g.mozImageSmoothingEnabled = g.imageSmoothingEnabled = v, c.sb ? (f.width = d.Sa() * e, f.height = d.Xa() * e, g.drawImage(d.ua, 0, 0, d.Sa() * e, d.Xa() * e)) : (f.width = d.ua.width, f.height = d.ua.height, g.drawImage(d.ua, 0, 0))) : (f.width = d.Sa() * e, f.height = d.Xa() * e, g.drawImage(d.ua, 0, 0, d.Sa() * e, d.Xa() * e));
									c.sb ? c.uk(d, f.width / d.ua.width, function() {
										jQuery(f).removeClass("flexpaper_redraw");
										jQuery(d.la + "_canvas_highres").show();
										jQuery(d.la + "_canvas").hide();
										jQuery(d.la + "_canvas_highres").addClass("flexpaper_flipview_canvas_highres");
										jQuery(d.la + "_canvas").addClass("flexpaper_flipview_canvas");
										c.Ea(d, -1);
									}) : (jQuery(f).removeClass("flexpaper_redraw"), jQuery(d.la + "_canvas_highres").show(), jQuery(d.la + "_canvas").hide(), jQuery(d.la + "_canvas_highres").addClass("flexpaper_flipview_canvas_highres"), jQuery(d.la + "_canvas").addClass("flexpaper_flipview_canvas"), c.ib && jQuery(d.la + "_canvas_highres").css("z-index", "-1"));
								} else {
									jQuery(d.la + "_pixel").css({
										width: 2 * d.Sa(),
										height: 2 * d.Xa()
									}), jQuery(d.la + "_canvas_highres").show(), jQuery(d.la + "_canvas").hide(), c.ib && jQuery(d.la + "_canvas_highres").css("z-index", "-1");
								}
							}
							d.Pb = setTimeout(function() {
								c.dc(d);
							}, 500);
						}
					}
					d.za = 0 < jQuery(d.va).length;
				}
			},
			unload: function(c, d) {
				d.ua = p;
				jQuery(d.va).css("background-image", "url(" + c.qa + ")");
				var e = document.getElementById(d.na + "_canvas");
				e && (e.width = 100, e.height = 100);
				if (e = document.getElementById(d.na + "_canvas_highres")) {
					e.width = 100, e.height = 100;
				}
			}
		};
		ImagePageRenderer.prototype.Di = function(c) {
			return eb.platform.touchdevice && (eb.platform.Jc || 5000000 < c.Be * c.Ae || eb.platform.android) && (eb.platform.Jc || eb.platform.android);
		};
		ImagePageRenderer.prototype.resize = function(c, d) {
			this.mf(d);
		};
		ImagePageRenderer.prototype.uk = function(c, d, e) {
			var f = this;
			window.Rg = d;
			jQuery.ajax({
				type: "GET",
				url: f.xa(c.pageNumber + 1, p, f.sb),
				cache: n,
				dataType: "xml",
				success: function(g) {
					var l = new Image;
					jQuery(l).bind("load", function() {
						var f = document.getElementById(c.na + "_canvas"),
							m = document.getElementById(c.na + "_canvas_highres").getContext("2d");
						m.Le = m.mozImageSmoothingEnabled = m.imageSmoothingEnabled = v;
						var q = f.getContext("2d");
						q.Le = q.mozImageSmoothingEnabled = q.imageSmoothingEnabled = v;
						f.width = c.ua.width * d;
						f.height = c.ua.height * d;
						q.drawImage(l, 0, 0, c.ua.width * d, c.ua.height * d);
						if (c.wi) {
							y = c.wi;
						} else {
							var y = [];
							jQuery(g).find("image").each(function() {
								var c = {};
								c.id = jQuery(this).attr("id");
								c.width = X(jQuery(this).attr("width"));
								c.height = X(jQuery(this).attr("height"));
								c.data = jQuery(this).attr("xlink:href");
								c.dataType = 0 < c.data.length ? c.data.substr(0, 15) : "";
								y[y.length] = c;
								jQuery(g).find("use[xlink\\:href='#" + c.id + "']").each(function() {
									if (jQuery(this).attr("transform") && (c.transform = jQuery(this).attr("transform"), c.transform = c.transform.substr(7, c.transform.length - 8), c.Bf = c.transform.split(" "), c.x = X(c.Bf[c.Bf.length - 2]), c.y = X(c.Bf[c.Bf.length - 1]), "g" == jQuery(this).parent()[0].nodeName && jQuery(this).parent().attr("clip-path") != p)) {
										var d = jQuery(this).parent().attr("clip-path"),
											d = d.substr(5, d.length - 6);
										jQuery(g).find("*[id='" + d + "']").each(function() {
											c.ne = [];
											jQuery(this).find("path").each(function() {
												var d = {};
												d.d = jQuery(this).attr("d");
												c.ne[c.ne.length] = d;
											});
										});
									}
								});
							});
							c.wi = y;
						}
						for (q = 0; q < y.length; q++) {
							if (y[q].ne) {
								for (var z = 0; z < y[q].ne.length; z++) {
									for (var t = y[q].ne[z].d.replace(/M/g, "M\x00").replace(/m/g, "m\x00").replace(/v/g, "v\x00").replace(/l/g, "l\x00").replace(/h/g, "h\x00").replace(/c/g, "c\x00").replace(/s/g, "s\x00").replace(/z/g, "z\x00").split(/(?=M|m|v|h|s|c|l|z)|\0/), s = 0, r = 0, w = 0, G = 0, I = v, B, J = n, F = 0; F < t.length; F += 2) {
										if ("M" == t[F] && t.length > F + 1 && (B = Y(t[F + 1]), w = s = X(B[0]), G = r = X(B[1]), J && (I = n)), "m" == t[F] && t.length > F + 1 && (B = Y(t[F + 1]), w = s += X(B[0]), G = r += X(B[1]), J && (I = n)), "l" == t[F] && t.length > F + 1 && (B = Y(t[F + 1]), s += X(B[0]), r += X(B[1])), "h" == t[F] && t.length > F + 1 && (B = Y(t[F + 1]), s += X(B[0])), "v" == t[F] && t.length > F + 1 && (B = Y(t[F + 1]), r += X(B[0])), "s" == t[F] && t.length > F + 1 && (B = Y(t[F + 1])), "c" == t[F] && t.length > F + 1 && (B = Y(t[F + 1])), "z" == t[F] && t.length > F + 1 && (s = w, r = G, B = p), I && (m.save(), m.beginPath(), J = I = v), "M" == t[F] || "m" == t[F]) {
											m.moveTo(s, r);
										} else {
											if ("c" == t[F] && B != p) {
												for (var S = 0; S < B.length; S += 6) {
													var V = s + X(B[S + 0]),
														ba = r + X(B[S + 1]),
														E = s + X(B[S + 2]),
														T = r + X(B[S + 3]),
														N = s + X(B[S + 4]),
														K = r + X(B[S + 5]);
													m.bezierCurveTo(V, ba, E, T, N, K);
													s = N;
													r = K;
												}
											} else {
												"s" == t[F] && B != p ? (E = s + X(B[0]), T = r + X(B[1]), N = s + X(B[2]), K = r + X(B[3]), m.bezierCurveTo(s, r, E, T, N, K), s = N, r = K) : "z" == t[F] ? (m.lineTo(s, r), m.closePath(), m.clip(), m.drawImage(f, 0, 0), m.restore(), J = n, F--) : m.lineTo(s, r);
											}
										}
									}
								}
							} else {
								fa("no clip path for image!");
							}
						}
						e && e();
					});
					l.src = f.xa(c.pageNumber + 1);
				}
			});
		};
		ImagePageRenderer.prototype.pd = function(c, d, e) {
			for (var f = this, g = 0, l = c.getDimensions(d)[d - 1].Aa / c.getDimensions(d)[d - 1].Oa, u = 1; u < d; u++) {
				g += 2;
			}
			var m = 1 == d ? g + 1 : g,
				q = new Image;
			jQuery(q).bind("load", function() {
				var u = d % 10;
				0 == u && (u = 10);
				var z = jQuery(".flexpaper_fisheye").find(String.format('*[data-thumbIndex="{0}"]', u)).get(0);
				z.width = e * l - 2;
				z.height = e / l / 2 - 2;
				var t = jQuery(z).parent().width() / z.width;
				z.getContext("2d").fillStyle = "#999999";
				var s = (z.height - z.height * l) / 2,
					r = z.height * l;
				0 > s && (z.height += z.width - r, s += (z.width - r) / 2);
				jQuery(z).data("origwidth", z.width * t);
				jQuery(z).data("origheight", z.height * t);
				(eb.browser.msie || eb.browser.safari && 5 > eb.browser.vb) && jQuery(z).css({
					width: z.width * t + "px",
					height: z.height * t + "px"
				});
				z.getContext("2d").fillRect(1 == d ? z.width / 2 : 0, s, m == c.getTotalPages() ? z.width / 2 + 2 : z.width + 2, r + 2);
				z.getContext("2d").drawImage(q, 1 == d ? z.width / 2 + 1 : 1, s + 1, z.width / 2, r);
				if (1 < d && g + 1 <= c.getTotalPages() && m + 1 <= c.getTotalPages()) {
					var w = new Image;
					jQuery(w).bind("load", function() {
						z.getContext("2d").drawImage(w, z.width / 2 + 1, s + 1, z.width / 2, r);
						z.getContext("2d").strokeStyle = "#999999";
						z.getContext("2d").moveTo(z.width - 1, s);
						z.getContext("2d").lineTo(z.width - 1, r + 1);
						z.getContext("2d").stroke();
						jQuery(c).trigger("onThumbPanelThumbAdded", {
							Ad: u,
							thumbData: z
						});
					});
					jQuery(w).attr("src", f.xa(m + 1, 200));
				} else {
					jQuery(c).trigger("onThumbPanelThumbAdded", {
						Ad: u,
						thumbData: z
					});
				}
			});
			m <= c.getTotalPages() && jQuery(q).attr("src", f.xa(m, 200));
		};
		return g;
	}(),
	ra = function() {
		function g() {}
		Z.prototype.bf = function() {
			var c = this.df(0),
				c = c.Aa / c.Oa,
				d = Math.round(this.ga.height() - 10);
			this.aa.ka.find(".flexpaper_fisheye");
			this.aa.ha.te && !this.aa.PreviewMode && (d -= eb.platform.touchonlydevice ? 0 : 0.15 * this.ga.height());
			var e = this.ga.width();
			2 * d * c > e - 44 && !this.aa.ha.hb && (d = e / 2 / c - 75);
			d * c > e - 44 && this.aa.ha.hb && (d = e / c - 100);
			if (!eb.browser.xm) {
				for (var e = 2.5 * Math.floor(d * (!this.aa.ha.hb ? 2 : 1) * c), f = 0; 0 != e % 4 && 20 > f;) {
					d += 0.5, e = 2.5 * Math.floor(d * (!this.aa.ha.hb ? 2 : 1) * c), f++;
				}
			}
			return d;
		};
		Z.prototype.ik = function(c, d) {
			var e = this;
			c = parseInt(c);
			e.aa.Ug = d;
			e.aa.renderer.Xc && e.ud(c);
			1 != this.aa.scale ? e.Qa(1, n, function() {
				e.aa.turn("page", c);
			}) : e.aa.turn("page", c);
		};
		Z.prototype.ig = function() {
			return (this.ga.width() - this.Fc()) / 2;
		};
		Z.prototype.Fc = function() {
			var c = this.df(0),
				c = c.Aa / c.Oa;
			return Math.floor(this.bf() * (!this.aa.ha.hb ? 2 : 1) * c);
		};
		Z.prototype.$c = function() {
			if (this.aa.ba == L) {
				return 0 < this.width ? this.width : this.width = this.ea(this.da).width();
			}
		};
		Z.prototype.Nd = function() {
			if (this.aa.ba == L) {
				return 0 < this.height ? this.height : this.height = this.ea(this.da).height();
			}
		};
		g.prototype = {
			ud: function(c, d) {
				for (var e = d - 10; e < d + 10; e++) {
					0 < e && e + 1 < c.aa.getTotalPages() + 1 && !c.getPage(e).initialized && (c.getPage(e).Va = n, c.aa.renderer.Ic(c.getPage(e)), c.getPage(e).Va = v);
				}
			},
			cc: function(c) {
				c.Qc != p && (window.clearTimeout(c.Qc), c.Qc = p);
				var d = 1 < c.ja ? c.ja - 1 : c.ja;
				if (!c.aa.renderer.pb || c.aa.renderer.yb && 1 == c.aa.scale) {
					1 <= c.ja ? (c.pages[d - 1].load(function() {
						1 < c.ja && c.pages[d] && c.pages[d].load(function() {
							c.pages[d].Ka();
							for (var e = c.ea(c.da).scrollTop(), f = 0; f < c.document.numPages; f++) {
								c.Wa(f) && (c.pages[f].Zb(e, c.ea(c.da).height(), n) ? (c.pages[f].Va = n, c.pages[f].load(x()), c.pages[f].Ka()) : c.pages[f].unload());
							}
						});
					}), c.pages[d - 1].Ka()) : c.pages[d] && c.pages[d].load(function() {
						c.pages[d].Ka();
						for (var e = c.ea(c.da).scrollTop(), f = 0; f < c.document.numPages; f++) {
							c.Wa(f) && (c.pages[f].Zb(e, c.ea(c.da).height(), n) ? (c.pages[f].Va = n, c.pages[f].load(x()), c.pages[f].Ka()) : c.pages[f].unload());
						}
					});
				} else {
					1 < c.ja ? (c.pages[d - 1] && c.pages[d - 1].load(x()), c.pages[d - 0] && c.pages[d - 0].load(x())) : c.pages[d] && c.pages[d].load(x());
					for (var e = c.ea(c.da).scrollTop(), f = 0; f < c.document.numPages; f++) {
						c.Wa(f) && (c.pages[f].Zb(e, c.ea(c.da).height(), n) ? (c.pages[f].Va = n, c.pages[f].load(x()), c.pages[f].Ka()) : c.pages[f].unload());
					}
				}
			},
			xg: function(c) {
				c.bg = setTimeout(function() {
					c.aa.pages && c.aa.ba == L && (1.1 < c.aa.scale ? (c.ea(c.da + "_panelLeft").fadeTo("fast", 0), c.ea(c.da + "_panelRight").fadeTo("fast", 0), c.aa.Ha.data().opts.cornerDragging = v) : (1 < c.ja ? c.ea(c.da + "_panelLeft").fadeTo("fast", 1) : c.ea(c.da + "_panelLeft").fadeTo("fast", 0), c.aa.sa < c.aa.getTotalPages() && c.ea(c.da + "_panelRight").fadeTo("fast", 1), c.aa.Ha && c.aa.Ha.data().opts && (c.aa.Ha.data().opts.cornerDragging = n)));
				}, 1000);
			},
			Ab: function(c) {
				return c.aa.ba == L && !(eb.browser.safari && 7 <= eb.browser.vb && !eb.platform.touchdevice);
			},
			Qa: function(c, d, e, f, g) {
				jQuery(c).trigger("onScaleChanged");
				if (c.aa.ba == L && (e >= 1 + c.aa.document.ZoomInterval ? jQuery(".flexpaper_page, " + c.da).removeClass("flexpaper_page_zoomIn").addClass("flexpaper_page_zoomOut") : jQuery(".flexpaper_page, " + c.da).removeClass("flexpaper_page_zoomOut").addClass("flexpaper_page_zoomIn"), jQuery(c.da).data().totalPages)) {
					var l = c.df(0),
						u = l.Aa / l.Oa,
						l = c.bf() * e,
						u = 2 * l * u;
					if (f && c.Ab() && (!(1 < d) || c.ea(c.da + "_parent").Pd())) {
						if (!c.animating || !c.ih) {
							c.animating = n, c.ih = f.Cc, jQuery(".flexpaper_flipview_canvas").show(), jQuery(".flexpaper_flipview_canvas_highres").hide(), jQuery(c).trigger("onScaleChanged"), l = 300, f && f.Fb && f.Xb ? (f.Cc && (f.Fb += c.ig()), !f.Cc && !eb.platform.touchonlydevice ? (d = c.ea(c.da + "_parent").css("transformOrigin").split(" "), 2 == d.length ? (d[0] = d[0].replace("px", ""), d[1] = d[1].replace("px", ""), c.zc = parseFloat(d[0]), c.Ac = parseFloat(d[1])) : (c.zc = f.Fb, c.Ac = f.Xb), c.gi = n) : (c.zc = f.Fb, c.Ac = f.Xb), f.ge && (l = f.ge)) : (c.zc = 0, c.Ac = 0), c.aa.renderer.pb && (c.aa.renderer.ib && 1 == e) && (d = 1 < c.ja ? c.ja - 1 : c.ja, 1 < c.ja && c.aa.renderer.Sb(c.pages[d - 1]), c.aa.renderer.Sb(c.pages[d])), "undefined" != f.ge && (l = f.ge), e >= 1 + c.aa.document.ZoomInterval ? ("preserve-3d" == c.ea(c.da + "_parent").css("transform-style") && (l = 0), (f.Cc || eb.platform.touchonlydevice) && c.ea(c.da + "_parent").css({
								transformOrigin: c.zc + "px " + c.Ac + "px"
							}), c.aa.Ha.turn("setCornerDragging", v)) : (c.ea(c.da).transition({
								x: 0,
								y: 0
							}, 0), c.aa.Ha.turn("setCornerDragging", n)), d = 1 == c.aa.sa && !c.aa.ha.hb ? -(c.Fc() / 4) : 0, c.ea(c.da + "_parent").transition({
								x: d,
								y: 0,
								scale: e
							}, l, "snap", function() {
								c.xd != p && (window.clearTimeout(c.xd), c.xd = p);
								c.xd = setTimeout(function() {
									for (var d = 0; d < c.document.numPages; d++) {
										c.pages[d].za = v;
									}
									c.mc = 0;
									c.cd = 0;
									c.pc();
									c.animating = v;
									c.ih = v;
								}, 50);
								1 == e && c.ea(c.da + "_parent").css("-webkit-transform-origin:", "");
								g != p && g();
							});
						}
					} else {
						if (c.ea(c.da + "_parent").Pd() && e >= 1 + c.aa.document.ZoomInterval && ((d = c.lg()) ? (c.ea(c.da + "_parent").transition({
							transformOrigin: "0px 0px"
						}, 0), c.ea(c.da + "_parent").transition({
							x: 0,
							y: 0,
							scale: 1
						}, 0), f.Fb = d.left, f.Xb = d.top, f.Cc = n) : (d = 1 == c.aa.sa && !c.aa.ha.hb ? -(c.Fc() / 4) : 0, c.ea(c.da + "_parent").transition({
							x: d,
							y: 0,
							scale: 1
						}, 0))), c.ea(c.da).Pd() && c.ea(c.da).transition({
							x: 0,
							y: 0,
							scale: 1
						}, 0), !c.animating) {
							c.uf || (c.uf = c.aa.Ha.width(), c.Ik = c.aa.Ha.height());
							1 == e && c.uf ? (turnwidth = c.uf, turnheight = c.Ik) : (turnwidth = u - (c.ea(c.da + "_panelLeft").width() + c.ea(c.da + "_panelRight").width() + 40), turnheight = l);
							c.ea(c.da).css({
								width: u,
								height: l
							});
							c.aa.Ha.turn("size", turnwidth, turnheight, v);
							e >= 1 + c.aa.document.ZoomInterval ? (f.Cc || eb.platform.touchonlydevice) && requestAnim(function() {
								c.ga.scrollTo({
									left: jQuery(c.ga).scrollLeft() + f.Fb / e + "px",
									top: jQuery(c.ga).scrollTop() + f.Xb / e + "px"
								});
							}, 500) : c.Gd();
							for (l = 0; l < c.document.numPages; l++) {
								c.Wa(l) && (c.pages[l].za = v);
							}
							1 < e ? c.aa.Ha.turn("setCornerDragging", v) : (c.ea(c.da + "_panelLeft").show(), c.ea(c.da + "_panelRight").show(), c.aa.Ha.turn("setCornerDragging", n));
							c.pc();
							setTimeout(function() {
								g != p && g();
							}, 200);
						}
					}
				}
			},
			resize: function(c, d, e, f) {
				c.width = -1;
				c.height = -1;
				jQuery(".flexpaper_pageword_" + c.ia + ", .flexpaper_interactiveobject_" + c.ia).remove();
				if (c.aa.ba == L) {
					1 == c.aa.sa && !c.aa.ha.hb ? jQuery(c.da + "_parent").transition({
						x: -(c.Fc() / 4)
					}, 0, "snap", x()) : c.aa.ha.hb || jQuery(c.da + "_parent").transition({
						x: 0
					}, 0, "snap", x());
					e = c.bf();
					var g = c.Fc();
					c.ea(c.da + "_parent").css({
						width: d,
						height: e
					});
					c.Uc = g;
					c.ke = e;
					d = c.ig();
					var l = 22 < d / 2 ? 22 : d / 2;
					c.aa.Ha && c.aa.Ha.turn("size", g, e, v);
					c.ea(c.da + "_panelLeft").css({
						"margin-left": d - l,
						width: l,
						height: e - 30
					});
					c.ea(c.da + "_arrowleft").css({
						top: (e - 30) / 2 + "px"
					});
					c.ea(c.da + "_arrowright").css({
						top: (e - 30) / 2 + "px"
					});
					c.ea(c.da + "_panelRight").css({
						width: l,
						height: e - 30
					});
					c.aa.PreviewMode ? (jQuery(c.da + "_arrowleftbottom").hide(), jQuery(c.da + "_arrowleftbottommarker").hide(), jQuery(c.da + "_arrowrightbottom").hide(), jQuery(c.da + "_arrowrightbottommarker").hide()) : (jQuery(c.da + "_arrowleftbottom").show(), jQuery(c.da + "_arrowleftbottommarker").show(), jQuery(c.da + "_arrowrightbottom").show(), jQuery(c.da + "_arrowrightbottommarker").show());
					c.uf = p;
					c.Rm = p;
				}
				jQuery(".flexpaper_flipview_page").addClass("flexpaper_redraw");
				for (e = 0; e < c.document.numPages; e++) {
					c.Wa(e) && c.pages[e].Qa();
				}
				c.aa.ba == L ? setTimeout(function() {
					for (var d = 0; d < c.document.numPages; d++) {
						c.Wa(d) && (c.pages[d].za = v, c.aa.renderer.resize != p && c.aa.renderer.resize(c.aa.renderer, c.pages[d]));
					}
					c.pc();
					jQuery(c.aa).trigger("onResizeCompleted");
					f && f();
				}, 300) : f && f();
			},
			ad: function(c, d) {
				c.aa.PreviewMode ? c.aa.openFullScreen() : (c.aa.ba == L ? d ? c.Qa(2.5, {
					Fb: jQuery(c.da + "_parent").width() / 2,
					Xb: jQuery(c.da + "_parent").height() / 2
				}) : c.Qa(2.5, {
					Fb: c.$b,
					Xb: c.ac
				}) : c.Qa(1), c.dd());
			},
			kc: function(c, d) {
				c.aa.ba == L ? c.Qa(1, n, d) : c.Qa(window.FitHeightScale);
				c.dd();
			},
			wg: function(c) {
				c.aa.ba == L && (this.touchwipe = c.ea(c.da).touchwipe({
					wipeLeft: function() {
						c.Ef = n;
						setTimeout(function() {
							c.Ef = v;
						}, 800);
						c.de = p;
						c.ma == p && !c.aa.Ha.turn("cornerActivated") && !c.animating && 1 == c.aa.scale && c.next();
					},
					wipeRight: function() {
						c.Ef = n;
						setTimeout(function() {
							c.Ef = v;
						}, 800);
						c.de = p;
						!c.aa.Ha.turn("cornerActivated") && !c.animating && c.ma == p && 1 == c.aa.scale && c.previous();
					},
					preventDefaultEvents: n,
					min_move_x: 100,
					min_move_y: 100
				}));
			},
			mh: function(c) {
				eb.platform.touchdevice && !c.aa.ha.se && c.ea(c.da).doubletap(function(d) {
					c.de = p;
					if (c.aa.ba == H || c.aa.ba == D || c.aa.ba == L) {
						(c.aa.ba == H || c.aa.ba == D) && 1 != c.aa.scale ? c.ad() : 1 == c.aa.scale && c.aa.ba == L ? c.ad() : c.aa.ba == L && 1 <= c.aa.scale && !c.Ag ? c.kc() : c.aa.ba == H && 1 == c.aa.scale && c.kc(), d.preventDefault(), c.Ag = v;
					}
				}, p, 300);
			},
			Uf: function(c, d) {
				if (c.aa.ba == L) {
					var e = c.bf(),
						f = c.Fc(),
						g = c.ig(),
						l = 22 < g / 2 ? 22 : g / 2,
						g = g - l;
					20 > l && (l = 20);
					var u = c.aa.ha.fd ? c.aa.ha.fd : "#555555";
					c.Uc = f;
					c.ke = e;
					d.append("<div id='" + c.container + "_parent' style='width:100%;height:" + e + "px;z-index:10" + (eb.browser.mozilla && eb.platform.mac && (!eb.platform.mac || !(18 > parseFloat(eb.browser.version) || 33 < parseFloat(eb.browser.version))) ? ";transform-style:preserve-3d;" : "") + "'><div id='" + c.container + "_panelLeft' class='flexpaper_arrow' style='cursor:pointer;opacity: 0;margin-top:15px;-moz-border-radius-topleft: 10px;border-top-left-radius: 10px;-moz-border-radius-bottomleft: 10px;border-bottom-left-radius: 10px;position:relative;float:left;background-color:" + u + ";left:0px;top:0px;height:" + (e - 30) + "px;width:" + l + "px;margin-left:" + g + "px;-moz-user-select:none;-webkit-user-select:none;-ms-user-select:none;user-select: none;'><div style='position:relative;left:" + (l - (l - 0.4 * l)) / 2 + "px;top:" + (e / 2 - l) + "px' id='" + c.container + "_arrowleft' class='flexpaper_arrow'></div><div style='position:absolute;left:" + (l - (l - 0.55 * l)) / 2 + "px;bottom:0px;margin-bottom:10px;' id='" + c.container + "_arrowleftbottom' class='flexpaper_arrow flexpaper_arrow_start'></div><div style='position:absolute;left:" + (l - 0.8 * l) + "px;bottom:0px;width:2px;margin-bottom:10px;' id='" + c.container + "_arrowleftbottommarker' class='flexpaper_arrow flexpaper_arrow_start'></div></div><div id='" + c.container + "' style='float:left;position:relative;height:" + e + "px;width:" + f + "px;margin-left:0px;z-index:10;-moz-user-select:none;-webkit-user-select:none;-ms-user-select:none;user-select: none;' class='flexpaper_twopage_container flexpaper_hidden'></div><div id='" + c.container + "_panelRight' class='flexpaper_arrow' style='cursor:pointer;opacity: 0;margin-top:15px;-moz-border-radius-topright: 10px;border-top-right-radius: 10px;-moz-border-radius-bottomright: 10px;border-bottom-right-radius: 10px;position:relative;float:left;background-color:" + u + ";left:0px;top:0px;height:" + (e - 30) + "px;width:" + l + "px;-moz-user-select:none;-webkit-user-select:none;-ms-user-select:none;user-select: none;'><div style='position:relative;left:" + (l - (l - 0.4 * l)) / 2 + "px;top:" + (e / 2 - l) + "px' id='" + c.container + "_arrowright' class='flexpaper_arrow'></div><div style='position:absolute;left:" + (l - (l - 0.55 * l)) / 2 + "px;bottom:0px;margin-bottom:10px;' id='" + c.container + "_arrowrightbottom' class='flexpaper_arrow flexpaper_arrow_end'></div><div style='position:absolute;left:" + ((l - (l - 0.55 * l)) / 2 + l - 0.55 * l) + "px;bottom:0px;width:2px;margin-bottom:10px;' id='" + c.container + "_arrowrightbottommarker' class='flexpaper_arrow flexpaper_arrow_end'></div></div></div>");
					e = ja(u);
					jQuery(c.da + "_panelLeft").css("background-color", "rgba(" + e.r + "," + e.g + "," + e.b + "," + c.aa.ha.tg + ")");
					jQuery(c.da + "_panelRight").css("background-color", "rgba(" + e.r + "," + e.g + "," + e.b + "," + c.aa.ha.tg + ")");
					jQuery(c.da + "_arrowleft").Dg(l - 0.4 * l);
					jQuery(c.da + "_arrowleftbottom").Dg(l - 0.55 * l);
					jQuery(c.da + "_arrowleftbottommarker").Pk(l - 0.55 * l, jQuery(c.da + "_arrowleftbottom"));
					jQuery(c.da + "_arrowright").vf(l - 0.4 * l);
					jQuery(c.da + "_arrowrightbottom").vf(l - 0.55 * l);
					jQuery(c.da + "_arrowrightbottommarker").Qk(l - 0.55 * l, jQuery(c.da + "_arrowrightbottom"));
					c.aa.ug || (jQuery(c.da + "_panelLeft").attr("id", c.da + "_panelLeft_disabled").css("visibility", "none"), jQuery(c.da + "_panelRight").attr("id", c.da + "_panelRight_disabled").css("visibility", "none"));
					c.aa.PreviewMode && (jQuery(c.da + "_arrowleftbottom").hide(), jQuery(c.da + "_arrowleftbottommarker").hide(), jQuery(c.da + "_arrowrightbottom").hide(), jQuery(c.da + "_arrowrightbottommarker").hide());
					jQuery(c.da).on(c.aa.ha.Dd ? "mouseup" : "mousedown", function(d) {
						if (jQuery(d.target).hasClass("flexpaper_mark")) {
							return v;
						}
						var e = n;
						if (c.aa.ha.Dd) {
							c.Ci();
							if (c.nb != p && (!d.pageX || !d.pageY || !(d.pageX <= c.nb + 2 && d.pageX >= c.nb - 2 && d.pageY <= c.Kb + 2 && d.pageY >= c.Kb - 2))) {
								e = v;
							}
							c.nb = p;
							c.Kb = p;
							c.Sd && eb.browser.safari && (jQuery(".flexpaper_flipview_canvas_highres").show(), jQuery(".flexpaper_flipview_canvas").hide(), c.Sd = v);
						}
						if ((!c.aa.ha.Dd || e) && !c.aa.ha.se) {
							var e = v,
								f = 0 < jQuery(d.target).parents(".flexpaper_page").children().find(".flexpaper_zine_page_left, .flexpaper_zine_page_left_noshadow").length;
							c.Fe = f ? c.aa.sa - 2 : c.aa.sa - 1;
							jQuery(d.target).hasClass("flexpaper_interactiveobject_" + c.ia) && (e = n);
							if (c.aa.Ha.turn("cornerActivated") || c.animating || jQuery(d.target).hasClass("turn-page-wrapper") || jQuery(d.target).hasClass("flexpaper_shadow") && jQuery(d.target).Pd()) {
								return;
							}
							if (c.aa.PreviewMode) {
								c.aa.openFullScreen();
								return;
							}
							eb.platform.bd || (f = jQuery(c.da).xe(d.pageX, d.pageY), !e && !c.aa.vc && 1 == c.aa.scale ? c.aa.Zoom(2.5, {
								Cc: n,
								Fb: f.x,
								Xb: f.y
							}) : !e && (!c.aa.vc && 1 < c.aa.scale) && c.aa.Zoom(1, {
								Cc: n,
								Fb: f.x,
								Xb: f.y
							}));
							var g = {};
							jQuery(jQuery(d.target).attr("class").split(" ")).each(function() {
								"" !== this && (g[this] = this);
							});
							for (class_name in g) {
								0 == class_name.indexOf("gotoPage") && c.gotoPage(parseInt(class_name.substr(class_name.indexOf("_") + 1)));
							}
						}
						if (c.aa.renderer.pb && c.aa.renderer.ib && 1 < c.aa.scale) {
							var k = 1 < c.ja ? c.ja - 1 : c.ja;
							setTimeout(function() {
								1 < c.aa.scale ? (1 < c.ja && c.aa.renderer.dc(c.pages[k - 1]), c.aa.renderer.dc(c.pages[k])) : (1 < c.ja && c.aa.renderer.Sb(c.pages[k - 1]), c.aa.renderer.Sb(c.pages[k]));
							}, 500);
						}
					});
					jQuery(c.da + "_parent").on("mousemove", function(d) {
						if (1 < c.aa.scale) {
							if (c.aa.ha.Dd && "down" == c.aa.Vh) {
								c.nb || (c.nb = d.pageX, c.Kb = d.pageY), !c.Sd && eb.browser.safari && (jQuery(".flexpaper_flipview_canvas").show(), jQuery(".flexpaper_flipview_canvas_highres").hide(), c.Sd = n), !eb.platform.touchdevice && !c.ea(c.da + "_parent").Pd() ? (c.ga.scrollTo({
									left: jQuery(c.ga).scrollLeft() + (c.nb - d.pageX) + "px",
									top: jQuery(c.ga).scrollTop() + (c.Kb - d.pageY) + "px"
								}, 0, {
									axis: "xy"
								}), c.nb = d.pageX + 3, c.Kb = d.pageY + 3) : (c.gi && (c.Ci(), c.gi = v), c.uh(d.pageX, d.pageY));
							} else {
								if (!c.aa.ha.Dd) {
									var e = c.ga.xe(d.pageX, d.pageY);
									!eb.platform.touchdevice && !c.ea(c.da + "_parent").Pd() && c.ga.scrollTo({
										left: d.pageX + "px",
										top: d.pageY + "px"
									}, 0, {
										axis: "xy"
									});
									d = e.x / jQuery(c.da + "_parent").width();
									e = e.y / jQuery(c.da + "_parent").height();
									c.Xe((jQuery(c.ga).width() + 150) * d - 20, (jQuery(c.ga).height() + 150) * e - 250);
								}
							}
							c.aa.renderer.pb && (c.aa.renderer.ib && !c.aa.ha.Dd) && (e = 1 < c.ja ? c.ja - 1 : c.ja, 1 < c.aa.scale ? (1 < c.ja && c.aa.renderer.dc(c.pages[e - 1]), c.aa.renderer.dc(c.pages[e])) : (1 < c.ja && c.aa.renderer.Sb(c.pages[e - 1]), c.aa.renderer.Sb(c.pages[e])));
						}
					});
					jQuery(c.da + "_parent").on("touchmove", function(d) {
						if (!eb.platform.ios && 2 == d.originalEvent.touches.length) {
							d.preventDefault && d.preventDefault();
							d.returnValue = v;
							var e = Math.sqrt((d.originalEvent.touches[0].pageX - d.originalEvent.touches[1].pageX) * (d.originalEvent.touches[0].pageX - d.originalEvent.touches[1].pageX) + (d.originalEvent.touches[0].pageY - d.originalEvent.touches[1].pageY) * (d.originalEvent.touches[0].pageY - d.originalEvent.touches[1].pageY)),
								e = 2 * e;
							if (c.ma == p) {
								c.lb = c.aa.scale, c.Ee = e;
							} else {
								c.aa.Ha.turn("setCornerDragging", v);
								1 > c.ma && (c.ma = 1);
								2.5 < c.ma && !eb.platform.Jc && (c.ma = 2.5);
								c.aa.renderer.ib && (4 < c.ma && eb.platform.De) && (c.ma = 4);
								!c.aa.renderer.ib && (2.5 < c.ma && eb.platform.De) && (c.ma = 2.5);
								var f = 1 == c.aa.sa && !c.aa.ha.hb ? -(c.Fc() / 4) : 0;
								c.ea(c.da + "_parent").transition({
									x: f,
									y: 0,
									scale: c.ma
								}, 0, "ease", x());
							}
							c.ma = c.lb + (e - c.Ee) / jQuery(c.da + "_parent").width();
						}
						if (1 < c.aa.scale || c.ma != p && 1 < c.ma) {
							e = d.originalEvent.touches[0] || d.originalEvent.changedTouches[0], !eb.platform.ios && 2 == d.originalEvent.touches.length ? c.nb || (f = d.originalEvent.touches[1] || d.originalEvent.changedTouches[1], f.pageX > e.pageX ? (c.nb = e.pageX + (f.pageX - e.pageX) / 2, c.Kb = e.pageY + (f.pageY - e.pageY) / 2) : (c.nb = f.pageX + (e.pageX - f.pageX) / 2, c.Kb = f.pageY + (e.pageY - f.pageY) / 2)) : c.nb || (c.nb = e.pageX, c.Kb = e.pageY), c.Sd || (jQuery(".flexpaper_flipview_canvas").show(), jQuery(".flexpaper_flipview_canvas_highres").hide(), c.Sd = n), c.uh(e.pageX, e.pageY), d.preventDefault();
						}
					});
					jQuery(c.da + "_parent, " + c.da).on("touchstart", function() {
						c.de = (new Date).getTime();
					});
					jQuery(c.da + "_parent").on("touchend", function(d) {
						c.aa.Fd && c.ma == p && !c.Ef && !c.aa.Ha.turn("cornerActivated") && !c.animating ? setTimeout(function() {
							!jQuery(d.target).hasClass("flexpaper_arrow") && 1 == c.aa.scale && c.de && c.de > (new Date).getTime() - 1000 ? 0 == c.aa.ha.Rc.position().top ? c.aa.ha.Rc.animate({
								opacity: 0,
								top: "-" + c.aa.ha.Rc.height() + "px"
							}, 300) : c.aa.ha.Rc.animate({
								opacity: 1,
								top: "0px"
							}, 300) : c.de = p;
						}, 600) : c.aa.Fd && 0 == c.aa.ha.Rc.position().top && c.aa.ha.Rc.animate({
							opacity: 0,
							top: "-" + c.aa.ha.Rc.height() + "px"
						}, 300);
						!eb.platform.ios && c.lb != p && (c.Ag = c.lb < c.ma, c.lb = p, c.Ee = p, c.ma = p, c.nb = p, c.Kb = p);
						if (1 < c.aa.scale) {
							var e = c.ea(c.da).css("transform") + "";
							e != p && (e = e.replace("translate", ""), e = e.replace("(", ""), e = e.replace(")", ""), e = e.replace("px", ""), e = e.split(","), c.mc = parseFloat(e[0]), c.cd = parseFloat(e[1]), isNaN(c.mc) && (c.mc = 0, c.cd = 0));
							c.nb && 1.9 < c.aa.scale && (jQuery(".flexpaper_flipview_canvas_highres").show(), jQuery(".flexpaper_flipview_canvas").hide());
							c.aa.renderer.pb && (c.aa.renderer.ib && 1.9 < c.aa.scale) && (e = 1 < c.ja ? c.ja - 1 : c.ja, 1 < c.ja && c.aa.renderer.dc(c.pages[e - 1]), c.aa.renderer.dc(c.pages[e]));
						} else {
							c.mc = 0, c.cd = 0;
						}
						c.Sd = v;
						c.nb = p;
						c.Kb = p;
					});
					jQuery(c.da + "_parent").on("gesturechange", function(d) {
						d.preventDefault();
						c.aa.ha.se || (c.ma == p && (c.lb = d.originalEvent.scale), c.aa.Ha.turn("setCornerDragging", v), c.ma = c.aa.scale + (c.lb > c.aa.scale ? (d.originalEvent.scale - c.lb) / 2 : 4 * (d.originalEvent.scale - c.lb)), 1 > c.ma && (c.ma = 1), 2.5 < c.ma && !eb.platform.Jc && (c.ma = 2.5), c.aa.renderer.ib && (4 < c.ma && eb.platform.De) && (c.ma = 4), !c.aa.renderer.ib && (2.5 < c.ma && eb.platform.De) && (c.ma = 2.5), d = 1 == c.aa.sa && !c.aa.ha.hb ? -(c.Fc() / 4) : 0, c.ea(c.da + "_parent").transition({
							x: d,
							y: 0,
							scale: c.ma
						}, 0, "ease", x()));
					});
					jQuery(c.da + "_parent").on("gestureend", function(d) {
						d.preventDefault();
						if (!c.aa.ha.se) {
							c.aa.scale = c.ma;
							for (d = 0; d < c.document.numPages; d++) {
								c.Wa(d) && (c.pages[d].scale = c.aa.scale, c.pages[d].Qa());
							}
							setTimeout(function() {
								1 == c.aa.scale && (c.ea(c.da).transition({
									x: 0,
									y: 0
								}, 0), c.aa.Ha.turn("setCornerDragging", n));
								for (var d = 0; d < c.document.numPages; d++) {
									c.Wa(d) && (c.pages[d].za = v);
								}
								c.pc();
								jQuery(c).trigger("onScaleChanged");
								c.ma = p;
							}, 300);
						}
					});
					jQuery(c.da + "_parent").on("mousewheel", function(d) {
						if (!c.aa.ha.se) {
							c.Bc || (c.Bc = 0);
							0 < d.deltaY ? c.aa.scale + c.Bc + 2 * c.aa.document.ZoomInterval < c.aa.document.MaxZoomSize && (c.Bc += 2 * c.aa.document.ZoomInterval) : c.Bc = 1.2 < c.aa.scale + c.Bc - 3 * c.aa.document.ZoomInterval ? c.Bc - 3 * c.aa.document.ZoomInterval : -(c.aa.scale - 1);
							c.xd != p && (window.clearTimeout(c.xd), c.xd = p);
							1.1 <= c.aa.scale + c.Bc ? (c.aa.fisheye && c.aa.fisheye.animate({
								opacity: 0
							}, 0, function() {
								c.aa.fisheye.hide();
							}), c.ea(c.da + "_panelLeft").fadeTo("fast", 0), c.ea(c.da + "_panelRight").fadeTo("fast", 0), c.aa.Ha.turn("setCornerDragging", v)) : (c.ea(c.da).transition({
								x: 0,
								y: 0
							}, 0), c.aa.fisheye && (c.aa.fisheye.show(), c.aa.fisheye.animate({
								opacity: 1
							}, 100)), c.nb = p, c.Kb = p, c.mc = 0, c.cd = 0);
							c.Zd = c.aa.scale + c.Bc;
							if (!(eb.browser.mozilla && 30 > eb.browser.version) && 0 < jQuery(c.da).find(d.target).length) {
								if (1 == c.Zd) {
									c.ea(c.da + "_parent").transition({
										transformOrigin: "0px 0px"
									}, 0);
								} else {
									if (1 == c.aa.scale && c.ea(c.da + "_parent").transition({
										transformOrigin: "0px 0px"
									}, 0), c.aa.Ha.turn("setCornerDragging", v), 0 < jQuery(c.da).has(d.target).length) {
										d = jQuery(c.da + "_parent").xe(d.pageX, d.pageY);
										var e = c.ea(c.da + "_parent").css("transformOrigin").split(" ");
										2 <= e.length ? (e[0] = e[0].replace("px", ""), e[1] = e[1].replace("px", ""), c.zc = parseFloat(e[0]), c.Ac = parseFloat(e[1]), 0 == c.zc && (c.zc = d.x), 0 == c.Ac && (c.Ac = d.y)) : (c.zc = d.x, c.Ac = d.y);
										c.ea(c.da + "_parent").transition({
											transformOrigin: c.zc + "px " + c.Ac + "px"
										}, 0);
									}
								}
							}
							c.ea(c.da + "_parent").transition({
								scale: c.Zd
							}, 0, "ease", function() {
								c.aa.Ha.turn("setCornerDragging", v);
								jQuery(".flexpaper_flipview_canvas").show();
								jQuery(".flexpaper_flipview_canvas_highres").hide();
								c.xd = setTimeout(function() {
									c.aa.scale = c.Zd;
									for (var d = c.Bc = 0; d < c.document.numPages; d++) {
										c.Wa(d) && (c.pages[d].scale = c.aa.scale, c.pages[d].Qa());
									}
									1 == c.aa.scale && (c.ea(c.da).transition({
										x: 0,
										y: 0
									}, 0), c.aa.Ha.turn("setCornerDragging", n));
									for (d = 0; d < c.document.numPages; d++) {
										c.Wa(d) && (c.pages[d].za = v);
									}
									c.pc();
									c.Zd = p;
									jQuery(c).trigger("onScaleChanged");
									jQuery(c.aa.ca).trigger("onScaleChanged", c.aa.scale / c.aa.document.MaxZoomSize);
								}, 1000);
							});
						}
					});
					jQuery(c.da + "_arrowleft, " + c.da + "_panelLeft").on(!eb.platform.touchonlydevice || eb.platform.bd ? "mousedown" : "touchstart", function(d) {
						if (c.aa.ug) {
							return jQuery(d.target).hasClass("flexpaper_arrow_start") ? c.gotoPage(1) : c.previous(), v;
						}
					});
					jQuery(c.da + "_arrowright, " + c.da + "_panelRight").on(!eb.platform.touchonlydevice || eb.platform.bd ? "mousedown" : "touchstart", function(d) {
						jQuery(d.target).hasClass("flexpaper_arrow_end") ? c.gotoPage(c.aa.getTotalPages()) : c.next();
						return v;
					});
					jQuery(d).css("overflow-y", "hidden");
					jQuery(d).css("overflow-x", "hidden");
					jQuery(d).css("-webkit-overflow-scrolling", "hidden");
				}
			},
			Gf: function(c, d) {
				d.append("<div id='" + c.container + "_play' class='abc' style='position:absolute;left:" + (d.width() / 2 - 20) + "px;top:" + (c.ke / 2 - 25) + "px;width:" + c.Uc + "px;height:" + c.ke + "px;z-index:100;'></div>");
				jQuery("#" + c.container + "_play").vf(50, n);
				jQuery("#" + c.container + "_play").on("mousedown", function() {
					c.aa.openFullScreen();
				});
			},
			Nk: function(c, d) {
				d.find("#" + c.container + "_play").remove();
			},
			previous: function(c) {
				if (c.aa.ba == L) {
					var d = c.ja - 1;
					c.aa.renderer.Xc && c.ud(d);
					1 != c.aa.scale ? c.Qa(1, n, function() {
						jQuery(c.aa.ca).trigger("onScaleChanged", 1 / c.aa.document.MaxZoomSize);
						c.aa.turn("previous");
					}) : c.aa.turn("previous");
				}
			},
			next: function(c) {
				if (c.aa.ba == L) {
					var d = c.ja + 1;
					c.aa.renderer.Xc && c.ud(d);
					1 != c.aa.scale ? c.Qa(1, n, function() {
						jQuery(c.aa.ca).trigger("onScaleChanged", 1 / c.aa.document.MaxZoomSize);
						c.aa.turn("next");
					}) : c.aa.turn("next");
				}
			},
			Xe: function(c, d, e) {
				var f = c.ga.width(),
					g = c.ga.height(),
					l = c.Zd == p ? c.aa.scale : c.Zd;
				c.aa.ba == L && 1 < l && !eb.browser.safari ? c.ea(c.da).transition({
					x: -c.ek(d, c.aa.scale),
					y: -c.fk(e)
				}, 0) : c.aa.ba == L && (1 < l && eb.browser.safari) && jQuery(".flexpaper_viewer").scrollTo({
					top: 100 * (0.9 * e / g) + "%",
					left: 100 * (d / f) + "%"
				}, 0, {
					axis: "xy"
				});
			},
			lg: function(c) {
				c = c.ea(c.da + "_parent").css("transformOrigin") + "";
				return c != p ? (c = c.replace("translate", ""), c = c.replace("(", ""), c = c.replace(")", ""), c = c.split(" "), 1 < c.length ? {
					left: parseFloat(c[0].replace("px", "")),
					top: parseFloat(c[1].replace("px", ""))
				} : p) : p;
			},
			Gd: function(c) {
				!eb.platform.touchdevice && c.aa.ba == L && 1 < c.aa.scale ? jQuery(".flexpaper_viewer").scrollTo({
					left: "50%"
				}, 0, {
					axis: "x"
				}) : !eb.platform.touchdevice && (c.aa.ba == L && 1 == c.aa.scale && !c.Ab()) && jQuery(".flexpaper_viewer").scrollTo({
					left: "0%",
					top: "0%"
				}, 0, {
					axis: "xy"
				});
			}
		};
		return g;
	}(),
	va = function() {
		function g() {}
		g.prototype = {
			Zb: function(c, d) {
				return d.pages.ja == d.pageNumber || d.ja == d.pageNumber + 1;
			},
			ck: function(c, d, e) {
				var f = d.dimensions.mb != p ? d.dimensions.mb : d.dimensions.Aa;
				return !d.pages.Ab() && c.pb && (!eb.browser.safari || eb.platform.touchdevice || eb.browser.safari && 7.1 > eb.browser.vb) ? e : d.dimensions.mb != p && c.pb && d.aa.renderer.Fa ? d.pages.Uc / 2 / f : d.yb && !d.aa.renderer.Fa ? d.pages.Uc / 2 / d.aa.renderer.Ga[d.pageNumber].mb : e;
			},
			wj: function(c, d, e) {
				jQuery(d.la + "_textoverlay").append(e);
			},
			jh: function(c, d, e, f) {
				var g = c.Kk == f && !d.aa.renderer.pb;
				e && !(e && e.attr("id") == c.Jk) && (c.Kk = f, c.Jk = e.attr("id"), c.Lk != e.css("top") || c.Mk != d.pageNumber ? (c.qc != p && c.qc.remove(), c.Lk = e.css("top"), c.qc = e.wrap(jQuery(String.format("<div class='flexpaper_pageword flexpaper_pageword_" + c.ia + "' style='{0};border-width: 3px;border-style:dotted;border-color: #ee0000;'></div>", e.attr("style")))).parent(), c.qc.css({
					"margin-left": "-3px",
					"margin-top": "-4px",
					"z-index": "11"
				}), jQuery(d.va).append(c.qc)) : g ? (c.qc.css("width", c.qc.width() + e.width()), jQuery(c.qc.children()[0]).width(c.qc.width())) : (c.qc.css("left", e.css("left")), c.qc.append(e)), e.css({
					left: "0px",
					top: "0px"
				}), e.addClass("flexpaper_selected"), e.addClass("flexpaper_selected_default"), e.addClass("flexpaper_selected_searchmatch"), c.Mk = d.pageNumber);
			}
		};
		return g;
	}(),
	sa = function() {
		function g() {}
		g.prototype = {
			create: function(c, d) {
				if (c.aa.ba == L && (c.Ij = 10 < c.pages.hd ? c.pages.hd : 10, !c.og && (!c.aa.renderer.Xc || c.Va || !(c.pageNumber > c.Ij + 6)))) {
					c.bc = jQuery("<div class='flexpaper_page flexpaper_page_zoomIn' id='" + c.nc + "' style='" + c.getDimensions() + ";z-index:2;background-size:cover;background-color:#ffffff;margin-bottom:0px;'><div id='" + c.na + "' style='height:100%;width:100%;'></div></div>");
					c.pages.aa.Ha && c.aa.renderer.Xc ? c.pages.aa.Ha.turn("addPage", c.bc, c.pageNumber + 1) : jQuery(d).append(c.bc);
					var e = c.Bh() * c.$a,
						f = c.Sa() / e;
					c.dimensions.mb != p && (c.pb && c.aa.renderer.Fa) && (f = c.pages.Uc / 2 / e);
					c.Uh = f;
					c.Ce(f);
					c.og = n;
					c.Va = n;
					c.aa.renderer.Ic(c);
					c.show();
					c.height = c.ea(c.va).height();
					c.li();
				}
			},
			$c: function(c) {
				return c.pages.$c() / (!c.aa.ha.hb ? 2 : 1);
			},
			Nd: function(c) {
				return c.pages.Nd();
			},
			getDimensions: function(c) {
				if (c.aa.ba == L) {
					return c.ga.width(), "position:absolute;left:0px;top:0px;width:" + c.Sa(c) + ";height:" + c.Xa(c);
				}
			},
			Sa: function(c) {
				if (c.aa.ba == L) {
					return c.pages.Uc / (!c.aa.ha.hb ? 2 : 1) * c.scale;
				}
			},
			kg: function(c) {
				if (c.aa.ba == L) {
					return 1 * (c.pages.Uc / (!c.aa.ha.hb ? 2 : 1));
				}
			},
			Xa: function(c) {
				if (c.aa.ba == L) {
					return c.pages.ke * c.scale;
				}
			},
			jg: function(c) {
				if (c.aa.ba == L) {
					return 1 * c.pages.ke;
				}
			},
			Gc: ca(0),
			Zb: function(c) {
				if (c.aa.ba == L) {
					return c.pages.ja >= c.pageNumber - 2 && c.pages.ja <= c.pageNumber + 4;
				}
			},
			unload: function(c) {
				var d = c.la;
				0 == jQuery(d).length && (d = jQuery(c.bc).find(c.la));
				if ((c.pageNumber < c.pages.ja - 15 || c.pageNumber > c.pages.ja + 15) && (c.bc && !c.bc.parent().hasClass("turn-page-wrapper") && !c.lc) && 0 != c.pageNumber) {
					jQuery(d).find("*").unbind(), jQuery(d).find("*").remove(), c.initialized = v, c.Lb = v;
				}
			}
		};
		pa.prototype.we = function() {
			return eb.platform.touchdevice ? this.aa.ba == L ? !this.aa.ha.hb && window.devicePixelRatio && 1 < window.devicePixelRatio ? 1.9 : 2.6 : 1 : this.aa.ba == L ? 2 : 1;
		};
		return g;
	}(),
	ya = "undefined" == typeof window;
ya && (window = []);
var FlexPaperViewer_HTML = window.FlexPaperViewer_HTML = function() {
	function g(c) {
		this.config = c;
		this.Oh = this.config.instanceid;
		this.document = this.config.document;
		this.ia = this.config.rootid;
		this.ga = {};
		this.jc = this.ka = p;
		this.selectors = {};
		this.ba = C;
		this.ub = c.document.InitViewMode != p && "undefined" != c.document.InitViewMode && "" != c.document.InitViewMode ? c.document.InitViewMode : C;
		this.initialized = v;
		this.gd = "flexpaper_selected_default";
		this.Gb = {};
		this.Ca = [];
		this.qj = "data:image/gif;base64,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%3D";
		this.bh = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA0AAAANCAYAAABy6+R8AAAAAXNSR0IArs4c6QAAAAZiS0dEAFEAUQBRjSJ44QAAAAlwSFlzAAALEwAACxMBAJqcGAAAAAd0SU1FB9wCCgEMO6ApCe8AAAFISURBVCjPfZJBi49hFMV/521MUYxEsSGWDDWkFKbkA/gAajaytPIFLKx8BVkodjP5AINGU0xZKAslC3Ys2NjP+VnM++rfPzmb23065z6de27aDsMwVD0C3AfOAYeB38BP9fEwDO/aMgwDAAFQDwKbwC9gZxScUM8Al5M8SPJ0Eu5JYV0FeAZcBFaAxSSPkjwHnrQ9Pf1E22XVsX5s+1m9o54cB9J2q+361KM+VN+ot9uqrjIH9VJbpz7qOvAeuAIcSnJzThA1SXaTBGAAvgCrwEvg0yxRXUhikrOjZ1RQz7uHFfUu/4C60fb16G9hetxq+1a9Pkdears2Dt1Rj87mdAx4BfwAttWvSQ4AV9W1aYlJtoFbmQJTjwP3gAvAIlDgG7CsXvu7uWQzs+cxmj0F7Fd3k3wfuRvqDWAfM+HxP6hL6oe2tn3xB7408HFbpc41AAAAAElFTkSuQmCC";
		this.ah = "data:image/png;base64,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%3D";
		this.Tl = this.ia + "_textoverlay";
		this.Xg = "#" + this.Tl;
		this.sa = 1;
		this.Um = p;
		this.renderer = this.config.renderer;
		this.Ja = "toolbar_" + this.ia;
		this.ca = "#" + this.Ja;
		this.vc = v;
		this.en = "highlight";
		this.scale = this.config.document.Scale;
		this.resources = new FlexPaper_Resources(this);
		this.Fd = v;
		this.me = 0;
		this.linkColor = "#72e6ff";
		this.of = 0.4;
		window.zine = n;
	}
	g.prototype = {
		ea: function(c) {
			if (0 < c.indexOf("undefined")) {
				return jQuery(p);
			}
			this.selectors || (this.selectors = {});
			this.selectors[c] || (this.selectors[c] = jQuery(c));
			return this.selectors[c];
		},
		ra: function() {
			return this.ha ? this.ha.ra : "";
		},
		loadFromUrl: function(c) {
			var d = this,
				e;
			eb.browser.jb.Fl && c.PDFFile ? e = new CanvasPageRenderer(this.ia, c.PDFFile, d.config.jsDirectory, {
				jsonfile: c.jsonfile,
				pageImagePattern: c.pageImagePattern,
				JSONDataType: d.renderer.config.JSONDataType,
				signature: d.renderer.config.signature
			}) : c.JSONFile && c.IMGFiles && (e = new ImagePageRenderer(this.ia, {
				jsonfile: c.JSONFile,
				pageImagePattern: c.IMGFiles,
				JSONDataType: d.renderer.config.JSONDataType,
				signature: d.renderer.config.signature
			}, d.config.jsDirectory));
			if (d.renderer = e) {
				d.initialize(), d.bindEvents(), e.initialize(function() {
					d.loadDoc(e, e.getNumPages());
				});
			}
		},
		loadDoc: function(c, d) {
			this.initialized = v;
			this.document.numPages = d;
			this.renderer = c;
			this.show();
			this.config.document.FitPageOnLoad || eb.platform.touchdevice ? this.fitheight() : this.config.document.FitWidthOnLoad ? this.fitwidth() : this.toolbar && (this.toolbar.wb && this.toolbar.wb.hk) && this.toolbar.wb.setValue(this.toolbar.wb.hk(), n);
		},
		getDimensions: function(c) {
			return this.renderer.getDimensions(c);
		},
		Pj: function(c) {
			if (jQuery(c.target).hasClass("flexpaper_note_container") && eb.platform.touchdevice) {
				return window.ec = v, n;
			}
			var d = !eb.platform.touchdevice || "undefined" === typeof c.originalEvent.touches ? c.pageX : c.originalEvent.touches[0].pageX,
				e = !eb.platform.touchdevice || "undefined" === typeof c.originalEvent.touches ? c.pageY : c.originalEvent.touches[0].pageY;
			if (this.vc || eb.platform.touchdevice) {
				c.target && (c.target.id && 0 <= c.target.id.indexOf("page") && 0 <= c.target.id.indexOf("word")) && (hoverPage = parseInt(c.target.id.substring(c.target.id.indexOf("_") + 1)), hoverPageObject = oa(this.ia));
				if ((hoverPageObject || window.ec) && window.ec) {
					eb.platform.touchdevice && (event.preventDefault(), event.stopPropagation(), this.pages.jScrollPane && this.pages.jScrollPane.data("jsp").disable());
				} else {
					return n;
				}
				window.b = hoverPageObject.match({
					left: d,
					top: e
				}, v);
				window.b != p && (window.a != p && window.a.pageNumber != window.b.pageNumber) && (window.a = hoverPageObject.match({
					left: d - 1,
					top: e - 1
				}, v));
				this.Hd(n);
				this.Wc = hoverPageObject.Jd(n, this.gd);
			} else {
				if (c.target && (c.target.id && 0 <= c.target.id.indexOf("page")) && (hoverPage = parseInt(c.target.id.substring(c.target.id.indexOf("_") + 1)), hoverPageObject = oa(this.ia)), hoverPageObject && hoverPageObject.match({
					left: d,
					top: e
				}, n), !hoverPageObject && !window.ec) {
					return n;
				}
			}
		},
		Hd: function(c) {
			eb.platform.touchdevice || (this.Wc = p);
			this.vc && (jQuery(".flexpaper_pageword_" + this.ia).removeClass("flexpaper_selected"), jQuery(".flexpaper_pageword_" + this.ia).removeClass("flexpaper_selected_default"));
			c && jQuery(".flexpaper_pageword_" + this.ia).each(function() {
				jQuery(this).hasClass("flexpaper_selected_yellow") && !jQuery(this).data("isMark") && jQuery(this).removeClass("flexpaper_selected_yellow");
				jQuery(this).hasClass("flexpaper_selected_orange") && !jQuery(this).data("isMark") && jQuery(this).removeClass("flexpaper_selected_orange");
				jQuery(this).hasClass("flexpaper_selected_green") && !jQuery(this).data("isMark") && jQuery(this).removeClass("flexpaper_selected_green");
				jQuery(this).hasClass("flexpaper_selected_blue") && !jQuery(this).data("isMark") && jQuery(this).removeClass("flexpaper_selected_blue");
				jQuery(this).hasClass("flexpaper_selected_strikeout") && !jQuery(this).data("isMark") && jQuery(this).removeClass("flexpaper_selected_strikeout");
			});
		},
		Qj: function(c) {
			this.Vh = "up";
			this.Yb = this.pg = v;
			this.Th = p;
			if (!this.pages || !this.pages.animating) {
				if (jQuery(c.target).hasClass("flexpaper_searchabstract_result") || jQuery(c.target).parent().hasClass("flexpaper_searchabstract_result") || jQuery(c.target).hasClass("flexpaper_note_container")) {
					return n;
				}
				if (this.vc || eb.platform.touchdevice) {
					if (hoverPageObject) {
						if (eb.platform.touchdevice) {
							var d = p;
							"undefined" != typeof c.originalEvent.touches && (d = c.originalEvent.touches[0] || c.originalEvent.changedTouches[0]);
							d != p && (this.$b == d.pageX && this.ac == d.pageY) && (this.Hd(), this.Wc = hoverPageObject.Jd(window.ec, this.gd));
							d != p && (this.$b = d.pageX, this.ac = d.pageY);
							this.pages.jScrollPane && this.pages.jScrollPane.data("jsp").enable();
						} else {
							window.b = hoverPageObject.match({
								left: c.pageX,
								top: c.pageY
							}, v);
						}
						this.Wc != p && this.ga.trigger("onSelectionCreated", this.Wc.text);
						window.ec = v;
						window.a = p;
						window.b = p;
					}
				} else {
					hoverPageObject && (window.b = hoverPageObject.match({
						left: c.pageX,
						top: c.pageY
					}, v), window.ec = v, this.Hd(), this.Wc = hoverPageObject.Jd(v, this.gd));
				}
			}
		},
		Oj: function(c) {
			var d = this;
			d.Vh = "down";
			if (jQuery(c.target).hasClass("flexpaper_note_textarea")) {
				window.b = p, window.a = p;
			} else {
				if (!d.pages.animating) {
					var e = !eb.platform.touchdevice || "undefined" === typeof c.originalEvent.touches ? c.pageX : c.originalEvent.touches[0].pageX,
						f = !eb.platform.touchdevice || "undefined" === typeof c.originalEvent.touches ? c.pageY : c.originalEvent.touches[0].pageY;
					d.$b = e;
					d.ac = f;
					eb.platform.touchdevice && (window.clearTimeout(d.xk), d.Th = (new Date).getTime(), document.activeElement && jQuery(document.activeElement).hasClass("flexpaper_note_textarea") && document.activeElement.blur(), d.xk = setTimeout(function() {
						if (d.Th != p && c.originalEvent.touches && 0 < c.originalEvent.touches.length) {
							var e = !eb.platform.touchdevice || "undefined" === typeof c.originalEvent.touches ? c.pageX : c.originalEvent.touches[0].pageX,
								f = !eb.platform.touchdevice || "undefined" === typeof c.originalEvent.touches ? c.pageY : c.originalEvent.touches[0].pageY;
							d.$b + 20 > e && d.$b - 20 < e && (d.ac + 20 > f && d.ac - 20 < f) && (hoverPage = parseInt(c.target.id.substring(c.target.id.indexOf("_") + 1)), hoverPageObject = oa(d.ia), hoverPageObject != p && (d.pages.jScrollPane != p && d.pages.jScrollPane.data("jsp").disable(), window.ec = n, d.Hd(n), window.b = hoverPageObject.match({
								left: e,
								top: f
							}, v), window.a = hoverPageObject.match({
								left: e - 1,
								top: f - 1
							}, v), d.Wc = hoverPageObject.Jd(n, d.gd)));
						}
					}, 800));
					if (d.vc || eb.platform.touchdevice) {
						if (!hoverPageObject) {
							if (eb.platform.touchdevice) {
								if (c.target && (c.target.id && 0 <= c.target.id.indexOf("page") && 0 <= c.target.id.indexOf("word")) && (hoverPage = parseInt(c.target.id.substring(c.target.id.indexOf("_") + 1)), hoverPageObject = oa(d.ia)), !hoverPageObject) {
									window.a = p;
									return;
								}
							} else {
								window.a = p;
								return;
							}
						}
						window.a = hoverPageObject.match({
							left: e,
							top: f
						}, n);
						if (window.a) {
							return window.ec = n, d.Hd(), d.Wc = hoverPageObject.Jd(v, d.gd), v;
						}!jQuery(c.target).hasClass("flexpaper_tbtextbutton") && (!jQuery(c.target).hasClass("flexpaper_colorselector") && !jQuery(c.target).hasClass("flexpaper_tbbutton")) && !eb.platform.touchdevice && (d.Hd(), d.Wc = hoverPageObject.Jd(v, d.gd));
						window.ec = v;
						return n;
					}
					window.a = hoverPageObject ? hoverPageObject.match({
						left: e,
						top: f
					}, n) : p;
				}
			}
		},
		$c: function() {
			this.width || (this.width = this.ka.width());
			return this.width;
		},
		Mi: function() {
			return this.pages != p ? this.ba != this.ra() ? this.pages.ja + 1 : this.pages.ja : 1;
		},
		bindEvents: function() {
			var c = this;
			hoverPage = 0;
			hoverPageObject = p;
			c.ka.bind("mousemove", function(d) {
				return c.Pj(d);
			});
			c.ka.bind("mousedown", function(d) {
				return c.Oj(d);
			});
			c.ka.bind("mouseup", function(d) {
				return c.Qj(d);
			});
			var d = jQuery._data(jQuery(window)[0], "events");
			jQuery(window).bind("resize", function(d) {
				c.Ul(d);
			});
			c.di = d.resize[d.resize.length - 1];
			try {
				jQuery.get(c.config.localeDirectory + c.document.localeChain + "/FlexPaper.txt", function(d) {
					c.toolbar.Sh(d);
					c.mi();
				}).error(function() {
					c.mi();
					A("Failed loading supplied locale (" + c.document.localeChain + ")");
				}), c.toolbar.Sh("");
			} catch (e) {}
			c.qg || (c.qg = "");
		},
		Ul: function(c) {
			if (!this.document.DisableOverflow && !jQuery(c.target).hasClass("flexpaper_note")) {
				c = this.ka.width();
				var d = this.ka.height(),
					e = v,
					f = -1;
				this.Hg ? f = this.Hg : 0 < this.ka[0].style.width.indexOf("%") && (this.Hg = f = parseFloat(this.ka[0].style.width.substr(0, this.ka[0].style.width.length - 1) / 100));
				0 < f && (c = 0 == this.ka.parent().width() ? jQuery(document).width() * f : this.ka.parent().width() * f, e = n);
				f = -1;
				this.Gg ? f = this.Gg : 0 < this.ka[0].style.height.indexOf("%") && (this.Gg = f = parseFloat(this.ka[0].style.height.substr(0, this.ka[0].style.height.length - 1) / 100));
				0 < f && (d = 0 == this.ka.parent().height() ? jQuery(window).height() * f : this.ka.parent().height() * f, e = n);
				f = document.bk || document.mozFullScreen || document.webkitIsFullScreen || window.Ui || window.$g;
				e && !f && this.resize(c, d);
			}
		},
		mi: function() {
			this.document.DisableOverflow || (this.Wd || (this.Wd = this.toolbar != p && this.toolbar.Ya != p ? this.toolbar.ya(this.toolbar.Ya, "LoadingPublication") : "Loading Publication"), this.Wd == p && (this.Wd = "Loading Publication"), (this.Ql = v) ? (this.Gb = jQuery(String.format("<div style='position:{1};margin: 0px auto;z-index:100;top:40%;left:{2};color:#ffffff;background-color:{3};padding: 15px 15px 15px 15px;border-radius: 10px 10px 10px 10px;'><div style='float:left'><img src='{5}' /></div><div style='float:left;padding-left:10px;width:170px;text-align:center;' class='flexpaper_initloader_title'>{4}<div id='flexpaper_initloader_{0}' class='flexpaper_initloader' style='padding-top:70px;position:relative;'></div>", this.ia, "static" == this.ka.css("position") ? "relative" : "absolute", "40%", this.ha.fd, this.ha.Sm, this.renderer.xa(1, 200)) + "</div></div>"), this.ka.append(this.Gb), this.bb = new CanvasLoader("flexpaper_initloader_" + this.ia), this.bb.setColor("#777777"), this.bb.setShape("square"), this.bb.setDiameter(40), this.bb.setDensity(151), this.bb.setRange(0.8), this.bb.setSpeed(2), this.bb.setFPS(42), this.bb.show()) : window.zine && !(eb.browser.msie && 10 > eb.browser.version) ? (this.Gb = jQuery(String.format("<div id='flexpaper_initloader_{0}' class='flexpaper_initloader' style='position:{1};margin: 0px auto;z-index:100;top:40%;left:{2}'></div>", this.ia, "static" == this.ka.css("position") ? "relative" : "absolute", eb.platform.Vd ? "40%" : "50%")), this.ka.append(this.Gb), this.bb = new CanvasLoader("flexpaper_initloader_" + this.ia), this.bb.setColor("#555555"), this.bb.setShape("square"), this.bb.setDiameter(70), this.bb.setDensity(151), this.bb.setRange(0.8), this.bb.setSpeed(2), this.bb.setFPS(42), this.bb.show()) : (this.Gb = jQuery(String.format("<div class='flexpaper_initloader' style='position:{2};z-index:100;'><div class='flexpaper_initloader_panel' style='{1};background-color:#ffffff;'><img src='{0}' style='vertical-align:middle;margin-top:7px;margin-left:5px;'><div style='float:right;margin-right:25px;margin-top:19px;' class='flexpaper_notifylabel'>" + this.Wd + "<br/><div style='margin-left:30px;' class='flexpaper_notifystatus'>" + this.qg + "</div></div></div></div>", this.qj, "margin: 0px auto;", "static" == this.ka.css("position") ? "relative" : "absolute")), this.ka.append(this.Gb)));
		},
		initialize: function() {
			var c = this;
			FLEXPAPER.Ih.init();
			c.Yk();
			c.ga = jQuery("#" + c.ia);
			c.toolbar = new qa(this, this.document);
			eb.platform.Vd && !c.config.document.InitViewMode && (c.ub = C);
			"BookView" == c.config.document.InitViewMode && 0 == c.document.StartAtPage % 2 && (c.document.StartAtPage += 1);
			c.config.document.TouchInitViewMode && (c.config.document.TouchInitViewMode != c.ub && eb.platform.touchonlydevice) && (c.ub = c.config.document.TouchInitViewMode);
			!c.config.document.TouchInitViewMode && eb.platform.touchonlydevice && (c.ub = U);
			window.zine ? (c.ha = c.toolbar.ag = new FlexPaperViewer_Zine(c.toolbar, this, c.ga), c.ha.initialize(), c.ba != c.ra() && (c.ba = c.ub)) : c.ba = c.ub;
			"CADView" == c.ba && (c.ba = "SinglePage");
			if (window.zine && (eb.browser.msie && 9 > eb.browser.version || eb.browser.safari && 5 > eb.browser.vb) && !eb.platform.touchonlydevice) {
				c.document.MinZoomSize = c.MinZoomSize = 0.3, c.ba = "BookView";
			}
			"0px" == c.ga.css("width") && c.ga.css("width", "1024px");
			"0px" == c.ga.css("height") && c.ga.css("height", "600px");
			c.Fd = c.ba == c.ra() && (eb.platform.Vd || eb.platform.hh);
			c.ka === p && !c.ha && (0 < c.ga[0].style.width.indexOf("%") && (c.Hg = parseFloat(c.ga[0].style.width.substr(0, c.ga[0].style.width.length - 1) / 100)), 0 < c.ga[0].style.height.indexOf("%") && (c.Gg = parseFloat(c.ga[0].style.height.substr(0, c.ga[0].style.height.length - 1) / 100)), c.document.DisableOverflow ? (c.config.document.FitPageOnLoad = v, c.config.document.FitWidthOnLoad = n, c.ka = jQuery("<div style='width:210mm;height:297mm;' class='flexpaper_viewer_container'/>")) : (c.ka = jQuery("<div style='" + c.ga.attr("style") + ";' class='flexpaper_viewer_wrap flexpaper_viewer_container'/>"), ("" == c.ka.css("position") || "static" == c.ka.css("position")) && c.ka.css({
				position: "relative"
			})), c.ka = c.ga.wrap(c.ka).parent(), c.document.DisableOverflow ? c.ga.css({
				left: "0px",
				top: "0px",
				position: "relative",
				width: "210mm",
				height: "297mm"
			}).addClass("flexpaper_viewer") : c.ga.css({
				left: "0px",
				top: "0px",
				position: "relative",
				width: "100%",
				height: "100%"
			}).addClass("flexpaper_viewer").addClass("flexpaper_viewer_gradient"), window.annotations && c.config.document.AnnotationToolsVisible && !c.document.DisableOverflow ? (c.me = eb.platform.touchdevice ? 15 : 22, c.ga.height(c.ga.height() - c.me)) : c.me = 0);
			eb.browser.msie && jQuery(".flexpaper_initloader_panel").css("left", c.ga.width() - 500);
			c.document.DisableOverflow || (c.config.Toolbar == p && 0 == jQuery("#" + c.Ja).length ? (c.Toolbar = c.ka.prepend("<div id='" + c.Ja + "' class='flexpaper_toolbarstd' style='z-index:200;overflow-y:hidden;overflow-x:hidden;'></div>").parent(), c.toolbar.create(c.Ja)) : c.config.Toolbar != p && !(c.Toolbar instanceof jQuery) && (c.config.Toolbar = unescape(c.config.Toolbar), c.Toolbar = jQuery(c.config.Toolbar), c.Toolbar.attr("id", c.Ja), c.ka.prepend(c.Toolbar)));
			c.vj();
			c.document.DisableOverflow || c.resources.initialize();
			hoverPage = 0;
			hoverPageObject = p;
			c.ha != p ? c.ha.Hj(c.Ja) : window.annotations && (c.Ob = new FlexPaperViewerAnnotations_Plugin(this, this.document, c.Ja + "_annotations"), c.Ob.create(c.Ja + "_annotations"), c.Ob.bindEvents(c.aa));
			c.document.DisableOverflow || (eb.platform.touchdevice || c.ka.append("<textarea id='selector' class='flexpaper_selector' rows='0' cols='0'></textarea>"), 0 == jQuery("#printFrame_" + c.ia).length && c.ka.append("<iframe id='printFrame_" + c.ia + "' name='printFrame_" + c.ia + "' class='flexpaper_printFrame'>"));
			jQuery(c.renderer).bind("loadingProgress", function(d, e) {
				c.$k(d, e);
			});
			jQuery(c.renderer).bind("loadingProgressStatusChanged", function(d, e) {
				c.al(d, e);
			});
			jQuery(c.renderer).bind("UIBlockingRenderingOperation", function(d, e) {
				c.rc(d, e);
			});
			jQuery(c.renderer).bind("UIBlockingRenderingOperationCompleted", function() {
				c.Jb();
			});
			$FlexPaper(c.ia).dispose = c.Bb;
			$FlexPaper(c.ia).getCurrentRenderingMode = c.getCurrentRenderingMode;
		},
		vj: function() {
			this.document.DisableOverflow || (eb.platform.touchonlydevice && !this.Fd ? eb.platform.touchonlydevice ? (window.zine ? this.ga.height(this.ga.height() - (!this.config.BottomToolbar ? 35 : 65)) : this.ga.height(this.ga.height() - (!this.config.BottomToolbar ? 25 : 65)), this.config.BottomToolbar && this.ka.height(this.ka.height() - (eb.platform.Nb ? 7 : 18))) : this.ga.height(this.ga.height() - 25) : window.zine || (this.config.BottomToolbar ? this.ga.height(this.ga.height() - jQuery(this.ca).height() + 11) : this.ga.height(this.ga.height() - 13)));
		},
		$k: function(c, d) {
			this.qg = d.progress;
			this.Gb.find(".flexpaper_notifystatus").html(d.progress);
		},
		al: function(c, d) {
			this.Wd = d.label;
			this.Gb.find(".flexpaper_notifylabel").html(d.label);
		},
		rc: function(c, d) {
			var e = this;
			!e.document.DisableOverflow && e.jc === p && (e.jc = jQuery("<div style='position:absolute;left:50%;top:50%;'></div>"), e.ka.append(e.jc), e.jc.spin({
				color: "#777"
			}), e.kf != p && (window.clearTimeout(e.kf), e.kf = p), d.Gk || (e.kf = setTimeout(function() {
				e.jc && (e.jc.remove(), e.jc = p);
			}, 1000)));
		},
		Jb: function() {
			this.jc && (this.jc.remove(), this.jc = p);
		},
		show: function() {
			var c = this;
			jQuery(c.resources).bind("onPostinitialized", function() {
				setTimeout(function() {
					c.xf();
					c.document.DisableOverflow || c.toolbar.bindEvents(c.ga);
					c.ha != p && c.ha.bindEvents(c.ga);
					c.gh(c.document.StartAtPage);
					jQuery(c.ga).trigger("onDocumentLoaded", c.renderer.getNumPages());
				}, 50);
				jQuery(c.resources).unbind("onPostinitialized");
			});
			c.resources.Hk();
		},
		Bb: function() {
			this.Jj = n;
			this.ga.unbind();
			this.ga.find("*").unbind();
			this.ka.find("*").unbind();
			this.ka.find("*").remove();
			this.ga.empty();
			this.ka.empty();
			jQuery(this).unbind();
			window.PDFJS && delete window.PDFJS;
			this.Ob && (jQuery(this.Ob).unbind(), this.Ob.Bb(), delete this.Ob, this.Ob = p);
			jQuery(this.renderer).unbind();
			this.renderer.Bb();
			delete this.renderer;
			delete this.config;
			jQuery(this.pages).unbind();
			this.pages.Bb();
			delete this.pages;
			delete window["wordPageList_" + this.ia];
			window["wordPageList_" + this.ia] = p;
			this.ka.unbind("mousemove");
			this.ka.unbind("mousedown");
			this.ka.unbind("mouseup");
			jQuery(window).unbind("resize", this.di);
			delete this.di;
			jQuery(this.renderer).unbind("loadingProgress");
			jQuery(this.renderer).unbind("loadingProgressStatusChanged");
			jQuery(this.renderer).unbind("UIBlockingRenderingOperation");
			jQuery(this.renderer).unbind("UIBlockingRenderingOperationCompleted");
			this.ha ? this.ha.Bb() : this.ga.parent().remove();
			this.ka.remove();
			delete this.ka;
			delete this.ga;
			CanvasPageRenderer && delete CanvasPageRenderer;
			ImagePageRenderer && delete ImagePageRenderer;
			la && delete la;
			FlexPaperViewerAnnotations_Plugin && delete FlexPaperViewerAnnotations_Plugin;
		},
		tf: function() {
			var c = this;
			eb.platform.touchonlydevice ? (c.initialized = n, (!c.ha && c.config.document.FitWidthOnLoad && c.ba != H && c.ba != D || c.ba == C || c.ba == U) && c.fitwidth(), (c.config.document.FitPageOnLoad || c.ba == H || c.ba == D || c.ha) && c.fitheight(), c.pages.Je(), c.pages.dd()) : (c.initialized = n, c.tm || c.toolbar.tj(c.config.document.MinZoomSize, c.config.document.MaxZoomSize), c.config.document.FitPageOnLoad || c.ba == H || c.ba == D ? c.fitheight() : c.config.document.FitWidthOnLoad && c.ba != H && c.ba != D ? c.fitwidth() : c.Zoom(c.config.document.Scale));
			(!c.document.StartAtPage || 1 == c.document.StartAtPage) && c.ba != c.ra() && c.ga.trigger("onCurrentPageChanged", c.pages.ja + 1);
			c.document.StartAtPage && 1 != c.document.StartAtPage && c.pages.scrollTo(c.document.StartAtPage);
			c.ha && c.ha.tf();
			c.Gb && c.Gb.fadeOut ? c.Gb.fadeOut(300, function() {
				c.Gb.remove();
				c.bb && (c.bb.kill(), delete c.bb);
				delete c.Gb;
				c.bb = p;
				jQuery(c.pages.da).fadeIn(300, x());
				c.PreviewMode && c.ha.cb.Gf(c.pages, c.ga);
			}) : (jQuery(c.pages.da).fadeIn(300, x()), c.PreviewMode && c.ha.cb.Gf(c.pages, c.ga));
			c.ga.trigger("onInitializationComplete");
		},
		xf: function() {
			this.renderer.cg = v;
			if (this.pages) {
				for (var c = 0; c < this.document.numPages; c++) {
					this.pages.pages[c] && window.clearTimeout(this.pages.pages[c].Pb);
				}
			}
			this.sa = 1;
			this.ga.find("*").unbind();
			this.ga.find("*").remove();
			this.ga.empty();
			this.renderer.Rd = v;
			jQuery(this.Xg).remove();
			this.ha && this.ha.xf();
		},
		gh: function(c) {
			this.pages = new Z(this.ga, this, this.ia, c);
			this.pages.create(this.ga);
		},
		previous: function() {
			var c = this;
			c.Bg || (c.Bg = setTimeout(function() {
				window.clearTimeout(c.Bg);
				c.Bg = p;
			}, 700), c.pages.previous());
		},
		setCurrentCursor: function(c) {
			"ArrowCursor" == c && (this.vc = v, addCSSRule(".flexpaper_pageword", "cursor", "default"), window.annotations || jQuery(".flexpaper_pageword_" + this.ia).remove());
			"TextSelectorCursor" == c && (this.vc = n, this.gd = "flexpaper_selected_default", addCSSRule(".flexpaper_pageword", "cursor", "text"), window.annotations || (this.pages.getPage(this.pages.ja - 1), this.pages.getPage(this.pages.ja - 2), this.pages.Ka()));
			this.ha && this.ha.setCurrentCursor(c);
			this.pages.setCurrentCursor(c);
			jQuery(this.ca).trigger("onCursorChanged", c);
		},
		printPaper: function(c) {
			if (eb.platform.touchonlydevice) {
				c = "current";
			} else {
				if (!c) {
					jQuery("#modal-print").css("background-color", "#dedede");
					jQuery("#modal-print").smodal({
						minHeight: 255,
						appendTo: this.ka
					});
					jQuery("#modal-print").parent().css("background-color", "#dedede");
					return;
				}
			}
			"current" == c && 0 < jQuery(this.ca).find(".flexpaper_txtPageNumber").val().indexOf("-") && (c = jQuery(this.ca).find(".flexpaper_txtPageNumber").val());
			var d = p;
			if ("ImagePageRenderer" == this.renderer.sd() || this.document.MixedMode || this.renderer.config.pageImagePattern && this.renderer.config.jsonfile) {
				d = "{key : '" + this.config.key + "',jsonfile : '" + this.renderer.config.jsonfile + "',compressedJsonFormat : " + (this.renderer.Ma ? this.renderer.Ma : v) + ",pageImagePattern : '" + this.renderer.config.pageImagePattern + "',JSONDataType : '" + this.renderer.config.JSONDataType + "',signature : '" + this.renderer.config.signature + "',UserCollaboration : " + this.config.UserCollaboration + "}";
			}
			"CanvasPageRenderer" == this.renderer.sd() && (d = "{key : '" + this.config.key + "',jsonfile : '" + this.renderer.config.jsonfile + "',PdfFile : '" + this.renderer.file + "',compressedJsonFormat : " + (this.renderer.Ma ? this.renderer.Ma : v) + ",pageThumbImagePattern : '" + this.renderer.config.pageThumbImagePattern + "',pageImagePattern : '" + this.renderer.config.pageImagePattern + "',JSONDataType : '" + this.renderer.config.JSONDataType + "',signature : '" + this.renderer.config.signature + "',UserCollaboration : " + this.config.UserCollaboration + "}");
			if (0 < jQuery("#printFrame_" + this.ia).length) {
				var e = window.printFrame = eb.browser.msie && 9 > eb.browser.version ? window.open().document : jQuery("#printFrame_" + this.ia)[0].contentWindow.document || jQuery("#printFrame_" + this.ia)[0].contentDocument;
				e.open();
				e.write("<html>");
				e.write("<head>");
				e.write("<script type='text/javascript' src='" + this.config.jsDirectory + "jquery.min.js'>\x3c/script>");
				e.write('<script type="text/javascript" src="' + this.config.jsDirectory + 'flexpaper.js">\x3c/script>');
				e.write('<script type="text/javascript" src="' + this.config.jsDirectory + 'flexpaper_handlers.js">\x3c/script>');
				e.write("<script type='text/javascript' src='" + this.config.jsDirectory + "FlexPaperViewer.js'>\x3c/script>");
				e.write("<style type='text/css' media='print'>html, body { height:100%; }body { margin:0; padding:0; } .flexpaper_ppage { display:block;max-width:210mm;max-height:297mm;margin-bottom:20px;margin-top:0px;} .ppage_break { page-break-after : right; } .ppage_none { page-break-after : avoid; } @page {}</style>");
				e.write("</head>");
				e.write("<body>");
				e.write('<script type="text/javascript">');
				e.write("window.focus();");
				e.write("print_flexpaper_Document('" + this.renderer.sd() + "'," + d + ",'" + c + "', " + this.Mi() + ", " + this.getTotalPages() + ", '" + this.config.jsDirectory + "');");
				e.write("\x3c/script>");
				e.write("</body></html>");
				eb.browser.msie || setTimeout("window['printFrame'].close();", 3000);
				eb.browser.msie && 9 < eb.browser.version && e.close();
			}
		},
		switchMode: function(c, d) {
			var e = this;
			if (e.ba != c && !(("TwoPage" == c || "BookView" == c) && 2 > e.getTotalPages())) {
				d > e.getTotalPages() && (d = e.getTotalPages()), e.Ta && e.lf(), jQuery(e.pages.da).Sj(function() {
					e.ha && e.ha.switchMode(c, d);
					"Tile" == c && (e.ba = M);
					c == C && (e.ba = e.ub == U ? U : C);
					c == U && (e.ba = U);
					c == H && (e.ba = H);
					c == D && (e.ba = D);
					e.xf();
					e.pages.Tk();
					e.renderer.Kd = -1;
					e.renderer.Ra && e.renderer.Ra.Wk();
					c != H && c != D && (d != p ? e.pages.ja = d - 1 : d = 1);
					e.gh(d);
					jQuery(e.ca).trigger("onViewModeChanged", c);
					setTimeout(function() {
						!eb.platform.touchdevice || eb.platform.touchdevice && (c == U || c == C) ? e.fitheight() : c != H && (c != D && c != e.ra()) && e.fitwidth();
						c != H && c != D && e.Vc(d);
					}, 100);
				});
			}
		},
		fitwidth: function() {
			if (!(this.ba == H || this.ba == D || this.ba == M)) {
				var c = jQuery(this.pages.da).width() - 15,
					d = 1 < this.getTotalPages() ? this.sa - 1 : 0;
				0 > d && (d = 0);
				this.document.DisplayRange && (d = parseInt(this.document.DisplayRange.split("-")[0]) - 1);
				var e = this.pages.getPage(d).dimensions.Aa / this.pages.getPage(d).dimensions.Oa;
				eb.platform.touchdevice ? (c = c / (this.pages.getPage(d).$a * e) - 0.03, window.FitWidthScale = c, this.fb(c), this.pages.Pg()) : (c = c / (this.pages.getPage(d).$a * this.document.MaxZoomSize * e) - 0.012, window.FitWidthScale = c, jQuery(this.ca).trigger("onScaleChanged", c / this.document.MaxZoomSize), c * this.document.MaxZoomSize >= this.document.MinZoomSize && c <= this.document.MaxZoomSize && this.fb(this.document.MaxZoomSize * c));
			}
		},
		getCurrentRenderingMode: function() {
			return this.renderer instanceof CanvasPageRenderer ? "html5" : "html";
		},
		fb: function(c, d) {
			var e = this;
			if (e.initialized) {
				if (!d || d && !d.yh) {
					var f = 100 / (100 * e.document.ZoomInterval);
					c = Math.round(c * f) / f;
				}
				e.ba == e.ra() && 1 > c && (c = 1);
				jQuery(e.ca).trigger("onScaleChanged", c / e.document.MaxZoomSize);
				var f = jQuery(e.pages.da).prop("scrollHeight"),
					g = jQuery(e.pages.da).scrollTop(),
					f = 0 < g ? g / f : 0;
				e.Ed != p && (window.clearTimeout(e.Ed), e.Ed = p);
				e.pages.Sk() && e.scale != c && (jQuery(".flexpaper_annotation_" + e.ia).remove(), jQuery(".flexpaper_pageword_" + e.ia).remove());
				e.Ed = setTimeout(function() {
					e.cc();
					e.pages.Ka();
				}, 500);
				if (0 < c) {
					c < e.config.document.MinZoomSize && (c = this.config.document.MinZoomSize);
					c > e.config.document.MaxZoomSize && (c = this.config.document.MaxZoomSize);
					e.pages.Qa(c, d);
					e.scale = c;
					!d || d && !d.Cc ? e.pages.pages[0] && e.pages.pages[0].Gd() : e.pages.Xe(d.Fb, d.Xb);
					jQuery(e.ca).trigger("onZoomFactorChanged", {
						Ld: c,
						aa: e
					});
					if ("undefined" != window.FitWidthScale && Math.round(100 * window.FitWidthScale) == Math.round(100 * (c / e.document.MaxZoomSize))) {
						if (jQuery(e.ca).trigger("onFitModeChanged", "FitWidth"), window.onFitModeChanged) {
							window.onFitModeChanged("Fit Width");
						}
					} else {
						if ("undefined" != window.FitHeightScale && Math.round(100 * window.FitHeightScale) == Math.round(100 * (c / e.document.MaxZoomSize))) {
							if (jQuery(e.ca).trigger("onFitModeChanged", "FitHeight"), window.onFitModeChanged) {
								window.onFitModeChanged("Fit Height");
							}
						} else {
							if (jQuery(e.ca).trigger("onFitModeChanged", "FitNone"), window.onFitModeChanged) {
								window.onFitModeChanged("Fit None");
							}
						}
					}
					e.pages.dd();
					e.pages.pc();
					e.pages.Pg();
					g = jQuery(e.pages.da).prop("scrollHeight");
					eb.browser.jb.rb && (!d || d && !d.Cc ? jQuery(e.pages.da).scrollTo({
						left: "50%",
						top: g * f + "px"
					}, 0, {
						axis: "xy"
					}) : jQuery(e.pages.da).scrollTo({
						top: g * f + "px"
					}, 0, {
						axis: "y"
					}));
				}
			}
		},
		cc: function() {
			this.Ed != p && (window.clearTimeout(this.Ed), this.Ed = p);
			"CanvasPageRenderer" == this.renderer.sd() && jQuery(".flexpaper_pageword_" + this.ia + ":not(.flexpaper_selected_searchmatch)").remove();
			this.pages.Fe && this.pages.pages[this.pages.Fe].Va && this.renderer.Eb(this.pages.pages[this.pages.Fe], n);
			for (var c = 0; c < this.document.numPages; c++) {
				this.pages.Wa(c) && c != this.pages.Fe && (this.pages.pages[c].Va ? this.renderer.Eb(this.pages.pages[c], n) : this.pages.pages[c].za = v);
			}
		},
		Zoom: function(c, d) {
			!eb.platform.touchonlydevice || !(this.ba == H || this.ba == D) ? (c > this.document.MaxZoomSize && (c = this.document.MaxZoomSize), c /= this.document.MaxZoomSize, jQuery(this.ca).trigger("onScaleChanged", c), c * this.document.MaxZoomSize >= this.document.MinZoomSize && c <= this.document.MaxZoomSize && this.fb(this.document.MaxZoomSize * c, d)) : 1 < c ? this.ba == H || this.ba == D ? this.pages.ad() : (this.ba == C || this.ba == U) && this.fitwidth() : this.ba == H || this.ba == D ? this.pages.kc() : (this.ba == C || this.ba == U) && this.fitheight();
		},
		ZoomIn: function() {
			this.Zoom(this.scale + 3 * this.document.ZoomInterval);
		},
		ZoomOut: function() {
			if (this.ba == C || this.ba == U) {
				this.pages.jScrollPane != p ? (this.pages.jScrollPane.data("jsp").scrollTo(0, 0, v), this.pages.jScrollPane.data("jsp").reinitialise(this.Tb)) : this.pages.ea(this.pages.da).parent().scrollTo({
					left: 0,
					top: 0
				}, 0, {
					axis: "xy"
				});
			}
			this.Zoom(this.scale - 3 * this.document.ZoomInterval);
		},
		Ng: function() {
			var c = this;
			if (!eb.platform.bd && !c.Ta) {
				jQuery(".flexpaper_searchabstract_result, .flexpaper_searchabstract_result_separator").remove();
				var d = c.Wd = c.toolbar != p && c.toolbar.Ya != p ? c.toolbar.ya(c.toolbar.Ya, "Search") : "Search",
					e = c.ga.height() - (c.Fi != p ? c.Fi.height() + 20 : 0),
					f = c.ba == c.ra() ? jQuery(c.ca).css("background-color") : "#c8c8c8",
					g = c.ba == c.ra() ? "40px" : jQuery(c.ca).height() + 2,
					l = c.ba == c.ra() ? "color:#ededed" : "color:#555555;",
					u = c.ba == c.ra() ? 70 : 40,
					m = c.ba == c.ra() ? 0 : 41;
				"rgba(0, 0, 0, 0)" == f.toString() && (f = "#555");
				c.Ai = jQuery(c.ca).css("margin-left");
				c.ba == c.ra() ? (c.ka.append(jQuery(String.format("<div class='flexpaper_searchabstracts' style='position:absolute;left:0px;top:0px;height:{5}px;width:{2};min-width:{3};opacity: 0;z-index:13;'><div style='margin: 20px 20px 20px 20px;padding: 10px 10px 10px 10px;background-color:{6};height:{7}px'><div style='height:25px;width:100%' <div class='flexpaper_tblabel' style='margin-left:10px; width: 100%;height:25px;'><img src='{1}' style='vertical-align: middle'><span style='margin-left:10px;vertical-align: middle'>{0}</span><img src='{4}' style='float:right;margin-right:5px;cursor:pointer;' class='flexpaper_searchabstracts_close' /></div><hr size='1' color='#ffffff' /></div></div>", d, c.bh, "20%", "250px", c.ah, e, f, e - 20))), c.Ta = jQuery(".flexpaper_searchabstracts"), jQuery(c.Ta.children()[0]).css({
					"border-radius": "3px",
					"-moz-border-radius": "3px"
				}), jQuery(c.Ta.children()[0]).append("<div class='flexpaper_searchabstracts_content' style='display:block;position:relative;height:" + (jQuery(c.Ta.children()[0]).height() - u) + "px;margin-bottom:50px;width:100%;overflow-y: auto;overflow-x: hidden;'></div>"), c.resize(c.ga.width() - c.Ta.width(), c.ga.height() + m, v, x()), c.ga.animate({
					left: c.Ta.width() + "px"
				}, 0)) : (c.ka.append(jQuery(String.format("<div class='flexpaper_searchabstracts' style='position:absolute;left:0px;top:0px;height:{5}px;width:{2};min-width:{3};opacity: 0;z-index:13;overflow:hidden;'><div style='margin: 0px 0px 0px 0px;padding: 10px 7px 10px 10px;background-color:{6};height:{7}px'><div style='height:25px;width:100%' <div class='flexpaper_tblabel' style='margin-left:10px; width: 100%;height:25px;'><img src='{1}' style='vertical-align: middle'><span style='margin-left:10px;vertical-align: middle'>{0}</span><img src='{4}' style='float:right;margin-right:5px;cursor:pointer;' class='flexpaper_searchabstracts_close' /></div><div class='flexpaper_bottom_fade'></div></div></div>", d, c.bh, "20%", "250px", c.ah, c.ga.height(), f, c.ka.height() - 58))), c.Ta = jQuery(".flexpaper_searchabstracts"), jQuery(c.Ta.children()[0]).append("<div class='flexpaper_searchabstracts_content' style='display:block;position:relative;height:" + e + "px;margin-bottom:50px;width:100%;overflow-y: auto;overflow-x: hidden;'></div>"), c.ba != H && c.resize(c.ga.width() - c.Ta.width() / 2, c.ka.height() + 1, v, x()), c.ga.animate({
					left: c.Ta.width() / 2 + "px"
				}, 0), c.fitheight());
				d = 0.5 * c.Ta.width();
				jQuery(c.ca).width() + d > c.ka.width() && (d = 0);
				jQuery(c.ca).animate({
					"margin-left": parseFloat(c.Ai) + d + "px"
				}, 200, function() {
					if (window.onresize) {
						window.onresize();
					}
				});
				0 == d && c.Ta.css({
					top: g,
					height: c.ga.height() - 40 + "px"
				});
				c.ba == c.ra() && c.ha.Ng();
				c.Ta.fadeTo("fast", 1);
				var q = jQuery(".flexpaper_searchabstracts_content");
				jQuery(c).bind("onSearchAbstractAdded", function(d, e) {
					var f = e.nd.$j;
					100 < f.length && (f = f.substr(0, 100) + "...");
					f = f.replace(RegExp(window.Pc, "g"), "<font style='color:#ffffff'>[" + window.Pc + "]</font>");
					f = "<b>p." + (e.nd.pageIndex + 1) + "</b> : " + f;
					q.append(jQuery(String.format("<div id='flexpaper_searchabstract_item_{1}' style='{2}' class='flexpaper_searchabstract_result'>{0}</div><hr size=1 color='#777777' style='margin-top:8px;' class='flexpaper_searchabstract_result_separator' />", f, e.nd.id, l)));
					jQuery("#flexpaper_searchabstract_item_" + e.nd.id).bind("mousedown", function(d) {
						window.Ua = e.nd.pageIndex + 1;
						window.ed = e.nd.Uk;
						window.Vb = -1;
						c.searchText(window.Pc, v);
						d.preventDefault && d.preventDefault();
						d.returnValue = v;
					});
					jQuery("#flexpaper_searchabstract_item_" + e.nd.id).bind("mouseup", function(c) {
						c.preventDefault && c.preventDefault();
						c.returnValue = v;
					});
				});
				jQuery(".flexpaper_searchabstracts_close").bind("mousedown", function() {
					c.lf();
				});
			}
		},
		lf: function() {
			var c = this;
			c.Ta && (c.Ta.hide(), jQuery(".flexpaper_searchabstract_result, .flexpaper_searchabstract_result_separator").remove(), c.ba == c.ra() ? (c.resize(c.ga.width() + c.Ta.width(), c.ga.height(), v), c.ga.css({
				left: "0px"
			})) : c.ba == H ? (c.ga.css({
				left: "0px",
				width: "100%"
			}), c.fitheight()) : (c.resize(c.ga.width() + c.Ta.width(), c.ka.height() + 1, v), c.ga.css({
				left: "0px"
			})), jQuery(c.ca).animate({
				"margin-left": parseFloat(c.Ai) + "px"
			}, 200), c.ba == c.ra() && c.ha.lf(), c.Ta.fadeTo("fast", 0, function() {
				c.Ta.remove();
				c.Ta = p;
			}));
			jQuery(c).unbind("onSearchAbstractAdded");
		},
		Qh: function(c, d) {
			jQuery(".flexpaper_searchabstract_blockspan").remove();
			var e = this.renderer.getNumPages();
			d || (d = 0);
			for (var f = d; f < e; f++) {
				this.sj(f, c);
			}
			this.ba != this.ra() && jQuery(".flexpaper_searchabstracts_content").append(jQuery("<div class='flexpaper_searchabstract_blockspan' style='display:block;clear:both;height:200px'></div>"));
		},
		sj: function(c, d) {
			var e = this,
				f = e.renderer.gb;
			if (f[c] != p) {
				for (var g = f[c].toLowerCase().indexOf(d), l = 0; 0 < g;) {
					var u = 0 < g - 50 ? g - 50 : 0,
						m = g + 75 < f[c].length ? g + 75 : f[c].length,
						q = window.Ub.length;
					window.Ub.yd[q] = [];
					window.Ub.yd[q].pageIndex = c;
					window.Ub.yd[q].Uk = l;
					window.Ub.yd[q].id = c + "_" + l;
					window.Ub.yd[q].$j = f[c].substr(u, m - u);
					g = f[c].toLowerCase().indexOf(d, g + 1);
					jQuery(e).trigger("onSearchAbstractAdded", {
						nd: window.Ub.yd[q]
					});
					l++;
				}
			} else {
				e.hi == p && (e.hi = setTimeout(function() {
					e.renderer.Nc == p && e.renderer.Lc(c + 1, v, function() {
						e.hi = p;
						e.Qh(d, c);
					});
				}, 100));
			}
		},
		searchText: function(c, d) {
			var e = this;
			if (c != p) {
				if (d === h && (e.ba == C || e.ba == H || e.ba == e.ra()) && e.document.EnableSearchAbstracts && !eb.platform.bd) {
					d = n;
				}
				d && (e.ba == e.ra() && 1 < e.scale) && (e.renderer.dc && e.renderer.Vm(), e.Zoom(1));
				jQuery(".flexpaper_txtSearch").val() != c && jQuery(".flexpaper_txtSearch").val(c);
				if (e.ba == M) {
					e.switchMode(C), setTimeout(function() {
						e.searchText(c);
					}, 1000);
				} else {
					var f = e.renderer.gb,
						g = e.renderer.getNumPages();
					e.yf || (e.yf = 0);
					if (0 == e.renderer.Ra.La.length && 10 > e.yf) {
						window.clearTimeout(e.Vk), e.Vk = setTimeout(function() {
							e.searchText(c, d);
						}, 500), e.yf++;
					} else {
						e.yf = 0;
						window.ed || (window.ed = 0);
						window.Ua || (window.Ua = -1);
						c != p && 0 < c.length && (c = c.toLowerCase());
						window.Pc != c && (window.Vb = -1, window.Pc = c, window.ed = 0, window.Ua = -1, window.Ub = [], window.Ub.yd = []); - 1 == window.Ua ? window.Ua = parseInt(e.sa) : window.Vb += c.length;
						0 == window.Ub.yd.length && (window.Ub.searchText != c && d) && (window.Ub.searchText != c && e.ka.find(".flexpaper_searchabstract_result, .flexpaper_searchabstract_result_separator").remove(), window.Ub.searchText = c, e.Ng(), e.Qh(c));
						for (; window.Ua - 1 < g;) {
							var l = f[window.Ua - 1];
							e.renderer.Fa && l == p && (jQuery(e.renderer).trigger("UIBlockingRenderingOperation", e.ia), e.kl = window.Ua, e.renderer.Lc(window.Ua, v, function() {
								l = f[window.Ua - 1];
								e.kl = p;
							}));
							window.Vb = l.indexOf(c, -1 == window.Vb ? 0 : window.Vb);
							if (0 <= window.Vb) {
								e.sa != window.Ua && (e.ba == e.ra() && e.sa != window.Ua + 1 || e.ba == D && e.sa != window.Ua + 1 || e.ba == H && e.sa != window.Ua - 1 || e.ba == U && e.sa != window.Ua) && (e.ba == H || e.ba == D || e.ba == U || e.ba == e.ra()) ? e.gotoPage(window.Ua, function() {
									window.Vb -= c.length;
									e.searchText(c);
								}) : (window.ed++, e.renderer.pb ? this.pages.getPage(window.Ua - 1).load(function() {
									e.pages.getPage(window.Ua - 1).Qb(window.Pc, v);
								}) : (e.ba == C && this.pages.getPage(window.Ua - 1).load(function() {
									e.pages.getPage(window.Ua - 1).Qb(window.Pc, v);
								}), (e.ba == H || e.ba == U || e.ba == e.ra()) && this.pages.getPage(window.Ua - 1).Qb(window.Pc, v)));
								break;
							}
							window.Ua++;
							window.Vb = -1;
							window.ed = 0;
						} - 1 == window.Vb && (window.Vb = -1, window.ed = 0, window.Ua = -1, e.Jb(), alert(e.toolbar != p && e.toolbar.Ya != p ? e.toolbar.ya(e.toolbar.Ya, "Finishedsearching") : "No more search matches."), e.gotoPage(1));
					}
				}
			}
		},
		fitheight: function() {
			if (eb.platform.touchdevice) {
				if (c = this.Gh()) {
					window.FitHeightScale = c, this.fb(c), this.pages.Pg();
				}
			} else {
				var c = this.Gh();
				window.FitHeightScale = c;
				jQuery(this.ca).trigger("onScaleChanged", c / this.document.MaxZoomSize);
				c * this.document.MaxZoomSize >= this.document.MinZoomSize && c <= this.document.MaxZoomSize && this.fb(this.document.MaxZoomSize * c);
			}
		},
		Gh: function() {
			if (this.document.DisableOverflow) {
				return window.FitHeightScale = 1;
			}
			this.sa - 1 && (this.sa = 1);
			if (this.ba == C || this.ba == U || this.ba == H || this.ba == D) {
				var c = this.pages.getPage(this.sa - 1).dimensions.width / this.pages.getPage(this.sa - 1).dimensions.height;
				if (eb.platform.touchdevice) {
					if (d = jQuery(this.ga).height() - (this.ba == H || this.ba == D ? 40 : 0), d /= this.pages.getPage(this.sa - 1).$a, e = this.pages.getPage(this.sa - 1), e = e.$a * (e.dimensions.Aa / e.dimensions.Oa) * d, (this.ba == H || this.ba == D) && 2 * e > this.ga.width()) {
						d = this.ga.width() - 0, d /= 4 * this.pages.getPage(this.sa - 1).$a;
					}
				} else {
					var d = jQuery(this.pages.da).height() - (this.ba == H || this.ba == D ? 25 : 0),
						d = d / (this.pages.getPage(this.sa - 1).$a * this.document.MaxZoomSize),
						e = this.pages.getPage(this.sa - 1),
						e = e.$a * (e.dimensions.Aa / e.dimensions.Oa) * this.document.MaxZoomSize * d;
					if ((this.ba == H || this.ba == D) && 2 * e > this.ga.width()) {
						d = (jQuery(this.ga).width() - (this.ba == H || this.ba == D ? 40 : 0)) / 1.48, d = d / 1.6 / (this.pages.getPage(this.sa - 1).$a * this.document.MaxZoomSize * c);
					}
				}
				return window.FitHeightScale = d;
			}
		},
		next: function() {
			var c = this;
			c.vg || (c.vg = setTimeout(function() {
				window.clearTimeout(c.vg);
				c.vg = p;
			}, 700), c.pages.next());
		},
		gotoPage: function(c, d) {
			var e = this;
			e.ba == M ? eb.platform.ios ? e.ha ? e.ha.Gl(c) : e.switchMode(C, c) : e.switchMode(C, c) : (e.ba == C && e.pages.scrollTo(c), e.ba == U && setTimeout(function() {
				e.pages.ye(c, d);
			}, 300), (e.ba == H || e.ba == D) && setTimeout(function() {
				e.pages.ze(c, d);
			}, 300), e.ha && e.ha.gotoPage(c, d));
		},
		rotate: function() {
			this.pages.rotate(this.getCurrPage() - 1);
		},
		getCurrPage: function() {
			return this.pages != p ? this.ba != this.ra() ? this.pages.ja + 1 : this.pages.ja : 1;
		},
		Yk: function() {
			this.version = "2.3.1";
		},
		getTotalPages: function() {
			return this.pages.getTotalPages();
		},
		Vc: function(c) {
			var d = this;
			d.ba != d.ra() && (this.sa = c, this.pages.ja = this.sa - 1);
			if ((this.ba == H || this.ba == D) && this.pages.ja == this.pages.getTotalPages() - 1 && 0 != this.pages.ja % 2) {
				this.pages.ja += 1;
			}
			d.ha && (0 == c && c++, d.ha.Vc(c));
			0 < jQuery(".flexpaper_mark_video").find("iframe").length && jQuery(".flexpaper_mark_video").find("iframe").each(function() {
				try {
					var c = jQuery(this).closest(".flexpaper_page").attr("id"),
						f = parseInt(c.substr(14, c.lastIndexOf("_") - 14));
					if (0 == f && 0 != d.pages.ja - 1 || 0 < f && f != d.pages.ja - 1 && f != d.pages.ja - 2) {
						jQuery(this).parent().remove();
						var g = d.pages.pages[f];
						g.Ce(g.Uh ? g.Uh : g.scale, g.Gc());
					}
				} catch (l) {}
			});
			this.toolbar.Ll(c);
			d.Ob != p && (this.ba == H ? (d.Ob.Ye(this.pages.ja + 1), d.Ob.Ye(this.pages.ja + 2)) : this.ba == D ? (1 != c && d.Ob.Ye(this.pages.ja), d.Ob.Ye(this.pages.ja + 1)) : d.Ob.Ye(this.sa));
		},
		addLink: function(c, d, e, f, g, l) {
			window[this.Oh].addLink = this.addLink;
			c = parseInt(c);
			this.Ca[c - 1] == p && (this.Ca[c - 1] = []);
			var u = {
				type: "link"
			};
			u.href = d;
			u.sk = e;
			u.tk = f;
			u.width = g;
			u.height = l;
			this.Ca[c - 1][this.Ca[c - 1].length] = u;
		},
		addVideo: function(c, d, e, f, g, l, u) {
			window[this.Oh].addVideo = this.addVideo;
			c = parseInt(c);
			this.Ca[c - 1] == p && (this.Ca[c - 1] = []);
			var m = {
				type: "video"
			};
			m.src = d;
			m.url = e;
			m.Rl = f;
			m.Sl = g;
			m.width = l;
			m.height = u;
			this.Ca[c - 1][this.Ca[c - 1].length] = m;
		},
		addImage: function(c, d, e, f, g, l, u, m) {
			c = parseInt(c);
			this.Ca[c - 1] == p && (this.Ca[c - 1] = []);
			var q = {
				type: "image"
			};
			q.src = d;
			q.nk = e;
			q.ok = f;
			q.width = g;
			q.height = l;
			q.href = u;
			q.lk = m;
			this.Ca[c - 1][this.Ca[c - 1].length] = q;
		},
		openFullScreen: function() {
			if(window.parent != null && window.parent.document.getElementById("ifm") != null && window.parent.document.getElementById("ifm") != undefined){
				var pdoc = window.parent.document;
				var ifm = pdoc.getElementById("ifm");
				if(pdoc.bk || pdoc.mozFullScreen || pdoc.webkitIsFullScreen){
					return pdoc.exitFullscreen ? pdoc.exitFullscreen() : pdoc.mozCancelFullScreen ? pdoc.mozCancelFullScreen() : pdoc.webkitExitFullscreen && pdoc.webkitExitFullscreen();
				} else {
					if(pdoc.documentElement.requestFullScreen){
						ifm.requestFullScreen();
					} else if(pdoc.documentElement.mozRequestFullScreen){
						ifm.mozRequestFullScreen();
					} else if(pdoc.documentElement.webkitRequestFullScreen){
						ifm.webkitRequestFullScreen(!eb.browser.safari ? 1 : 0);
					} else if(pdoc.documentElement.msRequestFullscreen){
						ifm.msRequestFullscreen();
					} else {
						this.Ak();
					}
					return;
				}
			}
			var c = this,
				d = document.bk || document.mozFullScreen || document.webkitIsFullScreen || window.Ui || window.$g,
				e = c.ka.get(0);
			if (d) {
				return document.exitFullscreen ? document.exitFullscreen() : document.mozCancelFullScreen ? document.mozCancelFullScreen() : document.webkitExitFullscreen && document.webkitExitFullscreen(), v;
			}
			"0" != c.ka.css("top") && (c.Ck = c.ka.css("top"));
			"0" != c.ka.css("left") && (c.Bk = c.ka.css("left"));
			c.ba == c.ra() && 1 < c.scale && (c.pages.kc(), c.fisheye.show(), c.fisheye.animate({
				opacity: 1
			}, 100));
			c.Aa = c.ka.width();
			c.Oa = c.ka.height();
			c.PreviewMode && eb.browser.jb.Kl && (c.PreviewMode = v, c.Ff = n, c.ha.cb.Nk(c.pages, c.ga), c.ha.il());
			c.ka.css({
				visibility: "hidden"
			});
			jQuery(document).bind("webkitfullscreenchange mozfullscreenchange fullscreenchange MSFullscreenChange", function() {
				setTimeout(function() {
					if (window.navigator.Ym || document.ak && document.ak != p || document.mozFullScreen || document.webkitIsFullScreen) {
						eb.browser.safari ? window.zine ? c.resize(screen.width, screen.height) : c.config.BottomToolbar ? c.resize(screen.width, screen.height - jQuery(c.ca).height() - 70) : c.resize(screen.width, screen.height - jQuery(c.ca).height()) : window.zine ? c.resize(window.outerWidth, window.outerHeight) : c.config.BottomToolbar ? c.resize(window.outerWidth, window.outerHeight - jQuery(c.ca).height() - 70) : c.resize(window.outerWidth, window.outerHeight - jQuery(c.ca).height());
					}
					window.annotations && (jQuery(".flexpaper_pageword_" + c.ia).remove(), c.cc(), c.pages.Ka());
					c.ka.css({
						visibility: "visible"
					});
				}, 500);
				jQuery(document).bind("webkitfullscreenchange mozfullscreenchange fullscreenchange MSFullscreenChange", function() {
					jQuery(document).unbind("webkitfullscreenchange mozfullscreenchange fullscreenchange MSFullscreenChange");
					c.ka.css({
						top: c.Ck,
						left: c.Bk
					});
					c.Ff && (c.PreviewMode = n, c.ha.Lh(), c.ha.jf(), setTimeout(function() {
						c.PreviewMode && c.ha.jf();
					}, 1000));
					c.ba == c.ra() && 1 < c.scale ? c.pages.kc(function() {
						c.fisheye.show();
						c.fisheye.animate({
							opacity: 1
						}, 100);
						c.resize(c.Aa, c.Oa - 2);
						jQuery(c.ca).trigger("onFullscreenChanged", v);
					}) : (c.resize(c.Aa, c.Oa - 2), jQuery(c.ca).trigger("onFullscreenChanged", v));
					jQuery(document).unbind("webkitfullscreenchange mozfullscreenchange fullscreenchange MSFullscreenChange");
					c.Ff && (c.Ff = v, c.ha.cb.Gf(c.pages, c.ga));
					window.annotations && (jQuery(".flexpaper_pageword_" + c.ia).remove(), c.cc(), c.pages.Ka());
				});
				window.clearTimeout(c.Fg);
				c.Fg = setTimeout(function() {
					!c.PreviewMode && (c.ha && c.ha.te) && c.ha.Mg();
				}, 1000);
			});
			d = eb.platform.android;
			document.documentElement.requestFullScreen && !d ? (c.ka.css({
				visibility: "hidden"
			}), e.requestFullScreen(), c.ka.css({
				left: "0px",
				top: "0px"
			})) : document.documentElement.mozRequestFullScreen && !d ? (c.ka.css({
				visibility: "hidden"
			}), e.mozRequestFullScreen(), c.ka.css({
				left: "0px",
				top: "0px"
			})) : document.documentElement.webkitRequestFullScreen && !d ? (c.ka.css({
				visibility: "hidden"
			}), e.webkitRequestFullScreen(!eb.browser.safari ? 1 : 0), c.ka.css({
				left: "0px",
				top: "0px"
			})) : document.documentElement.msRequestFullscreen ? (c.ka.css({
				visibility: "hidden"
			}), c.Nh ? (c.Nh = v, window.document.msExitFullscreen()) : (c.Nh = n, e.msRequestFullscreen()), setTimeout(function() {
				c.ka.css({
					visibility: "visible"
				});
				c.resize(window.outerWidth, window.outerHeight);
				window.annotations && (jQuery(".flexpaper_pageword_" + c.ia).remove(), c.cc(), c.pages.Ka());
			}, 500)) : (c.Ak(), setTimeout(function() {
				c.ka.css({
					visibility: "visible"
				});
			}, 500));
			jQuery(c.ca).trigger("onFullscreenChanged", n);
		},
		Ak: function() {
			var c = "",
				c = "toolbar=no, location=no, scrollbars=no, width=" + screen.width,
				c = c + (", height=" + screen.height),
				c = c + ", top=0, left=0, fullscreen=yes";
			nw = window.open("", "windowname4", c);
			nw.params = c;
			c = "<!doctype html><head>";
			c += '<meta name="viewport" content="initial-scale=1,user-scalable=no,maximum-scale=1,width=device-width" />';
			c += '<link rel="stylesheet" type="text/css" href="' + this.config.cssDirectory + (-1 == this.config.cssDirectory.indexOf("flexpaper.css") ? "flexpaper.css" : "") + '" />';
			c += '<script type="text/javascript" src="' + this.config.jsDirectory + 'jquery.js">\x3c/script>';
			c += '<script type="text/javascript" src="' + this.config.jsDirectory + 'jquery.extensions.min.js">\x3c/script>';
			c += '<script type="text/javascript" src="' + this.config.jsDirectory + 'flexpaper.js">\x3c/script>';
			c += '<script type="text/javascript" src="' + this.config.jsDirectory + 'flexpaper_handlers.js">\x3c/script>';
			c += '<style type="text/css" media="screen">body{ margin:0; padding:0; overflow-x:hidden;overflow-y:hidden; }</style>';
			c += "</head>";
			c += '<body onload="openViewer();">';
			c += '<div id="documentViewer" class="flexpaper_viewer" style="position:absolute;width:100%;height:' + (window.annotations ? "95" : "100") + '%;"></div>';
			c += '<script type="text/javascript">';
			c += "function openViewer(){";
			c += 'jQuery("#documentViewer").FlexPaperViewer(';
			c += "{ config : {";
			c += "";
			if(this.document.IMGFiles != undefined){
				c += 'IMGFiles : "' + this.document.IMGFiles + '",';
			}
			if(this.document.JSONFile != undefined){
				c += 'JSONFile : "' + this.document.JSONFile + '",';
			}
			c += 'PDFFile : "' + this.document.PDFFile + '",';
			c += 'localeDirectory : "' + this.config.localeDirectory + '",';
			c += 'UIConfig : "' + this.document.UIConfig + '",';
			c += 'localeChain: "' + this.document.localeChain + '"';
			c += "}});";
			c += "}";
			c += "\x3c/script>";
			c += "</body>";
			c += "</html>";
			nw.document.write(c);
			nw.$g = n;
			window.focus && nw.focus();
			nw.document.close();
			return v;
		},
		resize: function(c, d, e, f) {
			var g = this;
			g.width = p;
			if (g.ba == g.ra()) {
				g.ha.resize(c, d, e, f);
			} else {
				var l = jQuery(g.ca).height() + 1 + 14;
				g.ga.css({
					width: c,
					height: d - l - g.me
				});
				(e == p || e == n) && this.ka.css({
					width: c,
					height: d
				});
				g.pages.resize(c, d - l - g.me, f);
				jQuery(".flexpaper_interactiveobject_" + g.ia).remove();
				jQuery(".flexpaper_pageword_" + g.ia).remove();
				(g.ba == H || g.ba == D) && g.fitheight();
				window.clearTimeout(g.Ek);
				g.Ek = setTimeout(function() {
					g.pages.Ka();
				}, 700);
			}
			g.ha && g.ha.te && (window.clearTimeout(g.Fg), g.Fg = setTimeout(function() {
				g.PreviewMode || g.ha.Mg();
			}, 2500));
			g.Ta && !g.ha ? g.ga.animate({
				left: g.Ta.width() / 2 + "px"
			}, 0) : g.Ta && g.ha && g.ga.animate({
				left: g.Ta.width() + "px"
			}, 0);
		}
	};
	g.loadFromUrl = g.loadFromUrl;
	return g;
}();
window.print_flexpaper_Document = function(g, c, d, e, f, k) {
	FLEXPAPER.Ih.init();
	var l = Array(f + 1),
		u = 0;
	if ("all" == d) {
		for (d = 1; d < f + 1; d++) {
			l[d] = n;
		}
		u = f;
	} else {
		if ("current" == d) {
			l[e] = n, u = 1;
		} else {
			if (-1 == d.indexOf(",") && -1 < d.indexOf("-")) {
				f = parseInt(d.substr(0, d.toString().indexOf("-")));
				for (e = parseInt(d.substr(d.toString().indexOf("-") + 1)); f < e + 1; f++) {
					l[f] = n, u++;
				}
			} else {
				if (0 < d.indexOf(",")) {
					var m = d.split(",");
					for (d = 0; d < m.length; d++) {
						if (-1 < m[d].indexOf("-")) {
							f = parseInt(m[d].substr(0, m[d].toString().indexOf("-")));
							for (e = parseInt(m[d].substr(m[d].toString().indexOf("-") + 1)); f < e + 1; f++) {
								l[f] = n, u++;
							}
						} else {
							l[parseInt(m[d].toString())] = n, u++;
						}
					}
				}
			}
		}
	} if ("ImagePageRenderer" == g) {
		var q = new ImagePageRenderer(p, c, k);
		q.initialize(function() {
			for (var c = "", d = 0, e = 0; e < q.getNumPages(); e++) {
				if (l[e + 1] && (c += "<div class='flexpaper_ppage " + (e < q.getNumPages() - 1 ? "ppage_break" : "ppage_none") + "'><img id='printImage_" + e + "' src='" + q.xa(e + 1) + "'></div>", d = e), parent.onPrintRenderingProgress) {
					parent.onPrintRenderingProgress(e + 1);
				}
			}
			jQuery(document.body).append(c);
			jQuery("#printImage_" + d).bind("load", function() {
				window.document.close();
				window.focus();
				window.print();
				window.close();
			});
			parent.onPrintRenderingCompleted();
		}, {});
	}
	"CanvasPageRenderer" == g && (q = new CanvasPageRenderer(p, c.PdfFile, k, c), q.initialize(function() {
		for (var c = "", d = 0; d < q.getNumPages(); d++) {
			l[d + 1] && (c += "<div><canvas id='ppage_" + d + "' class='flexpaper_ppage " + (d < q.getNumPages() - 1 ? "ppage_break" : "ppage_none") + "'></canvas></div>");
		}
		jQuery(document.body).append(c);
		renderPrintPage(q, 0);
	}, {}));
};
window.renderPrintPage = function za(c, d) {
	"CanvasPageRenderer" == c.sd() && (d < c.getNumPages() ? c.Fa ? c.sg(d + 1, function() {
		if (parent.onPrintRenderingProgress) {
			parent.onPrintRenderingProgress(d + 1);
		}
		document.getElementById("ppage_" + d) ? c.Pa[d].getPage(1).then(function(e) {
			var f = document.getElementById("ppage_" + d);
			if (f) {
				var k = f.getContext("2d"),
					l = e.getViewport(1),
					k = {
						canvasContext: k,
						viewport: l,
						zf: p,
						continueCallback: function(c) {
							c();
						}
					};
				f.width = l.width;
				f.height = l.height;
				e.render(k).promise.then(function() {
					e.destroy();
					za(c, d + 1);
				}, function(c) {
					console.log(c);
				});
			} else {
				za(c, d + 1);
			}
		}) : za(c, d + 1);
	}) : c.Pa.getPage(d + 1).then(function(e) {
		if (parent.onPrintRenderingProgress) {
			parent.onPrintRenderingProgress(d + 1);
		}
		var f = document.getElementById("ppage_" + d);
		if (f) {
			var k = f.getContext("2d"),
				l = e.getViewport(4),
				k = {
					canvasContext: k,
					viewport: l,
					zf: p,
					continueCallback: function(c) {
						c();
					}
				};
			f.width = l.width;
			f.height = l.height;
			e.render(k).promise.then(function() {
				za(c, d + 1);
				e.destroy();
			}, function(c) {
				console.log(c);
			});
		} else {
			za(c, d + 1);
		}
	}) : (parent.onPrintRenderingCompleted(), window.print()));
};
ya && self.addEventListener("message", function(g) {
	g = g.data;
	if ("undefined" !== g.cmd) {
		switch (g.cmd) {
			case "loadImageResource":
				var c = new XMLHttpRequest;
				c.open("GET", "../../" + g.src);
				c.Mm = c.responseType = "arraybuffer";
				c.onreadystatechange = function() {
					if (4 == c.readyState && 200 == c.status) {
						for (var d = new Uint8Array(this.response), e = d.length, f = Array(e); e--;) {
							f[e] = String.fromCharCode(d[e]);
						}
						self.postMessage({
							status: "ImageResourceLoaded",
							blob: f.join("")
						});
						self.close();
					}
				};
				c.send(p);
		}
	}
}, v);