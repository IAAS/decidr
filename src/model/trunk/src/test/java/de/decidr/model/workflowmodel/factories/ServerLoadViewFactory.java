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

package de.decidr.model.workflowmodel.factories;

import java.util.ArrayList;
import java.util.List;

import de.decidr.model.entities.ServerLoadView;

/**
 * This class returns a list with an single {@link ServerLoadView} element
 *
 * @author Modood Alvi
 */
public class ServerLoadViewFactory {
   
    public static List<ServerLoadView> getServerStatistics(){
        
        List<ServerLoadView> statistics = new ArrayList<ServerLoadView>();
        ServerLoadView server = new ServerLoadView();
        server.setId(3l);
        server.setLoad((byte) 10);
        server.setLocation("");
        
        
        return null;
        
    }
    
    
    

}
