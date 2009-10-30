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
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import de.decidr.model.workflowmodel.humantask.DWDLSimpleVariableType;
import de.decidr.model.workflowmodel.humantask.THumanTaskData;
import de.decidr.model.workflowmodel.humantask.TTaskItem;

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
     *            human task data JAXB element.
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
                    result.add(Long.parseLong(taskItem.getValue().toString()));
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

        JAXBContext context = JAXBContext.newInstance(node.getClass());
        Marshaller marshaller = context.createMarshaller();
        JAXBElement<Object> element = new JAXBElement<Object>(new QName("uri",
                "local"), Object.class, node);

        marshaller.marshal(element, outStream);
        return outStream.toByteArray();
    }

}
