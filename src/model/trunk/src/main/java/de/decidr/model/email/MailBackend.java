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
package de.decidr.model.email;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import com.sun.mail.smtp.SMTPTransport;

import de.decidr.model.logging.DefaultLogger;

/**
 * A representation of a JavaMail <code>{@link Message message}</code>.<br>
 * I works by first collecting all the necessary information and then calling
 * JavaMail to construct and send the actual e-mail.<br>
 * <br>
 * Depends on JavaMail 1.4+
 * 
 * @author Reinhold
 */
public class MailBackend {

    /**
     * A warning message to be used in methods that are implemented to allow for
     * testing.
     */
    private static final String WARNING_TESTING = "This method is for testing use only!";

    private static final String EMPTY_SUBJECT = "<no subject>";

    private static Logger log = DefaultLogger.getLogger(MailBackend.class);

    /**
     * Uses <code>{@link #validateAddresses(String)}</code> to verify that a
     * list of e-mail addresses are formatted correctly.
     * 
     * @param addressList
     *            A list of e-mail addresses.
     * @return <code>true</code> - if the list parses correctly<br>
     *         <code>false</code> - otherwise
     */
    public static boolean validateAddresses(List<String> addressList) {
        log.trace("Entering " + MailBackend.class.getSimpleName()
                + ".validateAddresses(List<String>)");
        if (addressList == null || addressList.isEmpty()) {
            log.debug("empty parameter => invalid addresses");
            log.trace("Leaving " + MailBackend.class.getSimpleName()
                    + ".validateAddresses(List<String>)");
            return false;
        }

        StringBuilder addresses = new StringBuilder(addressList.size() * 20);

        log.debug("Constructing header string from list...");
        for (String recipient : addressList) {
            addresses.append(recipient);
            addresses.append(", ");
        }
        addresses.delete(addresses.length() - 2, addresses.length());
        log.debug("checking constructed address list: " + addresses);
        boolean result = validateAddresses(addresses.toString());

        log.trace("Leaving " + MailBackend.class.getSimpleName()
                + ".validateAddresses(List<String>)");
        return result;
    }

    /**
     * Uses <code>{@link InternetAddress#parse(String, boolean)
     * InternetAddress.parse(String, true)}</code> to verify that a list of
     * e-mail addresses are formatted correctly.<br>
     * Empty address strings are considered invalid.
     * 
     * @param addressList
     *            A comma separated list of e-mail addresses.
     * @return <code>true</code> - if the list parses correctly<br>
     *         <code>false</code> - otherwise
     */
    public static boolean validateAddresses(String addressList) {
        log.trace("Entering " + MailBackend.class.getSimpleName()
                + ".validateAddresses(String)");
        if (addressList == null || addressList.trim().isEmpty()) {
            log.debug("Passed address list is empty => invalid");
            log.trace("Leaving " + MailBackend.class.getSimpleName()
                    + ".validateAddresses(String)");
            return false;
        }

        log.debug("Using InternetAddress.parse() to determine validity");
        try {
            InternetAddress.parse(addressList, true);
        } catch (AddressException e) {
            log.debug("AddressException in InternetAddress.parse()"
                    + " => invalid", e);
            log.trace("Leaving " + MailBackend.class.getSimpleName()
                    + ".validateAddresses(String)");
            return false;
        }

        log.debug("No error and not empty => assuming correct format");
        log.trace("Leaving " + MailBackend.class.getSimpleName()
                + ".validateAddresses(String)");
        return true;
    }

    /**
     * A map containing additional headers and their contents.
     */
    private Map<String, String> additionalHeaderMap = new HashMap<String, String>();

    /**
     * Main header <code>{@link String strings}</code>.
     */
    private String headerTo, headerFromName, headerFromMail, headerSubject,
            headerCC, headerBCC, headerXMailer;

    /**
     * List of parts. If there are more than one, a multipart message is
     * constructed.
     */
    private List<MimeBodyPart> messageParts = new Vector<MimeBodyPart>();

    /**
     * The port number of the SMTP server. Defaults to whatever the protocol
     * specifies.
     */
    private int SMTPPortNum = -1;

    /**
     * The hostname of the SMTP server. Defaults to <code>localhost</code>.
     */
    private String SMTPServerHost = "localhost";

    /**
     * The authentification information. If unset, the server is assumed not to
     * use authentification.
     */
    private String username, password;

    /**
     * Whether to use TLS for the mail transfer. Defaults to <code>false</code>.
     */
    private boolean useTLS = false;

    /**
     * @param to
     *            The recipients of the Email. See
     *            <code>{@link #setReceiver(String)}</code>.
     * @param fromName
     *            The name of the sender of the Email. See
     *            <code>{@link #setSender(String, String)}</code>.
     * @param fromMail
     *            The email address of the sender of the Email. See
     *            <code>{@link #setSender(String, String)}</code>.
     * @param subject
     *            The subject of the Email. See
     *            <code>{@link #setSubject(String)}</code>.
     */
    public MailBackend(String to, String fromName, String fromMail,
            String subject) {
        log.trace("Entering " + MailBackend.class.getSimpleName()
                + " constructor");
        setReceiver(to);
        setSender(fromName, fromMail);
        setSubject(subject);
        log.trace("Leaving " + MailBackend.class.getSimpleName()
                + " constructor");
    }

    /**
     * @param to
     *            The recipients of the Email. See
     *            <code>{@link #setReceiver(List<String>)}</code>.
     * @param fromName
     *            The name of the sender of the Email. See
     *            <code>{@link #setSender(String, String)}</code>.
     * @param fromMail
     *            The email address of the sender of the Email. See
     *            <code>{@link #setSender(String, String)}</code>.
     * @param subject
     *            The subject of the Email. See
     *            <code>{@link #setSubject(String)}</code>.
     */
    public MailBackend(List<String> to, String fromName, String fromMail,
            String subject) {
        log.trace("Entering " + MailBackend.class.getSimpleName()
                + " constructor");
        setReceiver(to);
        setSender(fromName, fromMail);
        setSubject(subject);
        log.trace("Leaving " + MailBackend.class.getSimpleName()
                + " constructor");
    }

    /**
     * Adds a list of receivers to the current list of secret receivers of
     * carbon copies.
     * 
     * @param bcc
     *            A comma separated list of recipients.
     */
    public void addBCC(String bcc) {
        log.trace("Entering " + MailBackend.class.getSimpleName()
                + ".addBCC(String)");
        if (bcc == null || bcc.trim().isEmpty()) {
            log.debug("Empty or null string => nothing to append");
            log.trace("Leaving " + MailBackend.class.getSimpleName()
                    + ".addBCC(String)");
            return;
        }
        if (!validateAddresses(bcc)) {
            log.error("The following string is not a valid value "
                    + "for the \"BCC:\" header: " + bcc);
            throw new IllegalArgumentException(
                    "One or more of the specified recipients are formatted incorrectly!");
        }

        log.debug("Appending \"" + bcc + "\" to BCC header.");
        setBCC(headerBCC + ", " + bcc);
        log.trace("Leaving " + MailBackend.class.getSimpleName()
                + ".addBCC(String)");
    }

    /**
     * Adds a list of receivers to the current list of receivers of carbon
     * copies.
     * 
     * @param cc
     *            A comma separated list of recipients.
     */
    public void addCC(String cc) {
        log.trace("Entering " + MailBackend.class.getSimpleName()
                + ".addCC(String)");
        if (cc == null || cc.trim().isEmpty()) {
            log.debug("Empty or null string => nothing to append");
            log.trace("Leaving " + MailBackend.class.getSimpleName()
                    + ".addCC(String)");
            return;
        }
        if (!validateAddresses(cc)) {
            log.error("The following string is not a valid value "
                    + "for the \"CC:\" header: " + cc);
            throw new IllegalArgumentException(
                    "One or more of the specified recipients are formatted incorrectly!");
        }

        log.debug("Appending \"" + cc + "\" to CC header.");
        setCC(headerCC + ", " + cc);
        log.trace("Leaving " + MailBackend.class.getSimpleName()
                + ".addCC(String)");
    }

    /**
     * Attaches the file specified by the <code>{@link File}</code> parameter.
     * 
     * @param file
     *            A <code>{@link File}</code> pointing to the file to attach.
     * @throws IOException
     *             see <code>{@link #addFile(URI)}</code>
     * @throws MessagingException
     *             see <code>{@link #addFile(URI)}</code>
     * @throws MalformedURLException
     *             see <code>{@link #addFile(URI)}</code>
     * @return see <code>{@link #addFile(URI)}</code>
     */
    public MimeBodyPart addFile(File file) throws MalformedURLException,
            MessagingException, IOException {
        log.trace("Entering " + MailBackend.class.getSimpleName()
                + ".addFile(File)");
        log.debug("calling addFile(URI) with URI of passed file");
        MimeBodyPart result = addFile(file.toURI());
        log.trace("Leaving " + MailBackend.class.getSimpleName()
                + ".addFile(File)");
        return result;
    }

    /**
     * Attaches the file specified by the <code>{@link URI}</code> parameter.
     * 
     * @param file
     *            A <code>{@link URI}</code> specifying the file to attach.
     * @throws MalformedURLException
     *             see <code>{@link URI#toURL()}</code>
     * @throws MessagingException
     *             see <code>{@link URI#toURL()}</code>
     * @throws IOException
     *             see <code>{@link URI#toURL()}</code>
     * @return see <code>{@link #addFile(URL)}</code>
     */
    public MimeBodyPart addFile(URI file) throws MalformedURLException,
            MessagingException, IOException {
        log.trace("Entering " + MailBackend.class.getSimpleName()
                + ".addFile(URI)");
        log.debug("calling addFile(URL) with a URL equivalent "
                + "to the passed URI");
        MimeBodyPart result = addFile(file.toURL());
        log.trace("Leaving " + MailBackend.class.getSimpleName()
                + ".addFile(URI)");
        return result;
    }

    /**
     * Attaches the file specified by the <code>{@link URL}</code> parameter.
     * 
     * @param file
     *            A <code>{@link URL}</code> pointing to the file to attach.
     * @throws MessagingException
     *             see <code>{@link URL#openStream()}</code>
     * @throws IOException
     *             see
     *             <code>{@link MimeBodyPart#MimeBodyPart(java.io.InputStream)}</code>
     * @return The <code>{@link MimeBodyPart}</code> containing the file
     */
    public MimeBodyPart addFile(URL file) throws MessagingException,
            IOException {
        log.trace("Entering " + MailBackend.class.getSimpleName()
                + ".addFile(URL)");
        log.debug("Opening stream to passed URL and adding it "
                + "to message's body");
        MimeBodyPart result = new MimeBodyPart(file.openStream());
        addMimePart(result);
        log.trace("Leaving " + MailBackend.class.getSimpleName()
                + ".addFile(URL)");
        return result;
    }

    /**
     * Adds an arbitrary header name-value tuple to the headers of the message.
     * Overwrites any existing existing header of the same name, if necessary.
     * The header may be ignored or overwritten by the mail transport agent.<br>
     * <br>
     * Headers handled separately (subject, to, ...) are delegated to the
     * appropriate methods ({@link #setSender(String)},
     * {@link #setReceiver(String)}, ...)
     * 
     * @param headerName
     *            The name of the header to set.
     * @param headerContent
     *            The value of the header specified by <code>headerName</code>.
     */
    public void addHeader(String headerName, String headerContent) {
        log.trace("Entering " + MailBackend.class.getSimpleName()
                + ".addHeader(String, String)");
        /*
         * Trim the string in case somebody tries to circumvent the filter using
         * spaces.
         */
        headerName = headerName.trim();

        if (headerName.equalsIgnoreCase("To")) {
            log.warn("The To: header should not be set using the "
                    + "addHeader() method. Delegating to setReceiver()...");
            setReceiver(headerContent);
        } else if (headerName.equalsIgnoreCase("From")) {
            log.error("The From: header should not be set using the "
                    + "addHeader() method. Use the "
                    + "setSender(String, String) method...");
            throw new IllegalArgumentException(
                    "Attempted to set From: header using the addHeader() method");
        } else if (headerName.equalsIgnoreCase("Subject")) {
            log.warn("The Subject: header should not be set using the "
                    + "addHeader() method. Delegating to setSubject()...");
            setSubject(headerContent);
        } else if (headerName.equalsIgnoreCase("CC")) {
            log.warn("The CC: header should not be set using the "
                    + "addHeader() method. Delegating to setCC()...");
            setCC(headerContent);
        } else if (headerName.equalsIgnoreCase("BCC")) {
            log.warn("The BCC: header should not be set using the "
                    + "addHeader() method. Delegating to setBCC()...");
            setBCC(headerContent);
        } else if (headerName.equalsIgnoreCase("X-Mailer")
                || headerName.equalsIgnoreCase("User-Agent")
                || headerName.equalsIgnoreCase("X-Newsreader")) {
            log.warn("The " + headerName + ": header should not "
                    + "be set using the addHeader() method. "
                    + "Delegating to setXMailer()...");
            setXMailer(headerContent);
        } else {
            log.debug("Adding " + headerName + ": " + headerContent);
            additionalHeaderMap.put(headerName, headerContent);
        }
        log.trace("Leaving " + MailBackend.class.getSimpleName()
                + ".addHeader(String, String)");
    }

    /**
     * Has the same effect as calling
     * <code>{@link #addHeader(String, String)}</code> for every item in the
     * map.<br>
     * <br>
     * Headers handled separately (subject, to, ...) are delegated to the
     * appropriate methods ({@link #setSender(String)},
     * {@link #setReceiver(String)}, ...)
     * 
     * @param headers
     *            A <code>{@link Map}</code> of headers to set.
     */
    public void addHeaders(Map<String, String> headers) {
        log.trace("Entering " + MailBackend.class.getSimpleName()
                + ".addHeaders(Map<String, String>)");
        log.debug("Stepping through headers Map and "
                + "delegating to addHeader(String, String)...");
        for (String headerName : headers.keySet()) {
            addHeader(headerName, headers.get(headerName));
        }
        log.trace("Leaving " + MailBackend.class.getSimpleName()
                + ".addHeaders(Map<String, String>)");
    }

    /**
     * Adds a MIME part to the message. Can be used to attach files and other
     * content.
     * 
     * @param part
     *            The <code>{@link MimeBodyPart}</code> to add at the end of the
     *            MIME parts list.
     * @return see <code>{@link List#add(Object)}</code>
     */
    public boolean addMimePart(MimeBodyPart part) {
        log.trace("Entering " + MailBackend.class.getSimpleName()
                + ".addMimePart(MimeBodyPart)");
        boolean result = messageParts.add(part);
        log.trace("Leaving " + MailBackend.class.getSimpleName()
                + ".addMimePart(MimeBodyPart)");
        return result;
    }

    /**
     * Adds a list MIME part to the message. Can be used to attach files and
     * other content.
     * 
     * @param parts
     *            The <code>{@link MimeBodyPart MimeBodyParts}</code> to add at
     *            the end of the MIME parts list.
     * @return see <code>{@link List#addAll(Collection)}</code>
     */
    public boolean addMimeParts(List<MimeBodyPart> parts) {
        log.trace("Entering " + MailBackend.class.getSimpleName()
                + ".addMimeParts(List<MimeBodyPart>)");
        boolean result = messageParts.addAll(parts);
        log.trace("Leaving " + MailBackend.class.getSimpleName()
                + ".addMimeParts(List<MimeBodyPart>)");
        return result;
    }

    /**
     * Adds a list of receivers to the current list of receivers.
     * 
     * @param to
     *            A comma separated list of recipients.
     */
    public void addReceiver(String to) {
        log.trace("Entering " + MailBackend.class.getSimpleName()
                + ".addReceiver(String)");
        if (to == null || to.trim().isEmpty()) {
            log.debug("Empty or null string => nothing to append");
            log.trace("Leaving " + MailBackend.class.getSimpleName()
                    + ".addReceiver(String)");
            return;
        }
        if (!validateAddresses(to)) {
            log.error("The following string is not a valid value "
                    + "for the \"To:\" header: " + to);
            throw new IllegalArgumentException(
                    "One or more of the specified recipients are formatted incorrectly!");
        }

        setReceiver(headerTo + ", " + to);
        log.trace("Leaving " + MailBackend.class.getSimpleName()
                + ".addReceiver(String)");
    }

    /**
     * To be used for testing.
     * 
     * @return the additionalHeaderMap
     */
    final Map<String, String> getAdditionalHeaderMap() {
        log.warn(WARNING_TESTING);
        return additionalHeaderMap;
    }

    /**
     * To be used for testing.
     * 
     * @return the headerBCC
     */
    final String getHeaderBCC() {
        log.warn(WARNING_TESTING);
        return headerBCC;
    }

    /**
     * To be used for testing.
     * 
     * @return the headerCC
     */
    final String getHeaderCC() {
        log.warn(WARNING_TESTING);
        return headerCC;
    }

    /**
     * To be used for testing.
     * 
     * @return the headerFromName
     */
    final String getHeaderFromName() {
        log.warn(WARNING_TESTING);
        return headerFromName;
    }

    /**
     * To be used for testing.
     * 
     * @return the headerFromMail
     */
    final String getHeaderFromMail() {
        log.warn(WARNING_TESTING);
        return headerFromMail;
    }

    /**
     * To be used for testing.
     * 
     * @return the headerSubject
     */
    final String getHeaderSubject() {
        log.warn(WARNING_TESTING);
        return headerSubject;
    }

    /**
     * To be used for testing.
     * 
     * @return the headerTo
     */
    final String getHeaderTo() {
        log.warn(WARNING_TESTING);
        return headerTo;
    }

    /**
     * To be used for testing.
     * 
     * @return the headerXMailer
     */
    final String getHeaderXMailer() {
        log.warn(WARNING_TESTING);
        return headerXMailer;
    }

    /**
     * To be used for testing.
     * 
     * @return the messageParts
     */
    final List<MimeBodyPart> getMessageParts() {
        log.warn(WARNING_TESTING);
        return messageParts;
    }

    /**
     * To be used for testing.
     * 
     * @return the password
     */
    final String getPassword() {
        log.warn(WARNING_TESTING);
        return password;
    }

    /**
     * To be used for testing.
     * 
     * @return the sMTPPortNum
     */
    final int getSMTPPortNum() {
        log.warn(WARNING_TESTING);
        return SMTPPortNum;
    }

    /**
     * To be used for testing.
     * 
     * @return the sMTPServerHost
     */
    final String getSMTPServerHost() {
        log.warn(WARNING_TESTING);
        return SMTPServerHost;
    }

    /**
     * To be used for testing.
     * 
     * @return the username
     */
    final String getUsername() {
        log.warn(WARNING_TESTING);
        return username;
    }

    /**
     * To be used for testing.
     * 
     * @return the useTLS
     */
    final boolean isUseTLS() {
        log.warn(WARNING_TESTING);
        return useTLS;
    }

    /**
     * Removes any additional headers defined by
     * <code>{@link #addHeader(String, String)}</code> and
     * <code>{@link #addHeaders(Map)}</code>.
     * 
     * @param headerName
     *            The name of the header to remove.
     */
    public void removeHeader(String headerName) {
        log.trace("Entering " + MailBackend.class.getSimpleName()
                + ".removeHeader(String)");
        log.debug("attempting to remove the specified string");
        additionalHeaderMap.remove(headerName);
        log.trace("Leaving " + MailBackend.class.getSimpleName()
                + ".removeHeader(String)");
    }

    /**
     * Has the same effect as calling <code>{@link #removeHeader(String)}</code>
     * for every item in the list.
     * 
     * @param headers
     *            A <code>{@link List}</code> of headers to remove.
     */
    public void removeHeaders(List<String> headers) {
        log.trace("Entering " + MailBackend.class.getSimpleName()
                + ".removeHeaders(List<String>)");
        log.debug("attempting to remove the specified strings");
        for (String header : headers) {
            additionalHeaderMap.remove(header);
        }
        log.trace("Leaving " + MailBackend.class.getSimpleName()
                + ".removeHeaders(List<String>)");
    }

    /**
     * Removes a <code>{@link MimeBodyPart}</code> from the MIME parts list.
     * 
     * @param part
     *            The <code>{@link MimeBodyPart}</code> to remove.
     * @return see <code>{@link List#remove(Object)}</code>
     */
    public boolean removeMimePart(MimeBodyPart part) {
        log.trace("Entering " + MailBackend.class.getSimpleName()
                + ".removeMimePart(MimeBodyPart)");
        log.debug("attempting to remove the specified body part");
        boolean result = messageParts.remove(part);
        log.trace("Leaving " + MailBackend.class.getSimpleName()
                + ".removeMimePart(MimeBodyPart)");
        return result;
    }

    /**
     * Removes a list of <code>{@link MimeBodyPart MimeBodyParts}</code> from
     * the mime parts list.
     * 
     * @param parts
     *            The <code>{@link MimeBodyPart MimeBodyParts}</code> to remove.
     * @return see <code>{@link List#removeAll(Collection)}</code>
     */
    public boolean removeMimeParts(List<MimeBodyPart> parts) {
        log.trace("Entering " + MailBackend.class.getSimpleName()
                + ".removeMimeParts(List<MimeBodyPart>)");
        log.debug("attempting to remove the specified body parts");
        boolean result = messageParts.removeAll(parts);
        log.trace("Leaving " + MailBackend.class.getSimpleName()
                + ".removeMimeParts(List<MimeBodyPart>)");
        return result;
    }

    /**
     * Send the message using the data specified by the user.
     * 
     * @throws MessagingException
     *             May be thrown while setting headers, sending the message if
     *             the authentification data was incomplete/incorrect, etc.
     * @throws IOException
     *             see <code>{@link MimeBodyPart#getContent()}</code>
     */
    public void sendMessage() throws MessagingException, IOException {
        log.trace("Entering " + MailBackend.class.getSimpleName()
                + ".sendMessage()");
        if (SMTPServerHost == null || SMTPServerHost.isEmpty()) {
            log.debug("detected empty hostname => setting to localhost");
            SMTPServerHost = "localhost";
        }
        String protocol;
        if (useTLS) {
            log.info("Using SMTPS protocol for message delivery");
            protocol = "smtps";
        } else {
            log.info("Using SMTP protocol for message delivery");
            protocol = "smtp";
        }

        Properties sysProps = System.getProperties();
        sysProps.setProperty("mail." + protocol + ".host", SMTPServerHost);
        if (username != null && !username.isEmpty()) {
            log.debug("using username \"" + username
                    + "\" for authentification");
            sysProps.setProperty("mail." + protocol + ".auth", "true");
        }
        Session session = Session.getInstance(sysProps, null);
        // XXX check whether this should be <= or >=
        if (log.getLevel().toInt() <= Priority.DEBUG_INT) {
            session.setDebug(true);
        } else {
            session.setDebug(false);
        }

        MimeMessage message = new MimeMessage(session);

        if (messageParts.size() == 0) {
            log.error("Can't send a message without a body!");
            throw new IllegalArgumentException(
                    "You need to add some content to your message.");
        } else if (messageParts.size() == 1
                && messageParts.get(0).getContent() instanceof String) {
            log.debug("constructing message with simple body");
            message.setText((String) (messageParts.get(0).getContent()));
        } else {
            log.debug("constructing multipart message.");
            MimeMultipart multipartMessage = new MimeMultipart();
            for (MimeBodyPart part : messageParts) {
                multipartMessage.addBodyPart(part);
            }
            message.setContent(multipartMessage);
        }

        if (headerXMailer != null) {
            log.debug("adding User-Agent header");
            message.setHeader("User-Agent", headerXMailer);
        }
        if (additionalHeaderMap != null) {
            log.debug("adding additional headers");
            for (String header : additionalHeaderMap.keySet()) {
                message.addHeader(header, additionalHeaderMap.get(header));
            }
        }

        if (headerTo == null || headerTo.isEmpty()) {
            log.error("Can't send a message without recipient.");
            throw new IllegalArgumentException(
                    "You need to specify a receiver!");
        }
        if (headerFromMail == null || headerFromMail.isEmpty()) {
            log.error("Can't send a message from nobody.");
            throw new IllegalArgumentException("You need to specify a sender!");
        }
        if (headerSubject == null || headerSubject.trim().isEmpty()) {
            log.debug("detected null subject");
            headerSubject = EMPTY_SUBJECT;
        }
        log.debug("setting main headers to override additional "
                + "headers, if necessary");
        message.setFrom(new InternetAddress(headerFromMail, headerFromName));
        message.setRecipients(Message.RecipientType.TO, headerTo);
        if (!(headerCC == null || headerCC.isEmpty()))
            message.setRecipients(Message.RecipientType.CC, headerCC);
        if (!(headerBCC == null || headerBCC.isEmpty()))
            message.setRecipients(Message.RecipientType.BCC, headerBCC);
        message.setSubject(headerSubject);
        message.setSentDate(new Date());

        log.debug("opening mail transport");
        SMTPTransport transport = (SMTPTransport) session
                .getTransport(protocol);
        try {
            if (username != null && username.isEmpty()) {
                username = null;
                password = null;
            }
            log.info("Attempting connection with the provided detail..");
            transport.connect(SMTPServerHost, SMTPPortNum, username, password);
            log.info("Attempting to send message...");
            transport.sendMessage(message, message.getAllRecipients());
        } finally {
            transport.close();
        }
        log.trace("Leaving " + MailBackend.class.getSimpleName()
                + ".sendMessage()");
    }

    /**
     * Set the username and password needed for authentification.
     * 
     * @param username
     *            The username used for authentification. If <code>null</code>,
     *            authentification will not be used.
     * @param password
     *            The password used for authentification.
     */
    public void setAuthInfo(String username, String password) {
        log.trace("Entering " + MailBackend.class.getSimpleName()
                + ".setAuthInfo(String, String)");
        setUsername(username);
        setPassword(password);
        log.trace("Leaving " + MailBackend.class.getSimpleName()
                + ".setAuthInfo(String, String)");
    }

    /**
     * Sets the current list of secret receivers of carbon copies of this Email.
     * 
     * @param bcc
     *            A list of recipients.
     */
    public void setBCC(List<String> bcc) {
        log.trace("Entering " + MailBackend.class.getSimpleName()
                + ".setBCC(List<String>)");
        StringBuilder bccs = new StringBuilder(bcc.size() * 20);

        log.debug("Constructing header string from list...");
        for (String recipient : bcc) {
            bccs.append(recipient);
            bccs.append(", ");
        }
        bccs.delete(bccs.length() - 2, bccs.length());

        log.debug("delegating to setBCC(String)");
        setBCC(bccs.toString());
        log.trace("Leaving " + MailBackend.class.getSimpleName()
                + ".setBCC(List<String>)");
    }

    /**
     * Sets the current list of secret receivers of carbon copies of this Email.
     * 
     * @param bcc
     *            A comma separated list of recipients.
     */
    public void setBCC(String bcc) {
        log.trace("Entering " + MailBackend.class.getSimpleName()
                + ".setBCC(String)");
        if (bcc == null || bcc.trim().isEmpty()) {
            log.debug("detected empty parameter");
            headerBCC = "";
            log.trace("Leaving " + MailBackend.class.getSimpleName()
                    + ".setBCC(String)");
            return;
        }

        if (!validateAddresses(bcc)) {
            log.error("The following string is not a valid value "
                    + "for the \"BCC:\" header: " + bcc);
            throw new IllegalArgumentException(
                    "One or more of the specified recipients are formatted incorrectly!");
        }

        log.debug("setting BCC");
        headerBCC = bcc;
        log.trace("Leaving " + MailBackend.class.getSimpleName()
                + ".setBCC(String)");
    }

    /**
     * Set the string content of the main body part.
     * 
     * @param message
     *            The primary text message of the Email.
     * @throws MessagingException
     *             May be thrown while setting/getting message contents.
     * @throws IOException
     *             see <code>{@link MimeBodyPart#getContent()}</code>
     */
    public void setBodyText(String message) throws MessagingException,
            IOException {
        log.trace("Entering " + MailBackend.class.getSimpleName()
                + ".setBodyText(String)");
        // add a new part if the first on is not a string or there are no parts
        if (messageParts.isEmpty()
                || !(messageParts.get(0).getContent() instanceof String)) {
            log.debug("adding new first MimeBodyPart");
            messageParts.add(0, new MimeBodyPart());
        }

        log.debug("setting body text");
        messageParts.get(0).setText(message);
        log.trace("Leaving " + MailBackend.class.getSimpleName()
                + ".setBodyText(String)");
    }

    /**
     * Add an HTML part to the message.
     * 
     * @param message
     *            The HTML message to add to the Email.
     * @throws MessagingException
     *             May be thrown while setting/getting message contents.
     * @throws IOException
     *             see <code>{@link MimeBodyPart#getContent()}</code>
     */
    public void setBodyHTML(String message) throws MessagingException,
            IOException {
        log.trace("Entering " + MailBackend.class.getSimpleName()
                + ".setBodyHTML(String)");
        MimeBodyPart contents = new MimeBodyPart();
        contents.setContent(message, "text/html");

        if (messageParts.isEmpty()) {
            log.debug("adding new first MimeBodyPart");
            messageParts.add(0, contents);
        } else if (messageParts.get(0).getContent() instanceof String) {
            log.debug("adding new second MimeBodyPart");
            messageParts.add(1, contents);
        }

        log.trace("Leaving " + MailBackend.class.getSimpleName()
                + ".setBodyHTML(String)");
    }

    /**
     * Sets the current list of receivers of carbon copies of this Email.
     * 
     * @param cc
     *            A list of recipients.
     */
    public void setCC(List<String> cc) {
        log.trace("Entering " + MailBackend.class.getSimpleName()
                + ".setCC(List<String>)");
        StringBuilder ccs = new StringBuilder(cc.size() * 20);

        log.debug("Constructing header string from list...");
        for (String recipient : cc) {
            ccs.append(recipient);
            ccs.append(", ");
        }
        ccs.delete(ccs.length() - 2, ccs.length());

        log.debug("delegating to setCC(String)");
        setCC(ccs.toString());
        log.trace("Leaving " + MailBackend.class.getSimpleName()
                + ".setCC(List<String>)");
    }

    /**
     * Sets the current list of receivers of carbon copies of this Email.
     * 
     * @param cc
     *            A comma separated list of recipients.
     */
    public void setCC(String cc) {
        log.trace("Entering " + MailBackend.class.getSimpleName()
                + ".setCC(String)");
        if (cc == null || cc.trim().isEmpty()) {
            log.debug("detected empty parameter");
            headerBCC = "";
            log.trace("Leaving " + MailBackend.class.getSimpleName()
                    + ".setCC(String)");
            return;
        }

        if (!validateAddresses(cc)) {
            log.error("The following string is not a valid value "
                    + "for the \"CC:\" header: " + cc);
            throw new IllegalArgumentException(
                    "One or more of the specified recipients are formatted incorrectly!");
        }

        log.debug("setting CC header");
        headerCC = cc;
        log.trace("Leaving " + MailBackend.class.getSimpleName()
                + ".setCC(String)");
    }

    /**
     * Specify the host the MTA is running on. Defaults to
     * <code>localhost</code>.
     * 
     * @param hostname
     *            The hostname of the MTA. May also be an IP address.
     */
    public void setHostname(String hostname) {
        log.trace("Entering " + MailBackend.class.getSimpleName()
                + ".setHostname(String)");
        if (hostname == null || hostname.isEmpty()) {
            log.debug("detected empty hostname => setting to localhost");
            SMTPServerHost = "localhost";
        } else {
            log.debug("setting hostname");
            SMTPServerHost = hostname;
        }
        log.trace("Leaving " + MailBackend.class.getSimpleName()
                + ".setHostname(String)");
    }

    /**
     * Set the password needed for authentification.
     * 
     * @param password
     *            The password used for authentification.
     */
    public void setPassword(String password) {
        log.trace("Entering " + MailBackend.class.getSimpleName()
                + ".setPassword(String)");
        // XXX cleartext password in memory
        log.debug("setting password");
        this.password = password;
        log.trace("Leaving " + MailBackend.class.getSimpleName()
                + ".setPassword(String)");
    }

    /**
     * Specify the port the MTA is listening on.
     * 
     * @param portNum
     *            The port the MTA is listening on. <code>-1</code> specifies
     *            the protocol dependent default port.
     */
    public void setPortNum(int portNum) {
        log.trace("Entering " + MailBackend.class.getSimpleName()
                + ".setProtNum(int)");
        log.debug("setting port number");
        SMTPPortNum = portNum;
        log.trace("Leaving " + MailBackend.class.getSimpleName()
                + ".setProtNum(int)");
    }

    /**
     * Sets the current list of receivers of this Email.
     * 
     * @param to
     *            A list of recipients.
     */
    public void setReceiver(List<String> to) {
        log.trace("Entering " + MailBackend.class.getSimpleName()
                + ".setReceiver(List<String>)");
        StringBuilder tos = new StringBuilder(to.size() * 20);

        log.debug("Constructing header string from list...");
        for (String recipient : to) {
            tos.append(recipient);
            tos.append(", ");
        }
        tos.delete(tos.length() - 2, tos.length());

        log.debug("delegating to setReceiver(String)");
        setReceiver(tos.toString());
        log.trace("Leaving " + MailBackend.class.getSimpleName()
                + ".setReceiver(List<String>)");
    }

    /**
     * Sets the current list of receivers of this Email.
     * 
     * @param to
     *            A comma separated list of recipients.
     */
    public void setReceiver(String to) {
        log.trace("Entering " + MailBackend.class.getSimpleName()
                + ".setReceiver(String)");
        if (to == null || to.trim().isEmpty()) {
            log.error("Can't use empty To: header.");
            throw new IllegalArgumentException(
                    "Please specify some recipients!");
        }
        if (!validateAddresses(to)) {
            log.error("The following string is not a valid value "
                    + "for the \"To:\" header: " + to);
            throw new IllegalArgumentException(
                    "One or more of the specified recipients are formatted incorrectly!");
        }

        log.debug("setting To header");
        headerTo = to;
        log.trace("Leaving " + MailBackend.class.getSimpleName()
                + ".setReceiver(String)");
    }

    /**
     * Specify the sender of the Email.
     * 
     * @param fromName
     *            The name of the sender of this Email (may be <code>null</code>
     *            or empty).
     * @param fromMail
     *            The email address of sender of this Email.
     */
    public void setSender(String fromName, String fromMail) {
        log.trace("Entering " + MailBackend.class.getSimpleName()
                + ".setSender(String)");
        if (fromMail == null || fromMail.trim().isEmpty()
                || !validateAddresses(fromMail)) {
            log.error("The sender can't be empty or invalid.");
            throw new IllegalArgumentException("Please specify a valid sender!");
        }

        log.debug("setting From");
        headerFromMail = fromMail;
        if (fromName == null || fromName.trim().isEmpty()) {
            log.debug("sender name is empty");
            headerFromName = null;
        } else {
            headerFromName = fromName;
        }
        log.trace("Leaving " + MailBackend.class.getSimpleName()
                + ".setSender(String)");
    }

    /**
     * Specify the hostname and port the MTA is listening on.
     * 
     * @param hostname
     *            The hostname of the MTA. May also be an IP address.
     * @param portNum
     *            The port the MTA is listening on. <code>-1</code> specifies
     *            the protocol dependent default port.
     */
    public void setServerInfo(String hostname, int portNum) {
        log.trace("Entering " + MailBackend.class.getSimpleName()
                + ".setServerInfo(String, int)");
        setHostname(hostname);
        setPortNum(portNum);
        log.trace("Leaving " + MailBackend.class.getSimpleName()
                + ".setServerInfo(String, int)");
    }

    /**
     * Specify the subject of the Email.
     * 
     * @param subject
     *            The subject of the Email. If <code>null</code>, the subject
     *            will be set to <code>{@link #EMPTY_SUBJECT}</code> string.
     */
    public void setSubject(String subject) {
        log.trace("Entering " + MailBackend.class.getSimpleName()
                + ".setSubject(String)");
        if (subject == null || subject.trim().isEmpty()) {
            log.debug("null parameter => setting to default");
            headerSubject = EMPTY_SUBJECT;
        } else {
            log.debug("setting subject to " + subject);
            headerSubject = subject;
        }
        log.trace("Leaving " + MailBackend.class.getSimpleName()
                + ".setSubject(String)");
    }

    /**
     * Set the username needed for authentification.
     * 
     * @param username
     *            The username used for authentification. If <code>null</code>,
     *            authentification will not be used.
     */
    public void setUsername(String username) {
        log.trace("Entering " + MailBackend.class.getSimpleName()
                + ".setUsername(String)");
        log.debug("setting username");
        this.username = username;
        log.trace("Leaving " + MailBackend.class.getSimpleName()
                + ".setUsername(String)");
    }

    /**
     * Sets the &quot;<code>User-Agent</code>&quot; header which indicates the
     * mail client sending this Email.
     * 
     * @param newXMailer
     *            The name of the sending client or whatever you want to put
     *            into the &quot;<code>User-Agent</code>&quot; header.
     */
    public void setXMailer(String newXMailer) {
        log.trace("Entering " + MailBackend.class.getSimpleName()
                + ".setXMailer(String)");
        log.debug("setting user agent");
        headerXMailer = newXMailer;
        log.trace("Leaving " + MailBackend.class.getSimpleName()
                + ".setXMailer(String)");
    }

    /**
     * Specify whether to use STARTTLS or not.
     * 
     * @param use
     *            If <code>true</code>, STARTTLS and SSL encryption will be
     *            used.
     */
    public void useTLS(boolean use) {
        log.trace("Entering " + MailBackend.class.getSimpleName()
                + ".useTLS(boolean)");
        log.debug("setting transport protocol encryption to " + use);
        useTLS = use;
        log.trace("Leaving " + MailBackend.class.getSimpleName()
                + ".useTLS(boolean)");
    }
}