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

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import org.apache.log4j.Logger;

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

    public static String getActorXML(Actor actor) {
        String stringActor = null;
        try {
            StringWriter s = new StringWriter();
            Marshaller m = JAXBContext
                    .newInstance("de.decidr.model.soap.types")
                    .createMarshaller();
            JAXBElement<Actor> element = new JAXBElement<Actor>(new QName(
                    Constants.DECIDRTYPES_NAMESPACE, "actor"), Actor.class, actor);
            m.marshal(element, s);
            stringActor =  s.toString();
        } catch (JAXBException e) {
            log.error("Can't marshall " + actor, e);
        }
        return stringActor;
    }
    
    public static String getRoleXML(Role role){
        String stringRole = null;
        try {
            StringWriter s = new StringWriter();
            Marshaller m = JAXBContext
                    .newInstance("de.decidr.model.soap.types")
                    .createMarshaller();
            JAXBElement<Role> element = new JAXBElement<Role>(new QName(
                    Constants.DECIDRTYPES_NAMESPACE, "actor"), Role.class, role);
            m.marshal(element, s);
            stringRole =  s.toString();
        } catch (JAXBException e) {
            log.error("Can't marshall " + role, e);
        }
        return stringRole;
    }

}
