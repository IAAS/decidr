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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.util.ValidationEventCollector;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.dom4j.io.DocumentResult;
import org.xml.sax.SAXException;

import de.decidr.model.workflowmodel.dwdl.TWorkflow;

/**
 * This class provides the functionality to determine whether a given DWDL is
 * valid or not.
 * 
 * @author Modood Alvi
 * @version 0.1
 */
public class Validator {

    private JAXBContext context = null;
    // MA never use deprecated stuff
    private javax.xml.bind.Validator validator = null;
    private Marshaller marshaller = null;
    private Schema schema = null;

    public Validator() {
        try {
            context = JAXBContext.newInstance("");
            validator = context.createValidator();
            marshaller = context.createMarshaller();
            SchemaFactory sf = SchemaFactory
                    .newInstance("http://www.w3.org/2001/XMLSchema");
            schema = sf.newSchema(new File("dwdl.xsd"));

            marshaller.setSchema(schema);
        } catch (JAXBException e) {
            // MA Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // MA Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * MA: add comment
     * 
     * @param dwdl
     *            The DWDL workflow to validate
     * @return List of problems found during validation process.
     */
    public List<IProblem> validate(TWorkflow dwdl) {
        ValidationEventCollector vec = new ValidationEventCollector();
        List<IProblem> errorList = null;
        DocumentResult dr = new DocumentResult();

        // GH: jaxb2: validation only allowed when un/marshalling

        try {
            validator.setEventHandler(vec);

            validator.validate(dwdl);

            if (vec != null && vec.hasEvents()) {
                errorList = new ArrayList<IProblem>();
                for (ValidationEvent event : vec.getEvents()) {
                    String msg = event.getMessage();
                    String loc = "line: " + event.getLocator().getLineNumber();
                    Problem p = new Problem(msg, loc);
                    errorList.add(p);
                }
            }
            // redundant?
            vec.reset();
            marshaller.setEventHandler(vec);
            marshaller.marshal(dwdl, dr);
            if (vec != null && vec.hasEvents()) {
                errorList = new ArrayList<IProblem>();
                for (ValidationEvent event : vec.getEvents()) {
                    String msg = event.getMessage();
                    String loc = "line: " + event.getLocator().getLineNumber();
                    Problem p = new Problem(msg, loc);
                    errorList.add(p);
                }
            }
            dr = null;

        } catch (JAXBException e) {
            if (errorList == null) {
                errorList = new ArrayList<IProblem>();
                errorList.add(new Problem(e.getMessage(), "global"));
            }
        }

        return errorList;
    }

    /**
     * MA: add comment
     * 
     * @param dwdl
     *            The DWDL workflow to validate
     * @return List of problems found during validation process.
     */
    public List<IProblem> validate(byte[] dwdl) {
        return null;
    }

}
