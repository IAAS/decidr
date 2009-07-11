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

package de.decidr.odemonitor.service;

import java.math.BigInteger;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.Holder;

/**
 * RR: add comment
 * 
 * @author Reinhold
 */
public class ODEMonitorServiceImpl implements ODEMonitorService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.decidr.odemonitor.service.ODEMonitorService#getConfig(javax.xml.ws
     * .Holder, javax.xml.ws.Holder)
     */
    @Override
    public void getConfig(Holder<BigInteger> updateInterval,
            Holder<XMLGregorianCalendar> configChanged) {
        // RR Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see de.decidr.odemonitor.service.ODEMonitorService#registerODE(boolean)
     */
    @Override
    public String registerODE(boolean poolInstance) {
        // RR Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.decidr.odemonitor.service.ODEMonitorService#updateStats(java.math.
     * BigInteger, java.math.BigInteger, java.lang.String, javax.xml.ws.Holder,
     * javax.xml.ws.Holder)
     */
    @Override
    public void updateStats(BigInteger wfInstances, BigInteger wfModels,
            String odeID, Holder<XMLGregorianCalendar> configVersion,
            Holder<Boolean> run) {
        // RR Auto-generated method stub

    }
}
