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

package de.decidr.model.reveng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.cfg.reveng.DelegatingReverseEngineeringStrategy;
import org.hibernate.cfg.reveng.ReverseEngineeringSettings;
import org.hibernate.cfg.reveng.ReverseEngineeringStrategy;
import org.hibernate.cfg.reveng.TableIdentifier;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.ForeignKey;
import org.hibernate.mapping.MetaAttribute;

/**
 * Provides a customized reverse engineering strategy for the DecidR MySQL
 * database.
 * <p>
 * We have chosen a custom strategy over a set of XML files since programmers
 * that are able to understand the hibernate XML dialect are sure to understand
 * the code in this class.
 * <p>
 * If the meaning of a method is not apparent, please consult the
 * hibernate-tools documentation at <a
 * href="https://www.hibernate.org">https://www.hibernate.org</a>
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 1.0
 */
public class DecidrReverseEngineeringStrategy extends
        DelegatingReverseEngineeringStrategy {

    /**
     * @param delegate
     *            Fallback strategy
     */
    public DecidrReverseEngineeringStrategy(ReverseEngineeringStrategy delegate) {
        super(delegate);
    }

    /**
     * Turns MySQL underscored table_names into camelCased identifiers.<br>
     * Leading and trailing underscores are removed.
     * 
     * @param tableName
     *            underscored table name
     * @return camelCased table name
     */
    public static String convertTableNameToIdentifier(String tableName) {
        if (tableName == null) {
            throw new IllegalArgumentException("tableName must not be null");
        }
        // remove trailing and leading underscores and spaces
        tableName = tableName.replaceAll("^[_ ]*(.*)[_ ]*$", "$1");
        String[] split = tableName.toLowerCase().split("_");
        String result = split[0];
        for (int i = 1; i < split.length; i++) {
            String rest = split[i];
            rest = rest.length() > 1 ? rest.substring(1, rest.length()) : "";
            result += split[i].substring(0, 1).toUpperCase() + rest;
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public String foreignKeyToCollectionName(String keyname,
            TableIdentifier fromTable, List fromColumns,
            TableIdentifier referencedTable, List referencedColumns,
            boolean uniqueReference) {
        if ((keyname == null) || (fromTable == null) || (fromColumns == null)
                || (referencedTable == null) || (referencedColumns == null)) {
            throw new IllegalArgumentException("No parameter can be null.");
        }
        String result = null;

        /*
         * The following special cases have been hardcoded. Should more than a
         * few special cases occur, please consider using a configuration file.
         * 
         * We have tried using a reverse engineering configuration file
         * (reveng.xml) but it only allows manual overrides on a per-table basis
         * i.e. you have to specify the entire table.
         */
        if ("fk_tenant_admin".equals(keyname)) {
            result = "administratedTenants";
        } else if ("fk_user_tenant1".equals(keyname)) {
            result = "currentlyBustlingUsers";
        } else {
            result = super.foreignKeyToCollectionName(keyname, fromTable,
                    fromColumns, referencedTable, referencedColumns,
                    uniqueReference);
        }

        /*
         * Treatment for silly special cases such as "settingses"
         */
        if ((result.endsWith("Settingses")) || (result.endsWith("Accesses"))) {
            // trim last two characters ("es")
            result = result.substring(0, result.length() - 2);
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public String foreignKeyToEntityName(String keyname,
            TableIdentifier fromTable, List fromColumnNames,
            TableIdentifier referencedTable, List referencedColumnNames,
            boolean uniqueReference) {
        if ((keyname == null) || (fromTable == null)
                || (fromColumnNames == null) || (referencedTable == null)
                || (referencedColumnNames == null)) {
            throw new IllegalArgumentException("No parameter can be null.");
        }

        String fromColumn = ((Column) fromColumnNames.get(0)).getName();
        if (fromColumn.endsWith("Id")) {
            return fromColumn.substring(0, fromColumn.length() - 2);
        } else {
            return super.foreignKeyToEntityName(keyname, fromTable,
                    fromColumnNames, referencedTable, referencedColumnNames,
                    uniqueReference);
        }
    }

    @Override
    public String foreignKeyToManyToManyName(ForeignKey fromKey,
            TableIdentifier middleTable, ForeignKey toKey,
            boolean uniqueReference) {
        if ((fromKey == null) || (middleTable == null) || (toKey == null)) {
            throw new IllegalArgumentException("No parameter can be null.");
        }

        return convertTableNameToIdentifier(middleTable.getName());
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map tableToMetaAttributes(TableIdentifier tableIdentifier) {
        Map<String, MetaAttribute> metaAttributes = super
                .tableToMetaAttributes(tableIdentifier);

        if (tableIdentifier == null) {
            throw new IllegalArgumentException(
                    "table identifier must not be null.");
        }

        if (metaAttributes == null) {
            metaAttributes = new HashMap<String, MetaAttribute>();
        }

        /*
         * Unfortunately we cannot use this to enable dynamic-update and
         * dynamic-insert, a modified FreeMarker template (*.ftl) has been used
         * instead.
         */
        return metaAttributes;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map columnToMetaAttributes(TableIdentifier identifier, String column) {
        /*
         * XXX All primary key columns should be tagged with use-in-equals, but
         * how do we know which column is part of the primary key? :-(
         */
        Map attribs = super.columnToMetaAttributes(identifier, column);
        if (attribs == null) {
            attribs = new HashMap();
        }

        // Uncommenting the following lines would result in all columns being
        // used in equals (which is terribly wrong, only primary key columns
        // should be included)
        //
        // MetaAttribute attrib = new MetaAttribute("use-in-equals");
        // attrib.addValue("true");
        // attribs.put("use-in-equals", attrib);

        return attribs;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List getPrimaryKeyColumnNames(TableIdentifier identifier) {
        ArrayList<String> columns = new ArrayList<String>();

        if (identifier == null) {
            throw new IllegalArgumentException(
                    "table identifier must not be null.");
        }

        /*
         * For each MySQL view, we have to set the primary key manually since we
         * can't define one in the database schema.
         * 
         * To achieve this, each view must name its primary key column "id"
         */
        if (identifier.getName().endsWith("view")) {
            columns.add("id");
        } else {
            return super.getPrimaryKeyColumnNames(identifier);
        }

        return columns;
    }

    @Override
    public void setSettings(ReverseEngineeringSettings settings) {
        super.setSettings(settings);
        // We rely on the "many-to-many-entities" being present.
        if (settings != null) {
            settings.setDetectManyToMany(false);
        }
    }
}
