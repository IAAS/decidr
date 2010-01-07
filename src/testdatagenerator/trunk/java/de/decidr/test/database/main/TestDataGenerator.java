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

package de.decidr.test.database.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.JDBCException;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.commands.TransactionalCommand;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.HibernateTransactionCoordinator;
import de.decidr.model.transactions.TransactionAbortedEvent;
import de.decidr.model.transactions.TransactionEvent;
import de.decidr.test.database.factories.ActivityFactory;
import de.decidr.test.database.factories.InvitationFactory;
import de.decidr.test.database.factories.ServerFactory;
import de.decidr.test.database.factories.SystemSettingsFactory;
import de.decidr.test.database.factories.TenantFactory;
import de.decidr.test.database.factories.UserFactory;
import de.decidr.test.database.factories.WorkItemFactory;
import de.decidr.test.database.factories.WorkflowInstanceFactory;
import de.decidr.test.database.factories.WorkflowModelFactory;

/**
 * The main test data generator program that uses the entity factories to fill a
 * user-defined database with test data. This programm is designed to run via a
 * simple command line interface.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class TestDataGenerator {

    /**
     * Constants that identify application settings.
     */
    public static final String PROPERTY_CONNECTION_URL = "connection-url";
    public static final String PROPERTY_USERS = "users";
    public static final String PROPERTY_TENANTS = "tenants";
    public static final String PROPERTY_MODELS = "models";
    public static final String PROPERTY_MODELS_PER_TENANT = "models-per-tenant";
    public static final String PROPERTY_INSTANCES = "instances";
    public static final String PROPERTY_WORKITEMS = "workitems";
    public static final String PROPERTY_INVITATIONS = "invitations";
    public static final String PROPERTY_MAX_USERS_PER_TENANT = "max-users-per-tenant";

    /**
     * Constants that identify application switches.
     */
    public static final String SWITCH_HELP = "help";
    public static final String SWITCH_NON_INTERACTIVE = "non-interactive";
    public static final String SWITCH_NO_STDOUT = "no-stdout";
    public static final String SWITCH_NO_STDERR = "no-stderr";

    /**
     * Application settings.
     */
    private Properties settings;

    /**
     * Set of active switches such as "--help"
     */
    private Set<String> activeSwitches;

    /**
     * last recorded progress in percent
     */
    private long lastProgressPercent = 0;
    private Calendar lastProgressReportTime = null;

    /**
     * @param args
     */
    public static void main(String[] args) {
        TestDataGenerator program = new TestDataGenerator(args);
        program.run();
    }

    /**
     * Constructor that initializes the application
     * 
     * @param args
     *            agruments passed to the application via CLI
     */
    private TestDataGenerator(String[] args) {
        super();
        try {
            settings = getApplicationSettings(new File(
                    "testdatagenerator.properties"));
            activeSwitches = getSwitches(args);
            parseArguments(args, settings);
            applyCurrentSettings();
        } catch (IOException e) {
            System.err.println("Error: cannot read settings file. Exiting.");
        }
    }

    /**
     * Runs the program (with "error handling")
     */
    private void run() {
        try {
            doRun();
        } catch (Throwable e) {
            stdErr("An error occurred! Stack trace:");
            e.printStackTrace(System.err);

            while (e.getCause() != null) {
                e = e.getCause();
                if (e instanceof JDBCException) {
                    JDBCException jdbcException = (JDBCException) e;
                    stdErr("Statement that caused the exception:");
                    stdErr(jdbcException.getSQL());
                }
            }
        }
    }

    /**
     * Runs the program.
     * 
     * @throws TransactionException
     * @throws IOException
     */
    private void doRun() throws TransactionException, IOException {
        if (!isPropertiesValid()) {
            return;
        }

        if (activeSwitches.contains(SWITCH_HELP)) {
            displayHelp();
            return;
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(
                System.in));

        // give the user a chance to check the target database
        if (!activeSwitches.contains(SWITCH_NON_INTERACTIVE)) {
            stdOut("Please make sure you have deployed an empty database structure to:");
            stdOut(settings.getProperty(PROPERTY_CONNECTION_URL));
            stdOut("Hit ENTER to continue...");
            reader.readLine();
        }

        final ProgressListener progressListener = new ProgressListener() {
            @Override
            public void reportProgress(int totalItems, int doneItems) {
                long progressPercent;

                if (totalItems == 0) {
                    progressPercent = 0;
                } else {
                    progressPercent = Math
                            .round(((double) doneItems / (double) totalItems) * 100.0);
                }
                // report progress only once per second
                Calendar nextReportDate;
                if (lastProgressReportTime == null) {
                    nextReportDate = DecidrGlobals.getTime();
                    nextReportDate.add(Calendar.SECOND, -1);
                } else {
                    nextReportDate = (Calendar) lastProgressReportTime.clone();
                    nextReportDate.add(Calendar.SECOND, 1);
                }

                // report progress only if it has changed or we're done!
                if (totalItems == doneItems
                        || lastProgressPercent != progressPercent
                        && (DecidrGlobals.getTime().after(nextReportDate))) {
                    stdOut(progressPercent + "% done");
                    lastProgressPercent = progressPercent;
                    lastProgressReportTime = DecidrGlobals.getTime();
                }
            }
        };

        // fill the database!
        HibernateTransactionCoordinator.getInstance().runTransaction(
                new TransactionalCommand() {

                    @Override
                    public void transactionAborted(TransactionAbortedEvent evt)
                            throws TransactionException {
                        // nothing needs to be done
                    }

                    @Override
                    public void transactionCommitted(TransactionEvent evt)
                            throws TransactionException {
                        stdOut("Transaction successfully committed!");
                    }

                    @Override
                    public void transactionStarted(TransactionEvent evt)
                            throws TransactionException {
                        /*
                         * it is important to respect the dependencies of each
                         * entity factory. For example, the tenant factory
                         * requires some users to be present in the database.
                         */
                        Session s = evt.getSession();

                        stdOut("Creating users...");
                        resetProgress();
                        new UserFactory(s, progressListener)
                                .createRandomUsers(Integer.parseInt(settings
                                        .getProperty(PROPERTY_USERS)));
                        resetProgress();

                        stdOut("Creating activies / known web services");
                        new ActivityFactory(s, progressListener)
                                .createActivites();

                        stdOut("Creating system settings...");
                        resetProgress();
                        new SystemSettingsFactory(s, progressListener)
                                .createSystemSettings();

                        stdOut("Creating tenants...");
                        resetProgress();
                        new TenantFactory(s, progressListener)
                                .createRandomTenants(
                                        Integer.parseInt(settings
                                                .getProperty(PROPERTY_TENANTS)),
                                        Integer
                                                .parseInt(settings
                                                        .getProperty(PROPERTY_MAX_USERS_PER_TENANT)));

                        stdOut("Creating servers...");
                        resetProgress();
                        new ServerFactory(s, progressListener)
                                .createRandomServers();

                        stdOut("Creating workflow models...");
                        resetProgress();
                        new WorkflowModelFactory(s, progressListener)
                                .createRandomWorkflowModels(
                                        Integer.parseInt(settings
                                                .getProperty(PROPERTY_MODELS)),
                                        Integer
                                                .parseInt(settings
                                                        .getProperty(PROPERTY_MODELS_PER_TENANT)));

                        stdOut("Creating workflow instances...");
                        resetProgress();
                        new WorkflowInstanceFactory(s, progressListener)
                                .createRandomWorkflowInstances(Integer
                                        .parseInt(settings
                                                .getProperty(PROPERTY_INSTANCES)));

                        stdOut("Creating work items...");
                        resetProgress();
                        new WorkItemFactory(s, progressListener)
                                .createRandomWorkItems(Integer
                                        .parseInt(settings
                                                .getProperty(PROPERTY_WORKITEMS)));

                        stdOut("Creating invitations...");
                        resetProgress();
                        new InvitationFactory(s, progressListener)
                                .createRandomInvitations(Integer
                                        .parseInt(settings
                                                .getProperty(PROPERTY_INVITATIONS)));

                        stdOut("All done.");
                    }
                });
    }

    /**
     * Makes sure the given properties are valid.
     */
    private boolean isPropertiesValid() {
        boolean result = true;
        // XXX add more checks?

        try {
            int numUsers = Integer.parseInt(settings
                    .getProperty(PROPERTY_USERS));
            int numTenants = Integer.parseInt(settings
                    .getProperty(PROPERTY_TENANTS));

            if (numTenants > numUsers) {
                stdErr("Number of tenants must be smaller or equal to number of users!");
                result = false;
            }

        } catch (NumberFormatException e) {
            stdErr("Number of users and number of tenants must be integers!");
            result = false;
        }

        return result;
    }

    /**
     * Resets progress information so the next progress event will generate
     * output
     */
    private void resetProgress() {
        lastProgressPercent = 0;
        lastProgressReportTime = null;
    }

    /**
     * Displays a help message informing the user about the available switches
     * and properties
     */
    private void displayHelp() {
        StringBuffer help = new StringBuffer(10000);

        help.append("DecidR test data generator\n\n");
        help.append(DecidrGlobals.DISCLAIMER);
        help.append("\n\n");
        help.append("Usage: java -jar testdatagenerator.jar "
                + "[--property=value]+ [--switch]+\n");

        help.append("\n\nProperties:\n--------------------\n");
        help.append(PROPERTY_CONNECTION_URL);
        help
                .append("\t - \tHibernate connection URL override such as 'jdbc:mysql://localhost:3306/decidrdb'\n");
        help.append(PROPERTY_USERS);
        help.append("\t - \tTotal number of users to create\n");
        help.append(PROPERTY_TENANTS);
        help.append("\t - \tTotal number of tenants to create\n");
        help.append(PROPERTY_MODELS);
        help.append("\t - \tTotal number of workflow models to create\n");
        help.append(PROPERTY_MODELS_PER_TENANT);
        help.append("\t - \tNumber of workflow models per tenant\n");
        help.append(PROPERTY_INSTANCES);
        help.append("\t - \tTotal number of workflow instances to create\n");
        help.append(PROPERTY_WORKITEMS);
        help.append("\t - \tTotal number of work items to create\n");
        help.append(PROPERTY_INVITATIONS);
        help.append("\t - \tTotal number of invitations to create\n");
        help.append(PROPERTY_MAX_USERS_PER_TENANT);
        help
                .append("\t - \tMaximum number of users to associate with each tenant\n");

        help.append("\nSwitches:\n--------------------\n");
        help.append(SWITCH_HELP);
        help.append("\t - \tDisplay this help then exit\n");
        help.append(SWITCH_NON_INTERACTIVE);
        help.append("\t - \tDo not prompt for user input\n");
        help.append(SWITCH_NO_STDOUT);
        help.append("\t - \tSuppress output to STDOUT\n");
        help.append(SWITCH_NO_STDERR);
        help.append("\t - \tSuppress output to STDERR\n");

        System.out.println(help.toString());
    }

    /**
     * Applies the current application settings.
     */
    private void applyCurrentSettings() {
        String connectionUrl = settings.getProperty(PROPERTY_CONNECTION_URL);

        Configuration config = HibernateTransactionCoordinator.getInstance()
                .getConfiguration();
        config.setProperty("hibernate.connection.url", connectionUrl);
        HibernateTransactionCoordinator.getInstance().setConfiguration(config);
    }

    /**
     * 
     * @param settingsFile
     *            file that contains the system settings.
     * @return the user-defined application settings
     * @throws IOException
     *             if the settings file cannot be read
     */
    private Properties getApplicationSettings(File settingsFile)
            throws IOException {
        Properties result = new Properties(getDefaultSettings());
        if (settingsFile.isFile()) {
            result.load(new FileInputStream(settingsFile));
        }
        return result;
    }

    /**
     * @return the default application settings
     */
    private Properties getDefaultSettings() {
        Properties result = new Properties();

        result.setProperty(PROPERTY_CONNECTION_URL,
                "jdbc:mysql://localhost:3306/decidrdb");
        result.setProperty(PROPERTY_USERS, "500");
        result.setProperty(PROPERTY_TENANTS, "30");
        result.setProperty(PROPERTY_MODELS, "300");
        result.setProperty(PROPERTY_MODELS_PER_TENANT, "10");
        result.setProperty(PROPERTY_INSTANCES, "1000");
        result.setProperty(PROPERTY_WORKITEMS, "3000");
        result.setProperty(PROPERTY_INVITATIONS, "300");
        result.setProperty(PROPERTY_MAX_USERS_PER_TENANT, "50");

        return result;
    }

    /**
     * Puts all arguments of the form "--argument-name=argument value" in the
     * given property map.
     * 
     * @param args
     *            arguments to parse
     * @param properties
     *            properties to populate
     */
    private void parseArguments(String[] args, Properties properties) {
        // matches against arguments of the form
        // "--argument-name=argument value"
        Pattern pattern = Pattern.compile("^--((\\w+-?)+)=(.+)$");

        for (String arg : args) {
            Matcher match = pattern.matcher(arg.trim());
            if (match.matches()) {
                String propertyName = match.group(1);
                String propertyValue = match.group(3);

                if (!propertyName.isEmpty()) {
                    properties.setProperty(propertyName, propertyValue);
                }
            }
        }
    }

    /**
     * Returns a set of all command line switches that are present in args, such
     * as "--help" or "-help"
     * 
     * @param args
     *            arguments to scan for switches
     * @return set of switches without "-" or "--"
     */
    private Set<String> getSwitches(String[] args) {
        HashSet<String> result = new HashSet<String>();
        Pattern pattern = Pattern.compile("^--?((\\w+-?)+)$");

        for (String arg : args) {
            Matcher match = pattern.matcher(arg.trim());
            if (match.matches()) {
                String switchName = match.group(1);
                result.add(switchName);
            }
        }

        return result;
    }

    /**
     * Prints output to stdout unless the "no-stdout" switch is set.
     * 
     * @param output
     */
    private void stdOut(CharSequence output) {
        if (!activeSwitches.contains(SWITCH_NO_STDOUT)) {
            System.out.println(output);
        }
    }

    /**
     * Prints output to stderr unless the "no-stderr" switch is set.
     * 
     * @param output
     */
    private void stdErr(CharSequence output) {
        if (!activeSwitches.contains(SWITCH_NO_STDERR)) {
            System.err.println(output);
        }
    }
}
