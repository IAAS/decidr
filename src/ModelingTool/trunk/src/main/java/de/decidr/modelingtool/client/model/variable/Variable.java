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
 * TODO: add comment
 * 
 * @author Jonas Schlaak
 */
@SuppressWarnings("serial")
public class Variable extends BaseModelData {

    /* Field names */
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String TYPE = "type";
    public static final String TYPELOCALNAME = "typelocalname";
    public static final String VALUE = "value";
    public static final String ARRAYVAR = "array";
    public static final String CONFIGVAR = "config";

    /**
     * 
     * 
     * TODO: add comment
     */
    public Variable() {
        super();
        set(ID, new Date().getTime());
        set(NAME, ModelingToolWidget.messages.enterVariableName());
        set(TYPE, VariableType.STRING);
        set(TYPELOCALNAME, VariableType.STRING.getLocalName());
        ArrayList<String> values = new ArrayList<String>();
        values.add(ModelingToolWidget.messages.newStringValue());
        set(VALUE, values);
        set(ARRAYVAR, false);
        set(CONFIGVAR, false);
    }

    /**
     * TODO: add comment
     * 
     * Default constructor for a single non-configuration Variable
     */
    public Variable(String name, VariableType type, String value) {
        super();
        this.set(ID, new Date().getTime());
        this.set(NAME, name);
        this.set(TYPE, type);
        this.set(TYPELOCALNAME, type.getLocalName());
        ArrayList<String> values = new ArrayList<String>();
        values.add(value);
        this.set(VALUE, values);
        this.set(ARRAYVAR, false);
        this.set(CONFIGVAR, true);
    }

    public Variable copy() {
        Variable copy = new Variable();
        copy.setId(this.getId());
        copy.setName(this.getName());
        copy.setType(this.getType());
        copy.setValues(this.getValues());
        copy.setArray(this.isArray());
        copy.setConfig(this.isConfig());
        return copy;
    }

    /**
     * 
     * TODO: add comment
     * 
     * @return the ID
     */
    public Long getId() {
        return (Long) get(ID);
    }

    public void setId(Long id) {
        set(ID, id);
    }

    /**
     * TODO: add comment
     * 
     * @return the name
     */
    public String getName() {
        return get(NAME);
    }

    /**
     * TODO: add comment
     * 
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        set(NAME, name);
    }

    /**
     * TODO: add comment
     * 
     * @return the type
     */
    public VariableType getType() {
        return get(TYPE);
    }

    /**
     * TODO: add comment
     * 
     * @param type
     *            the type to set
     */
    public void setType(VariableType type) {
        set(TYPE, type);
        set(TYPELOCALNAME, type.getLocalName());
    }

    /**
     * TODO: add comment
     * 
     * @return the values
     */
    public List<String> getValues() {
        /*
         * If the variable is an array, return the arraylist, else add the
         * single value to an empty arraylist
         */

        // JS check this, we don't have single types anymore
        return get(VALUE);
    }

    /**
     * TODO: add comment
     * 
     * @param values
     *            the values to set
     */
    public void setValues(List<String> values) {
        set(VALUE, values);
        setArray(true);
    }

    /**
     * TODO: add comment
     * 
     * @return the array
     */
    public boolean isArray() {
        return (Boolean) get(ARRAYVAR);
    }

    /**
     * TODO: add comment
     * 
     * @param array
     *            the array to set
     */
    private void setArray(boolean array) {
        set(ARRAYVAR, array);
    }

    /**
     * TODO: add comment
     * 
     * @return the configVariable
     */
    public boolean isConfig() {
        return (Boolean) get(CONFIGVAR);
    }

    /**
     * TODO: add comment
     * 
     * @param config
     *            the configVariable to set
     */
    public void setConfig(boolean config) {
        set(CONFIGVAR, config);
    }

}
