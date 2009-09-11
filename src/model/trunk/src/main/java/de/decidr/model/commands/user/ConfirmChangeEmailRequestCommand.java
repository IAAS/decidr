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

import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.ChangeEmailRequest;
import de.decidr.model.entities.User;
import de.decidr.model.exceptions.AuthKeyException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionEvent;

/**
 * 
 * The function in this class changes the email address of the given user iff
 * the given auth key is correct. If not an exception will be thrown.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class ConfirmChangeEmailRequestCommand extends UserCommand {

    private String requestAuthKey;
    private Long userId;

    /**
     * 
     * Creates a new ConfirmChangeEmailRequestCommand. This command changes the
     * email address of the given user iff the given auth key is correct. If not
     * an exception will be thrown. The request object will be deleted as all.
     * 
     * @param actor
     * @param userId
     * @param requestAuthKey
     */
    public ConfirmChangeEmailRequestCommand(Role actor, Long userId,
            String requestAuthKey) {
        super(actor, userId);

        this.requestAuthKey = requestAuthKey;

    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        User user = (User) evt.getSession().load(User.class, userId);
        ChangeEmailRequest request = user.getChangeEmailRequest();

        if (requestAuthKey.equals(request.getAuthKey())) {

            // change email address
            user.setEmail(request.getNewEmail());
            evt.getSession().update(user);
            evt.getSession().delete(request);

        } else {
            throw new AuthKeyException("AuthKey does not match");
        }

    }

}
