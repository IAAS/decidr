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
import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModelData;

import de.decidr.modelingtool.client.model.IdGenerator;

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
     * Creates a new non-configuration variable and sets the id. The new
     * variable has one empty string array as values. Note that regardless of
     * the variable type, the values are stored internally always as strings.
     */
    public Variable() {
        super();

        set(ID, IdGenerator.generateId());

        ArrayList<String> values = new ArrayList<String>();
        setValues(values);

        setType(VariableType.STRING);

        setConfig(false);
    }

    public Variable(String label, VariableType type, Boolean config) {
        this();
        this.setLabel(label);
        this.setType(type);
        this.setConfig(config);
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
     * Sets the id of the variable. The uniqueness of the id will not be tested,
     * i.e. the caller of this method has to be sure that the id given to this
     * method is not assigned elsewhere.
     * 
     * @param id
     *            the id to set
     */
    public void setId(Long id) {
        set(ID, id);
        if (id > IdGenerator.generateId()) {
            IdGenerator.setHighestId(id);
        }
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
     * Returns whether the variable is a configuration variable or not.
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
