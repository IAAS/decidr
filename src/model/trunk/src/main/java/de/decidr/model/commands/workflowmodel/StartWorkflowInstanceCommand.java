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

import java.util.List;

import org.hibernate.Query;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.entities.DeployedWorkflowModel;
import de.decidr.model.entities.ServerLoadView;
import de.decidr.model.entities.WorkflowInstance;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.exceptions.WorkflowModelNotStartableException;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionAbortedEvent;
import de.decidr.model.transactions.TransactionEvent;
import de.decidr.model.workflowmodel.instancemanagement.InstanceManagerImpl;

/**
 * Creates a new workflow instance on the Apache ODE and writes a corresponding
 * entry to the database.
 * 
 * @author Daniel Huss
 * @author Markus Fischer
 * 
 * @version 0.1
 */
public class StartWorkflowInstanceCommand extends WorkflowModelCommand {

    private byte[] startConfiguration;
    private WorkflowInstance createdWorkflowInstance;

    /**
     * Constructor.
     * <p>
     * This command creates a new workflow instance on the Apache ODE and writes
     * a corresponding entry to the database.
     * 
     * @param role
     * @param workflowModelId
     * @param startConfiguration
     */
    public StartWorkflowInstanceCommand(Role role, Long workflowModelId,
            byte[] startConfiguration) {
        super(role, workflowModelId);
        this.startConfiguration = startConfiguration;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException, WorkflowModelNotStartableException {

        InstanceManagerImpl iManager = new InstanceManagerImpl();

        // create instance in database
        WorkflowInstance instance = new WorkflowInstance();
        instance.setStartedDate(DecidrGlobals.getTime().getTime());
        instance.setStartConfiguration(startConfiguration);

        evt.getSession().save(instance);
        createdWorkflowInstance = instance;

        // get server list
        String serverStatString = "from ServerLoadView";
        Query q = evt.getSession().createQuery(serverStatString);
        List<ServerLoadView> servers = q.list();

        // get deployed workflow model with highest version
        String HqlString = "from DeployedWorkflowModel w where w.id = :wid and w.version = (select max(w2.version) from DeployedWorkflowModel w2)";
        Query q2 = evt.getSession().createQuery(HqlString);

        DeployedWorkflowModel dwfm = (DeployedWorkflowModel) q2.uniqueResult();

        // start instance
        try {
            instance = iManager
                    .startInstance(dwfm, startConfiguration, servers);
            evt.getSession().update(instance);
            instance.setDeployedWorkflowModel(dwfm);
            instance.setCompletedDate(null);
            instance.setId(null); //make sure a new instance is inserted
            instance.setStartedDate(DecidrGlobals.getTime().getTime());
            // add new instance
            evt.getSession().save(instance);
            
            createdWorkflowInstance = instance;
        } catch (Exception e) {
            throw new TransactionException(e);
        }

    }

    @Override
    public void transactionAborted(TransactionAbortedEvent evt)
            throws TransactionException {

        InstanceManagerImpl iManager = new InstanceManagerImpl();
        iManager.stopInstance(createdWorkflowInstance);
        evt.getSession().delete(createdWorkflowInstance);

    }

    /**
     * @return the new workflow instance
     */
    public WorkflowInstance getCreatedWorkflowInstance() {
        return createdWorkflowInstance;
    }

}
