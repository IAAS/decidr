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

package de.decidr.model.commands.workflowmodel;

import javax.xml.bind.JAXBException;

import org.hibernate.Query;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.DeployedWorkflowModel;
import de.decidr.model.entities.StartConfiguration;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionEvent;
import de.decidr.model.workflowmodel.dwdl.translator.TransformUtil;
import de.decidr.model.workflowmodel.dwdl.translator.Translator;
import de.decidr.model.workflowmodel.wsc.TConfiguration;

/**
 * Saves the given start configuration as the last used start configuration of
 * the given model.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class SaveStartConfigurationCommand extends WorkflowModelCommand {

    private TConfiguration startConfiguration;

    public SaveStartConfigurationCommand(Role role, Long workflowModelId,
            TConfiguration startConfiguration) {
        super(role, workflowModelId);
        this.startConfiguration = startConfiguration;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        DeployedWorkflowModel deployedModel = fetchCurrentDeployedWorkflowModel(evt
                .getSession());

        if (deployedModel == null) {
            // the workflow model hasn't been deployed, can't save the config.
            throw new EntityNotFoundException(DeployedWorkflowModel.class);
        }

        // remove old start configuration(s)
        Query q = evt.getSession().createQuery(
                "delete from StartConfiguration "
                        + "where deployedWorkflowModel = :deployedModel")
                .setEntity("deployedModel", deployedModel);
        q.executeUpdate();

        // save new start configuration
        StartConfiguration lastStartConfiguration = new StartConfiguration();
        lastStartConfiguration.setDeployedWorkflowModel(deployedModel);
        try {
            lastStartConfiguration.setStartConfiguration(TransformUtil
                    .configuration2Bytes(startConfiguration));
        } catch (JAXBException e) {
            throw new TransactionException(e);
        }
        evt.getSession().save(lastStartConfiguration);
    }
}
