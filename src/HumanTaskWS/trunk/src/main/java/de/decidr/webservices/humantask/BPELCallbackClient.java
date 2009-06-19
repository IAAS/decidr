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
package de.decidr.webservices.humantask;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;

/**
 * A web service client for the callback method provided by the BPEL process.
 * 
 * @author Reinhold
 */
// RR: complete as soon as known
@WebServiceClient(name = "BPELCallbackClient", targetNamespace = "http://decidr.de/TODO", wsdlLocation = "TODO")
public class BPELCallbackClient extends Service {

    /**
     * TODO: add comment
     * 
     * @param wsdlDocumentLocation
     * @param serviceName
     */
    protected BPELCallbackClient(URL wsdlDocumentLocation, QName serviceName) {
        super(wsdlDocumentLocation, serviceName);
    }

    /**
     * TODO: add comment
     */
    public BPELCallbackClient(URL wsdlLocation) {
        // RR: add namespace
        // RR: add correct web service name
        this(wsdlLocation, new QName("http://decidr.de/TODO",
                "BPELCallbackInterface"));
    }

    /**
     * TODO: add comment
     * 
     * @return
     */
    // RR: add namespace
    // RR: add correct web service name
    @WebEndpoint(name = "BPELCallbackInterfaceHttpSoap12Endpoint")
    public BPELCallbackInterface getBPELCallbackInterfacePort() {
        return super.getPort(new QName("http://decidr.de/TODO",
                "BPELCallbackInterfaceHttpSoap12Endpoint"),
                BPELCallbackInterface.class);
    }

    /**
     * TODO: add comment
     * 
     * @param features
     * @return
     */
    // RR: add namespace
    // RR: add correct web service name
    @WebEndpoint(name = "BPELCallbackInterfaceHttpSoap12Endpoint")
    public BPELCallbackInterface getBPELCallbackInterfacePort(
            WebServiceFeature... features) {
        return super.getPort(new QName("http://decidr.de/TODO",
                "BPELCallbackInterfaceHttpSoap12Endpoint"),
                BPELCallbackInterface.class, features);
    }
}
