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

package de.decidr.model.acl.asserters;

import de.decidr.model.acl.permissions.CommandPermission;
import de.decidr.model.acl.permissions.Permission;
import de.decidr.model.commands.AbstractTransactionalCommand;
import de.decidr.model.commands.TransactionalCommand;

/**
 * Abstract base class for asserters that handle {@link CommandPermission}s. The
 * purpose of this class is to ease access to the command instance
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public abstract class CommandAsserter extends AbstractTransactionalCommand
        implements Asserter {

    /**
     * @param permission
     * @return The {@link TransactionalCommand} <b>instance</b> or null if the
     *         given permission is not a {@link CommandPermission}
     */
    public TransactionalCommand getCommandInstance(Permission permission) {
        TransactionalCommand result = null;
        if (permission instanceof CommandPermission) {
            TransactionalCommand instance = ((CommandPermission) permission)
                    .getCommand();
            result = instance;
        }
        return result;
    }
}
