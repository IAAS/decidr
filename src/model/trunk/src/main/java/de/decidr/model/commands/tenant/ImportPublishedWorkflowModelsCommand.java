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
package de.decidr.model.commands.tenant;

import java.util.Date;
import java.util.List;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.acl.access.WorkflowModelAccess;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.Tenant;
import de.decidr.model.entities.WorkflowModel;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Imports the given workflow models to the given tenant. If one or more of the
 * given models are not public an exception will be thrown.
 * 
 * @author Markus Fischer
 * 
 * @version 0.1
 */
public class ImportPublishedWorkflowModelsCommand extends TenantCommand
        implements WorkflowModelAccess {

    private List<Long> modelIdList;
    private List<WorkflowModel> modelList;

    /**
     * Creates a new ImportPublishedWorkflowModelsCommand. This command imports
     * the given workflow models to the given tenant. If one or more of the
     * given models are not public an exception will be thrown.
     * 
     * @param role
     *            user which executes the command
     * @param tenantId
     *            the ID of the tenant into which the models should be imported
     * @param workflowModelIds
     *            the IDs of the models which should be imported
     */
    public ImportPublishedWorkflowModelsCommand(Role role, Long tenantId,
            List<Long> workflowModelIds) {
        super(role, tenantId);
        this.modelIdList = workflowModelIds;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        Date now = DecidrGlobals.getTime().getTime();

        Tenant t = fetchTenant(evt.getSession());

        modelList = evt.getSession().createQuery(
                "from WorkflowModel m where m.id in (:modelIds)")
                .setParameterList("modelIds", modelIdList).list();

        for (WorkflowModel model : modelList) {
            // DH what if the published wfm belongs to the tenant t?
            if (model.isPublished()) {
                // create a copy of the model that belongs to tenant "t"
                WorkflowModel newModel = new WorkflowModel();
                newModel.setPublished(false);
                newModel.setTenant(t);
                newModel.setDwdl(model.getDwdl());
                newModel.setDescription(model.getDescription());
                newModel.setName(model.getName());
                newModel.setModifiedByUser(t.getAdmin());
                newModel.setModifiedDate(now);
                newModel.setCreationDate(now);
                newModel.setVersion(0);
                newModel.setExecutable(false);
                evt.getSession().save(newModel);
            } else {
                throw new TransactionException(
                        "Given workflowModel is not published.");
            }
        }
    }

    @Override
    public Long[] getWorkflowModelIds() {
        Long[] res = new Long[0];
        return modelList.toArray(res);
    }
}
