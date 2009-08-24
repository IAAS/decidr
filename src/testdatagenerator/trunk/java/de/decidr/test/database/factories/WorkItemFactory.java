package de.decidr.test.database.factories;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import de.decidr.model.entities.User;
import de.decidr.model.entities.WorkItem;
import de.decidr.model.entities.WorkflowInstance;
import de.decidr.model.enums.WorkItemStatus;
import de.decidr.test.database.main.ProgressListener;

/**
 * Creates randomized work item for testing purposes. Requires users and started
 * workflow instances to be present.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class WorkItemFactory extends EntityFactory {

    /**
     * Constructor
     */
    public WorkItemFactory(Session session) {
        super(session);
    }

    /**
     * @param session
     * @param progressListener
     */
    public WorkItemFactory(Session session, ProgressListener progressListener) {
        super(session, progressListener);
    }

    /**
     * Creates up to numWorkItems work items.
     * 
     * @param numWorkItems
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<WorkItem> createRandomWorkItems(int numWorkItems) {
        ArrayList<WorkItem> result = new ArrayList<WorkItem>();

        WorkItemStatus[] statuses = WorkItemStatus.values();

        List<WorkflowInstance> availableInstances = session.createQuery(
                "from WorkflowInstance wi where "
                        + "wi.startedDate is not null and "
                        + "wi.completedDate is null").list();

        if (availableInstances == null || availableInstances.isEmpty()) {
            throw new RuntimeException("No available workflow instances found.");
        }

        for (int i = 0; i < numWorkItems; i++) {
            WorkflowInstance randomWorkflowInstance = availableInstances
                    .get(rnd.nextInt(availableInstances.size()));

            String hql = "from User u where "
                    + "exists (from UserParticipatesInWorkflow rel where "
                    + "rel.workflowInstance = :instance and rel.user = u) order by rand()";
            User randomParticipatingUser = (User) session.createQuery(hql)
                    .setMaxResults(1).setEntity("instance",
                            randomWorkflowInstance).uniqueResult();

            if (randomParticipatingUser != null) {
                WorkItem item = new WorkItem();
                item.setCreationDate(getRandomDate(true, true, SPAN_WEEK));
                item.setData(getBlobStub());
                item
                        .setDescription("A random work item created by the test data generator. Created in loop #"
                                + Integer.toString(i));
                item.setName("Work item #" + Integer.toString(i));
                item.setWorkflowInstance(randomWorkflowInstance);
                item.setUser(randomParticipatingUser);
                item.setStatus(statuses[rnd.nextInt(statuses.length)]
                        .toString());

                session.save(item);
                result.add(item);

                fireProgressEvent(numWorkItems, i + 1);
            }
        }

        return result;
    }
}