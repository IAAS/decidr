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

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.XMLParser;

import de.decidr.modelingtool.client.io.WorkflowParser;
import de.decidr.modelingtool.client.io.WorkflowParserImpl;
import de.decidr.modelingtool.client.model.WorkflowModel;

/**
 * TODO: add comment
 * 
 * @author Jonas Schlaak
 */
public class WorkflowParserImplTest extends GWTTestCase {

    private WorkflowModel model;
    private Document doc;
    private WorkflowParser parser;

    @Before
    public void init() {
        doc = XMLParser.createDocument();
        parser = new WorkflowParserImpl();

    }

    @Test
    public void variablesAndRolesTest() {
        parser.parse(model);
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
