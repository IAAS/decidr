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
 * XXX add dynamic-update=true meta atrribute to each class. Wontfix: cannot be
 * done using ReverseEngineeringStrategy.
 * 
 * Provides a customized reverse engineering strategy for the DecidR MySQL
 * database.
 * <p>
 * We have chosen a custom strategy over a set of XML files since programmers
 * that are able to understand the hibernate XML dialect are sure to understand
 * the code in this class.
 * <p>
 * If the meaning of a method is not apparent, please consult the
 * hibernate-tools documentation at https://www.hibernate.org
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 1.0
 */
public class DecidrReverseEngineeringStrategy extends
        DelegatingReverseEngineeringStrategy {

    /**
     * Constructor
     * 
     * @param delegate
     *            Fallback strategy
     */
    public DecidrReverseEngineeringStrategy(ReverseEngineeringStrategy delegate) {
        super(delegate);
    }

    /**
     * Turns MySQL underscored table_names into camelCased identifiers.
     * 
     * @param tableName
     *            underscored table name
     * @return camelCased table name
     */
    public String convertTableNameToIdentifier(String tableName) {
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

        String result = null;
        /*
         * The following special case has been hardcoded. Should more than a few
         * special cases occur, please consider using a configuration file.
         * 
         * We have tried using a reverse engineering configuration file
         * (reveng.xml) but it only allows manual overrides on a per-table basis
         * i.e. you have to specify the entire table.
         */
        if ("fk_tenant_admin".equals(keyname)) {
            result = "administratedTenants";
        } else {
            result = super.foreignKeyToCollectionName(keyname, fromTable,
                    fromColumns, referencedTable, referencedColumns,
                    uniqueReference);
        }

        /*
         * Treatment for silly special cases such as "settingses"
         */
        if ((result.endsWith("Settingses")) || (result.endsWith("Accesses"))) {
            result = result.substring(0, result.length() - "es".length());
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public String foreignKeyToEntityName(String keyname,
            TableIdentifier fromTable, List fromColumnNames,
            TableIdentifier referencedTable, List referencedColumnNames,
            boolean uniqueReference) {

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

        return convertTableNameToIdentifier(middleTable.getName());
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map tableToMetaAttributes(TableIdentifier tableIdentifier) {
        Map<String, MetaAttribute> metaAttributes = super
                .tableToMetaAttributes(tableIdentifier);

        if (metaAttributes == null) {
            metaAttributes = new HashMap<String, MetaAttribute>();
        }

        /*
         * Unfortunately we cannot use this to enable dynamic-update and
         * dynamic-insert.
         */

        return metaAttributes;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List getPrimaryKeyColumnNames(TableIdentifier identifier) {
        ArrayList<String> columns = new ArrayList<String>();

        /**
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
        settings.setDetectManyToMany(false);
    }

}
