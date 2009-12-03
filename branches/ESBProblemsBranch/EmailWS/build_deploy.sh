#! /bin/bash

# The DecidR Development Team licenses this file to you under
# the Apache License, Version 2.0 (the "License"); you may
# not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied. See the License for the
# specific language governing permissions and limitations
# under the License.

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
