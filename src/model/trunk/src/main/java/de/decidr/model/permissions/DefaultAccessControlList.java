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
package de.decidr.model.permissions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.hibernate.tool.hbmlint.Issue;

import de.decidr.model.permissions.asserters.IsSuperAdmin;

/**
 * Provides a centralized mechanism for permissions checking. The default ACL
 * acts as a whitelist that grants or denies roles access to permissions based
 * on a set of conditions that must be met. These conditions are represented by
 * asserters.
 * 
 * The ruleset is currently hardcoded into the init method. In the future these
 * rules could potentially be externalized to a configuration file or the
 * database.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class DefaultAccessControlList implements AccessControlList {

    /**
     * The compound key of the ruleset which maps one role and one permission to
     * a set of asserters and an assert mode.
     * 
     * @author Markus Fischer
     * @author Daniel Huss
     * 
     * @version 0.1
     */
    private class Key {

        private Role role;
        private Permission permission;

        /**
         * Constructor of the compound key.
         * 
         * @param role
         * @param permission
         */
        public Key(Role role, Permission permission) {
            super();
            this.role = role;
            this.permission = permission;
        }

        @Override
        public boolean equals(Object obj) {

            Boolean equal = false;

            if (obj instanceof Key) {
                Key key = (Key) obj;

                Boolean roleEqual = role == null ? key.role == null : (role
                        .equals(key.role));

                Boolean permissionEqual = permission == null ? key.permission == null
                        : permission.equals(key.permission);

                equal = roleEqual && permissionEqual;
            }

            return equal;
        }

        @Override
        public int hashCode() {
            int roleHash = role == null ? 0 : role.hashCode();
            int permissionHash = permission == null ? 0 : permission.hashCode();
            return roleHash ^ permissionHash; // bitwise exclusive or
        }

        /**
         * Compares this key to the given key and returns true iff the given key
         * is implied by this key. This is the case iff all of the following
         * conditions are true:
         * <ul>
         * <li>The role of this key is a superclass of or the same as key.role</li>
         * <li>The permission of this key implies key.permission</li>
         * </ul>
         * 
         * @param key
         * @return true iff the given key is implied by this key.
         */
        public Boolean implies(Key key) {
            if (key == null) {
                return false;
            } else {
                Boolean isSuperClass = (key.role == null || role == null) ? false
                        : role.getClass().isAssignableFrom(key.role.getClass());
                return (isSuperClass && permission.implies(key.permission));

            }
        }
    }

    /**
     * The compound value of the ruleset which maps one role and one permission
     * to a set of asserters and an assert mode.
     * 
     * @author Markus Fischer
     * @author Daniel Huss
     * 
     * @version 0.1
     */
    private class Value {
        private Set<Asserter> asserters;
        private AssertMode assertMode;

        /**
         * Constructor of the compound value.
         * 
         * @param asserters
         * @param assertMode
         */
        public Value(Asserter[] asserters, AssertMode assertMode) {
            super();
            this.assertMode = assertMode;
            this.asserters = asserters == null ? null : new HashSet<Asserter>(
                    Arrays.asList(asserters));
        }

        @Override
        public boolean equals(Object obj) {

            Boolean equal = false;

            if (obj instanceof Value) {
                Value value = (Value) obj;

                Boolean assertEqual = asserters == null ? value.asserters == null
                        : asserters.equals(value.asserters);

                Boolean modeEqual = assertMode == null ? value.assertMode == null
                        : assertMode.equals(value.assertMode);

                equal = assertEqual && modeEqual;
            }

            return equal;
        }

        @Override
        public int hashCode() {
            int assertersHash = asserters == null ? 0 : asserters.hashCode();
            int modeHash = assertMode == null ? 0 : assertMode.hashCode();
            return assertersHash ^ modeHash; // bitwise exclusive or
        }
    }

    /**
     * A long-lived reusable instance of the stateless asserter that always
     * returns true.
     */
    private static final Asserter[] alwaysTrueAsserter = { new AlwaysTrue() };

    /**
     * Singleton instance.
     */
    private static DefaultAccessControlList instance = new DefaultAccessControlList();

    /**
     * Map containing all rules.
     */
    protected Map<Key, Value> ruleMap;

    /**
     * Singleton getter.
     * 
     * @return the acl
     */
    public static DefaultAccessControlList getInstance() {
        return instance;
    }

    /**
     * Constructor.
     */
    private DefaultAccessControlList() {
        super();
        this.ruleMap = new HashMap<Key, Value>();
        this.init();
    }

    /**
     * clears list and sets default rules
     * 
     */
    public void init() {
        clearRules();
        // TODO add rules.

        // The superadmin can do anything
        setRule(new SuperAdminRole(), new Permission("*"),
                new IsSuperAdmin(), AssertMode.SatisfyAny);
    }

    /**
     * {@inheritDoc}
     */
    public void clearRules() {
        ruleMap.clear();
    }

    /**
     * {@inheritDoc}
     */
    public Boolean setRule(Role role, Permission permission,
            Asserter[] asserters, AssertMode mode) {

        Boolean isAlreadyImplied = hasRule(role, permission);

        if (!isAlreadyImplied) {
            ruleMap.put(new Key(role, permission), new Value(asserters, mode));
        }

        return !isAlreadyImplied;
    }

    /**
     * {@inheritDoc}
     */
    public Boolean setRule(Role role, Permission permission, Asserter asserter,
            AssertMode mode) {
        Asserter[] asserters = { asserter };
        return setRule(role, permission, asserters, mode);
    }

    /**
     * {@inheritDoc}
     */
    public void allow(Role role, Permission permission) {
        setRule(role, permission, DefaultAccessControlList.alwaysTrueAsserter,
                AssertMode.SatisfyAll);
    }

    /**
     * {@inheritDoc}
     */
    public Boolean isAllowed(Role role, Permission permission) {
        Boolean allowed = true;
        Value rule = ruleMap.get(new Key(role, permission));

        if (rule == null) {
            allowed = false;
            return allowed;
        }

        switch (rule.assertMode) {

        case SatisfyAll:

            for (Asserter a : rule.asserters) {
                if (!a.assertRule(role, permission)) {
                    allowed = false;
                    break;
                }
            }
            break;

        case SatisfyAny:
            allowed = false;
            for (Asserter a : rule.asserters) {
                if (a.assertRule(role, permission)) {
                    allowed = true;
                    break;
                }
            }
            break;

        default:
            allowed = false;
            break;
        }

        return allowed;
    }

    @Override
    public Boolean hasRule(Role role, Permission permission) {
        Key key = new Key(role, permission);

        // Try to find implying rule
        Boolean isAlreadyImplied = false;
        for (Key oldKey : ruleMap.keySet()) {
            if (oldKey.implies(key)) {
                isAlreadyImplied = true;
                break;
            }
        }

        return isAlreadyImplied;
    }

}