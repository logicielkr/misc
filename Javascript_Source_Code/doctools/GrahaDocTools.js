if(typeof(GrahaDocTools)=='undefined') {
	function GrahaDocTools() {}
}

GrahaDocTools.lastX = 0;
GrahaDocTools.lastY = 0;
GrahaDocTools.index = 0;
GrahaDocTools.isAddContextMenu = false;
GrahaDocTools.isAddShortcut = false;
if(typeof(GrahaDocTools.fontColor)=='undefined') {
	GrahaDocTools.fontColor = "black";
//	GrahaDocTools.fontColor = "red";
}
if(typeof(GrahaDocTools.boxBorderStyle)=='undefined') {
	GrahaDocTools.boxBorderStyle = "2px dashed red";
//	GrahaDocTools.boxBorderStyle = "2px dashed blue";
}
if(typeof(GrahaDocTools.numberString)=='undefined') {
	GrahaDocTools.numberString = " ❶❷❸❹❺❻❼❽❾❿⓫⓬⓭⓮⓯⓰⓱⓲⓳⓴";
//음각 원문자가 눈에 잘 띄는데, 부득이한 경우 양각 원문자를 사용한다.
//	GrahaDocTools.numberString = " ①②③④⑤⑥⑦⑧⑨⑩⑪⑫⑬⑭⑮⑯⑰⑱⑲⑳㉑㉒㉓㉔㉕㉖㉗㉘㉙㉚㉛㉜㉝㉞㉟㊱㊲㊳㊴㊵㊶㊷㊸㊹㊺㊻㊼㊽㊾㊿";
}

GrahaDocTools.shortcutWithAltKey = function(event, item) {
	if(event.altKey) {
		return GrahaDocTools.shortcut(event, item);
	}
	return false;
}
GrahaDocTools.shortcut = function(event, item) {
	if(
		typeof(item) == "number" &&
		(
			event.keyCode == item ||
			event.keyCode == String.fromCharCode(item).toLowerCase().charCodeAt(0) ||
			event.keyCode == String.fromCharCode(item).toUpperCase().charCodeAt(0)
		)
	) {
		return true;
	} else if(
		typeof(item) == "string" &&
		item.length == 1 &&
		(
			event.keyCode == item.charCodeAt(0) ||
			event.keyCode == item.toLowerCase().charCodeAt(0) ||
			event.keyCode == item.toUpperCase().charCodeAt(0)
		)
	) {
		return true;
	} else if(
		typeof(item) == "string" &&
		item.toLowerCase() == "space" &&
		event.keyCode == 27
	) {
		return true;
	} else if(
		typeof(item) == "string" &&
		item.toLowerCase() == "enter" &&
		event.which == 13
	) {
		return true;
	} else if(
		typeof(item) == "string" &&
		item.toLowerCase() == "esc" &&
		event.which == 32
	) {
		return true;
	}
	return false;
};
GrahaDocTools.addRect = function(event) {
	GrahaDocTools.index++;
	if(GrahaDocTools.index >= GrahaDocTools.numberString.length) {
		alert("Maximum box count = " + (GrahaDocTools.numberString.length - 1));
		return;
	}
	$("body").append(
		$("<div class=\"GrahaDocTools.resizable.right\" id=\"GrahaDocTools.rect." + GrahaDocTools.index + "\">" + GrahaDocTools.numberString.charAt(GrahaDocTools.index) + "</div>"
		).css(
			"top", $(window).scrollTop() + GrahaDocTools.lastY
		).css(
			"left", $(window).scrollLeft() + GrahaDocTools.lastX
		).css(
			"position", "absolute"
		).css(
			"width", "150px"
		).css(
			"height", "40px"
		).css(
			"border", GrahaDocTools.boxBorderStyle
		).css(
			"text-align", "right"
		).css(
			"font-weight", "700"
		).css(
			"font-size", "large"
		).css(
			"padding", "0px"
		).css(
			"margin", "0px"
		).css(
			"color", GrahaDocTools.fontColor
		).resizable(
			{autoHide: true}
		).draggable()
	);
};
GrahaDocTools.addRectLeft = function(event) {
	if(GrahaDocTools.index >= GrahaDocTools.numberString.length) {
		alert("Maximum box count = " + (GrahaDocTools.numberString.length - 1));
		return;
	}
	GrahaDocTools.index++;
	$("body").append(
		$("<div class=\"GrahaDocTools.resizable.left\" id=\"GrahaDocTools.rect." + GrahaDocTools.index + "\">" + GrahaDocTools.numberString.charAt(GrahaDocTools.index) + "</div>"
		).css(
			"top", $(window).scrollTop() + GrahaDocTools.lastY
		).css(
			"position", "absolute"
		).css(
			"left", $(window).scrollLeft() + GrahaDocTools.lastX - 150
		).css(
			"width", "150px"
		).css(
			"height", "40px"
		).css(
			"border", GrahaDocTools.boxBorderStyle
		).css(
			"text-align", "left"
		).css(
			"font-weight", "700"
		).css(
			"font-size", "large"
		).css(
			"padding", "0px"
		).css(
			"margin", "0px"
		).css(
			"color", GrahaDocTools.fontColor
		).resizable(
			{autoHide: true}
		).draggable()
	);
};
GrahaDocTools.capture = function(event) {
	GrahaDocTools.lastX = event.clientX;
	GrahaDocTools.lastY = event.clientY;
};
GrahaDocTools.undo = function(event) {
	if(GrahaDocTools.index > 0) {
		$("div#GrahaDocTools\\.rect\\." + (GrahaDocTools.index)).remove();
		GrahaDocTools.index--;
	}
};
GrahaDocTools.clearAll = function(event) {
	var list = document.querySelectorAll("div.GrahaDocTools\\.resizable\\.left");
	for(let i = 0; i < list.length; i++) {
		list[i].parentElement.removeChild(list[i]);
	}
	
	list = document.querySelectorAll("div.GrahaDocTools\\.resizable\\.right");
	for(let i = 0; i < list.length; i++) {
		list[i].parentElement.removeChild(list[i]);
	}
	GrahaDocTools.index = 0;
};
if(typeof(GrahaDocTools.loadScript)=='undefined') {
	GrahaDocTools.loadScript = function(src) {
		var script = GrahaDocTools.loaded(src, "js");
		if(script == null) {
			return new Promise(function(resolve, reject) {
				script = document.createElement('script');
				script.src = src;
				script.setAttribute("charset", "utf-8");
				
				script.onload = function() {
					resolve(script);
				}
				script.onerror = function() {
					reject(new Error('loading fail'));
				}
				document.getElementsByTagName('HEAD').item(0).appendChild(script);
			});
		} else {
			return new Promise(function(resolve, reject)	 {
				resolve(script);
			});
		}
	}
};
/*
//For legacy Firefox

GrahaDocTools.addContextMenu = function() {
	try {
		if(GrahaDocTools.isAddContextMenu) {
			console.log("Already AddContextMenu");
			return;
		}
		var menu = document.createElement('menu');
		menu.setAttribute("type", "context");
		menu.setAttribute("id", "GrahaDocToolsContextMenu");
		
		var m1 = document.createElement('menu');
		m1.setAttribute("label", "문서화도구");
		
		var item1 = document.createElement('menuitem');
		item1.setAttribute("label", "사각형추가");
		item1.setAttribute("onclick", "GrahaDocTools.addRect(event)");
		
		var item2 = document.createElement('menuitem');
		item2.setAttribute("label", "사각형추가(L)");
		item2.setAttribute("onclick", "GrahaDocTools.addRectLeft(event)");
		
		var item3 = document.createElement('menuitem');
		item3.setAttribute("label", "Undo");
		item3.setAttribute("onclick", "GrahaDocTools.undo(event)");
		
		var item4 = document.createElement('menuitem');
		item4.setAttribute("label", "모두지우기");
		item4.setAttribute("onclick", "GrahaDocTools.clearAll(event)");
		
		m1.appendChild(item1);
		m1.appendChild(item2);
		m1.appendChild(item3);
		m1.appendChild(item4);
		menu.appendChild(m1);
		
		document.body.appendChild(menu);
		
		document.body.setAttribute("contextmenu", "GrahaDocToolsContextMenu");
		document.body.setAttribute("oncontextmenu", "GrahaDocTools.capture(event)");
		GrahaDocTools.isAddContextMenu = true;
	} catch (error) {
		alert(error);
	}
}
*/
GrahaDocTools.addShortcut = function() {
	try {
		if(GrahaDocTools.isAddShortcut) {
			console.log("Already AddShortcut");
			return;
		}
//		$(window).off("keydown");
//		$(window).off("mousemove");
		$(window).mousemove(function(e) {
			GrahaDocTools.capture(e);
		});
		$(window).keydown(function(e) {
			if(GrahaDocTools.shortcutWithAltKey(e, "r")) {
				GrahaDocTools.addRect(e);
				e.preventDefault();
				return false;
			} else if(GrahaDocTools.shortcutWithAltKey(e, "l")) {
				GrahaDocTools.addRectLeft(e);
				e.preventDefault();
				return false;
			} else if(GrahaDocTools.shortcutWithAltKey(e, "u")) {
				GrahaDocTools.undo(e);
				e.preventDefault();
				return false;
			} else if(GrahaDocTools.shortcutWithAltKey(e, "c")) {
				GrahaDocTools.clearAll(e);
				e.preventDefault();
				return false;
			}
		});
		GrahaDocTools.isAddShortcut = true;
	} catch (error) {
		alert(error);
	}
}
GrahaDocTools.getComponentURL = function(name, type) {
	if(type == "css") {
		if(name == "jquery-ui") {
			return "https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css";
		}
		throw new Error("type css, but name != jquery-ui");
	} else if(type == "js") {
		for(let i = 0; i < document.getElementsByTagName('script').length; i++) {
			let element= document.getElementsByTagName('script').item(i);
			if(element && element.src != null) {
				let src = element.src;
				if(src.lastIndexOf("/") >= 0) {
					src = src.substring(src.lastIndexOf("/") + 1);
				}
				if(name == "jquery") {
					if(
						!src.startsWith("jquery-ui") &&
						(
							src.startsWith("jquery-") ||
							src.startsWith("jquery.")
						)
					) {
						return element.src;
					}
				} else if(name == "jquery-ui") {
					if(src.startsWith("jquery-ui")) {
						return element.src;
					}
				}
			}
		}
		if(name == "jquery") {
			return "https://code.jquery.com/jquery-1.12.4.js";
		} else if(name == "jquery-ui") {
			return "https://code.jquery.com/ui/1.12.1/jquery-ui.js";
		}
		throw new Error("type css, but name not in (jquery, jquery-ui)");
	}
	throw new Error("type not in (css, js)");
}
GrahaDocTools.loaded = function(src, type) {
	if(type == "css") {
		for(let i = 0; i < document.getElementsByTagName('link').length; i++) {
			let element= document.getElementsByTagName('link').item(i);
			if(element && element.href != null && element.href == src) {
				console.log("Already Loaded", src);
				return element;
			}
		}
	} else if(type == "js") {
		for(let i = 0; i < document.getElementsByTagName('script').length; i++) {
			let element= document.getElementsByTagName('script').item(i);
			if(element && element.src != null && element.src == src) {
				return element;
			}
		}
	}
	return null;
}
GrahaDocTools.addMenuForWebDeveloperTools = function() {
	GrahaDocTools.loadCSS(GrahaDocTools.getComponentURL("jquery-ui", "css")).then(function(value) {
		GrahaDocTools.loadScript(GrahaDocTools.getComponentURL("jquery", "js")).then(function(value) {
			GrahaDocTools.loadScript(GrahaDocTools.getComponentURL("jquery-ui", "js")).then(function(value) {
					GrahaDocTools.addShortcut();
			});
		});
	});
};
GrahaDocTools.addMenuForFirefoxExtension = function() {
	GrahaDocTools.loadCSS(GrahaDocTools.getComponentURL("jquery-ui", "css")).then(function(value) {
		GrahaDocTools.addShortcut();
	});
};
GrahaDocTools.addMenuForWeb = function() {
	GrahaDocTools.addShortcut();
};
GrahaDocTools.loadCSS = function(src) {
	var script = GrahaDocTools.loaded(src, "css");
	if(script == null) {
		return new Promise(function(resolve, reject) {
			script = document.createElement('link');
			script.setAttribute("rel", "stylesheet");
			script.href = src;
			
			script.onload = function() {
				resolve(script);
			}
			script.onerror = function() {
				reject(new Error('loading fail'));
			}
			document.getElementsByTagName('HEAD').item(0).appendChild(script);
		});
	} else {
		return new Promise(function(resolve, reject) {
			resolve(script);
		});
	}
};