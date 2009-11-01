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

package de.decidr.model.workflowmodel.dwdl.transformation;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import javax.wsdl.xml.WSDLReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.xml.sax.InputSource;

import de.decidr.model.logging.DefaultLogger;
import de.decidr.model.workflowmodel.dwdl.Workflow;
import de.decidr.model.workflowmodel.humantask.THumanTaskData;
import de.decidr.model.workflowmodel.webservices.WebserviceMapping;
import de.decidr.model.workflowmodel.wsc.TConfiguration;

/**
 * A simple utility class for standard transformations
 * 
 * @author Modood Alvi
 * @version 0.1
 */
public class TransformUtil {

    private static JAXBContext dwdlCntxt = null;
    private static JAXBContext wscCntxt = null;
    private static JAXBContext mappingCntxt = null;
    private static JAXBContext htaskCntxt = null;
    private static Logger log = DefaultLogger.getLogger(TransformUtil.class);

    static {
        try {
            dwdlCntxt = JAXBContext.newInstance(Workflow.class);
            wscCntxt = JAXBContext.newInstance(TConfiguration.class);
            mappingCntxt = JAXBContext.newInstance(WebserviceMapping.class);
            htaskCntxt = JAXBContext.newInstance(THumanTaskData.class);
        } catch (JAXBException e) {
            log.error("Couldn't create JAXBContext", e);
        }
    }

    public static Workflow bytesToWorkflow(byte[] dwdl) throws JAXBException {
        Unmarshaller unmarshaller = dwdlCntxt.createUnmarshaller();
        JAXBElement<Workflow> dwdlElement = unmarshaller.unmarshal(
                new StreamSource(new ByteArrayInputStream(dwdl)),
                Workflow.class);
        return dwdlElement.getValue();
    }

    public static THumanTaskData bytesToHumanTask(byte[] humanTaskData)
            throws JAXBException {
        Unmarshaller unmarshaller = htaskCntxt.createUnmarshaller();
        JAXBElement<THumanTaskData> taskElement = unmarshaller.unmarshal(
                new StreamSource(new ByteArrayInputStream(humanTaskData)),
                THumanTaskData.class);
        return taskElement.getValue();
    }

    public static WebserviceMapping bytesToMapping(byte[] mapping)
            throws JAXBException {
        Unmarshaller unmarshaller = mappingCntxt.createUnmarshaller();
        JAXBElement<WebserviceMapping> dwdlElement = unmarshaller
                .unmarshal(new StreamSource(new ByteArrayInputStream(mapping)),
                        WebserviceMapping.class);
        return dwdlElement.getValue();
    }

    public static TConfiguration bytesToConfiguration(byte[] wsc)
            throws JAXBException {
        Unmarshaller wscUnmarshaller = wscCntxt.createUnmarshaller();
        JAXBElement<TConfiguration> dwdlElement = wscUnmarshaller.unmarshal(
                new StreamSource(new ByteArrayInputStream(wsc)),
                TConfiguration.class);
        return dwdlElement.getValue();
    }
    
    public static Definition bytes2Definition(byte[] wsdl) throws WSDLException {
        WSDLReader reader = new com.ibm.wsdl.xml.WSDLReaderImpl();
        InputSource in = new InputSource(new ByteArrayInputStream(wsdl));
        Definition def = reader.readWSDL(null, in);
        return def;
    }

    public static byte[] workflowToBytes(Workflow dwdl) throws JAXBException {

        Marshaller dwdlMarshaller = dwdlCntxt.createMarshaller();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        dwdlMarshaller.marshal(dwdl, os);

        return os.toByteArray();
    }

    public static byte[] configurationToBytes(TConfiguration con)
            throws JAXBException {
        Marshaller dwdlMarshaller = wscCntxt.createMarshaller();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        dwdlMarshaller.marshal(con, os);

        return os.toByteArray();
    }

    public static String element2XML(org.jdom.Element roleElement) {
        
        return null;
    }

}
