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
package de.decidr.test.database.factories;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import de.decidr.model.entities.Invitation;
import de.decidr.model.entities.Tenant;
import de.decidr.model.entities.User;
import de.decidr.model.entities.UserParticipatesInWorkflow;
import de.decidr.model.entities.UserParticipatesInWorkflowId;
import de.decidr.model.entities.WorkflowInstance;
import de.decidr.model.entities.WorkflowModel;
import de.decidr.test.database.main.ProgressListener;

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
     * Pools for random selection.
     */
    private List<User> userPool = null;

    /**
     * Constructor
     * 
     * @param session
     */
    public InvitationFactory(Session session) {
        super(session);
    }

    /**
     * @param session
     * @param progressListener
     */
    public InvitationFactory(Session session, ProgressListener progressListener) {
        super(session, progressListener);
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
            result.setJoinTenant(null);
            result.setParticipateInWorkflowInstance(null);
            result.setReceiver(receiver);
            result.setSender(sender);
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
            result.setAdministrateWorkflowModel(null);
            result.setParticipateInWorkflowInstance(null);
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
                .setMaxResults(1).uniqueResult();

        if (instance != null) {
            User sender = instance.getDeployedWorkflowModel().getTenant()
                    .getAdmin();

            result = new Invitation();
            result.setCreationDate(getRandomDate(true, true, SPAN_WEEK));
            result.setParticipateInWorkflowInstance(instance);
            result.setAdministrateWorkflowModel(null);
            result.setJoinTenant(null);
            result.setReceiver(receiver);
            result.setSender(sender);

            if (!waiting) {
                // gotta associate the user with the workflow instance now!
                UserParticipatesInWorkflowId relId = new UserParticipatesInWorkflowId(
                        receiver.getId(), instance.getId());

                UserParticipatesInWorkflow existingRelation = (UserParticipatesInWorkflow) session
                        .get(UserParticipatesInWorkflow.class, relId);

                if (existingRelation == null) {
                    existingRelation = new UserParticipatesInWorkflow();
                    existingRelation.setUser(receiver);
                    existingRelation.setWorkflowInstance(instance);
                    existingRelation.setId(relId);
                    session.save(existingRelation);
                }
            }
        }

        return result;
    }

    /**
     * Creates up to numInvitations persisted Invitations spread evenly across
     * all invitation types.
     * 
     * @param numInvitations
     * @return persisted invitation entities
     */
    @SuppressWarnings("unchecked")
    public List<Invitation> createRandomInvitations(int numInvitations) {
        ArrayList<Invitation> result = new ArrayList<Invitation>();

        // fill random pools
        userPool = session.createQuery("from User u").list();
        if ((userPool == null) || userPool.isEmpty()) {
            throw new RuntimeException(
                    "need at least one user to create invitations.");
        }

        for (int i = 0; i < numInvitations; i++) {
            Invitation newInvitation;
            User randomUser = userPool.get(rnd.nextInt(userPool.size()));

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

            fireProgressEvent(numInvitations, i + 1);
        }

        return result;
    }
}