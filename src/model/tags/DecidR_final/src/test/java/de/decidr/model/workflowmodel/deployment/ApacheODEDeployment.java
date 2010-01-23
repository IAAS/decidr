/*
 * The DecidR Development Team licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package de.decidr.model.workflowmodel.deployment;

import java.io.InputStream;

import org.apache.axis2.AxisFault;
import org.junit.BeforeClass;
import org.junit.Test;

public class ApacheODEDeployment {

    static DeployerImpl deployer = null;
    static byte[] simpleHelloLocal = null;
    static byte[] simpleHelloRemote = null;
    static String location_remote = null;
    static String location_local = null;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        location_remote = "http://129.69.214.90:8080";
        location_local = "http://localhost:8080";
        deployer = new DeployerImpl();
        InputStream inLocal = ApacheODEDeployment.class
                .getResourceAsStream("/test/HelloWorld2Local.zip");
        simpleHelloLocal = new byte[inLocal.available()];
        inLocal.read(simpleHelloLocal, 0, inLocal.available());
        InputStream inRemote = ApacheODEDeployment.class
                .getResourceAsStream("/test/HelloWorld2Remote.zip");
        simpleHelloRemote = new byte[inRemote.available()];
        inRemote.read(simpleHelloRemote, 0, inRemote.available());
    }

    @Test
    public void testDeployStringByteArrayString() throws AxisFault {
        // deployer.deploy("HelloWorld2", simpleHelloLocal, location_local);
        deployer.deploy("HelloWorld2", simpleHelloRemote, location_remote);
    }

}
