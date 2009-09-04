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
 * Represents the resources which are used in the web portal to have
 * a central point where all resources are handled.
 *
 * @author Geoffrey-Alexeij Heinze
 */
import com.vaadin.terminal.ThemeResource;

public class Resources {

        private static Resources resources = null;
        
        private ThemeResource imgDecidrLogo = null;
        
        private Resources(){
                //nothing to do yet
        }
        

        /**
         * Returns an instance of this class
         *
         * @return resources
         */
        public static Resources getInstance(){
        if(resources == null){
                resources = new Resources();
        }
        return resources;
    }
        
        
        /**
         * Returns the decidr logo as a theme resource
         *
         * @return imgDecidrLogo
         */
        public ThemeResource getDecidrLogo(){
                if(imgDecidrLogo == null){
                        imgDecidrLogo = new ThemeResource("img/decidrlogo.png");
                }
                return imgDecidrLogo;
        }
}
