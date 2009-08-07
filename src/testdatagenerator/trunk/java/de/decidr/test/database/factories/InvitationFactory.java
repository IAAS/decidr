package de.decidr.test.database.factories;

import java.util.Random;

import org.hibernate.Session;

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
