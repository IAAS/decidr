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
    public static final String LABEL = "label";
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
     * TODO: add comment
     * 
     * Default constructor for a single non-configuration Variable
     */
    public Variable(String name, VariableType type, String value) {
        super();
        this.set(ID, new Date().getTime());
        this.set(LABEL, name);
        this.set(TYPE, type);
        this.set(TYPELOCALNAME, type.getLocalName());
        ArrayList<String> values = new ArrayList<String>();
        values.add(type.getDefaultValue());
        this.set(VALUE, values);
        this.setArray();
        this.set(CONFIGVAR, true);
    }

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
     * 
     * TODO: add comment
     * 
     * @return the ID
     */
    public Long getId() {
        return (Long) get(ID);
    }

    /**
     * 
     * TODO: add comment
     * 
     * @param id
     */
    public void setId(Long id) {
        set(ID, id);
    }

    /**
     * TODO: add comment
     * 
     * @return the name
     */
    public String getLabel() {
        return get(LABEL);
    }

    /**
     * TODO: add comment
     * 
     * @param label
     *            the name to set
     */
    public void setLabel(String label) {
        set(LABEL, label);
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
        setArray();
    }

    /**
     * TODO: add comment
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
     * TODO: add comment
     * 
     * @param array
     *            the array to set
     */
    private void setArray() {
        set(ARRAYVAR, isArray());
    }

    /**
     * TODO: add comment
     * 
     * @return the configVariable
     */
    public Boolean isConfig() {
        return get(CONFIGVAR);
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
