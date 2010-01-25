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
package de.decidr.webservices.email;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jws.WebService;
import javax.mail.MessagingException;

import org.apache.log4j.Logger;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.acl.roles.EmailRole;
import de.decidr.model.commands.TransactionalCommand;
import de.decidr.model.commands.user.GetUserPropertiesCommand;
import de.decidr.model.email.MailBackend;
import de.decidr.model.entities.File;
import de.decidr.model.entities.SystemSettings;
import de.decidr.model.entities.User;
import de.decidr.model.exceptions.IncompleteConfigurationException;
import de.decidr.model.exceptions.StorageException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.FileFacade;
import de.decidr.model.logging.DefaultLogger;
import de.decidr.model.soap.exceptions.IllegalArgumentExceptionWrapper;
import de.decidr.model.soap.exceptions.IoExceptionWrapper;
import de.decidr.model.soap.exceptions.MalformedURLExceptionWrapper;
import de.decidr.model.soap.exceptions.MessagingExceptionWrapper;
import de.decidr.model.soap.types.AbstractUserList;
import de.decidr.model.soap.types.Actor;
import de.decidr.model.soap.types.ActorUser;
import de.decidr.model.soap.types.EmailUser;
import de.decidr.model.soap.types.IDList;
import de.decidr.model.soap.types.RoleUser;
import de.decidr.model.soap.types.StringMap;
import de.decidr.model.soap.types.StringMapping;
import de.decidr.model.storage.StorageProvider;
import de.decidr.model.storage.StorageProviderFactory;
import de.decidr.model.transactions.HibernateTransactionCoordinator;
import de.decidr.model.webservices.EmailInterface;

/**
 * Implementation of <code>{@link EmailInterface}</code> using
 * <code>{@link MailBackend}</code> to construct and send the e-mail.
 * 
 * @author Reinhold
 */
@WebService(endpointInterface = "de.decidr.model.webservices.EmailInterface", targetNamespace = EmailInterface.TARGET_NAMESPACE, portName = EmailInterface.PORT_NAME, serviceName = EmailInterface.SERVICE_NAME)
// @HandlerChain(file = "handler-chain.xml")
public class EmailService implements EmailInterface {

    private static final String VERSION = "0.1";
    private static final String USER_AGENT = "DecidR E-Mail Web Service v"
            + VERSION;

    private static final Logger log = DefaultLogger
            .getLogger(EmailService.class);

    /**
     * Attaches files to a passed <code>{@link MailBackend}</code> provided they
     * aren't to large or too many.
     * 
     * @param email
     *            The <code>{@link MailBackend}</code> to attach the files to.
     * @param attachments
     *            The files to attach.
     * @throws MessagingException
     *             see <code>{@link MailBackend#addFile(java.net.URI)}</code>
     * @throws MalformedURLException
     *             see <code>{@link MailBackend#addFile(java.net.URI)}</code>
     * @throws TransactionException
     *             Thrown by the <code>{@link de.decidr.model}</code>.
     * @throws StorageException
     *             see <code>{@link StorageProvider#getFile(Long)}</code>
     * @throws IncompleteConfigurationException
     *             see
     *             <code>{@link StorageProviderFactory#getStorageProvider()}</code>
     * @throws IllegalAccessException
     *             see
     *             <code>{@link StorageProviderFactory#getStorageProvider()}</code>
     * @throws InstantiationException
     *             see
     *             <code>{@link StorageProviderFactory#getStorageProvider()}</code>
     * @throws IoExceptionWrapper
     *             see <code>{@link MailBackend#addFile(java.net.URI)}</code>
     */
    private static void addAttachments(MailBackend email, IDList attachments)
            throws MessagingException, MalformedURLException,
            TransactionException, IOException, StorageException,
            IncompleteConfigurationException, InstantiationException,
            IllegalAccessException {
        log.trace("Entering " + EmailService.class.getSimpleName()
                + ".addAttachments(MailBackend, IDList)");
        log.debug("getting system settings from database");
        StorageProvider store = StorageProviderFactory.getDefaultFactory()
                .getStorageProvider();
        SystemSettings config = DecidrGlobals.getSettings();
        FileFacade fileAccess = new FileFacade(EmailRole.getInstance());
        Set<Long> normalisedIDs = new HashSet<Long>(attachments.getId().size());

        log.debug("getting settings");
        int maxAtts = config.getMaxAttachmentsPerEmail();

        log.debug("removing duplicate attachments");
        normalisedIDs.addAll(attachments.getId());

        log.debug("checking that there aren't too many attachments");
        if (normalisedIDs.toArray().length > maxAtts) {
            throw new IllegalArgumentExceptionWrapper("too many attachments");
        }

        File decidrFile;
        log.debug("attaching files");
        for (Long id : normalisedIDs) {
            decidrFile = fileAccess.getFileInfo(id);
            log.debug("attaching file with type=" + decidrFile.getMimeType()
                    + " and fileName=" + decidrFile.getFileName());
            email.addFile(store.getFile(id), decidrFile.getMimeType(),
                    decidrFile.getFileName());
        }
        log.trace("Leaving " + EmailService.class.getSimpleName()
                + ".addAttachments(MailBackend, IDList)");
    }

    /**
     * Applies the configuration retrieved through the <code>model</code> to a
     * <code>{@link MailBackend}</code>.
     * 
     * @param email
     *            The <code>{@link MailBackend}</code> to be configured.
     * @throws TransactionException
     *             Thrown by the <code>{@link de.decidr.model}</code>.
     */
    private static void applyConfig(MailBackend email)
            throws TransactionException {
        log.trace("Entering " + EmailService.class.getSimpleName()
                + ".applyConfig(MailBackend)");
        log.debug("fetching system settings from database");
        SystemSettings config = DecidrGlobals.getSettings();

        log.debug("applying settings to mail");
        email.setXMailer(USER_AGENT);
        email.setAuthInfo(config.getMtaUsername(), config.getMtaPassword());
        email.setHostname(config.getMtaHostname());
        email.setPortNum(config.getMtaPort());
        email.useTLS(config.isMtaUseTls());
        log.trace("Leaving " + EmailService.class.getSimpleName()
                + ".applyConfig(MailBackend)");
    }

    /**
     * Extracts a list of email addresses from a list of
     * <code>{@link Actor Actors}</code>.
     * 
     * @param users
     *            The list of <code>{@link Actor Actors}</code>.
     * @return A list of email addresses.
     * @throws TransactionException
     *             see
     *             <code>{@link HibernateTransactionCoordinator#runTransaction(TransactionalCommand...)}</code>
     */
    private static List<String> extractActorAddresses(List<Actor> users)
            throws TransactionException {
        log.trace("Entering " + EmailService.class.getSimpleName()
                + ".extractActorAddresses(List<Actor>)");
        List<String> emails = new ArrayList<String>();
        List<Long> userIDs = new ArrayList<Long>();

        log.debug("extracting included addresses");
        for (Actor actor : users) {
            if (actor.getEmail() != null) {
                emails.add(actor.getEmail());
            } else {
                userIDs.add(actor.getUserid());
            }
        }

        if (!userIDs.isEmpty()) {
            log.debug("asking the model for the users' email addresses");
            GetUserPropertiesCommand cmd = new GetUserPropertiesCommand(
                    EmailRole.getInstance(), userIDs, "email");
            HibernateTransactionCoordinator.getInstance().runTransaction(cmd);
            List<User> modelUsers = cmd.getUser();
            for (User user : modelUsers) {
                emails.add(user.getEmail());
            }
        }

        log.trace("Leaving " + EmailService.class.getSimpleName()
                + ".extractActorAddresses(List<Actor>)");
        return emails;
    }

    /**
     * Extracts a list of e-mail addresses from a
     * <code>{@link AbstractUserList}</code>.
     * 
     * @param userList
     *            The <code>{@link AbstractUserList}</code> containing the
     *            e-mail addresses.
     * @return A <code>{@link Set}</code> containing e-mail addresses.
     * @throws TransactionException
     *             see <code>{@link #extractActorAddresses(List)}</code>
     */
    private static Set<String> extractEmails(AbstractUserList userList)
            throws TransactionException {
        log.trace("Entering " + EmailService.class.getSimpleName()
                + ".extractEmails(AbstractUserList)");
        if (userList == null) {
            log.debug("Detected empty user list");
            log.trace("Leaving " + EmailService.class.getSimpleName()
                    + ".extractEmails(AbstractUserList)");
            return new HashSet<String>();
        }

        Set<String> emailSet = new HashSet<String>(userList.getActorUser()
                .size()
                + userList.getEmailUser().size()
                + userList.getRoleUser().size());
        List<Actor> actorList = new ArrayList<Actor>();

        log.debug("extracting EmailUsers");
        for (EmailUser user : userList.getEmailUser()) {
            emailSet.add(user.getEmail());
        }

        log.debug("extracting ActorUsers");
        for (ActorUser user : userList.getActorUser()) {
            actorList.add(user.getActor());
        }

        log.debug("extracting RoleUsers");
        for (RoleUser user : userList.getRoleUser()) {
            for (Actor actor : user.getRole().getActor()) {
                actorList.add(actor);
            }
        }

        if (!actorList.isEmpty()) {
            emailSet.addAll(extractActorAddresses(actorList));
        }

        log.trace("Leaving " + EmailService.class.getSimpleName()
                + ".extractEmails(AbstractUserList)");
        return emailSet;
    }

    /**
     * Takes a <code>{@link StringMap}</code> and parses it into a
     * <code>{@link Map}</code> containing mappings from
     * <code>{@link String strings}</code> to
     * <code>{@link String strings}</code>.
     * 
     * @param map
     *            The <code>{@link StringMap}</code> containing the
     *            <code>{@link StringMapping StringMappings}</code>.
     * @return A <code>{@link Map}</code> containing mappings from
     *         <code>{@link String strings}</code> to
     *         <code>{@link String strings}</code>.
     */
    private static Map<String, String> parseStringMap(StringMap map) {
        log.trace("Entering " + EmailService.class.getSimpleName()
                + ".parseStringMap(StringMap)");
        Map<String, String> result = new HashMap<String, String>(map.getItem()
                .size());

        log.debug("parsing StringMap to Map<String, String>");
        for (StringMapping mapping : map.getItem()) {
            result.put(mapping.getName(), mapping.getValue());
        }

        log.trace("Leaving " + EmailService.class.getSimpleName()
                + ".parseStringMap(StringMap)");
        return result;
    }

    public void sendEmail(AbstractUserList to, AbstractUserList cc,
            AbstractUserList bcc, String fromName, String fromAddress,
            String subject, StringMap headers, String bodyTXT, String bodyHTML,
            IDList attachments) throws MessagingExceptionWrapper,
            MalformedURLExceptionWrapper, TransactionException,
            IoExceptionWrapper, IllegalArgumentExceptionWrapper,
            StorageException, IncompleteConfigurationException {
        log.trace("Entering " + EmailService.class.getSimpleName()
                + ".sendEmail(...)");
        log.debug("to: " + to + ", cc: " + cc + ", bcc: " + bcc
                + ", fromName: " + fromName + ", fromAddress: " + fromAddress
                + ", subject: " + subject + ", headers: " + headers
                + ", bodyTXT: " + bodyTXT + ", bodyHTML: " + bodyHTML
                + ", attachments: " + attachments);
        try {
            log.debug("checking parameters for nulls");
            if ((to == null) || (fromAddress == null) || (subject == null)
                    || subject.isEmpty()) {
                log.error("A main parameter (to, "
                        + "fromAddress, subject) is null!");
                throw new IllegalArgumentExceptionWrapper(
                        "The main parameters (to, "
                                + "fromAddress, subject) must not be null!");
            }
            if (((bodyHTML == null) || bodyHTML.isEmpty())
                    && ((bodyTXT == null) || bodyTXT.isEmpty())) {
                log.error("Neither HTML nor text body was passed!");
                throw new IllegalArgumentExceptionWrapper(
                        "There must be either an HTML or a text body");
            }
            if (((to.getEmailUser() == null) || to.getEmailUser().isEmpty())
                    && ((to.getActorUser() == null) || to.getActorUser()
                            .isEmpty())
                    && ((to.getRoleUser() == null) || to.getRoleUser()
                            .isEmpty())) {
                log.error("No recipient specified!");
                throw new IllegalArgumentExceptionWrapper(
                        "The to list was empty");
            }

            log.debug("constructing e-mail");
            MailBackend email = new MailBackend(extractEmails(to), fromName,
                    fromAddress, subject);

            if (cc != null) {
                log.debug("adding CC");
                email.setCC(extractEmails(cc));
            }
            if (bcc != null) {
                log.debug("adding BCC");
                email.setBCC(extractEmails(bcc));
            }
            if (headers != null) {
                log.debug("adding additional headers");
                email.addHeaders(parseStringMap(headers));
            }

            if ((bodyHTML != null) && !bodyHTML.isEmpty()) {
                log.debug("adding HTML body");
                email.setBodyHTML(bodyHTML);
            }

            if ((bodyTXT != null) && !bodyTXT.isEmpty()) {
                log.debug("adding text body");
                email.setBodyText(bodyTXT);
            }

            if (attachments != null) {
                log.debug("adding attachments - this might take a while");
                try {
                    addAttachments(email, attachments);
                } catch (MalformedURLException e) {
                    throw new MalformedURLExceptionWrapper(e.getMessage());
                } catch (InstantiationException e) {
                    throw new TransactionException(e);
                } catch (IllegalAccessException e) {
                    throw new TransactionException(e);
                }
            }

            log.debug("setting info from config");
            applyConfig(email);

            log.debug("sending e-mail");
            email.sendMessage();
        } catch (MessagingExceptionWrapper e) {
            throw e;
        } catch (IoExceptionWrapper e) {
            throw e;
        } catch (IllegalArgumentExceptionWrapper e) {
            throw e;
        } catch (IOException e) {
            throw new IoExceptionWrapper(e.getMessage(), e.getCause());
        } catch (MessagingException e) {
            throw new MessagingExceptionWrapper(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentExceptionWrapper(e.getMessage(), e
                    .getCause());
        }

        log.trace("Leaving " + EmailService.class.getSimpleName()
                + ".sendEmail(...)");
    }
}