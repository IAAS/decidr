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

package de.decidr.model.workflowmodel.dwdl.translator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * TODO: add comment
 * 
 * @author Modood Alvi
 * @version 0.1
 */
public final class BPELConstants {

    public static final String WSDLIMPORTTYPE = "http://schemas.xmlsoap.org/wsdl/";
    public static final String XSDIMPORTTYPE = "http://www.w3.org/2001/XMLSchema";
    public static final String DECIDRTYPESNAMESPACE = "http://decidr.de/schema/DecidrTypes";
    public static final String HTWSNAMESPACE = "http://decidr.de/webservices/HumanTask";
    public static final String EWSNAMESPACE = "http://decidr.de/webservices/Email";
    public static final String EWSPL = "Email-PL";
    public static final String HTWSPL = "HumanTask-PL";
    public static final String EWSPLT = "Email-PLT";
    public static final String HTWSPLT = "HumanTask-PLT";
    public static final String HTWSLOCATION = "HumanTask.wsdl";
    public static final String EWSLOCATION = "Email.wsdl";

    private BPELConstants() {
        // do nothing
    }

    public static Properties properties = new Properties();
    static {
        FileInputStream inStream = null;
        try {
            inStream = new FileInputStream("p.properties");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            properties.load(inStream);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
