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

import com.sun.mail.smtp.SMTPTransport;

/**
 * JavaMail demo class for the DecidR prototype.
 * 
 * @author RR
 */
public class MailBackend {

	/**
	 * Header <code>{@link String strings}</code>.
	 */
	private String headerTo, headerFrom, headerSubject, headerCC, headerBCC,
			headerXMailer;

	/**
	 * A map containing additional headers and their contents.
	 */
	private Map<String, String> additionalHeaderMap = new HashMap<String, String>();

	/**
	 * The hostname of the SMTP server. Defaults to localhost.
	 */
	private String SMTPServerHost = "localhost";

	/**
	 * The port numer of the SMTP server. Defaults to whatever the protocol
	 * specifies.
	 */
	private int SMTPPortNum = -1;

	/**
	 * The authentification information. If unset, the server is assumed not to
	 * authenticate.
	 */
	private String username, password;

	/**
	 * List of parts. If there are more than one, a multipart message is
	 * constructed.
	 */
	private List<MimeBodyPart> messageParts = new Vector<MimeBodyPart>();

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
	public MailBackend(String to, String from, String subject)
			throws MessagingException {
		super();
		// System.out.println("JavaMail version: "
		// + Message.class.getPackage().getImplementationVersion());
		this.headerTo = to;
		this.headerFrom = from;
		this.headerSubject = subject;

		if (to == null || to.isEmpty())
			throw new IllegalArgumentException(
					"Please specify some recipients!");
		if (from == null || from.isEmpty())
			throw new IllegalArgumentException("Please specify a sender!");

		if (subject == null || subject.isEmpty())
			this.headerSubject = "<no subject>";
	}

	/**
	 * Set the string content of the main body part.
	 * 
	 * @param message
	 *            The primary text message of the Email.
	 */
	public void setBodyText(String message) throws MessagingException,
			IOException {
		if (messageParts.isEmpty()
				|| !(messageParts.get(0).getContent() instanceof String)) {
			messageParts.add(0, new MimeBodyPart());
		}

		messageParts.get(0).setText(message);
	}

	/**
	 * Adds a mime part to the message. Can be used to attach files and other
	 * content.
	 * 
	 * @param part
	 *            The <code>{@link MimeBodyPart}</code> to add at the end of the
	 *            mime parts list.
	 */
	public void addMimePart(MimeBodyPart part) {
		messageParts.add(part);
	}

	/**
	 * Adds a list mime part to the message. Can be used to attach files and
	 * other content.
	 * 
	 * @param parts
	 *            The <code>{@link MimeBodyPart MimeBodyParts}</code> to add at
	 *            the end of the mime parts list.
	 */
	public void addMimeParts(List<MimeBodyPart> parts) {
		messageParts.addAll(parts);
	}

	/**
	 * Removes a <code>{@link MimeBodyPart}</code> from the mime parts list.
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
	 * Adds an arbitrary header name-value tuple to the headers of the message.
	 * Overwrites any existing existing header of the same name, if necessary.
	 * The header may be ignored or overwritten by the mail transport agent. <br>
	 * <br>
	 * <em>WARNING:</em><br>
	 * This method can be used to bypass some error checking done by this class!<br>
	 * DO NOT ABUSE!
	 * 
	 * @param headerName
	 *            The name of the header to set.
	 * @param headerContent
	 *            The value of the header specified by <code>headerName</code>.
	 */
	public void addHeader(String headerName, String headerContent) {
		additionalHeaderMap.put(headerName, headerContent);
	}

	/**
	 * Has the same effect as calling
	 * <code>{@link #addHeader(String, String)}</code> for every item in the
	 * map.<br>
	 * <br>
	 * <em>WARNING:</em><br>
	 * This method can be used to bypass some error checking done by this class!<br>
	 * DO NOT ABUSE!
	 * 
	 * @param headers
	 *            A <code>{@link Map}</code> of headers to set.
	 */
	public void addHeaders(Map<String, String> headers) {
		additionalHeaderMap.putAll(headers);
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
	 * Specify the subject of the Email.
	 * 
	 * @param subject
	 *            The subject of the Email. If <code>null</code> or empty, this
	 *            will be set to <code>&lt;no subject&gt;</code>.
	 */
	public void setSubject(String subject) {
		if (subject == null || subject.isEmpty())
			this.headerSubject = "<no subject>";
		this.headerSubject = subject;
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
	 * Sets the current list of receivers of this Email.
	 * 
	 * @param to
	 *            A comma separated list of recipients.
	 */
	public void setReceiver(String to) {
		if (to == null || to.isEmpty())
			throw new IllegalArgumentException(
					"Please specify some recipients!");
		headerTo = to;
	}

	/**
	 * Adds a list of receivers to the current list of receivers.
	 * 
	 * @param to
	 *            A comma separated list of recipients.
	 */
	public void addReceiver(String to) {
		if (to == null || to.isEmpty())
			return;
		headerTo += ", " + to;
	}

	/**
	 * Sets the current list of receivers of carbon copies of this Email.
	 * 
	 * @param cc
	 *            A comma separated list of recipients.
	 */
	public void setCC(String cc) {
		headerCC = cc;
	}

	/**
	 * Adds a list of receivers to the current list of receivers of carbon
	 * copies.
	 * 
	 * @param cc
	 *            A comma separated list of recipients.
	 */
	public void addCC(String cc) {
		if (cc == null || cc.isEmpty())
			return;
		headerCC += ", " + cc;
	}

	/**
	 * Sets the current list of secret receivers of carbon copies of this Email.
	 * 
	 * @param bcc
	 *            A comma separated list of recipients.
	 */
	public void setBCC(String bcc) {
		headerBCC = bcc;
	}

	/**
	 * Adds a list of receivers to the current list of secret receivers of
	 * carbon copies.
	 * 
	 * @param bcc
	 *            A comma separated list of recipients.
	 */
	public void addBCC(String bcc) {
		if (bcc == null || bcc.isEmpty())
			return;
		headerBCC += ", " + bcc;
	}

	/**
	 * Sets the &quot;<code>X-Mailer</code>&quot; header which indicates the
	 * mail client sending this Email.
	 * 
	 * @param newXMailer
	 *            The name of the sending client or whatever you want to put
	 *            into the &quot;<code>X-Mailer</code>&quot; header.
	 */
	public void setXMailer(String newXMailer) {
		headerXMailer = newXMailer;
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
		SMTPServerHost = hostname;
		SMTPPortNum = portNum;
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
	 * Set the username and password needed for authentification.
	 * 
	 * @param username
	 *            The username used for authentification. If <code>null</code>,
	 *            authentification will not be used.
	 * @param password
	 *            The password used for authentification.
	 */
	public void setAuthInfo(String username, String password) {
		this.username = username;
		this.password = password;
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
	 * Set the password needed for authentification.
	 * 
	 * @param password
	 *            The password used for authentification.
	 */
	public void setPassword(String password) {
		this.password = password;
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
		// session.setDebug(true);

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
			message.setHeader("X-Mailer", headerXMailer);
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
}
