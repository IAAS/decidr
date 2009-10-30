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

package de.decidr.setup.input;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import de.decidr.setup.helpers.BooleanRequest;
import de.decidr.setup.helpers.StringRequest;

/**
 * Provides a function to write a string to a file.
 * 
 * @author Johannes Engelhardt
 */
public class FileIO {

    public static boolean writeToFile(String data) {
        System.out.println("------------------------------------------------");
        System.out.println("Write settings to file");
        System.out.println("------------------------------------------------");

        while (true) {
            String filename = StringRequest.getString(
                    "Specify output filename", "decidr_setup.sql");
            
            if (write(filename, data)) {
                System.out.println("Wrote file successfully.");
                return true;
                
            } else if (BooleanRequest.getResult(
                    "Error: couldn't write file. Try again?", "yes") == "'0'") {
                return false;
            }
        }
    }

    private static boolean write(String filename, String data) {
        Writer fw = null;

        if (filename.isEmpty()) {
            return false;
        }

        try {
            fw = new FileWriter(filename);
            fw.write(data);
            return true;

        } catch (IOException e) {
            return false;

        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {/* do nothing */
                }
            }
        }
    }
}
