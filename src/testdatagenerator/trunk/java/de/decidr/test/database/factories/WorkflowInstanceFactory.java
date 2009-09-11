package de.decidr.test.database.factories;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import de.decidr.model.acl.Password;
import de.decidr.model.entities.DeployedWorkflowModel;
import de.decidr.model.entities.Server;
import de.decidr.model.entities.User;
import de.decidr.model.entities.UserAdministratesWorkflowInstance;
import de.decidr.model.entities.UserAdministratesWorkflowInstanceId;
import de.decidr.model.entities.UserParticipatesInWorkflow;
import de.decidr.model.entities.UserParticipatesInWorkflowId;
import de.decidr.model.entities.WorkflowInstance;
import de.decidr.model.enums.ServerTypeEnum;
import de.decidr.test.database.main.ProgressListener;

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
     * @param session
     * @param progressListener
     */
    public WorkflowInstanceFactory(Session session,
            ProgressListener progressListener) {
        super(session, progressListener);
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
                newInstance
                        .setStartedDate(getRandomDate(true, true, SPAN_YEAR));
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

            newInstance.setOdePid(Password.getRandomAuthKey());
            newInstance.setServer(servers.get(rnd.nextInt(servers.size())));
            newInstance.setStartConfiguration(getBlobStub());
            newInstance.setDeployedWorkflowModel(deployedModels.get(rnd
                    .nextInt(deployedModels.size())));

            session.save(newInstance);
            result.add(newInstance);

            associateParticipantsWithInstance(newInstance, rnd.nextInt(50) + 1);
            associateAdministratorsWithInstance(newInstance, rnd.nextInt(5) + 1);

            fireProgressEvent(numInstances, i + 1);
        }
        return result;
    }

    /**
     * Makes up to numAdmin tenant members administrators of the given workflow
     * instance
     * 
     * @param instance
     * @param numAdmins
     */
    @SuppressWarnings("unchecked")
    private void associateAdministratorsWithInstance(WorkflowInstance instance,
            int numAdmins) {
        String hql = "from User u where "
                + "exists(from UserIsMemberOfTenant rel where rel.user = u and "
                + "rel.tenant = :tenant) and u.userProfile is not null "
                + "order by rand()";
        List<User> workflowAdmins = session.createQuery(hql).setEntity(
                "tenant", instance.getDeployedWorkflowModel().getTenant())
                .setMaxResults(numAdmins).list();

        if (workflowAdmins.isEmpty()) {
            throw new RuntimeException(
                    "Could not find a user to become workflow instance admin.");
        }

        for (User admin : workflowAdmins) {
            UserAdministratesWorkflowInstance rel = new UserAdministratesWorkflowInstance();
            rel.setUser(admin);
            rel.setWorkflowInstance(instance);
            rel.setId(new UserAdministratesWorkflowInstanceId(admin.getId(),
                    instance.getId()));
            session.save(rel);
        }
    }

    /**
     * Makes up to numUsers users participants of the given workflow instance.
     * 
     * @param instance
     * @param numUsers
     */
    @SuppressWarnings("unchecked")
    private void associateParticipantsWithInstance(WorkflowInstance instance,
            int numUsers) {
        String hql = "from User u where "
                + "exists(from UserIsMemberOfTenant rel where rel.user = u and "
                + "rel.tenant = :tenant) or "
                + "exists(from Tenant t where t.admin = u) "
                + "order by rand()";
        List<User> participants = session.createQuery(hql).setEntity("tenant",
                instance.getDeployedWorkflowModel().getTenant()).setMaxResults(
                numUsers).list();

        if (participants.isEmpty()) {
            throw new RuntimeException(
                    "Could not find a user to become workflow participant.");
        }

        for (User participant : participants) {
            UserParticipatesInWorkflow rel = new UserParticipatesInWorkflow();
            rel.setUser(participant);
            rel.setWorkflowInstance(instance);
            rel.setId(new UserParticipatesInWorkflowId(participant.getId(),
                    instance.getId()));
            session.save(rel);
        }
    }
}
