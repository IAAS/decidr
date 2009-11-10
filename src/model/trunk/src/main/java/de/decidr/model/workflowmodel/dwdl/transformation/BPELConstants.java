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
public final class BPELConstants {
    
    // global process variables names
    public static final String SUCCESS_MESSAGE_REQUEST = "successMessageRequest";
    public static final String SUCCESS_MESSAGE_RESPONSE = "successMessageResponse";
    public static final String FAULT_MESSAGE_REQUEST = "faultMessageRequest";
    public static final String FAULT_MESSAGE_RESPONSE = "faultMessageResponse";
    public static final String PROCESS_HUMANTASK_INPUT_VARIABLE = "taskDataMessageRequest";
    public static final String PROCESS_INPUT_VARIABLE = "startConfigurations";

    // namespace prefixes
    public static final String DECIDRTYPES_PREFIX = "decidr";
    public static final String DECIDRPROCESSTYPES_PREFIX = "pdecidr";
    public static final String PROCESS_PREFIX = "tns";

    // process message types and operations
    public static final String PROCESS_REQUEST_MESSAGE = "startMessage";
    public static final String PROCESS_HUMANTASK_REQUEST_MESSAGE = "taskCompletedRequest";

    // process partner link names and types
    public static final String PROCESS_PARTNERLINK = "ProcessPL";
    public static final String PROCESS_MYROLE = "ProcessProvider";

    // Decidr web services' names
    public static final String EMAIL_ACTIVITY_NAME = "Decidr-Email";
    public static final String HUMANTASK_ACTIVITY_NAME = "Decidr-HumanTask";
    
    // correlation properties used
    public static final String[] CORRELATION_PROPERTIES = {"processID", "userID", "taskID"};
    
    
    private BPELConstants(){
        // do nothing
    }

}
