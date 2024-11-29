# Graha 문서화 도구 (GrahaDocTools.js)

## 1. about

### 1.1. 소개 

Graha 문서화 도구 (GrahaDocTools.js)는 웹페이지에 원문자가 담겨 있는 박스를 삽입할 수 있는 도구이며,
화면캡처 확장기능과 함께 사용하면,
웹페이지를 설명하는 문서를 만들 때 유용하게 사용할 수 있다.

### 1.2. 다운로드

https://github.com/logicielkr/misc/tree/master/Javascript_Source_Code/doctools

### 1.3. 호환성

다음의 환경에서 테스트했다.

- Firefox 최신버전

### 1.4. 의존성

먼저 화면캡처를 할 수 있는 확장기능을 찾아서 설치해야 한다.

> Graha 문서화 도구 (GrahaDocTools.js)는 화면캡처기능을 지원하지 않는다.

## 2. 사용법

### 2.1. 이미지를 불러와서 사용하는 방법

1. doctools.html 을 Firefox 에서 불러온다.

2. 이미지 파일 1개를 드래그 앤 드롭(Drag and Drop) 한다.

	```doctools.html?image=``` 와 같은 방법으로 이미지 경로를 파라미터로 지정해서 이미지를 불어올 수도 있다.
	
	다만 최신의 Firefox 라면, 
	웹페이지에서 다른 서버에 있는 이미지를 불어올 때,
	(다른 탭(Tab) 에서 다른 서버에 인증(로그인)을 한 상태라고 하더라도,)
	다른 서버에는 쿠키(Cookie)를 보내지 않으므로
	다음의 이미지만 불러올 수 있다.
	
	- image 파라미터로 지정된 경로(URL)가 같은 서버에 위치하는 경우
	- image 파라미터로 지정된 경로(URL)가 별도의 인증(로그인) 없이 접근할 수 있는 경우

3. 다음과 같은 단축키를 사용해 본다.

   - Alt + l : (Left) 왼쪽 상단에 번호가 있는 박스를 만든다.
   - Alt + r : (Right) 오른쪽 상단에 번호가 있는 박스를 만든다.
   - Alt + u : (Undo) 최근에 만든 박스 1개를 지운다.
   - Alt + c : (Clear) 모든 박스를 지운다.

   > 음각 원문자가 ❶ 부터 ⓴ 까지 밖에 없기 때문에, 박스는 20개까지만 추가할 수 있으나, GrahaDocTools.numberString 에 적당한 원문자를 추가해서 20개보다 갯수를 늘릴 수 있다.
   
   > GrahaDocTools.fontColor 를 미리 정의하거나 변경하면, 박스 안에 원문자의 글자 색상을 변경 할 수 있다.
   
   > GrahaDocTools.boxBorderStyle 를 미리 정의하거나 변경하면, 박스의 테두리(Border) 모양 및 색상을 변경할 수 있다.

### 2.2. 웹 개발자 도구에서 사용하는 방법

1. "도구 > 브라우저 도구 > 웹 개발자 도구" 로 들어가서
   "콘솔" 탭을 클릭한 다음
   다음의 Javascript 코드를 복사하여 붙여넣기 한 뒤에
   "Ctrl + Enter" 혹은 실행을 클릭한다.
   
```javascript
function GrahaDocTools() {}
GrahaDocTools.loadScript = function(src) {
	var script = null;
	for(let i = 0; i < document.getElementsByTagName('script').length; i++) {
		let element= document.getElementsByTagName('script').item(i);
		if(element && element.src != null && element.src == src) {
			script = element;
			console.log("Already Loaded", src);
		}
	}
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
};
GrahaDocTools.loadScript("//graha.kr/static-contents/client_lib/doctools/lastest/GrahaDocTools.js").then(function(value) {
	GrahaDocTools.addMenuForWebDeveloperTools();
});
```

2. "웹 개발자 도구" 가장 오른쪽 상단의 X 를 클릭해서 "웹 개발자 도구" 를 닫는다.

3. 다음과 같은 단축키를 사용해 본다.

   - Alt + l : (Left) 왼쪽 상단에 번호가 있는 박스를 만든다.
   - Alt + r : (Right) 오른쪽 상단에 번호가 있는 박스를 만든다.
   - Alt + u : (Undo) 최근에 만든 박스 1개를 지운다.
   - Alt + c : (Clear) 모든 박스를 지운다.

   > 음각 원문자가 ❶ 부터 ⓴ 까지 밖에 없기 때문에, 박스는 20개까지만 추가할 수 있으나, GrahaDocTools.numberString 에 적당한 원문자를 추가해서 20개보다 갯수를 늘릴 수 있다.
   
   > GrahaDocTools.fontColor 를 미리 정의하거나 변경하면, 박스 안에 원문자의 글자 색상을 변경 할 수 있다.
   
   > GrahaDocTools.boxBorderStyle 를 미리 정의하거나 변경하면, 박스의 테두리(Border) 모양 및 색상을 변경할 수 있다.
   
4. 만들어진 박스는 마우스로 크기를 조정하거나, 위치를 이동할 수 있다.

5. 화면캡처를 할 수 있는 확장기능으로 화면을 캡처한다.

### 2.3. Firefox 디버깅 페이지에서 임시 확장 기능으로 추가해서 사용하는 방법

1. 다음과 같이 manifest.json 를 만든다.

```javascript
{
	"description": "Adds a GrahaDocTools",
	"manifest_version": 2,
	"name": "GrahaDocTools",
	"version": "1.0",
	"homepage_url": "https://graha.kr/static-contents/client_lib/doctools/lastest/README.md",
	
	"content_scripts": [
		{
			"matches": ["<all_urls>"],
			"js": ["jquery-1.12.4.js", "jquery-ui.js", "GrahaDocTools.js", "initForFirefoxExtension.js"]
		}
	]
}
```

```"matches"``` 부분을 다음과 같이 변경하면, 원하는 URL 에서만 Graha 문서화 도구 (GrahaDocTools.js)를 적용할 수 있다.
```
			"matches": ["*://*.graha.kr/*"],
```

> manifest.json 파일은 https://github.com/logicielkr/client_lib/tree/master/doctools/0.5.0.0/manifest.json 에서 다운로드 할 수도 있다.

2. 다음과 같이 initForFirefoxExtension.js 를 만든다.

```javascript
GrahaDocTools.addMenuForFirefoxExtension();
```

> initForFirefoxExtension.js 파일은 https://github.com/logicielkr/client_lib/tree/master/doctools/0.5.0.0/initForFirefoxExtension.js 에서 다운로드 할 수도 있다.

3. "jquery-1.12.4.js", "jquery-ui.js", "GrahaDocTools.js" 를 다운로드 한다.

	- "jquery-1.12.4.js" : https://code.jquery.com/jquery-1.12.4.js
	- "jquery-ui.js" : "https://code.jquery.com/ui/1.12.1/jquery-ui.js"
	- "GrahaDocTools.js" : https://github.com/logicielkr/client_lib/tree/master/doctools/0.5.0.0/GrahaDocTools.js
<!--
	- "GrahaDocTools.js" : https://graha.kr/static-contents/client_lib/doctools/lastest/GrahaDocTools.js
-->

4. 임시 확장 기능을 추가한다.

	> "manifest.json", "initForFirefoxExtension.js", "jquery-1.12.4.js", "jquery-ui.js", "GrahaDocTools.js" 는 같은 디렉토리에 있어야 한다.

	- "도구 > 브라우저 도구 > 원격 디버깅" 으로 들어간다(주소창에 "about:debugging" 를 입력해도 된다).
	- 왼쪽 메뉴 중 "이 Firefox" 를 클릭하여 "디버깅 - 런타임 / this-firefox" 페이지로 들어간다.
	- "임시 부가 기능 로드" 를 클릭하여 방금 전에 만들었던 "manifest.json" 파일을 선택한다. 

5. 다른 웹페이지, 예를 들어 [graha.kr](https://graha.kr) 를 열어서 다음과 같은 단축키를 사용해 본다.

   - Alt + l : (Left) 왼쪽 상단에 번호가 있는 박스를 만든다.
   - Alt + r : (Right) 오른쪽 상단에 번호가 있는 박스를 만든다.
   - Alt + u : (Undo) 최근에 만든 박스 1개를 지운다.
   - Alt + c : (Clear) 모든 박스를 지운다.
   
   > 음각 원문자가 ❶ 부터 ⓴ 까지 밖에 없기 때문에, 박스는 20개까지만 추가할 수 있으나, GrahaDocTools.numberString 에 적당한 원문자를 추가해서 20개보다 갯수를 늘릴 수 있다.
   
   > GrahaDocTools.fontColor 를 미리 정의하거나 변경하면, 박스 안에 원문자의 글자 색상을 변경 할 수 있다.
   
   > GrahaDocTools.boxBorderStyle 를 미리 정의하거나 변경하면, 박스의 테두리(Border) 모양 및 색상을 변경할 수 있다.

6. 만들어진 박스는 마우스로 크기를 조정하거나, 위치를 이동할 수 있다.

7. 화면캡처를 할 수 있는 다른 확장기능으로 화면을 캡처한다.

