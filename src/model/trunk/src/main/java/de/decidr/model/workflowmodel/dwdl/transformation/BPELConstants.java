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
    
    static class Process {
        
        public static final String NS_PREFIX = "tns";
        public static final String PARTNERLINK = "ProcessPL";
        public static final String MYROLE = "ProcessProvider";
        public static final String CORRELATION = "standard-correlation";
        public static final String[] CORRELATION_PROPERTIES = {"taskID"};
    }
    
    static class Humantask {
        public static final String NAME = "Decidr-HumanTask";
        
    }
    
    static class Email {
        public static final String NAME = "Decidr-Email";
    }
    
    static class Types {
        public static final String NS_PREFIX = "decidr";
    }
    
    static class Processtypes {
        public static final String NS_PREFIX = "pdecidr";
    }
    
    static class Variables {
        public static final String SUCCESS_IN = "successIn";
        public static final String SUCCESS_OUT = "successOut";
        public static final String FAULT_IN = "faultMessageIn";
        public static final String FAULT_OUT = "faultMessageOut";
        public static final String HUMANTASK_CALLBACK_IN = "humanTaskCallbackIn";
        public static final String PROCESS_IN = "processIn";
        public static final String PROCESS_OUT = "processOut";
    }
    
    
    
    private BPELConstants(){
        // do nothing
    }

}
