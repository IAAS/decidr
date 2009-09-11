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

package de.decidr.test.database.factories;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.hibernate.Session;

/**
 * Creates useful XML test data (or stubs) using existing database contents.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class XmlFactory extends EntityFactory {

    /**
     * Constructor
     * 
     * @param session
     *            current Hibernate session
     */
    public XmlFactory(Session session) {
        super(session);
    }

    /**
     * @return a random start configuration as raw xml bytes
     */
    public byte[] createStartConfiguration() {
        return getBlobStub();
        // DH implement me!
    }

    /**
     * @return the DWDL of a sample process as raw xml data
     */
    public byte[] getDwdl() {
        return getRawData("processes/sampleProcess.xml");
    }

    /**
     * @param activityName
     *            name of the activity such as "email" or "humanTask"
     * @return raw xml data of the activity mapping
     */
    public byte[] getActivityMapping(String activityName) {
        // convention: all activity mapping files have to be named
        // <activityname>.xml
        return getRawData("mappings/" + activityName + ".xml");
    }

    /**
     * @param string
     *            name of web service whose wsdl should be retrieved
     * @return raw xml data of the wsdl file
     */
    public byte[] getWsdl(String webServiceName) {
        // convention: all wsdl files have to be named <webservicename>.wsdl
        return getRawData("wsdl/" + webServiceName + ".wsdl");
    }

    /**
     * Returns the raw bytes of the resource identified by the given resource
     * path.
     * 
     * @param relativePath
     *            file path relative to "de.decidr.test.database.resources"
     *            Example: "processes.sampleProcess.xml"
     * @return raw resource bytes
     */
    private byte[] getRawData(String relativePath) {
        if (!relativePath.startsWith("/")) {
            relativePath = "/" + relativePath;
        }

        InputStream inStream = getClass().getClassLoader().getResourceAsStream(
                "de/decidr/test/database/resources" + relativePath);

        if (inStream == null) {
            throw new IllegalArgumentException("Cannot find resource "
                    + relativePath);
        }

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        try {
            IOUtils.copy(inStream, outStream);
        } catch (IOException e) {
            throw new IllegalArgumentException("Resource " + relativePath
                    + " is unreadable");
        }

        return outStream.toByteArray();
    }

}
