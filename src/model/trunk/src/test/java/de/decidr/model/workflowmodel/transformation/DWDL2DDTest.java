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

package de.decidr.model.workflowmodel.transformation;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import de.decidr.model.workflowmodel.dwdl.transformation.DWDL2DD;
import de.decidr.model.workflowmodel.webservices.DecidrWebserviceAdapter;


public class DWDL2DDTest {
    
    static DWDL2DD translater = null;
    static de.decidr.model.workflowmodel.bpel.Process bpel = null;
    Map<String, DecidrWebserviceAdapter> webservices = null;


    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        
        translater = new DWDL2DD();
        
        
    }

    /**
     * Test method for {@link de.decidr.model.workflowmodel.dwdl.transformation.DWDL2DD#getDD(de.decidr.model.workflowmodel.bpel.Process, java.util.Map)}.
     */
    @Test
    public void testGetDD() {
        
        translater.getDD(bpel, webservices);
    }

}
