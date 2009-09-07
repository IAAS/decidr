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

package de.decidr.modelingtool.client.model.variable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModelData;

import de.decidr.modelingtool.client.ModelingToolWidget;

/**
 * This is the model of variable for a workflow.
 * 
 * @author Jonas Schlaak
 */
@SuppressWarnings("serial")
public class Variable extends BaseModelData {

    /* Field names */
    public static final String ID = "id";
    public static final String LABEL = "label";
    public static final String TYPE = "type";
    public static final String TYPELOCALNAME = "typelocalname";
    public static final String VALUE = "value";
    public static final String ARRAYVAR = "array";
    public static final String CONFIGVAR = "config";

    /**
     * Creates a new non-configuration variable. The type is string. The new
     * variable has one empty string has single value.
     * 
     * WARNING: because the id of a variable is actually the current timestamp
     * of when this constructor is called, it is advised to use this constructor
     * cautiously in order to prevent that two variables share the same id.
     */
    public Variable() {
        super();
        set(ID, new Date().getTime());
        set(LABEL, ModelingToolWidget.messages.enterVariableName());
        set(TYPE, VariableType.STRING);
        set(TYPELOCALNAME, VariableType.STRING.getLocalName());
        ArrayList<String> values = new ArrayList<String>();
        values.add("");
        set(VALUE, values);
        setArray();
        set(CONFIGVAR, false);
    }

    /**
     * This method creates a copy of the variable, including id, label, values
     * etc. It does basically the same as you would expect from a clone method.
     * The method was named copy to avoid inheriting from the super class method
     * (which throws some exceptions which are inconvenient to deal with).
     * 
     * @return a copy of the variable
     */
    public Variable copy() {
        Variable copy = new Variable();
        copy.setId(this.getId());
        copy.setLabel(this.getLabel());
        copy.setType(this.getType());
        copy.setValues(this.getValues());
        copy.setArray();
        copy.setConfig(this.isConfig());
        return copy;
    }

    /**
     * Returns the id of the variable.
     * 
     * @return the Id
     */
    public Long getId() {
        return (Long) get(ID);
    }

    /**
     * Sets the id of the variable.
     * 
     * @param id
     *            the id to set
     */
    public void setId(Long id) {
        set(ID, id);
    }

    /**
     * Returns the label of the variable.
     * 
     * @return the label
     */
    public String getLabel() {
        return get(LABEL);
    }

    /**
     * Sets the label of the variable
     * 
     * @param label
     *            the label to set
     */
    public void setLabel(String label) {
        set(LABEL, label);
    }

    /**
     * Returns the type of the variable.
     * 
     * @return the type
     */
    public VariableType getType() {
        return get(TYPE);
    }

    /**
     * Sets the type of the variable.
     * 
     * @param type
     *            the type to set
     */
    public void setType(VariableType type) {
        set(TYPE, type);
        set(TYPELOCALNAME, type.getLocalName());
    }

    /**
     * Returns the values of the variable as list of strings.
     * 
     * @return the values
     */
    public List<String> getValues() {
        return get(VALUE);
    }

    /**
     * Sets the values of the variable.
     * 
     * @param values
     *            the values to set
     */
    public void setValues(List<String> values) {
        set(VALUE, values);
        setArray();
    }

    /**
     * Returns whether the variable is an array or not by determining the size
     * of the value list.
     * 
     * @return the array
     */
    public boolean isArray() {
        Boolean result = false;
        if (getValues().size() > 1) {
            result = true;
        }
        return result;
    }

    /**
     * Sets the internal property field to whether the variable is an array or
     * not. The values of the internal property fields are used by the grid of
     * the variable editor. The method is private because is only needs to be
     * called when the values of the variable are updated.
     * 
     * @param array
     *            the array to set
     */
    private void setArray() {
        set(ARRAYVAR, isArray());
    }

    /**
     * Returns whether the variable is a configurations variable or not.
     * 
     * @return the config boolean
     */
    public Boolean isConfig() {
        return get(CONFIGVAR);
    }

    /**
     * Sets whether the variable should be configuration variable or not.
     * 
     * @param config
     *            the config boolean to set
     */
    public void setConfig(boolean config) {
        set(CONFIGVAR, config);
    }

}
