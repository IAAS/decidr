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

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

/**
 * Unit tests for <code>{@link DecidrReverseEngineeringStrategy}</code>
 * 
 * @author Reinhold
 */
public class DecidrReverseEngineeringStrategyTest {
    DecidrReverseEngineeringStrategy dres = new DecidrReverseEngineeringStrategy(
            null);

    /**
     * Test method for
     * {@link DecidrReverseEngineeringStrategy#DecidrReverseEngineeringStrategy(ReverseEngineeringStrategy)}
     * .
     */
    @Test
    public void testDecidrReverseEngineeringStrategy() {
        new DecidrReverseEngineeringStrategy(
                new DecidrReverseEngineeringStrategy(null));
    }

    /**
     * Test method for
     * {@link DecidrReverseEngineeringStrategy#convertTableNameToIdentifier(String)}
     * .
     */
    @Test
    public void testConvertTableNameToIdentifier() {
        HashMap<String, String> IOMapping = new HashMap<String, String>();
        IOMapping.put("", "");
        IOMapping.put("_", "_");
        IOMapping.put("asd_efg", "asdEfg");
        IOMapping.put("_asd", "Asd");
        IOMapping.put("asd", "asd");
        IOMapping.put("AsDeFg", "AsDeFg");
        IOMapping.put("_asd_efg_", "AsdEfg_");
        IOMapping.put("As_Def_g", "AsDefG");
        IOMapping.put("asd_", "asd_");

        for (String input : IOMapping.keySet()) {
            assertEquals(IOMapping.get(input), DecidrReverseEngineeringStrategy
                    .convertTableNameToIdentifier(input));
        }

        try {
            DecidrReverseEngineeringStrategy.convertTableNameToIdentifier(null);
            fail("A null String is never a valid table name");
        } catch (IllegalArgumentException e) {
            // this exception is supposed to be thrown
        }
    }

    /**
     * Test method for
     * {@link DecidrReverseEngineeringStrategy#foreignKeyToCollectionName(String, TableIdentifier, List, TableIdentifier, List, boolean)}
     * .
     */
    @Test
    public void testForeignKeyToCollectionNameStringTableIdentifierListTableIdentifierListBoolean() {
        dres.foreignKeyToCollectionName(null, null, null, null, null, false);
    }

    /**
     * Test method for
     * {@link DecidrReverseEngineeringStrategy#foreignKeyToEntityName(String, TableIdentifier, List, TableIdentifier, List, boolean)}
     * .
     */
    @Test
    public void testForeignKeyToEntityNameStringTableIdentifierListTableIdentifierListBoolean() {
        dres.foreignKeyToEntityName(null, null, null, null, null, false);
    }

    /**
     * Test method for
     * {@link DecidrReverseEngineeringStrategy#foreignKeyToManyToManyName(ForeignKey, TableIdentifier, ForeignKey, boolean)}
     * .
     */
    @Test
    public void testForeignKeyToManyToManyNameForeignKeyTableIdentifierForeignKeyBoolean() {
        dres.foreignKeyToManyToManyName(null, null, null, false);
    }

    /**
     * Test method for
     * {@link DecidrReverseEngineeringStrategy#tableToMetaAttributes(TableIdentifier)}
     * .
     */
    @Test
    public void testTableToMetaAttributesTableIdentifier() {
       dres.tableToMetaAttributes(null);
    }

    /**
     * Test method for
     * {@link DecidrReverseEngineeringStrategy#getPrimaryKeyColumnNames(TableIdentifier)}
     * .
     */
    @Test
    public void testGetPrimaryKeyColumnNamesTableIdentifier() {
        dres.getPrimaryKeyColumnNames(null);
    }

    /**
     * Test method for
     * {@link DecidrReverseEngineeringStrategy#setSettings(ReverseEngineeringSettings)}
     * .
     */
    @Test
    public void testSetSettingsReverseEngineeringSettings() {
        dres.setSettings(null);
    }
}