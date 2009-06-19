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
package de.decidr.webservices.humantask;

import javax.jws.WebService;

import de.decidr.model.soap.types.TaskIdentifier;

/**
 * This is a temporary interface for the BEPL callback method web service
 * interface. It will be replaced by whatever the BEPL libraries offer as soon
 * as they do.
 * 
 * @author Reinhold
 */
@WebService(name = "BPELCallback")
public interface BPELCallbackInterface {

    public void taskCompleted(TaskIdentifier id, String data);
}
