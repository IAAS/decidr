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

package de.decidr.setup.input;

import de.decidr.setup.helpers.BooleanRequest;
import de.decidr.setup.helpers.EmailRequest;
import de.decidr.setup.helpers.NumberRequest;
import de.decidr.setup.helpers.StringRequest;
import de.decidr.setup.model.SystemSettings;

/**
 * TODO: add comment
 * 
 * @author Johannes Engelhardt
 */
public class InputSystemSettings {
    
    /*
    private String id = "1";
    private String modifiedDate;
    private String autoAcceptNewTenants;
    private String systemName;
    private String domain;
    private String systemEmailAddress;
    private String logLevel;
    private String superAdminId = "1";
    private String passwordResetRequestLifetimeSeconds;
    private String registrationRequestLifetimeSeconds;
    private String changeEmailRequestLifetimeSeconds;
    private String invitationLifetimeSeconds;
    private String mtaHostname;
    private String mtaPort;
    private String mtaUseTls;
    private String mtaUsername;
    private String mtaPassword;
    private String maxUploadFileSizeBytes;
    private String maxAttachmentsPerEmail;
    private String monitorUpdateIntervalSeconds;
    private String monitorAveragingPeriodSeconds;
    private String serverPoolInstances;
    private String minServerLoadForLock;
    private String maxServerLoadForUnlock;
    private String maxServerLoadForShutdown;
    private String minUnlockedServers;
    private String minWorkflowInstancesForLock;
    private String maxWorkflowInstancesForUnlock;
    private String maxWorkflowInstancesForShutdown;
    */

    public static SystemSettings getSystemSettings() {
        System.out.println("------------------------------------------------");
        System.out.println("Set up System Settings");
        System.out.println("------------------------------------------------");

        SystemSettings ss = new SystemSettings();

        ss.setAutoAcceptNewTenants(BooleanRequest.getResult("Auto accept new tenants", "yes"));
        ss.setSystemName(StringRequest.getResult("System name", "DecidR"));
        ss.setDomain(StringRequest.getResult("Domain", "decidr.de"));
        ss.setSystemEmailAddress(EmailRequest.getResult("System email address"));
        ss.setLogLevel(NumberRequest.getResult("Loglevel", "0"));
        
        
        return ss;
    }

    
}
