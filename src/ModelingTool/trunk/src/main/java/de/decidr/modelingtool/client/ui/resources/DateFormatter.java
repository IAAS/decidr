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

package de.decidr.modelingtool.client.ui.resources;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;

import de.decidr.modelingtool.client.ModelingToolWidget;

/**
 * This class converts and validates values of type {@link Date} into the date
 * format defined by the dwdl xml schema.
 * 
 * @author Jonas Schlaak
 */
public class DateFormatter {

    private static DateTimeFormat formatter = DateTimeFormat
            .getFormat(ModelingToolWidget.getMessages().dateFormat());

    /**
     * Converts a {@link Date} into a formatted string (format is currently
     * yyyy-MM-dd).
     * 
     * @param date
     *            the date to be converted
     * @return the formated date string
     */
    public static String format(Date date) {
        return formatter.format(date);
    }

    /**
     * Returns the current date as formatted string
     * 
     * @return the formatted date string
     */
    public static String getToday() {
        return formatter.format(new Date());
    }

    /**
     * Validated a formatted string to see if the format is correct and the date
     * is plausible.
     * 
     * @param date
     *            the date to be validated
     * @return the validation result
     */
    public static boolean validate(String date) {
        boolean result = true;
        if (formatter.parseStrict(date, 0, new Date()) == 0) {
            result = false;
        }
        return result;
    }

}
