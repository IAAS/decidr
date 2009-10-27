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

package de.decidr.setup.helpers;

import java.io.IOException;

/**
 * TODO: add comment
 * 
 * @author Johannes Engelhardt
 */
public class LoglevelRequest {

    public static String getResult(String question, String defaultVal) {
        question += " [WARN, ALL, DEBUG, FATAL, INFO, OFF]";
        
        String loglevel;
        
        while (true) {
            try {
                loglevel = CoreRequest.getResult(question, defaultVal);
            } catch (IOException e) {
                System.out.println("An error occured. please try again.");
                continue;
            }
            
            if (validateLoglevel(loglevel)) {
                break;
            } else {
                System.out.println("Please enter a valid log level.");
            }
        }
        
        return loglevel;
    }

    private static boolean validateLoglevel(String loglevel) {
        String ulog = loglevel.toUpperCase();
        return (ulog.equals("WARN") || ulog.equals("ALL")
                || ulog.equals("DEBUG") || ulog.equals("FATAL")
                || ulog.equals("INFO") || ulog.equals("OFF"));
    }
}
