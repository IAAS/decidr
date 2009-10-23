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

import java.net.URISyntaxException;

import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import javax.wsdl.xml.WSDLReader;

import com.ibm.wsdl.xml.WSDLReaderImpl;

/**
 * Reads the HumanTask WSDL and returns a {@link Definiton} object
 *
 * @author Modood Alvi
 */
public class WSDLFactory {

    
    public static Definition getDefintion() throws WSDLException, URISyntaxException{
        String wsdlName = "/HumanTaskTest.wsdl";
        WSDLReader reader = new WSDLReaderImpl();
        return reader.readWSDL(WSDLFactory.class
                .getResource(wsdlName).toURI().toString());
        
    }

}
