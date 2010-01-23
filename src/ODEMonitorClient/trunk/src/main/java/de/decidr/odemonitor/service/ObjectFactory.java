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

package de.decidr.odemonitor.service;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the de.decidr.webservices.odemonitor package.
 * <p>
 * An ObjectFactory allows you to programmatically construct new instances of
 * the Java representation for XML content. The Java representation of XML
 * content can consist of schema derived interfaces and classes representing the
 * binding of schema type definitions, element declarations and model groups.
 * Factory methods for each of these are provided in this class.
 */
@XmlRegistry
public class ObjectFactory {

    /**
     * Create a new ObjectFactory that can be used to create new instances of
     * schema derived classes for package: de.decidr.webservices.odemonitor
     */
    public ObjectFactory() {
        // needed by JAXB
    }

    /**
     * Create an instance of {@link GetConfig }
     */
    public GetConfig createGetConfig() {
        return new GetConfig();
    }

    /**
     * Create an instance of {@link GetConfigResponse }
     */
    public GetConfigResponse createGetConfigResponse() {
        return new GetConfigResponse();
    }

    /**
     * Create an instance of {@link RegisterODE }
     */
    public RegisterODE createRegisterODE() {
        return new RegisterODE();
    }

    /**
     * Create an instance of {@link RegisterODEResponse }
     */
    public RegisterODEResponse createRegisterODEResponse() {
        return new RegisterODEResponse();
    }

    /**
     * Create an instance of {@link UnregisterODE }
     */
    public UnregisterODE createUnregisterODE() {
        return new UnregisterODE();
    }

    /**
     * Create an instance of {@link UnregisterODEResponse }
     */
    public UnregisterODEResponse createUnregisterODEResponse() {
        return new UnregisterODEResponse();
    }

    /**
     * Create an instance of {@link UpdateStats }
     */
    public UpdateStats createUpdateStats() {
        return new UpdateStats();
    }

    /**
     * Create an instance of {@link UpdateStatsResponse }
     */
    public UpdateStatsResponse createUpdateStatsResponse() {
        return new UpdateStatsResponse();
    }
}
