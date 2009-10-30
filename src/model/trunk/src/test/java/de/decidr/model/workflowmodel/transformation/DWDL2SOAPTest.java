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

package de.decidr.model.workflowmodel.transformation;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import javax.xml.soap.SOAPException;

import de.decidr.model.workflowmodel.dwdl.transformation.DWDL2SOAP;
import de.decidr.model.workflowmodel.factories.WSDLFactory;

/**
 * TODO: add comment
 * 
 * @author AT
 */
public class DWDL2SOAPTest {

    /**
     * TODO: add comment
     * 
     * @param args
     */
    public static void main(String[] args) {
        try {
            Definition definition = WSDLFactory.getHumanTaskDefinition();
            DWDL2SOAP dwdl2Soap = new DWDL2SOAP();
            dwdl2Soap.getSOAP(definition, "HumanTaskPT", "createTask")
                    .writeTo(System.out);

        } catch (WSDLException exception) {
            System.out.println("WSDLException");
        } catch (SOAPException exception) {
            System.out.println("SOAPException");
        } catch (IOException exception) {
            System.out.println("IOException");
        }

    }

}
