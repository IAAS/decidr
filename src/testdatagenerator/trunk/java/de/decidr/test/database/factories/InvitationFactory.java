package de.decidr.test.database.factories;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import de.decidr.model.entities.Invitation;
import de.decidr.model.entities.Tenant;
import de.decidr.model.entities.User;
import de.decidr.model.entities.UserParticipatesInWorkflow;
import de.decidr.model.entities.WorkflowInstance;
import de.decidr.model.entities.WorkflowModel;

/**
 * Creates random persisted invitations. To create an invitation you need at
 * least one workflow instance, at least one approved tenant and at least one
 * workflow model within an approved tenant.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class InvitationFactory extends EntityFactory {

    /**
     * Constructor
     * 
     * @param session
     */
    public InvitationFactory(Session session) {
        super(session);
    }

    /**
     * Creates up to numInvitations persisted Invitations spread evenly across
     * all invitation types.
     * 
     * @param numInvitations
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Invitation> createRandomInvitations(int numInvitations) {
        ArrayList<Invitation> result = new ArrayList<Invitation>();

        // fetch available invitation receivers from database
        List<User> users = session.createQuery("from User u").list();
        if (users == null || users.isEmpty()) {
            throw new RuntimeException(
                    "need at least one user to create invitations.");
        }

        for (int i = 0; i < numInvitations; i++) {
            Invitation newInvitation;
            User randomUser = users.get(rnd.nextInt(users.size()));

            switch (i % 3) {
            case 0:
                newInvitation = createAdministrateWorkflowModelInvitation(randomUser);
                break;

            case 1:
                newInvitation = createJoinTenantInvitation(randomUser);
                break;

            default:
                newInvitation = createParticipateInWorkflowInvitation(randomUser);
                break;
            }

            if (newInvitation != null) {
                session.save(newInvitation);
                result.add(newInvitation);
            }
        }

        return result;
    }

    /**
     * Creates a "join tenant" invitation if there is a tenant that the receiver
     * is not already a member of.
     * 
     * @param receiver
     * @return null if no suitable tenant could be found.
     */
    private Invitation createJoinTenantInvitation(User receiver) {
        Invitation result = null;

        // need to find a tenant of which the receiver is not already a member.
        String hql = "from Tenant t where t.admin <> :receiver and "
                + "not exists(from UserIsMemberOfTenant rel "
                + "where rel.tenant = t and rel.user = :receiver) order by rand()";

        Tenant joinedTenant = (Tenant) session.createQuery(hql).setEntity(
                "receiver", receiver).setMaxResults(1).uniqueResult();

        if (joinedTenant != null) {
            User sender = joinedTenant.getAdmin();

            result = new Invitation();
            result.setCreationDate(getRandomDate(true, true, SPAN_WEEK));
            result.setJoinTenant(joinedTenant);
            result.setReceiver(receiver);
            result.setSender(sender);
        }

        return result;
    }

    /**
     * Creates an "administrate workflow model" invitation if there is a
     * workflow model that is not already administrated by the receiver.
     * 
     * @param receiver
     * @return null if no suitable workflow model is found.
     */
    private Invitation createAdministrateWorkflowModelInvitation(User receiver) {
        Invitation result = null;

        // need to find a workflow model that is not already administrated by
        // the receiver.
        String hql = "from WorkflowModel w where w.tenant.admin <> :receiver and "
                + "not exists(from UserAdministratesWorkflowModel rel where "
                + "rel.workflowModel = w and rel.user = :receiver) order by rand()";

        WorkflowModel model = (WorkflowModel) session.createQuery(hql)
                .setEntity("receiver", receiver).setMaxResults(1)
                .uniqueResult();

        if (model != null) {
            User sender = model.getTenant().getAdmin();

            result = new Invitation();
            result.setCreationDate(getRandomDate(true, true, SPAN_WEEK));
            result.setAdministrateWorkflowModel(model);
            result.setReceiver(receiver);
            result.setSender(sender);
        }

        return result;
    }

    /**
     * Creates a "participate in workflow" invitation if there is a workflow
     * instance in which the user does not already participate.
     * 
     * @return null if no suitable workflow instance is found.
     */
    private Invitation createParticipateInWorkflowInvitation(User receiver) {
        Invitation result = null;

        // 50% chance to invite for an instance that has not yet been started.
        boolean waiting = rnd.nextBoolean();

        String hql;
        if (waiting) {
            hql = "from WorkflowInstance wi where "
                    + "wi.startedDate is null and wi.completedDate is null "
                    + "order by rand()";
        } else {
            hql = "from WorkflowInstance wi where "
                    + "wi.startedDate is not null and wi.completedDate is null "
                    + "order by rand()";
        }

        WorkflowInstance instance = (WorkflowInstance) session.createQuery(hql)
                .setMaxResults(1);

        if (instance != null) {
            User sender = instance.getDeployedWorkflowModel().getTenant()
                    .getAdmin();

            result = new Invitation();
            result.setCreationDate(getRandomDate(true, true, SPAN_WEEK));
            result.setParticipateInWorkflowInstance(instance);
            result.setReceiver(receiver);
            result.setSender(sender);

            if (!waiting) {
                // gotta associate the user with the workflow instance now!
                UserParticipatesInWorkflow rel = new UserParticipatesInWorkflow();
                rel.setUser(receiver);
                rel.setWorkflowInstance(instance);
                session.saveOrUpdate(rel);
            }
        }

        return result;
    }
}
