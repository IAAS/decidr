#! /bin/bash

cd "$(dirname "$(readlink -e "${0}")")"

function compileUploadService()
{(
  cd ../"$1"* && \
  mvn clean package && \
  scp target/"$1"-0.0.*-*.jar root@ec2-174-129-25-57.compute-1.amazonaws.com:/opt/tomcat5.5/webapps/axis2/WEB-INF/servicejars/
)}

(
  cd ../model && \
  mvn clean install -Dmysql_host=localhost -Dmysql_root=root -Dmysql_rtpwd=rtpwd -Dmaven.test.skip=true && \
  scp target/model-0.0.*-*.jar root@ec2-174-129-25-57.compute-1.amazonaws.com:/opt/tomcat5.5/webapps/axis2/WEB-INF/decidrjars/
) || exit 1

ssh -x root@ec2-174-129-25-57.compute-1.amazonaws.com /etc/init.d/tomcat stop || exit 2

compileUploadService Email
compileUploadService HumanTask
#compileUploadService ODEMonitorService

ssh -x root@ec2-174-129-25-57.compute-1.amazonaws.com /etc/init.d/tomcat start
