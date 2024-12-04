# Office Document to PDF using JODConverter

## 1. about

### 1.1. 소개 

JODConverter 를 이용해서
Web 프로그램이나 명령행 프로그램으로
Office Document 나 Microsoft 문서 파일을
PDF 로 변환한다.

### 1.2. 다운로드

https://github.com/logicielkr/misc/tree/master/Java_Source_Code/Office_Document_to_PDF_using_JODConverter

### 1.3. 호환성

다음의 환경에서 테스트했다.

- JODConverter 3.0-beta-4 with Java 7 and OpenOffice 4.1.14 on CentOs 5.8
- JODConverter 4.4.6 with Java 8 and LibreOffice 7.5.4 on Ubuntu 20
- Apache Tomcat 7.x 이상 (8.x 이상을 권장)

### 1.4. 의존성

## 2. 사용법

### 2.1. OpenOffice 혹은 LibreOffice 설치

OpenOffice 4.1.15 over 혹은 LibreOffice 7.5.4 over 를 설치한다.

> 파일은 ```/opt/openoffice4``` 혹은 ```/opt/libreoffice24.8``` 같은 곳에 설치되는데, 이미 설치된 파일을 복사해도 특별한 문제가 발생하지 않는다.

#### 2.1.1.	OpenOffice 4.1.14 설치

RPM 설치방법은 다음과 같다(CentOS 5.8 에서 실행).

```bash
tar xvfz Apache_OpenOffice_4.1.15_Linux_x86-64_install-rpm_ko.tar.gz
cd ko/RPMS
rpm -Uvh *.rpm
```

DEB 설치방법은 다음과 같다(Ubuntu 20.04.6 에서 실행).

```bash
tar xvfz Apache_OpenOffice_4.1.15_Linux_x86-64_install-deb_ko.tar.gz
cd ko/DEBS
sudo dpkg -i *.deb
```

```/opt/openoffice4/program/soffice``` 명령을 실행할 때, 다음과 같은 에러가 발생할 수 있지만 이는  무시하는 것으로 한다.

```
no suitable windowing system found, exiting.
```

<!--
설치해제는 다음과 같은데, 이미 openoffice 의 다른 버전이 시스템에 설치되어 있었다면 주의를 요한다.

```bash
sudo apt-get remove --purge "openoffice*"
sudo apt-get clean
sudo apt-get autoremove
```
-->

#### 2.1.2.	LibreOffice 7.5.4 over 설치

DEB 설치방법은 다음과 같다(Ubuntu 20.04.6 에서 실행).

```bash
tar xvfz LibreOffice_7.5.4_Linux_x86-64_deb.tar.gz 
cd LibreOffice_7.5.4.2_Linux_x86-64_deb/DEBS/
sudo dpkg -i *.deb
```

LibreOffice 24.8.3 는 다음과 같다.

```bash
tar xvfz LibreOffice_24.8.3_Linux_x86-64_deb.tar.gz
cd LibreOffice_24.8.3.2_Linux_x86-64_deb/DEBS/
sudo dpkg -i *.deb
```

<!--
설치해제는 다음과 같은데, 이미 libreoffice 의 다른 버전이 시스템에 설치되어 있었다면 주의를 요한다.

```bash
sudo apt-get remove --purge "libreoffice*"
sudo apt-get remove --purge "libobasis*"
sudo apt-get clean
sudo apt-get autoremove
```
-->

#### 2.1.3.	한글 폰트 설치

> Ubuntu 19 혹은 20을 기준으로 설명한다.

##### 2.1.3.1.	패키지로 설치하는 방법

다음과 같이 설치할 수 있는 한글 폰트를 확인한다.

```
$ apt search "Korean fonts"

$ apt search "Korean Truetype fonts"
```

Ubuntu 19 혹은 20 이라면, 대략 다음과 같은 3개의 폰트가 검색된다.

- fonts-baekmuk
- fonts-nanum / fonts-nanum-extra
- fonts-unfonts-core / fonts-unfonts-extra

> Ubuntu Desktop 에서 사용되는 Unicode 폰트들은 "Korean" 으로 검색되지 않는데, 잘 하면 많은 폰트를 찾을 수도 있을 것이다. 

나눔 폰트를 설치한다면, 다음과 같다.

```
$ apt-get install fonts-nanum fonts-nanum-extra
```

##### 2.1.3.2.	폰트 파일을 다운로드 받아 설치하는 방법

먼저, 폰트 파일이 위치할 수 있는 디렉토리를 확인한다.

```
$ sudo vi /etc/fonts/fonts.conf
```

dir 로 검색해서 찾으면 되고, Ubuntu 20 를 기준으로 하면 대략 다음과 같다. 

```xml
<dir>/usr/share/fonts</dir>
<dir>/usr/local/share/fonts</dir>
<dir prefix="xdg">fonts</dir>
<dir>~/.fonts</dir>
```

위의 설정을 기준으로 ```/usr/share/fonts``` 이나 ```/usr/local/share/fonts``` 디렉토리가 적당해 보이는데,
저 디렉토리 아래로 한글 폰트 파일이 들어 있는 디렉토리를 복사하거나 심볼릭 링크 (Symbolic link) 로 걸면 된다.

한글 폰트 파일이 들어 있는 디렉토리를 ```/etc/fonts/fonts.conf``` 파일에 추가하는 방법도 있을 수 있다.

<!--
/opt/openoffice4/share/fonts/truetype
/opt/libreoffice24.8/share/fonts/truetype
-->

fontconfig 가 설치되어 있다면 다음과 같이 설치된 한글 폰트를 확인할 수 있다.

```
$ fc-cache -f -v

$ fc-list :lang=ko
```

#### 2.1.4.	OpenOffice vs LibreOffice

LibreOffice 의 장점은 모두가 잘 알고 있는 바와 같고,
OpenOffice 의 장점은 다음과 같다.

- (이 글을 쓰는 시점에서 최신의 안정버전인) OpenOffice 4.1.15 는 더 오래된 Linux 에서도 동작하고(최소 요구사항이 CentOs 5.8 = Linux kernel version 2.6 or higher, glibc2 version 2.5 or higher 와 같다), 용량도 작다(LibreOffice 24.8. : 1.1G, Openoffice 4. : 386M).
- (반대의 경우도 있겠지만) Microsoft Excel 수식 중에는 OpenOffice 에서만 정상인 것도 있었다.

OpenOffice 는 LibreOffice 와 다르게 
soffice 명령어에 
"--convert-to" 를 붙일 수 없고,
```--headless``` 대신 ```-headless``` 이다.

상세한 사항은 다음을 참조한다.

```bash
/opt/openoffice4/program/soffice -h
```

```/opt/openoffice4/program/soffice``` 명령으로는 pdf 변환이 지원되지 않는 것 같고,
"--convert-to" 대신
JODConverter 3.0-beta-4 다운로드 받아서 사용해야 하는 것으로 보인다.

> 이 프로그램을 사용한다면 명령행에서 odt 파일 따위를 pdf 파일로 변환한 수 있다.

<!--
LibreOffice 는 다음과 같이 docx 등을 pdf 로 변환할 수 있다(hwp, hwpx 는 확장기능 같은 걸 설치하지 않는 한 안된다).

```bash
$ /opt/libreoffice24.8/program/soffice --headless --convert-to pdf:writer_pdf_Export sample.docx
```
-->

### 2.2. JODConverter 다운로드 및 설치

Java 8 이상이면 JODConverter 4.4.6 를 다운로드 받고,
Java 7 이라면 JODConverter 3.0-beta-4 를 다운로드 받는다.

#### 2.2.1.	홈페이지

- GitHub(현재) : https://github.com/jodconverter/jodconverter
- Google Code(과거) : https://code.google.com/archive/p/jodconverter/
- GitHub(과거) : https://github.com/mirkonasato/jodconverter

#### 2.2.2.	JODConverter 4.4.6

Apache License, Version 2.0 조건하에서 배포된다.

- jodconverter-core-4.4.6.jar : [maven 저장소](https://search.maven.org/artifact/org.jodconverter/jodconverter-core/4.4.6/jar)
- jodconverter-local-4.4.6.jar : [maven 저장소](https://search.maven.org/artifact/org.jodconverter/jodconverter-local/4.4.6/jar)

의존성 있는 다른 라이브러리는 다음과 같다.

- gson-2.10.1.jar : [maven 저장소](https://search.maven.org/artifact/com.google.code.gson/gson/2.10.1/jar)
- slf4j-api-1.7.36.jar 이상 (현재 slf4j-api-2.0.7.jar) : [maven 저장소](https://search.maven.org/artifact/org.slf4j/slf4j-api/2.0.7/jar)
- unoil.jar : [maven 저장소](https://search.maven.org/artifact/org.openoffice/unoil/4.1.2/jar) 혹은 ${(OpenOffice | LibreOffice) HOME}/program/classes
- juh.jar : [maven 저장소](https://search.maven.org/artifact/org.openoffice/juh/4.1.2/jar) 혹은 ${(OpenOffice | LibreOffice) HOME}/program/classes

unoil.jar 와 juh.jar 파일은 OpenOffice 혹은 LibreOffice 와 함께 설치되므로, 파일을 복사하거나 classpath 를 추가할 수도 있다.

#### 2.2.3.	JODConverter 3.0-beta-4

GNU Lesser GPL 조건하에서 배포된다.

https://code.google.com/archive/p/jodconverter/downloads

### 2.3. 명령행에서 사용

#### 2.3.1. 의존성 있는 jar 파일들

위에서 이미 설명한 방법으로 다음의 파일을 구해서 lib 디렉토리에 넣고, classpath 를 잡는다.

- jodconverter-core-4.4.6.jar
- jodconverter-local-4.4.6.jar
- gson-2.10.1.jar
- slf4j-api-1.7.36.jar 이상
- unoil.jar
- juh.jar

#### 2.3.2. 실행

```bash
java -classpath \
JODConverterWrapper.0.5.0.0.jar:lib/jodconverter-local-4.4.6.jar:lib/jodconverter-core-4.4.6.jar:lib/gson-2.10.1.jar:lib/slf4j-api-2.0.7.jar:lib/unoil.jar:lib/juh.jar \
kr.graha.app.pdf.JODConverterManager \
${Office Document 파일이름}
```

만약 OpenOffice 혹은 LibreOffice 가 다음과 같은 경로에 설치되어 있지 않은 경우
```-Doffice.home=/opt/openoffice4``` 와 같은 환경변수를 추가해서
OpenOffice 혹은 LibreOffice 가 설치된 경로를 지정한다.

> 우선순위는 ```-Doffice.home=```, 다음의 경로이다. 

- /opt/libreoffice
- /opt/openoffice
- /opt/libreoffice7.5
- /opt/libreoffice24.8
- /opt/openoffice4
- /usr/lib/openoffice
- /usr/lib/libreoffice

```${Office Document 파일이름}``` 은
파일이나 디렉토리일 수도 있다.

만약 디렉토리가 파라미터로 넘어온다면, 그 디렉토리에 있는 모든 파일들에 대해 변환을 시도한다.

디렉토리를 파라미터로 넘긴다면,
그 디렉토리 아래의 모든 파일들에 대해 변환을 시도하기 때문에
그 디렉토리에는 변환할 파일만 있어야 한다.

> OpenOffice 혹은 LibreOffice 를 이용해서 PDF 로 변환할 수 있는 파일이 가질 수 있는 확장자가 생각 외로 많아서 프로그램에서 이를 check 하지 않는다.

### 2.4. Apache Tomcat 에서 사용

#### 2.4.1. WEB-INF/lib 디렉토리에 넣을 jar 파일들

다음 파일을 WEB-INF/lib 디렉토리에 복사하거나 심볼릭 링크 (Symbolic link) 로 걸면 된다.

- JODConverterWrapper.0.5.0.0.jar

다음과 같이 의존성 있는 jar 파일들도 WEB-INF/lib 디렉토리에 복사하거나 심볼릭 링크 (Symbolic link) 로 걸어준다.

- jodconverter-core-4.4.6.jar
- jodconverter-local-4.4.6.jar
- gson-2.10.1.jar
- slf4j-api-1.7.36.jar 이상
- unoil.jar
- juh.jar

#### 2.4.2. web.xml 설정

```xml
<context-param>
	<param-name>office.home</param-name>
	<param-value>/opt/openoffice4</param-value>
</context-param>
<context-param>
	<param-name>office.port</param-name>
	<param-value>2001, 2002, 2003, 2004, 2005, 2006, 2007, 2008</param-value>
</context-param>
<context-param>
	<param-name>office.timeout</param-name>
	<param-value>1800000</param-value>
</context-param>
<listener>
	<listener-class>kr.graha.app.pdf.JODConverterListener</listener-class>
</listener>
```

OpenOffice 혹은 LibreOffice 가 다음과 같은 경로에 설치되어 있는 경우
office.home 파라미터는 생략할 수 있고,
Apache Tomcat 을 실행시킬 때 ```-Doffice.home=/opt/openoffice4``` 와 같은 환경변수를 추가해도 된다.

> 우선순위는 context-param, ```-Doffice.home=```, 다음의 경로이다. 

- /opt/libreoffice
- /opt/openoffice
- /opt/libreoffice7.5
- /opt/libreoffice24.8
- /opt/openoffice4
- /usr/lib/openoffice
- /usr/lib/libreoffice

만약, 같은 경로를 참조하는 ```<Host>``` 설정이 여러 개인 경우 등
kr.graha.app.pdf.JODConverterListener 가 여러 번 실행될 가능성이 있다면
다음과 같은 내용도 추가한다.

```xml
<context-param>
	<param-name>office.virtualservername</param-name>
	<param-value>Catalina/localhost</param-value>
</context-param>
<context-param>
	<param-name>office.contextpath</param-name>
	<param-value>/</param-value>
</context-param>
```

> ```ServletContextEvent.getServletContext().getVirtualServerName()``` 를
> 지원하지 않는 Apache Tomcat 7.x 미만이라면,
> ```kr/graha/app/pdf/JODConverterListener.java``` 소스코드를 수정해야 하고,
> 설정 할 때 단 1번만 실행되도록 주의해야 하는데,
> 동일한 web.xml 을 바라보는 여러 개의 ```<Host>``` 설정이 있으면 안된다.

#### 2.4.3. 응용프로그램에서 호출

응용프로그램에서 다음과 같이 호출한다.

```java
import org.jodconverter.core.office.OfficeException;
import kr.graha.app.pdf.JODConverterManager;

File workingFile = new File("sample.odt");

try {
	JODConverterManager.getInstance().convert(workingFile);
} catch (OfficeException e) {

}
```

변환되는 파일이름은 변환 할 파일이름에서 (마지막 . 뒤의) 확장자를 떼어내고, pdf 를 붙인 형태이다.

```JODConverterManager.getInstance().convert()``` 메소드에 전달하는 파라미터는
File 이나 String 혹은 java.nio.file.Path 일 수도 있고,
파일이나 디렉토리일 수도 있다.

만약 디렉토리가 파라미터로 넘어온다면, 그 디렉토리에 있는 모든 파일들에 대해 변환을 시도한다.

디렉토리를 파라미터로 넘긴다면,
그 디렉토리 아래의 모든 파일들에 대해 변환을 시도하기 때문에
그 디렉토리에는 변환할 파일만 있어야 한다.

> OpenOffice 혹은 LibreOffice 를 이용해서
> PDF 로 변환할 수 있는 파일이 가질 수 있는 확장자가 생각 외로 많아서
> (org.jodconverter.document.DefaultDocumentFormatRegistry 참조)
> 프로그램에서 이를 check 하지 않는다.

기본 값은 하위 디렉토리로 내려가지 않지만,
파라미터로 DirectoryStream 을 넘긴다면,
2 번째 파라미터(recursive) 를 true 로 넘겨서 재귀적으로 변환을 시도하도록 할 수도 있다.

InputStream 과 OutputStream 을 파라미터로 넘길 수도 있고,
이 경우 3 번째 파라미터(closeStream) 을 boolean 값으로 넘길 수 있다.

> 변환되는 파일의 java.nio.file.Path  는 ```JODConverterManager.getPDFOutputFilePath(Path in)``` 로 얻을 수 있다.

### 2.5. 문제해결

#### 2.5.1.	Unsupported major.minor version 52.0

##### 2.5.1.1.		메시지 요약

```
java.lang.UnsupportedClassVersionError: org/jodconverter/core/office/OfficeManager : Unsupported major.minor version 52.0
```

##### 2.5.1.2.		원인

사용중인 Java 버전이 7 혹은 그 이하이다.  JODConverter 4.4.6 에서 발생한다.

JODConverter 4.4.6 라이브러리는 JDK 8 에서 컴파일되었기 때문에 Java 7 에서 사용할 수 없다.

##### 2.5.1.3.		해결방안

다음의 순서로 검토한다.

1. Java 버전을 upgrade 한다.
2. JODConverter 버전을 (3.0-beta-4 로) downgrade 한다.
3. JODConverter 4.4.6 을 JDK 7에서 다시 컴파일한다.

> 마지막 방법은 필자가 해본 적이 없어서 어떤 issue 가 있는지 알지 못한다.

#### 2.5.2.	org/slf4j/LoggerFactory

##### 2.5.2.1.		메시지 요약

```
java.lang.NoClassDefFoundError: org/slf4j/LoggerFactory
```

##### 2.5.2.2.		원인 및 해결방안

classpath 에 slf4j-api-1.7.36.jar 혹은 slf4j-api-2.0.7.jar 를 추가한다.

#### 2.5.3.	com/sun/star/lib/uno/helper/UnoUrl

##### 2.5.3.1.		메시지 요약

```
java.lang.NoClassDefFoundError: com/sun/star/lib/uno/helper/UnoUrl
```

##### 2.5.3.2.		원인 및 해결방안

classpath 에 juh.jar 를 추가한다.

juh.jar 는 /opt/openoffice4/program/classes/ 같은 곳에 있다.

#### 2.5.4.	com/google/gson/Gson

##### 2.5.4.1.		메시지 요약

```
java.lang.NoClassDefFoundError: com/google/gson/Gson
```

##### 2.5.4.2.		원인 및 해결방안

classpath 에 gson-2.10.1.jar 를 추가한다.

#### 2.5.5.	com/sun/star/task/ErrorCodeIOException

##### 2.5.5.1.		메시지 요약

```
java.lang.NoClassDefFoundError: com/sun/star/task/ErrorCodeIOException
```

##### 2.5.5.2.		원인 및 해결방안

classpath 에 unoil.jar 를 추가한다.

juh.jar 는 /opt/openoffice4/program/classes/ 같은 곳에 있다.

#### 2.5.6.	OfficeManager.stop() 에서 발생하는 예외

##### 2.5.6.1.		메시지 요약

```
Exception in thread "MessageDispatcher" java.util.concurrent.RejectedExecutionException: Task org.jodconverter.local.office.LocalOfficeProcessManager$$Lambda$68/0x00000008400c8040@5a831597 rejected from java.util.concurrent.ThreadPoolExecutor@8fb9798[Shutting down, pool size = 1, active threads = 1, queued tasks = 0, completed tasks = 2]
```

##### 2.5.6.2.		원인 및 해결방안

추정컨데,

- maxTasksPerProcess = 1
- convert 를 호출되는 메소드와 다른 synchronized 메소드에서 OfficeManager.stop() 이 호출되었다.

그러면
convert 가 호출되고 나서,
maxTasksPerProcess = 1 이므로 OfficeManager.stop() 가 호출될 것이고,
다시 synchronized 메소드에서 OfficeManager.stop() 가 호출되면,
위와 같은 에러가 발생할 가능성이 높다.

OfficeManager.stop() 이 경합하지 않도록 소스코드를 변경한다.

> maxTasksPerProcess 파라미터를 삭제하는 것을 권한다.

#### 2.5.7.	/opt/openoffice4/program/soffice 명령을 실행할 때 에러

에러메시지가 다음과 같다면, 무시한다.

```
no suitable windowing system found, exiting.
```

다음의 패키지를 설치하면 에러가 발생하지 않겠지만,
```/opt/openoffice4/program/soffice``` 명령을 실행할 일이 없으므로 에러를 무시하기로 한다.

```bash
sudo apt-get install libxt6
sudo apt-get install libxrender1
```

> OpenOffice 에서만 발생하는 것으로 생각된다.

#### 2.5.8.	DocumentConverter.convert 에서 멈춰있는 경우

JODConverter 는 soffice 를 headless 로 실행시킨 다음,
tcp 혹은 unix socket 으로 soffice 프로세스와 통신하면서,
PDF 로의 변환을 수행한다.

```org.jodconverter.local.office.LocalOfficeManager.start()``` 메소드를 호출하면
내부적으로 soffice 를 headless 로 실행시키는데,
이 과정에서 경합이 발생하면(OfficeManager.start() 를 여러 번 호출해서 soffice 프로세스를 여러 번 띄우면),
DocumentConverter.convert 메소드에서 멈춰있게 된다.

<!--
DocumentConverter 에는 isRunning() 메소드가 있지만,
기본적으로 다른 ClassLoader 에 있는 것들을 감지할 수 없다.

같은 ClassLoader 에 있다고 하더라도,
-->

DocumentConverter 에는 isRunning() 메소드가 있지만,
2개의 서로 다른 프로그램에서 OfficeManager.start() 가 호출되는 시차가 작다면,
2번 째 호출에서 isRunning() 메소드가 정확한 결과를 반환한다고 보장할 수는 없다.

> OfficeManager.start() 는 soffice 프로세스를 띄우는 일을 하기 때문에
> 기본적으로 걸리는 시간이 길다.

OfficeManager.start() 가 호출된 이후에
실행과정에서 예외가 발생했고,
예외를 제대로 처리하지 못해서 
OfficeManager.stop() 의 호출이 누락된 경우에도 유사한 일이 발생할 수 있다.

OfficeManager.start() 은 Singleton 으로 1번만 호출되어야 하고,
그것이 성능 측면에서도 유리하기 때문에
ServletContextListener 에서 호출되도록 해야 하고,
이를 위해 작성된 kr.graha.app.pdf.JODConverterListener 를 사용하기로 한다.

> 개별 프로그램에서 OfficeManager.start() 와 OfficeManager.stop() 를 호출하면 사용자가 몇 명만 붙어도 금방 경합이 발생한다.

한편,
ServletContextListener 에서 OfficeManager.start() 를 실행하는 경우,
(여러 Host 의 webapps 경로가 같은 경우와 같이) 여러 Context 가 같은 경로를 사용하는 경우,
ServletContextListener 가 여러 번 실행되면서 경합이 발생한다.

<!--
(각각의 Context 는 독립적인 ClassLoader 를 사용하므로)
-->

이 경우
Singleton 이라고 하더라도 OfficeManager.start() 가 짧은 시차를 두고 호출되면서 경합이 발생하게 되고,
따라서 ServletContextListener 에서
ServletContextEvent.getServletContext().getVirtualServerName() 를 이용해서
경합이 발생하지 않도록 해야 한다.

다만 ServletContext.getVirtualServerName() 은 Servlet 3.1 부터 지원하므로,
Servlet 3.0 = Apache Tomcat 7.x 인 경우,
여러 Host 혹은 Context 를 동일한 경로로 설정하지 않도록 주의해야 한다.

#### 2.5.9.	한글이 모두 깨지는 경우

위에서 설명한 바와 같이 한글 폰트 파일을 설치한다.

## 3. 컴파일 가이드 (Compile Guide)

컴파일은 Apache Ant 를 이용한다.

build.xml 을 수정하지 않고 컴파일을 하기 위해서는 다음과 같은 파일들이 있어야 한다.

- JODConverter 라이브러리

	- /opt/java/lib/jodconverter/jodconverter-core-4.4.6.jar
	- /opt/java/lib/jodconverter/jodconverter-local-4.4.6.jar

- Servlet API

	Apache Tomcat 9 이하는 다음과 같은 파일들이 있어야 한다.

	- /opt/java/lib/apache-tomcat-8.5.100/servlet-api.jar
	- /opt/java/lib/apache-tomcat-8.5.100/catalina.jar
	
	Apache Tomcat 10 이라면 다음과 같은 파일들이 있어야 한다. 
	
	- /opt/java/lib/apache-tomcat-10.0.0-M1/servlet-api.jar
	- /opt/java/lib/apache-tomcat-10.0.0-M1/tomcat-coyote.jar
	- /opt/java/lib/apache-tomcat-10.0.0-M1/catalina.jar
	
- Graha 라이브러리
	
	- /opt/java/lib/graha/version.property
	- /opt/java/lib/graha/graha.x.x.x.x.jar

```/opt/java/lib/graha/version.property``` 파일은 다음과 같은데
graha.x.x.x.x.jar 의 x.x.x.x 와 버전이 같아야 한다.

```
app.graha.version=0.5.1.325
```
