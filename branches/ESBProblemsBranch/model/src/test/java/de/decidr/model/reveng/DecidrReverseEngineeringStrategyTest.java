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
import java.util.List;

import org.hibernate.cfg.reveng.ReverseEngineeringSettings;
import org.hibernate.cfg.reveng.ReverseEngineeringStrategy;
import org.hibernate.cfg.reveng.TableIdentifier;
import org.hibernate.mapping.ForeignKey;
import org.junit.Test;

import de.decidr.model.testing.DecidrOthersTest;

/**
 * Unit tests for <code>{@link DecidrReverseEngineeringStrategy}</code>
 * 
 * @author Reinhold
 */
public class DecidrReverseEngineeringStrategyTest extends DecidrOthersTest {
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
        IOMapping.put("_", "");
        IOMapping.put("asd_efg", "asdEfg");
        IOMapping.put("_asd", "asd");
        IOMapping.put("asd", "asd");
        IOMapping.put("AsDeFg", "asdefg");
        IOMapping.put("_asd_efg_", "asdEfg");
        IOMapping.put("As_Def_g", "asDefG");
        IOMapping.put("asd_", "asd");

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
    @Test(expected = IllegalArgumentException.class)
    public void testForeignKeyToCollectionNameStringTableIdentifierListTableIdentifierListBoolean() {
        dres.foreignKeyToCollectionName(null, null, null, null, null, false);
    }

    /**
     * Test method for
     * {@link DecidrReverseEngineeringStrategy#foreignKeyToEntityName(String, TableIdentifier, List, TableIdentifier, List, boolean)}
     * .
     */
    @Test(expected = IllegalArgumentException.class)
    public void testForeignKeyToEntityNameStringTableIdentifierListTableIdentifierListBoolean() {
        dres.foreignKeyToEntityName(null, null, null, null, null, false);
    }

    /**
     * Test method for
     * {@link DecidrReverseEngineeringStrategy#foreignKeyToManyToManyName(ForeignKey, TableIdentifier, ForeignKey, boolean)}
     * .
     */
    @Test(expected = IllegalArgumentException.class)
    public void testForeignKeyToManyToManyNameForeignKeyTableIdentifierForeignKeyBoolean() {
        dres.foreignKeyToManyToManyName(null, null, null, false);
    }

    /**
     * Test method for
     * {@link DecidrReverseEngineeringStrategy#tableToMetaAttributes(TableIdentifier)}
     * .
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTableToMetaAttributesTableIdentifier() {
        dres.tableToMetaAttributes(null);
    }

    /**
     * Test method for
     * {@link DecidrReverseEngineeringStrategy#getPrimaryKeyColumnNames(TableIdentifier)}
     * .
     */
    @Test(expected = IllegalArgumentException.class)
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
        // null means default settings, so this should not throw an exception.
        dres.setSettings(null);
    }
}
