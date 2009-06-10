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

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
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
// RR: add logging
public class MailBackend {

    Logger log;

    /**
     * Uses {@link #validateAddresses(String)} to verify that a list of e-mail
     * addresses are formatted correctly.
     * 
     * @param addressList
     *            A list of e-mail addresses.
     * @return <code>true</code> - if the list parses correctly<br>
     *         <code>false</code> - otherwise
     */
    public static boolean validateAddresses(List<String> addressList) {
        StringBuilder addresses = new StringBuilder(addressList.size() * 20);

        for (String recipient : addressList) {
            addresses.append(recipient);
            addresses.append(", ");
        }
        addresses.delete(addresses.length() - 2, addresses.length());
        DefaultLogger.getLogger(MailBackend.class).debug(
                "checking constructed address list: " + addresses);

        return validateAddresses(addresses.toString());
    }

    /**
     * Uses {@link InternetAddress#parse(String, boolean)
     * InternetAddress.parse(String, true)} to verify that a list of e-mail
     * addresses are formatted correctly.<br>
     * Empty address strings are considered invalid.
     * 
     * @param addressList
     *            A comma separated list of e-mail addresses.
     * @return <code>true</code> - if the list parses correctly<br>
     *         <code>false</code> - otherwise
     */
    public static boolean validateAddresses(String addressList) {
        Logger log = DefaultLogger.getLogger(MailBackend.class);
        if (addressList == null || addressList.trim().isEmpty()) {
            log.debug("Passed address list is empty => invalid");
            return false;
        }

        try {
            InternetAddress.parse(addressList, true);
        } catch (AddressException e) {
            log.debug("AddressException in InternetAddress.parse()"
                    + " => invalid", e);
            return false;
        }
        
        log.debug("No error and not empty => assuming correct format");
        return true;
    }

    /**
     * A map containing additional headers and their contents.
     */
    private Map<String, String> additionalHeaderMap = new HashMap<String, String>();

    /**
     * Main header <code>{@link String strings}</code>.
     */
    private String headerTo, headerFrom, headerSubject, headerCC, headerBCC,
            headerXMailer;

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
     * authenticate.
     */
    private String username, password;

    /**
     * Whether to use TLS for the mail transfer. Defaults to <code>false</code>.
     */
    private boolean useTLS = false;

    /**
     * @param to
     *            The recipients of the Email as a comma separated list of
     *            addresses.
     * @param from
     *            The sender of the Email. May be overwritten by the mail
     *            transport agent.
     * @param subject
     *            The subject of the Email. If <code>null</code> or empty, this
     *            will be set to <code>&lt;no subject&gt;</code>.
     */
    public MailBackend(String to, String from, String subject) {
        setReceiver(to);
        setSender(from);
        setSubject(subject);
        log = DefaultLogger.getLogger(this.getClass());
    }

    /**
     * Adds a list of receivers to the current list of secret receivers of
     * carbon copies.
     * 
     * @param bcc
     *            A comma separated list of recipients.
     */
    public void addBCC(String bcc) {
        if (bcc == null || bcc.trim().isEmpty())
            return;
        if (!validateAddresses(bcc)) {
            log.error("The following string is not a valid value "
                    + "for the \"BCC:\" header: " + bcc);
            throw new IllegalArgumentException(
                    "One or more of the specified recipients are formatted incorrectly!");
        }

        log.debug("Appending \"" + bcc + "\" to BCC header.");
        headerBCC += ", " + bcc;
    }

    /**
     * Adds a list of receivers to the current list of receivers of carbon
     * copies.
     * 
     * @param cc
     *            A comma separated list of recipients.
     */
    public void addCC(String cc) {
        if (cc == null || cc.trim().isEmpty())
            return;
        if (!validateAddresses(cc)) {
            log.error("The following string is not a valid value "
                    + "for the \"CC:\" header: " + cc);
            throw new IllegalArgumentException(
                    "One or more of the specified recipients are formatted incorrectly!");
        }

        log.debug("Appending \"" + cc + "\" to CC header.");
        headerCC += ", " + cc;
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
     */
    public void addFile(File file) throws MalformedURLException,
            MessagingException, IOException {
        addFile(file.toURI());
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
     */
    public void addFile(URI file) throws MalformedURLException,
            MessagingException, IOException {
        addFile(file.toURL());
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
     */
    public void addFile(URL file) throws MessagingException, IOException {
        addMimePart(new MimeBodyPart(file.openStream()));
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
            log.warn("The From: header should not be set using the "
                    + "addHeader() method. Delegating to setSender()...");
            setSender(headerContent);
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
        log.debug("Stepping through headers Map and "
                + "delegating to addHeader(String, String)...");
        for (String headerName : headers.keySet()) {
            addHeader(headerName, headers.get(headerName));
        }
    }

    /**
     * Adds a MIME part to the message. Can be used to attach files and other
     * content.
     * 
     * @param part
     *            The <code>{@link MimeBodyPart}</code> to add at the end of the
     *            MIME parts list.
     */
    public void addMimePart(MimeBodyPart part) {
        messageParts.add(part);
    }

    /**
     * Adds a list MIME part to the message. Can be used to attach files and
     * other content.
     * 
     * @param parts
     *            The <code>{@link MimeBodyPart MimeBodyParts}</code> to add at
     *            the end of the MIME parts list.
     */
    public void addMimeParts(List<MimeBodyPart> parts) {
        messageParts.addAll(parts);
    }

    /**
     * Adds a list of receivers to the current list of receivers.
     * 
     * @param to
     *            A comma separated list of recipients.
     */
    public void addReceiver(String to) {
        if (to == null || to.trim().isEmpty())
            return;
        if (!validateAddresses(to)) {
            log.error("The following string is not a valid value "
                    + "for the \"To:\" header: " + to);
            throw new IllegalArgumentException(
                    "One or more of the specified recipients are formatted incorrectly!");
        }

        headerTo += ", " + to;
    }

    /**
     * To be used for testing.
     * 
     * @return the additionalHeaderMap
     */
    final Map<String, String> getAdditionalHeaderMap() {
        return additionalHeaderMap;
    }

    /**
     * To be used for testing.
     * 
     * @return the headerBCC
     */
    final String getHeaderBCC() {
        return headerBCC;
    }

    /**
     * To be used for testing.
     * 
     * @return the headerCC
     */
    final String getHeaderCC() {
        return headerCC;
    }

    /**
     * To be used for testing.
     * 
     * @return the headerFrom
     */
    final String getHeaderFrom() {
        return headerFrom;
    }

    /**
     * To be used for testing.
     * 
     * @return the headerSubject
     */
    final String getHeaderSubject() {
        return headerSubject;
    }

    /**
     * To be used for testing.
     * 
     * @return the headerTo
     */
    final String getHeaderTo() {
        return headerTo;
    }

    /**
     * To be used for testing.
     * 
     * @return the headerXMailer
     */
    final String getHeaderXMailer() {
        return headerXMailer;
    }

    /**
     * To be used for testing.
     * 
     * @return the messageParts
     */
    final List<MimeBodyPart> getMessageParts() {
        return messageParts;
    }

    /**
     * To be used for testing.
     * 
     * @return the password
     */
    final String getPassword() {
        return password;
    }

    /**
     * To be used for testing.
     * 
     * @return the sMTPPortNum
     */
    final int getSMTPPortNum() {
        return SMTPPortNum;
    }

    /**
     * To be used for testing.
     * 
     * @return the sMTPServerHost
     */
    final String getSMTPServerHost() {
        return SMTPServerHost;
    }

    /**
     * To be used for testing.
     * 
     * @return the username
     */
    final String getUsername() {
        return username;
    }

    /**
     * To be used for testing.
     * 
     * @return the useTLS
     */
    final boolean isUseTLS() {
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
        additionalHeaderMap.remove(headerName);
    }

    /**
     * Has the same effect as calling <code>{@link #removeHeader(String)}</code>
     * for every item in the list.
     * 
     * @param headers
     *            A <code>{@link List}</code> of headers to remove.
     */
    public void removeHeaders(List<String> headers) {
        for (String header : headers) {
            additionalHeaderMap.remove(header);
        }
    }

    /**
     * Removes a <code>{@link MimeBodyPart}</code> from the MIME parts list.
     * 
     * @param part
     *            The <code>{@link MimeBodyPart}</code> to remove.
     */
    public void removeMimePart(MimeBodyPart part) {
        messageParts.remove(part);
    }

    /**
     * Removes a list of <code>{@link MimeBodyPart MimeBodyParts}</code> from
     * the mime parts list.
     * 
     * @param parts
     *            The <code>{@link MimeBodyPart MimeBodyParts}</code> to remove.
     */
    public void removeMimeParts(List<MimeBodyPart> parts) {
        messageParts.removeAll(parts);
    }

    /**
     * Send the message using the data specified by the user.
     */
    public void sendMessage() throws AddressException, MessagingException,
            IOException {
        if (SMTPServerHost == null)
            SMTPServerHost = "localhost";
        String protocol;
        if (useTLS)
            protocol = "smtps";
        else
            protocol = "smtp";

        Properties sysProps = System.getProperties();
        sysProps.setProperty("mail." + protocol + ".host", SMTPServerHost);
        if (username != null && !username.isEmpty()) {
            sysProps.setProperty("mail." + protocol + ".auth", "true");
        }
        Session session = Session.getInstance(sysProps, null);

        MimeMessage message = new MimeMessage(session);

        if (messageParts.size() == 0) {
            throw new IllegalArgumentException(
                    "You need to add some content to your message.");
        } else if (messageParts.size() == 1
                && messageParts.get(0).getContent() instanceof String) {
            message.setText((String) (messageParts.get(0).getContent()));
        } else {
            MimeMultipart multipartMessage = new MimeMultipart();
            for (MimeBodyPart part : messageParts) {
                multipartMessage.addBodyPart(part);
            }
            message.setContent(multipartMessage);
        }

        if (headerXMailer != null)
            message.setHeader("User-Agent", headerXMailer);
        if (additionalHeaderMap != null) {
            for (String header : additionalHeaderMap.keySet()) {
                message.addHeader(header, additionalHeaderMap.get(header));
            }
        }

        if (headerTo == null || headerTo.isEmpty())
            throw new IllegalArgumentException(
                    "You need to specify a receiver!");
        if (headerFrom == null || headerFrom.isEmpty())
            throw new IllegalArgumentException("You need to specify a sender!");
        if (headerSubject == null || headerSubject.isEmpty())
            headerSubject = "<no subject>";

        message.setFrom(new InternetAddress(headerFrom));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(
                headerTo, false));
        if (headerCC != null)
            message.setRecipients(Message.RecipientType.CC, InternetAddress
                    .parse(headerCC, false));
        if (headerBCC != null)
            message.setRecipients(Message.RecipientType.BCC, InternetAddress
                    .parse(headerBCC, false));
        message.setSubject(headerSubject);
        message.setSentDate(new Date());

        SMTPTransport transport = (SMTPTransport) session
                .getTransport(protocol);
        try {
            if (username != null && !username.isEmpty())
                transport.connect(SMTPServerHost, SMTPPortNum, username,
                        password);
            else
                transport.connect();
            transport.sendMessage(message, message.getAllRecipients());
        } finally {
            transport.close();
        }
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
        setUsername(username);
        setPassword(password);
    }

    /**
     * Sets the current list of secret receivers of carbon copies of this Email.
     * 
     * @param bcc
     *            A list of recipients.
     */
    public void setBCC(List<String> bcc) {
        StringBuilder bccs = new StringBuilder(bcc.size() * 20);

        for (String recipient : bcc) {
            bccs.append(recipient);
            bccs.append(", ");
        }
        bccs.delete(bccs.length() - 2, bccs.length());

        setBCC(bccs.toString());
    }

    /**
     * Sets the current list of secret receivers of carbon copies of this Email.
     * 
     * @param bcc
     *            A comma separated list of recipients.
     */
    public void setBCC(String bcc) {
        if (bcc == null || bcc.trim().isEmpty()) {
            headerBCC = "";
            return;
        }

        if (!validateAddresses(bcc)) {
            log.error("The following string is not a valid value "
                    + "for the \"BCC:\" header: " + bcc);
            throw new IllegalArgumentException(
                    "One or more of the specified recipients are formatted incorrectly!");
        }

        headerBCC = bcc;
    }

    /**
     * Set the string content of the main body part.
     * 
     * @param message
     *            The primary text message of the Email.
     */
    public void setBodyText(String message) throws MessagingException,
            IOException {
        // add a new part if the first on is not a string or there are no parts
        if (messageParts.isEmpty()
                || !(messageParts.get(0).getContent() instanceof String)) {
            messageParts.add(0, new MimeBodyPart());
        }

        messageParts.get(0).setText(message);
    }

    /**
     * Sets the current list of receivers of carbon copies of this Email.
     * 
     * @param cc
     *            A list of recipients.
     */
    public void setCC(List<String> cc) {
        StringBuilder ccs = new StringBuilder(cc.size() * 20);

        for (String recipient : cc) {
            ccs.append(recipient);
            ccs.append(", ");
        }
        ccs.delete(ccs.length() - 2, ccs.length());

        setCC(ccs.toString());
    }

    /**
     * Sets the current list of receivers of carbon copies of this Email.
     * 
     * @param cc
     *            A comma separated list of recipients.
     */
    public void setCC(String cc) {
        if (cc == null || cc.trim().isEmpty()) {
            headerBCC = "";
            return;
        }

        if (!validateAddresses(cc)) {
            log.error("The following string is not a valid value "
                    + "for the \"CC:\" header: " + cc);
            throw new IllegalArgumentException(
                    "One or more of the specified recipients are formatted incorrectly!");
        }

        headerCC = cc;
    }

    /**
     * Specify the host the MTA is running on.
     * 
     * @param hostname
     *            The hostname of the MTA. May also be an IP address.
     */
    public void setHostname(String hostname) {
        SMTPServerHost = hostname;
    }

    /**
     * Set the password needed for authentification.
     * 
     * @param password
     *            The password used for authentification.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Specify the port the MTA is listening on.
     * 
     * @param portNum
     *            The port the MTA is listening on. <code>-1</code> specifies
     *            the protocol dependent default port.
     */
    public void setPortNum(int portNum) {
        SMTPPortNum = portNum;
    }

    /**
     * Sets the current list of receivers of this Email.
     * 
     * @param to
     *            A list of recipients.
     */
    public void setReceiver(List<String> to) {
        StringBuilder tos = new StringBuilder(to.size() * 20);

        for (String recipient : to) {
            tos.append(recipient);
            tos.append(", ");
        }
        tos.delete(tos.length() - 2, tos.length());

        setReceiver(tos.toString());
    }

    /**
     * Sets the current list of receivers of this Email.
     * 
     * @param to
     *            A comma separated list of recipients.
     */
    public void setReceiver(String to) {
        if (to == null || to.trim().isEmpty())
            throw new IllegalArgumentException(
                    "Please specify some recipients!");
        if (!validateAddresses(to)) {
            log.error("The following string is not a valid value "
                    + "for the \"To:\" header: " + to);
            throw new IllegalArgumentException(
                    "One or more of the specified recipients are formatted incorrectly!");
        }

        headerTo = to;
    }

    /**
     * Specify the sender of the Email.
     * 
     * @param from
     *            The sender of this Email.
     */
    public void setSender(String from) {
        if (from == null || from.isEmpty())
            throw new IllegalArgumentException("Please specify a sender!");
        this.headerFrom = from;
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
        setHostname(hostname);
        setPortNum(portNum);
    }

    /**
     * Specify the subject of the Email.
     * 
     * @param subject
     *            The subject of the Email. If <code>null</code>, the subject
     *            will be set to the empty string.
     */
    public void setSubject(String subject) {
        if (subject == null || subject.isEmpty())
            this.headerSubject = "";
        else
            this.headerSubject = subject;
    }

    /**
     * Set the username needed for authentification.
     * 
     * @param username
     *            The username used for authentification. If <code>null</code>,
     *            authentification will not be used.
     */
    public void setUsername(String username) {
        this.username = username;
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
        headerXMailer = newXMailer;
    }

    /**
     * Specify whether to use STARTTLS or not.
     * 
     * @param use
     *            If <code>true</code>, STARTTLS and SSL encryption will be
     *            used.
     */
    public void useTLS(boolean use) {
        useTLS = use;
    }
}
