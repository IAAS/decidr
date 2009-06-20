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

package de.decidr.model.commands.user;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Map;

import de.decidr.model.entities.User;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Sets one or more properties of the given user.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class SetUserPropertyCommand extends UserCommand {

    private Map<String, ? extends Serializable> properties = null;

    /**
     * Creates a new SetUserPropertyCommand.
     * 
     * @param role
     * @param userId
     * @param properties
     *            maps property names to the new Values.
     */
    public SetUserPropertyCommand(Role role, Long userId,
            Map<String, ? extends Serializable> properties) {
        super(role, userId);
        if (properties == null) {
            throw new NullPointerException();
        }
        this.properties = properties;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        User user = new User();
        user.setId(getUserId());
        //use reflection to set the desired user properties
        try {
            PropertyDescriptor[] descriptors = Introspector.getBeanInfo(
                    User.class).getPropertyDescriptors();

            for (java.util.Map.Entry<String, ? extends Serializable> entry : properties
                    .entrySet()) {

                Method setter = findSetter(entry.getKey(), descriptors);
                if (setter != null) {
                    setter.invoke(user, entry.getValue());
                }
            }
            
            evt.getSession().update(user);
        } catch (Exception e) {
            throw new TransactionException(e);
        }
    }

    /**
     * Finds the setter method of the given property in the given set of
     * property descriptors
     * 
     * @param propertyName
     * @param descriptors
     * @return the setter method or null if the property is not writable / does
     *         not exist.
     */
    private Method findSetter(String propertyName,
            PropertyDescriptor[] descriptors) {
        for (PropertyDescriptor descriptor : descriptors) {
            if (propertyName.equals(descriptor.getName())) {
                return descriptor.getWriteMethod();
            }
        }
        // the property wasn't found.
        return null;
    }

}