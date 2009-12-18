package de.decidr.test.database.factories;

import java.util.Calendar;
import java.util.Date;

import org.hibernate.Query;
import org.hibernate.Session;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.entities.SystemSettings;
import de.decidr.model.entities.User;
import de.decidr.test.database.main.ProgressListener;

/**
 * Creates system settings for testing purposes. It is assumed that some users
 * are already available in the target database (see UserFactory).
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * @version 0.1
 */
public class SystemSettingsFactory extends EntityFactory {

    /**
     * Constructor
     * 
     * @param session
     */
    public SystemSettingsFactory(Session session) {
        super(session);
    }

    /**
     * @param session
     * @param progressListener
     */
    public SystemSettingsFactory(Session session,
            ProgressListener progressListener) {
        super(session, progressListener);
    }

    /**
     * Creates and persists system settings.
     * 
     * @return TODO document
     */
    public SystemSettings createSystemSettings() {

        SystemSettings settings = new SystemSettings();

        // get super admin
        Query q = getSession().createQuery(
                "from User u where userProfile is not null");

        User superAdmin = (User) q.setMaxResults(1).uniqueResult();

        if (superAdmin == null) {
            throw new RuntimeException(
                    "Cannot find a suitable super admin user.");
        }

        superAdmin.setRegisteredSince(DecidrGlobals.getTime().getTime());
        superAdmin.setDisabledSince(null);
        superAdmin.setUnavailableSince(null);
        
        Object toDelete = superAdmin.getPasswordResetRequest();
        if (toDelete != null) {
            session.delete(toDelete);
        }
        
        toDelete = superAdmin.getChangeEmailRequest();
        if (toDelete != null) {
            session.delete(toDelete);
        }
        
        toDelete = superAdmin.getRegistrationRequest();
        if (toDelete != null) {
            session.delete(toDelete);
        }
        
        session.update(superAdmin);

        String logLevel = "DEBUG";

        Date modifiedDate = new Date();
        Calendar cal = DecidrGlobals.getTime();
        cal.set(1985, 12, 28);

        modifiedDate.setTime(cal.getTimeInMillis());

        settings.setAutoAcceptNewTenants(false);
        settings.setBaseUrl("iaassrv4stud.informatik.uni-stuttgart.de:8080/WebPortal");
        settings.setMaxAttachmentsPerEmail(10);
        settings.setMaxUploadFileSizeBytes(10485760);
        settings.setModifiedDate(modifiedDate);
        settings.setMonitorAveragingPeriodSeconds(300);
        settings.setMonitorUpdateIntervalSeconds(60);
        settings.setMtaHostname("smtp.googlemail.com");
        settings.setMtaPassword("DecidR0809");
        settings.setMtaPort(0);
        settings.setMtaUsername("decidr.iaas@googlemail.com");
        settings.setMtaUseTls(true);
        settings.setPasswordResetRequestLifetimeSeconds(259200);
        settings.setRegistrationRequestLifetimeSeconds(259200);
        settings.setChangeEmailRequestLifetimeSeconds(259200);
        settings.setInvitationLifetimeSeconds(259200);
        settings.setMaxWorkflowInstancesForUnlock((byte) 80);
        settings.setMinServerLoadForLock((byte) 80);
        settings.setMaxServerLoadForShutdown((byte) 20);
        settings.setMinUnlockedServers(1);
        settings.setServerPoolInstances(5);
        settings.setMinWorkflowInstancesForLock(10);
        settings.setMaxWorkflowInstancesForUnlock(8);
        settings.setMaxWorkflowInstancesForShutdown(1);
        settings.setSuperAdmin(superAdmin);
        settings.setSystemEmailAddress("test@decidr.de");
        settings.setSystemName("DecidR Test System");
        settings.setLogLevel(logLevel);

        session.save(settings);
        fireProgressEvent(1, 1);

        return settings;
    }

}