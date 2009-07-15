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
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.ws.Holder;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * The web service part of the ODE monitor. Interacts with the database and
 * makes decisions on which server gets locked.
 */
// RR: how to maintain the average & stuff?
// RR: what if a client stops updating?
@WebService(name = "ODEMonitorPT", targetNamespace = ODEMonitorService.TARGET_NAMESPACE, wsdlLocation = "ODEMonitor.wsdl")
@XmlSeeAlso( { ObjectFactory.class })
public interface ODEMonitorService {

    public static final String TARGET_NAMESPACE = "http://decidr.de/webservices/ODEMonitor";
    public static final String SERVICE_NAME = "ODEMonitor";
    public static final QName SERVICE = new QName(
            "http://decidr.de/webservices/ODEMonitor", SERVICE_NAME);
    public static final QName ENDPOINT = new QName(TARGET_NAMESPACE,
            "ODEMonitorSOAP");

    /**
     * Called to update a client's statistical data.
     * 
     * @param wfInstances
     *            The amount of workflow instances running on the client's ODE.
     * @param wfModels
     *            The amount of workflow models deployed on the client's ODE.
     * @param cpuUsage
     *            The CPU usage percentage on the client's machine
     * @param memUsage
     *            The memory usage percentage on the client's machine
     * @param odeID
     *            The ID of the updating ODE instance/monitor.
     * @param configVersion
     *            The timestamp of the last config change.
     * @param run
     *            Whether the client should consider shutting its instance down
     *            or re-starting it.
     */
    @WebMethod(action = "http://decidr.de/webservices/ODEMonitor/updateStats")
    @RequestWrapper(localName = "updateStats", targetNamespace = "http://decidr.de/webservices/ODEMonitor", className = "de.decidr.webservices.odemonitor.UpdateStats")
    @ResponseWrapper(localName = "updateStatsResponse", targetNamespace = "http://decidr.de/webservices/ODEMonitor", className = "de.decidr.webservices.odemonitor.UpdateStatsResponse")
    public void updateStats(
            @WebParam(name = "wfInstances", targetNamespace = "") int wfInstances,
            @WebParam(name = "wfModels", targetNamespace = "") int wfModels,
            @WebParam(name = "cpuUsage", targetNamespace = "") int cpuUsage,
            @WebParam(name = "memUsage", targetNamespace = "") int memUsage,
            @WebParam(name = "odeID", targetNamespace = "") String odeID,
            @WebParam(name = "configVersion", targetNamespace = "", mode = WebParam.Mode.OUT) Holder<XMLGregorianCalendar> configVersion,
            @WebParam(name = "run", targetNamespace = "", mode = WebParam.Mode.OUT) Holder<Boolean> run);

    /**
     * Called to register a new ODE instance/monitor.
     * 
     * @param poolInstance
     *            Whether the ODE instance was declared a pool instance.
     * @param odeID
     *            The ID of the registered ODE instance/monitor.
     */
    @WebMethod(action = "http://decidr.de/webservices/ODEMonitor/registerODE")
    @RequestWrapper(localName = "registerODE", targetNamespace = "http://decidr.de/webservices/ODEMonitor", className = "de.decidr.webservices.odemonitor.RegisterODE")
    @ResponseWrapper(localName = "registerODEResponse", targetNamespace = "http://decidr.de/webservices/ODEMonitor", className = "de.decidr.webservices.odemonitor.RegisterODEResponse")
    public void registerODE(
            @WebParam(name = "poolInstance", targetNamespace = "", mode = WebParam.Mode.OUT) Holder<Boolean> poolInstance,
            @WebParam(name = "odeID", targetNamespace = "", mode = WebParam.Mode.OUT) Holder<String> odeID);

    /**
     * Returns the current configuration.
     * 
     * @param configChanged
     *            The timestamp of the last config change.
     * @param updateInterval
     *            The interval (in seconds) between updates to the client's
     *            status.
     */
    @WebMethod(action = "http://decidr.de/webservices/ODEMonitor/getConfig")
    @RequestWrapper(localName = "getConfig", targetNamespace = "http://decidr.de/webservices/ODEMonitor", className = "de.decidr.webservices.odemonitor.GetConfig")
    @ResponseWrapper(localName = "getConfigResponse", targetNamespace = "http://decidr.de/webservices/ODEMonitor", className = "de.decidr.webservices.odemonitor.GetConfigResponse")
    public void getConfig(
            @WebParam(name = "updateInterval", targetNamespace = "", mode = WebParam.Mode.OUT) Holder<Integer> updateInterval,
            @WebParam(name = "configChanged", targetNamespace = "", mode = WebParam.Mode.OUT) Holder<XMLGregorianCalendar> configChanged);
}
