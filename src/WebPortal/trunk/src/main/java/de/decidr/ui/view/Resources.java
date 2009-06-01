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

package de.decidr.ui.view;

/**
 * TODO: add comment
 *
 * @author GH
 */
import com.vaadin.terminal.ExternalResource;

public class Resources {

        private static Resources resources = null;
        
        private ExternalResource imgDecidrLogo = null;
        
        private Resources(){
                //nothing to do yet
        }
        

        public static Resources getInstance(){
        if(resources == null){
                resources = new Resources();
        }
        return resources;
    }
        
        
        public ExternalResource getDecidrLogo(){
                if(imgDecidrLogo == null){
                        imgDecidrLogo = new ExternalResource("img/decidrlogo.png");
                }
                return imgDecidrLogo;
        }
}
