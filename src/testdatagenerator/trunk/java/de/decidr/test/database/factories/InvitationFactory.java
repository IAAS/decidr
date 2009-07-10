package de.decidr.test.database.factories;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.hibernate.Session;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.entities.File;
import de.decidr.model.entities.Tenant;
import de.decidr.model.entities.User;

/**
 * Creates random invitations. To create an invitation you need at least one
 * workflow instance, at least one approved tenant and at least one workflow
 * model within an approved tenant.
 * 
 * FIXME DH continue here
 * 
 * @author Thomas Karsten
 * @author Daniel Huss
 * @version 0.1
 */
public class InvitationFactory {

    private static Random rnd = new Random();

    private Session session;

    /**
     * Constructor
     * 
     * @param session
     */
    public InvitationFactory(Session session) {
        this.session = session;
    }

}
