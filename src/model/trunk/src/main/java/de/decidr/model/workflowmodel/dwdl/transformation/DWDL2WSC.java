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

import org.apache.log4j.Logger;

import de.decidr.model.logging.DefaultLogger;
import de.decidr.model.workflowmodel.dwdl.Actor;
import de.decidr.model.workflowmodel.dwdl.Boolean;
import de.decidr.model.workflowmodel.dwdl.Role;
import de.decidr.model.workflowmodel.dwdl.Variable;
import de.decidr.model.workflowmodel.dwdl.Workflow;
import de.decidr.model.workflowmodel.wsc.ObjectFactory;
import de.decidr.model.workflowmodel.wsc.TActor;
import de.decidr.model.workflowmodel.wsc.TAssignment;
import de.decidr.model.workflowmodel.wsc.TConfiguration;
import de.decidr.model.workflowmodel.wsc.TRole;
import de.decidr.model.workflowmodel.wsc.TRoles;

/**
 * This class builds the start configuration for the workflow
 * 
 * @author Modood Alvi
 * @version 0.1
 */
public class DWDL2WSC {
    
    private static Logger log = DefaultLogger.getLogger(DWDL2WSC.class);

    private ObjectFactory factory = new ObjectFactory();

    public TConfiguration getStartConfiguration(Workflow dwdl) {
        TConfiguration startConfiguration = factory.createTConfiguration();
        if (dwdl.isSetRoles()) {
            TRoles roles = factory.createTRoles();
            for (Role role : dwdl.getRoles().getRole()) {
                if (role.isSetConfigurationVariable()
                        && role.getConfigurationVariable().equals(Boolean.YES)) {
                    TRole newRole = factory.createTRole();
                    newRole.setName(role.getName());
                    roles.getRole().add(newRole);
                }
            }
            for (Actor actor : dwdl.getRoles().getActor()) {
                if (actor.isSetConfigurationVariable()
                        && actor.getConfigurationVariable().equals(Boolean.YES)) {
                    TActor newActor = factory.createTActor();
                    newActor.setName(actor.getName());
                    roles.getActor().add(newActor);
                }
            }
            startConfiguration.setRoles(roles);
            for (Variable variable : dwdl.getVariables().getVariable()) {
                if (variable.isSetConfigurationVariable()
                        && variable.getConfigurationVariable().equals(
                                Boolean.YES)) {
                    TAssignment assignment = factory.createTAssignment();
                    assignment.setKey(variable.getName());
                    assignment.setValueType(variable.getType());
                    startConfiguration.getAssignment().add(assignment);
                }
            }
        }

        return startConfiguration;
    }
}
