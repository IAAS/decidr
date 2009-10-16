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

package de.decidr.model.workflowmodel.dwdl.translator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import javax.wsdl.xml.WSDLReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import de.decidr.model.logging.DefaultLogger;
import de.decidr.model.workflowmodel.dwdl.Workflow;
import de.decidr.model.workflowmodel.humantask.THumanTaskData;
import de.decidr.model.workflowmodel.wsc.TConfiguration;

/**
 * A simple utility class for standard transformations
 * 
 * @author Modood Alvi
 * @version 0.1
 */
public class TransformUtil {

    private static Document doc = null;
    private static JAXBContext dwdlCntxt = null;
    private static JAXBContext wscCntxt = null;
    private static Logger log = DefaultLogger.getLogger(TransformUtil.class);

    static {
        try {
            dwdlCntxt = JAXBContext.newInstance(Workflow.class);
            wscCntxt = JAXBContext.newInstance(TConfiguration.class);
            DocumentBuilderFactory factory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.newDocument();
        } catch (ParserConfigurationException ex) {
            log.error("Couldn't create org.w3c.dom.Document", ex);
        } catch (JAXBException e) {
            log.error("Couldn't create JAXBContext for DWDL", e);
        }
    }

    /**
     * Marshals the given object to a byte array using the JAXB marshaller.
     * 
     * @param node
     *            object to marshal
     * @return the marshalled XML
     * @throws JAXBException
     *             if the given object is not recognized by JAXB as a valid XML
     *             entity.
     */
    public static byte[] getBytes(Object node) throws JAXBException {
        if (node == null) {
            throw new IllegalArgumentException("Node must not be null.");
        }

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        Class<?> clazz = node.getClass();
        JAXBContext context = JAXBContext.newInstance(clazz);
        Marshaller marshaller = context.createMarshaller();
        marshaller.marshal(node, outStream);
        return outStream.toByteArray();
    }

    /**
     * Unmarshalls an object from XML data stored in a byte array.
     * 
     * @author Daniel Huss
     * @param clazz
     *            the expected class of the unmarshalled object.
     * @param bytes
     *            the data source for unmarshalling
     * @return the unmarshalled object
     * @throws JAXBException
     *             if the given class is not recognized by JAXB as a valid XML
     *             entity.
     */
    @SuppressWarnings("unchecked")
    public static Object getElement(Class<?> clazz, byte[] bytes)
            throws JAXBException {
        if (clazz == null) {
            throw new IllegalArgumentException("Class must not be null.");
        }
        if (bytes == null || bytes.length == 0) {
            throw new IllegalArgumentException(
                    "Source byte array must not be null or empty.");
        }

        ByteArrayInputStream inStream = new ByteArrayInputStream(bytes);
        JAXBContext context = JAXBContext.newInstance(clazz);
        Object result = context.createUnmarshaller().unmarshal(inStream);

        if (clazz.isAssignableFrom(result.getClass())) {
            return result;
        } else if (result instanceof JAXBElement) {
            // this happens if there is no distinct root element for clazz
            return ((JAXBElement) result).getValue();
        } else {
            // Fuck.
            throw new JAXBException(
                    "Unmarshaller unmarshalled garbage without complaining.");
        }

    }

    /**
     * MA FIXME please document all methods :-) ~dh
     * 
     * @param file
     * @return
     * @throws IOException
     */
    public static byte[] fileToBytes(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        // Get the size of the file
        long length = file.length();

        if (length > Integer.MAX_VALUE) {
            // File is too large
        }

        // Create the byte array to hold the data
        byte[] bytes = new byte[(int) length];

        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "
                    + file.getName());
        }

        // Close the input stream and return bytes
        is.close();
        return bytes;
    }

    public static Workflow bytes2Workflow(byte[] dwdl) throws JAXBException {
        Unmarshaller dwdlUnmarshaller = dwdlCntxt.createUnmarshaller();
        JAXBElement<Workflow> dwdlElement = dwdlUnmarshaller.unmarshal(
                new StreamSource(new ByteArrayInputStream(dwdl)),
                Workflow.class);
        return dwdlElement.getValue();
    }

    public static THumanTaskData bytes2HumanTask(byte[] humanTaskData)
            throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(THumanTaskData.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        JAXBElement<THumanTaskData> taskElement = unmarshaller.unmarshal(
                new StreamSource(new ByteArrayInputStream(humanTaskData)),
                THumanTaskData.class);
        return taskElement.getValue();
    }

    public static Document workflow2DOM(Workflow dwdl) throws JAXBException,
            ParserConfigurationException {

        Marshaller dwdlMarshaller = dwdlCntxt.createMarshaller();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        dwdlMarshaller.marshal(dwdl, doc);
        return doc;
    }

    public static byte[] workflow2Bytes(Workflow dwdl) throws JAXBException {

        Marshaller dwdlMarshaller = dwdlCntxt.createMarshaller();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        dwdlMarshaller.marshal(dwdl, os);

        return os.toByteArray();

    }

    public static StreamSource workflow2StreamSource(Workflow dwdl)
            throws JAXBException {
        byte[] dwdlByte = workflow2Bytes(dwdl);
        ByteArrayInputStream in = new ByteArrayInputStream(dwdlByte);
        StreamSource source = new StreamSource(in);
        return source;
    }

    public static Definition bytes2Definition(byte[] wsdl) throws WSDLException {
        WSDLReader reader = new com.ibm.wsdl.xml.WSDLReaderImpl();
        InputSource in = new InputSource(new ByteArrayInputStream(wsdl));
        Definition def = reader.readWSDL(null, in);
        return def;
    }

    public static String element2XML(org.w3c.dom.Node node)
            throws TransformerException {
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer();

        DOMSource source = new DOMSource(node);
        StringWriter sw = new StringWriter();
        StreamResult result = new StreamResult(sw);
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(source, result);

        return sw.toString();
    }

    public static String DOM2XML(org.w3c.dom.Document doc)
            throws TransformerException {
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer();

        DOMSource source = new DOMSource(doc);
        StringWriter sw = new StringWriter();
        StreamResult result = new StreamResult(sw);
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(source, result);

        return sw.toString();
    }

    public static org.w3c.dom.Element createDOMElement(String namespace,
            String qName) {
        return doc.createElementNS(namespace, qName);
    }

    public static org.w3c.dom.Attr createAttributeNode(String namespace,
            String qName) {
        return doc.createAttributeNS(namespace, qName);
    }

    public static byte[] configuration2Bytes(TConfiguration con)
            throws JAXBException {
        Marshaller dwdlMarshaller = wscCntxt.createMarshaller();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        dwdlMarshaller.marshal(con, os);

        return os.toByteArray();
    }

    public static TConfiguration bytes2Configuration(byte[] wsc)
            throws JAXBException {
        Unmarshaller wscUnmarshaller = wscCntxt.createUnmarshaller();
        JAXBElement<TConfiguration> dwdlElement = wscUnmarshaller.unmarshal(
                new StreamSource(new ByteArrayInputStream(wsc)),
                TConfiguration.class);
        return dwdlElement.getValue();
    }

}
