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

import org.jdom.Namespace;

/**
 * A class holding schema and namespace information used in the transformation process of
 * {@link DWDL2BPEL}
 * 
 * @author Modood Alvi
 * @version 0.1
 */
public final class Constants {

    public static final String BPEL_NAMESPACE = "http://docs.oasis-pen.org/wsbpel/2.0/process/executable";
    public static final String DWDL_NAMESPACE = "http://decidr.de/schema/dwdl";
    public static final String DECIDRTYPES_NAMESPACE = "http://decidr.de/schema/DecidrTypes";
    public static final String ODE_NAMESPACE = "http://www.apache.org/ode/type/extension";
    public static final String MAPPING_NAMESPACE = "http://decidr.de/schema/wsmapping";
    public static final String CONFIGURATION_NAMESPACE = "http://decidr.de/schema/wsc";
    public static Namespace XML_SCHEMA_NS = Namespace.getNamespace("xsd",
    "http://www.w3.org/2001/XMLSchema");
    public static final String DECIDRTYPES_LOCATION = "DecidrTypes.xsd";
    public static final String DECIDRWSTYPES_LOCATION = "DecidrWSTypes.xsd";
    public static final String DECIDRPROCESSTYPES_LOCATION = "DecidrProcessTypes.xsd";
    public static final String DD_NAMESPACE = "http://www.apache.org/ode/schemas/dd/2007/03";
    
    private Constants() {
        // do nothing
    }
}
