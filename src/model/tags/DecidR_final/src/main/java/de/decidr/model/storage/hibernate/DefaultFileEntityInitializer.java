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

package de.decidr.model.storage.hibernate;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.entities.File;

/**
 * Initializes a file entity which is implemented as a {@link File}. Since the
 * {@link File} entity contains both metadata and file data, it is usually not a
 * good idea to set the "createEntity" property to true when using the
 * {@link File} with {@link HibernateEntityStorageProvider}.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class DefaultFileEntityInitializer implements FileEntityInitializer {

    @Override
    public void initEntity(Object entity) {
        File file = (File) entity;
        file.setCreationDate(DecidrGlobals.getTime().getTime());
        file.setFileName("file.tmp");
        file.setMayPublicDelete(false);
        file.setMayPublicRead(false);
        file.setMayPublicReplace(false);
        file.setMimeType("application/octet-stream");
        file.setTemporary(true);
    }
}
