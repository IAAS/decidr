package de.decidr.test.database.factories;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.entities.DeployedWorkflowModel;
import de.decidr.model.entities.Server;
import de.decidr.model.entities.Tenant;
import de.decidr.model.entities.User;
import de.decidr.model.entities.UserAdministratesWorkflowModel;
import de.decidr.model.entities.WorkflowModel;
import de.decidr.model.entities.WorkflowModelIsDeployedOnServer;
import de.decidr.model.enums.ServerTypeEnum;

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
     * Creates numModels random workflow models. Every other workflow model
     * created in this way will also have a deployed version. Every 10th
     * workflow model will be flagged as published.
     * 
     * @param numModels
     * @param modelsPerTenant
     * @return
     */
    public List<WorkflowModel> createRandomWorkflowModels(int numModels,
            int modelsPerTenant) {

        if (modelsPerTenant < 1) {
            throw new IllegalArgumentException(
                    "modelsPerTenant must be greater than zero");
        }

        ArrayList<WorkflowModel> result = new ArrayList<WorkflowModel>(
                numModels);

        Date now = DecidrGlobals.getTime().getTime();

        // find suitable owning tenants
        List<Tenant> owners = getRandomApprovedTenants(numModels
                / modelsPerTenant);

        for (int i = 0; i < numModels; i++) {
            WorkflowModel model = new WorkflowModel();
            model.setDescription("test workflow model #" + Integer.toString(i));
            model.setName("workflow " + Integer.toString(i));
            model.setDwdl(getSampleDwdl());
            model.setVersion(rnd.nextInt(1000000));
            model.setExecutable(rnd.nextBoolean());

            if (rnd.nextBoolean()) {
                // create some deployed versions of the model
                int numDeployedVersions = rnd.nextInt(MAX_DEPLOYED_VERSIONS) + 1;
                for (int j = 1; j <= numDeployedVersions; j++) {
                    addDeployedWorkflowModels(model);
                    // cannot create two deployed workflow models of the same
                    // version
                    if (j < numDeployedVersions) {
                        model.setVersion(model.getVersion() + 1L);
                    }
                }
            }

            Tenant owningTenant = owners.get(i % owners.size());
            model.setTenant(owningTenant);
            model.setModifiedByUser(owningTenant.getAdmin());
            model.setCreationDate(now);
            model.setModifiedDate(now);
            // every 10th model is available to the public for import
            model.setPublished(i % 10 == 0);

            // find suitable workflow administrators
            model
                    .setUserAdministratesWorkflowModels(getSuitableWorkflowAdministrators(model));

            session.save(model);
            result.add(model);
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

        String hql = "select distinct * from User u where u.userProfile is not null and "
                + "u.registeredSince is not null and "
                + "u.disabledSince is null and "
                + "exists(from UserIsMemberOfTenant rel where rel.user = u and rel.tenant = :tenant)";

        List<User> admins = session.createQuery(hql).setMaxResults(
                MAX_ADDITIONAL_WORKFLOW_ADMINS).setEntity("tenant",
                model.getTenant()).list();

        Set<UserAdministratesWorkflowModel> result = new HashSet<UserAdministratesWorkflowModel>();

        for (User admin : admins) {
            // hopefully Hibernate will generate the id from the given model and
            // admin. The model id is not available at this point since the
            // model hasn't been persisted yet.
            result.add(new UserAdministratesWorkflowModel(null, model, admin));
        }

        return null;
    }

    /**
     * Adds a deployed workflow model that corresponds with the given workflow
     * model.
     * 
     * @param model
     */
    public void addDeployedWorkflowModels(WorkflowModel model) {
        DeployedWorkflowModel deployed = new DeployedWorkflowModel();
        deployed.setOriginalWorkflowModel(model);
        deployed.setDeployDate(DecidrGlobals.getTime().getTime());
        deployed.setDescription(model.getDescription());
        deployed.setDwdl(model.getDwdl());
        deployed.setName(model.getName());
        deployed.setSoapTemplate(getBlobStub());
        deployed.setWsdl(getBlobStub());
        deployed.setTenant(model.getTenant());

        // make it seem as if the model had been deployed on an ODE server, but
        // do not actually deploy the model
        WorkflowModelIsDeployedOnServer deployedOn = new WorkflowModelIsDeployedOnServer();
        Server server = getRandomOdeServer();
        deployedOn.setDeployedWorkflowModel(deployed);
        deployedOn.setServer(server);

        Set<WorkflowModelIsDeployedOnServer> deployedOnSet = new HashSet<WorkflowModelIsDeployedOnServer>();
        deployedOnSet.add(deployedOn);

        deployed.setWorkflowModelIsDeployedOnServers(deployedOnSet);
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
     * @return a (not so large) blob that contains the binary UTF-8
     *         representation of the string "empty".
     */
    private byte[] getBlobStub() {
        return "empty".getBytes(Charset.forName("UTF-8"));
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

    /**
     * @return the contents of a sample DWDL file.
     */
    private byte[] getSampleDwdl() {
        java.io.File sampleFile = new java.io.File("sampleProcess.xml");
        byte[] result = new byte[(int) sampleFile.length()];

        FileInputStream stream = null;
        try {
            stream = new FileInputStream("sampleProcess.xml");
            int bytesRead = stream.read(result);
            if (sampleFile.length() != bytesRead || sampleFile.length() == 0) {
                throw new RuntimeException("could not read sample DWDL");
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return result;
    }
}
