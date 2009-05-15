
package de.decidr.webservices.humantask;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the de.decidr.webservices.humantask package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: de.decidr.webservices.humantask
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link TaskCompleted }
     * 
     */
    public TaskCompleted createTaskCompleted() {
        return new TaskCompleted();
    }

    /**
     * Create an instance of {@link CreateTask }
     * 
     */
    public CreateTask createCreateTask() {
        return new CreateTask();
    }

    /**
     * Create an instance of {@link RemoveTaskResponse }
     * 
     */
    public RemoveTaskResponse createRemoveTaskResponse() {
        return new RemoveTaskResponse();
    }

    /**
     * Create an instance of {@link TaskCompletedResponse }
     * 
     */
    public TaskCompletedResponse createTaskCompletedResponse() {
        return new TaskCompletedResponse();
    }

    /**
     * Create an instance of {@link RemoveTasks }
     * 
     */
    public RemoveTasks createRemoveTasks() {
        return new RemoveTasks();
    }

    /**
     * Create an instance of {@link RemoveTasksResponse }
     * 
     */
    public RemoveTasksResponse createRemoveTasksResponse() {
        return new RemoveTasksResponse();
    }

    /**
     * Create an instance of {@link CreateTaskResponse }
     * 
     */
    public CreateTaskResponse createCreateTaskResponse() {
        return new CreateTaskResponse();
    }

    /**
     * Create an instance of {@link RemoveTask }
     * 
     */
    public RemoveTask createRemoveTask() {
        return new RemoveTask();
    }

}
