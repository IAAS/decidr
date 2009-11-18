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

package de.decidr.odemonitor.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.ws.Holder;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

import de.decidr.model.exceptions.TransactionException;

/**
 * The web service part of the ODE monitor. Interacts with the database and
 * makes decisions on which server gets locked.
 */
@WebService(name = ODEMonitorService.SERVICE_NAME, serviceName = ODEMonitorService.SERVICE_NAME, targetNamespace = ODEMonitorService.TARGET_NAMESPACE, wsdlLocation = "ODEMonitor.wsdl", portName = ODEMonitorService.PORT_NAME)
@XmlSeeAlso( { ObjectFactory.class })
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.WRAPPED, style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
public interface ODEMonitorService {

    public static final String TARGET_NAMESPACE = "http://decidr.de/webservices/ODEMonitor";
    public static final String SERVICE_NAME = "ODEMonitor";
    public static final String PORT_TYPE_NAME = "ODEMonitorPT";
    public static final String PORT_NAME = "ODEMonitorSOAP11Endpoint";
    public static final QName SERVICE = new QName(TARGET_NAMESPACE,
            SERVICE_NAME);
    public static final QName ENDPOINT = new QName(TARGET_NAMESPACE, PORT_NAME);

    /**
     * Returns the current configuration.
     * 
     * @param configChanged
     *            The timestamp of the last config change.
     * @param averagePeriod
     *            The period of time (in seconds) to average the system load
     *            over.
     * @param updateInterval
     *            The interval (in seconds) between updates to the client's
     *            status.
     */
    @WebMethod(action = "http://decidr.de/webservices/ODEMonitor/getConfig")
    @RequestWrapper(localName = "getConfig", targetNamespace = TARGET_NAMESPACE, className = "de.decidr.webservices.odemonitor.GetConfig")
    @ResponseWrapper(localName = "getConfigResponse", targetNamespace = TARGET_NAMESPACE, className = "de.decidr.webservices.odemonitor.GetConfigResponse")
    public void getConfig(
            @WebParam(name = "updateInterval", targetNamespace = "", mode = WebParam.Mode.OUT) Holder<Integer> updateInterval,
            @WebParam(name = "averagePeriod", targetNamespace = "", mode = WebParam.Mode.OUT) Holder<Integer> averagePeriod,
            @WebParam(name = "configChanged", targetNamespace = "", mode = WebParam.Mode.OUT) Holder<XMLGregorianCalendar> configChanged);

    /**
     * Called to register a new ODE instance/monitor.
     * 
     * @param poolInstance
     *            Whether the ODE instance was declared a pool instance.
     * @param odeID
     *            The ID of the registered ODE instance/monitor.
     * @throws TransactionException
     *             May be thrown during the attempt to register the server
     * @throws IllegalArgumentException
     *             Is thrown when the <code>odeID</code> is incorrect.
     */
    @WebMethod(action = "http://decidr.de/webservices/ODEMonitor/registerODE")
    @RequestWrapper(localName = "registerODE", targetNamespace = TARGET_NAMESPACE, className = "de.decidr.webservices.odemonitor.RegisterODE")
    @ResponseWrapper(localName = "registerODEResponse", targetNamespace = TARGET_NAMESPACE, className = "de.decidr.webservices.odemonitor.RegisterODEResponse")
    public void registerODE(
            @WebParam(name = "poolInstance", targetNamespace = "", mode = WebParam.Mode.OUT) Holder<Boolean> poolInstance,
            @WebParam(name = "odeID", targetNamespace = "", mode = WebParam.Mode.IN) long odeID)
            throws TransactionException, IllegalArgumentException;

    /**
     * Called to unregister an ODE instance/monitor.
     * 
     * @param odeID
     *            The ID of the registered ODE instance/monitor.
     * @throws TransactionException
     *             May be thrown during the attempt to remove the server
     */
    @WebMethod(action = "http://decidr.de/webservices/ODEMonitor/unregisterODE")
    @RequestWrapper(localName = "unregisterODE", targetNamespace = TARGET_NAMESPACE, className = "de.decidr.webservices.odemonitor.UnregisterODE")
    @ResponseWrapper(localName = "unregisterODEResponse", targetNamespace = TARGET_NAMESPACE, className = "de.decidr.webservices.odemonitor.UnregisterODEResponse")
    public void unregisterODE(
            @WebParam(name = "odeID", targetNamespace = "") long odeID)
            throws TransactionException;

    /**
     * Called to update a client's statistical data.
     * 
     * @param wfInstances
     *            The amount of workflow instances running on the client's ODE.
     * @param wfModels
     *            The amount of workflow models deployed on the client's ODE.
     * @param avgLoad
     *            The average system load over a configurable period of time.
     * @param odeID
     *            The ID of the updating ODE instance/monitor.
     * @param configVersion
     *            The timestamp of the last config change.
     * @param run
     *            Whether the client should consider shutting its instance down
     *            or re-starting it.
     * @throws TransactionException
     *             thrown during database access, whenever an error occurs.
     */
    @WebMethod(action = "http://decidr.de/webservices/ODEMonitor/updateStats", operationName = "updateStats")
    @RequestWrapper(localName = "updateStats", targetNamespace = TARGET_NAMESPACE, className = "de.decidr.webservices.odemonitor.UpdateStats")
    @ResponseWrapper(localName = "updateStatsResponse", targetNamespace = TARGET_NAMESPACE, className = "de.decidr.webservices.odemonitor.UpdateStatsResponse")
    public void updateStats(
            @WebParam(name = "wfInstances", targetNamespace = "") int wfInstances,
            @WebParam(name = "wfModels", targetNamespace = "") int wfModels,
            @WebParam(name = "avgLoad", targetNamespace = "") int avgLoad,
            @WebParam(name = "odeID", targetNamespace = "") long odeID,
            @WebParam(name = "configVersion", targetNamespace = "", mode = WebParam.Mode.OUT) Holder<XMLGregorianCalendar> configVersion,
            @WebParam(name = "run", targetNamespace = "", mode = WebParam.Mode.OUT) Holder<Boolean> run)
            throws TransactionException;
}
