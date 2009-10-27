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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * TODO: add comment
 *
 * @author Johannes Engelhardt
 */
public class CoreRequest {

    private static BufferedReader console = new BufferedReader(
	    new InputStreamReader(System.in));

    public static String getResult(String question, String defaultVal) throws IOException {
        String msg;
        if (defaultVal.isEmpty()) {
            msg = question + ": ";
        } else {
            msg = question + " (" + defaultVal + "):";
        }
        
	System.out.print(msg);
	String read = console.readLine();
	
	if (!read.isEmpty()) {
	    return read;
	} else {
	    return defaultVal;
	}
    }
    
}
