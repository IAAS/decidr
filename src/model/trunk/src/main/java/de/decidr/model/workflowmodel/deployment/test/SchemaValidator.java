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

package de.decidr.model.workflowmodel.deployment.test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBException;

import de.decidr.model.workflowmodel.dwdl.translator.TransformUtil;
import de.decidr.model.workflowmodel.dwdl.validation.IProblem;

/**
 * A simple schema validator
 * 
 * @author Modood Alvi
 */
public class SchemaValidator {

    /**
     * A sample application which shows how to perform a XML document
     * validation.
     */
    public static void main(String[] args) {
//        try {
//            // define the type of schema - we use W3C:
//            String schemaLang = "http://www.w3.org/2001/XMLSchema";
//
//            // get validation driver:
//            SchemaFactory factory = SchemaFactory.newInstance(schemaLang);
//
//            // create schema by reading it from an XSD file:
//            Schema schema = factory.newSchema(new StreamSource("dwdl.xsd"));
//            Validator validator = schema.newValidator();
//
//            // at last perform validation:
//            validator.validate(new StreamSource("sampleProcess.xml"));
//
//        } catch (SAXException ex) {
//            // we are here if the document is not valid:
//            ex.printStackTrace();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
    }
    

}
