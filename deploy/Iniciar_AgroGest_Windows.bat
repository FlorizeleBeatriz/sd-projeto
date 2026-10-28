@echo off
rem Ajuste os dois caminhos abaixo ao seu PC.
set "JAVA_HOME=C:\Program Files\Java\jdk-21"
set "TOMCAT=C:\tomcat"
start "" "http://localhost:8080/AgroGest/"
cd /d "%TOMCAT%\bin"
call catalina.bat run
