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

package de.decidr.model.workflowmodel.factories;

import java.io.IOException;
import java.io.InputStream;

import javax.wsdl.Definition;
import javax.wsdl.WSDLException;

import de.decidr.model.workflowmodel.dwdl.transformation.TransformUtil;

/**
 * Reads the HumanTask WSDL and the Email WSDL and returns {@link Definiton}
 * objects
 * 
 * @author Modood Alvi
 */
public class WSDLFactory {

    static String humanTaskWSDLName = "/test/HumanTaskTest.wsdl";
    static String emailWSDLName = "/test/EmailTest.wsdl";

    public static Definition getHumanTaskDefintion() throws WSDLException,
            IOException {
        return TransformUtil.bytesToDefinition(getHumanTaskDefinitionByteArray());

    }

    public static Definition getEmailDefinition() throws WSDLException,
            IOException {
        return TransformUtil.bytesToDefinition(getEmailDefinitionByteArray());
    }

    public static byte[] getHumanTaskDefinitionByteArray() throws IOException,
            WSDLException {
        InputStream in = WSDLFactory.class
                .getResourceAsStream(humanTaskWSDLName);
        byte[] data = new byte[in.available()];
        in.read(data, 0, in.available());

        return data;
    }

    public static byte[] getEmailDefinitionByteArray() throws IOException,
            WSDLException {
        InputStream in = WSDLFactory.class
                .getResourceAsStream(emailWSDLName);
        byte[] data = new byte[in.available()];
        in.read(data, 0, in.available());

        return data;
    }

}
