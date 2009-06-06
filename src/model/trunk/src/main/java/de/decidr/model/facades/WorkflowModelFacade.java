package de.decidr.model.facades;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.lf5.LogLevel;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.ObjectProperty;

import de.decidr.model.commands.workflowmodel.GetWorkflowAdminsCommand;
import de.decidr.model.commands.workflowmodel.GetWorkflowModelCommand;
import de.decidr.model.commands.workflowmodel.MakeWorkflowModelExecutableCommand;
import de.decidr.model.commands.workflowmodel.PublishWorkflowModelsCommand;
import de.decidr.model.commands.workflowmodel.SaveWorkflowModelCommand;
import de.decidr.model.entities.User;
import de.decidr.model.entities.UserProfile;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.filters.Filter;
import de.decidr.model.filters.Paginator;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.HibernateTransactionCoordinator;

/**
 * Provides an interface for retrieving and modifying (deployed) workflow
 * models.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class WorkflowModelFacade extends AbstractFacade {

    /**
     * Constructor
     * 
     * @param actor
     *            user / system that is using this facade to access workflow
     *            models.
     */
    public WorkflowModelFacade(Role actor) {
        super(actor);
    }

    /**
     * Saves the given workflow model.
     * 
     * expected properties of the given Vaadin item:
     * 
     * @throws TransactionException
     *             if the transaction is aborted for any reason.
     */
    public void saveWorkflowModel(Long workflowModelId, String name,
            String description, byte[] dwdl) throws TransactionException {

        HibernateTransactionCoordinator.getInstance().runTransaction(
                new SaveWorkflowModelCommand(actor, workflowModelId, name,
                        description, dwdl));
    }

    /**
     * Returns the properties of the given workflow model as a Vaadin Item.
     * 
     * @param workflowModelId
     * @return
     * @throws TransactionException
     *             if the transaction is aborted for any reason.
     */
    public Item getWorkflowModel(Long workflowModelId)
            throws TransactionException {

        String[] properties = { "id", "dwdl", "modifiedDate", "version",
                "name", "description", "creationDate", "published" };

        GetWorkflowModelCommand cmd = new GetWorkflowModelCommand(actor,
                workflowModelId);

        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);

        return new BeanItem(cmd.getResult(), properties);
    }

    /**
     * Makes the given workflow models available for import by other tenants. No
     * exception will be thrown if one or more of the given workflow models do
     * not exist.
     * 
     * @param workflowModelIds
     *            the workflow models to publish.
     * @throws TransactionException
     *             if the transaction is aborted for any reason.
     */
    public void publishWorkflowModels(List<Long> workflowModelIds)
            throws TransactionException {

        HibernateTransactionCoordinator.getInstance()
                .runTransaction(
                        new PublishWorkflowModelsCommand(actor,
                                workflowModelIds, true));
    }

    /**
     * Makes the given workflow models unavailable for import by other tenants.
     * No exception will be thrown if one or more of the given workflow models
     * do not exist.
     * 
     * @param workflowModelIds
     *            the workflow models to publish.
     * @throws TransactionException
     *             if the transaction is aborted for any reason.
     */
    public void unpublishWorkflowModels(List<Long> workflowModelIds)
            throws TransactionException {

        HibernateTransactionCoordinator.getInstance()
                .runTransaction(
                        new PublishWorkflowModelsCommand(actor,
                                workflowModelIds, false));
    }

    /**
     * Sets the executable flag of the given workflow model. A workflow model is
     * made executable by deploying it on the Apache ODE iff it isn't already
     * deployed.
     * 
     * Workflow instances can only be created from workflow models that are
     * executable.
     * 
     * @param workflowModelId
     *            the workflow model to deploy
     * @param excecutable
     * @throws TransactionException
     *             if the transaction is aborted for any reason.
     */
    public void setExecutable(Long workflowModelId, Boolean excecutable)
            throws TransactionException {

        HibernateTransactionCoordinator.getInstance().runTransaction(
                new MakeWorkflowModelExecutableCommand(actor, workflowModelId,
                        excecutable));
    }

    /**
     * Returns a list of all tenant members that administrate the given worfklow
     * model, excluding the tenant admin (who implicitly administrates all
     * workflow models and instances).
     * 
     * @param workflowModelId
     *            workflow model of which the administrators should be retrieved
     * @return List of administrators as Vaadin items.
     * @throws TransactionException
     *             if the transaction is aborted for any reason.
     */
    public List<Item> getWorkflowAdministrators(Long workflowModelId)
            throws TransactionException {
        ArrayList<Item> result = new ArrayList<Item>();

        GetWorkflowAdminsCommand cmd = new GetWorkflowAdminsCommand(actor,
                workflowModelId);

        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);

        String[] properties = { "id", "email" };

        for (User admin : cmd.getWorkflowAdmins()) {
            BeanItem adminItem = new BeanItem(admin, properties);

            UserProfile profile = admin.getUserProfile();

            if (profile != null) {
                adminItem.addItemProperty("username", new ObjectProperty(
                        profile.getUsername()));
                adminItem.addItemProperty("firstName", new ObjectProperty(
                        profile.getFirstName()));
                adminItem.addItemProperty("lastName", new ObjectProperty(
                        profile.getLastName()));
            }
        }

        return result;
    }

    public void setWorkflowAdministrators(Long workflowModelId,
            List<Long> userIds) throws TransactionException {
        throw new UnsupportedOperationException();
    }

    public void deleteWorkflowModels(List<Long> workflowModelIds)
            throws TransactionException {
        throw new UnsupportedOperationException();
    }

    public List<Item> getWorkflowInstances(Long workflowModelId)
            throws TransactionException {
        throw new UnsupportedOperationException();
    }

    public List<Item> getAllPublishedWorkflowModels(List<Filter> filters,
            Paginator paginator) throws TransactionException {
        throw new UnsupportedOperationException();
    }

    public Long startWorkflowInstance(Long workflowModelId,
            byte[] startConfiguration) throws TransactionException {
        throw new UnsupportedOperationException();
    }

    public Item getLastStartConfiguration(Long workflowModelId)
            throws TransactionException {
        throw new UnsupportedOperationException();
    }
}