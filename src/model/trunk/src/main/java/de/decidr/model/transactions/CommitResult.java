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

package de.decidr.model.transactions;

/**
 * Bean containing the result of a commit.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class CommitResult {

    private boolean finalCommit = false;
    private Exception committedEventException = null;

    /**
     * Creates a new {@link CommitResult} using default values.
     */
    public CommitResult() {
        this(false, null);
    }

    /**
     * Creates a new {@link CommitResult}
     * 
     * @param finalCommit
     *            whether the outmost transaction was committed
     * @param committedEventException
     *            the exception that broke the transactionCommited event chain
     *            (can be <code>null</code> if everything was OK)
     */
    public CommitResult(boolean finalCommit, Exception committedEventException) {
        super();
        this.finalCommit = finalCommit;
        this.committedEventException = committedEventException;
    }

    /**
     * @return the exception that broke the transactionCommited event chain (can
     *         be <code>null</code> if everything was OK)
     */
    public Exception getCommittedEventException() {
        return committedEventException;
    }

    /**
     * @return whether the outmost transaction was committed
     */
    public boolean isFinalCommit() {
        return finalCommit;
    }

    /**
     * @param committedEventException
     *            the exception that broke the transactionCommited event chain
     *            (can be <code>null</code> if everything was OK)
     */
    final void setCommittedEventException(Exception committedEventException) {
        this.committedEventException = committedEventException;
    }

    /**
     * @param finalCommit
     *            whether the outmost transaction was committed
     */
    final void setFinalCommit(boolean finalCommit) {
        this.finalCommit = finalCommit;
    }

}
