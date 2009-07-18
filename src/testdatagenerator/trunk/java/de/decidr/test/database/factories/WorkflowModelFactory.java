package de.decidr.test.database.factories;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.entities.DeployedWorkflowModel;
import de.decidr.model.entities.Server;
import de.decidr.model.entities.Tenant;
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
public class WorkflowModelFactory {

    private static Random rnd = new Random();

    private Session session;

    /**
     * Constructor
     * 
     * @param session
     */
    public WorkflowModelFactory(Session session) {
        this.session = session;
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

            model.setExecutable(rnd.nextBoolean());

            if (rnd.nextBoolean()) {
                // create a deployed version of the model
                addDeployedWorkflowModels(model);
            }

            Tenant owningTenant = owners.get(i % owners.size());
            model.setTenant(owningTenant);
            model.setModifiedByUser(owningTenant.getAdmin());
            model.setCreationDate(now);
            model.setModifiedDate(now);
            // every 10th model is available to the public for import
            model.setPublished(i % 10 == 0);
            model.setVersion(rnd.nextInt(1000000));

            result.add(model);
        }

        return result;
    }

    /**
     * Adds a deployed workflow model that corresponds with the given workflow
     * model.
     * 
     * TODO create more than one deployed workflow model (?)
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
        String hql = "from Server s where s.serverType.name = :serverName order by rand() limit 1";

        Server result = (Server) session.createQuery(hql).setString(
                "serverName", ServerTypeEnum.Ode.toString()).uniqueResult();

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
                + "order by rand() limit :maxTenants";

        Query q = session.createQuery(hql).setInteger("maxTenants", maxTenants);

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
            if (sampleFile.length() != bytesRead) {
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