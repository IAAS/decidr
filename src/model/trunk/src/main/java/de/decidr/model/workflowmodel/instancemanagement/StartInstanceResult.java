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
 * Holds all relevant data created during invocation of the workflow model
 *
 * @author Modood Alvi
 * @version 0.1
 */
public interface StartInstanceResult {
    
    public long getServer();
    
    public Date getStartDate();
    
    public String getODEPid();
    
    public void setStartDate(Date startDate);
    
    public void setServer(long server);
    
    public void setODEPid(String pid);

}
