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
    static byte[] simpleHello = null;
    static String location_amazon = null;
    static String location_localhost = null;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        location_amazon = "http://ec2-174-129-24-232.compute-1.amazonaws.com:8080";
        location_localhost = "http://localhost:8080";
        deployer = new DeployerImpl();
        InputStream in = ApacheODEDeployment.class
                .getResourceAsStream("/test/HelloWorld2.zip");
        simpleHello = new byte[in.available()];
        in.read(simpleHello, 0, in.available());
    }

    @Test
    public void testDeployStringByteArrayString() throws AxisFault {
        deployer.deploy("HelloWorld2", simpleHello, location_localhost);
    }

}
