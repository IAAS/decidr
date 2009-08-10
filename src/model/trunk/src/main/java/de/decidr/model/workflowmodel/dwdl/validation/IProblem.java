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

/**
 * IProblem is a generic self contained "marker" or "note" or "message" that is
 * generated as a result of the validation pass. Instances of IProblem contain
 * descriptive information about the problem discovered by the validator.
 * 
 * @author Modood Alvi
 * @version 0.1
 */
public interface IProblem {
    
    /**
     * Returns description of the error
     * 
     * @return readable description
     */
    public String getErrorDescription();
    
    /**
     * Returns the position of the error, e.g line 12 or
     * variablename
     * 
     * @return position of the error
     */
    public String getErrorPosition();
    
}
