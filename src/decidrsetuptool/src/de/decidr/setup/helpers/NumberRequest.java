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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberRequest {

    public static String getResult(String question) {
        return getResult(question, "");
    }

    public static String getResult(String question, String defaultVal) {
        String number;

        while (true) {
            try {
                number = CoreRequest.getResult(question, defaultVal);
            } catch (IOException e) {
                System.out.println("An error occured. please try again.");
                continue;
            }

            if (validateNumber(number)) {
                break;
            } else {
                System.out.println("Please enter a valid number.");
            }
        }

        return "'" + number + "'";
    }

    /**
     * Checks weather String is a number or not.
     * 
     * @param mail
     * @return true if valid, else false
     */
    private static boolean validateNumber(String num) {

        // Set the email pattern string
        Pattern p = Pattern.compile("[0-9]+");

        // Match the given string with the pattern
        Matcher m = p.matcher(num);

        // check whether match is found
        boolean matchFound = m.matches();

        return matchFound || num.isEmpty();
    }
}
