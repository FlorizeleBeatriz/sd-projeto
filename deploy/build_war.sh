#!/bin/bash
# Alternativa ao "mvn clean package" (apenas JDK 17+ e o servlet-api.jar do Tomcat): gera target/AgroGest.war
set -e
cd "$(dirname "$0")/.."
SERVLET_API=${SERVLET_API:-/opt/tomcat/lib/servlet-api.jar}
rm -rf target && mkdir -p target/war/WEB-INF/classes
javac --release 17 -encoding UTF-8 -d target/war/WEB-INF/classes -cp "$SERVLET_API" $(find src/main/java -name '*.java')
cp -r src/main/webapp/. target/war/
(cd target/war && jar cf ../AgroGest.war .)
echo "WAR gerado em target/AgroGest.war"
