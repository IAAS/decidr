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

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.decidr.model.logging.DefaultLogger;
import de.decidr.model.soap.types.Actor;
import de.decidr.model.soap.types.Role;

/**
 * This class provides methods to handle DOM and JAXB related transformation
 * used in {@link DWDL2BPEL}
 * 
 * @author Modood Alvi
 */
public class BPELHelper {

    private static Logger log = DefaultLogger.getLogger(BPELHelper.class);
    private static DocumentBuilderFactory builderFactory = null;
    private static DocumentBuilder builder = null;
    private static Document doc = null;

    static {
        builderFactory = DocumentBuilderFactory.newInstance();
        builderFactory.setNamespaceAware(true);
        try {
            builder = builderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            log.error("Can't create DOM document builder", e);
        }
    }

    public static Element getActorEelment(Actor actor) {
        Element returnElement = null;
        try {
            returnElement = marshalToElement(
                    new JAXBElement<de.decidr.model.soap.types.Actor>(
                            new QName(Constants.DECIDRTYPES_NAMESPACE, "actor"),
                            de.decidr.model.soap.types.Actor.class, actor),
                    JAXBContext.newInstance("de.decidr.model.soap.types"));
        } catch (JAXBException e) {
            log.error("JAXBException", e);
        }
        return returnElement;
    }

    public static Element getRoleElement(Role role) {
        Element returnElement = null;
        try {
            returnElement = marshalToElement(
                    new JAXBElement<de.decidr.model.soap.types.Role>(new QName(
                            Constants.DECIDRTYPES_NAMESPACE, "role"),
                            de.decidr.model.soap.types.Role.class, role),
                    JAXBContext.newInstance("de.decidr.model.soap.types"));
        } catch (JAXBException e) {
            log.error("JAXBException", e);
        }
        return returnElement;
    }

    /**
     * Marshals a JAXB element into an org.w3c.Element
     * 
     * @param o
     *            the JAXB element to be converted
     * @param context
     *            the JAXBContext for the element being converted
     * @return an org.w3c.Element object representation of the JAXB element
     */
    @SuppressWarnings("unchecked")
    public static org.w3c.dom.Element marshalToElement(
            javax.xml.bind.JAXBElement o, JAXBContext context) {
        if (context == null) {
            throw new IllegalArgumentException("JAXBContext must not be null");
        }
        Marshaller m = null;
        try {
            m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
        } catch (JAXBException jaxbe) {
            log.error("JAXBException", jaxbe);
        }
        return marshalToElement(o, m);
    }

    /**
     * Marshals a JAXB element into an org.w3c.Element
     * 
     * @return an org.w3c.Element object representation of the JAXB element
     * @param m
     *            the marshaller to use to convert the element
     * @param v
     *            the validator to use in converting the element
     * @param o
     *            the JAXB element to be converted
     * @throws IndivoException
     *             if there is a marshalling error during the conversion
     */
    @SuppressWarnings("unchecked")
    public static org.w3c.dom.Element marshalToElement(
            javax.xml.bind.JAXBElement o, Marshaller m) {
        if (o == null) {
            return null;
        }

        try {
            if (m == null) {
                throw new IllegalArgumentException(
                        "Marshaller must not be null");
            }

            builder = builderFactory.newDocumentBuilder();
            doc = builder.newDocument();

            m.marshal(o, doc);

        } catch (ParserConfigurationException pce) {
            log.error("Parser exception", pce);
        } catch (JAXBException jaxb) {
            log.error("JAXBException", jaxb);
        }
        return (doc == null ? null : doc.getDocumentElement());
    }

}
