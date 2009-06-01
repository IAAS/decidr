package de.decidr.model.facades;

import java.util.List;

import com.vaadin.data.Item;

import de.decidr.model.commands.workflowmodel.SaveWorkflowModelCommand;
import de.decidr.model.exceptions.PropertyMissingException;
import de.decidr.model.exceptions.PropertyTypeException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.filters.Filter;
import de.decidr.model.filters.Paginator;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.HibernateTransactionCoordinator;

/**
 * Provides an interface for retrieving and modifying (deployed) workflow
 * models.
 * 
 * @author Markus Fischer
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
     * <ul>
     * <li>id - Long</li>
     * <li>name - String</li>
     * <li>description - String</li>
     * <li>dwdl - byte[]</li>
     * </ul>
     * 
     * @param workflowModel
     * @throws TransactionException
     *             on rollback or if the given workflow model doesn't exist.
     */
    public void saveWorkflowModel(Item workflowModel)
            throws TransactionException {

        // FIXME This is ugly. Are we going to do this every time an item is passed?
        try {
            Long id = (Long) workflowModel.getItemProperty("id").getValue();
            String name = (String) workflowModel.getItemProperty("name")
                    .getValue();
            String description = (String) workflowModel.getItemProperty(
                    "description").getValue();
            byte[] dwdl = (byte[]) workflowModel.getItemProperty("dwdl")
                    .getValue();

            HibernateTransactionCoordinator.getInstance().runTransaction(
                    new SaveWorkflowModelCommand(actor, id, name, description,
                            dwdl));

        } catch (NullPointerException npe) {
            throw new PropertyMissingException(npe);
        } catch (ClassCastException cce) {
            throw new PropertyTypeException(cce);
        }
    }

    public Item getWorkflowModel(Long workflowModelId)
            throws TransactionException {
        throw new UnsupportedOperationException();
    }

    public void publishWorkflowModels(List<Long> workflowModelIds)
            throws TransactionException {
        throw new UnsupportedOperationException();
    }

    public void unpublishWorkflowModels(List<Long> workflowModelIds)
            throws TransactionException {
        throw new UnsupportedOperationException();
    }

    public void setExecutable(Long workflowModelId, Boolean excecutable)
            throws TransactionException {
        throw new UnsupportedOperationException();
    }

    public List<Item> getWorkflowAdministrators(Long workflowModelId)
            throws TransactionException {
        throw new UnsupportedOperationException();
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