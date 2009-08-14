package de.decidr.test.database.factories;

import java.nio.charset.Charset;
import java.util.List;

import org.hibernate.Session;

import de.decidr.model.entities.WorkflowInstance;

/**
 * Creates random workflow instances. To create a workflow instance at least
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
    public List<WorkflowInstance> createRandomWorkflowModels(int numInstances,
            int instancesPerModel) {
        //FIXME DH continue here
        return null;
    }


    /**
     * @return a (not so large) blob that contains the binary UTF-8
     *         representation of the string "empty".
     */
    private byte[] getBlobStub() {
        return "empty".getBytes(Charset.forName("UTF-8"));
    }
}
