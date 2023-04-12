@echo off

set JAVA_HOME=C:/Program Files/Java/jdk-17.0.5
set PATH=%JAVA_HOME%\bin;%PATH%

set MAVEN_HOME=C:/software/Maven/apache-maven-3.8.3
set PATH=%MAVEN_HOME%\bin;%PATH%

call mvn clean package -DskipTests=true

SET MAIN_JAR_NAME=back_door-run.jar

SET iconFilePath=%~dp0%src/main/resources/favicon.ico

SET sourceFolder=%~dp0target\jpackage%

mkdir %sourceFolder%

echo %~dp0target\%MAIN_JAR_NAME%

copy %~dp0target\%MAIN_JAR_NAME%  %sourceFolder%

REM 调用jdeps --print-module-deps --ignore-missing-deps %~dp0target\%MAIN_JAR_NAME%并将结果赋值给modules
for /f %%i in ('call jdeps --print-module-deps --ignore-missing-deps %~dp0target\%MAIN_JAR_NAME%') do set modules=%%i

echo %modules%

jpackage.exe --type app-image -i "%sourceFolder%" -n backdoor --main-jar %MAIN_JAR_NAME% --icon %iconFilePath% --add-modules %modules% -d "%~dp0%target"
