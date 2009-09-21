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

import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gwt.junit.client.GWTTestCase;

import de.decidr.modelingtool.client.io.DWDLParser;
import de.decidr.modelingtool.client.io.DWDLParserImpl;
import de.decidr.modelingtool.client.model.WorkflowModel;

/**
 * TODO: add comment
 * 
 * @author Jonas Schlaak
 */
public class DWDLParserTest extends GWTTestCase {

 

    @Test
    public void testWorkflow() {
        assertTrue(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.google.gwt.junit.client.GWTTestCase#getModuleName()
     */
    @Override
    public String getModuleName() {
        return "de.decidr.modelingtool.ModelingTool";
    }
}
