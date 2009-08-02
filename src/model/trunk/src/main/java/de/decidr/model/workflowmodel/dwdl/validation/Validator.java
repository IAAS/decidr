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

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.UserFacade;
import de.decidr.model.permissions.UserRole;
import de.decidr.model.workflowmodel.dwdl.TActor;
import de.decidr.model.workflowmodel.dwdl.TLiteral;
import de.decidr.model.workflowmodel.dwdl.TVariable;
import de.decidr.model.workflowmodel.dwdl.TWorkflow;

/**
 * This class provides the functionality to determine whether a given DWDL is
 * valid or not.
 * 
 * @author Modood Alvi
 * @version 0.1
 */
// MA: bitte hier drin mal <ctrl><shift><f> drücken!
public class Validator {

    private javax.xml.validation.Validator validator = null;
    private Schema schema = null;

    public Validator() {
        try {
            SchemaFactory sf = SchemaFactory
                    .newInstance("http://www.w3.org/2001/XMLSchema");
            schema = sf.newSchema(new StreamSource("dwdl.xsd"));

            validator = schema.newValidator();
        } catch (SAXException e) {
            // MA Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * GH testing only
     * @param dwdl
     * @return
     */
    public List<IProblem> validate(StreamSource dwdl) {
        DWDLErrorHandler errHandler = null;
        List<IProblem> errList = null;
        
        validator.setErrorHandler(new DWDLErrorHandler());
        try {
            validator.validate(dwdl);
        } catch (SAXException e) {
            // MA errList ist hier immer null
            if (errList == null) {
                errList = new ArrayList<IProblem>();
            }
            errList.add(new Problem(e.getMessage(),"global"));
        } catch (IOException e) {
            // MA errList ist hier immer null
            if (errList == null) {
                errList = new ArrayList<IProblem>();
            }
            errList.add(new Problem(e.getMessage(),"global"));
        }

        errHandler = (DWDLErrorHandler)(validator.getErrorHandler());
        errList = errHandler.getProblemList();
        
        //GH how to transform from byte[] to TWorkflow?
        //errList.addAll(checkVariables(dwdl));
        //errList.addAll(checkUsers(dwdl));
        
        return errList;
    }
    
    /**
     * MA: add comment
     * 
     * @param dwdl
     *            The DWDL workflow to validate
     * @return List of problems found during validation process.
     */
    public List<IProblem> validate(TWorkflow dwdl) {
        DWDLErrorHandler errHandler = null;
        
        //GH requires confirmation
        DOMSource dom = new DOMSource((Node) dwdl);
        List<IProblem> errList = null;
        
        validator.setErrorHandler(new DWDLErrorHandler());
        try {
            validator.validate(dom);
        } catch (SAXException e) {
            // MA errList ist hier immer null
            if (errList == null) {
                errList = new ArrayList<IProblem>();
            }
            errList.add(new Problem(e.getMessage(),"global"));
        } catch (IOException e) {
            // MA errList ist hier immer null
            if (errList == null) {
                errList = new ArrayList<IProblem>();
            }
            errList.add(new Problem(e.getMessage(),"global"));
        }

        errHandler = (DWDLErrorHandler)(validator.getErrorHandler());
        errList = errHandler.getProblemList();
        
        errList.addAll(checkVariables(dwdl));
        errList.addAll(checkUsers(dwdl));
        
        return errList;
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

    /**
     * Checks for all users, whether they are registered
     * 
     * @param dwdl
     *          TWorkflow - the actual DWDL
     * @return
     *          A list of Problems with unregistered users
     */
    private List<IProblem> checkUsers(TWorkflow dwdl){
        List<IProblem> userErr = new ArrayList<IProblem>();
        UserFacade userFacade = new UserFacade(new UserRole());
        //GH: where to find the users:
        //    - Actors
        //    - EMail Activity
        //    - Human Task
        
        for (Iterator<TActor> iter = dwdl.getRoles().getActor().listIterator(); iter.hasNext();){
            TActor actor = iter.next();
            try {
                if (!userFacade.isRegistered(actor.getUserId()) ){
                    userErr.add(new Problem("User " + actor.getName() + "("+ actor.getEmail()+ "" +
                    		"does not exist!", actor.getEmail()));
                }
            } catch (TransactionException e) {
                userErr.add(new Problem("Connection to Database failed!", "global"));
                break;
            }
        }
                
        return userErr;
    }
    
    /**
     * Checks for all variables, whether the value matches the
     * variable type and returns a list of all found errors.
     * 
     * @param dwdl
     *          TWorkflow - the actual DWDL
     * @return
     *          List with all variables (including an error
     *          description) where value and type mismatch
     */
    private List<IProblem> checkVariables(TWorkflow dwdl){
        List<IProblem> varErr = new ArrayList<IProblem>();
        SimpleDateFormat sdfD = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfT = new SimpleDateFormat("HH:mm:ss");
        
        // MA bitte delegiere den nächsten Task Tag an den, der das implementiert hat, wenn du das nicht warst
        // MA Gesame Klasse: schonmal was von 'nem "foreach" und "Generics" gehört?
        for (Iterator<TVariable> iter = dwdl.getVariables().getVariable().listIterator(); iter.hasNext();){
            TVariable tVar = iter.next();
            String type = tVar.getType();
            
            if (tVar.getInitialValue() != null){
                if (type.toLowerCase().equals("integer")){
                    if (tVar.getInitialValues() == null){
                        try{
                            Integer.parseInt(tVar.getInitialValue().toString());    
                        }catch (NumberFormatException e){
                            varErr.add(new Problem("value is not integer!",tVar.getName()));
                        }
                    }else{
                        for (Iterator<TLiteral> literals = tVar.getInitialValues().getInitialValue().iterator(); literals.hasNext();){
                            try{
                                Integer.parseInt(literals.next().toString());    
                            }catch (NumberFormatException e){
                                varErr.add(new Problem("value(s) not integer!",tVar.getName()));
                                break;
                            }
                        }
                    }
                    
                }else if(type.toLowerCase().equals("float")){
                    if (tVar.getInitialValues() == null){
                        try{
                            Float.parseFloat(tVar.getInitialValue().toString());    
                        }catch (NumberFormatException e){
                            varErr.add(new Problem("value is not float!",tVar.getName()));
                        }
                    }else{
                        for (Iterator<TLiteral> literals = tVar.getInitialValues().getInitialValue().iterator(); literals.hasNext();){
                            try{
                                Float.parseFloat(literals.next().toString());    
                            }catch (NumberFormatException e){
                                varErr.add(new Problem("value(s) not float!",tVar.getName()));
                                break;
                            }
                        }
                    }
                    
                }else if(type.toLowerCase().equals("string")){
                    //GH even required?
                    
                }else if(type.toLowerCase().equals("boolean")){
                    if (tVar.getInitialValues() == null){
                        if (!(tVar.getInitialValue().toString() == "true" || tVar.getInitialValue().toString() == "false")){
                            varErr.add(new Problem("value is not boolean!",tVar.getName()));
                        }
                    }else{
                        for (Iterator<TLiteral> literals = tVar.getInitialValues().getInitialValue().iterator(); literals.hasNext();){
                            if (!(literals.next().toString() == "true" || literals.next().toString() == "false")){
                                varErr.add(new Problem("value(s) not boolean!",tVar.getName()));
                                break;
                            }
                        }
                    }
                    
                }else if(type.toLowerCase().equals("date")){
                    if (tVar.getInitialValues() == null){
                        try{
                            sdfD.parse(tVar.getInitialValue().toString());    
                        } catch (ParseException e) {
                            varErr.add(new Problem("value is not a valid date!",tVar.getName()));
                        }
                    }else{
                        for (Iterator<TLiteral> literals = tVar.getInitialValues().getInitialValue().iterator(); literals.hasNext();){
                            try{
                                sdfD.parse(literals.next().toString());    
                            } catch (ParseException e) {
                                varErr.add(new Problem("value(s) are not a valid date!",tVar.getName()));
                                break;
                            }
                        }
                    }
                    
                }else if(type.toLowerCase().equals("time")){
                    if (tVar.getInitialValues() == null){
                        try{
                            sdfT.parse(tVar.getInitialValue().toString());    
                        } catch (ParseException e) {
                            varErr.add(new Problem("value is not a valid time!",tVar.getName()));
                        }
                    }else{
                        for (Iterator<TLiteral> literals = tVar.getInitialValues().getInitialValue().iterator(); literals.hasNext();){
                            try{
                                sdfT.parse(literals.next().toString());    
                            } catch (ParseException e) {
                                varErr.add(new Problem("value(s) are not a valid time!",tVar.getName()));
                                break;
                            }
                        }
                    }
                }
            }
        }
        
        return varErr;
    }
}
