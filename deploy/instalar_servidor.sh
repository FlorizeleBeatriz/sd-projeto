#!/bin/bash
# Instalação de referência em Ubuntu/Debian (executar com sudo). Ajustar versão do Tomcat se necessário.
set -e
TOMCAT_VERSION=${TOMCAT_VERSION:-10.1.55}
apt-get update && apt-get install -y openjdk-17-jdk-headless curl
id tomcat >/dev/null 2>&1 || useradd -r -m -U -d /opt/tomcat -s /bin/false tomcat
curl -fsSL "https://archive.apache.org/dist/tomcat/tomcat-10/v${TOMCAT_VERSION}/bin/apache-tomcat-${TOMCAT_VERSION}.tar.gz" | tar xz -C /opt/tomcat --strip-components=1
# Porta HTTP 8081 (a do diagrama de arquitectura do enunciado)
sed -i 's/Connector port="8080"/Connector port="8081"/' /opt/tomcat/conf/server.xml
chown -R tomcat:tomcat /opt/tomcat
install -m 644 "$(dirname "$0")/tomcat.service" /etc/systemd/system/tomcat.service
install -o tomcat -g tomcat -m 644 "$(dirname "$0")/../target/AgroGest.war" /opt/tomcat/webapps/AgroGest.war
systemctl daemon-reload && systemctl enable --now tomcat
echo "Aplicação em http://localhost:8081/AgroGest/   |  logs: journalctl -u tomcat -f   |  dados: /opt/tomcat/agrogest-data.bin"
