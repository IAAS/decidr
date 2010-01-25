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

import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import de.decidr.model.soap.types.ReducedHumanTaskData;

/**
 * This is a temporary interface for the BEPL callback method web service
 * interface. It will be replaced by whatever the BEPL libraries offer as soon
 * as they do.
 * 
 * @author Reinhold
 */
@WebService(targetNamespace = BasicProcessInterface.TARGET_NAMESPACE)
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.WRAPPED, style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
public interface BasicProcessInterface {

    public static final String SERVICE_NAME_SUFFIX = ".basicProcessSOAP";
    public static final String PORT_SUFFIX = ".basicProcessSOAPSOAP11port_http";
    public static final String TARGET_NAMESPACE = "http://decidr.de/wsdl/basicProcess";
    // XXX use ESB and / or dynamic location
    public static final String SERVICE_LOCATION = "http://localhost:8080/ode/processes/";

    /**
     * The callback method for when a human task is completed.
     * 
     * @param taskID
     *            Information needed to identify the target ODE &amp; workflow
     *            instances.
     * @param dataList
     *            The data produced by the work item.
     */
    @Oneway
    @WebMethod(action = TARGET_NAMESPACE + "/taskCompleted", operationName = "taskCompleted")
    public void taskCompleted(@WebParam(name = "taskID") Long taskID,
            @WebParam(name = "dataList") ReducedHumanTaskData dataList);
}