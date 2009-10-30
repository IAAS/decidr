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

import java.io.ByteArrayOutputStream;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.Tenant;
import de.decidr.model.entities.WorkflowModel;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionEvent;
import de.decidr.model.workflowmodel.dwdl.Workflow;

/**
 * Creates a new workflow model with the given property Name. Furthermore the
 * model will be added to the given tenant. If the given tenant does not exist
 * an exception will be thrown.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class CreateWorkflowModelCommand extends TenantCommand {

    private String workflowModelName;
    private Long workflowModelId;

    /**
     * Creates a new CreateWorkflowModelCommand. This command creates a new
     * Workflow model with the given property Name. Furthermore the model will
     * be added to the given tenant. If the given tenant does not exist an
     * exception will be thrown.
     * 
     * @param role
     *            the user/system which executes the command
     * @param tenantId
     *            the id of the tenant to which the model should be added
     * @param workflowModelName
     *            the name of the model which should be created
     * @throws IllegalArgumentException
     *             if the tenant ID is <code>null</code> or if the workflow model name is
     *             null or empty.
     */
    public CreateWorkflowModelCommand(Role role, Long tenantId,
            String workflowModelName) {
        super(role, tenantId);

        if (tenantId == null) {
            throw new IllegalArgumentException("tenantId cannot be null");
        }

        if (workflowModelName == null || workflowModelName.isEmpty()) {
            throw new IllegalArgumentException(
                    "workflow model name cannot be empty");
        }

        this.workflowModelName = workflowModelName;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        workflowModelId = null;

        Tenant tenant = fetchTenant(evt.getSession());

        WorkflowModel model = new WorkflowModel();
        model.setName(workflowModelName);
        model.setDescription("");
        model.setCreationDate(DecidrGlobals.getTime().getTime());
        model.setModifiedByUser(tenant.getAdmin());
        model.setModifiedDate(DecidrGlobals.getTime().getTime());
        model.setVersion(0);
        model.setDwdl(new byte[0]);
        model.setExecutable(false);
        model.setPublished(false);
        model.setTenant(tenant);

        evt.getSession().save(model);

        try {
            model.setDwdl(createNewWorkflowModelXml(tenant.getName(), model
                    .getId(), model.getName()));
        } catch (JAXBException e) {
            throw new TransactionException(e);
        }
        evt.getSession().update(model);

        workflowModelId = model.getId();
    }

    /**
     * Creates the DWDL xml data for a new, empty workflow model using the
     * template found in the resources folder.
     * 
     * @param tenantName
     *            name of the tenant that owns the workflow model - required to
     *            set the target namespace.
     * @param id
     *            id of the new workflow model.
     * @param name
     *            name of the new workflow model
     * @return raw xml data of the new workflow model (created by unmarshalling
     *         the template then marshalling it again)
     * @throws JAXBException
     *             if the template cannot be found or contains invalid data.
     */
    @SuppressWarnings("unchecked")
    protected byte[] createNewWorkflowModelXml(String tenantName, Long id,
            String name) throws JAXBException {
        if ((tenantName == null) || (tenantName.isEmpty())) {
            throw new IllegalArgumentException(
                    "Tenant name cannot be null or empty");
        }

        if (id == null) {
            throw new IllegalArgumentException(
                    "Workflow model id cannot be null");
        }

        if (name == null) {
            throw new IllegalArgumentException(
                    "Workflow model name cannot be null");
        }

        URL templateUrl = getClass().getClassLoader().getResource(
                "dwdl/newWorkflowModel.template.xml");

        JAXBContext jc = JAXBContext.newInstance(Workflow.class);
        Unmarshaller u = jc.createUnmarshaller();

        /**
         * Modify the template where necessary
         */
        JAXBElement<Workflow> je = (JAXBElement<Workflow>) u
                .unmarshal(templateUrl);
        Workflow workflowModel = je.getValue();
        workflowModel.setName(name);
        workflowModel.setTargetNamespace("http://decidr.de/" + tenantName
                + "/processes/" + id.toString());
        workflowModel.setId(id);

        Marshaller m = jc.createMarshaller();
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        m.marshal(je, result);

        return result.toByteArray();
    }

    /**
     * @return the id of the created workflow model
     */
    public Long getWorkflowModelId() {
        return workflowModelId;
    }
}
