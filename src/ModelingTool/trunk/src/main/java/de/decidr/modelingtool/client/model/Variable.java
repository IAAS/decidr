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

package de.decidr.modelingtool.client.model;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * TODO: add comment
 * 
 * @author JS
 */
@SuppressWarnings("serial")
public class Variable extends BaseModelData {

    private String name;
    private VariableType type;
    private List<String> values;
    private boolean array;
    private boolean configVariable;

    /**
     * 
     * TODO: add comment
     * 
     */
    public Variable() {
        super();
        this.name = new String("");
        this.type = VariableType.STRING;
        this.values = new ArrayList<String>();
        this.values.add("");
        this.array = false;
        this.configVariable = false;
    }

    /**
     * TODO: add comment
     * 
     * Default constructor for a single non-configuration Variable
     */
    public Variable(String name, VariableType type, String value) {
        this.name = name;
        this.type = type;
        this.values = new ArrayList<String>();
        this.values.add(value);
        this.array = false;
        this.configVariable = false;
    }

    /**
     * TODO: add comment
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * TODO: add comment
     * 
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * TODO: add comment
     * 
     * @return the type
     */
    public VariableType getType() {
        return type;
    }

    /**
     * TODO: add comment
     * 
     * @param type
     *            the type to set
     */
    public void setType(VariableType type) {
        this.type = type;
    }

    /**
     * TODO: add comment
     * 
     * @return the values
     */
    public List<String> getValues() {
        return values;
    }

    /**
     * TODO: add comment
     * 
     * @param values
     *            the values to set
     */
    public void setValues(List<String> values) {
        this.values = values;
    }

    /**
     * TODO: add comment
     * 
     * @return the array
     */
    public boolean isArray() {
        return array;
    }

    /**
     * TODO: add comment
     * 
     * @param array
     *            the array to set
     */
    public void setArray(boolean array) {
        this.array = array;
    }

    /**
     * TODO: add comment
     * 
     * @return the configVariable
     */
    public boolean isConfigVariable() {
        return configVariable;
    }

    /**
     * TODO: add comment
     * 
     * @param configVariable
     *            the configVariable to set
     */
    public void setConfigVariable(boolean configVariable) {
        this.configVariable = configVariable;
    }

}
