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
 * TODO: add comment
 * 
 * @author Jonas Schlaak
 */
public class DateFormatter {

    private static DateTimeFormat formatter = DateTimeFormat
            .getFormat(ModelingToolWidget.messages.dateFormat());

    public static String format(Date date) {
        return formatter.format(date);
    }

    public static String getToday() {
        return formatter.format(new Date());
    }

    public static boolean validate(String date) {
        boolean result = true;
        if (formatter.parseStrict(date, 0, new Date()) == 0) {
            result = false;
        }
        return result;
    }

}
