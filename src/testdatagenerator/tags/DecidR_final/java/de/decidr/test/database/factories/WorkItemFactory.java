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

import javax.xml.bind.JAXBException;

import org.hibernate.Session;

import de.decidr.model.entities.File;
import de.decidr.model.entities.User;
import de.decidr.model.entities.UserHasFileAccess;
import de.decidr.model.entities.UserHasFileAccessId;
import de.decidr.model.entities.WorkItem;
import de.decidr.model.entities.WorkItemContainsFile;
import de.decidr.model.entities.WorkItemContainsFileId;
import de.decidr.model.entities.WorkflowInstance;
import de.decidr.model.enums.WorkItemStatus;
import de.decidr.model.soap.types.DWDLSimpleVariableType;
import de.decidr.model.workflowmodel.dwdl.transformation.TransformUtil;
import de.decidr.model.workflowmodel.humantask.THumanTaskData;
import de.decidr.model.workflowmodel.humantask.TTaskItem;
import de.decidr.test.database.main.ProgressListener;

/**
 * Creates randomized work item for testing purposes. Requires users and started
 * workflow instances to be present.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class WorkItemFactory extends EntityFactory {

    private XmlFactory xmlFactory;

    /**
     * Constructor
     */
    public WorkItemFactory(Session session) {
        super(session);
        xmlFactory = new XmlFactory(session);
    }

    /**
     * @param session
     * @param progressListener
     */
    public WorkItemFactory(Session session, ProgressListener progressListener) {
        super(session, progressListener);
        xmlFactory = new XmlFactory(session);
    }

    /**
     * Creates up to numWorkItems workitems.
     * 
     * @param numWorkItems
     * @return persisted workitems
     */
    @SuppressWarnings("unchecked")
    public List<WorkItem> createRandomWorkItems(int numWorkItems) {
        ArrayList<WorkItem> result = new ArrayList<WorkItem>();

        WorkItemStatus[] statuses = WorkItemStatus.values();

        List<WorkflowInstance> availableInstances = session.createQuery(
                "from WorkflowInstance wi where "
                        + "wi.startedDate is not null and "
                        + "wi.completedDate is null").list();

        if ((availableInstances == null) || availableInstances.isEmpty()) {
            throw new RuntimeException("No available workflow instances found.");
        }
        try {
            THumanTaskData data = TransformUtil.bytesToHumanTask(xmlFactory
                    .getWorkItem());

            Long existingFileId = (Long) session.createQuery(
                    "select id from File where fileSizeBytes > 0")
                    .setMaxResults(1).uniqueResult();

            if (existingFileId == null) {
                throw new RuntimeException(
                        "Cannot find file id to link with work item data");
            }

            setFileId(data, existingFileId);

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
                    // Give the user access to the file
                    UserHasFileAccess access = new UserHasFileAccess();
                    access.setId(new UserHasFileAccessId(
                            randomParticipatingUser.getId(), existingFileId));
                    access.setFile((File) session.load(File.class,
                            existingFileId));
                    access.setMayDelete(true);
                    access.setMayRead(true);
                    access.setMayReplace(true);
                    access.setUser(randomParticipatingUser);
                    session.merge(access);

                    WorkItem item = new WorkItem();

                    item.setCreationDate(getRandomDate(true, true, SPAN_WEEK));
                    item.setData(TransformUtil.humanTaskToByte(data));
                    item.setDescription("A random work item created by the "
                            + "test data generator. Created in loop #"
                            + Integer.toString(i));
                    item.setName("Work item #" + Integer.toString(i));
                    item.setWorkflowInstance(randomWorkflowInstance);
                    item.setUser(randomParticipatingUser);
                    item.setStatus(statuses[rnd.nextInt(statuses.length)]
                            .toString());

                    session.save(item);

                    // Associate file with workitem
                    WorkItemContainsFile rel = new WorkItemContainsFile();
                    rel.setId(new WorkItemContainsFileId(item.getId(),
                            existingFileId));
                    rel
                            .setFile((File) session.load(File.class,
                                    existingFileId));
                    rel.setWorkItem(item);
                    session.merge(rel);

                    result.add(item);
                }

                fireProgressEvent(numWorkItems, i + 1);
            }
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    /**
     * Sets the value of all task items that are files to the given file id
     */
    private void setFileId(THumanTaskData data, long id) {
        for (Object o : data.getTaskItemOrInformation()) {
            if (o instanceof TTaskItem) {
                TTaskItem item = (TTaskItem) o;
                if (DWDLSimpleVariableType.ANY_URI.equals(item.getType())
                        && (item.getValue() != null)
                        && !"".equals(item.getValue())) {
                    item.setValue(Long.toString(id));
                }
            }
        }
    }
}