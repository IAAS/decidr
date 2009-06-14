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

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * TODO: add comment
 * 
 * @author JS
 */
@SuppressWarnings("serial")
public class Variable extends BaseModelData {

    /* Field names */
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
        // TODO: think this over, consider add variable in variable editor, too
        super();
        set(NAME, "variable name");
        set(TYPE, VariableType.STRING);
        set(TYPELOCALNAME, VariableType.STRING.getLocalName());
        set(VALUE, "variable value");
        set(ARRAYVAR, false);
        set(CONFIGVAR, false);
    }

    /**
     * TODO: add comment
     * 
     * Default constructor for a single non-configuration Variable
     */
    public Variable(String name, VariableType type, String value) {
        this.set(NAME, name);
        this.set(TYPE, type);
        this.set(TYPELOCALNAME, type.getLocalName());
        this.set(VALUE, value);
        this.set(ARRAYVAR, false);
        this.set(CONFIGVAR, false);
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
        ArrayList<String> values = new ArrayList<String>();
        if (isArray()) {
            values = get(VALUE);
        } else {
            values.add((String) get(VALUE));
        }
        return values;
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
