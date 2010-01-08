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
import java.io.IOException;

import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import javax.wsdl.extensions.ExtensionRegistry;
import javax.wsdl.xml.WSDLReader;
import javax.wsdl.xml.WSDLWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.output.DOMOutputter;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.ibm.wsdl.xml.WSDLWriterImpl;

import de.decidr.model.logging.DefaultLogger;
import de.decidr.model.workflowmodel.bpel.Process;
import de.decidr.model.workflowmodel.dd.TDeployment;
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

    /**
     * This boolean decides, whether the {@link #bytesToWorkflow(byte[])}
     * validates or not.
     */
    private static final boolean validate = true;

    private static Logger log = DefaultLogger.getLogger(TransformUtil.class);

    private static JAXBContext dwdlCntxt = null;
    private static JAXBContext bpelCntxt = null;
    private static JAXBContext wscCntxt = null;
    private static JAXBContext mappingCntxt = null;
    private static JAXBContext htaskCntxt = null;
    private static JAXBContext ddCntxt = null;

    static {
        try {
            dwdlCntxt = JAXBContext
                    .newInstance("de.decidr.model.workflowmodel.dwdl");
            wscCntxt = JAXBContext
                    .newInstance("de.decidr.model.workflowmodel.wsc");
            mappingCntxt = JAXBContext
                    .newInstance("de.decidr.model.workflowmodel.webservices");
            htaskCntxt = JAXBContext
                    .newInstance("de.decidr.model.workflowmodel.humantask");
            bpelCntxt = JAXBContext
                    .newInstance("de.decidr.model.workflowmodel.bpel");
            ddCntxt = JAXBContext
                    .newInstance("de.decidr.model.workflowmodel.dd");
        } catch (JAXBException e) {
            log.error("Couldn't create JAXBContext", e);
        }
    }

    public static byte[] bpelToBytes(Process bpel) throws JAXBException {
        Marshaller marshaller = bpelCntxt.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        JAXBElement<Process> element = new JAXBElement<Process>(new QName(
                Constants.BPEL_NAMESPACE, "process"), Process.class, bpel);
        marshaller.marshal(element, os);

        return os.toByteArray();
    }

    public static TConfiguration bytesToConfiguration(byte[] wsc)
            throws JAXBException {
        Unmarshaller wscUnmarshaller = wscCntxt.createUnmarshaller();
        JAXBElement<TConfiguration> dwdlElement = wscUnmarshaller.unmarshal(
                new StreamSource(new ByteArrayInputStream(wsc)),
                TConfiguration.class);
        return dwdlElement.getValue();
    }

    public static Definition bytesToDefinition(byte[] wsdl)
            throws WSDLException {
        WSDLReader reader = new com.ibm.wsdl.xml.WSDLReaderImpl();
        reader.setFeature("javax.wsdl.importDocuments", false);
        reader.setFeature("javax.wsdl.verbose", false);
        ExtensionRegistry extensionRegistry = new DecidrExtensionRegistry();
        reader.setExtensionRegistry(extensionRegistry);
        InputSource in = new InputSource(new ByteArrayInputStream(wsdl));
        Definition def = reader.readWSDL(Constants.DOCUMENT_BASE_URI, in);
        return def;
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
        JAXBElement<WebserviceMapping> element = unmarshaller.unmarshal(
                new StreamSource(new ByteArrayInputStream(mapping)),
                WebserviceMapping.class);
        return element.getValue();
    }

    public static Workflow bytesToWorkflow(byte[] dwdl) throws JAXBException,
            SAXException {
        Unmarshaller unmarshaller = dwdlCntxt.createUnmarshaller();

        if (validate) {
            log.debug("creating schema factory");
            SchemaFactory sf = SchemaFactory
                    .newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
            try {
                log.debug("attempting to make unmarshaller validate");
                unmarshaller.setSchema(sf.newSchema(new StreamSource(
                        TransformUtil.class
                                .getResourceAsStream("/dwdl/dwdl.xsd"))));
            } catch (SAXException e) {
                // TODO RR what are we to do about validation?
                log.error("... failed", e);
                throw e;
            }
        }

        log.trace("unmarshalling:\n" + new String(dwdl));
        JAXBElement<Workflow> dwdlElement = unmarshaller.unmarshal(
                new StreamSource(new ByteArrayInputStream(dwdl)),
                Workflow.class);
        log.debug("returning Workflow object");
        return dwdlElement.getValue();
    }

    public static byte[] configurationToBytes(TConfiguration conf)
            throws JAXBException {
        Marshaller marshaller = wscCntxt.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        JAXBElement<TConfiguration> element = new JAXBElement<TConfiguration>(
                new QName(Constants.CONFIGURATION_NAMESPACE, "configurations"),
                TConfiguration.class, conf);
        marshaller.marshal(element, os);

        return os.toByteArray();
    }

    public static byte[] ddToBytes(TDeployment deployment) throws JAXBException {
        Marshaller marshaller = ddCntxt.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        JAXBElement<TDeployment> element = new JAXBElement<TDeployment>(
                new QName(Constants.DD_NAMESPACE, "deploy"), TDeployment.class,
                deployment);
        marshaller.marshal(element, os);

        return os.toByteArray();
    }

    public static byte[] definitionToBytes(Definition def) throws WSDLException {
        WSDLWriter writer = new WSDLWriterImpl();
        ExtensionRegistry extensionRegistry = new DecidrExtensionRegistry();
        def.setExtensionRegistry(extensionRegistry);
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        writer.writeWSDL(def, os);
        return os.toByteArray();
    }

    public static org.w3c.dom.Element jdomToW3c(org.jdom.Element jdomElement)
            throws JDOMException {
        Document doc = new Document();
        doc.setRootElement(jdomElement);
        DOMOutputter domOut = new DOMOutputter();
        org.w3c.dom.Document w3cDoc = domOut.output(doc);
        org.w3c.dom.Element w3cElment = w3cDoc.getDocumentElement();
        return w3cElment;
    }

    public static byte[] mappingToBytes(WebserviceMapping mapping)
            throws JAXBException {
        Marshaller marshaller = mappingCntxt.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        JAXBElement<WebserviceMapping> mappingElement = new JAXBElement<WebserviceMapping>(
                new QName(Constants.MAPPING_NAMESPACE, "mapping"),
                WebserviceMapping.class, mapping);
        marshaller.marshal(mappingElement, os);

        return os.toByteArray();
    }

    public static byte[] workflowToBytes(Workflow dwdl) throws JAXBException {
        Marshaller marshaller = dwdlCntxt.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        JAXBElement<Workflow> mappingElement = new JAXBElement<Workflow>(
                new QName(Constants.DWDL_NAMESPACE, "workflow"),
                Workflow.class, dwdl);
        marshaller.marshal(mappingElement, os);

        return os.toByteArray();
    }

    public static SOAPMessage bytesToSOAPMessage(byte[] message)
            throws SOAPException {

        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage msg = messageFactory.createMessage();

        SOAPPart soapPart = msg.getSOAPPart();

        StreamSource preppedMsgSrc = new StreamSource(new ByteArrayInputStream(
                message));
        soapPart.setContent(preppedMsgSrc);

        msg.saveChanges();

        return msg;
    }

    public static byte[] SOAPMessagetoBytes(SOAPMessage msg)
            throws SOAPException, IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        msg.writeTo(out);
        return out.toByteArray();
    }

    /**
     * Marshalls human task data into a byte array.
     * 
     * XXX a generic solution is preferable to one method per XML type. Hard to
     * achieve with the current XML -> POJO mapping. This is copy&paste
     * programming :-(
     * 
     * @author Daniel Huss
     * @param data
     *            human task data to marshal
     * @return marshalled human task
     * @throws JAXBException
     *             if the given human task data cannot be marshalled
     */
    public static byte[] humanTaskToByte(THumanTaskData data)
            throws JAXBException {
        Marshaller marshaller = htaskCntxt.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        JAXBElement<THumanTaskData> mappingElement = new JAXBElement<THumanTaskData>(
                new QName(Constants.HUMANTASK_NAMESPACE, "humanTaskData"),
                THumanTaskData.class, data);
        marshaller.marshal(mappingElement, os);

        return os.toByteArray();
    }

}
