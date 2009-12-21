package de.decidr.test.database.factories;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBException;

import org.hibernate.Query;
import org.hibernate.Session;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.XmlTools;
import de.decidr.model.entities.DeployedWorkflowModel;
import de.decidr.model.entities.Server;
import de.decidr.model.entities.Tenant;
import de.decidr.model.entities.User;
import de.decidr.model.entities.UserAdministratesWorkflowModel;
import de.decidr.model.entities.UserAdministratesWorkflowModelId;
import de.decidr.model.entities.WorkflowModel;
import de.decidr.model.entities.WorkflowModelIsDeployedOnServer;
import de.decidr.model.entities.WorkflowModelIsDeployedOnServerId;
import de.decidr.model.enums.ServerTypeEnum;
import de.decidr.model.workflowmodel.dwdl.Workflow;
import de.decidr.test.database.main.ProgressListener;

/**
 * Creates random workflow models. To create a workflow model at least one
 * approved tenant and one server of the "ode" type must be present in the
 * database.
 * 
 * @author Thomas Karsten
 * @author Daniel Huss
 * @version 0.1
 */
public class WorkflowModelFactory extends EntityFactory {

    /**
     * Maximum number of deployed workflow models to create per workflow model
     */
    private static final int MAX_DEPLOYED_VERSIONS = 3;

    /**
     * Maximum number of additional workflow administrators
     */
    private static final int MAX_ADDITIONAL_WORKFLOW_ADMINS = 3;

    /**
     * Constructor
     * 
     * @param session
     */
    public WorkflowModelFactory(Session session) {
        super(session);
    }

    /**
     * @param session
     * @param progressListener
     */
    public WorkflowModelFactory(Session session,
            ProgressListener progressListener) {
        super(session, progressListener);
    }

    /**
     * Creates numModels random workflow models. Every other workflow model
     * created in this way will also have a deployed version. Every 10th
     * workflow model will be flagged as published.
     * 
     * @param numModels
     *            TODO document
     * @param modelsPerTenant
     *            TODO document
     * @return TODO document
     */
    public List<WorkflowModel> createRandomWorkflowModels(int numModels,
            int modelsPerTenant) {

        ArrayList<WorkflowModel> result = new ArrayList<WorkflowModel>(
                numModels);

        if (modelsPerTenant < 1) {
            return result;
        }

        Date now = DecidrGlobals.getTime().getTime();

        // find suitable owning tenants
        List<Tenant> owners = getRandomApprovedTenants(numModels
                / modelsPerTenant);

        XmlFactory xml = new XmlFactory(session);
        byte[] sampleDwdl = xml.getDwdl();
        Workflow workflow;
        try {
            workflow = XmlTools.getElement(Workflow.class, sampleDwdl);

            for (int i = 0; i < numModels; i++) {
                WorkflowModel model = new WorkflowModel();
                model.setDescription("test workflow model #"
                        + Integer.toString(i));
                model.setName("workflow " + Integer.toString(i));
                model.setDwdl(new byte[0]);
                model.setVersion(rnd.nextInt(100));
                model.setExecutable(rnd.nextBoolean());

                Tenant owningTenant = owners.get(i % owners.size());
                model.setTenant(owningTenant);
                model.setModifiedByUser(owningTenant.getAdmin());
                model.setCreationDate(now);
                model.setModifiedDate(now);
                // every 10th model is available to the public for import
                model.setPublished(i % 10 == 0);
                session.save(model);

                // we need the workflow model id to set it within the DWDL file
                workflow.setId(model.getId());
                model.setDwdl(XmlTools.getBytes(workflow));

                // find suitable workflow administrators -requires persisted
                // workflow model!
                model
                        .setUserAdministratesWorkflowModels(getSuitableWorkflowAdministrators(model));

                result.add(model);

                if (rnd.nextBoolean()) {
                    // create some deployed versions of the model
                    int numDeployedVersions = rnd
                            .nextInt(MAX_DEPLOYED_VERSIONS) + 1;
                    for (int j = 1; j <= numDeployedVersions; j++) {
                        addDeployedWorkflowModels(model, j);
                    }
                }

                session.update(model);

                fireProgressEvent(numModels, i + 1);
            }
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * Tries to find up to MAX_ADDITIONAL_WORKFLOW_ADMINS active tenant members
     * that will become workflow administrators.
     * 
     * @param model
     *            workflow model to administrate
     * @return The new but not yet persisted "is-workflow-admin-of-models"
     *         relations
     */
    @SuppressWarnings("unchecked")
    private Set<UserAdministratesWorkflowModel> getSuitableWorkflowAdministrators(
            WorkflowModel model) {

        String hql = "from User u where u.userProfile is not null and "
                + "u.registeredSince is not null and "
                + "u.disabledSince is null and "
                + "exists(from UserIsMemberOfTenant rel where rel.user = u and rel.tenant = :tenant)";

        List<User> admins = session.createQuery(hql).setMaxResults(
                MAX_ADDITIONAL_WORKFLOW_ADMINS).setEntity("tenant",
                model.getTenant()).list();

        Set<UserAdministratesWorkflowModel> result = new HashSet<UserAdministratesWorkflowModel>();

        for (User admin : admins) {
            UserAdministratesWorkflowModelId relationId = new UserAdministratesWorkflowModelId(
                    admin.getId(), model.getId());
            UserAdministratesWorkflowModel relation = new UserAdministratesWorkflowModel(
                    relationId, model, admin);

            session.save(relation);

            result.add(relation);
        }

        return result;
    }

    /**
     * Adds a persisted deployed workflow model that corresponds with the given
     * workflow model.
     * 
     * @param model
     *            model for which a deployed version is added
     * @param n
     *            The Nth deployed version of a workflow model receives version
     *            model.getVersion() - (n-1). If that version would be less than
     *            zero, no deployed version is added.
     */
    public void addDeployedWorkflowModels(WorkflowModel model, int n) {
        if (n < 1) {
            throw new IllegalArgumentException("n must be > 0");
        }

        long deployedVersion = model.getVersion() - (n - 1);
        if (deployedVersion < 0) {
            return;
        }

        DeployedWorkflowModel deployed = new DeployedWorkflowModel();
        deployed.setOriginalWorkflowModel(model);
        deployed.setDeployDate(DecidrGlobals.getTime().getTime());
        deployed.setDescription(model.getDescription());
        deployed.setDwdl(model.getDwdl());
        deployed.setName(model.getName());
        deployed.setSoapTemplate(new byte[0]);
        deployed.setWsdl(new byte[0]);
        deployed.setVersion(deployedVersion);
        deployed.setTenant(model.getTenant());

        // make it seem as if the model had been deployed on an ODE server, but
        // do not actually deploy the model
        WorkflowModelIsDeployedOnServer deployedOn = new WorkflowModelIsDeployedOnServer();
        Server server = getRandomOdeServer();
        deployedOn.setDeployedWorkflowModel(deployed);
        deployedOn.setServer(server);

        session.save(deployed);
        deployedOn.setId(new WorkflowModelIsDeployedOnServerId(
                deployed.getId(), server.getId()));
        session.save(deployedOn);
    }

    /**
     * @return a random ODE server
     */
    private Server getRandomOdeServer() {
        String hql = "from Server s where s.serverType.name = :serverName order by rand()";

        Server result = (Server) session.createQuery(hql).setMaxResults(1)
                .setString("serverName", ServerTypeEnum.Ode.toString())
                .uniqueResult();

        if (result == null) {
            throw new RuntimeException(
                    "Cannot find a server to 'deploy' the workflow model on");
        }

        return result;
    }

    /**
     * @return a random list of approved tenants.
     */
    @SuppressWarnings("unchecked")
    private List<Tenant> getRandomApprovedTenants(int maxTenants) {
        String hql = "from Tenant t where t.approvedSince is not null "
                + "order by rand()";

        Query q = session.createQuery(hql).setMaxResults(maxTenants);

        List<Tenant> result = q.list();

        if (result.size() == 0) {
            throw new RuntimeException("Cannot find an approved tenant.");
        }

        return result;
    }
}
