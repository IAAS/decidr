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

package de.decidr.model.workflowmodel.dwdl.validation;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom ErrorHandler for DWDL validation.
 * 
 * @author Geoffrey-Alexeij Heinze
 */
public class DWDLErrorHandler implements ErrorHandler {

    private List<IProblem> errorList = new ArrayList<IProblem>();

    @Override
    public void error(SAXParseException e) throws SAXException {
        addProblemToList(e);
    }

    @Override
    public void fatalError(SAXParseException e) throws SAXException {
        addProblemToList(e);
    }

    @Override
    public void warning(SAXParseException e) throws SAXException {
        addProblemToList(e);
    }

    /**
     * Returns the list with all errors. If no error occurred, null is returned
     * 
     * @return 
     *          List<IProblem>: List of all found problems
     */
    public List<IProblem> getProblemList() {
        return errorList;
    }

    /**
     * Adds a new Problem to the errorList.
     * 
     * @param e
     *          The exception object
     */
    private void addProblemToList(SAXParseException e) {
        String msg = e.getMessage();
        String loc = "line: " + e.getLineNumber();
        Problem p = new Problem(msg, loc);
        errorList.add(p);
    }
}
