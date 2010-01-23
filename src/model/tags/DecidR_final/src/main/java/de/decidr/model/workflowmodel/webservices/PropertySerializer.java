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

package de.decidr.model.workflowmodel.webservices;

import java.io.PrintWriter;
import java.io.Serializable;

import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.ExtensionDeserializer;
import javax.wsdl.extensions.ExtensionRegistry;
import javax.wsdl.extensions.ExtensionSerializer;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import de.decidr.model.logging.DefaultLogger;
import de.decidr.model.workflowmodel.bpel.varprop.Property;
import de.decidr.model.workflowmodel.dwdl.transformation.Constants;

/**
 * A custom serializer to read and easily handle properties and property alias
 * in {@link Definition} This serializer wraps the JAXB marshaller and
 * unmarshaller created for properties.
 * 
 * @author Modood Alvi
 */
public class PropertySerializer implements ExtensionDeserializer,
        ExtensionSerializer, Serializable {

    private static final long serialVersionUID = 1L;

    private static Logger log = DefaultLogger
            .getLogger(PropertySerializer.class);

    /*
     * (non-Javadoc)
     * 
     * @see javax.wsdl.extensions.ExtensionSerializer#marshall(java.lang.Class,
     * javax.xml.namespace.QName, javax.wsdl.extensions.ExtensibilityElement,
     * java.io.PrintWriter, javax.wsdl.Definition,
     * javax.wsdl.extensions.ExtensionRegistry)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void marshall(Class parentType, QName elementType,
            ExtensibilityElement extension, PrintWriter pw, Definition def,
            ExtensionRegistry extReg) throws WSDLException {
        try {
            Property property = (Property) extension;
            Marshaller m = JAXBContext.newInstance(Property.class)
                    .createMarshaller();
            JAXBElement<Property> jaxbElement = new JAXBElement<Property>(
                    new QName(Constants.VARPROP_NAMESPACE, "property"),
                    Property.class, property);
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            m.marshal(jaxbElement, pw);
            pw.println();
        } catch (JAXBException e) {
            log.error("Can't marshall the property element in "
                    + def.getTargetNamespace(), e);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.wsdl.extensions.ExtensionDeserializer#unmarshall(java.lang.Class,
     * javax.xml.namespace.QName, org.w3c.dom.Element, javax.wsdl.Definition,
     * javax.wsdl.extensions.ExtensionRegistry)
     */
    @SuppressWarnings("unchecked")
    @Override
    public ExtensibilityElement unmarshall(Class parentType, QName elementType,
            Element el, Definition def, ExtensionRegistry extReg)
            throws WSDLException {
        Property extProperty = (Property) extReg.createExtension(parentType,
                elementType);
        Property tmpProperty = null;
        try {
            Unmarshaller u = JAXBContext.newInstance(Property.class)
                    .createUnmarshaller();
            JAXBElement<Property> jaxbElement = u.unmarshal(el, Property.class);
            tmpProperty = jaxbElement.getValue();
        } catch (JAXBException e) {
            log.error("Can't unmarshall property element in "
                    + def.getTargetNamespace(), e);
        }
        if (tmpProperty != null) {
            if (tmpProperty.isSetName()) {
                extProperty.setName(tmpProperty.getName());
            }
            if (tmpProperty.isSetElement()) {
                extProperty.setElement(tmpProperty.getElement());
            }
            if (tmpProperty.isSetType()) {
                extProperty.setType(tmpProperty.getType());
            }
            if (tmpProperty.isSetAny()) {
                extProperty.getAny().addAll(tmpProperty.getAny());
            }
            if (tmpProperty.isSetDocumentation()) {
                extProperty.getAny().addAll(tmpProperty.getDocumentation());
            }
        } else {
            log.warn("Property is null in " + def.getTargetNamespace());
        }
        return extProperty;
    }

}
