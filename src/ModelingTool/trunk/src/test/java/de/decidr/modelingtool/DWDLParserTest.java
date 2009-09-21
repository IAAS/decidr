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

package de.decidr.modelingtool;

import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import de.decidr.modelingtool.client.io.DWDLParser;
import de.decidr.modelingtool.client.io.DWDLParserImpl;
import de.decidr.modelingtool.client.model.WorkflowModel;

/**
 * TODO: add comment
 * 
 * @author Jonas Schlaak
 */
public class DWDLParserTest {

    private StringBuilder dwdl;
    private DWDLParser parser;
    private WorkflowModel model;

    @BeforeClass
    public void init() {
        FileReader reader = null;
        try {
            reader = new FileReader("sample_dwdl");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int c;
        try {
            while ((c = reader.read()) != -1) {
                dwdl.append((char) c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        parser = new DWDLParserImpl();
        model = parser.parse(dwdl.toString());
    }

    @Test
    public void workflowModel() {
        assertTrue(model.getId() != null);
    }
}
