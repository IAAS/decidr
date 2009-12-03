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

/**
 * A class that holds all BPEL relevant constants
 *
 * @author Modood Alvi
 */
public final class WSDLConstants {
    
    public static final String PROCESS_MESSAGE_IN = "startMessageRequest";
    public static final String PROCESS_MESSAGE_OUT = "startMessageResponse";
    public static final String PROCESS_PARTNERLINKTYPE = "ProcessPLT";
    public static final String PROCESS_MESSAGE_IN_ELEMENT = "startProcess";
    public static final String PROCESS_MESSAGE_OUT_ELEMENT = "startProcessResponse";
    public static final String PROCESS_MESSAGE_ELEMENT_TYPE = "startMessageType";
    public static final String PROCESS_OPERATION = "startProcess";
    
    // HumanTask callback message type in 
    public static final String HUMANTASK_CALLBACK_MESSAGETYPE_IN = "taskCompletedRequest";
    
    // HumanTask callback message type out
    public static final String HUMANTASK_CALLBACK_MESSAGETYPE_OUT = "taskCompletedResponse";
    
    // HumanTask callback port
    public static final String HUMANTASK_CALLBACK_PORT = "basicProcessPT";
    
    // HumanTask callback namespace
    public static final String HUMANTASK_CALLBACK_NAMESPACE = "http://decidr.de/wsdl/basicProcess";
    
    private WSDLConstants(){
        // do nothing
    }

}
