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
import java.util.List;
import java.util.Map;

import javax.jws.WebMethod;
import javax.mail.MessagingException;
import javax.xml.bind.TypeConstraintException;

import org.apache.log4j.Logger;

import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.logging.DefaultLogger;
import de.decidr.model.soap.exceptions.IoExceptionWrapper;
import de.decidr.model.soap.exceptions.MalformedURLExceptionWrapper;
import de.decidr.model.soap.exceptions.MessagingExceptionWrapper;
import de.decidr.model.soap.types.AbstractUser;
import de.decidr.model.soap.types.AbstractUserList;
import de.decidr.model.soap.types.Actor;
import de.decidr.model.soap.types.ActorUser;
import de.decidr.model.soap.types.EmailUser;
import de.decidr.model.soap.types.IDList;
import de.decidr.model.soap.types.RoleUser;
import de.decidr.model.soap.types.StringMap;
import de.decidr.model.soap.types.StringMapping;

/**
 * Implementation of <code>{@link EmailInterface}</code> using
 * <code>{@link MailBackend}</code> to construct and send the e-mail.
 * 
 * @author Reinhold
 */
@javax.jws.WebService(serviceName = "Email", portName = "EmailSOAP", targetNamespace = "http://decidr.de/webservices/Email", wsdlLocation = "file:Email.wsdl", endpointInterface = "de.decidr.webservices.email.EmailPT")
public class EmailService implements EmailInterface {

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
     * @throws IoExceptionWrapper
     *             see <code>{@link MailBackend#addFile(java.net.URI)}</code>
     */
    @WebMethod(exclude = true)
    private static void addAttachments(MailBackend email, IDList attachments)
            throws MessagingException, MalformedURLException,
            TransactionException, IOException {
        log.trace("Entering " + EmailService.class.getSimpleName()
                + ".addAttachments(MailBackend, IDList)");

        // RR: spec says amount of attachments & size need to be limited
        for (Long id : attachments.getId()) {
            // RR finish
            // email.addFile(model.getURIFromFileRef(id));
        }
        log.trace("Leaving " + EmailService.class.getSimpleName()
                + ".addAttachments(MailBackend, IDList)");
    }

    /**
     * Extracts a list of e-mail addresses from a
     * <code>{@link AbstractUserList}</code>.
     * 
     * @param userList
     *            The <code>{@link AbstractUserList}</code> containing the
     *            e-mail addresses.
     * @return A <code>{@link List}</code> containing e-mail addresses.
     */
    @WebMethod(exclude = true)
    private static List<String> extractEmails(AbstractUserList userList) {
        log.trace("Entering " + EmailService.class.getSimpleName()
                + ".extractEmails(AbstractUserList)");
        List<String> emailList = new ArrayList<String>(userList
                .getAbstractUser().size());

        log.debug("extracting email addresses from AbstractUserList");
        for (AbstractUser user : userList.getAbstractUser()) {
            if (user instanceof EmailUser) {
                log.debug("found EmailUser");
                emailList.add(((EmailUser) user).getUser());
            } else if (user instanceof ActorUser) {
                log.debug("found ActorUser");
                emailList.add(((ActorUser) user).getUser().getEmail());
            } else if (user instanceof RoleUser) {
                log.debug("found RoleUser");
                for (Actor actor : ((RoleUser) user).getUser().getActor()) {
                    emailList.add(actor.getEmail());
                }
            } else {
                log.error("The AbstractUser " + user + " was not recognised. "
                        + "Please check the ObjectFactory and "
                        + "update this implementation!");
                throw new TypeConstraintException("Invalid subclass of "
                        + AbstractUser.class.getName());
            }
        }

        log.trace("Leaving " + EmailService.class.getSimpleName()
                + ".extractEmails(AbstractUserList)");
        return emailList;
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
    @WebMethod(exclude = true)
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
            IoExceptionWrapper, IllegalArgumentException {
        log.trace("Entering " + EmailService.class.getSimpleName()
                + ".senEmail(...)");
        log.debug("checking parameters for nulls");
        if (to == null || fromAddress == null || subject == null) {
            log.error("A main parameter (to, fromAddress, subject) is null!");
            throw new IllegalArgumentException("The main parameters (to, "
                    + "fromAddress, subject) must not be null!");
        }
        if (bodyHTML == null && bodyTXT == null) {
            log.error("Neither HTML nor text body was passed!");
            throw new IllegalArgumentException("There must be either an "
                    + "HTML or a text body");
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

        if (bodyHTML != null) {
            log.debug("adding HTML body");
            try {
                email.setBodyHTML(bodyHTML);
            } catch (MessagingException e) {
                throw new MessagingExceptionWrapper(e.getMessage());
            } catch (IOException e) {
                throw new IoExceptionWrapper(e.getMessage(), e.getCause());
            }
        }

        if (bodyTXT != null) {
            log.debug("adding text body");
            try {
                email.setBodyText(bodyTXT);
            } catch (MessagingException e) {
                throw new MessagingExceptionWrapper(e.getMessage());
            } catch (IOException e) {
                throw new IoExceptionWrapper(e.getMessage(), e.getCause());
            }
        }

        if (attachments != null) {
            log.debug("adding attachments - this might take a while");
            try {
                addAttachments(email, attachments);
            } catch (MalformedURLException e) {
                throw new MalformedURLExceptionWrapper(e.getMessage());
            } catch (MessagingException e) {
                throw new MessagingExceptionWrapper(e.getMessage());
            } catch (IOException e) {
                throw new IoExceptionWrapper(e.getMessage(), e.getCause());
            }
        }

        log.debug("setting info from config");
        applyConfig(email);

        log.debug("sending e-mail");
        try {
            email.sendMessage();
        } catch (MessagingException e) {
            throw new MessagingExceptionWrapper(e.getMessage());
        } catch (IOException e) {
            throw new IoExceptionWrapper(e.getMessage(), e.getCause());
        }

        log.trace("Leaving " + EmailService.class.getSimpleName()
                + ".senEmail(...)");
    }

    /**
     * Applies the configuration retrieved through the <code>model</code> to a
     * <code>{@link MailBackend}</code>.
     * 
     * @param email
     *            The <code>{@link MailBackend}</code> to be configured.
     */
    private void applyConfig(MailBackend email) {
        // TODO Auto-generated method stub
    }
}