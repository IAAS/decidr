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

package de.decidr.model.workflowmodel.instancemanagement;

import java.util.Date;

/**
 * Implements {@link PrepareInstanceResult}
 * 
 * @author Modood Alvi
 * @version 0.1
 */
public class StartInstanceResultImpl implements PrepareInstanceResult {

    private long serverId = 0l;
    private Date startDate = null;
    private String pid = null;

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.decidr.model.workflowmodel.instancemanagement.StartInstanceResult#
     * getServer()
     */
    @Override
    public long getServer() {
        return serverId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.decidr.model.workflowmodel.instancemanagement.StartInstanceResult#
     * getStartDate()
     */
    @Override
    public Date getStartDate() {
        return startDate;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.decidr.model.workflowmodel.instancemanagement.StartInstanceResult#
     * setServer(long)
     */
    @Override
    public void setServer(long server) {
        serverId = server;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.decidr.model.workflowmodel.instancemanagement.StartInstanceResult#
     * setStartDate(java.util.Date)
     */
    @Override
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Override
    public String getODEPid() {
        return pid;
    }

    @Override
    public void setODEPid(String pid) {
        this.pid = pid;
    }
}
