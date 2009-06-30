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

package de.decidr.modelingtool.client.io;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * The interface of the remote service. It provides the two methods for saving 
 * a DWDL into the server and loading a DWDL from the server.
 *
 * @uthor Modood Alvi
 * @version 0.1
 */
public interface DWDLIOService extends RemoteService {
    
    /**
     * TODO: add comment
     *
     * @param dwdl
     */
    public void save(String dwdl);
    
    /**
     * TODO: add comment
     *
     * @return
     */
    public String load();
    
    

}
