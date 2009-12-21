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

package de.decidr.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBIntrospector;
import javax.xml.bind.Marshaller;

import org.w3c.dom.Element;

import de.decidr.model.workflowmodel.humantask.DWDLSimpleVariableType;
import de.decidr.model.workflowmodel.humantask.THumanTaskData;
import de.decidr.model.workflowmodel.humantask.TTaskItem;
import de.decidr.model.workflowmodel.wsc.TAssignment;
import de.decidr.model.workflowmodel.wsc.TConfiguration;

/**
 * Contains static utility methods for dealing with XML entities.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class XmlTools {

    /**
     * Extracts all file IDs from the given human task data element (which
     * remains unchanged).
     * 
     * @param data
     *            human task data pojo.
     * @return all file IDs found in the given human task data.
     * @throws IllegalArgumentException
     *             if data is <code>null</code>
     * @throws NumberFormatException
     *             if the data contains an unparsable file ID.
     */
    public static Set<Long> getFileIds(THumanTaskData data) {
        if (data == null) {
            throw new IllegalArgumentException(
                    "Human task data must not be null.");
        }
        HashSet<Long> result = new HashSet<Long>();
        for (Object o : data.getTaskItemOrInformation()) {
            if (o instanceof TTaskItem) {
                TTaskItem taskItem = (TTaskItem) o;
                if (DWDLSimpleVariableType.ANY_URI.equals(taskItem.getType())) {
                    try {
                        Object value = taskItem.getValue();
                        String toParse;
                        if (value instanceof Element) {
                            toParse = ((Element) value).getTextContent();
                        } else {
                            toParse = value.toString();
                        }

                        result.add(Long.parseLong(toParse));
                    } catch (NumberFormatException e) {
                        // the value is not a file, which is not a critical
                        // error and perfectly valid if the user has entered
                        // nothing.
                    }
                }
            }
        }
        return result;
    }

    /**
     * Extracts all file IDs from the given start configuration element (which
     * remains unchanged).
     * 
     * @param startConfiguration
     *            start configuration pojo
     * @return all file IDs found in the given human task data.
     * @throws IllegalArgumentException
     *             if startConfiguration is <code>null</code>
     * @throws NumberFormatException
     *             if the start configuration contains an unparsable file ID.
     */
    public static Set<Long> getFileIds(TConfiguration startConfiguration) {
        if (startConfiguration == null) {
            throw new IllegalArgumentException(
                    "Start configuration must not be null.");
        }
        HashSet<Long> result = new HashSet<Long>();
        for (TAssignment assignment : startConfiguration.getAssignment()) {
            if (DWDLSimpleVariableType.ANY_URI.toString().equals(
                    assignment.getValueType())) {
                try {
                    result.add(Long.parseLong(assignment.getValue().get(0)));
                } catch (NumberFormatException e) {
                    // the value is not a file, which is not a critical error
                    // and perfectly valid if the user has entered nothing.
                }
            }
        }
        return result;
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
    public static <T> T getElement(Class<? extends T> clazz, byte[] bytes)
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
            return (T) result;
        } else if (result instanceof JAXBElement) {
            // this happens if there is no distinct root element for clazz
            return (T) ((JAXBElement) result).getValue();
        } else {
            // Fuck. We don't have a clue what unmarshal() returned.
            throw new JAXBException(
                    "Unmarshaller unmarshalled garbage without complaining.");
        }
    }

    /**
     * Marshals the given object to a byte array using the JAXB marshaller.
     * 
     * @param <T>
     *            class of node
     * @param node
     *            object to marshal
     * @param clazz
     *            class of node
     * @return the marshalled XML
     * @throws JAXBException
     *             if the given object is not recognized by JAXB as a valid XML
     *             entity.
     */
    public static <T> byte[] getBytes(T node, Class<T> clazz)
            throws JAXBException {
        if (node == null) {
            throw new IllegalArgumentException("Node must not be null.");
        }

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        JAXBContext context = JAXBContext.newInstance(clazz);
        JAXBIntrospector introspector = context.createJAXBIntrospector();

        if (!introspector.isElement(node)) {
            throw new IllegalArgumentException(
                    "Given node is not recognized by JAXB introspector.");
        }

        JAXBElement<T> element = new JAXBElement<T>(introspector
                .getElementName(node), clazz, node);

        Marshaller marshaller = context.createMarshaller();
        marshaller.marshal(element, outStream);

        return outStream.toByteArray();
    }
}
