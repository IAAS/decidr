package de.decidr.test.database.factories;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import de.decidr.model.acl.Password;
import de.decidr.model.entities.DeployedWorkflowModel;
import de.decidr.model.entities.Server;
import de.decidr.model.entities.WorkflowInstance;
import de.decidr.model.enums.ServerTypeEnum;

/**
 * Creates random workflow instances. To create a workflow instance at least one
 * deployed workflow model and one server of the "ode" type must be present in
 * the database.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class WorkflowInstanceFactory extends EntityFactory {

    /**
     * Constructor
     * 
     * @param session
     */
    public WorkflowInstanceFactory(Session session) {
        super(session);
    }

    /**
     * Creates numInstances random persisted workflow instances.
     * 
     * @param numInstances
     * @param instancesPerModel
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<WorkflowInstance> createRandomWorkflowInstances(int numInstances) {
        ArrayList<WorkflowInstance> result = new ArrayList<WorkflowInstance>();

        // find some suitable deployed workflow models and ODE servers.
        String hql = "from DeployedWorkflowModel d "
                + "where d.originalWorkflowModel is not null "
                + "order by rand()";
        List<DeployedWorkflowModel> deployedModels = session.createQuery(hql)
                .setMaxResults(1000).list();

        if (deployedModels == null || deployedModels.isEmpty()) {
            throw new RuntimeException(
                    "Need at least one deployed workflow model to create a workflow instance.");
        }

        hql = "from Server s where s.serverType.name = :serverType";
        List<Server> servers = session.createQuery(hql).setString("serverType",
                ServerTypeEnum.Ode.toString()).list();

        if (servers == null || servers.isEmpty()) {
            throw new RuntimeException(
                    "Need at least one ODE server to create a workflow instance.");
        }

        for (int i = 0; i < numInstances; i++) {
            WorkflowInstance newInstance = new WorkflowInstance();

            if (i % 5 == 0) {
                // every 5th workflow instance is waiting to be started
                newInstance.setStartedDate(null);
            } else {
                // We allow startedDate to be in the future. The system
                // should be able to cope with it!
                newInstance.setStartedDate(getRandomDate(true, true, SPAN_YEAR));
                if (i % 2 == 0) {
                    // every other workflow instance that has been started has
                    // been completed or terminated, but the start date should
                    // always preceed the completed date.
                    long completedDateMilliSeconds = newInstance
                            .getStartedDate().getTime()
                            + rnd.nextInt(Integer.MAX_VALUE);
                    newInstance.setCompletedDate(new Date(
                            completedDateMilliSeconds));
                } else {
                    newInstance.setCompletedDate(null);
                }
            }

            try {
                newInstance.setOdePid(Password.getRandomAuthKey());
            } catch (UnsupportedEncodingException e) {
                // should never happen - abort!
                throw new RuntimeException(e);
            } catch (NoSuchAlgorithmException e) {
                // should never happen - abort!
                throw new RuntimeException(e);
            }
            newInstance.setServer(servers.get(rnd.nextInt(servers.size())));
            newInstance.setStartConfiguration(getBlobStub());

            session.save(newInstance);
            result.add(newInstance);
        }
        return result;
    }

}
