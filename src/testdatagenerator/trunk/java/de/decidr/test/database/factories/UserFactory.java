package de.decidr.test.database.factories;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.acl.Password;
import de.decidr.model.entities.ChangeEmailRequest;
import de.decidr.model.entities.PasswordResetRequest;
import de.decidr.model.entities.RegistrationRequest;
import de.decidr.model.entities.User;
import de.decidr.model.entities.UserProfile;
import de.decidr.test.database.main.ProgressListener;

/**
 * Creates randomized users for testing purposes.
 * 
 * @author Thomas Karsten
 * @author Daniel Huss
 * @version 0.1
 */
public class UserFactory extends EntityFactory {

    private static String[] firstNames = { "Thomas", "Modood", "Jenny",
            "Julia", "Geoffrey", "Markus", "Aleks", "Reinhold", "Gordon",
            "Daniel", "三郎", "屎臉" };

    private static String[] lastNames = { "Kraxelhuber", "大輔", "陰莖", "Smith",
            "Freeman", "Иванов", "Хус" };

    private static String[] streets = { "Marktstraße", "Silly Lane",
            "Red Light District", "Black Mesa", "Universitätsstraße" };

    private static String[] cities = { "Stuttgart", "Köln", "Berlin",
            "Gehdochheim", "Ростов-на-Дону", "Київ", "Харьков", "Ялта",
            "Владивосток", "Новосибирск", "Сочи", "東京", "横浜市", "City 17" };

    /**
     * Constructor
     */
    public UserFactory(Session session) {
        super(session);
    }

    /**
     * @param session
     * @param progressListener
     */
    public UserFactory(Session session, ProgressListener progressListener) {
        super(session, progressListener);
    }

    /**
     * Generates numUsers random persisted users. (Hopefully) all user statuses
     * are covered, including:
     * 
     * <ul>
     * <li>Account disabled by the super admin</li>
     * <li>Status set to "unavailable for workflow participation"</li>
     * <li>Having an active change email request</li>
     * <li>Having registered a user profile</li>
     * <li>Having confirmed the registration request</li>
     * <li>Having an active password reset</li>
     * </ul>
     * 
     * @param numUsers
     *            number of users to generate
     * @return persisted user entities
     */
    public List<User> createRandomUsers(int numUsers) {
        ArrayList<User> result = new ArrayList<User>(numUsers);

        for (int i = 0; i < numUsers; i++) {
            User user = new User();

            Date now = DecidrGlobals.getTime().getTime();

            user.setEmail(getUniqueEmailAddress(i));
            user.setCreationDate(now);
            session.save(user);

            // every 23th user is banned
            user.setDisabledSince((i % 23 == 0) ? now : null);

            // every 13th user is unavailable
            user.setUnavailableSince((i % 13 == 0) ? now : null);

            // every 31th user wants to change his email address
            if (i % 31 == 0) {
                ChangeEmailRequest request = new ChangeEmailRequest();
                request.setAuthKey(Password.getRandomAuthKey());
                request.setCreationDate(now);
                request.setNewEmail("new_" + getUniqueEmailAddress(i));
                request.setUser(user);
                session.save(request);
                user.setChangeEmailRequest(request);
            }

            // half our users will be registered users, the other half are
            // unregistered users
            if (i % 2 == 0) {
                // the user is a registered user

                UserProfile profile = new UserProfile();
                profile.setLastName(lastNames[rnd.nextInt(lastNames.length)]);
                profile
                        .setFirstName(firstNames[rnd.nextInt(firstNames.length)]);
                profile.setCity(cities[rnd.nextInt(cities.length)]);
                profile.setPostalCode(Integer.toString(rnd.nextInt(99999)));
                profile.setStreet(streets[rnd.nextInt(streets.length)] + " "
                        + Integer.toString(rnd.nextInt(999)));
                String username = "user" + Integer.toString(i);
                profile.setUsername(username);
                profile.setPasswordSalt(Password.getRandomSalt());
                profile.setPasswordHash(Password.getHash(username, profile
                        .getPasswordSalt()));
                profile.setUser(user);
                user.setRegisteredSince(now);

                user.setAuthKey(null);

                session.save(profile);
                user.setUserProfile(profile);

                // every 17th registered user has not yet confirmed his
                // registration request
                if (i % 17 == 0) {
                    RegistrationRequest request = new RegistrationRequest();
                    request.setAuthKey(Password.getRandomAuthKey());
                    request.setCreationDate(now);
                    request.setUser(user);
                    session.save(request);
                    user.setRegistrationRequest(request);
                }

                // every 29th registered user wants to reset his password
                if (i % 29 == 0) {
                    PasswordResetRequest request = new PasswordResetRequest();
                    request.setAuthKey(Password.getRandomAuthKey());
                    request.setCreationDate(now);
                    request.setUser(user);
                    session.save(request);
                    user.setPasswordResetRequest(request);
                }

            } else {
                // the user is not registered
                user.setAuthKey(Password.getRandomAuthKey());
            }

            session.update(user);
            result.add(user);

            fireProgressEvent(numUsers, i + 1);
        }
        return result;
    }

    /**
     * @param localPartNumber
     * @return a randomly generated email address.
     */
    private String getUniqueEmailAddress(int localPartNumber) {
        String localpart;
        localpart = "user_" + Integer.toString(localPartNumber) + "_"
                + Password.getRandomSalt().substring(0, 8);

        String domain = "test.decidr.de";

        return localpart + "@" + domain;
    }

}