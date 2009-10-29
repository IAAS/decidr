#! /bin/bash

cd "$(dirname "$(readlink -e "${0}")")"

(cd ../model && mvn clean install -Dmysql_host=localhost -Dmysql_root=root -Dmysql_rtpwd=rtpwd -Dmaven.test.skip=true ) && \
mvn clean package && \
ssh -x root@ec2-174-129-25-57.compute-1.amazonaws.com /etc/init.d/tomcat stop && sleep 3 && \
scp target/Email-0.0.*-*.jar root@ec2-174-129-25-57.compute-1.amazonaws.com:/opt/tomcat5.5/webapps/axis2_15/WEB-INF/servicejars/ && \
ssh -x root@ec2-174-129-25-57.compute-1.amazonaws.com /etc/init.d/tomcat start
